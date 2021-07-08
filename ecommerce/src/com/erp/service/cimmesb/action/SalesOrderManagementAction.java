package com.erp.service.cimmesb.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.http.HttpStatus;
import org.apache.struts2.ServletActionContext;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.service.UserManagement;
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
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCreditCardDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerCard;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOpenOrderInformation;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPaypalItemList;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralProfileList;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSalesOrderHistoryDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSalesOrderHistoryRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipVia;
import com.erp.service.cimmesb.utils.CimmESBServiceUtils;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.SalesOrderManagementModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.avalara.AvalaraUtility;
import com.unilog.cimmesb.client.ecomm.request.BillingAddress;
import com.unilog.cimmesb.client.ecomm.request.CimmAddressRequest;
import com.unilog.cimmesb.client.ecomm.request.CimmCCTransaction;
import com.unilog.cimmesb.client.ecomm.request.CimmContactRequest;
import com.unilog.cimmesb.client.ecomm.request.CimmCspOrderRequest;
import com.unilog.cimmesb.client.ecomm.request.CimmOrderRequest;
import com.unilog.cimmesb.client.ecomm.request.Credential;
import com.unilog.cimmesb.client.ecomm.request.PaymentGatewayRequest;
import com.unilog.cimmesb.client.ecomm.request.PaymentGatewayRequestV2;
import com.unilog.cimmesb.client.ecomm.request.Transaction;
import com.unilog.cimmesb.client.ecomm.response.CimmCspResponse;
import com.unilog.cimmesb.client.ecomm.response.CimmV2TaxResponse;
import com.unilog.cimmesb.client.ecomm.response.PaymentGatewayResponse;
import com.unilog.cimmesb.client.ecomm.response.TopCategoryDTO;
import com.unilog.cimmesb.client.ecomm.response.TopHitItemsDTO;
import com.unilog.cimmesb.client.exception.RestClientException;
import com.unilog.cimmesb.client.request.CimmOrderType;
import com.unilog.cimmesb.client.request.HostedPayment;
import com.unilog.cimmesb.client.request.RestRequest;
import com.unilog.cimmesb.client.request.ShippingAddress;
import com.unilog.cimmesb.client.response.CimmLineItem;
import com.unilog.cimmesb.client.response.CimmOrder;
import com.unilog.cimmesb.client.response.CimmShippingMethod;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BAddress;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCResponse;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BLineItem;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BOpenOrderInfo;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BOrder;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BPagedResponse;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BShippingMethod;
import com.unilog.cimmesb.ecomm.cimm2bc.model.CimmInvoiceResponse;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.SalesAction;
import com.unilog.sales.SalesDAO;
import com.unilog.sales.SalesModel;
import com.unilog.security.SecureData;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

public class SalesOrderManagementAction {
	static final Logger logger = LoggerFactory.getLogger(SalesOrderManagementAction.class);
	/*public static SalesModel submitSalesOrder(SalesOrderManagementModel salesOrderInput) {

		SalesModel orderDetail = new SalesModel();
		Connection conn = null;
		try
		{
			Cimm2BOrder orderResponse = processCreateOrder(salesOrderInput);
			if(orderResponse != null) {
				orderDetail.setExternalSystemId(orderResponse.getOrderERPId());
				orderDetail.setOrderNum(orderResponse.getOrderERPId());
				orderDetail.setErpOrderNumber(orderResponse.getOrderERPId());
				orderDetail.setOrderStatus(orderResponse.getOrderStatus().getOrderStatus());
			}
		}
		catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBConnection(conn);
		}

		return orderDetail;

	}*/
	
