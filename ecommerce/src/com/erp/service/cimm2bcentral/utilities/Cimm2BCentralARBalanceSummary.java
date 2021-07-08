package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralARBalanceSummary extends Cimm2BCentralResponseEntity {

	private String customerERPId;
	private String customerName;
	private Cimm2BCentralAddress customerAddress;
	private Double balance;
	private Double debit;
	private Double credit;
	private Double period1;
	private Double period2;
	private Double period3;
	private Double period4;
	private Double period5;
	private Double period6;
	private Double period7;
	private String billingPeriodBalance;
	private String openOrderBalance;
	private String futureBalance;
	private Double salesMTD;
	private Double salesYTD;
	private String curencyCode;
	private String termsAndCondition;
	private Double total;
	private Cimm2BCentralPeriod periodOne;
	private Cimm2BCentralPeriod periodTwo;
	private Cimm2BCentralPeriod periodThree;
	private Cimm2BCentralPeriod periodFour;
	private Cimm2BCentralPeriod periodFive;
	private Cimm2BCentralPeriod periodSix;
	private Cimm2BCentralPeriod periodSeven;
	private String lastSaleDate;
	private String lastPaymentDate;
	private String accountId;
	private String customerPONumber;
	private Double lastSaleAmount;
	private String age;
	private Double lastPaymentAmount;
	private double arCreditLimit;
	private String orderNumber;
	private String futureBalanceText;
	private String invoiceNumber;
	
	
	private String period1Text;
	private String period2Text;
	private String period3Text;
	private String period4Text;
	private String period5Text;
	
		
	private double totalBalance1;
	private double totalBalance2;
	private double totalBalance3;
	private double totalBalance4;
	private double totalBalance5;
	
	private String total1Text;
	private String total2Text;
	private String total3Text;
	private String total4Text;
	private String total5Text;
	private ArrayList<Cimm2BCentralARBalanceSummary> arBalanceSummary;
	
	public Double getLastPaymentAmount() {
		return lastPaymentAmount;
	}
	public void setLastPaymentAmount(Double lastPaymentAmount) {
		this.lastPaymentAmount = lastPaymentAmount;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getCustomerPONumber() {
		return customerPONumber;
	}
	public void setCustomerPONumber(String customerPONumber) {
		this.customerPONumber = customerPONumber;
	}
	public Double getLastSaleAmount() {
		return lastSaleAmount;
	}
	public void setLastSaleAmount(Double lastSaleAmount) {
		this.lastSaleAmount = lastSaleAmount;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Cimm2BCentralAddress getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(Cimm2BCentralAddress customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getBillingPeriodBalance() {
		return billingPeriodBalance;
	}
	public void setBillingPeriodBalance(String billingPeriodBalance) {
		this.billingPeriodBalance = billingPeriodBalance;
	}
	public String getOpenOrderBalance() {
		return openOrderBalance;
	}
	public void setOpenOrderBalance(String openOrderBalance) {
		this.openOrderBalance = openOrderBalance;
	}
	public String getFutureBalance() {
		return futureBalance;
	}
	public void setFutureBalance(String futureBalance) {
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
	public String getCurencyCode() {
		return curencyCode;
	}
	public void setCurencyCode(String curencyCode) {
		this.curencyCode = curencyCode;
	}
	public String getTermsAndCondition() {
		return termsAndCondition;
	}
	public void setTermsAndCondition(String termsAndCondition) {
		this.termsAndCondition = termsAndCondition;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Cimm2BCentralPeriod getPeriodOne() {
		return periodOne;
	}
	public void setPeriodOne(Cimm2BCentralPeriod periodOne) {
		this.periodOne = periodOne;
	}
	public Cimm2BCentralPeriod getPeriodTwo() {
		return periodTwo;
	}
	public void setPeriodTwo(Cimm2BCentralPeriod periodTwo) {
		this.periodTwo = periodTwo;
	}
	public Cimm2BCentralPeriod getPeriodThree() {
		return periodThree;
	}
	public void setPeriodThree(Cimm2BCentralPeriod periodThree) {
		this.periodThree = periodThree;
	}
	public Cimm2BCentralPeriod getPeriodFour() {
		return periodFour;
	}
	public void setPeriodFour(Cimm2BCentralPeriod periodFour) {
		this.periodFour = periodFour;
	}
	public Cimm2BCentralPeriod getPeriodFive() {
		return periodFive;
	}
	public void setPeriodFive(Cimm2BCentralPeriod periodFive) {
		this.periodFive = periodFive;
	}
	public Cimm2BCentralPeriod getPeriodSix() {
		return periodSix;
	}
	public void setPeriodSix(Cimm2BCentralPeriod periodSix) {
		this.periodSix = periodSix;
	}
	public Cimm2BCentralPeriod getPeriodSeven() {
		return periodSeven;
	}
	public void setPeriodSeven(Cimm2BCentralPeriod periodSeven) {
		this.periodSeven = periodSeven;
	}
	public String getLastSaleDate() {
		return lastSaleDate;
	}
	public void setLastSaleDate(String lastSaleDate) {
		this.lastSaleDate = lastSaleDate;
	}
	public String getLastPaymentDate() {
		return lastPaymentDate;
	}
	public void setLastPaymentDate(String lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
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
	public double getArCreditLimit() {
		return arCreditLimit;
	}
	public void setArCreditLimit(double arCreditLimit) {
		this.arCreditLimit = arCreditLimit;
	}
	public String getFutureBalanceText() {
		return futureBalanceText;
	}
	public void setFutureBalanceText(String futureBalanceText) {
		this.futureBalanceText = futureBalanceText;
	}
	public String getPeriod1Text() {
		return period1Text;
	}
	public void setPeriod1Text(String period1Text) {
		this.period1Text = period1Text;
	}
	public String getPeriod2Text() {
		return period2Text;
	}
	public void setPeriod2Text(String period2Text) {
		this.period2Text = period2Text;
	}
	public String getPeriod3Text() {
		return period3Text;
	}
	public void setPeriod3Text(String period3Text) {
		this.period3Text = period3Text;
	}
	public String getPeriod4Text() {
		return period4Text;
	}
	public void setPeriod4Text(String period4Text) {
		this.period4Text = period4Text;
	}
	public String getPeriod5Text() {
		return period5Text;
	}
	public void setPeriod5Text(String period5Text) {
		this.period5Text = period5Text;
	}
	public double getTotalBalance1() {
		return totalBalance1;
	}
	public void setTotalBalance1(double totalBalance1) {
		this.totalBalance1 = totalBalance1;
	}
	public double getTotalBalance2() {
		return totalBalance2;
	}
	public void setTotalBalance2(double totalBalance2) {
		this.totalBalance2 = totalBalance2;
	}
	public double getTotalBalance3() {
		return totalBalance3;
	}
	public void setTotalBalance3(double totalBalance3) {
		this.totalBalance3 = totalBalance3;
	}
	public double getTotalBalance4() {
		return totalBalance4;
	}
	public void setTotalBalance4(double totalBalance4) {
		this.totalBalance4 = totalBalance4;
	}
	public double getTotalBalance5() {
		return totalBalance5;
	}
	public void setTotalBalance5(double totalBalance5) {
		this.totalBalance5 = totalBalance5;
	}
	public String getTotal1Text() {
		return total1Text;
	}
	public void setTotal1Text(String total1Text) {
		this.total1Text = total1Text;
	}
	public String getTotal2Text() {
		return total2Text;
	}
	public void setTotal2Text(String total2Text) {
		this.total2Text = total2Text;
	}
	public String getTotal3Text() {
		return total3Text;
	}
	public void setTotal3Text(String total3Text) {
		this.total3Text = total3Text;
	}
	public String getTotal4Text() {
		return total4Text;
	}
	public void setTotal4Text(String total4Text) {
		this.total4Text = total4Text;
	}
	public String getTotal5Text() {
		return total5Text;
	}
	public void setTotal5Text(String total5Text) {
		this.total5Text = total5Text;
	}
	public ArrayList<Cimm2BCentralARBalanceSummary> getArBalanceSummary() {
		return arBalanceSummary;
	}
	public void setArBalanceSummary(ArrayList<Cimm2BCentralARBalanceSummary> arBalanceSummary) {
		this.arBalanceSummary = arBalanceSummary;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	
}
