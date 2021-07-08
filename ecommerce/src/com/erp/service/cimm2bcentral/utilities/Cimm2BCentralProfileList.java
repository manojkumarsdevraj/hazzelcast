package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralProfileList {
	private String creditCardNumber;
	private String creditCardType;
	private String expiryMonth;
	private String expiryYear;
	private ArrayList<String> customerPaymentIdList;
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
	public ArrayList<String> getCustomerPaymentIdList() {
		return customerPaymentIdList;
	}
	public void setCustomerPaymentIdList(ArrayList<String> customerPaymentIdList) {
		this.customerPaymentIdList = customerPaymentIdList;
	}
}
