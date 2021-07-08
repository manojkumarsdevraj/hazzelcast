package com.unilog.ecommerce.validataion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldValidator {
	public static boolean isValidEmail(String emailAddress) {
        boolean isValid = false;
        if (isValidString(emailAddress)){
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
	
	public static boolean isValidString(String value) {
		boolean isValid = false;
		if(value != null && value.trim().length() > 0) {
			isValid = true;
		}
		return isValid;
	}
	
	public static boolean isValidName(String name) {
		boolean isValid = false;
		if(name != null && name.trim().length() > 0 && name.matches("^[^\\d].*")) {
			isValid = true;
		}
		return isValid;
	}
	
	public static boolean isValidPhoneNumber(String phoneNumber) {
		boolean isValid = false;
		if(phoneNumber != null && phoneNumber.trim().length() > 0 && phoneNumber.matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")) {
			isValid = true;
		}
		return isValid;
	}
	
	public static boolean isValidZipCode(String zipCode) {
		boolean isValid = false;
		if(zipCode != null && zipCode.trim().length() > 0 && zipCode.matches("^[0-9]{5}(?:-[0-9]{4})?$")) {
			isValid = true;
		}
		return isValid;
	}
	
	public static boolean isValidPassword(String password) {
		//^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$  ----- RegEx for minimum of 8 characters, Number, special character, lower and upper case letters
		boolean isValid = false;
		if (password != null && password.trim().length() > 0 && password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")) {
			isValid = true;
		}
		return isValid;
	}
}
