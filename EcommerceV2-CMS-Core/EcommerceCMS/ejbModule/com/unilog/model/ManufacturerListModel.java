package com.unilog.model;

import java.util.List;

public class ManufacturerListModel {
	private int totalPages;
	private int totalRows;
	private int rowsOnPage;
	private int pageSize;
	private int pageNo;
	private List<ManufacturerDataModel> resultSet;
	
	
	
	
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
	public List<ManufacturerDataModel> getResultSet() {
		return resultSet;
	}
	public void setResultSet(List<ManufacturerDataModel> resultSet) {
		this.resultSet = resultSet;
	}
	
	
}
