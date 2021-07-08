package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralAcceptHostedTokenRequest {
	private double amount;
	private String customerProfileId;
	private String cardHolderFirstName;
	private String cardHolderLastName;
	private Cimm2BCentralAddress address;
	private Cimm2BCentralAcceptHostedPageSettings hostedPageSettings;
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCustomerProfileId() {
		return customerProfileId;
	}
	public void setCustomerProfileId(String customerProfileId) {
		this.customerProfileId = customerProfileId;
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
	public Cimm2BCentralAddress getAddress() {
		return address;
	}
	public void setAddress(Cimm2BCentralAddress address) {
		this.address = address;
	}
	public Cimm2BCentralAcceptHostedPageSettings getHostedPageSettings() {
		return hostedPageSettings;
	}
	public void setHostedPageSettings(Cimm2BCentralAcceptHostedPageSettings hostedPageSettings) {
		this.hostedPageSettings = hostedPageSettings;
	}
}
