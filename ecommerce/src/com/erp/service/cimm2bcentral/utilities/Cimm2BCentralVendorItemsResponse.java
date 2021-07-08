package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralVendorItemsResponse extends Cimm2BCentralResponseEntity{
	private String customerERPId;
	private ArrayList<Cimm2BCentralVendorLineItems> lineItems;
	
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public ArrayList<Cimm2BCentralVendorLineItems> getLineItems() {
		return lineItems;
	}
	public void setLineItems(ArrayList<Cimm2BCentralVendorLineItems> lineItems) {
		this.lineItems = lineItems;
	}
	
}
