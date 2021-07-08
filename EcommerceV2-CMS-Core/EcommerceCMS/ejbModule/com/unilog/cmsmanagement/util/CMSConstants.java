package com.unilog.cmsmanagement.util;

import com.unilog.utility.CommonUtility;

public final class CMSConstants {  

	 private CMSConstants() {
        // restrict instantiation
}

public static final String taxonomy = "/taxonomy";
public static final String taxonomyTreePath = "/taxonomytree";
public static final String mnufacturerPath = "/manufacturer";
public static final String brandPath = "/brand";
public static final String staticPage = "/staticpage";
public static final String context = "/admin";
public static final String list = "/findAll";
public static final String find = "/find/";


public  static String encodeString(String value){
	try {
		if(value!=null){
			byte spaceValue[]={-96};
			if(value.contains(new String(spaceValue))){
				byte replaceValue[]={32};
				value=value.replaceAll(new String(spaceValue), new String(replaceValue));
			}
			return value;
		}
		//return new String(value.getBytes(),"ISO-8859-15");
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return "";
}


public static String getWarFileName(String path){
	String warFile = "";
	if(CommonUtility.validateString(path).length()>0) {
		String str[] = path.split("/");
		if(str.length > 1){
			warFile = "/" + str[1] + ".war";
		}
		warFile = warFile + "/";
		for(int i=2; i<str.length; i++){
			
			warFile = warFile + str[i] + "/";
		}
	}
	return warFile;
}


}
