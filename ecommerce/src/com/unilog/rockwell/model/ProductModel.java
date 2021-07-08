package com.unilog.rockwell.model;

import java.util.List;

public class ProductModel {
	private String ProductName;
	private String Description;
	private int Qty;
	private boolean GroupMarker;
	private String RepPhoto;
	private String Product;
	private List<ProductModel> Accessories;
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getQty() {
		return Qty;
	}
	public void setQty(int qty) {
		Qty = qty;
	}
	public boolean isGroupMarker() {
		return GroupMarker;
	}
	public void setGroupMarker(boolean groupMarker) {
		GroupMarker = groupMarker;
	}
	public String getRepPhoto() {
		return RepPhoto;
	}
	public void setRepPhoto(String repPhoto) {
		RepPhoto = repPhoto;
	}
	public String getProduct() {
		return Product;
	}
	public void setProduct(String product) {
		Product = product;
	}
	public void setAccessories(List<ProductModel> accessories) {
		Accessories = accessories;
	}
	public List<ProductModel> getAccessories() {
		return Accessories;
	}
	
	
	
}
