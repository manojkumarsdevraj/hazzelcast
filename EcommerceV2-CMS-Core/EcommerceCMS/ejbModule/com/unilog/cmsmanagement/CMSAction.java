package com.unilog.cmsmanagement;

import static com.unilog.cmsmanagement.util.CMSConstants.brandPath;
import static com.unilog.cmsmanagement.util.CMSConstants.context;
import static com.unilog.cmsmanagement.util.CMSConstants.find;
import static com.unilog.cmsmanagement.util.CMSConstants.list;
import static com.unilog.cmsmanagement.util.CMSConstants.mnufacturerPath;
import static com.unilog.cmsmanagement.util.CMSConstants.staticPage;
import static com.unilog.cmsmanagement.util.CMSConstants.taxonomyTreePath;
import static com.unilog.cmsmanagement.util.CMSConstants.taxonomy;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.ConversionTool;
import org.apache.velocity.tools.generic.DisplayTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.apache.velocity.tools.generic.SortTool;
import org.apache.velocity.tools.view.tools.LinkTool;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.cmsmanagement.dao.CmsDao;
import com.unilog.cmsmanagement.model.BannerInfo;
import com.unilog.cmsmanagement.model.BannerInfoData;
import com.unilog.cmsmanagement.util.BannerTemplate;
import com.unilog.cmsmanagement.util.CMSConstants;
import com.unilog.cmsmanagement.util.CMSUtility;
import com.unilog.cmsmanagement.util.Cimm2BCentralClient;
import com.unilog.database.CommonDBQuery;
import com.unilog.model.BrandDataModel;
import com.unilog.model.BrandListModel;
import com.unilog.model.CategoryDataModel;
import com.unilog.model.CategoryListModel;
import com.unilog.model.CriteriaIntValue;
import com.unilog.model.ManufacturerDataModel;
import com.unilog.model.ManufacturerListModel;
import com.unilog.model.PageHistoryModel;
import com.unilog.model.PromotionIntRequest;
import com.unilog.model.StaticPageUpdateModel;
import com.unilog.model.TaxonomyListModel;
import com.unilog.model.ThemeModel;
import com.unilog.model.WebsiteModel;
import com.unilog.model.banners.BannerImageList;
import com.unilog.model.banners.BannerList;
import com.unilog.model.form.FormData;
import com.unilog.model.form.FormList;
import com.unilog.model.locale.LocaleModel;
import com.unilog.model.staticpage.StaticPageData;
import com.unilog.model.staticpage.StaticPageList;
import com.unilog.model.userLibrary.ComponentList;
import com.unilog.model.userLibrary.LibraryList;
import com.unilog.model.widget.WidgetData;
import com.unilog.model.widget.WidgetList;
import com.unilog.model.Criteria;
import com.unilog.model.PromotionRequest;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.dao.ThemesDao;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class CMSAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String renderContent; 
	private int page;
	private int pageSize = 10;
	private int siteId;
	private String selectedSiteId;
	private String selectedSiteName;
	private int pageId;
	private int bannerListId;
	private String reload;
	private String staticPageData;
	private StaticPageData pageData;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int widgetId;
	private int formId;
	private int libraryId;
	private WidgetData widget;
	private FormData formData;
	private String type;
	private String searchColumnType;
	private String[] widgetTypeKey;
	private String[] widgetTypeValue;
	private String query;
	private String sortEnabled;
	private String sortColumn;
	private static List<WebsiteModel> websiteList;
	private static LinkedHashMap<Integer, WebsiteModel> websiteDetailsBySiteId;

	
	public static LinkedHashMap<Integer, WebsiteModel> getWebsiteUrlListById() {
		return websiteDetailsBySiteId;
	}
	public static void setWebsiteUrlListById(LinkedHashMap<Integer, WebsiteModel> websiteUrlListById) {
		CMSAction.websiteDetailsBySiteId = websiteUrlListById;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public String getSearchColumnType() {
		return searchColumnType;
	}
	public void setSearchColumnType(String searchColumnType) {
		this.searchColumnType = searchColumnType;
	}
	public static List<WebsiteModel> getWebsiteList() {
		return websiteList;
	}
	public static void setWebsiteList(List<WebsiteModel> websiteList) {
		CMSAction.websiteList = websiteList;
	}
	public String getSelectedSiteName() {
		return selectedSiteName;
	}
	public void setSelectedSiteName(String selectedSiteName) {
		this.selectedSiteName = selectedSiteName;
	}
	public String getSelectedSiteId() {
		return selectedSiteId;
	}
	public void setSelectedSiteId(String selectedSiteId) {
		this.selectedSiteId = selectedSiteId;
	}
	public String getType(){
		return type;
	}
	public String getSortEnabled() {
		return sortEnabled;
	}
	public void setSortEnabled(String sortEnabled) {
		this.sortEnabled = sortEnabled;
	}
	public void setType(String type){
		this.type = type;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public String cmsPage(){
		renderContent = "Welcome new CMS";
		return "framePage";
	}
	
	static {
		getWebsiteListFromDB();
	}
	
	public static void getWebsiteListFromDB(){
		try{
			CmsDao cmsDao = new CmsDao();
			setWebsiteList(cmsDao.getWebsiteList());
			List<WebsiteModel> websiteList =  getWebsiteList();
			if(websiteList!=null && websiteList.size()>0){
				websiteDetailsBySiteId = new LinkedHashMap<Integer, WebsiteModel>();
    			for (WebsiteModel websiteModel : websiteList){
					if(websiteModel.getSiteId()>0) {
						websiteDetailsBySiteId.put(websiteModel.getSiteId(), websiteModel);
					}
				}
    		} 
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String updateTheme() {
		request =ServletActionContext.getRequest();
		response =ServletActionContext.getResponse();
		try {
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date date = new Date();
			String forDateParameter = CommonUtility.validateString(formatter.format(date));
			String themeFileName = request.getParameter("cssName");
			String domainName = request.getScheme()+"://"+request.getServerName();
		    String webthemePath = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES"));
		    String siteName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
		    String folderName = "/css/";
		    String filePathURL = CommonUtility.validateString(domainName)+"/"+webthemePath+"/"+siteName+folderName;
		    
			System.out.println("Domain Name: "+domainName+" : filePathURL: "+filePathURL); 
			
			if(CommonUtility.validateString(themeFileName).length()>0) {
				if (CommonUtility.validateString(CommonDBQuery.checkForTheme()).length()>0){
					renderContent = "Theme already scheduled";
				}else {
					if(CommonUtility.validateString(themeFileName).toLowerCase().contains(".css") && !CommonUtility.validateString(themeFileName).toLowerCase().contains("default")) {
						filePathURL = filePathURL+CommonUtility.validateString(themeFileName);
						if(CommonUtility.validateHttpUrlResponse(filePathURL)) {
							themeFileName = CommonUtility.validateString(themeFileName)+"?dt="+CommonUtility.validateString(forDateParameter);
							CommonDBQuery.getSystemParamtersList().put("ThemeFileName", CommonUtility.validateString(themeFileName));
							ThemesDao.updateAppliedThemeToSystemParameter(CommonUtility.validateString(themeFileName));
							renderContent = "Theme update successful";
						}else {
							CommonDBQuery.getSystemParamtersList().put("ThemeFileName", "");
							ThemesDao.updateAppliedThemeToSystemParameter("");
							renderContent = "Theme file not available on server";
						}
						
						
					}else {
						CommonDBQuery.getSystemParamtersList().put("ThemeFileName", "");
						ThemesDao.updateAppliedThemeToSystemParameter("");
						renderContent = "Switched to default theme";
					}
				}
			}else {
				renderContent = "Invalid theme name";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public boolean validateCms(){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		boolean isValidate = true;
		System.out.println("Inside Validate CMS");
		try{
			String auth = null;
			String validateCms = System.getenv("VALIDATE_CMS");
			if (CommonUtility.validateString(validateCms).length() > 0) {
				System.out.println("Checking Refere from session");
				String referer = (String) session.getAttribute("refererHost");
				if(request.getHeader("Referer")!=null){			
					System.out.println("Host : " + referer);
					if(CMSUtility.getCmsAuthData()!=null){
						auth = CMSUtility.getInstance().validateCms(referer);
					}
					if(auth!=null){
						session.setAttribute("cmsRole", auth);
						isValidate = true;
					}else{
						isValidate = false;
					}
				}else{
					isValidate = false;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return isValidate;
	}
	
	public boolean setReferer(){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		boolean isValidate = true;
		System.out.println("Inside Validate CMS");
		try{
			String auth = null;
			String validateCms = System.getenv("VALIDATE_CMS");
			if (CommonUtility.validateString(validateCms).length() > 0) {
				System.out.println("Checking Refere");
				if(request.getHeader("Referer")!=null){
					
					URL url1 = new URL(request.getHeader("Referer"));
					System.out.println("Host : " + url1.getHost());
					session.setAttribute("refererHost", url1.getHost());
					
				}else{
					isValidate = false;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return isValidate;
	}
	
	public String bandDetailList() {
		try {
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			BrandListModel brandListModel = null;
			PromotionRequest searchObj = null;
			PromotionRequest searchObjNew = null;
			String httpMethod = HttpMethod.GET;
			page = page + 1;
			String url = context+brandPath+list;
			
			StringBuilder requestString = new StringBuilder();
			requestString.append(url).append("?");
			requestString.append("page=").append(page).append("&");
			requestString.append("pageSize=").append(pageSize).append("&");
			requestString.append("status=Y").append("&");
			if(query == null){
				if(CommonUtility.validateString(type).equalsIgnoreCase("")){
					type = "ALL";
				}
			}
			
			if(!CommonUtility.validateString(query).equalsIgnoreCase("")){
    			if(CommonUtility.validateString(searchColumnType).equalsIgnoreCase("")){
    				searchColumnType = "brandName";
    			}
    			searchObj = CMSUtility.getInstance().buildSearchRequest(query, page, pageSize, searchColumnType,"Y", "");//"brandName"
    			httpMethod = HttpMethod.POST;
    			requestString = new StringBuilder();
    			requestString.append(url);
    		}
			if(!CommonUtility.validateString(type).equalsIgnoreCase("")){
				if(searchObj == null) {
    				searchObj = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "brandName","Y", "");
    			}else{
    				searchObjNew = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "brandName","Y", "");
    				if(searchObjNew!=null) {
    					ArrayList<Criteria> criteriaListNew = (ArrayList<Criteria>) searchObjNew.getCriteria();
    					if(criteriaListNew!=null && !criteriaListNew.isEmpty()) {
    						Criteria criteriaModelOld = criteriaListNew.get(0);
    						ArrayList<Criteria> criteriaListOld = (ArrayList<Criteria>) searchObj.getCriteria();
            			    criteriaListOld.add(criteriaModelOld);
            			    searchObj.setCriteria(criteriaListOld);
    					}
    				}
    			}
    			httpMethod = HttpMethod.POST;
    			requestString = new StringBuilder();
    			requestString.append(url);
    		}
			
			// added code for sort and search
    		if(!CommonUtility.validateString(sortEnabled).equalsIgnoreCase("")){
    			if(CommonUtility.validateString(sortColumn).equalsIgnoreCase("")) {
    				sortColumn = "brandName";
    			}
    			searchObjNew = CMSUtility.getInstance().buildSortRequest(Boolean.valueOf(sortEnabled), page, pageSize, sortColumn,"Y");
    			if(searchObj == null) {
    				searchObj = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "brandName","Y", "");
    			}
    			searchObj.setSort(searchObjNew.getSort());
    		}
			
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), httpMethod, searchObj, BrandListModel.class,siteId);
			
			brandListModel = (BrandListModel) response.getData();
			contentObject.put("query", query);
			contentObject.put("type", type);
			contentObject.put("selectedSiteId" , siteId);
			contentObject.put("searchColumnType", searchColumnType);
			contentObject.put("sortColumn" , CommonUtility.validateString(sortColumn));
			contentObject.put("responseType" , "Brand");
			contentObject.put("brandsList" , brandListModel);
			renderContent = CMSLayoutGenerator.templateLoader("CMSResultLoader", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String manufacturerDetailList() {
		try {
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			ManufacturerListModel manufacturerListModel = null;
			PromotionRequest searchObj = null;
			PromotionRequest searchObjNew = null;
			String httpMethod = HttpMethod.GET;
			page = page + 1;
			String url = context+mnufacturerPath+list;
			
			StringBuilder requestString = new StringBuilder();
			requestString.append(url).append("?");
			requestString.append("page=").append(page).append("&");
			requestString.append("pageSize=").append(pageSize).append("&");
			requestString.append("status=Y").append("&");
			if(query == null){
				if(CommonUtility.validateString(type).equalsIgnoreCase("")){
					type = "ALL";
				}
			}
			
			if(!CommonUtility.validateString(query).equalsIgnoreCase("")){
    			if(CommonUtility.validateString(searchColumnType).equalsIgnoreCase("")){
    				searchColumnType = "manufacturerName";
    			}
    			searchObj = CMSUtility.getInstance().buildSearchRequest(query, page, pageSize, searchColumnType,"Y", "");//"brandName"
    			httpMethod = HttpMethod.POST;
    			requestString = new StringBuilder();
    			requestString.append(url);
    		}
			if(!CommonUtility.validateString(type).equalsIgnoreCase("")){
				if(searchObj == null) {
    				searchObj = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "manufacturerName","Y", "");
    			}else{
    				searchObjNew = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "manufacturerName","Y", "");
    				if(searchObjNew!=null) {
    					ArrayList<Criteria> criteriaListNew = (ArrayList<Criteria>) searchObjNew.getCriteria();
    					if(criteriaListNew!=null && !criteriaListNew.isEmpty()) {
    						Criteria criteriaModelOld = criteriaListNew.get(0);
    						ArrayList<Criteria> criteriaListOld = (ArrayList<Criteria>) searchObj.getCriteria();
            			    criteriaListOld.add(criteriaModelOld);
            			    searchObj.setCriteria(criteriaListOld);
    					}
    				}
    			}
    			httpMethod = HttpMethod.POST;
    			requestString = new StringBuilder();
    			requestString.append(url);
    		}
			
			// added code for sort and search
    		if(!CommonUtility.validateString(sortEnabled).equalsIgnoreCase("")){
    			if(CommonUtility.validateString(sortColumn).equalsIgnoreCase("")) {
    				sortColumn = "manufacturerName";
    			}
    			searchObjNew = CMSUtility.getInstance().buildSortRequest(Boolean.valueOf(sortEnabled), page, pageSize, sortColumn,"Y");
    			if(searchObj == null) {
    				searchObj = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "manufacturerName","Y", "");
    			}
    			searchObj.setSort(searchObjNew.getSort());
    		}
			
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), httpMethod, searchObj, ManufacturerListModel.class,siteId);
			
			manufacturerListModel = (ManufacturerListModel) response.getData();
			contentObject.put("query", query);
			contentObject.put("type", type);
			contentObject.put("selectedSiteId" , siteId);
			contentObject.put("searchColumnType", searchColumnType);
			contentObject.put("sortColumn" , CommonUtility.validateString(sortColumn));
			contentObject.put("responseType" , "Manufacturer");
			contentObject.put("manufacturersList" , manufacturerListModel);
			renderContent = CMSLayoutGenerator.templateLoader("CMSResultLoader", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String categoryDetailList() {
		try {
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			CategoryListModel categoryListModel = null;
			PromotionRequest searchObj = null;
			PromotionRequest searchObjNew = null;
			String httpMethod = HttpMethod.GET;
			page = page + 1;
			String url = context+taxonomyTreePath+list;
			
			StringBuilder requestString = new StringBuilder();
			if(query == null){
				if(CommonUtility.validateString(type).equalsIgnoreCase("")){
					type = "ALL";
				}
			}
			
			if(!CommonUtility.validateString(query).equalsIgnoreCase("")){
    			if(CommonUtility.validateString(searchColumnType).equalsIgnoreCase("")){
    				searchColumnType = "categoryName";
    			}
    			searchObj = CMSUtility.getInstance().buildSearchRequest(query, page, pageSize, searchColumnType,"Y", "");//"brandName"
    			httpMethod = HttpMethod.POST;
    			requestString = new StringBuilder();
    			requestString.append(url);
    		}
			if(!CommonUtility.validateString(type).equalsIgnoreCase("")){
				if(searchObj == null) {
    				searchObj = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "categoryName","Y", "");
    			}else{
    				searchObjNew = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "categoryName","Y", "");
    				if(searchObjNew!=null) {
    					ArrayList<Criteria> criteriaListNew = (ArrayList<Criteria>) searchObjNew.getCriteria();
    					if(criteriaListNew!=null && !criteriaListNew.isEmpty()) {
    						Criteria criteriaModelOld = criteriaListNew.get(0);
    						ArrayList<Criteria> criteriaListOld = (ArrayList<Criteria>) searchObj.getCriteria();
            			    criteriaListOld.add(criteriaModelOld);
            			    searchObj.setCriteria(criteriaListOld);
    					}
    				}
    			}
    			httpMethod = HttpMethod.POST;
    			requestString = new StringBuilder();
    			requestString.append(url);
    		}
			
			// added code for sort and search
    		if(!CommonUtility.validateString(sortEnabled).equalsIgnoreCase("")){
    			if(CommonUtility.validateString(sortColumn).equalsIgnoreCase("")) {
    				sortColumn = "categoryName";
    			}
    			searchObjNew = CMSUtility.getInstance().buildSortRequest(Boolean.valueOf(sortEnabled), page, pageSize, sortColumn,"Y");
    			if(searchObj == null) {
    				searchObj = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "categoryName","Y", "");
    			}
    			searchObj.setSort(searchObjNew.getSort());
    		}
			
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), httpMethod, searchObj, CategoryListModel.class,siteId);
			
			categoryListModel = (CategoryListModel) response.getData();
			contentObject.put("query", query);
			contentObject.put("type", type);
			contentObject.put("selectedSiteId" , siteId);
			contentObject.put("searchColumnType", searchColumnType);
			contentObject.put("sortColumn" , CommonUtility.validateString(sortColumn));
			contentObject.put("responseType" , "Category");
			contentObject.put("categoryList" , categoryListModel);
			renderContent = CMSLayoutGenerator.templateLoader("CMSResultLoader", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String taxonomyDetailList() {
		try {
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			TaxonomyListModel taxonomyListModel = null;
			PromotionRequest searchObj = null;
			PromotionRequest searchObjNew = null;
			String httpMethod = HttpMethod.GET;
			page = page + 1;
			String url = context+taxonomy+list;
			
			StringBuilder requestString = new StringBuilder();
			if(query == null){
				if(CommonUtility.validateString(type).equalsIgnoreCase("")){
					type = "ALL";
				}
			}
			
			if(!CommonUtility.validateString(query).equalsIgnoreCase("")){
    			if(CommonUtility.validateString(searchColumnType).equalsIgnoreCase("")){
    				searchColumnType = "name";
    			}
    			searchObj = CMSUtility.getInstance().buildSearchRequest(query, page, pageSize, searchColumnType,"", "");//"brandName"
    			httpMethod = HttpMethod.POST;
    			requestString = new StringBuilder();
    			requestString.append(url);
    		}
			if(!CommonUtility.validateString(type).equalsIgnoreCase("")){
				if(searchObj == null) {
    				searchObj = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "name","", "");
    			}else{
    				searchObjNew = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "name","", "");
    				if(searchObjNew!=null) {
    					ArrayList<Criteria> criteriaListNew = (ArrayList<Criteria>) searchObjNew.getCriteria();
    					if(criteriaListNew!=null && !criteriaListNew.isEmpty()) {
    						Criteria criteriaModelOld = criteriaListNew.get(0);
    						ArrayList<Criteria> criteriaListOld = (ArrayList<Criteria>) searchObj.getCriteria();
            			    criteriaListOld.add(criteriaModelOld);
            			    searchObj.setCriteria(criteriaListOld);
    					}
    				}
    			}
    			httpMethod = HttpMethod.POST;
    			requestString = new StringBuilder();
    			requestString.append(url);
    		}
			
			// added code for sort and search
    		if(!CommonUtility.validateString(sortEnabled).equalsIgnoreCase("")){
    			if(CommonUtility.validateString(sortColumn).equalsIgnoreCase("")) {
    				sortColumn = "categoryName";
    			}
    			searchObjNew = CMSUtility.getInstance().buildSortRequest(Boolean.valueOf(sortEnabled), page, pageSize, sortColumn,"Y");
    			if(searchObj == null) {
    				searchObj = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "name","Y", "");
    			}
    			searchObj.setSort(searchObjNew.getSort());
    		}
			
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), httpMethod, searchObj, TaxonomyListModel.class,siteId);
			
			taxonomyListModel = (TaxonomyListModel) response.getData();
			contentObject.put("query", query);
			contentObject.put("type", type);
			contentObject.put("selectedSiteId" , siteId);
			contentObject.put("searchColumnType", searchColumnType);
			contentObject.put("sortColumn" , CommonUtility.validateString(sortColumn));
			contentObject.put("responseType" , "Taxonomy");
			contentObject.put("taxonomyListModel" , taxonomyListModel);
			
			//renderContent = CMSLayoutGenerator.templateLoader("CMSResultLoader", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String setHomePage(){
		try{
			request =ServletActionContext.getRequest();
			String dlink = request.getParameter("dlink");
			CmsDao cmsDao = new CmsDao();
			List<WebsiteModel> websiteList =  getWebsiteList();
			if(CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId))>0) {
				siteId = CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId));
			}else if(siteId == 0 && websiteList!=null && websiteList.size()>0){
    			for (WebsiteModel websiteModel : websiteList){
					if(CommonUtility.validateString(websiteModel.getPrimarySite()).equalsIgnoreCase("Y")) {
						siteId = websiteModel.getSiteId();
						break;
					}
				}
    		} 
			if(CommonDBQuery.getGlobalSiteId()>0 && siteId == 0){
				siteId = CommonDBQuery.getGlobalSiteId();
    		}
			if(pageId > 0){
				int userId = 1;
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_LEVEL_PAGE_ID")).trim().equalsIgnoreCase("Y")){
					int id = cmsDao.setHomePage(pageId, siteId,dlink);
				}else{
					CMSUtility.getInstance().setHomePage(pageId, userId, siteId,dlink);
				}
				
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_PREBUILT_CMS_HOMEPAGE")).equalsIgnoreCase("Y")){
					System.out.println("Initiate - write to CMS html file at setHomePage");
					CommonDBQuery.copyCMSPageContentToFile(CommonUtility.validateParseIntegerToString(pageId));
					CommonDBQuery.getSystemParamtersList().put("CMS_HOMEPAGE_ID", CommonUtility.validateParseIntegerToString(pageId));
					System.out.println("Completed - write to CMS html file at setHomePage");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return SUCCESS;
	}
	public String listStaticPage(){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try{
			String referer = (String) session.getAttribute("refererHost");
			if(referer==null){
				setReferer();
			}
			if(!validateCms()){
				return "unauthorized";
			}
			PromotionRequest searchObj = null;
			PromotionRequest searchObjNew = null;
			CmsDao cmsDao = new CmsDao();
			String httpMethod = HttpMethod.GET;
			page = page + 1;
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			StaticPageList staticPageList = new StaticPageList();
			StringBuilder requestString = new StringBuilder();
			String url = context+staticPage+list;
			requestString.append(url).append("?");
			requestString.append("page=").append(page).append("&");
			requestString.append("pageSize=").append(pageSize).append("&");
			requestString.append("status=A").append("&");
			if(query == null){
				if(CommonUtility.validateString(type).equalsIgnoreCase("")){
					type = "ALL";
				}
			}
			String originalQueryString = CommonUtility.validateString(query);
			
			List<WebsiteModel> websiteList =  getWebsiteList();
			if(CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId))>0) {
				siteId = CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId));
			}else if(CommonDBQuery.getGlobalSiteId() > 0){
				siteId = CommonDBQuery.getGlobalSiteId();
			}else if(siteId == 0 && websiteList!=null && websiteList.size()>0){
    			for (WebsiteModel websiteModel : websiteList){
					if(CommonUtility.validateString(websiteModel.getPrimarySite()).equalsIgnoreCase("Y")) {
						siteId = websiteModel.getSiteId();
						break;
					}
				}
    		} 
			if(CommonDBQuery.getGlobalSiteId()>0 && siteId == 0){
				siteId = CommonDBQuery.getGlobalSiteId();
    		}
			int homePageId=0;
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_LEVEL_PAGE_ID")).trim().equalsIgnoreCase("Y")){
			if(siteId>0) {
				homePageId = cmsDao.setHomePageIcon(siteId);
			}
			}
    		if(!CommonUtility.validateString(query).equalsIgnoreCase("")){
    			
    			if(CommonUtility.validateString(searchColumnType).equalsIgnoreCase("")){
    				searchColumnType = "pageName";
    			}
    			if(CommonUtility.validateString(searchColumnType).equalsIgnoreCase("pageTitle")){
    				query = CommonUtility.validateString(query).replaceAll("-", " ");
    			}
    			query = CommonUtility.validateString(query).replaceAll("\\/", "");
    			searchObj = CMSUtility.getInstance().buildSearchRequest(query, page, pageSize, searchColumnType,"", "");//"pageName"
    			httpMethod = HttpMethod.POST;
    			requestString = new StringBuilder();
    			requestString.append(url);
    		}
    		if(!CommonUtility.validateString(type).equalsIgnoreCase("")){
				if(searchObj == null) {
    				searchObj = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "pageType","", "");
    			}else{
    				searchObjNew = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "pageType","", "");
    				if(searchObjNew!=null) {
    					ArrayList<Criteria> criteriaListNew = (ArrayList<Criteria>) searchObjNew.getCriteria();
    					if(criteriaListNew!=null && !criteriaListNew.isEmpty()) {
    						Criteria criteriaModelOld = criteriaListNew.get(0);
    						ArrayList<Criteria> criteriaListOld = (ArrayList<Criteria>) searchObj.getCriteria();
            			    criteriaListOld.add(criteriaModelOld);
            			    searchObj.setCriteria(criteriaListOld);
    					}
    				}
    			}
    			httpMethod = HttpMethod.POST;
    			requestString = new StringBuilder();
    			requestString.append(url);
    		}
    		
    		// added code for sort and search
    		if(!CommonUtility.validateString(sortEnabled).equalsIgnoreCase("")){
    			if(CommonUtility.validateString(sortColumn).equalsIgnoreCase("")) {
    				sortColumn = "pageName";
    			}
    			searchObjNew = CMSUtility.getInstance().buildSortRequest(Boolean.valueOf(sortEnabled), page, pageSize, sortColumn,"");
    			if(searchObj == null) {
    				searchObj = CMSUtility.getInstance().buildSearchRequest(type, page, pageSize, "pageType","", "");
    			}
    			searchObj.setSort(searchObjNew.getSort());
    		}
    		
    		
			//requestString.append("siteId=").append(siteId);
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), httpMethod, searchObj, StaticPageList.class,siteId);
			staticPageList = (StaticPageList) response.getData();
			//contentObject.put("query", query);
			contentObject.put("query", originalQueryString);
			contentObject.put("type", type);
			contentObject.put("searchColumnType", searchColumnType);
			contentObject.put("staticPageList" , staticPageList);
			contentObject.put("selectedSiteId" , siteId);
			contentObject.put("websiteList" , websiteList);
			contentObject.put("homePageId" , homePageId);
			contentObject.put("sortEnabled" , sortEnabled);
			if(websiteList!=null && websiteList.size()>0) {
    			for (WebsiteModel websiteModel : websiteList) {
					if(websiteModel.getSiteId()==siteId) {
						contentObject.put("selectedSiteName" , CommonUtility.validateString(websiteModel.getSiteName()));
						break;
					}
				}
    		}
			renderContent = CMSLayoutGenerator.templateLoader("CMSStaticPageList", contentObject, null, null, null);
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String cmsStaticPage(){
		try{
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			if(!validateCms()){
				return "unauthorized";
			}else if(pageId ==0){
				String cmsRole = (String) session.getAttribute("cmsRole");
				if(cmsRole!=null && cmsRole.trim().equalsIgnoreCase("CLIENT")){
					return "unauthorized";
				}
			}
			
			StringBuilder requestString = new StringBuilder();
			requestString.append("/admin/userLibraries/findAll").append("?");
			//requestString.append("/admin/cimmUserLibrary/findAll").append("?");
			requestString.append("page=").append(page).append("&");
			requestString.append("pageSize=").append(pageSize);
			
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			CmsDao cmsDao = new CmsDao();
			List<LocaleModel> ldata=cmsDao.getLoacleList();
			StaticPageData staticPageData = new StaticPageData();
			
			List<WebsiteModel> websiteList =  getWebsiteList();
			if(CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId))>0) {
				siteId = CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId));
			}else if(CommonDBQuery.getGlobalSiteId() > 0){
				siteId = CommonDBQuery.getGlobalSiteId();
			}else if(siteId == 0 && websiteList!=null && websiteList.size()>0){
    			for (WebsiteModel websiteModel : websiteList){
					if(CommonUtility.validateString(websiteModel.getPrimarySite()).equalsIgnoreCase("Y")) {
						siteId = websiteModel.getSiteId();
						break;
					}
				}
    		} 
			if(CommonDBQuery.getGlobalSiteId()>0 && siteId == 0){
				siteId = CommonDBQuery.getGlobalSiteId();
    		}
			if(pageId>0){
				String url = context+staticPage+find+pageId;
				Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(url, HttpMethod.GET, null, StaticPageData.class,siteId);
				staticPageData = (StaticPageData) response.getData();
				if(staticPageData!=null){
					if(CommonUtility.validateString(staticPageData.getPageType()).equalsIgnoreCase("BRAND")) {
						
						BrandListModel brandListModel = null;
						PromotionIntRequest searchObjBrand = null;
						page = page + 1;
						String urlBrand = context+brandPath+list;
						StringBuilder requestStringBrand = new StringBuilder();
						searchObjBrand = CMSUtility.getInstance().buildIntegerSearchRequest(pageId, page, pageSize, "staticPageId","Y","EQUAL");
						String httpMethod = HttpMethod.POST;
			    		requestStringBrand = new StringBuilder();
			    		requestStringBrand.append(urlBrand);
						
						response = Cimm2BCentralClient.getInstance().getDataObject(requestStringBrand.toString(), httpMethod, searchObjBrand, BrandListModel.class,0);
						brandListModel = (BrandListModel) response.getData();
						if(brandListModel!=null && brandListModel.getResultSet()!=null && brandListModel.getResultSet().size()>0){
							BrandDataModel brandDataModel = (BrandDataModel) brandListModel.getResultSet().get(0);
							contentObject.put("selectedPageTypeValueId" , brandDataModel.getId());
							contentObject.put("selectedPageTypeValueName" , brandDataModel.getBrandName());
						}
						
					}else if(CommonUtility.validateString(staticPageData.getPageType()).equalsIgnoreCase("MANUFACTURER")) {
						
						ManufacturerListModel manufacturerListModel = null;
						PromotionIntRequest searchObjManufacturer = null;
						page = page + 1;
						String urlManufacturer = context+mnufacturerPath+list;
						StringBuilder requestStringBrand = new StringBuilder();
						searchObjManufacturer = CMSUtility.getInstance().buildIntegerSearchRequest(pageId, page, pageSize, "staticPageId","Y","EQUAL");
						String httpMethod = HttpMethod.POST;
			    		requestStringBrand = new StringBuilder();
			    		requestStringBrand.append(urlManufacturer);
						
						response = Cimm2BCentralClient.getInstance().getDataObject(requestStringBrand.toString(), httpMethod, searchObjManufacturer, ManufacturerListModel.class,0);
						manufacturerListModel = (ManufacturerListModel) response.getData();
						if(manufacturerListModel!=null && manufacturerListModel.getResultSet()!=null && manufacturerListModel.getResultSet().size()>0){
							ManufacturerDataModel manufacturerDataModel = (ManufacturerDataModel) manufacturerListModel.getResultSet().get(0);
							contentObject.put("selectedPageTypeValueId" , manufacturerDataModel.getId());
							contentObject.put("selectedPageTypeValueName" , manufacturerDataModel.getManufacturerName());
						}
						
					}else if(CommonUtility.validateString(staticPageData.getPageType()).equalsIgnoreCase("CATEGORY")) {
						
						CategoryListModel categoryListModel = null;
						PromotionIntRequest searchObjCategory = null;
						page = page + 1;
						String urlCategory = context+taxonomyTreePath+list;
						StringBuilder requestStringCategory = new StringBuilder();
						searchObjCategory = CMSUtility.getInstance().buildIntegerSearchRequest(pageId, page, pageSize, "staticPageId","Y","EQUAL");
						String httpMethod = HttpMethod.POST;
						requestStringCategory = new StringBuilder();
						requestStringCategory.append(urlCategory);
						
						response = Cimm2BCentralClient.getInstance().getDataObject(requestStringCategory.toString(), httpMethod, searchObjCategory, CategoryListModel.class,0);
						categoryListModel = (CategoryListModel) response.getData();
						if(categoryListModel!=null && categoryListModel.getResultSet()!=null && categoryListModel.getResultSet().size()>0){
							CategoryDataModel categoryDataModel = (CategoryDataModel) categoryListModel.getResultSet().get(0);
							contentObject.put("selectedPageTypeValueId" , categoryDataModel.getId());
							contentObject.put("selectedPageTypeValueName" , categoryDataModel.getCategoryName());
						}
						
					}
				}
			}else{
				staticPageData.setSiteId(siteId);
			}
		
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), HttpMethod.GET, null, LibraryList.class,0);
			LibraryList libraryList = (LibraryList) response.getData();
			loadAllDynamicTemplates();
			contentObject.put("libraryList" , libraryList);
			contentObject.put("staticPageData" , staticPageData);
			contentObject.put("localeListData" , ldata);
			contentObject.put("websiteList" , websiteList);
			contentObject.put("selectedSiteId" , siteId);
			contentObject.put("templateNames" , templateNames);
			renderContent = CMSLayoutGenerator.templateLoader("CMSStaticPage", contentObject, null, null, null);
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	public String addUpdateStaticPage(){
		
		try{
			if(!validateCms()){
				return "unauthorized";
			}
			request =ServletActionContext.getRequest();
			String pageContent = request.getParameter("pageContent");
			String fullPageLayout = request.getParameter("fullPageLayout");
			StaticPageData staticPageData = new StaticPageData();
			System.out.println("Edited USER ID: -->"+pageData.getUserEdited());
			System.out.println("PageContent: -->"+pageData.getPageContent());
			System.out.println(pageContent);
			String addEdit = "Add";
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			//StaticPageData staticPageData = new StaticPageData();
			
			CmsDao cmsDao = new CmsDao();
			List<WebsiteModel> websiteList =  getWebsiteList();
			if(CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId))>0) {
				siteId = CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId));
			}else if(CommonDBQuery.getGlobalSiteId() > 0){
				siteId = CommonDBQuery.getGlobalSiteId();
			}else if(siteId == 0 && websiteList!=null && websiteList.size()>0){
    			for (WebsiteModel websiteModel : websiteList){
					if(CommonUtility.validateString(websiteModel.getPrimarySite()).equalsIgnoreCase("Y")) {
						siteId = websiteModel.getSiteId();
						break;
					}
				}
    		} 
			if(CommonDBQuery.getGlobalSiteId()>0 && siteId == 0){
				siteId = CommonDBQuery.getGlobalSiteId();
    		}
			StringBuilder requestString = new StringBuilder();
			String url = context+staticPage+"/create";
			
			System.out.println(staticPageData);
			Gson gson = new Gson();
			String httpMethod = HttpMethod.POST;
			
			if(pageData!=null && CommonUtility.validateString(pageData.getCustomUrl()).length()>0) {
				String pattern = "[^A-Za-z0-9\\/]";
				String customUrl = CommonUtility.validateString(pageData.getCustomUrl()).replaceAll(pattern," ");
				customUrl = customUrl.replaceAll("\\s+","-");
				customUrl = customUrl.replaceAll("--","-");
				pageData.setCustomUrl(CommonUtility.validateString(customUrl).toLowerCase());
			}
			
		
			if(!pageData.isClonePage()){
				if(pageData.getId() > 0){
					
					if(pageData.getStatus() == null){
						pageData.setStatus("N");
					}
					
					if(type!=null && type.equalsIgnoreCase("delete")){
						httpMethod = HttpMethod.DELETE;
						url = context+staticPage+"/delete?id="+pageData.getId();
					}else{
						httpMethod = HttpMethod.PUT;
						url = context+staticPage+"/update";
					}
					addEdit = "EditStaticPage";
				}
			}else{
				pageData.setId(0);
			}
			if(fullPageLayout!=null){
				pageData.setFullPageLayout(true);
			}
			
			if(pageData!=null) {
				String removedNbsp = CommonUtility.validateString(pageData.getPageContent()).replaceAll("\\<.*?>","").replaceAll("&nbsp;", "").replaceAll("&Ecirc;", "");
				pageData.setPlainText(removedNbsp);
			}
			
			
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(url, httpMethod, pageData, StaticPageData.class,siteId);
			if(response!=null && response.getData()!=null) {
				staticPageData = (StaticPageData) response.getData();
			}
			contentObject.put("staticPageData" , staticPageData);
			if(response!=null && response.getStatus().getCode()==200){
				
				String staticPagesPath=CommonDBQuery.getSystemParamtersList().get("STATICPAGESPATH");
				CmsDao.generateXMLforStaticPage(staticPagesPath, staticPageData, addEdit);
				if (pageData.getPageType().equalsIgnoreCase("TEMPLATE")) {
					try{
							String virtualFolderPath = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH"));
							String warFileName = CMSConstants.getWarFileName(CommonDBQuery.getSystemParamtersList().get("STATIC_PAGE_TEMPLATE_LIST"));
							String folderPath = virtualFolderPath + warFileName;
							
							String warFileNameForImage = CMSConstants.getWarFileName(CommonDBQuery.getSystemParamtersList().get("STATIC_PAGE_TEMPLATE_IMAGE_LIST"));
							String imageFolderName = virtualFolderPath + warFileNameForImage;
							
							System.out.println("VIRTUALFOLDERPATH-----"+virtualFolderPath);
							System.out.println("STATIC_PAGE_TEMPLATE_LIST---"+warFileName);
							System.out.println("STATIC_PAGE_TEMPLATE_IMAGE_LIST"+warFileNameForImage);
								
							FileWriter fileWriter = null;
							boolean createFile = false;
								try{
								
									if(type!=null && type.equalsIgnoreCase("delete")){
										System.out.println("Folder path is " + imageFolderName);
										File file = new File(folderPath+ pageData.getPageName() + ".html");
										file.delete();
									}else { 
										System.out.println("Folder path is " + imageFolderName);
										File file = new File(folderPath+ pageData.getPageName() + ".html");
										createFile = file.createNewFile();
										
										if(createFile){
											fileWriter = new FileWriter(file);
											System.out.println(file);
											fileWriter.write(pageData.getPageContent());
											fileWriter.close();
											File f2 = null;
											int imageSize = 50;
											BufferedImage img = null;
											BufferedImage convertedImg = null;
											String imageData = "";
											imageData = request.getParameter("tempImgVal");
						
											System.out.println("Image Data is " + imageData);
											imageData = imageData.substring(22);
											byte[] imgByteArray = Base64.decodeBase64(imageData
													.getBytes());
											InputStream in = new ByteArrayInputStream(imgByteArray);
											f2 = new File(imageFolderName, pageData.getPageName()
													+ ".jpg");
											img = ImageIO.read(in); // load image
											convertedImg = Scalr.resize(img, Method.ULTRA_QUALITY,Mode.AUTOMATIC, imageSize, imageSize,	Scalr.OP_ANTIALIAS);
											ImageIO.write(convertedImg, "png", f2);
											img.flush();
										}
									}
								}catch (IOException ex) {
									ex.printStackTrace();
						        } finally {
						        	if(fileWriter!=null) {
						        		try {
							                fileWriter.close();
							              } catch (Exception ex) {
							               ex.printStackTrace();
							            }
						        	}
						        }	
						}catch(Exception e){
								e.printStackTrace();
						}
					
				}if (pageData.getPageType().equalsIgnoreCase("BRAND") && pageData.getBrandId()>0) {
					url = context+brandPath+"/update";
					StaticPageUpdateModel staticPageUpdateModel = new StaticPageUpdateModel();
					staticPageUpdateModel.setId(pageData.getBrandId());
					staticPageUpdateModel.setStaticPageId(pageData.getId());
					response = Cimm2BCentralClient.getInstance().getDataObject(url, httpMethod, staticPageUpdateModel, BrandDataModel.class,siteId);
										
				}if (pageData.getPageType().equalsIgnoreCase("MANUFACTURER") && pageData.getManufacturerId()>0) {
					url = context+mnufacturerPath+"/update";
					StaticPageUpdateModel staticPageUpdateModel = new StaticPageUpdateModel();
					staticPageUpdateModel.setId(pageData.getManufacturerId());
					staticPageUpdateModel.setStaticPageId(pageData.getId());
					response = Cimm2BCentralClient.getInstance().getDataObject(url, httpMethod, staticPageUpdateModel, ManufacturerDataModel.class,siteId);
					
				}if (pageData.getPageType().equalsIgnoreCase("CATEGORY") && pageData.getTaxonomyTreeId()>0) {
					url = context+taxonomyTreePath+"/update";
					StaticPageUpdateModel staticPageUpdateModel = new StaticPageUpdateModel();
					staticPageUpdateModel.setId(pageData.getTaxonomyTreeId());
					staticPageUpdateModel.setStaticPageId(pageData.getId());
					response = Cimm2BCentralClient.getInstance().getDataObject(url, httpMethod, staticPageUpdateModel, CategoryDataModel.class,siteId);
				}

				if(pageData!=null && pageData.getId()>0) {
					
					//--------------------------------------------------------------------------
					//Brand Update
						BrandListModel brandListModel = null;
						PromotionIntRequest searchObjBrand = null;
						String urlBrand = context+brandPath+list;
						searchObjBrand = CMSUtility.getInstance().buildIntegerSearchRequest(pageData.getId(), page, pageSize, "staticPageId","Y","EQUAL");
						if(pageData.getBrandId()>0) {
							PromotionIntRequest searchObjBrandTwo = null;
							searchObjBrandTwo = CMSUtility.getInstance().buildIntegerSearchRequest(pageData.getBrandId() , page, pageSize, "id","Y","NOT_EQUAL");
							if(searchObjBrandTwo!=null) {
								List<CriteriaIntValue> criteriaTwo = searchObjBrandTwo.getCriteria();
								if(criteriaTwo!=null && !criteriaTwo.isEmpty()) {
									CriteriaIntValue criteriaIntValueTwo = criteriaTwo.get(0);
									searchObjBrand.getCriteria().add(criteriaIntValueTwo);
								}
							}
						}
			    		httpMethod = HttpMethod.POST;
			    		StringBuilder requestStringBrand = new StringBuilder();
			    		requestStringBrand.append(urlBrand);
						
						response = Cimm2BCentralClient.getInstance().getDataObject(requestStringBrand.toString(), httpMethod, searchObjBrand, BrandListModel.class,0);
						brandListModel = (BrandListModel) response.getData();
						if(brandListModel!=null && brandListModel.getResultSet()!=null && brandListModel.getResultSet().size()>0){
							for(BrandDataModel brandDataModel : brandListModel.getResultSet()) {
								if(brandDataModel.getId()>0) {
									String urlBrandUpdate = context+brandPath+"/update";
									String httpMethodPut = HttpMethod.PUT;
									BrandDataModel brandDataModelUpdate = new BrandDataModel();
									brandDataModelUpdate.setId(brandDataModel.getId());
									brandDataModelUpdate.setStaticPageId(0);
									response = Cimm2BCentralClient.getInstance().getDataObject(urlBrandUpdate, httpMethodPut, brandDataModelUpdate, BrandDataModel.class,siteId);
								}
							}
						}
					//Brand Update
					
					//--------------------------------------------------------------------------
						
					//Manufacturer Update
					ManufacturerListModel manufacturerListModel = null;
					PromotionIntRequest searchObjManufacturer = null;
					String urlManufacturer = context+mnufacturerPath+list;
					searchObjManufacturer = CMSUtility.getInstance().buildIntegerSearchRequest(pageData.getId(), page, pageSize, "staticPageId","Y","EQUAL");
					if(pageData.getManufacturerId()>0) {
						PromotionIntRequest searchObjMfrTwo = null;
						searchObjMfrTwo = CMSUtility.getInstance().buildIntegerSearchRequest(pageData.getManufacturerId(), page, pageSize, "id","Y","NOT_EQUAL");
						if(searchObjMfrTwo!=null) {
							List<CriteriaIntValue> criteriaTwo = searchObjMfrTwo.getCriteria();
							if(criteriaTwo!=null && !criteriaTwo.isEmpty()) {
								CriteriaIntValue criteriaIntValueTwo = criteriaTwo.get(0);
								searchObjManufacturer.getCriteria().add(criteriaIntValueTwo);
							}
						}
					}
					httpMethod = HttpMethod.POST;
					StringBuilder requestStringManufacturer = new StringBuilder();
		    		requestStringManufacturer.append(urlManufacturer);
					
					response = Cimm2BCentralClient.getInstance().getDataObject(requestStringManufacturer.toString(), httpMethod, searchObjManufacturer, ManufacturerListModel.class,0);
					manufacturerListModel = (ManufacturerListModel) response.getData();
					if(manufacturerListModel!=null && manufacturerListModel.getResultSet()!=null && manufacturerListModel.getResultSet().size()>0){
						
						for( ManufacturerDataModel mamufacturerDataModel : manufacturerListModel.getResultSet()) {
							if(mamufacturerDataModel.getId()>0) {
								String manufacturerUpdateUrl = context+mnufacturerPath+"/update";
								String httpMethodPut = HttpMethod.PUT;
								ManufacturerDataModel mfrDataModel = new ManufacturerDataModel();
								mfrDataModel.setId(mamufacturerDataModel.getId());
								mfrDataModel.setStaticPageId(0);
								response = Cimm2BCentralClient.getInstance().getDataObject(manufacturerUpdateUrl, httpMethodPut, mfrDataModel, ManufacturerDataModel.class,siteId);
								
							}
						}
					}
					//Manufacturer Update
					
					//--------------------------------------------------------------------------
					
					
					//Category Update
					CategoryListModel categoryListModel = null;
					PromotionIntRequest searchObjCategory = null;
					String urlCategory = context+taxonomyTreePath+list;
					searchObjCategory = CMSUtility.getInstance().buildIntegerSearchRequest(pageData.getId(), page, pageSize, "staticPageId","Y","EQUAL");
					if(pageData.getTaxonomyTreeId()>0) {
						PromotionIntRequest searchObjMfrTwo = null;
						searchObjMfrTwo = CMSUtility.getInstance().buildIntegerSearchRequest(pageData.getTaxonomyTreeId(), page, pageSize, "id","Y","NOT_EQUAL");
						if(searchObjMfrTwo!=null) {
							List<CriteriaIntValue> criteriaTwo = searchObjMfrTwo.getCriteria();
							if(criteriaTwo!=null && !criteriaTwo.isEmpty()) {
								CriteriaIntValue criteriaIntValueTwo = criteriaTwo.get(0);
								searchObjCategory.getCriteria().add(criteriaIntValueTwo);
							}
						}
					}
					httpMethod = HttpMethod.POST;
					StringBuilder requestStringCategory = new StringBuilder();
		    		requestStringCategory.append(urlCategory);
					
					response = Cimm2BCentralClient.getInstance().getDataObject(requestStringCategory.toString(), httpMethod, searchObjCategory, CategoryListModel.class,0);
					categoryListModel = (CategoryListModel) response.getData();
					if(categoryListModel!=null && categoryListModel.getResultSet()!=null && categoryListModel.getResultSet().size()>0){
						
						for( CategoryDataModel categoryDataModel : categoryListModel.getResultSet()) {
							if(categoryDataModel.getId()>0) {
								String categoryUpdateUrl = context+taxonomyTreePath+"/update";
								String httpMethodPut = HttpMethod.PUT;
								StaticPageUpdateModel staticPageUpdateModel = new StaticPageUpdateModel();
								staticPageUpdateModel.setId(categoryDataModel.getId());
								staticPageUpdateModel.setStaticPageId(0);
								response = Cimm2BCentralClient.getInstance().getDataObject(categoryUpdateUrl, httpMethodPut, staticPageUpdateModel, CategoryDataModel.class,siteId);
								
							}
						}
					}
					//Category Update
					
					//--------------------------------------------------------------------------
				}
				
				renderContent = ""+staticPageData.getId();
				if(staticPageData!=null && staticPageData.getId()>0 && pageData.isHomePageIcon() && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_PREBUILT_CMS_HOMEPAGE")).equalsIgnoreCase("Y")) {
					System.out.println("Initiate - write to CMS html file");
					CommonDBQuery.copyCMSPageContentToFile(renderContent);
					System.out.println("Completed - write to CMS html file");
				}
			}else{
				renderContent ="error|" + response.getStatus().getMessage();
			}
			//renderContent = CMSLayoutGenerator.templateLoader("CMSStaticPage", contentObject, null, null, null);
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return SUCCESS;
	}

