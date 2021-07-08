package com.unilog.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.http.HttpStatus;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.erp.service.SalesOrderManagement;
import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.models.PackageInfo;
import com.erp.service.cimm2bcentral.models.UpsFreight;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthorizedSaveCreditCardRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralBillAndShipToContact;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCoupon;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerCard;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPriceAndAvailability;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipMethodQuantity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipVia;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSubOrderCriteria;
import com.erp.service.cimm2bcentral.utilities.Cimm2BPriceAndAvailabilityRequest;
import com.erp.service.cimmesb.utils.CimmESBServiceUtils;
import com.erp.service.impl.SalesOrderManagementImpl;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.ProductManagementModel;
import com.erp.service.model.SalesOrderManagementModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.avalara.AvalaraUtility;
import com.unilog.avalara.model.TaxResponse;
import com.unilog.cimmesb.client.ecomm.request.CimmOrderRequest;
import com.unilog.cimmesb.client.ecomm.request.CimmProductQueryLineItem;
import com.unilog.cimmesb.client.ecomm.request.ErpItemSearchRequest;
import com.unilog.cimmesb.client.response.CimmLineItem;
import com.unilog.cimmesb.client.request.RestRequest;
import com.unilog.cimmesb.client.response.CimmOrder;
import com.unilog.cimmesb.client.response.CimmSubOrder;
import com.unilog.cimmesb.client.response.OrderLevelCouponList;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCResponse;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCustomer;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.SalesAction;
import com.unilog.sales.SalesDAO;
import com.unilog.sales.SalesModel;
import com.unilog.security.SecureData;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.users.AddressModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

public class OrgillCustomServices implements UnilogFactoryInterface {
		/**
		 *Below code Written is for Tyndale *Reference- Chetan Sandesh
		 */

		private static final long serialVersionUID = 1L;
		private HttpServletRequest request;
		private static ArrayList<ShipVia> SiteShipViaList = null;
		private int defaultBillToId;
		private int defaultShipToId;
		private UsersModel billAddress = null;
		private UsersModel shipAddress = null;
		
		private static UnilogFactoryInterface serviceProvider;
		
		private OrgillCustomServices() {}
		
		public static UnilogFactoryInterface getInstance() {
				synchronized (OrgillCustomServices.class) {
					if(serviceProvider == null) {
						serviceProvider = new OrgillCustomServices();
					}
				}
			return serviceProvider;
		}

