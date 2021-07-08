package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralAuthenticationResponse extends Cimm2BCentralResponseEntity {
	
	private String customerId;
	private String customerERPId;
	private String customerName;
	private String address;
	private String billToCustomerId;
	private String billToCustomerERPId;
	private String customerLocations;
	private String termsType;
	private String termsTypeDescription;
	private String shippingMethods;
	private String homeBranch;
	private String salesPersonCode;
	private String codFlag;
	private String defaultShipLocationId;
	private String arCustomerNumber;
	private String poRequired;
	private String gasPORequired;
	private String defaultShipVia;
	private ArrayList<Cimm2BCentralContact> contacts;
	private String customerPoNumber;
	private String fuelSurcharge;
	private String minimumOrderAmount;
	private String prepaidFreightAmount;
	private String userERPId;
	private String sessionId;
	private Cimm2BCentralCustomer customerDetail;
	
	
	public Cimm2BCentralCustomer getCustomerDetail() {
		return customerDetail;
	}
	public void setCustomerDetail(Cimm2BCentralCustomer customerDetail) {
		this.customerDetail = customerDetail;
	}
	public String getUserERPId() {
		return userERPId;
	}
	public void setUserERPId(String userERPId) {
		this.userERPId = userERPId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBillToCustomerId() {
		return billToCustomerId;
	}
	public void setBillToCustomerId(String billToCustomerId) {
		this.billToCustomerId = billToCustomerId;
	}
	public String getBillToCustomerERPId() {
		return billToCustomerERPId;
	}
	public void setBillToCustomerERPId(String billToCustomerERPId) {
		this.billToCustomerERPId = billToCustomerERPId;
	}
	public String getCustomerLocations() {
		return customerLocations;
	}
	public void setCustomerLocations(String customerLocations) {
		this.customerLocations = customerLocations;
	}
	public String getTermsType() {
		return termsType;
	}
	public void setTermsType(String termsType) {
		this.termsType = termsType;
	}
	public String getTermsTypeDescription() {
		return termsTypeDescription;
	}
	public void setTermsTypeDescription(String termsTypeDescription) {
		this.termsTypeDescription = termsTypeDescription;
	}
	public String getShippingMethods() {
		return shippingMethods;
	}
	public void setShippingMethods(String shippingMethods) {
		this.shippingMethods = shippingMethods;
	}
	public String getHomeBranch() {
		return homeBranch;
	}
	public void setHomeBranch(String homeBranch) {
		this.homeBranch = homeBranch;
	}
	public String getSalesPersonCode() {
		return salesPersonCode;
	}
	public void setSalesPersonCode(String salesPersonCode) {
		this.salesPersonCode = salesPersonCode;
	}
	public String getCodFlag() {
		return codFlag;
	}
	public void setCodFlag(String codFlag) {
		this.codFlag = codFlag;
	}
	public String getDefaultShipLocationId() {
		return defaultShipLocationId;
	}
	public void setDefaultShipLocationId(String defaultShipLocationId) {
		this.defaultShipLocationId = defaultShipLocationId;
	}
	public String getArCustomerNumber() {
		return arCustomerNumber;
	}
	public void setArCustomerNumber(String arCustomerNumber) {
		this.arCustomerNumber = arCustomerNumber;
	}
	public String getPoRequired() {
		return poRequired;
	}
	public void setPoRequired(String poRequired) {
		this.poRequired = poRequired;
	}
	public String getGasPORequired() {
		return gasPORequired;
	}
	public void setGasPORequired(String gasPORequired) {
		this.gasPORequired = gasPORequired;
	}
	public String getDefaultShipVia() {
		return defaultShipVia;
	}
	public void setDefaultShipVia(String defaultShipVia) {
		this.defaultShipVia = defaultShipVia;
	}
	public ArrayList<Cimm2BCentralContact> getContacts() {
		return contacts;
	}
	public void setContacts(ArrayList<Cimm2BCentralContact> contacts) {
		this.contacts = contacts;
	}
	public String getCustomerPoNumber() {
		return customerPoNumber;
	}
	public void setCustomerPoNumber(String customerPoNumber) {
		this.customerPoNumber = customerPoNumber;
	}
	public String getFuelSurcharge() {
		return fuelSurcharge;
	}
	public void setFuelSurcharge(String fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}
	public String getMinimumOrderAmount() {
		return minimumOrderAmount;
	}
	public void setMinimumOrderAmount(String minimumOrderAmount) {
		this.minimumOrderAmount = minimumOrderAmount;
	}
	public String getPrepaidFreightAmount() {
		return prepaidFreightAmount;
	}
	public void setPrepaidFreightAmount(String prepaidFreightAmount) {
		this.prepaidFreightAmount = prepaidFreightAmount;
	}

}
