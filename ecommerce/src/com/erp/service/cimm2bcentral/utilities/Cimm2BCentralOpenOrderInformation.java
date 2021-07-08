package com.erp.service.cimm2bcentral.utilities;

import java.util.List;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOpenOrderInformationDetails;

public class Cimm2BCentralOpenOrderInformation extends Cimm2BCentralResponseEntity{
	private List<Cimm2BCentralOpenOrderInformationDetails> details;
	private int pageNumber;
	private int pageSize;
	private int totalPages;
	public List<Cimm2BCentralOpenOrderInformationDetails> getDetails() {
		return details;
	}
	public void setDetails(List<Cimm2BCentralOpenOrderInformationDetails> details) {
		this.details = details;
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
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
}
