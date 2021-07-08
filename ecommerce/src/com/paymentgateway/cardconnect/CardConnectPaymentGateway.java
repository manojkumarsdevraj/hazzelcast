package com.paymentgateway.cardconnect;
/*
Copyright 2014, CardConnect (http://www.cardconnect.com)

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
PERFORMANCE OF THIS SOFTWARE.
*/

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.paymentgateway.model.CreditCardManagementModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

/**
 * Client Example showing various service request calls to CardConnect using REST
 */
@SuppressWarnings("unchecked")
public class CardConnectPaymentGateway {
	private static final String ENDPOINT = CommonDBQuery.getSystemParamtersList().get("CARDCONNECT_URL");
	private static final String USERNAME = CommonDBQuery.getSystemParamtersList().get("CARDCONNECT_USERNAME");
	private static final String PASSWORD = CommonDBQuery.getSystemParamtersList().get("CARDCONNECT_PASSWORD");
	public static String merchantId = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_ACCEPTOR_ID");
	public static String paymentAccountType = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_TYPE");
	public static String ccCurrencyCode = CommonDBQuery.getSystemParamtersList().get("CARDCONNECT_CURRENCY");
	public static String ccCountry = CommonDBQuery.getSystemParamtersList().get("CARDCONNECT_COUNTRY");
	public static String autoCapture = CommonDBQuery.getSystemParamtersList().get("CARDCONNECT_PAYMENT_AUTO_CAPTURE");
	public static String tokenize = CommonDBQuery.getSystemParamtersList().get("CARDCONNECT_TOKENNIZE");
	public static String profile = CommonDBQuery.getSystemParamtersList().get("CARDCONNECT_PROFILE");
	
