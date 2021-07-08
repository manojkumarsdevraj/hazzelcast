package com.erp.service.cimm2bcentral.models;

import java.util.ArrayList;
import java.util.List;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralContact;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;

public class FedExFreight {
	
	Cimm2BCentralAddress shipFromAddress;
	Cimm2BCentralAddress shipToAddress;
	Cimm2BCentralContact shipFromContact;
	Cimm2BCentralContact shipToContact;
	PackageInfo packageInfo;
	String dropType;
	String serviceName;
	String packageType;
	PackageDimension packageDimension;
	List<Cimm2BCentralLineItem> lineItems = new ArrayList<Cimm2BCentralLineItem>();
	
	public Cimm2BCentralAddress getShipFromAddress() {
		return shipFromAddress;
	}
	public void setShipFromAddress(Cimm2BCentralAddress shipFromAddress) {
		this.shipFromAddress = shipFromAddress;
	}
	public Cimm2BCentralAddress getShipToAddress() {
		return shipToAddress;
	}
	public void setShipToAddress(Cimm2BCentralAddress shipToAddress) {
		this.shipToAddress = shipToAddress;
	}
	public Cimm2BCentralContact getShipFromContact() {
		return shipFromContact;
	}
	public void setShipFromContact(Cimm2BCentralContact shipFromContact) {
		this.shipFromContact = shipFromContact;
	}
	public Cimm2BCentralContact getShipToContact() {
		return shipToContact;
	}
	public void setShipToContact(Cimm2BCentralContact shipToContact) {
		this.shipToContact = shipToContact;
	}
	public PackageInfo getPackageInfo() {
		return packageInfo;
	}
	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}
	public String getDropType() {
		return dropType;
	}
	public void setDropType(String dropType) {
		this.dropType = dropType;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public PackageDimension getPackageDimension() {
		return packageDimension;
	}
	public void setPackageDimension(PackageDimension packageDimension) {
		this.packageDimension = packageDimension;
	}
	public List<Cimm2BCentralLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(List<Cimm2BCentralLineItem> lineItems) {
		this.lineItems = lineItems;
	}
}
