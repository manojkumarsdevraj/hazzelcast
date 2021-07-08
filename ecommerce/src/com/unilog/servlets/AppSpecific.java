package com.unilog.servlets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.database.CommonDBQuery;

public class AppSpecific extends ActionSupport{
	private static final long serialVersionUID = 8190151879990449869L;
	private String renderContent;
	
	
	
	
	public String firstHit(){
		Gson test = new Gson();
		renderContent = test.toJson(CommonDBQuery.getSystemParamtersList());
		return SUCCESS;
	}
	public String appLaunch(){
		renderContent = CommonDBQuery.getSystemParamtersList().get("AppReleaseDate");
		return SUCCESS;
	}
	public String getRenderContent() {
		return renderContent;
	}
	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}

}
