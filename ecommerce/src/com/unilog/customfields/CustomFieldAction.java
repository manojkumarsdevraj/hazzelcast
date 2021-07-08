package com.unilog.customfields;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.database.CommonDBQuery;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.WordPressMenuModel;

public class CustomFieldAction extends ActionSupport{


	private static final long serialVersionUID = 6592270966760417537L;
	private String renderContent;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String entityId;
	
	
	
	
	
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
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getEntityId() {
		return entityId;
	}
	
	
	
	
	public String customTableField(){
		
		request =ServletActionContext.getRequest();
		String result = "";
		try {
			
			String customFieldEntityType = CommonUtility.validateString(request.getParameter("EntityType")); // ITEM / BRAND / WEBSITE
			int customFieldEntityId = CommonUtility.validateNumber(request.getParameter("EntityId")); // ITEM_ID / BRAND_ID / SITE_ID
			String customFieldName = CommonUtility.validateString(request.getParameter("CustomFieldName")); // Custom Field name
			String CustomFieldResultType = CommonUtility.validateString(request.getParameter("ResultType")); // Result type JSON in this case
			
			CustomModel modelObj = new CustomModel();
			modelObj.setCustomFieldEntityType(customFieldEntityType);
			modelObj.setCustomFieldEntityId(customFieldEntityId);
			modelObj.setCustomFieldName(customFieldName);
			modelObj.setCustomFieldResultType(CustomFieldResultType);
			if(CommonUtility.validateString(customFieldEntityType).length()>0){
				CustomFieldDAO daoObj = new CustomFieldDAO();
				result = CommonUtility.validateString(daoObj.getCustomFieldTableDetailsInJson(modelObj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderContent = result;
		return "ResultLoader";
	}
	
	
	public String navigationMenu(){
		
		try{
			request =ServletActionContext.getRequest();
			response = ServletActionContext.getResponse();
			String menuType = request.getParameter("menutype");
			response.setContentType("application/json");
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		    int subsetId = CommonUtility.validateNumber(tempSubset);
		    ArrayList<WordPressMenuModel> wpMenuNew = null;
		    ArrayList<WordPressMenuModel> wpMenuNewAll = new ArrayList<WordPressMenuModel>();
		    Gson gson = new Gson();
		 if(!CommonUtility.validateString(menuType).trim().equalsIgnoreCase("")){
			LinkedHashMap<Integer, ArrayList<WordPressMenuModel>> wpMenuList = MenuAndBannersDAO.getWpStaticMenu().get(menuType);
			renderContent = gson.toJson(wpMenuList);
			
		 }else{
			 String searchIndex = "PH_SEARCH_"+subsetId;
			 if(generalSubset>0 && generalSubset!=subsetId)
				 searchIndex = "PH_SEARCH_"+generalSubset+"_"+subsetId;
			 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
				 searchIndex="PH_SEARCH_ALL";
				}
			 wpMenuNew = new ArrayList<WordPressMenuModel>();
			 wpMenuNew.clear();
			 wpMenuNew = MenuAndBannersDAO.getWpMenu().get(searchIndex);
			 wpMenuNewAll.addAll(wpMenuNew);
			 if(MenuAndBannersDAO.getWpMenuStatic().get("TopMenu")!=null)
				 wpMenuNewAll.addAll(MenuAndBannersDAO.getWpMenuStatic().get("TopMenu"));
			 renderContent = gson.toJson(wpMenuNewAll);
			 
		 }
		 	
		    response.getWriter().write(renderContent);			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public String rollerNavigationMenu(){
		
		try{
			request =ServletActionContext.getRequest();
			response = ServletActionContext.getResponse();
			String menuType = request.getParameter("menutype");
			response.setContentType("application/json");
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		    int subsetId = CommonUtility.validateNumber(tempSubset);
		    ArrayList<WordPressMenuModel> wpMenu = null;
		    Gson gson = new Gson();
		    String searchIndex = "PH_SEARCH_"+subsetId;
			 if(generalSubset>0 && generalSubset!=subsetId)
				 searchIndex = "PH_SEARCH_"+generalSubset+"_"+subsetId;
			 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
				 searchIndex="PH_SEARCH_ALL";
				}
			if(CommonUtility.validateString(menuType).trim().equalsIgnoreCase("topnav")){
				renderContent = gson.toJson(MenuAndBannersDAO.getTopMenuListBySubset().get(searchIndex));
			}else if(CommonUtility.validateString(menuType).trim().equalsIgnoreCase("topstatic")){
				 if(MenuAndBannersDAO.getWpMenuStatic().get("TopMenu")!=null)
				renderContent = gson.toJson(MenuAndBannersDAO.getWpStaticMenu().get("TopMenu"));
			}else if(CommonUtility.validateString(menuType).trim().equalsIgnoreCase("footerstatic")){
				 if(MenuAndBannersDAO.getWpMenuStatic().get("Footer")!=null)
						renderContent = gson.toJson(MenuAndBannersDAO.getWpStaticMenu().get("Footer"));
			}else{
				 renderContent = gson.toJson(MenuAndBannersDAO.getAllSecMenuList().get(searchIndex));
			}
			
		 	
		    response.getWriter().write(renderContent);			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String modelLookUp(){
			 
		
		 renderContent = LayoutGenerator.templateLoader("ModelLookUpPage", null , null, null, null); //CategoryPage
		return "ResultLoader"; 
	}
	
	public String modelLookUpDetail(){
		
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		 
		 contentObject.put("entityId", entityId);
		 renderContent = LayoutGenerator.templateLoader("ModelLookUpDetailPage", contentObject , null, null, null); //CategoryPage
		 
		return "ResultLoader";
	}

	

}
