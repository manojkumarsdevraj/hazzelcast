package com.unilog.custommodule.dao;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.feed.jaxb.DailyFeedRequest;
import com.unilog.feed.jaxb.TickFeedRequest;
import com.unilog.utility.CommonUtility;

public class DxFeedDAOService {
	
	private static DxFeedDAOService dxFeedDAOService=null;
	private static final String TICK_FEED="tick";
	private static final String MAP_KEY_PREFIX="copper";
	private ConcurrentHashMap<String,String> copperValuesMap = null;
	private ConcurrentHashMap<String,Date> lastLoadedTimeMap = null;
	
	private String SERVICE_URL;
	private String SERVICE_PATH_1;
	private String DAILY_FEED_SERVICE_PATH;
	private String inputMediaType = MediaType.APPLICATION_JSON;
	private String outputMediaType = MediaType.APPLICATION_JSON;
	private String TICK_FEED_SERVICE_PATH;
	private int TICK_FEED_SERVICE_INTERVAL;
	private String FEED_USER_NAME;
	private String FEED_PASSWORD;
	private Client client; 
	private boolean reInitializeClient = true;
	
	private DxFeedDAOService() {
		// no need to instantiate
		copperValuesMap = new ConcurrentHashMap<String,String>();
		lastLoadedTimeMap = new ConcurrentHashMap<String,Date>();
	}
	
	public static DxFeedDAOService getInstance() {
		synchronized(DxFeedDAOService.class) {
			if(dxFeedDAOService==null) {
				dxFeedDAOService = new DxFeedDAOService();				
			}
		}
		return dxFeedDAOService;
	}
	
	private boolean isItTimeToReload(int days) {
		boolean success=false;
		Date lastLoadedTime = null;
		Date now = null;
		if(days==0) {
			success=true;
		} else {
		
			now = new Date();
			
			lastLoadedTime = lastLoadedTimeMap.get(MAP_KEY_PREFIX+days);
						
			if(lastLoadedTime==null){
				lastLoadedTime = now;
		    }
		  
		    long diff = now.getTime() - lastLoadedTime.getTime();
		    long diffMinutes = diff / (60 * 1000);
		    
		    if(diffMinutes<0){
		    	diffMinutes=-(diffMinutes);
		    }
		    if(lastLoadedTimeMap.get(MAP_KEY_PREFIX+days)==null){
		    	success=true;
		    }
		    if(diffMinutes>=CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("COPPERDATARELOADTIME"))){
		    	success=true;
		    } 
		}
		return success;
	}	

	public String loadCopperFeedDataAndReturn(String resultPage,String symbol,String locale) {
		
		if(resultPage.equals("reload")) {
			return "";
		}
		
		Date now = null;
		String mapKey = MAP_KEY_PREFIX+resultPage;
		String feedResponse=null;
		String serverDownMsg = LayoutLoader.getMessageProperties().get(locale).getProperty("dxFeed.server.unavailable.message");
		String serviceDownMsg = LayoutLoader.getMessageProperties().get(locale).getProperty("dxFeed.service.unavailable.message");
		int days =0;
		
		if(reInitializeClient){
			initializeClient();	
		}
		
		if(!resultPage.equals(TICK_FEED)) {
			days = CommonUtility.validateNumber(resultPage);
		}
		
		if(isItTimeToReload(days)) {
			String copperData = CommonUtility.validateString(getCopperData(days,symbol,locale));
			if(copperData.length()>0 && (!copperData.equals(serverDownMsg)||!copperData.equals(serviceDownMsg))){
				synchronized(this) {
					copperValuesMap.put(mapKey, copperData);
					if(!resultPage.equals(TICK_FEED) && !resultPage.equals("2")) {
						now = new Date();
						lastLoadedTimeMap.put(mapKey,now);
					}
				}	
			}
		}			
		feedResponse = copperValuesMap.get(mapKey);
		return feedResponse;
	}
	
	private void initializeClient() {
		
		SERVICE_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("COPPERGRAPH_SERVICE_URL"));
		SERVICE_PATH_1 = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("COPPERGRAPH_SERVICE_PATH_1"));
		DAILY_FEED_SERVICE_PATH = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("COPPERGRAPH_DAILY_FEED_SERVICE_PATH"));
		TICK_FEED_SERVICE_PATH = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("COPPERGRAPH_TICK_FEED_SERVICE_PATH"));
		FEED_USER_NAME =  CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("COPPERGRAPH_FEED_USER_NAME"));
		FEED_PASSWORD = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("COPPERGRAPH_FEED_PASSWORD"));
		TICK_FEED_SERVICE_INTERVAL = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TICK_FEED_SERVICE_INTERVAL"));
		ClientConfig config = new DefaultClientConfig();
		client = Client.create(config);
		client.addFilter(new HTTPBasicAuthFilter(FEED_USER_NAME, FEED_PASSWORD));
		client.addFilter(new LoggingFilter(System.out));
		reInitializeClient = false;
	}
	
	private Builder getDXFeedService(DxFeedServiceType dxFeedServiceType) {
		WebResource service = client.resource(UriBuilder.fromUri(SERVICE_URL).build());
		String tempPath=TICK_FEED_SERVICE_PATH;
		if(dxFeedServiceType==DxFeedServiceType.DAILY) {
			tempPath = DAILY_FEED_SERVICE_PATH;
		}
		Builder builder = service.path(SERVICE_PATH_1).path(tempPath).accept(inputMediaType).type(outputMediaType);
		return builder;		
	}
	
	private String getCopperData(int days,String symbol,String locale){
		String responseJson = "";
		ClientResponse response = null;
		Builder builder = null;
		try {
			if(days==0){
				TickFeedRequest tickFeedRequest = new TickFeedRequest();
				tickFeedRequest.setSymbol(symbol);
				tickFeedRequest.setMinuteTimeInterval(TICK_FEED_SERVICE_INTERVAL);
				builder = getDXFeedService(DxFeedServiceType.TICK);
				response = builder.post(ClientResponse.class,tickFeedRequest);
			} else {
				DailyFeedRequest dailyFeedRequest = new DailyFeedRequest();
				dailyFeedRequest.setSymbol(symbol);
				dailyFeedRequest.setBackDays(days);
				builder = getDXFeedService(DxFeedServiceType.DAILY);
				response = builder.post(ClientResponse.class,dailyFeedRequest);
				
			}
			if(response!=null && response.getStatus()==200){
				responseJson = response.getEntity(String.class);
			}else{
				responseJson = LayoutLoader.getMessageProperties().get(locale).getProperty("dxFeed.server.unavailable.message");
			}
			
		}catch(Exception e){
			responseJson=LayoutLoader.getMessageProperties().get(locale).getProperty("dxFeed.service.unavailable.message");
		}
		return responseJson;
	}

	public void setReInitializeClient(boolean reInitializeClient) {
		this.reInitializeClient = reInitializeClient;
	}

	public boolean isReInitializeClient() {
		return reInitializeClient;
	}
	
	private enum DxFeedServiceType {
		TICK,DAILY;
	}
		
}
