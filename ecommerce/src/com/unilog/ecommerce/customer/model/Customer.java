package com.unilog.ecommerce.customer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.unilog.ecommerce.user.model.AccessPermission;
import com.unilog.ecommerce.user.model.Address;
import com.unilog.ecommerce.user.model.User;

public class Customer {
	private String accountNumber;
	private String accountName;
	
	private int subsetId;
	private String subsetName;
	private String warehouseCode;
	
	private  Address billAddress;
	private List<Address> shipAddresses = new ArrayList<>();
	
	private List<User> users;
	
	private AccessPermission accessPermission;
	private Terms terms;
	private SalesRep salesRep;
	private List<String> orderTypes;
	
	private Map<String, Object> otherDetails;
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Address getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(Address billAddress) {
		this.billAddress = billAddress;
	}

	public List<Address> getShipAddresses() {
		return shipAddresses;
	}

	public void setShipAddresses(List<Address> shipAddresses) {
		this.shipAddresses = shipAddresses;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getSubsetName() {
		return subsetName;
	}

	public void setSubsetName(String subsetName) {
		this.subsetName = subsetName;
	}

	public int getSubsetId() {
		return subsetId;
	}

	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public AccessPermission getAccessPermission() {
		return accessPermission;
	}

	public void setAccessPermission(AccessPermission accessPermission) {
		this.accessPermission = accessPermission;
	}

	public Terms getTerms() {
		return terms;
	}

	public void setTerms(Terms terms) {
		this.terms = terms;
	}

	public SalesRep getSalesRep() {
		return salesRep;
	}

	public void setSalesRep(SalesRep salesRep) {
		this.salesRep = salesRep;
	}

	public List<String> getOrderTypes() {
		return orderTypes;
	}

	public void setOrderTypes(List<String> orderTypes) {
		this.orderTypes = orderTypes;
	}

	public Map<String, Object> getOtherDetails() {
		return otherDetails;
	}

	public void setOtherDetails(Map<String, Object> otherDetails) {
		this.otherDetails = otherDetails;
	}
}
