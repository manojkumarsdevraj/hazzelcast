package com.unilog.ecommerce.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.ecommerce.user.model.User;
import com.unilog.security.SecureData;

public class GenericUserRepoImpl implements UserRepository {

	private GenericUserRepoImpl() {}
	
	private static final Logger logger = Logger.getLogger(GenericUserRepoImpl.class);
	protected static UserRepository userRepository;
	
	public static UserRepository getInstance() {
		if(userRepository == null) {
			synchronized (GenericUserRepoImpl.class) {
				userRepository = new GenericUserRepoImpl();
			}
		}
		return userRepository;
	}
	
	@Override
	public int addUser(Connection connection, User user) {
		int userId=0;
	    PreparedStatement pstmt = null;
	    int count = 0;
	    try{
	    	String sql = "INSERT INTO CIMM_USERS(USER_ID,USER_NAME,PASSWORD,FIRST_NAME,LAST_NAME,BUYING_COMPANY_ID,OFFICE_PHONE,CELL_PHONE,FAX,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,ECLIPSE_CONTACT_ID,EMAIL,UPDATED_DATETIME,FIRST_LOGIN,TERMS_TYPE,TERMS_TYPE_DESC,SHIP_VIA,SHIP_VIA_DESC,PARENT_USER_ID ,REGISTERED_DATE, STATUS,JOB_TITLE, SALES_REP_CONTACT,SITE_ID,ACCOUNT_MANAGER,CLOSEST_BRANCH,USER_TYPE,EXTERNAL_SYSTEM2_USER_ID,DEFAULT_BILLING_ADDRESS_ID,DEFAULT_SHIPPING_ADDRESS_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?,'ECOMM',?,?,?)";
			userId = CommonDBQuery.getSequenceId("USER_ID_SEQUENCE");
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setString(2, user.getAddress().getEmailId());//userName
			pstmt.setString(3, SecureData.getsecurePassword(user.getPassword()));
			pstmt.setString(4, user.getFirstName());
			pstmt.setString(5, user.getLastName());
			pstmt.setInt(6, user.getCustomerId());
			pstmt.setString(7, user.getAddress().getPhoneNumber());
			pstmt.setString(8, " ");
			pstmt.setString(9, ""); //user.getFaxNo()
			pstmt.setString(10, user.getAddress().getAddress1());
			pstmt.setString(11, user.getAddress().getAddress2());
			pstmt.setString(12, user.getAddress().getCity());
			pstmt.setString(13, user.getAddress().getState());
			pstmt.setString(14, user.getAddress().getZipCode());
			pstmt.setString(15, user.getAddress().getCountry());
			pstmt.setString(16, user.getAccountNumber());
			pstmt.setString(17, user.getAddress().getEmailId());
			pstmt.setString(18, "Y"); //first Login 
			pstmt.setString(19, ""); //userDetailList.getTermsType()
			pstmt.setString(20, ""); //userDetailList.getTermsTypeDesc()
			pstmt.setString(21, ""); //userDetailList.getShipViaID()
			pstmt.setString(22, ""); //userDetailList.getShipViaMethod()
			pstmt.setInt(23, user.getParentUserId()); //CommonUtility.validateNumber(parentUserId)
			pstmt.setString(24, user.getStatus());
			pstmt.setString(25, user.getJobTitle());
			pstmt.setString(26, ""); //userDetailList.getSalesRepContact()
			pstmt.setInt(27, CommonDBQuery.getGlobalSiteId());
			pstmt.setString(28, "");  // CommonUtility.validateString(userDetailList.getAccountManager())
			pstmt.setString(29, "");  // CommonUtility.validateString(userDetailList.getClosestBranch())
			pstmt.setString(30, user.getExternalUserId());  //CommonUtility.validateString(userDetailList.getUserERPId())
			pstmt.setInt(31, user.getBillAddressId()); // defaultBillId
			pstmt.setInt(32, user.getShipAddressId()); // defaultShipId
			count = pstmt.executeUpdate();
			if(count > 0) {
				assignRole(connection, userId, user.getAccessPermission().getUserRole());
			}else {
				userId = 0;
			}
	    }
	    catch(SQLException e)
	    {
	    	userId = 0;
	    	logger.error("SQL Exception Add User", e);
	    }
	    catch(Exception e)
	    {
	    	userId = 0;
	    	logger.error("Exception Add User", e);
	    }
	    finally
	    {
	    	ConnectionManager.closeDBPreparedStatement(pstmt);
	    }
		return userId;
	}

	@Override
	public boolean doesUserExists(String emailAddress) {
		boolean isUserExists= false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
	    try
	    {
	    	conn = ConnectionManager.getDBConnection();
			String sql = "select user_id from cimm_users where upper(user_name)=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, emailAddress.toUpperCase());
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				isUserExists = true;
			}
	    }catch(SQLException e){
	    	isUserExists = true;
	    	logger.error("SQL Exception- checking user exists in DB", e);
		}finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
		return isUserExists;
	}
	
	protected void assignRole(Connection connection, int userId, String role) {
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO CIMM_USER_ROLES(USER_ID,ROLE_ID,USER_EDITED,UPDATED_DATETIME) SELECT ? USER_ID,ROLE_ID,? USER_EDITED,SYSDATE FROM CIMM_ROLES WHERE ROLE_NAME=?";
		try
		{
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, userId);
			pstmt.setString(3, role);
			pstmt.executeUpdate();	
		}
		catch (Exception e) {
			logger.error("Exception Assigning Role to User", e);
		}
		finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
	}

	@Override
	public User setUserBillShipAddressId(User user, int customerId) {
		int billAddressId = 0, shipAddressId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
	    try
	    {
	    	conn = ConnectionManager.getDBConnection();
			String sql = "select BC_ADDRESS_BOOK_ID, BUYING_COMPANY_ID, ADDRESS_TYPE from BC_ADDRESS_BOOK where BUYING_COMPANY_ID = ? AND STATUS = 'A'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				if(billAddressId > 0 && shipAddressId > 0) {
					break;
				}
				if(rs.getString("ADDRESS_TYPE").equalsIgnoreCase("Bill")) {
					billAddressId = rs.getInt("BC_ADDRESS_BOOK_ID");
				}
				if(rs.getString("ADDRESS_TYPE").equalsIgnoreCase("Ship") && shipAddressId <= 0) {
					shipAddressId = rs.getInt("BC_ADDRESS_BOOK_ID");
				}
			}
	    }catch(SQLException e){
	    	logger.error("SQL Exception- fetching Bill and Ship address id's from BC address book", e);
		}finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
	    user.setBillAddressId(billAddressId);
	    user.setShipAddressId(shipAddressId);
		return user;
	}
}
