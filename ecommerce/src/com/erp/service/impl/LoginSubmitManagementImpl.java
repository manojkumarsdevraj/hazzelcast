package com.erp.service.impl;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.LoginSubmitManagement;
import com.erp.service.model.LoginSubmitManagementModel;
import com.unilog.utility.CommonUtility;

public class LoginSubmitManagementImpl  implements LoginSubmitManagement{
	
	private HttpServletRequest request;
	private HttpServletResponse response;

	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	
	public String ERPLOGIN(LoginSubmitManagementModel loginSubmitManagementModel){
		
		String erp = "cimm2bcentral";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		String userSessionId = "";
		
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = LoginSubmitManagementModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.LoginSubmitManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("ERPLOGIN", paramObject);
			userSessionId = (String) method.invoke(obj, loginSubmitManagementModel);
			if(CommonUtility.validateString(userSessionId).length()<1){
				userSessionId = "";
			}
			
		}
		catch (Exception e) {
			userSessionId = "";
			e.printStackTrace();
		}
		return userSessionId;
		
	}
	
}
