package com.erp.service.cimmesb.action;

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
	
	private HttpServletRequest request;
	private HttpSession session;
	
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
			}
			
			if(!loginSubmitManagementModel.isForgotPassword() && authResponseObj!=null){
				userSessionId = CommonUtility.validateString(authResponseObj.getSessionId());
				session.setAttribute("connectionError", "No");
				if(authResponseObj.getContacts()!=null){
					session.setAttribute("erpLoginContactDetails", authResponseObj.getUserERPId());
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
