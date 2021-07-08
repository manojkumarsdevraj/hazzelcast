package com.unilog.velocitytool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ConnectionUtility.Cimm2BCentralConnection;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.struts2.ServletActionContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.erp.service.ProductManagement;
import com.erp.service.impl.ProductManagementImpl;
import com.erp.service.model.ProductManagementModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unilog.customfields.CustomFieldDAO;
import com.unilog.customfields.CustomModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductHunterSolrUltimate;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.socialmedia.blog.model.EntiesModel;
import com.unilog.socialmedia.service.BlogFeed;
import com.unilog.socialmedia.service.ResponseBlog;
import com.unilog.socialmedia.service.ResponseBlogEachPost;
import com.unilog.socialmedia.service.ResponseDataBlog;
import com.unilog.socialmedia.service.ResponseFaceBook;
import com.unilog.socialmedia.service.ResponseTwitter;
import com.unilog.utility.CIMMTouchUtility;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.utility.CustomTableModel;
import com.unilog.velocitytool.contextkeyinterface.SocialMediaContextkey;
import com.unilog.express.ExpressServiceImpl;
import com.unilog.express.transaction.model.Items;

import java.util.Date;

import com.paymentgateway.elementexpress.services.Address;
import com.paymentgateway.elementexpress.services.Application;
import com.paymentgateway.elementexpress.services.BooleanType;
import com.paymentgateway.elementexpress.services.Credentials;
import com.paymentgateway.elementexpress.services.EMVEncryptionFormat;
import com.paymentgateway.elementexpress.services.ExtendedParameters;
import com.paymentgateway.elementexpress.services.MarketCode;
import com.paymentgateway.elementexpress.services.PASSUpdaterBatchStatus;
import com.paymentgateway.elementexpress.services.PASSUpdaterOption;
import com.paymentgateway.elementexpress.services.PASSUpdaterStatus;
import com.paymentgateway.elementexpress.services.PaymentAccount;
import com.paymentgateway.elementexpress.services.PaymentAccountParameters;
import com.paymentgateway.elementexpress.services.PaymentAccountType;
import com.paymentgateway.elementexpress.services.Response;
import com.paymentgateway.elementexpress.services.ReversalReason;
import com.paymentgateway.elementexpress.services.ReversalType;
import com.paymentgateway.elementexpress.services.Transaction;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang.RandomStringUtils;
import java.util.concurrent.TimeUnit;
import java.security.SecureRandom;

public class CIMM2VelocityTool {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static CIMM2VelocityTool cimm2VelocityTool =  null;
	private boolean reInitCategoryBrandType = true;
	private ConcurrentHashMap<String, ArrayList<ProductsModel>> brandDataList;
	private ConcurrentHashMap<String, ArrayList<ProductsModel>> typeDataList;
	private ArrayList<ProductsModel> categoryFilterData;
	public static LinkedHashMap<String, String> wareHouseCustomFields =null;
	public static LinkedHashMap<Integer, ProductsModel> featuredBrands = null;
	public static LinkedHashMap<String, List<CustomTable>> customTableData = null;
	
	
	public static LinkedHashMap<String, List<CustomTable>> getCustomTableData() {
		return customTableData;
	}

	public static void setCustomTableData(LinkedHashMap<String, List<CustomTable>> customTableData) {
		CIMM2VelocityTool.customTableData = customTableData;
	}

	public static LinkedHashMap<Integer, ProductsModel> getFeaturedBrands() {
		return featuredBrands;
	}