public String bannerList(){
	
	BannerList bannerList = null;
	PromotionRequest searchObj = null;
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		String httpMethod = HttpMethod.GET;
		request =ServletActionContext.getRequest();
		String requestType = request.getParameter("requestType");
		CmsDao cmsDao = new CmsDao();
		List<WebsiteModel> websiteList =  getWebsiteList();
		if(CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId))>0) {
			siteId = CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId));
		}else if(CommonDBQuery.getGlobalSiteId() > 0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}else if(siteId == 0 && websiteList!=null && websiteList.size()>0){
			for (WebsiteModel websiteModel : websiteList){
				if(CommonUtility.validateString(websiteModel.getPrimarySite()).equalsIgnoreCase("Y")) {
					siteId = websiteModel.getSiteId();
					break;
				}
			}
		} 
		if(CommonDBQuery.getGlobalSiteId()>0 && siteId == 0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}
		page = page + 1;
		
		if(pageSize==0){
			pageSize = 10;
		}
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		StringBuilder requestString = new StringBuilder();
		String url = context+"/bannerList"+list;
		requestString.append(url).append("?");
		requestString.append("page=").append(page).append("&");
		requestString.append("pageSize=").append(pageSize);//.append("&");
		//requestString.append("siteId=").append(siteId);
		requestString.append("&status=A");
		if(!CommonUtility.validateString(query).equalsIgnoreCase("")){
			searchObj = CMSUtility.getInstance().buildSearchRequest(query, page, pageSize, "bannerListName","A", "");
			httpMethod = HttpMethod.POST;
			requestString = new StringBuilder();
			requestString.append(url);
		}
		Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), httpMethod, searchObj, BannerList.class,siteId);
		bannerList = (BannerList) response.getData();
		contentObject.put("query" , query);
		contentObject.put("bannerList" , bannerList);
		if(requestType!=null && requestType.trim().equalsIgnoreCase("ajax")){
			renderContent = CMSLayoutGenerator.templateLoader("CMSBannerListAjax", contentObject, null, null, null);
		}else{		
			renderContent = CMSLayoutGenerator.templateLoader("CMSBannerList", contentObject, null, null, null);
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	
	return SUCCESS;
	
}


