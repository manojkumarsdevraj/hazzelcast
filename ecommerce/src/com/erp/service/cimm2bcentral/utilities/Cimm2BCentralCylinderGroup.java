package com.erp.service.cimm2bcentral.utilities;

public class Cimm2BCentralCylinderGroup extends Cimm2BCentralResponseEntity{
	
	private String cylinderGroup;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCylinderGroup() {
		return cylinderGroup;
	}
	public void setCylinderGroup(String cylinderGroup) {
		this.cylinderGroup = cylinderGroup;
	}

}
