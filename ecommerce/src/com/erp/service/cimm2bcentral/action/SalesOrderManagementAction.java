package com.erp.service.cimm2bcentral.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.http.HttpStatus;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCreditCardDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceSummaryRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthorizeCreditCardRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthorizeCreditCardResponse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthorizeCustomerPaymentProfileResponse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthorizeGetCustomerProfileResponse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthorizeSaveCreditCardResponse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthorizedSaveCreditCardRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralBillAndShipToContact;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerCard;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralDocumentDetail;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralGetInvoiceList;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralGetInvoiceRecallRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralGetInvoiceRecallResponse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralManagementBody;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralManagementHeader;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOpenOrderInformation;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOpenOrderInformationDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPaypalItemList;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPaypalRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPaypalResponse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralProfileList;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralProjectManagementInformation;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSalesOrderHistory;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSalesOrderHistoryAdditionalData;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSalesOrderHistoryDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSalesOrderHistoryRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipVia;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralUser;
import com.erp.service.cimm2bcentral.utilities.Cimm2BOpenOrdersRequest;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.SalesOrderManagementModel;
import com.google.gson.Gson;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.avalara.AvalaraUtility;
import com.unilog.avalara.model.TaxResponse;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.logocustomization.CustomizationCharges;
import com.unilog.logocustomization.LogoCustomization;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.ProjectManagementModel;
import com.unilog.sales.SalesAction;
import com.unilog.sales.SalesDAO;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralMaxRecallRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralMaxRecallResponse;