public String bannerImage(){
	
	BannerImageList bannerImageList = null;
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		page = page + 1;
		
		if(pageSize==0){
			pageSize = 10;
		}
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		StringBuilder requestString = new StringBuilder();
		String url = context+"/banner"+list;
		requestString.append(url).append("?");
		requestString.append("page=").append(page).append("&");
		requestString.append("pageSize=").append(pageSize).append("&");
		requestString.append("status=A");
		//requestString.append("siteId=").append(siteId);
		Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), HttpMethod.GET, null, BannerImageList.class,0);
		bannerImageList = (BannerImageList) response.getData();
		contentObject.put("bannerImageList" , bannerImageList);
		renderContent = CMSLayoutGenerator.templateLoader("CMSBannerImageList", contentObject, null, null, null);
	}catch (Exception e) {
		e.printStackTrace();
	}
	
	return SUCCESS;
	
}


public String bannerDataTemplate() {
	
	String bannerTemplate = null;
	String templateName = "jssortemplate.html";
	LinkedHashMap<String, Object> bannerTemplateObj = new LinkedHashMap<String, Object>();
	String bannerTemplateResponse = "";
	Gson gson = new Gson();
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		if(CommonUtility.validateString(reload).trim().equalsIgnoreCase("")){
			reload = "true";
		}
		CmsDao cmsDao = new CmsDao();
		List<WebsiteModel> websiteList =  getWebsiteList();
		if(CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId))>0) {
			siteId = CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId));
		}else if(CommonDBQuery.getGlobalSiteId() > 0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}else if(siteId == 0 && websiteList!=null && websiteList.size()>0){
			for (WebsiteModel websiteModel : websiteList){
				if(CommonUtility.validateString(websiteModel.getPrimarySite()).equalsIgnoreCase("Y")) {
					siteId = websiteModel.getSiteId();
					break;
				}
			}
		} 
		if(CommonDBQuery.getGlobalSiteId()>0 && siteId == 0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}
		BannerInfoData bannerListData = null;
		StringBuilder requestString = new StringBuilder();
		String url = context+"/bannerList/search/";
		requestString.append(url);
		requestString.append(bannerListId).append("?");
		requestString.append("reload=").append(reload);
		Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), HttpMethod.GET, null, BannerInfoData.class,siteId);
		bannerListData = (BannerInfoData) response.getData();
		LinkedHashMap<String, String> dynamicProperties = bannerListData.getDynamicProperties();
		if(dynamicProperties!=null && dynamicProperties.size()>0){
			templateName = dynamicProperties.get("templateName");
			if(templateName!=null){
				if(templateName.trim().equalsIgnoreCase("default")){
					templateName = dynamicProperties.get("bannerType")+".html";
				}	
			}else{
				templateName = "jssortemplate.html";
			}
		}
		bannerTemplate = BannerTemplate.getInstance().generateBanner(templateName, bannerListData);
		bannerTemplateObj.put("dynamicProperties", dynamicProperties);
		bannerTemplateObj.put("bannerTemplate", bannerTemplate);
		renderContent = gson.toJson(bannerTemplateObj);
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}



