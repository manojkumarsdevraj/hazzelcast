package com.unilog.avalara.model;

import java.util.List;

public class AvalaraAddressResponse {

	private AvalaraAddress address;
	private List<AvalaraValidatedAddress> validatedAddresses;
	private AvalaraCoordinates coordinates;
	private String resolutionQuality;
	private List<AvalaraMessages> messages;
	private List<TaxAuthority> taxAuthorities;

	public AvalaraAddress getAddress() {
		return address;
	}

	public void setAddress(AvalaraAddress address) {
		this.address = address;
	}

	public List<AvalaraValidatedAddress> getValidatedAddresses() {
		return validatedAddresses;
	}

	public void setValidatedAddresses(List<AvalaraValidatedAddress> validatedAddresses) {
		this.validatedAddresses = validatedAddresses;
	}

	public AvalaraCoordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(AvalaraCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	public String getResolutionQuality() {
		return resolutionQuality;
	}

	public void setResolutionQuality(String resolutionQuality) {
		this.resolutionQuality = resolutionQuality;
	}

	public List<AvalaraMessages> getMessages() {
		return messages;
	}

	public void setMessages(List<AvalaraMessages> messages) {
		this.messages = messages;
	}

	public List<TaxAuthority> getTaxAuthorities() {
		return taxAuthorities;
	}

	public void setTaxAuthorities(List<TaxAuthority> taxAuthorities) {
		this.taxAuthorities = taxAuthorities;
	}

}
