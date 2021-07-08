package com.unilog.express.transaction.model;

import javax.xml.bind.annotation.XmlElement;

public class Item {
	private String paymentAccountID;
	private String paymentAccountType;
	private String truncatedCardNumber;
	private String expirationMonth;
	private String expirationYear;
	private String transactionSetupID;
	private String paymentBrand;
	public String getPaymentAccountID() {
		return paymentAccountID;
	}
	
	@XmlElement(name = "PaymentAccountID")
	public void setPaymentAccountID(String paymentAccountID) {
		this.paymentAccountID = paymentAccountID;
	}
	public String getPaymentAccountType() {
		return paymentAccountType;
	}
	
	@XmlElement(name = "PaymentAccountType")
	public void setPaymentAccountType(String paymentAccountType) {
		this.paymentAccountType = paymentAccountType;
	}
	public String getTruncatedCardNumber() {
		return truncatedCardNumber;
	}
	
	@XmlElement(name = "TruncatedCardNumber")
	public void setTruncatedCardNumber(String truncatedCardNumber) {
		this.truncatedCardNumber = truncatedCardNumber;
	}
	public String getExpirationMonth() {
		return expirationMonth;
	}
	
	@XmlElement(name = "ExpirationMonth")
	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}
	public String getExpirationYear() {
		return expirationYear;
	}
	
	@XmlElement(name = "ExpirationYear")
	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}
	public String getTransactionSetupID() {
		return transactionSetupID;
	}
	
	@XmlElement(name = "TransactionSetupID")
	public void setTransactionSetupID(String transactionSetupID) {
		this.transactionSetupID = transactionSetupID;
	}

	@XmlElement(name = "PaymentBrand")
	public void setPaymentBrand(String paymentBrand) {
		this.paymentBrand = paymentBrand;
	}

	public String getPaymentBrand() {
		return paymentBrand;
	}
	
	
}
