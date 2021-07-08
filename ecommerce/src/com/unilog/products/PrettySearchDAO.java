package com.unilog.products;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;













import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

public class PrettySearchDAO {
	private static PrettySearchDAO prettySearchDAO=null;
	private boolean reInitializeClient = true;
	private final String USER_AGENT = "Mozilla/5.0";
	private final String CONTENT_TYPE = "Application/json";
	private String REQUEST_URL = null;
	
	public static PrettySearchDAO getInstance() {
		synchronized(PrettySearchDAO.class) {
			if(prettySearchDAO==null) {
				prettySearchDAO = new PrettySearchDAO();				
			}
		}
		return prettySearchDAO;
	}
	private void initializeClient() {
		reInitializeClient = false;
		REQUEST_URL = CommonDBQuery.getSystemParamtersList().get("AUTOCOMPLETE_URL_FOR_SEARCH_PAGE");
		
	}
	private HttpURLConnection  getConn(String keyWord){
		
		HttpURLConnection  conn = null;
		try {
			URL url =  new URL(REQUEST_URL+PrettySearchConstantVariables.AUTOCOMPLETE_URL_ACTION_NAME+"?q="+URLEncoder.encode(keyWord, "UTF-8"));
			conn  = (HttpURLConnection )url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-type", CONTENT_TYPE);
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestMethod("GET");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
	private ArrayList<ProductsModel> getItemDetail(ArrayList<ProductsModel> itemDetails,int subsetId,int generalSubset){
		String buildPartNumber = "";
		String delimit = "";
		try{
			for(ProductsModel ItemsModel : itemDetails){
				buildPartNumber = buildPartNumber+delimit+ItemsModel.getPartNumber();
				delimit = " OR ";
			}
			itemDetails = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubset, CommonUtility.validateString(buildPartNumber),0,null,"partnumber");
		}catch(Exception e){
			e.printStackTrace();
		}
		return itemDetails;
	}
	public LinkedHashMap<String, Object> getPrettySearchData(LinkedHashMap<String, Object> contentObject){
		if(reInitializeClient){
			initializeClient();
		}
		String keyWord = (String)contentObject.get(PrettySearchConstantVariables.keywordKey);
		int subsetId = (Integer) contentObject.get(PrettySearchConstantVariables.SUBSET_KEY);
		int generalSubset = (Integer) contentObject.get(PrettySearchConstantVariables.GENERAL_SUBSET_KEY);
		try {
			if(CommonUtility.validateString(keyWord).length()>0) {
				HttpURLConnection conn = null;
				if(getConn(keyWord)!=null) {
					conn = getConn(keyWord);
				}
				int responseCode = -1;
				if(conn!=null) {
					responseCode = conn.getResponseCode();
				}
				System.out.println("AutoComplete Model URL: " + conn.getURL() +"|Response Code: "+responseCode);
				if(responseCode == 200){
					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String inputLine;
					String outputTxt = "";
					while ((inputLine = in.readLine()) != null) {
						outputTxt = outputTxt + inputLine;
					}
					in.close();
					JSONObject jsonObject = new JSONObject(outputTxt);
				    JSONObject myResponse = jsonObject.getJSONObject("response");
				    JSONArray resultCount = (JSONArray) myResponse.get("ResultCount");
				    if(resultCount.length()>0){
				    	String catagory_brand = resultCount.getJSONObject(0).getString("CATEGORY_BRAND");
				    	String Illustrated_Diagram =  resultCount.getJSONObject(0).getString("Illustrated_Diagram");
				    	String Equipment =  resultCount.getJSONObject(0).getString("Equipment");
				    	String Parts =  resultCount.getJSONObject(0).getString("Parts");
				    	JSONArray result = (JSONArray) myResponse.get("docs");
				    	ArrayList<ProductsModel> catagoryBrand = new ArrayList<ProductsModel>();
				    	ArrayList<ProductsModel> illustratedDiagram = new ArrayList<ProductsModel>();
				    	ArrayList<ProductsModel> equipment = new ArrayList<ProductsModel>();
				    	ArrayList<ProductsModel> parts = new ArrayList<ProductsModel>();
				    	for(int i=0; i<result.length(); i++){
				    		ProductsModel eachLoopItem = new ProductsModel();
				    		if(result.getJSONObject(i).has("fayttype")){
				    			eachLoopItem = new ProductsModel();
				    			eachLoopItem.setCategoryName(result.getJSONObject(i).getString("category"));
				    			eachLoopItem.setItemTitleString(result.getJSONObject(i).getString("fayttext"));
				    			if(result.getJSONObject(i).has("taxonomyId")){
				    				eachLoopItem.setCategoryCode(result.getJSONObject(i).getString("taxonomyId"));	
				    			}
				    			eachLoopItem.setBrandId(CommonUtility.validateNumber(result.getJSONObject(i).getString("brandID")));
				    			eachLoopItem.setBrandName(result.getJSONObject(i).getString("brand"));
				    			catagoryBrand.add(eachLoopItem);
				    		}else if(result.getJSONObject(i).has("type")){
				    			String type =  result.getJSONObject(i).getString("type");
				    			if(CommonUtility.validateString(type).equalsIgnoreCase("Equipment")){
				    				eachLoopItem.setPartNumber(result.getJSONObject(i).getString("partnumber"));
				    				eachLoopItem.setItemTitleString(result.getJSONObject(i).getString("displaylabel"));
				    				eachLoopItem.setItemId(CommonUtility.validateNumber(result.getJSONObject(i).getString("itemid")));
				    				equipment.add(eachLoopItem);
				    			}else if(CommonUtility.validateString(type).equalsIgnoreCase("Illustrated Diagram")){
				    				eachLoopItem.setPartNumber(result.getJSONObject(i).getString("partnumber"));
				    				eachLoopItem.setBrandId(CommonUtility.validateNumber(result.getJSONObject(i).getString("brandID")));
				    				eachLoopItem.setBrandName(result.getJSONObject(i).getString("name"));
				    				eachLoopItem.setModelName(result.getJSONObject(i).getString("modelName"));
				    				eachLoopItem.setItemTitleString(result.getJSONObject(i).getString("displaylabel"));
				    				illustratedDiagram.add(eachLoopItem);	    			
				    			}else if(CommonUtility.validateString(type).equalsIgnoreCase("Parts")){
				    				eachLoopItem.setPartNumber(result.getJSONObject(i).getString("partnumber"));
				    				eachLoopItem.setItemTitleString(result.getJSONObject(i).getString("displaylabel"));
				    				eachLoopItem.setItemId(CommonUtility.validateNumber(result.getJSONObject(i).getString("itemid")));
				    				parts.add(eachLoopItem);	    			
				    			}
				    		}
				    		
				    	}
				    	equipment = getItemDetail(equipment,subsetId,generalSubset);
				    	parts = getItemDetail(parts,subsetId,generalSubset);
				    	contentObject.put("keyWordTxt",keyWord);
				    	contentObject.put("catagoryBrandCount",catagory_brand);
				    	contentObject.put("illustratedDiagramCount",Illustrated_Diagram);
				    	contentObject.put("equipmentCount",Equipment);
				    	contentObject.put("partsCount",Parts);
				    	contentObject.put("catagoryBrandData",catagoryBrand);
				    	contentObject.put("illustratedDiagramData",illustratedDiagram);
				    	contentObject.put("equipmentData",equipment);
				    	contentObject.put("partsData",parts);
				    }
				}
			}
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return contentObject;
		
	}
	
	
	
	public boolean isReInitializeClient() {
		return reInitializeClient;
	}
	public void setReInitializeClient(boolean reInitializeClient) {
		this.reInitializeClient = reInitializeClient;
	}
	public String getREQUEST_URL() {
		return REQUEST_URL;
	}
	public void setREQUEST_URL(String rEQUEST_URL) {
		REQUEST_URL = rEQUEST_URL;
	}
}
