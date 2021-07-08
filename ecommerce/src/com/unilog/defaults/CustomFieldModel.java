package com.unilog.defaults;

public class CustomFieldModel {


	private int customFieldID;
	private int locCustomFieldValueID;
	private int userCustomFieldValueID;
	private int id;
	private String fieldName;
	private String customFieldvalue;

	public CustomFieldModel(){

	}

	public CustomFieldModel(int id, String fieldName, String customFieldvalue){
		this.id = id;
		this.fieldName = fieldName;
		this.customFieldvalue = customFieldvalue;
	}

	public int getCustomFieldID() {
		return customFieldID;
	}

	public void setCustomFieldID(int customFieldID) {
		this.customFieldID = customFieldID;
	}

	public int getLocCustomFieldValueID() {
		return locCustomFieldValueID;
	}

	public void setLocCustomFieldValueID(int locCustomFieldValueID) {
		this.locCustomFieldValueID = locCustomFieldValueID;
	}

	public int getUserCustomFieldValueID() {
		return userCustomFieldValueID;
	}

	public void setUserCustomFieldValueID(int userCustomFieldValueID) {
		this.userCustomFieldValueID = userCustomFieldValueID;
	}

	public String getCustomFieldvalue() {
		return customFieldvalue;
	}

	public void setCustomFieldvalue(String customFieldvalue) {
		this.customFieldvalue = customFieldvalue;
	}

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
}
