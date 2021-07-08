package com.unilog.misc;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.ibm.wsdl.util.IOUtils;
import com.unilog.database.CommonDBQuery;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

public class YahooBossSupport {

	protected static String yahooServer = CommonDBQuery.getSystemParamtersList().get("GEO_API_URL");
	private static String consumer_key = CommonDBQuery.getSystemParamtersList().get("GEO_API_KEY");
	private static String consumer_secret = CommonDBQuery.getSystemParamtersList().get("GEO_API_SECRET");
	private static StHTTPRequest httpRequest = new StHTTPRequest();
	private static final String ENCODE_FORMAT = "UTF-8";
	private static final String callType = "placefinder";
	private static final int HTTP_STATUS_OK = 200;
	private static String longitude;
	private static String latitude;

	
	public UsersModel locateUser(UsersModel locationDetail){
		try {
			YahooBossSupport signPostTest = new YahooBossSupport();
			locationDetail = signPostTest.returnHttpData(locationDetail);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return locationDetail;
	}
	public UsersModel returnHttpData(UsersModel locationDetail)  throws UnsupportedEncodingException, Exception{
		if(this.isConsumerKeyExists() && this.isConsumerSecretExists()) {
			String params = callType;
			if(CommonUtility.validateString(locationDetail.getZipCode()).length()>0){
				params = params.concat("?postal=");
				params = params.concat(URLEncoder.encode(locationDetail.getZipCode(), ENCODE_FORMAT));
			}
			if(CommonUtility.validateString(locationDetail.getCity()).length()>0){
				//params = params.concat("&city=");
				//params = params.concat(URLEncoder.encode(locationDetail.getCity(), ENCODE_FORMAT));
			}
			if(CommonUtility.validateString(locationDetail.getState()).length()>0){
				//params = params.concat("&state=");
				//params = params.concat(URLEncoder.encode(locationDetail.getState(), ENCODE_FORMAT));
			}
			if(params!=null){
				String url = yahooServer + params;
				OAuthConsumer consumer = new DefaultOAuthConsumer(getConsumer_key(), consumer_secret);
				httpRequest.setOAuthConsumer(consumer);
				
				try {
					System.out.println("sending get request to" + URLDecoder.decode(url, ENCODE_FORMAT));
					locationDetail.setElementSetupUrl(url);
					locationDetail = httpRequest.sendGetRequest(locationDetail); 
					if(locationDetail.getResultCount() == HTTP_STATUS_OK) {
						System.out.println("Response ");
					} else {
						System.out.println("Error in response due to status code = " + locationDetail.getResultCount());
					}
					System.out.println(httpRequest.getResponseBody());
					
				} catch(UnsupportedEncodingException e) {
					System.out.println("Encoding/Decording error");
				} catch (IOException e) {
					System.out.println("Error with HTTP IO");
				} catch (Exception e) {
					System.out.printf(httpRequest.getResponseBody(), e);
				return null;
			}
		  }
		} else {
			System.out.println("Key/Secret does not exist");
		}
		return locationDetail;
	}
	public static String getLongitude() {
		return longitude;
	}

	public static void setLongitude(String longitude) {
		YahooBossSupport.longitude = longitude;
	}

	public static String getLatitude() {
		return latitude;
	}

	public static void setLatitude(String latitude) {
		YahooBossSupport.latitude = latitude;
	}
	private boolean isConsumerKeyExists() {
			if(getConsumer_key()!=null &&  !getConsumer_key().trim().equalsIgnoreCase("")) {
				return true;
			}
			return false;
	}
	private boolean isConsumerSecretExists() {
		if(consumer_secret!=null && !consumer_secret.trim().equalsIgnoreCase("")) {
			return true;
		}
		return false;
	}
	public static void setConsumer_key(String consumer_key) {
		YahooBossSupport.consumer_key = consumer_key;
	}
	public static String getConsumer_key() {
		return consumer_key;
	}
	public static String getYahooServer() {
		return yahooServer;
	}
	public static void setYahooServer(String yahooServer) {
		YahooBossSupport.yahooServer = yahooServer;
	}
	public static String getConsumer_secret() {
		return consumer_secret;
	}
	public static void setConsumer_secret(String consumer_secret) {
		YahooBossSupport.consumer_secret = consumer_secret;
	}
	
	
}
