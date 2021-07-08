package com.unilog.ecommerce.customer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.ecommerce.customer.model.Customer;
import com.unilog.ecommerce.user.model.Address;
import com.unilog.ecommerce.user.validation.util.RegistrationUtils;
import com.unilog.utility.CommonUtility;

public class GenericCustomerRepoImpl implements CustomerRepository {
	
	protected static CustomerRepository customerRepository;
	private static final Logger logger = Logger.getLogger(GenericCustomerRepoImpl.class);
	public static CustomerRepository getInstance() {
			synchronized (GenericCustomerRepoImpl.class) {
				if(customerRepository == null) {
					customerRepository = new GenericCustomerRepoImpl();
				}
		}
		return customerRepository;
	}
	
	@Override
	public int addCustomer(Connection connection, Customer customer) throws SQLException {
		PreparedStatement pstmt = null;
		int buyingCompanyId = 0, count = 0;
		try {
			int siteId = CommonDBQuery.getGlobalSiteId();
			String sqlQuery = "INSERT INTO BUYING_COMPANY (BUYING_COMPANY_ID,CUSTOMER_NAME,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,EMAIL,URL,SUBSET_ID,STATUS,UPDATED_DATETIME,ENTITY_ID,PARENT_COMPANY_ID,SHORT_NAME,CURRENCY,WAREHOUSE_CODE_ID,WAREHOUSE_CODE,SITE_ID,ALLOW_SUBMIT_PO,CREDIT_CARD_CHECKOUT,SALES_REP_ID,SALES_REP_EMAILID,TERMS_TYPE,TERMS_DESC,ALLOWED_ORDER_TYPES) VALUES(?,?,?,?,?,?,?,?,?,?,?,'A',SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			buyingCompanyId = CommonDBQuery.getSequenceId("BUYING_COMPANY_ID_SEQ");
			pstmt = connection.prepareStatement(sqlQuery);
	    	pstmt.setInt(1, buyingCompanyId);
	    	pstmt.setString(2, customer.getAccountName());
	    	pstmt.setString(3,(customer.getBillAddress().getAddress1() != null)? customer.getBillAddress().getAddress1() : "No Address");
	    	pstmt.setString(4, customer.getBillAddress().getAddress2());
	    	pstmt.setString(5, customer.getBillAddress().getCity());
	    	pstmt.setString(6, customer.getBillAddress().getState());
	    	pstmt.setString(7, customer.getBillAddress().getZipCode());
	    	pstmt.setString(8, customer.getBillAddress().getCountry());
	    	pstmt.setString(9, customer.getBillAddress().getEmailId());
	    	pstmt.setString(10, "");  //userDetailList.getWebAddress()
	    	pstmt.setInt(11, customer.getSubsetId());
	    	pstmt.setString(12, customer.getAccountNumber());
	    	pstmt.setString(13, "");   //parentCompanyId
	    	pstmt.setString(14, RegistrationUtils.getShortName(customer.getAccountName()));
	    	pstmt.setString(15, "");  // userDetailList.getCurrency()
	    	pstmt.setInt(16, 0);  //userDetailList.getWareHouseCode()  -- CIMM ID
	    	pstmt.setString(17, customer.getWarehouseCode());
	    	pstmt.setInt(18, siteId);
	    	pstmt.setString(19, customer.getAccessPermission().getSubmitPo());  // PO
	    	pstmt.setString(20, customer.getAccessPermission().getSubmitCc()); 
	    	pstmt.setString(21, customer.getSalesRep().getExternalId());
	    	pstmt.setString(22, customer.getSalesRep().getEmailId());
	    	pstmt.setString(23, customer.getTerms().getType());
	    	pstmt.setString(24, customer.getTerms().getDescription());
	    	if(customer.getOrderTypes() != null && customer.getOrderTypes().size() > 0) {
	    		pstmt.setString(25, StringUtils.join(customer.getOrderTypes(), ","));
	    	}
	    	else {
	    		pstmt.setString(25, "");
	    	}
	    	count = pstmt.executeUpdate();
	    	if(count < 1){
	    		buyingCompanyId = 0;
	    	}else{
	    		if(CommonDBQuery.getSystemParamtersList().get("ENABLE_GENERAL_CATALOG_ACCESS")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_GENERAL_CATALOG_ACCESS").trim().equalsIgnoreCase("Y")){
	    			grantGenralCatalogAccess(connection,buyingCompanyId);
	    		}
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		return buyingCompanyId;
	}

	@Override
	public int doesCustomerExists(String accountNumber) {
		int buyingcompanyId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
        try{
        	conn = ConnectionManager.getDBConnection();
			String sql = "select buying_company_id from buying_company where  STATUS= 'A' and ENTITY_ID =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(accountNumber).toUpperCase());
			rs = pstmt.executeQuery();
			if(rs.next()){
				buyingcompanyId = rs.getInt("buying_company_id");
			}
        }catch(SQLException e) {
        	buyingcompanyId = 0;
        	logger.error("SQL Exception while checking customer existence in DB", e);
        }catch(Exception e){
        	buyingcompanyId = 0;
        	logger.error("Exception while checking customer existence in DB", e);
        }
        finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
		return buyingcompanyId;
	}

	@Override
	public int addBillAddress(Connection connection, int customerId, Address address) {
		int addressBookId=0;
		int count = 0;
	    PreparedStatement pstmt = null;
       try
        {
        	String sql = "INSERT INTO BC_ADDRESS_BOOK(BC_ADDRESS_BOOK_ID,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,COUNTRY,PHONE,ADDRESS_TYPE,STATUS,UPDATED_DATETIME,ENTITY_ID,BUYING_COMPANY_ID,FIRST_NAME,LAST_NAME,SHIP_TO_ID,EMAIL,SHIP_TO_NAME,WAREHOUSE_CODE)VALUES(?,?,?,?,?,?,?,?,?,'A',SYSDATE,?,?,?,?,?,?,?,?)";
			addressBookId = CommonDBQuery.getSequenceId("BC_ADDRESS_BOOK_ID_SEQ");
			
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, addressBookId);
			pstmt.setString(2, (address.getAddress1() == null || address.getAddress1().trim().equalsIgnoreCase(""))?"No Address":address.getAddress1());
			pstmt.setString(3, address.getAddress2());
			pstmt.setString(4, address.getCity());
			pstmt.setString(5, address.getState());
			pstmt.setString(6, address.getZipCode());
			pstmt.setString(7, address.getCountry());
			pstmt.setString(8, address.getPhoneNumber());
			pstmt.setString(9, "Bill");
			pstmt.setString(10, address.getAccountNumber());
			pstmt.setInt(11, customerId);
			pstmt.setString(12, ""); //userDetailList.getFirstName()
			pstmt.setString(13, ""); //userDetailList.getLastName()
			pstmt.setString(14, address.getShipToId()); 
			pstmt.setString(15, address.getEmailId());
			pstmt.setString(16, address.getShipToName());
			pstmt.setString(17, address.getWarehouseCode()); 
			count = pstmt.executeUpdate();
			if(count<1){
				addressBookId = 0;
			}
        }
        catch(SQLException e)
        {
        	addressBookId = 0;
        	logger.error("SQL Exception while Adding Bill Address", e);
        }
        catch(Exception e)
        {
        	addressBookId = 0;
        	logger.error("Exception while Adding Bill Address", e);
        }
        finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        }
		return addressBookId;
	}

