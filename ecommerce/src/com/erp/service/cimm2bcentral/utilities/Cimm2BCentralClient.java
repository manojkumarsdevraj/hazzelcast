package com.erp.service.cimm2bcentral.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;

import com.erp.service.rest.util.HttpConnectionUtility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BShippingMethod;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.exception.RestServiceException;
import com.unilog.security.SecureData;
import com.unilog.users.AddressModel;
import com.unilog.users.UserLogin;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.Cimm2bBaseUrlProvider;
import com.unilog.utility.CommonUtility;
import com.unilog.cimmesb.client.request.HttpMethod;


public class Cimm2BCentralClient{

	private static Cimm2BCentralClient cimm2BCentralClient;
	private boolean reInitializeClient = true;
	Gson gson = new Gson();
	public static String ERP_NAME = "ErpName";
	public static String CLIENT_ID = "ClientId";
	public static String SITE_ID = "SiteId";
	public static String siteId;
	public static String clientId;
	public static String CUSTOMER_USER_NAME = "CustomerUsername";
	public static String CUSTOMER_PASSWORD = "CustomerPassword";



	public void initializeClient(){
		clientId = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CLEINT_ID"));
		siteId = CommonUtility.validateParseIntegerToString(CommonDBQuery.getGlobalSiteId());
		reInitializeClient = false;
	}

	public static Cimm2BCentralClient getInstance() {
		if (cimm2BCentralClient == null) {
			synchronized (Cimm2BCentralClient.class) {
					cimm2BCentralClient = new Cimm2BCentralClient();
			}
		}
		return cimm2BCentralClient;
	}
	
	public Cimm2BCentralResponseEntity getDataObjectWithSession(String url, String requestType, Object requestEntity, Class<?> dataClass,HttpSession session)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		if(reInitializeClient){
			initializeClient();
		}
		Cimm2BCentralResponseEntity responseEntity = null;

