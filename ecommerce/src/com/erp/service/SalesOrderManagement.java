package com.erp.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralProjectManagementInformation;
import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.cimmesb.client.ecomm.request.CimmOrderRequest;
import com.unilog.cimmesb.client.response.CimmOrder;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCResponse;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.ProjectManagementModel;
import com.unilog.sales.SalesModel;

public interface SalesOrderManagement {

	public SalesModel submitOrderToERP(SalesOrderManagementModel salesOrderInput);
	
	public ArrayList<SalesModel> OpenOrders(SalesModel salesInputParameter);
	
	public ArrayList<SalesModel> OpenOrdersInfoByDates(SalesModel salesInputParameter);
	
	public ArrayList<SalesModel> OrderHistory(SalesModel salesInputParameter);
	
	public LinkedHashMap<String, ArrayList<SalesModel>> OrderDetail(SalesModel salesInputParameter);
	
	public SalesModel createQuote(LinkedHashMap<String, Object> createQuoteInput);
	
	public ArrayList<SalesModel> reorderPadInquiry(SalesModel salesInputParameter);
	
	public LinkedHashMap<String, Object> getTaxFromERP(LinkedHashMap<String, Object> createQuoteInput);
	
	public ArrayList<SalesModel> invoicedOrdersFromOrderHistory(SalesModel salesInputParameter);
	
	public ArrayList<SalesModel> invoicedOrderDetailFromOrderHistory(SalesModel salesInputParameter);
	
	public SalesModel searchableAccountHistoryInquiry(SalesModel salesInputParameter);

	public CreditCardModel creditCardPreAuthorization(CreditCardModel creditCardValue) ;
	
	public SalesModel AuthorizeDotNetCardAuthentication(SalesModel salesInputParameter);
	
	public SalesModel getAuthorizeDotNetProfileData(SalesModel salesInputParameter);
	
	public SalesModel createPaypalPayment(SalesModel salesInputParameter);
	
	public SalesModel AuthorizeDotNetSaveCardAuthentication(SalesModel salesInputParameter);
	
	public SalesModel AuthorizeDotNetGetCustomerProfile(SalesModel salesInputParameter); 
	
	public SalesModel AuthorizeDeleteCustomerPaymentProfile(SalesModel salesInputParameter);
	
	public SalesModel AuthorizeCustomerProfileAuthentication(SalesModel salesInputParameter); 
	
	public SalesModel AuthorizeDotNetCreateCustomerPaymentProfile(SalesModel salesInputParameter);
	
	public Cimm2BCentralProjectManagementInformation ConstructionProjectManagement(ProjectManagementModel ProjectManagementParameter);

	public SalesModel createChargeLogicPayment(SalesModel salesInputParameter);
	
	public LinkedHashMap<String, Object> getQuotes(SalesModel salesInputParameter);

	public LinkedHashMap<String, Object> getQuoteDetail(SalesModel salesInputParameter);
	
	public LinkedHashMap<String, Object> getTopProducts(SalesModel salesInputParameter);
	
	public LinkedHashMap<String, Object> getTopCategories(SalesModel salesInputParameter);
	
	public SalesModel createPaypalPaymentV2(SalesModel salesInputParameter);
	
	public String getInvoiceRecall(SalesModel salesInputParameter);

	public SalesModel submitEventOrderToERP(SalesOrderManagementModel salesOrderInput);

	public SalesModel getmaxRecallInformation(SalesModel salesInputParameter);

	public SalesModel getmaxRecallInfoByDocId(SalesModel salesInputParameter);

	public Cimm2BCResponse<CimmOrder> validateCoupons(CimmOrderRequest orderRequest, HttpSession session, String erp);

}
