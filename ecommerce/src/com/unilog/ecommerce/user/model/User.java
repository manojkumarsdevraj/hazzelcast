package com.unilog.ecommerce.user.model;

import com.unilog.ecommerce.customer.model.Terms;

public class User {
	private String accountName;
	private String accountNumber;  
	
	private int customerId;   	// buying company id
	private int userId;			// CIMM user Id
	private int parentUserId;
	private String externalUserId;	// ERP userId
	private String defaultWareHouseCode;
	
	private String salutation;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private String confirmPassword;
	
	private Address address;
	private int billAddressId;
	private int shipAddressId;
	private String shipToId;
	
	private String phoneNumber;
	private String emailId;
	
	private int subsetId;
	private String subsetName;
	
	private boolean generalSubsetAllowed;
	
	private String status;
	private String jobTitle;
	private AccessPermission accessPermission;
	private Terms terms;
	private String subscribeForNewLetter;
	private String creditAccountRequest;
	
	public String getSubscribeForNewLetter() {
		return subscribeForNewLetter;
	}
	public void setSubscribeForNewLetter(String subscribeForNewLetter) {
		this.subscribeForNewLetter = subscribeForNewLetter;
	}
	public String getCreditAccountRequest() {
		return creditAccountRequest;
	}
	public void setCreditAccountRequest(String creditAccountRequest) {
		this.creditAccountRequest = creditAccountRequest;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public int getSubsetId() {
		return subsetId;
	}
	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}
	public String getSubsetName() {
		return subsetName;
	}
	public void setSubsetName(String subsetName) {
		this.subsetName = subsetName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getShipToId() {
		return shipToId;
	}
	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}
	
	public int getBillAddressId() {
		return billAddressId;
	}
	public void setBillAddressId(int billAddressId) {
		this.billAddressId = billAddressId;
	}
	public int getShipAddressId() {
		return shipAddressId;
	}
	public void setShipAddressId(int shipAddressId) {
		this.shipAddressId = shipAddressId;
	}
	@Override
	public String toString() {
		return "User [accountNumber=" + accountNumber + ", customerId=" + customerId + ", userId=" + userId
				+ ", externalUserId=" + externalUserId + ", defaultWareHouseCode=" + defaultWareHouseCode
				+ ", firstName=" + firstName + ", address=" + address + ", shipToId=" + shipToId + ", phoneNumber="
				+ phoneNumber + ", accessPermission=" + accessPermission + "]";
	}
	public AccessPermission getAccessPermission() {
		return accessPermission;
	}
	public void setAccessPermission(AccessPermission accessPermission) {
		this.accessPermission = accessPermission;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public Terms getTerms() {
		return terms;
	}
	public void setTerms(Terms terms) {
		this.terms = terms;
	}
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getExternalUserId() {
		return externalUserId;
	}
	public void setExternalUserId(String externalUserId) {
		this.externalUserId = externalUserId;
	}
	public boolean isGeneralSubsetAllowed() {
		return generalSubsetAllowed;
	}
	public void setGeneralSubsetAllowed(boolean generalSubsetAllowed) {
		this.generalSubsetAllowed = generalSubsetAllowed;
	}
	public String getDefaultWareHouseCode() {
		return defaultWareHouseCode;
	}
	public void setDefaultWareHouseCode(String defaultWareHouseCode) {
		this.defaultWareHouseCode = defaultWareHouseCode;
	}
	public int getParentUserId() {
		return parentUserId;
	}
	public void setParentUserId(int parentUserId) {
		this.parentUserId = parentUserId;
	}
		
}
