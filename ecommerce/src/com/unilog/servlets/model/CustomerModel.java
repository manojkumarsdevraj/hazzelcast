package com.unilog.servlets.model;

public class CustomerModel {
	private String buyingCompanyId;
	private String entityId;
	public void setBuyingCompanyId(String buyingCompanyId) {
		this.buyingCompanyId = buyingCompanyId;
	}
	public String getBuyingCompanyId() {
		return buyingCompanyId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getEntityId() {
		return entityId;
	}
}
