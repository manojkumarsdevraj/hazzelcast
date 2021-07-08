package com.erp.service.cimm2bcentral.utilities;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;

public class Cimm2BCentralOpenOrderInformationDetails extends Cimm2BCentralResponseEntity{
	private String orderNumber;
    private String backOrderSequence;
    private String supplier;
    private String part;
    private String location;
    private String description1;
    private String description2;
    private String orderDate;
    private Double orderTotal;
    private String customerPoNumber;
    private String invoiceNumber;
    private String shipToName;
    private String orderedBy;
    private Double totalInvoiceAmount;
    private String shipDate;
    private String orderStatus;
    private String poNumber;
    private String generationId;
    private String orderTypeCode;
    private String requiredDate;
    private String orderSuffix;
    private String orderERPId;
    private String customerERPId;
    private String shipToId;
    private String orderType;
    private String customerName;
        
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
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getOrderERPId() {
		return orderERPId;
	}
	public void setOrderERPId(String orderERPId) {
		this.orderERPId = orderERPId;
	}
    private String discountAmount;
    private String roeDiscountAmount;
    private String discountPercentage;
    
		public String getGenerationId() {
		return generationId;
	}
	public void setGenerationId(String generationId) {
		this.generationId = generationId;
	}
		public String getOrderNumber() {
			return orderNumber;
		}
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		public String getBackOrderSequence() {
			return backOrderSequence;
		}
		public void setBackOrderSequence(String backOrderSequence) {
			this.backOrderSequence = backOrderSequence;
		}
		public String getSupplier() {
			return supplier;
		}
		public void setSupplier(String supplier) {
			this.supplier = supplier;
		}
		public String getPart() {
			return part;
		}
		public void setPart(String part) {
			this.part = part;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getDescription1() {
			return description1;
		}
		public void setDescription1(String description1) {
			this.description1 = description1;
		}
		public String getDescription2() {
			return description2;
		}
		public void setDescription2(String description2) {
			this.description2 = description2;
		}
		public String getOrderDate() {
			return orderDate;
		}
		public void setOrderDate(String orderDate) {
			this.orderDate = orderDate;
		}
		public Double getOrderTotal() {
			return orderTotal;
		}
		public void setOrderTotal(Double orderTotal) {
			this.orderTotal = orderTotal;
		}
		public String getPoNumber() {
			return poNumber;
		}
		public void setPoNumber(String poNumber) {
			this.poNumber = poNumber;
		}
		public String getInvoiceNumber() {
			return invoiceNumber;
		}
		public void setInvoiceNumber(String invoiceNumber) {
			this.invoiceNumber = invoiceNumber;
		}
		public String getShipToName() {
			return shipToName;
		}
		public void setShipToName(String shipToName) {
			this.shipToName = shipToName;
		}
		public String getOrderedBy() {
			return orderedBy;
		}
		public void setOrderedBy(String orderedBy) {
			this.orderedBy = orderedBy;
		}
		public String getCustomerPoNumber() {
			return customerPoNumber;
		}
		public void setCustomerPoNumber(String customerPoNumber) {
			this.customerPoNumber = customerPoNumber;
		}
		public Double getTotalInvoiceAmount() {
			return totalInvoiceAmount;
		}
		public void setTotalInvoiceAmount(Double totalInvoiceAmount) {
			this.totalInvoiceAmount = totalInvoiceAmount;
		}
		public String getShipDate() {
			return shipDate;
		}
		public void setShipDate(String shipDate) {
			this.shipDate = shipDate;
		}
		public String getOrderStatus() {
			return orderStatus;
		}
		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}
		public String getOrderTypeCode() {
			return orderTypeCode;
		}
		public void setOrderTypeCode(String orderTypeCode) {
			this.orderTypeCode = orderTypeCode;
		}
		public String getRequiredDate() {
			return requiredDate;
		}
		public void setRequiredDate(String requiredDate) {
			this.requiredDate = requiredDate;
		}
		public String getOrderSuffix() {
			return orderSuffix;
		}
		public void setOrderSuffix(String orderSuffix) {
			this.orderSuffix = orderSuffix;
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
		public String getDiscountPercentage() {
			return discountPercentage;
		}
		public void setDiscountPercentage(String discountPercentage) {
			this.discountPercentage = discountPercentage;
		}
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		
	    
		
}
