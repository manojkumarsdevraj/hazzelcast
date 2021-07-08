package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralMaxRecallRequest {
	
	private String searchKeyword;
	private String userValue;
	private String documentId;
	private String orderSuffix;
	
	public String getOrderSuffix() {
		return orderSuffix;
	}
	public void setOrderSuffix(String orderSuffix) {
		this.orderSuffix = orderSuffix;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public String getUserValue() {
		return userValue;
	}
	public void setUserValue(String userValue) {
		this.userValue = userValue;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
}