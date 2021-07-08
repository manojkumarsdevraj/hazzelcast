package com.erp.service.cimm2bcentral.models;

public class PackageInfo {
	private String uom;
	private String packageCode;
	private String packageInstruction;
	private String weight;
	
	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPackageInstruction() {
		return packageInstruction;
	}

	public void setPackageInstruction(String packageInstruction) {
		this.packageInstruction = packageInstruction;
	}

	public PackageInfo(){
		
	}
	public PackageInfo(String packageCode,String packageInstruction, String uom, String weight){
		this.packageCode = packageCode;
		this.packageInstruction = packageInstruction;
		this.uom = uom;
		this.weight = weight;
	}
	public PackageInfo(String uom, String weight){
		this.uom = uom;
		this.weight = weight;
	}
	
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
}
