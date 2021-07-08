package com.erp.service.cimm2bcentral.action;

import java.util.List;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;

public class Cimm2BCAssociateItemsResponse  extends Cimm2BCentralResponseEntity {
	private String customerERPId;
	private Boolean customerTypeFlag;
	private Boolean customerTemplateOverride;
	private List<Cimm2BCentralLineItem> lineItems;
	private Boolean calculateTax;
	private List<Cimm2BCentralLineItem> substituteItems;
	private List<Cimm2BCentralLineItem> associateItems;
	
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public Boolean getCustomerTypeFlag() {
		return customerTypeFlag;
	}
	public void setCustomerTypeFlag(Boolean customerTypeFlag) {
		this.customerTypeFlag = customerTypeFlag;
	}
	public Boolean getCustomerTemplateOverride() {
		return customerTemplateOverride;
	}
	public void setCustomerTemplateOverride(Boolean customerTemplateOverride) {
		this.customerTemplateOverride = customerTemplateOverride;
	}
	public List<Cimm2BCentralLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(List<Cimm2BCentralLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	public Boolean getCalculateTax() {
		return calculateTax;
	}
	public void setCalculateTax(Boolean calculateTax) {
		this.calculateTax = calculateTax;
	}
	public List<Cimm2BCentralLineItem> getSubstituteItems() {
		return substituteItems;
	}
	public void setSubstituteItems(List<Cimm2BCentralLineItem> substituteItems) {
		this.substituteItems = substituteItems;
	}
	public List<Cimm2BCentralLineItem> getAssociateItems() {
		return associateItems;
	}
	public void setAssociateItems(List<Cimm2BCentralLineItem> associateItems) {
		this.associateItems = associateItems;
	}
	
}
