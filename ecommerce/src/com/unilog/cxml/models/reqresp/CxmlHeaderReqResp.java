/**
 *
 */
package com.unilog.cxml.models.reqresp;

import java.io.Serializable;


/**
 * @author satish
 *
 */
public class CxmlHeaderReqResp implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4315890869722434286L;
	private CredentialDetails fromCredentialsDetails;
	private CredentialDetails toCredentialsDetails;
	private CredentialDetails senderCredentialsDetails;
	private String senderUserAgent;
	private NodeDetails nodeDetails;
	private String originalDocumentPayloadId;


	/**
	 * @return the fromCredentialsDetails
	 */
	public CredentialDetails getFromCredentialsDetails() {
		return fromCredentialsDetails;
	}
	/**
	 * @param fromCredentialsDetails the fromCredentialsDetails to set
	 */
	public void setFromCredentialsDetails(CredentialDetails fromCredentialsDetails) {
		this.fromCredentialsDetails = fromCredentialsDetails;
	}
	/**
	 * @return the toCredentialsDetails
	 */
	public CredentialDetails getToCredentialsDetails() {
		return toCredentialsDetails;
	}
	/**
	 * @param toCredentialsDetails the toCredentialsDetails to set
	 */
	public void setToCredentialsDetails(CredentialDetails toCredentialsDetails) {
		this.toCredentialsDetails = toCredentialsDetails;
	}
	/**
	 * @return the senderCredentialsDetails
	 */
	public CredentialDetails getSenderCredentialsDetails() {
		return senderCredentialsDetails;
	}
	/**
	 * @param senderCredentialsDetails the senderCredentialsDetails to set
	 */
	public void setSenderCredentialsDetails(
			CredentialDetails senderCredentialsDetails) {
		this.senderCredentialsDetails = senderCredentialsDetails;
	}
	/**
	 * @return the senderUserAgent
	 */
	public String getSenderUserAgent() {
		return senderUserAgent;
	}
	/**
	 * @param senderUserAgent the senderUserAgent to set
	 */
	public void setSenderUserAgent(String senderUserAgent) {
		this.senderUserAgent = senderUserAgent;
	}
	/**
	 * @return the nodeDetails
	 */
	public NodeDetails getNodeDetails() {
		return nodeDetails;
	}
	/**
	 * @param nodeDetails the nodeDetails to set
	 */
	public void setNodeDetails(NodeDetails nodeDetails) {
		this.nodeDetails = nodeDetails;
	}
	/**
	 * @return the originalDocumentPayloadId
	 */
	public String getOriginalDocumentPayloadId() {
		return originalDocumentPayloadId;
	}
	/**
	 * @param originalDocumentPayloadId the originalDocumentPayloadId to set
	 */
	public void setOriginalDocumentPayloadId(String originalDocumentPayloadId) {
		this.originalDocumentPayloadId = originalDocumentPayloadId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CxmlHeaderReqResp [fromCredentialsDetails=");
		builder.append(fromCredentialsDetails.toString());
		builder.append(", toCredentialsDetails=");
		builder.append(toCredentialsDetails.toString());
		builder.append(", senderCredentialsDetails=");
		builder.append(senderCredentialsDetails.toString());
		builder.append(", senderUserAgent=");
		builder.append(senderUserAgent);
		builder.append(", nodeDetails=");
		builder.append(nodeDetails);
		builder.append(", originalDocumentPayloadId=");
		builder.append(originalDocumentPayloadId);
		builder.append("]");
		return builder.toString();
	}




}
