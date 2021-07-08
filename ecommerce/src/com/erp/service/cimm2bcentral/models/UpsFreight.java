package com.erp.service.cimm2bcentral.models;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import java.util.ArrayList;
import java.util.List;

public class UpsFreight {
	private ContactDetails shipperContact;
	private Cimm2BCentralAddress shipperAddress;
	private ContactDetails shipToContact;
	private Cimm2BCentralAddress shipToAddress;
	private ContactDetails shipFromContact;
	private Cimm2BCentralAddress shipFromAddress;
	private PackageInfo packageInfo;
	private PackageInfo shippingWeight;
	private String serviceCode;
	private String serviceName;
	private List<PackageInfo> listOfshippingWeight;
	private String shippingCarrierType;
	private ArrayList<PackageInfo> itemPackageInfo;
	
	public ContactDetails getShipToContact() {
		return shipToContact;
	}
	public void setShipToContact(ContactDetails shipToContact) {
		this.shipToContact = shipToContact;
	}
	public ContactDetails getShipFromContact() {
		return shipFromContact;
	}
	public void setShipFromContact(ContactDetails shipFromContact) {
		this.shipFromContact = shipFromContact;
	}
	public Cimm2BCentralAddress getShipperAddress() {
		return shipperAddress;
	}
	public void setShipperAddress(Cimm2BCentralAddress shipperAddress) {
		this.shipperAddress = shipperAddress;
	}
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
	public PackageInfo getPackageInfo() {
		return packageInfo;
	}
	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}
	public PackageInfo getShippingWeight() {
		return shippingWeight;
	}
	public void setShippingWeight(PackageInfo shippingWeight) {
		this.shippingWeight = shippingWeight;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public List<PackageInfo> getListOfshippingWeight() {
		return listOfshippingWeight;
	}
	public void setListOfshippingWeight(List<PackageInfo> listOfshippingWeight) {
		this.listOfshippingWeight = listOfshippingWeight;
	}
	public ContactDetails getShipperContact() {
		return shipperContact;
	}
	public void setShipperContact(ContactDetails shipperContact) {
		this.shipperContact = shipperContact;
	}
	public String getShippingCarrierType() {
		return shippingCarrierType;
	}
	public void setShippingCarrierType(String shippingCarrierType) {
		this.shippingCarrierType = shippingCarrierType;
	}
	public ArrayList<PackageInfo> getItemPackageInfo() {
		return itemPackageInfo;
	}
	public void setItemPackageInfo(ArrayList<PackageInfo> itemPackageInfo) {
		this.itemPackageInfo = itemPackageInfo;
	}
}
