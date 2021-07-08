package com.unilog.users;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.struts2.ServletActionContext;
import org.jdom.input.SAXBuilder;

import com.erp.eclipse.inquiry.LoginSubmit;
import com.erp.service.UserManagement;
import com.erp.service.impl.UserManagementImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.customfields.CustomFieldDAO;
import com.unilog.customfields.CustomModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.CustomFieldModel;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.defaults.ULLog;
import com.unilog.misc.EventModel;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.SalesAction;
import com.unilog.security.SecureData;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.utility.CommonUtility;
import com.unilognew.util.ECommerceEnumType.AddressType;

import WS.Nxtrend.ARGetCustomerDataOrderingResponse;
import oracle.jdbc.OracleTypes;

public class UsersDAO extends SecureData{
	private HttpServletRequest request;
	private static List<WarehouseModel> wareHouses;
	
	public void setSessionValue(String key,String value){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute(key, value);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setSessionValueArray(String key,ArrayList<Integer> userTab){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute(key, userTab);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setSessionIntValue(String key,int value){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute(key, value);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public static int authenticatUser(String userName,String userPassword, String userStatus)
	{
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    int userId=0;	   
	   
        try {
        	conn = ConnectionManager.getDBConnection();
	        if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SINGLE_USER_UNDER_MULTIPLE_CUSTOMER")).equalsIgnoreCase("Y")) {
            preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getMultiCustomerPasswordForUserName"));
            preStat.setString(1, userName);
            preStat.setString(2, getsecurePassword(userPassword));
            preStat.setString(3, userStatus);
            preStat.setInt(4, CommonDBQuery.getGlobalSiteId());
          
            ULLog.sqlTrace(PropertyAction.SqlContainer.get("getMultiCustomerPasswordForUserName")+"\t"+userName);
        }else {
            preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getPasswordForUserName"));
            preStat.setString(1, userName);
            preStat.setString(2, userStatus);
            preStat.setInt(3, CommonDBQuery.getGlobalSiteId());
          
            ULLog.sqlTrace(PropertyAction.SqlContainer.get("getPasswordForUserName")+"\t"+userName);
        }
			  rs =preStat.executeQuery();
			  if(rs.next())
			  {				
				  if(userPassword.equals(new SecureData().validatePassword(rs.getString("PASSWORD"))))				  
					  userId= rs.getInt("USER_ID");				  
			  }
			 
			  ConnectionManager.closeDBPreparedStatement(preStat);

	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	    	 
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
	      
	      return userId;
	}
	
	public static  HashMap<String,String> getUserNameAndPasswordForExternalSystemUserId(String userExternalSystemId, String userStatus){
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    HashMap<String,String> userDetails = new HashMap<String,String>();
        try {
        	  conn = ConnectionManager.getDBConnection();
	          preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getUserNamePasswordForExternalSystemUserId"));			 
			  preStat.setString(1, userStatus);
			  preStat.setString(2, userExternalSystemId);
			  preStat.setInt(3, CommonDBQuery.getGlobalSiteId());
			  int userId = 0;
			  ULLog.sqlTrace(PropertyAction.SqlContainer.get("getUserNamePasswordForExternalSystemUserId"+"\t"+userExternalSystemId));
			  rs =preStat.executeQuery();
			  if(rs.next()){
				  userId = rs.getInt("USER_ID");
				  SecureData checkPass = new SecureData();
				  String decryptPassword = checkPass.validatePassword(CommonUtility.validateString(rs.getString("PASSWORD")));
				  
				  userDetails.put("userId", Integer.toString(userId));
				  userDetails.put("userName", rs.getString("USER_NAME"));
				  userDetails.put("password", CommonUtility.validateString(decryptPassword));
			  }
	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	 
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } 
	      return userDetails;
	}
	
	public static  HashMap<String,String> getUserPasswordAndUserId(String userName, String userStatus)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    HashMap<String,String> userDetails = new HashMap<String,String>();
	    HttpServletRequest request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
	    //getPasswordForUserName - Single Site
	    //getPasswordForUserNameAndSiteSpecificSubset - Multi Site: User can login in both site but Site specific Subset if - Site Id will be considered only for USERSUBSETID from syetem param View
	    //getPasswordForUserNameAndSiteSpecificUserDetail - Multi Site: User can login only in specific site - Site Id will be considered in whole Query
        
        try {
        	conn = ConnectionManager.getDBConnection(); //
        	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SINGLE_USER_UNDER_MULTIPLE_CUSTOMER")).equalsIgnoreCase("Y") && CommonUtility.validateString((String)session.getAttribute("multipleAccountsAvailable")).equalsIgnoreCase("Y") && 
        			CommonUtility.validateNumber((String)session.getAttribute("selectedUserId"))>0) {
        		preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getUserCompleteDetailsByUserId"));
				preStat.setInt(1, CommonUtility.validateNumber((String)session.getAttribute("selectedUserId")));
				preStat.setString(2, userStatus);
				preStat.setInt(3, CommonDBQuery.getGlobalSiteId());
				ULLog.sqlTrace(PropertyAction.SqlContainer.get("getUserCompleteDetailsByUserId"+"\t"+userName));
        	}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MULTI_SITE")).equalsIgnoreCase("Y") && CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID"))>0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableSiteSpecificUserLogin")).equalsIgnoreCase("Y")) {
        		preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getPasswordForUserNameAndSiteSpecificUserDetail"));			 
        		preStat.setInt(1, CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
        		preStat.setInt(2, CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
        		preStat.setString(3, userName);
  			  	preStat.setString(4, userStatus);
  			  	ULLog.sqlTrace(PropertyAction.SqlContainer.get("getPasswordForUserNameAndSiteSpecificUserDetail")+"\t"+userName +"\t Site ID: "+CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
        	}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MULTI_SITE")).equalsIgnoreCase("Y") && CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID"))>0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableSiteSpecificSubsetOnly")).equalsIgnoreCase("Y")) {
        		preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getPasswordForUserNameAndSiteSpecificSubset"));			 
        		preStat.setInt(1, CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
        		preStat.setString(2, userName);
  			  	preStat.setString(3, userStatus);
  			  	preStat.setInt(4, CommonDBQuery.getGlobalSiteId());
  			    ULLog.sqlTrace(PropertyAction.SqlContainer.get("getPasswordForUserNameAndSiteSpecificSubset")+"\t"+userName +"\t Site ID: "+CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
        	}else {
        		preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getPasswordForUserName"));			 
  			  	preStat.setString(1, userName);
  			  	preStat.setString(2, userStatus);
  			  	preStat.setInt(3, CommonDBQuery.getGlobalSiteId());
  			    ULLog.sqlTrace(PropertyAction.SqlContainer.get("getPasswordForUserName"+"\t"+userName));
        	}
	          
			  int userId = 0;
			  rs =preStat.executeQuery();
			  if(rs.next()){
				  int parentCompanyId = rs.getInt("PARENT_COMPANY_ID");
				  int subsetId = rs.getInt("SUBSET_ID");
				  userId = rs.getInt("USER_ID");
				  
				  userDetails.put("userId", Integer.toString(userId));
				  userDetails.put("userName", userName);
				  userDetails.put("password", rs.getString("PASSWORD"));
				  userDetails.put("submitPO", rs.getString("ALLOW_SUBMIT_PO"));
				  userDetails.put("submitRFQ", rs.getString("ALLOW_SUBMIT_RFQ"));
				  userDetails.put("submitNPR", rs.getString("ALLOW_SUBMIT_NPR"));
				  userDetails.put("userSubsetId", rs.getString("SUBSET_ID"));
				  userDetails.put("acceptOrderByPO", rs.getString("ACCEPT_ORDER_BY_PO_NUM"));
				  userDetails.put("submitWithCC", rs.getString("CREDIT_CARD_CHECKOUT"));
				  //userDetails.put("isPunchoutUser", rs.getString("PUNCHOUT_USER"));
				  userDetails.put("firstLogin", rs.getString("FIRST_LOGIN"));
				  userDetails.put("contactId", rs.getString("ECLIPSE_CONTACT_ID"));
				  userDetails.put("firstName", rs.getString("FIRST_NAME"));
				  userDetails.put("lastName", rs.getString("LAST_NAME"));
				  userDetails.put("displayCustPartNum", rs.getString("DISPLAY_CUST_PART_NUMBER"));
				  userDetails.put("allowLiveChat", rs.getString("ALLOW_LIVE_CHAT"));
				  userDetails.put("buyingCompanyId", rs.getString("BUYING_COMPANY_ID"));
				  userDetails.put("defaultBillingAddressId", rs.getString("DEFAULT_BILLING_ADDRESS_ID"));
				  userDetails.put("billingEntityId", rs.getString("ENTITY_ID"));
				  userDetails.put("defaultShippingAddressId", rs.getString("DEFAULT_SHIPPING_ADDRESS_ID"));
				  userDetails.put("parentUserId", rs.getString("PARENT_USER_ID"));
				  userDetails.put("userRole", rs.getString("ROLE_NAME"));
				  userDetails.put("approvalUserId", rs.getString("APPROVAL_USER_ID"));
				  userDetails.put("wareHouseCode",rs.getString("WAREHOUSE_CODE"));
				  userDetails.put("customerCountry",rs.getString("COUNTRY"));
				  userDetails.put("customerShipVia",rs.getString("SHIP_VIA"));
				  userDetails.put("customerShipViaDesc",rs.getString("SHIP_VIA_DESC"));
				  userDetails.put("loginCustomerName",rs.getString("CUSTOMER_NAME"));
				  userDetails.put("userEmailAddress",rs.getString("EMAIL"));
				  userDetails.put("userLevelBuyingCompanyId",rs.getString("BUYING_COMPANY_ID"));
				  userDetails.put("userName",rs.getString("USER_NAME"));
				  userDetails.put("userOfficePhone",rs.getString("OFFICE_PHONE"));
				  userDetails.put("userCellPhone",rs.getString("CELL_PHONE"));
				  userDetails.put("userJobTitle",rs.getString("JOB_TITLE"));
				  userDetails.put("userSalesRepContactName",rs.getString("SALES_REP_CONTACT"));
				  userDetails.put("userProfileImage",rs.getString("PROFILE_IMAGE"));
				  userDetails.put("changePasswordOnLogin", CommonUtility.validateString(rs.getString("CHANGE_PASSWD_ON_LOGIN")));
				  userDetails.put("address", rs.getString("ADDRESS1"));
				  userDetails.put("city", rs.getString("CITY"));
				  userDetails.put("state", rs.getString("STATE"));
				  userDetails.put("zip", rs.getString("ZIP"));
				  userDetails.put("userErpId", rs.getString("EXTERNAL_SYSTEM2_USER_ID"));
				  userDetails.put("customerLandingPageId", rs.getString("LANDING_PAGE_ID"));
				  userDetails.put("systemCustomerId", CommonUtility.validateString(rs.getString("ENTITY_ID_NUMBER")));

				  userDetails.put("userPreferences", CommonUtility.validateString(rs.getString("USER_PREFERENCES")));
				  if(rs.getString("EXISTING_CUSTOMER")!=null){
					  userDetails.put("existingCustomer", rs.getString("EXISTING_CUSTOMER"));
				  }else{
					  userDetails.put("existingCustomer", "N");
				  }
				  userDetails.put("allowGeneralCatalog", rs.getString("GENERAL_CATALOG_ACCESS")==null?"N":rs.getString("GENERAL_CATALOG_ACCESS"));
				  userDetails.put("logo", rs.getString("LOGO")); 
				  userDetails.put("userLevelAcceptOrderByPO", rs.getString("USER_ACCEPT_ORDER_BY_PO_NUM"));
				  if(parentCompanyId>0)
				  {
					  
					        userDetails.put("buyingCompanyId",rs.getString("PARENT_COMPANY_ID"));
					        
					  if(subsetId==2)
					  {
						  HashMap<String,String> parentDetails = new HashMap<String, String>();
						  parentDetails = getParentCompanyDetail(parentCompanyId);
						  userDetails.put("logo", parentDetails.get("logo")); 
						  userDetails.put("userSubsetId",  parentDetails.get("userSubsetId"));
						  userDetails.put("allowGeneralCatalog", parentDetails.get("allowGeneralCatalog"));
						  
					  }
					  else if(rs.getString("LOGO")==null)
					  {
						  HashMap<String,String> parentDetails = new HashMap<String, String>();
						  parentDetails = getParentCompanyDetail(parentCompanyId);
						  userDetails.put("logo", parentDetails.get("logo")); 
					  }
				  }
			  }
			  
	        
	          ConnectionManager.closeDBPreparedStatement(preStat);

	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	 
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
        CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	      return userDetails;
	}
	public UsersModel getUserStatus(String userName,boolean caseSesitive){
		long startTimer = CommonUtility.startTimeDispaly();
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
	    UsersModel userDetails = new UsersModel();
	    request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
	    
	    try{
	    	conn = ConnectionManager.getDBConnection();
	    	String sql="";
	    	if(caseSesitive){
	    		sql = "SELECT USER_ID,USER_NAME,PASSWORD,STATUS,ECLIPSE_CONTACT_ID FROM CIMM_USERS WHERE USER_NAME = ? AND USER_TYPE='ECOMM' AND SITE_ID=?";
	    	}else{
	    		sql = "SELECT USER_ID,USER_NAME,PASSWORD,STATUS,ECLIPSE_CONTACT_ID FROM CIMM_USERS WHERE UPPER(USER_NAME) = UPPER(?)  AND USER_TYPE='ECOMM' AND SITE_ID=?";
	    	}
	    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SINGLE_USER_UNDER_MULTIPLE_CUSTOMER")).equalsIgnoreCase("Y") && CommonUtility.validateString((String)session.getAttribute("multipleAccountsAvailable")).equalsIgnoreCase("Y") && 
	    			CommonUtility.validateNumber((String)session.getAttribute("selectedAccountId"))>0) {
	    		sql += " AND ECLIPSE_CONTACT_ID = "+CommonUtility.validateNumber((String)session.getAttribute("selectedAccountId"));
	    	}
	    	pstmt = conn.prepareStatement(sql);
	    	pstmt.setString(1, userName);
	    	pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
	    	rs = pstmt.executeQuery();
	    	while(rs.next()){
	    		userDetails = new UsersModel();
	    		userDetails.setUserId(rs.getInt("USER_ID"));
				userDetails.setUserName(rs.getString("USER_NAME"));
				userDetails.setPassword(rs.getString("PASSWORD"));
				userDetails.setStatusDescription(rs.getString("STATUS")); 
				userDetails.setEntityId(CommonUtility.validateParseIntegerToString(rs.getInt("ECLIPSE_CONTACT_ID")));
	    	}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally{
	    	ConnectionManager.closeDBPreparedStatement(pstmt);
	    	ConnectionManager.closeDBConnection(conn);
		}
	    CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	    return userDetails;
		
	}
	public static int updatePasswordWithUserName(String userName,String password)
	{
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
	    int count = 0;
	
		try {
            conn = ConnectionManager.getDBConnection();
            sql = "UPDATE CIMM_USERS SET PASSWORD=? WHERE USER_NAME=? AND SITE_ID=?";
            String securePassword = SecureData.getPunchoutSecurePassword(password);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, securePassword);
            pstmt.setString(2, userName);
            pstmt.setInt(3, CommonDBQuery.getGlobalSiteId());
            count = pstmt.executeUpdate();
           
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	ConnectionManager.closeDBResultSet(rs);
    	ConnectionManager.closeDBPreparedStatement(pstmt);
    	ConnectionManager.closeDBConnection(conn);	
      }
		return count;
		
	}
	public static  HashMap<String,String> getParentCompanyDetail(int parentCompany)
	{
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    HashMap<String,String> userDetails = new HashMap<String,String>();
	 
	     
        
        try {
        	conn = ConnectionManager.getDBConnection();
	          preStat = conn.prepareStatement("SELECT SUBSET_ID,LOGO,GENERAL_CATALOG_ACCESS,DISPLAY_CUST_PART_NUMBER FROM BUYING_COMPANY WHERE BUYING_COMPANY_ID=?");			 
			  preStat.setInt(1, parentCompany);
			
			 
			  rs =preStat.executeQuery();
			  
			  if(rs.next())
			  {
				  userDetails.put("userSubsetId", rs.getString("SUBSET_ID"));
				  userDetails.put("displayCustPartNum", rs.getString("DISPLAY_CUST_PART_NUMBER"));
				  userDetails.put("allowGeneralCatalog", rs.getString("GENERAL_CATALOG_ACCESS")==null?"N":rs.getString("GENERAL_CATALOG_ACCESS"));
				  userDetails.put("logo", rs.getString("LOGO")); 
			  }
			  
	        
	          ConnectionManager.closeDBPreparedStatement(preStat);

	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	    	
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
	      
	      return userDetails;
	}
	public static UsersModel getUserEmail(int userId)
	{
		UsersModel userDetail = null;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
	
		try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("getEmailId");
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
           if(rs.next())
           {
        	   userDetail = new UsersModel();
        	   userDetail.setFirstName(rs.getString("FIRST_NAME"));
        	   userDetail.setLastName(rs.getString("LAST_NAME"));
        	   userDetail.setEmailAddress(rs.getString("EMAIL"));
        	   userDetail.setCustomerName(rs.getString("CUSTOMER_NAME"));
        	   userDetail.setUserName(rs.getString("USER_NAME"));
        	   userDetail.setJobTitle(rs.getString("JOB_TITLE"));
           }
           
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    		ConnectionManager.closeDBResultSet(rs);
    		ConnectionManager.closeDBPreparedStatement(pstmt);
    		ConnectionManager.closeDBConnection(conn);	
      }
		return userDetail;
	}
	public static int updateFirstLogin(int userId)
	{
		int count=0;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
		try {
			conn = ConnectionManager.getDBConnection();
            sql = "UPDATE CIMM_USERS SET FIRST_LOGIN=? WHERE USER_ID=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "N");
            pstmt.setInt(2, userId);
            count = pstmt.executeUpdate();
        } catch (SQLException e) { 
	        e.printStackTrace();
	    }finally{
	    	ConnectionManager.closeDBPreparedStatement(pstmt);
	    	ConnectionManager.closeDBConnection(conn);	
	    }
	    return count;
	}
	public static void updateUserLog(int userId,String action, String sessionId, String userIp, String actionType,String actionKey)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
	    PreparedStatement pstmt=null;

	    String sql = "";
	
		try {
            conn = ConnectionManager.getDBConnection();
            sql = "INSERT into USER_LOG(USER_LOG_ID,USER_ID,ACTION,SESSION_ID,DATETIME,USER_IP_ADDRESS,ACTION_TYPE,ACTION_KEY) VALUES(USER_LOG_ID_SEQ.NEXTVAL,?,?,?,SYSDATE,?,?,?)";
           
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, action);
            pstmt.setString(3, sessionId);
            pstmt.setString(4, userIp);
            pstmt.setString(5, actionType);
            pstmt.setString(6, actionKey);
            
            int count = pstmt.executeUpdate();
           
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	ConnectionManager.closeDBPreparedStatement(pstmt);
    	ConnectionManager.closeDBConnection(conn);	
      }
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	}
	public static String getTaxonomyByActiveMenu(String activeMenu)
	{
		
		
		String sql = "select taxonomy_id from taxonomies where taxonomy_name=?"; 
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		String taxonomyId = "";
		
		
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,activeMenu);
			
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				taxonomyId = rs.getString("taxonomy_id");
               
			}
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return taxonomyId;
		
	}
	public static String getCountryCode(String country)
	{
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
		String eclipseCountryCode = country.toUpperCase();
		if(eclipseCountryCode.equalsIgnoreCase("USA")){
			eclipseCountryCode = "US";
		}
		if(eclipseCountryCode.equalsIgnoreCase("CAN") || eclipseCountryCode.equalsIgnoreCase("CANADA") || eclipseCountryCode.equalsIgnoreCase("CA") ){
			eclipseCountryCode = "CA";
		}
		if(eclipseCountryCode.equalsIgnoreCase("MEX")){
			eclipseCountryCode = "MX";
		}
		try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("getCoutryCodeQuery");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, eclipseCountryCode);
            rs = pstmt.executeQuery();
            if(rs.next())
            {
            	if(rs.getString("COUNTRY_CODE")!=null && !rs.getString("COUNTRY_CODE").trim().equalsIgnoreCase(""))
            		eclipseCountryCode = rs.getString("COUNTRY_CODE");
            }
            else
            {
            	eclipseCountryCode = null;
            }
           
        
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBResultSet(rs);
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		return eclipseCountryCode;
		
	}
	public static String customFieldVal(int userId)
	{
		String fieldVal = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		 
	        try
	        {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = "SELECT FIELD_NAME FROM CUSTOM_FIELDS WHERE CUSTOM_FIELD_ID IN(SELECT CUSTOM_FIELD_ID FROM USER_CUSTOM_FIELD_VALUES WHERE USER_ID=?)";
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setInt(1, userId);
	        	rs = pstmt.executeQuery();
	        	//ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,EMAIL,ENTITY_ID;
	        	if(rs.next())
	        	{
	        		fieldVal = rs.getString("FIELD_NAME");
	        	}
	        }
	        catch(SQLException e)
	        {
	        	e.printStackTrace();
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBResultSet(rs);	
	        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	        	ConnectionManager.closeDBConnection(conn);	
	        }
		
		return fieldVal;
	}
	public static int insertNewAddressintoBCAddressBook(Connection conn,UsersModel userDetailList,String type)
	{
		int addressBookId=0;
		int count = 0;
		
	    PreparedStatement pstmt = null;
	          
	    try
	    {
	    	String phoneNumber = "";
	    	
			if(userDetailList.getContactShortList()!=null && userDetailList.getContactShortList().size() > 0){
				for(AddressModel phone : userDetailList.getContactShortList()){
					if(phone.getPhoneDescription().trim().equalsIgnoreCase("Business")){
						phoneNumber=phone.getPhoneNo();
					}
				}
			}else {
				phoneNumber = userDetailList.getPhoneNo();
			}
			String sql = PropertyAction.SqlContainer.get("insertBCAddressBook");
			addressBookId = CommonDBQuery.getSequenceId("BC_ADDRESS_BOOK_ID_SEQ");
			//ADDRESS_BOOK_ID,USER_ID,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,COUNTRY,PHONE,ADDRESS_TYPE
			String country = userDetailList.getCountry();
			if(country !=null){
				String ecCountry = UsersDAO.getCountryCode(country);
				if(ecCountry!=null && !ecCountry.equalsIgnoreCase(""))
					country = ecCountry;
				else
				country = "US";
			}else{
				country = " ";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, addressBookId);
			pstmt.setString(2, userDetailList.getAddress1());
			pstmt.setString(3, userDetailList.getAddress2());
			pstmt.setString(4, userDetailList.getCity());
			pstmt.setString(5, userDetailList.getState());
			pstmt.setString(6, userDetailList.getZipCode());
			pstmt.setString(7, country);
			pstmt.setString(8, phoneNumber);//pstmt.setString(8, userDetailList.getPhoneNo());
			pstmt.setString(9, type);
			pstmt.setString(10, userDetailList.getEntityId());
			pstmt.setInt(11, userDetailList.getBuyingCompanyId());
			pstmt.setString(12, userDetailList.getFirstName());
			if(userDetailList.getLastName() == null){
				pstmt.setString(13, "");
			}else{
				pstmt.setString(13, userDetailList.getLastName());
			}
			pstmt.setString(14, userDetailList.getShipToId());
			pstmt.setString(15, userDetailList.getEmailAddress());
			pstmt.setString(16, CommonUtility.validateString(userDetailList.getShipToName()));
			pstmt.setString(17, userDetailList.getWareHouseCodeStr());
			count = pstmt.executeUpdate();
			System.out.println("Insert Count"+count);
			if(count<1){
				addressBookId = 0;
			}else{
				conn.commit();
			}
				

	    }
	    catch(SQLException e)
	    {
	    	addressBookId = 0;
	    	e.printStackTrace();
	    }
	    catch(Exception e)
	    {
	    	addressBookId = 0;
	    	e.printStackTrace();
	    }
	    finally
	    {
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	
	    }
	    
		return addressBookId;
	}

	public static String assignRoleToUser(Connection conn,String sessionUserId,String roleName){
		long startTimer = CommonUtility.startTimeDispaly();
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO CIMM_USER_ROLES(USER_ID,ROLE_ID,USER_EDITED,UPDATED_DATETIME) SELECT ? USER_ID,ROLE_ID,? USER_EDITED,SYSDATE FROM CIMM_ROLES WHERE ROLE_NAME=?";
		
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, CommonUtility.validateNumber(sessionUserId));
			pstmt.setInt(2, CommonUtility.validateNumber(sessionUserId));
			pstmt.setString(3, roleName);
			pstmt.executeUpdate();
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return roleName;

	}
	public static LinkedHashMap<String,String> getBuyingCompanyCustomFields(int buyingCompanyID){
		LinkedHashMap<String,String> customField = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		ResultSet rs = null;
	
		try{
			conn = ConnectionManager.getDBConnection();
			sql = PropertyAction.SqlContainer.get("getCustomFieldForBuyingCompany");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,buyingCompanyID);
			rs = pstmt.executeQuery();
			customField = new LinkedHashMap<String, String>();
			while(rs.next())
			{
				customField.put(rs.getString("FIELD_NAME"), rs.getString("TEXT_FIELD_VALUE"));
				
			}
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Closing DB Connection finally at curd");
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		return customField;
	}
	public static String checkAndUpdateRoleToUser(String sessionUserId,String isSuperUser,String userRole){
		long startTimer = CommonUtility.startTimeDispaly();
		String roleName = "";
		boolean updateUserRole = false;
	try
	{
		if(isSuperUser == null)
		{
			roleName = "Ecomm Customer Auth Purchase Agent";
			updateUserRole = true;
		}
		else if(userRole.equalsIgnoreCase("Ecomm Customer Auth Purchase Agent") && isSuperUser!=null && isSuperUser.equalsIgnoreCase("Yes"))
		{
			roleName = "Ecomm Customer Super User";
			updateUserRole = true;
		}
		else if(userRole.equalsIgnoreCase("Ecomm Customer Super User") && isSuperUser!=null && isSuperUser.equalsIgnoreCase("No"))
		{
			roleName = "Ecomm Customer Auth Purchase Agent";
			updateUserRole = true;
		}
		else if(userRole.equalsIgnoreCase("Ecomm Customer General User") && isSuperUser!=null && isSuperUser.equalsIgnoreCase("Yes"))
		{
			roleName = "Ecomm Customer Super User";
			updateUserRole = true;
		}
		else if(userRole.equalsIgnoreCase("Ecomm Auth Technician") && isSuperUser!=null && isSuperUser.equalsIgnoreCase("Yes"))
		{
			roleName = "Ecomm Auth Technician";
			updateUserRole = true;
		}
		else
		{
			roleName = userRole;
			updateUserRole = true;
		}
		
		if(updateUserRole)
		{
			updateRoleToUser(sessionUserId, roleName);
		}
		
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}
	
	CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	return roleName;
	
	}
	public static String updateRoleToUser(String sessionUserId,String roleName){
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE CIMM_USER_ROLES SET ROLE_ID = (SELECT ROLE_ID FROM CIMM_ROLES WHERE ROLE_NAME = ?) WHERE USER_ID = ?";
		// UPDATE CIMM_USER_ROLES SET ROLE_ID = (SELECT ROLE_ID FROM CIMM_ROLES WHERE ROLE_NAME = ?) WHERE USER_ID = ?
		
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, roleName);
			pstmt.setInt(2, CommonUtility.validateNumber(sessionUserId));
			
			pstmt.executeUpdate();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			
		}

		return roleName;

	}
public static int getUserIdFromDB(String userName, String userStatus) {
		
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    int userId=0;	   
	     
     try {
    	 	conn = ConnectionManager.getDBConnection();
	          preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getPasswordForUserName"));			 
			  preStat.setString(1, userName);
			  preStat.setString(2, userStatus);
			  preStat.setInt(3, CommonDBQuery.getGlobalSiteId());
			  rs =preStat.executeQuery();
			  if(rs.next()){											  
				 userId= rs.getInt("USER_ID");				  
			  }
			  ConnectionManager.closeDBPreparedStatement(preStat);
	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	    	 
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);
	    	  ConnectionManager.closeDBResultSet(rs);
	      } // finally
	      
	      return userId;
	}
	public static String getShipBranchName(String wareHouseId)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		String shipBranchId = "Ship Branch Not Set.";
		String sql = "SELECT WAREHOUSE_NAME FROM WAREHOUSE WHERE upper(WAREHOUSE_CODE)=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, wareHouseId.trim().toUpperCase());
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				shipBranchId = rs.getString("WAREHOUSE_NAME");
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return shipBranchId;
	}
	public static String newsContent()
	{
		String staticPagePath = CommonDBQuery.getSystemParamtersList().get("STATICPAGESPATH");
		String newsPageId = CommonDBQuery.getSystemParamtersList().get("NEWSPAGEID");
		String newsPageContent = null;
		try{
			SAXBuilder builder = new SAXBuilder();
			File file = new File(staticPagePath+newsPageId+".xml");
			if(file.exists())
			{
				org.jdom.Document dom = builder.build(file);
				newsPageContent=dom.getRootElement().getChildText("pageContent");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return newsPageContent;
	}
	public static ArrayList<UsersModel> getSuperUserForCompany(String companyName)
	{
		ArrayList<UsersModel> localSet = new ArrayList<UsersModel>();
		String sql = PropertyAction.SqlContainer.get("getSuperUserOfCompany");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		String status = "";
		
		
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,companyName);
			
			
			rs = pstmt.executeQuery();
			
			if(rs!=null && rs.next())
			{
				UsersModel temp = new UsersModel();
				temp.setUserId(rs.getInt("USER_ID"));
				temp.setEmailAddress(rs.getString("EMAIL"));
				temp.setFirstName(rs.getString("FIRST_NAME"));
				temp.setLastName(rs.getString("LAST_NAME"));
				localSet.add(temp);
			}
			
			
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			
		}
		return localSet;
		
	}
	
	public static ArrayList<UsersModel> getSuperUserForAccount(String accountNumber)
	{
		ArrayList<UsersModel> localSet = new ArrayList<UsersModel>();
		String sql = PropertyAction.SqlContainer.get("getSuperUserOfAccount");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,accountNumber);
			rs = pstmt.executeQuery();
			while(rs!=null && rs.next())
			{
				UsersModel temp = new UsersModel();
				temp.setUserId(rs.getInt("USER_ID"));
				temp.setEmailAddress(rs.getString("EMAIL"));
				temp.setFirstName(rs.getString("FIRST_NAME"));
				temp.setLastName(rs.getString("LAST_NAME"));
				localSet.add(temp);
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);}
		return localSet;
	}
	
	public static int insertRegistrationToCustomTable(UsersModel userDetails){
		int shipToId = 0;
		String sql = PropertyAction.SqlContainer.get("insertCustomFormFieldDetails");
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    try {
	    	conn = ConnectionManager.getDBConnection();
	    	/*userDetails.setEntityName(companyName1B);
			 userDetails.setAccountName(accountNo1B);
			 userDetails.setFirstName(firstName1B);
			 userDetails.setLastName(lastName1B);
			 userDetails.setEmailAddress(emailAddress1B);
			 userDetails.setPassword(newPassword1B);
			 userDetails.setRequestAuthorizationLevel(requestAuthorization1B);
			 userDetails.setSalesContact(salesContact1B);
			 userDetails.setAddress1(companyBillingAddress1B);
			 userDetails.setAddress2(suiteNo1B);
			 userDetails.setCity(cityName1B);
			 userDetails.setState(stateName1B);
			 userDetails.setCountry(countryName1B);
			 userDetails.setZipCode(zipCode1B);
			 userDetails.setPhoneNo(phoneNo1B);
			 userDetails.setNewsLetterSub(newsLetterSub1B);*/


	    	preStat = conn.prepareStatement(sql);			 
	    	preStat.setString(1, userDetails.getBillEntityId());

	    } catch (Exception e) {         
	    	e.printStackTrace();
	    }finally{
	    	ConnectionManager.closeDBPreparedStatement(preStat);
	    	ConnectionManager.closeDBConnection(conn);

	    }
		return shipToId;
	}
	
	public static String getShipBranch(String wareHouseId)
	{
		String shipBranchId = "";
		
		//String sql = "SELECT SHIP_BRANCH FROM WAREHOUSE WHERE WAREHOUSE_CODE=?";
		//sql = "select vld.list_value from warehouse_custom_field_values wcfv,loc_custom_field_values lcfv,value_list_data vld,warehouse w where w.warehouse_code=? and wcfv.warehouse_id=w.warehouse_id and lcfv.loc_custom_field_value_id = wcfv.loc_custom_field_value_id and vld.value_data_id=lcfv.numeric_field_value";
		String sql = PropertyAction.SqlContainer.get("getShipBranchId");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, wareHouseId);
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				//shipBranchId = rs.getInt("list_value");
				shipBranchId = rs.getString("FILED_VALUE");
	        }
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			
		}
		
		return shipBranchId;
	}
	
	public static int updateQuoteCartDao(int iQty, String qPno){
		
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("updateQuoteCart");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, iQty);
			pstmt.setInt(2, iQty);
			pstmt.setInt(3, CommonUtility.validateNumber(qPno));
			count = pstmt.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return count;
	}
	
	public static int deleteQuoteCartById(int quoteCartId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try{
			conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("deleteQuoteCart");
        	pstmt = conn.prepareStatement(sql);
        	pstmt.setInt(1, quoteCartId);
        	count = pstmt.executeUpdate();
        }
        catch(Exception e){
         	e.printStackTrace();
        }
        finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return count;
	}
	
	public static ArrayList<ShipVia> getUserShipViaList(ARGetCustomerDataOrderingResponse customerShip)
	{


		
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    String sqlName = "getUserShipViaAdvanced";
	    if(CommonDBQuery.getSystemParamtersList().get("SHIP_VIA_QUERY")!=null && CommonDBQuery.getSystemParamtersList().get("SHIP_VIA_QUERY").trim().length() > 0){
	    	sqlName = CommonDBQuery.getSystemParamtersList().get("SHIP_VIA_QUERY");
	    }
	    String sql = PropertyAction.SqlContainer.get(sqlName);
	    ArrayList<ShipVia> shipViaResponse = new ArrayList<ShipVia>();
        try {
        	
        	
        	if(CommonUtility.validateString(sqlName).equalsIgnoreCase("SHIPVIA_COST_TABLE")){
        		CustomModel customFieldModel = new CustomModel();
        		customFieldModel.setCustomFieldEntityType("WEBSITE");// Type
        		customFieldModel.setCustomFieldEntityId(CommonUtility.validateNumber(CommonDBQuery.systemParametersListVal.get("SITE_ID")));//Site ID
        		customFieldModel.setCustomFieldName(CommonUtility.validateString(sqlName));// Custom Field Table Name
        		
        		shipViaResponse = CustomFieldDAO.getShipViaDetailsFromCustomFieldTable(customFieldModel);
        		
        	}else{
        		if(customerShip!=null)
    			{
    				if(customerShip.getShipVia()!=null && !customerShip.getShipVia().trim().equalsIgnoreCase(""))
    				{
    					 conn = ConnectionManager.getDBConnection();
    			         preStat = conn.prepareStatement(sql);			 
    					 preStat.setString(1, customerShip.getShipVia().toUpperCase());
    					
    						
    					  rs =preStat.executeQuery();
    					  while(rs.next())
    					  {				
    						  	ShipVia shipVia = new ShipVia();
    							shipVia.setShipViaID(rs.getString("SHIP_VIA_CODE"));
    							shipVia.setDescription(rs.getString("SHIP_VIA_NAME"));
    							shipVia.setServiceCode(rs.getInt("SERVICE_CODES"));
    							shipViaResponse.add(shipVia);		  
    					  }
    				}
    			}else{

    					conn = ConnectionManager.getDBConnection();
    					preStat = conn.prepareStatement(sql);			 
    					rs =preStat.executeQuery();
    					while(rs.next()){				
    					  	ShipVia shipVia = new ShipVia();
    						shipVia.setShipViaID(rs.getString("SHIP_VIA_CODE"));
    						shipVia.setDescription(rs.getString("SHIP_VIA_NAME"));
    						shipVia.setServiceCode(rs.getInt("SERVICE_CODES"));
    						shipViaResponse.add(shipVia);		  
    					}
    			
    			}
        	}
        	
 	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	    	 
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
	      
	      return shipViaResponse;
	
	}

	//naveen
	
	@SuppressWarnings("unused")
	public static void updateAssignedShipToApprover(int defaultShipToAddressId, int assginedShipTo, int sentByApproverUserId){
		String sql = PropertyAction.SqlContainer.get("updateAssignedShipToApprover");
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, defaultShipToAddressId);
			pstmt.setInt(2, assginedShipTo);
			pstmt.setInt(3, sentByApproverUserId);
			count = pstmt.executeUpdate();
		}
		catch (Exception e) {
			System.out.println("Error in updating Assigned_Ship_To_Id in SAVED_ITEM_LIST for General User");
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
	}
	
	
	public static int updateCimmUser(List<String> shipListFromErp, int buyingCompanyId, int userId){
		int count = -1;
		Connection  conn = null;
		PreparedStatement pstmt = null;   
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();
			String sql=PropertyAction.SqlContainer.get("getBcAddressBookIdFromShipToId");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, shipListFromErp.get(0));
			pstmt.setInt(2, buyingCompanyId);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				updateDefaultShipTo(userId,rs.getInt("BC_ADDRESS_BOOK_ID"));
			}
		} catch(Exception e) {			
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}

	public static List<String> getAllAssignedShipToApprover(int sentByApproverUserId) {
		String sql = PropertyAction.SqlContainer.get("getAssignedShipToApprover");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		List<String> assignedShipTo = null;
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sentByApproverUserId);
			rs = pstmt.executeQuery();
			assignedShipTo = new ArrayList<String>();
			while(rs.next()) {
				assignedShipTo.add(rs.getString("ASSIGNED_SHIP_TO"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return assignedShipTo;
	}

	//naveen
	public static HashMap<String,Integer> getDefaultAddressId(int userId){
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    HashMap<String, Integer> userAddressId = null;
	    
	    try {
	    	conn = ConnectionManager.getDBConnection();
	    	userAddressId = new HashMap<String, Integer>();
	    	pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getDefaultAddressId"));			 
	    	pstmt.setInt(1, userId);
			rs =pstmt.executeQuery();
			
			while(rs.next()){				
			  userAddressId.put("Bill", rs.getInt("DEFAULT_BILLING_ADDRESS_ID"));
			  userAddressId.put("Ship", rs.getInt("DEFAULT_SHIPPING_ADDRESS_ID"));
			}
			
	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      }
	      finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
		  }
	    CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return userAddressId;
	}
	
	public static HashMap<String,Integer> getDefaultAddressIdForBCAddressBook(String userName){
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
	    ResultSet rs = null;
	    HashMap<String, Integer> userAddressId = null;
	    int buyingCompanyId=CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId"));
	    
	    try {
	    	conn = ConnectionManager.getDBConnection();
	    	userAddressId = new HashMap<String, Integer>();
	    	pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getDefaultAddressIdforBCAddressBook"));			 
	    	pstmt.setString(1, userName);
	    	pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
	    	pstmt.setInt(3, buyingCompanyId);
	    	
			rs = pstmt.executeQuery();
			  while(rs.next()){				
				  userAddressId.put("Bill", rs.getInt("DEFAULT_BILLING_ADDRESS_ID"));
				  userAddressId.put("Ship", rs.getInt("DEFAULT_SHIPPING_ADDRESS_ID"));
			  }
	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      }
	      finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
		  }
	    CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return userAddressId;
	}
	public static HashMap<String, ArrayList<UsersModel>> getAgentAddressListFromBCAddressBook(int userId,String type)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		 ArrayList<UsersModel> billAddressList =null;
		 ArrayList<UsersModel> shipAddressList =null;
		 HashMap<String, ArrayList<UsersModel>> userAddressList = new HashMap<String, ArrayList<UsersModel>>();
		 Connection  conn = null;
		    PreparedStatement preStat=null;
		    ResultSet rs = null;

  
		    
		    try {
		    	conn = ConnectionManager.getDBConnection();
		    	billAddressList = new ArrayList<UsersModel>();
		    	shipAddressList = new ArrayList<UsersModel>();
		    	if(type!=null && type.trim().equalsIgnoreCase("FULL")){
		    		preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getAgentShipAddressFromBCAddressBookFULL"));
		    		System.out.println(" getAgentShipAddressFromBCAddressBookFULL : "+PropertyAction.SqlContainer.get("getAgentShipAddressFromBCAddressBookFULL"));
		    	}else{
		    		preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getAgentShipAddressFromBCAddressBook"));
		    		System.out.println(" getAgentShipAddressFromBCAddressBook : "+PropertyAction.SqlContainer.get("getAgentShipAddressFromBCAddressBook"));
		    	}
		    	System.out.println(" userId : "+userId);		 
		    	preStat.setInt(1,userId);
		    	rs = preStat.executeQuery();
				while(rs.next()){	
					  UsersModel addressListVal = new UsersModel();
					  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
					  addressListVal.setShipToId(rs.getString("SHIP_TO_ID"));
					  addressListVal.setShipToName(rs.getString("SHIP_TO_NAME"));
					  addressListVal.setAddressType(rs.getString("ADDRESS_TYPE"));
					  addressListVal.setAddress1(rs.getString("ADDRESS1"));
					  addressListVal.setFirstName(rs.getString("FIRST_NAME"));
					  addressListVal.setLastName(rs.getString("LAST_NAME"));
					  addressListVal.setEntityId(rs.getString("ENTITY_ID"));
					  addressListVal.setAddress2(rs.getString("ADDRESS2"));
					  addressListVal.setCity(rs.getString("CITY"));
					  addressListVal.setState(rs.getString("STATE"));
					  addressListVal.setZipCodeStringFormat(rs.getString("ZIPCODE"));
					  addressListVal.setCountry(rs.getString("COUNTRY"));
					  addressListVal.setPhoneNo(rs.getString("PHONE"));
					  if(rs.findColumn("SUBSET_ID")>0){
					        addressListVal.setSubsetId(rs.getInt("SUBSET_ID"));
					  }
					  
					  if(rs.getString("CUSTOMER_NAME")!=null && !rs.getString("CUSTOMER_NAME").trim().equalsIgnoreCase("")){
						  addressListVal.setCustomerName(rs.getString("CUSTOMER_NAME"));
					  }else{
						  if(rs.getString("LAST_NAME")!=null && !rs.getString("LAST_NAME").trim().equalsIgnoreCase("")){
							  addressListVal.setCustomerName(rs.getString("FIRST_NAME")+" "+rs.getString("LAST_NAME"));
						  }else{
							  addressListVal.setCustomerName(rs.getString("FIRST_NAME"));
						  }
					  }
					  if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill"))
					  {
						  billAddressList.add(addressListVal);
						  userAddressList.put(rs.getString("ADDRESS_TYPE"), billAddressList);
					  }
					  else
					  {
						 shipAddressList.add(addressListVal);
						 userAddressList.put(rs.getString("ADDRESS_TYPE"), shipAddressList);
					  }
				  }
				 
		          ConnectionManager.closeDBPreparedStatement(preStat);

		      } catch (Exception e) {         
		          e.printStackTrace();
		
		      } finally {	    
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(preStat);
		    	  ConnectionManager.closeDBConnection(conn);	
		      } // finally
		    CommonUtility.endTimeAndDiffrenceDisplay(startTimer);     
		 return userAddressList;
	}
	
	
	public static HashMap<String, ArrayList<UsersModel>> getAddressListFromBCAddressBookEclipse(int buyingCompanyId, int userId){
		
		 ArrayList<UsersModel> billAddressList =null;
		 ArrayList<UsersModel> shipAddressList =null;
		 HashMap<String, ArrayList<UsersModel>> userAddressList = new HashMap<String, ArrayList<UsersModel>>();
		 	
		 Connection  conn = null;
		 PreparedStatement pstmt=null;
		 ResultSet rs = null;
		 int defaultShipToId = 0;
		    
		  	  
		    
		    try {
		    	conn = ConnectionManager.getDBConnection();
		    	billAddressList = new ArrayList<UsersModel>();
		    	shipAddressList = new ArrayList<UsersModel>();
		    	  
		    	boolean assignedList = false;
		    	 System.out.println(PropertyAction.SqlContainer.get("getAssignedAddressFromBCAddressBook"));
		    	 pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAssignedAddressFromBCAddressBook"));
		    	 pstmt.setInt(1, buyingCompanyId);
		    	 pstmt.setInt(2, buyingCompanyId);
		    	 pstmt.setInt(3, userId);
				 rs =pstmt.executeQuery();
				 while(rs.next()){
					 
					  UsersModel addressListVal = new UsersModel();
					  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
					  addressListVal.setAddressType(rs.getString("ADDRESS_TYPE"));
					  addressListVal.setFirstName(rs.getString("FIRST_NAME"));
					  addressListVal.setLastName(rs.getString("LAST_NAME"));
					  addressListVal.setEntityId(rs.getString("ENTITY_ID"));
					  addressListVal.setAddress1(rs.getString("ADDRESS1"));
					  addressListVal.setAddress2(rs.getString("ADDRESS2"));
					  addressListVal.setCity(rs.getString("CITY"));
					  addressListVal.setState(rs.getString("STATE"));
					  addressListVal.setZipCodeStringFormat(rs.getString("ZIPCODE"));
					  addressListVal.setCountry(rs.getString("COUNTRY"));
					  addressListVal.setPhoneNo(rs.getString("PHONE"));
					  addressListVal.setCustomerName(rs.getString("CUSTOMER_NAME"));
					  addressListVal.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
					  addressListVal.setEmailAddress(rs.getString("EMAIL"));
					  if(rs.findColumn("SUBSET_ID")>0){
						  addressListVal.setSubsetId(rs.getInt("SUBSET_ID"));
					  }
					  if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill"))
					  {
						  billAddressList.add(addressListVal);
						  userAddressList.put(rs.getString("ADDRESS_TYPE"), billAddressList);
					  }
					  else
					  {
						 shipAddressList.add(addressListVal);
						 userAddressList.put(rs.getString("ADDRESS_TYPE"), shipAddressList);
					  }
					  
					  /*Removed whl dev I phase
					   * if(rs.getString("SHIP_TO_ID") != null && rs.getString("SHIP_TO_ID").trim().length()>0){
						  addressListVal.setShipToIdSx(rs.getString("SHIP_TO_ID").trim());
					  }else{
						  addressListVal.setShipToIdSx("");
					  }*/
				  }
		    	
				 if(userAddressList!=null && userAddressList.get("Ship")!=null){
					 assignedList =true;
				 }
				 
				 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISPLAY_USER_SHIPTO")).equalsIgnoreCase("Y")){
					 String sql = "SELECT DEFAULT_SHIPPING_ADDRESS_ID FROM CIMM_USERS WHERE USER_ID = ? ";
					 ConnectionManager.closeDBPreparedStatement(pstmt);
					 pstmt = conn.prepareStatement(sql);
					 pstmt.setInt(1, userId);
					 rs =pstmt.executeQuery();
					  while(rs.next()){	
						  defaultShipToId = rs.getInt("DEFAULT_SHIPPING_ADDRESS_ID");
					  }
				 }
				 
				 
				 if(!assignedList){
					 billAddressList = new ArrayList<UsersModel>();
					 shipAddressList = new ArrayList<UsersModel>();
					
					 ConnectionManager.closeDBPreparedStatement(pstmt); 
					 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISPLAY_USER_SHIPTO")).equalsIgnoreCase("Y")){
						 pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getUserAddressListFromBCAddressBookEclipse"));			 
						 pstmt.setInt(1, buyingCompanyId);
						 pstmt.setInt(2, defaultShipToId);
					 }else{
						 userAddressList = new HashMap<String, ArrayList<UsersModel>>();
						 pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBookEclipse"));			 
						 pstmt.setInt(1, buyingCompanyId);
					 }
					 rs =pstmt.executeQuery();
					  while(rs.next()){	
						  UsersModel addressListVal = new UsersModel();
						  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
						  addressListVal.setAddressType(rs.getString("ADDRESS_TYPE"));
						  addressListVal.setAddress1(rs.getString("ADDRESS1"));
						  addressListVal.setFirstName(rs.getString("FIRST_NAME"));
						  addressListVal.setLastName(rs.getString("LAST_NAME"));
						  addressListVal.setEntityId(rs.getString("ENTITY_ID"));
						  addressListVal.setAddress2(rs.getString("ADDRESS2"));
						  addressListVal.setCity(rs.getString("CITY"));
						  addressListVal.setState(rs.getString("STATE"));
						  addressListVal.setZipCodeStringFormat(rs.getString("ZIPCODE"));
						  addressListVal.setCountry(rs.getString("COUNTRY"));
						  addressListVal.setPhoneNo(rs.getString("PHONE"));
						  addressListVal.setCustomerName(rs.getString("CUSTOMER_NAME"));
						  addressListVal.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
						  addressListVal.setEmailAddress(rs.getString("EMAIL"));
						  if(rs.findColumn("SUBSET_ID")>0){
							  addressListVal.setSubsetId(rs.getInt("SUBSET_ID"));
			              }
						  if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill"))
						  {
							  billAddressList.add(addressListVal);
							  userAddressList.put(rs.getString("ADDRESS_TYPE"), billAddressList);
						  }
						  else
						  {
							 shipAddressList.add(addressListVal);
							 userAddressList.put(rs.getString("ADDRESS_TYPE"), shipAddressList);
						  }
						  /*Removed whl dev I phase
						   * if(rs.getString("SHIP_TO_ID") != null && rs.getString("SHIP_TO_ID").trim().length()>0){
							  addressListVal.setShipToIdSx(rs.getString("SHIP_TO_ID").trim());
						  }else{
							  addressListVal.setShipToIdSx("");
						  }*/
					  }
				 }
		      } catch (Exception e) {         
		          e.printStackTrace();
		
		      }
		      finally{
			    	ConnectionManager.closeDBResultSet(rs);
					ConnectionManager.closeDBPreparedStatement(pstmt);
					ConnectionManager.closeDBConnection(conn);
			  }
		 return userAddressList;
	}
	
	public static UsersModel getUserContactInformation(int userId){

		 Connection  conn = null;
		 PreparedStatement pstmt=null;
		 ResultSet rs = null;
		 UsersModel addressListVal = null;   
		    
		   
		    try {
		    	conn = ConnectionManager.getDBConnection();
		    	 pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getUserContactInformation"));
		    	 pstmt.setInt(1, userId);
				 rs =pstmt.executeQuery();
				 while(rs.next()){
					  //FIRST_NAME,LAST_NAME,MIDDLE_NAME,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,OFFICE_PHONE,CELL_PHONE,FAX,EMAIL,ECLIPSE_CONTACT_ID
					  addressListVal = new UsersModel();
					  addressListVal.setFirstName(rs.getString("FIRST_NAME"));
					  addressListVal.setLastName(rs.getString("LAST_NAME"));
					  addressListVal.setEntityId(rs.getString("ECLIPSE_CONTACT_ID"));
					  addressListVal.setAddress1(rs.getString("ADDRESS1"));
					  addressListVal.setAddress2(rs.getString("ADDRESS2"));
					  addressListVal.setCity(rs.getString("CITY"));
					  addressListVal.setState(rs.getString("STATE"));
					  addressListVal.setZipCodeStringFormat(rs.getString("ZIP"));
					  addressListVal.setCountry(rs.getString("COUNTRY"));
					  addressListVal.setPhoneNo(rs.getString("OFFICE_PHONE"));
					  addressListVal.setEmailAddress(rs.getString("EMAIL")!=null?rs.getString("EMAIL"):"");
				  }
		    	
		      } catch (Exception e) {         
		          e.printStackTrace();
		
		      }
		      finally{
			    	ConnectionManager.closeDBResultSet(rs);
					ConnectionManager.closeDBPreparedStatement(pstmt);
					ConnectionManager.closeDBConnection(conn);
			  }
		 return addressListVal;
	}
	
	public static HashMap<String, ArrayList<UsersModel>> getAddressListFromBCAddressBookDefault(int buyingCompanyId, int userId){
		long startTimer = CommonUtility.startTimeDispaly();
		 ArrayList<UsersModel> billAddressList =null;
		 ArrayList<UsersModel> shipAddressList =null;
		 ArrayList<UsersModel> jobList=null;
		 HashMap<String, ArrayList<UsersModel>> userAddressList = new HashMap<String, ArrayList<UsersModel>>();
		 Connection  conn = null;
		 PreparedStatement pstmt=null;
		 ResultSet rs = null;
		    try {
		    	conn = ConnectionManager.getDBConnection();
		    	billAddressList = new ArrayList<UsersModel>();
		    	shipAddressList = new ArrayList<UsersModel>();
		    	jobList = new ArrayList<UsersModel>(); 
		    	boolean assignedList = false;
		    	 pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAssignedAddressFromBCAddressBook"));
		    	 pstmt.setInt(1, buyingCompanyId);
		    	 pstmt.setInt(2, buyingCompanyId);
		    	 pstmt.setInt(3, userId);
				 rs =pstmt.executeQuery();
				 while(rs.next()){
					  UsersModel addressListVal = new UsersModel();
					  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
					  addressListVal.setAddressType(CommonUtility.validateString(rs.getString("ADDRESS_TYPE")));
					  addressListVal.setFirstName(CommonUtility.validateString(rs.getString("FIRST_NAME")));
					  addressListVal.setLastName(CommonUtility.validateString(rs.getString("LAST_NAME")));
					  addressListVal.setEntityId(CommonUtility.validateString(rs.getString("ENTITY_ID")));
					  addressListVal.setAddress1(CommonUtility.validateString(rs.getString("ADDRESS1")));
					  addressListVal.setAddress2(CommonUtility.validateString(rs.getString("ADDRESS2")));
					  addressListVal.setCity(CommonUtility.validateString(rs.getString("CITY")));
					  addressListVal.setState(CommonUtility.validateString(rs.getString("STATE")));
					  addressListVal.setZipCodeStringFormat(CommonUtility.validateString(rs.getString("ZIPCODE")));
					  addressListVal.setZipCode(CommonUtility.validateString(rs.getString("ZIPCODE")));
					  addressListVal.setCountry(CommonUtility.validateString(rs.getString("COUNTRY")));
					  addressListVal.setPhoneNo(CommonUtility.validateString(rs.getString("PHONE")));
					  addressListVal.setCustomerName(CommonUtility.validateString(rs.getString("CUSTOMER_NAME")));
					  addressListVal.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
					  addressListVal.setShipToId(CommonUtility.validateString(rs.getString("SHIP_TO_ID")));
					  addressListVal.setEmailAddress(rs.getString("EMAIL")!=null?CommonUtility.validateString(rs.getString("EMAIL")):"");
					  if(rs.findColumn("SUBSET_ID")>0){
					        addressListVal.setSubsetId(rs.getInt("SUBSET_ID"));
					  }
					  if(CommonUtility.validateString(rs.getString("ADDRESS_TYPE")).equalsIgnoreCase("Bill"))
					  {
						  billAddressList.add(addressListVal);
						  userAddressList.put(rs.getString("ADDRESS_TYPE"), billAddressList);
					  }
					  else if(CommonUtility.validateString(rs.getString("ADDRESS_TYPE")).trim().equalsIgnoreCase("Ship"))
					  {
						 shipAddressList.add(addressListVal);
						 userAddressList.put(rs.getString("ADDRESS_TYPE"), shipAddressList);
					  }
					  else
					  {
						  		jobList.add(addressListVal);
							 userAddressList.put(rs.getString("ADDRESS_TYPE"), jobList);
					  }
				  }
		    	
				 if(userAddressList!=null && userAddressList.get("Ship")!=null){
					 assignedList =true;
				 }
				 
				 if(!assignedList){
					 ConnectionManager.closeDBResultSet(rs);
					 billAddressList = new ArrayList<UsersModel>();
					 shipAddressList = new ArrayList<UsersModel>();
					 jobList = new ArrayList<UsersModel>();
					 userAddressList = new HashMap<String, ArrayList<UsersModel>>();
					 ConnectionManager.closeDBPreparedStatement(pstmt);
					 System.out.println(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBookDefault"));
			         pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBookDefault"));			 
					 pstmt.setInt(1, buyingCompanyId);
					 rs =pstmt.executeQuery();
					  while(rs.next()){	
						  UsersModel addressListVal = new UsersModel();
						  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
						  addressListVal.setAddressType(CommonUtility.validateString(rs.getString("ADDRESS_TYPE")));
						  addressListVal.setAddress1(CommonUtility.validateString(rs.getString("ADDRESS1")));
						  addressListVal.setFirstName(CommonUtility.validateString(rs.getString("FIRST_NAME")));
						  addressListVal.setLastName(CommonUtility.validateString(rs.getString("LAST_NAME")));
						  addressListVal.setEntityId(CommonUtility.validateString(rs.getString("ENTITY_ID")));
						  addressListVal.setAddress2(CommonUtility.validateString(rs.getString("ADDRESS2")));
						  addressListVal.setCity(CommonUtility.validateString(rs.getString("CITY")));
						  addressListVal.setState(CommonUtility.validateString(rs.getString("STATE")));
						  addressListVal.setZipCodeStringFormat(CommonUtility.validateString(rs.getString("ZIPCODE")));
						  addressListVal.setZipCode(CommonUtility.validateString(rs.getString("ZIPCODE")));
						  addressListVal.setCountry(CommonUtility.validateString(rs.getString("COUNTRY")));
						  addressListVal.setPhoneNo(CommonUtility.validateString(rs.getString("PHONE")));
						  addressListVal.setCustomerName(CommonUtility.validateString(rs.getString("CUSTOMER_NAME")));
						  addressListVal.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
						  addressListVal.setEmailAddress(rs.getString("EMAIL")!=null?CommonUtility.validateString(rs.getString("EMAIL")):"");
						  addressListVal.setShipToId(CommonUtility.validateString(rs.getString("SHIP_TO_ID")));
						  addressListVal.setWareHouseCodeStr(CommonUtility.validateString(rs.getString("WAREHOUSE_CODE")));
						 /* if(CommonUtility.customServiceUtility() != null) {
							  CommonUtility.customServiceUtility().getBCAddressBookCustomFieldDetails(addressListVal);//Electrozad Custom Service
						  }*/
						  if(CommonUtility.validateString(rs.getString("ADDRESS_TYPE")).equalsIgnoreCase("Bill"))
						  {
							  billAddressList.add(addressListVal);
							  userAddressList.put(rs.getString("ADDRESS_TYPE"), billAddressList);
						  }
						  else if(CommonUtility.validateString(rs.getString("ADDRESS_TYPE")).trim().equalsIgnoreCase("Ship"))
						  {
							 shipAddressList.add(addressListVal);
							 userAddressList.put(rs.getString("ADDRESS_TYPE"), shipAddressList);
						  }
						  else
						  {
							  		jobList.add(addressListVal);
								 userAddressList.put(rs.getString("ADDRESS_TYPE"), jobList);
						  }
					  }
				 }
		      } catch (Exception e) {         
		          e.printStackTrace();
		
		      }
		      finally{
			    	ConnectionManager.closeDBResultSet(rs);
					ConnectionManager.closeDBPreparedStatement(pstmt);
					ConnectionManager.closeDBConnection(conn);
			  }
	     CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		 return userAddressList;
	}
	
	public static HashMap<String, ArrayList<UsersModel>> getAddressListFromBCAddressBook(int buyingCompanyId, int userId){
		
		 ArrayList<UsersModel> billAddressList =null;
		 ArrayList<UsersModel> shipAddressList =null;
		 ArrayList<UsersModel> jobList=null;
		 HashMap<String, ArrayList<UsersModel>> userAddressList = new HashMap<String, ArrayList<UsersModel>>();
		 	
		 Connection  conn = null;
		 PreparedStatement pstmt=null;
		 ResultSet rs = null;
		   
		    
		   
		    try {
		    	conn = ConnectionManager.getDBConnection();
		    	billAddressList = new ArrayList<UsersModel>();
		    	shipAddressList = new ArrayList<UsersModel>();
		    	jobList = new ArrayList<UsersModel>();
		    	boolean assignedList = false;
		    	 pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAssignedAddressFromBCAddressBook"));
		    	 pstmt.setInt(1, buyingCompanyId);
		    	 pstmt.setInt(2, buyingCompanyId);
		    	 pstmt.setInt(3, userId);
				 rs =pstmt.executeQuery();
				 while(rs.next()){
					  UsersModel addressListVal = new UsersModel();
					  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
					  addressListVal.setAddressType(rs.getString("ADDRESS_TYPE"));
					  addressListVal.setFirstName(rs.getString("FIRST_NAME"));
					  addressListVal.setLastName(rs.getString("LAST_NAME"));
					  addressListVal.setEntityId(rs.getString("ENTITY_ID"));
					  addressListVal.setAddress1(rs.getString("ADDRESS1"));
					  addressListVal.setAddress2(rs.getString("ADDRESS2"));
					  addressListVal.setCity(rs.getString("CITY"));
					  addressListVal.setState(rs.getString("STATE"));
					  addressListVal.setZipCodeStringFormat(rs.getString("ZIPCODE"));
					  addressListVal.setZipCode(rs.getString("ZIPCODE"));
					  addressListVal.setCountry(rs.getString("COUNTRY"));
					  addressListVal.setPhoneNo(rs.getString("PHONE"));
					  addressListVal.setCustomerName(rs.getString("CUSTOMER_NAME"));
					  addressListVal.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
					  addressListVal.setEmailAddress(rs.getString("EMAIL")!=null?rs.getString("EMAIL"):"");
					  if(rs.findColumn("SUBSET_ID")>0){
					        addressListVal.setSubsetId(rs.getInt("SUBSET_ID"));
					  }
					  if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill"))
					  {
						  billAddressList.add(addressListVal);
						  userAddressList.put(rs.getString("ADDRESS_TYPE"), billAddressList);
					  }
					  else if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Ship"))
					  {
						 shipAddressList.add(addressListVal);
						 userAddressList.put(rs.getString("ADDRESS_TYPE"), shipAddressList);
					  }
					  else
		  				{
		  					jobList.add(addressListVal);
		  					userAddressList.put(rs.getString("ADDRESS_TYPE"), jobList);
		  				}
					  
					  //Removed whl dev I phase
					  if(rs.getString("SHIP_TO_ID") != null && rs.getString("SHIP_TO_ID").trim().length()>0){
						  addressListVal.setShipToId(rs.getString("SHIP_TO_ID").trim());
					  }else{
						  addressListVal.setShipToId("");
					  }
				  }
		    	
				 if(userAddressList!=null && userAddressList.get("Ship")!=null){
					 assignedList =true;
				 }
				 
				 if(!assignedList){
					 billAddressList = new ArrayList<UsersModel>();
					 shipAddressList = new ArrayList<UsersModel>();
					 jobList = new ArrayList<UsersModel>();
					 userAddressList = new HashMap<String, ArrayList<UsersModel>>();
					 ConnectionManager.closeDBPreparedStatement(pstmt);
					 System.out.println(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBook"));
			         pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBook"));			 
					 pstmt.setInt(1, buyingCompanyId);
					 rs =pstmt.executeQuery();
					  while(rs.next()){	
						  UsersModel addressListVal = new UsersModel();
						  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
						  addressListVal.setAddressType(rs.getString("ADDRESS_TYPE"));
						  addressListVal.setAddress1(rs.getString("ADDRESS1"));
						  addressListVal.setFirstName(rs.getString("FIRST_NAME"));
						  addressListVal.setLastName(rs.getString("LAST_NAME"));
						  addressListVal.setEntityId(rs.getString("ENTITY_ID"));
						  addressListVal.setAddress2(rs.getString("ADDRESS2"));
						  addressListVal.setCity(rs.getString("CITY"));
						  addressListVal.setState(rs.getString("STATE"));
						  addressListVal.setZipCodeStringFormat(rs.getString("ZIPCODE"));
						  addressListVal.setZipCode(rs.getString("ZIPCODE"));
						  addressListVal.setCountry(rs.getString("COUNTRY"));
						  addressListVal.setPhoneNo(rs.getString("PHONE"));
						  addressListVal.setCustomerName(rs.getString("CUSTOMER_NAME"));
						  addressListVal.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
						  addressListVal.setEmailAddress(rs.getString("EMAIL")!=null?rs.getString("EMAIL"):"");
						  addressListVal.setWareHouseCodeStr(rs.getString("WAREHOUSE_CODE")!=null?rs.getString("WAREHOUSE_CODE"):"");
						  if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill"))
						  {
							  billAddressList.add(addressListVal);
							  userAddressList.put(rs.getString("ADDRESS_TYPE"), billAddressList);
						  }
						  else if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Ship"))
						  {
							 shipAddressList.add(addressListVal);
							 userAddressList.put(rs.getString("ADDRESS_TYPE"), shipAddressList);
						  }
						  else
			  				{
			  					jobList.add(addressListVal);
			  					userAddressList.put(rs.getString("ADDRESS_TYPE"), jobList);
			  				}
						  //Removed whl dev I phase
						  if(rs.getString("SHIP_TO_ID") != null && rs.getString("SHIP_TO_ID").trim().length()>0){
							  addressListVal.setShipToId(rs.getString("SHIP_TO_ID").trim());
						  }else{
							  addressListVal.setShipToId("");
						  }
					  }
				 }
		      } catch (Exception e) {         
		          e.printStackTrace();
		
		      }
		      finally{
			    	ConnectionManager.closeDBResultSet(rs);
					ConnectionManager.closeDBPreparedStatement(pstmt);
					ConnectionManager.closeDBConnection(conn);
			  }
		 return userAddressList;
	}
	public static HashMap<String, ArrayList<UsersModel>> getAddressListFromBCAddressBookJDW(int buyingCompanyId, int userId){
		
		 ArrayList<UsersModel> billAddressList =null;
		 ArrayList<UsersModel> shipAddressList =null;
		 HashMap<String, ArrayList<UsersModel>> userAddressList = new HashMap<String, ArrayList<UsersModel>>();
		 	
		 Connection  conn = null;
		 PreparedStatement pstmt=null;
		 ResultSet rs = null;
		   
		    
		   
		    try {
		    	conn = ConnectionManager.getDBConnection();
		    	billAddressList = new ArrayList<UsersModel>();
		    	shipAddressList = new ArrayList<UsersModel>();
		    	  
		    	boolean assignedList = false;
		    	 pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAssignedAddressFromBCAddressBook"));
		    	 pstmt.setInt(1, buyingCompanyId);
		    	 pstmt.setInt(2, buyingCompanyId);
		    	 pstmt.setInt(3, userId);
				 rs =pstmt.executeQuery();
				 while(rs.next()){
					  UsersModel addressListVal = new UsersModel();
					  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
					  addressListVal.setAddressType(rs.getString("ADDRESS_TYPE"));
					  addressListVal.setFirstName(rs.getString("FIRST_NAME"));
					  addressListVal.setLastName(rs.getString("LAST_NAME"));
					  addressListVal.setEntityId(rs.getString("ENTITY_ID"));
					  addressListVal.setAddress1(rs.getString("ADDRESS1"));
					  addressListVal.setAddress2(rs.getString("ADDRESS2"));
					  addressListVal.setCity(rs.getString("CITY"));
					  addressListVal.setState(rs.getString("STATE"));
					  addressListVal.setZipCodeStringFormat(rs.getString("ZIPCODE"));
					  addressListVal.setCountry(rs.getString("COUNTRY"));
					  addressListVal.setPhoneNo(rs.getString("PHONE"));
					  addressListVal.setCustomerName(rs.getString("CUSTOMER_NAME"));
					  addressListVal.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
					  addressListVal.setEmailAddress(rs.getString("EMAIL")!=null?rs.getString("EMAIL"):"");
					  if(rs.findColumn("SUBSET_ID")>0){
					      addressListVal.setSubsetId(rs.getInt("SUBSET_ID"));
					  }
					  if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill"))
					  {
						  billAddressList.add(addressListVal);
						  userAddressList.put(rs.getString("ADDRESS_TYPE"), billAddressList);
					  }
					  else
					  {
						 shipAddressList.add(addressListVal);
						 userAddressList.put(rs.getString("ADDRESS_TYPE"), shipAddressList);
					  }
					  
					  /*Removed whl dev I phase
					   * if(rs.getString("SHIP_TO_ID") != null && rs.getString("SHIP_TO_ID").trim().length()>0){
						  addressListVal.setShipToIdSx(rs.getString("SHIP_TO_ID").trim());
					  }else{
						  addressListVal.setShipToIdSx("");
					  }*/
				  }
		    	
				 if(userAddressList!=null && userAddressList.get("Ship")!=null){
					 assignedList =true;
				 }
				 
				 if(!assignedList){
					 billAddressList = new ArrayList<UsersModel>();
					 shipAddressList = new ArrayList<UsersModel>();
					 userAddressList = new HashMap<String, ArrayList<UsersModel>>();
					 ConnectionManager.closeDBPreparedStatement(pstmt);
					 //System.out.println(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBookJDW"));
			         //pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBookJDW"));
					 
					System.out.println(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBook"));
			        pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBook"));
					 pstmt.setInt(1, buyingCompanyId);
					 rs =pstmt.executeQuery();
					  while(rs.next()){	
						  UsersModel addressListVal = new UsersModel();
						  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
						  addressListVal.setAddressType(rs.getString("ADDRESS_TYPE"));
						  addressListVal.setAddress1(rs.getString("ADDRESS1"));
						  addressListVal.setFirstName(rs.getString("FIRST_NAME"));
						  addressListVal.setLastName(rs.getString("LAST_NAME"));
						  addressListVal.setEntityId(rs.getString("ENTITY_ID"));
						  addressListVal.setAddress2(rs.getString("ADDRESS2"));
						  addressListVal.setCity(rs.getString("CITY"));
						  addressListVal.setState(rs.getString("STATE"));
						  addressListVal.setZipCodeStringFormat(rs.getString("ZIPCODE"));
						  addressListVal.setCountry(rs.getString("COUNTRY"));
						  addressListVal.setPhoneNo(rs.getString("PHONE"));
						  addressListVal.setCustomerName(rs.getString("CUSTOMER_NAME"));
						  addressListVal.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
						  addressListVal.setEmailAddress(rs.getString("EMAIL")!=null?rs.getString("EMAIL"):"");
						  if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill"))
						  {
							  billAddressList.add(addressListVal);
							  userAddressList.put(rs.getString("ADDRESS_TYPE"), billAddressList);
						  }
						  else
						  {
							 shipAddressList.add(addressListVal);
							 userAddressList.put(rs.getString("ADDRESS_TYPE"), shipAddressList);
						  }
						  /*Removed whl dev I phase
						   * if(rs.getString("SHIP_TO_ID") != null && rs.getString("SHIP_TO_ID").trim().length()>0){
							  addressListVal.setShipToIdSx(rs.getString("SHIP_TO_ID").trim());
						  }else{
							  addressListVal.setShipToIdSx("");
						  }*/
					  }
				 }
		      } catch (Exception e) {         
		          e.printStackTrace();
		
		      }
		      finally{
			    	ConnectionManager.closeDBResultSet(rs);
					ConnectionManager.closeDBPreparedStatement(pstmt);
					ConnectionManager.closeDBConnection(conn);
			  }
		 return userAddressList;
	}
	public static HashMap<String, UsersModel> getUserAddressFromBCAddressBookEclipse(int billId,int shipId){
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    HashMap<String, UsersModel> userAddress = null;
	   
        try {
        	conn = ConnectionManager.getDBConnection();
        	  userAddress = new HashMap<String, UsersModel>();
	          pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAddressByIdFromBCAddressBookEclipse"));			 
			  pstmt.setInt(1, billId);
			  pstmt.setInt(2, shipId);
			  rs =pstmt.executeQuery();
			  while(rs.next())
			  {				
				  String customerName = CommonUtility.validateString(rs.getString("CUSTOMER_NAME"));
				  if(CommonUtility.validateString(customerName).length() < 1){
					  customerName = CommonUtility.validateString(rs.getString("FIRST_NAME"));
					  if(CommonUtility.validateString(rs.getString("LAST_NAME")).length()>0){
						  customerName = customerName+" "+CommonUtility.validateString(rs.getString("LAST_NAME"));
					  } 
				  }
				  UsersModel userDefaultAddress = new UsersModel();
				  userDefaultAddress.setFirstName(rs.getString("FIRST_NAME"));
				  userDefaultAddress.setLastName(rs.getString("LAST_NAME"));
				  userDefaultAddress.setCustomerName(customerName);
				  userDefaultAddress.setEntityId(rs.getString("ENTITY_ID"));
				  userDefaultAddress.setAddress1(rs.getString("ADDRESS1"));
				  userDefaultAddress.setAddress2(rs.getString("ADDRESS2"));
				  userDefaultAddress.setCity(rs.getString("CITY"));
				  userDefaultAddress.setState(rs.getString("STATE"));
				  userDefaultAddress.setZipCodeStringFormat(rs.getString("ZIPCODE"));
				  userDefaultAddress.setCountry(rs.getString("COUNTRY"));
				  userDefaultAddress.setPhoneNo(rs.getString("PHONE"));
				  userDefaultAddress.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
				  userDefaultAddress.setEmailAddress(CommonUtility.validateString(rs.getString("EMAIL")));
				  userAddress.put(rs.getString("ADDRESS_TYPE"), userDefaultAddress);
			  }
	      } catch (Exception e) {         
	          e.printStackTrace();
	      }
	      finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
		  }
        CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return userAddress;
	}
	
	public static HashMap<String, UsersModel> getUserAddressFromBCAddressBook(int billId,int shipId){
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		HashMap<String, UsersModel> userAddress = null;

		try {
			conn = ConnectionManager.getDBConnection();
			userAddress = new HashMap<String, UsersModel>();
			pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAddressByIdFromBCAddressBook"));			 

			pstmt.setInt(1, billId);
			pstmt.setInt(2, shipId);
			rs =pstmt.executeQuery();
			while(rs.next())
			{				
				UsersModel userDefaultAddress = new UsersModel();
				userDefaultAddress.setFirstName(rs.getString("FIRST_NAME"));
				userDefaultAddress.setLastName(rs.getString("LAST_NAME"));
				userDefaultAddress.setCustomerName(rs.getString("CUSTOMER_NAME"));
				userDefaultAddress.setEntityId(rs.getString("ENTITY_ID"));
				userDefaultAddress.setAddress1(rs.getString("ADDRESS1"));
				userDefaultAddress.setAddress2(rs.getString("ADDRESS2"));
				userDefaultAddress.setCity(rs.getString("CITY"));
				userDefaultAddress.setState(rs.getString("STATE"));
				userDefaultAddress.setZipCodeStringFormat(rs.getString("ZIPCODE"));
				userDefaultAddress.setZipCode(rs.getString("ZIPCODE"));
				userDefaultAddress.setCountry(CommonUtility.validateString(rs.getString("COUNTRY")).length()>0?CommonUtility.validateString(rs.getString("COUNTRY")):"US");
				userDefaultAddress.setPhoneNo(rs.getString("PHONE"));
				userDefaultAddress.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
				userDefaultAddress.setShipToId(CommonUtility.validateString(rs.getString("SHIP_TO_ID")));
				userDefaultAddress.setEmailAddress(CommonUtility.validateString(rs.getString("EMAIL")));
				userDefaultAddress.setWareHouseCodeStr(rs.getString("WAREHOUSE_CODE")!=null?rs.getString("WAREHOUSE_CODE"):"");
				String shipToName = CommonUtility.validateString(rs.getString("FIRST_NAME"));
				if(CommonUtility.validateString(rs.getString("LAST_NAME")).length() > 0){
					shipToName = shipToName + CommonUtility.validateString(rs.getString("LAST_NAME"));
				}
				userDefaultAddress.setShipToName(shipToName);
				userDefaultAddress.setAddressType(rs.getString("ADDRESS_TYPE"));
				if(CommonUtility.customServiceUtility()!=null) {//String  firstOrdermail =
					CommonUtility.customServiceUtility().setDefaultAddress(rs.getString("SHIP_TO_NAME"),rs.getString("ADDRESS_TYPE"), userDefaultAddress);
				}
				userAddress.put(rs.getString("ADDRESS_TYPE"), userDefaultAddress);
			}
		} catch (Exception e) {         
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return userAddress;
	}
	
	public static String getAcceptPo(int userId){
		
		String acceptPo = "Y";
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    
	  
        try {
        	conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("getAcceptPo");
	          pstmt = conn.prepareStatement(sql);			 
			  pstmt.setInt(1, userId);
			  rs =pstmt.executeQuery();
			 
			  if(rs.next()){		
				  String uAllowPo = rs.getString("CU_ACCEPT_ORDER_BY_PO_NUM");
				  String bAllowPo = rs.getString("BC_ACCEPT_ORDER_BY_PO_NUM");
				  
				  if(bAllowPo.trim().equalsIgnoreCase("Y") && uAllowPo.trim().equalsIgnoreCase("N")){
					  acceptPo = "N";
				  }else{
					  acceptPo = bAllowPo.trim();
				  }
			  }

	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      }
	      finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
		  }
		return acceptPo;
	}
	
	public static String getNewsLetterSubScripitonStatusCustomField(String userId){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		String status = "";
		
	
		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getNewsLetterSubScripitonStatusCustomField");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
                status = rs.getString("TEXT_FIELD_VALUE");
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return status;
	}
	
	public static boolean isRegisteredUser(String email){

		boolean isUser= false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
        try{
        	conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getIsRegisteredUser");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(email).toUpperCase());
			pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				isUser = true;
			}
			
        }catch(SQLException e){
        	isUser =true;
        	e.printStackTrace();
        }
        catch(Exception e){
        	isUser =true;
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
        }
		
		return isUser;
	
	}
	
	public static boolean isRegisteredInactiveUser(String email){

		boolean isUser= false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
        try{
        	conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getIsRegisteredInactiveUser");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(email).toUpperCase());
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				isUser = true;
			}
			
        }catch(SQLException e){
        	isUser =true;
        	e.printStackTrace();
        }
        catch(Exception e){
        	isUser =true;
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
        }
		
		return isUser;
	
	}
	
	public static int insertNewAddressintoBCAddressBook(Connection conn, UsersModel userDetailList,String type,boolean connCommit){
		int addressBookId=0;
		int count = 0;
	    PreparedStatement pstmt = null;
       try
        {

        	String sql = PropertyAction.SqlContainer.get("insertBCAddressBook");
			addressBookId = CommonDBQuery.getSequenceId("BC_ADDRESS_BOOK_ID_SEQ");
			//ADDRESS_BOOK_ID,USER_ID,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,COUNTRY,PHONE,ADDRESS_TYPE
			String country = userDetailList.getCountry();
			if(CommonUtility.validateString(country).length()>0){
				String ecCountry = getCountryCode(country);
				if(CommonUtility.validateString(ecCountry).length()>0){
					country = ecCountry;
				}else{
					country = " ";
				}
			}else{
				country = " ";
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, addressBookId);
			pstmt.setString(2, (userDetailList.getAddress1()==null || userDetailList.getAddress1().trim().equalsIgnoreCase(""))?"No Address":userDetailList.getAddress1());
			pstmt.setString(3, userDetailList.getAddress2());
			pstmt.setString(4, userDetailList.getCity());
			pstmt.setString(5, userDetailList.getState());
			pstmt.setString(6, userDetailList.getZipCode());
			pstmt.setString(7, country);
			pstmt.setString(8, userDetailList.getPhoneNo());
			pstmt.setString(9, type);
			pstmt.setString(10, userDetailList.getEntityId());
			pstmt.setInt(11, userDetailList.getBuyingCompanyId());
			pstmt.setString(12, userDetailList.getFirstName());
			pstmt.setString(13, userDetailList.getLastName());
			pstmt.setString(14, userDetailList.getShipToId());
			pstmt.setString(15, userDetailList.getEmailAddress());
			pstmt.setString(16, userDetailList.getShipToName());
			pstmt.setString(17, userDetailList.getWareHouseCodeStr());
			count = pstmt.executeUpdate();
			
			if(count<1){
				addressBookId = 0;
			}else{
				if(connCommit)
				conn.commit();
			}

        }
        catch(SQLException e)
        {
        	addressBookId = 0;
        	e.printStackTrace();
        }
        catch(Exception e)
        {
        	addressBookId = 0;
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        }
        
		return addressBookId;
	}
	public static ArrayList<Integer> getApprovalUserList(int userId)
	{
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = PropertyAction.SqlContainer.get("getApprovalUserList");
		ArrayList<Integer> approverList = new ArrayList<Integer>();
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				approverList.add(rs.getInt("APPROVER_ID"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
			
		}
	return approverList;
		
	}
	
	
	public static ArrayList<UsersModel> getAgentDetailsForAPA(String buyingCompanyId,int userId,String AuthSet){
		ArrayList<UsersModel> agentUserList = new ArrayList<UsersModel>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Integer> approverList = UsersDAO.getApprovalUserList(userId);
		String sql = PropertyAction.SqlContainer.get("getAgentDetailsForAPA");
		try{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, CommonUtility.validateNumber(buyingCompanyId)); 
			rs=pstmt.executeQuery();
		
			while(rs.next()){
				//USER_ID,USER_NAME,FIRST_NAME,LAST_NAME,EMAIL,ADDRESS1,CITY,STATE,ZIP,COUNTRY
				if(rs.getInt("USER_ID")!=userId)
				{
					UsersModel temp = new UsersModel();
					temp.setUserId(rs.getInt("USER_ID"));
					temp.setUserName(rs.getString("USER_NAME"));
					temp.setFirstName(rs.getString("FIRST_NAME"));
					temp.setLastName(rs.getString("LAST_NAME")); 
					temp.setEmailAddress(rs.getString("EMAIL"));
					temp.setPhoneNo(rs.getString("CELL_PHONE"));
					String roleName = rs.getString("ROLE_NAME");
					temp.setApproverEmail(rs.getString("APPROVER_EMAIL"));
					if(roleName!=null && roleName.trim().equalsIgnoreCase("Ecomm Customer Super User"))
					{
						temp.setIsSuperUser("Y");
					}
					else if(roleName!=null && roleName.trim().equalsIgnoreCase("Ecomm Customer Auth Purchase Agent"))
					{
						temp.setIsAuthPurchaseAgent("Y");
					}
					if(approverList!=null && approverList.size()>0)
					{
						if(approverList.contains(temp.getUserId()))
						{
							temp.setApproverAgentAssignStatus("Y");
							//AuthSet = Integer.toString(userId); Check once
						}
					}
					agentUserList.add(temp);
				}
			}
	} catch (Exception e) {
		
		e.printStackTrace();
	}
	finally{
	  ConnectionManager.closeDBResultSet(rs);
	  ConnectionManager.closeDBPreparedStatement(pstmt);	
	  ConnectionManager.closeDBConnection(conn);
	}
	return agentUserList;
	}
	public static List<String> getAllUserRoles()
	{
		String sql = PropertyAction.SqlContainer.get("getAllUserRoles");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		List<String> roleNameList = null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			roleNameList=new ArrayList<String>();
			 while(rs.next())
	        {
				roleNameList.add(rs.getString("ROLE_NAME"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			
		}
		return roleNameList;
	}
	public static ArrayList<Integer> getAssignedShipToList(String userId)
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Integer> addressIdList = new ArrayList<Integer>();
		String sql = PropertyAction.SqlContainer.get("getAssignedShipToList");
		
		
		try
		{
			conn = ConnectionManager.getDBConnection();
		  pstmt = conn.prepareStatement(sql);
		  pstmt.setString(1, userId);
		  rs = pstmt.executeQuery();
		  int i = 0;
		  while(rs.next())
		  {
			  addressIdList.add(rs.getInt("BC_ADDRESS_BOOKID"));
			  i++;
		  }
		  if(i<1){
			 addressIdList = null;
		  }
		}
		catch (Exception e) {
	       e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return addressIdList;
	}	
	public static ArrayList<UsersModel> getDataForMPA(int buyingCompanyId,String authoListAssigned){
		ArrayList<UsersModel> agentUserList = new ArrayList<UsersModel>();
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
	    
	    String listofAssigned=null;
		
	  
	    try {
	    	conn = ConnectionManager.getDBConnection();
	    	System.out.println(PropertyAction.SqlContainer.get("getDataForMPA"));
			String sql = PropertyAction.SqlContainer.get("getDataForMPA");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,buyingCompanyId); 
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				//USER_ID,USER_NAME,FIRST_NAME,LAST_NAME,EMAIL,ADDRESS1,CITY,STATE,ZIP,COUNTRY
				UsersModel temp = new UsersModel();
				temp.setUserId(rs.getInt("USER_ID"));
				temp.setUserName(rs.getString("USER_NAME"));
				temp.setFirstName(rs.getString("FIRST_NAME"));
				temp.setLastName(rs.getString("LAST_NAME")); 
				temp.setEmailAddress(rs.getString("EMAIL"));
				temp.setPhoneNo(rs.getString("PHONE"));
				String roleName = rs.getString("ROLE_NAME");
				
				if(roleName!=null && roleName.trim().equalsIgnoreCase("Ecomm Customer Super User"))
				{
					temp.setIsSuperUser("Y");
				}
				else if(roleName!=null && roleName.trim().equalsIgnoreCase("Ecomm Customer Auth Purchase Agent"))
				{
					temp.setIsAuthPurchaseAgent("Y");
				}
				
				if(rs.getInt("APPROVAL_USER_ID")>0)
				{
					temp.setApproverAgentAssignStatus("Y");
					temp.setApproverEmail(rs.getString("APPROVER_EMAIL"));
				}
				listofAssigned = UsersDAO.getListAssignedAPAtoUser(Integer.toString(rs.getInt("USER_ID")));
				if (listofAssigned.equalsIgnoreCase("0")){
					//authoListAssigned=listofAssigned; check once
					temp.setIsBusyUser("0");
				}else{
					temp.setIsBusyUser("1");
					//authoListAssigned="1"; check once
				}
				agentUserList.add(temp);
	}
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
		  ConnectionManager.closeDBResultSet(rs);
		  ConnectionManager.closeDBPreparedStatement(pstmt);	
		  ConnectionManager.closeDBConnection(conn);
		}
		return agentUserList;
	} 
	public static String getUserRole(int userId)
	{
		String sql = PropertyAction.SqlContainer.get("getUserRole");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		String roleName = null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,userId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				roleName = rs.getString("ROLE_NAME");
               
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			
		}
		return roleName;
	}
	
	public static UsersModel getEntityDetailsByUserId(int userId)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
		UsersModel address = null;
		try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("getEntityAddressQuery");
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
            	address = new UsersModel();
            	address.setFirstName(rs.getString("FIRST_NAME"));
            	address.setLastName(rs.getString("LAST_NAME"));
            	address.setAddress1(rs.getString("ADDRESS1"));
            	address.setAddress2(rs.getString("ADDRESS2"));
            	address.setCity(rs.getString("CITY"));
            	address.setState(rs.getString("STATE"));
            	address.setCountry(rs.getString("COUNTRY"));
            	address.setEmailAddress(rs.getString("EMAIL"));
            	address.setZipCode(rs.getString("ZIP"));
            	address.setUserName(rs.getString("USER_NAME"));
            	address.setUserStatus(rs.getString("STATUS"));
            	address.setJobTitle(rs.getString("JOB_TITLE"));
            	SecureData getPassword = new SecureData();
        		String userPassword=getPassword.validatePassword(rs.getString("PASSWORD"));
            	address.setPassword(userPassword);
            	address.setPhoneNo(rs.getString("OFFICE_PHONE"));
            	address.setOfficePhone(rs.getString("CELL_PHONE"));
            	address.setContactId(rs.getString("ECLIPSE_CONTACT_ID"));
            	address.setAddressType("Ship");
            }
        
    }catch (SQLException e) { 
        e.printStackTrace();
    }finally {	    
    	  ConnectionManager.closeDBResultSet(rs);
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
    }
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return address;
	}
	public static String getListAssignedAPAtoUser(String userID){
		
		String sql =PropertyAction.SqlContainer.get("getListAssignedAPAtoUser");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		String status = "";
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
	            status = "1:1";
	            
			}else{
				
				status ="0";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return status;
	}
	public static UsersModel getDefaultShipNbill(String customerNumber){

		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		UsersModel uModel = new UsersModel();
		Connection conn = null;
		
		try{
			conn = ConnectionManager.getDBConnection();
			String sqlBill = PropertyAction.SqlContainer.get("getDefaultBill");
			String sqlShip = PropertyAction.SqlContainer.get("getDefaultShip");
			
			pstmt = conn.prepareStatement(sqlBill);
			pstmt.setString(1,customerNumber);
			rs = pstmt.executeQuery();
			if(rs!=null && rs.next()){
				uModel.setBillToId(rs.getString("BC_ADDRESS_BOOK_ID"));
			}
			
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			pstmt = conn.prepareStatement(sqlShip);
			pstmt.setString(1,customerNumber);
			rs = pstmt.executeQuery();
			if(rs!=null && rs.next()){
				uModel.setShipToId(rs.getString("BC_ADDRESS_BOOK_ID"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return uModel;
	}
	
	
	public static int updateUserAddressBook(Connection conn, int billAddressId,int shipAddressId,int userId,boolean connCommit)
	{
		int count = 0;
		PreparedStatement pstmt = null;
			
        try
        {
        	String sql = PropertyAction.SqlContainer.get("updateUserAddressId");
			//DEFAULT_BILLING_ADDRESS_ID=? AND DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE USER_ID=?
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, billAddressId);
			pstmt.setInt(2, shipAddressId);
			pstmt.setInt(3, userId);
			count = pstmt.executeUpdate();
			if(count>0){
				if(connCommit)
				conn.commit();
			}
        }
        catch(SQLException e)
        {
        	count =0;
        	e.printStackTrace();
        }
        catch(Exception e)
        {
        	count =0;
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
        }
		
		return count;
	}
	
public static CustomFieldModel getCustomIDs(Connection conn,String customFiledValue, String fieldName){
		
		CustomFieldModel customField = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
		  try {
			        
			        sql = "SELECT CUSTOM_FIELD_ID FROM CUSTOM_FIELDS WHERE FIELD_NAME = ?";
			        pstmt = conn.prepareStatement(sql);
			        pstmt.setString(1, fieldName);
			        rs = pstmt.executeQuery();
			        customField = new CustomFieldModel();
			        if(rs.next()){
			        	customField.setCustomFieldID(rs.getInt("CUSTOM_FIELD_ID"));
			        }else{
			        	customField = null;
			        }
			        if(customField!=null){
				        sql = "SELECT LOC_CUSTOM_FIELD_VALUE_ID FROM LOC_CUSTOM_FIELD_VALUES WHERE TEXT_FIELD_VALUE = ?";
				        ConnectionManager.closeDBResultSet(rs);
						ConnectionManager.closeDBPreparedStatement(pstmt);
				        pstmt = conn.prepareStatement(sql);
				        pstmt.setString(1, customFiledValue);
				        rs = pstmt.executeQuery();
				        if(rs.next()){
				        	customField.setLocCustomFieldValueID(rs.getInt("LOC_CUSTOM_FIELD_VALUE_ID"));
				        }
			        }
			    
			} catch (SQLException e) { 
			    e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);	
			  }
		
		return customField;
	}

	public static void isInternationalUser(Connection conn, int userId, int customFieldId, int locCustomFieldValId){
		
		int insertedRecords =0;
	    PreparedStatement pstmt=null;
	   
	    		try{
        	  pstmt = conn.prepareStatement("INSERT INTO USER_CUSTOM_FIELD_VALUES (USER_CUSTOM_FIELD_VALUE_ID,USER_ID,CUSTOM_FIELD_ID,LOC_CUSTOM_FIELD_VALUE_ID,USER_EDITED,UPDATED_DATETIME) VALUES (USER_CUSTOM_FIELD_VAL_ID_SEQ.NEXTVAL,?,?,?,?,TO_TIMESTAMP(SYSDATE,'DD-MON-RR HH.MI.SSXFF AM'))");
			  pstmt.setInt(1, userId);
			  pstmt.setInt(2, customFieldId);
			  pstmt.setInt(3, locCustomFieldValId);
			  pstmt.setInt(4, userId);
			  insertedRecords = pstmt.executeUpdate();
			  
			  if(insertedRecords>0){
				  System.out.println("IS INTERNATIONAL USER VALUE Inserted INTO CUSTOM FIELD FOR User ID : "+userId);
				  
			  }else{
				  System.out.println("IS INTERNATIONAL USER CUSTOM FIELD VALUE Insert Failed FOR User ID : "+userId);
			  }
	        
	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	    	 
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	      } 
	}
	
	public static int buildUserKeyWord(String type, int id){
		
		int count=-1;
		PreparedStatement pstmt=null;
	    String sql = "";
	    Connection  conn = null;
		try {
			conn = ConnectionManager.getDBConnection();
			if(type!=null && type.trim().equalsIgnoreCase("BUYINGCOMPANY")){
				sql = "UPDATE BUYING_COMPANY BC SET KEYWORDS = CUSTOMER_NAME || ' ; ' ||  SHORT_NAME || ' ; ' ||  ADDRESS1  || ' ; ' || CITY  || ' ; ' || STATE || ' ; ' ||  ZIP || ' ; ' ||  COUNTRY || ' ; ' ||  EMAIL || ' ; ' ||  URL || ' ; ' ||  ADDRESS2 || ' ; ' ||  ENTITY_ID || ' ; ' || (SELECT S.SUBSET_NAME FROM SUBSETS S WHERE S.SUBSET_ID = BC.SUBSET_ID) WHERE BUYING_COMPANY_ID=?";
			}
			if(type!=null && type.trim().equalsIgnoreCase("USER")){
				sql = "UPDATE CIMM_USERS SET KEYWORDS = USER_NAME || ' ; ' || FIRST_NAME  || ' ; ' || MIDDLE_NAME  || ' ; ' ||  LAST_NAME  || ' ; ' || EMAIL || ' ; ' ||  ADDRESS1 || ' ; ' || ADDRESS2 || ' ; ' || CITY || ' ; ' || STATE || ' ; ' || COUNTRY  || ' ; ' || ZIP || ' ; ' ||  OFFICE_PHONE  || ' ; ' || CELL_PHONE WHERE USER_ID=?";
			}
			if(type!=null && type.trim().equalsIgnoreCase("BUYINGCOMPANYALL")){
				sql = "UPDATE BUYING_COMPANY BC SET KEYWORDS = CUSTOMER_NAME || ' ; ' ||  SHORT_NAME || ' ; ' ||  ADDRESS1  || ' ; ' || CITY  || ' ; ' || STATE || ' ; ' ||  ZIP || ' ; ' ||  COUNTRY || ' ; ' ||  EMAIL || ' ; ' ||  URL || ' ; ' ||  ADDRESS2 || ' ; ' ||  ENTITY_ID || ' ; ' || (SELECT S.SUBSET_NAME FROM SUBSETS S WHERE S.SUBSET_ID = BC.SUBSET_ID) WHERE KEYWORDS IS NULL";
			}
            pstmt = conn.prepareStatement(sql);
            if(type!=null && !type.trim().equalsIgnoreCase("BUYINGCOMPANYALL")){
	           pstmt.setInt(1, id);
			}
            count = pstmt.executeUpdate();
            pstmt.close();
            if(count>0){
            	System.out.println(type+" Keywords updated");
            }else{
            	System.out.println(type+" Keywords Failed");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 finally {	    
			 ConnectionManager.closeDBPreparedStatement(pstmt);
			 ConnectionManager.closeDBConnection(conn);
	      }
			return count;
	}
	
	public static int buildKeyWord(String type, int id){
			
			int count=-1;
			PreparedStatement pstmt=null;
		    Connection  conn = null;
		    boolean flag=false;
			try {
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareCall("{call USER_KEYWORD_PROC(?,?)}");
				pstmt.setString(1,type);
				pstmt.setInt(2,id);
				flag = pstmt.execute();
				
	            if(flag){
	            	System.out.println(type+" Keywords updated");
	            }else{
	            	System.out.println(type+" Keywords Failed");
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			 finally {	    
				 ConnectionManager.closeDBPreparedStatement(pstmt);
				 ConnectionManager.closeDBConnection(conn);
		      }
				return count;
		}
	
	public static int grantGenralCatalogAccess(Connection conn, int buyingCompanyId){
		System.out.println(" ---- General Catalog Acess Start ---- ");
		int count=-1;
		PreparedStatement pstmt=null;
	    String sql = "";
		try {
			
			sql = "UPDATE BUYING_COMPANY SET GENERAL_CATALOG_ACCESS = 'Y' WHERE BUYING_COMPANY_ID=?";

			pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buyingCompanyId);
            count = pstmt.executeUpdate();
            
            pstmt.close();
            if(count>0){
            	System.out.println("General Catalog Acess Granted Sucessfully for : "+buyingCompanyId);
            }else{
            	System.out.println("General Catalog Acess Grant Failed for : "+buyingCompanyId);
            }
        System.out.println(" ---- General Catalog Acess End ---- ");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {	    
			 ConnectionManager.closeDBPreparedStatement(pstmt);
	      }
			return count;
	}

	public static int insertBuyingCompany(Connection conn, UsersModel userDetailList,int parentId){
		
		int buyingCompanyId=0;
		PreparedStatement pstmt = null;
	    int count = 0;
	    try{

	    	int siteId = 0;
	    	if(CommonDBQuery.getGlobalSiteId()>0){
	    		siteId = CommonDBQuery.getGlobalSiteId();
	    	}

	    	System.out.println("Inside insertBuyingCompany : Subset From Sys Param : "+CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("USERSUBSETID"))+" : Warehouse Subset : "+CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_WAREHOUSE_SUBSET_ID")));
	    	int subsetId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("USERSUBSETID"));
	    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_WAREHOUSE_SUBSET_ID")).equalsIgnoreCase("Y")){
	    		WarehouseModel warehouseModel = getWareHouseDetailsByCode(CommonUtility.validateString(userDetailList.getWareHouseCodeStr()));
	    		if(warehouseModel!=null && warehouseModel.getSubsetId()>0){
	    			subsetId = warehouseModel.getSubsetId();
	    		}else if(userDetailList!=null && userDetailList.getSubsetId()>0){
	    			subsetId = userDetailList.getSubsetId();
	    		}
	    	}else{
	    		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_SPECIFIC_SUBSET_ID")).equalsIgnoreCase("Y")) {
	    			subsetId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SPECIFIC_SUBSET"));
	    		}else if(userDetailList!=null && userDetailList.getSubsetId()>0){
	    			subsetId = userDetailList.getSubsetId();
	    		}
	    	}
			if(CommonUtility.customServiceUtility()!=null) { 	
				subsetId = CommonUtility.customServiceUtility().CustomizatedSubsetId(userDetailList, subsetId);
			}//CustomServiceProvider
	    	//String sql = PropertyAction.SqlContainer.get("insertBuyingCompany");
	    	String sql = null;
	    	if(CommonUtility.validateString(userDetailList.getDisableSubmitPOCC()).trim().length()>0 && CommonUtility.validateString(userDetailList.getDisableSubmitPOCC()).equalsIgnoreCase("Y")){
	    		sql = PropertyAction.SqlContainer.get("inserBuyingCompanyDisableSubmitPOCC");	
	    	}else if(CommonUtility.validateString(userDetailList.getDisableSubmitPO()).trim().length()>0 && CommonUtility.validateString(userDetailList.getDisableSubmitPO()).equalsIgnoreCase("Y")){
	    		sql = PropertyAction.SqlContainer.get("inserBuyingCompanyDisableSubmitPO");	
	    	}else if(CommonUtility.validateString(userDetailList.getCustomerType()).trim().length()>0 && CommonUtility.validateString(userDetailList.getCustomerType()).equalsIgnoreCase("G")){
	    		sql = PropertyAction.SqlContainer.get("insertBuyingCompanyForGuestUser");	
	    	}
	    	  
	    	else{
	    		sql = PropertyAction.SqlContainer.get("insertBuyingCompany");
	    	}
	    	String shortNameVal = "No Entity Name";
	    	if(userDetailList.getCustomerName()!=null && userDetailList.getCustomerName().trim().length()>0)
	    	{
	    		userDetailList.setEntityName(userDetailList.getCustomerName());
	    		shortNameVal=userDetailList.getEntityName();
	    	}
	    	else {
	    	if(userDetailList.getEntityName()!=null && userDetailList.getEntityName().trim().length()>0)
	    	{
	    		shortNameVal = userDetailList.getEntityName();
	    	}else{
	    		String customerName = "";
	    	
	    		if(userDetailList.getFirstName()!=null && userDetailList.getFirstName().trim().length()>0){
	    			customerName = userDetailList.getFirstName().trim();
	    		}
	    		if(userDetailList.getLastName()!=null && userDetailList.getLastName().trim().length()>0){
	    			customerName = customerName+" "+userDetailList.getLastName();
	    		}
	    		if(customerName!=null && customerName.trim().length()>0){
	    			userDetailList.setEntityName(customerName);
	    			shortNameVal = customerName;
	    		}else{
	    			userDetailList.setEntityName(shortNameVal);
	    		}
	    	}
	    	}
	    	String shortNameArr[] = shortNameVal.trim().split("\\s+");
	    	String shortName = "";

	    	if(shortNameArr!=null && shortNameArr.length>0)
	    	{
	    		for(String sName:shortNameArr)
	    		{
	    			shortName = shortName + sName.substring(0, 1);
	    		}
	    	}

	    	//BUYING_COMPANY_ID,CUSTOMER_NAME,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,EMAIL,URL,SUBSET_ID,STATUS,UPDATED_DATETIME,ENTITY_ID
	    	buyingCompanyId = CommonDBQuery.getSequenceId("BUYING_COMPANY_ID_SEQ");
	    	System.out.println("userDetailList.getCountry() : " + userDetailList.getCountry());
	    	String country = userDetailList.getCountry();
	    	if(country !=null){
	    		String ecCountry = getCountryCode(country);
	    		if(ecCountry!=null && !ecCountry.equalsIgnoreCase(""))
	    			country = ecCountry;
	    		else
	    			country = "US";
	    	}
	    	else
	    		country = "";
	    	pstmt = conn.prepareStatement(sql);
	    	pstmt.setInt(1, buyingCompanyId);
	    	pstmt.setString(2, userDetailList.getEntityName());

	    	if(userDetailList.getAddress1()==null || userDetailList.getAddress1().trim().equalsIgnoreCase(""))
	    		pstmt.setString(3, "No Address");
	    	else
	    		pstmt.setString(3, userDetailList.getAddress1());

	    	/*if(userDetailList.getAddress2()==null || userDetailList.getAddress2().trim().equalsIgnoreCase(""))
				pstmt.setString(4, "No Address");
			else*/
	    	pstmt.setString(4, userDetailList.getAddress2());
	    	pstmt.setString(5, userDetailList.getCity());
	    	pstmt.setString(6, userDetailList.getState());
	    	pstmt.setString(7, userDetailList.getZipCode());
	    	pstmt.setString(8, country);
	    	pstmt.setString(9, userDetailList.getEmailAddress());
	    	pstmt.setString(10, userDetailList.getWebAddress());
	    	pstmt.setInt(11, subsetId);
	    	pstmt.setString(12, userDetailList.getEntityId()!=null?userDetailList.getEntityId():CommonUtility.validateParseIntegerToString(buyingCompanyId));
	    	if(userDetailList!=null && CommonUtility.validateString(userDetailList.getEntityId()).length()>0 && userDetailList.getEntityId()!="0") {
	    		pstmt.setString(12, CommonUtility.validateString(userDetailList.getEntityId()).toUpperCase());
	    	}else {
	    		pstmt.setString(12, CommonUtility.validateParseIntegerToString(buyingCompanyId));
	    	}
	    	if(parentId>0){
	    		pstmt.setString(13, CommonUtility.validateParseIntegerToString(parentId));
	    	}else{
	    		pstmt.setString(13, CommonUtility.validateParseIntegerToString(buyingCompanyId));
	    	}
	    	pstmt.setString(14, shortName);
	    	pstmt.setString(15, userDetailList.getCurrency());
	    	pstmt.setInt(16, userDetailList.getWareHouseCode());
	    	pstmt.setString(17, userDetailList.getWareHouseCodeStr());
	    	pstmt.setInt(18, siteId);
	    	if(CommonUtility.validateString(userDetailList.getDisableSubmitPOCC()).trim().length()>0 && CommonUtility.validateString(userDetailList.getDisableSubmitPOCC()).equalsIgnoreCase("Y")){
	    		pstmt.setString(19, "N");
	    	    pstmt.setString(20, "N");
	    	}else if(CommonUtility.validateString(userDetailList.getDisableSubmitPO()).trim().length()>0 && CommonUtility.validateString(userDetailList.getDisableSubmitPO()).equalsIgnoreCase("Y")){
	    		pstmt.setString(19, "N");
	    	}
	    	else if(CommonUtility.validateString(userDetailList.getCustomerType()).trim().length()>0 && CommonUtility.validateString(userDetailList.getCustomerType()).equalsIgnoreCase("G")){
	    		pstmt.setString(19, userDetailList.getCustomerType());
	    	}
	    	count = pstmt.executeUpdate();
	    	if(count<1){
	    		buyingCompanyId = 0;
	    	}else{
	    		if(CommonDBQuery.getSystemParamtersList().get("ENABLE_GENERAL_CATALOG_ACCESS")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_GENERAL_CATALOG_ACCESS").trim().equalsIgnoreCase("Y")){
	    			grantGenralCatalogAccess(conn,buyingCompanyId);
	    		}
	    	}
	    }
	    catch(SQLException e)
	    {
	    	buyingCompanyId =0;
	    	e.printStackTrace();
	    }
	    catch(Exception e)
	    {
	    	buyingCompanyId =0;
	    	e.printStackTrace();
	    }
	    finally{
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    }
		return buyingCompanyId;
	}
	
	public static int insertNewUser(Connection conn, UsersModel userDetailList,String userName,String password,int buyingCompanyId, String parentUserId,boolean conCommit){
		
		int userId=0;
	    PreparedStatement pstmt = null;
	    int count = 0;
	     
	    try{
	
	    	String sql = PropertyAction.SqlContainer.get("insertNewUserFromERP");
	    	String securePassword = getsecurePassword(password);
			//USER_ID,USER_NAME,PASSWORD,FIRST_NAME,LAST_NAME,BUYING_COMPANY_ID,OFFICE_PHONE,CELL_PHONE,FAX,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,ECLIPSE_CONTACT_ID,UPDATED_DATETIME
			userId = CommonDBQuery.getSequenceId("USER_ID_SEQUENCE");
			String country = userDetailList.getCountry();
			String regUserName = userDetailList.getEmailAddress();
			if(CommonUtility.validateString(userDetailList.getUserName()).length()>0) {
				regUserName = userDetailList.getUserName();
			}
			if(country !=null){
				String ecCountry = getCountryCode(country);
				if(ecCountry!=null && !ecCountry.equalsIgnoreCase("")){
					country = ecCountry;
				}else{
					country = "US";
				}
			}else{
				country = "";
			}
			
			String firstName = userDetailList.getFirstName();
			String lastName = "";
			if(firstName!=null && !firstName.trim().equalsIgnoreCase("")){
				 int idx = firstName.lastIndexOf(" ");
				 if(idx>0){
					 lastName = (firstName.substring(idx)!=null?firstName.substring(idx).trim():"");
				 }
				 if(userDetailList.getLastName()==null || userDetailList.getLastName().trim().equalsIgnoreCase("")){
					 userDetailList.setLastName(lastName);
					 if(idx>0){
					 userDetailList.setFirstName((firstName.substring(0, idx)!=null?firstName.substring(0, idx).trim():firstName));
					 }
				 }
			}
	
	    	//INSERT INTO CIMM_USERS(USER_ID,USER_NAME,PASSWORD,FIRST_NAME,LAST_NAME,BUYING_COMPANY_ID,OFFICE_PHONE,CELL_PHONE,FAX,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,ECLIPSE_CONTACT_ID,EMAIL,UPDATED_DATETIME,FIRST_LOGIN,PARENT_USER_ID,EXISTING_CUSTOMER,REGISTERED_DATE)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,SYSDATE)
			pstmt = conn.prepareStatement(sql);
			System.out.println("Insert New user : "+sql);
			System.out.println("\n 1 : userId : "+userId+"\n 2 : userDetailList.getEmailAddress() : "+regUserName+"\n 3 : securePassword : "+securePassword+"\n 4 : userDetailList.getFirstName() : "+userDetailList.getFirstName()+"\n 5 : userDetailList.getLastName() : "+userDetailList.getLastName()+"\n 6 : buyingCompanyId : "+buyingCompanyId+"\n 7 : userDetailList.getPhoneNo() : "+userDetailList.getPhoneNo()+"\n 8 :  : \n 9 : userDetailList.getFaxNo() : "+userDetailList.getFaxNo()+"\n 10 : userDetailList.getAddress1() : "+userDetailList.getAddress1()+"\n 11 : userDetailList.getAddress2() : "+userDetailList.getAddress2()+"\n 12 : userDetailList.getCity() : "+userDetailList.getCity()+"\n 13 : userDetailList.getState() : "+userDetailList.getState()+"\n 14 : userDetailList.getZipCode() : "+userDetailList.getZipCode()+"\n 15 : country : "+country+"\n 16 : userDetailList.getContactId() : "+userDetailList.getContactId()+"\n 17 : userDetailList.getEmailAddress() : "+userDetailList.getEmailAddress()+"\n 18 :  : N\n 19 : userDetailList.getTermsType() : "+userDetailList.getTermsType()+"\n 20 : userDetailList.getTermsTypeDesc() : "+userDetailList.getTermsTypeDesc()+"\n 21 : userDetailList.getShipViaID() : "+userDetailList.getShipViaID()+"\n 22 : userDetailList.getShipViaMethod() : "+userDetailList.getShipViaMethod()+"\n 23 : CommonUtility.validateNumber(parentUserId) : "+CommonUtility.validateNumber(parentUserId)+"\n 24 : userDetailList.getUserStatus() : "+userDetailList.getUserStatus()+"\n 25 : userDetailList.getUserERPId() : "+userDetailList.getUserERPId()+"\n 26 : userDetailList.getEnablePO : "+userDetailList.getEnablePO());
			pstmt.setInt(1, userId);
			pstmt.setString(2, CommonUtility.validateString(regUserName));//userName
			pstmt.setString(3, securePassword);
			pstmt.setString(4, userDetailList.getFirstName());
			pstmt.setString(5, userDetailList.getLastName());
			pstmt.setInt(6, buyingCompanyId);
			pstmt.setString(7, userDetailList.getPhoneNo());
			pstmt.setString(8, " ");
			pstmt.setString(9, userDetailList.getFaxNo());
			pstmt.setString(10, userDetailList.getAddress1());
			pstmt.setString(11, userDetailList.getAddress2());
			pstmt.setString(12, userDetailList.getCity());
			pstmt.setString(13, userDetailList.getState());
			pstmt.setString(14, userDetailList.getZipCode());
			pstmt.setString(15, country);
			if(userDetailList.getContactId()!=null && CommonUtility.validateString(userDetailList.getContactId()).length()>0 && userDetailList.getContactId()!="0") {
				pstmt.setString(16, userDetailList.getContactId());
			}else {
				pstmt.setString(16, CommonUtility.validateParseIntegerToString(userDetailList.getBuyingCompanyId()));
			}
			pstmt.setString(17, CommonUtility.validateString(userDetailList.getEmailAddress()));
			if(CommonUtility.validateString(userDetailList.getUserStatus()).equalsIgnoreCase("Y")){
				pstmt.setString(18, "Y");
			}else{
				pstmt.setString(18, "N");
			}
			/**
			 * Below code is written for Electrozad to set first login as Y for inactive user *Reference - Deepak
			 */
			String result="";//Electrozad Custom mService
			if(CommonUtility.customServiceUtility()!=null){
				result=CommonUtility.customServiceUtility().setFirstLoginTrueForInactiveUser();//Electrozad Custom mService
			}
			if(result != null && result.trim().equalsIgnoreCase("Y")) {
				pstmt.setString(18, "Y");
			}
			pstmt.setString(19, userDetailList.getTermsType());
			pstmt.setString(20, userDetailList.getTermsTypeDesc());
			pstmt.setString(21, userDetailList.getShipViaID());
			pstmt.setString(22, userDetailList.getShipViaMethod());
			pstmt.setInt(23, CommonUtility.validateNumber(parentUserId));
			pstmt.setString(24, userDetailList.getUserStatus());
			pstmt.setString(25, userDetailList.getJobTitle());
			pstmt.setString(26, userDetailList.getSalesRepContact());
			pstmt.setInt(27, CommonDBQuery.getGlobalSiteId());
			pstmt.setString(28, CommonUtility.validateString(userDetailList.getAccountManager()));
			pstmt.setString(29, CommonUtility.validateString(userDetailList.getClosestBranch()));
			pstmt.setString(30, CommonUtility.validateString(userDetailList.getUserERPId()));
			pstmt.setString(31, CommonUtility.validateString(userDetailList.getIsTaxable()));
			pstmt.setString(32, CommonUtility.validateString(userDetailList.getEnablePO()));			
			count = pstmt.executeUpdate();
			if(count<1){
				userId = 0;
			}else{
				if(conCommit)
				conn.commit();
			}
			System.out.println("insertNewUser - count : "+count+" User Id : "+userId);
	    }
	    catch(SQLException e)
	    {
	    	userId = 0;
	    	e.printStackTrace();
	    }
	    catch(Exception e)
	    {
	    	userId = 0;
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	ConnectionManager.closeDBPreparedStatement(pstmt);
	    	
	    }
		return userId;
	}
	
	
public static int insertGuestUser(Connection conn, UsersModel userDetailList,String userName,String password,int buyingCompanyId, String parentUserId,boolean conCommit){
		
		int userId=0;
	    PreparedStatement pstmt = null;
	    int count = 0;
	     
	    try{
	
	    	String sql = PropertyAction.SqlContainer.get("insertGuestUserFromERP");
	    	String securePassword = getsecurePassword(password);;
			//USER_ID,USER_NAME,PASSWORD,FIRST_NAME,LAST_NAME,BUYING_COMPANY_ID,OFFICE_PHONE,CELL_PHONE,FAX,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,ECLIPSE_CONTACT_ID,UPDATED_DATETIME
			userId = CommonDBQuery.getSequenceId("USER_ID_SEQUENCE");
			String country = userDetailList.getCountry();
			if(country !=null){
				String ecCountry = getCountryCode(country);
				if(ecCountry!=null && !ecCountry.equalsIgnoreCase("")){
					country = ecCountry;
				}else{
					country = "US";
				}
			}else{
				country = "";
			}
			
			String firstName = userDetailList.getFirstName();
			String lastName = "";
			if(firstName!=null && !firstName.trim().equalsIgnoreCase("")){
				 int idx = firstName.lastIndexOf(" ");
				 if(idx>0){
					 lastName = (firstName.substring(idx)!=null?firstName.substring(idx).trim():"");
				 }
				 if(userDetailList.getLastName()==null || userDetailList.getLastName().trim().equalsIgnoreCase("")){
					 userDetailList.setLastName(lastName);
					 if(idx>0){
					 userDetailList.setFirstName((firstName.substring(0, idx)!=null?firstName.substring(0, idx).trim():firstName));
					 }
				 }
			}
	
	    	//INSERT INTO CIMM_USERS(USER_ID,USER_NAME,PASSWORD,FIRST_NAME,LAST_NAME,BUYING_COMPANY_ID,OFFICE_PHONE,CELL_PHONE,FAX,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,ECLIPSE_CONTACT_ID,EMAIL,UPDATED_DATETIME,FIRST_LOGIN,PARENT_USER_ID,EXISTING_CUSTOMER,REGISTERED_DATE)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,SYSDATE)
			pstmt = conn.prepareStatement(sql);
			System.out.println("Insert New user : "+sql);
			System.out.println("\n 1 : userId : "+userId+"\n 2 : userDetailList.getEmailAddress() : "+userDetailList.getEmailAddress()+"\n 3 : securePassword : "+securePassword+"\n 4 : userDetailList.getFirstName() : "+userDetailList.getFirstName()+"\n 5 : userDetailList.getLastName() : "+userDetailList.getLastName()+"\n 6 : buyingCompanyId : "+buyingCompanyId+"\n 7 : userDetailList.getPhoneNo() : "+userDetailList.getPhoneNo()+"\n 8 :  : \n 9 : userDetailList.getFaxNo() : "+userDetailList.getFaxNo()+"\n 10 : userDetailList.getAddress1() : "+userDetailList.getAddress1()+"\n 11 : userDetailList.getAddress2() : "+userDetailList.getAddress2()+"\n 12 : userDetailList.getCity() : "+userDetailList.getCity()+"\n 13 : userDetailList.getState() : "+userDetailList.getState()+"\n 14 : userDetailList.getZipCode() : "+userDetailList.getZipCode()+"\n 15 : country : "+country+"\n 16 : userDetailList.getContactId() : "+userDetailList.getContactId()+"\n 17 : userDetailList.getEmailAddress() : "+userDetailList.getEmailAddress()+"\n 18 :  : N\n 19 : userDetailList.getTermsType() : "+userDetailList.getTermsType()+"\n 20 : userDetailList.getTermsTypeDesc() : "+userDetailList.getTermsTypeDesc()+"\n 21 : userDetailList.getShipViaID() : "+userDetailList.getShipViaID()+"\n 22 : userDetailList.getShipViaMethod() : "+userDetailList.getShipViaMethod()+"\n 23 : CommonUtility.validateNumber(parentUserId) : "+CommonUtility.validateNumber(parentUserId)+"\n 24 : userDetailList.getUserStatus() : "+userDetailList.getUserStatus()+"\n 25 : userDetailList.getUserERPId() : "+userDetailList.getUserERPId());
			pstmt.setInt(1, userId);
			pstmt.setString(2, CommonUtility.validateString(userDetailList.getEmailAddress()));//userName
			pstmt.setString(3, securePassword);
			pstmt.setString(4, userDetailList.getFirstName());
			pstmt.setString(5, userDetailList.getLastName());
			pstmt.setInt(6, buyingCompanyId);
			pstmt.setString(7, userDetailList.getPhoneNo());
			pstmt.setString(8, " ");
			pstmt.setString(9, userDetailList.getFaxNo());
			pstmt.setString(10, userDetailList.getAddress1());
			pstmt.setString(11, userDetailList.getAddress2());
			pstmt.setString(12, userDetailList.getCity());
			pstmt.setString(13, userDetailList.getState());
			pstmt.setString(14, userDetailList.getZipCode());
			pstmt.setString(15, country);
			pstmt.setString(16, userDetailList.getContactId());
			pstmt.setString(17, CommonUtility.validateString(userDetailList.getEmailAddress()));
			if(CommonUtility.validateString(userDetailList.getUserStatus()).equalsIgnoreCase("Y")){
				pstmt.setString(18, "Y");
			}else{
				pstmt.setString(18, "N");
			}
			pstmt.setString(19, userDetailList.getTermsType());
			pstmt.setString(20, userDetailList.getTermsTypeDesc());
			pstmt.setString(21, userDetailList.getShipViaID());
			pstmt.setString(22, userDetailList.getShipViaMethod());
			pstmt.setInt(23, CommonUtility.validateNumber(parentUserId));
			pstmt.setString(24, userDetailList.getUserStatus());
			pstmt.setString(25, userDetailList.getJobTitle());
			pstmt.setString(26, userDetailList.getSalesRepContact());
			pstmt.setInt(27, CommonDBQuery.getGlobalSiteId());
			pstmt.setString(28, CommonUtility.validateString(userDetailList.getAccountManager()));
			pstmt.setString(29, CommonUtility.validateString(userDetailList.getClosestBranch()));
			pstmt.setString(30, CommonUtility.validateString(userDetailList.getCustomerType()));
			pstmt.setString(31, CommonUtility.validateString(userDetailList.getUserERPId()));
			pstmt.setString(32, CommonUtility.validateString(userDetailList.getIsTaxable()));
			count = pstmt.executeUpdate();
			if(count<1){
				userId = 0;
			}else{
				if(conCommit)
				conn.commit();
			}
			System.out.println("insertNewUser - count :"+count+" User Id : "+userId);
	    }
	    catch(SQLException e)
	    {
	    	userId = 0;
	    	e.printStackTrace();
	    }
	    catch(Exception e)
	    {
	    	userId = 0;
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	ConnectionManager.closeDBPreparedStatement(pstmt);
	    	
	    }
		return userId;
	}
	
	public static boolean checkCustomerNumberExistInDB(String customerNumber){
		boolean isUser= false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try{
	    	conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("checkCustomerNumberExistInDB");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, customerNumber.toUpperCase().trim());
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				isUser = true;
			}
	    }catch(SQLException e){
	    	isUser =true;
	    	e.printStackTrace();
	    }catch(Exception e){
	    	isUser =true;
	    	e.printStackTrace();
	    }finally{
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
	    	ConnectionManager.closeDBResultSet(rs);
	    }
		
		return isUser;
	}

	public static ArrayList<UsersModel> getUserAccountList(String userName)
	{
		ArrayList<UsersModel> userDetail = new ArrayList<UsersModel>();
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
	
		try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("getUserAccountList");
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName.toUpperCase());
            pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
            rs = pstmt.executeQuery();
           while(rs.next())
           {
        	   UsersModel userObject = new UsersModel();
        	   userObject.setFirstName(rs.getString("FIRST_NAME"));
        	   userObject.setLastName(rs.getString("LAST_NAME"));
        	   userObject.setPassword(rs.getString("PASSWORD"));
        	   userObject.setEmailAddress(rs.getString("EMAIL"));
        	   userObject.setCustomerName(rs.getString("CUSTOMER_NAME"));
        	   userObject.setUserName(rs.getString("USER_NAME"));
        	   userObject.setPhoneNo(rs.getString("OFFICE_PHONE"));
        	   userObject.setState(rs.getString("STATUS"));
        	   userObject.setEntityId(CommonUtility.validateParseIntegerToString(rs.getInt("ECLIPSE_CONTACT_ID")));
        	   userDetail.add(userObject);
           }
           
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    		ConnectionManager.closeDBResultSet(rs);
    		ConnectionManager.closeDBPreparedStatement(pstmt);
    		ConnectionManager.closeDBConnection(conn);	
      }
		return userDetail;
	}

	public static ArrayList<ShipVia> getShipViaList(){
		ArrayList<ShipVia> shipViaList = null;
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    ResultSet rs = null;
		    String sql = "";
		    
		    int shipViaNumId = 0;
		    String shipViaStrId="";
		    String shipViaDesc = "";
		       
			try {
		        conn = ConnectionManager.getDBConnection();
		        sql = PropertyAction.SqlContainer.get("getShipViaList");
		        pstmt = conn.prepareStatement(sql);
		        rs = pstmt.executeQuery();
		        
		        shipViaList = new ArrayList<ShipVia>();
		        while(rs.next()){
		        	ShipVia shipViaModel = new ShipVia();
		        	shipViaNumId = rs.getInt("SHIP_ID");
		        	shipViaStrId = rs.getString("SHIP_VIA_ID");
		        	shipViaDesc = rs.getString("SHIP_VIA_DESC");
		        	
		        	shipViaModel.setShipViaID(shipViaStrId);
		        	shipViaModel.setDescription(shipViaDesc);
		        	
		        	shipViaList.add(shipViaModel);
		        }
		    
		} catch (SQLException e) { 
		    e.printStackTrace();
		}
		finally {	    
			  ConnectionManager.closeDBResultSet(rs);
			  ConnectionManager.closeDBPreparedStatement(pstmt);
			  ConnectionManager.closeDBConnection(conn);	
		  }
		
		return shipViaList;
	}
	public static int getTaxInfo(String entityId)
	{
		
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    int taxId = 0;
	  
	     
        
        try {
        	conn = ConnectionManager.getDBConnection();
	          preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getTaxInfo"));			 
			  preStat.setString(1, entityId);
			  rs =preStat.executeQuery();
			  if(rs.next())
			  {				
				  taxId = rs.getInt("TAX_INFO");
			  }
			 
	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	    
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
		return taxId;
		
	}
	
	public static int getCustomerWareHouseID(String wareHouseCode){
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
	    int wareHouseCodeId = 0;

		try {
	        conn = ConnectionManager.getDBConnection();
	        sql = PropertyAction.SqlContainer.get("getCustomerWareHouseID");
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, wareHouseCode);
	       
	        rs = pstmt.executeQuery();
	        if(rs.next()){
	        	wareHouseCodeId = rs.getInt("WAREHOUSE_ID");
	        }
	} catch (SQLException e) { 
	    e.printStackTrace();
	}
	finally {	    
		  ConnectionManager.closeDBResultSet(rs);
		  ConnectionManager.closeDBPreparedStatement(pstmt);
		  ConnectionManager.closeDBConnection(conn);	
	  }
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return wareHouseCodeId;
		
	}
	public static UsersModel getCustomerDefault(UsersModel usersModel)
	{
		
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
		
	    String stateCode = "";
	    String zipCode = "";
	    String country = "";
	    if(usersModel!=null){
	    	stateCode = usersModel.getState();
		    zipCode = usersModel.getZipCode();
		    country = usersModel.getCountry();
	    }
	    
	    
		UsersModel customerDefault = null;
		try {
	        conn = ConnectionManager.getDBConnection();
	        sql = PropertyAction.SqlContainer.get("getCustomerDefault");
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, stateCode);
	        rs = pstmt.executeQuery();
	       
	        //int wareHouseCode = getCustomerLocation(stateCode, zipCode);
	        String wareHouseCodeStr = null;// removed as der is only one warehouse
	        //String wareHouseCodeStr = getCustomerLocationStr(stateCode, zipCode);
	        if(rs.next())
	        {//USER_NAME,PASSWORD,FIRST_NAME,LAST_NAME,ADDRESS1,ADDRESS2,CITY,STATE,COUNTRY,ZIP,ECLIPSE_CONTACT_ID,EMAIL
	        	customerDefault = new UsersModel();
	        	if(wareHouseCodeStr!=null && !wareHouseCodeStr.trim().equalsIgnoreCase("")  && !wareHouseCodeStr.trim().equalsIgnoreCase("0")){//if(wareHouseCode>0){
	        		//customerDefault.setWareHouseCode(wareHouseCode);
	        		customerDefault.setWareHouseCodeStr(wareHouseCodeStr);
	        	}else{
		        	//customerDefault.setWareHouseCode(rs.getInt("WAREHOUSE_CODE"));
		        	customerDefault.setWareHouseCodeStr(rs.getString("WAREHOUSE_CODE"));
	        	}
	        	customerDefault.setCreditManagerStr(rs.getString("CREDIT_MANGER_CODE"));
	        	//customerDefault.setCreditManager(rs.getInt("CREDIT_MANGER_CODE"));
	        	customerDefault.setSalesRepIn(rs.getString("SALES_REP_IN"));
	        	customerDefault.setSalesRepOut(rs.getString("SALES_REP_OUT"));
	        	customerDefault.setDivisionNo(rs.getInt("DIVISION_NO"));
	        	System.out.println("From Data Base: WAREHOUSE_CODE : "+rs.getString("WAREHOUSE_CODE")+", CREDIT_MANGER_CODE: "+rs.getString("CREDIT_MANGER_CODE")+", SALES_REP_IN :"+rs.getString("SALES_REP_IN")+", SALES_REP_OUT : "+rs.getString("SALES_REP_OUT"));
	         }else{
	        	 
	        	 
	        	 LinkedHashMap<String,String> systemParametersList= CommonDBQuery.getSystemParamtersList();
	        	 	customerDefault = new UsersModel();
	        	 	
	        	 	if(country!=null && (country.trim().equalsIgnoreCase("US") || country.trim().equalsIgnoreCase("USA"))){
	        	 		//customerDefault.setWareHouseCode(systemParametersList.get("DEFAULT_WAREHOUSE_CODE"));systemParametersList.get("DEFAULT_")
		        	 	customerDefault.setWareHouseCodeStr(CommonUtility.validateString(systemParametersList.get("DEFAULT_SX_WAREHOUSE")));
		        	 	customerDefault.setCreditManagerStr(CommonUtility.validateString(systemParametersList.get("DEFAULT_CREDIT_MANGER_CODE")));
			         	//customerDefault.setCreditManager(systemParametersList.get("DEFAULT_CREDIT_MANGER_CODE"));
		        	 	customerDefault.setSalesRepIn(CommonUtility.validateString(systemParametersList.get("DEFAULT_SALES_REP_IN")));
		        	 	customerDefault.setSalesRepOut(CommonUtility.validateString(systemParametersList.get("DEFAULT_SALES_REP_OUT")));
		        	 	customerDefault.setDivisionNo(CommonUtility.validateNumber(systemParametersList.get("DEFAULT_DIVISION_NO")));
			         	System.out.println("From Sys Param : WAREHOUSE_CODE : "+systemParametersList.get("DEFAULT_SX_WAREHOUSE")+", CREDIT_MANGER_CODE: "+systemParametersList.get("DEFAULT_CREDIT_MANGER_CODE")+", SALES_REP_IN :"+systemParametersList.get("DEFAULT_SALES_REP_IN")+", SALES_REP_OUT : "+systemParametersList.get("DEFAULT_SALES_REP_OUT"));
	        	 	}else{
	        	 		
	        	 		customerDefault.setWareHouseCodeStr(CommonUtility.validateString(systemParametersList.get("DEFAULT_SX_WAREHOUSE_INTERNATIONAL")));
		        	 	customerDefault.setCreditManagerStr(CommonUtility.validateString(systemParametersList.get("DEFAULT_CREDIT_MANGER_CODE_INTERNATIONAL")));
			         	customerDefault.setSalesRepIn(CommonUtility.validateString(systemParametersList.get("DEFAULT_SALES_REP_IN_INTERNATIONAL")));
		        	 	customerDefault.setSalesRepOut(CommonUtility.validateString(systemParametersList.get("DEFAULT_SALES_REP_OUT_INTERNATIONAL")));
		        	 	customerDefault.setDivisionNo(CommonUtility.validateNumber(systemParametersList.get("DEFAULT_DIVISION_NO_INTERNATIONAL")));
			         	System.out.println("From Sys Param : WAREHOUSE_CODE : "+systemParametersList.get("DEFAULT_SX_WAREHOUSE_INTERNATIONAL")+", CREDIT_MANGER_CODE: "+systemParametersList.get("DEFAULT_CREDIT_MANGER_CODE_INTERNATIONAL")+", SALES_REP_IN :"+systemParametersList.get("DEFAULT_SALES_REP_IN_INTERNATIONAL")+", SALES_REP_OUT : "+systemParametersList.get("DEFAULT_SALES_REP_OUT_INTERNATIONAL"));
	        	 		
	        	 	}
		        	
	         }
	 } catch (SQLException e) { 
	    e.printStackTrace();
	}
	finally {	    
		  ConnectionManager.closeDBResultSet(rs);
		  ConnectionManager.closeDBPreparedStatement(pstmt);
		  ConnectionManager.closeDBConnection(conn);	
	  }
		return customerDefault;
		
	}
	public static CustomFieldModel getCustomIDs(String customFiledValue, String fieldName){
			
			CustomFieldModel customField = null;
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    ResultSet rs = null;
		    String sql = "";
			  try {
				        conn = ConnectionManager.getDBConnection();
				        sql = "SELECT CUSTOM_FIELD_ID FROM CUSTOM_FIELDS WHERE FIELD_NAME = ?";
				        pstmt = conn.prepareStatement(sql);
				        pstmt.setString(1, fieldName);
				        rs = pstmt.executeQuery();
				        customField = new CustomFieldModel();
				        if(rs.next()){
				        	customField.setCustomFieldID(rs.getInt("CUSTOM_FIELD_ID"));
				        }else{
				        	customField = null;
				        }
				        if(customField!=null){
					        sql = "SELECT LOC_CUSTOM_FIELD_VALUE_ID FROM LOC_CUSTOM_FIELD_VALUES WHERE TEXT_FIELD_VALUE = ?";
					        ConnectionManager.closeDBResultSet(rs);
							ConnectionManager.closeDBPreparedStatement(pstmt);
					        pstmt = conn.prepareStatement(sql);
					        pstmt.setString(1, customFiledValue);
					        rs = pstmt.executeQuery();
					        if(rs.next()){
					        	customField.setLocCustomFieldValueID(rs.getInt("LOC_CUSTOM_FIELD_VALUE_ID"));
					        }
				        }
				    
				} catch (SQLException e) { 
				    e.printStackTrace();
				}
				finally {	    
					  ConnectionManager.closeDBResultSet(rs);
					  ConnectionManager.closeDBPreparedStatement(pstmt);
					  ConnectionManager.closeDBConnection(conn);	
				  }
			
			return customField;
		}
	
	public static boolean saveEmailsInDB(String toAddress, String emailSubject,String emailFrom, StringBuilder emailMessage,String sentMailStatus,String emailType,String bcc) {

		boolean success = false;
		ResultSet rs = null;
		String sqlQuery = null;
		Connection conn = null;
		Date date = new Date();
		//OraclePreparedStatement pstmt = null;
		PreparedStatement  pstmt = null;
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

		
		try {
			conn = ConnectionManager.getDBConnection();
			sqlQuery = "INSERT INTO EMAILS(EMAIL_TO, SUBJECT, EMAIL_FROM, EMAIL_BODY, SEND_STATUS, EMAIL_TYPE, LAST_UPDATE,TO2) VALUES (?, ?, ?, ?, ?, ?, to_date(?,'yyyy-mm-dd HH24:MI:SS'),?)";
			//pstmt = (OraclePreparedStatement) conn.prepareStatement(sqlQuery);
			pstmt = conn.prepareStatement(sqlQuery);
			pstmt.setString(1, toAddress);
			pstmt.setString(2, emailSubject);
			pstmt.setString(3, emailFrom);
			pstmt.setString(4, emailMessage.toString());
			//pstmt.setStringForClob(4, emailMessage.toString());
			pstmt.setString(5, sentMailStatus);
			pstmt.setString(6, emailType);
			pstmt.setString(7, simpledateformat.format(date));
			if(bcc==null){
				pstmt.setString(8, "");
			}else{
				pstmt.setString(8, bcc);
			}
			pstmt.executeUpdate();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		} 
		return success;
	}
	
	public static boolean checkForUserName(String userName)
	{
		boolean isUser= false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		
	    try
	    {
	    	conn = ConnectionManager.getDBConnection();
			String sql = "select user_id from cimm_users where upper(user_name)=? and upper(user_type)='ECOMM' and site_id=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName.toUpperCase());
			pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
			
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				isUser = true;
			}
	    }
	    catch(SQLException e)
	    {
	    	isUser =true;
	    	e.printStackTrace();
	    }
	    catch(Exception e)
	    {
	    	isUser =true;
	    	e.printStackTrace();
	    }
	    finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
		
		return isUser;
	}
	
	public static boolean checkForUserNameUnderCustomer(String userName,String companyId)
	{
		boolean isUser= false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		
	    try
	    {
	    	conn = ConnectionManager.getDBConnection();
			String sql = "select user_id from cimm_users where upper(user_name)=? and ECLIPSE_CONTACT_ID=? and upper(user_type)='ECOMM' and site_id=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName.toUpperCase());
			pstmt.setString(2, companyId);
			pstmt.setInt(3, CommonDBQuery.getGlobalSiteId());
			
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				isUser = true;
			}
	    }
	    catch(SQLException e)
	    {
	    	isUser =true;
	    	e.printStackTrace();
	    }
	    catch(Exception e)
	    {
	    	isUser =true;
	    	e.printStackTrace();
	    }
	    finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
		
		return isUser;
	}
	
	public static boolean checkForUsersCustomerId(String customerNumber){
		boolean isUser= false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		
	    try
	    {
	    	conn = ConnectionManager.getDBConnection();
			//String sql = "select user_id from cimm_users where upper(ECLIPSE_CONTACT_ID)=?";
			String sql = "SELECT BUYING_COMPANY_ID, ENTITY_ID,CUSTOMER_NAME FROM BUYING_COMPANY WHERE STATUS = 'A' AND UPPER(ENTITY_ID) = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, customerNumber.toUpperCase().trim());
			
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				isUser = true;
			}
	    }
	    catch(SQLException e)
	    {
	    	isUser =true;
	    	e.printStackTrace();
	    }
	    catch(Exception e)
	    {
	    	isUser =true;
	    	e.printStackTrace();
	    }
	    finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
		return isUser;
	}
	
	public static UsersModel getDefaultBillingAndShipping(int buyingCompanyId){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		UsersModel uModel = new UsersModel();
		
	    try{
	    	conn = ConnectionManager.getDBConnection();
			String sql = "SELECT BC_ADDRESS_BOOK_ID, ADDRESS_TYPE FROM BC_ADDRESS_BOOK WHERE STATUS = 'A' AND DEFAULT_FOR_ALL = 'Y' AND BUYING_COMPANY_ID =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, buyingCompanyId);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				if(rs.getString("ADDRESS_TYPE")!=null && rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill")){
				uModel.setBillToId(rs.getString("BC_ADDRESS_BOOK_ID"));
				}
				if(rs.getString("ADDRESS_TYPE")!=null && rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Ship")){
				uModel.setShipToId(rs.getString("BC_ADDRESS_BOOK_ID"));
				}
			}
	    }
	    catch(SQLException e)
	    {
	    	e.printStackTrace();
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
		return uModel;
	}

	public static int getBuyingCompanyIdByEntityId(String entityId){
		int companyId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
        try{
        	conn = ConnectionManager.getDBConnection();
        	//String sql = "select buying_company_id from buying_company where ENTITY_ID =?"; // 09 June 2014 coz of Gerrie Exes User Reg
			String sql = "select buying_company_id from buying_company where  STATUS= 'A' and ENTITY_ID =?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(entityId).toUpperCase());
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				companyId = rs.getInt("buying_company_id");
			}
        }catch(SQLException e) {
        	companyId =0;
        	e.printStackTrace();
        }catch(Exception e){
        	companyId =0;
        	e.printStackTrace();
        }
        finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
		return companyId;
	}
	
	
	public static int checkShipToIDForEntityIdAndBuyingCompanyId(String entityId, int buyingCompanyId, String addressType){
		int shipId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
        try{
        	conn = ConnectionManager.getDBConnection();
        	String sql = "SELECT BC_ADDRESS_BOOK_ID FROM BC_ADDRESS_BOOK WHERE STATUS = 'A' AND ENTITY_ID = ? AND BUYING_COMPANY_ID =?  AND ADDRESS_TYPE = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, entityId);
			pstmt.setInt(2, buyingCompanyId);
			pstmt.setString(3, addressType);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				shipId = rs.getInt("BC_ADDRESS_BOOK_ID");
			}
			
        }catch(SQLException e) {
        	shipId =0;
        	e.printStackTrace();
        }catch(Exception e){
        	shipId =0;
        	e.printStackTrace();
        }
        finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
		return shipId;
	}
	public static int updatePassword(int userId,String password){
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
	    int count = 0;
		try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("updatePassword");
            String securePassword = SecureData.getPunchoutSecurePassword(password);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, securePassword);
            pstmt.setInt(2, userId);
            count = pstmt.executeUpdate();
           
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBResultSet(rs);
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
    }
	return count;
	}
	
	public static LinkedHashMap<String, Object> agentListDAO(HttpSession session, LinkedHashMap<String, Object> contentObject){
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
	    
	    ArrayList<UsersModel> agentUserList = new ArrayList<UsersModel>();
	    UsersModel addressList = new UsersModel();
	    String listofAssigned = null;
	    String authSet = "0";
	    String buyingCompanyId = "0";
	    String isSuperUser = "N";
		String isAuthPurchaseAgent = "N";
		String authoListAssigned = "";
		String isAuthTechnician = "N";
		int approvalUserId = 0;
	    
		
		buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		
	 
	    
	    try {
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT C.*, BC.PHONE,CR.ROLE_NAME,C1.EMAIL APPROVER_EMAIL FROM CIMM_USERS C, BC_ADDRESS_BOOK BC, CIMM_ROLES CR, CIMM_USERS C1, CIMM_USER_ROLES CUR WHERE BC.BC_ADDRESS_BOOK_ID = C.DEFAULT_BILLING_ADDRESS_ID AND C.BUYING_COMPANY_ID =?  AND CUR.USER_ID(+) = C.USER_ID AND CR.ROLE_ID(+) = CUR.ROLE_ID AND C1.USER_ID(+) = C.APPROVAL_USER_ID";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, CommonUtility.validateNumber(buyingCompanyId));
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// USER_ID,USER_NAME,FIRST_NAME,LAST_NAME,EMAIL,ADDRESS1,CITY,STATE,ZIP,COUNTRY
				UsersModel temp = new UsersModel();
				temp.setUserId(rs.getInt("USER_ID"));
				temp.setUserName(rs.getString("USER_NAME"));
				temp.setFirstName(rs.getString("FIRST_NAME"));
				temp.setLastName(rs.getString("LAST_NAME"));
				temp.setEmailAddress(rs.getString("EMAIL"));
				temp.setPhoneNo(rs.getString("PHONE"));
				temp.setUserStatus(rs.getString("STATUS"));

				String roleName = rs.getString("ROLE_NAME");
				temp.setUserRole(roleName);
				if (roleName != null && roleName.trim().equalsIgnoreCase("Ecomm Customer Super User")) {
					temp.setIsSuperUser("Y");
				} else if (roleName != null && roleName.trim().equalsIgnoreCase("Ecomm Customer Auth Purchase Agent")) {
					temp.setIsAuthPurchaseAgent("Y");
				}

				if (rs.getInt("APPROVAL_USER_ID") > 0) {
					temp.setApproverAgentAssignStatus("Y");
					temp.setApproverEmail(rs.getString("APPROVER_EMAIL"));
				}
				else if(roleName!=null && CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE")!=null && CommonUtility.validateString(roleName).equalsIgnoreCase(CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE").trim()))
				{
					temp.setIsSpecialUser("Y");
				}
				
				if(rs.getInt("APPROVAL_USER_ID")>0)
				{
					temp.setApproverAgentAssignStatus("Y");
					temp.setApproverEmail(rs.getString("APPROVER_EMAIL"));
				}
				listofAssigned = UsersDAO.getListAssignedAPAtoUser(Integer.toString(rs.getInt("USER_ID")));
				if (listofAssigned.equalsIgnoreCase("0")){
					authoListAssigned=listofAssigned;
					temp.setIsBusyUser("0");
				}else{
					temp.setIsBusyUser("1");
					authoListAssigned="1";
				}
				agentUserList.add(temp);
	}
			contentObject.put("loggedInUserId", (String) session.getAttribute(Global.USERID_KEY));
			contentObject.put("agentUserList", agentUserList);
			contentObject.put("buyingCompanyId", buyingCompanyId);
			contentObject.put("addressList", addressList);
			contentObject.put("isSuperUser", isSuperUser);
			contentObject.put("isAuthPurchaseAgent", isAuthPurchaseAgent);
			contentObject.put("isAuthTechnician", isAuthTechnician);
			contentObject.put("authSet", authSet);
			contentObject.put("approvalUserId", approvalUserId);
			contentObject.put("authoListAssigned", authoListAssigned);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
		  ConnectionManager.closeDBResultSet(rs);
	  	  ConnectionManager.closeDBPreparedStatement(pstmt);	
	  	  ConnectionManager.closeDBConnection(conn);
		}
		return contentObject;
		
	}
	public static int getBcAddressBookIdFromShipToId(int buyingCompanyId, String shipToId){
		int bcAddressBookId = 0;
		Connection  conn = null;
		PreparedStatement pstmt = null;   
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getBcAddressBookIdFromShipToId");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, shipToId);
			pstmt.setInt(2, buyingCompanyId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				bcAddressBookId = rs.getInt("BC_ADDRESS_BOOK_ID");
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
        }
		return bcAddressBookId;
	}


	
	public static LinkedHashMap<String, Object> manageAgentDAO(int userId, HttpSession session, LinkedHashMap<String, Object> contentObject){
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
	    
	
	   
	    ArrayList<UsersModel> agentUserList = new ArrayList<UsersModel>();
	    UsersModel addressList = new UsersModel();
	    String listofAssigned = null;
	    String authSet = "0";
	    String buyingCompanyId = "0";
	    String userRole = "";
	    String isSuperUser = "N";
		String isAuthPurchaseAgent = "N";
		String isGeneralUser = "N";
		String isSpecialUser = "N";
		String isAuthTechnician	="N";
		String authoListAssigned = "";
		int approvalUserId = 0;
		
	    try {
	    	conn = ConnectionManager.getDBConnection();
	    	buyingCompanyId = (String)session.getAttribute("buyingCompanyId");
	   	 	addressList= getEntityDetailsByUserId(userId); 

	   		userRole = getUserRole(userId);
	   		ArrayList<UsersModel> approverList = UsersDAO.getApprovalUserDetailList(userId);
	   		ArrayList<UsersModel> listOfAssignedGeneralUserList = UsersDAO.getListOfAssignedGeneralUserList(userId);  
			/* ArrayList<Integer> approverList = UsersDAO.getApprovalUserList(userId); */
	   		
	   		if(userRole!=null && userRole.trim().equalsIgnoreCase("Ecomm Customer Super User")){
	   			isSuperUser = "Y";
	   		}else if(userRole!=null && userRole.trim().equalsIgnoreCase("Ecomm Customer Auth Purchase Agent")){
	   			isAuthPurchaseAgent = "Y";
	   		}else if(userRole!=null && userRole.equalsIgnoreCase("Ecomm Auth Technician")){
				isAuthTechnician = "Y";
			}else if(userRole!=null && userRole.trim().equalsIgnoreCase("Ecomm Customer General User")){
	   			isGeneralUser = "Y";
	   		}else if(userRole!=null && CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE")!=null && CommonUtility.validateString(userRole).equalsIgnoreCase(CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE").trim())){
	   			isSpecialUser = "Y";
	   		}
	   		
	   		String sql = "SELECT C.*, BC.PHONE,CR.ROLE_NAME,C1.EMAIL APPROVER_EMAIL,CUR.APPROVE_LIMIT,CUR.ALWAYS_APPROVER FROM CIMM_USERS C, BC_ADDRESS_BOOK BC, CIMM_ROLES CR, CIMM_USERS C1, CIMM_USER_ROLES CUR WHERE BC.BC_ADDRESS_BOOK_ID = C.DEFAULT_BILLING_ADDRESS_ID AND C.BUYING_COMPANY_ID =? AND C.STATUS = 'Y' AND CUR.USER_ID(+) = C.USER_ID AND CR.ROLE_ID(+) = CUR.ROLE_ID AND C1.USER_ID(+) = C.APPROVAL_USER_ID";
	   		
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, CommonUtility.validateNumber(buyingCompanyId)); 
			rs=pstmt.executeQuery();
			
			 while(rs.next()){
				 if(rs.getInt("USER_ID") == userId){
					 if(rs.getString("APPROVE_LIMIT") != null){
						 addressList.setApproveLimit(rs.getString("APPROVE_LIMIT"));
						}
					 if(rs.getString("ALWAYS_APPROVER") != null){
						 addressList.setAlwaysApprover(rs.getString("ALWAYS_APPROVER"));
						}
				 }
				


				if(rs.getInt("USER_ID")!=userId){
					UsersModel temp = new UsersModel();
					temp.setUserId(rs.getInt("USER_ID"));
					temp.setUserName(rs.getString("USER_NAME"));
					temp.setFirstName(rs.getString("FIRST_NAME"));
					temp.setLastName(rs.getString("LAST_NAME")); 
					temp.setEmailAddress(rs.getString("EMAIL"));
					temp.setPhoneNo(rs.getString("CELL_PHONE"));
					String roleName = rs.getString("ROLE_NAME");
					temp.setApproverEmail(rs.getString("APPROVER_EMAIL"));
					if(rs.getString("APPROVE_LIMIT") != null){
						temp.setApproveLimit(rs.getString("APPROVE_LIMIT"));
					}
					 if(approverList!=null && approverList.size()>0){
							
							for(UsersModel userModel : approverList){
								if(userModel.getUserId() == temp.getUserId()){
								temp.setApproverAgentAssignStatus("Y");								
								temp.setApproverSequence(CommonUtility.validateString(userModel.getApproverSequence()).length() > 0?userModel.getApproverSequence():"");
								authSet = Integer.toString(userId);
								}
							}
						}
					if(roleName!=null && roleName.trim().equalsIgnoreCase("Ecomm Customer Super User")){
						temp.setIsSuperUser("Y");
					}else if(roleName!=null && roleName.trim().equalsIgnoreCase("Ecomm Customer Auth Purchase Agent")){
						temp.setIsAuthPurchaseAgent("Y");
					}

					if(approverList!=null && approverList.size()>0){
						if(approverList.contains(temp.getUserId())){
							temp.setApproverAgentAssignStatus("Y");
							temp.setApproverAgentAssignStatus("Y");
							authSet = Integer.toString(userId);
						}
					}
					
					if(temp.getUserId()==userId){
						approvalUserId = rs.getInt("APPROVAL_USER_ID");
					}
					agentUserList.add(temp);
				}
			}
			
			listofAssigned = getListAssignedAPAtoUser(Integer.toString(userId));
			if (listofAssigned.equalsIgnoreCase("0")){
				authoListAssigned = listofAssigned;
			}else{
				authoListAssigned="1";
			}
			
			ArrayList<UsersModel> shipAddressList = new ArrayList<UsersModel>();
			ArrayList<UsersModel> jobList = new ArrayList<UsersModel>();
			//HashMap<String, ArrayList<UsersModel>> templist = getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId), userId);
			HashMap<String, ArrayList<UsersModel>> templist = getAllAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId));
			shipAddressList = templist.get("Ship");
			//Service
			UnilogFactoryInterface serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
			if(serviceClass!=null && shipAddressList!=null) {
				ArrayList<UsersModel> shipList = serviceClass.addShipAddressList(shipAddressList);
				if(shipList!=null){
					shipAddressList=shipList;
				}
			}
			jobList=templist.get("JOB");
			
			ArrayList<Integer> assignedShipIdList = new ArrayList<Integer>();
			assignedShipIdList = getAssignedShipToList(String.valueOf(userId));
		
			contentObject.put("agentUserList", agentUserList);
			contentObject.put("buyingCompanyId", buyingCompanyId);
			contentObject.put("addressList", addressList);
			contentObject.put("isSuperUser", isSuperUser);
			contentObject.put("isAuthPurchaseAgent", isAuthPurchaseAgent);
			contentObject.put("isGeneralUser", isGeneralUser);
			contentObject.put("isAuthTechnician",isAuthTechnician);
			contentObject.put("isSpecialUser", isSpecialUser);
			contentObject.put("thisUserId", userId);
			contentObject.put("authSet", authSet);
			contentObject.put("approvalUserId", approvalUserId);
			contentObject.put("authoListAssigned", authoListAssigned);
			contentObject.put("shipAddressList", shipAddressList);
			contentObject.put("currUserRole", userRole);
			contentObject.put("assignedShipIdList", assignedShipIdList);
			contentObject.put("jobList", jobList);
			contentObject.put("listOfAssignedGeneralUserList", listOfAssignedGeneralUserList);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
		  ConnectionManager.closeDBResultSet(rs);
	  	  ConnectionManager.closeDBPreparedStatement(pstmt);	
	  	  ConnectionManager.closeDBConnection(conn);
		}
		return contentObject;
	}
	
	public static ArrayList<UsersModel> getApprovalUserDetailList(int userId)
	{
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = PropertyAction.SqlContainer.get("getApprovalUserList");
		ArrayList<UsersModel> approverList = new ArrayList<UsersModel>();
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				UsersModel usermodelObject = new UsersModel();
				usermodelObject.setUserId(rs.getInt("APPROVER_ID"));
				usermodelObject.setApproverSequence(rs.getString("APPROVER_SEQUENCE"));
				approverList.add(usermodelObject);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
			
		}
	return approverList;
		
	}

	
	public static LinkedHashMap<String, Object> disableListDAO(int userId, HttpSession session, LinkedHashMap<String, Object> contentObject){
		
		String listofAssigned=null;
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ArrayList<UsersModel> agentUserList = new ArrayList<UsersModel>();
	    String authoListAssigned = "";
	    String buyingCompanyId = "0";
	   
	  
	    try {
	    	conn = ConnectionManager.getDBConnection();
	    	buyingCompanyId = (String) session.getAttribute("buyingCompanyId"); 
	    	String sql = "SELECT * FROM CIMM_USERS CU, CIMM_USER_ROLES CUR,CIMM_ROLES CR WHERE BUYING_COMPANY_ID =? AND CU.STATUS = 'Y' AND CUR.USER_ID = CU.USER_ID AND CR.ROLE_NAME='Ecomm Customer Auth Purchase Agent' AND CR.ROLE_ID = CUR.ROLE_ID";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, CommonUtility.validateNumber(buyingCompanyId)); 
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				//USER_ID,USER_NAME,FIRST_NAME,LAST_NAME,EMAIL,ADDRESS1,CITY,STATE,ZIP,COUNTRY
				UsersModel temp = new UsersModel();
				temp.setUserId(rs.getInt("USER_ID"));
				temp.setUserName(rs.getString("USER_NAME"));
				temp.setFirstName(rs.getString("FIRST_NAME"));
				temp.setLastName(rs.getString("LAST_NAME")); 
				temp.setEmailAddress(rs.getString("EMAIL"));
				String isAPAgent = "N";
				if(rs.getString("ROLE_NAME")!=null && rs.getString("ROLE_NAME").trim().equalsIgnoreCase("Ecomm Customer Auth Purchase Agent"))
					isAPAgent = "Y";
				if(isAPAgent.equalsIgnoreCase("Y")){
					temp.setIsAuthPurchaseAgent("Y");
					listofAssigned = getListAssignedAPAtoUser(Integer.toString(rs.getInt("USER_ID")));
					if (listofAssigned.equalsIgnoreCase("0")){
						authoListAssigned=listofAssigned;
						agentUserList.add(temp);
					}
					
				}
				
			}
			
			contentObject.put("agentUserList", agentUserList);
			contentObject.put("buyingCompanyId", buyingCompanyId);
			contentObject.put("userId", userId);
			contentObject.put("authoListAssigned", authoListAssigned);
	   				
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
		  ConnectionManager.closeDBResultSet(rs);
	  	  ConnectionManager.closeDBPreparedStatement(pstmt);	
	  	  ConnectionManager.closeDBConnection(conn);
		}
		return contentObject;
	}
	public static ArrayList<UsersModel> getListOfAssignedGeneralUserList(int userId)
	{
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = PropertyAction.SqlContainer.get("getListOfAssignedGeneralUserList");
		ArrayList<UsersModel> approverList = new ArrayList<UsersModel>();
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				UsersModel approverGeneralUserList = new UsersModel();
				
				approverGeneralUserList.setFirstName(rs.getString("FIRST_NAME"));
				approverGeneralUserList.setLastName(rs.getString("LAST_NAME"));
				approverGeneralUserList.setUserId(rs.getInt("USER_ID"));
				approverGeneralUserList.setEmailAddress(rs.getString("EMAIL"));
				approverGeneralUserList.setAddress1(rs.getString("ADDRESS1"));
				approverGeneralUserList.setAddress2(rs.getString("ADDRESS2"));
				approverGeneralUserList.setCity(rs.getString("CITY"));
				approverGeneralUserList.setState(rs.getString("STATE"));
				approverGeneralUserList.setZipCodeStringFormat(rs.getString("ZIP"));
				approverList.add(approverGeneralUserList);
				/*approverList.add(rs.getInt("APPROVER_ID"));*/
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
			
		}
	return approverList;
		
	}

	
	public static String disableAgentDAO(int userId){
		String result = "Unable to disable"; 
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
	    int count = 0;
	    
	   
	    
	    try{
	    	conn = ConnectionManager.getDBConnection();
	    	String sql = PropertyAction.SqlContainer.get("disableUser");
	    	pstmt = conn.prepareStatement(sql);
	    	pstmt.setInt(1, userId);
	    	count=pstmt.executeUpdate();
	    	if(count>0){
	    		result = "Successfully disabled";
	    	}else{
	    		result = "Problem faced in disabling the user, Please try after sometime!";
	    	}
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally{
		  ConnectionManager.closeDBResultSet(rs);
	  	  ConnectionManager.closeDBPreparedStatement(pstmt);	
	  	  ConnectionManager.closeDBConnection(conn);
		}
	    return result;
	}
	
	public static String enableAgentDAO(int userId){
		String result = "Unable to enable"; 
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
	    int count = 0;
	    
	   
	    
	    try{
	    	conn = ConnectionManager.getDBConnection();
	    	String sql = PropertyAction.SqlContainer.get("enableUser");
	    	pstmt = conn.prepareStatement(sql);
	    	pstmt.setInt(1, userId);
	    	count=pstmt.executeUpdate();
	    	if(count>0){
	    		result = "Successfully enabled";
	    	}else{
	    		result = "Problem faced in disabling the user, Please try after sometime!";
	    	}
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally{
		  ConnectionManager.closeDBResultSet(rs);
	  	  ConnectionManager.closeDBPreparedStatement(pstmt);	
	  	  ConnectionManager.closeDBConnection(conn);
		}
	    return result;
	}
	
	public String assignShipToForAgentDAO(HttpServletRequest request, String assignedNewId, int userId, ArrayList<Integer> shipIdList, String addressBId[], String enId[], String assignedList, String currentSUState, 
			String userPermission,String assignedId,String approveLimt,String alwaysApprover,String approverSequence){
		
		String result="1|";
		HttpSession session = request.getSession();
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    boolean shippingAddressRemoveFlag = false;
	    boolean flag = false;
	    ArrayList<String> status = new ArrayList<String>();
	    ArrayList<UsersModel> assignedShipList = new ArrayList<UsersModel>();
	    ArrayList<UsersModel> removedShipList = new ArrayList<UsersModel>(); 
	    
	    String buyingCompanyId = (String)session.getAttribute("buyingCompanyId");
	   
	    int begin = assignedList.indexOf('[');
	    int end = assignedList.indexOf(']');
	    assignedList = assignedList.substring(begin+1, end);
	    String[] alist = assignedList.split(",");
	    
	    HashMap<String, ArrayList<UsersModel>> templist = UsersDAO.getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId),CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY)));
	    ArrayList<UsersModel> shipAddressList = templist.get("Ship");
	    ArrayList<UsersModel> jobAddressList = templist.get("JOB");
		
		UsersModel user = UsersDAO.getEntityDetailsByUserId(userId);
		boolean insertFlag = true;

		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		
		int parentUserId = CommonUtility.validateNumber(sessionUserId);
		
	   
	    
	    try{
	    	conn = ConnectionManager.getDBConnection();
	    	/*---SUPER USER---*/
	    	UserManagement userManagement = new UserManagementImpl();
	    	UsersModel userInfo = new UsersModel();
	    	
	    	if(userPermission!=null && userPermission.trim().equalsIgnoreCase("superUser"))
	    		userPermission = "Ecomm Customer Super User";
	    	else if(userPermission!=null && userPermission.trim().equalsIgnoreCase("purchaseAgent"))
	    		userPermission = "Ecomm Customer Auth Purchase Agent";
	    	else if(userPermission!=null && userPermission.trim().equalsIgnoreCase("AuthTechnician"))
	    		userPermission = "Ecomm Auth Technician";
	    	else if(userPermission!=null && CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE")!=null && CommonUtility.validateString(userPermission).equalsIgnoreCase(CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE").trim()))
	    		userPermission = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE").trim());
	    	else
	    		userPermission = "Ecomm Customer General User";
	    	
	    	
	    	 String userRole = UsersDAO.getUserRole(userId);
			 if(userRole==null){
				 UsersDAO.assignRoleToUser(conn, String.valueOf(userId), "Ecomm Customer General User");
				 userRole = "Ecomm Customer General User";
			 }
			 
			 
			 if(!userRole.trim().equalsIgnoreCase(userPermission) || CommonUtility.validateString(approveLimt).length() >0 || ( CommonUtility.validateString(alwaysApprover).length()>0 || userPermission.trim().equalsIgnoreCase("Ecomm Customer General User"))){
				 String superUser = "No";
				 
				 /* if(userRole.trim().equalsIgnoreCase("Ecomm Customer Super User") && !userPermission.trim().equalsIgnoreCase("Ecomm Customer Super User")){
					 
					 superUser = "No";
					 String userName = "";
					 String sql = "select USER_NAME FROM CIMM_USERS WHERE USER_ID=?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, userId);
						rs = pstmt.executeQuery();
						while(rs.next()){
							userName=rs.getString("USER_NAME");
						ConnectionManager.closeDBResultSet(rs);
						}
						ConnectionManager.closeDBPreparedStatement(pstmt);
						userInfo.setUserRole(superUser);
						userInfo.setUserName(userName);
						userManagement.updatePrevilege(userInfo);
						
				 }
				 
				 else if(!userRole.trim().equalsIgnoreCase("Ecomm Customer Super User") && userPermission.trim().equalsIgnoreCase("Ecomm Customer Super User")){
					
					 superUser = "Yes";
					 String userName = "";
					 String sql = "select USER_NAME FROM CIMM_USERS WHERE USER_ID=?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, userId);
						rs = pstmt.executeQuery();
						while(rs.next()){
							userName=rs.getString("USER_NAME");
						}
						ConnectionManager.closeDBResultSet(rs);
						ConnectionManager.closeDBPreparedStatement(pstmt);
						userInfo.setUserRole(superUser);
						userInfo.setUserName(userName);
						userManagement.updatePrevilege(userInfo);
				 }
				*/ 
				 if(userRole.trim().equalsIgnoreCase("Ecomm Customer Super User") && userPermission.trim().equalsIgnoreCase("Ecomm Customer Super User")){
					 superUser = "Yes";
				 }

				 if(!userPermission.trim().equalsIgnoreCase("Ecomm Customer General User")){
					 UsersDAO.assignApproverIntoDB(parentUserId,new ArrayList<UsersModel>(), userId);
				 }
				 
				 UsersDAO.checkAndUpdateRoleToUser(Integer.toString(userId), superUser, userPermission);
				 UsersDAO.updateBudget(Integer.toString(userId), superUser, userPermission,approveLimt,alwaysApprover);
				 
				 String userPermissionForDisplay = "";
				 if(userPermission!=null && userPermission.trim().length()>0){
					 userPermissionForDisplay = userPermission.replaceAll("Auth", "Authorized");
				 }
				 if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.emailcontent")).length()>0){
                     status.add(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.emailcontent") +" "+ userPermissionForDisplay + ".");
                   }else{
                        status.add("You have been assigned as " + userPermissionForDisplay + ".");
                    }
				 result = result+"User assigned as "+userPermissionForDisplay+".|";
				
			 }
			 
			 
			/*---PURCHASE AGENT---*/
//			if(!CommonUtility.validateString(assignedNewId).equals(CommonUtility.validateString(assignedId))){
//	    		ArrayList<Integer> approverListId = new ArrayList<Integer>();
//	    		approverListId.add(CommonUtility.validateNumber(assignedNewId));
//	    		
//				int count = UsersDAO.assignApprover(parentUserId, approverListId, userId);
//	    		if(count > 0){
//	    			status.add(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("purchaseagent.status.purchaseagentadded"));
//	        		result = result+"Purchase Agent Assigned.|";
//	        	}
//	    	}
	    	
			if(assignedNewId !=null ){
			 	
			 	String[] assignedNewIdList = assignedNewId.split(",");
	    		List<String> assignedNewIdArrayList = Arrays.asList(assignedNewIdList);
	    		ArrayList<UsersModel> approverListId = new ArrayList<UsersModel>();
	    		for(String apaId : assignedNewIdArrayList){
	    			apaId = CommonUtility.validateString(apaId);
	    			if(CommonUtility.validateString(apaId).contains("~")){
	    				UsersModel approverList = new  UsersModel();
		    			approverList.setUserId(CommonUtility.validateNumber(apaId.split("~")[0].trim()));
		    			approverList.setApproverSequence(CommonUtility.validateString(apaId.split("~")[1].trim()));
		    			approverListId.add(approverList);
	    			}else{
	    				if(CommonUtility.validateString(apaId).length()  > 0){
		    				UsersModel approverList = new  UsersModel();
			    			approverList.setUserId(CommonUtility.validateNumber(apaId));
			    			approverList.setApproverSequence("");
			    			approverListId.add(approverList);
	    				}
	    			}
	    		}
	    		
	    		/*String[] approverSequenceList = approverSequence.split(",");
	    		List<String> approverSequenceValues = Arrays.asList(approverSequenceList);
	    		for(String appSeq : approverSequenceValues){
	    			
	    			for(UsersModel userObject : approverListId){
	    				int approverId = CommonUtility.validateNumber(appSeq.split("~")[0]);
	    				if(userObject.getUserId() == approverId){
	    					userObject.setApproverSequence(appSeq.split("~")[1]);
	    				}
	    			}
	    		}*/
	    		
	    		
	    		
				int count = UsersDAO.assignApproverIntoDB(parentUserId, approverListId, userId);
	    		if(count > 0){
	    			status.add(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("purchaseagent.status.purchaseagentadded"));
	        		result = result+"Purchase Agent Assigned.|";
	        	}
	    	}
	    	

	    	/*---SHIP TO---*/
	    	HashMap<String,Integer> defaultAddresssId = UsersDAO.getDefaultAddressId(userId);
	    	int currentDefaultId = defaultAddresssId.get("Ship");
	    	boolean defaultIdExist = false;
	    	
	    	if(shipIdList!=null && shipIdList.size()>0) {
	    		for(int i=0;i<shipIdList.size();i++){
			    	if(currentDefaultId==shipIdList.get(i)){
						defaultIdExist = true;
						break;
					}
		    	}
	    	}else{
	    		defaultIdExist = true;
	    	}
	    	
	    	if(!defaultIdExist){
	   			int count = -1;
	   			if(shipIdList!=null && shipIdList.size()>0) {
	   				count = UsersDAO.updateDefaultShipTo(userId,shipIdList.get(0));
	   			}
	    	}
	 
	    	
	    	String sql = "INSERT INTO USER_SHIPTO ( USER_SHIPTO_ID,ENTITY_ID,CIMM_USER_ID,BC_ADDRESS_BOOKID) VALUES (?,?,?,?)";
	    	if(addressBId!=null && addressBId.length>0 && shipIdList!=null &&  insertFlag ){
	    		int delCnt = deleteUserShipAddress(userId);
			    		
	    			for(int i=0;i<addressBId.length;i++){
	    				
			    			for(int j=0;j<shipIdList.size();j++){
			    				
			    					if(shipIdList.get(j)==CommonUtility.validateNumber(addressBId[i])){
			    						
			    					int userShipToId = CommonDBQuery.getSequenceId("USER_SHIPTO_ID_SEQ");
			    					pstmt=conn.prepareStatement(sql);
			    					pstmt.setInt(1, userShipToId);
			    					pstmt.setInt(2, CommonUtility.validateNumber(enId[i]));
			    					pstmt.setInt(3, userId);
			    					pstmt.setInt(4, CommonUtility.validateNumber(addressBId[i]));
			    					int count = pstmt.executeUpdate();
			    					ConnectionManager.closeDBResultSet(rs);
			    			    	ConnectionManager.closeDBPreparedStatement(pstmt);
			    			    	if(count>0){
			    			    		flag = true;
			    			    		if(status!=null && !status.contains("Shipping Address assigned.")){
											status.add("Shipping Address assigned.");
										}
			    			    	}
			    			    	
			    			    	for(UsersModel address : shipAddressList)
			    			    	{
			    			    	 
			    			    	if(address.getAddressBookId() == shipIdList.get(j))
			    			    	{
			    			    		assignedShipList.add(address);
			    			    		break;
			    			    	
			    			    	}
			    			    	}
			    			    	
			    				}else{
			    					
			    					
			    				}
			    					
			    			}
			    		}
	    			
	    			if(assignedList!=null && assignedList.length()>0)
					{
						for(String  assignedShipId : alist)
						{
							if(shipIdList!=null && shipIdList.contains(CommonUtility.validateNumber(assignedShipId.trim())))
							{
								System.out.println("do nothing");
							}
							else
							{
								UsersDAO.removeAssignedShip(String.valueOf(userId),String.valueOf(assignedShipId.trim()));
								for(UsersModel removeAddress : shipAddressList)
						    	{
						    	 
						    	if(removeAddress.getAddressBookId() == CommonUtility.validateNumber(assignedShipId.trim()))
						    	{
									removedShipList.add(removeAddress);
									//result = result+"Shipping Address Removed.|";
									if(status!=null && !status.contains("Job Account assigned.")){
										status.add("Job Account assigned.");
									}else{
										status.add("Shipping Address Removed.");
									}
									shippingAddressRemoveFlag = true;
									break;
						    	}
						    	}
							}
						}
					}
	    			

	    	}else if(shipIdList == null && shipAddressList != null){
	    		
	    		if(assignedList!=null){
	    			for(String  assignedShipId : alist){
	     					UsersDAO.removeAssignedShip(String.valueOf(userId),String.valueOf(assignedShipId));
	    					for(UsersModel removeAddress : shipAddressList){
	    				    	  
	    						if(assignedShipId!=null && !assignedShipId.toString().trim().equalsIgnoreCase("")){
		    				    	if(removeAddress.getAddressBookId() == CommonUtility.validateNumber(assignedShipId.trim().toString())){
			    						removedShipList.add(removeAddress);
			    						//result = result + "Ship to address removed for the user.|";
			    						shippingAddressRemoveFlag = true;
		    				    	}
	    				    	}
	    				   }
	     			}
	    		}
	    	
	    	}
	    	if(flag){
	    		result = result+"Shipping address assigned.";
	    	}
	    	
	    	if(shippingAddressRemoveFlag){
	    		result = result+"Ship to address removed for the user.";
	    	}
		if(result != "1|"){ 
			System.out.println("Updation successful");
			SendMailUtility sendMailObj = new SendMailUtility();
			boolean flagsent = sendMailObj.sendChangedPrivilegsMail(user.getEmailAddress(),user.getFirstName(), user.getLastName(), status, assignedShipList, removedShipList);
			
			if(flagsent){
				System.out.println("mail sent successfully");
				
			}
			
		}else if(result == "1|"){
			result = "0|Updation Unsuccessful, Please try again later.";
		}
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    finally{
	    	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
	    }
		
		return result;
	}
//	naveen
public static int updateBudget(String sessionUserId,String isSuperUser,String userRole,String approveLimt,String alwaysApprover){
		
		int count = 0;
		
		/*String roleName = "";
		boolean updateUserRole = false;*/
	try
	{
		/*if(isSuperUser == null)
		{
			roleName = "Ecomm Customer Auth Purchase Agent";
			updateUserRole = true;
		}
		else if(userRole.equalsIgnoreCase("Ecomm Customer Auth Purchase Agent") && isSuperUser!=null && isSuperUser.equalsIgnoreCase("Yes"))
		{
			roleName = "Ecomm Customer Super User";
			updateUserRole = true;
		}
		else if(userRole.equalsIgnoreCase("Ecomm Customer Super User") && isSuperUser!=null && isSuperUser.equalsIgnoreCase("No"))
		{
			roleName = "Ecomm Customer Auth Purchase Agent";
			updateUserRole = true;
		}
		else if(userRole.equalsIgnoreCase("Ecomm Customer General User") && isSuperUser!=null && isSuperUser.equalsIgnoreCase("Yes"))
		{
			roleName = "Ecomm Customer Super User";
			updateUserRole = true;
		}
		else
		{
			roleName = userRole;
			updateUserRole = true;
		}
		
		if(updateUserRole)*/
			count = addBudget(sessionUserId, userRole,approveLimt,alwaysApprover);
			if(count >=1){
				System.out.println("Budget Updated Successfully.");
			}else{
				System.out.println("Budget Update Failed.");
			}
		System.out.println("");
	}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
		return count;
		
	}

public static int  addBudget(String sessionUserId,String roleName,String approveLimt,String alwaysApprover){
	Connection conn = null;
	PreparedStatement pstmt = null;
	String sql = "UPDATE CIMM_USER_ROLES SET ROLE_ID = (SELECT ROLE_ID FROM CIMM_ROLES WHERE ROLE_NAME = ?), APPROVE_LIMIT=?,ALWAYS_APPROVER=? WHERE USER_ID = ?";
	// UPDATE CIMM_USER_ROLES SET ROLE_ID = (SELECT ROLE_ID FROM CIMM_ROLES WHERE ROLE_NAME = ?) WHERE USER_ID = ?
	int count = 0;
	try
	{
		
		if(roleName.equals("Ecomm Customer General User")){
			approveLimt ="";
		}
		conn = ConnectionManager.getDBConnection();
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, roleName);
		pstmt.setString(2, approveLimt);
		if(CommonUtility.validateString(alwaysApprover).length()>0){
			pstmt.setString(3, alwaysApprover);
		}else{
			pstmt.setString(3, "");
		}
		
		pstmt.setInt(4, CommonUtility.validateNumber(sessionUserId));
		
		count = pstmt.executeUpdate();
		if(count > 0){
			System.out.println("Budeget Updated for" + sessionUserId);
		}else{
			System.out.println("Failed to updated budeget for" + sessionUserId);
		}
		
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	finally
	{
		ConnectionManager.closeDBPreparedStatement(pstmt);
		ConnectionManager.closeDBConnection(conn);
		
	}

	return count;
}

public static int assignApproverIntoDB(int userEdited,ArrayList<UsersModel> approveIdList,int userId){
		
		String sql = "INSERT INTO CIMM_USERS_APPROVERS(CIMM_USER_APPROVER_ID,USER_ID,APPROVER_ID,USER_EDITED,UPDATED_DATETIME,APPROVER_SEQUENCE) VALUES(CIMM_USER_APPROVER_ID_SEQ.NEXTVAL,?,?,?,SYSDATE,?)";
		String deleteQuery = "DELETE FROM CIMM_USERS_APPROVERS WHERE USER_ID = ?";
		
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try
		{
			 conn = ConnectionManager.getDBConnection();
			 
			 
			 pstmt = conn.prepareStatement(deleteQuery);
			 pstmt.setInt(1, userId);
			 count = pstmt.executeUpdate();
			 pstmt.close();
			 //ArrayList<UsersModel> um = new ArrayList<UsersModel>();
			 
			 if(approveIdList!=null && approveIdList.size()>0)
			 {
				 for(UsersModel approvalUserId:approveIdList)
				 {

					 pstmt = conn.prepareStatement(sql);
					 pstmt.setInt(1, userId);
					 pstmt.setInt(2, approvalUserId.getUserId());
					 pstmt.setInt(3, userEdited);
					 if(CommonUtility.validateString(approvalUserId.getApproverSequence()).length()>0){
						 pstmt.setString(4, approvalUserId.getApproverSequence());
					 }else{
					 pstmt.setString(4, "");
					 }
					 
					 count = pstmt.executeUpdate();
					 ConnectionManager.closeDBPreparedStatement(pstmt);
				 }
				 
			 }
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		
		return count;
	}


	public static int assignApprover(int userEdited,ArrayList<Integer> approveIdList,int userId){
		
		String sql = "INSERT INTO CIMM_USERS_APPROVERS(CIMM_USER_APPROVER_ID,USER_ID,APPROVER_ID,USER_EDITED,UPDATED_DATETIME) VALUES(CIMM_USER_APPROVER_ID_SEQ.NEXTVAL,?,?,?,SYSDATE)";
		String deleteQuery = "DELETE FROM CIMM_USERS_APPROVERS WHERE USER_ID = ?";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		userEdited = Integer.parseInt((String)session.getAttribute("validUser"));
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try
		{
			 conn = ConnectionManager.getDBConnection();
			 
			 
			 pstmt = conn.prepareStatement(deleteQuery);
			 pstmt.setInt(1, userId);
			 count = pstmt.executeUpdate();
			 pstmt.close();
			 if(approveIdList!=null && approveIdList.size()>0)
			 {
				 for(Integer approvalUserId:approveIdList)
				 {
					 pstmt = conn.prepareStatement(sql);
					 pstmt.setInt(1, userId);
					 pstmt.setInt(2, approvalUserId);
					 pstmt.setInt(3, userEdited);
					 count = pstmt.executeUpdate();
					 ConnectionManager.closeDBPreparedStatement(pstmt);
				 }
				 
			 }
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		
		return count;
	}
	public static int updateDefaultShipTo(int userId,int shipToId)
	{
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    int count = -1;
	   	try {
            conn = ConnectionManager.getDBConnection();
            String sql = "UPDATE CIMM_USERS SET DEFAULT_SHIPPING_ADDRESS_ID=?  WHERE USER_ID=?";
        	pstmt=conn.prepareStatement(sql);
        	pstmt.setInt(1, shipToId);
        	pstmt.setInt(2, userId);
            count = pstmt.executeUpdate();
            if(count>0){
            	System.out.println(shipToId+" : Default Ship Address Updated for userId : "+userId);
            }else{
            	System.out.println("Failed : "+shipToId+" : Default Ship Address Updated for userId : "+userId);
            }
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBResultSet(rs);
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		return count;
	}
	
	public int deleteUserShipAddress(int userId){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		int delCnt = 0;
			
		try{
			conn = ConnectionManager.getDBConnection();
			String deleteSql = "DELETE FROM USER_SHIPTO WHERE CIMM_USER_ID = ?";
	    	pstmt=conn.prepareStatement(deleteSql);
	    	pstmt.setInt(1, userId);
	    	delCnt = pstmt.executeUpdate();
	    	if(delCnt>0){
	    		System.out.println("ShipTo address deleted successfully");
	    	}
		}catch(Exception e){
	    	e.printStackTrace();
	    }
		finally{
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
	    }
	    return delCnt;
	}
	
	public static boolean removeAssignedShip(String userId,String shipId ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean removeFlag = false;
		String sql = " DELETE FROM USER_SHIPTO WHERE CIMM_USER_ID = ? AND BC_ADDRESS_BOOKID = ?";
		
		
		
		try{
			conn = ConnectionManager.getDBConnection();
		  pstmt = conn.prepareStatement(sql);
		  pstmt.setString(1, userId);
		  pstmt.setString(2,shipId);
		  
		  int count  = pstmt.executeUpdate();
		  if(count>0){
			  System.out.println("Assigned ship address has been removed successfully.");
			  removeFlag = true;
		  }
		}catch (Exception e) {
	       e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
	    return removeFlag;
	}
	
	public LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getStaticBanners(String id){
		
		String sql = "SELECT * FROM ( SELECT 'TOP' BANNER_POSITION, VL.SCROLLABLE, VL.DELAY, B.BANNER_IMAGE_NAME, B.IMAGE_TYPE, V.BANNER_LANDING_URL, VL.NUMOFIMGSTOSCROLL, VL.DIRECTION,V.DISP_SEQ FROM BANNERS B, VALUE_LIST_DATA V, VALUES_LIST VL, STATIC_PAGES SP WHERE B.BANNER_ID   = V.BANNER_ID AND V.VALUE_LIST_ID = VL.VALUE_LIST_ID AND V.VALUE_LIST_ID = SP.TOP_BANNER_ID AND SP.STATIC_PAGE_ID = ? AND SYSDATE BETWEEN (SELECT NVL2(EFFECTIVE_DATE, EFFECTIVE_DATE, SYSDATE-1) FROM VALUES_LIST WHERE VALUE_LIST_ID=((SELECT TOP_BANNER_ID FROM STATIC_PAGES WHERE STATIC_PAGE_ID=?))) AND (SELECT NVL2(END_DATE, END_DATE, SYSDATE+1)FROM VALUES_LIST WHERE VALUE_LIST_ID=((SELECT TOP_BANNER_ID FROM STATIC_PAGES WHERE STATIC_PAGE_ID=?))) UNION SELECT 'RIGHT' BANNER_POSITION, VL.SCROLLABLE,VL.DELAY,B.BANNER_IMAGE_NAME,B.IMAGE_TYPE,V.BANNER_LANDING_URL,VL.NUMOFIMGSTOSCROLL,VL.DIRECTION,V.DISP_SEQ FROM BANNERS B,VALUE_LIST_DATA V,VALUES_LIST VL,STATIC_PAGES SP WHERE B.BANNER_ID   = V.BANNER_ID AND V.VALUE_LIST_ID = VL.VALUE_LIST_ID AND V.VALUE_LIST_ID = SP.RIGHT_BANNER_ID AND SP.STATIC_PAGE_ID = ? AND SYSDATE BETWEEN (SELECT NVL2(EFFECTIVE_DATE, EFFECTIVE_DATE, SYSDATE-1) FROM VALUES_LIST WHERE VALUE_LIST_ID=((SELECT RIGHT_BANNER_ID FROM STATIC_PAGES WHERE STATIC_PAGE_ID=?))) AND (SELECT NVL2(END_DATE, END_DATE, SYSDATE+1) FROM VALUES_LIST WHERE VALUE_LIST_ID=((SELECT RIGHT_BANNER_ID FROM STATIC_PAGES WHERE STATIC_PAGE_ID=?))) UNION SELECT 'BOTTOM' BANNER_POSITION,VL.SCROLLABLE,VL.DELAY,B.BANNER_IMAGE_NAME,B.IMAGE_TYPE,V.BANNER_LANDING_URL,VL.NUMOFIMGSTOSCROLL,VL.DIRECTION,V.DISP_SEQ FROM BANNERS B,VALUE_LIST_DATA V,VALUES_LIST VL,STATIC_PAGES SP WHERE B.BANNER_ID   = V.BANNER_ID AND V.VALUE_LIST_ID = VL.VALUE_LIST_ID AND V.VALUE_LIST_ID = SP.BOTTOM_BANNER_ID AND SP.STATIC_PAGE_ID = ? AND SYSDATE BETWEEN   (SELECT NVL2(EFFECTIVE_DATE, EFFECTIVE_DATE, SYSDATE-1) FROM VALUES_LIST WHERE VALUE_LIST_ID=((SELECT BOTTOM_BANNER_ID FROM STATIC_PAGES WHERE STATIC_PAGE_ID=?))) AND (SELECT NVL2(END_DATE, END_DATE, SYSDATE+1) FROM VALUES_LIST  WHERE VALUE_LIST_ID=( (SELECT BOTTOM_BANNER_ID FROM STATIC_PAGES WHERE STATIC_PAGE_ID=?))) UNION SELECT 'LEFT' BANNER_POSITION,VL.SCROLLABLE,VL.DELAY,B.BANNER_IMAGE_NAME,B.IMAGE_TYPE,V.BANNER_LANDING_URL,VL.NUMOFIMGSTOSCROLL,VL.DIRECTION,V.DISP_SEQ FROM BANNERS B,VALUE_LIST_DATA V,VALUES_LIST VL,STATIC_PAGES SP WHERE B.BANNER_ID   = V.BANNER_ID AND V.VALUE_LIST_ID = VL.VALUE_LIST_ID AND V.VALUE_LIST_ID = SP.LEFT_BANNER_ID AND SP.STATIC_PAGE_ID = ? AND SYSDATE BETWEEN  (SELECT NVL2(EFFECTIVE_DATE, EFFECTIVE_DATE, SYSDATE-1) FROM VALUES_LIST WHERE VALUE_LIST_ID=((SELECT LEFT_BANNER_ID FROM STATIC_PAGES WHERE STATIC_PAGE_ID=?))) AND (SELECT NVL2(END_DATE, END_DATE, SYSDATE+1) FROM VALUES_LIST WHERE VALUE_LIST_ID=((SELECT LEFT_BANNER_ID FROM STATIC_PAGES WHERE STATIC_PAGE_ID=?)))) ORDER BY BANNER_POSITION,DISP_SEQ";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		LinkedHashMap<String, ArrayList<MenuAndBannersModal>> staticBanners = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
		ArrayList<MenuAndBannersModal> staticBanner = new ArrayList<MenuAndBannersModal>();
		
		
		try{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			pstmt.setString(3, id);
			pstmt.setString(4, id);
			pstmt.setString(5, id);
			pstmt.setString(6, id);
			pstmt.setString(7, id);
			pstmt.setString(8, id);
			pstmt.setString(9, id);
			pstmt.setString(10, id);
			pstmt.setString(11, id);
			pstmt.setString(12, id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				MenuAndBannersModal bannerInfo = new MenuAndBannersModal();
				bannerInfo.setImageName(rs.getString("BANNER_IMAGE_NAME"));
				bannerInfo.setImageType(rs.getString("IMAGE_TYPE"));
				bannerInfo.setImageURL(rs.getString("BANNER_LANDING_URL"));
				bannerInfo.setImagePosition(rs.getString("BANNER_POSITION"));
				bannerInfo.setBannerScroll(rs.getString("SCROLLABLE"));
				bannerInfo.setBannerNumberofItem(rs.getString("NUMOFIMGSTOSCROLL"));
				bannerInfo.setBannerDirection(rs.getString("DIRECTION"));
				bannerInfo.setBannerDelay(rs.getString("DELAY"));
				String bannerPosition = rs.getString("BANNER_POSITION").toLowerCase();
				
				staticBanner = staticBanners.get(bannerPosition);
					
					if(staticBanner==null)
					{
						staticBanner = new ArrayList<MenuAndBannersModal>();
						staticBanner.add(bannerInfo);
						
					}
					else
					{
						staticBanner.add(bannerInfo);
						
					}
					
					staticBanners.put(bannerPosition, staticBanner);
					
					
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
		}
		return staticBanners;
	}
	
	public static ArrayList<ProductsModel> getStaticBreadCrumb(String pageID,int levelNumber){
	  		

	ResultSet rs = null;
    Connection  conn = null;
    PreparedStatement pstmt=null;
    StringBuilder selectSql = new StringBuilder();
    StringBuilder fromSql = new StringBuilder();
    StringBuilder whereSql = new StringBuilder();
    StringBuilder andSql = new StringBuilder();
   
    int pageId = CommonUtility.validateNumber(pageID);
    int i = 1;
	ArrayList<ProductsModel> breadCrumbList = new ArrayList<ProductsModel>();
	
	
    
    try {
    	conn = ConnectionManager.getDBConnection();
    	selectSql.append("SELECT ");
    	fromSql.append(" FROM  WEB_STATIC_LINKS_TREE WSLT,  ");
    	whereSql.append(" WHERE WSLT.STATIC_LINK_ID =(SELECT STATIC_LINK_ID from WEB_STATIC_LINKS_TREE WHERE STATIC_PAGE_ID =").append(pageId).append(")");
    	for(i=1;i<=levelNumber;i++)
    	{
    		selectSql.append("WSLT").append(i).append(".STATIC_PAGE_ID STATIC_PAGE_ID_"+i).append(",WSLT").append(i).append(".LINK_NAME LINK_NAME_"+i).append(",WSLT").append(i).append(".STATIC_LINK_ID STATIC_LINK_ID_"+i).append(", WSLT").append(i).append(".LEVEL_NUMBER LEVEL_NUMBER_"+i);
    		fromSql.append(" WEB_STATIC_LINKS_TREE WSLT").append(i);
    		andSql.append(" AND WSLT").append(i).append(".STATIC_LINK_ID = ").append("WSLT.LEVEL").append(i);
    		
    		if(i!=levelNumber){
    			selectSql.append(", ");
    			fromSql.append(", ");
    			
    		}
    		
    		
    	}
    	
    	System.out.println("sql : "+selectSql.toString()+fromSql.toString()+whereSql.toString()+andSql.toString());
        pstmt=conn.prepareStatement(selectSql.toString()+fromSql.toString()+whereSql.toString()+andSql.toString());
       
        rs=pstmt.executeQuery();
                   
          if(rs.next())
          {  
        	  for(i=2;i<=levelNumber;i++)
  	    	{
        	  ProductsModel nameCode= new ProductsModel();
        	  System.out.println("STATIC_PAGE_ID_"+i);
        	  nameCode.setCategoryCode(rs.getString("STATIC_PAGE_ID_"+i));
        	  //nameCode.setCategoryName(rs.getString("LINK_NAME_"+i).toString().trim());
        	  
        	  String s = rs.getString("LINK_NAME_"+i).toString().trim();
        	  if(Character.isUpperCase(s.charAt(1))){
        		  String ss = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        		  nameCode.setCategoryName(ss);
        	  }else{
        		  nameCode.setCategoryName(rs.getString("LINK_NAME_"+i).toString().trim());
        	  }
        	  
        	  nameCode.setLevelNumber(rs.getInt("LEVEL_NUMBER_"+i));
        	 
        	  breadCrumbList.add(nameCode);
  	    	}
        	  
          }
         
      } catch (Exception e) {         
          e.printStackTrace();

      } finally {
    	  ConnectionManager.closeDBResultSet(rs);
    	  ConnectionManager.closeDBPreparedStatement(pstmt);	
    	  ConnectionManager.closeDBConnection(conn);	
      } // finally
	
        return breadCrumbList;
	
	}
	
	public static String getLayoutType(String pageID){

		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "SELECT FULL_PAGE_LAYOUT FROM STATIC_PAGES WHERE STATIC_PAGE_ID=?";
	    String fullPageLayout = "N";
	    
	    int pageId = CommonUtility.validateNumber(pageID);
	    try {
	    	conn = ConnectionManager.getDBConnection();
	       pstmt=conn.prepareStatement(sql);
	       pstmt.setInt(1, pageId);
	       rs=pstmt.executeQuery();
	                   
	       if(rs.next()){  
	        	if(rs.getString("FULL_PAGE_LAYOUT")!=null){
	        		fullPageLayout = rs.getString("FULL_PAGE_LAYOUT");
	        	}
	       }
	         
	      } catch (Exception e) {         
	          e.printStackTrace();

	      } finally {
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
		return fullPageLayout;
	}
	
	public static String updateContactInformation(UsersModel addressList){
		int count=0;
		String result = "0|Update Failed";
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    String country = "";
    
	    
		try {
			conn = ConnectionManager.getDBConnection();
			
			if(addressList.getCountry()!=null && !addressList.getCountry().trim().equals(""))
				country = getCountryCode(addressList.getCountry());
				if(country==null){
					country = addressList.getCountry().trim();
				}
			if(country.length()>2)
				country = country.substring(0, 2);
			 
            sql = PropertyAction.SqlContainer.get("updateContactInformation");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, addressList.getAddress1());
            pstmt.setString(2, addressList.getAddress2());
            pstmt.setString(3, addressList.getCity());
            pstmt.setString(4, addressList.getState());
            pstmt.setString(5, addressList.getZipCode());
            pstmt.setString(6, country);
            pstmt.setString(7, addressList.getPhoneNo());
            pstmt.setString(8, addressList.getEmailAddress());
            pstmt.setString(9, addressList.getFirstName());
            pstmt.setString(10, addressList.getLastName());
            pstmt.setString(11, addressList.getJobTitle());
            pstmt.setInt(12, addressList.getUserId());
            count = pstmt.executeUpdate();
            System.out.println("Updated to CimmUser table @ updateBCABillAddress : "+count);
            if(count>0){
            	
            	result = "1|"+LayoutLoader.getMessageProperties().get(addressList.getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("contact.information.updated.success");
            }else{
            	result = "0|"+LayoutLoader.getMessageProperties().get(addressList.getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("contact.information.updated.failed");
            }

            
            
		
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		
		return result;
	}
	
	public static int updateUserContact(UsersModel addressList){
		int count=0;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    System.out.println(addressList.getEmailAddress());
	    System.out.println(addressList.getFirstName());
	    System.out.println(addressList.getLastName());
	    System.out.println(addressList.getFaxNo());
	    System.out.println(addressList.getPhoneNo());
		try {
			conn = ConnectionManager.getDBConnection();
			//country = CommonUtility.getCountryCode(addressList.getCountry(), "updateBillAddress");
            sql = PropertyAction.SqlContainer.get("updateUserContactInformation");//FIRST_NAME=?, LAST_NAME=?, OFFICE_PHONE=?, EMAIL=?, FAX=? WHERE USER_ID=?
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, CommonUtility.validateString(addressList.getFirstName()));
            pstmt.setString(2, CommonUtility.validateString(addressList.getLastName()));
            pstmt.setString(3, CommonUtility.validateString(addressList.getPhoneNo()));
            pstmt.setString(4, CommonUtility.validateString(addressList.getEmailAddress()));
            pstmt.setString(5, CommonUtility.validateString(addressList.getFaxNo()));
            pstmt.setInt(6, addressList.getUserId());
            count = pstmt.executeUpdate();
            System.out.println("Updated to CimmUser CIMM2BC specific DAO : "+count);
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		return count;
	}
	
	public static int updateBillAddressBCAddressBook(UsersModel addressList){
		int count=0;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    String country = "";
	    System.out.println(addressList.getAddress1());
	    System.out.println(addressList.getAddress2());
	    System.out.println(addressList.getCity());
	    System.out.println(addressList.getState());
	    System.out.println(addressList.getCountry());
	    System.out.println(addressList.getPhoneNo());
	    System.out.println(addressList.getZipCode());
	    System.out.println(addressList.getEmailAddress());
		try {
			conn = ConnectionManager.getDBConnection();
			country = CommonUtility.getCountryCode(addressList.getCountry(), "updateBillAddress");
            sql = PropertyAction.SqlContainer.get("updateBillCimm");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, addressList.getAddress1());
            pstmt.setString(2, addressList.getAddress2());
            pstmt.setString(3, addressList.getCity());
            pstmt.setString(4, addressList.getState());
            pstmt.setString(5, addressList.getZipCode());
            pstmt.setString(6, country);
            pstmt.setString(7, addressList.getPhoneNo());
            pstmt.setString(8, addressList.getEmailAddress());
            pstmt.setString(9, addressList.getFirstName());
            pstmt.setString(10, addressList.getLastName());
            pstmt.setInt(11, addressList.getUserId());
            count = pstmt.executeUpdate();
            System.out.println("Updated to CimmUser table @ updateBCABillAddress : "+count);

            count = updateBCAddressBook(""+addressList.getAddressBookId(), addressList);
            
		
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		
		return count;
	}
	
	public static int updateBCAddressBook(String addressBookId,UsersModel addressList){
		int count=0;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("updateBCAddressBook");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, addressList.getAddress1());
            pstmt.setString(2, addressList.getAddress2());
            pstmt.setString(3, addressList.getCity());
            pstmt.setString(4, addressList.getState());
            pstmt.setString(5, addressList.getZipCode());
            pstmt.setString(6, CommonUtility.getCountryCode(addressList.getCountry(), "UpdateBCAddressBook"));
	        pstmt.setString(7, addressList.getPhoneNo());
	        pstmt.setString(8, addressList.getFirstName());
	        pstmt.setString(9, addressList.getLastName());
	        pstmt.setString(10, addressList.getEmailAddress());
	        pstmt.setString(11, addressBookId);
            count = pstmt.executeUpdate();
            System.out.println("Updated to BCAddressBook table @ updateBCAddressBook : "+count);
            
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
    }
		return count;
	}
	
	public static int updateBCAddressBook(UsersModel addressList)
	{
		int count=0;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
		try {
			String phoneNumber = "";
			if(addressList.getContactShortList()!=null && addressList.getContactShortList().size() > 0){
				for(AddressModel phone : addressList.getContactShortList()){
					if(phone.getPhoneDescription().trim().equalsIgnoreCase("Business")){
						phoneNumber=phone.getPhoneNo();
					}
				}
			}
			
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("updateBCAddressBook");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, addressList.getAddress1());
            pstmt.setString(2, addressList.getAddress2());
            pstmt.setString(3, addressList.getCity());
            pstmt.setString(4, addressList.getState());
            pstmt.setString(5, addressList.getZipCode());
            pstmt.setString(6, CommonUtility.getCountryCode(addressList.getCountry(), "updateBCAddressBook"));
            pstmt.setString(7, phoneNumber);//pstmt.setString(7, addressList.getPhoneNo());
	        pstmt.setString(8, addressList.getFirstName());
	        pstmt.setString(9, addressList.getLastName());
	        pstmt.setString(10, addressList.getEmailAddress());
	        pstmt.setString(11, ""+addressList.getAddressBookId());
	       
            count = pstmt.executeUpdate();
           System.out.println("Shipp Address update @ updateBCAddressBook : "+count);
        
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		
		return count;
	}
	
	public static int updateBCAddressBookInforSx(UsersModel addressList)
	{
		int count=0;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
		try {
			//UPDATE BC_ADDRESS_BOOK SET ADDRESS1=?, ADDRESS2=?, CITY=?, STATE=?,ZIPCODE=?,COUNTRY=?, PHONE=?, FIRST_NAME=?, LAST_NAME=?,EMAIL=?, STATUS='A' WHERE BUYING_COMPANY_ID=? AND  SHIP_TO_ID=?
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("updateBCAddressBookInforSx");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, addressList.getAddress1());
            pstmt.setString(2, addressList.getAddress2());
            pstmt.setString(3, addressList.getCity());
            pstmt.setString(4, addressList.getState());
            pstmt.setString(5, addressList.getZipCode());
            if(addressList.getCountry().equalsIgnoreCase("USA"))
            	pstmt.setString(6, "US");
            else
            	pstmt.setString(6, addressList.getCountry());
	        pstmt.setString(7, addressList.getPhoneNo());
	        pstmt.setString(8, addressList.getFirstName());
	        pstmt.setString(9, addressList.getLastName());
	        pstmt.setString(10, addressList.getEmailAddress());
	        pstmt.setString(11, ""+addressList.getBuyingCompanyId());
	        pstmt.setString(12, addressList.getShipToId());
	       
            count = pstmt.executeUpdate();
            System.out.println("Shipp Address update @ updateBCAddressBook : "+count);
        
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		
		return count;
	}
	
	public static int disableBCAddressBookInforSx(UsersModel addressList)
	{
		int count=0;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    String shipToId = null;
		try {
			if(!CommonUtility.validateString(addressList.getShipToId()).trim().equalsIgnoreCase(""))
				shipToId = addressList.getShipToId().trim();
			//UPDATE STATUS='D' WHERE BUYING_COMPANY_ID=? AND  SHIP_TO_ID=?
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("disableBCAddressBookInforSx");
            if(shipToId==null)
            sql = PropertyAction.SqlContainer.get("disableBCAddressBookInforSxNull");
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ""+addressList.getBuyingCompanyId());
            if(shipToId!=null)
            	 pstmt.setString(2, shipToId);
	       
           count = pstmt.executeUpdate();
           System.out.println("Shipp Address update @ updateBCAddressBook : "+count+" : Buying Company Id : "+addressList.getBuyingCompanyId()+" : ShipToID:"+addressList.getShipToId());
        
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		
		return count;
	}
	
	public static UsersModel forgotPassword(String username,String email){
		
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    UsersModel userInfo = null;
	    SecureData secure = new SecureData();
	    int siteId = CommonDBQuery.getGlobalSiteId();
	           
        try {
        	conn = ConnectionManager.getDBConnection();
        	if(username==null || username.trim().equalsIgnoreCase("")){
        		preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getPasswordEmail"));
  			  	preStat.setString(1, email.toLowerCase());
  			  	preStat.setInt(2, siteId);
    		}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y")){
    			
    			if(CommonUtility.validateString(username).length()>0 && CommonUtility.validateString(email).length()<1){
    				preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getPasswordUserToUpperUserNameOnly"));			 
        			preStat.setString(1, username);
        			preStat.setInt(2, siteId);
    			}else if(CommonUtility.validateString(email).length()>0 && CommonUtility.validateString(username).length()<1){
    				preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getPasswordUserToUpperEmailOnly"));			 
      			  	preStat.setString(1, email);
      			  	preStat.setInt(2, siteId);
    			}else{
    				preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getPasswordUserToUpper"));			 
        			preStat.setString(1, username);
      			  	preStat.setString(2, email);
      			  	preStat.setInt(3, siteId);
    			}
			 }else{
    			preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getPasswordUser"));			 
    			preStat.setString(1, username);
  			  	preStat.setString(2, email.toLowerCase());
  			  	preStat.setInt(3, siteId);
    		}
	        rs =preStat.executeQuery();
			if(rs.next())
			{
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")).length()>0 && secure.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")).length()>0){
					String  encPas= rs.getString("PASSWORD");
					String decriptPas = secure.validatePassword(encPas);
					if(!decriptPas.trim().equalsIgnoreCase(secure.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")))){
						userInfo = new UsersModel();
						userInfo.setUserId(rs.getInt("USER_ID"));
						userInfo.setUserName(rs.getString("USER_NAME"));
						userInfo.setPassword(rs.getString("PASSWORD"));
						userInfo.setFirstName(rs.getString("FIRST_NAME"));
						userInfo.setLastName(rs.getString("LAST_NAME"));
						userInfo.setUserStatus(rs.getString("STATUS"));
						userInfo.setCustomerId(rs.getString("ECLIPSE_CONTACT_ID"));
						userInfo.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
					}
				}else{
					userInfo = new UsersModel();
					userInfo.setUserId(rs.getInt("USER_ID"));
					userInfo.setUserName(rs.getString("USER_NAME"));
					userInfo.setPassword(rs.getString("PASSWORD"));
					userInfo.setFirstName(rs.getString("FIRST_NAME"));
					userInfo.setLastName(rs.getString("LAST_NAME"));
					userInfo.setUserStatus(rs.getString("STATUS"));
					userInfo.setCustomerId(rs.getString("ECLIPSE_CONTACT_ID"));
					userInfo.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
				}
				
			 }
        	 
	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);	
	   
	      } // finally
	      
	   return userInfo;
	}
	public static  String getCustomerCustomTextFieldValue(int buyingCompanyID, String customFieldName)
	{
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    String textFieldValue = null;
	 
	   
	    
	    try {
	    	conn = ConnectionManager.getDBConnection();
	          preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getCustomerCustomTextFieldValue"));	
	          preStat.setString(1, customFieldName);
			  preStat.setInt(2, buyingCompanyID);
			  rs =preStat.executeQuery();

			  if(rs.next()){
				  textFieldValue =  rs.getString("TEXT_FIELD_VALUE");
			  }

	    	} catch (Exception e) {         
	          e.printStackTrace();

	      } finally {	    	
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } 
	      return textFieldValue;
	}
	public static ArrayList<UsersModel> getUsersForSharedCartDAO(String keyWord,int userId,String defaultBillId) {
		ArrayList<UsersModel> userList = new ArrayList<UsersModel>();
		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		String pnsimKeyword = "";
		String newPnKeyword = "";
		try
		{
			conn= ConnectionManager.getDBConnection();
			if(keyWord!=null && !keyWord.trim().equalsIgnoreCase(""))
	  		{
		  		HashMap<String,String> keyWordBuilder = ProductsDAO.searKeywordBuilder(keyWord);
		  		pnsimKeyword = keyWordBuilder.get("pnsimKeyword");
		  		newPnKeyword = keyWordBuilder.get("newPnKeyWord");
	  		}
			stmt = conn.prepareCall("{call SEARCH_USER_BY_BUYINGCOMPANY(?,?,?,?,?)}");
			stmt.setInt(1, CommonUtility.validateNumber(defaultBillId));
			stmt.setString(2, pnsimKeyword);
	    	stmt.setString(3, newPnKeyword);
	    	stmt.setInt(4, userId);
	    	stmt.registerOutParameter(5,OracleTypes.CURSOR);
	    	stmt.execute();
	    	if(stmt.getObject(5)!=null){
	    		rs = (ResultSet) stmt.getObject(5);	
	    	}
	    	if(rs!=null){
	    		 while(rs.next())
	    		 {
			  
	    			 UsersModel userListVal = new UsersModel();
	    			 userListVal.setUserId(rs.getInt("USER_ID"));
	    			 userListVal.setFirstName(rs.getString("FIRST_NAME"));
	    			 userListVal.setLastName(rs.getString("LAST_NAME"));
	    			 userListVal.setEmailAddress(rs.getString("EMAIL"));
	    			 userList.add(userListVal );
				 
				
	    		 }
	    	 }
			
		}catch (Exception e) {
	     	e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
			
		}
		return userList;
	}
	public static String getUsersName(int userID){
		String name="";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql= PropertyAction.SqlContainer.get("getUserName");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userID);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				name = rs.getString("FIRST_NAME");
				if(rs.getString("LAST_NAME")!= null && !rs.getString("LAST_NAME").equalsIgnoreCase("")){
					name = name +" "+ rs.getString("LAST_NAME");
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
		}
		return name;
	}
	public static int shareUserSavedCart(int savedGroupId,int sharedWithUserId,int userId,String msg)
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isShared = false;
		String sql="";
		int count =0;
		try{
			conn = ConnectionManager.getDBConnection();
		}catch (Exception e){
			e.printStackTrace();
		}
		try
		{
			isShared = isShared(conn, savedGroupId, userId);
			if(!isShared)
			{
				sql=PropertyAction.SqlContainer.get("shareUserSavedCart");
				pstmt =conn.prepareStatement(sql);
				pstmt.setInt(1, savedGroupId);
				pstmt.setInt(2, sharedWithUserId);
				pstmt.setInt(3, userId);
				pstmt.setInt(4, userId);
				pstmt.setString(5,msg);
				count = pstmt.executeUpdate();
			}
		}catch (Exception e) {
				e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return count;
	}
	public static boolean isShared(Connection conn,int savedGroupId,int userId)
	{
		boolean isShared = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql="";
		try
		{
			sql=PropertyAction.SqlContainer.get("isSharedCart");
			pstmt =conn.prepareStatement(sql);
			pstmt.setInt(1, savedGroupId);
			pstmt.setInt(2, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				isShared = true;
			}
		}catch (Exception e) {
				e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		return isShared;
	}
	public void checkEclipseSession()
	{
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		java.util.Date startTime = (Date) session.getAttribute("eclipseSessionTime");
		java.util.Date endTime =  new java.util.Date();
		long elapsed_time = 0L;
		double sec = 0L;
		String userName = (String) session.getAttribute(Global.USERNAME_KEY);
		if(startTime==null && userName!=null && userName.equalsIgnoreCase("web")){
			startTime = WebLogin.getEclipseSessionTime();
		}
		if(startTime!=null){
			elapsed_time = endTime.getTime() - startTime.getTime();
			sec = (elapsed_time) / 1000.00;
		    double min = sec / 60;
		    System.out.println((String)session.getAttribute("userToken"));
		    if(min>9 || session.getAttribute("userToken")==null || CommonUtility.validateString((String)session.getAttribute("connectionError")).equalsIgnoreCase("Yes")){
				SecureData validUserPass = new SecureData();
				HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId((String) session.getAttribute(Global.USERNAME_KEY),"Y");
				String password = userDetails.get("password");
				password = validUserPass.validatePassword(password);
				String activeCustomerId = "";
				if(userName!=null && userName.equalsIgnoreCase("web")){
						userName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_USERNAME"));
						password = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_PASSWORD"));
						activeCustomerId = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_ACTIVECUSTOMER_ID"));
						
				}
				String eclipseSessionId = LoginSubmit.ERPLOGIN(CommonUtility.validateString(userName),CommonUtility.validateString(password),CommonUtility.validateString(activeCustomerId));
		        session.setAttribute("userToken", eclipseSessionId);
		        session.setAttribute("eclipseSessionTime", new java.util.Date());
		        WebLogin.setEclipseSessionTime(new java.util.Date());
	        }else{
	        	session.setAttribute("eclipseSessionTime", new java.util.Date());
	        	WebLogin.setEclipseSessionTime(new java.util.Date());
	        }
	}else{
		session.setAttribute("eclipseSessionTime", new java.util.Date());
	}
	        
	}
	public static String getPunchoutShipTo(int buyingCompanyId)
	{
		String shipToId = "0";
		String sql = "SELECT LCFV.TEXT_FIELD_VALUE FROM LOC_CUSTOM_FIELD_VALUES LCFV,BC_CUSTOM_FIELD_VALUES BCFV,CUSTOM_FIELDS CF WHERE CF.FIELD_NAME = 'PUNCHOUT_RFQ_DEFAULT_SHIPTO' AND BCFV.CUSTOM_FIELD_ID = CF.CUSTOM_FIELD_ID AND BCFV.BUYING_COMPANY_ID = ? AND LCFV.LOC_CUSTOM_FIELD_VALUE_ID = BCFV.LOC_CUSTOM_FIELD_VALUE_ID";
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	   
	    
	    try {
	    	conn = ConnectionManager.getDBConnection();
	          preStat = conn.prepareStatement(sql);			 
			  preStat.setInt(1, buyingCompanyId);
			  rs =preStat.executeQuery();
			  if(rs.next())
			  {				
				String shipId = rs.getString("TEXT_FIELD_VALUE");
				  if(shipId!=null && !shipId.trim().equalsIgnoreCase(""))
				  {
					  shipToId = CommonUtility.validateString(shipId);
				  }
			  }
	      } catch (Exception e) {         
	          e.printStackTrace();
	      } finally {	    
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);	
	      }
		return shipToId;
	}
	public static int updateUserInDB(String emailId,String customerId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql="";
		int count =0;
		try
		{
			conn = ConnectionManager.getDBConnection();
			sql=PropertyAction.SqlContainer.get("updateExistingUserEmail");
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(1, emailId);
			pstmt.setString(2, customerId);
			count = pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return count;
	}
	public static int updateBuyingCompanyAndBCAddressBook(Connection conn,UsersModel userDetailList,String status,int userId)
	{
		int count = 0;
		String updateBCAddress = PropertyAction.SqlContainer.get("updateBCAddress");
		String updateBuyingCompany =PropertyAction.SqlContainer.get("updateBuyingCompany");
		PreparedStatement pstmt = null;
		try
		{
			String phoneNumber = "";
			if(userDetailList.getContactShortList()!=null && userDetailList.getContactShortList().size() > 0){
				for(AddressModel phone : userDetailList.getContactShortList()){
					if(phone.getPhoneDescription().trim().equalsIgnoreCase("Business")){
						phoneNumber=phone.getPhoneNo();
					}
				}
			}
			
			pstmt = conn.prepareStatement(updateBCAddress);
			pstmt.setString(1, userDetailList.getAddress1());
			pstmt.setString(2, userDetailList.getAddress2());
			pstmt.setString(3, userDetailList.getCity());
			pstmt.setString(4, userDetailList.getState());
			pstmt.setString(5, userDetailList.getZipCode());
			pstmt.setString(6, phoneNumber);//pstmt.setString(6, userDetailList.getPhoneNo());
			pstmt.setString(7, status);
			pstmt.setString(8, userDetailList.getEntityId());
			
			count = pstmt.executeUpdate();
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			
			
			
			pstmt = conn.prepareStatement(updateBuyingCompany);
			pstmt.setString(1, userDetailList.getEntityName());
			if(userDetailList.getAddress1()==null)
			pstmt.setString(2, "No Address");
			else
			pstmt.setString(2, userDetailList.getAddress1());
			pstmt.setString(3, userDetailList.getAddress2());
			pstmt.setString(4, userDetailList.getCity());
			pstmt.setString(5, userDetailList.getState());
			pstmt.setString(6, userDetailList.getZipCode());
			pstmt.setString(7, "US");
			pstmt.setString(8, userDetailList.getEmailAddress());
			pstmt.setString(9, status);
			pstmt.setInt(10, userId);
			pstmt.setString(11, userDetailList.getEntityId());
			count = pstmt.executeUpdate();
			
			conn.commit();
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally
		{
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
		}
		return count;
	}
	
	public static String getEclipseCountryCode(String country)
	{
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
		String eclipseCountryCode = country.toUpperCase();
		try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("getCoutryCode");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, eclipseCountryCode);
            rs = pstmt.executeQuery();
            if(rs.next())
            {
            	if(rs.getString("ECLIPSE_COUNTRY_CODE")!=null && !rs.getString("ECLIPSE_COUNTRY_CODE").trim().equalsIgnoreCase(""))
            		eclipseCountryCode = rs.getString("ECLIPSE_COUNTRY_CODE");
            }
           
        
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	ConnectionManager.closeDBResultSet(rs);
    	ConnectionManager.closeDBPreparedStatement(pstmt);
    	ConnectionManager.closeDBConnection(conn);	
      }
		return eclipseCountryCode;
		
	}
	
	public static int updateUserData(int userId, UsersModel addressList)
	{
		int count=0;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    String country = "";
	    System.out.println(addressList.getChangedPassword());
	    System.out.println(addressList.getUserName());
	    System.out.println(addressList.getFirstName());
	    System.out.println(addressList.getLastName());
	    System.out.println(addressList.getAddress1());
	    System.out.println(addressList.getAddress2());
	    System.out.println(addressList.getCity());
	    System.out.println(addressList.getState());
	    System.out.println(addressList.getCountry());
	    System.out.println(addressList.getEmailAddress());
	    System.out.println(addressList.getZipCode());
	    System.out.println(addressList.getUserStatus());
	    System.out.println(addressList.getJobTitle());
		try {
			if(addressList.getCountry()!=null && !addressList.getCountry().trim().equals("")){
				country = CommonUtility.getCountryCode(addressList.getCountry(), "UpdateUserData");
			}
			if(country.length()>2  && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("eclipse")){
				country = country.substring(0, 2);
			}
			String securePassword = SecureData.getPunchoutSecurePassword(addressList.getChangedPassword());
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("updateCimmUser");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, addressList.getFirstName());
            pstmt.setString(2, addressList.getLastName());
            pstmt.setString(3, addressList.getAddress1());
            pstmt.setString(4, addressList.getAddress2());
            pstmt.setString(5, addressList.getCity());
            pstmt.setString(6, addressList.getState());
            pstmt.setString(7, addressList.getZipCode());
            pstmt.setString(8, country);
            pstmt.setString(9, addressList.getEmailAddress());
            pstmt.setString(10, addressList.getUserName());
            if(securePassword!=null){
            	  pstmt.setString(11, securePassword);
            }
            pstmt.setString(12, addressList.getPhoneNo());
            pstmt.setString(13, addressList.getUserStatus());
            pstmt.setString(14, addressList.getJobTitle());
            pstmt.setInt(15, userId);
            count = pstmt.executeUpdate();
           
        
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		
		return count;
	}
	/*public static int updateInnovoUserData(int userId, UsersModel userContactDetailList) {
		int count=0;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    String country = "";
		try {
	        conn = ConnectionManager.getDBConnection();
	        HashMap<String,String> userDetails= getUserPasswordAndUserId(userContactDetailList.getWebCredentials().getUsername(),"Y");
		    String password = userDetails.get("password");
	        sql = PropertyAction.SqlContainer.get("updateCimmUser");
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userContactDetailList.getFirstName());
	        pstmt.setString(2, userContactDetailList.getLastName());
	        pstmt.setString(3, userContactDetailList.getAddress().getAddressLine1());
	        pstmt.setString(4, userContactDetailList.getAddress().getAddressLine2());
	        pstmt.setString(5, userContactDetailList.getAddress().getLocality());
	        pstmt.setString(6, userContactDetailList.getAddress().getRegion());
	        pstmt.setString(7, userContactDetailList.getAddress().getPostalCode());
	        pstmt.setString(8, "US"); //userContactDetailList.getAddress().getCountryCode()
	        pstmt.setString(9, userContactDetailList.getEmails().get(0).getAddress());
	        pstmt.setString(10, userContactDetailList.getWebCredentials().getUsername()); //username
	        pstmt.setString(11, password); //password
	        pstmt.setString(12, userContactDetailList.getPhoneNo());
            pstmt.setInt(13, userId);
	        count = pstmt.executeUpdate();
	       
	    
	} catch (SQLException e) { 
	    e.printStackTrace();
	}
	finally {	    
		  ConnectionManager.closeDBPreparedStatement(pstmt);
		  ConnectionManager.closeDBConnection(conn);	
	  }
		
		return count;
	}*/
	
	public static int getSubsetId(String subsetName){

		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    int susetId = 0;	   
	   
        try {
        	 conn = ConnectionManager.getDBConnection();
	         preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getSubsetIdFromSubsetName"));			 
			 preStat.setString(1, subsetName);
				
			  rs =preStat.executeQuery();
			  if(rs.next())
			  {				
					  susetId= rs.getInt("SUBSET_ID");				  
			  }
			 
			  ConnectionManager.closeDBPreparedStatement(preStat);

	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);	
	    	  
	      } // finally
	      
	      return susetId;
	
	}
	
	public static int updateSubsetIdFromERP(int buyingCompanyId, int subsetId) {
		int count=0;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    try {
	        conn = ConnectionManager.getDBConnection();
	        sql = PropertyAction.SqlContainer.get("updateSubsetIdFromERP");
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, subsetId);
	        pstmt.setInt(2, buyingCompanyId);
	        count = pstmt.executeUpdate();
	        if(count>0){
	        	System.out.println("Subset Updated to DB from ERP : For BuyingCompany : "+buyingCompanyId+" and Subset Id as : " +subsetId);
	        }
	} catch (SQLException e) { 
	    e.printStackTrace();
	}
	finally {	    
		  ConnectionManager.closeDBPreparedStatement(pstmt);
		  ConnectionManager.closeDBConnection(conn);	
	  }
		
		return count;
	}
	
	public static int checkAddressBook(String entityId,String addressType,String bcId,Connection conn)
	{
	
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		int parentId = 0;
	
	
	        try
	        {
	        	String sql = "SELECT BC_ADDRESS_BOOK_ID FROM BC_ADDRESS_BOOK WHERE ENTITY_ID=? AND ADDRESS_TYPE=? AND BUYING_COMPANY_ID = ?";
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setString(1, entityId);
	        	pstmt.setString(2, addressType);
	        	pstmt.setString(3, bcId);
	        	rs = pstmt.executeQuery();
	        	if(rs.next())
	        		parentId = rs.getInt("BC_ADDRESS_BOOK_ID");
	        }
	        catch(SQLException e)
	        {
	        	e.printStackTrace();
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBResultSet(rs);	
	        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	        	
	        }
		return parentId;
	}
	
	public static int updateNewsletterSubscription(UsersModel userDetail)
	{
		int count = -1;
		int customFieldId = 0;
		int customFieldValueId = 0;
		String sql = "";
		
		 PreparedStatement pstmt = null;
		 Connection  conn = null;
		    try
		    {
		    	int localeId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("DEFAULT_LOCALE_ID"));
		    	conn = ConnectionManager.getDBConnection();
				sql = PropertyAction.SqlContainer.get("customFieldValueInsert");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userDetail.getNewsLetterSub());
				pstmt.setInt(2, localeId);
				pstmt.setInt(3, userDetail.getUserId());
				pstmt.setString(4, userDetail.getNewsLetterSub());
				count = pstmt.executeUpdate();
				ConnectionManager.closeDBPreparedStatement(pstmt);
				
				count = -1;
				 sql = PropertyAction.SqlContainer.get("getCustomFieldId");
				 	pstmt = null;
			        pstmt = conn.prepareStatement(sql);
			        pstmt.setString(1, userDetail.getCustomFieldName().toUpperCase());
			        pstmt.setString(2, "USER");
			        ResultSet rs = pstmt.executeQuery();
			        if(rs.next())
			        {
			        	if(rs.getInt("CUSTOM_FIELD_ID")!=0 )
			        		customFieldId = rs.getInt("CUSTOM_FIELD_ID");
			        }
			        ConnectionManager.closeDBResultSet(rs);
					ConnectionManager.closeDBPreparedStatement(pstmt);
					
					if(customFieldId>0){
						sql = PropertyAction.SqlContainer.get("updateUserCustomFieldValue");
						pstmt = null;
				        pstmt = conn.prepareStatement(sql);
				        pstmt.setString(1, userDetail.getNewsLetterSub());
				        pstmt.setInt(2, customFieldId);
			            pstmt.setInt(3, userDetail.getUserId());
				        count = pstmt.executeUpdate();
				        ConnectionManager.closeDBPreparedStatement(pstmt);
				        
				        if(count<1){
							
							sql = "SELECT LCFV.LOC_CUSTOM_FIELD_VALUE_ID FROM LOC_CUSTOM_FIELD_VALUES LCFV WHERE UPPER(LCFV.TEXT_FIELD_VALUE)=?";
							pstmt = null;
				        	pstmt = conn.prepareStatement(sql);
				        	pstmt.setString(1, userDetail.getNewsLetterSub());
				        	rs = pstmt.executeQuery();
				        	if(rs.next()){
				        		customFieldValueId = rs.getInt("LOC_CUSTOM_FIELD_VALUE_ID");
				        	}
				        	
				        	ConnectionManager.closeDBResultSet(rs);
							ConnectionManager.closeDBPreparedStatement(pstmt);
							
							
							sql = PropertyAction.SqlContainer.get("insertUserCustomFieldValue");
							pstmt = null;
							pstmt = conn.prepareStatement(sql);
							pstmt.setInt(1, userDetail.getUserId());
							pstmt.setInt(2, customFieldId);
							pstmt.setInt(3, customFieldValueId);
							pstmt.setInt(4, userDetail.getUserId());

							count = pstmt.executeUpdate();
						}
					}
		    }
				catch(SQLException e)
		        {
		        	e.printStackTrace();
		        }
		        catch(Exception e)
		        {
		        	e.printStackTrace();
		        }
		        finally
		        {
					ConnectionManager.closeDBPreparedStatement(pstmt);
					ConnectionManager.closeDBConnection(conn);	
		        }
		
		
		
		return count;
		
	}
		
	public static ArrayList<ShipVia> getUserShipViaList()
	{
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    String sqlName = "getUserShipViaAdvanced";
	    if(CommonDBQuery.getSystemParamtersList().get("SHIP_VIA_QUERY")!=null && CommonDBQuery.getSystemParamtersList().get("SHIP_VIA_QUERY").trim().length() > 0){
	    	sqlName = CommonDBQuery.getSystemParamtersList().get("SHIP_VIA_QUERY");
	    }
	    String sql = PropertyAction.SqlContainer.get(sqlName);
	    ArrayList<ShipVia> shipViaResponse = new ArrayList<ShipVia>();
        try {
        	
        	
        	if(CommonUtility.validateString(sqlName).equalsIgnoreCase("SHIPVIA_COST_TABLE")){
        		CustomModel customFieldModel = new CustomModel();
        		customFieldModel.setCustomFieldEntityType("WEBSITE");// Type
        		customFieldModel.setCustomFieldEntityId(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));//Site ID
        		customFieldModel.setCustomFieldName(CommonUtility.validateString(sqlName));// Custom Field Table Name
        		
        		shipViaResponse = CustomFieldDAO.getShipViaDetailsFromCustomFieldTable(customFieldModel);
        		
        	}else{
    					conn = ConnectionManager.getDBConnection();
    					preStat = conn.prepareStatement(sql);			 
    					rs =preStat.executeQuery();
    					while(rs.next()){				
    					  	ShipVia shipVia = new ShipVia();
    						shipVia.setShipViaID(rs.getString("SHIP_VIA_CODE"));
    						shipVia.setDescription(rs.getString("SHIP_VIA_NAME"));
    						shipVia.setServiceCode(rs.getInt("SERVICE_CODES"));
    						shipViaResponse.add(shipVia);		  
    					}
        	}
        	
 	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	    	 
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
        CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	      return shipViaResponse;
	}
	
	
	public static String getUserNewsLetterSubscriptionStatus(int userId, String customFieldName){
		long startTimer = CommonUtility.startTimeDispaly();
		String newsLetterSub = "";
		
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	   
        try {
        	 conn = ConnectionManager.getDBConnection();
	         preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getUserCustomTextFieldValue"));			 
			 preStat.setString(1, customFieldName.toUpperCase());
			 preStat.setInt(2, userId);
				
			  rs =preStat.executeQuery();
			  if(rs.next())
			  {				
				  newsLetterSub= rs.getString("TEXT_FIELD_VALUE");				  
			  }
			 
			  ConnectionManager.closeDBPreparedStatement(preStat);

	      } catch (Exception e) {         
	          e.printStackTrace();
	
	      } finally {	   
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(preStat);
	    	  ConnectionManager.closeDBConnection(conn);	
	    	 
	      } // finally
        CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	      return newsLetterSub;
	}
	
	public static String getUserRoleByUserName(String userName)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		String sql = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		String roleName = null;
		HttpServletRequest request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try
		{
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SINGLE_USER_UNDER_MULTIPLE_CUSTOMER")).equalsIgnoreCase("Y") && CommonUtility.validateString((String)session.getAttribute("multipleAccountsAvailable")).equalsIgnoreCase("Y") && 
        			CommonUtility.validateNumber((String)session.getAttribute("selectedUserId"))>0) {
				sql = PropertyAction.SqlContainer.get("getUserRoleByUserId");
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,CommonUtility.validateNumber((String)session.getAttribute("selectedUserId")));
			}else {
				sql = PropertyAction.SqlContainer.get("getUserRoleByUserName");
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,userName);
				pstmt.setInt(2,CommonDBQuery.getGlobalSiteId());
			}
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				roleName = rs.getString("ROLE_NAME");
               
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return roleName;
	}
	
	public static UsersModel getEntityIdFromBCAddressBook(int bcAddressBookId,UsersModel userModel){
		Connection conn = null;
		ResultSet  rs =null;
		PreparedStatement pstmt = null;
		UsersModel shipAddressInfo = null;
		
		try{
			
			conn = ConnectionManager.getDBConnection();
			
			//BC_ADDRESS_BOOK_ID,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,COUNTRY,PHONE,ADDRESS_TYPE,STATUS,UPDATED_DATETIME,ENTITY_ID,BUYING_COMPANY_ID,FIRST_NAME,LAST_NAME,SHIP_TO_ID,EMAIL
			String sql = PropertyAction.SqlContainer.get("getEntityIdFromBCAddressBookBybcAddressBookId");
			if(userModel!=null && userModel.getRequestType()!=null && userModel.getRequestType().trim().equalsIgnoreCase("ADDRESS_BOOK_ID")){
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,bcAddressBookId);
				pstmt.setInt(2,userModel.getBuyingCompanyId());
				rs = pstmt.executeQuery();
				
				if(rs.next()){
					
					if(userModel!=null && rs.getString("ADDRESS_TYPE")!=null && userModel.getAddressType().trim().equalsIgnoreCase(rs.getString("ADDRESS_TYPE").trim())){ 
						shipAddressInfo = new UsersModel();
						
						if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill")){
							shipAddressInfo.setDefaultBillToId(rs.getInt("BC_ADDRESS_BOOK_ID"));
						}else{
							shipAddressInfo.setDefaultShipToId(rs.getInt("BC_ADDRESS_BOOK_ID"));
						}
						shipAddressInfo.setAddressType(rs.getString("ADDRESS_TYPE"));
						shipAddressInfo.setFirstName(rs.getString("FIRST_NAME"));
						shipAddressInfo.setLastName(rs.getString("LAST_NAME"));
						shipAddressInfo.setEntityId(rs.getString("ENTITY_ID"));
						shipAddressInfo.setAddress1(rs.getString("ADDRESS1"));
						shipAddressInfo.setAddress2(rs.getString("ADDRESS2"));
						shipAddressInfo.setCity(rs.getString("CITY"));
						shipAddressInfo.setState(rs.getString("STATE"));
						shipAddressInfo.setZipCode(rs.getString("ZIPCODE"));
						shipAddressInfo.setCountry(rs.getString("COUNTRY"));
						shipAddressInfo.setPhoneNo(rs.getString("PHONE"));
						shipAddressInfo.setBuyingCompanyId(userModel.getBuyingCompanyId());
						if(userModel!=null && userModel.getRequestType()!=null && userModel.getRequestType().trim().equalsIgnoreCase("ADDRESS_BOOK_ID")){
							shipAddressInfo.setShipToId(rs.getString("SHIP_TO_ID")!=null?rs.getString("SHIP_TO_ID"):"Ship"+rs.getString("BC_ADDRESS_BOOK_ID"));
						}else{
							shipAddressInfo.setShipToId(rs.getString("SHIP_TO_ID"));
						}
						shipAddressInfo.setMakeAsDefault("N");
						shipAddressInfo.setUserId(userModel.getUserId());
						shipAddressInfo.setSession(userModel.getSession());
						shipAddressInfo.setUserName((String) userModel.getSession().getAttribute(Global.USERNAME_KEY));
						shipAddressInfo.setUserToken((String) userModel.getSession().getAttribute("userToken"));
					}
				}
				
			}else{
				sql = PropertyAction.SqlContainer.get("getEntityIdFromBCAddressBookByEntityId");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,userModel.getEntityId());
				pstmt.setInt(2,userModel.getBuyingCompanyId());
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					
					//if(userModel!=null && rs.getString("ADDRESS_TYPE")!=null && userModel.getAddressType().trim().equalsIgnoreCase(rs.getString("ADDRESS_TYPE").trim())){ // because all the Shipp Entity Id are same in SX
					if(userModel!=null && rs.getString("ADDRESS_TYPE")!=null && userModel.getAddressType().trim().equalsIgnoreCase(rs.getString("ADDRESS_TYPE").trim()) && rs.getString("ADDRESS1")!=null && userModel.getAddress1().trim().equalsIgnoreCase(rs.getString("ADDRESS1").trim()) && rs.getString("ZIPCODE")!=null && userModel.getZipCode().trim().equalsIgnoreCase(rs.getString("ZIPCODE").trim())){
						shipAddressInfo = new UsersModel();
						
						if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill")){
							shipAddressInfo.setDefaultBillToId(rs.getInt("BC_ADDRESS_BOOK_ID"));
						}else{
							shipAddressInfo.setDefaultShipToId(rs.getInt("BC_ADDRESS_BOOK_ID"));
						}
						shipAddressInfo.setAddressType(rs.getString("ADDRESS_TYPE"));
						shipAddressInfo.setFirstName(rs.getString("FIRST_NAME"));
						shipAddressInfo.setLastName(rs.getString("LAST_NAME"));
						shipAddressInfo.setEntityId(rs.getString("ENTITY_ID"));
						shipAddressInfo.setAddress1(rs.getString("ADDRESS1"));
						shipAddressInfo.setAddress2(rs.getString("ADDRESS2"));
						shipAddressInfo.setCity(rs.getString("CITY"));
						shipAddressInfo.setState(rs.getString("STATE"));
						shipAddressInfo.setZipCode(rs.getString("ZIPCODE"));
						shipAddressInfo.setCountry(rs.getString("COUNTRY"));
						shipAddressInfo.setPhoneNo(rs.getString("PHONE"));
						shipAddressInfo.setBuyingCompanyId(userModel.getBuyingCompanyId());
						if(userModel!=null && userModel.getRequestType()!=null && userModel.getRequestType().trim().equalsIgnoreCase("ADDRESS_BOOK_ID")){
							shipAddressInfo.setShipToId(rs.getString("SHIP_TO_ID")!=null?rs.getString("SHIP_TO_ID"):"Ship"+rs.getString("BC_ADDRESS_BOOK_ID"));
						}else{
							shipAddressInfo.setShipToId(rs.getString("SHIP_TO_ID"));
						}
						shipAddressInfo.setMakeAsDefault("N");
						shipAddressInfo.setUserId(userModel.getUserId());
						shipAddressInfo.setSession(userModel.getSession());
						shipAddressInfo.setUserName((String) userModel.getSession().getAttribute(Global.USERNAME_KEY));
						shipAddressInfo.setUserToken((String) userModel.getSession().getAttribute("userToken"));
					}
				}
			}
			
			
			
			
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return shipAddressInfo;
	}
	public static LinkedHashMap<String, Object> getEventListV2(EventModel eventModel, String sortBy)
	{
		Connection  conn = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		int userId=0;
		//String date = "";
		String sql = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss"); 
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE, MMM dd yyyy hh:mm aaa"); 
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy"); 
		ArrayList<EventModel> events =  new ArrayList<EventModel>();
		ArrayList<EventModel> allEvents =  new ArrayList<EventModel>();
		LinkedHashMap<String, Object> eventObject = new LinkedHashMap<String, Object>();
		LinkedHashMap<String,ArrayList<EventModel>> eventsList = new LinkedHashMap<String, ArrayList<EventModel>>();
		LinkedHashMap<String,LinkedHashMap<String,ArrayList<EventModel>>> eventsListType = new LinkedHashMap<String, LinkedHashMap<String,ArrayList<EventModel>>>();
		Date startDate=null;
		Date endDate=null;
		Gson gson = new Gson();
		LinkedHashMap<Integer, String> dateList = new LinkedHashMap<Integer, String>();
		//Set<Integer> dateList = new LinkedHashSet<Integer>();
		try {
			
			 Date currentDate = new Date();
		     String currentMonth= new SimpleDateFormat("yyyy-MM").format(currentDate);
		     String appendSql = "";
		     String tableJoin = "";
		     
		     if(!CommonUtility.validateString(eventModel.getDate()).equalsIgnoreCase("")){
		    	 appendSql = " AND to_date('"+eventModel.getDate()+"','yyyy-mm-dd') between to_date(substr(start_date,1,10),'yyyy-mm-dd') and to_date(substr(end_date,1,10),'yyyy-mm-dd') ";
		     }
		     
		     
		     
 if(CommonUtility.validateNumber(eventModel.getEventCategory()) > 0 && CommonUtility.validateNumber(eventModel.getLocation()) > 0 ){
	 tableJoin = " , VALUE_LIST_DATA VLD ,  VALUE_LIST_DATA VLD1 ";
		    	 if(!CommonUtility.validateString(eventModel.getMonth()).equalsIgnoreCase("")){
		    		 appendSql = " AND (to_date(substr(start_date,1,7),'yyyy-mm') = to_date('"+eventModel.getMonth()+"','yyyy-mm') or to_date(substr(end_date,1,7),'yyyy-mm') = to_date('"+eventModel.getMonth()+"','yyyy-mm')) AND VLD.VALUE_DATA_ID = "+eventModel.getEventCategory()+" AND EM.EVENT_CATEGORY = VLD.LIST_VALUE AND VLD1.VALUE_DATA_ID = "+eventModel.getLocation()+" AND EM.LOCATION = VLD1.LIST_VALUE ";
		    		
		    		 
		    		 
		    		 
		    	 }else{
		    		 appendSql = " AND (to_date(substr(start_date,1,7),'yyyy-mm') = to_date('"+currentMonth+"','yyyy-mm') or to_date(substr(end_date,1,7),'yyyy-mm') = to_date('"+currentMonth+"','yyyy-mm')) AND VLD.VALUE_DATA_ID = "+eventModel.getEventCategory()+" AND EM.EVENT_CATEGORY = VLD.LIST_VALUE AND VLD1.VALUE_DATA_ID = "+eventModel.getLocation()+" AND EM.LOCATION = VLD1.LIST_VALUE ";
		    	 }
		    	
		     }else if(CommonUtility.validateNumber(eventModel.getEventCategory()) > 0 ){
		    	 tableJoin = " , VALUE_LIST_DATA VLD ";
		    	 if(!CommonUtility.validateString(eventModel.getMonth()).equalsIgnoreCase("")){
		    		 appendSql = " AND (to_date(substr(start_date,1,7),'yyyy-mm') = to_date('"+eventModel.getMonth()+"','yyyy-mm') or to_date(substr(end_date,1,7),'yyyy-mm') = to_date('"+eventModel.getMonth()+"','yyyy-mm')) AND VLD.VALUE_DATA_ID = "+eventModel.getEventCategory()+" AND EM.EVENT_CATEGORY = VLD.LIST_VALUE ";
		    	 }else{
		    		 appendSql = " AND (to_date(substr(start_date,1,7),'yyyy-mm') = to_date('"+currentMonth+"','yyyy-mm') or to_date(substr(end_date,1,7),'yyyy-mm') = to_date('"+currentMonth+"','yyyy-mm')) AND VLD.VALUE_DATA_ID = "+eventModel.getEventCategory()+" AND EM.EVENT_CATEGORY = VLD.LIST_VALUE ";
		    	 }
		    	
		     }else if(CommonUtility.validateNumber(eventModel.getLocation()) > 0){
		    	 tableJoin = " , VALUE_LIST_DATA VLD ";
		    	 if(!CommonUtility.validateString(eventModel.getMonth()).equalsIgnoreCase("")){
		    		 appendSql = " AND (to_date(substr(start_date,1,7),'yyyy-mm') = to_date('"+eventModel.getMonth()+"','yyyy-mm') or to_date(substr(end_date,1,7),'yyyy-mm') = to_date('"+eventModel.getMonth()+"','yyyy-mm')) AND VLD.VALUE_DATA_ID = "+eventModel.getLocation()+" AND EM.LOCATION = VLD.LIST_VALUE ";
		    	 }else{
		    		 appendSql = " AND (to_date(substr(start_date,1,7),'yyyy-mm') = to_date('"+currentMonth+"','yyyy-mm') or to_date(substr(end_date,1,7),'yyyy-mm') = to_date('"+currentMonth+"','yyyy-mm')) AND VLD.VALUE_DATA_ID = "+eventModel.getLocation()+" AND EM.LOCATION = VLD.LIST_VALUE ";
		    	 }
		    	
		     }else if(!CommonUtility.validateString(eventModel.getMonth()).equalsIgnoreCase("")){
		    	 appendSql = " AND (to_date(substr(start_date,1,7),'yyyy-mm') = to_date('"+eventModel.getMonth()+"','yyyy-mm') or to_date(substr(end_date,1,7),'yyyy-mm') = to_date('"+eventModel.getMonth()+"','yyyy-mm')) ";
		     }
 
 
		     
		     if(appendSql.trim().equalsIgnoreCase("")){
		    	 appendSql = " AND (to_date(substr(start_date,1,7),'yyyy-mm') = to_date('"+currentMonth+"','yyyy-mm') or to_date(substr(end_date,1,7),'yyyy-mm') = to_date('"+currentMonth+"','yyyy-mm')) ";
		     }
		     
		     if(!CommonUtility.validateString(eventModel.getEventType()).equalsIgnoreCase("")){
		    	 tableJoin = tableJoin + ", VALUE_LIST_DATA VLD2 ";
    			 appendSql = appendSql + " AND VLD2.VALUE_DATA_ID = "+eventModel.getEventType()+" AND EM.EVENT_TYPE = VLD2.LIST_VALUE  ";
		 }
		     
			conn = ConnectionManager.getDBConnection();
		

					eventsList = new LinkedHashMap<String, ArrayList<EventModel>>();
					
					
					sql = "SELECT * FROM EVENT_MANAGER EM "+tableJoin+"  WHERE EM.STATUS='Y'  AND EM.DISPLAY_ONLINE='Y' "+appendSql+sortBy;	
					pstmt = conn.prepareStatement(sql);
				
					System.out.println(sql);
					rs = pstmt.executeQuery();
					int currentMntCompare = 0;
					if(CommonUtility.validateString(eventModel.getMonth()).length()>0){
						currentMntCompare = CommonUtility.validateNumber(CommonUtility.validateString(eventModel.getMonth()).split("-")[1]);
					}else{
						 String yearMonth = new SimpleDateFormat("yyyy-MM").format(currentDate);
						 currentMntCompare = CommonUtility.validateNumber(CommonUtility.validateString(yearMonth).split("-")[1]);
					}
					while(rs.next())
					{
						int start = 0;
						int end = 0;
						int startMonth = 0;
						int endMonth = 0;
						//COST,EVENT_CATEGORY,LOCATION,ADDRESS,CONTACT,TOTAL_SEATS,BOOKED_SEATS   
						LinkedHashMap<String, ArrayList<EventModel>> tempEventType = eventsListType.get(rs.getString("EVENT_TYPE"));
						events = eventsList.get(rs.getString("EVENT_CATEGORY"));

						EventModel event = new EventModel();
						event.setId(rs.getInt("EVENT_ID"));
						
						String startdate = rs.getString("START_DATE");
						String enddateString = rs.getString("END_DATE");
						
						startDate = dateFormat.parse(startdate); 
						String startDateString = dateFormat1.format(startDate);
						event.setDate(startDateString);
			        	
			        	endDate = dateFormat.parse(enddateString); 
			        	startDateString = dateFormat1.format(endDate);
						event.setEnd(startDateString);
						
						start = CommonUtility.validateNumber(event.getDate().split("/")[1]);
						end = CommonUtility.validateNumber(event.getEnd().split("/")[1]);
						startMonth = CommonUtility.validateNumber(event.getDate().split("/")[0]);
						endMonth = CommonUtility.validateNumber(event.getEnd().split("/")[0]);
						
						event.setStartDay(start);
						event.setEndDay(end);
						
						/*if(start > end ){
							start = 1;
						}
						if(start==end){
							dateList.put(start,"Y");
						}else{
							for(int j=start;j<=end;j++){
								dateList.put(j,"Y");
							}
						}*/
						
						
						if(currentMntCompare==startMonth){
							if(start > end){
								dateList.put(start,"Y");
							}
							if(start==end){
								dateList.put(start,"Y");
							}else{
								for(int j=start;j<=end;j++){
									dateList.put(j,"Y");
								}
							}
						}else if(currentMntCompare==endMonth){
							if(start > end){
								start =1;
							}
							for(int j=start;j<=end;j++){
								dateList.put(j,"Y");
							}
						}
						
						
						
						String startDateString1 = dateFormat2.format(startDate);
						event.setDateV2(startDateString1);
						
						startDateString1 = dateFormat2.format(endDate);
						event.setEndV2(startDateString1);
						
						event.setDescription(rs.getString("EVENT_BODY"));
						event.setTitle(rs.getString("EVENT_TITLE"));
						event.setCost(rs.getDouble("COST"));
						event.setEventCategory(rs.getString("EVENT_CATEGORY"));
						event.setLocation(rs.getString("LOCATION"));
						event.setAddress(rs.getString("ADDRESS"));
						event.setContact(rs.getString("CONTACT"));
						event.setTotalSeats(rs.getInt("TOTAL_SEATS"));
						event.setBookedSeats(rs.getInt("BOOKED_SEATS"));
						event.setEventType(rs.getString("EVENT_TYPE"));
						event.setShowSeats(rs.getString("SHOW_SEATS"));

						if(events == null)

						{
							events = new ArrayList<EventModel>();
							events.add(event);
						}
						else
						{
							events.add(event);
						}
						
						eventsList.put(rs.getString("EVENT_CATEGORY"), events);
						eventsListType.put(rs.getString("EVENT_TYPE"), eventsList);
						long eventDuration = endDate.getTime() - startDate.getTime();
				        int eventdays = (int) (eventDuration / 86400000);
				        int eventhours = (int) ((eventDuration /(1000*60*60) % 24));
				        int eventmins = (int) (eventDuration / (1000*60) % 60);
				        event.setDurationDays(CommonUtility.validateParseIntegerToString(eventdays));
				        event.setDurationHours(CommonUtility.validateParseIntegerToString(eventhours));
				        event.setDurationMinutes(CommonUtility.validateParseIntegerToString(eventmins));
				        allEvents.add(event);
					}
					
					if(dateList!=null && dateList.size()>0){
						LinkedHashMap<Integer, String> dateListTemp = new LinkedHashMap<Integer, String>();
						int largeDate = 0;
						for(Entry<Integer, String> entry : dateList.entrySet()){
							if(largeDate<entry.getKey()){
								largeDate = entry.getKey();
								dateListTemp.put(entry.getKey(),"Y");
							}
						}
						dateList = dateListTemp;
					}
					eventObject.put("eventList", allEvents);
					eventObject.put("dateList", dateList);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return eventObject;

	}

	public static LinkedHashMap<String,LinkedHashMap<String,ArrayList<EventModel>>> getEventList(EventModel eventModel, String sortBy)
	{
		Connection  conn = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		int userId=0;
		String date = "";
		String sql = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss"); 
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE, MMM dd yyyy hh:mm aaa"); 
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy"); 
		ArrayList<EventModel> events =  new ArrayList<EventModel>();
		ArrayList<String> eventsType =  new ArrayList<String>();
		LinkedHashMap<String,ArrayList<EventModel>> eventsList = new LinkedHashMap<String, ArrayList<EventModel>>();
		LinkedHashMap<String,LinkedHashMap<String,ArrayList<EventModel>>> eventsListType = new LinkedHashMap<String, LinkedHashMap<String,ArrayList<EventModel>>>();
		Date startDate=null;
		Date endDate=null;
		Gson gson = new Gson();
		
		try {
			conn = ConnectionManager.getDBConnection();
			sql = "SELECT DISTINCT(EVENT_TYPE) FROM EVENT_MANAGER WHERE STATUS='Y'";
			if(sortBy.trim().contains("EVENT_TYPE")){
				sql = "SELECT DISTINCT(EVENT_TYPE) FROM EVENT_MANAGER WHERE STATUS='Y' "+sortBy;
			}

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				eventsType.add(rs.getString("EVENT_TYPE"));

			}
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			
			if (eventsType!=null){
				for(String eventType:eventsType){
					ConnectionManager.closeDBResultSet(rs);
					ConnectionManager.closeDBPreparedStatement(pstmt);
					
					eventsList = new LinkedHashMap<String, ArrayList<EventModel>>();
					String locationFilterString = "";
					String categoryFilterString = "";
					if(eventModel.getEventLocationFilter() != null && eventModel.getEventLocationFilter().length > 0 ){
						String separater = "";
						for (int i = 0; i < eventModel.getEventLocationFilter().length; i++) {
							locationFilterString = separater+locationFilterString+CommonUtility.validateString(eventModel.getEventLocationFilter()[i]);
							separater="','";
						}
					}

					if(eventModel.getEventCategoryFilter() != null && eventModel.getEventCategoryFilter().length > 0){
						String separater = "";
						for (int i = 0; i < eventModel.getEventCategoryFilter().length; i++) {
							categoryFilterString=separater+categoryFilterString+CommonUtility.validateString(eventModel.getEventCategoryFilter()[i]);
							separater="','";
						}

					}
					
					String append = "";
					if(CommonDBQuery.getSystemParamtersList().get("FILTER_EVENTS_BY_DATE_COLUMN_NAME")!=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FILTER_EVENTS_BY_DATE_COLUMN_NAME")).length()>0 ) {
						append = "AND (TO_DATE(SUBSTR("+CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FILTER_EVENTS_BY_DATE_COLUMN_NAME"))+",1,10),'yyyy-mm-dd') >= SYSDATE)";
					}
					
					if(CommonUtility.validateString(locationFilterString).replaceAll(",", "").length()>0 && CommonUtility.validateString(categoryFilterString).replaceAll(",","").length()>0){
						sql = "SELECT * FROM EVENT_MANAGER WHERE STATUS='Y' AND LOCATION in ('"+locationFilterString+"') AND EVENT_CATEGORY in ('"+categoryFilterString+"') AND EVENT_TYPE=? AND STATUS='Y' AND DISPLAY_ONLINE='Y'" +append+" "+sortBy;	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, eventType);
					}else if(CommonUtility.validateString(locationFilterString).replaceAll(",", "").length()>0){
						sql = "SELECT * FROM EVENT_MANAGER WHERE STATUS='Y' AND LOCATION IN ('"+locationFilterString+"') AND EVENT_TYPE=? AND STATUS='Y' AND DISPLAY_ONLINE='Y' "+append+" "+sortBy;	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, eventType);
					}else if(CommonUtility.validateString(categoryFilterString).replaceAll(",","").length()>0){
						sql = "SELECT * FROM EVENT_MANAGER WHERE STATUS='Y' AND EVENT_CATEGORY in ('"+categoryFilterString+"') AND EVENT_TYPE=? AND STATUS='Y' AND DISPLAY_ONLINE='Y' "+append+" "+sortBy;	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, eventType);
					}else{
						sql = "SELECT * FROM EVENT_MANAGER WHERE EVENT_TYPE=? AND STATUS='Y'  AND DISPLAY_ONLINE='Y' "+append+" "+sortBy;	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, eventType);
					}
					//June 14
					/*if(eventModel.getEventLocationFilter() != null && eventModel.getEventLocationFilter().length > 0 && eventModel.getEventCategoryFilter() != null && eventModel.getEventCategoryFilter().length > 0){
						sql = "SELECT * FROM EVENT_MANAGER WHERE LOCATION in ('"+locationFilterString+"') AND EVENT_CATEGORY in ('"+categoryFilterString+"') AND EVENT_TYPE=? AND STATUS='Y' AND DISPLAY_ONLINE='Y' "+sortBy;	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, eventType);
					}else if(eventModel.getEventLocationFilter() != null && eventModel.getEventLocationFilter().length > 0){
						sql = "SELECT * FROM EVENT_MANAGER WHERE LOCATION IN ('"+locationFilterString+"') AND EVENT_TYPE=? AND STATUS='Y' AND DISPLAY_ONLINE='Y' "+sortBy;	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, eventType);
					}else if(eventModel.getEventCategoryFilter() != null && eventModel.getEventCategoryFilter().length > 0){
						sql = "SELECT * FROM EVENT_MANAGER WHERE EVENT_CATEGORY in ('"+categoryFilterString+"') AND EVENT_TYPE=? AND STATUS='Y' AND DISPLAY_ONLINE='Y' "+sortBy;	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, eventType);
					}else{
						sql = "SELECT * FROM EVENT_MANAGER WHERE EVENT_TYPE=? AND STATUS='Y'  AND DISPLAY_ONLINE='Y' "+sortBy;	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, eventType);
					}*/

					/*if(locName==null || CommonUtility.validateString(locName).length() == 0){
						sql = "SELECT * FROM EVENT_MANAGER WHERE EVENT_TYPE=? AND STATUS='Y'  AND DISPLAY_ONLINE='Y' "+sortBy;	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, eventType);
					}else{
						sql = "SELECT * FROM EVENT_MANAGER WHERE LOCATION=? AND EVENT_TYPE=? AND STATUS='Y' AND DISPLAY_ONLINE='Y' "+sortBy;	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, locName);
						pstmt.setString(2, eventType);
					}*/
					System.out.println(sql);
					rs = pstmt.executeQuery();
					while(rs.next())
					{
						//COST,EVENT_CATEGORY,LOCATION,ADDRESS,CONTACT,TOTAL_SEATS,BOOKED_SEATS   
						LinkedHashMap<String, ArrayList<EventModel>> tempEventType = eventsListType.get(rs.getString("EVENT_TYPE"));
						events = eventsList.get(rs.getString("EVENT_CATEGORY"));

						EventModel event = new EventModel();
						event.setId(rs.getInt("EVENT_ID"));
						
						String startdate = rs.getString("START_DATE");
						String enddateString = rs.getString("END_DATE");
						
						startDate = dateFormat.parse(startdate); 
						String startDateString = dateFormat1.format(startDate);
						event.setDate(startDateString);
			        	
			        	endDate = dateFormat.parse(enddateString); 
			        	startDateString = dateFormat1.format(endDate);
						event.setEnd(startDateString);
						
						
						String startDateString1 = dateFormat2.format(startDate);
						event.setDateV2(startDateString1);
						
						startDateString1 = dateFormat2.format(endDate);
						event.setEndV2(startDateString1);
						
						event.setDescription(rs.getString("EVENT_BODY"));
						event.setTitle(rs.getString("EVENT_TITLE"));
						event.setCost(rs.getDouble("COST"));
						event.setEventCategory(rs.getString("EVENT_CATEGORY"));
						event.setLocation(rs.getString("LOCATION"));
						event.setAddress(rs.getString("ADDRESS"));
						event.setContact(rs.getString("CONTACT"));
						event.setTotalSeats(rs.getInt("TOTAL_SEATS"));
						event.setBookedSeats(rs.getInt("BOOKED_SEATS"));
						event.setEventType(rs.getString("EVENT_TYPE"));
						event.setShowSeats(rs.getString("SHOW_SEATS"));
						event.setFeaturedEvents(rs.getString("FEATURED_EVENTS"));
						
						if(events == null)

						{
							events = new ArrayList<EventModel>();
							events.add(event);
						}
						else
						{
							events.add(event);
						}
						eventsList.put(rs.getString("EVENT_CATEGORY"), events);
						eventsListType.put(rs.getString("EVENT_TYPE"), eventsList);
						long eventDuration = endDate.getTime() - startDate.getTime();
				        int eventdays = (int) (eventDuration / 86400000);
				        int eventhours = (int) ((eventDuration /(1000*60*60) % 24));
				        int eventmins = (int) (eventDuration / (1000*60) % 60);
				        event.setDurationDays(CommonUtility.validateParseIntegerToString(eventdays));
				        event.setDurationHours(CommonUtility.validateParseIntegerToString(eventhours));
				        event.setDurationMinutes(CommonUtility.validateParseIntegerToString(eventmins));
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return eventsListType;

	}
	
	public static ArrayList<EventModel> getEventType(){
		ArrayList<EventModel> EventsType=new ArrayList<EventModel>();
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    ResultSet rs = null;
	    try {
	        conn = ConnectionManager.getDBConnection();
	        sql = "SELECT DISTINCT(EVENT_CATEGORY),EVENT_TYPE FROM EVENT_MANAGER WHERE STATUS='Y' ORDER BY EVENT_TYPE";	
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();
	        while(rs.next())
	        {
	        	EventModel event = new EventModel();
	        	event.setEventType(rs.getString("EVENT_TYPE"));
	        	event.setEventCategory(rs.getString("EVENT_CATEGORY"));
	        	EventsType.add(event);
	        }
	        }catch (Exception e) {
		    		e.printStackTrace();
			}
		    finally
		    {
		    	ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);
		    	ConnectionManager.closeDBConnection(conn);
		    }
		return EventsType;
		
	}
	
	public static EventModel getEventDetails(int eventid){
		
		String sql = "SELECT * FROM EVENT_MANAGER WHERE EVENT_ID=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		EventModel event = new EventModel();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); 
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEE, MMM dd yyyy hh:mm aaa");
		ArrayList<EventModel> eventCustomFieldsList = new ArrayList<EventModel>();
		Date startDate=null;
		Date endDate=null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventid);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				event.setId(rs.getInt("EVENT_ID"));

				String startdate = rs.getString("START_DATE");
				String enddateString = rs.getString("END_DATE");
				
				startDate = dateFormat.parse(startdate); 
				String startDateString = dateFormat1.format(startDate);
				event.setDate(startDateString);
	        	
	        	endDate = dateFormat.parse(enddateString); 
	        	startDateString = dateFormat1.format(endDate);
				event.setEnd(startDateString);
				
	        	event.setDescription(rs.getString("EVENT_BODY"));
	        	event.setTitle(rs.getString("EVENT_TITLE"));
	        	event.setCost(rs.getDouble("COST"));
	        	event.setEventCategory(rs.getString("EVENT_CATEGORY"));
	        	event.setLocation(rs.getString("LOCATION"));
	        	event.setAddress(rs.getString("ADDRESS"));
	        	event.setContact(rs.getString("CONTACT"));
	        	event.setTotalSeats(rs.getInt("TOTAL_SEATS"));
	        	event.setBookedSeats(rs.getInt("BOOKED_SEATS"));
	        	event.setNotification(rs.getString("NOTIFICATION_EMAIL"));
	        	event.setFileLocation(rs.getString("EVENT_FILE_NAME"));
	        	event.setEventType(rs.getString("EVENT_TYPE"));
	        	event.setShowSeats(rs.getString("SHOW_SEATS"));
	        	event.setBlockOnlineReg(rs.getString("BLOCK_ONLINE_REG"));
	        	event.setColor(rs.getString("COLOR"));
	        	if(rs.getString("PAYMENT_OPTION_SELECTED")!=null)
	        	{
	        	event.setPaymentOptionSelected(rs.getString("PAYMENT_OPTION_SELECTED"));
	        	}
	            event.setIsAllDayEvent(rs.getInt("IS_ALL_DAY_EVENT"));
	            event.setClientCost(rs.getInt("CLIENT_COST"));
	            if(rs.getString("CONTACT_METHOD")!=null){
	            event.setContactMethod(rs.getString("CONTACT_METHOD"));
	            }
	            event.setDisplayOnline(rs.getString("DISPLAY_ONLINE"));
	            event.setIsCostRequired(rs.getString("IS_COST_REQUIRED"));
	            event.setIsFaxRequired(rs.getString("IS_FAX_REQUIRED"));
	            event.setUpdatedDatetime(rs.getString("UPDATED_DATETIME"));
	            event.setCustomerNotificationId(rs.getInt("CUSTOMER_NOTIFICATION_ID"));
	            event.setInternalNotificationId(rs.getInt("INTERNAL_NOTIFICATION_ID"));
	            event.setEventBodyKeyword(rs.getString("EVENT_BODY_KEYWORD"));
	            event.setStatus(rs.getString("STATUS"));
	            event.setRecurringRule(rs.getInt("RECURRING_RULE"));
	            event.setTimezoneOffset(rs.getString("TIME_ZONE_OFFSET"));
	            event.setServiceCenter(rs.getString("SERVICE_CENTER"));
	            event.setTimeZone(rs.getString("TIME_ZONE"));
	            event.setFeaturedEvents(rs.getString("FEATURED_EVENTS"));
	            event.setUserEdited(rs.getInt("USER_EDITED"));
	            if(rs.getString("NOTIFICATION_CONTACT_NUMBER")!=null){
	            	event.setPhoneNumber(rs.getString("NOTIFICATION_CONTACT_NUMBER"));
	            }
	            if(rs.getString("ADDITIONAL_INFORMATION")!=null){
	            	event.setAdditionalInformation(rs.getString("ADDITIONAL_INFORMATION"));
	            }
	            long eventDuration = endDate.getTime() - startDate.getTime();
	        	int eventdays = (int) (eventDuration / 86400000);
	        	int eventhours = (int) ((eventDuration /(1000*60*60) % 24));
	        	int eventmins = (int) (eventDuration / (1000*60) % 60);
	        	event.setDurationDays(CommonUtility.validateParseIntegerToString(eventdays));
	        	event.setDurationHours(CommonUtility.validateParseIntegerToString(eventhours));
	        	event.setDurationMinutes(CommonUtility.validateParseIntegerToString(eventmins));
			}
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
			sql = "SELECT * FROM EVENTS_CF_VALUES_VIEW WHERE EVENT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventid);
			rs = pstmt.executeQuery();
			while(rs.next())
	        {
			   if(rs.getString("FIELD_NAME") != null  && CommonUtility.validateString(rs.getString("FIELD_NAME")).equalsIgnoreCase("EVENT_IMAGE_URL")) {
				   event.setImageName(rs.getString("FIELD_VALUE"));
			   }
			   EventModel eventObject = new EventModel();
			   eventObject.setId(rs.getInt("EVENT_ID"));
			   eventObject.setTitle(rs.getString("EVENT_TITLE"));
			   eventObject.setEventCategory(rs.getString("EVENT_CATEGORY"));
			   eventObject.setFieldName(rs.getString("FIELD_NAME"));
			   eventObject.setFieldValue(rs.getString("FIELD_VALUE"));
			   eventCustomFieldsList.add(eventObject);
	        }
			event.setCustomFieldsList(eventCustomFieldsList);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return event;
	}
	
	public static int getTotalAvailableSeats(int eventId)
	{
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement ps=null;
		int AvailableSeats=0;
		try{
			conn=ConnectionManager.getDBConnection();	
			
		}catch(Exception e){
			e.printStackTrace();
		}	try{
			String sql = PropertyAction.SqlContainer.get("getEventAvailableSeats");
			ps=conn.prepareStatement(sql);	
			ps.setInt(1,eventId);
			rs=ps.executeQuery();
			while(rs.next())
			{
				AvailableSeats=rs.getInt("AVAILABLESEATS");
				
			}
			
		} catch (Exception e) {         
	        e.printStackTrace();
	    } finally {	    
	    	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(ps);
	    	ConnectionManager.closeDBConnection(conn);	
	    }
		
		
		return AvailableSeats;
		
	}
	
	public static int getEventCal(String upcomingEvents)
	{
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    int userId=0;
	    String sql = "";
	    ArrayList<EventModel> events =  new ArrayList<EventModel>();
	    ArrayList<EventModel> eventCustomFieldsList = new ArrayList<EventModel>();
	    EventModel customEvent = new EventModel();
	    Gson gson = new Gson();
	    try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("getEvents");
            if(upcomingEvents!=null && upcomingEvents.trim().equalsIgnoreCase("Y")){
            	sql = PropertyAction.SqlContainer.get("getUpcomingEvents");
            }
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
            	//COST,EVENT_CATEGORY,LOCATION,ADDRESS,CONTACT,TOTAL_SEATS,BOOKED_SEATS   
            	
            	EventModel event = new EventModel();
            	event.setId(rs.getInt("EVENT_ID"));
            	event.setDate(returnDate(rs.getString("START_DATE"),rs.getString("TIME_ZONE")));
            	event.setEnd(returnDate(rs.getString("END_DATE"),rs.getString("TIME_ZONE")));
            	event.setDateV2(returnDateWithoutDaylightSavings(rs.getString("START_DATE"),rs.getString("TIME_ZONE")));
            	event.setEndV2(returnDateWithoutDaylightSavings(rs.getString("END_DATE"),rs.getString("TIME_ZONE")));
            	event.setDescription(rs.getString("EVENT_BODY"));
            	event.setTitle(rs.getString("EVENT_TITLE"));
            	event.setCost(rs.getDouble("COST"));
            	event.setEventCategory(rs.getString("EVENT_CATEGORY"));
            	event.setLocation(rs.getString("LOCATION"));
            	event.setAddress(rs.getString("ADDRESS"));
            	event.setContact(rs.getString("CONTACT"));
            	event.setTotalSeats(rs.getInt("TOTAL_SEATS"));
            	event.setBookedSeats(rs.getInt("BOOKED_SEATS"));
            	event.setBlockOnlineReg(rs.getString("BLOCK_ONLINE_REG"));
            	event.setIsAllDayEvent(rs.getInt("IS_ALL_DAY_EVENT"));
            	event.setTimeZone(rs.getString("TIME_ZONE"));
            	event.setTimezoneOffset(rs.getString("TIME_ZONE_OFFSET"));
            	event.setStatus(rs.getString("STATUS"));
            	event.setDisplayOnline(rs.getString("DISPLAY_ONLINE"));
            	event.setAdditionalInformation(rs.getString("ADDITIONAL_INFORMATION"));
            	
            	if(rs.getString("STATUS").equals("Y") && rs.getString("DISPLAY_ONLINE").equals("Y")){
            		events.add(event);	
            	}
            }
            
        		
                sql = PropertyAction.SqlContainer.get("getEventsCustomFeildsList");
            	System.out.println(sql);
            	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
            	pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
            	
            	while(rs.next())
		           {
		        	   
					   EventModel eventObject = new EventModel();
					   eventObject.setId(rs.getInt("EVENT_ID"));
					   eventObject.setTitle(rs.getString("EVENT_TITLE"));
					   eventObject.setEventCategory(rs.getString("EVENT_CATEGORY"));
					   eventObject.setFieldName(rs.getString("FIELD_NAME"));
					   eventObject.setFieldValue(rs.getString("FIELD_VALUE"));
					   eventCustomFieldsList.add(eventObject);
		           }
            	
            if(events!=null && events.size()>0 && eventCustomFieldsList!=null &&  eventCustomFieldsList.size()>0)
        	     {
            		     	    
        	    	 for(EventModel eventList:events){
        	    		 ArrayList<EventModel> customFieldsList = new ArrayList<EventModel>();
        	    		for(EventModel eventCustomList:eventCustomFieldsList){
        	    			if(eventCustomList.getId() == eventList.getId()){
        	    			EventModel event = new EventModel();
        	    			event.setEventsCustomFieldList(eventCustomList);
        	    			customFieldsList.add(event);
        	    			}
        	    	   }
        	    		eventList.setCustomFieldsList(customFieldsList);
        	         }
        	    }
           
            //if(events!=null && events.size()>0){
              
               File eventFile = new File(CommonDBQuery.getSystemParamtersList().get("EVENTSDOC"));
               if(eventFile.exists())
               {
                 try
            	   {
                	 Writer output = null;
                	 
                	    StringBuilder eventXML = new StringBuilder();
                	    eventXML.append("<?xml version=\"1.0\" encoding=\"ISO-8859-2\"?><Calendar><CalendarEvent>").append(gson.toJson(events)).append("</CalendarEvent></Calendar>");
         				
                	    output = new BufferedWriter(new FileWriter(eventFile));
                	    output.write(eventXML.toString());
                	    output.close();
         			}
                                  		   
            	   
            	   catch (Exception e) {
					e.printStackTrace();
				   }
               }
              // }else{System.out.println("event file does not exists");}
           
    } catch(SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    		ConnectionManager.closeDBResultSet(rs);
    		ConnectionManager.closeDBPreparedStatement(pstmt);
    		ConnectionManager.closeDBConnection(conn);	
      }
		return userId;
	}
	
	public static String returnDate(String str_date, String timeZone){
		long outDate = 0;
		try {    
			DateFormat formatter ; 
			Date date ; 
			//timeZone = timeZone.split(",")[0];
			String daylightSavings = "";
			String[] daylightSavingsArray = timeZone.split(",");
			System.out.println("daylightSavingsArray.length : "+ daylightSavingsArray.length);
			if(daylightSavingsArray!=null && daylightSavingsArray.length>1){
				daylightSavings = daylightSavingsArray[1];
			}
			
			if(!timeZone.contains("-")){
				timeZone = "+"+timeZone;
			}
			timeZone = "GMT"+timeZone.split(",")[0];
			formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			if(timeZone!=null)
				formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
			if(daylightSavings.equals("1")){
				Calendar cal = Calendar.getInstance();
				cal.setTime((Date)formatter.parse(str_date));
				cal.add(Calendar.HOUR, -1);
				date = cal.getTime();		          		          
			}else{   
				date = (Date)formatter.parse(str_date);
			}
			System.out.println("Time zone set : " + timeZone);
			//System.out.println("J Stamp :  " + str_date + " / " + date.getTime());
			outDate = date.getTime();
			//1333974600000
		} catch (ParseException e){
			System.out.println("Exception :"+e);    
		}    
   
		return String.valueOf(outDate);
	}
	
	public static String returnDateWithoutDaylightSavings(String str_date, String timeZone){
		long outDate = 0;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date;
			if(!timeZone.contains("-")){
				timeZone = "+"+timeZone;
			}
			timeZone = "GMT"+timeZone.split(",")[0];
			if(timeZone!=null)
				formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
			date = (Date)formatter.parse(str_date);
			outDate = date.getTime();
		} catch (ParseException e){
			System.out.println("Exception :"+e);
		}
		return String.valueOf(outDate);
	}
	
	public static String getWareHouseDetail(int warehouseId){

		String warehouseEmail = "";
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    ResultSet rs = null;
	    try {
	        conn = ConnectionManager.getDBConnection();
	        sql = "SELECT EMAIL FROM WAREHOUSE WHERE WAREHOUSE_CODE = ?";	
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, warehouseId);
	        rs = pstmt.executeQuery();
	      
	        if(rs.next()){
	        	warehouseEmail = rs.getString("EMAIL");
	        }
	        }catch (Exception e) {
		    		e.printStackTrace();
			}
		    finally
		    {
		    	ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);
		    	ConnectionManager.closeDBConnection(conn);
		    }
		return warehouseEmail;
	}
	
	public static ArrayList<WarehouseModel> getWareHouseList(){

		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    ResultSet rs = null;
	    ArrayList<WarehouseModel> wareHouseList = null;
	    try {
	        
	    	sql = PropertyAction.SqlContainer.get("getWareHouseList");
	    	
	    	conn = ConnectionManager.getDBConnection();
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        wareHouseList = new ArrayList<WarehouseModel>();
	        while(rs.next()){
	        	
	        	WarehouseModel warehouseModel = new WarehouseModel();
	        	warehouseModel.setWareHouseId(rs.getInt("WAREHOUSE_ID"));
	        	warehouseModel.setWareHouseCode(rs.getString("WAREHOUSE_CODE"));
	        	warehouseModel.setWareHouseName(rs.getString("WAREHOUSE_NAME"));
	        	warehouseModel.setAddress1(rs.getString("ADDRESS1"));
	        	warehouseModel.setAddress2(rs.getString("ADDRESS2"));
	        	warehouseModel.setCountry(rs.getString("COUNTRY"));
	        	warehouseModel.setCity(rs.getString("CITY"));
	        	warehouseModel.setState(rs.getString("STATE"));
	        	warehouseModel.setZip(rs.getString("ZIP"));
	        	warehouseModel.setEmailAddress(rs.getString("EMAIL"));
	        	warehouseModel.setPhone(rs.getString("PHONE_NUMBER"));
	        	warehouseModel.setFax(rs.getString("FAX"));
	        	warehouseModel.setLatitude(rs.getString("LATTITUDE"));
	        	warehouseModel.setLongitude(rs.getString("LONGITUDE"));
	        	warehouseModel.setServiceManager(rs.getString("SERVICE_MANAGER"));
	        	warehouseModel.setWorkHours(rs.getString("WORK_HOUR"));
	        	warehouseModel.setNote(rs.getString("NOTE"));
	        	warehouseModel.setAreaCode(rs.getString("AC"));
	        	wareHouseList.add(warehouseModel);
	        	
	        	System.out.println(rs.getString("WAREHOUSE_NAME")+" : "+rs.getString("LATTITUDE")+" : "+rs.getString("LONGITUDE"));
	        }
	        
	     }catch (Exception e) {
		    		e.printStackTrace();
		 }
		 finally{
		    	ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);
		    	ConnectionManager.closeDBConnection(conn);
		 }
		return wareHouseList;
	}
	
	
	public static ArrayList<WarehouseModel> getWareHouseListByCustomFields(){

		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    String sql = "";
	    ResultSet rs = null;
	    ArrayList<WarehouseModel> wareHouseList = null;
	    try {
	        
	    	sql = PropertyAction.SqlContainer.get("getWareHouseList");
	    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WAREHOUSE_LIST_CUSTOM_SQL")).length()>0){
	    		sql = PropertyAction.SqlContainer.get(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WAREHOUSE_LIST_CUSTOM_SQL")));
	    	}
	    	
	    	conn = ConnectionManager.getDBConnection();
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        wareHouseList = new ArrayList<WarehouseModel>();
	        while(rs.next()){
	        	
	        	WarehouseModel warehouseModel = new WarehouseModel();
	        	warehouseModel.setWareHouseId(rs.getInt("WAREHOUSE_ID"));
	        	warehouseModel.setWareHouseCode(rs.getString("WAREHOUSE_CODE"));
	        	warehouseModel.setWareHouseName(rs.getString("WAREHOUSE_NAME"));
	        	warehouseModel.setAddress1(rs.getString("ADDRESS1"));
	        	warehouseModel.setAddress2(rs.getString("ADDRESS2"));
	        	warehouseModel.setCountry(rs.getString("COUNTRY"));
	        	warehouseModel.setCity(rs.getString("CITY"));
	        	warehouseModel.setState(rs.getString("STATE"));
	        	warehouseModel.setZip(rs.getString("ZIP"));
	        	warehouseModel.setEmailAddress(rs.getString("EMAIL"));
	        	warehouseModel.setPhone(rs.getString("PHONE_NUMBER"));
	        	warehouseModel.setFax(rs.getString("FAX"));
	        	warehouseModel.setLatitude(rs.getString("LATTITUDE"));
	        	warehouseModel.setLongitude(rs.getString("LONGITUDE"));
	        	warehouseModel.setServiceManager(rs.getString("SERVICE_MANAGER"));
	        	warehouseModel.setWorkHours(rs.getString("WORK_HOUR"));
	        	warehouseModel.setNote(rs.getString("NOTE"));
	        	warehouseModel.setAreaCode(rs.getString("AC"));
	        	wareHouseList.add(warehouseModel);
	        	
	        	System.out.println(rs.getString("WAREHOUSE_NAME")+" : "+rs.getString("LATTITUDE")+" : "+rs.getString("LONGITUDE"));
	        }
	        
	     }catch (Exception e) {
		    		e.printStackTrace();
		 }
		 finally{
		    	ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);
		    	ConnectionManager.closeDBConnection(conn);
		 }
		return wareHouseList;
	}
	
	public static ArrayList<WarehouseModel> getWareHouseListFromIndex(String wIndex){

		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    ArrayList<WarehouseModel> wareHouseList = null;
	    try {
	        conn = ConnectionManager.getDBConnection();
	        String sql = PropertyAction.SqlContainer.get("getWareHouseListFromIndex");
	        if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WAREHOUSE_DISPLAY_SORTBY_DISPLAY_SEQ_SQL")).length()>0){
	        	sql = PropertyAction.SqlContainer.get(CommonDBQuery.getSystemParamtersList().get("WAREHOUSE_DISPLAY_SORTBY_DISPLAY_SEQ_SQL"));
	        }
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, CommonUtility.validateString(wIndex).toUpperCase()+"%");
	        rs = pstmt.executeQuery();

	        wareHouseList = new ArrayList<WarehouseModel>();
	        
	        while(rs.next()){
	        	
	        	WarehouseModel warehouseModel = new WarehouseModel();
	        	
	        	warehouseModel.setWareHouseCode(rs.getString("WAREHOUSE_CODE"));
	        	warehouseModel.setWareHouseName(rs.getString("WAREHOUSE_NAME"));
	        	wareHouseList.add(warehouseModel);
	      }
	        
	     }catch (Exception e) {
		    		e.printStackTrace();
		 }
		 finally{
		    	ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);
		    	ConnectionManager.closeDBConnection(conn);
		 }
		return wareHouseList;
	}
	
	public static ArrayList<EventModel> getEventCategories(){
		ArrayList<EventModel> eventCatList= new ArrayList<EventModel>();
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
		try {
            conn = ConnectionManager.getDBConnection();
            sql = "SELECT DISTINCT EVENT_CATEGORY FROM EVENT_MANAGER WHERE STATUS='Y'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
            	EventModel event = new EventModel();
            	if(rs.getString("EVENT_CATEGORY")!=null && !rs.getString("EVENT_CATEGORY").trim().equalsIgnoreCase("")){
            		event.setEventCategory(rs.getString("EVENT_CATEGORY"));
            		eventCatList.add(event);
            	}
            	
            }
           
    } catch (SQLException e) { 
         e.printStackTrace();
    }
    finally {	    
    	ConnectionManager.closeDBResultSet(rs);
    	ConnectionManager.closeDBPreparedStatement(pstmt);
    	ConnectionManager.closeDBConnection(conn);	
      }
		return eventCatList;
	}
	
	public static ArrayList<EventModel> getEventLocation(){
		ArrayList<EventModel> eventCatList= new ArrayList<EventModel>();
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
		try {
            conn = ConnectionManager.getDBConnection();
            sql = "SELECT DISTINCT LOCATION FROM EVENT_MANAGER";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
            	EventModel event = new EventModel();
            	if(CommonUtility.validateString(rs.getString("LOCATION")).length()>0){
            		event.setLocation(CommonUtility.validateString(rs.getString("LOCATION")));
            		eventCatList.add(event);
            	}
            	
            }
           
    } catch (SQLException e) { 
         e.printStackTrace();
    }
    finally {	    
    	ConnectionManager.closeDBResultSet(rs);
    	ConnectionManager.closeDBPreparedStatement(pstmt);
    	ConnectionManager.closeDBConnection(conn);	
      }
		return eventCatList;
	}
	
	public static int updateUserContactEmail(UsersModel userModel){
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
	    int count = 0;
		try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("updateUserContactEmail");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userModel.getEmailAddress());
            pstmt.setInt(2, userModel.getUserId());
            count = pstmt.executeUpdate();
           
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBResultSet(rs);
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
    }
	return count;
	}
	
	public static UsersModel getWarehouseEmail(String warehouseCode)
	  {
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    UsersModel warehouseDetail = new UsersModel();
	    String sql = "";
	    try {
	      conn = ConnectionManager.getDBConnection();
	      sql = PropertyAction.SqlContainer.get("getWarehouseEmailID");
	      pstmt = conn.prepareStatement(sql);
	      pstmt.setString(1, warehouseCode);
	      rs = pstmt.executeQuery();

	      if (rs.next())
	      {
	    	  warehouseDetail.setEmailAddress(rs.getString("EMAIL"));
	    	  warehouseDetail.setPhoneNo(rs.getString("PHONE_NUMBER"));
	      }
	    }
	    catch (SQLException e)
	    {
	      e.printStackTrace();
	    }
	    finally {	    
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	    }
	    CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	    return warehouseDetail;
	  }

	public static HashMap<String, UsersModel> getUserShipVia()
	{
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
	    String shipVia = "";
	    HashMap<String,UsersModel> userShipDetails = new HashMap<String,UsersModel>();
	    UsersModel obj = null;
	    try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("getShipViaValuesFromERP");
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
            	obj = new UsersModel();
            	obj.setShipViaDescription(rs.getString("SHIP_VIA_DESCRIPTION"));
            	obj.setShipViaMethod(rs.getString("SHIP_METHOD"));
            	obj.setShipViaID(rs.getString("SHIP_VIA_VALUE_ID"));
            	shipVia = rs.getString("SHIP_VIA_CODE").trim().toUpperCase();
            	userShipDetails.put(shipVia, obj);
            }
	    }
	    catch (SQLException e) { 
	        e.printStackTrace();
	    }
	    finally {	    
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	    }
	    return userShipDetails;
	}

	public static void folderExist(String folderName)
	{
		try
		{
		File file = new File(folderName);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	public static int updateMailSentStatus(final int eventID){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try
		{
			conn = ConnectionManager.getDBConnection();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try{
			String sql = "UPDATE EVENT_REGISTRATION SET MAIL_SENT_STATUS = 'Y' WHERE EVENT_REG_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventID);
			count = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		
		}
		return count;
	}
	
	public static String constantContactOne(UsersModel userDetail){
		String response = "";
		try{
			String CCOUrl=CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_APP_URL");
			String ACTION_BY=CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_ACTION_BY");
			String API_KEY=CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_API_KEY");
			String API_TOKEN=CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_ACESS_TOKEN");
			String EMAIL_LIST_ID = CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_EMAIL_LIST_ID");
			URL urlString = new URL(CCOUrl+"?action_by="+ACTION_BY+"&api_key="+API_KEY);
			HttpURLConnection connection = (HttpURLConnection) urlString.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization","Bearer "+API_TOKEN);
			connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
			connection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
			connection.setDoOutput(true);
			OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
			String inputs = "";
			if(CommonUtility.validateString(userDetail.getCompanyName()).length()>0){
				inputs = "{\"lists\": [{\"id\": \""+EMAIL_LIST_ID+"\"}],\"email_addresses\": [{\"email_address\": \""+userDetail.getEmailAddress()+"\"}],\"first_name\": \""+userDetail.getFirstName()+"\",\"middle_name\": \"\",\"last_name\": \""+userDetail.getLastName()+"\",\"job_title\": \"\",\"company_name\": \""+userDetail.getCompanyName()+"\"}";
			}else{
				inputs = "{\"lists\": [{\"id\": \""+EMAIL_LIST_ID+"\"}],\"email_addresses\": [{\"email_address\": \""+userDetail.getEmailAddress()+"\"}],\"first_name\": \""+userDetail.getFirstName()+"\",\"middle_name\": \"\",\"last_name\": \""+userDetail.getLastName()+"\",\"job_title\": \"\",\"company_name\": \""+" "+"\"}";
			}
		
			osw.write(String.format(inputs));
	        osw.flush();
	        osw.close();
	        response = CommonUtility.validateString(""+connection.getResponseCode());
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	public static int updateShipViaFromERP(UsersModel userModel){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		
		try{
			conn = ConnectionManager.getDBConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			
				String sql = PropertyAction.SqlContainer.get("updateShipViaFromERP");	
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,userModel.getShipVia().toUpperCase());
				pstmt.setString(2,userModel.getShipViaDescription());
				pstmt.setInt(3,userModel.getUserId());
				count = pstmt.executeUpdate();
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);	
		}
		return count;
	}
	
	
	
	 public static int getLogidDetail(String logid)
	  {

		    Connection conn = null;
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    UsersDAO uUtil = new UsersDAO();
		    String sql = "";
		    int userId = 0;
		    String buyCookie = "";
		    String hookUrl = "";
		    String operTyp = "";
		    HashMap<String,String> userDetail = new HashMap<String, String>();
		    try
		    {
		    String erpLogin =  CommonDBQuery.getSystemParamtersList().get("ERPLOGIN");
		    String userToken = "";
		      conn = ConnectionManager.getDBConnection();
		      sql = PropertyAction.SqlContainer.get("getCxmlLoginDetails");
		      pstmt = conn.prepareStatement(sql);
		      pstmt.setString(1, logid);
		      pstmt.setString(2, "Y");
		      rs = pstmt.executeQuery();
		      int generalCatalogId = 0;
		      if (rs.next()) {
		        userId = rs.getInt("SL_USER_ID");
		        String subsetId = rs.getString("SUBSET_ID");
		        String userName = rs.getString("USER_NAME");
		        String slUserName = rs.getString("SL_USERNAME");
		        String activeCustomerId = "";//Set enntity id for logged in user & from Non loggedin user SYS.param BEFORE_LOGIN_ACTIVECUSTOMER_ID
		        
		        userDetail=getUserPasswordAndUserId(userName,"Y");
		        if(erpLogin!=null && erpLogin.trim().equalsIgnoreCase("Y")){
		        	SecureData getPassword = new SecureData();
	        		String userPassword=getPassword.validatePassword(userDetail.get("password"));
	 		        userToken = LoginSubmit.ERPLOGIN(userName, userPassword, CommonUtility.validateString(activeCustomerId));
	 		       uUtil.setSessionValue("userToken",userToken);
				}
		        int userRepId = CommonUtility.validateNumber(userDetail.get("userId"));
		        uUtil.setSessionValue("com.common.slUser", slUserName.replace("@" + userRepId + ".com", ""));
		        uUtil.setSessionValue("com.common.username", userName);
		        uUtil.setSessionValue("buyingCompanyLogo", rs.getString("LOGO"));
		        uUtil.setSessionValue("com.common.IdUser", Integer.toString(userId));
		        uUtil.setSessionValue("allowLiveChat", "Y");
		        uUtil.setSessionValue("displayCustPartNum", rs.getString("DISPLAY_CUST_PART_NUMBER"));
		        userDetail.put("isAdmin","Y");
		        userDetail.put("isAuthPurAgent","Y");
		        userDetail.put("isGeneralUser","N");
		        
		        uUtil.setSessionValue("isAdmin","Y");
		        uUtil.setSessionValue("isAuthPurAgent","Y");
		        uUtil.setSessionValue("isGeneralUser","N");
		        

		       
		    	String pricePrecision = CommonUtility.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision"));
		        uUtil.setSessionValue("pricePrecision", pricePrecision);

		      
		        uUtil.setSessionValue("userSubsetId", subsetId);
		        String generalCatalog = (String)CommonDBQuery.getSystemParamtersList().get("GENERALCATALOGID");

		        if ((rs.getString("GENERAL_CATALOG_ACCESS") != null) && (rs.getString("GENERAL_CATALOG_ACCESS").trim().equalsIgnoreCase("Y")))
		        {
		          uUtil.setSessionValue("generalCatalog", generalCatalog);
		          generalCatalogId = Integer.parseInt(generalCatalog);
		        }
		        else {
		          uUtil.setSessionValue("generalCatalog", "0");
		          generalCatalogId = 0;
		        }

		       

		        buyCookie = rs.getString("BUYER_COOKIE");
		        hookUrl = rs.getString("PUNCHOUT_URL");
		        operTyp = rs.getString("OPERATION_TYPE");
		        if ((rs.getString("FROM_ID") != null) && (!rs.getString("FROM_ID").trim().equalsIgnoreCase("")))
		          uUtil.setSessionValue("aribaFromId", rs.getString("FROM_ID"));
		        if ((rs.getString("TO_ID") != null) && (!rs.getString("TO_ID").trim().equalsIgnoreCase("")))
		          uUtil.setSessionValue("aribaToId", rs.getString("TO_ID"));
		        if ((rs.getString("USER_AGENT") != null) && (!rs.getString("USER_AGENT").trim().equalsIgnoreCase("")))
		        	uUtil.setSessionValue("userAgent", rs.getString("USER_AGENT"));
		        	uUtil.setSessionValue("buyCookie", buyCookie);
		        	uUtil.setSessionValue("hookUrl", hookUrl);
		        
		        	uUtil.setSessionValue("opType", operTyp);
		        	uUtil.setSessionValue("buyingCompanyId", rs.getString("BUYING_COMPANY_ID"));
		        	uUtil.setSessionValue("userBuyingCompanyId", rs.getString("BUYING_COMPANY_ID"));
		        	uUtil.setSessionValue("isEditable", "Y");
		        	uUtil.setSessionValue("view", "1");
		        	uUtil.setSessionValue("fromDomain",rs.getString("FROM_DOMAIN"));
		        	uUtil.setSessionValue("fromId",rs.getString("FROM_ID"));
		        	uUtil.setSessionValue("toDomain",rs.getString("TO_DOMAIN"));
		        	uUtil.setSessionValue("toId",rs.getString("TO_ID"));
		        	uUtil.setSessionValue("senderDomain",rs.getString("SENDER_DOMAIN"));
		        	uUtil.setSessionValue("senderId",rs.getString("SENDER_ID"));
		        LinkedHashMap<String, String> CustomFieldTxtList = UsersDAO.getBuyingCompanyCustomFields(Integer.parseInt(rs.getString("BUYING_COMPANY_ID")));
				if(CustomFieldTxtList!=null && CustomFieldTxtList.size()>0){
					for(Entry<String, String> entry : CustomFieldTxtList.entrySet()){
						uUtil.setSessionValue(entry.getKey(),entry.getValue());
					}
				}
				   uUtil.setSessionValue("isAuthPurchaseAgent","Y");
			        uUtil.setSessionValue("isGeneralUser","N");
		        ConnectionManager.closeDBResultSet(rs);
		        ConnectionManager.closeDBPreparedStatement(pstmt);
		        sql = PropertyAction.SqlContainer.get("updateCxmlUserSession");
		        pstmt = conn.prepareStatement(sql);
		        pstmt.setString(1, logid);
		        pstmt.executeUpdate();
		       
		       uUtil.setLoginSessionValue(userDetail);
		      
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		    finally {
		      ConnectionManager.closeDBResultSet(rs);
		      ConnectionManager.closeDBPreparedStatement(pstmt);
		      ConnectionManager.closeDBConnection(conn);
		    }
		    return userId;

	  }
	  
	  
	  
	  	public void setLoginSessionValue(HashMap<String, String> userDetails){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		LoginUserSessionObject.setUserSessionObject(session,userDetails);
	}
	  	
	  	
	  	public static int getSapUser(String sapUserName, String sapUserPassword, String hookUrl, String userName)
		  {
	  		Connection conn = null;
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    String sql = "";
		    int custId = 0;
		    int userId = 0;
		    int generalCatalogId = 0;

		     
		    try { 
			sapUserPassword = SecureData.getPunchoutSecurePassword(sapUserPassword);
		    String erpLogin =  CommonDBQuery.getSystemParamtersList().get("ERPLOGIN");
		    String userToken = "";
		      conn = ConnectionManager.getDBConnection();
		      sql = PropertyAction.SqlContainer.get("getOCIUser");
		      pstmt = conn.prepareStatement(sql);
		      pstmt.setString(1, sapUserName);
		      pstmt.setString(2, sapUserPassword);
		      rs = pstmt.executeQuery();
		      if (rs.next())
		      {
		        custId = rs.getInt("BUYING_COMPANY_ID");
		        userName = rs.getString("USER_NAME");
		        String subsetId = rs.getString("SUBSET_ID");
		        UsersDAO uUtil = new UsersDAO();
		        uUtil.setSessionValue("userSubsetId", subsetId);
		        uUtil.setSessionValue("com.common.username", userName);
		        userId = rs.getInt("USER_ID");
		        UsersDAO checkSession = new UsersDAO();
		        
		        String generalCatalog = (String)CommonDBQuery.getSystemParamtersList().get("GENERALCATALOGID");
		        if ((rs.getString("GENERAL_CATALOG_ACCESS") != null) && (rs.getString("GENERAL_CATALOG_ACCESS").trim().equalsIgnoreCase("Y")))
		        {
		          uUtil.setSessionValue("generalCatalog", generalCatalog);
		          generalCatalogId = Integer.parseInt(generalCatalog);
		        }
		        else {
		          uUtil.setSessionValue("generalCatalog", "0");
		          generalCatalogId = 0;
		        }
		        uUtil.setSessionValue("buyingCompanyLogo", rs.getString("LOGO"));
		        uUtil.setSessionValue("allowLiveChat", "Y");
		        uUtil.setSessionValue("displayCustPartNum", rs.getString("DISPLAY_CUST_PART_NUMBER"));
		        uUtil.setSessionValue("buyingCompanyId", Integer.toString(custId));
		        uUtil.setSessionValue("userBuyingCompanyId", Integer.toString(custId));
		        uUtil.setSessionValue("isEditable", "Y");
		        uUtil.setSessionValue("userPassword", sapUserPassword);
		        uUtil.setSessionValue("view", "1");
		        LinkedHashMap<String, String> CustomFieldTxtList = UsersDAO.getBuyingCompanyCustomFields(custId);
				if(CustomFieldTxtList!=null && CustomFieldTxtList.size()>0){
					for(Entry<String, String> entry : CustomFieldTxtList.entrySet()){
						uUtil.setSessionValue(entry.getKey(),entry.getValue());
					}
				}
				
				 String activeCustomerId = "";//Set enntity id for logged in user & from Non loggedin user SYS.param BEFORE_LOGIN_ACTIVECUSTOMER_ID
				
			    HashMap<String,String> userDetail=getUserPasswordAndUserId(userName,"Y");
				uUtil.setSessionValue("parentUserId", userDetail.get("parentUserId"));
		        uUtil.setSessionValue("contactId", userDetail.get("contactId"));
		        uUtil.setSessionValue("firstLogin", userDetail.get("firstLogin"));
		    	String pricePrecision = CommonUtility.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision"));
		        uUtil.setSessionValue("pricePrecision", pricePrecision);
		        uUtil.setSessionValue("defaultBillingAddressId", userDetail.get("defaultBillingAddressId"));
		        uUtil.setSessionValue("defaultShippingAddressId", userDetail.get("defaultShippingAddressId"));
		        
		        uUtil.setSessionValue("isAuthPurchaseAgent","Y");
		        uUtil.setSessionValue("isGeneralUser","N");
		        if(erpLogin!=null && erpLogin.trim().equalsIgnoreCase("Y")){
		        	SecureData getPassword = new SecureData();
	        		String userPassword=getPassword.validatePassword(userDetail.get("password"));
	 		        userToken = LoginSubmit.ERPLOGIN(userName, userPassword, CommonUtility.validateString(activeCustomerId));
	 		       uUtil.setSessionValue("userToken",userToken);
				}
		        ConnectionManager.closeDBPreparedStatement(pstmt);
		        uUtil.deleteSapCart(conn, userId);
		        uUtil.setLoginSessionValue(userDetail);
/*		        String groupType = getMGUPC(conn, userId);
		        if (groupType == null)
		        {
		          uUtil.setSessionValue("groupTypeVal", "UPC"); 
		        }

		        uUtil.setSessionValue("groupTypeVal", groupType.trim().toUpperCase());*/
		      }

		    }
		    catch (SQLException e)
		    {
		      e.printStackTrace();
		    }
		    finally {
		      ConnectionManager.closeDBResultSet(rs);
		      ConnectionManager.closeDBPreparedStatement(pstmt);
		      ConnectionManager.closeDBConnection(conn);
		    }
		    return userId;
		  }
	  	
	  	public void deleteSapCart(Connection conn,int userId){
	  		try{
				request =ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				System.out.println("Deleting Sap Cart Session : " + session.getId());
				SalesAction.deleteFromCartBySession(conn, userId, session.getId());
	  		}catch(Exception e){
				e.printStackTrace();
			}
		}
	  	
	  	public static HashMap<String, ArrayList<UsersModel>> getAllAddressListFromBCAddressBook(int buyingCompanyId){

	  		HashMap<String, ArrayList<UsersModel>> userAddressList = new HashMap<String, ArrayList<UsersModel>>();

	  		Connection  conn = null;
	  		PreparedStatement pstmt=null;
	  		ResultSet rs = null;


	  		ArrayList<UsersModel> billAddressList = new ArrayList<UsersModel>();
	  		ArrayList<UsersModel> shipAddressList = new ArrayList<UsersModel>();
	  		ArrayList<UsersModel> jobList = new ArrayList<UsersModel>();
	  		userAddressList = new HashMap<String, ArrayList<UsersModel>>();


	  		try {
	  			System.out.println(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBook"));
	  			conn = ConnectionManager.getDBConnection();
		    	billAddressList = new ArrayList<UsersModel>();
		    	shipAddressList = new ArrayList<UsersModel>();
		    	jobList=new ArrayList<UsersModel>();
		    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y")) {
		    		pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBookDefaults"));		
		    	}else {
	  			pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAddressListFromBCAddressBook"));			 
		    	}
		    	pstmt.setInt(1, buyingCompanyId);
	  			rs =pstmt.executeQuery();
	  			while(rs.next()){	
	  				UsersModel addressListVal = new UsersModel();
	  				addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
	  				addressListVal.setAddressType(rs.getString("ADDRESS_TYPE"));
	  				addressListVal.setAddress1(rs.getString("ADDRESS1"));
	  				addressListVal.setFirstName(rs.getString("FIRST_NAME"));
	  				addressListVal.setLastName(rs.getString("LAST_NAME"));
	  				addressListVal.setEntityId(rs.getString("ENTITY_ID"));
	  				addressListVal.setAddress2(rs.getString("ADDRESS2"));
	  				addressListVal.setCity(rs.getString("CITY"));
	  				addressListVal.setState(rs.getString("STATE"));
	  				addressListVal.setZipCodeStringFormat(rs.getString("ZIPCODE"));
	  				addressListVal.setCountry(rs.getString("COUNTRY"));
	  				addressListVal.setPhoneNo(rs.getString("PHONE"));
	  				addressListVal.setCustomerName(rs.getString("CUSTOMER_NAME"));
	  				addressListVal.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
	  				addressListVal.setEntityId(rs.getString("ENTITY_ID"));
	  				addressListVal.setEmailAddress(rs.getString("EMAIL")!=null?rs.getString("EMAIL"):"");
	  				if(rs.getString("SHIP_TO_ID") != null && rs.getString("SHIP_TO_ID").trim().length()>0){
	  					addressListVal.setShipToId(rs.getString("SHIP_TO_ID").trim());
	  				}else{
	  					addressListVal.setShipToId("");
	  				}
	  				if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Bill"))
	  				{
	  					billAddressList.add(addressListVal);
	  					userAddressList.put(rs.getString("ADDRESS_TYPE"), billAddressList);
	  				}
	  				else if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("Ship"))
	  				{
	  					shipAddressList.add(addressListVal);
	  					userAddressList.put(rs.getString("ADDRESS_TYPE"), shipAddressList);
	  				}
	  				else
	  				{
	  					jobList.add(addressListVal);
	  					userAddressList.put(rs.getString("ADDRESS_TYPE"), jobList);
	  				}
	  			}


	  		}  catch (Exception e) {         
	  			e.printStackTrace();

	  		}
	  		finally{
	  			ConnectionManager.closeDBResultSet(rs);
	  			ConnectionManager.closeDBPreparedStatement(pstmt);
	  			ConnectionManager.closeDBConnection(conn);
	  		}
	  		return userAddressList;

	  	}
	  	public static int advancedForgotPasswordInsert(UsersModel userDetail)
		{
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    String sql = "";
		    int count  = 0;
			try {
	            conn = ConnectionManager.getDBConnection();
	            sql = PropertyAction.SqlContainer.get("advancedForgotPasswordInsert");
	            if(CommonUtility.validateString(sql).length()>0){
	            	pstmt = conn.prepareStatement(sql);
		            pstmt.setString(1, userDetail.getaRPassword());
		            pstmt.setInt(2, userDetail.getUserId());
		            pstmt.setString(3, userDetail.getEmailAddress());
		            count = pstmt.executeUpdate();	
		            if(count>0) {
		            	ConnectionManager.closeDBPreparedStatement(pstmt);
		            	sql = PropertyAction.SqlContainer.get("afpDisableOtherActiveTokensForUser");
			            pstmt = conn.prepareStatement(sql);
			            pstmt.setString(1, userDetail.getaRPassword());
			            pstmt.setInt(2, userDetail.getUserId());
			            int countInavtive = pstmt.executeUpdate();
			            System.out.println(countInavtive+" prviously active tockes are disabled");
		            }
		        }else{
	            	System.out.println("No SQL Query in Sqlcontainer File:advancedForgotPasswordInsert");
	            }
	        }catch (SQLException e) { 
				e.printStackTrace();
			}finally {	    
		    	ConnectionManager.closeDBPreparedStatement(pstmt);
		    	ConnectionManager.closeDBConnection(conn);	
			}
			return count;
		}
		public static UsersModel retrieveAdvancedForgorPassword(UsersModel userInfo) {
			Connection  conn = null;
	  		PreparedStatement pstmt=null;
	  		ResultSet rs = null;
	  		try {
	  			conn = ConnectionManager.getDBConnection();
		    	pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("advancedForgotPasswordRead"));			 
	  			pstmt.setString(1, userInfo.getaRPassword());
	  			rs =pstmt.executeQuery();
	  			while(rs.next()){	
	  				userInfo.setUserId(rs.getInt("USER_ID"));
	  				userInfo.setUserName(rs.getString("USER_NAME"));
	  				userInfo.setCurrentPassword(rs.getString("PASSWORD"));
	  				userInfo.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
	  			}
	  		}catch (Exception e) {         
	  			e.printStackTrace();
	  		}finally{
	  			ConnectionManager.closeDBResultSet(rs);
	  			ConnectionManager.closeDBPreparedStatement(pstmt);
	  			ConnectionManager.closeDBConnection(conn);
	  		}
	  		return userInfo;
		}
		public static int advancedForgotPasswordDisable(UsersModel userDetail)
		{
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    String sql = "";
		    int count  = 0;
			try {
	            conn = ConnectionManager.getDBConnection();
	            sql = PropertyAction.SqlContainer.get("advancedForgotPasswordDisable");
	            if(CommonUtility.validateString(sql).length()>0){
	            	pstmt = conn.prepareStatement(sql);
	            	pstmt.setString(1, userDetail.getStatusDescription());
		            pstmt.setString(2, userDetail.getaRPassword());
		            pstmt.setInt(3, userDetail.getUserId());
		            count = pstmt.executeUpdate();	
		        }else{
	            	System.out.println("No SQL Query in Sqlcontainer File:advancedForgotPasswordInsert");
	            }
	        }catch (SQLException e) { 
				e.printStackTrace();
			}finally {	    
		    	ConnectionManager.closeDBPreparedStatement(pstmt);
		    	ConnectionManager.closeDBConnection(conn);	
			}
			return count;
		}
		
		public static int updateChangePasswordOnLogin(UsersModel userDetail)
		{
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    String sql = "";
		    int count  = 0;
			try {
	            conn = ConnectionManager.getDBConnection();
	            sql = PropertyAction.SqlContainer.get("updateChangePasswordOnLogin");
	            if(CommonUtility.validateString(sql).length()>0){
	            	pstmt = conn.prepareStatement(sql);
	            	pstmt.setInt(1, userDetail.getChangePasswordOnLogin());
		            pstmt.setString(2, userDetail.getUserName());
		            pstmt.setString(3, "ECOMM");
		            pstmt.setInt(4, CommonDBQuery.getGlobalSiteId());
		            count = pstmt.executeUpdate();	
		        }else{
	            	System.out.println("No SQL Query in Sqlcontainer File:updateChangePasswordOnLogin");
	            }
	        }catch (SQLException e) { 
				e.printStackTrace();
			}finally {	    
		    	ConnectionManager.closeDBPreparedStatement(pstmt);
		    	ConnectionManager.closeDBConnection(conn);	
			}
			return count;
		}
		
		public static String getUserCustomFieldValue(String userId,String customFieldName ){
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet  rs =null;
			String status = "";
			try
			{
				conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("getUserCustomTextFieldValue");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,customFieldName);
				pstmt.setString(2,userId);
				rs = pstmt.executeQuery();
				
				if(rs.next()){
	                status = rs.getString("TEXT_FIELD_VALUE");
				}
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return status;
		}
	  	public static void addressBookAtTable(Connection conn,List<Integer> addressBookId){

	  		PreparedStatement pstmt = null;
			String sql = "INSERT INTO AT_BC_ADDRESS_BOOK  SELECT * FROM BC_ADDRESS_BOOK WHERE BC_ADDRESS_BOOK_ID = ?";
			
			try
			{
				pstmt = conn.prepareStatement(sql);
				for(Integer bcAddressBookId:addressBookId){
					pstmt.setInt(1, bcAddressBookId);
					pstmt.addBatch();
				}
				pstmt.executeBatch();

				conn.commit();
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally{
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
		}
		
		public static void deleteAddressFromAddressBook(Connection conn,List<Integer> removeList){

			PreparedStatement pstmt = null;
			String sql = "DELETE FROM BC_ADDRESS_BOOK WHERE BC_ADDRESS_BOOK_ID = ?";
			
			try
			{
				pstmt = conn.prepareStatement(sql);
				for(Integer bcAddressBookId:removeList){
					pstmt.setInt(1, bcAddressBookId);
					pstmt.addBatch();
				}
				pstmt.executeBatch();

				conn.commit();
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally{
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
		}
		
		public static void updateDefaultShipAddress(Connection conn,List<Integer> addressBookId,int defaultShipAddressId){

			PreparedStatement pstmt = null;
			String sql = "UPDATE CIMM_USERS SET DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE DEFAULT_SHIPPING_ADDRESS_ID = ?";
			
			try
			{
				pstmt = conn.prepareStatement(sql);
				for(Integer bcAddressBookId:addressBookId){
					pstmt.setInt(1, defaultShipAddressId);
					pstmt.setInt(2, bcAddressBookId);
					pstmt.addBatch();
				}
				pstmt.executeBatch();

				conn.commit();
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally{
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
		}
		
		public static void insertUserCustomFieldValue(String customValue, int userId, String customFieldName){
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    ResultSet rs = null;
		    String sql = "";
		    UsersModel obj = null;
		    int customFieldId = 0;
			int customFieldValueId = 0;
		    
		    try {
		    	  conn = ConnectionManager.getDBConnection();
		    	 sql = PropertyAction.SqlContainer.get("getCustomFieldId");
				 	pstmt = null;
			        pstmt = conn.prepareStatement(sql);
			        pstmt.setString(1, customFieldName);
			        pstmt.setString(2, "USER");
			       rs = pstmt.executeQuery();
			        if(rs.next())
			        {
			        	if(rs.getInt("CUSTOM_FIELD_ID")!=0 )
			        		customFieldId = rs.getInt("CUSTOM_FIELD_ID");
			        }
			        ConnectionManager.closeDBResultSet(rs);
					ConnectionManager.closeDBPreparedStatement(pstmt);
		    	System.out.println("Customfield id of " +customFieldName+ " is " +  customFieldId );
		    	
		    	sql = "SELECT LCFV.LOC_CUSTOM_FIELD_VALUE_ID FROM LOC_CUSTOM_FIELD_VALUES LCFV WHERE UPPER(LCFV.TEXT_FIELD_VALUE)=?";
				pstmt = null;
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setString(1,customValue);
	        	rs = pstmt.executeQuery();
	        	if(rs.next()){
	        		customFieldValueId = rs.getInt("LOC_CUSTOM_FIELD_VALUE_ID");
	        	}
	        	
	        	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				
				
				sql = PropertyAction.SqlContainer.get("insertUserCustomFieldValue");
				pstmt = null;
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				pstmt.setInt(2, customFieldId);
				pstmt.setInt(3, customFieldValueId);
				pstmt.setInt(4, userId);

				pstmt.executeUpdate();
		    }
		    catch (SQLException e) { 
		        e.printStackTrace();
		    }
		    finally {	    
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(pstmt);
		    	  ConnectionManager.closeDBConnection(conn);	
		    }
		

		}
		public static String getUserCustomFieldValue(int userId, String customFieldName){
			long startTimer = CommonUtility.startTimeDispaly();
			String newsLetterSub = "";
			
			Connection  conn = null;
		    PreparedStatement preStat=null;
		    ResultSet rs = null; 
		   
	        try { 
	        	 conn = ConnectionManager.getDBConnection();
		         preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getUserCustomTextFieldValue"));			 
				 preStat.setString(1, customFieldName.toUpperCase());
				 preStat.setInt(2, userId);
					
				  rs =preStat.executeQuery();
				  if(rs.next())
				  {				
					  newsLetterSub= rs.getString("TEXT_FIELD_VALUE");				  
				  }
				 
				  ConnectionManager.closeDBPreparedStatement(preStat);

		      } catch (Exception e) {         
		          e.printStackTrace();
		
		      } finally {	 
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(preStat);
		    	  ConnectionManager.closeDBConnection(conn);	
		      } // finally
	        CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		    return newsLetterSub;
		}
		
		
		public static String updateContactEmailAddress(UsersModel addressList){
			int count=0;
			String result = "Update Failed";
			Connection  conn = null;
			PreparedStatement pstmt=null;
			String sql = "";
			try {
				conn = ConnectionManager.getDBConnection();
				sql = PropertyAction.SqlContainer.get("updateContactEmailAddress");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, addressList.getEmailAddress());
				pstmt.setInt(2, addressList.getUserId());
				count = pstmt.executeUpdate();
				System.out.println("Email address Updated to CimmUser table @ updateBCABillAddress : "+count);
				if(count>0){

					result = LayoutLoader.getMessageProperties().get(addressList.getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("contact.information.updated.success");;
				}else{
					result = "Update Failed";
				}
			} catch (SQLException e) { 
				e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);	
			}
			return result;
		}
		public static HashMap<String,String> checkUserExistInDBFromUserNameAndPassword(UsersModel userModel) {
			Connection  conn = null;
			PreparedStatement preStat=null;
			ResultSet rs = null;
			int userId=0;	   
			HashMap<String,String> userDetails = new HashMap<String,String>(); 
			try {
				conn = ConnectionManager.getDBConnection();
				preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getDetailOnAnonymousUserForUserNameAndPassword"));			 
				preStat.setString(1, userModel.getUserName());
				//preStat.setString(2, SecureData.getPunchoutSecurePassword(userModel.getPassword()));
				preStat.setString(2, userModel.getUserStatus());
				rs =preStat.executeQuery();
				if(rs.next()){											  
					userDetails.put("userId", rs.getString("USER_ID"));
					userDetails.put("userName", userModel.getUserName());
					userDetails.put("contactId", rs.getString("ECLIPSE_CONTACT_ID"));
					userDetails.put("customertype", rs.getString("CUSTOMER_TYPE"));
				}
				ConnectionManager.closeDBPreparedStatement(preStat);
			} catch (Exception e) {         
				e.printStackTrace();
			} finally {	    	 
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);	
			} // finally
			return userDetails;
		}
		public static boolean getZipcode(String zipcode){
			boolean zipcodeStatus  = false;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String strQuery = null;
			Connection conn = null;
			try
			{	
				strQuery="SELECT * FROM ZIP_CODES WHERE ZIP =?";
				conn =  ConnectionManager.getDBConnection();
				pstmt =conn.prepareStatement(strQuery);
				rs = pstmt.executeQuery();
				if(rs.next()){
					zipcodeStatus=true;
				}

			} 
			catch (Exception e){
				e.printStackTrace();
			} 
			finally{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}		
			return zipcodeStatus;
		}
		public static  LinkedHashMap<String, String> getAllUserCustomFieldValue(int userId)
		{
			Connection  conn = null;
		    PreparedStatement preStat=null;
		    ResultSet rs = null;
		    String textFieldValue = null;
		    String textFieldName = null;
		    LinkedHashMap<String, String> customValues = new LinkedHashMap<String, String>(); 		    
		    try {
		    	conn = ConnectionManager.getDBConnection();
		          preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getAllUserCustomFieldValue"));	
		          preStat.setInt(1, userId);
				  rs =preStat.executeQuery();
				  while(rs.next()){
					  
					  textFieldName = rs.getString("FIELD_NAME");
					  textFieldValue =  rs.getString("TEXT_FIELD_VALUE");
					  customValues.put(textFieldName, textFieldValue);
				  }
		    	} catch (Exception e) {         
		          e.printStackTrace();

		      } finally {	    	
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(preStat);
		    	  ConnectionManager.closeDBConnection(conn);	
		      } 
		      return customValues;
		}
		public static UsersModel getUserDetailById(int userID){
			UsersModel usersModel=null;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				conn = ConnectionManager.getDBConnection();
				String sql= PropertyAction.SqlContainer.get("getUserDetailById");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userID);
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					usersModel = new UsersModel();
					usersModel.setUserName(rs.getString("USER_NAME"));
					usersModel.setPassword(rs.getString("PASSWORD"));
					
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}finally{
				ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);
			}
			return usersModel;
		}
		public static WarehouseModel getWareHouseDetailsByCode(String warehouseCode){
			Connection  conn = null;
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			WarehouseModel warehouseModel = null;
			try {
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getWareHouseDetailsByCode"));
				pstmt.setString(1, warehouseCode);
				rs = pstmt.executeQuery();
				while(rs.next()){
					warehouseModel = new WarehouseModel();
					warehouseModel.setWareHouseId(rs.getInt("WAREHOUSE_ID"));
					warehouseModel.setWareHouseCode(rs.getString("WAREHOUSE_CODE"));
					warehouseModel.setWareHouseName(rs.getString("WAREHOUSE_NAME"));
					warehouseModel.setAddress1(rs.getString("ADDRESS1"));
					warehouseModel.setAddress2(rs.getString("ADDRESS2"));
					warehouseModel.setCountry(rs.getString("COUNTRY"));
					warehouseModel.setCity(rs.getString("CITY"));
					warehouseModel.setState(rs.getString("STATE"));
					warehouseModel.setZip(rs.getString("ZIP"));
					warehouseModel.setEmailAddress(rs.getString("EMAIL"));
					warehouseModel.setPhone(rs.getString("PHONE_NUMBER"));
					warehouseModel.setFax(rs.getString("FAX"));
					warehouseModel.setLatitude(rs.getString("LATTITUDE"));
					warehouseModel.setLongitude(rs.getString("LONGITUDE"));
					warehouseModel.setServiceManager(rs.getString("SERVICE_MANAGER"));
					warehouseModel.setWorkHours(rs.getString("WORK_HOUR"));
					warehouseModel.setNote(rs.getString("NOTE"));
					warehouseModel.setAreaCode(rs.getString("AC"));
					warehouseModel.setSubsetId(rs.getInt("SUBSET_ID"));
					System.out.println(rs.getString("WAREHOUSE_NAME")+" : "+rs.getString("LATTITUDE")+" : "+rs.getString("LONGITUDE")+" : "+rs.getInt("SUBSET_ID"));
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return warehouseModel;
		}
		public static int getBuyingCompanyIdByUserId(int userId){
			int buyingCompanyId = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection conn = null;
			
	        try{
	        	conn = ConnectionManager.getDBConnection();
	        	//String sql = "select buying_company_id from buying_company where ENTITY_ID =?"; // 09 June 2014 coz of Gerrie Exes User Reg
				String sql = PropertyAction.SqlContainer.get("getBuyingCompanyIdByUserId");
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				
				rs = pstmt.executeQuery();
				if(rs.next()){
					buyingCompanyId = rs.getInt("buying_company_id");
				}
	        }catch(SQLException e) {
	        	buyingCompanyId =0;
	        	e.printStackTrace();
	        }catch(Exception e){
	        	buyingCompanyId =0;
	        	e.printStackTrace();
	        }
	        finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
		    }
			return buyingCompanyId;
		}
		
		public int insertCustomerCreditApplicationDetails(int userId,CreditApplicationModel creditApplicationModel){
			int count= -1;
			PreparedStatement pstmt = null;
			Connection conn = null;
			int creditApplicationId = 0;
			try {
				conn = ConnectionManager.getDBConnection();
				creditApplicationId = CommonDBQuery.getSequenceId("CREDIT_APPLICATION_ID_SEQ");
				String sql = PropertyAction.SqlContainer.get("insertCustomerCreditApplicationDetails");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				pstmt.setString(2, creditApplicationModel.getDate());
				pstmt.setString(3, creditApplicationModel.getBusinessName());
				pstmt.setString(4, creditApplicationModel.getBusinessPhoneNumber());
				pstmt.setString(5, creditApplicationModel.getBusinessFaxNumber());
				pstmt.setString(6, creditApplicationModel.getBillingAddress2C());
				pstmt.setString(7, creditApplicationModel.getBillingAddressTwo2C());
				pstmt.setString(8, creditApplicationModel.getBillingCity2C());
				pstmt.setString(9, creditApplicationModel.getBillingState2C());
				pstmt.setString(10, creditApplicationModel.getBillingZipCode2C());
				pstmt.setString(11, creditApplicationModel.getShippingAddress2C());
				pstmt.setString(12, creditApplicationModel.getShippingAddressTwo2C());
				pstmt.setString(13, creditApplicationModel.getShippingCity2C());
				pstmt.setString(14, creditApplicationModel.getShippingState2C());
				pstmt.setString(15, creditApplicationModel.getShippingZipCode2C());
				pstmt.setString(16, creditApplicationModel.getLegalStructureRadio());
				pstmt.setString(17, creditApplicationModel.getBusinessTyp());
				pstmt.setString(18, creditApplicationModel.getDivisionOf());
				pstmt.setString(19, creditApplicationModel.getSubsidiaryOf());
				pstmt.setString(20, creditApplicationModel.getFederalIdNumber());
				pstmt.setString(21, creditApplicationModel.getSalesTaxStatusRadio());
				pstmt.setString(22, creditApplicationModel.getSalesTaxExemptionCertificateFileName());
				pstmt.setString(23, creditApplicationModel.getInvoiceRadio());
				pstmt.setString(24, creditApplicationModel.getInvoiceByEmailAddress());
				pstmt.setString(25, creditApplicationModel.getInvoiceByFaxNumber());
				pstmt.setString(26, creditApplicationModel.getInvoiceByEDIContactName());
				pstmt.setString(27, creditApplicationModel.getInvoiceByEDIEmailAddress());
				pstmt.setString(28, creditApplicationModel.getDateBusinessCommenced());
				pstmt.setInt(29, creditApplicationModel.getNumberOfEmployees());
				pstmt.setString(30, creditApplicationModel.getCreditLimitRequest());
				pstmt.setString(31, creditApplicationModel.getApContactPersonEmail());
				pstmt.setString(32, creditApplicationModel.getDeclarationName());
				pstmt.setString(33, creditApplicationModel.getDeclaratioEmailAddress());
				pstmt.setString(34, creditApplicationModel.getDeclarationTitle());
				pstmt.setString(35, creditApplicationModel.getDeclarationDate());
				pstmt.setInt(36, creditApplicationId);
				pstmt.setString(37, creditApplicationModel.getFinacialStatmentAvailableRadio());
				pstmt.setString(38, creditApplicationModel.getFinacialStatmentFileName());
				count = pstmt.executeUpdate();
				
				if(count>0){
					System.out.println("insertCustomerCreditApplicationDetails : "+count);
					//creditApplicationId = getCustomerCreditApplicationId(conn, userId);
					if(creditApplicationId>0){
						creditApplicationModel.setCreditApplicationId(creditApplicationId);
						insertCreditApplicationPrincipalOfficers(conn, userId, creditApplicationModel);
						insertCreditApplicationTradeReference(conn, userId, creditApplicationModel);
						insertCreditApplicationBankReference(conn, userId, creditApplicationModel);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
		    	ConnectionManager.closeDBStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
		    }
			return creditApplicationId;
		}
		
		public int getCustomerCreditApplicationId(Connection conn, int userId){
			int creditApplicationId = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
	        	String sql = PropertyAction.SqlContainer.get("getCustomerCreditApplicationId");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				rs = pstmt.executeQuery();
				if(rs.next()){
					creditApplicationId = rs.getInt("CUSTOMER_CREDIT_APPLICATION_ID");
				}
	        }catch(Exception e){
	        	creditApplicationId = 0;
	        	e.printStackTrace();
	        }
	        finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBStatement(pstmt);
			}
			return creditApplicationId;
		}
		
		public int insertCreditApplicationPrincipalOfficers(Connection conn, int userId, CreditApplicationModel creditApplicationModel){
			int count= -1;
			PreparedStatement pstmt = null;
			
			try {
				
				String[] principalOfficeName = creditApplicationModel.getPrincipalOfficeName();
				String[] principalOfficeTitle = creditApplicationModel.getPrincipalOfficeTitle();
				
				for(int i=0; i<principalOfficeName.length; i++){
					String sql = PropertyAction.SqlContainer.get("insertCreditApplicationPrincipalOfficers");
					//CUSTOMER_CREDIT_APPLICATION_ID, USER_ID, PRINCIPAL_OFFICER_NAMES, PRINCIPAL_OFFICER_TITLES
					ConnectionManager.closeDBPreparedStatement(pstmt);
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, creditApplicationModel.getCreditApplicationId());
					pstmt.setInt(2, userId);
					pstmt.setString(3, CommonUtility.validateString(principalOfficeName[i]));
					pstmt.setString(4, CommonUtility.validateString(principalOfficeTitle[i]));
					count = pstmt.executeUpdate();
					System.out.println("insertCreditApplicationPrincipalOfficers :  "+count);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
		    	ConnectionManager.closeDBStatement(pstmt);
		    }
			return count;
		}
		
		public int insertCreditApplicationTradeReference(Connection conn, int userId, CreditApplicationModel creditApplicationModel){
			int count= -1;
			PreparedStatement pstmt = null;
			
			try {
				
				String[] tradeReferenceName = creditApplicationModel.getTradeReferenceName();
				String[] tradeReferencePhoneNumber = creditApplicationModel.getTradeReferencePhoneNumber();
				String[] tradeReferenceFaxNumber = creditApplicationModel.getTradeReferenceFaxNumber();
				String[] tradeReferenceEmailAddress = creditApplicationModel.getTradeReferenceEmailAddress();
				
				for(int i=0; i<tradeReferenceName.length; i++){
					String sql = PropertyAction.SqlContainer.get("insertCreditApplicationTradeReference");
					//CUSTOMER_CREDIT_APPLICATION_ID, USER_ID, TRADE_REFERENCE_NAME, TRADE_REFERENCE_PHONE, TRADE_REFERENCE_FAX, TRADE_REFERENCE_EMAIL
					ConnectionManager.closeDBPreparedStatement(pstmt);
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, creditApplicationModel.getCreditApplicationId());
					pstmt.setInt(2, userId);
					pstmt.setString(3, CommonUtility.validateString(tradeReferenceName[i]));
					pstmt.setString(4, CommonUtility.validateString(tradeReferencePhoneNumber[i]));
					pstmt.setString(5, CommonUtility.validateString(tradeReferenceFaxNumber[i]));
					pstmt.setString(6, CommonUtility.validateString(tradeReferenceEmailAddress[i]));
					count = pstmt.executeUpdate();
					System.out.println("insertCreditApplicationPrincipalOfficers :  "+count);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
		    	ConnectionManager.closeDBStatement(pstmt);
		    }
			return count;
		}
		
		public int insertCreditApplicationBankReference(Connection conn, int userId, CreditApplicationModel creditApplicationModel){
			int count= -1;
			PreparedStatement pstmt = null;
			
			try {
				
				String[] bankReferenceName = creditApplicationModel.getBankReferenceName();
				String[] bankReferencePhoneNumber = creditApplicationModel.getBankReferencePhoneNumber();
				String[] bankReferenceFaxNumber = creditApplicationModel.getBankReferenceFaxNumber();
				String[] bankReferenceContactName = creditApplicationModel.getBankReferenceContactName();
				String[] bankReferenceAccountOrLoanNumber = creditApplicationModel.getBankReferenceAccountOrLoanNumber();
				
				for(int i=0; i<bankReferenceName.length; i++){
					String sql = PropertyAction.SqlContainer.get("insertCreditApplicationBankReference");
					//CUSTOMER_CREDIT_APPLICATION_ID, USER_ID, BANK_NAME, BANK_TELEPHONE_NUMBER,BANK_FAX_NUMBER,BANK_CONTACT_NAME,BANK_ACCOUNT_OR_LOAN_NUMBER
					ConnectionManager.closeDBPreparedStatement(pstmt);
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, creditApplicationModel.getCreditApplicationId());
					pstmt.setInt(2, userId);
					pstmt.setString(3, CommonUtility.validateString(bankReferenceName[i]));
					pstmt.setString(4, CommonUtility.validateString(bankReferencePhoneNumber[i]));
					pstmt.setString(5, CommonUtility.validateString(bankReferenceFaxNumber[i]));
					pstmt.setString(6, CommonUtility.validateString(bankReferenceContactName[i]));
					pstmt.setString(7, CommonUtility.validateString(bankReferenceAccountOrLoanNumber[i]));
					count = pstmt.executeUpdate();
					System.out.println("insertCreditApplicationBankReference :  "+count);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
		    	ConnectionManager.closeDBStatement(pstmt);
		    }
			return count;
		}
		
		public static String getUserCustomField(int userId, String customFieldName){
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet  rs =null;
			String custFieldValue = "";
			
		
			try
			{
				conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("getUserCustomFieldValues");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,customFieldName);
				pstmt.setInt(2,userId);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()){
					custFieldValue = rs.getString("TEXT_FIELD_VALUE");
				}
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return custFieldValue;
		}


		public static int getAdvPassword24Hours(String secretCode){
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet  rs =null;
			int count =0;
			
		
			try
			{
				conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("getAdvPassword24Hours");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,secretCode);			
				
				rs = pstmt.executeQuery();
				
				if(rs.next()){
					count=count+1;
				}
				
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return count;
		}
		public static String getSubScripitonCustomFieldsStatus(String userId, String customFieldName){
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet  rs =null;
			String status = "";
			
		
			try
			{
				conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("getCustomFieldsSubScripitonStatus");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,customFieldName);
				pstmt.setString(2,userId);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()){
	                status = rs.getString("TEXT_FIELD_VALUE");
				}
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return status;
		}
		public static int deleteBuyingCompanyAddressFromAddressBook(Connection conn,int buyingCompanyId){

			PreparedStatement pstmt = null;
			String sql = "DELETE FROM BC_ADDRESS_BOOK WHERE BUYING_COMPANY_ID = ?";
			int count = 0;
			try
			{
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, buyingCompanyId);
				count = pstmt.executeUpdate();

				conn.commit();
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally{
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
			return count;
		}

		public static int insertCustomField(String customFiledValue, String fieldName, int userId, int entityId, String dataEntity){

			Connection  conn = null;
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			int customFieldId = 0;
			int insertedRecords =0;
			int localeId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("DEFAULT_LOCALE_ID"));
			try {
				conn = ConnectionManager.getDBConnection();
				customFieldId = getCustomFieldId(conn, fieldName, dataEntity);
				if(customFieldId>0){
					int locCustomFieldValueId = getCustomFieldValueId(conn, customFiledValue);
					if(locCustomFieldValueId < -1){
						int count = insertCustomFieldValue(conn, customFiledValue, localeId, userId);
						if(count > 0){
							locCustomFieldValueId = getCustomFieldValueId(conn, customFiledValue);
						}
					}
					int dataEntityCustomFieldId = 0;
					if(dataEntity.equalsIgnoreCase("BUYING_COMPANY")){
						dataEntityCustomFieldId = getDataEntityCustomFieldId(conn, customFieldId, entityId, dataEntity);
					}else if(dataEntity.equalsIgnoreCase("USER")){
						dataEntityCustomFieldId = getDataEntityCustomFieldId(conn, customFieldId, userId, dataEntity);
					}else if(dataEntity.equalsIgnoreCase("BC_ADDRESS_BOOK")){
						dataEntityCustomFieldId = getDataEntityCustomFieldId(conn, customFieldId, entityId, dataEntity);
					}
					 
					
					if(dataEntityCustomFieldId > 0){
						insertedRecords = updateCustomFieldToDataEntity(conn, userId, entityId, customFieldId, locCustomFieldValueId, dataEntity);
					}else {
						insertedRecords = insertCustomFieldToDataEntity(conn, userId, entityId, customFieldId, locCustomFieldValueId, dataEntity);
					}
				}

				if(insertedRecords>0){
					System.out.println("custom field update : "+fieldName+"-"+customFiledValue + "\n DATA_ENTITY - " + dataEntity);
				}else{
					System.out.println("custom field update failed");
				}

			} catch (SQLException e) { 
				e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);	
			}

			return insertedRecords;
		}

		public static int getCustomFieldValueId(Connection  conn, String customFiledValue){
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			int locCustomFieldValueId = -2;
			try{
				String sql = "SELECT LOC_CUSTOM_FIELD_VALUE_ID FROM LOC_CUSTOM_FIELD_VALUES WHERE TEXT_FIELD_VALUE = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, customFiledValue);
				rs = pstmt.executeQuery();
				if(rs.next()){
					locCustomFieldValueId = rs.getInt("LOC_CUSTOM_FIELD_VALUE_ID");
				}

			} catch (SQLException e) { 
				e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
			return locCustomFieldValueId;
		}
		public static int insertCustomFieldValue(Connection  conn, String customFiledValue, int localeId, int userId){
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			int count = 0;
			try{
				pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("customFieldValueInsert"));
				pstmt.setString(1, customFiledValue);
				pstmt.setInt(2, localeId);
				pstmt.setInt(3, userId);
				pstmt.setString(4, customFiledValue);
				count = pstmt.executeUpdate();

			} catch (SQLException e) { 
				e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
			return count;
		}
		public static int insertCustomFieldToDataEntity(Connection  conn, int userId, int buyingCompanyId, int customFieldId, int locCustomFieldValueId, String dataEntity){
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			int insertedRecords = 0;
			try{
				if(CommonUtility.validateString(dataEntity).equalsIgnoreCase("USER")){
				
					pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("insertUserCustomFieldValue"));
					pstmt.setInt(1, userId);
					pstmt.setInt(2, customFieldId);
					pstmt.setInt(3, locCustomFieldValueId);
					pstmt.setInt(4, userId);
				}else if(CommonUtility.validateString(dataEntity).equalsIgnoreCase("BUYING_COMPANY")){
					pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("insertBuyingCompanyCustomFieldValue"));	
					pstmt.setInt(1, buyingCompanyId);
					pstmt.setInt(2, customFieldId);
					pstmt.setInt(3, locCustomFieldValueId);
					pstmt.setInt(4, userId);
				}
				else{
					pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("insertBcAddressBookCustomFieldValue"));	
					pstmt.setInt(1, buyingCompanyId);
					pstmt.setInt(2, customFieldId);
					pstmt.setInt(3, locCustomFieldValueId);
					pstmt.setInt(4, userId);
				}
				insertedRecords = pstmt.executeUpdate();

			} catch (SQLException e) { 
				e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
			return insertedRecords;
		}
		
		public static int updateCustomFieldToDataEntity(Connection  conn, int userId, int buyingCompanyId, int customFieldId, int locCustomFieldValueId, String dataEntity){
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			int insertedRecords = 0;
			try{
				if(CommonUtility.validateString(dataEntity).equalsIgnoreCase("USER")){
				
					pstmt = conn.prepareStatement("UPDATE USER_CUSTOM_FIELD_VALUES SET LOC_CUSTOM_FIELD_VALUE_ID = ? WHERE USER_ID = ? and CUSTOM_FIELD_ID = ?");
					pstmt.setInt(1, locCustomFieldValueId);
					pstmt.setInt(2, userId);
					pstmt.setInt(3, customFieldId);
				}else{
					pstmt = conn.prepareStatement("UPDATE BC_CUSTOM_FIELD_VALUES SET LOC_CUSTOM_FIELD_VALUE_ID = ? WHERE BUYING_COMPANY_ID = ? and CUSTOM_FIELD_ID = ?");	
					pstmt.setInt(1, locCustomFieldValueId);
					pstmt.setInt(2, buyingCompanyId);
					pstmt.setInt(3, customFieldId);
				}
				insertedRecords = pstmt.executeUpdate();

			} catch (SQLException e) { 
				e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
			return insertedRecords;
		}
		
		public static int getCustomFieldId(Connection  conn, String fieldName, String dataEntity){
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			int customFieldId = 0;
			try{
				String sql = PropertyAction.SqlContainer.get("getCustomFieldId");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, fieldName);
				pstmt.setString(2, dataEntity);
				rs = pstmt.executeQuery();
				if(rs.next()){
					customFieldId = rs.getInt("CUSTOM_FIELD_ID");
				}

			} catch (SQLException e) { 
				e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
			return customFieldId;
		}
		
		public static int getDataEntityCustomFieldId(Connection  conn, int customFieldId, int id, String dataEntity){
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			int entityCfValueId = 0;
			try{
				String sql = "";
				if(dataEntity.equalsIgnoreCase("BUYING_COMPANY")){
					sql = "SELECT BC_CUSTOM_FIELD_VALUE_ID ENTITY_CF_VALUE_ID FROM BC_CUSTOM_FIELD_VALUES WHERE  CUSTOM_FIELD_ID = ? AND BUYING_COMPANY_ID = ?";
				}else if(dataEntity.equalsIgnoreCase("USER")){
					sql = "SELECT USER_CUSTOM_FIELD_VALUE_ID ENTITY_CF_VALUE_ID FROM USER_CUSTOM_FIELD_VALUES WHERE  CUSTOM_FIELD_ID = ? AND USER_ID = ?";
				}else if(dataEntity.equalsIgnoreCase("BC_ADDRESS_BOOK")){
					sql = "SELECT BC_AB_CUSTOM_FIELD_VALUE_ID ENTITY_CF_VALUE_ID FROM BC_AB_CUSTOM_FIELD_VALUES WHERE  CUSTOM_FIELD_ID = ? AND ADDRESS_BOOK_ID = ?";
				}
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, customFieldId);
				pstmt.setInt(2, id);
				rs = pstmt.executeQuery();
				if(rs.next()){
					entityCfValueId = rs.getInt("ENTITY_CF_VALUE_ID");
				}

			} catch (SQLException e) { 
				e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
			return entityCfValueId;
		}
		public static  LinkedHashMap<String, String> getAllCustomerCustomFieldValue(int buyingCompanyID)
		{
			Connection  conn = null;
		    PreparedStatement preStat=null;
		    ResultSet rs = null;
		    String textFieldValue = null;
		    String textFieldName = null;
		    LinkedHashMap<String, String> customValues = new LinkedHashMap<String, String>();
		    
		    try {
		    	conn = ConnectionManager.getDBConnection();
		          preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getAllCustomerCustomFieldValue"));	
		          preStat.setInt(1, buyingCompanyID);
				  rs =preStat.executeQuery();

				  while(rs.next()){
					  textFieldName = rs.getString("FIELD_NAME");
					  textFieldValue =  rs.getString("FILED_VALUE");
					  customValues.put(textFieldName, textFieldValue);
				  }
		    	} catch (Exception e) {         
		          e.printStackTrace();

		      } finally {	    	
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(preStat);
		    	  ConnectionManager.closeDBConnection(conn);	
		      } 
		      return customValues;
		}
		public static boolean saveCXMLOrderInDB(String orderRef, String senderNetworkId,StringBuilder cxmlFile) {
		boolean success = false;

		ResultSet rs = null;
		String sqlQuery = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		

		try {
			 conn = ConnectionManager.getDBConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}

		

		try {

			sqlQuery = "INSERT INTO CXML_ORDERS(CXML_ORDERID, CUSTOMER_ORDER_REF,SENDER_NETWORKID,ORDER_FILE,RECEIVED_DATETIME) VALUES(CXMLORDER_ID_SEQ.NEXTVAL,?,?,?,SYSDATE)";
			
			pstmt =  conn.prepareStatement(sqlQuery);
			pstmt.setString(1, orderRef);
			pstmt.setString(2, senderNetworkId);
			pstmt.setString(3, cxmlFile.toString());
			
			pstmt.executeUpdate();
			success = true;

		} catch (Exception e) {
			// UNLog.jobError(e.getStackTrace(), 1);
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);

		} // finally

		return success;
	}
	
		public static boolean saveFlatFileInDB(String fromUrl, String fromIp,StringBuilder cxmlFile,String docType) {
			boolean success = false;

			ResultSet rs = null;
			String sqlQuery = null;
			Connection conn = null;
			PreparedStatement pstmt = null;
			

			try {
				 conn = ConnectionManager.getDBConnection();
			

				sqlQuery = "INSERT INTO DOCUMENT_RECEIVED(DOCUMENT_ID, FROM_URL,DOCUMENT_RECEIVED,DOCUMENT_TYPE,FROM_IP,DATETIME_RECEIVED) VALUES(DOCUMENT_ID_SEQ.NEXTVAL,?,?,?,?,SYSDATE)";
				
				pstmt = conn.prepareStatement(sqlQuery);
				pstmt.setString(1, fromUrl);
				pstmt.setString(2, cxmlFile.toString());
				pstmt.setString(3, docType);
				pstmt.setString(4, fromIp);
				
				
				pstmt.executeUpdate();
				success = true;

			} catch (Exception e) {
				// UNLog.jobError(e.getStackTrace(), 1);
				e.printStackTrace();

			} finally {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);

			} // finally

			return success;
		}
		
		public static int getBcAddressBookShipBillId(int buyingCompanyId, String entityId,AddressType value) {

			Connection conn = null;
			PreparedStatement preStat = null;
			ResultSet rs = null;
			int bcAddressBookId = 0;
			String address = "";
			if (value == AddressType.Bill) {
				address = "Bill";
			} else {
				address = "Ship";
			}
			try {
				conn = ConnectionManager.getDBConnection();
				preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getBcAddressBookShipBillId"));
				preStat.setString(1, entityId);
				preStat.setString(2,address);
				preStat.setInt(3,buyingCompanyId);
				rs = preStat.executeQuery();
				if (rs.next()) {
					if (rs.getInt("BC_ADDRESS_BOOK_ID") > 0) {
						bcAddressBookId = rs.getInt("BC_ADDRESS_BOOK_ID");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			}

			return bcAddressBookId;
		}
		public static int checkDefaultBillShipExist(int userId,AddressType value)
		{
			int addressid = 0;
			String address = "";
			if (value == AddressType.Bill) {
				address = "DEFAULT_BILLING_ADDRESS_ID";
			} else {
				address = "DEFAULT_SHIPPING_ADDRESS_ID";
			}
			String sql = "SELECT CU."
					+ address
					+ " FROM CIMM_USERS CU, BC_ADDRESS_BOOK BCA WHERE CU.USER_ID = ? AND BCA.BC_ADDRESS_BOOK_ID = CU."
					+ address + " AND BCA.BUYING_COMPANY_ID = CU.BUYING_COMPANY_ID";
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (value == AddressType.Bill) {
						addressid = rs.getInt("DEFAULT_BILLING_ADDRESS_ID");
					} else {
						addressid = rs.getInt("DEFAULT_SHIPPING_ADDRESS_ID");

					}
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			} finally {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);

			}
			return addressid;

		}

		public static int updateDefaultShiptoBillSupp(int billshiptoId,int userId,AddressType value)
		{
			Connection  conn = null;
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			String sql = "";
			int count = 0;

			try {
				conn = ConnectionManager.getDBConnection();
				if(value == AddressType.Bill){
					sql = "UPDATE CIMM_USERS SET DEFAULT_BILLING_ADDRESS_ID=? WHERE USER_ID=?";
				}
				else{
					sql = "UPDATE CIMM_USERS SET DEFAULT_SHIPPING_ADDRESS_ID=? WHERE USER_ID=?";
				}
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,billshiptoId);
				pstmt.setInt(2,userId);
				count = pstmt.executeUpdate();

			} catch (SQLException e) { 
				e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);	
			}
			return count;

		}
		public static int getBcAddressBookJob(String entityId,int buyingCompanyId,String type) {

			Connection conn = null;
			PreparedStatement preStat = null;
			ResultSet rs = null;
			int bcAddressBookId = 0;
			String address = "";
		
			try {
				conn = ConnectionManager.getDBConnection();
				preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getBcAddressJobEntityId"));
				preStat.setString(1, entityId);
				preStat.setInt(2, buyingCompanyId);
				preStat.setString(3, type);
				rs = preStat.executeQuery();
				if (rs.next()) {
					if (rs.getInt("BC_ADDRESS_BOOK_ID") > 0) {
						bcAddressBookId = rs.getInt("BC_ADDRESS_BOOK_ID");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			}

			return bcAddressBookId;
		}
		
		public static int addBcAddressBookJobEntityId(String entityId,String customerName,String value,int buyingCompanyId) {

			Connection conn = null;
			PreparedStatement preStat = null;
			ResultSet rs = null;
			int bcAddressBookId = 0;
			int count = 0;
			String address="";
			int addressBookId=0;
			String address1 =entityId;
			if (value != null) {
				address = value;
			}
			
			try {
				conn = ConnectionManager.getDBConnection();
				conn.setAutoCommit(false);
				addressBookId = CommonDBQuery.getSequenceId("BC_ADDRESS_BOOK_ID_SEQ");
				String sql = PropertyAction.SqlContainer.get("insertIntoBcAddressJobEntityId");
				preStat = conn.prepareStatement(sql);
				preStat.setInt(1, addressBookId);
				preStat.setInt(2,buyingCompanyId);
				preStat.setString(3,entityId);
				preStat.setString(4, customerName);
				preStat.setString(5, value);
				preStat.setString(6, address1);
				count = preStat.executeUpdate();
				if(count<1){
					bcAddressBookId = 0;
					conn.rollback();
				}else{
					conn.commit();
				}
					
			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			}

			return bcAddressBookId;
		}
		public static int addBuyingCompanyJobEntityId(String entityId,String customerName,String value,int ParentCompanyId) {

			Connection conn = null;
			PreparedStatement preStat = null;
			ResultSet rs = null;
			int bcAddressBookId = 0;
			int count = 0;
			String address="";
			int buyingCompanyId=0;
			String address1 =customerName;
			String shortName=value;
			String customerType="C";
			String generalCatalog="N";
			String status="A";
			if (value != null) {
				address = value;
			}
			 try {
				conn = ConnectionManager.getDBConnection();
				conn.setAutoCommit(false);
				buyingCompanyId = CommonDBQuery.getSequenceId("BUYING_COMPANY_ID_SEQ");
				String sql = PropertyAction.SqlContainer.get("insertIntoBuyingCompanyJobEntityId");
				preStat = conn.prepareStatement(sql);
				preStat.setInt(1, buyingCompanyId);
				preStat.setString(2,entityId);
				preStat.setString(3, customerName);
				preStat.setString(4, shortName);
				preStat.setString(5, customerType);
				preStat.setString(6, address1);
				preStat.setString(7, status);
				preStat.setString(8, generalCatalog);
				preStat.setInt(9, ParentCompanyId);
				count = preStat.executeUpdate();
				if(count<1){
					bcAddressBookId = 0;
					conn.rollback();
				}else{
					conn.commit();
				}
					
			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			}

			return buyingCompanyId;
		}
	public static ArrayList<UsersModel> getListOfJobAccount(int userId) {

			Connection conn = null;
			PreparedStatement preStat = null;
			ResultSet rs = null;
			int entityId = 0;
			String address = "";
			 ArrayList<UsersModel> JobAddressList =null;
			try {
				conn = ConnectionManager.getDBConnection();
				JobAddressList = new ArrayList<UsersModel>();
				preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getListOfJobAccounts"));
				preStat.setInt(1, userId);
				rs = preStat.executeQuery();
				  while(rs.next()){	
					  UsersModel addressListVal = new UsersModel();
					  addressListVal.setCustomerId(rs.getString("ENTITY_ID"));
					  addressListVal.setCustomerName(rs.getString("FIRST_NAME"));
					  addressListVal.setAddressType(rs.getString("ADDRESS_TYPE"));
					  addressListVal.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
					  addressListVal.setDefaultShipToId(rs.getInt("DEFAULT_SHIPPING_ADDRESS_ID"));
					  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOKID"));
					  if(rs.getString("ADDRESS_TYPE").trim().equalsIgnoreCase("JOB"))
					  {
						  JobAddressList.add(addressListVal);
					  }
					 
				  }
				
			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			}

			return JobAddressList;
		}
			
	public static String getBuyingCompanyIdByCustomerName(String entityId){
		String CUSTOMER_NAME = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
        try{
        	conn = ConnectionManager.getDBConnection();
        	//String sql = "select buying_company_id from buying_company where ENTITY_ID =?"; // 09 June 2014 coz of Gerrie Exes User Reg
			String sql = "select CUSTOMER_NAME from buying_company where  STATUS= 'A' and ENTITY_ID =?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(entityId).toUpperCase());
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				CUSTOMER_NAME = rs.getString("CUSTOMER_NAME");
			}
        }catch(SQLException e) {
        	CUSTOMER_NAME =null;
        	e.printStackTrace();
        }catch(Exception e){
        	CUSTOMER_NAME =null;
        	e.printStackTrace();
        }
        finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
		return CUSTOMER_NAME;
	}
	
	public static String checkForAnonymous(String userName){
		String entityid = "0";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
        try{
        	conn = ConnectionManager.getDBConnection();
        	String sql = "select ECLIPSE_CONTACT_ID from cimm_users where upper(user_name)=? and site_id=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName.toUpperCase());
			pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				entityid = rs.getString("ECLIPSE_CONTACT_ID");
			}
        }catch(SQLException e) {
        	entityid =null;
        	e.printStackTrace();
        }catch(Exception e){
        	entityid =null;
        	e.printStackTrace();
        }
        finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
	    }
		return entityid;
	}
	public static boolean updateAnonymousUserToRegister(String userName)
	{
		Connection  conn = null;
		boolean userExist = false;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String status = "";
	    SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
	    String date = s.format(new java.util.Date());
	    String updatedUser= userName +"_DEL_"+date;
		try {
            conn = ConnectionManager.getDBConnection();
            String sql =PropertyAction.SqlContainer.get("updateAnonymousUser");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "D");
            pstmt.setString(2, updatedUser);   
            pstmt.setString(3, userName);   
            pstmt.setInt(4, CommonDBQuery.getGlobalSiteId());
            rs = pstmt.executeQuery();
            if(rs.next())
			{
            	status = "D";
            	userExist=false;
			}
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	ConnectionManager.closeDBResultSet(rs);
    	ConnectionManager.closeDBPreparedStatement(pstmt);
    	ConnectionManager.closeDBConnection(conn);	
      }
		return userExist;
		
	}
		public static LinkedHashMap<String, String> numberOfTimesLoggedInLoggedout(int userId)
		{
			Connection  conn = null;
			PreparedStatement pstmt = null;   
			ResultSet rs = null;
			LinkedHashMap<String, String> LoginLogout=new LinkedHashMap<String, String>();
			try{
				conn = ConnectionManager.getDBConnection();
				String sql=PropertyAction.SqlContainer.get("getNumberOfTimesLoggedInLoggedout");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				rs=pstmt.executeQuery();
				while(rs.next())
				{	
					rs.getString(1);
					String keyValue = "";
					if(CommonUtility.validateString(rs.getString("ACTION")).equalsIgnoreCase("SignOut Click")){
						keyValue = "Logged Out";
					}
					if(CommonUtility.validateString(rs.getString("ACTION")).equalsIgnoreCase("Login")){
						keyValue = "Logged In";
					}
					LoginLogout.put(keyValue,rs.getString("COUNT(ACTION)"));
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			finally{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return LoginLogout;

			
		}
		public static  LinkedHashMap<String, String> lastLogin(int userId)
		{ 
			long startTimer = CommonUtility.startTimeDispaly();
			Connection  conn = null;
		   PreparedStatement pstmt = null;   
		   ResultSet rs = null;
		   String lastLogin=null;
		   String dateTime=null;
		   String database_timezone=null;
		   String current_timestamp=null;
		   String action_type=null;
		   LinkedHashMap<String, String> lastLogindate=new LinkedHashMap<String, String>();
		   try{
		    conn = ConnectionManager.getDBConnection();
		    String sql=PropertyAction.SqlContainer.get("getlastLogin");
		    pstmt = conn.prepareStatement(sql);
		    pstmt.setInt(1, userId);
		    rs=pstmt.executeQuery();
		     for (int i=0;i<2;i++){
		     rs.next();
		     lastLogin=rs.getString(2);
		        dateTime=rs.getString(3);
		        database_timezone=rs.getString(4);
		        current_timestamp=rs.getString(5);
		        action_type=rs.getString(7);
		        lastLogindate.put("lastLogin",lastLogin);
		        lastLogindate.put("dateTime",dateTime);
		        lastLogindate.put("database_timezone",database_timezone);
		        lastLogindate.put("current_timestamp",current_timestamp);
		        lastLogindate.put("action_type",action_type);
		      }
		   } catch(Exception e){
		    e.printStackTrace();
		   }
		   finally {
		    ConnectionManager.closeDBResultSet(rs);
		    ConnectionManager.closeDBStatement(pstmt);
		    ConnectionManager.closeDBConnection(conn);
		  }
		   CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		   return lastLogindate;
		}
		
		public static int updateUserProfileImage(String fileName,int userId)
		{
			int count = -1;
			Connection  conn = null;
			PreparedStatement pstmt = null;
				
	        try
	        {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("uploadUserProfileImage");
				//DEFAULT_BILLING_ADDRESS_ID=? AND DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE USER_ID=?
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, CommonUtility.validateString(fileName));
				pstmt.setInt(2, userId);
				count = pstmt.executeUpdate();
	        }
	        catch(SQLException e)
	        {
	        	count =-1;
	        	e.printStackTrace();
	        }
	        finally{
				ConnectionManager.closeDBStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
	        }
			return count;
		}
		
		public static int updateCustomerLogoImagePath(String fileName,int buyingCompanyId) {

			int count = -1;
			Connection  conn = null;
			PreparedStatement pstmt = null;
				
	        try
	        {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("uploadCustomerLogo");
				//DEFAULT_BILLING_ADDRESS_ID=? AND DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE USER_ID=?
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, CommonUtility.validateString(fileName));
				pstmt.setInt(2, buyingCompanyId);
				count = pstmt.executeUpdate();
	        }
	        catch(SQLException e)
	        {
	        	count =-1;
	        	e.printStackTrace();
	        }
	        finally{
				ConnectionManager.closeDBStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
	        }
			return count;
		
		}
		public static String getContactID(int userId)
		{
			
			Connection conn = null;
			String contactID="0";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = PropertyAction.SqlContainer.get("getcontactId");
			try
			{
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					contactID=(rs.getString("EXTERNAL_SYSTEM2_USER_ID"));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally
			{
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
				ConnectionManager.closeDBResultSet(rs);
				
			}
		return contactID;
			
		}
		public static boolean saveSpamEmailsInDB(String toAddress, String cc, String bcc, String user_id, String emailSubject) {

			boolean success = false;
			ResultSet rs = null;
			String sqlQuery = null;
			Connection conn = null;
			Date date = new Date();
			PreparedStatement pstmt = null;
			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

			try {
				conn = ConnectionManager.getDBConnection();
				sqlQuery = "INSERT INTO BLOCKED_MAIL_CONTENT(BLOCKED_EMAIL_ID, TO_EMAIL, CC_EMAIL, BCC_EMAIL, USER_EDITED, UPDATED_DATETIME, SUBJECT) VALUES (BLOCKED_EMAIL_SEQ.NEXTVAL, ?, ?, ?, ?, to_date(?,'yyyy-mm-dd HH24:MI:SS'),?)";
				// pstmt = (OraclePreparedStatement)
				// conn.prepareStatement(sqlQuery);
				pstmt = conn.prepareStatement(sqlQuery);
				pstmt.setString(1, toAddress);
				if (bcc == null) {
					pstmt.setString(2, "");
				} else {
					pstmt.setString(2, cc);
				}
				if (bcc == null) {
					pstmt.setString(3, "");
				} else {
					pstmt.setString(3, bcc);
				}
				pstmt.setString(4, user_id);
				pstmt.setString(5, simpledateformat.format(date));
				pstmt.setString(6, emailSubject);
				pstmt.executeUpdate();
				success = true;

			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return success;
		}

		public static int saveSendPageMail(String ipaddress, int requestcount, String sessionId) {
			ResultSet rs = null;
			String sqlQuery = null;
			Connection conn = null;
			Date date = new Date();
			PreparedStatement pstmt = null;
			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			long current_time = System.currentTimeMillis();
			int cnt = 0;
			int total_count = 0;

			try {
				conn = ConnectionManager.getDBConnection();
				sqlQuery = "SELECT TOTAL_COUNT FROM SEND_MAIL_REQUEST WHERE USER_IP_ADDRESS = ?";
				pstmt = conn.prepareStatement(sqlQuery);
				pstmt.setString(1, ipaddress);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					total_count = rs.getInt("TOTAL_COUNT");
				}
				if(total_count<1){
				sqlQuery = "INSERT INTO SEND_MAIL_REQUEST (USER_IP_ADDRESS, REQUEST_COUNT, SESSION_ID, UPDATED_DATETIME, TOTAL_COUNT, LAST_HIT_TIME) VALUES (?, ?, ?, to_date(?,'yyyy-mm-dd HH24:MI:SS'),?,?)";
				ConnectionManager.closeDBPreparedStatement(pstmt);
				pstmt = conn.prepareStatement(sqlQuery);
				pstmt.setString(1, ipaddress);
				pstmt.setInt(2, requestcount);
				pstmt.setString(3, sessionId);
				pstmt.setString(4, simpledateformat.format(date));
				pstmt.setInt(5, requestcount);
				pstmt.setLong(6, current_time);
				cnt = pstmt.executeUpdate();
				}else{
					updateSendPageMail(requestcount,ipaddress);
				}
			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return cnt;
		}

		public static int getSendMailCount(String ipaddress) {
			Connection conn = null;
			PreparedStatement preStat = null;
			ResultSet rs = null;
			int requestCount = 0;
			String sqlQuery = null;
			Date date = new Date();
			int timePerIp = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TIME_LIMIT_PERIP"));
			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MMM-dd");
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			Date today = new Date();

			Date todayWithZeroTime = null;
			try {
				todayWithZeroTime = formatter.parse(formatter.format(today));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String currentDate = formatter.format(date);
			Date lastDate = new Date();
			long lastHit=0;
			int cnt = 0;
			long current_time = System.currentTimeMillis();
			try {
				conn = ConnectionManager.getDBConnection();
				sqlQuery = "SELECT REQUEST_COUNT,UPDATED_DATETIME,LAST_HIT_TIME FROM SEND_MAIL_REQUEST WHERE USER_IP_ADDRESS = ?";
				preStat = conn.prepareStatement(sqlQuery);
				preStat.setString(1, ipaddress);
				rs = preStat.executeQuery();
				if (rs.next()) {
					requestCount = rs.getInt("REQUEST_COUNT");
					lastDate = rs.getDate("UPDATED_DATETIME");
					lastHit = rs.getLong("LAST_HIT_TIME");	
				}
				long Seconds = (System.currentTimeMillis()-lastHit)/1000;
				lastDate = formatter.parse(formatter.format(lastDate));
				if((todayWithZeroTime.compareTo(lastDate)>0)||(Seconds>timePerIp)){
					sqlQuery = "UPDATE SEND_MAIL_REQUEST SET REQUEST_COUNT = ?, LAST_HIT_TIME =?, UPDATED_DATETIME=to_date(?,'yyyy-mm-dd HH24:MI:SS') WHERE USER_IP_ADDRESS = ?";
					preStat = conn.prepareStatement(sqlQuery);
					preStat.setInt(1, 0);
					preStat.setLong(2,current_time);
					preStat.setString(3, simpledateformat.format(date));
					preStat.setString(4, ipaddress);
					cnt = preStat.executeUpdate();
					requestCount = 0;
				}
				ConnectionManager.closeDBPreparedStatement(preStat);

			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			} // finally

			return requestCount;
		}

		public static void updateSendPageMail(int requestCount, String ipaddress) {
			Connection conn = null;
			PreparedStatement preStat = null;
			ResultSet rs = null;
			String sqlQuery = null;
			int cnt = 0;
			int total_count = 0;
			Date date = new Date();
			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MMM-dd");
			try {
				conn = ConnectionManager.getDBConnection();
				sqlQuery = "SELECT TOTAL_COUNT FROM SEND_MAIL_REQUEST WHERE USER_IP_ADDRESS = ?";
				preStat = conn.prepareStatement(sqlQuery);
				preStat.setString(1, ipaddress);
				rs = preStat.executeQuery();
				if (rs.next()) {
					total_count = rs.getInt("TOTAL_COUNT");
				}
				sqlQuery = "UPDATE SEND_MAIL_REQUEST SET REQUEST_COUNT = ?,TOTAL_COUNT =?,UPDATED_DATETIME=to_date(?,'yyyy-mm-dd HH24:MI:SS')  WHERE USER_IP_ADDRESS = ?";
				ConnectionManager.closeDBPreparedStatement(preStat);
				preStat = conn.prepareStatement(sqlQuery);
				preStat.setInt(1, requestCount);
				preStat.setInt(2, total_count+1);
				preStat.setString(3, simpledateformat.format(date));
				preStat.setString(4, ipaddress);
				cnt = preStat.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			} // finally

			return;
		}
		
		public static String getCanonicalUrlValue(String pageID){
			ResultSet rs = null;
		    Connection  conn = null;
		    PreparedStatement pstmt=null;
		    String sql = "SELECT CANONICAL_URL FROM STATIC_PAGES WHERE STATIC_PAGE_ID=?";
		    String canonicalUrl = "";
		    int pageId = CommonUtility.validateNumber(pageID);
		    try {
		    	conn = ConnectionManager.getDBConnection();
		    	pstmt=conn.prepareStatement(sql);
		    	pstmt.setInt(1, pageId);
		    	rs=pstmt.executeQuery();           
		    	if(rs.next()){  
		    		if(rs.getString("CANONICAL_URL")!=null){
		    			canonicalUrl = rs.getString("CANONICAL_URL");
		    		}
		    	}
		    } catch (Exception e) {         
		    	e.printStackTrace();
		    } finally {
		    	ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);	
		    } // finally
		    return canonicalUrl;
		}
		public static String checkForCustomerType(String email){

			ResultSet rs = null;
		    Connection  conn = null;
		    PreparedStatement pstmt=null;
		    String sql = "SELECT GUEST_USER FROM CIMM_USERS WHERE USER_NAME =?";
		    String customerType = "";
		    
		    try {
		    	conn = ConnectionManager.getDBConnection();
		       pstmt=conn.prepareStatement(sql);
		       pstmt.setString(1, email);
		       rs=pstmt.executeQuery();
		                   
		       if(rs.next()){  
		        	if(rs.getString("GUEST_USER")!=null){
		        		customerType = rs.getString("GUEST_USER");
		        	}
		       }
		         
		      } catch (Exception e) {         
		          e.printStackTrace();

		      } finally {
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	  ConnectionManager.closeDBConnection(conn);	
		      } // finally
			return customerType;
		}
		public static String getCustomerAccount(String email){

			ResultSet rs = null;
		    Connection  conn = null;
		    PreparedStatement pstmt=null;
		    String sql = "SELECT ECLIPSE_CONTACT_ID FROM CIMM_USERS WHERE USER_NAME =?";
		    String customerAccount = "";
		    
		    try {
		    	conn = ConnectionManager.getDBConnection();
		       pstmt=conn.prepareStatement(sql);
		       pstmt.setString(1, email);
		       rs=pstmt.executeQuery();
		                   
		       if(rs.next()){  
		        	if(rs.getString("ECLIPSE_CONTACT_ID")!=null){
		        		customerAccount = rs.getString("ECLIPSE_CONTACT_ID");
		        	}
		       }
		         
		      } catch (Exception e) {         
		          e.printStackTrace();

		      } finally {
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	  ConnectionManager.closeDBConnection(conn);	
		      } // finally
			return customerAccount;
		}
		public static int deleteExistingGuestUser(String userName)
		{
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    ResultSet rs = null;
		    String sql = "";
		    int count = 0;
		
			try {
	            conn = ConnectionManager.getDBConnection();
	            sql = "UPDATE CIMM_USERS SET USER_NAME = USER_NAME||'_DEL_'||TO_CHAR(SYSDATE,'DDMM'), STATUS = 'D' WHERE USER_TYPE='ECOMM' AND USER_NAME=? AND SITE_ID=?";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, userName);
	            pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
	            count = pstmt.executeUpdate();
	           
	    } catch (SQLException e) { 
	        e.printStackTrace();
	    }
	    finally {	    
	    	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);
	    	ConnectionManager.closeDBConnection(conn);	
	      }
			return count;
			
		}
		public static int getBuyingCompanyIdByUserName(String userName){
			ResultSet rs = null;
		    Connection  conn = null;
		    PreparedStatement pstmt=null;
		    String sql = "SELECT BUYING_COMPANY_ID FROM CIMM_USERS WHERE USER_NAME =? AND SITE_ID=?";
		    int buyingCompanyId=0;
		    
		    try {
		    	conn = ConnectionManager.getDBConnection();
		       pstmt=conn.prepareStatement(sql);
		       pstmt.setString(1, userName);
		       pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
		       rs=pstmt.executeQuery();
		                   
		       if(rs.next()){  
		        	
		        		buyingCompanyId = rs.getInt("BUYING_COMPANY_ID");
		        
		       }
		         
		      } catch (Exception e) {         
		          e.printStackTrace();

		      } finally {
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	  ConnectionManager.closeDBConnection(conn);	
		      } // finally
			return buyingCompanyId;
		}
		
		
		public static int deleteExistingCustomer(int buyingCompanyId)
		{
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    ResultSet rs = null;
		    String sql = "";
		    int count = 0;
		
			try {
	            conn = ConnectionManager.getDBConnection();
	            sql = "UPDATE BUYING_COMPANY SET CUSTOMER_NAME = CUSTOMER_NAME||'_DEL_'||TO_CHAR(SYSDATE,'DDMM'), STATUS = 'D' WHERE CUSTOMER_TYPE='G' AND BUYING_COMPANY_ID=?";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, buyingCompanyId);
	            count = pstmt.executeUpdate();
	           
	    } catch (SQLException e) { 
	        e.printStackTrace();
	    }
	    finally {	    
	    	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);
	    	ConnectionManager.closeDBConnection(conn);	
	      }
			return count;
			
		}
		public static String getCustomerProfileId(int userId)
		  {
		   Connection  conn = null;
		   PreparedStatement pstmt = null;
		   ResultSet rs = null;
		   String customerProfileId = null;
		    
		         try
		         {
		          conn = ConnectionManager.getDBConnection();
		          String sql = PropertyAction.SqlContainer.get("getUserCustomerProfileId");
		    //DEFAULT_BILLING_ADDRESS_ID=? AND DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE USER_ID=?
		    pstmt = conn.prepareStatement(sql);
		    pstmt.setInt(1, userId);
		    rs = pstmt.executeQuery();
		    if(rs.next()){
		     customerProfileId = rs.getString("CUSTOMER_PROFILE_ID");
		    }
		         }
		         catch(SQLException e)
		         {
		          e.printStackTrace();
		         }
		         finally{
		    ConnectionManager.closeDBStatement(pstmt);
		    ConnectionManager.closeDBConnection(conn);
		         }
		   return customerProfileId;
		  }
		public static void updateCustomerProfileId(String customerProfileId, int userId)
		  {
		   Connection  conn = null;
		   PreparedStatement pstmt = null;
		   int count = 0;
		         try
		         {
		          conn = ConnectionManager.getDBConnection();
		          String sql = PropertyAction.SqlContainer.get("updateCustomerProfileId");
		    //DEFAULT_BILLING_ADDRESS_ID=? AND DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE USER_ID=?
		    pstmt = conn.prepareStatement(sql);
		    pstmt.setString(1, customerProfileId);
		    pstmt.setInt(2, userId);
		    count = pstmt.executeUpdate();
		    
		    if(count > 0) {
		     System.out.println("Customer Profile Id updated successfully");
		    } else {
		     System.out.println("Customer Profile Id not updated");
		    }
		    
		         }
		         catch(SQLException e)
		         {
		          e.printStackTrace();
		         }
		         finally{
		    ConnectionManager.closeDBStatement(pstmt);
		    ConnectionManager.closeDBConnection(conn);
		         }

		  }
		public static ArrayList<EventModel> getEventCustomFields(){
			System.out.println("Getting events Custom fields");
			PreparedStatement stmt = null;
			ResultSet rs = null;		
			Connection conn = null;
			ArrayList<EventModel> eventCustomFieldsList = new ArrayList<EventModel>();
			try{
				conn =  ConnectionManager.getDBConnection();
				String sql = "SELECT * FROM EVENTS_CF_VALUES_VIEW ORDER BY EVENT_ID";
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery(sql);
				while(rs.next())
		           {
		        	   
					   EventModel eventObject = new EventModel();
					   eventObject.setId(rs.getInt("EVENT_ID"));
					   eventObject.setTitle(rs.getString("EVENT_TITLE"));
					   eventObject.setEventCategory(rs.getString("EVENT_CATEGORY"));
					   eventObject.setFieldName(rs.getString("FIELD_NAME"));
					   eventObject.setFieldValue(rs.getString("FIELD_VALUE"));
		        	   eventCustomFieldsList.add(eventObject);
		           }
			}catch (SQLException e){
				e.printStackTrace();
			}catch (Exception e){
				e.printStackTrace();
			}finally{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBStatement(stmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return eventCustomFieldsList;
		}
	
		public static ArrayList<EventModel> getSingleEventCustomFields(int eventid){
			
			String sql = "SELECT * FROM EVENTS_CF_VALUES_VIEW WHERE EVENT_ID = ?";
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet  rs =null;
			ArrayList<EventModel> eventCustomFieldsList = new ArrayList<EventModel>();
			try
			{
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, eventid);
				rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					EventModel eventObject = new EventModel();
					eventObject.setId(rs.getInt("EVENT_ID"));
					eventObject.setTitle(rs.getString("EVENT_TITLE"));
					eventObject.setEventCategory(rs.getString("EVENT_CATEGORY"));
					eventObject.setFieldName(rs.getString("FIELD_NAME"));
					eventObject.setFieldValue(rs.getString("FIELD_VALUE"));
					eventCustomFieldsList.add(eventObject);
				}
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally{
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
				ConnectionManager.closeDBResultSet(rs);
			}
			
			return eventCustomFieldsList;
		}
		public static boolean copyFile(File from, File to, HttpSession session) {
			try {
				FileInputStream fileInStream = new FileInputStream(from);
				FileOutputStream fileOutStream = new FileOutputStream(to);
				boolean emptyFile = true;
				byte buf[] = new byte[2048];
				int i = 0;

				while((i = fileInStream.read(buf)) != -1) {
					emptyFile = false;
					fileOutStream.write(buf, 0, i);
				}
				fileInStream.close();
				fileOutStream.close();
				if(emptyFile == true) {
					to.delete();
					return false;
				}
				return true;
			}
			catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		public static HashMap<String,String> getBCAddressBookCustomFields(int addressBookId){
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    ResultSet rs = null;
		    String textFieldValue = null;
		    String textFieldName = null;
		    LinkedHashMap<String, String> customValues = new LinkedHashMap<String, String>();
		    
		    
		    try {
		    	conn = ConnectionManager.getDBConnection();
		    	pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getAllBCAddressBookCustomFields"));			 
		    	pstmt.setInt(1, addressBookId);
				rs = pstmt.executeQuery();
				  while(rs.next()){
					  textFieldName = rs.getString("FIELD_NAME");
					  textFieldValue =  rs.getString("TEXT_FIELD_VALUE");
					  customValues.put(textFieldName, textFieldValue);
				  }
		      } catch (Exception e) {         
		          e.printStackTrace();
		      }
		      finally{
			    	ConnectionManager.closeDBResultSet(rs);
					ConnectionManager.closeDBPreparedStatement(pstmt);
					ConnectionManager.closeDBConnection(conn);
			  }
			return customValues;
		}
		public static int updateBCAddressBookBS(AddressModel addressList)
		{
			int count=0;
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    String sql = "";
			try {
				conn = ConnectionManager.getDBConnection();
	            sql = PropertyAction.SqlContainer.get("updateBCAddressBook");
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, addressList.getAddress1());
	            pstmt.setString(2, addressList.getAddress2());
	            pstmt.setString(3, addressList.getCity());
	            pstmt.setString(4, addressList.getState());
	            pstmt.setString(5, addressList.getZipCode());
	            pstmt.setString(6, CommonUtility.getCountryCode(addressList.getCountry(), "updateBCAddressBook"));
	            pstmt.setString(7, addressList.getPhoneNo());//pstmt.setString(7, addressList.getPhoneNo());
		        pstmt.setString(8, addressList.getFirstName());
		        pstmt.setString(9, addressList.getLastName());
		        pstmt.setString(10, addressList.getEmailAddress());
		        pstmt.setString(11, ""+addressList.getAddressBookId());
		       
	            count = pstmt.executeUpdate();
	           System.out.println("Shipp Address update @ updateBCAddressBook : "+count);
	        
	    } catch (SQLException e) { 
	        e.printStackTrace();
	    }
	    finally {	    
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	      }
			
			return count;
		}
		public static List<WarehouseModel> getWareHouses() {
			return wareHouses;
		}
		public static void setWareHouses(List<WarehouseModel> wareHouses) {
			UsersDAO.wareHouses = wareHouses;
		}
		
		public static int updateDefaultShiptoId(int userId, int shiptoId)
		  {
		   Connection  conn = null;
		   PreparedStatement pstmt=null;
		   ResultSet rs = null;
		   String sql = "";
		   int count = 0;

		   try {
		    conn = ConnectionManager.getDBConnection();
		    sql = PropertyAction.SqlContainer.get("updateDefaultShipToId");
		    pstmt = conn.prepareStatement(sql);
		    pstmt.setInt(1,shiptoId);
		    pstmt.setInt(2,userId);
		    count = pstmt.executeUpdate();

		   } catch (SQLException e) { 
		    e.printStackTrace();
		   }
		   finally {     
		    ConnectionManager.closeDBResultSet(rs);
		    ConnectionManager.closeDBPreparedStatement(pstmt);
		    ConnectionManager.closeDBConnection(conn); 
		   }
		   return count;

		  }
		public static int updateBuyingComapnyAddressBook(Connection conn, int billAddressId,int shipAddressId,int buyingCompanyId,boolean connCommit)
		{
			int count = 0;
			PreparedStatement pstmt = null;
				
	        try
	        {
	        	String sql = PropertyAction.SqlContainer.get("updateBuyingAddressId");
				//DEFAULT_BILLING_ADDRESS_ID=? AND DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE USER_ID=?
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, billAddressId);
				pstmt.setInt(2, shipAddressId);
				pstmt.setInt(3, buyingCompanyId);
				count = pstmt.executeUpdate();
				if(count>0){
					if(connCommit)
					conn.commit();
				}
	        }
	        catch(SQLException e)
	        {
	        	count =0;
	        	e.printStackTrace();
	        }
	        catch(Exception e)
	        {
	        	count =0;
	        	e.printStackTrace();
	        }
	        finally{
	        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	        }
			
			return count;
		}
		
		public static void updateUserPermission(HashMap<String, String> userDetails) {
			String isAdmin = "N";
			String isRetailUser = "N";
			String isAuthPurAgent="N";
			String isSalesRep = "N";	
			String isSalesAdmin = "N";	
			String isGeneralUser = "N";		
			String role = userDetails.get("userRole");
			
			if(role!=null && role.equalsIgnoreCase("Ecomm Customer Super User")){
				isAdmin = "Y";
			}else if(role!=null && role.equalsIgnoreCase("Ecomm Retail User")){
				isRetailUser = "Y";
			}else if(role!=null && role.equalsIgnoreCase("Ecomm Customer Auth Purchase Agent")){
				isAuthPurAgent = "Y";
			}else if(role!=null && role.equalsIgnoreCase("Ecomm SALES_REP")){
				isSalesRep = "Y";
			}else if(role!=null && role.equalsIgnoreCase("Ecomm SALES_ADMIN")){
				isSalesAdmin = "Y";
			}else{
				isGeneralUser = "Y";
			}
			
			userDetails.put("isAdmin",isAdmin);
			userDetails.put("isRetailUser",isRetailUser);
			userDetails.put("isAuthPurAgent",isAuthPurAgent);
			userDetails.put("isSalesRep", isSalesRep);
			userDetails.put("isSalesAdmin", isSalesAdmin);
			userDetails.put("isGeneralUser", isGeneralUser);
		}
		
		public static void setSalesRepDetails(HttpSession session) {
			int customerId = Integer.parseInt(session.getAttribute("buyingCompanyId").toString());
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet results = null;
			JsonObject salesRepDetials = null;
			try {
				if(session.getAttribute("wareHouseCode") != null) {
					session.setAttribute("defaultWareHouseDetails", CommonUtility.getWareHouseByCode(session.getAttribute("wareHouseCode").toString()));
				}
				
				String query = PropertyAction.SqlContainer.get("getSalesRepUserDetailsForCustomer");
				connection = ConnectionManager.getDBConnection();
				statement = connection.prepareStatement(query);
				statement.setInt(1, customerId);
				results = statement.executeQuery();
				if(results.next()) {
					salesRepDetials = new JsonObject();
					salesRepDetials.addProperty("userId", results.getInt("USER_ID"));
					salesRepDetials.addProperty("firstName", results.getString("FIRST_NAME"));
					salesRepDetials.addProperty("lastName", results.getString("LAST_NAME"));
					salesRepDetials.addProperty("emailId", results.getString("EMAIL"));
					salesRepDetials.addProperty("userName", results.getString("USER_NAME"));
					salesRepDetials.addProperty("city", results.getString("CITY"));
					salesRepDetials.addProperty("state", results.getString("STATE"));
					salesRepDetials.addProperty("country", results.getString("COUNTRY"));
					salesRepDetials.addProperty("phoneNumber", results.getString("OFFICE_PHONE"));	
				}
				if(salesRepDetials != null) {
						Map<String, String> userCustomFieldValue = UsersDAO.getAllUserCustomFieldValue(results.getInt("USER_ID"));
						if(CommonUtility.validateString(userCustomFieldValue.get("DEFAULT_BRANCH_ID")).length() > 0) {
							salesRepDetials.addProperty("defaultWareHouseId", userCustomFieldValue.get("DEFAULT_BRANCH_ID"));
							session.setAttribute("saleRepDesignateWarehouse", CommonUtility.getWareHouseByCode(userCustomFieldValue.get("DEFAULT_BRANCH_ID")));
						}else {
							session.setAttribute("saleRepDesignateWarehouse", CommonUtility.getWareHouseByCode(userCustomFieldValue.get("DEFAULT_WAREHOUSE_CODE_STR")));
						}
					session.setAttribute("salesRepDetails", salesRepDetials);
					session.setAttribute("salesRepEmailId", salesRepDetials.get("emailId").getAsString());
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				ConnectionManager.closeDBPreparedStatement(statement);
				ConnectionManager.closeDBResultSet(results);
				ConnectionManager.closeDBConnection(connection);
			}
		}
		
		public static List<JsonObject> customers(HttpSession session){
			Map<String, String> salesUserDetails = extractSalesUserDetails(session);
			int userId = Integer.parseInt(salesUserDetails.get("userId"));
			return getCustomers(userId, salesUserDetails);
		}
		
		public static List<JsonObject> users(HttpServletRequest request){
			int customerId = Integer.parseInt(request.getParameter("customerId"));
			String accountNumber = request.getParameter("accountNumber");
			HttpSession session = request.getSession();
			Map<String, String> salesUserDetails = extractSalesUserDetails(session);
			return getUsersByCustomer(customerId, accountNumber, salesUserDetails);
		}
		
		
		
		private static List<JsonObject> getCustomers(int userId, Map<String, String> saleUserDetails){
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet results = null;
			boolean isSalesAdmin = false;
			int excludedCustomerId = Integer.parseInt(saleUserDetails.get("buyingCompanyId"));
			if(saleUserDetails.get("isSalesAdmin") != null && CommonUtility.validateString(saleUserDetails.get("isSalesAdmin")).equals("Y")) {
				isSalesAdmin = true;
			}
			
			String query = "";
			if(isSalesAdmin && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EXCLUDE_SALES_REP_BUYING_COMPANY")).equals("Y")) {
				query = PropertyAction.SqlContainer.get("getCustomersForSalesAdminExcludeSrcCompany");
			}else if(isSalesAdmin){
				query = PropertyAction.SqlContainer.get("getCustomersForSalesAdmin");
			} else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EXCLUDE_SALES_REP_BUYING_COMPANY")).equals("Y")) {
				query = PropertyAction.SqlContainer.get("getCustomersForSalesRepExcludeSrcCompany");
			}else {
				query = PropertyAction.SqlContainer.get("getCustomersForSalesRep");
			}
			
			List<JsonObject> customers = new ArrayList<>();
			try {
				connection = ConnectionManager.getDBConnection();
				statement = connection.prepareStatement(query);
				
				if(isSalesAdmin && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EXCLUDE_SALES_REP_BUYING_COMPANY")).equals("Y")) {
					statement.setInt(1, excludedCustomerId);
				}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EXCLUDE_SALES_REP_BUYING_COMPANY")).equals("Y")) {
					statement.setInt(1, userId);
					statement.setInt(2, excludedCustomerId);
				}else if(!isSalesAdmin){
					statement.setInt(1, userId);
				}
				
				results = statement.executeQuery();
				while(results.next()) {
					JsonObject customer = new JsonObject();
					customer.addProperty("customerId", results.getInt("BUYING_COMPANY_ID"));
					customer.addProperty("customerName", CommonUtility.validateString(results.getString("CUSTOMER_NAME")));
					customer.addProperty("accountNumber", CommonUtility.validateString(results.getString("ENTITY_ID")));
					customer.addProperty("address1", CommonUtility.validateString(results.getString("ADDRESS1")));
					customer.addProperty("state", CommonUtility.validateString(results.getString("STATE")));
					customer.addProperty("city", CommonUtility.validateString(results.getString("CITY")));
					customer.addProperty("zipCode", CommonUtility.validateString(results.getString("ZIP")));
					customer.addProperty("country", CommonUtility.validateString(results.getString("COUNTRY")));
					customers.add(customer);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally{
	        	ConnectionManager.closeDBPreparedStatement(statement);	
	        	ConnectionManager.closeDBResultSet(results);
	        	ConnectionManager.closeDBConnection(connection);
	        }
			return customers;
		}
		
		private static List<JsonObject> getUsersByCustomer(int customerId, String accountNumber, Map<String, String> salesUserDetails){
			List<JsonObject> users = new ArrayList<>();
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet results = null;
			try {
				
				String query = PropertyAction.SqlContainer.get("getUsersByCustomerForSalesRep");
				if(salesUserDetails!=null && CommonUtility.validateString(salesUserDetails.get("userRole")).equalsIgnoreCase("Ecomm SALES_ADMIN") ) {
					query = PropertyAction.SqlContainer.get("getUsersByCustomerSaleUsers");
				}
				connection = ConnectionManager.getDBConnection();
				statement = connection.prepareStatement(query);
				statement.setInt(1, customerId);
				results = statement.executeQuery();
				while(results.next()) {
					JsonObject user = new JsonObject();
					user.addProperty("userId", results.getInt("USER_ID"));
					user.addProperty("customerId", results.getInt("BUYING_COMPANY_ID"));
					user.addProperty("userName", CommonUtility.validateString(results.getString("USER_NAME")));
					user.addProperty("firstName", CommonUtility.validateString(results.getString("FIRST_NAME")));
					user.addProperty("lastName", CommonUtility.validateString(results.getString("LAST_NAME")));
					user.addProperty("address1", CommonUtility.validateString(results.getString("ADDRESS1")));
					user.addProperty("address2", CommonUtility.validateString(results.getString("ADDRESS2")));
					user.addProperty("city", CommonUtility.validateString(results.getString("CITY")));
					user.addProperty("state", CommonUtility.validateString(results.getString("STATE")));
					user.addProperty("country", CommonUtility.validateString(results.getString("COUNTRY")));
					user.addProperty("zipCode", CommonUtility.validateString(results.getString("ZIP")));
					user.addProperty("email", CommonUtility.validateString(results.getString("EMAIL")));
					user.addProperty("accountNumber", CommonUtility.validateString(results.getString("ECLIPSE_CONTACT_ID")));
					users.add(user);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally{
	        	ConnectionManager.closeDBPreparedStatement(statement);	
	        	ConnectionManager.closeDBResultSet(results);
	        	ConnectionManager.closeDBConnection(connection);
	        }
			return users;
		}
		
		@SuppressWarnings("unchecked")
		private static Map<String, String> extractSalesUserDetails(HttpSession session){
			return (Map<String, String>) session.getAttribute("salesUserDetails");
		}
		
		public static boolean isSalesUser(HttpSession session) {
			boolean salesUser = false;
			/*if(session.getAttribute("isSalesRep") != null &&  CommonUtility.validateString((String) session.getAttribute("isSalesRep")).equals("Y")) {
				salesUser = true;
			}else if(session.getAttribute("isSalesAdmin") != null &&  CommonUtility.validateString((String) session.getAttribute("isSalesAdmin")).equals("Y")) {
				salesUser = true;
			}*/
			if(CommonUtility.validateString((String) session.getAttribute("isSalesAdmin")).equalsIgnoreCase("Y") ||  CommonUtility.validateString((String) session.getAttribute("isSalesRep")).equalsIgnoreCase("Y")) {
				salesUser = true;
			}
			return salesUser;
		}
		
		public static void salesUserDetailsToSession(HttpSession session, Map<String, String> userDetails) {
			boolean salesUser = false;
			if(userDetails.get("isSalesRep") != null && CommonUtility.validateString(userDetails.get("isSalesRep")).equals("Y")) {
				salesUser = true;
			}
			else if(userDetails.get("isSalesAdmin") != null && CommonUtility.validateString(userDetails.get("isSalesAdmin")).equals("Y")) {
				salesUser = true;
			}
			if(salesUser) {
				session.setAttribute("isSalesUser", "Y");
				session.setAttribute("isSalesRep", userDetails.get("isSalesRep"));
				session.setAttribute("isSalesAdmin", userDetails.get("isSalesAdmin"));
				session.setAttribute("salesUserDetails", userDetails);
			}
		}
		
		public static String getSalesUserDetails(HttpSession session) {
			JsonObject userDetails = new JsonObject();
			userDetails.addProperty("salesUser", "Y");
			if(session.getAttribute("isSalesAdmin") != null &&  CommonUtility.validateString(session.getAttribute("isSalesAdmin").toString()).equals("Y")) {
				userDetails.addProperty("salesAdmin", "Y");
			}
			return new Gson().toJson(userDetails);
		}
		
		public static boolean checkRockwell(String zipcode){
			boolean result=false;
			Connection conn=null;
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			try{
				conn=ConnectionManager.getDBConnection();	
				pstmt = conn.prepareStatement("select * from rockwell_zip_codes where zipcode=?");
				pstmt.setString(1, zipcode);
				rs = pstmt.executeQuery();
				if(rs.next()){
					result=true;
				}

			} catch (SQLException e) { 
				e.printStackTrace();
			}
			finally {	    
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return result;
		}
		
		public static String getUserPreferencesJsonData(int userId) {
			String jsonData = "";
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet results = null;
			try {
				String query = PropertyAction.SqlContainer.get("getUserPreferencesJsonData");
				connection = ConnectionManager.getDBConnection();
				statement = connection.prepareStatement(query);
				statement.setInt(1, userId);
				results = statement.executeQuery();
				while(results.next()) {
					jsonData = results.getString("USER_PREFERENCES");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally{
	        	ConnectionManager.closeDBPreparedStatement(statement);	
	        	ConnectionManager.closeDBResultSet(results);
	        	ConnectionManager.closeDBConnection(connection);
	        }
			return jsonData;
		}
		
		public static int updateUserPreferencesJsonData(int userId, String jsonData) {
			Connection connection = null;
		    PreparedStatement statement = null;
		    ResultSet results = null;
		    int count = 0;
			try {
				connection = ConnectionManager.getDBConnection();
				String query = PropertyAction.SqlContainer.get("updateUserPreferencesJsonData");
				statement = connection.prepareStatement(query);
				statement.setString(1, jsonData);
				statement.setInt(2, userId);
	            count = statement.executeUpdate();
	           
		    } catch (SQLException e) { 
		        e.printStackTrace();
		    }
		    finally {	    
		    	  ConnectionManager.closeDBResultSet(results);
		    	  ConnectionManager.closeDBPreparedStatement(statement);
		    	  ConnectionManager.closeDBConnection(connection);	
		    }
			return count;
		}
		
		public static List<JsonObject> getAssetsByWareHouse(HttpSession session, String catagoryFilter){

			String warehousecode = (String) session.getAttribute("wareHouseCode");
				
			List<JsonObject> assets = new ArrayList<>();
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet results = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S"); 
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE, MMM dd yyyy hh:mm aaa"); 
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
			Date validFrom=null;
			Date validTo=null;
			boolean login = false;
			try {				
				String query = PropertyAction.SqlContainer.get("getAssetsByWareHouse");
				String catFilter = "";
				if(CommonUtility.validateString(catagoryFilter).length()>0) {
					catFilter = "AND ACL.CATEGORY_NAME IN (?)";
				}				
				query = query + " "+catFilter;
				connection = ConnectionManager.getDBConnection();
				statement = connection.prepareStatement(query);
				if (session.getAttribute("userLogin") != null) {
					login = (Boolean) session.getAttribute("userLogin");
				}
				if(login) {
					statement.setString(1, warehousecode);
				}else {
					statement.setString(1,null);
				}
				if(CommonUtility.validateString(catagoryFilter).length()>0) {
					statement.setString(2,catagoryFilter);
				}
				results = statement.executeQuery();
				while(results.next()) {
					JsonObject asset = new JsonObject();
					//user.addProperty("userId", results.getInt("USER_ID"));
					//user.addProperty("customerId", results.getInt("BUYING_COMPANY_ID"));
					asset.addProperty("assetId", results.getInt("ASSET_ID"));
					asset.addProperty("assetName", CommonUtility.validateString(results.getString("ASSET_NAME")));
					asset.addProperty("assetTitle", CommonUtility.validateString(results.getString("TITLE")));
					asset.addProperty("assetType", CommonUtility.validateString(results.getString("ASSET_TYPE")));
					asset.addProperty("assetImage", CommonUtility.validateString(results.getString("ASSET_THUMBNAIL")));
					asset.addProperty("assetDescription", CommonUtility.validateString(results.getString("DESCRIPTION")));
					
					String validFromDateString = "";					
					String validFromString = "";
					String validToDateString = "";					
					String validToString = "";
					
					String validFromDate = results.getString("VALID_FROM")!=null?results.getString("VALID_FROM"):"";
					if(CommonUtility.validateString(validFromDate)!=null &&  CommonUtility.validateString(validFromDate).length() > 0) {
						validFrom = dateFormat.parse(validFromDate);
						validFromDateString = dateFormat1.format(validFrom);					
						validFromString = dateFormat2.format(validFrom);
					}
					String validToDate = results.getString("VALID_TO")!=null?results.getString("VALID_TO"):"";
					if(CommonUtility.validateString(validToDate)!=null &&  CommonUtility.validateString(validToDate).length() > 0) {
						validTo = dateFormat.parse(validToDate);
						validToDateString = dateFormat1.format(validTo);
						validToString = dateFormat2.format(validTo);
					}
					
					asset.addProperty("validFromDate", CommonUtility.validateString(validFromDateString));
					asset.addProperty("validToDate", CommonUtility.validateString(validToDateString));
					asset.addProperty("validFromDateString", CommonUtility.validateString(validFromString));
					asset.addProperty("validToDateString", CommonUtility.validateString(validToString));
					asset.addProperty("categoryId", CommonUtility.validateString(results.getString("CATEGORY_ID")));
					asset.addProperty("categoryName", CommonUtility.validateString(results.getString("CATEGORY_NAME")));
					asset.addProperty("warahouseCode", CommonUtility.validateString(results.getString("WAREHOUSE_CODE")));
					asset.addProperty("WarahouseName", CommonUtility.validateString(results.getString("WAREHOUSE_NAME")));
					asset.addProperty("warahouseID", CommonUtility.validateString(results.getString("WAREHOUSE_ID")));					
					asset.addProperty("warahouseAddress1", CommonUtility.validateString(results.getString("ADDRESS1")));
					asset.addProperty("warahouseAddress2", CommonUtility.validateString(results.getString("ADDRESS2")));
					asset.addProperty("warahouseLocationID", CommonUtility.validateString(results.getString("BRANCH_LOCATION_ID")));
					asset.addProperty("warahouseCity", CommonUtility.validateString(results.getString("CITY")));
					asset.addProperty("warahouseState", CommonUtility.validateString(results.getString("STATE")));
					asset.addProperty("warahouseCountry", CommonUtility.validateString(results.getString("COUNTRY")));					
					assets.add(asset);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally{
	        	ConnectionManager.closeDBPreparedStatement(statement);	
	        	ConnectionManager.closeDBResultSet(results);
	        	ConnectionManager.closeDBConnection(connection);
	        }
			return assets;
		}
		
		public static  ArrayList<AddressModel> getShipToIdShipAddresses(int buyingCompanyID)
		{
			
			ArrayList<AddressModel> shipAddressList = new ArrayList<AddressModel>();
			Connection  conn = null;
		    PreparedStatement preStat=null;
		    ResultSet rs = null;
		    
		    try {
		    	conn = ConnectionManager.getDBConnection();
		          preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getShipToIdShipAddresses"));	
		         // preStat.setString(1, shipToId);
		          preStat.setInt(1, buyingCompanyID);
				  rs =preStat.executeQuery();

				  while(rs.next()){
					  AddressModel addressListVal = new AddressModel();
					  addressListVal.setAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
					  addressListVal.setAddressType(rs.getString("ADDRESS_TYPE"));
					  addressListVal.setFirstName(rs.getString("FIRST_NAME"));
					  addressListVal.setLastName(rs.getString("LAST_NAME"));
					  addressListVal.setEntityId(rs.getString("ENTITY_ID"));
					  addressListVal.setAddress1(rs.getString("ADDRESS1"));
					  addressListVal.setAddress2(rs.getString("ADDRESS2"));
					  addressListVal.setCity(rs.getString("CITY"));
					  addressListVal.setState(rs.getString("STATE"));
					  addressListVal.setZipCode(rs.getString("ZIPCODE"));
					  addressListVal.setCountry(rs.getString("COUNTRY"));
					  addressListVal.setPhoneNo(rs.getString("PHONE"));
					 // addressListVal.setCompanyName(rs.getString("CUSTOMER_NAME"));
					  addressListVal.setBuyingComanyIdStr(CommonUtility.validateParseIntegerToString(rs.getInt("BUYING_COMPANY_ID")));
					  addressListVal.setEmailAddress(rs.getString("EMAIL")!=null?rs.getString("EMAIL"):"");
					  
					 if(rs.getString("SHIP_TO_ID") != null && rs.getString("SHIP_TO_ID").trim().length()>0){
						  addressListVal.setShipToId(rs.getString("SHIP_TO_ID").trim());
					  }else{
						  addressListVal.setShipToId("");
					  }
					 
					 shipAddressList.add(addressListVal);
				  }
		    	} catch (Exception e) {         
		          e.printStackTrace();

		      } finally {	    	
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(preStat);
		    	  ConnectionManager.closeDBConnection(conn);	
		      } 
		      return shipAddressList;
		}
		public static String updateEclipseUserContact(UsersModel addressList)
		{
			int count=0;
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    String country = "";
		    String sql = "";
			String result = "0|Update Failed";
		    if(addressList.getCountry()!=null && !addressList.getCountry().trim().equals(""))
		    	country = getCountryCode(addressList.getCountry());
					if(country==null){
						country = addressList.getCountry().trim();
					}
				if(country.length()>2)
					country = country.substring(0, 2);
				System.out.println(CommonUtility.validateString(addressList.getFirstName()));
			    System.out.println(CommonUtility.validateString(addressList.getLastName()));
			    System.out.println(CommonUtility.validateString(addressList.getAddress1()));
			    System.out.println(CommonUtility.validateString(addressList.getAddress2()));
			    System.out.println(CommonUtility.validateString(addressList.getCity()));
			    System.out.println(CommonUtility.validateString(addressList.getState()));
			    System.out.println(CommonUtility.validateString(addressList.getZipCode()));
			    System.out.println(CommonUtility.validateString(country));
			    System.out.println(CommonUtility.validateString(addressList.getPhoneNo()));
			    System.out.println(CommonUtility.validateString(addressList.getEmailAddress()));
			    System.out.println(CommonUtility.validateString(addressList.getFaxNo()));
			    System.out.println(CommonUtility.validateNumber((String)addressList.getSession().getAttribute(Global.USERID_KEY)));
			try {
				conn = ConnectionManager.getDBConnection();
	            sql = PropertyAction.SqlContainer.get("updateEclipseUserContactInformation");
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, CommonUtility.validateString(addressList.getFirstName()));
	            pstmt.setString(2, CommonUtility.validateString(addressList.getLastName()));
	            pstmt.setString(3, CommonUtility.validateString(addressList.getAddress1()));
	            pstmt.setString(4, CommonUtility.validateString(addressList.getAddress2()));
	            pstmt.setString(5, CommonUtility.validateString(addressList.getCity()));
	            pstmt.setString(6, CommonUtility.validateString(addressList.getState()));
	            pstmt.setString(7, CommonUtility.validateString(addressList.getZipCode()));
	            pstmt.setString(8, CommonUtility.validateString(country));
	            pstmt.setString(9, CommonUtility.validateString(addressList.getPhoneNo()));
	            pstmt.setString(10, CommonUtility.validateString(addressList.getEmailAddress()));
	            pstmt.setString(11, CommonUtility.validateString(addressList.getFaxNo()));
	            pstmt.setInt(12, CommonUtility.validateNumber((String)addressList.getSession().getAttribute(Global.USERID_KEY)));
	            count = pstmt.executeUpdate();
		        if(count>0){
		            result = "1|"+LayoutLoader.getMessageProperties().get(addressList.getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("contact.information.updated.success");
		          }else{
		            result = "0|"+LayoutLoader.getMessageProperties().get(addressList.getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("contact.information.updated.failed");
		          }
	            System.out.println("Updated to Cimm User eclipse specific DAO : "+count);
			   } catch (SQLException e) { 
			        e.printStackTrace();
			   	}finally {	    
			    	  ConnectionManager.closeDBPreparedStatement(pstmt);
			    	  ConnectionManager.closeDBConnection(conn);	
			      }
			return result;
		}

		public static int updateBuyingCompany(String buyingComapnyId, UsersModel userDetailList) {
            int count = 0;
            Connection  conn = null;
         PreparedStatement pstmt=null;
         String sql = "";
         try {
                   conn = ConnectionManager.getDBConnection();
                   sql = PropertyAction.SqlContainer.get("updateBuyingCompanyWithBuyingCompanyId");
                   pstmt = conn.prepareStatement(sql);
                   pstmt.setString(1, userDetailList.getEntityName());
                   pstmt.setString(2, userDetailList.getAddress1());
                   pstmt.setString(3, userDetailList.getAddress2());
                   pstmt.setString(4, userDetailList.getCity());
                   pstmt.setString(5, userDetailList.getState());
                   pstmt.setString(6, userDetailList.getZipCode());
                   pstmt.setString(7, userDetailList.getCountry());
                   pstmt.setString(8, userDetailList.getEmailAddress());
                   pstmt.setString(9, userDetailList.getCustomerType());
                   pstmt.setString(10,buyingComapnyId);
                   count = pstmt.executeUpdate();
         } catch (SQLException e) { 
             e.printStackTrace();
            }finally {       
              ConnectionManager.closeDBPreparedStatement(pstmt);
              ConnectionManager.closeDBConnection(conn);   
           }
            return count;
     }
		
		public static String getEntityIdWithBuyingCompanyId(String buyingComapnyId) {
			 Connection  conn = null;
			 PreparedStatement pstmt=null;
	         String sql = "";
	         ResultSet rs = null;
	         String systemCustomerId = "";
	         try {
	                   conn = ConnectionManager.getDBConnection();
	                   sql = PropertyAction.SqlContainer.get("getEntityIdWithBuyingCompanyId");
	                   pstmt = conn.prepareStatement(sql);
	                   pstmt.setString(1, buyingComapnyId);
	                   rs =  pstmt.executeQuery();
	                   while(rs.next()){
	                	  systemCustomerId = rs.getString("ENTITY_ID_NUMBER") ;
	                   }
	         } catch (SQLException e) { 
	             e.printStackTrace();
	            }finally {       
	              ConnectionManager.closeDBPreparedStatement(pstmt);
	              ConnectionManager.closeDBConnection(conn);   
	           }
			return systemCustomerId;
		}
		public static ArrayList<UsersModel> getAllApprovalUserList(int userId)
		{
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = PropertyAction.SqlContainer.get("getAllApprovalUserList");
			ArrayList<UsersModel> approverList = new ArrayList<UsersModel>();
			try
			{
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					UsersModel userModelObject = new UsersModel();
					userModelObject.setApproverId(rs.getInt("APPROVER_ID"));
					userModelObject.setApproveLimit(rs.getString("APPROVE_LIMIT"));
					userModelObject.setApproverSequence(rs.getString("APPROVER_SEQUENCE"));
					approverList.add(userModelObject);
					
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally
			{
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
				ConnectionManager.closeDBResultSet(rs);
				
			}
		return approverList;
			
		}
		
		public static UsersModel checkAlwaysApprover(int userId)
		{
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = PropertyAction.SqlContainer.get("checkAlwaysApprover");
			UsersModel userModelObject = new UsersModel();
			try
			{
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					userModelObject.setApproveLimit(rs.getString("APPROVE_LIMIT"));
					userModelObject.setAlwaysApprover(CommonUtility.validateString(rs.getString("ALWAYS_APPROVER")).length() > 0 ? rs.getString("ALWAYS_APPROVER"):"N");
					userModelObject.setUserId(rs.getInt("USER_ID"));
					userModelObject.setUserName(rs.getString("USER_NAME"));
					
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally
			{
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
				ConnectionManager.closeDBResultSet(rs);
				
			}
		return userModelObject;
			
		}
		
		
		public static ArrayList<UsersModel> getApproverGroupList(int requestTokenId,int requesterUserId)
		{
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = PropertyAction.SqlContainer.get("getApproveGroupList");
			ArrayList<UsersModel> approveGroupList = new ArrayList<UsersModel>();
			try
			{
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, requestTokenId);
				pstmt.setInt(2, requesterUserId);
				
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					UsersModel userModelObject  = new UsersModel();

					userModelObject.setFirstName(rs.getString("FIRST_NAME"));
					userModelObject.setLastName(rs.getString("LAST_NAME"));
					userModelObject.setApprovalStatus(rs.getString("APPROVAL_STATUS"));
					userModelObject.setApproverComments(rs.getString("APPROVER_COMMENT"));
					userModelObject.setApproverSequence(rs.getString("APPROVER_SEQ"));
					userModelObject.setAlwaysApprover(CommonUtility.validateString(rs.getString("ALWAYS_APPROVE")).length() > 0 ? rs.getString("ALWAYS_APPROVE"):"N");
					userModelObject.setUserId(rs.getInt("USER_ID"));
					userModelObject.setUserName(rs.getString("USER_NAME"));
					userModelObject.setApproveCartGroupId(rs.getInt("SAVED_LIST_ID"));
					userModelObject.setApprovecartGroupName(rs.getString("SAVED_LIST_NAME"));
					userModelObject.setEmailAddress(rs.getString("EMAIL"));
					 
					approveGroupList.add(userModelObject);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally
			{
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
				ConnectionManager.closeDBResultSet(rs);
				
			}
		return approveGroupList;
			
		}
		
		
		public static ArrayList<UsersModel> getUnlimitedAccessSuperAndAPAlist(int userId,int buyingCompanyId)
		{
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = PropertyAction.SqlContainer.get("getUnlimitedAccessSuperAndAPAlist");
			ArrayList<UsersModel> approverList = new ArrayList<UsersModel>();
			try
			{
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, buyingCompanyId);
				pstmt.setInt(2, userId);
				
				
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					UsersModel userModelObject = new UsersModel();
					userModelObject.setApproverId(rs.getInt("APPROVER_ID"));
					userModelObject.setApproveLimit(rs.getString("APPROVE_LIMIT"));
					userModelObject.setEmailAddress(rs.getString("EMAIL"));
					approverList.add(userModelObject);
					
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally
			{
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
				ConnectionManager.closeDBResultSet(rs);
				
			}
		return approverList;
			
		}
		
		
		
		
		
}