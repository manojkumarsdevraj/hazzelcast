package com.unilog.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.unilog.misc.EventDetails;
import com.unilog.misc.EventModel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import java.sql.SQLException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.text.Format;
import java.text.SimpleDateFormat;
import com.erp.service.SalesOrderManagement;
import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerData;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPriceAndAvailability;
import com.erp.service.impl.SalesOrderManagementImpl;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.SalesOrderManagementModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCustomer;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.CustomFieldModel;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.maplocation.LocationModel;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.SalesModel;
import com.unilog.security.LoginAuthentication;
import com.unilog.users.UsersDAO;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersAction;
import com.unilog.users.WarehouseModel;
import com.unilog.velocitytool.CIMM2VelocityTool;
import com.unilognew.util.ECommerceEnumType.AddressType;

import java.util.Date;
import java.util.HashMap;

public class CustomServiceUtility {
	static final Logger logger = Logger.getLogger(CustomServiceUtility.class);
	private static final String SUCCESS = null;
	/**
-	 *Class created by shamith to trigger custom services for account level. Ajax calls/java function triggers can be done here.
	 */
	private HttpServletRequest request;
	private String renderContent;
	private String keyword;
	private String locationResponseList;
	private String locationDataFrom;
	private HttpServletResponse response;
	private String ccNumber;
	private String cardHolder;
	private String ccExp;
	private String ccTransactionId;
	private String streetAddress;
	private String postalCode;
	private String ccResponseCode;
	private String ccStatus;
	private String ccHostRefNumber;
	private String ccTaskID;
	private String ccAmount;
	private String ccDeclineResponseReason;
	private String ccCvv2VrfyCode;
	private String ccTip;
	private String ccTransTimeStamp;
	private String ccToken;
	private String ccApprovedAmount;
	private String ccRequestedAmount;
	private String ccHostResponseCode;
	private String ccRefrenceCode;
	private String ccInvoice;
	private String ccApprovalCode;
	private String ccServerTimestamp;
	private String ccFee;
	private String ccExternalSessionID;
	private String ccAddVrfyCode;
	private String ccTax;
	private String ccNewDomainKey;
	private String ccType;
	private String actualOrderTotal;
	private String ccMerchantId;
	private String saveCard;
	private String mobileUserId;
	private String type;
	private String mobileUserName;
	private int defaultBillToId;
	private int defaultShipToId;
	private Integer[] cartItemList;
	private String shipToAddress;
	private String result;
	
