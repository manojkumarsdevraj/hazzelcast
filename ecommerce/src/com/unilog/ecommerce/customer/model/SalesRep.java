package com.unilog.ecommerce.customer.model;

public class SalesRep {
	private String externalId;
	private String emailId;
	
	public SalesRep() {}
	
	public SalesRep(String externalId, String emailId) {
		this.externalId = externalId;
		this.emailId = emailId;
	}
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Override
	public String toString() {
		return "SalesRep [externalId=" + externalId + ", emailId=" + emailId + "]";
	}
	
}
