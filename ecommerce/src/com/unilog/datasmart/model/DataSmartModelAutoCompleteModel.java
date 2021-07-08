package com.unilog.datasmart.model;

import java.util.LinkedList;

public class DataSmartModelAutoCompleteModel extends ResponseStatusDataSmart{
	
	private LinkedList <ModelAutoComplete> data;
	public LinkedList <ModelAutoComplete> getData() {
		return data;
	}
	public void setData(LinkedList <ModelAutoComplete> data) {
		this.data = data;
	}

}
