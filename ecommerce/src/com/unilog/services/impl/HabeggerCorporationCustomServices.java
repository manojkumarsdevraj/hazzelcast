package com.unilog.services.impl;

import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthorizedSaveCreditCardRequest;
import com.erp.service.impl.UserManagementImpl;
import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.products.ProductsDAO;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.singlesignon.OAuth2Details;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jettison.json.JSONObject;
public class HabeggerCorporationCustomServices implements UnilogFactoryInterface {
	private static UnilogFactoryInterface serviceProvider;
	private HabeggerCorporationCustomServices() {}
	private HttpServletRequest request;
	public static UnilogFactoryInterface getInstance() {
			synchronized (HabeggerCorporationCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new HabeggerCorporationCustomServices();
				}
			}
		return serviceProvider;
	}  
	@Override
	    public void  connectToSso(HttpServletResponse response,OAuth2Details oauthDetails) {
	    	StringBuilder redirectUri = new StringBuilder();
			response.setContentType("text/html");
			try {
			PrintWriter output = response.getWriter();
			//output.write(location);
			String state=oauthDetails.getState();
			 UUID uuid = UUID.randomUUID();
		        String randomGUIDString = uuid.toString();
		        randomGUIDString = randomGUIDString.replaceAll("-", "");
		        System.out.println("GUID=" + randomGUIDString );
				state+=randomGUIDString;
			redirectUri.append(oauthDetails.getAuthenticationServerUrl()).append("?oauth=").append(oauthDetails.getOauth()).append("&client_id=").append(oauthDetails.getClientId()).append("&scope=").append(oauthDetails.getScope()).append("&state=").append(state).append("&redirect_uri=").append(oauthDetails.getRedirectURI());
			System.out.println("SSO UrL : " + redirectUri.toString());
			response.sendRedirect(redirectUri.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} 
	    }
	@Override 
	    public HashMap<String,String> registerSsoUserInDb(Map<String, String> ssoUserDetail){
		    LinkedHashMap<String, String> userDetailValues = new LinkedHashMap<String, String>();
			 UserManagement usersObj = new UserManagementImpl();
			 UsersModel userDetailsInput = new UsersModel();
			 request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
		    try {
		    	JSONObject ssoDetails = new JSONObject(ssoUserDetail.get("CustomInfo"));
				if(ssoDetails.getString("statuscode").equalsIgnoreCase("200") && ssoDetails.getString("erpaccount").length()>0) {
					userDetailsInput.setCustomerId(CommonUtility.validateString(ssoDetails.getString("erpaccount"))!=null?CommonUtility.validateString(ssoDetails.getString("erpaccount")):"");
					userDetailsInput.setEntityId(CommonUtility.validateString(ssoDetails.getString("erpaccount"))!=null?CommonUtility.validateString(ssoDetails.getString("erpaccount")):"");
					userDetailsInput.setAccountName(CommonUtility.validateString(ssoDetails.getString("erpaccount"))!=null?CommonUtility.validateString(ssoDetails.getString("erpaccount")):"");
				}
		    	userDetailsInput.setFirstName(CommonUtility.validateString(ssoUserDetail.get("first_name")));
		    	userDetailsInput.setLastName(CommonUtility.validateString(ssoUserDetail.get("last_name")));
		    	userDetailsInput.setPassword(CommonUtility.validateString(ssoUserDetail.get("guid")));
		    	userDetailsInput.setUserStatus("Y");
		    	userDetailsInput.setIsPunchoutUser("N");
		    	userDetailsInput.setUserName(CommonUtility.validateString(ssoUserDetail.get("username")));
		    	userDetailsInput.setEmailAddress(CommonUtility.validateString(ssoUserDetail.get("email")));
		    	userDetailsInput.setEmailId(CommonUtility.validateString(ssoUserDetail.get("email")));
		    	userDetailsInput.setCountry("");
		    	userDetailsInput.setPhoneNo("");
		    	userDetailsInput.setErpLoginId(CommonUtility.validateString(ssoUserDetail.get("guid")));
		    	if(CommonUtility.validateString(ssoUserDetail.get("subset")).length()>0) {
		    		userDetailsInput.setSubsetId(ProductsDAO.getSubsetIdFromName(CommonUtility.validateString(ssoUserDetail.get("subset"))));
		    		userDetailsInput.setSubsetFlag(CommonUtility.validateString(ssoUserDetail.get("subset")));
		    	}
		    	userDetailsInput.setEnablePO("Y");
		    	session.setAttribute("erpType",CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")));
		    	userDetailsInput.setSession(session);
		    	String result = usersObj.newOnAccountUserRegistration(userDetailsInput);
			    if(result!=null && result.toLowerCase().contains("successfully")){
				    userDetailValues.put("userName", ssoUserDetail.get("username"));
				    userDetailValues.put("password",CommonUtility.validateString(ssoUserDetail.get("guid")));
				    userDetailValues.put("checkWithCC", "N");
				    userDetailValues.put("entityId", userDetailsInput.getEntityId());
				    updateUserDetails(userDetailValues);
			    }
		    }
		    catch (Exception e) {         
		    	e.printStackTrace();
		    }
			return userDetailValues;
		}
	
	public LinkedHashMap<String,String> redefinedWarehouseList() {
		
		LinkedHashMap<String,String> activeWarehouseCodes = new LinkedHashMap<String,String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		Connection conn = null;
		
		try{
			conn = ConnectionManager.getDBConnection();
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sql = "SELECT W.WAREHOUSE_NAME,W.WAREHOUSE_CODE FROM WAREHOUSE_CLUSTER_MAPPING WCM INNER JOIN WAREHOUSE_CLUSTER_MAPPING WCM1 ON WCM.WAREHOUSE_CLUSTER_ID = WCM1.WAREHOUSE_CLUSTER_ID INNER JOIN WAREHOUSE W ON W.WAREHOUSE_ID = WCM1.WAREHOUSE_ID WHERE WCM.WAREHOUSE_ID=?";
			if (CommonUtility.validateString((String) session.getAttribute("wareHouseCode")).length() > 0) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, UsersDAO.getCustomerWareHouseID(
						CommonUtility.validateString((String) session.getAttribute("wareHouseCode"))));
				rs = pstmt.executeQuery();
				while (rs.next()) {
					activeWarehouseCodes.put(rs.getString("WAREHOUSE_CODE"), "");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}		
		return activeWarehouseCodes;
	}
	public ArrayList<WarehouseModel> getGroupedWareHouses() {
		ArrayList<WarehouseModel> wareHouseList = new ArrayList<WarehouseModel>();
		WarehouseModel warehouses = new WarehouseModel();
		try {
			LinkedHashMap<String, String> activeWarehouseCodes = redefinedWarehouseList();
			if (activeWarehouseCodes != null && activeWarehouseCodes.size() > 0) {
				for (Map.Entry<String, String> entry : activeWarehouseCodes.entrySet()) {
					if ((entry.getKey() != null)) {
						warehouses = UsersDAO.getWareHouseDetailsByCode(entry.getKey());
					}
					wareHouseList.add(warehouses);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wareHouseList;
	}
	public String modifySearchString(String queryString,String origKeyWord) {
		String reformedQuery = "";
		try {
        	// Check if single word query then do that wildCard query on all the fields
			if(queryString.contains("partnumbersearch:*")) {
				reformedQuery = "partnumbersearch:*" + origKeyWord + "* OR _query_:\"{!edismax}*" + origKeyWord + "*\""+" OR "+origKeyWord;
			}
			else if(queryString.split(" ").length > 1){
				reformedQuery = queryString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reformedQuery;
	}
	
	public Cimm2BCentralAuthorizedSaveCreditCardRequest prepareCreateProfileRequest(SalesModel salesInputParameter) {
		Cimm2BCentralAuthorizedSaveCreditCardRequest cimm2BCentralAuthorizeSaveCreditCardRequest = new Cimm2BCentralAuthorizedSaveCreditCardRequest();
		try {
			cimm2BCentralAuthorizeSaveCreditCardRequest.setCustomerERPId(salesInputParameter.getCustomerERPId());
			cimm2BCentralAuthorizeSaveCreditCardRequest.setCardHolderFirstName(salesInputParameter.getName());}
		catch (Exception e) {
			e.printStackTrace();
		}
		return cimm2BCentralAuthorizeSaveCreditCardRequest;
	}
	public int CustomizatedSubsetId( UsersModel userDetailList, int subsetId) {
		if(userDetailList.getSubsetId()>0) {
			 subsetId = userDetailList.getSubsetId();
		}
		return subsetId;
	}
	
public void updateUserDetails(Map<String, String> userDetails) {
		
		PreparedStatement pstmt = null;	
		Connection conn = null;
		try{
			String status = CommonUtility.validateString(userDetails.get("checkWithCC")).length()>0?CommonUtility.validateString(userDetails.get("checkWithCC")):"N";
			int entityId = CommonUtility.validateNumber(userDetails.get("entityId"));
			conn = ConnectionManager.getDBConnection();
			HttpServletRequest request = ServletActionContext.getRequest();
			String sql = "UPDATE BUYING_COMPANY SET CREDIT_CARD_CHECKOUT = ? WHERE ENTITY_ID = ? AND STATUS = 'A'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, status);
			pstmt.setInt(2, entityId);
			pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}		
	}
}
