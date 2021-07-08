package com.erp.service.cimm2bcentral.utilities;
import java.util.List;

public class Cimm2BCentralCustomerCard extends Cimm2BCentralResponseEntity{


	private String customerERPId;
	private String cardHolderName;
	private String creditCardNumber;
	private String creditCardType;
	private String cvv;
	private String expiryDate;
	private double amount;
	private Boolean saveCard;
	private String paymentAccountId;
	private String authorizationNumber;
	private String orderERPId;
	private Cimm2BCentralAddress address;
	private String expiryMonth;
	private String expiryYear;
	private String currencyCode;
	private String transactionId;
	private String elementProcessorID;
	private Cimm2BCentralAddress billingAddress;
	private String cardHolderFirstName;
	private String cardHolderLastName;
	private String authType;
	private List<String> customerPaymentIdList;
	private double authAmount;
	private String responseCode;
	private String dataValue;
	
	public double getAuthAmount() {
		return authAmount;
	}
	public void setAuthAmount(double authAmount) {
		this.authAmount = authAmount;
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
	public String getElementProcessorID() {
		return elementProcessorID;
	}
	public void setElementProcessorID(String elementProcessorID) {
		this.elementProcessorID = elementProcessorID;
	}
	public Cimm2BCentralAddress getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(Cimm2BCentralAddress billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
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
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Boolean getSaveCard() {
		return saveCard;
	}
	public void setSaveCard(Boolean saveCard) {
		this.saveCard = saveCard;
	}
	public String getPaymentAccountId() {
		return paymentAccountId;
	}
	public void setPaymentAccountId(String paymentAccountId) {
		this.paymentAccountId = paymentAccountId;
	}
	public String getOrderERPId() {
		return orderERPId;
	}
	public void setOrderERPId(String orderERPId) {
		this.orderERPId = orderERPId;
	}
	public Cimm2BCentralAddress getAddress() {
		return address;
	}
	public void setAddress(Cimm2BCentralAddress address) {
		this.address = address;
	}
	public String getExpiryMonth() {
		return expiryMonth;
	}
	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}
	public String getExpiryYear() {
		return expiryYear;
	}
	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public List<String> getCustomerPaymentIdList() {
		return customerPaymentIdList;
	}
	public void setCustomerPaymentIdList(List<String> customerPaymentIdList) {
		this.customerPaymentIdList = customerPaymentIdList;
	}
	public String getAuthorizationNumber() {
		return authorizationNumber;
	}
	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

}
