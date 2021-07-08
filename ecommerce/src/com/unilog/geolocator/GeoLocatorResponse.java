package com.unilog.geolocator;

public class GeoLocatorResponse {

	private String city;
	private String country;
	private String county;
	private String latitude;
	private String longitude;
	private String state;
	private String zipCode;

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getCounty() {
		return county;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getState() {
		return state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}
