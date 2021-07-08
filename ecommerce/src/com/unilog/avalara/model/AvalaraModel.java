package com.unilog.avalara.model;

import java.util.List;

public class AvalaraModel {

	private String CustomerCode;
	private String DocDate;
	private String DocCode;
	private List<Addresses> Addresses;
	private List<Lines> Lines;
	public String getCustomerCode() {
		return CustomerCode;
	}
	public void setCustomerCode(String customerCode) {
		CustomerCode = customerCode;
	}
	public String getDocDate() {
		return DocDate;
	}
	public void setDocDate(String docDate) {
		DocDate = docDate;
	}
	public String getDocCode() {
		return DocCode;
	}
	public void setDocCode(String docCode) {
		DocCode = docCode;
	}
	public List<Addresses> getAddresses() {
		return Addresses;
	}
	public void setAddresses(List<Addresses> addresses) {
		Addresses = addresses;
	}
	public List<Lines> getLines() {
		return Lines;
	}
	public void setLines(List<Lines> lines) {
		Lines = lines;
	}
	
}
