package com.paymentgateway.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreditCardManagementModel {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private double total;
	private String externalRefNumber;
	private String merchantId;
	private String hostPassword;
	private String hostKey;
	private String invoiceNumber;
	private String encryptedManifest;
	private String amount;
	
	//card connect
	private String ccToken;
	private String ccExp;
	private String ccCvv2VrfyCode;
	private double ccAmount;
	private int orderNumber;
	private String cardHolder;
	private String address1;
	private String city;
	private String state;
	private String zipCode;
	private String paymentAccountType;
	private String ccCurrencyCode;
	private String ccCountry;	
	private String ENDPOINT;
	private String USERNAME;
	private String PASSWORD;
	

	public String getCcCurrencyCode() {
		return ccCurrencyCode;
	}

	public void setCcCurrencyCode(String ccCurrencyCode) {
		this.ccCurrencyCode = ccCurrencyCode;
	}

	public String getCcCountry() {
		return ccCountry;
	}

	public void setCcCountry(String ccCountry) {
		this.ccCountry = ccCountry;
	}

	public String getENDPOINT() {
		return ENDPOINT;
	}

	public void setENDPOINT(String eNDPOINT) {
		ENDPOINT = eNDPOINT;
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getPaymentAccountType() {
		return paymentAccountType;
	}

	public void setPaymentAccountType(String paymentAccountType) {
		this.paymentAccountType = paymentAccountType;
	}

	public String getCcToken() {
		return ccToken;
	}

	public void setCcToken(String ccToken) {
		this.ccToken = ccToken;
	}

	public String getCcExp() {
		return ccExp;
	}

	public void setCcExp(String ccExp) {
		this.ccExp = ccExp;
	}

	public String getCcCvv2VrfyCode() {
		return ccCvv2VrfyCode;
	}

	public void setCcCvv2VrfyCode(String ccCvv2VrfyCode) {
		this.ccCvv2VrfyCode = ccCvv2VrfyCode;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public double getCcAmount() {
		return ccAmount;
	}

	public void setCcAmount(double ccAmount) {
		this.ccAmount = ccAmount;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getEncryptedManifest() {
		return encryptedManifest;
	}

	public void setEncryptedManifest(String encryptedManifest) {
		this.encryptedManifest = encryptedManifest;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getHostPassword() {
		return hostPassword;
	}

	public void setHostPassword(String hostPassword) {
		this.hostPassword = hostPassword;
	}

	public String getHostKey() {
		return hostKey;
	}

	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getExternalRefNumber() {
		return externalRefNumber;
	}

	public void setExternalRefNumber(String externalRefNumber) {
		this.externalRefNumber = externalRefNumber;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
