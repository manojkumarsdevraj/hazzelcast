package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralShipMethodQuantity {
	private String shipMethod;
	private String quantityAvailable;
	private double minimumOrderQuantity;
	private double orderQuantityInterval;
	
	public String getShipMethod() {
		return shipMethod;
	}
	public void setShipMethod(String shipMethod) {
		this.shipMethod = shipMethod;
	}
	public String getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(String quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	public double getOrderQuantityInterval() {
		return orderQuantityInterval;
	}
	public void setOrderQuantityInterval(double orderQuantityInterval) {
		this.orderQuantityInterval = orderQuantityInterval;
	}
	public double getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}
	public void setMinimumOrderQuantity(double minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}
	
}
