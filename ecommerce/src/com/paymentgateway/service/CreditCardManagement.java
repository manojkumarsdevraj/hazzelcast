package com.paymentgateway.service;

import com.paymentgateway.model.CreditCardManagementModel;
import com.unilog.sales.CreditCardModel;

public interface CreditCardManagement {
	
	public CreditCardModel creditCardTransactionSetup(CreditCardManagementModel creditCartInput);

}
