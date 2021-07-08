package com.unilog.avalara.model;

public class Lines {
	private double Amount;
	private String CustomerUsageType;
	private String Description;
	private String DestinationCode;
	private boolean Discounted;
	private String ItemCode;
	private String LineNo;
	private String OriginCode;
	private int Qty;
	private String TaxCode;
	private boolean TaxIncluded;
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	public String getCustomerUsageType() {
		return CustomerUsageType;
	}
	public void setCustomerUsageType(String customerUsageType) {
		CustomerUsageType = customerUsageType;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getDestinationCode() {
		return DestinationCode;
	}
	public void setDestinationCode(String destinationCode) {
		DestinationCode = destinationCode;
	}
	public boolean isDiscounted() {
		return Discounted;
	}
	public void setDiscounted(boolean discounted) {
		Discounted = discounted;
	}
	public String getItemCode() {
		return ItemCode;
	}
	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}
	public String getLineNo() {
		return LineNo;
	}
	public void setLineNo(String lineNo) {
		LineNo = lineNo;
	}
	public String getOriginCode() {
		return OriginCode;
	}
	public void setOriginCode(String originCode) {
		OriginCode = originCode;
	}
	public int getQty() {
		return Qty;
	}
	public void setQty(int qty) {
		Qty = qty;
	}
	public String getTaxCode() {
		return TaxCode;
	}
	public void setTaxCode(String taxCode) {
		TaxCode = taxCode;
	}
	public boolean isTaxIncluded() {
		return TaxIncluded;
	}
	public void setTaxIncluded(boolean taxIncluded) {
		TaxIncluded = taxIncluded;
	}
	
	
	
}
