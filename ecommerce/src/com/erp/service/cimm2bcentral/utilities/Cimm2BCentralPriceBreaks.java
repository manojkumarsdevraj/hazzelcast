package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralPriceBreaks {
	 private Double customerPrice; 
	 private Double minimumQuantity; 
	 private Double maximumQuantity;
	 private Double discountAmount;
	public Double getCustomerPrice() {
		return customerPrice;
	}
	public void setCustomerPrice(Double customerPrice) {
		this.customerPrice = customerPrice;
	}
	public Double getMinimumQuantity() {
		return minimumQuantity;
	}
	public void setMinimumQuantity(Double minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}
	public Double getMaximumQuantity() {
		return maximumQuantity;
	}
	public void setMaximumQuantity(Double maximumQuantity) {
		this.maximumQuantity = maximumQuantity;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
}


