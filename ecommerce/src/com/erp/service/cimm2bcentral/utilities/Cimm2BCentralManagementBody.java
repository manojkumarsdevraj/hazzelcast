package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralManagementBody {


	private String sortOrder;
	private String fixtureType;
	private String vendor;
	private String description;
	private int orderQty;
	private String status;
	private int shipQty;
	private String actualShipDate;
	private String estShipDate;
	private String shipper;
	private String trackingNumber;
	private String externalNote;
	private String invNumber;


	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getFixtureType() {
		return fixtureType;
	}
	public void setFixtureType(String fixtureType) {
		this.fixtureType = fixtureType;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getShipQty() {
		return shipQty;
	}
	public void setShipQty(int shipQty) {
		this.shipQty = shipQty;
	}
	public String getActualShipDate() {
		return actualShipDate;
	}
	public void setActualShipDate(String actualShipDate) {
		this.actualShipDate = actualShipDate;
	}
	public String getEstShipDate() {
		return estShipDate;
	}
	public void setEstShipDate(String estShipDate) {
		this.estShipDate = estShipDate;
	}
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getExternalNote() {
		return externalNote;
	}
	public void setExternalNote(String externalNote) {
		this.externalNote = externalNote;
	}
	public String getInvNumber() {
		return invNumber;
	}
	public void setInvNumber(String invNumber) {
		this.invNumber = invNumber;
	}


}
