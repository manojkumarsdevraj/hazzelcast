package com.erp.service.cimm2bcentral.utilities;

import java.util.List;

public class Cimm2BCentralItemHistoryResponse {
	private String maximumQuantity;
	private String customerPrice;
	private String unitPrice;
	private String toMonth;
	private String toYear;
	
	private List<Cimm2BCentralItemHistoryResponse> salesBreaks;

	public String getMaximumQuantity() {
		return maximumQuantity;
	}

	public void setMaximumQuantity(String maximumQuantity) {
		this.maximumQuantity = maximumQuantity;
	}

	public String getCustomerPrice() {
		return customerPrice;
	}

	public void setCustomerPrice(String customerPrice) {
		this.customerPrice = customerPrice;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getToMonth() {
		return toMonth;
	}

	public void setToMonth(String toMonth) {
		this.toMonth = toMonth;
	}

	public String getToYear() {
		return toYear;
	}

	public void setToYear(String toYear) {
		this.toYear = toYear;
	}

	public List<Cimm2BCentralItemHistoryResponse> getSalesBreaks() {
		return salesBreaks;
	}

	public void setSalesBreaks(List<Cimm2BCentralItemHistoryResponse> salesBreaks) {
		this.salesBreaks = salesBreaks;
	}
	
}