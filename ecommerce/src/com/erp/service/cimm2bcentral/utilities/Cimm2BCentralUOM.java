package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralUOM {
	private String customerERPId;
	private String partIdentifiers;
	private String uom;
	private String uomQty;
	private int uomQuantity;
	private double customerPrice;
	private double quantityAvailable;
	private double allBranchAvailability;
	/**
	 * @return the customerERPId
	 */
	public String getCustomerERPId() {
		return customerERPId;
	}
	/**
	 * @param customerERPId the customerERPId to set
	 */
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	/**
	 * @return the partIdentifiers
	 */
	public String getPartIdentifiers() {
		return partIdentifiers;
	}
	/**
	 * @param partIdentifiers the partIdentifiers to set
	 */
	public void setPartIdentifiers(String partIdentifiers) {
		this.partIdentifiers = partIdentifiers;
	}
	/**
	 * @return the uom
	 */
	public String getUom() {
		return uom;
	}
	/**
	 * @param uom the uom to set
	 */
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getUomQty() {
		return uomQty;
	}
	public void setUomQty(String uomQty) {
		this.uomQty = uomQty;
	}
	public int getUomQuantity() {
		return uomQuantity;
	}
	public void setUomQuantity(int uomQuantity) {
		this.uomQuantity = uomQuantity;
	}
	public double getCustomerPrice() {
		return customerPrice;
	}
	public void setCustomerPrice(double customerPrice) {
		this.customerPrice = customerPrice;
	}
	public double getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(double quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	public double getAllBranchAvailability() {
		return allBranchAvailability;
	}
	public void setAllBranchAvailability(double allBranchAvailability) {
		this.allBranchAvailability = allBranchAvailability;
	}
	
}
