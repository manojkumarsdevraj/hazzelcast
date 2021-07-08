package com.unilog.avalara.model;

public class TaxResponse {
	private String ResultCode;
	private String DocDate;
	private String DocCode;
	private double TotalAmount;
	private double TotalDiscount;
	private double TotalExemption;
	private double TotalTaxable;
	private double TotalTax;
	private double TotalTaxCalculated;
	public String getResultCode() {
		return ResultCode;
	}
	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}
	public String getDocDate() {
		return DocDate;
	}
	public void setDocDate(String docDate) {
		DocDate = docDate;
	}
	public String getDocCode() {
		return DocCode;
	}
	public void setDocCode(String docCode) {
		DocCode = docCode;
	}
	public double getTotalAmount() {
		return TotalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		TotalAmount = totalAmount;
	}
	public double getTotalDiscount() {
		return TotalDiscount;
	}
	public void setTotalDiscount(double totalDiscount) {
		TotalDiscount = totalDiscount;
	}
	public double getTotalExemption() {
		return TotalExemption;
	}
	public void setTotalExemption(double totalExemption) {
		TotalExemption = totalExemption;
	}
	public double getTotalTaxable() {
		return TotalTaxable;
	}
	public void setTotalTaxable(double totalTaxable) {
		TotalTaxable = totalTaxable;
	}
	public double getTotalTax() {
		return TotalTax;
	}
	public void setTotalTax(double totalTax) {
		TotalTax = totalTax;
	}
	public double getTotalTaxCalculated() {
		return TotalTaxCalculated;
	}
	public void setTotalTaxCalculated(double totalTaxCalculated) {
		TotalTaxCalculated = totalTaxCalculated;
	}
	
	
	
}
