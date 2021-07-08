package com.erp.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.service.ProductManagement;
import com.erp.service.model.ProductManagementModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.products.model.AssociateItemsModel;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class ProductManagementImpl implements ProductManagement{
	private static final Logger logger = LoggerFactory.getLogger(ProductManagementImpl.class);
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

	public String getUserToken(String userName, String password) {
		return null;
	}
	
	
	public ArrayList<ProductsModel> priceInquiry(ProductManagementModel priceInquiryInput,ArrayList<ProductsModel> productList) {
		String erp = "defaults";
		HttpSession session = null;
		if(priceInquiryInput!=null && priceInquiryInput.getSession()!=null) {
			session = priceInquiryInput.getSession();
		}else {
			request =ServletActionContext.getRequest();
			session = request.getSession();
		}
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		ArrayList<ProductsModel> priceOutput = new ArrayList<ProductsModel>();
		try{
			priceInquiryInput.setSession(session);
			Class<?>[] paramObject = new Class[2];	
			paramObject[0] = ProductManagementModel.class;
			paramObject[1] = ArrayList.class;
			Object[] arguments = new Object[2];
			arguments[0] = priceInquiryInput;
			arguments[1] = productList;
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getPriceFromERP", paramObject);
			priceOutput = (ArrayList<ProductsModel>) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return priceOutput;
	}

	public String ajaxBasePriceAndLeadTime(ProductManagementModel priceInquiryInput) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		String jsonPriceOutput = "";
		try
		{
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = ProductManagementModel.class;
		
		
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("ajaxBasePriceAndLeadTimeFromErp", paramObject);
		jsonPriceOutput = (String) method.invoke(obj, priceInquiryInput);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return jsonPriceOutput;
	}
	public ArrayList<ProductsModel>  itemsMultipleUOMList(String partNumber) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		ArrayList<ProductsModel>  response = null;
		try
		{
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = String.class;
		
		
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("getItemMultipleUOMData", paramObject);
		response = (ArrayList<ProductsModel>) method.invoke(obj, partNumber);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return response;
	}
	
	public String ajaxPriceInquiry(ProductManagementModel priceInquiryInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP_SWITCH_AJAXPRICEINQUIRY")).length()>0) {
			erp = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP_SWITCH_AJAXPRICEINQUIRY"));
		}
		String jsonPriceOutput = "";
		try{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = ProductManagementModel.class;
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getAjaxPriceFromErp", paramObject);
			jsonPriceOutput = (String) method.invoke(obj, priceInquiryInput);
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return jsonPriceOutput;
	}

	
	
	public ArrayList<ProductsModel> customerPartNumberQuery(ProductManagementModel customerPartNumberInquiryInput) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RESTRICT_CPN_FUNCTIONALITY_TO_ERP")).equals("Y")) {
			erp = "defaults";
		}
		
		ArrayList<ProductsModel> customerPartNumberlist = new ArrayList<ProductsModel>();
		try
		{
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = ProductManagementModel.class;
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("customerPartNumberQuery", paramObject);
		customerPartNumberlist = (ArrayList<ProductsModel>) method.invoke(obj, customerPartNumberInquiryInput);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return customerPartNumberlist;
	}
	public ArrayList<ProductsModel> getAlternateItems(ProductManagementModel alternateItemsRequest) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		ArrayList<ProductsModel> alternateItems = new ArrayList<ProductsModel>();
		try
		{
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = ProductManagementModel.class;
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("getAlternateItems", paramObject);
		alternateItems = (ArrayList<ProductsModel>) method.invoke(obj, alternateItemsRequest);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return alternateItems;
	}
	
public ProductManagementModel customerPartNumberInquiry(ProductManagementModel customerPartNumberInquiryInput) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RESTRICT_CPN_FUNCTIONALITY_TO_ERP")).equals("Y")) {
			erp = "defaults";
		}
		ProductManagementModel customerPartNumberlist = new ProductManagementModel();
		try
		{
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = ProductManagementModel.class;
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("customerPartNumberInquiry", paramObject);
		customerPartNumberlist = (ProductManagementModel) method.invoke(obj, customerPartNumberInquiryInput);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return customerPartNumberlist;
	}

	public ProductManagementModel customerPartNumberCreate(ProductManagementModel customerPartNumberInquiryInput) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RESTRICT_CPN_FUNCTIONALITY_TO_ERP")).equals("Y")) {
			erp = "defaults";
		}
		
		ProductManagementModel customerPartNumberResponse = new ProductManagementModel();
		try
		{
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = ProductManagementModel.class;
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("customerPartNumberCreate", paramObject);
		customerPartNumberResponse = (ProductManagementModel) method.invoke(obj, customerPartNumberInquiryInput);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return customerPartNumberResponse;
	}

	@Override
	public ProductManagementModel customerPartNumberDelete(ProductManagementModel customerPartNumberInquiryInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RESTRICT_CPN_FUNCTIONALITY_TO_ERP")).equals("Y")) {
			erp = "defaults";
		}
		ProductManagementModel customerPartNumberResponse = new ProductManagementModel();
		try
		{
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = ProductManagementModel.class;
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("customerPartNumberDelete", paramObject);
		customerPartNumberResponse = (ProductManagementModel) method.invoke(obj, customerPartNumberInquiryInput);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return customerPartNumberResponse;
	}
	
	@Override
	public String branchAvailabile(String pns, String branch, String entityId,String userToken) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		String BranchAvailValues = "";
		try
		{
		Class<?>[] paramObject = new Class[4];	
		paramObject[0] = String.class;
		paramObject[1] = String.class;
		paramObject[2] = String.class;
		paramObject[3] = String.class;
		
		Object[] arguments = new Object[4];
		arguments[0] = pns;
		arguments[1] = branch;
		arguments[2] = entityId;
		arguments[3] = userToken;
		
		
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("branchAvailabile", paramObject);
		BranchAvailValues = (String) method.invoke(obj, arguments);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return BranchAvailValues;
	}
	public UsersModel getCustomerDetail(UsersModel usersDetails) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		UsersModel userDetails = new UsersModel();
		try
		{
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = UsersModel.class;
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("getCustomerDetail", paramObject);
		userDetails = (UsersModel) method.invoke(obj, usersDetails);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return userDetails;
	}
	
	public String getAllBranchAvailabilityForProductCode(ProductManagementModel priceInquiryInput) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		String jsonPriceOutput = "";
		try
		{
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = ProductManagementModel.class;
		
		
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("getAllBranchAvailabilityForProductCode", paramObject);
		jsonPriceOutput = (String) method.invoke(obj, priceInquiryInput);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return jsonPriceOutput;
	}
	@Override
	public LinkedHashMap<String, Object> getItemHistoryDetails(ProductsModel productParameters) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		Object[] arguments = new Object[1];
		arguments[0] = productParameters;

		try
		{
		Class<?>[] paramObject1 = new Class[1];	
		paramObject1[0] = ProductsModel.class;
		
		
		Class<?> cls = Class.forName("com.erp.service."+erp+".action.ProductManagementAction");
		Object obj = cls.newInstance();
		
		Method method = cls.getDeclaredMethod("getItemHistoryDetails", paramObject1);
		contentObject =  (LinkedHashMap<String, Object>) method.invoke(obj, productParameters);
		
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return contentObject;
	}

	public AssociateItemsModel associateItems(ProductManagementModel associateInputItems) {
		String erp = "defaults";
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if (session.getAttribute("erpType") != null && session.getAttribute("erpType").toString().trim().length() > 0) {
			erp = session.getAttribute("erpType").toString().trim();
		}
		AssociateItemsModel associateItemsList = new AssociateItemsModel();
		try {
			Class<?>[] paramObject = new Class[1];
			paramObject[0] = ProductManagementModel.class;
			Class<?> cls = Class.forName("com.erp.service." + erp + ".action.ProductManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("associateItems", paramObject);
			associateItemsList = (AssociateItemsModel) method.invoke(obj, associateInputItems);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return associateItemsList;
	}
}