	@SuppressWarnings("unused")
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
		    DecimalFormat df = CommonUtility.getPricePrecision(session);
			LinkedHashMap<String, Cimm2BCentralShipVia> shipMap = new LinkedHashMap<String, Cimm2BCentralShipVia>();
			ArrayList<ProductsModel> orderDetails = SalesDAO.getOrderDetails(salesOrderInput.getSession(), salesOrderInput.getOrderId());
			 if(CommonDBQuery.getSystemParamtersList().get("ORDER_SUBMIT_V2") !=null &&CommonDBQuery.getSystemParamtersList().get("ORDER_SUBMIT_V2").equalsIgnoreCase("Y")){
				orderDetails = salesOrderInput.getOrderItems();
			 }
			 if(salesOrderInput.getOrderItems() == null) {
				 salesOrderInput.setOrderItems(orderDetails);
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
						if(CommonUtility.validateString(salesLocationId).length()>0){
							cimm2bCentralLineItem.setShippingBranch(salesLocationId);
						}else{
							cimm2bCentralLineItem.setShippingBranch(wareHousecode);	
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
						cimm2bCentralLineItem.setQty(item.getQty());
						cimm2bCentralLineItem.setManufacturer(item.getManufacturerName());
						cimm2bCentralLineItem.setLineItemComment(item.getLineItemComment());
						cimm2bCentralLineItem.setUnitPrice(item.getPrice());
						cimm2bCentralLineItem.setPage_title(item.getPageTitle());
						cimm2bCentralLineItem.setUomQty(item.getQtyUOM());
						cimm2bCentralLineItem.setItemId(item.getPartNumber());
						cimm2bCentralLineItem.setListPrice(item.getListPrice());
						cimm2bCentralLineItem.setShortDescription(item.getShortDesc()!=null?item.getShortDesc():".");
						cimm2bCentralLineItem.setProductCategory(item.getProductCategory()!=null?item.getProductCategory():"");
						/**
						 *Below code Written is for Turtle and Hughes for custom UOM *Reference- Nitesh
						 */
						if(CommonUtility.customServiceUtility()!=null) {
							double extndPrice = CommonUtility.customServiceUtility().getExtendedPrice(item.getPrice(),item.getQty(),item.getSaleQty(),item.getSalesQuantity());
							if(extndPrice > 0) {
								cimm2bCentralLineItem.setExtendedPrice(extndPrice);
								cimm2bCentralLineItem.setUom("");
							}else{
								cimm2bCentralLineItem.setUom(item.getUom() != null ? item.getUom().trim() : "");
								cimm2bCentralLineItem.setExtendedPrice(item.getPrice()*item.getQty());
							}
						}
						else{
							cimm2bCentralLineItem.setUom(item.getUom() != null ? item.getUom().trim() : "");
							cimm2bCentralLineItem.setExtendedPrice(item.getPrice()*item.getQty());
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
						}
						UnilogFactoryInterface serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
						if(serviceClass!=null && cimm2bCentralLineItem!=null) {
							Cimm2BCentralLineItem cimm2bCentralLineItemReArrange = serviceClass.submitSalesOrderItemList(cimm2bCentralLineItem);
							if(cimm2bCentralLineItemReArrange!=null){
								cimm2bCentralLineItem = cimm2bCentralLineItemReArrange;
							}
						}
						lineItems.add(cimm2bCentralLineItem);
						if(item.getCartTotal()>0) {
							subTotal = item.getCartTotal();
						}else {
							if(cimm2bCentralLineItem!=null && cimm2bCentralLineItem.getListPrice()>0){
								  subTotal=subTotal+(cimm2bCentralLineItem.getListPrice());
							  }else{
								  subTotal=subTotal+(cimm2bCentralLineItem.getExtendedPrice());
							  }
							}
						
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
			orderRequest.setBillingAddress(billingAddress);
			orderRequest.setShippingAddress(shippingAddress);
			orderRequest.setShipToContact(shipToContact);
			orderRequest.setBillToContact(billToContact);
			orderRequest.setLineItems(lineItems);
			orderRequest.setOrderDate(orderDate);
			orderRequest.setOrderNotes(CommonUtility.validateString(salesOrderInput.getReqDate()));
			orderRequest.setCustomerERPId(customerErpId);
			orderRequest.setUserERPId((String) session.getAttribute(Global.USERID_KEY));
			orderRequest.setBranchCode(branchCode);
			if(CommonUtility.customServiceUtility()!=null){
				CommonUtility.customServiceUtility().getBranchCode(orderRequest,wareHousecode);
			}
			orderRequest.setCustomerPoNumber(salesOrderInput.getPurchaseOrderNumber()!=null?salesOrderInput.getPurchaseOrderNumber().toUpperCase():"");
			orderRequest.setShippingInstruction(salesOrderInput.getShippingInstruction());
			orderRequest.setOrderERPId(salesOrderInput.getOrderERPId());
			orderRequest.setWarehouseLocation(CommonUtility.validateString(salesOrderInput.getUserSelectedLocation()).trim().length()>0?salesOrderInput.getUserSelectedLocation():wareHousecode);
			orderRequest.setOrderNumber(orderNumber);
			orderRequest.setBackOrderSequence(backOrderSeqNumber);
			orderRequest.setOrderComment(CommonUtility.validateString(salesOrderInput.getOrderNotes()));
			orderRequest.setOrderedBy(CommonUtility.validateString(salesOrderInput.getOrderedBy()));
			orderRequest.setRequiredDate(CommonUtility.validateString(salesOrderInput.getReqDate()));
			orderRequest.setFreightOut(CommonUtility.validateDoubleNumber(salesOrderInput.getFrieghtCharges()));
			orderRequest.setFreight(CommonUtility.validateDoubleNumber(salesOrderInput.getFrieghtCharges()));
			orderRequest.setOrderSubTotal(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(subTotal)));
			orderRequest.setBackOrderTotal(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(subTotal)));
			if(salesOrderInput.getDiscountAmount()>0){
				orderRequest.setDiscountAmount(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(salesOrderInput.getDiscountAmount())));
				orderRequest.setOrderTotal(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString((subTotal+orderRequest.getFreight()+salesOrderInput.getOrderTax())-salesOrderInput.getDiscountAmount())));
			}
			else {
				orderRequest.setOrderTotal(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(subTotal+orderRequest.getFreight()+salesOrderInput.getOrderTax())));
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
			orderRequest.setTaxAmount(salesOrderInput.getOrderTax());
			orderRequest.setCustomerName(billAddress.getShipToName());
			orderRequest.setFreightCode(salesOrderInput.getFreightCode());
			orderRequest.setOrderType(CommonUtility.validateString(salesOrderInput.getOrderType()));
			if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels")).length()>0){
				orderRequest.setOrderSource(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("orderType.labels"):"");
			}
			orderRequest.setOrderDisposition(salesOrderInput.getOrderDisposition()!=null?salesOrderInput.getOrderDisposition():"");
			if(CommonUtility.validateString(salesLocationId).length()>0){
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
					customerCard.setAuthorizationNumber(salesOrderInput.getCreditCardValue().getCreditCardTransactionID());
					customerCard.setTransactionId(salesOrderInput.getCreditCardValue().getCreditCardToken());
					customerCard.setCreditCardType(salesOrderInput.getCreditCardValue().getCreditCardType());
					customerCard.setCvv(salesOrderInput.getCreditCardValue().getCreditCardCvv2VrfyCode());
					customerCard.setCardHolderName(salesOrderInput.getCreditCardValue().getCardHolder());
					customerCard.setCreditCardNumber(salesOrderInput.getCreditCardValue().getCreditCardNumber());
					customerCard.setExpiryDate(salesOrderInput.getCreditCardValue().getDate());
					if(CommonUtility.customServiceUtility() != null) {
						CommonUtility.customServiceUtility().configureCreditCardDetails(customerCard);
						CommonUtility.customServiceUtility().getAuthAmount(customerCard,salesOrderInput);
					}
					
					//customerCard.setExpiryDate(salesOrderInput.getCreditCardValue().getDate());
					customerCard.setCardHolderFirstName(CommonUtility.validateString(billAddress.getFirstName()));
					customerCard.setCardHolderLastName(CommonUtility.validateString(billAddress.getLastName()));
					customerCard.setExpiryMonth(expMonth);
					customerCard.setExpiryYear(expYear);
					customerCard.setExpiryYear(expYear);
					if(CommonUtility.validateDoubleNumber(salesOrderInput.getCreditCardValue().getCreditCardAmount())>0){
						customerCard.setAmount(CommonUtility.validateDoubleNumber(salesOrderInput.getCreditCardValue().getCreditCardAmount()));
					}else{
						customerCard.setAmount(orderRequest.getOrderTotal());
					}
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
						cimm2bCentralShipVia.setShipViaCode("WILLCALL");// P21 standard name is willcall, If the user selected ship method = shipvia.label.willcallerpcode .. then WILLCALL will be set to TRUE on order for P21
						cimm2bCentralShipVia.setShipViaDescription("WILLCALL");// P21 standard name is willcall, If the user selected ship method = shipvia.label.willcallerpcode .. then WILLCALL will be set to TRUE on order for P21
					}else{
						//cimm2bCentralShipVia.setShipViaCode(salesOrderInput.getShipViaMethod());
						cimm2bCentralShipVia.setShipViaCode(salesOrderInput.getShipVia());
						cimm2bCentralShipVia.setShipViaDescription(salesOrderInput.getShipViaDescription()!=null?salesOrderInput.getShipViaDescription():salesOrderInput.getShipViaMethod());
					}
					cimm2bCentralShipVia.setShipViaErpId(salesOrderInput.getShipVia());
					shipVia.add(cimm2bCentralShipVia);
					orderRequest.setShipVia(shipVia);
				}
			}
			
			// CIMM ESB
			CimmContactRequest cimmContactRequest=new CimmContactRequest();
			if(session!=null && CommonUtility.validateString((String)session.getAttribute("loginCustomerName")).length()>0) {
			cimmContactRequest.setCompanyName(CommonUtility.validateString((String)session.getAttribute("loginCustomerName")));
			}
			CimmOrderRequest cimmOrderRequest=new CimmOrderRequest();
			cimmOrderRequest.setRequestType(salesOrderInput.getOrderType()!=null?salesOrderInput.getOrderType():"webOrder");
			cimmOrderRequest.setRequiredDate(!salesOrderInput.getReqDate().isEmpty()?CommonUtility.convertToDateFromString(salesOrderInput.getReqDate()):null);
			if(salesOrderInput.getOrderType().equalsIgnoreCase("CREDIT_CARD"))
			{
				cimmOrderRequest.setOrderType(CimmOrderType.CREDIT_CARD);
			}
			cimmOrderRequest.setCustomerPoNumber(salesOrderInput.getPurchaseOrderNumber());
			cimmOrderRequest.setShippingInstruction(salesOrderInput.getShippingInstruction());
			cimmOrderRequest.setOrderComment(CommonUtility.validateString(orderRequest.getOrderComment()));
			cimmOrderRequest.setReleaseNumber(CommonUtility.validateString(orderRequest.getReleaseNumber()));
			if(CommonUtility.validateNumber(salesOrderInput.getJobId())>0) {
				cimmOrderRequest.setJobId(salesOrderInput.getJobId());
			}
			cimmOrderRequest.setOrderDate(new SimpleDateFormat("MM/dd/yyyy").parse(orderDate));
			cimmOrderRequest.setOrderTotal(Double.parseDouble(df.format(orderRequest.getOrderTotal())));
			cimmOrderRequest.setOrderSubTotal(orderRequest.getOrderSubTotal());
			if(salesOrderInput.getSelectedBranch()!=null && salesOrderInput.getSelectedBranch().length()>0){
				cimmOrderRequest.setWarehouseLocation(salesOrderInput.getSelectedBranch());
			}
			else{
			cimmOrderRequest.setWarehouseLocation(wareHousecode);
			}
			
			cimmOrderRequest.setBillToContact(cimmContactRequest);
			cimmOrderRequest.setBillingAddress(extractCimmAddress(salesOrderInput.getBillAddress()));
			cimmOrderRequest.setShippingAddress(extractCimmAddress(salesOrderInput.getShipAddress()));
			cimmOrderRequest.setLineItems(extractLineItems(salesOrderInput.getOrderItems()));
			cimmOrderRequest.setShipVia(extractCimmShip(orderRequest.getShipVia()));
			cimmOrderRequest.setOrderDate(CommonUtility.convertToDateFromString(orderRequest.getOrderDate()));
			cimmOrderRequest.setFreightAmount(CommonUtility.validateDoubleNumber(salesOrderInput.getShippingAndHandlingFee()));
			cimmOrderRequest.setOrderedBy( salesOrderInput.getOrderedBy());
			cimmOrderRequest.setTaxAmount(salesOrderInput.getOrderTax()); 
			cimmOrderRequest.setDiscountAmount(orderRequest.getDiscountAmount());
			cimmOrderRequest.setDeliveryTerm(CommonUtility.validateString(salesOrderInput.getNotesIndicator()));
			cimmOrderRequest.setTransactionType(CommonUtility.validateString(salesOrderInput.getTransactionType()));
			cimmOrderRequest.setOrderDisposition(CommonUtility.validateString(salesOrderInput.getOrderDisposition()));
			if(customerCard!=null){
				cimmOrderRequest.setCustomerCard(extractCustomerCard(orderRequest.getCustomerCard()));
			}
			
		  //RestRequest<CimmOrderRequest> request =buildRequest(cimmOrderRequest, CimmOrderRequest.class, session);
			SecureData password=new SecureData();
		  RestRequest<CimmOrderRequest> request=null;
			if(CommonUtility.validateString(salesOrderInput.getOrderType()).equalsIgnoreCase("RFQ")) {
				 request = RestRequest.<CimmOrderRequest>builder()
						.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
						.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
						.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
						.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
						.withBody(cimmOrderRequest)
						.addQueryParameter("ordermode", "quote")
						.build();
			}
			else {
				request = RestRequest.<CimmOrderRequest>builder()
						.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
						.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
						.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
						.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
						.withBody(cimmOrderRequest)
						.build();
			}
			Cimm2BCResponse<Cimm2BOrder> response = CimmESBServiceUtils.getCreateOrderService().createCustomerOrder(request);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("CIMM ESB Order Request:"+mapper.writeValueAsString(cimmOrderRequest));
			System.out.println("CIMM ESB Order Response:"+mapper.writeValueAsString(response));
			DozerBeanMapper  dozzer = new DozerBeanMapper();
			Cimm2BCentralOrder orderResponse = new Cimm2BCentralOrder();
			dozzer.map(response.getData(),orderResponse); // Dozzer Mapper
			
			if(orderResponse!=null && response.getStatus() != null && response.getStatus().getCode() == HttpStatus.SC_OK){
				//orderResponse.setStatus(response.getStatus().);
				CommonUtility.customServiceUtility().customizeOrderResponse(salesOrderInput.getOrderItems(), orderResponse.getLineItems());
				
				ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
				if(orderResponse != null && ((CommonUtility.validateString(orderResponse.getOrderNumber()).length() > 0) || (CommonUtility.validateString(orderResponse.getOrderERPId()).length() > 0) || (CommonUtility.validateString(orderResponse.getCartId()).length() > 0))){
					orderDetail.setExternalCartId(quoteResponse != null ? quoteResponse.getExternalCartId() : "");
					orderDetail.setUserId(CommonUtility.validateNumber(orderResponse.getUserERPId()));
					if(CommonUtility.validateString(orderResponse.getOrderERPId()).trim().length() > 0){
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
					orderDetail.setOrderType(orderResponse.getOrderType()!=null?orderResponse.getOrderType():"");
					if(response.getData().getRequiredDate()!=null){
					orderDetail.setReqDate(CommonUtility.convertToStringFromDate(response.getData().getRequiredDate()));
					}
					orderDetail.setStatusDescription("New");
					orderDetail.setStatus(orderResponse.getOrderStatus()!=null?orderResponse.getOrderStatus():"New");
					orderDetail.setShippingInstruction(CommonUtility.validateString(orderResponse.getShippingInstruction()));
					orderDetail.setFreight(orderResponse.getFreight()!=null?orderResponse.getFreight() :0 );
					orderDetail.setTax(orderResponse.getTaxAmount()!=null?orderResponse.getTaxAmount():0);
					orderDetail.setSubtotal(orderResponse.getOrderSubTotal()!=null?orderResponse.getOrderSubTotal():0);
					orderDetail.setTotal(orderResponse.getOrderTotal()!=null?orderResponse.getOrderTotal():0 + orderDetail.getFreight());
					orderDetail.setDiscount(orderResponse.getDiscountAmount()!=null?orderResponse.getDiscountAmount():0 + orderDetail.getDiscount());
					orderDetail.setOrderedBy(orderResponse.getOrderedBy()!=null?orderResponse.getOrderedBy():"");
					orderDetail.setBillAddress(billAddress);
					orderDetail.setShipAddress(shipAddress);
					orderDetail.setPoNumber(orderResponse.getCustomerPoNumber()!=null?orderResponse.getCustomerPoNumber():"");
					if(response.getData().getOrderDate()!=null){
					orderDetail.setOrderDate(CommonUtility.convertToStringFromDate(response.getData().getOrderDate()));
					}
					orderDetail.setCustomerReleaseNumber(CommonUtility.validateString(orderResponse.getReleaseNumber()));
					orderDetail.setJobId(CommonUtility.validateString(orderResponse.getJobId()));
					orderDetail.setOrderNotes(CommonUtility.validateString(orderResponse.getOrderComment()));
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
							model.setRushFlag(CommonUtility.validateString(orderResponseLineItem.getRushFlag()));
							orderList.add(model);
						}
						
					}
						orderDetail.setOrderList(orderList);
						orderDetail.setHandling(handlingCharges);
						orderDetail.setDeliveryCharge(deliveryCharges);
					}
					if(orderResponse.getStatus() != null) {
						orderDetail.setStatus(CommonUtility.validateParseIntegerToString(orderResponse.getStatus().getCode()));
					}
					
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
				}
			}else{
				//orderDetail.setStatusDescription(orderResponseEntity.getStatus().getMessage());
				if(CommonUtility.validateParseIntegerToString(orderResponse.getStatus().getCode()).length()>0 && CommonUtility.validateParseIntegerToString(orderResponse.getStatus().getCode()).equalsIgnoreCase("424")){
					errorMessageToDisplay=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("errormsgtodisplay.labels"));
					orderDetail.setStatusDescription(errorMessageToDisplay);
				}else{
					orderDetail.setStatusDescription(orderResponse.getStatus().getMessage());
				}
				orderDetail.setStatus(CommonUtility.validateParseIntegerToString(orderResponse.getStatus().getCode()));
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

	/*public static ArrayList<SalesModel> OpenOrders(SalesModel salesInputParameter) {
		ArrayList<SalesModel> openOrderList = new ArrayList<SalesModel>();
		try {
			List<Cimm2BOpenOrderInfo> allOrders = processOrdersRequest(salesInputParameter);
			if (allOrders.size() > 0) {
				openOrderList = (ArrayList<SalesModel>) extractSalesOrders(allOrders);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return openOrderList;
	}*/
	
	@SuppressWarnings("unused")
	public static ArrayList<SalesModel> OpenOrders(SalesModel salesInputParameter) {
		ArrayList<SalesModel> openOrderList = new ArrayList<SalesModel>();
		HttpSession session = salesInputParameter.getSession();
		SalesModel pages=new SalesModel();
		Cimm2BCentralOpenOrderInformation cimm2BCntralOpenOrderList = new Cimm2BCentralOpenOrderInformation();
		try{
			String customerErpId = salesInputParameter.getCustomerNumber();
			
			// CIMM ESB
			SecureData password=new SecureData();
			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
					.addQueryParameter("filter.orderStatus", salesInputParameter.getOrderStatusType()!=null?salesInputParameter.getOrderStatusType():"OPEN")
					.build();
			@SuppressWarnings("rawtypes")
			Cimm2BCResponse<Cimm2BPagedResponse<List<Cimm2BOpenOrderInfo>>> response = CimmESBServiceUtils.getOrderService().getCurrentCustomerOrders(request);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("GET QUOTE REQUEST : "+mapper.writeValueAsString(request));
			System.out.println("GET QUOTE RESPONSE : "+mapper.writeValueAsString(response));
			DozerBeanMapper  dozzer = new DozerBeanMapper();
			dozzer.map(response.getData(),cimm2BCntralOpenOrderList);
			
			List<Cimm2BOpenOrderInfo> Cimm2BCentralOpenOrderInformationDetails = response.getData().getDetails();
			if(cimm2BCntralOpenOrderList!=null){
				pages.setTotalPages(cimm2BCntralOpenOrderList.getTotalPages());
				pages.setPageNumber(cimm2BCntralOpenOrderList.getPageNumber());
				pages.setTotal(cimm2BCntralOpenOrderList.getPageSize());
				if(cimm2BCntralOpenOrderList != null){
					
					for(Cimm2BOpenOrderInfo openOrder : Cimm2BCentralOpenOrderInformationDetails){
						SalesModel salesModel = new SalesModel();
						salesModel.setCustomerERPId(openOrder.getCustomerERPId()!=null?openOrder.getCustomerERPId():null);
						salesModel.setOrderNum(openOrder.getOrderERPId());
					/*	if(openOrder.getGenerationId()!=null) {
							salesModel.setGeneId(openOrder.getGenerationId());
						}else {*/
							salesModel.setGeneId(openOrder.getBackOrderSequence());
					/*	}*/
							if(openOrder.getShippingAddress()!=null)
							{
							salesModel.setShipCity(openOrder.getShippingAddress().getCity());
							salesModel.setCustomerCountry(openOrder.getShippingAddress().getCountryCode());
							salesModel.setShipZipCode(openOrder.getShippingAddress().getZipCode());
							}
							
						salesModel.setOrderDate(CommonUtility.convertToStringFromDate(openOrder.getOrderDate()));
					/*	salesModel.setManufacturer(openOrder.getSupplier());
						salesModel.setPartNumber(openOrder.getPart());
						salesModel.setDescription1(openOrder.getDescription1());
						salesModel.setDescription2(openOrder.getDescription2());
						salesModel.setDescription(openOrder.getLocation());*/	 // ---- For location field 
						salesModel.setTotal(openOrder.getOrderTotal()!=null?openOrder.getOrderTotal():0);
						salesModel.setPoNumber(openOrder.getPoNumber());
						salesModel.setInvoiceNumber(openOrder.getInvoiceNumber());
						salesModel.setShipToName(openOrder.getShipToName());
						salesModel.setOpenOrderAmount(openOrder.getOrderTotal()!=null?openOrder.getOrderTotal():0);
						salesModel.setOrderedBy(openOrder.getOrderedBy());
						salesModel.setOrderStatus(openOrder.getOrderStatus());
						if(openOrder.getPromiseDate()!=null) {
							salesModel.setPromiseDate(CommonUtility.convertToStringFromDate(openOrder.getPromiseDate()));
							}
					/*	salesModel.setShipDate(CommonUtility.convertToStringFromDate(openOrder.getShipDate()));*/
						if(openOrder.getRequiredDate()!=null) {
						salesModel.setRequiredByDate(CommonUtility.convertToStringFromDate(openOrder.getRequiredDate()));
						}
						salesModel.setOrderSuffix(CommonUtility.validateNumber(openOrder.getOrderSuffix()));
						salesModel.setInvoiceAmount(openOrder.getInvoiceAmount()!=null?openOrder.getInvoiceAmount():0);
						openOrderList.add(salesModel);
					}
				}
			}
		}catch (Exception e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return openOrderList;
		}
	
	private static List<SalesModel> extractSalesOrders(List<Cimm2BOpenOrderInfo> orders) {
		return orders.stream()
		.map(order -> extractSalesOrder(order))
		.collect(Collectors.toList());
	}
	
	private static SalesModel extractSalesOrder(Cimm2BOpenOrderInfo order) {
		SalesModel salesOrder = new SalesModel();
		salesOrder.setOrderNum(order.getOrderERPId());
		salesOrder.setOrderDate(new SimpleDateFormat("MM/dd/yyyy").format(order.getOrderDate()));
		salesOrder.setOrderStatus(order.getOrderStatus());
		return salesOrder;
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
	/*public static ArrayList<SalesModel> OrderHistory(SalesModel salesInputParameter) {
		ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
		//orderList = SalesDAO.getOrdersHistory(salesInputParameter);
		try{
			processOrdersRequest(salesInputParameter);
		}catch (Exception e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return orderList;
		}*/
	
	public static ArrayList<SalesModel> OrderHistory(SalesModel salesInputParameter) {
		ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
		try{
	
			Cimm2BCentralSalesOrderHistoryRequest salesOrderHistoryRequest = new Cimm2BCentralSalesOrderHistoryRequest();
			salesOrderHistoryRequest.setCustomerERPId(salesInputParameter.getCustomerNumber());
			salesOrderHistoryRequest.setFromDate(salesInputParameter.getStartDate());
			salesOrderHistoryRequest.setToDate(salesInputParameter.getEndDate());		
	
			HttpSession session = salesInputParameter.getSession();
			SecureData password=new SecureData();
			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
					.addQueryParameter("filter.orderStatus", salesInputParameter.getStatus()!=null?salesInputParameter.getStatus():"SALES")
					.addQueryParameter("filter.beginDate", salesInputParameter.getStartDate())
					.addQueryParameter("filter.endDate", salesInputParameter.getEndDate())
			     	.build();
			Cimm2BCResponse<Cimm2BPagedResponse<List<Cimm2BOpenOrderInfo>>> response = CimmESBServiceUtils.getOrderService().getCurrentCustomerOrders(request);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("ESB ORDER HISTORY Request:"+mapper.writeValueAsString(request));
			System.out.println("ESB ORDER HISTORY Response:"+mapper.writeValueAsString(response.getData()));
			List<Cimm2BOpenOrderInfo> cimm2BCentralOpenOrderInformationDetails = response.getData().getDetails();
			
	
			List<Cimm2BCentralSalesOrderHistoryDetails> salesOrderHistoryDetails = null;
			if(cimm2BCentralOpenOrderInformationDetails!=null){
					
				
						for(Cimm2BOpenOrderInfo orderHistory : cimm2BCentralOpenOrderInformationDetails){
							SalesModel salesModel = new SalesModel();
							salesModel.setOrderNum(orderHistory.getOrderNumber());
							/*if(orderHistory.getGenerationId() !=null) {
								salesModel.setGeneId(orderHistory.getGenerationId());
							}else {*/
								salesModel.setGeneId(orderHistory.getBackOrderSequence());
							/*}*/
							if(orderHistory.getOrderDate()!=null){
							salesModel.setOrderDate(CommonUtility.convertToStringFromDate(orderHistory.getOrderDate()));
							}
							/*salesModel.setManufacturer(orderHistory.getSupplier());
							salesModel.setPartNumber(orderHistory.getPart());*/
							if(orderHistory.getShipDate()!=null)
							salesModel.setShipDate(CommonUtility.convertToStringFromDate(orderHistory.getShipDate()));
							/*salesModel.setDescription1(orderHistory.getDescription1());
							salesModel.setDescription2(orderHistory.getDescription2());
							salesModel.setDescription(orderHistory.getLocation()); // ---- For location field
*/							salesModel.setTotal(orderHistory.getOrderTotal()!=null?orderHistory.getOrderTotal():0);
							salesModel.setPoNumber(orderHistory.getPoNumber());
							salesModel.setInvoiceNumber(orderHistory.getInvoiceNumber()!=null?orderHistory.getInvoiceNumber():"");
							salesModel.setInvoiceAmount(orderHistory.getInvoiceAmount()!=null?orderHistory.getInvoiceAmount():0);
							if(orderHistory.getInvoiceDate()!=null){
							salesModel.setInvoiceDate(CommonUtility.convertToStringFromDate(orderHistory.getInvoiceDate()));
							}
							salesModel.setShipToName(orderHistory.getShipToName());
							salesModel.setCustomerName(orderHistory.getCustomerName());
							//salesModel.setCustomerERPId(orderHistory.getCustomerERPId());
							salesModel.setOrderStatus(orderHistory.getOrderStatus());
							salesModel.setOrderedBy(orderHistory.getOrderedBy());
							if(orderHistory.getDueBalance()!=null){
							salesModel.setNetDue(orderHistory.getDueBalance());
							}
							if(CommonUtility.validateString(orderHistory.getShipToId()).length() > 0)
							{
								salesModel.setShipToId(orderHistory.getShipToId());	
							}
						/*	salesModel.setAccountId(orderHistory.getAccountId());*/
							/*salesModel.setOrderStatusType(orderHistory.getOrderTypeCode());*/
							salesModel.setOrderSuffix(CommonUtility.validateNumber(orderHistory.getOrderSuffix()));
							if(orderHistory.getShipVia()!=null && orderHistory.getShipVia().size()>0 ) {
							List<CimmShippingMethod> shipMethod = orderHistory.getShipVia();
								for (CimmShippingMethod historyShipMethod : shipMethod) {
									salesModel.setShipMethod(historyShipMethod.getShipViaDescription());
									salesModel.setShipMethodId(historyShipMethod.getShipViaCode());
									salesModel.setCarrierTrackingNumber(historyShipMethod.getCarrierTrackingNumber());
									salesModel.setShipViaErpId(historyShipMethod.getShipViaErpId());
									salesModel.setAccountNumber(historyShipMethod.getAccountNumber());
									salesModel.setReturnUrl(historyShipMethod.getCarrierTrackingURL());
								}
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

	/*public static LinkedHashMap<String, ArrayList<SalesModel>> OrderDetail(SalesModel salesInputParameter) {
		LinkedHashMap<String, ArrayList<SalesModel>> orderDetailInfo = new LinkedHashMap<String, ArrayList<SalesModel>>();
		try{
			Cimm2BOrder orderDetails = processGetOrder(salesInputParameter);
			if(orderDetails != null) {
				orderDetailInfo.put("OrderItemList", extractOrderedItems(orderDetails));
				SalesModel salesOrder = extractOrderDetails(orderDetails);
				ArrayList<SalesModel> tempList = new ArrayList<>();
				tempList.add(salesOrder);
				orderDetailInfo.put("OrderDetail", tempList);
			}
		}catch (Exception e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return orderDetailInfo;
	}*/
	
	public static LinkedHashMap<String, ArrayList<SalesModel>> OrderDetail(SalesModel salesInputParameter) {
		LinkedHashMap<String, ArrayList<SalesModel>> orderDetailInfo = new LinkedHashMap<String, ArrayList<SalesModel>>();
		try{
			HttpSession session = salesInputParameter.getSession();
			int defaultBillToId = 0;
			int defaultShipToId = 0;
	
			ArrayList<SalesModel> orderDetail = new ArrayList<SalesModel>();
			SalesModel orderDetailModel = new SalesModel();

			SecureData password=new SecureData();
			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
					.addQueryParameter("orderStatus", CommonUtility.validateString(salesInputParameter.getOrderStatus()).length()>0?salesInputParameter.getOrderStatus():"")
					.addQueryParameter("filter.invoiceNumber", CommonUtility.validateString(salesInputParameter.getInvoiceNumber()))
					.addQueryParameter("filter.orderSuffix", (CommonUtility.validateParseIntegerToString((salesInputParameter.getOrderSuffix()))))
					.build();
				
			Cimm2BCResponse<Cimm2BOrder> response = CimmESBServiceUtils.getOrderDetailService().getCurrentCustomerOrder(request, salesInputParameter.getOrderID());
			DozerBeanMapper  dozzer = new DozerBeanMapper();
			ObjectMapper mapper = new ObjectMapper();
		/*	System.out.println("CIMM ESB Tax Request:"+mapper.writeValueAsString(cimmOrderRequest));*/
			System.out.println("CIMM ESB order details Response:"+mapper.writeValueAsString(response.getData()));
			Cimm2BCentralOrder order = new Cimm2BCentralOrder();
			dozzer.map(response.getData(),order); // Dozzer Mapper
			
			ArrayList<SalesModel>orderItemList = null;
			if(order != null){
				//order.setStatus(response.getStatus().toString());
				orderDetailModel.setOrderType(order.getOrderType());
				orderDetailModel.setOrderNum(order.getOrderNumber());
				orderDetailModel.setCustomerNumber(order.getCustomerERPId()!=null?order.getCustomerERPId():"");
				orderDetailModel.setErpOrderNumber(order.getOrderERPId()!=null?order.getOrderERPId():"");
				if(response.getData().getRequiredDate()!=null){
					orderDetailModel.setReqDate(CommonUtility.convertToStringFromDate(response.getData().getRequiredDate()));
				}
				if(response.getData().getOrderStatus().getOrderStatus()!=null){
					
					orderDetailModel.setOrderStatus(response.getData().getOrderStatus().getOrderStatus());
				}
				
				orderDetailModel.setPoNumber(order.getCustomerPoNumber());
				orderDetailModel.setOrderedBy(order.getOrderedBy());
				if(response.getData().getShipDate()!=null){
				orderDetailModel.setShipDate(CommonUtility.convertToStringFromDate(response.getData().getShipDate()));
				}
				if(response.getData().getOrderDate()!=null){
				orderDetailModel.setOrderDate(CommonUtility.convertToStringFromDate(response.getData().getOrderDate()));}
				orderDetailModel.setOrderSource(order.getOrderSource());
				orderDetailModel.setOrderSuffix(order.getOrderSuffix());
				
				if(order.getBillToContact() != null) {
					orderDetailModel.setPhone(order.getBillToContact().getPrimaryPhoneNumber());
				}
				if(order.getShipToContact() != null) {
					orderDetailModel.setPhone(order.getShipToContact().getPrimaryPhoneNumber());
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
	
				ArrayList<Cimm2BShippingMethod> shipMethod = (ArrayList<Cimm2BShippingMethod>) response.getData().getShipVia();
				if(shipMethod != null && shipMethod.size()>0){
					orderDetailModel.setCimm2BCentralShipVia(Cimm2BCentralClient.getInstance().cimm2BcentralShipMethodToShipVia(shipMethod));
					for (Cimm2BShippingMethod orderResponseGetShipMethod : shipMethod) {
						orderDetailModel.setShipMethod(orderResponseGetShipMethod.getShipViaDescription());
						orderDetailModel.setShipMethodId(orderResponseGetShipMethod.getShipViaCode());
						orderDetailModel.setCarrierTrackingNumber(orderResponseGetShipMethod.getCarrierTrackingNumber());
						orderDetailModel.setShipViaErpId(orderResponseGetShipMethod.getShipViaErpId());
						orderDetailModel.setAccountNumber(orderResponseGetShipMethod.getAccountNumber());
						orderDetailModel.setShipViaMethod(orderResponseGetShipMethod.getShippingBranchCode());
	
					}
				}
				orderDetailModel.setPoNumber(order.getCustomerPoNumber());
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
				orderDetailModel.setInvoiceAmount(order.getInvoiceAmount()!=null?order.getInvoiceAmount():0);
				orderDetailModel.setInvoiceNumber(order.getInvoiceNumber());
				if(CommonUtility.validateString(order.getOrderTime()).length() > 0) {
				orderDetailModel.setOrderTime(order.getOrderTime());
				}
				if(CommonUtility.validateString(order.getReleaseNumber()).length() > 0) {
					orderDetailModel.setCustomerReleaseNumber(order.getReleaseNumber());
				}
				double orderTotal = order.getOrderTotal()!=null?order.getOrderTotal():0;
				orderDetailModel.setTotal(orderTotal);
	
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
						model.setRushFlag(itemModel.getRushFlag());
						
						model.setQtyordered(itemModel.getQty());
						if(itemModel.getQtyShipped()!=null){
						model.setQtyShipped(itemModel.getQtyShipped().intValue());
						}
						model.setShortDesc(itemModel.getShortDescription());
						model.setUnitPrice(itemModel.getUnitPrice() != null ? itemModel.getUnitPrice() : 0);
						model.setListPrice(itemModel.getListPrice());
						model.setUom(itemModel.getUom());
						model.setTrackingInfo(CommonUtility.validateString(itemModel.getOrderGenerationSequence()));
						model.setBackOrderQty(itemModel.getBackOrderQty());
						
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
	
	private static SalesModel extractOrderDetails(Cimm2BOrder orderDetails) {
		SalesModel salesOrderDetails = new SalesModel();
		
		Cimm2BAddress billingAddress = orderDetails.getBillingAddress();
		AddressModel billAddress= new AddressModel();
		billAddress.setAddress1(billingAddress.getAddressLine1());
		billAddress.setAddress2(billingAddress.getAddressLine2());
		billAddress.setCity(billingAddress.getCity());
		billAddress.setState(billingAddress.getState());
		billAddress.setCountry(billingAddress.getCountry());
		billAddress.setZipCode(billingAddress.getZipCode());
		
		Cimm2BAddress shippingAddress = orderDetails.getShippingAddress();
		AddressModel shipAddress= new AddressModel();
		shipAddress.setAddress1(shippingAddress.getAddressLine1());
		shipAddress.setAddress2(shippingAddress.getAddressLine2());
		shipAddress.setCity(shippingAddress.getCity());
		shipAddress.setState(billingAddress.getState());
		shipAddress.setCountry(shippingAddress.getCountry());
		shipAddress.setZipCode(shippingAddress.getZipCode());
		
		salesOrderDetails.setOrderNum(orderDetails.getOrderERPId());
		salesOrderDetails.setBillAddress(billAddress);
		salesOrderDetails.setShipAddress(shipAddress);
		if(orderDetails.getRequiredDate() != null) {
			salesOrderDetails.setReqDate(new SimpleDateFormat().format(orderDetails.getRequiredDate()));
		}
		
		salesOrderDetails.setTax(orderDetails.getTaxAmount());
		
		salesOrderDetails.setOrderStatus(orderDetails.getOrderStatus().getOrderStatus());
		
		return salesOrderDetails;
	}
	
	private static ArrayList<SalesModel> extractOrderedItems(Cimm2BOrder orderDetails){
		return orderDetails.getLineItems().stream()
				.map(i -> extractOrderedItem(i))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	private static SalesModel extractOrderedItem(Cimm2BLineItem item) {
		SalesModel orderedItem = new SalesModel();
		orderedItem.setPartNumber(item.getPartNumber());
		orderedItem.setQuantityOrdered(item.getQty());
		orderedItem.setUom(item.getUom());
		orderedItem.setListPrice(item.getListPrice() != null ? item.getListPrice() : 0);
		return orderedItem;
	}
	@SuppressWarnings("unchecked")
	public static SalesModel createQuote(LinkedHashMap<String, Object> createQuoteInput)
	{
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
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
			String calculateTax = (String) createQuoteInput.get("calculateTax");
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
					lineItem.setQty(itemModel.getQty()); 
					lineItem.setUom(itemModel.getUom());
					lineItem.setManufacturer(itemModel.getManufacturerName());
					lineItem.setUnitPrice(itemModel.getUnitPrice());
					lineItem.setExtendedPrice(itemModel.getUnitPrice() * itemModel.getQty());
					lineItem.setCustomerPartNumber(itemModel.getCustomerPartNumber());
					lineItem.setLineItemComment(itemModel.getLineItemComment());
					lineItem.setShippingBranch(warehouseCode);	
					//Code written for Turtel & Hughes by Nitish K
					
							lineItem.setExtendedPrice(itemModel.getUnitPrice() * itemModel.getQty());
						
					lineItems.add(lineItem);
					orderTotal = orderTotal + lineItem.getExtendedPrice();
				}
				Cimm2BCentralOrder order = new Cimm2BCentralOrder();
				ArrayList<Cimm2BCentralShipVia> shipviaArray = new ArrayList<Cimm2BCentralShipVia>();
				if(CommonUtility.validateString(shipVia).length() > 0){
					Cimm2BCentralShipVia cimm2bCentralShipVia = new Cimm2BCentralShipVia();
					cimm2bCentralShipVia.setShipViaCode(shipVia);
					cimm2bCentralShipVia.setShipViaDescription(shipViaDisplay);					
					shipviaArray.add(cimm2bCentralShipVia);
					order.setShipVia(shipviaArray);
				}
				CimmContactRequest cimmContactRequest=new CimmContactRequest();
				CimmContactRequest cimmShipToContactRequest=new CimmContactRequest();
				if(session!=null && CommonUtility.validateString((String)session.getAttribute("loginCustomerName")).length()>0) {
				cimmContactRequest.setCompanyName(CommonUtility.validateString((String)session.getAttribute("loginCustomerName")));
				cimmShipToContactRequest.setFirstName(CommonUtility.validateString((String)session.getAttribute("loginCustomerName")));
				}							
				CimmOrderRequest cimmOrderRequest=new CimmOrderRequest();
				Calendar cal = Calendar.getInstance();
			    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			    String orderDate = sdf.format(cal.getTime());
				cimmOrderRequest.setRequestType((String) createQuoteInput.get("orderType"));
				
				try {
					cimmOrderRequest.setRequiredDate(CommonUtility.convertToDateFromString(reqDate)!=null?CommonUtility.convertToDateFromString(reqDate):null);
					cimmOrderRequest.setOrderDate(CommonUtility.convertToDateFromString(orderDate)!=null?CommonUtility.convertToDateFromString(orderDate):null);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				cimmOrderRequest.setShipToContact(cimmShipToContactRequest!=null?cimmShipToContactRequest:null);
				cimmOrderRequest.setDueDate(null);
				cimmOrderRequest.setCustomerPoNumber(poNumber);
				cimmOrderRequest.setShippingInstruction(shippingInstruction);				
				cimmOrderRequest.setBillToContact(cimmContactRequest);
				cimmOrderRequest.setBillingAddress(extractCimmAddress(Cimm2BCentralClient.getInstance().userModelToAddressModel(billAddress)));
				cimmOrderRequest.setShippingAddress(extractCimmAddress(Cimm2BCentralClient.getInstance().userModelToAddressModel(shipAddress)));
				cimmOrderRequest.setShipVia(extractCimmShip(shipviaArray));			
				cimmOrderRequest.setLineItems(extractLineItems((List<ProductsModel>) createQuoteInput.get("itemList")));
				cimmOrderRequest.setFreightAmount(freightCharges>0 ? freightCharges:0.0);
				SecureData password=new SecureData();
				RestRequest<CimmOrderRequest> request=null;
				if(calculateTax!=null){
				if(calculateTax.equalsIgnoreCase("N")) {
					 request = RestRequest.<CimmOrderRequest>builder()
							.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
							.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
							.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
							.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
							.withBody(cimmOrderRequest)
							.addQueryParameter("ordermode", "quote")
							.build();
				}
				}
				else {
					request = RestRequest.<CimmOrderRequest>builder()
							.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
							.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
							.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
							.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
							.withBody(cimmOrderRequest).addQueryParameter("ordermode", "shell")
							.build();
				}
				
				Cimm2BCResponse<Cimm2BOrder> response = CimmESBServiceUtils.getCreateOrderService().createCustomerOrder(request);
				ObjectMapper mapper = new ObjectMapper();
				System.out.println("CIMM ESB Tax Request:"+mapper.writeValueAsString(cimmOrderRequest));
				System.out.println("CIMM ESB Tax Response:"+mapper.writeValueAsString(response));
				DozerBeanMapper  dozzer = new DozerBeanMapper();				
				Cimm2BCentralOrder orderResponse = new Cimm2BCentralOrder();
				if(response.getData() != null) {
					dozzer.map(response.getData(),orderResponse); // Dozzer Mapp
					if(orderResponse != null && ((CommonUtility.validateString(orderResponse.getOrderNumber()).length()> 0) || (CommonUtility.validateString(orderResponse.getCartId()).length()>0)) || (CommonUtility.validateString(orderResponse.getOrderERPId()).length()>0)){
						quoteInfo.setCustomerERPId(orderResponse.getCustomerERPId()!=null?orderResponse.getCustomerERPId():"");
						quoteInfo.setReqDate(orderResponse.getRequiredDate()!=null?orderResponse.getRequiredDate():"");
						quoteInfo.setOrderDate(orderResponse.getOrderDate()!=null?orderResponse.getOrderDate():"");
						if(orderResponse.getShipToContact() != null) {
							quoteInfo.setShipToName(orderResponse.getShipToContact().getFirstName()!=null?orderResponse.getShipToContact().getFirstName():"");
						}
						AddressModel billingAddress = new AddressModel();
						if(orderResponse.getBillingAddress() != null) {
							billingAddress.setCompanyName(orderResponse.getBillingAddress().getCompanyName());
							billingAddress.setAddress1(orderResponse.getBillingAddress().getAddressLine1());
							billingAddress.setAddress2(orderResponse.getBillingAddress().getAddressLine2());
							billingAddress.setCity(orderResponse.getBillingAddress().getCity());
							billingAddress.setZipCode(orderResponse.getBillingAddress().getZipCode());
							billingAddress.setCountry(orderResponse.getBillingAddress().getCountry());
							quoteInfo.setBillAddress(billingAddress);
						}
						AddressModel shippingAddress = new AddressModel();
						if(orderResponse.getShippingAddress() != null) {
							shippingAddress.setCompanyName(orderResponse.getShippingAddress().getCompanyName());
							shippingAddress.setAddress1(orderResponse.getShippingAddress().getAddressLine1());
							shippingAddress.setAddress2(orderResponse.getShippingAddress().getAddressLine2());
							shippingAddress.setCity(orderResponse.getShippingAddress().getCity());
							shippingAddress.setZipCode(orderResponse.getShippingAddress().getZipCode());
							shippingAddress.setCountry(orderResponse.getShippingAddress().getCountry());
							shippingAddress.setCountryCode(orderResponse.getShippingAddress().getCountryCode());							
							quoteInfo.setShipAddress(shippingAddress);
						}
						quoteInfo.setExternalCartId(orderResponse.getCartId());
						quoteInfo.setOrderNum(orderResponse.getOrderNumber());
						quoteInfo.setErpOrderNumber(orderResponse.getOrderERPId());
						quoteInfo.setQuoteNumber(orderResponse.getOrderNumber());
						quoteInfo.setTax(orderResponse.getTaxAmount()!=null?orderResponse.getTaxAmount():0.0);
						if(orderResponse.getFreight()!=null){
							quoteInfo.setFreight(orderResponse.getFreight());
						}
						if(orderResponse.getOrderTotal()!=null && orderResponse.getOrderTotal()>0){
							quoteInfo.setTotal(orderResponse.getOrderTotal() + freightCharges);
						}
						quoteInfo.setSubtotal(orderResponse.getOrderSubTotal()!=null?orderResponse.getOrderSubTotal():0.0);
						ArrayList<Cimm2BCentralLineItem> cimm2bCentralLineItem = orderResponse.getLineItems();
						if(cimm2bCentralLineItem != null && cimm2bCentralLineItem.size() > 0){
							double handlingCharges = 0.0;
							double deliveryCharges = 0.0;
							ArrayList<ProductsModel> nonCatalogItems = new ArrayList<ProductsModel>();
							ArrayList<SalesModel> lineItemsFromResponse = new ArrayList<SalesModel>();
							HashMap<String, SalesModel> lineItemsFromResponseCheckNonCatalogItems = new HashMap<String, SalesModel>();
							for (int i = 0; i < cimm2bCentralLineItem.size(); i++) {
								SalesModel model = new SalesModel();
								model.setLineNumber(cimm2bCentralLineItem.get(i).getLineNumber()!=null?cimm2bCentralLineItem.get(i).getLineNumber():0);
								model.setLineItemComment(cimm2bCentralLineItem.get(i).getLineItemComment()!=null?cimm2bCentralLineItem.get(i).getLineItemComment():"");
								model.setItemId(CommonUtility.validateNumber(cimm2bCentralLineItem.get(i).getItemId()!=null?cimm2bCentralLineItem.get(i).getItemId():""));
								model.setPartNumber(cimm2bCentralLineItem.get(i).getPartNumber());
								model.setCustomerPartNumber(cimm2bCentralLineItem.get(i).getCustomerPartNumber()!=null?cimm2bCentralLineItem.get(i).getCustomerPartNumber():"");
								model.setShortDesc(cimm2bCentralLineItem.get(i).getShortDescription()!=null?cimm2bCentralLineItem.get(i).getShortDescription():"");
								model.setUnitPrice(cimm2bCentralLineItem.get(i).getUnitPrice());
								model.setExtPrice(cimm2bCentralLineItem.get(i).getExtendedPrice());
								model.setQtyordered(cimm2bCentralLineItem.get(i).getQty());
								model.setQtyShipped(cimm2bCentralLineItem.get(i).getQtyShipped()!=null?cimm2bCentralLineItem.get(i).getQtyShipped().intValue():0);
								model.setUom(cimm2bCentralLineItem.get(i).getUom());
								model.setQtyUom(CommonUtility.validateString(cimm2bCentralLineItem.get(i).getUomQty()!=null?cimm2bCentralLineItem.get(i).getUomQty():"0"));
								model.setManufacturer(cimm2bCentralLineItem.get(i).getManufacturer()!=null?cimm2bCentralLineItem.get(i).getManufacturer():"");
								model.setManufacturerPartNumber(cimm2bCentralLineItem.get(i).getManufacturer()!=null?cimm2bCentralLineItem.get(i).getManufacturer():"");
								model.setTaxable(cimm2bCentralLineItem.get(i).getCalculateTax()!=null?cimm2bCentralLineItem.get(i).getCalculateTax():false);

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
						quoteInfo.setOrderStatus(orderResponse.getOrderStatus()!=null?orderResponse.getOrderStatus():"");
						
						if(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_CARDS") != null) {
							String GET_CUSTOMER_CARDS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_CARDS")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  customerErpId;
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
						/*if(orderResponse.getOrderTotal()>0){
							quoteInfo.setTotal(orderResponse.getOrderTotal() + freightCharges);
						}*/
						if(orderResponse.getOrderSubTotal()!=null){
						quoteInfo.setSubtotal(orderResponse.getOrderSubTotal());
						}
					
				}/*else{
					//quoteInfo.setStatusDescription(orderResponseEntity.getStatus().getCode() + " - " + orderResponseEntity.getStatus().getMessage());
					quoteInfo.setStatusDescription(errorMessageToDisplay);
				}*/
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return quoteInfo;
	}
	public ArrayList<SalesModel> reorderPadInquiry(SalesModel salesInputParameter) {
		ArrayList<SalesModel> ReorderPadItemList = null;
		try{
			ReorderPadItemList = new ArrayList<SalesModel>();
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return ReorderPadItemList;
	}

	public LinkedHashMap<String, Object> getTaxFromERP(LinkedHashMap<String, Object> salesInputParameter) {
		LinkedHashMap<String, Object> getTaxDetails = null;
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
	
		try{
			
			com.unilog.cimmesb.client.ecomm.request.CimmV2TaxRequest taxRequest = AvalaraUtility.getTaxModelEsb(salesInputParameter);
			SecureData password=new SecureData();
			RestRequest<com.unilog.cimmesb.client.ecomm.request.CimmV2TaxRequest> request = RestRequest.<com.unilog.cimmesb.client.ecomm.request.CimmV2TaxRequest>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
					.withBody(taxRequest)
					.build();
			
			//response = AvalaraUtility.getInstance().getTax(cartListData, overrideShipAddress, wareHouse, poNumber);

			ObjectMapper mapper = new ObjectMapper();
			System.out.println("CIMM ESB Avalara Tax  Request:"+mapper.writeValueAsString(taxRequest));
				
			Cimm2BCResponse<CimmV2TaxResponse> response = CimmESBServiceUtils.getTaxService().getAvalaraTaxCloud(request); // Avalara Tax
			System.out.println("CIMM ESB Avalara Tax Response:"+mapper.writeValueAsString(response));
			if(response!=null && response.getData() != null &&  response.getStatus().getCode() == 200 ){
				CimmV2TaxResponse taxResponse = (CimmV2TaxResponse) response.getData();
				getTaxDetails=new LinkedHashMap<>();
				SalesModel salesModel = new SalesModel();
				salesModel.setTax(taxResponse.getTotalTax());
				salesModel.setTotal(taxResponse.getTotalAmount());
				getTaxDetails.put("TaxInfo", salesModel);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return getTaxDetails;
	}
	
	public static ArrayList<SalesModel> OpenOrderInfoBydates(SalesModel salesInputParameter) {
		ArrayList<SalesModel> openOrderList = new ArrayList<SalesModel>();
		try{
			List<Cimm2BOpenOrderInfo> openOrders =	processOrdersRequest(salesInputParameter);
			for(Cimm2BOpenOrderInfo openOrder : openOrders){
				SalesModel salesModel = new SalesModel();
				salesModel.setCustomerERPId(openOrder.getCustomerERPId()!=null?openOrder.getCustomerERPId():null);
				salesModel.setOrderNum(openOrder.getOrderERPId());
				salesModel.setOrderDate(CommonUtility.convertToStringFromDate(openOrder.getOrderDate()));
				salesModel.setTotal(openOrder.getOrderTotal()!=null?openOrder.getOrderTotal():0);
				salesModel.setPoNumber(openOrder.getPoNumber());
				salesModel.setInvoiceNumber(openOrder.getInvoiceNumber());
				salesModel.setShipToName(openOrder.getShipToName());
				salesModel.setOpenOrderAmount(openOrder.getOrderTotal()!=null?openOrder.getOrderTotal():0);
				salesModel.setOrderedBy(openOrder.getOrderedBy());
				salesModel.setOrderStatus(openOrder.getOrderStatus());
				if(CommonUtility.validateString(openOrder.getShipToId()).length() > 0)
				{
					salesModel.setShipToId(openOrder.getShipToId());	
				}
				if(openOrder.getRequiredDate()!=null){
					salesModel.setReqDate(CommonUtility.convertToStringFromDate(openOrder.getRequiredDate()));
				}		
				if(openOrder.getShipDate()!=null){
				salesModel.setShipDate(CommonUtility.convertToStringFromDate(openOrder.getShipDate()));
				}
				/*salesModel.setOrderSuffix(CommonUtility.validateNumber(openOrder.getOrderSuffix()));*/
				openOrderList.add(salesModel);
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
			HttpSession session = salesInputParameter.getSession();
			SecureData password=new SecureData();
			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
					.build();
			Cimm2BCResponse<List<CimmInvoiceResponse>> response = CimmESBServiceUtils.getOrderService().getCustomerOrderOpenInvoice(request);
			DozerBeanMapper  dozzer = new DozerBeanMapper();
			List<CimmInvoiceResponse> cimm2BCentralInvoiceList = response.getData();
			
	
			if(salesInputParameter.getOrderNum()!=null && salesInputParameter.getOrderNum().trim().length()>0){
				orderNo = CommonUtility.validateNumber(salesInputParameter.getOrderNum());
			}
				if(cimm2BCentralInvoiceList != null && cimm2BCentralInvoiceList.size() > 0){
					for(CimmInvoiceResponse invoiceList : cimm2BCentralInvoiceList){
						SalesModel salesModel = new SalesModel();
						salesModel.setInvoiceNumber(invoiceList.getInvoiceNumber());
						salesModel.setOrderNum(invoiceList.getOrderNumber());
						
				if(invoiceList.getTotalAmount()!=null){
						salesModel.setTotal(invoiceList.getTotalAmount());
					}
				if(invoiceList.getTax()!=null){
				salesModel.setTax(invoiceList.getTax());
				}
					
						salesModel.setPoNumber(invoiceList.getPoNumber());
						/*salesModel.setFreight(invoiceList.getFreightAmount());*/
						//salesModel.setDocumentLinks(invoiceList.getDocumentLinks());
						//salesModel.setLineItemComment(invoiceList.getLineItems());
						/*salesModel.setInvoiceDate(invoiceList.getOrderDate());*/
						//salesModel.setOrderDate(invoiceList.getOrderedDate());
						//salesModel.setDueDate(invoiceList.getDueDate());
						orderList.add(salesModel);
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
					salesModel.setOrderNum(arBalance.getOrderNumber());
					salesModel.setTax(arBalance.getTax());
					salesModel.setTotal(arBalance.getTotalAmount());
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
			String roundTotalStr=CommonUtility.validateParseDoubleToString(salesInputParameter.getTotal());
			salesInputParameter.setTotal(CommonUtility.roundHalfUp(roundTotalStr));
		/*	PayPalRequest cimm2BCPaypalRequest = new PayPalRequest();*/
			PaymentGatewayRequest gatewayRequest=new  PaymentGatewayRequest();
			Transaction transaction=new Transaction();
			transaction.setAmount(salesInputParameter.getTotal());
			transaction.setTransactionType(currency);
			gatewayRequest.setTransaction(transaction);
		
			HttpServletRequest httpRequest=ServletActionContext.getRequest();
			
			
			HttpSession session = httpRequest.getSession();
			SecureData password=new SecureData();
			RestRequest<PaymentGatewayRequest> request = RestRequest.<PaymentGatewayRequest>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
					.withBody(gatewayRequest)
			     	.build();
			Cimm2BCResponse<PaymentGatewayResponse> response = CimmESBServiceUtils.getPaymentGatewayV1Service().getPayPalSecureToken(request);
			
		
			
			PaymentGatewayResponse cimm2BCPaypalResponse = null;
			
			
				cimm2BCPaypalResponse = (PaymentGatewayResponse) response.getData();
				if(cimm2BCPaypalResponse != null){
					salesOutputParameter =  new SalesModel();
					salesOutputParameter.setUserToken(cimm2BCPaypalResponse.getSecureToken());
					salesOutputParameter.setTakenBy(cimm2BCPaypalResponse.getSecureTokenId());
				
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
	   
	   cimm2BCentralAuthorizeSaveCreditCardRequest.setDataDescriptor(CommonUtility.validateString(salesInputParameter.getDataDescriptor()));
	   cimm2BCentralAuthorizeSaveCreditCardRequest.setDataValue(CommonUtility.validateString(salesInputParameter.getDataValue()));
	   cimm2BCentralAuthorizeSaveCreditCardRequest.setCustomerERPId(customerErpId);
	   cimm2BCentralAuthorizeSaveCreditCardRequest.setCardHolderFirstName(salesInputParameter.getName());

		AddressModel billAddress = salesInputParameter.getBillAddress();
		Cimm2BCentralAddress billingAddress = Cimm2BCentralClient.getInstance().ecomAddressModelToCimm2BCentralAddress(billAddress);
		cimm2BCentralAuthorizeSaveCreditCardRequest.setAddress(billingAddress);
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
			logger.info("---- Inside error code response  ----");
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
				lineItem.setUnitPrice(product.getUnitPrice());
				lineItem.setListPrice(product.getPrice());
				lineItem.setCost(product.getPrice());
				lineItem.setLineItemComment(product.getLineItemComment());
				lineItem.setShippingBranch(wareHouseCode);	
				lineItem.setCalculateTax(true);
				lineItem.setDiscount(0);
				lineItems.add(lineItem);
			}
			
			order.setCustomerERPId(customerErpId);
			order.setBranchCode(wareHouseCode);
			order.setShippingAddress(cimm2BCentralShipingAddress);
			order.setLineItems(lineItems);
			order.setOtherCharges(otherDetails.get("otherCharges") != null ? Double.parseDouble(otherDetails.get("otherCharges").toString()) : 0);
			order.setFreight(otherDetails.get("otherCharges") != null ? Double.parseDouble(otherDetails.get("otherCharges").toString()) : 0);
			String CREATE_SHELL_ORDER = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_SHELL_ORDER_API"));			
			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_SHELL_ORDER, HttpMethod.POST, order, Cimm2BCentralOrder.class);
			
			if(orderResponseEntity!=null && orderResponseEntity.getData() != null){
				Cimm2BCentralOrder orderResponse = (Cimm2BCentralOrder) orderResponseEntity.getData();
				if(orderResponse != null) {
					shellOrder.setTax(orderResponse.getTaxAmount());
					shellOrder.setTotal(orderResponse.getOrderTotal());
					shellOrder.setSubtotal(orderResponse.getOrderSubTotal());
					shellOrder.setExternalCartId(orderResponse.getCartId());
					shellOrder.setJurisdictionCode(orderResponse.getJurisdictionCode());
					shellOrder.setTaxable(orderResponse.isTaxable());
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
	private static List processOrdersRequest(SalesModel salesInputParameter) throws JsonProcessingException {
		HttpSession session = salesInputParameter.getSession();
		SecureData password=new SecureData();
		RestRequest<Void> request = RestRequest.<Void>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
				.addQueryParameter("filter.orderStatus", salesInputParameter.getStatus()!=null?salesInputParameter.getStatus():"OPEN")
				.addQueryParameter("filter.beginDate", salesInputParameter.getStartDate())
				.addQueryParameter("filter.endDate", salesInputParameter.getEndDate())
				.addQueryParameter("dateFormat", "MM/dd/yyyy")
				.build();
		Cimm2BCResponse<Cimm2BPagedResponse<List<Cimm2BOpenOrderInfo>>> response = CimmESBServiceUtils.getOrderService().getCurrentCustomerOrders(request);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("GET Open Orders REQUEST : "+mapper.writeValueAsString(request));
		System.out.println("GET Open Order RESPONSE : "+mapper.writeValueAsString(response));
		if(response != null && response.getStatus().getCode() == HttpStatus.SC_OK) {
			return response.getData().getDetails();
		}
		return Collections.emptyList();
	}
	
	private static Cimm2BOrder processGetOrder(SalesModel orderRequest) {
		SecureData password=new SecureData();
		HttpSession session = orderRequest.getSession();
		RestRequest<Void> request = RestRequest.<Void>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
				.build();
			
		Cimm2BCResponse<Cimm2BOrder> response = CimmESBServiceUtils.getOrderDetailService().getCurrentCustomerOrder(request, orderRequest.getOrderID());
		if(response != null && response.getStatus().getCode() == HttpStatus.SC_OK) {
			return response.getData();
		}
		return null;
	}
	
	private static Cimm2BOrder processCreateOrder(SalesOrderManagementModel salesOrderInput) {
		
		HttpSession session = salesOrderInput.getSession();
		
		CimmContactRequest cimmContactRequest=new CimmContactRequest();
		cimmContactRequest.setCompanyName(session.getAttribute("loginCustomerName").toString());
		
		CimmOrderRequest cimmOrderRequest=new CimmOrderRequest();
		cimmOrderRequest.setRequestType("webOrder");
		
		cimmOrderRequest.setBillToContact(cimmContactRequest);
		cimmOrderRequest.setBillingAddress(extractCimmAddress(salesOrderInput.getBillAddress()));
		cimmOrderRequest.setShippingAddress(extractCimmAddress(salesOrderInput.getShipAddress()));
		
		cimmOrderRequest.setLineItems(extractLineItems(salesOrderInput.getOrderItems()));
		SecureData password=new SecureData();
		RestRequest<CimmOrderRequest> request = RestRequest.<CimmOrderRequest>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
				.withBody(cimmOrderRequest)
				.build();
		
		
		Cimm2BCResponse<Cimm2BOrder> response = CimmESBServiceUtils.getCreateOrderService().createCustomerOrder(request);
		if(response != null && response.getStatus().getCode() == HttpStatus.SC_OK) {
			return response.getData();
		}
		return null;
	}
	private static List<CimmLineItem> extractLineItems(List<ProductsModel> items){
		AtomicInteger counter = new AtomicInteger(0);
		return items.stream().map(item -> {
			CimmLineItem lineItem = new CimmLineItem();
			lineItem.setBrandName(item.getBrandName());
			lineItem.setListPrice(item.getListPrice());
			lineItem.setPartNumber(item.getPartNumber());
			lineItem.setQty(Double.valueOf(item.getQty()));
			lineItem.setShortDescription(item.getShortDesc());
			if(CommonUtility.customServiceUtility()!=null) {
				lineItem.setRushFlag(CommonUtility.validateString(CommonUtility.customServiceUtility().setRushFlagToLineItems(lineItem, item)));
			}
			lineItem.setUom(item.getUom());
			lineItem.setUnitPrice(item.getUnitPrice());
			lineItem.setCartId(item.getCartId()); 
			lineItem.setExtendedPrice(item.getUnitPrice() * item.getQty());
			lineItem.setLineItemComment(item.getLineItemComment());
			lineItem.setLineNumber(counter.addAndGet(1));
			return lineItem;
		}).collect(Collectors.toList());
	}
	
	private static CimmAddressRequest extractCimmAddress(AddressModel address) {
		CimmAddressRequest cimmAddress = new CimmAddressRequest();
		cimmAddress.setCompanyName(address.getCompanyName());
		cimmAddress.setAddressLine1(address.getAddress1());
		cimmAddress.setCity(address.getCity());
		cimmAddress.setState(address.getState());
		cimmAddress.setCountry(address.getCountry());
		cimmAddress.setZipCode(address.getZipCode());
		cimmAddress.setAddressERPId(address.getShipToId()!=null?address.getShipToId():address.getAddressERPId());
		return cimmAddress;
	}
	private static List<CimmShippingMethod> extractCimmShip(List<Cimm2BCentralShipVia> shipVias) {
		return shipVias.stream().map(ship->{
		CimmShippingMethod cimmShipVia = new CimmShippingMethod();
		cimmShipVia.setShipViaCode(ship.getShipViaCode());
		cimmShipVia.setShipViaDescription(ship.getShipViaDescription());
		return cimmShipVia;
		}).collect(Collectors.toList());
	}
	
	private static CimmCCTransaction extractCustomerCard(Cimm2BCentralCustomerCard customerCard) {
		
		CimmCCTransaction ccDetails = new CimmCCTransaction();
		ccDetails.setPaymentAccountId(customerCard.getPaymentAccountId());
		ccDetails.setAuthorizationNumber(customerCard.getAuthorizationNumber());
		ccDetails.setTransactionId(customerCard.getTransactionId());
		ccDetails.setCreditCardNumber(customerCard.getCreditCardNumber());
		ccDetails.setCreditCardType(customerCard.getCreditCardType());
		ccDetails.setExpiryMonth(customerCard.getExpiryMonth());
		ccDetails.setExpiryYear(customerCard.getExpiryYear());
		ccDetails.setCvv(customerCard.getCvv());
		ccDetails.setCardHolderFirstName(customerCard.getCardHolderFirstName());
		ccDetails.setCardHolderLastName(customerCard.getCardHolderLastName());
		ccDetails.setAmount(customerCard.getAmount());
		ccDetails.setCurrencyCode(customerCard.getCurrencyCode());

		return ccDetails;
	
	}
	
	private static <T> RestRequest<T> buildRequest(T requestBody,Class<?> dataClass,HttpSession session){
		SecureData password=new SecureData();
		 RestRequest<T> request =RestRequest.<T>builder()
		.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
		.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
		.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
		.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
		.withBody(requestBody)
		.build();
		return request;
	}
	
		public static SalesModel createChargeLogicPayment(SalesModel salesInputParameter) {
		SalesModel salesOutputParameter =  new SalesModel();
		HttpSession session = salesInputParameter.getSession();
		Credential credential = new Credential();
		com.unilog.cimmesb.client.ecomm.request.Transaction transaction = new com.unilog.cimmesb.client.ecomm.request.Transaction();
		BillingAddress billingAddress = new BillingAddress();
		ShippingAddress shippingAddress = new ShippingAddress();
		HostedPayment hostedPayment = new HostedPayment();
		List<com.unilog.cimmesb.client.ecomm.request.LineItem> lineItemDetail = new ArrayList<com.unilog.cimmesb.client.ecomm.request.LineItem>();
		
		String storeNo = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_CREDENTIAL_STORE_NO"));
		String aPIKey = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_CREDENTIAL_API_KEY"));
		String applicationNo = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_CREDENTIAL_APPLICATION_NO"));
		String applicationVersion = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_CREDENTIAL_APPLICATION_VERSION"));
		String returnUrl = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_RESPONSE_PAGE"));
		String currencyCode = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_CURRENCY_CODE"));
		String paymentGateWayTransactionType = CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_TRANSACTION_TYPE");
		try {
			Date purchaseOrderDate=new SimpleDateFormat("MM/dd/yyyy").parse(salesInputParameter.getReqDate());
			
			credential.setStoreNo(storeNo);
			credential.setAPIKey(aPIKey);
			credential.setApplicationNo(applicationNo);
			credential.setApplicationVersion(applicationVersion);
			
			transaction.setCurrency(currencyCode);
			transaction.setAmount(salesInputParameter.getTotal());
			transaction.setFreightAmount(salesInputParameter.getFreight());
			transaction.setTaxAmount(salesInputParameter.getTax());
			transaction.setPurchaseOrderNumber("");
			transaction.setPurchaseOrderDate(purchaseOrderDate);
			transaction.setExternalReferenceNumber(salesInputParameter.getPoNumber());
			transaction.setConfirmationID("");
			transaction.setTransactionType(paymentGateWayTransactionType);
									
			for(ProductsModel cartItem : salesInputParameter.getCartData()){
				String description=null;
				com.unilog.cimmesb.client.ecomm.request.LineItem lineItem = new com.unilog.cimmesb.client.ecomm.request.LineItem();
				if(cartItem.getShortDesc().length()>47) {
					description=cartItem.getShortDesc().substring(0, 47)+"...";
				}
				else {
					description=cartItem.getShortDesc();
				}
				lineItem.setProductCode(cartItem.getPartNumber());
				lineItem.setCategory(cartItem.getBrandName());
				lineItem.setDescription(description);
				lineItem.setQuantity(cartItem.getQty());
				lineItem.setUnitOfMeasure("");
				lineItem.setUnitPrice(cartItem.getUnitPrice());
				lineItem.setLineTaxAmount(0.00);
				lineItem.setLineDiscountAmount(0.00);
				lineItem.setLineAmount(cartItem.getTotal());
				lineItemDetail.add(lineItem);
			}
			transaction.setLineItem(lineItemDetail);
											
			billingAddress.setName(salesInputParameter.getBillToName());
			billingAddress.setStreetAddress(salesInputParameter.getBillAddress1());
			billingAddress.setStreetAddress2(salesInputParameter.getBillAddress2());
			billingAddress.setCity(salesInputParameter.getBillCity());
			billingAddress.setState(salesInputParameter.getBillState());
			billingAddress.setPostCode(salesInputParameter.getBillZipCode());
			billingAddress.setCountry(salesInputParameter.getBillCountry());
			billingAddress.setPhoneNumber(salesInputParameter.getBillPhone());
			billingAddress.setEmail(salesInputParameter.getBillEmailAddress());
			
			shippingAddress.setName(salesInputParameter.getShipToName());
			shippingAddress.setStreetAddress(salesInputParameter.getShipAddress1());
			shippingAddress.setStreetAddress2(salesInputParameter.getShipAddress2());
			shippingAddress.setCity(salesInputParameter.getShipCity());
			shippingAddress.setState(salesInputParameter.getShipState());
			shippingAddress.setPostCode(salesInputParameter.getShipZipCode());
			shippingAddress.setCountry(salesInputParameter.getShipCountry());
			shippingAddress.setPhoneNumber(salesInputParameter.getShipPhone());
			shippingAddress.setEmail(salesInputParameter.getShipEmailAddress());
			
			session.setAttribute("shipCompanyName", salesInputParameter.getShipToName());
			session.setAttribute("shipAddress1", salesInputParameter.getShipAddress1());
			session.setAttribute("shipAddress2", salesInputParameter.getShipAddress2());
			session.setAttribute("shipCity", salesInputParameter.getShipCity());
			session.setAttribute("shipState", salesInputParameter.getShipState());
			session.setAttribute("shipZipCode", salesInputParameter.getShipZipCode());
			session.setAttribute("shipCountry", salesInputParameter.getShipCountry());
			session.setAttribute("shipPhoneNumber", salesInputParameter.getShipPhone());
			session.setAttribute("shipEmail",salesInputParameter.getShipEmailAddress());
			
			hostedPayment.setRequireCVV("Yes");
			hostedPayment.setReturnURL(returnUrl);
			hostedPayment.setLanguage("ENG");
			hostedPayment.setLogoURL("");
			hostedPayment.setShippingAgent("");
			hostedPayment.setShippingAgentDescription("");
			hostedPayment.setShippingAgentService("");
			hostedPayment.setShippingAgentServiceDescription("");
			hostedPayment.setConfirmationID("");
			hostedPayment.setPageBackgroundColor("");
			hostedPayment.setButtonBackgroundColor("");
			hostedPayment.setHeaderFontColor("");
			hostedPayment.setFieldLabelFontColor("");
			hostedPayment.setBorderColor("");
			hostedPayment.setErrorColor("");
			hostedPayment.setEmbedded("NO");
			hostedPayment.setMerchantResourceURL("");
			hostedPayment.setSalesperson("");
			hostedPayment.setVATRegistrationNo("");
			hostedPayment.setTaxType("");
			hostedPayment.setWebPaymentGateway("");
			hostedPayment.setWebPaymentTransactionID("");
			hostedPayment.setWebPaymentAmount("");
			hostedPayment.setAllowDefaultPaymentMethod("");
			hostedPayment.setSignature("");
			
			PaymentGatewayRequest chargeLogicRequest=new PaymentGatewayRequest();
			chargeLogicRequest.setCredential(credential);
			chargeLogicRequest.setTransaction(transaction);
			chargeLogicRequest.setBillingAddress(billingAddress);
			chargeLogicRequest.setShippingAddress(shippingAddress);
			chargeLogicRequest.setHostedPayment(hostedPayment);
									
			SecureData password=new SecureData();
				 RestRequest<PaymentGatewayRequest> request = RestRequest.<PaymentGatewayRequest>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
				.withBody(chargeLogicRequest)
				.build();
			 
			Cimm2BCResponse<PaymentGatewayResponse> response = CimmESBServiceUtils.getPaymentGatewayV1Service().getChargeLogicSecureToken(request);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("CHARGE LOGIC REQUEST : "+mapper.writeValueAsString(request));
			System.out.println("CHARGE LOGIC RESPONSE : "+mapper.writeValueAsString(response));
			PaymentGatewayResponse cimmESBServiceChargeLogicResponse = null;
			if(response!=null && response.getStatus().getCode() == 200 && response.getData() != null){
				cimmESBServiceChargeLogicResponse = response.getData();
				if(cimmESBServiceChargeLogicResponse != null){
					salesOutputParameter.setSecureToken(cimmESBServiceChargeLogicResponse.getSecureToken());
					salesOutputParameter.setSecureTokenId(cimmESBServiceChargeLogicResponse.getSecureTokenId());
					
				}
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
			
		return salesOutputParameter;
	}
		
		@SuppressWarnings("unused")
		public static LinkedHashMap<String, Object> QuoteList(SalesModel salesInputParameter) {
			ArrayList<SalesModel> QuoteList = new ArrayList<SalesModel>();
			HttpSession session = salesInputParameter.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			SalesModel pages=new SalesModel();
			Cimm2BCentralOpenOrderInformation CimmEsbQuoteList = new Cimm2BCentralOpenOrderInformation();
			try {
				
				// CIMM ESB
				SecureData password=new SecureData();
				RestRequest<Void> request = RestRequest.<Void>builder()
						.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
						.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
						.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
						.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
						.addQueryParameter("filter.orderStatus", "QUOTE")
						.build();
				@SuppressWarnings("rawtypes")
				Cimm2BCResponse<Cimm2BPagedResponse<List<Cimm2BOpenOrderInfo>>> response = CimmESBServiceUtils.getOrderService().getCurrentCustomerOrders(request);
				ObjectMapper mapper = new ObjectMapper();
				System.out.println(mapper.writeValueAsString(response));
				DozerBeanMapper  dozzer = new DozerBeanMapper();
				dozzer.map(response.getData(),CimmEsbQuoteList);
				
				List<Cimm2BOpenOrderInfo> CimmEsbQuoteDetails = response.getData().getDetails();
					if(CimmEsbQuoteDetails != null){

						for(Cimm2BOpenOrderInfo quotes : CimmEsbQuoteDetails){
							if(salesInputParameter.getSearchString()!=null && salesInputParameter.getSearchString().length()>0) {
								if(salesInputParameter.getSearchString().equals(quotes.getOrderERPId())) {
									SalesModel salesModel = new SalesModel();
									salesModel.setOrderNum(quotes.getOrderERPId());
									salesModel.setErpOrderNumber(quotes.getOrderNumber());
									salesModel.setOrderDate(CommonUtility.convertToStringFromDate(quotes.getOrderDate()));
									salesModel.setTotal(quotes.getOrderTotal()!=null?quotes.getOrderTotal():0);
									salesModel.setPoNumber(quotes.getPoNumber());
									salesModel.setInvoiceNumber(quotes.getOrderERPId());
									salesModel.setShipToName(quotes.getShipToName());
									salesModel.setOpenOrderAmount(quotes.getOrderTotal()!=null?quotes.getOrderTotal():0);
									salesModel.setOrderedBy(quotes.getOrderedBy());
									salesModel.setOrderStatus(quotes.getOrderStatus());
									salesModel.setCustomerName(quotes.getCustomerName());
									//salesModel.setOrderSuffix(CommonUtility.validateNumber(openOrder.getOrderSuffix()));
									QuoteList.add(salesModel);
								}
							}else {
								SalesModel salesModel = new SalesModel();
								salesModel.setOrderNum(quotes.getOrderERPId());
								salesModel.setErpOrderNumber(quotes.getOrderNumber());
								salesModel.setOrderDate(CommonUtility.convertToStringFromDate(quotes.getOrderDate()));
								salesModel.setTotal(quotes.getOrderTotal()!=null?quotes.getOrderTotal():0);
								salesModel.setPoNumber(quotes.getPoNumber());
								salesModel.setInvoiceNumber(quotes.getOrderERPId());
								salesModel.setShipToName(quotes.getShipToName());
								salesModel.setOpenOrderAmount(quotes.getOrderTotal()!=null?quotes.getOrderTotal():0);
								salesModel.setOrderedBy(quotes.getOrderedBy());
								salesModel.setOrderStatus(quotes.getOrderStatus());
								salesModel.setCustomerName(quotes.getCustomerName());
								//salesModel.setOrderSuffix(CommonUtility.validateNumber(openOrder.getOrderSuffix()));
								QuoteList.add(salesModel);
							}

						}
					}
				
				contentObject.put("QuoteList", QuoteList);
				
			}catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
			return contentObject;
			}
		
@SuppressWarnings("unused")
public static LinkedHashMap<String, Object> QuoteDetail(SalesModel salesInputParameter) {
	ArrayList<SalesModel> QuoteList = new ArrayList<SalesModel>();
	HttpSession session = salesInputParameter.getSession();
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	SalesModel pages=new SalesModel();
	String orderId = CommonUtility.validateParseIntegerToString(salesInputParameter.getOrderId());
	Cimm2BCentralOrder order = new Cimm2BCentralOrder();
	try {
		
		// CIMM ESB
		SecureData password=new SecureData();
		RestRequest<Void> request = RestRequest.<Void>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString())).addQueryParameter("orderStatus", "QUOTE")
				.build();
		@SuppressWarnings("rawtypes")
		Cimm2BCResponse<Cimm2BOrder> response = CimmESBServiceUtils.getOrderService().getCurrentCustomerOrder(request, orderId);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(response));
		DozerBeanMapper  dozzer = new DozerBeanMapper();
		dozzer.map(response.getData(),order);
		Cimm2BOrder quoteDate = response.getData();
		SalesModel orderDetailModel = new SalesModel();
		ArrayList<SalesModel>orderItemList = null;
		ArrayList<SalesModel> orderDetail = new ArrayList<SalesModel>();
		if(order!=null){			
				//order.setStatus(response.getStatus().toString());
				orderDetailModel.setOrderType(order.getOrderType());
				orderDetailModel.setOrderNum(order.getOrderNumber());
				orderDetailModel.setCustomerNumber(order.getCustomerERPId());
				orderDetailModel.setOrderID(order.getOrderERPId());
				if(response.getData().getOrderStatus().getOrderStatus()!=null){
					
					orderDetailModel.setOrderStatus(response.getData().getOrderStatus().getOrderStatus());
				}
				
				orderDetailModel.setPoNumber(order.getCustomerPoNumber());
				orderDetailModel.setOrderedBy(order.getOrderedBy());
				orderDetailModel.setShipDate(order.getShipDate());
				orderDetailModel.setOrderDate(CommonUtility.convertToStringFromDate(quoteDate.getOrderDate()));
				orderDetailModel.setReqDate(CommonUtility.convertToStringFromDate(quoteDate.getRequiredDate()));
				orderDetailModel.setOrderSource(order.getOrderSource());
				orderDetailModel.setOrderSuffix(order.getOrderSuffix());
				orderDetailModel.setPoNumber(order.getCustomerPoNumber());
				orderDetailModel.setTax(order.getTaxAmount()!=null?order.getTaxAmount():0);
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
	
				Cimm2BCentralBillAndShipToContact shipContact = order.getShipToContact();
				orderDetailModel.setPhone(shipContact.getPrimaryPhoneNumber());
				orderDetailModel.setShipEmailAddress(shipContact.getPrimaryEmailAddress());

				ArrayList<Cimm2BCentralShipVia> shipMethod = order.getShipVia();
	
				if(shipMethod != null && shipMethod.size()>0){
					for (Cimm2BCentralShipVia orderResponseGetShipMethod : shipMethod) {
						orderDetailModel.setShipMethod(orderResponseGetShipMethod.getShipViaDescription());
						orderDetailModel.setShipMethodId(orderResponseGetShipMethod.getShipViaCode());
						orderDetailModel.setCarrierTrackingNumber(orderResponseGetShipMethod.getCarrierTrackingNumber());
						orderDetailModel.setShipViaErpId(orderResponseGetShipMethod.getShipViaErpId());
						orderDetailModel.setAccountNumber(orderResponseGetShipMethod.getAccountNumber());
	
					}
				}

				if(order.getFreight()!=null){
						orderDetailModel.setFreight(order.getFreight());
				}
				orderItemList = new ArrayList<SalesModel>();
	
				ArrayList<Cimm2BCentralLineItem> lineItemModels = order.getLineItems();
	
				if(lineItemModels != null && lineItemModels.size() > 0){
					for(Cimm2BCentralLineItem itemModel : lineItemModels){
						SalesModel model = new SalesModel();
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
						model.setOrderPrice(itemModel.getQty() *itemModel.getExtendedPrice());
						if(itemModel.getQtyShipped()!=null){
						model.setQtyShipped(itemModel.getQtyShipped().intValue());
						}
						model.setShortDesc(itemModel.getShortDescription());
						model.setUnitPrice(itemModel.getUnitPrice() != null ? itemModel.getUnitPrice() : 0);
						model.setListPrice(itemModel.getListPrice());
						model.setUom(itemModel.getUom());
						orderItemList.add(model);
					}
				}
			}
		contentObject.put("QuoteDetail", orderDetailModel);
		contentObject.put("QuoteItemList", orderItemList);
	}catch (Exception e) {
		logger.error(e.getMessage());
		e.printStackTrace();
	}
	return contentObject;
	}
@SuppressWarnings("unused")
public static LinkedHashMap<String, Object> getTopProducts(SalesModel salesInputParameter) {
	ArrayList<SalesModel> QuoteList = new ArrayList<SalesModel>();
	HttpSession session = salesInputParameter.getSession();
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	SalesModel pages=new SalesModel();
	Cimm2BCentralOpenOrderInformation CimmEsbQuoteList = new Cimm2BCentralOpenOrderInformation();
	try {
		
		// CIMM ESB
		SecureData password=new SecureData();
		RestRequest<Void> request = RestRequest.<Void>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
				.build();
	
		Cimm2BCResponse<List<TopHitItemsDTO>>  response = CimmESBServiceUtils.getTdfTopItems().getTdfTophitItems(request);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(response));
		logger.info("Response: {}",response);
		contentObject.put("response", response.getData());
	}catch (Exception e) {
		logger.error(e.getMessage());
		e.printStackTrace();
	}
	return contentObject;
	}

@SuppressWarnings("unused")
public static LinkedHashMap<String, Object> getTopCategories(SalesModel salesInputParameter) {
	ArrayList<SalesModel> QuoteList = new ArrayList<SalesModel>();
	HttpSession session = salesInputParameter.getSession();
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	SalesModel pages=new SalesModel();
	Cimm2BCentralOpenOrderInformation CimmEsbQuoteList = new Cimm2BCentralOpenOrderInformation();
	try {
		
		// CIMM ESB
		SecureData password=new SecureData();
		RestRequest<Void> request = RestRequest.<Void>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
				.build();
	
		Cimm2BCResponse<List<TopCategoryDTO>>  response = CimmESBServiceUtils.getTdfTopItems().getTdfProducts(request);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(response));
		logger.info("Response: {}",response);
		contentObject.put("response", response.getData());
	}catch (Exception e) {
		logger.error(e.getMessage());
		e.printStackTrace();
	}
	return contentObject;
	}
public static SalesModel createPaypalPaymentV2(SalesModel salesInputParameter) {
	SalesModel salesOutputParameter =  null;
	try {
		String roundTotalStr=CommonUtility.validateParseDoubleToString(salesInputParameter.getTotal());
		salesInputParameter.setTotal(CommonUtility.roundHalfUp(roundTotalStr));
		PaymentGatewayRequestV2 gatewayRequest=new  PaymentGatewayRequestV2();
		gatewayRequest.setAmount(salesInputParameter.getTotal());
		gatewayRequest.setAuthType("A");

		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		SecureData password=new SecureData();
		RestRequest<PaymentGatewayRequestV2> request = RestRequest.<PaymentGatewayRequestV2>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
				.withBody(gatewayRequest)
		     	.build();
		Cimm2BCResponse<PaymentGatewayResponse> response = CimmESBServiceUtils.getPaymentGatewayV1Service().getPayPalSecureTokenV2(request);
		PaymentGatewayResponse cimm2BCPaypalResponse = null;
		cimm2BCPaypalResponse = (PaymentGatewayResponse) response.getData();
		if(cimm2BCPaypalResponse != null){
			salesOutputParameter =  new SalesModel();
			salesOutputParameter.setUserToken(cimm2BCPaypalResponse.getSecureToken());
			salesOutputParameter.setTakenBy(cimm2BCPaypalResponse.getSecureTokenId());
		}
	} catch (Exception e) {
		logger.error(e.getMessage());
		e.printStackTrace();
	}
	return salesOutputParameter;
}

public static int createCspOrder(AddressModel billaddress, double total, Set<Integer> designs, String orderNumber, HttpSession session) throws RestClientException, JsonProcessingException {
	List<Integer> designIds=new ArrayList<>();
	List<CimmCspOrderRequest.AdditionalInfo> additionalInfos =new ArrayList<>();
	int jobId = 0;
	try {
		for (int designId : designs) {
			designIds.add(designId);
		}
		CimmCspOrderRequest cimmCspOrderRequest=new CimmCspOrderRequest();
		cimmCspOrderRequest.setFirstName(billaddress.getFirstName());
		cimmCspOrderRequest.setLastName(billaddress.getLastName());
		cimmCspOrderRequest.setAddress1(billaddress.getAddress1());
		cimmCspOrderRequest.setAddress2(billaddress.getAddress2());
		cimmCspOrderRequest.setCity(billaddress.getCity());
		cimmCspOrderRequest.setCountry(billaddress.getCountry());
		cimmCspOrderRequest.setRegion(billaddress.getState());
		cimmCspOrderRequest.setOrderNumber(orderNumber);
		cimmCspOrderRequest.setPostalCode(billaddress.getZipCode());
		cimmCspOrderRequest.setEmail(billaddress.getEmailAddress());
		cimmCspOrderRequest.setPhone(billaddress.getPhoneNo());
		cimmCspOrderRequest.setOrderTotal(CommonUtility.validateInteger(total));
		cimmCspOrderRequest.setDesignIds(designIds);
		cimmCspOrderRequest.setAdditionalInfo(additionalInfos);
		SecureData password=new SecureData();
		RestRequest<CimmCspOrderRequest> request = RestRequest.<CimmCspOrderRequest>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString())).withBody(cimmCspOrderRequest)
				.build();
		ObjectMapper mapper2 = new ObjectMapper();
		System.out.println(mapper2.writeValueAsString(cimmCspOrderRequest));
		
		Cimm2BCResponse<CimmCspResponse>ecommCspResponse= CimmESBServiceUtils.getlogoDetails().getCspCreateOrder(request);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(ecommCspResponse));
		if(ecommCspResponse!=null && ecommCspResponse.getData() != null && ecommCspResponse.getStatus() != null && ecommCspResponse.getStatus().getCode() == HttpStatus.SC_OK){
			CimmCspResponse cimmresponse = ecommCspResponse.getData();
			jobId = cimmresponse.getJobId();
		}
	}catch (Exception e) {
		logger.error(e.getMessage());
		e.printStackTrace();
	}
	return jobId;
}

public Cimm2BCResponse<CimmOrder> validateCoupons(CimmOrderRequest orderRequest, HttpSession session) throws RestClientException, JsonProcessingException {
	SecureData password = new SecureData();
	Cimm2BCResponse<CimmOrder> response = new Cimm2BCResponse<CimmOrder>();
	try {
		RestRequest<CimmOrderRequest> request = RestRequest.<CimmOrderRequest>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY)))
				.withUserSecret(password.validatePassword((String)session.getAttribute("securedPassword"))).withBody(orderRequest)
				.build();
		
		ObjectMapper mapper = new ObjectMapper(); 
		System.out.println(mapper.writeValueAsString(orderRequest));
		String customerId= CommonUtility.validateString((String) session.getAttribute("billingEntityId"));
		
		response =  CimmESBServiceUtils.getOrderService().validateCustomerOrderCoupon(request, customerId); 
		
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	return response;
}

}