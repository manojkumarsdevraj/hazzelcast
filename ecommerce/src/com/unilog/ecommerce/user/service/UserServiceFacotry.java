package com.unilog.ecommerce.user.service;

import com.unilog.ecommerce.user.validation.RegRequestType;

public class UserServiceFacotry {
	
	private UserServiceFacotry() {}
		
	public static UserService getUserService(String type) {
		RegRequestType requestedType = RegRequestType.valueOf(type.toUpperCase());
		UserService userService = null;
		switch(requestedType) {
		case CIMM2BC :
			userService = Cimm2bcUserServiceImpl.getInstance();
			break;
		case CIMM2BC_ECLIPSEV1 :
			userService = Cimm2bcEclipseV1UserSerivce.getInstance();
			break;
		case PURVIS : 
			userService = PurvisCimm2bcUserServiceImpl.getInstance();
			break;
			
		case GENERIC :
			default : 
				userService = GenericUserServiceImpl.getInstance();
				break;
		}
		return userService;
	}
}
