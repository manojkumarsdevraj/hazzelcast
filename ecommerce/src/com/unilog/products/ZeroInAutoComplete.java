package com.unilog.products;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.utility.CommonUtility;

/**
 * Servlet implementation class ZeroInAutoComplete
 */
public class ZeroInAutoComplete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ZeroInAutoComplete() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSolrServer server = null;
		try{
			response.setContentType("application/json");
			String subsetId = (String) request.getSession().getAttribute("userSubsetId");
			subsetId = "112";
			String phString = "PH_SEARCH_1_252";
			String q = request.getParameter("q");
			String keyWord = request.getParameter("keyWord");
			String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey());
	
			if(q==null)
				q="";
			
			if(!q.trim().equalsIgnoreCase("") && q.trim().equalsIgnoreCase("brand")){
				String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+phString;
				if(!siteNameSolr.equalsIgnoreCase(""))
				{
					fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+phString;
				}
				System.out.println("Inside auto complete");
				server = ConnectionManager.getSolrClientConnection(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/mainitemdata");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				query.setQuery("*:*");
				query.setRows(1);
				query.set("wt", "json");
				query.setFilterQueries(fq);
				query.addFacetField("brand");
				query.addFacetField("category");
				query.addFacetField("manufacturerName");
				query.set("facet.limit","-1");
				query.set("json.nl","map");
				System.out.println("http://10.200.1.104:8983/solr/mainitemdata/select?"+query);
				
				HttpURLConnection eclipseConn = (HttpURLConnection) new URL("http://10.200.1.104:8983/solr/mainitemdata/select?"+query).openConnection();
							
				            eclipseConn.setDoOutput(true);
							
							
							BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
							String line = null;
									
							StringBuffer responseData = new StringBuffer();
							
							while((line = in.readLine()) != null) {
							
								responseData.append(line);
							}
								System.out.println(responseData);
								String op = responseData.toString();
								//op = "({response:{docs:"+op+"}})";
								//op="docs:"+op;
								 
								    PrintWriter out = response.getWriter();
								out.print(op);
								
			}
		}catch (Exception e) {
		 e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
