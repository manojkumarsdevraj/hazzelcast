package com.unilog.utility;

import java.util.ArrayList;
import java.util.List;

import com.unilog.database.CommonDBQuery;

public class RetryPolicy {
	private static RetryPolicy retryPolicy;

	List<String> retryableUriList = new ArrayList<>();

	private RetryPolicy() {
		initializeRetryPolicy();
	}

	public void initializeRetryPolicy() {
		// TODO: These uris must come from system Parameters tables
		// So have the system parameters keys of these uris in a property file(comma
		// separated value)
		// Example retryableUris=PRICE_AND_AVAILABILITY_URI,GET_USER_URI etc..
		// Load these keys into a list and dynamically pull the values from system
		// parameter table and populate the retryableUriList variable
		retryableUriList.add(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_PRICE_AND_AVAILABILITY_API")));
		retryableUriList.add(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHENTICATION_REQUEST_API")));	
		retryableUriList.add(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API"))); 	
		retryableUriList.add(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API")));
		retryableUriList.add(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONTACT_INQUIRY_API")));
	}
 
	public static RetryPolicy getInstance() {
		if (retryPolicy == null) {
			synchronized (RetryPolicy.class) {
				retryPolicy = new RetryPolicy();
			}
		}
		return retryPolicy;
	}

	public boolean checkRetryPolicy(String requestBase) {
		boolean flag = false;
		for (String uri : retryableUriList) {
			
			if (uri.length()>0 && requestBase.contains(uri)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

}
