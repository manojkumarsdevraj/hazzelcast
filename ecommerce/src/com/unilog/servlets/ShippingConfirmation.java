package com.unilog.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShippingConfirmation
 */
public class ShippingConfirmation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShippingConfirmation() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		handleRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		handleRequest(request, response);
	}
	
	
	public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException  {	 
        
        Enumeration<String> parameterNames = req.getParameterNames();
 
        while (parameterNames.hasMoreElements()) {
 
            String paramName = parameterNames.nextElement();
            System.out.println("paramName : " + paramName);
           
            String[] paramValues = req.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];

                System.out.println("paramValue : " + paramValue);
            }
 
        }
 
       
    }


}
