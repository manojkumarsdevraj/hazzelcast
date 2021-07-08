package com.unilog.ecommerce.user.dao;

import com.unilog.ecommerce.user.validation.RegRequestType;

public class UserRepositoryFactory {
	
	private UserRepositoryFactory() {}
	
	public static UserRepository getUserRepository(String type) {
		UserRepository userRepository = null;
		type = (type == null || type.trim().length() <= 0)? "GENERIC": type;
		RegRequestType requestType = RegRequestType.valueOf(type);
		switch(requestType){
			case GENERIC:
			default :
				userRepository = GenericUserRepoImpl.getInstance();
				break;
		}
		return userRepository;
	}
}
