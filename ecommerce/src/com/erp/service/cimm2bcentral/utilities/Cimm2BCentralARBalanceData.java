package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;
import java.util.List;

public class Cimm2BCentralARBalanceData extends Cimm2BCentralResponseEntity{

	private String customerERPId;
	private String documentNumber;
	private String applyToNumber;
	private Integer lineNumber;
	private String branch;
	private Integer documentType;
	private Integer detailedDocumentType;
	private Double debit;
	private Double credit;
	private Integer documentAge;
	private Integer pastDue;
	private Integer cashBatch;
	private Integer uniqueId;
	private String dueDate;
	private String documentDate;
	
	private Double balance;
	private Double period1;
	private Double period2;
	private Double period3;
	private Double period4;
	private Double period5;
	private Double period6;
	private Double period7;
	private Double openOrderBalance;
	private Double futureBalance;
	private Double salesMTD;
	private Double salesYTD;
	private String lastSaleDate;
	private String termsAndCondition;
	private Double total;
	private Double sixMonthHigh;
	private Double sixMonthAverage;
	private Double arCreditLimit;
	private Double arCreditAvail;
	private Double paymentDays;
	private Double lastSaleAmount;
	private Double lastPaymentAmount;
	private String lastPaymentDate;
	private String endDate;
	private ArrayList<Cimm2BCentralARBalanceSummary> arBalanceSummary;
	private List<Cimm2BCentralARBalanceDetails> details = new ArrayList<Cimm2BCentralARBalanceDetails>();
	private Double arTotal;
	private String dateOfFirstSale;
	
	public Double getArTotal() {
		return arTotal;
	}
	public void setArTotal(Double arTotal) {
		this.arTotal = arTotal;
	}
	public List<Cimm2BCentralARBalanceDetails> getDetails() {
		return details;
	}
	public void setDetails(List<Cimm2BCentralARBalanceDetails> details) {
		this.details = details;
	}
	public ArrayList<Cimm2BCentralARBalanceSummary> getArBalanceSummary() {
		return arBalanceSummary;
	}
	public void setArBalanceSummary(
			ArrayList<Cimm2BCentralARBalanceSummary> arBalanceSummary) {
		this.arBalanceSummary = arBalanceSummary;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Double getPeriod1() {
		return period1;
	}
	public void setPeriod1(Double period1) {
		this.period1 = period1;
	}
	public Double getPeriod2() {
		return period2;
	}
	public void setPeriod2(Double period2) {
		this.period2 = period2;
	}
	public Double getPeriod3() {
		return period3;
	}
	public void setPeriod3(Double period3) {
		this.period3 = period3;
	}
	public Double getPeriod4() {
		return period4;
	}
	public void setPeriod4(Double period4) {
		this.period4 = period4;
	}
	public Double getOpenOrderBalance() {
		return openOrderBalance;
	}
	public void setOpenOrderBalance(Double openOrderBalance) {
		this.openOrderBalance = openOrderBalance;
	}
	public Double getFutureBalance() {
		return futureBalance;
	}
	public void setFutureBalance(Double futureBalance) {
		this.futureBalance = futureBalance;
	}
	public Double getSalesMTD() {
		return salesMTD;
	}
	public void setSalesMTD(Double salesMTD) {
		this.salesMTD = salesMTD;
	}
	public Double getSalesYTD() {
		return salesYTD;
	}
	public void setSalesYTD(Double salesYTD) {
		this.salesYTD = salesYTD;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getSixMonthHigh() {
		return sixMonthHigh;
	}
	public void setSixMonthHigh(Double sixMonthHigh) {
		this.sixMonthHigh = sixMonthHigh;
	}
	public Double getSixMonthAverage() {
		return sixMonthAverage;
	}
	public void setSixMonthAverage(Double sixMonthAverage) {
		this.sixMonthAverage = sixMonthAverage;
	}
	public Double getArCreditLimit() {
		return arCreditLimit;
	}
	public void setArCreditLimit(Double arCreditLimit) {
		this.arCreditLimit = arCreditLimit;
	}
	public Double getArCreditAvail() {
		return arCreditAvail;
	}
	public void setArCreditAvail(Double arCreditAvail) {
		this.arCreditAvail = arCreditAvail;
	}
	public Double getPaymentDays() {
		return paymentDays;
	}
	public void setPaymentDays(Double paymentDays) {
		this.paymentDays = paymentDays;
	}
	public Double getLastSaleAmount() {
		return lastSaleAmount;
	}
	public void setLastSaleAmount(Double lastSaleAmount) {
		this.lastSaleAmount = lastSaleAmount;
	}
	public Double getLastPaymentAmount() {
		return lastPaymentAmount;
	}
	public void setLastPaymentAmount(Double lastPaymentAmount) {
		this.lastPaymentAmount = lastPaymentAmount;
	}
	public String getLastSaleDate() {
		return lastSaleDate;
	}
	public void setLastSaleDate(String lastSaleDate) {
		this.lastSaleDate = lastSaleDate;
	}
	public String getTermsAndCondition() {
		return termsAndCondition;
	}
	public void setTermsAndCondition(String termsAndCondition) {
		this.termsAndCondition = termsAndCondition;
	}
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
	public String getLastPaymentDate() {
		return lastPaymentDate;
	}
	public void setLastPaymentDate(String lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}
	public String getDateOfFirstSale() {
		return dateOfFirstSale;
	}
	public void setDateOfFirstSale(String dateOfFirstSale) {
		this.dateOfFirstSale = dateOfFirstSale;
	}
	public Double getPeriod5() {
		return period5;
	}
	public void setPeriod5(Double period5) {
		this.period5 = period5;
	}
	public Double getPeriod6() {
		return period6;
	}
	public void setPeriod6(Double period6) {
		this.period6 = period6;
	}
	public Double getPeriod7() {
		return period7;
	}
	public void setPeriod7(Double period7) {
		this.period7 = period7;
	}
	
}