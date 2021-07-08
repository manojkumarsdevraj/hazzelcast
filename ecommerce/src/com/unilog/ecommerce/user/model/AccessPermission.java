package com.unilog.ecommerce.user.model;

public class AccessPermission {
	private String userRole;
	private String submitPo;
	private String submitCc;
	
	public AccessPermission(String userRole, String submitPo, String submitCc) {
		this.userRole = userRole;
		this.submitPo = submitPo;
		this.submitCc = submitCc;
	}
		
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getSubmitPo() {
		return submitPo;
	}
	public void setSubmitPo(String submitPo) {
		this.submitPo = submitPo;
	}
	public String getSubmitCc() {
		return submitCc;
	}
	public void setSubmitCc(String submitCc) {
		this.submitCc = submitCc;
	}
}
