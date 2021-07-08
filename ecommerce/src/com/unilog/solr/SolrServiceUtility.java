package com.unilog.solr;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;

import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductHunterModel;
import com.unilog.products.ProductHunterSolrUltimateV2;
import com.unilog.products.ProductsModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.model.SwatchModel;

public class SolrServiceUtility {
	private static SolrServiceUtility instance = new SolrServiceUtility();
	private SolrServiceUtility() {}
		
	public static SolrServiceUtility getInstance() {
		return instance;
	}
	
	
	public ProductsModel buildItemList(QueryResponse response,int resultCount,LinkedHashMap<String, SolrDocumentList> expandObject,ProductHunterModel pModel){
		ProductsModel solrSearchVal = new ProductsModel();
		SolrDocumentList documents=response.getResults();
		Iterator<SolrDocument> itr = documents.iterator();
		String idList = "";
		String c = "";
		ArrayList<ProductsModel> itemLevelFilterData = new  ArrayList<ProductsModel>();
		ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
		List<FacetField> facetFeild = response.getFacetFields();
		List<RangeFacet> facetRanges = response.getFacetRanges();
		while (itr.hasNext()) {
			try{
			ProductsModel itemModel = new ProductsModel();
			SolrDocument doc = itr.next();
			String productId = doc.getFieldValue("productId")!=null?doc.getFieldValue("productId").toString():"";
			idList = idList + c + doc.getFieldValue("id").toString();
			itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("id").toString()));
			itemModel.setMinOrderQty(1);
			if(pModel.getBannerEntity()!=null){
				itemModel.setBannerEntity(pModel.getBannerEntity());
			}
			itemModel.setPartNumber(doc.getFieldValue("partNumber").toString());
			itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
			//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
			itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
			itemModel.setManufacturerName((doc.getFieldValue("manufacturer")!=null?doc.getFieldValue("manufacturer").toString():""));
			itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manufacturerId")));
			itemModel.setManufacturerPartNumber((doc.getFieldValue("manfPartNumber")!=null?doc.getFieldValue("manfPartNumber").toString():""));
			itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandId")!=null?doc.getFieldValue("brandId").toString():"")));
				
			itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
			itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
			itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
			itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
			itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
			itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
			itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
			
			
			/*if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB").trim().equalsIgnoreCase("Y"))
			{
				if(doc.getFieldValue("catSearchId")!=null && doc.getFieldValue("categoryNamePath")!=null)
				itemModel.setItemBreadCrumb(getItemBreadCrumb((List<Integer>) doc.getFieldValue("catSearchId"),(String) doc.getFieldValue("categoryNamePath")));
				
			}*/
			if(doc.getFieldValue("custom_Catalog_ID")!=null)
			{
				itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());

			}
			if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
			{
					if(expandObject!=null && expandObject.get(productId)!=null){
					
						itemModel.setProductItemCount((int)expandObject.get(productId).getNumFound()+1);
					}
			}
			if(doc.getFieldValue("categoryID")!=null){
				itemModel.setCategoryCode((doc.getFieldValue("categoryID")!=null?doc.getFieldValue("categoryID").toString():""));
			}
			
			
			itemModel.setResultCount(resultCount);
			
