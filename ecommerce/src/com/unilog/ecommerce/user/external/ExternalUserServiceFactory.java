package com.unilog.ecommerce.user.external;

import com.unilog.ecommerce.user.validation.RegRequestType;

public class ExternalUserServiceFactory {
	public static ExternalUserService getExterUserService(String type) {
		ExternalUserService externalUserService = null;
		type = (type == null || type.trim().length() <= 0)? "GENERIC": type;
		RegRequestType requestType = RegRequestType.valueOf(type);
		switch(requestType) {
			case  CIMM2BC_ECLIPSEV1:
				externalUserService = Cimm2bcEclipseV1ExternalUserService.getInstance();
				break;
			case CIMM2BC:
			case GENERIC:
			default :
				externalUserService = new Cimm2bcExternalUserSerice();
				break;
		}
		return externalUserService;
	}
}
