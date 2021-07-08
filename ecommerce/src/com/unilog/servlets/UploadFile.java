package com.unilog.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.unilog.database.CommonDBQuery;

public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		response.setContentType("text/html;charset=UTF-8");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		String filePath = "";
		String UPLOAD_DIRECTORY = CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH");
		// process only if it is multipart content
		if (isMultipart) {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				// Parse the request
				List<FileItem> multiparts = upload.parseRequest(request);
				List<String> uploadFiles = new ArrayList<String>();
				for (FileItem item : multiparts) {
					if (!item.isFormField()) {
						String name = session.getId() + "_" + new File(item.getName()).getName();
						filePath = UPLOAD_DIRECTORY +"/"+ name;
						
						item.write(new File(UPLOAD_DIRECTORY + File.separator + name));
						response.getWriter().print(filePath);
						System.out.println(filePath);
						uploadFiles.add(filePath);

					}
				}
				session.setAttribute("uploadedFiles", uploadFiles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}