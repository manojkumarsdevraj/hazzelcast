package com.unilog.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.action.UserManagementAction;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPriceBreaks;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralWarehouse;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.ProductManagementModel;
import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.custommodule.model.FreightCalculatorModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.misc.EventModel;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

public class ElectrozadCustomServices implements UnilogFactoryInterface {
	
	private static UnilogFactoryInterface serviceProvider;
	private ElectrozadCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (ElectrozadCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new ElectrozadCustomServices();
				}
			}
		return serviceProvider;
	}    
	
	@Override
	public void warehouseLevelCatalog(UsersModel catalog, HashMap<String, String> userDetails, HttpSession session) {
		if(CommonDBQuery.getSystemParamtersList().get("ENABLE_WAREHOUSE_CATALOG")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_WAREHOUSE_CATALOG").trim().equalsIgnoreCase("Y")){
			catalog.setBuyingCompanyId(CommonUtility.validateNumber(userDetails.get("buyingCompanyId")));
			UsersModel userDetailList = new UsersModel();
			userDetailList.setEntityId(userDetails.get("contactId"));
			userDetailList = CommonUtility.customServiceUtility().getCustomer(userDetailList);
			session.setAttribute("currencyCode",userDetailList.getCurrencyCode());
			
			if((userDetailList.getHomeBranch().length()>0) && (userDetailList.getHomeBranch()!=null)){//checking warehouse level subset
				WarehouseModel warehouseDetails = UsersDAO.getWareHouseDetailsByCode(userDetailList.getHomeBranch());
				int subsetId = warehouseDetails.getSubsetId();
				if(subsetId>0){
					session.setAttribute("userSubsetId", Integer.toString(subsetId));
				}
				//UsersDAO.updateSubsetIdFromERP(CommonUtility.validateNumber(userDetails.get("buyingCompanyId")), subsetId);
				else {
					session.setAttribute("userSubsetId", CommonDBQuery.getSystemParamtersList().get("USERSUBSETID"));
				}
			}
			/*catalog = userObj.getCatalogfromERP(catalog);
			//&& session.getAttribute("userSubsetName")!=null && session.getAttribute("userSubsetName").toString().trim().equalsIgnoreCase(catalog.getCatalogName().trim())
			if(catalog!=null && catalog.getCatalogName()!=null){
				 if(userDetails.get("userSubsetId")!=null && String.valueOf(catalog.getSubsetId())!=null && !String.valueOf(catalog.getSubsetId()).trim().equalsIgnoreCase(userDetails.get("userSubsetId").trim())){
					 UsersDAO.updateSubsetIdFromERP(CommonUtility.validateNumber(userDetails.get("buyingCompanyId")), catalog.getSubsetId());
				 }
				 session.setAttribute("userSubsetId", String.valueOf(catalog.getSubsetId()));
	        }*/
		}
	}

	@Override	
	public UsersModel getCustomer(UsersModel userDetails) {
		UsersModel userDetailslList = new UsersModel();
		try {
			String GET_CUSTOMER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  userDetails.getEntityId();
			Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_CUSTOMER_URL, HttpMethod.GET, null, Cimm2BCentralCustomer.class);
			Cimm2BCentralCustomer customerDetails  = (Cimm2BCentralCustomer) customerDetailsResponseEntity.getData();
			userDetailslList =  Cimm2BCentralClient.getInstance().getCustomerInfo(customerDetails);
			userDetailslList.setHomeBranch(customerDetails.getHomeBranch());

			if(customerDetails.getClassificationItems()!=null && customerDetails.getClassificationItems().size()>0) {
				for(int j=0;j<customerDetails.getClassificationItems().size();j++) {
					if(customerDetails.getClassificationItems().get(j).getClassificationName().toString().equalsIgnoreCase("WebCurrency")) {
						userDetailslList.setCurrencyCode(customerDetails.getClassificationItems().get(j).getClassificationValue());
						break;
					}
					else {
						userDetailslList.setCurrencyCode("CAD");
					}
				}
			}
			else {
				userDetailslList.setCurrencyCode("CAD");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return userDetailslList;
	} 
	
	
	public EventModel getEventRegistrationDetails(int eventRegId){
		String sql = "select EM.EVENT_TITLE,EM.COST,EM.EVENT_CATEGORY, ER.FIRST_NAME,ER.EVENT_ID,ER.LAST_NAME,ER.COMPANY_NAME,ER.EMAIL,ER.CONTACT_MOBILE_PHONE,ER.PO_NUMBER,ER.PAYMENT_MODE from EVENT_MANAGER EM, EVENT_REGISTRATION ER where EM.EVENT_ID = ER.EVENT_ID AND EVENT_REG_ID=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		EventModel event = new EventModel();
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventRegId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				event.setTitle(rs.getString("EVENT_TITLE"));
	        	event.setCost(rs.getDouble("COST"));
	        	event.setFirstName(rs.getString("FIRST_NAME"));                      
	        	event.setEventId(rs.getInt("EVENT_ID"));                    
	        	event.setLastName(rs.getString("LAST_NAME")); 
	        	event.setCompanyName(rs.getString("COMPANY_NAME"));
	        	event.setEmail(rs.getString("EMAIL"));                            
	        	event.setPhoneNumber(rs.getString("CONTACT_MOBILE_PHONE")); 
	        	event.setPoNumber(rs.getString("PO_NUMBER"));
	        	event.setPaymentMode(rs.getString("PAYMENT_MODE"));
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
		return event;
	}

	public HashMap<String,String> getBCAddressBookCustomFields(int addressBookId){
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
			while(rs.next()) {
				textFieldName = rs.getString("FIELD_NAME");
				textFieldValue =  rs.getString("TEXT_FIELD_VALUE");
				customValues.put(textFieldName, textFieldValue);
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
		return customValues;
	}

	public void getBCAddressBookCustomFieldDetails(UsersModel addressListVal) {
		HashMap<String,String> shippingCustomFieldData = new  HashMap<String,String>();
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION"))!=null){
			 shippingCustomFieldData = CommonUtility.customServiceUtility().getBCAddressBookCustomFields(addressListVal.getAddressBookId());
			 if(shippingCustomFieldData!=null && shippingCustomFieldData.size()>0){
				 String currencyCode = "CAD";
				 for(Entry<String, String> entry : shippingCustomFieldData.entrySet()){
					if(entry!=null && entry.getValue()!=null && entry.toString().contains("CURRENCY_CODE")){
						currencyCode=entry.getValue();
					}
				 }
				 addressListVal.setCurrencyCode(currencyCode);
			 }
		  }
	}

	@SuppressWarnings("resource")
	@Override
	public void checkShipToLevelCustomFieldsOnAssignShipEntity(HttpServletRequest request, HttpSession session, int subsetId) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHIP_TO_LEVEL_ROCKWELL_ENABLE")).equalsIgnoreCase("Y")){
			HashMap<String,String> userDetails = new HashMap<String, String>();
			 String userName = request.getParameter("userName");
			 String selectedShipLocation = (String) session.getAttribute("selectedShipLocation");
			 int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
			 int selectedShippingAddressId = 0;
			 boolean rockwellLocation = false;
			 boolean rockwellItem = false;
			 String currencyCode;
			 String sql = "";
			 PreparedStatement pstmt = null;
			 Connection conn = null;
			 ResultSet rs = null;
			 try {
				 if(selectedShipLocation!=null && selectedShipLocation!="" && selectedShipLocation.trim().length()>0){
					 selectedShippingAddressId=CommonUtility.validateNumber(selectedShipLocation);
				 }
				 else {
					 userDetails=UsersDAO.getUserPasswordAndUserId(userName,"Y");
					 selectedShippingAddressId = CommonUtility.validateNumber(userDetails.get("defaultShippingAddressId"));
				 }
				 session.setAttribute("selectedShippingAddressId",selectedShippingAddressId);
				 if(selectedShippingAddressId>0) {
					 HashMap<String,String> shippingCustomFieldData = CommonUtility.customServiceUtility().getBCAddressBookCustomFields(selectedShippingAddressId);
						 if(shippingCustomFieldData!=null && shippingCustomFieldData.size()>0){
							 for(Entry<String, String> entry : shippingCustomFieldData.entrySet()){
								if(entry!=null && entry.getValue()!=null && entry.toString().contains("CURRENCY_CODE")){
									currencyCode=entry.getValue();
									session.setAttribute("currencyCode",currencyCode);
								}
								if(entry!=null && entry.getValue()!=null && entry.toString().contains("SHIP_TO_LEVEL_ROCKWELL=Y")){
									rockwellLocation = true;
									break;
								}
								else{
									rockwellLocation = false;
								}
							 }
							 session.setAttribute("shippingCustomFieldData",shippingCustomFieldData);
						 }
						 else {
							 session.setAttribute("shippingCustomFieldData","");
						 }
					}
				 
				LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = null;
				String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
				int generalSubset = 0;				
				String tempSubset = (String) session.getAttribute("userSubsetId");
				subsetId = CommonUtility.validateNumber(tempSubset);
				String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
				generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
				ArrayList<Integer> cartItemList = new ArrayList<Integer>();
				ArrayList<Integer> rockWellItemList = new ArrayList<Integer>();
				int siteId = 0;
				if(CommonDBQuery.getGlobalSiteId()>0){
					siteId = CommonDBQuery.getGlobalSiteId();
				}
				int userId = Integer.parseInt(sessionUserId);
				conn = ConnectionManager.getDBConnection();
				sql = PropertyAction.SqlContainer.get("getCartItemDetailQuery");
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
                rs = pstmt.executeQuery();
				while(rs.next()){
					cartItemList.add(rs.getInt("ITEM_ID"));
				}
				
				if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
					customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(cartItemList," OR "),"itemid");
				}
				
				if(customFieldVal!=null && customFieldVal.size()>0){
					for(Map.Entry<Integer, LinkedHashMap<String, Object>> entry : customFieldVal.entrySet()){
						if(entry!=null && entry.getValue()!=null && entry.getValue().toString().contains("custom_ROCKWELL_ITEM=Y")){
							rockWellItemList.add(entry.getKey());
							rockwellItem = true;
							break;
						}
						else {
							rockwellItem = false;
						}
					}
				}
				
				if(rockwellLocation == false && rockwellItem == true) {
					if(rockWellItemList.size()>0) {
						String idList = rockWellItemList.toString();
						idList = idList.substring(1, idList.length() - 1).replace(", ", ",");
						sql = "DELETE FROM CART WHERE  USER_ID=? AND ITEM_ID IN ("+idList+")";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, userId);
						int count = pstmt.executeUpdate();
					}
					System.out.println("Selected Location is Rockwell Location and Rockwell Items found");
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
		}
	}

	@SuppressWarnings("resource")
	@Override
	public void checkBCAddressCustomFieldsAfterAutoSync(HttpSession session) {
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = Integer.parseInt(sessionUserId);
		int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
		try {
			if(userId>1) {
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_AUTO_SYNC_SHIPPING_ADDRESS")).equalsIgnoreCase("Y")){
					UsersModel customerInfoInput = new UsersModel();
					customerInfoInput.setSession(session);
					customerInfoInput.setUserToken((String) session.getAttribute("userToken"));
					customerInfoInput.setCustomerId(CommonUtility.validateString((String) session.getAttribute("customerId")));
					Connection conn = null;
					conn = ConnectionManager.getDBConnection();
					String sql = "";
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					String currencyCode;
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHIP_TO_LEVEL_ROCKWELL_ENABLE")).equalsIgnoreCase("Y")){
						try {
							ArrayList<Integer> rokwellShipToIdList = new ArrayList<Integer>();
							ArrayList<Integer> rokwellShipAddressList = new ArrayList<Integer>();
							ArrayList<Integer> rokwellBCAddressIdList = new ArrayList<Integer>();
							int entityId = CommonUtility.validateNumber((String) session.getAttribute("customerId"));
							String rokwellCustomFiledValue="Y";	
							int shippingBCAddressId = 0;
							boolean rockwellLocation = false;
							boolean rockwellItem = false;
							
							sql = "SELECT BC_ADDRESS_BOOK_ID FROM BC_ADDRESS_BOOK WHERE ADDRESS_TYPE='Ship' AND ENTITY_ID=?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setInt(1, entityId);
							rs = pstmt.executeQuery();
							while(rs.next()){
								rokwellBCAddressIdList.add(rs.getInt("BC_ADDRESS_BOOK_ID"));
								shippingBCAddressId=rs.getInt("BC_ADDRESS_BOOK_ID");
							}
							
							if(rokwellBCAddressIdList.size()==1) {
								session.setAttribute("selectedShippingAddressId",shippingBCAddressId);
								 if(shippingBCAddressId>0) {
									 HashMap<String,String> shippingCustomFieldData = CommonUtility.customServiceUtility().getBCAddressBookCustomFields(shippingBCAddressId);
									 if(shippingCustomFieldData!=null && shippingCustomFieldData.size()>0){
										 for(Entry<String, String> entry : shippingCustomFieldData.entrySet()){
											if(entry!=null && entry.getValue()!=null && entry.toString().contains("CURRENCY_CODE")){
												currencyCode=entry.getValue();
												session.setAttribute("currencyCode",currencyCode);
											}
											if(entry!=null && entry.getValue()!=null && entry.toString().contains("SHIP_TO_LEVEL_ROCKWELL=Y")){
												rockwellLocation = true;
												break;
											}
											else{
												rockwellLocation = false;
											}
										 }
										 session.setAttribute("shippingCustomFieldData",shippingCustomFieldData);
									 }
									 else {
										 session.setAttribute("shippingCustomFieldData","");
									 }
								}
							}
							if(rokwellBCAddressIdList.size()>1) {
								sql = "SELECT BCAB.SHIP_TO_ID,BCCF.BC_AB_CUSTOM_FIELD_VALUE_ID FROM BC_ADDRESS_BOOK BCAB,BC_AB_CUSTOM_FIELD_VALUES BCCF,CUSTOM_FIELDS CF,LOC_CUSTOM_FIELD_VALUES LCFV "
										+ "WHERE BCAB.ADDRESS_TYPE='Ship' AND CF.CUSTOM_FIELD_ID = BCCF.CUSTOM_FIELD_ID AND LCFV.LOC_CUSTOM_FIELD_VALUE_ID = BCCF.LOC_CUSTOM_FIELD_VALUE_ID "
										+ "AND BCAB.ENTITY_ID=? AND BCCF.ADDRESS_BOOK_ID=BCAB.BC_ADDRESS_BOOK_ID AND CF.FIELD_NAME='SHIP_TO_LEVEL_ROCKWELL' AND LCFV.TEXT_FIELD_VALUE='Y'";
								ConnectionManager.closeDBPreparedStatement(pstmt);
								pstmt = conn.prepareStatement(sql);
								pstmt.setInt(1, entityId);
								rs = pstmt.executeQuery();
								while(rs.next()){
									rokwellShipToIdList.add(rs.getInt("SHIP_TO_ID"));
									rokwellShipAddressList.add(rs.getInt("BC_AB_CUSTOM_FIELD_VALUE_ID"));
								}
								
								if(rokwellShipAddressList.size()>0) {
									String idList = rokwellShipAddressList.toString();
									idList = idList.substring(1, idList.length() - 1).replace(", ", ",");
									sql = "DELETE FROM BC_AB_CUSTOM_FIELD_VALUES WHERE BC_AB_CUSTOM_FIELD_VALUE_ID IN ("+idList+")";
									ConnectionManager.closeDBPreparedStatement(pstmt);
									pstmt = conn.prepareStatement(sql);
									int count = pstmt.executeUpdate();
								}
								
								if(rokwellShipToIdList.size()>0) {																		 
										LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = null;
										int generalSubset = 0;				
										String tempSubset = (String) session.getAttribute("userSubsetId");
										int subsetId = CommonUtility.validateNumber(tempSubset);
										subsetId = CommonUtility.validateNumber(tempSubset);
										String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
										generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
										ArrayList<Integer> cartItemList = new ArrayList<Integer>();
										ArrayList<Integer> rockWellItemList = new ArrayList<Integer>();
										int siteId = 0;
										if(CommonDBQuery.getGlobalSiteId()>0){
											siteId = CommonDBQuery.getGlobalSiteId();
										}
										sql = PropertyAction.SqlContainer.get("getCartItemDetailQuery");
										ConnectionManager.closeDBPreparedStatement(pstmt);
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
					                    rs = pstmt.executeQuery();
										while(rs.next()){
											cartItemList.add(rs.getInt("ITEM_ID"));
										}
										
										if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
											customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(cartItemList," OR "),"itemid");
										}
										
										if(customFieldVal!=null && customFieldVal.size()>0){
											for(Map.Entry<Integer, LinkedHashMap<String, Object>> entry : customFieldVal.entrySet()){
												if(entry!=null && entry.getValue()!=null && entry.getValue().toString().contains("custom_ROCKWELL_ITEM=Y")){
													rockWellItemList.add(entry.getKey());
													rockwellItem = true;
													break;
												}
												else {
													rockwellItem = false;
												}
											}
										}
																				
										/*if(rockwellLocation == false && rockwellItem == true) {
											if(rockWellItemList.size()>0) {
												String idList = rockWellItemList.toString();
												idList = idList.substring(1, idList.length() - 1).replace(", ", ",");
												sql = "DELETE FROM CART WHERE  USER_ID=? AND ITEM_ID IN ("+idList+")";
												ConnectionManager.closeDBPreparedStatement(pstmt);
												pstmt = conn.prepareStatement(sql);
												pstmt.setInt(1, userId);
												int count = pstmt.executeUpdate();
											}
											System.out.println("Selected Location is Rockwell Location and Rockwell Items found");
										}*/
										
										UserManagementAction.scynEntityAddress(customerInfoInput);
										String idList= StringUtils.join(rokwellShipToIdList, "','");
										idList = "'" + idList + "'";
										sql = "SELECT BC_ADDRESS_BOOK_ID FROM BC_ADDRESS_BOOK WHERE ADDRESS_TYPE='Ship' AND ENTITY_ID=? AND SHIP_TO_ID IN("+idList+")";
										ConnectionManager.closeDBPreparedStatement(pstmt);
										pstmt = conn.prepareStatement(sql);
										pstmt.setInt(1, entityId);
										rs = pstmt.executeQuery();
										while(rs.next()){
											int addressBookId=rs.getInt("BC_ADDRESS_BOOK_ID");
											UsersDAO.insertCustomField(CommonUtility.validateString(rokwellCustomFiledValue), "SHIP_TO_LEVEL_ROCKWELL",userId, addressBookId, "BC_ADDRESS_BOOK");
										}
								}else {
									UserManagementAction.scynEntityAddress(customerInfoInput);
								}
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}	
						finally {
							pstmt.close();
							rs.close();
							conn.close();
						}
					}	
					else {
						UserManagementAction.scynEntityAddress(customerInfoInput);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void checkOversizeAndHazmatFreightRule(LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal, SalesModel salesOrderDetailList) {
		FreightCalculatorModel freightInput = new FreightCalculatorModel();
		try {
			if(customFieldVal!=null && CommonDBQuery.getSystemParamtersList().get("OVERSIZE_ITEM_FREIGHT_RULE")!=null && CommonDBQuery.getSystemParamtersList().get("OVERSIZE_ITEM_FREIGHT_RULE").trim().equalsIgnoreCase("Y")){
				for(Entry<Integer, LinkedHashMap<String, Object>> entry : customFieldVal.entrySet()){
					if(entry!=null && entry.getValue()!=null && entry.getValue().toString().contains("custom_OverSize=Y")){
						freightInput.setOverSize("Y");
						salesOrderDetailList.setIsOverSize(freightInput.getOverSize());
						break;
					}
					else{
						freightInput.setOverSize("N");
					}
					
					if(entry!=null && entry.getValue()!=null && entry.getValue().toString().contains("custom_Hazmat=Y")){
						freightInput.setHazmat("Y");
						salesOrderDetailList.setHazardiousMaterial(freightInput.getHazmat());
						break;
					}
					else{
						freightInput.setHazmat("N");
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addExchangeRateToTax(HttpSession session, SalesModel quoteInfo, Cimm2BCentralOrder orderResponse) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION"))!=null){
			Double exchangeRate;
			if(session.getAttribute("exchangeRate") != null) {
				exchangeRate=(Double) session.getAttribute("exchangeRate");
				quoteInfo.setTax(orderResponse.getTaxAmount()/exchangeRate);
			}
			else {
				quoteInfo.setTax(orderResponse.getTaxAmount());
			}
		}
	}

	@Override
	public void addExchangeRateToTotal(HttpSession session, SalesModel quoteInfo, Cimm2BCentralOrder orderResponse, double freightCharges) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION"))!=null){
			Double exchangeRate;
			if(session.getAttribute("exchangeRate") != null) {
				exchangeRate=(Double) session.getAttribute("exchangeRate");
				quoteInfo.setTotal((orderResponse.getOrderTotal() + freightCharges)/exchangeRate);
			}
			else {
				quoteInfo.setTotal((orderResponse.getOrderTotal() + freightCharges));
			}
		}
	}

	@Override
	public void checkWebCurrency(Cimm2BCentralCustomer customerDetails, boolean webCurrency) {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String currencyCode = "CAD";
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION"))!=null){
			if(customerDetails.getClassificationItems()!=null && customerDetails.getClassificationItems().toString() !="") {
				for(int j=0;j<customerDetails.getClassificationItems().size();j++) {
					if(customerDetails.getClassificationItems().get(j).getClassificationName().toString().equalsIgnoreCase("WebCurrency")) {
						webCurrency=true;
						currencyCode = customerDetails.getClassificationItems().get(j).getClassificationValue();
						break;
					}
					else {
						webCurrency=false;
					}
				}
			}
			else {
				webCurrency=false;
			}
		}
		session.setAttribute("currencyCode",currencyCode);
	}

	@Override
	public void setCurrencyCodeDetails(Cimm2BCentralCustomer shipTo, AddressModel shipAddressModel) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION"))!=null){
			if(shipTo.getClassificationItems()!=null && shipTo.getClassificationItems().toString() !="") {
				for(int j=0;j<shipTo.getClassificationItems().size();j++) {
					if(shipTo.getClassificationItems().get(j).getClassificationName().toString().equalsIgnoreCase("WebCurrency")) {
						shipAddressModel.setCurrencyCode(shipTo.getClassificationItems().get(j).getClassificationValue());
					}
					else {
						shipAddressModel.setCurrencyCode("CAD");
					}
				}
			}
			else {
				shipAddressModel.setCurrencyCode("CAD");
			}
		}
		if(shipAddressModel.getCurrencyCode() == null || CommonUtility.validateString(shipAddressModel.getCurrencyCode()).length() == 0) {
			shipAddressModel.setCurrencyCode("CAD");
		}
	}

	@Override
	public void deleteBCAddressBookDetailsFromBCAddressBookCustomTable(Connection conn, HttpSession session) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION"))!=null){
			String sql = "";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ArrayList<Integer> shipAddressList = new ArrayList<Integer>();
			int entityId = CommonUtility.validateNumber((String) session.getAttribute("customerId"));
			try {
				sql = "SELECT BCAB.SHIP_TO_ID,BCCF.ADDRESS_BOOK_ID FROM BC_ADDRESS_BOOK BCAB,BC_AB_CUSTOM_FIELD_VALUES BCCF WHERE BCAB.ADDRESS_TYPE='Ship' AND BCAB.ENTITY_ID=? AND BCCF.ADDRESS_BOOK_ID=BCAB.BC_ADDRESS_BOOK_ID";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, entityId);
				rs = pstmt.executeQuery();
				while(rs.next()){
					shipAddressList.add(rs.getInt("ADDRESS_BOOK_ID"));
				}
				if(shipAddressList.size()>0) {
					String idList = shipAddressList.toString();
					idList = idList.substring(1, idList.length() - 1).replace(", ", ",");
					sql = "DELETE FROM BC_AB_CUSTOM_FIELD_VALUES WHERE ADDRESS_BOOK_ID IN ("+idList+")";
					ConnectionManager.closeDBPreparedStatement(pstmt);
					pstmt = conn.prepareStatement(sql);
					int count = pstmt.executeUpdate();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}finally {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
			}
		}
	}

	@Override
	public void insertBCAddressBookCustomFields(AddressModel getShipList, int userId, int shipId) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION"))!=null){
			String currencyCustomFiledValue = null;
			if(getShipList.getCurrencyCode()!=null && getShipList.getCurrencyCode() !="" && getShipList.getCurrencyCode().length()>0) {
				currencyCustomFiledValue=getShipList.getCurrencyCode();
			}
			UsersDAO.insertCustomField(CommonUtility.validateString(currencyCustomFiledValue), "CURRENCY_CODE",userId, shipId, "BC_ADDRESS_BOOK");
		}
	}

	@Override
	public void insertDefaultValueToBCAddressBookCustomFields(boolean webCurrency, int userId, int shipId) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION"))!=null){
			String currencyCustomFiledValue = "CAD";
			if(webCurrency == true) {
				currencyCustomFiledValue="USD";
			}
			UsersDAO.insertCustomField(CommonUtility.validateString(currencyCustomFiledValue), "CURRENCY_CODE",userId, shipId, "BC_ADDRESS_BOOK");
		}
	}

	@Override
	public int getUserId(Connection conn, UsersModel contactInfo, int userId) {
		String sql = PropertyAction.SqlContainer.get("getIsRegisteredUser");
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
        try {
        	conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(contactInfo.getUserName()).toUpperCase());
			rs = pstmt.executeQuery();
			if(rs.next()){
				userId=rs.getInt("USER_ID");
			}
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        finally {
        	ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
        }
        return userId;
	}

	@Override
	public ArrayList<ProductsModel> getEventOrderDetails(SalesOrderManagementModel salesOrderInput) {
		ArrayList<Cimm2BCentralLineItem> itemDetails = salesOrderInput.getLineItems();
	    ArrayList<ProductsModel> orderDetails = new ArrayList<ProductsModel>();	
		ProductsModel orderItemVal = new ProductsModel();
		try {
			orderItemVal.setItemId(CommonUtility.validateNumber(itemDetails.get(0).getItemId()));
			orderItemVal.setPartNumber(itemDetails.get(0).getPartNumber());
			orderItemVal.setShortDesc(itemDetails.get(0).getShortDescription());
			orderItemVal.setQty(itemDetails.get(0).getQty());
			orderItemVal.setPrice(itemDetails.get(0).getUnitPrice());
			orderItemVal.setUnitPrice(itemDetails.get(0).getUnitPrice());
			orderItemVal.setListPrice(itemDetails.get(0).getListPrice());
			orderItemVal.setExtendedPrice(itemDetails.get(0).getExtendedPrice());
			orderItemVal.setUom(itemDetails.get(0).getUom());
			orderItemVal.setLineItemComment(itemDetails.get(0).getLineItemComment());
			orderItemVal.setUomQty(CommonUtility.validateNumber(itemDetails.get(0).getUomQty()));				
			orderDetails.add(orderItemVal);
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ORDER_SUBMIT_VERSION")).equalsIgnoreCase("V2")){
				salesOrderInput.setOrderItems(orderDetails);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return orderDetails;		
	}

	@Override
	public void setOversizeAndHazmatFreightRule(LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal, LinkedHashMap<String, Object> contentObject) {
		FreightCalculatorModel freightInput = new FreightCalculatorModel();
		if(customFieldVal!=null && CommonDBQuery.getSystemParamtersList().get("OVERSIZE_ITEM_FREIGHT_RULE")!=null && CommonDBQuery.getSystemParamtersList().get("OVERSIZE_ITEM_FREIGHT_RULE").trim().equalsIgnoreCase("Y")){
			for(Map.Entry<Integer, LinkedHashMap<String, Object>> entry : customFieldVal.entrySet()){
				if(entry!=null && entry.getValue()!=null && entry.getValue().toString().contains("custom_OverSize=Y")){
					freightInput.setOverSize("Y");
					contentObject.put("overSizeItem",freightInput.getOverSize());
					break;
				}else{
					freightInput.setOverSize("N");
				}
				
				if(entry!=null && entry.getValue()!=null && entry.getValue().toString().contains("custom_Hazmat=Y")){
					freightInput.setHazmat("Y");
					contentObject.put("hazmatItem",freightInput.getHazmat());
					break;
				}else{
					freightInput.setHazmat("N");
				}
			}
		}
	}

	public double getFrieghtCharges(String shipViaDisplay, HttpSession session, LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal, double totalCartFrieghtCharges, FreightCalculatorModel freightValue) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_FIXED_FEDEX_FRIEGHT_CHARGES")).equalsIgnoreCase("Y") && CommonUtility.validateString(shipViaDisplay).toUpperCase(Locale.US).contains("FEDEX")){
			LinkedHashMap<String, String> customerCustomFieldValue = null;
			int buyingCompanyId = CommonUtility.validateNumber((String)session.getAttribute("buyingCompanyId"));
			if(buyingCompanyId > 0){
				customerCustomFieldValue = UsersDAO.getAllCustomerCustomFieldValue(buyingCompanyId);
				if(customerCustomFieldValue!=null && customerCustomFieldValue.size()>0){
					session.setAttribute("customerCustomFieldValue",customerCustomFieldValue);
				}
			}
			if(customerCustomFieldValue!=null && CommonUtility.validateString(customerCustomFieldValue.get("FREIGHT_OUT_EXEMPT")).length()>0 && !customerCustomFieldValue.get("FREIGHT_OUT_EXEMPT").equalsIgnoreCase("Y")){
				for(Map.Entry<Integer, LinkedHashMap<String, Object>> entry : customFieldVal.entrySet()){
					if(entry!=null && entry.getValue()!=null && entry.getValue().toString().contains("custom_OverSize=Y")){
						totalCartFrieghtCharges = totalCartFrieghtCharges+CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("FIXED_FEDEX_FRIEGHT_CHARGE"));
					}
				} 
			}
		}
		return totalCartFrieghtCharges;
	}

	@Override
	public void getCreditBalanceDetails(HttpSession session, HashMap<String, UsersModel> userAddress, LinkedHashMap<String, Object> contentObject) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREDIT_BALANCE_EVALUATION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREDIT_BALANCE_EVALUATION"))!=null){
			UsersModel userAccountDetail = new UsersModel();
			UserManagement userManagement = new UserManagementImpl();
			LinkedHashMap<String, String> accountInquiryInput = new LinkedHashMap<String, String>();
			accountInquiryInput.put("userToken", (String)session.getAttribute("userToken"));
			accountInquiryInput.put("userName", (String) session.getAttribute(Global.USERNAME_KEY));
			accountInquiryInput.put("entityId", userAddress.get("Bill").getEntityId());
			userAccountDetail = userManagement.getAccountDetail(accountInquiryInput);
			contentObject.put("arCreditLimit", userAccountDetail.getArCreditLimit());
			contentObject.put("arCreditAvail", userAccountDetail.getArCreditAvail());
		}
	}

	public void checkCurrencyConversion(HttpSession session, Cimm2BCentralItem item) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION"))!=null){
			String currencyCode = "CAD";
			if(item.getPricingWarehouse().getCurrencyDetails()!=null && item.getPricingWarehouse().getCurrencyDetails().toString() !="") {
				item.getPricingWarehouse().setCustomerPrice(item.getPricingWarehouse().getCustomerPrice()!=null?((item.getPricingWarehouse().getCustomerPrice())/(item.getPricingWarehouse().getCurrencyDetails().getExchangeRate())):0.0);
				session.setAttribute("exchangeRate", item.getPricingWarehouse().getCurrencyDetails().getExchangeRate());
			}
			
			if(session!=null && session.getAttribute("selectedShippingAddressId")!=null) {
				Integer selectedShippingAddressId = (Integer) session.getAttribute("selectedShippingAddressId");
				HashMap<String,String> shippingCustomFieldData = CommonUtility.customServiceUtility().getBCAddressBookCustomFields(selectedShippingAddressId);
				if(shippingCustomFieldData!=null && shippingCustomFieldData.size()>0){
					 for(Entry<String, String> entry : shippingCustomFieldData.entrySet()){
						if(entry!=null && entry.getValue()!=null && entry.toString().contains("CURRENCY_CODE")){
							currencyCode=entry.getValue();
							break;
						}
					 }
				 }
			}
			else {
				if(item.getPricingWarehouse().getCurrencyDetails()!=null) {
					currencyCode=item.getPricingWarehouse().getCurrencyDetails().getForeignCurrency();
				}
				else {
					currencyCode=item.getPricingWarehouse().getCurrencyCode();
				}
			}
			item.getPricingWarehouse().setCurrencyCode(currencyCode);
		}
	}
	
	@Override
	public void addExchangeRateToQuantityBreakItems(Cimm2BCentralItem item, ProductsModel productsModel, Cimm2BCentralWarehouse wareHouseDetail) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION"))!=null){
			if(item.getPricingWarehouse().getCurrencyDetails()!=null && item.getPricingWarehouse().getCurrencyDetails().toString() !="") {
				productsModel.setPrice(wareHouseDetail.getCustomerPrice()!=null?(wareHouseDetail.getCustomerPrice())/(item.getPricingWarehouse().getCurrencyDetails().getExchangeRate()):0.0);
			}
			productsModel.setCurrencyCode(item.getPricingWarehouse().getCurrencyCode());
		}
	}

	@Override
	public void setQuantityBreakItemsPrice(Cimm2BCentralItem item, ProductsModel productsModel, ProductManagementModel priceInquiryInput) {
		if(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")!=null && CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK").trim().equalsIgnoreCase("Y") && item.getPricingWarehouse().getPriceBreaks()!=null && item.getPricingWarehouse().getPriceBreaks().size() > 0 && !item.getPricingWarehouse().getPriceBreaks().isEmpty()){
			ArrayList<ProductsModel> quantityBreak = new ArrayList<ProductsModel>();
			for(Cimm2BCentralPriceBreaks priceBreaks: item.getPricingWarehouse().getPriceBreaks() ){
				ProductsModel quantityBreaks = new ProductsModel();
				quantityBreaks.setMaximumQuantityBreak(priceBreaks.getMaximumQuantity()!=null?priceBreaks.getMaximumQuantity():0);
				quantityBreaks.setMinimumQuantityBreak(priceBreaks.getMinimumQuantity());
				quantityBreaks.setCustomerPriceBreak(priceBreaks.getCustomerPrice());
				
				quantityBreak.add(quantityBreaks);
				productsModel.setQuantityBreakList(quantityBreak);
			}
			if(priceInquiryInput.getPartIdentifierQuantity()!=null){
			for(int i=0;i<quantityBreak.size();i++)
             {
				//logger.info(quantityBreak.get(i).getCustomerPriceBreak() + " : " + quantityBreak.get(i).getMinimumQuantityBreak()+ " : " + quantityBreak.get(i).getMaximumQuantityBreak());
            
				double maximumQuantityBreak = 0.0;
				if(quantityBreak.get(i).getMaximumQuantityBreak()==0) {
					if(i+1 < quantityBreak.size()) {
						maximumQuantityBreak = quantityBreak.get(i+1).getMinimumQuantityBreak();
					}
					if(i+1 == quantityBreak.size()) {
						maximumQuantityBreak = quantityBreak.get(i).getMinimumQuantityBreak();
					}
				}
														
               if(priceInquiryInput.getPartIdentifierQuantity().get(0) >= quantityBreak.get(i).getMinimumQuantityBreak() && priceInquiryInput.getPartIdentifierQuantity().get(0) <= (quantityBreak.get(i).getMaximumQuantityBreak()!=0?quantityBreak.get(i).getMaximumQuantityBreak():maximumQuantityBreak))
              {
            	   if(priceInquiryInput.getPartIdentifierQuantity().get(0) == maximumQuantityBreak) {
            		   if(i+1 < quantityBreak.size()) {
            			   productsModel.setPrice(quantityBreak.get(i+1).getCustomerPriceBreak()); 
						}
					}
            	   else {
            		   productsModel.setPrice(quantityBreak.get(i).getCustomerPriceBreak());  
            	   }
            	   break;
              }else{
            	  productsModel.setPrice(item.getPricingWarehouse().getCustomerPrice());					                            	  
              }
             }
			}
		}
	}

	@Override
	public void insertOrderStatus(LinkedHashMap<String, Object> orderDetails, String orderStatus) {
		orderDetails.put("orderStatus", orderStatus);
	}

	@Override
	public void setOrderSatusToOrderDetail(SalesModel erpOrderDetail, SalesModel defaultOrderDetail, String orderStatus) {
		erpOrderDetail.setOrderStatus(orderStatus);
		defaultOrderDetail.setOrderStatus(orderStatus);
	}

	@Override
	public String getOrderStatus(LinkedHashMap<String, Object> orderDetails) {
		String result = null;
		try {
			if(orderDetails.get("orderStatus")!=null && orderDetails.get("orderStatus")!="" && orderDetails.get("orderStatus").toString().trim().length()>0) {
				result=orderDetails.get("orderStatus").toString();
			} 
			else {
				result="New";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String setFirstLoginTrueForInactiveUser() {
		return "Y";
	}
	
	@Override
	public String setCountryName(UsersModel userInfo) {
		String country = null;
		if(userInfo.getCountry().equalsIgnoreCase("CA")) {
			country="CANADA";
		}
		else if(userInfo.getCountry().equalsIgnoreCase("CANADA")) {
			country="CA";
		}
		return country;
	}

	public void insertCustomFieldValues(String newsLetterCustomFiledValue, int userId, int buyingCompanyid) {
		UsersDAO.insertCustomField(newsLetterCustomFiledValue, "NEWSLETTER",userId, buyingCompanyid, "USER");
	}
	
	public Cimm2BCentralLineItem  disableUmqtyToErp(Cimm2BCentralLineItem cimm2bCentralLineItem){
		 cimm2bCentralLineItem.setUomQty("");
		 return cimm2bCentralLineItem;
	 }
	
	@Override
	public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
	{	
		String newsLetterCustomFiledValue="";
		if(userDetailsInput.getNewsLetterSub()!=null && userDetailsInput.getNewsLetterSub().equals("Y")){
			newsLetterCustomFiledValue="Y";
		}else{
			newsLetterCustomFiledValue="N";
		}
		UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
	}
	
	@Override
	public void setCurrencyCodeToSession(Cimm2BCentralCustomer customerDetails, HttpSession session) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CURRENCY_CONVERSION")).equalsIgnoreCase("Y")){
		   String currencyCode = "CAD";
		   if(customerDetails!=null && customerDetails.getClassificationItems()!=null && customerDetails.getClassificationItems().size()>0) {
	            for(int j=0;j<customerDetails.getClassificationItems().size();j++) {
	                   if(customerDetails.getClassificationItems().get(j).getClassificationName().toString().equalsIgnoreCase("WebCurrency")) {
	                         currencyCode = customerDetails.getClassificationItems().get(j).getClassificationValue();
	                         break;
	                   }
	            }
	        }
	        session.setAttribute("currencyCode",currencyCode);
		}
	}
}