		/**
		 *Below code Written is for Tyndale to get Group of items based on Ship Methods at product level. *Reference- Chetan Sandesh
		 */
		public LinkedHashMap<String, ArrayList<ProductsModel>> getGroupedItemsInProductsData(ArrayList<ProductsModel> productListData) {
			LinkedHashMap<String, ArrayList<ProductsModel>> cartItemDataList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
			ArrayList<ProductsModel> cartItems = new ArrayList<ProductsModel>();
			try {
				for (ProductsModel product : productListData) {
					cartItems = cartItemDataList.get(CommonUtility.validateString(product.getMultipleShipVia()));
					if (cartItems == null) {
						cartItems = new ArrayList<ProductsModel>();
						cartItems.add(product);
						cartItemDataList.put(CommonUtility.validateString(product.getMultipleShipVia()), cartItems);
					} else {
						cartItems.add(product);
						cartItemDataList.put(CommonUtility.validateString(product.getMultipleShipVia()), cartItems);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cartItemDataList;
		}
		/**
		 *Below code Written is for Tyndale to get Group of items based on Ship Methods at sales level. *Reference- Chetan Sandesh
		 */
		public LinkedHashMap<String, ArrayList<SalesModel>> getGroupedItemsInSalesData(ArrayList<SalesModel> orderList) {
			LinkedHashMap<String, ArrayList<SalesModel>> cartItemDataList = new LinkedHashMap<String, ArrayList<SalesModel>>();
			ArrayList<SalesModel> cartItems = new ArrayList<SalesModel>();
			try {
				for (SalesModel product : orderList) {
					cartItems = cartItemDataList.get(CommonUtility.validateString(product.getMultipleShipVia()));
					if (cartItems == null) {
						cartItems = new ArrayList<SalesModel>();
						cartItems.add(product);
						cartItemDataList.put(CommonUtility.validateString(product.getMultipleShipVia()), cartItems);
					} else {
						cartItems.add(product);
						cartItemDataList.put(CommonUtility.validateString(product.getMultipleShipVia()), cartItems);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return cartItemDataList;
		}
		/**
		 *Below code Written is for Tyndale to get Group of tax based on Ship Methods. *Reference- Chetan Sandesh
		 */
		public double getGroupOfTax(LinkedHashMap<String, Object> salesInputParameter,String customParamter) {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> getTaxDetails = new LinkedHashMap<String, Object>();
			Cimm2BCentralResponseEntity response = null;
			double totalTax =0.0;
			ArrayList<ProductsModel> lineItems = null;
			LinkedHashMap<String, ArrayList<ProductsModel>> cartItemDataList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
			try {
				session.removeAttribute("shipViaTax");
				LinkedHashMap<String, ArrayList<ProductsModel>> cartListData = getGroupedItemsInProductsData((ArrayList<ProductsModel>) salesInputParameter.get("itemList"));
				AddressModel overrideShipAddress = null;
				String wareHouse = CommonUtility.validateString(salesInputParameter.get("wareHousecode").toString());
				String poNumber = "";
				if (CommonUtility.validateString(salesInputParameter.get("poNumber").toString()).length() > 0) {
					poNumber = CommonUtility.validateString(salesInputParameter.get("poNumber").toString());
				}
				
				if (cartListData != null && cartListData.size() > 0) {
					for (Map.Entry<String, ArrayList<ProductsModel>> entry : cartListData.entrySet()) {
						lineItems = new ArrayList<ProductsModel>();
						for (ProductsModel cModel : entry.getValue()) {
							if (cModel.getMultipleShipVia().equalsIgnoreCase(entry.getKey())) {
								lineItems.add(cModel);
								cartItemDataList.put(CommonUtility.validateString(cModel.getMultipleShipVia()), lineItems);
							}
							
						}
						
					}
				}
				
				if (cartItemDataList != null && cartItemDataList.size() > 0) {
					for (Map.Entry<String, ArrayList<ProductsModel>> entry : cartItemDataList.entrySet()) {
						if (CommonUtility.validateString(customParamter).equalsIgnoreCase(entry.getKey())) {
							overrideShipAddress = (AddressModel) salesInputParameter.get("overrideShipAddress");
							response = AvalaraUtility.getInstance().getTax(entry.getValue(), overrideShipAddress, wareHouse,poNumber);
							System.out.println("overrideShipAddress for tax to shiptoMe");
						}else {
							String wareHseCode ="";
							if(session!=null && session.getAttribute("wareHouseCode")!=null) {
							wareHseCode = CommonUtility.validateString((String)session.getAttribute("wareHouseCode"));
							}
							WarehouseModel wareHouseDetail = new WarehouseModel();
							if(CommonUtility.validateString(wareHseCode).length()>0) {
								overrideShipAddress = new AddressModel();
								wareHouseDetail = UsersDAO.getWareHouseDetailsByCode(wareHseCode);
								overrideShipAddress.setCity(wareHouseDetail.getCity());
								overrideShipAddress.setCountry(wareHouseDetail.getCountry());
								overrideShipAddress.setAddress1(wareHouseDetail.getAddress1());
								overrideShipAddress.setAddress2(wareHouseDetail.getAddress2());
								overrideShipAddress.setZipCode(wareHouseDetail.getZip());
								overrideShipAddress.setState(wareHouseDetail.getState());
							}
							response = AvalaraUtility.getInstance().getTax(entry.getValue(), overrideShipAddress, wareHouse,poNumber);
						}
						
						
						if (response != null && response.getData() != null) {
							TaxResponse taxresponse = (TaxResponse) response.getData();
							getTaxDetails.put(entry.getKey(), taxresponse.getTotalTax());
							System.out.println("ShipMethod:==="+entry.getKey()+"tax:==="+taxresponse.getTotalTax());
							totalTax += taxresponse.getTotalTax();
							getTaxDetails.put("totalTax", totalTax);
						}	
						}
					}
				session.setAttribute("shipViaTax", getTaxDetails);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return totalTax;
		}
		
		@SuppressWarnings("unchecked")
		/**
		 *Below code Written is for Tyndale to build shipVia list for each shipMethods. *Reference- Chetan Sandesh
		 */
		public LinkedHashMap<String, Cimm2BCentralShipVia> getCustomShipViaData(ArrayList<ProductsModel> orderDetails) {
			LinkedHashMap<String, Cimm2BCentralShipVia> shipViaData = new LinkedHashMap<String, Cimm2BCentralShipVia>();
			LinkedHashMap<String, Object> taxDetailsMap = new LinkedHashMap<String, Object>();
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String freightKey= CommonUtility.validateString((String)session.getAttribute("shipMethodKey"));
			taxDetailsMap = (LinkedHashMap<String, Object>) session.getAttribute("shipViaTax"); 
			LinkedHashMap<String, ArrayList<ProductsModel>> cartItemDataList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
			try {
				cartItemDataList = getGroupedItemsInProductsData(orderDetails);
				if (cartItemDataList != null && cartItemDataList.size() > 0) {
					for (Map.Entry<String, ArrayList<ProductsModel>> entry : cartItemDataList.entrySet()) {
						Cimm2BCentralShipVia shipVias = new Cimm2BCentralShipVia();
						double subTotal=0.0;
						double Tax=0.0;
						double freight =0.0; 
						String shipToAccNo= "";
						for (ProductsModel cModel : entry.getValue()) {
							if(cModel.getMultipleShipVia().equalsIgnoreCase(entry.getKey())) {
								subTotal += cModel.getPrice()*cModel.getQty();
								if(taxDetailsMap!=null && taxDetailsMap.size()>0 && taxDetailsMap.get(entry.getKey())!=null) {
								Tax=(double) taxDetailsMap.get(entry.getKey());
								}
								if(freightKey.equalsIgnoreCase(entry.getKey()) && session!=null && session.getAttribute("totalCartFrieghtCharges")!=null) {
									freight =(double)session.getAttribute("totalCartFrieghtCharges");
								}
								shipVias.setFreight(freight);
								shipVias.setTax(Tax);
								shipVias.setOrderSubTotal(subTotal);
								shipVias.setOrderTotal(subTotal+Tax+freight);
								
								if(entry.getKey()!=null && session!=null && session.getAttribute("wareHouseCode")!=null) {
									shipToAccNo = (getWarehouseCustomFieldByWhrsCode((String)session.getAttribute("wareHouseCode"),"SHIPTO_ACCOUNT_NUMBER"));
									shipVias.setAccountNumber(shipToAccNo);
								}
							}
						}
						shipViaData.put(entry.getKey(), shipVias);
						}
					}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			return shipViaData;
		}
		/**
		 *Below code Written is for Tyndale to get warehouse custom fields based on custom field name and warehouse code. *Reference- Chetan Sandesh
		 */
		public static String getWarehouseCustomFieldByWhrsCode(String wareHouseCode, String customFieldName){
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet  rs =null;
			String warehseCustFieldValue = "";
			
		
			try
			{
				conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("getWhseCustFieldValByCode");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,wareHouseCode);
				pstmt.setString(2,customFieldName);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()){
					warehseCustFieldValue = rs.getString("TEXT_FIELD_VALUE");
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
			return warehseCustFieldValue;
		}

		public void setRequest(HttpServletRequest request) {
			this.request = request;
		}

		public HttpServletRequest getRequest() {
			return request;
		}

		public static ArrayList<ShipVia> getSiteShipViaList() {
			return SiteShipViaList;
		}

		public static void setSiteShipViaList(ArrayList<ShipVia> siteShipViaList) {
			SiteShipViaList = siteShipViaList;
		}

		public int getDefaultBillToId() {
			return defaultBillToId;
		}

		public void setDefaultBillToId(int defaultBillToId) {
			this.defaultBillToId = defaultBillToId;
		}

		public int getDefaultShipToId() {
			return defaultShipToId;
		}

		public void setDefaultShipToId(int defaultShipToId) {
			this.defaultShipToId = defaultShipToId;
		}

		public UsersModel getBillAddress() {
			return billAddress;
		}

		public void setBillAddress(UsersModel billAddress) {
			this.billAddress = billAddress;
		}

		public UsersModel getShipAddress() {
			return shipAddress;
		}

		public void setShipAddress(UsersModel shipAddress) {
			this.shipAddress = shipAddress;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public String getShipToStoreWarehouseCode(String warehouse) {
			String dcNumber = "";
			try
			{
				UserManagement usersObj = new UserManagementImpl();
				dcNumber = usersObj.getShipToStoreWarehouseCode(warehouse);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return dcNumber;
		}
		public String getCustomShipViaDescription(HttpSession session,Cimm2BCentralShipVia orderResponseGetShipVia) {
			String ShipViaDescription = "";
			try
			{
				if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.ship.method.name")).length()>0 &&  orderResponseGetShipVia.getShipViaCode().equalsIgnoreCase(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.ship.method.name")))) {
					ShipViaDescription = orderResponseGetShipVia.getShipViaDescription();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return ShipViaDescription;
		}
		public int getItemsResultCount(String keyWord,int subsetId,int generalSubset,int fromRow,int toRow,String requestType,int psid,int npsid,int treeId,int levelNo,String attrFilterList,String brandId, String sessionId,String sortBy, int resultPerPage, String narrowKeyword, int buyingCompanyId,String userName,String userToken,String entityId,int userId,String homeTeritory,String type, String wareHousecode,String customerId,String customerCountry,boolean isCategoryNav,boolean exactSearch,String viewFrequentlyPurcahsedOnly, String clearanceFlag, String wareHouseItems) {
			int resultCount = 0;
			HashMap<String, ArrayList<ProductsModel>> searchResultList = ProductHunterSolr.searchNavigation(keyWord, subsetId, generalSubset, 0, toRow, requestType, psid, npsid, treeId, levelNo,attrFilterList,brandId,sessionId,sortBy,12,narrowKeyword,buyingCompanyId,userName,userToken,entityId,userId,homeTeritory,type,wareHousecode,customerId,customerCountry,isCategoryNav,exactSearch,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHouseItems);
    		if(searchResultList!=null && searchResultList.get("itemList")!=null) {
    			ArrayList<ProductsModel> itemLevelFilterData = searchResultList.get("itemList");
    			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0) {
    				if(itemLevelFilterData.get(0).getItemResultCount()>0){
    					resultCount = itemLevelFilterData.get(0).getItemResultCount();
		 			}else{
		 				resultCount = itemLevelFilterData.get(0).getResultCount();
		 			}
    			}
    		}
    		return resultCount;
		}
		@SuppressWarnings("unchecked")
		// To check t&c login warehouse retain: Ref: Shamith 
		public int checkItemsFordifferentLocation(int userId,String sessionId,HttpSession session,String flag) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			PreparedStatement pstmt1 = null;
			PreparedStatement pstmt2 = null;
			ResultSet  rs =null;
			int count = 0;
			String afterLoginStore = "";
			String beforeLoginStore = "";
			Boolean shipToMeItem = false;
			ArrayList<ProductsModel> userCartDetail= new ArrayList<ProductsModel>();
			ArrayList<ProductsModel> sessionCartDetail= new ArrayList<ProductsModel>();
			ArrayList<ProductsModel> cartDetails= null;
			try
			{
				if(CommonUtility.validateString(flag).isEmpty()) {
					cartDetails =  ProductsDAO.selectFromCartWithUserIdOrSessonId(session,userId, sessionId);
					if(cartDetails.size()>0) {
						for(ProductsModel splitCartDetails: cartDetails) {
							if(splitCartDetails.getUserId() == 1) {
								sessionCartDetail.add(splitCartDetails);
							}
							else {
								userCartDetail.add(splitCartDetails);
							}
						}
						if(sessionCartDetail.size()>0 && userCartDetail.size()>0) {
							for(ProductsModel userCartDetails:userCartDetail){
								for(ProductsModel sessionCartDetails:sessionCartDetail) {
									if(sessionCartDetails.getItemId() == userCartDetails.getItemId()) {
										ProductsDAO.deleteFromCart(userId, userCartDetails.getCartId());
									}
								}
							}
						}
				}
				}
				conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("checkForItemsWithDifferentLocation");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,userId);
				pstmt.setString(2,sessionId);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					if(CommonUtility.validateString(rs.getString("ITEM_LEVEL_SHIPVIA")).length()>0 && CommonUtility.validateString(rs.getString("ITEM_LEVEL_SHIPVIA")).equalsIgnoreCase("SHIPTOME")) {
						shipToMeItem = true;
					}
					if(CommonUtility.validateString(rs.getString("ITEM_LEVEL_SHIPVIA")).length()>0 && (CommonUtility.validateString(rs.getString("ITEM_LEVEL_SHIPVIA")).equalsIgnoreCase("SHIPTOSTORE") || CommonUtility.validateString(rs.getString("ITEM_LEVEL_SHIPVIA")).equalsIgnoreCase("STOREPICKUP"))) {
						if(rs.getInt("USER_ID") > 1){
							if(afterLoginStore.isEmpty()) {
								count += 1;
							}
							afterLoginStore = CommonUtility.validateString(rs.getString("STORE"));
						}
						if(rs.getInt("USER_ID") == 1 && CommonUtility.validateString(rs.getString("STORE")).length()>0){
							if(beforeLoginStore.isEmpty()) {
								count += 1;
							}
							beforeLoginStore = CommonUtility.validateString(rs.getString("STORE"));
						}
					}
				}
				if(!CommonUtility.validateString(flag).isEmpty()) {
					if(CommonUtility.validateString(flag).equals("1")) {
						// if 1 update guest cart warehouse to old user cart warehouse
						if(CommonUtility.validateString(beforeLoginStore).length()>0) {
							ProductsDAO.updateCart(userId, sessionId);
							
							String sql1 = PropertyAction.SqlContainer.get("updateStoreLocation");
				        	pstmt1 = conn.prepareStatement(sql1);
				        	pstmt1.setString(1, "SHIPTOSTORE,"+beforeLoginStore);
							pstmt1.setInt(2,userId);
							pstmt1.setString(3, "SHIPTOSTORE");
							int count1 = pstmt1.executeUpdate();
							
							String sql2 = PropertyAction.SqlContainer.get("updateStoreLocation");
				        	pstmt2 = conn.prepareStatement(sql2);
				        	pstmt2.setString(1, "STOREPICKUP,"+beforeLoginStore);
							pstmt2.setInt(2,userId);
							pstmt2.setString(3, "STOREPICKUP");
							int count2 = pstmt2.executeUpdate();
							
							count = count1>count2?count1:count2;
							
							session.setAttribute("wareHouseCode", beforeLoginStore);
							WarehouseModel warehouseDetails = UsersDAO.getWareHouseDetailsByCode(beforeLoginStore);
							session.setAttribute("wareHouseName", warehouseDetails.getWareHouseName());
							
							LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
							contentObject = ProductsDAO.getShoppingCartDao(session, contentObject);
							if(contentObject!=null && contentObject.get("productListData")!=null){
								ArrayList<ProductsModel> itemDetailList = (ArrayList<ProductsModel>) contentObject.get("productListData");
								boolean itemFound = false;
								if(itemDetailList!=null && itemDetailList.size()>0){
									for(ProductsModel item: itemDetailList) {
										itemFound = false; 
										ArrayList<Cimm2BCentralShipMethodQuantity> shipMethodQuantity = item.getShipMethodQuantity();
										if(shipMethodQuantity!=null && shipMethodQuantity.size()>0) {
											for(Cimm2BCentralShipMethodQuantity shipMethod: shipMethodQuantity) {
												if(shipMethod.getShipMethod().equalsIgnoreCase(item.getMultipleShipVia()) && item.getPrice()>0) {
													itemFound = true;
												}
											}
										}
										if(itemFound==false && item.getProductListId()>0) {
											//delete the item from cart if ship method is not available for selected location
											ProductsDAO.deleteFromCart(userId, item.getProductListId());
										}
									}
								}
							}
						}
				    	
					}else if(CommonUtility.validateString(flag).equals("2")) {
						// if 2 retain old user cart warehouse and update guest selected warehouse with old one 
						if(CommonUtility.validateString(afterLoginStore).length()>0) {
							ProductsDAO.updateCart(userId, sessionId);
							
							String sql1 = PropertyAction.SqlContainer.get("updateStoreLocation");
				        	pstmt1 = conn.prepareStatement(sql1);
				        	pstmt1.setString(1, "SHIPTOSTORE,"+afterLoginStore);
							pstmt1.setInt(2,userId);
							pstmt1.setString(3, "SHIPTOSTORE");
							int count1 = pstmt1.executeUpdate();
							
							String sql2 = PropertyAction.SqlContainer.get("updateStoreLocation");
				        	pstmt2 = conn.prepareStatement(sql2);
				        	pstmt2.setString(1, "STOREPICKUP,"+afterLoginStore);
							pstmt2.setInt(2,userId);
							pstmt2.setString(3, "STOREPICKUP");
							int count2 = pstmt2.executeUpdate();
							
							count = count1>count2?count1:count2;
							
							session.setAttribute("wareHouseCode", afterLoginStore);
							WarehouseModel warehouseDetails = UsersDAO.getWareHouseDetailsByCode(afterLoginStore);
							session.setAttribute("wareHouseName", warehouseDetails.getWareHouseName());
							
							LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
							contentObject = ProductsDAO.getShoppingCartDao(session, contentObject);
							if(contentObject!=null && contentObject.get("productListData")!=null){
								ArrayList<ProductsModel> itemDetailList = (ArrayList<ProductsModel>) contentObject.get("productListData");
								boolean itemFound = false;
								if(itemDetailList!=null && itemDetailList.size()>0){
									for(ProductsModel item: itemDetailList) {
										itemFound = false; 
										ArrayList<Cimm2BCentralShipMethodQuantity> shipMethodQuantity = item.getShipMethodQuantity();
										if(shipMethodQuantity!=null && shipMethodQuantity.size()>0) {
											for(Cimm2BCentralShipMethodQuantity shipMethod: shipMethodQuantity) {
												if(shipMethod.getShipMethod().equalsIgnoreCase(item.getMultipleShipVia()) && item.getPrice()>0) {
													itemFound = true;
												}
											}
										}
										if(itemFound==false && item.getProductListId()>0) {
											//delete the item from cart if ship method is not available for selected location
											ProductsDAO.deleteFromCart(userId, item.getProductListId());
										}
									}
								}
							}
						}
					}else {
						// if 3 clear login user cart & keep before login cart
						if(CommonUtility.validateString(beforeLoginStore).length()>0) {
							session.setAttribute("wareHouseCode", beforeLoginStore);
							WarehouseModel warehouseDetails = UsersDAO.getWareHouseDetailsByCode(beforeLoginStore);
							session.setAttribute("wareHouseName", warehouseDetails.getWareHouseName());
						}
						ProductsDAO.clearCartCustom(userId,"STORE");
						ProductsDAO.updateCart(userId, sessionId);
						count = 1;
					}
					
					
				}else {
					if((afterLoginStore.equalsIgnoreCase(beforeLoginStore) || afterLoginStore.isEmpty()) && !beforeLoginStore.isEmpty()) {
						session.setAttribute("wareHouseCode", beforeLoginStore);
						WarehouseModel warehouseDetails = UsersDAO.getWareHouseDetailsByCode(beforeLoginStore);
						session.setAttribute("wareHouseName", warehouseDetails.getWareHouseName());
						session.setAttribute("defaultsWrhseSet", "Y");
						session.setAttribute("shipViaFlag", "Y");
						ProductsDAO.updateCart(userId, sessionId);
						count = 0;
					}
					if(beforeLoginStore.isEmpty() && shipToMeItem == true) {
						ProductsDAO.updateCart(userId, sessionId);
					}
					if(beforeLoginStore.isEmpty() && !afterLoginStore.isEmpty()) {
						session.setAttribute("wareHouseCode", afterLoginStore);
						WarehouseModel warehouseDetails = UsersDAO.getWareHouseDetailsByCode(afterLoginStore);
						session.setAttribute("wareHouseName", warehouseDetails.getWareHouseName());
						session.setAttribute("defaultsWrhseSet", "Y");
						session.setAttribute("shipViaFlag", "Y");
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally{
		    	ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBPreparedStatement(pstmt1);
				ConnectionManager.closeDBPreparedStatement(pstmt2);
				ConnectionManager.closeDBConnection(conn);
			}
			return count;
		}
		
		public String addressLine1Combined(AddressModel address) {
			String address1Combined = null;
			try
			{
				address1Combined = address.getAddress1() + " " +address.getCity() + " " +address.getState() +" " +address.getZipCode();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return address1Combined;
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
		
		@SuppressWarnings("unchecked")
		public SalesModel createQuote(LinkedHashMap<String, Object> createQuoteInput) {
			SalesModel quoteInfo = new SalesModel();
			ArrayList<Cimm2BCentralLineItem> lineItems = new ArrayList<Cimm2BCentralLineItem>();
			try{
				String warehouseCode = (String) createQuoteInput.get("wareHousecode");
				String customerErpId = (String) createQuoteInput.get("customerId");
				String shippingInstruction = (String) createQuoteInput.get("shippingInstruction");
				String orderNotes = (String) createQuoteInput.get("orderNotes");
				String poNumber = (String) createQuoteInput.get("poNumber");
				String gasPoNumber = (String) createQuoteInput.get("gasPoNumber");
				String buyingCompanyId = (String) createQuoteInput.get("buyingCompanyId"); 
				String shipVia = (String) createQuoteInput.get("shipVia");
				String shipViaDisplay = (String) createQuoteInput.get("shipViaDisplay");
				String handlingListItems = (String) createQuoteInput.get("handlingListItems");
				String deliveryListItems = (String) createQuoteInput.get("deliveryListItems");
				String errorMessageToDisplay = (String) createQuoteInput.get("errorMsgToDisplay");
				String reqDate = (String) createQuoteInput.get("reqDate");
				double freightCharges = CommonUtility.validateDoubleNumber(createQuoteInput.get("totalCartFrieghtCharges").toString());
				double orderTotal = 0;
		
				String branchCode = ProductsDAO.getWarehouseCustomField(CommonUtility.validateNumber(buyingCompanyId), "LOCATION_CODE");
		
				UsersModel billAddress = (UsersModel) createQuoteInput.get("billAddress");
				UsersModel shipAddress = (UsersModel) createQuoteInput.get("shipAddress");

				ArrayList<ProductsModel> itemList = (ArrayList<ProductsModel>) createQuoteInput.get("itemList");
				if(itemList!=null && itemList.size()>0){
					for(ProductsModel itemModel : itemList){
						Cimm2BCentralLineItem lineItem = new Cimm2BCentralLineItem();
						lineItem.setItemId(itemModel.getPartNumber());
						lineItem.setPartNumber(itemModel.getPartNumber());
						lineItem.setQty(itemModel.getQty()); 
						lineItem.setUom(itemModel.getUom());
						lineItem.setManufacturer(itemModel.getManufacturerName());
						if(itemModel.getPromoPrice()>0) {
							lineItem.setUnitPrice(itemModel.getPromoPrice());
						}else if(itemModel.getImapPrice()>0) {
							lineItem.setUnitPrice(itemModel.getImapPrice());
						}else {
							lineItem.setUnitPrice(itemModel.getUnitPrice());
						}
						lineItem.setExtendedPrice(itemModel.getTotal());
						lineItem.setCustomerPartNumber(itemModel.getCustomerPartNumber());
						lineItem.setLineItemComment(itemModel.getLineItemComment());
						lineItem.setShippingBranch(warehouseCode);	
						lineItem.setExtendedPrice(itemModel.getTotal());
						lineItem.setShortDescription(itemModel.getShortDesc()!=null?itemModel.getShortDesc():".");
						UpsFreight shipViaInfo = new UpsFreight();
						shipViaInfo.setShippingCarrierType("UPS");
						String shipViaServiceCode = CommonUtility.validateString(String.valueOf(createQuoteInput.get("shipViaServiceCode")));
						if(shipViaServiceCode.length()==1 && shipViaServiceCode.length()<2) {
							shipViaServiceCode = "0"+shipViaServiceCode;
						}
						shipViaInfo.setServiceCode(shipViaServiceCode);
						shipViaInfo.setServiceName(shipViaDisplay);
						PackageInfo itemPackageInfo = new PackageInfo();
						itemPackageInfo.setPackageCode("02");
						itemPackageInfo.setPackageInstruction("Package");
						String defaultUom = CommonDBQuery.getSystemParamtersList().get("DEFAULT_CART_WEIGHT_UOM");
						itemPackageInfo.setUom(defaultUom);
						double weight=0;
						if(itemModel.getWeight()>0.0){
							weight = itemModel.getWeight()*itemModel.getQty();
						}else{
							weight = CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("DEFAULT_ITEM_WEIGHT"))*itemModel.getQty();
						}
						itemPackageInfo.setWeight(CommonUtility.validateParseDoubleToString(weight));
						ArrayList<PackageInfo> itemPackageInfoList = new ArrayList<PackageInfo>();
						itemPackageInfoList.add(itemPackageInfo);
						shipViaInfo.setItemPackageInfo(itemPackageInfoList);
						lineItem.setShipViaInfo(shipViaInfo);
						Cimm2BCentralSubOrderCriteria subOrderCriteria = new Cimm2BCentralSubOrderCriteria();
						JSONParser parser = new JSONParser();
						try {
							if(itemModel.getAdditionalProperties()!=null) {
								JSONObject json = (JSONObject) parser.parse(itemModel.getAdditionalProperties());
								if(json.get("supplierCode")!=null) {
									subOrderCriteria.setCode((String)json.get("supplierCode"));
								}
								if(json.get("storeSku")!=null) {
									lineItem.setStorePartNumber((String)json.get("storeSku"));
								}
								if(json.get("supplierSku")!=null) {
									lineItem.setSupplierPartNumber((String)json.get("supplierSku"));
								}
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
						subOrderCriteria.setValue(itemModel.getMultipleShipVia());
						lineItem.setSubOrderCriteria(subOrderCriteria);
						lineItems.add(lineItem);
						orderTotal = orderTotal + lineItem.getExtendedPrice();
					}

					Cimm2BCentralAddress cimm2BCentralBillingAddress = Cimm2BCentralClient.getInstance().ecomUserModelToCimm2BCentralAddress(billAddress);
					Cimm2BCentralAddress cimm2BCentralShipingAddress = Cimm2BCentralClient.getInstance().ecomUserModelToCimm2BCentralAddress(shipAddress);

					Cimm2BCentralOrder order = new Cimm2BCentralOrder();
					order.setOrderedBy(CommonUtility.validateString((String)createQuoteInput.get("orderedBy")));
					order.setReleaseNumber(CommonUtility.validateString((String)createQuoteInput.get("customerReleaseNumber")));
					Calendar cal = Calendar.getInstance();
				    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				    String orderDate = sdf.format(cal.getTime());
					order.setOrderDate(orderDate);
					Cimm2BCentralBillAndShipToContact shipToContact = new Cimm2BCentralBillAndShipToContact();
					shipToContact.setFirstName(CommonUtility.validateString(shipAddress.getFirstName()));
					shipToContact.setLastName(CommonUtility.validateString(shipAddress.getLastName()));
					shipToContact.setPrimaryPhoneNumber(CommonUtility.validateString(shipAddress.getPhoneNo()));
					shipToContact.setPrimaryEmailAddress(CommonUtility.validateString(shipAddress.getEmailAddress()));
					shipToContact.setCarrierId(shipVia);
					order.setShipToContact(shipToContact);
					order.setCustomerERPId(customerErpId);
					order.setBranchCode(branchCode);
					order.setWarehouseLocation(warehouseCode);
					order.setLineItems(lineItems);
					order.setCustomerPoNumber(poNumber);
					order.setShippingInstruction(shippingInstruction);
					order.setOrderComment(orderNotes);
					order.setBillingAddress(cimm2BCentralBillingAddress);
					order.setShippingAddress(cimm2BCentralShipingAddress);
					order.setGasPoNumber(gasPoNumber);
					order.setFreight(freightCharges);
					order.setOrderTotal(orderTotal + freightCharges);
					order.setRequiredDate(reqDate);
					order.setOtherAmount(freightCharges);
					order.setOrderSubTotal(orderTotal);
					order.setFreightOut(freightCharges);
					order.setSalesLocationId(warehouseCode);
					order.setUserERPId(CommonUtility.validateString(String.valueOf(createQuoteInput.get("userId"))));
					order.setOrderStatus("BIDS");
					order.setPreAuthorize(false);
					order.setImportAsQuote(false);
					order.setAnonymous("N");
					order.setCreateShippingAndHandlingAsNewLineItem(true);
					if(CommonUtility.validateString(shipVia).length() > 0){
						Cimm2BCentralShipVia cimm2bCentralShipVia = new Cimm2BCentralShipVia();
						cimm2bCentralShipVia.setShipViaErpId(shipVia);
						cimm2bCentralShipVia.setShipViaCode(shipVia);
						cimm2bCentralShipVia.setShipViaDescription(shipViaDisplay);
						ArrayList<Cimm2BCentralShipVia> shipviaArray = new ArrayList<Cimm2BCentralShipVia>();
						shipviaArray.add(cimm2bCentralShipVia);
						order.setShipVia(shipviaArray);
					}
					String CREATE_SHELL_ORDER = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_SHELL_ORDER_API"));			
					Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_SHELL_ORDER, HttpMethod.POST, order, Cimm2BCentralOrder.class);

					Cimm2BCentralOrder orderResponse = null;
					if(orderResponseEntity!=null && orderResponseEntity.getData() != null){
						orderResponse = (Cimm2BCentralOrder) orderResponseEntity.getData();
						orderResponse.setStatus(orderResponseEntity.getStatus());
						if(orderResponse != null && ((CommonUtility.validateString(orderResponse.getOrderNumber()).length()> 0) || (CommonUtility.validateString(orderResponse.getCartId()).length()>0))){
							quoteInfo.setExternalCartId(orderResponse.getCartId());
							quoteInfo.setOrderNum(orderResponse.getOrderNumber());
							quoteInfo.setErpOrderNumber(orderResponse.getOrderERPId());
							quoteInfo.setQuoteNumber(orderResponse.getOrderNumber());
							quoteInfo.setTax(orderResponse.getTaxAmount());
							if(orderResponse.getFreight()!=null){
								quoteInfo.setFreight(orderResponse.getFreight());
							}
							if(orderResponse.getOrderTotal()>0){
								quoteInfo.setTotal(orderResponse.getOrderTotal() + freightCharges);
							}
							quoteInfo.setSubtotal(orderResponse.getOrderSubTotal());
							
							ArrayList<Cimm2BCentralLineItem> cimm2bCentralLineItem = orderResponse.getLineItems();
							if(cimm2bCentralLineItem != null && cimm2bCentralLineItem.size() > 0){
								double handlingCharges = 0.0;
								double deliveryCharges = 0.0;
								ArrayList<ProductsModel> nonCatalogItems = new ArrayList<ProductsModel>();
								ArrayList<SalesModel> lineItemsFromResponse = new ArrayList<SalesModel>();
								HashMap<String, SalesModel> lineItemsFromResponseCheckNonCatalogItems = new HashMap<String, SalesModel>();
								for (int i = 0; i < cimm2bCentralLineItem.size(); i++) {

									SalesModel model = new SalesModel();
									model.setLineNumber(cimm2bCentralLineItem.get(i).getLineNumber());
									model.setLineItemComment(cimm2bCentralLineItem.get(i).getLineItemComment());
									model.setItemId(CommonUtility.validateNumber(cimm2bCentralLineItem.get(i).getItemId()!=null?cimm2bCentralLineItem.get(i).getItemId():""));
									model.setPartNumber(cimm2bCentralLineItem.get(i).getPartNumber());
									model.setCustomerPartNumber(cimm2bCentralLineItem.get(i).getCustomerPartNumber());
									model.setShortDesc(cimm2bCentralLineItem.get(i).getShortDescription()!=null?cimm2bCentralLineItem.get(i).getShortDescription():"");
									model.setUnitPrice(cimm2bCentralLineItem.get(i).getUnitPrice());
									model.setExtPrice(cimm2bCentralLineItem.get(i).getExtendedPrice());
									model.setQtyordered(cimm2bCentralLineItem.get(i).getQty());
									model.setQtyShipped(cimm2bCentralLineItem.get(i).getQtyShipped().intValue());
									model.setUom(cimm2bCentralLineItem.get(i).getUom());
									model.setQtyUom(CommonUtility.validateString(cimm2bCentralLineItem.get(i).getUomQty()!=null?cimm2bCentralLineItem.get(i).getUomQty():"0"));
									model.setManufacturer(cimm2bCentralLineItem.get(i).getManufacturer());
									model.setManufacturerPartNumber(cimm2bCentralLineItem.get(i).getManufacturer()!=null?cimm2bCentralLineItem.get(i).getManufacturer():"");

									lineItemsFromResponse.add(model);
									lineItemsFromResponseCheckNonCatalogItems.put(cimm2bCentralLineItem.get(i).getPartNumber(), model);
								}

								for (ProductsModel orderItem : itemList) {
									lineItemsFromResponseCheckNonCatalogItems.remove(orderItem.getPartNumber());
								}
								
								if(handlingListItems.length() > 0 || deliveryListItems.length() > 0){
									List<String> handlingListItemsArray = Arrays.asList(handlingListItems.split(","));
									List<String> deliveryListItemsArray = Arrays.asList(deliveryListItems.split(","));

									Iterator<Entry<String, SalesModel>> it = lineItemsFromResponseCheckNonCatalogItems.entrySet().iterator();
									while (it.hasNext()) {
										Map.Entry<String, SalesModel> pair = (Map.Entry<String, SalesModel>)it.next();

										if(handlingListItemsArray.contains(pair.getValue().getPartNumber())){
											handlingCharges = handlingCharges + pair.getValue().getExtPrice();
										}else if(deliveryListItemsArray.contains(pair.getValue().getPartNumber())){
											deliveryCharges = deliveryCharges + pair.getValue().getExtPrice();
										}

										ProductsModel nonCatalogItem = new ProductsModel();

										nonCatalogItem.setLineItemComment(pair.getValue().getLineItemComment());
										nonCatalogItem.setItemId(pair.getValue().getItemId());
										nonCatalogItem.setPartNumber(pair.getValue().getPartNumber());
										nonCatalogItem.setCustomerPartNumber(pair.getValue().getCustomerPartNumber());
										nonCatalogItem.setShortDesc(pair.getValue().getShortDesc()!=null? pair.getValue().getShortDesc():"");
										nonCatalogItem.setUnitPrice(pair.getValue().getUnitPrice());
										nonCatalogItem.setExtendedPrice( pair.getValue().getExtPrice());
										nonCatalogItem.setQty(pair.getValue().getQtyShipped());
										nonCatalogItem.setUom(pair.getValue().getUom());
										nonCatalogItem.setQtyUOM(CommonUtility.validateString(pair.getValue().getQtyUom()!=null? pair.getValue().getQtyUom():"0"));
										nonCatalogItem.setManufacturerName(pair.getValue().getManufacturer());
										nonCatalogItem.setManufacturerPartNumber(pair.getValue().getManufacturer()!=null? pair.getValue().getManufacturer():"");

										nonCatalogItems.add(nonCatalogItem);

										it.remove(); // avoids a ConcurrentModificationException
									}

								}

								quoteInfo.setDeliveryCharge(deliveryCharges);
								quoteInfo.setHandling(handlingCharges);
								quoteInfo.setNonCatalogItem(nonCatalogItems);
								quoteInfo.setOrderList(lineItemsFromResponse);
							}

							String GET_CUSTOMER_CARDS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_CARDS")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  customerErpId;
							Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_CUSTOMER_CARDS, HttpMethod.GET, null, Cimm2BCentralCustomerCard.class);
							List<Cimm2BCentralCustomerCard> customerDetails = null;

							if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  
								ArrayList<CreditCardModel> creditCardList = new ArrayList<CreditCardModel>();
								customerDetails = (List<Cimm2BCentralCustomerCard>) customerDetailsResponseEntity.getData();

								if(customerDetails != null && customerDetails.size() > 0){
									for(Cimm2BCentralCustomerCard customerCard : customerDetails){
										CreditCardModel creditCardModel = new CreditCardModel();
										creditCardModel.setCardHolder(customerCard.getCardHolderName());
										creditCardModel.setCreditCardType(customerCard.getCreditCardType());
										creditCardModel.setCreditCardNumber(customerCard.getCreditCardNumber());
										creditCardModel.setCreditCardCvv2VrfyCode(customerCard.getCvv());
										creditCardModel.setElementPaymentAccountId(customerCard.getPaymentAccountId());
										creditCardModel.setExpDate(customerCard.getExpiryDate());

										creditCardList.add(creditCardModel);
									}
									quoteInfo.setCreditCardList(creditCardList);
								}
							}
						}else{
							if(orderResponse.getTaxAmount() != null && orderResponse.getTaxAmount() > 0.0){
								quoteInfo.setTax(orderResponse.getTaxAmount());
							}else{
								quoteInfo.setTax(orderResponse.getSalesTax()!=null?orderResponse.getSalesTax():0.0);
							}
							if(orderResponse.getFreight()!=null){
								quoteInfo.setFreight(orderResponse.getFreight());
							}
							if(orderResponse.getOrderTotal()>0){
								quoteInfo.setTotal(orderResponse.getOrderTotal() + freightCharges);
							}
							quoteInfo.setSubtotal(orderResponse.getOrderSubTotal());
							quoteInfo.setCimm2BCentralShipVia(orderResponse.getShipVia());
							freightCharges = 0;
							double tax = 0;
							double subTotal = 0;
							orderTotal = 0;
							if(orderResponse.getShipVia().size()>0) {
								for(Cimm2BCentralShipVia c2bcshipVia: orderResponse.getShipVia()) {
									freightCharges += c2bcshipVia.getFreight();
									tax += c2bcshipVia.getTax();
									subTotal += c2bcshipVia.getOrderSubTotal();
									orderTotal += c2bcshipVia.getOrderTotal();
								}
							}
							quoteInfo.setFreight(freightCharges);
							quoteInfo.setTax(tax);
							quoteInfo.setSubtotal(subTotal);
							quoteInfo.setTotal(orderTotal);
							quoteInfo.setCimm2BCentralShipVia(orderResponse.getShipVia());
						}
					}else{
						quoteInfo.setStatusDescription(errorMessageToDisplay);
						if(orderResponseEntity.getStatus()!=null && orderResponseEntity.getStatus().getMessage()!=null) {
							quoteInfo.setStatusDescription(orderResponseEntity.getStatus().getMessage());
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return quoteInfo;
		}

		@SuppressWarnings("unchecked")
		public SalesModel submitOrderToERP(SalesOrderManagementModel salesOrderInput) {
			SalesModel orderDetail = new SalesModel();
			Connection conn = null;
			try
			{	
				HttpSession session = salesOrderInput.getSession();
				String wareHousecode = CommonUtility.validateString((String) session.getAttribute("wareHouseCode"));
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PREFIX_ZEROS_TO_WAREHOUSE_CODE")).equals("Y")) {
					wareHousecode = CommonUtility.prefixWarehouseCode(wareHousecode);
				}
				
				String salesLocationId = (String) session.getAttribute("salesLocationId");
				String customerErpId = CommonUtility.validateString((String) session.getAttribute("buyingCompanyId"));
				ArrayList<Cimm2BCentralShipVia> cimm2BCentralShipViaFromQuote = (ArrayList<Cimm2BCentralShipVia>) session.getAttribute("quoteResponseCimm2BCentralShipVia");
				String orderNumber = "";
				int backOrderSeqNumber = 0;
				double subTotal=0.0;
				String handlingListItems = "";
				String deliveryListItems = "";
				String errorMessageToDisplay = "";
				ArrayList<Cimm2BCentralLineItem> lineItems = new ArrayList<Cimm2BCentralLineItem>();
				SalesModel quoteResponse = salesOrderInput.getQuoteResponse();
				Calendar cal = Calendar.getInstance();
			    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			    String orderDate = sdf.format(cal.getTime());
				LinkedHashMap<String, Cimm2BCentralShipVia> shipMap = new LinkedHashMap<String, Cimm2BCentralShipVia>();
				ArrayList<ProductsModel> orderDetails = SalesDAO.getOrderDetails(salesOrderInput.getSession(), salesOrderInput.getOrderId());
				 if(CommonDBQuery.getSystemParamtersList().get("ORDER_SUBMIT_V2") !=null &&CommonDBQuery.getSystemParamtersList().get("ORDER_SUBMIT_V2").equalsIgnoreCase("Y")){
					orderDetails = salesOrderInput.getOrderItems();
				 }
				if(salesOrderInput.getQuoteNumber()!=null && quoteResponse != null){
					int i =1;
					ArrayList<SalesModel> quoteResponseOrderList = quoteResponse.getOrderList();
					if(quoteResponseOrderList != null && quoteResponseOrderList.size() > 0){
						for(SalesModel item : quoteResponseOrderList){
							Cimm2BCentralLineItem cimm2bCentralLineItem = new Cimm2BCentralLineItem();
							cimm2bCentralLineItem.setLineNumber(item.getLineNumber());
							cimm2bCentralLineItem.setPartNumber(item.getPartNumber());
							cimm2bCentralLineItem.setQty(item.getQtyordered());
							cimm2bCentralLineItem.setLineIdentifier(i++);
							cimm2bCentralLineItem.setUom(item.getUom() != null ? item.getUom().trim() : "");
							cimm2bCentralLineItem.setManufacturer(item.getManufacturer());
							cimm2bCentralLineItem.setShortDescription(item.getShortDesc()!=null?item.getShortDesc():".");
							cimm2bCentralLineItem.setLineItemComment(item.getLineItemComment());
							cimm2bCentralLineItem.setPage_title(item.getPageTitle());
							if(salesOrderInput.getTaxExempt()!=null && salesOrderInput.getTaxExempt().equalsIgnoreCase("N")){
								cimm2bCentralLineItem.setCalculateTax(true);
							}else{
								cimm2bCentralLineItem.setCalculateTax(false);
							}
							if(CommonUtility.validateString(salesLocationId).length()>0){
								cimm2bCentralLineItem.setShippingBranch(salesLocationId);
							}else{
								cimm2bCentralLineItem.setShippingBranch(wareHousecode);	
							}
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
								cimm2bCentralLineItem.setShipMethod(item.getMultipleShipVia());
								if(item.getMultipleShipViaDesc().split(",").length > 1){
									cimm2bCentralLineItem.setShippingBranch(item.getMultipleShipViaDesc().split(",")[1]);
								}else{
									if(CommonUtility.validateString(salesLocationId).length()>0){
										cimm2bCentralLineItem.setShippingBranch(salesLocationId);
									}else{
										cimm2bCentralLineItem.setShippingBranch(wareHousecode);
									}
								}
							}
							lineItems.add(cimm2bCentralLineItem);
						}
					}
					String tempQuOrd = salesOrderInput.getQuoteNumber();
					String quoteOrder[] = tempQuOrd.split("-");
					orderNumber = quoteOrder[0];
					handlingListItems = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("handlingcharge.labels");
					deliveryListItems = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("deliverycharge.labels");
					errorMessageToDisplay = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("errormsgtodisplay.labels");
					backOrderSeqNumber = 0;
					if(quoteOrder != null && quoteOrder.length > 1){
						backOrderSeqNumber = CommonUtility.validateNumber(quoteOrder[1]);
					}
				}else{
					if(orderDetails != null && orderDetails.size() > 0){
						int i =1;
						for(ProductsModel item : orderDetails){
							Cimm2BCentralLineItem cimm2bCentralLineItem = new Cimm2BCentralLineItem();
							//cimm2bCentralLineItem.setLineNumber(item.getLineNumber());
							cimm2bCentralLineItem.setLineIdentifier(i++);
							cimm2bCentralLineItem.setPartNumber(item.getPartNumber());
							cimm2bCentralLineItem.setQty(item.getQty());
							cimm2bCentralLineItem.setManufacturer(item.getManufacturerName());
							cimm2bCentralLineItem.setLineItemComment(item.getLineItemComment());
							if(item.getPromoPrice()>0) {
								cimm2bCentralLineItem.setUnitPrice(item.getPromoPrice());
							}else if(item.getImapPrice()>0) {
								cimm2bCentralLineItem.setUnitPrice(item.getImapPrice());
							}else {
								cimm2bCentralLineItem.setUnitPrice(item.getPrice());
							}
							cimm2bCentralLineItem.setPage_title(item.getPageTitle());
							cimm2bCentralLineItem.setUomQty(item.getQtyUOM());
							cimm2bCentralLineItem.setItemId(item.getPartNumber());
							cimm2bCentralLineItem.setListPrice(item.getListPrice());
							cimm2bCentralLineItem.setShortDescription(item.getShortDesc()!=null?item.getShortDesc():".");
							cimm2bCentralLineItem.setProductCategory(item.getCategoryName()!=null?item.getCategoryName():"");
							/**
							 *Below code Written is for Turtle and Hughes for custom UOM *Reference- Nitesh
							 */
							if(CommonUtility.customServiceUtility()!=null) {
								double extndPrice = CommonUtility.customServiceUtility().getExtendedPrice(item.getPrice(),item.getQty(),item.getSaleQty(),item.getSalesQuantity());
								if(extndPrice > 0) {
									cimm2bCentralLineItem.setExtendedPrice(extndPrice);
									cimm2bCentralLineItem.setUom("");
								}else{
									cimm2bCentralLineItem.setUom(item.getUom() != null ? item.getUom().trim() : "");
									cimm2bCentralLineItem.setExtendedPrice(item.getTotal());
								}
							}
							else{
								cimm2bCentralLineItem.setUom(item.getUom() != null ? item.getUom().trim() : "");
								cimm2bCentralLineItem.setExtendedPrice(item.getTotal());
							}
							if(salesOrderInput.getTaxExempt()!=null && salesOrderInput.getTaxExempt().equalsIgnoreCase("N")){
								cimm2bCentralLineItem.setCalculateTax(true);
							}else{
								cimm2bCentralLineItem.setCalculateTax(false);
							}
							/**
							 *Below code Written is for Tyndale to get item level ship via details. *Reference- Chetan Sandesh
							 */
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
								cimm2bCentralLineItem.setShipMethod(item.getMultipleShipVia());
								if(item.getMultipleShipViaDesc()!=null){
									if(item.getMultipleShipViaDesc().split(",").length > 1)
									cimm2bCentralLineItem.setShippingBranch(item.getMultipleShipViaDesc().split(",")[1]);
								}else{
									if(CommonUtility.validateString(salesLocationId).length()>0){
										cimm2bCentralLineItem.setShippingBranch(salesLocationId);
									}else{
										cimm2bCentralLineItem.setShippingBranch(wareHousecode);
									}
								}
							}
							UnilogFactoryInterface serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
							if(serviceClass!=null && cimm2bCentralLineItem!=null) {
								Cimm2BCentralLineItem cimm2bCentralLineItemReArrange = serviceClass.submitSalesOrderItemList(cimm2bCentralLineItem);
								if(cimm2bCentralLineItemReArrange!=null){
									cimm2bCentralLineItem = cimm2bCentralLineItemReArrange;
								}
							}
							
							UpsFreight shipViaInfo = new UpsFreight();
							shipViaInfo.setShippingCarrierType("UPS");
							String shipViaServiceCode = salesOrderInput.getShipViaServiceCode();
							if(shipViaServiceCode.length()==1 && shipViaServiceCode.length()<2) {
								shipViaServiceCode = "0"+shipViaServiceCode;
							}
							shipViaInfo.setServiceCode(shipViaServiceCode);
							shipViaInfo.setServiceName(salesOrderInput.getShipViaDescription());
							PackageInfo itemPackageInfo = new PackageInfo();
							itemPackageInfo.setPackageCode("02");
							itemPackageInfo.setPackageInstruction("Package");
							String defaultUom = CommonDBQuery.getSystemParamtersList().get("DEFAULT_CART_WEIGHT_UOM");
							itemPackageInfo.setUom(defaultUom);
							double weight=0;
							if(item.getWeight()>0.0){
								weight = item.getWeight()*item.getQty();
							}else{
								weight = CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("DEFAULT_ITEM_WEIGHT"))*item.getQty();
							}
							itemPackageInfo.setWeight(CommonUtility.validateParseDoubleToString(weight));
							ArrayList<PackageInfo> itemPackageInfoList = new ArrayList<PackageInfo>();
							itemPackageInfoList.add(itemPackageInfo);
							shipViaInfo.setItemPackageInfo(itemPackageInfoList);
							cimm2bCentralLineItem.setShipViaInfo(shipViaInfo);
							Cimm2BCentralSubOrderCriteria subOrderCriteria = new Cimm2BCentralSubOrderCriteria();
							JSONParser parser = new JSONParser();
							try {
								if(item.getAdditionalProperties()!=null) {
									JSONObject json = (JSONObject) parser.parse(item.getAdditionalProperties());
									if(json.get("supplierCode")!=null) {
										subOrderCriteria.setCode((String)json.get("supplierCode"));
									}
									if(json.get("storeSku")!=null) {
										cimm2bCentralLineItem.setStorePartNumber((String)json.get("storeSku"));
									}
									if(json.get("supplierSku")!=null) {
										cimm2bCentralLineItem.setSupplierPartNumber((String)json.get("supplierSku"));
									}
								}
							}catch (Exception e) {
								e.printStackTrace();
							}
							subOrderCriteria.setValue(item.getMultipleShipVia());
							cimm2bCentralLineItem.setSubOrderCriteria(subOrderCriteria);
							
							lineItems.add(cimm2bCentralLineItem);
							if(item.getCartTotal()>0) {
								subTotal = item.getCartTotal();
							}else {
								if(cimm2bCentralLineItem!=null && cimm2bCentralLineItem.getListPrice()>0){
									  subTotal=subTotal+(cimm2bCentralLineItem.getListPrice());
								  }else{
									  subTotal=subTotal+(cimm2bCentralLineItem.getExtendedPrice());
								  }
							}
						}
					}
				}
				
				String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
				String branchCode = ProductsDAO.getWarehouseCustomField(CommonUtility.validateNumber(buyingCompanyId), "LOCATION_CODE");
				boolean isDFMModeActive = false; //DFM Changes
				boolean isMaximumThresholdMet = false; //DFM Changes
				if(session.getAttribute("DFMMode")!=null && CommonUtility.validateString(session.getAttribute("DFMMode").toString()).equalsIgnoreCase("Y")){
					isDFMModeActive = true;
					if(session.getAttribute("maximumThresholdMet")!=null && CommonUtility.validateString(session.getAttribute("maximumThresholdMet").toString()).equalsIgnoreCase("Y")){
						isMaximumThresholdMet = true;
					}
				}
				AddressModel billAddress = salesOrderInput.getBillAddress();
				AddressModel shipAddress =  salesOrderInput.getShipAddress();
				Cimm2BCentralAddress billingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(billAddress);
				Cimm2BCentralAddress shippingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(shipAddress);
				Cimm2BCentralBillAndShipToContact shipToContact = new Cimm2BCentralBillAndShipToContact();
				Cimm2BCentralBillAndShipToContact billToContact = new Cimm2BCentralBillAndShipToContact();
				
				String couponInfo = CommonUtility.validateString(salesOrderInput.getCouponInfo());
				
				ArrayList<Cimm2BCentralCoupon> orderLevelCouponList = new ArrayList<Cimm2BCentralCoupon>();
				if(couponInfo.length()>0) {
					String[] splitCoupon = couponInfo.split("\\^");
					for(int i=0;i<splitCoupon.length;i++){
							Cimm2BCentralCoupon couponData = new Cimm2BCentralCoupon();
							couponData.setCouponCode(splitCoupon[i]);
							orderLevelCouponList.add(couponData);
					}
				}
				
				Cimm2BCentralOrder orderRequest = new Cimm2BCentralOrder();
				
				if(orderLevelCouponList.size()>0) {
					orderRequest.setOrderLevelCouponList(orderLevelCouponList);
				}
				
				orderRequest.setCartId(salesOrderInput.getExternalCartId());
				orderRequest.setJurisdictionCode(salesOrderInput.getJurisdictionCode());
				orderRequest.setBillingAddress(billingAddress);
				orderRequest.setShippingAddress(shippingAddress);
				orderRequest.setShipToContact(shipToContact);
				orderRequest.setBillToContact(billToContact);
				orderRequest.setLineItems(lineItems);
				orderRequest.setOrderDate(orderDate);
				orderRequest.setOrderNotes(CommonUtility.validateString(salesOrderInput.getReqDate()));
				orderRequest.setCustomerERPId(customerErpId);
				if(CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY)) == 1) {
					orderRequest.setAnonymousUser(true);
				}
				orderRequest.setUserERPId((String) session.getAttribute(Global.USERID_KEY));
				orderRequest.setBranchCode(branchCode);
				if(CommonUtility.customServiceUtility()!=null){
					CommonUtility.customServiceUtility().getBranchCode(orderRequest,wareHousecode);
				}
				orderRequest.setCustomerPoNumber(salesOrderInput.getPurchaseOrderNumber()!=null?salesOrderInput.getPurchaseOrderNumber().toUpperCase():"");
				orderRequest.setShippingInstruction(salesOrderInput.getShippingInstruction());
				orderRequest.setOrderERPId(salesOrderInput.getOrderERPId());
				orderRequest.setWarehouseLocation(CommonUtility.validateString(salesOrderInput.getUserSelectedLocation()).trim().length()>0?salesOrderInput.getUserSelectedLocation():wareHousecode);
				orderRequest.setOrderNumber(orderNumber);
				orderRequest.setBackOrderSequence(backOrderSeqNumber);
				orderRequest.setOrderComment(salesOrderInput.getOrderNotes());
				orderRequest.setOrderedBy(CommonUtility.validateString(salesOrderInput.getOrderedBy()));
				orderRequest.setRequiredDate(CommonUtility.validateString(salesOrderInput.getReqDate()));
				orderRequest.setFreightOut(CommonUtility.validateDoubleNumber(salesOrderInput.getFrieghtCharges()));
				orderRequest.setFreight(CommonUtility.validateDoubleNumber(salesOrderInput.getFrieghtCharges()));
				orderRequest.setOrderSubTotal(subTotal);
				orderRequest.setBackOrderTotal(subTotal);
				orderRequest.setOrderTotal(subTotal+orderRequest.getFreight()+salesOrderInput.getOrderTax());
				orderRequest.setGasPoNumber(salesOrderInput.getGasPoNumber());
				orderRequest.setReleaseNumber(CommonUtility.validateString(salesOrderInput.getCustomerReleaseNumber()));
				orderRequest.setOtherAmount(CommonUtility.validateDoubleNumber(salesOrderInput.getShippingAndHandlingFee())); // PSS DFM
				orderRequest.setDfmOrder(isDFMModeActive); // PSS DFM
				orderRequest.setAnonymous(salesOrderInput.getAnonymous());
				orderRequest.setMaximumOrderThresholdMet(isMaximumThresholdMet); // PSS DFM
				orderRequest.setUniqueWebReferenceNumber(salesOrderInput.getOrderId());
				if(salesOrderInput.getOrderStatus() !=null)
				{
					orderRequest.setOrderStatus(salesOrderInput.getOrderStatus());
				}else {
					if(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderStatus.labels")!=null) {
						orderRequest.setOrderStatus(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderStatus.labels"));
					}
				}
				orderRequest.setTaxAmount(salesOrderInput.getOrderTax());
				orderRequest.setCustomerName(billAddress.getShipToName());
				orderRequest.setFreightCode(salesOrderInput.getFreightCode());
				orderRequest.setOrderType(CommonUtility.validateString(salesOrderInput.getOrderType()));
				if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels")).length()>0){
					orderRequest.setOrderSource(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels"):"");
				}
				orderRequest.setOrderDisposition(salesOrderInput.getOrderDisposition()!=null?salesOrderInput.getOrderDisposition():"");
				if(CommonUtility.validateString(salesLocationId).length()>0){
					orderRequest.setSalesLocationId(salesLocationId);
				}else{
					orderRequest.setSalesLocationId(wareHousecode);	
				}
				if(CommonDBQuery.getSystemParamtersList().get("CREATE_FREIGHT_LINE_ITEM") !=null &&CommonDBQuery.getSystemParamtersList().get("CREATE_FREIGHT_LINE_ITEM").equalsIgnoreCase("N")){
					orderRequest.setCreateShippingAndHandlingAsNewLineItem(false);
				}else{
					orderRequest.setCreateShippingAndHandlingAsNewLineItem(true);
				}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFUALT_CONTACT_ID")).length()>0){
				orderRequest.setContactId(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFUALT_CONTACT_ID")));
				}else if(CommonUtility.validateNumber(salesOrderInput.getErpUserContactId())>0){
					orderRequest.setContactId(CommonUtility.validateString(salesOrderInput.getErpUserContactId()));
				}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONSTANT_ORDER_COMMENT")).length()>0){
				orderRequest.setOrderCommentDisplayArea(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONSTANT_ORDER_COMMENT")));
				}
				if(salesOrderInput.getAnonymous()!=null && CommonUtility.validateString(salesOrderInput.getAnonymous()).equalsIgnoreCase("Y")){
					shipToContact.setFirstName(shipAddress.getFirstName());
					shipToContact.setLastName(shipAddress.getLastName());
					if(CommonUtility.validateString(shipAddress.getEmailAddress()).length()>0){
						shipToContact.setPrimaryEmailAddress(shipAddress.getEmailAddress().toUpperCase()!=null?shipAddress.getEmailAddress().toUpperCase():"");	
					}else{
						shipToContact.setPrimaryEmailAddress(billAddress.getEmailAddress()!=null?billAddress.getEmailAddress().toUpperCase():"");
					}
				}else{
					if(session.getAttribute("shipTofirstName")!= null && (String)session.getAttribute("shipTofirstName") != null){
						shipToContact.setFirstName((String)session.getAttribute("shipTofirstName"));
						if(session.getAttribute("shipToLastName")!= null && (String)session.getAttribute("shipToLastName") != null){
						shipToContact.setLastName((String)session.getAttribute("shipToLastName"));
						}else{
							shipToContact.setLastName(".");
						}
					}else{
						shipToContact.setFirstName(salesOrderInput.getShipAddress().getFirstName());
						if(CommonUtility.validateString(salesOrderInput.getShipAddress().getLastName()).length()>0){
							shipToContact.setLastName(salesOrderInput.getShipAddress().getLastName());
						}else{
							shipToContact.setLastName(".");
						}
					}
				}
				if(salesOrderInput.getShipAddress().getEmailAddress()!=null && salesOrderInput.getShipAddress().getEmailAddress()!=""){
					shipToContact.setPrimaryEmailAddress(salesOrderInput.getShipAddress().getEmailAddress().toUpperCase());
				}else if(CommonUtility.validateString(salesOrderInput.getBillAddress().getEmailAddress()).length()>0){
					shipToContact.setPrimaryEmailAddress(salesOrderInput.getBillAddress().getEmailAddress());
				}else {
					if(session!=null && (String)session.getAttribute("userEmailAddress")!=null) {
					shipToContact.setPrimaryEmailAddress(CommonUtility.validateString((String)session.getAttribute("userEmailAddress")));
					}
				}
				shipToContact.setPrimaryPhoneNumber(salesOrderInput.getShipAddress().getPhoneNo()!=null?salesOrderInput.getShipAddress().getPhoneNo():salesOrderInput.getBillAddress().getPhoneNo());
				shipToContact.setCarrierId(salesOrderInput.getShipVia());
				shipToContact.setContactId(customerErpId);
				shipToContact.setCompanyName(shippingAddress.getCustomerName());
				shipToContact.setShipAttention(shippingAddress.getCustomerName());
				billToContact.setPrimaryEmailAddress(billingAddress.getPrimaryEmailAddress());
				billToContact.setFirstName(billAddress.getFirstName());
				billToContact.setContactId(customerErpId);
				billToContact.setLastName(billAddress.getLastName());
				billToContact.setPrimaryPhoneNumber(billAddress.getPhoneNo());
				billToContact.setCompanyName(billAddress.getShipToName());
				billToContact.setShipAttention(billAddress.getShipToName());
				Cimm2BCentralCustomerCard customerCard = null;
				if(salesOrderInput.getCreditCardValue() != null){
					customerCard = new Cimm2BCentralCustomerCard();
					if(CommonUtility.validateString(salesOrderInput.getCreditCardValue().getElementPaymentAccountId()).length() > 0){
						customerCard.setPaymentAccountId(salesOrderInput.getCreditCardValue().getElementPaymentAccountId());
						String[] expDate = null;
						if(salesOrderInput.getCreditCardValue().getDate()!=null){
							expDate = salesOrderInput.getCreditCardValue().getDate().split("/");
						}
						String expMonth=null;
						String expYear=null;
						if(expDate!=null && expDate.length>1){
							expMonth = expDate[0];
							if(expDate.length>1){
								customerCard.setExpiryYear(expDate[1]);
							}
						}
						customerCard.setCustomerERPId(customerErpId);
						if(salesOrderInput.getBillAddress()!=null && CommonUtility.validateString(salesOrderInput.getBillAddress().getFirstName()).equalsIgnoreCase("")){
							customerCard.setCardHolderFirstName(salesOrderInput.getBillAddress().getFirstName());
						}
						if(salesOrderInput.getBillAddress()!=null && CommonUtility.validateString(salesOrderInput.getBillAddress().getLastName()).equalsIgnoreCase("")){
							customerCard.setCardHolderFirstName(salesOrderInput.getBillAddress().getLastName());
						}
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_MERCHANT_ID")).length()>0){
							customerCard.setPaymentAccountId(CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_MERCHANT_ID"));//(salesOrderInput.getCreditCardValue().getCreditCardTransactionID()); //CardTransactionID = PaymentAccountID
						}else{
							customerCard.setPaymentAccountId(salesOrderInput.getCreditCardValue().getCreditCardTransactionID());
						}
						customerCard.setAuthorizationNumber(salesOrderInput.getCreditCardValue().getCreditCardTransactionID());
						customerCard.setTransactionId(salesOrderInput.getCreditCardValue().getCreditCardTransactionID());
						customerCard.setCreditCardType(salesOrderInput.getCreditCardValue().getCreditCardType());
						customerCard.setCvv(salesOrderInput.getCreditCardValue().getCreditCardCvv2VrfyCode());
						customerCard.setCardHolderName(salesOrderInput.getCreditCardValue().getCardHolder());
						customerCard.setCreditCardNumber(salesOrderInput.getCreditCardValue().getCreditCardNumber());
						customerCard.setExpiryDate(salesOrderInput.getCreditCardValue().getDate());
						if(CommonUtility.customServiceUtility() != null) {
							CommonUtility.customServiceUtility().configureCreditCardDetails(customerCard);
							CommonUtility.customServiceUtility().getAuthAmount(customerCard,salesOrderInput);
						}
						
						//customerCard.setExpiryDate(salesOrderInput.getCreditCardValue().getDate());
						customerCard.setCardHolderFirstName(CommonUtility.validateString(billAddress.getFirstName()));
						customerCard.setCardHolderLastName(CommonUtility.validateString(billAddress.getLastName()));
						customerCard.setExpiryMonth(expMonth);
						customerCard.setExpiryYear(expYear);
						customerCard.setAmount(CommonUtility.validateDoubleNumber(salesOrderInput.getCreditCardValue().getCreditCardAmount()));
						customerCard.setAuthType("");
						if(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID")!=null && CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID").length()>0){
							customerCard.setElementProcessorID(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID")));
						}
						customerCard.setAddress(billingAddress);
						if(CommonUtility.validateString(salesOrderInput.getCreditCardValue().getSaveCard()).equalsIgnoreCase("Y")){
							customerCard.setSaveCard(true);	
						}else{
							customerCard.setSaveCard(false);
						}
						customerCard.setOrderERPId(salesOrderInput.getOrderERPId());
						orderRequest.setCustomerCard(customerCard);
		
					}
				}else{
					orderRequest.setPreAuthorize(false);
				}
				
				//CustomServiceProvider
				if(CommonUtility.customServiceUtility()!=null) {
					shipMap= CommonUtility.customServiceUtility().getCustomShipViaData(orderDetails);
				}
				//CustomServiceProvider
				String shipToAccNo= "";
				if(shipMap!=null && shipMap.size()>0) {
					String freightKey= CommonUtility.validateString((String)session.getAttribute("shipMethodKey"));
					ArrayList<Cimm2BCentralShipVia> shipVia=new ArrayList<Cimm2BCentralShipVia>();
					for (Entry<String, Cimm2BCentralShipVia> entry : shipMap.entrySet()) {
						try {
							if(entry.getKey().equalsIgnoreCase("SHIPTOSTORE")) {
								shipToAccNo = entry.getValue().getAccountNumber();
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
						Cimm2BCentralShipVia cimm2bCentralShipVia = new Cimm2BCentralShipVia();
						cimm2bCentralShipVia.setShipViaCode(entry.getKey());
						cimm2bCentralShipVia.setFreight(entry.getValue().getFreight());
						cimm2bCentralShipVia.setTax(entry.getValue().getTax());
						cimm2bCentralShipVia.setOrderSubTotal(entry.getValue().getOrderSubTotal());
						cimm2bCentralShipVia.setOrderTotal(entry.getValue().getOrderTotal());
						cimm2bCentralShipVia.setShipViaErpId(salesOrderInput.getShipVia()!=null?salesOrderInput.getShipVia():"");
						if(entry.getValue().getAccountNumber()!=null) {
						cimm2bCentralShipVia.setAccountNumber(entry.getValue().getAccountNumber());
						}
						if(entry.getKey().equalsIgnoreCase(freightKey)) {
							if(CommonUtility.validateString((String)session.getAttribute("shipMethod")).length()>0) {
								cimm2bCentralShipVia.setShipViaDescription(CommonUtility.validateString((String)session.getAttribute("shipMethod")));
							}else {
							cimm2bCentralShipVia.setShipViaDescription(salesOrderInput.getShipViaDescription()!=null?salesOrderInput.getShipViaDescription():"");
							}
						}
						if(entry.getKey().equalsIgnoreCase(CommonUtility.validateString((LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.ship.method.storePickup"))))) {
							cimm2bCentralShipVia.setAuthorizedPickupPerson(salesOrderInput.getAdditionalName());
							cimm2bCentralShipVia.setShippingInstruction(salesOrderInput.getAdditionalComments());
						}else if(entry.getKey().equalsIgnoreCase(CommonUtility.validateString((LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.ship.method.shipToStore"))))) {
							cimm2bCentralShipVia.setAuthorizedPickupPerson(salesOrderInput.getAdditionalPickupPerson());
							cimm2bCentralShipVia.setShippingInstruction(salesOrderInput.getAdditionalCommentsShipToStore());
						}
						shipVia.add(cimm2bCentralShipVia);
					}
					orderRequest.setShipVia(shipVia);
				}else {
					if(CommonUtility.validateString(salesOrderInput.getShipVia()).length() > 0){
						ArrayList<Cimm2BCentralShipVia> shipVia=new ArrayList<Cimm2BCentralShipVia>();
						Cimm2BCentralShipVia cimm2bCentralShipVia = new Cimm2BCentralShipVia();
		
						if(CommonUtility.validateString(salesOrderInput.getShipViaMethod()).equalsIgnoreCase(CommonUtility.validateString((LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("shipvia.label.willcallerpcode").trim())))){
							cimm2bCentralShipVia.setShipViaCode("WILLCALL");// P21 standard name is willcall, If the user selected ship method = shipvia.label.willcallerpcode .. then WILLCALL will be set to TRUE on order for P21
							cimm2bCentralShipVia.setShipViaDescription("WILLCALL");// P21 standard name is willcall, If the user selected ship method = shipvia.label.willcallerpcode .. then WILLCALL will be set to TRUE on order for P21
						}else{
							//cimm2bCentralShipVia.setShipViaCode(salesOrderInput.getShipViaMethod());
							cimm2bCentralShipVia.setShipViaCode(salesOrderInput.getShipVia());
							cimm2bCentralShipVia.setShipViaDescription(salesOrderInput.getShipViaMethod());
						}
						cimm2bCentralShipVia.setShipViaErpId(salesOrderInput.getShipVia());
						shipVia.add(cimm2bCentralShipVia);
						orderRequest.setShipVia(shipVia);
					}
				}
				for(Cimm2BCentralShipVia shipVia: cimm2BCentralShipViaFromQuote) {
					Cimm2BCentralSubOrderCriteria cimm2BCentralSubOrderCriteria = shipVia.getSubOrderCriteria();
					if(cimm2BCentralSubOrderCriteria.getValue().equalsIgnoreCase("STOREPICKUP")) {
						shipVia.setShipViaCode("STOREPICKUP");
						shipVia.setAuthorizedPickupPerson(salesOrderInput.getAdditionalName());
						shipVia.setShippingInstruction(salesOrderInput.getAdditionalComments());
					}else if(cimm2BCentralSubOrderCriteria.getValue().equalsIgnoreCase("SHIPTOSTORE")) {
						shipVia.setShipViaCode("SHIPTOSTORE");
						shipVia.setAuthorizedPickupPerson(salesOrderInput.getAdditionalPickupPerson());
						shipVia.setShippingInstruction(salesOrderInput.getAdditionalCommentsShipToStore());
						shipVia.setAccountNumber(shipToAccNo);
					}else if(cimm2BCentralSubOrderCriteria.getValue().equalsIgnoreCase("SHIPTOME")) {
						shipVia.setShipViaCode("SHIPTOME");
						shipVia.setShipViaDescription(salesOrderInput.getShipViaDescription()!=null?salesOrderInput.getShipViaDescription():"");
						shipVia.setShipViaErpId(salesOrderInput.getShipVia());
						if(CommonDBQuery.getSiteShipViaList()!=null) {
							for (ShipVia ship : CommonDBQuery.getSiteShipViaList()) {
								if(ship.getShipViaID().equalsIgnoreCase("WD")) {
									shipVia.setAccountNumber(CommonUtility.validateString(ship.getAccountNumber()));
								}
							}
						}
					}
				}
				orderRequest.setShipVia(cimm2BCentralShipViaFromQuote);
				String CREATE_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_ORDER_API"));
				Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_ORDER_URL, HttpMethod.POST, orderRequest, Cimm2BCentralOrder.class);


				Cimm2BCentralOrder orderResponse = null;
				if(orderResponseEntity!=null && orderResponseEntity.getData() != null && orderResponseEntity.getStatus() != null && orderResponseEntity.getStatus().getCode() == HttpStatus.SC_OK){
					ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
					orderResponse = (Cimm2BCentralOrder) orderResponseEntity.getData();
					orderResponse.setStatus(orderResponseEntity.getStatus());
					if(orderResponse != null && ((CommonUtility.validateString(orderResponse.getOrderNumber()).length() > 0) || (CommonUtility.validateString(orderResponse.getCartId()).length() > 0))){
						orderDetail.setExternalCartId(quoteResponse != null ? quoteResponse.getExternalCartId() : "");
						orderDetail.setUserId(CommonUtility.validateNumber(orderResponse.getUserERPId()));
						if(CommonUtility.validateString(orderResponse.getOrderERPId()).trim().length() > 0){
							orderDetail.setErpOrderNumber(orderResponse.getOrderERPId());
						}else{
							orderDetail.setErpOrderNumber(orderResponse.getOrderNumber());
						}	
						orderDetail.setOrderNum(orderResponse.getOrderNumber());
						orderDetail.setExternalSystemId(orderResponse.getOrderNumber());
						if(orderResponse.getOrderId()!=null){
							orderDetail.setOrderId(CommonUtility.validateNumber(orderResponse.getOrderId()));
						}else{
							orderDetail.setOrderID(orderResponse.getOrderNumber());
						}
						orderDetail.setStatusDescription("New");
						orderDetail.setStatus(orderResponse.getOrderStatus()!=null?orderResponse.getOrderStatus():"New");
						orderDetail.setShippingInstruction(CommonUtility.validateString(orderResponse.getShippingInstruction()));
						orderDetail.setFreight(orderResponse.getFreight()!=null?orderResponse.getFreight() :0 );
						orderDetail.setTax(orderResponse.getTaxAmount()!=null?orderResponse.getTaxAmount():0);
						orderDetail.setSubtotal(orderResponse.getOrderSubTotal()!=null?orderResponse.getOrderSubTotal():0);
						orderDetail.setTotal(orderResponse.getOrderTotal()!=null?orderResponse.getOrderTotal():0 + orderDetail.getFreight());
						orderDetail.setDiscount(orderResponse.getDiscountAmount()!=null?orderResponse.getDiscountAmount():0 + orderDetail.getDiscount());
						orderDetail.setOrderedBy(orderResponse.getOrderedBy()!=null?orderResponse.getOrderedBy():"");
						orderDetail.setBillAddress(billAddress);
						orderDetail.setShipAddress(shipAddress);
						orderDetail.setPoNumber(orderResponse.getCustomerPoNumber()!=null?orderResponse.getCustomerPoNumber():"");
						orderDetail.setOrderDate(orderResponse.getOrderDate()!=null?orderResponse.getOrderDate():"");
						orderDetail.setShipViaDescription(salesOrderInput.getShipViaDescription());
						if(orderResponse.getShipToContact()!=null && orderResponse.getShipToContact().getPrimaryPhoneNumber()!=null) {
							orderDetail.setShipPhone(orderResponse.getShipToContact().getPrimaryPhoneNumber());
							orderDetail.setShipFirstName(orderResponse.getShipToContact().getFirstName()!=null?orderResponse.getShipToContact().getFirstName():"");
							orderDetail.setShipLastName(orderResponse.getShipToContact().getLastName()!=null?orderResponse.getShipToContact().getLastName():"");

						}
						if(orderResponse.getShipToContact()!=null && orderResponse.getShipToContact().getPrimaryEmailAddress()!=null) {
							orderDetail.setShipEmailAddress(orderResponse.getShipToContact().getPrimaryEmailAddress());
						}
						/**
						 *Below code Written is for Adapt Pharma to get item level ship via details. *Reference- Prashanth GM
						 */
						UnilogFactoryInterface serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
						if(serviceClass!=null && orderResponse.getOrderDate()!=null) {
							String expectedDeliveryDate = serviceClass.getExpectedDeliveryDate(orderResponse.getOrderDate());
							if(CommonUtility.validateString(expectedDeliveryDate).length()>0){
								orderDetail.setShipDate(expectedDeliveryDate);
							}
						}
						ArrayList<Cimm2BCentralShipVia> cimm2BCentralShipVia = orderResponse.getShipVia();
						if(orderResponse.getShipVia()!=null && orderResponse.getShipVia().size()>0){
							orderDetail.setCimm2BCentralShipVia(cimm2BCentralShipVia);
							for (Cimm2BCentralShipVia orderResponseGetShipVia : cimm2BCentralShipVia) {
								orderDetail.setShipViaID(orderResponseGetShipVia.getShipViaCode());
								orderDetail.setShipViaMethod(orderResponseGetShipVia.getShipViaDescription());
								orderDetail.setShipViaErpId(orderResponseGetShipVia.getShipViaErpId());
								orderDetail.setAccountNumber(orderResponseGetShipVia.getAccountNumber());
								orderDetail.setCarrierTrackingNumber(orderResponseGetShipVia.getCarrierTrackingNumber());
								//CustomServiceProvider
								String customShipViaDescription = null;
								if(CommonUtility.customServiceUtility()!=null) {
									customShipViaDescription = CommonUtility.customServiceUtility().getCustomShipViaDescription(session,orderResponseGetShipVia);
								}
								//CustomServiceProvider
								if(customShipViaDescription!=null) {
									if(customShipViaDescription.length()>0) {
										orderDetail.setShipViaDescription(customShipViaDescription);
									}
								}else {
									orderDetail.setShipViaDescription(orderResponseGetShipVia.getShipViaDescription());
								}
							}
						}
						ArrayList<Cimm2BCentralLineItem> cimm2bCentralLineItem = orderResponse.getLineItems();
						String NonCatalogItemId = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID_FOR_ADDTOCART"));
						if(cimm2bCentralLineItem != null && cimm2bCentralLineItem.size() > 0){
							double handlingCharges = 0.0;
							double deliveryCharges = 0.0;
							
							if(NonCatalogItemId.length()>0) {
							HashMap<String, SalesModel> lineItemsFromResponseCheckNonCatalogItems = new HashMap<String, SalesModel>();

							for (Cimm2BCentralLineItem orderResponseLineItem : cimm2bCentralLineItem) {

								SalesModel model = new SalesModel();

								model.setLineNumber(orderResponseLineItem.getLineNumber()!=null?orderResponseLineItem.getLineNumber():0);
								model.setCategoryName(orderResponseLineItem.getProductCategory());
								model.setLineItemComment(CommonUtility.validateString(orderResponseLineItem.getLineItemComment()!=null?orderResponseLineItem.getLineItemComment():""));
								model.setItemId(CommonUtility.validateNumber(orderResponseLineItem.getItemId()!=null?orderResponseLineItem.getItemId():""));
								model.setPartNumber(CommonUtility.validateString(orderResponseLineItem.getPartNumber()!=null?orderResponseLineItem.getPartNumber():""));
								model.setCustomerPartNumber(CommonUtility.validateString(orderResponseLineItem.getCustomerPartNumber()!=null?orderResponseLineItem.getCustomerPartNumber():""));
								model.setShortDesc(orderResponseLineItem.getShortDescription()!=null?orderResponseLineItem.getShortDescription():"");
								model.setUnitPrice(orderResponseLineItem.getUnitPrice());
								model.setExtPrice(orderResponseLineItem.getExtendedPrice()!=null?orderResponseLineItem.getExtendedPrice():0);
								model.setQtyordered(orderResponseLineItem.getQty());
								if(orderResponseLineItem.getQtyShipped()!=null){
									model.setQtyShipped(orderResponseLineItem.getQtyShipped().intValue());
								}
								model.setUom(orderResponseLineItem.getUom());
								model.setQtyUom(CommonUtility.validateString(orderResponseLineItem.getUomQty()!=null?orderResponseLineItem.getUomQty():"0"));
								model.setBrandName(CommonUtility.validateString(orderResponseLineItem.getBrandName()!=null?orderResponseLineItem.getBrandName():""));
								model.setManufacturer(CommonUtility.validateString(orderResponseLineItem.getManufacturer()!=null?orderResponseLineItem.getManufacturer():""));
								model.setManufacturerPartNumber(CommonUtility.validateString(orderResponseLineItem.getManufacturerPartNumber()!=null?orderResponseLineItem.getManufacturerPartNumber():""));
								if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
									if(orderResponseLineItem.getSubOrderCriteria()!=null && orderResponseLineItem.getSubOrderCriteria().getValue()!=null) {
										model.setMultipleShipVia(orderResponseLineItem.getSubOrderCriteria().getValue());
									}
									model.setMultipleShipViaDesc(orderResponseLineItem.getShippingBranch());
								}
								lineItemsFromResponseCheckNonCatalogItems.put(orderResponseLineItem.getPartNumber(), model);
							}
							
							for (ProductsModel orderItem : orderDetails) {
								lineItemsFromResponseCheckNonCatalogItems.remove(orderItem.getPartNumber());
							}
							List<String> handlingListItemsArray = Arrays.asList(handlingListItems.split(","));
							List<String> deliveryListItemsArray = Arrays.asList(deliveryListItems.split(","));
							Iterator<Entry<String, SalesModel>> it = lineItemsFromResponseCheckNonCatalogItems.entrySet().iterator();
							if(salesOrderInput.getOrderId()>0){
								while (it.hasNext()) {
									Map.Entry<String, SalesModel> pair = (Map.Entry<String, SalesModel>)it.next();
		
									if(handlingListItemsArray.contains(pair.getValue().getPartNumber())){
										handlingCharges = handlingCharges + pair.getValue().getExtPrice();
									}else if(deliveryListItemsArray.contains(pair.getValue().getPartNumber())){
										deliveryCharges = deliveryCharges + pair.getValue().getExtPrice();
									}
		
									ProductsModel nonCatalogItem = new ProductsModel();
									//nonCatalogItem.setLineNumber(cimm2bCentralLineItem.get(i).getLineNumber());
									nonCatalogItem.setLineItemComment(pair.getValue().getLineItemComment());
									nonCatalogItem.setItemId(CommonUtility.validateNumber(NonCatalogItemId));
									nonCatalogItem.setPartNumber(pair.getValue().getPartNumber());
									nonCatalogItem.setCustomerPartNumber(pair.getValue().getCustomerPartNumber());
									nonCatalogItem.setShortDesc(pair.getValue().getShortDesc()!=null?pair.getValue().getShortDesc():"");
									nonCatalogItem.setPrice(pair.getValue().getUnitPrice());
									nonCatalogItem.setExtendedPrice(pair.getValue().getExtPrice());
									nonCatalogItem.setQty(pair.getValue().getQtyShipped()!=0?pair.getValue().getQtyShipped():1);
									//model.setQtyShipped(pair.getValue().getQtyShipped().intValue());
									nonCatalogItem.setUom(pair.getValue().getUom());
									nonCatalogItem.setQtyUOM(CommonUtility.validateString(pair.getValue().getQtyUom()!=null?pair.getValue().getQtyUom():"0"));
									nonCatalogItem.setManufacturerName(pair.getValue().getManufacturer());
									nonCatalogItem.setManufacturerPartNumber(pair.getValue().getManufacturer()!=null?pair.getValue().getManufacturer():"");
		
									conn = ConnectionManager.getDBConnection();
									SalesAction.saveOrderItems(conn, salesOrderInput.getOrderId(), nonCatalogItem, "", 0, 0, "");
									it.remove(); // avoids a ConcurrentModificationException
								}
							}
						}else {
							for (Cimm2BCentralLineItem orderResponseLineItem : cimm2bCentralLineItem) {

								SalesModel model = new SalesModel();
								
								model.setLineNumber(orderResponseLineItem.getLineNumber()!=null?orderResponseLineItem.getLineNumber():0);
								model.setCategoryName(orderResponseLineItem.getProductCategory());
								model.setLineItemComment(CommonUtility.validateString(orderResponseLineItem.getLineItemComment()!=null?orderResponseLineItem.getLineItemComment():""));
								model.setItemId(CommonUtility.validateNumber(orderResponseLineItem.getItemId()!=null?orderResponseLineItem.getItemId():""));
								model.setPartNumber(CommonUtility.validateString(orderResponseLineItem.getPartNumber()!=null?orderResponseLineItem.getPartNumber():""));
								model.setCustomerPartNumber(CommonUtility.validateString(orderResponseLineItem.getCustomerPartNumber()!=null?orderResponseLineItem.getCustomerPartNumber():""));
								model.setShortDesc(orderResponseLineItem.getShortDescription()!=null?orderResponseLineItem.getShortDescription():"");
								model.setUnitPrice(orderResponseLineItem.getUnitPrice());
								if(CommonUtility.customServiceUtility()!=null) {
									CommonUtility.customServiceUtility().getUnitPriceofOrders(model,orderResponseLineItem,orderResponse);
								}
								if(orderResponseLineItem.getExtendedPrice()!=null && orderResponseLineItem.getExtendedPrice()>0) {
									model.setExtPrice(orderResponseLineItem.getExtendedPrice());
								}else {
								model.setExtPrice(orderResponseLineItem.getQty()*orderResponseLineItem.getUnitPrice());
								}
								model.setQtyordered(orderResponseLineItem.getQty());
								if(orderResponseLineItem.getQtyShipped()!=null){
									model.setQtyShipped(orderResponseLineItem.getQtyShipped().intValue());
								}
								model.setUom(orderResponseLineItem.getUom());
								model.setQtyUom(CommonUtility.validateString(orderResponseLineItem.getUomQty()!=null?orderResponseLineItem.getUomQty():"0"));
								model.setImageName(CommonUtility.validateString(orderResponseLineItem.getImageName()!=null?orderResponseLineItem.getImageName():""));
								model.setBrandName(CommonUtility.validateString(orderResponseLineItem.getBrandName()!=null?orderResponseLineItem.getBrandName():""));
								model.setManufacturer(CommonUtility.validateString(orderResponseLineItem.getManufacturer()!=null?orderResponseLineItem.getManufacturer():""));
								model.setManufacturerPartNumber(CommonUtility.validateString(orderResponseLineItem.getManufacturerPartNumber()!=null?orderResponseLineItem.getManufacturerPartNumber():""));
								if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
									if(orderResponseLineItem.getSubOrderCriteria()!=null && orderResponseLineItem.getSubOrderCriteria().getValue()!=null) {
										model.setMultipleShipVia(orderResponseLineItem.getSubOrderCriteria().getValue());
									}
									model.setMultipleShipViaDesc(orderResponseLineItem.getShippingBranch());
									WarehouseModel wareHouseDetail = new WarehouseModel();
									wareHouseDetail = UsersDAO.getWareHouseDetailsByCode(orderResponseLineItem.getShippingBranch());
									if(wareHouseDetail!=null) {
										model.setWareHouseDetails(wareHouseDetail);
									}
								}
								model.setAltPartNumber1(CommonUtility.validateString(orderResponseLineItem.getStorePartNumber()));
								model.setAltPartNumber2(CommonUtility.validateString(orderResponseLineItem.getSupplierPartNumber()));
								orderList.add(model);
							}
							
						}
							orderDetail.setOrderList(orderList);
							orderDetail.setHandling(handlingCharges);
							orderDetail.setDeliveryCharge(deliveryCharges);
						}
						orderDetail.setStatus(CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()));
						orderDetail.setOrderStatusDesc("successfully");
						UnilogFactoryInterface orderClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
						if(orderClass!=null && cimm2bCentralLineItem!=null) {
							SalesModel ordersList = orderClass.orderdetail(cimm2bCentralLineItem);
							if(ordersList!=null){
								orderDetail.setQtyordered(ordersList.getQtyordered());
								orderDetail.setQtyShipped((ordersList.getQtyShipped()));
								orderDetail.setCustomerPrice(ordersList.getCustomerPrice());
								orderDetail.setLineItemComment(salesOrderInput.getComments());
								orderDetail.setPageTitle(salesOrderInput.getComments());
							}
						}
					}
					String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
					int userId = CommonUtility.validateNumber(sessionUserId);
					if (userId > 1) {
						UsersModel userAddress = UsersDAO.getEntityDetailsByUserId(userId);
						if(userAddress!=null) {
							AddressModel address = new AddressModel();
							address.setFirstName(userAddress.getFirstName());
			            	address.setLastName(userAddress.getLastName());
			            	address.setAddress1(userAddress.getAddress1());
			            	address.setAddress2(userAddress.getAddress2());
			            	address.setCity(userAddress.getCity());
			            	address.setState(userAddress.getState());
			            	address.setCountry(userAddress.getCountry());
			            	address.setEmailAddress(userAddress.getEmailAddress());
			            	address.setZipCode(userAddress.getZipCode());
			            	address.setUserName(userAddress.getUserName());
			            	address.setUserStatus(userAddress.getUserStatus());
			            	address.setJobTitle(userAddress.getJobTitle());
			            	address.setPhoneNo(userAddress.getPhoneNo());
			            	orderDetail.setShippedAddress(address);
						}
					}
				}else{
					if(CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()).length()>0 && CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()).equalsIgnoreCase("424")){
						errorMessageToDisplay=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("errormsgtodisplay.labels"));
						orderDetail.setStatusDescription(errorMessageToDisplay);
					}
					if(orderResponseEntity.getStatus()!=null && orderResponseEntity.getStatus().getMessage()!=null) {
						orderDetail.setStatusDescription(orderResponseEntity.getStatus().getMessage());
					}
					orderDetail.setStatus(CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()));
					orderDetail.setSendMailFlag(false);
				}
			}
			catch (SQLException e) { 
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				ConnectionManager.closeDBConnection(conn);
			}
			return orderDetail;
		}
		
		public void setOrderValuesFromQuoteResponse(SalesModel quoteResponse,LinkedHashMap<String, Object> contentObject){
			double taxFromErp = quoteResponse.getTax();
			double totalCartFrieghtCharges = quoteResponse.getFreight();
			double total = quoteResponse.getSubtotal();
			double orderTotal = quoteResponse.getTotal();
			contentObject.put("orderTax",taxFromErp);
			contentObject.put("totalCartFrieghtCharges",totalCartFrieghtCharges);
			contentObject.put("subTotal", total);
			contentObject.put("orderTotal", orderTotal);
		}
		
		public Cimm2BCentralPriceAndAvailability getAllStoreAvailability(String[] partNumber) {
			Cimm2BCentralPriceAndAvailability allBranchAvailability = null;
			try
			{
				Cimm2BPriceAndAvailabilityRequest allStoreAvailabilityRequest = new Cimm2BPriceAndAvailabilityRequest();
				List<String> partIdentifiers = new ArrayList<String>();
				for(String pn:partNumber) {
					partIdentifiers.add(pn);
				}
				allStoreAvailabilityRequest.setPartIdentifiers(partIdentifiers);
				String GET_ALL_STORE_AVAILABILITY_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ALL_STORE_AVAILABILITY_API"));
				Cimm2BCentralResponseEntity allStoreAvailabilityResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_ALL_STORE_AVAILABILITY_API, HttpMethod.POST,allStoreAvailabilityRequest,Cimm2BCentralPriceAndAvailability.class);
				if(allStoreAvailabilityResponseEntity!=null && allStoreAvailabilityResponseEntity.getData() != null &&  allStoreAvailabilityResponseEntity.getStatus().getCode() == HttpStatus.SC_OK ){
					allBranchAvailability = (Cimm2BCentralPriceAndAvailability) allStoreAvailabilityResponseEntity.getData();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return allBranchAvailability;
		}
		
		public AddressModel createRetailUser(UsersModel userDetails) {
			AddressModel userRegistrationDetail = null;
			AddressModel shipAddress = null;
			try {
				userRegistrationDetail = new AddressModel();
				userRegistrationDetail.setFirstName(userDetails.getFirstName());
				userRegistrationDetail.setLastName(userDetails.getLastName());
				userRegistrationDetail.setEmailAddress(userDetails.getEmailAddress());
				userRegistrationDetail.setUserPassword(userDetails.getPassword());
				userRegistrationDetail.setCompanyName(userDetails.getBillAddress().getCompanyName());
				userRegistrationDetail.setAddress1(userDetails.getBillAddress().getAddress1());
				userRegistrationDetail.setAddress2(userDetails.getBillAddress().getAddress2());
				userRegistrationDetail.setCity(userDetails.getBillAddress().getCity());
				userRegistrationDetail.setState(userDetails.getBillAddress().getState());
				userRegistrationDetail.setZipCode(userDetails.getBillAddress().getZipCode());
				userRegistrationDetail.setCountry(userDetails.getBillAddress().getCountry());
				userRegistrationDetail.setPhoneNo(userDetails.getBillAddress().getPhoneNo());
				userRegistrationDetail.setCustomerType(userDetails.getCustomerType());
				userRegistrationDetail.setFaxNumber("");
				userRegistrationDetail.setRole("Ecomm Retail User");
				userRegistrationDetail.setUpdateRole(true);
				userRegistrationDetail.setUserStatus("Y");
				userRegistrationDetail.setNewsLetterSub("N");
				userRegistrationDetail.setAnonymousUser(true);
				
				if(CommonUtility.validateString(userDetails.getSameAsBillingAddress()).equalsIgnoreCase("N")){
					shipAddress = userDetails.getShipAddress();
					userRegistrationDetail.setShippingAddress(shipAddress);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return userRegistrationDetail;
		}
		
		public void overrideAuAddress(UsersModel userDetail,UsersModel billAddress,UsersModel shipAddress) {
			if(userDetail.getBillAddress()!=null) {
				billAddress.setFirstName(userDetail.getBillAddress().getFirstName());
				billAddress.setLastName(userDetail.getBillAddress().getLastName());
				billAddress.setCompanyName(userDetail.getBillAddress().getCompanyName());
				billAddress.setAddress1(userDetail.getBillAddress().getAddress1());
				billAddress.setAddress2(userDetail.getBillAddress().getAddress2());
				billAddress.setCity(userDetail.getBillAddress().getCity());
				billAddress.setState(userDetail.getBillAddress().getState());
				billAddress.setCountry(userDetail.getBillAddress().getCountry());
				billAddress.setPhoneNo(userDetail.getBillAddress().getPhoneNo());
				billAddress.setZipCode(userDetail.getBillAddress().getZipCode());
				billAddress.setEmailAddress(userDetail.getBillAddress().getEmailAddress());
			}
			if(userDetail.getShipAddress()!=null) {
				shipAddress.setFirstName(userDetail.getShipAddress().getFirstName());
				shipAddress.setLastName(userDetail.getShipAddress().getLastName());
				shipAddress.setCompanyName(userDetail.getShipAddress().getCompanyName());
				shipAddress.setAddress1(userDetail.getShipAddress().getAddress1());
				shipAddress.setAddress2(userDetail.getShipAddress().getAddress2());
				shipAddress.setCity(userDetail.getShipAddress().getCity());
				shipAddress.setState(userDetail.getShipAddress().getState());
				shipAddress.setCountry(userDetail.getShipAddress().getCountry());
				shipAddress.setPhoneNo(userDetail.getShipAddress().getPhoneNo());
				shipAddress.setZipCode(userDetail.getShipAddress().getZipCode());
			}
		}
		
		public double excludeItemWeight(String selectedShipMethod, double weight) {
			if(selectedShipMethod == null || !selectedShipMethod.equalsIgnoreCase(LayoutLoader.getMessageProperties().get("EN").getProperty("label.ship.method.name"))){
				weight = 0;
			}
			return weight;
		}
		
		public void setAdditionalInfo(ProductsModel itemPrice,ProductsModel eclipseitemPrice) {
			itemPrice.setAltPartNumber1(CommonUtility.validateString(eclipseitemPrice.getStoreSku()));
			itemPrice.setAltPartNumber2(CommonUtility.validateString(eclipseitemPrice.getSupplierSku()));
			itemPrice.setSupplierCode(CommonUtility.validateString(eclipseitemPrice.getSupplierCode()));
			if(eclipseitemPrice.getWeight()>0) {
				itemPrice.setWeight(eclipseitemPrice.getWeight());
			}
		}
		
		public ErpItemSearchRequest buildCustomPriceEnquiryRequestBody(ErpItemSearchRequest requestBody, ProductManagementModel priceInquiryInput, HttpSession session){
			String warehouseCode = (String)session.getAttribute("wareHouseCode");
			if(CommonUtility.validateString(warehouseCode).length() == 0){
				warehouseCode = CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR");
			}
			return new ErpItemSearchRequest.Builder()
					.pricingWarehouseCode(LayoutLoader.getMessageProperties().get("EN").getProperty("erp.price.warehouseCode")!=null?LayoutLoader.getMessageProperties().get("EN").getProperty("erp.price.warehouseCode"):"")
					.lineItems(extractLineItems(priceInquiryInput.getPartIdentifier()))
					.warehouseCodes(CommonDBQuery.getWareHouseList().keySet())
					.customerErpId(priceInquiryInput.getEntityId())
					.customerJobId("0")
					.build();
		}
		
		private static List<CimmProductQueryLineItem> extractLineItems(List<ProductsModel> products) {
			return products.stream().filter(i -> !(CommonUtility.validateString(i.getOverRidePriceRule()).equalsIgnoreCase("Y") && i.getPrice() > 0))
					.map(i -> {
					return new CimmProductQueryLineItem.Builder(i.getPartNumber())
							.quantity(Double.valueOf(i.getQty()))
							.uom(i.getUom())
							.build();
				}).collect(Collectors.toList());
		}
		
		public  boolean checkInactiveStatus(String buyingCompanyId) {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			try {
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_POS_INACTIVE_CUSTOMER")).equalsIgnoreCase("Y")) {
				String erpCustomerId = UsersDAO.getEntityIdWithBuyingCompanyId(buyingCompanyId);
				if(CommonUtility.validateString(erpCustomerId).length()>0) {
			    	   if(checkPurgeStatus(erpCustomerId, session)) {
			    		   return true;
			    	   }
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		public void getUserInfoFromErp(HttpSession session) {
			long startTimer = CommonUtility.startTimeDispaly();
			request = ServletActionContext.getRequest();
			try {
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_POS_INACTIVE_CUSTOMER")).equalsIgnoreCase("Y")) {
		    	   if(checkPurgeStatus(CommonUtility.validateString((String)session.getAttribute("systemCustomerId")), session)) {
		    		   session.setAttribute("posInactiveFlag", "Y");
		    	   }
		       }
			}catch (Exception e) {
				e.printStackTrace();
			}
			CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		}
		
		public boolean checkPurgeStatus(String systemCustomerId, HttpSession session) {
			long startTimer = CommonUtility.startTimeDispaly();
			String erp = "cimmesb";
			try {
				UserManagement userManagementImp = new UserManagementImpl();
				UsersModel userData = new UsersModel();
				userData.setCustomerId(systemCustomerId);
				userData.setErpOverrideFlag(erp);
				userData.setSession(session);
				Cimm2BCustomer userDataFromErp = userManagementImp.getUserInfoFromErp(userData);
				if(userDataFromErp.getPurged() && userDataFromErp != null) {
	    		   return true;
				}
				}catch (Exception e) {
					e.printStackTrace();
				}
			CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
			return false;
		}
		public String validateCoupons(SalesModel couponsInput) {
			SecureData password=new SecureData();
			HttpSession session = couponsInput.getSession();
			Gson gson = new Gson();
			String result = "";
			final String pickupAtStoreCode = CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("coupons.pickupatstorecode"));
			final String shipToStoreCode = CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("coupons.shiptostorecode"));
			final String shipToMeCode = CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("coupons.shiptomecode"));
			String erp = CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("coupons.erpValue"));
			try {
				double shiptoStoreTotal = 0.0;
				double shiptoMeTotal = 0.0;
				double pickupAtStoreTotal = 0.0;
				List<OrderLevelCouponList> orderCouponLists=new ArrayList<>();
				String[] spitCoupons = couponsInput.getDiscountCouponCode().split("\\|");
				if(CommonUtility.validateString(couponsInput.getDiscountCouponCode()).length()>0 && spitCoupons.length>0) {
				for(int i=0; i<spitCoupons.length;i++) {
					OrderLevelCouponList coupon=new OrderLevelCouponList();
					coupon.setCouponCode(spitCoupons[i]);
					orderCouponLists.add(coupon);
					}
				}
				List<CimmLineItem> shipToMeLineItem=new ArrayList<>();
				List<CimmLineItem> shipToStoreLineItem=new ArrayList<>();
				List<CimmLineItem> pickpAtStoreLineItem=new ArrayList<>();
				for(ProductsModel items: couponsInput.getCartData()) {
					CimmLineItem lineItem=new CimmLineItem();
					lineItem.setExtendedPrice(items.getTotal());
					lineItem.setUnitPrice(items.getUnitPrice());
					lineItem.setQty((double) items.getQty());
					lineItem.setPartNumber(items.getPartNumber());
					lineItem.setShortDescription(items.getShortDesc());
					if(items.getMultipleShipVia().equals("SHIPTOME")) {
						shiptoMeTotal =  shiptoMeTotal + items.getTotal();
						shipToMeLineItem.add(lineItem);
					}else if(items.getMultipleShipVia().equals("SHIPTOSTORE")) {
						shiptoStoreTotal = shiptoStoreTotal + items.getTotal();
						shipToStoreLineItem.add(lineItem);
					}else if(items.getMultipleShipVia().equals("STOREPICKUP")) {
						pickupAtStoreTotal = pickupAtStoreTotal + items.getTotal();
						pickpAtStoreLineItem.add(lineItem);
					}
				}
				CimmSubOrder pickUpAtStorCimmSubOrder=new CimmSubOrder();
				CimmSubOrder shiptoStoreCimmSubOrder=new CimmSubOrder();
				CimmSubOrder shipToMeCimmSubOrder=new CimmSubOrder();
				List<CimmSubOrder> cimmSubOrders=new ArrayList<>();
				if(pickpAtStoreLineItem.size()>0) {
					pickUpAtStorCimmSubOrder.setShipMethod(pickupAtStoreCode);
					pickUpAtStorCimmSubOrder.setOrderSubTotal(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(pickupAtStoreTotal)));
					pickUpAtStorCimmSubOrder.setLineItems(pickpAtStoreLineItem);
					cimmSubOrders.add(pickUpAtStorCimmSubOrder);
				}
				if(shipToStoreLineItem.size()>0) {
					shiptoStoreCimmSubOrder.setShipMethod(shipToStoreCode);
					shiptoStoreCimmSubOrder.setOrderSubTotal(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(shiptoStoreTotal)));
					shiptoStoreCimmSubOrder.setLineItems(shipToStoreLineItem);
					cimmSubOrders.add(shiptoStoreCimmSubOrder);
				}
				if(shipToMeLineItem.size()>0) {
					shipToMeCimmSubOrder.setShipMethod(shipToMeCode);
					shipToMeCimmSubOrder.setOrderSubTotal(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(shiptoMeTotal)));
					shipToMeCimmSubOrder.setLineItems(shipToMeLineItem);
					cimmSubOrders.add(shipToMeCimmSubOrder);
				}
				CimmOrderRequest orderRequest=new CimmOrderRequest();
				orderRequest.setWarehouseLocation(CommonUtility.validateString((String) session.getAttribute("wareHouseCode")).replaceAll("MY_", ""));
				orderRequest.setOrderTotal(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(couponsInput.getSubtotal()+couponsInput.getFreight()+couponsInput.getTax())));
				orderRequest.setOrderSubTotal(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(couponsInput.getSubtotal())));
				orderRequest.setTaxAmount(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(couponsInput.getTax())));
				orderRequest.setFreightAmount(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(couponsInput.getFreight())));
				orderRequest.setOrderLevelCouponList(orderCouponLists);
				orderRequest.setCimmSubOrders(cimmSubOrders);
				
				SalesOrderManagement salesOrderImp = new SalesOrderManagementImpl();
				Cimm2BCResponse<CimmOrder> response = salesOrderImp.validateCoupons(orderRequest, session, erp);
				
				if(response != null && response.getStatus().getCode()==200){  
				 result = gson.toJson(response);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result;
		}
}
