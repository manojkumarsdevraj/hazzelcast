package com.unilog.express.transaction.model;

import javax.xml.bind.annotation.XmlElement;

public class Response {
	
	private String expressResponseCode;
	private String expressResponseMessage;
	private Transaction transaction;

	
	@XmlElement(name = "ExpressResponseCode")
	public void setExpressResponseCode(String expressResponseCode) {
		this.expressResponseCode = expressResponseCode;
	}

	public String getExpressResponseCode() {
		return expressResponseCode;
	}

	@XmlElement(name = "ExpressResponseMessage")
	public void setExpressResponseMessage(String expressResponseMessage) {
		this.expressResponseMessage = expressResponseMessage;
	}

	public String getExpressResponseMessage() {
		return expressResponseMessage;
	}

	@XmlElement(name = "Transaction")
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Transaction getTransaction() {
		return transaction;
	}


}
