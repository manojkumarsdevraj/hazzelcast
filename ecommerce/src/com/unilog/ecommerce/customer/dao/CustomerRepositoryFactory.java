package com.unilog.ecommerce.customer.dao;

import com.unilog.ecommerce.user.validation.RegRequestType;

public class CustomerRepositoryFactory {
	public static CustomerRepository getCustomerRepository(String type) {
		CustomerRepository customerRepository = null;
		type = (type == null || type.trim().length() <= 0)? "GENERIC": type;
		RegRequestType requestType = RegRequestType.valueOf(type);
		switch(requestType){
			case GENERIC:
			default :
				customerRepository = GenericCustomerRepoImpl.getInstance();
				break;
		}
		return customerRepository;
	}
}
