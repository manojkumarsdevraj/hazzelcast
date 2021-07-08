package com.unilog.datasmart.model;

import java.util.LinkedList;

public class DataSmartSearchModelAssembliesModel  extends ResponseStatusDataSmart{
	private LinkedList <SearchModelAssemblyInfo> data;

	public LinkedList <SearchModelAssemblyInfo> getData() {
		return data;
	}
	public void setData(LinkedList <SearchModelAssemblyInfo> data) {
		this.data = data;
	}

}
