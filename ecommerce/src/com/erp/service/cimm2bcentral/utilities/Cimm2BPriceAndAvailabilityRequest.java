package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Cimm2BPriceAndAvailabilityRequest extends Cimm2BCentralResponseEntity{

	private String customerERPId;
	private String shipTo;
	private Set<String> warehouseCodes;
	private List<String> partIdentifiers;
	private String pricingWarehouseCode;
	private boolean directFromManufacturer;
	private ArrayList<Cimm2BCentralLineItem> lineItems;
	private boolean includeProductUOMlist;
	private boolean includeAllBranchAvailability;
	private String contractUID;
	private boolean includeCompanyAvailability;
	private String includeExcludeWarehouse;
	
	public boolean isIncludeCompanyAvailability() {
		return includeCompanyAvailability;
	}
	public void setIncludeCompanyAvailability(boolean includeCompanyAvailability) {
		this.includeCompanyAvailability = includeCompanyAvailability;
	}
	public String getIncludeExcludeWarehouse() {
		return includeExcludeWarehouse;
	}
	public void setIncludeExcludeWarehouse(String includeExcludeWarehouse) {
		this.includeExcludeWarehouse = includeExcludeWarehouse;
	}
	public String getPricingWarehouseCode() {
		return pricingWarehouseCode;
	}
	public void setPricingWarehouseCode(String pricingWarehouseCode) {
		this.pricingWarehouseCode = pricingWarehouseCode;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getShipTo() {
		return shipTo;
	}
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	public Set<String> getWarehouseCodes() {
		return warehouseCodes;
	}
	public void setWarehouseCodes(Set<String> warehouseCodes) {
		this.warehouseCodes = warehouseCodes;
	}
	public List<String> getPartIdentifiers() {
		return partIdentifiers;
	}
	public void setPartIdentifiers(List<String> partIdentifiers) {
		this.partIdentifiers = partIdentifiers;
	}
	public boolean isDirectFromManufacturer() {
		return directFromManufacturer;
	}
	public void setDirectFromManufacturer(boolean directFromManufacturer) {
		this.directFromManufacturer = directFromManufacturer;
	}
	public ArrayList<Cimm2BCentralLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(ArrayList<Cimm2BCentralLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	public boolean isIncludeProductUOMlist() {
		return includeProductUOMlist;
	}
	public void setIncludeProductUOMlist(boolean includeProductUOMlist) {
		this.includeProductUOMlist = includeProductUOMlist;
	}
	public boolean isIncludeAllBranchAvailability() {
		return includeAllBranchAvailability;
	}
	public void setIncludeAllBranchAvailability(boolean includeAllBranchAvailability) {
		this.includeAllBranchAvailability = includeAllBranchAvailability;
	}
	public String getContractUID() {
		return contractUID;
	}
	public void setContractUID(String contractUID) {
		this.contractUID = contractUID;
	}
	
}