	public static void setFeaturedBrands(LinkedHashMap<Integer, ProductsModel> featuredBrands) {
		CIMM2VelocityTool.featuredBrands = featuredBrands;
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

	public ArrayList<ProductsModel> getCategoryFilterData() {
		return categoryFilterData;
	}

	public void setCategoryFilterData(ArrayList<ProductsModel> categoryFilterData) {
		this.categoryFilterData = categoryFilterData;
	}
	public void setReInitCategoryBrandType(boolean reInitCategoryBrandType) {
		this.reInitCategoryBrandType = reInitCategoryBrandType;
	}

	public boolean isReInitCategoryBrandType() {
		return reInitCategoryBrandType;
	}

	public void setBrandDataList(ConcurrentHashMap<String, ArrayList<ProductsModel>> brandDataList) {
		this.brandDataList = brandDataList;
	}

	public ConcurrentHashMap<String, ArrayList<ProductsModel>> getBrandDataList() {
		return brandDataList;
	}

	public void setTypeDataList(ConcurrentHashMap<String, ArrayList<ProductsModel>> typeDataList) {
		this.typeDataList = typeDataList;
	}

	public ConcurrentHashMap<String, ArrayList<ProductsModel>> getTypeDataList() {
		return typeDataList;
	}

	private CIMM2VelocityTool(){
		
	}
	
	public static CIMM2VelocityTool getInstance(){
		try{
			synchronized (CIMM2VelocityTool.class) {
				if(cimm2VelocityTool==null){
					cimm2VelocityTool = new CIMM2VelocityTool();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return cimm2VelocityTool;
	}
	
	public String getDynamicStaticPageByName(String pageName){
		String pageContent = "";
		try{
			LinkedHashMap<String, Object> pageContentObject = CIMMTouchUtility.getInstance().getStaticPageById(null, pageName);
			if(pageContentObject!=null){
				pageContent = (String) pageContentObject.get("pageContent");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return pageContent;
	}
	
	
	public String getDynamicStaticPageById(String pageId){
		String pageContent = "";
		try{
			LinkedHashMap<String, Object> pageContentObject = CIMMTouchUtility.getInstance().getStaticPageById(pageId, null);
			if(pageContentObject!=null){
				pageContent = (String) pageContentObject.get("pageContent");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return pageContent;
	}
	
	public ProductsModel getItemDetailByPartNumber(int subsetId,int generalSubset){
		ProductsModel itemDetail = null;
		try{
			ProductHunterSolr.getFeatureProduct(subsetId, generalSubset, 1, 4, false);
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return itemDetail;
	}
	
	public ArrayList<ProductsModel> getClearanceProduct(int subsetId,int generalSubset,HttpSession session){
		ArrayList<ProductsModel> featureProductList = null;
		 ArrayList<ProductsModel> itemDataList = new ArrayList<ProductsModel>();
	      ProductManagementModel productManagement= new ProductManagementModel();
	      Set<Integer> itemSet = new HashSet<Integer>();
	      ProductManagement priceInquiry = new ProductManagementImpl();
	      ArrayList<ProductsModel> resultWithoutDuplicate = new ArrayList<ProductsModel>();
	         ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		try{
			int pageNo = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("NUMBER_OF_CLEARANCE_PRODUCT"));
			if(pageNo==0)
				pageNo = 5;
			featureProductList = ProductHunterSolr.getClearanceProduct(subsetId, generalSubset, 0, pageNo, false);
				 if(featureProductList!=null) {
	             for( ProductsModel itemModel : featureProductList ) {  
	              if(itemSet.add(itemModel.getItemId())) {
	               resultWithoutDuplicate.add(itemModel);
	               partIdentifierQuantity.add(itemModel.getQty());
	              }
	             }
			
	         
	         if(featureProductList!=null) {
	             for( ProductsModel itemModel : featureProductList ) {
	              if(itemSet.add(itemModel.getItemId())) {
	               resultWithoutDuplicate.add(itemModel);
	               partIdentifierQuantity.add(itemModel.getQty());
	              }
	             }
	             productManagement.setPartIdentifierQuantity(partIdentifierQuantity);
	             productManagement.setPartIdentifier(featureProductList);
	             productManagement.setSession(session);
	             if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MAKE_PRICE_ENQUIRY_CLEARANCE_ITEMS")).equalsIgnoreCase("Y") && featureProductList.size()>0){
	            	 itemDataList= priceInquiry.priceInquiry(productManagement, featureProductList);
	             }
	         }
		}
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return featureProductList;
	}
	
	public ArrayList<ProductsModel> getFeaturedProduct(int subsetId,int generalSubset,HttpSession session){
		ArrayList<ProductsModel> featureProductList = null;
		 ArrayList<ProductsModel> itemDataList = new ArrayList<ProductsModel>();
	      ProductManagementModel productManagement= new ProductManagementModel();
	      Set<Integer> itemSet = new HashSet<Integer>();
	      ProductManagement priceInquiry = new ProductManagementImpl();
	      ArrayList<ProductsModel> resultWithoutDuplicate = new ArrayList<ProductsModel>();
	         ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		try{
			int pageNo = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("NUMBER_OF_FEATURE_PRODUCT"));
			if(pageNo==0)
				pageNo = 5;
			featureProductList = ProductHunterSolr.getFeatureProduct(subsetId, generalSubset, 0, pageNo, false);
				 if(featureProductList!=null) {
	             for( ProductsModel itemModel : featureProductList ) {  
	              if(itemSet.add(itemModel.getItemId())) {
	               resultWithoutDuplicate.add(itemModel);
	               partIdentifierQuantity.add(itemModel.getQty());
	              }
	             }
			
	         
	         if(featureProductList!=null) {
	             for( ProductsModel itemModel : featureProductList ) {
	              if(itemSet.add(itemModel.getItemId())) {
	               resultWithoutDuplicate.add(itemModel);
	               partIdentifierQuantity.add(itemModel.getQty());
	              }
	             }
	             productManagement.setPartIdentifierQuantity(partIdentifierQuantity);
	             productManagement.setPartIdentifier(featureProductList);
	             productManagement.setSession(session);
	             if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MAKE_PRICE_ENQUIRY_FEATURED_ITEMS")).equalsIgnoreCase("Y") && featureProductList.size()>0) {
	                 itemDataList= priceInquiry.priceInquiry(productManagement, featureProductList);
	             }
	         }
		}
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return featureProductList;
	}
	
	public LinkedHashMap<String, ArrayList<ProductsModel>> getFeaturedProductGroupList(int subsetId,int generalSubset,HttpSession session){
		ArrayList<ProductsModel> featureProductList = null;
		LinkedHashMap<String, ArrayList<ProductsModel>> featuredProductsGroupMap = null;
		LinkedHashMap<String, ArrayList<ProductsModel>> featuredProductsGroupMapFinal = null;
		ArrayList<ProductsModel> itemDataListForMap = null;
		ArrayList<ProductsModel> itemDataList = new ArrayList<ProductsModel>();
	    ProductManagementModel productManagement= new ProductManagementModel();
	    Set<Integer> itemSet = new HashSet<Integer>();
	    ProductManagement priceInquiry = new ProductManagementImpl();
	    ArrayList<ProductsModel> resultWithoutDuplicate = new ArrayList<ProductsModel>();
	    ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		
	    try{
			int pageNo =  CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("NUMBER_OF_FEATURE_PRODUCT_GROUP_LIST"));
			featureProductList = ProductHunterSolr.getFeatureProduct(subsetId, generalSubset, 0, pageNo, true);
	         if(featureProductList!=null) {
	        	 
	        	 featuredProductsGroupMap = new LinkedHashMap<String, ArrayList<ProductsModel>>();
	        	 itemDataListForMap = new ArrayList<ProductsModel>();
	        	 
	        	 for( ProductsModel itemModel : featureProductList ) {
	        		 if(itemModel!=null && CommonUtility.validateString(itemModel.getMaterialGroup()).length()>0){
	        			 itemDataListForMap = featuredProductsGroupMap.get(CommonUtility.validateString(itemModel.getMaterialGroup()));
	        			 if(itemDataListForMap == null) {
	        				 itemDataListForMap = new ArrayList<ProductsModel>();
	        				 itemDataListForMap.add(itemModel);
	        				 featuredProductsGroupMap.put(CommonUtility.validateString(itemModel.getMaterialGroup()), itemDataListForMap);
	        			 }else {
	        				 itemDataListForMap.add(itemModel);
	        				 featuredProductsGroupMap.put(CommonUtility.validateString(itemModel.getMaterialGroup()), itemDataListForMap);
	        			 }
	        		 }
	        		 
	        	 }
	        	 
	        	 if(featuredProductsGroupMap!=null && !featuredProductsGroupMap.isEmpty()) {
	        		 featuredProductsGroupMapFinal = new LinkedHashMap<String, ArrayList<ProductsModel>>();
	        		 for(String key:featuredProductsGroupMap.keySet()){
	 					ArrayList<Integer> eachItemList = new ArrayList<Integer>();
	 					itemDataListForMap = new ArrayList<ProductsModel>();
	 					itemDataListForMap = featuredProductsGroupMap.get(key);
	 					
	 					for(ProductsModel eachGroupItem:itemDataListForMap){
	 						if(itemSet.add(eachGroupItem.getItemId())) {
	 	 		               resultWithoutDuplicate.add(eachGroupItem);
	 	 		               partIdentifierQuantity.add(eachGroupItem.getQty());
	 	 		            }
	 						eachItemList.add(eachGroupItem.getItemId());
	 					}
	 					//Enable this if item details are not filled from Main item data from ProductHunterSolr.getFeatureProduct
	 					//itemDataListForMap = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId, generalSubset, StringUtils.join(eachItemList," OR "),0,null,"itemid");
	 						 					
	 					 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MAKE_PRICE_ENQUIRY_FEATURED_ITEMS")).equalsIgnoreCase("Y") && itemDataListForMap.size()>0) {
	 						productManagement.setPartIdentifierQuantity(partIdentifierQuantity);
	 			            productManagement.setPartIdentifier(itemDataListForMap);
	 			            productManagement.setSession(session);
	 						itemDataList= priceInquiry.priceInquiry(productManagement, itemDataListForMap);
	 		             }else {
	 		            	itemDataList = itemDataListForMap;
	 		             }
	 					String[] keySplit = key.split("_");
	 					if(keySplit!=null && keySplit.length>1) {
	 						key = CommonUtility.validateString(keySplit[1]);
	 					}
	 					featuredProductsGroupMapFinal.put(key, itemDataList);
	 				}
	        	 }
	         }
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return featuredProductsGroupMapFinal;
	}
	
	public List<CustomTable> getCusomTableData(String entityType,String tableName){
		long startTimer = CommonUtility.startTimeDispaly();
		List<CustomTable> customTableout = null;
		try{
			
			if(customTableData == null){
				customTableData = new LinkedHashMap<>();
			}
			if(customTableData.get(tableName) == null){
			System.out.println("getCusomTableData : entityType-"+CommonUtility.validateString(entityType)+" : tableName-"+CommonUtility.validateString(tableName));
			CustomFieldDAO customFieldDao = new CustomFieldDAO();
			CustomModel customFieldModel = new CustomModel();
			customFieldModel.setCustomFieldEntityType(entityType);
			customFieldModel.setCustomFieldName(tableName);
			customFieldModel.setCustomFieldResultType("JSON");
			Gson gson = new Gson();
			String jsonObject = customFieldDao.getCustomFieldTableDetailsInJson(customFieldModel);
			
			if(!CommonUtility.validateString(jsonObject).isEmpty()){
				jsonObject = "{\"customTableObject\":"+jsonObject+"}";
				CustomTableModel output = gson.fromJson(jsonObject, CustomTableModel.class);
				customTableout = output.getCustomTableObject();	
			}
			customTableData.put(tableName, customTableout);
		}
			customTableout = customTableData.get(tableName);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return customTableout;
	}
	
	public List<CustomTable> getCusomTableData(String entityType,int entityId,String tableName){
		List<CustomTable> customTableout = null;
		try{
			CustomFieldDAO customFieldDao = new CustomFieldDAO();
			CustomModel customFieldModel = new CustomModel();
			customFieldModel.setCustomFieldEntityType(entityType);
			customFieldModel.setCustomFieldName(tableName);
			customFieldModel.setCustomFieldEntityId(entityId);
			customFieldModel.setCustomFieldResultType("JSON");
			Gson gson = new Gson();
			String jsonObject = customFieldDao.getCustomFieldTableDetailsInJson(customFieldModel);
			
			if(!CommonUtility.validateString(jsonObject).isEmpty()){
				jsonObject = "{\"customTableObject\":"+jsonObject+"}";
				CustomTableModel output = gson.fromJson(jsonObject, CustomTableModel.class);
				customTableout = output.getCustomTableObject();
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return customTableout;
	}
	
	public List<CustomTable> getCusomTableDataByFieldValue(int customFieldEntityId,String customFieldEntityType,int customFieldItemPerPage,int customFieldPageNumber,String customFieldName,String customFieldNameList,String customFieldValueList,String sourceColumnName,String sourceTableName){
		List<CustomTable> customTableout = null;
		
		try{
			String[] customFieldNameListArr = CommonUtility.validateString(customFieldNameList).split(",");
			String[] customFieldValueListArr = CommonUtility.validateString(customFieldValueList).split(",");
			CustomModel customFieldModel = new CustomModel();
			customFieldModel.setCustomFieldEntityId(customFieldEntityId);
			customFieldModel.setCustomFieldEntityType(customFieldEntityType);
			customFieldModel.setCustomFieldItemPerPage(customFieldItemPerPage);
			customFieldModel.setCustomFieldName(customFieldName);
			customFieldModel.setCustomFieldNameList(customFieldNameListArr);
			customFieldModel.setCustomFieldPageNumber(customFieldPageNumber);
			customFieldModel.setCustomFieldResultType("JSON");
			customFieldModel.setCustomFieldValueList(customFieldValueListArr);
			customFieldModel.setSourceColumnName(sourceColumnName);
			customFieldModel.setSourceTableName(sourceTableName);
			customTableout = getCusomTableDataByFieldValue(customFieldModel);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return customTableout;
	}
	
	
	public List<CustomTable> getCusomTableFormBuilder(int customFieldEntityId,String customFieldEntityType,int customFieldItemPerPage,int customFieldPageNumber,String customFieldName,String sortOn){
		List<CustomTable> customTableout = null;
		
		try{
			
			CustomModel customFieldModel = new CustomModel();
			customFieldModel.setCustomFieldEntityId(customFieldEntityId);
			customFieldModel.setCustomFieldEntityType(customFieldEntityType);
			customFieldModel.setCustomFieldItemPerPage(customFieldItemPerPage);
			customFieldModel.setCustomFieldName(customFieldName);
			customFieldModel.setSortOn(sortOn);
			customFieldModel.setCustomFieldPageNumber(customFieldPageNumber);
			customFieldModel.setCustomFieldResultType("JSON");

			customTableout = getCusomTableFormBuilderParser(customFieldModel);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return customTableout;
	}
	
	public List<CustomTable> getCusomTableFormBuilderParser(CustomModel customFieldModel){
		List<CustomTable> customTableout = null;
		try{
			Gson gson = new GsonBuilder().create();
			
			String jsonObject = CustomFieldDAO.getCustomFieldTableFormBuilder(customFieldModel);
			
			if(!CommonUtility.validateString(jsonObject).isEmpty()){
				jsonObject = "{\"customTableObject\":"+jsonObject+"}";
				CustomTableModel output = gson.fromJson(jsonObject, CustomTableModel.class);
				customTableout = output.getCustomTableObject();
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return customTableout;
	}
	
	public List<CustomTable> getCusomTableDataByFieldValue(CustomModel customFieldModel){
		List<CustomTable> customTableout = null;
		try{
			Gson gson = new GsonBuilder().create();
			
			String jsonObject = CustomFieldDAO.getCustomFieldTableByFieldValue(customFieldModel);
			
			if(!CommonUtility.validateString(jsonObject).isEmpty()){
				jsonObject = "{\"customTableObject\":"+jsonObject+"}";
				CustomTableModel output = gson.fromJson(jsonObject, CustomTableModel.class);
				customTableout = output.getCustomTableObject();
				for(CustomTable test:customTableout){
					if(test.getTableDetails().get(0).get("DISPLAY_SEQUENCE")!=null){
						test.setDisplaySequence(CommonUtility.validateNumber(test.getTableDetails().get(0).get("DISPLAY_SEQUENCE")));	
					}else{
						test.setDisplaySequence(0);	
					}
				}
				Collections.sort(customTableout, CustomTable.DisplaySeqComparator);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return customTableout;
	}
	
	public ArrayList<ResponseBlogEachPost> getBlogPosts(){
		ArrayList<ResponseBlogEachPost> responseBlogPost = new ArrayList<ResponseBlogEachPost>();
		int numberOfPostsToDisplay = 1;
			try {
				ResponseBlog output = new ResponseBlog();
				LinkedHashMap<String, String> contentObject = new LinkedHashMap<String, String>();
				
				StringBuilder finalUrl = new StringBuilder();
				finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CONNECTION_URL)));
				finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_BLOG_URL_SOURCE)));
				//finalUrl.append("?noOfStatus=").append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.BLOG_NUMBER_OF_POSTS)));
				
				if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.BLOG_NUMBER_OF_POSTS))>0){
					numberOfPostsToDisplay = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.BLOG_NUMBER_OF_POSTS));
				}
				
				contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_FINAL_REQUEST_URL, finalUrl.toString());
				contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID)));
				contentObject.put(SocialMediaContextkey.SITE_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.SITE_ID)));
				
