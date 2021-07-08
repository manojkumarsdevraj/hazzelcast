package com.unilog.solr.connection;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

public class BrandAutoComplete {


	private static BrandAutoComplete instance;
	private HttpSolrServer server;
	
	private BrandAutoComplete() {
		try {
		    server = new HttpSolrServer(CommonUtility.validateString( CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/brandautocomplete"));
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
	 
	public static BrandAutoComplete getInstance() {
        if (instance == null){
            synchronized(BrandAutoComplete.class){
               if(instance == null){
            	   instance = new BrandAutoComplete();
               }
            }
            
        } else if (instance.getConnection() == null) {
            instance = new BrandAutoComplete();
        }
        return instance ;
     }

}
