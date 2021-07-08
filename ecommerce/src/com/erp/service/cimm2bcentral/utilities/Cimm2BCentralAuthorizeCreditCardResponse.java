package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralAuthorizeCreditCardResponse {
	private String authorizationCode;
	private String avsResultCode;
	private String cvvResultCode;
	private String cavvResultCode;
	private String transactionId;
	private String transactionHash;
	private String customerProfileId;
	private String paymentStatus;
	private String description;
	private Cimm2BCentralProfileList paymentProfileList;
	
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getAvsResultCode() {
		return avsResultCode;
	}
	public void setAvsResultCode(String avsResultCode) {
		this.avsResultCode = avsResultCode;
	}
	public String getCvvResultCode() {
		return cvvResultCode;
	}
	public void setCvvResultCode(String cvvResultCode) {
		this.cvvResultCode = cvvResultCode;
	}
	public String getCavvResultCode() {
		return cavvResultCode;
	}
	public void setCavvResultCode(String cavvResultCode) {
		this.cavvResultCode = cavvResultCode;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionHash() {
		return transactionHash;
	}
	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}
	public String getCustomerProfileId() {
		return customerProfileId;
	}
	public void setCustomerProfileId(String customerProfileId) {
		this.customerProfileId = customerProfileId;
	}
	public Cimm2BCentralProfileList getPaymentProfileList() {
		return paymentProfileList;
	}
	public void setPaymentProfileList(Cimm2BCentralProfileList paymentProfileList) {
		this.paymentProfileList = paymentProfileList;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
