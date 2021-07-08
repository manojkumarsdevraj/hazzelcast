package com.unilog.sales;

import java.util.ArrayList;

public class ProjectManagementModel {

	
	 private String entityId;
	
	// Report Header details
	 private String jobId ;
	 private String jobName;
     private String jobNumber;
    private String projectManager;
    private String email;
    private String empPhone;
    private String customerName;
    private String primaryContactName;
    private String shipToAddress;
    private Double shipCity;
    private String contactInfo;
    private String customerPO;
  
    //ReportBody details
    private String sortOrder;
    private String fixtureType;
	private String vendor;
    private String description;
    private int orderQty;
    private String statusDetail;
    private int shipQty;
    private String actualShipDate;
    private String shipper;
    

	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	private String trackingNumber;
    private String externalNote;
    private String invNumber;
    private String status;
    
    
    
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	public String getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmpPhone() {
		return empPhone;
	}
	public void setEmpPhone(String empPhone) {
		this.empPhone = empPhone;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPrimaryContactName() {
		return primaryContactName;
	}
	public void setPrimaryContactName(String primaryContactName) {
		this.primaryContactName = primaryContactName;
	}
	public String getShipToAddress() {
		return shipToAddress;
	}
	public void setShipToAddress(String shipToAddress) {
		this.shipToAddress = shipToAddress;
	}
	public Double getShipCity() {
		return shipCity;
	}
	public void setShipCity(Double shipCity) {
		this.shipCity = shipCity;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	public String getCustomerPO() {
		return customerPO;
	}
	public void setCustomerPO(String customerPO) {
		this.customerPO = customerPO;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getFixtureType() {
		return fixtureType;
	}
	public void setFixtureType(String fixtureType) {
		this.fixtureType = fixtureType;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}
	public String getStatusDetail() {
		return statusDetail;
	}
	public void setStatusDetail(String statusDetail) {
		this.statusDetail = statusDetail;
	}
	public int getShipQty() {
		return shipQty;
	}
	public void setShipQty(int shipQty) {
		this.shipQty = shipQty;
	}
	public String getActualShipDate() {
		return actualShipDate;
	}
	public void setActualShipDate(String actualShipDate) {
		this.actualShipDate = actualShipDate;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getExternalNote() {
		return externalNote;
	}
	public void setExternalNote(String externalNote) {
		this.externalNote = externalNote;
	}
	public String getInvNumber() {
		return invNumber;
	}
	public void setInvNumber(String invNumber) {
		this.invNumber = invNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
	
}
