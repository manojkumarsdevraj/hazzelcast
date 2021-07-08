package com.unilognew.erp.service;

import java.util.List;
import java.util.Map;

import com.unilognew.exception.ERPServiceException;
import com.unilognew.model.BuyingCompanyAddressBook;
import com.unilognew.model.ERPServiceRequest;
import com.unilognew.util.ECommerceEnumType.AddressType;

public interface IUserERPService {
	
	public Map<AddressType,List<BuyingCompanyAddressBook>> getBillToAndShipToAddressForSync(ERPServiceRequest billToAddressRequest,ERPServiceRequest shipToAddressRequest) throws ERPServiceException;
	public List<BuyingCompanyAddressBook> getCustomerShipToAddressList(ERPServiceRequest shipToAddressRequest) throws ERPServiceException;
	public BuyingCompanyAddressBook getCustomerBillToAddress(ERPServiceRequest billToAddressRequest)  throws ERPServiceException;
}
