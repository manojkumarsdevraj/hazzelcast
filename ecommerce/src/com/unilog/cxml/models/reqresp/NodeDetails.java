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
public class NodeDetails implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7551576431446140180L;
	private String nodeType;
	private ArrayList<CredentialDetails> nodeDetailsList;

	/**
	 *
	 */
	public NodeDetails() {
		
	}

	/**
	 * @return the nodeType
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * @param nodeType the nodeType to set
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * @return the nodeDetailsList
	 */
	public ArrayList<CredentialDetails> getNodeDetailsList() {
		return nodeDetailsList;
	}

	/**
	 * @param nodeDetailsList the nodeDetailsList to set
	 */
	public void setNodeDetailsList(ArrayList<CredentialDetails> nodeDetailsList) {
		this.nodeDetailsList = nodeDetailsList;
	}



}
