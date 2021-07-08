package com.unilog.ecommerce.user.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.ecommerce.customer.external.ExternalCustomerService;
import com.unilog.ecommerce.customer.external.ExternalCustomerServiceFactory;
import com.unilog.ecommerce.customer.model.Customer;
import com.unilog.ecommerce.customer.service.CustomerService;
import com.unilog.ecommerce.customer.service.CustomerServiceFactory;
import com.unilog.ecommerce.user.dao.UserRepository;
import com.unilog.ecommerce.user.dao.UserRepositoryFactory;
import com.unilog.ecommerce.user.external.ExternalUserService;
import com.unilog.ecommerce.user.external.ExternalUserServiceFactory;
import com.unilog.ecommerce.user.model.Address;
import com.unilog.ecommerce.user.model.User;
import com.unilog.ecommerce.user.service.UserService;
import com.unilog.ecommerce.user.validation.util.RegistrationUtils;

public class Cimm2bcEclipseV1UserSerivce implements UserService {
	protected static UserService userService; 
	private static final Logger logger = Logger.getLogger(Cimm2bcEclipseV1UserSerivce.class);
	
	public static UserService getInstance() {
			synchronized (Cimm2bcEclipseV1UserSerivce.class) {
				if(userService == null) {
					userService = new Cimm2bcEclipseV1UserSerivce();
				}
			}
		return userService;
	}
	
