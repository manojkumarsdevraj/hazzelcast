package com.unilog.users;

import java.io.Serializable;

public class ShipVia implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ShipViaID;
	private String Description;
	private boolean Ispickup;
	private double ShipCost;
	private String ShipCode;
	private String ShipViaWebSiteName;
	private int ShipViaWebSiteId;
	private int displaySeq;
	private int serviceCode;
	private String externalSystemCode;
	private String restrictedTo;
	private String serviceProvider;
	private String type;
	private String accountNumber;
	
	public String getRestrictedTo() {
		return restrictedTo;
	}
	public void setRestrictedTo(String restrictedTo) {
		this.restrictedTo = restrictedTo;
	}
	public String getServiceProvider() {
		return serviceProvider;
	}
	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	public void setShipViaID(String shipViaID) {
		ShipViaID = shipViaID;
	}
	public String getShipViaID() {
		return ShipViaID;
	}
	public double getShipCost() {
		return ShipCost;
	}
	public void setShipCost(double shipCost) {
		ShipCost = shipCost;
	}
	public String getShipCode() {
		return ShipCode;
	}
	public void setShipCode(String shipCode) {
		ShipCode = shipCode;
	}
	public String getShipViaWebSiteName() {
		return ShipViaWebSiteName;
	}
	public void setShipViaWebSiteName(String shipViaWebSiteName) {
		ShipViaWebSiteName = shipViaWebSiteName;
	}
	public int getShipViaWebSiteId() {
		return ShipViaWebSiteId;
	}
	public void setShipViaWebSiteId(int shipViaWebSiteId) {
		ShipViaWebSiteId = shipViaWebSiteId;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getDescription() {
		return Description;
	}
	public boolean isIspickup() {
		return Ispickup;
	}
	public void setIspickup(boolean ispickup) {
		Ispickup = ispickup;
	}
	public int getDisplaySeq() {
		return displaySeq;
	}
	public void setDisplaySeq(int displaySeq) {
		this.displaySeq = displaySeq;
	}
	public int getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(int serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getExternalSystemCode() {
		return externalSystemCode;
	}
	public void setExternalSystemCode(String externalSystemCode) {
		this.externalSystemCode = externalSystemCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

}
