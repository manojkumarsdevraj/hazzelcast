package com.unilog.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;

import com.unilog.cmsmanagement.util.CMSConstants;
import com.unilog.database.CommonDBQuery;

/**
 * Servlet implementation class FontUpload
 */
public class FontUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FontUpload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try{
			String templatePath = CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH");// CimmResources.systemParameterNameValueMap.get("BANNER_TEMPLATE_PATH");
			String warFileName = CMSConstants.getWarFileName(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES"));
			String siteName = CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
			StringBuilder fontLocation = new StringBuilder();
			String fileName = request.getHeader("X-File-Name");
			fontLocation .append(templatePath).append(warFileName).append(siteName).append("/fonts/").append(fileName);
			
			InputStream inputStream = request.getInputStream();
			System.out.println(fileName);
			File targetFile = new File(fontLocation.toString());
			 
		  FileUtils.copyInputStreamToFile(inputStream, targetFile);
			/* DiskFileUpload upload = new DiskFileUpload();
	         
			 List items = upload.parseRequest(request);
	         Iterator iter = items.iterator();
	         while (iter.hasNext()) {
	             FileItem item = (FileItem) iter.next();

	             if (!item.isFormField()) {
	                 String fileName = new File(item.getName()).getName();
	                 String filePath = "d:\\csstest" + File.separator + fileName;
	                 System.out.println(filePath);
	                 File uploadedFile = new File(filePath);
	                 System.out.println(filePath);
	                 // saves the file to upload directory
	                 item.write(uploadedFile);
	             }
	         }*/
			}
			catch (Exception e) {
				e.printStackTrace();// TODO: handle exception
			}
			response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
