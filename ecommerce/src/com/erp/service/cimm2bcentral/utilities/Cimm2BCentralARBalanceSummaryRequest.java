package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralARBalanceSummaryRequest {

	private String customerERPId;
	private String invoiceNumber;
	private String cylinderGroup;
	private String agingDate;
	private Boolean dataUpto30Days;
	private Boolean dataUpto60Days;
	private Boolean dataUpto90Days;
	private Boolean dataUpto120Days;
	
	private String summaryMonth;
	private String summaryYear;
	
	private String textSearch;
	private String asOfDate;
	
	private boolean includeARBalanceAdditionalInfo;
	
	
	public String getAsOfDate() {
		return asOfDate;
	}
	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}
	public String getTextSearch() {
		return textSearch;
	}
	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}
	public String getSummaryMonth() {
		return summaryMonth;
	}
	public void setSummaryMonth(String summaryMonth) {
		this.summaryMonth = summaryMonth;
	}
	public String getSummaryYear() {
		return summaryYear;
	}
	public void setSummaryYear(String summaryYear) {
		this.summaryYear = summaryYear;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getCylinderGroup() {
		return cylinderGroup;
	}
	public void setCylinderGroup(String cylinderGroup) {
		this.cylinderGroup = cylinderGroup;
	}
	public String getAgingDate() {
		return agingDate;
	}
	public void setAgingDate(String agingDate) {
		this.agingDate = agingDate;
	}
	public Boolean getDataUpto30Days() {
		return dataUpto30Days;
	}
	public void setDataUpto30Days(Boolean dataUpto30Days) {
		this.dataUpto30Days = dataUpto30Days;
	}
	public Boolean getDataUpto60Days() {
		return dataUpto60Days;
	}
	public void setDataUpto60Days(Boolean dataUpto60Days) {
		this.dataUpto60Days = dataUpto60Days;
	}
	public Boolean getDataUpto90Days() {
		return dataUpto90Days;
	}
	public void setDataUpto90Days(Boolean dataUpto90Days) {
		this.dataUpto90Days = dataUpto90Days;
	}
	public Boolean getDataUpto120Days() {
		return dataUpto120Days;
	}
	public void setDataUpto120Days(Boolean dataUpto120Days) {
		this.dataUpto120Days = dataUpto120Days;
	}
	public boolean isIncludeARBalanceAdditionalInfo() {
		return includeARBalanceAdditionalInfo;
	}
	public void setIncludeARBalanceAdditionalInfo(boolean includeARBalanceAdditionalInfo) {
		this.includeARBalanceAdditionalInfo = includeARBalanceAdditionalInfo;
	}
	
}
