package com.unilog.sales;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.erp.service.ProductManagement;
import com.erp.service.impl.ProductManagementImpl;
import com.erp.service.model.ProductManagementModel;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailModel;
import com.unilog.defaults.SendMailUtility;
import com.unilog.ecomm.model.Coupon;
import com.unilog.ecomm.model.Discount;
import com.unilog.ecomm.model.DiscountType;
import com.unilog.logocustomization.LogoCustomization;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.users.AddressModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
public class SalesDAO {


	public static ArrayList<SalesModel> sendApprovalMailDao(ArrayList<SalesModel> salesOrderItem, String orderStatus, int orderId, int GroupId, int userId, int subsetId, int generalSubset, int buyingCompanyId)
	{
		//UsersModel userDetail = null;
		Connection  conn = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));

		try {
			conn = ConnectionManager.getDBConnection();
			if(orderStatus.contains("Approved")){
				System.out.println("inside order status contain -approved ");
				// QTY,PART_NUMBER ,IMAGE_NAME,SHORT_DESC,ITEM_ID ,UPDATED_DATETIME
				sql = PropertyAction.SqlContainer.get("getOrderItemDetail");
				System.out.println("Order Detail query: " +sql);
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom")).length()>0){
					System.out.println("inside system parameter-getOrderItemDetailCustom");
					pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom"))));
					pstmt.setInt(1,CommonDBQuery.getGlobalSiteId());					
					pstmt.setInt(2, orderId);
				}else{
					System.out.println("orderId in pstatement"+orderId);
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, orderId);
					
				}
				/*   pstmt = conn.prepareStatement(sql);*/
				rs = pstmt.executeQuery();
				System.out.println("ressult set q"+rs);
			}else if(orderStatus.contains("Rejected")){

				sql = PropertyAction.SqlContainer.get("selectGroupItem");
				//String type = "";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, GroupId);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, buyingCompanyId);
				pstmt.setInt(4, subsetId);
				pstmt.setInt(5, activeTaxonomyId);
				pstmt.setInt(6, GroupId);
				pstmt.setInt(7, userId);
				pstmt.setInt(8, buyingCompanyId);
				pstmt.setInt(9, generalSubset);
				pstmt.setInt(10, activeTaxonomyId);
				pstmt.setInt(11, GroupId);
				pstmt.setInt(12, subsetId);
				System.out.println(pstmt);
				rs = pstmt.executeQuery();
			}		
			while(rs.next())
			{
				if(rs.getInt("CART_ID")!=0){
					SalesModel salesOrderDetail = new SalesModel();
					salesOrderDetail.setItemId(rs.getInt("ITEM_ID"));
					salesOrderDetail.setPartNumber(rs.getString("PART_NUMBER"));
					salesOrderDetail.setShortDesc(rs.getString("SHORT_DESC"));
					salesOrderDetail.setOrderQty(rs.getInt("QTY"));
					salesOrderDetail.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
					String imageName = "";
					if(rs.getString("IMAGE_NAME")==null){
						imageName = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")+CommonDBQuery.getSystemParamtersList().get("WEB_THEMES")+CommonDBQuery.getSystemParamtersList().get("SITE_NAME")+"/images/NoImage.png";
					}else{
						imageName = rs.getString("IMAGE_NAME");

						if(!imageName.startsWith("http")){
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ORDER_MAIL_IMAGE_FOLDER_NAME")).length()>0){
								imageName = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")+CommonDBQuery.getSystemParamtersList().get("ORDER_MAIL_IMAGE_FOLDER_NAME")+imageName;
							}else{
								imageName = CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")+CommonDBQuery.getSystemParamtersList().get("PRODUCT_IMAGES_FOLDER")+"/75X75px/"+imageName;
							}
						}
					}
					salesOrderDetail.setImageName(imageName);
					salesOrderDetail.setUom(rs.getString("UOM"));
					salesOrderDetail.setPrice(rs.getDouble("PRICE"));
					salesOrderItem.add(salesOrderDetail);
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
		System.out.println("orderId:"+orderId);
		System.out.println("Inside send sendApprovalMailDao:"+salesOrderItem.size());
		return salesOrderItem;
	}

	

	public static ArrayList<ProductsModel> getOrderDetails(HttpSession session, int orderId)
	{
		ArrayList<ProductsModel> orderDetails = null;
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		String c = "";
		String idList = "";
		try
		{
			conn = ConnectionManager.getDBConnection();
			orderDetails = new ArrayList<ProductsModel>();
			String sql = PropertyAction.SqlContainer.get("getOrderItemQuery");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, orderId);
			rs = pstmt.executeQuery();
			//ITEM_ID,PART_NUMBER,SHORT_DESC,QTY,PRICE,UOM,EXTPRICE
			while(rs.next())
			{
				ProductsModel orderItemVal = new ProductsModel();
				orderItemVal.setItemId(rs.getInt("ITEM_ID"));
				orderItemVal.setPartNumber(rs.getString("PART_NUMBER"));
				orderItemVal.setShortDesc(rs.getString("SHORT_DESC"));
				orderItemVal.setQty(rs.getInt("QTY"));
				orderItemVal.setPrice(rs.getDouble("PRICE"));
				orderItemVal.setListPrice(rs.getDouble("LIST_PRICE"));
				orderItemVal.setUom(rs.getString("UOM"));
				orderItemVal.setTotal(rs.getDouble("EXTPRICE"));
				orderItemVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
				orderItemVal.setItemLevelRequiredByDate(rs.getString("ITEMLEVEL_REQUIREDBYDATE"));
				orderItemVal.setUomQty(rs.getInt("PER_QTY"));
				idList = idList + c + rs.getInt("ITEM_ID");
				c = " OR ";
				if(CommonUtility.checkDBColumn(rs, "GET_PRICE_FROM")){
					orderItemVal.setGetPriceFrom(rs.getString("GET_PRICE_FROM"));
					if(CommonUtility.validateString(rs.getString("GET_PRICE_FROM")).equalsIgnoreCase("CART")){
						if(CommonUtility.customServiceUtility()!=null){
							CommonUtility.customServiceUtility().configOrderItemValues(orderItemVal, rs);
						}
					}
				}
				orderDetails.add(orderItemVal);
			}
			int buyingCompanyId = 0;
			if (session.getAttribute("buyingCompanyId") != null) {
				buyingCompanyId = CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString());
				if(idList.length()>0){
					LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = ProductHunterSolr.getcustomerPartnumber(idList, buyingCompanyId, buyingCompanyId);
					if(customerPartNumber!=null && customerPartNumber.size()>0){
						for(ProductsModel item : orderDetails) {
							item.setCustomerPartNumberList(customerPartNumber.get(item.getItemId()));
						}
					}
				}
				
			}
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);
		}
		return orderDetails;
	}

	
	
	public static String approveCartBySequenceLevelDao(HttpSession session,  int requestTokenId,String approveSenderid,int nextLevelApprover,int loginUserDeq,String approverComments){
		String result = "";
		ResultSet rs = null;
		Connection  conn = null;
		String sql ="";
		PreparedStatement pstmt = null;
		String approverComment = approverComments;
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String updatedDate = (String) session.getAttribute("updatedDate");
		String senderID = (String) session.getAttribute("senderId");
		String savedGroupName = (String) session.getAttribute("groupName");
		//int userId = CommonUtility.validateNumber(sessionUserId);
		String reminderMail = "N";
		UsersModel ApproverEmail = ProductsDAO.getUserEmail(CommonUtility.validateNumber(sessionUserId));
		int count = 0;
		try {
			conn = ConnectionManager.getDBConnection();
		} catch (SQLException e) { 
			e.printStackTrace();
		}	
		try
		{
			/*sql= PropertyAction.SqlContainer.get("updateApproveCartStatus");*/
			sql = "UPDATE SAVED_ITEM_LIST SET APPROVAL_STATUS ='S', STATUS = 'S',APPROVER_COMMENT='"+approverComment+"', UPDATED_DATETIME=SYSDATE WHERE APPROVER_SEQ=? AND REQ_TOKEN_ID=? AND TYPE = 'A'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateParseIntegerToString(loginUserDeq));
			pstmt.setInt(2, requestTokenId);
			count = pstmt.executeUpdate();
			if(count>0){
				ConnectionManager.closeDBPreparedStatement(pstmt);
				String sql1 = PropertyAction.SqlContainer.get("getNextLevelApproverEmailId");
				pstmt = conn.prepareStatement(sql1);
				pstmt.setInt(1, requestTokenId);
				pstmt.setInt(2, CommonUtility.validateNumber(senderID));
				pstmt.setString(3, CommonUtility.validateParseIntegerToString(nextLevelApprover));
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					SendMailUtility mailObj = new SendMailUtility();
					mailObj.sendApprovalMailBySequenceLevel(savedGroupName,rs.getString("EMAIL"),rs.getString("FIRST_NAME"),rs.getString("LAST_NAME"),updatedDate,approverComment,ApproverEmail.getEmailAddress(),reminderMail);
					result = "1|Cart Approved";
				}

			}
			else{
				result="0|Cart Approval Failed. Please try after sometime";
			}

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
		return result;
	}


	public static ArrayList<SalesModel> getOpenOrdersList(SalesModel salesInputParameter)
	{
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt=null;
		ArrayList<SalesModel> orderStatus = null;
		int userId = 0;
		String reqType = "";
		String orderStatusType = "";



		try {
			conn = ConnectionManager.getDBConnection();
			if(salesInputParameter!=null){
				userId = salesInputParameter.getUserId();
				reqType = salesInputParameter.getReqType();
				orderStatusType = salesInputParameter.getOrderStatusType();
			}
			//COUNT(ORI.ORDER_ID) ITEM_CNT ORD.ORDER_ID, ORD.PURCHASE_ORDER_NUMBER, ORD.ORDER_NUMBER, ORD.EXTERNAL_SYSTEM_ID, ORD.ORDER_DATE, ORD.SUBTOTAL_AMOUNT, ORD.TOTAL_AMOUNT, ORD.SHIP_METHOD, ORD.ORDER_STATUS, ORD.REQUIRED_BY_DATE, ORD.SHIP_ADDRESS1,ORD.SHIP_ADDRESS2,ORD.SHIP_CITY,ORD.SHIP_STATE,ORD.SHIP_ZIP_CODE,ORD.SHIP_COUNTRY_CODE
			//ORD.USER_ID = ?  AND ORD.REQUEST_TYPE = ? AND ORD.ORDER_STATUS = ? 
			pstmt=conn.prepareStatement(PropertyAction.SqlContainer.get("getOpenOrdersList"));
			pstmt.setInt(1, userId);
			pstmt.setString(2, reqType);
			pstmt.setString(3, orderStatusType);
			rs=pstmt.executeQuery();
			orderStatus = new ArrayList<SalesModel>();
			while(rs.next())
			{
				SalesModel orderStatusVal = new SalesModel();
				orderStatusVal.setOrderId(rs.getInt("ORDER_ID"));
				orderStatusVal.setPoNumber(rs.getString("PURCHASE_ORDER_NUMBER"));
				orderStatusVal.setOrderNum(rs.getString("ORDER_NUMBER"));
				orderStatusVal.setExternalSystemId(rs.getString("EXTERNAL_SYSTEM_ID"));
				orderStatusVal.setOrderDate(rs.getString("ORDER_DATE"));
				orderStatusVal.setSubtotal(rs.getDouble("SUBTOTAL_AMOUNT"));
				orderStatusVal.setTotal(rs.getDouble("TOTAL_AMOUNT"));
				orderStatusVal.setShipMethod(rs.getString("SHIP_METHOD"));
				orderStatusVal.setFreight(rs.getDouble("FREIGHT"));
				orderStatusVal.setDiscount(rs.getDouble("DISCOUNT_AMOUNT"));
				if(orderStatusVal.getTotal()<=orderStatusVal.getSubtotal()) {
					orderStatusVal.setTotal(orderStatusVal.getSubtotal()+orderStatusVal.getFreight());
				}
				if(orderStatusVal.getDiscount()>0) {
					orderStatusVal.setTotal(orderStatusVal.getTotal()-orderStatusVal.getDiscount());
				}
				orderStatusVal.setOrderStatus(rs.getString("ORDER_STATUS"));
				orderStatusVal.setReqDate(rs.getString("REQUIRED_BY_DATE"));
				orderStatusVal.setShipAddress1(rs.getString("SHIP_ADDRESS1"));
				orderStatusVal.setShipAddress2(rs.getString("SHIP_ADDRESS2"));
				orderStatusVal.setShipCity(rs.getString("SHIP_CITY"));
				orderStatusVal.setShipState(rs.getString("SHIP_STATE"));
				orderStatusVal.setShipZipCode(rs.getString("SHIP_ZIP_CODE"));
				orderStatusVal.setShipCountry(rs.getString("SHIP_COUNTRY_CODE"));
				orderStatus.add(orderStatusVal);
			}

		} catch (Exception e) {         
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);	
		} 
		return orderStatus;

	}

	public static ArrayList<SalesModel> getOrdersHistory(SalesModel salesInputParameter){

		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt=null;
		ArrayList<SalesModel> orderStatus = new ArrayList<SalesModel>();
		Map<String, String> inputParam = null;
		int userId = 0;
		String orderNumber = "";
		String customerPoNumber = "";
		String startDate = "";
		String endDate = "";
		String customerErpId ="";
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

		try {
			conn = ConnectionManager.getDBConnection();
			if(salesInputParameter!=null){
				userId = salesInputParameter.getUserId();
				orderNumber = salesInputParameter.getOrderNum();
				customerPoNumber = salesInputParameter.getPoNumber();
				startDate = salesInputParameter.getStartDate();
				endDate = salesInputParameter.getEndDate();
			}
			if(CommonUtility.customServiceUtility() != null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MultipleShipToAddressCartAP")).equalsIgnoreCase("Y")) {
				if(CommonUtility.validateString(salesInputParameter.getEntityId()).length()>0){
					customerErpId = salesInputParameter.getEntityId();
				}else if(CommonUtility.validateString(salesInputParameter.getBillToEntityId()).length()>0){
					customerErpId = salesInputParameter.getBillToEntityId();
				}

				inputParam = new LinkedHashMap<>();
				inputParam.put("userId", String.valueOf(userId));
				inputParam.put("customerErpId", customerErpId);
				inputParam.put("orderNumber", orderNumber);
				inputParam.put("customerPoNumber", customerPoNumber);
				inputParam.put("startDate", startDate);
				inputParam.put("endDate", endDate);
				orderStatus = CommonUtility.customServiceUtility().getCustomerOrderHistory(inputParam);
			} else {
				pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getOrdersHistory"));
				pstmt.setInt(1, userId);
				
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SalesModel orderStatusVal = new SalesModel();
					orderStatusVal.setOrderId(rs.getInt("ORDER_ID"));
					orderStatusVal.setPoNumber(rs.getString("PURCHASE_ORDER_NUMBER"));
					orderStatusVal.setOrderNum(rs.getString("ORDER_NUMBER"));
					orderStatusVal.setExternalSystemId(rs.getString("EXTERNAL_SYSTEM_ID"));
					Date date = rs.getDate("ORDER_DATE");
					String orderDate = "";
					if(date!=null) {
						orderDate = df.format(date);
					}
					orderStatusVal.setOrderDate(orderDate);
					orderStatusVal.setSubtotal(rs.getDouble("SUBTOTAL_AMOUNT"));
					orderStatusVal.setTotal(rs.getDouble("TOTAL_AMOUNT"));
					orderStatusVal.setShipMethod(rs.getString("SHIP_METHOD"));
					orderStatusVal.setOrderStatus(rs.getString("ORDER_STATUS"));
					orderStatusVal.setReqDate(rs.getString("REQUIRED_BY_DATE"));
					orderStatusVal.setShipAddress1(rs.getString("SHIP_ADDRESS1"));
					orderStatusVal.setShipAddress2(rs.getString("SHIP_ADDRESS2"));
					orderStatusVal.setShipCity(rs.getString("SHIP_CITY"));
					orderStatusVal.setShipState(rs.getString("SHIP_STATE"));
					orderStatusVal.setShipZipCode(rs.getString("SHIP_ZIP_CODE"));
					orderStatusVal.setShipCountry(rs.getString("SHIP_COUNTRY_CODE"));
					orderStatusVal.setPaymentMethod(rs.getString("PAYMENT_METHOD"));
					orderStatusVal.setCarrierTrackingNumber(rs.getString("ORDER_TRACKING_NUMBER"));
					orderStatusVal.setQtyordered(rs.getInt("QTY"));
					orderStatusVal.setPrimaryOrderNumber(rs.getString("PRIMARY_ORDER_NUMBER"));
					orderStatus.add(orderStatusVal);
				}
			}

		} catch (Exception e) {         
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);	
		} 
		return orderStatus;

	}

	

	public static int sendConfirmationMail(int orderId,int userId,int type, String emailAddress,SalesModel otherDetail){

		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt = null;

		boolean flag = false;
		boolean sent = false;
		String sql = "";
		double salesOrderTotal = 0d;
		double salesOrderTotalV2 = 0d;
		SalesModel salesOrderDetailList = new SalesModel();
		SendMailModel sendMailModel = null;
		int punchoutUser = 0;
		DecimalFormat df2 = null;
		if(otherDetail!=null){
			 df2 = CommonUtility.getPricePrecision(otherDetail.getSession());
		}else{
			HttpSession session = null;
			 df2 = CommonUtility.getPricePrecision(session);
		}
		ArrayList<SalesModel> salesOrderItem = new ArrayList<SalesModel>();

		List<Discount> appliedlDiscounts = new ArrayList<Discount>();
		double orderItemsDiscountVal = 0.0D;
		double orderDiscountVal = 0.0D;
		double totalSavingOnOrder = 0.0D;
		double taxAmount = 0.0;
		String buyingCompanyId = "";
		int subsetId = 0;
		int generalSubset = 0;
		try {
			HttpSession session = null;
			if(otherDetail!=null){
				session = otherDetail.getSession();
				buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
				
				String tempSubset = (String) session.getAttribute("userSubsetId");
				String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
				
				subsetId = CommonUtility.validateNumber(tempSubset);
				generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			}
			
			
			
			
			conn = ConnectionManager.getDBConnection();
			sql = PropertyAction.SqlContainer.get("getOrderDetail");

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, orderId);
			pstmt.setInt(2, userId);
			rs = pstmt.executeQuery();

			CreditCardModel creditCardValue = null;
			if(rs.next()){
				creditCardValue = new CreditCardModel();
				SalesModel salesOrderDetail = new SalesModel();
				salesOrderDetail.setShipMethod(rs.getString("SHIP_METHOD"));
				LinkedHashMap<String, String> shipViaMap = new LinkedHashMap<String, String>();
				if(CommonUtility.validateString(rs.getString("SHIP_METHOD_ID")).length()>0 && session!=null && session.getAttribute("shipViaMap")!=null){
					shipViaMap = (LinkedHashMap<String, String>) session.getAttribute("shipViaMap");
					salesOrderDetail.setShipViaMethod(shipViaMap.get(rs.getString("SHIP_METHOD_ID").toUpperCase()));
				}else if(CommonUtility.validateString(rs.getString("SHIP_METHOD_ID")).length()>0 && session!=null && session.getAttribute("customerShipViaList")!=null){
					ArrayList<ShipVia> customerShipViaListArray = (ArrayList<ShipVia>)session.getAttribute("customerShipViaList");
					if(customerShipViaListArray!=null && customerShipViaListArray.size()>0){
						for(ShipVia shipVia:customerShipViaListArray){
							shipViaMap.put(shipVia.getShipViaID().trim().toUpperCase(), shipVia.getDescription());
						}
					}
					salesOrderDetail.setShipViaMethod(shipViaMap.get(rs.getString("SHIP_METHOD_ID").toUpperCase()));
				}else{
					salesOrderDetail.setShipViaMethod(rs.getString("SHIP_METHOD"));
					salesOrderDetail.setShipMethodId(rs.getString("SHIP_METHOD_ID"));
				}
				salesOrderDetail.setOrderSuffix(rs.getInt("ORDER_SUFFIX"));
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
				taxAmount = rs.getDouble("TAX_AMOUNT");
				salesOrderDetail.setSubtotal(rs.getDouble("SUBTOTAL_AMOUNT"));
				salesOrderDetail.setFreight(rs.getDouble("FREIGHT"));
				salesOrderDetail.setHandling(rs.getDouble("HANDLING_FEE"));
				salesOrderDetail.setDiscount(rs.getDouble("CASHDISCOUNT_AMOUNT"));
				salesOrderDetail.setTotal(rs.getDouble("TOTAL_AMOUNT"));
				salesOrderDetail.setTotalV2(rs.getDouble("TOTAL_AMOUNT"));
				salesOrderDetail.setSubtotalV2(rs.getDouble("SUBTOTAL_AMOUNT"));
				salesOrderDetail.setOrderStatusDesc(rs.getString("EXTERNAL_SYSTEM_ERROR"));
				salesOrderDetail.setRefrenceKey(rs.getString("REFERENCE_KEY"));
				salesOrderDetail.setDeliveryCharge(rs.getDouble("DELIVERY_FEE"));
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
				addressModel.setCompanyName(rs.getString("SHIP_COMPANY_NAME"));	
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
				String overRideShipEmail = "";

				salesOrderDetail.setBillEmailAddress(rs.getString("BILL_EMAIL_ADDRESS"));

				if(CommonUtility.validateString(rs.getString("SHIP_EMAIL_ADDRESS")).trim().length()>0){
					salesOrderDetail.setShipEmailAddress(rs.getString("SHIP_EMAIL_ADDRESS"));
				}else{
					if(CommonUtility.validateString((String)session.getAttribute("overRideShipEmail")).trim().length()>0){
						overRideShipEmail = (String)session.getAttribute("overRideShipEmail");
						session.setAttribute("overRideShipEmail",overRideShipEmail);
						salesOrderDetail.setShipEmailAddress(overRideShipEmail);
					}
				}


				if(rs.getString("DISCOUNT_COUPON_CODE") != null && rs.getString("DISCOUNT_COUPON_CODE").trim().length() > 0 ){
					salesOrderDetail.setDiscount(rs.getDouble("DISCOUNT_AMOUNT"));
					salesOrderDetail.setDiscountCouponCode(rs.getString("DISCOUNT_COUPON_CODE"));
					salesOrderDetail.setDiscountType(DiscountType.valueOf(rs.getString("DISCOUNT_TYPE")));
					LinkedHashMap<String, Object> orderDetails = new LinkedHashMap<String, Object>();
					orderDetails.put(SalesActionContantVariables.ORDER_ID, orderId);
					orderDetails.put(SalesActionContantVariables.USER_ID, userId);
					orderDetails.put(SalesActionContantVariables.COUPONS_CRUD_KEY,SalesActionContantVariables.COUPONS_CRUD_READ_VALUE);
					orderDetails = SalesDAO.CouponDataCrud(orderDetails);
					ArrayList<SalesModel> couponDetails = new ArrayList<SalesModel>();
					couponDetails = (ArrayList<SalesModel>) orderDetails.get("couponDetails");
					for(SalesModel eachCoupon:couponDetails){
						Coupon coupon = new Coupon();
						coupon.setCopounCode(eachCoupon.getDiscountCouponCode());
						Discount orderDiscount = new Discount();
						orderDiscount.setDiscountCoupon(coupon);
						orderDiscount.setDiscountValue(eachCoupon.getDiscount());
						orderDiscount.setDiscountType(eachCoupon.getDiscountType());
						appliedlDiscounts.add(orderDiscount);
					}
					orderDiscountVal = salesOrderDetail.getDiscount();
				}else{
					orderDiscountVal = salesOrderDetail.getDiscount();
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

				salesOrderDetail.setShippingBranchId(rs.getString("SHIPPING_BRANCH_ID"));
				salesOrderDetail.setOrderStatus(rs.getString("ORDER_STATUS"));
				salesOrderDetailList = salesOrderDetail;
				flag = true;
			}
			//ITEM_ID,PART_NUMBER,SHORT_DESC,QTY,PRICE,UOM, EXTPRICE
			if(flag){
				rs.close();
				pstmt.close();
				String idListForSolr = "";

				String cartSortByValue = "";
				String cartSortColumn = "";
				if(session.getAttribute("cartSortByValue")!=null){
					cartSortByValue = CommonUtility.validateString(session.getAttribute("cartSortByValue").toString());
					String[] sortCoumnArray = cartSortByValue.split(" ");
					if(sortCoumnArray!=null && sortCoumnArray.length>0){
						cartSortColumn = CommonUtility.validateString(sortCoumnArray[0]);
					}
				}
				sql = PropertyAction.SqlContainer.get("getOrderItemDetail");
				if(CommonUtility.validateString(cartSortColumn).length()>0 && sql.toUpperCase().contains(cartSortColumn.toUpperCase())){
					System.out.println("sendConfirmationMail cartSortByValue : "+cartSortByValue);
					sql = "SELECT * FROM ("+sql+") ORDER BY "+cartSortByValue;
				}

				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom")).length()>0){
					pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom"))));
					pstmt.setInt(1,CommonDBQuery.getGlobalSiteId());
					pstmt.setInt(2, orderId);
				}else{
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, orderId);
				}
				rs = pstmt.executeQuery();

				ArrayList<Integer> itemList = new ArrayList<Integer>();
				while(rs.next()){
					SalesModel salesOrderDetail = new SalesModel();
					itemList.add(rs.getInt("ITEM_ID"));
					salesOrderDetail.setListPrice(rs.getDouble("LIST_PRICE"));
					salesOrderDetail.setItemPriceId(CommonUtility.validateNumber(ProductHunterSolr.getItemPriceIdByItemId(rs.getInt("ITEM_ID"), subsetId, generalSubset)));
					salesOrderDetail.setItemId(rs.getInt("ITEM_ID"));
					salesOrderDetail.setPartNumber(rs.getString("PART_NUMBER"));
					salesOrderDetail.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
					salesOrderDetail.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
					salesOrderDetail.setShortDesc(rs.getString("SHORT_DESC"));
					salesOrderDetail.setOrderQty(rs.getInt("QTY"));
					salesOrderDetail.setOrderPrice(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(rs.getDouble("PRICE"))));
					salesOrderDetail.setOrderPriceStr(df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(rs.getDouble("PRICE")))));
					salesOrderDetail.setExtPrice(rs.getDouble("EXTPRICE"));
					salesOrderDetail.setOrderUom(rs.getString("UOM"));
					salesOrderDetail.setTotal(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(rs.getDouble("EXTPRICE"))));
					salesOrderDetail.setTotalStr(df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(rs.getDouble("EXTPRICE")))));
					salesOrderDetail.setTotalV2(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(rs.getDouble("EXT_PRICE"))));
					salesOrderDetail.setTotalStrV2(df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(rs.getDouble("EXT_PRICE")))));
					salesOrderDetail.setExtPrice(rs.getDouble("EXTPRICE"));
					salesOrderDetail.setUom(rs.getString("PER_UOM"));
					salesOrderDetail.setCatalogId(rs.getString("CATALOG_ID"));						
					salesOrderDetail.setImageType(rs.getString("IMAGE_TYPE"));
					salesOrderDetail.setHazardiousMaterial(rs.getString("HAZARDIOUS_MATERIAL"));
					salesOrderDetail.setStatus(CommonUtility.validateString(rs.getString("STATUS")));
					salesOrderDetail.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));  
					
					String imageName = "";
					if(rs.findColumn("LEAD_TIME")>0){
						salesOrderDetail.setLeadTime(rs.getString("LEAD_TIME"));
					}
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
					/*salesOrderTotal = salesOrderTotal + rs.getDouble("EXTPRICE");
						salesOrderTotalV2 = salesOrderTotalV2 + rs.getDouble("EXT_PRICE");*/

					if(rs.findColumn("UNITS_PER_STOCKING")>0){
						salesOrderDetail.setUnitsPerStocking(rs.getDouble("UNITS_PER_STOCKING"));
					}
					if(rs.findColumn("UNITS_PER_STOCKING_STRING")>0){
						salesOrderDetail.setUnitsPerStockingString(rs.getString("UNITS_PER_STOCKING_STRING"));
					}


					// Discount properties
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom")).length()>0){
						if(rs.getString("DISCOUNT_COUPON_CODE") != null && rs.getString("DISCOUNT_COUPON_CODE").trim().length() > 0 ){
							salesOrderDetail.setDiscount(rs.getDouble("DISCOUNT_AMOUNT"));
							salesOrderDetail.setDiscountCouponCode(rs.getString("DISCOUNT_COUPON_CODE"));
							salesOrderDetail.setDiscountType(DiscountType.valueOf(rs.getString("DISCOUNT_TYPE")));

							Discount ItemDiscount = new Discount();
							Coupon coupon = new Coupon();
							coupon.setCopounCode(salesOrderDetail.getDiscountCouponCode());
							ItemDiscount.setDiscountCoupon(coupon);
							ItemDiscount.setDiscountValue(salesOrderDetail.getDiscount());
							ItemDiscount.setDiscountType(salesOrderDetail.getDiscountType());
							appliedlDiscounts.add(ItemDiscount);

							orderItemsDiscountVal += salesOrderDetail.getDiscount();
						}
					}
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_NON_CATALOG_ITEM_IN_ORDER")).equalsIgnoreCase("Y")){
						if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID_FOR_ADDTOCART")).equalsIgnoreCase(CommonUtility.validateParseIntegerToString(rs.getInt("ITEM_ID")))){
							salesOrderTotal = salesOrderTotal + rs.getDouble("EXTPRICE");
							salesOrderItem.add(salesOrderDetail);
						}
					}else{
						salesOrderTotal = salesOrderTotal + rs.getDouble("EXTPRICE");
						salesOrderTotalV2 = salesOrderTotalV2 + rs.getDouble("EXT_PRICE");
						salesOrderItem.add(salesOrderDetail);
					}
					salesOrderDetail.setNetAmount(salesOrderDetail.getExtPrice() - salesOrderDetail.getDiscount());
				}
				
				LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = null;
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")).equalsIgnoreCase("Y") && !itemList.isEmpty()){
					customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "),"itemid");
					salesOrderDetailList.setCustomFieldVal(customFieldVal);
				}
				if(CommonUtility.customServiceUtility() != null) {
					CommonUtility.customServiceUtility().checkOversizeAndHazmatFreightRule(customFieldVal,salesOrderDetailList);//Electrozad Custom Service
				}
				
				
				if(salesOrderItem!=null && salesOrderItem.size()>0){
					String c = "";
					for(SalesModel sModel:salesOrderItem){
						idListForSolr = idListForSolr + c + sModel.getItemId();
						c = " OR ";
					}
					if (CommonUtility.validateString(idListForSolr).length()>0){
						LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = ProductHunterSolr.getcustomerPartnumber(idListForSolr, CommonUtility.validateNumber(buyingCompanyId), CommonUtility.validateNumber(buyingCompanyId));
						if(customerPartNumber!=null && customerPartNumber.size()>0){
							for(SalesModel item : salesOrderItem) {
								item.setCustomerPartNumberList(customerPartNumber.get(item.getItemId()));
							}
						}
					}
				}
				totalSavingOnOrder = orderItemsDiscountVal + orderDiscountVal;
				salesOrderDetailList.setOrderItemsDiscount(orderItemsDiscountVal);
				salesOrderDetailList.setTotalSavingsOnOrder(totalSavingOnOrder);
				salesOrderDetailList.setSubtotal(salesOrderTotal - (orderItemsDiscountVal));
				salesOrderDetailList.setSubtotalV2(salesOrderTotalV2 - (orderItemsDiscountVal));
				salesOrderDetailList.setTotal(salesOrderTotal+salesOrderDetailList.getFreight()+salesOrderDetailList.getTax() -(orderDiscountVal + orderItemsDiscountVal) + salesOrderDetailList.getHandling() + salesOrderDetailList.getDeliveryCharge());
				salesOrderDetailList.setTotalV2(salesOrderTotalV2+salesOrderDetailList.getFreight()+salesOrderDetailList.getTax()-(orderDiscountVal + orderItemsDiscountVal) + salesOrderDetailList.getHandling() + salesOrderDetailList.getDeliveryCharge());
				salesOrderDetailList.setSendmailToSalesRepOnly(otherDetail.getSendmailToSalesRepOnly());
				UsersModel userDetail = UsersDAO.getUserEmail(userId);
				if(type==1){
					SendMailUtility sendMailUtility = new SendMailUtility();
					sendMailModel = new SendMailModel();
					sendMailModel.setToEmailId(userDetail.getEmailAddress());
					sendMailModel.setFirstName(userDetail.getFirstName());
					sendMailModel.setLastName(userDetail.getLastName());
					if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("rfq.confirmation.subject")).length()>0){
						sendMailModel.setMailSubject(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("rfq.confirmation.subject")));
					}else{
						sendMailModel.setMailSubject("Request For Quote");
					}
					
					sendMailModel.setPunchoutUser(punchoutUser);
					if(emailAddress!=null){
						punchoutUser = 1;
						sendMailModel.setToEmailId(emailAddress);
						sendMailModel.setPunchoutUser(punchoutUser);
					}
					sent= sendMailUtility.sendRFQConfirmationMail(salesOrderDetailList, salesOrderItem, sendMailModel);
				}else{
					SendMailUtility sendMailUtility = new SendMailUtility();
					sendMailModel = new SendMailModel();
					sendMailModel.setToEmailId(userDetail.getEmailAddress());
					sendMailModel.setFirstName(userDetail.getFirstName());
					sendMailModel.setLastName(userDetail.getLastName());
					if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("order.confirmation.subject")).length()>0){
						sendMailModel.setMailSubject(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("order.confirmation.subject")));
					}else{
						sendMailModel.setMailSubject("Order Confirmation");
					}
					sendMailModel.setSendQuoteMail(otherDetail.getSendQuoteMail());
					if(CommonUtility.validateString(otherDetail.getSendQuoteMail()).equalsIgnoreCase("Y")){
						if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("quote.confirmation.subject")).length()>0){
							sendMailModel.setMailSubject(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("quote.confirmation.subject")));
						}else{
							sendMailModel.setMailSubject("Quote Confirmation");
						}
					}
					sent = sendMailUtility.sendOrderConfirmationMail(salesOrderDetailList, salesOrderItem, sendMailModel, creditCardValue,appliedlDiscounts);
				}
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);
		}
		if(sent){
			return 1;
		}else{
			return 0;
		}
	}

	public static SalesModel getOrderDetailByID(SalesModel salesModel) {
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt=null;
		SalesModel orderDetail = null;
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

		try {
			conn = ConnectionManager.getDBConnection();
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GETORDER_DETAIL_BYNUMBER")).length()>0){
				pstmt=conn.prepareStatement(PropertyAction.SqlContainer.get("getOrderDetailByNumber"));
				pstmt.setString(1, salesModel.getOrderNum());
			}else {
				pstmt=conn.prepareStatement(PropertyAction.SqlContainer.get("getOrderDetailById"));
				pstmt.setString(1, salesModel.getOrderID());
			}
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				orderDetail = new SalesModel();
				String fName = rs.getString("FIRST_NAME");
				String lName = rs.getString("LAST_NAME");
				String orderedBy = (fName==null?" - ":fName)+" "+(lName==null?"":lName);
				orderDetail.setOrderedBy(orderedBy);
				orderDetail.setOrderId(rs.getInt("ORDER_ID"));
				orderDetail.setOrderNum(rs.getString("ORDER_NUMBER"));
				orderDetail.setOrderStatus(rs.getString("ORDER_STATUS")==null?" - ":rs.getString("ORDER_STATUS"));
				orderDetail.setOrderDate(df.format(rs.getDate("ORDER_DATE")));
				orderDetail.setReqDate(rs.getString("REQUIRED_BY_DATE"));
				orderDetail.setShipDate(rs.getString("SHIP_DATE"));
				orderDetail.setExternalSystemId(rs.getString("EXTERNAL_SYSTEM_ID"));
				orderDetail.setBillAddress1(rs.getString("BILL_ADDRESS1"));
				orderDetail.setBillAddress2(rs.getString("BILL_ADDRESS2"));
				orderDetail.setBillCity(rs.getString("BILL_CITY"));
				orderDetail.setBillCountry(rs.getString("BILL_COUNTRY_CODE"));
				orderDetail.setBillPhone(rs.getString("BILL_PHONE"));
				orderDetail.setBillState(rs.getString("BILL_STATE"));
				orderDetail.setBillZipCode(rs.getString("BILL_ZIP_CODE"));
				orderDetail.setShipAddress1(rs.getString("SHIP_ADDRESS1"));
				orderDetail.setShipAddress2(rs.getString("SHIP_ADDRESS2"));
				orderDetail.setShipCity(rs.getString("SHIP_CITY"));
				orderDetail.setShipState(rs.getString("SHIP_STATE"));
				orderDetail.setShipPhone(rs.getString("SHIP_PHONE"));
				orderDetail.setShipZipCode(rs.getString("SHIP_ZIP_CODE"));
				orderDetail.setShipCountry(rs.getString("SHIP_COUNTRY_CODE"));
				orderDetail.setFreight(rs.getDouble("FREIGHT"));
				orderDetail.setHandling(rs.getDouble("HANDLING_FEE"));
				orderDetail.setDiscount(rs.getDouble("CASHDISCOUNT_AMOUNT"));
				orderDetail.setTax(rs.getDouble("TAX_AMOUNT"));
				orderDetail.setExternalSysError(rs.getString("EXTERNAL_SYSTEM_ERROR"));
				orderDetail.setPaymentRefrenceId(rs.getString("PAYMENT_REF_ID"));
				orderDetail.setCreditCardNumber(rs.getString("CC_NUMBER"));
				orderDetail.setCreditCardExpDate(rs.getString("CC_EXPIRE_DATE"));
				orderDetail.setCreditCardHolderName(rs.getString("CC_CARD_HOLDER"));
				orderDetail.setUserNote(rs.getString("USER_NOTE"));
				orderDetail.setPaymentDate(rs.getString("PAYMENT_DATE"));
				orderDetail.setPaymentMethod(rs.getString("PAYMENT_METHOD"));
				orderDetail.setTransactionId(rs.getString("TRANSACTION_ID"));
				orderDetail.setPaymentAuthCode(rs.getString("PAYMENT_AUTH_CODE"));
				orderDetail.setRefrenceKey(rs.getString("REFERENCE_KEY"));
				orderDetail.setShipMethod(rs.getString("SHIP_METHOD"));
				orderDetail.setShipMethodId(rs.getString("SHIP_METHOD_ID"));
				orderDetail.setFederalExciseTax(rs.getDouble("FEDERALEXCISETAX"));
				orderDetail.setPoNumber(rs.getString("PURCHASE_ORDER_NUMBER"));
				orderDetail.setOrderNotes(rs.getString("ORDER_NOTES"));
				orderDetail.setUserName(rs.getString("USER_NAME"));
				orderDetail.setBillEmailAddress(rs.getString("BILL_EMAIL_ADDRESS"));
				orderDetail.setShipEmailAddress(rs.getString("SHIP_EMAIL_ADDRESS"));
				orderDetail.setCarrierTrackingNumber(rs.getString("ORDER_TRACKING_NUMBER"));
				orderDetail.setCustomerName(rs.getString("SHIP_ADDRESSEE"));
				if(CommonUtility.validateString(rs.getString("DISCOUNT_COUPON_CODE")).length()>0){
					orderDetail.setDiscountCouponCode(rs.getString("DISCOUNT_COUPON_CODE"));
					orderDetail.setDiscount(rs.getDouble("DISCOUNT_AMOUNT"));
					orderDetail.setDiscountType(DiscountType.valueOf(rs.getString("DISCOUNT_TYPE")));
				}
				orderDetail.setSubtotal(rs.getDouble("SUBTOTAL_AMOUNT"));
				orderDetail.setTotal(rs.getDouble("TOTAL_AMOUNT"));
				if(orderDetail.getTotal()<=orderDetail.getSubtotal()) {
					orderDetail.setTotal(orderDetail.getSubtotal()+orderDetail.getFreight());
				}
				if(orderDetail.getDiscount()>0) {
					orderDetail.setTotal(orderDetail.getTotal()-orderDetail.getDiscount());
				}
				orderDetail.setShippingOrgType(CommonUtility.validateString(salesModel.getShippingOrgType()));
				orderDetail.setCompanyName(CommonUtility.validateString(salesModel.getCompanyName()));
			}

		} catch (Exception e) {         
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);	
		} 
		return orderDetail;
	}

	public static ArrayList<SalesModel> getOrderItemDetailByID(SalesModel salesModel) {
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt=null;
		ArrayList<SalesModel> orderItemList = null;
		try {
			conn = ConnectionManager.getDBConnection();

			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom")).length()>0){
				pstmt= conn.prepareStatement(PropertyAction.SqlContainer.get(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom"))));
				//pstmt.setInt(1,CommonDBQuery.getGlobalSiteId());
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GETORDER_DETAIL_BYNUMBER")).length()>0){
					pstmt.setString(1, salesModel.getOrderNum());
				}else {
				pstmt.setString(2, salesModel.getOrderID());
				}
			}else{
				pstmt= conn.prepareStatement(PropertyAction.SqlContainer.get("getOrderItemDetail"));
				pstmt.setString(1, salesModel.getOrderID());
			}
			rs=pstmt.executeQuery();
			orderItemList = new ArrayList<SalesModel>();
			while(rs.next()){
				SalesModel itemDetail = new SalesModel();
				itemDetail.setItemId(rs.getInt("ITEM_ID"));
				itemDetail.setPartNumber(rs.getString("PART_NUMBER"));
				itemDetail.setDescription(rs.getString("SHORT_DESC"));
				itemDetail.setQtyordered(rs.getInt("QTY"));
				itemDetail.setUnitPrice(rs.getDouble("PRICE"));
				itemDetail.setUom(rs.getString("UOM"));
				itemDetail.setExtPrice(rs.getDouble("EXTPRICE"));
				itemDetail.setImageName(rs.getString("IMAGE_NAME"));
				itemDetail.setImageType(rs.getString("IMAGE_TYPE"));
				itemDetail.setPageTitle(rs.getString("PAGE_TITLE"));
				if(CommonUtility.validateString(rs.getString("DISCOUNT_COUPON_CODE")).length()>0){
					itemDetail.setDiscountCouponCode(rs.getString("DISCOUNT_COUPON_CODE"));
					itemDetail.setDiscount(rs.getDouble("DISCOUNT_AMOUNT"));
					if(CommonUtility.validateString(rs.getString("DISCOUNT_TYPE")).length()>0) {
						itemDetail.setDiscountType(DiscountType.valueOf(rs.getString("DISCOUNT_TYPE")));
					}
				}
				itemDetail.setNetAmount(itemDetail.getExtPrice() - itemDetail.getDiscount());
				itemDetail.setOrderItemId(rs.getInt("ORDER_ITEM_ID"));
				orderItemList.add(itemDetail);
			}

		} catch (Exception e) {         
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		} 
		return orderItemList;
	}

	public static int saveQuoteCart(SalesModel quoteCart,String sessionId)
	{

		int cartId = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("createQuoteCart");

			cartId = CommonDBQuery.getSequenceId("QUOTE_CART_ID_SEQ");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, quoteCart.getPartNumber());
			pstmt.setString(2, quoteCart.getManufacturerPartNumber());
			pstmt.setString(3, quoteCart.getUpc());
			pstmt.setString(4, quoteCart.getShortDesc());
			pstmt.setString(5, quoteCart.getDescription());
			pstmt.setDouble(6, quoteCart.getPrice());
			pstmt.setDouble(7, quoteCart.getTotal());
			pstmt.setString(8, sessionId);
			pstmt.setInt(9, quoteCart.getItemId());
			pstmt.setString(10, quoteCart.getUnspc());
			pstmt.setString(11, quoteCart.getMaterialGroup());
			pstmt.setInt(12, quoteCart.getOrderQty());
			pstmt.setString(13, quoteCart.getUom());
			pstmt.setInt(14, cartId);
			pstmt.setInt(15, CommonDBQuery.getGlobalSiteId());
			pstmt.setString(16, quoteCart.getLineItemComment());
			pstmt.setString(17, quoteCart.getCatalogId());
			pstmt.setString(18, quoteCart.getGetPriceFrom());
			pstmt.executeUpdate();			
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
		return cartId;

	}

	public static SalesModel getQuoteItemDetail(String partNumber,int subsetId ,int generalSubsetId)
	{
		SalesModel cartListVal = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		try{
			conn = ConnectionManager.getDBConnection();
			//String sql = "SELECT PART_NUMBER,MANUFACTURER_PART_NUMBER,SHORT_DESC,ITEM_ID,UNSPSC,MATERIAL_GROUP FROM ITEM_DETAILS_MV WHERE UPPER(PART_NUMBER) = ? AND SUBSET_ID = ?";
			String sql = PropertyAction.SqlContainer.get("getQuoteItemDetail");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, partNumber.toUpperCase());
			pstmt.setInt(2, subsetId);
			pstmt.setString(3, partNumber.toUpperCase());
			pstmt.setInt(4, generalSubsetId);
			pstmt.setInt(5, subsetId);
			rs = pstmt.executeQuery();		
			if(rs.next()){
				cartListVal = new SalesModel();
				cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
				cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
				cartListVal.setItemId(rs.getInt("ITEM_ID"));
				cartListVal.setUnspc(rs.getString("UNSPSC"));
				cartListVal.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
				if(rs.findColumn("CATALOG_ID")>0){
					cartListVal.setCatalogId(rs.getString("CATALOG_ID"));
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
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);	
		}
		return cartListVal;
	}

	public static void deleteQuoteCart(String sessionId)
	{
		PreparedStatement pstmt = null;
		Connection conn = null;

		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql = "DELETE FROM QUOTE_CART WHERE SESSION_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sessionId);
			int count = pstmt.executeUpdate();
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
	}
	public static boolean updateSaveOrderItems(int orderId,int itemId,int qty,Connection conn)
	{
		boolean flag = false;

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		int count = 0;
		try
		{
			String sql = "SELECT ORDER_ITEM_ID FROM ORDER_ITEMS WHERE ORDER_ID=? AND ITEM_ID=?";
			pstmt = conn.prepareStatement(sql);
			//ORDER_ID,ITEM_ID,PART_NUMBER,CUSTOMER_PART_NUMBER,SHORT_DESC,QTY,PRICE,USER_NOTE,ORDER_ITEM_STATUS
			//ORDER_ID,ITEM_ID,PART_NUMBER,CUSTOMER_PART_NUMBER,SHORT_DESC,QTY,PRICE,USER_NOTE,INVOICE_DESC,UOM,PACK_DESC,ORDER_ITEM_STATUS,UPDATED_DATETIME	
			pstmt.setInt(1, orderId);
			pstmt.setInt(2, itemId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				sql = "UPDATE ORDER_ITEMS SET QTY = QTY + "+qty+" WHERE ORDER_ITEM_ID=?";
				pstmt1 = conn.prepareStatement(sql);
				pstmt1.setInt(1, rs.getInt("ORDER_ITEM_ID"));
				count = pstmt1.executeUpdate();
				if(count>0)
					flag = true;
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
			ConnectionManager.closeDBPreparedStatement(pstmt1);	
		}

		return flag;

	}
	public static int saveNewProductItem(Connection conn,int nprId,String manfPartNum,String manfName,String itemDesc,int qty)
	{
		int count = 0;
		PreparedStatement pstmt = null;
		int newProductId = CommonDBQuery.getSequenceId("NEW_PRODUCT_REQUEST_ITEMS_SEQ");
		try
		{
			String sql = "INSERT INTO NEW_PRODUCT_REQUEST_ITEMS(NPR_ID,MANUFACTURER_PART_NUMBER,MANUFACTURER_NAME,ITEM_DESC,NPR_ITEM_ID,QTY)VALUES(?,?,?,?,?,?)";
			//NPR_ID,MANUFACTURER_PART_NUMBER,MANUFACTURER_NAME,ITEM_DESC
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nprId);
			pstmt.setString(2, manfPartNum);
			pstmt.setString(3, manfName);
			pstmt.setString(4, itemDesc);
			pstmt.setInt(5, newProductId);
			pstmt.setInt(6, qty);
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
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}
		return newProductId;
	}
	public static ProductsModel getNPRItemDetail(Connection conn, String partNumber,int subsetId, String fieldType)
	{
		ProductsModel cartListVal = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			
			if(CommonUtility.validateString(fieldType).length()>0){
				fieldType = CommonUtility.validateString(fieldType);
			}else{
				fieldType = "manufacturer_part_number";
			}
			String sql = "SELECT PART_NUMBER,MANUFACTURER_PART_NUMBER,SHORT_DESC,ITEM_ID,UNSPSC,MATERIAL_GROUP,NET_PRICE FROM ITEM_DETAILS_MV WHERE regexp_replace("+fieldType+", '[^A-Za-z0-9]', '') = ?";


			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, partNumber);


			rs = pstmt.executeQuery();		
			if(rs.next())
			{
				cartListVal = new ProductsModel();
				cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
				cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
				cartListVal.setItemId(rs.getInt("ITEM_ID"));
				cartListVal.setUnspc(rs.getString("UNSPSC"));
				cartListVal.setPrice(rs.getDouble("NET_PRICE"));
				cartListVal.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
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
		}

		return cartListVal;
	}
	public static int updateNewProductItem(Connection conn,int nprId,String productId)
	{
		int count = 0;
		PreparedStatement pstmt = null;

		try
		{
			String sql = PropertyAction.SqlContainer.get("updateNewProductItem");
			//NPR_ID,MANUFACTURER_PART_NUMBER,MANUFACTURER_NAME,ITEM_DESC
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productId);
			pstmt.setInt(2, nprId);
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
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}
		return count;
	}
	public static void clearRfqCart(int userId)
	{
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = ConnectionManager.getDBConnection();
			String sql = "DELETE FROM RFQ_CART WHERE USER_ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			int count = pstmt.executeUpdate();

		} catch (SQLException e) { 
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
	}
	public static ArrayList<ProductsModel> getOrderDetails(int savedGroupId,int userId,int subsetId,int generalSubset,HttpSession session,String fromFunction) {
		String sql="";
		ArrayList<ProductsModel> cartListData = new ArrayList<ProductsModel>();
		PreparedStatement pstmt = null;
		ArrayList<ProductsModel> cartItems = new ArrayList<ProductsModel>();
		Connection conn = null;
		ResultSet rs = null;
		double total = 0;
		String c = "";
		String idList = "";
		String itemShip="";
		String delimeter="";
		SendMailModel sendMailModel = null;
		String itemShipDesc="";
		String delimeterDesc="";
		String auUser = (String)session.getAttribute("auUserLogin");
		String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
		ArrayList<String> partList = new ArrayList<String>();
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = null;
		int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));

		String cartSortByValue = "";
		String cartSortColumn = "";
		try {
			if(session.getAttribute("cartSortByValue")!=null){
				cartSortByValue = CommonUtility.validateString(session.getAttribute("cartSortByValue").toString());
				String[] sortCoumnArray = cartSortByValue.split(" ");
				if(sortCoumnArray!=null && sortCoumnArray.length>0){
					cartSortColumn = CommonUtility.validateString(sortCoumnArray[0]);
				}
			}
			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT")).length() > 0 && CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT").trim().equalsIgnoreCase("Y")) {
				sql=PropertyAction.SqlContainer.get("getPartialCartItemDetailQuery");
			}else {
			    sql=PropertyAction.SqlContainer.get("getCartItemDetailQuery");
			}
			if(CommonUtility.validateString(cartSortColumn).length()>0 && sql.toUpperCase().contains(cartSortColumn.toUpperCase())){
				sql = "SELECT * FROM ("+sql+") ORDER BY "+cartSortByValue;
			}
			int siteId = 0;
			if(CommonDBQuery.getGlobalSiteId()>0){
				siteId = CommonDBQuery.getGlobalSiteId();
			}	
			conn = ConnectionManager.getDBConnection();
			if(savedGroupId==0){
				pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, siteId);
                pstmt.setInt(2, userId);
                pstmt.setInt(3, subsetId);
                pstmt.setInt(4, siteId);
                pstmt.setInt(5, userId);
                pstmt.setInt(6, generalSubset);
                pstmt.setInt(7, siteId);
                pstmt.setInt(8, userId);
                pstmt.setInt(9, subsetId);
                rs = pstmt.executeQuery();
			}else if(CommonUtility.validateString(auUser).equalsIgnoreCase("Y")){
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT")).length() > 0 && CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT").trim().equalsIgnoreCase("Y")) {
					sql=PropertyAction.SqlContainer.get("getPartialCartItemDetailQueryBySession");
				}else {
					sql = PropertyAction.SqlContainer.get("getCartItemDetailQueryBySession");
				}
				if(CommonUtility.validateString(cartSortColumn).length()>0 && sql.toUpperCase().contains(cartSortColumn.toUpperCase())){
					sql = "SELECT * FROM ("+sql+") ORDER BY "+cartSortByValue;
				}
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, siteId);
				pstmt.setInt(2, userId);
				pstmt.setString(3, session.getId());
				pstmt.setInt(4, subsetId);
				pstmt.setInt(5, activeTaxonomyId);
				pstmt.setInt(6, siteId);
				pstmt.setInt(7, userId);
				pstmt.setString(8, session.getId());
				pstmt.setInt(9, generalSubset);
				pstmt.setInt(10, activeTaxonomyId);
				pstmt.setInt(11, siteId);
				pstmt.setInt(12, userId);
				pstmt.setString(13, session.getId());
				pstmt.setInt(14, subsetId);
				rs = pstmt.executeQuery();

			}else{
				sql = PropertyAction.SqlContainer.get("selectGroupItem");
				if(CommonUtility.validateString(cartSortColumn).length()>0 && sql.toUpperCase().contains(cartSortColumn.toUpperCase())){
					sql = "SELECT * FROM ("+sql+") ORDER BY "+cartSortByValue;
				}
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, savedGroupId);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, buyingCompanyId);
				pstmt.setInt(4, subsetId);
				pstmt.setInt(5, activeTaxonomyId);
				pstmt.setInt(6, savedGroupId);
				pstmt.setInt(7, userId);
				pstmt.setInt(8, buyingCompanyId);
				pstmt.setInt(9, generalSubset);
				pstmt.setInt(10, activeTaxonomyId);
				pstmt.setInt(11, savedGroupId);
				pstmt.setInt(12, subsetId);
				rs = pstmt.executeQuery();
			}
			ArrayList<Integer> itemList = new ArrayList<Integer>();
			System.out.println(rs.getStatement().toString());
			while(rs.next()){
				ProductsModel cartListVal = new ProductsModel();
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
				int packageQty = 1;
				itemUrl = itemUrl.replaceAll(" ","-");
				cartListVal.setItemUrl(itemUrl);
				cartListVal.setProductListId(rs.getInt("CART_ID"));
				cartListVal.setItemId(rs.getInt("ITEM_ID"));
				cartListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				cartListVal.setItemSubset(rs.getString("SUBSET_ID"));
				cartListVal.setInvoiceDesc(rs.getString("INVOICE_DESC"));
				cartListVal.setImageType(rs.getString("IMAGE_TYPE"));
				cartListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				cartListVal.setUnspc(rs.getString("UNSPSC"));
				cartListVal.setPackDesc(rs.getString("PACK_DESC"));
				cartListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				cartListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				cartListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
				cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				cartListVal.setPageTitle(rs.getString("PAGE_TITLE"));
				cartListVal.setUom(rs.getString("SALES_UOM"));
				cartListVal.setSalesUom(rs.getString("SALES_UOM"));
				cartListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
				cartListVal.setMultipleShipVia(rs.getString("ITEM_LEVEL_SHIPVIA"));
				cartListVal.setMultipleShipViaDesc(rs.getString("ITEM_LEVEL_SHIPVIA_DESC"));
				cartListVal.setItemLevelRequiredByDate(rs.getString("ITEMLEVEL_REQUIREDBYDATE"));
				cartListVal.setCatalogId(rs.getString("CATALOG_ID"));
				cartListVal.setWeight(rs.getDouble("WEIGHT"));
				cartListVal.setHeight(rs.getDouble("HEIGHT"));
				cartListVal.setWidth(rs.getDouble("WIDTH"));
				cartListVal.setLength(rs.getDouble("LENGTH"));
				if(rs.findColumn("GET_PRICE_FROM")>0 && CommonUtility.validateString(rs.getString("GET_PRICE_FROM")).equalsIgnoreCase("CART")){
					cartListVal.setPartNumber(rs.getString("NC_PART_NUMBER"));
					cartListVal.setQty(rs.getInt("QTY"));
					cartListVal.setPrice(rs.getDouble("PRICE")*rs.getInt("QTY"));
					cartListVal.setUnitPrice(rs.getDouble("PRICE"));
					//cartListVal.setNetPrice(rs.getDouble("PRICE")*rs.getInt("QTY"));
					cartListVal.setTotal(rs.getDouble("PRICE")*rs.getInt("QTY"));
					cartListVal.setNetPrice(rs.getDouble("PRICE")*rs.getInt("QTY"));
					cartListVal.setShortDesc(rs.getString("SHORT_DESCRIPTION"));
					partIdentifierQuantity.add(rs.getInt("QTY"));
					cartListVal.setManufacturerName(rs.getString("MANUFACTURER"));
					cartListVal.setBrandName(rs.getString("BRAND"));
					cartListVal.setOverRidePriceRule(rs.getString("OVERRIDE_PRICE_RULE"));
					partList.add(rs.getString("NC_PART_NUMBER"));
					ProductsModel cartItem = new ProductsModel();
					cartItem.setErpPartNumber(rs.getString("NC_PART_NUMBER"));
					if(CommonUtility.customServiceUtility()!=null){
						CommonUtility.customServiceUtility().configCartListValues(cartListVal, rs);
						CommonUtility.customServiceUtility().configOrderItemValues(cartListVal, rs);
					}
					cartItems.add(cartItem);
				}else{
					cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
					cartListVal.setQty(rs.getInt("QTY"));
					cartListVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
					cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
					cartListVal.setOverRidePriceRule(rs.getString("OVERRIDE_PRICE_RULE"));
					ProductsModel cartItem = new ProductsModel();
					cartItem.setErpPartNumber(rs.getString("PART_NUMBER"));
					cartItems.add(cartItem);
					partList.add(rs.getString("PART_NUMBER"));
					partIdentifierQuantity.add(rs.getInt("QTY"));
					cartListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
					cartListVal.setBrandName(rs.getString("BRAND_NAME"));
					// "SavePriceInShoppingCart" below line to get price stored in cart and revoke Price call from erp for Shopping cart - note: Availability cannot be pulled on Cart if enabled 
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInShoppingCart")).equalsIgnoreCase("Y")) {
						cartListVal.setPrice(rs.getDouble("PRICE"));
						cartListVal.setUnitPrice(rs.getDouble("PRICE"));
						cartListVal.setTotal(rs.getDouble("PRICE")*rs.getInt("QTY"));
						cartListVal.setNetPrice(rs.getDouble("PRICE")*rs.getInt("QTY"));
					}else {
						cartListVal.setPrice(rs.getDouble("NET_PRICE"));
						cartListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
						cartListVal.setNetPrice(rs.getDouble("EXTPRICE"));
						cartListVal.setTotal(rs.getDouble("EXTPRICE"));
					}
				}
				if(rs.getInt("SALES_QTY")==0){
					cartListVal.setSaleQty(1);
				}else{
					cartListVal.setSaleQty(rs.getInt("SALES_QTY"));
				}
				cartListVal.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
				cartListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
				if(rs.getInt("PACKAGE_QTY")>0){
					packageQty = rs.getInt("PACKAGE_QTY");
				}
				cartListVal.setPackageFlag(rs.getInt("PACKAGE_FLAG"));
				cartListVal.setPackageQty(packageQty);

				if(rs.findColumn("GET_PRICE_FROM")>0 && CommonUtility.validateString(rs.getString("GET_PRICE_FROM")).equalsIgnoreCase("CART")){
					total = total + cartListVal.getTotal();
				}else{
					// "SavePriceInShoppingCart" below line to get price stored in cart and revoke Price call from erp for Shopping cart - note: Availability cannot be pulled on Cart if enabled 
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInShoppingCart")).equalsIgnoreCase("Y")) {
						total = total + cartListVal.getTotal();
					}else {
						total = total + rs.getDouble("EXTPRICE");
						idList = idList + c + rs.getInt("ITEM_ID");
						c = " OR ";
					}
				}
				itemList.add(rs.getInt("ITEM_ID"));
				//cartListVal.setUom(rs.getString("UOM"));
				if(rs.findColumn("UOM")>0 && rs.getString("UOM")!=null){
					cartListVal.setUom(rs.getString("UOM"));
				}
				cartListVal.setCartTotal(total);
				if(CommonUtility.customServiceUtility() != null) {
					CommonUtility.customServiceUtility().mergeRequiredFields(cartListVal, rs);
				}
				if(rs.findColumn("ADDITIONAL_PROPERTIES")>0) {
					cartListVal.setAdditionalProperties(rs.getString("ADDITIONAL_PROPERTIES"));
				}
				cartListVal.setCategoryName(rs.getString("CATEGORY_NAME"));
				cartListData.add(cartListVal);

				if(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")!=null &&CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA").equalsIgnoreCase("Y")){
					itemShip =itemShip+delimeter+rs.getString("ITEM_LEVEL_SHIPVIA");
					delimeter=",";
					System.out.println("itemLevelShip:"+itemShip);
					session.setAttribute("itemleveShip",itemShip);
					itemShipDesc =itemShipDesc+delimeterDesc+rs.getString("ITEM_LEVEL_SHIPVIA_DESC");
					delimeterDesc=",";
					System.out.println("itemShipDesc:"+itemShipDesc);
					session.setAttribute("itemShipDesc",itemShipDesc);
				}
			}
			boolean sendMailFlag= false;
			ArrayList<ProductsModel> productRestrcitionItems = new ArrayList<ProductsModel>();
			if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
				if(itemList!=null && itemList.size()>0){
					customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "),"itemid");
					if(customFieldVal!=null && customFieldVal.size()>0){
						for(ProductsModel cartItem:cartListData){
							cartItem.setCustomFieldVal(customFieldVal.get(cartItem.getItemId()));
							for(java.util.Map.Entry<Integer, LinkedHashMap<String, Object>> entry : customFieldVal.entrySet()){
								if(entry!=null && entry.getValue()!=null && entry.getKey() == cartItem.getItemId() && (entry.getValue().toString().contains("custom_OverSize=Y") || entry.getValue().toString().contains("custom_Hazmat=Y") || entry.getValue().toString().contains("custom_Fragile=Y"))){
									productRestrcitionItems.add(cartItem);
									break;
								}
							}
						}
					}
				}
			}
			if(partList!=null && partList.size()>0 && cartListData!=null && cartListData.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
			{
				ProductManagement priceInquiry = new ProductManagementImpl();
				ProductManagementModel priceInquiryInput = new ProductManagementModel();
				String entityId = (String) session.getAttribute("entityId");
				priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
				priceInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
				priceInquiryInput.setPartIdentifier(cartListData);
				priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
				priceInquiryInput.setRequiredAvailabilty("Y");
				priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
				priceInquiryInput.setSession(session);
				cartListData = priceInquiry.priceInquiry(priceInquiryInput , cartListData);
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_DISCOUNT_COUPONS_ENABLED")).equalsIgnoreCase("Y") && (!CommonUtility.validateString(fromFunction).equalsIgnoreCase("confirmOrder") && !CommonUtility.validateString(fromFunction).equalsIgnoreCase("auConfirmOrder"))){
				double totalUpdated = 0;
				for(ProductsModel cartItem: cartListData){
					Discount itemDiscount = SalesAction.getItemDiscount(session,cartItem.getProductListId(),cartItem.getPartNumber(),cartItem.getQty());
					if(itemDiscount!=null){
						totalUpdated = totalUpdated+(cartItem.getTotal()-itemDiscount.getDiscountValue());
						cartItem.setTotal(cartItem.getTotal()-itemDiscount.getDiscountValue());
						cartItem.setdDiscountValue(itemDiscount.getDiscountValue());
						cartItem.setCartTotal(totalUpdated);
						cartItem.setDiscountCouponCode(itemDiscount.getDiscountCoupon().getCopounCode());
					}
				}
				if(totalUpdated>0){
					cartListData.get(0).setCartTotal(totalUpdated);	
				}

			}
			LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = null;
			if(CommonUtility.validateString(idList).length()>0){
				customerPartNumber = ProductHunterSolr.getcustomerPartnumber(idList, buyingCompanyId, buyingCompanyId);
			}
			if(customerPartNumber!=null && customerPartNumber.size()>0){
				for(ProductsModel item : cartListData) {
					item.setCustomerPartNumberList(customerPartNumber.get(item.getItemId()));
				}
			}
			
			if(cartListData != null && cartListData.size() > 0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCSPConfigurator")).equalsIgnoreCase("Y")) {
				LogoCustomization logoCustomization =new LogoCustomization();
				buyingCompanyId = CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString());
				cartListData = logoCustomization.setDesignFeesItems(cartListData, userId, buyingCompanyId, session);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return cartListData;
	}
	public static ArrayList<ProductsModel> getOrderDetailsQuote(HttpSession session) {
		String sql="";
		ArrayList<ProductsModel> cartListData = new ArrayList<ProductsModel>();
		PreparedStatement pstmt = null;
		//ArrayList<ProductsModel> cartItems = new ArrayList<ProductsModel>();
		String tempSubset = (String) session.getAttribute("userSubsetId");
		int subsetId = CommonUtility.validateNumber(tempSubset);
		String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
		if(ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"))>0){
			generalSubset = ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"));
		}
		Connection conn = null;
		ResultSet rs = null;
		double total = 0;
		ArrayList<String> partList = new ArrayList<String>();
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		String itemShip="";
		String delimeter="";
		String itemShipDesc="";
		String delimeterDesc="";
		//sql=PropertyAction.SqlContainer.get("getCartItemDetailQueryQuote");
		try {
			conn = ConnectionManager.getDBConnection();
			/*pstmt = conn.prepareStatement(sql);
    	pstmt.setString(1, session.getId());*/

			String cartSortByValue = "";
			String cartSortColumn = "";
			if(session.getAttribute("cartSortByValue")!=null){
				cartSortByValue = CommonUtility.validateString(session.getAttribute("cartSortByValue").toString());
				String[] sortCoumnArray = cartSortByValue.split(" ");
				if(sortCoumnArray!=null && sortCoumnArray.length>0){
					cartSortColumn = CommonUtility.validateString(sortCoumnArray[0]);
				}
			}


			sql = PropertyAction.SqlContainer.get("getQuoteCartItemDetailQuery");
			if(CommonUtility.validateString(cartSortColumn).length()>0 && sql.toUpperCase().contains(cartSortColumn.toUpperCase())){
				sql = "SELECT * FROM ("+sql+") ORDER BY "+cartSortByValue;
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, session.getId());
			pstmt.setInt(2, subsetId);
			pstmt.setInt(3, activeTaxonomyId);
			pstmt.setString(4, session.getId());
			pstmt.setInt(5, generalSubset);
			pstmt.setInt(6, activeTaxonomyId);
			pstmt.setString(7, session.getId());
			pstmt.setInt(8, subsetId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ProductsModel cartListVal = new ProductsModel();
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
				int packageQty = 1;
				itemUrl = itemUrl.replaceAll(" ","-");
				cartListVal.setItemUrl(itemUrl);
				cartListVal.setProductListId(rs.getInt("QUOTE_CART_ID"));
				cartListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
				cartListVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
				cartListVal.setItemSubset(rs.getString("SUBSET_ID"));
				/*ProductsModel cartItem = new ProductsModel();
			cartItem.setErpPartNumber(rs.getString("PART_NUMBER"));*/
				cartListVal.setImageType(rs.getString("IMAGE_TYPE"));
				cartListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				cartListVal.setUnspc(rs.getString("UNSPSC"));
				cartListVal.setPackDesc(rs.getString("PACK_DESC"));
				cartListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				cartListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				cartListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
				cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				cartListVal.setUom(rs.getString("SALES_UOM"));
				cartListVal.setMultipleShipVia(rs.getString("ITEM_LEVEL_SHIPVIA"));
				cartListVal.setMultipleShipViaDesc(rs.getString("ITEM_LEVEL_SHIPVIA_DESC"));
				cartListVal.setItemLevelRequiredByDate(rs.getString("ITEMLEVEL_REQUIREDBYDATE"));
				cartListVal.setCatalogId(rs.getString("CATALOG_ID"));
				cartListVal.setWeight(rs.getDouble("WEIGHT"));
				cartListVal.setHeight(rs.getDouble("HEIGHT"));
			    cartListVal.setWidth(rs.getDouble("WIDTH"));
				cartListVal.setLength(rs.getDouble("LENGTH"));
				
				if(rs.findColumn("GET_PRICE_FROM")>0 && CommonUtility.validateString(rs.getString("GET_PRICE_FROM")).equalsIgnoreCase("CART")){
					cartListVal.setItemId(rs.getInt("ITEM_ID"));
					cartListVal.setQty(rs.getInt("QTY"));
					cartListVal.setPrice(rs.getDouble("PRICE"));
					cartListVal.setUnitPrice(rs.getDouble("PRICE"));
					cartListVal.setTotal(rs.getDouble("PRICE") * rs.getInt("QTY"));
					cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
					cartListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
					cartListVal.setBrandName(rs.getString("BRAND_NAME"));
					cartListVal.setGetPriceFrom(rs.getString("GET_PRICE_FROM"));
				}else{
					partList.add(rs.getString("PART_NUMBER"));
					partIdentifierQuantity.add(rs.getInt("QTY"));
					cartListVal.setItemId(rs.getInt("ITEM_ID"));
					cartListVal.setQty(rs.getInt("QTY"));
					cartListVal.setPrice(rs.getDouble("NET_PRICE"));
					cartListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
					cartListVal.setTotal(rs.getDouble("EXTPRICE"));
					cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
					cartListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
					cartListVal.setBrandName(rs.getString("BRAND_NAME"));
				}
				//cartItems.add(cartItem);
				if(rs.getInt("SALES_QTY")==0){
					cartListVal.setSaleQty(1);
				}else{
					cartListVal.setSaleQty(rs.getInt("SALES_QTY"));
				}
				cartListVal.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
				cartListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
				if(rs.getInt("PACKAGE_QTY")>0){
					packageQty = rs.getInt("PACKAGE_QTY");
				}
				cartListVal.setPackageFlag(rs.getInt("PACKAGE_FLAG"));
				cartListVal.setPackageQty(packageQty);
				if(rs.findColumn("GET_PRICE_FROM")>0 && CommonUtility.validateString(rs.getString("GET_PRICE_FROM")).equalsIgnoreCase("CART")){
					total = total + cartListVal.getTotal();
				}else{
					total = total + rs.getDouble("EXTPRICE");
				}
				
				if(rs.findColumn("UOM")>0 && rs.getString("UOM")!=null){
					cartListVal.setUom(rs.getString("UOM"));
				}
				
				if(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")!=null &&CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA").equalsIgnoreCase("Y")){
					itemShip =itemShip+delimeter+rs.getString("ITEM_LEVEL_SHIPVIA");
					delimeter=",";
					System.out.println("itemLevelShip:"+itemShip);
					session.setAttribute("itemleveShip",itemShip);
					itemShipDesc =itemShipDesc+delimeterDesc+rs.getString("ITEM_LEVEL_SHIPVIA_DESC");
					delimeterDesc=",";
					System.out.println("itemShipDesc:"+itemShipDesc);
					session.setAttribute("itemShipDesc",itemShipDesc);
				}
				cartListVal.setCartTotal(total);
				cartListData.add(cartListVal);
			}
			if(cartListData!=null && cartListData.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
			{
				ProductManagement priceInquiry = new ProductManagementImpl();
				ProductManagementModel priceInquiryInput = new ProductManagementModel();
				String entityId = (String) session.getAttribute("entityId");
				priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
				priceInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
				priceInquiryInput.setPartIdentifier(cartListData);
				priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
				priceInquiryInput.setRequiredAvailabilty("Y");
				priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
				priceInquiryInput.setSession(session);
				cartListData = priceInquiry.priceInquiry(priceInquiryInput , cartListData);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return cartListData;
	}
	public static ArrayList<CreditCardModel> getCreditCardDetails(int userId)
	{
		ArrayList<CreditCardModel> cardDetail = new ArrayList<CreditCardModel>();
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getCreditCardInfo");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				String expDate = rs.getDate("EXP_DATE").toString();
				String expDateArr[]=expDate.split("-");
				expDate = expDateArr[1]+"/"+expDateArr[2]+"/"+expDateArr[0];
				CreditCardModel cardDetailVal = new CreditCardModel();
				cardDetailVal.setPcardId(rs.getInt("USER_PCARD_ID"));
				cardDetailVal.setElementPaymentAccountId(rs.getString("ELEMENT_ACCOUNT_ID"));
				cardDetailVal.setCardHolder(rs.getString("CARD_HOLDER_NAME"));
				cardDetailVal.setCreditCardType(rs.getString("CARD_TYPE"));
				cardDetailVal.setDate(expDate);
				cardDetailVal.setAddress1(rs.getString("STREET_ADDRESS"));
				cardDetailVal.setZipCode(rs.getString("ZIP_CODE"));
				cardDetailVal.setCreditCardNumber("XXXX-XXXX-XXXX-"+rs.getString("CARDNUMBER").trim());
				cardDetailVal.setNickName(rs.getString("NICK_NAME"));
				cardDetailVal.setExpDate(expDate);
				cardDetailVal.setStatusDescription(rs.getString("EXPRESS_CHECKOUT_DEFAULT"));
				cardDetail.add(cardDetailVal);
			}
		}catch (SQLException e) { 
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);
		}
		return cardDetail;
	}
	public static int deleteCreditCardInfo(int ccId,int userId){
		Connection  conn = null;
		PreparedStatement pstmt=null;

		String sql = "";
		int count = 0;
		try {
			conn = ConnectionManager.getDBConnection();
			sql = PropertyAction.SqlContainer.get("deleteCCInfoQuery");

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ccId);
			pstmt.setInt(2, userId);


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
	public static int updateDefaultCreditCardInfo(UsersModel userDetail){
		Connection  conn = null;
		PreparedStatement pstmt=null;
		PreparedStatement pstmt1 = null;
		String sql = "";
		int count = 0;
		try {
			conn = ConnectionManager.getDBConnection();
			sql = "UPDATE USER_ELEMENT_P_CARD SET EXPRESS_CHECKOUT_DEFAULT = 'N' WHERE USER_ID =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userDetail.getUserId());
			count = pstmt.executeUpdate();
			sql = PropertyAction.SqlContainer.get("updatedefaultCreditCardId");
			pstmt1 = conn.prepareStatement(sql);
			pstmt1.setInt(1, userDetail.getUserId());
			pstmt1.setInt(2, userDetail.getPcardId());
			count = pstmt1.executeUpdate();

		} catch (SQLException e) { 
			e.printStackTrace();
		}
		finally {	    
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBPreparedStatement(pstmt1);	
			ConnectionManager.closeDBConnection(conn);	
		}
		return count;
	}
	public static void insertCreditCardDetail(int userId, CreditCardModel creditCardDetail,String transactionSetupId)
	{
		Connection  conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("insertCreditCard");
			String tempDate[] = creditCardDetail.getDate().split("/");
			String date = tempDate[2]+"/"+tempDate[0]+"/"+tempDate[1];
			String updateDate = "TO_DATE('"+date+"','yyyy/mm/dd')";
			sql = sql.replace("REPLACEEXPDATE", updateDate);
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, CommonDBQuery.getSequenceId("USER_ELEMENT_SEQ"));
			pstmt.setInt(2,userId);
			pstmt.setString(3,creditCardDetail.getElementPaymentAccountId());
			pstmt.setString(4,creditCardDetail.getCreditCardType());
			pstmt.setString(5,creditCardDetail.getCardHolder());
			pstmt.setString(6,creditCardDetail.getAddress1());
			pstmt.setString(7,creditCardDetail.getZipCode());
			pstmt.setString(8,creditCardDetail.getCreditCardNumber());
			pstmt.setString(9,transactionSetupId);
			if(creditCardDetail.getNickName()==null)
				pstmt.setString(10,"");
			else
				pstmt.setString(10,creditCardDetail.getNickName());
			int count = pstmt.executeUpdate();
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
	}
	public static UsersModel getDefaultCreditCardDetails(UsersModel userDetail)
	{
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnectionManager.getDBConnection();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		try {
			String sql = PropertyAction.SqlContainer.get("getdefaultCreditCardInfo");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userDetail.getUserId());
			pstmt.setInt(2, userDetail.getPcardId());
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				String expDate = rs.getDate("EXP_DATE").toString();
				String expDateArr[]=expDate.split("-");
				expDate = expDateArr[1]+"/"+expDateArr[2]+"/"+expDateArr[0];
				userDetail.setPcardId(rs.getInt("USER_PCARD_ID"));
				userDetail.setElementPaymentAccountId(rs.getString("ELEMENT_ACCOUNT_ID"));
				userDetail.setCardHolder(rs.getString("CARD_HOLDER_NAME"));
				userDetail.setCreditCardType(rs.getString("CARD_TYPE"));
				userDetail.setDate(expDate);
				userDetail.setAddress1(rs.getString("STREET_ADDRESS"));
				userDetail.setZipCode(rs.getString("ZIP_CODE"));
				userDetail.setCreditCardNumber("XXXX-XXXX-XXXX-"+rs.getString("CARDNUMBER").trim());
				userDetail.setNickName(rs.getString("NICK_NAME"));
				userDetail.setExpDate(expDate);

			}
		}
		catch (SQLException e) { 
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
		return userDetail;
	}
	public static int getDefaultCcId(UsersModel userDetail)
	{
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		int pCardId = 0;
		try {
			conn = ConnectionManager.getDBConnection();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		try {
			String sql = PropertyAction.SqlContainer.get("getdefaultCreditCardId");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userDetail.getUserId());
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				pCardId = rs.getInt("USER_PCARD_ID");
			}
		}
		catch (SQLException e) { 
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
		return pCardId;
	}
	public static String approveCartDao(HttpSession session, int savedGroupId, int orderId){
		String result = "";
		ResultSet rs = null;
		Connection  conn = null;
		String sql ="";
		PreparedStatement pstmt = null;
		String rejectReason = "";
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String updatedDate = (String) session.getAttribute("updatedDate");
		String senderID = (String) session.getAttribute("senderId");
		String savedGroupName = (String) session.getAttribute("groupName");
		int userId = CommonUtility.validateNumber(sessionUserId);
		UsersModel ApproverEmail = ProductsDAO.getUserEmail(CommonUtility.validateNumber(sessionUserId));
		int count = 0;
		try {
			conn = ConnectionManager.getDBConnection();
		} catch (SQLException e) { 
			e.printStackTrace();
		}	
		try
		{
			sql= PropertyAction.SqlContainer.get("updateApproveCartStatus");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, savedGroupId);
			pstmt.setInt(2, userId);
			count = pstmt.executeUpdate();
			if(count>0){
				ConnectionManager.closeDBPreparedStatement(pstmt);
				String sql1 = PropertyAction.SqlContainer.get("getEmailId");
				pstmt = conn.prepareStatement(sql1);
				pstmt.setString(1, senderID);
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					SendMailUtility mailObj = new SendMailUtility();
					mailObj.sendApprovalMail(savedGroupId,savedGroupName,rs.getString("EMAIL"),rs.getString("FIRST_NAME"),rs.getString("LAST_NAME"),updatedDate,"Approved",orderId,rejectReason,ApproverEmail.getEmailAddress());
					result = "1|Cart Approved";
				}

			}
			else{
				result="0|Cart Approval Failed. Please try after sometime";
			}

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
		return result;
	}
	public static int updateCouponUse(String buyingCompany, String couponCode, String discountAmount)
	{
		Connection conn = null;
		int counter = 0;
		PreparedStatement pstmt = null;
		int orderItemId = CommonDBQuery.getSequenceId("COUPON_USE_ID");
		String sql = PropertyAction.SqlContainer.get("updateCouponUse");
		try
		{
			conn = ConnectionManager.getDBConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, orderItemId);
			pstmt.setString(2, buyingCompany);
			pstmt.setString(3, couponCode);
			pstmt.setString(4, discountAmount);
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
	public static double getTotalCartWeight(ArrayList<ProductsModel>cartListData) {

		double frieghtWeight= 0;
		double weight=0;
		try{
		for(ProductsModel item : cartListData){
			String selectedShipMethod= item.getMultipleShipVia();
			if(item.getWeight()>0.0){
				weight = item.getWeight()*item.getQty();
			}else{
				weight = CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("DEFAULT_ITEM_WEIGHT"))*item.getQty();
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID")).length()>0) {
					if(item.getItemId()==CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID")))
						weight = 0;
				}
			}
			if(CommonUtility.customServiceUtility()!=null) {
				weight = CommonUtility.customServiceUtility().excludeItemWeight(selectedShipMethod,weight);
			}
			if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
				frieghtWeight = frieghtWeight + weight;
			}else{
					frieghtWeight = frieghtWeight + weight;
			}
		}
		}catch (Exception e){
			e.printStackTrace();
		}
		return frieghtWeight;
	}
	
	public static ProductsModel getTotalDimensions(ArrayList<ProductsModel>cartListData) {
		int frieghtWidth= 0;
		int width=0;
		int frieghtHeight= 0;
		int height=0;
		int frieghtLength= 0;
		int length=0;
		ProductsModel totalDimensions = new ProductsModel();
		try{
			for(ProductsModel item : cartListData){
				width = (int) item.getWidth();
				frieghtWidth = frieghtWidth + width;
				height = (int) item.getHeight() * item.getQty();
				frieghtHeight = frieghtHeight + height;
				 length = (int) item.getLength();
				 frieghtLength = frieghtLength + length;
			}
			totalDimensions.setWidth(frieghtWidth);
			totalDimensions.setHeight(frieghtHeight);
			totalDimensions.setLength(frieghtLength);
		}catch (Exception e){
			e.printStackTrace();
		}
		return totalDimensions;
	}
	public static int deleteAllCreditCardInfo(int userId){
		Connection  conn = null;
		PreparedStatement pstmt=null;

		String sql = "";
		int count = 0;
		try {
			conn = ConnectionManager.getDBConnection();
			sql = "DELETE FROM USER_ELEMENT_P_CARD WHERE USER_ID=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);

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


	public static int outStandingPaymentDAO(SalesModel orderModel){
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = ConnectionManager.getDBConnection();
		}catch (Exception e){
			e.printStackTrace();
		}
		try{
			String sql = PropertyAction.SqlContainer.get("insertIntoOutstandingPaymentTable");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orderModel.getOrderNum());
			pstmt.setDouble(2, orderModel.getTotal());
			pstmt.setString(3, orderModel.getCustomerNumber());
			pstmt.setString(4, CommonUtility.validateParseIntegerToString(orderModel.getStageCode()));
			count = pstmt.executeUpdate();
			if(count>0){
				System.out.println("outStandingPaymentDAO Insert Success : "+orderModel.getOrderNum());
			}else{
				System.out.println("outStandingPaymentDAO Insert Failed : "+orderModel.getOrderNum());
			}
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
		return count;
	}

	public static int outStandingPaymentDAOUpdate(String paymentTransactionId,String paymentAuthCode,String paymentRefrenceNo,String paymentApproveMessage,double approvedAmount,String erpOrderNumbers, String cardNumber){
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = ConnectionManager.getDBConnection();
		}catch (Exception e){
			e.printStackTrace();
		}
		try{
			//PAYMENT_STATUS=?,PAYMENT_TRANSACTION_ID=?,PAYMENT_AUTH_CODE=?,PAYMENT_REFRENCE_NUMBER=?,PAYMENT_APPROVAL_MESSAGE=?,APPROVED_AMOUNT=? WHERE ERP_ORDER_ID IN (?)
			String sql = PropertyAction.SqlContainer.get("updatetoOutstandingPaymentTable").replaceAll("#REPLACETEXT#", CommonUtility.validateString(erpOrderNumbers));
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(paymentTransactionId));
			pstmt.setString(2, CommonUtility.validateString(paymentAuthCode));
			pstmt.setString(3, CommonUtility.validateString(paymentRefrenceNo));
			pstmt.setString(4, CommonUtility.validateString(paymentApproveMessage));
			pstmt.setDouble(5, approvedAmount);
			pstmt.setString(6, cardNumber);
			count = pstmt.executeUpdate();
			if(count>0){
				System.out.println("outStandingPaymentDAOUpdate Success : "+erpOrderNumbers);
			}else{
				System.out.println("outStandingPaymentDAOUpdate Failed : "+erpOrderNumbers);
			}

		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
		return count;
	}
	public static LinkedHashMap<String, Object> CouponDataCrud(LinkedHashMap<String, Object> orderDetails){
		int count = 0;
		String sql = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<SalesModel> couponDetails = new ArrayList<SalesModel>();
		try{
			conn = ConnectionManager.getDBConnection();
			String crudValue = (String)orderDetails.get(SalesActionContantVariables.COUPONS_CRUD_KEY);
			if(CommonUtility.validateString(crudValue).equalsIgnoreCase(SalesActionContantVariables.COUPONS_CRUD_CREATE_VALUE)){
				//ORDER_ID,USER_ID,DISCOUNT_AMOUNT,DISCOUNT_PERCENTAGE,COUPON_CODE,DISCOUNT_TYPE
				sql = PropertyAction.SqlContainer.get("insertCounponData");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, (Integer)orderDetails.get(SalesActionContantVariables.ORDER_ID));
				pstmt.setInt(2, (Integer)orderDetails.get(SalesActionContantVariables.USER_ID));
				pstmt.setString(3, (String)orderDetails.get(SalesActionContantVariables.COUPONS_DISCOUNT_AMOUNT_KEY));
				pstmt.setString(4, (String)orderDetails.get(SalesActionContantVariables.COUPONS_DISCOUNT_PERCENTAGE_KEY));
				pstmt.setString(5, (String)orderDetails.get(SalesActionContantVariables.COUPONS_DISCOUNT_COUPONCODE_KEY));
				pstmt.setString(6, (String)orderDetails.get(SalesActionContantVariables.COUPONS_DISCOUNT_TYPE_KEY));
				count = pstmt.executeUpdate();
			}else if(CommonUtility.validateString(crudValue).equalsIgnoreCase(SalesActionContantVariables.COUPONS_CRUD_READ_VALUE)){
				//USER_ID=? AND ORDER_ID=?
				sql = PropertyAction.SqlContainer.get("readCouponData");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, (Integer)orderDetails.get(SalesActionContantVariables.USER_ID));
				pstmt.setInt(2, (Integer)orderDetails.get(SalesActionContantVariables.ORDER_ID));
				rs = pstmt.executeQuery();
				while(rs!=null && rs.next())
				{
					SalesModel eachDiscount = new SalesModel();
					eachDiscount.setDiscountCouponCode(rs.getString("COUPON_CODE"));
					eachDiscount.setDiscountType(DiscountType.valueOf(rs.getString("DISCOUNT_TYPE")));
					eachDiscount.setDiscount(rs.getDouble("DISCOUNT_AMOUNT"));
					couponDetails.add(eachDiscount);
				}
				if(couponDetails!=null && couponDetails.size()>0){
					orderDetails.put("couponDetails", couponDetails);
				}
			}

		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(rs!=null){
				ConnectionManager.closeDBResultSet(rs);
			}
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
		return orderDetails;
	}
	public static ArrayList<SalesModel> getShipToEntityOpenQuotes(int shipToBuyingCompanyId) {
		
		 ArrayList<SalesModel> openQuoteList = new ArrayList<SalesModel>();
		 ResultSet rs = null;
		    Connection  conn = null;
		    PreparedStatement pstmt=null;
			
			
			try {
		        conn = ConnectionManager.getDBConnection();
		    } catch (SQLException e) { 
		        e.printStackTrace();
		    }	
		    
		    try {
		    	String sql = PropertyAction.SqlContainer.get("getShipToEntityOpenQuotes");
	              pstmt=conn.prepareStatement(sql);
		          pstmt.setInt(1, shipToBuyingCompanyId);
		        
		          rs=pstmt.executeQuery();
		          while(rs.next())
		          {
		        	  SalesModel openQuoteVal = new SalesModel();
		        	  openQuoteVal.setOrderID(rs.getString("ERP_QUOTE_ID"));
		        	  openQuoteVal.setOrderStatus(rs.getString("STATUS"));
		        	  openQuoteVal.setOrderNotes(rs.getString("ORDER_NOTES"));
		        	  openQuoteList.add(openQuoteVal);
		        	 
		          }
		          
		    } catch (Exception e) {         
		          e.printStackTrace();

		      } finally {
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(pstmt);
		    	  ConnectionManager.closeDBConnection(conn);	
		      } 
		 
		 return openQuoteList;
	}
	
	public static ProductsModel getItemPriceIdOnPartNumber(ProductsModel pmodel,int subsetId,int generalSubset) {
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt=null;
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt=conn.prepareStatement(PropertyAction.SqlContainer.get("getItemPriceIdOnPartNumber"));
			pstmt.setString(1,pmodel.getPartNumber());
			pstmt.setInt(2,subsetId);
			pstmt.setInt(3,generalSubset);
			rs=pstmt.executeQuery();
			
			while(rs.next())
			{
				pmodel.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
			}

		} catch (Exception e) {         
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);	
		} 
		return pmodel;
	}
}