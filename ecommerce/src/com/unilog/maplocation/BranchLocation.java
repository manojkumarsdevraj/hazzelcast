package com.unilog.maplocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralNearestWareHouse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralNearestWareHouseResponse;
import com.erp.service.impl.UserManagementImpl;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.CustomFieldModel;
import com.unilog.defaults.Global;
import com.unilog.geolocator.GeoLocatorEnums;
import com.unilog.geolocator.GeoLocatorEnums.GeoLocatorProvider;
import com.unilog.geolocator.GeoLocatorRequest;
import com.unilog.geolocator.GeoLocatorResponse;
import com.unilog.geolocator.IGeoLocatorService;
import com.unilog.geolocator.service.provider.GeoLocatorServiceProvider;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.security.LoginAuthentication;
import com.unilog.users.UsersAction;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

public class BranchLocation extends ActionSupport implements ServletResponseAware {


	private static final long serialVersionUID = 1L;

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String locationResponseList;
	protected String target = ERROR;
	private String locationDataFrom;
	private String googleSpreadSheetUrl;
	ArrayList<LocationModel> locationResult = new ArrayList<LocationModel>();
	private String renderContent;
	private String service;
	private String nearestlocationResponseList;
	private String responseType;
	private String customParam;

	public String getRenderContent() {
		return renderContent;
	}
	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	public String getLocationDataFrom() {
		return locationDataFrom;
	}
	public void setLocationDataFrom(String locationDataFrom) {
		this.locationDataFrom = locationDataFrom;
	}

	public String getGoogleSpreadSheetUrl() {
		return googleSpreadSheetUrl;
	}

	public void setGoogleSpreadSheetUrl(String googleSpreadSheetUrl) {
		this.googleSpreadSheetUrl = googleSpreadSheetUrl;
	}

	public String getLocationResponseList() {
		return locationResponseList;
	}

	public void setLocationResponseList(String locationResponseList) {
		this.locationResponseList = locationResponseList;
	}

	public String getLocationList() {
		return locationResponseList;
	}

	public void setLocationList(String locationResponseList) {
		this.locationResponseList = locationResponseList;
	}

	public ArrayList<LocationModel> getLocationResult() {
		return locationResult;
	}

	public void setLocationResult(ArrayList<LocationModel> locationResult) {
		this.locationResult = locationResult;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	
	public String getNearestlocationResponseList() {
		return nearestlocationResponseList;
	}
	public void setNearestlocationResponseList(String nearestlocationResponseList) {
		this.nearestlocationResponseList = nearestlocationResponseList;
	}
	public String getResponseType() {
		  return responseType;
	}
	public void setResponseType(String responseType) {
		  this.responseType = responseType;
	}
	public String getCustomParam() {
		  return customParam;
	}
	public void setCustomParam(String customParam) {
		  this.customParam = customParam;
	}
	public String storeList() throws Exception {
		WarehouseModel warehouseModel = new WarehouseModel();
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String latitude = "0";
		String longitude = "0";
		String responseType = CommonUtility.validateString(request.getParameter("responseType"));
		String identifier = "";
		if(CommonUtility.validateString(request.getParameter("latitude")).trim().length()>0){
			latitude = request.getParameter("latitude");
		}
		if(CommonUtility.validateString(request.getParameter("longitude")).trim().length()>0){
			longitude = request.getParameter("longitude");
		}
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		if(CommonUtility.validateString(request.getParameter("identifier")).trim().length()>0){
			identifier = request.getParameter("identifier");
		}
		warehouseModel.setLatitude(latitude);
		warehouseModel.setLongitude(longitude);
		ArrayList<WarehouseModel> WarehouseList = new UsersAction().getWareHouseListAndDistanceToUsersLocation(warehouseModel);
		contentObject.put("warehouseList", WarehouseList);
			if(!(CommonUtility.validateString(identifier).length()>0)) {
			session.removeAttribute("wareHouseCode");
			session.removeAttribute("defaultsWrhseSet");
			session.setAttribute("wareHouseCode", WarehouseList.get(0).getWareHouseCode());
			session.removeAttribute("wareHouseName");
			session.setAttribute("wareHouseName", WarehouseList.get(0).getWareHouseName());
			}
		if(CommonUtility.validateString(responseType).length() > 0) {
			contentObject.put("responseType", responseType);
			renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject , null, null, null);
		}else {
			renderContent = LayoutGenerator.templateLoader("StoreListPage", contentObject , null, null, null);
		}
		return SUCCESS;
	}
	
