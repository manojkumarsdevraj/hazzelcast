package com.unilog.api.bronto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.SalesModel;
import com.unilog.security.SecureData;
import com.unilog.users.AddressModel;
import com.unilog.utility.CommonUtility;

public class BrontoDAO {

	public static void addAbandonedCart(int subsetId, int userId, String sessionId, String beforeLogin){
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		try{
			conn = ConnectionManager.getDBConnection();
			int count1 = 0;
			Date timeNow = new Date();
			String key = SecureData.getPunchoutSecurePassword(timeNow.toString());
			if(checkAbandonedCartEntry(conn, userId, sessionId)){
				sql = PropertyAction.SqlContainer.get("updateAbandonedCart");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, key);
				pstmt.setInt(2, userId);
				pstmt.setString(3, sessionId);
				count1 = pstmt.executeUpdate();
			}else{
				sql = PropertyAction.SqlContainer.get("insertAbandonedCart");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				pstmt.setString(2, key);
				pstmt.setInt(3, subsetId);
				pstmt.setString(4, sessionId);
				pstmt.setString(5, beforeLogin);
				count1 = pstmt.executeUpdate();
			}

			if(count1 > 0){
				System.out.println("Abandoned cart entry added.");
			}else{
				System.out.println("Abandoned cart entry add failed.");
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		finally {	    
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);	
		}
	}

