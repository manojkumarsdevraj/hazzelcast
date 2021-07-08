package com.unilog.products;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.promotion.BannerEntity;
import com.unilog.searchconfig.SearchConfigUtility;
import com.unilog.solr.SolrService;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.model.SwatchModel;
import com.unilognew.util.ECommerceEnumType.RequestHandlers;
import com.unilog.solr.connection.CustomerData;
import com.unilog.solr.connection.MainItem;

public class ProductHunterSolrUltimate {
	
	
	//local server
//	static String solrURL = "http://192.168.1.24:8983/solrVanMeter/";
	static String solrURL = CommonDBQuery.getSystemParamtersList().get("SOLR_URL");
	static String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey()); 
	
	//beta server
	 //static String solrURL =  "http://199.66.100.122:8983/solrVanMeter/";
	

	public static String escapeQueryCulprits(String s)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			// These characters are part of the query syntax and must be escaped
			if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
					|| c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
					|| c == '*' || c == '?' || c == '|' || c == '&' || c == ';'
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
		//return sb.toString();

		return s;
	}
	
	public static String escapeQueryCulpritsWithoutWhiteSpace(String s)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			// These characters are part of the query syntax and must be escaped
			if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
					|| c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
					|| c == '*' || c == '?' || c == '|' || c == '&' || c == ';'
					)
			{
				sb.append('\\');
			}

			sb.append(c);
		}
		return sb.toString();
	}
	
	
	public static ArrayList<ProductsModel> staticContentSearch(String connUrl,String queryString,String origKeyWord,int fromRow,int noOfRows)
	{
		ArrayList<ProductsModel> staticContentResult = new ArrayList<ProductsModel>();
		ProductsModel solrSearchVal = new ProductsModel();
		HttpSolrServer server = null;
		Gson gson = new Gson();
		try
		{
			server = ConnectionManager.getSolrClientConnection(connUrl);
			SolrQuery query = new SolrQuery();
				query.setQuery(queryString);
				query.set("spellcheck", "true");
				 query.set("spellcheck.extendedResults", "true");
				 query.set("spellcheck.collate", "true");
				 query.set("spellcheck.q", origKeyWord);
				 query.setHighlight(true);
				 query.set("hl.fl", "keywords");
				 query.set("hl.simple.pre", "<strong>");
				 query.set("hl.simple.post", "</strong>");
				 query.setHighlightFragsize(250);
				//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				
				query.setStart(fromRow);
				query.setRows(noOfRows);
				
				System.out.println("query : " + query);
				QueryResponse response = server.query(query);
				
				SolrDocumentList documents = response.getResults();
				int resultCount = (int) response.getResults().getNumFound();
				

				
				SpellCheckResponse spellCheckRes = response.getSpellCheckResponse();
				if(spellCheckRes!=null){
					System.out.println(spellCheckRes.isCorrectlySpelled());
					solrSearchVal.setSuggestedValue(spellCheckRes.getCollatedResult());
					System.out.println("DOCUMENTS Suggestion : " + spellCheckRes.getCollatedResult());
					
					if(!spellCheckRes.getSuggestions().isEmpty()){
						Iterator<Suggestion> suggestionArray = spellCheckRes.getSuggestions().iterator();
						if(suggestionArray!=null){
							List<String> suggestionList = suggestionArray.next().getAlternatives();
							solrSearchVal.setSuggestedValueList(new ArrayList<String>(suggestionList));
						}
						
					}
				}
				Iterator<SolrDocument> itr = documents.iterator();
				staticContentResult = new ArrayList<ProductsModel>();
				while (itr.hasNext()) {
					ProductsModel pageModel = new ProductsModel();
					SolrDocument doc = itr.next();
					String id = doc.getFieldValue("id").toString();
					pageModel.setStaticPageId(id);
					if(doc.getFieldValue("pageTitle")!=null)
					{
						String pattern = "[^A-Za-z0-9]";
						String keyWord = doc.getFieldValue("pageTitle").toString();
						String scrubbedKeyword = keyWord.replaceAll(pattern,"-");
						pageModel.setStaticPageTitle(doc.getFieldValue("pageTitle").toString());
						pageModel.setItemUrl(scrubbedKeyword);
					}
					if(doc.getFieldValue("pageName")!=null){
						pageModel.setPageName(doc.getFieldValue("pageName").toString());
					}
					if(doc.getFieldValue("keywords")!=null)
					{
						pageModel.setStaticContent(doc.getFieldValue("keywords").toString());
					}
					
					String pageBreadCrumb = ProductHunterSolr.getSolrFieldValue(doc,"breadCrumb");
					if(CommonUtility.validateString(pageBreadCrumb).length()>0) {
						ArrayList<ProductsModel> staticpageBreadCrumb = gson.fromJson(CommonUtility.validateString(pageBreadCrumb),new TypeToken<ArrayList<ProductsModel>>() {}.getType());
						pageModel.setStaticpageBreadCrumb(staticpageBreadCrumb);
					}
					
					pageModel.setResultCount(resultCount);
					staticContentResult.add(pageModel);
					
					if (response.getHighlighting().get(id) != null) {
						
						Map<String, List<String>> highlightList = response.getHighlighting().get(id);
						if(highlightList.get("keywords")!=null)
						{
							if(highlightList.size()>0)
							{
								List<String> highlightValList = highlightList.get("keywords");
								if(highlightValList.get(0)!=null)
								pageModel.setStaticContent(highlightValList.get(0));
							}
						}
			      }
				}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return staticContentResult;
	}
	
	
	
	public static ArrayList<String> phTypeList()
	{
		ArrayList<String> phList = new ArrayList<String>();
		HttpSolrServer server = null;
		try{
			//http://localhost:8983/solrnce/itempricedata/select?q=*%3A*&group=true&group.field=type&fl=type
			String connUrl = solrURL+"/itempricedata";//CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"mainitemdata";
			String  queryString = "*:*";
			server = ConnectionManager.getSolrClientConnection(connUrl);
			SolrQuery query = new SolrQuery();
				query.setQuery(queryString);
				query.set("group","true");
				query.set("group.field","type");
				query.set("group.field","type");
				query.setFields("type");
				//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				
				query.setStart(0);
				query.setRows(10);
				
				System.out.println("query : " + query);
				QueryResponse response = server.query(query);
				GroupResponse groupResponse = response.getGroupResponse();
				List<GroupCommand> groupValues = groupResponse.getValues();
				
				for(GroupCommand groupList:groupValues)
				{
					List<Group> groupValueList = groupList.getValues();
					
					for(Group groupVal:groupValueList)
					{
						System.out.println("Ph List : " + groupVal.getGroupValue().toString().toUpperCase() );
						phList.add(groupVal.getGroupValue().toString().toUpperCase());
					}
					
				}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return phList;
		//http://localhost:8983/solrnce/itempricedata/select?q=*%3A*&group=true&group.field=type&fl=type;
	}
	
	public static ArrayList<ProductsModel> productPromotion(String indexType,String promotionType)
	{
		ArrayList<ProductsModel> productPromotionGroup = new ArrayList<ProductsModel>();
		HttpSolrServer server = null;
		try
		{
			String connUrl = solrURL+"/mainitemdata";//CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"mainitemdata";
			String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			if(!siteNameSolr.equalsIgnoreCase(""))
			{
				fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			}
			String fq1 = "{!join from=categoryID to=categoryID fromIndex=catalogdata}taxonomyId:208";
			if(!siteNameSolr.equalsIgnoreCase(""))
			{
				fq1 = "{!join from=categoryID to=categoryID fromIndex="+siteNameSolr+"_catalogdata method=crossCollection}taxonomyId:208";
			}
			String filterQuery[] = new String[2];
			filterQuery[0] = fq;
			filterQuery[1] = fq1;
			String  queryString = promotionType+":\"Y\"";
			server = ConnectionManager.getSolrClientConnection(connUrl);
			SolrQuery query = new SolrQuery();
				query.setQuery(queryString);
				query.setFilterQueries(filterQuery);
				String fields = "idCategory";
				query.addFacetField(fields);
				query.setFacetLimit(-1);
				//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				
				query.setStart(0);
				query.setRows(10);
				
				System.out.println("query : " + query);
				QueryResponse response = server.query(query);
				
				
				int resultCount = (int) response.getResults().getNumFound();
				
			
				
				List<FacetField> facetFeild = response.getFacetFields();
				facetFeild = response.getFacetFields();
				if(resultCount>0)
				{
					for(FacetField facetFilter:facetFeild)
					{
						
						
						if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
						{
							
							productPromotionGroup = new ArrayList<ProductsModel>();
					
							List<Count> attrValArr = facetFilter.getValues();
							for(Count attrArr : attrValArr)
							{
								ProductsModel attrListVal = new ProductsModel();
								attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
								attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
								attrListVal.setAttrValue(attrArr.getName());
								String attrName = attrArr.getName();
								String valueArr[] = attrName.split("~");
								attrListVal.setCategoryCode(valueArr[0]);
								attrListVal.setCategoryName(valueArr[1]);
								productPromotionGroup.add(attrListVal);
								
							}
							
						
							
						}
						
					}
				}
				
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return productPromotionGroup;
	}
	
	
	public static ArrayList<ProductsModel> topSellers(String indexType)
	{
		ArrayList<ProductsModel> productPromotionGroup = new ArrayList<ProductsModel>();
		HttpSolrServer server = null;
		try
		{
			String connUrl = solrURL+"/mainitemdata";//CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"mainitemdata";
			String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			if(!siteNameSolr.equalsIgnoreCase(""))
			{
				fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			}
			
			String filterQuery[] = new String[2];
			filterQuery[0] = fq;
			
			String  queryString = "isHotDeal:\"Y\"";
			server = ConnectionManager.getSolrClientConnection(connUrl);
			SolrQuery query = new SolrQuery();
				query.setQuery(queryString);
				query.setFilterQueries(filterQuery);
				String fields = "manufacturerName";
				query.addFacetField(fields);
				query.setFacetLimit(-1);
				//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				
				query.setStart(0);
				query.setRows(10);
				
				System.out.println("query : " + query);
				QueryResponse response = server.query(query);
				
				
				int resultCount = (int) response.getResults().getNumFound();
				
			
				
				List<FacetField> facetFeild = response.getFacetFields();
				facetFeild = response.getFacetFields();
				if(resultCount>0)
				{
					for(FacetField facetFilter:facetFeild)
					{
						
						
						if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
						{
							
							productPromotionGroup = new ArrayList<ProductsModel>();
					
							List<Count> attrValArr = facetFilter.getValues();
							for(Count attrArr : attrValArr)
							{
								ProductsModel attrListVal = new ProductsModel();
								attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
								attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
								attrListVal.setAttrValue(attrArr.getName());
								attrListVal.setResultCount((int) attrArr.getCount());
								productPromotionGroup.add(attrListVal);
								
							}
							
						
							
						}
						
					}
				}
				
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return productPromotionGroup;
	}
	
	public static ArrayList<String> buildNarrowKeyword(String keyWord,String scrubbedKeyword)
	{
		ArrayList<String> narrowQuery = new ArrayList<String>();

		try
		{
			
			
						
			String validateForSearch[] = keyWord.split("\\s+",-1);
				 Pattern p = Pattern.compile("[^a-zA-Z0-9]");
				 Pattern p1 = Pattern.compile("[\\d]");
				 boolean includePartNumberSearch = false;
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
							 separator = " ";
						 }
						 includePartNumberSearch = false;
					 }
					 
					multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord) + multiKeyword;
					multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
					System.out.println("multiKeyword Query Keyword : " + multiKeyword);
				 }
				 else
				 {

					 includePartNumberSearch = true;
					 singleKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
					 //singleKeyword = "("+ProductHunterSolr.buildKeyword(keyWord)+")";
					 System.out.println("Single Query Keyword : " + singleKeyword);
					 
				 
				 }
				 
				 String queryString ="";
				if(includePartNumberSearch)
					 queryString   = singleKeyword;
					else
						queryString = multiKeyword;
				 
			narrowQuery.add(queryString);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			return narrowQuery;
	
	}
	
	
	
	
	public static ProductsModel solrSearchResult(String narrowKeyword, String queryString,String connUrl,String[] attrFtr,int fromRow,int resultPerPage,String innerUrl,String fields[],String origKeyWord,String sortField,ORDER sortOrder, boolean includePartNumberSearch,boolean isAdvancedSearch, ArrayList<ProductsModel> itemLevelFilterData, String customerPartIdList, String[] attrFtrGlobal, String fq, LinkedHashMap<String, ArrayList<String>> filteredMultList,boolean wildCardSearch,boolean orSearch,boolean singleItem, boolean isProductGroup,String viewFrequentlyPurcahsedOnly,boolean isPromotion, boolean isExcludeItems, String elevateIds, String excludeItems, BannerEntity bannerEntity, boolean isBrandBoost, String brandBoost)
	{
		HttpSolrServer server = null;
		ProductsModel solrSearchVal = new ProductsModel();
		try
		{
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SINGLETON_SOLR")).equalsIgnoreCase("Y")){
				server = MainItem.getInstance().getConnection();
			}else { 
				server = ConnectionManager.getSolrClientConnection(connUrl);
			}
			SolrQuery query = new SolrQuery();
			String idList = "";
			String c = "";
			String[] narrowrAttrFtr = attrFtr;
			String changeRequestHandler = (String)CommonDBQuery.getSystemParamtersList().get("CHANGE_REQUEST_HANDLER");
			if(CommonUtility.customServiceUtility()!=null)
			{
				isProductGroup = CommonUtility.customServiceUtility().enableMainItemWPEnable(attrFtrGlobal, isProductGroup);
			}

		    // specific to Pervis : when  getting two word query , it will go as wildcard query PI-960
		       // specific to Pervis : when  getting two word query , it will go as wildcard query , PI-960
			 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WILD_CARD_SEARCH")).equalsIgnoreCase("Y")) {
				 /**Habegger Custom Service for wild card search for more than 2 keywords*/
					if(CommonUtility.customServiceUtility() != null && CommonUtility.customServiceUtility().modifySearchString(queryString,origKeyWord)!=null) {
						query.setQuery(CommonUtility.customServiceUtility().modifySearchString(queryString,origKeyWord));
	    			}else {
	            int queryLength = queryString.split(" ").length;
	            	if(queryLength == 2) {
	                   
	                   String queryStringParsed = ClientUtils.escapeQueryChars(queryString);
	                   String parsedQuery= "partnumbersearch:*"+queryStringParsed+"* OR _query_:\"{!edismax}"+queryStringParsed+ "\"";
	                   query.setQuery(parsedQuery);
	            	}else {
		            	query.setQuery(queryString);
	   			 }
	    			}
			 }else {
	            	query.setQuery(queryString);
			 }
			 if(SearchConfigUtility.getQueryFields()!=null){
				 query.set("qf", SearchConfigUtility.getQueryFields());
			 }
			 
				//query.addSortField("goldenitem", SolrQuery.ORDER.desc );
				if(CommonUtility.validateString(sortField).length()>0 && sortField.equalsIgnoreCase("default")){
					String defaultSort = CommonDBQuery.getSystemParamtersList().get("DEFAULT_SEARCH_SORTING");
					if(CommonUtility.validateString(defaultSort).length()>0){
						if(defaultSort.trim().equalsIgnoreCase("elevate")){
							query.set("forceElevation", "true");	
						}
						else{
							query.set("sort", defaultSort);	
						}
							
					}else{
						query.set("sort", "score desc");
					}
					
				}else{
					if(CommonUtility.validateString(sortField).length()>0 && sortField.equalsIgnoreCase("elevate")){
						query.set("forceElevation", "true");	
					}
					else{
						query.addSortField(sortField, sortOrder);
					}
						
				}
				
				if ((isPromotion) || (isExcludeItems)) {
			        query.setRequestHandler("/elevate");
			        query.set("forceElevation", new String[] { "true" });
			        if (isPromotion) {
			          query.set("elevateIds", new String[] { elevateIds });
			        }
			        if (isExcludeItems)
			          query.set("excludeIds", new String[] { excludeItems });
				}else if(CommonUtility.validateString(changeRequestHandler).equalsIgnoreCase("Y") && !CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE).equalsIgnoreCase("/mainitem_keywordsearch") && isProductGroup) {
						 query.setRequestHandler(CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE));
			     }else if (CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y")) {
				       query.setRequestHandler("/mainitem_keywordsearch");
				        System.out.println("requestHandler"+query.getRequestHandler());
			     }else{
					 query.set("defType", "edismax"); //17 May removed as it was not working for Product Group
						
						if(isAdvancedSearch)
						{
							query.set("qf", "partkeywords");
							query.set("pf", "partkeywords^50");
						}
						else
						{
							if(wildCardSearch)
							{
								query.set("qf", "partkeywords keywordswildcard");
								query.set("pf", "partkeywords^100 keywordswildcard^50");
							}
							else if(orSearch)
							{
								query.set("qf", "keywords");
								query.set("pf", "keywords^100");
							}
							else if(singleItem){
								query.set("qf", "productpartkeywords");
								query.set("pf", "productpartkeywords^100");
								
							}
							else
							{
								query.set("qf", "partkeywords keywords");
								query.set("pf", "partkeywords^100 keywords^50");
							}
							
						}
						
						query.set("ps", "0");
				 }
				if(CommonUtility.customServiceUtility()!=null)
				{
					query = CommonUtility.customServiceUtility().getquery(queryString , query);
				}
				 query.set("spellcheck", "true");
				 query.set("spellcheck.extendedResults", "true");
				 query.set("spellcheck.collate", "true");
				 query.set("spellcheck.q", origKeyWord);
				 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CLICK_STREAM_BOOST")).equalsIgnoreCase("Y")) {
					 buildBoostQuery(query,origKeyWord);//Click Stream - Machine learning
				 }
				 
				 if(isBrandBoost){
					 buildBrandBoostQuery(query, brandBoost);
				 }
				 ProductHunterUtility.getInstance().setRangeFilteQuery(query);
				 
				 if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
					 String validateForSearch[] = narrowKeyword.split("\\s+",-1);
					 if(validateForSearch.length>1){
						 query.addFilterQuery("{!edismax}"+narrowKeyword);
						 fq = fq + "~{!edismax}"+narrowKeyword;
						 
					 }else{
						 query.addFilterQuery("{!edismax}*"+narrowKeyword+"* OR " + narrowKeyword);
						 fq = fq + "~{!edismax}"+narrowKeyword+"* OR " + narrowKeyword;
					 }
				 }
				
				//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				if(fields!=null && fields.length>0)
				{
					query.addFacetField(fields);
					query.setFacetLimit(-1);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
						query.setFacetSort(true);
					}else{
						query.setFacetSort(false);
					}
				}
				query.setStart(fromRow);
				query.setRows(resultPerPage);
				
				
				
				query.addFilterQuery(attrFtr);
				
				/*if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")).equalsIgnoreCase("Y") && !singleItem && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_PRODUCT_MODE_IN_SEARCH")).equalsIgnoreCase("Y") && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_VERSION")).equalsIgnoreCase("10")){
					if(!viewFrequentlyPurcahsedOnly.equalsIgnoreCase("Y")){
						query.addFilterQuery("defaultProductItem:Y");
					}else{
						query.addFilterQuery("defaultProductItem:(Y OR N)");
					}
					
				}*/
				/*if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_CHILD_PRODUCT")).equalsIgnoreCase("Y")){
				     query.setQuery(queryString.replace("partkeywords:", ""));
				     //query.set("qf", "partnumbersearch^100 manfpartnumbersearch^1.5 description^1.5 upc^0.25 catPathSearch^0.25 brandSearch^0.25 manufacturerSearch^0.25");
				     //query.set("pf", "partnumbersearch^100 manfpartnumbersearch^1.5 description^1.5 upc^0.25 catPathSearch^0.25 brandSearch^0.25 manufacturerSearch^0.25");
				}*/
				if(customerPartIdList!=null && !customerPartIdList.trim().equalsIgnoreCase(""))
				{
				    query.addFilterQuery("-itemid:("+customerPartIdList+")");  
				}
				
				/*if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")).equalsIgnoreCase("Y")){
					if(!isProductGroup){
						query.addFilterQuery("{!collapse field=productId nullPolicy=expand}");
					}
					
				}*/
				if(!siteNameSolr.equalsIgnoreCase(""))
                {
                	query.set("customerName",siteNameSolr);
                	query.set("q.original",origKeyWord);
                }
				 QueryResponse response = new QueryResponse();
				if(CommonDBQuery.getSystemParamtersList().get("PARTNUMBER_EXACT_SEARCH")!=null && CommonDBQuery.getSystemParamtersList().get("PARTNUMBER_EXACT_SEARCH").trim().equalsIgnoreCase("Y") && (!isPromotion && !isExcludeItems)){
				System.out.println("keyword is" + origKeyWord );
				String partNumberExactMatch = "partnumberexact:\"" + origKeyWord + "\"";
                query.setQuery(partNumberExactMatch);

                int partNumberResultCount= 0;
                int keyWordLength = 0;
                if(CommonUtility.validateString(origKeyWord).length()>0) {
                	keyWordLength = origKeyWord.split(" ").length;
				}
                if(keyWordLength==1) {
                	System.out.println("queryParNumberExact : " + query);
	                 response  = server.query(query,METHOD.POST);
	                  partNumberResultCount = (int) response.getResults().getNumFound();
                }
	                 if(partNumberResultCount == 0){
	                	 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WILD_CARD_SEARCH")).equalsIgnoreCase("Y")) {
	        				 /**Habegger Custom Service for wild card search for more than 2 keywords*/
	        					if(CommonUtility.customServiceUtility() != null && CommonUtility.customServiceUtility().modifySearchString(queryString,origKeyWord)!=null) {
	        						query.setQuery(CommonUtility.customServiceUtility().modifySearchString(queryString,origKeyWord));
	        	    			}else {
	        	            int queryLength = queryString.split(" ").length;
	        	            	if(queryLength == 2) {
	        	                   
	        	                   String queryStringParsed = ClientUtils.escapeQueryChars(queryString);
	        	                   String parsedQuery= "partnumbersearch:*"+queryStringParsed+"* OR _query_:\"{!edismax}"+queryStringParsed+ "\"";
	        	                   query.setQuery(parsedQuery);
	        	            	}else {
	        		            	query.setQuery(queryString);
	        	   			 }
	        	    			}
	        			 }else {
	        	            	query.setQuery(queryString);
	        			 }
	                	 System.out.println("query : " + query);
	                	 response = server.query(query,METHOD.POST); 
	                	}
				}
				
				
				SolrDocumentList documents = null;
				int resultCount = 0;
				List<RangeFacet> facetRanges = null;
				List<FacetField> facetFeild = null;
				LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();
				response = server.query(query,METHOD.POST);
				facetFeild = response.getFacetFields();
				documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				// overriding the new request handler
				if(CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y") && !CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE).equalsIgnoreCase("/mainitem_keywordsearch")) {
					 query.setRequestHandler(CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE));
					 QueryResponse response1 = SolrService.getInstance().executeSolrQuery(ProductHunterSolrV2.solrURL+"/mainitemdata", query);
					 facetFeild = response1.getFacetFields();
					 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_PRODUCT_MODE")).equalsIgnoreCase("Y")) {
						 documents = response1.getResults();
						 resultCount = (int) response1.getResults().getNumFound();
					 }
				}
				System.out.println("query : " + query);
				/*if(resultCount>0 && narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					String narrowKeywordOrg = narrowKeyword;
					boolean narrowOrSearch = true;
					 narrowKeyword = narrowKeyword.replaceAll("\\s+", " AND ");
					
					String pattern = "[^A-Za-z0-9]";
					narrowKeyword = narrowKeyword.toUpperCase();
					String scrubbedKeyword = narrowKeyword.replaceAll(pattern,"");
				
					 narrowKeyword = narrowKeyword.trim();
					
					 ArrayList<String> buildNarrowKeyword = buildNarrowKeyword(narrowKeyword, scrubbedKeyword);
					 
					 
					 for(String narrowQuery: buildNarrowKeyword)
					 {
						 solrSearchVal = solrNarrowSearchResult(narrowQuery, queryString, connUrl, narrowrAttrFtr, fromRow, resultPerPage, innerUrl, fields, origKeyWord, sortField, sortOrder,includePartNumberSearch,false);
							if(solrSearchVal.getItemDataList().size()>0)
							{
								narrowOrSearch = false;
								break;
							}
					 }
					 
					 
					 if(narrowOrSearch)
					 {
						 narrowKeyword = narrowKeywordOrg.toUpperCase();
					  		scrubbedKeyword = narrowKeyword.replaceAll(pattern,"");
						
						 narrowKeyword = narrowKeyword.trim();
			
						
						buildNarrowKeyword = buildNarrowKeyword(narrowKeyword, scrubbedKeyword);
						 
						 
						 for(String narrowQuery: buildNarrowKeyword)
						 {
							 solrSearchVal = solrNarrowSearchResult(narrowQuery, queryString, connUrl, narrowrAttrFtr, fromRow, resultPerPage, innerUrl, fields, origKeyWord, sortField, sortOrder,includePartNumberSearch,false);
								if(solrSearchVal.getItemDataList().size()>0)
								{
									break;
								}
						 }
					 }
					 
					
				}
				
				else
				{*/
					SpellCheckResponse spellCheckRes = response.getSpellCheckResponse();
					if(spellCheckRes!=null){
						System.out.println(spellCheckRes.isCorrectlySpelled());
						solrSearchVal.setSuggestedValue(spellCheckRes.getCollatedResult());
						System.out.println("DOCUMENTS Suggestion : " + spellCheckRes.getCollatedResult());
						
						if(!spellCheckRes.getSuggestions().isEmpty()){
							Iterator<Suggestion> suggestionArray = spellCheckRes.getSuggestions().iterator();
							if(suggestionArray!=null){
								List<String> suggestionList = suggestionArray.next().getAlternatives();
								solrSearchVal.setSuggestedValueList(new ArrayList<String>(suggestionList));
							}
							
						}
					}
					Iterator<SolrDocument> itr = documents.iterator();
					System.out.println("DOCUMENTS");
					idList = "";
					c = "";
					//facetFeild = response.getFacetFields();
					//facetRanges = response.getFacetRanges();
					attributeList = new LinkedHashMap<String, List<Count>>();

					LinkedHashMap<String, ArrayList<ProductsModel>> filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
					ArrayList<ProductsModel>  attrList = new ArrayList<ProductsModel>();
					
					ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
					
					System.out.println("DOCUMENTS");
					
					//itemLevelFilterData = new ArrayList<ProductsModel>();
					
					boolean appendResult = false;
					int prevCount = 0;
					if(itemLevelFilterData.size()>0)
					{
						prevCount = itemLevelFilterData.get(0).getResultCount();
						resultCount = resultCount + prevCount;
						itemLevelFilterData.get(0).setResultCount(resultCount);
					}
					
					while (itr.hasNext()) {
						
						if(resultPerPage>0)
						{
						
							try{
							ProductsModel itemModel = new ProductsModel();
							SolrDocument doc = itr.next();
							idList = idList + c + doc.getFieldValue("itemid").toString();
							itemModel.setBannerEntity(bannerEntity);
							itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
							itemModel.setMinOrderQty(1);
							itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
							itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
							itemModel.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
							//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
							itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
							itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
							itemModel.setManufacturerId(CommonUtility.validateNumber(ProductHunterSolr.getSolrFieldValue(doc,"manfId")));
							itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
							itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
								
							itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
							itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
							itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
							itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
							itemModel.setProductName((doc.getFieldValue("productName")!=null?doc.getFieldValue("productName").toString():""));
							itemModel.setProductDescription((doc.getFieldValue("productDesc")!=null?doc.getFieldValue("productDesc").toString():""));
							itemModel.setProductCategoryImageName((doc.getFieldValue("productImageName")!=null?doc.getFieldValue("productImageName").toString():""));
							itemModel.setProductCategoryImageType((doc.getFieldValue("productImageType")!=null?doc.getFieldValue("productImageType").toString():""));
							itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
							itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
							itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
							itemModel.setItemType(CommonUtility.validateString(ProductHunterSolr.getSolrFieldValue(doc,"itemType")));
							itemModel.setRentals(CommonUtility.validateString(ProductHunterSolr.getSolrFieldValue(doc,"rentals")));
							if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB").trim().equalsIgnoreCase("Y"))
							{
								if(doc.getFieldValue("catSearchId")!=null && doc.getFieldValue("categoryNamePath")!=null){
									itemModel.setItemBreadCrumb(getItemBreadCrumb((List<Integer>) doc.getFieldValue("catSearchId"),(String) doc.getFieldValue("categoryNamePath")));
								}
								
							}
							if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
							{
								if(doc.getFieldValue("productItemCount")!=null){
									itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
								}
								if(doc.getFieldValue("productId")!=null){
									itemModel.setProductId(CommonUtility.validateNumber(doc.getFieldValue("productId").toString()));
								}
								
							}
							if(doc.getFieldValue("categoryID")!=null){
								itemModel.setCategoryCode((doc.getFieldValue("categoryID")!=null?doc.getFieldValue("categoryID").toString():""));
							}
							
							if(doc.getFieldValue("custom_Catalog_ID")!=null)
							{
								itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());

							}
							
							itemModel.setResultCount(resultCount);
							String imageName = null;
			       			String imageType = null;
			       			
			       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
			       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
			       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
			       			}
			       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
			       			String strRegEx = "&[^;]*;";
			       			String pattern = "[^A-Za-z0-9]";
			   			 itemUrl = itemUrl.replaceAll(strRegEx," ").replaceAll(pattern," ");
			   			 itemUrl = itemUrl.replaceAll("\\s+","-");
			            	itemModel.setItemUrl(itemUrl);
			       			
			            	Map<String, Object> customFieldValMap = doc.getFieldValueMap();
			            	
			            	if(doc.getFieldValue("orderQtyIntervalByShipMethod")!=null && doc.getFieldValue("orderQtyIntervalByShipMethod").toString().trim().length()>0){
								LinkedHashMap<String, ProductsModel>shipOrderQtyAndInterval =  ProductHunterSolr.getOrderQtyIntervalByShipMethod(doc.getFieldValue("orderQtyIntervalByShipMethod").toString());
								itemModel.setShipOrderQtyAndIntervalFieldVal(shipOrderQtyAndInterval);
							}
							
							LinkedHashMap<String, Object> customFieldVal = ProductHunterSolr.getAllCustomFieldVal(customFieldValMap);
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
						}
						else if(!appendResult && itemLevelFilterData.size()>0)
						{
							itemLevelFilterData.get(0).setResultCount(resultCount);
							appendResult = true;
						}
						
						
					}
					if(itemLevelFilterData.size()>0)
					{
						
						
						 LinkedHashMap<String, ArrayList<ProductsModel>> multiFaucetResult = new LinkedHashMap<String, ArrayList<ProductsModel>>();
							if(resultCount>0 && attrFtrGlobal!=null && attrFtrGlobal.length>0)
							{
								multiFaucetResult = ProductHunterSolrUltimate.generateFaucetFilter(queryString, attrFtrGlobal, fq,true,isAdvancedSearch,null,"defaultCategory:Y",filteredMultList);
								
								if(multiFaucetResult!=null && multiFaucetResult.get("price")!=null)
								{
									ArrayList<ProductsModel> fRange = multiFaucetResult.get("price");
									if(fRange!=null && fRange.size()>0 && fRange.get(0).getFacetRange()!=null && fRange.get(0).getFacetRange().size()>0)
										facetRanges = fRange.get(0).getFacetRange();
									
								}
								
							}
							
						/*for(FacetField facetFilter:facetFeild)
						{
							ProductsModel tempVal = new ProductsModel();
							
							if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
							{
								filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
								attrList = new ArrayList<ProductsModel>();
								if(multiFaucetResult!=null && multiFaucetResult.get(facetFilter.getName().replace("attr_", ""))!=null)
								{
									
									filteredList.put(facetFilter.getName().replace("attr_", ""), multiFaucetResult.get(facetFilter.getName().replace("attr_", "")));
									tempVal.setAttrFilterList(filteredList);
									attrFilteredList.add(tempVal);
								}
								else
								{
									List<Count> attrValArr = facetFilter.getValues();
									for(Count attrArr : attrValArr)
									{
										ProductsModel attrListVal = new ProductsModel();
										if(filteredMultList ==null)
											filteredMultList = new  LinkedHashMap<String, ArrayList<String>>();
										ArrayList<String> aList = filteredMultList.get(facetFilter.getName());
										if(aList!=null && aList.size()>0)
										{
											if(!aList.contains(attrArr.getName()))
											{
											attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
											attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace(attributePrepend, ""),"UTF-8"));
											attrListVal.setAttrValue(attrArr.getName());
											attrListVal.setResultCount((int) attrArr.getCount());
											attrList.add(attrListVal);
											}
										}
										else
										{
											attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
											attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace(attributePrepend, ""),"UTF-8"));
											attrListVal.setAttrValue(attrArr.getName());
											attrListVal.setResultCount((int) attrArr.getCount());
											attrList.add(attrListVal);
										}
										
										
									
										
									}
									if(attrList!=null && attrList.size()>0)
									{
									filteredList.put(facetFilter.getName().replace(attributePrepend, ""), attrList);
									tempVal.setAttrFilterList(filteredList);
									attrFilteredList.add(tempVal);
									attributeList.put(facetFilter.getName().replace(attributePrepend, ""), facetFilter.getValues());
									}
								}
								
							}
							
						}*/
						attrFilteredList = buildFacetFilter(facetFeild, multiFaucetResult, filteredMultList);
					}
					
					solrSearchVal.setItemDataList(itemLevelFilterData);
					solrSearchVal.setAttributeDataList(attrFilteredList);
					solrSearchVal.setFacetRange(facetRanges);
					solrSearchVal.setIdList(idList);
					
					System.out.println("At the end : " + solrSearchVal.getSuggestedValue());
				/*}*/
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SINGLETON_SOLR")).equalsIgnoreCase("Y")){
				ConnectionManager.closeSolrClientConnection(server);
			}
		}
		return solrSearchVal;
	}
	
	public static ArrayList<ProductsModel> buildFacetFilter(List<FacetField> facetFeild, LinkedHashMap<String, ArrayList<ProductsModel>> multiFaucetResult,LinkedHashMap<String, ArrayList<String>> filteredMultList){
		
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
				if(isSwathAttribute(facetFilter.getName().replace(attributePrepend, "")) && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SWATCH")).trim().equalsIgnoreCase("Y") && CommonDBQuery.getSwatchValueList()!=null && CommonDBQuery.getSwatchValueList().size()>0){
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
	
	public static boolean isSwathAttribute(String attrName){
		boolean isSwatch =false;
		try{
			String swatchAttribute = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SWATCH_ATTRIBUTE"));
			if(swatchAttribute.trim().length()>0){
				String swatchAttrList[] = swatchAttribute.split(",");
				for(String swatch:swatchAttrList){
					if(swatch.trim().equalsIgnoreCase(attrName)){
						isSwatch = true;
						break;
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return isSwatch;
	}
	public static ProductsModel solrNarrowSearchResult(String narrowKeyword, String queryString,String connUrl,String[] attrFtr,int fromRow,int resultPerPage,String innerUrl,String fields[],String origKeyWord,String sortField,ORDER sortOrder)
	{
		ArrayList<ProductsModel>  itemLevelFilterData = new ArrayList<ProductsModel>();
		HttpSolrServer server = null;
		ProductsModel solrSearchVal = new ProductsModel();
		try
		{
			server = ConnectionManager.getSolrClientConnection(connUrl);
			SolrQuery query = new SolrQuery();
			String idList = "";
			String c = "";
				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
					 narrowKeyword = narrowKeyword.trim();
					 				 	
				 	int attrIndexSize = attrFtr.length;
				 	attrFtr[attrIndexSize-1] = narrowKeyword;
				 	System.out.println("Before Append : " + attrIndexSize);
				 	
				 }
				query.setQuery(queryString);
				query.addSortField("goldenitem", SolrQuery.ORDER.desc );
				query.addSortField(sortField, sortOrder);
				
				query.set("spellcheck", "true");
				 query.set("spellcheck.extendedResults", "true");
				 query.set("spellcheck.collate", "true");
				 query.set("spellcheck.q", origKeyWord);
				
				//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				if(fields!=null && fields.length>0)
				{
					query.addFacetField(fields);
					query.setFacetLimit(-1);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
						query.setFacetSort(true);
					}else{
						query.setFacetSort(false);
					}
				}
				query.setStart(fromRow);
				query.setRows(resultPerPage);
				query.setFilterQueries(attrFtr);
				if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y")){
				query.addFilterQuery("defaultProductItem:Y");
				}
				System.out.println("query : " + query);
				QueryResponse response = server.query(query);
				
				SolrDocumentList documents = response.getResults();
				int resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();

				
				SpellCheckResponse spellCheckRes = response.getSpellCheckResponse();
				if(spellCheckRes!=null){
					System.out.println(spellCheckRes.isCorrectlySpelled());
					solrSearchVal.setSuggestedValue(spellCheckRes.getCollatedResult());
					System.out.println("DOCUMENTS Suggestion : " + spellCheckRes.getCollatedResult());
					
					if(!spellCheckRes.getSuggestions().isEmpty()){
						Iterator<Suggestion> suggestionArray = spellCheckRes.getSuggestions().iterator();
						if(suggestionArray!=null){
							List<String> suggestionList = suggestionArray.next().getAlternatives();
							solrSearchVal.setSuggestedValueList(new ArrayList<String>(suggestionList));
						}
						
					}
				}
				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("DOCUMENTS");
				idList = "";
				c = "";
				facetFeild = response.getFacetFields();
				attributeList = new LinkedHashMap<String, List<Count>>();

				LinkedHashMap<String, ArrayList<ProductsModel>> filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				ArrayList<ProductsModel>  attrList = new ArrayList<ProductsModel>();
				
				ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
				
				System.out.println("DOCUMENTS");
				
				itemLevelFilterData = new ArrayList<ProductsModel>();
				while (itr.hasNext()) {
					
					try{
					
					ProductsModel itemModel = new ProductsModel();
					SolrDocument doc = itr.next();
					idList = idList + c + doc.getFieldValue("itemid").toString();
					
					itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
					itemModel.setMinOrderQty(1);
					itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
					itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
					//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
					itemModel.setManufacturerId(CommonUtility.validateNumber(ProductHunterSolr.getSolrFieldValue(doc,"manfId")));
					itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
					itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
						
					itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
					itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
					itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
					itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
					itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
					if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
					{
						if(doc.getFieldValue("productItemCount")!=null){
							itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
						}
					}
					itemModel.setResultCount(resultCount);
					String imageName = null;
	       			String imageType = null;
	       			
	       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
	       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			}
	       			
	       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	       			String pattern = "[^A-Za-z0-9]";
	   			 itemUrl = itemUrl.replaceAll(pattern," ");
	   			 itemUrl = itemUrl.replaceAll("\\s+","-");
	            	itemModel.setItemUrl(itemUrl);
	            	
	            	Map<String, Object> customFieldValMap = doc.getFieldValueMap();
					
	            	if(doc.getFieldValue("orderQtyIntervalByShipMethod")!=null && doc.getFieldValue("orderQtyIntervalByShipMethod").toString().trim().length()>0){
						LinkedHashMap<String, ProductsModel>shipOrderQtyAndInterval =  ProductHunterSolr.getOrderQtyIntervalByShipMethod(doc.getFieldValue("orderQtyIntervalByShipMethod").toString());
						itemModel.setShipOrderQtyAndIntervalFieldVal(shipOrderQtyAndInterval);
					}
	            	
					LinkedHashMap<String, Object> customFieldVal = ProductHunterSolr.getAllCustomFieldVal(customFieldValMap);
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
					
				}
				if(itemLevelFilterData.size()>0)
				{
					for(FacetField facetFilter:facetFeild)
					{
						ProductsModel tempVal = new ProductsModel();
						
						if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
						{
							filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
							attrList = new ArrayList<ProductsModel>();
					
							List<Count> attrValArr = facetFilter.getValues();
							for(Count attrArr : attrValArr)
							{
								ProductsModel attrListVal = new ProductsModel();
								attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
								attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
								attrListVal.setAttrValue(attrArr.getName());
								attrListVal.setResultCount((int) attrArr.getCount());
								attrList.add(attrListVal);
								
							}
							
							filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
							tempVal.setAttrFilterList(filteredList);
							attrFilteredList.add(tempVal);
							attributeList.put(facetFilter.getName().replace("attr_", ""), facetFilter.getValues());
						}
						
					}
				}
				solrSearchVal.setItemDataList(itemLevelFilterData);
				solrSearchVal.setAttributeDataList(attrFilteredList);
				solrSearchVal.setIdList(idList);
				
				System.out.println("At the end : " + solrSearchVal.getSuggestedValue());
		 
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return solrSearchVal;
	}
	
	
	public static ProductsModel solrSearchResultCustomerPartNumber(String narrowKeyword, String queryString,String connUrl,String[] attrFtr,String[] attrFtr1,int fromRow,int resultPerPage,String innerUrl,String fields[])
	{
		ArrayList<ProductsModel>  itemLevelFilterData = new ArrayList<ProductsModel>();
		HttpSolrServer server1 = null;
		HttpSolrServer server2 = null;
		ProductsModel solrSearchVal = new ProductsModel();
		try
		{
			String idList = "";
			String c = "";
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SINGLETON_SOLR")).equalsIgnoreCase("Y")){
				server1 = CustomerData.getInstance().getConnection();
			}else {
				server1 =  ConnectionManager.getSolrClientConnection(connUrl);
			}
	  		
	  		SolrQuery query = new SolrQuery();
			query = new SolrQuery();
			boolean isCustomerData = false;
			query.setQuery(queryString);
			
			
			if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
			 {
				 narrowKeyword = narrowKeyword.trim();
				 narrowKeyword = escapeQueryCulprits(narrowKeyword);
			 	
			 	int attrIndexSize = attrFtr1.length;
			 	
			 	attrFtr1[attrIndexSize-1] = "customerPartNumber: *"+narrowKeyword+"*";
			 	System.out.println("Before Append : " + attrIndexSize);
			 	
			 }
			//System.out.println("after Append : " + attrFtr1.length);
			//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
			
			query.setStart(fromRow);
			query.setRows(resultPerPage);
			query.setFilterQueries(attrFtr1);

			System.out.println("Competator Query : " + query);
			QueryResponse response = server1.query(query,METHOD.POST);
			
			SolrDocumentList documents = response.getResults();
			int resultCount = (int) response.getResults().getNumFound();
			List<FacetField> facetFeild = response.getFacetFields();
			LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();

			LinkedHashMap<String, ArrayList<ProductsModel>>  filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
			ArrayList<ProductsModel> attrList = new ArrayList<ProductsModel>();
			
      		ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
			Iterator<SolrDocument> itr = documents.iterator();
			System.out.println("DOCUMENTS");
			
			  
			itemLevelFilterData = new ArrayList<ProductsModel>();
			while (itr.hasNext()) {
				
				
				SolrDocument doc = itr.next();
				idList = idList + c + doc.getFieldValue("itemid").toString();
				
				
				c = " OR ";
				
				isCustomerData = true;
			}
			
		
			
			if(isCustomerData)
			{
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SINGLETON_SOLR")).equalsIgnoreCase("Y")){
					server2 = MainItem.getInstance().getConnection();
				}else {
					server2 = ConnectionManager.getSolrClientConnection(innerUrl);
				}				
				server2.setParser(new XMLResponseParser());
				QueryRequest req2 = new QueryRequest();
				req2.setMethod(METHOD.POST);
				SolrQuery query2 = new SolrQuery();
				query2.setQuery("itemid:("+idList+")");
				//String fq3 = "type:"+indexType;
				if(fields!=null && fields.length>0)
				{
					query.addFacetField(fields);
					query.setFacetLimit(-1);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
						query.setFacetSort(true);
					}else{
						query.setFacetSort(false);
					}
				}
				query2.setFilterQueries(attrFtr);
				query2.setStart(0);
				query2.setRows(12);
				
				QueryResponse response2 = server2.query(query2,METHOD.POST);
				facetFeild = response2.getFacetFields();
				SolrDocumentList documents2 = response2.getResults();
				int itemResultCount = (int) response2.getResults().getNumFound();
				
				idList = "";
				c = "";
				
				Iterator<SolrDocument> itr2 = documents2.iterator();
				NumberFormat.getCurrencyInstance(Locale.US);
				while (itr2.hasNext()) {
					
					try{
					
					ProductsModel itemModel = new ProductsModel();
					SolrDocument doc = itr2.next();
					idList = idList + c + doc.getFieldValue("itemid").toString();
					itemModel.setItemResultCount(itemResultCount);
					itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
					itemModel.setMinOrderQty(Integer.parseInt(doc.getFieldValue("minordqty") != null ? doc.getFieldValue("minordqty").toString() : "1"));
					itemModel.setOrderInterval(Integer.parseInt(doc.getFieldValue("orderQtyInterval") != null ? doc.getFieldValue("orderQtyInterval").toString() : "1"));
					itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
					itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
					//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
					itemModel.setManufacturerId(CommonUtility.validateNumber(ProductHunterSolr.getSolrFieldValue(doc,"manfId")));
					itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
					itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
						
					itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
					itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
					itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
					itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
					itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
					itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
					itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
					if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB").trim().equalsIgnoreCase("Y"))
					{
						if(doc.getFieldValue("catSearchId")!=null && doc.getFieldValue("categoryNamePath")!=null)
						itemModel.setItemBreadCrumb(getItemBreadCrumb((List<Integer>) doc.getFieldValue("catSearchId"),(String) doc.getFieldValue("categoryNamePath")));
						
					}
					if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
					{
						if(doc.getFieldValue("productItemCount")!=null){
							itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
						}
						if(doc.getFieldValue("productId")!=null){
							itemModel.setProductId(CommonUtility.validateNumber(doc.getFieldValue("productId").toString()));
						}
					}
					if(doc.getFieldValue("categoryID")!=null){
						itemModel.setCategoryCode((doc.getFieldValue("categoryID")!=null?doc.getFieldValue("categoryID").toString():""));
					}
					if(doc.getFieldValue("custom_Catalog_ID")!=null)
					{
						itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());

					}
					//itemModel.setResultCount(resultCount); // issue found in count and result missmatch when searched for CPN
					itemModel.setResultCount(itemResultCount);
					String imageName = null;
	       			String imageType = null;
	       			
	       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
	       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			}
	       			
	       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	       			String pattern = "[^A-Za-z0-9]";
	   			 itemUrl = itemUrl.replaceAll(pattern," ");
	   			 itemUrl = itemUrl.replaceAll("\\s+","-");
	            	itemModel.setItemUrl(itemUrl);
	            	
	            	Map<String, Object> customFieldValMap = doc.getFieldValueMap();
					
	            	if(doc.getFieldValue("orderQtyIntervalByShipMethod")!=null && doc.getFieldValue("orderQtyIntervalByShipMethod").toString().trim().length()>0){
						LinkedHashMap<String, ProductsModel>shipOrderQtyAndInterval =  ProductHunterSolr.getOrderQtyIntervalByShipMethod(doc.getFieldValue("orderQtyIntervalByShipMethod").toString());
						itemModel.setShipOrderQtyAndIntervalFieldVal(shipOrderQtyAndInterval);
					}
	            	
					LinkedHashMap<String, Object> customFieldVal = ProductHunterSolr.getAllCustomFieldVal(customFieldValMap);
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
					
				}
				  
				   
				for(FacetField facetFilter:facetFeild)
				{
					ProductsModel tempVal = new ProductsModel();
					
					if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
					{
						filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
						attrList = new ArrayList<ProductsModel>();
				
						List<Count> attrValArr = facetFilter.getValues();
						for(Count attrArr : attrValArr)
						{
							ProductsModel attrListVal = new ProductsModel();
							attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
							attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
							attrListVal.setAttrValue(attrArr.getName());
							attrListVal.setResultCount((int) attrArr.getCount());
							attrList.add(attrListVal);
							
						}
						
						filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
						tempVal.setAttrFilterList(filteredList);
						attrFilteredList.add(tempVal);
						attributeList.put(facetFilter.getName().replace("attr_", ""), facetFilter.getValues());
					}
					
				}
				
			}
			solrSearchVal.setItemDataList(itemLevelFilterData);
			solrSearchVal.setAttributeDataList(attrFilteredList);
			solrSearchVal.setIdList(idList);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SINGLETON_SOLR")).equalsIgnoreCase("Y")){
				ConnectionManager.closeSolrClientConnection(server1);
				ConnectionManager.closeSolrClientConnection(server2);
			}
		}
		return solrSearchVal;
	}
	
	
	public static ProductsModel checkCategoryExist(String connUrl,String queryString,String indexType)
	{
		 HttpSolrServer server1 = null;
		 ProductsModel categoryResult = null;
		    int idx = 0;
		    int count = 0;
		    ArrayList<ProductsModel> categoryList = new ArrayList<ProductsModel>();
		    try
		    {
		      String prevVal = "";
		      boolean loop = true;
		      String parenCategory = "";
		      String prev = "";
		      server1 = ConnectionManager.getSolrClientConnection(connUrl);
		      SolrQuery query = new SolrQuery();
		      query = new SolrQuery();
		      query.setQuery(queryString);
		      query.setFilterQueries(new String[] { "type:" + indexType });
		      query.setStart(Integer.valueOf(0));
		      if ((CommonDBQuery.getSystemParamtersList().get("SINGLE_CATEGORY_SEARCH") != null) && (((String)CommonDBQuery.getSystemParamtersList().get("SINGLE_CATEGORY_SEARCH")).trim().equalsIgnoreCase("Y")))
		        query.setRows(Integer.valueOf(1));
		      else {
		        query.setRows(Integer.valueOf(100));
		      }

		      System.out.println("Category Query : " + query);
		      QueryResponse response = server1.query(query);
		      SolrDocumentList documents = response.getResults();
		      Iterator itr = documents.iterator();
		      System.out.println("DOCUMENTS");
		      while (itr.hasNext()) {
		        categoryResult = new ProductsModel();
		        SolrDocument doc = (SolrDocument)itr.next();
		        categoryResult.setCategoryCode(doc.getFieldValue("categoryID").toString());
		        categoryResult.setLevelNumber(CommonUtility.validateNumber(doc.getFieldValue("levelNumber").toString()));
		        categoryResult.setParentCategory(doc.getFieldValue("breadCrumb").toString());
		        categoryList.add(categoryResult);
		      }
		      if (categoryList.size() > 1) {
		        while (loop) {
		          prev = "";
		          for (ProductsModel categoryValue : categoryList) {
		            String[] categoryArray = categoryValue.getParentCategory().split(" > ");
		            if ((categoryArray != null) && (categoryArray.length > idx))
		            {
		              if (prev.equalsIgnoreCase("")) {
		                prev = categoryArray[idx];
		              } else if (prev.equalsIgnoreCase(categoryArray[idx])) {
		                parenCategory = categoryArray[idx];
		                System.out.println("last found : " + prev);
		                count++;
		              } else {
		                loop = false;
		              }
		            }
		            else {
		              loop = false;
		            }
		          }

		          idx++;
		        }

		        if (count > 1) {
		          System.out.println("Final : " + parenCategory + " - " + idx);
		          String[] facetFilterplit = parenCategory.split("\\{");
		          if (facetFilterplit.length > 1) {
		            categoryResult = new ProductsModel();
		            categoryResult.setCategoryCode(facetFilterplit[1].replace("}", ""));
		          }
		        }
		      }

		    }catch (Exception e){
		      e.printStackTrace();
		    }finally {
				ConnectionManager.closeSolrClientConnection(server1);
			}
		    return categoryResult;
	}
	
	
	public static String[] getProductSelectableAttribute(String connUrl,String categoryName,String queryString,String innerUrl,int productId, ArrayList<ProductsModel> refinedList,boolean isPriceRange,boolean isBrand,boolean isSubCategory,boolean isDifferentiator,boolean isSelectable,ArrayList<String> defaultFacet,ArrayList<String> arrayList){
		String faucetFieldList[] = null;
		HttpSolrServer server1 = null;
		String attributePrepend = "attr_";
		if(CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE")!=null && CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE").trim().equalsIgnoreCase("Y")){
			attributePrepend = "attr_Multi_";
		}
		try
		{
				server1 =  ConnectionManager.getSolrClientConnection(connUrl);
		  		SolrQuery query = new SolrQuery();
				query = new SolrQuery();
				query.setQuery("productId:"+productId);
				
				query.setStart(0);
				query.setRows(100);
				System.out.println("ProductAttribute Query : " + query);
				QueryResponse response = server1.query(query);
				
				SolrDocumentList documents = response.getResults();
				Iterator<SolrDocument> itr = documents.iterator();
				int resultCount = (int) response.getResults().getNumFound();
				int i = 0;
				if(resultCount>0){
					faucetFieldList = new String[resultCount];
					System.out.println("DOCUMENTS");
					while (itr.hasNext()) {
						SolrDocument doc = itr.next();
						faucetFieldList[i] = attributePrepend+doc.getFieldValue("attrName").toString();
						i++;
					}
				}
		
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server1);
		}
		return faucetFieldList;
	
	
		
	}
	
	
	public static String[]  getFaucetFieldByName(String connUrl,String categoryName,String queryString,String innerUrl,int treeId, ArrayList<ProductsModel> refinedList,boolean isPriceRange,boolean isBrand,boolean isSubCategory,boolean isDifferentiator,boolean isSelectable,ArrayList<String> defaultFacet,ArrayList<String> arrayList)
	{
		HttpSolrServer server1 = null;
		HttpSolrServer server2 = null;
		String faucetFieldList[] = null;
		String enableMultifilter = "N";
		boolean multiCategoryAttribute = false;
		boolean fromSearch = false;
		String multiCategoryId = "";
		int attrSize = 0;
		if(arrayList!=null && arrayList.size()>0)
			attrSize = arrayList.size();
		//CommonUtility.validateString()
		if(CommonDBQuery.getSystemParamtersList().get("ENABLE_MULTIFILTER")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_MULTIFILTER").trim().equalsIgnoreCase("Y"))
		{
			enableMultifilter = "Y";
		}
		
		if(CommonDBQuery.getSystemParamtersList().get("MULTI_CATEGORY_ATTRIBUTE")!=null && CommonDBQuery.getSystemParamtersList().get("MULTI_CATEGORY_ATTRIBUTE").trim().equalsIgnoreCase("Y"))
		{
			multiCategoryAttribute = true;
		}
		try
		{
			boolean isCustomerData = false;
			if(treeId==0)
			{
				String idList = "";
				String c = "";
		  		server1 =  ConnectionManager.getSolrClientConnection(connUrl);
		  		SolrQuery query = new SolrQuery();
				query = new SolrQuery();
				query.setQuery(queryString);
				
				query.setStart(0);
				query.setRows(100);
				System.out.println("Category Query : " + query);
				QueryResponse response = server1.query(query);
				
				SolrDocumentList documents = response.getResults();
				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("DOCUMENTS");
				while (itr.hasNext()) {
					SolrDocument doc = itr.next();
					idList = doc.getFieldValue("categoryID").toString();
					treeId = CommonUtility.validateNumber(idList);
					multiCategoryId = multiCategoryId + c + treeId;
					c = " OR ";
					isCustomerData = true;
				}
				fromSearch = true;
			}
			else
			{
				isCustomerData = true;
			}
			
			if(isCustomerData)
			{
				
				boolean addSortBy = false;
				String sequenceField = "attrNameSeq";
				ORDER sortOrder = null;
				String sortType =  CommonDBQuery.getSystemParamtersList().get("FILTER_ATTRIBUTE_ORDER_BY");
				if(sortType!=null && sortType.trim().equalsIgnoreCase("DISPLAY_SEQUENCE_ASC"))
				{
					sequenceField = "displaySeq";
					sortOrder = SolrQuery.ORDER.asc;
					addSortBy = true;
				}
				else if(sortType!=null && sortType.trim().equalsIgnoreCase("DISPLAY_SEQUENCE_DESC"))
				{
					sequenceField = "displaySeq";
					sortOrder = SolrQuery.ORDER.desc;
					addSortBy = true;
				}
				else if(sortType!=null && sortType.trim().equalsIgnoreCase("ATTRIBUTE_NAME_ASC"))
				{
					sequenceField = "attrNameSeq";
					sortOrder = SolrQuery.ORDER.asc;
					addSortBy = true;
				}
				else if(sortType!=null && sortType.trim().equalsIgnoreCase("ATTRIBUTE_NAME_DESC"))
				{
					sequenceField = "attrNameSeq";
					sortOrder = SolrQuery.ORDER.desc;
					addSortBy = true;
				}
				
				server2 = ConnectionManager.getSolrClientConnection(innerUrl);
				server2.setParser(new XMLResponseParser());
				QueryRequest req2 = new QueryRequest();
				req2.setMethod(METHOD.POST);
				SolrQuery query2 = new SolrQuery();
				if(multiCategoryAttribute && fromSearch){
					
					query2.setQuery("categoryID:("+multiCategoryId+")");
				}else{
					query2.setQuery("categoryID:"+treeId);
				}
				
				if(isDifferentiator)
					query2.addFilterQuery("differentiatorAttribute:\"Y\"");
				if(addSortBy)
					query2.addSortField(sequenceField, sortOrder );
				query2.setStart(0);
				query2.setRows(200);
				QueryResponse response2 = server2.query(query2);
				
				SolrDocumentList documents2 = response2.getResults();
				int resultCount = (int) response2.getResults().getNumFound();
				int faucetSize =(resultCount);
					
				if(refinedList.size()>0 && enableMultifilter.equalsIgnoreCase("N"))
				{
					if(isPriceRange){
						faucetSize = faucetSize + 1;
					}
					faucetSize = faucetSize + attrSize;;
					faucetSize = faucetSize - refinedList.size();
				}
				else
				{
					faucetSize = faucetSize +defaultFacet.size();
				}
				if(isSelectable)
					faucetSize = resultCount;
				faucetFieldList = new String[faucetSize];
				Iterator<SolrDocument> itr2 = documents2.iterator();
				int i = 0;
				
				for(String defaultAttr : defaultFacet)
				{
					faucetFieldList[i] = defaultAttr;
					i++;
				}
			/*	boolean addCategory = true;
				boolean addBrand = true;
				boolean addManufacturer = true;
				
				if(isCategory && isBrand && isSubCategory)
				{
					addCategory = false;
					addBrand = false;
					addManufacturer = false;
				}
				
				if(isCategory)
				{
					addCategory = false;
				}
				 if(isBrand)
				{
					addBrand = false;
				}
				if(isSubCategory)
				{
					addManufacturer = false;
				}

				if(addCategory)
				{
					faucetFieldList[i] = "category";
					i++;
				}
				if(addManufacturer)
				{
					faucetFieldList[i] = "attr_Sub Category";
					i++;
				}
				if(addBrand)
				{
					faucetFieldList[i] = "brand";
					i++;
				}*/
				
				String attributePrepend = "attr_";
				
				/*if(CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE")!=null && CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE").trim().equalsIgnoreCase("Y")){
					attributePrepend = "attr_Multi_";
				}*/
					
				while (itr2.hasNext()) { 
					SolrDocument doc = itr2.next();
					boolean isRemove = false;
					
					
					if(enableMultifilter.trim().equalsIgnoreCase("N"))
					{
						for(ProductsModel removeFaucet:refinedList)
						{
							
							if(!doc.getFieldValue("attrName").toString().trim().equalsIgnoreCase("Sub Category"))
							{
								if(doc.getFieldValue("attrName").toString().trim().equalsIgnoreCase(removeFaucet.getAttrName().replaceAll(attributePrepend, "")))
								{
									isRemove = true;
									break;
								}
							}
						}
					}
					
					if(!isRemove)
					{
						if(arrayList!=null && arrayList.size()>0 && (arrayList.contains(attributePrepend+doc.getFieldValue("attrName").toString())|| arrayList.contains("attr_"+doc.getFieldValue("attrName").toString())))
						{
							faucetFieldList[i] = "attr_Attribute";
						}
						else
						{
							if(doc.getFieldValue("attrName").toString().trim().equalsIgnoreCase("Sub Category"))
							{
								faucetFieldList[i] = "attr_nocategory";
							}
							else
							{
								faucetFieldList[i] = attributePrepend+doc.getFieldValue("attrName").toString();
							}
						}
						
						
						i++;
					}
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server1);
			ConnectionManager.closeSolrClientConnection(server2);
		}
		
		return faucetFieldList;
	}
	
	
	
	public static String[]  getFaucetFieldForSearch(String queryString,String innerUrl,int treeId, ArrayList<ProductsModel> refinedList,boolean isPriceRange,boolean isBrand,boolean isSubCategory,boolean isDifferentiator,boolean isSelectable,ArrayList<String> defaultFacet,ArrayList<String> arrayList)
	{
		
		String faucetFieldList[] = null;
		String enableMultifilter = "N";
		int attrSize = 0;
		HttpSolrServer server2 = null;
		if(arrayList!=null && arrayList.size()>0)
			attrSize = arrayList.size();
		//CommonUtility.validateString()
		if(CommonDBQuery.getSystemParamtersList().get("ENABLE_MULTIFILTER")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_MULTIFILTER").trim().equalsIgnoreCase("Y"))
		{
			enableMultifilter = "Y";
		}
		try
		{
				boolean addSortBy = false;
				String sequenceField = "attrNameSeq";
				ORDER sortOrder = null;
				String sortType =  CommonDBQuery.getSystemParamtersList().get("FILTER_ATTRIBUTE_ORDER_BY");
				if(sortType!=null && sortType.trim().equalsIgnoreCase("DISPLAY_SEQUENCE_ASC"))
				{
					sequenceField = "displaySeq";
					sortOrder = SolrQuery.ORDER.asc;
					addSortBy = true;
				}
				else if(sortType!=null && sortType.trim().equalsIgnoreCase("DISPLAY_SEQUENCE_DESC"))
				{
					sequenceField = "displaySeq";
					sortOrder = SolrQuery.ORDER.desc;
					addSortBy = true;
				}
				else if(sortType!=null && sortType.trim().equalsIgnoreCase("ATTRIBUTE_NAME_ASC"))
				{
					sequenceField = "attrNameSeq";
					sortOrder = SolrQuery.ORDER.asc;
					addSortBy = true;
				}
				else if(sortType!=null && sortType.trim().equalsIgnoreCase("ATTRIBUTE_NAME_DESC"))
				{
					sequenceField = "attrNameSeq";
					sortOrder = SolrQuery.ORDER.desc;
					addSortBy = true;
				}
				
				server2 = ConnectionManager.getSolrClientConnection(innerUrl);
				server2.setParser(new XMLResponseParser());
				QueryRequest req2 = new QueryRequest();
				req2.setMethod(METHOD.POST);
				SolrQuery query2 = new SolrQuery();
				
				query2.setQuery("*:*");
				query2.addFacetField("attrName");
				if(isDifferentiator)
					query2.addFilterQuery("differentiatorAttribute:\"Y\"");
				if(addSortBy)
					query2.addSortField(sequenceField, sortOrder );
				query2.setStart(0);
				query2.setRows(200);
				System.out.println("Search Filter Query : " + query2);
				QueryResponse response2 = server2.query(query2);
				
				SolrDocumentList documents2 = response2.getResults();
				List<FacetField> facetFeild = response2.getFacetFields();
				
				int resultCount = 0;
				if(facetFeild!=null && facetFeild.size()>0)
				{
					resultCount = facetFeild.get(0).getValueCount();
					System.out.println("resultCount : " + resultCount);
				}
				int faucetSize =(resultCount);
				
				if(refinedList.size()>0 && enableMultifilter.equalsIgnoreCase("N"))
				{
					if(isPriceRange){
						faucetSize = faucetSize + 1;
					}
					faucetSize = faucetSize + attrSize;;
					faucetSize = faucetSize - refinedList.size();
				}
				else
				{
					faucetSize = faucetSize +defaultFacet.size();
				}
				if(isSelectable)
					faucetSize = resultCount;
				faucetFieldList = new String[faucetSize];
				documents2.iterator();
				
				int i = 0;
				
				for(String defaultAttr : defaultFacet)
				{
					faucetFieldList[i] = defaultAttr;
					i++;
				}
				
				

				String attributePrepend = "attr_";
				
				/*if(CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE")!=null && CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE").trim().equalsIgnoreCase("Y")){
					attributePrepend = "attr_Multi_";
				}*/
				for(FacetField facetFilter:facetFeild)
				{
					if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
					{
						List<Count> attrValArr = facetFilter.getValues();
						for(Count attrArr : attrValArr)
						{
							
							 
						
							boolean isRemove = false;
							
							
							if(enableMultifilter.trim().equalsIgnoreCase("N"))
							{
								for(ProductsModel removeFaucet:refinedList)
								{
									
									if(!attrArr.getName().trim().equalsIgnoreCase("Sub Category"))
									{
										if(attrArr.getName().trim().equalsIgnoreCase(removeFaucet.getAttrName().replaceAll(attributePrepend, "")))
										{
											isRemove = true;
											break;
										}
									}
								}
							}
							
							if(!isRemove)
							{
								if(arrayList!=null && arrayList.size()>0 && (arrayList.contains(attrArr.getName()) || arrayList.contains(attributePrepend+attrArr.getName()) || arrayList.contains("attr_"+attrArr.getName())))
								{
									faucetFieldList[i] = "attr_Attribute";
								}
								else
								{
									if(attrArr.getName().trim().equalsIgnoreCase("Sub Category"))
									{
										faucetFieldList[i] = "attr_nocategory";
									}
									else
									{
										faucetFieldList[i] = attributePrepend+attrArr.getName();
									}
								}
								
								
								i++;
							}
							
						
							System.out.println("Search facet : " + attrArr.getName());
						}
					}
				}
			
				
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server2);
		}
		
		return faucetFieldList;
	}
	
	public static ProductsModel solrNarrowSearchResult(String narrowKeyword, String queryString,String connUrl,String[] attrFtr,int fromRow,int resultPerPage,String innerUrl,String fields[],String origKeyWord,String sortField,ORDER sortOrder, boolean includePartNumberSearch,boolean isAdvancedSearch)
	{
		ArrayList<ProductsModel>  itemLevelFilterData = new ArrayList<ProductsModel>();
		HttpSolrServer server = null;
		ProductsModel solrSearchVal = new ProductsModel();
		try
		{
			server = ConnectionManager.getSolrClientConnection(connUrl);
			SolrQuery query = new SolrQuery();
			String idList = "";
			String c = "";
				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				 {
					 narrowKeyword = narrowKeyword.trim();
					 				 	



				 	int attrIndexSize = attrFtr.length;
				 	attrFtr[attrIndexSize-1] = "*:*";
				 	System.out.println("Before Append : " + attrIndexSize);
				 	
				 }
				queryString = queryString.replaceAll("\"", "%22");

				query.setQuery(queryString + " AND ("+ narrowKeyword+")");
				query.addSortField("score", SolrQuery.ORDER.desc);
				query.addSortField(sortField, sortOrder);
				
				query.set("defType", "edismax");
			
			if(isAdvancedSearch)
			{
				query.set("qf", "partkeywords");
				query.set("pf", "partkeywords^50");
			}
			else
			{
				
				query.set("qf", "partkeywords keywords");
				query.set("pf", "partkeywords^100 keywords^50");
				
			}
			
			query.set("ps", "0");
			
				query.set("spellcheck", "true");
				 query.set("spellcheck.extendedResults", "true");
				 query.set("spellcheck.collate", "true");
				 query.set("spellcheck.q", origKeyWord);
				
				//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				if(fields!=null && fields.length>0)
				{
					query.addFacetField(fields);
					query.setFacetLimit(-1);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
						query.setFacetSort(true);
					}else{
						query.setFacetSort(false);
					}
				}
				query.setStart(fromRow);
				query.setRows(resultPerPage);
				query.setFilterQueries(attrFtr);
				if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y")){
				query.addFilterQuery("defaultProductItem:Y");
				}
				System.out.println("query : " + query);
				QueryResponse response = server.query(query);
				
				SolrDocumentList documents = response.getResults();
				int resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();

				
				SpellCheckResponse spellCheckRes = response.getSpellCheckResponse();
				if(spellCheckRes!=null){
					System.out.println(spellCheckRes.isCorrectlySpelled());
					solrSearchVal.setSuggestedValue(spellCheckRes.getCollatedResult());
					System.out.println("DOCUMENTS Suggestion : " + spellCheckRes.getCollatedResult());
					
					if(!spellCheckRes.getSuggestions().isEmpty()){
						Iterator<Suggestion> suggestionArray = spellCheckRes.getSuggestions().iterator();
						if(suggestionArray!=null){
							List<String> suggestionList = suggestionArray.next().getAlternatives();
							solrSearchVal.setSuggestedValueList(new ArrayList<String>(suggestionList));
						}
						
					}
				}
				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("DOCUMENTS");
				idList = "";
				c = "";
				facetFeild = response.getFacetFields();
				attributeList = new LinkedHashMap<String, List<Count>>();

				LinkedHashMap<String, ArrayList<ProductsModel>> filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				ArrayList<ProductsModel>  attrList = new ArrayList<ProductsModel>();
				
				ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
				
				System.out.println("DOCUMENTS");
				
				itemLevelFilterData = new ArrayList<ProductsModel>();
				while (itr.hasNext()) {
					
					try{
					
					ProductsModel itemModel = new ProductsModel();
					SolrDocument doc = itr.next();
					idList = idList + c + doc.getFieldValue("itemid").toString();
					
					itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
					itemModel.setMinOrderQty(1);
					itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
					itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
					//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
					itemModel.setManufacturerId(CommonUtility.validateNumber(ProductHunterSolr.getSolrFieldValue(doc,"manfId")));
					itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
					itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
						


					itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
					itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
					itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
					itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
					itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
					if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
					{
						if(doc.getFieldValue("productItemCount")!=null){
							itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
						}
					}
					itemModel.setResultCount(resultCount);
					String imageName = null;
	       			String imageType = null;
	       			


	       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
	       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			}
	       			
	       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	       			String pattern = "[^A-Za-z0-9]";
	   			 itemUrl = itemUrl.replaceAll(pattern," ");
	   			 itemUrl = itemUrl.replaceAll("\\s+","-");
	            	itemModel.setItemUrl(itemUrl);
	            	
	            	Map<String, Object> customFieldValMap = doc.getFieldValueMap();
					
	            	if(doc.getFieldValue("orderQtyIntervalByShipMethod")!=null && doc.getFieldValue("orderQtyIntervalByShipMethod").toString().trim().length()>0){
						LinkedHashMap<String, ProductsModel>shipOrderQtyAndInterval =  ProductHunterSolr.getOrderQtyIntervalByShipMethod(doc.getFieldValue("orderQtyIntervalByShipMethod").toString());
						itemModel.setShipOrderQtyAndIntervalFieldVal(shipOrderQtyAndInterval);
					}
	            	
					LinkedHashMap<String, Object> customFieldVal = ProductHunterSolr.getAllCustomFieldVal(customFieldValMap);
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
					
				}
				if(itemLevelFilterData.size()>0)
				{
					for(FacetField facetFilter:facetFeild)
					{
						ProductsModel tempVal = new ProductsModel();
						
						if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
						{
							filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
							attrList = new ArrayList<ProductsModel>();
					
							List<Count> attrValArr = facetFilter.getValues();
							for(Count attrArr : attrValArr)
							{
								ProductsModel attrListVal = new ProductsModel();
								attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
								attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
								attrListVal.setAttrValue(attrArr.getName());
								attrListVal.setResultCount((int) attrArr.getCount());
								attrList.add(attrListVal);
								
							}
							
							filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
							tempVal.setAttrFilterList(filteredList);
							attrFilteredList.add(tempVal);
							attributeList.put(facetFilter.getName().replace("attr_", ""), facetFilter.getValues());
						}
						
					}
				}
				solrSearchVal.setItemDataList(itemLevelFilterData);
				solrSearchVal.setAttributeDataList(attrFilteredList);
				solrSearchVal.setIdList(idList);
				
				System.out.println("At the end : " + solrSearchVal.getSuggestedValue());
		 
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return solrSearchVal;
	}
	
	public static ProductsModel solrNavigationSearchResult(String queryString,String connUrl,String[] attrFtr,int fromRow,int resultPerPage,String innerUrl,String fields[],String origKeyWord,String sortField,ORDER sortOrder, boolean includePartNumberSearch,String insideSearchKey, String[] attrFtrGlobal, String fq,String fq1,LinkedHashMap<String, ArrayList<String>> filteredMultList,boolean wildCardSearch)
	{
		ArrayList<ProductsModel>  itemLevelFilterData = new ArrayList<ProductsModel>();
		HttpSolrServer server = null;
		ProductsModel solrSearchVal = new ProductsModel();
		try
		{
			server = ConnectionManager.getSolrClientConnection(connUrl);
			SolrQuery query = new SolrQuery();
			String idList = "";
			String c = "";
			
				query.setQuery(queryString);
				query.addSortField("goldenitem", SolrQuery.ORDER.desc );
				query.addSortField(sortField, sortOrder);
				
				query.set("defType", "edismax");
				
			
					if(includePartNumberSearch)
					{
					query.set("qf", "partkeywords keywords");
					query.set("pf", "partkeywords^100 keywords^50");
					}
					else
					{
						if(wildCardSearch)
						{
							query.set("qf", "partkeywords keywordswildcard");
							query.set("pf", "partkeywords^100 keywordswildcard^50");
						}
						else
						{
							query.set("qf", "partkeywords keywords");
							query.set("pf", "partkeywords^100 keywords^50");
						}
						
					}
			
				
				query.set("ps", "0");
				
				query.set("spellcheck", "true");
				 query.set("spellcheck.extendedResults", "true");
				 query.set("spellcheck.collate", "true");
				 query.set("spellcheck.q", origKeyWord);
				 
				 ProductHunterUtility.getInstance().setRangeFilteQuery(query);
				//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				if(fields!=null && fields.length>0)
				{
					query.addFacetField(fields);
					query.setFacetLimit(-1);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
						query.setFacetSort(true);
					}else{
						query.setFacetSort(false);
					}
				}
				query.setStart(fromRow);
				query.setRows(resultPerPage);
				query.addFilterQuery(attrFtr);
				query.addFilterQuery(insideSearchKey);
				
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")).equalsIgnoreCase("Y") && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_VERSION")).equalsIgnoreCase("10")){
					query.addFilterQuery("defaultProductItem:Y");
				}else{
					if (CommonUtility.validateString((String)CommonDBQuery.getSystemParamtersList().get("CHANGE_REQUEST_HANDLER")).equalsIgnoreCase("Y")) {
						query.setRequestHandler("/mainitem_keywordsearch");
					}
				}
				if(!siteNameSolr.equalsIgnoreCase(""))
                {
                	query.set("customerName",siteNameSolr);
                	query.set("q.original",origKeyWord);
                }
				System.out.println("query : " + query);
				QueryResponse response = server.query(query,METHOD.POST);
				
				SolrDocumentList documents = response.getResults();
				int resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				List<RangeFacet> facetRanges = response.getFacetRanges();
				
				LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();
				
					SpellCheckResponse spellCheckRes = response.getSpellCheckResponse();
					if(spellCheckRes!=null){
						System.out.println(spellCheckRes.isCorrectlySpelled());
						solrSearchVal.setSuggestedValue(spellCheckRes.getCollatedResult());
						System.out.println("DOCUMENTS Suggestion : " + spellCheckRes.getCollatedResult());
						
						if(!spellCheckRes.getSuggestions().isEmpty()){
							Iterator<Suggestion> suggestionArray = spellCheckRes.getSuggestions().iterator();
							if(suggestionArray!=null){
								List<String> suggestionList = suggestionArray.next().getAlternatives();
								solrSearchVal.setSuggestedValueList(new ArrayList<String>(suggestionList));
							}
							
						}
					}
					Iterator<SolrDocument> itr = documents.iterator();
					System.out.println("DOCUMENTS");
					idList = "";
					c = "";
					facetFeild = response.getFacetFields();
					// overriding the new request handler
					if(CommonUtility.validateString((String)CommonDBQuery.getSystemParamtersList().get("CHANGE_REQUEST_HANDLER")).equalsIgnoreCase("Y") && !CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE).equalsIgnoreCase("/mainitem_keywordsearch")) {
						 query.setRequestHandler(CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE));
						 QueryResponse response1 = SolrService.getInstance().executeSolrQuery(ProductHunterSolrV2.solrURL+"/mainitemdata", query);
						 facetFeild = response1.getFacetFields();
						 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_PRODUCT_MODE")).equalsIgnoreCase("Y")) {
							 documents = response1.getResults();
							 resultCount = (int) response1.getResults().getNumFound();
						 }
					}
					attributeList = new LinkedHashMap<String, List<Count>>();

					LinkedHashMap<String, ArrayList<ProductsModel>> filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
					ArrayList<ProductsModel>  attrList = new ArrayList<ProductsModel>();
					
					ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
					
					System.out.println("DOCUMENTS");
					
					itemLevelFilterData = new ArrayList<ProductsModel>();
					while (itr.hasNext()) {
						
						try{
						
						ProductsModel itemModel = new ProductsModel();
						SolrDocument doc = itr.next();
						idList = idList + c + doc.getFieldValue("itemid").toString();
						
						itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
						itemModel.setMinOrderQty(1);
						itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
						itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
						//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
						itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
						itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
						itemModel.setManufacturerId(CommonUtility.validateNumber(ProductHunterSolr.getSolrFieldValue(doc,"manfId")));
						itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
						itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
							
						itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
						itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
						itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
						itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
						itemModel.setProductName((doc.getFieldValue("productName")!=null?doc.getFieldValue("productName").toString():""));
						itemModel.setProductDescription((doc.getFieldValue("productDesc")!=null?doc.getFieldValue("productDesc").toString():""));
						itemModel.setProductCategoryImageName((doc.getFieldValue("productImageName")!=null?doc.getFieldValue("productImageName").toString():""));
						itemModel.setProductCategoryImageType((doc.getFieldValue("productImageType")!=null?doc.getFieldValue("productImageType").toString():""));
						itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
						itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
						itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
						
						if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB").trim().equalsIgnoreCase("Y"))
						{
							if(doc.getFieldValue("catSearchId")!=null && doc.getFieldValue("categoryNamePath")!=null)
							itemModel.setItemBreadCrumb(getItemBreadCrumb((List<Integer>) doc.getFieldValue("catSearchId"),(String) doc.getFieldValue("categoryNamePath")));
							
						}
						if(doc.getFieldValue("custom_Catalog_ID")!=null)
						{
							itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());

						}
						if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
						{
							if(doc.getFieldValue("productItemCount")!=null){
								itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
							}
							if(doc.getFieldValue("productId")!=null){
								itemModel.setProductId(CommonUtility.validateNumber(doc.getFieldValue("productId").toString()));
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
		       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
		       			}
		       			
		       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
		       			String pattern = "[^A-Za-z0-9]";
		   			 itemUrl = itemUrl.replaceAll(pattern," ");
		   			 itemUrl = itemUrl.replaceAll("\\s+","-");
		            	itemModel.setItemUrl(itemUrl);
		            	
		            	Map<String, Object> customFieldValMap = doc.getFieldValueMap();
						
		            	if(doc.getFieldValue("orderQtyIntervalByShipMethod")!=null && doc.getFieldValue("orderQtyIntervalByShipMethod").toString().trim().length()>0){
							LinkedHashMap<String, ProductsModel>shipOrderQtyAndInterval =  ProductHunterSolr.getOrderQtyIntervalByShipMethod(doc.getFieldValue("orderQtyIntervalByShipMethod").toString());
							itemModel.setShipOrderQtyAndIntervalFieldVal(shipOrderQtyAndInterval);
						}
		            	
						LinkedHashMap<String, Object> customFieldVal = ProductHunterSolr.getAllCustomFieldVal(customFieldValMap);
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
						
					}
					if(itemLevelFilterData.size()>0)
					{
						
						LinkedHashMap<String, ArrayList<ProductsModel>> multiFaucetResult = new LinkedHashMap<String, ArrayList<ProductsModel>>();
						if(resultCount>0 && attrFtrGlobal!=null && attrFtrGlobal.length>0)
						{
							multiFaucetResult = ProductHunterSolrUltimate.generateFaucetFilter(queryString, attrFtrGlobal, fq, true, false,insideSearchKey,fq1,filteredMultList);
							
							if(multiFaucetResult!=null && multiFaucetResult.get("price")!=null)
							{
								ArrayList<ProductsModel> fRange = multiFaucetResult.get("price");
								if(fRange!=null && fRange.size()>0 && fRange.get(0).getFacetRange()!=null && fRange.get(0).getFacetRange().size()>0)
									facetRanges = fRange.get(0).getFacetRange();
							}
							
						}
						
						/*for(FacetField facetFilter:facetFeild)
						{
							ProductsModel tempVal = new ProductsModel();
							
							if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
							{
								filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
								attrList = new ArrayList<ProductsModel>();
								if(multiFaucetResult!=null && multiFaucetResult.get(facetFilter.getName().replace("attr_", ""))!=null)
								{
									
									filteredList.put(facetFilter.getName().replace("attr_", ""), multiFaucetResult.get(facetFilter.getName().replace("attr_", "")));
									tempVal.setAttrFilterList(filteredList);
									attrFilteredList.add(tempVal);
								}
						else
						{
							List<Count> attrValArr = facetFilter.getValues();
							for(Count attrArr : attrValArr)
							{
								ProductsModel attrListVal = new ProductsModel();
								if(filteredMultList ==null)
									filteredMultList = new  LinkedHashMap<String, ArrayList<String>>();
								ArrayList<String> aList = filteredMultList.get(facetFilter.getName());
								if(aList!=null && aList.size()>0)
								{
									if(!aList.contains(attrArr.getName()))
									{
									attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
									attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
									attrListVal.setAttrValue(attrArr.getName());
									attrListVal.setResultCount((int) attrArr.getCount());
									attrList.add(attrListVal);
									}
								}
								else
								{
									attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
									attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
									attrListVal.setAttrValue(attrArr.getName());
									attrListVal.setResultCount((int) attrArr.getCount());
									attrList.add(attrListVal);
								}
								
								
							}
							if(attrList!=null && attrList.size()>0)
							{
							filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
							tempVal.setAttrFilterList(filteredList);
							attrFilteredList.add(tempVal);
							attributeList.put(facetFilter.getName().replace("attr_", ""), facetFilter.getValues());
							}
						}
								
							}
						}*/
						attrFilteredList = buildFacetFilter(facetFeild, multiFaucetResult, filteredMultList);
					}
					solrSearchVal.setItemDataList(itemLevelFilterData);
					solrSearchVal.setAttributeDataList(attrFilteredList);
					solrSearchVal.setFacetRange(facetRanges);
					solrSearchVal.setIdList(idList);
					System.out.println("At the end : " + solrSearchVal.getSuggestedValue());
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return solrSearchVal;
	}
	
	
	public static LinkedHashMap<String, ArrayList<ProductsModel>> generateFaucetFilter(String queryString,String attrFilterQuery[],String fq,boolean isSearch, boolean isAdvancedSearch,String navSearch, String fq1, LinkedHashMap<String, ArrayList<String>> filteredMultList)
	{
		 LinkedHashMap<String, ArrayList<ProductsModel>>  filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
		 String changeRequestHandler = (String)CommonDBQuery.getSystemParamtersList().get("CHANGE_REQUEST_HANDLER");
		 HttpSolrServer server = null;
		try{
			
			 
			 SolrQuery query = new SolrQuery();
			 
			 
			 System.out.println("Fq : " + fq);
			 String appendFtr[] = null;
			 String append = "";
			 String c = "";
			
			 
			 for(String attr: attrFilterQuery)
			 {
				 query = new SolrQuery();
				 query.setQuery(queryString);
				
				 if(appendFtr!=null)
                 {
                       query.setFilterQueries(appendFtr);
                       
                 }
                 if (CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y") && !queryString.contains("productId")) {
                         //query.setRequestHandler("/mainitem_keywordsearch");
                	 query.setRequestHandler(CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE));
                 }else{
                       if(isSearch)
                       {
                                  query.set("defType", "edismax");
                             
                                   if(isAdvancedSearch)
                                   {
                                         query.set("qf", "partkeywords");
                                         query.set("pf", "partkeywords^50");
                                   }
                                   else
                                   {
                                         query.set("qf", "partkeywords keywords");
                                         query.set("pf", "partkeywords^100 keywords^50");
                                   }
                                   
                                   query.set("ps", "0");
                       }
                 }

				 query.addFilterQuery(fq.split("~"));
				 /*if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y") && !queryString.contains("productId")){
					query.addFilterQuery("defaultProductItem:Y");
				 }
				 if(fq1!=null && !fq1.trim().equalsIgnoreCase(""))
				 query.addFilterQuery("defaultCategory:Y");*/
				 
				 if(navSearch!=null && !navSearch.trim().equalsIgnoreCase("")) {
					 query.addFilterQuery(navSearch);
				 }
				 //query.addFacetField(escapeQueryCulpritsWithoutWhiteSpace(attr.split(":")[0].replaceAll("\\\\ ", " ")));
				 query.addFacetField(attr.split(":")[0].replaceAll("\\\\ ", " ").replaceAll("\\\\/", "/"));
				 query.setFacetLimit(-1);
				
				 if(attr.split(":")[0]!=null && attr.split(":")[0].trim().equalsIgnoreCase("price")){
					 ProductHunterUtility.getInstance().setRangeFilteQuery(query);
				 }
					//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
					
					
					query.setStart(0);
					query.setRows(1);
					//query.setFilterQueries(attrFtrNav);
				
					System.out.println("Navigation query for faucer : " + query);
					
					server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
					QueryResponse response = server.query(query,METHOD.POST);
					
				 System.out.println("faucet Generator : "+attr.split(":")[0]);
				 append = append + c + attr;
				 c = "~";
				 appendFtr = append.split("~");
				 ArrayList<ProductsModel> attrList = null;
				 ProductsModel attrListVal = null;
				 List<FacetField> facetFeild = response.getFacetFields();
				 List<RangeFacet> facetRange = response.getFacetRanges();
				 if(attr.split(":")[0]!=null && attr.split(":")[0].trim().equalsIgnoreCase("price")){
					 attrList = new ArrayList<ProductsModel>();
					 attrListVal = new ProductsModel();
					 attrListVal.setFacetRange(facetRange);
					 attrList.add(attrListVal);
					 filteredList.put("price", attrList);
				 }else{
					 for(FacetField facetFilter:facetFeild)
						{
							
							if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
							{
								//filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
								attrList = new ArrayList<ProductsModel>();
						
								List<Count> attrValArr = facetFilter.getValues();
								System.out.println("Attribute Name : " + URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
								for(Count attrArr : attrValArr)
								{
									attrListVal = new ProductsModel();
									if(filteredMultList ==null)
										filteredMultList = new  LinkedHashMap<String, ArrayList<String>>();
									ArrayList<String> aList = filteredMultList.get(facetFilter.getName());
									if(aList!=null && aList.size()>0)
									{
										if(!aList.contains(attrArr.getName()))
										{
										attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
										attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
										attrListVal.setAttrValue(attrArr.getName());
										attrListVal.setResultCount((int) attrArr.getCount());
										attrList.add(attrListVal);
										}
									}
									else
									{
										attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
										attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
										attrListVal.setAttrValue(attrArr.getName());
										attrListVal.setResultCount((int) attrArr.getCount());
										attrList.add(attrListVal);
									}
									
									
								}
								if(attrList.size()>0)
								filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
								
							}
							
						}
				 }
				 
				
			 }
			 //query.addFacetField(appendFtr);
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return filteredList;
	}
	
	public static List<ProductsModel> getDoucmentList(String jsonString)
	{
		List<ProductsModel> documentList = new ArrayList<ProductsModel>();
		try
		{
			
			Gson gson = new Gson();
			DocumentModel output = gson.fromJson(jsonString, DocumentModel.class);
			documentList = output.getDocuments();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return documentList;
	}
	
	public static List<ProductsModel> getImageList(String jsonString)
	{
		List<ProductsModel> imageList = new ArrayList<ProductsModel>();
		try
		{
			
			Gson gson = new Gson();
			ImagesModel output = gson.fromJson(jsonString, ImagesModel.class);
			imageList = output.getImages();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return imageList;
	}
	
	public static List<ProductsModel> getVideoList(String jsonString)
	{
		List<ProductsModel> videoList = new ArrayList<ProductsModel>();
		try
		{
			
			Gson gson = new Gson();
			VideoModel output = gson.fromJson(jsonString, VideoModel.class);
			videoList = output.getVideos();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return videoList;
	}
	
	public static ArrayList<ProductsModel> getLinkedItemList(String jsonString,int subsetId,int generalSubset)
	{

		List<ProductsModel> linkedItemList = new ArrayList<ProductsModel>();
		 ArrayList<ProductsModel> linkedItemDetailList = new ArrayList<ProductsModel>();
		 ArrayList<ProductsModel> linkedDetail = new ArrayList<ProductsModel>();
		try
		{
			
			Gson gson = new Gson();
			LinkTypeModel output = gson.fromJson(jsonString, LinkTypeModel.class);
			linkedItemList = output.getLinkTypes();
			ArrayList<Integer> idList = new ArrayList<Integer>();
			LinkedHashMap<String, ArrayList<Integer>> linkedItemListByType = new LinkedHashMap<String, ArrayList<Integer>>();
			if(linkedItemList!=null && linkedItemList.size()>0)
			{
				for(ProductsModel linkedItem:linkedItemList)
				{
					idList = linkedItemListByType.get(linkedItem.getLinkTypeName());
					if(idList==null)
					{
						idList = new ArrayList<Integer>();
						idList.add(linkedItem.getItemId());
					}
					else
					{
						idList.add(linkedItem.getItemId());
					}
					linkedItemListByType.put(linkedItem.getLinkTypeName(), idList);
				}
				
			}
			if(linkedItemListByType!=null && linkedItemListByType.size()>0)
			{
				
				for (Map.Entry<String, ArrayList<Integer>> entry : linkedItemListByType.entrySet()) 
		        {            
					idList = new ArrayList<Integer>();
					idList = entry.getValue();
					if(idList!=null && idList.size()>0)
					{
						linkedDetail = ProductHunterSolr.getItemListByItemId(StringUtils.join(idList," OR "), subsetId, generalSubset, idList.size(), 0, entry.getKey());
						if(linkedDetail!=null && linkedDetail.size()>0)
						linkedItemDetailList.addAll(linkedDetail);
					}
		            System.out.println(entry.getKey()+" --------  "+entry.getValue());
		        }
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return linkedItemDetailList;
	
	}
	
	public static ArrayList<ProductsModel> getItemBreadCrumb(List<Integer> catSearchId, String categoryNamePath){
		
		ArrayList<ProductsModel> itemBreadCrumb = new ArrayList<ProductsModel>();
		try{
			 ProductsModel nameCode = null;
			 String pattern = "[^A-Za-z0-9]";
			 String itemUrl = null;
			if(catSearchId!=null && categoryNamePath!=null && catSearchId.size()>0)
			{
				int catIndex = 1;
				String[] categoryNamePathList = categoryNamePath.split("/\\*/");
				if(categoryNamePathList!=null && categoryNamePathList.length>0){
					for(String categroryName:categoryNamePathList){
						System.out.println(catSearchId.get(catIndex)+"/category/"+categroryName);
						nameCode = new ProductsModel();
						nameCode.setCategoryCode(Integer.toString(catSearchId.get(catIndex)));
						nameCode.setCategoryName(categroryName);
						itemUrl = nameCode.getCategoryName();
						itemUrl = itemUrl.replaceAll(pattern," ");
						itemUrl = itemUrl.replaceAll("\\s+","-");
			            nameCode.setItemUrl(itemUrl);
			            itemBreadCrumb.add(nameCode);
						catIndex++;
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return itemBreadCrumb;
	}
	public static ArrayList<ProductsModel> SearchBlogContent(String keyWord,int fromRow,int noOfPage){
		HttpSolrServer server = null;
		ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
		 try
		 {
			 ProductsModel solrSearchVal = new ProductsModel();
			 if(keyWord!=null && !keyWord.trim().equalsIgnoreCase(""))
			 {
				 keyWord = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
				 String  queryString  = keyWord;
				 String connUrl = solrURL+"/blog";
				 
				 	server = ConnectionManager.getSolrClientConnection(connUrl);
					SolrQuery query = new SolrQuery();
						query.setRequestHandler("/blog_search");
						query.setQuery(queryString);
						query.set("spellcheck", "true");
						 query.set("spellcheck.extendedResults", "true");
						 query.set("spellcheck.collate", "true");
						 query.set("spellcheck.q", keyWord);
						 query.setHighlight(true);
						 query.set("hl.fl", "content");
						 query.set("hl.simple.pre", "<strong>");
						 query.set("hl.simple.post", "</strong>");
						 query.setHighlightFragsize(250);
						query.setStart(fromRow);
						query.setRows(noOfPage);
						
						System.out.println("query : " + query);
						QueryResponse response = server.query(query);
						
						SolrDocumentList documents = response.getResults();
						int resultCount = (int) response.getResults().getNumFound();
						

						
						SpellCheckResponse spellCheckRes = response.getSpellCheckResponse();
						if(spellCheckRes!=null){
							System.out.println(spellCheckRes.isCorrectlySpelled());
							solrSearchVal.setSuggestedValue(spellCheckRes.getCollatedResult());
							System.out.println("DOCUMENTS Suggestion : " + spellCheckRes.getCollatedResult());
						}
						Iterator<SolrDocument> itr = documents.iterator();
						
						while (itr.hasNext()) {
							ProductsModel pageModel = new ProductsModel();
							SolrDocument doc = itr.next();
							String id = doc.getFieldValue("id").toString();
							pageModel.setStaticPageId(id);
							if(doc.getFieldValue("title")!=null)
							{
								
								pageModel.setStaticPageTitle(doc.getFieldValue("title").toString());
								pageModel.setItemUrl(doc.getFieldValue("link").toString());
							}
							if(doc.getFieldValue("title")!=null){
								pageModel.setPageName(doc.getFieldValue("title").toString());
							}
							if(doc.getFieldValue("content")!=null)
							{
								pageModel.setStaticContent(doc.getFieldValue("content").toString());
							}
							pageModel.setResultCount(resultCount);
							itemLevelFilterData.add(pageModel);
							
							if (response.getHighlighting().get(id) != null) {
								
								Map<String, List<String>> highlightList = response.getHighlighting().get(id);
								if(highlightList.get("title")!=null)
								{
									if(highlightList.size()>0)
									{
										List<String> highlightValList = highlightList.get("content");
										if(highlightValList.get(0)!=null)
										pageModel.setStaticContent(highlightValList.get(0));
									}
								}
					      }
						}
			 
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally {
				ConnectionManager.closeSolrClientConnection(server);
		}
		return itemLevelFilterData;
	}
	
	public static void buildBoostQuery(SolrQuery query,String keyWord){
		try{
			keyWord = keyWord.toLowerCase();
			String key = "searchKeyword_1_";
			String val = "_val_:searchKeywordScore_1_";
			StringBuilder bq = null;
			for(int i=0;i<10;i++){
				bq = new StringBuilder();
				bq.append(key).append(i+1).append(":\"").append(keyWord).append("\"");
				bq.append(" AND ").append(val).append(i+1);
				System.out.println(bq.toString());
				query.add("bq",bq.toString());
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void buildBrandBoostQuery(SolrQuery query,String brandBoost){
		
		StringBuilder boostObj = null;
		String validateForSearch1[] = null;
		String brandName = null;
		try{
			JsonElement json = new JsonParser().parse(brandBoost);
			JsonArray arr = json.getAsJsonArray();
			for (int i = 0; i < arr.size(); i++) {
				boostObj = new StringBuilder();
				JsonObject obj = arr.get(i).getAsJsonObject();
				brandName = obj.get("displaylabel").getAsString();
				validateForSearch1 = brandName.split("\\s+",-1);
				if(validateForSearch1.length>1){
					brandName = "\""+brandName+"\"";
				}
				System.out.println(obj.get("displaylabel").getAsString());
				//boostObj.append("brand:").append(URLEncoder.encode(brandName)).append("^").append(obj.get("boost").getAsString());
				boostObj.append("brand:").append(brandName).append("^").append(obj.get("boost").getAsString());
				query.add("bq",boostObj.toString());
			}
		}catch (Exception e) {
			
			e.printStackTrace();// TODO: handle exception
		}
		
	
	}
	
}
