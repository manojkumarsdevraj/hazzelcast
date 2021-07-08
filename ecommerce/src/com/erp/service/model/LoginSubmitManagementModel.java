package com.erp.service.model;

public class LoginSubmitManagementModel {
	
	private String userName;
	private String password;
	private boolean forgotPassword=false;
	private String activeCustomerId;
	
	
	public String getActiveCustomerId() {
		return activeCustomerId;
	}
	public void setActiveCustomerId(String activeCustomerId) {
		this.activeCustomerId = activeCustomerId;
	}
	public boolean isForgotPassword() {
		return forgotPassword;
	}
	public void setForgotPassword(boolean forgotPassword) {
		this.forgotPassword = forgotPassword;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
