package com.unilog.ecomm.promotion;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SalesPromotionSynchService extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sLoadModule = request.getParameter("loadKieModule");
		if(sLoadModule == null || sLoadModule.trim().isEmpty()){
			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("<html><body><p> Bad request. </p></body></html>");

		}else {
			Boolean loadModule = null;
			
			try {
				loadModule = Boolean.parseBoolean(sLoadModule);
				if(loadModule==null) {
					loadModule = Boolean.FALSE;
				}
			} catch(Exception e) {
				loadModule = Boolean.FALSE;
			}
			//Load kie module parameter is not actually Required, Just we are checking to prevent anonymous requests	
			SalesPromotionService.getInstance().setLoadModule(loadModule);
			response.getWriter().print("<html><body><p>Module");
			if(!loadModule) {
				response.getWriter().print(" not");
			}
			response.getWriter().print(" reloaded</p></body></html>");
		}
	
	}

}
