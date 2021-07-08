package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralCustomerData extends Cimm2BCentralResponseEntity {
	private String totalPages;
	private String pageNumber;
	private String pageSize;
	private ArrayList<Cimm2BCentralCustomer> details;
	public String getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public ArrayList<Cimm2BCentralCustomer> getDetails() {
		return details;
	}
	public void setDetails(ArrayList<Cimm2BCentralCustomer> details) {
		this.details = details;
	}
	
}
