package com.unilog.cmsmanagement.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.unilog.cmsmanagement.dao.CmsDao;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.model.CriteriaIntValue;
import com.unilog.model.PromotionIntRequest;
import com.unilog.model.SystemParameterModel;
import com.unilog.model.ThemeModel;
import com.unilog.model.Criteria;
import com.unilog.model.PromotionRequest;
import com.unilog.model.Sort;
import com.unilog.utility.CommonUtility;


public class CMSUtility {
	private static CMSUtility cmsUtility;
	private static Properties props = null;
	private static LinkedHashMap<String, String> cmsAuthData = null;
	
	public static LinkedHashMap<String, String> getCmsAuthData() {
		return cmsAuthData;
	}

	public static void setCmsAuthData(LinkedHashMap<String, String> cmsAuthData) {
		CMSUtility.cmsAuthData = cmsAuthData;
	}
	
	static {
		cmsAuth();
	}
	
public static void cmsAuth(){

		
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			cmsAuthData = new LinkedHashMap<>();
			conn = ConnectionManager.getDBConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM cms_auth";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				cmsAuthData.put(rs.getString("DOMAIN"), rs.getString("ROLE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
	
	}

	public static CMSUtility getInstance(){
		synchronized (CMSUtility.class) {
			if(cmsUtility==null){
				cmsUtility = new CMSUtility();
			}
		}
		return cmsUtility;
	}
	
