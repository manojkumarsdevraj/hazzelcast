package com.unilog.cmsmanagement.util;

import java.io.StringWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.ws.rs.HttpMethod;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.unilog.cmsmanagement.model.BannerInfoData;
import com.unilog.database.CommonDBQuery;
import com.unilog.model.form.FormData;
import com.unilog.model.widget.WidgetData;


public class BannerTemplate {
	private static BannerTemplate bannerTemplate;
	
	
	private BannerTemplate(){
		
	}
	
	public static BannerTemplate getInstance(){
		synchronized (BannerTemplate.class) {
			if(bannerTemplate==null){
				bannerTemplate = new BannerTemplate();
			}
		}
		return bannerTemplate;
	}
	
	public String generateBanner(String template,BannerInfoData bannerInfoList){
		String templateOutput = null;
		try{
			VelocityEngine velocityTemplateEngine = new VelocityEngine();
			String templatePath = CommonDBQuery.getSystemParamtersList().get("BANNER_TEMPLATE_PATH");//"D:\\BannerTemplate";// CommonDBQuery.getSystemParamtersList().get("BANNER_TEMPLATE_PATH");
			System.out.println("templatePath @ templateLoader : "+templatePath);
			velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
			velocityTemplateEngine.init();
		    Template t = velocityTemplateEngine.getTemplate(template);//TemplateEngine.getVelocityTemplateEngine().getTemplate(template.getLayoutName()+".html");
		    VelocityContext context = new VelocityContext();
		    context.put("bannerInfoList", bannerInfoList);
		    context.put("bannerImagePath", CommonDBQuery.getSystemParamtersList().get("BANNERLOGO"));
		    StringWriter writer = new StringWriter();
			t.merge(context, writer);
			templateOutput = writer.toString();
				    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return templateOutput;
	}
	
	public ArrayList<String> templateList() {
		
		ArrayList<String> templateList = new ArrayList<String>();
		try{
			String templatePath = CommonDBQuery.getSystemParamtersList().get("BANNER_TEMPLATE_PATH");// CimmResources.systemParameterNameValueMap.get("BANNER_TEMPLATE_PATH");
			int i = 1;
		      System.out.println( "NIO run" );
		      long start = System.currentTimeMillis();
		      Path dir = FileSystems.getDefault().getPath( templatePath );
		      DirectoryStream<Path> stream = Files.newDirectoryStream( dir );
		      for (Path path : stream) {
		    	  
		    	  templateList.add(path.getFileName().toString());
		        System.out.println( "" + i + ": " + path.getFileName() );
		        i++;
		      }
		      stream.close();
		      long stop = System.currentTimeMillis();
		      System.out.println( "Elapsed: " + (stop - start) + " ms" );
		}catch (Exception e) {
			e.printStackTrace();
		}
		return templateList;
	}



	public WidgetData getWidgetData(int widgetId,int siteId) {
		
		WidgetData widgetData = null;
		try{
			StringBuilder requestString = new StringBuilder();
			String url = "/admin/widget/";
			requestString.append(url);
			requestString.append(widgetId);
			Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), HttpMethod.GET, null, WidgetData.class, siteId);
			widgetData = (WidgetData) response.getData();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return widgetData;
	}

	
	public FormData getFormData(int formId,int siteId) {
		
		FormData formData = null;
		try{
			StringBuilder requestString = new StringBuilder();
			String url = "/admin/cimmUIForm/";
			requestString.append(url);
			requestString.append(formId);
			Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), HttpMethod.GET, null, FormData.class, siteId);
			formData = (FormData) response.getData();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return formData;
	}

	
	public String convertBooleanToString(Object b){
		
		if(b!=null && b instanceof Boolean ){
			Boolean b1 = (Boolean)b;
			if(b1){
				return "Y";
			}else{
				return "N";
			}
		}
		if(b != null && b instanceof String){
			if(((String)b).equalsIgnoreCase("true") || ((String)b).equalsIgnoreCase("Y")){
				return "Y";
			}
			
		}
		return "N";
	}

	public java.sql.Date convertUtilToSqlDate(java.util.Date utilDate){
		if(utilDate != null)
			return new java.sql.Date(utilDate.getTime());
		else
			return null;
	}
	
	public String encodeStringV2(String value, boolean importing){
		try {
			if(value!=null){
				byte spaceValue[]={-96};
				if(value.contains(new String(spaceValue))){
					byte replaceValue[]={32};
					value=value.replaceAll(new String(spaceValue), new String(replaceValue));
				}
				if(importing){
					value = StringEscapeUtils.escapeHtml4(value);
				}else{
					value = StringEscapeUtils.unescapeHtml4(value);
				}
				return value;
			}
			//return new String(value.getBytes(),"ISO-8859-15");
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}

