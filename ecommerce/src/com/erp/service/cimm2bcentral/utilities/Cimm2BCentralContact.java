package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralContact extends Cimm2BCentralResponseEntity{
	
	private String firstName;
	private String middleName;
	private String lastName;
	private String primaryPhoneNumber;
	private String primaryPhoneExtension;
	private String alternatePhoneNumber;
	private String alternatePhoneExtension;
	private String faxNumber;
	private String primaryEmailAddress;
	private String alternateEmailAddress;
	private String salutation;
	private String title;
	private String contactId;
	private String companyName;
	private String customerERPId;
	private String userERPId;
	private String userRoleType;
	private ArrayList<Cimm2BCentralCustomer> customerLocations=new 	ArrayList<Cimm2BCentralCustomer>();

	
	public ArrayList<Cimm2BCentralCustomer> getCustomerLocations() {
		return customerLocations;
	}
	public void setCustomerLocations(
			ArrayList<Cimm2BCentralCustomer> customerLocations) {
		this.customerLocations = customerLocations;
	}
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPrimaryPhoneNumber() {
		return primaryPhoneNumber;
	}
	public void setPrimaryPhoneNumber(String primaryPhoneNumber) {
		this.primaryPhoneNumber = primaryPhoneNumber;
	}
	public String getPrimaryPhoneExtension() {
		return primaryPhoneExtension;
	}
	public void setPrimaryPhoneExtension(String primaryPhoneExtension) {
		this.primaryPhoneExtension = primaryPhoneExtension;
	}
	public String getAlternatePhoneNumber() {
		return alternatePhoneNumber;
	}
	public void setAlternatePhoneNumber(String alternatePhoneNumber) {
		this.alternatePhoneNumber = alternatePhoneNumber;
	}
	public String getAlternatePhoneExtension() {
		return alternatePhoneExtension;
	}
	public void setAlternatePhoneExtension(String alternatePhoneExtension) {
		this.alternatePhoneExtension = alternatePhoneExtension;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getPrimaryEmailAddress() {
		return primaryEmailAddress;
	}
	public void setPrimaryEmailAddress(String primaryEmailAddress) {
		this.primaryEmailAddress = primaryEmailAddress;
	}
	public String getAlternateEmailAddress() {
		return alternateEmailAddress;
	}
	public void setAlternateEmailAddress(String alternateEmailAddress) {
		this.alternateEmailAddress = alternateEmailAddress;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getUserERPId() {
		return userERPId;
	}
	public void setUserERPId(String userERPId) {
		this.userERPId = userERPId;
	}
	public String getUserRoleType() {
		return userRoleType;
	}
	public void setUserRoleType(String userRoleType) {
		this.userRoleType = userRoleType;
	}

}