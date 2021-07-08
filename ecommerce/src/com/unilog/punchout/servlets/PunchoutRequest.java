package com.unilog.punchout.servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.unilog.database.ConnectionManager;
import com.unilog.punchout.jaxb.CXML;
import com.unilog.punchout.jaxb.ObjectFactory;
import com.unilog.punchout.utility.XmlToObjectConverter;
import com.unilog.utility.CommonUtility;

/**
 * Servlet implementation class PunchoutRequest
 */
public class PunchoutRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PunchoutRequest() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String reqType = response.getContentType();
		if(CommonUtility.validateString(reqType).length()>0){
			response.setContentType(reqType);
		}else{
			response.setContentType("text/xml");
		}

		PrintStream outputStream = new PrintStream(response.getOutputStream());
		String requestStr = getRequest(request);
		//String requestStr = getRequestFromFile(request);
		int reqLen = requestStr.length();
		try{
			 if(reqLen>0) {
			          	HttpSession session = request.getSession();
			          	Object xmlStringToObject = XmlToObjectConverter.convertXmlStringToJavaObject(requestStr);
				        ObjectFactory obj = new ObjectFactory();
				        CXML cxml = obj.createCXML();
				        cxml = (CXML) xmlStringToObject;
			    		ProcessPunchoutRequest.getInstance().processRequest(cxml,outputStream,session.getId());
			          	//parseResp.read(inpNew,out,logId);
			    }else{
			    	getServletContext().getRequestDispatcher("/ocipunchout.action").forward(request, response);
			    }
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(outputStream!=null) {
				outputStream.close();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public String getRequest(HttpServletRequest req) {
		try {
			System.out.println("Inside getRequest"); 
			InputStream in=req.getInputStream();
			//System.out.println(req.getParameter("username")); 
			StringBuffer xmlStr=new StringBuffer();
			int d;
			while((d=in.read()) != -1){
				xmlStr.append((char)d);
			}
			System.out.println("xmlStr : -- "+xmlStr.toString());
			return xmlStr.toString();
		}catch (IOException e) {
			System.out.println("File I/O error:");
			return "";
		}
	}
	
	public String getRequestFromFile(HttpServletRequest req) {
		BufferedReader br = null;
		FileReader fileReader = null;
		try {
			StringBuffer xmlStr = new StringBuffer();
			String sCurrentLine = "";
			fileReader = new FileReader("D:\\PunchoutRequest\\punchout.xml");
			br = new BufferedReader(fileReader);
			while ((sCurrentLine = br.readLine()) != null) {
				xmlStr.append(sCurrentLine);
			}
			ConnectionManager.closeFileReader(fileReader);
			System.out.println("xmlStr : -- "+xmlStr.toString());
			return xmlStr.toString();
		}catch (IOException e) {
			System.out.println("File I/O error:");
			return "";
		}finally {
			if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fileReader!=null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
