package com.unilog.users;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.unilog.defaults.Global;
import com.unilog.security.LoginAuthentication;
import com.unilog.security.SecureData;
import com.unilog.utility.CommonUtility;


public class Logoff extends ActionSupport {
	
	private String loginId;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String renderContent;
	private String requestFrom;
	
	
	
	public String getRequestFrom() {
		return requestFrom;
	}
	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}
	public String getRenderContent() {
		return renderContent;
	}

	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}

	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6091751320468706883L;

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		
		boolean logoutSuccess=false;
		request =ServletActionContext.getRequest();
	    response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		String ipaddress = request.getHeader("X-Forwarded-For");
		String logOffType = CommonUtility.validateString(request.getParameter("lType"));
		if (ipaddress  == null)
			ipaddress = request.getRemoteAddr();
		if(session.getAttribute(Global.USERNAME_KEY)!=null)
		UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "SignOut Click", session.getId(), ipaddress, "SignOut", "NA");
		loginId = CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY));
		String emailAddress = CommonUtility.validateString((String) session.getAttribute("userEmailAddress"));
		System.out.println("loginId:---------"+loginId);
		System.out.println("Email Address:---"+emailAddress);
		System.out.println("logOffType:------"+logOffType);
		
		LoginAuthentication loginAuthentication = new LoginAuthentication();
		logoutSuccess=loginAuthentication.logout(session);
		System.out.println("logoutSuccess:---"+logoutSuccess);
		if(logoutSuccess){
			System.out.println("logoutSuccess:--- Assigning Web Session Values");
			//Commented while working on werner redesign site slowness issue 29-04-2019, get back to shashi or bharath if there are concerns
			/*WebLogin.loadWebUser();
			WebLogin.webUserSession(session.getId());*/
			if(CommonUtility.validateString(logOffType).length()>0){
				if(CommonUtility.validateString(logOffType).equalsIgnoreCase("changePasswordOnLogin") && CommonUtility.validateEmail(emailAddress)){
					UsersModel userInfo = UsersDAO.forgotPassword(loginId, emailAddress);
					Date timeNow = new Date();
					userInfo.setaRPassword(SecureData.getPunchoutSecurePassword(timeNow.toString()));
					userInfo.setEmailAddress(emailAddress);
					int results = UsersDAO.advancedForgotPasswordInsert(userInfo);
					if(results>0){
						renderContent = "/AFP/"+SecureData.getPunchoutSecurePassword(timeNow.toString())+"/cpol";
						return "ResultLoader";//return "changePasswordOnLogin";
					}else{
						renderContent = "Logout";
						return "ResultLoader";
					}
				}if(CommonUtility.validateString(logOffType).equalsIgnoreCase("PasswordUpdatedSuccessfully")) {
					renderContent = "-"+loginId;
					return "LoginPage";
				}else{
					renderContent = "Logout";
					return "ResultLoader";
				}
			}else{
				return SUCCESS;
			}
		}else{
			return ERROR;
		}
	}
	
	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	

}