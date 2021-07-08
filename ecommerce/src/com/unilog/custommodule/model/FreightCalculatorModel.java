package com.unilog.custommodule.model;

public class FreightCalculatorModel {
	private String message;
	private double freightValue;
	private double cartTotal;
	private String customerNumber;
	private String wareHouseCode;
	private String overSize;
	private String shipVia;
	private String state;
	private String country;
	private String locale;
	private String hazmat;
	
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setFreightValue(double freightValue) {
		this.freightValue = freightValue;
	}
	public double getFreightValue() {
		return freightValue;
	}
	public void setCartTotal(double cartTotal) {
		this.cartTotal = cartTotal;
	}
	public double getCartTotal() {
		return cartTotal;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}
	public String getWareHouseCode() {
		return wareHouseCode;
	}
	public void setShipVia(String shipVia) {
		this.shipVia = shipVia;
	}
	public String getShipVia() {
		return shipVia;
	}
	public void setOverSize(String overSize) {
		this.overSize = overSize;
	}
	public String getOverSize() {
		return overSize;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getState() {
		return state;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return country;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getLocale() {
		return locale;
	}
	public String getHazmat() {
		return hazmat;
	}
	public void setHazmat(String hazmat) {
		this.hazmat = hazmat;
	}
}
