package com.unilog.ecomm.model;


public class Discount {
	
	private Coupon discountCoupon;
	private DiscountType discountType;
	private int discountInPercent;
	private double discountValue;
	
	
	public Coupon getDiscountCoupon() {
		return discountCoupon;
	}
	public void setDiscountCoupon(Coupon discountCoupon) {
		this.discountCoupon = discountCoupon;
	}
	public DiscountType getDiscountType() {
		return discountType;
	}
	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}
	public int getDiscountInPercent() {
		return discountInPercent;
	}
	public void setDiscountInPercent(int discountInPercent) {
		this.discountInPercent = discountInPercent;
	}
	
	public double getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(double discountValue) {
		this.discountValue = discountValue;
	}
	@Override
	public String toString() {
		return "Discount [discountCoupon=" + discountCoupon + ", discountType="
				+ discountType + ", discountInPercent=" + discountInPercent
				+ ", discountValue=" + discountValue + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((discountCoupon == null) ? 0 : discountCoupon.hashCode());
		result = prime * result
				+ ((discountType == null) ? 0 : discountType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Discount other = (Discount) obj;
		if (discountCoupon == null) {
			if (other.discountCoupon != null)
				return false;
		} else if (!discountCoupon.equals(other.discountCoupon))
			return false;
		if (discountType != other.discountType)
			return false;
		return true;
	}
	

}
