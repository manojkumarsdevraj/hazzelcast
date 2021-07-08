package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralVendorLineItems {
	private String partNumber;
    private Boolean calculateTax;
    private Boolean nonStockFlag;
    private String itemCount;
    private Boolean printPriceFlag;
    
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public Boolean getCalculateTax() {
		return calculateTax;
	}
	public void setCalculateTax(Boolean calculateTax) {
		this.calculateTax = calculateTax;
	}
	public Boolean getNonStockFlag() {
		return nonStockFlag;
	}
	public void setNonStockFlag(Boolean nonStockFlag) {
		this.nonStockFlag = nonStockFlag;
	}
	public String getItemCount() {
		return itemCount;
	}
	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}
	public Boolean getPrintPriceFlag() {
		return printPriceFlag;
	}
	public void setPrintPriceFlag(Boolean printPriceFlag) {
		this.printPriceFlag = printPriceFlag;
	}
    
}
