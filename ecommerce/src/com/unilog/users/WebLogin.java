package com.unilog.users;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.UserManagement;
import com.erp.service.impl.UserManagementImpl;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsDAO;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.utility.CommonUtility;

public class WebLogin {

	private static LinkedHashMap<String,String> webUserProperties=null;
	private static Date eclipseSessionTime = null;
	private static ArrayList<Integer> userTopTab = null;
	private static ArrayList<String> brandList = null;
	static
	{
		loadWebUser();
	}
	
	public static void loadWebUser()
	{
		try
		{
			String generalCatalog = CommonDBQuery.getSystemParamtersList().get("GENERALCATALOGID");
			HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId("web","Y");
			UserManagement usersObj = new UserManagementImpl();   
			UsersModel userInfo = new UsersModel();
			webUserProperties = new LinkedHashMap<String, String>();
			webUserProperties.put("validUser", userDetails.get("userId"));
			//webUserProperties.put("isPunchoutUser", userDetails.get("isPunchoutUser"));			
			webUserProperties.put("buyingCompanyLogo", userDetails.get("logo"));
			webUserProperties.put("parentUserId", userDetails.get("parentUserId"));
			webUserProperties.put("canSubmitPO", userDetails.get("submitPO"));
			webUserProperties.put("canSubmitRFQ", userDetails.get("submitRFQ"));
			webUserProperties.put("canSubmitNPR", userDetails.get("submitNPR"));
			webUserProperties.put("userSubsetId", CommonDBQuery.getSystemParamtersList().get("USERSUBSETID"));//Remove userDetails.get("userSubsetId") CommonDBQuery.getSystemParamtersList().get("USERSUBSETID") 
			webUserProperties.put("contactId", userDetails.get("contactId"));
			webUserProperties.put("userToken", userDetails.get("contactId"));
			webUserProperties.put("firstLogin", userDetails.get("firstLogin"));
			webUserProperties.put("displayCustPartNum", userDetails.get("displayCustPartNum"));
			webUserProperties.put("allowLiveChat", userDetails.get("allowLiveChat"));
			webUserProperties.put("buyingCompanyId", userDetails.get("buyingCompanyId"));
			webUserProperties.put("defaultBillToId", userDetails.get("defaultBillingAddressId"));
			webUserProperties.put("defaultShipToId", userDetails.get("defaultShippingAddressId"));
			webUserProperties.put("billingEntityId", userDetails.get("billingEntityId"));
			webUserProperties.put("existingCustomer", userDetails.get("existingCustomer"));
			webUserProperties.put("approvalUserId", userDetails.get("approvalUserId"));
			webUserProperties.put("customerBuyingCompanyId","0");
			webUserProperties.put("entityId", userDetails.get("billingEntityId"));
			webUserProperties.put("securedPassword", userDetails.get("password"));
			
			if(userDetails.get("wareHouseCode") != null) {
				webUserProperties.put("wareHouseCode", userDetails.get("wareHouseCode"));
			}else {
				webUserProperties.put("wareHouseCode", CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR"));
			}
			
			webUserProperties.put("webUserMsg", "Getting web user details..");
			
			if(userDetails.get("allowGeneralCatalog")!=null && userDetails.get("allowGeneralCatalog").trim().equalsIgnoreCase("Y")){
				webUserProperties.put("generalCatalog", generalCatalog);
			}else{
				webUserProperties.put("generalCatalog", "0");
			}
			userInfo.setUserName("web");
			userInfo.setBillEntityId(CommonUtility.validateString(userDetails.get("billingEntityId")));
			usersObj.getErpData(userInfo, webUserProperties);
			eclipseSessionTime = new java.util.Date();
			String tempSubset = userDetails.get("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
		/*	userTopTab = new ArrayList<Integer>(); 
			userTopTab = ProductsDAO.getTopTab(subsetId, 0);*/
			brandList = ProductsDAO.getbrandListIndex(subsetId,0);
			     	
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static void webUserSession(String previousSessionId)
	{	
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String erpLogin =  CommonDBQuery.getSystemParamtersList().get("ERPLOGIN");
			UserManagement usersObj = new UserManagementImpl();
			//session.setAttribute(Global.USERID_KEY,"");
			//session.setAttribute(Global.USERID_KEY, webUserProperties.get("validUser"));
			session.setAttribute(Global.USERID_KEY,"1"); //hardcoded as 1 in order to support for multisite web users
			session.setAttribute(Global.USERNAME_KEY,"web") ;
			session.setAttribute("securedPassword",webUserProperties.get("securedPassword"));
			session.setAttribute("userLogin",false);
			//session.setAttribute("validUser",webUserProperties.get("validUser"));
			session.setAttribute("validUser","1"); //hardcoded as 1 in order to support for multisite web users
			//session.setAttribute("isPunchoutUser",webUserProperties.get("isPunchoutUser")); //-- Need to remove as self service punchout flow is introduced  
			session.setAttribute("buyingCompanyLogo",webUserProperties.get("buyingCompanyLogo"));
			session.setAttribute("parentUserId",webUserProperties.get("parentUserId"));
			session.setAttribute("canSubmitPO",webUserProperties.get("canSubmitPO"));
			session.setAttribute("canSubmitRFQ",webUserProperties.get("canSubmitRFQ"));
			session.setAttribute("canSubmitNPR",webUserProperties.get("canSubmitNPR"));
			session.setAttribute("userSubsetId",webUserProperties.get("userSubsetId"));
			session.setAttribute("contactId",webUserProperties.get("contactId"));
			session.setAttribute("firstLogin",webUserProperties.get("firstLogin"));
			session.setAttribute("displayCustPartNum",webUserProperties.get("displayCustPartNum"));
			session.setAttribute("allowLiveChat",webUserProperties.get("allowLiveChat"));
			session.setAttribute("buyingCompanyId",webUserProperties.get("buyingCompanyId"));
			session.setAttribute("defaultBillToId",webUserProperties.get("defaultBillToId"));
			session.setAttribute("defaultShipToId",webUserProperties.get("defaultShipToId"));
			session.setAttribute("billingEntityId",webUserProperties.get("billingEntityId"));
			session.setAttribute("existingCustomer",webUserProperties.get("existingCustomer"));
			session.setAttribute("approvalUserId",webUserProperties.get("approvalUserId"));
			session.setAttribute("customerBuyingCompanyId",webUserProperties.get("customerBuyingCompanyId"));
			session.setAttribute("entityId",webUserProperties.get("entityId"));
			session.setAttribute("customerId", webUserProperties.get("contactId"));
			session.setAttribute("webUserMsg",webUserProperties.get("webUserMsg"));
			session.setAttribute("generalCatalog", webUserProperties.get("generalCatalog"));
			if(session.getAttribute("pricePrecision")==null){
				session.setAttribute("pricePrecision", CommonUtility.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision")));	
			}
			session.setAttribute("combinCart", CommonDBQuery.getSystemParamtersList().get("COMBINED_CART"));
			session.setAttribute("qtyIntervalFromErp", CommonDBQuery.getSystemParamtersList().get("QTY_INTERVALSFROM_ERP"));
			session.setAttribute("newsSection", CommonDBQuery.getSystemParamtersList().get("NEWS_SECTION"));
			session.setAttribute("eventSection", CommonDBQuery.getSystemParamtersList().get("EVENT_SECTION"));
			session.setAttribute("disappearCartPop", CommonDBQuery.getSystemParamtersList().get("DISAPPEAR_ADDTOCART_POPUP"));
			session.setAttribute("leadTime", CommonDBQuery.getSystemParamtersList().get("LEAD_TIME"));
			session.setAttribute("isInternationalUser", CommonDBQuery.getSystemParamtersList().get("ENABLE_INTERNATIONAL_USER"));
			session.setAttribute("wareHouseCode",webUserProperties.get("wareHouseCode"));
			if(erpLogin!=null && erpLogin.trim().equalsIgnoreCase("Y")){
				session.setAttribute("userToken",webUserProperties.get("userToken"));
				if(eclipseSessionTime!=null){
					session.setAttribute("eclipseSessionTime", eclipseSessionTime);
				}else{
					session.setAttribute("eclipseSessionTime", new java.util.Date());
				}
			}else{
				session.setAttribute("userToken", webUserProperties.get("contactId"));
			}
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_WAREHOUSE_DETAILS_FOR_WEB")).equalsIgnoreCase("Y")){
				ArrayList<String> warehouseIndexList = new ArrayList<String>();
				warehouseIndexList = ProductsDAO.getWarehouseIndex();
				session.setAttribute("warehouseIndexList",warehouseIndexList);
			}
			
			String erp = "defaults";
			if(CommonDBQuery.getSystemParamtersList().get("ERP")!=null && CommonDBQuery.getSystemParamtersList().get("ERP").trim().length()>0){
				erp = CommonDBQuery.getSystemParamtersList().get("ERP").trim();
			}
			session.setAttribute("erpType", erp);
			
			String paymentGateway = null;
			if(CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY")!=null && CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY").trim().length()>0){
				paymentGateway = CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY").trim();
			}
			session.setAttribute("PAYMENT_GATEWAY", paymentGateway);
			
			session.setAttribute("entityId",webUserProperties.get("entityId"));
			session.setAttribute("shipBranchId",webUserProperties.get("shipBranchId"));
			session.setAttribute("shipBranchName",webUserProperties.get("shipBranchName"));
			session.setAttribute("userTabList", getUserTopTab());
			if(CommonUtility.validateNumber(webUserProperties.get("defaultShipToId")) > 0){
				ArrayList<AddressModel> shipAddressList = UsersDAO.getShipToIdShipAddresses(CommonUtility.validateNumber(webUserProperties.get("defaultShipToId")));
				if(shipAddressList != null && shipAddressList.size() > 0 && CommonUtility.validateString(shipAddressList.get(0).getShipToId()).length() > 0) {
					session.setAttribute("selectedshipToIdSx", CommonUtility.validateString(shipAddressList.get(0).getShipToId()));
				}
			}
			//----Loading Time
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ANONYMOUS_CHECKOUT_ENABLED")).equalsIgnoreCase("Y")){
				if(session.getAttribute("customerShipViaListJson")==null || session.getAttribute("customerShipViaList")==null){
					UsersAction usersAction = new UsersAction();
					usersAction.loadShipDetailsForUser();
				}
			}
			if(!CommonUtility.validateString(previousSessionId).equals(session.getId())){
				ProductsDAO.updateCartSessionId(previousSessionId, session.getId());
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		//----Loading Time

	}

	public static void setWebUserProperties(LinkedHashMap<String,String> webUserProperties) {
		WebLogin.webUserProperties = webUserProperties;
	}

	public static LinkedHashMap<String,String> getWebUserProperties() {
		return webUserProperties;
	}

	public static void setUserTopTab(ArrayList<Integer> userTopTab) {
		WebLogin.userTopTab = userTopTab;
	}

	public static ArrayList<Integer> getUserTopTab() {
		return userTopTab;
	}

	public static void setEclipseSessionTime(Date eclipseSessionTime) {
		WebLogin.eclipseSessionTime = eclipseSessionTime;
	}

	public static Date getEclipseSessionTime() {
		return eclipseSessionTime;
	}

	public static ArrayList<String> getBrandList() {
		return brandList;
	}

	public static void setBrandList(ArrayList<String> brandList) {
		WebLogin.brandList = brandList;
	}
}