public String editBanner() {
	
	BannerInfoData bannerListData = null;
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	request =ServletActionContext.getRequest();
	String isEdit = request.getParameter("isEdit");
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		reload = "true";
		if(CommonUtility.validateString(reload).trim().equalsIgnoreCase("")){
			reload = "false";
		}
		if(bannerListId > 0){
			CmsDao cmsDao = new CmsDao();
			List<WebsiteModel> websiteList =  getWebsiteList();
			if(CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId))>0) {
				siteId = CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId));
			}else if(CommonDBQuery.getGlobalSiteId() > 0){
				siteId = CommonDBQuery.getGlobalSiteId();
			}else if(siteId == 0 && websiteList!=null && websiteList.size()>0){
    			for (WebsiteModel websiteModel : websiteList){
					if(CommonUtility.validateString(websiteModel.getPrimarySite()).equalsIgnoreCase("Y")) {
						siteId = websiteModel.getSiteId();
						break;
					}
				}
    		} 
			if(CommonDBQuery.getGlobalSiteId()>0 && siteId == 0){
				siteId = CommonDBQuery.getGlobalSiteId();
    		}
			StringBuilder requestString = new StringBuilder();
			String url = context+"/bannerList/search/";
			requestString.append(url);
			requestString.append(bannerListId).append("?");
			requestString.append("reload=").append(reload);
			Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), HttpMethod.GET, null, BannerInfoData.class,siteId);
			bannerListData = (BannerInfoData) response.getData();
			contentObject.put("bannerListData" , bannerListData);
		}
		ArrayList<String> templateList = BannerTemplate.getInstance().templateList();
		contentObject.put("isEdit", isEdit);
		contentObject.put("bannerListId", bannerListId);
		contentObject.put("templateList", templateList);
		renderContent = CMSLayoutGenerator.templateLoader("CMSBannerEdit", contentObject, null, null, null);
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}

