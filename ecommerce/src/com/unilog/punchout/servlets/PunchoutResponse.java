package com.unilog.punchout.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PunchoutResponse
 */
public class PunchoutResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PunchoutResponse() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String punchoutUrl = request.getParameter("cxmlParamHOOK_URL");
		String outputDoc = ProcessPunchoutResponse.getInstance().processResponse(request);
		request.setAttribute("punchoutUrl" , punchoutUrl);
		request.setAttribute("outputDoc" , outputDoc);
		request.getRequestDispatcher("punchoutOrderMessage.action").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
