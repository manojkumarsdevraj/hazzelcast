package com.erp.service.cimm2bcentral.utilities;
//cpe
public class Cimm2BCentralGetInvoiceList {
	
	private String invoiceNumber;
    private String orderNumber;
    private double totalAmount;
    private double tax;
    private String poNumber;
    private double freightAmount;
    private String documentLinks;
    private String lineItems;
    private String invoicedDate;
    private String orderedDate;
    private String dueDate;
    private String invoiceType;
    private int invoiceSuffix;
    private double discountAmount;
    private String transactionType;
    private String transactionCodeType;
    
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public double getFreightAmount() {
		return freightAmount;
	}
	public void setFreightAmount(double freightAmount) {
		this.freightAmount = freightAmount;
	}
	public String getDocumentLinks() {
		return documentLinks;
	}
	public void setDocumentLinks(String documentLinks) {
		this.documentLinks = documentLinks;
	}
	public String getLineItems() {
		return lineItems;
	}
	public void setLineItems(String lineItems) {
		this.lineItems = lineItems;
	}
	public String getInvoicedDate() {
		return invoicedDate;
	}
	public void setInvoicedDate(String invoicedDate) {
		this.invoicedDate = invoicedDate;
	}
	public String getOrderedDate() {
		return orderedDate;
	}
	public void setOrderedDate(String orderedDate) {
		this.orderedDate = orderedDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public int getInvoiceSuffix() {
		return invoiceSuffix;
	}
	public void setInvoiceSuffix(int invoiceSuffix) {
		this.invoiceSuffix = invoiceSuffix;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionCodeType() {
		return transactionCodeType;
	}
	public void setTransactionCodeType(String transactionCodeType) {
		this.transactionCodeType = transactionCodeType;
	}
}
