package com.unilog.geolocator.service.yahoo;

import com.unilog.database.CommonDBQuery;
import com.unilog.geolocator.service.IGeoLocatorUtil;
import com.unilog.utility.CommonUtility;

public abstract class AbstractYahooServiceImpl implements IGeoLocatorUtil {

	public static String CALL_TYPE;

	public static String GEO_API_KEY;

	public static String GEO_API_SECRET;

	public static String GEO_API_URL;

	public static String getCALL_TYPE() {
		return CALL_TYPE;
	}

	public static String getGEO_API_KEY() {
		return GEO_API_KEY;
	}

	public static String getGEO_API_SECRET() {
		return GEO_API_SECRET;
	}

	public static String getGEO_API_URL() {
		return GEO_API_URL;
	}

	public static void setCALL_TYPE(String cALL_TYPE) {
		CALL_TYPE = cALL_TYPE;
	}

	public static void setGEO_API_KEY(String gEO_API_KEY) {
		GEO_API_KEY = gEO_API_KEY;
	}

	public static void setGEO_API_SECRET(String gEO_API_SECRET) {
		GEO_API_SECRET = gEO_API_SECRET;
	}

	public static void setGEO_API_URL(String gEO_API_URL) {
		GEO_API_URL = gEO_API_URL;
	}

	private boolean initRequired;

	public AbstractYahooServiceImpl() {
		setInitRequired(true);
	}

	public void init() {
		setGEO_API_URL(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GEO_API_URL")));
		setGEO_API_KEY(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GEO_API_KEY")));
		setGEO_API_SECRET(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GEO_API_SECRET")));
		setCALL_TYPE("placefinder");
	}

	public boolean isInitRequired() {
		return initRequired;
	}

	public void setInitRequired(boolean initRequired) {
		this.initRequired = initRequired;
	}

}
