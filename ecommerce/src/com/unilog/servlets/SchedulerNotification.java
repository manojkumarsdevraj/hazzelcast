package com.unilog.servlets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.SendMailUtility;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

public class SchedulerNotification{
	
	public void execute() {
		
		Connection  conn = null;
	    PreparedStatement preStat=null;
	    ResultSet rs = null;
	    HashMap<String,String> userDetails = new HashMap<String,String>();
	    ArrayList<ProductsModel> itemDetailList = null;
	    String mailSubject="Did you forget something?";
	    
		try{
			conn = ConnectionManager.getDBConnection();
	          preStat = conn.prepareStatement(PropertyAction.SqlContainer.get("getAbandonCart"));			 
			  rs =preStat.executeQuery();
			  while(rs.next()){
				  String userName = rs.getString("user_name");
				  String status = rs.getString("status");
				  String firstName = rs.getString("first_name");
				  String lastName = rs.getString("last_name");
				  String emailId = rs.getString("email");
				  userDetails = UsersDAO.getUserPasswordAndUserId(userName, status);
				  if(userDetails!=null && userDetails.size()>0){
					  itemDetailList = getAbandonShoppingCart(userDetails);
					  SendMailUtility sendAbandonCartMail = new SendMailUtility();
					  boolean flag = sendAbandonCartMail.sendAbandonCartMail(itemDetailList,firstName,lastName,emailId,mailSubject);	
					  if(flag){
						  System.out.println("-------------Abandon cart Mail sent-------------");
						  //Update cart 
						  Integer userId = CommonUtility.validateNumber(userDetails.get("userId"));
						  ProductsDAO.updateABCartNotificaion(userId);
					  }else{
						  System.out.println("-------------Abandon cart something went wrong-------------");
					  }
				  }
			  }
			 
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public static ArrayList<ProductsModel> getAbandonShoppingCart(HashMap<String,String> userDetails){
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    ArrayList<ProductsModel> productListData = new ArrayList<ProductsModel>();
	    int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
	    String sortByValue = " PART_NUMBER ASC ";
	    String quickCartView = null;
	    DecimalFormat df = new DecimalFormat("####0.00");
	    ArrayList<String> partIdentifier = new ArrayList<String>();
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		ArrayList<Double> nglantzPartIdentifierQuantity = new ArrayList<Double>();		//newly added for Nglantz Qty. decimal changes
	    
	    try{
	    	int siteId = 0;
    		if(CommonDBQuery.getGlobalSiteId()>0){
        		siteId = CommonDBQuery.getGlobalSiteId();
    		}
	    	conn = ConnectionManager.getDBConnection();
	    	String sql = PropertyAction.SqlContainer.get("getCartItemDetailQuery");
    		sql = "SELECT * FROM ("+sql+") ORDER BY "+sortByValue;
    		if(quickCartView!=null && quickCartView.trim().equalsIgnoreCase("Y")){
    			sql = "SELECT * FROM (SELECT * FROM ("+sql+")ORDER BY CART_ID DESC) WHERE ROWNUM<=4";
    		}
    		
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, siteId);
			pstmt.setInt(2, Integer.parseInt(userDetails.get("userId")));
			pstmt.setInt(3,  Integer.parseInt(userDetails.get("userSubsetId")));
            pstmt.setInt(4, activeTaxonomyId);
            pstmt.setInt(5, siteId);
			pstmt.setInt(6, Integer.parseInt(userDetails.get("userId")));
			pstmt.setInt(7, 0);
            pstmt.setInt(8, activeTaxonomyId);
            pstmt.setInt(9, siteId);
			pstmt.setInt(10, Integer.parseInt(userDetails.get("userId")));
			pstmt.setInt(11,  Integer.parseInt(userDetails.get("userSubsetId")));
            rs = pstmt.executeQuery();
			while(rs.next()){
       			
				ProductsModel cartListVal = new ProductsModel();
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
				int packageQty = 1;
            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
            	itemUrl = itemUrl.replaceAll(" ","-");
            	cartListVal.setItemUrl(itemUrl);
				cartListVal.setProductListId(rs.getInt("CART_ID"));
				cartListVal.setItemId(rs.getInt("ITEM_ID"));
				cartListVal.setQty(rs.getInt("QTY"));
				cartListVal.setPrice(rs.getDouble("NET_PRICE"));
				cartListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
				cartListVal.setTotal(Double.parseDouble(df.format(rs.getDouble("NET_PRICE") * rs.getDouble("QTY"))));
				cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
				cartListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
				cartListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
				cartListVal.setBrandName(rs.getString("BRAND_NAME"));
				cartListVal.setImageType(rs.getString("IMAGE_TYPE"));
				cartListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				cartListVal.setUnspc(rs.getString("UNSPSC"));
				cartListVal.setPackDesc(rs.getString("PACK_DESC"));
				cartListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				cartListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				cartListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
				cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				cartListVal.setUom(rs.getString("SALES_UOM"));
				cartListVal.setSalesUom(rs.getString("SALES_UOM"));
				cartListVal.setSaleQty(rs.getInt("SALES_QTY"));
				cartListVal.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
				cartListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
				cartListVal.setPageTitle(rs.getString("PAGE_TITLE"));
				cartListVal.setMultipleShipVia(rs.getString("ITEM_LEVEL_SHIPVIA"));
				cartListVal.setMultipleShipViaDesc(rs.getString("ITEM_LEVEL_SHIPVIA_DESC"));
				
				partIdentifier.add(cartListVal.getPartNumber());
				partIdentifierQuantity.add(rs.getInt("QTY"));
				nglantzPartIdentifierQuantity.add(rs.getDouble("QTY"));		//newly added for Nglantz Qty. decimal changes
				
				if(rs.getInt("PACKAGE_QTY")>0){
					packageQty = rs.getInt("PACKAGE_QTY");
				}
				cartListVal.setPackageFlag(rs.getInt("PACKAGE_FLAG"));
				cartListVal.setPackageQty(packageQty);
				cartListVal.setExtendedPrice(rs.getDouble("EXTPRICE"));
				
				productListData.add(cartListVal);
				
				
			}
			
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally {	 
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	      }
	    
	    return productListData;
	}
	
	
	
}
