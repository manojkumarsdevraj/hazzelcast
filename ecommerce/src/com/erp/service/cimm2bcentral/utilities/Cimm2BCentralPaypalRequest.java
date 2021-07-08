package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralPaypalRequest {
	private String currency;
	private String description;
	private double shipping;
	private String totalAmount;
	private double taxAmount;
	private double discount;
	private ArrayList<Cimm2BCentralPaypalItemList> itemList;
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getShipping() {
		return shipping;
	}
	public void setShipping(double shipping) {
		this.shipping = shipping;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public ArrayList<Cimm2BCentralPaypalItemList> getItemList() {
		return itemList;
	}
	public void setItemList(ArrayList<Cimm2BCentralPaypalItemList> itemList) {
		this.itemList = itemList;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	
}
