package com.unilog.model.banners;

import java.util.LinkedHashMap;


public class BannerListData{
	private int id;
	private String bannerListName;
	private String startDate;
	private String expiryDate;
	private BannerInfo bannerInfo;
	private LinkedHashMap<String, String> dynamicProperties;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBannerListName() {
		return bannerListName;
	}
	public void setBannerListName(String bannerListName) {
		this.bannerListName = bannerListName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public void setBannerInfo(BannerInfo bannerInfo) {
		this.bannerInfo = bannerInfo;
	}
	public BannerInfo getBannerInfo() {
		return bannerInfo;
	}
	public void setDynamicProperties(LinkedHashMap<String, String> dynamicProperties) {
		this.dynamicProperties = dynamicProperties;
	}
	public LinkedHashMap<String, String> getDynamicProperties() {
		return dynamicProperties;
	}
	
	
}
