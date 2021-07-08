package com.unilog.services.impl;

import javax.servlet.http.HttpSession;

import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class PipesValvesCustomServices implements UnilogFactoryInterface{
	private static UnilogFactoryInterface serviceProvider;
	private PipesValvesCustomServices() {}
	public static UnilogFactoryInterface getInstance() {
		synchronized (PipesValvesCustomServices.class) {
			if(serviceProvider == null) {
				serviceProvider = new PipesValvesCustomServices();
			}
		}
		return serviceProvider;
	}
	
	 @Override
	 public UsersModel getUserContactAddress(int userId,  HttpSession session) {
		UsersModel addressList = null;
		String isRetailUser = (String) session.getAttribute("isRetailUser");
		if(CommonUtility.validateString(isRetailUser).equalsIgnoreCase("Y")){
			addressList= UsersDAO.getEntityDetailsByUserId(userId);
			addressList.setAddressType("Ship");
			if(addressList!=null){
				session.setAttribute("userContactAddress", addressList);
			}
		}
		return addressList;
	}
}
