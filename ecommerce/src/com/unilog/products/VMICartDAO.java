package com.unilog.products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.utility.CommonUtility;


public class VMICartDAO {
	static final Logger logger = LoggerFactory.getLogger(VMICartDAO.class);
	public static LinkedHashMap<String, Object> getVMICartDao(HttpSession session, LinkedHashMap<String, Object> contentObject){
		int customerNumber = CommonUtility.validateNumber((String) session.getAttribute("customerId"));
		int subsetId = CommonUtility.validateNumber(CommonUtility.validateString((String) session.getAttribute("userSubsetId")));
		int generalCatalog = CommonUtility.validateNumber(CommonUtility.validateString((String) session.getAttribute("generalCatalog")));
		ArrayList<ProductsModel> productListData = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		try {
			ArrayList<String> partIdentifier = new ArrayList<String>();
			ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();    		
			ArrayList<Integer> itemList = new ArrayList<Integer>();
			productListData = new ArrayList<ProductsModel>();
			conn = ConnectionManager.getDBConnection();
			String sql = "";
			int siteId = 0;
			DecimalFormat df = CommonUtility.getPricePrecision(session);
			if(CommonDBQuery.getGlobalSiteId() > 0 ) {
				siteId = CommonDBQuery.getGlobalSiteId();
			}
			sql = PropertyAction.SqlContainer.get("getVMIItems");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, siteId);
			pstmt.setInt(2, customerNumber);
			pstmt.setInt(3, subsetId);
			pstmt.setInt(4, siteId);
			pstmt.setInt(5, customerNumber);
			pstmt.setInt(6, generalCatalog);
			pstmt.setInt(7, siteId);
			pstmt.setInt(8, customerNumber);
			pstmt.setInt(9, subsetId);
			rs = pstmt.executeQuery();			
			while(rs.next()) {
				ProductsModel cartListVal = new ProductsModel();
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
				int packageQty = 1;
				itemUrl = itemUrl.replace(" ", "-");
				cartListVal.setItemUrl(itemUrl);
				cartListVal.setProductListId(rs.getInt("CART_ID"));
				cartListVal.setItemId(rs.getInt("ITEM_ID"));
				cartListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				cartListVal.setImageType(rs.getString("IMAGE_TYPE"));
				cartListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				cartListVal.setUpc(rs.getString("UPC"));
				cartListVal.setPackDesc(rs.getString("PACK_DESC"));
				cartListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				cartListVal.setClearance(rs.getString("CLEARANCE"));
				cartListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				cartListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
				cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				cartListVal.setUom(CommonUtility.validateString(rs.getString("SALES_UOM")));
				cartListVal.setSalesUom(rs.getString("SALES_UOM"));
				cartListVal.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
				cartListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
				cartListVal.setDisplayPrice(rs.getString("DISPLAY_PRICING"));
				cartListVal.setPageTitle(rs.getString("PAGE_TITLE"));
				cartListVal.setWeight(rs.getDouble("WEIGHT"));
				cartListVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
				cartListVal.setAltPartNumber2(rs.getString("ALT_PART_NUMBER2"));
				if(rs.findColumn("GET_PRICE_FROM")>0 && CommonUtility.validateString(rs.getString("GET_PRICE_FROM")).equalsIgnoreCase("CART")){
					cartListVal.setManufacturerName(rs.getString("MANUFACTURER"));
					cartListVal.setPartNumber(rs.getString("NC_PART_NUMBER"));
					cartListVal.setShortDesc(rs.getString("SHORT_DESCRIPTION"));
					cartListVal.setPrice(rs.getDouble("PRICE"));
					cartListVal.setUnitPrice(rs.getDouble("PRICE"));
					if(rs.getString("QTY") != null) {
						cartListVal.setQty(rs.getInt("QTY"));
						cartListVal.setItemQty(rs.getInt("QTY"));
					}
					cartListVal.setOverRidePriceRule("Y");
					cartListVal.setTotal(Double.parseDouble(df.format(rs.getDouble("PRICE") * rs.getInt("QTY"))));
					cartListVal.setBrandName(rs.getString("BRAND"));
				}else{
					cartListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
					cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
					cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
					cartListVal.setPrice(rs.getDouble("NET_PRICE"));
					cartListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
					cartListVal.setQty(rs.getInt("QTY"));
					if(((String) rs.getString("QTY")) != null) {						
						cartListVal.setItemQty(rs.getInt("QTY") );
					}
					cartListVal.setTotal(Double.parseDouble(df.format(rs.getDouble("NET_PRICE") * rs.getInt("QTY"))));
					cartListVal.setBrandName(rs.getString("BRAND_NAME"));
				}
				if(rs.findColumn("CATALOG_ID")>0){
					cartListVal.setCatalogId(rs.getString("CATALOG_ID"));	
				}
				if(CommonUtility.validateString(cartListVal.getCatalogId()).length() <= 0) {
					partIdentifier.add(cartListVal.getPartNumber());
				}
				if(((String) rs.getString("QTY")) != null) {
					partIdentifierQuantity.add(rs.getInt("QTY"));
				}
				itemList.add(rs.getInt("ITEM_ID"));
				if(rs.getInt("PACKAGE_QTY")>0){
					packageQty = rs.getInt("PACKAGE_QTY");
				}
				cartListVal.setPackageFlag(rs.getInt("PACKAGE_FLAG"));
				cartListVal.setPackageQty(packageQty);
				if(rs.findColumn("UOM")>0 && rs.getString("SALES_UOM")==null){
					cartListVal.setUom(CommonUtility.validateString(rs.getString("UOM")));
				}
				cartListVal.setThresholdQty(rs.getInt("THRESHOLD_QTY"));
				cartListVal.setReplenishToQty(rs.getInt("REPLENISH_TO_QTY"));
				cartListVal.setSubsetId(rs.getInt("SUBSET_ID"));
				productListData.add(cartListVal);
			}
			contentObject.put("productListData", productListData);			
		}catch(SQLException sqlEx) {
			logger.error(sqlEx.getMessage());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}finally {
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);		
		}
		return contentObject;		
	}
	
	public static int insertItemsToVMICart(int userId, int customerId, String partNumber,int quantity, int thresholdQty, int replenishToQty, ProductsModel product, HttpSession session) {
		int count = -1;
		int siteId = 0;
		if(CommonDBQuery.getGlobalSiteId()>0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}
		String sessionId = (String) session.getAttribute(Global.USERID_KEY);
		int subsetId = CommonUtility.validateNumber(CommonUtility.validateString((String) session.getAttribute("userSubsetId")));
		int itemId = product.getItemId();
		String brand = product.getBrandName();
		String manfacturer = product.getManufacturerName();
		String sql = ""; 
		sql = PropertyAction.SqlContainer.get("insertItemsToVMICart");
		Connection  conn = null;
		PreparedStatement pstmt = null;		
		try{
			conn = ConnectionManager.getDBConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, itemId);
			pstmt.setInt(2, userId);
			pstmt.setInt(3, siteId);
			pstmt.setString(4, brand);
			pstmt.setString(5, partNumber);
			pstmt.setString(6, manfacturer);
			pstmt.setInt(7, customerId);
			pstmt.setInt(8, thresholdQty);
			pstmt.setInt(9, replenishToQty);
			pstmt.setString(10, sessionId);
			pstmt.setInt(11, subsetId);
			count =  pstmt.executeUpdate();			
		}catch(SQLException sqlEx) {
			logger.error(sqlEx.getMessage());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}finally {
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		return count;
	}
	
	public static int updateItemsInVMICart(int productListId, int userId, int customerId, String partNumber,int quantity, int thresholdQty, int replenishToQty, ProductsModel product, HttpSession session) {
		int count = -1;
		int siteId = 0;
		if(CommonDBQuery.getGlobalSiteId()>0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}
		String sessionId = (String) session.getAttribute(Global.USERID_KEY);
		int subsetId = CommonUtility.validateNumber(CommonUtility.validateString((String) session.getAttribute("userSubsetId")));
		int itemId = product.getItemId();
		String brand = product.getBrandName();
		String manfacturer = product.getManufacturerName();		
		String sql = "";
		sql = PropertyAction.SqlContainer.get("updateItemsInVMICart");
		Connection  conn = null;
		PreparedStatement pstmt = null;		
		try{
			conn = ConnectionManager.getDBConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, itemId);
			pstmt.setInt(2, userId);
			pstmt.setInt(3, siteId);
			pstmt.setString(4, brand);
			pstmt.setString(5, partNumber);
			pstmt.setString(6, manfacturer);
			pstmt.setInt(7, customerId);
			pstmt.setInt(8, thresholdQty);
			pstmt.setInt(9, replenishToQty);
			pstmt.setString(10, sessionId);
			pstmt.setInt(11, subsetId);
			pstmt.setInt(12, productListId);			
			count =  pstmt.executeUpdate();				
		}catch(SQLException sqlEx) {
			logger.error(sqlEx.getMessage());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}finally {
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		return count;
	}
	
	public static int updateVMICart(int userId, int customerId, HttpSession session, ProductsModel vmiCartModel) {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt =null;
		try {
			String sessionId = (String) session.getAttribute(Global.USERID_KEY);
			conn = ConnectionManager.getDBConnection();
			String sql = "";
			sql = PropertyAction.SqlContainer.get("updateVMICart");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setString(2, sessionId);
			pstmt.setInt(3, vmiCartModel.getQty());
			pstmt.setInt(4, vmiCartModel.getSaleQty());
			pstmt.setString(5, vmiCartModel.getAddToCartFalg());
			pstmt.setInt(6, customerId);
			pstmt.setInt(7, vmiCartModel.getProductListId());
			count = pstmt.executeUpdate(); 
		}catch(SQLException sqlEx) {
			logger.error(sqlEx.getMessage());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		return count;
	}
	
	public static int deleteFromVMICart(int customerId, int userId,int cartId, HttpSession session){
		int count = -1;
		Connection conn = null;
		String sql = ""; 
		sql = PropertyAction.SqlContainer.get("deleteFromVMICart");
		PreparedStatement pstmt = null;
		try	{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			pstmt.setInt(2, cartId);
			count = pstmt.executeUpdate();
		}catch(SQLException sqlEx) {
			logger.error(sqlEx.getMessage());
		}catch(Exception e)	{
			logger.error(e.getMessage());
		}finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
		return count;
	}
	
	public static int resetQtyInVMICart(HttpSession session, int customerId, int userId, ProductsModel vmiCartModel){
		int count = -1;
		Connection conn = null;
		int siteId = 0;
		if(CommonDBQuery.getGlobalSiteId() > 0 ) {
			siteId = CommonDBQuery.getGlobalSiteId();
		}
		String sql = "";
		sql = PropertyAction.SqlContainer.get("resetQtyInVMICart");
		PreparedStatement pstmt = null;
		try	{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			pstmt.setInt(2, siteId);
			pstmt.setInt(3, vmiCartModel.getProductListId());
			count = pstmt.executeUpdate();
		}catch(SQLException sqlEx) {
			logger.error(sqlEx.getMessage());
		}catch(Exception e)	{
			logger.error(e.getMessage());
		}finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
		return count;
	}
	
	public static LinkedHashMap<String, Object> getVMIOrderedItemsList(HttpSession session, LinkedHashMap<String, Object> contentObject){
		int customerNumber = CommonUtility.validateNumber((String) session.getAttribute("customerId"));
		int subsetId = CommonUtility.validateNumber(CommonUtility.validateString((String) session.getAttribute("userSubsetId")));
		int generalCatalog = CommonUtility.validateNumber(CommonUtility.validateString((String) session.getAttribute("generalCatalog")));
		ArrayList<ProductsModel> productListData = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			ArrayList<String> partIdentifier = new ArrayList<String>();
			ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();    		
			ArrayList<Integer> itemList = new ArrayList<Integer>();
			productListData = new ArrayList<ProductsModel>();
			conn = ConnectionManager.getDBConnection();
			String sql = "";
			int siteId = 0;
			String addToCartFlag = "Y";
			DecimalFormat df = CommonUtility.getPricePrecision(session);
			if(CommonDBQuery.getGlobalSiteId() > 0 ) {
				siteId = CommonDBQuery.getGlobalSiteId();
			}			
			sql = PropertyAction.SqlContainer.get("getVMIOrderedItemsList");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, siteId);
			pstmt.setInt(2, customerNumber);
			pstmt.setInt(3, subsetId);
			pstmt.setString(4, addToCartFlag);
			pstmt.setInt(5, siteId);
			pstmt.setInt(6, customerNumber);
			pstmt.setInt(7, generalCatalog);
			pstmt.setString(8, addToCartFlag);
			pstmt.setInt(9, siteId);
			pstmt.setInt(10, customerNumber);
			pstmt.setInt(11, subsetId);
			pstmt.setString(12, addToCartFlag);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getString("ADDTOCART_FLAG") != null && rs.getString("ADDTOCART_FLAG").equalsIgnoreCase("Y")) {
					ProductsModel cartListVal = new ProductsModel();
					String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
					int packageQty = 1;
					itemUrl = itemUrl.replace(" ", "-");
					cartListVal.setItemUrl(itemUrl);
					cartListVal.setProductListId(rs.getInt("CART_ID"));
					cartListVal.setItemId(rs.getInt("ITEM_ID"));
					cartListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
					cartListVal.setImageType(rs.getString("IMAGE_TYPE"));
					cartListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
					cartListVal.setUpc(rs.getString("UPC"));
					cartListVal.setPackDesc(rs.getString("PACK_DESC"));
					cartListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
					cartListVal.setClearance(rs.getString("CLEARANCE"));
					cartListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
					cartListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
					cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
					cartListVal.setUom(CommonUtility.validateString(rs.getString("SALES_UOM")));
					cartListVal.setSalesUom(rs.getString("SALES_UOM"));
					cartListVal.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
					cartListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
					cartListVal.setDisplayPrice(rs.getString("DISPLAY_PRICING"));
					cartListVal.setPageTitle(rs.getString("PAGE_TITLE"));
					cartListVal.setWeight(rs.getDouble("WEIGHT"));
					cartListVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
					cartListVal.setAltPartNumber2(rs.getString("ALT_PART_NUMBER2"));
					if(rs.findColumn("GET_PRICE_FROM")>0 && CommonUtility.validateString(rs.getString("GET_PRICE_FROM")).equalsIgnoreCase("CART")){
						cartListVal.setManufacturerName(rs.getString("MANUFACTURER"));
						cartListVal.setPartNumber(rs.getString("NC_PART_NUMBER"));
						cartListVal.setShortDesc(rs.getString("SHORT_DESCRIPTION"));
						cartListVal.setPrice(rs.getDouble("PRICE"));
						cartListVal.setUnitPrice(rs.getDouble("PRICE"));
						cartListVal.setQty(rs.getInt("QTY"));
						cartListVal.setOverRidePriceRule("Y");
						cartListVal.setTotal(Double.parseDouble(df.format(rs.getDouble("PRICE") * rs.getDouble("QTY"))));
						cartListVal.setBrandName(rs.getString("BRAND"));
					}else{
						cartListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
						cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
						cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
						cartListVal.setPrice(rs.getDouble("NET_PRICE"));
						cartListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
						cartListVal.setQty(rs.getInt("QTY"));
						cartListVal.setTotal(Double.parseDouble(df.format(rs.getDouble("NET_PRICE") * rs.getDouble("QTY"))));
						cartListVal.setBrandName(rs.getString("BRAND_NAME"));
					}
					if(rs.findColumn("CATALOG_ID")>0){
						cartListVal.setCatalogId(rs.getString("CATALOG_ID"));	
					}
					if(CommonUtility.validateString(cartListVal.getCatalogId()).length() <= 0) {
						partIdentifier.add(cartListVal.getPartNumber());
					}
					partIdentifierQuantity.add(rs.getInt("QTY"));
					itemList.add(rs.getInt("ITEM_ID"));
					if(rs.getInt("PACKAGE_QTY")>0){
						packageQty = rs.getInt("PACKAGE_QTY");
					}
					cartListVal.setPackageFlag(rs.getInt("PACKAGE_FLAG"));
					cartListVal.setPackageQty(packageQty);
					if(rs.findColumn("UOM")>0 && rs.getString("SALES_UOM")==null){
						cartListVal.setUom(CommonUtility.validateString(rs.getString("UOM")));
					}
					
					cartListVal.setThresholdQty(rs.getInt("THRESHOLD_QTY"));
					cartListVal.setReplenishToQty(rs.getInt("REPLENISH_TO_QTY"));
					cartListVal.setSaleQty(rs.getInt("ORDERED_QTY"));
					cartListVal.setSubsetId(rs.getInt("SUBSET_ID"));
					productListData.add(cartListVal);
				}				
			}
			contentObject.put("productListData", productListData);
			
		}catch(SQLException sqlEx) {
			logger.error(sqlEx.getMessage());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}finally {
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);		
		}
		return contentObject;
	}
	
	public static ArrayList<Integer> selectFromVMICart(int userId, int customerId, String partNumber){
		ArrayList<Integer> cartId = new ArrayList<Integer>();		
		Connection conn = null;
		int siteId = 0;
		if(CommonDBQuery.getGlobalSiteId() > 0 ) {
			siteId = CommonDBQuery.getGlobalSiteId();
		}
		String sql = ""; 
		sql = PropertyAction.SqlContainer.get("selectFromVMICart");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try	{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			pstmt.setString(2, partNumber);
			pstmt.setInt(3, siteId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				cartId.add(rs.getInt("CART_ID"));
				cartId.add(rs.getInt("QTY"));
			}
		}catch(SQLException sqlEx){
			logger.error(sqlEx.getMessage());
		}catch(Exception e)	{
			logger.error(e.getMessage());
		}finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);
		}
		return cartId;
	}
}
