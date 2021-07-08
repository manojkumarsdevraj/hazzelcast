package com.erp.service.cimm2bcentral.utilities;

import java.util.List;

public class Cimm2BCentralSalesOrderHistory extends Cimm2BCentralResponseEntity{

	private List<Cimm2BCentralSalesOrderHistoryDetails> details;
	private int pageNumber;
	private int pageSize;
	private int totalPages;
	private Cimm2BCentralSalesOrderHistoryAdditionalData additionalData;

	public List<Cimm2BCentralSalesOrderHistoryDetails> getDetails() {
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

	public void setDetails(List<Cimm2BCentralSalesOrderHistoryDetails> details) {
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

	public Cimm2BCentralSalesOrderHistoryAdditionalData getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(Cimm2BCentralSalesOrderHistoryAdditionalData additionalData) {
		this.additionalData = additionalData;
	}

}
