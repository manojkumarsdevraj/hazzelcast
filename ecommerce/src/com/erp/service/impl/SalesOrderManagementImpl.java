package com.erp.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.SalesOrderManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralProjectManagementInformation;
import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.cimmesb.client.ecomm.request.CimmOrderRequest;
import com.unilog.cimmesb.client.response.CimmOrder;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCResponse;
import com.unilog.database.CommonDBQuery;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.ProjectManagementModel;
import com.unilog.sales.SalesModel;
import com.unilog.utility.CommonUtility;

public class SalesOrderManagementImpl implements SalesOrderManagement{
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	

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

	public SalesModel submitOrderToERP(SalesOrderManagementModel salesOrderInput) {
		
		SalesModel salesOrderOutput = new SalesModel();
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		
		if(CommonUtility.validateString(salesOrderInput.getErpOrderType()).equalsIgnoreCase("SO") && !CommonUtility.validateString(erp).equalsIgnoreCase("eclipse") && (CommonDBQuery.getSystemParamtersList().get("DISABLE_CREATE_QUOTE_CART")).equalsIgnoreCase("N")){
			
			LinkedHashMap<String, Object> createQuoteInput = salesOrderInput.getSalesOrderInput();

			Class<?>[] paramObject = new Class[1];
			paramObject[0] = LinkedHashMap.class;
			
			Object[] arguments = new Object[1];
			arguments[0] = createQuoteInput;
			
			try{
				Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
				Object obj = cls.newInstance();
				Method method = cls.getDeclaredMethod("createQuote", paramObject);
				salesOrderOutput = (SalesModel) method.invoke(obj, arguments);
			}catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}else{
			
			//String parameter
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = SalesOrderManagementModel.class;
			
			Object[] arguments = new Object[1];
			arguments[0] = salesOrderInput;

			try
			{
				Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
				Object obj = cls.newInstance();
				Method method = cls.getDeclaredMethod("submitSalesOrder", paramObject);
				salesOrderOutput = (SalesModel) method.invoke(obj, arguments);
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		
		return salesOrderOutput;
	}


	public ArrayList<SalesModel> OpenOrders(SalesModel salesInputParameter) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		ArrayList<SalesModel> openOrderList = new ArrayList<SalesModel>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;


		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("OpenOrders", paramObject);
			openOrderList = (ArrayList<SalesModel>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return openOrderList;
	}


	public ArrayList<SalesModel> OrderHistory(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if((!CommonUtility.validateString(salesInputParameter.getErpOverrideFlag()).equalsIgnoreCase("N") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y")) || CommonUtility.validateString(salesInputParameter.getUserNote()).equalsIgnoreCase("Y")){
			  erp = "defaults";
		  }
		ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("OrderHistory", paramObject);
			orderList = (ArrayList<SalesModel>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return orderList;
	}
	
	public ArrayList<SalesModel> invoicedOrdersFromOrderHistory(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("invoicedOrdersFromOrderHistory", paramObject);
			orderList = (ArrayList<SalesModel>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return orderList;
	}
	public ArrayList<SalesModel> invoicedOrderDetailFromOrderHistory(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("invoicedOrderDetailFromOrderHistory", paramObject);
			orderList = (ArrayList<SalesModel>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return orderList;
	}
	
	public LinkedHashMap<String, ArrayList<SalesModel>> OrderDetail(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if((!CommonUtility.validateString(salesInputParameter.getErpOverrideFlag()).equalsIgnoreCase("N") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y")) || CommonUtility.validateString(salesInputParameter.getUserNote()).equalsIgnoreCase("Y")){
			  erp = "defaults";
		  }
		LinkedHashMap<String, ArrayList<SalesModel>> orderDetailInfo = new LinkedHashMap<String, ArrayList<SalesModel>>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("OrderDetail", paramObject);
			orderDetailInfo = (LinkedHashMap<String, ArrayList<SalesModel>>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return orderDetailInfo;
	}

	@Override
	public SalesModel createQuote(LinkedHashMap<String, Object> createQuoteInput) {
		
		

		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		SalesModel quoteDetailInfo = new SalesModel();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = LinkedHashMap.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = createQuoteInput;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("createQuote", paramObject);
			quoteDetailInfo = (SalesModel) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return quoteDetailInfo;
	
		
	}

	@Override
	public ArrayList<SalesModel> reorderPadInquiry(
			SalesModel salesInputParameter) {
		
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		ArrayList<SalesModel> salesOrderDeatilsByItems = new ArrayList<SalesModel>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("reorderPadInquiry", paramObject);
			salesOrderDeatilsByItems = (ArrayList<SalesModel>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return salesOrderDeatilsByItems;
	}
	
	public LinkedHashMap<String, Object> getTaxFromERP(LinkedHashMap<String, Object> createQuoteInput) {
		
		

		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		LinkedHashMap<String, Object> taxOutParameter = new LinkedHashMap<String, Object>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = LinkedHashMap.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = createQuoteInput;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getTaxFromERP", paramObject);
			taxOutParameter = (LinkedHashMap<String, Object>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return taxOutParameter;
	
		
	}
	public CreditCardModel creditCardPreAuthorization(CreditCardModel creditCardValue) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		CreditCardModel creditCardDetail = new CreditCardModel();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = CreditCardModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = creditCardValue;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("creditCardPreAuthorization", paramObject);
			creditCardDetail = (CreditCardModel) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return creditCardDetail;
	}

	public ArrayList<SalesModel> OpenOrdersInfoByDates(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		ArrayList<SalesModel> openOrderList = new ArrayList<SalesModel>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;


		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("OpenOrderInfoBydates", paramObject);
			openOrderList = (ArrayList<SalesModel>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return openOrderList;
	}
	@Override
	public SalesModel searchableAccountHistoryInquiry(
			SalesModel salesInputParameter) {
		
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		SalesModel salesOrderDeatilsByItems = new SalesModel();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("searchableAccountHistoryInquiry", paramObject);
			salesOrderDeatilsByItems = (SalesModel) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return salesOrderDeatilsByItems;
	}
	public SalesModel AuthorizeDotNetCardAuthentication(SalesModel salesInputParameter) {
		
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		SalesModel salesOutputParameter = new SalesModel();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("AuthorizeDotNetCardAuthentication", paramObject);
			salesOutputParameter = (SalesModel) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return salesOutputParameter;
	}
	public SalesModel getAuthorizeDotNetProfileData(SalesModel salesInputParameter) {
		
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		SalesModel salesOutputParameter = new SalesModel();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getAuthorizeDotNetProfileData", paramObject);
			salesOutputParameter = (SalesModel) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return salesOutputParameter;
	}
	@Override
	public SalesModel createPaypalPayment(SalesModel salesInputParameter) {
		
		
				String erp = "defaults";
				request =ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
					erp = session.getAttribute("erpType").toString().trim();
				}
				SalesModel salesOutputParameter = new SalesModel();
				
				//String parameter
				Class<?>[] paramObject = new Class[1];
				paramObject[0] = SalesModel.class;
				
				
				Object[] arguments = new Object[1];
				arguments[0] = salesInputParameter;
				
				try
				{
					Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
					Object obj = cls.newInstance();
					Method method = cls.getDeclaredMethod("createPaypalPayment", paramObject);
					salesOutputParameter = (SalesModel) method.invoke(obj, arguments);
				}
				catch (Exception e) {
					
					e.printStackTrace();
				}
				return salesOutputParameter;
	}
	
	public SalesModel AuthorizeDeleteCustomerPaymentProfile(SalesModel salesInputParameter) {
		
		
				String erp = "defaults";
				request =ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
					erp = session.getAttribute("erpType").toString().trim();
				}
				SalesModel salesOutputParameter = new SalesModel();
				
				//String parameter
				Class<?>[] paramObject = new Class[1];
				paramObject[0] = SalesModel.class;
				
				
				Object[] arguments = new Object[1];
				arguments[0] = salesInputParameter;
				
				try
				{
					Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
					Object obj = cls.newInstance();
					Method method = cls.getDeclaredMethod("AuthorizeDeleteCustomerPaymentProfile", paramObject);
					salesOutputParameter = (SalesModel) method.invoke(obj, arguments);
				}
				catch (Exception e) {
					
					e.printStackTrace();
				}
				return salesOutputParameter;
	}
	public SalesModel AuthorizeDotNetGetCustomerProfile(SalesModel salesInputParameter) {
		
		
				String erp = "defaults";
				request =ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
					erp = session.getAttribute("erpType").toString().trim();
				}
				SalesModel salesOutputParameter = new SalesModel();
				
				//String parameter
				Class<?>[] paramObject = new Class[1];
				paramObject[0] = SalesModel.class;
				
				
				Object[] arguments = new Object[1];
				arguments[0] = salesInputParameter;
				
				try
				{
					Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
					Object obj = cls.newInstance();
					Method method = cls.getDeclaredMethod("AuthorizeDotNetGetCustomerProfile", paramObject);
					salesOutputParameter = (SalesModel) method.invoke(obj, arguments);
				}
				catch (Exception e) {
					
					e.printStackTrace();
				}
				return salesOutputParameter;
	}
	public SalesModel AuthorizeDotNetSaveCardAuthentication(SalesModel salesInputParameter) {
		
		
				String erp = "defaults";
				request =ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
					erp = session.getAttribute("erpType").toString().trim();
				}
				SalesModel salesOutputParameter = new SalesModel();
				
				//String parameter
				Class<?>[] paramObject = new Class[1];
				paramObject[0] = SalesModel.class;
				
				
				Object[] arguments = new Object[1];
				arguments[0] = salesInputParameter;
				
				try
				{
					Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
					Object obj = cls.newInstance();
					Method method = cls.getDeclaredMethod("AuthorizeDotNetSaveCardAuthentication", paramObject);
					salesOutputParameter = (SalesModel) method.invoke(obj, arguments);
				}
				catch (Exception e) {
					
					e.printStackTrace();
				}
				return salesOutputParameter;
	}
	public SalesModel AuthorizeCustomerProfileAuthentication(SalesModel salesInputParameter) {
		
		
				String erp = "defaults";
				request =ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
					erp = session.getAttribute("erpType").toString().trim();
				}
				SalesModel salesOutputParameter = new SalesModel();
				
				//String parameter
				Class<?>[] paramObject = new Class[1];
				paramObject[0] = SalesModel.class;
				
				
				Object[] arguments = new Object[1];
				arguments[0] = salesInputParameter;
				
				try
				{
					Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
					Object obj = cls.newInstance();
					Method method = cls.getDeclaredMethod("AuthorizeCustomerProfileAuthentication", paramObject);
					salesOutputParameter = (SalesModel) method.invoke(obj, arguments);
				}
				catch (Exception e) {
					
					e.printStackTrace();
				}
				return salesOutputParameter;
	}
	public SalesModel AuthorizeDotNetCreateCustomerPaymentProfile(SalesModel salesInputParameter) {
		
		
				String erp = "defaults";
				request =ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
					erp = session.getAttribute("erpType").toString().trim();
				}
				SalesModel salesOutputParameter = new SalesModel();
				
				//String parameter
				Class<?>[] paramObject = new Class[1];
				paramObject[0] = SalesModel.class;
				
				
				Object[] arguments = new Object[1];
				arguments[0] = salesInputParameter;
				
				try
				{
					Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
					Object obj = cls.newInstance();
					Method method = cls.getDeclaredMethod("AuthorizeDotNetCreateCustomerPaymentProfile", paramObject);
					salesOutputParameter = (SalesModel) method.invoke(obj, arguments);
				}
				catch (Exception e) {
					
					e.printStackTrace();
				}
				return salesOutputParameter;
	}
	
	public Cimm2BCentralProjectManagementInformation ConstructionProjectManagement(ProjectManagementModel ProjectManagementParameter)
	 {
		String erp = "cimm2bcentral";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("cimm2bcentral"))
		{
			erp = session.getAttribute("erpType").toString().trim();
		}
		 Cimm2BCentralProjectManagementInformation managementList = new Cimm2BCentralProjectManagementInformation();
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = ProjectManagementModel.class;
		Object[] arguments = new Object[1];
		arguments[0] = ProjectManagementParameter;


		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("ConstructionProjectManagement", paramObject);
		     managementList = (Cimm2BCentralProjectManagementInformation)method.invoke(obj, arguments);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return managementList;
	
		
	}

	@Override
	public SalesModel createChargeLogicPayment(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		SalesModel salesOutputParameter = new SalesModel();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("createChargeLogicPayment", paramObject);
			salesOutputParameter = (SalesModel) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return salesOutputParameter;
	}
	@Override
	public LinkedHashMap<String, Object> getQuotes(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if((!CommonUtility.validateString(salesInputParameter.getErpOverrideFlag()).equalsIgnoreCase("N") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y")) || CommonUtility.validateString(salesInputParameter.getUserNote()).equalsIgnoreCase("Y")){
			  erp = "defaults";
		}
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;


		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("QuoteList", paramObject);
			contentObject = (LinkedHashMap<String, Object>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return contentObject;
	}

	@Override
	public LinkedHashMap<String, Object> getQuoteDetail(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if((!CommonUtility.validateString(salesInputParameter.getErpOverrideFlag()).equalsIgnoreCase("N") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y")) || CommonUtility.validateString(salesInputParameter.getUserNote()).equalsIgnoreCase("Y")){
			  erp = "defaults";
		}
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;


		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("QuoteDetail", paramObject);
			contentObject = (LinkedHashMap<String, Object>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return contentObject;
	}
	
	@Override
	public LinkedHashMap<String, Object> getTopProducts(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if((!CommonUtility.validateString(salesInputParameter.getErpOverrideFlag()).equalsIgnoreCase("N") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y")) || CommonUtility.validateString(salesInputParameter.getUserNote()).equalsIgnoreCase("Y")){
			  erp = "defaults";
		}
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;


		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getTopProducts", paramObject);
			contentObject =  (LinkedHashMap<String, Object>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return contentObject;
	}
	
	@Override
	public LinkedHashMap<String, Object> getTopCategories(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if((!CommonUtility.validateString(salesInputParameter.getErpOverrideFlag()).equalsIgnoreCase("N") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y")) || CommonUtility.validateString(salesInputParameter.getUserNote()).equalsIgnoreCase("Y")){
			  erp = "defaults";
		}
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;


		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getTopCategories", paramObject);
			contentObject =  (LinkedHashMap<String, Object>) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return contentObject;
	}
	public SalesModel createPaypalPaymentV2(SalesModel salesInputParameter) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//Override ERP. To override add "erpOverrideFlag" hidden field in the html
		if(CommonUtility.validateString(salesInputParameter.getErpOverrideFlag()).length()>0){
			erp = CommonUtility.validateString(salesInputParameter.getErpOverrideFlag());
		}
		SalesModel salesOutputParameter = new SalesModel();

		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;

		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;

		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("createPaypalPaymentV2", paramObject);
			salesOutputParameter = (SalesModel) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return salesOutputParameter;
	}

	public String getInvoiceRecall(SalesModel salesInputParameter) {

		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		String invoiceDetailResponse = new String();
		
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = salesInputParameter;
		
		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("invoiceDetailPrint", paramObject);
			invoiceDetailResponse = (String) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return invoiceDetailResponse;
	}
	public SalesModel submitEventOrderToERP(SalesOrderManagementModel salesOrderInput) {
		
		SalesModel salesOrderOutput = new SalesModel();
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
			
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = SalesOrderManagementModel.class;
		
		Object[] arguments = new Object[1];
		arguments[0] = salesOrderInput;

		try
		{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("eventRegistrationOrder", paramObject);
			salesOrderOutput = (SalesModel) method.invoke(obj, arguments);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return salesOrderOutput;
	}
	public SalesModel getmaxRecallInformation(SalesModel salesOrderInput) {
		// TODO Auto-generated method stub
		SalesModel salesOrderOutput = new SalesModel();
		
		String erp = "cimm2bcentral";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
			
		Object[] arguments = new Object[1];
		arguments[0] = salesOrderInput;
			
		try{
			if(CommonUtility.validateString((String)session.getAttribute("erpType")).equalsIgnoreCase(erp)){
				Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
				Object obj = cls.newInstance();
				Method method = cls.getDeclaredMethod("getMaxRecallInformation", paramObject);
				salesOrderOutput = (SalesModel) method.invoke(obj, arguments);
			}
		}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
		}
			
		return salesOrderOutput;

	}
	
	public SalesModel getmaxRecallInfoByDocId(SalesModel salesOrderInput) {
		SalesModel salesOrderOutput = new SalesModel();
		String erp = "cimm2bcentral";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = SalesModel.class;
		Object[] arguments = new Object[1];
		arguments[0] = salesOrderInput;
			
		try{
			if(CommonUtility.validateString((String)session.getAttribute("erpType")).equalsIgnoreCase(erp)){
				Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
				Object obj = cls.newInstance();
				Method method = cls.getDeclaredMethod("getMaxRecallInfoByDocId", paramObject);
				salesOrderOutput = (SalesModel) method.invoke(obj, arguments);
			}
		}catch (Exception e) {
				e.printStackTrace();
		}
		return salesOrderOutput;
	}

	@Override
	public Cimm2BCResponse<CimmOrder> validateCoupons(CimmOrderRequest orderRequest, HttpSession session, String erpType) {
		Cimm2BCResponse<CimmOrder> erpResponse = new  Cimm2BCResponse<CimmOrder>();
		String erp = "defaults";
		if(CommonUtility.validateString((String) session.getAttribute("erpType")).length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if (CommonUtility.validateString(erpType).length()>0) {
			erp = CommonUtility.validateString(erpType);
		} 
		Class<?>[] paramObject = new Class[2];
		paramObject[0] = CimmOrderRequest.class;
		paramObject[1] = HttpSession.class;
		
		Object[] arguments = new Object[2];
		arguments[0] = orderRequest;
		arguments[1] = session;
		
		try {
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.SalesOrderManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("validateCoupons", paramObject);
			erpResponse =  (Cimm2BCResponse<CimmOrder>) method.invoke(obj, arguments);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return erpResponse;
	}

}