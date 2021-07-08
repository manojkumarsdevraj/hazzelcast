package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralAuthenticationRequest {
	private String username;
	private String password;
	private boolean forgotPassword;
	private String newPassword;
	private Cimm2BCentralContact contact;

	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public Cimm2BCentralContact getContact() {
		return contact;
	}
	public void setContact(Cimm2BCentralContact contact) {
		this.contact = contact;
	}
	public boolean isForgotPassword() {
		return forgotPassword;
	}
	public void setForgotPassword(boolean forgotPassword) {
		this.forgotPassword = forgotPassword;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
}
