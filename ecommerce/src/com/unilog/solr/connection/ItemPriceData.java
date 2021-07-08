package com.unilog.solr.connection;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

public class ItemPriceData {


	private static ItemPriceData instance;
	private HttpSolrServer server;
	
	private ItemPriceData() {
		try {
		    server = new HttpSolrServer(CommonUtility.validateString( CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/itempricedata"));
		    server.setDefaultMaxConnectionsPerHost(100);
		    server.setMaxTotalConnections(100);
		    server.setFollowRedirects(false); // defaults to false
		    server.setAllowCompression(true);
		    server.setMaxRetries(1); // defaults to 0. > 1 not recommended.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public HttpSolrServer getConnection() {
	        return server;
	    }
	 
	public static ItemPriceData getInstance() {
        if (instance == null){
            synchronized(ItemPriceData.class){
               if(instance == null){
            	   instance = new ItemPriceData();
               }
            }
            
        } else if (instance.getConnection() == null) {
            instance = new ItemPriceData();
        }
        return instance ;
     }

}
