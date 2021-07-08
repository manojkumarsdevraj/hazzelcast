package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralGetInvoiceRecallRequest {

	private String customerERPId;
	private String invoiceNumber;
	private Cimm2BCentralDocumentDetail documentLinks;
	private String orderNumber;
	
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public Cimm2BCentralDocumentDetail getDocumentLinks() {
		return documentLinks;
	}
	public void setDocumentLinks(Cimm2BCentralDocumentDetail documentLinks) {
		this.documentLinks = documentLinks;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	
}

