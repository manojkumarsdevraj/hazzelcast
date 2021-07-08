package com.unilog.express.transaction.model;

import javax.xml.bind.annotation.XmlElement;

public class Transaction {
	private String transactionSetupID;

	@XmlElement(name = "TransactionSetupID")
	public void setTransactionSetupID(String transactionSetupID) {
		this.transactionSetupID = transactionSetupID;
	}

	public String getTransactionSetupID() {
		return transactionSetupID;
	}

	
}
