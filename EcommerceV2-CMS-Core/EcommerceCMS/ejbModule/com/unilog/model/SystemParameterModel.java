package com.unilog.model;

public class SystemParameterModel {
	private String configKey;
	private Object configValue;
	private String category="GENERAL";
	private String fieldType;
	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
	public Object getConfigValue() {
		return configValue;
	}
	public void setConfigValue(Object configValue) {
		this.configValue = configValue;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	
}
