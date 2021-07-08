package com.unilog.model;

public class BrandDataModel {
	private int id;
	private String status;
	private int staticPageId;
	private String brandName;
	private String brandDescription;
	private String brandImage;
	private String cneBatchId;
	private String brandUrl;
	private ManufacturerDataModel manufacturer;
	
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStaticPageId() {
		return staticPageId;
	}
	public void setStaticPageId(int staticPageId) {
		this.staticPageId = staticPageId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandDescription() {
		return brandDescription;
	}
	public void setBrandDescription(String brandDescription) {
		this.brandDescription = brandDescription;
	}
	public String getBrandImage() {
		return brandImage;
	}
	public void setBrandImage(String brandImage) {
		this.brandImage = brandImage;
	}
	public String getCneBatchId() {
		return cneBatchId;
	}
	public void setCneBatchId(String cneBatchId) {
		this.cneBatchId = cneBatchId;
	}
	public String getBrandUrl() {
		return brandUrl;
	}
	public void setBrandUrl(String brandUrl) {
		this.brandUrl = brandUrl;
	}
	public ManufacturerDataModel getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(ManufacturerDataModel manufacturer) {
		this.manufacturer = manufacturer;
	}
}