public String saveBanner(){
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		request =ServletActionContext.getRequest();
		String bannerImageName[] = request.getParameterValues("bannerImageNameNew");
		String bannerIdList[] = request.getParameterValues("bannerIdNew");
		String bannerListName = request.getParameter("bannerListName");
		String captionText  = null;
		String leftPos = null;
		String topPos = null;
		String url = null;
		int userId = 1;
		int captionTopPos = 0;
		int captionLeftPos = 0;
		boolean saveBannerList = true;
		String bannerResponse = "success";
		
		String templateName = request.getParameter("templateName");
		String bannerType = request.getParameter("bannerType");
		LinkedHashMap<String,String> bannerProperties = new LinkedHashMap<String,String>();
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String bannerTransistion = request.getParameter("ssTransition");
		String carouselSetting = request.getParameter("carouselSetting");
		

		if(!CommonUtility.validateString(bannerTransistion).trim().equalsIgnoreCase("")){
			bannerProperties.put("bannerTransistion",bannerTransistion);
		}

		if(!CommonUtility.validateString(carouselSetting).trim().equalsIgnoreCase("")){
			bannerProperties.put("carouselSetting",carouselSetting);
		}


		if(!CommonUtility.validateString(bannerType).trim().equalsIgnoreCase("")){
			bannerProperties.put("bannerType",bannerType);
		}


		if(CommonUtility.validateString(templateName).trim().equalsIgnoreCase("")){
			templateName = "default";
		}

		bannerProperties.put("templateName",templateName);


		
		if(bannerListName==null){
			saveBannerList = false;
			bannerResponse = "Please enter banner list name.";
		}
		System.out.println("bannerImageName ; " + bannerImageName.length);
		if(bannerIdList!=null && bannerIdList.length>0){

			
		}else{
			bannerResponse = "Please select banner image.";
			saveBannerList = false;
		}

		if(saveBannerList){
			BannerInfoData bannerInfoData = new BannerInfoData();
			BannerInfo bannerInfo = null;
			bannerInfoData.setBannerListName(bannerListName);
			if(!CommonUtility.validateString(startDate).equalsIgnoreCase("")){
				bannerInfoData.setStartDate(startDate);
			}
			if(!CommonUtility.validateString(endDate).equalsIgnoreCase("")){
				bannerInfoData.setExpiryDate(endDate);
			}
			CmsDao cmsDao = new CmsDao();
			int result = cmsDao.saveBannerListInfo(userId,bannerInfoData,siteId,bannerProperties);
			if(result>0){
				bannerInfoData.setId(result);
				int bannerSaveResult = 0;
				ArrayList<BannerInfo> bannerInfoList = new ArrayList<BannerInfo>();
				for(String bannerId:bannerIdList){
					//captionText = request.getParameter("bannerCaption_"+bannerId);
					//topPos = request.getParameter("bannerCaptionNew_"+bannerId);
					captionText = request.getParameter("bannerCaptionNew_"+bannerId);
					topPos = request.getParameter("bannerTopPosNew_"+bannerId); 
					leftPos = request.getParameter("bannerLeftPosNew_"+bannerId);
					
					url = request.getParameter("URL_"+bannerId);
					if(leftPos!=null && !leftPos.isEmpty()){
						captionLeftPos = Integer.parseInt(leftPos);
					}
					if(topPos!=null && !topPos.isEmpty()){
						captionTopPos = Integer.parseInt(topPos);
					}
					bannerInfo = new BannerInfo();
					bannerInfo.setId(CommonUtility.validateNumber(bannerId));
					
					bannerInfo.setCaptionText(captionText);
					bannerInfo.setCaptionLeftPos(captionLeftPos);
					bannerInfo.setCaptionTopPos(captionTopPos);
					bannerInfo.setBannerUrl(url);
					bannerInfoList.add(bannerInfo);
					System.out.println("Banner Image : " + bannerId);
				}
				bannerInfoData.setBannerInfo(bannerInfoList);
				bannerSaveResult = cmsDao.saveBannerInfo(bannerInfoData,userId);
				if(bannerSaveResult<1)
					bannerResponse = "Something went wrong. Please call technical support for assistance.";

			}else{
				if(result == -1){
					bannerResponse = "BannerList name already exist.";
				}else if(result == -9){
					bannerResponse = "Something went wrong. Please call technical support for assistance.";
				}
				
				}
		}
		renderContent = bannerResponse;
	}catch (Exception e) {
		renderContent = "Failed";
		e.printStackTrace();
	}
	return SUCCESS;
}

public String updateBanner(){
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		request =ServletActionContext.getRequest();
		String bannerImageName[] = request.getParameterValues("bannerImageName");

		String bannerIdList[] = request.getParameterValues("bannerId");
		String bannerIdListNew[] = request.getParameterValues("bannerIdNew");

		String bannerListName = request.getParameter("bannerListName");

		String bannerListId = request.getParameter("bannerListId");
		String bannerDeleteIdList = request.getParameter("bannerDeleteIdList");
		System.out.println("Banner List Id:"+bannerListId);
		String captionText  = null;
		String leftPos = null;
		String topPos = null;
		String url = null;
		int displaySeq = 0;
		String bannerInfoId = null;
		int userId = 0;
		int captionTopPos = 20;
		int captionLeftPos = 30;
		boolean saveBannerList = true;
		int bannerUpdateResponse = 0;
		String bannerResponse = "success";
		String bannerTransistion = request.getParameter("ssTransition");
		String bannerTransistionDelay = request.getParameter("ssTransitionDelay");
		String bannerTransistionAutoPlay = request.getParameter("ssTransitionAutoPlay");
		String carouselSetting = request.getParameter("carouselSetting");
		String templateName = request.getParameter("templateName");
		String bannerType = request.getParameter("bannerType");
		LinkedHashMap<String,String> bannerProperties = new LinkedHashMap<String,String>();

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date sDate = null;
		Date eDate = null;
		
		System.out.println("Banner Transistion Properties:\n"+bannerTransistion+"\n"+bannerTransistionDelay+"\n"+bannerTransistionAutoPlay);
		
		if(!CommonUtility.validateString(startDate).equalsIgnoreCase("")){
		sDate = formatter.parse(startDate);
		}
		if(!CommonUtility.validateString(endDate).equalsIgnoreCase("")){
		eDate = formatter.parse(endDate);
		}
		if(!CommonUtility.validateString(carouselSetting).trim().equalsIgnoreCase("")){
			bannerProperties.put("carouselSetting",carouselSetting);
		}
		bannerProperties.put("bannerTransistion",CommonUtility.validateString(bannerTransistion));
		bannerProperties.put("bannerTransistionDelay",CommonUtility.validateString(bannerTransistionDelay));
		bannerProperties.put("bannerTransistionAutoPlay",CommonUtility.validateString(bannerTransistionAutoPlay));
		if(!CommonUtility.validateString(bannerType).trim().equalsIgnoreCase("")){
			bannerProperties.put("bannerType",bannerType);
		}
		if(CommonUtility.validateString(templateName).trim().equalsIgnoreCase("")){
			templateName = "default";
		}
		bannerProperties.put("templateName",templateName);



		if(bannerListName==null){
			saveBannerList = false;
			bannerResponse = "Please enter banner list name.";
		}
		if(bannerImageName!=null)
		System.out.println("bannerImageName : " + bannerImageName.length);

		if(saveBannerList){
			BannerInfo bannerInfo = null;
			ArrayList<BannerInfo> bannerInfoList = new ArrayList<BannerInfo>();
			ArrayList<BannerInfo> bannerInfoListNew = new ArrayList<BannerInfo>();
			ArrayList<BannerInfo> bannerDelList = new ArrayList<BannerInfo>();
			CmsDao cmsDao = new CmsDao();
			
			if(bannerDeleteIdList!=null){
				String tempBannerListInfo[] = bannerDeleteIdList.split(",");
				
				if(tempBannerListInfo!=null && tempBannerListInfo.length > 0){
					for(String delInfo : tempBannerListInfo){
						bannerInfo = new BannerInfo();
						bannerInfo.setId(CommonUtility.validateNumber(delInfo));
						bannerDelList.add(bannerInfo);
					}
				}
			}
			
		if(bannerIdListNew!=null && bannerIdListNew.length>0){
			for(String bannerId:bannerIdListNew){
				captionText = request.getParameter("bannerCaptionNew_"+bannerId);
				leftPos = request.getParameter("bannerLeftPosNew_"+bannerId);
				topPos = request.getParameter("bannerTopPosNew_"+bannerId);
				url = request.getParameter("URL_"+bannerId);
				displaySeq = CommonUtility.validateNumber(request.getParameter("displaySeq_"+bannerId));
				if(leftPos!=null && !leftPos.isEmpty()){
					captionLeftPos = Integer.parseInt(leftPos);
				}
				if(topPos!=null && !topPos.isEmpty()){
					captionTopPos = Integer.parseInt(topPos);
				}
				bannerInfo = new BannerInfo();
				bannerInfo.setId(CommonUtility.validateNumber(bannerId));
				//bannerInfo.setBannerListId(Integer.parseInt(bannerListId));
				bannerInfo.setCaptionText(captionText);
				bannerInfo.setCaptionLeftPos(captionLeftPos);
				bannerInfo.setCaptionTopPos(captionTopPos);
				bannerInfo.setBannerUrl(url);
				bannerInfo.setDisplaySequence(displaySeq);
				bannerInfoListNew.add(bannerInfo);
					
				System.out.println("Banner Image : " + bannerId);
			
			}
				
		}
			if(bannerIdList!=null && bannerIdList.length>0){
				for(String bannerId:bannerIdList){
					captionText = request.getParameter("bannerCaption_"+bannerId);
					System.out.println("Caption : " + captionText);
					leftPos = request.getParameter("bannerLeftPos_"+bannerId);
					topPos = request.getParameter("bannerTopPos_"+bannerId);
					displaySeq = CommonUtility.validateNumber(request.getParameter("displaySeq_"+bannerId));
					System.out.println("displaySeq : " + displaySeq);
					bannerInfoId = request.getParameter("bannerInfoId_"+bannerId);
					url = request.getParameter("URL_"+bannerId);
					System.out.println("url : " + url);
					if(leftPos!=null && !leftPos.isEmpty()){
						captionLeftPos = Integer.parseInt(leftPos);
					}
					if(topPos!=null && !topPos.isEmpty()){
						captionTopPos = Integer.parseInt(topPos);
					}
					bannerInfo = new BannerInfo();
					bannerInfo.setId(CommonUtility.validateNumber(bannerInfoId));
					//bannerInfo.setBannerListId(Integer.parseInt(bannerListId));
					bannerInfo.setCaptionText(captionText);
					bannerInfo.setCaptionLeftPos(captionLeftPos);
					bannerInfo.setCaptionTopPos(captionTopPos);
					bannerInfo.setBannerUrl(url);
					bannerInfo.setDisplaySequence(displaySeq);
					bannerInfoList.add(bannerInfo);
					//updateBannerListInfo(User user, ArrayList<Banners> bannerInfoList,int bannerListId,String bannerName, ArrayList<Banners> bannerDelList)
					
					
					System.out.println("Banner Image : " + bannerId);
				
				}
			}
			if((bannerInfoList!=null && bannerInfoList.size()>0) || (bannerInfoListNew!=null && bannerInfoListNew.size()>0)){
				bannerUpdateResponse = cmsDao.updateBannerListInfo(bannerInfoList,Integer.parseInt(bannerListId),bannerListName,bannerDelList,bannerInfoListNew,bannerProperties,sDate,eDate,userId);
			}
			
			if(bannerUpdateResponse == -1){
				bannerResponse = "BannerList name already exist.";
			}else if(bannerUpdateResponse == -9){
				bannerResponse = "Something went wrong. Please call technical support for assistance.";
			}}
renderContent = bannerResponse;
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}