	public String getUrl(String widgetType){
		String url = null;
		try{
			url = getProps().getProperty(widgetType);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	
	public static void loadProperties(){
		try{
			String propertiesFilePath = CommonDBQuery.getSystemParamtersList().get("WIDGET_PROPERTIES_FILE");
			FileInputStream fis = new FileInputStream(propertiesFilePath);
			props = new Properties();
			props.load(fis);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public String writeCssFile(ThemeModel themeModel){
		FileWriter fileWriter = null;
		String message = null;
		try{
			 StringBuilder folderPath = new StringBuilder();
			String templatePath = CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH");// CimmResources.systemParameterNameValueMap.get("BANNER_TEMPLATE_PATH");
			String warFileName = CMSConstants.getWarFileName(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES"));
			String isKubernetes = System.getenv("ECOMM_REPO");  
			if(isKubernetes!=null){
				warFileName = "";
				templatePath = "/var/persistent/CIMM2VirtualFolder/ASSETS.war/WEB_THEMES/";
			}
			String siteName = CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
			folderPath.append(templatePath).append(warFileName).append(siteName).append("/css/");
			boolean createFile = false;
			System.out.println("CSS file path : "+folderPath.toString()+ themeModel.getCssName());
			File file = new File(folderPath.toString()+ themeModel.getCssName());
			createFile = file.createNewFile();
			
		
				fileWriter = new FileWriter(file);
				System.out.println(file);
				fileWriter.write(themeModel.getCss());
				fileWriter.close();
			
			message = "success";
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return message;
	}
	
	public String setHomePage(int pageId,int userId,int siteId,String dlink){
		try{
			boolean isDlink =false;
			if(dlink!=null && dlink.trim().equalsIgnoreCase("Y")){
				isDlink = true;
			}
			int created = 0;
			SystemParameterModel systemParametersInfo = new SystemParameterModel();
			systemParametersInfo.setConfigKey("GET_HOMEPAGE_FROM_CMS");
			if(isDlink){
				systemParametersInfo.setConfigValue("N");
			}else{
				systemParametersInfo.setConfigValue("Y");
			}
			
			systemParametersInfo.setCategory("GENERAL");
			systemParametersInfo.setFieldType("CB");
			
			LinkedHashMap<String, String> basicConfigs = CommonDBQuery.checkMultiSite();
			if (CommonUtility.validateString(basicConfigs.get("MULTI_SITE")).equalsIgnoreCase("Y")) {
				int systemParamId = 0; 
				if(CommonDBQuery.getSystemParameterConfigKeyAndParamIdList()!=null) {
					systemParamId = CommonUtility.validateNumber(CommonDBQuery.getSystemParameterConfigKeyAndParamIdList().get("GET_HOMEPAGE_FROM_CMS"));
				}
				created = CmsDao.addUpdateSystemParameterMultiSite(systemParametersInfo,userId,siteId,systemParamId);
				if(created == 1){
					if(CommonDBQuery.getSystemParameterConfigKeyAndParamIdList()!=null) {
						systemParamId = CommonUtility.validateNumber(CommonDBQuery.getSystemParameterConfigKeyAndParamIdList().get("CMS_HOMEPAGE_ID"));
					}
					SystemParameterModel systemParametersInfoForStPage = new SystemParameterModel();
					systemParametersInfoForStPage.setConfigKey("CMS_HOMEPAGE_ID");
					if(isDlink){
					systemParametersInfoForStPage.setConfigValue(0);
					}else{
						systemParametersInfoForStPage.setConfigValue(pageId);
					}
					systemParametersInfoForStPage.setCategory("GENERAL");
					systemParametersInfoForStPage.setFieldType("ITB");
					created = CmsDao.addUpdateSystemParameterMultiSite(systemParametersInfoForStPage,userId,siteId,systemParamId);
				}
			} else {
				created = CmsDao.addUpdateSystemParameter(systemParametersInfo,userId,siteId);
				if(created == 1){
					SystemParameterModel systemParametersInfoForStPage = new SystemParameterModel();
					systemParametersInfoForStPage.setConfigKey("CMS_HOMEPAGE_ID");
					if(isDlink){
					systemParametersInfoForStPage.setConfigValue(0);
					}else{
						systemParametersInfoForStPage.setConfigValue(pageId);
					}
					systemParametersInfoForStPage.setCategory("GENERAL");
					systemParametersInfoForStPage.setFieldType("ITB");
					created = CmsDao.addUpdateSystemParameter(systemParametersInfoForStPage,userId,siteId);
				}
			}

			
		
			CmsDao.setasHomePageIcon(pageId,isDlink,siteId);
		
		}catch (Exception e) {
			e.printStackTrace();
			
		}
	
		return null;
	}
	
	 public PromotionRequest buildSearchRequest(String keyWord,int page,int pageSize,String columnName, String status, String searchClauses)
	  {
	    PromotionRequest pro = new PromotionRequest();
	    try
	    {
	     if(CommonUtility.validateString(searchClauses).equalsIgnoreCase("")) {
	    	  searchClauses = "CONTAINS_IGNORE_CASE";
	      }
	      pro.setPage(page);
	      pro.setPageSize(pageSize);
//	      pro.setStatus(status);
	      ArrayList<Criteria> criteria = new ArrayList<Criteria>();
	      Criteria c = new Criteria();
	      c.setName("status");
	      status ="A,Y";
	      if(CommonDBQuery.getSystemParamtersList().get("SHOWINACTIVEPAGE")!=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHOWINACTIVEPAGE")).equalsIgnoreCase("Y")) {
	    	  status ="A,Y,N";
	      }
	      String[] statusArr = status.split(",");
	      c.setValues(statusArr);
	      c.setClause("IN");
	      c.setApplyAnd(true);
	      criteria.add(c);
	      pro.setCriteria(criteria);
	      if(!CommonUtility.validateString(keyWord).equalsIgnoreCase("ALL")) {
	    	  c = new Criteria();
		      c.setName(columnName);
		      c.setValue(keyWord);
		      c.setClause(searchClauses);
		      c.setApplyAnd(true);
		      criteria.add(c);
		      pro.setCriteria(criteria);
		      // pro.setSort(sortList);
	      }
	    } catch (Exception e) {
	     
	      e.printStackTrace();
	    }
	    return pro;
	  }
	
	 public PromotionIntRequest buildIntegerSearchRequest(int keyWord,int page,int pageSize,String columnName, String status, String searchClauses)
	  {
		 PromotionIntRequest pro = new PromotionIntRequest();
	    try
	    {
	      if(CommonUtility.validateString(searchClauses).equalsIgnoreCase("")) {
	    	  searchClauses = "CONTAINS_IGNORE_CASE";
	      }
	      pro.setPage(page);
	      pro.setPageSize(pageSize);
	      pro.setStatus(status);
	      ArrayList<CriteriaIntValue> criteria = new ArrayList<CriteriaIntValue>();
	      
	    	  CriteriaIntValue c = new CriteriaIntValue();
		      c.setName(columnName);
		      c.setValue(keyWord);
		      c.setClause(searchClauses);
		      c.setApplyAnd(true);
		      criteria.add(c);
		      pro.setCriteria(criteria);
		      // pro.setSort(sortList);
	    
	    } catch (Exception e) {
	     
	      e.printStackTrace();
	    }
	    return pro;
	  } 
	 
	public Properties getWidgetTypes() {
		Properties output = null;
		try{
			output = CMSUtility.getProps();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public static Properties getProps() {
		if(props==null)
			loadProperties();
		//System.out.println("URL : " + props.getProperty("URL"));
		return props;
	}

	public static void setProps(Properties props) {
		CMSUtility.props = props;
	}

	
	 public PromotionRequest buildSortRequest(boolean sortOrder,int page,int pageSize,String columnName, String status)
	  {
	    PromotionRequest pro = new PromotionRequest();
	    try
	    {
	      pro.setPage(page);
	      pro.setPageSize(pageSize);
//	      pro.setStatus(status);
	      ArrayList<Criteria> criteria = new ArrayList<Criteria>();
	      Criteria c = new Criteria();
	      c.setName("status");
	      status ="A,Y";
	      if(CommonDBQuery.getSystemParamtersList().get("SHOWINACTIVEPAGE")!=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHOWINACTIVEPAGE")).equalsIgnoreCase("Y")) {
	    	  status ="A,Y,N";
	      }
	      String[] statusArr = status.split(",");
	      c.setValues(statusArr);
	      c.setClause("IN");
	      c.setApplyAnd(true);
	      criteria.add(c);
	      pro.setCriteria(criteria);
	      
	      ArrayList<Sort> sortp = new ArrayList<Sort>();
	      Sort s= new Sort();
	      s.setName(columnName);
	      s.setAscending(sortOrder);
	      s.setIgnoreCase(true);
	     
	      sortp.add(s);
	      pro.setSort(sortp);
	     // pro.setSort(sortList);
	     
	    } catch (Exception e) {
	     
	      e.printStackTrace();
	    }
	    return pro;
	  }
	 
	 public String getJsonRequest(HttpServletRequest request){
			String jsonRequest = "";
			try{
				InputStream requestInputStream = request.getInputStream();
				BufferedReader reader =  new BufferedReader(new InputStreamReader(requestInputStream));
				 StringBuilder out = new StringBuilder();
			        String inLine;
			        while ((inLine = reader.readLine()) != null) {
			            out.append(inLine);
			        }
			        reader.close();
				jsonRequest =  out.toString();
			}catch (Exception e) {
				e.printStackTrace();// TODO: handle exception
			}
			return jsonRequest;
		}
	 
	 public String validateCms(String host){
		 String cmsRole = null;
		 try{
			 cmsRole = cmsAuthData.get(host);
		 }catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		 return cmsRole;
	 }

}
