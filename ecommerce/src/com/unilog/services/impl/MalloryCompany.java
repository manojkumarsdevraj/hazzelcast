package com.unilog.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralContracts;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralGetInvoiceList;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSalesOrderHistoryRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BPriceAndAvailabilityRequest;
import com.erp.service.cimmesb.action.UserManagementAction;
import com.erp.service.model.ProductManagementModel;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class MalloryCompany implements UnilogFactoryInterface{
	static final Logger logger = Logger.getLogger(MalloryCompany.class);
	private static UnilogFactoryInterface serviceProvider;
	private MalloryCompany() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (MalloryCompany.class) {
				if(serviceProvider == null) {
					serviceProvider = new MalloryCompany();
				}
			}
		return serviceProvider;
	}
	
	@Override
	public boolean doTwoStepValidation(Cimm2BCentralCustomer customerDetails, String invoiceNo, String poNumber,
			String zipCode) {
		
		boolean validInvoice = false;
		try {
		ArrayList<Cimm2BCentralGetInvoiceList> cimm2BInvoices = customerDetails.getCimm2BInvoices();
		if(cimm2BInvoices!=null && cimm2BInvoices.size()>0){
			for(Cimm2BCentralGetInvoiceList invoiceList : cimm2BInvoices){
				UsersModel invoices = new UsersModel();
				invoices.setInvoiceNumber(invoiceList.getInvoiceNumber());
				invoices.setPoNumber(invoiceList.getPoNumber());
				if((CommonUtility.validateString(invoiceNo).trim().length() > 0 && invoiceNo.equalsIgnoreCase(invoices.getInvoiceNumber())) || (CommonUtility.validateString(poNumber).trim().length() > 0 && poNumber.equalsIgnoreCase(invoices.getPoNumber()))){
					validInvoice = true;
					break;
				}
			}
		}
		if(!validInvoice){
			Cimm2BCentralAddress address = customerDetails.getAddress();
			if((address != null && CommonUtility.validateString(zipCode).trim().length()>0 && CommonUtility.validateString(address.getZipCode()).trim().length()>0 && ((CommonUtility.validateString(address.getZipCode()).trim().length()> 5 && CommonUtility.validateString(address.getZipCode()).trim().substring(0,5).equalsIgnoreCase(zipCode)) || CommonUtility.validateString(address.getZipCode()).trim().equalsIgnoreCase(zipCode)))) {
				validInvoice = true;
			}
			
		}
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return validInvoice;
	}
	
	//Mallory
		public Cimm2BPriceAndAvailabilityRequest priceAndAvailIncludeUomList(Cimm2BPriceAndAvailabilityRequest priceAndAvailabilityRequest,ProductManagementModel priceInquiryInput) {
			try {
				if(!CommonUtility.validateString(priceInquiryInput.getRequestFrom()).equalsIgnoreCase("N")) {
					priceAndAvailabilityRequest.setIncludeProductUOMlist(true);
				}
				if(CommonUtility.validateString(priceInquiryInput.getAllBranchavailabilityRequired()).equalsIgnoreCase("Y")) {
					priceAndAvailabilityRequest.setIncludeAllBranchAvailability(true);
					Set<String> allWarehouseCodes = new HashSet<String>();
					allWarehouseCodes.add(priceAndAvailabilityRequest.getPricingWarehouseCode());
					priceAndAvailabilityRequest.setWarehouseCodes(allWarehouseCodes);
				}
			}catch(Exception e) {
				logger.error(e.getMessage());
			}
			return priceAndAvailabilityRequest;
		}
		
		@Override
		public String setFirstLoginTrueForInactiveUser() {
			return "Y";
		}
		
		@Override
		public void setContractIdToSession(Cimm2BCentralCustomer customerDetails, HttpSession session) {
			try {
			for(Cimm2BCentralContracts contracts : customerDetails.getContracts()){
				if(contracts != null && CommonUtility.validateString(contracts.getContractUID()).length() > 0 && session != null && session.getAttribute("localeCode")!= null && CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.contractId.PoNumber")).length()>0 && !CommonUtility.validateString(contracts.getCustomerPoNumber()).equalsIgnoreCase(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.contractId.PoNumber")))) {
					session.setAttribute("userContractId", CommonUtility.validateString(contracts.getContractUID()));
					break;
				}
			}
			}catch(Exception e) {
				logger.error(e.getMessage());
			}
		}
		
	@Override
	public void insertCustomFields(String brabchid, int userid, int buyingCompanyid, UsersModel userDetailsInput) {
		String newsLetterCustomFiledValue = "";
		try {
			if (CommonUtility.validateString(userDetailsInput.getNewsLetterSub()).equalsIgnoreCase("Y")) {
				newsLetterCustomFiledValue = "Y";
			} else {
				newsLetterCustomFiledValue = "N";
			}
			UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue), "NEWSLETTER", userid,
					buyingCompanyid, "USER");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public void setOrderNotes(Cimm2BCentralOrder orderRequest) {
		try {
			String orderNote = CommonUtility.validateString(orderRequest.getOrderNotes()) + " WebReferenceNumber -" + CommonUtility.validateParseIntegerToString(orderRequest.getUniqueWebReferenceNumber());
			orderRequest.setOrderNotes(orderNote);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public void setOrderHistorySalesInputParams(SalesModel salesInputParameter, HttpServletRequest request) {
		try {
			salesInputParameter.setSortDirection(CommonUtility.validateString(request.getParameter("sortDirection")).length()>0?CommonUtility.validateString(request.getParameter("sortDirection")):"desc");
			salesInputParameter.setRowDetails(CommonUtility.convertToBoolean(request.getParameter("rowDetails"))?CommonUtility.convertToBoolean(request.getParameter("rowDetails")):false);
			salesInputParameter.setPageSize(CommonUtility.validateNumber(request.getParameter("pageSize"))>0?CommonUtility.validateNumber(request.getParameter("pageSize")):10);
			salesInputParameter.setStartRowId(CommonUtility.validateString(request.getParameter("startRowId")).length()>0?CommonUtility.validateString(request.getParameter("startRowId")):"");
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public void getOrderHistoryRequestParams(SalesModel salesInputParameter, Cimm2BCentralSalesOrderHistoryRequest salesOrderHistoryRequest) {
		try {
			salesOrderHistoryRequest.setSortDirection(salesInputParameter.getSortDirection());
			salesOrderHistoryRequest.setPageSize(salesInputParameter.getPageSize());
			salesOrderHistoryRequest.setRowDetails(salesInputParameter.isRowDetails());
			salesOrderHistoryRequest.setStartRowId(salesInputParameter.getStartRowId());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	@Override
	public void assignEntityIdForShip(UsersModel usersModel, HttpSession session) {
		try {
			String userToken = (String) session.getAttribute("userToken");
			if(CommonUtility.validateString(usersModel.getShipToId()).length()==0) {
				usersModel.setShipToId(userToken);
			}	
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public String getModifiedOrderMailSubject(String orderMailSubject, SalesModel orderDetail, String mailSubject) {
		try {
			if(orderDetail!= null && orderDetail.getErpOrderNumber()!=null && orderDetail.getOrderNum()!=null && CommonUtility.validateString(orderDetail.getErpOrderNumber()).length()>0 && CommonUtility.validateString(orderDetail.getOrderNum()).length()>0 && CommonUtility.validateString(orderDetail.getErpOrderNumber()).equalsIgnoreCase(CommonUtility.validateString(orderDetail.getOrderNum()))) {
				orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+mailSubject+" - #"+orderDetail.getUniqueWebReferenceNumber(); 
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return orderMailSubject;
	}
}
