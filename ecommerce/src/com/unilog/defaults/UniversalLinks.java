package com.unilog.defaults;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jdom.input.SAXBuilder;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.datasmart.DataSmartController;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.socialmedia.service.BlogFeed;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CIMMTouchUtility;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class UniversalLinks  extends ActionSupport {
	
	static final Logger logger = Logger.getLogger(UniversalLinks.class);
	
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String renderContent;
	private String propertyKey;
	private String args;
	private String result;
	protected String target = ERROR;
	private String afterRegistration;
	public String creditCardMsg;
	public String terms;
	public String eComm;
	public String ecommTerms;
	public String pageContent;
	private String manufacturerIndexShop = "0";
	private static boolean reInitHomeBanners = true;
	private static ArrayList<MenuAndBannersModal> homePageBannerList;
	private static LinkedHashMap<String, ArrayList<MenuAndBannersModal>>  getAllBannersList;
	private String requestFrom;
	private String redirectLandingUrl;
	
	
	public String getRequestFrom() {
		return requestFrom;
	}
	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}
	public static boolean isReInitHomeBanners() {
		return reInitHomeBanners;
	}
	public static void setReInitHomeBanners(boolean reInitHomeBanners) {
		UniversalLinks.reInitHomeBanners = reInitHomeBanners;
	}
	public static ArrayList<MenuAndBannersModal> getHomePageBannerList() {
		return homePageBannerList;
	}
	public static void setHomePageBannerList(
			ArrayList<MenuAndBannersModal> homePageBannerList) {
		UniversalLinks.homePageBannerList = homePageBannerList;
	}
	
	public String getManufacturerIndexShop() {
		return manufacturerIndexShop;
	}
	public void setManufacturerIndexShop(String manufacturerIndexShop) {
		this.manufacturerIndexShop = manufacturerIndexShop;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
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
	
	public String Welcome(){
		target = "welcome";
		//System Parameters
		if(CommonDBQuery.getSystemParamtersList()==null){
			CommonDBQuery.getSystemParameters();
		}
		
		//Properties
		if(PropertyAction.SqlContainer==null){
			PropertyAction.loadBasicConfigs(); 
		}
		
		//Menu
		if(MenuAndBannersDAO.topStaticMenu==null){
			MenuAndBannersDAO.getHeaderMenu();
			MenuAndBannersDAO.getStaticMenu();
			MenuAndBannersDAO.getHomePageBanners();
		}
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		    String customerTypeValue = (String)session.getAttribute("customerFlag");
		    String customerTypeName = (String)session.getAttribute("customerTypeName");
		    String browserType = (String) session.getAttribute("browseType"); 
		    boolean loadStaticPage = false;
		    boolean redirectLandingFlag = false;
		    boolean isFeatureProduct = true;
	    
		    System.out.println("userSubsetId : "+tempSubset+" : generalCatalog : "+tempGeneralSubset+" : USER ID : "+(String) session.getAttribute(Global.USERID_KEY));
		    if(session.getAttribute("generalCatalog")==null){
		    	session.setAttribute("generalCatalog","0");
		    	tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		    }
		    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			System.out.println("User Agent : " + request.getHeader("User-Agent"));
			if(!(request.getHeader("User-Agent")!= null && request.getHeader("User-Agent").equalsIgnoreCase("WEBVIEW")) && !CommonUtility.validateString(browserType).equalsIgnoreCase("Mobile") && CommonDBQuery.getSystemParamtersList().get("GET_HOMEPAGE_FROM_CMS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_HOMEPAGE_FROM_CMS").trim().equalsIgnoreCase("Y") && CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID")!=null && CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID").trim()) > 0){
				loadStaticPage = true;
				
				String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
				int userId = CommonUtility.validateNumber(sessionUserId);
				if(userId>1 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_CMS_HOMEPAGE")).equalsIgnoreCase("Y")) {
					contentObject = CIMMTouchUtility.getInstance().getStaticPageById(CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_CMS_HOMEPAGE_ID"), null);
				}else {
					//-- Customer specific landing page
					if(session!=null && CommonUtility.validateNumber((String) session.getAttribute("customerLandingPageId"))>0 ) {
						String customerLandingPageId = CommonUtility.validateString((String) session.getAttribute("customerLandingPageId"));
						if(CommonUtility.validateNumber(customerLandingPageId)<99 && CommonUtility.validateString((String)session.getAttribute("customerLandingPageUrl")).length()>0){
							loadStaticPage = false;
							redirectLandingFlag = true;
							target = "redirectLanding";
							redirectLandingUrl = CommonUtility.validateString((String)session.getAttribute("customerLandingPageUrl"));
						}else{
							contentObject = CIMMTouchUtility.getInstance().getStaticPageById(customerLandingPageId, null);
						}
					 }else {
						contentObject = CIMMTouchUtility.getInstance().getStaticPageById(CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID"), null);
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_PREBUILT_CMS_HOMEPAGE")).equalsIgnoreCase("Y")) {
							String htmlFilePath = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("STATICPAGESPATH"))+CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID"))+".html";
							StringBuilder contentBuilder = CIMMTouchUtility.getInstance().getFileContent(htmlFilePath);
							if(contentBuilder!=null && CommonUtility.validateString(contentBuilder.toString()).length()>0) {
								contentObject.put("pageContent",CommonUtility.validateString(contentBuilder.toString()));
								contentObject.put("enablePrebuiltCmsHomepageScript","Y");
							}
						}
					 }
					//-- Customer specific landing page
				}
				if(!redirectLandingFlag) { //-- Customer specific landing page redirect flag
					contentObject.put("isStaticHomePage", "Y");
					if(contentObject==null){
						contentObject = new LinkedHashMap<>();
					}
				}
			}else{
				if(reInitHomeBanners){
					homePageBannerList = new ArrayList<MenuAndBannersModal>();
					homePageBannerList = ProductsDAO.getHomePageBanners();
					getAllBannersList = ProductsDAO.getAllBannersList();
					reInitHomeBanners = false;
				}
				ArrayList<ProductsModel> featureProducts = new ArrayList<ProductsModel>();	
				if(CommonDBQuery.getSystemParamtersList().get("HOMEPAGE_FEATURE_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("HOMEPAGE_FEATURE_PRODUCT").trim().equalsIgnoreCase("N")){
					isFeatureProduct = false;
				}
				if(isFeatureProduct){
					featureProducts = ProductsDAO.getFeatureProduct(subsetId, generalSubset);
				}
				if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
					ArrayList<Integer> itemList = new ArrayList<Integer>();	
					for(ProductsModel item : featureProducts) {
						int itemId = item.getItemId();
						itemList.add(itemId);
					}
					if(itemList!=null && itemList.size()>0){
						LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "),"itemid");
						contentObject.put("customFieldVal", customFieldVal);
					}
				}	
				contentObject.put("homePageBannerList", homePageBannerList);
				contentObject.put("featuredProducts", featureProducts);
				contentObject.put("getAllBannersList", getAllBannersList);
				if(CommonDBQuery.getSystemParamtersList().get("BESTSELLERITEM")!=null && CommonDBQuery.getSystemParamtersList().get("BESTSELLERITEM").trim().equalsIgnoreCase("Y")){
					ArrayList<ProductsModel> bestSelleritems = ProductHunterSolr.getSpecialItems("bestSellersItem",subsetId);
					contentObject.put("bestSelleritems", bestSelleritems);
				}
				if(CommonDBQuery.getSystemParamtersList().get("POPULARITEM")!=null && CommonDBQuery.getSystemParamtersList().get("POPULARITEM").trim().equalsIgnoreCase("Y")){
					ArrayList<ProductsModel> popularitems = ProductHunterSolr.getSpecialItems("popular",subsetId);
					contentObject.put("popularitems", popularitems);
				}
				if(CommonDBQuery.getSystemParamtersList().get("EXCHANGERATE")!=null && CommonDBQuery.getSystemParamtersList().get("EXCHANGERATE").trim().equalsIgnoreCase("Y")){
					contentObject.put("exchangeDate",new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
					contentObject.put("exchangeRate",ExchangeRate(CommonDBQuery.getSystemParamtersList().get("EXCHANGERATEFROM"),CommonDBQuery.getSystemParamtersList().get("EXCHANGERATETO")));
				}
				if(CommonDBQuery.getSystemParamtersList().get("EVENTS_LIST_AT_HOME_PAGE")!=null && CommonDBQuery.getSystemParamtersList().get("EVENTS_LIST_AT_HOME_PAGE").trim().equalsIgnoreCase("Y")){
					contentObject.put("eventsList",ProductHunterSolr.getEventDetails());
				}
			}
			if(!redirectLandingFlag){ //-- Customer specific landing page redirect flag
				if (customerTypeValue!=null && CommonUtility.validateString(customerTypeValue).equalsIgnoreCase("Y") && CommonUtility.validateString(customerTypeName).length()>0){
						String customerName = customerTypeName.replaceAll("[^a-zA-Z0-9 -]", "");
						customerName = customerName.toLowerCase();
						renderContent = LayoutGenerator.templateLoader(customerName+"NationalAccountsPage", contentObject , null, null, null); //National accountPage
				}else{
					if(loadStaticPage){
						if(contentObject!=null && contentObject.get("fullPageLayout")!=null && CommonUtility.validateString(contentObject.get("fullPageLayout").toString()).equalsIgnoreCase("Y")){
							renderContent = LayoutGenerator.templateLoader("StaticPageFullPageLayout", contentObject , null, null, null);
						}else{
							renderContent = LayoutGenerator.templateLoader("StaticPage", contentObject , null, null, null);
						}
						
					}else if((request.getHeader("User-Agent")!= null && request.getHeader("User-Agent").equalsIgnoreCase("WEBVIEW"))){
						renderContent = LayoutGenerator.templateLoader("MobileHomePage", contentObject , null, null, null);
					}else {
						renderContent = LayoutGenerator.templateLoader("HomePage", contentObject , null, null, null);
					}
				}
			}
		}catch (Exception e) {
	    	e.printStackTrace();
	    	SendMailUtility.sendErrorMail("NullPointerException",e);
		}
		return target;
	}
	
	public String Login() {
		target = SUCCESS;
		request =ServletActionContext.getRequest();
        response = ServletActionContext.getResponse();
        LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
        
		try{
			HttpSession session = request.getSession();
			String type = request.getParameter("type");
			if(type==null){
				contentObject.put("type", "");
			}else{
				contentObject.put("type", type);
			}
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			if(userId>1){
				if(CommonUtility.validateString(type).equalsIgnoreCase("approveCart")) {
					target="approveCart";
				}else {
					target = "welcome";
				}
			}else{
				if(result!=null && result.trim().length()>0){
					String[] arrayStr = result.split("-");
					contentObject.put("message", arrayStr[0]);
					if(arrayStr!=null && arrayStr.length>1 && arrayStr[1]!=null){
						contentObject.put("userNameTmp", arrayStr[1]);
					}
				}
				renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject, null, null, null);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}
	
	public String Brands() {
		try{
			request =ServletActionContext.getRequest();
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
	       
	        String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
	        
	        ArrayList<String> manufacturerIndex = ProductsDAO.getbrandListIndex(CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
			session.setAttribute("manufacturerIndex",manufacturerIndex);
			
			ArrayList<ProductsModel> manufacturerList = ProductsDAO.brandList(manufacturerIndex.get(0),CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
			
			Map<String, ArrayList<ProductsModel>> brandListAsMap = new TreeMap<String, ArrayList<ProductsModel>>();

			for(ProductsModel eachBrand : manufacturerList){
				if(brandListAsMap.containsKey(String.valueOf(eachBrand.getBrandName().charAt(0)).toUpperCase())){
					 ((ArrayList<ProductsModel>)brandListAsMap.get(String.valueOf(eachBrand.getBrandName().charAt(0)).toUpperCase())).add(eachBrand);
				}
				else{
					ArrayList<ProductsModel> brandList  = new ArrayList<ProductsModel>();
					brandList.add(eachBrand);
					brandListAsMap.put(String.valueOf(eachBrand.getBrandName().charAt(0)).toUpperCase(), brandList);
				}
			}
			
			session.setAttribute("brandList",manufacturerList);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			System.out.println("manufacturerIndexShop == " + manufacturerIndexShop);
			contentObject.put("brandListAsMap", brandListAsMap);
			if(manufacturerIndexShop!="0")
			{
				contentObject.put("RunQuery","true");
				contentObject.put("manufactureIndex",manufacturerIndexShop);
			}
			renderContent = LayoutGenerator.templateLoader("BrandPage", contentObject, null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String Manufacturer() {
		try{
			request =ServletActionContext.getRequest();
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
	       
	        String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
	        
	        ArrayList<String> manufacturerIndex = ProductsDAO.getManufacturerIndex(CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
			session.setAttribute("manufacturerIndex",manufacturerIndex);
			
			ArrayList<ProductsModel> manufacturerList = ProductsDAO.getManufacturerList(manufacturerIndex.get(0),CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
			
			Map<String, ArrayList<ProductsModel>> manufacturersAsMap = new TreeMap<String, ArrayList<ProductsModel>>();

			for(ProductsModel eachManufacturer : manufacturerList){
				if(manufacturersAsMap.containsKey(String.valueOf(eachManufacturer.getManufacturerName().charAt(0)).toUpperCase())){
					 ((ArrayList<ProductsModel>)manufacturersAsMap.get(String.valueOf(eachManufacturer.getManufacturerName().charAt(0)).toUpperCase())).add(eachManufacturer);
				}else{
					ArrayList<ProductsModel> manufacturers  = new ArrayList<ProductsModel>();
					manufacturers.add(eachManufacturer);
					manufacturersAsMap.put(String.valueOf(eachManufacturer.getManufacturerName().charAt(0)).toUpperCase(), manufacturers);
				}
			}
			
			session.setAttribute("manufacturerList",manufacturerList);
			
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("manufacturersAsMap",manufacturersAsMap);
			System.out.println("manufacturerIndexShop == " + manufacturerIndexShop);
			if(manufacturerIndexShop!="0")
			{
				contentObject.put("RunQuery","true");
				contentObject.put("manufactureIndex",manufacturerIndexShop);
			}
			renderContent = LayoutGenerator.templateLoader("ManufacturerPage", contentObject, null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String ManufacturerAll() {
		try{
			request =ServletActionContext.getRequest();
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
	       
	        String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
	        
	        ArrayList<String> manufacturerIndex = null;
	        if(session!=null && session.getAttribute("manufacturerIndex")!=null && ((ArrayList<String>)session.getAttribute("manufacturerIndex"))!=null && !((ArrayList<String>)session.getAttribute("manufacturerIndex")).isEmpty()){
	        	manufacturerIndex = (ArrayList<String>)session.getAttribute("manufacturerIndex");
	        }else if(manufacturerIndex==null){
	        	manufacturerIndex = ProductsDAO.getManufacturerIndex(CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
	        }
			session.setAttribute("manufacturerIndex",manufacturerIndex);
			
			LinkedHashMap<String, ArrayList<ProductsModel>> getAllManufacturerList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
			if(manufacturerIndex!=null && manufacturerIndex.size()>0){
				for (String manufIdx : manufacturerIndex) {
					ArrayList<ProductsModel> manufacturerList = ProductsDAO.getManufacturerList(manufIdx,CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
					if(manufacturerList!=null && !manufacturerList.isEmpty()){
						getAllManufacturerList.put(manufIdx, manufacturerList);
					}
				}
			}
			session.setAttribute("getAllManufacturerList",getAllManufacturerList);
			
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("getAllManufacturerList",getAllManufacturerList);

			renderContent = LayoutGenerator.templateLoader("ManufacturerPage", contentObject, null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String DirectFromManufacturer() {
		try{
			request =ServletActionContext.getRequest();
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
	       
	        String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			
			int subsetIdForDFM = CommonUtility.validateNumber((String) session.getAttribute("userSubsetId"));
        	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID")).length()>0 && CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID"))>0){
        		subsetIdForDFM = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID"));
			}
			
	        ArrayList<String> manufacturerIndex = null;
	        if(session!=null && session.getAttribute("directFromManufacturerIndex")!=null && ((ArrayList<String>)session.getAttribute("directFromManufacturerIndex"))!=null && !((ArrayList<String>)session.getAttribute("directFromManufacturerIndex")).isEmpty()){
	        	manufacturerIndex = (ArrayList<String>)session.getAttribute("directFromManufacturerIndex");
	        }else if(manufacturerIndex==null){
	        	manufacturerIndex = ProductsDAO.getDirectFromManufacturerIndex(subsetIdForDFM,generalSubset);
	        }
			session.setAttribute("directFromManufacturerIndex",manufacturerIndex);
			
			LinkedHashMap<String, ArrayList<ProductsModel>> getAllDirectFromManufacturerIndexList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
			if(manufacturerIndex!=null && manufacturerIndex.size()>0){
				for (String manufIdx : manufacturerIndex) {
					ArrayList<ProductsModel> manufacturerList = ProductsDAO.getDirectFromManufacturerListDAO(manufIdx, subsetIdForDFM, generalSubset);
					if(manufacturerList!=null && !manufacturerList.isEmpty()){
						getAllDirectFromManufacturerIndexList.put(manufIdx, manufacturerList);
					}
				}
			}
			session.setAttribute("getAllDirectFromManufacturerIndexList",getAllDirectFromManufacturerIndexList);
			
			session.setAttribute("PREVIOUS_USER_SUBSET_ID", CommonUtility.validateString((String) session.getAttribute("userSubsetId")));
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID")).length()>0 && CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID"))>0){
				session.setAttribute("userSubsetId", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID")));
				session.setAttribute("DFM_SUBSET_ID", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID")));
			}
			
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("getAllDirectFromManufacturerIndexList",getAllDirectFromManufacturerIndexList);

			renderContent = LayoutGenerator.templateLoader("DircetFromManufacturerPage", contentObject, null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String Register() {
		String staticPagePath = CommonDBQuery.getSystemParamtersList().get("STATICPAGESPATH");
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		target = SUCCESS;
		try
		{
			if(userId>1){
				target = "welcome";
			}else{
				
				SAXBuilder builder = new SAXBuilder();
				File file = new File(staticPagePath+CommonDBQuery.getSystemParamtersList().get("REGISTRATION_TERMS_PAGE")+".xml");
				System.out.println(file.getPath());
				if(file.exists())
				{
					org.jdom.Document dom = builder.build(file);
					pageContent=dom.getRootElement().getChildText("pageContent");
				
				}
				builder = new SAXBuilder();
				file = new File(staticPagePath+CommonDBQuery.getSystemParamtersList().get("REGISTRATION_COMPLETE_MSG_PAGE")+".xml");
				if(file.exists())
				{
					org.jdom.Document dom = builder.build(file);
					setAfterRegistration(dom.getRootElement().getChildText("pageContent"));
				
				}
				builder = new SAXBuilder();
				file = new File(staticPagePath+CommonDBQuery.getSystemParamtersList().get("REGISTRATION_CREDITCARD_MSG_PAGE")+".xml");
				if(file.exists())
				{
					org.jdom.Document dom = builder.build(file);
					creditCardMsg=dom.getRootElement().getChildText("pageContent");
				
				}
				builder = new SAXBuilder();
				file = new File(staticPagePath+CommonDBQuery.getSystemParamtersList().get("REGISTRATION_ECOMM_TERMS_PAGE")+".xml");
				if(file.exists())
				{
					org.jdom.Document dom = builder.build(file);
					ecommTerms=dom.getRootElement().getChildText("pageContent");
				}
				terms = CommonDBQuery.getSystemParamtersList().get("REGISTRATION_TERMS_WORDS");
				eComm = CommonDBQuery.getSystemParamtersList().get("REGISTRATION_ECOMM_WORDS");
			
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ISPRICETEMPLATEOVERRIDE")).equalsIgnoreCase("Y")){
					String priceClassEntityId =request.getParameter("entityId");
					if(CommonUtility.validateString(priceClassEntityId).length()>0){
						session.setAttribute("entityId", priceClassEntityId);
					}
				}
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("creditCardMsg", creditCardMsg);
				contentObject.put("ecommTerms", ecommTerms);
				contentObject.put("afterRegistration", afterRegistration);
				contentObject.put("pageContent", pageContent);
				contentObject.put("wearhouseList", CommonDBQuery.getWareHouseList());
				contentObject.put("firstName", CommonUtility.validateString(request.getParameter("firstName")));
				contentObject.put("lastName", CommonUtility.validateString(request.getParameter("lastName")));
				contentObject.put("emailAddress", CommonUtility.validateString(request.getParameter("email")));
				renderContent = LayoutGenerator.templateLoader("RegisterPage", contentObject , null, null, null);
			}
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return target;
	};
	
	public String ChangePassword() {
		try{
			request =ServletActionContext.getRequest();
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
	        LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	        String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			
			if(userId>1 && !CommonUtility.validateString((String) session.getAttribute("isPunchoutUser")).equalsIgnoreCase("Y")){
				contentObject.put("userName", session.getAttribute(Global.USERNAME_KEY));
				renderContent = LayoutGenerator.templateLoader("ChangePasswordPage", contentObject , null, null, null);
				target = SUCCESS;
			
			}else{
				target = "SESSIONEXPIRED";
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}
	
	public String Forgot() {
		try{
			
			renderContent = LayoutGenerator.templateLoader("ForgotPasswordPage", null , null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String NewShipto() {
		try{
			request =ServletActionContext.getRequest();
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			
			if(userId>1){
				renderContent = LayoutGenerator.templateLoader("NewShippingAddressPage", null , null, null, null);
				target = SUCCESS;
			
			}else{
				target = "SESSIONEXPIRED";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}
	
/*	public String systemParameter()
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		String configKey = request.getParameter("configKey");
		if(CommonUtility.validateString(configKey).length()>0){
			renderContent = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(configKey));
		}else{
			renderContent = "";
		}
		return SUCCESS;
	}*/
	
	
	public String sessionValue()
	{
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String keyValue = request.getParameter("keyValue");
			String insertValue = request.getParameter("insertValue");
			String crud = request.getParameter("crud");
			if(!CommonUtility.validateString(keyValue).toLowerCase().contains("password")) {
				if(CommonUtility.validateString(crud).length()>0 && crud.equalsIgnoreCase("r")){
					session.removeAttribute(keyValue);
				}
				if(CommonUtility.validateString(crud).length()>0 && crud.equalsIgnoreCase("s")){
					session.setAttribute(CommonUtility.validateString(keyValue), CommonUtility.validateString(insertValue));
				}
				if(CommonUtility.validateString(keyValue).length()>0){
					renderContent = (String) session.getAttribute(keyValue);
					if(CommonUtility.validateString(renderContent).length()==0){
						renderContent = "NoValue";
					}
				}else{
					renderContent = "";
				}
			}else{
				renderContent = "";
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String ClusterData() {
		try{
			
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String keyValue = request.getParameter("keyValue");
			String insertValue = request.getParameter("insertValue");
			String crud = request.getParameter("crud");
			if(CommonUtility.validateString(crud).length()>0 && crud.equalsIgnoreCase("r")){
				session.removeAttribute(keyValue);
			}
			if(CommonUtility.validateString(crud).length()>0 && crud.equalsIgnoreCase("s")){
				session.setAttribute(CommonUtility.validateString(keyValue), CommonUtility.validateString(insertValue));
			}
			if(CommonUtility.validateString(keyValue).length()>0){
				renderContent = (String) session.getAttribute(keyValue);
				if(CommonUtility.validateString(renderContent).length()==0){
					renderContent = "NoValue";
				}
			}else{
				renderContent = "";
			}
		//CustomServiceProvider
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String customerErpId;
		if(CommonUtility.customServiceUtility()!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_CLUSTER_DATA")!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_CLUSTER_DATA").trim().equalsIgnoreCase("Y")) {
			CommonUtility.customServiceUtility().sessionValue();
			if(userId > 1) {
					CommonUtility.customServiceUtility().getClusterdata();
			}else {
				String clusterlistjson = null;
				Map<String,List<String>> clusterlist =  CommonUtility.customServiceUtility().getClusterdatabeforelogin();
				if(clusterlist.size() > 0) {
					clusterlistjson =   new Gson().toJson(clusterlist);
			    }
				renderContent = clusterlistjson;
			}
		}	
		//CustomServiceProvider
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String CustomerHomeBranch() {
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			//HttpSession session = request.getSession();
			String customerID = request.getParameter("customerid");
			String zipcode = request.getParameter("selectedzipcode");
			String targetid = request.getParameter("targetbranch");
			//CustomServiceProvider
			if(customerID !=null) {
				if(CommonUtility.customServiceUtility()!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_CLUSTER_DATA")!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_CLUSTER_DATA").trim().equalsIgnoreCase("Y")) {
					renderContent = CommonUtility.customServiceUtility().getwareHouseDetail(customerID);
				}
			}else {
				renderContent = CommonUtility.customServiceUtility().getShipviaDetail(zipcode);
			}
			if(targetid !=null) {
				if(CommonUtility.customServiceUtility()!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_CLUSTER_DATA")!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_CLUSTER_DATA").trim().equalsIgnoreCase("Y")) {
					renderContent = CommonUtility.customServiceUtility().getwareHouseDetail(targetid);
				}	
			}
			
		//CustomServiceProvider
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
		
	public String propertyLoader()
	{
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			Properties locale = null;
			if(session.getAttribute("localeCode")!=null ){
				locale = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase());
		    }else{
		    	locale = LayoutLoader.getMessageProperties().get("EN");
		    }
			String argList[] = CommonUtility.validateString(args).trim().split(",");
			Object[] arguments = argList;
			renderContent = MessageFormat.format(CommonUtility.validateString(locale.getProperty(propertyKey)), arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String FeatureProducts(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		    System.out.println("userSubsetId : "+tempSubset+" : generalCatalog : "+tempGeneralSubset);
		    if(session.getAttribute("generalCatalog")==null){
		    	session.setAttribute("generalCatalog","0");
		    }
		    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
		
	    	ArrayList<ProductsModel> featureProducts = ProductsDAO.getFeatureProduct(subsetId, generalSubset);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("featuredProducts", featureProducts);
			contentObject.put("blockType", "FeaturedProducts");
			renderContent = LayoutGenerator.templateLoader("HomePageCategory", contentObject , null, null, null);
	     }catch (Exception e) {
	    	e.printStackTrace();
	    	SendMailUtility.sendErrorMail("NullPointerException",e);
		}
		return SUCCESS;
	
	}
	
	public String ExchangeRate(String from, String to){
		URL url;
		String exchangeRate = "";
		DecimalFormat df = new DecimalFormat("####0.###");
		try {
			
			String exchangeUrl = "";
			if(CommonDBQuery.getSystemParamtersList().get("EXCHANGERATEURL")!=null && !CommonDBQuery.getSystemParamtersList().get("EXCHANGERATEURL").trim().equalsIgnoreCase(""))
			{
				exchangeUrl = CommonDBQuery.getSystemParamtersList().get("EXCHANGERATEURL")+"&from="+from+"&to="+to;
			url = new URL(exchangeUrl);
			URLConnection yc = url.openConnection();
	        BufferedReader in = new BufferedReader(
	                            new InputStreamReader(
	                            yc.getInputStream()));
	        String jsonObject = "";
	        String line;
	        while ((line = in.readLine()) != null){ 
	            jsonObject += line;
	        }
	        in.close();
	        System.out.println(jsonObject);
	         Gson gson = new Gson();
	        ProductsModel data = gson.fromJson(jsonObject, ProductsModel.class);
	        System.out.println(" : "+data.getRate());
	        exchangeRate = df.format(data.getRate());
	        System.out.println("exchangeRate : "+exchangeRate);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
        return exchangeRate;
	}
	
	public String SendThisPage() {
		try{
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("result",result);
			renderContent = LayoutGenerator.templateLoader("SendThisPage", contentObject , null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String SendPage() {
		try{
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("result",result);
			renderContent = LayoutGenerator.templateLoader("SendThisPageForAll", contentObject , null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String shareEvent() {
		try{
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			
			renderContent = LayoutGenerator.templateLoader("ShareEvent", contentObject , null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String AdvForgotPassword() {
		try{
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("requestFrom", CommonUtility.validateString(requestFrom));
			if(CommonUtility.validateString(propertyKey).length()>0){
				int count=UsersDAO.getAdvPassword24Hours(propertyKey);
				if(count>0){
					UsersModel userInfo = new UsersModel();
					userInfo.setaRPassword(CommonUtility.validateString(propertyKey));
					userInfo = UsersDAO.retrieveAdvancedForgorPassword(userInfo);
					userInfo.setStatusDescription("NA");
				    UsersDAO.advancedForgotPasswordDisable(userInfo);
					contentObject.put("accessDisabled", "Y");					
				}else{
					contentObject.put("mailMessage", propertyKey);	
				}
			}
			renderContent = LayoutGenerator.templateLoader("AdvForgotPassword", contentObject , null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String RegisterFull() {
		String staticPagePath = CommonDBQuery.getSystemParamtersList().get("STATICPAGESPATH");
		try
		{
			SAXBuilder builder = new SAXBuilder();
			File file = new File(staticPagePath+CommonDBQuery.getSystemParamtersList().get("REGISTRATION_TERMS_PAGE")+".xml");
			System.out.println(file.getPath());
			if(file.exists())
			{
				org.jdom.Document dom = builder.build(file);
				pageContent=dom.getRootElement().getChildText("pageContent");
			
			}
			builder = new SAXBuilder();
			file = new File(staticPagePath+CommonDBQuery.getSystemParamtersList().get("REGISTRATION_COMPLETE_MSG_PAGE")+".xml");
			if(file.exists())
			{
				org.jdom.Document dom = builder.build(file);
				setAfterRegistration(dom.getRootElement().getChildText("pageContent"));
			
			}
			builder = new SAXBuilder();
			file = new File(staticPagePath+CommonDBQuery.getSystemParamtersList().get("REGISTRATION_CREDITCARD_MSG_PAGE")+".xml");
			if(file.exists())
			{
				org.jdom.Document dom = builder.build(file);
				creditCardMsg=dom.getRootElement().getChildText("pageContent");
			
			}
			builder = new SAXBuilder();
			file = new File(staticPagePath+CommonDBQuery.getSystemParamtersList().get("REGISTRATION_ECOMM_TERMS_PAGE")+".xml");
			if(file.exists())
			{
				org.jdom.Document dom = builder.build(file);
				ecommTerms=dom.getRootElement().getChildText("pageContent");
			}
			terms = CommonDBQuery.getSystemParamtersList().get("REGISTRATION_TERMS_WORDS");
			eComm = CommonDBQuery.getSystemParamtersList().get("REGISTRATION_ECOMM_WORDS");
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		contentObject.put("creditCardMsg", creditCardMsg);
		contentObject.put("ecommTerms", ecommTerms);
		contentObject.put("afterRegistration", afterRegistration);
		contentObject.put("pageContent", pageContent);
		contentObject.put("wearhouseList", CommonDBQuery.getWareHouseList());
		renderContent = LayoutGenerator.templateLoader("RegisterPageFull", contentObject , null, null, null);
		return SUCCESS;
	};
	public String app() {
		try{
        request =ServletActionContext.getRequest();
	        String str = request.getParameter("key");
	        LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	        if(CommonUtility.validateString(str).length()>0)
	        {
	        	renderContent = LayoutGenerator.templateLoader(str,contentObject, null, null, null);
	        }
		}catch (Exception e) {
			
			e.printStackTrace();
		}
        return SUCCESS;
	}
	public String checkBrandsCustomTable() {
        Gson gson = new Gson();
        try{
	        request =ServletActionContext.getRequest();
	        String brandID = request.getParameter("brandId");
	        String result = "";
	        if(CommonUtility.validateNumber(brandID)>0){
	        	List<CustomTable> getReslt = CIMM2VelocityTool.getInstance().getCusomTableDataByFieldValue(CommonUtility.validateNumber(brandID),"BRAND",0,0,"ARI_BRAND_CONFIGURATION","HAS_BRAND_PART_PAGE,WEBSITE,","Y,WEINGARTZ","BRAND_NAME","BRANDS");
	        	result = gson.toJson(getReslt);
	        }
	        renderContent = result;
        }catch (Exception e) {
			
			e.printStackTrace();
		}
        return SUCCESS;
	}
	public String PartsLookup() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String brandName = request.getParameter("brand");
		try{
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			//contentObject = DataSmartController.getInstance().getAllBrandsWithARI(contentObject);
			if(CommonUtility.validateString(brandName).length()>0){
				brandName = DataSmartController.getInstance().getBrandIdandName().get(brandName);
				brandName = brandName.replaceAll("\\-", " ");
				brandName = brandName.replaceAll("and","&");
				contentObject.put("BrandName", CommonUtility.validateString(brandName));
				contentObject.put("BrandNameEncoded", URLEncoder.encode(CommonUtility.validateString(brandName),"UTF-8"));
			}
			renderContent = LayoutGenerator.templateLoader("partLookup", contentObject , null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String ModelGuide() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String brandName = request.getParameter("brand");
		try{
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			//contentObject = DataSmartController.getInstance().getAllBrandsWithARI(contentObject);
			if(CommonUtility.validateString(brandName).length()>0){
				brandName = DataSmartController.getInstance().getBrandIdandName().get(brandName);
				brandName = brandName.replaceAll("\\-", " ");
				brandName = brandName.replaceAll("and","&");
				contentObject.put("BrandName", CommonUtility.validateString(brandName));
				contentObject.put("BrandNameEncoded", URLEncoder.encode(CommonUtility.validateString(brandName),"UTF-8"));
			}
			renderContent = LayoutGenerator.templateLoader("ModelLookUp", contentObject , null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String getBlogSearch(){
		BlogFeed responseBlog = null;
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String keyWord = CommonUtility.validateString(request.getParameter("keyWord"));
			String searchType = CommonUtility.validateString(request.getParameter("searchType"));
			responseBlog = CIMM2VelocityTool.getInstance().getBlogFeedSearch(keyWord, searchType);
			Gson test = new Gson();
			renderContent = test.toJson(responseBlog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getBlogPosts(){
		BlogFeed responseBlog = null;
		try {
			responseBlog = CIMM2VelocityTool.getInstance().getBlogFeed();
			Gson test = new Gson();
			renderContent = test.toJson(responseBlog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String orderLookUp(){
		try{
			request =ServletActionContext.getRequest();
			String userName =request.getParameter("userName"); 
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("userName", userName);
			renderContent = LayoutGenerator.templateLoader("OrderLookUp", contentObject , null, null, null);
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return SUCCESS;  
	}
	
	public String sendCallForPriceNotification(){
		request =ServletActionContext.getRequest();
		try {
			String userName =request.getParameter("userName"); 
			String partNumber =request.getParameter("partNumber");
			if(CommonUtility.validateString(partNumber).length()>0){
				ArrayList<ProductsModel> itemList = new ArrayList<ProductsModel>();
				UsersModel userDetails =new UsersModel();
				
				String[] splitDetails = CommonUtility.validateString(partNumber).split("~");
				if(splitDetails.length>0){
					for(String partValue : splitDetails){
						ProductsModel pModel = new ProductsModel();
						pModel.setPartNumber(partValue);
						itemList.add(pModel);
					}
				}
				
				if(CommonUtility.validateString(userName).length()>0){
					if(CommonUtility.validateString(userName).equalsIgnoreCase("web")){
						userName = "Guest User";
					}
					userDetails.setUserName(CommonUtility.validateString(userName));
				}
				SendMailUtility.sendNotificationOnCallForPrice(userDetails, itemList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String configCatalog(){
		request =ServletActionContext.getRequest();
		BufferedReader br = null;
		FileReader fr = null;
		try{
			String id = request.getParameter("id");
			if(CommonUtility.validateNumber(id)>0) {
				System.out.println(CommonDBQuery.getSystemParamtersList().get("SMC_FILE_PATH")+"SMC_XML/"+CommonUtility.validateNumber(id)+".xml");
				fr = new FileReader(CommonDBQuery.getSystemParamtersList().get("SMC_FILE_PATH")+"SMC_XML/"+CommonUtility.validateNumber(id)+".xml");
			}else {
				System.out.println(CommonDBQuery.getSystemParamtersList().get("SMC_FILE_PATH")+CommonUtility.validateNumber(id)+".xml");
				fr = new FileReader(CommonDBQuery.getSystemParamtersList().get("SMC_FILE_PATH")+CommonUtility.validateNumber(id)+".xml");
			}
			br = new BufferedReader(fr);
			String sCurrentLine;
			StringBuilder xmlString = new StringBuilder();
			String decodedString = null;

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				xmlString.append(sCurrentLine);
			}
			decodedString = xmlString.toString();
			decodedString = StringEscapeUtils.unescapeHtml(decodedString);
			Source xml = null;
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			xml = new StreamSource(new StringReader(decodedString));
			Source xslt = new StreamSource(CommonDBQuery.getSystemParamtersList().get("SMC_FILE_PATH")+"style.xsl");
			//System.out.println(xml.toString());
			contentObject.put("pageContent", CommonUtility.convertXMLToHTML(xml, xslt));
			renderContent = LayoutGenerator.templateLoader("StaticPageFullPageLayout", contentObject, null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.closeFileReader(fr);
		}
		return SUCCESS;
	}
	
	public String abc012XYZ890()
	{
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			String configKey = request.getParameter("ck");//configKey
			if(CommonUtility.validateString(configKey).length()>0){
				renderContent = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get(configKey));
			}else{
				renderContent = "";
			}
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String continueRegister() {
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String userName = CommonUtility.validateString(request.getParameter("userName"));
		target = "success";
		if(userName!=null && userName.equalsIgnoreCase("approval")){
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_SUCCESS_PAGE")).equalsIgnoreCase("Y")){
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();	
			contentObject.put("Result", "Approval Pending");
			renderContent = LayoutGenerator.templateLoader("RegistrationSuccess", contentObject , null, null, null);
			}
		}else{
		try
		{
			 LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			 HashMap<String,String> userDetails = UsersDAO.getUserPasswordAndUserId(userName,"P");
			 if(userDetails!=null && userDetails.size()>0){}else{
				 userDetails=UsersDAO.getUserPasswordAndUserId(userName,"SP");
			 }
			if(userDetails!=null && userDetails.size()>0){
				HashMap<String,UsersModel>	getDetails =UsersDAO.getUserAddressFromBCAddressBook(CommonUtility.validateNumber(userDetails.get("defaultBillingAddressId")),CommonUtility.validateNumber(userDetails.get("defaultShippingAddressId")));
				UsersModel userBillAddress = getDetails.get("Bill");
				UsersModel userShipAddress = getDetails.get("Ship");	
				HashMap<String,String>	customerCustomFieldData = UsersDAO.getAllCustomerCustomFieldValue(CommonUtility.validateNumber(userDetails.get("buyingCompanyId")));
				HashMap<String,String>	shippingCustomFieldData = UsersDAO.getBCAddressBookCustomFields(CommonUtility.validateNumber(userDetails.get("defaultShippingAddressId")));
				HashMap<String,String>	userCustomFieldData = UsersDAO.getAllUserCustomFieldValue(CommonUtility.validateNumber(userDetails.get("userId")));
				contentObject.put("userDetails", userDetails);
				contentObject.put("customerCustomFieldData", customerCustomFieldData);
				contentObject.put("shippingCustomFieldData", shippingCustomFieldData);
				contentObject.put("userCustomFieldData", userCustomFieldData);
				contentObject.put("userBillAddress", userBillAddress);
				contentObject.put("userShipAddress", userShipAddress);
				if(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register.user.status")!=null){
				contentObject.put("isPartialUser", LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register.user.status"));
				}
				renderContent = LayoutGenerator.templateLoader("RegisterPage", contentObject , null, null, null);
			}
			
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		}
		return target;
	}
	public String setSelectedBranch() {
		try{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String homeBranchId = "";
		homeBranchId = request.getParameter("wareHouseCode"); 
		if( homeBranchId == null ) {
		homeBranchId = (String) session.getAttribute("wareHouseCode");
		}
		LinkedHashMap<String,ProductsModel> branchDetail = CommonDBQuery.branchDetailData;
		ProductsModel branch = branchDetail.get(homeBranchId);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		contentObject.put("homeBranchDetails",branch);
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
	}	
	
	public String emailAuthentication() {
		try {
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			if(userId<=1) {
				String emailAddress = CommonUtility.validateString(request.getParameter("email"));
				String authenticationCode = CommonUtility.validateString(request.getParameter("authcode"));
				if(CommonUtility.validateString(authenticationCode).length()>0 && CommonUtility.validateString(authenticationCode).length()>0) {
					contentObject.put("emailAddress", emailAddress);
					contentObject.put("authenticationCode", authenticationCode);
					renderContent = LayoutGenerator.templateLoader("EmailAuthenticationPage", contentObject , null, null, null);
				}
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String changeEmail() {
		try{
			request =ServletActionContext.getRequest();
	        HttpSession session = request.getSession();
	        LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	        String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			
			if(userId>1){
				contentObject.put("userName", session.getAttribute(Global.USERNAME_KEY));
				renderContent = LayoutGenerator.templateLoader("ChangeEmailPage", contentObject , null, null, null);
				target = SUCCESS;
			}else{
				target = "SESSIONEXPIRED";
			}
			
		}catch (Exception e) {
			logger.error(e);
		}
		return target;
	}
	
	public String BrandsV2() {
		try{
			request =ServletActionContext.getRequest();
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
	        String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
	        
			LinkedHashMap<String, Object> brandData = ProductHunterSolr.brandsList( CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
			
			ArrayList<String> manufacturerIndex = (ArrayList<String>) brandData.get("mindex");
			session.setAttribute("manufacturerIndex",manufacturerIndex);
			
			ArrayList<ProductsModel> manufacturerList = (ArrayList<ProductsModel>) brandData.get("manufacturerList");
			
			Map<String, ArrayList<ProductsModel>> brandListAsMap = (Map<String, ArrayList<ProductsModel>>) brandData.get("brandList");
			
			session.setAttribute("brandList",manufacturerList);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			System.out.println("manufacturerIndexShop == " + manufacturerIndexShop);
			contentObject.put("brandListAsMap", brandListAsMap);
			if(manufacturerIndexShop!="0")
			{
				contentObject.put("RunQuery","true");
				contentObject.put("manufactureIndex",manufacturerIndexShop);
			}
			renderContent = LayoutGenerator.templateLoader("BrandPage", contentObject, null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/*
	public String getTwittePosts(){
		ResponseTwitter responseTwitter = null;
		try {
			CIMM2VelocityTool cimm2VelocityTool = new CIMM2VelocityTool();
			responseTwitter = cimm2VelocityTool.getTwittePosts();
			Gson test = new Gson();
			renderContent = test.toJson(responseTwitter);;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getFacebookPosts(){
		ResponseFaceBook responseFaceBook = null;
		try {
			CIMM2VelocityTool cimm2VelocityTool = new CIMM2VelocityTool();
			responseFaceBook = cimm2VelocityTool.getFacebookPosts();
			Gson test = new Gson();
			renderContent = test.toJson(responseFaceBook);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	*/
	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	public String getRenderContent() {
		return renderContent;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getArgs() {
		return args;
	}
	public String getAfterRegistration() {
		return afterRegistration;
	}
	public void setAfterRegistration(String afterRegistration) {
		this.afterRegistration = afterRegistration;
	}
	public String getCreditCardMsg() {
		return creditCardMsg;
	}
	public void setCreditCardMsg(String creditCardMsg) {
		this.creditCardMsg = creditCardMsg;
	}
	public String getTerms() {
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	public String geteComm() {
		return eComm;
	}
	public void seteComm(String eComm) {
		this.eComm = eComm;
	}
	public String getEcommTerms() {
		return ecommTerms;
	}
	public void setEcommTerms(String ecommTerms) {
		this.ecommTerms = ecommTerms;
	}
	public String getPageContent() {
		return pageContent;
	}
	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}
	public static void setGetAllBannersList(LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getAllBannersList) {
		UniversalLinks.getAllBannersList = getAllBannersList;
	}
	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getGetAllBannersList() {
		return getAllBannersList;
	}
	public String getRedirectLandingUrl() {
		return redirectLandingUrl;
	}
	public void setRedirectLandingUrl(String redirectLandingUrl) {
		this.redirectLandingUrl = redirectLandingUrl;
	}

}