	public String setWareHouseCodeSession() throws Exception {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String wareHouseCode = "";
		String wareHouseName = "";
		//String identifier = request.getParameter("identifier");
		if(request.getParameter("wareHouseCode")!=null && request.getParameter("wareHouseCode").trim().length()>0){
			wareHouseCode = request.getParameter("wareHouseCode");
			wareHouseName = request.getParameter("wareHouseName");
//			if(identifier!=null && identifier.trim().length()>0){
//				if(identifier.equalsIgnoreCase("SHIPTOSTORE")){
//					session.removeAttribute("shipToStoreWareHouseCode");
//					session.removeAttribute("shipToStoreWareHouseName");
//					session.setAttribute("shipToStoreWareHouseCode", wareHouseCode);
//					session.setAttribute("shipToStoreWareHouseName", wareHouseName);
//				}else if(identifier.equalsIgnoreCase("STOREPICKUP")){
//					session.removeAttribute("pickUpAtWareHouseCode");
//					session.removeAttribute("pickUpAtWareHouseName");
//					session.setAttribute("pickUpAtWareHouseCode", wareHouseCode);
//					session.setAttribute("pickUpAtWareHouseName", wareHouseName);
//				}else{
					session.removeAttribute("wareHouseCode");
					session.removeAttribute("wareHouseName");
					session.setAttribute("wareHouseCode", wareHouseCode);
					session.setAttribute("wareHouseName", wareHouseName);
					session.setAttribute("defaultsWrhseSet", "Y");
//					session.removeAttribute("pickUpAtWareHouseCode");
//					session.removeAttribute("pickUpAtWareHouseName");
//					session.removeAttribute("shipToStoreWareHouseCode");
//					session.removeAttribute("shipToStoreWareHouseName");
//				}
//			}
		}else {
			session.setAttribute("wareHouseCode", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR")));
		}
		return SUCCESS;
	}
	
	public String Location() throws Exception {
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		LinkedHashMap<String,String> activeWarehouses = null;
		String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, String> distinctState = new LinkedHashMap<String, String>();
		ArrayList<LocationModel> locationListHide = new ArrayList<LocationModel>();
		Gson priceInquiry = new Gson();
		String selectedState = CommonUtility.validateString(request.getParameter("selectedState"));
		if(sessionUserId==null || sessionUserId.trim().length()<1){
			    session = request.getSession();
			    LoginAuthentication authentication = new LoginAuthentication();
				authentication.authenticate("web", "web", session);
				sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
		}
		
		int userId = CommonUtility.validateNumber(sessionUserId);
		locationDataFrom = CommonDBQuery.getSystemParamtersList().get("GET_MAP_FROM");
		googleSpreadSheetUrl = CommonDBQuery.getSystemParamtersList().get("GOOGLE_MAP_DOC_URL");
		
		//System.out.println("locationDataFrom = "+locationDataFrom);
		//System.out.println("googleSpreadSheetUrl = "+googleSpreadSheetUrl);
		
		ArrayList<LocationModel> locationList = new ArrayList<LocationModel>();
		ArrayList<LocationModel> serviceList = new ArrayList<LocationModel>();
		ArrayList<LocationModel> nearestLoctionList = new ArrayList<LocationModel>();
		LinkedHashMap<String, ProductsModel> BranchList = null;
		//CustomServiceProvider
		if(CommonUtility.customServiceUtility()!=null) {
			BranchList = CommonUtility.customServiceUtility().activeWarehouseList();
		}
		//CustomServiceProvider
		if(BranchList != null && BranchList.size() > 0){
			
			for(Entry<String, ProductsModel> entry :BranchList.entrySet()){      
				ProductsModel temp = entry.getValue();
				
				LocationModel locationVal = new LocationModel();
				locationVal.setBranchName(CommonUtility.validateString(temp.getBranchName()));
				locationVal.setBranchId(CommonUtility.validateString(temp.getBranchID()));
				locationVal.setBranchCode(CommonUtility.validateString(entry.getKey()));
				locationVal.setLatitude(CommonUtility.validateString((temp.getBranchLatitude())));
				locationVal.setLongitude(CommonUtility.validateString(temp.getBranchLongitude()));
				locationVal.setPhone(CommonUtility.validateString(CommonUtility.validateString(temp.getBranchPhoneNumber())));
				locationVal.setLocality(CommonUtility.validateString(temp.getBranchCity()) + ", " + CommonUtility.validateString(temp.getBranchState()) + " " + CommonUtility.validateString(temp.getBranchPostalCode()));
				locationVal.setCity(CommonUtility.validateString(temp.getBranchCity()));
				locationVal.setState(CommonUtility.validateString(temp.getBranchState()));
				locationVal.setZip(CommonUtility.validateString(temp.getBranchPostalCode()));
				locationVal.setStreet(CommonUtility.validateString(temp.getBranchAddress1()));
				locationVal.setWorkHour(CommonUtility.validateString(temp.getBranchWorkingHours()));
				locationVal.setEmail(CommonUtility.validateString(temp.getBranchEmail()));
				locationVal.setNote("");
				locationVal.setCountry(CommonUtility.validateString(temp.getBranchCountry()));
				locationVal.setTollFreeNum(CommonUtility.validateString(temp.getBranchTollFreeNumber()));
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("LOAD_WAREHOUSE_LOCATIONS")).equalsIgnoreCase("Y")){
					locationVal.setCustomFields(CommonDBQuery.getWareHouseCustomFieldsList().get(temp.getBranchID()));
				}
				if(CommonDBQuery.getWareHouseCustomFieldsList().get(temp.getBranchID()) != null && CommonDBQuery.getWareHouseCustomFieldsList().get(temp.getBranchID()).size() > 0) {
					ArrayList<CustomFieldModel> warehouseCustomFields = CommonDBQuery.getWareHouseCustomFieldsList().get(temp.getBranchID());
					for (CustomFieldModel customFieldModel : warehouseCustomFields) {
						if(CommonUtility.validateString(customFieldModel.getFieldName()).equalsIgnoreCase("WAREHOUSE_IMAGE")) {
							locationVal.setWarehouseImage(customFieldModel.getCustomFieldvalue());
						}
					}
				}
				distinctState.put(CommonUtility.validateString(temp.getBranchState()), CommonUtility.validateString(temp.getBranchState()));
				locationList.add(locationVal);
			}
			
		}else if (locationDataFrom!=null && locationDataFrom.equalsIgnoreCase("database")) {
			if(CommonUtility.customServiceUtility()!=null) {
				activeWarehouses = CommonUtility.customServiceUtility().redefinedWarehouseList();
			if(activeWarehouses!=null && activeWarehouses.size()>0){
				for(Entry<String, String> entry : activeWarehouses.entrySet()){
					LocationModel locationValhide = new LocationModel();
					locationValhide.setBranchCode(entry.getKey());
					locationValhide.setBranchName(entry.getValue());
					locationListHide.add(locationValhide);
				}
			}
			}
			String getLocationInfo = "";
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("LOAD_WAREHOUSE_LOCATIONS")).equalsIgnoreCase("Y")){
				getLocationInfo = PropertyAction.SqlContainer.get("getBranchLocationsFromWarehouse");
			}else{
				getLocationInfo = PropertyAction.SqlContainer.get("getBranchLocation");
			}
			Connection conn = null;
			ResultSet rs = null;
			PreparedStatement preStat = null;

			try {
				conn = ConnectionManager.getDBConnection();
				preStat = conn.prepareStatement(getLocationInfo);
				rs = preStat.executeQuery();

				while (rs.next()) {
					//BRANCH_LOC_ID,LOCATION_TITLE,PHONE,EMAIL,LATTITUDE,LONGITUDE,STREET_ADDRESS,STATE,AC,CITY,ZIP,FAX,WORK_HOUR,NOTE
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
					locationVal.setServiceManager(CommonUtility.validateString(rs.getString("SERVICE_MANAGER")));
					
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("LOAD_WAREHOUSE_LOCATIONS")).equalsIgnoreCase("Y")){
						locationVal.setCustomFields(CommonDBQuery.getWareHouseCustomFieldsList().get(rs.getString("WAREHOUSE_ID")));
					}
					if(CommonDBQuery.getWareHouseCustomFieldsList().get(rs.getString("WAREHOUSE_ID")) != null && CommonDBQuery.getWareHouseCustomFieldsList().get(rs.getString("WAREHOUSE_ID")).size() > 0) {
						ArrayList<CustomFieldModel> warehouseCustomFields = CommonDBQuery.getWareHouseCustomFieldsList().get(rs.getString("WAREHOUSE_ID"));
						for (CustomFieldModel customFieldModel : warehouseCustomFields) {
							if(CommonUtility.validateString(customFieldModel.getFieldName()).equalsIgnoreCase("WAREHOUSE_IMAGE")) {
								locationVal.setWarehouseImage(customFieldModel.getCustomFieldvalue());
							}
							if(CommonUtility.validateString(customFieldModel.getFieldName()).equalsIgnoreCase("WAREHOUSE_DESCRIPTION")) {
								locationVal.setWarehouseDescription(customFieldModel.getCustomFieldvalue());
							}
							if(CommonUtility.validateString(customFieldModel.getFieldName()).equalsIgnoreCase("LINE_CARD_NAME")) {
								locationVal.setLineCardName(customFieldModel.getCustomFieldvalue());
							}
							if(CommonUtility.validateString(customFieldModel.getFieldName()).equalsIgnoreCase("LINE_CARD_LINK")) {
								locationVal.setLineCardLink(customFieldModel.getCustomFieldvalue());
							}
						}
					}
					distinctState.put(CommonUtility.validateString(rs.getString("STATE")), CommonUtility.validateString(rs.getString("STATE")));
					locationList.add(locationVal);
				}
				
				if(CommonDBQuery.getSystemParamtersList().get("SORT_BY_NEAREST_WAREHOUSE")!=null && CommonDBQuery.getSystemParamtersList().get("SORT_BY_NEAREST_WAREHOUSE").trim().equalsIgnoreCase("Y")){
					String currentLatitude = "";
					String currentLongitude = "";
					Cookie[] cookie_jar = request.getCookies();
					if (cookie_jar != null){
						for (int i =0; i< cookie_jar.length; i++){
							Cookie aCookie = cookie_jar[i];
							System.out.println("Name : " + aCookie.getName()+" : Value : " + aCookie.getValue());
							if(aCookie!=null && aCookie.getName()!=null && aCookie.getName().trim().equalsIgnoreCase("currentLongitude")){
								currentLongitude = aCookie.getValue();
							}else if(aCookie!=null && aCookie.getName()!=null && aCookie.getName().trim().equalsIgnoreCase("currentLatitude")){
								currentLatitude = aCookie.getValue();
							}
							
						}
					}
						
					if(currentLatitude!=null && currentLongitude!=null && currentLatitude.trim().length()>0 && currentLongitude.trim().length()>0){
						if(locationList!=null && locationList.size()>0){
							for(LocationModel locationModel : locationList){
								double distance;
							    distance = 	distanceCalculatorBetweenTwoPoints(Double.parseDouble(CommonUtility.validateString(currentLatitude)),Double.parseDouble(CommonUtility.validateString(currentLongitude)),Double.parseDouble(CommonUtility.validateString(locationModel.getLatitude())),Double.parseDouble(CommonUtility.validateString(locationModel.getLongitude())),"M");
							    locationModel.setDistance(distance);
							}
							Collections.sort(locationList);
						}
					}	
				}
				
				
				if(CommonUtility.customServiceUtility()!=null) {
					serviceList = CommonUtility.customServiceUtility().loadServices();
					nearestLoctionList = CommonUtility.customServiceUtility().loadnearestwarehouses(locationList);
					nearestlocationResponseList = priceInquiry.toJson(nearestLoctionList);
				} 	
				
				 if(userId>1){
					 target = "success";
				 }

			} catch (Exception e) {
				
				e.printStackTrace();
			}
			finally {
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			}
		}
		
		locationResponseList = priceInquiry.toJson(locationList);
		System.out.println(locationResponseList);
		
		contentObject.put("nearestlocationResponseList", nearestlocationResponseList);
		contentObject.put("locationResponseList", locationResponseList);
		contentObject.put("locationListHide", locationListHide);
		contentObject.put("locationDataFrom", locationDataFrom);
		contentObject.put("googleSpreadSheetUrl", googleSpreadSheetUrl);
		contentObject.put("getServiceInfo", serviceList);
		contentObject.put("mapCenterCord", CommonDBQuery.getSystemParamtersList().get("MAP_CENTRE_COORDINATES"));
		contentObject.put("zoomVal",CommonDBQuery.getSystemParamtersList().get("MAP_ZOOM_VALUE"));
		contentObject.put("locationList", locationList);
		contentObject.put("distinctState", distinctState);
		contentObject.put("selectedState", CommonUtility.validateString(selectedState));
		if(CommonUtility.validateString(request.getParameter("requestType")).equalsIgnoreCase("JSON")){
			renderContent = locationResponseList;
		}else{
			renderContent = LayoutGenerator.templateLoader("BranchLocationPage", contentObject , null, null, null);
		}
		//System.out.println("renderContent : \n"+renderContent);
		return SUCCESS;
	}
	
	public String CustomLocations() throws Exception {
		
		request =ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		Gson priceInquiry = new Gson();
		ArrayList<LocationModel> serviceList = new ArrayList<LocationModel>();
		ArrayList<LocationModel> locationList = new ArrayList<LocationModel>();
		ArrayList<LocationModel> nearestLoctionList = new ArrayList<LocationModel>();
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
        String partNumber = "";
		
		if(CommonUtility.validateString(request.getParameter("selectedState")).length()>0){
			selectedState = CommonUtility.validateString(request.getParameter("selectedState"));
		}else if(CommonUtility.validateString(request.getParameter("selectedZipCode")).length()>0){
			selectedZipCode = CommonUtility.validateString(request.getParameter("selectedZipCode"));
			if(CommonUtility.validateNumber(request.getParameter("selectedMiles"))>0){
				selectedMiles = CommonUtility.validateNumber(request.getParameter("selectedMiles"));
			}
			if(CommonUtility.validateString(request.getParameter("partNumber")).length()>0){
			    partNumber = CommonUtility.validateString(request.getParameter("partNumber"));
			}			
			/*YahooBossSupport getUserLocation = new YahooBossSupport();
			UsersModel locaDetail = new UsersModel();
			locaDetail.setZipCode(CommonUtility.validateString(selectedZipCode));
			locaDetail = getUserLocation.locateUser(locaDetail);*/
			
			GeoLocatorProvider geoLocatorProvider = GeoLocatorEnums.getGeoLocatorType(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GEO_LOCATOR")));
			IGeoLocatorService geoLocatorService = GeoLocatorServiceProvider.getServiceProvider(geoLocatorProvider);
			GeoLocatorRequest geoLocatorRequest = new GeoLocatorRequest(CommonUtility.validateString(selectedZipCode));
			GeoLocatorResponse geoLocatorResponse = geoLocatorService.locateUser(geoLocatorRequest);
			
			//String latitude  = "41.26095";
	        //String longitude = "-110.99039";
	        latitude  = geoLocatorResponse != null ? geoLocatorResponse.getLatitude() : "";
	        longitude = geoLocatorResponse != null ? geoLocatorResponse.getLongitude() : "";
	        
	        /*Cookie myCookieLongitude = new Cookie("currentLongitude", longitude);
	        myCookieLongitude.setSecure(false);
	        myCookieLongitude.setMaxAge(-1);
	        myCookieLongitude.setPath("/");
	        response.addCookie(myCookieLongitude);
	        
	        Cookie myCookieLatitude = new Cookie("currentLatitude", latitude);
	        myCookieLatitude.setSecure(false);
	        myCookieLatitude.setMaxAge(-1);
	        myCookieLatitude.setPath("/");
	        response.addCookie(myCookieLatitude);*/
		}
		
		 
		
		int userId = CommonUtility.validateNumber(sessionUserId);
		locationDataFrom = CommonDBQuery.getSystemParamtersList().get("GET_MAP_FROM");
		googleSpreadSheetUrl = CommonDBQuery.getSystemParamtersList().get("GOOGLE_MAP_DOC_URL");
		
		//System.out.println("locationDataFrom = "+locationDataFrom);
		//System.out.println("googleSpreadSheetUrl = "+googleSpreadSheetUrl);
		
		if (locationDataFrom!=null && locationDataFrom.equalsIgnoreCase("database") && CommonUtility.validateString(latitude).length()>0 && CommonUtility.validateString(longitude).length()>0) {
			String getLocationInfo = "";
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("LOAD_WAREHOUSE_LOCATIONS")).equalsIgnoreCase("Y")){
				getLocationInfo = PropertyAction.SqlContainer.get("getBranchLocationsFromWarehouse");
			}else{
				getLocationInfo = PropertyAction.SqlContainer.get("getBranchLocation");
			}
			Connection conn = null;
			ResultSet rs = null;
			PreparedStatement preStat = null;

			try {
				conn = ConnectionManager.getDBConnection();
				preStat = conn.prepareStatement(getLocationInfo);
				rs = preStat.executeQuery();

				while (rs.next()) {
					//BRANCH_LOC_ID,LOCATION_TITLE,PHONE,EMAIL,LATTITUDE,LONGITUDE,STREET_ADDRESS,STATE,AC,CITY,ZIP,FAX,WORK_HOUR,NOTE
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
					locationVal.setServiceManager(CommonUtility.validateString(rs.getString("SERVICE_MANAGER")));
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
							if(CommonUtility.validateString(customFieldModel.getFieldName()).equalsIgnoreCase("LINE_CARD_NAME")) {
								locationVal.setLineCardName(customFieldModel.getCustomFieldvalue());
							}
							if(CommonUtility.validateString(customFieldModel.getFieldName()).equalsIgnoreCase("LINE_CARD_LINK")) {
								locationVal.setLineCardLink(customFieldModel.getCustomFieldvalue());
							}
						}
					}		
					if(CommonUtility.validateString(selectedState).length()>0 && CommonUtility.validateString(selectedState).equalsIgnoreCase(CommonUtility.validateString(rs.getString("STATE")))){
						locationList.add(locationVal);
					}else if(CommonUtility.validateString(selectedZipCode).length()>0 && selectedMiles>0){
						locationList.add(locationVal);
					}
				}
				if(CommonDBQuery.getSystemParamtersList().get("SORT_BY_NEAREST_WAREHOUSE")!=null && CommonDBQuery.getSystemParamtersList().get("SORT_BY_NEAREST_WAREHOUSE").trim().equalsIgnoreCase("Y")){
					if(latitude!=null && longitude!=null && CommonUtility.validateString(latitude).length()>0 && CommonUtility.validateString(longitude).length()>0){
						if(locationList!=null && locationList.size()>0){
							for(LocationModel locationModel : locationList){
								double distance;
							    distance = 	CommonUtility.calculateDistBetweenPoints(Double.parseDouble(CommonUtility.validateString(latitude)),Double.parseDouble(CommonUtility.validateString(longitude)),Double.parseDouble(CommonUtility.validateString(locationModel.getLatitude())),Double.parseDouble(CommonUtility.validateString(locationModel.getLongitude())));
							    locationModel.setDistance(distance);
							}
							Collections.sort(locationList);
						}
					}
					if(selectedMiles>0){
						ArrayList<LocationModel> locationListWithInMiles = new ArrayList<LocationModel>();
						for(LocationModel locationDistanceVal:locationList){
							if(locationDistanceVal.getDistance()<=selectedMiles){
								locationListWithInMiles.add(locationDistanceVal);
							}
						}
						locationList = new ArrayList<LocationModel>();
						locationList = locationListWithInMiles;
					}
					
				}
				if(CommonUtility.customServiceUtility()!=null) {
					serviceList = CommonUtility.customServiceUtility().loadServices();
					nearestLoctionList = CommonUtility.customServiceUtility().loadnearestwarehouses(locationList);
					nearestlocationResponseList = priceInquiry.toJson(locationList);
				} 		
				 if(userId>1){
					 target = "success";
				 }

			} catch (Exception e) {
				
				e.printStackTrace();
			}
			finally {
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			}
		}
		locationResponseList = priceInquiry.toJson(locationList);
		System.out.println(locationResponseList);
		contentObject.put("nearestlocationResponseList", nearestlocationResponseList);
		contentObject.put("locationResponseList", locationResponseList);
		contentObject.put("locationDataFrom", locationDataFrom);
		contentObject.put("googleSpreadSheetUrl", googleSpreadSheetUrl);
		contentObject.put("mapCenterCord", CommonDBQuery.getSystemParamtersList().get("MAP_CENTRE_COORDINATES"));
		contentObject.put("zoomVal",CommonDBQuery.getSystemParamtersList().get("MAP_ZOOM_VALUE"));
		contentObject.put("locationList", locationList);
		contentObject.put("getServiceInfo", serviceList);
		contentObject.put("distinctState", distinctState);
		contentObject.put("selectedState", CommonUtility.validateString(selectedState));
		contentObject.put("selectedMiles", CommonUtility.validateParseIntegerToString(selectedMiles));
		contentObject.put("selectedZipCode", CommonUtility.validateString(selectedZipCode));
		contentObject.put("responseType", responseType);
		contentObject.put("customParam", customParam);
		contentObject.put("partNumber", partNumber);
		if(CommonUtility.validateString(responseType).length()>0) {
		   renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		}else {
		   renderContent = LayoutGenerator.templateLoader("BranchLocationPage", contentObject , null, null, null);
		}
		return SUCCESS;
	}
	
	public static double distanceCalculatorBetweenTwoPoints(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
	    double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	    dist = Math.acos(dist);
	    dist = rad2deg(dist);
	    dist = dist * 60 * 1.1515;
	    if (unit == "K") {
	      dist = dist * 1.609344;
	    }else if (unit == "N"){
	      dist = dist * 0.8684;
	    }
	    double finalValue = Math.round( dist * 100.0 ) / 100.0;
	    return (finalValue);
	}
	
	private static double rad2deg(double rad) {
	    return (rad * 180.0 / Math.PI);
	}
	
	private static double deg2rad(double deg) {
	    return (deg * Math.PI / 180.0);
	}
	
	
	public String nearestLocationBasedOnZipCode() throws Exception {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String identifier = "";
		String zipCode = "";
		if(request.getParameter("zipCode")!=null && request.getParameter("zipCode").trim().length()>0){
			zipCode = request.getParameter("zipCode");
		}
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		if(request.getParameter("identifier")!=null && request.getParameter("identifier").trim().length()>0){
			identifier = request.getParameter("identifier");
			contentObject.put("identifier", identifier);
		}
		ArrayList<WarehouseModel> WarehouseList = null; 
		Cimm2BCentralNearestWareHouseResponse response = new UserManagementImpl().getNearestWareHouseForCimm2BCentral(zipCode);
		if(response != null){
			WarehouseList = new ArrayList<WarehouseModel>();
			ArrayList<Cimm2BCentralNearestWareHouse> NearestwarehouseList = response.getDetails();
			for(int i=0; i< NearestwarehouseList.size(); i++){
				
				WarehouseModel model = new WarehouseModel();
				model.setWareHouseCode(NearestwarehouseList.get(i).getWarehouseCode());
				model.setWareHouseName(NearestwarehouseList.get(i).getWarehouseName());
				model.setDistance(Math.round(NearestwarehouseList.get(i).getDistance() * 100d)/100d);
				model.setAddress1(NearestwarehouseList.get(i).getWarehouseAddress().getAddressLine1());
				model.setCity(NearestwarehouseList.get(i).getWarehouseAddress().getCity());
				model.setState(NearestwarehouseList.get(i).getWarehouseAddress().getState());
				model.setZip(NearestwarehouseList.get(i).getWarehouseAddress().getZipCode());
				WarehouseList.add(model);
			}
		}

		contentObject.put("warehouseList", WarehouseList);
		if(session.getAttribute("wareHouseCode") == null || session.getAttribute("wareHouseCode").toString().trim().equals("")){
			session.removeAttribute("wareHouseCode");
			session.removeAttribute("wareHouseName");
			if(WarehouseList!=null && WarehouseList.size()>0) {
				session.setAttribute("wareHouseCode", WarehouseList.get(0).getWareHouseCode());
				session.setAttribute("wareHouseName", WarehouseList.get(0).getWareHouseName());
			}
		}
		renderContent = LayoutGenerator.templateLoader("StoreListPage", contentObject , null, null, null);
		return SUCCESS;
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
        customParam = request.getParameter("customParam");
        locationDataFrom = CommonDBQuery.getSystemParamtersList().get("GET_MAP_FROM");
	    selectedZipCode = CommonUtility.validateString(request.getParameter("selectedZipCode"));	
	    selectedState = CommonUtility.validateString(request.getParameter("selectedState"));
		ArrayList<LocationModel> locationList = new ArrayList<LocationModel>();
		if (locationDataFrom!=null && locationDataFrom.equalsIgnoreCase("database")) {
			String getLocationInfo = "";
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("LOAD_WAREHOUSE_LOCATIONS")).equalsIgnoreCase("Y")){
				getLocationInfo = PropertyAction.SqlContainer.get("getBranchLocationsFromWarehouse");
			}else{
				getLocationInfo = PropertyAction.SqlContainer.get("getBranchLocation");
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
					locationVal.setPhone(CommonUtility.validateString((rs.getString("AC")).length()>0 && CommonUtility.validateNumber(rs.getString("AC"))>0?CommonUtility.validateString(rs.getString("AC")) + "-":"") + CommonUtility.validateString(rs.getString("PHONE")));
					locationVal.setFaxNum(CommonUtility.validateString((rs.getString("AC")).length()>0 && CommonUtility.validateNumber(rs.getString("AC"))>0?CommonUtility.validateString(rs.getString("AC")) + "-":"")+ CommonUtility.validateString(rs.getString("FAX")));
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
							if(CommonUtility.validateString(customFieldModel.getFieldName()).equalsIgnoreCase("LINE_CARD_NAME")) {
								locationVal.setLineCardName(customFieldModel.getCustomFieldvalue());
							}
							if(CommonUtility.validateString(customFieldModel.getFieldName()).equalsIgnoreCase("LINE_CARD_LINK")) {
								locationVal.setLineCardLink(customFieldModel.getCustomFieldvalue());
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
		contentObject.put("googleSpreadSheetUrl", googleSpreadSheetUrl);
		contentObject.put("mapCenterCord", CommonDBQuery.getSystemParamtersList().get("MAP_CENTRE_COORDINATES"));
		contentObject.put("zoomVal",CommonDBQuery.getSystemParamtersList().get("MAP_ZOOM_VALUE"));
		contentObject.put("locationList", locationList);
		contentObject.put("distinctState", distinctState);
		contentObject.put("selectedState", CommonUtility.validateString(selectedState));
		contentObject.put("selectedMiles", CommonUtility.validateParseIntegerToString(selectedMiles));
		contentObject.put("selectedZipCode", CommonUtility.validateString(selectedZipCode));
		contentObject.put("responseType", "locationsWithin");			
		if(CommonUtility.validateString(request.getParameter("requestType")).equalsIgnoreCase("JSON")){
			renderContent = locationResponseList;
		}else{
			renderContent = LayoutGenerator.templateLoader("BranchLocationPage", contentObject , null, null, null);
		}
		return SUCCESS;
	}

public  String getServiceInfo() throws Exception {
	
	request = ServletActionContext.getRequest();
    String serviceName= CommonUtility.validateString(service);
    ArrayList<LocationModel> serviceDetails=new ArrayList<LocationModel>();
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	Connection  conn = null;
    PreparedStatement preStat=null;
    ResultSet rs = null;
	try {
		conn = ConnectionManager.getDBConnection();
		String getLocationInfo = PropertyAction.SqlContainer.get("getServiceDetails");
		preStat = conn.prepareStatement(getLocationInfo);
		preStat.setString(1, serviceName);
        rs = preStat.executeQuery();
		 while (rs.next()) {
			 LocationModel serviceInfo =new LocationModel();
			 serviceInfo.setBranchId(rs.getString("WAREHOUSE_ID"));
			 serviceInfo.setServiceManager(rs.getString("FIELD_NAME"));
			 serviceInfo.setBranchName(rs.getString("WAREHOUSE_NAME"));
			 serviceInfo.setBranchCode(rs.getString("WAREHOUSE_CODE"));
			 serviceInfo.setPhone(rs.getString("PHONE"));
			 serviceInfo.setEmail(rs.getString("EMAIL"));
			 serviceInfo.setLatitude(rs.getString("LATTITUDE"));
			 serviceInfo.setLongitude(rs.getString("LONGITUDE"));
			 serviceInfo.setStreet(rs.getString("STREET_ADDRESS"));
			 serviceInfo.setState(rs.getString("STATE"));
			 serviceInfo.setCity(rs.getString("CITY"));
			 serviceInfo.setZip(rs.getString("ZIP"));
			 serviceInfo.setFaxNum(rs.getString("FAX"));
			 serviceInfo.setWorkHour(rs.getString("WORK_HOUR"));
			 serviceInfo.setNote(rs.getString("NOTE"));
			 serviceInfo.setCountry(rs.getString("COUNTRY"));
			 serviceInfo.setTollFreeNum(rs.getString("TOLL_FREE"));
			 serviceDetails.add(serviceInfo);
		 }

	}
	catch (SQLException e) { 
		e.printStackTrace();
	}
	finally {	    
		    	ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(preStat);
		    	ConnectionManager.closeDBConnection(conn);	
		     }
	contentObject.put("serviceDetails",serviceDetails);
	contentObject.put("responseType","serviceDetails");
	renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
	return SUCCESS;

	}

}
