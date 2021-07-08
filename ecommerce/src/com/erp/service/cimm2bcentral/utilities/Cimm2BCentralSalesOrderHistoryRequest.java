package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralSalesOrderHistoryRequest {
	
	private String customerERPId;
 	private String fromDate;
  	private String toDate;
  	private String textSearch;
  	private String warehouseLocation;
  	private int recordLimit;
  	private String sortDirection;
  	private String orderType;
  	private int pageSize;
	private boolean rowDetails;
	private String startRowId;
	private String orderNumber;
  	
  	public String getWarehouseLocation() {
		return warehouseLocation;
	}
	public void setWarehouseLocation(String warehouseLocation) {
		this.warehouseLocation = warehouseLocation;
	}
	public int getRecordLimit() {
		return recordLimit;
	}
	public void setRecordLimit(int recordLimit) {
		this.recordLimit = recordLimit;
	}
	public String getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
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
	public String getTextSearch() {
		return textSearch;
	}
	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public boolean isRowDetails() {
		return rowDetails;
	}
	public void setRowDetails(boolean rowDetails) {
		this.rowDetails = rowDetails;
	}
	public String getStartRowId() {
		return startRowId;
	}
	public void setStartRowId(String startRowId) {
		this.startRowId = startRowId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

}
