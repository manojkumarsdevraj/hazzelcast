package com.unilog.searchconfig;

import java.util.LinkedHashMap;

public class SearchConfigUtility {

	private static String queryFields = null;
	static {
		searchQueryFields();
	}
	
	public static void searchQueryFields(){
		try{
			queryFields = SearchConfigDAO.getQueryFields(0);
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
	}

	public static String getQueryFields() {
		return queryFields;
	}

	public static void setQueryFields(String queryFields) {
		SearchConfigUtility.queryFields = queryFields;
	}
	
}
