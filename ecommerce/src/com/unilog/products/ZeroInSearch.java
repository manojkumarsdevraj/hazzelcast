package com.unilog.products;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.erpmanager.ERPProductsWrapper;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

public class ZeroInSearch {

static String solrURL = CommonDBQuery.getSystemParamtersList().get("SOLR_URL");
static String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey());
	
	//beta server
// static String solrURL =  "http://199.66.100.122:8983/solrVanMeter/";

public static String escapeQueryCulprits(String s)
{
	StringBuilder sb = new StringBuilder();
	try{
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
	}
	 catch (Exception e) {
		
		 e.printStackTrace();
	}	
	return sb.toString();
}

public static String escapeQueryCulpritsWithoutWhiteSpace(String s)
{
StringBuilder sb = new StringBuilder();
	try{
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
	}
	 catch (Exception e) {
		
		 e.printStackTrace();
	}
	return sb.toString();
}

	public static HashMap<String, ArrayList<ProductsModel>> searchNavigation(String keyWord,int subsetId,int generalSubset,int fromRow,int toRow,String requestType,int psid,int npsid,int treeId,int levelNo,String attrFilterList,String brandId, String sessionId,String sortBy, int resultPerPage, String narrowKeyword, int buyingCompanyId,String userName,String userToken,String entityId,int userId,String homeTeritory,String type, String wareHousecode,String customerId,String customerCountry,String brandFilterQuery,String manfFilterQuery,String categoryFilterQuery)
		{
		
		 HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
		 int resultCount = 0;
		 
			 
		 if(keyWord!=null)
			 keyWord = keyWord.trim();
		 else
			 keyWord = "";
		 String origKeyWord = keyWord.toUpperCase();
		 HttpSolrServer server = null;
		 HttpSolrServer server1 = null;
		 try
		 {
			 if(type==null)
				 type = "";
				boolean massEnquiry = false;
			 String suggestedValue = null;
			 String indexType = "PH_SEARCH_"+subsetId;
			 if(generalSubset>0 && generalSubset!=subsetId)
				 indexType = "PH_SEARCH_"+generalSubset+"_"+subsetId;
			 String idList = "";
				String c = "";
				server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);

			 ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
			 ArrayList<ProductsModel> itemLevelFilterDataSug = new ArrayList<ProductsModel>();

			 String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			 if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			 }
			 String rebuildAttrNav = "";
			 String aFilterNav = "";
			 String attrFtrNav[] = null;
			 String categoryName = null;
			 boolean isGetAttribute = false;
			 String faucetField[] = null;
			 ArrayList<ProductsModel> refinedList = new ArrayList<ProductsModel>();
			 boolean isCategory = false;
			 boolean isBrand = false;
			 boolean isSubCategory = false;
			 isGetAttribute = true;
			 

				String sortField = "partnumber";
				ORDER sortOrder = SolrQuery.ORDER.asc;
				
				sortField = "externalHitSort";
				sortOrder = SolrQuery.ORDER.desc;
				
				if (requestType.trim().equalsIgnoreCase("SEARCH")) {
					sortField = "score";
					
				//	sortField =  "externalHitSort";
					sortOrder = SolrQuery.ORDER.desc;
				}
				
				if(sortBy == null){
					sortBy = "externalHitSort_desc";
				}

				if(sortBy!=null && !sortBy.trim().equalsIgnoreCase(""))
				{

					if(sortBy.trim().equalsIgnoreCase("price_high"))
					{
						sortBy = "NET_PRICE DESC";
					}
					else if(sortBy.trim().equalsIgnoreCase("price_low"))
					{
						sortBy = "NET_PRICE ASC";
					}
					else if(sortBy.trim().equalsIgnoreCase("brand"))
					{
						sortBy = "brand";
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
						
					}
				}
			 if(attrFilterList!=null && !attrFilterList.trim().equalsIgnoreCase(""))
				{
					attrFilterList = attrFilterList.replace("attr_brand", "brand");
					attrFilterList = attrFilterList.replace("attr_category", "category");
					String tempArr[] = attrFilterList.split("~");
					c = "";
					for(String sArr:tempArr)
					{
						String attrArray[]=sArr.split(":");
						 ProductsModel filterListVal = new ProductsModel();
						 if(!isBrand)
						 {
							 if(attrArray[0].trim().equalsIgnoreCase("brand"))
							 {
								 isBrand = true;
							 }
						 }
						 
						 if(!isCategory)
						 {
							 if(attrArray[0].trim().equalsIgnoreCase("category"))
							 {
								 isCategory = true;
							 }
						 }
						 if(!isSubCategory)
						 {
							 if(attrArray[0].trim().equalsIgnoreCase("attr_Sub Category"))
							 {
								 isSubCategory = true;
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
						 }
						 else
							 arFilterVal = attrArray[1];
						 filterListVal.setAttrValue(arFilterVal);
						 if(arFilterVal!=null)
						 filterListVal.setAttrValueEncoded(URLEncoder.encode(arFilterVal,"UTF-8"));
						 refinedList.add(filterListVal);
						if(attrArray[0].trim().equalsIgnoreCase("category"))
						{
							categoryName = attrArray[1];
							
						}
						String replaceFilterVal = arFilterVal;
						if(replaceFilterVal!=null)
						{
							replaceFilterVal = replaceFilterVal.substring(1, replaceFilterVal.length()-1);
							replaceFilterVal = escapeQueryCulpritsWithoutWhiteSpace(replaceFilterVal);
							replaceFilterVal = "\""+replaceFilterVal+"\"";
							rebuildAttrNav = rebuildAttrNav+c+escapeQueryCulprits(attrArray[0])+":"+replaceFilterVal;
							c = "~";
						}
							
					}
					aFilterNav = fqNav + "~"+rebuildAttrNav;
					if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					 {
						aFilterNav = aFilterNav +"~replaceString";
					 }
					
					attrFtrNav = aFilterNav.split("~");
				}
				else
				{
					aFilterNav = fqNav;
					if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					 {
						aFilterNav = aFilterNav +"~replaceString";
					 }
					attrFtrNav = aFilterNav.split("~");
				}
			 
			 String fqB = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			 if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fqB = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			 }
			 String sFqB = "defaultCategory:Y";
			 String rebuildAttrB = "";
			 String aFilterB = "";
			 String attrFtrB[] = null;
			
			 
			 c = "";
				if(attrFilterList!=null &&  !attrFilterList.trim().equalsIgnoreCase(""))
				{
					attrFilterList = attrFilterList.replace("attr_brand", "brand");
					attrFilterList = attrFilterList.replace("attr_category", "category");
					String tempArr[] = attrFilterList.split("~");
					c = "";
					for(String sArr:tempArr)
					{
						String attrArray[]=sArr.split(":");
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
							
						 }
						 else
							 arFilterVal = attrArray[1];
						 
						 String replaceFilterVal = arFilterVal;
						 if(replaceFilterVal!=null)
						 {
									replaceFilterVal = replaceFilterVal.substring(1, replaceFilterVal.length()-1);
								replaceFilterVal = escapeQueryCulpritsWithoutWhiteSpace(replaceFilterVal);
								replaceFilterVal = "\""+replaceFilterVal+"\"";
								
							rebuildAttrB = rebuildAttrB+c+escapeQueryCulprits(attrArray[0])+":"+replaceFilterVal;
							c = "~";
						 }
							
					}
					aFilterB = fqB + "~"+rebuildAttrB;
					if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					 {
						aFilterB = aFilterB +"~replaceString";
					 }
					
					attrFtrB = aFilterB.split("~");
				}
				else
				{
					aFilterB = fqB;
					if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					 {
						aFilterB = aFilterB +"~replaceString";
					 }
					attrFtrB = aFilterB.split("~");
				}
			 
			 String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			 if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			 }
			 String sFq = "defaultCategory:Y";
			 String rebuildAttr = "";
			 String aFilter = "";
			 String attrFtr[] = null;
			
			 
			 c = "";
				if(attrFilterList!=null &&  !attrFilterList.trim().equalsIgnoreCase(""))
				{
					attrFilterList = attrFilterList.replace("attr_brand", "brand");
					attrFilterList = attrFilterList.replace("attr_category", "category");
					String tempArr[] = attrFilterList.split("~");
					c = "";
					for(String sArr:tempArr)
					{
						String attrArray[]=sArr.split(":");
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
							
						 }
						 else
							 arFilterVal = attrArray[1];
						 
						 String buildFilterKey = "";
						 String appendFilter[] = arFilterVal.split(" OR ");
						 if(appendFilter!=null && appendFilter.length>0)
						 {
							 String appendOr = "";
							 for(String append:appendFilter)
							 {
								 String replaceFilterVal = append;
								 if(replaceFilterVal!=null)
								 {

										replaceFilterVal = replaceFilterVal.substring(1, replaceFilterVal.length()-1);
									replaceFilterVal = escapeQueryCulpritsWithoutWhiteSpace(replaceFilterVal);
									replaceFilterVal = "\""+replaceFilterVal+"\"";
									
									buildFilterKey = buildFilterKey + appendOr + replaceFilterVal;
									appendOr = " OR ";
								 }
									
							 }
							
						 }
						 
							
						rebuildAttr = rebuildAttr+c+escapeQueryCulprits(attrArray[0])+":("+buildFilterKey+")";
						c = "~";
					}
					aFilter = sFq + "~" +fq + "~"+rebuildAttr;
					if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					 {
						aFilter = aFilter +"~replaceString";
					 }
					
					attrFtr = aFilter.split("~");
				}
				else
				{
					aFilter = sFq + "~" +fq;
					if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					 {
						aFilter = aFilter +"~replaceString";
					 }
					attrFtr = aFilter.split("~");
				}
				
				 String fq1 = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
				 if(!siteNameSolr.equalsIgnoreCase(""))
				 {
					 fq1 = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
				 }
				 String cFq = "buyingCompanyId:"+buyingCompanyId;
				
				 String rebuildAttr1 = "";
				 String aFilter1 = "";
				 String attrFtr1[] = null;
				c = "";
				if(attrFilterList!=null &&  !attrFilterList.trim().equalsIgnoreCase(""))
				{
					attrFilterList = attrFilterList.replace("attr_brand", "brand");
					attrFilterList = attrFilterList.replace("attr_category", "category");
					String tempArr[] = attrFilterList.split("~");
					c = "";
					for(String sArr:tempArr)
					{
						String attrArray[]=sArr.split(":");
						rebuildAttr1 = rebuildAttr1+c+attrArray[0].replaceAll(" ", "\\\\ ")+":"+attrArray[1];
						c = "~";
					}
					aFilter1 = cFq + "~" +fq1 + "~"+rebuildAttr1;
					if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					 {
						aFilter1 = aFilter1 +"~replaceString";
					 }
					
					attrFtr1 = aFilter1.split("~");
				}
				else
				{
					aFilter1 = cFq + "~" +fq1;
					if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					 {
						aFilter1 = aFilter1 +"~replaceString";
					 }
					attrFtr1 = aFilter1.split("~");
				}
				//query.setFilterQueries(attrFtr);
				c = "";
				String facetConUrl = solrURL+"/catalogdata";
				 String facetQueryString = "category:"+categoryName;
				 String facetInnderUrl = solrURL+"/categoryattribute";
				 isGetAttribute = false;
				 if(treeId>0)
				 {
					 faucetField = ProductHunterSolrUltimate.getFaucetFieldByName(facetConUrl, categoryName, facetQueryString, facetInnderUrl, treeId,refinedList,isCategory,isBrand,isSubCategory,false,false,CommonDBQuery.getDefaultFacet(),new ArrayList<String>());
				 }
				 else if(isGetAttribute)
				 {
					 
					 faucetField = ProductHunterSolrUltimate.getFaucetFieldByName(facetConUrl, categoryName, facetQueryString, facetInnderUrl, 0,refinedList,isCategory,isBrand,isSubCategory,false,false,CommonDBQuery.getDefaultFacet(),new ArrayList<String>());
				 }
				 else
				 {
					 if(isCategory && isBrand)
						{
							System.out.println("nothing");
						}
						else if(isCategory)
						{
							if(!isSubCategory)
							 {
								 faucetField = new String[2];
								 faucetField[0] = "attr_Sub Category";
								 faucetField[1] = "brand";
								 
							 }
							else
							{
								 
								 faucetField = new String[1];
								 faucetField[0] = "brand";
							}
							
						}
						else if(isBrand)
						{
							
							if(!isSubCategory)
							 {
								 faucetField = new String[2];
								 faucetField[0] = "category";
								 faucetField[1] = "attr_Sub Category";
							 }
							else
							{
								faucetField = new String[1];
								 faucetField[0] = "category";
							}
						}
						else
						{
							 if(!isSubCategory)
							 {
								 faucetField = new String[3];
								 faucetField[0] = "category";
								 faucetField[1] = "attr_Sub Category";
								 faucetField[2] = "brand";
							 }
							else
							{
								faucetField = new String[2];
								 faucetField[0] = "category";
								 faucetField[1] = "attr_Sub Category";
							}
						}
					 
					 if(faucetField!=null && faucetField.length > 0)
					 {
						 
					 }
					 else
					 {
						 if(!isSubCategory)
						 {
							 faucetField = new String[1];
							 faucetField[0] = "attr_Sub Category";
							 
						 }
					 }
					 
				 }
				 
				 String multiKeyWordArr[] = keyWord.split("\\|\\|");
				 System.out.println(multiKeyWordArr.length);
				 String searchableKeyword = "*:*";
				 Pattern p = Pattern.compile("[^a-zA-Z0-9]");
				 Pattern p1 = Pattern.compile("[\\d]");
				 String pattern = "[^A-Za-z0-9]";
				 boolean includePartNumberSearch = false;
				 if(multiKeyWordArr!=null && multiKeyWordArr.length>0 && !keyWord.trim().equalsIgnoreCase(""))
				 {
					 searchableKeyword = "";
					 String tempKeyGenerator = "";
					 String appender = "";
					 for(String multiKey:multiKeyWordArr)
					 {
						 includePartNumberSearch = false;
						 String validateForSearch[] = multiKey.split("\\s+",-1);
							
							
						 String singleKeyword = "";
						 String multiKeyword = "";

						 if(validateForSearch!=null && validateForSearch.length>1)
						 {
							 String separator = "";
							 for(String keyBuilder:validateForSearch)
							 {
								 boolean hasSpecialChar = p1.matcher(keyBuilder.trim()).find();
								 if(keyBuilder.matches("[-+]?\\d*\\.?\\d+"))
								 {
									 includePartNumberSearch = true;
								 }
								 else
								 {
									 if(hasSpecialChar)
									 {
										 includePartNumberSearch = true;
									 }
									 else
									 {
										 hasSpecialChar = p.matcher(keyBuilder.trim()).find();
										 if(hasSpecialChar)
										 {
											 includePartNumberSearch = true;
										 }
									 }
									 
								 }
								 
								 if(includePartNumberSearch)
								 {
									 multiKeyword = multiKeyword + separator + ProductHunterSolr.buildKeyword(keyBuilder);
									 separator = " AND ";
								 }
								 includePartNumberSearch = false;
							 }
							 
							//multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord) + multiKeyword;
							multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(multiKey);
							
							multiKeyword = multiKeyword.replaceAll("\\s+", " AND ");
							System.out.println("multiKeyword Query Keyword : " + multiKeyword);
						 }
						 else
						 {

							 includePartNumberSearch = true;
							 singleKeyword = "("+ProductHunterSolr.buildKeyword(multiKey)+")";
							 System.out.println("Single Query Keyword : " + singleKeyword);
							 
						 
						 }
						 if(includePartNumberSearch)
						 searchableKeyword = searchableKeyword + appender + singleKeyword;
						 else
							 searchableKeyword = searchableKeyword + appender + "("+multiKeyword+")";
						 appender = " OR ";
					 }
					 
					 
				 }
				 
				 
				 
			if(requestType.trim().equalsIgnoreCase("SEARCH"))
	    	 {
				 keyWord = keyWord.toUpperCase();
			  		String scrubbedKeyword = keyWord.replaceAll(pattern,"");
				 String keyWordWithOutSpace = keyWord.trim();
				 keyWord = keyWord.trim();
				 keyWord = escapeQueryCulprits(keyWord);
				 keyWordWithOutSpace = escapeQueryCulpritsWithoutWhiteSpace(keyWordWithOutSpace);
				 boolean isCustomerPartNumber = false;
				 int customerPartCount = 0;
				 
				 //1.	Customer part number exact OR Customer part number squashed (all special characters removed)
				 String narrowSearchKey = null;
				 String queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+" OR "+keyWord+"* OR "+scrubbedKeyword+"*)";
				 String connUrl = solrURL+"/customerdata";
				 String connUrlSub = solrURL+"/mainitemdata";
				 ProductsModel searchResultData = new ProductsModel();
				 
				 if(keyWord.trim().equalsIgnoreCase(scrubbedKeyword))
				 queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+"*)";
				 
				 if(includePartNumberSearch)
				 {
					 System.out.println("Include Part Number Search");
					 
					 if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					 {
						 narrowSearchKey = narrowKeyword;// "customerPartNumberKeyword:"+narrowKeyword+"*";
					 }
					 
					 searchResultData = ProductHunterSolrUltimate.solrSearchResultCustomerPartNumber( narrowKeyword, queryString, connUrl, attrFtr, attrFtr1, fromRow, resultPerPage,connUrlSub,faucetField);
					 itemLevelFilterData = searchResultData.getItemDataList();
					 customerPartCount = searchResultData.getResultCount();
					 if(itemLevelFilterData.size()>0)
					 {
						 ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
						 idList = searchResultData.getIdList();
						 searchResultList.put("attrList", attrFilteredList );
						 isCustomerPartNumber = true;
					 }

					 
					//3.	Customer part number % exact % OR customer part number squashed%
					 if(itemLevelFilterData.size()<1)
					 {
						 narrowSearchKey = null;
						 queryString  = "customerPartNumberKeyword:(*"+keyWord+"* OR *"+scrubbedKeyword+"*)";
						 connUrl = solrURL+"/customerdata";
						 connUrlSub = solrURL+"/mainitemdata";
						 searchResultData = new ProductsModel();
						 
						 if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
						 {
							 narrowSearchKey = narrowKeyword;// "customerPartNumberKeyword: *"+narrowKeyword+"*";
						 }
						 
						 searchResultData = ProductHunterSolrUltimate.solrSearchResultCustomerPartNumber( narrowKeyword, queryString, connUrl, attrFtr, attrFtr1, fromRow, resultPerPage,connUrlSub,faucetField);
						 customerPartCount = searchResultData.getResultCount();
						 itemLevelFilterData = searchResultData.getItemDataList();
						 if(itemLevelFilterData.size()>0)
						 {
							 ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
							 idList = searchResultData.getIdList();
							 searchResultList.put("attrList", attrFilteredList );
							 isCustomerPartNumber = true;
						 }
						 
					 }
				 }
				 
				 	
					narrowSearchKey = null;
						queryString = searchableKeyword;
					 connUrl = solrURL+"/mainitemdata";
					 connUrlSub = solrURL+"/mainitemdata";
					 searchResultData = new ProductsModel();
					 
					 if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					 {
						 narrowSearchKey = narrowKeyword;// "partnumber:(*"+narrowKeyword+"*)";
					 }
					 
					 if(resultPerPage > itemLevelFilterData.size())
						 resultPerPage = resultPerPage - itemLevelFilterData.size();
					 else
						 resultPerPage = 0;
					 
					 if(fromRow > customerPartCount)
						 fromRow = fromRow - customerPartCount;
					 searchResultData = ZeroInSearchUltimate.zeroInsolrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,includePartNumberSearch,false,itemLevelFilterData,idList);
					 itemLevelFilterData = searchResultData.getItemDataList();
					 if(suggestedValue==null)
						 suggestedValue = searchResultData.getSuggestedValue();System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());
					 if(itemLevelFilterData.size()>0)
					 {
						 ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
						 if(idList!=null && !idList.trim().equalsIgnoreCase(""))
						 {
							 if(searchResultData.getIdList()!=null && !searchResultData.getIdList().trim().equalsIgnoreCase(""))
								 idList = idList + " OR "+searchResultData.getIdList();
						 }
						 else
							 idList = searchResultData.getIdList();
						 searchResultList.put("attrList", attrFilteredList );
					 }

				/* 
				 if (itemLevelFilterData.size() < 1) {
						narrowSearchKey = null;
						if (!includePartNumberSearch)
							{
							queryString = multiKeywordOr;
							connUrl = solrURL+"/mainitemdata";
							connUrlSub = solrURL+ "/mainitemdata";
							searchResultData = new ProductsModel();

							if (narrowKeyword != null && !narrowKeyword.trim().equalsIgnoreCase("")) {
								narrowSearchKey = narrowKeyword;
							}

							searchResultData = ProductHunterSolrUltimate.zeroInsolrSearchResult(narrowSearchKey, queryString,	connUrl, attrFtr, fromRow, resultPerPage,connUrlSub, faucetField, origKeyWord,sortField, sortOrder,includePartNumberSearch, false,itemLevelFilterData,"");
							itemLevelFilterData = searchResultData.getItemDataList();
							if (suggestedValue == null)
								suggestedValue = searchResultData.getSuggestedValue();
							System.out.println("Suggtion Set : " + suggestedValue+ " - " + searchResultData.getSuggestedValue());
							if (itemLevelFilterData.size() > 0) {
								ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
								idList = searchResultData.getIdList();
								searchResultList.put("attrList", attrFilteredList);
							}
							}
							

					}*/
				 


	    	 }
			 
			 
			 itemLevelFilterDataSug = new ArrayList<ProductsModel>();
				ProductsModel itemSuggestion = new ProductsModel();
				itemSuggestion.setSuggestedValue(suggestedValue);
				itemLevelFilterDataSug.add(itemSuggestion);
				System.out.println("Suggen value updated");
			 searchResultList.put("itemSuggest", itemLevelFilterDataSug);
			 if(itemLevelFilterData.size()>0)
				{
					server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
					server1.setParser(new XMLResponseParser());
					QueryRequest req1 = new QueryRequest();
					req1.setMethod(METHOD.POST);
					SolrQuery query1 = new SolrQuery();
					
					query1.setQuery("itemid:("+idList+")");
					String fq2 = "type:"+indexType;
					query1.setFilterQueries(fq2);
					query1.setStart(0);
					query1.setRows(itemLevelFilterData.size());
					
					QueryResponse response1 = server1.query(query1);
					
					SolrDocumentList documents1 = response1.getResults();
					resultCount = (int) response1.getResults().getNumFound();
					

					
					Iterator<SolrDocument> itr1 = documents1.iterator();
					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
					ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
					while (itr1.hasNext()) {
						
						SolrDocument doc = itr1.next();
						for(ProductsModel iList : itemLevelFilterData)
						{
							int itemId = CommonUtility.validateNumber(doc.getFieldValue("itemid").toString());
							
							
						
							if(itemId==iList.getItemId())
							{
								ProductsModel ProductsModel = new ProductsModel();
				       	        ProductsModel.setErpPartNumber(iList.getPartNumber());
				       	        partIdentifiersList.add(ProductsModel);
								int minOrdQty = 1;
								int orderQtyInterval = 1;
								int saleQty = 1;
								int packageQty = 1;
								int packageFlag = 0;
								String mOrdQty = null;
								String iPrice ="";
								if(doc.getFieldValue("price")!=null)
								{
									iPrice = doc.getFieldValue("price").toString();
								}
									
								
								double itemPrice = 0d;
								if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
								{
									itemPrice = Double.parseDouble(iPrice);
								}
								
								iList.setPrice(itemPrice);
								if(doc.getFieldValue("uom")!=null)
								iList.setUom(doc.getFieldValue("uom").toString());
								iList.setItemPriceId(CommonUtility.validateNumber(doc.getFieldValue("itemPriceId").toString()));
								
															
							/*
							 *  commented next lines 
							 *  
							 	if(doc.getFieldValue("customerPartNumber")!=null)
									iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
									
							*/		
								
								if(doc.getFieldValue("materialGroup")!=null)
									iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
								
								if(doc.getFieldValue("minordqty")!=null)
								{
									if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
										minOrdQty = CommonUtility.validateNumber(doc.getFieldValue("minordqty").toString());
									if(minOrdQty==0)
										minOrdQty = 1;
								}
								if(doc.getFieldValue("orderQtyInterval")!=null)
								{
									if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
										orderQtyInterval = CommonUtility.validateNumber(doc.getFieldValue("orderQtyInterval").toString());
									if(orderQtyInterval==0)
										orderQtyInterval = 1;
								}
								if(doc.getFieldValue("salesQty")!=null)
								{
									if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
										saleQty = CommonUtility.validateNumber(doc.getFieldValue("salesQty").toString());
									
									if(saleQty==0)
										saleQty = 1;
								}
								
								if(doc.getFieldValue("packageFlag")!=null)
								{
									if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
										packageFlag = CommonUtility.validateNumber(doc.getFieldValue("packageFlag").toString());
								}
								
								if(doc.getFieldValue("packageQty")!=null)
								{
									if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
									{
										packageQty = CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString());
									}
									
								}
								
								if(doc.getFieldValue("packDesc")!=null)
								{
									String packDesc = doc.getFieldValue("packDesc").toString();
									iList.setPackDesc(packDesc);
								}
								
								if(doc.getFieldValue("displayPricing") != null){
									
									iList.setDisplayPricing(doc.getFieldValue("displayPricing").toString());
								}
								if(doc.getFieldValue("clearanceFlag") != null){
									iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
								}
								if(doc.getFieldValue("clearanceFlag") != null){
									iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
								}
								iList.setPackageFlag(packageFlag);
								iList.setPackageQty(packageQty);
								iList.setSaleQty(saleQty);
								iList.setMinOrderQty(minOrdQty);
								iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
								
								//iList.setCustomerPartNumberList(getcustomerParnumber(itemId, buyingCompanyId, buyingCompanyId));
								
								
							}
						}
					}
					 
					 String requiredAvailabilty = null;
			 			if(userId>1){
			 				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_AVAILABILITY_PRODUCT_LISTING");
			 			}else{
			 				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_AVAILABILITY_PRODUCT_LISTING");	
			 			}
					 String isErpDown = CommonDBQuery.getSystemParamtersList().get("ECLIPSEAVAILABLE");
		               String swithOnEclipse = CommonDBQuery.getSystemParamtersList().get("SWITCHONECLIPSEPRICE");
		               
		               
		               if(swithOnEclipse!=null && swithOnEclipse.equalsIgnoreCase("Y") && type.equalsIgnoreCase("mobile")){
		            	   massEnquiry = true;
		               }else if(swithOnEclipse!=null && swithOnEclipse.equalsIgnoreCase("N") && type.equalsIgnoreCase("")){
		            	   massEnquiry = true;
		               }else {
		            	   massEnquiry = false;
		               }
		               
			        	 if(massEnquiry){
	            if(itemLevelFilterData!=null && itemLevelFilterData.size()>0 && userToken != null && !userToken.trim().equalsIgnoreCase(""))
	            {
	         	ArrayList<ProductsModel> productPriceOutput = new ArrayList<ProductsModel>();
	         	productPriceOutput = ERPProductsWrapper.massProductInquiry(userToken,partIdentifiersList, userName,entityId,requiredAvailabilty);
		            for(ProductsModel itemPrice : itemLevelFilterData)
	         	   {
	         	   
	         		   	for(ProductsModel eclipseitemPrice : productPriceOutput)
	         		   		{
	         		   			if(itemPrice!=null)
	         		   				{
	         		   					if(itemPrice.getPartNumber().equals(eclipseitemPrice.getPartNumber().trim()))
	         		   							{
	         		   						if(itemPrice.getIsImap()!=null && itemPrice.getIsImap().trim().equalsIgnoreCase("Y"))
	         		   							itemPrice.setImapPrice(eclipseitemPrice.getCustomerPrice());
	         		   						else
	         		   						           itemPrice.setPrice(eclipseitemPrice.getCustomerPrice());
	         		   						           itemPrice.setBranchAvail(eclipseitemPrice.getBranchAvail());
	         		   						           
	         		   						           if(itemPrice.getUom() == null)
	         		   						        	   itemPrice.setUom(eclipseitemPrice.getUom());
	         		   						           
	         		   						        LinkedHashMap<String, ProductsModel> branchTeritory = eclipseitemPrice.getBranchTeritory();
	         		   						     if(branchTeritory!=null)
	            		   						     {
	         		   						    	 ProductsModel bTeritory = branchTeritory.get(homeTeritory);
	            		   						    	 if(bTeritory!=null)
	            		   						    	 {
	            		   						    		itemPrice.setBranchID(bTeritory.getBranchID());
	            		   						    		itemPrice.setBranchName(bTeritory.getBranchName());
	            		   						    		itemPrice.setHomeBranchavailablity(bTeritory.getAvailQty());
	            		   						    	 itemPrice.setBranchTotalQty(bTeritory.getAvailQty());
	            		   						    	 }
	            		   						     }
	         		   						        if(eclipseitemPrice.getBranchTotalQty()>0)
	         		   						           {
	         		   						        	   if(eclipseitemPrice.getBranchAvail().get(0).getAvailQty()==0)
	         		   						        	   {
	         		   						        		   itemPrice.setDisplayFrieghtAlert("Y");
	         		   						        	   }
	         		   						           }
	         		   						           //itemPrice.setBranchTotalQty(eclipseitemPrice.getBranchTotal());
	         		   						        if( CommonDBQuery.getSystemParamtersList().get("GET_PRICE_FROM").equalsIgnoreCase("eclipse")){
	         		   						        	 
	         		   						        	itemPrice.setPrice(eclipseitemPrice.getExtendedPrice()*eclipseitemPrice.getQty());
	         		   						        	itemPrice.setQtyUOM(eclipseitemPrice.getQtyUOM());
	         		   						        	itemPrice.setQty(eclipseitemPrice.getQty());
	         		   						           }
	         		   							}
	         		   				}
	         	   }
	         	   
	            }
	            }
		        	 }
			 			
					   searchResultList.put("itemList", itemLevelFilterData);
					   searchResultList.put("filteredList", refinedList);
					   
				}
			
		 }
		 catch (Exception e) {
			
			 e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
			ConnectionManager.closeSolrClientConnection(server1);
		}
		return searchResultList;
		}
	
	
	
	
	public static String getFiltersAttribute(String keyWord,int subsetId,int generalSubset,int fromRow,int toRow,String requestType,int psid,int npsid,int treeId,int levelNo,String attrFilterList,String brandId, String sessionId,String sortBy, int resultPerPage, String narrowKeyword, int buyingCompanyId,String userName,String userToken,String entityId,int userId,String homeTeritory,String type, String wareHousecode,String customerId,String customerCountry,String brandFilterQuery,String manfFilterQuery,String categoryFilterQuery, String isAttribute, String attrName)
	{
	
	 HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
	 HttpSolrServer server = null;
	 int resultCount = 0;
	 String jsonResponse = "";
		 
	 if(keyWord!=null)
		 keyWord = keyWord.trim();
	 else
		 keyWord = "";
	 String origKeyWord = keyWord.toUpperCase();
	 try
	 {
		 
		 if(type==null)
			 type = "";
		 String suggestedValue = null;
		 String indexType = "PH_SEARCH_"+subsetId;
		 if(generalSubset>0 && generalSubset!=subsetId)
			 indexType = "PH_SEARCH_"+generalSubset+"_"+subsetId;
		 String idList = "";
			String c = "";
			server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
		 ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
		 String categoryName = null;
		 boolean isGetAttribute = false;
		 String faucetField[] = null;
		 ArrayList<ProductsModel> refinedList = new ArrayList<ProductsModel>();
		 boolean isCategory = false;
		 boolean isBrand = false;
		 boolean isSubCategory = false;
		 isGetAttribute = true;
		 

			String sortField = "partnumber";
			ORDER sortOrder = SolrQuery.ORDER.asc;
			
			sortField = "externalHitSort";
			sortOrder = SolrQuery.ORDER.desc;
			
			if (requestType.trim().equalsIgnoreCase("SEARCH")) {
				sortField = "score";
				
			//	sortField =  "externalHitSort";
				sortOrder = SolrQuery.ORDER.desc;
			}
			
		 String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
		 if(!siteNameSolr.equalsIgnoreCase(""))
		 {
			 fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
		 }
		 String sFq = "defaultCategory:Y";
		 String rebuildAttr = "";
		 String aFilter = "";
		 String attrFtr[] = null;
		
		 c = "";
			if(attrFilterList!=null &&  !attrFilterList.trim().equalsIgnoreCase(""))
			{
				attrFilterList = attrFilterList.replace("attr_brand", "brand");
				attrFilterList = attrFilterList.replace("attr_category", "category");
				String tempArr[] = attrFilterList.split("~");
				c = "";
				for(String sArr:tempArr)
				{
					String attrArray[]=sArr.split(":");
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
						
					 }
					 else
						 arFilterVal = attrArray[1];
					 
					 String buildFilterKey = "";
					 String appendFilter[] = arFilterVal.split(" OR ");
					 if(appendFilter!=null && appendFilter.length>0)
					 {
						 String appendOr = "";
						 for(String append:appendFilter)
						 {
							 String replaceFilterVal = append;
							 if(replaceFilterVal!=null)
							 {
										replaceFilterVal = replaceFilterVal.substring(1, replaceFilterVal.length()-1);
									replaceFilterVal = escapeQueryCulpritsWithoutWhiteSpace(replaceFilterVal);
									replaceFilterVal = "\""+replaceFilterVal+"\"";
									
									buildFilterKey = buildFilterKey + appendOr + replaceFilterVal;
									appendOr = " OR ";
							 }
								
						 }
						
					 }
					 
						
					rebuildAttr = rebuildAttr+c+escapeQueryCulprits(attrArray[0])+":("+buildFilterKey+")";
					c = "~";
				}
				aFilter = sFq + "~" +fq + "~"+rebuildAttr;
				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
					aFilter = aFilter +"~replaceString";
				 }
				
				attrFtr = aFilter.split("~");
			}
			else
			{
				aFilter = sFq + "~" +fq;
				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
					aFilter = aFilter +"~replaceString";
				 }
				attrFtr = aFilter.split("~");
			}
			
			 String fq1 = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			 if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fq1 = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			 }
			 String cFq = "buyingCompanyId:"+buyingCompanyId;
			
			 String rebuildAttr1 = "";
			 String aFilter1 = "";
			 String attrFtr1[] = null;
			c = "";
			if(attrFilterList!=null &&  !attrFilterList.trim().equalsIgnoreCase(""))
			{
				attrFilterList = attrFilterList.replace("attr_brand", "brand");
				attrFilterList = attrFilterList.replace("attr_category", "category");
				String tempArr[] = attrFilterList.split("~");
				c = "";
				for(String sArr:tempArr)
				{
					String attrArray[]=sArr.split(":");
					rebuildAttr1 = rebuildAttr1+c+attrArray[0].replaceAll(" ", "\\\\ ")+":"+attrArray[1];
					c = "~";
				}
				aFilter1 = cFq + "~" +fq1 + "~"+rebuildAttr1;
				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
					aFilter1 = aFilter1 +"~replaceString";
				 }
				
				attrFtr1 = aFilter1.split("~");
			}
			else
			{
				aFilter1 = cFq + "~" +fq1;
				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
					aFilter1 = aFilter1 +"~replaceString";
				 }
				attrFtr1 = aFilter1.split("~");
			}
			//query.setFilterQueries(attrFtr);
			c = "";
			String facetConUrl = solrURL+"/catalogdata";
			 String facetQueryString = "category:"+categoryName;
			 String facetInnderUrl = solrURL+"/categoryattribute";
			 isGetAttribute = false;
			 if(treeId>0)
			 {
				 faucetField = ProductHunterSolrUltimate.getFaucetFieldByName(facetConUrl, categoryName, facetQueryString, facetInnderUrl, treeId,refinedList,isCategory,isBrand,isSubCategory,false,false,CommonDBQuery.getDefaultFacet(),new ArrayList<String>());
			 }
			 else if(isGetAttribute)
			 {
				 
				 faucetField = ProductHunterSolrUltimate.getFaucetFieldByName(facetConUrl, categoryName, facetQueryString, facetInnderUrl, 0,refinedList,isCategory,isBrand,isSubCategory,false,false,CommonDBQuery.getDefaultFacet(),new ArrayList<String>());
			 }
			 else
			 {
				 if(isCategory && isBrand)
					{
						System.out.println("nothing");
					}
					else if(isCategory)
					{
						if(!isSubCategory)
						 {
							 faucetField = new String[2];
							 faucetField[0] = "attr_Sub Category";
							 faucetField[1] = "brand";
							 
						 }
						else
						{
							 
							 faucetField = new String[1];
							 faucetField[0] = "brand";
						}
						
					}
					else if(isBrand)
					{
						
						if(!isSubCategory)
						 {
							 faucetField = new String[2];
							 faucetField[0] = "category";
							 faucetField[1] = "attr_Sub Category";
						 }
						else
						{
							faucetField = new String[1];
							 faucetField[0] = "category";
						}
					}
					else
					{
						 if(!isSubCategory)
						 {
							 faucetField = new String[3];
							 faucetField[0] = "category";
							 faucetField[1] = "attr_Sub Category";
							 faucetField[2] = "brand";
						 }
						else
						{
							faucetField = new String[2];
							 faucetField[0] = "category";
							 faucetField[1] = "attr_Sub Category";
						}
					}
				 
				 if(faucetField!=null && faucetField.length > 0)
				 {
					 
				 }
				 else
				 {
					 if(!isSubCategory)
					 {
						 faucetField = new String[1];
						 faucetField[0] = "attr_Sub Category";
						 
					 }
				 }
				 
			 }
			 
			 String multiKeyWordArr[] = keyWord.split("\\|\\|");
			 System.out.println(multiKeyWordArr.length);
			 String searchableKeyword = "*:*";
			 Pattern p = Pattern.compile("[^a-zA-Z0-9]");
			 Pattern p1 = Pattern.compile("[\\d]");
			 String pattern = "[^A-Za-z0-9]";
			 boolean includePartNumberSearch = false;
			 
			 if(keyWord!=null && keyWord.trim().equalsIgnoreCase("*:*"))
			 {
				 searchableKeyword = keyWord;
			 }
			 else if(multiKeyWordArr!=null && multiKeyWordArr.length>0)
			 {
				 searchableKeyword = "";
				 
				 String appender = "";
				 for(String multiKey:multiKeyWordArr)
				 {
					 includePartNumberSearch = false;
					 String validateForSearch[] = multiKey.split("\\s+",-1);
						
						
					 String singleKeyword = "";
					 String multiKeyword = "";

					 if(validateForSearch!=null && validateForSearch.length>1)
					 {
						 String separator = "";
						 for(String keyBuilder:validateForSearch)
						 {
							 boolean hasSpecialChar = p1.matcher(keyBuilder.trim()).find();
							 if(keyBuilder.matches("[-+]?\\d*\\.?\\d+"))
							 {
								 includePartNumberSearch = true;
							 }
							 else
							 {
								 if(hasSpecialChar)
								 {
									 includePartNumberSearch = true;
								 }
								 else
								 {
									 hasSpecialChar = p.matcher(keyBuilder.trim()).find();
									 if(hasSpecialChar)
									 {
										 includePartNumberSearch = true;
									 }
								 }
								 
							 }
							 
							 if(includePartNumberSearch)
							 {
								 multiKeyword = multiKeyword + separator + ProductHunterSolr.buildKeyword(keyBuilder);
								 separator = " AND ";
							 }
							 includePartNumberSearch = false;
						 }
						 
						//multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord) + multiKeyword;
						multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(multiKey);
						
						multiKeyword = multiKeyword.replaceAll("\\s+", " AND ");
						System.out.println("multiKeyword Query Keyword : " + multiKeyword);
					 }
					 else
					 {

						 includePartNumberSearch = true;
						 singleKeyword = "("+ProductHunterSolr.buildKeyword(multiKey)+")";
						 System.out.println("Single Query Keyword : " + singleKeyword);
						 
					 
					 }
					 if(includePartNumberSearch)
					 searchableKeyword = searchableKeyword + appender + singleKeyword;
					 else
						 searchableKeyword = searchableKeyword + appender + "("+multiKeyword+")";
					 appender = " OR ";
				 }
				 
				 
			 }
			
			 
			 
			 
			 
			 if(isAttribute!=null && isAttribute.trim().equalsIgnoreCase("Yes"))
			 {
				 //http://10.200.1.104:8983/solr/categoryattribute/select?q=*%3A*&wt=xml&facet.field=attrName
				 if(keyWord.trim().equalsIgnoreCase("*:*") && CommonUtility.validateString(attrFilterList).equalsIgnoreCase(""))
				 {
					 HttpURLConnection eclipseConn = (HttpURLConnection) new URL(solrURL+"/categoryattribute/select?q=*%3A*&wt=json&&facet.field=attrName&json.nl=map").openConnection();
						
			            eclipseConn.setDoOutput(true);
						
						
						BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
						String line = null;
								
						StringBuffer responseData = new StringBuffer();
						
						while((line = in.readLine()) != null) {
						
							responseData.append(line);
						}
							System.out.println(responseData);
							jsonResponse = responseData.toString();
				 }
				 else
				 {

					 keyWord = keyWord.toUpperCase();
				  		String scrubbedKeyword = keyWord.replaceAll(pattern,"");
					 String keyWordWithOutSpace = keyWord.trim();
					 keyWord = keyWord.trim();
					 keyWord = escapeQueryCulprits(keyWord);
					 keyWordWithOutSpace = escapeQueryCulpritsWithoutWhiteSpace(keyWordWithOutSpace);
					 int customerPartCount = 0;
					 //1.	Customer part number exact OR Customer part number squashed (all special characters removed)
					 String narrowSearchKey = null;
					 String queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+" OR "+keyWord+"* OR "+scrubbedKeyword+"*)";
					 String connUrl = solrURL+"/customerdata";
					 String connUrlSub = solrURL+"/mainitemdata";
					 String attributeCategList = "";
					 if(keyWord.trim().equalsIgnoreCase(scrubbedKeyword))
					 queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+"*)";
					 narrowSearchKey = null;
					 queryString = searchableKeyword;
					 connUrl = solrURL+"/mainitemdata";
					 connUrlSub = solrURL+"/mainitemdata";
						 
						 attributeCategList = ZeroInSearchUltimate.getFacetCategoryList(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,includePartNumberSearch,false,itemLevelFilterData,idList);
						 
						 if(attributeCategList!=null && !attributeCategList.trim().equalsIgnoreCase(""))
						 {
							String categoryIdList =  ZeroInSearchUltimate.getFaucetFieldFull(facetConUrl, attributeCategList, facetQueryString, facetInnderUrl, treeId,refinedList,isCategory,isBrand,isSubCategory,indexType);
							if(categoryIdList!=null && !categoryIdList.trim().equalsIgnoreCase(""))
							{
								SolrQuery query = new SolrQuery();
								query.setQuery("categoryID:("+categoryIdList+")");
								query.set("wt", "json");
								query.addFacetField("attrName");
								query.set("json.nl","map");
								 HttpURLConnection eclipseConn = (HttpURLConnection) new URL(solrURL+"/categoryattribute/select?"+query).openConnection();
								 
						            eclipseConn.setDoOutput(true);
									
									
									BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
									String line = null;
											
									StringBuffer responseData = new StringBuffer();
									
									while((line = in.readLine()) != null) {
									
										responseData.append(line);
									}
										System.out.println(responseData);
										jsonResponse = responseData.toString();
							}
						 }
				 }
			 }
			 
			 else if(requestType.trim().equalsIgnoreCase("SEARCH"))
    	 {
			 keyWord = keyWord.toUpperCase();
		  		String scrubbedKeyword = keyWord.replaceAll(pattern,"");
			 String keyWordWithOutSpace = keyWord.trim();
			 keyWord = keyWord.trim();
			 keyWord = escapeQueryCulprits(keyWord);
			 keyWordWithOutSpace = escapeQueryCulpritsWithoutWhiteSpace(keyWordWithOutSpace);
			 boolean isCustomerPartNumber = false;
			 int customerPartCount = 0;
			 
			 //1.	Customer part number exact OR Customer part number squashed (all special characters removed)
			 String narrowSearchKey = null;
			 String queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+" OR "+keyWord+"* OR "+scrubbedKeyword+"*)";
			 String connUrl = solrURL+"/customerdata";
			 String connUrlSub = solrURL+"/mainitemdata";
			 ProductsModel searchResultData = new ProductsModel();
			 
			 if(keyWord.trim().equalsIgnoreCase(scrubbedKeyword))
			 queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+"*)";
			
			 	
				narrowSearchKey = null;
					queryString = searchableKeyword;
				 connUrl = solrURL+"/mainitemdata";
				 connUrlSub = solrURL+"/mainitemdata";
				 searchResultData = new ProductsModel();
				 
				 if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
					 narrowSearchKey = narrowKeyword;// "partnumber:(*"+narrowKeyword+"*)";
				 }
				 
				 if(resultPerPage > itemLevelFilterData.size())
					 resultPerPage = resultPerPage - itemLevelFilterData.size();
				 else
					 resultPerPage = 0;
				 
				 if(fromRow > customerPartCount)
					 fromRow = fromRow - customerPartCount;
				 searchResultData = ZeroInSearchUltimate.zeroInsolrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,includePartNumberSearch,false,itemLevelFilterData,idList);
				 itemLevelFilterData = searchResultData.getItemDataList();
				 if(suggestedValue==null)
					 suggestedValue = searchResultData.getSuggestedValue();System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());
				 if(itemLevelFilterData.size()>0)
				 {
					 ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
					 if(idList!=null && !idList.trim().equalsIgnoreCase(""))
					 {
						 if(searchResultData.getIdList()!=null && !searchResultData.getIdList().trim().equalsIgnoreCase(""))
							 idList = idList + " OR "+searchResultData.getIdList();
					 }
					 else
						 idList = searchResultData.getIdList();
					 searchResultList.put("attrList", attrFilteredList );
					 
					 SolrQuery solrQuery = searchResultData.getSolrQuery();
					 
					 if(attrName!=null && !attrName.equalsIgnoreCase(""))
						 solrQuery.addFacetField("attr_"+escapeQueryCulpritsWithoutWhiteSpace(attrName));
					 else
					 {
						 solrQuery.addFacetField("brand");
						 solrQuery.addFacetField("category");
						 solrQuery.addFacetField("manufacturerName");
					 }
					 
					 System.out.println("attribute query : " + solrURL+"/mainitemdata/select?"+solrQuery);
					 
	HttpURLConnection eclipseConn = (HttpURLConnection) new URL(solrURL+"/mainitemdata/select?"+solrQuery).openConnection();
						
			            eclipseConn.setDoOutput(true);
						
						
						BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
						String line = null;
								
						StringBuffer responseData = new StringBuffer();
						
						while((line = in.readLine()) != null) {
						
							responseData.append(line);
						}
							System.out.println(responseData);
							jsonResponse = responseData.toString();
							System.out.println("json Response length: " + jsonResponse.length());
							System.out.println("json Response : " + jsonResponse);
				 }
    	 }
		 
	
		
	 }catch (Exception e) {
		
		 e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return jsonResponse;
	}
}
