package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralNearestWareHouseResponse {
	private int totalPages;
	private int pageNumber;
	private int pageSize;
	private ArrayList<Cimm2BCentralNearestWareHouse> details;
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
	public ArrayList<Cimm2BCentralNearestWareHouse> getDetails() {
		return details;
	}
	public void setDetails(ArrayList<Cimm2BCentralNearestWareHouse> details) {
		this.details = details;
	}
	
}
