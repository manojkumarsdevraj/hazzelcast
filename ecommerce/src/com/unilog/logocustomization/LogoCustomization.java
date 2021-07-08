package com.unilog.logocustomization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.users.AddressModel;
import com.unilog.utility.CommonUtility;

public class LogoCustomization {
	
	public LinkedHashMap<String, Object> setDesignDetails(ArrayList<ProductsModel> productListData, int userId, int buyingCompanyId, LinkedHashMap<String, Object> contentObject) {
		Set<Integer> designs = new HashSet<Integer>();
		HashMap<Integer,CustomizationCharges> designFees = new HashMap<Integer,CustomizationCharges>();
		double total = (double) contentObject.get("cartTotal");
		try {
			for(ProductsModel item : productListData){
				if(CommonUtility.validateNumber(item.getAdditionalProperties()) > 0) {
					designs.add(CommonUtility.validateNumber(item.getAdditionalProperties()));
					item.setDesignId(CommonUtility.validateNumber(item.getAdditionalProperties()));
				}
			}
			if(designs.size() > 0) {
				List<LogoCustomizationModule> designList = ProductsDAO.getDesignDetails(buyingCompanyId, userId);
				for (int designId : designs) {
					for(LogoCustomizationModule design : designList) {
						if(designId== design.getDesignId()) {
							designFees.put(designId, design.getFees());
							total = total + design.getFees().getArtCost() + design.getFees().getDecoratorCost() + design.getFees().getSetupCost();
							ArrayList<ProductsModel> designItems = design.getLineItems();
							for(ProductsModel designItem : designItems) {
								for(ProductsModel item : productListData){
									if(CommonUtility.validateString(designItem.getPartNumber()).equalsIgnoreCase(CommonUtility.validateString(item.getManufacturerPartNumber())) && item.getDesignId() == designId) {
										item.setImageName(designItem.getImageName());
										item.setImageType("http");
										item.setCalculateTax(designItem.isCalculateTax());
										item.setRushFlag(designItem.getRushFlag());
										item.setLineItemComment(CommonUtility.validateParseIntegerToString(designId));
									}
								}
							}
							if(design.getFees().getArtCost() > 0.0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_ART_COST_ITEM")).length() > 0) {
								ProductsModel designFeeItem = new ProductsModel();
								designFeeItem.setPartNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_ART_COST_ITEM")));
								designFeeItem.setQty(1);
								designFeeItem.setUomQty(1);
								designFeeItem.setUomQuantity(1);
								designFeeItem.setUom("EA");
								designFeeItem.setUnitPrice(design.getFees().getArtCost());
								designFeeItem.setExtendedPrice(design.getFees().getArtCost());
								designFeeItem.setTotal(design.getFees().getArtCost());
								designFeeItem.setDesignId(designId);
								designFeeItem.setLineItemComment(CommonUtility.validateParseIntegerToString(designId));
								designFeeItem.setDesignFees(true);
								productListData.add(designFeeItem);
							}
							if(design.getFees().getDecoratorCost() > 0.0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_DECORATER_COST_ITEM")).length() > 0) {
								ProductsModel designFeeItem = new ProductsModel();
								designFeeItem.setPartNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_DECORATER_COST_ITEM")));
								designFeeItem.setQty(1);
								designFeeItem.setUomQty(1);
								designFeeItem.setUomQuantity(1);
								designFeeItem.setUom("EA");
								designFeeItem.setUnitPrice(design.getFees().getDecoratorCost());
								designFeeItem.setExtendedPrice(design.getFees().getDecoratorCost());
								designFeeItem.setTotal(design.getFees().getDecoratorCost());
								designFeeItem.setDesignId(designId);
								designFeeItem.setLineItemComment(CommonUtility.validateParseIntegerToString(designId));
								designFeeItem.setDesignFees(true);
								productListData.add(designFeeItem);
							}
							if(design.getFees().getSetupCost() > 0.0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_SETUP_COST_ITEM")).length() > 0) {
								ProductsModel designFeeItem = new ProductsModel();
								designFeeItem.setPartNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_SETUP_COST_ITEM")));
								designFeeItem.setQty(1);
								designFeeItem.setUomQty(1);
								designFeeItem.setUomQuantity(1);
								designFeeItem.setUom("EA");
								designFeeItem.setUnitPrice(design.getFees().getSetupCost());
								designFeeItem.setExtendedPrice(design.getFees().getSetupCost());
								designFeeItem.setTotal(design.getFees().getSetupCost());
								designFeeItem.setDesignId(designId);
								designFeeItem.setLineItemComment(CommonUtility.validateParseIntegerToString(designId));
								designFeeItem.setDesignFees(true);
								productListData.add(designFeeItem);
							}
						}
					}
				}
			}
			productListData.get(0).setCartTotal(total);
			contentObject.put("cartSubTotal", total);
			contentObject.put("cartTotal", total);
			contentObject.put("designFees", designFees);
			contentObject.put("productListData", productListData);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return contentObject;
	}
	
	public LinkedHashMap<String, Object> getselectableItemsCustomFields(LinkedHashMap<String, Object> contentObject,LinkedHashMap<String, Object> selectableObject, int subsetId, int generalSubset) {
		try {
			if(selectableObject !=null && selectableObject.get("itemList") != null  ){
				ArrayList<ProductsModel> selectableItem = (ArrayList<ProductsModel>) selectableObject.get("itemList");
				ArrayList<Integer> selectableItemList = new ArrayList<Integer>();
				for(ProductsModel item : selectableItem) {
					selectableItemList.add(item.getItemId());
				}
				LinkedHashMap<Integer, LinkedHashMap<String, Object>> selectableCustomFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(selectableItemList," OR "),"itemid");
				contentObject.put("selectableCustomFieldVal", selectableCustomFieldVal);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return contentObject;
	}
	
	public ArrayList<ProductsModel> setDesignFeesItems(ArrayList<ProductsModel> productListData, int userId, int buyingCompanyId, HttpSession session) {
		Set<Integer> designs = new HashSet<Integer>();
		HashMap<Integer,CustomizationCharges> designFees = new HashMap<Integer,CustomizationCharges>();
		double total = (double) productListData.get(0).getCartTotal();
		try {
			for(ProductsModel item : productListData){
				if(CommonUtility.validateNumber(item.getAdditionalProperties()) > 0) {
					designs.add(CommonUtility.validateNumber(item.getAdditionalProperties()));
					item.setDesignId(CommonUtility.validateNumber(item.getAdditionalProperties()));
				}
			}
			if(designs.size() > 0) {
				List<LogoCustomizationModule> designList = ProductsDAO.getDesignDetails(buyingCompanyId, userId);
				for (int designId : designs) {
					for(LogoCustomizationModule design : designList) {
						if(designId== design.getDesignId()) {
							designFees.put(designId, design.getFees());
							total = total + design.getFees().getArtCost() + design.getFees().getDecoratorCost() + design.getFees().getSetupCost();
							ArrayList<ProductsModel> designItems = design.getLineItems();
							for(ProductsModel designItem : designItems) {
								for(ProductsModel item : productListData){
									if(CommonUtility.validateString(designItem.getPartNumber()).equalsIgnoreCase(CommonUtility.validateString(item.getManufacturerPartNumber())) && item.getDesignId() == designId) {
										item.setImageName(designItem.getImageName());
										item.setImageType("http");
										item.setCalculateTax(designItem.isCalculateTax());
										item.setRushFlag(designItem.getRushFlag());
										item.setLineItemComment(CommonUtility.validateParseIntegerToString(designId));
									}
								}
							}
							if(design.getFees().getArtCost() > 0.0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_ART_COST_ITEM")).length() > 0) {
								ProductsModel designFeeItem = new ProductsModel();
								designFeeItem.setPartNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_ART_COST_ITEM")));
								designFeeItem.setQty(1);
								designFeeItem.setUomQty(1);
								designFeeItem.setUomQuantity(1);
								designFeeItem.setUom("EA");
								designFeeItem.setUnitPrice(design.getFees().getArtCost());
								designFeeItem.setExtendedPrice(design.getFees().getArtCost());
								designFeeItem.setTotal(design.getFees().getArtCost());
								designFeeItem.setDesignId(designId);
								designFeeItem.setLineItemComment(CommonUtility.validateParseIntegerToString(designId));
								designFeeItem.setDesignFees(true);
								productListData.add(designFeeItem);
							}
							if(design.getFees().getDecoratorCost() > 0.0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_DECORATER_COST_ITEM")).length() > 0) {
								ProductsModel designFeeItem = new ProductsModel();
								designFeeItem.setPartNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_DECORATER_COST_ITEM")));
								designFeeItem.setQty(1);
								designFeeItem.setUomQty(1);
								designFeeItem.setUomQuantity(1);
								designFeeItem.setUom("EA");
								designFeeItem.setUnitPrice(design.getFees().getDecoratorCost());
								designFeeItem.setExtendedPrice(design.getFees().getDecoratorCost());
								designFeeItem.setTotal(design.getFees().getDecoratorCost());
								designFeeItem.setDesignId(designId);
								designFeeItem.setLineItemComment(CommonUtility.validateParseIntegerToString(designId));
								designFeeItem.setDesignFees(true);
								productListData.add(designFeeItem);
							}
							if(design.getFees().getSetupCost() > 0.0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_SETUP_COST_ITEM")).length() > 0) {
								ProductsModel designFeeItem = new ProductsModel();
								designFeeItem.setPartNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CSP_SETUP_COST_ITEM")));
								designFeeItem.setQty(1);
								designFeeItem.setUomQty(1);
								designFeeItem.setUomQuantity(1);
								designFeeItem.setUom("EA");
								designFeeItem.setUnitPrice(design.getFees().getSetupCost());
								designFeeItem.setExtendedPrice(design.getFees().getSetupCost());
								designFeeItem.setTotal(design.getFees().getSetupCost());
								designFeeItem.setDesignId(designId);
								designFeeItem.setLineItemComment(CommonUtility.validateParseIntegerToString(designId));
								designFeeItem.setDesignFees(true);
								productListData.add(designFeeItem);
							}
						}
					}
				}
			}
			productListData.get(0).setCartTotal(total);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return productListData;
	}
	
	public LinkedHashMap<String, Object> createCspOrder(ArrayList<ProductsModel> productListData, ArrayList<SalesModel> orderList, int userId, int buyingCompanyId, SalesModel orderDetail, HttpSession session) {
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		Set<Integer> designs = new HashSet<Integer>();
		HashMap<Integer,CustomizationCharges> designFees = new HashMap<Integer,CustomizationCharges>();
		com.erp.service.cimmesb.action.SalesOrderManagementAction createCspOrder = new com.erp.service.cimmesb.action.SalesOrderManagementAction();
		int orderJobId = 0;
		try {
			
			for(ProductsModel item : productListData){
				if(item.getDesignId() > 0) {
					designs.add(item.getDesignId());
					for(SalesModel orderItem : orderList){
						if(item.getPartNumber().equalsIgnoreCase(orderItem.getPartNumber()) && item.getDesignId() == CommonUtility.validateNumber(orderItem.getLineItemComment())) {
							orderItem.setDesignId(item.getDesignId());
							orderItem.setDesignFees(item.isDesignFees());
							orderItem.setImageType(item.getImageType());
							orderItem.setImageName(item.getImageName());
						}
					}
				}
			}
			if(designs.size() > 0) {
				AddressModel billaddress = orderDetail.getBillAddress();
				orderJobId = createCspOrder.createCspOrder(billaddress, orderDetail.getTotal(), designs,orderDetail.getErpOrderNumber(), session);
				List<LogoCustomizationModule> designList = ProductsDAO.getDesignDetails(buyingCompanyId, userId);
				for (int designId : designs) {
					for(LogoCustomizationModule design : designList) {
						designFees.put(designId, design.getFees());
					}
				}
				
				if(orderJobId > 0) {
					ProductsDAO.updateDesignDetails(designs, orderJobId);
				}
			}
			contentObject.put("designFees", designFees);
			contentObject.put("orderList", orderList);
			contentObject.put("jobId", orderJobId);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return contentObject;
	}
}
