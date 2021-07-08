package com.unilog.ecommerce.user.service;

import java.sql.Connection;
import java.sql.SQLException;

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
import com.unilog.ecommerce.user.model.Address;
import com.unilog.ecommerce.user.model.User;
import com.unilog.ecommerce.user.service.UserService;
import com.unilog.ecommerce.user.validation.util.RegistrationUtils;

public class Cimm2bcUserServiceImpl implements UserService {

	private static final Logger logger = Logger.getLogger(Cimm2bcUserServiceImpl.class);
	protected static UserService userService;
	public static UserService getInstance() {
			synchronized (Cimm2bcUserServiceImpl.class) {
				if(userService == null) {
					userService = new Cimm2bcUserServiceImpl();
				}
			}
		return userService;
	}
	@Override
	public boolean b2bOnAccountService(User user) {
		Connection connection = null;
		boolean status = false;
		
		String customerServiceType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_REG_SERVICE_TYPE");
		String userRepoType = CommonDBQuery.getSystemParamtersList().get("USER_REG_REPO_TYPE");
		CustomerService customerService = CustomerServiceFactory.getCustomerService(customerServiceType);
		try {
			connection = ConnectionManager.getDBConnection();
			connection.setAutoCommit(false);
			user.getAddress().setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
			user.getAddress().setAccountNumber(user.getAccountNumber());
			
			int customerId = customerService.doesCustomerExists(user.getAccountNumber());
			if(customerId <= 0) {
				String externalServiceType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_EXTERNAL_SERVICE_TYPE");
				ExternalCustomerService externalService = ExternalCustomerServiceFactory.getExternalService(externalServiceType);
				Customer customer = externalService.getCustomer(user.getAccountNumber());
				if(customer != null) {
					if(customer.getAccountName() == null) {
						customer.setAccountName(user.getAccountName());
					}
					customer.setAccessPermission(user.getAccessPermission());
					customerId = customerService.addCustomer(connection, customer);
					if(customerId > 0) {
						int billAddressId = customerService.addBillAddress(connection, customerId, customer.getBillAddress());
						int shipAddressId = 0;
						for(Address shipAddress : customer.getShipAddresses()) {
							shipAddressId = customerService.addShipAddress(connection, customerId, shipAddress);
						}
						user.setBillAddressId(billAddressId);
						user.setShipAddressId(shipAddressId);
					}
				}
			}
			if(customerId > 0){
				UserRepository userRepository = UserRepositoryFactory.getUserRepository(userRepoType);
				user = userRepository.setUserBillShipAddressId(user, customerId);
				user.setCustomerId(customerId);
				int userId = userRepository.addUser(connection, user);
				if(userId > 0) {
					connection.commit();
					status = true;
					RegistrationUtils.buildRequiredKeywords(customerId, userId);
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
		user.getAddress().setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
		user.getAddress().setAccountNumber(user.getAccountNumber());
		return false;
	}

	@Override
	public boolean b2cRetailService(User user) {
		user.getAddress().setWarehouseCode(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
		user.getAddress().setAccountNumber(user.getAccountNumber());
		return false;
	}
	@Override
	public boolean b2bCreditRequest() {
		
		return false;
	}

}
