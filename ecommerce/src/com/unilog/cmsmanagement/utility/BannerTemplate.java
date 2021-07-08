package com.unilog.cmsmanagement.utility;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.unilog.cmsmanagement.model.BannerInfoData;
import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

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
			HttpServletRequest request =ServletActionContext.getRequest();
			//HttpSession session = request.getSession();
			//String browseType = (String)session.getAttribute("browseType");
			System.out.println(CommonUtility.validateString(request.getHeader("User-Agent")));
			VelocityEngine velocityTemplateEngine = new VelocityEngine();
			String templatePath = CommonDBQuery.getSystemParamtersList().get("BANNER_TEMPLATE_PATH");
			System.out.println("templatePath @ templateLoader : "+templatePath);
			velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
			velocityTemplateEngine.init();
		    Template t = velocityTemplateEngine.getTemplate(template);//TemplateEngine.getVelocityTemplateEngine().getTemplate(template.getLayoutName()+".html");
		    VelocityContext context = new VelocityContext();
		    context.put("bannerInfoList", bannerInfoList);
		    context.put("bannerImagePath", CommonDBQuery.getSystemParamtersList().get("BANNERLOGO"));
		    /*if(CommonUtility.validateString(browseType).equalsIgnoreCase("PHONE")) {
		    	context.put("bannerImagePath", context.get("bannerImagePath")+CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FOLDER_PATH_DEVICE")));//-"MOBILE/"
		    }*/
		    StringWriter writer = new StringWriter();
			t.merge(context, writer);
			templateOutput = writer.toString();
				    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return templateOutput;
	}
}