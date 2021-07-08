package com.erp.service.cimmesb.action;

import java.util.ArrayList;
import java.util.List;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralStatus;

public class Cimm2BCentralSalesOrderItems {
	private ArrayList<Cimm2BCentralLineItem> lineItems;
	private String customerERPId;
	private List<Cimm2BCentralSalesOrderItems> details;
	private List<Cimm2BCentralStatus> status;

	public ArrayList<Cimm2BCentralLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(ArrayList<Cimm2BCentralLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	public List<Cimm2BCentralSalesOrderItems> getDetails() {
		return details;
	}
	public void setDetails(List<Cimm2BCentralSalesOrderItems> details) {
		this.details = details;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public List<Cimm2BCentralStatus> getStatus() {
		return status;
	}
	public void setStatus(List<Cimm2BCentralStatus> status) {
		this.status = status;
	}
}
