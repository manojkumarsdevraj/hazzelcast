package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralItemHistoryRequest {

	private String customerERPId;
	private String fromMonth;
	private String fromYear;
	private String textSearch;
	private String toMonth;
	private String toYear;

	public String getCustomerERPId() {
		return customerERPId;
	}

	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}

	public String getFromMonth() {
		return fromMonth;
	}

	public void setFromMonth(String fromMonth) {
		this.fromMonth = fromMonth;
	}

	public String getFromYear() {
		return fromYear;
	}

	public void setFromYear(String fromYear) {
		this.fromYear = fromYear;
	}

	public String getTextSearch() {
		return textSearch;
	}

	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
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
}
