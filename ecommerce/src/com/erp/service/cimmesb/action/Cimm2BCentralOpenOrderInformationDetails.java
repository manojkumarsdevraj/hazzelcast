package com.erp.service.cimmesb.action;

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
	    private String poNumber;
	    private String invoiceNumber;
	    private String shipToName;
	    private String orderedBy;
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
	    
}
