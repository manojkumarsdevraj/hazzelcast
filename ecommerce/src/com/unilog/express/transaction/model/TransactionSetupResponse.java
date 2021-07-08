package com.unilog.express.transaction.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TransactionSetupResponse")
public class TransactionSetupResponse {
	
	private Response response;

	@XmlElement(name = "Response")
	public void setResponse(Response response) {
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}
	
	
}
