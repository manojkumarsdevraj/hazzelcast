package com.unilog.cmsmanagement.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import javax.ws.rs.HttpMethod;
import com.unilog.cimmesb.client.request.HttpMethod;
import org.apache.http.entity.StringEntity;
import javax.ws.rs.core.MediaType;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralStatus;
import com.erp.service.rest.util.HttpConnectionUtility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unilog.database.CommonDBQuery;
import com.unilog.exception.RestServiceException;
import com.unilog.products.ProductsModel;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;


public class Cimm2BCentralClient{

	private static Cimm2BCentralClient Cimm2BCentralClient;
	private String WEB_ADDRESS;
	private String CIMM2BC_URL;
    public static String CLIENT_ID = "ClientId";
	public static String SITE_ID = "SiteId";
	public static String siteId;
	public static String clientId;
	private boolean reInitializeClient = true;
	Gson gson = new Gson();
	HttpResponse response;

	public void initializeClient(){
		WEB_ADDRESS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
		CIMM2BC_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BCENTRAL_ACCESS_URL"));
		clientId = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CLEINT_ID"));
		siteId = CommonUtility.validateParseIntegerToString(CommonDBQuery.getGlobalSiteId());
		reInitializeClient = false;
	}

	public static Cimm2BCentralClient getInstance() {
		if(Cimm2BCentralClient==null) {
			Cimm2BCentralClient = new Cimm2BCentralClient();				
		}
		return Cimm2BCentralClient;
	}

	public Cimm2BCentralResponseEntity getDataObject(String url, String requestType, Object requestEntity, Class<?> dataClass,int siteId)
	{
		if(reInitializeClient){
			initializeClient();
		}
		Cimm2BCentralResponseEntity responseEntity = null;

		try
		{
			if(CommonUtility.validateString(CIMM2BC_URL).length()>0){
				url = CIMM2BC_URL+url;
			}else{
				url = WEB_ADDRESS+url;
			}
			
			String result = getJsonResponse(url, requestType, requestEntity,siteId);
			
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
					for (int i = 0; i < dataArray.size(); i++) {
						dataArrayList.add(gson.fromJson(dataArray.get(i).getAsJsonObject(), dataClass));
					}
					data = dataArrayList;
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
		return responseEntity;
	}

	public String getJsonResponse(String requestUrl, String requestType, Object requestParameters,int siteId) throws RestServiceException, IOException{
		String result = "";


		try {
			System.out.println("Request url : " + requestUrl);
			HttpConnectionUtility httpConnectionUtility = HttpConnectionUtility.getInstance();

			List<NameValuePair> headerList = new ArrayList<NameValuePair>();
			headerList.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, CommonDBQuery.getCimm2bCentralAuthorization()));
			headerList.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
			if(siteId > 0){
				headerList.add(new BasicNameValuePair(Cimm2BCentralClient.SITE_ID, CommonUtility.validateParseIntegerToString(siteId)));
			}
			
			headerList.add(new BasicNameValuePair(Cimm2BCentralClient.CLIENT_ID, clientId));

			StringEntity stringEntity=null;
			if (requestParameters != null && (requestType.equalsIgnoreCase(HttpMethod.POST.name()) || (requestType.equalsIgnoreCase(HttpMethod.PUT.name())))) {

				stringEntity = new StringEntity(gson.toJson(requestParameters), "UTF-8");
				System.out.println("RequestEntityParameters --- " + gson.toJson(requestParameters));
			}
			result = httpConnectionUtility.getApiResponse(requestUrl, requestType, headerList, stringEntity);

			/*int responseCode = response.getStatusLine().getStatusCode();
			result = EntityUtils.toString(response.getEntity()).trim();
			System.out.println("Response Code : " + responseCode);
			System.out.println("Response for '" + requestUrl + "' --- "+ result);*/
		}
		catch (Exception e) {
			throw new RestServiceException(e.getMessage());
		}

		return result;
	}
	
	

}
