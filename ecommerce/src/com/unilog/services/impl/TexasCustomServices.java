package com.unilog.services.impl;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;

import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;


public class TexasCustomServices implements UnilogFactoryInterface{
	private static UnilogFactoryInterface serviceProvider;
	private TexasCustomServices() {}
	public static UnilogFactoryInterface getInstance() {
			synchronized (WernerRedesignCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new TexasCustomServices();
				}
			}
		return serviceProvider;
	}

	 public Cimm2BCentralLineItem  disableUmqtyToErp(Cimm2BCentralLineItem cimm2bCentralLineItem){
		 cimm2bCentralLineItem.setUomQty("");
		 return cimm2bCentralLineItem;
	 }
	 
	@Override
	public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
	{	
		String newsLetterCustomFiledValue="";
		if(userDetailsInput.getNewsLetterSub()!=null && (userDetailsInput.getNewsLetterSub().equalsIgnoreCase("Y") || 
				userDetailsInput.getNewsLetterSub().equalsIgnoreCase("ON"))){
			newsLetterCustomFiledValue="Y";
		}else{
			newsLetterCustomFiledValue="N";
		}
		UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
	}
}
