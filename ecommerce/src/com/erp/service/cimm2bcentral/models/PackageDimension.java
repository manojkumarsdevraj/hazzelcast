package com.erp.service.cimm2bcentral.models;

public class PackageDimension {
	private String uom;
	private int height;
	private int width;
	private int length;
	
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public PackageDimension(){
		
	}
	
	public  PackageDimension(String uom, int height, int width, int length){
		this.uom = uom;
		this.height = height;
		this.width = width;
		this.length = length;
	}
	
}
