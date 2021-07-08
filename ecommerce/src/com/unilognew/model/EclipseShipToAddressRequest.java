package com.unilognew.model;

public class EclipseShipToAddressRequest implements ERPServiceRequest {
	
	private boolean fullDetails=false;

	/**
	 * @return the fullDetails
	 */
	public boolean isFullDetails() {
		return fullDetails;
	}

	/**
	 * @param fullDetails the fullDetails to set
	 */
	public void setFullDetails(boolean fullDetails) {
		this.fullDetails = fullDetails;
	}
		
}
