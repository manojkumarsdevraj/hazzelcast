package com.unilog.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CIMMTouchUtility;
/**
 * Servlet implementation class MobileAppMultiPartMail
 */
public class MobileAppMultiPartMail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileAppMultiPartMail() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	 public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	  {
		 System.out.println("--- BEGIN DOPOST ---");
		 response.setContentType("application/json");
		 response.setHeader("Cache-Control", "no-cache");
		 PrintWriter out = response.getWriter();

		 try {
			 System.out.println("Path : " + CommonDBQuery.getSystemParamtersList().get("APP_ATTACHMENT_FILE_PATH")); 
			
			    	 DiskFileUpload upload = new DiskFileUpload();
			         
			         List items = upload.parseRequest(request);

			         Map fields = new HashMap();

			         Iterator iter = items.iterator();
			         while (iter.hasNext()) {
			           FileItem item = (FileItem)iter.next();
			           if (item.isFormField())
			             fields.put(item.getFieldName(), item.getString());
			           else
			             fields.put(item.getFieldName(), item);
			         }
			        
			         String virtualPath = CommonDBQuery.getSystemParamtersList().get("APP_ATTACHMENT_FILE_PATH");
			         System.out.println("Virtual path for app attachment : " + virtualPath);

			       
			        
			         FileItem uplFile = (FileItem)fields.get("file");
			         String name = (String) fields.get("Name");
			         String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			         String emailFrom = (String) fields.get("From");
			         String toEmail = (String) fields.get("email");
			         String message = (String) fields.get("message");
			         String subject = (String) fields.get("Subject");
			         StringBuilder messageBuilder = new StringBuilder();
			         messageBuilder.append(message).append("<br /><br />").append("Name : ").append(name).append("<br />Email : ").append(emailFrom);
			         
			         String fileNameLong = uplFile.getName();
			         fileNameLong = fileNameLong.replace('\\', '/');
			         String[] pathParts = fileNameLong.split("/");
			         String fileName = pathParts[(pathParts.length - 1)];
			         System.out.println(virtualPath+fileName);
			         File pathToSave = new File(virtualPath, fileName);
			         uplFile.write(pathToSave);
			         out.print("{ \"path\": \""+virtualPath+fileName+"\" }");
			         out.flush();
			         out.close();
			         CIMMTouchUtility.getInstance().generateAndSendEmail(fromEmail, toEmail, emailFrom, subject, messageBuilder.toString(), virtualPath+fileName);
	    } catch (Exception ex) {
			out.print("{ \"error\": \"Authentication Required.\" }");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		  	 ex.printStackTrace();
		   
		    }
	    

	     System.out.println("--- END DOPOST ---");
	  }
	 
	  
	  private static String getWarFileName(String path){
			
			String str[] = path.split("/");
			String warFile = "";
			if(str.length > 1){
				warFile = "/" + str[1] + ".war";
			}
			warFile = warFile + "/";
			for(int i=2; i<str.length; i++){
				
				warFile = warFile + str[i] + "/";
			}
			
			return warFile;
		}

}
