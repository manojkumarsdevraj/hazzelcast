package com.erp.service.cimm2bcentral.utilities;

import java.util.List;

public class Cimm2BCentralARBalanceDetails extends Cimm2BCentralResponseEntity{

	private String customerERPId;
	private String documentNumber;
	private String applyToNumber;
	private Integer lineNumber;
	private String branch;
	private Integer documentType;
	private Integer detailedDocumentType;
	private Double balance;
	private Double debit;
	private Double credit;
	private Integer documentAge;
	private Integer pastDue;
	private Integer cashBatch;
	private Integer uniqueId;
	private String dueDate;
	private String documentDate;
	private Double salesTotal;
	private String invoicedDate;
	private String invoiceNumber;
	private String orderNumber;
	private String orderedDate;
	private double dueBalance;
	private double highBalance;
	private String termsTypeDescription;
	private String documentDiscountDate;
	private String customerPONumber;
	
	
	private List<Cimm2BCentralARBalanceSummary> arBalanceSummary;
	
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	public String getApplyToNumber() {
		return applyToNumber;
	}
	public void setApplyToNumber(String applyToNumber) {
		this.applyToNumber = applyToNumber;
	}
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public Integer getDocumentType() {
		return documentType;
	}
	public void setDocumentType(Integer documentType) {
		this.documentType = documentType;
	}
	public Integer getDetailedDocumentType() {
		return detailedDocumentType;
	}
	public void setDetailedDocumentType(Integer detailedDocumentType) {
		this.detailedDocumentType = detailedDocumentType;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getDebit() {
		return debit;
	}
	public void setDebit(Double debit) {
		this.debit = debit;
	}
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	public Integer getDocumentAge() {
		return documentAge;
	}
	public void setDocumentAge(Integer documentAge) {
		this.documentAge = documentAge;
	}
	public Integer getPastDue() {
		return pastDue;
	}
	public void setPastDue(Integer pastDue) {
		this.pastDue = pastDue;
	}
	public Integer getCashBatch() {
		return cashBatch;
	}
	public void setCashBatch(Integer cashBatch) {
		this.cashBatch = cashBatch;
	}
	public Integer getUniqueId() {
		return uniqueId;
	}
	public Double getSalesTotal() {
		return salesTotal;
	}
	public void setSalesTotal(Double salesTotal) {
		this.salesTotal = salesTotal;
	}
	public String getInvoicedDate() {
		return invoicedDate;
	}
	public void setInvoicedDate(String invoicedDate) {
		this.invoicedDate = invoicedDate;
	}
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
	public String getOrderedDate() {
		return orderedDate;
	}
	public void setOrderedDate(String orderedDate) {
		this.orderedDate = orderedDate;
	}
	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(String documentDate) {
		this.documentDate = documentDate;
	}
	public List<Cimm2BCentralARBalanceSummary> getArBalanceSummary() {
		return arBalanceSummary;
	}
	public void setArBalanceSummary(List<Cimm2BCentralARBalanceSummary> arBalanceSummary) {
		this.arBalanceSummary = arBalanceSummary;
	}
	public double getDueBalance() {
		return dueBalance;
	}
	public void setDueBalance(double dueBalance) {
		this.dueBalance = dueBalance;
	}
	public double getHighBalance() {
		return highBalance;
	}
	public void setHighBalance(double highBalance) {
		this.highBalance = highBalance;
	}
	public String getTermsTypeDescription() {
		return termsTypeDescription;
	}
	public void setTermsTypeDescription(String termsTypeDescription) {
		this.termsTypeDescription = termsTypeDescription;
	}
	public String getDocumentDiscountDate() {
		return documentDiscountDate;
	}
	public void setDocumentDiscountDate(String documentDiscountDate) {
		this.documentDiscountDate = documentDiscountDate;
	}
	public String getCustomerPONumber() {
		return customerPONumber;
	}
	public void setCustomerPONumber(String customerPONumber) {
		this.customerPONumber = customerPONumber;
	}
	
}