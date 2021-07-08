package com.erp.service.cimmesb.utils;

import java.util.HashMap;
import java.util.Map;

import com.unilog.cimmesb.client.ecomm.ECommRestClientProvider;
import com.unilog.cimmesb.client.ecomm.service.ECommerceTDFV1ServiceImpl;
import com.unilog.cimmesb.client.ecomm.service.IECommerceCSPV1Service;
import com.unilog.cimmesb.client.ecomm.service.IECommerceCustomerV1Service;
import com.unilog.cimmesb.client.ecomm.service.IECommerceExternalTokenV1Service;
import com.unilog.cimmesb.client.ecomm.service.IECommerceFreightService;
import com.unilog.cimmesb.client.ecomm.service.IECommerceOrdersV1Service;
import com.unilog.cimmesb.client.ecomm.service.IECommercePaymentGatewayV1Service;
import com.unilog.cimmesb.client.ecomm.service.IECommercePriceAndAvailabilityV1Service;
import com.unilog.cimmesb.client.ecomm.service.IECommerceTAXV1Service;
import com.unilog.cimmesb.client.ecomm.service.IECommerceTDFV1Service;
import com.unilog.cimmesb.client.ecomm.service.IECommerceUserV1Service;
import com.unilog.database.CommonDBQuery;

public class CimmESBServiceUtils {
	public static Map<String, String> esbConfigParams(){
		Map<String, String> parameters = new HashMap<>();
		parameters.put("ecommBaseUrl", CommonDBQuery.getSystemParamtersList().get("CIMMESB_ECOMM_BASE_URL"));
		parameters.put("aasBaseUrl", CommonDBQuery.getSystemParamtersList().get("CIMMESB_AAS_BASE_URL"));
		return parameters;
	}
	
	public static IECommercePriceAndAvailabilityV1Service getPriceEnquiryService() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getpriceAndAvailability();
	}
	
	public static IECommerceUserV1Service getUserService() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getUserV1Service();
	}
	
	public static IECommerceUserV1Service getUpdateUserService() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getUpdateUserV1Service();
	}
	
	public static IECommerceUserV1Service getChangePasswordUserService() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).changePasswordV1Service();
	}
	
	public static IECommerceCustomerV1Service getCustomerService() {	
		return ECommRestClientProvider.getInstance(esbConfigParams()).getCustomerV1Service();
	}
	
	public static IECommerceExternalTokenV1Service getExteranalToken() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getExternalToken();
	}
	
	public static IECommerceOrdersV1Service getOrderService() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getOrderV1Service();
	}
	
	public static IECommerceOrdersV1Service getOrderDetailService() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getOrdersV1Service();
	}
	public static IECommerceOrdersV1Service getCreateOrderService() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).createOrdersV1Service();
	}
	public static IECommercePaymentGatewayV1Service getPaymentGatewayV1Service() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getPaymentGatewayV1Service();
	}
	public static IECommerceFreightService getFreightService() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getFreightService();
	}
	public static IECommerceTDFV1Service getTdfTopItems() {
		return ECommerceTDFV1ServiceImpl.getInstance();
	}
	public static IECommerceTDFV1Service getTdfPurchasedItems() {
		return ECommerceTDFV1ServiceImpl.getInstance();
	}
	public static IECommerceTDFV1Service getTdfProducts() {
		return ECommerceTDFV1ServiceImpl.getInstance();
	}
	
	public static IECommerceCSPV1Service getlogoDetails() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getCspDesignDetails();
	}
	public static IECommerceTAXV1Service getTaxService() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getTaxV1Service();
	}
	public static IECommerceExternalTokenV1Service chatbotExternalToken() {
		return ECommRestClientProvider.getInstance(esbConfigParams()).getExternalToken();
	}
}
