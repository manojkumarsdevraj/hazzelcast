package com.unilog.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.unilog.database.CommonDBQuery;

/**
 * Servlet implementation class UploadFileToServer
 */
public class UploadFileToServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public UploadFileToServer() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		MultipartRequest multi = new MultipartRequest(request, ".", 1024 * 1024 * 1024);
		Enumeration files = multi.getFileNames();
		String savedFileName = null;
		String fileName = "";
		String filePathToDelete = "";
		try{
			while(files.hasMoreElements()) {
				String name = (String) files.nextElement();
				File fileToUpload = multi.getFile(name);
				fileName = multi.getFilesystemName(name);
				savedFileName = sessionId+"_"+fileName  ;
				String folder = CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH");
				String saveFile = CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH")+"/"+savedFileName;
				filePathToDelete =  CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH")+"/"+savedFileName;
				String path = folder.trim();
				File destination = new File(path);
				if(!destination.exists()){
					System.out.println(path+" : destination dir doesnt exists");
					destination.mkdir();
				}
				if(copyFile(fileToUpload, new File(saveFile), session) == true){
					try{
						System.out.println("Copied");
						out.print(savedFileName);
					}catch(Exception exec){
						exec.printStackTrace();
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

	private boolean copyFile(File from, File to, HttpSession session) {
		try {
			FileInputStream fileInStream = new FileInputStream(from);
			FileOutputStream fileOutStream = new FileOutputStream(to);
			boolean emptyFile = true;
			byte buf[] = new byte[1024];
			int i = 0;

			while((i = fileInStream.read(buf)) != -1) {
				emptyFile = false;
				fileOutStream.write(buf, 0, i);
			}
			fileInStream.close();
			fileOutStream.close();
			if(emptyFile == true) {
				to.delete();
				return false;
			}
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static void deletefile(String file){
		File f1 = new File(file);
		boolean success = f1.delete();
		if (!success){
			System.out.println("PartList Excel Deletion failed.");
		}else{
			System.out.println("PartList Excel File deleted.");
		}
	}
	
	private String getDate(){
		String dateString="";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss");
			Date date = new Date();
			dateString = dateFormat.format(date);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		    // formatting
		    System.out.println(dateString);
		    return dateString;
	}
}
