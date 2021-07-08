package com.unilog.ecommerce.customer.external;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralContact;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.unilog.database.CommonDBQuery;
import com.unilog.ecommerce.customer.model.Customer;
import com.unilog.ecommerce.customer.model.SalesRep;
import com.unilog.ecommerce.customer.model.Terms;
import com.unilog.ecommerce.user.model.Address;
import com.unilog.ecommerce.user.model.User;

public class CustomerMappingUtil {
	public static Customer extractGetCustomerDetails(Cimm2BCentralCustomer rawDetails) {
		Customer customer = new Customer();
		customer.setAccountNumber(rawDetails.getCustomerERPId());
		if(rawDetails.getCustomerName() != null) {
			customer.setAccountName(rawDetails.getCustomerName());
		}
		
		Address billAddress = new Address();
		billAddress.setAddress1(rawDetails.getAddress().getAddressLine1());
		billAddress.setAddress2(rawDetails.getAddress().getAddressLine2());
		billAddress.setCity(rawDetails.getAddress().getCity());
		billAddress.setState(rawDetails.getAddress().getState());
		billAddress.setCountry(rawDetails.getAddress().getCountry());
		billAddress.setZipCode(rawDetails.getAddress().getZipCode());
		billAddress.setType(rawDetails.getAddress().getAddressType());
		billAddress.setAccountNumber(customer.getAccountNumber());
		if(billAddress.getAddress1() == null) {
			if(rawDetails.getCustomerLocations() != null && rawDetails.getCustomerLocations().size() > 0) {
				billAddress.setAddress1(rawDetails.getCustomerLocations().get(0).getAddress().getAddressLine1());
				billAddress.setAddress2(rawDetails.getCustomerLocations().get(0).getAddress().getAddressLine2());
				billAddress.setCity(rawDetails.getCustomerLocations().get(0).getAddress().getCity());
				billAddress.setState(rawDetails.getCustomerLocations().get(0).getAddress().getState());
				billAddress.setCountry(rawDetails.getCustomerLocations().get(0).getAddress().getCountry());
				billAddress.setZipCode(rawDetails.getCustomerLocations().get(0).getAddress().getZipCode());
			}
		}
		
		customer.setBillAddress(billAddress);
		
		List<Address> shipAddresses = new ArrayList<>();
		for(Cimm2BCentralCustomer shipAddress : rawDetails.getCustomerLocations()) {
			Address address = new Address();
			address.setShipToId(shipAddress.getAddress().getAddressERPId());
			address.setAddress1(shipAddress.getAddress().getAddressLine1());
			address.setAddress2(shipAddress.getAddress().getAddressLine2());
			address.setCity(shipAddress.getAddress().getCity());
			address.setState(shipAddress.getAddress().getState());
			address.setCountry(shipAddress.getAddress().getCountry());
			address.setZipCode(shipAddress.getAddress().getZipCode());
			address.setType(shipAddress.getAddress().getAddressType());
			address.setAccountNumber(customer.getAccountNumber());
			shipAddresses.add(address);
		}
		customer.setShipAddresses(shipAddresses);
		customer.setTerms(new Terms(rawDetails.getTermsType(), ""));
		if(rawDetails.getHomeBranch() != null && rawDetails.getHomeBranch().length() > 0) {
			customer.setWarehouseCode(rawDetails.getHomeBranch());
		}else {
			customer.setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
		}
		
		customer.setSalesRep(new SalesRep(rawDetails.getSalesPersonCode(), ""));
		customer.setOrderTypes(rawDetails.getOrderStatus());
		
		if(rawDetails.getOrderStatus() != null && rawDetails.getOrderStatus().size() > 0) {
			customer.setOrderTypes(rawDetails.getOrderStatus());
		}
		
		return customer;
	}
	
	public static Cimm2BCentralCustomer mapCustomerToRequestBody(Customer customer) {
		Cimm2BCentralCustomer rawCustomer = new Cimm2BCentralCustomer();
		User user = customer.getUsers().get(0);
		rawCustomer.setCustomerName(user.getAccountName());
		
		Cimm2BCentralAddress address = new Cimm2BCentralAddress();
		address.setAddressLine1(user.getAddress().getAddress1());
		address.setAddressLine2(user.getAddress().getAddress2());
		address.setCity(user.getAddress().getCity());
		address.setState(user.getAddress().getState());
		address.setCountry(user.getAddress().getCountry());
		address.setZipCode(user.getAddress().getZipCode());
		
		rawCustomer.setAddress(address);
		Cimm2BCentralContact contact = new Cimm2BCentralContact();
		contact.setFirstName(user.getFirstName());
		contact.setLastName(user.getLastName());
		contact.setPrimaryPhoneNumber(user.getAddress().getPhoneNumber());
		contact.setFaxNumber(user.getAddress().getFaxNumber());
		contact.setPrimaryEmailAddress(user.getAddress().getEmailId());
		
		rawCustomer.setContacts(new ArrayList<Cimm2BCentralContact>());
		rawCustomer.getContacts().add(contact);
		
		rawCustomer.setPassword(user.getPassword());
		return rawCustomer;
	}
	
	public static Customer extractCreateCustomerDetails(Customer customer, Cimm2BCentralContact rawDetails) {
		User user = customer.getUsers().get(0);
		user.setExternalUserId(rawDetails.getUserERPId());
		user.setAccountNumber(rawDetails.getCustomerERPId());
		customer.setAccountNumber(rawDetails.getCustomerERPId());
		customer.setAccountName(user.getAccountName());
		customer.setBillAddress(user.getAddress());
		customer.setAccessPermission(user.getAccessPermission());
		customer.setShipAddresses(Arrays.asList(user.getAddress()));
		customer.setTerms(new Terms());
		customer.setSalesRep(new SalesRep());
		
		//customer.getUsers().add(0, user);
		return customer;
	}
}
