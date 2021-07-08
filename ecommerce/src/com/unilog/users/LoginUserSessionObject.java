package com.unilog.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.erp.service.UserManagement;
import com.erp.service.impl.UserManagementImpl;
import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsDAO;
import com.unilog.utility.CommonUtility;

public class LoginUserSessionObject {

	public static void setUserSessionObject(HttpSession session, HashMap<String, String> userDetails) {
		long startTimer = CommonUtility.startTimeDispaly();
		try{
			UserManagement userObj = new UserManagementImpl();
			UsersModel catalog = new UsersModel();
			Gson gson = new Gson();
			String erp = "defaults";
			String paymentGateway = null;
			String generalCatalog = CommonDBQuery.getSystemParamtersList().get("GENERALCATALOGID");
			String erpLogin =  CommonDBQuery.getSystemParamtersList().get("ERPLOGIN");
			session.setAttribute("combinCart", CommonDBQuery.getSystemParamtersList().get("COMBINED_CART"));
			session.setAttribute("qtyIntervalFromErp", CommonDBQuery.getSystemParamtersList().get("QTY_INTERVALSFROM_ERP"));
			session.setAttribute("newsSection", CommonDBQuery.getSystemParamtersList().get("NEWS_SECTION"));
			session.setAttribute("eventSection", CommonDBQuery.getSystemParamtersList().get("EVENT_SECTION"));
			session.setAttribute("disappearCartPop", CommonDBQuery.getSystemParamtersList().get("DISAPPEAR_ADDTOCART_POPUP"));
			session.setAttribute("leadTime", CommonDBQuery.getSystemParamtersList().get("LEAD_TIME"));
			//session.setAttribute("isInternationalUser", CommonDBQuery.getSystemParamtersList().get("ENABLE_INTERNATIONAL_USER"));
			if(userDetails.get("customerCountry")!=null && (userDetails.get("customerCountry").equalsIgnoreCase("US") || userDetails.get("customerCountry").equalsIgnoreCase("USA"))){
				session.setAttribute("isInternationalUser", "N");
			}else{
				session.setAttribute("isInternationalUser", "Y");
			}
			
			session.setAttribute(Global.USERID_KEY, userDetails.get("userId"));
			if(session.getAttribute("pricePrecision")==null){
				session.setAttribute("pricePrecision", CommonUtility.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision")));	
			}
			
			session.setAttribute(Global.USERNAME_KEY,userDetails.get("userName")) ;
			session.setAttribute("validUser", userDetails.get("userId"));
			//session.setAttribute("isPunchoutUser", userDetails.get("isPunchoutUser"));	//-- Need to remove as self service punchout flow is introduced  		
			session.setAttribute("buyingCompanyLogo", userDetails.get("logo"));
			session.setAttribute("parentUserId", userDetails.get("parentUserId"));
			session.setAttribute("canSubmitPO", userDetails.get("submitPO"));
			session.setAttribute("canSubmitRFQ", userDetails.get("submitRFQ"));
			session.setAttribute("canSubmitNPR", userDetails.get("submitNPR"));
			session.setAttribute("canSubmitWithCC", userDetails.get("submitWithCC"));
			session.setAttribute("acceptOrderByPO", userDetails.get("acceptOrderByPO"));
			session.setAttribute("userLevelAcceptOrderByPO", userDetails.get("userLevelAcceptOrderByPO"));
			session.setAttribute("contactId", userDetails.get("contactId"));
			session.setAttribute("firstLogin", userDetails.get("firstLogin"));
			session.setAttribute("displayCustPartNum", userDetails.get("displayCustPartNum"));
			session.setAttribute("allowLiveChat", userDetails.get("allowLiveChat"));
			session.setAttribute("buyingCompanyId", userDetails.get("buyingCompanyId"));
			session.setAttribute("defaultBillingAddressId", userDetails.get("defaultBillingAddressId"));
			session.setAttribute("defaultShippingAddressId", userDetails.get("defaultShippingAddressId"));
			session.setAttribute("defaultBillToId", userDetails.get("defaultBillingAddressId"));
			session.setAttribute("defaultShipToId", userDetails.get("defaultShippingAddressId"));
			session.setAttribute("billingEntityId", userDetails.get("billingEntityId"));
			session.setAttribute("userBuyingCompanyId", userDetails.get("buyingCompanyId"));
			session.setAttribute("userLevelBuyingCompanyId", userDetails.get("userLevelBuyingCompanyId"));
			session.setAttribute("existingCustomer", userDetails.get("existingCustomer"));
			session.setAttribute("approvalUserId", userDetails.get("approvalUserId"));
			session.setAttribute("customerId", userDetails.get("contactId"));
			session.setAttribute("loginCustomerName", userDetails.get("loginCustomerName"));
			session.setAttribute("userFirstName", userDetails.get("firstName"));
			session.setAttribute("userLastName", userDetails.get("lastName"));
			session.setAttribute("userEmailAddress", userDetails.get("userEmailAddress"));
			session.setAttribute("userOfficePhone", userDetails.get("userOfficePhone"));
			session.setAttribute("userCellPhone", userDetails.get("userCellPhone"));
			session.setAttribute("userJobTitle", userDetails.get("userJobTitle"));
			session.setAttribute("userSalesRepContactName", userDetails.get("userSalesRepContactName"));
			session.setAttribute("userProfileImage", userDetails.get("userProfileImage"));
			session.setAttribute("changePasswordOnLogin", CommonUtility.validateString(userDetails.get("changePasswordOnLogin")));
			session.setAttribute("securedPassword", userDetails.get("password"));
			session.setAttribute("systemCustomerId", CommonUtility.validateString(userDetails.get("systemCustomerId")));
			session.removeAttribute("distributionCenter");
			
			if(CommonUtility.validateString(CommonDBQuery.webSiteName).length()>0){
				session.setAttribute("websiteName", CommonDBQuery.webSiteName.toUpperCase());
			}

			if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
				catalog.setUserToken((String)session.getAttribute("userToken"));
				catalog.setEntityId(CommonUtility.validateString(userDetails.get("billingEntityId")));
			}else{
				catalog.setUserToken(userDetails.get("contactId"));
			}
			
			//<------------------------->
			session.setAttribute("userSubsetId", userDetails.get("userSubsetId"));
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_ERP_CATALOG")).equalsIgnoreCase("Y")){
				catalog.setBuyingCompanyId(CommonUtility.validateNumber(userDetails.get("buyingCompanyId")));
				catalog = userObj.getCatalogfromERP(catalog);
					//&& session.getAttribute("userSubsetName")!=null && session.getAttribute("userSubsetName").toString().trim().equalsIgnoreCase(catalog.getCatalogName().trim())
					if(catalog!=null && catalog.getCatalogName()!=null){
						 if(userDetails.get("userSubsetId")!=null && String.valueOf(catalog.getSubsetId())!=null && !String.valueOf(catalog.getSubsetId()).trim().equalsIgnoreCase(userDetails.get("userSubsetId").trim())){
							 UsersDAO.updateSubsetIdFromERP(CommonUtility.validateNumber(userDetails.get("buyingCompanyId")), catalog.getSubsetId());
						 }
						 session.setAttribute("userSubsetId", String.valueOf(catalog.getSubsetId()));
			        }
			}
			//<------------------------->	
			
			/*if(CommonUtility.customServiceUtility() != null) {
				CommonUtility.customServiceUtility().warehouseLevelCatalog(catalog,userDetails,session);//Electrozad Custom Service
			}*/
			session.setAttribute("customerCountry", userDetails.get("customerCountry"));
			session.setAttribute("termsType", userDetails.get("termsType"));
			session.setAttribute("termsTypeDesc", userDetails.get("termsTypeDesc"));
			session.setAttribute("customerShipVia", userDetails.get("customerShipVia"));
			session.setAttribute("actualCustomerShipVia", userDetails.get("customerShipVia"));
			session.setAttribute("customerShipViaDesc", userDetails.get("customerShipViaDesc"));
			session.setAttribute("wareHouseCodeID", Integer.toString(UsersDAO.getCustomerWareHouseID(userDetails.get("wareHouseCode"))));
			session.setAttribute("userFirstName", userDetails.get("firstName"));
			session.setAttribute("userLastName", userDetails.get("lastName"));
			session.setAttribute("userLogin",true);
			session.setAttribute("isGeneralUser",userDetails.get("isGeneralUser"));
			session.setAttribute("isAuthTechnician",userDetails.get("isAuthTechnician"));
			session.setAttribute("isTishmanUser",userDetails.get("isTishmanUser")); 
			session.setAttribute("isAdmin", userDetails.get("isAdmin"));
			session.setAttribute("isAuthPurchaseAgent",userDetails.get("isAuthPurAgent"));
			session.setAttribute("isRetailUser", userDetails.get("isRetailUser"));
			session.setAttribute("newsLetterSub", userDetails.get("newsLetterSub"));
			session.setAttribute("isSpecialUser", userDetails.get("isSpecialUser"));
			session.setAttribute("isSalesAdmin", userDetails.get("isSalesAdmin"));
			session.setAttribute("homeBranchId", userDetails.get("wareHouseCode"));
			UsersDAO.salesUserDetailsToSession(session, userDetails);
			
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SHIP_VIA_FROM_ERP")).equalsIgnoreCase("Y")){
				HashMap<String,UsersModel> userShipListDetails = UsersDAO.getUserShipVia();
				if(userShipListDetails!=null && userShipListDetails.size()>0 && userDetails.get("customerShipVia")!=null){
					UsersModel shiplistModel = new UsersModel();
					shiplistModel = userShipListDetails.get(userDetails.get("customerShipVia").trim().toUpperCase());
					session.setAttribute("actualCustomerShipViaFromERP", userShipListDetails);
					session.setAttribute("customerShipVia", shiplistModel.getShipViaMethod());
					userDetails.put("customerShipVia",shiplistModel.getShipViaMethod());
				}
			}

			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("LOAD_SHIP_VIA")).equalsIgnoreCase("Y")){
				System.out.println("Inside shipvia");
				catalog.setShipVia(userDetails.get("customerShipVia"));
				catalog.setSession(session);
				ArrayList<ShipVia> shipViaArryList = CommonDBQuery.getSiteShipViaList(); //userObj.getUserShipViaList(catalog);
				String shipViaJson = gson.toJson(shipViaArryList);
				session.setAttribute("customerShipViaListJson",shipViaJson);
				session.setAttribute("customerShipViaList",shipViaArryList);
				System.out.println(shipViaJson);
			}
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID")).length()>0){
				String[] brandIDList = null;
				if(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID").contains(",")){
					brandIDList = CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID").split(",");
				}else{
					brandIDList = new String[]{CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID")};
				}
				boolean isExcludeBranch = false;
				if(brandIDList!=null){
					for(String brandId:brandIDList){
						isExcludeBranch = ProductsDAO.getCartBrandExcludeStatus(CommonUtility.validateNumber(userDetails.get("userId")),null,CommonUtility.validateNumber(brandId),"cart");
						if(isExcludeBranch){
							break;
						}
					}
					if(isExcludeBranch){
						session.setAttribute("isWillCallExclude", "Y");
					}else{
						session.setAttribute("isWillCallExclude", "N");
					}
				}
			}
			
			if(session.getAttribute("customerShipViaList")!=null){
				ArrayList<ShipVia> customerShipViaListArray = (ArrayList<ShipVia>)session.getAttribute("customerShipViaList");
				if(customerShipViaListArray!=null && customerShipViaListArray.size()>0){
					LinkedHashMap<String, String> shipViaMap = new LinkedHashMap<String, String>();
					for(ShipVia shipVia:customerShipViaListArray){
						if(CommonUtility.validateString(shipVia.getDescription()).length()>0){
							shipViaMap.put(CommonUtility.validateString(shipVia.getShipViaID()).toUpperCase(), CommonUtility.validateString(shipVia.getDescription()));
						}else{
							shipViaMap.put(CommonUtility.validateString(shipVia.getShipViaID()).toUpperCase(), CommonUtility.validateString(shipVia.getShipViaID()));
						}
					}
					session.setAttribute("shipViaMap",shipViaMap);
					if(userDetails.get("customerShipVia")!=null && userDetails.get("customerShipVia").trim().length()>0){
						session.setAttribute("customerDefaultShipVia",userDetails.get("customerShipVia"));
						session.setAttribute("selectedShipVia", userDetails.get("customerShipVia"));
					}else{
						if(CommonDBQuery.getSystemParamtersList().get("DEFAULT_SHIP_VIA")!=null && CommonDBQuery.getSystemParamtersList().get("DEFAULT_SHIP_VIA").trim().length()>0){
							session.setAttribute("customerDefaultShipVia",CommonDBQuery.getSystemParamtersList().get("DEFAULT_SHIP_VIA").trim());
							session.setAttribute("selectedShipVia", CommonDBQuery.getSystemParamtersList().get("DEFAULT_SHIP_VIA").trim());
						}
					}
					
				}
			}
			
			if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y") && CommonUtility.customServiceUtility() != null){
				System.out.println("Erp Token Assigned");
				CommonUtility.customServiceUtility().setUserToken(userDetails, session); 
			}else{
				session.setAttribute("userToken", userDetails.get("contactId"));
			}
			
			if(CommonUtility.validateString(userDetails.get("allowGeneralCatalog")).equalsIgnoreCase("Y")){
				session.setAttribute("generalCatalog", generalCatalog);
			}else{
				session.setAttribute("generalCatalog", "0");
			}
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).length()>0){
    			erp = CommonDBQuery.getSystemParamtersList().get("ERP").trim();
    		}
			session.setAttribute("erpType", erp);
			
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY")).length()>0){
				paymentGateway = CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY").trim();
			}
			session.setAttribute("PAYMENT_GATEWAY", paymentGateway);
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ASYNC_PROCESS")).equalsIgnoreCase("Y")) {
				new Thread(new AsyncUserSession(session, userDetails)).start();
			}else {
				setAsyncUserSessionObject(session, userDetails);
			}
			
			if(CommonUtility.customServiceUtility()!=null){
				Map<String, String> customerCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("customerCustomFieldValue");
				String SalesRepName = null;
				if((customerCustomFieldValue!=null)){
					 SalesRepName = CommonUtility.customServiceUtility(). getSalesRepEmailDetail(customerCustomFieldValue.get("SManID"));
				}
				if(SalesRepName!=null) {
					session.setAttribute("userSalesRepContactName", SalesRepName); 
				}else {
					session.setAttribute("userSalesRepContactName", userDetails.get("userSalesRepContactName"));
				}	
				
				CommonUtility.customServiceUtility().getClusterdata();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	}
	
	//Make sure addin db calls in below method as it will be called using async thread
	public static void setAsyncUserSessionObject (HttpSession session, HashMap<String, String> userDetails) {
		long startTimer = CommonUtility.startTimeDispaly();
		try {
			//-- Customer specific landing page
			String customerLandingPageId = CommonUtility.validateString(userDetails.get("customerLandingPageId"));
			if(CommonUtility.validateNumber(customerLandingPageId)>0) {
				session.setAttribute("customerLandingPageId", customerLandingPageId);
				if(CommonDBQuery.getCustomerDynamicPagesList()!=null && CommonUtility.validateString(CommonDBQuery.getCustomerDynamicPagesList().get(customerLandingPageId)).length()>0) {
					session.setAttribute("customerLandingPageUrl", CommonUtility.validateString(CommonDBQuery.getCustomerDynamicPagesList().get(customerLandingPageId)));
				}
			}
			//-- Customer specific landing page
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_WAREHOUSE_DETAILS")).equalsIgnoreCase("Y")){
				ArrayList<String> warehouseIndexList = new ArrayList<String>();
				warehouseIndexList = ProductsDAO.getWarehouseIndex();
				session.setAttribute("warehouseIndexList",warehouseIndexList);
			}

			if(CommonUtility.validateString((String)session.getAttribute("wareHouseCode")).length()<=0 || CommonUtility.validateString(userDetails.get("wareHouseCode")).length()>0){
				session.setAttribute("wareHouseCode", CommonUtility.validateString(userDetails.get("wareHouseCode")));
				if(CommonUtility.validateString(userDetails.get("wareHouseCode")).length()>0) {
					WarehouseModel warehouseDetails = UsersDAO.getWareHouseDetailsByCode(CommonUtility.validateString(userDetails.get("wareHouseCode")));
					if(warehouseDetails!=null) {
						session.setAttribute("wareHouseName", warehouseDetails.getWareHouseName());
					}
				}
			}else {
				session.setAttribute("wareHouseCode", CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR"));
			}
			
			
			UsersModel catalog = UsersDAO.getWarehouseEmail(userDetails.get("wareHouseCode"));
			session.setAttribute("wareHousePhone", catalog.getPhoneNo());
			if( CommonDBQuery.getSystemParamtersList().get("ENABLE_WAREHOUSE_SPECIFIC_ORDER_MAIL")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_WAREHOUSE_SPECIFIC_ORDER_MAIL").trim().equalsIgnoreCase("Y")){
				session.setAttribute("wareHouseEmailID", catalog.getEmailAddress());
			}
			
			/*if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISPLAY_CUSTOM_MESSAGE")).equalsIgnoreCase("Y")){
				String customerSpecificCustomField = CommonDBQuery.getSystemParamtersList().get("CUSTOMER_CUSTOM_MESSAGE_FIELD_NAME");
				String userSpecificCustomField = CommonDBQuery.getSystemParamtersList().get("USER_CUSTOM_MESSAGE_FIELD_NAME");
				String userId = userDetails.get("userId");
				int buyingCompanyId = CommonUtility.validateNumber(userDetails.get("buyingCompanyId"));
				String customFieldValue = "";
				if(userId!=null && userId.length() > 0 && userSpecificCustomField!=null && userSpecificCustomField.length() > 0){
					customFieldValue = UsersDAO.getUserCustomFieldValue(userId,userSpecificCustomField);
					if(customFieldValue!=null && customFieldValue.length() > 0){
						session.setAttribute("customFieldValue",customFieldValue);
					}
					else if(customerSpecificCustomField!=null && customerSpecificCustomField.length()>0 && buyingCompanyId > 0){
						customFieldValue = UsersDAO.getCustomerCustomTextFieldValue(buyingCompanyId,customerSpecificCustomField);
						session.setAttribute("customFieldValue",customFieldValue);
					}
				}
			}*/
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ALL_USER_CUSTOMER_CUSTOM_FIELDS")).equalsIgnoreCase("Y")){
				int userId = CommonUtility.validateNumber(userDetails.get("userId"));
				int buyingCompanyId = CommonUtility.validateNumber(userDetails.get("buyingCompanyId"));
				LinkedHashMap<String, String> userCustomFieldValue = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> customerCustomFieldValue = new LinkedHashMap<String, String>();
				if(userId > 0 ){
					userCustomFieldValue = UsersDAO.getAllUserCustomFieldValue(userId);
					if(userCustomFieldValue!=null){
						session.setAttribute("userCustomFieldValue",userCustomFieldValue);
						session.setAttribute("isCreditCardOnly", userCustomFieldValue.get("CREDITCARDONLY"));
						//session.setAttribute("isPickUp", userCustomFieldValue.get("ISPICKUP"));
						//session.setAttribute("hidePrice", userCustomFieldValue.get("HIDEPRICE"));
					}
				}
				if(buyingCompanyId > 0){
					customerCustomFieldValue = UsersDAO.getAllCustomerCustomFieldValue(buyingCompanyId);
					if(customerCustomFieldValue!=null && customerCustomFieldValue.size()>0){
						session.setAttribute("customerCustomFieldValue",customerCustomFieldValue);
						session.setAttribute("matrixPricing", customerCustomFieldValue.get("MARTIX_PRICING"));
                        ProductsDAO.myItemListForMatrixPricing(CommonUtility.validateNumber(userDetails.get("userSubsetId")), CommonUtility.validateNumber((String)session.getAttribute("generalCatalog")), CommonUtility.validateNumber(userDetails.get("buyingCompanyId")), userDetails.get("billingEntityId"));
					}
					
				}
			}
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SALES_REP_MODULE_CONFIGURED")).equalsIgnoreCase("Y")) {
				UsersDAO.setSalesRepDetails(session);
			}
			
			setUserPreferencesToSession(session,userDetails.get("userPreferences"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	}

	public static void setUserPreferencesToSession(HttpSession session,String userPreferences) {
		long startTimer = CommonUtility.startTimeDispaly();
		try {
			session.removeAttribute("timezone");
			if(CommonUtility.validateString(userPreferences).length()>0) {
				Object obj = new JSONParser().parse(userPreferences);
				JSONObject jsonObj = (JSONObject) obj;
				String timeZone = (String) jsonObj.get("timezone");
				session.setAttribute("timezone", timeZone);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	}
}