package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralARBalanceDetailsRequest {

	private String customerERPId;
	private String agingDate;
	private String invoiceNumber;
	
	
		
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
	public String getAgingDate() {
		return agingDate;
	}
	public void setAgingDate(String agingDate) {
		this.agingDate = agingDate;
	}
}
