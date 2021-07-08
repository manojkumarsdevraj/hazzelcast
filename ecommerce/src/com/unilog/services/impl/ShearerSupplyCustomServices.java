package com.unilog.services.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.unilog.defaults.Global;
import com.unilog.security.SecureData;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.utility.CommonUtility;

public class ShearerSupplyCustomServices implements UnilogFactoryInterface{
	private static UnilogFactoryInterface serviceProvider;
	private ShearerSupplyCustomServices() {}
	public static UnilogFactoryInterface getInstance() {
			synchronized (WernerRedesignCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new ShearerSupplyCustomServices();
				}
			}
		return serviceProvider;
	}
	
	public void setHeaderLevelAuthDetails(String userName,String password){
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
		 	session.setAttribute(Global.USERNAME_KEY,userName);
			session.setAttribute("securedPassword",SecureData.getsecurePassword(password));
	} 
	
	public void setCustomerHomeBranch(Cimm2BCentralCustomer customerInfo){
		
		if(CommonUtility.validateString(customerInfo.getHomeBranch()).length()>0){
			customerInfo.setHomeBranch(customerInfo.getHomeBranch().substring(Math.max(customerInfo.getHomeBranch().length() - 2, 0)));
			customerInfo.setDefaultShipLocationId(customerInfo.getDefaultShipLocationId().substring(Math.max(customerInfo.getHomeBranch().length() - 2, 0)));
		}
	} 
	
}
