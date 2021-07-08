package com.unilog.ecommerce.customer.service;

import java.sql.Connection;

import com.unilog.ecommerce.customer.model.Customer;
import com.unilog.ecommerce.user.model.Address;

public interface CustomerService {
	int addCustomer(Connection connection, Customer customer);
	int doesCustomerExists(String accountNumber);
	int addBillAddress(Connection connection, int customerId, Address address);
	int addShipAddress(Connection connection, int customerId, Address address);
	
}
