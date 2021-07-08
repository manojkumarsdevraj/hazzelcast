package com.unilog.autocomplete;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.products.ProductHunterSolr;
import com.unilog.utility.CommonUtility;

public class AutoComplete extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public static String categoryAuto = "N";
	public static String manufacturerAuto = "N";
	public static String brandAuto = "N";
	public static String attributesAuto = "N";
	public static String manufacturerPartNumberAuto = "N";
	public static String eventsAuto = "N";
	public static String staticAuto = "N";
	public static String IllustratedDiagram = "N";
	public static String partList = "N";
	public static String modelPart = "N";
	public static String newEquipmentAuto = "N";
	public static String partsAuto = "N";
	public static String categoryFAYT = "N";
	public static String brandFAYT = "N";
	public static String attributeCategoryFAYT = "N";
	public static String brandCategoryFAYT = "N";
	public static String attributeCategoryBrandFAYT = "N";
	public static String partNumberAutoSuggest = "N";
	public static String upcAutoSuggest = "N";
	public static String customerNumberAutoSuggest = "N";
	public static String itemPartNumberAutoSuggest = "N";
	public static  LinkedHashMap<Integer, String> displaySeq;
		
	public AutoComplete() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean categoryAutoFlag = true;
		boolean manufacturerAutoFlag = true;
		boolean brandAutoFlag = true;
		boolean attributesAutoFlag = true;
		boolean manufacturerPartNumberAutoFlag = true;
		//boolean eventsAutoFlag = true;
		//boolean staticAutoFlag = true;
		//boolean IllustratedDiagramFlag = true;
		//boolean partListFlag = true;
		boolean modelPartFlag = true;
		boolean newEquipmentAutoFlag = true;
		boolean partsAutoFlag = true;
		boolean categoryFAYTFlag = true;
		//boolean brandFAYTFlag = true;
		//boolean attributeCategoryFAYTFlag = true;
		boolean brandCategoryFAYTFlag = true;
		//boolean attributeCategoryBrandFAYTFlag = true;
		boolean partNumberAutoSuggestFlag = true;
		boolean upcAutoSuggestFlag = true;
		boolean customerNumberAutoSuggestFlag = true;
		boolean itemPartNumberAutoSuggestFlag = true;
		ArrayList<String> QuickOrderArray = new ArrayList<String>();
		ArrayList<String> partNumberArray = new ArrayList<String>();
		ArrayList<String> manfPartNumberArray = new ArrayList<String>();
		ArrayList<String> upcArray = new ArrayList<String>();
		ArrayList<String> altPartNumberArray = new ArrayList<String>();
		boolean isFromQuickorder = false;
		String solrURL = CommonDBQuery.getSystemParamtersList().get("SOLR_URL");
		String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey());
		
		
		String subsetId = (String) request.getSession().getAttribute("userSubsetId");
		String generalSubset = (String) request.getSession().getAttribute("generalCatalog");
		
		HttpSession session = request.getSession();
		int buyingCompanyId = 0;
		if(session!=null && session.getAttribute("buyingCompanyId")!=null && CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId"))>1){
			buyingCompanyId =  CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId"));
		}
		
		if(subsetId==null){
			System.out.println("Subset is Null");
			
			if(session!=null && session.getAttribute("userSubsetId")!=null && CommonUtility.validateNumber((String) session.getAttribute("userSubsetId"))>0){
				subsetId = (String) session.getAttribute("userSubsetId");
				System.out.println("Session is not Null tempSubset : "+subsetId);
			}else{
				subsetId = CommonDBQuery.getSystemParamtersList().get("USERSUBSETID");
				System.out.println("Session Subset Is null and Taking value from SystemParam subsetId is : "+subsetId);
			}
		}else{
			System.out.println("Subset is Not Null");
		}
		String phString = "";
		String q = request.getParameter("q");
		String requestFrom = request.getParameter("reqFrom");
		int noOfResult = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("AUTO_COMPLETE_NUMBER_OF_RESULT"));
		String changeRequestHandler = (String)CommonDBQuery.getSystemParamtersList().get("CHANGE_REQUEST_HANDLER");
		String autoCompleteVersion = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTO_COMPLETE_VERSION"));
		
		
		if(noOfResult==0){
			noOfResult = 5;
		}
		Gson gson = new Gson();
		String op = "";
		if(q==null){
			q="";
		}
		//query.addField(fl);
		//String fl = "itemid,partnumber,altPartNumber1,brand,manufacturerName,manfId,manfpartnumber,brandID,salesUom,qtyAvailable,packageQty,productItemCount,imageName,imageType,attr_*";
		
		if(CommonUtility.validateString(requestFrom).equalsIgnoreCase("QuickOrder")){
			isFromQuickorder = true;
			
			categoryAutoFlag= false;
			manufacturerAutoFlag= false;
			brandAutoFlag= false;
			attributesAutoFlag= false;
			manufacturerPartNumberAutoFlag= false;
			//eventsAutoFlag= false;
			//staticAutoFlag= false;
			//IllustratedDiagramFlag= false;
			//partListFlag= false;
			modelPartFlag= false;
			newEquipmentAutoFlag= false;
			partsAutoFlag= false;
			categoryFAYTFlag= false;
			//brandFAYTFlag= false;
			//attributeCategoryFAYTFlag= false;
			brandCategoryFAYTFlag= false;
			//attributeCategoryBrandFAYTFlag= false;
			partNumberAutoSuggestFlag= true;
			upcAutoSuggestFlag= false;
			customerNumberAutoSuggestFlag= false;
			itemPartNumberAutoSuggestFlag = false;
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_QOP_UPC_MNP_AUTOCOMPLETE")).length()>0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_QOP_UPC_MNP_AUTOCOMPLETE")).equalsIgnoreCase("Y")) {
				upcAutoSuggestFlag= true;
				manufacturerPartNumberAutoFlag= true;
			}
		}else{
			partNumberAutoSuggestFlag = false;
			categoryAutoFlag = true;
			manufacturerAutoFlag = true;
			brandAutoFlag = true;
			itemPartNumberAutoSuggestFlag = true;
		}
		
		
		
		try {
			if(CommonUtility.validateString(q).length()>0){
				q = ProductHunterSolr.escapeQueryCulpritsWithoutWhiteSpace(q);
				System.out.println("Inside auto complete");
				HttpSolrServer server = null;
				SolrDocumentList documents = new SolrDocumentList();
				SolrDocumentList manufacturerAutoCompleteResponse =null;
				SolrDocumentList manufacturerPartNumberAutoCompleteResponse =null;
				SolrDocumentList BrandAutoCompleteResponse =null;
				SolrDocumentList ModelPartAutoCompleteResponse =null;
				SolrDocumentList AttributeAutoCompleteResponse=null;
				SolrDocumentList NewEquipmentAutoCompleteResponse =null;
				SolrDocumentList PartsAutoCompleteResponse =null; 
				SolrDocumentList categoryFAYTResponse =null;
				SolrDocumentList brandFAYTResponse =null;
				SolrDocumentList attributeCategoryFAYTResponse =null;
				SolrDocumentList brandCategoryFAYTResponse =null;
				SolrDocumentList attributeCategoryBrandFAYTResponse =null;
				SolrDocumentList categoryAutoResponse =null;
				SolrDocumentList partNumberAutoCompleteResponse =null;
				SolrDocumentList customerPartNumberAutoCompleteResponse =null;
				SolrDocumentList itemAutoCompleteResponse =null;
				SolrDocumentList upcAutoCompleteResponse =null;
				
				LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();
				LinkedHashMap<String, Object> autoCompleteResponse = new LinkedHashMap<String, Object>();
				
				if(brandCategoryFAYT.equalsIgnoreCase("Y") && brandCategoryFAYTFlag){
					server = ConnectionManager.getSolrClientConnection(solrURL+"/faytautocomplete");
					phString = "*";//"PH_SEARCH_IDX_"+subsetId;
					String fq = "";
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					fq = "catalogId:"+phString;
					fq = fq + "~" + "fayttypeidx:BRAND_CATEGORY";
					query.setQuery(q.replaceAll("\\s+", " AND "));
					query.setRows(noOfResult);
					String solrParams =CommonDBQuery.getSystemParamtersList().get("FAYT_AUTOCOMPLETE_SOLR_PARAMS");
					if(CommonUtility.validateString(solrParams).length()>0){
						if(solrParams.contains("|")){
							String[] values =solrParams.split("\\|");	
							for(String params:values){
								if(params.contains(",")){
									String[] param = params.split("\\,");
									query.set(param[0],param[1]);
								}
							}
						}else if(solrParams.contains(",")){
							String[] values =solrParams.split("\\,");
							query.set(values[0],values[1]);
						}else{
							query.set("wt", "json");
							query.set("defType", "edismax");
							query.set("qf", "fayttextsearch");
							query.set("pf", "fayttext^100");
							query.set("json.wrf","?");
							query.set("sort", "score desc");
						}
					}else{
						query.set("wt", "json");
						query.set("defType", "edismax");
						query.set("qf", "fayttextsearch");
						query.set("pf", "fayttext^100");
						query.set("json.wrf","?");
						query.set("sort", "score desc");
					}
					query.setFilterQueries(fq.split("~"));
					QueryResponse solrResponse = server.query(query);
					System.out.println("faytautocomplete Auto Complete: "+query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("CATEGORY_BRAND", resultCount);
					brandCategoryFAYTResponse = solrResponse.getResults();
				}
				if(modelPart.equalsIgnoreCase("Y") && modelPartFlag){
					server = ConnectionManager.getSolrClientConnection(solrURL+"/modelpartautocomplete");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					query.setQuery("user_query:(*"+q+"*)");
					query.setRows(noOfResult);
					String solrParams =CommonDBQuery.getSystemParamtersList().get("MODEL_PART_AUTOCOMPLETE_SOLR_PARAMS"); 
					if(CommonUtility.validateString(solrParams).length()>0){
						if(solrParams.contains("|")){
							String[] values =solrParams.split("\\|");	
							for(String params:values){
								if(params.contains(",")){
									String[] param = params.split("\\,");
									query.set(param[0],param[1]);
								}
							}
						}else if(solrParams.contains(",")){
							String[] values =solrParams.split("\\,");
							query.set(values[0],values[1]);
						}else{
							query.set("defType","edismax");
							query.set("qf","user_query");
							query.set("pf","user_query^50");
							query.set("wt", "json");
							query.set("json.wrf","?");
							query.set("sort", "score desc");
						}
					}else{
						query.set("defType","edismax");
						query.set("qf","user_query");
						query.set("pf","user_query^50");
						query.set("wt", "json");
						query.set("json.wrf","?");
						query.set("sort", "score desc");
					}
					
					QueryResponse solrResponse = server.query(query);
					System.out.println("Model Part Auto Complete: "+query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("Illustrated_Diagram", resultCount);
					ModelPartAutoCompleteResponse = solrResponse.getResults();
				}
				if(categoryFAYT.equalsIgnoreCase("Y") && categoryFAYTFlag){
					server = ConnectionManager.getSolrClientConnection(solrURL+"/faytautocomplete");
					phString = "PH_SEARCH_IDX_"+subsetId+"_MV";
					if(CommonUtility.validateNumber(generalSubset) > 0 && CommonUtility.validateNumber(generalSubset)!=CommonUtility.validateNumber(subsetId)) {
						phString = "PH_SEARCH_IDX_"+generalSubset+"_"+subsetId+"_MV";
					}
					String fq = "";
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
						phString="PH_SEARCH_ALL_MV";
					}
					fq = "catalogId:"+phString;
					fq = fq + "~" + "fayttypeidx:CATEGORY";
					query.setQuery(q);
					query.setRows(noOfResult);
					String solrParams =CommonDBQuery.getSystemParamtersList().get("FAYT_AUTOCOMPLETE_SOLR_PARAMS");
					if(CommonUtility.validateString(solrParams).length()>0){
						if(solrParams.contains("|")){
							String[] values =solrParams.split("\\|");	
							for(String params:values){
								if(params.contains(",")){
									String[] param = params.split("\\,");
									query.set(param[0],param[1]);
								}
							}
						}else if(solrParams.contains(",")){
							String[] values =solrParams.split("\\,");
							query.set(values[0],values[1]);
						}else{
							query.set("wt", "json");
							query.set("defType", "edismax");
							query.set("qf", "fayttextsearch");
							query.set("pf", "fayttext^100");
							query.set("json.wrf","?");
							query.set("sort", "score desc");
						}
					}else{
						query.set("wt", "json");
						query.set("defType", "edismax");
						query.set("qf", "fayttextsearch");
						query.set("pf", "fayttext^100");
						query.set("json.wrf","?");
						query.set("sort", "score desc");
					}
					query.setFilterQueries(fq.split("~"));
					QueryResponse solrResponse = server.query(query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("CATEGORY_FAYT", resultCount);
					categoryFAYTResponse = solrResponse.getResults();
				}
				if(manufacturerAuto.equalsIgnoreCase("Y") && manufacturerAutoFlag){
					phString = "PH_SEARCH_"+subsetId+"_MV";
					if(CommonUtility.validateNumber(generalSubset) > 0 && CommonUtility.validateNumber(generalSubset)!=CommonUtility.validateNumber(subsetId)) {
						phString = "PH_SEARCH_"+generalSubset+"_"+subsetId+"_MV";
					}
					server = ConnectionManager.getSolrClientConnection(solrURL+"/manfautocomplete");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					
					if(CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y")){
						query.setQuery(q);
						query.set("qf", "user_query");
						query.set("pf", "user_query_exact^100");
						query.set("defType", "dismax");
						query.addSortField("score", SolrQuery.ORDER.desc);
					}else{
						query.setQuery("user_query:(*"+q+"*)");
					}
					
					query.setRows(noOfResult);
					query.set("wt", "json");
					query.set("json.wrf","?");
					query.setFilterQueries("indexType:"+phString);
					QueryResponse solrResponse = server.query(query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("MANUFACTURER", resultCount);
					System.out.println("Manufacturer Auto Complete: "+query);
					manufacturerAutoCompleteResponse = solrResponse.getResults();
				}
				if(manufacturerPartNumberAuto.equalsIgnoreCase("Y") && manufacturerPartNumberAutoFlag){
					phString = "PH_SEARCH_"+subsetId;
					if(CommonUtility.validateNumber(generalSubset) > 0 && CommonUtility.validateNumber(generalSubset)!=CommonUtility.validateNumber(subsetId)) {
						phString = "PH_SEARCH_"+generalSubset+"_"+subsetId;
					}
					String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+phString;
					if(!siteNameSolr.equalsIgnoreCase(""))
					{
						fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+phString;
					}
					server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					query.setQuery("manfpartnumbersearch:(*"+q.replaceAll("\\_", "-")+"*)");
					query.setRows(noOfResult);
					query.set("wt", "json");
					query.set("json.wrf","?");
					query.setFilterQueries(fqNav);
					if(!siteNameSolr.equalsIgnoreCase(""))
                    {
                    	query.set("customerName",siteNameSolr);
                    	query.set("q.original",q);
                    }
					QueryResponse solrResponse = server.query(query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("MANUFACTURER_PART_NUMBER", resultCount);
					System.out.println("Manufacturer PART NUMBER Auto Complete: "+query);
					if(solrResponse.getResults()!=null && !solrResponse.getResults().isEmpty()){
						for(SolrDocument solrDoc : solrResponse.getResults()){
							solrDoc.put("type", "Manufacturer Part Number");
						}
					}
					
					
					if(solrResponse.getResults()!=null && !solrResponse.getResults().isEmpty() && isFromQuickorder){
						SolrDocumentList documentObj = solrResponse.getResults();
						Iterator itr = documentObj.iterator();
				        while (itr.hasNext()) {
					      SolrDocument doc = (SolrDocument)itr.next();
					      manfPartNumberArray.add(doc.getFieldValue("manfpartnumber").toString());
					    }
					}
					manufacturerPartNumberAutoCompleteResponse = solrResponse.getResults();
				}
				
				if(partNumberAutoSuggest.equalsIgnoreCase("Y") && partNumberAutoSuggestFlag){
					String solrSearchField = request.getParameter("partNumSolrField");
					phString = "PH_SEARCH_"+subsetId;
					if(CommonUtility.validateNumber(generalSubset) > 0 && CommonUtility.validateNumber(generalSubset)!=CommonUtility.validateNumber(subsetId)) {
						phString = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
					}
					String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+phString;
					if(!siteNameSolr.equalsIgnoreCase(""))
					{
						fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+phString;
					}

					//server = new HttpSolrServer(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/mainitemdata");
					server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					if(CommonUtility.validateString(solrSearchField).length()>0){
						query.setQuery(CommonUtility.validateString(solrSearchField)+":(*"+q.replaceAll("\\_", "-")+"*)");
					}else{
						query.setQuery("partkeywords:(*"+q.replaceAll("\\_", "-")+"*)");
					}
					query.setRows(noOfResult);
					query.set("wt", "json");
					query.set("json.wrf","?");
					query.setFilterQueries(fqNav);
					if(!siteNameSolr.equalsIgnoreCase(""))
                    {
                    	query.set("customerName",siteNameSolr);
                    	query.set("q.original",q);
                    }
					QueryResponse solrResponse = server.query(query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("PART_NUMBER", resultCount);
					System.out.println("PART NUMBER Auto Complete: "+query);
					if(solrResponse.getResults()!=null && !solrResponse.getResults().isEmpty()){
						for(SolrDocument solrDoc : solrResponse.getResults()){
							solrDoc.put("type", "Part Number");
						}
					}
					
					if(solrResponse.getResults()!=null && !solrResponse.getResults().isEmpty() && isFromQuickorder){
						SolrDocumentList documentObj = solrResponse.getResults();
						Iterator itr = documentObj.iterator();
				        while (itr.hasNext()) {
					      SolrDocument doc = (SolrDocument)itr.next();
					      if(CommonUtility.validateString(request.getParameter("quickOrderAutoSuggentionsFields")).length()>0){
					    	  String[] fieldsFormat = CommonUtility.validateString(request.getParameter("quickOrderAutoSuggentionsFields")).split("\\|");
					    	  String partNumberArrayValue = "";
					    	  for(int i=0;i<fieldsFormat.length;i++) {
					    		  if(CommonUtility.validateString(fieldsFormat[i]).length()>0) {
					    			  partNumberArrayValue += doc.getFieldValue(CommonUtility.validateString(fieldsFormat[i])).toString();
					    		  }
					    		  if(i==fieldsFormat.length-1)
					    			  partNumberArrayValue+="~|~";
					    		  else
					    			  partNumberArrayValue+="|~|";
					    	  }
					    	  partNumberArray.add(partNumberArrayValue);
					      }else{
					    	  partNumberArray.add(doc.getFieldValue("partnumber").toString());
					      }
					    }
					}
					
					if(solrResponse.getResults()!=null && !solrResponse.getResults().isEmpty() && isFromQuickorder){
						SolrDocumentList documentObj = solrResponse.getResults();
						Iterator itr = documentObj.iterator();
				        while (itr.hasNext()) {
					      SolrDocument doc = (SolrDocument)itr.next();
					      if(doc.getFieldValue("alternatePartNumber1")!=null) {
					    	  altPartNumberArray.add(doc.getFieldValue("alternatePartNumber1").toString()); 
					      } 
					    }
					}
					partNumberAutoCompleteResponse = solrResponse.getResults();
				}
				
				if(customerNumberAutoSuggest.equalsIgnoreCase("Y") && customerNumberAutoSuggestFlag){
					//phString = "PH_SEARCH_"+subsetId;
					server = ConnectionManager.getSolrClientConnection(solrURL+"/customerdata");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					query.setQuery("customerPartNumberKeyword:(*"+q.replaceAll("\\_", "-")+"*)");
					query.setRows(noOfResult);
					query.set("wt", "json");
					query.set("json.wrf","?");
					String fq = "buyingCompanyId:"+buyingCompanyId;
					query.setFilterQueries(fq);
					//query.setFilterQueries("indexType:"+phString);
					QueryResponse solrResponse = server.query(query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("CUSTOMER_PART_NUMBER", resultCount);
					System.out.println("Customer Part Number Auto Complete: "+query);
					if(solrResponse.getResults()!=null && !solrResponse.getResults().isEmpty()){
						for(SolrDocument solrDoc : solrResponse.getResults()){
							solrDoc.put("type", "Customer Part Number");
						}
					}
					customerPartNumberAutoCompleteResponse = solrResponse.getResults();
				}
				
				if(upcAutoSuggest.equalsIgnoreCase("Y") && upcAutoSuggestFlag){
					phString = "PH_SEARCH_"+subsetId;
					if(CommonUtility.validateNumber(generalSubset) > 0 && CommonUtility.validateNumber(generalSubset)!=CommonUtility.validateNumber(subsetId)) {
						phString = "PH_SEARCH_"+generalSubset+"_"+subsetId;
					}
					String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+phString;
					if(!siteNameSolr.equalsIgnoreCase(""))
					{
						fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+phString;
					}

					//server = new HttpSolrServer(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/mainitemdata");
					server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					query.setQuery("upc:(*"+q.replaceAll("\\_", "-")+"*)");
					query.setRows(noOfResult);
					query.set("wt", "json");
					query.set("json.wrf","?");
					query.setFilterQueries(fqNav);
					if(!siteNameSolr.equalsIgnoreCase(""))
                    {
                    	query.set("customerName",siteNameSolr);
                    	query.set("q.original",q);
                    }
					QueryResponse solrResponse = server.query(query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("UPC", resultCount);
					System.out.println("UPC Auto Complete: "+query);
					if(solrResponse.getResults()!=null && !solrResponse.getResults().isEmpty()){
						for(SolrDocument solrDoc : solrResponse.getResults()){
							solrDoc.put("type", "UPC");
						}
					}
					
					if(solrResponse.getResults()!=null && !solrResponse.getResults().isEmpty() && isFromQuickorder){
						SolrDocumentList documentObj = solrResponse.getResults();
						Iterator itr = documentObj.iterator();
				        while (itr.hasNext()) {
					      SolrDocument doc = (SolrDocument)itr.next();
					      upcArray.add(doc.getFieldValue("upc").toString());
					    }
					}
					upcAutoCompleteResponse = solrResponse.getResults();
					
				}
				
				if(brandAuto.equalsIgnoreCase("Y") && brandAutoFlag){
					phString = "PH_SEARCH_"+subsetId+"_MV";
					if(CommonUtility.validateNumber(generalSubset) > 0 && CommonUtility.validateNumber(generalSubset)!=CommonUtility.validateNumber(subsetId)) {
						phString = "PH_SEARCH_"+generalSubset+"_"+subsetId+"_MV";
					}
					server = ConnectionManager.getSolrClientConnection(solrURL+"/brandautocomplete");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					if(CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y")){
						query.setQuery(q);
						query.set("qf", "user_query");
						query.set("pf", "user_query_exact^100");
						query.set("defType", "dismax");
						query.addSortField("score", SolrQuery.ORDER.desc);
					}else{
						query.setQuery("user_query:(*"+q+"*)");
					}
					query.setRows(noOfResult);
					query.set("wt", "json");
					query.set("json.wrf","?");
					query.setFilterQueries("indexType:"+phString);
					QueryResponse solrResponse = server.query(query);
					System.out.println("Brand Auto Complete: "+query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("BRAND", resultCount);
					BrandAutoCompleteResponse = solrResponse.getResults();
				}
				if(newEquipmentAuto.equalsIgnoreCase("Y") && newEquipmentAutoFlag){
					/*phString = "PH_SEARCH_"+subsetId+"_MV";
					if(generalSubset!=null &&  generalSubset!="0" && generalSubset!=subsetId) {
						phString = "PH_SEARCH_"+generalSubset+"_"+subsetId+"_MV";
					}*/
					server = ConnectionManager.getSolrClientConnection(solrURL+"/newequipmentautocomplete");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					query.setQuery("user_query:(*"+q+"*)");
					query.setRows(noOfResult);
					query.set("wt", "json");
					query.set("json.wrf","?");
					query.set("sort", "score desc");
					QueryResponse solrResponse = server.query(query);
					System.out.println("New Equipment Auto Complete: "+query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					System.out.println("Equipment Result Count: "+resultCount);
					totalResults.put("Equipment", resultCount);
					NewEquipmentAutoCompleteResponse = solrResponse.getResults();
				}
				if(partsAuto.equalsIgnoreCase("Y") && partsAutoFlag){
					/*phString = "PH_SEARCH_"+subsetId+"_MV";
					if(generalSubset!=null &&  generalSubset!="0" && generalSubset!=subsetId) {
						phString = "PH_SEARCH_"+generalSubset+"_"+subsetId+"_MV";
					}*/
					server = ConnectionManager.getSolrClientConnection(solrURL+"/partautocomplete");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					query.setQuery("user_query:(*"+q+"*)");
					query.setRows(noOfResult);
					query.set("wt", "json");
					query.set("json.wrf","?");
					query.set("sort", "score desc");
					QueryResponse solrResponse = server.query(query);
					System.out.println("Parts Auto Complete: "+query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("Parts", resultCount);
					PartsAutoCompleteResponse = solrResponse.getResults();
				}
				if(attributesAuto.equalsIgnoreCase("Y") && attributesAutoFlag){
					phString = "PH_SEARCH_"+subsetId+"_MV";
					if(CommonUtility.validateNumber(generalSubset) > 0 && CommonUtility.validateNumber(generalSubset)!=CommonUtility.validateNumber(subsetId)) {
						phString = "PH_SEARCH_"+generalSubset+"_"+subsetId+"_MV";
					}					
					server = ConnectionManager.getSolrClientConnection(solrURL+"/AttributeAutoComplete");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					query.setQuery("user_query:(*"+q+"*)");
					query.setRows(noOfResult);
					query.set("wt", "json");
					query.set("json.wrf","?");
					query.setFilterQueries("indexType:"+phString);
					
					QueryResponse solrResponse = server.query(query);
					int resultCount = (int) solrResponse.getResults().getNumFound();
					totalResults.put("ATTRIBUTES", resultCount);
					System.out.println("Attribute Auto Complete: "+query);
					AttributeAutoCompleteResponse = solrResponse.getResults();
				}
				if(categoryAuto.equalsIgnoreCase("Y") && categoryAutoFlag){
					phString = "PH_SEARCH_"+subsetId+"_MV";
					if(CommonUtility.validateNumber(generalSubset) > 0 && CommonUtility.validateNumber(generalSubset)!=CommonUtility.validateNumber(subsetId)) {
						phString = "PH_SEARCH_"+generalSubset+"_"+subsetId+"_MV";
					}
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
						phString="PH_SEARCH_ALL_MV";
					}
					String taxonomyId = CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID");
					String fq[] = new String[2];
					fq[0] = "phsearchtype:"+phString;
					fq[1] = "taxonomyId:"+taxonomyId;
					//HttpSolrServer server1 = new HttpSolrServer(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/categoryautocomplete");
					server = ConnectionManager.getSolrClientConnection(solrURL+"/categoryautocomplete");
					server.setParser(new XMLResponseParser());
					QueryRequest req1 = new QueryRequest();
					req1.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					if(CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y")){
						query.setQuery(q);
						query.set("qf", "user_query");
						query.set("pf", "user_query_exact^100");
						query.set("defType", "dismax");
						query.addSortField("score", SolrQuery.ORDER.desc);
					}else{
						query.setQuery("user_query:(*"+q+"*)");
					}
					query.setRows(noOfResult);
					query.set("wt", "json");
					query.set("json.wrf","?");
					query.setFilterQueries(fq);
					QueryResponse solrResponse1 = server.query(query);
					System.out.println("Category Auto Complete: "+query);
					categoryAutoResponse = solrResponse1.getResults();
					int resultCount = (int) solrResponse1.getResults().getNumFound();
					totalResults.put("Product_Category", resultCount);
				}
				
				if(itemPartNumberAutoSuggest.equalsIgnoreCase("Y") && itemPartNumberAutoSuggestFlag){
					phString = "PH_SEARCH_"+subsetId;
					if(CommonUtility.validateNumber(generalSubset) > 0 && CommonUtility.validateNumber(generalSubset)!=CommonUtility.validateNumber(subsetId)) {
						phString = "PH_SEARCH_"+generalSubset+"_"+subsetId;
					}
					String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+phString;
					if(!siteNameSolr.equalsIgnoreCase(""))
					{
						fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+phString;
					}

                    server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
                    server.setParser(new XMLResponseParser());
                    QueryRequest req = new QueryRequest();
                    req.setMethod(METHOD.POST);
                    SolrQuery query = new SolrQuery();
            		String validateForSearch[] = q.split("\\s+",-1);
            		//Pattern p = Pattern.compile("[^a-zA-Z0-9]");
            		String queryKey = "";
        			if(validateForSearch!=null && validateForSearch.length>1){
        				queryKey = q;
        			}else{
        				queryKey = "partnumbersearch:*"+q+"* OR "+q;
        			}
                    query.setQuery(queryKey);
                    query.setRequestHandler("/suggest");
                    query.setRows(noOfResult);
                    query.set("wt", "json");
                    query.set("json.wrf","?");
                    query.setFilterQueries(fqNav);
                    query.addFilterQuery("defaultCategory:Y");
                    if(!siteNameSolr.equalsIgnoreCase(""))
                    {
                    	query.set("customerName",siteNameSolr);
                    	query.set("q.original",q);
                    }
                    QueryResponse solrResponse = server.query(query);
                    int resultCount = (int) solrResponse.getResults().getNumFound();
                    totalResults.put("ITEM", resultCount);
                    System.out.println("ITEM Complete: "+query);
                    itemAutoCompleteResponse = solrResponse.getResults();

				}
				
				if(displaySeq!=null){
					for ( int key : displaySeq.keySet() ) {
						String val = displaySeq.get(key);
						if(CommonUtility.validateString(val).equalsIgnoreCase("MODELPART") && ModelPartAutoCompleteResponse!=null){
							documents.addAll(ModelPartAutoCompleteResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("EQUIPMENT") && NewEquipmentAutoCompleteResponse!=null){
							documents.addAll(NewEquipmentAutoCompleteResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("PARTS") && PartsAutoCompleteResponse!=null){
							documents.addAll(PartsAutoCompleteResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("BRAND") && BrandAutoCompleteResponse!=null){
							documents.addAll(BrandAutoCompleteResponse);
							autoCompleteResponse.put("brand", BrandAutoCompleteResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("MANUFACTURER") && manufacturerAutoCompleteResponse!=null){
							documents.addAll(manufacturerAutoCompleteResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("ATTRIBUTES") && AttributeAutoCompleteResponse!=null){
							documents.addAll(AttributeAutoCompleteResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("BRAND_CATEGORY_FAYT") && brandCategoryFAYTResponse!=null){
							documents.addAll(brandCategoryFAYTResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("CATEGORY_FAYT") && categoryFAYTResponse!=null){
							documents.addAll(categoryFAYTResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("CATEGORY") && categoryAutoResponse!=null){
							documents.addAll(categoryAutoResponse);
							autoCompleteResponse.put("category", categoryAutoResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("MANUFACTURER_PART_NUMBER") && manufacturerPartNumberAutoCompleteResponse!=null){
							documents.addAll(manufacturerPartNumberAutoCompleteResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("PART_NUMBER") && partNumberAutoCompleteResponse!=null){
							documents.addAll(partNumberAutoCompleteResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("CUSTOMER_PART_NUMBER") && customerPartNumberAutoCompleteResponse!=null){
							documents.addAll(customerPartNumberAutoCompleteResponse);
							autoCompleteResponse.put("customerpartnumber", customerPartNumberAutoCompleteResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("UPC") && upcAutoCompleteResponse!=null){
							documents.addAll(upcAutoCompleteResponse);
						}else if(CommonUtility.validateString(val).equalsIgnoreCase("ITEM") && itemAutoCompleteResponse!=null){
                            documents.addAll(itemAutoCompleteResponse);
                            autoCompleteResponse.put("item", itemAutoCompleteResponse);
						}
					}	
				}else{
					if(manufacturerAutoCompleteResponse!=null){
						documents.addAll(manufacturerAutoCompleteResponse);
					}
					if(ModelPartAutoCompleteResponse!=null){
						documents.addAll(ModelPartAutoCompleteResponse);
					}
					if(BrandAutoCompleteResponse!=null){
						documents.addAll(BrandAutoCompleteResponse);
					}
					if(AttributeAutoCompleteResponse!=null){
						documents.addAll(AttributeAutoCompleteResponse);
					}
					if(PartsAutoCompleteResponse!=null){
						documents.addAll(PartsAutoCompleteResponse);
					}
					if(NewEquipmentAutoCompleteResponse!=null){
						documents.addAll(NewEquipmentAutoCompleteResponse);
					}
					if(categoryAutoResponse!=null){
						documents.addAll(categoryAutoResponse);
					}
					if(partNumberAutoCompleteResponse!=null){
						documents.addAll(partNumberAutoCompleteResponse);
					}
					if(manufacturerPartNumberAutoCompleteResponse!=null){
						documents.addAll(manufacturerPartNumberAutoCompleteResponse);
					}
					if(upcAutoCompleteResponse!=null){
						documents.addAll(upcAutoCompleteResponse);
					}
					if(customerPartNumberAutoCompleteResponse!=null){
						documents.addAll(customerPartNumberAutoCompleteResponse);
					}
					
				}
				
				if(!documents.isEmpty()){
					String resultJson = gson.toJson(totalResults);
					String resultCounts  = ",\"ResultCount\":["+resultJson+"]";
					if(isFromQuickorder){
						
						if(partNumberArray!=null && partNumberArray.size()>0){
							for(int i=0; i<partNumberArray.size(); i++){
								QuickOrderArray.add(partNumberArray.get(i));
								if(i==5){
									break;
								}
							}
						}
						
						if(altPartNumberArray!=null && altPartNumberArray.size()>0){
							for(int i=0; i<altPartNumberArray.size(); i++){
								QuickOrderArray.add(altPartNumberArray.get(i));
								if(i==5){
									break;
								}
							}
						}
						if(manfPartNumberArray!=null && manfPartNumberArray.size()>0){
							for(int i=0; i<manfPartNumberArray.size(); i++){
								QuickOrderArray.add(manfPartNumberArray.get(i));
								if(i==5){
									break;
								}
							}
						}
						if(upcArray!=null && upcArray.size()>0){
							for(int i=0; i<upcArray.size(); i++){
								QuickOrderArray.add(upcArray.get(i));
								if(i==5){
									break;
								}
							}
						}
						
						op = QuickOrderArray.toString();
					}else{
						if(CommonUtility.validateString(autoCompleteVersion).equalsIgnoreCase("V2")){
							op = gson.toJson(autoCompleteResponse);
						}else{
							op = gson.toJson(documents);
						}
						op = "{\"response\":{\"docs\":"+op+resultCounts+"}}";
					}
					System.out.println("op: "+op);
				}else{
					String resultJson = gson.toJson(totalResults);
					String resultCounts  = ",\"ResultCount\":["+resultJson+"]";
					if(isFromQuickorder){
						if(partNumberArray!=null && partNumberArray.size()>0){
							for(int i=0; i<partNumberArray.size(); i++){
								QuickOrderArray.add(partNumberArray.get(i));
								if(i==5){
									break;
								}
							}
						}
						if(manfPartNumberArray!=null && manfPartNumberArray.size()>0){
							for(int i=0; i<manfPartNumberArray.size(); i++){
								QuickOrderArray.add(manfPartNumberArray.get(i));
								if(i==5){
									break;
								}
							}
						}
						if(upcArray!=null && upcArray.size()>0){
							for(int i=0; i<upcArray.size(); i++){
								QuickOrderArray.add(upcArray.get(i));
								if(i==5){
									break;
								}
							}
						}
						
						op = QuickOrderArray.toString();
					}else{
						if(CommonUtility.validateString(autoCompleteVersion).equalsIgnoreCase("V2")){
							op = gson.toJson(autoCompleteResponse);
						}else{
							op = gson.toJson(documents);
						}
						op = "{\"response\":{\"docs\":"+op+resultCounts+"}}";
					}
					
				}
				
				ConnectionManager.closeSolrClientConnection(server);
			}
			response.setContentType("text/json");  
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(op);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