	public static boolean checkAbandonedCartEntry(Connection conn, int userId, String sessionId){

		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "";
			sql = PropertyAction.SqlContainer.get("checkAbandonedCartEntry");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setString(2, sessionId);
			rs =pstmt.executeQuery();

			if(rs.next()){
				if(CommonUtility.validateString(rs.getString("EMAIL_STATUS")).equalsIgnoreCase("N")){
					flag = true;
				}
			}
		}catch (SQLException e) { 
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}
		return flag;
	}


	public static LinkedHashMap<String, Object> getShoppingCartForAbandonedCart(int userId, String sessionId, int subsetId, int generalSubset, LinkedHashMap<String, Object> contentObject, boolean isBeforeLogin){


		ArrayList<Integer> itemList = new ArrayList<Integer>();
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt = null;

		double total = 0.0;
		double cartTotal = 0.0;
		HttpSession session = null;
		DecimalFormat df = CommonUtility.getPricePrecision(session);
		int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
		/*double flatRate = 0.0;
		String freightMessage = null;
		String freightHeader = null;
		String freeFrieghtMessage = null;
		boolean isOverSizeItem = false;
		String isPreauth = "N";*/
		String sql = "";
		try{
			int siteId = 0;
			if(CommonDBQuery.getGlobalSiteId()>0){
				siteId = CommonDBQuery.getGlobalSiteId();
			}
			conn = ConnectionManager.getDBConnection();
			ArrayList<ProductsModel> productListData = new ArrayList<ProductsModel>();

			if(!isBeforeLogin){
				sql = PropertyAction.SqlContainer.get("getCartItemDetailQuery");
				sql = "SELECT * FROM ("+sql+") ORDER BY  CART_ID ";

				pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, siteId);
                pstmt.setInt(2, userId);
                pstmt.setInt(3, subsetId);
                pstmt.setInt(4, activeTaxonomyId);
                pstmt.setInt(5, siteId);
                pstmt.setInt(6, userId);
                pstmt.setInt(7, generalSubset);
                pstmt.setInt(8, activeTaxonomyId);
                pstmt.setInt(9, siteId);
                pstmt.setInt(10, userId);
                pstmt.setInt(11, subsetId);
			}else{

				sql = PropertyAction.SqlContainer.get("getCartItemDetailQueryBySession");
				sql = "SELECT * FROM ("+sql+") ORDER BY CART_ID";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, siteId);
				pstmt.setInt(2, 1);
				pstmt.setString(3, sessionId);
				pstmt.setInt(4, subsetId);
				pstmt.setInt(5, activeTaxonomyId);
				pstmt.setInt(6, siteId);
				pstmt.setInt(7, 1);
				pstmt.setString(8, sessionId);
				pstmt.setInt(9, generalSubset);
				pstmt.setInt(10, activeTaxonomyId);
				pstmt.setInt(11, siteId);
				pstmt.setInt(12, 1);
				pstmt.setString(13, sessionId);
				pstmt.setInt(14, subsetId);
				rs = pstmt.executeQuery();

			}
			rs = pstmt.executeQuery();

			while(rs.next()){

				ProductsModel cartListVal = new ProductsModel();
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
				int packageQty = 1;
				double price = CommonUtility.validateDoubleNumber(rs.getString("PRICE"));
				double extPrice = price * rs.getInt("QTY");
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
				cartListVal.setPrice(price);
				cartListVal.setExtendedPrice(extPrice);
				if(rs.findColumn("CATALOG_ID")>0){
					cartListVal.setCatalogId(rs.getString("CATALOG_ID"));	
				}

				itemList.add(rs.getInt("ITEM_ID"));
				if(rs.getInt("PACKAGE_QTY")>0){
					packageQty = rs.getInt("PACKAGE_QTY");
				}
				cartListVal.setPackageFlag(rs.getInt("PACKAGE_FLAG"));
				cartListVal.setPackageQty(packageQty);
				total = total + extPrice;

				productListData.add(cartListVal);
				partIdentifierQuantity.add(rs.getInt("QTY"));
			}

			/*UsersModel usersModel = UsersDAO.getEntityDetailsByUserId(userId);

			ProductManagementModel priceInquiryInput = new ProductManagementModel();
			priceInquiryInput.setPartIdentifier(productListData);
			priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
			priceInquiryInput.setRequiredAvailabilty("Y");
			priceInquiryInput.setWareHouse(CommonUtility.validateParseIntegerToString(usersModel.getWareHouseCode()));
			priceInquiryInput.setCustomerId(usersModel.getContactId());
			priceInquiryInput.setCustomerCountry(usersModel.getCountry());
			productListData = BrontoUtility.getInstance().massProductInquiry(priceInquiryInput);

			total = productListData.get(0).getCartTotal();*/

			String twoDecimalTotal = df.format(total);
			cartTotal = Double.parseDouble(twoDecimalTotal);
			System.out.println("CartTotal In GetShopping Cart : "+cartTotal);
			contentObject.put("productListData", productListData);
			contentObject.put("cartSubTotal", cartTotal);
			contentObject.put("cartTotal", cartTotal);
		}catch(SQLException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return contentObject;
	}



	public static HashMap<String, Object> getOrderDetailsForBronto(int orderId, int userId){

		HashMap<String, Object> order = new LinkedHashMap<String, Object>();
		double orderTotal = 0d;
		ArrayList<SalesModel> orderItemList = new ArrayList<SalesModel>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		boolean flag=false;
		try{
			conn = ConnectionManager.getDBConnection();

			String sql = PropertyAction.SqlContainer.get("getOrderDetail");

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, orderId);
			pstmt.setInt(2, userId);
			rs = pstmt.executeQuery();

			CreditCardModel creditCardValue = null;
			if(rs.next()){
				creditCardValue = new CreditCardModel();
				SalesModel salesOrderDetail = new SalesModel();
				salesOrderDetail.setShipMethod(rs.getString("SHIP_METHOD"));


				salesOrderDetail.setOrderedBy(rs.getString("ORDERED_BY"));
				salesOrderDetail.setPoNumber(rs.getString("PURCHASE_ORDER_NUMBER"));
				salesOrderDetail.setOrderNum(rs.getString("ORDER_NUMBER"));
				salesOrderDetail.setExternalSystemId(rs.getString("EXTERNAL_SYSTEM_ID")!=null?rs.getString("EXTERNAL_SYSTEM_ID"):"");
				salesOrderDetail.setBillAddress1(rs.getString("BILL_ADDRESS1"));
				salesOrderDetail.setBillAddress2(rs.getString("BILL_ADDRESS2"));
				salesOrderDetail.setBillCity(rs.getString("BILL_CITY"));
				salesOrderDetail.setBillCountry(rs.getString("BILL_COUNTRY_CODE"));
				salesOrderDetail.setBillPhone(rs.getString("BILL_PHONE"));
				salesOrderDetail.setBillState(rs.getString("BILL_STATE"));
				salesOrderDetail.setBillZipCode(rs.getString("BILL_ZIP_CODE"));
				salesOrderDetail.setShipAddress1(rs.getString("SHIP_ADDRESS1"));
				salesOrderDetail.setShipAddress2(rs.getString("SHIP_ADDRESS2"));
				salesOrderDetail.setShipCity(rs.getString("SHIP_CITY"));
				salesOrderDetail.setShipCountry(rs.getString("SHIP_COUNTRY_CODE"));
				salesOrderDetail.setShipPhone(rs.getString("SHIP_PHONE"));
				salesOrderDetail.setShipState(rs.getString("SHIP_STATE"));
				salesOrderDetail.setShipZipCode(rs.getString("SHIP_ZIP_CODE"));
				salesOrderDetail.setTax(rs.getDouble("TAX_AMOUNT"));
				salesOrderDetail.setSubtotal(rs.getDouble("SUBTOTAL_AMOUNT"));
				salesOrderDetail.setFreight(rs.getDouble("FREIGHT"));
				salesOrderDetail.setHandling(rs.getDouble("HANDLING_FEE"));
				salesOrderDetail.setDiscount(rs.getDouble("CASHDISCOUNT_AMOUNT"));
				salesOrderDetail.setTotal(rs.getDouble("TOTAL_AMOUNT"));
				salesOrderDetail.setOrderStatusDesc(rs.getString("EXTERNAL_SYSTEM_ERROR"));
				salesOrderDetail.setRefrenceKey(rs.getString("REFERENCE_KEY"));
				salesOrderDetail.setPaymentMethod(rs.getString("PAYMENT_METHOD"));
				salesOrderDetail.setTransactionId(rs.getString("TRANSACTION_ID"));
				salesOrderDetail.setCustomerName(rs.getString("CUSTOMER_NAME"));
				salesOrderDetail.setShipFirstName(rs.getString("SHIP_FIRST_NAME"));
				salesOrderDetail.setShipLastName(rs.getString("SHIP_LAST_NAME"));
				salesOrderDetail.setCustomerReleaseNumber(rs.getString("CUSTOMER_RELEASE_NUMBER"));
				salesOrderDetail.setHomeBranchName(rs.getString("PRICING_BRANCH_NAME"));
				SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
				salesOrderDetail.setShipFirstName(rs.getString("SHIP_FIRST_NAME"));
				salesOrderDetail.setShipLastName(rs.getString("SHIP_LAST_NAME"));

				AddressModel addressModel = new AddressModel();
				addressModel.setFirstName(rs.getString("BILL_FIRST_NAME"));
				addressModel.setLastName(rs.getString("BILL_LAST_NAME"));
				addressModel.setAddress1(rs.getString("BILL_ADDRESS1"));
				addressModel.setAddress2(rs.getString("BILL_ADDRESS2"));
				addressModel.setCity(rs.getString("BILL_CITY"));
				addressModel.setCountry(rs.getString("BILL_COUNTRY_CODE"));
				addressModel.setPhoneNo(rs.getString("BILL_PHONE"));
				addressModel.setState(rs.getString("BILL_STATE"));
				addressModel.setZipCode(rs.getString("BILL_ZIP_CODE"));
				addressModel.setShipToId(rs.getString("BILL_SHIP_TO_ID"));
				addressModel.setCompanyName(rs.getString("CUSTOMER_NAME"));
				salesOrderDetail.setBillAddress(addressModel);

				addressModel = new AddressModel();
				addressModel.setFirstName(rs.getString("SHIP_FIRST_NAME"));
				addressModel.setLastName(rs.getString("SHIP_LAST_NAME"));
				addressModel.setAddress1(rs.getString("SHIP_ADDRESS1"));
				addressModel.setAddress2(rs.getString("SHIP_ADDRESS2"));
				addressModel.setCity(rs.getString("SHIP_CITY"));
				addressModel.setCountry(rs.getString("SHIP_COUNTRY_CODE"));
				addressModel.setPhoneNo(rs.getString("SHIP_PHONE"));
				addressModel.setState(rs.getString("SHIP_STATE"));
				addressModel.setZipCode(rs.getString("SHIP_ZIP_CODE"));
				addressModel.setShipToId(rs.getString("SHIPPING_SHIP_TO_ID"));
				addressModel.setShipToName(rs.getString("SHIPPING_SHIP_TO_NAME"));
				salesOrderDetail.setShipAddress(addressModel);				

				String reqDate = "";
				if(rs.getString("REQUIRED_BY_DATE")!=null && rs.getString("REQUIRED_BY_DATE").trim().length()>0){
					reqDate = myFormat.format(fromDB.parse(rs.getString("REQUIRED_BY_DATE")));
				}
				salesOrderDetail.setReqDate(reqDate);
				salesOrderDetail.setShippingInstruction(rs.getString("SHIPPING_INSTRUCTIONS"));
				salesOrderDetail.setOrderNotes(rs.getString("ORDER_NOTES"));
				salesOrderDetail.setUserNote(rs.getString("USER_NOTE"));
				salesOrderDetail.setShipToName(rs.getString("SHIPPING_SHIP_TO_NAME"));

				salesOrderDetail.setBillEmailAddress(rs.getString("BILL_EMAIL_ADDRESS"));

				if(CommonUtility.validateString(rs.getString("SHIP_EMAIL_ADDRESS")).trim().length()>0){
					salesOrderDetail.setShipEmailAddress(rs.getString("SHIP_EMAIL_ADDRESS"));
				}

				creditCardValue = new CreditCardModel();
				/*creditCardValue.setCardHolder(cardHolder);
				creditCardValue.setDate(ccExp);
				creditCardValue.setElementPaymentAccountId(ccTransactionId);
				creditCardValue.setAddress1(streetAddress);
				creditCardValue.setZipCode(postalCode);
				creditCardValue.setCreditCardResponseCode(ccResponseCode);
				creditCardValue.setCreditCardStatus(ccStatus);
				creditCardValue.setCreditCardHostRefNumber(ccHostRefNumber);
				creditCardValue.setCreditCardTaskID(ccTaskID);
				creditCardValue.setCreditCardAmount(ccAmount);
				creditCardValue.setCreditCardDeclineResponseReason(ccDeclineResponseReason);
				creditCardValue.setCreditCardCvv2VrfyCode(ccCvv2VrfyCode);
				creditCardValue.setCreditCardTip(ccTip);
				creditCardValue.setCreditCardTransTimeStamp(ccTransTimeStamp);
				creditCardValue.setCreditCardToken(ccToken);
				creditCardValue.setCreditCardApprovedAmount(ccApprovedAmount);
				creditCardValue.setCreditCardRequestedAmount(ccRequestedAmount);
				creditCardValue.setCreditCardHostResponseCode(ccHostResponseCode);
				creditCardValue.setCreditCardInvoice(ccInvoice);
				creditCardValue.setCreditCardApprovalCode(ccApprovalCode);*/
				creditCardValue.setCreditCardTransactionID(rs.getString("TRANSACTION_ID"));
				/*creditCardValue.setCreditCardServerTimestamp(ccServerTimestamp);
				creditCardValue.setCreditCardType(ccType);
				creditCardValue.setCreditCardFee(ccFee);
				creditCardValue.setCreditCardExternalSessionID(ccExternalSessionID);
				creditCardValue.setCreditCardAddVrfyCode(ccAddVrfyCode);
				creditCardValue.setCreditCardTax(ccTax);
				creditCardValue.setCreditCardNewDomainKey(ccNewDomainKey);
				creditCardValue.setCreditCardNumber(ccNumber);*/


				order.put("orderDetail", salesOrderDetail);
				flag = true;
			}


			if(flag){
				rs.close();
				pstmt.close();
				DecimalFormat df2;
				String pricePrecision = CommonUtility.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision"));
				if(pricePrecision.equalsIgnoreCase("2")){
					df2 = new DecimalFormat( "####0.00" );
				}else{
					df2 = new DecimalFormat( "####0.0000" );
				}
				sql = PropertyAction.SqlContainer.get("getOrderItemDetail");
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom")).length()>0){
					pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom"))));
					pstmt.setInt(1,CommonDBQuery.getGlobalSiteId());
					pstmt.setInt(2, orderId);
				}else{
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, orderId);
				}
				rs = pstmt.executeQuery();

				while(rs.next()){
					SalesModel salesOrderDetail = new SalesModel();
					salesOrderDetail.setItemId(rs.getInt("ITEM_ID"));
					salesOrderDetail.setPartNumber(rs.getString("PART_NUMBER"));
					salesOrderDetail.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
					salesOrderDetail.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
					salesOrderDetail.setShortDesc(rs.getString("SHORT_DESC"));
					salesOrderDetail.setOrderQty(rs.getInt("QTY"));
					salesOrderDetail.setOrderPrice(Double.parseDouble(df2.format(rs.getDouble("PRICE"))));
					salesOrderDetail.setOrderPriceStr(df2.format(rs.getDouble("PRICE")));
					salesOrderDetail.setOrderUom(rs.getString("UOM"));
					salesOrderDetail.setTotal(Double.parseDouble(df2.format(rs.getDouble("EXTPRICE"))));
					salesOrderDetail.setTotalStr(df2.format(rs.getDouble("EXTPRICE")));
					salesOrderDetail.setTotalV2(Double.parseDouble(df2.format(rs.getDouble("EXT_PRICE"))));
					salesOrderDetail.setTotalStrV2(df2.format(rs.getDouble("EXT_PRICE")));
					salesOrderDetail.setUom(rs.getString("PER_UOM"));
					salesOrderDetail.setCatalogId(rs.getString("CATALOG_ID"));						
					salesOrderDetail.setImageType(rs.getString("IMAGE_TYPE"));
					salesOrderDetail.setHazardiousMaterial(rs.getString("HAZARDIOUS_MATERIAL"));
					String imageName = "";
					if(rs.getString("IMAGE_NAME")==null){
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES")).length()>0){
							imageName = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")+CommonDBQuery.getSystemParamtersList().get("WEB_THEMES")+CommonDBQuery.getSystemParamtersList().get("SITE_NAME")+"/images/NoImage.png";	
						}else{
							imageName = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")+"/WEB_THEMES/"+CommonDBQuery.getSystemParamtersList().get("SITE_NAME")+"/images/NoImage.png";
						}

					}else{
						imageName = rs.getString("IMAGE_NAME");
						if(rs.getString("IMAGE_TYPE")!=null && rs.getString("IMAGE_TYPE").trim().equalsIgnoreCase("URL")){
							imageName = rs.getString("IMAGE_NAME");
						}else{
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ORDER_MAIL_IMAGE_FOLDER_NAME")).length()>0){
								imageName = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")+CommonDBQuery.getSystemParamtersList().get("ORDER_MAIL_IMAGE_FOLDER_NAME")+imageName;
							}else{
								imageName = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")+CommonDBQuery.getSystemParamtersList().get("PRODUCT_IMAGES_FOLDER")+"/75X75px/"+imageName;	
							}


						}
					}
					salesOrderDetail.setImageName(imageName);
					orderTotal = orderTotal + rs.getDouble("EXTPRICE");
					orderItemList.add(salesOrderDetail);
				}

				order.put("orderTotal", orderTotal);
				order.put("orderItemList", orderItemList);

			}
		}catch (SQLException e) { 
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);
		}
		return order;
	}


	public static int updateAbandonedCartStatus(int userId, String key, String status, String type, String sessionId)
	{
		Connection conn = null;
		int counter = 0;
		PreparedStatement pstmt = null;
		String sql = "";
		if(CommonUtility.validateString(type).equalsIgnoreCase("cartProcessed")){
			sql = "UPDATE ABANDONED_CART SET CART_PROCESSED = ? WHERE USER_ID = ? AND SECRET_KEY=? AND SESSIONID = ?";
		}else{
			sql = "UPDATE ABANDONED_CART SET EMAIL_STATUS = ? WHERE USER_ID = ? AND SECRET_KEY=? AND SESSIONID = ?";
		}
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, status);
			pstmt.setInt(2, userId);
			pstmt.setString(3, key);
			pstmt.setString(4, sessionId);
			counter = pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);
		}

		return counter;
	}


	public static  int getAbandonedCartUserIdFromKey(String key, String sessionId)
	{
		Connection  conn = null;
		PreparedStatement preStat=null;
		ResultSet rs = null;
		int userId = 0;

		try {
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getAbandonedCartUserIdFromKey");
			preStat = conn.prepareStatement(sql);	
			preStat.setString(1, key);
			preStat.setString(2, sessionId);
			rs =preStat.executeQuery();

			while(rs.next()){

				if(CommonUtility.validateNumber(rs.getInt("USER_ID")+"") > 0){
					userId = rs.getInt("USER_ID");
				}
			}
		} catch (Exception e) {         
			e.printStackTrace();

		} finally {	    	
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(preStat);
			ConnectionManager.closeDBConnection(conn);	
		} 
		return userId;
	}

	public static  int getWLAbandonedCartUserIdFromKey(String sessionId)
	{
		Connection  conn = null;
		PreparedStatement preStat=null;
		ResultSet rs = null;
		int userId = 0;

		try {
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getWLAbandonedCartUserIdFromKey");
			preStat = conn.prepareStatement(sql);	
			preStat.setString(1, sessionId);
			rs =preStat.executeQuery();

			while(rs.next()){

				if(CommonUtility.validateNumber(rs.getInt("USER_ID")+"") > 0){
					userId = rs.getInt("USER_ID");
				}
			}
		} catch (Exception e) {         
			e.printStackTrace();

		} finally {	    	
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(preStat);
			ConnectionManager.closeDBConnection(conn);	
		} 
		return userId;
	}

	public static int abandonedCartLoginDisable(int userId, String key, String sessionId)
	{
		Connection  conn = null;
		PreparedStatement pstmt=null;
		String sql = "";
		int count  = 0;
		try {
			conn = ConnectionManager.getDBConnection();
			sql = PropertyAction.SqlContainer.get("updateAbandonedCartKeyStatus");
			if(CommonUtility.validateString(sql).length()>0){
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				pstmt.setString(2, key);
				pstmt.setString(3, sessionId);
				count = pstmt.executeUpdate();	
			}else{
				System.out.println("No SQL Query in Sqlcontainer File:abandonedCartLoginDisable");
			}
		}catch (SQLException e) { 
			e.printStackTrace();
		}finally {	    
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);	
		}
		return count;
	}

	public static String getAbandonedCartKey(int userId, String sessionId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String secretKey = "";
		try{
			conn = ConnectionManager.getDBConnection();
			String sql =  PropertyAction.SqlContainer.get("getAbandonedCartKey"); 
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setString(2, sessionId);
			rs =pstmt.executeQuery();

			if(rs.next()){
				if(CommonUtility.validateString(rs.getString("SECRET_KEY")).length() > 0){
					secretKey = rs.getString("SECRET_KEY");
				}
			}
		}catch (SQLException e) { 
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);

		}
		return secretKey;	
	}

	public static int updateAbandonedCartSessionId(String oldSessionId,String newSessionId)
	{
		Connection  conn = null;
		PreparedStatement pstmt = null;
		int count=0;


		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("updateAbandonedCartSessionId");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,newSessionId);
			pstmt.setString(2, oldSessionId);
			count = pstmt.executeUpdate();


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

	public static BrontoModel getOrderAndCouponDetails(int userId, BrontoModel brontoModel){

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		String dateString = null;
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		Date date = null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getOrderAndCouponDetails");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,userId);

			rs = pstmt.executeQuery();

			if(rs.next()){
				brontoModel.setCouponTotalDiscounts(rs.getString("COUPONTOTALDISCOUNTS"));

				dateString = CommonUtility.validateString(rs.getString("ORDERFIRSTDATE"));
				date = dateTimeFormat.parse(dateString);
				brontoModel.setOrderFirstDate(date);

				dateString = CommonUtility.validateString(rs.getString("ORDERLASTDATE"));
				date = dateTimeFormat.parse(dateString);
				brontoModel.setOrderLastDate(date);

				brontoModel.setOrderTotalNumber(rs.getString("ORDERTOTALNUMBER"));
				brontoModel.setCouponFirstCode(rs.getString("COUPONFIRSTCODE"));
				brontoModel.setCouponLastCode(rs.getString("COUPONLASTCODE"));
				brontoModel.setCouponTotalNumber(rs.getString("COUPONTOTALNUMBER"));
				brontoModel.setOrderAverageTotal(rs.getString("ORDERAVERAGETOTAL"));
				brontoModel.setOrderLastTotal(rs.getString("ORDERLASTTOTAL"));
				brontoModel.setOrderTotalRevenue(rs.getString("ORDERTOTALREVENUE"));
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
		return brontoModel;
	}
}
