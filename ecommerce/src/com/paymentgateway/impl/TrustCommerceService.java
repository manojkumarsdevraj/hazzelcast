package com.paymentgateway.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.unilog.database.CommonDBQuery;

public class TrustCommerceService {
	public static String generateToken() {
		String url = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_TOKEN_URL");
		List<NameValuePair> fields = new ArrayList<>();
		fields.add(new BasicNameValuePair("custid", CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_HOST_ID")));
		fields.add(new BasicNameValuePair("password", CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_HOST_PASSWORD")));  
		fields.add(new BasicNameValuePair("action", CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_TRANSACTION_TYPE")));
		fields.add(new BasicNameValuePair("returnurl", CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_RETURN_URL")));
		return httpConnectivity(url, fields);
	}
	public static String authorizePayment(String token) {
		String url = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_AUTHORIZATION_URL");
		List<NameValuePair> fields = new ArrayList<>();
		fields.add(new BasicNameValuePair("token", token));
		fields.add(new BasicNameValuePair("password", CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_HOST_PASSWORD"))); 
		return httpConnectivity(url, fields);
	}
	private static String httpConnectivity(String url, List<NameValuePair> fields){
		String responseStr = "";
		try{
			HttpClient httpClient = HttpClientBuilder
					.create()
					.setSslcontext(SSLContexts.custom().useProtocol("TLSv1.1").build())
					.build();
			
			UrlEncodedFormEntity urlFormEntity=new UrlEncodedFormEntity(fields,"UTF-8");
			
			HttpPost post = new HttpPost(url);
			post.setEntity(urlFormEntity);
			
			post.setHeader("Content-type", "application/x-www-form-urlencoded");
			
			HttpResponse response = httpClient.execute(post);
			
			responseStr = EntityUtils.toString(response.getEntity());
			System.out.println(responseStr);
			
		}
		catch(IOException ex){
			System.out.println("IO Exception while estabkishing UPS cnnection");
			ex.printStackTrace();
		} catch (KeyManagementException e) {
			System.out.println("Key management Exception while using custom TLSv1.1");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException Exception while using custom TLSv1.1");
			e.printStackTrace();
		}
		
		return responseStr;
	}
	public static Map<String, String> authStatusAsMap(String authResponse){
		Map<String, String> authDetailsAsMap = new HashMap<>();
		if(authResponse != null) {
			String[] authFields = authResponse.split("\n");
			for(String eachField : authFields) {
				String[] eachFieldInfo = eachField.split("=");
				if(eachFieldInfo != null && eachFieldInfo.length > 1) {
					authDetailsAsMap.put(eachFieldInfo[0], eachFieldInfo[1]);
				}
			}
		}
		return authDetailsAsMap;
	}
	
	public static void setCheckoutDetailsToSession(HttpSession session, HttpServletRequest request) {
		session.setAttribute("CCsavedGroupId", request.getParameter("savedGroupId"));
		session.setAttribute("CCshippingAccountNumber", request.getParameter("shippingAccountNumber"));
		session.setAttribute("CCrequiredDate", request.getParameter("requiredDate"));
		session.setAttribute("CCshippingInstruction", request.getParameter("shippingInstruction"));
		session.setAttribute("CCpoNumber", request.getParameter("poNumber"));
		session.setAttribute("CCupsFrieghtCharges", request.getParameter("upsFrieghtCharges"));
		session.setAttribute("CCshipVia", request.getParameter("shipVia"));
		session.setAttribute("CCshipViaDescription", request.getParameter("shipViaDescription"));
		session.setAttribute("CCpickUpLocationCode", request.getParameter("pickUpLocationCode"));
		session.setAttribute("CCdefaultShipToId", request.getParameter("defaultShipToId"));
		session.setAttribute("CCdefaultBillToId", request.getParameter("defaultBillToId"));
		session.setAttribute("CCexternalCartId", request.getParameter("externalCartId"));
		session.setAttribute("CCjurisdictionCode", request.getParameter("jurisdictionCode"));
		session.setAttribute("CCorderTax", request.getParameter("orderTax"));
		session.setAttribute("CCaddress1", request.getParameter("address1"));
		session.setAttribute("CCaddress2", request.getParameter("address2"));
		session.setAttribute("CCcity", request.getParameter("city"));
		session.setAttribute("CCstate", request.getParameter("state"));
		session.setAttribute("CCphoneNumber", request.getParameter("phoneNumber"));
		session.setAttribute("CCemail", request.getParameter("email"));
		session.setAttribute("CCcountry", request.getParameter("country"));
		session.setAttribute("CCzipCode", request.getParameter("zipCode"));
		session.setAttribute("CCfreightCode", request.getParameter("freightCode"));	
		session.setAttribute("CCshipBranchId", request.getParameter("shipBranchId"));	
		
	}
	
	public static void setRequestFromSession(HttpSession session, HttpServletRequest request) {
		request.setAttribute("savedGroupId", session.getAttribute("CCsavedGroupId"));
		request.setAttribute("shippingAccountNumber", session.getAttribute("CCshippingAccountNumber"));
		request.setAttribute("requiredDate", session.getAttribute("CCrequiredDate"));
		request.setAttribute("shippingInstruction", session.getAttribute("CCshippingInstruction"));
		request.setAttribute("poNumber", session.getAttribute("CCpoNumber"));
		request.setAttribute("upsFrieghtCharges", session.getAttribute("CCupsFrieghtCharges"));
		request.setAttribute("shipVia", session.getAttribute("CCshipVia"));
		request.setAttribute("shipViaDescription", session.getAttribute("CCshipViaDescription"));
		request.setAttribute("pickUpLocationCode", session.getAttribute("CCpickUpLocationCode"));
		request.setAttribute("defaultShipToId", session.getAttribute("CCdefaultShipToId"));
		request.setAttribute("defaultBillToId", session.getAttribute("CCdefaultBillToId"));
		request.setAttribute("externalCartId", session.getAttribute("CCexternalCartId"));
		request.setAttribute("jurisdictionCode", session.getAttribute("CCjurisdictionCode"));
		request.setAttribute("orderTax", session.getAttribute("CCorderTax"));
		request.setAttribute("address1", session.getAttribute("CCaddress1"));
		request.setAttribute("address2", session.getAttribute("CCaddress2"));
		request.setAttribute("city", session.getAttribute("CCcity"));
		request.setAttribute("state", session.getAttribute("CCstate"));
		request.setAttribute("phoneNumber", session.getAttribute("CCphoneNumber"));
		request.setAttribute("email", session.getAttribute("CCemail"));
		request.setAttribute("country", session.getAttribute("CCcountry"));
		request.setAttribute("zipCode", session.getAttribute("CCzipCode"));
		request.setAttribute("freightCode", session.getAttribute("CCfreightCode"));
		request.setAttribute("shipBranchId", session.getAttribute("CCshipBranchId"));
		
		session.removeAttribute("CCsavedGroupId");
		session.removeAttribute("CCshippingAccountNumber");
		session.removeAttribute("CCrequiredDate");
		session.removeAttribute("CCshippingInstruction");
		session.removeAttribute("CCpoNumber");
		session.removeAttribute("CCupsFrieghtCharges");
		session.removeAttribute("CCshipVia");
		session.removeAttribute("CCshipViaDescription");
		session.removeAttribute("CCpickUpLocationCode");
		session.removeAttribute("CCdefaultShipToId");
		session.removeAttribute("CCdefaultBillToId");
		session.removeAttribute("CCexternalCartId");
		session.removeAttribute("CCjurisdictionCode");
		session.removeAttribute("CCorderTax");
		session.removeAttribute("CCaddress1");
		session.removeAttribute("CCaddress2");
		session.removeAttribute("CCcity");
		session.removeAttribute("CCstate");
		session.removeAttribute("CCphoneNumber");
		session.removeAttribute("CCemail");
		session.removeAttribute("CCcountry");
		session.removeAttribute("CCzipCode");
		session.removeAttribute("CCfreightCode");
		session.removeAttribute("CCshipBranchId");
	}
}
