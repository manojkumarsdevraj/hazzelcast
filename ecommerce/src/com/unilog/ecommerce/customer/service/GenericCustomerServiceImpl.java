package com.unilog.ecommerce.customer.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.unilog.database.CommonDBQuery;
import com.unilog.ecommerce.customer.dao.CustomerRepository;
import com.unilog.ecommerce.customer.dao.CustomerRepositoryFactory;
import com.unilog.ecommerce.customer.model.Customer;
import com.unilog.ecommerce.user.model.Address;

public class GenericCustomerServiceImpl implements CustomerService {
	private static final Logger logger = Logger.getLogger(GenericCustomerServiceImpl.class);
	private static CustomerService customerService;
	public static CustomerService getInstance() {
			synchronized (GenericCustomerServiceImpl.class) {
				if(customerService == null) {
					customerService = new GenericCustomerServiceImpl();
				}
			}
		return customerService;
	}
	@Override
	public int addCustomer(Connection connection, Customer customer) {
		int customerId = 0;
		String subsetId = CommonDBQuery.getSystemParamtersList().get("USERSUBSETID");
		customer.setSubsetId(Integer.parseInt(subsetId));
		CustomerRepository customerRepo = CustomerRepositoryFactory.getCustomerRepository("GENERIC");
		try {
			customerId = customerRepo.addCustomer(connection, customer);
		} catch (SQLException e) {
			customerId = 0;
			logger.error("SQL Exception while adding customer", e);
		}
		return customerId;
	}

	@Override
	public int doesCustomerExists(String accountNumber) {
		String repoRequestType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_REG_REPO_TYPE");
		CustomerRepository customerRepo =  CustomerRepositoryFactory.getCustomerRepository(repoRequestType);
		return customerRepo.doesCustomerExists(accountNumber);
	}

	@Override
	public int addBillAddress(Connection connection, int customerId, Address address) {
		String repoRequestType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_REG_REPO_TYPE");
		CustomerRepository customerRepo =  CustomerRepositoryFactory.getCustomerRepository(repoRequestType);
		return customerRepo.addBillAddress(connection, customerId, address);
	}

	@Override
	public int addShipAddress(Connection connection, int customerId, Address address) {
		String repoRequestType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_REG_REPO_TYPE");
		CustomerRepository customerRepo =  CustomerRepositoryFactory.getCustomerRepository(repoRequestType);
		return customerRepo.addShipAddress(connection, customerId, address);
	}

}