				Cimm2BCentralConnection cimm2BCentralConnection = new Cimm2BCentralConnection();
				HttpURLConnection conn = cimm2BCentralConnection.connectCim2BCentralByGetMethod(contentObject);
				
				BufferedReader in;
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				String outputTxt = "";
				while ((inputLine = in.readLine()) != null) {
					outputTxt = outputTxt + inputLine;
				}
				
				Gson gson = new Gson();
				output = gson.fromJson(outputTxt, ResponseBlog.class);
				System.out.println("Blog output : "+output);
				
				if(output!=null && output.getData()!=null && output.getData().size()>0){
					int index = 1;
					for(ResponseDataBlog responseDataBlog : output.getData()){
						ResponseBlogEachPost responseBlogEachPost = getBlogDetailedPosts(responseDataBlog.getId());
						if(responseBlogEachPost!=null){
							responseBlogPost.add(responseBlogEachPost);
						}
						if(numberOfPostsToDisplay==index)
							break;
						index++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return responseBlogPost;
	}
	
	public ResponseBlogEachPost getBlogDetailedPosts(String webLogId){
		
		ResponseBlogEachPost output = new ResponseBlogEachPost();	
			try {
				
				LinkedHashMap<String, String> contentObject = new LinkedHashMap<String, String>();
				
				StringBuilder finalUrl = new StringBuilder();
				finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CONNECTION_URL)));
				finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_BLOG_POST_DETAIL_URL_SOURCE)));
				finalUrl.append("?weblogId=").append(CommonUtility.validateString(webLogId));
				
				contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_FINAL_REQUEST_URL, finalUrl.toString());
				contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID)));
				contentObject.put(SocialMediaContextkey.SITE_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.SITE_ID)));
				
				Cimm2BCentralConnection cimm2BCentralConnection = new Cimm2BCentralConnection();
				HttpURLConnection conn = cimm2BCentralConnection.connectCim2BCentralByGetMethod(contentObject);
				
				BufferedReader in;
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				String outputTxt = "";
				while ((inputLine = in.readLine()) != null) {
					outputTxt = outputTxt + inputLine;
				}
				
				Gson gson = new Gson();
				output = gson.fromJson(outputTxt, ResponseBlogEachPost.class);
				System.out.println("Blog output : "+output);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return output;
	}
	
	public BlogFeed getBlogFeed(){
		
		BlogFeed output = new BlogFeed();
		
		try {
			
			LinkedHashMap<String, String> contentObject = new LinkedHashMap<String, String>();
			
			StringBuilder finalUrl = new StringBuilder();
			finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CONNECTION_URL)));
			finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_BLOG_POST_DETAIL_URL_SOURCE)));
			finalUrl.append("?feedUrl=").append(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_BLOG_FEED_URL));
			finalUrl.append("&noOfEntry=").append(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.BLOG_NUMBER_OF_POSTS));
			
			contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_FINAL_REQUEST_URL, finalUrl.toString());
			contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID)));
			contentObject.put(SocialMediaContextkey.SITE_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.SITE_ID)));
			
			Cimm2BCentralConnection cimm2BCentralConnection = new Cimm2BCentralConnection();
			HttpURLConnection conn = cimm2BCentralConnection.connectCim2BCentralByGetMethod(contentObject);
			
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			String outputTxt = "";
			while ((inputLine = in.readLine()) != null) {
				outputTxt = outputTxt + inputLine;
			}
			System.out.println("Blog : "+outputTxt);
			Gson gson = new Gson();
			output = gson.fromJson(outputTxt, BlogFeed.class);
			
			if(output!=null){
				for(EntiesModel emodel : output.getData().getEntries()){
					if(CommonUtility.validateString(emodel.getContent()).length()>0){
						Document doc = Jsoup.parse(CommonUtility.validateString(emodel.getContent()));
						Elements img = doc.getElementsByTag("img");
						int index = 0;
						for (Element elment : img) {
							if(index<1){
								emodel.setImageUrl(CommonUtility.validateString(elment.absUrl("src")));
							}
							index++;
						}
						emodel.setContent(Jsoup.parse(CommonUtility.validateString(emodel.getContent())).text());
					}
					if(CommonUtility.validateString(emodel.getDescription()).length()>0){
						emodel.setDescription(Jsoup.parse(CommonUtility.validateString(emodel.getDescription())).text());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return output;
	}
	
	
	public BlogFeed getBlogFeedSearch(String keyWord, String searchType){
		
		BlogFeed output = new BlogFeed();
		
		try {
			
			LinkedHashMap<String, String> contentObject = new LinkedHashMap<String, String>();
			
			StringBuilder finalUrl = new StringBuilder();
			finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CONNECTION_URL)));
			finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_BLOG_POST_DETAIL_URL_SOURCE)));
			if(CommonUtility.validateString(searchType).equalsIgnoreCase("CASE_STUDIES")){
				finalUrl.append("?feedUrl=").append(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CASE_STUDIES_FEED_URL));
			}else if(CommonUtility.validateString(searchType).equalsIgnoreCase("PRODUCT_ARTICLE")){
				finalUrl.append("?feedUrl=").append(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_PRODUCT_ARTICLE_FEED_URL));
			}else{
				finalUrl.append("?feedUrl=").append(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_BLOG_FEED_URL));
			}
			
			
			
			finalUrl.append("&search=").append(CommonUtility.validateString(keyWord).replaceAll(" ","+"));
			
			contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_FINAL_REQUEST_URL, finalUrl.toString());
			contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID)));
			contentObject.put(SocialMediaContextkey.SITE_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.SITE_ID)));
			
			Cimm2BCentralConnection cimm2BCentralConnection = new Cimm2BCentralConnection();
			HttpURLConnection conn = cimm2BCentralConnection.connectCim2BCentralByGetMethod(contentObject);
			
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			String outputTxt = "";
			while ((inputLine = in.readLine()) != null) {
				outputTxt = outputTxt + inputLine;
			}
			System.out.println("Blog Search : "+outputTxt);
			Gson gson = new Gson();
			output = gson.fromJson(outputTxt, BlogFeed.class);
			System.out.println("Blog output : "+output);
			
			if(output!=null && output.getData()!=null && output.getData().getEntries()!=null){
				int index = 0;
				ArrayList<EntiesModel> entries = new ArrayList<EntiesModel>();
				//System.out.println("(1) output.getData().getEntries().size() : "+output.getData().getEntries().size());
				for(int i=0; i<output.getData().getEntries().size(); i++){
					if(index<CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("DEFAULT_NUMBER_OF_STATIC_RESULT"))){
						entries.add(output.getData().getEntries().get(i));
					}else{
						break;
					}
					index++;
				}
				output.getData().setEntries(entries);
				System.out.println("(2) output.getData().getEntries().size() : "+output.getData().getEntries().size());
				for(EntiesModel emodel : output.getData().getEntries()){
					if(CommonUtility.validateString(emodel.getContent()).length()>0){
						emodel.setContent(Jsoup.parse(CommonUtility.validateString(emodel.getContent())).text());
					}
					if(CommonUtility.validateString(emodel.getDescription()).length()>0){
						emodel.setDescription(Jsoup.parse(CommonUtility.validateString(emodel.getDescription())).text());
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return output;
	}
	
	public ResponseFaceBook getFacebookPosts(){
		
		ResponseFaceBook output = new ResponseFaceBook();
		
		try {
			
			LinkedHashMap<String, String> contentObject = new LinkedHashMap<String, String>();
			
			StringBuilder finalUrl = new StringBuilder();
			finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CONNECTION_URL)));
			finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_FACEBOOK_URL_SOURCE)));
			finalUrl.append("?noOfPosts=").append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.FACEBOOK_NUMBER_OF_POSTS)));
			
			contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_FINAL_REQUEST_URL, finalUrl.toString());
			contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID)));
			contentObject.put(SocialMediaContextkey.SITE_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.SITE_ID)));
			
			Cimm2BCentralConnection cimm2BCentralConnection = new Cimm2BCentralConnection();
			HttpURLConnection conn = cimm2BCentralConnection.connectCim2BCentralByGetMethod(contentObject);
			
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			String outputTxt = "";
			while ((inputLine = in.readLine()) != null) {
				outputTxt = outputTxt + inputLine;
			}
			System.out.println("Facebook : "+outputTxt);
			//Gson gson = new Gson();
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			output = gson.fromJson(outputTxt, ResponseFaceBook.class);
			System.out.println("Facebook output : "+output);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return output;
	}

	public ResponseTwitter getTwittePosts(){
		ResponseTwitter twitterPost = new ResponseTwitter();
		try {
			LinkedHashMap<String, String> contentObject = new LinkedHashMap<String, String>();
			StringBuilder finalUrl = new StringBuilder();
			finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CONNECTION_URL)));
			finalUrl.append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_TWITTER_URL_SOURCE)));
			finalUrl.append("?noOfStatus=").append(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.TWITTER_NUMBER_OF_POSTS)));
			
			contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_FINAL_REQUEST_URL, finalUrl.toString());
			contentObject.put(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.CIMM2BCENTRAL_CLIENT_ID)));
			contentObject.put(SocialMediaContextkey.SITE_ID, CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(SocialMediaContextkey.SITE_ID)));
			
			Cimm2BCentralConnection cimm2BCentralConnection = new Cimm2BCentralConnection();
			HttpURLConnection conn = cimm2BCentralConnection.connectCim2BCentralByGetMethod(contentObject);
			
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			String outputTxt = "";
			while ((inputLine = in.readLine()) != null) {
				outputTxt = outputTxt + inputLine;
			}
			
			System.out.println("Twitter : "+outputTxt);
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			twitterPost = gson.fromJson(outputTxt, ResponseTwitter.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return twitterPost;
	}
	
	public void categoryBrandAttributeFilter(int subsetId,int generalSubsetId){
		ProductsModel categoryData = null;
		ProductsModel searchResultData = null;
		try{
			//reInitCategoryBrandType = true;
				if(reInitCategoryBrandType && CommonDBQuery.getSystemParamtersList().get("CATEGORY_BRAND_ATTRIBUTE_FILTER")!=null){
					brandDataList = new ConcurrentHashMap<String, ArrayList<ProductsModel>>();
					typeDataList = new ConcurrentHashMap<String, ArrayList<ProductsModel>>();
					ArrayList<ProductsModel> brandFilter = null;
					ArrayList<ProductsModel> typeFilter = null;
					String indexType = "PH_SEARCH_"+subsetId;
					String categoryList[] = CommonDBQuery.getSystemParamtersList().get("CATEGORY_BRAND_ATTRIBUTE_FILTER").split(",");
					if(generalSubsetId>0 && generalSubsetId!=subsetId){
						indexType = "PH_SEARCH_"+generalSubsetId+"_"+subsetId;
					}
					String faucetField[] = new String[1];
					faucetField[0]  = "brand";
					String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey());
					String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
					if(!siteNameSolr.equalsIgnoreCase(""))
					{
						fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
					}
					String filterString = "";
					
					for(String category:categoryList){
						categoryData = ProductHunterSolr.searchCategory(category, subsetId, generalSubsetId);

						if(categoryData!=null && !CommonUtility.validateString(categoryData.getCategoryCode()).isEmpty()){
							HashMap<String, ArrayList<ProductsModel>> categoryListData = ProductHunterSolr.taxonomyList("", subsetId, generalSubsetId, 1, 10, "NAVIGATION", 0, 0, CommonUtility.validateNumber(categoryData.getCategoryCode()), CommonUtility.validateNumber("0"),null,null,null,null,0,"",0,"");
							categoryFilterData = categoryListData.get("categeoryList");
							if(categoryFilterData!=null && categoryFilterData.size()>10){
								categoryFilterData = new ArrayList<ProductsModel>(categoryFilterData.subList(0, 10));
							}
							
							for(ProductsModel catogeryFilter:categoryFilterData){
								faucetField[0]  = "brand";
								searchResultData = getItemDataFromSolr(faucetField, fqNav.split("~"), 1, "catSearchId:"+catogeryFilter.getCategoryCode());
								brandFilter = getAttributeFilter(searchResultData,10,"brand");
								if(brandFilter!=null && brandFilter.size()>0)
									
								brandDataList.put(catogeryFilter.getCategoryCode(), brandFilter);
								for(ProductsModel brandList:brandFilter){
									faucetField[0]  = "attr_Type";
									filterString = fqNav + "~" + "brand:(\""+brandList.getAttrValue()+"\")";
									searchResultData = getItemDataFromSolr(faucetField, filterString.split("~"), 1, "catSearchId:"+catogeryFilter.getCategoryCode());
									typeFilter = getAttributeFilter(searchResultData,10,"Type");
									if(typeFilter!=null && typeFilter.size()>0){
										typeFilter.get(0).setCategoryCode(catogeryFilter.getCategoryCode());
										typeFilter.get(0).setBrandName(brandList.getAttrValueEncoded());
									typeDataList.put(catogeryFilter.getCategoryCode()+"_"+brandList.getAttrValue().replaceAll(" ", "_"), typeFilter);
									}
									else{
										System.out.println("No Attribute for Brand : " + brandList.getAttrValue() + " - " + catogeryFilter.getCategoryName());
									}
								}
							}
						}
						
					}
					
					reInitCategoryBrandType = false;
				}
			}catch (Exception e) {
			   e.printStackTrace();
		   }
	}

	public ProductsModel getItemDataFromSolr(String faucetField[],String filterQuery[],int numberOfResult,String innerFilterQuery){
		ProductsModel searchResultData = null;
		try{
			searchResultData = ProductHunterSolrUltimate.solrNavigationSearchResult("*:*", CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/mainitemdata", filterQuery, 0, numberOfResult, "", faucetField, "", "partnumber", SolrQuery.ORDER.asc, true, innerFilterQuery,null,null,null,null,false);
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return searchResultData;
		
	}
	
	public ArrayList<ProductsModel> getAttributeFilter(ProductsModel searchResultData,int numberOfList,String attrName){
		ArrayList<ProductsModel> brandFilter = null;
		try{
			if(searchResultData!=null && searchResultData.getItemDataList().size()>0){
				
				for(ProductsModel filterList:searchResultData.getAttributeDataList()){
					brandFilter = filterList.getAttrFilterList().get(attrName);
					if(numberOfList>0 && brandFilter!=null && brandFilter.size()>numberOfList){
						System.out.println("Found : " + brandFilter.size());
						brandFilter = new ArrayList<ProductsModel>(brandFilter.subList(0, numberOfList));
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return brandFilter;
	}
	
	public ProductsModel getBranchDetails(String branchID){
		ProductsModel branch = null;
		try{
				branch = CommonDBQuery.branchDetailData.get(branchID);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return branch;
	}
	
	public ArrayList<ProductsModel> featureProductsListCimmVelocity(int generalSubset, int subsetId){
		ArrayList<ProductsModel> featureProducts = new ArrayList<ProductsModel>();
		try {
			featureProducts = ProductsDAO.getFeatureProduct(subsetId, generalSubset);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return featureProducts;
	}
	
	public ArrayList<ProductsModel> eventListCimmVelocity(){
		ArrayList<ProductsModel> eventsList = new ArrayList<ProductsModel>();
		try {
			eventsList = ProductHunterSolr.getEventDetails();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return eventsList;
	}
	
	public LinkedHashMap<Integer, ProductsModel> featuredBrandsVelocity(){
		
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			if(featuredBrands==null){
				featuredBrands = new LinkedHashMap<>();
			}
			/*if(session!=null && session.getAttribute("featuredBrands")!=null && (LinkedHashMap<Integer, ProductsModel>)session.getAttribute("featuredBrands")!=null){
				featuredBrands = (LinkedHashMap<Integer, ProductsModel>)session.getAttribute("featuredBrands");
			}else{*/
			if(featuredBrands.size() == 0){
				featuredBrands = ProductsDAO.getBrandCustomFieldDetailsFromView("FEATURED_BRANDS");
			}
				session.setAttribute("featuredBrands", featuredBrands);
			/*}*/
		}catch (Exception e) {
			e.printStackTrace();
		}
		return featuredBrands;
	}
	
	public LinkedHashMap<Integer, ProductsModel> featuredManufacturersCimmVelocity(){
		LinkedHashMap<Integer, ProductsModel> featuredManufacturers = new LinkedHashMap<Integer, ProductsModel>();
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			/*if(session!=null && session.getAttribute("featuredManufacturers")!=null && (LinkedHashMap<Integer, ProductsModel>)session.getAttribute("featuredManufacturers")!=null){
				featuredManufacturers = (LinkedHashMap<Integer, ProductsModel>)session.getAttribute("featuredManufacturers");
			}else{*/								
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_MANUFACTURER_CUSTOMFIELD")).equalsIgnoreCase("Y")) {				
					featuredManufacturers = ProductsDAO.getManufacturerMultipleCustomFields("FEATURED_MANUFACTURER");
				}
				else {
				    featuredManufacturers = ProductsDAO.getManufacturerCustomFieldsFromView("FEATURED_MANUFACTURER");
				}
				session.setAttribute("featuredManufacturers", featuredManufacturers);
			/*}*/
		}catch (Exception e) {
			e.printStackTrace();
		}
		return featuredManufacturers;
	}
	
	public ArrayList<ProductsModel> featuredBrands(){
		ArrayList<ProductsModel> featuredBrands = new ArrayList<ProductsModel>();
		try {
			featuredBrands = ProductsDAO.getFeaturedBrand("FEATURED_BRANDS");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return featuredBrands;
	}
	
	public ArrayList<ProductsModel> featuredManufacturers(){
		ArrayList<ProductsModel> featuredManufacturers = new ArrayList<ProductsModel>();
		try {
			request = ServletActionContext.getRequest();
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_MANUFACTURER_CUSTOMFIELD")).equalsIgnoreCase("Y")) {				
				featuredManufacturers = ProductsDAO.getFeaturedManufacturerMultipleCustomFields("FEATURED_MANUFACTURER");
			}
			else {
			    featuredManufacturers = ProductsDAO.getFeaturedManufacturer("FEATURED_MANUFACTURER");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return featuredManufacturers;
	}
	
	public ArrayList<ProductsModel> categoryMenus(){
		ArrayList<ProductsModel> categMenu = new ArrayList<ProductsModel>();
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			
			String tempSubset = (String) session.getAttribute("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String searchIndex = "PH_SEARCH_"+subsetId;
			if(generalSubset>0 && generalSubset!=subsetId){
				searchIndex = "PH_SEARCH_"+generalSubset+"_"+subsetId;
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
				searchIndex = "PH_SEARCH_ALL";
			}
			categMenu = MenuAndBannersDAO.getTopMenuListBySubset().get(searchIndex);
			session.setAttribute("categMenuCimmVelovityTool", categMenu);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return categMenu;
	}
	
	public Map<String, Object> allCategoryMenus(){
		Map<String, Object> categoryMenus = new HashMap<>();
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			
			String tempSubset = (String) session.getAttribute("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String searchIndex = "PH_SEARCH_"+subsetId;
			if(generalSubset>0 && generalSubset!=subsetId){
				searchIndex = "PH_SEARCH_"+generalSubset+"_"+subsetId;
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
				searchIndex="PH_SEARCH_ALL";
			}
			categoryMenus.put("categMenu", MenuAndBannersDAO.getTopMenuListBySubset().get(searchIndex));
			categoryMenus.put("secondLevelMenu", MenuAndBannersDAO.getAllSecMenuList().get(searchIndex));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return categoryMenus;
	
	}
	
	public Map<String, Object> brandsMenus(){
		Map<String, Object> brandsMenus = new HashMap<>();
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			
			String tempSubset = (String) session.getAttribute("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String searchIndex = "PH_SEARCH_"+subsetId;
			if(generalSubset>0 && generalSubset!=subsetId){
				searchIndex = "PH_SEARCH_"+generalSubset+"_"+subsetId;
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
				searchIndex="PH_SEARCH_ALL";
			}
			brandsMenus.put("brandMenu", MenuAndBannersDAO.getAllBrandData().get(searchIndex+"_MV"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return brandsMenus;	
	}
	
	public String taxonomyImageName(){
		String taxonomyImage = CommonDBQuery.getSystemParamtersList().get("TAXONOMYIMAGEPATH");
		return taxonomyImage;
	}
	public String paymentAccountCreate(String transactionID){
		String paymentAccountId = null;
		try{
			System.out.println("Creating payment account id");
			ExpressServiceImpl expService = new ExpressServiceImpl();
			long referenceNumber = new Date().getTime();

			Credentials credentials = new Credentials();
			Application application = new Application();
			Transaction transaction = new Transaction();
			PaymentAccount paymentAccount = new PaymentAccount();
			Address address = null;
			ExtendedParameters[] extendedParameters = null;

			application.setApplicationID(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID"));
			application.setApplicationName(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_NAME"));
			application.setApplicationVersion(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_VERSION"));

			credentials.setAccountID(CommonDBQuery.getSystemParamtersList().get("TRANS_ACCOUNT_ID"));
			credentials.setAccountToken(CommonDBQuery.getSystemParamtersList().get("TRANS_ACCOUNT_TOKEN"));
			credentials.setAcceptorID(CommonDBQuery.getSystemParamtersList().get("TRANS_ACCEPTOR_ID"));

			transaction.setTransactionID(transactionID);
			transaction.setReversalType(ReversalType.System);
			transaction.setMarketCode(MarketCode.ECommerce);
			transaction.setBillPaymentFlag(BooleanType.False);
			transaction.setDuplicateCheckDisableFlag(BooleanType.False);
			transaction.setDuplicateOverrideFlag(BooleanType.True);
			transaction.setRecurringFlag(BooleanType.False);
			transaction.setPartialApprovedFlag(BooleanType.True);
			transaction.setEMVEncryptionFormat(EMVEncryptionFormat.Default);
			transaction.setReversalReason(ReversalReason.Unknown);
			paymentAccount.setPaymentAccountType(PaymentAccountType.CreditCard);
			paymentAccount.setPASSUpdaterBatchStatus(PASSUpdaterBatchStatus.Null);
			paymentAccount.setPASSUpdaterOption(PASSUpdaterOption.Null);
			paymentAccount.setPaymentAccountReferenceNumber(Long.toString(referenceNumber));
			Response response = expService.paymentAccountCreateWithTransID(credentials, application, transaction, paymentAccount, address, extendedParameters);
			if(response.getPaymentAccount()!=null && response.getPaymentAccount().getPaymentAccountID()!=null){
				paymentAccountId = response.getPaymentAccount().getPaymentAccountID();
				System.out.println("paymentAccountId : " + paymentAccountId);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		return paymentAccountId;
	}
	public Items paymentAccountQuery(String paymentAccountID){
		Items paymentQueryResponse = null;
		try{
			ExpressServiceImpl expService = new ExpressServiceImpl();


			Credentials credentials = new Credentials();
			Application application = new Application();
			PaymentAccountParameters paymentAccountParameters = new PaymentAccountParameters();
			ExtendedParameters[] extendedParameters = null;

			application.setApplicationID(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_ID"));
			application.setApplicationName(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_NAME"));
			application.setApplicationVersion(CommonDBQuery.getSystemParamtersList().get("TRANS_APPLICATION_VERSION"));

			credentials.setAccountID(CommonDBQuery.getSystemParamtersList().get("TRANS_ACCOUNT_ID"));
			credentials.setAccountToken(CommonDBQuery.getSystemParamtersList().get("TRANS_ACCOUNT_TOKEN"));
			credentials.setAcceptorID(CommonDBQuery.getSystemParamtersList().get("TRANS_ACCEPTOR_ID"));

			/*application.setApplicationID("16");
			application.setApplicationName("GAINESVILLE");
			application.setApplicationVersion("2.5.1");

			credentials.setAccountID("1011063");
			credentials.setAccountToken("5BB722C2973B8C564BB15CAC878A6DBAB931A37CFEACA69A8432D0A60FFA87E5497DA201");
			credentials.setAcceptorID("345190700889");*/

			paymentAccountParameters.setPaymentAccountID(paymentAccountID);
			paymentAccountParameters.setPaymentAccountType(PaymentAccountType.CreditCard);
			paymentAccountParameters.setPASSUpdaterStatus(PASSUpdaterStatus.Null);
			paymentAccountParameters.setPASSUpdaterBatchStatus(PASSUpdaterBatchStatus.Null);

			Response response = expService.paymentAccountQuery(credentials, application, paymentAccountParameters, extendedParameters );

			if(response!=null && response.getQueryData()!=null){
				System.out.println("QueryData : " + response.getQueryData());
				javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(Items.class);
				java.io.StringReader reader = new java.io.StringReader(response.getQueryData());
				javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				paymentQueryResponse = (Items) jaxbUnmarshaller.unmarshal(reader);
				System.out.println(paymentQueryResponse.getItem().getTruncatedCardNumber());
			}


		}catch (Exception e) {
			e.printStackTrace();
		}
		return paymentQueryResponse;
	}
	public static JsonObject geoLocationByZipCode(String zipCode) {
		JsonObject location = null;
		String url = CommonDBQuery.getSystemParamtersList().get("GOOGLE_LOCATION_BY_ZIPCODE");
		String defaultCountry = CommonDBQuery.getSystemParamtersList().get("GOOGLE_LOCATION_DEFAULT_COUNTRY");
		
		url += "?address=" + zipCode;
		if(CommonUtility.validateString(defaultCountry).length()>0) {
			url += "&components=country:"+defaultCountry;
		}else {
			url += "&components=country:US";
		}
		
		url += "&sensor=true";
		url += "&key=" + CommonDBQuery.getSystemParamtersList().get("GEO_API_KEY");
		
		CloseableHttpClient httpClient;
		try {
			httpClient = HttpClientBuilder.create().setSslcontext(SSLContexts.custom().useProtocol("TLSv1.1").build()).build();
			httpClient = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier(null)).build();
			HttpGet httpGetRequest = new HttpGet(url);
			httpGetRequest.setHeader("Content-type", "application/json");
			
			HttpResponse response = httpClient.execute(httpGetRequest);
			String jsonResponseStr = EntityUtils.toString(response.getEntity());
			
			JsonParser parser = new JsonParser();
			location = (JsonObject) parser.parse(jsonResponseStr);
			
			System.out.println("JSON Response " + location);
			
		} catch (KeyManagementException e) {
			System.out.println("Key management Exception while using custom TLSv1.1");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException Exception while using custom TLSv1.1");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			System.out.println("ClientProtocolException Exception while using custom TLSv1.1");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception while estabkishing UPS cnnection");
			e.printStackTrace();
		}
		return location;
	}
	public String getCreditCardCode(List<CustomTable> creditCardList,String cardName){
		String creditCardCode = null;
		try{
			for(CustomTable cardList:creditCardList){
				List<java.util.Map<String, String>> cardDataList = cardList.getTableDetails();
				for(java.util.Map<String, String> cardData:cardDataList){
					System.out.println(cardData.get("CREDIT_CARD_NAME"));
					if(cardData.get("CREDIT_CARD_NAME")!=null && cardData.get("CREDIT_CARD_NAME").toUpperCase().trim().equalsIgnoreCase(cardName.toUpperCase())){
						System.out.println("Card Code : " + cardData.get("CREDIT_CARD_CODE"));
						creditCardCode = cardData.get("CREDIT_CARD_CODE");
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return creditCardCode;
	}
	public ArrayList<ProductsModel> featuredCategories(){
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		String tempSubset = (String) session.getAttribute("userSubsetId");
		int subsetId = CommonUtility.validateNumber(tempSubset);
		ArrayList<ProductsModel> featuredCategeories = new ArrayList<ProductsModel>();
		HashMap<String, ArrayList<ProductsModel>> featuredCategeoryList = new HashMap<String, ArrayList<ProductsModel>>();
		try {
			//featuredCategeoryList = ProductHunterSolr.taxonomyList("", subsetId, generalSubset, 1, 100, "NAVIGATION", 0, 0, CommonUtility.validateNumber("0"), CommonUtility.validateNumber("0"),null,null,session.getId(),null,0,"",0,"");
			featuredCategeoryList = ProductHunterSolr.taxonomyList("", subsetId, generalSubset, 1, 100, "NAVIGATION", 0, 0, CommonUtility.validateNumber("0"), CommonUtility.validateNumber("0"),null,null,session.getId(),null,0,"",0,"FeatureCategories");
			featuredCategeories = featuredCategeoryList.get("featuredCategeoryList");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return featuredCategeories;
	}
	public static String getWareHouseCusomTableData(String entityType,String tableName){
		List<CustomTable> customTableout = null;
		if(wareHouseCustomFields == null){
			wareHouseCustomFields = new LinkedHashMap<>();
		}
		String jsonObject =null;
		try{
			if(wareHouseCustomFields.get(tableName) == null){
			System.out.println("getCusomTableData : entityType-"+CommonUtility.validateString(entityType)+" : tableName-"+CommonUtility.validateString(tableName));
			CustomFieldDAO customFieldDao = new CustomFieldDAO();
			CustomModel customFieldModel = new CustomModel();
			customFieldModel.setCustomFieldEntityType(entityType);
			customFieldModel.setCustomFieldName(tableName);
			customFieldModel.setCustomFieldResultType("JSON");
			Gson gson = new Gson();
			 String tempJsonObject = customFieldDao.getCustomFieldTableDetailsInJson(customFieldModel);
			 wareHouseCustomFields.put(tableName, tempJsonObject);
			}
			jsonObject = wareHouseCustomFields.get(tableName);
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return jsonObject;
	}
	
	public EntiesModel parseBlogFeed(String content,String description){
		EntiesModel blogData = new EntiesModel();
		try{

			if(CommonUtility.validateString(content).length()>0){
				Document doc = Jsoup.parse(CommonUtility.validateString(content));
				Elements img = doc.getElementsByTag("img");
				int index = 0;
				for (Element elment : img) {
					if(index<1){
						blogData.setImageUrl(CommonUtility.validateString(elment.absUrl("src")));
					}
					index++;
				}
				blogData.setContent(Jsoup.parse(CommonUtility.validateString(content)).text());
			}
			if(CommonUtility.validateString(description).length()>0){
				blogData.setDescription(Jsoup.parse(CommonUtility.validateString(description)).text());
			}
		
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return blogData;
	}
	public ArrayList<ProductsModel> recentlyOrderedItems(){
		
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String tempsubsetId = (String) session.getAttribute("userSubsetId");
		int subsetId = CommonUtility.validateNumber(tempsubsetId);
	    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		int generalSubsetId = CommonUtility.validateNumber(tempGeneralSubset);
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
	    ArrayList<ProductsModel> recentlyOrderedItems = ProductsDAO.getRecentlyOrderedItems(userId, subsetId, generalSubsetId);
	    if (recentlyOrderedItems != null && recentlyOrderedItems.size() > 0) {
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		ArrayList<String> itemIds = new ArrayList<>();
		for (ProductsModel eachProduct : recentlyOrderedItems) {
			itemIds.add(String.valueOf(eachProduct.getItemId()));
			partIdentifierQuantity.add(eachProduct.getItemId());
		}
		/*if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CPN_FOR_RECENTLY_PURCHASED_ITEMS")).equalsIgnoreCase("Y")) {
			LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumbers = ProductHunterSolr.getcustomerPartnumber(StringUtils.join(itemIds, " OR "), Integer.parseInt(buyingCompanyId), Integer.parseInt(buyingCompanyId));
			contentObject.put("customerPartNumbers", customerPartNumbers);
		}*/
		if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRICE_ENQUIRY_FOR_RECENT_ORDERED_ITEMS")).equalsIgnoreCase("Y")) {
			ProductManagementModel productManagement = new ProductManagementModel();
			ProductManagement priceInquiry = new ProductManagementImpl();
			productManagement.setPartIdentifier(recentlyOrderedItems);
			productManagement.setPartIdentifierQuantity(partIdentifierQuantity);
			recentlyOrderedItems = priceInquiry.priceInquiry(productManagement, recentlyOrderedItems);
		}
	}
	    
	return recentlyOrderedItems;
	}

	public static LinkedHashMap<String, String> getWareHouseCustomFields() {
		return wareHouseCustomFields;
	}

	public static void setWareHouseCustomFields(LinkedHashMap<String, String> wareHouseCustomFields) {
		CIMM2VelocityTool.wareHouseCustomFields = wareHouseCustomFields;
	}
	
	public String  generateSalt() {
		 
		 // Assume its HTTP
		request = ServletActionContext.getRequest();
	     HttpServletRequest httpReq = (HttpServletRequest) request;

	     // Check the user session for the salt cache, if none is present we create one
	     Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, Boolean>)
	         httpReq.getSession().getAttribute("csrfPreventionSaltCache");

	     if (csrfPreventionSaltCache == null){
	         csrfPreventionSaltCache = CacheBuilder.newBuilder()
	             .maximumSize(5000)
	             .expireAfterWrite(5, TimeUnit.MINUTES)
	             .build();

	         httpReq.getSession().setAttribute("csrfPreventionSaltCache", csrfPreventionSaltCache);
	     }
	     // Generate the salt and store it in the users cache
	     String salt = RandomStringUtils.random(20, 0, 0, true, true, null, new SecureRandom());
	     csrfPreventionSaltCache.put(salt, Boolean.TRUE);

	     // Add the salt to the current request so it can be used
	     // by the page rendered in this request
	     httpReq.setAttribute("csrfPreventionSalt", salt); 
	     httpReq.getSession().setAttribute("csrfPreventionSalt", salt);
	     //setNewsalt(salt);
		 
		return salt;
		
		 
	 }
	

}