	@Override
	public int addShipAddress(Connection connection, int customerId, Address address) {

		int addressBookId=0;
		int count = 0;
	    PreparedStatement pstmt = null;
       try
        {
        	String sql = "INSERT INTO BC_ADDRESS_BOOK(BC_ADDRESS_BOOK_ID,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,COUNTRY,PHONE,ADDRESS_TYPE,STATUS,UPDATED_DATETIME,ENTITY_ID,BUYING_COMPANY_ID,FIRST_NAME,LAST_NAME,SHIP_TO_ID,EMAIL,SHIP_TO_NAME,WAREHOUSE_CODE)VALUES(?,?,?,?,?,?,?,?,?,'A',SYSDATE,?,?,?,?,?,?,?,?)";
			addressBookId = CommonDBQuery.getSequenceId("BC_ADDRESS_BOOK_ID_SEQ");
			
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, addressBookId);
			pstmt.setString(2, (address.getAddress1()==null || address.getAddress1().trim().equalsIgnoreCase(""))?"No Address":address.getAddress1());
			pstmt.setString(3, address.getAddress2());
			pstmt.setString(4, address.getCity());
			pstmt.setString(5, address.getState());
			pstmt.setString(6, address.getZipCode());
			pstmt.setString(7, address.getCountry());
			pstmt.setString(8, address.getPhoneNumber());
			pstmt.setString(9, "Ship");
			pstmt.setString(10, address.getAccountNumber());
			pstmt.setInt(11, customerId);
			pstmt.setString(12, ""); //userDetailList.getFirstName()
			pstmt.setString(13, ""); //userDetailList.getLastName()
			pstmt.setString(14, address.getShipToId());   
			pstmt.setString(15, address.getEmailId());
			pstmt.setString(16, address.getShipToName());
			pstmt.setString(17, address.getWarehouseCode());
			count = pstmt.executeUpdate();
			if(count<1){
				addressBookId = 0;
			}
        }
        catch(SQLException e)
        {
        	addressBookId = 0;
        	logger.error("SQL Exception while Adding Ship Address", e);
        }
        catch(Exception e)
        {
        	addressBookId = 0;
        	logger.error("Exception while Adding Ship Address", e);
        }
        finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        }
		return addressBookId;
	
	}

	protected void grantGenralCatalogAccess(Connection connection, int buyingCompanyId) {
		int count=-1;
		PreparedStatement pstmt=null;
	    String sql = "";
		try {
			sql = "UPDATE BUYING_COMPANY SET GENERAL_CATALOG_ACCESS = 'Y' WHERE BUYING_COMPANY_ID=?";
			pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, buyingCompanyId);
            count = pstmt.executeUpdate();
            pstmt.close();
            if(count>0){
            	logger.info("General Catalog Acess Granted Sucessfully for : "+buyingCompanyId);
            }else{
            	logger.info("General Catalog Acess Grant Failed for : "+buyingCompanyId);
            }
		} catch (Exception e) {
			logger.error("Exception while providing general catalog access", e);
		}finally {	    
			 ConnectionManager.closeDBPreparedStatement(pstmt);
	      }
	}
}
