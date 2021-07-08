package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralUser extends Cimm2BCentralResponseEntity {
	private Cimm2BCentralAddress address;
	private ArrayList<Cimm2BCentralContact> contacts;
	private String userERPId;
	private String customerERPId;
	private String password;
	private String department;
	private String classificationId;
	private String contactTitle;
	private String contactWebsite;
	public String getUserERPId() {
		return userERPId;
	}
	public void setUserERPId(String userERPId) {
		this.userERPId = userERPId;
	}
	public String getClassificationId() {
		return classificationId;
	}
	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Cimm2BCentralAddress getAddress() {
		return address;
	}
	public void setAddress(Cimm2BCentralAddress address) {
		this.address = address;
	}
	public ArrayList<Cimm2BCentralContact> getContacts() {
		return contacts;
	}
	public void setContacts(ArrayList<Cimm2BCentralContact> contacts) {
		this.contacts = contacts;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getContactTitle() {
		return contactTitle;
	}
	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}
	public String getContactWebsite() {
		return contactWebsite;
	}
	public void setContactWebsite(String contactWebsite) {
		this.contactWebsite = contactWebsite;
	}

}
