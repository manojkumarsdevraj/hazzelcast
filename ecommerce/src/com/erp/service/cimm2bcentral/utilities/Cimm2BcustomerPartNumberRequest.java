package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BcustomerPartNumberRequest {
	private String customerERPId;
	private ArrayList<Cimm2BCentralcpnLineItem> lineItems;
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public ArrayList<Cimm2BCentralcpnLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(ArrayList<Cimm2BCentralcpnLineItem> lineItems) {
		this.lineItems = lineItems;
	}
}