public String bannerUpload(){
	if(!validateCms()){
		return "unauthorized";
	}
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	try{
		renderContent = CMSLayoutGenerator.templateLoader("CMSBannerUpload", contentObject, null, null, null);
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}

public String widgetList(){
	PromotionRequest searchObj = null;		
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		String httpMethod = HttpMethod.GET;
		request =ServletActionContext.getRequest();
		String requestType = request.getParameter("requestType");
		page = page + 1;
		
		if(pageSize==0){
			pageSize = 10;
		}
		StringBuilder requestString = new StringBuilder();
		String url = "/admin/widget/findAll";
		requestString.append(url).append("?");
		requestString.append("page=").append(page).append("&");
		requestString.append("pageSize=").append(pageSize);
		requestString.append("&status=A");	
		//requestString.append("siteId=").append(siteId);
		if(!CommonUtility.validateString(query).equalsIgnoreCase("")){
			searchObj = CMSUtility.getInstance().buildSearchRequest(query, page, pageSize, "name","", "");
			httpMethod = HttpMethod.POST;
			requestString = new StringBuilder();
			requestString.append(url).append("?status=A");
		}
		if(CommonUtility.validateString(query).equalsIgnoreCase("")){
		searchObj = CMSUtility.getInstance().buildSortRequest(true, page, pageSize, "Name",""); 
		httpMethod = HttpMethod.POST;
		requestString = new StringBuilder();
		requestString.append(url).append("?status=A");
		}		
		Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), httpMethod, searchObj, WidgetList.class, siteId);
		WidgetList widgetList = (WidgetList) response.getData();
		contentObject.put("query" , query);
		contentObject.put("widgetList" , widgetList);
		if(requestType!=null && requestType.trim().equalsIgnoreCase("ajax")){
			renderContent = CMSLayoutGenerator.templateLoader("CMSWidgetListAjax", contentObject, null, null, null);
		}else{
			renderContent = CMSLayoutGenerator.templateLoader("CMSWidgetList", contentObject, null, null, null);
		}
			
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}



public String formList(){
	PromotionRequest searchObj = null;
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		String httpMethod = HttpMethod.GET;
		request =ServletActionContext.getRequest();
		String requestType = request.getParameter("requestType");
		page = page + 1;
		
		if(pageSize==0){
			pageSize = 10;
		}
		StringBuilder requestString = new StringBuilder();
		String url = "/admin/cimmUIForm/findAll";
		requestString.append(url).append("?");
		requestString.append("page=").append(page).append("&");
		requestString.append("pageSize=").append(pageSize);
		requestString.append("&status=A");
		//requestString.append("siteId=").append(siteId);
		if(!CommonUtility.validateString(query).equalsIgnoreCase("")){
			searchObj = CMSUtility.getInstance().buildSearchRequest(query, page, pageSize, "formName","", "");
			httpMethod = HttpMethod.POST;
			requestString = new StringBuilder();
			requestString.append(url);
		}
		Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), httpMethod, searchObj, FormList.class, siteId);
		FormList formList = (FormList) response.getData();
		contentObject.put("query" , query);
		contentObject.put("formList" , formList);
		if(requestType!=null && requestType.trim().equalsIgnoreCase("ajax")){
			renderContent = CMSLayoutGenerator.templateLoader("CMSFormListAjax", contentObject, null, null, null);
		}else{
			renderContent = CMSLayoutGenerator.templateLoader("CMSFormList", contentObject, null, null, null);
		}
			
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}


public String editFormData() {
	
	FormData formData = null;
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		if(formId>0){
			formData = BannerTemplate.getInstance().getFormData(formId, siteId);
		}
		ArrayList<String> notificationList = CmsDao.notificationList();
		contentObject.put("formData", formData);
		contentObject.put("formId", formId);
		contentObject.put("notificationList", notificationList);
		renderContent = CMSLayoutGenerator.templateLoader("CMSFormEdit", contentObject, null, null, null);
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}

public String addUpdateFormData(){

	String requestType = "success ";
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		request =ServletActionContext.getRequest();
		
		FormData staticFormData = new FormData();
		
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		//StaticPageData staticPageData = new StaticPageData();
		
		CmsDao cmsDao = new CmsDao();
		List<WebsiteModel> websiteList =  getWebsiteList();
		if(CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId))>0) {
			siteId = CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId));
		}else if(CommonDBQuery.getGlobalSiteId() > 0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}else if(siteId == 0 && websiteList!=null && websiteList.size()>0){
			for (WebsiteModel websiteModel : websiteList){
				if(CommonUtility.validateString(websiteModel.getPrimarySite()).equalsIgnoreCase("Y")) {
					siteId = websiteModel.getSiteId();
					break;
				}
			}
		} 
		if(CommonDBQuery.getGlobalSiteId()>0 && siteId == 0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}
		
		String url = "/admin/cimmUIForm/create";

		
		String httpMethod = HttpMethod.POST;
		if(formData.getId() > 0){
			
			
			
			if(type!=null && type.equalsIgnoreCase("delete")){
				httpMethod = HttpMethod.DELETE;
				url = "/admin/cimmUIForm/delete?id="+formData.getId();
			}else{
				httpMethod = HttpMethod.PUT;
				url ="/admin/cimmUIForm/update";
				requestType = "update ";
			}
			
			
			
		}
		if(formData.getSaveToDB()!=null){
			formData.setSaveToDB("Y");
		}else{
			formData.setSaveToDB("N");
		}
		
		if(formData.getEmailSent()!=null){
			formData.setEmailSent("Y");
		}else{
			formData.setEmailSent("N");
		}
		
		System.out.println(formData.getId());
	 Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(url, httpMethod, formData, FormData.class,siteId);
	 staticFormData = (FormData) response.getData();
	
		if(response!=null && response.getStatus().getCode()==200){
			renderContent = requestType+staticFormData.getId();
			//renderContent = "success "+staticFormData.getId();
		}else{
			renderContent ="error|" + response.getStatus().getMessage();
		}
		//renderContent = CMSLayoutGenerator.templateLoader("CMSStaticPage", contentObject, null, null, null);
	}catch (Exception e) {
		
		e.printStackTrace();
	}
	
	return SUCCESS;

}

public String generateFrom(){
	
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		String httpMethod = HttpMethod.GET;
		request =ServletActionContext.getRequest();
		String url = "/admin/cimmUIForm/find/"+formId;
		Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(url, httpMethod, null, FormData.class, siteId);
		FormData formData = (FormData) response.getData();
		renderContent = formData.getHtmlCode();
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}

public String widgetType(){
	
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		Properties widgetTypeList = CMSUtility.getInstance().getWidgetTypes();
		contentObject.put("widgetTypeList", widgetTypeList);
		renderContent = CMSLayoutGenerator.templateLoader("CMSWidgetType", contentObject, null, null, null);
	}catch (Exception e) {
		e.printStackTrace();
	}
	
	return SUCCESS;
}

