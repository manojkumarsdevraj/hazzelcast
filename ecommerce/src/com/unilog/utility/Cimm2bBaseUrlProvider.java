package com.unilog.utility;

import java.util.ArrayList;
import java.util.List;

import com.unilog.database.CommonDBQuery;

public class Cimm2bBaseUrlProvider {
	private static Cimm2bBaseUrlProvider cimm2bBaseUrlProvider;
	List<String> internalCimm2bcUriList = new ArrayList<>();

	private Cimm2bBaseUrlProvider() {
		initializeCimm2BCUriList();
	}

	public void initializeCimm2BCUriList() {
		// TODO: These uris must come from system Parameters tables
		// So have the system parameters keys of these uris in a property file(comma
		// separated value)
		// Example internalCimm2bcUri=PRICE_AND_AVAILABILITY_URI,GET_USER_URI etc..
		// Load these keys into a list and dynamically pull the values from system
		// parameter table and populate the internalCimm2bcUriList variable
		internalCimm2bcUriList.add(
				CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_ORDER_API")));
		internalCimm2bcUriList.add(
				CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2B_ERP_UPDATE_ORDER_URI")));
		internalCimm2bcUriList.add(
				CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2B_ERP_DELETE_ORDER_URI")));
		internalCimm2bcUriList.add(
				CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_SHELL_ORDER_API")));
		internalCimm2bcUriList.add(
				CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPS_FREIGHT_RATE_URL")));
		internalCimm2bcUriList.add(
				CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_WIDGET_FINDALL")));
		internalCimm2bcUriList.add(
				CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_BANNERLIST"))) ;

	}

	public static Cimm2bBaseUrlProvider getInstance() {
		if (cimm2bBaseUrlProvider == null) {
			synchronized (RetryPolicy.class) {
				cimm2bBaseUrlProvider = new Cimm2bBaseUrlProvider();
			}
		}
		return cimm2bBaseUrlProvider;
	}

	public String getBaseUri(String requestBase) {
		Boolean isInternalCimm2bcUri = false;
		String baseUri = CommonUtility
				.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BCENTRAL_ACCESS_URL"));
		for (String uri : internalCimm2bcUriList) {
			if (requestBase.contains(uri)) {
				isInternalCimm2bcUri = true;
				break;
			}

		}
		if (!isInternalCimm2bcUri) {
			String aceUri = CommonUtility
					.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BCENTRAL_ACE_ACCESS_BASE_URL"));
			if (aceUri != null && !aceUri.isEmpty()) {
				baseUri = aceUri;

			}

		}
		return baseUri;
	}
}
