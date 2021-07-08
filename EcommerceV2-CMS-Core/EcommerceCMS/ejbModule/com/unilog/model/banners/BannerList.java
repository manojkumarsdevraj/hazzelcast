package com.unilog.model.banners;

import java.util.List;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;


public class BannerList extends Cimm2BCentralResponseEntity{
	private int totalPages;
	private int totalRows;
	private int rowsOnPage;
	private int pageSize;
	private int pageNo;
	private List<BannerListData> resultSet;
	
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	public int getRowsOnPage() {
		return rowsOnPage;
	}
	public void setRowsOnPage(int rowsOnPage) {
		this.rowsOnPage = rowsOnPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public List<BannerListData> getResultSet() {
		return resultSet;
	}
	public void setResultSet(List<BannerListData> resultSet) {
		this.resultSet = resultSet;
	}
	
	
}
