package com.shippingcarrier.ups.model;

public class UpsCarrierModel {
	
	private int serviceCode;
	private String measurementCode;
	private double weight;
	private double billingWeight;
	private String TransportationCurrencyCode;
	private double TransportationMonetaryValue;
	private String ServiceOptionsCurrencyCode;
	private double ServiceOptionsMonetaryValue;
	private String TotalCurrencyCode;
	private double TotalMonetaryValue;
	private String customerContext;
	private String StatusCode;
	private String StatusDescription;
	
	
	public int getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(int serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getMeasurementCode() {
		return measurementCode;
	}
	public void setMeasurementCode(String measurementCode) {
		this.measurementCode = measurementCode;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getBillingWeight() {
		return billingWeight;
	}
	public void setBillingWeight(double billingWeight) {
		this.billingWeight = billingWeight;
	}
	public String getTransportationCurrencyCode() {
		return TransportationCurrencyCode;
	}
	public void setTransportationCurrencyCode(String transportationCurrencyCode) {
		TransportationCurrencyCode = transportationCurrencyCode;
	}
	public double getTransportationMonetaryValue() {
		return TransportationMonetaryValue;
	}
	public void setTransportationMonetaryValue(double transportationMonetaryValue) {
		TransportationMonetaryValue = transportationMonetaryValue;
	}
	public String getServiceOptionsCurrencyCode() {
		return ServiceOptionsCurrencyCode;
	}
	public void setServiceOptionsCurrencyCode(String serviceOptionsCurrencyCode) {
		ServiceOptionsCurrencyCode = serviceOptionsCurrencyCode;
	}
	public double getServiceOptionsMonetaryValue() {
		return ServiceOptionsMonetaryValue;
	}
	public void setServiceOptionsMonetaryValue(double serviceOptionsMonetaryValue) {
		ServiceOptionsMonetaryValue = serviceOptionsMonetaryValue;
	}
	public String getTotalCurrencyCode() {
		return TotalCurrencyCode;
	}
	public void setTotalCurrencyCode(String totalCurrencyCode) {
		TotalCurrencyCode = totalCurrencyCode;
	}
	public double getTotalMonetaryValue() {
		return TotalMonetaryValue;
	}
	public void setTotalMonetaryValue(double totalMonetaryValue) {
		TotalMonetaryValue = totalMonetaryValue;
	}
	public String getCustomerContext() {
		return customerContext;
	}
	public void setCustomerContext(String customerContext) {
		this.customerContext = customerContext;
	}
	public String getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}
	public String getStatusDescription() {
		return StatusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		StatusDescription = statusDescription;
	}

}
