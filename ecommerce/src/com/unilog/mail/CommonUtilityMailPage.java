package com.unilog.mail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtilityMailPage {
	
	public static String validateString(String s)
	{
		try{
			if(s==null){
				s="";
			}else{
				if(s.trim().equalsIgnoreCase("NAN")){
					s="";
				}else{
					s = s.trim();
				}
			}
		}catch (Exception e) {
			
			s = "";
		}
		return s;
	}
	public static boolean validateEmail(String emailAddress)
	{
		boolean isValid = false;
		if (validateString(emailAddress).length()>0){
			String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[_A-Z-a-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
			CharSequence inputStr = emailAddress;  
			Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
			Matcher matcher = pattern.matcher(inputStr);  
			if(matcher.matches()){  
				isValid = true;  
			}  
		}
		return isValid;
	}
	
	public static int validateNumber(String num)
	{
		int intNum = 0;
		try
		{
			intNum = Integer.parseInt(num);
		}
		catch (Exception e) {
			intNum = 0;
		}
		return intNum;
	}

}