		try
		{
			String baseUri="";
			if(!(baseUri=Cimm2bBaseUrlProvider.getInstance().getBaseUri(url)).isEmpty()){
				if(baseUri.endsWith("/") && url.startsWith("/")) {
					url=url.substring(1);
				}
				url = baseUri+url;
			}else{
				throw new RuntimeException("CIMM2BC URL is not configured in the system parameter with the key 'CIMM2BCENTRAL_ACCESS_URL'");
			}

			String result = getJsonResponseSession(url, requestType, requestEntity, session);
			JsonElement json = new JsonParser().parse(result);
			JsonObject jsonObject = json.getAsJsonObject();
			JsonElement dataElement = jsonObject.get("data");
			Object data = null;
			if(dataElement != null && !dataElement.isJsonNull()){
				if(dataElement.isJsonObject()){
					data = gson.fromJson(dataElement, dataClass);
				}else{
					if(CommonUtility.validateString(dataElement.toString()).length()>0){
						data = CommonUtility.validateString(dataElement.toString()).replaceAll("\"", "");
					}
				}
				if(dataElement.isJsonArray()){
					List<Object> dataArrayList = new ArrayList<Object>();
					JsonArray dataArray = dataElement.getAsJsonArray();
					try {
						for (int i = 0; i < dataArray.size(); i++) {
							dataArrayList.add(gson.fromJson(dataArray.get(i).getAsJsonObject(), dataClass));
						}
					}catch (Exception e) {
						System.out.println(e);
					}
					if(dataArrayList.size() > 0) {
						data = dataArrayList;
					}
					else {
						for (int i = 0; i < dataArray.size(); i++) {
							dataArrayList.add(gson.fromJson(dataArray.get(i).toString(), dataClass));
						}
						data = dataArrayList;
					}
				}
			}
			JsonObject statusObject = jsonObject.getAsJsonObject("status");
			Cimm2BCentralStatus status = gson.fromJson(statusObject, Cimm2BCentralStatus.class);

			responseEntity = new Cimm2BCentralResponseEntity(data, status);
		}catch (Exception e) {
			e.printStackTrace();
			Cimm2BCentralStatus centralStatus = new Cimm2BCentralStatus();
			centralStatus.setCode(HttpStatus.SC_FAILED_DEPENDENCY);
			centralStatus.setMessage("Error while processing the request.");
			responseEntity = new Cimm2BCentralResponseEntity(null, centralStatus);
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return responseEntity;
	}
	public String getJsonResponseSession(String requestUrl, String requestType, Object requestParameters,HttpSession session) throws RestServiceException, IOException{
		String result = "";
		String customerUserName="";//reconstruct this as local variable(toggle case etc)
		String customerPassword="";

		try {
			System.out.println("Request url : " + requestUrl);
			HttpConnectionUtility httpConnectionUtility = HttpConnectionUtility.getInstance();

			List<NameValuePair> headerList = new ArrayList<NameValuePair>();
			headerList.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, CommonDBQuery.getCimm2bCentralAuthorization()));
			headerList.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
			headerList.add(new BasicNameValuePair(Cimm2BCentralClient.SITE_ID, siteId));
			headerList.add(new BasicNameValuePair(Cimm2BCentralClient.CLIENT_ID, clientId));
			if(requestUrl.contains("/payment/")){ 
				customerUserName =  ((Cimm2BCentralCreditCardDetails) requestParameters).getUserName();
				customerPassword = ((Cimm2BCentralCreditCardDetails) requestParameters).getPassword();

			}else {
				SecureData userPassword=new SecureData();
				customerUserName =CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY));
				customerPassword = userPassword.validatePassword((String)session.getAttribute("securedPassword"));
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WIDGET_URL")).length()>0 && requestUrl.contains(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WIDGET_URL")))) {
                    headerList.add(new BasicNameValuePair("Cookie","JSESSIONID=" + (String) session.getId()));
                }
			}
			
			StringEntity stringEntity=null;
			if (requestParameters != null && (requestType.equalsIgnoreCase(HttpMethod.POST.name()) || (requestType.equalsIgnoreCase(HttpMethod.PUT.name())) || (requestType.equalsIgnoreCase(HttpMethod.DELETE.name())))) {

				stringEntity = new StringEntity(gson.toJson(requestParameters), "UTF-8");
				System.out.println("RequestEntityParameters --- " + gson.toJson(requestParameters));
			}
			
			headerList.add(new BasicNameValuePair(Cimm2BCentralClient.CUSTOMER_USER_NAME, customerUserName));
			headerList.add(new BasicNameValuePair(Cimm2BCentralClient.CUSTOMER_PASSWORD, customerPassword));
			result = httpConnectionUtility.getApiResponse(requestUrl, requestType, headerList, stringEntity);
			
			/*Header[] headers = httpConnectionUtility.getHeaders(headerList);
			response = httpConnectionUtility.getApiResponse(requestUrl, requestType, headers, requestParameters);
			int responseCode = response.getStatusLine().getStatusCode();
			result = EntityUtils.toString(response.getEntity()).trim();
			System.out.println("Response Code : " + responseCode);
			System.out.println("Response for '" + requestUrl + "' --- "+ result);*/
		}
		catch (Exception e) {
			throw new RestServiceException(e.getMessage());
		}

		return result;
	}

	public Cimm2BCentralResponseEntity getDataObject(String url, String requestType, Object requestEntity, Class<?> dataClass)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		if(reInitializeClient){
			initializeClient();
		}
		Cimm2BCentralResponseEntity responseEntity = null;

		try
		{
			String baseUri="";
			if(!(baseUri=Cimm2bBaseUrlProvider.getInstance().getBaseUri(url)).isEmpty()){
				if(baseUri.endsWith("/") && url.startsWith("/")) {
					url=url.substring(1);
				}
				url = baseUri+url;
			}else{
				throw new RuntimeException("CIMM2BC URL is not configured in the system parameter with the key 'CIMM2BCENTRAL_ACCESS_URL'");
			}

			String result = getJsonResponse(url, requestType, requestEntity);
			JsonElement json = new JsonParser().parse(result);
			JsonObject jsonObject = json.getAsJsonObject();
			JsonElement dataElement = jsonObject.get("data");
			Object data = null;
			if(dataElement != null && !dataElement.isJsonNull()){
				if(dataElement.isJsonObject()){
					data = gson.fromJson(dataElement, dataClass);
				}else{
					if(CommonUtility.validateString(dataElement.toString()).length()>0){
						data = CommonUtility.validateString(dataElement.toString()).replaceAll("\"", "");
					}
				}
				if(dataElement.isJsonArray()){
					List<Object> dataArrayList = new ArrayList<Object>();
					JsonArray dataArray = dataElement.getAsJsonArray();
					try {
						for (int i = 0; i < dataArray.size(); i++) {
							dataArrayList.add(gson.fromJson(dataArray.get(i).getAsJsonObject(), dataClass));
						}
					}catch (Exception e) {
						System.out.println(e);
					}
					if(dataArrayList.size() > 0) {
						data = dataArrayList;
					}
					else {
						for (int i = 0; i < dataArray.size(); i++) {
							dataArrayList.add(gson.fromJson(dataArray.get(i).toString(), dataClass));
						}
						data = dataArrayList;
					}
				}
			}
			JsonObject statusObject = jsonObject.getAsJsonObject("status");
			Cimm2BCentralStatus status = gson.fromJson(statusObject, Cimm2BCentralStatus.class);

			responseEntity = new Cimm2BCentralResponseEntity(data, status);
		}catch (Exception e) {
			e.printStackTrace();
			Cimm2BCentralStatus centralStatus = new Cimm2BCentralStatus();
			centralStatus.setCode(HttpStatus.SC_FAILED_DEPENDENCY);
			centralStatus.setMessage("Error while processing the request.");
			responseEntity = new Cimm2BCentralResponseEntity(null, centralStatus);
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return responseEntity;
	}

	public String getJsonResponse(String requestUrl, String requestType, Object requestParameters) throws RestServiceException, IOException{
		String result = "";
		String customerUserName="";//reconstruct this as local variable(toggle case etc)
		String customerPassword="";

		try {
			System.out.println("Request url : " + requestUrl);
			HttpConnectionUtility httpConnectionUtility = HttpConnectionUtility.getInstance();

			List<NameValuePair> headerList = new ArrayList<NameValuePair>();
			headerList.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, CommonDBQuery.getCimm2bCentralAuthorization()));
			headerList.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
			headerList.add(new BasicNameValuePair(Cimm2BCentralClient.SITE_ID, siteId));
			headerList.add(new BasicNameValuePair(Cimm2BCentralClient.CLIENT_ID, clientId));
			if(requestUrl.contains("/payment/")){ 
				customerUserName =  ((Cimm2BCentralCreditCardDetails) requestParameters).getUserName();
				customerPassword = ((Cimm2BCentralCreditCardDetails) requestParameters).getPassword();

			}else {
				HttpServletRequest request;
				request = ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				SecureData userPassword=new SecureData();
				customerUserName =CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY));
				customerPassword = userPassword.validatePassword((String)session.getAttribute("securedPassword"));
			}
			
			StringEntity stringEntity=null;
			if (requestParameters != null && (requestType.equalsIgnoreCase(HttpMethod.POST.name()) || (requestType.equalsIgnoreCase(HttpMethod.PUT.name())) || (requestType.equalsIgnoreCase(HttpMethod.DELETE.name())))) {

				stringEntity = new StringEntity(gson.toJson(requestParameters), "UTF-8");
				System.out.println("RequestEntityParameters --- " + gson.toJson(requestParameters));
			}

			headerList.add(new BasicNameValuePair(Cimm2BCentralClient.CUSTOMER_USER_NAME, customerUserName));
			headerList.add(new BasicNameValuePair(Cimm2BCentralClient.CUSTOMER_PASSWORD, customerPassword));
			
			result = httpConnectionUtility.getApiResponse(requestUrl, requestType, headerList, stringEntity);

			/*Header[] headers = httpConnectionUtility.getHeaders(headerList);
			response = httpConnectionUtility.getApiResponse(requestUrl, requestType, headers, requestParameters);
			int responseCode = response.getStatusLine().getStatusCode();
			result = EntityUtils.toString(response.getEntity()).trim();
			System.out.println("Response Code : " + responseCode);
			System.out.println("Response for '" + requestUrl + "' --- "+ result);*/
		}
		catch (Exception e) {
			throw new RestServiceException(e.getMessage());
		}

		return result;
	}
	public AddressModel userModelToAddressModel(UsersModel userModel){
		AddressModel addressModel = new AddressModel();

		addressModel.setEntityId(userModel.getEntityId());
		addressModel.setEmailAddress(userModel.getEmailAddress());
		addressModel.setCompanyName(userModel.getCompanyName());
		addressModel.setFirstName(userModel.getFirstName());
		addressModel.setLastName(userModel.getLastName());
		addressModel.setAddressBookId(userModel.getAddressBookId());
		addressModel.setAddress1(userModel.getAddress1());
		addressModel.setAddress2(userModel.getAddress2());
		addressModel.setAddressType(userModel.getAddressType());
		addressModel.setCity(userModel.getCity());
		addressModel.setCountry(userModel.getCountry());
		addressModel.setState(userModel.getState());
		addressModel.setZipCode(userModel.getZipCode());
		addressModel.setShipToId(userModel.getShipToId());
		addressModel.setShipToName(userModel.getShipToName());
		addressModel.setPhoneNo(userModel.getPhoneNo());
		

		return addressModel;
	}
	
	public UsersModel addressModelToUsersModel( AddressModel addressModel){
		UsersModel usersModel = new UsersModel();
		usersModel.setAccountName(addressModel.getAccountName()!=null?addressModel.getAccountName():"");
		usersModel.setEntityId(addressModel.getEntityId());
		usersModel.setEmailAddress(addressModel.getEmailAddress());
		usersModel.setCompanyName(addressModel.getCompanyName());
		usersModel.setFirstName(addressModel.getFirstName());
		usersModel.setLastName(addressModel.getLastName());
		//addressModel.setAddressBookId(CommonUtility.validateNumber(userModel.getAddressId()));
		usersModel.setAddress1(addressModel.getAddress1());
		usersModel.setAddress2(addressModel.getAddress2()); 
		usersModel.setAddressType(addressModel.getAddressType());
		usersModel.setCity(addressModel.getCity());
		usersModel.setCountry(addressModel.getCountry());
		usersModel.setState(addressModel.getState());
		usersModel.setZipCode(addressModel.getZipCode());
		usersModel.setAnonymousUser(addressModel.isAnonymousUser());
		usersModel.setPhoneNo(addressModel.getPhoneNo());
		usersModel.setCustomerType(addressModel.getCustomerType());
		usersModel.setFaxNumber(addressModel.getFaxNumber()!=null?addressModel.getFaxNumber():"");
		usersModel.setRole(addressModel.getRole());
		usersModel.setUpdateRole(addressModel.isUpdateRole());
		usersModel.setUserStatus(addressModel.getUserStatus());
		usersModel.setNewsLetterSub(addressModel.getNewsLetterSub());
		usersModel.setShipAddress(addressModel.getShippingAddress());
		return usersModel;
	}
	

	public Cimm2BCentralAddress ecomUserModelToCimm2BCentralAddress(UsersModel addressModel){
		Cimm2BCentralAddress cimm2BCentralBillingAddress = new Cimm2BCentralAddress();
		if(	addressModel.getShipToId()!=null && addressModel.getShipToId().length()>0 && addressModel.getShipToId()!="0"){
			cimm2BCentralBillingAddress.setAddressERPId(addressModel.getShipToId());
		}else{
			cimm2BCentralBillingAddress.setAddressERPId(addressModel.getEntityId());
			if(CommonUtility.customServiceUtility()!=null)
			CommonUtility.customServiceUtility().overRideERPID(addressModel,cimm2BCentralBillingAddress);
			}
		cimm2BCentralBillingAddress.setAddressId(CommonUtility.validateParseIntegerToString(addressModel.getAddressBookId()));
		if(CommonUtility.customServiceUtility()!=null){
			CommonUtility.customServiceUtility().overRideAddressID(addressModel,cimm2BCentralBillingAddress);
			}
		cimm2BCentralBillingAddress.setAddressLine1(addressModel.getAddress1());
		cimm2BCentralBillingAddress.setAddressLine2(addressModel.getAddress2());
		cimm2BCentralBillingAddress.setAddressType(addressModel.getAddressType());
		cimm2BCentralBillingAddress.setCity(addressModel.getCity());
		cimm2BCentralBillingAddress.setCountry(addressModel.getCountry());
		cimm2BCentralBillingAddress.setState(addressModel.getState());
		cimm2BCentralBillingAddress.setZipCode(addressModel.getZipCode());
		cimm2BCentralBillingAddress.setCountryCode(addressModel.getCountry()!=null?addressModel.getCountry():"US");
		return cimm2BCentralBillingAddress;
	}

	public Cimm2BCentralAddress ecomAddressModelToCimm2BCentralAddress(AddressModel addressModel){
		Cimm2BCentralAddress cimm2BCentralBillingAddress = null;

		try {
			cimm2BCentralBillingAddress = new Cimm2BCentralAddress();
			if(addressModel.getShipToId()!=null && addressModel.getShipToId().length()>0){
				cimm2BCentralBillingAddress.setAddressERPId(addressModel.getShipToId());
			}else {
			cimm2BCentralBillingAddress.setAddressERPId(addressModel.getEntityId());
			if(CommonUtility.customServiceUtility()!=null)
			CommonUtility.customServiceUtility().overRideERPIDRequired(addressModel,cimm2BCentralBillingAddress);
			}
			cimm2BCentralBillingAddress.setCompanyName(addressModel.getCompanyName()!=null?addressModel.getCompanyName():addressModel.getShipToName());
			cimm2BCentralBillingAddress.setAddressId(CommonUtility.validateParseIntegerToString(addressModel.getAddressBookId()));
			if(CommonUtility.customServiceUtility()!=null){
				CommonUtility.customServiceUtility().overRideAddressID(addressModel,cimm2BCentralBillingAddress);
				}
			cimm2BCentralBillingAddress.setAddressLine1((addressModel.getAddress1()!=null)?addressModel.getAddress1():"");
			cimm2BCentralBillingAddress.setAddressLine2((addressModel.getAddress2()!=null)?addressModel.getAddress2():"");
			cimm2BCentralBillingAddress.setAddressType((addressModel.getAddressType()!=null)?addressModel.getAddressType():"");
			cimm2BCentralBillingAddress.setCity((addressModel.getCity()!=null)?addressModel.getCity():"");
			cimm2BCentralBillingAddress.setCountry((addressModel.getCountry()!=null)?addressModel.getCountry():"US");
			cimm2BCentralBillingAddress.setState((addressModel.getState()!=null)?addressModel.getState():"");
			cimm2BCentralBillingAddress.setZipCode(addressModel.getZipCode());
			cimm2BCentralBillingAddress.setPrimaryEmailAddress(addressModel.getEmailAddress());
			cimm2BCentralBillingAddress.setCustomerName(addressModel.getShipToName());
			cimm2BCentralBillingAddress.setCountryCode((addressModel.getCountry()!=null)?addressModel.getCountry():"US");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cimm2BCentralBillingAddress;

	}

	public AddressModel cimm2BCentralAddressToEcomAddressModel(Cimm2BCentralAddress cimm2BCentralAddress){
		AddressModel addressModel = new AddressModel();

		addressModel.setEntityId(cimm2BCentralAddress.getAddressERPId());
		addressModel.setAddressBookId(CommonUtility.validateNumber(cimm2BCentralAddress.getAddressId()));
		addressModel.setAddress1(cimm2BCentralAddress.getAddressLine1());
		addressModel.setAddress2(cimm2BCentralAddress.getAddressLine2());
		addressModel.setAddress3(cimm2BCentralAddress.getAddressLine3());
		addressModel.setAddressType(cimm2BCentralAddress.getAddressType());
		addressModel.setCity(cimm2BCentralAddress.getCity());
		addressModel.setCountry(cimm2BCentralAddress.getCountry());
		addressModel.setState(cimm2BCentralAddress.getState());
		addressModel.setZipCode(cimm2BCentralAddress.getZipCode());
		addressModel.setCompanyName(cimm2BCentralAddress.getCompanyName());
		return addressModel;
	}
	
	public UsersModel getCustomerInfo(Cimm2BCentralCustomer customerDetails){
		UsersModel customerinfo = new UsersModel();
		String warehouse = customerDetails.getHomeBranch()!=null?customerDetails.getHomeBranch():"";
		customerinfo.setEntityName(customerDetails.getCustomerName());

		Cimm2BCentralAddress address = customerDetails.getAddress();

		if(address != null){

			if(CommonUtility.validateString(address.getAddressLine1()).length() > 0){
				customerinfo.setAddress1(address.getAddressLine1());
			}else{
				customerinfo.setAddress1("No Address");
			}

			if(CommonUtility.validateString(address.getAddressLine2()).length() > 0){
				customerinfo.setAddress1(address.getAddressLine2());
			}else{
				customerinfo.setAddress1("No Address");
			}
			customerinfo.setCity(address.getCity());
			customerinfo.setState(address.getState());
			customerinfo.setZipCode(address.getZipCode());
			if(CommonUtility.validateString(address.getCountry()).length() > 0){
				customerinfo.setCountry(address.getCountry());
			}

		}

		customerinfo.setTermsType(customerDetails.getTermsType());
		customerinfo.setTermsTypeDesc(customerDetails.getTermsTypeDescription());
		customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(warehouse.replaceFirst("^0+(?!$)", "")));
		customerinfo.setWareHouseCodeStr(warehouse.replaceFirst("^0+(?!$)", ""));
		customerinfo.setBillEntityId(customerDetails.getBillToCustomerERPId());


		return customerinfo;
	}

	public ArrayList<Cimm2BCentralShipVia> cimm2BcentralShipMethodToShipVia(ArrayList<Cimm2BShippingMethod> shipMethod) {
		
		ArrayList<Cimm2BCentralShipVia> shipViaList = new ArrayList<Cimm2BCentralShipVia>();
		for (Cimm2BShippingMethod multipleShipVia : shipMethod) {
			Cimm2BCentralShipVia shipVia = new Cimm2BCentralShipVia();
			shipVia.setShipViaDescription(CommonUtility.validateString(multipleShipVia.getShipViaDescription()));
			shipVia.setShipViaCode(CommonUtility.validateString(multipleShipVia.getShipViaCode()));
			shipVia.setCarrierTrackingNumber(CommonUtility.validateString(multipleShipVia.getCarrierTrackingNumber()));
			shipVia.setShipViaErpId(CommonUtility.validateString(multipleShipVia.getShipViaErpId()));
			shipVia.setAccountNumber(CommonUtility.validateString(multipleShipVia.getAccountNumber()));
		shipViaList.add(shipVia);
		}
		return shipViaList;
	}
}