package com.unilog.services.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerCard;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSalesOrderHistoryRequest;
import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class GearyPacificCustomServices implements UnilogFactoryInterface {
	private static UnilogFactoryInterface serviceProvider;
	private GearyPacificCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (GearyPacificCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new GearyPacificCustomServices();
				}
			}
		return serviceProvider;
	}    	
	@Override
	public void getBranchCode(Cimm2BCentralOrder order, String warehouseCode) {
		order.setBranchCode(warehouseCode);
	}

	@Override
	public void getAuthAmount(Cimm2BCentralCustomerCard customerCard, SalesOrderManagementModel salesOrderInput) {
		customerCard.setAuthAmount(salesOrderInput.getCCAmount());
	}

	@Override
	public String getBrandPrefix() {
		// Set brandPrefix value empty
		return "";
	}
	
	@Override
	public void configCartListValues(ProductsModel cartListVal, ResultSet rs) {
		try {
			cartListVal.setOverRidePriceRule(rs.getString("OVERRIDE_PRICE_RULE"));
			cartListVal.setPrice(rs.getDouble("PRICE"));
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void configureCreditCardDetails(Cimm2BCentralCustomerCard creditCardDetails) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREDIT_CARD_FLAG")).trim().length() > 0) {
			creditCardDetails.setCreditCardNumber(CommonDBQuery.getSystemParamtersList().get("CREDIT_CARD_FLAG"));
		}
		creditCardDetails.setAuthorizationNumber(null);
	}
	
	@Override
	public void getListPrice(ProductsModel itemModel, Cimm2BCentralLineItem lineItem) {
		if(itemModel.getUnitPrice()>0) {
			lineItem.setListPrice(itemModel.getUnitPrice());
		}else if(itemModel.getPrice()>0) {
			lineItem.setListPrice(itemModel.getPrice());
		}else {
			lineItem.setListPrice(0);
		}
	}
	
	@Override
	public void setDefaultWareHouseFromERP(UsersModel customerinfo, Cimm2BCentralCustomer customerDetails) {
		if(customerDetails.getDefaultBranchId()!=null && customerDetails.getDefaultBranchId().length()>0) {
			customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(customerDetails.getDefaultBranchId())));
			customerinfo.setWareHouseCodeStr(CommonUtility.validateString(customerDetails.getDefaultBranchId()));
		}else{
			customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR"))));
			customerinfo.setWareHouseCodeStr(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR")));	
		}
	}
	
	@Override
	public List<CustomTable> getPromotional_Image(){
		List<CustomTable> PromotionalTable  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","PROMOTIONAL_IMAGES");
		return PromotionalTable;
	}
	
	@Override
	public void setUserStatus(ArrayList<UsersModel> superUserStatus,UsersModel userDetails, LinkedHashMap<String, String> userRegisteration) {
		superUserStatus = UsersDAO.getSuperUserForAccount(userDetails.getAccountName());
		if(superUserStatus!=null && superUserStatus.size()>0) {
			userRegisteration.put("Status", "I");
			userDetails.setUserStatus("I");
		}
	}
	
	@Override
	public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
	{	
		String newsLetterCustomFiledValue="";
		if(userDetailsInput.getNewsLetterSub()!=null && userDetailsInput.getNewsLetterSub().equals("Y")){
			newsLetterCustomFiledValue="Y";
		}else{
			newsLetterCustomFiledValue="N";
		}
		UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
	}
	
	@Override	
	public void getItemLevelPrice(Cimm2BCentralItem item,ProductsModel product) {		
		double price=0.0;
		if(item!=null && item.getCustomerPrice()!=null) {
			if(item.getCustomerPrice() > 0) {
				price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(item.getCustomerPrice()));
			}
			product.setListPrice(item.getListPrice()!=null?CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(item.getListPrice())):0.0);
			product.setCustomerPrice(price);
			product.setUnitPrice(price);
			product.setPrice(price);
		}
	}
	
	@Override
	public String setFirstLoginTrueForInactiveUser() {
		return "Y";
	}
	
	@Override
	public void setOpenOrderSalesInputParams(SalesModel salesInputParameter, HttpServletRequest request) {
		int recordLimit = CommonUtility.validateNumber(request.getParameter("recordLimit"));
		String sortDirection = CommonUtility.validateString(request.getParameter("sortDirection"));
		String orderType = CommonUtility.validateString(request.getParameter("orderType"));
		salesInputParameter.setRecordLimit(recordLimit);
		salesInputParameter.setSortDirection(sortDirection);
		salesInputParameter.setOrderType(orderType);
	}
	
	@Override
	public String getOpenOrdersRequestParams(SalesModel salesInputParameter, String GET_OPEN_ORDERS) {
		GET_OPEN_ORDERS = "";
		int recordLimit = salesInputParameter.getRecordLimit();
		String sortDirection = salesInputParameter.getSortDirection();
		String orderType = salesInputParameter.getOrderType();
		String startDate = salesInputParameter.getStartDate();
		String endDate = salesInputParameter.getEndDate();
		GET_OPEN_ORDERS = "&" +Cimm2BCentralRequestParams.recordLimit + "=" +recordLimit + "&" + Cimm2BCentralRequestParams.startDate + "=" +startDate + "&" + Cimm2BCentralRequestParams.endDate + "=" + endDate+ "&" + Cimm2BCentralRequestParams.sortDirection +"=" + sortDirection + "&" +Cimm2BCentralRequestParams.orderType +"="+ orderType;
		return GET_OPEN_ORDERS;		
	}
	
	@Override
	public void setOrderHistorySalesInputParams(SalesModel salesInputParameter, HttpServletRequest request) {
		int recordLimit = CommonUtility.validateNumber(request.getParameter("recordLimit"));
		String sortDirection = CommonUtility.validateString(request.getParameter("sortDirection"));
		String orderType = CommonUtility.validateString(request.getParameter("orderType"));
		salesInputParameter.setRecordLimit(recordLimit);
		salesInputParameter.setSortDirection(sortDirection);
		salesInputParameter.setOrderType(orderType);
	}
	
	@Override
	public void getOrderHistoryRequestParams(SalesModel salesInputParameter, Cimm2BCentralSalesOrderHistoryRequest salesOrderHistoryRequest) {
		salesOrderHistoryRequest.setRecordLimit(salesInputParameter.getRecordLimit());
		salesOrderHistoryRequest.setSortDirection(salesInputParameter.getSortDirection()!=null?salesInputParameter.getSortDirection():"");
		salesOrderHistoryRequest.setOrderType(salesInputParameter.getOrderType()!=null?salesInputParameter.getOrderType():"");
	}
	
	@Override
	public void updateRoleForFirstUser(Integer userId,UsersModel userDetailsInput) {
		String userRole = "Ecomm Customer Super User";
		userDetailsInput.setUserRole(userRole);
		String roleName = UsersDAO.updateRoleToUser(Integer.toString(userId),userRole);
		System.out.println("Updated role for the user "+userId+" : " + roleName);
	}
	
	@Override
	public double calculatingOrderSubTotal(ArrayList<ProductsModel> itemDetailObject, double orderSubTotal) {
		orderSubTotal = 0.0;
		for(ProductsModel eachItem:itemDetailObject){
			eachItem.setNetPrice(eachItem.getPrice() * eachItem.getQty());
			orderSubTotal = orderSubTotal+eachItem.getNetPrice();
		}
		return orderSubTotal;
	}
	 
	@Override
	public double assigningTotal(double orderGrandTotal, double orderSubTotal, String orderTax, String orderFreight) {
		orderGrandTotal = orderSubTotal + CommonUtility.validateDoubleNumber(orderTax) + CommonUtility.validateDoubleNumber(orderFreight);
		return orderGrandTotal;
	}
	
	@Override
	public Cimm2BCentralAddress billAddressExist(Cimm2BCentralAddress address,Cimm2BCentralCustomer customerDetails){
		if(!CommonUtility.doesBillAddressExist(address)) {
			if(customerDetails.getCustomerLocations() != null && customerDetails.getCustomerLocations().size() > 0) {
				address = customerDetails.getCustomerLocations().get(0).getAddress();
			}
		}
		return address;
	}

}
