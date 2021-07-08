package com.unilog.searchconfig;

public class SearchConfigModel {
	private int id;
	private String fieldName;
	private String fieldType;
	private String searchable;
	private String filterable;
	private double queryBoost;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getSearchable() {
		return searchable;
	}
	public void setSearchable(String searchable) {
		this.searchable = searchable;
	}
	public String getFilterable() {
		return filterable;
	}
	public void setFilterable(String filterable) {
		this.filterable = filterable;
	}
	public double getQueryBoost() {
		return queryBoost;
	}
	public void setQueryBoost(double queryBoost) {
		this.queryBoost = queryBoost;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	

}
