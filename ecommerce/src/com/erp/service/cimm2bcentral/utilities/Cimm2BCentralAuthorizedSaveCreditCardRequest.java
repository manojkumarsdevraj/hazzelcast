package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralAuthorizedSaveCreditCardRequest {
	
	private String dataDescriptor;
	private String dataValue;
	private String customerERPId;
	private String customerProfileId;
	private Cimm2BCentralAddress address;
	private String cardHolderFirstName;
	private String cardHolderLastName;
	
	
	
	
	public String getCustomerProfileId() {
		return customerProfileId;
	}
	public void setCustomerProfileId(String customerProfileId) {
		this.customerProfileId = customerProfileId;
	}
	public String getDataDescriptor() {
		return dataDescriptor;
	}
	public String getDataValue() {
		return dataValue;
	}
	
	public void setDataDescriptor(String dataDescriptor) {
		this.dataDescriptor = dataDescriptor;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
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
	
	

}
