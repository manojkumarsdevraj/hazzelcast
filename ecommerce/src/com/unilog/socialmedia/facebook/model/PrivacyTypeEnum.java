package com.unilog.socialmedia.facebook.model;

import com.google.gson.annotations.Expose;

public enum PrivacyTypeEnum {
	 @Expose EVERYONE, 
	 @Expose ALL_FRIENDS, 
	 @Expose NETWORKS_FRIENDS, 
	 @Expose FRIENDS_OF_FRIENDS, 
	 @Expose SOME_FRIENDS, 
	 @Expose NO_FRIENDS, 
	 @Expose SELF, 
	 @Expose CUSTOM, 
	 @Expose EMPTY;
	
	
	public static PrivacyTypeEnum getInstance(String privacyTypeString)
	{
	     if (privacyTypeString == null) {
	       return null;
	     }
	     if (privacyTypeString.equals("")) {
	      return EMPTY;
	     }
	     if (privacyTypeString.toUpperCase().equals("FRIENDS")) {
	      return ALL_FRIENDS;
	     }
	     for (PrivacyTypeEnum privacyType : values()) {
	       if (privacyType.toString().equals(privacyTypeString.toUpperCase())) {
	         return privacyType;
	       }
	     }
	     return null;
	 }
	
}