			String imageName = null;
   			String imageType = null;
   			
   			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
   			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
   				itemUrl = (doc.getFieldValue("manufacturer")!=null?doc.getFieldValue("manufacturer").toString():"")+" "+(doc.getFieldValue("manfPartNumber")!=null?doc.getFieldValue("manfPartNumber").toString():"");
   			}
   			
   			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
   			String pattern = "[^A-Za-z0-9]";
			 itemUrl = itemUrl.replaceAll(pattern," ");
			 itemUrl = itemUrl.replaceAll("\\s+","-");
        	itemModel.setItemUrl(itemUrl);
        	
        	Map<String, Object> customFieldValMap = doc.getFieldValueMap();
			
        	if(doc.getFieldValue("orderQtyIntervalByShipMethod")!=null && doc.getFieldValue("orderQtyIntervalByShipMethod").toString().trim().length()>0){
				LinkedHashMap<String, ProductsModel>shipOrderQtyAndInterval =  getOrderQtyIntervalByShipMethod(doc.getFieldValue("orderQtyIntervalByShipMethod").toString());
				itemModel.setShipOrderQtyAndIntervalFieldVal(shipOrderQtyAndInterval);
			}
        	
			LinkedHashMap<String, Object> customFieldVal = getAllCustomFieldVal(customFieldValMap);
			if(customFieldVal!=null && customFieldVal.size()>0)
			itemModel.setCustomFieldVal(customFieldVal);
   			
   			if(doc.getFieldValue("imageName")!=null)
   				imageName = doc.getFieldValue("imageName").toString();
   			
   					       			
   			if(doc.getFieldValue("imageType")!=null)
   				imageType = doc.getFieldValue("imageType").toString();
   			if(doc.getFieldValue("upc")!=null)
   				itemModel.setUpc(doc.getFieldValue("upc").toString());	
   			
   			if(imageName==null)
   			{
   				imageName = "NoImage.png";
   				imageType = "IMAGE";
   			}
   			itemModel.setImageName(imageName.trim());
   			itemModel.setImageType(imageType);
			c = " OR ";
			itemLevelFilterData.add(itemModel);
			
			}catch (Exception e) {
				System.out.println("Error Occured While retriving data from solr.");
				e.printStackTrace();
			}	
			
			if(itemLevelFilterData.size()>0)
			{
				
				
				 LinkedHashMap<String, ArrayList<ProductsModel>> multiFaucetResult = new LinkedHashMap<String, ArrayList<ProductsModel>>();
					if(resultCount>0 && pModel.getFilterQuery()!=null && pModel.getFilterQuery().length>0)
					{
						multiFaucetResult = ProductHunterSolrUltimateV2.generateFaucetFilter("", pModel.getFilterQuery(), pModel.getFq(),true,false,null,"defaultCategory:Y",pModel.getFilteredMultList());
						
						if(multiFaucetResult!=null && multiFaucetResult.get("price")!=null)
						{
							ArrayList<ProductsModel> fRange = multiFaucetResult.get("price");
							if(fRange!=null && fRange.size()>0 && fRange.get(0).getFacetRange()!=null && fRange.get(0).getFacetRange().size()>0)
								facetRanges = fRange.get(0).getFacetRange();
							
						}
						
					}

				attrFilteredList = buildFacetFilter(facetFeild, multiFaucetResult, pModel.getFilteredMultList());
			}
			
			solrSearchVal.setItemDataList(itemLevelFilterData);
			solrSearchVal.setAttributeDataList(attrFilteredList);
			solrSearchVal.setFacetRange(facetRanges);
			solrSearchVal.setIdList(idList);
		}
		return solrSearchVal;
		
	}
	
	public LinkedHashMap<String, SolrDocumentList> buildProductCount(QueryResponse response){
		LinkedHashMap<String, SolrDocumentList> expandObject = new LinkedHashMap<String, SolrDocumentList>();
		try{
			NamedList<Object> responseObject = response.getResponse();
			Object objectType = responseObject.get("expanded");
			if(objectType!=null){
			System.out.println(objectType.getClass());
			if(objectType.getClass() == SimpleOrderedMap.class){
				SimpleOrderedMap<SolrDocumentList> expandObjectTmp =  (SimpleOrderedMap<SolrDocumentList>) responseObject.get("expanded");
				for(Map.Entry e : expandObjectTmp) {
					expandObject.put((String)e.getKey(), (SolrDocumentList)e.getValue());
				}
				
			}else{
				expandObject = (LinkedHashMap<String, SolrDocumentList>) responseObject.get("expanded");
			}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return expandObject;
	}
	
	public ArrayList<ProductsModel> buildItemFacetFilter(List<FacetField> facetFeild){
		return null;
	}
	
	
	
	public ArrayList<ProductsModel> buildFacetFilter(List<FacetField> facetFeild, LinkedHashMap<String, ArrayList<ProductsModel>> multiFaucetResult, LinkedHashMap<String, ArrayList<String>> filteredMultList){

		
		LinkedHashMap<String, ArrayList<ProductsModel>> filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
		ArrayList<ProductsModel>  attrList = new ArrayList<ProductsModel>();
		ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
		String attrValue = "";
		String attributePrepend = "attr_";
		String imageFilter = "N";
		String displayTextFilter = "N";
		String attributeImageName = "";
		String attributeImageType =  "";
		String itemImageFilter ="";
		String itemDisplayTextFilter = "";
		boolean mulitAttribute = false;
		boolean isSwatch = false;
		SwatchModel swatchModel = null;
	/*	if(CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE")!=null && CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE").trim().equalsIgnoreCase("Y")){
			attributePrepend = "attr_Multi_";
			mulitAttribute = true;
		}*/
		
		try{
		if(facetFeild!=null){
			for(FacetField facetFilter:facetFeild)
			{
				isSwatch = false;
				ProductsModel tempVal = new ProductsModel();
				if(ProductHunterSolrUltimateV2.isSwathAttribute(facetFilter.getName().replace(attributePrepend, "")) && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SWATCH")).trim().equalsIgnoreCase("Y") && CommonDBQuery.getSwatchValueList()!=null && CommonDBQuery.getSwatchValueList().size()>0){
					isSwatch = true;
					
				}
				if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
				{
					filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
					attrList = new ArrayList<ProductsModel>();
					if(multiFaucetResult!=null && multiFaucetResult.get(facetFilter.getName().replace(attributePrepend, ""))!=null)
					{
						
						filteredList.put(facetFilter.getName().replace(attributePrepend, ""), multiFaucetResult.get(facetFilter.getName().replace(attributePrepend, "")));
						tempVal.setAttrFilterList(filteredList);
						attrFilteredList.add(tempVal);
					}
					else
					{
						List<Count> attrValArr = facetFilter.getValues();
						for(Count attrArr : attrValArr)
						{
							String attributeValueArr = attrArr.getName();
							String[] valueArr = null;
							imageFilter = "";
							displayTextFilter = "";
							attributeImageName = "";
							attributeImageType = "";
							attrValue = "";
							if(mulitAttribute){
								valueArr = attributeValueArr.split("\\|~\\|");
								
								if(valueArr!=null && valueArr.length>2){
									attrValue = valueArr[1];
									imageFilter = valueArr[2];
									if(valueArr.length>3){
										displayTextFilter = valueArr[3];
									}if(valueArr.length>4){
										itemImageFilter = valueArr[4];
									}if(valueArr.length>5){
										itemDisplayTextFilter = valueArr[5];
									}if(valueArr.length>6){
										attributeImageName = valueArr[6];
									}if(valueArr.length>7){
										attributeImageType = valueArr[7];
									}
									
									
								}else if(valueArr!=null && valueArr.length>1){
									attrValue = valueArr[1];
								}else{
									attrValue = attrArr.getName();
								}
							}else{
								attrValue = attrArr.getName();
							}
							ProductsModel attrListVal = new ProductsModel();
							if(filteredMultList ==null)
								filteredMultList = new  LinkedHashMap<String, ArrayList<String>>();
							ArrayList<String> aList = filteredMultList.get(facetFilter.getName());
							if(aList!=null && aList.size()>0)
							{
								if(!aList.contains(attrArr.getName()))
								{
									attrListVal.setAttrValueEncoded(URLEncoder.encode(attrValue,"UTF-8"));
									attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace(attributePrepend, ""),"UTF-8"));
									attrListVal.setAttrValue(attrValue);
									attrListVal.setResultCount((int) attrArr.getCount());
									
									if(isSwatch){
										swatchModel = CommonDBQuery.getSwatchValueList().get(attrValue.toUpperCase());
										if(swatchModel!=null){
											attrListVal.setImageFilter("Y");
											attrListVal.setItemImageFilter("Y");
											attrListVal.setDisplayFilterText(displayTextFilter);
											attrListVal.setImageName(swatchModel.getSwathcImage());
											attrListVal.setSwatchColorCode(swatchModel.getColorCode());
											if(swatchModel.getColorCode()!=null && swatchModel.getColorCode().startsWith("#")){
												attrListVal.setImageType("CODE");
											}else if(swatchModel.getSwathcImage()!=null){
												attrListVal.setImageType("IMG");
											}else{
												attrListVal.setImageType("VALUE");
											}
										}else{
											attrListVal.setImageFilter("Y");
											attrListVal.setItemImageFilter("Y");
											attrListVal.setImageType("NOIMG");
										}
											
										
										
									}							
									attrListVal.setItemDisplayFilterText(itemDisplayTextFilter);
									attrList.add(attrListVal);
								}
							}
							else
							{
								attrListVal.setAttrValueEncoded(URLEncoder.encode(attrValue,"UTF-8"));
								attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace(attributePrepend, ""),"UTF-8"));
								attrListVal.setAttrValue(attrValue);
								attrListVal.setResultCount((int) attrArr.getCount());
								if(isSwatch){
									swatchModel = CommonDBQuery.getSwatchValueList().get(attrValue.trim().toUpperCase());
									if(swatchModel!=null){
										attrListVal.setImageFilter("Y");
										attrListVal.setItemImageFilter("Y");
										attrListVal.setDisplayFilterText(displayTextFilter);
										attrListVal.setImageName(swatchModel.getSwathcImage());
										attrListVal.setSwatchColorCode(swatchModel.getColorCode());
										if(swatchModel.getColorCode()!=null && swatchModel.getColorCode().startsWith("#")){
											attrListVal.setImageType("CODE");
										}else if(swatchModel.getSwathcImage()!=null){
											attrListVal.setImageType("IMG");
										}else{
											attrListVal.setImageType("VALUE");
										}
									}else{
										attrListVal.setImageFilter("Y");
										attrListVal.setItemImageFilter("Y");
										attrListVal.setImageType("NOIMG");
									}
									
								}	
								attrList.add(attrListVal);
							}
							
							
						
							
						}
						if(attrList!=null && attrList.size()>0)
						{
						filteredList.put(facetFilter.getName().replace(attributePrepend, ""), attrList);
						tempVal.setAttrFilterList(filteredList);
						attrFilteredList.add(tempVal);
						
						}
					}
					
				}
				
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return attrFilteredList;
	
	}

	public String getSolrFieldValue(SolrDocument doc, String fieldName)
	{
		String fieldValue = "";
		try
		{
			if(doc.getFieldValue(fieldName)!=null)
				fieldValue = doc.getFieldValue(fieldName).toString();
		}
		catch (Exception e) {
			fieldValue = "";
		}
		return fieldValue;
	}
	

public LinkedHashMap<String, ProductsModel> getOrderQtyIntervalByShipMethod(String shipQtyFieldValString)
{
	LinkedHashMap<String, ProductsModel> shipQtyFieldValMap = new LinkedHashMap<String, ProductsModel>();
	try{
		if(shipQtyFieldValString!=null && shipQtyFieldValString.trim().length()>0){
			String[] splitString = shipQtyFieldValString.split(";");
			
			if(splitString!=null && splitString.length>0){
				
				for(String shipOrderQtyDetails : splitString){
					
					if(shipOrderQtyDetails!=null && shipOrderQtyDetails.trim().length()>0){
						
						String[] splitQtyDetails = shipOrderQtyDetails.split("\\|");
						
						if(splitQtyDetails!=null && splitQtyDetails.length>0){
							
							ProductsModel shipQtyModel = new ProductsModel();
							
							String shipViaCode = "";
							String shipWareHouse = "";
							String shipMinOrderQty = "";
							String shipOrderQtyInterval = "";
							
							if(splitQtyDetails.length>0)
								shipViaCode = splitQtyDetails[0];
							if(splitQtyDetails.length>1)
								shipWareHouse = splitQtyDetails[1];
							if(splitQtyDetails.length>2)
								shipMinOrderQty = splitQtyDetails[2];
							if(splitQtyDetails.length>3)
								shipOrderQtyInterval = splitQtyDetails[3];
							
							shipQtyModel.setShipViaCode(shipViaCode);
							shipQtyModel.setWareHouseCode(shipWareHouse);
							shipQtyModel.setMinOrderQty(CommonUtility.validateNumber(shipMinOrderQty));
							shipQtyModel.setOrderInterval(CommonUtility.validateNumber(shipOrderQtyInterval));
							
							
							shipQtyFieldValMap.put(shipViaCode.trim().toUpperCase(), shipQtyModel);
						}
					}
					
				}
				
			}
			
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return shipQtyFieldValMap;
}

public LinkedHashMap<String, Object> getAllCustomFieldVal(Map<String, Object> customFieldValMap)
{
	LinkedHashMap<String, Object> customFieldVal = new LinkedHashMap<String, Object>();
	
	try{
		for (String key: customFieldValMap.keySet()) 
	    {            
			if(key.startsWith("custom_"))
			{
				customFieldVal.put(key, customFieldValMap.get(key));
			}
	        
	    }
	}catch(Exception e){
		e.printStackTrace();
	}
	return customFieldVal;
}


}
