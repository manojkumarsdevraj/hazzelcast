package com.unilog.api.widget;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

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

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.api.widget.model.WidgetData;
import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class WidgetUtility {

	private static WidgetUtility widgetUtility = null;
	private static Properties props = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
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
	
	private WidgetUtility(){
		
	}
	
	public static WidgetUtility getInstance(){
		synchronized (WidgetUtility.class) {
			if(widgetUtility==null){
				widgetUtility = new WidgetUtility();
			}
		}
		return widgetUtility;
	}
	
	Gson gson = new Gson();
	public WidgetData getWidgetData(int widgetId) {
		
		WidgetData widgetData = null;
		try{
			StringBuilder requestString = new StringBuilder();
			String url = "/admin/widget/";
			requestString.append(url);
			requestString.append(widgetId);
			Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), HttpMethod.GET, null, WidgetData.class);
			widgetData = (WidgetData) response.getData();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return widgetData;
	}
	
	public String generateWidget(int widgetId) {
		String templateOutput = null;
		try{
			request =ServletActionContext.getRequest();
			response = ServletActionContext.getResponse();
			HttpSession session = request.getSession();
			String cimm2bcUrl = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BCENTRAL_ACCESS_URL"));
			Map<String,Object> widgetTypeObj = null;
			Type listType = new TypeToken<HashMap<String,Object>>(){}.getType();
			WidgetData widgetData = getWidgetData(widgetId);
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
			String url = getUrl(widgetType);
			ToolManager velocityToolManager = new ToolManager();
	    	VelocityContext context = new VelocityContext(velocityToolManager.createContext());
	    	VelocityEngine velocityTemplateEngine = new VelocityEngine();
	        String templatePath = CommonDBQuery.getSystemParamtersList().get("SITE_TEMPLATE_PATH")+CommonDBQuery.getSystemParamtersList().get("SITE_NAME")+CommonDBQuery.getSystemParamtersList().get("MACROS_FOLDER");
	        velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
	        velocityTemplateEngine.setProperty("velocimacro.library.autoreload", true);
	        velocityTemplateEngine.setProperty("velocimacro.library", "defaultMacro.vm");
	        velocityTemplateEngine.init();
			StringWriter writer = new StringWriter();
			context.put("CIMM2VelocityTool", CIMM2VelocityTool.getInstance());
			context.put("CIMMUtility", CIMM2VelocityTool.getInstance());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        context.put("numberTool", new NumberTool());
	        context.put("escapeTool", new EscapeTool());
	        context.put("math", new MathTool());
	        context.put("dispalyTool", new DisplayTool());
	        context.put("convert", new ConversionTool());
	        context.put("dateTool", new ComparisonDateTool());
	        context.put("timeStamp",timestamp.getTime());
			String pricePrecision = "";
		    String pricePrecisionFormate = "";
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
		    String refreshVersion = "rv="+session.getId();
			context.put("refreshVersion",CommonUtility.validateString(refreshVersion));
		    context.put("webThemes", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES")));
		    context.put("siteName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_NAME")));
		    context.put("siteId", CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
		    context.put("siteDisplayName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")));
		    context.put("thumbNail", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("THUMBNAIL")));
		    context.put("itemImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMIMAGE")));
		    context.put("detailImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DETAILIMAGE")));
		    context.put("enlargedImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENLARGEIMAGE")));
		    context.put("productImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRODUCTIMAGE")));
		    context.put("productmodethumbNail", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRODUCTMODETHUMBNAIL")));
		    context.put("productmodeimage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRODUCTMODEIMAGE")));
		    context.put("productmodeenlargedImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRODUCTMODEENLARGEDIMAGE")));
		    context.put("taxonomyImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TAXONOMYIMAGEPATH")));
		    context.put("documentPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DOCUMENTS")));
		    context.put("brandLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRANDLOGO")));
		    context.put("buyingCompanyLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BUYINGCOMPANYLOGO")));
		    context.put("bannerLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BANNERLOGO")));
		    context.put("manufacturerLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MNF_IMAGES")));
		    context.put("pricePrecision", CommonUtility.validateString(pricePrecision));
		    context.put("pricePrecisionFormate", CommonUtility.validateString(pricePrecisionFormate));
		    context.put("cdnUrl", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CDN_URL")));
		    if(session.getAttribute("userLogin")!=null){
		    	context.put("userLogin", (Boolean) session.getAttribute("userLogin"));
		    }
		    if(session.getAttribute("userEmailAddress")!=null){
		    	context.put("userEmailAddress", CommonUtility.validateString((String) session.getAttribute("userEmailAddress")));
		    }
			if(session.getAttribute("localeCode")!=null ){
		    	context.put("locale",LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()));
		    	context.put("localeLang",(String)session.getAttribute("sessionLocale"));
		    }else{
		    	context.put("locale",LayoutLoader.getMessageProperties().get("EN"));
		    	context.put("localeLang","1_en");
		    }
			if(!CommonUtility.validateString(url).equalsIgnoreCase("")){
				if(!url.startsWith("http")){
					url = cimm2bcUrl+url;
				}
				String output = "";
				if(url.contains("http://ecommerce:9443")) {
					output = Cimm2BCentralClient.getInstance().getJsonResponseSession(url, HttpMethod.GET, null, session);
				}else {
					output = Cimm2BCentralClient.getInstance().getJsonResponse(url, HttpMethod.GET, null);
				}
				
				Map<String,Object> convertedMap = gson.fromJson(output, listType);
				Map<String,Object> list = (Map<String, Object>) convertedMap.get("data");
			    context.put("data", list);
			}
			velocityTemplateEngine.evaluate(context, writer, "", widgetData.getTemplateCode());
			templateOutput = writer.toString();
			System.out.println(templateOutput);
			//requestString.append(widgetId);
			//Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), HttpMethod.GET, null, DynamicModel.class, siteId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return templateOutput;
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
		FileInputStream fis = null;
		try{
			String propertiesFilePath = CommonDBQuery.getSystemParamtersList().get("WIDGET_PROPERTIES_FILE");//"D:/BannerTemplate/widget.properties";
			fis = new FileInputStream(propertiesFilePath);
			props = new Properties();
			props.load(fis);
			if(fis!=null) {
				fis.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(fis!=null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void setProps(Properties props) {
		WidgetUtility.props = props;
	}

	public static Properties getProps() {
		if(props==null)
			loadProperties();
		//System.out.println("URL : " + props.getProperty("URL"));
		return props;
	}
}
