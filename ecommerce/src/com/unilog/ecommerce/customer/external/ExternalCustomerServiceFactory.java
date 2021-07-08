package com.unilog.ecommerce.customer.external;

import com.unilog.ecommerce.user.validation.RegRequestType;

public class ExternalCustomerServiceFactory {
	
	public static ExternalCustomerService getExternalService(String type) {
		ExternalCustomerService externalService = null;
		type = (type == null || type.trim().length() <= 0)? "GENERIC": type;
		RegRequestType requestType = RegRequestType.valueOf(type);
		switch(requestType){
			case CIMM2BC_ECLIPSEV1:
			case GENERIC:	case CIMM2BC:
			default :
				externalService = Cimm2bcExternalCustomerServiceImpl.getInstance();
				break;
		}
		return externalService;
	}
}
