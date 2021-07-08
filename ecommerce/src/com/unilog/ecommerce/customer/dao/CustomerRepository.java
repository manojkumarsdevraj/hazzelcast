package com.unilog.ecommerce.customer.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.unilog.ecommerce.customer.model.Customer;
import com.unilog.ecommerce.user.model.Address;

public interface CustomerRepository {
	public int addCustomer(Connection connection, Customer customer)throws SQLException;
	public int doesCustomerExists(String accountNumber);
	public int addBillAddress(Connection connection, int customerId, Address address);
	public int addShipAddress(Connection connection, int customerId,Address address);
}
