package com.unilog.ecomm.model;

import java.util.ArrayList;
import java.util.List;

public class LineItem {
	
	private int lineItemId;
	private String partNumber;
	private String catagoryCode;
	private String catagoryname;
	private String warehouseCode;
	private String wareHouseName;
	private List<Discount> discounts;
	private double orderQuantity;
	private double price;
	private double extendedPrice;
	private double netPrice;
	private Discount availedDiscount;
	private double discount; // availed discount value
	private String itemGroupName;
	
	public LineItem(){
		discounts = new ArrayList<Discount>();
	}

	
	public void addDiscount(Discount discount){
		discounts.add(discount);
	}

	public int getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(int lineItemId) {
		this.lineItemId = lineItemId;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	public List<Discount> getDiscounts() {
		return discounts;
	}
	public String getCatagoryCode() {
		return catagoryCode;
	}
	public void setCatagoryCode(String catagoryCode) {
		this.catagoryCode = catagoryCode;
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
	public String getCatagoryname() {
		return catagoryname;
	}
	public void setCatagoryname(String catagoryname) {
		this.catagoryname = catagoryname;
	}
	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}
	
	public double getOrderQuantity() {
		return orderQuantity;
	}


	public void setOrderQuantity(double orderQuantity) {
		this.orderQuantity = orderQuantity;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public double getExtendedPrice() {
		return extendedPrice;
	}


	public void setExtendedPrice(double extendedPrice) {
		this.extendedPrice = extendedPrice;
	}


	public double getNetPrice() {
		return netPrice;
	}


	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}
	
	
	public Discount getAvailedDiscount() {
		return availedDiscount;
	}


	public void setAvailedDiscount(Discount availedDiscount) {
		this.availedDiscount = availedDiscount;
	}
	
	public double getDiscount() {
		return discount;
	}


	public void setDiscount(double discount) {
		this.discount = discount;
	}
	

	public String getItemGroupName() {
		return itemGroupName;
	}


	public void setItemGroupName(String itemGroupName) {
		this.itemGroupName = itemGroupName;
	}


	@Override
	public String toString() {
		return "LineItem [lineItemId=" + lineItemId + ", partNumber="
				+ partNumber + ", catagoryname=" + catagoryname
				+ ", wareHouseName=" + wareHouseName + ", discounts="
				+ discounts + ", orderQuantity=" + orderQuantity + ", price="
				+ price + ", extendedPrice=" + extendedPrice + ", netPrice="
				+ netPrice + ", availedDiscount=" + availedDiscount + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((catagoryname == null) ? 0 : catagoryname.hashCode());
		result = prime * result
				+ ((itemGroupName == null) ? 0 : itemGroupName.hashCode());
		long temp;
		temp = Double.doubleToLongBits(orderQuantity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((partNumber == null) ? 0 : partNumber.hashCode());
		result = prime * result
				+ ((wareHouseName == null) ? 0 : wareHouseName.hashCode());
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
		LineItem other = (LineItem) obj;
		if (catagoryname == null) {
			if (other.catagoryname != null)
				return false;
		} else if (!catagoryname.equals(other.catagoryname))
			return false;
		if (itemGroupName == null) {
			if (other.itemGroupName != null)
				return false;
		} else if (!itemGroupName.equals(other.itemGroupName))
			return false;
		if (Double.doubleToLongBits(orderQuantity) != Double
				.doubleToLongBits(other.orderQuantity))
			return false;
		if (partNumber == null) {
			if (other.partNumber != null)
				return false;
		} else if (!partNumber.equals(other.partNumber))
			return false;
		if (wareHouseName == null) {
			if (other.wareHouseName != null)
				return false;
		} else if (!wareHouseName.equals(other.wareHouseName))
			return false;
		return true;
	}

}
