package com.unilog.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.supercsv.cellprocessor.ParseInt;

import com.unilog.sales.CIMMTaxModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;


public class MarksSupplyCustomServices implements UnilogFactoryInterface{
	
	private static UnilogFactoryInterface serviceProvider;	
	
	private MarksSupplyCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (MacombGroupCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new MarksSupplyCustomServices();
				}
			}
		return serviceProvider;
	}
	public void setUserToken(HashMap<String, String> userDetails, HttpSession session){
		session.setAttribute("userToken", userDetails.get("contactId")); 
	}
	public void updateUserCustomFields(String hidePrice, String invoiceDetails, String invoiceSummary, String buyingCompanyId, String entityId, int userId) {
		int entity = Integer.parseInt(entityId);			
		if(hidePrice !=null && hidePrice.equalsIgnoreCase("on")){
     	UsersDAO.insertCustomField("Y", "HIDEPRICE", userId, entity,"USER");
        } 
		else if(hidePrice==null)
        {
     	 UsersDAO.insertCustomField("N", "HIDEPRICE", userId, entity,"USER");
        }
        if(invoiceDetails !=null && invoiceDetails.equalsIgnoreCase("on"))
        {
         UsersDAO.insertCustomField("Y", "HIDE_DASHBOARD_INVOICE_SUMMARY", userId, entity,"USER");
        } 
        else if(invoiceDetails==null)
        {
     	UsersDAO.insertCustomField("N", "HIDE_DASHBOARD_INVOICE_SUMMARY", userId, entity,"USER");
        }        
        if(invoiceSummary !=null && invoiceSummary.equalsIgnoreCase("on"))
        {
        UsersDAO.insertCustomField("Y", "HIDE_DASHBOARD_INVOICE_DETAILS", userId, entity,"USER");
        }
        else if(invoiceSummary==null)
        {
        UsersDAO.insertCustomField("N", "HIDE_DASHBOARD_INVOICE_DETAILS", userId, entity,"USER");
        }		
	}
}