package com.unilog.cmsmanagement;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.cmsmanagement.model.BannerInfoData;
import com.unilog.cmsmanagement.utility.BannerTemplate;
import com.unilog.utility.CommonUtility;

public class CmsManagementAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String renderContent;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String siteId;
	private int bannerListId;
	
	
	
	public int getBannerListId() {
		return bannerListId;
	}
	public void setBannerListId(int bannerListId) {
		this.bannerListId = bannerListId;
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
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String bannerTemplate(){
		
		try{
			Gson gson = new Gson();
			LinkedHashMap<String, Object> bannerTemplateObj = new LinkedHashMap<String, Object>();
			String bannerTemplate = "";
			String templateName = "jssortemplate.html";
			BannerInfoData bannerListData = null;
			StringBuilder requestString = new StringBuilder();
			String url = "/admin/bannerList/search/";
			requestString.append(url);
			requestString.append(bannerListId).append("?");
			requestString.append("reload=false");
			Cimm2BCentralResponseEntity response  = Cimm2BCentralClient.getInstance().getDataObject(requestString.toString(), HttpMethod.GET, null, BannerInfoData.class);
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
			bannerTemplateObj.put("id", bannerListData.getId());
			bannerTemplateObj.put("status", bannerListData.getStatus());
			bannerTemplateObj.put("bannerListName", bannerListData.getBannerListName());
			bannerTemplateObj.put("startDate", bannerListData.getStartDate());
			bannerTemplateObj.put("expiryDate", bannerListData.getExpiryDate());
			bannerTemplateObj.put("siteId", siteId);
			bannerTemplateObj.put("dynamicProperties", dynamicProperties);
			bannerTemplateObj.put("bannerTemplate", bannerTemplate);
			
			renderContent = gson.toJson(bannerTemplateObj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

}
