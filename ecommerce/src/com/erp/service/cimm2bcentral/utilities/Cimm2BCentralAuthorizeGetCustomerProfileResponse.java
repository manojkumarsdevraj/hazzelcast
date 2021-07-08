package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralAuthorizeGetCustomerProfileResponse {
	
	private String customerERPId;
	private String customerProfileId;
	private ArrayList<Cimm2BCentralCustomerCard> paymentProfileList;
	
	
	public String getCustomerERPId() {
		return customerERPId;
	}
	public String getCustomerProfileId() {
		return customerProfileId;
	}
	public ArrayList<Cimm2BCentralCustomerCard> getPaymentProfileList() {
		return paymentProfileList;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public void setCustomerProfileId(String customerProfileId) {
		this.customerProfileId = customerProfileId;
	}
	public void setPaymentProfileList(
			ArrayList<Cimm2BCentralCustomerCard> paymentProfileList) {
		this.paymentProfileList = paymentProfileList;
	}
	
	

}