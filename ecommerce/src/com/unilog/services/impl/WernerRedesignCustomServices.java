package com.unilog.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.impl.UserManagementImpl;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class WernerRedesignCustomServices implements UnilogFactoryInterface{
	private static UnilogFactoryInterface serviceProvider;
	private WernerRedesignCustomServices() {}
	public static UnilogFactoryInterface getInstance() {
			synchronized (WernerRedesignCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new WernerRedesignCustomServices();
				}
			}
		return serviceProvider;
	}
	public String userIdSetUp() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = session.getAttribute("customerId").toString();
		return userId;
	}
	
	public void  setSubsetViaZipCode(UsersModel uModel, UsersModel usersModel) {
		HttpServletRequest request =ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String entityId = CommonUtility.validateString(usersModel.getEntityId());
	    String homeBranchZipCode = CommonUtility.validateString(usersModel.getZipCodeStringFormat());
	    int buyingCompanyId=UsersDAO.getBuyingCompanyIdByEntityId(entityId);

	    if(CommonDBQuery.zipCodeShipBranches != null && CommonDBQuery.zipCodeShipBranches.size() > 0){
	    	for (CustomTable customTable : CommonDBQuery.zipCodeShipBranches) {
	    		List<Map<String, String>> tableDetails = customTable.getTableDetails();
	    		for (Map<String, String> map : tableDetails) {
					if(homeBranchZipCode.equalsIgnoreCase(map.get("ZIP_CODE"))){
						uModel.setBranchID(map.get("SHIP_BRANCH_ID"));
						uModel.setBranchName(map.get("SHIP_BRANCH_NAME"));
						session.setAttribute("overrideShipBranch", "Y");
						session.setAttribute("ZipcodeShipBranch",uModel.getBranchID()); //new
					}
				}
			}
	    }
		String overrideZipcodeRule=UsersDAO.getCustomerCustomTextFieldValue(buyingCompanyId,"overrideZipcodeRule"); 
    	if(UsersDAO.checkRockwell(usersModel.getZipCodeStringFormat()) && !CommonUtility.validateString(overrideZipcodeRule).equalsIgnoreCase("Y")) //for production deployment
    	{
    		session.setAttribute("userSubsetId", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NEENAH_SUBSET_ID")));
    	}
    	else if((CommonUtility.validateString(uModel.getBranchID()).equalsIgnoreCase(CommonDBQuery.getSystemParamtersList().get("NORTH_DAKOTA_BRANCH")))
    			|| (CommonUtility.validateString(uModel.getBranchID()).equalsIgnoreCase(CommonDBQuery.getSystemParamtersList().get("FARGO_BRANCH"))))
    	{
    		session.setAttribute("userSubsetId", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NORTH_DAKOTA_SUBSET_ID")));
    	}

	}
	
	public List<CustomTable> getWebsiteZipCodeCustomTable()
	{
		System.out.println("Getting Website Zip code Custom Table");		
		List<CustomTable> zipCodeShipBranches = CIMM2VelocityTool.getInstance().getCusomTableData("Website","NEW_BRANCH_ZIP_CODES");
		return zipCodeShipBranches;
	}
	
	@Override	
	public void addNonCimmItemsToCart(List<ProductsModel> items, HttpSession session) {
		if(items != null && items.size() > 0) {
			int UserId = CommonUtility.validateNumber(session.getAttribute(Global.USERID_KEY).toString());
			persistNonCimmItems(UserId, session.getId(), items);
		}
	}
	
	private static int persistNonCimmItems(int userId, String sessionId, List<ProductsModel> items) {
		int count = -1;
		int siteId = 0;
		int defaultCimmItemId = Integer.parseInt(CommonDBQuery.getSystemParamtersList().get("DEFAULT_ITEM_ID"));
		String query = "INSERT INTO CART (CART_ID,ITEM_ID,QTY,SESSIONID,USER_ID,UPDATED_DATETIME,SITE_ID,LINE_ITEM_COMMENT, CATALOG_ID,UOM, PRICE, MIN_ORDER_QTY, ITEM_WEIGHT, UNITS,MANUFACTURER,PART_NUMBER, ORDER_QUOTE_NUMBER, SHORT_DESCRIPTION,GET_PRICE_FROM) VALUES(CART_ID_SEQ.NEXTVAL,?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection  conn = null;
		PreparedStatement pstmt = null;
		
		if(CommonDBQuery.getGlobalSiteId()>0){
    		siteId = CommonDBQuery.getGlobalSiteId();
		}
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(query);
			
			for(ProductsModel item : items) {
				pstmt.setInt(1, defaultCimmItemId);
				pstmt.setInt(2, item.getQty());
				pstmt.setString(3, sessionId);
				pstmt.setInt(4, userId);
				pstmt.setInt(5, siteId);
				pstmt.setString(6, item.getLineItemComment());
				pstmt.setString(7, "NPNS");
				if(CommonUtility.validateString(item.getUom()).length()==0){
					pstmt.setString(8," ");	
				}else{
					pstmt.setString(8, CommonUtility.validateString(item.getUom()));	
				}
				pstmt.setString(9, String.valueOf(item.getPrice()));
				pstmt.setInt(10, item.getMinOrderQty() > 0 ? item.getMinOrderQty() : 1);
				pstmt.setDouble(11, item.getWeight());
				pstmt.setDouble(12, 0);
				pstmt.setString(13, item.getManufacturerPartNumber());
				pstmt.setString(14, item.getPartNumber());
				pstmt.setString(15, item.getOrderOrQuoteNumber());
				pstmt.setString(16, item.getShortDesc());
				pstmt.setString(17, "CART");
				pstmt.addBatch();
			}
			int[] executeBatch = pstmt.executeBatch();
			if(executeBatch.length >= 0) {
				count = 1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}

	 public Cimm2BCentralLineItem  disableUmqtyToErp(Cimm2BCentralLineItem cimm2bCentralLineItem){
		 cimm2bCentralLineItem.setUomQty("");
		 return cimm2bCentralLineItem;
	 }
	 
	 public void setUserToken(HashMap<String, String> userDetails, HttpSession session){
			session.setAttribute("userToken", userDetails.get("contactId")); 
		}
	 public ArrayList<UsersModel> userSharedCart(String buyingCompanyId, ArrayList<UsersModel> userList) {
			ResultSet rs = null;
		    Connection  conn = null;
		    PreparedStatement pstmt = null;
			try
			{
				conn = ConnectionManager.getDBConnection();
		        String sql = ""; 
		        sql = "SELECT * FROM CIMM_USERS WHERE BUYING_COMPANY_ID=? AND STATUS='Y' AND EMAIL is not NULL";
		        pstmt=conn.prepareStatement(sql);
		        pstmt.setInt(1, CommonUtility.validateNumber(buyingCompanyId));
		        rs=pstmt.executeQuery();
		    	if(rs!=null){
		    		 while(rs.next())
		    		 {
				  
		    			 UsersModel userListVal = new UsersModel();
		    			 userListVal.setUserId(rs.getInt("USER_ID"));
		    			 userListVal.setFirstName(rs.getString("FIRST_NAME"));
		    			 userListVal.setLastName(rs.getString("LAST_NAME"));
		    			 userListVal.setEmailAddress(rs.getString("EMAIL"));
		    			 userList.add(userListVal);
		    		 }
		    	 }
				
			}catch (Exception e) {
		     	e.printStackTrace();
			}
			finally
			{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
				
			}
			return userList;
		}
		
		public String sendSharedCart(String savedGroupName)
		{
			
			ResultSet rs = null;
		    Connection  conn = null;
		    PreparedStatement pstmt = null;
			String savedGroupId = null;
			try
			{
				conn = ConnectionManager.getDBConnection();
		        String sql = ""; 
		        sql = "SELECT * FROM SAVED_ITEM_LIST where SAVED_LIST_NAME=? AND TYPE='C'";
		        pstmt=conn.prepareStatement(sql);
		        pstmt.setString(1, savedGroupName);
		        rs=pstmt.executeQuery();
		    	if(rs!=null){
		    		 while(rs.next())
		    		 { 
		    			 savedGroupId=rs.getString("SAVED_LIST_ID");
		    			 
		    		 }
		    	 }
				
			}catch (Exception e) {
		     	e.printStackTrace();
			}
			finally
			{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
				
			}
			
			return savedGroupId;
		}
	public void updateQuotesItemsToCart(String qPno,ProductsModel quotesCart,HttpServletRequest request){
			
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			int count=0;
			if(CommonUtility.validateString((String)session.getAttribute("isPunchoutUser")).equalsIgnoreCase("Y")){
				String qty = request.getParameter("qty_"+qPno);
				String partNumber = request.getParameter("partNumber_"+qPno);
				String mpn = request.getParameter("mpn_"+qPno);
				String shortDesc = request.getParameter("shortDesc_"+qPno);
				String stringPrice = request.getParameter("price_"+qPno);
				String uom = request.getParameter("uom_"+qPno);
				
				ArrayList<Integer> cartId = new ArrayList<Integer>();
				cartId = ProductsDAO.selectFromCart(userId, quotesCart.getItemId(),uom);
				
				if(cartId!=null && cartId.size()>0){
					
					Connection  conn = null;
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try{
						
						conn = ConnectionManager.getDBConnection();
						String sql = "UPDATE CART SET PRICE = ?,PART_NUMBER=?,SHORT_DESCRIPTION=?, GET_PRICE_FROM = ? WHERE USER_ID=? AND CART_ID=?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, stringPrice);
							pstmt.setString(2, partNumber);
							pstmt.setString(3, shortDesc);
							pstmt.setString(4, "CART");
							pstmt.setInt(5, userId);
							pstmt.setInt(6, cartId.get(0));
							count = pstmt.executeUpdate();
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
			        	ConnectionManager.closeDBPreparedStatement(pstmt);
			        	ConnectionManager.closeDBConnection(conn);
			        }
				}
			}
		}

			public String setContactEmailToOrder(int userId,String userERPId) {
				UserManagement userObj = new UserManagementImpl();
				String contactEmail="";
				if (userId > 1)
				{
				UsersModel userDetails=userObj.contactInquiry(userERPId);
				if(userDetails!=null)
				{
				contactEmail=userDetails.getEmailAddress();
				}
				
				}
				return contactEmail;
			} 

}
