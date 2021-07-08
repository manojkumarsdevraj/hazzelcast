package com.unilog.products;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.RangeFacet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

public class ProductHunterUtility {

	private static ProductHunterUtility productHunterUtility = null;
	private static String rangeStart = "10";
	private static String rangeEnd = "10000";
	private static String rangeGap = "10";
	
	private ProductHunterUtility(){
		
	}
	
	public static ProductHunterUtility getInstance(){
		synchronized (ProductHunterUtility.class) {
			if(productHunterUtility==null){
				productHunterUtility = new ProductHunterUtility();
			}
		}
		return productHunterUtility;
	}
	
	public void setRangeFilteQuery(SolrQuery query){
		try{
		 if(CommonDBQuery.getSystemParamtersList().get("PRICE_RANGE_FILTER")!=null && CommonDBQuery.getSystemParamtersList().get("PRICE_RANGE_FILTER").trim().equalsIgnoreCase("Y")){
			query.set("facet.range", "price");
			query.set("f.price.facet.range.start", CommonDBQuery.getSystemParamtersList().get("PRICE_RANGE_START")==null?rangeStart:CommonDBQuery.getSystemParamtersList().get("PRICE_RANGE_START"));
			query.set("f.price.facet.range.end", CommonDBQuery.getSystemParamtersList().get("PRICE_RANGE_END")==null?rangeStart:CommonDBQuery.getSystemParamtersList().get("PRICE_RANGE_END"));
			query.set("facet.range.gap", CommonDBQuery.getSystemParamtersList().get("PRICE_RANGE_GAP")==null?rangeStart:CommonDBQuery.getSystemParamtersList().get("PRICE_RANGE_GAP"));
			query.add("facet.range.other", "before");
			query.add("facet.range.other", "after");
		 }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<ProductsModel> setFacetRanges(List<RangeFacet> facetRanges){
		ArrayList<ProductsModel> facetRangeList = null;
		try{
			if(facetRanges!=null){
			facetRangeList = new ArrayList<ProductsModel>();
			ProductsModel facetRange = new ProductsModel();
			facetRange.setFacetRange(facetRanges);
			facetRangeList.add(facetRange);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return facetRangeList;
		
	}
	public String buildSearchKeyword(ProductHunterModel pModel){
		
		String singleKeyword = "";
		try{
			String keyWord = pModel.getKeyWord();
			keyWord = keyWord.replaceAll("\\|", " ");
			String narrowKeyword = pModel.getNarrowKeyword();
			 String fq = pModel.getFq();
			String validateForSearch[] = keyWord.split("\\s+",-1);
				if(validateForSearch!=null && validateForSearch.length>1){
				    String validateOr[] = keyWord.split(" OR ",-1);
					    if(validateOr!=null && validateOr.length>1){
					    	String s = "";
						    for(String sKey :validateOr){
						    	
							     String validateWordLength[] = sKey.split("\\s+",-1);
							     if(validateWordLength!=null && validateWordLength.length > 1){
							    	 singleKeyword = singleKeyword + s + sKey;
							     }else{
							    	 singleKeyword = singleKeyword + s + "partnumbersearch:*"+sKey+"* OR "+sKey;
							     }
							     
							     s = " OR ";
						    }
						    }else{
						    	singleKeyword = keyWord;
						    }
					    
					    
				 }else{
				   singleKeyword = "partnumbersearch:*"+keyWord+"* OR "+keyWord;
			   }
				
				
				 if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
					 String validateFornSearch[] = narrowKeyword.split("\\s+",-1);
					 if(validateFornSearch.length>1){
						 //query.addFilterQuery("{!edismax}"+narrowKeyword);
						 fq = fq + "~{!edismax}"+narrowKeyword;
						 
					 }else{
						 //query.addFilterQuery("{!edismax}*"+narrowKeyword+"* OR " + narrowKeyword);
						 fq = fq + "~{!edismax}"+narrowKeyword+"* OR " + narrowKeyword;
					 }
					 pModel.setFq(fq);
				 }
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return singleKeyword;
	}
	
	public void getSortOption(String requestType,String sortBy, SolrQuery query){
		String sortField = "partnumber";
		ORDER sortOrder = SolrQuery.ORDER.asc;
		try{

			if (requestType.trim().equalsIgnoreCase("SEARCH") && sortBy == null ) {
				sortField = "default";
				sortBy = "default"; //On Dec 4th 2014 Aramsco and others 
				//sortField =  "externalHitSort";
				sortOrder = SolrQuery.ORDER.desc;
			}

			if(CommonUtility.validateString(sortBy).trim().length()==0){
				sortBy = "externalHitSort_desc";
			}
			if(sortBy!=null && !sortBy.trim().equalsIgnoreCase(""))
			{

				if(sortBy.trim().equalsIgnoreCase("price_high"))
				{
					sortField = "NET_PRICE";
					sortOrder = SolrQuery.ORDER.desc;
				}
				else if(sortBy.trim().equalsIgnoreCase("price_low"))
				{
					sortField = "NET_PRICE";
					sortOrder = SolrQuery.ORDER.asc;
				}
				else if(sortBy.trim().equalsIgnoreCase("erp_price_low"))
				{
					sortField = "price";
					sortOrder = SolrQuery.ORDER.asc;
				}
				else if(sortBy.trim().equalsIgnoreCase("erp_price_high"))
				{
					sortField = "price";
					sortOrder = SolrQuery.ORDER.desc;
				}

				else if(sortBy.trim().equalsIgnoreCase("brand"))
				{
					sortField = "brand";
					sortOrder = SolrQuery.ORDER.asc;
				}
				else if(sortBy.trim().equalsIgnoreCase("brand_asc"))
				{
					sortField = "brand";
					sortOrder = SolrQuery.ORDER.asc;
				}
				else if(sortBy.trim().equalsIgnoreCase("brand_desc"))
				{
					sortField = "brand";
					sortOrder = SolrQuery.ORDER.desc;
				}
				else if(sortBy.trim().equalsIgnoreCase("partnum_asc"))
				{
					sortField = "partnumber";
					sortOrder = SolrQuery.ORDER.asc;
				}
				else if(sortBy.trim().equalsIgnoreCase("partnum_desc"))
				{
					sortField = "partnumber";
					sortOrder = SolrQuery.ORDER.desc;
				}else if(sortBy.trim().equalsIgnoreCase("externalHitSort_desc")){

					sortField = "externalHitSort";
					sortOrder = SolrQuery.ORDER.desc;

				}else if(sortBy.trim().equalsIgnoreCase("externalHitSort_asc")){

					sortField = "externalHitSort";
					sortOrder = SolrQuery.ORDER.asc;

				}else if(sortBy.trim().equalsIgnoreCase("bestSellerSort_desc")){

					sortField = "bestSellerSort";
					sortOrder = SolrQuery.ORDER.desc;

				}else if(sortBy.trim().equalsIgnoreCase("bestSellerSort_asc")){

					sortField = "bestSellerSort";
					sortOrder = SolrQuery.ORDER.asc;

				}else if(sortBy.trim().equalsIgnoreCase("internalHitSort_desc")){

					sortField = "internalHitSort";
					sortOrder = SolrQuery.ORDER.desc;

				}else if(sortBy.trim().equalsIgnoreCase("internalHitSort_asc")){

					sortField = "internalHitSort";
					sortOrder = SolrQuery.ORDER.asc;

				}else if(sortBy.trim().equalsIgnoreCase("MPN_desc")){

					sortField = "manfpartnumber";
					sortOrder = SolrQuery.ORDER.desc;

				}else if(sortBy.trim().equalsIgnoreCase("MPN_asc")){

					sortField = "manfpartnumber";
					sortOrder = SolrQuery.ORDER.asc;

				}else if(sortBy.trim().equalsIgnoreCase("upc_desc")){

					sortField = "upc";
					sortOrder = SolrQuery.ORDER.desc;

				}else if(sortBy.trim().equalsIgnoreCase("upc_asc")){

					sortField = "upc";
					sortOrder = SolrQuery.ORDER.asc;

				}else if(sortBy.trim().equalsIgnoreCase("desc_desc")){

					sortField = "description_sort";
					sortOrder = SolrQuery.ORDER.desc;

				}else if(sortBy.trim().equalsIgnoreCase("desc_asc")){

					sortField = "description_sort";
					sortOrder = SolrQuery.ORDER.asc;

				}else if(sortBy.trim().equalsIgnoreCase("manufacturer_asc")){

					sortField = "manufacturerName";
					sortOrder = SolrQuery.ORDER.asc;

				}else if(sortBy.trim().equalsIgnoreCase("manufacturer_desc")){

					sortField = "manufacturerName";
					sortOrder = SolrQuery.ORDER.desc;

				}else if(sortBy.trim().equalsIgnoreCase("popularity_desc")){
					sortField = "popularity";
					sortOrder = SolrQuery.ORDER.desc;
				}else if(sortBy.trim().equalsIgnoreCase("elevate")){
					sortField = "elevate";
					sortOrder = SolrQuery.ORDER.desc;
				}else if(sortBy.trim().equalsIgnoreCase("manufacturerDisplayNameSort_asc")){
					sortField = "manufacturerDisplayNameSort";
					sortOrder = SolrQuery.ORDER.asc;
			    }else if(sortBy.trim().equalsIgnoreCase("manufacturerDisplayNameSort_desc")){
					sortField = "manufacturerDisplayNameSort";
					sortOrder = SolrQuery.ORDER.desc;
			    }else if (sortBy.trim().equalsIgnoreCase("altpartnum_asc")){                                                       
			          sortField = "alternatePartNumber1";                          
			          sortOrder = SolrQuery.ORDER.asc;                 
			    }else if (sortBy.trim().equalsIgnoreCase("altpartnum_desc")){                      
			          sortField = "alternatePartNumber1";                          
			          sortOrder = SolrQuery.ORDER.desc;                              
			    }else{
					if(CommonUtility.validateString(sortBy).contains("|")){
						String[] customSort=sortBy.split("\\|");
						if(CommonUtility.validateString(customSort[0]).length()>0){
							sortField = customSort[0];
						}
						if(CommonUtility.validateString(customSort[1]).equalsIgnoreCase("asc")){
							sortOrder = SolrQuery.ORDER.asc;
						}else if(CommonUtility.validateString(customSort[1]).equalsIgnoreCase("desc")){
							sortOrder = SolrQuery.ORDER.desc;
						}
					}
				}
				query.setSortField(sortField, sortOrder);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ProductHunterModel getFilterAndFacet(ProductHunterModel pModel){
		String attrFtr[] = null;
		String attrFtrGlobal[] = null;
		String attrFilterList = pModel.getAttrFilterList();
		String enableMultifilter = "N";
		String  fq = pModel.getFq();
		 
		boolean isPriceRange =false;
		boolean isCategoryNav = pModel.isCategoryNav();
		int treeId = pModel.getTreeId();
		String taxonomyId = pModel.getTaxonomyId();
		try{
			String rebuildAttr = "";
			String aFilter = "";
			String categoryName = null;
			ArrayList<String> defaultFacetFilterList = ProductHunterUtility.getInstance().defaultFilterAttribute(taxonomyId);
			ArrayList<String> removeAttributeList = new ArrayList<String>();
			ArrayList<String> valueList = new ArrayList<String>();	
			ArrayList<ProductsModel> refinedList = new ArrayList<ProductsModel>();
			LinkedHashMap<String, ArrayList<String>> filteredMultList = new LinkedHashMap<String, ArrayList<String>>();
			String c = "";
			
			if(CommonDBQuery.getSystemParamtersList().get("ENABLE_MULTIFILTER")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_MULTIFILTER").trim().equalsIgnoreCase("Y"))
			{
				enableMultifilter = "Y";
			}
		
			if(attrFilterList!=null && !attrFilterList.trim().equalsIgnoreCase(""))
			{
				attrFilterList = attrFilterList.replace("attr_brand", "brand");
				attrFilterList = attrFilterList.replace("attr_category", "category_"+taxonomyId+"_Y");
				attrFilterList = attrFilterList.replace("attr_manufacturerName", "manufacturerName");
				attrFilterList = attrFilterList.replace("attr_manufacturer", "manufacturer");
				attrFilterList = attrFilterList.replace("attr_price", "price");
				String tempArr[] = attrFilterList.split("~");
				c = "";
				for(String sArr:tempArr)
				{
					
					String attrArray[]=sArr.split(":");
					ProductsModel filterListVal = new ProductsModel();
					if(enableMultifilter.trim().equalsIgnoreCase("N"))
					{
						if(defaultFacetFilterList!=null && defaultFacetFilterList.size()>0)
						{
							if(defaultFacetFilterList.contains(attrArray[0].trim()))
							{
								removeAttributeList.add(attrArray[0].trim());
							}
						}

					}

					filterListVal.setAttrName(attrArray[0]);
					filterListVal.setAttrNameEncoded(URLEncoder.encode(attrArray[0],"UTF-8"));
					String arFilterVal = "";
					String arFilterDel = "";
					if(attrArray!=null && attrArray.length>2)
					{
						int arIndex = 0;

						for(String arAppend:attrArray)
						{
							if(arIndex>0)
							{
								System.out.println("Split length > 2 : Appending - " + arFilterVal +" with "+arAppend);
								arFilterVal = arFilterVal + arFilterDel + arAppend;
								arFilterDel = ":";
							}
							arIndex++;
						}
						filterListVal.setAttrValue(arFilterVal);
					}else if(attrArray!=null && attrArray.length<=1){
						arFilterVal = attrArray[0];
					}else{
						arFilterVal = attrArray[1];
					}
					filterListVal.setAttrValue(arFilterVal);
					filterListVal.setAttrValueEncoded(URLEncoder.encode(arFilterVal,"UTF-8"));
					//if(addRefinedList)
					refinedList.add(filterListVal);
					if(attrArray[0].trim().equalsIgnoreCase("price"))
					{
						isPriceRange = true;
					}
					if(attrArray[0].trim().equalsIgnoreCase("category"))
					{
						categoryName = attrArray[1];
						categoryName = categoryName.replace("|", " OR ");
						//isGetAttribute = true;
					}
					valueList = new ArrayList<String>();
					String buildFilterKey = "";
					String appendFilter[] = arFilterVal.split("\\|");
					if(appendFilter!=null && appendFilter.length>0)
					{
						String appendOr = "";
						for(String append:appendFilter)
						{
							String replaceFilterVal = append;
							if(replaceFilterVal!=null)
							{
								replaceFilterVal = replaceFilterVal.substring(1, replaceFilterVal.length()-1);
								valueList.add(replaceFilterVal);
								if(!attrArray[0].trim().equalsIgnoreCase("price"))
								{
									replaceFilterVal = escapeQueryCulpritsWithoutWhiteSpace(replaceFilterVal);
									replaceFilterVal = "\""+replaceFilterVal+"\"";
								}


								buildFilterKey = buildFilterKey + appendOr + replaceFilterVal;
								appendOr = " OR ";
							}

						}

					}
					filteredMultList.put(attrArray[0], valueList);

					rebuildAttr = rebuildAttr+c+escapeQueryCulprits(attrArray[0])+":("+buildFilterKey+")";
					c = "~";
				}
				attrFtrGlobal= rebuildAttr.split("~");
				aFilter = fq + "~"+rebuildAttr;
				attrFtr = aFilter.split("~");
			}else{
				attrFtr = fq.split("~");
			}
			pModel.setFilterQuery(attrFtrGlobal);
			pModel.setAttrFtr(attrFtr);
			pModel.setFilteredMultList(filteredMultList);
			String[] facetField = facetField(categoryName, removeAttributeList, treeId, refinedList, isPriceRange, isCategoryNav,defaultFacetFilterList);
			pModel.setFacetField(facetField);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pModel;
	}
	
	public String[] facetField(String categoryName,ArrayList<String> removeAttributeList,int treeId,ArrayList<ProductsModel> refinedList,boolean isPriceRange,boolean isCategoryNav, ArrayList<String> defaultFacetFilterList){
		ArrayList<String> finalRemoveList = new ArrayList<String>();
		String faucetField[] = null;
		String facetConUrl = ProductHunterSolrV2.solrURL+"/catalogdata";
		String facetQueryString = "category:"+categoryName;
		String facetInnderUrl = ProductHunterSolrV2.solrURL+"/categoryattribute";
		// finalRemoveList = CommonDBQuery.getDefaultFacet();
		if(removeAttributeList!=null && removeAttributeList.size()>0)
		{
			for(String lList: defaultFacetFilterList)
			{
				boolean addToList = true;
				for(String rmList:removeAttributeList)
				{
					if(lList!=null && lList.trim().equalsIgnoreCase(rmList))
					{
						addToList = false;
						break;
					}


				}
				if(addToList)
				{
					finalRemoveList.add(lList);
				}
			}


		}
		else
		{
			finalRemoveList = defaultFacetFilterList;
		}

		if(isCategoryNav)
		{
			faucetField = ProductHunterSolrUltimateV2.getFaucetFieldForSearch(facetQueryString, facetInnderUrl, treeId,refinedList,isPriceRange,false,false,false,finalRemoveList,defaultFacetFilterList);
		}
		else if(treeId>0)
		{
			faucetField = ProductHunterSolrUltimateV2.getFaucetFieldByName(facetConUrl, categoryName, facetQueryString, facetInnderUrl, treeId,refinedList,isPriceRange,false,false,false,finalRemoveList,defaultFacetFilterList);
		}		
		else if(CommonDBQuery.getSystemParamtersList().get("ENABLE_SEARCH_FACET")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_SEARCH_FACET").trim().equalsIgnoreCase("Y"))
		{
			faucetField = ProductHunterSolrUltimateV2.getFaucetFieldForSearch(facetQueryString, facetInnderUrl, treeId,refinedList,isPriceRange,false,false,false,finalRemoveList,defaultFacetFilterList);
		}
		else
		{
			if( defaultFacetFilterList!=null &&  defaultFacetFilterList.size()>0)
			{
				if(removeAttributeList!=null && removeAttributeList.size()>0)
				{


					if(finalRemoveList!=null && finalRemoveList.size()>0)
					{
						faucetField = new String[finalRemoveList.size()];
						faucetField = finalRemoveList.toArray(faucetField);
					}
				}
				else
				{
					faucetField = new String[defaultFacetFilterList.size()];
					faucetField = defaultFacetFilterList.toArray(faucetField);
				}

			}

		}
		
		return faucetField;
	}
	

	public ArrayList<String> defaultFilterAttribute(String taxonomyId){
		ArrayList<String> defaultFacetFilterList = new ArrayList<String>(CommonDBQuery.getDefaultFacet());
		try{
			int index = 0;
			
			for(String s:defaultFacetFilterList){
				System.out.println(s);
				if(s.equalsIgnoreCase("category")){
					s = "category_"+taxonomyId+"_Y";
					defaultFacetFilterList.set(index, s);
				}else if(s.equalsIgnoreCase("manufacturerName")){
					s = "manufacturer";
					defaultFacetFilterList.set(index, s);
				}
				index++;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return defaultFacetFilterList;
	}
	
	public HashMap<String, ArrayList<ProductsModel>> buildNavigationSearchResult(ProductsModel searchResultData,ProductHunterModel pmodel){
		HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
		try{
			ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
			ArrayList<ProductsModel> itemLevelFilterDataSug = new ArrayList<ProductsModel>();
			String idList = null;
			String suggestedValue = null;
			ArrayList<String> suggestedValueList = null;
			 itemLevelFilterData = searchResultData.getItemDataList();
			 suggestedValue = searchResultData.getSuggestedValue();
			 System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());
			 suggestedValueList = searchResultData.getSuggestedValueList();
			 if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
					idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList);
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}
			 
			 searchResultList.put("itemList", itemLevelFilterData);
			 searchResultList.put("filteredList", pmodel.getRefinedList());
			 itemLevelFilterDataSug = new ArrayList<ProductsModel>();
				ProductsModel itemSuggestion = new ProductsModel();
				itemSuggestion.setSuggestedValue(suggestedValue);
				itemLevelFilterDataSug.add(itemSuggestion);
				System.out.println("Suggen value updated");
				searchResultList.put("itemSuggest", itemLevelFilterDataSug);
				
				if(suggestedValueList!=null){
					ArrayList<ProductsModel> tempSuggestionList = new ArrayList<ProductsModel>();
					ProductsModel itemSuggestionModel = new ProductsModel();
					itemSuggestionModel.setSuggestedValueList(suggestedValueList);
					tempSuggestionList.add(itemSuggestionModel);
					searchResultList.put("itemSuggestValueList", tempSuggestionList); 
				}
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		return searchResultList;
	}
public String escapeQueryCulprits(String s)
{
StringBuilder sb = new StringBuilder();
for (int i = 0; i < s.length(); i++)
{
char c = s.charAt(i);
// These characters are part of the query syntax and must be escaped
if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
|| c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
|| c == '*' || c == '?' || c == '|' || c == '&' || c == ';' || c == '/'
)
{
sb.append('\\');
}
if(Character.isWhitespace(c))
{
sb.append("\\");
}
sb.append(c);
}
return sb.toString();
}

public String escapeQueryCulpritsWithoutWhiteSpace(String s){
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < s.length(); i++)
	{
		char c = s.charAt(i);
		// These characters are part of the query syntax and must be escaped
		if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
			|| c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
				|| c == '*' || c == '?' || c == '|' || c == '&' || c == ';' || c == '/'
		)
		{
			sb.append('\\');
		}

		sb.append(c);
	}
	return sb.toString();
}



	public static void setRangeStart(String rangeStart) {
		ProductHunterUtility.rangeStart = rangeStart;
	}

	public static String getRangeStart() {
		return rangeStart;
	}

	public static void setRangeEnd(String rangeEnd) {
		ProductHunterUtility.rangeEnd = rangeEnd;
	}

	public static String getRangeEnd() {
		return rangeEnd;
	}

	public static void setRangeGap(String rangeGap) {
		ProductHunterUtility.rangeGap = rangeGap;
	}

	public static String getRangeGap() {
		return rangeGap;
	}


}
