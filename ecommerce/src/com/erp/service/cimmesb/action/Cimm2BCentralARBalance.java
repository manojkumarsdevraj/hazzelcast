package com.erp.service.cimmesb.action;

import java.util.List;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;


public class Cimm2BCentralARBalance extends
Cimm2BCentralResponseEntity{
	private List<Cimm2BCentralARBalanceDetails> details;
	private int pageNumber;
	private int pageSize;
	private int totalPages;

	public List<Cimm2BCentralARBalanceDetails> getDetails() {
		return details;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setDetails(List<Cimm2BCentralARBalanceDetails> details) {
		this.details = details;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

}
