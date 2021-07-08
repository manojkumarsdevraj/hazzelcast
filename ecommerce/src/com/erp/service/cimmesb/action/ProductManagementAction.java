package com.erp.service.cimmesb.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.http.HttpStatus;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerPartNumberInformation;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPriceBreaks;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralcpnLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralcustomerPartNumber;
import com.erp.service.cimm2bcentral.utilities.Cimm2BcustomerPartNumberRequest;
import com.erp.service.cimm2bcentral.utilities.ProductsUtility;
import com.erp.service.cimmesb.utils.CimmESBServiceUtils;
import com.erp.service.model.ProductManagementModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.unilog.cimmesb.client.ecomm.request.CimmProductQueryLineItem;
import com.unilog.cimmesb.client.ecomm.request.ErpItemSearchRequest;
import com.unilog.cimmesb.client.ecomm.response.CimmItem;
import com.unilog.cimmesb.client.ecomm.response.CimmCspResponse;
import com.unilog.cimmesb.client.ecomm.response.CimmLineItem;
import com.unilog.cimmesb.client.exception.RestClientException;
import com.unilog.cimmesb.client.request.RestRequest;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCResponse;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BItem;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BPriceAndAvailabilityResponse;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BUOM;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BWarehouse;
import com.unilog.cimmesb.ecomm.cimm2bc.model.CimmPriceBreak;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.logocustomization.CustomizationCharges;
import com.unilog.logocustomization.LogoCustomizationModule;
import com.unilog.products.ProductsModel;
import com.unilog.security.SecureData;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class ProductManagementAction {
	
	private static ProductManagementAction instance;
	private static HttpServletRequest request;

	public static ProductManagementAction getInstance() {
		synchronized (ProductManagementAction.class) {
			if(instance == null) {
				instance = new ProductManagementAction();
			}
		}
		return instance;
	}
	
	static final Logger logger = LoggerFactory.getLogger(ProductManagementAction.class);
	public static String getAjaxPriceFromErp(ProductManagementModel priceInquiryInput)
	{

		String jsonResponse = "";
		Gson gson = new Gson();

		try
		{
			ArrayList<ProductsModel> priceResponse = massProductInquiry(priceInquiryInput);
			
			if(CommonUtility.convertToBoolean(CommonDBQuery.getSystemParamtersList().get("ENABLE_CPN_SYNC"))){
				String sessionUserId = (String) priceInquiryInput.getSession().getAttribute(Global.USERID_KEY);
				String entityId = (String) priceInquiryInput.getSession().getAttribute("billingEntityId");
				int userId = CommonUtility.validateNumber(sessionUserId);
				int buyingCompanyId = CommonUtility.validateNumber(priceInquiryInput.getSession().getAttribute("buyingCompanyId").toString());
				for(ProductsModel productsModel : priceResponse){
					productsModel.setCpnSyncResult(ProductsUtility.syncCPN(productsModel.getItemId(), buyingCompanyId, userId, productsModel.getCustomerPartNumberList(), entityId, "", productsModel.getPartNumber(), ""));
				}
			}
			if(priceResponse!=null && priceResponse.size()>0){
				jsonResponse = gson.toJson(priceResponse);	
			}
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return jsonResponse;
	}


	public static ArrayList<ProductsModel> massProductInquiry(ProductManagementModel priceInquiryInput) {
		try{
			HttpSession session = priceInquiryInput.getSession();
			String tempWarehouseCode = (String)session.getAttribute("wareHouseCode");
			if(CommonUtility.validateString(tempWarehouseCode).length() == 0){
				tempWarehouseCode = CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR");
			}
			final String warehouseCode = tempWarehouseCode;
			List<Cimm2BItem> pricingResponses = getInstance().processPriceEnquiryRequest(priceInquiryInput);
			if(pricingResponses.size() > 0) {
				for(ProductsModel item : priceInquiryInput.getPartIdentifier()) {
					for(Cimm2BItem pricingResponse : pricingResponses) {
						if(pricingResponse.getPartIdentifier().equals(item.getPartNumber())) {
							setGeneralInfo(item, pricingResponse);
							
							item.setBranchAvail(extractBranchDetails(pricingResponse));
							
							if(pricingResponse.getPricingWarehouse() != null && pricingResponse.getPricingWarehouse().getUomList()!=null) {
								setPriceDetails(item, pricingResponse.getPricingWarehouse());
							}else{
								Optional<Cimm2BWarehouse> homeBranch = pricingResponse.getWarehouses().stream().filter(eachWarehouse -> warehouseCode.equalsIgnoreCase(eachWarehouse.getWarehouseCode())).findAny();
								if(!homeBranch.isPresent()) {
									homeBranch = pricingResponse.getWarehouses().stream().findFirst();
								}
								homeBranch.ifPresent(details -> {
									setPriceDetails(item, details);
								});
							}
							if(CommonUtility.validateString(pricingResponse.getStockStatus()).length() > 0) {							
								item.setStockStatus(CommonUtility.validateString(pricingResponse.getStockStatus()));
							}
							break;
						}
					}
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return priceInquiryInput.getPartIdentifier();
	}
	
	private static void setGeneralInfo(ProductsModel item,Cimm2BItem pricingResponse) {
		if(pricingResponse.getMinimumOrderQuantity() != null) {
			item.setMinimumOrderQuantity(pricingResponse.getMinimumOrderQuantity());
		}
		if(pricingResponse.getOrderQuantityInterval() != null) {
			item.setOrderQuantityInterval(pricingResponse.getOrderQuantityInterval());
		}
		if(pricingResponse.getAvailableDescription() != null) {
			item.setAvailableDescription(pricingResponse.getAvailableDescription());
		}
		if(CommonUtility.validateString(pricingResponse.getCustomerPartNumber()).length()>0) {
	            item.setCustomerPartNumber(pricingResponse.getCustomerPartNumber());
	    }
		if(CommonUtility.validateString(pricingResponse.getDescription()).length()>0) {
			item.setCustomDescription(pricingResponse.getDescription());
		}
	}
	
	private static void setPriceDetails(ProductsModel item, Cimm2BWarehouse priceDetails) {
		final String uom = CommonUtility.validateString(item.getUom());
		Optional<Cimm2BUOM> priceByUom;
        request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(CommonUtility.validateString(uom).length() > 0) {
			priceByUom = priceDetails.getUomList().stream().filter(u -> uom.equalsIgnoreCase(u.getUom())).findAny();
		}else {
			priceByUom = priceDetails.getUomList().stream().filter(u -> u.getDefaultUom()).findAny();
		}
		
		if(!priceByUom.isPresent()) {
			priceByUom = priceDetails.getUomList().stream().findFirst();
		}
		priceByUom.ifPresent(details -> {
			LinkedHashMap<String, String> customerCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("customerCustomFieldValue");
			
			 if(customerCustomFieldValue!= null && CommonUtility.validateString((String)customerCustomFieldValue.get("OVERRIDE_UOM_FROM_CIMM")).equalsIgnoreCase("Y"))
			 {
				 item.setUom(item.getUom());
			 }
			 else
			 {
				item.setUom(details.getUom());
			 }
			item.setPrice(details.getCustomerPrice()!=null ? details.getCustomerPrice():details.getUnitPrice());
			item.setCustomerPrice(details.getCustomerPrice()!=null ? details.getCustomerPrice():details.getUnitPrice());
			item.setListPrice(details.getListPrice() != null ? details.getListPrice() : 0);
			item.setUnitPrice(details.getUnitPrice() != null ? details.getUnitPrice() : details.getCustomerPrice());
			item.setImapPrice(details.getImapPrice() != null ? details.getImapPrice() : 0);
			item.setQuantity(details.getQuantityAvailable() != null ? details.getQuantityAvailable().intValue() : 0);
		});
		
		if(priceDetails.getUomList() != null) {
			item.setUomList(extractUoms(priceDetails));
		}
		if(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")!=null && CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK").trim().equalsIgnoreCase("Y") ){
		if(priceDetails.getPriceBreaks()!=null){
			item.setQuantityBreakList(extractBreak(priceDetails, item));
		}
		}
		
	}
	

	public static ArrayList<ProductsModel> getPriceFromERP(ProductManagementModel priceInquiryInput,ArrayList<ProductsModel> productList){
		ArrayList<ProductsModel> productPriceOutput = null;
		DecimalFormat df = CommonUtility.getPricePrecision(priceInquiryInput.getSession());
		HttpSession session = priceInquiryInput.getSession();
		String entityID = "0";
		boolean quantityBreakFlag = false;
		if(session!=null && (String)session.getAttribute("entityId")!=null) {
			entityID= (String)session.getAttribute("entityId");
		}
		//DecimalFormat df1 = new DecimalFormat("####0.#####");

		try
		{
			productPriceOutput = massProductInquiry(priceInquiryInput);
			double total = 0;

			if(productList!=null && productList.size()>0){
				for(ProductsModel itemPrice : productList){
					double price = 0;
					double priceFromEcommerce = itemPrice.getPrice();
					double unitPriceFromEcommerce = itemPrice.getUnitPrice();
					
					if(itemPrice!=null){
						if(productPriceOutput!=null && productPriceOutput.size()>0){
							for(ProductsModel eclipseitemPrice : productPriceOutput){
								if(itemPrice.getPartNumber().equals(eclipseitemPrice.getPartNumber().trim())){
									if(CommonUtility.validateString(itemPrice.getUom()).length()==0 || CommonUtility.validateString(itemPrice.getUom()).equalsIgnoreCase(CommonUtility.validateString(eclipseitemPrice.getUom()))){
									itemPrice.setUom(eclipseitemPrice.getUom());
									/**
									 *Below code Written is for Turtle and Hughes to get Custom UOM *Reference- Nitesh
									 */
									if(CommonUtility.customServiceUtility()!=null) {
										String uom = CommonUtility.customServiceUtility().getUom(eclipseitemPrice.getSalesQuantity());
										if(CommonUtility.validateString(uom).trim().length() > 0) {
											itemPrice.setUom(uom);
										}
									}
									if(eclipseitemPrice.getAvailableDescription() != null){
									itemPrice.setAvailableDescription(eclipseitemPrice.getAvailableDescription());
									}
									itemPrice.setBranchAvail(eclipseitemPrice.getBranchAvail());
									itemPrice.setBranchTotalQty(eclipseitemPrice.getBranchTotalQty());
									if(eclipseitemPrice.getUomList()!=null && eclipseitemPrice.getUomList().size()>0)
									itemPrice.setAvailQty(CommonUtility.validateNumber((eclipseitemPrice.getUomList().get(0).getBranchAvailability())));
									itemPrice.setRestrictiveProduct(CommonUtility.validateString(eclipseitemPrice.getRestrictiveProduct()));
									itemPrice.setShipMethodQuantity(eclipseitemPrice.getShipMethodQuantity());
									itemPrice.setPromoPrice(eclipseitemPrice.getPromoPrice());
									itemPrice.setSalesQuantity(eclipseitemPrice.getSalesQuantity());
									itemPrice.setUomPack(eclipseitemPrice.getUomPack());
									itemPrice.setProductCategory(eclipseitemPrice.getProductCategory());
									itemPrice.setMinimumOrderQuantity(eclipseitemPrice.getMinimumOrderQuantity());
									itemPrice.setOrderQuantityInterval(eclipseitemPrice.getOrderQuantityInterval());
									if(eclipseitemPrice.getMinOrderQty()>0){
										itemPrice.setMinOrderQty(eclipseitemPrice.getMinOrderQty());
									}
									if(eclipseitemPrice.getMinOrderQty()>0){
										itemPrice.setOrderInterval(eclipseitemPrice.getOrderInterval());
									}
									if(eclipseitemPrice.getMinOrderQty()>0){
										itemPrice.setMinimumOrderQuantity(eclipseitemPrice.getMinimumOrderQuantity());
									}
									if(eclipseitemPrice.getMinOrderQty()>0){
										itemPrice.setMinimumOrderInterval(eclipseitemPrice.getMinimumOrderInterval());
									}
									itemPrice.setCurrencyCode(eclipseitemPrice.getCurrencyCode());
									if(CommonUtility.customServiceUtility()!=null) {
										CommonUtility.customServiceUtility().setMinOrdQtyandQtyIntForMultpleUOM(eclipseitemPrice,itemPrice);
									}
									
										//--Override ERP Price from CIMM Net Price
										if(!CommonUtility.validateString(itemPrice.getOverRidePriceRule()).equalsIgnoreCase("Y")){
											itemPrice.setUnitPrice(eclipseitemPrice.getPrice());
											if(CommonUtility.customServiceUtility()!=null) {
												CommonUtility.customServiceUtility().getUnitPrice(eclipseitemPrice,itemPrice);
											}
											itemPrice.setListPrice(eclipseitemPrice.getListPrice());
											itemPrice.setPrice(eclipseitemPrice.getPrice());
											
											//--- Check for price change - related to price saved in Cart
											if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInShoppingCart")).equalsIgnoreCase("Y")) {
												itemPrice.setPriceChanged(priceFromEcommerce != itemPrice.getPrice() ? true : false);
												itemPrice.setUnitPriceChanged(unitPriceFromEcommerce != itemPrice.getUnitPrice() ? true : false);
												itemPrice.setOldPrice(priceFromEcommerce);
												itemPrice.setOldUnitPrice(unitPriceFromEcommerce);
												System.out.println("priceFromEcommerce: "+priceFromEcommerce+" - itemPrice.getPrice() : "+itemPrice.getPrice());
												System.out.println("unitPriceFromEcommerce: "+unitPriceFromEcommerce+" - itemPrice.getUnitPrice() : "+itemPrice.getUnitPrice());
											}//--- Check for price change - related to price saved in Cart
											
												if(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID")!=null && entityID!=null && entityID.equalsIgnoreCase(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID"))){
													price = price + eclipseitemPrice.getListPrice();
													itemPrice.setUnitPrice(eclipseitemPrice.getListPrice());
													if(CommonUtility.customServiceUtility()!=null) {
														CommonUtility.customServiceUtility().getUnitPrice(eclipseitemPrice,itemPrice);
													}
													if(CommonUtility.customServiceUtility()!=null) {
														price=CommonUtility.customServiceUtility().getdefaultCustomerPrice(eclipseitemPrice,itemPrice,price);
													}
													
													//--- Check for price change - related to price saved in Cart
													if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInShoppingCart")).equalsIgnoreCase("Y")) {
														itemPrice.setUnitPriceChanged(unitPriceFromEcommerce != itemPrice.getUnitPrice() ? true : false);
														itemPrice.setOldUnitPrice(unitPriceFromEcommerce);
														System.out.println("unitPriceFromEcommerce: "+unitPriceFromEcommerce+" - itemPrice.getUnitPrice() : "+itemPrice.getUnitPrice());
													}//--- Check for price change - related to price saved in Cart
													
													break;
												}else{
													if(eclipseitemPrice.getPromoPrice()>0) {
														price = price + eclipseitemPrice.getPromoPrice();
													}else if(eclipseitemPrice.getImapPrice()>0) {
														price = price + eclipseitemPrice.getImapPrice();
													}else {
														price = price + eclipseitemPrice.getPrice();
														if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")).equalsIgnoreCase("Y")){
															if(itemPrice.getQuantityBreakList()!=null && itemPrice.getQuantityBreakList().size()>0){
																for(int i=0;i<itemPrice.getQuantityBreakList().size();i++){
																	if(itemPrice.getQty() >= itemPrice.getQuantityBreakList().get(i).getMinimumQuantityBreak())
																	{
																		itemPrice.setPrice(itemPrice.getQuantityBreakList().get(i).getPrice());
																		itemPrice.setUnitPrice(itemPrice.getQuantityBreakList().get(i).getPrice());
																		price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(itemPrice.getQuantityBreakList().get(i).getPrice()));
																		quantityBreakFlag =true;
																		
																		//--- Check for price change - related to price saved in Cart
																		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInShoppingCart")).equalsIgnoreCase("Y")) {
																			itemPrice.setPriceChanged(priceFromEcommerce != itemPrice.getPrice() ? true : false);
																			itemPrice.setUnitPriceChanged(unitPriceFromEcommerce != itemPrice.getUnitPrice() ? true : false);
																			itemPrice.setOldPrice(priceFromEcommerce);
																			itemPrice.setOldUnitPrice(unitPriceFromEcommerce);
																			System.out.println("priceFromEcommerce: "+priceFromEcommerce+" - itemPrice.getPrice() : "+itemPrice.getPrice());
																			System.out.println("unitPriceFromEcommerce: "+unitPriceFromEcommerce+" - itemPrice.getUnitPrice() : "+itemPrice.getUnitPrice());
																		}//--- Check for price change - related to price saved in Cart
																	}
																}
															}
														}
													}
													// Overriding promo/imap price
													if(CommonUtility.customServiceUtility()!=null) {
														price = CommonUtility.customServiceUtility().getPriceCustomService(price,itemPrice,quantityBreakFlag);
														itemPrice.setUnitPrice(price);
													}
													break;
												}
											}
									//Removed on 24 April 2019 
									}/*else {
										price = price + itemPrice.getPrice(); //--Override ERP Price from CIMM Net Price
										break;
									}*/
									//--Override ERP Price from CIMM Net Price
								}
							}
							}
						}
						if(CommonUtility.validateString(itemPrice.getOverRidePriceRule()).equalsIgnoreCase("Y") && price==0.0) {
							price = itemPrice.getPrice();
						}
						if(CommonUtility.customServiceUtility()!=null) {
							double extndPrice = CommonUtility.customServiceUtility().getExtendedPrice(price,itemPrice.getQty(),itemPrice.getSaleQty(),itemPrice.getSalesQuantity());
							if(extndPrice > 0) {
								itemPrice.setTotal(extndPrice);
							}
							else{
								itemPrice.setTotal(price * itemPrice.getQty());
							}
							CommonUtility.customServiceUtility().getTotalForDefault(itemPrice,price);
						}
						else{

							itemPrice.setTotal(price * itemPrice.getQty());
						}
						double roundedTotal;
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WITHOUT_ROUNDUP")).equalsIgnoreCase("Y")){
							roundedTotal = itemPrice.getTotal();
						}else{
							roundedTotal = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(itemPrice.getTotal()));
						}
						logger.info("Price of " + itemPrice.getPartNumber() + " - " + df.format(roundedTotal));
						total = total + roundedTotal;
						itemPrice.setCartTotal(total);
					}

				}
				
			if(productList!=null && productList.size()>0)
				productList.get(0).setCartTotal(total);
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return productList;
	}
	public static ArrayList<ProductsModel> customerPartNumberQuery(ProductManagementModel priceInquiryInput) {

		ArrayList<ProductsModel> customerPartNumberlist = new ArrayList<ProductsModel>();

		try{
				String partnumber = priceInquiryInput.getProductNumber();
				String customerErpId = priceInquiryInput.getEntityId();

			String GET_CUSTOMER_PART_NUMBER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_PART_NUMBER_URL")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +customerErpId+"&"+ Cimm2BCentralRequestParams.partNumber+ "=" + URLEncoder.encode(partnumber, "UTF-8");

			Cimm2BCentralResponseEntity customerPartNumberResponseEntity  = Cimm2BCentralClient.getInstance().getDataObject(GET_CUSTOMER_PART_NUMBER_URL, "GET", null, Cimm2BCentralCustomerPartNumberInformation.class);
			Cimm2BCentralCustomerPartNumberInformation customerPartNumberInformation=null;	
			if(customerPartNumberResponseEntity!=null && customerPartNumberResponseEntity.getData() != null &&  customerPartNumberResponseEntity.getStatus().getCode() == 200 ){
			customerPartNumberInformation=(Cimm2BCentralCustomerPartNumberInformation)customerPartNumberResponseEntity.getData();

			ArrayList<Cimm2BCentralcpnLineItem> cpnInfo= null;
				if(customerPartNumberInformation!=null){
					cpnInfo=customerPartNumberInformation.getLineItems();
					if(cpnInfo != null && cpnInfo.size() > 0){
						for(Cimm2BCentralcpnLineItem item : cpnInfo ){
							ProductsModel productsModel = new ProductsModel();
							productsModel.setCustomerPartNumber(item.getCustomerPartNumber());
							logger.info(item.getCustomerPartNumber());	
							productsModel.setPartNumber(item.getPartNumber());
							logger.info(item.getPartNumber());	
							customerPartNumberlist.add(productsModel);
						}
					}
				}
			}
		
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return customerPartNumberlist;
	}
	public static ArrayList<ProductsModel> getAlternateItems(ProductManagementModel priceInquiryInput) {

		ArrayList<ProductsModel> alternateItemsList = new ArrayList<ProductsModel>();

		try{
			alternateItemsList = null;


		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return alternateItemsList;
	}
	
	public static ProductManagementModel customerPartNumberCreate(ProductManagementModel priceInquiryInput) {

		ProductManagementModel customerPartNumberResponse = new ProductManagementModel();
		//HttpSession session = priceInquiryInput.getSession();
		try{
			
			String partnumber = priceInquiryInput.getProductNumber();
			String customerPartNUmber=priceInquiryInput.getNewCustomerPartNumber();
			String customerErpId =priceInquiryInput.getCustomerId();
			String cpn=null;
			//String cpnSuccesMessage=null;

			Cimm2BcustomerPartNumberRequest customerPartNumberRequest = new Cimm2BcustomerPartNumberRequest();
			ArrayList<Cimm2BCentralcpnLineItem> lineItems =new ArrayList<Cimm2BCentralcpnLineItem>();
			
			Cimm2BCentralcpnLineItem Cimm2BCentralcpnLineItem=new Cimm2BCentralcpnLineItem();
			Cimm2BCentralcpnLineItem.setPartNumber(partnumber);
			Cimm2BCentralcpnLineItem.setCustomerPartNumber(customerPartNUmber);
			
			lineItems.add(Cimm2BCentralcpnLineItem);
			
			customerPartNumberRequest.setCustomerERPId(customerErpId);
			customerPartNumberRequest.setLineItems(lineItems);
			
			String CREATE_CUSTOMER_PART_NUMBER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_CUSTOMER_PART_NUMBER_URL"));
			Cimm2BCentralResponseEntity customerPartNumberResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_CUSTOMER_PART_NUMBER_URL, "POST", customerPartNumberRequest, Cimm2BCentralcustomerPartNumber.class);
			if(customerPartNumberResponseEntity!=null && customerPartNumberResponseEntity.getData() != null &&  customerPartNumberResponseEntity.getStatus().getCode() == 200 ){
				cpn=customerPartNumberResponseEntity.getStatus().getCode().toString();
				//cpnSuccesMessage = customerPartNumberResponseEntity.getStatus().getMessage();	
				customerPartNumberResponse.setErrorCode("0");
				customerPartNumberResponse.setErrorMessage(null);
			}
			logger.info(cpn);
		
			
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return customerPartNumberResponse;
	}
	
	public static ProductManagementModel customerPartNumberDelete(ProductManagementModel priceInquiryInput) {

		ProductManagementModel customerPartNumberResponse = new ProductManagementModel();
		//HttpSession session = priceInquiryInput.getSession();
		try {

			String partnumber = priceInquiryInput.getProductNumber();
			String[] customerPartNumber = priceInquiryInput.getCustomerPartNumbers();
			String customerErpId = priceInquiryInput.getCustomerId();
			String cpn = null;
			//String cpnSuccesMessage = null;
			Cimm2BcustomerPartNumberRequest customerPartNumberRequest = new Cimm2BcustomerPartNumberRequest();
			ArrayList<Cimm2BCentralcpnLineItem> lineItems = new ArrayList<Cimm2BCentralcpnLineItem>();
			if (customerPartNumber != null && customerPartNumber.length > 0) {
				int i = 0;
				for (i = 0; i < customerPartNumber.length; i++)
				{
					Cimm2BCentralcpnLineItem Cimm2BCentralcpnLineItem = new Cimm2BCentralcpnLineItem();
					String customerPartNum = customerPartNumber[i];
					Cimm2BCentralcpnLineItem.setPartNumber(partnumber);
					Cimm2BCentralcpnLineItem
					.setCustomerPartNumber(customerPartNum);
					lineItems.add(Cimm2BCentralcpnLineItem);
				}
			}
			customerPartNumberRequest.setCustomerERPId(customerErpId);
			customerPartNumberRequest.setLineItems(lineItems);

			String DELETE_CUSTOMER_PART_NUMBER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DELETE_CUSTOMER_PART_NUMBER_URL"));
			Cimm2BCentralResponseEntity customerPartNumberResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(DELETE_CUSTOMER_PART_NUMBER_URL, HttpMethod.DELETE,customerPartNumberRequest,Cimm2BCentralcustomerPartNumber.class);
			if(customerPartNumberResponseEntity != null && customerPartNumberResponseEntity.getData() != null && customerPartNumberResponseEntity.getStatus().getCode() == 200) {
				cpn = customerPartNumberResponseEntity.getStatus().getCode().toString();
				//cpnSuccesMessage = customerPartNumberResponseEntity.getStatus().getMessage();
				customerPartNumberResponse.setErrorCode("0");
				customerPartNumberResponse.setErrorMessage(null);
			}
			logger.info(cpn);
			
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return customerPartNumberResponse;
	}

	public static String ajaxBasePriceAndLeadTimeFromErp(ProductManagementModel priceInquiryInput)
	{

		String jsonResponse = "";
		Gson gson = new Gson();
		try
		{
			ArrayList<ProductsModel> priceResponse = new ArrayList<ProductsModel>();
			jsonResponse = gson.toJson(priceResponse);
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return jsonResponse;
	}
	
	public static ProductManagementModel customerPartNumberInquiry(ProductManagementModel priceInquiryInput) {
		ProductManagementModel customerPartNumberUpdateResponse = null;
		try{
			customerPartNumberUpdateResponse = new ProductManagementModel();
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return customerPartNumberUpdateResponse;
	}

	public static UsersModel getCustomerDetail(UsersModel userInfo){
		UsersModel customerDetails = null;
		try{
			customerDetails = new UsersModel();
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return customerDetails;
	}
	public static ArrayList<ProductsModel> getItemMultipleUOMData(String partNumber){
		return null;
	}
	private List<Cimm2BItem> processPriceEnquiryRequest(ProductManagementModel priceInquiryInput){
		HttpSession session = priceInquiryInput.getSession();
		ErpItemSearchRequest requestBody = buildPriceEnquiryRequestBody(priceInquiryInput, session);
		if(CommonUtility.customServiceUtility()!=null) {
			requestBody = CommonUtility.customServiceUtility().buildCustomPriceEnquiryRequestBody(requestBody, priceInquiryInput, session);
		}
		SecureData password=new SecureData();
		RestRequest<ErpItemSearchRequest> request = RestRequest.<ErpItemSearchRequest>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString()).withBody(requestBody)
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
				.build();
		try {
			if(requestBody.getLineItems().size() > 0) {
				ObjectMapper mapper = new ObjectMapper();
				System.out.println("CIMM ESB Price Request:"+mapper.writeValueAsString(requestBody));
				Cimm2BCResponse<Cimm2BPriceAndAvailabilityResponse> response = CimmESBServiceUtils.getPriceEnquiryService().getPriceAndAvailability(request);
				System.out.println("Price Response from CIMMESB:"+mapper.writeValueAsString(response));
				if(response != null && response.getStatus() != null && response.getStatus().getCode() == HttpStatus.SC_OK) {
					logger.info("price enquiry success");
					return response.getData().getItems();
				}
			}
		} catch (UnsupportedEncodingException | RestClientException | JsonProcessingException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	private static ArrayList<ProductsModel>  extractBranchDetails(Cimm2BItem item) {
		return item.getWarehouses().stream().map(b -> extractBranch(b)).collect(Collectors.toCollection(ArrayList::new));
	}
	
	private static ProductsModel extractBranch(Cimm2BWarehouse details) {
		ProductsModel branchDetails = new ProductsModel();
		branchDetails.setWareHouseCode(details.getWarehouseCode());
		branchDetails.setWarehouseName(CommonUtility.getWareHouseByCode(details.getWarehouseCode()).getWareHouseName());
		branchDetails.setBranchAvailability(details.getAvailabilityText());
		if(details.getClearanceItem()!=null){
		branchDetails.setClearance(String.valueOf(details.getClearanceItem()));}
		
		if(details.getUomList() != null) {
			branchDetails.setUomList(extractUoms(details));
		}
		
		return branchDetails;
	}
	
	
	private static ArrayList<ProductsModel> extractBreak(Cimm2BWarehouse priceDetails,ProductsModel item) {
		return (priceDetails).getPriceBreaks().stream().map(priceBreaks->
		{
			ArrayList<ProductsModel> quantityBreak = new ArrayList<ProductsModel>();
			ProductsModel eachBreak = new ProductsModel();
			
			if(priceBreaks.getMaximumQuantity()!=null) {
				eachBreak.setMaximumQuantityBreak(priceBreaks.getMaximumQuantity()!=null?priceBreaks.getMaximumQuantity():0.0);}
			eachBreak.setMinimumQuantityBreak(priceBreaks.getMinimumQuantity()!=null?priceBreaks.getMinimumQuantity():0.0);
			eachBreak.setPrice(priceBreaks.getCustomerPrice()!=null?priceBreaks.getCustomerPrice():0.0);
			eachBreak.setUom(priceBreaks.getUom()!=null?priceBreaks.getUom():"");
			
			quantityBreak.add(eachBreak);
			if(item.getPartIdentifierQuantity()!=null){
				for(int i=0;i<quantityBreak.size();i++)
                 {
					logger.info(quantityBreak.get(i).getCustomerPriceBreak() + " : " + quantityBreak.get(i).getMinimumQuantityBreak()+ " : " + quantityBreak.get(i).getMaximumQuantityBreak());
                
                   if(item.getPartIdentifierQuantity().get(0) >= quantityBreak.get(i).getMinimumQuantityBreak() && item.getPartIdentifierQuantity().get(0)<= quantityBreak.get(i).getMaximumQuantityBreak())
                  {
                	   eachBreak.setPrice(quantityBreak.get(i).getCustomerPriceBreak());  
                	   break;
                  }else{
                	  eachBreak.setPrice(item.getCimm2BCentralPricingWarehouse().getCustomerPrice());
                	 
                  }
                 }
				}
			return eachBreak;
		
		}).collect(Collectors.toCollection(ArrayList::new));
	}


	private static ArrayList<ProductsModel> extractQty(Cimm2BWarehouse details) {
		return details.getUomList().stream().map(uom -> {
			ProductsModel eachUom = new ProductsModel();
			eachUom.setUom(uom.getUom());
			eachUom.setCustomerPrice(uom.getCustomerPrice()!=null ? uom.getCustomerPrice():uom.getUnitPrice());
			eachUom.setListPrice(uom.getListPrice() != null ? uom.getListPrice() : 0);
			eachUom.setUnitPrice(uom.getUnitPrice() != null ? uom.getUnitPrice() : uom.getCustomerPrice());
			eachUom.setImapPrice(uom.getImapPrice() != null ? uom.getImapPrice() : 0);
			eachUom.setBranchAvailability(CommonUtility.validateParseIntegerToString(uom.getQuantityAvailable() != null ? uom.getQuantityAvailable().intValue() : 0));
				
			return eachUom;
		}).collect(Collectors.toCollection(ArrayList::new));
	}
	private static ArrayList<ProductsModel> extractUoms(Cimm2BWarehouse details) {
		return details.getUomList().stream().map(uom -> {
			ProductsModel eachUom = new ProductsModel();
			eachUom.setUom(uom.getUom());
			eachUom.setCustomerPrice(uom.getCustomerPrice()!=null ? uom.getCustomerPrice():uom.getUnitPrice());
			eachUom.setListPrice(uom.getListPrice() != null ? uom.getListPrice() : 0);
			eachUom.setUnitPrice(uom.getUnitPrice() != null ? uom.getUnitPrice() : uom.getCustomerPrice());
			eachUom.setImapPrice(uom.getImapPrice() != null ? uom.getImapPrice() : 0);
			eachUom.setBranchAvailability(CommonUtility.validateParseIntegerToString(uom.getQuantityAvailable() != null ? uom.getQuantityAvailable().intValue() : 0));
			eachUom.setUomQty(CommonUtility.validateInteger(uom.getUomQuantity()));
			eachUom.setUomQtyERP(CommonUtility.validateInteger(uom.getQtyPerSellUOM()));
			eachUom.setUomDescription(CommonUtility.validateString(uom.getUomDescription()));
			return eachUom;
		}).collect(Collectors.toCollection(ArrayList::new));
	}
	
	private static ErpItemSearchRequest buildPriceEnquiryRequestBody(ProductManagementModel priceInquiryInput, HttpSession session) {
		String warehouseCode = (String)session.getAttribute("wareHouseCode");
		if(CommonUtility.validateString(warehouseCode).length() == 0){
			warehouseCode = CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR");
		}
		Map<String, String> wareHouseList = new HashMap<>();
		if(CommonUtility.convertToBoolean(priceInquiryInput.getAllBranchavailabilityRequired())){
			wareHouseList = CommonDBQuery.getWareHouseList();
		}
		else {
		wareHouseList.put(warehouseCode, warehouseCode);
		}
		return new ErpItemSearchRequest.Builder()
				.pricingWarehouseCode(warehouseCode)
				.lineItems(extractLineItems(priceInquiryInput.getPartIdentifier()))
				.warehouseCodes(wareHouseList.keySet())
				.build();
	}

	private static List<CimmProductQueryLineItem> extractLineItems(List<ProductsModel> products) {
		return products.stream().filter(i -> !(CommonUtility.validateString(i.getOverRidePriceRule()).equalsIgnoreCase("Y") && i.getPrice() > 0))
				.map(i -> {
				return new CimmProductQueryLineItem.Builder(i.getPartNumber())
						.quantity(Double.valueOf(i.getQty()))
						.uom(i.getUom())
						.build();
			}).collect(Collectors.toList());
	}
	
	private static <T> RestRequest<T> buildRequest(T requestBody,Class<?> dataClass,HttpSession session){
		SecureData password=new SecureData();
		 RestRequest<T> request =RestRequest.<T>builder()
		.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
		.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
		.withUserName(session.getAttribute(Global.USERNAME_KEY).toString()).withBody(requestBody)
		.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
		.build();
		return request;
	}
	
	public static LinkedHashMap<String, Object> getItemHistoryDetails(ProductsModel productParameters) {
		HttpSession session = productParameters.getSession();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		try {
			
			// CIMM ESB
			SecureData password=new SecureData();
			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
					.addQueryParameter("partNumber", CommonUtility.validateString(productParameters.getPartNumber()).length()>0?productParameters.getPartNumber():"")
					.build();
			
			Cimm2BCResponse<CimmItem> response = CimmESBServiceUtils.getPriceEnquiryService().getlastPricePaidItemDetails(request,productParameters.getPartNumber());
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
	
public List<LogoCustomizationModule> getLogoCustomizationDetails(Integer designId, HttpSession session) {
		
		List<LogoCustomizationModule> response = new ArrayList<LogoCustomizationModule>();
		try {
			SecureData password=new SecureData();
			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
					.build();
			Cimm2BCResponse<List<CimmCspResponse>>ecommCspResponse= CimmESBServiceUtils.getlogoDetails().getCspItemDesignDetails(request,designId);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.writeValueAsString(ecommCspResponse));
			if(ecommCspResponse!=null && ecommCspResponse.getData() != null && ecommCspResponse.getStatus() != null && ecommCspResponse.getStatus().getCode() == HttpStatus.SC_OK){
				List<CimmCspResponse> cimmresponse = ecommCspResponse.getData();
				for (CimmCspResponse cimmCspResponse : cimmresponse) {
					LogoCustomizationModule logoModule = new LogoCustomizationModule();
					logoModule.setStatus(cimmCspResponse.getExternalStatus());
					if(cimmCspResponse.getFees() != null) {
						CustomizationCharges fees = new CustomizationCharges();
						fees.setArtCost(cimmCspResponse.getFees().getArtCost());
						fees.setDecoratorCost(cimmCspResponse.getFees().getDecoratorCost());
						fees.setSetupCost(cimmCspResponse.getFees().getSetupCost());
						logoModule.setFees(fees);
					}
					if(cimmCspResponse.getSingleQuantityFees() != null) {
						CustomizationCharges fees = new CustomizationCharges();
						fees.setArtCost(cimmCspResponse.getSingleQuantityFees().getArtCost());
						fees.setDecoratorCost(cimmCspResponse.getSingleQuantityFees().getDecoratorCost());
						fees.setSetupCost(cimmCspResponse.getSingleQuantityFees().getSetupCost());
						logoModule.setSingleQuantityFees(fees);
					}
					if(cimmCspResponse.getLineItems() != null && cimmCspResponse.getLineItems().size() > 0) {
						ArrayList<ProductsModel> prodList = new ArrayList<ProductsModel>();
						for (CimmLineItem item : cimmCspResponse.getLineItems()){
							ProductsModel prod = new ProductsModel();
							prod.setPartNumber(item.getPartNumber());
							prod.setQty(CommonUtility.validateInteger(item.getQty()));
							prod.setImageName(item.getImageName());
							prod.setCalculateTax(item.getCalculateTax());
							prod.setRushFlag(item.getRushFlag());
							prod.setDesignId(item.getDesignId()!=null?item.getDesignId():0);
							prodList.add(prod);
						}
						logoModule.setLineItems(prodList);
					}
					logoModule.setJobId(cimmCspResponse.getJobId()!=null?cimmCspResponse.getJobId():0);
					response.add(logoModule);
				}
				System.out.println(response);
			}else {
				response = null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
