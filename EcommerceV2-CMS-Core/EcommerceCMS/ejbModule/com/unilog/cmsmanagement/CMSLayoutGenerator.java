package com.unilog.cmsmanagement;

import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.ConversionTool;
import org.apache.velocity.tools.generic.DisplayTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.apache.velocity.tools.generic.SortTool;
import org.apache.velocity.tools.view.tools.LinkTool;

import com.unilog.VelocityTemplateEngine.ContentLayout;
import com.unilog.VelocityTemplateEngine.HeaderLayout;

import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.VelocityTemplateEngine.LeftLayout;
import com.unilog.VelocityTemplateEngine.RightLayout;
import com.unilog.VelocityTemplateEngine.TemplateModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.datasmart.DataSmartController;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsAction;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.utility.CommonUtility;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class CMSLayoutGenerator {
	public static String templateLoader(String layoutName,LinkedHashMap<String, Object> contentObject,LinkedHashMap<String, Object> leftContentObject, LinkedHashMap<String, Object> rightContentObject,LinkedHashMap<String, Object> footerContentObject)
	{
		String templateOutput = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try
		{
			String browseType = "";
			String siteName = "";
			String browseString = (String) session.getAttribute("browseType");
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			if(browseString!=null && browseString.trim().equalsIgnoreCase("Mobile")){
				browseType = "Mobile";
			}else{
				browseType = "";
			}
			
			if(contentObject!=null && contentObject.get("selectedSiteName")!=null && CommonUtility.validateString(contentObject.get("selectedSiteName").toString()).length()>0){
				siteName = CommonUtility.validateString(contentObject.get("selectedSiteName").toString())+browseType;
		    }else {
		    	siteName = CommonDBQuery.getSystemParamtersList().get("SITE_NAME")+browseType;
		    }
			//siteName = CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
			System.out.println("siteName @ templateLoader : "+siteName);
			TemplateModel template = null;
			LinkedHashMap<String, TemplateModel> layoutList = CmsLayoutLoader.getLayoutModel().getLayoutList();
			
			if(layoutList!=null)
			{
				template =  new TemplateModel();
				template = layoutList.get(layoutName);
			}
			
			if(template!=null)
			{
				System.out.println("Mood name at Loader : " + CommonDBQuery.getSystemParamtersList().get("MOOD_NAME"));
				
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
				
				VelocityEngine velocityTemplateEngine = new VelocityEngine();
				String templatePath = CommonDBQuery.getSystemParamtersList().get("SITE_TEMPLATE_PATH")+CommonDBQuery.getSystemParamtersList().get("SITE_NAME")+browseType;
				System.out.println("templatePath @ templateLoader : "+templatePath);
				
				velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
				velocityTemplateEngine.init();
				
				System.out.println(template.getLayoutName());
			    Template t = velocityTemplateEngine.getTemplate(template.getLayoutName()+".html");//TemplateEngine.getVelocityTemplateEngine().getTemplate(template.getLayoutName()+".html");
			    
			    	//ToolManager velocityToolManager = new ToolManager();
			    	//velocityToolManager.configure("velocity-tools.xml");
			    	//VelocityContext context = new VelocityContext(velocityToolManager.createContext());
			    	VelocityContext context = new VelocityContext();
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
			    	context.put(Integer.class.getSimpleName(), Integer.class);
			    	context.put("session", session);
			    	context.put("CIMMUtility", CIMM2VelocityTool.getInstance());
			    	context.put("CommonDBQuerySystemParameter", CommonDBQuery.getSystemParamtersList());
			    	
			    	String protocol = request.getScheme();
			    	context.put("hostName", protocol+"://"+request.getServerName()+"/");
			    	
			    	context.put("moodname", CommonDBQuery.getSystemParamtersList().get("MOOD_NAME"));
			    	
			    if(session.getAttribute("localeCode")!=null ){
			    	context.put("locale",LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()));
			    	context.put("localeLang",(String)session.getAttribute("sessionLocale"));
			    }else{
			    	context.put("locale",LayoutLoader.getMessageProperties().get("EN"));
			    	context.put("localeLang","1_en");
			    	session.setAttribute("localeCode","EN");
			    	session.setAttribute("sessionLocale","1_en");
			    }
			    
			    HeaderLayout.generateHeaderContent(context);
			    
			    if(template.getLeftContent()!=null)
			    {
			    	LeftLayout.generateLeftContent(context,leftContentObject);
			    	if(leftContentObject!=null && leftContentObject.size()>0 && leftContentObject.get("leftContent")!=null && leftContentObject.get("leftContent").toString().trim().length()>0){
			    		context.put("leftContent", leftContentObject.get("leftContent").toString());
			    	}else{
			    		context.put("leftContent", template.getLeftContent());
			    	}
			    	
			    }
			    
			    if(template.getBodyContent()!=null)
			    {
			    	ContentLayout.generateBodyContent(context,contentObject);
			    	context.put("bodyContent", template.getBodyContent());
			    }
			    
			    if(template.getRightContent()!=null)
			    {
			    	context.put("rightContent", RightLayout.generateRightContent(template.getRightContent(),rightContentObject));
			    }
			    if(contentObject!=null && contentObject.get("pageTypeReq")!=null && contentObject.get("pageTypeReq").toString().trim().equalsIgnoreCase("staticPage")){
			    	System.out.println("******************************CIMM Touch Static Page****************************************");
			    	if(contentObject.get("headerHtml")!=null && !contentObject.get("headerHtml").toString().isEmpty()){
			    		context.put("headerHtml", contentObject.get("headerHtml").toString());
			    	}
			    	if(contentObject.get("footerHtml")!=null && !contentObject.get("footerHtml").toString().isEmpty()){
			    		context.put("footerHtml", contentObject.get("footerHtml").toString());
			    	}
			    	
			    }
			    ProductsAction productsAction = new ProductsAction();
			    context = productsAction.getCartCount(context,request);
			    if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_ARI_API")).equalsIgnoreCase("Y")){
			    	context.put("ARIData", DataSmartController.getInstance());
			    }
			    if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID")).length()>0){
			    	String sessionId = session.getId();
					String[] brandIDList = CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID").split(",");
					boolean isExcludeBranch = false;
					if(brandIDList!=null){
						for(String brandId:brandIDList){
							if(userId>1){
								isExcludeBranch = ProductsDAO.getCartBrandExcludeStatus(userId,null,Integer.parseInt(brandId),"cart");	
							}else{
								isExcludeBranch = ProductsDAO.getCartBrandExcludeStatus(userId,sessionId,Integer.parseInt(brandId),"cart");
							}
							
							if(isExcludeBranch){
								break;
							}
						}
						if(isExcludeBranch){
							session.setAttribute("isWillCallExclude", "Y");
						}else{
							session.setAttribute("isWillCallExclude", "N");
						}
					}
				}
			    
			    String buyingCompanyId = (String)session.getAttribute("buyingCompanyId");
			    if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getGroupNameEverywhere")).equalsIgnoreCase("Y")&& userId>1){
			    	contentObject = ProductsDAO.getSavedGroupNameByUserIdDao(userId, contentObject, CommonUtility.validateNumber(buyingCompanyId));
			    	String advancedProductGroup = CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP");
			    	if(!CommonUtility.validateString(advancedProductGroup).equalsIgnoreCase("Y")){
			    		context.put("groupListData", contentObject.get("groupListData"));
			    	}
			    	context.put("savedCartData", contentObject.get("savedCartData"));
		        	context.put("approveCartData", contentObject.get("approveCartData"));
			    }
			    if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SET_HOMEBRANCH_DETAILS")).equalsIgnoreCase("Y")&& userId>1){
			    	String homeBranchId = (String) session.getAttribute("homeBranchId");
					LinkedHashMap<String,ProductsModel> branchDetail = CommonDBQuery.branchDetailData;
					ProductsModel branch = branchDetail.get(homeBranchId);
					context.put("homeBranchDetails",branch);
				}

			    if(contentObject!=null && contentObject.get("selectedSiteId")!=null && CommonUtility.validateNumber(CommonUtility.validateString(contentObject.get("selectedSiteId").toString()))>0){
			    	context.put("siteId", CommonUtility.validateNumber(CommonUtility.validateString(contentObject.get("selectedSiteId").toString())));
			    }else {
				    context.put("siteId", CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
			    }
			    
			    String validateCms = System.getenv("VALIDATE_CMS");
				if (CommonUtility.validateString(validateCms).length() > 0) {
					context.put("siteName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_NAME")));
				}else{
					if(contentObject!=null && contentObject.get("selectedSiteName")!=null && CommonUtility.validateString(contentObject.get("selectedSiteName").toString()).length()>0){
				    	 context.put("siteName", CommonUtility.validateString(contentObject.get("selectedSiteName").toString()));
				    }else {
				    	context.put("siteName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_NAME")));
				    }
				}
			    
			    
			    String cdnUrl = System.getenv("CDN_URL");
				if(CommonUtility.validateString(cdnUrl).length()>0) {
					context.put("cdnUrl",cdnUrl );	
				}
				String refreshVersion = "rv="+session.getId();
				context.put("refreshVersion",CommonUtility.validateString(refreshVersion));
			    context.put("siteDisplayName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")));
			    context.put("LayoutName", CommonUtility.validateString(layoutName));
			    context.put("webThemes", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES")));
			    context.put("internationalUser", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_INTERNATIONAL_USER")));
			    context.put("eclipseIsDownMessage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ECLIPSEISDOWNMESSAGE")));
			    context.put("eclipseDownCartMessage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNCARTMESSAGE")));
			    context.put("POValidStatus", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_PO_MANDATORY")));
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
			    context.put("siteLogo",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("siteLogo")));
			    if(session.getAttribute("userLogin")!=null){
			    	context.put("userLogin", (Boolean) session.getAttribute("userLogin"));
			    }
			    if(session.getAttribute("userEmailAddress")!=null){
			    	context.put("userEmailAddress", CommonUtility.validateString((String) session.getAttribute("userEmailAddress")));
			    }
			    
			    
			    StringWriter writer = new StringWriter();
			    t.merge(context, writer);
			    templateOutput = writer.toString();
			    //System.out.println("templateOutput @ templateLoader : "+templateOutput);
			}
			else
			{
				 VelocityContext context = new VelocityContext();
				   // VelocityContext context = new VelocityContext();
				    context.put("session", session);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return templateOutput;
	}
}
