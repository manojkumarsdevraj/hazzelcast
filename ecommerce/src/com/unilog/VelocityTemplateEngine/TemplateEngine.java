package com.unilog.VelocityTemplateEngine;

import org.apache.velocity.app.VelocityEngine;

import com.unilog.database.CommonDBQuery;

public class TemplateEngine {

	private static VelocityEngine velocityTemplateEngine;
	
	static
	{
		initiateTemplateEngine();
	}
	
	public static void initiateTemplateEngine()
	{
		try
		{
			
			//HttpServletRequest request = ServletActionContext.getRequest();
			velocityTemplateEngine = new VelocityEngine();
			String templatePath = CommonDBQuery.getSystemParamtersList().get("SITE_TEMPLATE_PATH")+CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
			System.out.println("templatePath : "+templatePath);
			velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
			velocityTemplateEngine.init();
		}
		catch (Exception e) {
			
			e.printStackTrace();
			
		}
	}

	public static void setVelocityTemplateEngine(VelocityEngine velocityTemplateEngine) {
		TemplateEngine.velocityTemplateEngine = velocityTemplateEngine;
	}

	public static VelocityEngine getVelocityTemplateEngine() {
		return velocityTemplateEngine;
	}
	
	
}