	public HashMap<String,String> authCaptureCardDetails(CreditCardManagementModel ccmodel) {
		HashMap<String,String> cardConnectResponse = new HashMap<String,String>();
		String authCode = null;
		try{
			// Send an Auth Transaction request
			cardConnectResponse = authTransaction(ccmodel);
			authCode = CommonUtility.validateString(cardConnectResponse.get("authcode"));
			
			if(cardConnectResponse.get("respstat").equalsIgnoreCase("A") && cardConnectResponse.get("resptext").contains("Appr") && CommonUtility.validateString(authCode).length()>0){
				if(CommonDBQuery.getSystemParamtersList().get("CARDCONNECT_CAPTURETRANSACTION").equalsIgnoreCase("Y")){
					cardConnectResponse = captureTransaction(cardConnectResponse.get("retref"),ccmodel);
				}
				return cardConnectResponse;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return cardConnectResponse;
	}
	
	
	/**
	 * Authorize Transaction REST Example
	 * @return
	 */
	public HashMap<String,String> authTransaction(CreditCardManagementModel ccmodel) {
		System.out.println("\nAuthorization Request");
		HashMap<String,String> cardConnectResponse = new HashMap<String,String>();
		try{
			// Create Authorization Transaction request
			JSONObject request = new JSONObject();
			request.put("merchid", ccmodel.getMerchantId());
			request.put("accttype", ccmodel.getPaymentAccountType());
			request.put("account", ccmodel.getCcToken());
			request.put("expiry", ccmodel.getCcExp());
			request.put("cvv2", ccmodel.getCcCvv2VrfyCode());
			request.put("amount", ccmodel.getCcAmount());
			request.put("currency", ccmodel.getCcCurrencyCode());
			request.put("orderid", ccmodel.getOrderNumber());
			request.put("name", ccmodel.getCardHolder());
			request.put("Street", ccmodel.getAddress1());
			request.put("city", ccmodel.getCity() );
			request.put("region",ccmodel.getState() );
			request.put("country", ccmodel.getCcCountry());
			request.put("postal", ccmodel.getZipCode());
			request.put("profile", profile);
			request.put("tokenize", tokenize);
			//request.put("capture", autoCapture);
			
			System.out.println("capture Value = " +autoCapture);
			
			// Handle request
			System.out.println("-------------------- Auth Transaction Request --------------------------");
			Set<String> requestKeys = request.keySet();
			for (String key : requestKeys){
				if(key == "cvv2") {
					System.out.println("cvv2 : *** ");
				}else {
					System.out.println(key + ": " + request.get(key));
				}
			}
			
			// Create the REST client
			CardConnectRestClient client = new CardConnectRestClient(ccmodel.getENDPOINT(), ccmodel.getUSERNAME(), ccmodel.getPASSWORD());
			
			// Send an AuthTransaction request
			JSONObject response = client.authorizeTransaction(request);
			
			// Handle response
			System.out.println("-------------------- Auth Transaction Response --------------------------");
			Set<String> keys = response.keySet();
			for (String key : keys){ 
				System.out.println(key + ": " + response.get(key));
				cardConnectResponse.put(key, (String) response.get(key));
			}
			return cardConnectResponse;
		}catch(Exception e){
			e.printStackTrace();
		}
		return cardConnectResponse;
	}
	
	/**
	 * Capture Transaction REST Example
	 * @param retref
	 */
	public HashMap<String,String> captureTransaction(String retref,CreditCardManagementModel ccmodel) {
		HashMap<String,String> cardConnectResponse = new HashMap<String,String>();
		System.out.println("\nCapture Transaction Request");
		
		// Create Authorization Transaction request
		JSONObject request = new JSONObject();
		// Merchant ID
		request.put("merchid", ccmodel.getMerchantId());
		// Transaction amount
		request.put("amount", ccmodel.getCcAmount());
		// Transaction currency
		request.put("currency", ccmodel.getCcCurrencyCode());
		// Order ID
		request.put("retref", retref);
		// Purchase Order Number
		request.put("ponumber",ccmodel.getOrderNumber() );
		// Tax Amount
		request.put("taxamnt","");
		// Ship From ZipCode
		request.put("shipfromzip", ccmodel.getZipCode());
		// Ship To Zip
		request.put("shiptozip", ccmodel.getZipCode());
		// Ship to County
		request.put("shiptocountry", ccmodel.getCcCountry());
		// Cardholder Zip-Code
		request.put("postal", ccmodel.getZipCode());
		
		// Line item details
		JSONArray items = new JSONArray();
		// Singe line item
		JSONObject item = new JSONObject();
		item.put("lineno", "");
		item.put("material", "");
		item.put("description", "");
		item.put("upc", "");
		item.put("quantity", "");
		item.put("uom", "");
		item.put("unitcost", "");
		items.add(item);
		// Add items to request
		request.put("items", items);
		
		// Authorization Code from auth response
		request.put("authcode", "");
		// Invoice ID
		request.put("invoiceid", "");
		// Order Date
		request.put("orderdate", "");
		// Total Order Freight Amount
		request.put("frtamnt", "");
		// Total Duty Amount
		request.put("dutyamnt", "");
		
		// Handle request
		System.out.println("-------------------- Capture Transaction Request --------------------------");
		Set<String> requestKeys = request.keySet();
		for (String key : requestKeys){
			if(key == "cvv2") {
				System.out.println("cvv2 : *** ");
			}else {
				System.out.println(key + ": " + request.get(key));
			}
		}
				
		// Create the CardConnect REST client
		CardConnectRestClient client = new CardConnectRestClient(ENDPOINT, USERNAME, PASSWORD);
		
		// Send a captureTransaction request
		JSONObject response = client.captureTransaction(request);
		
		// Handle response
		System.out.println("-------------------- Capture Transaction Response --------------------------");
		Set<String> keys = response.keySet();
		for (String key : keys){
			System.out.println(key + ": " + response.get(key));
			cardConnectResponse.put(key, (String) response.get(key));
		}
		return cardConnectResponse;
	}
	
}
