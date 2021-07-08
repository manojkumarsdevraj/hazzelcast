package com.unilog.rockwell.model;

import com.google.gson.annotations.Expose;

public class BomModel {
	@Expose private String LineID;
	@Expose private String Product;
	@Expose private int Qty;
	@Expose private String Description;
	@Expose private double ListPrice;
	@Expose private String DS;
	@Expose private String PGC;
	
	
	public double getListPrice() {
		return ListPrice;
	}
	public void setListPrice(double listPrice) {
		ListPrice = listPrice;
	}
	public String getDS() {
		return DS;
	}
	public void setDS(String dS) {
		DS = dS;
	}
	public String getPGC() {
		return PGC;
	}
	public void setPGC(String pGC) {
		PGC = pGC;
	}
	public String getLineID() {
		return LineID;
	}
	public void setLineID(String lineID) {
		LineID = lineID;
	}
	public String getProduct() {
		return Product;
	}
	public void setProduct(String product) {
		Product = product;
	}
	public int getQty() {
		return Qty;
	}
	public void setQty(int qty) {
		Qty = qty;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	
}
