package com.unilog.products.model;

import java.util.List;

public class AssociateItemsModel {
	private List<LineItemsModel> lineItems;
	private String customerERPId;
	private boolean customerTypeFlag;
	private boolean customerTemplateOverride;
	private int statusCode;
	private String statusMessage;
	
	public List<LineItemsModel> getLineItems() {
		return lineItems;
	}
	public void setLineItems(List<LineItemsModel> lineItems) {
		this.lineItems = lineItems;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public boolean isCustomerTypeFlag() {
		return customerTypeFlag;
	}
	public void setCustomerTypeFlag(boolean customerTypeFlag) {
		this.customerTypeFlag = customerTypeFlag;
	}
	public boolean isCustomerTemplateOverride() {
		return customerTemplateOverride;
	}
	public void setCustomerTemplateOverride(boolean customerTemplateOverride) {
		this.customerTemplateOverride = customerTemplateOverride;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
}
