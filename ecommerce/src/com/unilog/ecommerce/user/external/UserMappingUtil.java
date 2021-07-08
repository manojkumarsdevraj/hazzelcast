package com.unilog.ecommerce.user.external;

import java.util.ArrayList;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralContact;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralUser;
import com.unilog.ecommerce.user.model.User;

public class UserMappingUtil {
	public static Cimm2BCentralUser mapUserToRequestBody(User user){
		Cimm2BCentralUser rawUser = new Cimm2BCentralUser();
		rawUser.setCustomerERPId(user.getAccountNumber());
		rawUser.setPassword(user.getPassword());
		
		Cimm2BCentralAddress address = new Cimm2BCentralAddress();
		address.setAddressLine1(user.getAddress().getAddress1());
		address.setAddressLine2(user.getAddress().getAddress2());
		address.setCity(user.getAddress().getCity());
		address.setState(user.getAddress().getState());
		address.setCountry(user.getAddress().getCountry());
		address.setZipCode(user.getAddress().getZipCode());
		
		rawUser.setAddress(address);
		
		Cimm2BCentralContact contactDetails = new Cimm2BCentralContact();
		contactDetails.setSalutation(user.getSalutation());
		contactDetails.setFirstName(user.getFirstName());
		contactDetails.setLastName(user.getLastName());
		contactDetails.setPrimaryEmailAddress(user.getAddress().getEmailId());
		contactDetails.setPrimaryPhoneNumber(user.getAddress().getPhoneNumber());
		contactDetails.setFaxNumber(user.getAddress().getFaxNumber());
		
		rawUser.setContacts(new ArrayList<Cimm2BCentralContact>());
		rawUser.getContacts().add(contactDetails);
		
		return rawUser;
	}
}
