package com.unilog.api.widget;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsAction;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.users.UsersAction;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;
import com.unilog.velocitytool.CIMM2VelocityTool;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class WidgetApi extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String renderContent;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int widgetId;
	Gson gson = new Gson();
	
	public String featuredBrand(){
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		try{
			ArrayList<ProductsModel> responseObject = CIMM2VelocityTool.getInstance().featuredBrands();
			LinkedHashMap<String, Object> brandList = new LinkedHashMap<String, Object>();
			brandList.put("brandList", responseObject);
			jsonObject.put("data", brandList);
			renderContent = gson.toJson(jsonObject);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String featuredManufacturer(){
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		try{
			ArrayList<ProductsModel> responseObject = CIMM2VelocityTool.getInstance().featuredManufacturers();
			LinkedHashMap<String, Object> manufacturerList = new LinkedHashMap<String, Object>();
			manufacturerList.put("manufacturerList", responseObject);
			jsonObject.put("data", manufacturerList);
			renderContent = gson.toJson(jsonObject);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String featuredProductGroupList(){
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			LinkedHashMap<String, ArrayList<ProductsModel>> responseObject = CIMM2VelocityTool.getInstance().getFeaturedProductGroupList(subsetId, generalSubset,session);
			LinkedHashMap<String, Object> itemList = new LinkedHashMap<String, Object>();
			itemList.put("itemMapList", responseObject);
			jsonObject.put("data", itemList);
			renderContent = gson.toJson(jsonObject);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String featuredProduct(){
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			ArrayList<ProductsModel> responseObject = CIMM2VelocityTool.getInstance().getFeaturedProduct(subsetId, generalSubset,session);
			LinkedHashMap<String, Object> itemList = new LinkedHashMap<String, Object>();
			itemList.put("itemList", responseObject);
			jsonObject.put("data", itemList);
			renderContent = gson.toJson(jsonObject);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String clearanceProduct(){
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			ArrayList<ProductsModel> responseObject = CIMM2VelocityTool.getInstance().getClearanceProduct(subsetId, generalSubset,session);
			LinkedHashMap<String, Object> itemList = new LinkedHashMap<String, Object>();
			itemList.put("itemList", responseObject);
			jsonObject.put("data", itemList);
			renderContent = gson.toJson(jsonObject);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String featuredCategories(){
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		try{
			ArrayList<ProductsModel> responseObject = CIMM2VelocityTool.getInstance().featuredCategories();
			LinkedHashMap<String, Object> categoryList = new LinkedHashMap<String, Object>();
			categoryList.put("categoryList", responseObject);
			jsonObject.put("data", categoryList);
			renderContent = gson.toJson(jsonObject);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String categories(){
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		try{
			request =ServletActionContext.getRequest();
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SUB_CATEGORY_LIST")).equalsIgnoreCase("Y"))
			{
				Map<String, Object>  categoryList = CIMM2VelocityTool.getInstance().allCategoryMenus();
				jsonObject.put("data", categoryList);
			}
			else {
				ArrayList<ProductsModel>  responseObject = CIMM2VelocityTool.getInstance().categoryMenus();
				LinkedHashMap<String, Object> categoryList = new LinkedHashMap<String, Object>();
				categoryList.put("categoryList", responseObject);
				jsonObject.put("data", categoryList);
			}	
			renderContent = gson.toJson(jsonObject);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String brands(){
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		try{
			request =ServletActionContext.getRequest();
			Map<String, Object>  responseObject = CIMM2VelocityTool.getInstance().brandsMenus();
			LinkedHashMap<String, Object> brandsList = new LinkedHashMap<String, Object>();
			brandsList.put("brandsList", responseObject);
			jsonObject.put("data", brandsList);
			renderContent = gson.toJson(jsonObject);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String events(){
		try{
			LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
			ArrayList<ProductsModel> eventList = ProductHunterSolr.getEventDetails();
			LinkedHashMap<String, Object> eventListObj = new LinkedHashMap<String, Object>();
			eventListObj.put("eventList", eventList);
			jsonObject.put("data", eventListObj);
			renderContent = gson.toJson(jsonObject);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String getProductGroupData() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		int savedGroupId=0;
		if(CommonUtility.validateString(request.getParameter("savedGroupName")).length()>0 ) {
			savedGroupId = ProductsDAO.getSavedGroupIDByName(request.getParameter("savedGroupName"));
		}
		if(savedGroupId>0) {
			LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
			LinkedHashMap<String, Object> ListObj = new LinkedHashMap<String, Object>();
			ListObj = ProductsDAO.getProductListDataDao(session,ListObj,savedGroupId, 0, null, null, request.getParameter("savedGroupName"),1,120,"","P");
			jsonObject.put("data", ListObj);
			renderContent = gson.toJson(jsonObject); 
		}
		return SUCCESS;
	}
	
	public String generateTemplate(){
		try{
			renderContent = WidgetUtility.getInstance().generateWidget(widgetId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getTopStaticMenu(){
	  LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
	  try{
	   ArrayList<MenuAndBannersModal> responseObject = MenuAndBannersDAO.topStaticMenu;
	   
	   LinkedHashMap<String, Object> manufacturerList = new LinkedHashMap<String, Object>();
	   manufacturerList.put("topStaticMenuList", responseObject);
	   jsonObject.put("data", manufacturerList);
	   renderContent = gson.toJson(jsonObject);
	  }catch (Exception e) {
	   e.printStackTrace();
	  }
	  return SUCCESS;
	 }
	
	public String locations() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			System.out.println("IP Address:- " + inetAddress.getHostAddress());
			JsonObject location = null;
			try {
				String url = CommonDBQuery.getSystemParamtersList().get("GEO_LOCATION_API_URL");
				System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
				CloseableHttpClient httpclient = HttpClients.custom().build();
				HttpPost httppost = new HttpPost(url);
				CloseableHttpResponse response = httpclient.execute(httppost);
				System.out.println("Response " + response);
				String jsonResponseStr = EntityUtils.toString(response.getEntity());
	
				JsonParser parser = new JsonParser();
				location = (JsonObject) parser.parse(jsonResponseStr);
				System.out.println("JSON Response " + location);
			}catch (Exception e) {
				e.printStackTrace();
			}
			WarehouseModel warehouseModel = new WarehouseModel();
			LinkedHashMap<String, Object> locationListObj = new LinkedHashMap<String, Object>();
			request = ServletActionContext.getRequest();
			String latitude = "0";
			String longitude =  "0";
			if(location != null) {
				latitude = location.get("geobyteslatitude").toString().replaceAll("^\"|\"$", "");
				longitude = location.get("geobyteslongitude").toString().replaceAll("^\"|\"$", "");
			}
			warehouseModel.setLatitude(latitude);
			warehouseModel.setLongitude(longitude);
			ArrayList<WarehouseModel> WarehouseList = new UsersAction().getWareHouseListAndDistanceToUsersLocation(warehouseModel);
			LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
			locationListObj.put("WarehouseList", WarehouseList);
			jsonObject.put("data", locationListObj);
			renderContent = gson.toJson(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String recentlyOrderedItems(){
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		try{
			ArrayList<ProductsModel> responseObject = CIMM2VelocityTool.getInstance().recentlyOrderedItems();
			LinkedHashMap<String, Object> recentlyOrderedList = new LinkedHashMap<String, Object>();
			recentlyOrderedList.put("recentlyOrderedList", responseObject);
			jsonObject.put("data", recentlyOrderedList);
			renderContent = gson.toJson(jsonObject);
		}catch (Exception e) {
			e.printStackTrace();
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
	public void setWidgetId(int widgetId) {
		this.widgetId = widgetId;
	}
	public int getWidgetId() {
		return widgetId;
	}
}
