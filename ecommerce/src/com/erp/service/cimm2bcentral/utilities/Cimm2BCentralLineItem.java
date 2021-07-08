package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;
import com.erp.service.cimm2bcentral.models.UpsFreight;

public class Cimm2BCentralLineItem {
	
	private Integer lineNumber;
	private String lineItemComment;
	private String itemId;
	private String partNumber;
	private String customerPartNumber;
	private String shortDescription;
	private Double unitPrice;
	private double listPrice;
	private Double extendedPrice;
	private int qty;
	private Double qtyShipped;
	private String uom;
	private String uomQty;
	private String manufacturer;
	private String manufacturerPartNumber;
	private Double cost;
	private ArrayList<Cimm2BCentralCoupon> itemLevelCouponList;
	private String shippingBranch;
	private String taxDesc;
	private double discount;
	public Boolean calculateTax;
	public String shipMethod;
	public String imageName;
	public String brandName;
	private String page_title;
	public int lineIdentifier;
	public String productCategory;
	public String storePartNumber;
	public String supplierPartNumber;
	private String MPN;
	private String orderLastDate;
	private Integer orderLastQty;
	private String status;
	private String catalogNumber;
	private UpsFreight shipViaInfo;
	private Cimm2BCentralSubOrderCriteria subOrderCriteria;
	private Double discountAmount;
	public int cartId;
	private String rushFlag;
	private int backOrderQty;
	public Boolean nonStockFlag;
	public String backorderType;
	private int qtyOpen;
	private String dispositionDescription;
	private double openItemsNumber;
	private String orderGenerationSequence;
	private Boolean printPriceFlag;
	private double quantityAvailable;
	private String recordType;
	private String recordTypeDesc;
	private double netPrice;
	
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
	public String getBackorderType() {
		return backorderType;
	}
	public void setBackorderType(String backorderType) {
		this.backorderType = backorderType;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String getShipMethod() {
		return shipMethod;
	}
	public void setShipMethod(String shipMethod) {
		this.shipMethod = shipMethod;
	}
	public Boolean getCalculateTax() {
		return calculateTax;
	}
	public void setCalculateTax(Boolean calculateTax) {
		this.calculateTax = calculateTax;
	}
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
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
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getExtendedPrice() {
		return extendedPrice;
	}
	public void setExtendedPrice(Double extendedPrice) {
		this.extendedPrice = extendedPrice;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public Double getQtyShipped() {
		return qtyShipped;
	}
	public void setQtyShipped(Double qtyShipped) {
		this.qtyShipped = qtyShipped;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getUomQty() {
		return uomQty;
	}
	public void setUomQty(String uomQty) {
		this.uomQty = uomQty;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}
	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public ArrayList<Cimm2BCentralCoupon> getItemLevelCouponList() {
		return itemLevelCouponList;
	}
	public void setItemLevelCouponList(
			ArrayList<Cimm2BCentralCoupon> itemLevelCouponList) {
		this.itemLevelCouponList = itemLevelCouponList;
	}
	public String getLineItemComment() {
		return lineItemComment;
	}
	public void setLineItemComment(String lineItemComment) {
		this.lineItemComment = lineItemComment;
	}
	public String getShippingBranch() {
		return shippingBranch;
	}
	public void setShippingBranch(String shippingBranch) {
		this.shippingBranch = shippingBranch;
	}
	public double getListPrice() {
		return listPrice;
	}
	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}
	public String getPage_title() {
		return page_title;
	}
	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}
	
	public String getTaxDesc() {
		return taxDesc;
	}
	public void setTaxDesc(String taxDesc) {
		this.taxDesc = taxDesc;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public int getLineIdentifier() {
		return lineIdentifier;
	}
	public void setLineIdentifier(int lineIdentifier) {
		this.lineIdentifier = lineIdentifier;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	public String getStorePartNumber() {
		return storePartNumber;
	}
	public void setStorePartNumber(String storePartNumber) {
		this.storePartNumber = storePartNumber;
	}
	public String getSupplierPartNumber() {
		return supplierPartNumber;
	}
	public void setSupplierPartNumber(String supplierPartNumber) {
		this.supplierPartNumber = supplierPartNumber;
	}
	public String getMPN() {
		return MPN;
	}
	public void setMPN(String mPN) {
		MPN = mPN;
	}
	public String getOrderLastDate() {
		return orderLastDate;
	}
	public void setOrderLastDate(String orderLastDate) {
		this.orderLastDate = orderLastDate;
	}
	public Integer getOrderLastQty() {
		return orderLastQty;
	}
	public void setOrderLastQty(Integer orderLastQty) {
		this.orderLastQty = orderLastQty;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCatalogNumber() {
		return catalogNumber;
	}
	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}
	public UpsFreight getShipViaInfo() {
		return shipViaInfo;
	}
	public void setShipViaInfo(UpsFreight shipViaInfo) {
		this.shipViaInfo = shipViaInfo;
	}
	public Cimm2BCentralSubOrderCriteria getSubOrderCriteria() {
		return subOrderCriteria;
	}
	public void setSubOrderCriteria(Cimm2BCentralSubOrderCriteria subOrderCriteria) {
		this.subOrderCriteria = subOrderCriteria;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public int getBackOrderQty() {
		return backOrderQty;
	}
	public void setBackOrderQty(int backOrderQty) {
		this.backOrderQty = backOrderQty;
	}
	public String getRushFlag() {
		return rushFlag;
	}
	public void setRushFlag(String rushFlag) {
		this.rushFlag = rushFlag;
	}
	public Boolean getNonStockFlag() {
		return nonStockFlag;
	}
	public void setNonStockFlag(Boolean nonStockFlag) {
		this.nonStockFlag = nonStockFlag;
	}
	public int getQtyOpen() {
		return qtyOpen;
	}
	public void setQtyOpen(int qtyOpen) {
		this.qtyOpen = qtyOpen;
	}
	public String getDispositionDescription() {
		return dispositionDescription;
	}
	public void setDispositionDescription(String dispositionDescription) {
		this.dispositionDescription = dispositionDescription;
	}
	public double getOpenItemsNumber() {
		return openItemsNumber;
	}
	public void setOpenItemsNumber(double openItemsNumber) {
		this.openItemsNumber = openItemsNumber;
	}
	public String getOrderGenerationSequence() {
		return orderGenerationSequence;
	}
	public void setOrderGenerationSequence(String orderGenerationSequence) {
		this.orderGenerationSequence = orderGenerationSequence;
	}
	public double getNetPrice() {
		return netPrice;
	}
	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}
	
}
