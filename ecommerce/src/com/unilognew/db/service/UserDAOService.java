package com.unilognew.db.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unilog.database.ConnectionManager;
import com.unilognew.exception.DBServiceException;
import com.unilognew.model.BuyingCompany;
import com.unilognew.model.BuyingCompanyAddressBook;
import com.unilognew.util.CimmUtil;
import com.unilognew.util.ECommerceEnumType.AddressType;
import com.unilognew.util.ECommerceEnumType.KeywordsEntity;

public class UserDAOService extends BaseDAOService {

	private static UserDAOService userDAOService;
	private Logger logger = LoggerFactory.getLogger(UserDAOService.class);

	private UserDAOService() {

	}

	public static UserDAOService getInstance() {
		try{
			synchronized (UserDAOService.class) {
				if (userDAOService == null) {
					userDAOService = new UserDAOService();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return userDAOService;
	}

	public BuyingCompany getBuyingCompany(int buyingCompany) throws DBServiceException {
		List<BuyingCompany> buyingCompanies = null;
		logger.info("Fetching buying company...based on buying company id..");
		buyingCompanies = getBuyingCompanies(buyingCompany,"BUYING_COMPANY_ID");
		logger.info("Fetching buying company...based on buying company id..ends");
		if(buyingCompanies!=null && !buyingCompanies.isEmpty()) {
			return buyingCompanies.get(0);
		} else {
			return null;
		}
	}

	public List<BuyingCompany> getChildBuyingCompany(int buyingCompany) throws DBServiceException {
		logger.info("Fetching buying company...based on parent company id..");
		List<BuyingCompany> childBuyingCompanies =  getBuyingCompanies(buyingCompany,"PARENT_COMPANY_ID");
		logger.info("Fetching buying company...based on parent company id..ends");
		return childBuyingCompanies;
	}


	private List<BuyingCompany> getBuyingCompanies(int buyingCompanyId,String whereConditionColumn)
			throws DBServiceException {
		logger.info("Fetching buying company...");
		List<BuyingCompany> childrenBuyingCompanyList = null;
		StringBuilder queryBuilder = null;
		BuyingCompany buyingCompany;

		queryBuilder = new StringBuilder("")
		.append("SELECT ")
		.append("BC.BUYING_COMPANY_ID, BC.ENTITY_ID, BC.CUSTOMER_NAME,BC.ADDRESS1, BC.ADDRESS2")
		.append(",BC.CITY, BC.STATE, BC.COUNTRY, BC.ZIP,BC.CUSTOMER_TYPE,BC.SHIP_TO_ID,BC.EMAIL")
		.append(" FROM ").append("BUYING_COMPANY BC")
		.append(" WHERE ")
		.append(" UPPER(BC.STATUS) = 'A'")
		.append(" AND ")
		.append("BC.").append(whereConditionColumn)
		.append(" = ?");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String customerType;
		try {
			conn = ConnectionManager.getDBConnection();

			pstmt = conn.prepareStatement(queryBuilder.toString());
			pstmt.setInt(1, buyingCompanyId);

			rs = pstmt.executeQuery();

			childrenBuyingCompanyList = new ArrayList<BuyingCompany>();

			while (rs.next()) {

				buyingCompany = new BuyingCompany();
				buyingCompany.setBuyingCompanyId(rs
						.getInt("BUYING_COMPANY_ID"));
				buyingCompany.setCustomerName(rs
						.getString("CUSTOMER_NAME"));
				buyingCompany.setEntityId(rs.getString("ENTITY_ID"));
				buyingCompany.setAddress1(rs.getString("ADDRESS1"));
				buyingCompany.setAddress2(rs.getString("ADDRESS2"));
				buyingCompany.setCity(rs.getString("CITY"));
				buyingCompany.setState(rs.getString("STATE"));
				buyingCompany.setCountry(rs.getString("COUNTRY"));
				buyingCompany.setZipcode(rs.getString("ZIP"));

				customerType = rs.getString("CUSTOMER_TYPE");
				if (customerType == null || customerType.isEmpty()) {
					customerType = "C";
				}
				buyingCompany.setCustomerType(customerType);
				buyingCompany.setShipToId(rs.getString("SHIP_TO_ID"));
				buyingCompany.setEmailAddress(rs.getString("EMAIL"));
				buyingCompany.setSelectedForUpdation(true);
				childrenBuyingCompanyList.add(buyingCompany);
			}
		} catch (Exception e) {
			throw new DBServiceException(e.getMessage());
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		logger.info("Fetching buying company...Done");
		return childrenBuyingCompanyList;
	}

	public BuyingCompanyAddressBook getBillToAddress(int buyingCompanyId)
			throws DBServiceException {
		logger.info("Fetching bill to address...");

		BuyingCompanyAddressBook billToAddress = null;

		StringBuilder queryBuilder = null;

		queryBuilder = new StringBuilder("")
		.append("SELECT ")
		.append("BCAB.BC_ADDRESS_BOOK_ID, BCAB.FIRST_NAME, BCAB.LAST_NAME, BCAB.ADDRESS1, BCAB.ADDRESS2")
		.append(",BCAB.CITY, BCAB.STATE, BCAB.ZIPCODE, BCAB.ENTITY_ID, BCAB.COUNTRY, BCAB.PHONE,BCAB.ADDRESS_TYPE")
		.append(",BCAB.SHIP_TO_ID,BCAB.SHIP_TO_NAME,BCAB.EMAIL,BC.BUYING_COMPANY_ID,BC.CUSTOMER_TYPE")
		.append(" FROM ")
		.append("BC_ADDRESS_BOOK BCAB,BUYING_COMPANY BC")
		.append(" WHERE ").append("BC.BUYING_COMPANY_ID = ?")
		.append(" AND ")
		.append("BCAB.BUYING_COMPANY_ID = BC.BUYING_COMPANY_ID ")
		.append(" AND ").append("UPPER(BCAB.ADDRESS_TYPE) = 'BILL' ")
		.append(" AND ").append("UPPER(BC.STATUS)='A'").append(" AND ")
		.append("UPPER(BCAB.STATUS) = 'A'");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();

			pstmt = conn.prepareStatement(queryBuilder.toString());
			pstmt.setInt(1, buyingCompanyId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				billToAddress = new BuyingCompanyAddressBook();
				billToAddress.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
				AddressType addressType = CimmUtil.getAddressType(rs
						.getString("ADDRESS_TYPE"));
				billToAddress.setAddressType(addressType);
				billToAddress.setFirstName(rs.getString("FIRST_NAME"));
				billToAddress.setLastName(rs.getString("LAST_NAME"));
				billToAddress.setEntityId(rs.getString("ENTITY_ID"));
				billToAddress.setAddress1(rs.getString("ADDRESS1"));
				billToAddress.setAddress2(rs.getString("ADDRESS2"));
				billToAddress.setCity(rs.getString("CITY"));
				billToAddress.setState(rs.getString("STATE"));
				billToAddress.setZipcode(rs.getString("ZIPCODE"));
				billToAddress.setCountry(rs.getString("COUNTRY"));
				billToAddress.setPhone(rs.getString("PHONE"));
				billToAddress.setShipToId(rs.getString("SHIP_TO_ID"));
				billToAddress.setShipToName(rs.getString("SHIP_TO_NAME"));
				billToAddress
				.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
				billToAddress.setEmailAddress(rs.getString("EMAIL"));
				billToAddress.setCustomerType(rs.getString("CUSTOMER_TYPE"));
			}
		} catch (Exception e) {
			throw new DBServiceException(e.getMessage());
		} finally {
			ConnectionManager.closeDBConnection(conn);
		}
		logger.info("Fetching bill to address...Done");
		return billToAddress;

	}

	public List<BuyingCompanyAddressBook> getShipToAddressList(int userId,
			int buyingCompanyId) throws DBServiceException {
		logger.info("Fetching ship to address...");

		List<BuyingCompanyAddressBook> shipAddressList = null;

		StringBuilder selectPart = null;
		StringBuilder conditionPart = null;
		StringBuilder queryBuilder = null;

		selectPart = new StringBuilder("")
		.append("SELECT ")
		.append("BCAB.BC_ADDRESS_BOOK_ID, BCAB.FIRST_NAME, BCAB.LAST_NAME, BCAB.ADDRESS1, BCAB.ADDRESS2")
		.append(",BCAB.CITY, BCAB.STATE, BCAB.ZIPCODE, BCAB.ENTITY_ID, BCAB.COUNTRY, BCAB.PHONE,BCAB.ADDRESS_TYPE")
		.append(",BCAB.SHIP_TO_ID,BCAB.SHIP_TO_NAME,BCAB.EMAIL,BC.BUYING_COMPANY_ID");

		conditionPart = new StringBuilder("").append(" AND ")
				.append("BCAB.BUYING_COMPANY_ID = BC.BUYING_COMPANY_ID ")
				.append(" AND ").append("UPPER(BCAB.ADDRESS_TYPE) = 'SHIP' ")
				.append(" AND ").append("UPPER(BC.STATUS)='A'").append(" AND ")
				.append("UPPER(BCAB.STATUS) = 'A'");

		queryBuilder = new StringBuilder("")
		.append(selectPart.toString())
		.append(",USP.USER_SHIPTO_ID")
		.append(" FROM ")
		.append("USER_SHIPTO USP,BC_ADDRESS_BOOK BCAB,BUYING_COMPANY BC")
		.append(" WHERE ").append("USP.CIMM_USER_ID = ?")
		.append(" AND ")
		.append("USP.BC_ADDRESS_BOOKID = BCAB.BC_ADDRESS_BOOK_ID")
		.append(conditionPart.toString())
		.append(" ORDER BY USP.DISPLAY_SEQ ASC ");

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = ConnectionManager.getDBConnection();

			pstmt = conn.prepareStatement(queryBuilder.toString());
			pstmt.setInt(1, userId);

			shipAddressList = processShipToAddressListData(pstmt);

			if (shipAddressList.isEmpty()) {
				queryBuilder = new StringBuilder("")
				.append(selectPart.toString())
				.append(",null as USER_SHIPTO_ID").append(" FROM ")
				.append("BC_ADDRESS_BOOK BCAB,BUYING_COMPANY BC")
				.append(" WHERE ").append("BC.BUYING_COMPANY_ID = ?")
				.append(conditionPart.toString());

				pstmt = conn.prepareStatement(queryBuilder.toString());
				pstmt.setInt(1, buyingCompanyId);

				shipAddressList = processShipToAddressListData(pstmt);
			}

		} catch (Exception e) {
			throw new DBServiceException(e.getMessage());
		} finally {

			// prepared statement is already closed
			ConnectionManager.closeDBConnection(conn);
		}
		logger.info("Fetching ship to address...Done");
		return shipAddressList;

	}

	private List<BuyingCompanyAddressBook> processShipToAddressListData(
			PreparedStatement pstmt) throws SQLException {
		List<BuyingCompanyAddressBook> shipAddressList = null;
		BuyingCompanyAddressBook buyingCompanyAddressBook = null;

		ResultSet rs = null;
		try {

			rs = pstmt.executeQuery();
			shipAddressList = new ArrayList<BuyingCompanyAddressBook>();
			while (rs.next()) {				
				buyingCompanyAddressBook = new BuyingCompanyAddressBook();
				buyingCompanyAddressBook.setAddressBookId(rs
						.getInt("BC_ADDRESS_BOOK_ID"));
				AddressType addressType = CimmUtil.getAddressType(rs
						.getString("ADDRESS_TYPE"));
				buyingCompanyAddressBook.setAddressType(addressType);
				buyingCompanyAddressBook.setFirstName(rs
						.getString("FIRST_NAME"));
				buyingCompanyAddressBook.setLastName(rs.getString("LAST_NAME"));
				buyingCompanyAddressBook.setEntityId(rs.getString("ENTITY_ID"));
				buyingCompanyAddressBook.setAddress1(rs.getString("ADDRESS1"));
				buyingCompanyAddressBook.setAddress2(rs.getString("ADDRESS2"));
				buyingCompanyAddressBook.setCity(rs.getString("CITY"));
				buyingCompanyAddressBook.setState(rs.getString("STATE"));
				buyingCompanyAddressBook.setZipcode(rs.getString("ZIPCODE"));
				buyingCompanyAddressBook.setCountry(rs.getString("COUNTRY"));
				buyingCompanyAddressBook.setPhone(rs.getString("PHONE"));
				buyingCompanyAddressBook.setShipToId(rs.getString("SHIP_TO_ID"));
				buyingCompanyAddressBook.setShipToName(rs
						.getString("SHIP_TO_NAME"));
				buyingCompanyAddressBook.setBuyingCompanyId(rs
						.getInt("BUYING_COMPANY_ID"));
				buyingCompanyAddressBook.setEmailAddress(rs.getString("EMAIL"));
				buyingCompanyAddressBook.setUserShipToId(rs
						.getInt("USER_SHIPTO_ID"));

				shipAddressList.add(buyingCompanyAddressBook);
			}
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		return shipAddressList;
	}

	public void generateOrResetKeywordForBuyingCompany(int buyingCompanyId) {

		List<BuyingCompany> childBuyingCompanyList = null;
		int buyingCompanyIds[] = null;
		int count = 1;
		try {
			childBuyingCompanyList = getChildBuyingCompany(buyingCompanyId);
			if (childBuyingCompanyList != null
					&& !childBuyingCompanyList.isEmpty()) {
				buyingCompanyIds = new int[childBuyingCompanyList.size() + 1]; // add
				// the
				// parent
				// to
				// the
				// list
				buyingCompanyIds[0] = buyingCompanyId; // add the parent to the
				// list
				for (BuyingCompany buyingCompany : childBuyingCompanyList) {
					buyingCompanyIds[count] = buyingCompany
							.getBuyingCompanyId();
					count++;
				}

				generateOrResetKeyword(KeywordsEntity.BUYINGCOMPANY,
						buyingCompanyIds);
			}
		} catch (DBServiceException e) {
			// This exception is being ignored intentionally
			logger.info("Exception occured while updating the keyword column in Buying Company table "
					+ e.getMessage());
		}

	}

	public void generateOrResetKeyword(KeywordsEntity type, int[] ids) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		int i = 0;
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareCall("{call USER_KEYWORD_PROC(?,?)}");
			for (i = 0; i < ids.length; i++) {
				pstmt.setString(1, type.name());
				pstmt.setInt(2, ids[i]);
				pstmt.execute();
			}
		} catch (Exception e) {
			// This exception is being ignored intentionally
			logger.info("Exception occured while invoking USER_KEYWORD_PROC procedure for the type "
					+ type.name()
					+ " and id "
					+ ids[i]
							+ " Exception :"
							+ e.getMessage());
		} finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
	}

}
