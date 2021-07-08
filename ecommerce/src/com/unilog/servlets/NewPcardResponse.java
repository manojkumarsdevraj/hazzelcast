package com.unilog.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NewPcardResponse
 */
public class NewPcardResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewPcardResponse() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String transactionSetupID = "";
		String servicesID = "";
		String expressResponseCode = "";
		String expressResponseMessage = "";
		String paymentAccountID = "";
		String lastFour = "";
		//String validationCode = "";
		
		String hostedPaymentStatus = request.getParameter("HostedPaymentStatus");
		if(!hostedPaymentStatus.trim().equalsIgnoreCase("Cancelled"))
		{
			expressResponseMessage = request.getParameter("ExpressResponseMessage");
			transactionSetupID = request.getParameter("TransactionSetupID");
			servicesID = request.getParameter("ServicesID");
			expressResponseCode = request.getParameter("ExpressResponseCode");
			paymentAccountID = request.getParameter("PaymentAccountID");
			lastFour = request.getParameter("LastFour");
			//validationCode = request.getParameter("ValidationCode");
			
		}
		request.setAttribute("expressResponseCode", expressResponseCode);
		request.setAttribute("hostedPaymentStatus", hostedPaymentStatus);
		request.setAttribute("paymentAccountID", paymentAccountID);
		request.setAttribute("expressResponseMessage", expressResponseMessage);
		request.setAttribute("transactionSetupID", transactionSetupID);
		request.setAttribute("expressResponseCode", expressResponseCode);
		request.setAttribute("lastFour", lastFour);
		request.setAttribute("servicesID", servicesID);
		request.getRequestDispatcher("/saveCreditCardInfoSale.action").forward(request, response); 

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
		System.out.println("inside post");
	}

}
