package com.unilog.datasmart.model;

import java.util.LinkedList;

public class DataSmartPartAutoCompleteModel  extends ResponseStatusDataSmart{
	
	private LinkedList <PartAutoComplete> data;
	
	public LinkedList <PartAutoComplete> getData() {
		return data;
	}
	public void setData(LinkedList <PartAutoComplete> data) {
		this.data = data;
	}


}
