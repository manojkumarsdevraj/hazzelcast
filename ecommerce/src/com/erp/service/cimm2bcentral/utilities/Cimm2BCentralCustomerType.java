package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralCustomerType {

	private String type;
	private ArrayList<String> accounts;
	private ArrayList<String> names;
	public ArrayList<String> getAccounts() {
		return accounts;
	}
	public void setAccounts(ArrayList<String> accounts) {
		this.accounts = accounts;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<String> getNames() {
		return names;
	}
	public void setNames(ArrayList<String> names) {
		this.names = names;
	}
}
