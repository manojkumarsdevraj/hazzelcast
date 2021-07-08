package com.erp.service.defaults.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsModel;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.SalesDAO;
import com.unilog.sales.SalesModel;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class SalesOrderManagementAction {
	
	public static SalesModel submitSalesOrder(SalesOrderManagementModel salesOrderInput) {
		
		SalesModel salesOrderDetailList = null;
		
		return salesOrderDetailList;

	}
	
	public static ArrayList<SalesModel> OpenOrders(SalesModel salesInputParameter) {
		ArrayList<SalesModel> openOrderList = new ArrayList<SalesModel>();
		try{
			openOrderList = SalesDAO.getOpenOrdersList(salesInputParameter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return openOrderList;
	}

	public static ArrayList<SalesModel> OrderHistory(SalesModel salesInputParameter) {
		ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
		try{
			orderList = SalesDAO.getOrdersHistory(salesInputParameter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return orderList;
	}
	
	public static LinkedHashMap<String, ArrayList<SalesModel>> OrderDetail(SalesModel salesInputParameter) {
		HttpServletRequest request =ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
        int userId = CommonUtility.validateNumber(sessionUserId);
		LinkedHashMap<String, ArrayList<SalesModel>> orderDetailInfo = new LinkedHashMap<String, ArrayList<SalesModel>>();
		try{
			ArrayList<SalesModel> orderDetail = new ArrayList<SalesModel>();
			SalesModel orderDetailModel = SalesDAO.getOrderDetailByID(salesInputParameter);
			orderDetail.add(orderDetailModel);
			ArrayList<SalesModel>orderItemList = SalesDAO.getOrderItemDetailByID(salesInputParameter);
			if(CommonUtility.customServiceUtility()!=null) {
				CommonUtility.customServiceUtility().getUserContactAddress(userId, session);
			}
			orderDetailInfo.put("OrderDetail", orderDetail);
			orderDetailInfo.put("OrderItemList", orderItemList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return orderDetailInfo;
	}
	
	@SuppressWarnings("unchecked")
	public static SalesModel createQuote(LinkedHashMap<String, Object> createQuoteInput)
	{
		SalesModel quoteInfo = new SalesModel();
		try
		{
			ArrayList<ProductsModel> itemList = (ArrayList<ProductsModel>) createQuoteInput.get("itemList");
			double totalAmount = 0;
			double freightAmount = 0;
			double taxAmount = 0;
			if(itemList!=null && itemList.size()>0){
				for(ProductsModel itemModel : itemList){
					totalAmount = totalAmount+itemModel.getTotal();
				}
			}
			quoteInfo.setFreight(freightAmount);
			quoteInfo.setSubtotal(totalAmount - taxAmount - freightAmount);
			quoteInfo.setTotal(totalAmount);
			quoteInfo.setTax(taxAmount);
			quoteInfo.setStatusDescription("Order Created Successfully");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return quoteInfo;
		
	}
public ArrayList<SalesModel> reorderPadInquiry(SalesModel salesInputParameter) {
		
		ArrayList<SalesModel> ReorderPadItemList = new ArrayList<SalesModel>();
		return ReorderPadItemList;
	}

public LinkedHashMap<String, Object> getTaxFromERP(LinkedHashMap<String, Object> salesInputParameter) {
	LinkedHashMap<String, Object> getTaxDetails = null;
	return getTaxDetails;
}
public static ArrayList<SalesModel> OpenOrderInfoBydates(SalesModel salesInputParameter) {
	return null;
}
public static SalesModel createPaypalPayment(SalesModel salesInputParameter) {
		return null;
}
public static CreditCardModel creditCardPreAuthorization(CreditCardModel creditCardValue) {
	return null;
}
public static SalesModel AuthorizeDotNetCardAuthentication(SalesModel salesInputParameter) {
	return null;
}
public static SalesModel getAuthorizeDotNetProfileData(SalesModel salesInputParameter) {
	return null;
}	
}
