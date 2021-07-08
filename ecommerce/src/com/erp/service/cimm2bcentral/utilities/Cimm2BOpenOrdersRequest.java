package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BOpenOrdersRequest {
	private String customerERPId;
	private String fromDate;
	private String toDate;
	private String warehouseLocation;
	
	
	public String getWarehouseLocation() {
		return warehouseLocation;
	}
	public void setWarehouseLocation(String warehouseLocation) {
		this.warehouseLocation = warehouseLocation;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	
}
