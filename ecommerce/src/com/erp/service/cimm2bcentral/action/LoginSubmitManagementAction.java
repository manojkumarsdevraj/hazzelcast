package com.erp.service.cimm2bcentral.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.http.HttpStatus;
import org.apache.struts2.ServletActionContext;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthenticationRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthenticationResponse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.model.LoginSubmitManagementModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

public class LoginSubmitManagementAction {
	
	private  HttpServletRequest request;
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	private  HttpSession session;
	
	public  String ERPLOGIN(LoginSubmitManagementModel loginSubmitManagementModel) {
		
		request = ServletActionContext.getRequest();
		session = request.getSession();
		session.removeAttribute("erpLoginContactDetails");
		String userName = CommonUtility.validateString(loginSubmitManagementModel.getUserName());
		String password = CommonUtility.validateString(loginSubmitManagementModel.getPassword());
		String userSessionId = "";
		try{
			
			Cimm2BCentralAuthenticationRequest requestObj = new Cimm2BCentralAuthenticationRequest();
			requestObj.setUsername(userName);
			requestObj.setPassword(password);
			requestObj.setForgotPassword(loginSubmitManagementModel.isForgotPassword());
			System.out.println("=============================================");
			System.out.println("Login submit session id in request: "+session.getId());
			System.out.println("=============================================");	
			String AUTHENTICATION_REQUEST_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHENTICATION_REQUEST_API"));
			Cimm2BCentralResponseEntity authenticationResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(AUTHENTICATION_REQUEST_API, HttpMethod.POST, requestObj, Cimm2BCentralAuthenticationResponse.class);
			
			Cimm2BCentralAuthenticationResponse authResponseObj = null;
			if(authenticationResponseEntity!=null && authenticationResponseEntity.getData() != null && authenticationResponseEntity.getStatus() != null && authenticationResponseEntity.getStatus().getCode() == HttpStatus.SC_OK){
				authResponseObj = (Cimm2BCentralAuthenticationResponse) authenticationResponseEntity.getData();
				if(loginSubmitManagementModel.isForgotPassword() && authResponseObj!=null){
					if(authenticationResponseEntity.getStatus().getCode() == 200 && !CommonUtility.validateString(authenticationResponseEntity.getStatus().getMessage()).toLowerCase().contains("unsuccessful")){
						userSessionId = "Y";
					}else{
						userSessionId = "N";
					}
				}
			}else {
				session.setAttribute("connectionError", "No");
			}
			
			if(!loginSubmitManagementModel.isForgotPassword() && authResponseObj!=null){
				System.out.println("=============================================");
				System.out.println("Login submit session id in response: "+session.getId());
				System.out.println("=============================================");
				userSessionId = CommonUtility.validateString(authResponseObj.getCustomerERPId());
				session.setAttribute("connectionError", "No");
				 session.setAttribute("loginCustomerERPId", authResponseObj.getCustomerERPId());
				 session.setAttribute("loginUserERPId", authResponseObj.getUserERPId());
				if(CommonUtility.customServiceUtility()!=null) {
					 CommonUtility.customServiceUtility().setHeaderLevelAuthDetails(userName,password);
				}
				
				 if(authResponseObj.getCustomerDetail()!=null){
					 session.setAttribute("loginCustomerERPId", authResponseObj.getCustomerDetail().getCustomerERPId());
					 userSessionId = authResponseObj.getCustomerDetail().getCustomerERPId();
				 }
				if(authResponseObj.getContacts()!=null){
					session.setAttribute("erpLoginContactDetails", authResponseObj.getContacts());
					session.setAttribute("EntityID",authResponseObj.getCustomerERPId());
				}
			}
			
		}catch(Exception e){
			userSessionId = "";
			if(loginSubmitManagementModel.isForgotPassword()){
				userSessionId = "N";
			}
			e.printStackTrace();
		}
		
			
		
	    return userSessionId;
	}

}
