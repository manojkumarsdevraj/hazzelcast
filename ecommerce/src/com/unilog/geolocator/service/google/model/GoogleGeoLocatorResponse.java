package com.unilog.geolocator.service.google.model;

import java.util.ArrayList;

public class GoogleGeoLocatorResponse {

	private ArrayList<GoogleGeoLocatorData> results;
	private String status;

	public ArrayList<GoogleGeoLocatorData> getResults() {
		return results;
	}

	public String getStatus() {
		return status;
	}

	public void setResults(ArrayList<GoogleGeoLocatorData> results) {
		this.results = results;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
