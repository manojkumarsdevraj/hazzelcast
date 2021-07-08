package com.erp.service.cimm2bcentral.utilities;

import java.util.List;

public class Cimm2BCentralAuthorizeSaveCreditCardResponse {
	
	private boolean saveCard;
	private String customerProfileId;
	private List<String> customerPaymentIdList;
	
	
	public boolean isSaveCard() {
		return saveCard;
	}
	public String getCustomerProfileId() {
		return customerProfileId;
	}
	public List<String> getCustomerPaymentIdList() {
		return customerPaymentIdList;
	}
	public void setSaveCard(boolean saveCard) {
		this.saveCard = saveCard;
	}
	public void setCustomerProfileId(String customerProfileId) {
		this.customerProfileId = customerProfileId;
	}
	public void setCustomerPaymentIdList(List<String> customerPaymentIdList) {
		this.customerPaymentIdList = customerPaymentIdList;
	}
	
}
