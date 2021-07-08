package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;
import java.util.List;

public class Cimm2BCentralItem extends Cimm2BCentralResponseEntity{

	private Double listPrice;
	private Double costPrice;
	private Double customerPrice;
	private String uom;
	private String partIdentifier;
	private String partNumber;
	private Double minimumOrderQuantity;
	private Double orderQuantityInterval;
	private ArrayList<Cimm2BCentralWarehouse> warehouses;
	private Cimm2BCentralWarehouse pricingWarehouse;
	private Double extendedPrice;
	private Integer qty;
	private Integer quantityAvailable;
	private List<Cimm2BCentralLineItem> details;
	private String productStatus;
	private Double totalQtyAvailable;
	private Double discountAmount;
	private String prop65;
	private String prop65Agent;
	private double itemWeight;
	
	public String getProp65() {
		return prop65;
	}
	public void setProp65(String prop65) {
		this.prop65 = prop65;
	}
	public String getProp65Agent() {
		return prop65Agent;
	}
	public void setProp65Agent(String prop65Agent) {
		this.prop65Agent = prop65Agent;
	}
	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	public Double getListPrice() {
		return listPrice;
	}
	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	public Double getCustomerPrice() {
		return customerPrice;
	}
	public void setCustomerPrice(Double customerPrice) {
		this.customerPrice = customerPrice;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getPartIdentifier() {
		return partIdentifier;
	}
	public void setPartIdentifier(String partIdentifier) {
		this.partIdentifier = partIdentifier;
	}
	public Double getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}
	public void setMinimumOrderQuantity(Double minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}
	public Double getOrderQuantityInterval() {
		return orderQuantityInterval;
	}
	public void setOrderQuantityInterval(Double orderQuantityInterval) {
		this.orderQuantityInterval = orderQuantityInterval;
	}
	public ArrayList<Cimm2BCentralWarehouse> getWarehouses() {
		return warehouses;
	}
	public void setWarehouses(ArrayList<Cimm2BCentralWarehouse> warehouses) {
		this.warehouses = warehouses;
	}
	public Cimm2BCentralWarehouse getPricingWarehouse() {
		return pricingWarehouse;
	}
	public void setPricingWarehouse(Cimm2BCentralWarehouse pricingWarehouse) {
		this.pricingWarehouse = pricingWarehouse;
	}
	public Double getExtendedPrice() {
		return extendedPrice;
	}
	public void setExtendedPrice(Double extendedPrice) {
		this.extendedPrice = extendedPrice;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public List<Cimm2BCentralLineItem> getDetails() {
		return details;
	}
	public void setDetails(List<Cimm2BCentralLineItem> details) {
		this.details = details;
	}
	public String getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	public Double getTotalQtyAvailable() {
		return totalQtyAvailable;
	}
	public void setTotalQtyAvailable(Double totalQtyAvailable) {
		this.totalQtyAvailable = totalQtyAvailable;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public double getItemWeight() {
		return itemWeight;
	}
	public void setItemWeight(double itemWeight) {
		this.itemWeight = itemWeight;
	}
}
