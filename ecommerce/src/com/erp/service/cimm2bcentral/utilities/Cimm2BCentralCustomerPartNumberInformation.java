package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralCustomerPartNumberInformation {
	
	private String customerERPId;
	private String customerName;
	private ArrayList<Cimm2BCentralcpnLineItem> lineItems;
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public ArrayList<Cimm2BCentralcpnLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(ArrayList<Cimm2BCentralcpnLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	

}
