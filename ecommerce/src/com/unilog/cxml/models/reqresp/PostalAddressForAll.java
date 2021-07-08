/**
 *
 */
package com.unilog.cxml.models.reqresp;

import java.io.Serializable;

/**
 * @author satish
 *
 */
public class PostalAddressForAll implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -2207336899728074316L;
	private String city;
	private String isoCountryCode;
	private String countryValue;
	private String deliverToDetails;
	private String streetAddress;
	private String name;
	private String postalCode;
	private String state;
	private String urlAddress;

	/**
	 *
	 */
	public PostalAddressForAll() {
		
	}

	/**
	 * @param city
	 * @param isoCountryCode
	 * @param countryValue
	 * @param deliverToDetails
	 * @param streetAddress
	 * @param name
	 * @param postalCode
	 * @param state
	 * @param urlAddress
	 */
	public PostalAddressForAll(String city, String isoCountryCode,
			String countryValue, String deliverToDetails, String streetAddress,
			String name, String postalCode, String state, String urlAddress) {
		this.city = city;
		this.isoCountryCode = isoCountryCode;
		this.countryValue = countryValue;
		this.deliverToDetails = deliverToDetails;
		this.streetAddress = streetAddress;
		this.name = name;
		this.postalCode = postalCode;
		this.state = state;
		this.urlAddress = urlAddress;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the isoCountryCode
	 */
	public String getIsoCountryCode() {
		return isoCountryCode;
	}

	/**
	 * @return the countryValue
	 */
	public String getCountryValue() {
		return countryValue;
	}

	/**
	 * @return the deliverToDetails
	 */
	public String getDeliverToDetails() {
		return deliverToDetails;
	}

	/**
	 * @return the streetAddress
	 */
	public String getStreetAddress() {
		return streetAddress;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return the urlAddress
	 */
	public String getUrlAddress() {
		return urlAddress;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param isoCountryCode the isoCountryCode to set
	 */
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}

	/**
	 * @param countryValue the countryValue to set
	 */
	public void setCountryValue(String countryValue) {
		this.countryValue = countryValue;
	}

	/**
	 * @param deliverToDetails the deliverToDetails to set
	 */
	public void setDeliverToDetails(String deliverToDetails) {
		this.deliverToDetails = deliverToDetails;
	}

	/**
	 * @param streetAddress the streetAddress to set
	 */
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @param urlAddress the urlAddress to set
	 */
	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PostalAddressForAll [city=");
		builder.append(city);
		builder.append(", isoCountryCode=");
		builder.append(isoCountryCode);
		builder.append(", countryValue=");
		builder.append(countryValue);
		builder.append(", deliverToDetails=");
		builder.append(deliverToDetails);
		builder.append(", streetAddress=");
		builder.append(streetAddress);
		builder.append(", name=");
		builder.append(name);
		builder.append(", postalCode=");
		builder.append(postalCode);
		builder.append(", state=");
		builder.append(state);
		builder.append(", urlAddress=");
		builder.append(urlAddress);
		builder.append("]");
		return builder.toString();
	}



}
