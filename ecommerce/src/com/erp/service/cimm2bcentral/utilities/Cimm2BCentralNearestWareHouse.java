package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralNearestWareHouse {
	private String warehouseCode;
	private String warehouseName;
	private Cimm2BCentralAddress warehouseAddress;
	private double distance;
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public Cimm2BCentralAddress getWarehouseAddress() {
		return warehouseAddress;
	}
	public void setWarehouseAddress(Cimm2BCentralAddress warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}
	
}
