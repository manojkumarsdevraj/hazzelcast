package com.unilog.ecommerce.customer.service;

import com.unilog.ecommerce.user.validation.RegRequestType;

public class CustomerServiceFactory {
	public static CustomerService getCustomerService(String type) {
		CustomerService customerService = null;
		type = (type == null || type.trim().length() <= 0)? "GENERIC": type;
		RegRequestType requestType = RegRequestType.valueOf(type);
		switch(requestType){
			case GENERIC:
			default :
				customerService = GenericCustomerServiceImpl.getInstance();
				break;
		}
		return customerService;
	}
}
