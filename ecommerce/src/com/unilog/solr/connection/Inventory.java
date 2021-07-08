package com.unilog.solr.connection;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

public class Inventory {


	private static Inventory instance;
	private HttpSolrServer server;
	
	private Inventory() {
		try {
		    server = new HttpSolrServer(CommonUtility.validateString( CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/inventory"));
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
	 
	public static Inventory getInstance() {
        if (instance == null){
            synchronized(Inventory.class){
               if(instance == null){
            	   instance = new Inventory();
               }
            }
            
        } else if (instance.getConnection() == null) {
            instance = new Inventory();
        }
        return instance ;
     }

}
