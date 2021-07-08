package com.unilog.products;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

public class FindAsYouType extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public static String manufacturerAuto = "N";
	public static String brandAuto = "N";
	public static String attributesAuto = "N";
	public static String eventsAuto = "N";
	public static String staticAuto = "N";
       
    public FindAsYouType() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String subsetId = (String) request.getSession().getAttribute("userSubsetId");
		String phString = "PH_SEARCH_"+subsetId+"_MV";
		String q = request.getParameter("q");
		String fq = "catalogId:"+phString;
		int numberOfFaytRec = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("NUMBER_OF_FAYT_RECORDS"));
		String faytTypes = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FAYT_TYPES"));
		String faytTypesList[] = null;
		if(faytTypes.trim().length()>0)
			faytTypesList = faytTypes.split(",");
		Gson gson = new Gson();
		ArrayList<String> faytTypeList = new ArrayList<String>();
		if(faytTypesList!=null && faytTypesList.length>0){
			for(String faytList:faytTypesList)
			{
				faytTypeList.add(faytList);
			}
		}else{
		faytTypeList.add("CATEGORY");
		faytTypeList.add("BRAND");
		faytTypeList.add("ATTRIBUTE_CATEGORY");
		faytTypeList.add("BRAND_CATEGORY");
		faytTypeList.add("ATTRIBUTE_CATEGORY_BRAND");}
		if(q==null)
			q="";
		HttpSolrServer server = null;
		try {
			if(!q.trim().equalsIgnoreCase("")){
				q = ProductHunterSolr.escapeQueryCulpritsWithoutWhiteSpace(q);
				System.out.println("Inside auto complete");
				SolrDocumentList autoCompleteResponse =null;
				SolrDocumentList allDocumentList = new SolrDocumentList();
				if(numberOfFaytRec==0)
					numberOfFaytRec = 10;
				String solrUrl = CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/faytautocomplete";
				
				phString = "PH_SEARCH_IDX_"+subsetId;
				server = ConnectionManager.getSolrClientConnection(solrUrl);
				server.setParser(new XMLResponseParser());
				SolrQuery query = null;
				autoCompleteResponse = null;
				
				for(String faytType:faytTypeList)
				{
					fq = "catalogId:"+phString;
					fq = fq + "~" + "fayttypeidx:"+faytType;
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					
					query = new SolrQuery();
					query.setQuery(q);
					query.setRows(numberOfFaytRec);
					query.set("wt", "json");
					query.set("defType", "edismax");
					query.set("qf", "fayttextsearch");
					query.set("pf", "fayttext^100");
					query.set("json.wrf","?");
					query.set("sort", "score desc");
					query.setFilterQueries(fq.split("~"));
					
					
					QueryResponse solrResponse = server.query(query);
					System.out.println("Fayt Auto Complete: "+query);
					autoCompleteResponse = solrResponse.getResults();
					if(autoCompleteResponse!=null)
					allDocumentList.addAll(autoCompleteResponse);
				}
				
				response.setContentType("text/json");  
				response.setCharacterEncoding("UTF-8");
				
				String op = gson.toJson(allDocumentList);
				op = "{\"response\":{\"docs\":"+op+"}}";
				PrintWriter out = response.getWriter();
				out.print(op);
				System.out.println(op);
			}
		} catch (SolrServerException e) {
			
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
