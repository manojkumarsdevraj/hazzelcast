package com.unilognew.model;

public class SXShipToAddressRequest implements ERPServiceRequest {
	
	private String customerNumber;
	private String city;
	private boolean includeClosedJobs;
	private String name;
	private String phoneNumber;
	private String shipTo;
	private String state;
	private String postalCode;
	private int recordLimit;
	
	public SXShipToAddressRequest() {

	}
	
	
	/**
	 * @return the customerNumber
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}
	/**
	 * @param customerNumber the customerNumber to set
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the includeClosedJobs
	 */
	public boolean isIncludeClosedJobs() {
		return includeClosedJobs;
	}
	/**
	 * @param includeClosedJobs the includeClosedJobs to set
	 */
	public void setIncludeClosedJobs(boolean includeClosedJobs) {
		this.includeClosedJobs = includeClosedJobs;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the shipTo
	 */
	public String getShipTo() {
		return shipTo;
	}
	/**
	 * @param shipTo the shipTo to set
	 */
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	/**
	 * @return the recordLimit
	 */
	public int getRecordLimit() {
		return recordLimit;
	}
	/**
	 * @param recordLimit the recordLimit to set
	 */
	public void setRecordLimit(int recordLimit) {
		this.recordLimit = recordLimit;
	}
	
}
