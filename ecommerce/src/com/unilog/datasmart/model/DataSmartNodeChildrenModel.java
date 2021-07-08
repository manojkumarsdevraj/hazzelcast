package com.unilog.datasmart.model;


import java.util.LinkedList;

public class DataSmartNodeChildrenModel extends ResponseStatusDataSmart{
	private LinkedList <NodeChildren> data;
	public LinkedList <NodeChildren> getData() {
		return data;
	}
	public void setData(LinkedList <NodeChildren> data) {
		this.data = data;
	}
}
