package com.erp.service.cimm2bcentral.models;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;

public class TimeInTransit {
	private Cimm2BCentralAddress shipToAddress;
	private Cimm2BCentralAddress shipFromAddress;
	private PackageInfo shippingWeight;
	private String pickUpDate;
	// date format MM/DD/YYYY
	public Cimm2BCentralAddress getShipToAddress() {
		return shipToAddress;
	}
	public void setShipToAddress(Cimm2BCentralAddress shipToAddress) {
		this.shipToAddress = shipToAddress;
	}
	public Cimm2BCentralAddress getShipFromAddress() {
		return shipFromAddress;
	}
	public void setShipFromAddress(Cimm2BCentralAddress shipFromAddress) {
		this.shipFromAddress = shipFromAddress;
	}
	public PackageInfo getShippingWeight() {
		return shippingWeight;
	}
	public void setShippingWeight(PackageInfo shippingWeight) {
		this.shippingWeight = shippingWeight;
	}
	public String getPickUpDate() {
		return pickUpDate;
	}
	public void setPickUpDate(String pickUpDate) {
		this.pickUpDate = pickUpDate;
	}
	
}
