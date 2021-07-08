package com.unilog.geolocator.service.google.model;

public class GoogleGeometry {

	private LocationCoordinates location;
	private String location_type;

	public LocationCoordinates getLocation() {
		return location;
	}

	public String getLocation_type() {
		return location_type;
	}

	public void setLocation(LocationCoordinates location) {
		this.location = location;
	}

	public void setLocation_type(String location_type) {
		this.location_type = location_type;
	}
}
