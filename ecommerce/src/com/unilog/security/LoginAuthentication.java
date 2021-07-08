package com.unilog.security;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;

import com.erp.service.UserManagement;
import com.erp.service.impl.UserManagementImpl;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.defaults.ULLog;
import com.unilog.users.LoginUserSessionObject;
import com.unilog.users.UserLogin;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class LoginAuthentication
{
	private static final long serialVersionUID = -3459347464645014823L;
	private  LoginContext context = null;

	public boolean authenticate(String userName,String userPassword, HttpSession session) 
	{		
		long startTimer = CommonUtility.startTimeDispaly();
		boolean loginSuccess=false; 
		System.out.println("inside authenticate userName : "+ userName);
		String erp = "defaults";
		if(CommonDBQuery.getSystemParamtersList().get("ERP")!=null && CommonDBQuery.getSystemParamtersList().get("ERP").trim().length()>0){
			erp = CommonDBQuery.getSystemParamtersList().get("ERP").trim();
		}
		session.setAttribute("erpType", erp);
		
		String paymentGateway = null;
		if(CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY")!=null && CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY").trim().length()>0){
			paymentGateway = CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY").trim();
		}
		session.setAttribute("PAYMENT_GATEWAY", paymentGateway);
		
		
		final HashMap<String, String> authDetails = new HashMap<String, String>();
		ULLog.debug("In LoginAuthentication");
		HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId(userName,"Y");
		try 
		{
			context = new LoginContext("supplyForce",new SampleCallbackHandler(userName,userPassword));
			System.out.println("reg context:---"+context);
			System.out.println("companyLogo----"+session.getAttribute(Global.LOGONAME_KEY)); 

		} catch (final LoginException exception){
			ULLog.debug("\n** The LoginContext security layer was expecting to find a\n** descriptor for an application by the name of 'sample'.\n** Please check your JAAS login configuration provider\n** (javax.security.auth.login.Configuration).\n** In a simple environment, this would be your jaas.config file.");
			ULLog.debug("Exception:" + exception.fillInStackTrace());
			authDetails.put("authentic", "false");
			authDetails.put("message","The LoginContext security layer was expecting to find a\n** descriptor for an application by the name of 'sample'.\n** Please check your JAAS login configuration provider\n** (javax.security.auth.login.Configuration).\n** In a simple environment, this would be your jaas.config file.");
		}

		try {
	          if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SINGLE_USER_UNDER_MULTIPLE_CUSTOMER")).equalsIgnoreCase("Y")) {
	                int userId = UsersDAO.authenticatUser(userName, userPassword, "Y");
	                if(userId<=0) {loginSuccess=false;return loginSuccess;}
	            }else {
				context.login();
			}
			System.out.println("userid:---"+userDetails.get("userId"));
			String isAdmin = "N";
			String isRetailUser = "N";
			String isAuthPurAgent="N";
			String isGeneralUser = "N";
			String isAuthTechnician	="N";
			String isTishmanUser ="N";
			UsersModel defaultContact = new UsersModel();
			UsersModel userInfo = new UsersModel();
			UserManagement usersObj = new UserManagementImpl();   
			userInfo.setUserToken((String)session.getAttribute("userToken"));
			String contactIdStr = "0";
			if(userDetails.get("contactId")!=null && userDetails.get("contactId").trim().length()>0){
				contactIdStr = userDetails.get("contactId");
			}
			userInfo.setContactId(CommonUtility.validateString(contactIdStr));
			userInfo.setUserName(userName);

			defaultContact = usersObj.getRoleFromErp(userInfo);

			if(userDetails.get("userRole")==null)
			{
				Connection conn =null;
				try
				{
					conn = ConnectionManager.getDBConnection();
					if(defaultContact.getIsSuperUser()!=null && defaultContact.getIsSuperUser().trim().equalsIgnoreCase("Yes"))
					{
						UsersDAO.assignRoleToUser(conn,userDetails.get("userId"), "Ecomm Customer Super User");
						isAdmin = "Y";
					}
					else
					{
						UsersDAO.assignRoleToUser(conn,userDetails.get("userId"), "Ecomm Customer Auth Purchase Agent");
						isAuthPurAgent = "Y";
					}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally
					{
						ConnectionManager.closeDBConnection(conn);
					}
					
					
				}
				else
				{	
					String role = "";
					if(CommonUtility.validateString(erp).equalsIgnoreCase("defaults") && CommonUtility.validateString(userDetails.get("userRole")).length()>0){
						role = userDetails.get("userRole");
					}else{
						role = UsersDAO.checkAndUpdateRoleToUser(userDetails.get("userId"), defaultContact.getIsSuperUser(), userDetails.get("userRole"));
					}
					if(role!=null && role.equalsIgnoreCase("Ecomm Customer Super User")){
						isAdmin = "Y";
					}else if(role!=null && role.equalsIgnoreCase("Ecomm Retail User")){
						isRetailUser = "Y";
					}else if(role!=null && role.equalsIgnoreCase("Ecomm Customer Auth Purchase Agent")){
						isAuthPurAgent = "Y";
					}else if(role!=null && role.equalsIgnoreCase("Ecomm Auth Technician")){
						isAuthTechnician = "Y";
					}else if(role!=null && role.equalsIgnoreCase("Ecomm SALES_REP")){
						userDetails.put("isSalesRep", "Y");
					}else if(role!=null && role.equalsIgnoreCase("Ecomm SALES_ADMIN")){
						userDetails.put("isSalesAdmin", "Y");
					}else if(role!=null && CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE")!=null && CommonUtility.validateString(role).equalsIgnoreCase(CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE").trim())){
						userDetails.put("isSpecialUser", "Y");
					}else if(role!=null && role.equalsIgnoreCase("Ecomm Purchaser with Single Order limit")){
						isTishmanUser = "Y";
					}else{
						isGeneralUser = "Y";
					}
				}
				String newsLetterSub = UsersDAO.getUserNewsLetterSubscriptionStatus(CommonUtility.validateNumber(userDetails.get("userId")), "NEWSLETTER");
				userDetails.put("newsLetterSub", newsLetterSub);
				userDetails.put("isAdmin",isAdmin);
				userDetails.put("isAuthPurAgent",isAuthPurAgent);
				userDetails.put("isRetailUser",isRetailUser);
				userDetails.put("isGeneralUser",isGeneralUser);
				userDetails.put("isAuthTechnician", isAuthTechnician);
				userDetails.put("isTishmanUser", isTishmanUser);
				userDetails.put("isCreditCardOnly",CommonUtility.validateString(UsersDAO.getUserCustomFieldValue(CommonUtility.validateNumber(userDetails.get("userId")),"CREDITCARDONLY")));
				userDetails.put("isPickUp",CommonUtility.validateString(UsersDAO.getUserCustomFieldValue(CommonUtility.validateNumber(userDetails.get("userId")),"ISPICKUP")));
				userDetails.put("hidePrice", CommonUtility.validateString(UsersDAO.getUserCustomFieldValue(CommonUtility.validateNumber(userDetails.get("userId")),"HIDEPRICE")));

				LoginUserSessionObject.setUserSessionObject(session,userDetails);
				loginSuccess=true;
				//UserLogin.headerUserName = userName;
				//UserLogin.headerPassword = userPassword;
		
		} catch (final FailedLoginException exception) {
			
			loginSuccess=false;
			//doneMessage = new FacesMessage("Authentication ERROR.");			
			
		} catch (final AccountExpiredException exception) {
	
			loginSuccess=false;
			//doneMessage = new FacesMessage("Your account has expired.");	
			
		} catch (final CredentialExpiredException exception) {
	
			loginSuccess=false;
			//doneMessage = new FacesMessage("Your credentials have expired.");	
			
		} catch (final Exception exception) {
			exception.printStackTrace();
			loginSuccess=false;
			ULLog.error("ULLoginAuthentication::Exception"	+ exception.fillInStackTrace());
		}		
		
		// return authDetails;
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return loginSuccess;
	}
	
	
	//*****************Logout************
	public boolean logout(HttpSession session) {
		ULLog.debug("In ULLoginAuthentication.logout");
		boolean logoutSuccess=false;
		try {
			if (session != null) {
				session.removeAttribute(Global.SUBJECT_KEY);
				session.removeAttribute(Global.USERNAME_KEY);
				session.removeAttribute(Global.ORGCODE_KEY);
				session.removeAttribute(Global.USERID_KEY);
				session.removeAttribute(Global.CONTEXT_KEY);
				session.removeAttribute(Global.LOGONAME_KEY);
				session.removeAttribute(Global.SLUSERNAME_KEY);
				session.removeAttribute("cartCountSession");
				context = (LoginContext) session.getAttribute(Global.CONTEXT_KEY);
				System.out.println("logoff context:---"+context);
			} else {
				ULLog.debug("ULLoginAuthentication:: session is NULL");
			}

			if (context != null) {
				context.logout();
			} else {
				ULLog.debug("ULLoginAuthentication:: context is NULL");
			}

			if (session != null) {
				session.invalidate();
			}

			logoutSuccess = true;
		} catch (LoginException loginException) {				
			logoutSuccess = false;
			loginException.printStackTrace();
		} catch (Exception exception) {		
			logoutSuccess = false;
			exception.printStackTrace();
		}

		return logoutSuccess;
	}	
}



