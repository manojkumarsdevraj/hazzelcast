package com.unilog.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erp.eclipse.inquiry.LoginSubmit;
import com.unilog.users.AddressModel;
import com.unilog.users.UserRegisterUtility;
import com.unilog.utility.CommonUtility;

/**
 * Servlet implementation class NewUserRegistration
 */
public class NewUserRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewUserRegistration() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName="";
		String password="";
		userName = request.getParameter("userName");
		password = request.getParameter("password");
		System.out.println("user Name : " + userName);
		if(userName==null)
			userName = "";
		if(password==null)
			password="";
		StringBuilder sb = new StringBuilder();
		OutputStream os = response.getOutputStream();
		
		
		if(!userName.trim().equalsIgnoreCase("") && !password.trim().equalsIgnoreCase(""))
		{
			String eclipseSessionId = LoginSubmit.ERPLOGINWithoutSession(userName, password);
			if(eclipseSessionId!=null && !eclipseSessionId.trim().equalsIgnoreCase(""))
			{
				String eclipseResponse="";
				AddressModel addressModel = new AddressModel();
				addressModel.setEmailAddress(userName);
				addressModel.setUserPassword(password);
				addressModel.setUserToken(eclipseSessionId);
				addressModel.setExistingUser("Y");
				addressModel.setParentUserId(CommonUtility.validateParseIntegerToString(0));
				addressModel.setUserStatus("Y");
				//eclipseResponse = UserRegisterUtility.registerUserInDB(userName, password,eclipseSessionId,"Y",0,"Y");
				eclipseResponse = UserRegisterUtility.registerUserInDB(addressModel);
				if(eclipseResponse!=null && eclipseResponse.trim().contains("successfully"))
				{
					sb.append("User registered successfully"); 
					os.write(sb.toString().getBytes());
				}
				else
				{
					sb.append("User registration failed. Please contact customer service.");
					os.write(sb.toString().getBytes());
				}
				
			}
			else
			{
				sb.append("User Not Registered In Eclipse");
				os.write(sb.toString().getBytes());
			}
		}
		else
		{
			sb.append("User name or Password is empty");
			os.write(sb.toString().getBytes());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName="";
		String password="";
		userName = request.getParameter("userName");
		password = request.getParameter("password");
		System.out.println("user Name : " + userName);
		if(userName==null)
			userName = "";
		if(password==null)
			password="";
		StringBuilder sb = new StringBuilder();
		OutputStream os = response.getOutputStream();
		
		
		if(!userName.trim().equalsIgnoreCase("") && !password.trim().equalsIgnoreCase(""))
		{
			String eclipseSessionId = LoginSubmit.ERPLOGINWithoutSession(userName, password);
			if(eclipseSessionId!=null && !eclipseSessionId.trim().equalsIgnoreCase(""))
			{
				String eclipseResponse="";
				AddressModel addressModel = new AddressModel();
				addressModel.setEmailAddress(userName);
				addressModel.setUserPassword(password);
				addressModel.setUserToken(eclipseSessionId);
				addressModel.setExistingUser("Y");
				addressModel.setParentUserId(CommonUtility.validateParseIntegerToString(0));
				addressModel.setUserStatus("Y");
				//eclipseResponse = UserRegisterUtility.registerUserInDB(userName, password,eclipseSessionId,"Y",0,"Y");
				eclipseResponse = UserRegisterUtility.registerUserInDB(addressModel);
				if(eclipseResponse!=null && eclipseResponse.trim().contains("successfully"))
				{
					sb.append("User registered successfully");
					os.write(sb.toString().getBytes());
				}
				else
				{
					sb.append("User registration failed. Please contact customer service.");
					os.write(sb.toString().getBytes());
				}
				
			}
			else
			{
				sb.append("User Not Registered In Eclipse");
				os.write(sb.toString().getBytes());
			}
		}
		else
		{
			sb.append("User name or Password is empty");
			os.write(sb.toString().getBytes());
		}
	
	}

}
