package com.erp.service.cimm2bcentral.utilities;



public class Cimm2BCentralSalesOrderHistoryDetails extends Cimm2BCentralResponseEntity {

	private String backOrderSequence;
	private String customerERPId;
	// industicals
	private String customerName;
	private String description1;
	private String description2;
	private String invoiceNumber;
	private String location;
	private String orderDate;
	private String orderedBy;
	private String orderNumber;
	private String orderStatus;
	private Double orderTotal;
	private String part;
	private String poNumber;
	private String shipToName;
	private String supplier;
	private String accountId;
	private String generationId;
	private String orderTypeCode;
	private String orderSuffix;
	private String shipDate; 
	private String discountAmount;
	private String roeDiscountAmount;
	private String postDate;
	private String discountPercentage;
	private String balance;
	private String invoiceValue;
	private String shipToId;	
	private String orderType;
	private String shippingBranch;
	private String description;
	private Double totalInvoiceAmount;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getShippingBranch() {
		return shippingBranch;
	}

	public void setShippingBranch(String shippingBranch) {
		this.shippingBranch = shippingBranch;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getShipToId() {
		return shipToId;
	}

	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}

	public String getGenerationId() {
		return generationId;
	}

	public void setGenerationId(String generationId) {
		this.generationId = generationId;
	}
	
	public String getBackOrderSequence() {
		return backOrderSequence;
	}

	public String getCustomerERPId() {
		return customerERPId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getDescription1() {
		return description1;
	}

	public String getDescription2() {
		return description2;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public String getLocation() {
		return location;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public Double getOrderTotal() {
		return orderTotal;
	}

	public String getPart() {
		return part;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public String getShipToName() {
		return shipToName;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setBackOrderSequence(String backOrderSequence) {
		this.backOrderSequence = backOrderSequence;
	}

	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOrderTypeCode() {
		return orderTypeCode;
	}

	public void setOrderTypeCode(String orderTypeCode) {
		this.orderTypeCode = orderTypeCode;
	}
	public String getOrderSuffix() {
		return orderSuffix;
	}

	public void setOrderSuffix(String orderSuffix) {
		this.orderSuffix = orderSuffix;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getRoeDiscountAmount() {
		return roeDiscountAmount;
	}

	public void setRoeDiscountAmount(String roeDiscountAmount) {
		this.roeDiscountAmount = roeDiscountAmount;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public String getInvoiceValue() {
		return invoiceValue;
	}

	public void setInvoiceValue(String invoiceValue) {
		this.invoiceValue = invoiceValue;
	}
	
	public Double getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}

	public void setTotalInvoiceAmount(Double totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}
}
