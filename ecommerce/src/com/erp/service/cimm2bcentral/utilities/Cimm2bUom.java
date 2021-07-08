package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2bUom  extends Cimm2BCentralResponseEntity {
	private int totalPages;
	private int pageNumber;
	private int pageSize;
	
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public ArrayList<Cimm2BCentralUOM> getDetails() {
		return details;
	}
	public void setDetails(ArrayList<Cimm2BCentralUOM> details) {
		this.details = details;
	}
	private ArrayList<Cimm2BCentralUOM> details;

}
