package com.unilog.model;

public class TaxonomyDataModel {
	private int id;
	private int userEdited;
	private String status;
	private String name;
    private String description;
    private int supplierId;
    private String cimmOrExternalSystem;
    private int topBannerId;
    private int leftBannerId;
    private int rightBannerId;
    private int bottomBannerId;
    
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserEdited() {
		return userEdited;
	}
	public void setUserEdited(int userEdited) {
		this.userEdited = userEdited;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public String getCimmOrExternalSystem() {
		return cimmOrExternalSystem;
	}
	public void setCimmOrExternalSystem(String cimmOrExternalSystem) {
		this.cimmOrExternalSystem = cimmOrExternalSystem;
	}
	public int getTopBannerId() {
		return topBannerId;
	}
	public void setTopBannerId(int topBannerId) {
		this.topBannerId = topBannerId;
	}
	public int getLeftBannerId() {
		return leftBannerId;
	}
	public void setLeftBannerId(int leftBannerId) {
		this.leftBannerId = leftBannerId;
	}
	public int getRightBannerId() {
		return rightBannerId;
	}
	public void setRightBannerId(int rightBannerId) {
		this.rightBannerId = rightBannerId;
	}
	public int getBottomBannerId() {
		return bottomBannerId;
	}
	public void setBottomBannerId(int bottomBannerId) {
		this.bottomBannerId = bottomBannerId;
	}
    
    
    
}
