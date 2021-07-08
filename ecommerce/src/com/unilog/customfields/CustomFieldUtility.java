package com.unilog.customfields;

import com.unilog.utility.CommonUtility;

public class CustomFieldUtility {

	private static CustomFieldUtility customFieldUtility = null;
	public static String userCustomFieldType = "USER";
	public static String buyingCompanyCustomFieldType = "BUYING_COMPANY";
	public static String bcAddressBookCustomFieldType = "BC_ADDRESS_BOOK";
	private CustomFieldUtility(){
		
	}
	
	public static CustomFieldUtility getInstance() {
		synchronized (CustomFieldUtility.class) {
			if (customFieldUtility == null) {
				customFieldUtility = new CustomFieldUtility();
			}
		}
		return customFieldUtility;
	}
	
	
	public String buildJsonField(String inputField){
		String jsonField = "";
		String c = "";
		try{
			
		//SRC_COLUMN_NAMES := 'BRAND_NAME,BRAND_IMAGE';
		//SRC_JSON_COL_NAMES := "BrandName":"''||TB.BRAND_NAME||''","BrandImage":"''||TB.BRAND_IMAGE||''"';
			//'"BRAND_NAME":"''||TB.BRAND_NAME||''",'"BRAND_IMAGE":"''||TB.BRAND_IMAGE||''"'
			if(!CommonUtility.validateString(inputField).isEmpty()){
				String[] columnName = inputField.split(",");
				if(columnName!=null && columnName.length>0){
					for(String column:columnName){
						jsonField = jsonField + c + "\""+column+"\":\"'||TB."+column+"||'\"";
						c = ",";
					}
				}
				System.out.println("jsonField : " + jsonField);
			}
			
		}catch (Exception e) {
			jsonField = "";
			e.printStackTrace();
			
		}
		return jsonField;
	}

}
