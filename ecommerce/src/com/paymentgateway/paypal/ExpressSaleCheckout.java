package com.paymentgateway.paypal;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;


/**
 * Servlet implementation class ExpressSaleCheckout
 */
public class ExpressSaleCheckout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	
	private final String webAddress = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
	    public void doGet(HttpServletRequest request,
	                      HttpServletResponse response)
	        throws ServletException, IOException {
	
	        // Use "request" to read incoming HTTP headers (e.g. cookies)
	        // and HTML form data (e.g. data the user entered and submitted)
	
	        // Use "response" to specify the HTTP response line and headers
	        // (e.g. specifying the content type, setting cookies).
	
	        ///PrintWriter out = response.getWriter();
	        // Use "out" to send content to browser
	        ///  out.println("Hello World");
	
	
	        HttpSession session = request.getSession(true);
	
	        /*
	        '-------------------------------------------
	        ' The paymentAmount is the total value of
	        ' the shopping cart, that was set
	        ' earlier in a session variable
	        ' by the shopping cart page
	        '-------------------------------------------
	        */
	
	        //String paymentAmount = (String) session.getAttribute("Payment_Amount");
	        String paymentAmount = request.getParameter("amount");
	
	        /*
	        '------------------------------------
	        ' The returnURL is the location where buyers return to when a
	        ' payment has been succesfully authorized.
	        '
	        ' This is set to the value entered on the Integration Assistant
	        '------------------------------------
	        */
	
	        //String returnURL = "http://122.166.57.30/PayPalResponseSale.action";
	        String returnURL = webAddress+CommonUtility.validateString(request.getParameter("returnURL"));//"http://122.166.57.30/PayPalResponseSale.action"
	
	        /*
	        '------------------------------------
	        ' The cancelURL is the location buyers are sent to when they hit the
	        ' cancel button during authorization of payment during the PayPal flow
	        '
	        ' This is set to the value entered on the Integration Assistant
	        '------------------------------------
	        */
	        //String cancelURL = "http://122.166.57.30/Cart";
	        String cancelURL =  webAddress+CommonUtility.validateString(request.getParameter("cancelURL"));//"http://122.166.57.30/Cart";
	
	        /*
	        '------------------------------------
	        ' Calls the SetExpressCheckout API call
	        '
	        ' The CallShortcutExpressCheckout function is defined in the file PayPalFunctions.asp,
	        ' it is included at the top of this file.
	        '-------------------------------------------------
	        */
	        boolean bSandbox = true;
	        if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYPAL_LIVE_ENVIRONMENT")).equalsIgnoreCase("Y")){
	        	bSandbox = false;
	        }
	        
	        
	        PaypalSaleFunctions ppf = new PaypalSaleFunctions();
	        HashMap nvp = ppf.CallShortcutExpressCheckout (paymentAmount, returnURL, cancelURL);
	        String strAck = nvp.get("ACK").toString();
	        if(strAck !=null && strAck.equalsIgnoreCase("Success"))
	        {
	            session.setAttribute("token", nvp.get("TOKEN").toString());
	            //' Redirect to paypal.com
	           // response.sendRedirect(response.encodeRedirectURL( nvp.get("TOKEN").toString() ));
	            if (bSandbox == true){
	            	response.sendRedirect(response.encodeRedirectURL("https://www.sandbox.paypal.com/webscr?cmd=_express-checkout&token="+ nvp.get("TOKEN").toString() ));
	            }else{
	            	response.sendRedirect(response.encodeRedirectURL("https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token="+ nvp.get("TOKEN").toString() ));
	            }
	        }
	        else
	        {
	            // Display a user friendly Error on the page using any of the following error information returned by PayPal
	
	            String ErrorCode = nvp.get("L_ERRORCODE0").toString();
	            String ErrorShortMsg = nvp.get("L_SHORTMESSAGE0").toString();
	            String ErrorLongMsg = nvp.get("L_LONGMESSAGE0").toString();
	            String ErrorSeverityCode = nvp.get("L_SEVERITYCODE0").toString();
	        }
	}
	
	public void doPost(HttpServletRequest request,
	                   HttpServletResponse response)
	    throws ServletException, IOException {
	  doGet(request, response);
	}

}
