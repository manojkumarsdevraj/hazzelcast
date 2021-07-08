package com.erp.service.cimm2bcentral.action;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerPartNumberInformation;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItemHistoryRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItemHistoryResponse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPriceAndAvailability;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPriceBreaks;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralUOM;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralWarehouse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralcpnLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralcustomerPartNumber;
import com.erp.service.cimm2bcentral.utilities.Cimm2BPriceAndAvailabilityRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BcustomerPartNumberRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2bUom;
import com.erp.service.cimm2bcentral.utilities.ProductsUtility;
import com.erp.service.model.ProductManagementModel;
import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsModel;
import com.unilog.products.model.AssociateItemsModel;
import com.unilog.products.model.LineItemsModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

public class ProductManagementAction {

	static final Logger logger = LoggerFactory.getLogger(ProductManagementAction.class);
	public static String getAjaxPriceFromErp(ProductManagementModel priceInquiryInput)
	{
		long startTimer = CommonUtility.startTimeDispaly();
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
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return jsonResponse;
	}

	public static ArrayList<ProductsModel> massProductInquiry(ProductManagementModel priceInquiryInput) {
		long startTimer = CommonUtility.startTimeDispaly();
		ArrayList<ProductsModel> partNumberPriceList = null;
		try{
			HttpSession session = priceInquiryInput.getSession();
			Set<String> allWarehouseCodes = new HashSet<String>();
			LinkedHashMap<String,String> activeWarehouses = null;
			//CustomServiceProvider
			if(CommonUtility.customServiceUtility()!=null) {
				activeWarehouses = CommonUtility.customServiceUtility().redefinedWarehouseList();
			}
		    //CustomServiceProvider
			if(activeWarehouses != null){
				allWarehouseCodes.addAll(activeWarehouses.keySet());
			}else{
				allWarehouseCodes.addAll(CommonDBQuery.getWareHouseList().keySet());
			}
			String warehouseCode = (String)session.getAttribute("wareHouseCode");
			if(CommonUtility.validateString(warehouseCode).length() == 0){
				warehouseCode = CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR");
			}
			logger.info("warehouse code in Product Management Action--"+warehouseCode);
			if(warehouseCode!=null){
			warehouseCode = warehouseCode.replaceFirst("^+(?!$)", "");
			}
			String customerErpId = (String)session.getAttribute("customerId");
			
			boolean directFromManufacturer = false;
			if(session!=null && session.getAttribute("DFMMode")!=null && CommonUtility.validateString(session.getAttribute("DFMMode").toString()).equalsIgnoreCase("Y")){
				directFromManufacturer = true;
			}
	
			//HashMap<String, String> partNumbersFromPartIdentifiers = priceInquiryInput.getAltPartNumbersWithPartNumberRef();
			ArrayList<ProductsModel> products = priceInquiryInput.getPartIdentifier();
	
	
			String GET_PRICE_AND_AVAILABILITY_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_PRICE_AND_AVAILABILITY_API"));
	
			ArrayList<String> partNumbersUniqueId = new ArrayList<String>();
			ArrayList<Cimm2BCentralLineItem> lineItems = new ArrayList<Cimm2BCentralLineItem>();
			LinkedHashMap<String, String> partNumbersFromPartIdentifiers = new LinkedHashMap<String, String>();
			LinkedHashMap<String, Integer> itemIdsFromPartIdentifiers = new LinkedHashMap<String, Integer>();
			int itemQty=1;
			for(ProductsModel productsModel : products){
				Cimm2BCentralLineItem cimm2bCentralLineItem = new Cimm2BCentralLineItem();
				if(CommonUtility.validateString(productsModel.getAltPartNumber1()).length() > 0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRICE_BY_ALT_PART_NUMBER")).equalsIgnoreCase("Y")){
					if(!partNumbersFromPartIdentifiers.containsKey(productsModel.getAltPartNumber1())){
						partNumbersUniqueId.add(productsModel.getAltPartNumber1());
						partNumbersFromPartIdentifiers.put(productsModel.getAltPartNumber1(), productsModel.getPartNumber());
					}
					if(!itemIdsFromPartIdentifiers.containsKey(productsModel.getAltPartNumber1())){
						itemIdsFromPartIdentifiers.put(productsModel.getAltPartNumber1(), productsModel.getItemId());
					}
				}else{
					if(productsModel.getPartNumber()!=null){
					cimm2bCentralLineItem.setPartNumber(productsModel.getPartNumber());
					}
					if(productsModel.getQty()>0){
					cimm2bCentralLineItem.setQty(productsModel.getQty());
					}else {
						cimm2bCentralLineItem.setQty(itemQty);
					}
					if(productsModel.getUom() != null && productsModel.getUom().trim().length() > 0) {
						cimm2bCentralLineItem.setUom(productsModel.getUom());
					}
					//partNumbersUniqueId.add(productsModel.getPartNumber());	
				}
				if(cimm2bCentralLineItem.getPartNumber()!=null){
					if(CommonUtility.customServiceUtility() != null && CommonUtility.customServiceUtility().isRequired(productsModel, 0)) {
						lineItems.add(cimm2bCentralLineItem);
					}
				}	
			}
	
			Cimm2BPriceAndAvailabilityRequest priceAndAvailabilityRequest = new Cimm2BPriceAndAvailabilityRequest();			
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SET_SHIPTOIDBASED_PRICE")).length()>0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SET_SHIPTOIDBASED_PRICE")).equalsIgnoreCase("Y")) {
				if(session!=null && session.getAttribute("selectedshipToIdSx") !="" && session.getAttribute("selectedshipToIdSx") != null) {				
					priceAndAvailabilityRequest.setCustomerERPId(String.valueOf(session.getAttribute("selectedshipToIdSx")));
					}
				else {
					priceAndAvailabilityRequest.setCustomerERPId(customerErpId);
				}
			}
			else {
				priceAndAvailabilityRequest.setCustomerERPId(customerErpId);
			}			
			//priceAndAvailabilityRequest.setPartIdentifiers(partNumbersUniqueId);
			if(session!=null && session.getAttribute("selectedshipToIdSx") != null) {
			priceAndAvailabilityRequest.setShipTo(String.valueOf(session.getAttribute("selectedshipToIdSx")));
			}
			priceAndAvailabilityRequest.setLineItems(lineItems);
			if(CommonUtility.customServiceUtility()!=null) {
				warehouseCode = CommonUtility.customServiceUtility().setPricingDefaultWareHouseCode(warehouseCode);
			}
			priceAndAvailabilityRequest.setPricingWarehouseCode(CommonUtility.validateString(warehouseCode));
			priceAndAvailabilityRequest.setDirectFromManufacturer(directFromManufacturer);
			
			if(!CommonUtility.convertToBoolean(priceInquiryInput.getAllBranchavailabilityRequired())){
				allWarehouseCodes = new HashSet<String>();
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y") &&  priceInquiryInput.getWareHouse() != null && priceInquiryInput.getWareHouse().length() > 0  && priceInquiryInput.getWareHouse().split(",").length > 0){
					for(int i = 0; i < priceInquiryInput.getWareHouse().split(",").length; i++)
					{
						allWarehouseCodes.add(priceInquiryInput.getWareHouse().split(",")[i]);
					}
				}else{
					allWarehouseCodes.add(warehouseCode);
				}
			}
			if(CommonUtility.customServiceUtility()!=null) {
				CommonUtility.customServiceUtility().availWarehouse(warehouseCode,allWarehouseCodes);
			   }
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BULK_INVENTORY_WAREHOUSE_ID")).length()>0 && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BULK_INVENTORY_WAREHOUSE_ID")).equalsIgnoreCase(warehouseCode)){
				allWarehouseCodes.add(CommonDBQuery.getSystemParamtersList().get("BULK_INVENTORY_WAREHOUSE_ID"));
			}
			
			if(CommonUtility.convertToBoolean(CommonDBQuery.getSystemParamtersList().get("GET_FROM_DISTRIBUTION_CENTRE")) && !CommonUtility.convertToBoolean(priceInquiryInput.getAllBranchavailabilityRequired())){
				allWarehouseCodes.add(CommonDBQuery.getSystemParamtersList().get("DISTRIBUTION_CENTRE_ID"));
			}
			
			priceAndAvailabilityRequest.setWarehouseCodes(allWarehouseCodes);
			
			//Written for Mallory. TO enable uom list in P21 API.
			if(CommonUtility.customServiceUtility()!=null) {
				priceAndAvailabilityRequest = CommonUtility.customServiceUtility().priceAndAvailIncludeUomList(priceAndAvailabilityRequest, priceInquiryInput);
			}
			if(session != null && CommonUtility.validateString((String)session.getAttribute("userContractId")).length() > 0) {
				priceAndAvailabilityRequest.setContractUID(CommonUtility.validateString((String)session.getAttribute("userContractId")));
			}
	
	
			//Cimm2BCentralResponseEntity priceResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_PRICE_AND_AVAILABILITY_URL, "POST", priceAndAvailabilityRequest, Cimm2BCentralPriceAndAvailability.class);
			Cimm2BCentralResponseEntity priceResponseEntity = Cimm2BCentralClient.getInstance().getDataObjectWithSession(GET_PRICE_AND_AVAILABILITY_URL, "POST", priceAndAvailabilityRequest, Cimm2BCentralPriceAndAvailability.class,session);
			
			Cimm2BCentralPriceAndAvailability priceAndAvailability = null;
			if(priceResponseEntity!=null && priceResponseEntity.getData() != null &&  priceResponseEntity.getStatus().getCode() == 200 ){
				Map<String, WarehouseModel> wareHousesAsMap = null;
				if(CommonUtility.convertToBoolean(priceInquiryInput.getAllBranchavailabilityRequired()) && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SORT_BY_CLOSEST_LOCATION")).equalsIgnoreCase("Y")) {
					wareHousesAsMap = CommonUtility.getWarehousesAsMap(UsersDAO.getWareHouses());
				}
				priceAndAvailability = (Cimm2BCentralPriceAndAvailability) priceResponseEntity.getData();
				if(priceAndAvailability != null  ){
					partNumberPriceList = new ArrayList<ProductsModel>();
					ArrayList<Cimm2BCentralItem> itemList = priceAndAvailability.getItems();
					LinkedHashMap<String, ProductsModel> branchTeritory = new LinkedHashMap<String, ProductsModel>();
	
					if(itemList != null && itemList.size() > 0){
						for(Cimm2BCentralItem item : itemList ){
							ProductsModel productsModel = new ProductsModel();
	
							if(item.getPricingWarehouse() != null){
								if(CommonUtility.customServiceUtility() != null) {
									CommonUtility.customServiceUtility().checkCurrencyConversion(session,item);//Electrozad Custom Service
								}
								productsModel.setPrice(item.getPricingWarehouse().getCustomerPrice()!=null?item.getPricingWarehouse().getCustomerPrice():0.0);
								if(CommonUtility.customServiceUtility()!=null) {
									CommonUtility.customServiceUtility().getPrice(item,productsModel);
								}
								productsModel.setListPrice(item.getPricingWarehouse().getListPrice()!=null?item.getPricingWarehouse().getListPrice():0.0);
								productsModel.setUom(item.getPricingWarehouse().getUom()!=null?item.getPricingWarehouse().getUom():"");
								if(CommonUtility.customServiceUtility()!=null) {
									CommonUtility.customServiceUtility().setHomeBranchName(productsModel,warehouseCode);
								}
								if(CommonUtility.customServiceUtility()!=null) {
									CommonUtility.customServiceUtility().getUomPackAsUom(item,productsModel);
								}
								productsModel.setUomQuantity(item.getPricingWarehouse().getUomQuantity()>0?item.getPricingWarehouse().getUomQuantity():0);
								if(item.getPricingWarehouse().getQuantityAvailable()!=null){
									productsModel.setQty(item.getPricingWarehouse().getQuantityAvailable());
								}
								productsModel.setQuantity(item.getPricingWarehouse().getQty()!=null?item.getPricingWarehouse().getQty():0);
								productsModel.setImapPrice(item.getPricingWarehouse().getImapPrice()!=null?item.getPricingWarehouse().getImapPrice():0.0);
								productsModel.setImapPrice1(item.getPricingWarehouse().getImapPrice1()!=null?item.getPricingWarehouse().getImapPrice1():0.0);
								productsModel.setPromoPrice(item.getPricingWarehouse().getPromoPrice()!=null?item.getPricingWarehouse().getPromoPrice():0.0);
								productsModel.setSalesQuantity(item.getPricingWarehouse().getSalesQuantity());
								productsModel.setUomPack(item.getPricingWarehouse().getUomPack() != null?item.getPricingWarehouse().getUomPack():"");
								productsModel.setProductCategory(item.getPricingWarehouse().getProductCategory()!= null?item.getPricingWarehouse().getProductCategory():"");
								productsModel.setMinimumOrderQuantity(item.getPricingWarehouse().getMinimumOrderQuantity());
								productsModel.setOrderQuantityInterval(item.getPricingWarehouse().getOrderQuantityInterval());
								productsModel.setProp65(item.getProp65()!=null?item.getProp65():"");
								productsModel.setProp65Agent(item.getProp65Agent()!=null?item.getProp65Agent():"");
								if(item.getPricingWarehouse().getPriceUomQuantity()!=null) {
									productsModel.setUomQtyERP(item.getPricingWarehouse().getPriceUomQuantity()>0?item.getPricingWarehouse().getPriceUomQuantity():1);
								}
								if(item.getPricingWarehouse().getReplacementCost()!=null) {
									productsModel.setReplacementCost(item.getPricingWarehouse().getReplacementCost());
								}
								if(item.getPricingWarehouse().getOtherAmount()!=null) {
									productsModel.setOtherAmount(item.getPricingWarehouse().getOtherAmount());
								}
								if(item.getPricingWarehouse().getExtendedPrice()!=null){
									productsModel.setExtendedPrice(item.getPricingWarehouse().getExtendedPrice());
								}else {
									productsModel.setExtendedPrice(item.getPricingWarehouse().getCustomerPrice()!=null?item.getPricingWarehouse().getCustomerPrice():0.0);
								}
								if(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")!=null && CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK").trim().equalsIgnoreCase("Y") && item.getPricingWarehouse().getPriceBreaks()!=null && item.getPricingWarehouse().getPriceBreaks().size() > 0 && !item.getPricingWarehouse().getPriceBreaks().isEmpty()){
									ArrayList<ProductsModel> quantityBreak = new ArrayList<ProductsModel>();
									for(Cimm2BCentralPriceBreaks priceBreaks: item.getPricingWarehouse().getPriceBreaks() ){
										ProductsModel quantityBreaks = new ProductsModel();
										if(CommonUtility.customServiceUtility()!=null) {
		   									CommonUtility.customServiceUtility().quanitityBreakCalculation(productsModel, priceBreaks);
		   									CommonUtility.customServiceUtility().quanitityBreakCalculationwithDiscount(item,productsModel, priceBreaks);
										}										
										if(priceBreaks.getMaximumQuantity()!=null) {
										quantityBreaks.setMaximumQuantityBreak(priceBreaks.getMaximumQuantity()!=null?priceBreaks.getMaximumQuantity():0.0);}
										quantityBreaks.setMinimumQuantityBreak(priceBreaks.getMinimumQuantity()!=null?priceBreaks.getMinimumQuantity():0.0);
										quantityBreaks.setCustomerPriceBreak(priceBreaks.getCustomerPrice()!=null?priceBreaks.getCustomerPrice():0.0);
										quantityBreaks.setPartNumber(item.getPartIdentifier()!=null?item.getPartIdentifier():item.getPartNumber());
										quantityBreaks.setdDiscountValue(priceBreaks.getDiscountAmount()!=null?priceBreaks.getDiscountAmount():0.0);
										quantityBreak.add(quantityBreaks);
										productsModel.setQuantityBreakList(quantityBreak);
									}
									if(priceInquiryInput.getPartIdentifierQuantity()!=null){
										if(CommonDBQuery.getSystemParamtersList().get("IS_MULTIPLE_QUANTITY_PRODUCTS")!=null && CommonDBQuery.getSystemParamtersList().get("IS_MULTIPLE_QUANTITY_PRODUCTS").trim().equalsIgnoreCase("Y")){
											if(CommonUtility.customServiceUtility()!=null) {
			   									CommonUtility.customServiceUtility().erpQuanitityBreakUpdate(priceInquiryInput,productsModel,quantityBreak);
			   								   }
											}
											else {
									for(int i=quantityBreak.size()-1;i>=0;i--)
		                             {
										logger.info(quantityBreak.get(i).getCustomerPriceBreak() + " : " + quantityBreak.get(i).getMinimumQuantityBreak()+ " : " + quantityBreak.get(i).getMaximumQuantityBreak());
		                            
		                               if(priceInquiryInput.getPartIdentifierQuantity().get(0) >0 && priceInquiryInput.getPartIdentifierQuantity().get(0)>= quantityBreak.get(i).getMinimumQuantityBreak())
		                              {
		                            	   productsModel.setPrice(quantityBreak.get(i).getCustomerPriceBreak());  
		                            	   if(CommonUtility.customServiceUtility()!=null) {
		   									CommonUtility.customServiceUtility().getPrice(item,productsModel);
		   								   }
		                            	   break;
		                              }else{
		                            	  if(CommonUtility.customServiceUtility()!=null) {
		  									CommonUtility.customServiceUtility().getPrice(item,productsModel);
		  								   }
		                            	  productsModel.setPrice(item.getPricingWarehouse().getCustomerPrice());
		                              }
		                             }
											}
								}
								}
								if(CommonUtility.customServiceUtility() != null) {
									CommonUtility.customServiceUtility().setQuantityBreakItemsPrice(item,productsModel,priceInquiryInput);//Electrozad Custom Service																																																																																	
								}
								if(CommonUtility.validateString(item.getPricingWarehouse().getRestrictiveProduct()).length()>0){
									productsModel.setRestrictiveProduct(CommonUtility.validateString(item.getPricingWarehouse().getRestrictiveProduct()));
								}else{
									productsModel.setRestrictiveProduct("N");
								}
									if(item.getPricingWarehouse().getMinimumOrderQuantity() > 0){
										productsModel.setMinimumOrderQuantity(CommonUtility.validateInteger(item.getPricingWarehouse().getMinimumOrderQuantity()));// Double
										productsModel.setMinOrderQty(CommonUtility.validateInteger(item.getPricingWarehouse().getMinimumOrderQuantity()));// Int
									}else if(item.getMinimumOrderQuantity()!=null && item.getMinimumOrderQuantity()>0){
										productsModel.setMinOrderQty(CommonUtility.validateNumber(CommonUtility.validateParseDoubleToString(item.getMinimumOrderQuantity())));// Int
										productsModel.setMinimumOrderQuantity(CommonUtility.validateNumber(CommonUtility.validateParseDoubleToString(item.getMinimumOrderQuantity())));// Double

									}/*else{
										productsModel.setMinOrderQty(1);// Int
										productsModel.setMinimumOrderQuantity(1);
									}*/
									
									if(item.getPricingWarehouse().getOrderQuantityInterval()>0){
										productsModel.setOrderInterval(CommonUtility.validateInteger(item.getPricingWarehouse().getOrderQuantityInterval()));// Int
									}else if(item.getOrderQuantityInterval()!=null && item.getMinimumOrderQuantity()!=null && item.getMinimumOrderQuantity()>0){
										productsModel.setOrderInterval(CommonUtility.validateNumber(CommonUtility.validateParseDoubleToString(item.getOrderQuantityInterval())));// Int
									}/*else{
										productsModel.setOrderInterval(1);// Int
									}*/
									if(CommonUtility.customServiceUtility()!=null) {
										CommonUtility.customServiceUtility().setRoundPrice(item,productsModel);
									}
									if(item.getPricingWarehouse()!=null){
										productsModel.setCimm2BCentralPricingWarehouse(item.getPricingWarehouse());
									}
								
								if(item.getPricingWarehouse().getCustomerPartNumbers() != null && !item.getPricingWarehouse().getCustomerPartNumbers().isEmpty()){
									ArrayList<ProductsModel> customerPartNumberList = new ArrayList<ProductsModel>();
									for (String customerPartNumber : item.getPricingWarehouse().getCustomerPartNumbers()) {
										ProductsModel cpnModel = new ProductsModel();
										cpnModel.setPartNumber(customerPartNumber);
										
										customerPartNumberList.add(cpnModel);
									}
									
									productsModel.setCustomerPartNumberList(customerPartNumberList);
								}
								productsModel.setCurrencyCode(item.getPricingWarehouse().getCurrencyCode());
								productsModel.setSupplierCode(item.getPricingWarehouse().getSupplierCode());
								productsModel.setSupplierSku(item.getPricingWarehouse().getSupplierSku());
								productsModel.setStoreSku(item.getPricingWarehouse().getStoreSku());
								productsModel.setItemDiscontinued(item.getPricingWarehouse().isItemDiscontinued());
								productsModel.setDueDate(item.getPricingWarehouse().getDueDate());
							}
							productsModel.setProductStatus(item.getProductStatus()!=null?item.getProductStatus():"");
							productsModel.setErpPartNumber(item.getPartIdentifier());
							String partNumber = "";
							if(partNumbersFromPartIdentifiers != null && partNumbersFromPartIdentifiers.size() > 0){
								partNumber = partNumbersFromPartIdentifiers.get(item.getPartIdentifier().toUpperCase());
							}else{
								partNumber = item.getPartNumber();
							}
							if(partNumber!=null && partNumber.length()>0) {
							productsModel.setPartNumber(partNumber);
							}else {
								productsModel.setPartNumber(item.getPartIdentifier());
							}
							
							int itemId = 0;
							if(itemIdsFromPartIdentifiers != null && !itemIdsFromPartIdentifiers.isEmpty()){
								itemId = itemIdsFromPartIdentifiers.get(item.getPartIdentifier().toUpperCase());
							}
							productsModel.setItemId(itemId);
	
							if(item.getMinimumOrderQuantity() != null){
								productsModel.setMinOrderQty(item.getMinimumOrderQuantity().intValue());
							}
							if(item.getOrderQuantityInterval() != null){
								productsModel.setOrderInterval(item.getOrderQuantityInterval().intValue());
							}
							if(item.getTotalQtyAvailable()!=null) {
								productsModel.setBranchTotalQty(CommonUtility.validateInteger(item.getTotalQtyAvailable()));
								productsModel.setOtherBranchAvailability(CommonUtility.validateInteger(item.getTotalQtyAvailable()));
							}else {
								productsModel.setBranchTotalQty(item.getPricingWarehouse()!=null && item.getPricingWarehouse().getQuantityAvailable()!=null?item.getPricingWarehouse().getQuantityAvailable().intValue():0);
							}
							ArrayList<Cimm2BCentralWarehouse> wareHouseList = item.getWarehouses();
							ArrayList<ProductsModel> branchAvail = new ArrayList<ProductsModel>();
							int branchTotalQuantity = 0;
							if(wareHouseList!=null){
								for(Cimm2BCentralWarehouse wareHouseDetail : wareHouseList ){
									/*if(CommonUtility.validateString(wareHouseDetail.getWarehouseCode()).equalsIgnoreCase(warehouseCode) && wareHouseDetail.getCustomerPrice() != null){
										productsModel.setPrice(wareHouseDetail.getCustomerPrice());
										productsModel.setUom(wareHouseDetail.getUom());
									}*/
									
									String currentWareHouseCode = wareHouseDetail.getWarehouseCode();
									if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TRIM_LEADING_ZEROS")).equals("Y")) {
										currentWareHouseCode = wareHouseDetail.getWarehouseCode().replaceFirst("^0*", "");
									}
									
									if(CommonDBQuery.getWareHouseList().get(CommonUtility.validateString(currentWareHouseCode)) !=null)
									{
										ProductsModel branchModel = new ProductsModel();
										String branchId = CommonUtility.validateString(wareHouseDetail.getWarehouseCode());
										if(CommonUtility.customServiceUtility()!=null) {
											CommonUtility.customServiceUtility().setWareHousePrice(wareHouseDetail,productsModel);
										}
										branchModel.setPrice(wareHouseDetail.getCustomerPrice()!=null?wareHouseDetail.getCustomerPrice():0.0);
										if(CommonUtility.customServiceUtility()!=null) {
											CommonUtility.customServiceUtility().getPrice(item,branchModel);
										}
										branchModel.setPromoPrice(wareHouseDetail.getPromoPrice()!=null?wareHouseDetail.getPromoPrice():0.0);
										branchModel.setImapPrice(wareHouseDetail.getImapPrice()!=null?wareHouseDetail.getImapPrice():0.0);
										branchModel.setImapPrice1(wareHouseDetail.getImapPrice1()!=null?wareHouseDetail.getImapPrice1():0.0);
										branchModel.setUom(wareHouseDetail.getUom()!=null?wareHouseDetail.getUom():"");
										branchModel.setBranchID(branchId);
										
										boolean wareHouseCustomerPrice= true;
										if(CommonUtility.customServiceUtility()!=null) {
											wareHouseCustomerPrice = CommonUtility.customServiceUtility().verifyingWareHouse(warehouseCode,currentWareHouseCode, wareHouseCustomerPrice);
										}
										
										if(wareHouseDetail.getCustomerPrice() != null && wareHouseDetail.getCustomerPrice() > 0.0 && wareHouseCustomerPrice) {
											productsModel.setPrice(wareHouseDetail.getCustomerPrice());
										}
										productsModel.setUom(wareHouseDetail.getUom()!=null?wareHouseDetail.getUom():"");
										if(CommonUtility.customServiceUtility()!=null) {
											CommonUtility.customServiceUtility().getUomPackAsUom(item,productsModel);
										}
										if(wareHouseDetail.getUomList()!=null && wareHouseDetail.getUomList().size()>0) {
											ArrayList<Cimm2BCentralUOM> uomLists = wareHouseDetail.getUomList();
											ArrayList<ProductsModel> uomList = new ArrayList<ProductsModel>();
											for(Cimm2BCentralUOM uomListDetail : uomLists ){
												ProductsModel uomListdata = new ProductsModel();
												uomListdata.setUom(uomListDetail.getUom());														
												uomListdata.setUomQty(uomListDetail.getUomQuantity());
												uomListdata.setCustomerPrice(uomListDetail.getCustomerPrice());
												uomListdata.setBranchTotalQty(CommonUtility.validateInteger(uomListDetail.getAllBranchAvailability()));
												uomListdata.setQty(CommonUtility.validateInteger(uomListDetail.getQuantityAvailable()));
												uomList.add(uomListdata);
											}	
											branchModel.setUomList(uomList);
											if(CommonUtility.validateString(currentWareHouseCode).equalsIgnoreCase(warehouseCode)) {
											productsModel.setUomList(uomList);
										}
										
										branchModel.setUom(wareHouseDetail.getUom()!=null?wareHouseDetail.getUom():"");
										branchModel.setUomQty(wareHouseDetail.getUomQuantity());										
										if(CommonUtility.validateString(currentWareHouseCode).equalsIgnoreCase(warehouseCode)) {
										productsModel.setUomQty(wareHouseDetail.getUomQuantity());
											if(CommonUtility.customServiceUtility()!=null) {
												CommonUtility.customServiceUtility().setAvailQty(warehouseCode,wareHouseDetail,productsModel);
											}
										}
										}
										if(CommonDBQuery.branchDetailData.get(branchId)!=null){
											branchModel.setBranchName(CommonDBQuery.branchDetailData.get(branchId).getBranchName());
										}
										if(branchModel.getBranchName() == null) {
											branchModel.setBranchName(CommonDBQuery.getWareHouseList().get(currentWareHouseCode));
										}
										branchModel.setPartNumber(item.getPartNumber());
										if(wareHouseDetail.getEarliestMoreQuantity()!=null){
									          branchModel.setEarliestMoreQuantity(wareHouseDetail.getEarliestMoreQuantity());
									    }
										if(CommonUtility.convertToBoolean(priceInquiryInput.getAllBranchavailabilityRequired()) && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SORT_BY_CLOSEST_LOCATION")).equalsIgnoreCase("Y")) {
											WarehouseModel wareHouseLocation = wareHousesAsMap.get(currentWareHouseCode);
											if(wareHouseLocation != null) {
												branchModel.setLatitude(Double.parseDouble(wareHouseLocation.getLatitude()));
												branchModel.setLongitude(Double.parseDouble(wareHouseLocation.getLongitude()));
												branchModel.setBranchAddress1(wareHouseLocation.getAddress1());
												branchModel.setBranchAddress2(wareHouseLocation.getAddress2());
												branchModel.setBranchCity(wareHouseLocation.getCity());
												branchModel.setBranchState(wareHouseLocation.getState());
												branchModel.setBranchPostalCode(wareHouseLocation.getZip());
											}
										}
										branchModel.setBranchAvailability(CommonUtility.validateParseIntegerToString(wareHouseDetail.getQuantityAvailable()));
										if(wareHouseDetail.getShipMethods() != null && wareHouseDetail.getShipMethods().size() > 0){
											branchModel.setShipMethodQuantity(wareHouseDetail.getShipMethods());
										}
										branchModel.setSupplierCode(wareHouseDetail.getSupplierCode());
										branchModel.setSupplierSku(wareHouseDetail.getSupplierSku());
										branchModel.setStoreSku(wareHouseDetail.getStoreSku());
										branchModel.setItemDiscontinued(wareHouseDetail.isItemDiscontinued());
										branchAvail.add(branchModel);
										//branchTeritory.put(wareHouseDetail.getWarehouseCode(), branchModel);
										if(CommonUtility.validateString(currentWareHouseCode).equalsIgnoreCase(warehouseCode)){
											branchTotalQuantity += wareHouseDetail.getQuantityAvailable() != null ? wareHouseDetail.getQuantityAvailable() : 0;
											productsModel.setPrice(wareHouseDetail.getCustomerPrice()!=null?wareHouseDetail.getCustomerPrice():0.0);
											if(CommonUtility.customServiceUtility() != null) {
												CommonUtility.customServiceUtility().addExchangeRateToQuantityBreakItems(item,productsModel,wareHouseDetail);//Electrozad Custom Service
											}
											if(CommonUtility.customServiceUtility()!=null) {
												CommonUtility.customServiceUtility().getPrice(item,productsModel);
											}
											productsModel.setAvailQty(wareHouseDetail.getOtherBranchAvailability());
											if(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")!=null && CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK").trim().equalsIgnoreCase("Y") && item.getPricingWarehouse().getPriceBreaks()!=null && item.getPricingWarehouse().getPriceBreaks().size() > 0 && !item.getPricingWarehouse().getPriceBreaks().isEmpty()){
												ArrayList<ProductsModel> quantityBreak = new ArrayList<ProductsModel>();
												for(Cimm2BCentralPriceBreaks priceBreaks: item.getPricingWarehouse().getPriceBreaks() ){
													ProductsModel quantityBreaks = new ProductsModel();
													if(priceBreaks.getMaximumQuantity()!=null) {
														quantityBreaks.setMaximumQuantityBreak(priceBreaks.getMaximumQuantity()!=null?priceBreaks.getMaximumQuantity():0.0);}
													quantityBreaks.setMinimumQuantityBreak(priceBreaks.getMinimumQuantity()!=null?priceBreaks.getMinimumQuantity():0.0);
													quantityBreaks.setCustomerPriceBreak(priceBreaks.getCustomerPrice()!=null?priceBreaks.getCustomerPrice():0.0);
													quantityBreaks.setPartNumber(item.getPartIdentifier()!=null?item.getPartIdentifier():item.getPartNumber());
													quantityBreaks.setdDiscountValue(priceBreaks.getDiscountAmount());
													quantityBreak.add(quantityBreaks);
													productsModel.setQuantityBreakList(quantityBreak);
												}
												if(priceInquiryInput.getPartIdentifierQuantity()!=null){
													if(CommonDBQuery.getSystemParamtersList().get("IS_MULTIPLE_QUANTITY_PRODUCTS")!=null && CommonDBQuery.getSystemParamtersList().get("IS_MULTIPLE_QUANTITY_PRODUCTS").trim().equalsIgnoreCase("Y")){
														if(CommonUtility.customServiceUtility()!=null) {
						   									CommonUtility.customServiceUtility().erpQuanitityBreakUpdate(priceInquiryInput,productsModel,quantityBreak);
						   								   }
														}
														else {
												for(int i=quantityBreak.size()-1;i>=0;i--)
					                             {
													logger.info(quantityBreak.get(i).getCustomerPriceBreak() + " : " + quantityBreak.get(i).getMinimumQuantityBreak()+ " : " + quantityBreak.get(i).getMaximumQuantityBreak());
					                            
					                               if(priceInquiryInput.getPartIdentifierQuantity().get(0) >0 && priceInquiryInput.getPartIdentifierQuantity().get(0)>= quantityBreak.get(i).getMinimumQuantityBreak())
					                              {
					                            	   productsModel.setPrice(quantityBreak.get(i).getCustomerPriceBreak());  
					                            	   if(CommonUtility.customServiceUtility()!=null) {
					   									CommonUtility.customServiceUtility().getPrice(item,productsModel);
					   								   }
					                            	   break;
					                              }else{
					                            	  if(CommonUtility.customServiceUtility()!=null) {
					  									CommonUtility.customServiceUtility().getPrice(item,productsModel);
					  								   }
					                            	  productsModel.setPrice(item.getPricingWarehouse().getCustomerPrice());
					                              }
					                             }
														}
											}
											}
											if(item.getTotalQtyAvailable()!=null) {
												productsModel.setBranchTotalQty(CommonUtility.validateInteger(item.getTotalQtyAvailable()));
											}else {
											productsModel.setBranchTotalQty(wareHouseDetail.getQuantityAvailable()!=null?wareHouseDetail.getQuantityAvailable().intValue():0);
											}
											if(CommonUtility.customServiceUtility()!=null) {
												CommonUtility.customServiceUtility().availQtyUpdate(warehouseCode,wareHouseDetail,productsModel,item,CommonUtility.validateString(currentWareHouseCode));
											}
											if(wareHouseDetail.getShipMethods() != null && wareHouseDetail.getShipMethods().size() > 0){
												productsModel.setShipMethodQuantity(wareHouseDetail.getShipMethods());
											}
										}
									}
									if(CommonUtility.validateString(wareHouseDetail.getErrorMessage()).length()>1){
										productsModel.setWarehouseErrorMsg(wareHouseDetail.getErrorMessage());
									}
								}
								if(CommonUtility.customServiceUtility() != null) {
									CommonUtility.customServiceUtility().setQuantityBreakItemsPrice(item,productsModel,priceInquiryInput);//Electrozad Custom Service
								}
								if(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")!=null && CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK").trim().equalsIgnoreCase("Y") && item.getPricingWarehouse().getPriceBreaks()!=null && item.getPricingWarehouse().getPriceBreaks().size() > 0 && !item.getPricingWarehouse().getPriceBreaks().isEmpty()){
									ArrayList<ProductsModel> quantityBreak = new ArrayList<ProductsModel>();
									for(Cimm2BCentralPriceBreaks priceBreaks: item.getPricingWarehouse().getPriceBreaks() ){
										ProductsModel quantityBreaks = new ProductsModel();
										if(priceBreaks.getMaximumQuantity()!=null) {
											quantityBreaks.setMaximumQuantityBreak(priceBreaks.getMaximumQuantity()!=null?priceBreaks.getMaximumQuantity():0.0);}
										quantityBreaks.setMinimumQuantityBreak(priceBreaks.getMinimumQuantity()!=null?priceBreaks.getMinimumQuantity():0.0);
										quantityBreaks.setCustomerPriceBreak(priceBreaks.getCustomerPrice()!=null?priceBreaks.getCustomerPrice():0.0);
										quantityBreaks.setPartNumber(item.getPartIdentifier());
										quantityBreak.add(quantityBreaks);
										productsModel.setQuantityBreakList(quantityBreak);
									}
									if(priceInquiryInput.getPartIdentifierQuantity()!=null){
										if(CommonDBQuery.getSystemParamtersList().get("IS_MULTIPLE_QUANTITY_PRODUCTS")!=null && CommonDBQuery.getSystemParamtersList().get("IS_MULTIPLE_QUANTITY_PRODUCTS").trim().equalsIgnoreCase("Y")){
											if(CommonUtility.customServiceUtility()!=null) {
			   									CommonUtility.customServiceUtility().erpQuanitityBreakUpdate(priceInquiryInput,productsModel,quantityBreak);
			   								   }
											}
											else {
												
												if(priceInquiryInput.getPartIdentifierQuantity()!=null){
													for(int j=priceInquiryInput.getPartIdentifierQuantity().size()-1;j>=0;j--) {
														for(int i=quantityBreak.size()-1;i>=0;i--)
														 {
															if(priceInquiryInput.getPartIdentifier().get(j).getPartNumber().equals(quantityBreak.get(i).getPartNumber()) && priceInquiryInput.getPartIdentifierQuantity().get(j) >0 && priceInquiryInput.getPartIdentifierQuantity().get(j)>= quantityBreak.get(i).getMinimumQuantityBreak())
															{
																System.out.println(quantityBreak.get(i).getCustomerPriceBreak() + " : " + quantityBreak.get(i).getMinimumQuantityBreak()+ " : " + quantityBreak.get(i).getMaximumQuantityBreak());
																double price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(quantityBreak.get(i).getCustomerPriceBreak()));
																productsModel.setPrice(price); 
																break;
															}
															else{
								                            	  if(CommonUtility.customServiceUtility()!=null) {
								  									CommonUtility.customServiceUtility().getPrice(item,productsModel);
								  								   }
								                            	  productsModel.setPrice(item.getPricingWarehouse().getCustomerPrice());
								                              }
														 }
													}
												}
									
											}
								}
								}
							}
							if(CommonDBQuery.getSystemParamtersList().get("PRODUCTUNITOFMEASURES")!=null && CommonDBQuery.getSystemParamtersList().get("PRODUCTUNITOFMEASURES").length()>0){
							String AVAILABILITY_URL =CommonDBQuery.getSystemParamtersList().get("PRODUCTUNITOFMEASURES")+productsModel.getPartNumber();
							
							Cimm2BCentralResponseEntity qtyResponse=Cimm2BCentralClient.getInstance().getDataObject(AVAILABILITY_URL, "GET", null, Cimm2bUom.class);
							Cimm2bUom uomQtyValue = null;
							if(qtyResponse!=null && qtyResponse.getData() != null &&  qtyResponse.getStatus().getCode() == 200 ){
								uomQtyValue = (Cimm2bUom) qtyResponse.getData();
								ArrayList<Cimm2BCentralUOM> uomList = uomQtyValue.getDetails();
								if(uomList != null && uomList.size() > 0){
									
									for(int i=0;i<uomList.size();i++) {
										
										if(uomList.get(i).getUom().equalsIgnoreCase(productsModel.getUom())) {
											productsModel.setUomQty(CommonUtility.validateNumber(uomList.get(i).getUomQty()));
										break;
									}
									}
								}
							}
							}
							if((productsModel.getUomList() == null || productsModel.getUomList().size() == 0) && item.getPricingWarehouse()!=null && item.getPricingWarehouse().getUomList() != null && item.getPricingWarehouse().getUomList().size() > 0) {
								ArrayList<Cimm2BCentralUOM> uomLists = item.getPricingWarehouse().getUomList();
								ArrayList<ProductsModel> uomList = new ArrayList<ProductsModel>();
								for(Cimm2BCentralUOM uomListDetail : uomLists ){
									ProductsModel uomListdata = new ProductsModel();
									uomListdata.setUom(uomListDetail.getUom());														
									uomListdata.setUomQty(uomListDetail.getUomQuantity());
									uomListdata.setCustomerPrice(uomListDetail.getCustomerPrice());
									uomListdata.setBranchTotalQty(CommonUtility.validateInteger(uomListDetail.getAllBranchAvailability()));
									uomListdata.setQty(CommonUtility.validateInteger(uomListDetail.getQuantityAvailable()));
									uomList.add(uomListdata);
									if(CommonUtility.validateString(item.getPricingWarehouse().getUom()).equalsIgnoreCase(uomListDetail.getUom())) {
										productsModel.setUomQuantity(uomListDetail.getUomQuantity());
										if(productsModel.getCimm2BCentralPricingWarehouse() != null) {
											productsModel.getCimm2BCentralPricingWarehouse().setOtherBranchAvailability(CommonUtility.validateInteger(uomListDetail.getAllBranchAvailability()));
										}
									}
								}	
								productsModel.setUomList(uomList);						
							}
							if(CommonUtility.customServiceUtility()!=null) {
								CommonUtility.customServiceUtility().getItemLevelPrice(item,productsModel);
							}
							if(CommonUtility.validateString(priceInquiryInput.getZipCode()).length()> 0 && CommonUtility.convertToBoolean(priceInquiryInput.getAllBranchavailabilityRequired()) && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SORT_BY_CLOSEST_LOCATION")).equalsIgnoreCase("Y") ) {
								CommonUtility.sortBasedOnClosestLocation(branchAvail, priceInquiryInput.getZipCode());
							}else {
								Collections.sort(branchAvail, ProductsModel.branchAscendingComparator);
							}
							productsModel.setBranchTotalQty(branchTotalQuantity);
							productsModel.setBranchTeritory(branchTeritory);
							productsModel.setBranchAvail(branchAvail);
							productsModel.setProp65(item.getProp65()!=null?item.getProp65():"");
							productsModel.setProp65Agent(item.getProp65Agent()!=null?item.getProp65Agent():"");
							productsModel.setWeight(item.getItemWeight());
							partNumberPriceList.add(productsModel);
						}
					}
				}
			}else{
				partNumberPriceList = new ArrayList<ProductsModel>();
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return partNumberPriceList;
	}

	public static ArrayList<ProductsModel> getPriceFromERP(ProductManagementModel priceInquiryInput,ArrayList<ProductsModel> productList){
		ArrayList<ProductsModel> productPriceOutput = null;
		DecimalFormat df = CommonUtility.getPricePrecision(priceInquiryInput.getSession());
		HttpSession session = priceInquiryInput.getSession();
		String entityID = "0";
		if(session!=null && (String)session.getAttribute("entityId")!=null) {
			entityID= (String)session.getAttribute("entityId");
		}
		//DecimalFormat df1 = new DecimalFormat("####0.#####");

		try
		{
			productPriceOutput = massProductInquiry(priceInquiryInput);
			double total = 0;
			double uomBasedPrice = 0;
			boolean uomBasedPriceFlag = false;
			boolean quantityBreakFlag = false;			

			if(productList!=null && productList.size()>0){
				for(ProductsModel itemPrice : productList){
					double price = 0;
					double priceFromEcommerce = itemPrice.getPrice();
					double unitPriceFromEcommerce = itemPrice.getUnitPrice();
					int i = -1;
					if(itemPrice!=null){
						if(productPriceOutput!=null && productPriceOutput.size()>0){
							for(ProductsModel eclipseitemPrice : productPriceOutput){
								i++;
								if(itemPrice.getPartNumber().equals(eclipseitemPrice.getPartNumber().trim())){
									productPriceOutput.remove(i);
									//itemPrice.setUom(eclipseitemPrice.getUom());
									/**
									 *Below code Written is for Turtle and Hughes to get Custom UOM *Reference- Nitesh
									 */
									if(CommonUtility.customServiceUtility()!=null) {
										String uom = CommonUtility.customServiceUtility().getUom(eclipseitemPrice.getSalesQuantity());
										if(CommonUtility.validateString(uom).trim().length() > 0) {
											itemPrice.setUom(uom);
										}
									}	
										boolean uomFlag = false;
										if(eclipseitemPrice.getUomList()!=null && eclipseitemPrice.getUomList().size()>0){
											for(ProductsModel uomListModel : eclipseitemPrice.getUomList()){
												if(CommonUtility.validateString(itemPrice.getUom()).equalsIgnoreCase(CommonUtility.validateString(uomListModel.getUom()))){
													uomFlag = true;
													itemPrice.setUomQty(uomListModel.getUomQty());												
													itemPrice.setQtyUOM(uomListModel.getUom());	
													if(uomListModel.getCustomerPrice() > 0.0) {
														itemPrice.setUomPrice(uomListModel.getCustomerPrice());
													}
													break;
												}
											}
										}
										if(!uomFlag){
											itemPrice.setUom(eclipseitemPrice.getUom());											
											itemPrice.setQtyUOM(eclipseitemPrice.getUom());
											itemPrice.setUomQty(eclipseitemPrice.getUomQty());											
										}
										if(	eclipseitemPrice.getUomQuantity() == 0) {
											eclipseitemPrice.setUomQuantity(1);
											itemPrice.setUomQuantity(eclipseitemPrice.getUomQuantity());
										}
										else {
											itemPrice.setUomQuantity(eclipseitemPrice.getUomQuantity());
										}
										
									if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CALCULATE_UOM_BASED_PRICE")).equalsIgnoreCase("Y") && uomFlag){
										uomBasedPrice = 0;
										uomBasedPrice = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString((eclipseitemPrice.getExtendedPrice()/eclipseitemPrice.getUomQuantity()) * (itemPrice.getUomQty())));
										
										if(itemPrice.getUomQty()>0){
											uomBasedPriceFlag = true;
										}else{
											uomBasedPriceFlag = false;
										}
										if(uomBasedPrice == 0.0 && itemPrice.getUomPrice() > 0.0)
										{
											uomBasedPrice = itemPrice.getUomPrice();
											uomBasedPriceFlag = true;
										}
										//eclipseitemPrice.setExtendedPrice(uomBasedPrice);
									}
									if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CALCULATE_UOM_BASED_PRICE")).equalsIgnoreCase("Y") && !uomFlag){
										uomBasedPrice = 0;
										uomBasedPrice = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString((eclipseitemPrice.getExtendedPrice()/eclipseitemPrice.getUomQuantity()) * (itemPrice.getUomQty())));
										
										if(itemPrice.getUomQty()>0){
											uomBasedPriceFlag = true;
										}else{
											uomBasedPriceFlag = false;
										}
										//eclipseitemPrice.setExtendedPrice(uomBasedPrice);
									}
									itemPrice.setBranchAvail(eclipseitemPrice.getBranchAvail());
									itemPrice.setBranchTotalQty(eclipseitemPrice.getBranchTotalQty());
									itemPrice.setAvailQty(eclipseitemPrice.getBranchTotalQty()); //itemPrice.setAvailQty(eclipseitemPrice.getAvailQty()); // value Changed while working on werner Redesign
									itemPrice.setRestrictiveProduct(CommonUtility.validateString(eclipseitemPrice.getRestrictiveProduct()));
									itemPrice.setShipMethodQuantity(eclipseitemPrice.getShipMethodQuantity());
									itemPrice.setPromoPrice(eclipseitemPrice.getPromoPrice());
									itemPrice.setSalesQuantity(eclipseitemPrice.getSalesQuantity());
									itemPrice.setUomPack(eclipseitemPrice.getUomPack());
									itemPrice.setProductCategory(eclipseitemPrice.getProductCategory());
									itemPrice.setMinimumOrderQuantity(eclipseitemPrice.getMinimumOrderQuantity());
									itemPrice.setUomQtyERP(eclipseitemPrice.getUomQtyERP());
									itemPrice.setDueDate(CommonUtility.validateString(eclipseitemPrice.getDueDate()));
									itemPrice.setCimm2BCentralPricingWarehouse(eclipseitemPrice.getCimm2BCentralPricingWarehouse());
									if(eclipseitemPrice.getOrderQuantityInterval()>0) {
									itemPrice.setOrderQuantityInterval(eclipseitemPrice.getOrderQuantityInterval());
									}else{
										itemPrice.setOrderQuantityInterval(itemPrice.getOrderInterval());
									}
									if(eclipseitemPrice.getMinOrderQty()>0){
										itemPrice.setMinOrderQty(eclipseitemPrice.getMinOrderQty());
									}else{
										itemPrice.setMinOrderQty(itemPrice.getMinOrderQty());
									}
									if(eclipseitemPrice.getMinOrderQty()>0){
										itemPrice.setOrderInterval(eclipseitemPrice.getOrderInterval());
									}else{
										itemPrice.setOrderInterval(itemPrice.getOrderInterval());
									}
									if(eclipseitemPrice.getMinOrderQty()>0){
										itemPrice.setMinimumOrderQuantity(eclipseitemPrice.getMinimumOrderQuantity());
									}else{
										itemPrice.setMinimumOrderQuantity(itemPrice.getMinOrderQty());
									}
									if(eclipseitemPrice.getMinOrderQty()>0){
										itemPrice.setMinimumOrderInterval(eclipseitemPrice.getMinimumOrderInterval());
									}else{
										itemPrice.setMinimumOrderInterval(itemPrice.getMinOrderQty());
									}
									itemPrice.setCurrencyCode(eclipseitemPrice.getCurrencyCode());
									itemPrice.setProductStatus(eclipseitemPrice.getProductStatus()!=null?eclipseitemPrice.getProductStatus():"");
									itemPrice.setWarehouseErrorMsg(eclipseitemPrice.getWarehouseErrorMsg());
									if(CommonUtility.customServiceUtility()!=null) {
										CommonUtility.customServiceUtility().setAdditionalInfo(itemPrice,eclipseitemPrice);
									}
										//--Override ERP Price from CIMM Net Price
										if(!CommonUtility.validateString(itemPrice.getOverRidePriceRule()).equalsIgnoreCase("Y")){
											CommonUtility.customServiceUtility().erpQuanitityBreakUpdate(itemPrice, eclipseitemPrice);
											if(itemPrice.getUomQty()>0) {
												/*double chekPrice=df.format(eclipseitemPrice.getPrice());*/
													eclipseitemPrice.setPrice((eclipseitemPrice.getPrice()*itemPrice.getUomQty()));
												}
												else {
													itemPrice.setPrice(eclipseitemPrice.getPrice());
												}
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
												itemPrice.setUnitPrice(eclipseitemPrice.getPrice()>0?eclipseitemPrice.getPrice():eclipseitemPrice.getListPrice());
												if(CommonUtility.customServiceUtility()!=null) {
													CommonUtility.customServiceUtility().getUnitPrice(eclipseitemPrice,itemPrice);
												}
												if(CommonUtility.customServiceUtility()!=null) {
													price=CommonUtility.customServiceUtility().getdefaultCustomerPrice(eclipseitemPrice,itemPrice,price);
												}
												if(price == 0) {
													price = eclipseitemPrice.getPrice();
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
												}
												if(CommonUtility.customServiceUtility()!=null) {
													price = CommonUtility.customServiceUtility().getPriceCustomService(price,eclipseitemPrice,false);
												}
												break;
											}
										}else {
											price = price + itemPrice.getPrice(); //--Override ERP Price from CIMM Net Price
											break;
										}
									//--Override ERP Price from CIMM Net Price
								}
							}
						}
						if(uomBasedPriceFlag && !quantityBreakFlag){
							itemPrice.setTotal(uomBasedPrice * itemPrice.getQty());
							System.out.println("Uombased Decimal Price : " + uomBasedPrice * itemPrice.getQty());
							String twoDecimalTotal = df.format(itemPrice.getTotal());
							total = total + Double.parseDouble(twoDecimalTotal);
						}else{
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
	
	public static AssociateItemsModel associateItems(ProductManagementModel associateInputItems) {
		AssociateItemsModel associateItemslist = new AssociateItemsModel();
		try {
			String warehouseCode = associateInputItems.getWareHouse();
			String customerErpId = associateInputItems.getCustomerId();
			String partnumber = associateInputItems.getProductNumber();
			String recordType = associateInputItems.getRecordType();
			String getAssociateItemsURL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ASSOCIATE_ITEMS_URL"));
			if(CommonUtility.validateString(recordType).equalsIgnoreCase("Y"))
			{
				Boolean includeAssociateItems = true;
				getAssociateItemsURL = getAssociateItemsURL + "?" + "customerERPId=" + customerErpId + "&warehouseCode=" + warehouseCode + "&" + "partNumber=" + partnumber + "&includeAssociateItems=" + includeAssociateItems;
			}
			else if(CommonUtility.validateString(recordType).equalsIgnoreCase("N"))
			{
				Boolean includeSubstituteItems = true;
				getAssociateItemsURL = getAssociateItemsURL + "?" + "customerERPId=" + customerErpId + "&warehouseCode=" + warehouseCode + "&" + "partNumber=" + partnumber + "&includeSubstituteItems=" + includeSubstituteItems;
			}
			else {
			getAssociateItemsURL = getAssociateItemsURL + "?" + "customerERPId=" + customerErpId + "&warehouseCode=" + warehouseCode + "&" + "partNumber=" + partnumber + "&recordType=" + recordType;
			}
			Cimm2BCentralResponseEntity associateItemsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(getAssociateItemsURL, "GET", null, Cimm2BCAssociateItemsResponse.class);
			Cimm2BCAssociateItemsResponse associateItemsInformation = null;			
			if(associateItemsResponseEntity != null && associateItemsResponseEntity.getData() != null) {
				associateItemsInformation = (Cimm2BCAssociateItemsResponse) associateItemsResponseEntity.getData();
				if(associateItemsInformation != null) {
					associateItemslist.setCustomerERPId(associateItemsInformation.getCustomerERPId());
					if(associateItemsInformation.getCustomerTemplateOverride() != null) {
						associateItemslist.setCustomerTemplateOverride(associateItemsInformation.getCustomerTemplateOverride());
					}
					if(associateItemsInformation.getCustomerTypeFlag() != null) {
						associateItemslist.setCustomerTypeFlag(associateItemsInformation.getCustomerTypeFlag());
					}
					
					if(associateItemsInformation.getLineItems() != null && !associateItemsInformation.getLineItems().isEmpty()) {
						List<Cimm2BCentralLineItem> cimm2BCLineItemList = associateItemsInformation.getLineItems();
						List<LineItemsModel> lineItemList = new ArrayList<>();
						for(Cimm2BCentralLineItem cimm2BCLineItem : cimm2BCLineItemList) {
							LineItemsModel lineItem = new LineItemsModel();
							lineItem.setCalculateTax(cimm2BCLineItem.getCalculateTax());
							lineItem.setCustomerPartNumber(cimm2BCLineItem.getCustomerPartNumber());
							lineItem.setNonStockFlag(cimm2BCLineItem.getNonStockFlag());
							lineItem.setPartNumber(cimm2BCLineItem.getPartNumber());
							lineItem.setPrintPriceFlag(cimm2BCLineItem.getPrintPriceFlag());
							lineItem.setQuantityAvailable(cimm2BCLineItem.getQuantityAvailable());
							lineItem.setRecordType(cimm2BCLineItem.getRecordType());
							lineItem.setRecordTypeDesc(cimm2BCLineItem.getRecordTypeDesc());
							lineItemList.add(lineItem);
						}
						associateItemslist.setLineItems(lineItemList);
					}else if(associateItemsInformation.getSubstituteItems() != null && !associateItemsInformation.getSubstituteItems().isEmpty()) {
						List<Cimm2BCentralLineItem> cimm2BCSubstituteItemList = associateItemsInformation.getSubstituteItems();
						List<LineItemsModel> lineItemList = new ArrayList<>();
						for(Cimm2BCentralLineItem cimm2BCSubstituteItem : cimm2BCSubstituteItemList) {
							LineItemsModel lineItem = new LineItemsModel();
							lineItem.setPartNumber(cimm2BCSubstituteItem.getPartNumber());
							lineItem.setShortDescription(cimm2BCSubstituteItem.getShortDescription());
							lineItem.setNetPrice(cimm2BCSubstituteItem.getNetPrice());
							lineItemList.add(lineItem);
						} 
						associateItemslist.setLineItems(lineItemList);
					}else if(associateItemsInformation.getAssociateItems() != null && !associateItemsInformation.getAssociateItems().isEmpty()) {
						List<Cimm2BCentralLineItem> cimm2BCAssociateItemList = associateItemsInformation.getAssociateItems();
						List<LineItemsModel> lineItemList = new ArrayList<>();
						for(Cimm2BCentralLineItem cimm2BCAssociateItem : cimm2BCAssociateItemList) {
							LineItemsModel lineItem = new LineItemsModel();
							lineItem.setPartNumber(cimm2BCAssociateItem.getPartNumber());
							lineItem.setShortDescription(cimm2BCAssociateItem.getShortDescription());
							lineItem.setNetPrice(cimm2BCAssociateItem.getNetPrice());
							lineItemList.add(lineItem);
						}
						associateItemslist.setLineItems(lineItemList);
					}
				}
			}			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return associateItemslist;
	}
	
	public static LinkedHashMap<String, Object> getItemHistoryDetails(ProductsModel productsModel) {
		LinkedHashMap<String, Object> contentObject=new LinkedHashMap<String, Object>();
		try {
			Cimm2BCentralItemHistoryRequest reqParam=new Cimm2BCentralItemHistoryRequest();
			reqParam.setCustomerERPId(CommonUtility.validateString((String)productsModel.getSession().getAttribute("entityId")));
			reqParam.setTextSearch(productsModel.getPartNumber());
			reqParam.setFromMonth(productsModel.getFromMonth());
			reqParam.setFromYear(productsModel.getFromYear());
			reqParam.setToMonth(productsModel.getToMonth());
			reqParam.setToYear(productsModel.getToYear());
			
			String getItemHistoryUrl = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_HISTORY"));
			Cimm2BCentralResponseEntity itemHistoryCimm2BCResponse = Cimm2BCentralClient.getInstance().getDataObject(getItemHistoryUrl, HttpMethod.POST,reqParam,Cimm2BCentralItemHistoryResponse.class);
			Cimm2BCentralItemHistoryResponse itemHistoryResp=null;
			if(itemHistoryCimm2BCResponse!=null && itemHistoryCimm2BCResponse.getData()!=null && itemHistoryCimm2BCResponse.getStatus().getCode()==HttpStatus.SC_OK) {
			 itemHistoryResp=(Cimm2BCentralItemHistoryResponse)itemHistoryCimm2BCResponse.getData();
			}
			contentObject.put("response", itemHistoryResp); 
			
		} catch (Exception e) {
		}
		
		return contentObject;
		
	}

}
