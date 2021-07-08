package com.unilog.datasmart.model;

public class DataSmartsSearchModel  extends ResponseStatusDataSmart{
	
	private SearchPartModels data;
	public SearchPartModels getData() {
		return data;
	}
	public void setData(SearchPartModels data) {
		this.data = data;
	}
}
