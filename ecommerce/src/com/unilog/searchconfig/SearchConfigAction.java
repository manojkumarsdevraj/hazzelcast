package com.unilog.searchconfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.cmsmanagement.util.CMSUtility;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductHunterSolrV2;
import com.unilog.solr.SolrService;
import com.unilog.utility.CommonUtility;
import com.unilognew.util.ECommerceEnumType.RequestHandlers;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import java.util.Scanner;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class SearchConfigAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String renderContent;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int configId;
	private String fileContent;
	private String fname;
	
	public enum SolrFiles {
		protwords,synonyms,stopwords;
	}
	
	public String defaultConfig(){
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		try{
			Gson gson = new Gson();
			
			ArrayList<SearchConfigModel> defaultConfig = SearchConfigDAO.getDefaultConfigData();
			renderContent = gson.toJson(defaultConfig);
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	
	
	public String deleteConfig(){
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		try{
			String strSiteId = request.getHeader("siteId");
			String strUserId = request.getHeader("userId");
			int userId = 1;
			int siteId =CommonDBQuery.getGlobalSiteId();
			if(CommonUtility.validateNumber(strUserId)>0){
				userId = CommonUtility.validateNumber(strUserId);
			}
			if(CommonUtility.validateNumber(strSiteId)>0){
				siteId = CommonUtility.validateNumber(strSiteId);
			}
			Gson gson = new Gson();
			if("DELETE".equals(request.getMethod())){
			SearchConfigDAO.deleteConfig(configId);
			}
			 ArrayList<SearchConfigListModel> configList = SearchConfigDAO.getConfigList(siteId);
				renderContent = gson.toJson(configList);
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	
	
	public String debugQuery(){
		String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey());
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		response.setContentType("text/plain;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		try{
			 Enumeration enumeration = request.getParameterNames();
			 Gson gson = new Gson();
			 String c = "";
			 String qt = "/mainitem_keywordsearch";
			 StringBuilder mainUrl = new StringBuilder();
			   StringBuilder url = new StringBuilder();
			   url.append("?");
			   if(!siteNameSolr.equalsIgnoreCase("")){
			   	   mainUrl.append(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/"+siteNameSolr+"_mainitemdata");
			   }else{
				   mainUrl.append(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/mainitemdata");
			   }
			    
		
			   while(enumeration.hasMoreElements()){
			        String parameterName = (String) enumeration.nextElement();
			        System.out.println(parameterName + " - " + request.getParameter(parameterName));
			        if(parameterName.equalsIgnoreCase("q")){
			        	
			        	String keyWord = request.getParameter(parameterName).replaceAll("\\|", " ");
			    		String validateForSearch[] = keyWord.split("\\s+",-1);		
			    		String singleKeyword = null;
			    		if(validateForSearch!=null && validateForSearch.length>1){
			    			    String validateOr[] = keyWord.split(" OR ",-1);
			    				    if(validateOr!=null && validateOr.length>1){
			    				    	String s = "";
			    					    for(String sKey :validateOr){
			    					    	
			    						     String validateWordLength[] = sKey.split("\\s+",-1);
			    						     if(validateWordLength!=null && validateWordLength.length > 1){
			    						    	 singleKeyword = singleKeyword + s + sKey;
			    						     }else{
			    						    	 singleKeyword = singleKeyword + s + "partnumbersearch:*"+sKey+"* OR "+sKey;
			    						     }
			    						     
			    						     s = " OR ";
			    					    }
			    					 }else{
			    					    	singleKeyword = keyWord;
			    					 }
			    				    
			    				    
			    			 }else{
			    			   singleKeyword = "partnumbersearch:*"+keyWord+"* OR "+keyWord;
			    		   }
			        	url.append(c).append(parameterName).append("=").append(URLEncoder.encode(singleKeyword));
			        
			        }
			        else if(parameterName.equalsIgnoreCase("qt")){
			        	qt = request.getParameter(parameterName);
			        }
			        else if(parameterName.equalsIgnoreCase("fl")){
			        	url.append(c).append(parameterName).append("=").append("*,score");
			        }else if(parameterName.equalsIgnoreCase("fq")){
			        	String filterQuery = request.getParameter(parameterName);
			        	if(filterQuery!=null && !filterQuery.trim().equalsIgnoreCase("")){
			        		String filterQueryArr[] = filterQuery.split("~");
			        		for(String fq:filterQueryArr){
			        			
			        			url.append(c).append("fq").append("=").append(URLEncoder.encode(fq));
			        			c = "&";
			        		}
			        	}
			        	
			        }else if(parameterName.equalsIgnoreCase("bq")){
			        	String boostQuery = request.getParameter(parameterName);
			        	if(boostQuery!=null && !boostQuery.trim().equalsIgnoreCase("")){
			        		String boostQueryArr[] = boostQuery.split("~");
			        		for(String bq:boostQueryArr){
			        			
			        			url.append(c).append("bq").append("=").append(URLEncoder.encode(bq));
			        			c = "&";
			        		}
			        	}
			        	
			        }else if(parameterName.equalsIgnoreCase("hl.fl")){
			        	
			        }else{
			        	url.append(c).append(parameterName).append("=").append(request.getParameter(parameterName));
			        }
			      
			       c = "&";
			    }
			  
			  if(configId>0){
				 url.append("&qf=").append(URLEncoder.encode(SearchConfigDAO.getQueryFields(configId), "UTF-8"));
			   }else{
				   if(SearchConfigUtility.getQueryFields()!=null){
					   url.append("&qf=").append(URLEncoder.encode(SearchConfigUtility.getQueryFields()));
					 }
			   }

				  
					/* if(SearchConfigUtility.getQueryFields()!=null){
						 query.set("qf", SearchConfigUtility.getQueryFields());
					 }*/
			  mainUrl.append(qt).append(url.toString());
			  System.out.println(mainUrl.toString());
					 
						 HttpURLConnection httpConnection = (HttpURLConnection) new URL(mainUrl.toString()).openConnection();
							httpConnection.setDoOutput(true);
							BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
							String line = null;
							StringBuffer responseData = new StringBuffer();
							while((line = in.readLine()) != null) {
								//System.out.println(line);
								responseData.append(line);
							}
			    
			    
			    
			    
			renderContent = responseData.toString();
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	
	public String getBrands(){
		String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey());
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		try{
			 Enumeration enumeration = request.getParameterNames();
			 Gson gson = new Gson();
			 String c = "";
			   StringBuilder url = new StringBuilder();
			   if(!siteNameSolr.equalsIgnoreCase("")){
				   url.append(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/"+siteNameSolr+"_brandautocomplete/").append("select?");
			   }else{
				   url.append(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/brandautocomplete/").append("select?");
			   }   
		
			   while(enumeration.hasMoreElements()){
			        String parameterName = (String) enumeration.nextElement();
			        System.out.println(parameterName + " - " + request.getParameter(parameterName));
			        if(parameterName.equalsIgnoreCase("sort") || parameterName.equalsIgnoreCase("q")){
			        	url.append(c).append(parameterName).append("=").append(URLEncoder.encode(request.getParameter(parameterName)));
			        }else{
			        	url.append(c).append(parameterName).append("=").append(request.getParameter(parameterName));
			        }
		        	
			       c = "&";
			    }			   
			 System.out.println(url.toString());
			   HttpURLConnection httpConnection = (HttpURLConnection) new URL(url.toString()).openConnection();
			   httpConnection.setDoOutput(true);
			   BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			   String line = null;
			   StringBuffer responseData = new StringBuffer();
			   while((line = in.readLine()) != null) {
				   responseData.append(line);
			   }
			   renderContent = responseData.toString();
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	
	}
	
	
	public String editConfig(){
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		try{
			Gson gson = new Gson();
			ArrayList<SearchConfigModel> defaultConfig = SearchConfigDAO.getConfigData(configId);
			
			renderContent = gson.toJson(defaultConfig);
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	
	
	public String configList(){
		request = ServletActionContext.getRequest();
		System.out.println("Header " + request.getHeader("siteId"));
		
		String strSiteId = request.getHeader("siteId");
		String strUserId = request.getHeader("userId");
		int userId = 1;
		int siteId =CommonDBQuery.getGlobalSiteId();
		if(CommonUtility.validateNumber(strUserId)>0){
			userId = CommonUtility.validateNumber(strUserId);
		}
		if(CommonUtility.validateNumber(strSiteId)>0){
			siteId = CommonUtility.validateNumber(strSiteId);
		}
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		
		try{
			Gson gson = new Gson();
			 ArrayList<SearchConfigListModel> configList = SearchConfigDAO.getConfigList(siteId);
			renderContent = gson.toJson(configList);
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		
		return SUCCESS;
	}
	
	public String saveConfig(){
		request = ServletActionContext.getRequest();
		System.out.println("Header " + request.getHeader("siteId"));
		
		String strSiteId = request.getHeader("siteId");
		String strUserId = request.getHeader("userId");
		int userId = 1;
		int siteId =CommonDBQuery.getGlobalSiteId();
		if(CommonUtility.validateNumber(strUserId)>0){
			userId = CommonUtility.validateNumber(strUserId);
		}
		if(CommonUtility.validateNumber(strSiteId)>0){
			siteId = CommonUtility.validateNumber(strSiteId);
		}
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		try{
			
			LinkedHashMap<String, Object> resObj = new LinkedHashMap<>();
			if("POST".equals(request.getMethod())){
				Gson gson = new Gson();
				String requestData = CMSUtility.getInstance().getJsonRequest(request);
				System.out.println(requestData);
				SearchConfigDataModel defaultConfig = gson.fromJson(requestData, SearchConfigDataModel.class);
				System.out.println(gson.toJson(defaultConfig));
				if(defaultConfig!=null && CommonUtility.validateString(defaultConfig.getConfigName()).length()>0){
					System.out.println(CommonDBQuery.getGlobalSiteId());
					resObj = SearchConfigDAO.insertSearchConfig(defaultConfig.getConfigName(), siteId, userId, defaultConfig.getConfigData());
					
				}
				//SearchConfigDAO.updateConfig(defaultConfig);
				renderContent = gson.toJson(resObj);
			}
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	
	public String updateConfig(){
		request = ServletActionContext.getRequest();
		System.out.println("Header " + request.getHeader("siteId"));
		
		String strSiteId = request.getHeader("siteId");
		String strUserId = request.getHeader("userId");
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		int userId = 1;
		int siteId =CommonDBQuery.getGlobalSiteId();
		if(CommonUtility.validateNumber(strUserId)>0){
			userId = CommonUtility.validateNumber(strUserId);
		}
		if(CommonUtility.validateNumber(strSiteId)>0){
			siteId = CommonUtility.validateNumber(strSiteId);
		}
		try{
			
			LinkedHashMap<String, Object> resObj = new LinkedHashMap<>();
			if("POST".equals(request.getMethod())){
				Gson gson = new Gson();
				String requestData = CMSUtility.getInstance().getJsonRequest(request);
				System.out.println(requestData);
				SearchConfigDataModel defaultConfig = gson.fromJson(requestData, SearchConfigDataModel.class);
				System.out.println(gson.toJson(defaultConfig));
				if(defaultConfig.getConfigId() > 0){
					System.out.println(CommonDBQuery.getGlobalSiteId());
					resObj = SearchConfigDAO.mergeConfig(defaultConfig.getConfigData(), defaultConfig.getConfigId(),userId);;
					
				}else{
					resObj.put("error", "Configuration not found");
				}
			renderContent = gson.toJson(resObj);
			}
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	
	
	
	public String activeConfig(){
		request = ServletActionContext.getRequest();
		System.out.println("Header " + request.getHeader("siteId"));
		
		String strSiteId = request.getHeader("siteId");
		String strUserId = request.getHeader("userId");
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		int userId = 1;
		int siteId =CommonDBQuery.getGlobalSiteId();
		if(CommonUtility.validateNumber(strUserId)>0){
			userId = CommonUtility.validateNumber(strUserId);
		}
		if(CommonUtility.validateNumber(strSiteId)>0){
			siteId = CommonUtility.validateNumber(strSiteId);
		}
		try{
			
			LinkedHashMap<String, Object> resObj = new LinkedHashMap<>();
			if("POST".equals(request.getMethod())){
				Gson gson = new Gson();
				String requestData = CMSUtility.getInstance().getJsonRequest(request);
				System.out.println(requestData);
				SearchConfigDataModel defaultConfig = gson.fromJson(requestData, SearchConfigDataModel.class);
				System.out.println(gson.toJson(defaultConfig));
				if(defaultConfig.getConfigId() > 0){
					System.out.println(CommonDBQuery.getGlobalSiteId());
					resObj = SearchConfigDAO.activeConfig(defaultConfig.getConfigId(), siteId);
				
				}else{
					resObj.put("error", "Configuration not found");
				}
			renderContent = gson.toJson(resObj);
			}
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	
	
	
	public String getFileData(){
		String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey());
		request = ServletActionContext.getRequest();
		System.out.println("Header " + request.getHeader("siteId"));
		
		String strSiteId = request.getHeader("siteId");
		String strUserId = request.getHeader("userId");
		response = ServletActionContext.getResponse();
		response.setContentType("text/plain;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		
		try{
			StringBuilder data = new StringBuilder();
			String line = null;
			String c = "";
			boolean isValid = false;
			if(fname!=null && !fname.trim().equalsIgnoreCase("")){
				SolrFiles val = SolrFiles.valueOf(fname);
				switch(val){
				case synonyms:
					isValid = true;
					break;
				case protwords:
					isValid = true;
					break;
				case stopwords:
					isValid = true;
					break;
				default:
					isValid = false;
				}
			}
			
			if(isValid){
				if(!siteNameSolr.equalsIgnoreCase("")) {
					System.out.println("Solr Data");
					String url=CommonDBQuery.getSystemParamtersList().get("SOLR_SERVICE_URL")+"SearchConfig/GetFileData?customerName="+siteNameSolr+"&fname="+fname;
					CloseableHttpClient httpclient = HttpClients.createDefault();
					HttpGet httpget = new HttpGet(url);
					CloseableHttpResponse response = httpclient.execute(httpget);
					Scanner sc = new Scanner(response.getEntity().getContent());

					  System.out.println(response.getEntity().getContent());
					  System.out.println(response.getStatusLine());
					  while(sc.hasNext()) {
						  data.append(c).append(sc.nextLine());
							c = "\n";
					  }
					  System.out.println(data.toString());
					  renderContent = data.toString();
				}else {
					System.out.println("Server Data");
					StringBuilder fileName = new StringBuilder();
					fileName.append("/var/persistent/solrconfig/conf/").append(fname).append(".txt");
					 File f1 = new File(fileName.toString());
					 f1.createNewFile();
		            FileReader fr = new FileReader(f1);
		            BufferedReader br = new BufferedReader(fr);
		            while ((line = br.readLine()) != null) {
		                if (line.contains("java"))
		                    line = line.replace("java", " ");
		                data.append(c).append(line);
		                c = "\n";
		            }
		            fr.close();
		            br.close();
		            renderContent = data.toString();
			}
			}else{
				renderContent = "Error - Invalid file configuration";
			}
			
		}catch (Exception e) {
			renderContent = "Error while reading file content";
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}

	
	
	public String saveFileData(){
		String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey());
		request = ServletActionContext.getRequest();

		response = ServletActionContext.getResponse();
		response.setContentType("text/plain;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		try{
			boolean isValid = false;
			if(fname!=null && !fname.trim().equalsIgnoreCase("")){
				SolrFiles val = SolrFiles.valueOf(fname);
				switch(val){
				case synonyms:
					isValid = true;
					break;
				case protwords:
					isValid = true;
					break;
				case stopwords:
					isValid = true;
					break;
				default:
					isValid = false;
				}
			}
			if(isValid){
				if(!siteNameSolr.equalsIgnoreCase("")) {
					System.out.println("Solr Data");
					String url=CommonDBQuery.getSystemParamtersList().get("SOLR_SERVICE_URL")+"SearchConfig/SaveFileData?customerName="+siteNameSolr+"&fname="+fname;
					CloseableHttpClient client = HttpClients.createDefault();
					HttpPost postRequest = new HttpPost(url);
					if(CommonUtility.validateString(fileContent).equalsIgnoreCase(""))
			        {
			        	fileContent = " ";
			        }
					StringEntity userEntity = new StringEntity(fileContent);
				
				   postRequest.setEntity(userEntity);
						
				   CloseableHttpResponse response = client.execute(postRequest);
				 
				   int statusCode = response.getStatusLine().getStatusCode();
				 
				   if (statusCode != 200) 
				   {
					   throw new RuntimeException("HTTP error code : " + statusCode);
				   }
				  
				   client.close();
				   response=null;
				   postRequest=null;
				}else {
					System.out.println("Server Data");
					StringBuilder fileName = new StringBuilder();
					fileName.append("/var/persistent/solrconfig/conf/").append(fname).append(".txt");
					File f1 = new File(fileName.toString());
					FileWriter fw = new FileWriter(f1);
			            BufferedWriter out = new BufferedWriter(fw);
			            out.write(fileContent);
			            out.flush();
			            out.close();
			            renderContent = "Saved Successfully";
				}
			}else{
				renderContent = "Error - Invalid file";
			}
		}catch (Exception e) {
			renderContent = "Error while saving file";
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	
	public String queryExecutor(){
		String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey());
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		try{
			String corename = request.getParameter("core");
			if(CommonUtility.validateString(corename).length()>0){
				Enumeration enumeration = request.getParameterNames();
				 Gson gson = new Gson();
				 String c = "";
				   StringBuilder url = new StringBuilder();
				   if(!siteNameSolr.equalsIgnoreCase("")){
					   url.append(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/"+siteNameSolr+"_"+corename+"/").append("select?");
				   }else {
					   url.append(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/"+corename+"/").append("select?");
				   }
				    
			
				   while(enumeration.hasMoreElements()){
				        String parameterName = (String) enumeration.nextElement();
				        System.out.println(parameterName + " - " + request.getParameter(parameterName));
				        if(!parameterName.trim().equalsIgnoreCase("core")){
				        	if(parameterName.equalsIgnoreCase("sort") || parameterName.equalsIgnoreCase("q")){
					        	url.append(c).append(parameterName).append("=").append(URLEncoder.encode(request.getParameter(parameterName)));
					        }else{
					        	url.append(c).append(parameterName).append("=").append(request.getParameter(parameterName));
					        }
				        	
					       c = "&";	
				        }
				        
				    }			   
				   System.out.println(url.toString());
				   HttpURLConnection httpConnection = (HttpURLConnection) new URL(url.toString()).openConnection();
				   httpConnection.setDoOutput(true);
				   BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
				   String line = null;
				   StringBuffer responseData = new StringBuffer();
				   while((line = in.readLine()) != null) {
					   responseData.append(line);
				   }
				   renderContent = responseData.toString();
			}else{
				renderContent = "{}";
			}
			 
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	
	}
	
	
	public String filterAttributesList(){
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		try{
			ArrayList<FilterAttributeModel> filterList = new ArrayList<>();
			FilterAttributeModel tempObj = new FilterAttributeModel();
			Gson gson = new Gson();
			int resultCount = 0;
			LinkedHashMap<String, Integer> selectedAttribute = SearchConfigDAO.getSelectedFilterAttributes();
			tempObj.setAttrName("category");
			tempObj.setDisplaySeq(0);
			tempObj.setStatus("I");
			if(selectedAttribute.get("category")!=null){
				tempObj.setStatus("A");
				tempObj.setDisplaySeq(selectedAttribute.get("category"));
			}
			filterList.add(tempObj);
			tempObj = new FilterAttributeModel();
			tempObj.setAttrName("brand");
			tempObj.setDisplaySeq(0);
			tempObj.setStatus("I");
			if(selectedAttribute.get("brand")!=null){
				tempObj.setStatus("A");
				tempObj.setDisplaySeq(selectedAttribute.get("brand"));
			}
			filterList.add(tempObj);
			tempObj = new FilterAttributeModel();
			tempObj.setAttrName("manufacturerName");
			tempObj.setDisplaySeq(0);
			tempObj.setStatus("I");
			if(selectedAttribute.get("manufacturerName")!=null){
				tempObj.setStatus("A");
				tempObj.setDisplaySeq(selectedAttribute.get("manufacturerName"));
			}
			filterList.add(tempObj);
			SolrQuery query = new SolrQuery();
			query.setQuery("*:*");
			query.setFacet(true);
			query.setFacetLimit(-1);
			query.setFacetMinCount(1);
			query.addFacetField("attrName");
			query.setRows(1);
			QueryResponse response = SolrService.getInstance().executeSolrQuery(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/categoryattribute", query);
			resultCount = (int) response.getResults().getNumFound();
			List<FacetField> facetFeild = response.getFacetFields();
			for(FacetField facetFilter:facetFeild)
			{
				if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
				{
					List<Count> attrValArr = facetFilter.getValues();
					for(Count attrArr : attrValArr)
					{
						tempObj = new FilterAttributeModel();
						tempObj.setAttrName(attrArr.getName());
						if(selectedAttribute.get("attr_"+attrArr.getName())!=null){
							tempObj.setDisplaySeq(selectedAttribute.get("attr_"+attrArr.getName()));
							tempObj.setStatus("A");
						}else{
							tempObj.setDisplaySeq(0);
							tempObj.setStatus("I");
						}
						filterList.add(tempObj);
						
						System.out.println(attrArr.getName());
					}
				}
			}
			renderContent = gson.toJson(filterList);
			System.out.println(renderContent);
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	
	
	public String updateFilter(){
		request = ServletActionContext.getRequest();
		System.out.println("Header " + request.getHeader("siteId"));
		
		String strSiteId = request.getHeader("siteId");
		String strUserId = request.getHeader("userId");
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
		int userId = 1;
		int siteId =CommonDBQuery.getGlobalSiteId();
		if(CommonUtility.validateNumber(strUserId)>0){
			userId = CommonUtility.validateNumber(strUserId);
		}
		if(CommonUtility.validateNumber(strSiteId)>0){
			siteId = CommonUtility.validateNumber(strSiteId);
		}
		try{
			
			LinkedHashMap<String, Object> resObj = new LinkedHashMap<>();
			if("POST".equals(request.getMethod())){
				Gson gson = new Gson();
				String requestData = CMSUtility.getInstance().getJsonRequest(request);
				System.out.println(requestData);
				List<FilterAttributeModel> filterList = gson.fromJson(requestData, new TypeToken<List<FilterAttributeModel>>(){}.getType());
				
				if(filterList.size() > 0){
					SearchConfigDAO.inActiveFilter();
					System.out.println(CommonDBQuery.getGlobalSiteId());
					resObj = SearchConfigDAO.mergeFilter(filterList);
					SearchConfigDAO.deleteFilter();
					
				}else{
					resObj.put("error", "Configuration not found");
				}
			renderContent = gson.toJson(resObj);
			}
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	
	
	public String getRenderContent() {
		return renderContent;
	}

	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}


	public int getConfigId() {
		return configId;
	}


	public void setConfigId(int configId) {
		this.configId = configId;
	}


	public String getFileContent() {
		return fileContent;
	}


	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}


	public String getFname() {
		return fname;
	}


	public void setFname(String fname) {
		this.fname = fname;
	}
	

}
