package com.unilog.maplocation;

import java.util.ArrayList;

import com.unilog.defaults.CustomFieldModel;

public class LocationModel implements Comparable<LocationModel> {
	private String latitude;
	private String longitude;
	private String branchName;
	private String branchId;
	private String branchCode;
	private String locality;
	private String street;
	private String phone;
	private String faxNum;
	private String workHour;
	private String note;
	private String email;
	private double distance;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String tollFreeNum;
	private ArrayList<CustomFieldModel> customFields;
	private String warehouseImage;
	private String serviceManager;
	private String warehouseDescription;	
	private String lineCardName;	
	private String lineCardLink;
	
	public String getWarehouseDescription() {
		return warehouseDescription;
	}
	public void setWarehouseDescription(String warehouseDescription) {
		this.warehouseDescription = warehouseDescription;
	}
	public String getBranchId() {
		return branchId;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getWarehouseImage() {
		return warehouseImage;
	}

	public void setWarehouseImage(String warehouseImage) {
		this.warehouseImage = warehouseImage;
	}

	public String getTollFreeNum() {
		return tollFreeNum;
	}

	public void setTollFreeNum(String tollFreeNum) {
		this.tollFreeNum = tollFreeNum;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getWorkHour() {
		return workHour;
	}

	public void setWorkHour(String workHour) {
		this.workHour = workHour;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFaxNum() {
		return faxNum;
	}

	public void setFaxNum(String faxNum) {
		this.faxNum = faxNum;
	}
	@Override
	public int compareTo(LocationModel locationModel) {
		return new Double(this.distance).compareTo( locationModel.getDistance());
	}
	public ArrayList<CustomFieldModel> getCustomFields() {
		return customFields;
	}
	public void setCustomFields(ArrayList<CustomFieldModel> customFields) {
		this.customFields = customFields;
	}

	public String getServiceManager() {
		return serviceManager;
	}
	public void setServiceManager(String serviceManager) {
		this.serviceManager = serviceManager;
	}

	public String getLineCardName() {
		return lineCardName;
	}
	public void setLineCardName(String lineCardName) {
		this.lineCardName = lineCardName;
	}
	public String getLineCardLink() {
		return lineCardLink;
	}
	public void setLineCardLink(String lineCardLink) {
		this.lineCardLink = lineCardLink;
	}
}
