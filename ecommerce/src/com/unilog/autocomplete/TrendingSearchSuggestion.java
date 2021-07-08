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

public class TrendingSearchSuggestion extends HttpServlet {
	
	Gson gson = new Gson();
	static String solrURLForTrendingSearch = CommonDBQuery.getSystemParamtersList().get("SOLR_URL");
		
	public TrendingSearchSuggestion() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		SolrDocumentList trendingSearchResponse = new SolrDocumentList();
		HttpSolrServer server = null;
		
		try{
				server = ConnectionManager.getSolrClientConnection(solrURLForTrendingSearch+"/trendingSearch"); 
				trendingSearchResponse = new SolrDocumentList();
	            server.setParser(new XMLResponseParser());
	            QueryRequest req = new QueryRequest();
	            req.setMethod(METHOD.POST);	
	            SolrQuery query = new SolrQuery();
	            query.setQuery("*:*");
	            //query.addFilterQuery("type:search");
	            query.setRequestHandler("/select");
	            query.setRows(10);
	            query.set("wt", "json");
	            query.set("json.wrf","?");
	            //query.setFilterQueries(fqNav);
	            //query.addFilterQuery("defaultCategory:Y");
	            query.set("sort", "searchKeywordScore desc");
	          
	            QueryResponse solrResponse = server.query(query);
	            int resultCount = (int) solrResponse.getResults().getNumFound();
	           // totalResults.put("ITEM", resultCount);
	            System.out.println("trending search Complete: "+query);
	            trendingSearchResponse = solrResponse.getResults();
	            LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
	            jsonObject.put("data", trendingSearchResponse);
	           response.getWriter().print(gson.toJson(jsonObject));
	        		  
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		
		finally
		{
			 server.shutdown();
			
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
	
	
	