public String editWidgetData() {
	
	WidgetData widgetData = null;
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	Map<String, String> propertiesMap = null;
	Gson gson = new Gson();
	try{
		String widgetType = "custom";
		String properties = null;
		if(widgetId>0){
			widgetData = BannerTemplate.getInstance().getWidgetData(widgetId, siteId);
			properties =CommonUtility.validateString(widgetData.getProperties());
			Type type = new TypeToken<Map<String, String>>(){}.getType();
	    	propertiesMap = gson.fromJson(properties, type);
	    	if(propertiesMap!=null && propertiesMap.get("type")!=null){
	    		widgetType = propertiesMap.get("type");
	    	}
		}
		
    	Properties widgetTypeList = CMSUtility.getInstance().getWidgetTypes();
    	contentObject.put("widgetTypeList", widgetTypeList);
    	contentObject.put("widgetType", widgetType);
		contentObject.put("widgetData", widgetData);
		
		renderContent = CMSLayoutGenerator.templateLoader("CMSWidgetEdit", contentObject, null, null, null);
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}


public String updateWidget() {
	if(!validateCms()){
		return "unauthorized";
	}
	boolean proceedFlag = true;
	WidgetData widgetDataResponse = null;
	request =ServletActionContext.getRequest();
	String widgetType = request.getParameter("widgetType");
	String method = HttpMethod.PUT;
	String url = "/admin/widget/"+type;
	if(CommonUtility.validateString(type).equalsIgnoreCase("create")){
		method = HttpMethod.POST;
		
		//---------------
		page = page + 1;
		if(pageSize==0){
			pageSize = 10;
		}
		StringBuilder requestString = new StringBuilder();
		String urlGet = "/admin/widget/findAll";
		PromotionRequest searchObj = CMSUtility.getInstance().buildSearchRequest(CommonUtility.validateString(widget.getName()), page, pageSize, "name","", "EQUAL_IGNORE_CASE");
		requestString = new StringBuilder();
		requestString.append(urlGet);
		Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), method, searchObj, WidgetList.class, siteId);
		if(response!=null && response.getData()!=null){
			WidgetList widgetList = (WidgetList) response.getData();
			if(widgetList!=null && widgetList.getResultSet()!=null && widgetList.getResultSet().size()>0) {
				for(WidgetData wData : widgetList.getResultSet()) {
					if(wData!=null && widget!=null && CommonUtility.validateString(wData.getName()).equalsIgnoreCase(CommonUtility.validateString(widget.getName()))) {
						proceedFlag = false;
						break;
					}
				}
			}
		}
		//===============
		
	}else if(CommonUtility.validateString(type).equalsIgnoreCase("delete")){
		method = HttpMethod.DELETE;
		url = url + "?id="+widget.getId();
	}
	try{
		if(proceedFlag) {
			Gson gson = new Gson();
			  LinkedHashMap<String,String> properties = new LinkedHashMap<String,String>();
			  if(!CommonUtility.validateString(widgetType).trim().equalsIgnoreCase("")){
			    	properties.put("type",widgetType);
			    	widget.setProperties(gson.toJson(properties));
			    }else{
			    	properties.put("type","custom");
			    	widget.setProperties(gson.toJson(properties));
			    }
			Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(url,method, widget, WidgetData.class, siteId);
			if(response!=null){
				widgetDataResponse = (WidgetData) response.getData();
				if(widgetDataResponse==null){
					widgetDataResponse = new WidgetData();
				}
				widgetDataResponse.setStatus(response.getStatus().getMessage());
				renderContent = response.getStatus().getMessage();
			}
		}else{
			renderContent = "unique constraint";
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}

public String generateWidget() {
	String templateOutput = null;
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Gson gson = new Gson();
		String cimm2bcUrl = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BCENTRAL_ACCESS_URL"));
		Map<String,Object> widgetTypeObj = null;
		Type listType = new TypeToken<HashMap<String,Object>>(){}.getType();
		
		WidgetData widgetData = BannerTemplate.getInstance().getWidgetData(widgetId, siteId);
		String widgetType = "";
		String widgetProperties = "";
		if(widgetData!=null){
			widgetProperties = widgetData.getProperties();
			widgetTypeObj = gson.fromJson(widgetProperties, listType);
			if(widgetTypeObj!=null && widgetTypeObj.get("type")!=null){
			widgetType = (String) widgetTypeObj.get("type");
			}
		}
		System.out.println("widgetType : " + widgetType);
		
		String pricePrecision = "";
	    String pricePrecisionFormate = "#0.00";
	    if(session!=null && session.getAttribute("pricePrecision")!=null && CommonUtility.validateString((String)session.getAttribute("pricePrecision")).length()>0){
			pricePrecision = CommonUtility.validateString((String)session.getAttribute("pricePrecision"));
		}else if(CommonUtility.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision")).length()>0){
			pricePrecision = CommonUtility.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision"));
		}else{
			pricePrecision="2";
		}
	    if(CommonUtility.validateString(pricePrecision).equalsIgnoreCase("5")){
	    	pricePrecisionFormate = "#0.00000";
		}else if(CommonUtility.validateString(pricePrecision).equalsIgnoreCase("4")){
			pricePrecisionFormate = "#0.0000";
		}else if(CommonUtility.validateString(pricePrecision).equalsIgnoreCase("3")){
			pricePrecisionFormate = "#0.000";
		}else{
			pricePrecisionFormate = "#0.00";
		}
		
		ToolManager velocityToolManager = new ToolManager();
    	velocityToolManager.configure("velocity-tools.xml");
    	VelocityContext context = new VelocityContext(velocityToolManager.createContext());
    	VelocityEngine velocityTemplateEngine = new VelocityEngine();
        String templatePath = CommonDBQuery.getSystemParamtersList().get("SITE_TEMPLATE_PATH")+CommonDBQuery.getSystemParamtersList().get("SITE_NAME")+CommonDBQuery.getSystemParamtersList().get("MACROS_FOLDER");
        //System.out.println("templatePath : "+templatePath);
        velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
        velocityTemplateEngine.setProperty("velocimacro.library.autoreload", true);
        velocityTemplateEngine.setProperty("velocimacro.library", "defaultMacro.vm");
        velocityTemplateEngine.init();
		String url = CMSUtility.getInstance().getUrl(widgetType);
		
	
		StringWriter writer = new StringWriter();
		context.put("CIMM2VelocityTool", CIMM2VelocityTool.getInstance());
		if(!CommonUtility.validateString(url).equalsIgnoreCase("")){
			if(!url.startsWith("http")){
				url = cimm2bcUrl+url;
			}
			String output = Cimm2BCentralClient.getInstance().getJsonResponse(url, HttpMethod.GET, null, siteId);
			Map<String,Object> convertedMap = gson.fromJson(output, listType);
			Map<String,Object> list = (Map<String, Object>) convertedMap.get("data");
		    context.put("data", list);
		    if(session.getAttribute("localeCode")!=null ){
		    	context.put("locale",LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()));
		    	context.put("localeLang",(String)session.getAttribute("sessionLocale"));
		    }else{
		    	context.put("locale",LayoutLoader.getMessageProperties().get("EN"));
		    	context.put("localeLang","1_en");
		    	session.setAttribute("localeCode","EN");
		    	session.setAttribute("sessionLocale","1_en");
		    }
		    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		    context.put("numberTool", new NumberTool());
	    	context.put("escapeTool", new EscapeTool());
	    	context.put("math", new MathTool());
	    	context.put("dispalyTool", new DisplayTool());
	    	context.put("convert", new ConversionTool());
	    	context.put("dateTool", new ComparisonDateTool());
	    	context.put(Integer.class.getSimpleName(), Integer.class);
	    	context.put("session", session);
	    	context.put("timeStamp",timestamp.getTime());
	    	context.put("CIMMUtility", CIMM2VelocityTool.getInstance());
	    	context.put("CIMM2VelocityTool", CIMM2VelocityTool.getInstance());
	    	context.put("linkTool",new LinkTool());
	    	context.put("sortTool", new SortTool());
	    	context.put("pricePrecisionFormate", CommonUtility.validateString(pricePrecisionFormate));
			context.put("siteId", CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
		    context.put("siteName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_NAME")));
		    context.put("webThemes", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES")));
		    context.put("thumbNail", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("THUMBNAIL")));
		    context.put("itemImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMIMAGE")));
		    context.put("detailImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DETAILIMAGE")));
		    context.put("enlargedImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENLARGEIMAGE")));
		    context.put("taxonomyImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TAXONOMYIMAGEPATH")));
		    context.put("documentPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DOCUMENTS")));
		    context.put("brandLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRANDLOGO")));
		    context.put("buyingCompanyLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BUYINGCOMPANYLOGO")));
		    context.put("bannerLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BANNERLOGO")));
		    context.put("manufacturerLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MNF_IMAGES")));
		    String refreshVersion = "rv="+session.getId();
			context.put("refreshVersion",CommonUtility.validateString(refreshVersion));
		}
		velocityTemplateEngine.evaluate(context, writer, "", widgetData.getTemplateCode());
		templateOutput = writer.toString();
		System.out.println(templateOutput);
		renderContent = templateOutput;
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
	
}


public String addUpdateWidgetType() {
	
	renderContent = "Updated Successfully;";
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		Properties prop = new Properties();
		OutputStream output = null;

		try {
			if(widgetTypeKey!=null && widgetTypeKey.length > 0){
				output = new FileOutputStream(CommonDBQuery.getSystemParamtersList().get("WIDGET_PROPERTIES_FILE"));
				for(int i=0;i<widgetTypeKey.length;i++){
					if(widgetTypeKey[i]!=null && !widgetTypeKey[i].trim().equalsIgnoreCase("")){
						prop.setProperty(widgetTypeKey[i], widgetTypeValue[i]);
					}
					if(i < widgetTypeKey.length-1 &&  widgetTypeKey[i].equals(widgetTypeKey[widgetTypeKey.length-1])) {
						renderContent = "Duplicate Widget-Type!!";
					}
				}
			}
			
			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			CMSUtility.setProps(null);
		}
		System.out.println(widgetTypeKey);
	}catch (Exception e) {
		renderContent = "Something went wrong.";
		e.printStackTrace();
	}
	return SUCCESS;
}

public String getUserLibrary(){
	PromotionRequest searchObj = null;
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		String httpMethod = HttpMethod.GET;
		request =ServletActionContext.getRequest();
		String requestType = request.getParameter("requestType");
		page = page + 1;
		
		if(pageSize==0){
			pageSize = 10;
		}

		String url = "/admin/userLibraries/search/"+libraryId+"?reload=true";
		//String url = "/admin/cimmUserLibrary/search/"+libraryId+"?reload=true";


		Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(url, httpMethod, searchObj, ComponentList.class, 0);
		ComponentList componentList = (ComponentList) response.getData();
		contentObject.put("query" , query);
		contentObject.put("componentList" , componentList);
		if(requestType!=null && requestType.trim().equalsIgnoreCase("ajax")){
			renderContent = CMSLayoutGenerator.templateLoader("CMSLibraryListAjax", contentObject, null, null, null);
		}else{
			renderContent = CMSLayoutGenerator.templateLoader("CMSLibraryList", contentObject, null, null, null);
		}
			
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}
private ArrayList<StaticPageData> templateNames;


public ArrayList<StaticPageData> getTemplateNames() {
return templateNames;
}

public void setTemplateNames(ArrayList<StaticPageData> templateNames) {
this.templateNames = templateNames;
}
public String loadAllDynamicTemplates(){
	
	
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		templateNames=new ArrayList<StaticPageData>();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		String imagepath= CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("STATIC_PAGE_TEMPLATE_IMAGE_LIST"));
		String warFileName = CMSConstants.getWarFileName(CommonDBQuery.getSystemParamtersList().get("STATIC_PAGE_TEMPLATE_LIST"));	
		String virtualFolderPath = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH"));
		//String warFileName = CMSConstants.getWarFileName(staticPageTemplateList);
		String folderPath = CommonUtility.validateString(virtualFolderPath) + CommonUtility.validateString(warFileName);
		
		System.out.println("warFileName----"+warFileName);
		System.out.println("virtualFolderPath----"+virtualFolderPath);
		System.out.println("folderPath---"+folderPath);
		
		String imageName = ""; 
		File folder =  new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles!=null && listOfFiles.length>0){
			for (int i = 0; i < listOfFiles.length; i++) {
			      if (listOfFiles[i].isFile()) {
			    	  String fileName = listOfFiles[i].getName();
			    	  if(fileName.toUpperCase().endsWith(".HTML")){
			    		  StaticPageData stpages = new StaticPageData();
			    		  stpages.setTemplateName(fileName);
			    		  imageName =  fileName.substring(0, fileName.lastIndexOf("."))+".jpg";
			    		  if(imageName!=null && imageName.length()>0){
			    		  stpages.setTemplateImgName("../../.."+imagepath+imageName);
			    		  }
			    		  System.out.println("Image Names "+stpages.getTemplateImgName());
			    		  templateNames.add(stpages);
			    		  System.out.println("Inside File " + listOfFiles[i].getName());
			    	  }
			      }
			    }
		}
		contentObject.put("templateNames" , templateNames);
	}catch(Exception e){
		e.printStackTrace();
	}
	return null;
}



	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	public String getRenderContent() {
		return renderContent;
	}


	public void setPageId(int pageId) {
		this.pageId = pageId;
	}


	public int getPageId() {
		return pageId;
	}
	
	
	public void setWidgetId(int pageId) {
		this.widgetId = pageId;
	}


	public int getWidgetId() {
		return widgetId;
	}


	public void setStaticPageData(String staticPageData) {
		this.staticPageData = staticPageData;
	}


	public String getStaticPageData() {
		return staticPageData;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public HttpServletRequest getRequest() {
		return request;
	}


	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}


	public HttpServletResponse getResponse() {
		return response;
	}


	public void setPageData(StaticPageData pageData) {
		this.pageData = pageData;
	}


	public StaticPageData getPageData() {
		return pageData;
	}


	public void setBannerListId(int bannerListId) {
		this.bannerListId = bannerListId;
	}


	public int getBannerListId() {
		return bannerListId;
	}


	public void setReload(String reload) {
		this.reload = reload;
	}


	public String getReload() {
		return reload;
	}


	public void setWidget(WidgetData widget) {
		this.widget = widget;
	}


	public WidgetData getWidget() {
		return widget;
	}

	public void setWidgetTypeKey(String[] widgetTypeKey) {
		this.widgetTypeKey = widgetTypeKey;
	}

	public String[] getWidgetTypeKey() {
		return widgetTypeKey;
	}

	public void setWidgetTypeValue(String[] widgetTypeValue) {
		this.widgetTypeValue = widgetTypeValue;
	}

	public String[] getWidgetTypeValue() {
		return widgetTypeValue;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public int getFormId() {
		return formId;
	}

	public void setFormData(FormData formData) {
		this.formData = formData;
	}

	public FormData getFormData() {
		return formData;
	}

	public void setLibraryId(int libraryId) {
		this.libraryId = libraryId;
	}

	public int getLibraryId() {
		return libraryId;
	}
	
	// form export
	private String saveMessage;	
public String getSaveMessage() {
		return saveMessage;
	}

	public void setSaveMessage(String saveMessage) {
		this.saveMessage = saveMessage;
	}

public String exportFormContents() {
		
		 
		try {
			request =ServletActionContext.getRequest();
			response =ServletActionContext.getResponse();
			String formName=request.getParameter("formName");
			long formId=Long.parseLong(request.getParameter("formId"));

			SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
			String date = s.format(new java.util.Date());  
			
			response.setContentType("application/octet-stream");
			//response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment; filename=" + formName + date + "_Form.zip");
			OutputStream respOs = response.getOutputStream();
			CmsDao.exportFormContentsForSpecificFormName(respOs, formId,formName);
			System.out.println("-----download Completed----- :" + "");

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
		return SUCCESS;
	}

public String cmsStaticPageByPageName(){
	
	try{
		if(!validateCms()){
			return "unauthorized";
		}
		request =ServletActionContext.getRequest();
		response =ServletActionContext.getResponse();
		StringBuilder requestString = new StringBuilder();
		CmsDao cmsDao = new CmsDao();
		requestString.append("/admin/userLibraries/findAll").append("?");
		//requestString.append("/admin/cimmUserLibrary/findAll").append("?");
		requestString.append("page=").append(page).append("&");
		requestString.append("pageSize=").append(pageSize);
		
		String pageName=request.getParameter("pageName");
		pageName=pageName.substring(0, pageName.lastIndexOf('.'));
		
		pageId= CmsDao.getIdByPageName(pageName);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		
		List<LocaleModel> ldata=cmsDao.getLoacleList();
		StaticPageData staticPageData = new StaticPageData();
		
		List<WebsiteModel> websiteList =  getWebsiteList();
		if(CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId))>0) {
			siteId = CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId));
		}else if(CommonDBQuery.getGlobalSiteId() > 0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}else if(siteId == 0 && websiteList!=null && websiteList.size()>0){
			for (WebsiteModel websiteModel : websiteList){
				if(CommonUtility.validateString(websiteModel.getPrimarySite()).equalsIgnoreCase("Y")) {
					siteId = websiteModel.getSiteId();
					break;
				}
			}
		} 
		if(CommonDBQuery.getGlobalSiteId()>0 && siteId == 0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}
		
		if(pageId>0){
			String url = context+staticPage+find+pageId;
			
			 Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(url, HttpMethod.GET, null, StaticPageData.class,siteId);
			 staticPageData = (StaticPageData) response.getData();
		}else{
			staticPageData.setSiteId(siteId);
		}
	
		 Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), HttpMethod.GET, null, LibraryList.class,0);
		 LibraryList libraryList = (LibraryList) response.getData();
		loadAllDynamicTemplates();
		contentObject.put("libraryList" , libraryList);
		contentObject.put("staticPageData" , staticPageData);
		contentObject.put("localeListData" , ldata);
		contentObject.put("templateNames" , templateNames);
		renderContent = CMSLayoutGenerator.templateLoader("CMSStaticPage", contentObject, null, null, null);
	}catch (Exception e) {
		
		e.printStackTrace();
	}
	
	return SUCCESS;
}

