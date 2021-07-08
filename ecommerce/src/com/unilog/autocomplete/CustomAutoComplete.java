package com.unilog.autocomplete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.net.ssl.HttpsURLConnection;

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
import com.unilog.products.ProductsModel;
import com.unilog.utility.CommonUtility;




public class CustomAutoComplete {
	private static CustomAutoComplete CustomAutoComplete=null;
	private final String USER_AGENT = "Mozilla/5.0";
	private String SERVICE_URL;
	private String ARI_AUTOCOMPLETE_SOURCE;
	private String ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS;
	private String solrURL;
	private boolean reInitializeClient = true;

	private void initializeClient() {
		SERVICE_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ARI_SERVICE_URL"));
		ARI_AUTOCOMPLETE_SOURCE = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ARI_AUTOCOMPLETE_SOURCE"));
		ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS"));
		if(CommonUtility.validateString(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS).length()==0){
			ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS = "5";
		}
		solrURL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_URL"));
		reInitializeClient = false;
	}
	public static CustomAutoComplete getInstance() {
		if(CustomAutoComplete==null) {
			CustomAutoComplete = new CustomAutoComplete();				
		}
		return CustomAutoComplete;
	}
	public String CustomAutoCompleteReturn(String serviceName,ProductsModel variables) {
		if(reInitializeClient){
			initializeClient();
		}
		String returnResult = "";
		if(ARI_AUTOCOMPLETE_SOURCE.equalsIgnoreCase("ARI")){
			returnResult = getAutoCompleteFromARI(serviceName,variables);
		}else{
			returnResult = getFullDataFromSolr(serviceName,variables);
		}

		return returnResult;
	}
	public String getAutoCompleteFromSolr(String serviceName,ProductsModel variables){
		HttpSolrServer server = null;
		Gson gson = new Gson();
		String op = "";
		SolrDocumentList ModelPartAutoCompleteResponse =null;
		try {
			server = ConnectionManager.getSolrClientConnection(solrURL+"/modelpartautocomplete");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			if(CommonUtility.validateString(serviceName).equalsIgnoreCase("MODELONLY")){
				query.setQuery("modelName:(*"+variables.getModelName()+"*)");	
			}else if(CommonUtility.validateString(serviceName).equalsIgnoreCase("PARTONLY")){
				query.setQuery("partnumbersearch:(*"+variables.getPartNumber()+"*)");	
			}else{
				query.setQuery("user_query:(*"+variables.getQueryString()+"*)");	
			}
			query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));
			query.set("wt", "json");
			query.set("json.wrf","?");
			query.set("defType", "edismax");
			query.set("qf", "fayttextsearch");
			query.set("pf", "fayttext^100");
			query.set("sort", "score desc");
			QueryResponse solrResponse;
			solrResponse = server.query(query);
			System.out.println("Model Part Auto Complete: "+query);
			ModelPartAutoCompleteResponse = solrResponse.getResults();
			op = gson.toJson(ModelPartAutoCompleteResponse);
			op = "{\"response\":{\"docs\":"+op+"}}";
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return op;
	}
	public String getFullDataFromSolr(String serviceName,ProductsModel variables){
		HttpSolrServer server = null;
		Gson gson = new Gson();
		String op = "";
		SolrDocumentList totalSolrResult =null;
		try {
			if(CommonUtility.validateString(serviceName).equalsIgnoreCase("ID")){
				server = ConnectionManager.getSolrClientConnection(solrURL+"/modelpartautocomplete");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				query.setQuery("user_query:(*"+variables.getQueryString()+"*)");
				query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));
				query.set("wt", "json");
				query.set("json.wrf","?");
				QueryResponse solrResponse;
				solrResponse = server.query(query);
				System.out.println("Model Part Auto Complete: "+query);
				totalSolrResult = solrResponse.getResults();
			}else if(CommonUtility.validateString(serviceName).equalsIgnoreCase("CAT")){
				server = ConnectionManager.getSolrClientConnection(solrURL+"/faytautocomplete");
				String phString = "PH_SEARCH_IDX_"+variables.getSubsetId();
				String fq = "catalogId:"+phString;
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				fq = "catalogId:"+phString;
				fq = fq + "~" + "fayttypeidx:BRAND_CATEGORY";
				query = new SolrQuery();
				query.setQuery(variables.getQueryString());
				query.setRows(variables.getResultCount());
				query.set("wt", "json");
				query.set("defType", "edismax");
				query.set("qf", "fayttextsearch");
				query.set("pf", "fayttext^100");
				query.set("json.wrf","?");
				query.set("sort", "score desc");
				query.setFilterQueries(fq.split("~"));
				QueryResponse solrResponse = server.query(query);
				totalSolrResult = solrResponse.getResults();
			}
			if(totalSolrResult!=null){
				op = gson.toJson(totalSolrResult);
				op = "{\"response\":{\"docs\":"+op+"}}";
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return op;	
	}

	public String getAutoCompleteFromARI(String serviceName,ProductsModel variables){
		if(reInitializeClient){
			initializeClient();
		}
		String strTemp = "";
		try {

			String paramConfig ="";
			if(CommonUtility.validateString(serviceName).equalsIgnoreCase("PARTONLY")){
				paramConfig = variables.getBrandName()+"/"+variables.getPartNumber()+"/"+ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS;
				serviceName = "partAutoComplete";
			}else{
				paramConfig = variables.getBrandName()+"/"+variables.getModelName()+"/"+ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS;
				serviceName = "modelAutoComplete";
			}
			String uriRequest = SERVICE_URL+serviceName+"/"+paramConfig;
			URL url = new URL(uriRequest);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setRequestProperty("User-Agent", USER_AGENT);
			InputStream content = null;
			if(conn.getResponseCode()==200){
				content = conn.getInputStream();
			}else{
				content = conn.getErrorStream();
			}
			BufferedReader in = new BufferedReader( new InputStreamReader( content ) );
			String line;
			String outputTxt="";
			while ((line = in.readLine()) != null) {
				outputTxt = outputTxt + line;
			} 
			strTemp = outputTxt;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strTemp;
	}

	public String getAllModelParts(AutoCompleteDataTableModel autoCompleteDataTableModel){
		if(reInitializeClient){
			initializeClient();
		}
		Gson gson = new Gson();
		String result = "";
		SolrDocumentList modelPartAutoCompleteResponse =null;
		HttpSolrServer server = null;
		LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();
		try{
			server = ConnectionManager.getSolrClientConnection(solrURL+"/modelpartautocomplete");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSearchTerm()).length() > 0){
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"*)");
				String innerQuery = ProductHunterSolr.escapeQueryCulpritsWithoutWhiteSpace(autoCompleteDataTableModel.getSearchTerm()).replaceAll("\\s+", " AND ");
				query.setFilterQueries("user_query:(*"+innerQuery+"*)");
			}else{
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"*)");
			}
			if(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount())>0){
				query.setRows(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount()));	
			}else{
				query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));	
			}
			query.setStart(CommonUtility.validateNumber(autoCompleteDataTableModel.getsStart()));
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
				}
			}else{
				query.set("defType","edismax");
				query.set("qf","user_query");
				query.set("pf","user_query^50");
				query.set("wt", "json");
				query.set("json.wrf","?");
			}
			/*if(CommonUtility.validateString(autoCompleteDataTableModel.getSdir()).length()>0){
				String sortBy = "score desc";//, displaylabel " + autoCompleteDataTableModel.getSdir();
				query.set("sort", sortBy);	
			}else{
				query.set("sort", "score desc");	
			}*/
			QueryResponse solrResponse = server.query(query);
			System.out.println("Model Part Auto Complete: "+query);
			int resultCount = (int) solrResponse.getResults().getNumFound();
			totalResults.put("Illustrated_Diagram", resultCount);
			modelPartAutoCompleteResponse = solrResponse.getResults();


			if(CommonUtility.validateString(autoCompleteDataTableModel.getResponseType()).equalsIgnoreCase("datatable")){

				StringBuilder builder = new StringBuilder();
				result = "{\"recordsTotal\":"+resultCount+",\"recordsFiltered\":"+resultCount+",\"data\":[";

				Iterator<SolrDocument> itr = modelPartAutoCompleteResponse.iterator();
				while (itr.hasNext()) {
					ArrayList<String> test = new ArrayList<String>();
					SolrDocument doc = itr.next();
					test.add(doc.getFieldValue("displaylabel").toString()); 
					test.add(doc.getFieldValue("brand").toString());
					test.add(doc.getFieldValue("partnumber").toString());
					test.add(doc.getFieldValue("name").toString());
					test.add(doc.getFieldValue("brandID").toString());
					builder.append(gson.toJson(test)+",");
				}

				String data = "";
				if(CommonUtility.validateString(builder.toString()).length() > 0){
					data = builder.toString().substring(0, builder.toString().length() -1);
				}
				result = result + data+"]}";

				System.out.println("Response-----" + result);
			}else{
				result = getResponse(modelPartAutoCompleteResponse, totalResults, gson);
			}

		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return result;
	}

	public String getAllNewEquipment(AutoCompleteDataTableModel autoCompleteDataTableModel){
		if(reInitializeClient){
			initializeClient();
		}
		Gson gson = new Gson();
		String result = "";
		SolrDocumentList newEquipmentAutoCompleteResponse =null;
		HttpSolrServer server = null;
		LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();

		try{
			server = ConnectionManager.getSolrClientConnection(solrURL+"/newequipmentautocomplete");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			query.setStart(CommonUtility.validateNumber(autoCompleteDataTableModel.getsStart()));
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSearchTerm()).length() > 0){
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"* AND *"+autoCompleteDataTableModel.getSearchTerm()+"*)");
			}else{
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"*)");
			}

			if(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount())>0){
				query.setRows(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount()));	
			}else{
				query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));	
			}
			query.set("wt", "json");
			query.set("json.wrf","?");
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSdir()).length()>0){
				String sortBy = "displaylabel " + autoCompleteDataTableModel.getSdir();
				query.set("sort", sortBy);
			}
			QueryResponse solrResponse = server.query(query);
			System.out.println("New Equipment Auto Complete: "+query);
			int resultCount = (int) solrResponse.getResults().getNumFound();
			totalResults.put("Equipment", resultCount);
			newEquipmentAutoCompleteResponse = solrResponse.getResults();


			if(CommonUtility.validateString(autoCompleteDataTableModel.getResponseType()).equalsIgnoreCase("datatable")){
				StringBuilder builder = new StringBuilder();
				result = "{\"recordsTotal\":"+resultCount+",\"recordsFiltered\":"+resultCount+",\"data\":[";

				Iterator<SolrDocument> itr = newEquipmentAutoCompleteResponse.iterator();
				while (itr.hasNext()) {
					ArrayList<String> test = new ArrayList<String>();
					SolrDocument doc = itr.next();
					test.add(doc.getFieldValue("displaylabel").toString()); 
					test.add(doc.getFieldValue("itemid").toString());
					test.add(doc.getFieldValue("id").toString());
					builder.append(gson.toJson(test)+",");
				}

				String data = "";
				if(CommonUtility.validateString(builder.toString()).length() > 0){
					data = builder.toString().substring(0, builder.toString().length() -1);


				}
				result = result + data+"]}";

				System.out.println("Response-----" + result);
			}else{
				result = getResponse(newEquipmentAutoCompleteResponse, totalResults, gson);
			}

		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return result;
	}

	public String getAllParts(AutoCompleteDataTableModel autoCompleteDataTableModel){
		if(reInitializeClient){
			initializeClient();
		}
		Gson gson = new Gson();
		String result = "";
		SolrDocumentList partsAutoCompleteResponse =null;
		HttpSolrServer server = null;
		LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();
		try{
			server = ConnectionManager.getSolrClientConnection(solrURL+"/partautocomplete");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			query.setStart(CommonUtility.validateNumber(autoCompleteDataTableModel.getsStart()));
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSearchTerm()).length() > 0){
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"* AND *"+autoCompleteDataTableModel.getSearchTerm()+"*)");
			}else{
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"*)");
			}

			if(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount())>0){
				query.setRows(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount()));	
			}else{
				query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));	
			}
			query.set("wt", "json");
			query.set("json.wrf","?");

			if(CommonUtility.validateString(autoCompleteDataTableModel.getSdir()).length()>0){
				String sortBy = "displaylabel " + autoCompleteDataTableModel.getSdir();
				query.set("sort", sortBy);
			}
			QueryResponse solrResponse = server.query(query);
			System.out.println("Parts Auto Complete: "+query);
			int resultCount = (int) solrResponse.getResults().getNumFound();
			totalResults.put("Parts", resultCount);
			partsAutoCompleteResponse = solrResponse.getResults();

			if(CommonUtility.validateString(autoCompleteDataTableModel.getResponseType()).equalsIgnoreCase("datatable")){
				StringBuilder builder = new StringBuilder();
				result = "{\"recordsTotal\":"+resultCount+",\"recordsFiltered\":"+resultCount+",\"data\":[";

				Iterator<SolrDocument> itr = partsAutoCompleteResponse.iterator();
				while (itr.hasNext()) {
					ArrayList<String> test = new ArrayList<String>();
					SolrDocument doc = itr.next();
					test.add(doc.getFieldValue("displaylabel").toString()); 
					test.add(doc.getFieldValue("itemid").toString());
					test.add(doc.getFieldValue("id").toString());
					builder.append(gson.toJson(test)+",");
				}

				String data = "";
				if(CommonUtility.validateString(builder.toString()).length() > 0){
					data = builder.toString().substring(0, builder.toString().length() -1);


				}
				result = result + data+"]}";

				System.out.println("Response-----" + result);
			}else{
				result = getResponse(partsAutoCompleteResponse, totalResults, gson);
			}

		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return result;
	}


	public String getAllAttribute(AutoCompleteDataTableModel autoCompleteDataTableModel){
		if(reInitializeClient){
			initializeClient();
		}
		Gson gson = new Gson();
		String result = "";
		SolrDocumentList attributeAutoCompleteResponse =null;
		HttpSolrServer server = null;
		LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();

		try{
			server = ConnectionManager.getSolrClientConnection(solrURL+"/AttributeAutoComplete");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			query.setStart(CommonUtility.validateNumber(autoCompleteDataTableModel.getsStart()));
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSearchTerm()).length() > 0){
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"* AND *"+autoCompleteDataTableModel.getSearchTerm()+"*)");
			}else{
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"*)");
			}

			if(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount())>0){
				query.setRows(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount()));	
			}else{
				query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));	
			}
			query.set("wt", "json");
			query.set("json.wrf","?");
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSdir()).length()>0){
				String sortBy = "displaylabel " + autoCompleteDataTableModel.getSdir();
				query.set("sort", sortBy);
			}
			QueryResponse solrResponse = server.query(query);
			System.out.println("Attribute Auto Complete: "+query);
			int resultCount = (int) solrResponse.getResults().getNumFound();
			totalResults.put("ATTRIBUTES", resultCount);
			attributeAutoCompleteResponse = solrResponse.getResults();

			if(CommonUtility.validateString(autoCompleteDataTableModel.getResponseType()).equalsIgnoreCase("datatable")){
				StringBuilder builder = new StringBuilder();
				result = "{\"recordsTotal\":"+resultCount+",\"recordsFiltered\":"+resultCount+",\"data\":[";

				Iterator<SolrDocument> itr = attributeAutoCompleteResponse.iterator();
				while (itr.hasNext()) {
					ArrayList<String> test = new ArrayList<String>();
					SolrDocument doc = itr.next();
					test.add(doc.getFieldValue("displaylabel").toString()); 
					test.add(doc.getFieldValue("itemid").toString());
					test.add(doc.getFieldValue("id").toString());
					builder.append(gson.toJson(test)+",");
				}

				String data = "";
				if(CommonUtility.validateString(builder.toString()).length() > 0){
					data = builder.toString().substring(0, builder.toString().length() -1);


				}
				result = result + data+"]}";

				System.out.println("Response-----" + result);
			}else{
				result = getResponse(attributeAutoCompleteResponse, totalResults, gson);
			}

		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return result;
	}


	public String getAllCategories(AutoCompleteDataTableModel autoCompleteDataTableModel){
		if(reInitializeClient){
			initializeClient();
		}
		Gson gson = new Gson();
		String result = "";
		SolrDocumentList categoryAutoCompleteResponse =null;
		HttpSolrServer server = null;
		LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();
		try{
			server = ConnectionManager.getSolrClientConnection(solrURL+"/categoryautocomplete");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			query.setStart(CommonUtility.validateNumber(autoCompleteDataTableModel.getsStart()));
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSearchTerm()).length() > 0){
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"* AND *"+autoCompleteDataTableModel.getSearchTerm()+"*)");
			}else{
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"*)");
			}

			if(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount())>0){
				query.setRows(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount()));	
			}else{
				query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));	
			}
			query.set("wt", "json");
			query.set("json.wrf","?");
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSdir()).length()>0){
				String sortBy = "displaylabel " + autoCompleteDataTableModel.getSdir();
				query.set("sort", sortBy);
			}
			QueryResponse solrResponse = server.query(query);
			System.out.println("Category Auto Complete: "+query);
			int resultCount = (int) solrResponse.getResults().getNumFound();
			totalResults.put("Product_Category", resultCount);
			categoryAutoCompleteResponse = solrResponse.getResults();

			if(CommonUtility.validateString(autoCompleteDataTableModel.getResponseType()).equalsIgnoreCase("datatable")){
				StringBuilder builder = new StringBuilder();
				result = "{\"recordsTotal\":"+resultCount+",\"recordsFiltered\":"+resultCount+",\"data\":[";

				Iterator<SolrDocument> itr = categoryAutoCompleteResponse.iterator();
				while (itr.hasNext()) {
					ArrayList<String> test = new ArrayList<String>();
					SolrDocument doc = itr.next();
					test.add(doc.getFieldValue("displaylabel").toString());
					test.add(doc.getFieldValue("user_query").toString()); 
					test.add(doc.getFieldValue("taxonomyId").toString());
					test.add(doc.getFieldValue("id").toString());
					test.add(doc.getFieldValue("levelNumber").toString());
					test.add(doc.getFieldValue("type").toString());
					test.add(doc.getFieldValue("indexType").toString());
					test.add(doc.getFieldValue("phsearchtype").toString());
					test.add(doc.getFieldValue("codeId").toString());
					builder.append(gson.toJson(test)+",");
				}

				String data = "";
				if(CommonUtility.validateString(builder.toString()).length() > 0){
					data = builder.toString().substring(0, builder.toString().length() -1);


				}
				result = result + data+"]}";

				System.out.println("Response-----" + result);
			}else{
				result = getResponse(categoryAutoCompleteResponse, totalResults, gson);
			}

		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return result;
	}

	public String getAllManufacturers(AutoCompleteDataTableModel autoCompleteDataTableModel){
		if(reInitializeClient){
			initializeClient();
		}
		Gson gson = new Gson();
		String result = "";
		SolrDocumentList manufacturerAutoCompleteResponse =null;
		HttpSolrServer server = null;
		String phString = "PH_SEARCH_"+autoCompleteDataTableModel.getSubsetId();
		LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();

		try{
			server = ConnectionManager.getSolrClientConnection(solrURL+"/manfautocomplete");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			query.setStart(CommonUtility.validateNumber(autoCompleteDataTableModel.getsStart()));
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSearchTerm()).length() > 0){
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"* AND *"+autoCompleteDataTableModel.getSearchTerm()+"*)");
			}else{
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"*)");
			}

			if(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount())>0){
				query.setRows(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount()));	
			}else{
				query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));	
			}
			query.set("wt", "json");
			query.set("json.wrf","?");
			query.setFilterQueries("indexType:"+phString);
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSdir()).length()>0){
				String sortBy = "displaylabel " + autoCompleteDataTableModel.getSdir();	
				query.set("sort", sortBy);
			}
			QueryResponse solrResponse = server.query(query);
			System.out.println("Manufacturer Auto Complete: "+query);
			int resultCount = (int) solrResponse.getResults().getNumFound();
			totalResults.put("MANUFACTURER", resultCount);
			manufacturerAutoCompleteResponse = solrResponse.getResults();

			if(CommonUtility.validateString(autoCompleteDataTableModel.getResponseType()).equalsIgnoreCase("datatable")){
				StringBuilder builder = new StringBuilder();
				result = "{\"recordsTotal\":"+resultCount+",\"recordsFiltered\":"+resultCount+",\"data\":[";

				Iterator<SolrDocument> itr = manufacturerAutoCompleteResponse.iterator();
				while (itr.hasNext()) {
					ArrayList<String> test = new ArrayList<String>();
					SolrDocument doc = itr.next();
					test.add(doc.getFieldValue("displaylabel").toString()); 
					test.add(doc.getFieldValue("codeId").toString());
					test.add(doc.getFieldValue("id").toString());
					builder.append(gson.toJson(test)+",");
				}

				String data = "";
				if(CommonUtility.validateString(builder.toString()).length() > 0){
					data = builder.toString().substring(0, builder.toString().length() -1);


				}
				result = result + data+"]}";

				System.out.println("Response-----" + result);
			}else{
				result = getResponse(manufacturerAutoCompleteResponse, totalResults, gson);
			}

		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return result;
	}


	public String getAllBrands(AutoCompleteDataTableModel autoCompleteDataTableModel){
		if(reInitializeClient){
			initializeClient();
		}
		Gson gson = new Gson();
		String result = "";
		SolrDocumentList brandsAutoCompleteResponse =null;
		HttpSolrServer server = null;
		String phString = "PH_SEARCH_"+autoCompleteDataTableModel.getSubsetId();
		LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();
		try{
			server = ConnectionManager.getSolrClientConnection(solrURL+"/brandautocomplete");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			query.setStart(CommonUtility.validateNumber(autoCompleteDataTableModel.getsStart()));
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSearchTerm()).length() > 0){
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"* AND *"+autoCompleteDataTableModel.getSearchTerm()+"*)");
			}else{
				query.setQuery("user_query:(*"+autoCompleteDataTableModel.getQuery()+"*)");
			}

			if(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount())>0){
				query.setRows(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount()));	
			}else{
				query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));	
			}
			query.set("wt", "json");
			query.set("json.wrf","?");
			query.setFilterQueries("indexType:"+phString);
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSdir()).length()>0){
				String sortBy = "displaylabel " + autoCompleteDataTableModel.getSdir();
				query.set("sort", sortBy);
			}
			QueryResponse solrResponse = server.query(query);
			System.out.println("Manufacturer Auto Complete: "+query);
			int resultCount = (int) solrResponse.getResults().getNumFound();
			totalResults.put("BRAND", resultCount);
			brandsAutoCompleteResponse = solrResponse.getResults();


			if(CommonUtility.validateString(autoCompleteDataTableModel.getResponseType()).equalsIgnoreCase("datatable")){
				StringBuilder builder = new StringBuilder();
				result = "{\"recordsTotal\":"+resultCount+",\"recordsFiltered\":"+resultCount+",\"data\":[";

				Iterator<SolrDocument> itr = brandsAutoCompleteResponse.iterator();
				while (itr.hasNext()) {
					ArrayList<String> test = new ArrayList<String>();
					SolrDocument doc = itr.next();
					test.add(doc.getFieldValue("displaylabel").toString()); 
					test.add(doc.getFieldValue("codeId").toString());
					test.add(doc.getFieldValue("id").toString());
					builder.append(gson.toJson(test)+",");
				}

				String data = "";
				if(CommonUtility.validateString(builder.toString()).length() > 0){
					data = builder.toString().substring(0, builder.toString().length() -1);


				}
				result = result + data+"]}";

				System.out.println("Response-----" + result);
			}else{
				result = getResponse(brandsAutoCompleteResponse, totalResults, gson);
			}
		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return result;
	}

	public String getAllFAYT(AutoCompleteDataTableModel autoCompleteDataTableModel){
		if(reInitializeClient){
			initializeClient();
		}
		Gson gson = new Gson();
		String result = "";
		SolrDocumentList categoryFAYTResponse =null;
		HttpSolrServer server = null;
		String phString = "PH_SEARCH_"+autoCompleteDataTableModel.getSubsetId()+"_MV";
		LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();

		try{
			server = ConnectionManager.getSolrClientConnection(solrURL+"/faytautocomplete");
			phString = "*";//"PH_SEARCH_IDX_"+autoCompleteDataTableModel.getSubsetId();
			String fq = "catalogId:"+phString;
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			fq = "catalogId:"+phString;
			fq = fq + "~" + "fayttypeidx:BRAND_CATEGORY";
			
			fq = "catalogId:"+phString;
			fq = fq + "~" + "fayttypeidx:"+autoCompleteDataTableModel.getFayttypeidx();
			query.setStart(CommonUtility.validateNumber(autoCompleteDataTableModel.getsStart()));
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSearchTerm()).length() > 0){
				query.setQuery("(*"+autoCompleteDataTableModel.getQuery()+"* AND "+autoCompleteDataTableModel.getSearchTerm()+")");
			}else{
				query.setQuery(autoCompleteDataTableModel.getQuery());
			}

			if(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount())>0){
				query.setRows(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount()));	
			}else{
				query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));	
			}
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
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSdir()).length()>0){
				String sortBy = "fayttext " + autoCompleteDataTableModel.getSdir();
				query.set("sort", sortBy);
			}
			query.setFilterQueries(fq.split("~"));
			QueryResponse solrResponse = server.query(query);
			System.out.println("CategoryFAYT Auto Complete: "+query);
			int resultCount = (int) solrResponse.getResults().getNumFound();
			if(CommonUtility.validateString(autoCompleteDataTableModel.getFayttypeidx()).equalsIgnoreCase("BRAND_CATEGORY")){
				totalResults.put("CATEGORY_FAYT", resultCount);
			}else{
				totalResults.put("CATEGORY_BRAND", resultCount);
			}
			categoryFAYTResponse = solrResponse.getResults();

			if(CommonUtility.validateString(autoCompleteDataTableModel.getResponseType()).equalsIgnoreCase("datatable")){
				StringBuilder builder = new StringBuilder();
				result = "{\"recordsTotal\":"+resultCount+",\"recordsFiltered\":"+resultCount+",\"data\":[";

				Iterator<SolrDocument> itr = categoryFAYTResponse.iterator();
				while (itr.hasNext()) {
					ArrayList<String> test = new ArrayList<String>();
					SolrDocument doc = itr.next();
					test.add(doc.getFieldValue("fayttext").toString());
					test.add(doc.getFieldValue("faytappend").toString()); 
					test.add(doc.getFieldValue("fayttypeidx").toString());
					test.add(doc.getFieldValue("fayttype").toString());
					test.add(doc.getFieldValue("catalogId").toString());
					test.add(doc.getFieldValue("faytprepend").toString());
					test.add(doc.getFieldValue("fayttextsearch").toString());
					test.add(doc.getFieldValue("id").toString());
					builder.append(gson.toJson(test)+",");
				}

				String data = "";
				if(CommonUtility.validateString(builder.toString()).length() > 0){
					data = builder.toString().substring(0, builder.toString().length() -1);


				}

				result = result + data+"]}";

				System.out.println("Response-----" + result);

			}else{
				result = getResponse(categoryFAYTResponse, totalResults, gson);
				//result = gson.toJson(categoryFAYTResponse);
			}

		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return result;
	}

	public String getAllPartNumbers(AutoCompleteDataTableModel autoCompleteDataTableModel){
		if(reInitializeClient){
			initializeClient();
		}
		Gson gson = new Gson();
		String result = "";
		SolrDocumentList partNumberAutoCompleteResponse =null;
		HttpSolrServer server = null;
		LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();
		String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey());
		try{
			server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			query.setStart(CommonUtility.validateNumber(autoCompleteDataTableModel.getsStart()));
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSearchTerm()).length() > 0){
				query.setQuery("partkeywords:("+autoCompleteDataTableModel.getQuery()+"* AND *"+autoCompleteDataTableModel.getSearchTerm()+"*)");
			}else{
				query.setQuery("partkeywords:("+autoCompleteDataTableModel.getQuery()+"*)");
			}

			if(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount())>0){
				query.setRows(CommonUtility.validateNumber(autoCompleteDataTableModel.getsAmount()));	
			}else{
				query.setRows(CommonUtility.validateNumber(ARI_AUTOCOMPLETE_NUMBER_OF_RESULTS));	
			}
			query.set("wt", "json");
			query.set("json.wrf","?");
			if(CommonUtility.validateString(autoCompleteDataTableModel.getSdir()).length()>0){
				String sortBy = "partnumber " + autoCompleteDataTableModel.getSdir();
				query.set("sort", sortBy);
			}
			if(!siteNameSolr.equalsIgnoreCase(""))
            {
            	query.set("customerName",siteNameSolr);
            	query.set("q.original",autoCompleteDataTableModel.getSearchTerm());
            }
			QueryResponse solrResponse = server.query(query);
			System.out.println("Part Numbers Auto Complete: "+query);
			int resultCount = (int) solrResponse.getResults().getNumFound();
			totalResults.put("PART_NUMBERS", resultCount);
			partNumberAutoCompleteResponse = solrResponse.getResults();


			if(CommonUtility.validateString(autoCompleteDataTableModel.getResponseType()).equalsIgnoreCase("datatable")){

				StringBuilder builder = new StringBuilder();
				result = "{\"recordsTotal\":"+resultCount+",\"recordsFiltered\":"+resultCount+",\"data\":[";

				Iterator<SolrDocument> itr = partNumberAutoCompleteResponse.iterator();
				while (itr.hasNext()) {
					ArrayList<String> dataList = new ArrayList<String>();
					SolrDocument doc = itr.next();
					dataList.add(doc.getFieldValue("partnumber").toString());
					builder.append(gson.toJson(dataList)+",");
				}

				String data = "";
				if(CommonUtility.validateString(builder.toString()).length() > 0){
					data = builder.toString().substring(0, builder.toString().length() -1);
				}
				result = result + data+"]}";

				System.out.println("Response-----" + result);
			}else{
				result = getResponse(partNumberAutoCompleteResponse, totalResults, gson);
			}

		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return result;
	}
	public String getUserDefinied(AutoCompleteDataTableModel autoCompleteDataTableModel){
		if(reInitializeClient){
			initializeClient();
		}
		Gson gson = new Gson();
		String result = "";
		SolrDocumentList partNumberAutoCompleteResponse =null;
		HttpSolrServer server = null;
		LinkedHashMap<String, Integer>  totalResults = new LinkedHashMap<String, Integer>();
		try{
			server = ConnectionManager.getSolrClientConnection(solrURL+"/faytautocomplete");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			query.setStart(CommonUtility.validateNumber(autoCompleteDataTableModel.getsStart()));
			String phString = "*";//"PH_SEARCH_IDX_"+subsetId;
			String fq = "catalogId:"+phString;
			server.setParser(new XMLResponseParser());
			req.setMethod(METHOD.POST);
			fq = "fayttypeidx:User_Defined";
			query = new SolrQuery();
			query.setQuery("\""+autoCompleteDataTableModel.getQuery()+"\"");
			query.setRows(1);
			String solrParams =CommonDBQuery.getSystemParamtersList().get("FAYT_USERDEFINED_AUTOCOMPLETE_SOLR_PARAMS");
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
			partNumberAutoCompleteResponse = solrResponse.getResults();
			totalResults.put("User_Defined", resultCount);
			if(CommonUtility.validateString(autoCompleteDataTableModel.getResponseType()).equalsIgnoreCase("datatable")){

				StringBuilder builder = new StringBuilder();
				result = "{\"recordsTotal\":"+resultCount+",\"recordsFiltered\":"+resultCount+",\"data\":[";

				Iterator<SolrDocument> itr = partNumberAutoCompleteResponse.iterator();
				while (itr.hasNext()) {
					ArrayList<String> dataList = new ArrayList<String>();
					SolrDocument doc = itr.next();
					dataList.add(doc.getFieldValue("partnumber").toString());
					builder.append(gson.toJson(dataList)+",");
				}

				String data = "";
				if(CommonUtility.validateString(builder.toString()).length() > 0){
					data = builder.toString().substring(0, builder.toString().length() -1);
				}
				result = result + data+"]}";

				System.out.println("Response-----" + result);
			}else{
				result = getResponse(partNumberAutoCompleteResponse, totalResults, gson);
			}

		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return result;
	}

	public void setReInitializeClient(boolean reInitializeClient) {
		this.reInitializeClient = reInitializeClient;
	}
	public boolean isReInitializeClient() {
		return reInitializeClient;
	}

	private String getResponse(SolrDocumentList categoryFAYTResponse, LinkedHashMap<String, Integer>  totalResults, Gson gson ){
		String result = "";
		if(!categoryFAYTResponse.isEmpty()){
			String resultJson = gson.toJson(totalResults);
			String resultCounts  = ",\"ResultCount\":["+resultJson+"]";
			result = gson.toJson(categoryFAYTResponse);
			result = "{\"response\":{\"docs\":"+result+resultCounts+"}}";
			System.out.println(result);
		}
		return result;
	}

}
