package com.unilog.products;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.erpmanager.ERPProductsWrapper;
import com.unilog.misc.EventModel;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.promotion.BannerEntity;
import com.unilog.promotion.PromotionList;
import com.unilog.promotion.PromotionResponse;
import com.unilog.promotion.PromotionUtility;
import com.unilog.solr.SolrService;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.WordPressMenuModel;
import com.unilognew.util.ECommerceEnumType.RequestHandlers;
import com.unilog.solr.connection.ItemPriceData;
import com.unilog.solr.connection.MainItem;


public class ProductHunterSolr {
	
	
	//local server
	//static String solrURL = "http://192.168.1.189/solrVanMeter/";
 static String solrURL = CommonDBQuery.getSystemParamtersList().get("SOLR_URL");
 static private HttpServletRequest request;
 static String siteNameSolr = CommonUtility.validateString(CommonDBQuery.getJiraKey()); 

	//beta server
// static String solrURL =  "http://199.66.100.122:8983/solrVanMeter/";
 public static void reloadSolrUrl()
 {
	 solrURL = CommonDBQuery.getSystemParamtersList().get("SOLR_URL");
 }

public static String escapeQueryCulprits(String s)
{
StringBuilder sb = new StringBuilder();
for (int i = 0; i < s.length(); i++)
{
char c = s.charAt(i);
// These characters are part of the query syntax and must be escaped
if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
|| c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
|| c == '*' || c == '?' || c == '|' || c == '&' || c == ';' || c == '/'
)
{
sb.append('\\');
}
if(Character.isWhitespace(c))
{
sb.append("\\");
}
sb.append(c);
}
return sb.toString();
}

public static String escapeQueryCulpritsWithoutWhiteSpace(String s){
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < s.length(); i++)
	{
		char c = s.charAt(i);
		// These characters are part of the query syntax and must be escaped
		if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
			|| c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
				|| c == '*' || c == '?' || c == '|' || c == '&' || c == ';' || c == '/'
		)
		{
			sb.append('\\');
		}

		sb.append(c);
	}
	return sb.toString();
}

public static HashMap<String, ArrayList<ProductsModel>> getProductGroupAndItems(int productGroupId,int userId,int fromRow,int resultPerPage, String keyWord)
{ 
	HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
	try
	{
		searchResultList.put("groupListData", getProductGroup(productGroupId, userId));
		searchResultList.put("itemList", geetProductGroupItems(productGroupId,fromRow,resultPerPage,CommonUtility.validateString(keyWord)));
		
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return searchResultList;
}

public static ArrayList<ProductsModel> getProductGroup(int productGroupId,int userId){
	ArrayList<ProductsModel> taxonomyListVal = new ArrayList<ProductsModel>();
	HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
	HttpSolrServer server = null;
	try{
		server = ConnectionManager.getSolrClientConnection(solrURL+"/productgroup");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
		String fq="indexType:"+userId;
		fq = fq + "~" + "parentCategoryId:"+productGroupId;
		int resultCount = 0;
		query.setQuery("*:*");
		//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
		String attrFtr[] = fq.split("~");
		query.setStart(0);
		query.setRows(100);

		query.addSortField("displaySeq", SolrQuery.ORDER.asc );
		query.setFilterQueries(attrFtr);

		QueryResponse response = server.query(query);
		System.out.println("Category Query : " + query);

		SolrDocumentList documents = response.getResults();
		resultCount = (int) response.getResults().getNumFound();
		List<FacetField> facetFeild = response.getFacetFields();
		Iterator<SolrDocument> itr = documents.iterator();
		while (itr.hasNext()) {


			SolrDocument doc = itr.next();

			ProductsModel nameCode= new ProductsModel();
			String itemUrl = doc.getFieldValue("category").toString();
			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
			String pattern = "[^A-Za-z0-9]";
			itemUrl = itemUrl.replaceAll(pattern," ");
			itemUrl = itemUrl.replaceAll("\\s+","-");
			nameCode.setItemUrl(itemUrl);
			nameCode.setProductListId(CommonUtility.validateNumber((String)doc.getFieldValue("id")));
			nameCode.setProductListName((String) doc.getFieldValue("category"));
			/* nameCode.setCategoryCode(doc.getFieldValue("categoryID").toString());
        	 nameCode.setCategoryName(doc.getFieldValue("category").toString());*/
			// nameCode.setCategoryCount(searchCategory.getInt("CATEGORY_COUNT"));
			int levelNum = CommonUtility.validateNumber(doc.getFieldValue("levelNumber").toString());
			nameCode.setLevelNumber(levelNum);
			String imageName = null;
			if(doc.getFieldValue("imageName")==null){
				imageName = "NoImage.png";
			}else{
				imageName = doc.getFieldValue("imageName").toString();
			}
			nameCode.setImageName(imageName);
			taxonomyListVal.add(nameCode);

		}
		searchResultList.put("categeoryList", taxonomyListVal);


	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return taxonomyListVal;

}


public  static ArrayList<ProductsModel> geetProductGroupItems(int productGroupId,int fromRow,int resultPerPage, String keyWord){
	ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
	HttpSolrServer server = null;
	try
	{
		String idList = "";
		String c = "";
		server = ConnectionManager.getSolrClientConnection(solrURL+"/productgroupitems");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
		String fq="categoryID:"+productGroupId;
		
		int resultCount = 0;
		if(keyWord!=null && !keyWord.trim().equalsIgnoreCase(""))
		{
			 keyWord = keyWord.replaceAll("\\|", " ");
			 String validateForSearch[] = keyWord.split("\\s+",-1);
			 Pattern p = Pattern.compile("[^a-zA-Z0-9]");
			 Pattern p1 = Pattern.compile("[\\d]");
			 boolean includePartNumberSearch = false;
			 String singleKeyword = "";
			 String multiKeyword = "";
			 String multiKeywordOr = "";
			 String pattern = "[^A-Za-z0-9]";
			 if(validateForSearch!=null && validateForSearch.length>1)
			 {
				 String separator = "";
				 for(String keyBuilder:validateForSearch)
				 {
					 boolean hasSpecialChar = p1.matcher(keyBuilder.trim()).find();
					 if(keyBuilder.matches("[-+]?\\d*\\.?\\d+"))
					 {
						 includePartNumberSearch = true;
					 }
					 else
					 {
						 if(hasSpecialChar)
						 {
							 includePartNumberSearch = true;
						 }
						 else
						 {
							 hasSpecialChar = p.matcher(keyBuilder.trim()).find();
							 if(hasSpecialChar)
							 {
								 includePartNumberSearch = true;
							 }
						 }
						 
					 }
					 
					 if(includePartNumberSearch)
					 {
						 multiKeyword = multiKeyword + separator + buildKeyword(keyBuilder);
						 separator = " AND ";
					 }
					 includePartNumberSearch = false;
				 }
				 
				multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord) + multiKeyword;
				multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
				
				multiKeywordOr = multiKeyword;
				multiKeyword = multiKeyword.replaceAll("\\s+", " AND ");
				System.out.println("multiKeyword Query Keyword : " + multiKeyword);
			 }
			 else
			 {

				 includePartNumberSearch = true;
				 singleKeyword = "("+buildKeyword(keyWord)+")";
				 System.out.println("Single Query Keyword : " + singleKeyword);
				 
			 
			 }
			 String queryString = "";
			 if(includePartNumberSearch) {
				queryString  = singleKeyword;
			 }else {
				queryString = multiKeyword;
			 }
			 query.setQuery(queryString);
			 query.set("qf", "partkeywords keywords");
			 query.set("pf", "partkeywords^100 keywords^50");
			 query.addSortField("score", SolrQuery.ORDER.desc );
		}
		else
		{
			query.setQuery("*:*");
		}
		
		//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
		String attrFtr[] = fq.split("~");
	
		query.setStart(fromRow);
		query.setRows(resultPerPage);
		query.addSortField("displaySeq", SolrQuery.ORDER.asc );
		query.setFilterQueries(attrFtr);
		
		QueryResponse response = server.query(query);
		System.out.println("Category Query : " + query);
		
		SolrDocumentList documents = response.getResults();
		resultCount = (int) response.getResults().getNumFound();
		Iterator<SolrDocument> itr = documents.iterator();
		while (itr.hasNext()) {
			
			try{
			
			ProductsModel itemModel = new ProductsModel();
			SolrDocument doc = itr.next();
			idList = idList + c + doc.getFieldValue("itemPriceId").toString();
			
			itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
			itemModel.setMinOrderQty(1);
			itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
			itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
			itemModel.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
			//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
			
			itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
			itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
			itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
			itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
			itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
			itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
			itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
			itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
			itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
			itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
			itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
			if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
			{
				if(doc.getFieldValue("productItemCount")!=null){
					//&& doc.getFieldValue("defaultProductItem")!=null && CommonUtility.validateString(doc.getFieldValue("defaultProductItem").toString()).equalsIgnoreCase("Y")
					itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
				}
			}
			
			itemModel.setResultCount(resultCount);
			
			String imageName = null;
   			String imageType = null;
   			
   			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
   			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
   				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
   			}
   			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
   			
		
   			String pattern = "[^A-Za-z0-9]";
			 itemUrl = itemUrl.replaceAll(pattern," ");
			 itemUrl = itemUrl.replaceAll("\\s+","-");
        	itemModel.setItemUrl(itemUrl);
   			
   			if(doc.getFieldValue("imageName")!=null)
   				imageName = doc.getFieldValue("imageName").toString();
   			
   					       			
   			if(doc.getFieldValue("imageType")!=null)
   				imageType = doc.getFieldValue("imageType").toString();
   			if(doc.getFieldValue("upc")!=null)
   				itemModel.setUpc(doc.getFieldValue("upc").toString());	
   			
   			if(imageName==null)
   			{
   				imageName = "NoImage.png";
   				imageType = "IMAGE";
   			}
   			itemModel.setImageName(imageName.trim());
   			itemModel.setImageType(imageType);
			c = " OR ";
			itemLevelFilterData.add(itemModel);
			
			}catch (Exception e) {
				System.out.println("Error Occured While retriving data from solr.");
				e.printStackTrace();
			}
			
			
			 if(itemLevelFilterData.size()>0)
				{
					HttpSolrServer server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
					server1.setParser(new XMLResponseParser());
					QueryRequest req1 = new QueryRequest();
					req1.setMethod(METHOD.POST);
					SolrQuery query1 = new SolrQuery();
					
					query1.setQuery("itemPriceId:("+idList+")");
					
					query1.setStart(0);
					query1.setRows(itemLevelFilterData.size());
					
					QueryResponse response1 = server1.query(query1);
					
					SolrDocumentList documents1 = response1.getResults();
					resultCount = (int) response1.getResults().getNumFound();
					

					
					Iterator<SolrDocument> itr1 = documents1.iterator();
					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
					ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
					while (itr1.hasNext()) {
						
						SolrDocument doc = itr1.next();
						for(ProductsModel iList : itemLevelFilterData)
						{
							int itemId = CommonUtility.validateNumber(doc.getFieldValue("itemid").toString());
							
							
						
							if(itemId==iList.getItemId())
							{
								ProductsModel ProductsModel = new ProductsModel();
				       	        ProductsModel.setErpPartNumber(iList.getPartNumber());
				       	        partIdentifiersList.add(ProductsModel);
								int minOrdQty = 1;
								int orderQtyInterval = 1;
								int saleQty = 1;
								int packageQty = 1;
								int packageFlag = 0;
								String mOrdQty = null;
								String iPrice ="";
								if(doc.getFieldValue("price")!=null)
								{
									iPrice = doc.getFieldValue("price").toString();
								}
									
								
								double itemPrice = 0d;
								if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
								{
									itemPrice = Double.parseDouble(iPrice);
								}
								
								iList.setPrice(itemPrice);
								if(doc.getFieldValue("uom")!=null)
								iList.setUom(doc.getFieldValue("uom").toString());
								iList.setItemPriceId(CommonUtility.validateNumber(doc.getFieldValue("itemPriceId").toString()));
								
															
							/*
							 *  commented next lines 
							 *  
							 	if(doc.getFieldValue("customerPartNumber")!=null)
									iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
									
							*/		
								
								if(doc.getFieldValue("materialGroup")!=null)
									iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
								
								if(doc.getFieldValue("minordqty")!=null)
								{
									if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
										minOrdQty = CommonUtility.validateNumber(doc.getFieldValue("minordqty").toString());
									if(minOrdQty==0)
										minOrdQty = 1;
								}
								if(doc.getFieldValue("orderQtyInterval")!=null)
								{
									if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
										orderQtyInterval = CommonUtility.validateNumber(doc.getFieldValue("orderQtyInterval").toString());
									if(orderQtyInterval==0)
										orderQtyInterval = 1;
								}
								if(doc.getFieldValue("salesQty")!=null)
								{
									if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
										saleQty = CommonUtility.validateNumber(doc.getFieldValue("salesQty").toString());
									
									if(saleQty==0)
										saleQty = 1;
								}
								
								if(doc.getFieldValue("packageFlag")!=null)
								{
									if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
										packageFlag = CommonUtility.validateNumber(doc.getFieldValue("packageFlag").toString());
								}
								
								if(doc.getFieldValue("packageQty")!=null)
								{
									if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
									{
										packageQty = CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString());
									}
									
								}
								
								if(doc.getFieldValue("packDesc")!=null)
								{
									String packDesc = doc.getFieldValue("packDesc").toString();
									iList.setPackDesc(packDesc);
								}
								
								if(doc.getFieldValue("displayPricing") != null){
									
									iList.setDisplayPricing(doc.getFieldValue("displayPricing").toString());
								}
								if(doc.getFieldValue("clearanceFlag") != null){
									
									iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
								}
								
								iList.setPackageFlag(packageFlag);
								iList.setPackageQty(packageQty);
								iList.setSaleQty(saleQty);
								iList.setMinOrderQty(minOrdQty);
								iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
								
								//iList.setCustomerPartNumberList(getcustomerParnumber(itemId, buyingCompanyId, buyingCompanyId));
								
								
							}
						}
					}
					ConnectionManager.closeSolrClientConnection(server1);
				}
			
		}
	}
	catch (Exception e) {
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	
	return itemLevelFilterData;
}




public static String productGroupAutoComplete(int userId){
	String jsonResponse = "";
	HttpSolrServer server = null;
	try
	{
		server = ConnectionManager.getSolrClientConnection(solrURL+"/productgroup");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
		String fq="indexType:"+userId;
		
		int resultCount = 0;
		query.setQuery("*:*");
		//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
		String attrFtr[] = fq.split("~");
		query.setStart(0);
		query.setRows(1);
		
		query.addSortField("displaySeq", SolrQuery.ORDER.asc );
		query.setFilterQueries(attrFtr);
		
		QueryResponse response = server.query(query);
		System.out.println("Category Query : " + query);
		resultCount = (int) response.getResults().getNumFound();
		query.setRows(resultCount);
		query.set("wt", "json");
		query.set("json.nl","map");
		System.out.println("after Query : " + query);
		HttpURLConnection eclipseConn = (HttpURLConnection) new URL(solrURL+"/productgroup/select?"+query).openConnection();
        eclipseConn.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
		String line = null;
		StringBuffer responseData = new StringBuffer();
		while((line = in.readLine()) != null) {
			responseData.append(line);
		}
			System.out.println(responseData);
			jsonResponse = responseData.toString();
		
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return jsonResponse;

}




public static LinkedHashMap<String, Object> getSelectableItems(int productId,int subsetId,int generalSubset,int treeId)
{
	LinkedHashMap<String, Object> selectableObj = new LinkedHashMap<String, Object>();
	try
	{
		
		boolean multiAttribute = false;
		String attributePrepend = "attr_";
		if(CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE")!=null && CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE").trim().equalsIgnoreCase("Y")){
			multiAttribute = true;
			attributePrepend = "attr_Multi_";
		}
		
		ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
		String facetConUrl = solrURL+"/catalogdata";
		String productConfigUrl = solrURL+"/productattribute";
		 String facetQueryString = ""; 
		 String facetInnderUrl = solrURL+"/categoryattribute";
		 String faucetField[] = null;
		 boolean isDifferentiator = true;
		 String indexType = "PH_SEARCH_"+subsetId;
		 if(generalSubset>0 && generalSubset!=subsetId)
			 indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
		 
			// indexType = "PH_SEARCH_"+generalSubset+"_"+subsetId;

		 ArrayList<ProductsModel> refinedList = new ArrayList<ProductsModel>();
		 ProductsModel attributeRemove = new ProductsModel();
		 attributeRemove.setAttrName("Brand");
		 refinedList.add(attributeRemove);
		 refinedList.add(attributeRemove);
		 refinedList.add(attributeRemove);
		if(treeId >0)
		 {
			if(CommonDBQuery.getSystemParamtersList().get("DIFFERENTIATOR_ATTRIBUTE")!=null && CommonDBQuery.getSystemParamtersList().get("DIFFERENTIATOR_ATTRIBUTE").trim().equalsIgnoreCase("N"))
			{
				isDifferentiator = false;
			}
			if(CommonDBQuery.getSystemParamtersList().get("PRODUCT_ATTRIBUTE")!=null && CommonDBQuery.getSystemParamtersList().get("PRODUCT_ATTRIBUTE").trim().equalsIgnoreCase("Y")){
				 faucetField = ProductHunterSolrUltimate.getProductSelectableAttribute(productConfigUrl, "", facetQueryString, facetInnderUrl, productId,refinedList,true,true,true,isDifferentiator,true,new ArrayList<String>(),new ArrayList<String>());
			}
			
			if(faucetField==null){
				attributePrepend = "attr_";
			 faucetField = ProductHunterSolrUltimate.getFaucetFieldByName(facetConUrl, "", facetQueryString, facetInnderUrl, treeId,refinedList,true,true,true,isDifferentiator,true,new ArrayList<String>(),new ArrayList<String>());
			}
		 }
		 
		 if(faucetField!=null && faucetField.length>0)
		 {
			 String c="";
			 String idList = "";
			 String pattern = "[^A-Za-z0-9]";
			 HttpSolrServer server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
			 String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			 if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			 }
			 String fl = "itemid,partnumber,altPartNumber1,brand,manufacturerName,manfId,manfpartnumber,brandID,salesUom,qtyAvailable,packageQty,productItemCount,imageName,imageType,attr_*";
					//&fl=itemid,partnumber,altPartNumber1,brand,manufacturerName,manfId,manfpartnumber,brandID,description,salesUom,qtyAvailable,packageQty,productItemCount,imageName,imageType,upc,custom_*,	 
				
					 server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				
				int resultCount = 0;
				query.setQuery("productId:"+productId);
				query.addFacetField(faucetField);
				query.addFilterQuery(fq);
				query.addField(fl);
				query.setStart(0);
				query.setRows(1);
				query.addFilterQuery("defaultCategory:Y");
				System.out.println("Category Query : " + query);
				QueryResponse response = server.query(query,METHOD.POST);
				query.setRows((int) response.getResults().getNumFound());
				response = server.query(query,METHOD.POST);
				
				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				Iterator<SolrDocument> itr = documents.iterator();
				LinkedHashMap<String, ArrayList<String>> selectableList = new LinkedHashMap<String, ArrayList<String>>();
				LinkedHashMap<String, ArrayList<ProductsModel>> selectableMultiList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				LinkedHashMap<String, ArrayList<String>> selectableMultiListString = new LinkedHashMap<String, ArrayList<String>>();
				ArrayList<String> selMultiVal = new ArrayList<String>();
				ArrayList<ProductsModel> selMultiValArr = new ArrayList<ProductsModel>();
				LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> slectableGen = new LinkedHashMap<String, LinkedHashMap<String,ArrayList<String>>>();
				while (itr.hasNext()) {
					
					try{
					
					ProductsModel itemModel = new ProductsModel();
					SolrDocument doc = itr.next();
					idList = idList + c + doc.getFieldValue("itemid").toString();
					
					itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
					itemModel.setMinOrderQty(1);
					itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
					itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
					//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
					itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
					itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
					itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
					itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
					itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
					itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
					itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
					itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
					itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
					if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
					{
						if(doc.getFieldValue("productItemCount")!=null){
							//&& doc.getFieldValue("defaultProductItem")!=null && CommonUtility.validateString(doc.getFieldValue("defaultProductItem").toString()).equalsIgnoreCase("Y")
							itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
						}
					}
					
					ArrayList<ProductsModel> attributeDataList = new  ArrayList<ProductsModel>();
					for(FacetField test:facetFeild)
					{
						ProductsModel attrList = new ProductsModel();
						System.out.println(test.getName());
						 //String atrVal = (String)doc.getFieldValue(test.getName());
						String attributeValueArr = (String)doc.getFieldValue(test.getName());
						String[] valueArr = null;
						String attrValue = "";
						if(multiAttribute){
							
							

							valueArr = attributeValueArr.split("\\|~\\|");
							
							if(valueArr!=null && valueArr.length>2){
								attrValue = valueArr[1];
								
								
							}else if(valueArr!=null && valueArr.length>1){
								attrValue = valueArr[1];
							}else{
								attrValue = (String)doc.getFieldValue(test.getName());
							}
							attrValue = attrValue.trim();
							attrValue = attrValue.replaceAll(pattern," ");
							attrValue = attrValue.replaceAll("\\s+","-");
							
						}else{
							attrValue = (String)doc.getFieldValue(test.getName());
						}
						
						if( attrValue!=null)
						{
							 LinkedHashMap<String, ArrayList<String>> temp1 = slectableGen.get(test.getName().replace(attributePrepend, ""));
							 
							 if(temp1!=null)
							 {
								 ArrayList<String> temp2 = temp1.get(attrValue);
								 if(temp2!=null)
								 {
									 if(!temp2.contains((String) doc.getFieldValue("itemid")))
									 {
									 temp2.add((String) doc.getFieldValue("itemid")) ;
									 temp1.put(attrValue, temp2);
									 slectableGen.put(test.getName().replace(attributePrepend, ""), temp1);
									 }
								 }
								 else
								 {
									 temp2 = new ArrayList<String>();
									 temp2.add((String) doc.getFieldValue("itemid")) ;
									 temp1.put(attrValue, temp2);
									 slectableGen.put(test.getName().replace(attributePrepend, ""), temp1);
								 }
							 }
							 else
							 {
								 temp1 = new LinkedHashMap<String, ArrayList<String>>();
								 ArrayList<String> temp2 = new ArrayList<String>();
								 temp2.add((String) doc.getFieldValue("itemid")) ;
								 temp1.put(attrValue, temp2);
								 slectableGen.put(test.getName().replace(attributePrepend, ""), temp1);
								
							 }
						}
						
						ArrayList<String> selVal = selectableList.get(test.getName().replace("attr_", ""));
						
						if(selVal!=null)
						{
							if(doc.getFieldValue(test.getName())!=null)
							{
								if(!selVal.contains((String)doc.getFieldValue(test.getName())))
								{
										selVal.add((String)doc.getFieldValue(test.getName()));
										selectableList.put(test.getName().replace("attr_", ""), selVal);
								}
							}                                         
							
						}
						else
						{
							selVal = new ArrayList<String>();
							if(doc.getFieldValue(test.getName())!=null)
							{
								
										selVal.add((String)doc.getFieldValue(test.getName()));
										selectableList.put(test.getName().replace("attr_", ""), selVal);
							}
						}
						
						
						
						if(multiAttribute){
							String imageFilter = "N";
							String displayTextFilter = "N";
							String itemImageFilter = "N";
							String itemDisplayTextFilter = "N";
							String attributeValueEncoded = "";
							String attributeImageName = "";
							String attributeImageType =  "";
							attrValue = "";
							attributeValueArr = (String)doc.getFieldValue(test.getName());
							valueArr = null;
							

							valueArr = attributeValueArr.split("\\|~\\|");
							
							if(valueArr!=null && valueArr.length>2){
								attrValue = valueArr[1];
								imageFilter = valueArr[2];
								if(valueArr.length>3){
									displayTextFilter = valueArr[3];
								}if(valueArr.length>4){
									itemImageFilter = valueArr[4];
								}if(valueArr.length>5){
									itemDisplayTextFilter = valueArr[5];
								}if(valueArr.length>6){
									attributeImageName = valueArr[6];
								}if(valueArr.length>7){
									attributeImageType = valueArr[7];
								}
								
								
							}else if(valueArr!=null && valueArr.length>1){
								attrValue = valueArr[1];
							}else{
								attrValue = (String)doc.getFieldValue(test.getName());
							}
							attrValue = attrValue.trim();
							attributeValueEncoded = attrValue.replaceAll(pattern," ");
							attributeValueEncoded = attributeValueEncoded.replaceAll("\\s+","-");
							
							 selMultiVal = selectableMultiListString.get(test.getName().replace("attr_Multi_", ""));
							 selMultiValArr = selectableMultiList.get(test.getName().replace("attr_Multi_", ""));
							 ProductsModel multiVal = new ProductsModel();
							 
							 	multiVal.setAttrValue(attrValue.trim());
								multiVal.setImageFilter(imageFilter);
								multiVal.setDisplayFilterText(displayTextFilter);
								multiVal.setImageName(attributeImageName);
								multiVal.setImageType(attributeImageType);
								multiVal.setItemImageFilter(itemImageFilter);
								multiVal.setItemDisplayFilterText(itemDisplayTextFilter);
								multiVal.setAttrValueEncoded(attributeValueEncoded);
								
								
							if(selMultiVal!=null)
							{
								if(doc.getFieldValue(test.getName())!=null)
								{
									if(!selMultiVal.contains((String)doc.getFieldValue(test.getName())))
									{
										selMultiVal.add((String)doc.getFieldValue(test.getName()));
										selectableMultiListString.put(test.getName().replace("attr_Multi_", ""), selMultiVal);
										selMultiValArr.add(multiVal);
										selectableMultiList.put(test.getName().replace("attr_Multi_", ""), selMultiValArr);
									}
								}                                         
								
							}
							else
							{
								selMultiVal = new ArrayList<String>();
								selMultiValArr = new ArrayList<ProductsModel>();
								selMultiValArr.add(multiVal);
								if(doc.getFieldValue(test.getName())!=null)
								{
									selectableMultiList.put(test.getName().replace("attr_Multi_", ""), selMultiValArr);
									selMultiVal.add((String)doc.getFieldValue(test.getName()));
									selectableMultiListString.put(test.getName().replace("attr_Multi_", ""), selMultiVal);
								}
							}
						}
						
						;
						
						attrList.setAttrValue(attrValue);
						attrList.setAttrName(test.getName().replace("attr_", ""));
						attributeDataList.add(attrList);
					}
					itemModel.setAttributeDataList(attributeDataList);
					itemModel.setResultCount(resultCount);
					
					String imageName = null;
	       			String imageType = null;
	       			
	       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
	       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			}
	       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	       			
	       			
	   			 itemUrl = itemUrl.replaceAll(pattern," ");
	   			 itemUrl = itemUrl.replaceAll("\\s+","-");
	            	itemModel.setItemUrl(itemUrl);
	       			
	       			if(doc.getFieldValue("imageName")!=null)
	       				imageName = doc.getFieldValue("imageName").toString();
	       			
	       					       			
	       			if(doc.getFieldValue("imageType")!=null)
	       				imageType = doc.getFieldValue("imageType").toString();
	       			if(doc.getFieldValue("upc")!=null)
	       				itemModel.setUpc(doc.getFieldValue("upc").toString());	
	       			
	       			if(imageName==null)
	       			{
	       				imageName = "NoImage.png";
	       				imageType = "IMAGE";
	       			}
	       			itemModel.setImageName(imageName.trim());
	       			itemModel.setImageType(imageType);
					c = " OR ";
					itemLevelFilterData.add(itemModel);
					
					}catch (Exception e) {
						System.out.println("Error Occured While retriving data from solr.");
						e.printStackTrace();
					}
					
				}
				
				
				 if(itemLevelFilterData.size()>0)
					{
						HttpSolrServer server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
						LinkedHashMap<String, String> itemMap = new LinkedHashMap<String, String>();
						server1.setParser(new XMLResponseParser());
						QueryRequest req1 = new QueryRequest();
						req1.setMethod(METHOD.POST);
						SolrQuery query1 = new SolrQuery();
						
						if(generalSubset>0 && generalSubset!=subsetId) {
							 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";
							 if(!siteNameSolr.equalsIgnoreCase(""))
							 {
								 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
							 }
						}

						query1.setQuery("itemid:("+idList+")");
						String fq2 = "type:"+indexType;
						query1.setFilterQueries(fq2);
						query1.setStart(0);
						query1.setRows(itemLevelFilterData.size());
						System.out.println("query1 : "+query1);
						QueryResponse response1 = server1.query(query1);
						
						SolrDocumentList documents1 = response1.getResults();
						resultCount = (int) response1.getResults().getNumFound();
						

						
						Iterator<SolrDocument> itr1 = documents1.iterator();
						NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
						ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
						while (itr1.hasNext()) {
							
							SolrDocument doc = itr1.next();
							for(ProductsModel iList : itemLevelFilterData)
							{
								int itemId = CommonUtility.validateNumber(doc.getFieldValue("itemid").toString());
								
								
							
								if(itemId==iList.getItemId())
								{
									itemMap.put((String) doc.getFieldValue("itemid"), (String)doc.getFieldValue("itemPriceId")); 
									ProductsModel ProductsModel = new ProductsModel();
					       	        ProductsModel.setErpPartNumber(iList.getPartNumber());
					       	        partIdentifiersList.add(ProductsModel);
									int minOrdQty = 1;
									int orderQtyInterval = 1;
									int saleQty = 1;
									int packageQty = 1;
									int packageFlag = 0;
									String mOrdQty = null;
									String iPrice ="";
									if(doc.getFieldValue("price")!=null)
									{
										iPrice = doc.getFieldValue("price").toString();
									}
										
									
									double itemPrice = 0d;
									if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
									{
										itemPrice = Double.parseDouble(iPrice);
									}
									
									iList.setPrice(itemPrice);
									if(doc.getFieldValue("uom")!=null)
									iList.setUom(doc.getFieldValue("uom").toString());
									iList.setItemPriceId(CommonUtility.validateNumber(doc.getFieldValue("itemPriceId").toString()));
									
																
								/*
								 *  commented next lines 
								 *  
								 	if(doc.getFieldValue("customerPartNumber")!=null)
										iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
										
								*/		
									
									if(doc.getFieldValue("materialGroup")!=null)
										iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
									
									if(doc.getFieldValue("minordqty")!=null)
									{
										if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
											minOrdQty = CommonUtility.validateNumber(doc.getFieldValue("minordqty").toString());
										if(minOrdQty==0)
											minOrdQty = 1;
									}
									if(doc.getFieldValue("orderQtyInterval")!=null)
									{
										if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
											orderQtyInterval = CommonUtility.validateNumber(doc.getFieldValue("orderQtyInterval").toString());
										if(orderQtyInterval==0)
											orderQtyInterval = 1;
									}
									if(doc.getFieldValue("salesQty")!=null)
									{
										if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
											saleQty = CommonUtility.validateNumber(doc.getFieldValue("salesQty").toString());
										
										if(saleQty==0)
											saleQty = 1;
									}
									
									if(doc.getFieldValue("packageFlag")!=null)
									{
										if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
											packageFlag = CommonUtility.validateNumber(doc.getFieldValue("packageFlag").toString());
									}
									
									if(doc.getFieldValue("packageQty")!=null)
									{
										if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
										{
											packageQty = CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString());
										}
										
									}
									
									if(doc.getFieldValue("packDesc")!=null)
									{
										String packDesc = doc.getFieldValue("packDesc").toString();
										iList.setPackDesc(packDesc);
									}
									
									if(doc.getFieldValue("displayPricing") != null){
										
										iList.setDisplayPricing(doc.getFieldValue("displayPricing").toString());
									}
									if(doc.getFieldValue("clearanceFlag") != null){
										
										iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
									}
									
									iList.setPackageFlag(packageFlag);
									iList.setPackageQty(packageQty);
									iList.setSaleQty(saleQty);
									iList.setMinOrderQty(minOrdQty);
									iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
									
									//iList.setCustomerPartNumberList(getcustomerParnumber(itemId, buyingCompanyId, buyingCompanyId));
									
									
								}
							}
						}
						selectableObj.put("itemList", itemLevelFilterData);
						selectableObj.put("selectableList", selectableList);
						selectableObj.put("selectableMultiList", selectableMultiList);
						
						System.out.println("SELECT:" + slectableGen);
						
						Gson gson = new Gson();
						System.out.println("Json Data : " + gson.toJson(slectableGen));
						System.out.println("Multi data : " + gson.toJson(selectableMultiList));
						
						selectableObj.put("selecatableJson", gson.toJson(slectableGen));
						selectableObj.put("itemMap", gson.toJson(itemMap));
						selectableObj.put("selecatableMap", slectableGen);
						
						ConnectionManager.closeSolrClientConnection(server1);
						
					}
				//searchResultList.put("categeoryList", taxonomyListVal);
				 ConnectionManager.closeSolrClientConnection(server);
		 }
		
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return selectableObj;
	
}

public static LinkedHashMap<String, Object> getSelectableItemsV2(int productId,int subsetId,int generalSubset,int treeId,String attrFilterList,int fromRow,int resultPerPage)
{
	
	LinkedHashMap<String, Object> selectableObj = new LinkedHashMap<String, Object>();
	try
	{
		 String indexType = "PH_SEARCH_"+subsetId;
		 if(generalSubset>0 && generalSubset!=subsetId)
			 indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
	 		
 		 //indexType = "PH_SEARCH_"+generalSubset+"_"+subsetId;

		ArrayList<String> removeAttributeList = new ArrayList<String>();
		String attrFtrGlobal[] = null;
		LinkedHashMap<String, ArrayList<String>> filteredMultList = new LinkedHashMap<String, ArrayList<String>>();
		ArrayList<String> valueList = new ArrayList<String>();	
		ArrayList<ProductsModel> refinedList = new ArrayList<ProductsModel>();
		String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
		if(!siteNameSolr.equalsIgnoreCase("")) {
			fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
		}

		String rebuildAttrNav = "";
		String aFilterNav = "";
		String attrFtrNav[] = null;
		if(attrFilterList!=null && !attrFilterList.trim().equalsIgnoreCase(""))
		{

			String tempArr[] = attrFilterList.split("~");
			String c = "";
			for(String sArr:tempArr)
			{
				boolean addRefinedList = true;
				String attrArray[]=sArr.split(":");
				ProductsModel filterListVal = new ProductsModel();
				

				filterListVal.setAttrName(attrArray[0]);
				filterListVal.setAttrNameEncoded(URLEncoder.encode(attrArray[0],"UTF-8"));
				String arFilterVal = "";
				String arFilterDel = "";
				if(attrArray!=null && attrArray.length>2)
				{
					int arIndex = 0;

					for(String arAppend:attrArray)
					{
						if(arIndex>0)
						{
							System.out.println("Split length > 2 : Appending - " + arFilterVal +" with "+arAppend);
							arFilterVal = arFilterVal + arFilterDel + arAppend;
							arFilterDel = ":";


						}
						arIndex++;
					}
					filterListVal.setAttrValue(arFilterVal);
				}else if(attrArray!=null && attrArray.length<=1){
					arFilterVal = attrArray[0];
				}else{
					arFilterVal = attrArray[1];
				}
				filterListVal.setAttrValue(arFilterVal);
				filterListVal.setAttrValueEncoded(URLEncoder.encode(arFilterVal,"UTF-8"));
				//if(addRefinedList)
				refinedList.add(filterListVal);
				
				valueList = new ArrayList<String>();
				String buildFilterKey = "";
				String appendFilter[] = arFilterVal.split("\\|");
				if(appendFilter!=null && appendFilter.length>0)
				{
					String appendOr = "";
					for(String append:appendFilter)
					{
						String replaceFilterVal = append;
						if(replaceFilterVal!=null)
						{
							replaceFilterVal = replaceFilterVal.substring(1, replaceFilterVal.length()-1);
							valueList.add(replaceFilterVal);
							if(!attrArray[0].trim().equalsIgnoreCase("price"))
							{
								replaceFilterVal = escapeQueryCulpritsWithoutWhiteSpace(replaceFilterVal);
								replaceFilterVal = "\""+replaceFilterVal+"\"";
							}


							buildFilterKey = buildFilterKey + appendOr + replaceFilterVal;
							appendOr = " OR ";
						}

					}

				}
				filteredMultList.put(attrArray[0], valueList);

				rebuildAttrNav = rebuildAttrNav+c+escapeQueryCulprits(attrArray[0])+":("+buildFilterKey+")";
				c = "~";
			}
			attrFtrGlobal= rebuildAttrNav.split("~");
			aFilterNav = fqNav + "~"+rebuildAttrNav;
			attrFtrNav = aFilterNav.split("~");
		}else{
			aFilterNav = fqNav;
			attrFtrNav = aFilterNav.split("~");
		}
		boolean multiAttribute = false;
		String attributePrepend = "attr_";
		if(CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE")!=null && CommonDBQuery.getSystemParamtersList().get("MULTI_ATTR_VALUE").trim().equalsIgnoreCase("Y")){
			multiAttribute = true;
			attributePrepend = "attr_Multi_";
		}
		
		ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
		String facetConUrl = solrURL+"/catalogdata";
		String productConfigUrl = solrURL+"/productattribute";
		 String facetQueryString = ""; 
		 String facetInnderUrl = solrURL+"/categoryattribute";
		 String faucetField[] = null;
		 boolean isDifferentiator = true;
		
		 ProductsModel attributeRemove = new ProductsModel();
		 ArrayList<String> finalRemoveList = new ArrayList<String>();

			// finalRemoveList = CommonDBQuery.getDefaultFacet();
			if(removeAttributeList!=null && removeAttributeList.size()>0)
			{
				for(String lList: CommonDBQuery.getDefaultFacet())
				{
					boolean addToList = true;
					for(String rmList:removeAttributeList)
					{
						if(lList!=null && lList.trim().equalsIgnoreCase(rmList))
						{
							addToList = false;
							break;
						}


					}
					if(addToList)
					{
						finalRemoveList.add(lList);
					}
				}


			}
		
		if(treeId >0)
		 {
			if(CommonDBQuery.getSystemParamtersList().get("DIFFERENTIATOR_ATTRIBUTE")!=null && CommonDBQuery.getSystemParamtersList().get("DIFFERENTIATOR_ATTRIBUTE").trim().equalsIgnoreCase("N"))
			{
				isDifferentiator = false;
			}
			if(CommonDBQuery.getSystemParamtersList().get("PRODUCT_ATTRIBUTE")!=null && CommonDBQuery.getSystemParamtersList().get("PRODUCT_ATTRIBUTE").trim().equalsIgnoreCase("Y")){
				 faucetField = ProductHunterSolrUltimate.getProductSelectableAttribute(productConfigUrl, "", facetQueryString, facetInnderUrl, productId,refinedList,true,true,true,isDifferentiator,true,new ArrayList<String>(),new ArrayList<String>());
			}
			
			if(faucetField==null){
				attributePrepend = "attr_";
			 faucetField = ProductHunterSolrUltimate.getFaucetFieldByName(facetConUrl, "", facetQueryString, facetInnderUrl, treeId,refinedList,true,true,true,isDifferentiator,true,finalRemoveList,new ArrayList<String>());
			}
		 }
		 
		 if(faucetField!=null && faucetField.length>0)
		 {
			 String c="";
			 String idList = "";
			 String pattern = "[^A-Za-z0-9]";
			 HttpSolrServer server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
			 String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			 if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			 }
			 String fl = "itemid,partnumber,altPartNumber1,brand,manufacturerName,manfId,manfpartnumber,brandID,salesUom,qtyAvailable,packageQty,productName,productDesc,productImageName,productImageType,productItemCount,imageName,imageType,description,upc,attr_*,custom_*,pageTitle";
					//&fl=itemid,partnumber,altPartNumber1,brand,manufacturerName,manfId,manfpartnumber,brandID,description,salesUom,qtyAvailable,packageQty,productItemCount,imageName,imageType,upc,custom_*,	 
				
					 server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				
				int resultCount = 0;
				query.setQuery("productId:"+productId);
				query.addField(fl);
				query.setStart(0);
				query.setRows(1);
				
				if(faucetField!=null && faucetField.length>0)
				{
					query.addFacetField(faucetField);
					query.setFacetLimit(-1);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
						query.setFacetSort(true);
					}else{
						query.setFacetSort(false);
					}
				}

				query.setStart(fromRow);
				query.setRows(resultPerPage);
				query.setFilterQueries(attrFtrNav);
				query.addSortField("displaySeq", SolrQuery.ORDER.asc);
				
				query.addFilterQuery("defaultCategory:Y");
				System.out.println("Category Query : " + query);
				QueryResponse response = server.query(query,METHOD.POST);
				query.setRows((int) response.getResults().getNumFound());
				response = server.query(query,METHOD.POST); 
				
				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				Iterator<SolrDocument> itr = documents.iterator();
				LinkedHashMap<String, ArrayList<String>> selectableList = new LinkedHashMap<String, ArrayList<String>>();
				LinkedHashMap<String, ArrayList<ProductsModel>> selectableMultiList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				LinkedHashMap<String, ArrayList<String>> selectableMultiListString = new LinkedHashMap<String, ArrayList<String>>();
				ArrayList<String> selMultiVal = new ArrayList<String>();
				ArrayList<ProductsModel> selMultiValArr = new ArrayList<ProductsModel>();
				LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> slectableGen = new LinkedHashMap<String, LinkedHashMap<String,ArrayList<String>>>();
				
				LinkedHashMap<String, ArrayList<ProductsModel>> multiFaucetResult = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				if(resultCount>0 && attrFtrGlobal!=null && attrFtrGlobal.length>0)
				{
					multiFaucetResult = ProductHunterSolrUltimate.generateFaucetFilter("productId:"+productId, attrFtrGlobal, fq, false, false,null,null,filteredMultList);
				}
				
				
				ArrayList<ProductsModel> attrFilteredList = ProductHunterSolrUltimate.buildFacetFilter(facetFeild, multiFaucetResult, filteredMultList);
				selectableObj.put("attrList", attrFilteredList);
				ArrayList<Integer> itemList = new ArrayList<Integer>();
				
				while (itr.hasNext()) {
					
					try{
					
					ProductsModel itemModel = new ProductsModel();
					SolrDocument doc = itr.next();
					idList = idList + c + doc.getFieldValue("itemid").toString();
					itemList.add(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
					itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
					itemModel.setMinOrderQty(1);
					itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
					itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
					itemModel.setUpc((doc.getFieldValue("upc")!=null?doc.getFieldValue("upc").toString():""));
					//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
					itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
					itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
					itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
					itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
					itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
					itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
					itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
					itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
					itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
					itemModel.setProductName((doc.getFieldValue("productName")!=null?doc.getFieldValue("productName").toString():""));
					itemModel.setProductDescription((doc.getFieldValue("productDesc")!=null?doc.getFieldValue("productDesc").toString():""));
					itemModel.setProductCategoryImageName((doc.getFieldValue("productImageName")!=null?doc.getFieldValue("productImageName").toString():""));
					itemModel.setProductCategoryImageType((doc.getFieldValue("productImageType")!=null?doc.getFieldValue("productImageType").toString():""));
					itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
					if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
					{
						if(doc.getFieldValue("productItemCount")!=null){
							itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
						}
					}
					
					ArrayList<ProductsModel> attributeDataList = new  ArrayList<ProductsModel>();
					for(FacetField test:facetFeild)
					{
						ProductsModel attrList = new ProductsModel();
						System.out.println(test.getName());
						 //String atrVal = (String)doc.getFieldValue(test.getName());
						String attributeValueArr = (String)doc.getFieldValue(test.getName());
						String[] valueArr = null;
						String attrValue = "";
						if(multiAttribute){
							
							

							valueArr = attributeValueArr.split("\\|~\\|");
							
							if(valueArr!=null && valueArr.length>2){
								attrValue = valueArr[1];
								
								
							}else if(valueArr!=null && valueArr.length>1){
								attrValue = valueArr[1];
							}else{
								attrValue = (String)doc.getFieldValue(test.getName());
							}
							attrValue = attrValue.trim();
							attrValue = attrValue.replaceAll(pattern," ");
							attrValue = attrValue.replaceAll("\\s+","-");
							
						}else{
							attrValue = (String)doc.getFieldValue(test.getName());
						}
						
						if( attrValue!=null)
						{
							 LinkedHashMap<String, ArrayList<String>> temp1 = slectableGen.get(test.getName().replace(attributePrepend, ""));
							 
							 if(temp1!=null)
							 {
								 ArrayList<String> temp2 = temp1.get(attrValue);
								 if(temp2!=null)
								 {
									 if(!temp2.contains((String) doc.getFieldValue("itemid")))
									 {
									 temp2.add((String) doc.getFieldValue("itemid")) ;
									 temp1.put(attrValue, temp2);
									 slectableGen.put(test.getName().replace(attributePrepend, ""), temp1);
									 }
								 }
								 else
								 {
									 temp2 = new ArrayList<String>();
									 temp2.add((String) doc.getFieldValue("itemid")) ;
									 temp1.put(attrValue, temp2);
									 slectableGen.put(test.getName().replace(attributePrepend, ""), temp1);
								 }
							 }
							 else
							 {
								 temp1 = new LinkedHashMap<String, ArrayList<String>>();
								 ArrayList<String> temp2 = new ArrayList<String>();
								 temp2.add((String) doc.getFieldValue("itemid")) ;
								 temp1.put(attrValue, temp2);
								 slectableGen.put(test.getName().replace(attributePrepend, ""), temp1);
								
							 }
						}
						
						ArrayList<String> selVal = selectableList.get(test.getName().replace("attr_", ""));
						
						if(selVal!=null)
						{
							if(doc.getFieldValue(test.getName())!=null)
							{
								if(!selVal.contains((String)doc.getFieldValue(test.getName())))
								{
										selVal.add((String)doc.getFieldValue(test.getName()));
										selectableList.put(test.getName().replace("attr_", ""), selVal);
								}
							}                                         
							
						}
						else
						{
							selVal = new ArrayList<String>();
							if(doc.getFieldValue(test.getName())!=null)
							{
								
										selVal.add((String)doc.getFieldValue(test.getName()));
										selectableList.put(test.getName().replace("attr_", ""), selVal);
							}
						}
						
						
						
						if(multiAttribute){
							String imageFilter = "N";
							String displayTextFilter = "N";
							String itemImageFilter = "N";
							String itemDisplayTextFilter = "N";
							String attributeValueEncoded = "";
							String attributeImageName = "";
							String attributeImageType =  "";
							attrValue = "";
							attributeValueArr = (String)doc.getFieldValue(test.getName());
							valueArr = null;
							

							valueArr = attributeValueArr.split("\\|~\\|");
							
							if(valueArr!=null && valueArr.length>2){
								attrValue = valueArr[1];
								imageFilter = valueArr[2];
								if(valueArr.length>3){
									displayTextFilter = valueArr[3];
								}if(valueArr.length>4){
									itemImageFilter = valueArr[4];
								}if(valueArr.length>5){
									itemDisplayTextFilter = valueArr[5];
								}if(valueArr.length>6){
									attributeImageName = valueArr[6];
								}if(valueArr.length>7){
									attributeImageType = valueArr[7];
								}
								
								
							}else if(valueArr!=null && valueArr.length>1){
								attrValue = valueArr[1];
							}else{
								attrValue = (String)doc.getFieldValue(test.getName());
							}
							attrValue = attrValue.trim();
							attributeValueEncoded = attrValue.replaceAll(pattern," ");
							attributeValueEncoded = attributeValueEncoded.replaceAll("\\s+","-");
							
							 selMultiVal = selectableMultiListString.get(test.getName().replace("attr_Multi_", ""));
							 selMultiValArr = selectableMultiList.get(test.getName().replace("attr_Multi_", ""));
							 ProductsModel multiVal = new ProductsModel();
							 
							 	multiVal.setAttrValue(attrValue.trim());
								multiVal.setImageFilter(imageFilter);
								multiVal.setDisplayFilterText(displayTextFilter);
								multiVal.setImageName(attributeImageName);
								multiVal.setImageType(attributeImageType);
								multiVal.setItemImageFilter(itemImageFilter);
								multiVal.setItemDisplayFilterText(itemDisplayTextFilter);
								multiVal.setAttrValueEncoded(attributeValueEncoded);
								
								
							if(selMultiVal!=null)
							{
								if(doc.getFieldValue(test.getName())!=null)
								{
									if(!selMultiVal.contains((String)doc.getFieldValue(test.getName())))
									{
										selMultiVal.add((String)doc.getFieldValue(test.getName()));
										selectableMultiListString.put(test.getName().replace("attr_Multi_", ""), selMultiVal);
										selMultiValArr.add(multiVal);
										selectableMultiList.put(test.getName().replace("attr_Multi_", ""), selMultiValArr);
									}
								}                                         
								
							}
							else
							{
								selMultiVal = new ArrayList<String>();
								selMultiValArr = new ArrayList<ProductsModel>();
								selMultiValArr.add(multiVal);
								if(doc.getFieldValue(test.getName())!=null)
								{
									selectableMultiList.put(test.getName().replace("attr_Multi_", ""), selMultiValArr);
									selMultiVal.add((String)doc.getFieldValue(test.getName()));
									selectableMultiListString.put(test.getName().replace("attr_Multi_", ""), selMultiVal);
								}
							}
						}
						
						;
						
						attrList.setAttrValue(attrValue);
						attrList.setAttrName(test.getName().replace("attr_", ""));
						attributeDataList.add(attrList);
					}
					itemModel.setAttributeDataList(attributeDataList);
					itemModel.setResultCount(resultCount);
					
					String imageName = null;
	       			String imageType = null;
	       			
	       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
	       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			}
	       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	       			
	       			
	   			 itemUrl = itemUrl.replaceAll(pattern," ");
	   			 itemUrl = itemUrl.replaceAll("\\s+","-");
	            	itemModel.setItemUrl(itemUrl);
	       			
	       			if(doc.getFieldValue("imageName")!=null)
	       				imageName = doc.getFieldValue("imageName").toString();
	       			
	       					       			
	       			if(doc.getFieldValue("imageType")!=null)
	       				imageType = doc.getFieldValue("imageType").toString();
	       			if(doc.getFieldValue("upc")!=null)
	       				itemModel.setUpc(doc.getFieldValue("upc").toString());	
	       			
	       			if(imageName==null)
	       			{
	       				imageName = "NoImage.png";
	       				imageType = "IMAGE";
	       			}
	       			itemModel.setImageName(imageName.trim());
	       			itemModel.setImageType(imageType);
					c = " OR ";
					
					Map<String, Object> customFieldValMap = doc.getFieldValueMap();
					LinkedHashMap<String, Object> customFieldVal = ProductHunterSolr.getAllCustomFieldVal(customFieldValMap);
					if(customFieldVal!=null && customFieldVal.size()>0)
					itemModel.setCustomFieldVal(customFieldVal);
					
					itemLevelFilterData.add(itemModel);
					
					}catch (Exception e) {
						System.out.println("Error Occured While retriving data from solr.");
						e.printStackTrace();
					}
					
				}
				
				
				 if(itemLevelFilterData.size()>0)
					{
						HttpSolrServer server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
						LinkedHashMap<String, String> itemMap = new LinkedHashMap<String, String>();
						
						server1.setParser(new XMLResponseParser());
						QueryRequest req1 = new QueryRequest();
						req1.setMethod(METHOD.POST);
						SolrQuery query1 = new SolrQuery();
						if(generalSubset>0 && generalSubset!=subsetId) {
							 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
							 if(!siteNameSolr.equalsIgnoreCase(""))
							 {
								 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
							 }
						}

						query1.setQuery("itemid:("+idList+")");
						String fq2 = "type:"+indexType;
						query1.setFilterQueries(fq2);
						query1.setStart(0);
						query1.setRows(itemLevelFilterData.size());
						System.out.println("query1 : "+query1);
						QueryResponse response1 = server1.query(query1);
						
						SolrDocumentList documents1 = response1.getResults();
						resultCount = (int) response1.getResults().getNumFound();
						

						
						Iterator<SolrDocument> itr1 = documents1.iterator();
						NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
						ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
						while (itr1.hasNext()) {
							
							SolrDocument doc = itr1.next();
							for(ProductsModel iList : itemLevelFilterData)
							{
								int itemId = CommonUtility.validateNumber(doc.getFieldValue("itemid").toString());
								
								
							
								if(itemId==iList.getItemId())
								{
									itemMap.put((String) doc.getFieldValue("itemid"), (String)doc.getFieldValue("itemPriceId"));
									ProductsModel ProductsModel = new ProductsModel();
					       	        ProductsModel.setErpPartNumber(iList.getPartNumber());
					       	        partIdentifiersList.add(ProductsModel);
									int minOrdQty = 1;
									int orderQtyInterval = 1;
									int saleQty = 1;
									int packageQty = 1;
									int packageFlag = 0;
									String mOrdQty = null;
									String iPrice ="";
									if(doc.getFieldValue("price")!=null)
									{
										iPrice = doc.getFieldValue("price").toString();
									}
										
									
									double itemPrice = 0d;
									if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
									{
										itemPrice = Double.parseDouble(iPrice);
									}
									
									iList.setPrice(itemPrice);
									if(doc.getFieldValue("uom")!=null)
									iList.setUom(doc.getFieldValue("uom").toString());
									iList.setItemPriceId(CommonUtility.validateNumber(doc.getFieldValue("itemPriceId").toString()));
									
																
								/*
								 *  commented next lines 
								 *  
								 	if(doc.getFieldValue("customerPartNumber")!=null)
										iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
										
								*/		
									
									if(doc.getFieldValue("materialGroup")!=null)
										iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
									
									if(doc.getFieldValue("minordqty")!=null)
									{
										if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
											minOrdQty = CommonUtility.validateNumber(doc.getFieldValue("minordqty").toString());
										if(minOrdQty==0)
											minOrdQty = 1;
									}
									if(doc.getFieldValue("orderQtyInterval")!=null)
									{
										if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
											orderQtyInterval = CommonUtility.validateNumber(doc.getFieldValue("orderQtyInterval").toString());
										if(orderQtyInterval==0)
											orderQtyInterval = 1;
									}
									if(doc.getFieldValue("salesQty")!=null)
									{
										if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
											saleQty = CommonUtility.validateNumber(doc.getFieldValue("salesQty").toString());
										
										if(saleQty==0)
											saleQty = 1;
									}
									
									if(doc.getFieldValue("packageFlag")!=null)
									{
										if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
											packageFlag = CommonUtility.validateNumber(doc.getFieldValue("packageFlag").toString());
									}
									
									if(doc.getFieldValue("packageQty")!=null)
									{
										if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
										{
											packageQty = CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString());
										}
										
									}
									
									if(doc.getFieldValue("packDesc")!=null)
									{
										String packDesc = doc.getFieldValue("packDesc").toString();
										iList.setPackDesc(packDesc);
									}
									
									if(doc.getFieldValue("displayPricing") != null){
										
										iList.setDisplayPricing(doc.getFieldValue("displayPricing").toString());
									}
									if(doc.getFieldValue("clearanceFlag") != null){
										
										iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
									}
									
									iList.setPackageFlag(packageFlag);
									iList.setPackageQty(packageQty);
									iList.setSaleQty(saleQty);
									iList.setMinOrderQty(minOrdQty);
									iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
									
									//iList.setCustomerPartNumberList(getcustomerParnumber(itemId, buyingCompanyId, buyingCompanyId));
									
									
								}
							}
						}
						
						
						Gson gson = new Gson();
						
						selectableObj.put("itemList", itemLevelFilterData);
						selectableObj.put("selectableList", selectableList);
						selectableObj.put("selectableMultiList", selectableMultiList);
						selectableObj.put("selecatableJson", gson.toJson(slectableGen));
						selectableObj.put("itemMap", gson.toJson(itemMap));
						selectableObj.put("selecatableMap", slectableGen);
						selectableObj.put("itemIdAndpriceidMap", itemMap);
						
						System.out.println("SELECT:" + slectableGen);
						System.out.println("Json Data : " + gson.toJson(slectableGen));
						System.out.println("Multi data : " + gson.toJson(selectableMultiList));
						
						ConnectionManager.closeSolrClientConnection(server1);
					}
				//searchResultList.put("categeoryList", taxonomyListVal);
				 ConnectionManager.closeSolrClientConnection(server);
		 }
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return selectableObj;
}

public static LinkedHashMap<Integer, ArrayList<ProductsModel>> getcustomerPartnumber(String idList,int buyingCompanyId,int customerBuyingCompanyId)
{
	LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = new LinkedHashMap<Integer, ArrayList<ProductsModel>>();
	ArrayList<ProductsModel> customerPartNumberList = new ArrayList<ProductsModel>();
	HttpSolrServer server = null;
	request =ServletActionContext.getRequest();
	HttpSession session = request.getSession();
	try
	{
		int userLevelBuyingCompanyId= CommonUtility.validateNumber((String)session.getAttribute("userLevelBuyingCompanyId"));
		if(userLevelBuyingCompanyId >0 && userLevelBuyingCompanyId != buyingCompanyId && userLevelBuyingCompanyId != customerBuyingCompanyId && CommonUtility.customServiceUtility() != null )
		{
		
			 buyingCompanyId = CommonUtility.customServiceUtility().getUserBuyingCompanyId( buyingCompanyId ,  userLevelBuyingCompanyId);
			 customerBuyingCompanyId =CommonUtility.customServiceUtility().getUserBuyingCompanyId( customerBuyingCompanyId ,  userLevelBuyingCompanyId);
		
		}
		server = ConnectionManager.getSolrClientConnection(solrURL+"/customerdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
		String fq="buyingCompanyId:"+buyingCompanyId;
		if(customerBuyingCompanyId>0)
			fq = "buyingCompanyId:("+buyingCompanyId + " OR "+customerBuyingCompanyId+")";
		int resultCount = 0;
		query.setQuery("itemid:("+idList+")");
		String attrFtr[] = fq.split("~");
		query.setStart(0);
		query.setRows(100);
		
		query.setFilterQueries(attrFtr);
		
		QueryResponse response = server.query(query);
		System.out.println("Customer Part Number Query : " + query);
		SolrDocumentList documents = response.getResults();
		resultCount = (int) response.getResults().getNumFound();
		Iterator<SolrDocument> itr = documents.iterator();
		while (itr.hasNext()) {
			
			SolrDocument doc = itr.next();
			int id = CommonUtility.validateNumber(doc.getFieldValue("itemid").toString());
			customerPartNumberList = customerPartNumber.get(id);
			ProductsModel nameCode= new ProductsModel();
			String pattern = "[^A-Za-z0-9]";
			nameCode.setPartNumber(doc.getFieldValue("customerPartNumber").toString());
        	nameCode.setIdList(doc.getFieldValue("id").toString());
        	if(doc.getFieldValue("recordType")!=null)
        	nameCode.setRecordType(doc.getFieldValue("recordType").toString());
        	if(doc.getFieldValue("recordDescription")!=null)
        	nameCode.setRecordDescription(doc.getFieldValue("recordDescription").toString());
        	if(doc.getFieldValue("shipToId")!=null)
        	nameCode.setShipToId(doc.getFieldValue("shipToId").toString());
        	if(customerPartNumberList==null)
        	{
        		customerPartNumberList = new ArrayList<ProductsModel>();
        		customerPartNumberList.add(nameCode);
        	}
        	else
        	customerPartNumberList.add(nameCode);
        	
        	customerPartNumber.put(id, customerPartNumberList);
        	
		}
		
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return customerPartNumber;
}
public static int removeCustomerPartNumber(String custPartList[],String custPlist)
{
	int count = 0;
	HttpSolrServer server = ConnectionManager.getSolrClientConnection(solrURL+"/customerdata");
	Connection conn = null;
	PreparedStatement pstmt = null;
	try
	{
		conn = ConnectionManager.getDBConnection();
		String sql = "DELETE FROM CUSTOMER_PART_NUMBERS WHERE CUSTOMER_PART_ID IN ("+custPlist+")";
		pstmt = conn.prepareStatement(sql);
		count = pstmt.executeUpdate();
		if(count>0)
		{
			
		
		List<String> cust = new ArrayList<String>();
		Collections.addAll(cust, custPartList);
		server.deleteById(cust);
		server.commit(); 
		}
		
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}
	finally
	{
		ConnectionManager.closeDBPreparedStatement(pstmt);
		ConnectionManager.closeDBConnection(conn);
		ConnectionManager.closeSolrClientConnection(server);
	}
	
	return count;
}

public static int removeAllCustomerPartNumber(String custPartList[],int itemId, int buyingCompanyId)
{
	int count = 0;
	HttpSolrServer server = ConnectionManager.getSolrClientConnection(solrURL+"/customerdata");
	Connection conn = null;
	PreparedStatement pstmt = null;
	try
	{
		
		conn = ConnectionManager.getDBConnection();
		String sql = "DELETE FROM CUSTOMER_PART_NUMBERS WHERE ITEM_ID="+itemId+" AND BUYING_COMPANY_ID="+buyingCompanyId;
		pstmt = conn.prepareStatement(sql);
		count = pstmt.executeUpdate();
		if(count>0)
		{
			
		
		List<String> cust = new ArrayList<String>();
		Collections.addAll(cust, custPartList);
		server.deleteById(cust);
		server.commit(); 
		}
		
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}
	finally
	{
		ConnectionManager.closeDBPreparedStatement(pstmt);
		ConnectionManager.closeDBConnection(conn);
		ConnectionManager.closeSolrClientConnection(server);
	}
	
	return count;
}
public static int addNewCustomerPartNumber(String customerPartNumber,int itemId,int userId,String entityId,String updateBuyingCompany)
{
	
	HttpSolrServer server = ConnectionManager.getSolrClientConnection(solrURL+"/customerdata");
	Connection conn = null;
	PreparedStatement pstmt = null;
	int count = 0;
try
{


int customperPartId = CommonDBQuery.getSequenceId("CUSTOMER_PART_ID_SEQ");
String sql1 = "INSERT INTO CUSTOMER_PART_NUMBERS(CUSTOMER_PART_ID,ITEM_ID,BUYING_COMPANY_ID,CUSTOMER_PART_NUMBER,CUSTOMER_PARTNUMBER_KEYWORDS,UPDATED_DATETIME,USER_EDITED) SELECT ? CUSTOMER_PART_ID,? ITEM_ID,BUYING_COMPANY_ID,? CUSTOMER_PART_NUMBER,? CUSTOMER_PARTNUMBER_KEYWORDS,SYSDATE UPDATED_DATETIME,? USER_EDITED FROM BUYING_COMPANY WHERE ENTITY_ID =?";

String sql2 = "INSERT INTO CUSTOMER_PART_NUMBERS(CUSTOMER_PART_ID,ITEM_ID,BUYING_COMPANY_ID,CUSTOMER_PART_NUMBER,CUSTOMER_PARTNUMBER_KEYWORDS,UPDATED_DATETIME,USER_EDITED) SELECT ? CUSTOMER_PART_ID,? ITEM_ID,BC.BUYING_COMPANY_ID,? CUSTOMER_PART_NUMBER,? CUSTOMER_PARTNUMBER_KEYWORDS,SYSDATE UPDATED_DATETIME,? USER_EDITED FROM BUYING_COMPANY BC,BC_ADDRESS_BOOK BCAB,CIMM_USERS CU WHERE CU.USER_ID = ? AND BCAB.BC_ADDRESS_BOOK_ID = CU.DEFAULT_BILLING_ADDRESS_ID AND BC.ENTITY_ID = BCAB.ENTITY_ID";

conn = ConnectionManager.getDBConnection();
int buyingCompanyId = getBuyingcompanyIdForCustomerPartNumber(conn, entityId, userId, updateBuyingCompany);
if(buyingCompanyId>0)
{
	
	if(!customerPartnumberExist(conn, customerPartNumber, itemId, buyingCompanyId))
	{
		if(updateBuyingCompany!=null)
		{
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, customperPartId);
			pstmt.setInt(2, itemId);
			pstmt.setString(3, customerPartNumber);
			pstmt.setString(4, customerPartNumber);
			pstmt.setInt(5, userId);
			pstmt.setInt(6, userId);
		}
		else
		{
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, customperPartId);
			pstmt.setInt(2, itemId);
			pstmt.setString(3, customerPartNumber);
			pstmt.setString(4, customerPartNumber);
			pstmt.setInt(5, userId);
			pstmt.setString(6, entityId);
		}
		String pattern = "[^A-Za-z0-9]";
  		String scrubbedKeyword = customerPartNumber.replaceAll(pattern,"");
		count = pstmt.executeUpdate();
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", customperPartId);
		doc.addField("itemid", itemId);
		doc.addField("customerPartNumber", customerPartNumber);
		doc.addField("customerPartNumberKeyword", customerPartNumber+" ; "+scrubbedKeyword);
		doc.addField("buyingCompanyId", buyingCompanyId);
		server.add(doc);
		server.commit(); 
	}
	else
	{
		count = -1;
	}
	
}

}
catch (Exception e) {

e.printStackTrace();
}
finally
{
ConnectionManager.closeDBPreparedStatement(pstmt);
ConnectionManager.closeDBConnection(conn);
ConnectionManager.closeSolrClientConnection(server);
}
	    
	    
	System.out.println(customerPartNumber);
	return count;
}



public static int addNewCustomerPartNumber(String customerPartNumber,int itemId,int userId,int entityId,String updateBuyingCompany, int buyingCompanyId, String shipToId, String recordType, String recordDescription)
{
	
	HttpSolrServer server = ConnectionManager.getSolrClientConnection(solrURL+"/customerdata");
	Connection conn = null;
	PreparedStatement pstmt = null;
	int count = 0;
try
{


int customperPartId = CommonDBQuery.getSequenceId("CUSTOMER_PART_ID_SEQ");

String sql2 = "INSERT INTO CUSTOMER_PART_NUMBERS(CUSTOMER_PART_ID,ITEM_ID,BUYING_COMPANY_ID,CUSTOMER_PART_NUMBER,CUSTOMER_PARTNUMBER_KEYWORDS,UPDATED_DATETIME,USER_EDITED,SHIP_TO_ID,RECORD_TYPE,RECORD_DESCRIPTION) VALUES (? ,?,?,?,?,SYSDATE,?,?,?,?)";

conn = ConnectionManager.getDBConnection();

if(buyingCompanyId>0)
{
	
	if(!customerPartnumberExist(conn, customerPartNumber, itemId, buyingCompanyId))
	{
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, customperPartId);
			pstmt.setInt(2, itemId);
			pstmt.setInt(3, buyingCompanyId);
			pstmt.setString(4, customerPartNumber);
			pstmt.setString(5, customerPartNumber);
			pstmt.setInt(6, userId);
			pstmt.setString(7, CommonUtility.validateString(shipToId));
			pstmt.setString(8, CommonUtility.validateString(recordType));
			pstmt.setString(9, CommonUtility.validateString(recordDescription));
	}
		String pattern = "[^A-Za-z0-9]";
  		String scrubbedKeyword = customerPartNumber.replaceAll(pattern,"");
  		if(pstmt!=null)
		count = pstmt.executeUpdate();
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", customperPartId);
		doc.addField("itemid", itemId);
		doc.addField("customerPartNumber", customerPartNumber);
		doc.addField("customerPartNumberKeyword", customerPartNumber+" ; "+scrubbedKeyword);
		doc.addField("buyingCompanyId", buyingCompanyId);
		doc.addField("shipToId", CommonUtility.validateString(shipToId));
		doc.addField("recordType", CommonUtility.validateString(recordType));
		doc.addField("recordDescription", CommonUtility.validateString(recordDescription));
		server.add(doc);
		server.commit(); 
	}else{
		count = -1;
	}
}
catch (Exception e) {

e.printStackTrace();
}
finally
{
ConnectionManager.closeDBPreparedStatement(pstmt);
ConnectionManager.closeDBConnection(conn);
ConnectionManager.closeSolrClientConnection(server);
}
	    
	    
	System.out.println(customerPartNumber);
	return count;
}


public static boolean customerPartnumberExist(Connection conn,String customerPartnumber,int itemId,int buyingCompanyId)
{
	boolean partNumberExist = false;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try
	{
		String sql = "SELECT CUSTOMER_PART_ID FROM CUSTOMER_PART_NUMBERS WHERE regexp_replace(UPPER(CUSTOMER_PART_NUMBER), '[^A-Za-z0-9_-]', '') = ? AND ITEM_ID = ? AND BUYING_COMPANY_ID = ?";
		
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, customerPartnumber.toUpperCase());
		pstmt.setInt(2, itemId);
		pstmt.setInt(3, buyingCompanyId);
		rs = pstmt.executeQuery();
		if(rs.next()){
			partNumberExist = true;
		}
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}
	finally
	{
		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);
	}
	
	return partNumberExist;
}


/**
 *  Gets the Cutomer Part Nuber associated with Customer Part Number ID
 * @param customerPartNumberID ID of the Customer Part Number
 * @return Customer Part Number of which The Customer Part Number ID points to.
 */

public static String getCustomerPartNumber(int customerPartNumberID)
{
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	
	
	try
	{
		String sql = "SELECT CUSTOMER_PART_NUMBER  FROM CUSTOMER_PART_NUMBERS WHERE CUSTOMER_PART_ID = ?";
		conn = ConnectionManager.getDBConnection();
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, customerPartNumberID);
		
		rs = pstmt.executeQuery();
		if(rs.next())
			return rs.getString("CUSTOMER_PART_NUMBER");
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}
	finally
	{
		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);
		ConnectionManager.closeDBConnection(conn);
		
	}
	
	return null;
}



public static int getBuyingcompanyIdForCustomerPartNumber(Connection conn,String entityId, int userId,String updateBuyingCompany)
{
	int buyingCompanyId = 0;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try
	{
		String sql1 = "SELECT BC.BUYING_COMPANY_ID FROM BUYING_COMPANY BC, BC_ADDRESS_BOOK BCAB, CIMM_USERS CU WHERE CU.USER_ID = ? AND BCAB.BC_ADDRESS_BOOK_ID = CU.DEFAULT_BILLING_ADDRESS_ID AND BC.ENTITY_ID = BCAB.ENTITY_ID";
		String sql2 = "SELECT BUYING_COMPANY_ID FROM BUYING_COMPANY WHERE ENTITY_ID = ?";
		if(updateBuyingCompany!=null && updateBuyingCompany.trim().equalsIgnoreCase("Y"))
		{
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, userId);
		}
		else
		{
			pstmt = conn.prepareStatement(sql2);
			pstmt.setString(1, entityId);
		}
		
		rs = pstmt.executeQuery();
		if(rs.next())
		{
			buyingCompanyId = rs.getInt("BUYING_COMPANY_ID");
		}
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}
	finally
	{
		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);
	}
	
	return buyingCompanyId;
	
}


public static HashMap<String, ArrayList<ProductsModel>> taxonomyList(String keyWord,int subsetId,int generalSubset,int fromRow,int toRow,String requestType,int psid,int npsid,int treeId,int levelNo,String attrFilterList,String brandId, String sessionId,String sortBy, int resultPerPage, String narrowKeyword, int buyingCompanyId, String type)
{
	ArrayList<ProductsModel> taxonomyListVal = new ArrayList<ProductsModel>();
	ArrayList<ProductsModel> featuredTaxonomyListVal = new ArrayList<ProductsModel>();
	 HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
	 HttpSolrServer server = null;
	try{
		ORDER sortOrder = null;
		String sortField = "";
		SolrQuery query = new SolrQuery();
		if(sortBy!=null && !sortBy.trim().equalsIgnoreCase("")){
			if(sortBy.trim().equalsIgnoreCase("category")){
				sortField = "category";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("category_asc")){
				sortField = "category";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("category_desc")){
				sortField = "category";
				sortOrder = SolrQuery.ORDER.desc;
			}else if(sortBy.trim().equalsIgnoreCase("displaySequence")){
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("displaySequence_asc")){
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("displaySequence_desc")){
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.desc;
			}else if(sortBy.trim().equalsIgnoreCase("partnum_asc")){
				sortField = "partnumber";
				sortOrder = SolrQuery.ORDER.asc;
			}else{
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.asc;
			}
		}else{
			sortField = "displaySeq";
			sortOrder = SolrQuery.ORDER.asc;
		}
		if(type==null){
			type = "";
		}
		String indexType = "PH_SEARCH_"+subsetId;
		if(generalSubset>0 && generalSubset!=subsetId){
			//indexType = "PH_SEARCH_"+generalSubset+"_"+subsetId;
			   indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";

		}
		server = ConnectionManager.getSolrClientConnection(solrURL+"/catalogdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		//String fq="type:"+indexType+"~"+"parentCategoryId:"+treeId;
		
		String fq="type:"+indexType;
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
			fq="type:PH_SEARCH_ALL";
		}
		//to fetch featured Categories of child and Parent
			if(CommonUtility.validateString(type).equalsIgnoreCase("FeatureCategories")){
				fq+="~"+"featuredCategory:\"Y\"";
			}else {
				fq+="~"+"parentCategoryId:"+treeId;
			}
		int resultCount = 0;
		query.setQuery("*:*");
		//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
		String attrFtr[] = fq.split("~");
		query.setStart(0);
		query.setRows(100);
		
		query.addSortField(sortField, sortOrder);
		query.setFilterQueries(attrFtr);
		
		QueryResponse response = server.query(query);
		System.out.println("Category Query : " + query);
		
		SolrDocumentList documents = response.getResults();
		resultCount = (int) response.getResults().getNumFound();
		List<FacetField> facetFeild = response.getFacetFields();
		Iterator<SolrDocument> itr = documents.iterator();
		while (itr.hasNext()) {
			
			
			SolrDocument doc = itr.next();
			
			
			ProductsModel nameCode= new ProductsModel();
			String itemUrl = doc.getFieldValue("category").toString();
        	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
			String pattern = "[^A-Za-z0-9]";
			 itemUrl = itemUrl.replaceAll(pattern," ");
			 itemUrl = itemUrl.replaceAll("\\s+","-");
        	 nameCode.setItemUrl(itemUrl);
        	 nameCode.setCategoryCode(doc.getFieldValue("categoryID").toString());
        	 nameCode.setCategoryName(doc.getFieldValue("category").toString());
        	// nameCode.setCategoryCount(searchCategory.getInt("CATEGORY_COUNT"));
        	 int levelNum = CommonUtility.validateNumber(doc.getFieldValue("levelNumber").toString());
        	 nameCode.setLevelNumber(levelNum);
        	 String imageName = null;
        	 if(doc.getFieldValue("imageName")==null){
        		 imageName = "NoImage.png";
        	 }else{
        		 imageName = doc.getFieldValue("imageName").toString();
        	 }
        	 if(doc.getFieldValue("imageType")!=null){
        		 nameCode.setImageType(doc.getFieldValue("imageType").toString());
        	 }
        	 if(doc.getFieldValue("categoryDesc")!=null){
        		 nameCode.setCategoryDesc(doc.getFieldValue("categoryDesc").toString());
        	 }
        	 nameCode.setImageName(imageName);
        	 if(doc.getFieldValue("featuredCategory")!=null && doc.getFieldValue("featuredCategory").toString().equalsIgnoreCase("Y")){
        		 nameCode.setFeaturedCategory(doc.getFieldValue("featuredCategory").toString());
        		 featuredTaxonomyListVal.add(nameCode);
        	 }
        	 taxonomyListVal.add(nameCode);
			
		}
		searchResultList.put("categeoryList", taxonomyListVal);
		searchResultList.put("featuredCategeoryList", featuredTaxonomyListVal);
		if(resultCount>0)
		{
			String attrFtr1[] = null;
			String faucetField[] = new String[CommonDBQuery.getDefaultFacet().size()];
			faucetField  = CommonDBQuery.getDefaultFacet().toArray(faucetField);
			 String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			 if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			 }
			 if(treeId==0){
				 fqNav = fqNav + "~defaultCategory:\"Y\"";				 
			 }
			 ProductsModel searchResultData = ProductHunterSolrUltimate.solrNavigationSearchResult("*:*", solrURL+"/mainitemdata", fqNav.split("~"), 0, 1, "", faucetField, "", "partnumber", SolrQuery.ORDER.asc, true, "catSearchId:"+treeId,null,fq,null,null,false);
			 ArrayList<ProductsModel> itemLevelFilterData = searchResultData.getItemDataList();
			 if(itemLevelFilterData.size()>0){
				 ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
				 searchResultList.put("attrList", attrFilteredList );
			 }
		}
		
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return searchResultList;
}
public static HashMap<String, ArrayList<ProductsModel>> taxonomyListCategory(String keyWord,int subsetId,int generalSubset,int fromRow,int toRow,String requestType,int psid,int npsid,int treeId,int levelNo,String attrFilterList,String brandId, String sessionId,String sortBy, int resultPerPage, String narrowKeyword, int buyingCompanyId, String type)
{
	ArrayList<ProductsModel> taxonomyListVal = new ArrayList<ProductsModel>();
	 HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
	 HttpSolrServer server = null;
	 HttpSolrServer server1 = null;
	try{
		ORDER sortOrder = null;
		String sortField = "";
		SolrQuery query = new SolrQuery();
		if(sortBy!=null && !sortBy.trim().equalsIgnoreCase("")){
			if(sortBy.trim().equalsIgnoreCase("category")){
				sortField = "category";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("category_asc")){
				sortField = "category";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("category_desc")){
				sortField = "category";
				sortOrder = SolrQuery.ORDER.desc;
			}else if(sortBy.trim().equalsIgnoreCase("displaySequence")){
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("displaySequence_asc")){
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("displaySequence_desc")){
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.desc;
			}else if(sortBy.trim().equalsIgnoreCase("price_high")){
				sortField = "NET_PRICE";
				sortOrder = SolrQuery.ORDER.desc;
			}
			else if(sortBy.trim().equalsIgnoreCase("price_low"))
			{
				sortField = "NET_PRICE";
				sortOrder = SolrQuery.ORDER.asc;
			}
			else if(sortBy.trim().equalsIgnoreCase("erp_price_low"))
			{
				sortField = "price";
				sortOrder = SolrQuery.ORDER.asc;
			}
			else if(sortBy.trim().equalsIgnoreCase("erp_price_high"))
			{
				sortField = "price";
				sortOrder = SolrQuery.ORDER.desc;
			}

			else if(sortBy.trim().equalsIgnoreCase("brand"))
			{
				sortField = "brand";
				sortOrder = SolrQuery.ORDER.asc;
			}
			else if(sortBy.trim().equalsIgnoreCase("brand_asc"))
			{
				sortField = "brand";
				sortOrder = SolrQuery.ORDER.asc;
			}
			else if(sortBy.trim().equalsIgnoreCase("brand_desc"))
			{
				sortField = "brand";
				sortOrder = SolrQuery.ORDER.desc;
			}
			else if(sortBy.trim().equalsIgnoreCase("partnum_asc"))
			{
				sortField = "partnumber";
				sortOrder = SolrQuery.ORDER.asc;
			}
			else if(sortBy.trim().equalsIgnoreCase("partnum_desc"))
			{
				sortField = "partnumber";
				sortOrder = SolrQuery.ORDER.desc;
			}else if(sortBy.trim().equalsIgnoreCase("externalHitSort_desc")){

				sortField = "externalHitSort";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("externalHitSort_asc")){

				sortField = "externalHitSort";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("bestSellerSort_desc")){

				sortField = "bestSellerSort";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("bestSellerSort_asc")){

				sortField = "bestSellerSort";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("internalHitSort_desc")){

				sortField = "internalHitSort";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("internalHitSort_asc")){

				sortField = "internalHitSort";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("MPN_desc")){

				sortField = "manfpartnumber";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("MPN_asc")){

				sortField = "manfpartnumber";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("upc_desc")){

				sortField = "upc";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("upc_asc")){

				sortField = "upc";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("desc_desc")){

				sortField = "description_sort";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("desc_asc")){

				sortField = "description_sort";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("manufacturer_asc")){

				sortField = "manufacturerName";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("manufacturer_desc")){

				sortField = "manufacturerName";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("popularity_desc")){
				sortField = "popularity";
				sortOrder = SolrQuery.ORDER.desc;
			}else if(sortBy.trim().equalsIgnoreCase("manufacturerDisplayNameSort_asc")){
				sortField = "manufacturerDisplayNameSort";
				sortOrder = SolrQuery.ORDER.asc;
		    }else if(sortBy.trim().equalsIgnoreCase("manufacturerDisplayNameSort_desc")){
				sortField = "manufacturerDisplayNameSort";
				sortOrder = SolrQuery.ORDER.desc;
		    }else if (sortBy.trim().equalsIgnoreCase("altpartnum_asc")){                                                       
		          sortField = "alternatePartNumber1";                          
		          sortOrder = SolrQuery.ORDER.asc;                 
		    }else if (sortBy.trim().equalsIgnoreCase("altpartnum_desc")){                      
		          sortField = "alternatePartNumber1";                          
		          sortOrder = SolrQuery.ORDER.desc;                              
		    }
			else{
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.asc;
			}
		}else{
			sortField = "displaySeq";
			sortOrder = SolrQuery.ORDER.asc;
		}
		if(type==null){
			type = "";
		}
		String indexType = "PH_SEARCH_"+subsetId;
		if(generalSubset>0 && generalSubset!=subsetId){
			indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
		}
		server = ConnectionManager.getSolrClientConnection(solrURL+"/catalogdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
			indexType="PH_SEARCH_ALL";
		}
		String fq="type:"+indexType+"~"+"parentCategoryId:"+treeId;
		int resultCount = 0;
		int rowId=0;
		rowId=CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
		query.setQuery("*:*");
		//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
		String attrFtr[] = fq.split("~");
		query.setStart(0);
		query.setRows(100);
		
		query.addSortField(sortField, sortOrder);
		query.setFilterQueries(attrFtr);
		
		QueryResponse response = server.query(query);
		System.out.println("Category Query : " + query);
		
		SolrDocumentList documents = response.getResults();
		resultCount = (int) response.getResults().getNumFound();
		List<FacetField> facetFeild = response.getFacetFields();
		Iterator<SolrDocument> itr = documents.iterator();
		while (itr.hasNext()) {
			
			
			SolrDocument doc = itr.next();
			
			
			ProductsModel nameCode= new ProductsModel();
			String itemUrl = doc.getFieldValue("category").toString();
        	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
			String pattern = "[^A-Za-z0-9]";
			 itemUrl = itemUrl.replaceAll(pattern," ");
			 itemUrl = itemUrl.replaceAll("\\s+","-");
        	 nameCode.setItemUrl(itemUrl);
        	 nameCode.setCategoryCode(doc.getFieldValue("categoryID").toString());
        	 nameCode.setCategoryName(doc.getFieldValue("category").toString());
        	 if(doc.getFieldValue("categoryDesc")!=null){
        		 nameCode.setCategoryDesc(doc.getFieldValue("categoryDesc").toString());
        	 }
        	 server = ConnectionManager.getSolrClientConnection(solrURL+"/catalogdata");
        	 query = new SolrQuery();
        	 query.setQuery("parentCategoryId:"+doc.getFieldValue("categoryID"));
        	 QueryResponse response1 = server.query(query);        	
        	 int resultCount1 = (int) response1.getResults().getNumFound();
        	 nameCode.setCategoryCount(resultCount1);

        	 int levelNum = CommonUtility.validateNumber(doc.getFieldValue("levelNumber").toString());
        	 nameCode.setLevelNumber(levelNum);
        	 String imageName = null;
        	 if(doc.getFieldValue("imageName")==null){
        		 imageName = "NoImage.png";
        	 }else{
        		 imageName = doc.getFieldValue("imageName").toString();
        	 }
        	 if(doc.getFieldValue("imageType")!=null){
        		 nameCode.setImageType(doc.getFieldValue("imageType").toString());
        	 }
        	 nameCode.setImageName(imageName);
        	 taxonomyListVal.add(nameCode);
			
		}
		searchResultList.put("categeoryList", taxonomyListVal);
		if(resultCount>0)
		{
			String attrFtr1[] = null;
			String faucetField[] = new String[CommonDBQuery.getDefaultFacet().size()];
			String facetInnderUrl = solrURL+"/categoryattribute"; 
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CATEGORY_LEVEL_ATTRIBUTES")).equals("Y")) {
				faucetField = ProductHunterSolrUltimate.getFaucetFieldForSearch("", facetInnderUrl, treeId,new ArrayList<ProductsModel>(),false,false,false,false,false,CommonDBQuery.getDefaultFacet(),CommonDBQuery.getDefaultFacet());
			}else {
				faucetField  = CommonDBQuery.getDefaultFacet().toArray(new String[0]);
			}
			 String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			 if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			 }
			 if(treeId==0){
				 fqNav = fqNav + "~defaultCategory:\"Y\"";				 
			 }
				if(CommonDBQuery.getSystemParamtersList().get("ENABLE_CATEGORY_LEVEL_ITEMS")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_CATEGORY_LEVEL_ITEMS").trim().equalsIgnoreCase("Y"))
				{
					ProductsModel searchResultData = ProductHunterSolrUltimate.solrNavigationSearchResult("*:*", solrURL+"/mainitemdata", fqNav.split("~"), fromRow, toRow, "", faucetField, "", sortField, sortOrder, true, "catSearchId:"+treeId,null,fq,null,null,false);
					 ArrayList<ProductsModel> itemLevelFilterData = searchResultData.getItemDataList();
						
						if(itemLevelFilterData.size()>0)
						{
							String idList = "";
							idList = searchResultData.getIdList();
							LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartnumber = getcustomerPartnumber(idList, buyingCompanyId, buyingCompanyId);
							server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
							server1.setParser(new XMLResponseParser());
							QueryRequest req1 = new QueryRequest();
							req1.setMethod(METHOD.POST);
							SolrQuery query1 = new SolrQuery();
							if(generalSubset>0 && generalSubset!=subsetId) {
								 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
								 if(!siteNameSolr.equalsIgnoreCase(""))
								 {
									 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
								 }
							    }


							query1.setQuery("itemid:("+idList+")");
							String fq2 = "type:"+indexType;
							query1.setFilterQueries(fq2);
							query1.setStart(0);
							query1.setRows(itemLevelFilterData.size());

							QueryResponse response1 = server1.query(query1);

							SolrDocumentList documents1 = response1.getResults();
							resultCount = (int) response1.getResults().getNumFound();



							Iterator<SolrDocument> itr1 = documents1.iterator();
							NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
							ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
							while (itr1.hasNext()) {

								SolrDocument doc = itr1.next();
								for(ProductsModel iList : itemLevelFilterData)
								{
									int itemId = CommonUtility.validateNumber(doc.getFieldValue("itemid").toString());
									if(itemId==iList.getItemId())
									{
										ProductsModel ProductsModel = new ProductsModel();
										ProductsModel.setErpPartNumber(iList.getPartNumber());
										partIdentifiersList.add(ProductsModel);
										int minOrdQty = 1;
										int orderQtyInterval = 1;
										int saleQty = 1;
										
										int packageQty = 1;
										if(iList.getPackageQty()>0){
											packageQty = iList.getPackageQty();
										}
										
										
										int packageFlag = 0;
										String mOrdQty = null;
										String iPrice ="";
										float imapPrice = 0;
										String imap = null;
										if(doc.getFieldValue("price")!=null)
										{
											iPrice = doc.getFieldValue("price").toString();
										}


										double itemPrice = 0d;
										if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
										{
											itemPrice = Double.parseDouble(iPrice);
										}

										iList.setPrice(itemPrice);
										if(doc.getFieldValue("uom")!=null)
											iList.setUom(doc.getFieldValue("uom").toString());
										iList.setItemPriceId(CommonUtility.validateNumber(doc.getFieldValue("itemPriceId").toString()));


										/*
										 *  commented next lines 
										 *  
										 	if(doc.getFieldValue("customerPartNumber")!=null)
												iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());

										 */		
										
										if(doc.getFieldValue("overRidePriceRule")!=null){
											iList.setOverRidePriceRule((String)doc.getFieldValue("overRidePriceRule"));
										}
										if(doc.getFieldValue("materialGroup")!=null)
											iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());

										if(doc.getFieldValue("minordqty")!=null)
										{
											if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
												minOrdQty = CommonUtility.validateNumber(doc.getFieldValue("minordqty").toString());
											if(minOrdQty==0)
												minOrdQty = 1;
										}
										if(doc.getFieldValue("orderQtyInterval")!=null)
										{
											if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
												orderQtyInterval = CommonUtility.validateNumber(doc.getFieldValue("orderQtyInterval").toString());
											if(orderQtyInterval==0)
												orderQtyInterval = 1;
										}
										if(doc.getFieldValue("salesQty")!=null)
										{
											if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
												saleQty = CommonUtility.validateNumber(doc.getFieldValue("salesQty").toString());

											if(saleQty==0)
												saleQty = 1;
										}

										if(doc.getFieldValue("packageFlag")!=null)
										{
											if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
												packageFlag = CommonUtility.validateNumber(doc.getFieldValue("packageFlag").toString());
										}

										if(doc.getFieldValue("packageQty")!=null)
										{
											if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
											{
												packageQty = CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString());
											}

										}

										if(doc.getFieldValue("packDesc")!=null)
										{
											String packDesc = doc.getFieldValue("packDesc").toString();
											iList.setPackDesc(packDesc);
										}

										if(doc.getFieldValue("displayPricing") != null){

											iList.setDisplayPricing(doc.getFieldValue("displayPricing").toString());
										}
										if(doc.getFieldValue("imap")!=null){
											imap = (doc.getFieldValue("imap").toString());
										}
										if(CommonUtility.validateString(imap).length()>0 && imap.toUpperCase().contains("Y") && doc.getFieldValue("imapPrice")!=null){
											imapPrice = (Float) (doc.getFieldValue("imapPrice"));
										}
										if(doc.getFieldValue("clearanceFlag") != null){
											
											iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
										}

										iList.setPackageFlag(packageFlag);
										iList.setPackageQty(packageQty);
										iList.setSaleQty(saleQty);
										iList.setMinOrderQty(minOrdQty);
										iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
										iList.setCustomerPartNumberList(customerPartnumber.get(itemId));
										iList.setImapPrice(imapPrice);
										iList.setIsImap(imap);

									}
								}
							}
							searchResultList.put("itemLevelFilterData", itemLevelFilterData);
					 }
				 }
				 ProductsModel searchResultData = ProductHunterSolrUltimate.solrNavigationSearchResult("*:*", solrURL+"/mainitemdata", fqNav.split("~"), fromRow, toRow, "", faucetField, "", sortField, sortOrder, true, "catSearchId:"+treeId,null,fq,null,null,false);
				 ArrayList<ProductsModel> itemLevelFilterData = searchResultData.getItemDataList();
				 if(itemLevelFilterData.size()>0){
					 ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
					 searchResultList.put("attrList", attrFilteredList );
				 }
			}
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
			ConnectionManager.closeSolrClientConnection(server1);
		}
		return searchResultList;
	}
public static ArrayList<ProductsModel> breadCrumbList(int subsetId,int generalSubset,int treeId)
{
	ArrayList<ProductsModel> taxonomyListVal = new ArrayList<ProductsModel>();
	HttpSolrServer server = null;
	try
	{
		 String indexType = "PH_SEARCH_"+subsetId;
		 if(generalSubset>0 && generalSubset!=subsetId)
			 indexType = "PH_SEARCH_"+generalSubset+"_"+subsetId;
		server = ConnectionManager.getSolrClientConnection(solrURL+"/catalogdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
		String	fq ;
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
			fq="type:PH_SEARCH_ALL~categoryID:"+treeId;
		}
		else {
			fq="type:"+indexType+"~"+"categoryID:"+treeId;
		}		
	
		int resultCount = 0;
		query.setQuery("*:*");
		//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
		String attrFtr[] = fq.split("~");
		query.setStart(0);
		query.setRows(100);
		query.setFacet(false);
		query.setFilterQueries(attrFtr);
		
		QueryResponse response = server.query(query);
		
		System.out.println("bread crumb query : "+query);
		
		SolrDocumentList documents = response.getResults();
		resultCount = (int) response.getResults().getNumFound();
		List<FacetField> facetFeild = response.getFacetFields();
		Iterator<SolrDocument> itr = documents.iterator();
		String toSplit = "";
		while (itr.hasNext()) {
			
			
			SolrDocument doc = itr.next();
			toSplit = doc.getFieldValue("breadCrumb").toString();
		}
		
		if(toSplit!=null && !toSplit.trim().equalsIgnoreCase(""))
		{
			toSplit = toSplit.replaceAll("}", "");
			toSplit = toSplit.replaceAll("  ", " ");
			System.out.println(toSplit);
			String splitVal[] = toSplit.split(">");
			
			
			int levelNum = 1;
			for(String ss:splitVal)
			{
				ProductsModel nameCode= new ProductsModel();
				String facetFilterplit[] = ss.split("\\{");
				if(facetFilterplit.length>1)
				{
					 nameCode.setCategoryCode(facetFilterplit[1].trim());
					  nameCode.setCategoryName(facetFilterplit[0].trim());
					  String itemUrl = nameCode.getCategoryName();
		            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					  String pattern = "[^A-Za-z0-9]";
						 itemUrl = itemUrl.replaceAll(pattern," ");
						 itemUrl = itemUrl.replaceAll("\\s+","-");
		            	nameCode.setItemUrl(itemUrl);
				}
				else
				{
					 nameCode.setCategoryCode("0");
					  nameCode.setCategoryName(facetFilterplit[0].trim());
					  String itemUrl = nameCode.getCategoryName();
					  String pattern = "[^A-Za-z0-9]";
						 itemUrl = itemUrl.replaceAll(pattern," ");
						 itemUrl = itemUrl.replaceAll("\\s+","-");
		            	nameCode.setItemUrl(itemUrl);
				}
				 
				  nameCode.setLevelNumber(levelNum);
				  taxonomyListVal.add(nameCode);
				  levelNum++;
				System.out.println(ss.trim());
			}
		}
		
		
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return taxonomyListVal;
}

public static ProductsModel searchCategory(String keyWord,int subsetId,int generalSubset)
{
	ProductsModel searchCatResult = new ProductsModel();
	try
	{
		 String indexType = "PH_SEARCH_"+subsetId;
		 if(generalSubset>0 && generalSubset!=subsetId)
			 indexType = "PH_SEARCH_"+generalSubset+"_"+subsetId;
		 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
			 indexType="PH_SEARCH_ALL";
			}
		String facetConUrl = solrURL+"/catalogdata";
		 String facetQueryString = "categorysearch:\""+escapeQueryCulpritsWithoutWhiteSpace(keyWord.toLowerCase())+"\"";
		 
		 searchCatResult =  ProductHunterSolrUltimate.checkCategoryExist(facetConUrl, facetQueryString,indexType);
		 
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return searchCatResult;
	
}

public static ArrayList<ProductsModel> SearchStaticContent(String keyWord,String searchType,int fromRow,int noOfPage)
{
	 ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
	 try
	 {
		 if(keyWord!=null && !keyWord.trim().equalsIgnoreCase(""))
		 {
			 keyWord = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
			 String  queryString  = "keywords:(\""+keyWord.toUpperCase()+"\")";
			 String connUrl = solrURL+"/staticcontent";
			 if(searchType!=null && searchType.trim().equalsIgnoreCase("Event"))
			 {
				 connUrl = solrURL+"/evendatamanager";
			 }
			 if(itemLevelFilterData.size()<1)
			 {
				 itemLevelFilterData = ProductHunterSolrUltimate.staticContentSearch(connUrl, queryString, keyWord,fromRow,noOfPage);
			 }
			 
				if(itemLevelFilterData.size()<1)
				 {

					 queryString  = "keywords:(*"+keyWord.toUpperCase()+"*)";
					
					 itemLevelFilterData = ProductHunterSolrUltimate.staticContentSearch(connUrl, queryString, keyWord,fromRow,noOfPage);
					
				 }

				if(itemLevelFilterData.size()<1)
				 {
					 queryString  = "keywords:ASDF (\""+keyWord.toUpperCase()+"\")";
					 itemLevelFilterData = ProductHunterSolrUltimate.staticContentSearch(connUrl, queryString, keyWord,fromRow,noOfPage);
				 }
				
				if(itemLevelFilterData.size()<1)
				 {
					 queryString  = "keywords:ASDF ("+keyWord.toUpperCase()+")";
					 itemLevelFilterData = ProductHunterSolrUltimate.staticContentSearch(connUrl, queryString, keyWord,fromRow,noOfPage);
				 }
		 }
		 
	 }
	 catch(Exception e)
	 {
		 e.printStackTrace();
	 }
	 return itemLevelFilterData;
}



public static ArrayList<ProductsModel> searchBlogContent(String keyWord,String searchType,int fromRow,int noOfPage)
{
	HttpSolrServer server = null;
	ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
	 try{
		 
		 ProductsModel solrSearchVal = new ProductsModel();
		 if(keyWord!=null && !keyWord.trim().equalsIgnoreCase("")){
			 keyWord = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
			 String  queryString  = keyWord;
			 String connUrl = solrURL+"/blog";
			 
			  server = ConnectionManager.getSolrClientConnection(connUrl);
				SolrQuery query = new SolrQuery();
					query.setQuery(queryString);
					query.set("spellcheck", "true");
					 query.set("spellcheck.extendedResults", "true");
					 query.set("spellcheck.collate", "true");
					 query.set("spellcheck.q", keyWord);
					 query.setHighlight(true);
					 query.set("hl.fl", "content");
					 query.set("hl.simple.pre", "<strong>");
					 query.set("hl.simple.post", "</strong>");
					 query.setHighlightFragsize(250);
					//fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
					
					query.setStart(fromRow);
					query.setRows(noOfPage);
					
					System.out.println("query : " + query);
					QueryResponse response = server.query(query);
					
					SolrDocumentList documents = response.getResults();
					int resultCount = (int) response.getResults().getNumFound();
					

					
					SpellCheckResponse spellCheckRes = response.getSpellCheckResponse();
					if(spellCheckRes!=null){
						System.out.println(spellCheckRes.isCorrectlySpelled());
						solrSearchVal.setSuggestedValue(spellCheckRes.getCollatedResult());
						System.out.println("DOCUMENTS Suggestion : " + spellCheckRes.getCollatedResult());
						
						if(!spellCheckRes.getSuggestions().isEmpty()){
							Iterator<Suggestion> suggestionArray = spellCheckRes.getSuggestions().iterator();
							if(suggestionArray!=null){
								List<String> suggestionList = suggestionArray.next().getAlternatives();
								solrSearchVal.setSuggestedValueList(new ArrayList<String>(suggestionList));
							}
							
						}
					}
					Iterator<SolrDocument> itr = documents.iterator();
					
					while (itr.hasNext()) {
						ProductsModel pageModel = new ProductsModel();
						SolrDocument doc = itr.next();
						String id = doc.getFieldValue("id").toString();
						pageModel.setStaticPageId(id);
						if(doc.getFieldValue("title")!=null)
						{
							
							pageModel.setStaticPageTitle(doc.getFieldValue("title").toString());
							pageModel.setItemUrl(doc.getFieldValue("link").toString());
						}
						if(doc.getFieldValue("title")!=null){
							pageModel.setPageName(doc.getFieldValue("title").toString());
						}
						if(doc.getFieldValue("content")!=null)
						{
							pageModel.setStaticContent(doc.getFieldValue("content").toString());
						}
						pageModel.setResultCount(resultCount);
						itemLevelFilterData.add(pageModel);
						
						if (response.getHighlighting().get(id) != null) {
							
							Map<String, List<String>> highlightList = response.getHighlighting().get(id);
							if(highlightList.get("title")!=null)
							{
								if(highlightList.size()>0)
								{
									List<String> highlightValList = highlightList.get("content");
									if(highlightValList.get(0)!=null)
									pageModel.setStaticContent(highlightValList.get(0));
								}
							}
				      }
					}
		 
		 }
		 
	 }
	 catch(Exception e)
	 {
		 e.printStackTrace();
	 }finally {
			ConnectionManager.closeSolrClientConnection(server);
	 }
	 return itemLevelFilterData;
}
public static ProductsModel generateMainItemObject(int itemId, ProductsModel itemModel) {
	HttpSolrServer server = null;
	try {
	
		
		SolrQuery query = new SolrQuery();
			query = new SolrQuery();
			query.setQuery("itemid:"+itemId);
			query.addFilterQuery("defaultCategory:\"Y\"");	
			System.out.println("Navigation query : " + query);
			String pattern = "[^A-Za-z0-9]";
			server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
			QueryResponse response = server.query(query,METHOD.POST);
			SolrDocumentList documents = response.getResults();
			Iterator<SolrDocument> itr = documents.iterator();
			while (itr.hasNext()) {
				try{
					SolrDocument doc = itr.next();
					Map<String, Object> customFieldValMap = doc.getFieldValueMap();
					if(doc.getFieldValue("orderQtyIntervalByShipMethod")!=null && doc.getFieldValue("orderQtyIntervalByShipMethod").toString().trim().length()>0){
						LinkedHashMap<String, ProductsModel>shipOrderQtyAndInterval =  getOrderQtyIntervalByShipMethod(doc.getFieldValue("orderQtyIntervalByShipMethod").toString());
						itemModel.setShipOrderQtyAndIntervalFieldVal(shipOrderQtyAndInterval);
					}
					LinkedHashMap<String, Object> customFieldVal = getAllCustomFieldVal(customFieldValMap);
					if(customFieldVal!=null && customFieldVal.size()>0){
						itemModel.setCustomFieldVal(customFieldVal);
					}
					itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
					itemModel.setMinOrderQty(CommonUtility.validateInteger((double)(itemModel.getMinOrderQty() > 0?itemModel.getMinOrderQty():1)));
					itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
					itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
					itemModel.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
					itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
					itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
					itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
					itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
					itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
					itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
					itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
					itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
					itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
					itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
					itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
					itemModel.setClearance((doc.getFieldValue("clearanceFlag")!=null?doc.getFieldValue("clearanceFlag").toString():""));
					if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB").trim().equalsIgnoreCase("Y")){
						if(doc.getFieldValue("catSearchId")!=null && doc.getFieldValue("categoryNamePath")!=null){
							itemModel.setItemBreadCrumb(ProductHunterSolrUltimate.getItemBreadCrumb((List<Integer>) doc.getFieldValue("catSearchId"),(String) doc.getFieldValue("categoryNamePath")));
						}
					}
					if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y")){
						if(doc.getFieldValue("productItemCount")!=null){
							itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
						}
						if(doc.getFieldValue("productId")!=null){
							itemModel.setProductId(CommonUtility.validateNumber(doc.getFieldValue("productId").toString()));
						}
					}
					itemModel.setCategoryCode((doc.getFieldValue("categoryID")!=null?doc.getFieldValue("categoryID").toString():""));
					itemModel.setProductId((doc.getFieldValue("productId")!=null?CommonUtility.validateNumber(doc.getFieldValue("productId").toString()):0));
					itemModel.setProductName((doc.getFieldValue("productName")!=null?doc.getFieldValue("productName").toString():""));
					itemModel.setProductDescription((doc.getFieldValue("productDesc")!=null?doc.getFieldValue("productDesc").toString():""));
					itemModel.setProductCategoryImageName((doc.getFieldValue("productImageName")!=null?doc.getFieldValue("productImageName").toString():""));
					itemModel.setProductCategoryImageType((doc.getFieldValue("productImageType")!=null?doc.getFieldValue("productImageType").toString():""));
					
					String imageName = null;
					String imageType = null;
					String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
					if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
						itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
					}
					//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					itemUrl = itemUrl.replaceAll(pattern," ");
					itemUrl = itemUrl.replaceAll("\\s+","-");
					itemModel.setItemUrl(itemUrl);
					if(doc.getFieldValue("imageName")!=null){
						imageName = doc.getFieldValue("imageName").toString();
					}
					if(doc.getFieldValue("imageType")!=null){
						imageType = doc.getFieldValue("imageType").toString();
					}
					if(doc.getFieldValue("upc")!=null){
						itemModel.setUpc(doc.getFieldValue("upc").toString());	
					}
					if(doc.getFieldValue("custom_Catalog_ID")!=null){
						itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());
					}
					
					itemModel.setItemType(CommonUtility.validateString(getSolrFieldValue(doc,"itemType")));
					itemModel.setRentals(CommonUtility.validateString(getSolrFieldValue(doc,"rentals")));
					if(imageName==null){
						imageName = "NoImage.png";
						imageType = "IMAGE";
					}
					itemModel.setImageName(imageName.trim());
					itemModel.setImageType(imageType);
					
				}catch (Exception e) {
					System.out.println("Error Occured While retriving data from solr.");
					e.printStackTrace();
				}	

			}
	}catch (Exception e) {
		e.printStackTrace();// TODO: handle exception
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}

	return itemModel;
}


public static HashMap<String, ArrayList<ProductsModel>> searchNavigation(String keyWord,int subsetId,int generalSubset,int fromRow,int toRow,String requestType,int psid,int npsid,int treeId,int levelNo,String attrFilterList,String brandId, String sessionId,String sortBy, int resultPerPage, String narrowKeyword, int buyingCompanyId,String userName,String userToken,String entityId,int userId,String homeTeritory,String type, String wareHousecode,String customerId,String customerCountry,boolean isCategoryNav,boolean exactSearch,String viewFrequentlyPurcahsedOnly, String clearanceFlag, String wareHouseItems)
{

	//keyWord = keyWord.replaceAll("_","");
	boolean isPromotion=false;
	boolean isExcludeItems=false;
	String elevateIds=null;
	String excludeItems=null;
	BannerEntity bannerEntity=null;
	
	HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
	int resultCount = 0;
	String appendNarrowSearch = "";
	List<RangeFacet> facetRanges = null;
	String enableMultifilter = "N";
	String changeRequestHandler = (String)CommonDBQuery.getSystemParamtersList().get("CHANGE_REQUEST_HANDLER");
	HttpSolrServer server = null;
	HttpSolrServer server1 = null;
	try
	{
		if(CommonDBQuery.getSystemParamtersList().get("ENABLE_MULTIFILTER")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_MULTIFILTER").trim().equalsIgnoreCase("Y"))
		{
			enableMultifilter = "Y";
		}
	
		ArrayList<String> defaultFacetFilterList = CommonDBQuery.getDefaultFacet();
		if(keyWord!=null){
			//keyWord = ClientUtils.escapeQueryChars(keyWord.trim()); //Coz of / on every special characters search for multi keyword wasnt working in most of the cases
			keyWord = CommonUtility.validateString(keyWord);
		}else{
			keyWord = "";
		}
		if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase("") && !CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y"))
		{
			appendNarrowSearch = narrowKeyword;
			narrowKeyword = null;
		}
		String origKeyWord = keyWord.toUpperCase();
		String spellCheckKeyword = new String(keyWord);

	

		if(type==null)
			type = "";
		boolean massEnquiry = false;
		String suggestedValue = null;
		ArrayList<String> suggestedValueList = null;
		String indexType = "PH_SEARCH_"+subsetId;
		if(generalSubset>0 && generalSubset!=subsetId)
			indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
		String idList = "";
		String c = "";
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SINGLETON_SOLR")).equalsIgnoreCase("Y")){
			 server = MainItem.getInstance().getConnection();
		}else {
			server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
		}
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
		ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
		ArrayList<ProductsModel> itemLevelFilterDataSug = new ArrayList<ProductsModel>();

		String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
		if(!siteNameSolr.equalsIgnoreCase(""))
		{
			fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
		}

		if(viewFrequentlyPurcahsedOnly.equalsIgnoreCase("Y"))
		 {	 
			 fqNav = fqNav + "~frequentlyPurchasedUser:"+userId;
		 }
		/**
		 *Below code Written is for Tyndale *Reference- Chetan Sandesh
		 */
		if(clearanceFlag.equalsIgnoreCase("Y"))
		 {	if(!siteNameSolr.equalsIgnoreCase("")) 
			{
				fqNav = fqNav + "~{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}clearanceFlag:Y";
			}else{
				fqNav = fqNav + "~{!join from=itemid to=itemid fromIndex=itempricedata}clearanceFlag:Y";
			}
		 }
		if(CommonUtility.validateString(wareHouseItems).length()>0)
		 {	 
			if(!siteNameSolr.equalsIgnoreCase(""))
			{
				 fqNav = fqNav + "~{!join from=partnumber to=partnumber fromIndex="+siteNameSolr+"_inventory method=crossCollection}(warehouseCode:"+wareHouseItems+")";
			}else {
				 fqNav = fqNav + "~{!join from=partnumber to=partnumber fromIndex=inventory}(warehouseCode:"+wareHouseItems+")";
			}
		 }
		String rebuildAttrNav = "";
		String aFilterNav = "";
		String attrFtrNav[] = null;
		String categoryName = null;
		boolean isGetAttribute = false;
		String faucetField[] = null;
		ArrayList<ProductsModel> refinedList = new ArrayList<ProductsModel>();
		boolean isPriceRange = false;
		boolean isBrand = false;
		boolean isSubCategory = false;



		String sortField = "partnumber";
		ORDER sortOrder = SolrQuery.ORDER.asc;

		sortField = "externalHitSort";
		sortOrder = SolrQuery.ORDER.desc;

		if (requestType.trim().equalsIgnoreCase("SEARCH") && sortBy == null ) {
			sortField = "default";
			sortBy = "default"; //On Dec 4th 2014 Aramsco and others 
			//sortField =  "externalHitSort";
			sortOrder = SolrQuery.ORDER.desc;
		}

		if(CommonUtility.validateString(sortBy).trim().length()==0){
			sortBy = "externalHitSort_desc";
		}
		if(sortBy!=null && !sortBy.trim().equalsIgnoreCase(""))
		{

			if(sortBy.trim().equalsIgnoreCase("price_high"))
			{
				sortField = "NET_PRICE";
				sortOrder = SolrQuery.ORDER.desc;
			}
			else if(sortBy.trim().equalsIgnoreCase("price_low"))
			{
				sortField = "NET_PRICE";
				sortOrder = SolrQuery.ORDER.asc;
			}
			else if(sortBy.trim().equalsIgnoreCase("erp_price_low"))
			{
				sortField = "price";
				sortOrder = SolrQuery.ORDER.asc;
			}
			else if(sortBy.trim().equalsIgnoreCase("erp_price_high"))
			{
				sortField = "price";
				sortOrder = SolrQuery.ORDER.desc;
			}

			else if(sortBy.trim().equalsIgnoreCase("brand"))
			{
				sortField = "brand";
				sortOrder = SolrQuery.ORDER.asc;
			}
			else if(sortBy.trim().equalsIgnoreCase("brand_asc"))
			{
				sortField = "brand";
				sortOrder = SolrQuery.ORDER.asc;
			}
			else if(sortBy.trim().equalsIgnoreCase("brand_desc"))
			{
				sortField = "brand";
				sortOrder = SolrQuery.ORDER.desc;
			}
			else if(sortBy.trim().equalsIgnoreCase("partnum_asc"))
			{
				sortField = "partnumber";
				sortOrder = SolrQuery.ORDER.asc;
			}
			else if(sortBy.trim().equalsIgnoreCase("partnum_desc"))
			{
				sortField = "partnumber";
				sortOrder = SolrQuery.ORDER.desc;
			}else if(sortBy.trim().equalsIgnoreCase("externalHitSort_desc")){

				sortField = "externalHitSort";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("externalHitSort_asc")){

				sortField = "externalHitSort";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("bestSellerSort_desc")){

				sortField = "bestSellerSort";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("bestSellerSort_asc")){

				sortField = "bestSellerSort";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("internalHitSort_desc")){

				sortField = "internalHitSort";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("internalHitSort_asc")){

				sortField = "internalHitSort";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("MPN_desc")){

				sortField = "manfpartnumber";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("MPN_asc")){

				sortField = "manfpartnumber";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("upc_desc")){

				sortField = "upc";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("upc_asc")){

				sortField = "upc";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("desc_desc")){

				sortField = "description_sort";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("desc_asc")){

				sortField = "description_sort";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("manufacturer_asc")){

				sortField = "manufacturerName";
				sortOrder = SolrQuery.ORDER.asc;

			}else if(sortBy.trim().equalsIgnoreCase("manufacturer_desc")){

				sortField = "manufacturerName";
				sortOrder = SolrQuery.ORDER.desc;

			}else if(sortBy.trim().equalsIgnoreCase("popularity_desc")){
				sortField = "popularity";
				sortOrder = SolrQuery.ORDER.desc;
			}else if(sortBy.trim().equalsIgnoreCase("elevate")){
				sortField = "elevate";
				sortOrder = SolrQuery.ORDER.desc;
			}else if(sortBy.trim().equalsIgnoreCase("manufacturerDisplayNameSort_asc")){
				sortField = "manufacturerDisplayNameSort";
				sortOrder = SolrQuery.ORDER.asc;
		    }else if(sortBy.trim().equalsIgnoreCase("manufacturerDisplayNameSort_desc")){
				sortField = "manufacturerDisplayNameSort";
				sortOrder = SolrQuery.ORDER.desc;
		    }else if (sortBy.trim().equalsIgnoreCase("altpartnum_asc")){                                                       
		        sortField = "alternatePartNumber1";
		        sortOrder = SolrQuery.ORDER.asc;                 
		    }else if (sortBy.trim().equalsIgnoreCase("altpartnum_desc")){                      
		          sortField = "alternatePartNumber1";                          
		          sortOrder = SolrQuery.ORDER.desc;                              
		    }else if(sortBy.trim().equalsIgnoreCase("availability_high")){ 
		          sortField = "qtyAvailable";
		          sortOrder = SolrQuery.ORDER.desc;
		    }else if(sortBy.trim().equalsIgnoreCase("availability_low")){
		        sortField = "qtyAvailable";
		        sortOrder = SolrQuery.ORDER.asc;
		    }else{
				if(CommonUtility.validateString(sortBy).contains("|")){
					String[] customSort=sortBy.split("\\|");
					if(CommonUtility.validateString(customSort[0]).length()>0){
						sortField = customSort[0];
					}
					if(CommonUtility.validateString(customSort[1]).equalsIgnoreCase("asc")){
						sortOrder = SolrQuery.ORDER.asc;
					}else if(CommonUtility.validateString(customSort[1]).equalsIgnoreCase("desc")){
						sortOrder = SolrQuery.ORDER.desc;
					}
				}
			}

		}
		ArrayList<String> removeAttributeList = new ArrayList<String>();
		String attrFtrGlobal[] = null;
		LinkedHashMap<String, ArrayList<String>> filteredMultList = new LinkedHashMap<String, ArrayList<String>>();
		ArrayList<String> valueList = new ArrayList<String>();	
		if(attrFilterList!=null && !attrFilterList.trim().equalsIgnoreCase(""))
		{
			attrFilterList = attrFilterList.replace("attr_brand", "brand");
			attrFilterList = attrFilterList.replace("attr_category", "category");
			attrFilterList = attrFilterList.replace("attr_manufacturerName", "manufacturerName");
			attrFilterList = attrFilterList.replace("attr_level1Category", "level1Category");
			attrFilterList = attrFilterList.replace("attr_price", "price");
			String tempArr[] = attrFilterList.split("~");
			c = "";
			for(String sArr:tempArr)
			{
				boolean addRefinedList = true;
				String attrArray[]=sArr.split(":");
				ProductsModel filterListVal = new ProductsModel();
				if(enableMultifilter.trim().equalsIgnoreCase("N"))
				{
					if(defaultFacetFilterList!=null && defaultFacetFilterList.size()>0)
					{
						if(defaultFacetFilterList.contains(attrArray[0].trim()))
						{
							removeAttributeList.add(attrArray[0].trim());
							addRefinedList = false;
						}

					}

				}

				filterListVal.setAttrName(attrArray[0]);
				filterListVal.setAttrNameEncoded(URLEncoder.encode(attrArray[0],"UTF-8"));
				String arFilterVal = "";
				String arFilterDel = "";
				if(attrArray!=null && attrArray.length>2)
				{
					int arIndex = 0;

					for(String arAppend:attrArray)
					{
						if(arIndex>0)
						{
							System.out.println("Split length > 2 : Appending - " + arFilterVal +" with "+arAppend);
							arFilterVal = arFilterVal + arFilterDel + arAppend;
							arFilterDel = ":";


						}
						arIndex++;
					}
					filterListVal.setAttrValue(arFilterVal);
				}else if(attrArray!=null && attrArray.length<=1){
					arFilterVal = attrArray[0];
				}else{
					arFilterVal = attrArray[1];
				}
				filterListVal.setAttrValue(arFilterVal);
				filterListVal.setAttrValueEncoded(URLEncoder.encode(arFilterVal,"UTF-8"));
				//if(addRefinedList)
				refinedList.add(filterListVal);
				if(attrArray[0].trim().equalsIgnoreCase("price"))
				{
					isPriceRange = true;
				}
				if(attrArray[0].trim().equalsIgnoreCase("category"))
				{
					categoryName = attrArray[1];
					categoryName = categoryName.replace("|", " OR ");
					//isGetAttribute = true;
				}
				valueList = new ArrayList<String>();
				String buildFilterKey = "";
				String appendFilter[] = arFilterVal.split("\\|");
				if(appendFilter!=null && appendFilter.length>0)
				{
					String appendOr = "";
					for(String append:appendFilter)
					{
						String replaceFilterVal = append;
						if(replaceFilterVal!=null)
						{
							replaceFilterVal = replaceFilterVal.substring(1, replaceFilterVal.length()-1);
							valueList.add(replaceFilterVal);
							if(!attrArray[0].trim().equalsIgnoreCase("price"))
							{
								replaceFilterVal = escapeQueryCulpritsWithoutWhiteSpace(replaceFilterVal);
								replaceFilterVal = "\""+replaceFilterVal+"\"";
							}


							buildFilterKey = buildFilterKey + appendOr + replaceFilterVal;
							appendOr = " OR ";
						}

					}

				}
				filteredMultList.put(attrArray[0], valueList);

				rebuildAttrNav = rebuildAttrNav+c+escapeQueryCulprits(attrArray[0])+":("+buildFilterKey+")";
				c = "~";
			}
			attrFtrGlobal= rebuildAttrNav.split("~");
			aFilterNav = fqNav + "~"+rebuildAttrNav;
			attrFtrNav = aFilterNav.split("~");
		}else{
			aFilterNav = fqNav;
			attrFtrNav = aFilterNav.split("~");
		}

		String fqB = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
		if(!siteNameSolr.equalsIgnoreCase(""))
		{
			fqB = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
		}

		if(viewFrequentlyPurcahsedOnly.equalsIgnoreCase("Y"))
		 {	 
			 fqB= fqB + "~frequentlyPurchasedUser:"+userId;
		 }
		/**
		 *Below code Written is for Tyndale *Reference- Chetan Sandesh
		 */
		if(clearanceFlag.equalsIgnoreCase("Y"))
		 {	
			if(!siteNameSolr.equalsIgnoreCase(""))
			{
				fqB = fqB + "~{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}clearanceFlag:Y";
			}else {
				fqB = fqB + "~{!join from=itemid to=itemid fromIndex=itempricedata}clearanceFlag:Y";
			}
		 }
		if(CommonUtility.validateString(wareHouseItems).length()>0) 
		{
			if(!siteNameSolr.equalsIgnoreCase(""))
			{
				fqB = fqB +"~{!join from=partnumber to=partnumber fromIndex="+siteNameSolr+"_inventory method=crossCollection}(warehouseCode:"+wareHouseItems+")";
			}else {
				fqB = fqB +"~{!join from=partnumber to=partnumber fromIndex=inventory}(warehouseCode:"+wareHouseItems+")";
			}
		}
		
		String rebuildAttrB = "";
		String aFilterB = "";
		String sFq = "defaultCategory:Y";
		String attrFtrB[] = null;


		c = "";
		if(attrFilterList!=null && !attrFilterList.trim().equalsIgnoreCase("")){
			attrFilterList = attrFilterList.replace("attr_brand", "brand");
			attrFilterList = attrFilterList.replace("attr_category", "category");
			attrFilterList = attrFilterList.replace("attr_manufacturerName", "manufacturerName");
			String tempArr[] = attrFilterList.split("~");
			c = "";
			for(String sArr:tempArr){
				String attrArray[]=sArr.split(":");
				String arFilterVal = "";
				String arFilterDel = "";
				if(attrArray!=null && attrArray.length>2){
					int arIndex = 0;
					for(String arAppend:attrArray){
						if(arIndex>0){
							System.out.println("Split length > 2 : Appending - " + arFilterVal +" with "+arAppend);
							arFilterVal = arFilterVal + arFilterDel + arAppend;
							arFilterDel = ":";
						}
						arIndex++;
					}
				}else{
					arFilterVal = attrArray[1];
				}
				String buildFilterKey = "";
				String appendFilter[] = arFilterVal.split("\\|");
				if(appendFilter!=null && appendFilter.length>0)
				{
					String appendOr = "";
					for(String append:appendFilter)
					{
						String replaceFilterVal = append;
						if(replaceFilterVal!=null)
						{
							replaceFilterVal = replaceFilterVal.substring(1, replaceFilterVal.length()-1);
							if(!attrArray[0].trim().equalsIgnoreCase("price"))
							{
								replaceFilterVal = escapeQueryCulpritsWithoutWhiteSpace(replaceFilterVal);
								replaceFilterVal = "\""+replaceFilterVal+"\"";
							}

							buildFilterKey = buildFilterKey + appendOr + replaceFilterVal;
							appendOr = " OR ";
						}

					}

				}


				rebuildAttrB = rebuildAttrB+c+escapeQueryCulprits(attrArray[0])+":("+buildFilterKey+")";
				c = "~";
			}
			attrFtrGlobal= rebuildAttrB.split("~");
			aFilterB = sFq + "~" +fqB + "~"+rebuildAttrB;
			attrFtrB = aFilterB.split("~");
		}else{
			aFilterB = sFq + "~" +fqB;
			attrFtrB = aFilterB.split("~");
		}
		String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
		if(!siteNameSolr.equalsIgnoreCase(""))
		{
			fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
		}
		 if(viewFrequentlyPurcahsedOnly.equalsIgnoreCase("Y"))
		 {
			 fq = fq + "~frequentlyPurchasedUser:"+userId;
		 }
			/**
			 *Below code Written is for Tyndale *Reference- Chetan Sandesh
			 */
		 if(clearanceFlag.equalsIgnoreCase("Y"))
		 {	 
			 if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fq = fq + "~{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}clearanceFlag:Y";
			 }else {
				 fq = fq + "~{!join from=itemid to=itemid fromIndex=itempricedata}clearanceFlag:Y";
			 }
		 }
		 if(CommonUtility.validateString(wareHouseItems).length()>0)
		 {
			if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fq = fq +"~{!join from=partnumber to=partnumber fromIndex="+siteNameSolr+"_inventory method=crossCollection}(warehouseCode:"+wareHouseItems+")";
			 }else{
				 fq = fq +"~{!join from=partnumber to=partnumber fromIndex=inventory}(warehouseCode:"+wareHouseItems+")";
			 }
		}
		String rebuildAttr = "";
		String aFilter = "";
		String attrFtr[] = null;
		c = "";
		if(attrFilterList!=null &&  !attrFilterList.trim().equalsIgnoreCase(""))
		{
			attrFilterList = attrFilterList.replace("attr_brand", "brand");
			attrFilterList = attrFilterList.replace("attr_category", "category");
			attrFilterList = attrFilterList.replace("attr_manufacturerName", "manufacturerName");
			String tempArr[] = attrFilterList.split("~");
			c = "";
			for(String sArr:tempArr)
			{
				String attrArray[]=sArr.split(":");
				String arFilterVal = "";
				String arFilterDel = "";
				if(attrArray!=null && attrArray.length>2)
				{
					int arIndex = 0;

					for(String arAppend:attrArray)
					{
						if(arIndex>0)
						{
							System.out.println("Split length > 2 : Appending - " + arFilterVal +" with "+arAppend);
							arFilterVal = arFilterVal + arFilterDel + arAppend;
							arFilterDel = ":";


						}
						arIndex++;
					}

				}
				else
					arFilterVal = attrArray[1];

				String buildFilterKey = "";
				String appendFilter[] = arFilterVal.split("\\|");
				if(appendFilter!=null && appendFilter.length>0)
				{
					String appendOr = "";
					for(String append:appendFilter)
					{
						String replaceFilterVal = append;
						if(replaceFilterVal!=null)
						{
							replaceFilterVal = replaceFilterVal.substring(1, replaceFilterVal.length()-1);
							if(!attrArray[0].trim().equalsIgnoreCase("price"))
							{
								replaceFilterVal = escapeQueryCulpritsWithoutWhiteSpace(replaceFilterVal);
								replaceFilterVal = "\""+replaceFilterVal+"\"";
							}

							buildFilterKey = buildFilterKey + appendOr + replaceFilterVal;
							appendOr = " OR ";
						}


					}

				}


				rebuildAttr = rebuildAttr+c+escapeQueryCulprits(attrArray[0])+":("+buildFilterKey+")";
				c = "~";
			}
			attrFtrGlobal= rebuildAttr.split("~");
			aFilter = sFq + "~" +fq + "~"+rebuildAttr;
			attrFtr = aFilter.split("~");
		}
		else
		{
			aFilter = sFq + "~" +fq;
			attrFtr = aFilter.split("~");
		}

		String fq1 = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
		if(!siteNameSolr.equalsIgnoreCase(""))
		{
			fq1 = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
		}
		if(viewFrequentlyPurcahsedOnly.equalsIgnoreCase("Y"))
		 {
			 fq1 = fq1 + "~frequentlyPurchasedUser:"+userId;
		 }
		/**
		 *Below code Written is for Tyndale *Reference- Chetan Sandesh
		 */
		if(clearanceFlag.equalsIgnoreCase("Y"))
		 {	 
			if(!siteNameSolr.equalsIgnoreCase(""))
			{
				fq1 = fq1 + "~{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}clearanceFlag:Y";
			}else {
				fq1 = fq1 + "~{!join from=itemid to=itemid fromIndex=itempricedata}clearanceFlag:Y";
			}
		 }
		if(CommonUtility.validateString(wareHouseItems).length()>0)
		{
			if(!siteNameSolr.equalsIgnoreCase(""))
			{
				fq1 = fq1 +"~{!join from=partnumber to=partnumber fromIndex="+siteNameSolr+"_inventory method=crossCollection}(warehouseCode:"+wareHouseItems+")";
			}else {
                fq1 = fq1 +"~{!join from=partnumber to=partnumber fromIndex=inventory}(warehouseCode:"+wareHouseItems+")";
			}
		}
		String cFq = "buyingCompanyId:"+buyingCompanyId;

		String rebuildAttr1 = "";
		String aFilter1 = "";
		String attrFtr1[] = null;
		c = "";
		if(attrFilterList!=null &&  !attrFilterList.trim().equalsIgnoreCase(""))
		{


			attrFilterList = attrFilterList.replace("attr_brand", "brand");
			attrFilterList = attrFilterList.replace("attr_category", "category");
			attrFilterList = attrFilterList.replace("attr_manufacturerName", "manufacturerName");
			String tempArr[] = attrFilterList.split("~");
			c = "";
			for(String sArr:tempArr)
			{
				String attrArray[]=sArr.split(":");
				String arFilterVal = "";
				String arFilterDel = "";
				if(attrArray!=null && attrArray.length>2)
				{
					int arIndex = 0;

					for(String arAppend:attrArray)
					{
						if(arIndex>0)
						{
							System.out.println("Split length > 2 : Appending - " + arFilterVal +" with "+arAppend);
							arFilterVal = arFilterVal + arFilterDel + arAppend;
							arFilterDel = ":";


						}
						arIndex++;
					}

				}
				else
					arFilterVal = attrArray[1];

				String buildFilterKey = "";
				String appendFilter[] = arFilterVal.split("\\|");
				if(appendFilter!=null && appendFilter.length>0)
				{
					String appendOr = "";
					for(String append:appendFilter)
					{
						String replaceFilterVal = append;
						if(replaceFilterVal!=null)
						{
							replaceFilterVal = replaceFilterVal.substring(1, replaceFilterVal.length()-1);
							if(!attrArray[0].trim().equalsIgnoreCase("price"))
							{
								replaceFilterVal = escapeQueryCulpritsWithoutWhiteSpace(replaceFilterVal);
								replaceFilterVal = "\""+replaceFilterVal+"\"";
							}

							buildFilterKey = buildFilterKey + appendOr + replaceFilterVal;
							appendOr = " OR ";
						}


					}

				}


				rebuildAttr1 = rebuildAttr1+c+escapeQueryCulprits(attrArray[0])+":("+buildFilterKey+")";
				c = "~";
			}
			attrFtrGlobal= rebuildAttr1.split("~");
			aFilter1 = cFq + "~" +fq1 + "~"+rebuildAttr1;
			attrFtr1 = cFq.split("~");

		}
		else
		{
			aFilter1 = cFq + "~" +fq1;
			attrFtr1 = cFq.split("~");
		}
		//query.setFilterQueries(attrFtr);
		c = "";
		String facetConUrl = solrURL+"/catalogdata";
		String facetQueryString = "category:"+categoryName;
		String facetInnderUrl = solrURL+"/categoryattribute";
		ArrayList<String> finalRemoveList = new ArrayList<String>();

		// finalRemoveList = CommonDBQuery.getDefaultFacet();
		if(removeAttributeList!=null && removeAttributeList.size()>0)
		{
			for(String lList: CommonDBQuery.getDefaultFacet())
			{
				boolean addToList = true;
				for(String rmList:removeAttributeList)
				{
					if(lList!=null && lList.trim().equalsIgnoreCase(rmList))
					{
						addToList = false;
						break;
					}


				}
				if(addToList)
				{
					finalRemoveList.add(lList);
				}
			}


		}
		else
		{
			finalRemoveList = CommonDBQuery.getDefaultFacet();
		}

		if(isCategoryNav)
		{
			faucetField = ProductHunterSolrUltimate.getFaucetFieldForSearch(facetQueryString, facetInnderUrl, treeId,refinedList,isPriceRange,false,isSubCategory,false,false,finalRemoveList,CommonDBQuery.getDefaultFacet());
		}
		else if(treeId>0)
		{
			faucetField = ProductHunterSolrUltimate.getFaucetFieldByName(facetConUrl, categoryName, facetQueryString, facetInnderUrl, treeId,refinedList,isPriceRange,false,isSubCategory,false,false,finalRemoveList,CommonDBQuery.getDefaultFacet());
		}
		else if(isGetAttribute)
		{

			faucetField = ProductHunterSolrUltimate.getFaucetFieldByName(facetConUrl, categoryName, facetQueryString, facetInnderUrl, 0,refinedList,isPriceRange,false,isSubCategory,false,false,finalRemoveList,CommonDBQuery.getDefaultFacet());
		}
		else if(CommonDBQuery.getSystemParamtersList().get("ENABLE_SEARCH_FACET")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_SEARCH_FACET").trim().equalsIgnoreCase("Y"))
		{
			faucetField = ProductHunterSolrUltimate.getFaucetFieldForSearch(facetQueryString, facetInnderUrl, treeId,refinedList,isPriceRange,false,isSubCategory,false,false,finalRemoveList,CommonDBQuery.getDefaultFacet());
		}
		else
		{
			if( CommonDBQuery.getDefaultFacet()!=null &&  CommonDBQuery.getDefaultFacet().size()>0)
			{
				if(removeAttributeList!=null && removeAttributeList.size()>0)
				{


					if(finalRemoveList!=null && finalRemoveList.size()>0)
					{
						faucetField = new String[finalRemoveList.size()];
						faucetField = finalRemoveList.toArray(faucetField);
					}
				}
				else
				{
					faucetField = new String[CommonDBQuery.getDefaultFacet().size()];
					faucetField = CommonDBQuery.getDefaultFacet().toArray(faucetField);
				}

			}

		}

		keyWord = keyWord.replaceAll("\\|", " ");
		keyWord = keyWord.toLowerCase();
		String validateForSearch[] = keyWord.split("\\s+",-1);
		Pattern p = Pattern.compile("[^a-zA-Z0-9]");
		Pattern p1 = Pattern.compile("[\\d]");
		boolean includePartNumberSearch = false;
		String singleKeyword = "";
		String multiKeyword = "";
		String multiKeywordOr = "";
		String multiKeywordWildCard = "";
		String pattern = "[^A-Za-z0-9]";
		
		if(CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y")){
			includePartNumberSearch = true;
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
		}else{
			if(validateForSearch!=null && validateForSearch.length>1)
			{
				String separator = "";
				for(String keyBuilder:validateForSearch)
				{
					boolean hasSpecialChar = p1.matcher(keyBuilder.trim()).find();
					if(keyBuilder.matches("[-+]?\\d*\\.?\\d+"))
					{
						includePartNumberSearch = true;
					}
					else
					{
						if(hasSpecialChar)
						{
							includePartNumberSearch = true;
						}
						else
						{
							hasSpecialChar = p.matcher(keyBuilder.trim()).find();
							if(hasSpecialChar)
							{
								includePartNumberSearch = true;
							}
						}

					}

					if(includePartNumberSearch)
					{
						multiKeyword = multiKeyword + separator + buildKeyword(keyBuilder);
						separator = " AND ";
					}
					includePartNumberSearch = false;
				}

				//multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord) + multiKeyword;
				multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord);

				multiKeywordOr = multiKeyword;
				if(appendNarrowSearch!=null && !appendNarrowSearch.trim().equalsIgnoreCase(""))
				{
					appendNarrowSearch = escapeQueryCulpritsWithoutWhiteSpace(appendNarrowSearch);
					String appendNarrowSearchAnd =  appendNarrowSearch.replaceAll("\\s+", " AND ");
					multiKeywordOr = multiKeywordOr.substring(0, multiKeywordOr.length() - appendNarrowSearch.length());
					multiKeywordOr = "("+multiKeywordOr.trim() +") AND "+appendNarrowSearchAnd;
					narrowKeyword = null;
				}


				/*	if(CommonDBQuery.getSystemParamtersList().get("WILD_CARD_SEARCH")!=null && CommonDBQuery.getSystemParamtersList().get("WILD_CARD_SEARCH").trim().equalsIgnoreCase("Y"))
					{
						multiKeyword = multiKeyword.replaceAll("\\s+", "* AND *");
						multiKeyword = "*"+multiKeyword+"*";
					}
					else
						multiKeyword = multiKeyword.replaceAll("\\s+", " AND ");*/

				multiKeywordWildCard = multiKeyword;
				multiKeyword = multiKeyword.replaceAll("\\s+", " AND ");
				multiKeywordWildCard = "*"+multiKeywordWildCard.replaceAll("\\s+", "* AND *")+"*";
				System.out.println("multiKeyword Query Keyword : " + multiKeyword);
				System.out.println("multiKeyword Query Keyword : " + multiKeywordWildCard);

				System.out.println("multiKeyword Query Keyword : " + multiKeyword);
			}
			else
			{

				includePartNumberSearch = true;
				singleKeyword = "("+buildKeyword(keyWord)+")";
				System.out.println("Single Query Keyword : " + singleKeyword);


			}
		}
		

		if(requestType.trim().equalsIgnoreCase("NAVIGATION"))
		{

			String queryString  = "";
			String connUrl = solrURL+"/mainitemdata";
			String connUrlSub = solrURL+"/mainitemdata";


			ProductsModel searchResultData = new ProductsModel();
			if(!keyWord.trim().equalsIgnoreCase("") && keyWord.length() > 1 && !CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y"))
			{
				if(itemLevelFilterData.size()<1)
				{

					if(includePartNumberSearch)
						queryString  = singleKeyword;
					else
						queryString = multiKeyword;
					connUrl = solrURL+"/mainitemdata";
					connUrlSub = solrURL+"/mainitemdata";
					searchResultData = new ProductsModel();



					searchResultData =  ProductHunterSolrUltimate.solrNavigationSearchResult(queryString, connUrl, attrFtrNav, fromRow, resultPerPage, connUrlSub, faucetField, origKeyWord, sortField, sortOrder, includePartNumberSearch, "catSearchId:"+treeId,attrFtrGlobal,fq,null,filteredMultList,false);
					itemLevelFilterData = searchResultData.getItemDataList();
					if(suggestedValue==null)
						suggestedValue = searchResultData.getSuggestedValue();System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());
						
						if(suggestedValueList==null){
							suggestedValueList = searchResultData.getSuggestedValueList();
						}
						
						if(itemLevelFilterData.size()>0)
						{
							ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
							idList = searchResultData.getIdList();
							searchResultList.put("attrList", attrFilteredList );
							searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
						}

				}

				if (itemLevelFilterData.size() < 1) {

					if (!includePartNumberSearch)
					{
						queryString = multiKeyword;
						connUrl = solrURL+"/mainitemdata";
						connUrlSub = solrURL+ "/mainitemdata";
						searchResultData = new ProductsModel();
						searchResultData =  ProductHunterSolrUltimate.solrNavigationSearchResult(queryString, connUrlSub, attrFtrNav, fromRow, resultPerPage, connUrlSub, faucetField, origKeyWord, sortField, sortOrder, includePartNumberSearch, "catSearchId:"+treeId,attrFtrGlobal,fq,null,filteredMultList,false);
						itemLevelFilterData = searchResultData.getItemDataList();
						if (suggestedValue == null)
							suggestedValue = searchResultData.getSuggestedValue();
						System.out.println("Suggtion Set : " + suggestedValue+ " - " + searchResultData.getSuggestedValue());
						
						if(suggestedValueList==null){
							suggestedValueList = searchResultData.getSuggestedValueList();
						}
						
						if (itemLevelFilterData.size() > 0) {
							ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
							idList = searchResultData.getIdList();
							searchResultList.put("attrList", attrFilteredList);
							searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
						}
					}
				}


				if (itemLevelFilterData.size() < 1) {

					if (!includePartNumberSearch)
					{
						queryString = multiKeywordWildCard;
						connUrl = solrURL+"/mainitemdata";
						connUrlSub = solrURL+ "/mainitemdata";
						searchResultData = new ProductsModel();
						searchResultData =  ProductHunterSolrUltimate.solrNavigationSearchResult(queryString, connUrlSub, attrFtrNav, fromRow, resultPerPage, connUrlSub, faucetField, origKeyWord, sortField, sortOrder, includePartNumberSearch, "catSearchId:"+treeId,attrFtrGlobal,fq,null,filteredMultList,true);
						itemLevelFilterData = searchResultData.getItemDataList();
						if (suggestedValue == null)
							suggestedValue = searchResultData.getSuggestedValue();
						System.out.println("Suggtion Set : " + suggestedValue+ " - " + searchResultData.getSuggestedValue());
						
						if(suggestedValueList==null){
							suggestedValueList = searchResultData.getSuggestedValueList();
						}
						
						if (itemLevelFilterData.size() > 0) {
							ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
							idList = searchResultData.getIdList();
							searchResultList.put("attrList", attrFilteredList);
							searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
						}
					}
				}

			}
			else
			{
				query = new SolrQuery();
				if(isCategoryNav)
					query.setQuery("catSearchId:"+treeId);
				else
					query.setQuery("categoryID:"+treeId);


				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_VERSION")).equalsIgnoreCase("10")){
					query.setRequestHandler("/mainitem_keywordsearch");
				}
				if(CommonUtility.customServiceUtility()!=null)
				{
					query = CommonUtility.customServiceUtility().setRequestHandler(attrFtrGlobal , query);
				}
				//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				if(faucetField!=null && faucetField.length>0)
				{
					query.addFacetField(faucetField);
					query.setFacetLimit(-1);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
						query.setFacetSort(true);
					}else{
						query.setFacetSort(false);
					}
				}

				query.setStart(fromRow);
				query.setRows(resultPerPage);
				query.setFilterQueries(attrFtrNav);
				if(treeId==0){
					query.addFilterQuery("defaultCategory:\"Y\"");						
				}

				if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y") && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_VERSION")).equalsIgnoreCase("10")){
					if(!viewFrequentlyPurcahsedOnly.equalsIgnoreCase("Y")){
						query.addFilterQuery("defaultProductItem:Y");
					}else{
						query.addFilterQuery("defaultProductItem:(Y OR N)");
					}
				}
				query.addSortField("goldenitem", SolrQuery.ORDER.desc );
				query.addSortField(sortField, sortOrder);
				if(keyWord!=null && !keyWord.trim().equalsIgnoreCase("") && CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y")){
					 if(validateForSearch.length>1){
						 query.addFilterQuery("{!edismax}"+keyWord);
					 }else{
						 query.addFilterQuery("{!edismax}*"+keyWord+"* OR " + keyWord);
					 }
				 }
				ProductHunterUtility.getInstance().setRangeFilteQuery(query);

				System.out.println("Navigation query : " + query);

				QueryResponse response = server.query(query,METHOD.POST);
				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				facetRanges = response.getFacetRanges();
				//overriding the new request handler
				if(CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y") && !CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE).equalsIgnoreCase("/mainitem_keywordsearch")) {
					 query.setRequestHandler(CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE));
					 QueryResponse response1 = SolrService.getInstance().executeSolrQuery(ProductHunterSolrV2.solrURL+"/mainitemdata", query);
					 facetFeild = response1.getFacetFields();
				}
				
				ArrayList<ProductsModel> facetRangeArr = ProductHunterUtility.getInstance().setFacetRanges(facetRanges);
				ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("DOCUMENTS");
				LinkedHashMap<String, ArrayList<ProductsModel>> multiFaucetResult = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				if(resultCount>0 && attrFtrGlobal!=null && attrFtrGlobal.length>0)
				{
					if(isCategoryNav){
						multiFaucetResult = ProductHunterSolrUltimate.generateFaucetFilter("catSearchId:"+treeId, attrFtrGlobal, fq, false, false,null,null,filteredMultList);
					}else{
						multiFaucetResult = ProductHunterSolrUltimate.generateFaucetFilter("categoryID:"+treeId, attrFtrGlobal, fq, false, false,null,null,filteredMultList);
					}
				}
				if(multiFaucetResult!=null && multiFaucetResult.get("price")!=null){
					ArrayList<ProductsModel> fRange = multiFaucetResult.get("price");
					if(fRange!=null && fRange.size()>0 && fRange.get(0).getFacetRange()!=null && fRange.get(0).getFacetRange().size()>0){
						facetRangeArr = fRange;
					}
				}
				
				attrFilteredList = ProductHunterSolrUltimate.buildFacetFilter(facetFeild, multiFaucetResult, filteredMultList);
				searchResultList.put("attrList", attrFilteredList);
				searchResultList.put("facetRanges", facetRangeArr);
				itemLevelFilterData = new ArrayList<ProductsModel>();
				System.out.println("DOCUMENTS");
				itemLevelFilterData = new ArrayList<ProductsModel>();
				while (itr.hasNext()) {
					try{
						ProductsModel itemModel = new ProductsModel();
						SolrDocument doc = itr.next();
						idList = idList + c + doc.getFieldValue("itemid").toString();
						Map<String, Object> customFieldValMap = doc.getFieldValueMap();
						if(doc.getFieldValue("orderQtyIntervalByShipMethod")!=null && doc.getFieldValue("orderQtyIntervalByShipMethod").toString().trim().length()>0){
							LinkedHashMap<String, ProductsModel>shipOrderQtyAndInterval =  getOrderQtyIntervalByShipMethod(doc.getFieldValue("orderQtyIntervalByShipMethod").toString());
							itemModel.setShipOrderQtyAndIntervalFieldVal(shipOrderQtyAndInterval);
						}
						LinkedHashMap<String, Object> customFieldVal = getAllCustomFieldVal(customFieldValMap);
						if(customFieldVal!=null && customFieldVal.size()>0){
							itemModel.setCustomFieldVal(customFieldVal);
						}
						itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
						itemModel.setMinOrderQty(1);
						itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
						itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
						itemModel.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
						itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
						itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
						itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
						itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
						itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
						itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
						itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
						itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
						itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
						itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
						itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
						itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
						itemModel.setClearance((doc.getFieldValue("clearanceFlag")!=null?doc.getFieldValue("clearanceFlag").toString():""));
						if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB").trim().equalsIgnoreCase("Y")){
							if(doc.getFieldValue("catSearchId")!=null && doc.getFieldValue("categoryNamePath")!=null){
								itemModel.setItemBreadCrumb(ProductHunterSolrUltimate.getItemBreadCrumb((List<Integer>) doc.getFieldValue("catSearchId"),(String) doc.getFieldValue("categoryNamePath")));
							}
						}
						if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y")){
							if(doc.getFieldValue("productItemCount")!=null){
								itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
							}
							if(doc.getFieldValue("productId")!=null){
								itemModel.setProductId(CommonUtility.validateNumber(doc.getFieldValue("productId").toString()));
							}
						}
						itemModel.setCategoryCode((doc.getFieldValue("categoryID")!=null?doc.getFieldValue("categoryID").toString():""));
						itemModel.setProductId((doc.getFieldValue("productId")!=null?CommonUtility.validateNumber(doc.getFieldValue("productId").toString()):0));
						itemModel.setProductName((doc.getFieldValue("productName")!=null?doc.getFieldValue("productName").toString():""));
						itemModel.setProductDescription((doc.getFieldValue("productDesc")!=null?doc.getFieldValue("productDesc").toString():""));
						itemModel.setProductCategoryImageName((doc.getFieldValue("productImageName")!=null?doc.getFieldValue("productImageName").toString():""));
						itemModel.setProductCategoryImageType((doc.getFieldValue("productImageType")!=null?doc.getFieldValue("productImageType").toString():""));
						itemModel.setResultCount(resultCount);
						String imageName = null;
						String imageType = null;
						String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
						if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
							itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
						}
						//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
						itemUrl = itemUrl.replaceAll(pattern," ");
						itemUrl = itemUrl.replaceAll("\\s+","-");
						itemModel.setItemUrl(itemUrl);
						if(doc.getFieldValue("imageName")!=null){
							imageName = doc.getFieldValue("imageName").toString();
						}
						if(doc.getFieldValue("imageType")!=null){
							imageType = doc.getFieldValue("imageType").toString();
						}
						if(doc.getFieldValue("upc")!=null){
							itemModel.setUpc(doc.getFieldValue("upc").toString());	
						}
						if(doc.getFieldValue("custom_Catalog_ID")!=null){
							itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());
						}
						
						itemModel.setItemType(CommonUtility.validateString(getSolrFieldValue(doc,"itemType")));
						itemModel.setRentals(CommonUtility.validateString(getSolrFieldValue(doc,"rentals")));
						if(imageName==null){
							imageName = "NoImage.png";
							imageType = "IMAGE";
						}
						itemModel.setImageName(imageName.trim());
						itemModel.setImageType(imageType);
						c = " OR ";
						itemLevelFilterData.add(itemModel);
					}catch (Exception e) {
						System.out.println("Error Occured While retriving data from solr.");
						e.printStackTrace();
					}	

				}
			}


		}
		if(requestType.trim().equalsIgnoreCase("PROMOTION"))
		{

			query = new SolrQuery();
			if(treeId>0)
			{
				if(sortBy!=null && sortBy.trim().equalsIgnoreCase("isTopSeller"))
					query.setQuery("manfId:"+treeId+" AND "+sortBy+":\"Y\"");
				else
					query.setQuery("categoryID:"+treeId+" AND "+sortBy+":\"Y\"");
			}
			else
			{
				query.setQuery(sortBy+":\"Y\"");
			}
			//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
			if(faucetField!=null && faucetField.length>0)
			{
				query.addFacetField(faucetField);
				query.setFacetLimit(-1);
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
					query.setFacetSort(true);
				}else{
					query.setFacetSort(false);
				}

			}

			query.setStart(fromRow);
			query.setRows(resultPerPage);
			query.setFilterQueries(attrFtrNav);
			query.addSortField("goldenitem", SolrQuery.ORDER.desc );
			query.addSortField(sortField, sortOrder);

			QueryResponse response = server.query(query);

			SolrDocumentList documents = response.getResults();
			resultCount = (int) response.getResults().getNumFound();
			List<FacetField> facetFeild = response.getFacetFields();
			LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();

			LinkedHashMap<String, ArrayList<ProductsModel>>  filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
			ArrayList<ProductsModel> attrList = new ArrayList<ProductsModel>();

			ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
			Iterator<SolrDocument> itr = documents.iterator();
			System.out.println("DOCUMENTS");
			for(FacetField facetFilter:facetFeild)
			{
				ProductsModel tempVal = new ProductsModel();

				if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
				{
					filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
					attrList = new ArrayList<ProductsModel>();

					List<Count> attrValArr = facetFilter.getValues();
					for(Count attrArr : attrValArr)
					{
						ProductsModel attrListVal = new ProductsModel();
						attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
						attrListVal.setAttrValue(attrArr.getName());
						attrListVal.setResultCount((int) attrArr.getCount());
						attrList.add(attrListVal);

					}

					filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
					tempVal.setAttrFilterList(filteredList);
					attrFilteredList.add(tempVal);
					attributeList.put(facetFilter.getName().replace("attr_", ""), facetFilter.getValues());
				}

			}
			searchResultList.put("attrList", attrFilteredList);
			//  searchResultList.put("facetRanges", facetRangeArr);
			itemLevelFilterData = new ArrayList<ProductsModel>();
			System.out.println("DOCUMENTS");

			itemLevelFilterData = new ArrayList<ProductsModel>();
			while (itr.hasNext()) {

				try{

					ProductsModel itemModel = new ProductsModel();
					SolrDocument doc = itr.next();
					idList = idList + c + doc.getFieldValue("itemid").toString();

					itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
					itemModel.setMinOrderQty(1);
					itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
					itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
					itemModel.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
					//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
					itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
					itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
					itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
					itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
					itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
					itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
					itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
					itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
					itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
					itemModel.setClearance((doc.getFieldValue("clearanceFlag")!=null?doc.getFieldValue("clearanceFlag").toString():""));
					if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
					{
						if(doc.getFieldValue("productItemCount")!=null){
							itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
						}
						if(doc.getFieldValue("productId")!=null){
							itemModel.setProductId(CommonUtility.validateNumber(doc.getFieldValue("productId").toString()));
						}
					}
					if(doc.getFieldValue("categoryID")!=null){
						itemModel.setCategoryCode((doc.getFieldValue("categoryID")!=null?doc.getFieldValue("categoryID").toString():""));
					}
					itemModel.setResultCount(resultCount);


					String imageName = null;
					String imageType = null;

					String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
					if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
						itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
					}
					//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");


					itemUrl = itemUrl.replaceAll(pattern," ");
					itemUrl = itemUrl.replaceAll("\\s+","-");
					itemModel.setItemUrl(itemUrl);

					if(doc.getFieldValue("imageName")!=null)
						imageName = doc.getFieldValue("imageName").toString();


					if(doc.getFieldValue("imageType")!=null)
						imageType = doc.getFieldValue("imageType").toString();
					if(doc.getFieldValue("upc")!=null)
						itemModel.setUpc(doc.getFieldValue("upc").toString());	

					if(doc.getFieldValue("custom_Catalog_ID")!=null)
					{
						itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());

					}

					if(imageName==null)
					{
						imageName = "NoImage.png";
						imageType = "IMAGE";
					}
					itemModel.setImageName(imageName.trim());
					itemModel.setImageType(imageType);
					c = " OR ";
					itemLevelFilterData.add(itemModel);

				}catch (Exception e) {
					System.out.println("Error Occured While retriving data from solr.");
					e.printStackTrace();
				}

			}
		}
		else  if(requestType.trim().equalsIgnoreCase("SHOP_BY_BRAND"))
		{

			String queryString  = "";
			String connUrl = solrURL+"/mainitemdata";
			String connUrlSub = solrURL+"/mainitemdata";


			ProductsModel searchResultData = new ProductsModel();

			if(!keyWord.trim().equalsIgnoreCase("") && keyWord.length() > 3 && !CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y"))
			{
				if(includePartNumberSearch)
					queryString  = singleKeyword;
				else
					queryString = multiKeyword;
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();



				searchResultData =  ProductHunterSolrUltimate.solrNavigationSearchResult(queryString, connUrl, attrFtrB, fromRow, resultPerPage, connUrlSub, faucetField, origKeyWord, sortField, sortOrder, includePartNumberSearch, "brandID:"+brandId,attrFtrGlobal,fq,"defaultCategory:Y",filteredMultList,false);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(suggestedValue==null){
					suggestedValue = searchResultData.getSuggestedValue();System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());
				}	
				if(suggestedValueList==null){
					suggestedValueList = searchResultData.getSuggestedValueList();
				}
					
					if(itemLevelFilterData.size()>0)
					{
						ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
						idList = searchResultData.getIdList();
						searchResultList.put("attrList", attrFilteredList );
						searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
					}

			}
			else
			{
				query = new SolrQuery();
				query.setQuery("brandID:"+brandId);
				if(faucetField!=null && faucetField.length>0)
				{
					query.addFacetField(faucetField);
					query.setFacetLimit(-1);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
						query.setFacetSort(true);
					}else{
						query.setFacetSort(false);
					}
				}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_VERSION")).equalsIgnoreCase("10")){
					query.setRequestHandler("/mainitem_keywordsearch");
				}

				query.setStart(fromRow);
				query.setRows(resultPerPage);

				query.setFilterQueries(attrFtrB);
				if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y") && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_VERSION")).equalsIgnoreCase("10")){
					if(!viewFrequentlyPurcahsedOnly.equalsIgnoreCase("Y")){
						query.addFilterQuery("defaultProductItem:Y");
					}else{
						query.addFilterQuery("defaultProductItem:(Y OR N)");
					}
				}
				query.addSortField("goldenitem", SolrQuery.ORDER.desc );
				query.addSortField(sortField, sortOrder);	
				if(keyWord!=null && !keyWord.trim().equalsIgnoreCase("") && CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y")){
					 if(validateForSearch.length>1){
						 query.addFilterQuery("{!edismax}"+keyWord);
						 
					 }else{
						 query.addFilterQuery("{!edismax}*"+keyWord+"* OR " + keyWord);
					 }
				 }
				ProductHunterUtility.getInstance().setRangeFilteQuery(query);

				QueryResponse response = server.query(query,METHOD.POST);
				System.out.println("Brand Query : " + query);

				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				facetRanges = response.getFacetRanges();

				LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();
				LinkedHashMap<String, ArrayList<ProductsModel>>  filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				ArrayList<ProductsModel> attrList = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> facetRangeArr = ProductHunterUtility.getInstance().setFacetRanges(facetRanges);

				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("DOCUMENTS");
				LinkedHashMap<String, ArrayList<ProductsModel>> multiFaucetResult = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				if(resultCount>0 && attrFtrGlobal!=null && attrFtrGlobal.length>0)
				{
					multiFaucetResult = ProductHunterSolrUltimate.generateFaucetFilter("brandID:"+brandId, attrFtrGlobal, fq, false, false,null,"defaultCategory:Y",filteredMultList);

					if(multiFaucetResult!=null && multiFaucetResult.get("price")!=null)
					{
						ArrayList<ProductsModel> fRange = multiFaucetResult.get("price");
						if(fRange!=null && fRange.size()>0 && fRange.get(0).getFacetRange()!=null && fRange.get(0).getFacetRange().size()>0)
							facetRangeArr = fRange;
					}

				}
				/*for(FacetField facetFilter:facetFeild)
					{
						ProductsModel tempVal = new ProductsModel();

						if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
						{

							filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
							attrList = new ArrayList<ProductsModel>();
							if(multiFaucetResult!=null && multiFaucetResult.get(facetFilter.getName().replace("attr_", ""))!=null)
							{

								filteredList.put(facetFilter.getName().replace("attr_", ""), multiFaucetResult.get(facetFilter.getName().replace("attr_", "")));
								tempVal.setAttrFilterList(filteredList);
								attrFilteredList.add(tempVal);
							}
							else
							{
								List<Count> attrValArr = facetFilter.getValues();
								System.out.println("Attribute Name : " + URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
								for(Count attrArr : attrValArr)
								{
									ProductsModel attrListVal = new ProductsModel();
									if(filteredMultList ==null)
										filteredMultList = new  LinkedHashMap<String, ArrayList<String>>();
									ArrayList<String> aList = filteredMultList.get(facetFilter.getName());
									if(aList!=null && aList.size()>0)
									{
										if(!aList.contains(attrArr.getName()))
										{
										attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
										attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
										attrListVal.setAttrValue(attrArr.getName());
										attrListVal.setResultCount((int) attrArr.getCount());
										attrList.add(attrListVal);
										}
									}
									else
									{
										attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
										attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
										attrListVal.setAttrValue(attrArr.getName());
										attrListVal.setResultCount((int) attrArr.getCount());
										attrList.add(attrListVal);
									}


								}
								if(attrList!=null && attrList.size()>0)
								{
								filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
								tempVal.setAttrFilterList(filteredList);
								attrFilteredList.add(tempVal);
								}
							}
							attributeList.put(facetFilter.getName().replace("attr_", ""), facetFilter.getValues());
						}

					}*/
				attrFilteredList = ProductHunterSolrUltimate.buildFacetFilter(facetFeild, multiFaucetResult, filteredMultList);
				searchResultList.put("attrList", attrFilteredList);
				searchResultList.put("facetRanges", facetRangeArr);
				itemLevelFilterData = new ArrayList<ProductsModel>();
				while (itr.hasNext()) {

					try{

						ProductsModel itemModel = new ProductsModel();
						SolrDocument doc = itr.next();
						idList = idList + c + doc.getFieldValue("itemid").toString();

						Map<String, Object> customFieldValMap = doc.getFieldValueMap();
						LinkedHashMap<String, Object> customFieldVal = getAllCustomFieldVal(customFieldValMap);
						if(customFieldVal!=null && customFieldVal.size()>0)
							itemModel.setCustomFieldVal(customFieldVal);

						itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
						itemModel.setMinOrderQty(1);
						itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
						itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
						itemModel.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
						//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
						itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
						itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
						itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
						itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
						itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
						itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
						itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
						itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
						itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
						itemModel.setProductName((doc.getFieldValue("productName")!=null?doc.getFieldValue("productName").toString():""));
						itemModel.setProductDescription((doc.getFieldValue("productDesc")!=null?doc.getFieldValue("productDesc").toString():""));
						itemModel.setProductCategoryImageName((doc.getFieldValue("productImageName")!=null?doc.getFieldValue("productImageName").toString():""));
						itemModel.setProductCategoryImageType((doc.getFieldValue("productImageType")!=null?doc.getFieldValue("productImageType").toString():""));
						itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
						itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
						itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
						itemModel.setClearance((doc.getFieldValue("clearanceFlag")!=null?doc.getFieldValue("clearanceFlag").toString():""));
						if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
						{
							if(doc.getFieldValue("productItemCount")!=null){
								itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
							}
							if(doc.getFieldValue("productId")!=null){
								itemModel.setProductId(CommonUtility.validateNumber(doc.getFieldValue("productId").toString()));
							}
						}
						if(doc.getFieldValue("categoryID")!=null){
							itemModel.setCategoryCode((doc.getFieldValue("categoryID")!=null?doc.getFieldValue("categoryID").toString():""));
						}
						itemModel.setResultCount(resultCount);


						String imageName = null;
						String imageType = null;


						String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
						if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
							itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
						}
						//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");


						itemUrl = itemUrl.replaceAll(pattern," ");
						itemUrl = itemUrl.replaceAll("\\s+","-");
						itemModel.setItemUrl(itemUrl);

						if(doc.getFieldValue("imageName")!=null)
							imageName = doc.getFieldValue("imageName").toString();


						if(doc.getFieldValue("imageType")!=null)
							imageType = doc.getFieldValue("imageType").toString();
						if(doc.getFieldValue("upc")!=null)
							itemModel.setUpc(doc.getFieldValue("upc").toString());	

						if(doc.getFieldValue("custom_Catalog_ID")!=null)
						{
							itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());

						}

						if(imageName==null)
						{
							imageName = "NoImage.png";
							imageType = "IMAGE";
						}
						itemModel.setImageName(imageName.trim());
						itemModel.setImageType(imageType);
						c = " OR ";
						itemLevelFilterData.add(itemModel);

					}catch (Exception e) {
						System.out.println("Error Occured While retriving data from solr.");
						e.printStackTrace();
					}

				}

			}

		}else  if(requestType.trim().equalsIgnoreCase("SHOP_BY_MANF"))
		{

			String queryString  = "";
			String connUrl = solrURL+"/mainitemdata";
			String connUrlSub = solrURL+"/mainitemdata";


			ProductsModel searchResultData = new ProductsModel();

			if(!keyWord.trim().equalsIgnoreCase("") && keyWord.length() > 3 && !CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y"))
			{
				if(includePartNumberSearch)
					queryString  = singleKeyword;
				else
					queryString = multiKeyword;
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();



				searchResultData =  ProductHunterSolrUltimate.solrNavigationSearchResult(queryString, connUrl, attrFtrB, fromRow, resultPerPage, connUrlSub, faucetField, origKeyWord, sortField, sortOrder, includePartNumberSearch, "manfId:"+brandId,attrFtrGlobal,fq,"defaultCategory:Y",filteredMultList,false);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(suggestedValue==null)
					suggestedValue = searchResultData.getSuggestedValue();System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());
					
					if(suggestedValueList==null){
						suggestedValueList = searchResultData.getSuggestedValueList();
					}
					
					if(itemLevelFilterData.size()>0)
					{
						ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
						idList = searchResultData.getIdList();
						searchResultList.put("attrList", attrFilteredList );
						searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
					}

			}
			else
			{
				query = new SolrQuery();
				query.setQuery("manfId:"+brandId);
				if(faucetField!=null && faucetField.length>0)
				{
					query.addFacetField(faucetField);
					query.setFacetLimit(-1);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ATTRIBUTE_VALUE_SORT_BY")).equalsIgnoreCase("COUNT")){
						query.setFacetSort(true);
					}else{
						query.setFacetSort(false);
					}
				}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_VERSION")).equalsIgnoreCase("10")){
					query.setRequestHandler("/mainitem_keywordsearch");
				}
				query.setStart(fromRow);
				query.setRows(resultPerPage);

				query.setFilterQueries(attrFtrB);
				if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y") && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_VERSION")).equalsIgnoreCase("10")){
					if(!viewFrequentlyPurcahsedOnly.equalsIgnoreCase("Y")){
						query.addFilterQuery("defaultProductItem:Y");
					}else{
						query.addFilterQuery("defaultProductItem:(Y OR N)");
					}
				}
				query.addSortField("goldenitem", SolrQuery.ORDER.desc );
				query.addSortField(sortField, sortOrder);	
				if(keyWord!=null && !keyWord.trim().equalsIgnoreCase("") && CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y")){
					 if(validateForSearch.length>1){
						 query.addFilterQuery("{!edismax}"+keyWord);
						 
					 }else{
						 query.addFilterQuery("{!edismax}*"+keyWord+"* OR " + keyWord);
					 }
				 }
				ProductHunterUtility.getInstance().setRangeFilteQuery(query);
				QueryResponse response = server.query(query,METHOD.POST);
				System.out.println("Shop By Manufacturer Query : " + query);

				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				facetRanges = response.getFacetRanges();

				LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();
				LinkedHashMap<String, ArrayList<ProductsModel>>  filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				ArrayList<ProductsModel> attrList = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> facetRangeArr = ProductHunterUtility.getInstance().setFacetRanges(facetRanges);
				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("DOCUMENTS");
				LinkedHashMap<String, ArrayList<ProductsModel>> multiFaucetResult = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				if(resultCount>0 && attrFtrGlobal!=null && attrFtrGlobal.length>0)
				{
					multiFaucetResult = ProductHunterSolrUltimate.generateFaucetFilter("manfId:"+brandId, attrFtrGlobal, fq, false, false,null,"defaultCategory:Y",filteredMultList);

					if(multiFaucetResult!=null && multiFaucetResult.get("price")!=null)
					{
						ArrayList<ProductsModel> fRange = multiFaucetResult.get("price");
						if(fRange!=null && fRange.size()>0 && fRange.get(0).getFacetRange()!=null && fRange.get(0).getFacetRange().size()>0)
							facetRangeArr = fRange;
					}

				}
				/*for(FacetField facetFilter:facetFeild)
					{
						ProductsModel tempVal = new ProductsModel();

						if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
						{

							filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
							attrList = new ArrayList<ProductsModel>();
							if(multiFaucetResult!=null && multiFaucetResult.get(facetFilter.getName().replace("attr_", ""))!=null)
							{

								filteredList.put(facetFilter.getName().replace("attr_", ""), multiFaucetResult.get(facetFilter.getName().replace("attr_", "")));
								tempVal.setAttrFilterList(filteredList);
								attrFilteredList.add(tempVal);
							}
							else
							{
								List<Count> attrValArr = facetFilter.getValues();
								System.out.println("Attribute Name : " + URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
								for(Count attrArr : attrValArr)
								{
									ProductsModel attrListVal = new ProductsModel();
									if(filteredMultList ==null)
										filteredMultList = new  LinkedHashMap<String, ArrayList<String>>();
									ArrayList<String> aList = filteredMultList.get(facetFilter.getName());
									if(aList!=null && aList.size()>0)
									{
										if(!aList.contains(attrArr.getName()))
										{
										attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
										attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
										attrListVal.setAttrValue(attrArr.getName());
										attrListVal.setResultCount((int) attrArr.getCount());
										attrList.add(attrListVal);
										}
									}
									else
									{
										attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
										attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
										attrListVal.setAttrValue(attrArr.getName());
										attrListVal.setResultCount((int) attrArr.getCount());
										attrList.add(attrListVal);
									}


								}
								if(attrList!=null && attrList.size()>0)
								{
								filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
								tempVal.setAttrFilterList(filteredList);
								attrFilteredList.add(tempVal);
								}
							}
							attributeList.put(facetFilter.getName().replace("attr_", ""), facetFilter.getValues());
						}

					}*/
				attrFilteredList = ProductHunterSolrUltimate.buildFacetFilter(facetFeild, multiFaucetResult, filteredMultList);
				searchResultList.put("attrList", attrFilteredList);
				searchResultList.put("facetRanges", facetRangeArr);
				itemLevelFilterData = new ArrayList<ProductsModel>();
				while (itr.hasNext()) {

					try{

						ProductsModel itemModel = new ProductsModel();
						SolrDocument doc = itr.next();
						Map<String, Object> customFieldValMap = doc.getFieldValueMap();
						LinkedHashMap<String, Object> customFieldVal = getAllCustomFieldVal(customFieldValMap);
						if(customFieldVal!=null && customFieldVal.size()>0)
							itemModel.setCustomFieldVal(customFieldVal);
						idList = idList + c + doc.getFieldValue("itemid").toString();

						itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
						itemModel.setMinOrderQty(1);
						itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
						itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
						itemModel.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
						//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
						itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
						itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
						itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
						itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
						itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
						itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
						itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
						itemModel.setProductName((doc.getFieldValue("productName")!=null?doc.getFieldValue("productName").toString():""));
						itemModel.setProductDescription((doc.getFieldValue("productDesc")!=null?doc.getFieldValue("productDesc").toString():""));
						itemModel.setProductCategoryImageName((doc.getFieldValue("productImageName")!=null?doc.getFieldValue("productImageName").toString():""));
						itemModel.setProductCategoryImageType((doc.getFieldValue("productImageType")!=null?doc.getFieldValue("productImageType").toString():""));
						itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
						itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
						itemModel.setClearance((doc.getFieldValue("clearanceFlag")!=null?doc.getFieldValue("clearanceFlag").toString():""));
						if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB").trim().equalsIgnoreCase("Y"))
						{
							if(doc.getFieldValue("catSearchId")!=null && doc.getFieldValue("categoryNamePath")!=null)
								itemModel.setItemBreadCrumb(ProductHunterSolrUltimate.getItemBreadCrumb((List<Integer>) doc.getFieldValue("catSearchId"),(String) doc.getFieldValue("categoryNamePath")));

						}
						if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB").trim().equalsIgnoreCase("Y"))
						{
							if(doc.getFieldValue("catSearchId")!=null && doc.getFieldValue("categoryNamePath")!=null)
								itemModel.setItemBreadCrumb(ProductHunterSolrUltimate.getItemBreadCrumb((List<Integer>) doc.getFieldValue("catSearchId"),(String) doc.getFieldValue("categoryNamePath")));

						}
						if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
						{
							if(doc.getFieldValue("productItemCount")!=null){
								itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
							}
							if(doc.getFieldValue("productId")!=null){
								itemModel.setProductId(CommonUtility.validateNumber(doc.getFieldValue("productId").toString()));
							}
						}
						if(doc.getFieldValue("categoryID")!=null){
							itemModel.setCategoryCode((doc.getFieldValue("categoryID")!=null?doc.getFieldValue("categoryID").toString():""));
						}
						itemModel.setResultCount(resultCount);


						String imageName = null;
						String imageType = null;


						String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
						if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
							itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
						}
						//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");


						itemUrl = itemUrl.replaceAll(pattern," ");
						itemUrl = itemUrl.replaceAll("\\s+","-");
						itemModel.setItemUrl(itemUrl);

						if(doc.getFieldValue("imageName")!=null)
							imageName = doc.getFieldValue("imageName").toString();


						if(doc.getFieldValue("imageType")!=null)
							imageType = doc.getFieldValue("imageType").toString();
						if(doc.getFieldValue("upc")!=null)
							itemModel.setUpc(doc.getFieldValue("upc").toString());	

						if(doc.getFieldValue("custom_Catalog_ID")!=null)
						{
							itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());

						}

						if(imageName==null)
						{
							imageName = "NoImage.png";
							imageType = "IMAGE";
						}
						itemModel.setImageName(imageName.trim());
						itemModel.setImageType(imageType);

						itemModel.setManufacturerLogo((doc.getFieldValue("manufacturerLogo")!=null?doc.getFieldValue("manufacturerLogo").toString():"NoImage.png"));

						c = " OR ";
						itemLevelFilterData.add(itemModel);

					}catch (Exception e) {
						System.out.println("Error Occured While retriving data from solr.");
						e.printStackTrace();
					}

				}

			}

		}
		else if(requestType.trim().equalsIgnoreCase("SEARCH"))
		{
			//------ S promotion
			isPromotion = false;
	        isExcludeItems = false;
	        boolean isBrandBoost = false;
	        elevateIds = null;
	        excludeItems = null;
	        bannerEntity = null;
	        String brandBoost = null;
	      
	        int bannerId = 0;
	        if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SEARCH_PROMOTIONS")).equalsIgnoreCase("Y")){
	        	PromotionResponse promotions = PromotionUtility.getInstance().getPromotionByKeyword(keyWord,CommonUtility.validateParseIntegerToString(subsetId),CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
		        if ((promotions != null) && (promotions.getData() != null) && (promotions.getData().getResultSet() != null) && (promotions.getData().getResultSet().size() > 0)) {
		          bannerId = ((PromotionList)promotions.getData().getResultSet().get(0)).getBannerId(); 
		          bannerEntity = ((PromotionList)promotions.getData().getResultSet().get(0)).getBannerEntity();
		          elevateIds = ((PromotionList)promotions.getData().getResultSet().get(0)).getDocuments();
		          excludeItems = ((PromotionList)promotions.getData().getResultSet().get(0)).getExcludeDocuments();
		          brandBoost =  ((PromotionList)promotions.getData().getResultSet().get(0)).getBrands();
		          if ((elevateIds != null) && (!elevateIds.trim().equalsIgnoreCase(""))) {
		            elevateIds = elevateIds.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "");
		            if (elevateIds.length() > 0) {
		              isPromotion = true;
		            }
		          }
		         

		          if ((excludeItems != null) && (!excludeItems.trim().equalsIgnoreCase(""))) {
		            excludeItems = excludeItems.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "");
		            if (excludeItems.length() > 0) {
		              isExcludeItems = true;
		            }
		          }
		          
		          if(brandBoost!=null && !brandBoost.trim().replaceAll("\\[", "").replaceAll("]", "").equalsIgnoreCase("")){
		        	  isBrandBoost = true;
		          }

		        }
	        }
			//------------ S promotion
			keyWord = keyWord.toUpperCase();
			String scrubbedKeyword = keyWord.replaceAll(pattern,"");
			String keyWordWithOutSpace = keyWord.trim();
			keyWord = keyWord.trim();
			keyWord = escapeQueryCulprits(keyWord);
			keyWordWithOutSpace = escapeQueryCulpritsWithoutWhiteSpace(keyWordWithOutSpace);
			boolean isCustomerPartNumber = false;
			int customerPartCount = 0;

			//1.	Customer part number exact OR Customer part number squashed (all special characters removed)
			String narrowSearchKey = null;
			String queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+" OR "+keyWord+"* OR "+scrubbedKeyword+"*)";
			String connUrl = solrURL+"/customerdata";
			String connUrlSub = solrURL+"/mainitemdata";
			ProductsModel searchResultData = new ProductsModel();

			if(keyWord.trim().equalsIgnoreCase(scrubbedKeyword))
				queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+"*)";

			if(includePartNumberSearch)
			{
				System.out.println("Include Part Number Search");

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowSearchKey = narrowKeyword;// "customerPartNumberKeyword:"+narrowKeyword+"*";
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResultCustomerPartNumber( narrowKeyword, queryString, connUrl, attrFtr, attrFtr1, fromRow, resultPerPage,connUrlSub,faucetField);
				itemLevelFilterData = searchResultData.getItemDataList();
				customerPartCount = searchResultData.getResultCount();
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
					idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList );
					//searchResultList.put("facetRanges", facetRangeArr); todo
					isCustomerPartNumber = true;
				}


				//3.	Customer part number % exact % OR customer part number squashed%
				if(itemLevelFilterData.size()<1)
				{
					narrowSearchKey = null;
					queryString  = "customerPartNumberKeyword:(*"+keyWord+"* OR *"+scrubbedKeyword+"*)";
					connUrl = solrURL+"/customerdata";
					connUrlSub = solrURL+"/mainitemdata";
					searchResultData = new ProductsModel();

					if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
					{
						narrowSearchKey = narrowKeyword;// "customerPartNumberKeyword: *"+narrowKeyword+"*";
					}

					searchResultData = ProductHunterSolrUltimate.solrSearchResultCustomerPartNumber( narrowKeyword, queryString, connUrl, attrFtr, attrFtr1, fromRow, resultPerPage,connUrlSub,faucetField);
					customerPartCount = searchResultData.getResultCount();
					itemLevelFilterData = searchResultData.getItemDataList();
					if(itemLevelFilterData.size()>0)
					{
						ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
						idList = searchResultData.getIdList();
						searchResultList.put("attrList", attrFilteredList );
						//searchResultList.put("facetRanges", facetRangeArr); todo
						isCustomerPartNumber = true;
					}

				}
			}


			narrowSearchKey = null;
			if(includePartNumberSearch)
				queryString  = singleKeyword;
			else
				queryString = multiKeyword;
			connUrl = solrURL+"/mainitemdata";
			connUrlSub = solrURL+"/mainitemdata";
			searchResultData = new ProductsModel();

			if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
			{
				narrowSearchKey = narrowKeyword;// "partnumber:(*"+narrowKeyword+"*)";
			}

			if(resultPerPage > itemLevelFilterData.size())
				resultPerPage = resultPerPage - itemLevelFilterData.size();
			else
				resultPerPage = 0;

			if(fromRow > customerPartCount)
				fromRow = fromRow - customerPartCount;
			searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,spellCheckKeyword,sortField,sortOrder,includePartNumberSearch,false,itemLevelFilterData,idList,attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,isBrandBoost,brandBoost);
			itemLevelFilterData = searchResultData.getItemDataList();
			if(suggestedValue==null)
				suggestedValue = searchResultData.getSuggestedValue();System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());
				
				if(suggestedValueList==null){
					suggestedValueList = searchResultData.getSuggestedValueList();
				}
				
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
					if(idList!=null && !idList.trim().equalsIgnoreCase(""))
					{
						if(searchResultData.getIdList()!=null && !searchResultData.getIdList().trim().equalsIgnoreCase(""))
							idList = idList + " OR "+searchResultData.getIdList();
					}
					else
						idList = searchResultData.getIdList();
					//if(!isCustomerPartNumber) {
					if(attrFilteredList.size()>0) {
						searchResultList.put("attrList", attrFilteredList );
					}
					//}
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}


				if (itemLevelFilterData.size() < 1) {
					narrowSearchKey = null;
					if (!includePartNumberSearch)
					{
						queryString = multiKeyword;
						connUrl = solrURL+"/mainitemdata";
						connUrlSub = solrURL+ "/mainitemdata";
						searchResultData = new ProductsModel();

						if (narrowKeyword != null && !narrowKeyword.trim().equalsIgnoreCase("")) {
							narrowSearchKey = narrowKeyword;
						}

						searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString,	connUrl, attrFtr, fromRow, resultPerPage,connUrlSub, faucetField, spellCheckKeyword,sortField, sortOrder,includePartNumberSearch, false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,isBrandBoost,brandBoost);
						itemLevelFilterData = searchResultData.getItemDataList();
						if (suggestedValue == null)
							suggestedValue = searchResultData.getSuggestedValue();
						System.out.println("Suggtion Set : " + suggestedValue+ " - " + searchResultData.getSuggestedValue());
						
						if(suggestedValueList==null){
							suggestedValueList = searchResultData.getSuggestedValueList();
						}
						
						if (itemLevelFilterData.size() > 0) {
							ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
							idList = searchResultData.getIdList();
							searchResultList.put("attrList", attrFilteredList);
							searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
						}
					}


				}


				if (itemLevelFilterData.size() < 1) {
					narrowSearchKey = null;
					if (!includePartNumberSearch)
					{
						queryString = multiKeywordWildCard;
						connUrl = solrURL+"/mainitemdata";
						connUrlSub = solrURL+ "/mainitemdata";
						searchResultData = new ProductsModel();

						if (narrowKeyword != null && !narrowKeyword.trim().equalsIgnoreCase("")) {
							narrowSearchKey = narrowKeyword;
						}

						searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString,	connUrl, attrFtr, fromRow, resultPerPage,connUrlSub, faucetField, spellCheckKeyword,sortField, sortOrder,includePartNumberSearch, false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,true,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,isBrandBoost,brandBoost);
						itemLevelFilterData = searchResultData.getItemDataList();
						if (suggestedValue == null)
							suggestedValue = searchResultData.getSuggestedValue();
						System.out.println("Suggtion Set : " + suggestedValue+ " - " + searchResultData.getSuggestedValue());
						
						if(suggestedValueList==null){
							suggestedValueList = searchResultData.getSuggestedValueList();
						}
						
						if (itemLevelFilterData.size() > 0) {
							ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
							idList = searchResultData.getIdList();
							searchResultList.put("attrList", attrFilteredList);
							searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
						}
					}


				}


				if (itemLevelFilterData.size() < 1 && CommonDBQuery.getSystemParamtersList().get("ENABLE_SOLR_OR_CONDITION")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_SOLR_OR_CONDITION").trim().equalsIgnoreCase("Y")) {
					narrowSearchKey = null;
					if (!includePartNumberSearch)
					{
						queryString = multiKeywordOr;
						connUrl = solrURL+"/mainitemdata";
						connUrlSub = solrURL+ "/mainitemdata";
						searchResultData = new ProductsModel();

						if (narrowKeyword != null && !narrowKeyword.trim().equalsIgnoreCase("")) {
							narrowSearchKey = narrowKeyword;
						}

						searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString,	connUrl, attrFtr, fromRow, resultPerPage,connUrlSub, faucetField, spellCheckKeyword,sortField, sortOrder,includePartNumberSearch, false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,true,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,isBrandBoost,brandBoost);
						itemLevelFilterData = searchResultData.getItemDataList();
						if (suggestedValue == null)
							suggestedValue = searchResultData.getSuggestedValue();
						System.out.println("Suggtion Set : " + suggestedValue+ " - " + searchResultData.getSuggestedValue());
						
						if(suggestedValueList==null){
							suggestedValueList = searchResultData.getSuggestedValueList();
						}
						
						if (itemLevelFilterData.size() > 0) {
							ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
							idList = searchResultData.getIdList();
							searchResultList.put("attrList", attrFilteredList);
							searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
						}
					}


				}

				if(includePartNumberSearch && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
				{
					/*if(itemLevelFilterData!=null && itemLevelFilterData.size()==1 && itemLevelFilterData.get(0).getProductItemCount()>1){
						 ArrayList<ProductsModel> itemLevelFilterDataSingle = new ArrayList<ProductsModel>();
						 queryString  = singleKeyword;

						 connUrl = solrURL+"/mainitemdata";
						 connUrlSub = solrURL+"/mainitemdata";
						 searchResultData = new ProductsModel();
						 searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,spellCheckKeyword,sortField,sortOrder,includePartNumberSearch,false,itemLevelFilterDataSingle,"",attrFtrGlobal,fq,filteredMultList,false,false,true);
						 itemLevelFilterDataSingle = searchResultData.getItemDataList();
						 if(itemLevelFilterDataSingle!=null && itemLevelFilterDataSingle.size()==1){
							 itemLevelFilterData = itemLevelFilterDataSingle;
						 }
						if (suggestedValue == null)
							suggestedValue = searchResultData.getSuggestedValue();
							System.out.println("Suggtion Set : " + suggestedValue+ " - " + searchResultData.getSuggestedValue());
						if (itemLevelFilterData.size() > 0) {
							ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
							idList = searchResultData.getIdList();
							searchResultList.put("attrList", attrFilteredList);
							searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
						}
					 }
					if(itemLevelFilterData!=null && itemLevelFilterData.size()==1 && itemLevelFilterData.get(0).getProductItemCount()>1){
						ArrayList<ProductsModel> itemLevelFilterDataSingle = new ArrayList<ProductsModel>();
						queryString  = singleKeyword;

						connUrl = solrURL+"/mainitemdata";
						connUrlSub = solrURL+"/mainitemdata";
						searchResultData = new ProductsModel();
						searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,spellCheckKeyword,sortField,sortOrder,includePartNumberSearch,false,itemLevelFilterDataSingle,"",attrFtrGlobal,fq,filteredMultList,false,false,true,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity);
						itemLevelFilterDataSingle = searchResultData.getItemDataList();
						if(itemLevelFilterDataSingle!=null && itemLevelFilterDataSingle.size()==1){
							itemLevelFilterData = itemLevelFilterDataSingle;
							ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
							idList = searchResultData.getIdList();
							searchResultList.put("attrList", attrFilteredList);
							searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
							if (suggestedValue == null)
								suggestedValue = searchResultData.getSuggestedValue();
							System.out.println("Suggtion Set : " + suggestedValue+ " - " + searchResultData.getSuggestedValue());
							
							if(suggestedValueList==null){
								suggestedValueList = searchResultData.getSuggestedValueList();
							}

						}

					}*/

				}

		}
		else if(requestType.trim().equalsIgnoreCase("SEARCH-AS01"))
		{
			keyWord = keyWord.toUpperCase();

			String scrubbedKeyword = keyWord.replaceAll(pattern,"");

			//escaping the special chars

			keyWord = escapeQueryCulprits(keyWord);

			String narrowSearchKey = null;
			//String queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+" OR "+keyWord+"* OR "+scrubbedKeyword+"*)";

			String queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+keyWord+"*" +")";

			if(keyWord.trim().equalsIgnoreCase(scrubbedKeyword))
				queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+"*)";

			String connUrl = solrURL+"/customerdata";
			String connUrlSub = solrURL+"/mainitemdata";
			ProductsModel searchResultData = new ProductsModel();
			//manfpartnumber Part number exact  OR part number squashed

			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				connUrl = solrURL+"/customerdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{

					narrowSearchKey = narrowKeyword;
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResultCustomerPartNumber( narrowSearchKey, queryString, connUrl, attrFtr, attrFtr1, fromRow, resultPerPage,connUrlSub,faucetField);
				itemLevelFilterData = searchResultData.getItemDataList();



				if(itemLevelFilterData.size()>0)
				{

					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
					idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList );
					//searchResultList.put("facetRanges", facetRangeArr); todo


				}

			}
		}
		else if(requestType.trim().equalsIgnoreCase("SEARCH-AS02"))
		{
			keyWord = keyWord.toUpperCase();

			String scrubbedKeyword = keyWord.replaceAll(pattern,"");


			//escaping the special chars

			keyWord = escapeQueryCulprits(keyWord);

			String narrowSearchKey = null;
			String queryString  = "customerPartNumber:("+keyWord+" OR "+scrubbedKeyword+")";
			String connUrl = solrURL+"/customerdata";
			String connUrlSub = solrURL+"/mainitemdata";
			ProductsModel searchResultData = new ProductsModel();
			//upc Part number exact  OR part number squashed
			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				queryString  = "upc:("+keyWord+" OR "+scrubbedKeyword+")";
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowSearchKey = "upc:"+ narrowKeyword;
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowKeyword, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,false,false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
					idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList );
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}

			}


			//manfpartnumber number exact % OR part number squashed%
			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				queryString  = "upc:("+keyWord+"* OR "+scrubbedKeyword+"*)";
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowSearchKey = "upc:(*"+narrowKeyword+"*)";
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowKeyword, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,false,false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
					searchResultList.put("attrList", attrFilteredList );
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}

			}

			//manfpartnumber number exact % OR part number squashed%
			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				queryString  = "upc:(*"+keyWord+"* OR *"+scrubbedKeyword+"*)";
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowSearchKey = "upc:(*"+narrowKeyword+"*)";
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowKeyword, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,false,false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList(); idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList );
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}

			}


		}
		else if(requestType.trim().equalsIgnoreCase("SEARCH-AS03"))
		{//Part number exact  OR part number squashed


			String validateForSearch1[] = keyWord.split("\\s+",-1);
			p = Pattern.compile("[^a-zA-Z0-9]");
			p1 = Pattern.compile("[\\d]");
			includePartNumberSearch = false;
			singleKeyword = "";
			multiKeyword = "";
			pattern = "[^A-Za-z0-9]";
			if(validateForSearch1!=null && validateForSearch1.length>1)
			{
				String separator = "";
				for(String keyBuilder:validateForSearch1)
				{
					boolean hasSpecialChar = p1.matcher(keyBuilder.trim()).find();
					if(keyBuilder.matches("[-+]?\\d*\\.?\\d+"))
					{
						includePartNumberSearch = true;
					}
					else
					{
						if(hasSpecialChar)
						{
							includePartNumberSearch = true;
						}
						else
						{
							hasSpecialChar = p.matcher(keyBuilder.trim()).find();
							if(hasSpecialChar)
							{
								includePartNumberSearch = true;
							}
						}

					}

					if(includePartNumberSearch)
					{
						multiKeyword = multiKeyword + separator + buildKeyword(keyBuilder);
						separator = " ";
					}
					includePartNumberSearch = false;
				}

				multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord) + multiKeyword;
				multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
				System.out.println("multiKeyword Query Keyword : " + multiKeyword);
			}
			else
			{

				includePartNumberSearch = true;
				singleKeyword = "("+buildKeyword(keyWord)+")";
				System.out.println("Single Query Keyword : " + singleKeyword);


			}
			keyWord = keyWord.toUpperCase();


			String scrubbedKeyword = keyWord.replaceAll(pattern,"");
			String narrowSearchKey = null;

			keyWord = escapeQueryCulprits(keyWord);

			String queryString  = "customerPartNumber:("+keyWord+" OR "+scrubbedKeyword+")";
			String connUrl = solrURL+"/customerdata";
			String connUrlSub = solrURL+"/mainitemdata";
			ProductsModel searchResultData = new ProductsModel();

			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				if(includePartNumberSearch)
					queryString  = singleKeyword;
				else
					queryString = multiKeyword;
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowSearchKey = narrowKeyword;
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,false,true,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(suggestedValue==null)
					suggestedValue = searchResultData.getSuggestedValue();System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());
					
					if(suggestedValueList==null){
						suggestedValueList = searchResultData.getSuggestedValueList();
					}
					
					if(itemLevelFilterData.size()>0)
					{
						ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();
						idList = searchResultData.getIdList();
						searchResultList.put("attrList", attrFilteredList );
						searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
					}

			}
			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				queryString  = "partnumbersearch:("+keyWord+" OR "+scrubbedKeyword+")";
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowSearchKey = narrowKeyword;
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,false,false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList(); idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList );
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}

			}


			//Part number exact % OR part number squashed%
			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				queryString  = "partnumbersearch:("+keyWord+"* OR "+scrubbedKeyword+"*)";
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowSearchKey = narrowKeyword;
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,false,false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList(); idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList );
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}

			}

			//Part number exact % OR part number squashed%
			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				queryString  = "partnumbersearch:(*"+keyWord+"* OR *"+scrubbedKeyword+"*)";
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowSearchKey = narrowKeyword;
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,false,false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList(); idList = searchResultData.getIdList();idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList );
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}

			}
		}
		else if(requestType.trim().equalsIgnoreCase("SEARCH-AS04"))
		{
			//only sfPartnumber number
			keyWord = keyWord.toLowerCase();

			String scrubbedKeyword = keyWord.replaceAll(pattern,"");
			String narrowSearchKey = null;
			String queryString  = "customerPartNumber:("+keyWord+" OR "+scrubbedKeyword+")";
			String connUrl = solrURL+"/customerdata";
			String connUrlSub = solrURL+"/mainitemdata";
			ProductsModel searchResultData = new ProductsModel();
			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				keyWord = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
				//keyWord = keyWord.replaceAll("\\s", " AND ");
				queryString  = "description:("+keyWord.toLowerCase().replaceAll("\\s", " AND ")+")";
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowKeyword = narrowKeyword.trim();
					narrowSearchKey = "keywords:(*"+narrowKeyword+"*)";
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowKeyword, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,false,false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList(); idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList );
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}

			}

		}
		else if(requestType.trim().equalsIgnoreCase("SEARCH-AS05"))
		{
			keyWord = keyWord.toUpperCase();
			//only sfPartnumber number

			String scrubbedKeyword = keyWord.replaceAll(pattern,"");

			keyWord = escapeQueryCulprits(keyWord);				  	
			String narrowSearchKey = null;
			String queryString  = "customerPartNumber:("+keyWord+" OR "+scrubbedKeyword+")";
			String connUrl = solrURL+"/customerdata";
			String connUrlSub = solrURL+"/mainitemdata";
			ProductsModel searchResultData = new ProductsModel();
			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				queryString  = "\""+keyWord.toUpperCase()+"\"";
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowKeyword = narrowKeyword.trim();
					narrowSearchKey = "keywords:(*"+narrowKeyword+"*)";
				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowKeyword, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,false,false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList(); idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList );
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}

			}

		}
		else if(requestType.trim().equalsIgnoreCase("SEARCH-AS06"))
		{
			keyWord = keyWord.toUpperCase();

			String scrubbedKeyword = keyWord.replaceAll(pattern,"");
			keyWord = escapeQueryCulprits(keyWord);
			String narrowSearchKey = null;
			String queryString  = "customerPartNumber:("+keyWord+" OR "+scrubbedKeyword+")";
			String connUrl = solrURL+"/customerdata";
			String connUrlSub = solrURL+"/mainitemdata";
			ProductsModel searchResultData = new ProductsModel();
			if(itemLevelFilterData.size()<1)
			{
				narrowSearchKey = null;
				keyWord = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
				keyWord = keyWord.replaceAll("\\s", " OR ");
				queryString  = keyWord.toUpperCase();
				connUrl = solrURL+"/mainitemdata";
				connUrlSub = solrURL+"/mainitemdata";
				searchResultData = new ProductsModel();

				if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
				{
					narrowKeyword = narrowKeyword.trim();

				}

				searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowKeyword, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,false,false,itemLevelFilterData,"",attrFtrGlobal,fq,filteredMultList,false,false,false,false,viewFrequentlyPurcahsedOnly, isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				itemLevelFilterData = searchResultData.getItemDataList();
				if(itemLevelFilterData.size()>0)
				{
					ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList(); idList = searchResultData.getIdList();
					searchResultList.put("attrList", attrFilteredList );
					searchResultList.put("facetRanges", ProductHunterUtility.getInstance().setFacetRanges(searchResultData.getFacetRange()));
				}

			}
		}else if(requestType.trim().equalsIgnoreCase("SEARCH-AS07")){
			ProductsModel searchResultData = new ProductsModel();
			//manfpartnumber Part number exact  OR part number squashed

			itemLevelFilterData = advanceMpnSearch(keyWord,fromRow, resultPerPage,narrowKeyword, fqNav, sortField, sortOrder,exactSearch,brandId);

			idList = "";

			for(ProductsModel ic : itemLevelFilterData) {

				idList += ic.getItemId() + " ";

			}

		}else if(requestType.trim().equalsIgnoreCase("SEARCH-AS08")){
			ProductsModel searchResultData = new ProductsModel();
			//manfpartnumber Part number exact  OR part number squashed

			itemLevelFilterData = advancePartNumberSearch(keyWord,fromRow, resultPerPage, narrowKeyword, fqNav, sortField, sortOrder,exactSearch);

			idList = "";

			for(ProductsModel ic : itemLevelFilterData) {

				idList += ic.getItemId() + " ";

			}

		} else if(requestType.trim().equalsIgnoreCase("SEARCH-AS09")){
			ProductsModel searchResultData = new ProductsModel();
			//manfpartnumber Part number exact  OR part number squashed

			itemLevelFilterData = advancePartNumberSearch(keyWord,fromRow, resultPerPage, narrowKeyword, fqNav, sortField, sortOrder,exactSearch);

			idList = "";

			for(ProductsModel ic : itemLevelFilterData) {

				idList += ic.getItemId() + " ";

			}

		}else if(requestType.trim().equalsIgnoreCase("SEARCH-AS10")){
			ProductsModel searchResultData = new ProductsModel();
			//manfpartnumber Part number exact  OR part number squashed

			itemLevelFilterData = advanceUPCSearch(keyWord,fromRow, resultPerPage,narrowKeyword, fqNav, sortField, sortOrder,exactSearch);

			idList = "";

			for(ProductsModel ic : itemLevelFilterData) {

				idList += ic.getItemId() + " ";

			}

		}


		itemLevelFilterDataSug = new ArrayList<ProductsModel>();
		ProductsModel itemSuggestion = new ProductsModel();
		itemSuggestion.setSuggestedValue(suggestedValue);
		itemLevelFilterDataSug.add(itemSuggestion);
		System.out.println("Suggen value updated");
		searchResultList.put("itemSuggest", itemLevelFilterDataSug);
		
		if(suggestedValueList!=null){
			ArrayList<ProductsModel> tempSuggestionList = new ArrayList<ProductsModel>();
			ProductsModel itemSuggestionModel = new ProductsModel();
			itemSuggestionModel.setSuggestedValueList(suggestedValueList);
			tempSuggestionList.add(itemSuggestionModel);
			searchResultList.put("itemSuggestValueList", tempSuggestionList); 
		}
		
		if(itemLevelFilterData.size()>0)
		{
			LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartnumber = getcustomerPartnumber(idList, buyingCompanyId, buyingCompanyId);
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SINGLETON_SOLR")).equalsIgnoreCase("Y")){
				server1 = ItemPriceData.getInstance().getConnection();
			}else {
				server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
			}			
			server1.setParser(new XMLResponseParser());
			QueryRequest req1 = new QueryRequest();
			req1.setMethod(METHOD.POST);
			SolrQuery query1 = new SolrQuery();
			if(generalSubset>0 && generalSubset!=subsetId) {
				 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";
				 if(!siteNameSolr.equalsIgnoreCase(""))
				 {
					 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
				 }
			}


			query1.setQuery("itemid:("+idList+")");
			String fq2 = "type:"+indexType;
			query1.setFilterQueries(fq2);
			query1.setStart(0);
			query1.setRows(itemLevelFilterData.size());

			QueryResponse response1 = server1.query(query1);

			SolrDocumentList documents1 = response1.getResults();
			resultCount = (int) response1.getResults().getNumFound();



			Iterator<SolrDocument> itr1 = documents1.iterator();
			NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
			ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
			while (itr1.hasNext()) {

				SolrDocument doc = itr1.next();
				for(ProductsModel iList : itemLevelFilterData)
				{
					int itemId = CommonUtility.validateNumber(doc.getFieldValue("itemid").toString());
					if(itemId==iList.getItemId())
					{
						ProductsModel ProductsModel = new ProductsModel();
						ProductsModel.setErpPartNumber(iList.getPartNumber());
						partIdentifiersList.add(ProductsModel);
						int minOrdQty = 1;
						int orderQtyInterval = 1;
						int saleQty = 1;
						
						int packageQty = 1;
						if(iList.getPackageQty()>0){
							packageQty = iList.getPackageQty();
						}
						
						
						int packageFlag = 0;
						String mOrdQty = null;
						String iPrice ="";
						float imapPrice = 0;
						String imap = null;
						if(doc.getFieldValue("price")!=null)
						{
							iPrice = doc.getFieldValue("price").toString();
						}


						double itemPrice = 0d;
						if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
						{
							itemPrice = Double.parseDouble(iPrice);
						}

						iList.setPrice(itemPrice);
						if(doc.getFieldValue("uom")!=null)
							iList.setUom(doc.getFieldValue("uom").toString());
						iList.setItemPriceId(CommonUtility.validateNumber(doc.getFieldValue("itemPriceId").toString()));


						/*
						 *  commented next lines 
						 *  
						 	if(doc.getFieldValue("customerPartNumber")!=null)
								iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());

						 */		
						
						if(doc.getFieldValue("overRidePriceRule")!=null){
							iList.setOverRidePriceRule((String)doc.getFieldValue("overRidePriceRule"));
						}
						if(doc.getFieldValue("materialGroup")!=null)
							iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());

						if(doc.getFieldValue("minordqty")!=null)
						{
							if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
								minOrdQty = CommonUtility.validateNumber(doc.getFieldValue("minordqty").toString());
							if(minOrdQty==0)
								minOrdQty = 1;
						}
						if(doc.getFieldValue("orderQtyInterval")!=null)
						{
							if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
								orderQtyInterval = CommonUtility.validateNumber(doc.getFieldValue("orderQtyInterval").toString());
							if(orderQtyInterval==0)
								orderQtyInterval = 1;
						}
						if(doc.getFieldValue("salesQty")!=null)
						{
							if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
								saleQty = CommonUtility.validateNumber(doc.getFieldValue("salesQty").toString());

							if(saleQty==0)
								saleQty = 1;
						}

						if(doc.getFieldValue("packageFlag")!=null)
						{
							if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
								packageFlag = CommonUtility.validateNumber(doc.getFieldValue("packageFlag").toString());
						}

						if(doc.getFieldValue("packageQty")!=null)
						{
							if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
							{
								packageQty = CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString());
							}

						}

						if(doc.getFieldValue("packDesc")!=null)
						{
							String packDesc = doc.getFieldValue("packDesc").toString();
							iList.setPackDesc(packDesc);
						}

						if(doc.getFieldValue("displayPricing") != null){

							iList.setDisplayPricing(doc.getFieldValue("displayPricing").toString());
						}
						if(doc.getFieldValue("clearanceFlag") != null){
							
							iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
						}
						
						if(doc.getFieldValue("imap")!=null){
							imap = (doc.getFieldValue("imap").toString());
						}
						if(CommonUtility.validateString(imap).length()>0 && imap.toUpperCase().contains("Y") && doc.getFieldValue("imapPrice")!=null){
							imapPrice = (Float) (doc.getFieldValue("imapPrice"));
						}

						if(doc.getFieldValue("seourl")!=null){
							iList.setSupercedeurl(doc.getFieldValue("seourl").toString());
						}
						
						iList.setPackageFlag(packageFlag);
						iList.setPackageQty(packageQty);
						iList.setSaleQty(saleQty);
						iList.setMinOrderQty(minOrdQty);
						iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
						iList.setCustomerPartNumberList(customerPartnumber.get(itemId));
						iList.setImapPrice(imapPrice);
						iList.setIsImap(imap);

					}
				}
			}

			String requiredAvailabilty = null;
			if(userId>1){
				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_AVAILABILITY_PRODUCT_LISTING");
			}else{
				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_AVAILABILITY_PRODUCT_LISTING");	
			}
			String isErpDown = CommonDBQuery.getSystemParamtersList().get("ECLIPSEAVAILABLE");
			String swithOnEclipse = CommonDBQuery.getSystemParamtersList().get("SWITCHONECLIPSEPRICE");


			if(swithOnEclipse!=null && swithOnEclipse.equalsIgnoreCase("Y") && type.equalsIgnoreCase("mobile")){
				massEnquiry = true;

			}else if(swithOnEclipse!=null && swithOnEclipse.equalsIgnoreCase("N") && type.equalsIgnoreCase("")){
				massEnquiry = true;
			}else {
				massEnquiry = false;
			}
			if(massEnquiry){
				if(itemLevelFilterData!=null && itemLevelFilterData.size()>0 && userToken != null && !userToken.trim().equalsIgnoreCase("")){
					ArrayList<ProductsModel> productPriceOutput = new ArrayList<ProductsModel>();
					productPriceOutput = ERPProductsWrapper.massProductInquiry(userToken,partIdentifiersList, userName,entityId,requiredAvailabilty);
					for(ProductsModel itemPrice : itemLevelFilterData){
						for(ProductsModel eclipseitemPrice : productPriceOutput){
							if(itemPrice!=null){
								if(itemPrice.getPartNumber().equals(eclipseitemPrice.getPartNumber().trim())){
									if(itemPrice.getIsImap()!=null && itemPrice.getIsImap().trim().equalsIgnoreCase("Y")){
										itemPrice.setImapPrice(eclipseitemPrice.getCustomerPrice());
									}else{
										itemPrice.setPrice(eclipseitemPrice.getCustomerPrice());
									}
									itemPrice.setBranchAvail(eclipseitemPrice.getBranchAvail());
									if(itemPrice.getUom() == null){
										itemPrice.setUom(eclipseitemPrice.getUom());
									}
									LinkedHashMap<String, ProductsModel> branchTeritory = eclipseitemPrice.getBranchTeritory();
									if(branchTeritory!=null){
										ProductsModel bTeritory = branchTeritory.get(homeTeritory);
										if(bTeritory!=null)
										{
											itemPrice.setBranchID(bTeritory.getBranchID());
											itemPrice.setBranchName(bTeritory.getBranchName());
											itemPrice.setHomeBranchavailablity(bTeritory.getAvailQty());
											itemPrice.setBranchTotalQty(bTeritory.getAvailQty());
										}
									}
									if(eclipseitemPrice.getBranchTotalQty()>0){
										if(eclipseitemPrice.getBranchAvail().size()>0 && eclipseitemPrice.getBranchAvail().get(0).getAvailQty()==0){
											itemPrice.setDisplayFrieghtAlert("Y");
										}
									}
									//itemPrice.setBranchTotalQty(eclipseitemPrice.getBranchTotal());
									if(CommonDBQuery.getSystemParamtersList().get("GET_PRICE_FROM").equalsIgnoreCase("eclipse")){
										itemPrice.setPrice(eclipseitemPrice.getExtendedPrice()*eclipseitemPrice.getQty());
										itemPrice.setQtyUOM(eclipseitemPrice.getQtyUOM());
										itemPrice.setQty(eclipseitemPrice.getQty());
									}
								}
							}
						}

					}
				}
			}
			searchResultList.put("itemList", itemLevelFilterData);
			searchResultList.put("filteredList", refinedList);
		}
	}catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SINGLETON_SOLR")).equalsIgnoreCase("Y")){
			ConnectionManager.closeSolrClientConnection(server);
			ConnectionManager.closeSolrClientConnection(server1);
		}
	}
	return searchResultList;
}

public static LinkedHashMap<String, ArrayList<ProductsModel>> getPromotionDeals(String promotionType)
{
	LinkedHashMap<String, ArrayList<ProductsModel>> promotionListGroup = new LinkedHashMap<String, ArrayList<ProductsModel>>();
	try
	{
		ArrayList<String> phList = ProductHunterSolrUltimate.phTypeList();
		for(String pList:phList)
		{
			ArrayList<ProductsModel> promotionList = ProductHunterSolrUltimate.productPromotion(pList,promotionType);
			if(promotionList!=null && promotionList.size()>0)
			{
				promotionListGroup.put(pList, promotionList);
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return promotionListGroup;
}

public static String  buildKeyword(String keyWord)
{
	String singleKeyword = "";
	String pattern = "[^A-Za-z0-9]";
	try{
		singleKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
		 
		 if(!keyWord.replaceAll(pattern,"").trim().equalsIgnoreCase(singleKeyword))
		 {
			 singleKeyword = singleKeyword +" "+ keyWord.replaceAll(pattern,"")+" *"+singleKeyword+"* *"+keyWord.replaceAll(pattern,"")+"*";
		 }
		 else
		 {
			 singleKeyword = singleKeyword +" *"+keyWord.replaceAll(pattern,"")+"*";
		 }
	}catch(Exception e){
		e.printStackTrace();
	}
	 return singleKeyword;
	 
}

public static String  buildExactKeyword(String keyWord)
{
	String singleKeyword = "";
	String pattern = "[^A-Za-z0-9]";
	try{
		singleKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
		singleKeyword = singleKeyword.replaceAll("\\s+", " AND ");
		if(!keyWord.replaceAll(pattern,"").trim().equalsIgnoreCase(singleKeyword))
		{
			singleKeyword = singleKeyword +" "+ keyWord.replaceAll(pattern,"")+" "+singleKeyword+" "+keyWord.replaceAll(pattern,"");
		}
		else
		{
			singleKeyword = singleKeyword +" "+keyWord.replaceAll(pattern,"");
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return "("+singleKeyword+")";

}

static ArrayList<ProductsModel> advanceMpnSearch(String queryString, int start, int resultPerPage, String narrowKeyWord, 
		String fqNav, String sortField, ORDER sortOrder,boolean exactSearch, String manfId ) {
	String searchKeyword = queryString;
	String keyWord = buildKeyword(queryString);
	if(exactSearch)
		keyWord = buildExactKeyword(queryString);
	
	queryString = keyWord;
	
	ArrayList<ProductsModel> itemList = new ArrayList<ProductsModel>();
	HttpSolrServer server = null;
	try {
		server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
	

		int resultCount = 0;
		
		
			query.setQuery("manfpartnumbersearch:" + queryString);
			
			
			if(sortField != null && sortOrder != null){
				query.addSortField(sortField, sortOrder);
			}else {
				
				query.addSortField("manfpartnumber", SolrQuery.ORDER.asc);
				
			}
			
			
			if(narrowKeyWord != null && narrowKeyWord.trim().length() > 0){
				
				narrowKeyWord = escapeQueryCulprits(narrowKeyWord.trim());
				
				query.setFilterQueries(new String[]{fqNav, narrowKeyWord,"defaultCategory:Y"});
			}else {
				query.setFilterQueries(new String[]{fqNav,"defaultCategory:Y"});
			}
			if(CommonUtility.validateNumber(manfId)>0)
				query.addFilterQuery("manfId:"+manfId);
			query.setStart(start);
			query.setRows(resultPerPage);
			if(!siteNameSolr.equalsIgnoreCase(""))
            {
            	query.set("customerName",siteNameSolr);
            	query.set("q.original",searchKeyword);
            }
			
			QueryResponse response = server.query(query);
			System.out.println("MPN  Query : " + query);
			SolrDocumentList documents = response.getResults();
			resultCount = (int) response.getResults().getNumFound();
			
			if(resultCount < 1 && !exactSearch) {
				
				queryString = buildKeyword(keyWord);
				
				query.setQuery("manfpartnumbersearch:" + queryString);
				
				if(sortField != null && sortOrder != null){
					query.addSortField(sortField, sortOrder);
				}else {
					
					query.addSortField("manfpartnumber", SolrQuery.ORDER.asc);
					
				}
				
				
				if(narrowKeyWord != null && narrowKeyWord.trim().length() > 0){
					narrowKeyWord = escapeQueryCulprits(narrowKeyWord.trim());
					
					query.setFilterQueries(new String[]{fqNav, narrowKeyWord,"defaultCategory:Y"});
				}else {
					query.setFilterQueries(new String[]{fqNav,"defaultCategory:Y"});
				}
				
				query.setStart(start);
				query.setRows(resultPerPage);
				
				response = server.query(query);
				System.out.println("MPN  Query : " + query);
				documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				
			}
			
			Iterator<SolrDocument> itr = documents.iterator();
			
			System.out.println("Total Reulsts Found : " + resultCount);
			
			while (itr.hasNext()) {
				
				try{
				
				ProductsModel itemModel = new ProductsModel();
				SolrDocument doc = itr.next();
				
				
				itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
				itemModel.setMinOrderQty(1);
				itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
				itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
				//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
				itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
				itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
				itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
				itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
				itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
				itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
				itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
				itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
				itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
				itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
				itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
				if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
				{
					if(doc.getFieldValue("productItemCount")!=null){
						itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
					}
				}
				itemModel.setResultCount(resultCount);
				
				String imageName = null;
       			String imageType = null;
       			
       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
       			}
       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
       			String pattern = "[^A-Za-z0-9]";
   			 itemUrl = itemUrl.replaceAll(pattern," ");
   			 itemUrl = itemUrl.replaceAll("\\s+","-");
            	itemModel.setItemUrl(itemUrl);
       			
       			if(doc.getFieldValue("imageName")!=null)
       				imageName = doc.getFieldValue("imageName").toString();
       			
       					       			
       			if(doc.getFieldValue("imageType")!=null)
       				imageType = doc.getFieldValue("imageType").toString();
       			if(doc.getFieldValue("upc")!=null)
       				itemModel.setUpc(doc.getFieldValue("upc").toString());	
       			
       			if(imageName==null)
       			{
       				imageName = "NoImage.png";
       				imageType = "IMAGE";
       			}
       			itemModel.setImageName(imageName.trim());
       			itemModel.setImageType(imageType);
       			
				itemList.add(itemModel);
				
				}catch (Exception e) {
					System.out.println("Error Occured While retriving data from solr.");
					e.printStackTrace();
				}
				
			}
	 
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return itemList;
}



static ArrayList<ProductsModel> advanceUPCSearch(String queryString, int start, int resultPerPage, String narrowKeyWord, 
		String fqNav, String sortField, ORDER sortOrder,boolean exactSearch) {
	
	String searchKeyword = queryString;
	String keyWord = buildKeyword(queryString);
	
	if(exactSearch)
		keyWord = buildExactKeyword(queryString);
	
	queryString = keyWord;
	
	ArrayList<ProductsModel> itemList = new ArrayList<ProductsModel>();
	HttpSolrServer server = null;
	try {
		server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
	

		int resultCount = 0;
		
		
			query.setQuery("upc:" + queryString);
			
			if(sortField != null && sortOrder != null){
				query.addSortField(sortField, sortOrder);
			}else {
				
				query.addSortField("upc", SolrQuery.ORDER.asc);
				
			}
			
			
			if(narrowKeyWord != null && narrowKeyWord.trim().length() > 0){
				
				narrowKeyWord = escapeQueryCulprits(narrowKeyWord.trim());
				
				query.setFilterQueries(new String[]{fqNav, narrowKeyWord,"defaultCategory:Y"});
			}else {
				query.setFilterQueries(new String[]{fqNav,"defaultCategory:Y"});
			}
			
			query.setStart(start);
			query.setRows(resultPerPage);
			if(!siteNameSolr.equalsIgnoreCase(""))
            {
            	query.set("customerName",siteNameSolr);
            	query.set("q.original",searchKeyword);
            }
			
			QueryResponse response = server.query(query);
			System.out.println("MPN  Query : " + query);
			SolrDocumentList documents = response.getResults();
			resultCount = (int) response.getResults().getNumFound();
			
			if(resultCount < 1 && !exactSearch){
				
				queryString = buildKeyword(keyWord);
				
				query.setQuery("upc:" + queryString);
				
				if(sortField != null && sortOrder != null){
					query.addSortField(sortField, sortOrder);
				}else {
					
					query.addSortField("upc", SolrQuery.ORDER.asc);
					
				}
				
				
				if(narrowKeyWord != null && narrowKeyWord.trim().length() > 0){
					narrowKeyWord = escapeQueryCulprits(narrowKeyWord.trim());
					
					query.setFilterQueries(new String[]{fqNav, narrowKeyWord,"defaultCategory:Y"});
				}else {
					query.setFilterQueries(new String[]{fqNav,"defaultCategory:Y"});
				}
				
				query.setStart(start);
				query.setRows(resultPerPage);
				
				response = server.query(query);
				System.out.println("UPC  Query : " + query);
				documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				
			}
			
			Iterator<SolrDocument> itr = documents.iterator();
			
			System.out.println("Total Reulsts Found : " + resultCount);
			
			while (itr.hasNext()) {
				
				try{
				
				ProductsModel itemModel = new ProductsModel();
				SolrDocument doc = itr.next();
				
				
				itemModel.setItemId(Integer.parseInt(doc.getFieldValue("itemid").toString()));
				itemModel.setMinOrderQty(1);
				itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
				itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
				//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
				itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
				itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
				itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
				itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
				itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
				itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
				itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
				itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
				itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
				itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
				itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
				if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
				{
					if(doc.getFieldValue("productItemCount")!=null){
						itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
					}
				}
				itemModel.setResultCount(resultCount);
				
				String imageName = null;
       			String imageType = null;
       			
       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
       			}
       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
       			String pattern = "[^A-Za-z0-9]";
   			 itemUrl = itemUrl.replaceAll(pattern," ");
   			 itemUrl = itemUrl.replaceAll("\\s+","-");
            	itemModel.setItemUrl(itemUrl);
       			
       			if(doc.getFieldValue("imageName")!=null)
       				imageName = doc.getFieldValue("imageName").toString();
       			
       					       			
       			if(doc.getFieldValue("imageType")!=null)
       				imageType = doc.getFieldValue("imageType").toString();
       			if(doc.getFieldValue("upc")!=null)
       				itemModel.setUpc(doc.getFieldValue("upc").toString());	
       			
       			if(imageName==null)
       			{
       				imageName = "NoImage.png";
       				imageType = "IMAGE";
       			}
       			itemModel.setImageName(imageName.trim());
       			itemModel.setImageType(imageType);
       			
				itemList.add(itemModel);
				
				}catch (Exception e) {
					System.out.println("Error Occured While retriving data from solr.");
					e.printStackTrace();
				}
				
			}
	 
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return itemList;
}


/**
 *  Searches Items on Part Number using Exact or Wild chars
 * @param queryString
 * @param start
 * @param resultPerPage
 * @param narrowKeyWord
 * @return
 */
static ArrayList<ProductsModel> advancePartNumberSearch(String queryString, int start, int resultPerPage, String narrowKeyWord, 
	
	String fqNav, String sortField, ORDER sortOrder,boolean exactSearch) {
	
	String searchKeyword = queryString;
	String keyWord = buildKeyword(queryString);
	
	if(exactSearch)
		keyWord = buildExactKeyword(queryString);
	
	queryString = keyWord;
	
	ArrayList<ProductsModel> itemList = new ArrayList<ProductsModel>();
	HttpSolrServer server = null;
	try {
		server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
	

		int resultCount = 0;
		
		
			query.setQuery("partnumbersearch:" + queryString);
		
			
			if(sortField != null && sortOrder != null){
				query.addSortField(sortField, sortOrder);
			} else {
				query.addSortField("partnumber", SolrQuery.ORDER.asc);
			}
			
			
			if(narrowKeyWord != null && narrowKeyWord.trim().length() > 0){
				
				narrowKeyWord = escapeQueryCulprits(narrowKeyWord.trim());
				query.setFilterQueries(new String[]{fqNav, narrowKeyWord, "defaultCategory:Y"});
			} else {
				query.setFilterQueries(new String[]{fqNav, "defaultCategory:Y"});
			}
			
			query.setStart(start);
			query.setRows(resultPerPage);
			if(!siteNameSolr.equalsIgnoreCase(""))
            {
            	query.set("customerName",siteNameSolr);
            	query.set("q.original",searchKeyword);
            }
			
			QueryResponse response = server.query(query);
			System.out.println("MPN  Query : " + query);
			SolrDocumentList documents = response.getResults();
			resultCount = (int) response.getResults().getNumFound();
			
			if(resultCount < 1 && !exactSearch) {
				
				query.setQuery("partnumbersearch:" + queryString + "*");
				
				
				if(sortField != null && sortOrder != null){
					query.addSortField(sortField, sortOrder);
				} else {
					query.addSortField("partnumber", SolrQuery.ORDER.asc);
				}
				
				
				if(narrowKeyWord != null && narrowKeyWord.trim().length() > 0){
					
					narrowKeyWord = escapeQueryCulprits(narrowKeyWord);
					query.setFilterQueries(new String[]{fqNav, narrowKeyWord, "defaultCategory:Y"});
				} else {
					query.setFilterQueries(new String[]{fqNav, "defaultCategory:Y"});
				}
				
				query.setStart(start);
				query.setRows(resultPerPage);
				
				response = server.query(query);
				System.out.println("MPN  Query : " + query);
				documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				
			}
			
			Iterator<SolrDocument> itr = documents.iterator();
			
			System.out.println("Total Reulsts Found : " + resultCount);
			
			while (itr.hasNext()) {
				
				try{
				
					ProductsModel itemModel = new ProductsModel();
					SolrDocument doc = itr.next();
					
					
					itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
					itemModel.setMinOrderQty(1);
					itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
					itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
					//itemModel.setManufacturerName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
					itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
					itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
					itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
					itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
					itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
					itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
					itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
					itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
					itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
					if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
					{
						if(doc.getFieldValue("productItemCount")!=null){
							itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
						}
					}
					
					itemModel.setResultCount(resultCount);
				
					String imageName = null;
	       			String imageType = null;
	       			
	       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
	       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			}
	       			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	       			String pattern = "[^A-Za-z0-9]";
	   			 itemUrl = itemUrl.replaceAll(pattern," ");
	   			 itemUrl = itemUrl.replaceAll("\\s+","-");
	            	itemModel.setItemUrl(itemUrl);
	       			
	       			if(doc.getFieldValue("imageName")!=null)
	       				imageName = doc.getFieldValue("imageName").toString();
	       			
	       					       			
	       			if(doc.getFieldValue("imageType")!=null)
	       				imageType = doc.getFieldValue("imageType").toString();
	       			if(doc.getFieldValue("upc")!=null)
	       				itemModel.setUpc(doc.getFieldValue("upc").toString());	
	       			
	       			if(imageName==null)
	       			{
	       				imageName = "NoImage.png";
	       				imageType = "IMAGE";
	       			}
	       			itemModel.setImageName(imageName.trim());
	       			itemModel.setImageType(imageType);
	       			
				
	       			itemList.add(itemModel);
       			
				}catch (Exception e) {
					System.out.println("Error Occured While retriving data from solr.");
					e.printStackTrace();
				}	
				
				
			}
	 
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	
	return itemList;
	
	
}


/**
* 
* To search CPN Matches for File Upload feature
* @param queryString
* @param start
* @param resultPerPage
* @param subsetId
* @param buyingCompanyId
* @return Item List 
*/
public static ArrayList<ProductsModel>  customerPartNumberSearch(String queryString, int start, int resultPerPage,  int subsetId, int buyingCompanyId){
	
	String keyWord = buildKeyword(queryString);
	keyWord = escapeQueryCulprits(queryString);
//	keyWord = keyWord.toUpperCase();
	queryString = keyWord;
	
	 String indexType = "PH_SEARCH_"+subsetId;
	// String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
	 
	 String idList = "";
	
	ArrayList<ProductsModel> itemList = new ArrayList<ProductsModel>();
	HttpSolrServer server = null;
	HttpSolrServer server1 = null;
	try {
		server = ConnectionManager.getSolrClientConnection(solrURL+"/customerdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
		
		
		int resultCount = 0;
		
		
			query.setQuery("customerPartNumberKeyword:" + queryString);
			query.setFilterQueries(new String[]{"buyingCompanyId:"+buyingCompanyId});
			query.setStart(start);
			query.setRows(resultPerPage);
			
			QueryResponse response = server.query(query);
			System.out.println("CPN  Query : " + query);
			SolrDocumentList documents = response.getResults();
			resultCount = (int) response.getResults().getNumFound();
			
			
			
			Iterator<SolrDocument> itr = documents.iterator();
			
			System.out.println("Total Reulsts Found : " + resultCount);
			
			while (itr.hasNext()) {
				
				try{
				
					ProductsModel itemModel = new ProductsModel();
					SolrDocument doc = itr.next();
					
					
					itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
					itemModel.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
					
					idList += doc.getFieldValue("itemid").toString() + " ";
					
					itemList.add(itemModel);
					
					
				}catch (Exception e) {
					System.out.println("Error Occured While retriving data from solr.");
					e.printStackTrace();
				}
				
				
				server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
				server1.setParser(new XMLResponseParser());
				QueryRequest req1 = new QueryRequest();
				req1.setMethod(METHOD.POST);
				SolrQuery query1 = new SolrQuery();
				
				query1.setQuery("itemid:("+idList+")");
				String fq2 = "type:"+indexType;
				query1.setFilterQueries(new String[]{fq2});
				
				query1.setStart(0);
				query1.setRows(itemList.size());
				
				
				QueryResponse response1 = server1.query(query1);
				
				SolrDocumentList documents1 = response1.getResults();
				resultCount = (int) response1.getResults().getNumFound();
				

				
				Iterator<SolrDocument> itr1 = documents1.iterator();
			
				while (itr1.hasNext()) {
					
					SolrDocument doc = itr1.next();
					for(ProductsModel iList : itemList)
					{
						int itemId = CommonUtility.validateNumber(doc.getFieldValue("itemid").toString());
						
						
					
						if(itemId==iList.getItemId())
						{
							
							int minOrdQty = 1;
							int orderQtyInterval = 1;
							int saleQty = 1;
							String iPrice ="";
							if(doc.getFieldValue("price")!=null)
							{
								iPrice = doc.getFieldValue("price").toString();
							}
								
							
							double itemPrice = 0d;
							if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
							{
								itemPrice = Double.parseDouble(iPrice);
							}
							
							iList.setPrice(itemPrice);
							if(doc.getFieldValue("uom")!=null)
							iList.setUom(doc.getFieldValue("uom").toString());
							iList.setItemPriceId(CommonUtility.validateNumber(doc.getFieldValue("itemPriceId").toString()));
							
							
							if(doc.getFieldValue("minordqty")!=null)
							{
								if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
									minOrdQty = CommonUtility.validateNumber(doc.getFieldValue("minordqty").toString());
								if(minOrdQty==0)
									minOrdQty = 1;
							}
							if(doc.getFieldValue("orderQtyInterval")!=null)
							{
								if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
									orderQtyInterval = CommonUtility.validateNumber(doc.getFieldValue("orderQtyInterval").toString());
								if(orderQtyInterval==0)
									orderQtyInterval = 1;
							}
							if(doc.getFieldValue("salesQty")!=null)
							{
								if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
									saleQty = CommonUtility.validateNumber(doc.getFieldValue("salesQty").toString());
								
								if(saleQty==0)
									saleQty = 1;
							}
							
							
							
							if(doc.getFieldValue("displayPricing") != null){
								
								iList.setDisplayPricing(doc.getFieldValue("displayPricing").toString());
							}
							if(doc.getFieldValue("clearanceFlag") != null){
								
								iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
							}
							
							iList.setPartNumber(queryString);
							
							iList.setSaleQty(saleQty);
							iList.setMinOrderQty(minOrdQty);
							iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
							
							
							
							
						}
					}
				}
				
					
			}
	 
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
		ConnectionManager.closeSolrClientConnection(server1);
	}
return itemList;
	
}
public static ArrayList<ProductsModel> myProductGroupSearch(String keyWord,int savedGroupId,int userId,int subsetId,int generalSubsetId,int fromRow,int resultPerPage,String sortBy, String eclipseSessionId, String entityId, String homeTeritory, String type,String userName)
{
	
	ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
	HttpSolrServer server1 = null;
	try
	{
		boolean isPromotion=false;
		boolean isExcludeItems=false;
		String elevateIds=null;
		String excludeItems=null;
		BannerEntity bannerEntity=null;
		boolean customerPartnumberSearch = true;
		if(keyWord==null)
			keyWord = "";
		
		if(keyWord.trim().equalsIgnoreCase(""))
		{
			keyWord = "*";
			customerPartnumberSearch = false;
		}
		
		String origKeyWord = keyWord.toUpperCase();
		String idList = "";
		String c = "";
		 String attrFtr[] = null;
		 String attrFtr1[] = null;
		
		String faucetField[] = null;
		 String validateForSearch[] = keyWord.split("\\s+",-1);
		 Pattern p = Pattern.compile("[^a-zA-Z0-9]");
		 Pattern p1 = Pattern.compile("[\\d]");
		 boolean includePartNumberSearch = false;
		 String singleKeyword = "";
		 String multiKeyword = "";
		 String multiKeywordWildCard = "";
		 String multiKeywordOr = "";
		 String pattern = "[^A-Za-z0-9]";
		 int resultCount = 0;
		 String indexType = "PH_SEARCH_"+subsetId;
		 if(generalSubsetId>0 && generalSubsetId!=subsetId)
			 indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubsetId+")";
		
		 String sortField = "externalHits";
			ORDER sortOrder = SolrQuery.ORDER.desc;
			
				sortField = "score";
				sortOrder = SolrQuery.ORDER.desc;
			
			
			if(sortBy!=null && !sortBy.trim().equalsIgnoreCase(""))
			{

				if(sortBy.trim().equalsIgnoreCase("MPNA"))
				{
					sortField = "manfpartnumber";
					sortOrder = SolrQuery.ORDER.asc;
				}
				else if(sortBy.trim().equalsIgnoreCase("MPND"))
				{
					sortField = "manfpartnumber";
					sortOrder = SolrQuery.ORDER.desc;
				}
				else if(sortBy.trim().equalsIgnoreCase("PartA"))
				{
					sortBy = "partnumber";
					sortOrder = SolrQuery.ORDER.asc;
				}
				else if(sortBy.trim().equalsIgnoreCase("PartD"))
				{
					sortField = "partnumber";
					sortOrder = SolrQuery.ORDER.desc;
				}
				else if(sortBy.trim().equalsIgnoreCase("ProdNameA"))
				{
					sortField = "manfpartnumber";
					sortOrder = SolrQuery.ORDER.asc;
				}
				else if(sortBy.trim().equalsIgnoreCase("ProdNameB"))
				{
					sortField = "manfpartnumber";
					sortOrder = SolrQuery.ORDER.desc;
				}
				else if(sortBy.trim().equalsIgnoreCase("UpcA"))
				{
					sortField = "upc";
					sortOrder = SolrQuery.ORDER.asc;
				}
				
				else if(sortBy.trim().equalsIgnoreCase("UpcD"))
				{
					sortField = "upc";
					sortOrder = SolrQuery.ORDER.desc;
				}
		
				
			}
			
			String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			if(!siteNameSolr.equalsIgnoreCase(""))
			{
				fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			}
			 String sFq = "defaultCategory:Y";
			 String getIdList = "itemid:("+getProductGroupIdList(subsetId, generalSubsetId, userId, savedGroupId)+")";
			 String aFilter = sFq + "~" +fq+"~"+getIdList;
			 attrFtr = aFilter.split("~");
				/**
 			 *Below code Written is for Tyndale *Reference- Chetan Sandesh
 			 */
			 String changeRequestHandler = (String)CommonDBQuery.getSystemParamtersList().get("CHANGE_REQUEST_HANDLER");
			 if(CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y")){
					includePartNumberSearch = true;
					if(validateForSearch!=null && validateForSearch.length>1){
						singleKeyword = keyWord;
					}else{
						singleKeyword = "partnumbersearch:*"+keyWord+"* OR "+keyWord;
					}
			 }else {
			 String separator = "";
			 for(String keyBuilder:validateForSearch)


			 {
				 boolean hasSpecialChar = p1.matcher(keyBuilder.trim()).find();
				 if(keyBuilder.matches("[-+]?\\d*\\.?\\d+"))





				 {
					 includePartNumberSearch = true;
				 }
				 else
				 {

					 if(hasSpecialChar)
					 {
						 includePartNumberSearch = true;
					 }
					 else
					 {
						 hasSpecialChar = p.matcher(keyBuilder.trim()).find();
						 if(hasSpecialChar)
						 {
							 includePartNumberSearch = true;
						 }
					 }
					 
				 }
				 
				 if(includePartNumberSearch)
				 {
					 multiKeyword = multiKeyword + separator + buildKeyword(keyBuilder);
					 separator = " ";
				 }
				 includePartNumberSearch = false;
			 }
			 
			multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord) + multiKeyword;
			multiKeyword = escapeQueryCulpritsWithoutWhiteSpace(keyWord);
			
			multiKeywordOr = multiKeyword;
			multiKeywordWildCard = multiKeyword;
			multiKeyword = multiKeyword.replaceAll("\\s+", " AND ");
			multiKeywordWildCard = "*"+multiKeywordWildCard.replaceAll("\\s+", "* AND *")+"*";
			System.out.println("multiKeyword Query Keyword : " + multiKeyword);
			System.out.println("multiKeyword Query Keyword : " + multiKeywordWildCard);
		 }
		 /*else
		 {

			 includePartNumberSearch = true;
			 singleKeyword = "("+buildKeyword(keyWord)+")";
			 System.out.println("Single Query Keyword : " + singleKeyword);
			 
		 
		 }*/
		 keyWord = keyWord.toUpperCase();
	  		String scrubbedKeyword = keyWord.replaceAll(pattern,"");
		 String keyWordWithOutSpace = keyWord.trim();
		 keyWord = keyWord.trim();
		 keyWord = escapeQueryCulprits(keyWord);
		 keyWordWithOutSpace = escapeQueryCulpritsWithoutWhiteSpace(keyWordWithOutSpace);
		 
		 
		 //1.	Customer part number exact OR Customer part number squashed (all special characters removed)
		 String narrowSearchKey = null;
		 String queryString  = singleKeyword;
		 String connUrl = solrURL +"/customerdata";
		 String connUrlSub = solrURL+"/mainitemdata";
		 ProductsModel searchResultData = new ProductsModel();
		 
		/* if(keyWord.trim().equalsIgnoreCase(scrubbedKeyword))
		 queryString  = "customerPartNumberKeyword:("+keyWord+" OR "+scrubbedKeyword+"*)";
		 */
		 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("INCLUDE_ATTRFTR")).equalsIgnoreCase("Y")){
			 request =ServletActionContext.getRequest();
			 HttpSession session = request.getSession();
			 String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		 	 int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
			 String cFq = "buyingCompanyId:"+buyingCompanyId;
			 attrFtr1 = cFq.split("~");
		} 

		 if(includePartNumberSearch && customerPartnumberSearch)
		 {
			 System.out.println("Include Part Number Search");
			 
			/* if(narrowKeyword!=null && !narrowKeyword.trim().equalsIgnoreCase(""))
			 {
				 narrowSearchKey = "customerPartNumberKeyword:"+narrowKeyword+"*";
			 }*/

			 
			 searchResultData = ProductHunterSolrUltimate.solrSearchResultCustomerPartNumber( narrowSearchKey, queryString, connUrl, attrFtr, attrFtr1, fromRow, resultPerPage,connUrlSub,faucetField);
			 itemLevelFilterData = searchResultData.getItemDataList();
			 
			 if(itemLevelFilterData.size()>0)
			 {
				
				 idList = searchResultData.getIdList();
				
			 }
			 
			//3.	Customer part number % exact % OR customer part number squashed%
			 if(itemLevelFilterData.size()<1)
			 {
				 narrowSearchKey = null;
				 queryString  = "customerPartNumberKeyword:(*"+keyWord+"* OR *"+scrubbedKeyword+"*)";
				 connUrl = solrURL+"/customerdata";
				 connUrlSub = solrURL+"/mainitemdata";
				 searchResultData = new ProductsModel();
	
				 
				 searchResultData = ProductHunterSolrUltimate.solrSearchResultCustomerPartNumber( narrowSearchKey, queryString, connUrl, attrFtr, attrFtr1, fromRow, resultPerPage,connUrlSub,faucetField);
				 itemLevelFilterData = searchResultData.getItemDataList();
				 if(itemLevelFilterData.size()>0)
				 {
					
					 idList = searchResultData.getIdList();
					
				 }
				 
			 }
			 
			 
				
		

		 }
		 //Keword exact pharse
		 if(itemLevelFilterData.size()<1)
		 {
			narrowSearchKey = null;
			if(includePartNumberSearch)
			 queryString  = singleKeyword;
			else
				queryString = multiKeyword;
			 connUrl = solrURL+"/mainitemdata";
			 connUrlSub = solrURL+"/mainitemdata";
			 searchResultData = new ProductsModel();
			 
			
			 
			 searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,includePartNumberSearch,false,itemLevelFilterData,idList,null,fq,null,false,false,false,true,"", isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);	
			 itemLevelFilterData = searchResultData.getItemDataList();
			/* if(suggestedValue==null)
				 suggestedValue = searchResultData.getSuggestedValue();System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());*/

			 if(itemLevelFilterData.size()>0)
			 {
				 idList = searchResultData.getIdList();
				 
			 }
			
		 }
		 
		 
		 if(itemLevelFilterData.size()<1)
		 {
			narrowSearchKey = null;
			if(!includePartNumberSearch)
			{
				queryString = multiKeywordWildCard;
				
				 connUrl = solrURL+"/mainitemdata";
				 connUrlSub = solrURL+"/mainitemdata";
				 searchResultData = new ProductsModel();
				 
				 
				 
				 searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,includePartNumberSearch,false,itemLevelFilterData,idList,null,fq,null,true,false,false,true,"", isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				 itemLevelFilterData = searchResultData.getItemDataList();
				/* if(suggestedValue==null)
					 suggestedValue = searchResultData.getSuggestedValue();System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());
*/
				 if(itemLevelFilterData.size()>0)
				 {
					/* exactMatchNotFound = "Y";
					 ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();*/
					 idList = searchResultData.getIdList();
					/* searchResultList.put("attrList", attrFilteredList );*/
				 }
			}
			
			
		 }
		 
		 if(itemLevelFilterData.size()<1)
		 {
			narrowSearchKey = null;
			if(!includePartNumberSearch)
			{
				queryString = multiKeywordOr;
				
				 connUrl = solrURL+"/mainitemdata";
				 connUrlSub = solrURL+"/mainitemdata";
				 searchResultData = new ProductsModel();
				 
				 
				 
				 searchResultData = ProductHunterSolrUltimate.solrSearchResult(narrowSearchKey, queryString, connUrl, attrFtr, fromRow, resultPerPage,connUrlSub,faucetField,origKeyWord,sortField,sortOrder,includePartNumberSearch,false,itemLevelFilterData,idList,null,fq,null,false,false,false,true,"", isPromotion,  isExcludeItems,  elevateIds,  excludeItems, bannerEntity,false,null);
				 itemLevelFilterData = searchResultData.getItemDataList();
				/* if(suggestedValue==null)
					 suggestedValue = searchResultData.getSuggestedValue();System.out.println("Suggtion Set : " + suggestedValue +" - " + searchResultData.getSuggestedValue());
*/
				 if(itemLevelFilterData.size()>0)
				 {
					/* exactMatchNotFound = "Y";
					 ArrayList<ProductsModel> attrFilteredList = searchResultData.getAttributeDataList();*/
					 idList = searchResultData.getIdList();
					/* searchResultList.put("attrList", attrFilteredList );*/
				 }
			}
			
			
		 }
		 
		 
		 
			//Keword exact OR
		 
		 if(itemLevelFilterData.size()>0)
			{
			 LinkedHashMap<String, LinkedHashMap<Integer,Integer>> productItem = getProductGroupQty(savedGroupId, idList);
		     LinkedHashMap<Integer, Integer> productItemQty = productItem.get("productQty");
		     LinkedHashMap<Integer, Integer> productItemListId=productItem.get("productItemListId");
			 	server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
				server1.setParser(new XMLResponseParser());
				QueryRequest req1 = new QueryRequest();
				req1.setMethod(METHOD.POST);
				SolrQuery query1 = new SolrQuery();
				if(generalSubsetId>0 && generalSubsetId!=subsetId) {
					indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubsetId+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
					if(!siteNameSolr.equalsIgnoreCase(""))
					{
						indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubsetId+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
					}
                }

				query1.setQuery("itemid:("+idList+")");
				String fq2 = "type:"+indexType;
				query1.setFilterQueries(fq2);
				query1.setStart(0);
				query1.setRows(resultPerPage);
				
				QueryResponse response1 = server1.query(query1);
				
				SolrDocumentList documents1 = response1.getResults();
				resultCount = (int) response1.getResults().getNumFound();
				

				
				Iterator<SolrDocument> itr1 = documents1.iterator();
				NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
				ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
				String sep = "";
		       	String innovoPartList = "";
				while (itr1.hasNext()) {
					SolrDocument doc = itr1.next();
					for(ProductsModel iList : itemLevelFilterData)
					{
						int itemId = CommonUtility.validateNumber(doc.getFieldValue("itemid").toString());
						
						
					
						if(itemId==iList.getItemId())
						{
							if(productItemQty!=null)
							{
								iList.setQty(productItemQty.get(itemId));
							}
							if(productItemListId!=null)
							{
								iList.setProductListId(productItemListId.get(itemId));
							}
							ProductsModel partIdentifiers = new ProductsModel();
			       	        partIdentifiers.setPartNumber(iList.getPartNumber());
			       	        partIdentifiersList.add(partIdentifiers);
			       	        innovoPartList = innovoPartList + sep + iList.getPartNumber();
			       	        sep = ",";
							int minOrdQty = 1;
							int orderQtyInterval = 1;
							int saleQty = 1;
							int packageQty = 1;
							int packageFlag = 0;
							String mOrdQty = null;
							String iPrice ="";
							
							if(iList.getPackageQty()>0){
								packageQty = iList.getPackageQty();
							}
							
							if(doc.getFieldValue("price")!=null)
							{
								iPrice = doc.getFieldValue("price").toString();
							}
								
							
							double itemPrice = 0d;
							if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
							{
								itemPrice = Double.parseDouble(iPrice);
							}
							iList.setPrice(itemPrice);
							iList.setUnitPrice(itemPrice);
							
							double extPrice = 0d;
							if(iList.getQty()>0 && itemPrice>0){
								extPrice = (itemPrice*iList.getQty());
							}
							iList.setTotal(extPrice);
							
							if(doc.getFieldValue("uom")!=null)
							iList.setUom(doc.getFieldValue("uom").toString());
							iList.setItemPriceId(CommonUtility.validateNumber(doc.getFieldValue("itemPriceId").toString()));
							
							
							
														
							if(doc.getFieldValue("customerPartNumber")!=null)
								iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
							if(doc.getFieldValue("eclipseitem")!=null)
								iList.setIsEclipseItem(doc.getFieldValue("eclipseitem").toString());
							
							if(doc.getFieldValue("materialGroup")!=null)
								iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
							
							if(doc.getFieldValue("minordqty")!=null)
							{
								if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
									minOrdQty = CommonUtility.validateNumber(doc.getFieldValue("minordqty").toString());
							}
							if(doc.getFieldValue("orderQtyInterval")!=null)
							{
								if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
									orderQtyInterval = CommonUtility.validateNumber(doc.getFieldValue("orderQtyInterval").toString());
							}
							if(doc.getFieldValue("salesQty")!=null)
							{
								if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
									saleQty = CommonUtility.validateNumber(doc.getFieldValue("salesQty").toString());
							}
							
							if(doc.getFieldValue("packageFlag")!=null)
							{
								if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
									packageFlag = CommonUtility.validateNumber(doc.getFieldValue("packageFlag").toString());
							}
							
							if(doc.getFieldValue("packageQty")!=null)
							{
								if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
								{
									packageQty = CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString());
								}
								
							}
							if(doc.getFieldValue("overRidePriceRule")!=null){
								iList.setOverRidePriceRule((String)doc.getFieldValue("overRidePriceRule"));
							}
							
							if(doc.getFieldValue("packDesc")!=null)
							{
								String packDesc = doc.getFieldValue("packDesc").toString();
								iList.setPackDesc(packDesc);
							}
							iList.setPackageFlag(packageFlag);
							iList.setPackageQty(packageQty);
							iList.setSaleQty(saleQty);
							if(minOrdQty>0){
								iList.setMinOrderQty(minOrdQty);	
							}else{
								iList.setMinOrderQty(1);
							}
							iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
						}
					}
				}
				
	              
		        	 
		      
			}
	
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server1);
	}
	return itemLevelFilterData;
}

public static String getProductGroupIdList(int subsetId,int generalSubsetId,int userId,int savedGroupId)
{
	String sql = " SELECT XMLELEMENT(\"ProductList\",XMLAGG(XMLELEMENT(\"ItemId\",item_id) ORDER BY item_id)).getClobVal() as PRODUCT_GROUP_ITEM FROM( SELECT SLI.ITEM_ID FROM SAVED_LIST_ITEMS SLI,ITEM_DETAILS_MV IDMV WHERE SAVED_LIST_ID = ? AND IDMV.ITEM_ID = SLI.ITEM_ID and subset_id = ? UNION SELECT SLI.ITEM_ID FROM SAVED_LIST_ITEMS SLI,ITEM_DETAILS_MV IDMV WHERE SAVED_LIST_ID = ? AND IDMV.ITEM_ID = SLI.ITEM_ID and SUBSET_ID = ? AND IDMV.ITEM_ID NOT IN (SELECT SLI.ITEM_ID FROM SAVED_LIST_ITEMS SLI, ITEM_DETAILS_MV IDMV WHERE SLI.SAVED_LIST_ID = ? AND IDMV.ITEM_ID = SLI.ITEM_ID AND IDMV.SUBSET_ID =? ))";
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String generatedList = "";
	String idList = "";
	try
	{
		
		conn = ConnectionManager.getDBConnection();
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, savedGroupId);
		pstmt.setInt(2, subsetId);
		pstmt.setInt(3, savedGroupId);
		pstmt.setInt(4, generalSubsetId);
		pstmt.setInt(5, savedGroupId);
		pstmt.setInt(6, subsetId);
		rs = pstmt.executeQuery();
		
		if(rs.next())
		{
			int columnIndex=1;
			generatedList = rs.getString("PRODUCT_GROUP_ITEM");
			JAXBContext jaxbContext = JAXBContext.newInstance(ProductGroupModel.class);
			//File file = new File("D:\\file.xml");
			if(generatedList!=null && !generatedList.trim().equalsIgnoreCase(""))
			{
			StringReader reader = new StringReader(generatedList);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ProductGroupModel customer = (ProductGroupModel) jaxbUnmarshaller.unmarshal(reader);
			idList = StringUtils.join(customer.getList(), " OR ");
			
			}
		}
		
	
		
	}
	catch (Exception e) {
		e.printStackTrace();
	}finally
    {
		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);	
		ConnectionManager.closeDBConnection(conn);
    }
	
	
	return idList;
}


public static LinkedHashMap<Integer, LinkedHashMap<String, ProductsModel>> getshipOrderQtyAndIntervalByItems(int subsetId,int generalSubsetId,String itemList)
{
	
	LinkedHashMap<Integer, LinkedHashMap<String, ProductsModel>> shipOrderQtyAndIntervalByItem = new LinkedHashMap<Integer, LinkedHashMap<String,ProductsModel>>();
	HttpSolrServer server = null;
	try
	{
		
		 String attrFtr[] = null;
		 String indexType = "PH_SEARCH_"+subsetId;
		 if(generalSubsetId>0 && generalSubsetId!=subsetId)
			 indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubsetId+")";
		 String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
		 if(!siteNameSolr.equalsIgnoreCase(""))
		 {
			 fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
		 }
			 String sFq = "defaultCategory:Y";
			 String getIdList = "itemid:(0)";
			 if(itemList!=null && itemList.trim().length()>0){
				 getIdList = "itemid:("+itemList+")";
			 }
			 String aFilter = sFq + "~" +fq+"~"+getIdList;
			 attrFtr = aFilter.split("~");
			 
		
			 	server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
			
		 
		 String queryString  = "*:*";
		 
		 query = new SolrQuery();
		
		 query.setQuery(queryString);
		
			query.setStart(0);
			query.setRows(CommonUtility.validateString(itemList).split(" OR ").length);
			query.setFilterQueries(attrFtr);
			
			
			System.out.println("Ship Order Qty and Interval query : " + query);
			
			QueryResponse response = server.query(query);
			SolrDocumentList documents = response.getResults();
			
			Iterator<SolrDocument> itr = documents.iterator();
			
			while (itr.hasNext()) {
			try{
				SolrDocument doc = itr.next();
				
				if(doc.getFieldValue("orderQtyIntervalByShipMethod")!=null && doc.getFieldValue("orderQtyIntervalByShipMethod").toString().trim().length()>0){
					LinkedHashMap<String, ProductsModel>shipOrderQtyAndInterval =  getOrderQtyIntervalByShipMethod(doc.getFieldValue("orderQtyIntervalByShipMethod").toString());
					if(shipOrderQtyAndInterval!=null && shipOrderQtyAndInterval.size()>0){
						shipOrderQtyAndIntervalByItem.put(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()), shipOrderQtyAndInterval);
					}
				}
				

			}catch (Exception e) {
				System.out.println("Error Occured While retriving data from solr.");
				e.printStackTrace();
			}	
				
			}
	 
	
	}
	catch (Exception e) {
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	
	return shipOrderQtyAndIntervalByItem;

	
}


public static LinkedHashMap<Integer, LinkedHashMap<String, Object>> getCustomFieldValuesByItems(int subsetId,int generalSubsetId,String itemList,String searchBy)
{
	
	LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldList = new LinkedHashMap<Integer, LinkedHashMap<String,Object>>();
	HttpSolrServer server = null;
	try
	{
		
		String finalSearchField = "itemdid";
		if(CommonUtility.validateString(searchBy).length()>0){
			finalSearchField = searchBy;
		}
		 String attrFtr[] = null;
		 String indexType = "PH_SEARCH_"+subsetId;
		 if(generalSubsetId>0 && generalSubsetId!=subsetId)
			 indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubsetId+")";
		 String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
		 if(!siteNameSolr.equalsIgnoreCase(""))
		 {
			 fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
		 }
			 String sFq = "defaultCategory:Y";
			 
			 String getIdList = finalSearchField+":(0)";
			 if(itemList!=null && itemList.trim().length()>0){
				 getIdList = finalSearchField+":("+itemList+")";
			 }
			 String aFilter = sFq + "~" +fq+"~"+getIdList;
			 attrFtr = aFilter.split("~");
			 
		
			    server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
			
		 
		 String queryString  = "*:*";
		 
		 query = new SolrQuery();
		
		 query.setQuery(queryString);
		
			query.setStart(0);
			query.setRows(CommonUtility.validateString(itemList).split(" OR ").length);
			query.setFilterQueries(attrFtr);
			
			
			System.out.println("Custom field query : " + query);
			
			QueryResponse response = server.query(query);
			SolrDocumentList documents = response.getResults();
			
			Iterator<SolrDocument> itr = documents.iterator();
			
			while (itr.hasNext()) {
			try{
				SolrDocument doc = itr.next();
				Map<String, Object> customFieldValMap = doc.getFieldValueMap();
				
				LinkedHashMap<String, Object> customFieldVal = getAllCustomFieldVal(customFieldValMap);
				if(customFieldVal!=null && customFieldVal.size()>0)
				customFieldList.put(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()), customFieldVal);
				}catch (Exception e) {
					System.out.println("Error Occured While retriving data from solr.");
					e.printStackTrace();
				}	
				
			}
	 
	
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return customFieldList;

	
}

public static LinkedHashMap<String, LinkedHashMap<Integer, Integer>>  getProductGroupQty(int savedGroupId,String idList)
{
	
	 LinkedHashMap<Integer, Integer> productQty = new LinkedHashMap<Integer, Integer>();
	 LinkedHashMap<Integer, Integer> productItemListId= new LinkedHashMap<Integer, Integer>();
	 LinkedHashMap<String, LinkedHashMap<Integer, Integer>> productDetails=new  LinkedHashMap<String, LinkedHashMap<Integer, Integer>>();
	if(idList!=null)
		idList = idList.replaceAll("OR", ",");
	String sql = " SELECT ITEM_ID,QTY,LIST_ITEM_ID FROM SAVED_LIST_ITEMS WHERE ITEM_ID IN("+idList+") AND SAVED_LIST_ID = ? ";
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try
	{
		conn = ConnectionManager.getDBConnection();
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, savedGroupId);
		rs = pstmt.executeQuery();
		while(rs.next())
		{
			productQty.put(rs.getInt("ITEM_ID"), rs.getInt("QTY"));
		    productItemListId.put(Integer.valueOf(rs.getInt("ITEM_ID")), Integer.valueOf(rs.getInt("LIST_ITEM_ID")));
		}
		   productDetails.put("productQty", productQty);
		   productDetails.put("productItemListId", productItemListId);
	}
	catch (Exception e) {
		e.printStackTrace();
	}finally
    {
		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);	
		ConnectionManager.closeDBConnection(conn);
    }
	
	
	return productDetails;
}

public static LinkedHashMap<String, Object> getAllCustomFieldVal(Map<String, Object> customFieldValMap)
{
	LinkedHashMap<String, Object> customFieldVal = new LinkedHashMap<String, Object>();
	
	try{
		for (String key: customFieldValMap.keySet()) 
	    {            
			if(key.startsWith("custom_"))
			{
				customFieldVal.put(key, customFieldValMap.get(key));
			}
	        
	    }
	}catch(Exception e){
		e.printStackTrace();
	}
	return customFieldVal;
}


public static LinkedHashMap<String, ProductsModel> getOrderQtyIntervalByShipMethod(String shipQtyFieldValString)
{
	LinkedHashMap<String, ProductsModel> shipQtyFieldValMap = new LinkedHashMap<String, ProductsModel>();
	try{
		if(shipQtyFieldValString!=null && shipQtyFieldValString.trim().length()>0){
			String[] splitString = shipQtyFieldValString.split(";");
			
			if(splitString!=null && splitString.length>0){
				
				for(String shipOrderQtyDetails : splitString){
					
					if(shipOrderQtyDetails!=null && shipOrderQtyDetails.trim().length()>0){
						
						String[] splitQtyDetails = shipOrderQtyDetails.split("\\|");
						
						if(splitQtyDetails!=null && splitQtyDetails.length>0){
							
							ProductsModel shipQtyModel = new ProductsModel();
							
							String shipViaCode = "";
							String shipWareHouse = "";
							String shipMinOrderQty = "";
							String shipOrderQtyInterval = "";
							
							if(splitQtyDetails.length>0)
								shipViaCode = splitQtyDetails[0];
							if(splitQtyDetails.length>1)
								shipWareHouse = splitQtyDetails[1];
							if(splitQtyDetails.length>2)
								shipMinOrderQty = splitQtyDetails[2];
							if(splitQtyDetails.length>3)
								shipOrderQtyInterval = splitQtyDetails[3];
							
							shipQtyModel.setShipViaCode(shipViaCode);
							shipQtyModel.setWareHouseCode(shipWareHouse);
							shipQtyModel.setMinOrderQty(CommonUtility.validateNumber(shipMinOrderQty));
							shipQtyModel.setOrderInterval(CommonUtility.validateNumber(shipOrderQtyInterval));
							
							
							shipQtyFieldValMap.put(shipViaCode.trim().toUpperCase(), shipQtyModel);
						}
					}
					
				}
				
			}
			
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return shipQtyFieldValMap;
}




public static LinkedHashMap<String, ArrayList<ProductsModel>> generateNavigationMenu(String taxonomyId,int rows,boolean allCategory)
{LinkedHashMap<String, ArrayList<ProductsModel>> navigationMenu = new LinkedHashMap<String, ArrayList<ProductsModel>>();
ArrayList<ProductsModel> taxonomyListVal = new ArrayList<ProductsModel>();
HttpSolrServer server = null;
try
{

	LinkedHashMap<String, ArrayList<WordPressMenuModel>> wpCustomMenu = new LinkedHashMap<String, ArrayList<WordPressMenuModel>>();
	List<String> classes = new ArrayList<String>();
	classes.add("");
	classes.add("menu-item");
	classes.add("menu-item-type-custom");
	classes.add("menu-item-object-custom");
	WordPressMenuModel wpMenu = null;
	ArrayList<WordPressMenuModel> wpMenuList = new ArrayList<WordPressMenuModel>();
	int i = 1;
	server = ConnectionManager.getSolrClientConnection(solrURL+"/catalogdata");
	server.setParser(new XMLResponseParser());
	QueryRequest req = new QueryRequest();
	String wpUrl = "";
	req.setMethod(METHOD.POST);
	SolrQuery query = new SolrQuery();
	String fq="-type:PH_SEARCH_0*";
	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
		fq="type:PH_SEARCH_ALL";
	}
	if(!allCategory)
	fq=fq + "~parentCategoryId:0";
	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CATEGORY_SORT_BY")).length()>0){
		String sortBy = CommonDBQuery.getSystemParamtersList().get("CATEGORY_SORT_BY");
		String sortField = "";
		ORDER sortOrder = null;
		if(sortBy!=null && !sortBy.trim().equalsIgnoreCase("")){
			if(sortBy.trim().equalsIgnoreCase("category")){
				sortField = "category";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("category_asc")){
				sortField = "category";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("category_desc")){
				sortField = "category";
				sortOrder = SolrQuery.ORDER.desc;
			}else if(sortBy.trim().equalsIgnoreCase("displaySequence")){
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("displaySequence_asc")){
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.asc;
			}else if(sortBy.trim().equalsIgnoreCase("displaySequence_desc")){
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.desc;
			}
		}else{
			sortField = "displaySeq";
			sortOrder = SolrQuery.ORDER.asc;
		}
		query.addSortField(sortField, sortOrder);
	}
	int resultCount = 0;
	query.setQuery("taxonomyId:"+taxonomyId);
	String attrFtr[] = fq.split("~");
	query.setStart(0);
	query.setRows(rows);
	
	query.setFilterQueries(attrFtr);
	
	QueryResponse response = server.query(query);
	System.out.println("NavigationMenu Query : " + query);
	SolrDocumentList documents = response.getResults();
	resultCount = (int) response.getResults().getNumFound();
	Iterator<SolrDocument> itr = documents.iterator();
	String pattern = "[^A-Za-z0-9]";
	while (itr.hasNext()) {
		
		 wpMenu = new WordPressMenuModel();
		 
		SolrDocument doc = itr.next();
		String subsetIndex = doc.getFieldValue("type").toString();
		taxonomyListVal = navigationMenu.get(subsetIndex);
		wpMenuList = wpCustomMenu.get(subsetIndex);
		ProductsModel nameCode= new ProductsModel();
		String itemUrl = doc.getFieldValue("category").toString();
    
		
		
		 itemUrl = itemUrl.replaceAll(pattern," ");
		 itemUrl = itemUrl.replaceAll("\\s+","-");
		
		 nameCode.setItemUrl(itemUrl);
		 nameCode.setResultCount(resultCount);
    	 
    	 nameCode.setCategoryName(doc.getFieldValue("category").toString());
    	 nameCode.setCategoryCode(doc.getFieldValue("categoryID").toString());
    	 nameCode.setParentCategory((String)doc.getFieldValue("parentCategoryId"));
    	 nameCode.setLevelNumber(CommonUtility.validateNumber(doc.getFieldValue("levelNumber").toString()));
    	 nameCode.setCategoryDesc(CommonUtility.validateString((String)doc.getFieldValue("categoryDesc")));
    	 String imageName = null;
    	 if(doc.getFieldValue("imageName")==null)
    		 imageName = "NoImage.png";
    	 else
    		 imageName = doc.getFieldValue("imageName").toString();
    	 nameCode.setImageName(imageName);
    	 if(doc.getFieldValue("imageType")!=null)
    		 nameCode.setImageType(doc.getFieldValue("imageType").toString());
    	 
		if(taxonomyListVal==null)
		{
			taxonomyListVal = new ArrayList<ProductsModel>();
			taxonomyListVal.add(nameCode);
		}
		else
		{
			taxonomyListVal.add(nameCode);
		}
		if(nameCode.getCategoryName().equalsIgnoreCase("Parts")){
			wpUrl = "/parts-lookup";
		}else{
			wpUrl = "/"+nameCode.getCategoryCode()+"/category/"+itemUrl;
		}
		 
		 wpMenu.setID(Long.parseLong(nameCode.getCategoryCode()));
		 wpMenu.setMenu_item_parent(nameCode.getParentCategory());
		 wpMenu.setUrl(wpUrl);
		 wpMenu.setTitle(nameCode.getCategoryName());
		 wpMenu.setDb_id(Long.parseLong(nameCode.getCategoryCode()));
		 wpMenu.setMenu_order(""+i);
		 wpMenu.setClasses(classes);
		 if(wpMenuList==null){
			 wpMenuList = new ArrayList<WordPressMenuModel>();
		 }
		 wpMenuList.add(wpMenu);
		 wpCustomMenu.put(subsetIndex, wpMenuList);
		navigationMenu.put(subsetIndex, taxonomyListVal);
		i++;
	}
	MenuAndBannersDAO.setWpMenu(wpCustomMenu);
	
}
catch (Exception e) {
	
	e.printStackTrace();
}finally {
	ConnectionManager.closeSolrClientConnection(server);
}
return navigationMenu;
}

public static LinkedHashMap<String, LinkedHashMap<String, ArrayList<MenuAndBannersModal>>> generateNavigationSubMenu(String taxonomyId,int rows)
{
	LinkedHashMap<String, ArrayList<MenuAndBannersModal>> navigationMenu = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
	LinkedHashMap<String, LinkedHashMap<String, ArrayList<MenuAndBannersModal>>> subMenu = new LinkedHashMap<String, LinkedHashMap<String,ArrayList<MenuAndBannersModal>>>();
	ArrayList<MenuAndBannersModal> taxonomyListVal = new ArrayList<MenuAndBannersModal>();
	HttpSolrServer server = null;
	try
	{
		List<String> classes = new ArrayList<String>();
		classes.add("");
		classes.add("menu-item");
		classes.add("menu-item-type-custom");
		classes.add("menu-item-object-custom");
		
		LinkedHashMap<String, ArrayList<WordPressMenuModel>> wpCustomMenu = MenuAndBannersDAO.getWpMenu();
		if(wpCustomMenu==null){
			wpCustomMenu = new LinkedHashMap<String, ArrayList<WordPressMenuModel>>();
			MenuAndBannersDAO.setWpMenu(wpCustomMenu);
		}
		
		WordPressMenuModel wpMenu = null;
		ArrayList<WordPressMenuModel> wpMenuList = new ArrayList<WordPressMenuModel>();
		String wpUrl = "";
		server = ConnectionManager.getSolrClientConnection(solrURL+"/catalogdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
		String fq="-type:PH_SEARCH_0*";
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
			fq="type:PH_SEARCH_ALL";
		}
		fq=fq + "~-parentCategoryId:0";
		int resultCount = 0;
		query.setQuery("taxonomyId:"+taxonomyId);
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CATEGORY_SORT_BY")).length()>0){
			String sortBy = CommonDBQuery.getSystemParamtersList().get("CATEGORY_SORT_BY");
			String sortField = "";
			ORDER sortOrder = null;
			if(sortBy!=null && !sortBy.trim().equalsIgnoreCase("")){
				if(sortBy.trim().equalsIgnoreCase("category")){
					sortField = "category";
					sortOrder = SolrQuery.ORDER.asc;
				}else if(sortBy.trim().equalsIgnoreCase("category_asc")){
					sortField = "category";
					sortOrder = SolrQuery.ORDER.asc;
				}else if(sortBy.trim().equalsIgnoreCase("category_desc")){
					sortField = "category";
					sortOrder = SolrQuery.ORDER.desc;
				}else if(sortBy.trim().equalsIgnoreCase("displaySequence")){
					sortField = "displaySeq";
					sortOrder = SolrQuery.ORDER.asc;
				}else if(sortBy.trim().equalsIgnoreCase("displaySequence_asc")){
					sortField = "displaySeq";
					sortOrder = SolrQuery.ORDER.asc;
				}else if(sortBy.trim().equalsIgnoreCase("displaySequence_desc")){
					sortField = "displaySeq";
					sortOrder = SolrQuery.ORDER.desc;
				}
			}else{
				sortField = "displaySeq";
				sortOrder = SolrQuery.ORDER.asc;
			}
			query.addSortField(sortField, sortOrder);
		}
		String attrFtr[] = fq.split("~");
		query.setStart(0);
		query.setRows(rows);
		
		query.setFilterQueries(attrFtr);
		
		QueryResponse response = server.query(query);
		System.out.println("NavigationMenu Query : " + query);
		SolrDocumentList documents = response.getResults();
		resultCount = (int) response.getResults().getNumFound();
		Iterator<SolrDocument> itr = documents.iterator();
		
		int i = rows + 1;
		
		
		while (itr.hasNext()) {
			
			wpMenu = new WordPressMenuModel();
			SolrDocument doc = itr.next();
			String subsetIndex = doc.getFieldValue("type").toString();
			String parentId = doc.getFieldValue("parentCategoryId").toString();
			
			wpMenuList = MenuAndBannersDAO.getWpMenu().get(subsetIndex);
			navigationMenu = subMenu.get(subsetIndex);
			if(navigationMenu!=null && navigationMenu.size()>0){
				taxonomyListVal = navigationMenu.get(parentId);
			}else{
				taxonomyListVal = null;
			}
			MenuAndBannersModal nameCode= new MenuAndBannersModal();
			String itemUrl = doc.getFieldValue("category").toString();
        
			String pattern = "[^A-Za-z0-9]";
			 itemUrl = itemUrl.replaceAll(pattern," ");
			 itemUrl = itemUrl.replaceAll("\\s+","-");
			 nameCode.setItemUrl(itemUrl);
			 
        	 
        	 nameCode.setCategoryName(doc.getFieldValue("category").toString());
        	 nameCode.setCategoryCode(doc.getFieldValue("categoryID").toString());
        	 nameCode.setLevelNumber(CommonUtility.validateNumber(doc.getFieldValue("levelNumber").toString()));
        	 String imageName = null;
        	 if(doc.getFieldValue("imageName")==null)
        		 imageName = "NoImage.png";
        	 else
        		 imageName = doc.getFieldValue("imageName").toString();
        	 nameCode.setImageName(imageName);
        	 if(doc.getFieldValue("imageType")!=null)
        		 nameCode.setImageType(doc.getFieldValue("imageType").toString());
        	 
			if(taxonomyListVal==null)
			{
				taxonomyListVal = new ArrayList<MenuAndBannersModal>();
				taxonomyListVal.add(nameCode);
			}
			else
			{
				taxonomyListVal.add(nameCode);
			}
			if(navigationMenu==null){
				navigationMenu = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
			}
			navigationMenu.put(parentId, taxonomyListVal);
			subMenu.put(subsetIndex, navigationMenu);
			wpUrl = "/"+nameCode.getCategoryCode()+"/category/"+itemUrl;
			 wpMenu.setID(Long.parseLong(nameCode.getCategoryCode()));
			 wpMenu.setMenu_item_parent(parentId);
			 wpMenu.setUrl(wpUrl);
			 wpMenu.setDb_id(Long.parseLong(nameCode.getCategoryCode()));
			 wpMenu.setTitle(nameCode.getCategoryName());
			 wpMenu.setMenu_order(""+i);
			 wpMenu.setClasses(classes);
			 if(wpMenuList==null){
				 wpMenuList = new ArrayList<WordPressMenuModel>();
				 if(!wpMenu.getMenu_item_parent().equalsIgnoreCase("124033")){
					 wpMenuList.add(wpMenu);	 
				 }
			 }else{
				 if(!wpMenu.getMenu_item_parent().equalsIgnoreCase("124033")){
					 wpMenuList.add(wpMenu);
				 }
			 }
			 wpCustomMenu.put(subsetIndex, wpMenuList);
			 
			i++;
		}
		MenuAndBannersDAO.setWpMenu(wpCustomMenu);
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return subMenu;
}


public static LinkedHashMap<String, LinkedHashMap<String, ArrayList<MenuAndBannersModal>>> generateNavigationSubMenuBySubsetList(String taxonomyId,int rows,ArrayList<String> subsetList)
{
	LinkedHashMap<String, ArrayList<MenuAndBannersModal>> navigationMenu = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
	LinkedHashMap<String, LinkedHashMap<String, ArrayList<MenuAndBannersModal>>> subMenu = new LinkedHashMap<String, LinkedHashMap<String,ArrayList<MenuAndBannersModal>>>();
	ArrayList<MenuAndBannersModal> taxonomyListVal = new ArrayList<MenuAndBannersModal>();
	HttpSolrServer server = null;
	try
	{
		List<String> classes = new ArrayList<String>();
		classes.add("");
		classes.add("menu-item");
		classes.add("menu-item-type-custom");
		classes.add("menu-item-object-custom");
		
		LinkedHashMap<String, ArrayList<WordPressMenuModel>> wpCustomMenu = MenuAndBannersDAO.getWpMenu();
		if(wpCustomMenu==null){
			wpCustomMenu = new LinkedHashMap<String, ArrayList<WordPressMenuModel>>();
			MenuAndBannersDAO.setWpMenu(wpCustomMenu);
		}
		
		WordPressMenuModel wpMenu = null;
		ArrayList<WordPressMenuModel> wpMenuList = new ArrayList<WordPressMenuModel>();
		String wpUrl = "";
		server = ConnectionManager.getSolrClientConnection(solrURL+"/catalogdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		
		
	
		
		if(subsetList!=null && subsetList.size()>0){
			for(String subset:subsetList){
				
				SolrQuery query = new SolrQuery();
				String fq="-type:PH_SEARCH_0*";
				fq=fq + "~-parentCategoryId:0";
				fq=fq + "~type:"+subset;
				int resultCount = 0;
				query.setQuery("taxonomyId:"+taxonomyId);
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CATEGORY_SORT_BY")).length()>0){
					String sortBy = CommonDBQuery.getSystemParamtersList().get("CATEGORY_SORT_BY");
					String sortField = "";
					ORDER sortOrder = null;
					if(sortBy!=null && !sortBy.trim().equalsIgnoreCase("")){
						if(sortBy.trim().equalsIgnoreCase("category")){
							sortField = "category";
							sortOrder = SolrQuery.ORDER.asc;
						}else if(sortBy.trim().equalsIgnoreCase("category_asc")){
							sortField = "category";
							sortOrder = SolrQuery.ORDER.asc;
						}else if(sortBy.trim().equalsIgnoreCase("category_desc")){
							sortField = "category";
							sortOrder = SolrQuery.ORDER.desc;
						}else if(sortBy.trim().equalsIgnoreCase("displaySequence")){
							sortField = "displaySeq";
							sortOrder = SolrQuery.ORDER.asc;
						}else if(sortBy.trim().equalsIgnoreCase("displaySequence_asc")){
							sortField = "displaySeq";
							sortOrder = SolrQuery.ORDER.asc;
						}else if(sortBy.trim().equalsIgnoreCase("displaySequence_desc")){
							sortField = "displaySeq";
							sortOrder = SolrQuery.ORDER.desc;
						}
					}else{
						sortField = "displaySeq";
						sortOrder = SolrQuery.ORDER.asc;
					}
					query.addSortField(sortField, sortOrder);
				}
				String attrFtr[] = fq.split("~");
				query.setStart(0);
				query.setRows(rows);
				
				query.setFilterQueries(attrFtr);
				
				QueryResponse response = server.query(query);
				System.out.println("NavigationMenu Query : " + query);
				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				Iterator<SolrDocument> itr = documents.iterator();
				
				int i = rows + 1;
				
				
				while (itr.hasNext()) {
					
					wpMenu = new WordPressMenuModel();
					SolrDocument doc = itr.next();
					String subsetIndex = doc.getFieldValue("type").toString();
					String parentId = doc.getFieldValue("parentCategoryId").toString();
					
					wpMenuList = MenuAndBannersDAO.getWpMenu().get(subsetIndex);
					navigationMenu = subMenu.get(subsetIndex);
					if(navigationMenu!=null && navigationMenu.size()>0){
						taxonomyListVal = navigationMenu.get(parentId);
					}else{
						taxonomyListVal = null;
					}
					MenuAndBannersModal nameCode= new MenuAndBannersModal();
					String itemUrl = doc.getFieldValue("category").toString();
		        
					String pattern = "[^A-Za-z0-9]";
					 itemUrl = itemUrl.replaceAll(pattern," ");
					 itemUrl = itemUrl.replaceAll("\\s+","-");
					 nameCode.setItemUrl(itemUrl);
					 
		        	 
		        	 nameCode.setCategoryName(doc.getFieldValue("category").toString());
		        	 nameCode.setCategoryCode(doc.getFieldValue("categoryID").toString());
		        	 nameCode.setLevelNumber(CommonUtility.validateNumber(doc.getFieldValue("levelNumber").toString()));
		        	 String imageName = null;
		        	 if(doc.getFieldValue("imageName")==null)
		        		 imageName = "NoImage.png";
		        	 else
		        		 imageName = doc.getFieldValue("imageName").toString();
		        	 nameCode.setImageName(imageName);
		        	 if(doc.getFieldValue("imageType")!=null)
		        		 nameCode.setImageType(doc.getFieldValue("imageType").toString());
		        	 
					if(taxonomyListVal==null)
					{
						taxonomyListVal = new ArrayList<MenuAndBannersModal>();
						taxonomyListVal.add(nameCode);
					}
					else
					{
						taxonomyListVal.add(nameCode);
					}
					if(navigationMenu==null){
						navigationMenu = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
					}
					navigationMenu.put(parentId, taxonomyListVal);
					subMenu.put(subsetIndex, navigationMenu);
					wpUrl = "/"+nameCode.getCategoryCode()+"/category/"+itemUrl;
					 wpMenu.setID(Long.parseLong(nameCode.getCategoryCode()));
					 wpMenu.setMenu_item_parent(parentId);
					 wpMenu.setUrl(wpUrl);
					 wpMenu.setDb_id(Long.parseLong(nameCode.getCategoryCode()));
					 wpMenu.setTitle(nameCode.getCategoryName());
					 wpMenu.setMenu_order(""+i);
					 wpMenu.setClasses(classes);
					 if(wpMenuList==null){
						 wpMenuList = new ArrayList<WordPressMenuModel>();
						 if(!wpMenu.getMenu_item_parent().equalsIgnoreCase("124033")){
							 wpMenuList.add(wpMenu);	 
						 }
					 }else{
						 if(!wpMenu.getMenu_item_parent().equalsIgnoreCase("124033")){
							 wpMenuList.add(wpMenu);
						 }
					 }
					 wpCustomMenu.put(subsetIndex, wpMenuList);
					 
					i++;
				}
				
			}
		}
		
		
		
		
		
		MenuAndBannersDAO.setWpMenu(wpCustomMenu);
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return subMenu;
}



public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> generateBrandMenu(int rows)
{
	LinkedHashMap<String, ArrayList<MenuAndBannersModal>> navigationMenu = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
	ArrayList<MenuAndBannersModal> brandList = new ArrayList<MenuAndBannersModal>();
	HttpSolrServer server = null;
	try
	{
		
		server = ConnectionManager.getSolrClientConnection(solrURL+"/brandautocomplete");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
		String fq="-type:PH_SEARCH_0*";
		
		
		int resultCount = 0;
		query.setQuery("*:*");
		String attrFtr[] = fq.split("~");
		query.setStart(0);
		query.setRows(rows);
		query.addSortField("brandSort", SolrQuery.ORDER.asc );
		query.setFilterQueries(attrFtr);
		
		QueryResponse response = server.query(query);
		System.out.println("NavigationMenu Query : " + query);
		SolrDocumentList documents = response.getResults();
		resultCount = (int) response.getResults().getNumFound();
		Iterator<SolrDocument> itr = documents.iterator();
		while (itr.hasNext()) {
			
			
			SolrDocument doc = itr.next();
			String subsetIndex = doc.getFieldValue("indexType").toString();
			brandList = navigationMenu.get(subsetIndex);
			MenuAndBannersModal brandListVal = new MenuAndBannersModal();
			String itemUrl = doc.getFieldValue("displaylabel").toString();
        
			String pattern = "[^A-Za-z0-9]";
			 itemUrl = itemUrl.replaceAll(pattern," ");
			 itemUrl = itemUrl.replaceAll("\\s+","-");
			 brandListVal.setItemUrl(itemUrl);
			 brandListVal.setResultCount(resultCount);
        	 
			 brandListVal.setBrandName(doc.getFieldValue("displaylabel").toString());
			 brandListVal.setBrandId(CommonUtility.validateNumber(doc.getFieldValue("codeId").toString()));
        	
        	 
			if(brandList==null)
			{
				brandList = new ArrayList<MenuAndBannersModal>();
				brandList.add(brandListVal);
			}
			else
			{
				brandList.add(brandListVal);
			}
			
			navigationMenu.put(subsetIndex, brandList);
			
		}
		
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return navigationMenu;
}



public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> generateManufacturerMenuBySubset(int rows,ArrayList<String> subsetList)
{
	LinkedHashMap<String, ArrayList<MenuAndBannersModal>> navigationMenu = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
	ArrayList<MenuAndBannersModal> brandList = new ArrayList<MenuAndBannersModal>();
	HttpSolrServer server = null;
	try
	{
		
		server = ConnectionManager.getSolrClientConnection(solrURL+"/manfautocomplete");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		
		
		
		if(subsetList!=null && subsetList.size()>0){
			for(String subset:subsetList){
				SolrQuery query = new SolrQuery();
				String fq="-type:PH_SEARCH_0*";
				fq=fq + "~type:"+subset;
				
				
				int resultCount = 0;
				query.setQuery("*:*");
				String attrFtr[] = fq.split("~");
				query.setStart(0);
				query.setRows(rows);
				query.addSortField("manufacturerSort", SolrQuery.ORDER.asc );
				query.setFilterQueries(attrFtr);
				
				QueryResponse response = server.query(query);
				System.out.println("ProductHunterSolr.generateManufacturerMenu() Query : " + query);
				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				Iterator<SolrDocument> itr = documents.iterator();
				while (itr.hasNext()) {
					
					
					SolrDocument doc = itr.next();
					String subsetIndex = doc.getFieldValue("indexType").toString();
					brandList = navigationMenu.get(subsetIndex);
					MenuAndBannersModal brandListVal = new MenuAndBannersModal();
					String itemUrl = doc.getFieldValue("displaylabel").toString();
		        
					String pattern = "[^A-Za-z0-9]";
					 itemUrl = itemUrl.replaceAll(pattern," ");
					 itemUrl = itemUrl.replaceAll("\\s+","-");
					 brandListVal.setItemUrl(itemUrl);
					 brandListVal.setResultCount(resultCount);
		        	 
					 brandListVal.setManufacturerName(doc.getFieldValue("displaylabel").toString());
					 brandListVal.setManufacturerId(CommonUtility.validateNumber(doc.getFieldValue("codeId").toString()));
		        	
		        	 
					if(brandList==null)
					{
						brandList = new ArrayList<MenuAndBannersModal>();
						brandList.add(brandListVal);
					}
					else
					{
						brandList.add(brandListVal);
					}
					
					navigationMenu.put(subsetIndex, brandList);
					
				}
			}
		}

	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return navigationMenu;
}

public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> generateBrandMenuBySubset(int rows,ArrayList<String> subsetList)
{
	LinkedHashMap<String, ArrayList<MenuAndBannersModal>> navigationMenu = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
	ArrayList<MenuAndBannersModal> brandList = new ArrayList<MenuAndBannersModal>();
	HttpSolrServer server = null;
	try
	{
		
		server = ConnectionManager.getSolrClientConnection(solrURL+"/brandautocomplete");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		
		if(subsetList!=null && subsetList.size()>0){
			for(String subset:subsetList){
				
				SolrQuery query = new SolrQuery();
				String fq="-type:PH_SEARCH_0*";
				fq=fq + "~indexType:"+subset;
				
				int resultCount = 0;
				query.setQuery("*:*");
				String attrFtr[] = fq.split("~");
				query.setStart(0);
				query.setRows(rows);
				query.addSortField("brandSort", SolrQuery.ORDER.asc );
				query.setFilterQueries(attrFtr);
				
				QueryResponse response = server.query(query);
				System.out.println("NavigationMenu Query : " + query);
				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				Iterator<SolrDocument> itr = documents.iterator();
				while (itr.hasNext()) {
					
					
					SolrDocument doc = itr.next();
					String subsetIndex = doc.getFieldValue("indexType").toString();
					brandList = navigationMenu.get(subsetIndex);
					MenuAndBannersModal brandListVal = new MenuAndBannersModal();
					String itemUrl = doc.getFieldValue("displaylabel").toString();
		        
					String pattern = "[^A-Za-z0-9]";
					 itemUrl = itemUrl.replaceAll(pattern," ");
					 itemUrl = itemUrl.replaceAll("\\s+","-");
					 brandListVal.setItemUrl(itemUrl);
					 brandListVal.setResultCount(resultCount);
		        	 
					 brandListVal.setBrandName(doc.getFieldValue("displaylabel").toString());
					 brandListVal.setBrandId(CommonUtility.validateNumber(doc.getFieldValue("codeId").toString()));
		        	
		        	 
					if(brandList==null)
					{
						brandList = new ArrayList<MenuAndBannersModal>();
						brandList.add(brandListVal);
					}
					else
					{
						brandList.add(brandListVal);
					}
					
					navigationMenu.put(subsetIndex, brandList);
					
				}
			}
		}
		
		
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return navigationMenu;
}


public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> generateManufacturerMenu(int rows)
{
	LinkedHashMap<String, ArrayList<MenuAndBannersModal>> navigationMenu = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
	ArrayList<MenuAndBannersModal> manufacturerList = new ArrayList<MenuAndBannersModal>();
	HttpSolrServer server = null;
	try
	{
		
		server = ConnectionManager.getSolrClientConnection(solrURL+"/manfautocomplete");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();
		String fq="-type:PH_SEARCH_0*";
		
		
		int resultCount = 0;
		query.setQuery("*:*");
		String attrFtr[] = fq.split("~");
		query.setStart(0);
		query.setRows(rows);
		query.addSortField("manufacturerSort", SolrQuery.ORDER.asc );
		query.setFilterQueries(attrFtr);
		
		QueryResponse response = server.query(query);
		System.out.println("ProductHunterSolr.generateManufacturerMenu() Query : " + query);
		SolrDocumentList documents = response.getResults();
		resultCount = (int) response.getResults().getNumFound();
		Iterator<SolrDocument> itr = documents.iterator();
		while (itr.hasNext()) {
			
			
			SolrDocument doc = itr.next();
			String subsetIndex = doc.getFieldValue("indexType").toString();
			manufacturerList = navigationMenu.get(subsetIndex);
			MenuAndBannersModal manufacturerListVal = new MenuAndBannersModal();
			String itemUrl = doc.getFieldValue("displaylabel").toString();
        
			String pattern = "[^A-Za-z0-9]";
			 itemUrl = itemUrl.replaceAll(pattern," ");
			 itemUrl = itemUrl.replaceAll("\\s+","-");
			 manufacturerListVal.setItemUrl(itemUrl);
			 manufacturerListVal.setResultCount(resultCount);
        	 
			 manufacturerListVal.setManufacturerName(doc.getFieldValue("displaylabel").toString());
			 manufacturerListVal.setManufacturerId(CommonUtility.validateNumber(doc.getFieldValue("codeId").toString()));
        	
        	 
			if(manufacturerList==null)
			{
				manufacturerList = new ArrayList<MenuAndBannersModal>();
				manufacturerList.add(manufacturerListVal);
			}
			else
			{
				manufacturerList.add(manufacturerListVal);
			}
			
			navigationMenu.put(subsetIndex, manufacturerList);
			
		}
		
	}
	catch (Exception e) {
		
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
	}
	return navigationMenu;
}
public static ArrayList<ProductsModel> getCustomerAlsoBought(String idList,int subsetId,int generalSubset,int rowSize,int fromRow)
{
	 ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
	 ArrayList<ProductsModel> itemLevelFilterDataTemp = new ArrayList<ProductsModel>();
	 HttpSolrServer server = null;
	 HttpSolrServer server1 = null;
	 try
	 {
		 String indexType = "PH_SEARCH_"+subsetId;
			
		 if(generalSubset>0 && generalSubset!=subsetId)
			 indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
		 	server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			
			 String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
			 if(!siteNameSolr.equalsIgnoreCase(""))
			 {
				 fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
			 }
		 query = new SolrQuery();
			
			query.setQuery("itemid:("+idList+")");
			//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
			
			
			query.setStart(fromRow);
			query.setRows(rowSize);
			query.setFilterQueries(fq);
			
			System.out.println("Navigation query : " + query);
			
			QueryResponse response = server.query(query);
			
			
			
			SolrDocumentList documents = response.getResults();
			int resultCount = (int) response.getResults().getNumFound();
			List<FacetField> facetFeild = response.getFacetFields();
			LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();

			LinkedHashMap<String, ArrayList<ProductsModel>>  filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
			ArrayList<ProductsModel> attrList = new ArrayList<ProductsModel>();
			
      		ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
			Iterator<SolrDocument> itr = documents.iterator();
			System.out.println("DOCUMENTS");
			for(FacetField facetFilter:facetFeild)
			{
				ProductsModel tempVal = new ProductsModel();
				
				if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
				{
					filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
					attrList = new ArrayList<ProductsModel>();
			
					List<Count> attrValArr = facetFilter.getValues();
					System.out.println("Attribute Name : " + URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
					for(Count attrArr : attrValArr)
					{
						ProductsModel attrListVal = new ProductsModel();
						attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
						attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
						attrListVal.setAttrValue(attrArr.getName());
						attrListVal.setResultCount((int) attrArr.getCount());
						attrList.add(attrListVal);
						
					}
					
					filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
					tempVal.setAttrFilterList(filteredList);
					attrFilteredList.add(tempVal);
					attributeList.put(facetFilter.getName().replace("attr_", ""), facetFilter.getValues());
				}
				
			}
			
			itemLevelFilterData = new ArrayList<ProductsModel>();
			System.out.println("DOCUMENTS");
			
			itemLevelFilterData = new ArrayList<ProductsModel>();
			while (itr.hasNext()) {
				
				ProductsModel itemModel = new ProductsModel();
				SolrDocument doc = itr.next();
				
				
				itemModel.setItemId(Integer.parseInt(doc.getFieldValue("itemid").toString()));
				itemModel.setMinOrderQty(1);
				itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
				itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
				//if(doc.getFieldValue("brand")!=null)
				//itemModel.setManufacturerName(doc.getFieldValue("brand").toString());
				
				itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
				itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
				itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
				itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
				if(doc.getFieldValue("manfpartnumber")!=null)
				itemModel.setManufacturerPartNumber(doc.getFieldValue("manfpartnumber").toString());
				if(doc.getFieldValue("description")!=null)
				itemModel.setShortDesc(doc.getFieldValue("description").toString());
				itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
				itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
				itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
				itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
				itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
				itemModel.setResultCount(resultCount);
				String imageName = null;
       			String imageType = null;
       			
       			String itemUrl = doc.getFieldValue("brand").toString();
       			if(doc.getFieldValue("manfpartnumber")!=null)
       			itemUrl = doc.getFieldValue("brand").toString()+" "+doc.getFieldValue("manfpartnumber").toString();
       			
       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
       			}
       			
            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
       			String pattern = "[^A-Za-z0-9]";
   			 itemUrl = itemUrl.replaceAll(pattern," ");
   			 itemUrl = itemUrl.replaceAll("\\s+","-");
            	itemModel.setItemUrl(itemUrl);
       			
       			if(doc.getFieldValue("imageName")!=null)
       				imageName = doc.getFieldValue("imageName").toString();
       			
       					       			
       			if(doc.getFieldValue("imageType")!=null)
       				imageType = doc.getFieldValue("imageType").toString();
       			if(doc.getFieldValue("upc")!=null)
       				itemModel.setUpc(doc.getFieldValue("upc").toString());	
       			
       			if(imageName==null)
       			{
       				imageName = "NoImage.png";
       				imageType = "IMAGE";
       			}
       			itemModel.setImageName(imageName.trim());
       			itemModel.setImageType(imageType);
				
				itemLevelFilterData.add(itemModel);
				
			}
			
			if(itemLevelFilterData.size()>0)
			{
				server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
				server1.setParser(new XMLResponseParser());
				QueryRequest req1 = new QueryRequest();
				req1.setMethod(METHOD.POST);
				SolrQuery query1 = new SolrQuery();
				if(generalSubset>0 && generalSubset!=subsetId) {
					 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";
					 if(!siteNameSolr.equalsIgnoreCase(""))
					 {
						 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
					 }
				}

				query1.setQuery("itemid:("+idList+")");
				String fq2 = "type:"+indexType;
				query1.setFilterQueries(fq2);
				query1.setStart(0);
				query1.setRows(rowSize);
				
				QueryResponse response1 = server1.query(query1);
				
				SolrDocumentList documents1 = response1.getResults();
				resultCount = (int) response1.getResults().getNumFound();
				

				
				Iterator<SolrDocument> itr1 = documents1.iterator();
				NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
				
				while (itr1.hasNext()) {
					
					SolrDocument doc = itr1.next();
					for(ProductsModel iList : itemLevelFilterData)
					{
						int itemId = Integer.parseInt(doc.getFieldValue("itemid").toString());
						
						
					
						if(itemId==iList.getItemId())
						{
							
							int minOrdQty = 1;
							int orderQtyInterval = 1;
							int saleQty = 1;
							int packageQty = 1;
							int packageFlag = 0;
							String mOrdQty = null;
							String iPrice ="";
							if(doc.getFieldValue("price")!=null)
							{
								iPrice = doc.getFieldValue("price").toString();
							}
								
							
							double itemPrice = 0d;
							if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
							{
								itemPrice = Double.parseDouble(iPrice);
							}
							
							iList.setPrice(itemPrice);
							if(doc.getFieldValue("uom")!=null)
							iList.setUom(doc.getFieldValue("uom").toString());
							iList.setItemPriceId(Integer.parseInt(doc.getFieldValue("itemPriceId").toString()));
							
							
							
														
							if(doc.getFieldValue("customerPartNumber")!=null)
								iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
							
							if(doc.getFieldValue("materialGroup")!=null)
								iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
							
							if(doc.getFieldValue("minordqty")!=null)
							{
								if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
									minOrdQty = Integer.parseInt(doc.getFieldValue("minordqty").toString());
								if(minOrdQty==0)
									minOrdQty = 1;
							}
							if(doc.getFieldValue("orderQtyInterval")!=null)
							{
								if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
									orderQtyInterval = Integer.parseInt(doc.getFieldValue("orderQtyInterval").toString());
								if(orderQtyInterval==0)
									orderQtyInterval = 1;
							}
							if(doc.getFieldValue("salesQty")!=null)
							{
								if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
									saleQty = Integer.parseInt(doc.getFieldValue("salesQty").toString());
								
								if(saleQty==0)
									saleQty = 1;
							}
							
							if(doc.getFieldValue("packageFlag")!=null)
							{
								if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
									packageFlag = Integer.parseInt(doc.getFieldValue("packageFlag").toString());
							}
							
							if(doc.getFieldValue("packageQty")!=null)
							{
								if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
								{
									packageQty = Integer.parseInt(doc.getFieldValue("packageQty").toString());
								}
								
							}
							
							if(doc.getFieldValue("packDesc")!=null)
							{
								String packDesc = doc.getFieldValue("packDesc").toString();
								iList.setPackDesc(packDesc);
							}
							
							iList.setSaleQty(saleQty);
							iList.setMinOrderQty(minOrdQty);
							iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
							
						}
					}
				}
				 
	               if(idList!=null)
	               {
	            	   String idListArr[] = idList.split(" OR ");
	            	   for(String idListLoop:idListArr)
	            	   {
	            		   for(ProductsModel listVal:itemLevelFilterData)
	            		   {
	            			   if(idListLoop!=null)
	            			   {
	            				   if(Integer.parseInt(idListLoop)==listVal.getItemId())
	            				   {
	            					   itemLevelFilterDataTemp.add(listVal);
	            				   }
	            			   }
	            		   }
	            	   }
	               }
				
				
	               itemLevelFilterData = itemLevelFilterDataTemp;
				   
			}
     
	 }
	 catch (Exception e) {
		
		 e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
		ConnectionManager.closeSolrClientConnection(server1);
	}
	 
	 return itemLevelFilterData;
}

public static ArrayList<ProductsModel> getItemDetailsForGivenPartNumbers (int subsetId,int generalSubsetId,String partNumberList,int brandID,String searchBrand,String searchBySolrFieldName)
{
	long startTimer = CommonUtility.startTimeDispaly();
	ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
	ArrayList<ProductsModel> itemLevelFilterDataTemp = new ArrayList<ProductsModel>();
	String idList = "";
	String c = "";
	int resultCount = 0;
	HttpSolrServer server = null;
	HttpSolrServer server1 = null;
	try{

		String attrFtr[] = null;
		String indexType = "PH_SEARCH_"+subsetId;
		if(generalSubsetId>0 && generalSubsetId!=subsetId)
			indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubsetId+")";
		String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
		if(!siteNameSolr.equalsIgnoreCase(""))
		{
			fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
		}
		String sFq = "defaultCategory:Y";
		String getIdList = "itemid:(0)";
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_ARI_API")).equalsIgnoreCase("Y")){
			if(partNumberList!=null && partNumberList.trim().length()>0){
				getIdList = CommonUtility.validateString(searchBySolrFieldName)+":("+partNumberList.trim()+")";
				//getIdList = "partnumber:"+partNumberList.trim();
			}
		}else{
			if(partNumberList!=null && partNumberList.trim().length()>0){
				getIdList = CommonUtility.validateString(searchBySolrFieldName)+":("+partNumberList.trim()+")";
				//getIdList = "partnumber:"+partNumberList.trim();
			}
		}
		List<String> itemIdList = new ArrayList<String>();
		itemIdList = new ArrayList<String>(Arrays.asList(partNumberList.replaceAll(" ", "").split("OR")));
		int itemListCount = itemIdList.size();
		ProductsModel[] itemLeveFilterDataArray = new ProductsModel[itemListCount];
		
		String aFilter = sFq + "~" +fq+"~"+getIdList;
		attrFtr = aFilter.split("~");


		 server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
		server.setParser(new XMLResponseParser());
		QueryRequest req = new QueryRequest();
		req.setMethod(METHOD.POST);
		SolrQuery query = new SolrQuery();


		String queryString  = "*:*";

		query = new SolrQuery();

		query.setQuery(queryString);
		query.setFilterQueries(attrFtr);
		query.setStart(0);
		query.setRows(CommonUtility.validateString(partNumberList).split(" OR ").length);
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_ARI_API")).equalsIgnoreCase("Y")){
			if(CommonUtility.validateString(searchBrand).equalsIgnoreCase("Y")){
				query.addFilterQuery("brandID:"+brandID);
			}else{
				query.addFilterQuery("-brandID:"+brandID);
			}
		}
		System.out.println("Get Item Details For given PartNumbers : " + query);

		QueryResponse response = server.query(query);
		SolrDocumentList documents = response.getResults();
		resultCount = (int) response.getResults().getNumFound();
		Iterator<SolrDocument> itr = documents.iterator();

		itemLevelFilterData = new ArrayList<ProductsModel>();
		while (itr.hasNext()) {

			ProductsModel itemModel = new ProductsModel();
			SolrDocument doc = itr.next();



			itemModel.setItemId(Integer.parseInt(doc.getFieldValue("itemid").toString()));
			idList = idList+ c + itemModel.getItemId();
			c = " OR ";

			itemModel.setMinOrderQty(1);
			itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
			itemModel.setLevel1Menu(getSolrFieldValue(doc,"level1Category"));
			itemModel.setCategoryName(getSolrFieldValue(doc,"category"));
			itemModel.setBrandName(doc.getFieldValue("brand").toString());
			itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
			/*if(doc.getFieldValue("manufacturerName")!=null)
					itemModel.setManufacturerName(doc.getFieldValue("brand").toString());
				if(doc.getFieldValue("brand")!=null)
					itemModel.setBrandName(doc.getFieldValue("manufacturerName").toString());*/

			itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
			itemModel.setManufacturerName(getSolrFieldValue(doc,"manufacturerName"));
			itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));

			itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
			if(doc.getFieldValue("manfpartnumber")!=null){
				itemModel.setManufacturerPartNumber(doc.getFieldValue("manfpartnumber").toString());
			}
			if(doc.getFieldValue("description")!=null){
				itemModel.setShortDesc(doc.getFieldValue("description").toString());
			}
			itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
			itemModel.setEcommerceProductTitle(getSolrFieldValue(doc,"ecommerceProductTitle").replaceAll("\\<.*?>",""));
			itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
			itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
			itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
			itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
			itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
			itemModel.setResultCount(resultCount);
			String imageName = null;
			String imageType = null;

			String itemUrl = doc.getFieldValue("brand").toString();
			if(doc.getFieldValue("manfpartnumber")!=null)
				itemUrl = doc.getFieldValue("brand").toString()+" "+doc.getFieldValue("manfpartnumber").toString();

			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
			}

			//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
			String pattern = "[^A-Za-z0-9]";
			itemUrl = itemUrl.replaceAll(pattern," ");
			itemUrl = itemUrl.replaceAll("\\s+","-");
			itemModel.setItemUrl(itemUrl);

			if(doc.getFieldValue("imageName")!=null)
				imageName = doc.getFieldValue("imageName").toString();


			if(doc.getFieldValue("imageType")!=null)
				imageType = doc.getFieldValue("imageType").toString();
			if(doc.getFieldValue("upc")!=null)
				itemModel.setUpc(doc.getFieldValue("upc").toString());	

			if(imageName==null)
			{
				imageName = "NoImage.png";
				imageType = "IMAGE";
			}
			itemModel.setImageName(imageName.trim());
			itemModel.setImageType(imageType);
			// changes done for habegger to fetch the same sequence of item id in response. : Changes approved by Shashi, ref: chetan Sandesh
			int serpPosition = itemIdList.indexOf(doc.getFieldValue("itemid").toString());
			if(serpPosition>=0){
			itemLeveFilterDataArray[serpPosition]=itemModel;
			itemLevelFilterData = new ArrayList<ProductsModel>(Arrays.asList(itemLeveFilterDataArray));
			if(itemLeveFilterDataArray!=null && itemLeveFilterDataArray.length > 0 && itemIdList.size() > resultCount){
				itemLevelFilterData.removeAll(Collections.singleton(null));
				}
			}else{
				itemLevelFilterData.add(itemModel);
			}
		}

		if(itemLevelFilterData.size()>0)
		{
			server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
			server1.setParser(new XMLResponseParser());
			QueryRequest req1 = new QueryRequest();
			req1.setMethod(METHOD.POST);
			SolrQuery query1 = new SolrQuery();
			if(generalSubsetId>0 && generalSubsetId!=subsetId) {
				 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubsetId+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
				 if(!siteNameSolr.equalsIgnoreCase(""))
				 {
					 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubsetId+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
				 }
			}


			query1.setQuery("itemid:("+idList+")");
			String fq2 = "type:"+indexType;
			query1.setFilterQueries(fq2);
			query1.setStart(0);
			query1.setRows(CommonUtility.validateString(partNumberList).split(" OR ").length);

			QueryResponse response1 = server1.query(query1);

			SolrDocumentList documents1 = response1.getResults();
			resultCount = (int) response1.getResults().getNumFound();
			idList = "";
			c="";
			


			Iterator<SolrDocument> itr1 = documents1.iterator();
			NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
			
			while (itr1.hasNext()) {

				SolrDocument doc = itr1.next();
				for(ProductsModel iList : itemLevelFilterData)
				{
					int itemId = Integer.parseInt(doc.getFieldValue("itemid").toString());
					
					if(itemId==iList.getItemId())
					{

						int minOrdQty = 1;
						int orderQtyInterval = 1;
						int saleQty = 1;
						int packageQty = 1;
						int packageFlag = 0;
						String mOrdQty = null;
						String iPrice ="";
						if(doc.getFieldValue("price")!=null)
						{
							iPrice = doc.getFieldValue("price").toString();
						}


						double itemPrice = 0d;
						if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
						{
							itemPrice = Double.parseDouble(iPrice);
						}

						iList.setPrice(itemPrice);
						if(doc.getFieldValue("uom")!=null)
							iList.setUom(doc.getFieldValue("uom").toString());
						iList.setItemPriceId(Integer.parseInt(doc.getFieldValue("itemPriceId").toString()));




						if(doc.getFieldValue("customerPartNumber")!=null)
							iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());

						if(doc.getFieldValue("materialGroup")!=null)
							iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());

						if(doc.getFieldValue("minordqty")!=null)
						{
							if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
								minOrdQty = Integer.parseInt(doc.getFieldValue("minordqty").toString());
							if(minOrdQty==0)
								minOrdQty = 1;
						}
						if(doc.getFieldValue("orderQtyInterval")!=null)
						{
							if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
								orderQtyInterval = Integer.parseInt(doc.getFieldValue("orderQtyInterval").toString());
							if(orderQtyInterval==0)
								orderQtyInterval = 1;
						}
						if(doc.getFieldValue("salesQty")!=null)
						{
							if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
								saleQty = Integer.parseInt(doc.getFieldValue("salesQty").toString());

							if(saleQty==0)
								saleQty = 1;
						}

						if(doc.getFieldValue("packageFlag")!=null)
						{
							if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
								packageFlag = Integer.parseInt(doc.getFieldValue("packageFlag").toString());
						}
						
						if(doc.getFieldValue("packageQty")!=null)
						{
							if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
							{
								packageQty = Integer.parseInt(doc.getFieldValue("packageQty").toString());
							}

						}

						if(doc.getFieldValue("packDesc")!=null)
						{
							String packDesc = doc.getFieldValue("packDesc").toString();
							iList.setPackDesc(packDesc);
						}

						iList.setSaleQty(saleQty);
						iList.setMinOrderQty(minOrdQty);
						iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
						iList.setDisplayPricing(CommonUtility.validateString(getSolrFieldValue(doc,"displayPricing")));
						if(doc.getFieldValue("clearanceFlag") != null){
							iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
						}
						if(doc.getFieldValue("overRidePriceRule") != null){
							iList.setOverRidePriceRule(CommonUtility.validateString(doc.getFieldValue("overRidePriceRule").toString()));
						}
					}
				}
			}

			/*if(idList!=null)
			{
				String idListArr[] = idList.split(" OR ");
				for(String idListLoop:idListArr)
				{
					for(ProductsModel listVal:itemLevelFilterData)
					{
						if(idListLoop!=null)
						{
							if(Integer.parseInt(idListLoop)==listVal.getItemId())
							{
								itemLevelFilterDataTemp.add(listVal);
							}
						}
					}
				}
			}


			itemLevelFilterData = itemLevelFilterDataTemp;*/

		}



	}

	catch (Exception e) {
		e.printStackTrace();
	}finally {
		ConnectionManager.closeSolrClientConnection(server);
		ConnectionManager.closeSolrClientConnection(server1);
	}
	CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	return itemLevelFilterData;


}
	public static ArrayList<ProductsModel> getEventDetails(){
		ArrayList<ProductsModel> staticContentResult = null;
		HttpSolrServer server = null;
		 try
		 {
			    server = ConnectionManager.getSolrClientConnection( solrURL+"/evendatamanager");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				int resultCount = 0;
				query.setQuery("*:*");
				query.setStart(0);
				query.setRows(100);
				//query.addSortField("displaySeq", SolrQuery.ORDER.asc );
				query.addSortField("startDate", SolrQuery.ORDER.asc ); //----- Check

				Date currentTime = new Date();
				String time= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(currentTime);
				query.addFilterQuery("startDate:["+time+" TO *]"); //----- Check
				
				QueryResponse response = server.query(query);
				System.out.println("Event Details Query : " + query);
				
				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				Iterator<SolrDocument> itr = documents.iterator();
				staticContentResult = new ArrayList<ProductsModel>();
				while (itr.hasNext()) {	
					SolrDocument doc = itr.next();
					//System.out.println("ID: "+doc.getFieldValue("id").toString());
					//System.out.println("pageTitle: "+doc.getFieldValue("pageTitle").toString());
					//System.out.println("pageContent: \n"+doc.getFieldValue("pageContent").toString());
					ProductsModel pageModel = new ProductsModel();
					String id = doc.getFieldValue("id").toString();
					pageModel.setStaticPageId(id);
					if(doc.getFieldValue("pageTitle")!=null)
					{
						String pattern = "[^A-Za-z0-9]";
						String keyWord = doc.getFieldValue("pageTitle").toString();
						String scrubbedKeyword = keyWord.replaceAll(pattern,"-");
						pageModel.setStaticPageTitle(doc.getFieldValue("pageTitle").toString());
						pageModel.setItemUrl(scrubbedKeyword);
					}
					if(doc.getFieldValue("pageName")!=null){
						pageModel.setPageName(doc.getFieldValue("pageName").toString());
					}
					if(doc.getFieldValue("keywords")!=null)
					{
						pageModel.setStaticContent(doc.getFieldValue("keywords").toString());
					}
					if(doc.getFieldValue("startDate")!=null)
					{
						pageModel.setStartDate(doc.getFieldValue("startDate").toString());
					}
					if(doc.getFieldValue("endDate")!=null)
					{
						pageModel.setEndDate(doc.getFieldValue("endDate").toString());
					}
					
					if(doc.getFieldValue("featuredEvent")!=null)
					{
						pageModel.setFeaturedEvent(doc.getFieldValue("featuredEvent").toString());
					}
					if(doc.getFieldValue("locationName")!=null){
						pageModel.setLocation(doc.getFieldValue("locationName").toString());
					}
					if(doc.getFieldValue("addressOne")!=null){
						pageModel.setAddressOne(doc.getFieldValue("addressOne").toString());
					}
					if(doc.getFieldValue("notificationContactNumber")!=null){
						pageModel.setNotificationContactNumber(doc.getFieldValue("notificationContactNumber").toString());
					}
					if(doc.getFieldValue("contact")!=null){
						pageModel.setContact(doc.getFieldValue("contact").toString());
					}
					if(doc.getFieldValue("notificationEmail")!=null){
						pageModel.setNotificationEmail(doc.getFieldValue("notificationEmail").toString());
					}
					if(doc.getFieldValue("eventFee")!=null){
						pageModel.setEventFee(doc.getFieldValue("eventFee").toString());
					}
					if(doc.getFieldValue("showSeats")!=null){
						pageModel.setShowSeats(doc.getFieldValue("showSeats").toString());
					}
					if(doc.getFieldValue("totalseats")!=null){
						pageModel.setTotalseats(doc.getFieldValue("totalseats").toString());
					}
					if(doc.getFieldValue("bookedseats")!=null){
						pageModel.setBookedseats(doc.getFieldValue("bookedseats").toString());
					}
					if(doc.getFieldValue("timeZoneOffset")!=null){
						pageModel.setTimeZoneOffset(doc.getFieldValue("timeZoneOffset").toString());
					}
					if(doc.getFieldValue("filelocation")!=null){
						pageModel.setFilelocation(doc.getFieldValue("filelocation").toString());
					}
					pageModel.setResultCount(resultCount);
					ArrayList<EventModel> eventCustomFieldList = UsersDAO.getSingleEventCustomFields(Integer.parseInt(id));
					pageModel.setEventCustomFieldList(eventCustomFieldList);
					staticContentResult.add(pageModel);
					
				}
			 
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally {
				ConnectionManager.closeSolrClientConnection(server);
		 }
		 return staticContentResult;
	}

	public static ArrayList<ProductsModel> getSpecialItems(String customFieldValue,int subsetId){
		ArrayList<ProductsModel> itemList = new ArrayList<ProductsModel>();
		ArrayList<ProductsModel> itemLevelFilterDataTemp = new ArrayList<ProductsModel>();
		String idList = "";
		String c = "";
		HttpSolrServer server = null;
		HttpSolrServer server1 = null;
		try {
			String attrFtr[] = null;
			String indexType = "PH_SEARCH_"+subsetId;
			String sFq = "defaultCategory:Y";
			server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			int resultCount = 0;
			if(CommonUtility.validateString(customFieldValue).length()>0){
				//query.setQuery(customFieldValue+":Y"+":"+sFq); 
				 String queryString  = "*:*";
				query.setQuery(queryString);
				String aFilter = sFq + "~" +customFieldValue+":Y";
				attrFtr = aFilter.split("~");
				query.setFilterQueries(attrFtr);
				QueryResponse response = server.query(query);
				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				response = server.query(query);
				System.out.println("Best Seller Query : " + query);
				documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("Total Reulsts Found : " + resultCount);
				while (itr.hasNext()) {
					try{
						ProductsModel itemModel = new ProductsModel();
						SolrDocument doc = itr.next();
						itemModel.setItemId(Integer.parseInt(doc.getFieldValue("itemid").toString()));
						idList = idList+ c + itemModel.getItemId();
						c = " OR ";
						itemModel.setItemId(Integer.parseInt(doc.getFieldValue("itemid").toString()));
						itemModel.setMinOrderQty(1);
						itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
						itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
						itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
						itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
						itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
						itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
						itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
						itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
						itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
						itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
						if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y"))
						{
							if(doc.getFieldValue("productItemCount")!=null){
								itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
							}
						}
						itemModel.setResultCount(resultCount);
						String imageName = null;
		       			String imageType = null;
		       			String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
		       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
		       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
		       			}
		       			String pattern = "[^A-Za-z0-9]";
		   			 itemUrl = itemUrl.replaceAll(pattern," ");
		   			 itemUrl = itemUrl.replaceAll("\\s+","-");
		            	itemModel.setItemUrl(itemUrl);
		       			if(doc.getFieldValue("imageName")!=null){
		       				imageName = doc.getFieldValue("imageName").toString();
		       			}
		       			if(doc.getFieldValue("imageType")!=null){
		       				imageType = doc.getFieldValue("imageType").toString();
		       			}
		       			if(doc.getFieldValue("upc")!=null){
		       				itemModel.setUpc(doc.getFieldValue("upc").toString());
		       			}
		       			if(imageName==null){
		       				imageName = "NoImage.png";
		       				imageType = "IMAGE";
		       			}
		       			itemModel.setImageName(imageName.trim());
		       			itemModel.setImageType(imageType);
		       			itemList.add(itemModel);
						}catch (Exception e) {
							System.out.println("Error Occured While retriving data from solr.");
							e.printStackTrace();
						}
					}
			}else{
				itemList = null;
			}
			
			
			
			if(itemList.size()>0)
			{
				server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
				server1.setParser(new XMLResponseParser());
				QueryRequest req1 = new QueryRequest();
				req1.setMethod(METHOD.POST);
				SolrQuery query1 = new SolrQuery();
				
				query1.setQuery("itemid:("+idList+")");
				String fq2 = "type:"+indexType;
				query1.setFilterQueries(fq2);
				query1.setStart(0);
				query1.setRows(CommonUtility.validateString(idList).split(" OR ").length);
				
				QueryResponse response1 = server1.query(query1);
				
				SolrDocumentList documents1 = response1.getResults();
				resultCount = (int) response1.getResults().getNumFound();
				

				
				Iterator<SolrDocument> itr1 = documents1.iterator();
				NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
				
				while (itr1.hasNext()) {
					
					SolrDocument doc = itr1.next();
					for(ProductsModel iList : itemList)
					{
						int itemId = Integer.parseInt(doc.getFieldValue("itemid").toString());
						
						
					
						if(itemId==iList.getItemId())
						{
							
							int minOrdQty = 1;
							int orderQtyInterval = 1;
							int saleQty = 1;
							int packageQty = 1;
							int packageFlag = 0;
							String mOrdQty = null;
							String iPrice ="";
							if(doc.getFieldValue("price")!=null)
							{
								iPrice = doc.getFieldValue("price").toString();
							}
								
							
							double itemPrice = 0d;
							if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
							{
								itemPrice = Double.parseDouble(iPrice);
							}
							
							iList.setPrice(itemPrice);
							if(doc.getFieldValue("uom")!=null)
							iList.setUom(doc.getFieldValue("uom").toString());
							iList.setItemPriceId(Integer.parseInt(doc.getFieldValue("itemPriceId").toString()));
							
							
							
														
							if(doc.getFieldValue("customerPartNumber")!=null)
								iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
							
							if(doc.getFieldValue("materialGroup")!=null)
								iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
							
							if(doc.getFieldValue("minordqty")!=null)
							{
								if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
									minOrdQty = Integer.parseInt(doc.getFieldValue("minordqty").toString());
								if(minOrdQty==0)
									minOrdQty = 1;
							}
							if(doc.getFieldValue("orderQtyInterval")!=null)
							{
								if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
									orderQtyInterval = Integer.parseInt(doc.getFieldValue("orderQtyInterval").toString());
								if(orderQtyInterval==0)
									orderQtyInterval = 1;
							}
							if(doc.getFieldValue("salesQty")!=null)
							{
								if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
									saleQty = Integer.parseInt(doc.getFieldValue("salesQty").toString());
								
								if(saleQty==0)
									saleQty = 1;
							}
							
							if(doc.getFieldValue("packageFlag")!=null)
							{
								if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
									packageFlag = Integer.parseInt(doc.getFieldValue("packageFlag").toString());
							}
							
							if(doc.getFieldValue("packageQty")!=null)
							{
								if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
								{
									packageQty = Integer.parseInt(doc.getFieldValue("packageQty").toString());
								}
								
							}
							
							if(doc.getFieldValue("packDesc")!=null)
							{
								String packDesc = doc.getFieldValue("packDesc").toString();
								iList.setPackDesc(packDesc);
							}
							
							iList.setSaleQty(saleQty);
							iList.setMinOrderQty(minOrdQty);
							iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
							
						}
					}
				}
				 
	               if(idList!=null)
	               {
	            	   String idListArr[] = idList.split(" OR ");
	            	   for(String idListLoop:idListArr)
	            	   {
	            		   for(ProductsModel listVal:itemList)
	            		   {
	            			   if(idListLoop!=null)
	            			   {
	            				   if(Integer.parseInt(idListLoop)==listVal.getItemId())
	            				   {
	            					   itemLevelFilterDataTemp.add(listVal);
	            				   }
	            			   }
	            		   }
	            	   }
	               }
				
				
	               itemList = itemLevelFilterDataTemp;
				   
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
			ConnectionManager.closeSolrClientConnection(server1);
		}
		return itemList;
		}

	
	

	public static ProductsModel getTaxonomyDetail(int itemId,int subsetId,int generalSubset)
	{
		ProductsModel categoryResult = new ProductsModel();
		HttpSolrServer server =  null;
		try
		{
			 String indexType = "PH_SEARCH_"+subsetId;
			 if(generalSubset>0 && generalSubset!=subsetId)
				 indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")"; 
			
			
			 server =  ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
			 SolrQuery query = new SolrQuery();
				query = new SolrQuery();
				query.setQuery("itemid:"+itemId);
				if(!siteNameSolr.equalsIgnoreCase(""))
				{
					query.setFilterQueries("{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType);
				}else {
					query.setFilterQueries("{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType);
				}
				query.setStart(0);
				query.setRows(100);
				query.addFilterQuery("defaultCategory:Y");
				System.out.println("Taxonomy Query : " + query);
				QueryResponse response = server.query(query);
				SolrDocumentList documents = response.getResults();
				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("DOCUMENTS");
				while (itr.hasNext()) {
					categoryResult = new ProductsModel();
					SolrDocument doc = itr.next();
					if(doc.getFieldValue("categoryID") != null) {
						categoryResult.setCategoryCode(doc.getFieldValue("categoryID").toString());
					}
				}
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return categoryResult;
		
	}



	public static ProductsModel getCategoryDescription(String categoryId)
	{
		ProductsModel categoryResult = new ProductsModel();
		HttpSolrServer server = null;
		try
		{
			 server =  ConnectionManager.getSolrClientConnection(solrURL+"/catalogdata");
			 SolrQuery query = new SolrQuery();
				query = new SolrQuery();
				query.setQuery("categoryID:"+categoryId);
				
				query.setStart(0);
				query.setRows(1);
				System.out.println("Taxonomy Query : " + query);
				QueryResponse response = server.query(query);
				SolrDocumentList documents = response.getResults();
				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("DOCUMENTS");
				while (itr.hasNext()) {
					categoryResult = new ProductsModel();
					SolrDocument doc = itr.next();
					categoryResult.setShortDesc(getSolrFieldValue(doc, "categoryDesc"));
					categoryResult.setCategoryCode(doc.getFieldValue("categoryID").toString());
					categoryResult.setStaticPageId(getSolrFieldValue(doc,"staticPageId"));
				}
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return categoryResult;
		
	}



	public static ArrayList<ProductsModel> getItemListByItemId(String idList,int subsetId,int generalSubset,int rowSize,int fromRow,String linkType)
	{
		 ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
		 ArrayList<ProductsModel> itemLevelFilterDataTemp = new ArrayList<ProductsModel>();
		 HttpSolrServer server = null;
		 HttpSolrServer server1 = null;
		 try
		 {
			 String indexType = "PH_SEARCH_"+subsetId;
				
			 if(generalSubset>0 && generalSubset!=subsetId)
				 indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
			    server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				
				 String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
				 if(!siteNameSolr.equalsIgnoreCase(""))
				 {
					 fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
				 }
			 query = new SolrQuery();
				
				query.setQuery("itemid:("+idList+")");
				//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				
				
				query.setStart(fromRow);
				query.setRows(rowSize);
				query.setFilterQueries(fq);
				
				System.out.println("Navigation query : " + query);
				
				QueryResponse response = server.query(query);
				
				
				
				SolrDocumentList documents = response.getResults();
				int resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				LinkedHashMap<String, List<Count>> attributeList = new LinkedHashMap<String, List<Count>>();

				LinkedHashMap<String, ArrayList<ProductsModel>>  filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				ArrayList<ProductsModel> attrList = new ArrayList<ProductsModel>();
				
	      		ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
				Iterator<SolrDocument> itr = documents.iterator();
				System.out.println("DOCUMENTS");
				for(FacetField facetFilter:facetFeild)
				{
					ProductsModel tempVal = new ProductsModel();
					
					if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
					{
						filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
						attrList = new ArrayList<ProductsModel>();
				
						List<Count> attrValArr = facetFilter.getValues();
						System.out.println("Attribute Name : " + URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
						for(Count attrArr : attrValArr)
						{
							ProductsModel attrListVal = new ProductsModel();
							attrListVal.setAttrValueEncoded(URLEncoder.encode(attrArr.getName(),"UTF-8"));
							attrListVal.setAttrNameEncoded(URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
							attrListVal.setAttrValue(attrArr.getName());
							attrListVal.setResultCount((int) attrArr.getCount());
							attrList.add(attrListVal);
							
						}
						
						filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
						tempVal.setAttrFilterList(filteredList);
						attrFilteredList.add(tempVal);
						attributeList.put(facetFilter.getName().replace("attr_", ""), facetFilter.getValues());
					}
					
				}
				
				itemLevelFilterData = new ArrayList<ProductsModel>();
				System.out.println("DOCUMENTS");
				
				itemLevelFilterData = new ArrayList<ProductsModel>();
				while (itr.hasNext()) {
					
					ProductsModel itemModel = new ProductsModel();
					SolrDocument doc = itr.next();
					
					itemModel.setLinkTypeName(linkType);
					itemModel.setItemId(Integer.parseInt(doc.getFieldValue("itemid").toString()));
					itemModel.setMinOrderQty(1);
					itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
					itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
					//if(doc.getFieldValue("brand")!=null)
					//itemModel.setManufacturerName(doc.getFieldValue("brand").toString());
					
					itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
					itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
					itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
					itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
					if(doc.getFieldValue("manfpartnumber")!=null)
					itemModel.setManufacturerPartNumber(doc.getFieldValue("manfpartnumber").toString());
					if(doc.getFieldValue("description")!=null)
					itemModel.setShortDesc(doc.getFieldValue("description").toString());
					itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
					itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
					itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
					itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
					itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
					itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?CommonUtility.validateString(doc.getFieldValue("pageTitle").toString()):""));
					itemModel.setResultCount(resultCount);
					String imageName = null;
	       			String imageType = null;
	       			
	       			String itemUrl = doc.getFieldValue("brand").toString();
	       			if(doc.getFieldValue("manfpartnumber")!=null)
	       			itemUrl = doc.getFieldValue("brand").toString()+" "+doc.getFieldValue("manfpartnumber").toString();
	       			
	       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
	       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
	       			}
	       			
	            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	       			String pattern = "[^A-Za-z0-9]";
					 itemUrl = itemUrl.replaceAll(pattern,"-");
					 itemUrl = itemUrl.replaceAll("\\s","-");
	            	itemUrl = itemUrl.replaceAll("\\s","-");
	            	itemModel.setItemUrl(itemUrl);
	       			
	       			if(doc.getFieldValue("imageName")!=null)
	       				imageName = doc.getFieldValue("imageName").toString();
	       			
	       					       			
	       			if(doc.getFieldValue("imageType")!=null)
	       				imageType = doc.getFieldValue("imageType").toString();
	       			if(doc.getFieldValue("upc")!=null)
	       				itemModel.setUpc(doc.getFieldValue("upc").toString());	
	       			
	       			if(imageName==null)
	       			{
	       				imageName = "NoImage.png";
	       				imageType = "IMAGE";
	       			}
	       			itemModel.setImageName(imageName.trim());
	       			itemModel.setImageType(imageType);
					
					itemLevelFilterData.add(itemModel);
					
				}
				
				if(itemLevelFilterData.size()>0)
				{
					server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
					server1.setParser(new XMLResponseParser());
					QueryRequest req1 = new QueryRequest();
					req1.setMethod(METHOD.POST);
					SolrQuery query1 = new SolrQuery();
					if(generalSubset>0 && generalSubset!=subsetId) {
						indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
						if(!siteNameSolr.equalsIgnoreCase(""))
						{
							indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
						}
					}

					query1.setQuery("itemid:("+idList+")");
					String fq2 = "type:"+indexType;
					query1.setFilterQueries(fq2);
					query1.setStart(0);
					query1.setRows(rowSize);
					
					QueryResponse response1 = server1.query(query1);
					
					SolrDocumentList documents1 = response1.getResults();
					resultCount = (int) response1.getResults().getNumFound();
					

					
					Iterator<SolrDocument> itr1 = documents1.iterator();
					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
					
					while (itr1.hasNext()) {
						
						SolrDocument doc = itr1.next();
						for(ProductsModel iList : itemLevelFilterData)
						{
							int itemId = Integer.parseInt(doc.getFieldValue("itemid").toString());
							
							
						
							if(itemId==iList.getItemId())
							{
								
								int minOrdQty = 1;
								int orderQtyInterval = 1;
								int saleQty = 1;
								int packageQty = 1;
								int packageFlag = 0;
								String mOrdQty = null;
								String iPrice ="";
								if(doc.getFieldValue("price")!=null)
								{
									iPrice = doc.getFieldValue("price").toString();
								}
									
								
								double itemPrice = 0d;
								if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
								{
									itemPrice = Double.parseDouble(iPrice);
								}
								
								iList.setPrice(itemPrice);
								if(doc.getFieldValue("uom")!=null)
								iList.setUom(doc.getFieldValue("uom").toString());
								iList.setItemPriceId(Integer.parseInt(doc.getFieldValue("itemPriceId").toString()));
								
								
								
															
								if(doc.getFieldValue("customerPartNumber")!=null)
									iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
								
								if(doc.getFieldValue("materialGroup")!=null)
									iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
								
								if(doc.getFieldValue("minordqty")!=null)
								{
									if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
										minOrdQty = Integer.parseInt(doc.getFieldValue("minordqty").toString());
									if(minOrdQty==0)
										minOrdQty = 1;
								}
								if(doc.getFieldValue("orderQtyInterval")!=null)
								{
									if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
										orderQtyInterval = Integer.parseInt(doc.getFieldValue("orderQtyInterval").toString());
									if(orderQtyInterval==0)
										orderQtyInterval = 1;
								}
								if(doc.getFieldValue("salesQty")!=null)
								{
									if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
										saleQty = Integer.parseInt(doc.getFieldValue("salesQty").toString());
									
									if(saleQty==0)
										saleQty = 1;
								}
								
								if(doc.getFieldValue("packageFlag")!=null)
								{
									if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
										packageFlag = Integer.parseInt(doc.getFieldValue("packageFlag").toString());
								}
								
								if(doc.getFieldValue("packageQty")!=null)
								{
									if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
									{
										packageQty = Integer.parseInt(doc.getFieldValue("packageQty").toString());
									}
									
								}
								
								if(doc.getFieldValue("packDesc")!=null)
								{
									String packDesc = doc.getFieldValue("packDesc").toString();
									iList.setPackDesc(packDesc);
								}
								
								iList.setSaleQty(saleQty);
								iList.setMinOrderQty(minOrdQty);
								iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
								
							}
						}
					}
					 
		               if(idList!=null)
		               {
		            	   String idListArr[] = idList.split(" OR ");
		            	   for(String idListLoop:idListArr)
		            	   {
		            		   for(ProductsModel listVal:itemLevelFilterData)
		            		   {
		            			   if(idListLoop!=null)
		            			   {
		            				   if(Integer.parseInt(idListLoop)==listVal.getItemId())
		            				   {
		            					   itemLevelFilterDataTemp.add(listVal);
		            				   }
		            			   }
		            		   }
		            	   }
		               }
					
					
		               itemLevelFilterData = itemLevelFilterDataTemp;
					   
				}
	     
		 }
		 catch (Exception e) {
			 e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
			ConnectionManager.closeSolrClientConnection(server1);
		}
		 return itemLevelFilterData;
	}


	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getBannerList(String treeId){
			
			LinkedHashMap<String,  ArrayList<MenuAndBannersModal>> bannerList = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
			ArrayList<MenuAndBannersModal> bannerArray = new ArrayList<MenuAndBannersModal>();
			HttpSolrServer server = null;
			try
			{
				server = ConnectionManager.getSolrClientConnection(solrURL+"/bannerlist");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
	 	 	   
				
				String bannerListName[] = {"TOP","BOTTOM","RIGHT","LEFT"};

				for(String bList:bannerListName)
				{
					bannerArray = new ArrayList<MenuAndBannersModal>();
					int resultCount = 0;
					query.setQuery("*:*");
					String fq=  "{!join from="+bList+"_BANNER_ID to=valueListId fromIndex=catalogdata}categoryID:"+treeId;
					if(!siteNameSolr.equalsIgnoreCase(""))
					{
						fq=  "{!join from="+bList+"_BANNER_ID to=valueListId fromIndex="+siteNameSolr+"_catalogdata method=crossCollection}categoryID:"+treeId;
					}
					fq = fq + "~" + "-(-startDate:[* TO NOW] AND startDate:[* TO *])";
					fq = fq + "~" + "-(-endDate:[NOW TO *] AND endDate:[* TO *])";
					String attrFtr[] = fq.split("~");
					query.setStart(0);
					query.setRows(100);
					

					query.setFilterQueries(attrFtr);
					System.out.println("Banner Query : " + query);
					QueryResponse response = server.query(query);

					SolrDocumentList documents = response.getResults();
					resultCount = (int) response.getResults().getNumFound();
					List<FacetField> facetFeild = response.getFacetFields();
					Iterator<SolrDocument> itr = documents.iterator();
					while (itr.hasNext()) {

						SolrDocument doc = itr.next();
						
						MenuAndBannersModal bannerInfo= new MenuAndBannersModal();
						
						if(doc.getFieldValue("bannerImageName")!=null)
						bannerInfo.setImageName(doc.getFieldValue("bannerImageName").toString());
						
						if(doc.getFieldValue("imageType")!=null)
						bannerInfo.setImageType(doc.getFieldValue("imageType").toString());
						
						if(doc.getFieldValue("bannerLandingUrl")!=null)
						bannerInfo.setImageURL(doc.getFieldValue("bannerLandingUrl").toString());
						
						bannerInfo.setImagePosition(bList);
						
						if(doc.getFieldValue("scrollable")!=null)
						bannerInfo.setBannerScroll(doc.getFieldValue("scrollable").toString());
						
						if(doc.getFieldValue("numberofimgtoscroll")!=null)
						bannerInfo.setBannerNumberofItem(doc.getFieldValue("numberofimgtoscroll").toString());
						
						if(doc.getFieldValue("bannerDirection")!=null)
						bannerInfo.setBannerDirection(doc.getFieldValue("bannerDirection").toString());
						
						if(doc.getFieldValue("bannerDelay")!=null)
						bannerInfo.setBannerDelay(doc.getFieldValue("bannerDelay").toString());
						
						bannerArray.add(bannerInfo);
						
						
					}
					bannerList.put(bList.toLowerCase(), bannerArray);
				}
				
				
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server);
			}
			return bannerList;

		}
		
		
		
	public static ArrayList<MenuAndBannersModal> getBannerListByName(String bannerListName){
			ArrayList<MenuAndBannersModal> bannerArray = new ArrayList<MenuAndBannersModal>();
			HttpSolrServer server = null;
			try
			{
				server = ConnectionManager.getSolrClientConnection(solrURL+"/bannerlist");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();

					int resultCount = 0;
					query.setQuery("bannerListName:\""+bannerListName+"\"");
					String fq= "-(-startDate:[* TO NOW] AND startDate:[* TO *])";
					fq = fq + "~" + "-(-endDate:[NOW TO *] AND endDate:[* TO *])";
					String attrFtr[] = fq.split("~");
					query.setStart(0);
					query.setRows(100);
					

					query.setFilterQueries(attrFtr);
					System.out.println("Banner Query : " + query);
					QueryResponse response = server.query(query);

					SolrDocumentList documents = response.getResults();
					resultCount = (int) response.getResults().getNumFound();
					Iterator<SolrDocument> itr = documents.iterator();
					while (itr.hasNext()) {

						SolrDocument doc = itr.next();
						
						MenuAndBannersModal bannerInfo= new MenuAndBannersModal();
						
						if(doc.getFieldValue("bannerImageName")!=null)
						bannerInfo.setImageName(doc.getFieldValue("bannerImageName").toString());
						
						if(doc.getFieldValue("imageType")!=null)
						bannerInfo.setImageType(doc.getFieldValue("imageType").toString());
						
						if(doc.getFieldValue("bannerLandingUrl")!=null)
						bannerInfo.setImageURL(doc.getFieldValue("bannerLandingUrl").toString());

						
						if(doc.getFieldValue("bannerType")!=null)
							bannerInfo.setBannerType(doc.getFieldValue("bannerType").toString());
						
						if(doc.getFieldValue("bannerDelay")!=null)
						bannerInfo.setBannerScrollDelay(doc.getFieldValue("bannerDelay").toString());
						
						bannerArray.add(bannerInfo);
						
						
					}
					
				
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server);
			}
			return bannerArray;
		}

		
		public static HashMap<String, ArrayList<ProductsModel>> itemDetailData(String taxonomyId,String eclipseSessionId,String userName,String entityId,String homeTeritory,int userId,String type,String wareHousecode,String customerId,String customerCountry,int generalSubset,int subsetId)
		{
			double price = 0.00;
			double discountAmount = 0.00;
			HttpSolrServer server1 = null;
			HttpSolrServer server = null;
			HashMap<String, ArrayList<ProductsModel>> itemDetailList = new HashMap<String, ArrayList<ProductsModel>>();
			try{
				
				ArrayList<ProductsModel> itemDetailObject = new ArrayList<ProductsModel>();
			    ArrayList<String> partIdentifier = new ArrayList<String>();
			    ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
			    String displayPricing = "Y";
			    String docList = null;
			    String imgList = null;
			    String videoList = null;
			    String linkedItems = null;
			    String attributeList = null;
		        int itemId = 0;
		        int resultCount = 0;
				server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
				server1.setParser(new XMLResponseParser());
				QueryRequest req1 = new QueryRequest();
				req1.setMethod(METHOD.POST);
				SolrQuery query1 = new SolrQuery();
		
				 String indexType = "PH_SEARCH_"+subsetId;
				 if(generalSubset>0 && generalSubset!=subsetId) {
					 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
					 if(!siteNameSolr.equalsIgnoreCase(""))
					 {
						 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
					 }
				}

				query1.setQuery("itemPriceId:("+taxonomyId+")");
				String fq2 = "type:"+indexType;
				query1.setFilterQueries(fq2);
				query1.setStart(0);
				query1.setRows(CommonUtility.validateString(taxonomyId).split(" OR ").length);
				System.out.println("ItemDetail Price Query : " + query1);
				
				QueryResponse response1 = server1.query(query1);
				
				SolrDocumentList documents1 = response1.getResults();
				resultCount = (int) response1.getResults().getNumFound();
				

				
				Iterator<SolrDocument> itr1 = documents1.iterator();
				NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
				
				while (itr1.hasNext()) {
					
					SolrDocument doc = itr1.next();
					ProductsModel iList = new ProductsModel();
						itemId = Integer.parseInt(doc.getFieldValue("itemid").toString());
					
							iList.setItemId(itemId);
							int minOrdQty = 1;
							int orderQtyInterval = 1;
							int saleQty = 1;
							int packageQty = 1;
							int packageFlag = 0;
							String mOrdQty = null;
							String iPrice ="";
							if(doc.getFieldValue("price")!=null)
							{
								iPrice = doc.getFieldValue("price").toString();
							}
								
							
							double itemPrice = 0d;
							if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
							{
								itemPrice = Double.parseDouble(iPrice);
							}
							
							
							iList.setPrice(itemPrice);
							if(doc.getFieldValue("uom")!=null)
							iList.setUom(doc.getFieldValue("uom").toString());
							iList.setItemPriceId(Integer.parseInt(doc.getFieldValue("itemPriceId").toString()));
							
							
							
														
							if(doc.getFieldValue("customerPartNumber")!=null)
								iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
							
							if(doc.getFieldValue("materialGroup")!=null)
								iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
							
							if(doc.getFieldValue("minordqty")!=null)
							{
								if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
									minOrdQty = Integer.parseInt(doc.getFieldValue("minordqty").toString());
								if(minOrdQty==0)
									minOrdQty = 1;
							}
							if(doc.getFieldValue("orderQtyInterval")!=null)
							{
								if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
									orderQtyInterval = Integer.parseInt(doc.getFieldValue("orderQtyInterval").toString());
								if(orderQtyInterval==0)
									orderQtyInterval = 1;
							}
							if(doc.getFieldValue("salesQty")!=null)
							{
								if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
									saleQty = Integer.parseInt(doc.getFieldValue("salesQty").toString());
								
								if(saleQty==0)
									saleQty = 1;
							}
							
							if(doc.getFieldValue("packageFlag")!=null)
							{
								if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
									packageFlag = Integer.parseInt(doc.getFieldValue("packageFlag").toString());
							}
							
							if(doc.getFieldValue("packageQty")!=null)
							{
								if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
								{
									packageQty = Integer.parseInt(doc.getFieldValue("packageQty").toString());
								}
								
							}
							
							if(doc.getFieldValue("packDesc")!=null)
							{
								String packDesc = doc.getFieldValue("packDesc").toString();
								iList.setPackDesc(packDesc);
							}
							
							if(doc.getFieldValue("seourl")!=null){
								iList.setSupercedeurl(doc.getFieldValue("seourl").toString());
							}
							iList.setClearance(CommonUtility.validateString(getSolrFieldValue(doc,"clearanceFlag")));
							iList.setSaleQty(saleQty);
							iList.setMinOrderQty(minOrdQty);
							iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
							
							itemDetailObject.add(iList);
					
					
				}
				
				
				
				server = ConnectionManager.getSolrClientConnection(solrURL+"/itemdetaildata");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				query.setQuery("itemPriceId:"+taxonomyId);
		       // query.setQuery("itemid:"+itemId);
		       query.setStart(0);
		       query.setRows(1);

				System.out.println("ItemDetail Query : " + query);
				QueryResponse response = server.query(query);

				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				Iterator<SolrDocument> itr = documents.iterator();
				
				while (itr.hasNext()) {
					SolrDocument doc = itr.next();
						for(ProductsModel detailData : itemDetailObject){
							
							//if(itemId==detailData.getItemId() && CommonUtility.validateNumber(taxonomyId)==CommonUtility.validateNumber(getSolrFieldValue(doc,"itemPriceId"))){
	
					        	String longDesc = "";
					        	int packageQty = 1;
					        	String itemUrl = CommonUtility.validateString(getSolrFieldValue(doc,"brand")) + CommonUtility.validateString(getSolrFieldValue(doc,"manfpartnumber"));
					        	
					        	longDesc = CommonUtility.validateString(getSolrFieldValue(doc,"longdescriptionone"));
						        itemUrl = CommonUtility.validateString(itemUrl).replaceAll(" ","-");
						        displayPricing = CommonUtility.validateString(getSolrFieldValue(doc,"displayPricing"));
						        itemId = CommonUtility.validateNumber(getSolrFieldValue(doc,"itemid"));	
						        
						        if(CommonUtility.validateString(getSolrFieldValue(doc,"longdescriptiontwo")).length()>0){
					        		if(CommonUtility.validateString(longDesc).length()>0){
					        			longDesc = longDesc+" ";
					        		}
					        		longDesc = longDesc+CommonUtility.validateString(getSolrFieldValue(doc,"longdescriptiontwo"));
					        	}
						        
						        
						        detailData.setLongDesc(CommonUtility.validateString(longDesc));
						        detailData.setItemUrl(itemUrl);
						        detailData.setDisplayPricing(displayPricing);
						        detailData.setItemId(itemId);
						        detailData.setBrandId(CommonUtility.validateNumber(getSolrFieldValue(doc,"brandID")));
						        detailData.setBrandName(CommonUtility.validateString(getSolrFieldValue(doc,"brand")));
						        detailData.setItemPriceId(CommonUtility.validateNumber(getSolrFieldValue(doc,"itemPriceId")));
						        detailData.setBrandImage(CommonUtility.validateString(getSolrFieldValue(doc,"brandImage")));    	
						        detailData.setPartNumber(CommonUtility.validateString(getSolrFieldValue(doc,"partnumber")));
						        detailData.setManufacturerPartNumber(CommonUtility.validateString(getSolrFieldValue(doc,"manfpartnumber")));
						        detailData.setManufacturerName(CommonUtility.validateString(getSolrFieldValue(doc,"manufacturerName")));
						        detailData.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
						        detailData.setAltPartNumber1(CommonUtility.validateString(getSolrFieldValue(doc,"alternatePartNumber1")));
						        detailData.setAltPartNumber2(CommonUtility.validateString(getSolrFieldValue(doc,"alternatePartNumber2")));
						        detailData.setCustomerPartNumber(CommonUtility.validateString(getSolrFieldValue(doc,"customerPartNumber")));
						        detailData.setItemFeature(CommonUtility.validateString(getSolrFieldValue(doc,"itemFeatures")));
						        detailData.setUpc(CommonUtility.validateString(getSolrFieldValue(doc,"upc")));
						        detailData.setShortDesc(CommonUtility.validateString(getSolrFieldValue(doc,"description")));
						        detailData.setOverRidePriceRule(CommonUtility.validateString(getSolrFieldValue(doc,"overRidePriceRule")));
						        detailData.setMarketingText(CommonUtility.validateString(getSolrFieldValue(doc,"itemMarketingText")));
					        	detailData.setMetaDesc(CommonUtility.validateString(getSolrFieldValue(doc,"metaDesc")));
					        	detailData.setMetaKeyword(CommonUtility.validateString(getSolrFieldValue(doc,"metaKeyword")));
					        	detailData.setPageTitle(CommonUtility.validateString(getSolrFieldValue(doc,"pageTitle")));
					        	detailData.setIsImap(CommonUtility.validateString(getSolrFieldValue(doc,"imapFlag")));
					        	detailData.setPrice(CommonUtility.validateDoubleNumber(getSolrFieldValue(doc,"netPrice")));
					        	detailData.setImapPrice(CommonUtility.validateDoubleNumber(getSolrFieldValue(doc,"imapPrice")));
					        	detailData.setPackDesc(CommonUtility.validateString(getSolrFieldValue(doc,"packDesc")));
					        	detailData.setUom(CommonUtility.validateString(getSolrFieldValue(doc,"uom")));
					        	detailData.setSalesUom(CommonUtility.validateString(getSolrFieldValue(doc,"salesUom")));	
					        	detailData.setProductId(CommonUtility.validateNumber(getSolrFieldValue(doc,"productId")));
					        	detailData.setPackageFlag(CommonUtility.validateNumber(getSolrFieldValue(doc,"packageFlag")));
					        	detailData.setImageType(CommonUtility.validateString(getSolrFieldValue(doc,"imageType")));
					        	detailData.setImageName(CommonUtility.validateString(getSolrFieldValue(doc,"imageName")).length()>0?CommonUtility.validateString(getSolrFieldValue(doc,"imageName")):"NoImage.png");
					        	detailData.setNotes(CommonUtility.validateString(getSolrFieldValue(doc,"notes")));
					        	detailData.setMaterialGroup(CommonUtility.validateString(getSolrFieldValue(doc,"materialGroup")));
					        	detailData.setCollectionAttr(CommonUtility.validateString(getSolrFieldValue(doc,"collectionAttribute")));
					        	detailData.setAvailQty(CommonUtility.validateNumber(getSolrFieldValue(doc,"qtyAvailable")));
					        	detailData.setWeight(CommonUtility.validateDoubleNumber(getSolrFieldValue(doc,"weight")));
					        	detailData.setWeightUom(CommonUtility.validateString(getSolrFieldValue(doc,"weightUom")));
					        	detailData.setInvoiceDesc(CommonUtility.validateString(getSolrFieldValue(doc,"invoiceDescription")));
					        	//detailData.setClearance(CommonUtility.validateString(getSolrFieldValue(doc,"clearanceFlag")));
					      
					        	detailData.setItemType(CommonUtility.validateString(getSolrFieldValue(doc,"itemType")));
					        	detailData.setRentals(CommonUtility.validateString(getSolrFieldValue(doc,"rentals")));
					        	
					        	if(CommonUtility.validateNumber(getSolrFieldValue(doc,"packageQty"))>0){
					        		detailData.setPackageQty(CommonUtility.validateNumber(getSolrFieldValue(doc,"packageQty")));
					        	}else{
					        		detailData.setPackageQty(packageQty);
					        	}
					        	if(CommonUtility.validateNumber(getSolrFieldValue(doc,"minordqty"))>0){
									detailData.setMinOrderQty(CommonUtility.validateNumber(getSolrFieldValue(doc,"minordqty")));
								}else{
									detailData.setMinOrderQty(1);
								}
					        	if(CommonUtility.validateNumber(getSolrFieldValue(doc,"orderQtyInterval"))>0){
					        		detailData.setOrderInterval(CommonUtility.validateNumber(getSolrFieldValue(doc,"orderQtyInterval")));
								}else{
									detailData.setOrderInterval(1);
								}
					        	if(CommonUtility.validateNumber(getSolrFieldValue(doc,"salesQty"))>0){
					        		detailData.setSaleQty(CommonUtility.validateNumber(getSolrFieldValue(doc,"salesQty")));
					        	}else{
					        		detailData.setSaleQty(packageQty);
					        	}
					        	//-----------------------------------------------------
					        	
					        	server1 = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
								server1.setParser(new XMLResponseParser());
								req1 = new QueryRequest();
								req1.setMethod(METHOD.POST);
								query1 = new SolrQuery();
								
								query1.setQuery("itemid:("+itemId+")");
								fq2 = "defaultCategory:\"Y\"";
								query1.setFilterQueries(fq2);
								query1.setStart(0);
								query1.setRows(1);
								response1 = server1.query(query1);
								documents1 = response1.getResults();
								
								Iterator<SolrDocument> itr2 = documents1.iterator();
								while (itr2.hasNext()) {
									SolrDocument doc2 = itr2.next();
						        	detailData.setProductName(getSolrFieldValue(doc2,"productName"));
						        	detailData.setProductDescription(getSolrFieldValue(doc2,"productDesc"));
						        	detailData.setProductCategoryImageName(getSolrFieldValue(doc2,"productImageName"));
						        	detailData.setProductCategoryImageType(getSolrFieldValue(doc2,"productImageType"));
						        	detailData.setSalesUom(getSolrFieldValue(doc2,"salesUom"));
								}
								//-----------------------------------------------------
					        	
					        	imgList = CommonUtility.validateString(getSolrFieldValue(doc,"imageList"));
					        	attributeList = CommonUtility.validateString(getSolrFieldValue(doc,"attrList"));
					        	detailData.setAttrValue(getSolrFieldValue(doc,"attrList"));
					        	//check
					        	//detailData.setCollectionAttr(getSolrFieldValue(doc,"COLLECTION"));
					        	docList = getSolrFieldValue(doc,"documentList");
					        	
					        	videoList = getSolrFieldValue(doc,"videoList");
					        	linkedItems = getSolrFieldValue(doc,"linkedItems");
					        	
							//}
							
						}

					}
					itemDetailList.put("itemData", itemDetailObject);
					
					if(CommonUtility.validateString(imgList).length()>0){
						 itemDetailObject = new ArrayList<ProductsModel>();
						 itemDetailObject = new ArrayList<ProductsModel>(ProductHunterSolrUltimate.getImageList(imgList));
			        	 itemDetailList.put("itemImages", itemDetailObject);
		        	 }
					
					itemDetailObject = new ArrayList<ProductsModel>();
					if(CommonUtility.validateString(attributeList).length()>0){
						String[] attributeArray = CommonUtility.validateString(attributeList).split("}~}");
						 if(attributeArray!=null && attributeArray.length>0){
							 
							 for(String singleAttributeString : attributeArray){
								 String[] attributeValueArray = CommonUtility.validateString(singleAttributeString).split("\\|~\\|");
								 if(attributeValueArray!=null && attributeValueArray.length>0){
									 if(attributeValueArray[1]!=null && CommonUtility.validateString(attributeValueArray[1]).length()>0){
										ProductsModel detailData = new ProductsModel();
										detailData.setAttrName(CommonUtility.validateString(attributeValueArray[0]));
										detailData.setAttrValue(CommonUtility.validateString(attributeValueArray[1]));
										itemDetailObject.add(detailData);
									 }
									 
								 }
							 }
						 }
		        	 }
					itemDetailList.put("itemAttributes", itemDetailObject);
					
					itemDetailObject = new ArrayList<ProductsModel>();
		        	if(CommonUtility.validateString(docList).length()>0){
			         itemDetailObject = new ArrayList<ProductsModel>(ProductHunterSolrUltimate.getDoucmentList(docList));
			        	 
		        	}
		        	itemDetailList.put("itemDocuments", itemDetailObject);
		        	 
		        	itemDetailObject = new ArrayList<ProductsModel>();
		        	if(CommonUtility.validateString(videoList).length()>0){
			         itemDetailObject = new ArrayList<ProductsModel>(ProductHunterSolrUltimate.getVideoList(videoList));
		        	}
		        	itemDetailList.put("videoLinks", itemDetailObject);
		        	
		       	 	itemDetailObject = new ArrayList<ProductsModel>();
		        	if(CommonUtility.validateString(linkedItems).length()>0){
		        	 itemDetailObject = ProductHunterSolrUltimate.getLinkedItemList(linkedItems, subsetId, generalSubset);
		        	}
		        	itemDetailList.put("linkedItems", itemDetailObject);
		        	
			}
			catch (Exception e) {
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server1);
				ConnectionManager.closeSolrClientConnection(server);
			}
			return itemDetailList;
		}
		
		public static HashMap<String, ArrayList<ProductsModel>> itemDetailDataV2(String taxonomyId,String eclipseSessionId,String userName,String entityId,String homeTeritory,int userId,String type,String wareHousecode,String customerId,String customerCountry,int generalSubset,int subsetId)
		{
			double price = 0.00;
			double discountAmount = 0.00;
			HttpSolrServer server1 = null;
			HttpSolrServer server = null;
			HashMap<String, ArrayList<ProductsModel>> itemDetailList = new HashMap<String, ArrayList<ProductsModel>>();
			try{
				
				ArrayList<ProductsModel> itemDetailObject = new ArrayList<ProductsModel>();
			    ArrayList<String> partIdentifier = new ArrayList<String>();
			    ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
			    String displayPricing = "Y";
			    String docList = null;
			    String imgList = null;
			    String videoList = null;
			    String linkedItems = null;
			    String attributeList = null;
		        int itemId = 0;
		        int resultCount = 0;
				server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
				server1.setParser(new XMLResponseParser());
				QueryRequest req1 = new QueryRequest();
				req1.setMethod(METHOD.POST);
				SolrQuery query1 = new SolrQuery();
		
				 String indexType = "PH_SEARCH_"+subsetId;
				 if(generalSubset>0 && generalSubset!=subsetId) {
					 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
					 if(!siteNameSolr.equalsIgnoreCase(""))
					 {
						 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
					 }

				}
					 
					 //indexType = "PH_SEARCH_"+generalSubset+"_"+subsetId;
				query1.setQuery("itemPriceId:("+taxonomyId+")");
				String fq2 = "type:"+indexType;
				query1.setFilterQueries(fq2);
				query1.setStart(0);
				query1.setRows(CommonUtility.validateString(taxonomyId).split(" OR ").length);
				System.out.println("ItemDetail Price Query : " + query1);
				
				QueryResponse response1 = server1.query(query1);
				
				SolrDocumentList documents1 = response1.getResults();
				resultCount = (int) response1.getResults().getNumFound();
				

				
				Iterator<SolrDocument> itr1 = documents1.iterator();
				NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
				
				while (itr1.hasNext()) {
					
					SolrDocument doc = itr1.next();
					ProductsModel iList = new ProductsModel();
						itemId = Integer.parseInt(doc.getFieldValue("itemid").toString());
					
							iList.setItemId(itemId);
							int minOrdQty = 1;
							int orderQtyInterval = 1;
							int saleQty = 1;
							int packageQty = 1;
							int packageFlag = 0;
							String mOrdQty = null;
							String iPrice ="";
							if(doc.getFieldValue("price")!=null)
							{
								iPrice = doc.getFieldValue("price").toString();
							}
								
							
							double itemPrice = 0d;
							if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
							{
								itemPrice = Double.parseDouble(iPrice);
							}
							
							
							iList.setPrice(itemPrice);
							if(doc.getFieldValue("uom")!=null)
							iList.setUom(doc.getFieldValue("uom").toString());
							iList.setItemPriceId(Integer.parseInt(doc.getFieldValue("itemPriceId").toString()));
							
							
							
														
							if(doc.getFieldValue("customerPartNumber")!=null)
								iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
							
							if(doc.getFieldValue("materialGroup")!=null)
								iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
							
							if(doc.getFieldValue("minordqty")!=null)
							{
								if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
									minOrdQty = Integer.parseInt(doc.getFieldValue("minordqty").toString());
								if(minOrdQty==0)
									minOrdQty = 1;
							}
							if(doc.getFieldValue("orderQtyInterval")!=null)
							{
								if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
									orderQtyInterval = Integer.parseInt(doc.getFieldValue("orderQtyInterval").toString());
								if(orderQtyInterval==0)
									orderQtyInterval = 1;
							}
							if(doc.getFieldValue("salesQty")!=null)
							{
								if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
									saleQty = Integer.parseInt(doc.getFieldValue("salesQty").toString());
								
								if(saleQty==0)
									saleQty = 1;
							}
							
							if(doc.getFieldValue("packageFlag")!=null)
							{
								if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
									packageFlag = Integer.parseInt(doc.getFieldValue("packageFlag").toString());
							}
							
							if(doc.getFieldValue("packageQty")!=null)
							{
								if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
								{
									packageQty = Integer.parseInt(doc.getFieldValue("packageQty").toString());
								}
								
							}
							
							if(doc.getFieldValue("packDesc")!=null)
							{
								String packDesc = doc.getFieldValue("packDesc").toString();
								iList.setPackDesc(packDesc);
							}
							
							if(doc.getFieldValue("seourl")!=null){
								iList.setSupercedeurl(doc.getFieldValue("seourl").toString());
							}
							if(doc.getFieldValue("overRidePriceRule")!=null){
                                iList.setOverRidePriceRule((String)doc.getFieldValue("overRidePriceRule"));
                            }
							iList.setClearance(CommonUtility.validateString(getSolrFieldValue(doc,"clearanceFlag")));
							iList.setSaleQty(saleQty);
							iList.setMinOrderQty(minOrdQty);
							iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
							iList = generateMainItemObject(itemId,iList);
							itemDetailObject.add(iList);
					
					
				}
				
				
				
				server = ConnectionManager.getSolrClientConnection(solrURL+"/itemdetaildata");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				query.setQuery("itemid:"+itemId);
		       // query.setQuery("itemid:"+itemId);
		       query.setStart(0);
		       query.setRows(1);

				System.out.println("ItemDetail Query : " + query);
				QueryResponse response = server.query(query);

				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				Iterator<SolrDocument> itr = documents.iterator();
				
				while (itr.hasNext()) {
					SolrDocument doc = itr.next();
						for(ProductsModel detailData : itemDetailObject){
							
							//if(itemId==detailData.getItemId() && CommonUtility.validateNumber(taxonomyId)==CommonUtility.validateNumber(getSolrFieldValue(doc,"itemPriceId"))){
	
	      
						        detailData.setItemId(itemId);
						        
						        detailData.setItemFeature(CommonUtility.validateString(getSolrFieldValue(doc,"itemFeatures")));
						        detailData.setMarketingText(CommonUtility.validateString(getSolrFieldValue(doc,"itemMarketingText")));
					        	detailData.setMetaDesc(CommonUtility.validateString(getSolrFieldValue(doc,"metaDesc")));
					        	detailData.setMetaKeyword(CommonUtility.validateString(getSolrFieldValue(doc,"metaKeyword")));	
					        	detailData.setWeight(CommonUtility.validateDoubleNumber(getSolrFieldValue(doc,"weight")));
					        	detailData.setWeightUom(CommonUtility.validateString(getSolrFieldValue(doc,"weightUom")));
					        	detailData.setInvoiceDesc(CommonUtility.validateString(getSolrFieldValue(doc,"invoiceDescription")));
					        	//detailData.setClearance(CommonUtility.validateString(getSolrFieldValue(doc,"clearanceFlag")));
					      
					        	
					        	//-----------------------------------------------------
					        	
					        	
					        	
					        	imgList = CommonUtility.validateString(getSolrFieldValue(doc,"imageList"));
					        	attributeList = CommonUtility.validateString(getSolrFieldValue(doc,"attrList"));
					        	detailData.setAttrValue(getSolrFieldValue(doc,"attrList"));
					        	//check
					        	//detailData.setCollectionAttr(getSolrFieldValue(doc,"COLLECTION"));
					        	docList = getSolrFieldValue(doc,"documentList");
					        	
					        	videoList = getSolrFieldValue(doc,"videoList");
					        	linkedItems = getSolrFieldValue(doc,"linkedItems");
					        	
							//}
							
						}

					}
					itemDetailList.put("itemData", itemDetailObject);
					
					if(CommonUtility.validateString(imgList).length()>0){
						 itemDetailObject = new ArrayList<ProductsModel>();
						 itemDetailObject = new ArrayList<ProductsModel>(ProductHunterSolrUltimate.getImageList(imgList));
			        	 itemDetailList.put("itemImages", itemDetailObject);
		        	 }
					
					itemDetailObject = new ArrayList<ProductsModel>();
					if(CommonUtility.validateString(attributeList).length()>0){
						String[] attributeArray = CommonUtility.validateString(attributeList).split("}~}");
						 if(attributeArray!=null && attributeArray.length>0){
							 
							 for(String singleAttributeString : attributeArray){
								 String[] attributeValueArray = CommonUtility.validateString(singleAttributeString).split("\\|~\\|");
								 if(attributeValueArray!=null && attributeValueArray.length>0){
									 if(attributeValueArray[1]!=null && CommonUtility.validateString(attributeValueArray[1]).length()>0){
										ProductsModel detailData = new ProductsModel();
										detailData.setAttrName(CommonUtility.validateString(attributeValueArray[0]));
										detailData.setAttrValue(CommonUtility.validateString(attributeValueArray[1]));
										itemDetailObject.add(detailData);
									 }
									 
								 }
							 }
						 }
		        	 }
					itemDetailList.put("itemAttributes", itemDetailObject);
					
					itemDetailObject = new ArrayList<ProductsModel>();
		        	if(CommonUtility.validateString(docList).length()>0){
			         itemDetailObject = new ArrayList<ProductsModel>(ProductHunterSolrUltimate.getDoucmentList(docList));
			        	 
		        	}
		        	itemDetailList.put("itemDocuments", itemDetailObject);
		        	 
		        	itemDetailObject = new ArrayList<ProductsModel>();
		        	if(CommonUtility.validateString(videoList).length()>0){
			         itemDetailObject = new ArrayList<ProductsModel>(ProductHunterSolrUltimate.getVideoList(videoList));
		        	}
		        	itemDetailList.put("videoLinks", itemDetailObject);
		        	
		       	 	itemDetailObject = new ArrayList<ProductsModel>();
		        	if(CommonUtility.validateString(linkedItems).length()>0){
		        	 itemDetailObject = ProductHunterSolrUltimate.getLinkedItemList(linkedItems, subsetId, generalSubset);
		        	}
		        	itemDetailList.put("linkedItems", itemDetailObject);
		        	
			}
			catch (Exception e) {
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server1);
				ConnectionManager.closeSolrClientConnection(server);
			}
			return itemDetailList;
		}
		

		
		public static String getSolrFieldValue(SolrDocument doc, String fieldName)
		{
			String fieldValue = "";
			try
			{
				if(doc.getFieldValue(fieldName)!=null)
					fieldValue = doc.getFieldValue(fieldName).toString();
			}
			catch (Exception e) {
				fieldValue = "";
			}
			return fieldValue;
		}

		public static ArrayList<ProductsModel> getClearanceProduct(int subsetId, int generalSubset, int fromRow, int rowSize, boolean isClearanceGroupList) {
			ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
			HttpSolrServer server = null;
			HttpSolrServer server1 = null;
			String idList = "";
			String c ="";
			 try{
				 String sortField = "";
				 String indexType = "PH_SEARCH_"+subsetId;
				 if(generalSubset>0 && generalSubset!=subsetId)
					indexType = "PH_SEARCH_"+generalSubset+"_"+subsetId;
				    server = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					
					String fq = "{!join from=itemid to=itemid fromIndex=mainitemdata}defaultCategory:Y";
					if(!siteNameSolr.equalsIgnoreCase(""))
					{
						fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_mainitemdata method=crossCollection}defaultCategory:Y";
					}
					fq = fq +"~" +"clearanceFlag:\"Y\"";
					query = new SolrQuery();
					query.setQuery("type:"+indexType);
					query.setStart(fromRow);
					if(rowSize>0) {
						query.setRows(rowSize);
					}
					
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CLEARANCE_PRODUCT_GROUP_NAME_LOOKUP_FIELD")).length()>0) {
						if(isClearanceGroupList) {
							 fq = fq +"~" +CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CLEARANCE_PRODUCT_GROUP_NAME_LOOKUP_FIELD"))+":[* TO *]";
							 sortField = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CLEARANCE_PRODUCT_GROUP_NAME_LOOKUP_FIELD"));
							 ORDER sortOrder = SolrQuery.ORDER.asc;
							 query.addSortField(sortField, sortOrder);
							 //query.addSortField("materialNumber", sortOrder);
						}else {
							 fq = fq +"~" +"-"+CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CLEARANCE_PRODUCT_GROUP_NAME_LOOKUP_FIELD"))+":[* TO *]";
						}
					}
					
					query.setFilterQueries(fq.split("~"));
					System.out.println("Feature Prodcut query : " + query);
					
					QueryResponse response = server.query(query);
					SolrDocumentList documents = response.getResults();
					int resultCount = (int) response.getResults().getNumFound();
					if(isClearanceGroupList && rowSize<=0) {
						query.setRows(resultCount);
						System.out.println("Feature Prodcut query  second Query: " + query);
						response = server.query(query);
						documents = response.getResults();
						resultCount = (int) documents.getNumFound();
						rowSize = resultCount;
					}
					Iterator<SolrDocument> itr = documents.iterator();
					itemLevelFilterData = new ArrayList<ProductsModel>();
					System.out.println("DOCUMENTS");
					
					itemLevelFilterData = new ArrayList<ProductsModel>();
					SolrDocument doc = new SolrDocument();
					
					while (itr.hasNext()) {
						ProductsModel iList = null;
						iList = new ProductsModel();
						doc = itr.next();
						int minOrdQty = 1;
						int orderQtyInterval = 1;
						int saleQty = 1;
						int packageQty = 1;
						int packageFlag = 0;
						String mOrdQty = null;
						String iPrice ="";
						int itemId = Integer.parseInt(doc.getFieldValue("itemid").toString());
						iList.setItemId(itemId);
						idList = idList + c + itemId;
						c = " OR ";
						if(doc.getFieldValue("price")!=null)
						{
							iPrice = doc.getFieldValue("price").toString();
						}
							
						double itemPrice = 0d;
						if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
						{
							itemPrice = Double.parseDouble(iPrice);
						}
						
						iList.setPrice(itemPrice);
						if(doc.getFieldValue("uom")!=null)
						iList.setUom(doc.getFieldValue("uom").toString());
						iList.setItemPriceId(Integer.parseInt(doc.getFieldValue("itemPriceId").toString()));
						
						if(doc.getFieldValue("customerPartNumber")!=null)
							iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
						
						if(doc.getFieldValue("materialGroup")!=null)
							iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
						
						if(doc.getFieldValue("minordqty")!=null)
						{
							if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
								minOrdQty = Integer.parseInt(doc.getFieldValue("minordqty").toString());
							if(minOrdQty==0)
								minOrdQty = 1;
						}
						if(doc.getFieldValue("orderQtyInterval")!=null)
						{
							if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
								orderQtyInterval = Integer.parseInt(doc.getFieldValue("orderQtyInterval").toString());
							if(orderQtyInterval==0)
								orderQtyInterval = 1;
						}
						if(doc.getFieldValue("salesQty")!=null)
						{
							if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
								saleQty = Integer.parseInt(doc.getFieldValue("salesQty").toString());
							if(saleQty==0)
								saleQty = 1;
						}
						
						if(doc.getFieldValue("packageFlag")!=null)
						{
							if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
								packageFlag = Integer.parseInt(doc.getFieldValue("packageFlag").toString());
						}
						
						if(doc.getFieldValue("packageQty")!=null)
						{
							if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
							{
								packageQty = Integer.parseInt(doc.getFieldValue("packageQty").toString());
							}
							
						}
						
						if(doc.getFieldValue("packDesc")!=null)
						{
							String packDesc = doc.getFieldValue("packDesc").toString();
							iList.setPackDesc(packDesc);
						}
						
						iList.setSaleQty(saleQty);
						iList.setMinOrderQty(minOrdQty);
						iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
						if(doc.getFieldValue("clearanceFlag")!=null) {
							iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
						}
						itemLevelFilterData.add(iList);
						
					}
					
					if(itemLevelFilterData.size()>0)
					{
						server1 = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
						server1.setParser(new XMLResponseParser());
						QueryRequest req1 = new QueryRequest();
						req1.setMethod(METHOD.POST);
						SolrQuery query1 = new SolrQuery();
						
						query1.setQuery("itemid:("+idList+")");
						String fq2 = "defaultCategory:\"Y\"";
						query1.setFilterQueries(fq2);
						query1.setStart(0);
						query1.setRows(rowSize);
						
						QueryResponse response1 = server1.query(query1);
						
						SolrDocumentList documents1 = response1.getResults();
						resultCount = (int) response1.getResults().getNumFound();

						Iterator<SolrDocument> itr1 = documents1.iterator();
						NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
						
						while (itr1.hasNext()) {
							
							doc = itr1.next();
							for(ProductsModel iList : itemLevelFilterData)
							{
								int itemId = Integer.parseInt(doc.getFieldValue("itemid").toString());
								if(itemId==iList.getItemId())
								{
									
									iList.setItemId(Integer.parseInt(doc.getFieldValue("itemid").toString()));
									iList.setPartNumber(doc.getFieldValue("partnumber").toString());
									iList.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
									iList.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
									//if(doc.getFieldValue("brand")!=null)
									//iList.setManufacturerName(doc.getFieldValue("brand").toString());
									
									iList.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
									iList.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
									iList.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
									iList.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
									if(doc.getFieldValue("manfpartnumber")!=null)
									iList.setManufacturerPartNumber(doc.getFieldValue("manfpartnumber").toString());
									if(doc.getFieldValue("description")!=null)
									iList.setShortDesc(doc.getFieldValue("description").toString());
									iList.setEcommerceProductTitle(getSolrFieldValue(doc,"ecommerceProductTitle").replaceAll("\\<.*?>",""));
									iList.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
									iList.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
									iList.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
									iList.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
									iList.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
									iList.setResultCount(resultCount);
									String imageName = null;
					       			String imageType = null;
					       			
					       			String itemUrl = doc.getFieldValue("brand").toString();
					       			if(doc.getFieldValue("manfpartnumber")!=null)
					       			itemUrl = doc.getFieldValue("brand").toString()+" "+doc.getFieldValue("manfpartnumber").toString();
					       			
					       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
					       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
					       			}
					       			
					            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					       			String pattern = "[^A-Za-z0-9]";
									 itemUrl = itemUrl.replaceAll(pattern,"-");
									 itemUrl = itemUrl.replaceAll("\\s","-");
					            	itemUrl = itemUrl.replaceAll("\\s","-");
					            	iList.setItemUrl(itemUrl);
					       			
					       			if(doc.getFieldValue("imageName")!=null)
					       				imageName = doc.getFieldValue("imageName").toString();
					       			
					       					       			
					       			if(doc.getFieldValue("imageType")!=null)
					       				imageType = doc.getFieldValue("imageType").toString();
					       			if(doc.getFieldValue("upc")!=null)
					       				iList.setUpc(doc.getFieldValue("upc").toString());	
					       			
					       			if(imageName==null)
					       			{
					       				imageName = "NoImage.png";
					       				imageType = "IMAGE";
					       			}
					       			iList.setImageName(imageName.trim());
					       			iList.setImageType(imageType);
								}
							}
						}
						 
			               
					}
		     
			}catch (Exception e) {
				 e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server);
				ConnectionManager.closeSolrClientConnection(server1);
			}
			 return itemLevelFilterData;
		}
		
		public static ArrayList<ProductsModel> getFeatureProduct(int subsetId, int generalSubset, int fromRow, int rowSize, boolean isFeaturedGroupList) {
			ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
			HttpSolrServer server = null;
			HttpSolrServer server1 = null;
			String idList = "";
			String c ="";
			 try{
				 String sortField = "";
				 String indexType = "PH_SEARCH_"+subsetId;
				 if(generalSubset>0 && generalSubset!=subsetId) {
					 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
					 if(!siteNameSolr.equalsIgnoreCase(""))
					 {
						 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
					 }
				}

				    server = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					
					
					String fq = "{!join from=itemid to=itemid fromIndex=mainitemdata}defaultCategory:Y";//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
					if(!siteNameSolr.equalsIgnoreCase(""))
					{
						fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_mainitemdata method=crossCollection}defaultCategory:Y";
					}
					fq = fq +"~" +"isFeatureProduct:\"Y\"";
					query = new SolrQuery();
					query.setQuery("type:"+indexType);
					query.setStart(fromRow);
					if(rowSize>0) {
						query.setRows(rowSize);
					}
					
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FEATURE_PRODUCT_GROUP_NAME_LOOKUP_FIELD")).length()>0) {
						if(isFeaturedGroupList) {
							 fq = fq +"~" +CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FEATURE_PRODUCT_GROUP_NAME_LOOKUP_FIELD"))+":[* TO *]";
							 sortField = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FEATURE_PRODUCT_GROUP_NAME_LOOKUP_FIELD"));
							 ORDER sortOrder = SolrQuery.ORDER.asc;
							 query.addSortField(sortField, sortOrder);
							 //query.addSortField("materialNumber", sortOrder);
						}else {
							 fq = fq +"~" +"-"+CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FEATURE_PRODUCT_GROUP_NAME_LOOKUP_FIELD"))+":[* TO *]";
						}
					}
					
					query.setFilterQueries(fq.split("~"));
					System.out.println("Feature Prodcut query : " + query);
					
					QueryResponse response = server.query(query);
					SolrDocumentList documents = response.getResults();
					int resultCount = (int) response.getResults().getNumFound();
					if(isFeaturedGroupList && rowSize<=0) {
						query.setRows(resultCount);
						System.out.println("Feature Prodcut query  second Query: " + query);
						response = server.query(query);
						documents = response.getResults();
						resultCount = (int) documents.getNumFound();
						rowSize = resultCount;
					}
					Iterator<SolrDocument> itr = documents.iterator();
					itemLevelFilterData = new ArrayList<ProductsModel>();
					System.out.println("DOCUMENTS");
					
					itemLevelFilterData = new ArrayList<ProductsModel>();
					SolrDocument doc = new SolrDocument();
					
					while (itr.hasNext()) {
						ProductsModel iList = null;
						iList = new ProductsModel();
						doc = itr.next();
						int minOrdQty = 1;
						int orderQtyInterval = 1;
						int saleQty = 1;
						int packageQty = 1;
						int packageFlag = 0;
						String mOrdQty = null;
						String iPrice ="";
						int itemId = Integer.parseInt(doc.getFieldValue("itemid").toString());
						iList.setItemId(itemId);
						idList = idList + c + itemId;
						c = " OR ";
						if(doc.getFieldValue("price")!=null)
						{
							iPrice = doc.getFieldValue("price").toString();
						}
							
						
						double itemPrice = 0d;
						if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
						{
							itemPrice = Double.parseDouble(iPrice);
						}
						
						iList.setPrice(itemPrice);
						if(doc.getFieldValue("uom")!=null)
						iList.setUom(doc.getFieldValue("uom").toString());
						iList.setItemPriceId(Integer.parseInt(doc.getFieldValue("itemPriceId").toString()));
						
						
						
													
						if(doc.getFieldValue("customerPartNumber")!=null)
							iList.setCustomerPartNumber(doc.getFieldValue("customerPartNumber").toString());
						
						if(doc.getFieldValue("materialGroup")!=null)
							iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());
						
						if(doc.getFieldValue("minordqty")!=null)
						{
							if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
								minOrdQty = Integer.parseInt(doc.getFieldValue("minordqty").toString());
							if(minOrdQty==0)
								minOrdQty = 1;
						}
						if(doc.getFieldValue("orderQtyInterval")!=null)
						{
							if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
								orderQtyInterval = Integer.parseInt(doc.getFieldValue("orderQtyInterval").toString());
							if(orderQtyInterval==0)
								orderQtyInterval = 1;
						}
						if(doc.getFieldValue("salesQty")!=null)
						{
							if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
								saleQty = Integer.parseInt(doc.getFieldValue("salesQty").toString());
							
							if(saleQty==0)
								saleQty = 1;
						}
						
						if(doc.getFieldValue("packageFlag")!=null)
						{
							if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
								packageFlag = Integer.parseInt(doc.getFieldValue("packageFlag").toString());
						}
						
						if(doc.getFieldValue("packageQty")!=null)
						{
							if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
							{
								packageQty = Integer.parseInt(doc.getFieldValue("packageQty").toString());
							}
							
						}
						
						if(doc.getFieldValue("packDesc")!=null)
						{
							String packDesc = doc.getFieldValue("packDesc").toString();
							iList.setPackDesc(packDesc);
						}
						
						iList.setSaleQty(saleQty);
						iList.setMinOrderQty(minOrdQty);
						iList.setOrderInterval(orderQtyInterval);//orderQtyInterval
						
						itemLevelFilterData.add(iList);
						
					}
					
					if(itemLevelFilterData.size()>0)
					{
						server1 = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
						server1.setParser(new XMLResponseParser());
						QueryRequest req1 = new QueryRequest();
						req1.setMethod(METHOD.POST);
						SolrQuery query1 = new SolrQuery();
						
						query1.setQuery("itemid:("+idList+")");
						String fq2 = "defaultCategory:\"Y\"";
						query1.setFilterQueries(fq2);
						query1.setStart(0);
						query1.setRows(rowSize);
						
						QueryResponse response1 = server1.query(query1);
						
						SolrDocumentList documents1 = response1.getResults();
						resultCount = (int) response1.getResults().getNumFound();
						

						
						Iterator<SolrDocument> itr1 = documents1.iterator();
						NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
						
						while (itr1.hasNext()) {
							
							doc = itr1.next();
							for(ProductsModel iList : itemLevelFilterData)
							{
								int itemId = Integer.parseInt(doc.getFieldValue("itemid").toString());
								
								
							
								if(itemId==iList.getItemId())
								{
									
									iList.setItemId(Integer.parseInt(doc.getFieldValue("itemid").toString()));
									iList.setPartNumber(doc.getFieldValue("partnumber").toString());
									iList.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
									iList.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
									//if(doc.getFieldValue("brand")!=null)
									//iList.setManufacturerName(doc.getFieldValue("brand").toString());
									
									iList.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
									iList.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
									iList.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
									iList.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
									if(doc.getFieldValue("manfpartnumber")!=null)
									iList.setManufacturerPartNumber(doc.getFieldValue("manfpartnumber").toString());
									if(doc.getFieldValue("description")!=null)
									iList.setShortDesc(doc.getFieldValue("description").toString());
									iList.setEcommerceProductTitle(getSolrFieldValue(doc,"ecommerceProductTitle").replaceAll("\\<.*?>",""));
									iList.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
									iList.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
									iList.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
									iList.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
									iList.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
									iList.setResultCount(resultCount);
									String imageName = null;
					       			String imageType = null;
					       			
					       			String itemUrl = doc.getFieldValue("brand").toString();
					       			if(doc.getFieldValue("manfpartnumber")!=null)
					       			itemUrl = doc.getFieldValue("brand").toString()+" "+doc.getFieldValue("manfpartnumber").toString();
					       			
					       			if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
					       				itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
					       			}
					       			
					            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					       			String pattern = "[^A-Za-z0-9]";
									 itemUrl = itemUrl.replaceAll(pattern,"-");
									 itemUrl = itemUrl.replaceAll("\\s","-");
					            	itemUrl = itemUrl.replaceAll("\\s","-");
					            	iList.setItemUrl(itemUrl);
					       			
					       			if(doc.getFieldValue("imageName")!=null)
					       				imageName = doc.getFieldValue("imageName").toString();
					       			
					       					       			
					       			if(doc.getFieldValue("imageType")!=null)
					       				imageType = doc.getFieldValue("imageType").toString();
					       			if(doc.getFieldValue("upc")!=null)
					       				iList.setUpc(doc.getFieldValue("upc").toString());	
					       			
					       			if(imageName==null)
					       			{
					       				imageName = "NoImage.png";
					       				imageType = "IMAGE";
					       			}
					       			iList.setImageName(imageName.trim());
					       			iList.setImageType(imageType);
								}
							}
						}
						 
			            
						
						
			              
						   
					}
		     
			}catch (Exception e) {
				 e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server);
				ConnectionManager.closeSolrClientConnection(server1);
			}
			 return itemLevelFilterData;
		}
		
		public static String getCatalogNumber(String idList)
		{
			String catalogNumber="";
			HttpSolrServer server = null;
			try{
				server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				int resultCount = 0;
				query.setQuery("itemid:("+idList+")");
				query.setStart(0);
				query.setRows(100);
				QueryResponse response = server.query(query);
				System.out.println("Customer Part Number Query : " + query);
				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				Iterator<SolrDocument> itr = documents.iterator();
				while (itr.hasNext()) {
					SolrDocument doc = itr.next();
					if(doc.getFieldValue("custom_Catalog_ID")!=null)
					{
					catalogNumber=doc.getFieldValue("custom_Catalog_ID").toString();

					}
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server);
			}
			return catalogNumber;
		}
		
		public static HashMap<String, ArrayList<ProductsModel>> getBrandManufacturerCategory(String id,String searchType,int subsetId,int generalSubset){
			HashMap<String, ArrayList<ProductsModel>> categoryData = new HashMap<String, ArrayList<ProductsModel>>();
			HttpSolrServer server = null;
			try{
				 String fieldName = "manfId";
				 if(CommonUtility.validateString(searchType).trim().equalsIgnoreCase("BRAND"))
					 fieldName = "brandID";
				
				 String indexType = "PH_SEARCH_"+subsetId;
					if(generalSubset>0 && generalSubset!=subsetId){
						indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
					}
						
					 String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
					 if(!siteNameSolr.equalsIgnoreCase(""))
					 {
						 fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
					 }
					 String idList = "";
					 String c = "";
				 
					 	SolrQuery query = new SolrQuery();
						query.setQuery(fieldName+":"+id);
						
						query.setStart(0);
						query.setRows(1);
						query.addFacetField("categoryID");
						
						query.setFilterQueries(fq);
						if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y")){
							query.addFilterQuery("defaultProductItem:Y");
						}
						
						server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
						QueryResponse response = server .query(query);
						System.out.println("Shop By Manufacturer Query: " + query);
					
						List<FacetField> facetFeild = response.getFacetFields();
						
						LinkedHashMap<String, ArrayList<ProductsModel>>  filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
						ArrayList<ProductsModel> attrList = new ArrayList<ProductsModel>();
		         		ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();

						
						for(FacetField facetFilter:facetFeild)
						{
							ProductsModel tempVal = new ProductsModel();
							
							if(facetFilter.getValues()!=null && facetFilter.getValues().size()>0)
							{
								
								

								List<Count> attrValArr = facetFilter.getValues();
								System.out.println("Attribute Name : " + URLEncoder.encode(facetFilter.getName().replace("attr_", ""),"UTF-8"));
								for(Count attrArr : attrValArr)
								{
									idList = idList + c + attrArr.getName();
									c = " OR ";
								}
								if(attrList!=null && attrList.size()>0)
								{
								filteredList.put(facetFilter.getName().replace("attr_", ""), attrList);
								tempVal.setAttrFilterList(filteredList);
								attrFilteredList.add(tempVal);
								}
							
							}
							
						}
						
						/*if(attrFilteredList.size()>0){*/
							categoryData = taxonomyListById(subsetId, generalSubset, "category", idList);
						/*}*/
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server);
			}
			return categoryData;
		}



		public static HashMap<String, ArrayList<ProductsModel>> taxonomyListById(int subsetId,int generalSubset,String sortBy,String idList)
		{
			ArrayList<ProductsModel> taxonomyListVal = new ArrayList<ProductsModel>();
			HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
			HttpSolrServer server = null;
			try{
				ORDER sortOrder = null;
				String sortField = "";
				SolrQuery query = new SolrQuery();
				if(sortBy!=null && !sortBy.trim().equalsIgnoreCase("")){
					if(sortBy.trim().equalsIgnoreCase("category")){
						sortField = "category";
						sortOrder = SolrQuery.ORDER.asc;
					}else if(sortBy.trim().equalsIgnoreCase("category_asc")){
						sortField = "category";
						sortOrder = SolrQuery.ORDER.asc;
					}else if(sortBy.trim().equalsIgnoreCase("category_desc")){
						sortField = "category";
						sortOrder = SolrQuery.ORDER.desc;
					}else if(sortBy.trim().equalsIgnoreCase("displaySequence")){
						sortField = "displaySeq";
						sortOrder = SolrQuery.ORDER.asc;
					}else if(sortBy.trim().equalsIgnoreCase("displaySequence_asc")){
						sortField = "displaySeq";
						sortOrder = SolrQuery.ORDER.asc;
					}else if(sortBy.trim().equalsIgnoreCase("displaySequence_desc")){
						sortField = "displaySeq";
						sortOrder = SolrQuery.ORDER.desc;
					}else if(sortBy.trim().equalsIgnoreCase("partnum_asc")){
						sortField = "partnumber";
						sortOrder = SolrQuery.ORDER.asc;
					}else{
						sortField = "displaySeq";
						sortOrder = SolrQuery.ORDER.asc;
					}
				}else{
					sortField = "displaySeq";
					sortOrder = SolrQuery.ORDER.asc;
				}
				
				String indexType = "PH_SEARCH_"+subsetId;
				if(generalSubset>0 && generalSubset!=subsetId){
					 indexType = "PH_SEARCH_"+generalSubset+"_"+subsetId;
				}
				server = ConnectionManager.getSolrClientConnection(solrURL+"/catalogdata");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				
				String fq="type:"+indexType+"~"+"categoryID:("+idList+")";
				int resultCount = 0;
				query.setQuery("*:*");
				//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
				String attrFtr[] = fq.split("~");
				query.setStart(0);
				query.setRows(100);
				
				query.addSortField(sortField, sortOrder);
				query.setFilterQueries(attrFtr);
				
				QueryResponse response = server.query(query);
				System.out.println("Category Query : " + query);
				
				SolrDocumentList documents = response.getResults();
				resultCount = (int) response.getResults().getNumFound();
				List<FacetField> facetFeild = response.getFacetFields();
				Iterator<SolrDocument> itr = documents.iterator();
				while (itr.hasNext()) {
					
					
					SolrDocument doc = itr.next();
					
					
					ProductsModel nameCode= new ProductsModel();
					String itemUrl = doc.getFieldValue("category").toString();
		        	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					String pattern = "[^A-Za-z0-9]";
					 itemUrl = itemUrl.replaceAll(pattern," ");
					 itemUrl = itemUrl.replaceAll("\\s+","-");
		        	 nameCode.setItemUrl(itemUrl);
		        	 nameCode.setCategoryCode(doc.getFieldValue("categoryID").toString());
		        	 nameCode.setCategoryName(doc.getFieldValue("category").toString());
		        	// nameCode.setCategoryCount(searchCategory.getInt("CATEGORY_COUNT"));
		        	 int levelNum = CommonUtility.validateNumber(doc.getFieldValue("levelNumber").toString());
		        	 nameCode.setLevelNumber(levelNum);
		        	 String imageName = null;
		        	 if(doc.getFieldValue("imageName")==null){
		        		 imageName = "NoImage.png";
		        	 }else{
		        		 imageName = doc.getFieldValue("imageName").toString();
		        	 }
		        	 if(doc.getFieldValue("imageType")!=null){
		        		 nameCode.setImageType(doc.getFieldValue("imageType").toString());
		        	 }
		        	 nameCode.setImageName(imageName);
		        	 taxonomyListVal.add(nameCode);
					
				}
				searchResultList.put("categoryList", taxonomyListVal);
				
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server);
			}
			return searchResultList;
		}
		
		public static HashMap<String, ArrayList<ProductsModel>> itemAutoSuggest(int subsetId,int generalSubset,int fromRow,int toRow,String requestType,int treeId,String brandId, String sortBy, int resultPerPage, boolean isCategoryNav)
		{
			HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
			int resultCount = 0;
			HttpSolrServer server = null;
			HttpSolrServer server1 = null;
			try
			{
				String indexType = "PH_SEARCH_"+subsetId;
				if(generalSubset>0 && generalSubset!=subsetId)
					indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
				String idList = "";
				String c = "";
				server = ConnectionManager.getSolrClientConnection(solrURL+"/mainitemdata");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
				

				String fq = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+indexType;
				if(!siteNameSolr.equalsIgnoreCase(""))
				{
					fq = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+indexType;
				}
				String pattern = "[^A-Za-z0-9]";

				String sortField = "partnumber";
				ORDER sortOrder = SolrQuery.ORDER.asc;

				sortField = "externalHitSort";
				sortOrder = SolrQuery.ORDER.desc;

				if (CommonUtility.validateString(requestType).equalsIgnoreCase("SEARCH") && sortBy == null ) {
					sortField = "default";
					sortBy = "default"; //On Dec 4th 2014 Aramsco and others 
					//sortField =  "externalHitSort";
					sortOrder = SolrQuery.ORDER.desc;
				}

				if(CommonUtility.validateString(sortBy).trim().length()==0){
					sortBy = "externalHitSort_desc";
				}
				if(sortBy!=null && !sortBy.trim().equalsIgnoreCase(""))
				{

					if(sortBy.trim().equalsIgnoreCase("price_high"))
					{
						sortField = "NET_PRICE";
						sortOrder = SolrQuery.ORDER.desc;
					}
					else if(sortBy.trim().equalsIgnoreCase("price_low"))
					{
						sortField = "NET_PRICE";
						sortOrder = SolrQuery.ORDER.asc;
					}
					else if(sortBy.trim().equalsIgnoreCase("erp_price_low"))
					{
						sortField = "price";
						sortOrder = SolrQuery.ORDER.asc;
					}
					else if(sortBy.trim().equalsIgnoreCase("erp_price_high"))
					{
						sortField = "price";
						sortOrder = SolrQuery.ORDER.desc;
					}

					else if(sortBy.trim().equalsIgnoreCase("brand"))
					{
						sortField = "brand";
						sortOrder = SolrQuery.ORDER.asc;
					}
					else if(sortBy.trim().equalsIgnoreCase("brand_asc"))
					{
						sortField = "brand";
						sortOrder = SolrQuery.ORDER.asc;
					}
					else if(sortBy.trim().equalsIgnoreCase("brand_desc"))
					{
						sortField = "brand";
						sortOrder = SolrQuery.ORDER.desc;
					}
					else if(sortBy.trim().equalsIgnoreCase("partnum_asc"))
					{
						sortField = "partnumber";
						sortOrder = SolrQuery.ORDER.asc;
					}
					else if(sortBy.trim().equalsIgnoreCase("partnum_desc"))
					{
						sortField = "partnumber";
						sortOrder = SolrQuery.ORDER.desc;
					}else if(sortBy.trim().equalsIgnoreCase("externalHitSort_desc")){

						sortField = "externalHitSort";
						sortOrder = SolrQuery.ORDER.desc;

					}else if(sortBy.trim().equalsIgnoreCase("externalHitSort_asc")){

						sortField = "externalHitSort";
						sortOrder = SolrQuery.ORDER.asc;

					}else if(sortBy.trim().equalsIgnoreCase("bestSellerSort_desc")){

						sortField = "bestSellerSort";
						sortOrder = SolrQuery.ORDER.desc;

					}else if(sortBy.trim().equalsIgnoreCase("bestSellerSort_asc")){

						sortField = "bestSellerSort";
						sortOrder = SolrQuery.ORDER.asc;

					}else if(sortBy.trim().equalsIgnoreCase("internalHitSort_desc")){

						sortField = "internalHitSort";
						sortOrder = SolrQuery.ORDER.desc;

					}else if(sortBy.trim().equalsIgnoreCase("internalHitSort_asc")){

						sortField = "internalHitSort";
						sortOrder = SolrQuery.ORDER.asc;

					}else if(sortBy.trim().equalsIgnoreCase("MPN_desc")){

						sortField = "manfpartnumber";
						sortOrder = SolrQuery.ORDER.desc;

					}else if(sortBy.trim().equalsIgnoreCase("MPN_asc")){

						sortField = "manfpartnumber";
						sortOrder = SolrQuery.ORDER.asc;

					}else if(sortBy.trim().equalsIgnoreCase("upc_desc")){

						sortField = "upc";
						sortOrder = SolrQuery.ORDER.desc;

					}else if(sortBy.trim().equalsIgnoreCase("upc_asc")){

						sortField = "upc";
						sortOrder = SolrQuery.ORDER.asc;

					}else if(sortBy.trim().equalsIgnoreCase("desc_desc")){

						sortField = "description_sort";
						sortOrder = SolrQuery.ORDER.desc;

					}else if(sortBy.trim().equalsIgnoreCase("desc_asc")){

						sortField = "description_sort";
						sortOrder = SolrQuery.ORDER.asc;

					}else if(sortBy.trim().equalsIgnoreCase("manufacturer_asc")){

						sortField = "manufacturerName";
						sortOrder = SolrQuery.ORDER.asc;

					}else if(sortBy.trim().equalsIgnoreCase("manufacturer_desc")){

						sortField = "manufacturerName";
						sortOrder = SolrQuery.ORDER.desc;

					}else if(sortBy.trim().equalsIgnoreCase("popularity_desc")){
						sortField = "popularity";
						sortOrder = SolrQuery.ORDER.desc;
					}else if(sortBy.trim().equalsIgnoreCase("elevate")){
						sortField = "elevate";
						sortOrder = SolrQuery.ORDER.desc;
					}else if(sortBy.trim().equalsIgnoreCase("manufacturerDisplayNameSort_asc")){
						sortField = "manufacturerDisplayNameSort";
						sortOrder = SolrQuery.ORDER.asc;
				    }else if(sortBy.trim().equalsIgnoreCase("manufacturerDisplayNameSort_desc")){
						sortField = "manufacturerDisplayNameSort";
						sortOrder = SolrQuery.ORDER.desc;
				    }else if (sortBy.trim().equalsIgnoreCase("altpartnum_asc")){                                                       
				          sortField = "alternatePartNumber1";                          
				          sortOrder = SolrQuery.ORDER.asc;                 
				    }else if (sortBy.trim().equalsIgnoreCase("altpartnum_desc")){                      
				          sortField = "alternatePartNumber1";                          
				          sortOrder = SolrQuery.ORDER.desc;                              
				    }else{
						if(CommonUtility.validateString(sortBy).contains("|")){
							String[] customSort=sortBy.split("\\|");
							if(CommonUtility.validateString(customSort[0]).length()>0){
								sortField = customSort[0];
							}
							if(CommonUtility.validateString(customSort[1]).equalsIgnoreCase("asc")){
								sortOrder = SolrQuery.ORDER.asc;
							}else if(CommonUtility.validateString(customSort[1]).equalsIgnoreCase("desc")){
								sortOrder = SolrQuery.ORDER.desc;
							}
						}
					}

				}
				
			
						query = new SolrQuery();
						if(CommonUtility.validateString(requestType).equalsIgnoreCase("NAVIGATION")){
							if(isCategoryNav)
								query.setQuery("catSearchId:"+treeId);
							else
								query.setQuery("categoryID:"+treeId);
						}else if(CommonUtility.validateString(requestType).equalsIgnoreCase("SHOP_BY_BRAND")){
							query.setQuery("brandID:"+brandId);
						}else if(CommonUtility.validateString(requestType).equalsIgnoreCase("SHOP_BY_MANF")){
							query.setQuery("manfId:"+brandId);
						}
						

						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_VERSION")).equalsIgnoreCase("10")){
							query.setRequestHandler("/mainitem_keywordsearch");
						}
						

						query.setStart(fromRow);
						query.setRows(resultPerPage);
						query.setFilterQueries(fq);
						if(treeId==0){
							query.addFilterQuery("defaultCategory:\"Y\"");						
						}

						if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y") && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SOLR_VERSION")).equalsIgnoreCase("10")){
							query.addFilterQuery("defaultProductItem:Y");
						}
						query.addSortField("goldenitem", SolrQuery.ORDER.desc );
						query.addSortField(sortField, sortOrder);
						
						
						System.out.println("Navigation query : " + query);

						QueryResponse response = server.query(query);
						SolrDocumentList documents = response.getResults();
						resultCount = (int) response.getResults().getNumFound();
	
						Iterator<SolrDocument> itr = documents.iterator();

						
		
						itemLevelFilterData = new ArrayList<ProductsModel>();
						System.out.println("DOCUMENTS");
						itemLevelFilterData = new ArrayList<ProductsModel>();
						while (itr.hasNext()) {
							try{
								ProductsModel itemModel = new ProductsModel();
								SolrDocument doc = itr.next();
								idList = idList + c + doc.getFieldValue("itemid").toString();
								if(CommonDBQuery.getSystemParamtersList().get("GET_AUTOSUG_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_AUTOSUG_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
									Map<String, Object> customFieldValMap = doc.getFieldValueMap();
									LinkedHashMap<String, Object> customFieldVal = getAllCustomFieldVal(customFieldValMap);
									if(customFieldVal!=null && customFieldVal.size()>0){
										itemModel.setCustomFieldVal(customFieldVal);
									}
								}
								itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
								itemModel.setMinOrderQty(1);
								itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
								itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
								itemModel.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
								itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
								itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
								itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
								itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
								itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
								itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
								itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
								itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
								itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
								itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
								itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
								itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
								if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB").trim().equalsIgnoreCase("Y")){
									if(doc.getFieldValue("catSearchId")!=null && doc.getFieldValue("categoryNamePath")!=null){
										itemModel.setItemBreadCrumb(ProductHunterSolrUltimate.getItemBreadCrumb((List<Integer>) doc.getFieldValue("catSearchId"),(String) doc.getFieldValue("categoryNamePath")));
									}
								}
								if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y")){
									if(doc.getFieldValue("productItemCount")!=null){
										itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
									}
								}
								itemModel.setResultCount(resultCount);
								String imageName = null;
								String imageType = null;
								String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
								if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
									itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
								}
								//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
								itemUrl = itemUrl.replaceAll(pattern," ");
								itemUrl = itemUrl.replaceAll("\\s+","-");
								itemModel.setItemUrl(itemUrl);
								if(doc.getFieldValue("imageName")!=null){
									imageName = doc.getFieldValue("imageName").toString();
								}
								if(doc.getFieldValue("imageType")!=null){
									imageType = doc.getFieldValue("imageType").toString();
								}
								if(doc.getFieldValue("upc")!=null){
									itemModel.setUpc(doc.getFieldValue("upc").toString());	
								}
								if(doc.getFieldValue("custom_Catalog_ID")!=null){
									itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());
								}
								if(imageName==null){
									imageName = "NoImage.png";
									imageType = "IMAGE";
								}
								itemModel.setImageName(imageName.trim());
								itemModel.setImageType(imageType);
								c = " OR ";
								itemLevelFilterData.add(itemModel);
							}catch (Exception e) {
								System.out.println("Error Occured While retriving data from solr.");
								e.printStackTrace();
							}	

						}
				if(itemLevelFilterData.size()>0)
				{
					
					server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
					server1.setParser(new XMLResponseParser());
					QueryRequest req1 = new QueryRequest();
					req1.setMethod(METHOD.POST);
					SolrQuery query1 = new SolrQuery();
					if(generalSubset>0 && generalSubset!=subsetId) {
						 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
						 if(!siteNameSolr.equalsIgnoreCase(""))
						 {
							 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
						 }
					}


					query1.setQuery("itemid:("+idList+")");
					String fq2 = "type:"+indexType;
					query1.setFilterQueries(fq2);
					query1.setStart(0);
					query1.setRows(itemLevelFilterData.size());

					QueryResponse response1 = server1.query(query1);

					SolrDocumentList documents1 = response1.getResults();
					resultCount = (int) response1.getResults().getNumFound();

				Iterator<SolrDocument> itr1 = documents1.iterator();
					NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
					ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
					while (itr1.hasNext()) {

						SolrDocument doc = itr1.next();
						for(ProductsModel iList : itemLevelFilterData)
						{
							int itemId = CommonUtility.validateNumber(doc.getFieldValue("itemid").toString());
							if(itemId==iList.getItemId())
							{
								ProductsModel ProductsModel = new ProductsModel();
								ProductsModel.setErpPartNumber(iList.getPartNumber());
								partIdentifiersList.add(ProductsModel);
								int minOrdQty = 1;
								int orderQtyInterval = 1;
								int saleQty = 1;
								
								int packageQty = 1;
								if(iList.getPackageQty()>0){
									packageQty = iList.getPackageQty();
								}
								
								
								int packageFlag = 0;
								String mOrdQty = null;
								String iPrice ="";
								float imapPrice = 0;
								String imap = null;
								if(doc.getFieldValue("price")!=null)
								{
									iPrice = doc.getFieldValue("price").toString();
								}


								double itemPrice = 0d;
								if(iPrice!=null && !iPrice.trim().equalsIgnoreCase(""))
								{
									itemPrice = Double.parseDouble(iPrice);
								}

								iList.setPrice(itemPrice);
								if(doc.getFieldValue("uom")!=null)
									iList.setUom(doc.getFieldValue("uom").toString());
								iList.setItemPriceId(CommonUtility.validateNumber(doc.getFieldValue("itemPriceId").toString()));

								
								if(doc.getFieldValue("overRidePriceRule")!=null){
									iList.setOverRidePriceRule((String)doc.getFieldValue("overRidePriceRule"));
								}
								if(doc.getFieldValue("materialGroup")!=null)
									iList.setMaterialGroup(doc.getFieldValue("materialGroup").toString());

								if(doc.getFieldValue("minordqty")!=null)
								{
									if(!doc.getFieldValue("minordqty").toString().trim().equalsIgnoreCase(""))
										minOrdQty = CommonUtility.validateNumber(doc.getFieldValue("minordqty").toString());
									if(minOrdQty==0)
										minOrdQty = 1;
								}
								if(doc.getFieldValue("orderQtyInterval")!=null)
								{
									if(!doc.getFieldValue("orderQtyInterval").toString().trim().equalsIgnoreCase(""))
										orderQtyInterval = CommonUtility.validateNumber(doc.getFieldValue("orderQtyInterval").toString());
									if(orderQtyInterval==0)
										orderQtyInterval = 1;
								}
								if(doc.getFieldValue("salesQty")!=null)
								{
									if(!doc.getFieldValue("salesQty").toString().trim().equalsIgnoreCase(""))
										saleQty = CommonUtility.validateNumber(doc.getFieldValue("salesQty").toString());

									if(saleQty==0)
										saleQty = 1;
								}

								if(doc.getFieldValue("packageFlag")!=null)
								{
									if(!doc.getFieldValue("packageFlag").toString().trim().equalsIgnoreCase(""))
										packageFlag = CommonUtility.validateNumber(doc.getFieldValue("packageFlag").toString());
								}

								if(doc.getFieldValue("packageQty")!=null)
								{
									if(!doc.getFieldValue("packageQty").toString().trim().equalsIgnoreCase(""))
									{
										packageQty = CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString());
									}

								}

								if(doc.getFieldValue("packDesc")!=null)
								{
									String packDesc = doc.getFieldValue("packDesc").toString();
									iList.setPackDesc(packDesc);
								}

								if(doc.getFieldValue("displayPricing") != null){

									iList.setDisplayPricing(doc.getFieldValue("displayPricing").toString());
								}
								if(doc.getFieldValue("clearanceFlag") != null){
									iList.setClearance(doc.getFieldValue("clearanceFlag").toString());
								}
								if(doc.getFieldValue("imap")!=null){
									imap = (doc.getFieldValue("imap").toString());
								}
								if(CommonUtility.validateString(imap).length()>0 && imap.toUpperCase().contains("Y") && doc.getFieldValue("imapPrice")!=null){
									imapPrice = (Float) (doc.getFieldValue("imapPrice"));
								}

								iList.setPackageFlag(packageFlag);
								iList.setPackageQty(packageQty);
								iList.setSaleQty(saleQty);
								iList.setMinOrderQty(minOrdQty);
								iList.setOrderInterval(orderQtyInterval);//orderQtyInterval

								iList.setImapPrice(imapPrice);
								iList.setIsImap(imap);

							}
						}
					}
					searchResultList.put("itemList", itemLevelFilterData);
				}
			}catch (Exception e) {
				
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server);
				ConnectionManager.closeSolrClientConnection(server1);
			}
			return searchResultList;
		}
		
		
		public static String getItemPriceIdByItemId(int itemId,int subsetId,int generalSubset){
		    String itemPriceId = "";
		    HttpSolrServer server1 = null;
		    try{
		          String indexType = "PH_SEARCH_"+subsetId;
		          

		          server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
		          server1.setParser(new XMLResponseParser());
		          QueryRequest req1 = new QueryRequest();
		          req1.setMethod(METHOD.POST);
		          SolrQuery query1 = new SolrQuery();
		          if(generalSubset>0 && generalSubset!=subsetId) {
						 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
						 if(!siteNameSolr.equalsIgnoreCase(""))
						 {
							 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
						 }
					}

		          query1.setQuery("itemid:"+itemId);   
		          String fq2 = "type:"+indexType;
		          query1.setFilterQueries(fq2);
		          query1.setStart(0);
		          query1.setRows(1);
		          
		          QueryResponse response1 = server1.query(query1);
		          SolrDocumentList documents1 = response1.getResults();
		          Iterator<SolrDocument> itr1 = documents1.iterator();
		          NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
		          //ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
		          while (itr1.hasNext()) {
		                SolrDocument doc = itr1.next();
		                itemPriceId = doc.getFieldValue("itemPriceId").toString();    
		          }
		    }catch (Exception e) {
		          e.printStackTrace();
		    }finally {
				ConnectionManager.closeSolrClientConnection(server1);
			}
		    return itemPriceId;
		}
		
		
		
		public static HashMap<String,String> getItemPriceIdByItemIdForSupercedItem(int itemId,int subsetId,int generalSubset){
		    String itemPriceId = "";
		    String supercededUrl = "";
		    HashMap<String,String> itemDetail = new HashMap<String, String>();
		    HttpSolrServer server1 = null;
		    try{
		          String indexType = "PH_SEARCH_"+subsetId;
		          if(generalSubset>0 && generalSubset!=subsetId) {
		        	  indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
		          }
		          server1 = ConnectionManager.getSolrClientConnection(solrURL+"/itempricedata");
		          server1.setParser(new XMLResponseParser());
		          QueryRequest req1 = new QueryRequest();
		          req1.setMethod(METHOD.POST);
		          SolrQuery query1 = new SolrQuery();
		          if(generalSubset>0 && generalSubset!=subsetId) {
						 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex=itempricedata}type:PH_SEARCH_"+subsetId +")";	
						 if(!siteNameSolr.equalsIgnoreCase(""))
						 {
							 indexType = "PH_SEARCH_"+subsetId+" OR "+"(type:PH_SEARCH_"+generalSubset+" -itemid:{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:PH_SEARCH_"+subsetId +")";
						 }
					}

		          query1.setQuery("itemid:"+itemId);
		          String fq2 = "type:"+indexType;
		          query1.setFilterQueries(fq2);
		          query1.setStart(0);
		          query1.setRows(1);
		          
		          QueryResponse response1 = server1.query(query1);
		          
		          SolrDocumentList documents1 = response1.getResults();
		          
		          Iterator<SolrDocument> itr1 = documents1.iterator();
		          NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
		          //ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
		          while (itr1.hasNext()) {
		                SolrDocument doc = itr1.next();
		                itemPriceId = doc.getFieldValue("itemPriceId").toString();
		                if(doc.getFieldValue("seourl")!= null) {
		                	  supercededUrl = doc.getFieldValue("seourl").toString();  
		                }
		          }
		          itemDetail.put("itemPriceId", itemPriceId);
		          itemDetail.put("supercededUrl", supercededUrl);
		    }catch (Exception e) {
		          e.printStackTrace();
		    }finally {
				ConnectionManager.closeSolrClientConnection(server1);
			}
		    return itemDetail;
		}
		
		public static ProductsModel findItemByPartNumber(String partNumber,int userSubsetId, int generalSubsetId, int buyingCompanyId) {
			String filterType = "PH_SEARCH_" + userSubsetId;
			if(generalSubsetId > 0  && generalSubsetId!=userSubsetId) {
				filterType += "_" + generalSubsetId;
	        }
            String fqNav = "{!join from=itemid to=itemid fromIndex=itempricedata}type:"+filterType;
            if(!siteNameSolr.equalsIgnoreCase(""))
            {
            	fqNav = "{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:"+filterType;
            }
            String cFq = "buyingCompanyId:"+buyingCompanyId;
            String sFq = "defaultCategory:Y";
            String attrFtr[] = {sFq, fqNav};
            String attrFtr1[] = {fqNav, cFq};
            String queryString  = "customerPartNumberKeyword:(" + partNumber + " OR "+ partNumber + "*)";
            String connUrl = solrURL+"/customerdata";
			String connUrlSub = solrURL+"/mainitemdata";
			ProductsModel product = null;
			HttpSolrServer solrServer = new HttpSolrServer(solrURL+"/mainitemdata");
			solrServer.setParser(new XMLResponseParser());
			SolrQuery query = new SolrQuery("partnumbersearch:"+ partNumber);
			query.setRequestHandler("/mainitem_keywordsearch");
			QueryResponse queryResponse;
			try {
                ProductsModel customerPartResult = ProductHunterSolrUltimate.solrSearchResultCustomerPartNumber(null, queryString, connUrl, attrFtr, attrFtr1, 0, 12,connUrlSub,CommonDBQuery.getDefaultFacet().toArray(new String[0]));
                if(customerPartResult != null && customerPartResult.getItemDataList() != null && customerPartResult.getItemDataList().size() == 1) {
                	product = customerPartResult.getItemDataList().get(0);
                }else {
                	queryResponse = solrServer.query(query);
    				SolrDocumentList results = queryResponse.getResults();
    				
    				Optional<SolrDocument> isFound = results.parallelStream().filter(
    						r -> {
    							return (r.getFieldValue("partnumber") != null && r.getFieldValue("partnumber").toString().equalsIgnoreCase(partNumber)) || (r.getFieldValue("manfpartnumber") != null && r.getFieldValue("manfpartnumber").toString().equalsIgnoreCase(partNumber));
    						}).findAny();
    				
    				if(isFound.isPresent()) {
    					product = extractProduct(isFound.get());
    					if(product != null) {
    						HashMap<String,String> itemDetail = getItemPriceIdByItemIdForSupercedItem(product.getItemId(), userSubsetId, generalSubsetId);
    						String itemId = "";
							String supercededUrl = "";
    						for (Map.Entry<String, String> entry : itemDetail.entrySet()) {
    							if(entry.getKey().equals("itemPriceId")) {
    								itemId = entry.getValue();
        						}
    							if(entry.getKey().equals("supercededUrl")) {
    								supercededUrl = entry.getValue();
        						}
    						}
    						if(itemId == null || itemId.trim().length()<=0) {
    							product = null;
    						}else {
    							supercededUrl = supercededUrl.trim();
    							product.setSupercedeurl(supercededUrl);
    						}
    						
    					}
    				}
                }
			} catch (SolrServerException e) {
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(solrServer);
			}
			return product;
		}
		private static ProductsModel extractProduct(SolrDocument solrDoc) {
			ProductsModel product = new ProductsModel();
			product.setItemId(Integer.parseInt(solrDoc.getFieldValue("itemid").toString()));
			product.setPartNumber(solrDoc.getFieldValue("partnumber").toString());
			product.setManufacturerId(Integer.parseInt(solrDoc.getFieldValue("manfId").toString()));
			product.setManufacturerName(solrDoc.getFieldValue("manufacturerName").toString());
			product.setManufacturerPartNumber(solrDoc.getFieldValue("manfpartnumber").toString());
			product.setMinOrderQty(Integer.parseInt(solrDoc.getFieldValue("minordqty") != null ? solrDoc.getFieldValue("minordqty").toString() : "1"));
			product.setOrderInterval(Integer.parseInt(solrDoc.getFieldValue("orderQtyInterval") != null ? solrDoc.getFieldValue("orderQtyInterval").toString() : "1"));
			product.setShortDesc(solrDoc.getFieldValue("description").toString());
			product.setUpc((solrDoc.getFieldValue("upc") != null) ? solrDoc.getFieldValue("upc").toString() : "");
			product.setImageName((solrDoc.getFieldValue("imageName") != null) ? solrDoc.getFieldValue("imageName").toString() : "");
			product.setImageType((solrDoc.getFieldValue("imageType") != null) ? solrDoc.getFieldValue("imageType").toString() : "");
			product.setUom((solrDoc.getFieldValue("uom") != null) ? solrDoc.getFieldValue("uom").toString() : "");
			return product;
		}
		
		public static ProductsModel getFeatureProductByCategory(int categoryId,int subsetId,int generalSubset){
		    String itemPriceId = "";
		    ProductsModel itemList = new ProductsModel();
		    try{
		          String indexType = "PH_SEARCH_"+subsetId;
		          if(generalSubset>0 && generalSubset!=subsetId) {
		        	  indexType = "(PH_SEARCH_"+subsetId+" OR "+"PH_SEARCH_"+generalSubset+")";
		          }
		          StringBuilder fqBuilder = new StringBuilder();
		          if(!siteNameSolr.equalsIgnoreCase(""))
		          {
		        	  fqBuilder.append("{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}type:").append(indexType).append("~catSearchId:").append(categoryId).append("~{!join from=itemid to=itemid fromIndex="+siteNameSolr+"_itempricedata method=crossCollection}isFeatureProduct:Y");
		          }else {
		        	  fqBuilder.append("{!join from=itemid to=itemid fromIndex=itempricedata}type:").append(indexType).append("~catSearchId:").append(categoryId).append("~{!join from=itemid to=itemid fromIndex=itempricedata}isFeatureProduct:Y");
		          }
		          String fq = fqBuilder.toString();
		         SolrQuery query = new SolrQuery();
		         query.setQuery("*:*");
		         query.setFilterQueries(fq.split("~"));
				QueryResponse response = SolrService.getInstance().executeSolrQuery(solrURL+"/mainitemdata", query);
				int resultCount = (int) response.getResults().getNumFound();
				SolrDocumentList documents = response.getResults();
				Iterator<SolrDocument> itr = documents.iterator();
				ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
				String pattern = "[^A-Za-z0-9]";
				
				while (itr.hasNext()) {
					try{
						ProductsModel itemModel = new ProductsModel();
						SolrDocument doc = itr.next();
						Map<String, Object> customFieldValMap = doc.getFieldValueMap();
						if(doc.getFieldValue("orderQtyIntervalByShipMethod")!=null && doc.getFieldValue("orderQtyIntervalByShipMethod").toString().trim().length()>0){
							LinkedHashMap<String, ProductsModel>shipOrderQtyAndInterval =  getOrderQtyIntervalByShipMethod(doc.getFieldValue("orderQtyIntervalByShipMethod").toString());
							itemModel.setShipOrderQtyAndIntervalFieldVal(shipOrderQtyAndInterval);
						}
						LinkedHashMap<String, Object> customFieldVal = getAllCustomFieldVal(customFieldValMap);
						if(customFieldVal!=null && customFieldVal.size()>0){
							itemModel.setCustomFieldVal(customFieldVal);
						}
						itemModel.setItemId(CommonUtility.validateNumber(doc.getFieldValue("itemid").toString()));
						itemModel.setMinOrderQty(1);
						itemModel.setPartNumber(doc.getFieldValue("partnumber").toString());
						itemModel.setAltPartNumber1((doc.getFieldValue("alternatePartNumber1")!=null?doc.getFieldValue("alternatePartNumber1").toString():""));
						itemModel.setAltPartNumber2((doc.getFieldValue("alternatePartNumber2")!=null?doc.getFieldValue("alternatePartNumber2").toString():""));
						itemModel.setBrandName((doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
						itemModel.setManufacturerName((doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():""));
						itemModel.setManufacturerId(CommonUtility.validateNumber(getSolrFieldValue(doc,"manfId")));
						itemModel.setManufacturerPartNumber((doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
						itemModel.setBrandId(CommonUtility.validateNumber((doc.getFieldValue("brandID")!=null?doc.getFieldValue("brandID").toString():"")));
						itemModel.setShortDesc((doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
						itemModel.setLongDesc((doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
						itemModel.setSalesUom((doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
						itemModel.setAvailQty((doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
						itemModel.setPackageQty((doc.getFieldValue("packageQty")!=null?CommonUtility.validateNumber(doc.getFieldValue("packageQty").toString()):0));
						itemModel.setWeight((doc.getFieldValue("weight")!=null?CommonUtility.validateDoubleNumber(doc.getFieldValue("weight").toString()):0));
						itemModel.setPageTitle((doc.getFieldValue("pageTitle")!=null?doc.getFieldValue("pageTitle").toString():""));
						itemModel.setClearance((doc.getFieldValue("clearanceFlag")!=null?doc.getFieldValue("clearanceFlag").toString():""));
						if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_LEVEL_BREADCRUMB").trim().equalsIgnoreCase("Y")){
							if(doc.getFieldValue("catSearchId")!=null && doc.getFieldValue("categoryNamePath")!=null){
								itemModel.setItemBreadCrumb(ProductHunterSolrUltimate.getItemBreadCrumb((List<Integer>) doc.getFieldValue("catSearchId"),(String) doc.getFieldValue("categoryNamePath")));
							}
						}
						if(CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y")){
							if(doc.getFieldValue("productItemCount")!=null){
								itemModel.setProductItemCount(CommonUtility.validateNumber(doc.getFieldValue("productItemCount").toString()));
							}
							if(doc.getFieldValue("productId")!=null){
								itemModel.setProductId(CommonUtility.validateNumber(doc.getFieldValue("productId").toString()));
							}
						}
						itemModel.setCategoryCode((doc.getFieldValue("categoryID")!=null?doc.getFieldValue("categoryID").toString():""));
						itemModel.setProductId((doc.getFieldValue("productId")!=null?CommonUtility.validateNumber(doc.getFieldValue("productId").toString()):0));
						itemModel.setProductName((doc.getFieldValue("productName")!=null?doc.getFieldValue("productName").toString():""));
						itemModel.setProductDescription((doc.getFieldValue("productDesc")!=null?doc.getFieldValue("productDesc").toString():""));
						itemModel.setProductCategoryImageName((doc.getFieldValue("productImageName")!=null?doc.getFieldValue("productImageName").toString():""));
						itemModel.setProductCategoryImageType((doc.getFieldValue("productImageType")!=null?doc.getFieldValue("productImageType").toString():""));
						itemModel.setResultCount(resultCount);
						String imageName = null;
						String imageType = null;
						String itemUrl = (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
						if(CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE")!=null && CommonDBQuery.getSystemParamtersList().get("MANUFACTURER_NAME_FOR_ITEM_TITLE").trim().equalsIgnoreCase("Y")){
							itemUrl = (doc.getFieldValue("manufacturerName")!=null?doc.getFieldValue("manufacturerName").toString():"")+" "+(doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():"");
						}
						//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
						itemUrl = itemUrl.replaceAll(pattern," ");
						itemUrl = itemUrl.replaceAll("\\s+","-");
						itemModel.setItemUrl(itemUrl);
						if(doc.getFieldValue("imageName")!=null){
							imageName = doc.getFieldValue("imageName").toString();
						}
						if(doc.getFieldValue("imageType")!=null){
							imageType = doc.getFieldValue("imageType").toString();
						}
						if(doc.getFieldValue("upc")!=null){
							itemModel.setUpc(doc.getFieldValue("upc").toString());	
						}
						if(doc.getFieldValue("custom_Catalog_ID")!=null){
							itemModel.setCatalogId(doc.getFieldValue("custom_Catalog_ID").toString());
						}
						if(imageName==null){
							imageName = "NoImage.png";
							imageType = "IMAGE";
						}
						itemModel.setImageName(imageName.trim());
						itemModel.setImageType(imageType);
					
						itemLevelFilterData.add(itemModel);
					}catch (Exception e) {
						System.out.println("Error Occured While retriving data from solr.");
						e.printStackTrace();
					}	

				}
				itemList.setItemDataList(itemLevelFilterData);
		    }catch (Exception e) {
		          e.printStackTrace();
		    }
		    return itemList;
		}
		
		public static ArrayList<ProductsModel> getStaticPageDetailsById (int staticPageId){

			HttpSolrServer server = null;
			Gson gson = new Gson();
			ArrayList<ProductsModel> staticContentResult = new ArrayList<ProductsModel>();
			try{
					server = ConnectionManager.getSolrClientConnection(solrURL+"/staticcontent");
					server.setParser(new XMLResponseParser());
					QueryRequest req = new QueryRequest();
					req.setMethod(METHOD.POST);
					SolrQuery query = new SolrQuery();
					query.setQuery("id:("+staticPageId+")");
					query.setStart(0);
					query.setRows(1);
					QueryResponse response = server.query(query);
					SolrDocumentList documents = response.getResults();
					int resultCount = (int) response.getResults().getNumFound();
					query.setRows(resultCount);
					response = server.query(query,METHOD.POST);
					Iterator<SolrDocument> itr = documents.iterator();
						staticContentResult = new ArrayList<ProductsModel>();
						while (itr.hasNext()) {
							ProductsModel pageModel = new ProductsModel();
							SolrDocument doc = itr.next();
							String id = doc.getFieldValue("id").toString();
							pageModel.setStaticPageId(id);
							if(doc.getFieldValue("pageTitle")!=null)
							{
								String pattern = "[^A-Za-z0-9]";
								String keyWord = doc.getFieldValue("pageTitle").toString();
								String scrubbedKeyword = keyWord.replaceAll(pattern,"-");
								pageModel.setStaticPageTitle(doc.getFieldValue("pageTitle").toString());
								pageModel.setItemUrl(scrubbedKeyword);
							}
							if(doc.getFieldValue("pageName")!=null){
								pageModel.setPageName(doc.getFieldValue("pageName").toString());
							}
							if(doc.getFieldValue("keywords")!=null)
							{
								pageModel.setStaticContent(doc.getFieldValue("keywords").toString());
							}
							String pageBreadCrumb = ProductHunterSolr.getSolrFieldValue(doc,"breadCrumb");
							if(CommonUtility.validateString(pageBreadCrumb).length()>0) {
								ArrayList<ProductsModel> staticpageBreadCrumb = gson.fromJson(CommonUtility.validateString(pageBreadCrumb),new TypeToken<ArrayList<ProductsModel>>() {}.getType());
								pageModel.setStaticpageBreadCrumb(staticpageBreadCrumb);
							}
							pageModel.setResultCount(resultCount);
							staticContentResult.add(pageModel);
						}

			}
			catch (Exception e) {
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server);
			}
			return staticContentResult;
		}
		
		public static LinkedHashMap<String, Object> brandsList(int subsetId, int generalSubsetId){
			
			LinkedHashMap<String, Object> brandData= new LinkedHashMap<String, Object>();
			ArrayList<String> manufacturerIndex = new ArrayList<String>();
			ArrayList<ProductsModel> manufacturerList = new ArrayList<ProductsModel>();
			Map<String, ArrayList<ProductsModel>> brandListAsMap = new TreeMap<String, ArrayList<ProductsModel>>();
			HttpSolrServer server = null;
			String clientIdx = "";
			
			try
			{
				server = ConnectionManager.getSolrClientConnection(solrURL+"/brandautocomplete");
				server.setParser(new XMLResponseParser());
				QueryRequest req = new QueryRequest();
				req.setMethod(METHOD.POST);
				SolrQuery query = new SolrQuery();
				
				if(generalSubsetId > 0  && generalSubsetId!=subsetId) {
					clientIdx = "PH_SEARCH_"+generalSubsetId+"_"+subsetId+"_MV";
				}else {
					clientIdx = "PH_SEARCH_"+subsetId+"_MV";
				}
				
				String queryString = "indexType:"+clientIdx;
				
				query.setQuery("*:*");
				query.set("group", "true");
				query.set("group.field", "brand_s");
				query.setFilterQueries(queryString);
				query.set("group.ngroups", "true");
				query.setRows(0);
				query.setFacet(true);
				query.addFacetField("brand_s");
				query.setFacetLimit(1);
				query.setFacetSort("count");
				query.setFacetMinCount(1);
			
				QueryResponse response = server.query(query);
				
				System.out.println("brand groups query : "+query);
				
				SolrQuery brandQuery = new SolrQuery();
				brandQuery.setQuery("*:*");
				brandQuery.set("group", "true");
				brandQuery.set("group.field", "brand_s");
				brandQuery.setFilterQueries(queryString);
				brandQuery.set("group.ngroups", "true");
				brandQuery.setFacet(true);
				brandQuery.addFacetField("brand_s");
				brandQuery.set("group.limit", (int) response.getFacetFields().get(0).getValues().get(0).getCount());
				brandQuery.setRows(response.getGroupResponse().getValues().get(0).getNGroups());
				
				QueryResponse brandResponse = server.query(brandQuery);
				
				System.out.println("brand list query : "+brandQuery);
				
				ArrayList<Group> brandSolrResponse = (ArrayList<Group>) brandResponse.getGroupResponse().getValues().get(0).getValues();
				
				for(int i=0; i < brandSolrResponse.size(); i++) {
					ArrayList<ProductsModel> brandsList = new ArrayList<ProductsModel>();
					manufacturerIndex.add(brandSolrResponse.get(i).getGroupValue());
					SolrDocumentList documents = brandSolrResponse.get(i).getResult();
					Iterator<SolrDocument> itr = documents.iterator();
					while (itr.hasNext()) {
						SolrDocument doc = itr.next();
						ProductsModel brandDetail = new ProductsModel();
						brandDetail.setBrandName(CommonUtility.validateString((String) doc.getFieldValue("displaylabel")));
						brandDetail.setBrandImage(CommonUtility.validateString((String) doc.getFieldValue("brandImage")));
						brandDetail.setBrandId(CommonUtility.validateNumber((String) doc.getFieldValue("codeId")));
						brandDetail.setItemUrl(CommonUtility.validateString((String) doc.getFieldValue("seourl")));
						manufacturerList.add(brandDetail);
						brandsList.add(brandDetail);
					}
					brandListAsMap.put(brandSolrResponse.get(i).getGroupValue(), brandsList);
				}
				
				brandData.put("brandList", brandListAsMap);
				brandData.put("mindex", manufacturerIndex);
				brandData.put("manufacturerList", manufacturerList);
			}
			catch (Exception e) {
				e.printStackTrace();
			}finally {
				ConnectionManager.closeSolrClientConnection(server);
			}
			return brandData;
		}
}
