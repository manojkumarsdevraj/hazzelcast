package com.unilog.datasmart;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.unilog.database.CommonDBQuery;
import com.unilog.datasmart.DatasmartConstantVariables.ARIServices;
import com.unilog.datasmart.model.DataSmartAssemblyInfoModel;
import com.unilog.datasmart.model.DataSmartHotSpotModel;
import com.unilog.datasmart.model.DataSmartModelAutoCompleteModel;
import com.unilog.datasmart.model.DataSmartNodeChildrenModel;
import com.unilog.datasmart.model.DataSmartPartAutoCompleteModel;
import com.unilog.datasmart.model.DataSmartPartInfoModel;
import com.unilog.datasmart.model.DataSmartSearchModelAssembliesModel;
import com.unilog.datasmart.model.DataSmartSearchPartModels;
import com.unilog.datasmart.model.DataSmartsSearchModel;
import com.unilog.datasmart.model.HotSpot;
import com.unilog.datasmart.model.NodeChildren;
import com.unilog.datasmart.model.Part;
import com.unilog.datasmart.model.PartQtyRuntime;
import com.unilog.datasmart.model.PartQtyTypeAdapter;
import com.unilog.datasmart.model.Results;
import com.unilog.datasmart.model.SearchModelAssemblyInfo;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;
public class DataSmartController {
	private static DataSmartController dataSmartController=null;
	private ConcurrentHashMap<String,String> ARIBrandCode = null;
	private ConcurrentHashMap<String,String> prefixBrandCode = null;
	private ConcurrentHashMap<String,String> prefixARIMappingData = null;
	private ConcurrentHashMap<String,String> BRAND_NAMEFromARI = null;
	private ConcurrentHashMap<String,String> brandIdandName = null;
	private List<CustomTable> customTableBrandData = null;
	private String SERVICE_URL;
	private final String USER_AGENT = "Mozilla/5.0";
	private final String CONTENT_TYPE = "Application/json";
	private boolean reInitializeClient = true;
	private DataSmartController() {
		ARIBrandCode = new ConcurrentHashMap<String,String>();
		prefixBrandCode = new ConcurrentHashMap<String, String>();
		prefixARIMappingData = new ConcurrentHashMap<String, String>();
		BRAND_NAMEFromARI = new ConcurrentHashMap<String, String>();
		customTableBrandData = new ArrayList<CustomTable>();
		brandIdandName  =new ConcurrentHashMap<String, String>();
		if(reInitializeClient){
			initializeClient();
		}
	}
	public static DataSmartController getInstance() {
		synchronized(DataSmartController.class) {
			if(dataSmartController==null) {
				dataSmartController = new DataSmartController();				
			}
		}
		return dataSmartController;
	}
	private void initializeClient() {
		reInitializeClient = false;
		SERVICE_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ARI_SERVICE_URL"));
		customTableBrandData = CIMM2VelocityTool.getInstance().getCusomTableDataByFieldValue(0,"BRAND",0,0,"ARI_BRAND_CONFIGURATION","IS_ARI_BRAND,WEBSITE","Y,"+CommonDBQuery.webSiteName,"BRAND_NAME,BRAND_ID","BRANDS");
		for(CustomTable eachBrand:customTableBrandData){
			ARIBrandCode.put(eachBrand.getEntityFieldValue().get("BRAND_NAME"), eachBrand.getTableDetails().get(0).get("ARI_BRAND_CODE"));
			prefixBrandCode.put(eachBrand.getEntityFieldValue().get("BRAND_NAME"), eachBrand.getTableDetails().get(0).get("SX_BRAND_PREFIX"));
			BRAND_NAMEFromARI.put(eachBrand.getTableDetails().get(0).get("ARI_BRAND_CODE"), eachBrand.getEntityFieldValue().get("BRAND_NAME"));
			prefixARIMappingData.put(eachBrand.getTableDetails().get(0).get("ARI_BRAND_CODE"), eachBrand.getTableDetails().get(0).get("SX_BRAND_PREFIX"));
			brandIdandName.put(eachBrand.getEntityFieldValue().get("BRAND_ID"), eachBrand.getEntityFieldValue().get("BRAND_NAME"));
		}

	}
	public String firstHitCheck(){
		if(reInitializeClient){
			initializeClient();
		}
		return ""; 
	}
	private String buildJsonParams(ARIServices serviceRequested,String[] urlParam,String BrandARICode){
		LinkedHashMap<String, Object> jsonObj = new LinkedHashMap<String, Object>();
		jsonObj.put(DatasmartConstantVariables.BRAND_CODE, BrandARICode);
		switch(serviceRequested){
		case assemblyImage:
			jsonObj.put(DatasmartConstantVariables.PARENT_ID, CommonUtility.validateNumber(urlParam[1]));
			jsonObj.put(DatasmartConstantVariables.ASSEMBLY_ID, CommonUtility.validateNumber(urlParam[2]));
			jsonObj.put(DatasmartConstantVariables.WIDTH, CommonUtility.validateNumber(urlParam[3]));
			break;
		case assemblyInfo:
			jsonObj.put(DatasmartConstantVariables.PARENT_ID, CommonUtility.validateNumber(urlParam[1]));
			jsonObj.put(DatasmartConstantVariables.ASSEMBLY_ID, CommonUtility.validateNumber(urlParam[2]));
			break;
		case assemblyInfoNoHotSpot:
			jsonObj.put(DatasmartConstantVariables.PARENT_ID, CommonUtility.validateNumber(urlParam[1]));
			jsonObj.put(DatasmartConstantVariables.ASSEMBLY_ID, CommonUtility.validateNumber(urlParam[2]));
			break;
		case hotSpots:
			jsonObj.put(DatasmartConstantVariables.ASSEMBLY_ID, CommonUtility.validateNumber(urlParam[1]));
			jsonObj.put(DatasmartConstantVariables.ZOOM_LEVEL, CommonUtility.validateNumber(urlParam[2]));
			break;
		case modelAutoComplete:
			jsonObj.put(DatasmartConstantVariables.MODEL_NAME, CommonUtility.validateString(urlParam[1]));
			jsonObj.put(DatasmartConstantVariables.NO_OF_RESULTS,(urlParam.length>2 && CommonUtility.validateNumber(urlParam[2])>0)?urlParam[2]:1);
			break;
		case nodeChildren:
			if(urlParam.length>1){
				jsonObj.put(DatasmartConstantVariables.PARENT_ID, CommonUtility.validateNumber(urlParam[1]));	
			}else{
				jsonObj.put(DatasmartConstantVariables.PARENT_ID, -1);
			}
			
			break;
		case partAutoComplete:
			jsonObj.put(DatasmartConstantVariables.PART_SKU, CommonUtility.validateString(urlParam[1]));
			jsonObj.put(DatasmartConstantVariables.NO_OF_RESULTS,(urlParam.length>2 && CommonUtility.validateNumber(urlParam[2])>0)?urlParam[2]:5);
			break;
		case partInfo:
			jsonObj.put(DatasmartConstantVariables.PART_ID, CommonUtility.validateNumber(urlParam[1]));
			break;
		case searchModel:
			jsonObj.put(DatasmartConstantVariables.MODEL_NAME, CommonUtility.validateString(urlParam[1]));
			if(urlParam.length>2){
				jsonObj.put(DatasmartConstantVariables.PAGE, (CommonUtility.validateNumber(urlParam[2])>0)?urlParam[2]:1);	
			}else{
				jsonObj.put(DatasmartConstantVariables.PAGE, 1);	
			}
			if(urlParam.length>3){
				jsonObj.put(DatasmartConstantVariables.PAGE_SIZE, (CommonUtility.validateNumber(urlParam[3])>0)?urlParam[3]:1);
			}else{
				jsonObj.put(DatasmartConstantVariables.PAGE_SIZE,1);
			}
			
			break;
		case searchModelAssemblies:
			jsonObj.put(DatasmartConstantVariables.MODEL_ID, CommonUtility.validateNumber(urlParam[1]));
			break;
		case searchModelAssembliesWithInfo:
			jsonObj.put(DatasmartConstantVariables.MODEL_ID, CommonUtility.validateNumber(urlParam[1]));
			break;
		case searchPartModels:
			jsonObj.put(DatasmartConstantVariables.PART_SKU, CommonUtility.validateString(strJoinForPartsSearch(urlParam,"-")));
			jsonObj.put(DatasmartConstantVariables.PAGE, (urlParam.length>2 && CommonUtility.validateNumber(urlParam[urlParam.length-2])>0)?urlParam[urlParam.length-2]:1);
			jsonObj.put(DatasmartConstantVariables.PAGE_SIZE,(urlParam.length>3 && CommonUtility.validateNumber(urlParam[urlParam.length-1])>0)?urlParam[urlParam.length-1]:1);
			break;
		case searchParts:
			jsonObj.put(DatasmartConstantVariables.SEARCH, CommonUtility.validateString(urlParam[1]));
			break;
		case searchPartsWithinModel:
			jsonObj.put(DatasmartConstantVariables.MODEL_SEARCH, CommonUtility.validateString(urlParam[1]));
			jsonObj.put(DatasmartConstantVariables.PART_SEARCH, CommonUtility.validateString(urlParam[2]));
			jsonObj.put(DatasmartConstantVariables.PAGE, (urlParam.length>3 && CommonUtility.validateNumber(urlParam[3])>0)?urlParam[3]:1);
			jsonObj.put(DatasmartConstantVariables.PAGE_SIZE, (urlParam.length>4 && CommonUtility.validateNumber(urlParam[4])>0)?urlParam[4]:1);
			break;
		default:
			break;
			
		}
		Gson gson = new Gson();
		String data1 = gson.toJson(jsonObj);
		System.out.println("Request Json:"+data1);
		return data1;
	}
	private HttpURLConnection getConn(LinkedHashMap<String, Object> contentObject){
		String[] urlParam = (String[]) contentObject.get(DatasmartConstantVariables.URL_PARAM);
		ARIServices serviceRequested =ARIServices.valueOf((String)contentObject.get(DatasmartConstantVariables.REQ_TYPE)); 
		String BRAND_ARI_CODE =(String)contentObject.get(DatasmartConstantVariables.BRAND_ARI_CODE);
		HttpURLConnection conn = null;
		try {
			URL url =  new URL(SERVICE_URL+serviceRequested);
			conn  = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Authorization",CommonDBQuery.getCimm2bCentralAuthorization());
			conn.setRequestProperty("Content-type", CONTENT_TYPE);
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestProperty("ClientId","1");
			conn.setRequestProperty("SiteId", ""+CommonDBQuery.getGlobalSiteId());
			conn.setRequestMethod("POST");
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(buildJsonParams(serviceRequested,urlParam,BRAND_ARI_CODE));
			wr.flush();
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
	public LinkedHashMap<String, Object> getARIData(LinkedHashMap<String, Object> contentObject){
		if(reInitializeClient){
			initializeClient();
		}
		try {
			if(contentObject!=null) {
				HttpURLConnection conn = null;
				if(getConn(contentObject)!=null) {
					conn = getConn(contentObject);
				}
				int responseCode = -1;
				if(conn!=null) {
					responseCode = conn.getResponseCode();
				}
				System.out.println("Sending 'POST' request to URL : " + conn.getURL() +"|Response Code: "+responseCode);
				if(responseCode == 200){
					String reqType = (String)contentObject.get(DatasmartConstantVariables.REQ_TYPE);
					if(reqType.equalsIgnoreCase("assemblyImage")){
						BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
						ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
						int c;
					    while ((c = in.read()) != -1) {
					    	byteArrayOut.write(c);
					    }
					    String encodedImage = Base64.encode(byteArrayOut.toByteArray());
						contentObject.put(DatasmartConstantVariables.IMAGE, encodedImage);
					}else{
						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String inputLine;
						String outputTxt = "";
						while ((inputLine = in.readLine()) != null) {
							outputTxt = outputTxt + inputLine;
						}
						in.close();
						GsonBuilder builder = new GsonBuilder();
						builder.registerTypeAdapter(PartQtyRuntime.class, new PartQtyTypeAdapter());
						Gson gson = builder.create();
						if(CommonUtility.validateString(reqType).equalsIgnoreCase("assemblyInfo")){
							DataSmartAssemblyInfoModel output = gson.fromJson(outputTxt, DataSmartAssemblyInfoModel.class);
							String delimit = "";
							String toCheck = "";
							LinkedHashMap<String, Part>hotspotParts = new LinkedHashMap<String, Part>();
							ArrayList<Integer> itemList = new ArrayList<Integer>();
							for(Part listParts:output.getData().getParts()){
								//String partNumber = (String)contentObject.get(DatasmartConstantVariables.BRAND_PREFIX_CODE)+listParts.getSku();
								String partNumber = "??"+ProductHunterSolr.escapeQueryCulpritsWithoutWhiteSpace(listParts.getSku());
								if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("SX")){
									partNumber = partNumber.replaceAll("\\ ", "-");
								}
								toCheck = toCheck+delimit+partNumber;
								delimit = " OR ";
								hotspotParts.put(CommonUtility.validateString(listParts.getTag()), listParts);
							}
							output.getData().setImageUrl(output.getData().getImageUrl().substring(0, output.getData().getImageUrl().length()-1) + "5");
							ArrayList<ProductsModel> checkedItems = ProductHunterSolr.getItemDetailsForGivenPartNumbers(CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.SUBSET)),  CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.GENERAL_SUBSET)), CommonUtility.validateString(toCheck),CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.BRAND_ID)),"Y","partnumber");
							ArrayList<ProductsModel> secondCheckItems = ProductHunterSolr.getItemDetailsForGivenPartNumbers(CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.SUBSET)),  CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.GENERAL_SUBSET)), CommonUtility.validateString(toCheck),CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.BRAND_ID)),"N","partnumber");
							LinkedHashMap<String, ProductsModel> mappedData = new LinkedHashMap<String, ProductsModel>();
							for(ProductsModel added:secondCheckItems){
								mappedData.put(added.getPartNumber().substring(2), added);
								itemList.add(added.getItemId());
							}
							for(ProductsModel added:checkedItems){
								mappedData.put(added.getPartNumber().substring(2), added);
								itemList.add(added.getItemId());
							}
							if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
								if(itemList!=null && itemList.size()>0){
									LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.SUBSET)),  CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.GENERAL_SUBSET)), StringUtils.join(itemList," OR "),"itemid");
									contentObject.put("customFieldVal", customFieldVal);
								}
							}
							String metaKeyword = "";
							metaKeyword = output.getData().getName();
							contentObject.put("metaKeyword",metaKeyword);
							String [] urlParmsForHotSpot = {(String) contentObject.get(DatasmartConstantVariables.BRAND_ID),""+output.getData().getAssemblyId(),"5"};
							LinkedList<HotSpot> UpdatedHotSpot = getHotSpotInfo("hotSpots",urlParmsForHotSpot,(String)contentObject.get(DatasmartConstantVariables.BRAND_ARI_CODE));
							output.getData().setHotSpots(UpdatedHotSpot);
							contentObject.put("hotspotParts", hotspotParts);
							contentObject.put("checkedItems",mappedData);
							contentObject.put("ObjectResponse",output);
							contentObject.put("pageTitle", (String)contentObject.get(DatasmartConstantVariables.BRAND_NAME)+" - "+output.getData().getParentName()+" - "+output.getData().getName());
						}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("nodeChildren")){
							DataSmartNodeChildrenModel output = gson.fromJson(outputTxt, DataSmartNodeChildrenModel.class);
							String delimiter = "";
							String metaKeyword = "";
							for(NodeChildren eachNode:output.getData()){
								metaKeyword = metaKeyword + delimiter +eachNode.getName();
								delimiter = ",";
							}
							contentObject.put("metaKeyword",metaKeyword);
							contentObject.put("ObjectResponse",output);
							contentObject.put("pageTitle", (String)contentObject.get(DatasmartConstantVariables.BRAND_NAME)+" - Parts Lookup");
						}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("searchModelAssemblies")){
							DataSmartSearchModelAssembliesModel output = gson.fromJson(outputTxt, DataSmartSearchModelAssembliesModel.class);
							String delimiter = "";
							String metaKeyword = "";
							for(SearchModelAssemblyInfo eachNode:output.getData()){
								metaKeyword = metaKeyword + delimiter +eachNode.getName();
								delimiter = ",";
							}
							contentObject.put("metaKeyword",metaKeyword);
							contentObject.put("ObjectResponse",output);
						}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("hotSpots")){
							HotSpot output = gson.fromJson(outputTxt, HotSpot.class);
							contentObject.put("ObjectResponse",output);
						}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("searchPartModels")){
							DataSmartSearchPartModels output = gson.fromJson(outputTxt, DataSmartSearchPartModels.class);
							String delimiter = "";
							String metaKeyword = "";
							for(Results eachNode:output.getData().getResults()){
								metaKeyword = metaKeyword + delimiter +eachNode.getModelName();
								delimiter = ",";
							}
							contentObject.put("metaKeyword",metaKeyword);
							contentObject.put("ObjectResponse",output);
						}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("partInfo")){
							DataSmartPartInfoModel output = gson.fromJson(outputTxt, DataSmartPartInfoModel.class);
							ArrayList<ProductsModel> checkedItems = new ArrayList<ProductsModel>();
							if(CommonUtility.validateString(output.getData().getSku()).length()>0){
								checkedItems = ProductHunterSolr.getItemDetailsForGivenPartNumbers(CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.SUBSET)),CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.GENERAL_SUBSET)), CommonUtility.validateString((String)contentObject.get(DatasmartConstantVariables.BRAND_PREFIX_CODE)+output.getData().getSku()),CommonUtility.validateNumber((String)contentObject.get(DatasmartConstantVariables.BRAND_ID)),"Y","partnumber");	
							}
							LinkedHashMap<String, ProductsModel> mappedData = new LinkedHashMap<String, ProductsModel>();
							for(ProductsModel added:checkedItems){
								mappedData.put(added.getPartNumber(), added);
							}
							contentObject.put("checkedItems",mappedData);
							contentObject.put("ObjectResponse",output);
						}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("modelAutoComplete")){
							DataSmartModelAutoCompleteModel output = gson.fromJson(outputTxt, DataSmartModelAutoCompleteModel.class);
							contentObject.put("ObjectResponse",output);
						}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("partAutoComplete")){
							DataSmartPartAutoCompleteModel output = gson.fromJson(outputTxt, DataSmartPartAutoCompleteModel.class);
							contentObject.put("ObjectResponse",output);
						}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("searchModel")){
							DataSmartsSearchModel output = gson.fromJson(outputTxt, DataSmartsSearchModel.class);
							String delimiter = "";
							String metaKeyword = "";
							for(Results eachNode:output.getData().getResults()){
								metaKeyword = metaKeyword + delimiter +eachNode.getModelName();
								delimiter = ",";
							}
							contentObject.put("metaKeyword",metaKeyword);
							contentObject.put("ObjectResponse",output);
						}
						contentObject.put("JsonResponse",outputTxt);
						System.out.println(reqType+" Response: "+outputTxt);
					}
					
				}else{
					contentObject.put("isError","Y");
					contentObject.put("ErrorMsg","Internal Server Error");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contentObject;

	}
	public LinkedList<HotSpot> getHotSpotInfo(String reqType,String[] urlParam,String BrandARICode){
		LinkedList<HotSpot> output = new LinkedList<HotSpot>();
		try {
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put(DatasmartConstantVariables.REQ_TYPE,reqType);
			contentObject.put(DatasmartConstantVariables.URL_PARAM,urlParam);
			contentObject.put(DatasmartConstantVariables.BRAND_ARI_CODE,BrandARICode);
			if(contentObject!=null) {
				HttpURLConnection conn = null;
				if(getConn(contentObject)!=null) {
					conn = getConn(contentObject);
				}
				int responseCode = -1;
				if(conn!=null) {
					responseCode = conn.getResponseCode();
				}
				System.out.println("Sending 'GET' request to URL : " + conn.getURL() +"|Response Code: "+responseCode);
				if(responseCode==200){
					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String inputLine;
					String outputTxt = "";
					while ((inputLine = in.readLine()) != null) {
						outputTxt = outputTxt + inputLine;
					}
					in.close();
					System.out.println("HotSpot Response:"+outputTxt);
					GsonBuilder builder = new GsonBuilder();
					builder.registerTypeAdapter(PartQtyRuntime.class, new PartQtyTypeAdapter());
					Gson gson = builder.create();
					DataSmartHotSpotModel outPut = gson.fromJson(outputTxt, DataSmartHotSpotModel.class);
					output = outPut.getData();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return output;
	}
	public LinkedHashMap<String, Object> getARIDataToPage(String BrandName,String reqType,String urlParam){
		if(reInitializeClient){
			initializeClient();
		}
		String[] urlParamObj;
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		contentObject.put(DatasmartConstantVariables.BRAND_ARI_CODE, ARIBrandCode.get(BrandName.replaceAll("-"," ")));
		contentObject.put(DatasmartConstantVariables.BRAND_NAME, BrandName);
		contentObject.put(DatasmartConstantVariables.BRAND_PREFIX_CODE, prefixBrandCode.get(BrandName.replaceAll("-"," ")));
		contentObject.put(DatasmartConstantVariables.REQ_TYPE,reqType);
		if(CommonUtility.validateString(urlParam).length()>0){
			urlParamObj= urlParam.split("\\|");	
			contentObject.put(DatasmartConstantVariables.URL_PARAM,urlParamObj);
		}
		try {
			
			if(contentObject!=null) {
				HttpURLConnection conn = null;
				if(getConn(contentObject)!=null) {
					conn = getConn(contentObject);
				}
				int responseCode = -1;
				if(conn!=null) {
					responseCode = conn.getResponseCode();
				}
				System.out.println("Sending 'GET' request to URL : " + conn.getURL());
				if(responseCode == 200){
					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String inputLine;
					String outputTxt = "";
					while ((inputLine = in.readLine()) != null) {
						outputTxt = outputTxt + inputLine;
					}
					in.close();
					GsonBuilder builder = new GsonBuilder();
					builder.registerTypeAdapter(PartQtyRuntime.class, new PartQtyTypeAdapter());
					Gson gson = builder.create();
					reqType = (String)contentObject.get(DatasmartConstantVariables.REQ_TYPE);
					if(CommonUtility.validateString(reqType).equalsIgnoreCase("assemblyInfo")){
						DataSmartAssemblyInfoModel output = gson.fromJson(outputTxt, DataSmartAssemblyInfoModel.class);
						contentObject.put("ObjectResponse",output);
					}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("nodeChildren")){
						DataSmartNodeChildrenModel output = gson.fromJson(outputTxt, DataSmartNodeChildrenModel.class);
						contentObject.put("ObjectResponse",output);
					}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("searchModelAssemblies")){
						DataSmartSearchModelAssembliesModel output = gson.fromJson(outputTxt, DataSmartSearchModelAssembliesModel.class);
						contentObject.put("ObjectResponse",output);
					}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("hotSpots")){
						HotSpot output = gson.fromJson(outputTxt, HotSpot.class);
						contentObject.put("ObjectResponse",output);
					}
					contentObject.put("JsonResponse",outputTxt);
					System.out.println(reqType+" Response: "+outputTxt);
				}else{
					contentObject.put("isError","Y");
					contentObject.put("ErrorMsg","Internal Server Error");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contentObject;

	}
	public String getARIBrandCode(String BRAND_NAME){
		if(reInitializeClient){
			initializeClient();
		}
		String brandCode  = "";
		if(CommonUtility.validateString(BRAND_NAME).length()>0){
			if(ARIBrandCode.size()>0){
				brandCode = ARIBrandCode.get(BRAND_NAME);
			}else{
				ARIBrandCode = ProductsDAO.getCustomFieldDetails("ARI_BRAND_CODE");
				brandCode = ARIBrandCode.get(BRAND_NAME);
			}
		}else{
			brandCode = "";
		}
		
		return brandCode;
	}
	public String getBrandPrefixFromARICode(String ariBrandCode){
		if(reInitializeClient){
			initializeClient();
		}
		String brandCode  = "";
		if(CommonUtility.validateString(ariBrandCode).length()>0){
			brandCode = prefixARIMappingData.get(ariBrandCode);
		}
		return brandCode;
	}
	public String getBrandNameFromARICode(String ariBrandCode){
		if(reInitializeClient){
			initializeClient();
		}
		String BRAND_NAME  = "";
		if(CommonUtility.validateString(ariBrandCode).length()>0){
			BRAND_NAME = BRAND_NAMEFromARI.get(ariBrandCode);
		}
		return BRAND_NAME;
	}
	public String getBrandPrefix(String BRAND_NAME){
		if(reInitializeClient){
			initializeClient();
		}
		String brandPrefix  = "";
		if(CommonUtility.validateString(BRAND_NAME).length()>0){
			if(prefixBrandCode.size()>0){
				brandPrefix = prefixBrandCode.get(BRAND_NAME);
			}else{
				prefixBrandCode = ProductsDAO.getCustomFieldDetails("BRAND_PREFIX");
				brandPrefix = prefixBrandCode.get(BRAND_NAME);
			}
		}else{
			brandPrefix = "";
		}
		return brandPrefix;
	}
	public LinkedHashMap<String, Object> getAllBrandsWithARI(LinkedHashMap<String, Object> contentObject){
		if(ARIBrandCode.size()>0){
			contentObject.put("ARIBrands",ARIBrandCode);
		}else{
			ARIBrandCode = ProductsDAO.getCustomFieldDetails("ARI_BRAND_CODE");
			contentObject.put("ARIBrands",ARIBrandCode);
		}
		
		return contentObject;
	}
	
	public String scrubTextForSEO(String scrubString){
		Pattern pt = Pattern.compile("[^a-zA-Z0-9 ]");
        Matcher match= pt.matcher(scrubString);
        while(match.find())
        {
            String s= match.group();
            scrubString=scrubString.replaceAll("\\"+s, "-");
        }
		scrubString = scrubString.replaceAll("--", "-");
		scrubString = scrubString.replaceAll("--", "-");
		scrubString = scrubString.toLowerCase();
		return scrubString;
	}
	public String strJoinForPartsSearch(String[] aArr, String sSep) {
	    StringBuilder sbStr = new StringBuilder();
	    for (int i = 1, il = aArr.length-2; i < il; i++) {
	        if (i > 1){
	        	sbStr.append(sSep);
	        }
	        sbStr.append(aArr[i]);
	    }
	    return sbStr.toString();
	}
	public void setReInitializeClient(boolean reInitializeClient) {
		this.reInitializeClient = reInitializeClient;
	}
	public boolean isReInitializeClient() {
		return reInitializeClient;
	}
	public List<CustomTable> getCustomTableBrandData() {
		return customTableBrandData;
	}
	public void setCustomTableBrandData(List<CustomTable> customTableBrandData) {
		this.customTableBrandData = customTableBrandData;
	}
	public ConcurrentHashMap<String, String> getBrandIdandName() {
		return brandIdandName;
	}
	public void setBrandIdandName(ConcurrentHashMap<String, String> brandIdandName) {
		this.brandIdandName = brandIdandName;
	}
	
}