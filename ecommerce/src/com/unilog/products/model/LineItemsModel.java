package com.unilog.products.model;

public class LineItemsModel {
	private String partNumber;
	private String customerPartNumber;
	private String shortDescription;
	private Boolean calculateTax;
	private String recordType;
	private String recordTypeDesc;
	private Boolean nonStockFlag;
	private Boolean printPriceFlag;
	private double quantityAvailable;
	private double netPrice;
	
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getCustomerPartNumber() {
		return customerPartNumber;
	}
	public void setCustomerPartNumber(String customerPartNumber) {
		this.customerPartNumber = customerPartNumber;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public Boolean getCalculateTax() {
		return calculateTax;
	}
	public void setCalculateTax(Boolean calculateTax) {
		this.calculateTax = calculateTax;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getRecordTypeDesc() {
		return recordTypeDesc;
	}
	public void setRecordTypeDesc(String recordTypeDesc) {
		this.recordTypeDesc = recordTypeDesc;
	}
	public Boolean getNonStockFlag() {
		return nonStockFlag;
	}
	public void setNonStockFlag(Boolean nonStockFlag) {
		this.nonStockFlag = nonStockFlag;
	}
	public Boolean getPrintPriceFlag() {
		return printPriceFlag;
	}
	public void setPrintPriceFlag(Boolean printPriceFlag) {
		this.printPriceFlag = printPriceFlag;
	}
	public double getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(double quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	public double getNetPrice() {
		return netPrice;
	}
	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}		
	
}
