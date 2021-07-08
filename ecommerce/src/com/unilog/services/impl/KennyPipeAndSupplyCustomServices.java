package com.unilog.services.impl;

import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class KennyPipeAndSupplyCustomServices implements UnilogFactoryInterface {
	private static UnilogFactoryInterface serviceProvider;
	private KennyPipeAndSupplyCustomServices() {}
	public static UnilogFactoryInterface getInstance() {
		synchronized (KennyPipeAndSupplyCustomServices.class) {
			if(serviceProvider == null) {
				serviceProvider = new KennyPipeAndSupplyCustomServices();
			}
	}
	return serviceProvider;
}

	 public void insertCustomFields(String wareHouse,int userid,int buyingCompanyid,UsersModel userDetailsInput)
		{	
			String newsLetterCustomFiledValue="";
			if(userDetailsInput.getNewsLetterSub()!=null && userDetailsInput.getNewsLetterSub().equalsIgnoreCase("ON")){
				newsLetterCustomFiledValue="Y";
			}else{
				newsLetterCustomFiledValue="N";
			}
			UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
		}

}
