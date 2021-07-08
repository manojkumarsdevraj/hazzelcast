package com.unilog.ecommerce.user.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.ecommerce.customer.model.Customer;
import com.unilog.ecommerce.customer.model.SalesRep;
import com.unilog.ecommerce.customer.model.Terms;
import com.unilog.ecommerce.customer.service.CustomerService;
import com.unilog.ecommerce.customer.service.CustomerServiceFactory;
import com.unilog.ecommerce.user.dao.UserRepository;
import com.unilog.ecommerce.user.dao.UserRepositoryFactory;
import com.unilog.ecommerce.user.model.User;
import com.unilog.ecommerce.user.service.UserService;
import com.unilog.ecommerce.user.validation.util.RegistrationUtils;

public class GenericUserServiceImpl implements UserService {
	
	private GenericUserServiceImpl() {}
	
	private static final Logger logger = Logger.getLogger(GenericUserServiceImpl.class);
	protected static UserService userService;
	
	public static UserService getInstance() {
		if(userService == null) {
			synchronized (GenericUserServiceImpl.class) {
				userService = new GenericUserServiceImpl();
			}
		}
		return userService;
	}
	
	@Override
	public boolean b2bOnAccountService(User user) {
		return false;
	}

	@Override
	public boolean b2bNewAccountService(User user) {
		Customer customer = new Customer();
		boolean status = false;
		Connection connection = null;
		try {
			connection = ConnectionManager.getDBConnection();
			connection.setAutoCommit(false);
			
			user.getAddress().setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
			user.getAddress().setAccountNumber(user.getAccountNumber());
			
			customer.setAccountName(user.getAccountName());
			customer.setAccountNumber(user.getAccountNumber());
			customer.setAccessPermission(user.getAccessPermission());
			customer.setSalesRep(new SalesRep());	// when you have these details use overloaded constructor
			customer.setTerms(new Terms());			// when you have these details use overloaded constructor
			customer.setBillAddress(user.getAddress());
			customer.setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
			
			String customerServiceType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_REG_SERVICE_TYPE");
			String userRepoType = CommonDBQuery.getSystemParamtersList().get("USER_REG_REPO_TYPE");
			
			CustomerService customerService = CustomerServiceFactory.getCustomerService(customerServiceType);
			int customerId = customerService.addCustomer(connection, customer);
			
			if(customerId > 0){
				int billAddressId = customerService.addBillAddress(connection, customerId, user.getAddress());
				int shipAddressId = customerService.addShipAddress(connection, customerId, user.getAddress());
				user.setBillAddressId(billAddressId);
				user.setShipAddressId(shipAddressId);
				UserRepository userRepository = UserRepositoryFactory.getUserRepository(userRepoType);
				user.setCustomerId(customerId);
				int userId = userRepository.addUser(connection, user);
				if(userId > 0) {
					connection.commit();
					status = true;
					RegistrationUtils.buildRequiredKeywords(customerId, userId);
				}
			}
		} catch (SQLException e) {
			logger.error("B2B New Account Registration User Service", e);
		}
		finally {
			ConnectionManager.closeDBConnection(connection);
		}
		return status;
	}

	@Override
	public boolean b2cRetailService(User user) {
		Customer customer = new Customer();
		boolean status = false;
		Connection connection = null;
		try {
			connection = ConnectionManager.getDBConnection();
			connection.setAutoCommit(false);
			
			user.getAddress().setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
			user.getAddress().setAccountNumber(user.getAccountNumber());
			
			customer.setAccountName(user.getAccountName());
			customer.setAccountNumber(user.getAccountNumber());
			customer.setAccessPermission(user.getAccessPermission());
			customer.setSalesRep(new SalesRep());	// when you have these details use overloaded constructor
			customer.setTerms(new Terms());			// when you have these details use overloaded constructor
			customer.setBillAddress(user.getAddress());
			customer.setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));

			String customerServiceType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_REG_SERVICE_TYPE");
			String userRepoType = CommonDBQuery.getSystemParamtersList().get("USER_REG_REPO_TYPE");
			
			CustomerService customerService = CustomerServiceFactory.getCustomerService(customerServiceType);
			int customerId = customerService.addCustomer(connection, customer);
			
			if(customerId > 0){
				int billAddressId = customerService.addBillAddress(connection, customerId, user.getAddress());
				int shipAddressId = customerService.addShipAddress(connection, customerId, user.getAddress());
				user.setBillAddressId(billAddressId);
				user.setShipAddressId(shipAddressId);
				UserRepository userRepository = UserRepositoryFactory.getUserRepository(userRepoType);
				user.setCustomerId(customerId);
				int userId = userRepository.addUser(connection, user);
				if(userId > 0) {
					connection.commit();
					status = true;
					RegistrationUtils.buildRequiredKeywords(customerId, userId);
				}
			}
		} catch (SQLException e) {
			logger.error("B2C New Account Registration User Service", e);
		}
		finally {
			ConnectionManager.closeDBConnection(connection);
		}
		return status;
	}

	@Override
	public boolean b2bCreditRequest() {
		
		return false;
	}
}
