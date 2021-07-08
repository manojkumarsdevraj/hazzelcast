package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralPaypalResponse {

	private String state;
	private String createdTime;
	private String paymentId;
	private String redirectUrl;
	private String description;
	private Double totalAmount;
	private Double shipping;
	private Double subTotal;
	private Double tax;
	private String currency;
	private ArrayList<Cimm2BCentralPaypalItemList> itemList;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getShipping() {
		return shipping;
	}
	public void setShipping(Double shipping) {
		this.shipping = shipping;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public ArrayList<Cimm2BCentralPaypalItemList> getItemList() {
		return itemList;
	}
	public void setItemList(ArrayList<Cimm2BCentralPaypalItemList> itemList) {
		this.itemList = itemList;
	}
}
