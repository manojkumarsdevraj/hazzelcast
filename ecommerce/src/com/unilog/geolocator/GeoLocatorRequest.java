package com.unilog.geolocator;

public class GeoLocatorRequest {

	public GeoLocatorRequest(String searchString) {
		this.searchString = searchString;
	}

	private String searchString;

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}
