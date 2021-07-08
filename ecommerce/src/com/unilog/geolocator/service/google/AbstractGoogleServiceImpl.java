package com.unilog.geolocator.service.google;

import com.unilog.database.CommonDBQuery;
import com.unilog.geolocator.service.IGeoLocatorUtil;
import com.unilog.utility.CommonUtility;

public abstract class AbstractGoogleServiceImpl implements IGeoLocatorUtil {

	public static String GEO_API_KEY;

	public static String GEO_API_URL;

	public static final String URL_PARAMETER_ADDRESS = "address";

	public static final String URL_PARAMETER_COMPONENTS = "components";

	public static final String URL_PARAMETER_KEY = "key";

	public static String getGEO_API_KEY() {
		return GEO_API_KEY;
	}
	public static String getGEO_API_URL() {
		return GEO_API_URL;
	}
	public static void setGEO_API_KEY(String gEO_API_KEY) {
		GEO_API_KEY = gEO_API_KEY;
	}

	public static void setGEO_API_URL(String gEO_API_URL) {
		GEO_API_URL = gEO_API_URL;
	}

	private boolean initRequired;

	public AbstractGoogleServiceImpl() {
		setInitRequired(true);
	}

	public void init() {
		setGEO_API_URL(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GEO_API_URL")));
		setGEO_API_KEY(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GEO_API_KEY")));
	}

	public boolean isInitRequired() {
		return initRequired;
	}

	public void setInitRequired(boolean initRequired) {
		this.initRequired = initRequired;
	}

}