	public String getShipToAddress() {
		return shipToAddress;
	}
	public void setShipToAddress(String shipToAddress) {
		this.shipToAddress = shipToAddress;
	}
	public Integer[] getCartItemList() {
		return cartItemList;
	}
	public void setCartItemList(Integer[] cartItemList) {
		this.cartItemList = cartItemList;
	}
	public String getCcStatus() {
		return ccStatus;
	}
	public void setCcStatus(String ccStatus) {
		this.ccStatus = ccStatus;
	}
	public String getCcHostRefNumber() {
		return ccHostRefNumber;
	}
	public void setCcHostRefNumber(String ccHostRefNumber) {
		this.ccHostRefNumber = ccHostRefNumber;
	}
	public String getCcTaskID() {
		return ccTaskID;
	}
	public void setCcTaskID(String ccTaskID) {
		this.ccTaskID = ccTaskID;
	}
	public String getCcAmount() {
		return ccAmount;
	}
	public void setCcAmount(String ccAmount) {
		this.ccAmount = ccAmount;
	}
	public String getCcDeclineResponseReason() {
		return ccDeclineResponseReason;
	}
	public void setCcDeclineResponseReason(String ccDeclineResponseReason) {
		this.ccDeclineResponseReason = ccDeclineResponseReason;
	}
	public String getCcCvv2VrfyCode() {
		return ccCvv2VrfyCode;
	}
	public void setCcCvv2VrfyCode(String ccCvv2VrfyCode) {
		this.ccCvv2VrfyCode = ccCvv2VrfyCode;
	}
	public String getCcTip() {
		return ccTip;
	}
	public void setCcTip(String ccTip) {
		this.ccTip = ccTip;
	}
	public String getCcTransTimeStamp() {
		return ccTransTimeStamp;
	}
	public void setCcTransTimeStamp(String ccTransTimeStamp) {
		this.ccTransTimeStamp = ccTransTimeStamp;
	}
	public String getCcToken() {
		return ccToken;
	}
	public void setCcToken(String ccToken) {
		this.ccToken = ccToken;
	}
	public String getCcApprovedAmount() {
		return ccApprovedAmount;
	}
	public void setCcApprovedAmount(String ccApprovedAmount) {
		this.ccApprovedAmount = ccApprovedAmount;
	}
	public String getCcRequestedAmount() {
		return ccRequestedAmount;
	}
	public void setCcRequestedAmount(String ccRequestedAmount) {
		this.ccRequestedAmount = ccRequestedAmount;
	}
	public String getCcHostResponseCode() {
		return ccHostResponseCode;
	}
	public void setCcHostResponseCode(String ccHostResponseCode) {
		this.ccHostResponseCode = ccHostResponseCode;
	}
	public String getCcRefrenceCode() {
		return ccRefrenceCode;
	}
	public void setCcRefrenceCode(String ccRefrenceCode) {
		this.ccRefrenceCode = ccRefrenceCode;
	}
	public String getCcInvoice() {
		return ccInvoice;
	}
	public void setCcInvoice(String ccInvoice) {
		this.ccInvoice = ccInvoice;
	}
	public String getCcApprovalCode() {
		return ccApprovalCode;
	}
	public void setCcApprovalCode(String ccApprovalCode) {
		this.ccApprovalCode = ccApprovalCode;
	}
	public String getCcServerTimestamp() {
		return ccServerTimestamp;
	}
	public void setCcServerTimestamp(String ccServerTimestamp) {
		this.ccServerTimestamp = ccServerTimestamp;
	}
	public String getCcFee() {
		return ccFee;
	}
	public void setCcFee(String ccFee) {
		this.ccFee = ccFee;
	}
	public String getCcExternalSessionID() {
		return ccExternalSessionID;
	}
	public void setCcExternalSessionID(String ccExternalSessionID) {
		this.ccExternalSessionID = ccExternalSessionID;
	}
	public String getCcAddVrfyCode() {
		return ccAddVrfyCode;
	}
	public void setCcAddVrfyCode(String ccAddVrfyCode) {
		this.ccAddVrfyCode = ccAddVrfyCode;
	}
	public String getCcTax() {
		return ccTax;
	}
	public void setCcTax(String ccTax) {
		this.ccTax = ccTax;
	}
	public String getCcNewDomainKey() {
		return ccNewDomainKey;
	}
	public void setCcNewDomainKey(String ccNewDomainKey) {
		this.ccNewDomainKey = ccNewDomainKey;
	}
	public String getCcType() {
		return ccType;
	}
	public void setCcType(String ccType) {
		this.ccType = ccType;
	}
	public String getActualOrderTotal() {
		return actualOrderTotal;
	}
	public void setActualOrderTotal(String actualOrderTotal) {
		this.actualOrderTotal = actualOrderTotal;
	}
	public String getCcMerchantId() {
		return ccMerchantId;
	}
	public void setCcMerchantId(String ccMerchantId) {
		this.ccMerchantId = ccMerchantId;
	}
	public String getSaveCard() {
		return saveCard;
	}
	public void setSaveCard(String saveCard) {
		this.saveCard = saveCard;
	}
	public String getMobileUserId() {
		return mobileUserId;
	}
	public void setMobileUserId(String mobileUserId) {
		this.mobileUserId = mobileUserId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMobileUserName() {
		return mobileUserName;
	}
	public void setMobileUserName(String mobileUserName) {
		this.mobileUserName = mobileUserName;
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
	public String getCcResponseCode() {
		return ccResponseCode;
	}
	public void setCcResponseCode(String ccResponseCode) {
		this.ccResponseCode = ccResponseCode;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getCcTransactionId() {
		return ccTransactionId;
	}
	public void setCcTransactionId(String ccTransactionId) {
		this.ccTransactionId = ccTransactionId;
	}
	public String getCcExp() {
		return ccExp;
	}
	public void setCcExp(String ccExp) {
		this.ccExp = ccExp;
	}
	public String getCardHolder() {
		return cardHolder;
	}
	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}
	public String getCcNumber() {
		return ccNumber;
	}
	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getRenderContent() {
		return renderContent;
	}
	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String performCartOperation() {
		 request =ServletActionContext.getRequest();
		 HttpSession session = request.getSession();
		 try{
			//CustomServiceProvider
			if(CommonUtility.customServiceUtility()!=null) {
				int count = CommonUtility.customServiceUtility().checkItemsFordifferentLocation(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), session.getId(),session, keyword);
				if(count > 0) {
					session.setAttribute("itemsWithDifferentLocation", 'N');
					renderContent = "success";
				}
			}
			//CustomServiceProvider
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return "success"; 
	}
	public String getShipToStoreWarehouseCode(){
		request =ServletActionContext.getRequest();
		try{
			String warehouse = CommonUtility.validateString(request.getParameter("warehouse"));
			renderContent = "";
			//CustomServiceProvider
			if(CommonUtility.customServiceUtility()!=null && warehouse.length()>0) {
				renderContent = CommonUtility.customServiceUtility().getShipToStoreWarehouseCode(warehouse);
			}
			//CustomServiceProvider
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	public String CustomLocationsByZipCode() throws Exception {
		
		request =ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, String> distinctState = new LinkedHashMap<String, String>();
		if(sessionUserId==null || sessionUserId.trim().length()<1){
			    session = request.getSession();
			    LoginAuthentication authentication = new LoginAuthentication();
				authentication.authenticate("web", "web", session);
				sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
		}
		String selectedState = "";
		String selectedZipCode = "";
		int selectedMiles = 0;
		String latitude  = "";
        String longitude = "";
        String customParam = "";
        String selectedVertical="";
        
        customParam = CommonUtility.validateString(request.getParameter("customParam"));
        selectedVertical= CommonUtility.validateString(request.getParameter("selectedVertical"));
        locationDataFrom = CommonDBQuery.getSystemParamtersList().get("GET_MAP_FROM");
	    selectedZipCode = CommonUtility.validateString(request.getParameter("selectedZipCode"));	
	    selectedState = CommonUtility.validateString(request.getParameter("selectedState"));
		ArrayList<LocationModel> locationList = new ArrayList<LocationModel>();
		if (locationDataFrom!=null && locationDataFrom.equalsIgnoreCase("database")) {
			String getLocationInfo = "";
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("LOAD_WAREHOUSE_LOCATIONS")).equalsIgnoreCase("Y")){
				getLocationInfo = PropertyAction.SqlContainer.get("getBranchLocationsFromWarehouse");
			}else{
				if(CommonUtility.validateString(selectedVertical).length()>0 && CommonUtility.validateString(selectedState).length()>0){
					getLocationInfo = PropertyAction.SqlContainer.get("getBranchLocationFilterQuery") +" WHERE UPPER(WAREHOUSE_CODE)=UPPER('"+selectedVertical+"') AND UPPER(STATE)=UPPER('"+selectedState+"')";
				}else{
					getLocationInfo = PropertyAction.SqlContainer.get("getBranchLocation");
				}
			}
			Connection conn = null;
			ResultSet rs = null;
			PreparedStatement preStat = null;

			try {
				conn = ConnectionManager.getDBConnection();
				preStat = conn.prepareStatement(getLocationInfo);
				rs = preStat.executeQuery();

				while (rs.next()) {				
					LocationModel locationVal = new LocationModel();
					locationVal.setBranchName(CommonUtility.validateString(rs.getString("LOCATION_TITLE")));
					locationVal.setBranchId(CommonUtility.validateString(rs.getString("WAREHOUSE_ID")));
					locationVal.setBranchCode(CommonUtility.validateString(rs.getString("WAREHOUSE_CODE")));
					locationVal.setLatitude(CommonUtility.validateString(rs.getString("LATTITUDE")));
					locationVal.setLongitude(CommonUtility.validateString(rs.getString("LONGITUDE")));
					locationVal.setPhone(CommonUtility.validateString(rs.getString("AC")!=null && (rs.getString("AC")).length()>0 && CommonUtility.validateNumber(rs.getString("AC"))>0?CommonUtility.validateString(rs.getString("AC")) + "-":"") + CommonUtility.validateString(rs.getString("PHONE")));
					locationVal.setFaxNum(CommonUtility.validateString(rs.getString("AC")!=null && (rs.getString("AC")).length()>0 && CommonUtility.validateNumber(rs.getString("AC"))>0?CommonUtility.validateString(rs.getString("AC")) + "-":"")+ CommonUtility.validateString(rs.getString("FAX")));
					locationVal.setLocality(CommonUtility.validateString(rs.getString("CITY")) + ", " + CommonUtility.validateString(rs.getString("STATE")) + " " + CommonUtility.validateString(rs.getString("ZIP")));
					locationVal.setCity(CommonUtility.validateString(rs.getString("CITY")));
					locationVal.setState(CommonUtility.validateString(rs.getString("STATE")));
					locationVal.setZip(CommonUtility.validateString(rs.getString("ZIP")));
					locationVal.setStreet(CommonUtility.validateString(rs.getString("STREET_ADDRESS")));
					locationVal.setWorkHour(CommonUtility.validateString(rs.getString("WORK_HOUR")));
					locationVal.setNote(CommonUtility.validateString(rs.getString("NOTE")));
					locationVal.setEmail(CommonUtility.validateString(rs.getString("EMAIL")));
					locationVal.setCountry(CommonUtility.validateString(rs.getString("COUNTRY")));
					locationVal.setTollFreeNum(CommonUtility.validateString(rs.getString("TOLL_FREE")));
					distinctState.put(CommonUtility.validateString(rs.getString("STATE")), CommonUtility.validateString(rs.getString("STATE")));
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("LOAD_WAREHOUSE_LOCATIONS")).equalsIgnoreCase("Y")){
						locationVal.setCustomFields(CommonDBQuery.getWareHouseCustomFieldsList().get(rs.getString("WAREHOUSE_ID")));
					}
					if(CommonDBQuery.getWareHouseCustomFieldsList().get(rs.getString("WAREHOUSE_ID")) != null && CommonDBQuery.getWareHouseCustomFieldsList().get(rs.getString("WAREHOUSE_ID")).size() > 0) {
						ArrayList<CustomFieldModel> warehouseCustomFields = CommonDBQuery.getWareHouseCustomFieldsList().get(rs.getString("WAREHOUSE_ID"));
						for (CustomFieldModel customFieldModel : warehouseCustomFields) {
							if(CommonUtility.validateString(customFieldModel.getFieldName()).equalsIgnoreCase("WAREHOUSE_IMAGE")) {
								locationVal.setWarehouseImage(customFieldModel.getCustomFieldvalue());
							}
						}
					}
					locationList.add(locationVal);					
				}
				
				if(selectedZipCode != "") {
					locationList = CommonUtility.sortBasedOnClosestLocationwareHouse(locationList, selectedZipCode);
				}else {
					locationList = CommonUtility.sortBasedOnClosestLocationwareHouse(locationList, selectedState);
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally {
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			}
		}
		Gson priceInquiry = new Gson();
		locationResponseList = priceInquiry.toJson(locationList);
		System.out.println(locationResponseList);
		
		contentObject.put("locationResponseList", locationResponseList);
		contentObject.put("locationDataFrom", locationDataFrom);
		contentObject.put("mapCenterCord", CommonDBQuery.getSystemParamtersList().get("MAP_CENTRE_COORDINATES"));
		contentObject.put("zoomVal",CommonDBQuery.getSystemParamtersList().get("MAP_ZOOM_VALUE"));
		contentObject.put("locationList", locationList);
		contentObject.put("distinctState", distinctState);
		contentObject.put("selectedState", CommonUtility.validateString(selectedState));
		contentObject.put("selectedMiles", CommonUtility.validateParseIntegerToString(selectedMiles));
		contentObject.put("selectedZipCode", CommonUtility.validateString(selectedZipCode));
		contentObject.put("selectedVertical", CommonUtility.validateString(selectedVertical));
		contentObject.put("responseType", "locationsWithin");			
		if(CommonUtility.validateString(request.getParameter("requestType")).equalsIgnoreCase("JSON")){
			renderContent = locationResponseList;
		}else{
			renderContent = LayoutGenerator.templateLoader("BranchLocationPage", contentObject , null, null, null);
		}
		return "success";
	}

	public String notificationCount(){
	  request =ServletActionContext.getRequest();
	  HttpSession session = request.getSession();
	  String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
	  int userId = CommonUtility.validateNumber(sessionUserId);		
	  try{
	   renderContent = "";
	   if(userId>1){
	    //Get count of open orders
	    SalesOrderManagement salesObj = new SalesOrderManagementImpl();
	    SalesModel salesInputParameter = new SalesModel();
	    salesInputParameter.setCustomerNumber(session.getAttribute("customerId")!=null?(String) session.getAttribute("customerId"):"0");
	    ArrayList<SalesModel> orderList = salesObj.OpenOrders(salesInputParameter);
	    int openOrdersCount = 0;
	    for (SalesModel each : orderList) {
			   if(each.getOrderStatus()!=null && !each.getOrderStatus().equalsIgnoreCase("B"))
			   {
				   openOrdersCount +=1;
			   }
			
		}
	    //Get count of products groups and approve carts
	    String buyingCompanyId = (String)session.getAttribute("buyingCompanyId");
	    LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	    contentObject.put("session", session);
	    contentObject = ProductsDAO.getSavedGroupNameByUserIdDao(userId, contentObject, CommonUtility.validateNumber(buyingCompanyId));
	    ArrayList<ProductsModel> groupListData = (ArrayList<ProductsModel>) contentObject.get("groupListData");
	    int productGroupsCount = groupListData!=null?groupListData.size():0;
	    ArrayList<ProductsModel> approveCartData = (ArrayList<ProductsModel>) contentObject.get("approveCartData");
	    int approveCartsCount = approveCartData!=null?approveCartData.size():0;
	    //Get Invoiced count
	    int historyconunt = 0;
	   // salesInputParameter.setStartDate(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("Open_Invoice_StartDate")));
	    Date date = new Date();		
		Calendar c = Calendar.getInstance(); 
		Format formatter=new SimpleDateFormat("MM/dd/yyyy");
		int minusMonth = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("ORDER_HISTORY_DEFAULT_DURATION"))>0?CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("ORDER_HISTORY_DEFAULT_DURATION")):1;
		minusMonth = minusMonth*-1;
		c.add(Calendar.MONTH, minusMonth);
		String startDate = formatter.format(c.getTime());
		System.out.println(new Date());
		salesInputParameter.setStartDate(startDate);
	    salesInputParameter.setEndDate(formatter.format(date));
	    salesInputParameter.setCustomerNumber(session.getAttribute("customerId")!=null?(String) session.getAttribute("customerId"):"0");
	    ArrayList<SalesModel> orderHistoryList = salesObj.OrderHistory(salesInputParameter);
	   for (SalesModel each : orderHistoryList) {
		   if(each.getOrderStatus()!=null && each.getOrderStatus().equalsIgnoreCase("I"))
		   {
			   historyconunt +=1;
		   }
		
	}
	    
	    //open orders count | products groups count | approve carts count
	    renderContent = openOrdersCount+"|"+productGroupsCount+"|"+approveCartsCount+"|"+historyconunt;
	   }
	  }catch(Exception e){
	   e.printStackTrace();
	  }
	  return "success";

	}
	public ArrayList<LocationModel> sortBasedOnClosestLocationwareHouse(ArrayList<LocationModel> locationList, String zipCode){
		JsonObject location = CIMM2VelocityTool.geoLocationByZipCode(zipCode);
		if(location != null && location.get("status").toString().replace("\"", "").equals("OK")) {
			JsonObject extractLatAndLang = CommonUtility.extractLatAndLang(location);
			if(extractLatAndLang != null) {
				double lat1 = Double.parseDouble(extractLatAndLang.get("lat").toString());
				double lng1 = Double.parseDouble(extractLatAndLang.get("lng").toString());
				
				for(LocationModel wareHouse : locationList) {
					//wareHouse.setDistance(BranchLocation.distanceCalculatorBetweenTwoPoints(lat1, lng1, wareHouse.getLatitude(), wareHouse.getLongitude(), "K"));
					double wLat = Double.parseDouble(wareHouse.getLatitude());
					double wLan = Double.parseDouble(wareHouse.getLongitude());
					wareHouse.setDistance(CommonUtility.calculateDistBetweenPoints(lat1, lng1, wLat, wLan));
				}
				Collections.sort(locationList, new Comparator<LocationModel>() {
					@Override
					public int compare(LocationModel o1, LocationModel o2) {
						return (int) (o1.getDistance() - o2.getDistance());
					}
				});
				Collections.reverse(locationList);
				
			}
		}
		return locationList;
	}
	