public String listThemes(){
	request =ServletActionContext.getRequest();
	response = ServletActionContext.getResponse();
	response.setContentType("application/json");
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	Gson gson = new Gson();
	try{
		String activeTheme = CommonDBQuery.getSystemParamtersList().get("ThemeFileName");
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ScheduledThemeFileName")).length() > 0){
			activeTheme = CommonDBQuery.getSystemParamtersList().get("ScheduledThemeFileName");
		}
		List<WebsiteModel> websiteList =  getWebsiteList();
		if(CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId))>0) {
			siteId = CommonUtility.validateNumber(CommonUtility.validateString(selectedSiteId));
		}else if(CommonDBQuery.getGlobalSiteId() > 0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}else if(siteId == 0 && websiteList!=null && websiteList.size()>0){
			for (WebsiteModel websiteModel : websiteList){
				if(CommonUtility.validateString(websiteModel.getPrimarySite()).equalsIgnoreCase("Y")) {
					siteId = websiteModel.getSiteId();
					break;
				}
			}
		} 
		CmsDao cmsDao = new CmsDao();
		setWebsiteList(cmsDao.getWebsiteList());
		List<ThemeModel> themeList = cmsDao.getThemeList(siteId);
		contentObject.put("wibsiteList",websiteList);
		contentObject.put("themeList", themeList);
		contentObject.put("siteId", siteId);
		contentObject.put("activeTheme", activeTheme);
		renderContent = gson.toJson(contentObject);
	}catch (Exception e) {
		e.printStackTrace();// TODO: handle exception
	}
	return SUCCESS;
}

public String addUpdateTheme(){
	request = ServletActionContext.getRequest();
	HttpSession session = request.getSession();
	response = ServletActionContext.getResponse();
	try{
		Gson gson = new Gson();
		String requestData = CMSUtility.getInstance().getJsonRequest(request);
		System.out.println(requestData);
		ThemeModel themeModel = gson.fromJson(requestData, ThemeModel.class);
		String themeName = themeModel.getThemeName();
		themeName = themeName.replaceAll("[^A-Za-z0-9 ]", "").replaceAll("\\s+","_").toLowerCase()+".css";
		themeModel.setCssName(themeName);
		CmsDao cmsDao = new CmsDao();
		int id = cmsDao.addUpdateTheme(themeModel);
		if(id > 0){
			CMSUtility.getInstance().writeCssFile(themeModel);
		}
		renderContent = CommonUtility.validateParseIntegerToString(id);
	}catch (Exception e) {
		e.printStackTrace();// TODO: handle exception
	}
	return SUCCESS;
}

public String cloneTheme(){
	request = ServletActionContext.getRequest();
	HttpSession session = request.getSession();
	response = ServletActionContext.getResponse();
	try{
		Gson gson = new Gson();
		String requestData = CMSUtility.getInstance().getJsonRequest(request);
		System.out.println(requestData);
		ThemeModel themeModel = gson.fromJson(requestData, ThemeModel.class);
		String themeName = themeModel.getThemeName();
		themeName = themeName.replaceAll("[^A-Za-z0-9 ]", "").replaceAll("\\s+","_").toLowerCase()+".css";
		themeModel.setCssName(themeName);
		CmsDao cmsDao = new CmsDao();
		int id = cmsDao.cloneTheme(themeModel);
		renderContent = CommonUtility.validateParseIntegerToString(id);
	}catch (Exception e) {
		e.printStackTrace();// TODO: handle exception
	}
	return SUCCESS;
}

public String deleteTheme(){
	request = ServletActionContext.getRequest();
	response = ServletActionContext.getResponse();
	try{
		Gson gson = new Gson();
		String requestData = CMSUtility.getInstance().getJsonRequest(request);
		System.out.println(requestData);
		ThemeModel themeModel = gson.fromJson(requestData, ThemeModel.class);
		CmsDao cmsDao = new CmsDao();
		int id = cmsDao.deleteTheme(themeModel);
		renderContent = CommonUtility.validateParseIntegerToString(id);
	}catch (Exception e) {
		e.printStackTrace();// TODO: handle exception
	}
	return SUCCESS;
}


public String getFontList() {
	
	
	HashSet<String> fontList = new HashSet<String>();
	Gson gson = new Gson();
	try{
		LinkedHashMap<String, Object> contentObj = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, String> tempVar = new LinkedHashMap<>();
		StringBuilder fontLocation = new StringBuilder();
		StringBuilder fontLocationUrl = new StringBuilder();
		StringBuilder defaultFonts = new StringBuilder();
		String templatePath = CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH");// CimmResources.systemParameterNameValueMap.get("BANNER_TEMPLATE_PATH");
		String warFileName = CMSConstants.getWarFileName(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES"));
		String warFileNameUrl = CommonDBQuery.getSystemParamtersList().get("WEB_THEMES");
		String siteName = CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
		//fontLocation.append(templatePath).append(warFileName).append(siteName).append("/fonts");
		fontLocation.append(templatePath).append(warFileName).append("/"+siteName).append("/fonts");
		fontLocationUrl.append(warFileNameUrl).append("/"+siteName).append("/fonts/");
		System.out.println("Font Path : " + fontLocation);
		String fileName = null;
		int i = 1;
		String str1  = "@include font-face(";
		String str2 = "', normal, normal, eot woff ttf svg,";
		String c = "";
		String cleanstring = "";
		fontList.add("RobotoRegular");
		fontList.add("RobotoCondensedBold");
		fontList.add("RobotoBlack");
	      System.out.println( "NIO run" );
	      long start = System.currentTimeMillis();
	      String directoryName = fontLocation.toString();
			File directory = new File(directoryName);
		    if (! directory.exists()){
		        directory.mkdir();
		    }
	      Path dir = FileSystems.getDefault().getPath( fontLocation.toString() );
	      DirectoryStream<Path> stream = Files.newDirectoryStream( dir );
	      for (Path path : stream) {
	    	  fileName = path.getFileName().toString();
	    	  if (fileName.indexOf(".") > 0 && !fileName.toLowerCase().contains("glyphicons") && !fileName.toLowerCase().contains("fontawesome")){
	    		  fileName = fileName.substring(0, fileName.lastIndexOf("."));
	    		 // if(!fileName.toLowerCase().startsWith("roboto")){
	    			  if(tempVar.get(fileName)==null){
	    			  cleanstring = fileName.replaceAll("[^A-Za-z0-9 ]", "").replaceAll("\\s+", "");
	    			 // defaultFonts.append(c).append(str1).append(cleanstring).append(",'https://cdn.cimm2.com/fonts/").append(fileName).append(str2).append(cleanstring.toLowerCase()).append(");");
	    			  defaultFonts.append(c).append(str1).append(cleanstring).append(",'"+fontLocationUrl).append(fileName).append(str2).append(cleanstring.toLowerCase()).append(");");
	    			  c="\n";
		    		  fontList.add(cleanstring);
		    		  tempVar.put(fileName, fileName);
	    			  }
	    		 // }
	    		 
	    	  }
	    	  
	        System.out.println( "" + i + ": " + path.getFileName() );
	        i++;
	      }
	      stream.close();
	      contentObj.put("fontList", fontList);
	      contentObj.put("defaultfonts", defaultFonts.toString());
	      long stop = System.currentTimeMillis();
	      System.out.println( "Elapsed: " + (stop - start) + " ms" );
	      renderContent = gson.toJson(contentObj);
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}

public String assetsUpload(){

	request =ServletActionContext.getRequest();
	response =ServletActionContext.getResponse();
	// TODO Auto-generated method stub
	try{
		//org.apache.struts2.ServletActionContext.setRequest(request);
		String templatePath = CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH");// CimmResources.systemParameterNameValueMap.get("BANNER_TEMPLATE_PATH");
		String warFileName = CMSConstants.getWarFileName(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES"));
		String isKubernetes = System.getenv("ECOMM_REPO");
		int siteId = CommonDBQuery.getGlobalSiteId();
		if(isKubernetes!=null){
			warFileName = "";
			templatePath = "/var/persistent/CIMM2VirtualFolder/ASSETS.war/WEB_THEMES/";
		}
		
		
		String siteName = CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
		StringBuilder fontLocation = new StringBuilder();
		String fileName = request.getHeader("X-File-Name");
		String fileType = request.getHeader("X-File-Type");
		String fileUType = request.getHeader("X-File-UType");
		System.out.println(fileType);
		if(fileType!=null && fileType.trim().contains("image") && !fileType.trim().contains("svg+xml")){
			String directoryName = "";
			if(fileUType!=null && fileUType.equalsIgnoreCase("bgimg")){
				
				
				directoryName = fontLocation.append(templatePath).append(warFileName).append("background_img/").toString();
			}else{
				
				if(isKubernetes!=null){
					warFileName = CMSConstants.getWarFileName(CommonDBQuery.getSystemParamtersList().get("CLIENT_LOGO"));
					templatePath = CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH");
				}else{
					
				}
				directoryName = fontLocation.append(templatePath).append(warFileName).append(siteId).append("/").toString();
			}
			
			
			
			
			
			File directory = new File(directoryName);
		    if (! directory.exists()){
		        directory.mkdir();
		        // If you require it to make the entire directory path including parents,
		        // use directory.mkdirs(); here instead.
		    }
		    fontLocation.append(fileName);

		}else{
			String directoryName = fontLocation.append(templatePath).append(warFileName).append(siteName).append("/fonts/").toString();
			File directory = new File(directoryName);
		    if (! directory.exists()){
		        directory.mkdir();
		        // If you require it to make the entire directory path including parents,
		        // use directory.mkdirs(); here instead.
		    }
		    fontLocation.append(fileName);
		}
		
		
		InputStream inputStream = request.getInputStream();
		System.out.println(fileName);
		File targetFile = new File(fontLocation.toString());
		 
	  FileUtils.copyInputStreamToFile(inputStream, targetFile);
		
		}
		catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
	return SUCCESS;
}

public String getBgImgList() {
	
	
	HashSet<String> imageList = new HashSet<String>();
	Gson gson = new Gson();
	try{
		LinkedHashMap<String, Object> contentObj = new LinkedHashMap<String, Object>();

		StringBuilder imgLocation = new StringBuilder();

		String templatePath = CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH");// CimmResources.systemParameterNameValueMap.get("BANNER_TEMPLATE_PATH");
		String warFileName = CMSConstants.getWarFileName(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES"));
		String bgPath = CommonDBQuery.getSystemParamtersList().get("WEB_THEMES");
		String isKubernetes = System.getenv("ECOMM_REPO");
		if(isKubernetes!=null){
			warFileName = "";
			templatePath = "/var/persistent/CIMM2VirtualFolder/ASSETS.war/WEB_THEMES/";
		}
		String siteName = CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
		imgLocation.append(templatePath).append(warFileName).append("/background_img");
		System.out.println("Font Path : " + imgLocation);
		String fileName = null;

		
	      System.out.println( "NIO run" );
	      long start = System.currentTimeMillis();
	      Path dir = FileSystems.getDefault().getPath( imgLocation.toString() );
	      DirectoryStream<Path> stream = Files.newDirectoryStream( dir );
	      for (Path path : stream) {
	    	  fileName = path.getFileName().toString();
	    	  imageList.add(fileName);
	      }
	      stream.close();
	      contentObj.put("imageList", imageList);
	      contentObj.put("bgPath", bgPath+"/background_img/");
	     
	      long stop = System.currentTimeMillis();
	      System.out.println( "Elapsed: " + (stop - start) + " ms" );
	      renderContent = gson.toJson(contentObj);
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}

public String pageHistory()
{
	request =ServletActionContext.getRequest();
	response = ServletActionContext.getResponse();
	response.setContentType("application/json");
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	Gson gson = new Gson(); 
	try {
		if(!validateCms()){
			return "unauthorized";
		}
		CmsDao cmsDao = new CmsDao();
		List<PageHistoryModel> PageHistoryList = cmsDao.getPageHistory(pageId);
		contentObject.put("pageHistoryList", PageHistoryList);
		renderContent = CMSLayoutGenerator.templateLoader("CMSPageHistory", contentObject, null, null, null);
	}catch (Exception e) {
		e.printStackTrace();
	}
	
	return SUCCESS;
}

}
