package com.unilog.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;


public class UploadProfileImageToServer extends HttpServlet {

	private static final long serialVersionUID = -1081473314966951829L;

	public UploadProfileImageToServer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		String userIdString = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(userIdString);
		MultipartRequest multi = new MultipartRequest(request, ".", 1024 * 1024 * 1024);
		Enumeration files = multi.getFileNames();
		String savedFileName = null;
		String fileName = "";
		boolean fileValidate=true;
		//String filePathToDelete = "";
		try{
			if(userId>1){
				while(files.hasMoreElements()) {
					String name = (String) files.nextElement();
					File fileToUpload = multi.getFile(name);
					fileName = multi.getFilesystemName(name);
					//savedFileName = sessionId+"_"+fileName  ;
					savedFileName = CommonUtility.validateString(fileName);
					/*String patthtemp = "D:\\VirtualFolder\\deploy\\ASSETS.WAR\\IMAGES\\USER\\DISPLAY_IMAGE";
					String folder = CommonUtility.validateString(patthtemp);
					String saveFile = CommonUtility.validateString(patthtemp)+"/"+savedFileName;*/
					///USER_PROFILE_THUMB_IMAGE_PATH
					String folder = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USER_PROFILE_IMAGE_UPLOAD_DIRECTORY"));
					String saveFile = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USER_PROFILE_IMAGE_UPLOAD_DIRECTORY"))+"/"+savedFileName;
					String path = CommonUtility.validateString(folder);
					String ExtnPattern="([^\\s]+(\\.(?i)(jpg|jpeg|png|pdf|gif|tif))$)";
					fileValidate= CommonUtility.validateUploadFileExtn(fileName, ExtnPattern);
					File destination = new File(path);
					if(!destination.exists()){
						System.out.println(path+" : destination dir doesnt exists");
						destination.mkdir();
					}
					if(CommonUtility.copyFile(fileToUpload, new File(saveFile), session) == true && fileValidate){
						try{
							System.out.println("Profile Picture Copied");
							out.print(savedFileName);
							UsersDAO.updateUserProfileImage(savedFileName, userId);
							session.setAttribute("userProfileImage", savedFileName);
						}catch(Exception exec){
							exec.printStackTrace();
						}
					}else {
						System.out.println("File format has to be either a gif, jpg, tif, png or pdf and File name cannot contain space or symbol (e.g. &, %, $ etc.)");
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
