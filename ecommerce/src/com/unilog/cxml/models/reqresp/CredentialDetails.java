/**
 *
 */
package com.unilog.cxml.models.reqresp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author satish
 *
 */
public class CredentialDetails implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6897647767960154933L;
	private String credentialDomain;
	private String credentialIdentity;
	private String credentialSecret;

	private ArrayList<CredentialDetails> fromDetailsList;

	/**
	 *
	 */
	public CredentialDetails() {
		
	}


	/**
	 * @param credentialDomain
	 * @param credentialIdentity
	 * @param credentialSecret
	 * @param fromDetailsList
	 */
	public CredentialDetails(String credentialDomain,String credentialIdentity, String credentialSecret) {
		this.credentialDomain = credentialDomain;
		this.credentialIdentity = credentialIdentity;
		this.credentialSecret = credentialSecret;

	}


	/**
	 * @return the credentialDomain
	 */
	public String getCredentialDomain() {
		return credentialDomain;
	}


	/**
	 * @param credentialDomain the credentialDomain to set
	 */
	public void setCredentialDomain(String credentialDomain) {
		this.credentialDomain = credentialDomain;
	}


	/**
	 * @return the credentialIdentity
	 */
	public String getCredentialIdentity() {
		return credentialIdentity;
	}


	/**
	 * @param credentialIdentity the credentialIdentity to set
	 */
	public void setCredentialIdentity(String credentialIdentity) {
		this.credentialIdentity = credentialIdentity;
	}


	/**
	 * @return the credentialSecret
	 */
	public String getCredentialSecret() {
		return credentialSecret;
	}


	/**
	 * @param credentialSecret the credentialSecret to set
	 */
	public void setCredentialSecret(String credentialSecret) {
		this.credentialSecret = credentialSecret;
	}


	/**
	 * @return the fromDetailsList
	 */
	public ArrayList<CredentialDetails> getFromDetailsList() {
		return fromDetailsList;
	}


	/**
	 * @param fromDetailsList the fromDetailsList to set
	 */
	public void setFromDetailsList(ArrayList<CredentialDetails> fromDetailsList) {
		this.fromDetailsList = fromDetailsList;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CredentialDetails [credentialDomain=");
		builder.append(credentialDomain);
		builder.append(", credentialIdentity=");
		builder.append(credentialIdentity);
		builder.append(", credentialSecret=");
		builder.append(credentialSecret);
		builder.append(", fromDetailsList=");
		builder.append(fromDetailsList);
		builder.append("]");
		return builder.toString();
	}



}