public class SalesOrderManagementAction {
	static final Logger logger = LoggerFactory.getLogger(SalesOrderManagementAction.class);
	public static SalesModel submitSalesOrder(SalesOrderManagementModel salesOrderInput) {

		SalesModel orderDetail = new SalesModel();
		Connection conn = null;
		try
		{	
			HttpSession session = salesOrderInput.getSession();
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PREFIX_ZEROS_TO_WAREHOUSE_CODE")).equals("Y")) {
				wareHousecode = CommonUtility.prefixWarehouseCode(wareHousecode);
			}
			
			String salesLocationId = (String) session.getAttribute("salesLocationId");
			String customerErpId ="";
			if(CommonUtility.validateString((String) session.getAttribute("customerId")).length()>0){
				customerErpId =(String) session.getAttribute("customerId");
			}else if(CommonUtility.validateString((String) session.getAttribute("billingEntityId")).length()>0){
				customerErpId =(String) session.getAttribute("billingEntityId");
			}
			String orderNumber = "";
			int backOrderSeqNumber = 0;
			double subTotal=0.0;
			String handlingListItems = "";
			String deliveryListItems = "";
			String errorMessageToDisplay = "";
			ArrayList<Cimm2BCentralLineItem> lineItems = new ArrayList<Cimm2BCentralLineItem>();
			SalesModel quoteResponse = salesOrderInput.getQuoteResponse();
			Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    String orderDate = sdf.format(cal.getTime());
			LinkedHashMap<String, Cimm2BCentralShipVia> shipMap = new LinkedHashMap<String, Cimm2BCentralShipVia>();
			ArrayList<ProductsModel> orderDetails = SalesDAO.getOrderDetails(salesOrderInput.getSession(), salesOrderInput.getOrderId());
			/**
			 * Below code written for Electrozad Event PO Payment
			 */
			if(CommonUtility.customServiceUtility() != null) {
				if((salesOrderInput.getPartNumberForEventpayment() != null) && (salesOrderInput.getPartNumberForEventpayment().length() >0)) {
					orderDetails = CommonUtility.customServiceUtility().getEventOrderDetails(salesOrderInput);// Electrozad Custom Service
				}
			}
			 if(CommonDBQuery.getSystemParamtersList().get("ORDER_SUBMIT_V2") !=null &&CommonDBQuery.getSystemParamtersList().get("ORDER_SUBMIT_V2").equalsIgnoreCase("Y")){
				orderDetails = salesOrderInput.getOrderItems();
			 }
			if(salesOrderInput.getQuoteNumber()!=null && quoteResponse != null){
				int i =1;
				ArrayList<SalesModel> quoteResponseOrderList = quoteResponse.getOrderList();
				if(quoteResponseOrderList != null && quoteResponseOrderList.size() > 0){
					for(SalesModel item : quoteResponseOrderList){
						Cimm2BCentralLineItem cimm2bCentralLineItem = new Cimm2BCentralLineItem();
						cimm2bCentralLineItem.setLineNumber(item.getLineNumber());
						cimm2bCentralLineItem.setPartNumber(item.getPartNumber());
						cimm2bCentralLineItem.setQty(item.getQtyordered());
						cimm2bCentralLineItem.setLineIdentifier(i++);
						cimm2bCentralLineItem.setUom(item.getUom() != null ? item.getUom().trim() : "");
						cimm2bCentralLineItem.setManufacturer(item.getManufacturer());
						cimm2bCentralLineItem.setShortDescription(item.getShortDesc()!=null?item.getShortDesc():".");
						cimm2bCentralLineItem.setLineItemComment(item.getLineItemComment());
						cimm2bCentralLineItem.setPage_title(item.getPageTitle());
						if(salesOrderInput.getTaxExempt()!=null && salesOrderInput.getTaxExempt().equalsIgnoreCase("N")){
							cimm2bCentralLineItem.setCalculateTax(true);
						}else{
							cimm2bCentralLineItem.setCalculateTax(false);
						}
						if(CommonUtility.validateString(salesLocationId).length()>0 && CommonUtility.validateString(salesLocationId)!="0"){
							cimm2bCentralLineItem.setShippingBranch(salesLocationId);
						}else{
							cimm2bCentralLineItem.setShippingBranch(wareHousecode);	
						}
						if(CommonUtility.customServiceUtility()!=null) {
							CommonUtility.customServiceUtility().addDefaultWarehouseToItems(cimm2bCentralLineItem,session,salesOrderInput,wareHousecode,salesLocationId);
						}
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
							cimm2bCentralLineItem.setShipMethod(item.getMultipleShipVia());
							if(item.getMultipleShipViaDesc().split(",").length > 1){
								cimm2bCentralLineItem.setShippingBranch(item.getMultipleShipViaDesc().split(",")[1]);
							}else{
								if(CommonUtility.validateString(salesLocationId).length()>0){
									cimm2bCentralLineItem.setShippingBranch(salesLocationId);
								}else{
									cimm2bCentralLineItem.setShippingBranch(wareHousecode);
								}
							}
						}
						lineItems.add(cimm2bCentralLineItem);
					}
					//Discount Available
					if(CommonUtility.customServiceUtility()!=null) {
						CommonUtility.customServiceUtility().addDiscountToLineItems(lineItems,salesOrderInput.getDiscountAmount(),session);
					}
				}
				String tempQuOrd = salesOrderInput.getQuoteNumber();
				String quoteOrder[] = tempQuOrd.split("-");
				orderNumber = quoteOrder[0];
				handlingListItems = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("handlingcharge.labels");
				deliveryListItems = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("deliverycharge.labels");
				errorMessageToDisplay = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("errormsgtodisplay.labels");
				backOrderSeqNumber = 0;
				if(quoteOrder != null && quoteOrder.length > 1){
					backOrderSeqNumber = CommonUtility.validateNumber(quoteOrder[1]);
				}
			}else{
				if(orderDetails != null && orderDetails.size() > 0){
					int i =1;
					for(ProductsModel item : orderDetails){
						Cimm2BCentralLineItem cimm2bCentralLineItem = new Cimm2BCentralLineItem();
						//cimm2bCentralLineItem.setLineNumber(item.getLineNumber());
						cimm2bCentralLineItem.setLineIdentifier(i++);
						cimm2bCentralLineItem.setPartNumber(item.getPartNumber());
						if(item.getItemId() == CommonUtility.validateNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID")))){
							cimm2bCentralLineItem.setPartNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_PARTNUMBER")));
						}
						cimm2bCentralLineItem.setQty(item.getQty());
						cimm2bCentralLineItem.setManufacturer(item.getManufacturerName());
						cimm2bCentralLineItem.setLineItemComment(item.getLineItemComment());
						if(CommonUtility.validateString(item.getGetPriceFrom()).equalsIgnoreCase("CART") && (item.getItemId() == CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID")))){
							String lineItemComment = item.getLineItemComment()!=null?item.getLineItemComment():" "+" "+ item.getShortDesc()!=null?item.getShortDesc():" ";
							cimm2bCentralLineItem.setLineItemComment(lineItemComment);	
						}
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ORDER_PRICE_SENDING_SETUP")).equalsIgnoreCase("ONLYOVPRITEMS") ) {					
							 if(CommonUtility.validateString(item.getOverRidePriceRule()).equalsIgnoreCase("Y")){
								cimm2bCentralLineItem.setUnitPrice(item.getPrice());
								cimm2bCentralLineItem.setListPrice(item.getListPrice());  
							}
						 }
						
						else {
							cimm2bCentralLineItem.setUnitPrice(item.getPrice());
							cimm2bCentralLineItem.setListPrice(item.getListPrice());
						}
						cimm2bCentralLineItem.setPage_title(item.getPageTitle());
						cimm2bCentralLineItem.setUomQty(Integer.toString(item.getQty()));
						cimm2bCentralLineItem.setCartId(item.getCartId());
						if(CommonUtility.customServiceUtility()!=null) {
							cimm2bCentralLineItem =  CommonUtility.customServiceUtility().disableUmqtyToErp(cimm2bCentralLineItem);
						}
						cimm2bCentralLineItem.setItemId(item.getPartNumber());
						
						
						cimm2bCentralLineItem.setShortDescription(item.getShortDesc()!=null?item.getShortDesc():".");
						cimm2bCentralLineItem.setProductCategory(item.getProductCategory()!=null?item.getProductCategory():"");
						cimm2bCentralLineItem.setShippingBranch(wareHousecode);
						if(CommonUtility.customServiceUtility()!=null) {
							cimm2bCentralLineItem =  CommonUtility.customServiceUtility().disablePriceToErp(cimm2bCentralLineItem, item);
						}
						/**
						 *Below code Written is for Turtle and Hughes for custom UOM *Reference- Nitesh
						 */
						if(CommonUtility.customServiceUtility()!=null) {
							CommonUtility.customServiceUtility().customizeOrderLineItem(cimm2bCentralLineItem, item);
							CommonUtility.customServiceUtility().setBackOrderTypeToLineItems(cimm2bCentralLineItem, item ,salesOrderInput);
							double extndPrice = CommonUtility.customServiceUtility().getExtendedPrice(item.getPrice(),item.getQty(),item.getSaleQty(),item.getSalesQuantity());
							if(item.getWarehouseErrorMsg()!=null && item.getWarehouseErrorMsg().equalsIgnoreCase(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("itemsetup.error.warhouse")))){
								cimm2bCentralLineItem.setNonStockFlag(true);
							}
							if(extndPrice > 0) {
								cimm2bCentralLineItem.setExtendedPrice(extndPrice);
								cimm2bCentralLineItem.setUom("");
								if(CommonUtility.customServiceUtility()!=null) {
									CommonUtility.customServiceUtility().updateUom(cimm2bCentralLineItem,item);
								}
							}else{
								cimm2bCentralLineItem.setUom(item.getUom() != null ? item.getUom().trim() : "");
								if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CALCULATE_UOM_BASED_PRICE")).equalsIgnoreCase("Y") ) {
									if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("OVERRIDE_PRICE_RULE")).equals("N")) {
										cimm2bCentralLineItem.setExtendedPrice((item.getUomQty()/item.getUomQuantity()) * item.getQty() *item.getUnitPrice() );
										cimm2bCentralLineItem.setUnitPrice(item.getUnitPrice());
										if(item.getUomPrice() > 0.0) {
											cimm2bCentralLineItem.setExtendedPrice(item.getUomPrice() * item.getQty());
											cimm2bCentralLineItem.setUnitPrice(item.getUomPrice());
										}
									}
								}
								else {
									if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ORDER_PRICE_SENDING_SETUP")).equalsIgnoreCase("ONLYOVPRITEMS") ) {
										 if(CommonUtility.validateString(item.getOverRidePriceRule()).equalsIgnoreCase("Y")){
										    cimm2bCentralLineItem.setExtendedPrice(item.getUnitPrice() * item.getQty());
										    cimm2bCentralLineItem.setUnitPrice(item.getPrice());    
										}									
									
									}
									else {
										cimm2bCentralLineItem.setExtendedPrice(item.getUnitPrice() * item.getQty());
										cimm2bCentralLineItem.setUnitPrice(item.getPrice());
									}
							}
								CommonUtility.customServiceUtility().getListPrice(item, cimm2bCentralLineItem);
						}
						}
						else{
							cimm2bCentralLineItem.setUom(item.getUom() != null ? item.getUom().trim() : "");
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CALCULATE_UOM_BASED_PRICE")).equalsIgnoreCase("Y") ) {
								if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("OVERRIDE_PRICE_RULE")).equals("N")) {
									cimm2bCentralLineItem.setExtendedPrice((item.getUomQty()/item.getUomQuantity()) * item.getQty() *item.getUnitPrice() );
									cimm2bCentralLineItem.setUnitPrice(item.getUnitPrice());
									if(item.getUomPrice() > 0.0) {
										cimm2bCentralLineItem.setExtendedPrice(item.getUomPrice() * item.getQty());
										cimm2bCentralLineItem.setUnitPrice(item.getUomPrice());
									}
								}
								else{
								}
							}
							else {
								cimm2bCentralLineItem.setExtendedPrice(item.getUnitPrice() * item.getQty());
								cimm2bCentralLineItem.setUnitPrice(item.getPrice());
							}
						}
						
						if(salesOrderInput.getTaxExempt()!=null && salesOrderInput.getTaxExempt().equalsIgnoreCase("N")){
							cimm2bCentralLineItem.setCalculateTax(true);
						}else{
							cimm2bCentralLineItem.setCalculateTax(false);
						}
						/**
						 *Below code Written is for Tyndale to get item level ship via details. *Reference- Chetan Sandesh
						 */
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
							cimm2bCentralLineItem.setShipMethod(item.getMultipleShipVia());
							if(item.getMultipleShipViaDesc()!=null){
								if(item.getMultipleShipViaDesc().split(",").length > 1)
								cimm2bCentralLineItem.setShippingBranch(item.getMultipleShipViaDesc().split(",")[1]);
							}else{
								if(CommonUtility.validateString(salesLocationId).length()>0){
									cimm2bCentralLineItem.setShippingBranch(salesLocationId);
								}else{
									cimm2bCentralLineItem.setShippingBranch(wareHousecode);
								}
							}
						}else if(CommonUtility.validateString(salesLocationId).length()>0 && !CommonUtility.validateString(salesLocationId).equalsIgnoreCase("0")){
							cimm2bCentralLineItem.setShippingBranch(salesLocationId);
						}else{
							cimm2bCentralLineItem.setShippingBranch(wareHousecode);	
						}
						if(CommonUtility.customServiceUtility()!=null) {
							CommonUtility.customServiceUtility().addDefaultWarehouseToItems(cimm2bCentralLineItem,session,salesOrderInput,wareHousecode,salesLocationId);
						}
						UnilogFactoryInterface serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
						if(serviceClass!=null && cimm2bCentralLineItem!=null) {
							Cimm2BCentralLineItem cimm2bCentralLineItemReArrange = serviceClass.submitSalesOrderItemList(cimm2bCentralLineItem);
							if(cimm2bCentralLineItemReArrange!=null){
								cimm2bCentralLineItem = cimm2bCentralLineItemReArrange;
							}
						}
						lineItems.add(cimm2bCentralLineItem);
						if(orderDetails.get(0).getCartTotal()>0) {
							subTotal = orderDetails.get(0).getCartTotal();
						}else {
							if(cimm2bCentralLineItem!=null && cimm2bCentralLineItem.getListPrice()>0){
								  subTotal=subTotal+(cimm2bCentralLineItem.getListPrice());
							  }else{
								  if(cimm2bCentralLineItem.getExtendedPrice()!=null) {
								  subTotal=subTotal+(cimm2bCentralLineItem.getExtendedPrice());
								  }
							  }
							}
						
					}
					//Discount Available
					if(CommonUtility.customServiceUtility()!=null) {
						CommonUtility.customServiceUtility().addDiscountToLineItems(lineItems,salesOrderInput.getDiscountAmount(),session);
					}
				}
			}
			
			String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			String branchCode = ProductsDAO.getWarehouseCustomField(CommonUtility.validateNumber(buyingCompanyId), "LOCATION_CODE");
			boolean isDFMModeActive = false; //DFM Changes
			boolean isMaximumThresholdMet = false; //DFM Changes
			if(session.getAttribute("DFMMode")!=null && CommonUtility.validateString(session.getAttribute("DFMMode").toString()).equalsIgnoreCase("Y")){
				isDFMModeActive = true;
				if(session.getAttribute("maximumThresholdMet")!=null && CommonUtility.validateString(session.getAttribute("maximumThresholdMet").toString()).equalsIgnoreCase("Y")){
					isMaximumThresholdMet = true;
				}
			}
			AddressModel billAddress = salesOrderInput.getBillAddress();
			AddressModel shipAddress =  salesOrderInput.getShipAddress();
			Cimm2BCentralAddress billingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(billAddress);
			Cimm2BCentralAddress shippingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(shipAddress);
			Cimm2BCentralBillAndShipToContact shipToContact = new Cimm2BCentralBillAndShipToContact();
			Cimm2BCentralBillAndShipToContact billToContact = new Cimm2BCentralBillAndShipToContact();
			Cimm2BCentralOrder orderRequest = new Cimm2BCentralOrder();
			orderRequest.setCartId(salesOrderInput.getExternalCartId());
			orderRequest.setJurisdictionCode(salesOrderInput.getJurisdictionCode());
			if(CommonUtility.validateString(salesOrderInput.getGuestFlag()).length()>0 		
					&& CommonUtility.validateString(salesOrderInput.getShipViaMethod()).equalsIgnoreCase(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("shipvia.method.pickup")))) {		
				shippingAddress.setAddressERPId("");		
			}
			orderRequest.setBillingAddress(billingAddress);
			orderRequest.setShippingAddress(shippingAddress);
			orderRequest.setShipToContact(shipToContact);
			orderRequest.setBillToContact(billToContact);
			orderRequest.setLineItems(lineItems);
			orderRequest.setOrderDate(orderDate);
			orderRequest.setOrderNotes(salesOrderInput.getOrderNotes()!=null?salesOrderInput.getOrderNotes():salesOrderInput.getReqDate());
	     	orderRequest.setCustomerERPId(customerErpId);
			orderRequest.setUserERPId((String) session.getAttribute(Global.USERID_KEY));
			
			if(CommonUtility.validateString((String)session.getAttribute("loginUserERPId")).length()>0){
				Cimm2BCentralUser userDetails =new Cimm2BCentralUser();
				userDetails.setUserERPId((String)session.getAttribute("loginUserERPId"));
				orderRequest.setUserDetails(userDetails);	
			}
			
			if(salesOrderInput.getSelectedBranch() !=null) {
				orderRequest.setBranchCode(salesOrderInput.getSelectedBranch());
			}else {
				orderRequest.setBranchCode(branchCode);
			}
			if(CommonUtility.customServiceUtility()!=null){
				CommonUtility.customServiceUtility().getBranchCode(orderRequest,wareHousecode);
				CommonUtility.customServiceUtility().setBackOrdersFlag(orderRequest,salesOrderInput);
			}
			if(CommonUtility.customServiceUtility()!=null){
				CommonUtility.customServiceUtility().getPickUpBranchCode(orderRequest,salesLocationId);
			}
			if(CommonUtility.customServiceUtility()!=null){
				CommonUtility.customServiceUtility().setWrittenBy(orderRequest);
			}
			orderRequest.setCustomerPoNumber(salesOrderInput.getPurchaseOrderNumber()!=null?salesOrderInput.getPurchaseOrderNumber().toUpperCase():"");
			orderRequest.setShippingInstruction(salesOrderInput.getShippingInstruction());
			orderRequest.setOrderERPId(salesOrderInput.getOrderERPId());
			orderRequest.setWarehouseLocation(CommonUtility.validateString(salesOrderInput.getUserSelectedLocation()).trim().length()>0?salesOrderInput.getUserSelectedLocation():wareHousecode);
			orderRequest.setOrderNumber(orderNumber);
			orderRequest.setBackOrderSequence(backOrderSeqNumber);
			orderRequest.setOrderComment(salesOrderInput.getOrderNotes());
			UnilogFactoryInterface serviceClassAPR = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
			if(serviceClassAPR!=null && orderRequest!=null) {
				orderRequest = serviceClassAPR.disableInteralNoteToERP(orderRequest);	
			}
			orderRequest.setOrderedBy(CommonUtility.validateString(salesOrderInput.getOrderedBy()));
			orderRequest.setRequiredDate(CommonUtility.validateString(salesOrderInput.getReqDate()));
			orderRequest.setFreightOut(CommonUtility.validateDoubleNumber(salesOrderInput.getFrieghtCharges()));
			orderRequest.setFreight(CommonUtility.validateDoubleNumber(salesOrderInput.getFrieghtCharges()));
			orderRequest.setOrderSubTotal(subTotal);
			orderRequest.setBackOrderTotal(subTotal);
			if(salesOrderInput.getDiscountAmount()>0){
				orderRequest.setDiscountAmount(salesOrderInput.getDiscountAmount());
				orderRequest.setOrderTotal((subTotal+orderRequest.getFreight()+CommonUtility.validateDoubleNumber(salesOrderInput.getShippingAndHandlingFee())+salesOrderInput.getOrderTax())-salesOrderInput.getDiscountAmount());
				}
			else {
				orderRequest.setOrderTotal(subTotal+orderRequest.getFreight()+CommonUtility.validateDoubleNumber(salesOrderInput.getShippingAndHandlingFee())+salesOrderInput.getOrderTax());
				}
			orderRequest.setGasPoNumber(salesOrderInput.getGasPoNumber());
			orderRequest.setReleaseNumber(CommonUtility.validateString(salesOrderInput.getCustomerReleaseNumber()));
			orderRequest.setOtherAmount(CommonUtility.validateDoubleNumber(salesOrderInput.getShippingAndHandlingFee())); // PSS DFM
			orderRequest.setDfmOrder(isDFMModeActive); // PSS DFM
			orderRequest.setAnonymous(salesOrderInput.getAnonymous());
			orderRequest.setMaximumOrderThresholdMet(isMaximumThresholdMet); // PSS DFM
			orderRequest.setUniqueWebReferenceNumber(salesOrderInput.getOrderId());
			if(CommonUtility.customServiceUtility()!=null){
				CommonUtility.customServiceUtility().setOrderNotes(orderRequest);
			}
			if(salesOrderInput.getOrderStatus() !=null)
			{
				orderRequest.setOrderStatus(salesOrderInput.getOrderStatus());
			}else {
				if(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderStatus.labels")!=null) {
					orderRequest.setOrderStatus(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderStatus.labels"));
				}
			}
			orderRequest.setOrderStatusCode(CommonUtility.validateString(salesOrderInput.getOrderStatusCode()));
			orderRequest.setTaxAmount(salesOrderInput.getOrderTax());
			orderRequest.setCustomerName(billAddress.getShipToName());
			if(CommonUtility.customServiceUtility() != null) {
				CommonUtility.customServiceUtility().changeCustomerName(orderRequest,salesOrderInput);
			}
			orderRequest.setFreightCode(salesOrderInput.getFreightCode());
			orderRequest.setOrderType(CommonUtility.validateString(salesOrderInput.getOrderType()));
			if(CommonUtility.validateString(salesOrderInput.getOrderSource()).length() > 0) {
				orderRequest.setOrderSource(salesOrderInput.getOrderSource());
			}else {
				if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels")).length()>0){
					orderRequest.setOrderSource(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels"):"");
				}
			}
			
			orderRequest.setOrderDisposition(salesOrderInput.getOrderDisposition()!=null?salesOrderInput.getOrderDisposition():"");
			if(CommonUtility.validateString(salesLocationId).length()>0 && !CommonUtility.validateString(salesLocationId).equalsIgnoreCase("0")){
				orderRequest.setSalesLocationId(salesLocationId);
			}else{
				orderRequest.setSalesLocationId(wareHousecode);	
			}
			if(CommonUtility.customServiceUtility() != null) {
				CommonUtility.customServiceUtility().pricingBranchId(orderRequest);
			}
			if(CommonUtility.customServiceUtility()!=null) {
				CommonUtility.customServiceUtility().addDefaultWarehouseToOrderRequest(orderRequest,session,salesOrderInput,wareHousecode,salesLocationId);
			}
			if(CommonDBQuery.getSystemParamtersList().get("CREATE_FREIGHT_LINE_ITEM") !=null &&CommonDBQuery.getSystemParamtersList().get("CREATE_FREIGHT_LINE_ITEM").equalsIgnoreCase("N")){
				orderRequest.setCreateShippingAndHandlingAsNewLineItem(false);
			}else{
				orderRequest.setCreateShippingAndHandlingAsNewLineItem(true);
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFUALT_CONTACT_ID")).length()>0){
			orderRequest.setContactId(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFUALT_CONTACT_ID")));
			}else if(CommonUtility.validateNumber(salesOrderInput.getErpUserContactId())>0){
				orderRequest.setContactId(CommonUtility.validateString(salesOrderInput.getErpUserContactId()));
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONSTANT_ORDER_COMMENT")).length()>0){
			orderRequest.setOrderCommentDisplayArea(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONSTANT_ORDER_COMMENT")));
			}
			if(salesOrderInput.getAnonymous()!=null && CommonUtility.validateString(salesOrderInput.getAnonymous()).equalsIgnoreCase("Y")){
				shipToContact.setFirstName(shipAddress.getFirstName());
				shipToContact.setLastName(shipAddress.getLastName());
				if(CommonUtility.validateString(shipAddress.getEmailAddress()).length()>0){
					shipToContact.setPrimaryEmailAddress(shipAddress.getEmailAddress().toUpperCase()!=null?shipAddress.getEmailAddress().toUpperCase():"");	
				}else{
					shipToContact.setPrimaryEmailAddress(billAddress.getEmailAddress()!=null?billAddress.getEmailAddress().toUpperCase():"");
				}
			}else{
				if(session.getAttribute("shipTofirstName")!= null && (String)session.getAttribute("shipTofirstName") != null){
					shipToContact.setFirstName((String)session.getAttribute("shipTofirstName"));
					if(session.getAttribute("shipToLastName")!= null && (String)session.getAttribute("shipToLastName") != null){
					shipToContact.setLastName((String)session.getAttribute("shipToLastName"));
					}else{
						shipToContact.setLastName(".");
					}
				}else{
					shipToContact.setFirstName(salesOrderInput.getShipAddress().getFirstName());
					if(CommonUtility.validateString(salesOrderInput.getShipAddress().getLastName()).length()>0){
						shipToContact.setLastName(salesOrderInput.getShipAddress().getLastName());
					}else{
						shipToContact.setLastName(".");
					}
				}
			}
			if(salesOrderInput.getShipAddress().getEmailAddress()!=null && salesOrderInput.getShipAddress().getEmailAddress()!=""){
				shipToContact.setPrimaryEmailAddress(salesOrderInput.getShipAddress().getEmailAddress().toUpperCase());
			}else if(CommonUtility.validateString(salesOrderInput.getBillAddress().getEmailAddress()).length()>0){
				shipToContact.setPrimaryEmailAddress(salesOrderInput.getBillAddress().getEmailAddress());
			}else {
				if(session!=null && (String)session.getAttribute("userEmailAddress")!=null) {
				shipToContact.setPrimaryEmailAddress(CommonUtility.validateString((String)session.getAttribute("userEmailAddress")));
				}
			}
			shipToContact.setPrimaryPhoneNumber(salesOrderInput.getShipAddress().getPhoneNo()!=null?salesOrderInput.getShipAddress().getPhoneNo():salesOrderInput.getBillAddress().getPhoneNo());
			shipToContact.setCarrierId(salesOrderInput.getShipVia());
			shipToContact.setContactId(customerErpId);
			shipToContact.setCompanyName(shippingAddress.getCustomerName());
			shipToContact.setShipAttention(shippingAddress.getCustomerName());
			billToContact.setPrimaryEmailAddress(billingAddress.getPrimaryEmailAddress());
			billToContact.setFirstName(billAddress.getFirstName());
			billToContact.setContactId(customerErpId);
			billToContact.setLastName(billAddress.getLastName());
			billToContact.setPrimaryPhoneNumber(billAddress.getPhoneNo());
			billToContact.setCompanyName(billAddress.getShipToName());
			billToContact.setShipAttention(billAddress.getShipToName());
			Cimm2BCentralCustomerCard customerCard = null;
			if(salesOrderInput.getCreditCardValue() != null){
				customerCard = new Cimm2BCentralCustomerCard();
				if(CommonUtility.validateString(salesOrderInput.getCreditCardValue().getElementPaymentAccountId()).length() > 0){
					customerCard.setPaymentAccountId(salesOrderInput.getCreditCardValue().getElementPaymentAccountId());
					String[] expDate = null;
					if(salesOrderInput.getCreditCardValue().getDate()!=null){
						expDate = salesOrderInput.getCreditCardValue().getDate().split("/");
					}
					String expMonth=null;
					String expYear=null;
					if(expDate!=null && expDate.length>1){
						expMonth = expDate[0];
						if(expDate.length>1){
							expYear = expDate[1];
							customerCard.setExpiryYear(expDate[1]);
						}
					}
					customerCard.setCustomerERPId(customerErpId);
					if(salesOrderInput.getBillAddress()!=null && CommonUtility.validateString(salesOrderInput.getBillAddress().getFirstName()).equalsIgnoreCase("")){
						customerCard.setCardHolderFirstName(salesOrderInput.getBillAddress().getFirstName());
					}
					if(salesOrderInput.getBillAddress()!=null && CommonUtility.validateString(salesOrderInput.getBillAddress().getLastName()).equalsIgnoreCase("")){
						customerCard.setCardHolderFirstName(salesOrderInput.getBillAddress().getLastName());
					}
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_MERCHANT_ID")).length()>0){
						customerCard.setPaymentAccountId(CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_MERCHANT_ID"));//(salesOrderInput.getCreditCardValue().getCreditCardTransactionID()); //CardTransactionID = PaymentAccountID
					}else{
						customerCard.setPaymentAccountId(salesOrderInput.getCreditCardValue().getCreditCardTransactionID());
					}
					if(CommonUtility.validateString(salesOrderInput.getCreditCardValue().getCreditCardToken()).length()>0){
						customerCard.setAuthorizationNumber(salesOrderInput.getCreditCardValue().getCreditCardToken());
					}else {
						customerCard.setAuthorizationNumber(salesOrderInput.getCreditCardValue().getCreditCardTransactionID());
					}
					customerCard.setCreditCardType(salesOrderInput.getCreditCardValue().getCreditCardType());
					customerCard.setCvv(salesOrderInput.getCreditCardValue().getCreditCardCvv2VrfyCode());
					customerCard.setCardHolderName(salesOrderInput.getCreditCardValue().getCardHolder());
					customerCard.setCreditCardNumber(salesOrderInput.getCreditCardValue().getCreditCardNumber());
					customerCard.setResponseCode(salesOrderInput.getCreditCardValue().getCreditCardResponseCode());
					customerCard.setExpiryDate(salesOrderInput.getCreditCardValue().getDate());
					if(CommonUtility.customServiceUtility() != null) {
						//Setting Authorization number as order comment.
						CommonUtility.customServiceUtility().setOrderComment(customerCard,orderRequest);
						CommonUtility.customServiceUtility().configureCreditCardDetails(customerCard);
						CommonUtility.customServiceUtility().getAuthAmount(customerCard,salesOrderInput);
					}
					
					//customerCard.setExpiryDate(salesOrderInput.getCreditCardValue().getDate());
					customerCard.setCardHolderFirstName(CommonUtility.validateString(billAddress.getFirstName()));
					customerCard.setCardHolderLastName(CommonUtility.validateString(billAddress.getLastName()));
					customerCard.setExpiryMonth(expMonth);
					customerCard.setExpiryYear(expYear);
					customerCard.setAmount(CommonUtility.validateDoubleNumber(salesOrderInput.getCreditCardValue().getCreditCardAmount()));
					customerCard.setAuthType(CommonUtility.validateString(salesOrderInput.getCreditCardValue().getCreditCardAuthType()));
					customerCard.setDataValue(salesOrderInput.getCreditCardValue().getDataValue());
					logger.info("credit card amount" +salesOrderInput.getCreditCardValue().getCreditCardAmount());
					if(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID")!=null && CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID").length()>0){
						customerCard.setElementProcessorID(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID")));
					}
					customerCard.setAddress(billingAddress);
					if(CommonUtility.validateString(salesOrderInput.getCreditCardValue().getSaveCard()).equalsIgnoreCase("Y")){
						customerCard.setSaveCard(true);	
					}else{
						customerCard.setSaveCard(false);
					}
					customerCard.setOrderERPId(salesOrderInput.getOrderERPId());
					orderRequest.setCustomerCard(customerCard);
					if(CommonUtility.customServiceUtility()!=null) {
						 CommonUtility.customServiceUtility().setPaymentTerm(orderRequest);
					}
				}
			}else{
				orderRequest.setPreAuthorize(false);
			}
			
			//CustomServiceProvider
			if(CommonUtility.customServiceUtility()!=null) {
				shipMap= CommonUtility.customServiceUtility().getCustomShipViaData(orderDetails);
			}
			//CustomServiceProvider
			
			if(shipMap!=null && shipMap.size()>0) {
				String freightKey= CommonUtility.validateString((String)session.getAttribute("shipMethodKey"));
				ArrayList<Cimm2BCentralShipVia> shipVia=new ArrayList<Cimm2BCentralShipVia>();
				for (Entry<String, Cimm2BCentralShipVia> entry : shipMap.entrySet()) {
					Cimm2BCentralShipVia cimm2bCentralShipVia = new Cimm2BCentralShipVia();
					cimm2bCentralShipVia.setShipViaCode(entry.getKey());
					cimm2bCentralShipVia.setFreight(entry.getValue().getFreight());
					cimm2bCentralShipVia.setTax(entry.getValue().getTax());
					cimm2bCentralShipVia.setOrderSubTotal(entry.getValue().getOrderSubTotal());
					cimm2bCentralShipVia.setOrderTotal(entry.getValue().getOrderTotal());
					cimm2bCentralShipVia.setShipViaErpId(salesOrderInput.getShipVia()!=null?salesOrderInput.getShipVia():"");
					if(entry.getValue().getAccountNumber()!=null) {
					cimm2bCentralShipVia.setAccountNumber(entry.getValue().getAccountNumber());
					}
					if(entry.getKey().equalsIgnoreCase(freightKey)) {
						if(CommonUtility.validateString((String)session.getAttribute("shipMethod")).length()>0) {
							cimm2bCentralShipVia.setShipViaDescription(CommonUtility.validateString((String)session.getAttribute("shipMethod")));
						}else {
						cimm2bCentralShipVia.setShipViaDescription(salesOrderInput.getShipViaDescription()!=null?salesOrderInput.getShipViaDescription():"");
						}
					}
					if(entry.getKey().equalsIgnoreCase(CommonUtility.validateString((LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.ship.method.storePickup"))))) {
						cimm2bCentralShipVia.setAuthorizedPickupPerson(salesOrderInput.getAdditionalName());
						cimm2bCentralShipVia.setShippingInstruction(salesOrderInput.getAdditionalComments());
					}else if(entry.getKey().equalsIgnoreCase(CommonUtility.validateString((LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.ship.method.shipToStore"))))) {
						cimm2bCentralShipVia.setAuthorizedPickupPerson(salesOrderInput.getAdditionalPickupPerson());
						cimm2bCentralShipVia.setShippingInstruction(salesOrderInput.getAdditionalCommentsShipToStore());
					}
					shipVia.add(cimm2bCentralShipVia);
				}
				orderRequest.setShipVia(shipVia);
			}else {
				if(CommonUtility.validateString(salesOrderInput.getShipVia()).length() > 0){
					ArrayList<Cimm2BCentralShipVia> shipVia=new ArrayList<Cimm2BCentralShipVia>();
					Cimm2BCentralShipVia cimm2bCentralShipVia = new Cimm2BCentralShipVia();
	
					if(CommonUtility.validateString(salesOrderInput.getShipViaMethod()).equalsIgnoreCase(CommonUtility.validateString((LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("shipvia.label.willcallerpcode").trim())))){
						cimm2bCentralShipVia.setShipViaCode(CommonUtility.validateString((LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("shipvia.label.willcallerpcode").trim())));// P21 standard name is willcall, If the user selected ship method = shipvia.label.willcallerpcode .. then WILLCALL will be set to TRUE on order for P21
						cimm2bCentralShipVia.setShipViaDescription(CommonUtility.validateString((LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("shipvia.label.willcallerpcode").trim())));// P21 standard name is willcall, If the user selected ship method = shipvia.label.willcallerpcode .. then WILLCALL will be set to TRUE on order for P21
					}else{
						//cimm2bCentralShipVia.setShipViaCode(salesOrderInput.getShipViaMethod());
						cimm2bCentralShipVia.setShipViaCode(salesOrderInput.getShipVia());
						cimm2bCentralShipVia.setShipViaDescription(salesOrderInput.getShipViaDescription()!=null?salesOrderInput.getShipViaDescription():salesOrderInput.getShipViaMethod());
					}
					 if(CommonUtility.customServiceUtility()!=null) {
							CommonUtility.customServiceUtility().setShipViaDescription(salesOrderInput,cimm2bCentralShipVia);
						}
					cimm2bCentralShipVia.setShipViaErpId(salesOrderInput.getShipVia());
					shipVia.add(cimm2bCentralShipVia);
					orderRequest.setShipVia(shipVia);
				}
			}
			orderRequest.setApprovalType(salesOrderInput.getApprovalType());
			orderRequest.setIncludeApprovalType(salesOrderInput.getIncludeApprovalType());

			if(session != null && session.getAttribute("localeCode")!= null && CommonUtility.validateString(LayoutLoader.getMessageProperties().get(((String)session.getAttribute("localeCode")).toUpperCase()).getProperty("orders.refrenceType")).length()>0){
				orderRequest.setReferenceId(LayoutLoader.getMessageProperties().get(((String)session.getAttribute("localeCode")).toUpperCase()).getProperty("orders.refrenceType")!=null?LayoutLoader.getMessageProperties().get(((String)session.getAttribute("localeCode")).toUpperCase()).getProperty("orders.refrenceType"):"");
			}
			if(session != null && session.getAttribute("localeCode")!= null && CommonUtility.validateString(LayoutLoader.getMessageProperties().get(((String)session.getAttribute("localeCode")).toUpperCase()).getProperty("orders.agencyType")).length()>0){
				orderRequest.setAgencyName(LayoutLoader.getMessageProperties().get(((String)session.getAttribute("localeCode")).toUpperCase()).getProperty("orders.agencyType")!=null?LayoutLoader.getMessageProperties().get(((String)session.getAttribute("localeCode")).toUpperCase()).getProperty("orders.agencyType"):"");
			}
			
			String CREATE_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_ORDER_API"));
			HttpServletRequest request = ServletActionContext.getRequest();
			if(CommonUtility.validateString(request.getHeader("User-Agent")).equalsIgnoreCase("WEBVIEW")) {
				System.out.println("Mobile App Order");
				CREATE_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_MOBILE_ORDER_API"));				
			}
			
			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_ORDER_URL, HttpMethod.POST, orderRequest, Cimm2BCentralOrder.class);


			Cimm2BCentralOrder orderResponse = null;
			if(orderResponseEntity!=null && orderResponseEntity.getData() != null && orderResponseEntity.getStatus() != null && (orderResponseEntity.getStatus().getCode() == HttpStatus.SC_OK )){
				ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
				orderResponse = (Cimm2BCentralOrder) orderResponseEntity.getData();
				orderResponse.setStatus(orderResponseEntity.getStatus());
				//From ERP - orderNumber is set to orderERPId in SX not sure why wasnt orderERPId included in if Statment
				if(orderResponse != null && ((CommonUtility.validateString(orderResponse.getOrderERPId()).length() > 0) || (CommonUtility.validateString(orderResponse.getOrderNumber()).length() > 0) || (CommonUtility.validateString(orderResponse.getCartId()).length() > 0))){
					
					orderDetail.setExternalCartId(quoteResponse != null ? quoteResponse.getExternalCartId() : "");
					orderDetail.setUserId(CommonUtility.validateNumber(orderResponse.getUserERPId()));
					if(CommonUtility.validateString(orderResponse.getOrderERPId()).length() > 0){
						orderDetail.setErpOrderNumber(orderResponse.getOrderERPId());
					}else{
						orderDetail.setErpOrderNumber(orderResponse.getOrderNumber());
					}	
					orderDetail.setOrderNum(orderResponse.getOrderNumber());
					orderDetail.setExternalSystemId(orderResponse.getOrderNumber());
					if(orderResponse.getOrderId()!=null){
						orderDetail.setOrderId(CommonUtility.validateNumber(orderResponse.getOrderId()));
					}else{
						orderDetail.setOrderID(orderResponse.getOrderNumber());
					}
					orderDetail.setStatusDescription("New");
					orderDetail.setOrderType(CommonUtility.validateString(orderResponse.getOrderType()));
					orderDetail.setOrderStatus(orderResponse.getOrderStatus()!=null?orderResponse.getOrderStatus():"New");
					orderDetail.setShippingInstruction(CommonUtility.validateString(orderResponse.getShippingInstruction()));
					orderDetail.setReqDate(CommonUtility.validateString(orderResponse.getRequiredDate()));
					orderDetail.setOrderNotes(CommonUtility.validateString(orderResponse.getOrderNotes()));
					orderDetail.setFreight(orderResponse.getFreight()!=null?orderResponse.getFreight() :0 );
					orderDetail.setTax(orderResponse.getTaxAmount()!=null?orderResponse.getTaxAmount():0);
					orderDetail.setOtherCharges(orderResponse.getOtherAmount()!=0?orderResponse.getOtherAmount():0);
					orderDetail.setHandling(orderResponse.getOtherCharges()!=0?orderResponse.getOtherCharges():0);
					orderDetail.setSubtotal(orderResponse.getOrderSubTotal()!=null?orderResponse.getOrderSubTotal():0);
					orderDetail.setTotal(orderResponse.getOrderTotal()!=null?orderResponse.getOrderTotal():0 + orderDetail.getFreight());
					orderDetail.setDiscount(orderResponse.getDiscountAmount()!=null?Math.abs(orderResponse.getDiscountAmount()):0 + orderDetail.getDiscount());
					orderDetail.setrOEDiscount(orderResponse.getRoeDiscountAmount()!=null?Math.abs(orderResponse.getRoeDiscountAmount()):0 + orderDetail.getrOEDiscount());
					orderDetail.setOrderedBy(orderResponse.getOrderedBy()!=null?orderResponse.getOrderedBy():"");
					orderDetail.setBillAddress(billAddress);
					orderDetail.setShipAddress(shipAddress);
					orderDetail.setOrderType(orderResponse.getOrderType());
					orderDetail.setPoNumber(orderResponse.getCustomerPoNumber()!=null?orderResponse.getCustomerPoNumber():"");
					orderDetail.setOrderDate(orderResponse.getOrderDate()!=null?orderResponse.getOrderDate():"");
					orderDetail.setShipViaDescription(salesOrderInput.getShipViaDescription());
					orderDetail.setAcceptBackorders(orderResponse.getAcceptBackorders()!=null?orderResponse.getAcceptBackorders():"");
					if(orderResponse.getShipToContact()!=null && orderResponse.getShipToContact().getPrimaryPhoneNumber()!=null) {
						orderDetail.setShipPhone(orderResponse.getShipToContact().getPrimaryPhoneNumber());
						orderDetail.setShipFirstName(orderResponse.getShipToContact().getFirstName()!=null?orderResponse.getShipToContact().getFirstName():"");
						orderDetail.setShipLastName(orderResponse.getShipToContact().getLastName()!=null?orderResponse.getShipToContact().getLastName():"");

					}
					if(orderResponse.getShipToContact()!=null && orderResponse.getShipToContact().getPrimaryEmailAddress()!=null) {
						orderDetail.setShipEmailAddress(orderResponse.getShipToContact().getPrimaryEmailAddress());
					}
					if(orderResponse.getBillToContact()!=null ) {
						if(orderResponse.getBillToContact().getPrimaryPhoneNumber()!=null) {
							orderDetail.setBillPhone(orderResponse.getBillToContact().getPrimaryPhoneNumber());
						}
						if(orderResponse.getBillToContact().getPrimaryEmailAddress()!=null) {
							orderDetail.setBillEmailAddress(orderResponse.getBillToContact().getPrimaryEmailAddress());
						}
						orderDetail.setBillFirstName(orderResponse.getBillToContact().getFirstName()!=null?orderResponse.getBillToContact().getFirstName():"");
						orderDetail.setBillLastName(orderResponse.getBillToContact().getLastName()!=null?orderResponse.getBillToContact().getLastName():"");
						orderDetail.setCompanyName(orderResponse.getBillToContact().getCompanyName()!=null?orderResponse.getBillToContact().getCompanyName():"");
					}
					if(orderResponse.getCustomerCard()!=null) {
						if(CommonUtility.validateString(orderResponse.getCustomerCard().getCreditCardNumber()).length()>0) {
							orderDetail.setCreditCardNumber(orderResponse.getCustomerCard().getCreditCardNumber());
						}
					}
					/**
					 *Below code Written is for Adapt Pharma to get item level ship via details. *Reference- Prashanth GM
					 */
					UnilogFactoryInterface serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
					if(serviceClass!=null && orderResponse.getOrderDate()!=null) {
						String expectedDeliveryDate = serviceClass.getExpectedDeliveryDate(orderResponse.getOrderDate());
						if(CommonUtility.validateString(expectedDeliveryDate).length()>0){
							orderDetail.setShipDate(expectedDeliveryDate);
						}
					}
					ArrayList<Cimm2BCentralShipVia> cimm2BCentralShipVia = orderResponse.getShipVia();
					if(orderResponse.getShipVia()!=null && orderResponse.getShipVia().size()>0){
						orderDetail.setCimm2BCentralShipVia(cimm2BCentralShipVia);
						for (Cimm2BCentralShipVia orderResponseGetShipVia : cimm2BCentralShipVia) {
							orderDetail.setShipViaID(orderResponseGetShipVia.getShipViaCode());
							orderDetail.setShipViaMethod(orderResponseGetShipVia.getShipViaDescription());
							orderDetail.setShipViaErpId(orderResponseGetShipVia.getShipViaErpId());
							orderDetail.setAccountNumber(orderResponseGetShipVia.getAccountNumber());
							orderDetail.setCarrierTrackingNumber(orderResponseGetShipVia.getCarrierTrackingNumber());
							//CustomServiceProvider
							String customShipViaDescription = null;
							if(CommonUtility.customServiceUtility()!=null) {
								customShipViaDescription = CommonUtility.customServiceUtility().getCustomShipViaDescription(session,orderResponseGetShipVia);
							}
							//CustomServiceProvider
							if(customShipViaDescription!=null) {
								if(customShipViaDescription.length()>0) {
									orderDetail.setShipViaDescription(customShipViaDescription);
								}
							}else {
								orderDetail.setShipViaDescription(orderResponseGetShipVia.getShipViaDescription());
							}
						}
					}
					ArrayList<Cimm2BCentralLineItem> cimm2bCentralLineItem = orderResponse.getLineItems();
					String NonCatalogItemId = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID_FOR_ADDTOCART"));
					if(cimm2bCentralLineItem != null && cimm2bCentralLineItem.size() > 0){
						double handlingCharges = 0.0;
						double deliveryCharges = 0.0;
						DecimalFormat df = CommonUtility.getPricePrecision(session);
						if(NonCatalogItemId.length()>0) {
						HashMap<String, SalesModel> lineItemsFromResponseCheckNonCatalogItems = new HashMap<String, SalesModel>();

						for (Cimm2BCentralLineItem orderResponseLineItem : cimm2bCentralLineItem) {

							SalesModel model = new SalesModel();

							model.setLineNumber(orderResponseLineItem.getLineNumber()!=null?orderResponseLineItem.getLineNumber():0);
							model.setLineItemComment(CommonUtility.validateString(orderResponseLineItem.getLineItemComment()!=null?orderResponseLineItem.getLineItemComment():""));
							model.setItemId(CommonUtility.validateNumber(orderResponseLineItem.getItemId()!=null?orderResponseLineItem.getItemId():""));
							model.setPartNumber(CommonUtility.validateString(orderResponseLineItem.getPartNumber()!=null?orderResponseLineItem.getPartNumber():""));
							model.setCustomerPartNumber(CommonUtility.validateString(orderResponseLineItem.getCustomerPartNumber()!=null?orderResponseLineItem.getCustomerPartNumber():""));
							model.setShortDesc(orderResponseLineItem.getShortDescription()!=null?orderResponseLineItem.getShortDescription():"");
							model.setUnitPrice(orderResponseLineItem.getUnitPrice());
							model.setExtPrice(orderResponseLineItem.getExtendedPrice()!=null?orderResponseLineItem.getExtendedPrice():0);
							model.setQtyordered(orderResponseLineItem.getQty());
							if(orderResponseLineItem.getQtyShipped()!=null){
								model.setQtyShipped(orderResponseLineItem.getQtyShipped().intValue());
							}
							model.setUom(orderResponseLineItem.getUom());
							model.setQtyUom(CommonUtility.validateString(orderResponseLineItem.getUomQty()!=null?orderResponseLineItem.getUomQty():"0"));
							model.setBrandName(CommonUtility.validateString(orderResponseLineItem.getBrandName()!=null?orderResponseLineItem.getBrandName():""));
							model.setManufacturer(CommonUtility.validateString(orderResponseLineItem.getManufacturer()!=null?orderResponseLineItem.getManufacturer():""));
							model.setManufacturerPartNumber(CommonUtility.validateString(orderResponseLineItem.getManufacturerPartNumber()!=null?orderResponseLineItem.getManufacturerPartNumber():""));
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
								model.setMultipleShipVia(orderResponseLineItem.getShipMethod());
								model.setMultipleShipViaDesc(orderResponseLineItem.getShippingBranch());
							}
							lineItemsFromResponseCheckNonCatalogItems.put(orderResponseLineItem.getPartNumber(), model);
						}
						
						for (ProductsModel orderItem : orderDetails) {
							lineItemsFromResponseCheckNonCatalogItems.remove(orderItem.getPartNumber());
						}
						List<String> handlingListItemsArray = Arrays.asList(handlingListItems.split(","));
						List<String> deliveryListItemsArray = Arrays.asList(deliveryListItems.split(","));
						Iterator<Entry<String, SalesModel>> it = lineItemsFromResponseCheckNonCatalogItems.entrySet().iterator();
						if(salesOrderInput.getOrderId()>0){
							while (it.hasNext()) {
								Map.Entry<String, SalesModel> pair = (Map.Entry<String, SalesModel>)it.next();
	
								if(handlingListItemsArray.contains(pair.getValue().getPartNumber())){
									handlingCharges = handlingCharges + pair.getValue().getExtPrice();
								}else if(deliveryListItemsArray.contains(pair.getValue().getPartNumber())){
									deliveryCharges = deliveryCharges + pair.getValue().getExtPrice();
								}
	
								ProductsModel nonCatalogItem = new ProductsModel();
								//nonCatalogItem.setLineNumber(cimm2bCentralLineItem.get(i).getLineNumber());
								nonCatalogItem.setLineItemComment(pair.getValue().getLineItemComment());
								nonCatalogItem.setItemId(CommonUtility.validateNumber(NonCatalogItemId));
								nonCatalogItem.setPartNumber(pair.getValue().getPartNumber());
								nonCatalogItem.setCustomerPartNumber(pair.getValue().getCustomerPartNumber());
								nonCatalogItem.setShortDesc(pair.getValue().getShortDesc()!=null?pair.getValue().getShortDesc():"");
								nonCatalogItem.setPrice(pair.getValue().getUnitPrice());
								nonCatalogItem.setExtendedPrice(pair.getValue().getExtPrice());
								nonCatalogItem.setQty(pair.getValue().getQtyShipped()!=0?pair.getValue().getQtyShipped():1);
								//model.setQtyShipped(pair.getValue().getQtyShipped().intValue());
								nonCatalogItem.setUom(pair.getValue().getUom());
								nonCatalogItem.setQtyUOM(CommonUtility.validateString(pair.getValue().getQtyUom()!=null?pair.getValue().getQtyUom():"0"));
								nonCatalogItem.setManufacturerName(pair.getValue().getManufacturer());
								nonCatalogItem.setManufacturerPartNumber(pair.getValue().getManufacturer()!=null?pair.getValue().getManufacturer():"");
	
								conn = ConnectionManager.getDBConnection();
								SalesAction.saveOrderItems(conn, salesOrderInput.getOrderId(), nonCatalogItem, "", 0, 0, "");
								it.remove(); // avoids a ConcurrentModificationException
							}
						}
					}else {
						for (Cimm2BCentralLineItem orderResponseLineItem : cimm2bCentralLineItem) {

							SalesModel model = new SalesModel();
							
							model.setLineNumber(orderResponseLineItem.getLineNumber()!=null?orderResponseLineItem.getLineNumber():0);
							model.setLineItemComment(CommonUtility.validateString(orderResponseLineItem.getLineItemComment()!=null?orderResponseLineItem.getLineItemComment():""));
							model.setItemId(CommonUtility.validateNumber(orderResponseLineItem.getItemId()!=null?orderResponseLineItem.getItemId():""));
							model.setPartNumber(CommonUtility.validateString(orderResponseLineItem.getPartNumber()!=null?orderResponseLineItem.getPartNumber():""));
							model.setCustomerPartNumber(CommonUtility.validateString(orderResponseLineItem.getCustomerPartNumber()!=null?orderResponseLineItem.getCustomerPartNumber():""));
							model.setShortDesc(orderResponseLineItem.getShortDescription()!=null?orderResponseLineItem.getShortDescription():"");
							if(orderResponseLineItem.getUnitPrice()!=null) {
								model.setUnitPrice(Double.parseDouble(df.format(orderResponseLineItem.getUnitPrice())));
							}else {
								model.setUnitPrice(0.0);
							}
							model.setBackorderType(orderResponseLineItem.getBackorderType()!=null?orderResponseLineItem.getBackorderType():"");
							if(CommonUtility.customServiceUtility()!=null) {
								CommonUtility.customServiceUtility().setQtyOnERP(model,orderResponseLineItem);
							}
							if(CommonUtility.customServiceUtility()!=null) {
								CommonUtility.customServiceUtility().getUnitPriceofOrders(model,orderResponseLineItem,orderResponse);
							}
							if(orderResponseLineItem.getExtendedPrice()!=null && orderResponseLineItem.getExtendedPrice()>0) {
								model.setExtPrice(Double.parseDouble(df.format(orderResponseLineItem.getExtendedPrice())));	
							}else {
								if(orderResponseLineItem.getUnitPrice()!=null) {
									model.setExtPrice(orderResponseLineItem.getQty()*orderResponseLineItem.getUnitPrice());
								}else {
									model.setExtPrice(0.0);
								}
							}
							model.setQtyordered(orderResponseLineItem.getQty());
							if(orderResponseLineItem.getQtyShipped()!=null){
								model.setQtyShipped(orderResponseLineItem.getQtyShipped().intValue());
							}
							model.setUom(orderResponseLineItem.getUom());
							model.setQtyUom(CommonUtility.validateString(orderResponseLineItem.getUomQty()!=null?orderResponseLineItem.getUomQty():"0"));
							model.setImageName(CommonUtility.validateString(orderResponseLineItem.getImageName()!=null?orderResponseLineItem.getImageName():""));
							model.setBrandName(CommonUtility.validateString(orderResponseLineItem.getBrandName()!=null?orderResponseLineItem.getBrandName():""));
							model.setManufacturer(CommonUtility.validateString(orderResponseLineItem.getManufacturer()!=null?orderResponseLineItem.getManufacturer():""));
							model.setManufacturerPartNumber(CommonUtility.validateString(orderResponseLineItem.getManufacturerPartNumber()!=null?orderResponseLineItem.getManufacturerPartNumber():""));
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
								model.setMultipleShipVia(orderResponseLineItem.getShipMethod());
								model.setMultipleShipViaDesc(orderResponseLineItem.getShippingBranch());
								WarehouseModel wareHouseDetail = new WarehouseModel();
								wareHouseDetail = UsersDAO.getWareHouseDetailsByCode(orderResponseLineItem.getShippingBranch());
								if(wareHouseDetail!=null) {
									model.setWareHouseDetails(wareHouseDetail);
								}
							}
							model.setAltPartNumber1(CommonUtility.validateString(orderResponseLineItem.getStorePartNumber()));
							model.setAltPartNumber2(CommonUtility.validateString(orderResponseLineItem.getSupplierPartNumber()));
							orderList.add(model);
						}
						
					}
						orderDetail.setOrderList(orderList);
						orderDetail.setHandling(orderResponse.getOtherCharges()!=0?orderResponse.getOtherCharges():0 + handlingCharges);
						orderDetail.setDeliveryCharge(deliveryCharges);
					}
					orderDetail.setStatus(CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()));
					orderDetail.setOrderStatusDesc("successfully");
					UnilogFactoryInterface orderClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
					if(orderClass!=null && cimm2bCentralLineItem!=null) {
						SalesModel ordersList = orderClass.orderdetail(cimm2bCentralLineItem);
						if(ordersList!=null){
							orderDetail.setQtyordered(ordersList.getQtyordered());
							orderDetail.setQtyShipped((ordersList.getQtyShipped()));
							orderDetail.setCustomerPrice(ordersList.getCustomerPrice());
							orderDetail.setLineItemComment(salesOrderInput.getComments());
							orderDetail.setPageTitle(salesOrderInput.getComments());
						}
					}
					
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCSPConfigurator")).equalsIgnoreCase("Y") && orderList != null && orderList.size() > 0 && orderDetails != null && orderDetails.size() > 0) {
						int userId = CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY));
						LogoCustomization logoCustomization = new LogoCustomization();
						LinkedHashMap<String, Object> cspOrderDetail = logoCustomization.createCspOrder(orderDetails, orderList, userId, CommonUtility.validateNumber(buyingCompanyId), orderDetail, session);
						if(cspOrderDetail != null) {
							ArrayList<SalesModel> cspOrderList = (ArrayList<SalesModel>) cspOrderDetail.get("orderList");
							if(cspOrderList != null && cspOrderList.size() > 0) {
								orderDetail.setOrderList(cspOrderList);
							}
							orderDetail.setDesignFeesModel((HashMap<Integer, CustomizationCharges>) cspOrderDetail.get("designFees"));
						}
					}
				orderDetail.setUniqueWebReferenceNumber(orderResponse.getUniqueWebReferenceNumber());
				}
			}else{
				//orderDetail.setStatusDescription(orderResponseEntity.getStatus().getMessage());
				if(CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()).length()>0 && CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()).equalsIgnoreCase("424")){
					errorMessageToDisplay=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("errormsgtodisplay.labels"));
					orderDetail.setStatusDescription(errorMessageToDisplay);

					ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
					if(orderResponseEntity.getData() !=null) {
					orderResponse = (Cimm2BCentralOrder) orderResponseEntity.getData();
					orderResponse.setStatus(orderResponseEntity.getStatus());
					//From ERP - orderNumber is set to orderERPId in SX not sure why wasnt orderERPId included in if Statment
					if(orderResponse != null && ((CommonUtility.validateString(orderResponse.getOrderNumber()).length() > 0) || (CommonUtility.validateString(orderResponse.getCartId()).length() > 0))){
						
						orderDetail.setExternalCartId(quoteResponse != null ? quoteResponse.getExternalCartId() : "");
						orderDetail.setUserId(CommonUtility.validateNumber(orderResponse.getUserERPId()));
						orderDetail.setOrderNum(orderResponse.getOrderNumber());
						orderDetail.setExternalSystemId(orderResponse.getOrderNumber());
						if(orderResponse.getOrderId()!=null){
							orderDetail.setOrderId(CommonUtility.validateNumber(orderResponse.getOrderId()));
						}else{
							orderDetail.setOrderID(orderResponse.getOrderNumber());
						}
						orderDetail.setOrderStatus(orderResponse.getOrderStatus()!=null?orderResponse.getOrderStatus():"New");
						orderDetail.setShippingInstruction(CommonUtility.validateString(orderResponse.getShippingInstruction()));
						orderDetail.setReqDate(CommonUtility.validateString(orderResponse.getRequiredDate()));
						orderDetail.setOrderNotes(CommonUtility.validateString(orderResponse.getOrderNotes()));
						orderDetail.setFreight(orderResponse.getFreight()!=null?orderResponse.getFreight() :0 );
						orderDetail.setTax(orderResponse.getTaxAmount()!=null?orderResponse.getTaxAmount():0);
						orderDetail.setOtherCharges(orderResponse.getOtherAmount()!=0?orderResponse.getOtherAmount():0);
						orderDetail.setHandling(orderResponse.getOtherCharges()!=0?orderResponse.getOtherCharges():0);
						orderDetail.setSubtotal(orderResponse.getOrderSubTotal()!=null?orderResponse.getOrderSubTotal():0);
						orderDetail.setTotal(orderResponse.getOrderTotal()!=null?orderResponse.getOrderTotal():0 + orderDetail.getFreight());
						orderDetail.setDiscount(orderResponse.getDiscountAmount()!=null?Math.abs(orderResponse.getDiscountAmount()):0 + orderDetail.getDiscount());
						orderDetail.setOrderedBy(orderResponse.getOrderedBy()!=null?orderResponse.getOrderedBy():"");
						orderDetail.setBillAddress(billAddress);
						orderDetail.setShipAddress(shipAddress);
						orderDetail.setPoNumber(orderResponse.getCustomerPoNumber()!=null?orderResponse.getCustomerPoNumber():"");
						orderDetail.setOrderDate(orderResponse.getOrderDate()!=null?orderResponse.getOrderDate():"");
						orderDetail.setShipViaDescription(salesOrderInput.getShipViaDescription());
						if(orderResponse.getShipToContact()!=null && orderResponse.getShipToContact().getPrimaryPhoneNumber()!=null) {
							orderDetail.setShipPhone(orderResponse.getShipToContact().getPrimaryPhoneNumber());
							orderDetail.setShipFirstName(orderResponse.getShipToContact().getFirstName()!=null?orderResponse.getShipToContact().getFirstName():"");
							orderDetail.setShipLastName(orderResponse.getShipToContact().getLastName()!=null?orderResponse.getShipToContact().getLastName():"");

						}
						if(orderResponse.getShipToContact()!=null && orderResponse.getShipToContact().getPrimaryEmailAddress()!=null) {
							orderDetail.setShipEmailAddress(orderResponse.getShipToContact().getPrimaryEmailAddress());
						}
						if(orderResponse.getBillToContact()!=null ) {
							if(orderResponse.getBillToContact().getPrimaryPhoneNumber()!=null) {
								orderDetail.setBillPhone(orderResponse.getBillToContact().getPrimaryPhoneNumber());
							}
							if(orderResponse.getBillToContact().getPrimaryEmailAddress()!=null) {
								orderDetail.setBillEmailAddress(orderResponse.getBillToContact().getPrimaryEmailAddress());
							}
							orderDetail.setBillFirstName(orderResponse.getBillToContact().getFirstName()!=null?orderResponse.getBillToContact().getFirstName():"");
							orderDetail.setBillLastName(orderResponse.getBillToContact().getLastName()!=null?orderResponse.getBillToContact().getLastName():"");
							orderDetail.setCompanyName(orderResponse.getBillToContact().getCompanyName()!=null?orderResponse.getBillToContact().getCompanyName():"");
						}
						if(orderResponse.getCustomerCard()!=null) {
							if(CommonUtility.validateString(orderResponse.getCustomerCard().getCreditCardNumber()).length()>0) {
								orderDetail.setCreditCardNumber(orderResponse.getCustomerCard().getCreditCardNumber());
							}
						}
						
						ArrayList<Cimm2BCentralShipVia> cimm2BCentralShipVia = orderResponse.getShipVia();
						if(orderResponse.getShipVia()!=null && orderResponse.getShipVia().size()>0){
							orderDetail.setCimm2BCentralShipVia(cimm2BCentralShipVia);
							for (Cimm2BCentralShipVia orderResponseGetShipVia : cimm2BCentralShipVia) {
								orderDetail.setShipViaID(orderResponseGetShipVia.getShipViaCode());
								orderDetail.setShipViaMethod(orderResponseGetShipVia.getShipViaDescription());
								orderDetail.setShipViaErpId(orderResponseGetShipVia.getShipViaErpId());
								orderDetail.setAccountNumber(orderResponseGetShipVia.getAccountNumber());
								orderDetail.setCarrierTrackingNumber(orderResponseGetShipVia.getCarrierTrackingNumber());
								orderDetail.setShipViaDescription(orderResponseGetShipVia.getShipViaDescription());
							}
						}
						ArrayList<Cimm2BCentralLineItem> cimm2bCentralLineItem = orderResponse.getLineItems();
						if(cimm2bCentralLineItem != null && cimm2bCentralLineItem.size() > 0){
							double handlingCharges = 0.0;
							double deliveryCharges = 0.0;
							DecimalFormat df = CommonUtility.getPricePrecision(session);
							for (Cimm2BCentralLineItem orderResponseLineItem : cimm2bCentralLineItem) {

								SalesModel model = new SalesModel();
								
								model.setLineNumber(orderResponseLineItem.getLineNumber()!=null?orderResponseLineItem.getLineNumber():0);
								model.setLineItemComment(CommonUtility.validateString(orderResponseLineItem.getLineItemComment()!=null?orderResponseLineItem.getLineItemComment():""));
								model.setItemId(CommonUtility.validateNumber(orderResponseLineItem.getItemId()!=null?orderResponseLineItem.getItemId():""));
								model.setPartNumber(CommonUtility.validateString(orderResponseLineItem.getPartNumber()!=null?orderResponseLineItem.getPartNumber():""));
								model.setCustomerPartNumber(CommonUtility.validateString(orderResponseLineItem.getCustomerPartNumber()!=null?orderResponseLineItem.getCustomerPartNumber():""));
								model.setShortDesc(orderResponseLineItem.getShortDescription()!=null?orderResponseLineItem.getShortDescription():"");
								if(orderResponseLineItem.getUnitPrice()!=null) {
									model.setUnitPrice(Double.parseDouble(df.format(orderResponseLineItem.getUnitPrice())));
								}else {
									model.setUnitPrice(0.0);
								}
								model.setBackorderType(orderResponseLineItem.getBackorderType()!=null?orderResponseLineItem.getBackorderType():"");
								if(CommonUtility.customServiceUtility()!=null) {
									CommonUtility.customServiceUtility().setQtyOnERP(model,orderResponseLineItem);
								}
								if(CommonUtility.customServiceUtility()!=null) {
									CommonUtility.customServiceUtility().getUnitPriceofOrders(model,orderResponseLineItem,orderResponse);
								}
								if(orderResponseLineItem.getExtendedPrice()!=null && orderResponseLineItem.getExtendedPrice()>0) {
									model.setExtPrice(Double.parseDouble(df.format(orderResponseLineItem.getExtendedPrice())));	
								}else {
									if(orderResponseLineItem.getUnitPrice()!=null) {
										model.setExtPrice(orderResponseLineItem.getQty()*orderResponseLineItem.getUnitPrice());
									}else {
										model.setExtPrice(0.0);
									}
								}
								model.setQtyordered(orderResponseLineItem.getQty());
								if(orderResponseLineItem.getQtyShipped()!=null){
									model.setQtyShipped(orderResponseLineItem.getQtyShipped().intValue());
								}
								model.setUom(orderResponseLineItem.getUom());
								model.setQtyUom(CommonUtility.validateString(orderResponseLineItem.getUomQty()!=null?orderResponseLineItem.getUomQty():"0"));
								model.setImageName(CommonUtility.validateString(orderResponseLineItem.getImageName()!=null?orderResponseLineItem.getImageName():""));
								model.setBrandName(CommonUtility.validateString(orderResponseLineItem.getBrandName()!=null?orderResponseLineItem.getBrandName():""));
								model.setManufacturer(CommonUtility.validateString(orderResponseLineItem.getManufacturer()!=null?orderResponseLineItem.getManufacturer():""));
								model.setManufacturerPartNumber(CommonUtility.validateString(orderResponseLineItem.getManufacturerPartNumber()!=null?orderResponseLineItem.getManufacturerPartNumber():""));
								if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
									model.setMultipleShipVia(orderResponseLineItem.getShipMethod());
									model.setMultipleShipViaDesc(orderResponseLineItem.getShippingBranch());
									WarehouseModel wareHouseDetail = new WarehouseModel();
									wareHouseDetail = UsersDAO.getWareHouseDetailsByCode(orderResponseLineItem.getShippingBranch());
									if(wareHouseDetail!=null) {
										model.setWareHouseDetails(wareHouseDetail);
									}
								}
								model.setAltPartNumber1(CommonUtility.validateString(orderResponseLineItem.getStorePartNumber()));
								model.setAltPartNumber2(CommonUtility.validateString(orderResponseLineItem.getSupplierPartNumber()));
								orderList.add(model);
							}
							
						}
							orderDetail.setOrderList(orderList);
							orderDetail.setHandling(orderResponse.getOtherCharges());
							
						}
					}
						orderDetail.setStatus(CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()));
						orderDetail.setOrderStatusDesc("Failure");
					orderDetail.setUniqueWebReferenceNumber(orderResponse.getUniqueWebReferenceNumber());
					}
				else{
					orderDetail.setStatusDescription(orderResponseEntity.getStatus().getMessage());
				}
				orderDetail.setStatus(CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()));
				orderDetail.setSendMailFlag(false);
				orderDetail.setUniqueWebReferenceNumber(orderRequest.getUniqueWebReferenceNumber());
			}

		}
		catch (SQLException e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBConnection(conn);
		}

		return orderDetail;

	}

	public static ArrayList<SalesModel> OpenOrders(SalesModel salesInputParameter) {
		ArrayList<SalesModel> openOrderList = new ArrayList<SalesModel>();
		SalesModel pages=new SalesModel();
		Cimm2BCentralOpenOrderInformation cimm2BCntralOpenOrderList = null;
		try{
			String customerErpId = salesInputParameter.getCustomerNumber();
			//int pageNumber=salesInputParameter.getPageNumber();
			String GET_OPEN_ORDERS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_OPEN_ORDERS_PAGINATION")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" + customerErpId;
			if((CommonUtility.validateString(salesInputParameter.getGuestFlag()).length()>0) 		
					&& (CommonUtility.validateString(salesInputParameter.getStartDate()).length()>0 && CommonUtility.validateString(salesInputParameter.getEndDate()).length()>0)){		
				GET_OPEN_ORDERS = GET_OPEN_ORDERS + "&" + Cimm2BCentralRequestParams.startDate + "=" + salesInputParameter.getStartDate() + "&" + Cimm2BCentralRequestParams.endDate + "=" + salesInputParameter.getEndDate();		
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("QOUTE_ORDER_REQUIRED")).equalsIgnoreCase("Y") || CommonUtility.validateString(salesInputParameter.getStatus()).equalsIgnoreCase("quotes")){
				GET_OPEN_ORDERS = GET_OPEN_ORDERS + "&" + Cimm2BCentralRequestParams.qouteOrder + "=true";
			}
			if(CommonUtility.customServiceUtility()!=null) {
				GET_OPEN_ORDERS = GET_OPEN_ORDERS + CommonUtility.customServiceUtility().getOpenOrdersRequestParams(salesInputParameter, GET_OPEN_ORDERS);
			}
			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_OPEN_ORDERS, "GET", null, Cimm2BCentralOpenOrderInformation.class);
		
			List<Cimm2BCentralOpenOrderInformationDetails> Cimm2BCentralOpenOrderInformationDetails = null;
			if(orderResponseEntity!=null && orderResponseEntity.getData() != null){
				cimm2BCntralOpenOrderList = (Cimm2BCentralOpenOrderInformation)orderResponseEntity.getData();
				pages.setTotalPages(cimm2BCntralOpenOrderList.getTotalPages());
				pages.setPageNumber(cimm2BCntralOpenOrderList.getPageNumber());
				pages.setTotal(cimm2BCntralOpenOrderList.getPageSize());
				if(cimm2BCntralOpenOrderList != null){
					Cimm2BCentralOpenOrderInformationDetails = cimm2BCntralOpenOrderList.getDetails();
					if(Cimm2BCentralOpenOrderInformationDetails!=null) {
						for(Cimm2BCentralOpenOrderInformationDetails openOrder : Cimm2BCentralOpenOrderInformationDetails){
							SalesModel salesModel = new SalesModel();
							salesModel.setOrderNum(openOrder.getOrderNumber());
							if(openOrder.getGenerationId()!=null) {
								salesModel.setGeneId(openOrder.getGenerationId());
							}else {
								salesModel.setGeneId(openOrder.getBackOrderSequence());
							}
							salesModel.setOrderDate(openOrder.getOrderDate());
							salesModel.setManufacturer(openOrder.getSupplier());
							salesModel.setPartNumber(openOrder.getPart());
							salesModel.setDescription1(openOrder.getDescription1());
							salesModel.setDescription2(openOrder.getDescription2());
							salesModel.setDescription(openOrder.getLocation()); // ---- For location field 
							salesModel.setTotal(openOrder.getOrderTotal()!=null?openOrder.getOrderTotal():0);
							salesModel.setPoNumber(openOrder.getPoNumber());
							salesModel.setInvoiceNumber(openOrder.getInvoiceNumber());
							salesModel.setShipToName(openOrder.getShipToName());
							salesModel.setShipToId(openOrder.getShipToId()!=null?openOrder.getShipToId():"");
							salesModel.setOrderType(openOrder.getOrderType()!=null?openOrder.getOrderType():"");
							salesModel.setOpenOrderAmount(openOrder.getOrderTotal()!=null?openOrder.getOrderTotal():0);
							salesModel.setOrderedBy(openOrder.getOrderedBy());
							salesModel.setOrderStatus(openOrder.getOrderStatus());
							salesModel.setShipDate(openOrder.getShipDate());
							salesModel.setItemLevelRequiredByDate(openOrder.getRequiredDate());
							salesModel.setReqDate(openOrder.getRequiredDate());
							salesModel.setOrderSuffix(CommonUtility.validateNumber(openOrder.getOrderSuffix()));
							salesModel.setrOEDiscount(openOrder.getRoeDiscountAmount()!=null?Double.parseDouble(openOrder.getRoeDiscountAmount()):0);
							salesModel.setDiscount(openOrder.getDiscountAmount()!=null?Double.parseDouble(openOrder.getDiscountAmount()):0);
							salesModel.setInvoiceAmount(openOrder.getTotalInvoiceAmount()!=null?openOrder.getTotalInvoiceAmount():0);
							openOrderList.add(salesModel);
						}
					}
				}
				//openOrderList.add(pages);
			}
		}catch (Exception e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return openOrderList;
		}
	public static CreditCardModel creditCardPreAuthorization(CreditCardModel creditCardValue) {
		//ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
		//orderList = SalesDAO.getOrdersHistory(salesInputParameter);
		CreditCardModel creditCardDetails=null;
		try{
			String GET_CREDIT_CARD_AUTHORIZATION = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CREDIT_CARD_AUTHORIZATION"));	
			Cimm2BCentralAddress address=new Cimm2BCentralAddress();
	
			Cimm2BCentralCustomerCard creditCardAuthroziation = new Cimm2BCentralCustomerCard();
	
			creditCardAuthroziation.setCardHolderName(creditCardValue.getCardHolder());
	
			creditCardAuthroziation.setCreditCardNumber(creditCardValue.getCreditCardNumber());
			creditCardAuthroziation.setCreditCardType(creditCardValue.getCreditCardType());
			creditCardAuthroziation.setCvv(creditCardValue.getCreditCardCvv2VrfyCode());
			creditCardAuthroziation.setExpiryMonth(creditCardValue.getCreditCardExpiryMonth());
			creditCardAuthroziation.setExpiryYear(creditCardValue.getCreditCardExpiryYear());
			creditCardAuthroziation.setAmount(CommonUtility.validateDoubleNumber(creditCardValue.getAmount()));
			
			creditCardAuthroziation.setCurrencyCode(creditCardValue.getCreditCardResponseCode());
	
			address.setAddressLine1(creditCardValue.getAddress1());
			address.setState(creditCardValue.getState());
			address.setCountry(creditCardValue.getCountry());
			address.setZipCode(creditCardValue.getZipCode());
	
			creditCardAuthroziation.setAddress(address);
			Cimm2BCentralResponseEntity creditCardAuthroziationResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_CREDIT_CARD_AUTHORIZATION, "POST", creditCardAuthroziation, Cimm2BCentralCustomerCard.class);
	
			Cimm2BCentralCustomerCard customerCard = null;//Check ..1
	
			if(creditCardAuthroziationResponseEntity!=null && creditCardAuthroziationResponseEntity.getData() != null){
				customerCard = (Cimm2BCentralCustomerCard) creditCardAuthroziationResponseEntity.getData();
				customerCard.setStatus(creditCardAuthroziationResponseEntity.getStatus());
				if(customerCard != null && CommonUtility.validateString(customerCard.getTransactionId()).length() > 0){
					creditCardDetails = new CreditCardModel();
					creditCardDetails.setCreditCardTransactionID(customerCard.getTransactionId());
				}
			}
		}catch (Exception e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return creditCardDetails;
	}
	public static ArrayList<SalesModel> OrderHistory(SalesModel salesInputParameter) {
		ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
		//orderList = SalesDAO.getOrdersHistory(salesInputParameter);
		try{
			String GET_ORDER_HISTORY = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ORDER_HISTORY"));	
	
			Cimm2BCentralSalesOrderHistoryRequest salesOrderHistoryRequest = new Cimm2BCentralSalesOrderHistoryRequest();
			salesOrderHistoryRequest.setCustomerERPId(salesInputParameter.getCustomerNumber());
			salesOrderHistoryRequest.setFromDate(salesInputParameter.getStartDate());
			salesOrderHistoryRequest.setToDate(salesInputParameter.getEndDate());	
			salesOrderHistoryRequest.setTextSearch(salesInputParameter.getSearchString());
			salesOrderHistoryRequest.setWarehouseLocation(salesInputParameter.getWareHouseCode());
			if(CommonUtility.customServiceUtility()!=null) {
				CommonUtility.customServiceUtility().getOrderHistoryRequestParams(salesInputParameter, salesOrderHistoryRequest);
			}
			if(CommonUtility.customServiceUtility()!=null) {
					CommonUtility.customServiceUtility().getTrackOrderRequestParams(salesInputParameter, salesOrderHistoryRequest);
			}

	
			Cimm2BCentralResponseEntity salesOrderHistoryResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_ORDER_HISTORY, HttpMethod.POST, salesOrderHistoryRequest, Cimm2BCentralSalesOrderHistory.class);
	
			Cimm2BCentralSalesOrderHistory salesOrderHistory = null;
			List<Cimm2BCentralSalesOrderHistoryDetails> salesOrderHistoryDetails = null;
			if(salesOrderHistoryResponseEntity!=null && salesOrderHistoryResponseEntity.getData() != null){
				salesOrderHistory = (Cimm2BCentralSalesOrderHistory) salesOrderHistoryResponseEntity.getData();
				if(salesOrderHistory != null) {
					salesOrderHistoryDetails = salesOrderHistory.getDetails();
					if(salesOrderHistoryDetails != null && salesOrderHistoryDetails.size() > 0){
						for(Cimm2BCentralSalesOrderHistoryDetails orderHistory : salesOrderHistoryDetails){
							SalesModel salesModel = new SalesModel();
							salesModel.setOrderNum(orderHistory.getOrderNumber());
							if(orderHistory.getGenerationId() !=null) {
								salesModel.setGeneId(orderHistory.getGenerationId());
							}else {
								salesModel.setGeneId(orderHistory.getBackOrderSequence());
							}
							
							salesModel.setOrderDate(orderHistory.getOrderDate());
							salesModel.setManufacturer(orderHistory.getSupplier());
							salesModel.setPartNumber(orderHistory.getPart());
							salesModel.setShipDate(orderHistory.getShipDate());
							salesModel.setDescription1(orderHistory.getDescription1());
							salesModel.setDescription2(orderHistory.getDescription2());
							salesModel.setDescription(orderHistory.getLocation()); // ---- For location field
							salesModel.setTotal(orderHistory.getOrderTotal()!=null?orderHistory.getOrderTotal():0);
							salesModel.setPoNumber(orderHistory.getPoNumber());
							salesModel.setInvoiceNumber(orderHistory.getInvoiceNumber());
							salesModel.setShipToName(orderHistory.getShipToName());
							salesModel.setShipToId(orderHistory.getShipToId()!=null?orderHistory.getShipToId():"");
							salesModel.setOrderType(orderHistory.getOrderType()!=null?orderHistory.getOrderType():"");
							salesModel.setCustomerName(orderHistory.getCustomerName());
							//salesModel.setCustomerERPId(orderHistory.getCustomerERPId());
							salesModel.setOrderStatus(orderHistory.getOrderStatus());
							salesModel.setOrderedBy(orderHistory.getOrderedBy());
							salesModel.setAccountId(orderHistory.getAccountId());
							salesModel.setOrderStatusType(orderHistory.getOrderTypeCode());
							salesModel.setOrderSuffix(CommonUtility.validateNumber(orderHistory.getOrderSuffix()));	
							salesModel.setrOEDiscount(orderHistory.getRoeDiscountAmount()!=null?Double.parseDouble(orderHistory.getRoeDiscountAmount()):0);
							salesModel.setDiscount(orderHistory.getDiscountAmount()!=null?Double.parseDouble(orderHistory.getDiscountAmount()):0);
							salesModel.setNetAmount(orderHistory.getInvoiceValue()!=null?Double.parseDouble(orderHistory.getInvoiceValue()):0);
							salesModel.setBalanceAmount(orderHistory.getBalance()!=null?orderHistory.getBalance():"0.00");
							salesModel.setPostDate(orderHistory.getPostDate());
							salesModel.setShippingBranchId(orderHistory.getShippingBranch());
							salesModel.setShippingBranchName(CommonUtility.getWareHouseByCode(orderHistory.getShippingBranch())!=null?CommonUtility.getWareHouseByCode(orderHistory.getShippingBranch()).getWareHouseName():"");
							salesModel.setStatusDescription(orderHistory.getDescription());
							salesModel.setInvoiceAmount(orderHistory.getTotalInvoiceAmount()!=null?orderHistory.getTotalInvoiceAmount():0);
							orderList.add(salesModel);
						}
						
					}
					
					if(salesOrderHistory.getAdditionalData() != null) {
						Cimm2BCentralSalesOrderHistoryAdditionalData additionalData = salesOrderHistory.getAdditionalData();
						if(additionalData.getRowIds() != null && additionalData.getRowIds().size() > 0 && orderList != null && orderList.size() > 0) {
							orderList.get(0).setRowIds(additionalData.getRowIds());
							orderList.get(0).setStartRowId(salesInputParameter.getStartRowId());
							orderList.get(0).setPageSize(salesInputParameter.getPageSize());
						}
					}
				}
			}
		}catch (Exception e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return orderList;
		}

	public static LinkedHashMap<String, ArrayList<SalesModel>> OrderDetail(SalesModel salesInputParameter) {
		LinkedHashMap<String, ArrayList<SalesModel>> orderDetailInfo = new LinkedHashMap<String, ArrayList<SalesModel>>();
		try{
			HttpSession session = salesInputParameter.getSession();
			int defaultBillToId = 0;
			int defaultShipToId = 0;
			int orderSuffix = salesInputParameter.getOrderSuffix();
			String orderNumber = salesInputParameter.getOrderID();
			String genId= salesInputParameter.getGeneId();
			String invoiceid = salesInputParameter.getInvoiceNumber();
			String customerErpId = salesInputParameter.getUserToken()!=null?salesInputParameter.getUserToken():CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID");
			String warehouseCode = salesInputParameter.getWareHouseCode();
			String sequenceNumber = CommonUtility.validateParseIntegerToString(salesInputParameter.getSeqnum());
			ArrayList<SalesModel> orderDetail = new ArrayList<SalesModel>();
			SalesModel orderDetailModel = new SalesModel();
			String GET_ORDER_URL =  null;
			if(salesInputParameter.getOrderStatus()!= null && salesInputParameter.getOrderStatus().contains("History")) {
				if(CommonUtility.validateString(sequenceNumber).length()>0){
					GET_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ORDER_API")) + "?" + Cimm2BCentralRequestParams.orderNumber+ "=" + orderNumber +"&" + Cimm2BCentralRequestParams.sequenceNumber + "=" + sequenceNumber + "&"  +Cimm2BCentralRequestParams.customerERPId + "=" + customerErpId;
				} else {
					GET_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ORDER_API")) + "?" + Cimm2BCentralRequestParams.orderNumber+ "=" + orderNumber + "&"  + Cimm2BCentralRequestParams.orderInvoiceNumber + "=" + orderSuffix+""+invoiceid + "&" +Cimm2BCentralRequestParams.customerERPId + "=" + customerErpId +"&"+Cimm2BCentralRequestParams.warehouseCode +"="+warehouseCode;
				}
			}else if(salesInputParameter.getOrderStatus()!= null && salesInputParameter.getOrderStatus().contains("TrackOrder")){
				 GET_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ORDER_API")) + "?" + Cimm2BCentralRequestParams.orderNumber+ "=" + orderNumber + "&" + Cimm2BCentralRequestParams.orderSuffix + "=" + orderSuffix + "&"  +Cimm2BCentralRequestParams.customerERPId + "=" + customerErpId;
			}else if(CommonUtility.validateString(salesInputParameter.getGuestFlag()).length()>0){
				GET_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ORDER_API")) + "?" + Cimm2BCentralRequestParams.orderNumber+ "=" + orderNumber + "&" + Cimm2BCentralRequestParams.orderGenerationNumber + "=" + orderSuffix+""+genId + "&"  +Cimm2BCentralRequestParams.customerERPId + "=" + customerErpId + "&" + Cimm2BCentralRequestParams.orderType + "=" + salesInputParameter.getOrderStatus()+"&"+Cimm2BCentralRequestParams.warehouseCode +"="+warehouseCode + "&"+ Cimm2BCentralRequestParams.guestFlag + "=" + salesInputParameter.getGuestFlag();
			}else {
				 GET_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ORDER_API")) + "?" + Cimm2BCentralRequestParams.orderNumber+ "=" + orderNumber + "&" + Cimm2BCentralRequestParams.orderGenerationNumber + "=" + orderSuffix+""+genId + "&"  +Cimm2BCentralRequestParams.customerERPId + "=" + customerErpId + "&" + Cimm2BCentralRequestParams.orderType + "=" + salesInputParameter.getOrderStatus()+"&"+Cimm2BCentralRequestParams.warehouseCode +"="+warehouseCode;
			}
			
			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_ORDER_URL,HttpMethod.GET, null, Cimm2BCentralOrder.class);
			ArrayList<SalesModel>orderItemList = null;
			Cimm2BCentralOrder order = null;
			if(orderResponseEntity != null && orderResponseEntity.getData() != null){
				order = (Cimm2BCentralOrder) orderResponseEntity.getData();
				order.setStatus(orderResponseEntity.getStatus());
				orderDetailModel.setOrderType(order.getOrderType());
				orderDetailModel.setOrderNum(order.getOrderNumber());
				orderDetailModel.setErpOrderNumber(orderNumber);
				orderDetailModel.setCustomerNumber(order.getCustomerERPId());
				orderDetailModel.setOrderStatus(order.getOrderStatus());
				orderDetailModel.setPoNumber(order.getCustomerPoNumber());
				orderDetailModel.setOrderedBy(order.getOrderedBy());
				orderDetailModel.setShipDate(order.getShipDate());
				orderDetailModel.setOrderDate(order.getOrderDate());
				orderDetailModel.setOrderSource(order.getOrderSource());
				orderDetailModel.setOrderSuffix(order.getOrderSuffix());
				orderDetailModel.setOrderGenerationNumber(order.getOrderGenerationNumber());
				orderDetailModel.setGeneId(genId);
				orderDetailModel.setInvoiceNumber(invoiceid);
				orderDetailModel.setInvoiceAmount(order.getTotalInvoiceAmount()!=null?order.getTotalInvoiceAmount():0);
			    orderDetailModel.setAddOnNumber3(order.getAddOnNumber3());
				if(order.getBillToContact() != null) {
					orderDetailModel.setPhone(order.getBillToContact().getPrimaryPhoneNumber());
				}
				
				if(session.getAttribute("defaultBillToId")!=null && session.getAttribute("defaultBillToId").toString().trim().length()>0){
					defaultBillToId = CommonUtility.validateNumber((String)session.getAttribute("defaultBillToId"));
				}
				if(session.getAttribute("defaultShipToId")!=null && session.getAttribute("defaultShipToId").toString().trim().length()>0){
					defaultShipToId = CommonUtility.validateNumber((String)session.getAttribute("defaultShipToId"));
				}
				UserManagement usersObj = new UserManagementImpl();
				HashMap<String, UsersModel> userAddress = usersObj.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
				//UsersModel billAddress = userAddress.get("Bill");
	
				AddressModel billAddressModel = null;
				if(order.getBillingAddress() != null){
					billAddressModel = Cimm2BCentralClient.getInstance().cimm2BCentralAddressToEcomAddressModel(order.getBillingAddress());
				}
				AddressModel shipAddressModel = null;
				if(order.getShippingAddress() != null){
					shipAddressModel = Cimm2BCentralClient.getInstance().cimm2BCentralAddressToEcomAddressModel(order.getShippingAddress());
				}
				orderDetailModel.setBillAddress(billAddressModel);
				orderDetailModel.setShipAddress(shipAddressModel);
	
				ArrayList<Cimm2BCentralShipVia> shipMethod = order.getShipVia();
	
				if(shipMethod != null && shipMethod.size()>0){
					orderDetailModel.setCimm2BCentralShipVia(shipMethod);
					for (Cimm2BCentralShipVia orderResponseGetShipMethod : shipMethod) {
						if(CommonUtility.validateString(orderResponseGetShipMethod.getShipViaDescription()).length()>0) {
							orderDetailModel.setShipMethod(orderResponseGetShipMethod.getShipViaDescription());
						}
						if(CommonUtility.validateString(orderResponseGetShipMethod.getShipViaCode()).length()>0) {
							orderDetailModel.setShipMethodId(orderResponseGetShipMethod.getShipViaCode());
						}
						orderDetailModel.setCarrierTrackingNumber(orderResponseGetShipMethod.getCarrierTrackingNumber());
						orderDetailModel.setShipViaErpId(orderResponseGetShipMethod.getShipViaErpId());
						orderDetailModel.setAccountNumber(orderResponseGetShipMethod.getAccountNumber());
						if(CommonUtility.customServiceUtility()!=null) {
							CommonUtility.customServiceUtility().getMultipleCarrierTrackingNumber(shipMethod, orderDetailModel);
					 	}
					}
				}
				//orderDetailModel.setPoNumber(order.getCustomerPoNumber());
				orderDetailModel.setTax(order.getTaxAmount()!=null?order.getTaxAmount():0);
				if(order.getFreight()!=null){
						orderDetailModel.setFreight(order.getFreight());
				}
				orderDetailModel.setSubtotal(order.getOrderSubTotal()!=null?order.getOrderSubTotal():0);
				orderDetailModel.setDiscount(order.getDiscountAmount()!=null?order.getDiscountAmount():0);
				orderDetailModel.setSubtotalV2(order.getBackOrderTotal());
				orderDetailModel.setPreAuthorize(order.getPreAuthorize());
				orderDetailModel.setSalesPersonCode(order.getSalesPersonCode());
				orderDetailModel.setCustomerName(order.getCustomerName());
				orderDetailModel.setShippingInstruction(order.getShippingInstruction());
				orderDetailModel.setOrderComment(order.getOrderComment());
				orderDetailModel.setWarehouseLocation(order.getWarehouseLocation());
				orderDetailModel.setOtherCharges(order.getOtherCharges());
				double orderTotal = order.getOrderTotal()!=null?order.getOrderTotal():0;
				orderDetailModel.setTotal(orderTotal);
				orderDetailModel.setrOEDiscount(order.getRoeDiscountAmount()!=null?order.getRoeDiscountAmount():0);
				orderDetailModel.setPaymentAmount(order.getOtherAmount()!=null?order.getOtherAmount():0);
				orderDetailModel.setCashDiscountPercentage(order.getDiscountPercentage()!=null?order.getDiscountPercentage():0);
				if(order.getReleaseNumber()!=null)
				orderDetailModel.setCustomerReleaseNumber(order.getReleaseNumber());
				if(order.getDiscountDate()!=null)
				orderDetailModel.setDiscountDate(order.getDiscountDate());
				 if(order.getOrderNotes()!=null && !(order.getOrderNotes().isEmpty()))
				     orderDetailModel.setOrderNotes(order.getOrderNotes());	
				 if(order.getDocumentDetail()!=null){
					 Cimm2BCentralDocumentDetail documentDetail=new Cimm2BCentralDocumentDetail();
					 documentDetail.setDocContentType(order.getDocumentDetail().getDocContentType());
					 documentDetail.setDocumentDesc(order.getDocumentDetail().getDocumentDesc());
					 documentDetail.setDocumentId(order.getDocumentDetail().getDocumentId());
					 documentDetail.setDocumentName(order.getDocumentDetail().getDocumentName());
					 orderDetailModel.setDocumentDetail(documentDetail);
				 }
				 if(CommonUtility.validateString(order.getDeliveryDate()).length()>0) {
						orderDetailModel.setDeliveryDate(order.getDeliveryDate());
					}
				 if(CommonUtility.validateString(order.getDeliveryTime()).length()>0) {
						orderDetailModel.setDeliveryTime(order.getDeliveryTime());
					}
				 if(CommonUtility.validateString(order.getUser18()).length()>0) {
						orderDetailModel.setUser18(order.getUser18());
					}
				 if(CommonUtility.customServiceUtility()!=null) {
						CommonUtility.customServiceUtility().getTotalFrieghtAddonCharges(order, orderDetailModel);
				 	}
				 if(CommonUtility.validateString(order.getPaidDate()).length()>0) {
						orderDetailModel.setPaidDate(order.getPaidDate());
					}
				orderDetailModel.setRouteData(order.getRouteData());
				orderDetail.add(orderDetailModel);
	
				orderItemList = new ArrayList<SalesModel>();
	
				ArrayList<Cimm2BCentralLineItem> lineItemModels = order.getLineItems();
	
				if(lineItemModels != null && lineItemModels.size() > 0){
					String handlingListItems = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("handlingcharge.labels");
					String deliveryListItems = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("deliverycharge.labels");
					List<String> handlingListItemsArray = Arrays.asList(CommonUtility.validateString(handlingListItems).split(","));
					List<String> deliveryListItemsArray = Arrays.asList(CommonUtility.validateString(deliveryListItems).split(","));
					for(Cimm2BCentralLineItem itemModel : lineItemModels){
						SalesModel model = new SalesModel();
						if(handlingListItemsArray.contains(itemModel.getPartNumber()) || deliveryListItemsArray.contains(itemModel.getPartNumber())){
							model.setDisableAddToCart(true);
						}
						model.setCustomerPartNumber(itemModel.getCustomerPartNumber());
						model.setExtPrice(itemModel.getExtendedPrice()!=null?itemModel.getExtendedPrice():0);
						model.setItemId(CommonUtility.validateNumber(itemModel.getItemId()));
						model.setAltPartNumber(itemModel.getItemId());
						model.setLineItemComment(itemModel.getLineItemComment());
						if(itemModel.getLineNumber()!=null){
						model.setLineNumber(itemModel.getLineNumber());}
						model.setManufacturer(itemModel.getManufacturer());
						model.setManufacturerPartNumber(itemModel.getManufacturerPartNumber());
						model.setPartNumber(itemModel.getPartNumber());
						
						model.setQtyordered(itemModel.getQty());
						if(itemModel.getQtyShipped()!=null){
						model.setQtyShipped(itemModel.getQtyShipped().intValue());
						}
						model.setShortDesc(itemModel.getShortDescription());
						model.setUnitPrice(itemModel.getUnitPrice() != null ? itemModel.getUnitPrice() : 0);
						model.setListPrice(itemModel.getListPrice());
						model.setUom(itemModel.getUom());
						model.setCatalogNumber(itemModel.getCatalogNumber()!=null?itemModel.getCatalogNumber():"");
						model.setBackOrderQty(itemModel.getBackOrderQty());
						model.setQtyOpen(itemModel.getQtyOpen());
						model.setDispositionDescription(itemModel.getDispositionDescription());
						model.setOpenItemsNumber(itemModel.getOpenItemsNumber());
						orderItemList.add(model);
					}
				}
			}
	
			orderDetailInfo.put("OrderDetail", orderDetail);
			orderDetailInfo.put("OrderItemList", orderItemList);
		}catch (Exception e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return orderDetailInfo;
	}

	@SuppressWarnings("unchecked")
	public static SalesModel createQuote(LinkedHashMap<String, Object> createQuoteInput)
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		SalesModel quoteInfo = new SalesModel();
		ArrayList<Cimm2BCentralLineItem> lineItems = new ArrayList<Cimm2BCentralLineItem>();
		try{
			String warehouseCode = (String) createQuoteInput.get("wareHousecode");
			String customerErpId = (String) createQuoteInput.get("customerId");//String customerId = (String) createQuoteInput.get("userToken");
			String shippingInstruction = (String) createQuoteInput.get("shippingInstruction");
			String orderNotes = (String) createQuoteInput.get("orderNotes");
			String poNumber = (String) createQuoteInput.get("poNumber");
			String gasPoNumber = (String) createQuoteInput.get("gasPoNumber");
			String buyingCompanyId = (String) createQuoteInput.get("buyingCompanyId"); 
			String shipVia = (String) createQuoteInput.get("shipVia");
			String shipViaDisplay = (String) createQuoteInput.get("shipViaDisplay");
			String handlingListItems = (String) createQuoteInput.get("handlingListItems");
			String deliveryListItems = (String) createQuoteInput.get("deliveryListItems");
			String errorMessageToDisplay = (String) createQuoteInput.get("errorMsgToDisplay");
			String reqDate = (String) createQuoteInput.get("reqDate");
			double freightCharges = CommonUtility.validateDoubleNumber(createQuoteInput.get("totalCartFrieghtCharges").toString());
			double orderTotal = 0;
	
			String branchCode = ProductsDAO.getWarehouseCustomField(CommonUtility.validateNumber(buyingCompanyId), "LOCATION_CODE");
	
			UsersModel billAddress = (UsersModel) createQuoteInput.get("billAddress");
			UsersModel shipAddress = (UsersModel) createQuoteInput.get("shipAddress");

			ArrayList<ProductsModel> itemList = (ArrayList<ProductsModel>) createQuoteInput.get("itemList");
			if(itemList!=null && itemList.size()>0){
				for(ProductsModel itemModel : itemList){

					Cimm2BCentralLineItem lineItem = new Cimm2BCentralLineItem();
					lineItem.setPartNumber(itemModel.getPartNumber());
					if(itemModel.getItemId() == CommonUtility.validateNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID")))){
						lineItem.setPartNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_PARTNUMBER")));
					}
					lineItem.setQty(itemModel.getQty()); 
					lineItem.setUom(itemModel.getUom());
					lineItem.setManufacturer(itemModel.getManufacturerName());					
					//lineItem.setExtendedPrice(itemModel.getUnitPrice() * itemModel.getQty());
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CALCULATE_UOM_BASED_PRICE")).equalsIgnoreCase("Y")){
						
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("OVERRIDE_PRICE_RULE")).equals("N")) {
							lineItem.setExtendedPrice((itemModel.getUnitPrice()/itemModel.getUomQuantity()) * itemModel.getQty() * itemModel.getUomQty());
							lineItem.setUnitPrice(itemModel.getUnitPrice());
							if(itemModel.getUomPrice() > 0.0) {
								lineItem.setExtendedPrice(itemModel.getUomPrice() * itemModel.getQty());
								lineItem.setUnitPrice(itemModel.getUomPrice());
							}
						}
						else{									
					}
						
					}
					else {
						lineItem.setExtendedPrice(itemModel.getUnitPrice() * itemModel.getQty());
						lineItem.setUnitPrice(itemModel.getUnitPrice());
					}
					lineItem.setUomQty(Integer.toString(itemModel.getUomQty()));	
					if(CommonUtility.customServiceUtility()!=null) {
						lineItem =  CommonUtility.customServiceUtility().disableUmqtyToErp(lineItem);
					}
					//lineItem.setUnitPrice(itemModel.getUnitPrice());					
					lineItem.setCustomerPartNumber(itemModel.getCustomerPartNumber());
					lineItem.setLineItemComment(itemModel.getLineItemComment());
					lineItem.setShippingBranch(warehouseCode);	
					//Code written for Turtel & Hughes by Nitish K
					if(CommonUtility.customServiceUtility()!=null) {						
						double extndPrice = CommonUtility.customServiceUtility().getExtendedPrice(itemModel.getUnitPrice(),itemModel.getQty(),itemModel.getSaleQty(),itemModel.getSalesQuantity());
						if(itemModel.getWarehouseErrorMsg()!=null && itemModel.getWarehouseErrorMsg().equalsIgnoreCase(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("itemsetup.error.warhouse")))){
							lineItem.setNonStockFlag(true);
						}
						if(extndPrice > 0) {
							lineItem.setExtendedPrice(extndPrice);
							lineItem.setUom("");
						}else{
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CALCULATE_UOM_BASED_PRICE")).equalsIgnoreCase("Y")){
								if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("OVERRIDE_PRICE_RULE")).equals("N")) {
									lineItem.setExtendedPrice((itemModel.getUnitPrice()/itemModel.getUomQuantity()) * itemModel.getQty() * itemModel.getUomQty());
									lineItem.setUnitPrice(itemModel.getUnitPrice());
									if(itemModel.getUomPrice() > 0.0) {
										lineItem.setExtendedPrice(itemModel.getUomPrice() * itemModel.getQty());
										lineItem.setUnitPrice(itemModel.getUomPrice());
									}
								}
								else{									
							}			
						}
							else {
								lineItem.setExtendedPrice(itemModel.getUnitPrice() * itemModel.getQty());
								lineItem.setUnitPrice(itemModel.getUnitPrice());
							}
						}
						
					}
					CommonUtility.customServiceUtility().getListPrice(itemModel,lineItem); // Geary Pacific Supply
					
					lineItems.add(lineItem);
					
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CALCULATE_UOM_BASED_PRICE")).equalsIgnoreCase("Y")){
						if(itemModel.getUomPrice() > 0.0) {
							orderTotal = orderTotal + (itemModel.getUomPrice() * itemModel.getQty());
						}else {
							orderTotal = orderTotal + ((itemModel.getUnitPrice()/itemModel.getUomQuantity()) * itemModel.getQty() * itemModel.getUomQty());
						}
						}
						else {
							orderTotal = orderTotal + lineItem.getExtendedPrice();
						}
				}

				Cimm2BCentralAddress cimm2BCentralBillingAddress = Cimm2BCentralClient.getInstance().ecomUserModelToCimm2BCentralAddress(billAddress);
				Cimm2BCentralAddress cimm2BCentralShipingAddress = Cimm2BCentralClient.getInstance().ecomUserModelToCimm2BCentralAddress(shipAddress);

				Cimm2BCentralOrder order = new Cimm2BCentralOrder();

				order.setCustomerERPId(customerErpId);
				order.setBranchCode(branchCode);
				if(CommonUtility.customServiceUtility()!=null){
					CommonUtility.customServiceUtility().getBranchCode(order,warehouseCode);
				}
				order.setWarehouseLocation(warehouseCode);
				order.setLineItems(lineItems);
				if(CommonUtility.customServiceUtility()!=null)
				{
					CommonUtility.customServiceUtility().UniquePoNumberHide(order);
				}
				else
				{
					order.setCustomerPoNumber(poNumber);
                }
				order.setShippingInstruction(shippingInstruction);
				order.setOrderComment(orderNotes);
				order.setBillingAddress(cimm2BCentralBillingAddress);
				order.setShippingAddress(cimm2BCentralShipingAddress);
				order.setGasPoNumber(gasPoNumber);
				order.setFreight(freightCharges);
				order.setOrderTotal(orderTotal + freightCharges);
				order.setRequiredDate(reqDate);
				order.setSalesLocationId(warehouseCode);
				order.setBranchCode(CommonUtility.validateString(shipAddress.getBranchID()).length()>0?CommonUtility.validateString(shipAddress.getBranchID()):warehouseCode);
				if(CommonUtility.validateString(shipVia).length() > 0){
					Cimm2BCentralShipVia cimm2bCentralShipVia = new Cimm2BCentralShipVia();
					cimm2bCentralShipVia.setShipViaCode(shipVia);
					cimm2bCentralShipVia.setShipViaDescription(shipViaDisplay);
					ArrayList<Cimm2BCentralShipVia> shipviaArray = new ArrayList<Cimm2BCentralShipVia>();
					shipviaArray.add(cimm2bCentralShipVia);
					order.setShipVia(shipviaArray);
				}
				
				String CREATE_SHELL_ORDER = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_SHELL_ORDER_API"));			
				Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_SHELL_ORDER, HttpMethod.POST, order, Cimm2BCentralOrder.class);

				Cimm2BCentralOrder orderResponse = null;
				if(orderResponseEntity!=null && orderResponseEntity.getData() != null){
					orderResponse = (Cimm2BCentralOrder) orderResponseEntity.getData();
					orderResponse.setStatus(orderResponseEntity.getStatus());
					if(orderResponse != null && ((CommonUtility.validateString(orderResponse.getOrderNumber()).length()> 0) || (CommonUtility.validateString(orderResponse.getCartId()).length()>0))){
						quoteInfo.setExternalCartId(orderResponse.getCartId());
						quoteInfo.setOrderNum(orderResponse.getOrderNumber());
						quoteInfo.setErpOrderNumber(orderResponse.getOrderERPId());
						quoteInfo.setQuoteNumber(orderResponse.getOrderNumber());
						quoteInfo.setTax(orderResponse.getTaxAmount());
						if(CommonUtility.customServiceUtility() != null) {
							CommonUtility.customServiceUtility().addExchangeRateToTax(session,quoteInfo,orderResponse);// Electrozad Custom Service
						}
						if(orderResponse.getFreight()!=null){
							quoteInfo.setFreight(orderResponse.getFreight());
						}
						if(orderResponse.getOrderTotal()>0){
							quoteInfo.setTotal(orderResponse.getOrderTotal() + freightCharges);
							if(CommonUtility.customServiceUtility() != null) {
								CommonUtility.customServiceUtility().addExchangeRateToTotal(session,quoteInfo,orderResponse,freightCharges);// Electrozad Custom Service
							}
						}
						quoteInfo.setSubtotal(orderResponse.getOrderSubTotal());
						
						ArrayList<Cimm2BCentralLineItem> cimm2bCentralLineItem = orderResponse.getLineItems();
						if(cimm2bCentralLineItem != null && cimm2bCentralLineItem.size() > 0){
							double handlingCharges = 0.0;
							double deliveryCharges = 0.0;
							ArrayList<ProductsModel> nonCatalogItems = new ArrayList<ProductsModel>();
							ArrayList<SalesModel> lineItemsFromResponse = new ArrayList<SalesModel>();
							HashMap<String, SalesModel> lineItemsFromResponseCheckNonCatalogItems = new HashMap<String, SalesModel>();
							for (int i = 0; i < cimm2bCentralLineItem.size(); i++) {

								SalesModel model = new SalesModel();
								
								//model.setLineNumber(cimm2bCentralLineItem.get(i).getLineNumber());
								model.setLineNumber(cimm2bCentralLineItem.get(i).getLineNumber()!=null?cimm2bCentralLineItem.get(i).getLineNumber():0);
								model.setLineItemComment(cimm2bCentralLineItem.get(i).getLineItemComment());
								model.setItemId(CommonUtility.validateNumber(cimm2bCentralLineItem.get(i).getItemId()!=null?cimm2bCentralLineItem.get(i).getItemId():""));
								model.setPartNumber(cimm2bCentralLineItem.get(i).getPartNumber());
								model.setCustomerPartNumber(cimm2bCentralLineItem.get(i).getCustomerPartNumber());
								model.setShortDesc(cimm2bCentralLineItem.get(i).getShortDescription()!=null?cimm2bCentralLineItem.get(i).getShortDescription():"");
								//model.setUnitPrice(cimm2bCentralLineItem.get(i).getUnitPrice());
								model.setUnitPrice(cimm2bCentralLineItem.get(i).getUnitPrice()!=null?cimm2bCentralLineItem.get(i).getUnitPrice():0.0);
								//model.setExtPrice(cimm2bCentralLineItem.get(i).getExtendedPrice());
								model.setExtPrice(cimm2bCentralLineItem.get(i).getExtendedPrice()!=null?cimm2bCentralLineItem.get(i).getExtendedPrice():0.0);
								model.setQtyordered(cimm2bCentralLineItem.get(i).getQty());
								//model.setQtyShipped(cimm2bCentralLineItem.get(i).getQtyShipped().intValue());
								model.setQtyShipped(cimm2bCentralLineItem.get(i).getQtyShipped()!=null?cimm2bCentralLineItem.get(i).getQtyShipped().intValue():0);
								model.setUom(cimm2bCentralLineItem.get(i).getUom());
								model.setQtyUom(CommonUtility.validateString(cimm2bCentralLineItem.get(i).getUomQty()!=null?cimm2bCentralLineItem.get(i).getUomQty():"0"));
								model.setManufacturer(cimm2bCentralLineItem.get(i).getManufacturer());
								model.setManufacturerPartNumber(cimm2bCentralLineItem.get(i).getManufacturer()!=null?cimm2bCentralLineItem.get(i).getManufacturer():"");

								lineItemsFromResponse.add(model);
								lineItemsFromResponseCheckNonCatalogItems.put(cimm2bCentralLineItem.get(i).getPartNumber(), model);
							}

							for (ProductsModel orderItem : itemList) {
								lineItemsFromResponseCheckNonCatalogItems.remove(orderItem.getPartNumber());
							}
							
							if(handlingListItems.length() > 0 || deliveryListItems.length() > 0){
								List<String> handlingListItemsArray = Arrays.asList(handlingListItems.split(","));
								List<String> deliveryListItemsArray = Arrays.asList(deliveryListItems.split(","));

								Iterator<Entry<String, SalesModel>> it = lineItemsFromResponseCheckNonCatalogItems.entrySet().iterator();
								while (it.hasNext()) {
									Map.Entry<String, SalesModel> pair = (Map.Entry<String, SalesModel>)it.next();

									if(handlingListItemsArray.contains(pair.getValue().getPartNumber())){
										handlingCharges = handlingCharges + pair.getValue().getExtPrice();
									}else if(deliveryListItemsArray.contains(pair.getValue().getPartNumber())){
										deliveryCharges = deliveryCharges + pair.getValue().getExtPrice();
									}

									ProductsModel nonCatalogItem = new ProductsModel();

									//nonCatalogItem.setLineNumber(cimm2bCentralLineItem.get(i).getLineNumber());
									nonCatalogItem.setLineItemComment(pair.getValue().getLineItemComment());
									nonCatalogItem.setItemId(pair.getValue().getItemId());
									nonCatalogItem.setPartNumber(pair.getValue().getPartNumber());
									nonCatalogItem.setCustomerPartNumber(pair.getValue().getCustomerPartNumber());
									nonCatalogItem.setShortDesc(pair.getValue().getShortDesc()!=null? pair.getValue().getShortDesc():"");
									nonCatalogItem.setUnitPrice(pair.getValue().getUnitPrice());
									nonCatalogItem.setExtendedPrice( pair.getValue().getExtPrice());
									nonCatalogItem.setQty(pair.getValue().getQtyShipped());
									//model.setQtyShipped(pair.getValue().getQtyShipped().intValue());
									nonCatalogItem.setUom(pair.getValue().getUom());
									nonCatalogItem.setQtyUOM(CommonUtility.validateString(pair.getValue().getQtyUom()!=null? pair.getValue().getQtyUom():"0"));
									nonCatalogItem.setManufacturerName(pair.getValue().getManufacturer());
									nonCatalogItem.setManufacturerPartNumber(pair.getValue().getManufacturer()!=null? pair.getValue().getManufacturer():"");

									nonCatalogItems.add(nonCatalogItem);

									it.remove(); // avoids a ConcurrentModificationException
								}

							}

							quoteInfo.setDeliveryCharge(deliveryCharges);
							quoteInfo.setHandling(handlingCharges);
							quoteInfo.setNonCatalogItem(nonCatalogItems);
							quoteInfo.setOrderList(lineItemsFromResponse);
						}

						String GET_CUSTOMER_CARDS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_CARDS")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  customerErpId;
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_CARDS")).length() > 0) {
							Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_CUSTOMER_CARDS, HttpMethod.GET, null, Cimm2BCentralCustomerCard.class);
							List<Cimm2BCentralCustomerCard> customerDetails = null;
							
							if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  
								ArrayList<CreditCardModel> creditCardList = new ArrayList<CreditCardModel>();
								customerDetails = (List<Cimm2BCentralCustomerCard>) customerDetailsResponseEntity.getData();
	
								if(customerDetails != null && customerDetails.size() > 0){
									for(Cimm2BCentralCustomerCard customerCard : customerDetails){
										CreditCardModel creditCardModel = new CreditCardModel();
										creditCardModel.setCardHolder(customerCard.getCardHolderName());
										creditCardModel.setCreditCardType(customerCard.getCreditCardType());
										creditCardModel.setCreditCardNumber(customerCard.getCreditCardNumber());
										creditCardModel.setCreditCardCvv2VrfyCode(customerCard.getCvv());
										creditCardModel.setElementPaymentAccountId(customerCard.getPaymentAccountId());
										creditCardModel.setExpDate(customerCard.getExpiryDate());
	
										creditCardList.add(creditCardModel);
									}
									quoteInfo.setCreditCardList(creditCardList);
								}
							}
						}
					}else{
						if(orderResponse.getTaxAmount() != null && orderResponse.getTaxAmount() > 0.0){
							quoteInfo.setTax(orderResponse.getTaxAmount());
						}else{
							quoteInfo.setTax(orderResponse.getSalesTax()!=null?orderResponse.getSalesTax():0.0);
						}
						if(orderResponse.getFreight()!=null){
							quoteInfo.setFreight(orderResponse.getFreight());
						}
						if(orderResponse.getOtherCharges()>0){
							quoteInfo.setOtherCharges(orderResponse.getOtherCharges());
						}
						if(orderResponse.getOrderTotal()>0){
							quoteInfo.setTotal(orderResponse.getOrderTotal() + freightCharges);
						}
						if(CommonUtility.customServiceUtility() != null) {
							CommonUtility.customServiceUtility().dontaddfreightcharges(quoteInfo,orderResponse);
						}
						quoteInfo.setSubtotal(orderResponse.getOrderSubTotal());
					}
				}else{
					//quoteInfo.setStatusDescription(orderResponseEntity.getStatus().getCode() + " - " + orderResponseEntity.getStatus().getMessage());
					quoteInfo.setStatusDescription(errorMessageToDisplay);
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return quoteInfo;
	}
	public ArrayList<SalesModel> reorderPadInquiry(SalesModel salesInputParameter) {
		ArrayList<SalesModel> ReorderPadItemList = new ArrayList<SalesModel>();
		//orderList = SalesDAO.getOrdersHistory(salesInputParameter);
		try{
			String GET_ORDERED_ITEMS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ORDERED_ITEMS")); 
			Cimm2BCentralSalesOrderedItemsRequest salesOrderItemsRequest = new Cimm2BCentralSalesOrderedItemsRequest();
			//salesOrderItemsRequest.setCustomerERPId(salesInputParameter.getEntityId());
			salesOrderItemsRequest.setCustomerERPId(salesInputParameter.getEntityId());
			Cimm2BCentralResponseEntity salesOrderedItemsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_ORDERED_ITEMS, HttpMethod.POST, salesOrderItemsRequest, Cimm2BCentralSalesOrderItems.class);
			Cimm2BCentralSalesOrderItems salesOrderItemsDetails = null;    
			if(salesOrderedItemsResponseEntity!=null && salesOrderedItemsResponseEntity.getData() != null){
				salesOrderItemsDetails = (Cimm2BCentralSalesOrderItems) salesOrderedItemsResponseEntity.getData();
				if(salesOrderItemsDetails != null) {     
					ArrayList<Cimm2BCentralLineItem> itemList = salesOrderItemsDetails.getLineItems();
					if(itemList != null && itemList.size()>0) {
						for(Cimm2BCentralLineItem orderedItems : itemList){
							SalesModel salesModel = new SalesModel();
							salesModel.setPartNumber(orderedItems.getPartNumber());
							salesModel.setManufacturerPartNumber(orderedItems.getMPN());
							salesModel.setShortDesc(orderedItems.getShortDescription());
							salesModel.setQtyordered(orderedItems.getQty());
							salesModel.setUom(orderedItems.getUom());
							salesModel.setQtyUom(orderedItems.getUomQty()); 
							salesModel.setOrderLastDate(orderedItems.getOrderLastDate());
							salesModel.setOrderLastQty(orderedItems.getOrderLastQty());
							salesModel.setStatus(orderedItems.getStatus());
							ReorderPadItemList.add(salesModel);
						}
					}
				}
			}
		}
		catch (Exception e){ 
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return ReorderPadItemList;
	}

	public LinkedHashMap<String, Object> getTaxFromERP(LinkedHashMap<String, Object> salesInputParameter) {
		LinkedHashMap<String, Object> getTaxDetails = null;
		Cimm2BCentralResponseEntity response = null;
		ArrayList<ProductsModel> cartListData = (ArrayList<ProductsModel>) salesInputParameter.get("itemList");
		AddressModel overrideShipAddress = (AddressModel) salesInputParameter.get("overrideShipAddress");
		String wareHouse = CommonUtility.validateString(salesInputParameter.get("wareHousecode").toString());
		String poNumber = "";
		if(CommonUtility.validateString(salesInputParameter.get("poNumber").toString()).length()>0) {
		poNumber = CommonUtility.validateString(salesInputParameter.get("poNumber").toString());
		}
		response = AvalaraUtility.getInstance().getTax(cartListData, overrideShipAddress, wareHouse, poNumber);
		if(response != null && response.getData() != null)
		{
			getTaxDetails = new LinkedHashMap<String, Object>();
			TaxResponse taxresponse = (TaxResponse) response.getData();
			if(taxresponse != null){
				SalesModel salesModel = new SalesModel();
				salesModel.setTax(taxresponse.getTotalTax());
				salesModel.setTotal(taxresponse.getTotalAmount());
				salesModel.setCashDiscountAmount(taxresponse.getTotalDiscount());
				getTaxDetails.put("TaxInfo", salesModel);
			}
			
		}
		return getTaxDetails;
	}
	
	public static ArrayList<SalesModel> OpenOrderInfoBydates(SalesModel salesInputParameter) {
		ArrayList<SalesModel> openOrderList = new ArrayList<SalesModel>();
		try{
			String customerErpId = salesInputParameter.getCustomerNumber();
			String fromDate =salesInputParameter.getStartDate();
			String toDate = salesInputParameter.getEndDate();
			String GET_OPEN_ORDERS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_OPEN_ORDERS"));
			Cimm2BOpenOrdersRequest  OpenOrdersRequest=new Cimm2BOpenOrdersRequest();
			OpenOrdersRequest.setCustomerERPId(customerErpId);
			OpenOrdersRequest.setFromDate(fromDate);
			OpenOrdersRequest.setToDate(toDate);
			OpenOrdersRequest.setWarehouseLocation(salesInputParameter.getWarehouseLocation());
	
			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_OPEN_ORDERS, "POST", OpenOrdersRequest, Cimm2BCentralOpenOrderInformationDetails.class);
	
			List<Cimm2BCentralOpenOrderInformationDetails> Cimm2BCentralOpenOrderInformationDetails = null;
			
			if(orderResponseEntity!=null && orderResponseEntity.getData() != null){
				Cimm2BCentralOpenOrderInformationDetails = (List<Cimm2BCentralOpenOrderInformationDetails>) orderResponseEntity.getData();
				if(Cimm2BCentralOpenOrderInformationDetails != null){
					for(Cimm2BCentralOpenOrderInformationDetails openOrder : Cimm2BCentralOpenOrderInformationDetails){
						SalesModel salesModel = new SalesModel();
						salesModel.setOrderNum(openOrder.getOrderNumber());
						if(openOrder.getShipDate()!=null && openOrder.getShipDate().length()>0){
							salesModel.setShipDate(openOrder.getShipDate());
						}
						if(openOrder.getOrderStatus()!=null && openOrder.getOrderStatus().length()>0){
							salesModel.setStatus(openOrder.getOrderStatus());
						}
						if(openOrder.getPoNumber()!=null){
							salesModel.setPoNumber(openOrder.getPoNumber()!=null?openOrder.getPoNumber().trim():"");
						}else{
							salesModel.setPoNumber(openOrder.getCustomerPoNumber());
						}
						salesModel.setOrderDate(openOrder.getOrderDate());
						salesModel.setManufacturer(openOrder.getSupplier());
						salesModel.setPartNumber(openOrder.getPart());
						salesModel.setDescription1(openOrder.getDescription1());
						salesModel.setDescription2(openOrder.getDescription2());
						salesModel.setDescription(openOrder.getLocation()); // ---- For location field 
						if(openOrder.getOrderTotal()!=null && openOrder.getOrderTotal()>0.0){
							salesModel.setTotal(openOrder.getOrderTotal()!=null?openOrder.getOrderTotal():0);
						}else{
							salesModel.setTotal(openOrder.getTotalInvoiceAmount()!=null?openOrder.getTotalInvoiceAmount():0);
						}
						salesModel.setInvoiceNumber(openOrder.getInvoiceNumber());
						salesModel.setShipToName(openOrder.getShipToName());
						salesModel.setShipToId(openOrder.getShipToId()!=null?openOrder.getShipToId():"");
						salesModel.setOrderType(openOrder.getOrderType()!=null?openOrder.getOrderType():"");
						salesModel.setOpenOrderAmount(openOrder.getOrderTotal()!=null?openOrder.getOrderTotal():0);
						salesModel.setOrderedBy(openOrder.getOrderedBy());
						salesModel.setShipDate(openOrder.getShipDate());
						salesModel.setOrderStatus(openOrder.getOrderStatus());
						salesModel.setOrderStatusType(openOrder.getOrderTypeCode());
						openOrderList.add(salesModel);
					}
				}
				//openOrderList.add(pages);
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return openOrderList;
		}
	
	public static ArrayList<SalesModel> invoicedOrdersFromOrderHistory(SalesModel salesInputParameter) {
		ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
		try{
			Integer orderNo = null;
			String customerErpId = salesInputParameter.getCustomerNumber();
			String startDate = salesInputParameter.getStartDate();
			String endDate = salesInputParameter.getEndDate();
			String sortOrder = salesInputParameter.getSortDirection();
		   
			String GET_INVOICES_LIST = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_INVOICES_LIST")) + "?"  + Cimm2BCentralRequestParams.customerERPId + "=" + customerErpId+ "&"  +Cimm2BCentralRequestParams.startDate + "=" + startDate+ "&"  +Cimm2BCentralRequestParams.endDate + "=" + endDate +"&" +Cimm2BCentralRequestParams.sortDirection + "=" + sortOrder;

			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_INVOICES_LIST, "GET", null, Cimm2BCentralGetInvoiceList.class);
	
			if(salesInputParameter.getOrderNum()!=null && salesInputParameter.getOrderNum().trim().length()>0){
				orderNo = CommonUtility.validateNumber(salesInputParameter.getOrderNum());
			}
	
			List<Cimm2BCentralGetInvoiceList> cimm2BCentralInvoiceList = null;
			if(orderResponseEntity!=null && orderResponseEntity.getData() != null){
				cimm2BCentralInvoiceList = (ArrayList<Cimm2BCentralGetInvoiceList>) orderResponseEntity.getData();
				if(cimm2BCentralInvoiceList != null && cimm2BCentralInvoiceList.size() > 0){
					for(Cimm2BCentralGetInvoiceList invoiceList : cimm2BCentralInvoiceList){
						SalesModel salesModel = new SalesModel();
						salesModel.setInvoiceNumber(invoiceList.getInvoiceNumber());
						salesModel.setOrderNum(invoiceList.getOrderNumber());
						salesModel.setTotal(invoiceList.getTotalAmount());
						salesModel.setTax(invoiceList.getTax());
						salesModel.setPoNumber(invoiceList.getPoNumber());
						salesModel.setFreight(invoiceList.getFreightAmount());
						salesModel.setDocumentLinks(invoiceList.getDocumentLinks());
						salesModel.setLineItemComment(invoiceList.getLineItems());
						salesModel.setInvoiceDate(invoiceList.getInvoicedDate());
						salesModel.setOrderDate(invoiceList.getOrderedDate());
						salesModel.setDueDate(invoiceList.getDueDate());
						salesModel.setTransactionCode(invoiceList.getTransactionCodeType());
						salesModel.setTransactionType(invoiceList.getTransactionType());
						orderList.add(salesModel);
					}
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return orderList;
	}	
	public static ArrayList<SalesModel> invoicedOrderDetailFromOrderHistory(SalesModel salesInputParameter) {
		ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
		ArrayList<SalesModel> orderItemList = new ArrayList<SalesModel>();
		Cimm2BCentralResponseEntity orderResponseEntity = null;
		Cimm2BCentralInvoiceDetail arBalance=null;
		try{
			Cimm2BCentralARBalanceSummaryRequest invoiceDetailRequest = new Cimm2BCentralARBalanceSummaryRequest();
			invoiceDetailRequest.setCustomerERPId(salesInputParameter.getCustomerNumber());
			invoiceDetailRequest.setInvoiceNumber(salesInputParameter.getOrderNum());
			String url = CommonDBQuery.getSystemParamtersList().get("GET_INVOICE_DETAIL");
			orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(url, "POST", invoiceDetailRequest, Cimm2BCentralInvoiceDetail.class);
			
	if(orderResponseEntity!=null && orderResponseEntity.getData() != null && orderResponseEntity.getStatus() != null && orderResponseEntity.getStatus().getCode() == HttpStatus.SC_OK){
				
				arBalance = (Cimm2BCentralInvoiceDetail)orderResponseEntity.getData();
				
				if(arBalance!=null){
					SalesModel salesModel=new SalesModel();
					salesModel.setInvoiceNumber(arBalance.getInvoiceNumber());
					salesModel.setOrderDate(arBalance.getOrderedDate());
					salesModel.setInvoiceDate(arBalance.getInvoicedDate());
					salesModel.setOrderNum(arBalance.getOrderNumber());
					salesModel.setTax(arBalance.getTax());
					salesModel.setTotal(arBalance.getTotalAmount());
					salesModel.setFreightAmount(arBalance.getFreightAmount());
					salesModel.setBillToName(arBalance.getBillToAddress().getCompanyName());
					salesModel.setBillAddress1(arBalance.getBillToAddress().getAddressLine1());
					salesModel.setBillCity(arBalance.getBillToAddress().getCity());
					salesModel.setBillState(arBalance.getBillToAddress().getState());
					salesModel.setBillZipCode(arBalance.getBillToAddress().getZipCode());
					salesModel.setShipToName(arBalance.getShipToAddress().getCompanyName());
					salesModel.setShipAddress1(arBalance.getShipToAddress().getAddressLine1());
					salesModel.setShipCity(arBalance.getShipToAddress().getCity());
					salesModel.setShipState(arBalance.getShipToAddress().getState());
					salesModel.setShipZipCode(arBalance.getShipToAddress().getZipCode());
					salesModel.setPoNumber(arBalance.getPoNumber());
					salesModel.setDueDate(arBalance.getDueDate());
					salesModel.setSalesPersonCode(arBalance.getPrimarySalesRepId());
					
					ArrayList<Cimm2BCentralLineItem> arLineItemList = arBalance.getLineItems();
					if(arLineItemList!=null && arLineItemList.size()>0){
						for(Cimm2BCentralLineItem arLineItems :arLineItemList) {
							SalesModel saleModel=new SalesModel();
							saleModel.setItemId(CommonUtility.validateNumber(arLineItems.getItemId()));
							saleModel.setPartNumber(arLineItems.getPartNumber());
							saleModel.setShortDesc(arLineItems.getShortDescription());
							saleModel.setUnitPrice(arLineItems.getUnitPrice());
							saleModel.setExtPrice(arLineItems.getExtendedPrice());
							saleModel.setUom(arLineItems.getUom());
							saleModel.setQtyUom(arLineItems.getUomQty());
							saleModel.setQtyordered(arLineItems.getQty());
							
							orderItemList.add(saleModel);
						}
						salesModel.setOrderList(orderItemList);
					}
					orderList.add(salesModel);
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return orderList;
	}
	public static SalesModel createPaypalPayment(SalesModel salesInputParameter) {
		
		SalesModel salesOutputParameter =  null;
		
		try {
			String CREATE_PAYPAL_PAYMENT = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_PAYPAL_PAYMENT"));
			
			ArrayList<Cimm2BCentralPaypalItemList> lineItems = new ArrayList<Cimm2BCentralPaypalItemList>();
			if(salesInputParameter.getCartData() != null && salesInputParameter.getCartData().size() > 0){
				for(ProductsModel items : salesInputParameter.getCartData()){
					Cimm2BCentralPaypalItemList cimm2bCentralItemList = new Cimm2BCentralPaypalItemList();
					cimm2bCentralItemList.setQuantity(items.getQty());
					cimm2bCentralItemList.setName(items.getPartNumber());
					cimm2bCentralItemList.setSku(items.getPartNumber());
					cimm2bCentralItemList.setPrice(items.getPrice());

					lineItems.add(cimm2bCentralItemList);
				}
			}

			String currency = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYPAL_CURRENCY_TYPE"));
			String description = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYPAL_DESCRIPTION"));
			Cimm2BCentralPaypalRequest cimm2BCPaypalRequest = new Cimm2BCentralPaypalRequest();
			cimm2BCPaypalRequest.setCurrency(currency);
			cimm2BCPaypalRequest.setDescription(description);
			cimm2BCPaypalRequest.setShipping(salesInputParameter.getFreight());
			cimm2BCPaypalRequest.setItemList(lineItems);
			cimm2BCPaypalRequest.setTaxAmount(salesInputParameter.getTax());
			cimm2BCPaypalRequest.setDiscount(salesInputParameter.getDiscount());
			cimm2BCPaypalRequest.setTotalAmount(CommonUtility.validateParseDoubleToString(salesInputParameter.getTotal()));
			
			Cimm2BCentralResponseEntity paypalResponse = Cimm2BCentralClient.getInstance().getDataObject(CREATE_PAYPAL_PAYMENT, "POST", cimm2BCPaypalRequest, Cimm2BCentralPaypalResponse.class);
			
			Cimm2BCentralPaypalResponse cimm2BCPaypalResponse = null;
			
			if(paypalResponse!=null && paypalResponse.getStatus().getCode() == 200 && paypalResponse.getData() != null){
				cimm2BCPaypalResponse = (Cimm2BCentralPaypalResponse) paypalResponse.getData();
				if(cimm2BCPaypalResponse != null){
					salesOutputParameter =  new SalesModel();
					salesOutputParameter.setPaymentAuthCode(cimm2BCPaypalResponse.getPaymentId());
					salesOutputParameter.setReturnUrl(cimm2BCPaypalResponse.getRedirectUrl());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return salesOutputParameter;
	}
	public static SalesModel getAuthorizeDotNetProfileData(SalesModel salesInputParameter) {
		Cimm2BCentralCustomerCard CCprofileInfo =  new Cimm2BCentralCustomerCard();
		SalesModel salesOutputParameter =  null;
		String customerProfileID = (String) salesInputParameter.getProfileID();
		try {
			
			String AUTHORIZE_DOT_NET_GET_PROFILE = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHORIZE_DOT_NET_GET_PROFILE")) + "?" + customerProfileID;
			Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(AUTHORIZE_DOT_NET_GET_PROFILE, HttpMethod.GET, null, Cimm2BCentralCustomerCard.class);
			
			Cimm2BCentralAuthorizeCreditCardResponse cimm2BCentralAuthorizeCreditCardResponse = null;
			
			if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getStatus().getCode() == 200 && customerDetailsResponseEntity.getData() != null){
				cimm2BCentralAuthorizeCreditCardResponse = (Cimm2BCentralAuthorizeCreditCardResponse) customerDetailsResponseEntity.getData();
				Cimm2BCentralProfileList customerProfile = cimm2BCentralAuthorizeCreditCardResponse.getPaymentProfileList();
				CCprofileInfo.setCreditCardNumber(customerProfile.getCreditCardNumber());
				CCprofileInfo.setCreditCardType(customerProfile.getCreditCardType());
				CCprofileInfo.setExpiryMonth(customerProfile.getExpiryMonth());
				CCprofileInfo.setExpiryYear(customerProfile.getExpiryYear());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return salesOutputParameter;
	}
	
	public static SalesModel AuthorizeDotNetGetCustomerProfile(SalesModel salesInputParameter) {
		
		try {
			
			String customerProfileId = salesInputParameter.getCustomerProfileId();
			String AUTHORIZE_DOT_NET_GET_CUSTOMER_PROFILE = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHORIZE_DOT_NET_GET_CUSTOMER_PROFILE")) + customerProfileId;
			
			Cimm2BCentralResponseEntity authorizeDotNetResponse = Cimm2BCentralClient.getInstance().getDataObject(AUTHORIZE_DOT_NET_GET_CUSTOMER_PROFILE, HttpMethod.GET, "", Cimm2BCentralAuthorizeGetCustomerProfileResponse.class);
			
			Cimm2BCentralAuthorizeGetCustomerProfileResponse cimm2BCentralAuthorizeGetCustomerProfileResponse = null;
			
			if(authorizeDotNetResponse!=null && authorizeDotNetResponse.getData() != null){
				cimm2BCentralAuthorizeGetCustomerProfileResponse = (Cimm2BCentralAuthorizeGetCustomerProfileResponse) authorizeDotNetResponse.getData();
				
				salesInputParameter.setCustomerERPId(cimm2BCentralAuthorizeGetCustomerProfileResponse.getCustomerERPId());
				salesInputParameter.setCustomerProfileId(cimm2BCentralAuthorizeGetCustomerProfileResponse.getCustomerProfileId());
				
				List<Cimm2BCentralCustomerCard> paymentProfileList = cimm2BCentralAuthorizeGetCustomerProfileResponse.getPaymentProfileList();
				ArrayList<CreditCardModel> creditCardList = new ArrayList<CreditCardModel>();
				if(paymentProfileList!=null && paymentProfileList.size()>0){
					for(Cimm2BCentralCustomerCard cardDetails : paymentProfileList){
						CreditCardModel creditCardModel = new CreditCardModel();
						creditCardModel.setCreditCardNumber(cardDetails.getCreditCardNumber());
						creditCardModel.setCreditCardType(cardDetails.getCreditCardType());
						creditCardModel.setCreditCardExpiryMonth(cardDetails.getExpiryMonth());
						creditCardModel.setCreditCardExpiryYear(cardDetails.getExpiryYear());
						creditCardModel.setCustomerPaymentId(cardDetails.getCustomerPaymentIdList());
						if(cardDetails.getAddress()!=null ) {
						creditCardModel.setAddress1(cardDetails.getAddress().getAddressLine1()!=null?cardDetails.getAddress().getAddressLine1():"");
						creditCardModel.setState(cardDetails.getAddress().getState()!=null?cardDetails.getAddress().getState():"");
						creditCardModel.setCity(cardDetails.getAddress().getCity()!=null?cardDetails.getAddress().getCity():"");
						creditCardModel.setCountry(cardDetails.getAddress().getCountry()!=null?cardDetails.getAddress().getCountry():"");
						creditCardModel.setZipCode(cardDetails.getAddress().getZipCode()!=null?cardDetails.getAddress().getZipCode():"");
						}
						
						Gson g=new Gson();
						System.out.println("json data:"+g.toJson(cardDetails.getCustomerPaymentIdList()));
						creditCardList.add(creditCardModel);
					}
				}
				salesInputParameter.setCreditCardList(creditCardList);		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return salesInputParameter;
	}
	public static SalesModel AuthorizeCustomerProfileAuthentication(SalesModel salesInputParameter) {
		SalesModel salesOutputParameter =  null;
		try {
			
			String AUTHORIZE_CUSTOMER_PROFILE_AUTHENTICATION = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHORIZE_CUSTOMER_PROFILE_AUTHENTICATION"));
			
			Cimm2BCentralAuthorizeCreditCardRequest cimm2BCentralAuthorizeCreditCardRequest = new Cimm2BCentralAuthorizeCreditCardRequest();
			cimm2BCentralAuthorizeCreditCardRequest.setCustomerProfileId(CommonUtility.validateString(salesInputParameter.getCustomerProfileId()));
			cimm2BCentralAuthorizeCreditCardRequest.setPaymentAccountId(CommonUtility.validateString(salesInputParameter.getPaymentAccountId()));
			cimm2BCentralAuthorizeCreditCardRequest.setAmount(salesInputParameter.getAmount());
			cimm2BCentralAuthorizeCreditCardRequest.setCvv(salesInputParameter.getCvv());			
			Cimm2BCentralResponseEntity authorizeDotNetResponse = Cimm2BCentralClient.getInstance().getDataObject(AUTHORIZE_CUSTOMER_PROFILE_AUTHENTICATION, "POST", cimm2BCentralAuthorizeCreditCardRequest, Cimm2BCentralAuthorizeCreditCardResponse.class);
			Cimm2BCentralAuthorizeCreditCardResponse cimm2BCentralAuthorizeCreditCardResponse = null;
			
			if(authorizeDotNetResponse!=null && authorizeDotNetResponse.getData() != null){
				cimm2BCentralAuthorizeCreditCardResponse = (Cimm2BCentralAuthorizeCreditCardResponse) authorizeDotNetResponse.getData();
				if(cimm2BCentralAuthorizeCreditCardResponse != null){
					salesOutputParameter =  new SalesModel();
					salesOutputParameter.setAuthorizationCode(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getAuthorizationCode()));
					salesOutputParameter.setAvsResultCode(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getAvsResultCode()));
					salesOutputParameter.setCvvResultCode(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getCvvResultCode()));
					salesOutputParameter.setCavvResultCode(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getCavvResultCode()));
					salesOutputParameter.setTransactionId(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getTransactionId()));
					salesOutputParameter.setTransactionHash(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getTransactionHash()));
					salesOutputParameter.setPaymentStatus(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getPaymentStatus()));
					salesOutputParameter.setDescription(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getDescription()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return salesOutputParameter;
	}
	
public static SalesModel AuthorizeDotNetCreateProfile(SalesModel salesInputParameter) {
		
		SalesModel salesOutputParameter =  null;
		
		try {
			String AUTHORIZE_DOT_NET_CARD_AUTHENTICATION = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHORIZE_DOT_NET_CREATE_PROFILE"));
			
			Cimm2BCentralAuthorizeCreditCardRequest cimm2BCentralAuthorizeCreditCardRequest = new Cimm2BCentralAuthorizeCreditCardRequest();
			cimm2BCentralAuthorizeCreditCardRequest.setDataDescriptor(CommonUtility.validateString(salesInputParameter.getDataDescriptor()));
			cimm2BCentralAuthorizeCreditCardRequest.setDataValue(CommonUtility.validateString(salesInputParameter.getDataValue()));
			cimm2BCentralAuthorizeCreditCardRequest.setCustomerERPId(salesInputParameter.getCustomerERPId());
			cimm2BCentralAuthorizeCreditCardRequest.setCardHolderFirstName(salesInputParameter.getName());

			AddressModel billAddress = salesInputParameter.getBillAddress();
			Cimm2BCentralAddress billingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(billAddress);
			cimm2BCentralAuthorizeCreditCardRequest.setAddress(billingAddress);
			Cimm2BCentralResponseEntity authorizeDotNetResponse = Cimm2BCentralClient.getInstance().getDataObject(AUTHORIZE_DOT_NET_CARD_AUTHENTICATION, "POST", cimm2BCentralAuthorizeCreditCardRequest, Cimm2BCentralAuthorizeCreditCardResponse.class);
			
			Cimm2BCentralAuthorizeCreditCardResponse cimm2BCentralAuthorizeCreditCardResponse = null;
			
			if(authorizeDotNetResponse!=null && authorizeDotNetResponse.getStatus().getCode() == 200 && authorizeDotNetResponse.getData() != null){
				cimm2BCentralAuthorizeCreditCardResponse = (Cimm2BCentralAuthorizeCreditCardResponse) authorizeDotNetResponse.getData();
				if(cimm2BCentralAuthorizeCreditCardResponse != null){
					salesOutputParameter =  new SalesModel();
					salesOutputParameter.setProfileID(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getCustomerProfileId()));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return salesOutputParameter;
	}
public static SalesModel AuthorizeDotNetCardAuthentication(SalesModel salesInputParameter) {
		
		SalesModel salesOutputParameter =  null;
		try {
			String AUTHORIZE_DOT_NET_CARD_AUTHENTICATION = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHORIZE_DOT_NET_CARD_AUTHENTICATION"));
			
			Cimm2BCentralAuthorizeCreditCardRequest cimm2BCentralAuthorizeCreditCardRequest = new Cimm2BCentralAuthorizeCreditCardRequest();
			cimm2BCentralAuthorizeCreditCardRequest.setDataDescriptor(CommonUtility.validateString(salesInputParameter.getDataDescriptor()));
			cimm2BCentralAuthorizeCreditCardRequest.setDataValue(CommonUtility.validateString(salesInputParameter.getDataValue()));
			cimm2BCentralAuthorizeCreditCardRequest.setAmount(salesInputParameter.getAmount());
			cimm2BCentralAuthorizeCreditCardRequest.setCardHolderFirstName(salesInputParameter.getName());

				AddressModel billAddress = salesInputParameter.getBillAddress();
				Cimm2BCentralAddress billingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(billAddress);
				cimm2BCentralAuthorizeCreditCardRequest.setAddress(billingAddress);
			logger.info("---------------- CIMM2BC AUTH RESPONSE----------------------");
			Cimm2BCentralResponseEntity authorizeDotNetResponse = Cimm2BCentralClient.getInstance().getDataObject(AUTHORIZE_DOT_NET_CARD_AUTHENTICATION, "POST", cimm2BCentralAuthorizeCreditCardRequest, Cimm2BCentralAuthorizeCreditCardResponse.class);
			
			Cimm2BCentralAuthorizeCreditCardResponse cimm2BCentralAuthorizeCreditCardResponse = null;
			
			if(authorizeDotNetResponse!=null && authorizeDotNetResponse.getData() != null && authorizeDotNetResponse.getStatus().getCode() == HttpStatus.SC_OK){
				cimm2BCentralAuthorizeCreditCardResponse = (Cimm2BCentralAuthorizeCreditCardResponse) authorizeDotNetResponse.getData();
				if(cimm2BCentralAuthorizeCreditCardResponse != null){
					salesOutputParameter =  new SalesModel();
					salesOutputParameter.setAuthorizationCode(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getAuthorizationCode()));
					salesOutputParameter.setAvsResultCode(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getAvsResultCode()));
					salesOutputParameter.setCvvResultCode(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getCvvResultCode()));
					salesOutputParameter.setCavvResultCode(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getCavvResultCode()));
					salesOutputParameter.setTransactionId(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getTransactionId()));
					salesOutputParameter.setTransactionHash(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getTransactionHash()));
					salesOutputParameter.setPaymentStatus(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getPaymentStatus()));
					salesOutputParameter.setDescription(CommonUtility.validateString(cimm2BCentralAuthorizeCreditCardResponse.getDescription()));
				}
			}else if(authorizeDotNetResponse!=null && authorizeDotNetResponse.getStatus()!=null) {
				salesOutputParameter =  new SalesModel();
				salesOutputParameter.setStatus(CommonUtility.validateParseIntegerToString(authorizeDotNetResponse.getStatus().getCode()));
				salesOutputParameter.setStatusDescription(CommonUtility.validateString(authorizeDotNetResponse.getStatus().getMessage()));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return salesOutputParameter;
	}
public static SalesModel AuthorizeDeleteCustomerPaymentProfile(SalesModel salesInputParameter){
	 SalesModel salesOutputParameter =  null;
	 try{
	  String AUTHORIZE_DELETE_CUSTOMER_PROFILE = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHORIZE_DELETE_CUSTOMER_PROFILE"));
	  
	  Cimm2BCentralAuthorizeCreditCardRequest cimm2BCentralAuthorizeCreditCardRequest = new Cimm2BCentralAuthorizeCreditCardRequest();
	  cimm2BCentralAuthorizeCreditCardRequest.setCustomerProfileId(CommonUtility.validateString(salesInputParameter.getCustomerProfileId()));
	  cimm2BCentralAuthorizeCreditCardRequest.setPaymentAccountId(CommonUtility.validateString(salesInputParameter.getPaymentAccountId()));
	  
	  Cimm2BCentralResponseEntity authorizeDotNetResponse = Cimm2BCentralClient.getInstance().getDataObject(AUTHORIZE_DELETE_CUSTOMER_PROFILE, "DELETE", cimm2BCentralAuthorizeCreditCardRequest, Cimm2BCentralAuthorizeCreditCardResponse.class);
	  Cimm2BCentralResponseEntity cimm2BCentralResponseEntity = null;
	  
	  if(authorizeDotNetResponse!=null && authorizeDotNetResponse.getData()!=null && authorizeDotNetResponse.getStatus().getCode() == HttpStatus.SC_OK){
	   System.out.println("------");
	   if(authorizeDotNetResponse.getData() == "Successful."){
	    System.out.println("-- Inside Delete CC method --");
	    String message = null;
	    salesOutputParameter = new SalesModel();
	    salesOutputParameter.setDescription(message);
	   }else{
	    String message = "Deleted";
	    salesOutputParameter = new SalesModel();
	    salesOutputParameter.setDescription(message);
	    System.out.println("DELETED MESSAGE - "+message);
	   }
	  }
	 }
	 catch(Exception e){
	  e.printStackTrace();
	 }

	 return salesOutputParameter;
	}
public static SalesModel AuthorizeDotNetSaveCardAuthentication(SalesModel salesInputParameter) {
	  
	  SalesModel salesOutputParameter =  null;
	  
	  try {
	   String customerErpId = salesInputParameter.getCustomerERPId();
	   String AUTHORIZE_DOT_NET_CARD_SAVE_AUTHENTICATION = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHORIZE_DOT_NET_CARD_SAVE_AUTHENTICATION"));
	   
	   Cimm2BCentralAuthorizedSaveCreditCardRequest cimm2BCentralAuthorizeSaveCreditCardRequest = new Cimm2BCentralAuthorizedSaveCreditCardRequest();
	   
	   Cimm2BCentralAuthorizedSaveCreditCardRequest cimm2BCentralAuthorizeCreateCustomerProfileRequest = null;
	   if(CommonUtility.customServiceUtility()!=null) {
			cimm2BCentralAuthorizeCreateCustomerProfileRequest = CommonUtility.customServiceUtility().prepareCreateProfileRequest(salesInputParameter);
	   }
	   if(cimm2BCentralAuthorizeCreateCustomerProfileRequest!=null) {
		   cimm2BCentralAuthorizeSaveCreditCardRequest = cimm2BCentralAuthorizeCreateCustomerProfileRequest;
	   }else {
		   cimm2BCentralAuthorizeSaveCreditCardRequest.setDataDescriptor(CommonUtility.validateString(salesInputParameter.getDataDescriptor()));
		   cimm2BCentralAuthorizeSaveCreditCardRequest.setDataValue(CommonUtility.validateString(salesInputParameter.getDataValue()));
		   cimm2BCentralAuthorizeSaveCreditCardRequest.setCustomerERPId(customerErpId);
		   cimm2BCentralAuthorizeSaveCreditCardRequest.setCardHolderFirstName(salesInputParameter.getName());

		   AddressModel billAddress = salesInputParameter.getBillAddress();
		   Cimm2BCentralAddress billingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(billAddress);
		   cimm2BCentralAuthorizeSaveCreditCardRequest.setAddress(billingAddress);
	   }
	   Cimm2BCentralResponseEntity authorizeDotNetResponse = Cimm2BCentralClient.getInstance().getDataObject(AUTHORIZE_DOT_NET_CARD_SAVE_AUTHENTICATION, "POST", cimm2BCentralAuthorizeSaveCreditCardRequest, Cimm2BCentralAuthorizeSaveCreditCardResponse.class);
	   
	   Cimm2BCentralAuthorizeSaveCreditCardResponse cimm2BCentralAuthorizeSaveCreditCardResponse = null;
	   
	   if(authorizeDotNetResponse!=null && authorizeDotNetResponse.getData() != null &&  authorizeDotNetResponse.getStatus().getCode() == HttpStatus.SC_OK){
	    cimm2BCentralAuthorizeSaveCreditCardResponse = (Cimm2BCentralAuthorizeSaveCreditCardResponse) authorizeDotNetResponse.getData();
	    List<String> customerPaymentId = cimm2BCentralAuthorizeSaveCreditCardResponse.getCustomerPaymentIdList();
	    
	    if(cimm2BCentralAuthorizeSaveCreditCardResponse != null){
	     salesOutputParameter = new SalesModel();
	     salesOutputParameter.setSaveCard(cimm2BCentralAuthorizeSaveCreditCardResponse.isSaveCard());
	     salesOutputParameter.setCustomerProfileId(CommonUtility.validateString(cimm2BCentralAuthorizeSaveCreditCardResponse.getCustomerProfileId()));
	     salesOutputParameter.setCustomerPaymentIdList(customerPaymentId);
	     
	    }
	   }else if(authorizeDotNetResponse!=null && authorizeDotNetResponse.getStatus()!=null) {
			salesOutputParameter =  new SalesModel();
			salesOutputParameter.setStatus(CommonUtility.validateParseIntegerToString(authorizeDotNetResponse.getStatus().getCode()));
			salesOutputParameter.setStatusDescription(CommonUtility.validateString(authorizeDotNetResponse.getStatus().getMessage()));
		}
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  
	  return salesOutputParameter;
	 }
public static SalesModel AuthorizeDotNetCreateCustomerPaymentProfile(SalesModel salesInputParameter) {
	
	SalesModel salesOutputParameter =  null;
	String errorMsg = "";
	try {
		//HttpSession session = salesInputParameter.getSession();
		String customerProfileId = salesInputParameter.getCustomerProfileId();
		
		String AUTHORIZE_DOT_NET_CREATE_CUSTOMER_PAYMENT_PROFILE = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHORIZE_DOT_NET_CREATE_CUSTOMER_PAYMENT_PROFILE"));
		
		Cimm2BCentralAuthorizedSaveCreditCardRequest cimm2BCentralAuthorizeSaveCreditCardRequest = new Cimm2BCentralAuthorizedSaveCreditCardRequest();
		cimm2BCentralAuthorizeSaveCreditCardRequest.setDataDescriptor(CommonUtility.validateString(salesInputParameter.getDataDescriptor()));
		cimm2BCentralAuthorizeSaveCreditCardRequest.setDataValue(CommonUtility.validateString(salesInputParameter.getDataValue()));
		cimm2BCentralAuthorizeSaveCreditCardRequest.setCustomerProfileId(customerProfileId);
		cimm2BCentralAuthorizeSaveCreditCardRequest.setCardHolderFirstName(salesInputParameter.getName());

		AddressModel billAddress = salesInputParameter.getBillAddress();
		Cimm2BCentralAddress billingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(billAddress);
		cimm2BCentralAuthorizeSaveCreditCardRequest.setAddress(billingAddress);
		Cimm2BCentralResponseEntity authorizeDotNetResponse = Cimm2BCentralClient.getInstance().getDataObject(AUTHORIZE_DOT_NET_CREATE_CUSTOMER_PAYMENT_PROFILE, "POST", cimm2BCentralAuthorizeSaveCreditCardRequest, Cimm2BCentralAuthorizeCustomerPaymentProfileResponse.class);
		
		Cimm2BCentralAuthorizeCustomerPaymentProfileResponse cimm2BCentralAuthorizeCustomerPaymentProfileResponse = null;
		
		salesOutputParameter = new SalesModel();
		if(authorizeDotNetResponse!=null && authorizeDotNetResponse.getData() != null &&  authorizeDotNetResponse.getStatus().getCode() == HttpStatus.SC_OK ){
			String customerPaymentId = (String) authorizeDotNetResponse.getData();
			if(customerPaymentId!=null){
				salesOutputParameter.setCustomerPaymentId(customerPaymentId);
			}
		}else if(authorizeDotNetResponse!=null && authorizeDotNetResponse.getStatus()!=null){
			System.out.println("---- Inside error code response  ----");
			salesOutputParameter.setStatus(CommonUtility.validateParseIntegerToString(authorizeDotNetResponse.getStatus().getCode()));
			salesOutputParameter.setStatusDescription(CommonUtility.validateString(authorizeDotNetResponse.getStatus().getMessage()));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return salesOutputParameter;
}

	public static SalesModel createShellOrder(UsersModel shipAddress, UsersModel billAddress, List<ProductsModel> cartDetails, Map<String, Object> otherDetails) {
		Cimm2BCentralOrder order = new Cimm2BCentralOrder();
		SalesModel shellOrder = new SalesModel();
		ArrayList<Cimm2BCentralLineItem> lineItems = new ArrayList<Cimm2BCentralLineItem>();
		String wareHouseCode = otherDetails.get("wareHouseCode").toString();
		String pickUpWareHouseCode = null;
		if(CommonUtility.validateString((String) otherDetails.get("pickUpWareHouseCode")).length()>0){
			pickUpWareHouseCode = CommonUtility.validateString((String) otherDetails.get("pickUpWareHouseCode"));
		} 
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PREFIX_ZEROS_TO_WAREHOUSE_CODE")).equals("Y")) {
			wareHouseCode = CommonUtility.prefixWarehouseCode(wareHouseCode);
		}
		String customerErpId = otherDetails.get("customerERPId").toString();
		//Cimm2BCentralAddress cimm2BCentralBillingAddress = Cimm2BCentralClient.getInstance().ecomUserModelToCimm2BCentralAddress(billAddress);
		try {
			Cimm2BCentralAddress cimm2BCentralShipingAddress = Cimm2BCentralClient.getInstance().ecomUserModelToCimm2BCentralAddress(shipAddress);
			
			if(CommonUtility.customServiceUtility() != null) {
				cartDetails = CommonUtility.customServiceUtility().extractCatalogOrCimmItems((ArrayList<ProductsModel>) cartDetails);
			}
			
			for(ProductsModel product : cartDetails) {
				Cimm2BCentralLineItem lineItem = new Cimm2BCentralLineItem();
				lineItem.setPartNumber(product.getPartNumber());
				lineItem.setQty(product.getQty()); 
				
				if(product.getUom() != null) {
					lineItem.setUom(product.getUom().trim());
				}
				lineItem.setTaxDesc("Y");
				lineItem.setListPrice(product.getPrice());
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CALCULATE_UOM_BASED_PRICE")).equalsIgnoreCase("Y")){
					
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("OVERRIDE_PRICE_RULE")).equals("N")) {
						lineItem.setExtendedPrice((product.getUnitPrice()/product.getUomQuantity()) * product.getQty() * product.getUomQty());
						lineItem.setUnitPrice(product.getUnitPrice());
						if(product.getUomPrice() > 0.0) {
							lineItem.setExtendedPrice(product.getUomPrice() * product.getQty());
							lineItem.setUnitPrice(product.getUomPrice());
						}
					}	
				}else {
					lineItem.setUnitPrice(product.getUnitPrice());
				}
				lineItem.setUomQty(Integer.toString(product.getUomQty()));
				lineItem.setCost(product.getPrice());
				lineItem.setLineItemComment(product.getLineItemComment());
				lineItem.setShippingBranch(shipAddress.getBranchID()!=null?shipAddress.getBranchID():wareHouseCode);
				lineItem.setCalculateTax(true);
				lineItem.setDiscount(0);
				lineItems.add(lineItem);
			}
			ArrayList<Cimm2BCentralShipVia> shipVia=new ArrayList<Cimm2BCentralShipVia>();
			Cimm2BCentralShipVia cimm2bCentralShipVia = new Cimm2BCentralShipVia();
			
			order.setCustomerERPId(customerErpId);
			order.setBranchCode(shipAddress.getBranchID()!=null?shipAddress.getBranchID():wareHouseCode);
			order.setSalesLocationId(wareHouseCode);
			order.setShippingAddress(cimm2BCentralShipingAddress);
			order.setLineItems(lineItems);
			order.setOrderStatus(shipAddress.getOrderStatus());
			order.setOrderSubTotal(cartDetails.get(0).getCartTotal());
			order.setOrderTotal(cartDetails.get(0).getTotal());
			cimm2bCentralShipVia.setShipViaCode(shipAddress.getShipVia());
			shipVia.add(cimm2bCentralShipVia);
			order.setShipVia(shipVia);
			order.setDiscount(otherDetails.get("totalSavingOnOrder") != null ? Double.parseDouble(otherDetails.get("totalSavingOnOrder").toString()) : 0);
			order.setOtherCharges(otherDetails.get("otherCharges") != null ? Double.parseDouble(otherDetails.get("otherCharges").toString()) : 0);
			order.setFreight(otherDetails.get("otherCharges") != null ? Double.parseDouble(otherDetails.get("otherCharges").toString()) : 0);
			order.setUniqueWebReferenceNumber(CommonDBQuery.getSequenceId("ORDER_ID_SEQ"));
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFUALT_CONTACT_ID")).length()>0){
				order.setContactId(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFUALT_CONTACT_ID")));
			}
			order.setSalesLocationId(CommonUtility.validateString(shipAddress.getBranchID()).length()>0?shipAddress.getBranchID():wareHouseCode);
			if(CommonUtility.customServiceUtility()!=null && CommonUtility.validateString(pickUpWareHouseCode).length()>0) {
				CommonUtility.customServiceUtility().pricingBranchCode(order,otherDetails);
			}
			if(CommonDBQuery.getSystemParamtersList().get("CREATE_FREIGHT_LINE_ITEM") !=null &&CommonDBQuery.getSystemParamtersList().get("CREATE_FREIGHT_LINE_ITEM").equalsIgnoreCase("N")){
				order.setCreateShippingAndHandlingAsNewLineItem(false);
			}else{
				order.setCreateShippingAndHandlingAsNewLineItem(true);
			}
			String CREATE_SHELL_ORDER = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_SHELL_ORDER_API"));			
			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_SHELL_ORDER, HttpMethod.POST, order, Cimm2BCentralOrder.class);
			
			if(orderResponseEntity!=null && orderResponseEntity.getData() != null){
				Cimm2BCentralOrder orderResponse = (Cimm2BCentralOrder) orderResponseEntity.getData();
				if(orderResponse != null) {
					shellOrder.setTax(orderResponse.getTaxAmount()!=null?orderResponse.getTaxAmount():0.0);
					shellOrder.setTotal(orderResponse.getOrderTotal()!=null?orderResponse.getOrderTotal():0.0);
					shellOrder.setSubtotal(orderResponse.getOrderSubTotal()!=null?orderResponse.getOrderSubTotal():0.0);
					shellOrder.setExternalCartId(orderResponse.getCartId()!=null?orderResponse.getCartId():"");
					shellOrder.setJurisdictionCode(orderResponse.getJurisdictionCode()!=null?orderResponse.getJurisdictionCode():"");
					shellOrder.setTaxable(orderResponse.isTaxable());
					shellOrder.setOtherCharges(orderResponse.getOtherAmount()!=null?orderResponse.getOtherAmount():0.0);
					shellOrder.setDiscount(orderResponse.getDiscountAmount()!=null?orderResponse.getDiscountAmount():0.0);
					if(orderResponse.getFreight()!=null) {
						shellOrder.setFreight(orderResponse.getFreight());
					}else {
						shellOrder.setFreight(orderResponse.getFreightOut()!=null?orderResponse.getFreightOut():0.0);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return shellOrder;
	}

	public static UsersModel elementSetup(String userName, String password, String cardHolder, String address1,String zipCode, String responseUrl) {
		
		UsersModel wsLogin = new UsersModel();
		Cimm2BCentralCreditCardDetails creditCardDetails = new Cimm2BCentralCreditCardDetails();
		Cimm2BCentralAddress creditCardAddress = new Cimm2BCentralAddress();
		creditCardAddress.setAddressLine1(address1);
		creditCardAddress.setZipCode(zipCode);
		creditCardDetails.setCardHolderName(cardHolder);
		creditCardDetails.setReturnUrl(responseUrl);
		creditCardDetails.setAddress(creditCardAddress);
		creditCardDetails.setUserName(userName);
		creditCardDetails.setPassword(password);
		try{
			String SAVE_CREDIT_CARD = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SAVE_CREDIT_CARD"));
			Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(SAVE_CREDIT_CARD, HttpMethod.POST, creditCardDetails, Cimm2BCentralCreditCardDetails.class);
			
			Cimm2BCentralCreditCardDetails cimm2BCentralAuthorizeCreditCardResponse = null;
			
			if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getStatus().getCode() == 200 && customerDetailsResponseEntity.getData() != null){
				cimm2BCentralAuthorizeCreditCardResponse = (Cimm2BCentralCreditCardDetails) customerDetailsResponseEntity.getData();
				wsLogin.setElementSetupId(cimm2BCentralAuthorizeCreditCardResponse.getElementSetupId());
				wsLogin.setElementSetupUrl(cimm2BCentralAuthorizeCreditCardResponse.getReturnUrl());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return wsLogin;

	}
	//ConstructionProjectManagement

	public static Cimm2BCentralProjectManagementInformation ConstructionProjectManagement(ProjectManagementModel projectManagementParameter) {


		Cimm2BCentralProjectManagementInformation result=new Cimm2BCentralProjectManagementInformation();
		Cimm2BCentralProjectManagementInformation managementList = new Cimm2BCentralProjectManagementInformation();
		String jobId = projectManagementParameter.getJobId();


		String GET_CONSTRUCTION_MANAGEMENT_DETAILS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CONSTRUCTION_MANAGEMENT_DETAILS")) + "?" + Cimm2BCentralRequestParams.jobId + "=" + jobId;

		Cimm2BCentralResponseEntity managementResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_CONSTRUCTION_MANAGEMENT_DETAILS, "GET", null, Cimm2BCentralProjectManagementInformation.class);


		if(managementResponseEntity != null && managementResponseEntity.getData() != null){
			result = (Cimm2BCentralProjectManagementInformation) managementResponseEntity.getData();

			ArrayList<Cimm2BCentralManagementHeader> reportHeaderFromERP = (ArrayList<Cimm2BCentralManagementHeader>)result.getReportHeader();
			ArrayList<Cimm2BCentralManagementHeader> jobReportList =new ArrayList<Cimm2BCentralManagementHeader>();
			for (int i = 0; i < reportHeaderFromERP.size(); i++) {

				Cimm2BCentralManagementHeader jobReport= new Cimm2BCentralManagementHeader();
				jobReport.setJobId(jobId);
				jobReport.setJobName(reportHeaderFromERP.get(i).getJobName());
				jobReport.setJobNumber(reportHeaderFromERP.get(i).getJobNumber());
				jobReport.setProjectManager(reportHeaderFromERP.get(i).getProjectManager());
				jobReport.setEmail(reportHeaderFromERP.get(i).getEmail());
				jobReport.setEmpPhone(reportHeaderFromERP.get(i).getEmpPhone());
				jobReport.setCustomerName(reportHeaderFromERP.get(i).getCustomerName());
				jobReport.setPrimaryContactName(reportHeaderFromERP.get(i).getPrimaryContactName());
				jobReport.setShipToAddress(reportHeaderFromERP.get(i).getShipToAddress());
				jobReport.setShipCity(reportHeaderFromERP.get(i).getShipCity());
				jobReport.setContactInfo(reportHeaderFromERP.get(i).getContactInfo());
				jobReport.setCustomerPO(reportHeaderFromERP.get(i).getCustomerPO());

				jobReportList.add(jobReport);
			}
			ArrayList<Cimm2BCentralManagementBody> orderListFromERP = (ArrayList<Cimm2BCentralManagementBody>)result.getReportBody();
			ArrayList<Cimm2BCentralManagementBody> orderList=new ArrayList<Cimm2BCentralManagementBody>();
			if(orderListFromERP != null){
				for (int i = 0; i < orderListFromERP.size(); i++) {
					Cimm2BCentralManagementBody orderReport =new Cimm2BCentralManagementBody();
					orderReport.setSortOrder(orderListFromERP.get(i).getSortOrder());
					orderReport.setFixtureType(orderListFromERP.get(i).getFixtureType());
					orderReport.setVendor(orderListFromERP.get(i).getVendor());
					orderReport.setDescription(orderListFromERP.get(i).getDescription());
					orderReport.setOrderQty(orderListFromERP.get(i).getOrderQty());
					orderReport.setStatus(orderListFromERP.get(i).getStatus());
					orderReport.setShipQty(orderListFromERP.get(i).getShipQty());
					orderReport.setActualShipDate(orderListFromERP.get(i).getActualShipDate());
					orderReport.setEstShipDate(orderListFromERP.get(i).getEstShipDate());
					orderReport.setTrackingNumber(orderListFromERP.get(i).getTrackingNumber());
					orderReport.setExternalNote(orderListFromERP.get(i).getExternalNote());
					orderReport.setInvNumber(orderListFromERP.get(i).getInvNumber());
					orderReport.setShipper(orderListFromERP.get(i).getShipper());

					orderList.add(orderReport);
				}
			}
			managementList.setReportHeader(jobReportList);
			managementList.setReportBody(orderList);
		}
		return managementList;

	}
	
	public static String invoiceDetailPrint(SalesModel salesInputParameter) {
		String responseData = null;
		try {
				String customerErpId = CommonUtility.validateString(salesInputParameter.getCustomerNumber());
				String invoiceNumber = CommonUtility.validateString(salesInputParameter.getInvoiceNumber());
				String documentId = "";
				if(salesInputParameter.getDocumentDetail()!=null) {
					documentId=	CommonUtility.validateString(salesInputParameter.getDocumentDetail().getDocumentId());
				}
				Cimm2BCentralDocumentDetail documentLinks = new Cimm2BCentralDocumentDetail();
				String orderNumber = CommonUtility.validateString(salesInputParameter.getOrderNum());
				
				String GET_INVOICES_DETAILS_PRINT = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_INVOICES_DETAILS_PRINT"));
				
				Cimm2BCentralGetInvoiceRecallRequest invoiceRecallRequest = new Cimm2BCentralGetInvoiceRecallRequest();
				invoiceRecallRequest.setCustomerERPId(customerErpId);
				invoiceRecallRequest.setInvoiceNumber(invoiceNumber);
				documentLinks.setDocumentId(documentId);
				invoiceRecallRequest.setDocumentLinks(documentLinks);
				invoiceRecallRequest.setOrderNumber(orderNumber);
				
				Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_INVOICES_DETAILS_PRINT, "POST", invoiceRecallRequest, Cimm2BCentralGetInvoiceRecallResponse.class);
				
				String data = null;
				if(orderResponseEntity!=null && orderResponseEntity.getData() != null && orderResponseEntity.getStatus().getCode() == HttpStatus.SC_OK ){
					data = (String) orderResponseEntity.getData();
					if(data != null && !data.isEmpty()){
						responseData = data;
					}
				}
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return responseData;
	}
	public static SalesModel eventRegistrationOrder(SalesOrderManagementModel salesOrderInput) {

		SalesModel orderDetail = new SalesModel();
		Connection conn = null;
		try
		{	
			HttpSession session = salesOrderInput.getSession();
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PREFIX_ZEROS_TO_WAREHOUSE_CODE")).equals("Y")) {
				wareHousecode = CommonUtility.prefixWarehouseCode(wareHousecode);
			}
			
			String salesLocationId = (String) session.getAttribute("salesLocationId");
			String customerErpId ="";
			if(CommonUtility.validateString((String) session.getAttribute("customerId")).length()>0){
				customerErpId =(String) session.getAttribute("customerId");
			}else if(CommonUtility.validateString((String) session.getAttribute("billingEntityId")).length()>0){
				customerErpId =(String) session.getAttribute("billingEntityId");
			}
			String orderNumber = "";
			int backOrderSeqNumber = 0;
			double subTotal=0.0;
			String handlingListItems = "";
			String deliveryListItems = "";
			String errorMessageToDisplay = "";
			ArrayList<Cimm2BCentralLineItem> lineItems = new ArrayList<Cimm2BCentralLineItem>();
			SalesModel quoteResponse = salesOrderInput.getQuoteResponse();
			Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    String orderDate = sdf.format(cal.getTime());
			LinkedHashMap<String, Cimm2BCentralShipVia> shipMap = new LinkedHashMap<String, Cimm2BCentralShipVia>();
			ArrayList<ProductsModel> orderDetails = salesOrderInput.getOrderItems();
			 if(orderDetails != null && orderDetails.size() > 0){
				int i =1;
				for(ProductsModel item : orderDetails){
					Cimm2BCentralLineItem cimm2bCentralLineItem = new Cimm2BCentralLineItem();					
					cimm2bCentralLineItem.setLineIdentifier(i++);
					cimm2bCentralLineItem.setPartNumber(item.getPartNumber());
					cimm2bCentralLineItem.setQty(item.getQty());
					cimm2bCentralLineItem.setManufacturer(item.getManufacturerName());
					cimm2bCentralLineItem.setLineItemComment(item.getLineItemComment());
					cimm2bCentralLineItem.setUnitPrice(item.getPrice());
					cimm2bCentralLineItem.setPage_title(item.getPageTitle());
					cimm2bCentralLineItem.setUomQty("1");
					cimm2bCentralLineItem.setItemId(item.getPartNumber());
					cimm2bCentralLineItem.setListPrice(item.getListPrice());
					cimm2bCentralLineItem.setShortDescription(item.getShortDesc()!=null?item.getShortDesc():".");
					cimm2bCentralLineItem.setProductCategory(item.getProductCategory()!=null?item.getProductCategory():"");
					cimm2bCentralLineItem.setShippingBranch(wareHousecode);
					cimm2bCentralLineItem.setUom(item.getUom() != null ? item.getUom().trim() : "");
					cimm2bCentralLineItem.setExtendedPrice(item.getUnitPrice() * item.getQty());
					cimm2bCentralLineItem.setUnitPrice(item.getPrice());
					
					if(salesOrderInput.getTaxExempt()!=null && salesOrderInput.getTaxExempt().equalsIgnoreCase("N")){
						cimm2bCentralLineItem.setCalculateTax(true);
					}else{
						cimm2bCentralLineItem.setCalculateTax(false);
					}
					
					cimm2bCentralLineItem.setShippingBranch(wareHousecode);	
					lineItems.add(cimm2bCentralLineItem);
					if(orderDetails.get(0).getCartTotal()>0) {
						subTotal = orderDetails.get(0).getCartTotal();
					}else {
						if(cimm2bCentralLineItem!=null && cimm2bCentralLineItem.getListPrice()>0){
							subTotal=subTotal+(cimm2bCentralLineItem.getListPrice());
						}else{
							subTotal=subTotal+(cimm2bCentralLineItem.getExtendedPrice());
						}
					}
				}
			}
			
			String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			String branchCode = ProductsDAO.getWarehouseCustomField(CommonUtility.validateNumber(buyingCompanyId), "LOCATION_CODE");
			boolean isDFMModeActive = false; //DFM Changes
			boolean isMaximumThresholdMet = false; //DFM Changes
			AddressModel billAddress = salesOrderInput.getBillAddress();
			AddressModel shipAddress =  salesOrderInput.getShipAddress();
			Cimm2BCentralAddress billingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(billAddress);
			Cimm2BCentralAddress shippingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(shipAddress);
			Cimm2BCentralBillAndShipToContact shipToContact = new Cimm2BCentralBillAndShipToContact();
			Cimm2BCentralBillAndShipToContact billToContact = new Cimm2BCentralBillAndShipToContact();
			Cimm2BCentralOrder orderRequest = new Cimm2BCentralOrder();
			orderRequest.setCartId(salesOrderInput.getExternalCartId());
			orderRequest.setJurisdictionCode(salesOrderInput.getJurisdictionCode());
			orderRequest.setBillingAddress(billingAddress);
			orderRequest.setShippingAddress(shippingAddress);
			orderRequest.setShipToContact(shipToContact);
			orderRequest.setBillToContact(billToContact);
			orderRequest.setLineItems(lineItems);
			orderRequest.setOrderDate(orderDate);
			orderRequest.setOrderNotes(salesOrderInput.getOrderNotes()!=null?salesOrderInput.getOrderNotes():salesOrderInput.getReqDate());
	     	orderRequest.setCustomerERPId(customerErpId);
			orderRequest.setUserERPId((String) session.getAttribute(Global.USERID_KEY));
			if(salesOrderInput.getSelectedBranch() !=null) {
				orderRequest.setBranchCode(salesOrderInput.getSelectedBranch());
			}else {
				orderRequest.setBranchCode(branchCode);
			}
			orderRequest.setCustomerPoNumber(salesOrderInput.getPurchaseOrderNumber()!=null?salesOrderInput.getPurchaseOrderNumber().toUpperCase():"");
			orderRequest.setShippingInstruction(salesOrderInput.getShippingInstruction());
			orderRequest.setOrderERPId(salesOrderInput.getOrderERPId());
			orderRequest.setWarehouseLocation(CommonUtility.validateString(salesOrderInput.getUserSelectedLocation()).trim().length()>0?salesOrderInput.getUserSelectedLocation():wareHousecode);
			orderRequest.setOrderNumber(orderNumber);
			orderRequest.setBackOrderSequence(backOrderSeqNumber);
			orderRequest.setOrderComment(salesOrderInput.getOrderNotes());
			orderRequest.setOrderedBy(CommonUtility.validateString(salesOrderInput.getOrderedBy()));
			orderRequest.setRequiredDate(CommonUtility.validateString(salesOrderInput.getReqDate()));
			orderRequest.setFreightOut(CommonUtility.validateDoubleNumber(salesOrderInput.getFrieghtCharges()));
			orderRequest.setFreight(CommonUtility.validateDoubleNumber(salesOrderInput.getFrieghtCharges()));
			orderRequest.setOrderSubTotal(subTotal);
			orderRequest.setBackOrderTotal(subTotal);
			if(salesOrderInput.getDiscountAmount()>0){
				orderRequest.setDiscountAmount(salesOrderInput.getDiscountAmount());
				orderRequest.setOrderTotal((subTotal+orderRequest.getFreight()+salesOrderInput.getOrderTax())-salesOrderInput.getDiscountAmount());
				}
			else {
				orderRequest.setOrderTotal(subTotal+orderRequest.getFreight()+salesOrderInput.getOrderTax());
				}
			orderRequest.setGasPoNumber(salesOrderInput.getGasPoNumber());
			orderRequest.setReleaseNumber(CommonUtility.validateString(salesOrderInput.getCustomerReleaseNumber()));
			orderRequest.setOtherAmount(CommonUtility.validateDoubleNumber(salesOrderInput.getShippingAndHandlingFee())); // PSS DFM
			orderRequest.setDfmOrder(isDFMModeActive); // PSS DFM
			orderRequest.setAnonymous(salesOrderInput.getAnonymous());
			orderRequest.setMaximumOrderThresholdMet(isMaximumThresholdMet); // PSS DFM
			orderRequest.setUniqueWebReferenceNumber(salesOrderInput.getOrderId());
			if(salesOrderInput.getOrderStatus() !=null)
			{
				orderRequest.setOrderStatus(salesOrderInput.getOrderStatus());
			}else {
				if(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderStatus.labels")!=null) {
					orderRequest.setOrderStatus(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderStatus.labels"));
				}
			}
			orderRequest.setOrderStatusCode(CommonUtility.validateString(salesOrderInput.getOrderStatusCode()));
			orderRequest.setTaxAmount(salesOrderInput.getOrderTax());
			orderRequest.setCustomerName(billAddress.getShipToName());
			orderRequest.setFreightCode(salesOrderInput.getFreightCode());
			orderRequest.setOrderType(CommonUtility.validateString(salesOrderInput.getOrderType()));
			if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels")).length()>0){
				orderRequest.setOrderSource(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels"):"");
			}
			orderRequest.setOrderDisposition(salesOrderInput.getOrderDisposition()!=null?salesOrderInput.getOrderDisposition():"");
			if(CommonUtility.validateString(salesLocationId).length()>0 && !CommonUtility.validateString(salesLocationId).equalsIgnoreCase("0")){
				orderRequest.setSalesLocationId(salesLocationId);
			}else{
				orderRequest.setSalesLocationId(wareHousecode);	
			}
			if(CommonDBQuery.getSystemParamtersList().get("CREATE_FREIGHT_LINE_ITEM") !=null &&CommonDBQuery.getSystemParamtersList().get("CREATE_FREIGHT_LINE_ITEM").equalsIgnoreCase("N")){
				orderRequest.setCreateShippingAndHandlingAsNewLineItem(false);
			}else{
				orderRequest.setCreateShippingAndHandlingAsNewLineItem(true);
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFUALT_CONTACT_ID")).length()>0){
			orderRequest.setContactId(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFUALT_CONTACT_ID")));
			}else if(CommonUtility.validateNumber(salesOrderInput.getErpUserContactId())>0){
				orderRequest.setContactId(CommonUtility.validateString(salesOrderInput.getErpUserContactId()));
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONSTANT_ORDER_COMMENT")).length()>0){
			orderRequest.setOrderCommentDisplayArea(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONSTANT_ORDER_COMMENT")));
			}
			if(salesOrderInput.getAnonymous()!=null && CommonUtility.validateString(salesOrderInput.getAnonymous()).equalsIgnoreCase("Y")){
				shipToContact.setFirstName(shipAddress.getFirstName());
				shipToContact.setLastName(shipAddress.getLastName());
				if(CommonUtility.validateString(shipAddress.getEmailAddress()).length()>0){
					shipToContact.setPrimaryEmailAddress(shipAddress.getEmailAddress().toUpperCase()!=null?shipAddress.getEmailAddress().toUpperCase():"");	
				}else{
					shipToContact.setPrimaryEmailAddress(billAddress.getEmailAddress()!=null?billAddress.getEmailAddress().toUpperCase():"");
				}
			}else{
				if(session.getAttribute("shipTofirstName")!= null && (String)session.getAttribute("shipTofirstName") != null){
					shipToContact.setFirstName((String)session.getAttribute("shipTofirstName"));
					if(session.getAttribute("shipToLastName")!= null && (String)session.getAttribute("shipToLastName") != null){
					shipToContact.setLastName((String)session.getAttribute("shipToLastName"));
					}else{
						shipToContact.setLastName(".");
					}
				}else{
					shipToContact.setFirstName(salesOrderInput.getShipAddress().getFirstName());
					if(CommonUtility.validateString(salesOrderInput.getShipAddress().getLastName()).length()>0){
						shipToContact.setLastName(salesOrderInput.getShipAddress().getLastName());
					}else{
						shipToContact.setLastName(".");
					}
				}
			}
			if(salesOrderInput.getShipAddress().getEmailAddress()!=null && salesOrderInput.getShipAddress().getEmailAddress()!=""){
				shipToContact.setPrimaryEmailAddress(salesOrderInput.getShipAddress().getEmailAddress().toUpperCase());
			}else if(CommonUtility.validateString(salesOrderInput.getBillAddress().getEmailAddress()).length()>0){
				shipToContact.setPrimaryEmailAddress(salesOrderInput.getBillAddress().getEmailAddress());
			}else {
				if(session!=null && (String)session.getAttribute("userEmailAddress")!=null) {
				shipToContact.setPrimaryEmailAddress(CommonUtility.validateString((String)session.getAttribute("userEmailAddress")));
				}
			}
			shipToContact.setPrimaryPhoneNumber(salesOrderInput.getShipAddress().getPhoneNo()!=null?salesOrderInput.getShipAddress().getPhoneNo():salesOrderInput.getBillAddress().getPhoneNo());
			shipToContact.setCarrierId(salesOrderInput.getShipVia());
			shipToContact.setContactId(customerErpId);
			shipToContact.setCompanyName(shippingAddress.getCustomerName());
			shipToContact.setShipAttention(shippingAddress.getCustomerName());
			billToContact.setPrimaryEmailAddress(billingAddress.getPrimaryEmailAddress());
			billToContact.setFirstName(billAddress.getFirstName());
			billToContact.setContactId(customerErpId);
			billToContact.setLastName(billAddress.getLastName());
			billToContact.setPrimaryPhoneNumber(billAddress.getPhoneNo());
			billToContact.setCompanyName(billAddress.getShipToName());
			billToContact.setShipAttention(billAddress.getShipToName());
			Cimm2BCentralCustomerCard customerCard = null;
			if(salesOrderInput.getCreditCardValue() != null){
				customerCard = new Cimm2BCentralCustomerCard();
				if(CommonUtility.validateString(salesOrderInput.getCreditCardValue().getElementPaymentAccountId()).length() > 0){
					customerCard.setPaymentAccountId(salesOrderInput.getCreditCardValue().getElementPaymentAccountId());
					String[] expDate = null;
					if(salesOrderInput.getCreditCardValue().getDate()!=null){
						expDate = salesOrderInput.getCreditCardValue().getDate().split("/");
					}
					String expMonth=null;
					String expYear=null;
					if(expDate!=null && expDate.length>1){
						expMonth = expDate[0];
						if(expDate.length>1){
							expYear = expDate[1];
							customerCard.setExpiryYear(expDate[1]);
						}
					}
					customerCard.setCustomerERPId(customerErpId);
					if(salesOrderInput.getBillAddress()!=null && CommonUtility.validateString(salesOrderInput.getBillAddress().getFirstName()).equalsIgnoreCase("")){
						customerCard.setCardHolderFirstName(salesOrderInput.getBillAddress().getFirstName());
					}
					if(salesOrderInput.getBillAddress()!=null && CommonUtility.validateString(salesOrderInput.getBillAddress().getLastName()).equalsIgnoreCase("")){
						customerCard.setCardHolderFirstName(salesOrderInput.getBillAddress().getLastName());
					}
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_MERCHANT_ID")).length()>0){
						customerCard.setPaymentAccountId(CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_MERCHANT_ID"));//(salesOrderInput.getCreditCardValue().getCreditCardTransactionID()); //CardTransactionID = PaymentAccountID
					}else{
						customerCard.setPaymentAccountId(salesOrderInput.getCreditCardValue().getCreditCardTransactionID());
					}
					if(CommonUtility.validateString(salesOrderInput.getCreditCardValue().getCreditCardToken()).length()>0){
						customerCard.setAuthorizationNumber(salesOrderInput.getCreditCardValue().getCreditCardToken());
					}else {
						customerCard.setAuthorizationNumber(salesOrderInput.getCreditCardValue().getCreditCardTransactionID());
					}
					customerCard.setCreditCardType(salesOrderInput.getCreditCardValue().getCreditCardType());
					customerCard.setCvv(salesOrderInput.getCreditCardValue().getCreditCardCvv2VrfyCode());
					customerCard.setCardHolderName(salesOrderInput.getCreditCardValue().getCardHolder());
					customerCard.setCreditCardNumber(salesOrderInput.getCreditCardValue().getCreditCardNumber());
					customerCard.setExpiryDate(salesOrderInput.getCreditCardValue().getDate());
					customerCard.setDataValue(salesOrderInput.getCreditCardValue().getDataValue());
					if(CommonUtility.customServiceUtility() != null) {
						//Setting Authorization number as order comment.
						CommonUtility.customServiceUtility().setOrderComment(customerCard,orderRequest);
						CommonUtility.customServiceUtility().configureCreditCardDetails(customerCard);
						CommonUtility.customServiceUtility().getAuthAmount(customerCard,salesOrderInput);
					}
					
					//customerCard.setExpiryDate(salesOrderInput.getCreditCardValue().getDate());
					customerCard.setCardHolderFirstName(CommonUtility.validateString(billAddress.getFirstName()));
					customerCard.setCardHolderLastName(CommonUtility.validateString(billAddress.getLastName()));
					customerCard.setExpiryMonth(expMonth);
					customerCard.setExpiryYear(expYear);
					customerCard.setAmount(CommonUtility.validateDoubleNumber(salesOrderInput.getCreditCardValue().getCreditCardAmount()));
					customerCard.setAuthType("");
					logger.info("credit card amount" +salesOrderInput.getCreditCardValue().getCreditCardAmount());
					if(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID")!=null && CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID").length()>0){
						customerCard.setElementProcessorID(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID")));
					}
					customerCard.setAddress(billingAddress);
					if(CommonUtility.validateString(salesOrderInput.getCreditCardValue().getSaveCard()).equalsIgnoreCase("Y")){
						customerCard.setSaveCard(true);	
					}else{
						customerCard.setSaveCard(false);
					}
					customerCard.setOrderERPId(salesOrderInput.getOrderERPId());
					orderRequest.setCustomerCard(customerCard);
	
				}
			}else{
				orderRequest.setPreAuthorize(false);
			}

			String CREATE_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_ORDER_API"));
			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_ORDER_URL, HttpMethod.POST, orderRequest, Cimm2BCentralOrder.class);


			Cimm2BCentralOrder orderResponse = null;
			if(orderResponseEntity!=null && orderResponseEntity.getData() != null && orderResponseEntity.getStatus() != null && orderResponseEntity.getStatus().getCode() == HttpStatus.SC_OK){
				ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
				orderResponse = (Cimm2BCentralOrder) orderResponseEntity.getData();
				orderResponse.setStatus(orderResponseEntity.getStatus());
				//From ERP - orderNumber is set to orderERPId in SX not sure why wasnt orderERPId included in if Statment
				if(orderResponse != null && ((CommonUtility.validateString(orderResponse.getOrderERPId()).length() > 0) || (CommonUtility.validateString(orderResponse.getOrderNumber()).length() > 0) || (CommonUtility.validateString(orderResponse.getCartId()).length() > 0))){
					
					orderDetail.setExternalCartId(quoteResponse != null ? quoteResponse.getExternalCartId() : "");
					orderDetail.setUserId(CommonUtility.validateNumber(orderResponse.getUserERPId()));
					if(CommonUtility.validateString(orderResponse.getOrderERPId()).length() > 0){
						orderDetail.setErpOrderNumber(orderResponse.getOrderERPId());
					}else{
						orderDetail.setErpOrderNumber(orderResponse.getOrderNumber());
					}	
					orderDetail.setOrderNum(orderResponse.getOrderNumber());
					orderDetail.setExternalSystemId(orderResponse.getOrderNumber());
					if(orderResponse.getOrderId()!=null){
						orderDetail.setOrderId(CommonUtility.validateNumber(orderResponse.getOrderId()));
					}else{
						orderDetail.setOrderID(orderResponse.getOrderNumber());
					}
					orderDetail.setStatusDescription("New");
					orderDetail.setOrderStatus(orderResponse.getOrderStatus()!=null?orderResponse.getOrderStatus():"New");
					orderDetail.setShippingInstruction(CommonUtility.validateString(orderResponse.getShippingInstruction()));
					orderDetail.setFreight(orderResponse.getFreight()!=null?orderResponse.getFreight() :0 );
					orderDetail.setTax(orderResponse.getTaxAmount()!=null?orderResponse.getTaxAmount():0);
					orderDetail.setHandling(orderResponse.getOtherCharges()!=0?orderResponse.getOtherCharges():0);
					orderDetail.setSubtotal(orderResponse.getOrderSubTotal()!=null?orderResponse.getOrderSubTotal():0);
					orderDetail.setTotal(orderResponse.getOrderTotal()!=null?orderResponse.getOrderTotal():0 + orderDetail.getFreight());
					orderDetail.setDiscount(orderResponse.getDiscountAmount()!=null?Math.abs(orderResponse.getDiscountAmount()):0 + orderDetail.getDiscount());
					orderDetail.setrOEDiscount(orderResponse.getRoeDiscountAmount()!=null?Math.abs(orderResponse.getRoeDiscountAmount()):0 + orderDetail.getrOEDiscount());
					orderDetail.setOrderedBy(orderResponse.getOrderedBy()!=null?orderResponse.getOrderedBy():"");
					orderDetail.setBillAddress(billAddress);
					orderDetail.setShipAddress(shipAddress);
					orderDetail.setPoNumber(orderResponse.getCustomerPoNumber()!=null?orderResponse.getCustomerPoNumber():"");
					orderDetail.setOrderDate(orderResponse.getOrderDate()!=null?orderResponse.getOrderDate():"");
					orderDetail.setShipViaDescription(salesOrderInput.getShipViaDescription());
					if(orderResponse.getShipToContact()!=null && orderResponse.getShipToContact().getPrimaryPhoneNumber()!=null) {
						orderDetail.setShipPhone(orderResponse.getShipToContact().getPrimaryPhoneNumber());
						orderDetail.setShipFirstName(orderResponse.getShipToContact().getFirstName()!=null?orderResponse.getShipToContact().getFirstName():"");
						orderDetail.setShipLastName(orderResponse.getShipToContact().getLastName()!=null?orderResponse.getShipToContact().getLastName():"");

					}
					if(orderResponse.getShipToContact()!=null && orderResponse.getShipToContact().getPrimaryEmailAddress()!=null) {
						orderDetail.setShipEmailAddress(orderResponse.getShipToContact().getPrimaryEmailAddress());
					}
					if(orderResponse.getCustomerCard()!=null) {
						if(CommonUtility.validateString(orderResponse.getCustomerCard().getCreditCardNumber()).length()>0) {
							orderDetail.setCreditCardNumber(orderResponse.getCustomerCard().getCreditCardNumber());
						}
					}
					ArrayList<Cimm2BCentralLineItem> cimm2bCentralLineItem = orderResponse.getLineItems();
					String NonCatalogItemId = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID_FOR_ADDTOCART"));
					if(cimm2bCentralLineItem != null && cimm2bCentralLineItem.size() > 0){
						double handlingCharges = 0.0;
						double deliveryCharges = 0.0;
						
						if(NonCatalogItemId.length()>0) {
						HashMap<String, SalesModel> lineItemsFromResponseCheckNonCatalogItems = new HashMap<String, SalesModel>();

						for (Cimm2BCentralLineItem orderResponseLineItem : cimm2bCentralLineItem) {

							SalesModel model = new SalesModel();

							model.setLineNumber(orderResponseLineItem.getLineNumber()!=null?orderResponseLineItem.getLineNumber():0);
							model.setLineItemComment(CommonUtility.validateString(orderResponseLineItem.getLineItemComment()!=null?orderResponseLineItem.getLineItemComment():""));
							model.setItemId(CommonUtility.validateNumber(orderResponseLineItem.getItemId()!=null?orderResponseLineItem.getItemId():""));
							model.setPartNumber(CommonUtility.validateString(orderResponseLineItem.getPartNumber()!=null?orderResponseLineItem.getPartNumber():""));
							model.setCustomerPartNumber(CommonUtility.validateString(orderResponseLineItem.getCustomerPartNumber()!=null?orderResponseLineItem.getCustomerPartNumber():""));
							model.setShortDesc(orderResponseLineItem.getShortDescription()!=null?orderResponseLineItem.getShortDescription():"");
							model.setUnitPrice(orderResponseLineItem.getUnitPrice());
							model.setExtPrice(orderResponseLineItem.getExtendedPrice()!=null?orderResponseLineItem.getExtendedPrice():0);
							model.setQtyordered(orderResponseLineItem.getQty());
							if(orderResponseLineItem.getQtyShipped()!=null){
								model.setQtyShipped(orderResponseLineItem.getQtyShipped().intValue());
							}
							model.setUom(orderResponseLineItem.getUom());
							model.setQtyUom(CommonUtility.validateString(orderResponseLineItem.getUomQty()!=null?orderResponseLineItem.getUomQty():"0"));
							model.setBrandName(CommonUtility.validateString(orderResponseLineItem.getBrandName()!=null?orderResponseLineItem.getBrandName():""));
							model.setManufacturer(CommonUtility.validateString(orderResponseLineItem.getManufacturer()!=null?orderResponseLineItem.getManufacturer():""));
							model.setManufacturerPartNumber(CommonUtility.validateString(orderResponseLineItem.getManufacturerPartNumber()!=null?orderResponseLineItem.getManufacturerPartNumber():""));
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
								model.setMultipleShipVia(orderResponseLineItem.getShipMethod());
								model.setMultipleShipViaDesc(orderResponseLineItem.getShippingBranch());
							}
							lineItemsFromResponseCheckNonCatalogItems.put(orderResponseLineItem.getPartNumber(), model);
						}
						
						for (ProductsModel orderItem : orderDetails) {
							lineItemsFromResponseCheckNonCatalogItems.remove(orderItem.getPartNumber());
						}
						List<String> handlingListItemsArray = Arrays.asList(handlingListItems.split(","));
						List<String> deliveryListItemsArray = Arrays.asList(deliveryListItems.split(","));
						Iterator<Entry<String, SalesModel>> it = lineItemsFromResponseCheckNonCatalogItems.entrySet().iterator();
						if(salesOrderInput.getOrderId()>0){
							while (it.hasNext()) {
								Map.Entry<String, SalesModel> pair = (Map.Entry<String, SalesModel>)it.next();
	
								if(handlingListItemsArray.contains(pair.getValue().getPartNumber())){
									handlingCharges = handlingCharges + pair.getValue().getExtPrice();
								}else if(deliveryListItemsArray.contains(pair.getValue().getPartNumber())){
									deliveryCharges = deliveryCharges + pair.getValue().getExtPrice();
								}
	
								ProductsModel nonCatalogItem = new ProductsModel();
								//nonCatalogItem.setLineNumber(cimm2bCentralLineItem.get(i).getLineNumber());
								nonCatalogItem.setLineItemComment(pair.getValue().getLineItemComment());
								nonCatalogItem.setItemId(CommonUtility.validateNumber(NonCatalogItemId));
								nonCatalogItem.setPartNumber(pair.getValue().getPartNumber());
								nonCatalogItem.setCustomerPartNumber(pair.getValue().getCustomerPartNumber());
								nonCatalogItem.setShortDesc(pair.getValue().getShortDesc()!=null?pair.getValue().getShortDesc():"");
								nonCatalogItem.setPrice(pair.getValue().getUnitPrice());
								nonCatalogItem.setExtendedPrice(pair.getValue().getExtPrice());
								nonCatalogItem.setQty(pair.getValue().getQtyShipped()!=0?pair.getValue().getQtyShipped():1);
								//model.setQtyShipped(pair.getValue().getQtyShipped().intValue());
								nonCatalogItem.setUom(pair.getValue().getUom());
								nonCatalogItem.setQtyUOM(CommonUtility.validateString(pair.getValue().getQtyUom()!=null?pair.getValue().getQtyUom():"0"));
								nonCatalogItem.setManufacturerName(pair.getValue().getManufacturer());
								nonCatalogItem.setManufacturerPartNumber(pair.getValue().getManufacturer()!=null?pair.getValue().getManufacturer():"");
	
								conn = ConnectionManager.getDBConnection();
								SalesAction.saveOrderItems(conn, salesOrderInput.getOrderId(), nonCatalogItem, "", 0, 0, "");
								it.remove(); // avoids a ConcurrentModificationException
							}
						}
					}else {
						for (Cimm2BCentralLineItem orderResponseLineItem : cimm2bCentralLineItem) {

							SalesModel model = new SalesModel();
							
							model.setLineNumber(orderResponseLineItem.getLineNumber()!=null?orderResponseLineItem.getLineNumber():0);
							model.setLineItemComment(CommonUtility.validateString(orderResponseLineItem.getLineItemComment()!=null?orderResponseLineItem.getLineItemComment():""));
							model.setItemId(CommonUtility.validateNumber(orderResponseLineItem.getItemId()!=null?orderResponseLineItem.getItemId():""));
							model.setPartNumber(CommonUtility.validateString(orderResponseLineItem.getPartNumber()!=null?orderResponseLineItem.getPartNumber():""));
							model.setCustomerPartNumber(CommonUtility.validateString(orderResponseLineItem.getCustomerPartNumber()!=null?orderResponseLineItem.getCustomerPartNumber():""));
							model.setShortDesc(orderResponseLineItem.getShortDescription()!=null?orderResponseLineItem.getShortDescription():"");
							model.setUnitPrice(orderResponseLineItem.getUnitPrice());
							if(CommonUtility.customServiceUtility()!=null) {
								CommonUtility.customServiceUtility().setQtyOnERP(model,orderResponseLineItem);
							}
							if(CommonUtility.customServiceUtility()!=null) {
								CommonUtility.customServiceUtility().getUnitPriceofOrders(model,orderResponseLineItem,orderResponse);
							}
							if(orderResponseLineItem.getExtendedPrice()!=null && orderResponseLineItem.getExtendedPrice()>0) {
								model.setExtPrice(orderResponseLineItem.getExtendedPrice());
							}else {
							model.setExtPrice(orderResponseLineItem.getQty()*orderResponseLineItem.getUnitPrice());
							}
							model.setQtyordered(orderResponseLineItem.getQty());
							if(orderResponseLineItem.getQtyShipped()!=null){
								model.setQtyShipped(orderResponseLineItem.getQtyShipped().intValue());
							}
							model.setUom(orderResponseLineItem.getUom());
							model.setQtyUom(CommonUtility.validateString(orderResponseLineItem.getUomQty()!=null?orderResponseLineItem.getUomQty():"0"));
							model.setImageName(CommonUtility.validateString(orderResponseLineItem.getImageName()!=null?orderResponseLineItem.getImageName():""));
							model.setBrandName(CommonUtility.validateString(orderResponseLineItem.getBrandName()!=null?orderResponseLineItem.getBrandName():""));
							model.setManufacturer(CommonUtility.validateString(orderResponseLineItem.getManufacturer()!=null?orderResponseLineItem.getManufacturer():""));
							model.setManufacturerPartNumber(CommonUtility.validateString(orderResponseLineItem.getManufacturerPartNumber()!=null?orderResponseLineItem.getManufacturerPartNumber():""));
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
								model.setMultipleShipVia(orderResponseLineItem.getShipMethod());
								model.setMultipleShipViaDesc(orderResponseLineItem.getShippingBranch());
								WarehouseModel wareHouseDetail = new WarehouseModel();
								wareHouseDetail = UsersDAO.getWareHouseDetailsByCode(orderResponseLineItem.getShippingBranch());
								if(wareHouseDetail!=null) {
									model.setWareHouseDetails(wareHouseDetail);
								}
							}
							model.setAltPartNumber1(CommonUtility.validateString(orderResponseLineItem.getStorePartNumber()));
							model.setAltPartNumber2(CommonUtility.validateString(orderResponseLineItem.getSupplierPartNumber()));
							orderList.add(model);
						}
						
					}
						orderDetail.setOrderList(orderList);
						orderDetail.setHandling(orderResponse.getOtherCharges());
						orderDetail.setDeliveryCharge(deliveryCharges);
					}
					orderDetail.setStatus(CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()));
					orderDetail.setOrderStatusDesc("Failure");
				}
			}else{
				//orderDetail.setStatusDescription(orderResponseEntity.getStatus().getMessage());
				if(CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()).length()>0 && CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()).equalsIgnoreCase("424")){
					errorMessageToDisplay=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("errormsgtodisplay.labels"));
					orderDetail.setStatusDescription(errorMessageToDisplay);
				}else{
					orderDetail.setStatusDescription(orderResponseEntity.getStatus().getMessage());
				}
				orderDetail.setStatus(CommonUtility.validateParseIntegerToString(orderResponseEntity.getStatus().getCode()));
				orderDetail.setSendMailFlag(false);
			}

		}
		catch (SQLException e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBConnection(conn);
		}

		return orderDetail;
	}
	@SuppressWarnings("unchecked")
	public static SalesModel getMaxRecallInformation(SalesModel salesOrderInput) {
		SalesModel salesOrderOutput = new SalesModel();
		ArrayList<SalesModel> maxOrderList = new ArrayList<SalesModel>();
		List<Cimm2BCentralMaxRecallRequest> listOfCimm2BCentralMaxRecallRequest=new ArrayList<Cimm2BCentralMaxRecallRequest>();
		try{
			String MAX_RECALL_GET_DOCUMENT = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MAX_RECALL_GET_DOCUMENT"));
			Cimm2BCentralMaxRecallRequest cimm2BCentralMaxRecallRequest = new Cimm2BCentralMaxRecallRequest();
			cimm2BCentralMaxRecallRequest.setSearchKeyword(CommonUtility.validateString(salesOrderInput.getSearchKeyword()));
			cimm2BCentralMaxRecallRequest.setUserValue(CommonUtility.validateString(salesOrderInput.getUserValue()));
			cimm2BCentralMaxRecallRequest.setDocumentId(CommonUtility.validateString(salesOrderInput.getDocumentId()));

			listOfCimm2BCentralMaxRecallRequest.add(cimm2BCentralMaxRecallRequest);
			if(CommonUtility.validateParseIntegerToString(salesOrderInput.getOrderSuffix())!=null && !(CommonUtility.validateString(salesOrderInput.getSearchKeywordForOrderSuffix()).length()<=0)){
				
				Cimm2BCentralMaxRecallRequest cimm2BCentralMaxRecallRequestSuffix = new Cimm2BCentralMaxRecallRequest();
				cimm2BCentralMaxRecallRequestSuffix.setSearchKeyword(CommonUtility.validateString(salesOrderInput.getSearchKeywordForOrderSuffix()));
				cimm2BCentralMaxRecallRequestSuffix.setUserValue("0"+CommonUtility.validateString(String.valueOf(salesOrderInput.getOrderSuffix())));
				cimm2BCentralMaxRecallRequestSuffix.setDocumentId(CommonUtility.validateString(salesOrderInput.getDocumentId()));
				listOfCimm2BCentralMaxRecallRequest.add(cimm2BCentralMaxRecallRequestSuffix);
				
			}
			
			Cimm2BCentralResponseEntity maxListResponse = null;
			maxListResponse = Cimm2BCentralClient.getInstance().getDataObject(MAX_RECALL_GET_DOCUMENT,"POST",listOfCimm2BCentralMaxRecallRequest, Cimm2BCentralMaxRecallResponse.class);
			
			List<Cimm2BCentralMaxRecallResponse> maxDataList = null;
		
			if(maxListResponse!=null && maxListResponse.getData()!=null && maxListResponse.getStatus().getCode() == 200 ){
				maxDataList = (List<Cimm2BCentralMaxRecallResponse>) maxListResponse.getData();
				if(maxDataList!=null){
					for(Cimm2BCentralMaxRecallResponse maxList : maxDataList){
						SalesModel salesModel = new SalesModel();
						salesModel.setMaxData(maxList.getData());
						salesModel.setMaxTitle(maxList.getTitle());
						salesModel.setMaxName(maxList.getName());
						salesModel.setMaxDate(maxList.getDate());
						salesModel.setMaxUrl(maxList.getUrl());
						maxOrderList.add(salesModel);
						
					}
				}
				salesOrderOutput.setMaxRecallList(maxOrderList);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return salesOrderOutput;
	}
	
	public static SalesModel getMaxRecallInfoByDocId(SalesModel salesOrderInput) {
		String MAX_RECALL_GET_DOCUMENT = "";
		SalesModel salesOrderOutput = new SalesModel();
		try{
			 Cimm2BCentralResponseEntity maxPdfURLResponse = null;
			if(CommonUtility.validateString(salesOrderInput.getPdfDocumentId()).length() > 0) {
				MAX_RECALL_GET_DOCUMENT = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MAX_RECALL_GET_PDF_DOCUMENT_BY_ID"))+CommonUtility.validateString(salesOrderInput.getPdfDocumentId());
				maxPdfURLResponse = Cimm2BCentralClient.getInstance().getDataObject(MAX_RECALL_GET_DOCUMENT,"GET",null, Cimm2BCentralMaxRecallResponse.class);
			}	
			if(maxPdfURLResponse!= null && maxPdfURLResponse.getStatus().getCode()==200) {
				salesOrderOutput.setMaxUrl(maxPdfURLResponse.getData().toString());
				salesOrderOutput.setStatus(CommonUtility.validateParseIntegerToString(maxPdfURLResponse.getStatus().getCode()));
			}
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return salesOrderOutput;
	}
	
}