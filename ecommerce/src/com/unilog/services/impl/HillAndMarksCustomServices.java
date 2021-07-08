package com.unilog.services.impl;



import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;

import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.defaults.SendMailModel;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.utility.CommonUtility;


public class HillAndMarksCustomServices implements UnilogFactoryInterface{
	private static UnilogFactoryInterface serviceProvider;

	private HillAndMarksCustomServices() {}

	public static UnilogFactoryInterface getInstance() {
		synchronized (HillAndMarksCustomServices.class) {
			if(serviceProvider == null) {
				serviceProvider = new HillAndMarksCustomServices();
			}
		}
	return serviceProvider;
	}	
	public void changeCustomerName(Cimm2BCentralOrder orderRequest,SalesOrderManagementModel salesOrderInput) {
		orderRequest.setCustomerName("");		
	}

	public void setMailSubject(SalesModel erpOrderDetail, SendMailModel sendMailModel, HttpSession session) {
		if(CommonUtility.validateString(erpOrderDetail.getOrderType()).equalsIgnoreCase("QU")){
			if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("quote.confirmation.subject")).length()>0){
				sendMailModel.setMailSubject(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("quote.confirmation.subject")));
			}else{
				sendMailModel.setMailSubject("Quote Confirmation");
			
			}
		}				
	}
}