class SampleCallbackHandler implements CallbackHandler {

	private String userName;
	private String userPassword;

	/*
	 * The user supplied the credentials on the command line.
	 */

	protected SampleCallbackHandler(String userName, String userPassword) {
		this.userName = userName;
		this.userPassword = userPassword;
	}

	
	public void handle(Callback[] callbacks) throws IOException,UnsupportedCallbackException 
	{

		for (int i = 0; i < callbacks.length; i++) 
		{
			if (callbacks[i] instanceof TextOutputCallback) 
			{

				// display the message according to the specified type
				TextOutputCallback toc = (TextOutputCallback) callbacks[i];
				switch (toc.getMessageType()) 
				{
				case TextOutputCallback.INFORMATION:
					ULLog.debug("INFORMATION: " + toc.getMessage());
					break;
				case TextOutputCallback.ERROR:
					ULLog.debug("ERROR: " + toc.getMessage());
					break;
				case TextOutputCallback.WARNING:
					ULLog.debug("WARNING: " + toc.getMessage());
					break;
				default:
					throw new IOException("Unsupported message type: "+ toc.getMessageType());
				}
				
			} 
			else if (callbacks[i] instanceof NameCallback) 
			{
				NameCallback nc = (NameCallback) callbacks[i];
				if (nc.getPrompt().equalsIgnoreCase("username"))
					nc.setName(userName);

			} 
			else if (callbacks[i] instanceof PasswordCallback) 
			{
				PasswordCallback pc = (PasswordCallback) callbacks[i];
				pc.setPassword(userPassword.toCharArray());

			} else {
				throw new UnsupportedCallbackException(callbacks[i],
						"Unrecognized Callback");
			}
		}
	}
}