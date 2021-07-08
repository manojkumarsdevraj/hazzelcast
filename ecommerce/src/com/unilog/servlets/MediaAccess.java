package com.unilog.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

/**
 * Servlet implementation class MediaAccess
 */
public class MediaAccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MediaAccess() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = null;
		
		try {
			filename = request.getParameter("filename");
			String ext[] = null;
			String fileExt = "";
			boolean validExt = true;
			if(filename!=null){
				ext = filename.split("\\.");
				System.out.println(ext[ext.length-1]);
				fileExt = ext[ext.length-1];
			}
			
			if(fileExt!=null && (fileExt.equalsIgnoreCase("png") || fileExt.equalsIgnoreCase("jpg") || fileExt.equalsIgnoreCase("jpeg") || fileExt.equalsIgnoreCase("gif"))){
				response.setContentType("image/jpeg");  
			}else if(fileExt!=null && fileExt.equalsIgnoreCase("pdf")){
				response.setContentType("application/pdf");  
			}else if(fileExt!=null && fileExt.equalsIgnoreCase("docx")){
				response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"); 
			}else if(fileExt!=null && fileExt.equalsIgnoreCase("doc")){
				response.setContentType("application/msword"); 
			}else if(fileExt!=null && fileExt.equalsIgnoreCase("xls")){
				response.setContentType("application/vnd.ms-excel"); 
			}else if(fileExt!=null && fileExt.equalsIgnoreCase("xlsx")){
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); 
			}else{
				validExt = false;
			}
			
			if(validExt){
				ServletOutputStream out;  
			    out = response.getOutputStream();  
			    //CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SECURED_PORTAL_MEDIA_FILE_PATH"))
			    //FileInputStream fin = new FileInputStream("D:\\ws\\ECOMV2\\EcommerceRepository\\WEB_THEMES.war\\AdCsr\\images\\"+filename);
			    FileInputStream fin = new FileInputStream(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SECURED_PORTAL_MEDIA_FILE_PATH"))+filename);  
			    BufferedInputStream bin = new BufferedInputStream(fin);  
			    BufferedOutputStream bout = new BufferedOutputStream(out);  
			    int ch =0; ;  
			    while((ch=bin.read())!=-1)  
			    {  
			    bout.write(ch);  
			    }  
			      
			    bin.close();  
			    fin.close();  
			    bout.close();  
			    out.close();  
			}else{
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
			
			
		}catch (FileNotFoundException e) {
			
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			e.printStackTrace();
		}
		catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
