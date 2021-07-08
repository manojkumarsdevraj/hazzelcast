package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralPriceAndAvailability extends Cimm2BCentralResponseEntity{

	private String customerERPId;
	private ArrayList<Cimm2BCentralItem> items;
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public ArrayList<Cimm2BCentralItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<Cimm2BCentralItem> items) {
		this.items = items;
	}
}
