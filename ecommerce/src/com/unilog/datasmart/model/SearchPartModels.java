package com.unilog.datasmart.model;

import java.util.LinkedList;

public class SearchPartModels {
	
	
	private LinkedList<Results> Results;
	private int Page;
	private int PageSize;
	private int TotalNumberOfResults;
	private String PartSearchText;
	private String ModelSearchText;
	
	public LinkedList<Results> getResults() {
		return Results;
	}
	public void setResults(LinkedList<Results> results) {
		Results = results;
	}
	public int getPage() {
		return Page;
	}
	public void setPage(int page) {
		Page = page;
	}
	public int getPageSize() {
		return PageSize;
	}
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}
	public int getTotalNumberOfResults() {
		return TotalNumberOfResults;
	}
	public void setTotalNumberOfResults(int totalNumberOfResults) {
		TotalNumberOfResults = totalNumberOfResults;
	}
	public String getPartSearchText() {
		return PartSearchText;
	}
	public void setPartSearchText(String partSearchText) {
		PartSearchText = partSearchText;
	}
	public String getModelSearchText() {
		return ModelSearchText;
	}
	public void setModelSearchText(String modelSearchText) {
		ModelSearchText = modelSearchText;
	}
	
}
