package com.unilog.punchout.model;

public class PunchoutModel {
	private String username;
	private String password;
	private int siteId;
	private int customerId;
	private int userId;
	private String punchoutType;
	private String procurementSystem;
	private String shareScret;
	private String networkId;
	private int id;
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPunchoutType() {
		return punchoutType;
	}
	public void setPunchoutType(String punchoutType) {
		this.punchoutType = punchoutType;
	}
	public String getProcurementSystem() {
		return procurementSystem;
	}
	public void setProcurementSystem(String procurementSystem) {
		this.procurementSystem = procurementSystem;
	}
	public String getShareScret() {
		return shareScret;
	}
	public void setShareScret(String shareScret) {
		this.shareScret = shareScret;
	}
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
