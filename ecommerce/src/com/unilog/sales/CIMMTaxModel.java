package com.unilog.sales;

public class CIMMTaxModel {
	private String country;
	private String state;
	private String zipCodeStringFormat; 
	private Double taxPercentage;
	private String jurisdictionId;
	private String startZip; 
	private String endZip;

	public Double getTaxPercentage() {
		return taxPercentage;
	}
	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}
	public String getJurisdictionId() {
		return jurisdictionId;
	}
	public void setJurisdictionId(String jurisdictionId) {
		this.jurisdictionId = jurisdictionId;
	}
	public String getStartZip() {
		return startZip;
	}
	public void setStartZip(String startZip) {
		this.startZip = startZip;
	}
	public String getEndZip() {
		return endZip;
	}
	public void setEndZip(String endZip) {
		this.endZip = endZip;
	}
	public String getZipCodeStringFormat() {
		return zipCodeStringFormat;
	}
	public void setZipCodeStringFormat(String zipCodeStringFormat) {
		this.zipCodeStringFormat = zipCodeStringFormat;
	}
	private double taxPrecentage;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public double getTaxPrecentage() {
		return taxPrecentage;
	}
	public void setTaxPrecentage(double taxPrecentage) {
		this.taxPrecentage = taxPrecentage;
	}
}