	@Override
	public boolean b2bOnAccountService(User user) {
		Connection connection = null;
		boolean status = false, iscustomerExists = true;
		String externalUserId = null;
		try {
			
			user.getAddress().setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
			user.getAddress().setAccountNumber(user.getAccountNumber());
			
			String customerServiceType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_REG_SERVICE_TYPE");
			String userRepoType = CommonDBQuery.getSystemParamtersList().get("USER_REG_REPO_TYPE");
			CustomerService customerService = CustomerServiceFactory.getCustomerService(customerServiceType);
			
			String externalCustomerServiceType = CommonDBQuery.getSystemParamtersList().get("EXTERNAL_CUSTOMER_SERVICE_TYPE");
			ExternalCustomerService externalCustomerService = ExternalCustomerServiceFactory.getExternalService(externalCustomerServiceType);
			
			String externalUserServiceType = CommonDBQuery.getSystemParamtersList().get("EXTERNAL_USER_SERVICE_TYPE");
			ExternalUserService externalUserService = ExternalUserServiceFactory.getExterUserService(externalUserServiceType);
			
			connection = ConnectionManager.getDBConnection();
			connection.setAutoCommit(false);
			int customerId = customerService.doesCustomerExists(user.getAccountNumber());
			if(customerId <= 0) {
				iscustomerExists = false;
				Customer customer = externalCustomerService.getCustomer(user.getAccountNumber());
				if(customer != null) {
					externalUserId = externalUserService.createUser(user);
					if(externalUserId != null && externalUserId.trim().length() > 0) {
						customer.setAccessPermission(user.getAccessPermission());
						customerId = customerService.addCustomer(connection, customer);
						int billAddressId = customerService.addBillAddress(connection, customerId, customer.getBillAddress());
						int shipAddressId = 0;
						for(Address shipAddress : customer.getShipAddresses()) {
							shipAddressId = customerService.addShipAddress(connection, customerId, shipAddress);
						}
						user.setExternalUserId(externalUserId);
						user.setBillAddressId(billAddressId);
						user.setShipAddressId(shipAddressId);
					}
				}
			}
			if(customerId > 0){
				UserRepository userRepository = UserRepositoryFactory.getUserRepository(userRepoType);
				if(iscustomerExists) {
					externalUserId = externalUserService.createUser(user);
				}
				if(externalUserId != null && externalUserId.trim().length() > 0) {
					user.setExternalUserId(externalUserId);
					user = userRepository.setUserBillShipAddressId(user, customerId);
					user.setCustomerId(customerId);
					int userId = userRepository.addUser(connection, user);
					if(userId > 0) {
						connection.commit();
						status = true;
						RegistrationUtils.buildRequiredKeywords(customerId, userId);
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Exception : B2B on Account Service ", e);
		}
		finally {
			ConnectionManager.closeDBConnection(connection);
		}
		return status;
	}

	@Override
	public boolean b2bNewAccountService(User user) {
		Connection connection = null;
		boolean status = false;
		int customerId = 0;
		try {
			
			user.getAddress().setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
			user.getAddress().setAccountNumber(user.getAccountNumber());
			
			String externalCustomerServiceType = CommonDBQuery.getSystemParamtersList().get("EXTERNAL_CUSTOMER_SERVICE_TYPE");
			ExternalCustomerService externalCustomerService = ExternalCustomerServiceFactory.getExternalService(externalCustomerServiceType);
			
			Customer customer = new Customer();
			customer.setUsers(Arrays.asList(user));
			customer = externalCustomerService.createCustomer(customer);
			
			if(customer != null) {
				connection = ConnectionManager.getDBConnection();
				connection.setAutoCommit(false);
				
				String customerServiceType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_REG_SERVICE_TYPE");
				CustomerService customerService = CustomerServiceFactory.getCustomerService(customerServiceType);
				customerId = customerService.addCustomer(connection, customer);
				int billAddressId = customerService.addBillAddress(connection, customerId, customer.getBillAddress());
				int shipAddressId = 0;
				for(Address shipAddress : customer.getShipAddresses()) {
					shipAddressId = customerService.addShipAddress(connection, customerId, shipAddress);
				}
				user = customer.getUsers().get(0);
				user.setBillAddressId(billAddressId);
				user.setShipAddressId(shipAddressId);
				
				String userRepoType = CommonDBQuery.getSystemParamtersList().get("USER_REG_REPO_TYPE");
				UserRepository userRepository = UserRepositoryFactory.getUserRepository(userRepoType);
				int userId = userRepository.addUser(connection, user);
				if(userId > 0) {
					connection.commit();
					status = true;
					RegistrationUtils.buildRequiredKeywords(customerId, userId);
				}
			}
		}catch (Exception e) {
			logger.error("Exception in B2C Service layer", e);
		}
		return status;
	}

	@Override
	public boolean b2cRetailService(User user) {
		Connection connection = null;
		boolean status = false;
		int customerId = 0, userId = 0;
		try {
			
			user.getAddress().setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
			user.getAddress().setAccountNumber(user.getAccountNumber());
			
			String externalCustomerServiceType = CommonDBQuery.getSystemParamtersList().get("EXTERNAL_CUSTOMER_SERVICE_TYPE");
			ExternalCustomerService externalCustomerService = ExternalCustomerServiceFactory.getExternalService(externalCustomerServiceType);
			
			Customer customer = new Customer();
			customer.setUsers(Arrays.asList(user));
			customer = externalCustomerService.createCustomer(customer);
			
			if(customer != null) {
				connection = ConnectionManager.getDBConnection();
				connection.setAutoCommit(false);
				customer.setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
				
				String customerServiceType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_REG_SERVICE_TYPE");
				CustomerService customerService = CustomerServiceFactory.getCustomerService(customerServiceType);
				customerId = customerService.addCustomer(connection, customer);
				int billAddressId = customerService.addBillAddress(connection, customerId, customer.getBillAddress());
				int shipAddressId = 0;
				for(Address shipAddress : customer.getShipAddresses()) {
					shipAddressId = customerService.addShipAddress(connection, customerId, shipAddress);
				}
				user = customer.getUsers().get(0);
				user.setBillAddressId(billAddressId);
				user.setShipAddressId(shipAddressId);
				user.setCustomerId(customerId);
				String userRepoType = CommonDBQuery.getSystemParamtersList().get("USER_REG_REPO_TYPE");
				UserRepository userRepository = UserRepositoryFactory.getUserRepository(userRepoType);
				userId = userRepository.addUser(connection, user);
				if(userId > 0) {
					connection.commit();
					status = true;
					RegistrationUtils.buildRequiredKeywords(customerId, userId);
				}
			}
		}catch (Exception e) {
			logger.error("Exception in B2C Service layer", e);
		}
		return status;
	}

	@Override
	public boolean b2bCreditRequest() {
		return false;
	}

}
