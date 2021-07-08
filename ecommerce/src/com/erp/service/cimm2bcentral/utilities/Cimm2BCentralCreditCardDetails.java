package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralCreditCardDetails extends Cimm2BCentralResponseEntity{
	
	private String cardHolderName;
	private String returnUrl;
	private Cimm2BCentralAddress address;
	private String elementSetupId;
	private String userName;
	private String password;
	
	private String creditCardNumber;
	private String creditCardType;
	private String paymentAccountId;
	private String expiryDate;
	
	private String customerERPId;
	private String cardHolderFirstName;
	private String cardHolderLastName;
	private String cvv;

	private String elementProcessorId;
	
	public String getElementProcessorId() {
		return elementProcessorId;
	}
	public void setElementProcessorId(String elementProcessorId) {
		this.elementProcessorId = elementProcessorId;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getCardHolderFirstName() {
		return cardHolderFirstName;
	}
	public void setCardHolderFirstName(String cardHolderFirstName) {
		this.cardHolderFirstName = cardHolderFirstName;
	}
	public String getCardHolderLastName() {
		return cardHolderLastName;
	}
	public void setCardHolderLastName(String cardHolderLastName) {
		this.cardHolderLastName = cardHolderLastName;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public String getCreditCardType() {
		return creditCardType;
	}
	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}
	public String getPaymentAccountId() {
		return paymentAccountId;
	}
	public void setPaymentAccountId(String paymentAccountId) {
		this.paymentAccountId = paymentAccountId;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getElementSetupId() {
		return elementSetupId;
	}
	public void setElementSetupId(String elementSetupId) {
		this.elementSetupId = elementSetupId;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public Cimm2BCentralAddress getAddress() {
		return address;
	}
	public void setAddress(Cimm2BCentralAddress address) {
		this.address = address;
	}
	
		
}
