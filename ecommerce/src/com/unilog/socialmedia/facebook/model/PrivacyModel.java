package com.unilog.socialmedia.facebook.model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class PrivacyModel {
	  
	  @Expose private PrivacyTypeEnum value;

	  @Expose private PrivacyTypeEnum friends;

	  @Expose private ArrayList<String> networks;

	  @Expose private ArrayList<String> allow;

	  @Expose private ArrayList<String> deny;

	  @Expose private ArrayList<String> description;
	  

	public PrivacyTypeEnum getValue() {
		return value;
	}

	public void setValue(PrivacyTypeEnum value) {
		this.value = value;
	}

	public PrivacyTypeEnum getFriends() {
		return friends;
	}

	public void setFriends(PrivacyTypeEnum friends) {
		this.friends = friends;
	}

	public ArrayList<String> getNetworks() {
		return networks;
	}

	public void setNetworks(ArrayList<String> networks) {
		this.networks = networks;
	}

	public ArrayList<String> getAllow() {
		return allow;
	}

	public void setAllow(ArrayList<String> allow) {
		this.allow = allow;
	}

	public ArrayList<String> getDeny() {
		return deny;
	}

	public void setDeny(ArrayList<String> deny) {
		this.deny = deny;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public void setDescription(ArrayList<String> description) {
		this.description = description;
	}
	  
	  
}
