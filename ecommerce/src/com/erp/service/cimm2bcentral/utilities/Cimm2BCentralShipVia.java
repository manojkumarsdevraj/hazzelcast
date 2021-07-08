package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralShipVia {

	private String shipViaCode;
	private String shipViaDescription;
	private String shipViaErpId;
	private String accountNumber;
	private String carrierTrackingNumber;
	private double freight;
	private double orderTotal;
	private double orderSubTotal;
	private double tax;
	private String authorizedPickupPerson;
	private String shippingInstruction;
	private String orderNumber;
	private Cimm2BCentralSubOrderCriteria subOrderCriteria;
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getFreight() {
		return freight;
	}
	public void setFreight(double freight) {
		this.freight = freight;
	}
	public double getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}
	public double getOrderSubTotal() {
		return orderSubTotal;
	}
	public void setOrderSubTotal(double orderSubTotal) {
		this.orderSubTotal = orderSubTotal;
	}
	public String getShipViaCode() {
		return shipViaCode;
	}
	public void setShipViaCode(String shipViaCode) {
		this.shipViaCode = shipViaCode;
	}
	public String getShipViaDescription() {
		return shipViaDescription;
	}
	public void setShipViaDescription(String shipViaDescription) {
		this.shipViaDescription = shipViaDescription;
	}
	public String getShipViaErpId() {
		return shipViaErpId;
	}
	public void setShipViaErpId(String shipViaErpId) {
		this.shipViaErpId = shipViaErpId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getCarrierTrackingNumber() {
		return carrierTrackingNumber;
	}
	public void setCarrierTrackingNumber(String carrierTrackingNumber) {
		this.carrierTrackingNumber = carrierTrackingNumber;
	}
	public String getAuthorizedPickupPerson() {
		return authorizedPickupPerson;
	}
	public void setAuthorizedPickupPerson(String authorizedPickupPerson) {
		this.authorizedPickupPerson = authorizedPickupPerson;
	}
	public String getShippingInstruction() {
		return shippingInstruction;
	}
	public void setShippingInstruction(String shippingInstruction) {
		this.shippingInstruction = shippingInstruction;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Cimm2BCentralSubOrderCriteria getSubOrderCriteria() {
		return subOrderCriteria;
	}
	public void setSubOrderCriteria(Cimm2BCentralSubOrderCriteria subOrderCriteria) {
		this.subOrderCriteria = subOrderCriteria;
	}
}
