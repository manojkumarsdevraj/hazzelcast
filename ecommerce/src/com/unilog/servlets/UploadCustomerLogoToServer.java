package com.unilog.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oreilly.servlet.MultipartRequest;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;


public class UploadCustomerLogoToServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UploadCustomerLogoToServer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		int buyingCompanyId = CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString());
		MultipartRequest multi = new MultipartRequest(request, ".", 1024 * 1024 * 1024);
		Enumeration files = multi.getFileNames();
		String savedFileName = null;
		String fileName = "";
		try{
			if(buyingCompanyId>1){
				while(files.hasMoreElements()) {
					String name = (String) files.nextElement();
					File fileToUpload = multi.getFile(name);
					fileName = multi.getFilesystemName(name);
					savedFileName = CommonUtility.validateString(fileName);
					
					String folder = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("COMPANY_PROFILE_IMAGE_UPLOAD_DIRECTORY"));

					String saveFile = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("COMPANY_PROFILE_IMAGE_UPLOAD_DIRECTORY"))+"/"+buyingCompanyId + savedFileName;

					String path = CommonUtility.validateString(folder);
					File destination = new File(path);
					if(!destination.exists()){
						System.out.println(path+" : destination dir doesnt exists");
						destination.mkdir();
					}
					if(CommonUtility.copyFile(fileToUpload, new File(saveFile), session) == true){
						try{
							System.out.println("Profile Picture Copied");
							JsonObject jsonRes = new JsonObject();
							jsonRes.addProperty("fileName", savedFileName);
							jsonRes.addProperty("src", "customerLogo");
							out.print(new Gson().toJson(jsonRes));
							UsersDAO.updateCustomerLogoImagePath(savedFileName, buyingCompanyId);
							session.setAttribute("buyingCompanyLogo", savedFileName);
						}catch(Exception exec){
							exec.printStackTrace();
						}
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
