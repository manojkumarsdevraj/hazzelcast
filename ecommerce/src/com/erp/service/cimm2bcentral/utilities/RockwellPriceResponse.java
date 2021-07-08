package com.erp.service.cimm2bcentral.utilities;

public class RockwellPriceResponse {
	private String product;
	private String productName;
	private String description;
	private String typicalDelivery;
	private double listPrice;
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTypicalDelivery() {
		return typicalDelivery;
	}
	public void setTypicalDelivery(String typicalDelivery) {
		this.typicalDelivery = typicalDelivery;
	}
	public double getListPrice() {
		return listPrice;
	}
	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

}
