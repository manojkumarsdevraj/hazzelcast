package com.unilog.products;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.erp.service.ProductManagement;
import com.erp.service.impl.ProductManagementImpl;
import com.erp.service.model.ProductManagementModel;
import com.interactiveadvisor.epiphany.rpserviceimpl.Rpserviceimpl;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.defaults.Global;
import com.unilog.utility.CommonUtility;

public class ProductsActionUtility {

	public static LinkedHashMap<String, Object> getAlternateItems(HttpSession session, ArrayList<Integer> itemList, String itemPartNumber){
		
		LinkedHashMap<String, Object> productsActionUtilityContentObject = new LinkedHashMap<String, Object>();
		
		ArrayList<ProductsModel> alternativeItems = new ArrayList<ProductsModel>();
		
		try {
			
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);

			ProductManagement productOBJ = new ProductManagementImpl();
			ArrayList<String> erpList = new ArrayList<String>();
			ProductManagementModel alternateItemsRequest = new ProductManagementModel();
			alternateItemsRequest.setEntityId(CommonUtility.validateString((String) session.getAttribute("entityId")));
			alternateItemsRequest.setHomeTerritory((String) session.getAttribute("shipBranchId"));
			alternateItemsRequest.setProductNumber(itemPartNumber);
			alternateItemsRequest.setRequiredAvailabilty("Y");
			alternateItemsRequest.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
			alternateItemsRequest.setUserToken((String)session.getAttribute("userToken"));
			alternateItemsRequest.setCustomerId((String) session.getAttribute("customerId"));
			ArrayList<ProductsModel> alternativeItemsErp = productOBJ.getAlternateItems(alternateItemsRequest);
			
			if(alternativeItemsErp!=null && !alternativeItemsErp.isEmpty()){
				String buildPartNumberForAlternateItems = "";
				String delimit = "";
				
				for(ProductsModel alternativeItemsErpModel : alternativeItemsErp){
					buildPartNumberForAlternateItems = buildPartNumberForAlternateItems+delimit+alternativeItemsErpModel.getPartNumber();
					delimit = " OR ";
				}
				
				if(CommonUtility.validateString(buildPartNumberForAlternateItems).length()>0){
					alternativeItems = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubset, CommonUtility.validateString(buildPartNumberForAlternateItems),0,null,"partnumber");
				}
				
				if(alternativeItems!=null && !alternativeItems.isEmpty()){
					for(ProductsModel alternativeItemsModelOne : alternativeItems){
						for(ProductsModel alternativeItemsERPModel : alternativeItemsErp){
							if(CommonUtility.validateString(alternativeItemsModelOne.getPartNumber()).equalsIgnoreCase(CommonUtility.validateString(alternativeItemsERPModel.getPartNumber()))){
								alternativeItemsModelOne.setRequestType(alternativeItemsERPModel.getRequestType());
								int itemIdAlt = alternativeItemsModelOne.getItemId();
								itemList.add(itemIdAlt);
							}
						}
					}
				}
				
			}
			//alternativeItems = alternativeItemsErp;
			
			productsActionUtilityContentObject.put("alternativeItems", alternativeItems);
			productsActionUtilityContentObject.put("itemList", itemList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productsActionUtilityContentObject;
	}
	
	public static LinkedHashMap<String, Object> getInteractiveAdvisor(HttpSession session, LinkedHashMap<String, Object> contentObject, String itemPartNumber){
		
		try {
			
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			
			ArrayList<ProductsModel> interActiveAdvisorItemList = null;
			ArrayList<ProductsModel> interActiveAdvisorItemDetailsList = null;
			if(itemPartNumber!=null && itemPartNumber.trim().length()>0){
				interActiveAdvisorItemList = Rpserviceimpl.callInterActiveAdvisor(itemPartNumber);
				if(interActiveAdvisorItemList!=null && interActiveAdvisorItemList.size()>0){
					String buildPartNumberForInterActiveAdvoisor = "";
					String delimit = "";
					
					for(ProductsModel interActiveModel : interActiveAdvisorItemList){
						buildPartNumberForInterActiveAdvoisor = buildPartNumberForInterActiveAdvoisor+delimit+interActiveModel.getPartNumber();
						delimit = " OR ";
					}
					
					if(buildPartNumberForInterActiveAdvoisor!=null && buildPartNumberForInterActiveAdvoisor.trim().length()>0){
						interActiveAdvisorItemDetailsList = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubset, buildPartNumberForInterActiveAdvoisor,0,null,"partnumber");//buildPartNumberForInterActiveAdvoisor
						if(interActiveAdvisorItemDetailsList!=null && interActiveAdvisorItemDetailsList.size()>0){
							
							for(ProductsModel interActiveAdvisorItemDetailsListModel : interActiveAdvisorItemDetailsList){
								if(interActiveAdvisorItemDetailsListModel!=null){
									for(ProductsModel interActiveAdvisorItemListModel : interActiveAdvisorItemList){
										if(interActiveAdvisorItemDetailsListModel.getPartNumber().equals(interActiveAdvisorItemListModel.getPartNumber().trim())){
											interActiveAdvisorItemDetailsListModel.setInterActiveOfferPosition(interActiveAdvisorItemListModel.getInterActiveOfferPosition());
										}
									}
								}
						   	}
							
							Collections.sort(interActiveAdvisorItemDetailsList, ProductsModel.interActiveOfferPositionComparator);
							for(ProductsModel interActiveAdvisorItemDetailsListModel : interActiveAdvisorItemDetailsList){
								System.out.println(interActiveAdvisorItemDetailsListModel.getInterActiveOfferPosition()+" : "+interActiveAdvisorItemDetailsListModel.getPartNumber());
							}
							contentObject.put("interActiveAdvisorItemDetailsList", interActiveAdvisorItemDetailsList);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contentObject;
	}
	
	public static LinkedHashMap<String, Object> getCustomerAlsoBoughtItems(HttpSession session, String itemIdString){

		LinkedHashMap<String, Object> productsActionUtilityContentObject = new LinkedHashMap<String, Object>();
		
		try {
			 String tempSubset = (String) session.getAttribute("userSubsetId");
             String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
             int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
             int subsetId = CommonUtility.validateNumber(tempSubset);
			
			ArrayList<Integer> customerAlsoBoughtItemIdList = ProductsDAO.customerAlsoBoughtDAO(CommonUtility.validateString(itemIdString));	
			if(customerAlsoBoughtItemIdList!=null && !customerAlsoBoughtItemIdList.isEmpty()){
				ArrayList<ProductsModel> customerAlsoBoughtItemArrayList = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId, generalSubset, StringUtils.join(customerAlsoBoughtItemIdList," OR "),0,null,"itemid");
				productsActionUtilityContentObject.put("customerAlsoBoughtItemArrayList", customerAlsoBoughtItemArrayList);
				productsActionUtilityContentObject.put("responseType", "CustomerAlsoBought");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productsActionUtilityContentObject;
	}
	
}