	public String removeSessionvalues() {
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();;
			//CustomServiceProvider
				if(CommonUtility.customServiceUtility()!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_CLUSTER_DATA")!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_CLUSTER_DATA").trim().equalsIgnoreCase("Y")) {
					 CommonUtility.customServiceUtility().removesessionvalues(session);
				}	
		//CustomServiceProvider
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	public String getWarehouseDetailforAnonymous_rep() {
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();;
			String targetid = request.getParameter("targetbranch");
			//CustomServiceProvider
			if(targetid !=null) {
				if(CommonUtility.customServiceUtility()!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_CLUSTER_DATA")!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_CLUSTER_DATA").trim().equalsIgnoreCase("Y")) {
					renderContent = CommonUtility.customServiceUtility().getwareHouseDetailforanonymous_rep(targetid);
				}	
			}
			
		//CustomServiceProvider
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	public String getEventsCustom() {
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    int userId=0;
	    String sql = "";
	    LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	    ArrayList<EventModel> recentEvents =  new ArrayList<EventModel>();
	    ArrayList<EventModel> eventCustomFieldsList = new ArrayList<EventModel>();
	    EventModel customEvent = new EventModel();
	    try {
	    	conn = ConnectionManager.getDBConnection();
            sql = "SELECT * FROM EVENT_MANAGER WHERE TO_CHAR(TO_DATE(REPLACE(END_DATE,'T',' '),'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS') < TO_CHAR(sysdate,'YYYYMMDDHH24MISS') AND DISPLAY_ONLINE = 'Y' ORDER BY START_DATE DESC";
        	System.out.println(sql);
        	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
        	pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
            	//COST,EVENT_CATEGORY,LOCATION,ADDRESS,CONTACT,TOTAL_SEATS,BOOKED_SEATS   
            	
            	EventModel event = new EventModel();
            	event.setId(rs.getInt("EVENT_ID"));
            	event.setDate(UsersDAO.returnDate(rs.getString("START_DATE"),rs.getString("TIME_ZONE")));
            	event.setTitle(rs.getString("EVENT_TITLE"));
            	
            	if(rs.getString("STATUS").equals("Y") && rs.getString("DISPLAY_ONLINE").equals("Y")){
            		recentEvents.add(event);	
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
            	
            if(recentEvents!=null && recentEvents.size()>0 && eventCustomFieldsList!=null &&  eventCustomFieldsList.size()>0)
       	     {
           		     	    
       	    	 for(EventModel eventList:recentEvents){
       	    		 ArrayList<EventModel> customFieldsList = new ArrayList<EventModel>();
       	    		for(EventModel eventCustomList:eventCustomFieldsList){
       	    			if(eventCustomList.getId() == eventList.getId()){
       	    				if(eventCustomList.getFieldName() != null && CommonUtility.validateString(eventCustomList.getFieldName()).equalsIgnoreCase("EVENT_IMAGE_URL"))
    	    				{
    	    					eventList.setImageName(eventCustomList.getFieldValue());
    	    				}
	       	    			EventModel event = new EventModel();
	       	    			event.setEventsCustomFieldList(eventCustomList);
	       	    			customFieldsList.add(event);
       	    			}
       	    	   }
       	    		eventList.setCustomFieldsList(customFieldsList);
       	         }
       	    }
            
            contentObject.put("recentEvents", recentEvents);
            contentObject.put("responseType", "customEvent");
            renderContent = renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
           
    } catch(SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    		ConnectionManager.closeDBResultSet(rs);
    		ConnectionManager.closeDBPreparedStatement(pstmt);
    		ConnectionManager.closeDBConnection(conn);	
      }
	return "success";
	}
	public String getAcceptHostedFormToken() {
		request =ServletActionContext.getRequest();
		renderContent = "";
		try{
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			LinkedHashMap<String, String> tokenRequest = new LinkedHashMap<String, String>();
			tokenRequest.put("orderTotal", CommonUtility.validateString(request.getParameter("orderTotal")));
			if(userId>1) {
				String customerProfileId = UsersDAO.getCustomerProfileId(userId);
				if(CommonUtility.validateString(customerProfileId).length()>0) {
					tokenRequest.put("customerProfileId", customerProfileId);
				}else {
					SalesModel salesInputParameter = new SalesModel();
					String name = session.getAttribute("userFirstName")!=null?(String)session.getAttribute("userFirstName"):"";
					name += " "+session.getAttribute("userLastName")!=null?(String)session.getAttribute("userLastName"):"";
					salesInputParameter.setName(name);
					salesInputParameter.setCustomerERPId(CommonUtility.validateParseIntegerToString(userId));
					SalesOrderManagement salesOrderObj = new SalesOrderManagementImpl();
					SalesModel salesOutParameter = salesOrderObj.AuthorizeDotNetSaveCardAuthentication(salesInputParameter);
					if(salesOutParameter!=null && CommonUtility.validateString(salesOutParameter.getCustomerProfileId()).length()>0){
						UsersDAO.updateCustomerProfileId(salesOutParameter.getCustomerProfileId(),userId);
						tokenRequest.put("customerProfileId", salesOutParameter.getCustomerProfileId());
					}
				}
			}
			tokenRequest.put("cardHolderFirstName", session.getAttribute("userFirstName")!=null?(String)session.getAttribute("userFirstName"):"");
			tokenRequest.put("cardHolderLastName", session.getAttribute("userLastName")!=null?(String)session.getAttribute("userLastName"):"");

			tokenRequest.put("excludeAddress", CommonUtility.validateString(request.getParameter("excludeAddress")));
			tokenRequest.put("billAddress1", CommonUtility.validateString(request.getParameter("billAddress1")));
			tokenRequest.put("billAddress2", CommonUtility.validateString(request.getParameter("billAddress2")));
			tokenRequest.put("companyName", CommonUtility.validateString(request.getParameter("companyName")));
			tokenRequest.put("billCity", CommonUtility.validateString(request.getParameter("billCity")));
			tokenRequest.put("billState", CommonUtility.validateString(request.getParameter("billState")));
			tokenRequest.put("billCountry", CommonUtility.validateString(request.getParameter("billCountry")));
			tokenRequest.put("billZipcode", CommonUtility.validateString(request.getParameter("billZipcode")));
			
			tokenRequest.put("pageReturnOptions", CommonUtility.validateString(request.getParameter("pageReturnOptions")));
			tokenRequest.put("pageButtonOptions", CommonUtility.validateString(request.getParameter("pageButtonOptions")));
			tokenRequest.put("pageStyleOptions", CommonUtility.validateString(request.getParameter("pageStyleOptions")));
			tokenRequest.put("pagePaymentOptions", CommonUtility.validateString(request.getParameter("pagePaymentOptions")));
			tokenRequest.put("pageSecurityOptions", CommonUtility.validateString(request.getParameter("pageSecurityOptions")));
			tokenRequest.put("pageShippingAddressOptions", CommonUtility.validateString(request.getParameter("pageShippingAddressOptions")));
			tokenRequest.put("pageBillingAddressOptions", CommonUtility.validateString(request.getParameter("pageBillingAddressOptions")));
			tokenRequest.put("pageCustomerOptions", CommonUtility.validateString(request.getParameter("pageCustomerOptions")));
			tokenRequest.put("pageOrderOptions", CommonUtility.validateString(request.getParameter("pageOrderOptions")));
			tokenRequest.put("pageIFrameCommunicatorUrl", CommonUtility.validateString(request.getParameter("pageIFrameCommunicatorUrl")));
			UserManagement usersObj = new UserManagementImpl();
			renderContent = usersObj.requestAcceptHostedFormToken(tokenRequest);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	@SuppressWarnings("unchecked")
	public String userPreferencesOperation() {
		request =ServletActionContext.getRequest();
		renderContent = "";
		try {
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String key = CommonUtility.validateString(request.getParameter("key"));
			String value = CommonUtility.validateString(request.getParameter("value"));
			String jsonData = UsersDAO.getUserPreferencesJsonData(userId);
			JSONObject jsonObj;
			if(CommonUtility.validateString(jsonData).length()>0) {
				Object obj = new JSONParser().parse(jsonData);
				jsonObj = (JSONObject) obj;
			}else {
				jsonObj = new JSONObject();
			}
			jsonObj.put(key, value);
			int result = UsersDAO.updateUserPreferencesJsonData(userId,jsonObj.toString());
			if(result>0) {
				renderContent = "success";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	@SuppressWarnings("unchecked")
	public String getAllStoreAvailability() {
		WarehouseModel warehouseModel = new WarehouseModel();
		request = ServletActionContext.getRequest();
		String productIdList = "";
		String latitude = "0";
		String longitude = "0";
		String[] priceIdList = null;
		renderContent = "";
		Cimm2BCentralPriceAndAvailability storeList = null;
		ArrayList<WarehouseModel> warehouseList = null;
		try {
			if(CommonUtility.validateString(request.getParameter("latitude")).trim().length()>0){
				latitude = request.getParameter("latitude");
			}
			if(CommonUtility.validateString(request.getParameter("longitude")).trim().length()>0){
				longitude = request.getParameter("longitude");
			}
			if(CommonUtility.validateString(request.getParameter("productIdList")).trim().length()>0){
				productIdList = request.getParameter("productIdList");
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTNUMBER_SPLIT_CHARACTER")).length()>0) {
					priceIdList = productIdList.split(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTNUMBER_SPLIT_CHARACTER")));	
				}else {
					priceIdList = productIdList.split(",");
				}
			}
			if(CommonUtility.customServiceUtility()!=null && priceIdList!=null && priceIdList.length>0) {
				storeList = CommonUtility.customServiceUtility().getAllStoreAvailability(priceIdList);
			}
			if(!latitude.equalsIgnoreCase("0") && !longitude.equalsIgnoreCase("0")) {
				warehouseModel.setLatitude(latitude);
				warehouseModel.setLongitude(longitude);
				warehouseList = new UsersAction().getWareHouseListAndDistanceToUsersLocation(warehouseModel);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("storeList", storeList);
			jsonObject.put("warehouseList", warehouseList);
			Gson gson = new Gson();
			renderContent = gson.toJson(jsonObject);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	public String getTopSelledCategories() {
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String customerId= (String)session.getAttribute("customerId");
		String fromDate=CommonUtility.validateString(request.getParameter("fromDate"));
		String endDate=CommonUtility.validateString(request.getParameter("endDate"));
		String flag="";
		ArrayList<ProductsModel> categoryList = new ArrayList<ProductsModel>();
		
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductsModel data =null;
		Gson gson = new Gson(); 
		
	/*	//customer
	  SELECT * FROM 
(select tt.CATEGORY_NAME NAME, sum(to_char(oi.ext_price,'999999999.99')) Extended_Price, oi.MANUFACTURER MANUFACTURER from order_items oi, ITEM_CLASSIFICATION ic,
TAXONOMY_TREE tt, orders od where oi.item_id = ic.item_id and ic.TAXONOMY_TREE_ID=tt.TAXONOMY_TREE_ID and ic.DEFAULT_CATEGORY='Y'
and oi.ext_price is not null  and oi.ext_price>0 and oi.UPDATED_DATETIME BETWEEN to_date(?) AND to_date(?) and od.CUSTOMER_ERP_ID=?
group by tt.CATEGORY_NAME, oi.MANUFACTURER ORDER BY EXTENDED_PRICE DESC) WHERE;




//user
		SELECT * FROM 
(select tt.CATEGORY_NAME NAME, sum(to_char(oi.ext_price,'999999999.99')) Extended_Price, oi.MANUFACTURER MANUFACTURER from order_items oi, ITEM_CLASSIFICATION ic,
TAXONOMY_TREE tt, orders od where oi.item_id = ic.item_id and ic.TAXONOMY_TREE_ID=tt.TAXONOMY_TREE_ID and ic.DEFAULT_CATEGORY='Y'
and oi.ext_price is not null  and oi.ext_price>0 and oi.UPDATED_DATETIME BETWEEN to_date(?) AND to_date(?) and od.USER_ID=?
group by tt.CATEGORY_NAME, oi.MANUFACTURER ORDER BY EXTENDED_PRICE DESC);       
*/
		
        try
        {
        	conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("getTopSelledCategoriesByCustomer");
        	pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromDate);
			pstmt.setString(2, endDate);
        	if(CommonUtility.validateString(flag).equalsIgnoreCase("user")){
        		sql = PropertyAction.SqlContainer.get("getTopSelledCategoriesByUser");
        		pstmt.setInt(3, userId);	
        	}else {
        		pstmt.setString(3, customerId);	
        	}
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				data = new ProductsModel();
				data.setCategoryName(rs.getString("NAME"));
				data.setExtendedPrice(rs.getInt("EXTENDED_PRICE"));
				data.setManufacturerName(rs.getString("MANUFACTURER"));
				categoryList.add(data);
			}
			renderContent = gson.toJson(categoryList);
        }catch (Exception e) {
			e.printStackTrace();
        }finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		
		return "success";
	}
	public String getCustomerDetails() {
		UserManagement userObj = new UserManagementImpl();
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		UsersModel userModelForERP = new UsersModel();
		if (CommonUtility.validateString(request.getParameter("customerId")).length() > 0) {
			try {
				userModelForERP.setCustomerId(CommonUtility.validateString(request.getParameter("customerId")));
				userModelForERP.setSession(session);
				userObj.getCustomerDataFromERP(userModelForERP);
				if (CommonUtility.customServiceUtility() != null && session != null
						&& session.getAttribute("customerDetails") != null) {
					renderContent = CommonUtility.customServiceUtility().getShipDetails(renderContent, session);// Turtle services
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "success";
	}

	public String updateBCAddressInDB() {
		String result ="";
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		int buyingCompanyid = 0;
		Connection conn = null;
		String type= "";
		String shipId = "";
		int bcShipAddressId = 0;
		int bcBillAddressId = 0;
		String networkWarehouse="";
		if(CommonUtility.validateString(request.getParameter("type")).length() > 0){
			type = CommonUtility.validateString(request.getParameter("type"));
		}
		if(CommonUtility.validateString(request.getParameter("shipToIdSx")).length() > 0){
			shipId = CommonUtility.validateString(request.getParameter("shipToIdSx"));
		}
		if (CommonUtility.validateString(request.getParameter("selectedCustId")).length() > 0) {
			try {
				conn = ConnectionManager.getDBConnection();
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				UsersModel customerinfo = new UsersModel();
				Cimm2BCentralCustomer data = null;
				if(session!=null && session.getAttribute("customerDetails")!=null) {
				data = (Cimm2BCentralCustomer) session.getAttribute("customerDetails");
				}
				buyingCompanyid = UsersDAO.getBuyingCompanyIdByEntityId(CommonUtility.validateString(request.getParameter("selectedCustId")));
				bcBillAddressId = UsersDAO.getBcAddressBookShipBillId(buyingCompanyid, CommonUtility.validateString(request.getParameter("selectedCustId")), AddressType.Bill);
					switch(type) {
					case "Bill":
						buyingCompanyid = UsersDAO.getBuyingCompanyIdByEntityId(CommonUtility.validateString(request.getParameter("selectedCustId")));
							if (buyingCompanyid ==0 && data!= null) {
								customerinfo.setCustomerId(data.getCustomerERPId());
								customerinfo.setBillEntityId(data.getCustomerERPId());
								customerinfo.setEntityId(data.getCustomerERPId());
								customerinfo.setCustomerName(data.getCustomerName()!=null?data.getCustomerName():"");
								customerinfo.setAddress1(data.getAddress().getAddressLine1()!=null?data.getAddress().getAddressLine1():"");
								customerinfo.setAddress2(data.getAddress().getAddressLine2()!=null?data.getAddress().getAddressLine2():"");
								customerinfo.setState(data.getAddress().getState()!=null?data.getAddress().getState():"");
								customerinfo.setCity(data.getAddress().getCity()!=null?data.getAddress().getCity():"");
								customerinfo.setZipCode(data.getAddress().getZipCode()!=null?data.getAddress().getZipCode():"");
								customerinfo.setZipCodeStringFormat(data.getAddress().getZipCode()!=null?data.getAddress().getZipCode():"");
								customerinfo.setCountry(data.getAddress().getCountry()!=null?data.getAddress().getCountry():"US");
								customerinfo.setCountryCode(data.getAddress().getCountryCode()!=null?data.getAddress().getCountry():"US");
								customerinfo.setEntityName(data.getCustomerName()!=null?data.getCustomerName():"");
								customerinfo.setFirstName(data.getCustomerName()!=null?data.getCustomerName():"");
								if(data.getDefaultShipLocationId()!=null && data.getDefaultShipLocationId().length()>0){
									customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(data.getDefaultShipLocationId())));
									customerinfo.setWareHouseCodeStr(CommonUtility.validateString(data.getDefaultShipLocationId()));
								}else if(data.getHomeBranch()!=null && data.getHomeBranch().length()>0){
									customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(data.getHomeBranch())));
									customerinfo.setWareHouseCodeStr(CommonUtility.validateString(data.getHomeBranch()));
								}else{
									customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR"))));
									customerinfo.setWareHouseCodeStr(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR")));	
								}
								if(data.getContacts() != null && data.getContacts().size() > 0){
									if(CommonUtility.validateString(data.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
										customerinfo.setPhoneNo(data.getContacts().get(0).getPrimaryPhoneNumber());
									}
									if(CommonUtility.validateString(data.getContacts().get(0).getPrimaryEmailAddress()).length() > 0){
										customerinfo.setEmailAddress(data.getContacts().get(0).getPrimaryEmailAddress());
									}else {
										customerinfo.setEmailAddress(data.getAddress().getPrimaryEmailAddress());
									}
								}
								buyingCompanyid = UsersDAO.insertBuyingCompany(conn, customerinfo, 0);
								customerinfo.setBuyingCompanyId(buyingCompanyid);
								if(buyingCompanyid>0) {bcBillAddressId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo, "Bill");}
								conn.commit();
							}
						session.setAttribute("contactId", CommonUtility.validateString(request.getParameter("selectedCustId")));
						session.setAttribute("billingEntityId", CommonUtility.validateString(request.getParameter("selectedCustId")));
						session.setAttribute("buyingCompanyId", CommonUtility.validateParseIntegerToString(buyingCompanyid));
						session.setAttribute("userBuyingCompanyId", CommonUtility.validateParseIntegerToString(buyingCompanyid));
						session.setAttribute("customerId", CommonUtility.validateString(request.getParameter("customerId")));
						session.setAttribute("defaultBillToId", CommonUtility.validateParseIntegerToString(bcBillAddressId));
						result = CommonUtility.validateParseIntegerToString(bcBillAddressId);
						break;
					case "Ship":
						List<Cimm2BCentralCustomer> shipToList = null;
						shipToList = data.getCustomerLocations();
						Map<String, ArrayList<UsersModel>> allShipAddressList = UsersDAO.getAllAddressListFromBCAddressBook(buyingCompanyid);
						
						if(allShipAddressList!=null) {
							for (Map.Entry<String, ArrayList<UsersModel>> entry : allShipAddressList.entrySet()) {
								for (UsersModel uModel : entry.getValue()) {
									if(uModel.getShipToId().equalsIgnoreCase(shipId)) {
										bcShipAddressId = 	uModel.getAddressBookId();
										networkWarehouse=uModel.getHomeBranch()!=null?uModel.getHomeBranch():CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR");
										break;
									}
							}
						}
						}
						
						if (bcShipAddressId==0 && shipToList.size() > 0) {
							for (Cimm2BCentralCustomer shipTo : shipToList) {
								Cimm2BCentralAddress shipAddress = shipTo.getAddress();
								if(shipAddress.getAddressERPId() != null && shipAddress.getAddressERPId().equalsIgnoreCase(shipId)) {
									customerinfo.setAddress1(shipAddress.getAddressLine1()!=null?shipAddress.getAddressLine1():"");
									customerinfo.setAddress2(shipAddress.getAddressLine2()!=null?shipAddress.getAddressLine2():"");
									customerinfo.setCity(shipAddress.getCity()!=null?shipAddress.getCity():"");
									customerinfo.setState(shipAddress.getState()!=null?shipAddress.getState():"");
									customerinfo.setCountry(shipAddress.getCountry()!=null?shipAddress.getCountry():"");
									customerinfo.setZipCode(shipAddress.getZipCode()!=null?shipAddress.getZipCode():"");
									customerinfo.setZipCodeStringFormat(shipAddress.getZipCode()!=null?shipAddress.getZipCode():"");
									customerinfo.setEntityId(shipTo.getCustomerERPId());
									customerinfo.setCustomerName(shipAddress.getCustomerName()!=null?shipAddress.getCustomerName():"");
									customerinfo.setShipToId(shipAddress.getAddressERPId());
									customerinfo.setShipEntityId(shipTo.getCustomerERPId());
									customerinfo.setEntityId(shipTo.getCustomerERPId());
									customerinfo.setBuyingCompanyId(buyingCompanyid);
									if(shipTo.getContacts() != null && shipTo.getContacts().size() > 0 && CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
										customerinfo.setPhoneNo(CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()));
									}
									if(shipTo.getContacts() != null && shipTo.getContacts().size() > 0 && CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
										customerinfo.setPhoneNo(CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()));
									}
									if(CommonUtility.validateString(shipTo.getHomeBranch()).trim().length() > 0 && shipTo.getHomeBranch()!="0"){
										customerinfo.setWareHouseCodeStr(shipTo.getHomeBranch());
									}
									bcShipAddressId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo, "Ship");
									networkWarehouse=shipTo.getHomeBranch();
									session.setAttribute("selectedshipToIdSx",shipAddress.getAddressERPId());
									session.setAttribute("wareHouseCode",CommonUtility.validateString(networkWarehouse));
									break;
								}
							}
						}
						session.setAttribute("defaultShipToId", CommonUtility.validateParseIntegerToString(bcShipAddressId));
						session.setAttribute("shipTofirstName", customerinfo.getCustomerName());
						session.setAttribute("customerId", CommonUtility.validateString(request.getParameter("customerId")));
						if(CommonUtility.customServiceUtility()!=null && !networkWarehouse.equalsIgnoreCase(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR")))) {
							 networkWarehouse= CommonUtility.customServiceUtility().getNetworkWarehouseCode(CommonUtility.validateString(networkWarehouse));
			  			}
						//CustomServiceProvider for turtle
			  			if(CommonUtility.validateString(networkWarehouse).length()>0){
			  				session.setAttribute("wareHouseCode",CommonUtility.validateString(networkWarehouse));
			  			}
						result = CommonUtility.validateParseIntegerToString(bcShipAddressId);
						break;
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				ConnectionManager.closeDBConnection(conn);
			}
		}
		renderContent = result;
		return "success";
	}

	public String getCustomerDetailsFromErp() {
		UserManagementImpl userObj = new UserManagementImpl();
		request = ServletActionContext.getRequest();
		UsersModel userModelForERP = new UsersModel();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		try {
			userModelForERP.setCustomerId(CommonUtility.validateString(request.getParameter("searchCustId")));
			userModelForERP.setCustomerName(CommonUtility.validateString(request.getParameter("customerName")));
			userModelForERP.setDescription(CommonUtility.validateString(request.getParameter("customerDescription")));
			userModelForERP.setCustomerAccountExist(false);
			userModelForERP.setCity(CommonUtility.validateString(request.getParameter("customerCity")));
			userModelForERP.setState(CommonUtility.validateString(request.getParameter("customerState")));
			userModelForERP.setZipCode(CommonUtility.validateString(request.getParameter("customerZipCode")));
			userModelForERP.setPhoneNo(CommonUtility.validateString(request.getParameter("customerPhone")));
			Cimm2BCentralCustomerData customerData = userObj.searchCustomerInERP(userModelForERP);
			if(customerData != null) {
				ArrayList<Cimm2BCentralCustomer> customerList = customerData.getDetails();
				contentObject.put("customerList", customerList);
			}
		  contentObject.put("responseType", "AdvCustomerSearch");
          renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	public String eventPOPayment(){
		  
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		String isSavedCreditCart=request.getParameter("isSavedCreditCart");
		String studentFirstName = CommonUtility.validateString((String)session.getAttribute("studentFirstName"));
		String studentLastName = CommonUtility.validateString((String)session.getAttribute("studentLastName"));
		if(isSavedCreditCart==null){
			isSavedCreditCart="N";
		}
		try {
			if(session.getAttribute("eventRegId")!=null) {
				int eventRegId = (Integer) session.getAttribute("eventRegId");
				int eventId = 0;
				int orderId = 0;
				System.out.println(eventId);
				    
				String partNumberForEventpayment = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EVENT_PAYMENT_PART_NUMBER"));
				UsersModel userDetails = new UsersModel();
				String userName = (String) session.getAttribute(Global.USERNAME_KEY);
				int userId = UsersDAO.getUserIdFromDB(userName, "Y");
				String shipBranch = (String) session.getAttribute("shipBranchId");
				String homeBranch = (String) session.getAttribute("homeBranchId");
				Boolean userLogin= (Boolean) session.getAttribute("userLogin");
				CreditCardModel creditCardValue = null;
				if(userLogin && isSavedCreditCart.equalsIgnoreCase("Y")){
					String paymentGatewayType = "";
					if(session.getAttribute("PAYMENT_GATEWAY")!=null && session.getAttribute("PAYMENT_GATEWAY").toString().length()>0){
						paymentGatewayType = (String)session.getAttribute("PAYMENT_GATEWAY");
					}else{
						paymentGatewayType = CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY");
					}
					ccNumber = ccNumber.replaceAll("XXXX-", "");
					ccNumber = ccNumber.replaceAll("\\*", "");
					ccNumber = ccNumber.replaceAll("#", "");
					ccNumber = ccNumber.replaceAll("-", "");
					creditCardValue = new CreditCardModel();
					creditCardValue.setCardHolder(cardHolder);
					creditCardValue.setDate(ccExp);
					creditCardValue.setElementPaymentAccountId(ccTransactionId);
					creditCardValue.setAddress1(streetAddress);
					creditCardValue.setZipCode(postalCode);
	
	
					creditCardValue.setPaymentGatewayType(paymentGatewayType);
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
					creditCardValue.setCreditCardApprovalCode(ccApprovalCode);
					creditCardValue.setCreditCardTransactionID(ccTransactionId);
					creditCardValue.setCreditCardServerTimestamp(ccServerTimestamp);
					creditCardValue.setCreditCardType(ccType);
					creditCardValue.setCreditCardFee(ccFee);
					creditCardValue.setCreditCardExternalSessionID(ccExternalSessionID);
					creditCardValue.setCreditCardAddVrfyCode(ccAddVrfyCode);
					creditCardValue.setCreditCardTax(ccTax);
					creditCardValue.setCreditCardNewDomainKey(ccNewDomainKey);
					creditCardValue.setCreditCardNumber(ccNumber);
					creditCardValue.setCreditCardRefNumber(ccRefrenceCode);
					creditCardValue.setCreditCardActualOrderTotal(actualOrderTotal);
					creditCardValue.setCreditCardMerchantId(ccMerchantId);
					creditCardValue.setSaveCard(saveCard);
				}
				else{
					if(userDetails.getCreditCardList()!=null && userDetails.getCreditCardList().size()>0)
					creditCardValue = userDetails.getCreditCardList().get(0);
				}
				
				/*if(creditCardValue!=null){*/
					EventModel eventModel = null;
					if(eventRegId>0){
						//eventModel = UsersDAO.getEventRegistrationDetails(eventRegId);
						eventModel = CommonUtility.customServiceUtility().getEventRegistrationDetails(eventRegId);
					}
					if(eventModel!=null){
						eventId = eventModel.getEventId();
						HashMap<String, Integer> userAddressId = new HashMap<String, Integer>();
						if(type!=null && type.trim().equalsIgnoreCase("mobile") && mobileUserId!=null){
							userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(mobileUserName);
						}else{
							userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook((String)session.getAttribute(Global.USERNAME_KEY));
						}
							defaultBillToId = userAddressId.get("Bill");
							defaultShipToId = userAddressId.get("Ship");
						
						HashMap<String, UsersModel> userAddress = UsersDAO.getUserAddressFromBCAddressBook(defaultBillToId,defaultShipToId);
							UsersModel userBillAddress = userAddress.get("Bill");
							UsersModel userShipAddress = userAddress.get("Ship");
						
						AddressModel billAddress = new AddressModel();
						if(userBillAddress!=null){
							billAddress.setAddress1(CommonUtility.validateString(userBillAddress.getAddress1()));
							billAddress.setAddress2(CommonUtility.validateString(userBillAddress.getAddress2()));
							billAddress.setCity(CommonUtility.validateString(userBillAddress.getCity()));
							billAddress.setState(CommonUtility.validateString(userBillAddress.getState()));
							billAddress.setZipCode(CommonUtility.validateString(userBillAddress.getZipCodeStringFormat()));
							billAddress.setCountry(CommonUtility.validateString(userBillAddress.getCountry()));
							billAddress.setAddressType(CommonUtility.validateString(userBillAddress.getAddressType()));
						}
						AddressModel shipAddress = new AddressModel();
						if(userShipAddress!=null){
							shipAddress.setAddress1(CommonUtility.validateString(userShipAddress.getAddress1()));
							shipAddress.setAddress2(CommonUtility.validateString(userShipAddress.getAddress2()));
							shipAddress.setCity(CommonUtility.validateString(userShipAddress.getCity()));
							shipAddress.setState(CommonUtility.validateString(userShipAddress.getState()));
							shipAddress.setZipCode(CommonUtility.validateString(userShipAddress.getZipCodeStringFormat()));
							shipAddress.setCountry(CommonUtility.validateString(userShipAddress.getCountry()));
							shipAddress.setShipToId(CommonUtility.validateString((String)session.getAttribute("selectedShipToId")));
							shipAddress.setAddressType(CommonUtility.validateString(userShipAddress.getAddressType()));
						}
						SalesOrderManagementModel salesOrderInput = new SalesOrderManagementModel();
						salesOrderInput.setUserName(CommonUtility.validateString(userName));
						
						if(userBillAddress!=null && CommonUtility.validateString(userBillAddress.getEntityId()).length()>0){
							salesOrderInput.setBillEntityId(CommonUtility.validateString(userShipAddress.getEntityId()));
						}else{
							salesOrderInput.setBillEntityId(CommonUtility.validateString(userDetails.getEntityId()));
						}
						
						if(userShipAddress!=null && CommonUtility.validateString(userShipAddress.getEntityId()).length()>0){
							salesOrderInput.setShipEntityId(CommonUtility.validateString(userBillAddress.getEntityId()));
						}else{
							if(userDetails.getShipIdList()!=null && CommonUtility.validateString(userDetails.getShipIdList().get(0)).length()>0){
								salesOrderInput.setShipEntityId(CommonUtility.validateString(userDetails.getShipIdList().get(0)));
							}else{
								salesOrderInput.setShipEntityId(CommonUtility.validateString(userDetails.getEntityId()));
							}
						}
						
						if(CommonUtility.validateString(homeBranch).length()>0){
							salesOrderInput.setSelectedBranch(CommonUtility.validateString(homeBranch));
						}else{
							salesOrderInput.setSelectedBranch(CommonUtility.validateString(userDetails.getBranchID()));
						}
	
						ArrayList<Cimm2BCentralLineItem> lineItemdetails = new ArrayList<Cimm2BCentralLineItem>();
						Cimm2BCentralLineItem lineItem = new Cimm2BCentralLineItem();
						lineItem.setItemId(partNumberForEventpayment);
						lineItem.setPartNumber(partNumberForEventpayment);
						lineItem.setQty(1);
						lineItem.setUom("ea");
						if(!studentFirstName.isEmpty() && !studentLastName.isEmpty()){
							lineItem.setLineItemComment(eventModel.getTitle() + " " + studentFirstName + " " + studentLastName);
						}else{
							lineItem.setLineItemComment(eventModel.getTitle() + " " +eventModel.getFirstName()+ " " + eventModel.getLastName());
						}
						lineItem.setUnitPrice(eventModel.getCost());
						lineItem.setExtendedPrice(eventModel.getCost());
						lineItemdetails.add(lineItem);
						
						salesOrderInput.setBillAddress(billAddress);
						salesOrderInput.setShipAddress(shipAddress);
						if(CommonUtility.validateString(eventModel.getPaymentMode()).equalsIgnoreCase("Credit Card"))
							salesOrderInput.setCreditCardValue(creditCardValue);
						salesOrderInput.setComments(eventModel.getDescription());
						salesOrderInput.setOrderStatus(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EVENT_ORDERSTATUS")));
						salesOrderInput.setOrderStatusCode(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EVENT_ORDERSTATUS_CODE")));
						//salesOrderInput.setOrderedBy(CommonUtility.validateString(userName));
						if(eventModel.getLastName()!=null)
							salesOrderInput.setOrderedBy(eventModel.getFirstName()+ " " + eventModel.getLastName());
						else
							salesOrderInput.setOrderedBy(eventModel.getFirstName());
						salesOrderInput.setSession(session);
						salesOrderInput.setLineItems(lineItemdetails);
						salesOrderInput.setPurchaseOrderNumber(eventModel.getPoNumber());
					    salesOrderInput.setPartNumberForEventpayment(partNumberForEventpayment);
					    salesOrderInput.setOrderId(eventRegId);
						
						SalesModel erpOrderDetail = new SalesModel();
						SalesOrderManagement salesOrderSubmit = new SalesOrderManagementImpl();
						erpOrderDetail = salesOrderSubmit.submitOrderToERP(salesOrderInput);
						contentObject.put("orderDetail", erpOrderDetail);
						contentObject.put("confirmationId", erpOrderDetail.getOrderID());
						contentObject.put("eventRegistrationId", eventRegId);
						contentObject.put("eventId", eventId);
						contentObject.put("eventTitle", eventModel.getTitle());
						contentObject.put("eventCost", eventModel.getCost());
						contentObject.put("firstName", eventModel.getFirstName());
						contentObject.put("lastName", eventModel.getLastName());
						contentObject.put("purchaseOrderNumber", eventModel.getPoNumber());
						contentObject.put("paymentMode",eventModel.getPaymentMode());
						contentObject.put("externalSystemId", erpOrderDetail.getExternalSystemId());
						if(erpOrderDetail.getOrderID()!=null && !erpOrderDetail.getOrderID().contains("Error")){
						EventDetails eventsDetails = new EventDetails();
						
						EventModel event = UsersDAO.getEventDetails(eventId);
						
						SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
						SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm"); 
						SimpleDateFormat time = new SimpleDateFormat("HH:mm");
						String date = sdf.format(new Date());
						String address = event.getAddress()+" "+event.getAddress2()+" "+event.getCity()+" "+event.getState()+" "+" "+event.getCountry();
						eventsDetails.setFirstName(eventModel.getFirstName());
						eventsDetails.setLastName(eventModel.getLastName());
						eventsDetails.setToday(date);
						eventsDetails.setAddress(address);
						eventsDetails.setStudentFirstName(studentFirstName);
						eventsDetails.setStudentLastName(studentLastName);
						session.removeAttribute("studentFirstName");
						session.removeAttribute("studentFirstName");
										
						eventsDetails.setTitle(event.getTitle());
						Date startDate = dateFormat.parse(event.getDate());
						eventsDetails.setEventDate(sdf.format(startDate));
						Date t1 = dateFormat.parse(event.getDate());
						Date t2 = dateFormat.parse(event.getEnd());			 
						eventsDetails.setEventTime(time.format(t1)+" AM - "+time.format(t2)+" PM");
						eventsDetails.setPaymentMode(eventModel.getPaymentMode());
						/*if(eventModel.getPaymentMode()!=null && eventModel.getPaymentMode().trim().equalsIgnoreCase("Purchase Order")){}*/
						//eventsDetails.setPoNum(event.getPoNumber());
						eventsDetails.setCost(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(eventModel.getCost())));
						eventsDetails.setCourseLocation(event.getLocation());
						eventsDetails.setContactEmail(CommonUtility.validateString(event.getNotification()));
						eventsDetails.setStudentEmail(eventModel.getEmail());
						eventsDetails.setCompanyName(eventModel.getCompanyName());
						eventsDetails.setStudentPhone(eventModel.getPhoneNumber());
						eventsDetails.setContactPhone(eventModel.getPhoneNumber());
						eventsDetails.setContactMobilePhone(eventModel.getPoNumber());
						eventsDetails.setPoNum(eventModel.getPoNumber());
						eventsDetails.setErpOrderId(CommonUtility.validateString(erpOrderDetail.getOrderID()));
						//eventsDetails.setN(CommonUtility.validateString(event.getNotification()));
						
						SendMailUtility sendMailUtility = new SendMailUtility();
						boolean flag =false; 
						boolean flag1 = false;
						flag1 = sendMailUtility.sendEventMailToCoOrdinator(eventId,eventsDetails);
						if(CommonUtility.validateString(CommonDBQuery.systemParametersListVal.get("EVENTS_MAIL_TO_REGISTERER")).length()>0 && CommonDBQuery.systemParametersListVal.get("EVENTS_MAIL_TO_REGISTERER").equalsIgnoreCase("Y")){
							flag = sendMailUtility.sendEventMailToRegisterer(eventId,eventsDetails);
						}
						if(flag && flag1){
							int cnt = UsersDAO.updateMailSentStatus(eventRegId);
							if(cnt>0){
								System.out.println("Event Registration Mail Sent Successfully!");
								contentObject.put("EventMsg", "Event Registration Mail Sent Successfully!");
							}
						}else{
							contentObject.put("EventMsg", "Mail not sent successfully!");
							System.out.println("Mail not sent successfully!");
						}
						}
						else{
							contentObject.put("EventMsg", "Something went wrong. Please try again");
						}
						renderContent = LayoutGenerator.templateLoader("EventPaymentResponse", contentObject, null, null, null);
					}
					else{
						contentObject.put("isEventCancelled", "Y");
						renderContent = LayoutGenerator.templateLoader("EventPaymentResponse", contentObject, null, null, null);
					}
				session.removeAttribute("eventRegId");
				session.removeAttribute("fromPage");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return "success";
	}
	
	public String checkRockwellItems() throws SQLException {
		renderContent = "";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		boolean login = (Boolean) session.getAttribute("userLogin");
		String userName =request.getParameter("userName");
		int rockwellLocation = 0;
		int rockwellItem = 0;
		System.out.println(getCartItemList());
		int selectedShippingAddressId=0;
		HashMap<String, String> shippingCustomFieldData = new HashMap<String, String>();
		int subsetId = 0;
		int generalSubset = 0;
		String tempSubset = (String) session.getAttribute("userSubsetId");
		subsetId = CommonUtility.validateNumber(tempSubset);
		String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
		Gson gson = new Gson();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		String sql = "";
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		if(login){
			try {
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHIP_TO_LEVEL_ROCKWELL_ENABLE")).equalsIgnoreCase("Y")){
					if(getShipToAddress() != null && getShipToAddress() != "" && getShipToAddress().length()>0) {
						selectedShippingAddressId=CommonUtility.validateNumber(getShipToAddress());
					}
					else {
						HashMap<String,String> userDetails = new HashMap<String, String>();
						userDetails=UsersDAO.getUserPasswordAndUserId(userName,"Y");
						selectedShippingAddressId = CommonUtility.validateNumber(userDetails.get("defaultShippingAddressId"));
					}
					
					if(selectedShippingAddressId>0) {
						//shippingCustomFieldData = (LinkedHashMap<String, String>) UsersDAO.getBCAddressBookCustomFields(selectedShippingAddressId);
						shippingCustomFieldData = CommonUtility.customServiceUtility().getBCAddressBookCustomFields(selectedShippingAddressId);
						 if(shippingCustomFieldData!=null && shippingCustomFieldData.size()>0){
							 for(Entry<String, String> entry : shippingCustomFieldData.entrySet()){
								if(entry!=null && entry.getValue()!=null && entry.toString().contains("SHIP_TO_LEVEL_ROCKWELL=Y")){
									rockwellLocation = 1;
									break;
								}
								else{
									rockwellLocation = 0;
								}
							 }
							 session.setAttribute("shippingCustomFieldData",shippingCustomFieldData);
						 }
					}
					
					LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = null;
					ArrayList<Integer> cartItemList = new ArrayList<Integer>();
					ArrayList<Integer> rockwellItemList = new ArrayList<Integer>();
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
								rockwellItem = 1;
								rockwellItemList.add(entry.getKey());
							}
							else {
								rockwellItem = 0;
							}
						}
					}
					
					if(rockwellLocation == 0 && rockwellItem == 1) {
						System.out.println("Selected location is non rockwell location and rockwell items found");
					}
					contentObject.put("rockwellLocation", rockwellLocation);
					contentObject.put("rockwellItem", rockwellItem);
					contentObject.put("rockwellItemList", rockwellItemList);
				}
				//contentObject.put("rockwellData", gson.toJson(contentObject));
				renderContent = gson.toJson(contentObject);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally {
				pstmt.close();
				rs.close();
				conn.close();
			}
			return "success";
		}
		else {
			return "SESSIONEXPIRED";
		}
	}
	
	public String deleteRockwellItems() throws SQLException {
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String rockWellItemList = (String) request.getParameter("rockwellItemList");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String sql = "";
		PreparedStatement pstmt = null;
		Connection conn = null;
		if(userId>1){
			try{				
				conn = ConnectionManager.getDBConnection();
				sql = "DELETE FROM CART WHERE USER_ID=? AND ITEM_ID IN ("+rockWellItemList+")";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				int count = pstmt.executeUpdate();
				
				if(count>0){
					result = "1|Rockwell items deleted successfully";
				}
				else{
					result = "0|Delete failed";
				}
				
				renderContent = result;
			}
			catch(Exception e){
				e.printStackTrace();
				return "SESSIONEXPIRED";
			}
			finally {
				pstmt.close();
				conn.close();
			}
			return "success";
		}
		else {
			return "SESSIONEXPIRED";
		}
	}
	public String getManufacturerRistrictionlist() throws SQLException {
		request =ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		renderContent = "";
		String jsonResponse = "";		
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
	    LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	    List<String> stateCodes = new ArrayList<String>();
	    String manfId;	
	    String temp = null;
	    try {
	    	conn = ConnectionManager.getDBConnection();
	    	sql = PropertyAction.SqlContainer.get("getRistrictionStateNames");           
        	System.out.println(sql);
        	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
        	pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
            temp = rs.getString("MANUFACTURER_ID") +  ':' + rs.getString("STATE_CODE");
            stateCodes.add(temp);
            }
            Gson gson = new Gson();
            locationResponseList= gson.toJson(stateCodes);
            System.out.println(locationResponseList);
            
    		renderContent = locationResponseList;    		
    		return "success";
    	}
	    catch(Exception e){
			e.printStackTrace();
			return "SESSIONEXPIRED";
		}
		finally {
			conn.close();
		}
	 

	}
	   public String getStatenameFromShipToID() { 
		   	request =ServletActionContext.getRequest();
		   	HttpSession session = request.getSession();
		    int	defaultShipTo = CommonUtility.validateNumber(request.getParameter("defaultShipToId"));
			Connection  conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String stateName = null;
			 
			try{
				conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("getStateNameBySHIPToID");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, defaultShipTo);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					stateName = rs.getString("STATE");
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
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			renderContent = stateName;    		
    		return "success";			
		}
	   
	   
	   
	   public static ArrayList<EventModel> getEventslist(String startDate, String endDate) throws SQLException {				
			Connection  conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;	
			
			ArrayList<EventModel> events = new ArrayList<EventModel>();
			try{
				conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("getEventListByDate");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
				rs = pstmt.executeQuery();
					while (rs.next()) {
					EventModel event = new EventModel();
					event.setTitle(rs.getString("EVENT_TITLE"));
					event.setDateV2( rs.getString("START_DATE"));
					event.setEndV2(rs.getString("END_DATE"));
					event.setEventBodyKeyword(rs.getString("EVENT_BODY_KEYWORD"));
					event.setEventCategory(rs.getString("EVENT_CATEGORY"));
					event.setLocation(rs.getString("LOCATION"));
					event.setContact(rs.getString("CONTACT"));
					event.setAddress(rs.getString("ADDRESS"));
					event.setTotalSeats(rs.getInt("TOTAL_SEATS"));
					event.setBookedSeats(rs.getInt("BOOKED_SEATS"));
					events.add(event);
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
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}				
    		return events;			
	    	}
	

	public String shareGroupToPublic() {
		renderContent = "";
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String groupId = request.getParameter("groupId");
		String GroupType = request.getParameter("GroupType");
		String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		String key = request.getParameter("key");
		int count = 0;
		try {
			count = ProductsDAO.updateGroupToPublic(CommonUtility.validateNumber(buyingCompanyId), GroupType,CommonUtility.validateNumber(groupId),key);
			if (count > 0) {
				result = "0|success";
			}else{
				result = "1|Failed";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderContent = result;
		return "success";
	}
	public String getPayflowFormToken() {
		request =ServletActionContext.getRequest();
		renderContent = "";
		try{
			Gson gson = new Gson();
			SalesModel quoteResponse = null;
			SalesModel salesInputParameter = new SalesModel();
			salesInputParameter.setTotal(Double.parseDouble(CommonUtility.validateString(request.getParameter("orderTotal"))));
			salesInputParameter.setErpOverrideFlag(CommonUtility.validateString(request.getParameter("erpOverrideFlag")));
			SalesOrderManagement salesObj = new SalesOrderManagementImpl();
			quoteResponse = salesObj.createPaypalPaymentV2(salesInputParameter);
			renderContent = gson.toJson(quoteResponse);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	public String getSalesByDates() {
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String customerId= (String)session.getAttribute("customerId");
		String fromDate=CommonUtility.validateString(request.getParameter("fromDate"));
		String endDate=CommonUtility.validateString(request.getParameter("endDate"));
		String flag="";
		ArrayList<ProductsModel> categoryList = new ArrayList<ProductsModel>();
		
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductsModel data =null;
		Gson gson = new Gson(); 
		
        try
        {
        	conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("getCustomerSalesByDate");
        	pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromDate);
			pstmt.setString(2, endDate);
        	if(CommonUtility.validateString(flag).equalsIgnoreCase("user")){
        		sql = PropertyAction.SqlContainer.get("getUserSalesByDate");
        		pstmt.setInt(3, userId);	
        	}else {
        		pstmt.setString(3, customerId);	
        	}
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				data = new ProductsModel();
				data.setDate(rs.getString("SALESDATE"));
				data.setExtendedPrice(rs.getInt("EXTENDED_PRICE"));
				categoryList.add(data);
			}
			renderContent = gson.toJson(categoryList);
        }catch (Exception e) {
			e.printStackTrace();
        }finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return "success";
	}
	
	public String captchaValidation(){
		
		request =ServletActionContext.getRequest();		
		HttpSession session = request.getSession();
		String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String userCaptchaResponse = request.getParameter("jcaptcha");
		String captchaRequired = request.getParameter("captchaRequired");		
		boolean captchaPassed = false;
		try
        {
		if(userId>1){
			captchaPassed = true;
		}else{
			if(CommonUtility.validateString(captchaRequired).equalsIgnoreCase("N")){
				captchaPassed = true;
			}else{
				String captcha = (String) session.getAttribute("captcha");
				if (captcha != null && userCaptchaResponse != null) {

					if (captcha.equals(userCaptchaResponse)) {
						captchaPassed = true;
						result = "1|Captcha validation Successfull";
					} 
					else {
						result = "0|Invalid Captcha Entered ";
						
					}
	}	
	}
			renderContent = result;			
		
	}
        }catch (Exception e) {
			e.printStackTrace();
        }
		return "success";
	}
	public String getIframeCustomerPortalToken() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String iframeCustomPortalToken = "";
		try {
			UsersModel tokenInputParameters = new UsersModel();
			UsersModel outputRespose = new UsersModel();
			tokenInputParameters.setUserName(CommonUtility.validateString(request.getParameter("userName")));
			tokenInputParameters.setPassword(CommonUtility.validateString(request.getParameter("password")));
			tokenInputParameters.setErpOverrideFlag(CommonUtility.validateString(request.getParameter("erpOverrideFlag")));
			UserManagement usersObj = new UserManagementImpl();
			outputRespose = usersObj.getIframeAccessToken(tokenInputParameters);
			iframeCustomPortalToken = outputRespose.getUserToken();
			if (iframeCustomPortalToken != null) {
				session.setAttribute("iframeCustomToken", iframeCustomPortalToken);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iframeCustomPortalToken;
	}
	public String checkForEmailAddressInDb() {
	request =ServletActionContext.getRequest();
	renderContent = "";
	try{
		int userID = 0;
		HashMap<String,String> userDetails = new HashMap<String,String>();
		userDetails = UsersDAO.getUserPasswordAndUserId(CommonUtility.validateString(request.getParameter("emailId")),"Y");
		if(CommonUtility.validateString(userDetails.get("userId")).length() >0) {
			userID= CommonUtility.validateNumber(userDetails.get("userId"));
		}
		if(userID >1 && !CommonUtility.validateString(userDetails.get("password")).equals(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")))) {
			if(CommonUtility.validateString(userDetails.get("billingEntityId")).indexOf("delete")>-1 ) {
				renderContent = "userDeletedInPOS";
			}else {
			renderContent = "userExistInCIMM";
			}
		}else{
			if(userID >1  && CommonUtility.validateString(userDetails.get("password")).equals(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")))) {
				UsersDAO.deleteExistingGuestUser(CommonUtility.validateString(request.getParameter("emailId")));
			}
			String responseOutput = "";
			UserManagement usersObj = new UserManagementImpl();
			UsersModel inputDetails = new UsersModel();
			inputDetails.setUserName(CommonUtility.validateString(request.getParameter("emailId")));
			inputDetails.setErpOverrideFlag(CommonUtility.validateString(request.getParameter("erpOverrideFlag")));
			inputDetails.setFirstName(CommonUtility.validateString(request.getParameter("firstName")));
			inputDetails.setLastName(CommonUtility.validateString(request.getParameter("lastName")));
			responseOutput = usersObj.checkforUserNameInERP(inputDetails);
			renderContent = responseOutput;
		}
	}catch (Exception e) {
		logger.error(e.getMessage());
	}
	return "success";
	}
	
	public String validateEmail()
	{
		request = ServletActionContext.getRequest();
		renderContent = "";
		String inputUserName = CommonUtility.validateString(request.getParameter("userName"));
		String authCode = CommonUtility.validateString(request.getParameter("authCode"));
		String erpOverrideFlag = CommonUtility.validateString(request.getParameter("erpOverrideFlag"));
		System.out.println("@validateEmail-inputUserName: "+inputUserName+":authCode:"+authCode);
		try {
			UserManagement usersObj = new UserManagementImpl();
			renderContent = usersObj.validateEmailfromESB(inputUserName, authCode, erpOverrideFlag);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	public String createEmptyProductGroups() {
		Connection  conn = null;
		try {
			conn = ConnectionManager.getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		renderContent = "";
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		int count = 0;
		int savedListId = CommonDBQuery.getSequenceId("SAVED_ITEM_LIST_SEQ");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String pGroupName =  CommonUtility.validateString(request.getParameter("pGroupName"));
		try {
			count = ProductsDAO.createGroupName(conn, savedListId, userId, "P", pGroupName);
			if (count > 0) {
				result = "0|success|"+pGroupName+"|"+savedListId;
			}else{
				result = "1|Failed";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
        {
        	ConnectionManager.closeDBConnection(conn);
        }
		renderContent = result;
		return "success";
	}
	
	public String validateCoupons() {
		renderContent = "";
		try {
				request =ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				LinkedHashMap<String, Object> cartDeatils = new LinkedHashMap<String, Object>();
				cartDeatils = ProductsDAO.getShoppingCartDao(session, cartDeatils);
				SalesModel couponsInput = new SalesModel(); 
				couponsInput.setTax(CommonUtility.validateDoubleNumber(CommonUtility.validateString(request.getParameter("orderTax"))));
				couponsInput.setFreight(CommonUtility.validateDoubleNumber(CommonUtility.validateString(request.getParameter("orderFreight"))));
				couponsInput.setDiscountCouponCode(CommonUtility.validateString(request.getParameter("couponsList")));
				couponsInput.setCartData((ArrayList<ProductsModel>) cartDeatils.get("productListData"));
				couponsInput.setSubtotal((double) cartDeatils.get("cartTotal"));
				couponsInput.setSession(session);
				if(CommonUtility.customServiceUtility() != null) {
					renderContent = CommonUtility.customServiceUtility().validateCoupons(couponsInput);
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
}
