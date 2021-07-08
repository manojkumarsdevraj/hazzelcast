package com.unilognew.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.unilog.punchout.jaxb.CXML;
import com.unilog.punchout.jaxb.ObjectFactory;
import com.unilog.punchout.utility.XmlToObjectConverter;

/**
 * Servlet implementation class PunchoutCartServlet
 */
public class PunchoutCartTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PunchoutCartTestServlet() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String reqType = response.getContentType();
		response.setContentType(reqType);
		PrintStream outputStream = new PrintStream(response.getOutputStream());
		String requestStr = getRequest(request);
		
		requestStr = URLDecoder.decode(requestStr,"UTF-8");
		
		int reqLen = requestStr.length();
		try {
			 if(reqLen>0) {
			     
		          	HttpSession session = request.getSession();
		          	Object xmlStringToObject = XmlToObjectConverter.convertXmlStringToJavaObject(requestStr);
			        ObjectFactory obj = new ObjectFactory();
			        CXML cxml = obj.createCXML();
			        cxml = (CXML) xmlStringToObject;
			        outputStream.print(cxml);
			    }
		}
		catch (Exception e) {
			e.printStackTrace();
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
        InputStream in=req.getInputStream();
        StringBuffer xmlStr=new StringBuffer();
           int d;
          while((d=in.read()) != -1){
        	  xmlStr.append((char)d);
          }
      System.out.println("xmlStr : -- "+xmlStr.toString());
          return xmlStr.toString();
         }
        catch (IOException e) {
           System.out.println("File I/O error:");
           return "";
        }
      }

}
