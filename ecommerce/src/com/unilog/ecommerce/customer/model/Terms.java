package com.unilog.ecommerce.customer.model;

public class Terms {
	private String type;
	private String description;
	
	public Terms() {}
	
	public Terms(String type, String description) {
		this.type = type;
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Terms [type=" + type + ", description=" + description + "]";
	}
	
}
