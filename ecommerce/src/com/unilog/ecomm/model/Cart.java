package com.unilog.ecomm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cart {
	
	private List<LineItem> lineItems;
	private List<Discount> discounts; // applied discounts from rules engine
	private String customerName;
	private String customerGroupName;
	private String userName;
	private long userId; // Cimm users Primary Key
	private long buyingCompanyId; // Cimm Buying company Primary Key
	private String userGroupName;
	private double orderTotal; //order total after discount
	private Date purcahseDate;
	private String website;
	private double total; // order total before discount
	private boolean freeShipping;
	private boolean itemDiscountAvailed;
	private List<Discount> availedDiscounts; //trade disount
	private List<Coupon> rejectedCoupons;
	private double discount; // availed discount value
	private String warehouseCode;
	private String wareHouseName;
	private String selectedShipMethod;
	private Discount availedShippingDiscount; //shipping discount
	private String nonShipChargeShipMethods; //',' separated ship methods to exclude free shipping discount apply
	private String appliedDiscountStrategy;
	
	public Cart(){
		purcahseDate = new Date();
		 discounts = new ArrayList<Discount>();
		 lineItems = new ArrayList<LineItem>();
		 rejectedCoupons = new ArrayList<Coupon>();
		 availedDiscounts = new ArrayList<Discount>();
	}
	
	
	public void addDiscount(Discount discount){
		discounts.add(discount);
	}
	
	public List<LineItem> getLineItems() {
		return lineItems;
	}
	
	public List<Discount> getDiscounts() {
		return discounts;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
	}


	public long getBuyingCompanyId() {
		return buyingCompanyId;
	}


	public void setBuyingCompanyId(long buyingCompanyId) {
		this.buyingCompanyId = buyingCompanyId;
	}


	public double getOrderTotal() {
		return orderTotal;
	}


	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public Date getPurcahseDate() {
		return purcahseDate;
	}
	public void setPurcahseDate(Date purcahseDate) {
		this.purcahseDate = purcahseDate;
	}
	
	public double getTotal() {
		return total;
	}


	public void setTotal(double total) {
		this.total = total;
	}


	public boolean isFreeShipping() {
		return freeShipping;
	}

	public void setFreeShipping(boolean freeShipping) {
		this.freeShipping = freeShipping;
	}
	

	public boolean isItemDiscountAvailed() {
		return itemDiscountAvailed;
	}


	public void setItemDiscountAvailed(boolean itemDiscountAvailed) {
		this.itemDiscountAvailed = itemDiscountAvailed;
	}



	public List<Discount> getAvailedDiscounts() {
		return availedDiscounts;
	}


	public void setAvailedDiscounts(List<Discount> availedDiscounts) {
		this.availedDiscounts = availedDiscounts;
	}


	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}


	public String getCustomerGroupName() {
		return customerGroupName;
	}


	public void setCustomerGroupName(String customerGroupName) {
		this.customerGroupName = customerGroupName;
	}


	public String getUserGroupName() {
		return userGroupName;
	}


	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}
	
	


	public String getWarehouseCode() {
		return warehouseCode;
	}


	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}


	public String getWareHouseName() {
		return wareHouseName;
	}


	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}

	public String getSelectedShipMethod() {
		return selectedShipMethod;
	}


	public void setSelectedShipMethod(String selectedShipMethod) {
		this.selectedShipMethod = selectedShipMethod;
	}


	public Discount getAvailedShippingDiscount() {
		return availedShippingDiscount;
	}

	public void setAvailedShippingDiscount(Discount availedShippingDiscount) {
		this.availedShippingDiscount = availedShippingDiscount;
	}
	
	public String getNonShipChargeShipMethods() {
		return nonShipChargeShipMethods;
	}


	public void setNonShipChargeShipMethods(String nonShipChargeShipMethods) {
		this.nonShipChargeShipMethods = nonShipChargeShipMethods;
	}
	

	public String getAppliedDiscountStrategy() {
		return appliedDiscountStrategy;
	}


	public void setAppliedDiscountStrategy(String appliedDiscountStrategy) {
		this.appliedDiscountStrategy = appliedDiscountStrategy;
	}


	public List<Coupon> getRejectedCoupons() {
		return rejectedCoupons;
	}


	public void setRejectedCoupons(List<Coupon> rejectedCoupons) {
		this.rejectedCoupons = rejectedCoupons;
	}
	
	
	public void setSelectedShipMethod(ArrayList<String> shipVia) {
		this.selectedShipMethod = selectedShipMethod;
	}

}
