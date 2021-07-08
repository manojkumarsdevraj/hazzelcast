package com.unilog.ecommerce.user.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.ecommerce.customer.external.ExternalCustomerService;
import com.unilog.ecommerce.customer.external.ExternalCustomerServiceFactory;
import com.unilog.ecommerce.customer.model.Customer;
import com.unilog.ecommerce.customer.service.CustomerService;
import com.unilog.ecommerce.customer.service.CustomerServiceFactory;
import com.unilog.ecommerce.user.dao.UserRepository;
import com.unilog.ecommerce.user.dao.UserRepositoryFactory;
import com.unilog.ecommerce.user.model.AccessPermission;
import com.unilog.ecommerce.user.model.Address;
import com.unilog.ecommerce.user.model.User;
import com.unilog.ecommerce.user.validation.util.RegistrationUtils;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

public class PurvisCimm2bcUserServiceImpl extends Cimm2bcUserServiceImpl {
	private static final Logger logger = Logger.getLogger(PurvisCimm2bcUserServiceImpl.class);
	
	public static UserService getInstance() {
			synchronized (PurvisCimm2bcUserServiceImpl.class) {
				if(userService == null) {
					userService = new PurvisCimm2bcUserServiceImpl();
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
			user.getAddress().setWarehouseCode(user.getDefaultWareHouseCode() != null ? user.getDefaultWareHouseCode() : CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
			user.getAddress().setAccountNumber(user.getAccountNumber());
			
			int customerId = customerService.doesCustomerExists(user.getAccountNumber());
			if(customerId <= 0) {
				String externalServiceType = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_EXTERNAL_SERVICE_TYPE");
				ExternalCustomerService externalService = ExternalCustomerServiceFactory.getExternalService(externalServiceType);
				Customer customer = externalService.getCustomer(user.getAccountNumber());
				if(customer != null) {
					if(customer.getAccountName() == null || customer.getAccountName().isEmpty()) {
						customer.setAccountName(user.getAccountName());
					}
					if(user.getDefaultWareHouseCode() != null) {
						customer.setWarehouseCode(user.getDefaultWareHouseCode());
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
				List<User> superUsers = RegistrationUtils.getActiveSuperUsers(customerId, connection);
				User randomSuperUser = null;
				if(superUsers.size() > 0) {
					randomSuperUser = superUsers.get(0);
					user.setStatus("I");
					user.getAccessPermission().setUserRole("Ecomm Customer General User");
					user.setParentUserId(randomSuperUser.getUserId());
				}
				UserRepository userRepository = UserRepositoryFactory.getUserRepository(userRepoType);
				user = userRepository.setUserBillShipAddressId(user, customerId);
				user.setCustomerId(customerId);
				int userId = userRepository.addUser(connection, user);
				if(userId > 0) {
					connection.commit();
					status = true;
					RegistrationUtils.buildRequiredKeywords(customerId, userId);
					if(superUsers.size() > 0) {
						ArrayList<Integer> approverIds = new ArrayList<>();
						if(randomSuperUser!=null) {
							approverIds.add(randomSuperUser.getUserId());
						}
						UsersDAO.assignApprover(randomSuperUser.getUserId(), approverIds, userId);
						List<String> superUserMailIds = RegistrationUtils.extractEmailIds(superUsers);
						RegistrationUtils.notifyResgistrationToSuperUsers(user, superUserMailIds);
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
		return true;
	}
	
	@Override
	public boolean b2cRetailService(User user) {
		return true;
	}
	
	@Override
	public void customerImport(HttpServletRequest request) {
		try {
			List<User> users = extractUsersFromFile();
			if(users.size() > 0) {
				for(User user : users) {
					System.out.println("Registration Status For " + user.getEmailId() +"/"+ user.getAddress().getEmailId() + " : " + b2bOnAccountService(user));
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<User> extractUsersFromFile() throws IOException{
		List<User> users = new ArrayList<>();
		String filePath = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_IMPORT_FILE_PATH"));
		if(filePath.length() > 0) {
			File srcFile = new File(filePath);
			FileInputStream fis = new FileInputStream(srcFile);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);

            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()) {
            	Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                
                if(row.getRowNum() > 0){
                	User user = new User();
            		Address address = new Address();
                	while (cellIterator.hasNext()) {
                		Cell cell = cellIterator.next();
                		
                		switch(cell.getCellType()) {
                		case Cell.CELL_TYPE_STRING :
                			if(cell.getColumnIndex() == 0) {
                				user.setFirstName(cell.getStringCellValue());
                			}else if(cell.getColumnIndex() == 1) {
                				user.setLastName(cell.getStringCellValue());
                			}else if(cell.getColumnIndex() == 2) {
                				user.setExternalUserId(cell.getStringCellValue());
                			}else if(cell.getColumnIndex() == 3) {
                				address.setEmailId(cell.getStringCellValue());
                				user.setEmailId(cell.getStringCellValue());
                			}else if(cell.getColumnIndex() == 4) {
                				user.setDefaultWareHouseCode(cell.getStringCellValue());
                			}else if(cell.getColumnIndex() == 5) {
                				if(CommonUtility.validateString(user.getEmailId()).length() <= 0){
                					address.setEmailId(cell.getStringCellValue());
                					user.setEmailId(cell.getStringCellValue());
                				}
                			}
                			
                			break;
                		case Cell.CELL_TYPE_NUMERIC :
                			System.out.println(cell.getNumericCellValue());
                			break;
                		}
                		
                	}
                	address.setAddress1("5050 North Freeway");
                	address.setCity("Fort Worth");
                	address.setState("TX");
                	address.setZipCode("76137");
                	if(CommonUtility.validateString(user.getEmailId()).length() <= 0){
                		user.setEmailId(user.getExternalUserId() +"rep@purvisindustries.com");
                		address.setEmailId(user.getExternalUserId() +"rep@purvisindustries.com");
                	}
                	user.setAccountNumber("SALESREP");
                	
                	if(user.getLastName() == null) {
                		user.setLastName(" ");
                	}
        			
        			user.setPassword("Purvis123##");
        			user.setConfirmPassword("Purvis123##");
        			user.setAddress(address);
        			user.setAccessPermission(new AccessPermission("Ecomm SALES_REP", "Y", "Y"));
        			user.setStatus("Y");
        			users.add(user);
        			System.out.println("User" + user);
                	System.out.println("---------------------------------------------------------------");
                }
            }
		}
		System.out.println("No of Users : " + users.size());
		return users;
	}
}
