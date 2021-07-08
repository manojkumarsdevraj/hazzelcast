package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralAuthorizeCreditCardRequest {
	
	private String dataDescriptor;
	private String dataValue;
	private double amount;
	private String customerERPId;	
	private String paymentAccountId;
	private String customerProfileId;
	private Cimm2BCentralAddress address;
	private String cardHolderFirstName;
	private String cardHolderLastName;
	private String cvv;
	
	public String getDataDescriptor() {
		return dataDescriptor;
	}
	public void setDataDescriptor(String dataDescriptor) {
		this.dataDescriptor = dataDescriptor;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getPaymentAccountId() {
		return paymentAccountId;
	}
	public void setPaymentAccountId(String paymentAccountId) {
		this.paymentAccountId = paymentAccountId;
	}
	public String getCustomerProfileId() {
		return customerProfileId;
	}
	public void setCustomerProfileId(String customerProfileId) {
		this.customerProfileId = customerProfileId;
	}
	public Cimm2BCentralAddress getAddress() {
		return address;
	}
	public void setAddress(Cimm2BCentralAddress address) {
		this.address = address;
	}
	public String getCardHolderFirstName() {
		return cardHolderFirstName;
	}
	public void setCardHolderFirstName(String cardHolderFirstName) {
		this.cardHolderFirstName = cardHolderFirstName;
	}
	public String getCardHolderLastName() {
		return cardHolderLastName;
	}
	public void setCardHolderLastName(String cardHolderLastName) {
		this.cardHolderLastName = cardHolderLastName;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	
}
