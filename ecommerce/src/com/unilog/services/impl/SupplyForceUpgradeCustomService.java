package com.unilog.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.utility.CommonUtility;

public class SupplyForceUpgradeCustomService implements UnilogFactoryInterface {
	private static UnilogFactoryInterface serviceProvider;
	
	private SupplyForceUpgradeCustomService() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (SupplyForceUpgradeCustomService.class) {
				if(serviceProvider == null) {
					serviceProvider = new SupplyForceUpgradeCustomService();
				}
			}
		return serviceProvider;
	}
	
	@Override
	public void customizeOrderResponse(List<ProductsModel> lineItems, List<Cimm2BCentralLineItem> orderedItems) {
		for(Cimm2BCentralLineItem orderedItem : orderedItems) {
			for(ProductsModel lineItem : lineItems) {
				if(orderedItem.getPartNumber().equalsIgnoreCase(lineItem.getPartNumber()) && CommonUtility.validateString(lineItem.getOverRidePriceRule()).equalsIgnoreCase("Y") && lineItem.getPrice() > 0) {
					orderedItem.setUnitPrice(lineItem.getUnitPrice());
					orderedItem.setExtendedPrice(lineItem.getUnitPrice() * lineItem.getQty());
					break;
				}
			}
		}
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
	
	public int getUserBuyingCompanyId(int buyingCompanyId, int userBuyingCompanyId) 
	{ 
		return userBuyingCompanyId;
	} 
	
	
	
	
	
	public void updateQuotesItemsToCart(String qPno,ProductsModel quotesCart,HttpServletRequest request){
		
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		int count=0;
		String orderStatus=request.getParameter("orderStatus");
		if(CommonUtility.validateString(orderStatus).equalsIgnoreCase("QUOTE")){
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
					String sql = "UPDATE CART SET PRICE = ?,PART_NUMBER=?,SHORT_DESCRIPTION=?, GET_PRICE_FROM = ?, SESSIONID=? WHERE USER_ID=? AND CART_ID=?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, stringPrice);
						pstmt.setString(2, partNumber);
						pstmt.setString(3, shortDesc);
						pstmt.setString(4, "CART");
						pstmt.setString(5,session.getId());
						pstmt.setInt(6, userId);
						pstmt.setInt(7, cartId.get(0));
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
}
