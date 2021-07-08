package com.unilog.datasmart.model;

import java.util.LinkedList;

public class DataSmartHotSpotModel extends ResponseStatusDataSmart {
	private LinkedList <HotSpot> data;

	public LinkedList <HotSpot> getData() {
		return data;
	}

	public void setData(LinkedList <HotSpot> data) {
		this.data = data;
	}

}
