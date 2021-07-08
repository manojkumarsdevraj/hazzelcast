package com.unilog.geolocator.service.google.model;

import java.util.ArrayList;

public class GoogleGeoLocatorData {

	private ArrayList<GoogleAddressComponent> address_components;
	private String formatted_address;
	private GoogleGeometry geometry;
	private ArrayList<String> type;

	public ArrayList<GoogleAddressComponent> getAddress_components() {
		return address_components;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public GoogleGeometry getGeometry() {
		return geometry;
	}

	public ArrayList<String> getType() {
		return type;
	}

	public void setAddress_components(ArrayList<GoogleAddressComponent> address_components) {
		this.address_components = address_components;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public void setGeometry(GoogleGeometry geometry) {
		this.geometry = geometry;
	}

	public void setType(ArrayList<String> type) {
		this.type = type;
	}

}
