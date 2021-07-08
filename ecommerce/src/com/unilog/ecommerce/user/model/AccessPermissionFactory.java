package com.unilog.ecommerce.user.model;

import com.unilog.database.CommonDBQuery;

public class AccessPermissionFactory {
	public static AccessPermission getUserAccessRight(String type) {
		AccessPermission accessRights = null;
		String defaultRole, submitByPo, submitByCc;
		UserType userType = UserType.valueOf(type.toUpperCase());
		switch(userType) {
			case B2BNEWACCOUNT :
				//Ecomm Customer Auth Purchase Agent
				defaultRole = CommonDBQuery.getSystemParamtersList().get("B2B_NEW_ACCOUNT_DEFAULT_USER_ROLE");
				submitByPo = CommonDBQuery.getSystemParamtersList().get("B2B_NEW_ACCOUNT_SUBMIT_BY_PO");
				submitByCc = CommonDBQuery.getSystemParamtersList().get("B2B_NEW_ACCOUNT_SUBMIT_BY_CC");
				
				defaultRole = (defaultRole != null)? defaultRole : "Ecomm Customer Super User";
				submitByPo = (submitByPo != null)? submitByPo : "Y";
				submitByCc = (submitByCc != null)? submitByCc : "Y";
				accessRights = new AccessPermission(defaultRole, submitByPo, submitByCc);
				break;
				
			case B2BONACCOUNT : 
				//Ecomm Customer Auth Purchase Agent
				defaultRole = CommonDBQuery.getSystemParamtersList().get("B2B_ON_ACCOUNT_DEFAULT_USER_ROLE");
				submitByPo = CommonDBQuery.getSystemParamtersList().get("B2B_ON_ACCOUNT_SUBMIT_BY_PO");
				submitByCc = CommonDBQuery.getSystemParamtersList().get("B2B_ON_ACCOUNT_SUBMIT_BY_CC");
				
				defaultRole = (defaultRole != null)? defaultRole : "Ecomm Customer Super User";
				submitByPo = (submitByPo != null)? submitByPo : "Y";
				submitByCc = (submitByCc != null)? submitByCc : "Y";
				accessRights = new AccessPermission(defaultRole, submitByPo, submitByCc);
				break;
				
			case B2CRETAIL : 
					accessRights = new AccessPermission("Ecomm Retail User", "N", "Y");
				break;
		}
		return accessRights;
	}
}
