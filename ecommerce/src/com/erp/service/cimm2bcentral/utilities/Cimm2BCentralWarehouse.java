package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralWarehouse {

	private ArrayList<String> customerPartNumbers;
	private String warehouseCode;
	private Double listPrice;
	private Double customerPrice;
	private Double imapPrice;
	private String uom;
	private Integer quantityAvailable;
	private Double earliestMoreQuantity;
	private String earliestMoreDate;
	private Double nextQuantityBreak;
	private Double nextQuantityPrice;
	private String nextBreakType;
	private String restrictiveProduct; 
	private ArrayList<Cimm2BCentralPriceBreaks> priceBreaks;
	private Double extendedPrice;
	private Integer qty;
	private Double promoPrice;
	private Double imapPrice1;
	private ArrayList<Cimm2BCentralShipMethodQuantity> shipMethods;
	private String currencyCode;
	private int otherBranchAvailability;
	private double salesQuantity;
	private String uomPack;
	private double minimumOrderQuantity;
	private double orderQuantityInterval;
	private String productCategory;
	private Cimm2BCentralCurrencyDetails currencyDetails;
	private Double costPrice;
	private String supplierSku;
	private String storeSku;
	private String supplierCode;
	private boolean itemDiscontinued;
	private boolean orgillItem;
	private boolean nonWebItem;
	private boolean displayPrice;
	private ArrayList<Cimm2BCentralUOM> uomList;
	private int uomQuantity;
	private Double replacementCost;
	private Double discountAmount;
	private Double priceUomQuantity;
	private String errorMessage;
	private Double otherAmount;
	private String dueDate;
	private String shelfPackQty;
	
	
	public String getShelfPackQty() {
		return shelfPackQty;
	}
	public void setShelfPackQty(String shelfPackQty) {
		this.shelfPackQty = shelfPackQty;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}	
	public Double getOtherAmount() {
		return otherAmount;
	}
	public void setOtherAmount(Double otherAmount) {
		this.otherAmount = otherAmount;
	}	
	public Double getPriceUomQuantity() {
		return priceUomQuantity;
	}
	public void setPriceUomQuantity(Double priceUomQuantity) {
		this.priceUomQuantity = priceUomQuantity;
	}
	public Double getReplacementCost() {
		return replacementCost;
	}
	public void setReplacementCost(Double replacementCost) {
		this.replacementCost = replacementCost;
	}
	public String getUomPack() {
		return uomPack;
	}
	public void setUomPack(String uomPack) {
		this.uomPack = uomPack;
	}
	public double getSalesQuantity() {
		return salesQuantity;
	}
	public void setSalesQuantity(double salesQuantity) {
		this.salesQuantity = salesQuantity;
	}
	public String getRestrictiveProduct() {
		return restrictiveProduct;
	}
	public void setRestrictiveProduct(String restrictiveProduct) {
		this.restrictiveProduct = restrictiveProduct;
	}
	public ArrayList<String> getCustomerPartNumbers() {
		return customerPartNumbers;
	}
	public void setCustomerPartNumbers(ArrayList<String> customerPartNumbers) {
		this.customerPartNumbers = customerPartNumbers;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public Double getListPrice() {
		return listPrice;
	}
	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}
	public Double getCustomerPrice() {
		return customerPrice;
	}
	public void setCustomerPrice(Double customerPrice) {
		this.customerPrice = customerPrice;
	}
	public Double getImapPrice() {
		return imapPrice;
	}
	public void setImapPrice(Double imapPrice) {
		this.imapPrice = imapPrice;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	public Double getEarliestMoreQuantity() {
		return earliestMoreQuantity;
	}
	public void setEarliestMoreQuantity(Double earliestMoreQuantity) {
		this.earliestMoreQuantity = earliestMoreQuantity;
	}
	public String getEarliestMoreDate() {
		return earliestMoreDate;
	}
	public void setEarliestMoreDate(String earliestMoreDate) {
		this.earliestMoreDate = earliestMoreDate;
	}
	public Double getNextQuantityBreak() {
		return nextQuantityBreak;
	}
	public void setNextQuantityBreak(Double nextQuantityBreak) {
		this.nextQuantityBreak = nextQuantityBreak;
	}
	public Double getNextQuantityPrice() {
		return nextQuantityPrice;
	}
	public void setNextQuantityPrice(Double nextQuantityPrice) {
		this.nextQuantityPrice = nextQuantityPrice;
	}
	public String getNextBreakType() {
		return nextBreakType;
	}
	public void setNextBreakType(String nextBreakType) {
		this.nextBreakType = nextBreakType;
	}
	public ArrayList<Cimm2BCentralPriceBreaks> getPriceBreaks() {
		return priceBreaks;
	}
	public void setPriceBreaks(ArrayList<Cimm2BCentralPriceBreaks> priceBreaks) {
		this.priceBreaks = priceBreaks;
	}
	public Double getExtendedPrice() {
		return extendedPrice;
	}
	public void setExtendedPrice(Double extendedPrice) {
		this.extendedPrice = extendedPrice;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public ArrayList<Cimm2BCentralShipMethodQuantity> getShipMethods() {
		return shipMethods;
	}
	public void setShipMethods(ArrayList<Cimm2BCentralShipMethodQuantity> shipMethods) {
		this.shipMethods = shipMethods;
	}
	public Double getPromoPrice() {
		return promoPrice;
	}
	public void setPromoPrice(Double promoPrice) {
		this.promoPrice = promoPrice;
	}
	public Double getImapPrice1() {
		return imapPrice1;
	}
	public void setImapPrice1(Double imapPrice1) {
		this.imapPrice1 = imapPrice1;
	}
	public int getOtherBranchAvailability() {
		return otherBranchAvailability;
	}
	public void setOtherBranchAvailability(int otherBranchAvailability) {
		this.otherBranchAvailability = otherBranchAvailability;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public double getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}
	public void setMinimumOrderQuantity(double minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}
	public double getOrderQuantityInterval() {
		return orderQuantityInterval;
	}
	public void setOrderQuantityInterval(double orderQuantityInterval) {
		this.orderQuantityInterval = orderQuantityInterval;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	public Cimm2BCentralCurrencyDetails getCurrencyDetails() {
		return currencyDetails;
	}
	public void setCurrencyDetails(Cimm2BCentralCurrencyDetails currencyDetails) {
		this.currencyDetails = currencyDetails;
	}
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	public String getSupplierSku() {
		return supplierSku;
	}
	public void setSupplierSku(String supplierSku) {
		this.supplierSku = supplierSku;
	}
	public String getStoreSku() {
		return storeSku;
	}
	public void setStoreSku(String storeSku) {
		this.storeSku = storeSku;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public boolean isItemDiscontinued() {
		return itemDiscontinued;
	}
	public void setItemDiscontinued(boolean itemDiscontinued) {
		this.itemDiscontinued = itemDiscontinued;
	}
	public boolean isNonWebItem() {
		return nonWebItem;
	}
	public void setNonWebItem(boolean nonWebItem) {
		this.nonWebItem = nonWebItem;
	}
	public boolean isDisplayPrice() {
		return displayPrice;
	}
	public void setDisplayPrice(boolean displayPrice) {
		this.displayPrice = displayPrice;
	}
	public boolean isOrgillItem() {
		return orgillItem;
	}
	public void setOrgillItem(boolean orgillItem) {
		this.orgillItem = orgillItem;
	}
	public ArrayList<Cimm2BCentralUOM> getUomList() {
		return uomList;
	}
	public void setUomList(ArrayList<Cimm2BCentralUOM> uomList) {
		this.uomList = uomList;
	}
	public int getUomQuantity() {
		return uomQuantity;
	}
	public void setUomQuantity(int uomQuantity) {
		this.uomQuantity = uomQuantity;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
