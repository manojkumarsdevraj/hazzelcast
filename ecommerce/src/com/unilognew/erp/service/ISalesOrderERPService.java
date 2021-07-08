package com.unilognew.erp.service;

import java.util.List;

import com.unilognew.model.BuyingCompanyAddressBook;
import com.unilognew.model.ERPServiceRequest;

public interface ISalesOrderERPService {
	
	public List<BuyingCompanyAddressBook> getCustomerShipToAddressList(List<ERPServiceRequest> erpAddressRequestList);

}
