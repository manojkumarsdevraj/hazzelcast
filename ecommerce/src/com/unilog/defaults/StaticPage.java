package com.unilog.defaults;

import java.io.File;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolManager;
import org.jdom.input.SAXBuilder;

import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsModel;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.model.LocaleModel;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class StaticPage extends ActionSupport {

	private static final long serialVersionUID = 2121415267712163684L;
	private String renderContent;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int pId;
	private ArrayList<MenuAndBannersModal> topBanners = new ArrayList<>();
	private ArrayList<MenuAndBannersModal> rightBanners = new ArrayList<>();
	private ArrayList<MenuAndBannersModal> leftBanners = new ArrayList<>();
	private ArrayList<MenuAndBannersModal> bottomBanners = new ArrayList<>();
	private ArrayList<MenuAndBannersModal> subMenuList = null;
	private ArrayList<ProductsModel> breadCrumbList = null;
	private String fullPageLayout;
	private String pageContent;
	private String pageTitle;
	private String pageName;
	private String metaKeywords;
	private String metaDesc;
	private String type;
	private String ID;
	private String parentID;
	private String canonicalUrl;

	public String getCanonicalUrl() {
		return canonicalUrl;
	}

	public void setCanonicalUrl(String canonicalUrl) {
		this.canonicalUrl = canonicalUrl;
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

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public ArrayList<MenuAndBannersModal> getTopBanners() {
		return topBanners;
	}

	public void setTopBanners(ArrayList<MenuAndBannersModal> topBanners) {
		this.topBanners = topBanners;
	}

	public ArrayList<MenuAndBannersModal> getRightBanners() {
		return rightBanners;
	}

	public void setRightBanners(ArrayList<MenuAndBannersModal> rightBanners) {
		this.rightBanners = rightBanners;
	}

	public ArrayList<MenuAndBannersModal> getLeftBanners() {
		return leftBanners;
	}

	public void setLeftBanners(ArrayList<MenuAndBannersModal> leftBanners) {
		this.leftBanners = leftBanners;
	}

	public ArrayList<MenuAndBannersModal> getBottomBanners() {
		return bottomBanners;
	}

	public void setBottomBanners(ArrayList<MenuAndBannersModal> bottomBanners) {
		this.bottomBanners = bottomBanners;
	}

	public ArrayList<MenuAndBannersModal> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(ArrayList<MenuAndBannersModal> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public String getFullPageLayout() {
		return fullPageLayout;
	}

	public void setFullPageLayout(String fullPageLayout) {
		this.fullPageLayout = fullPageLayout;
	}

	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public String getMetaDesc() {
		return metaDesc;
	}

	public void setMetaDesc(String metaDesc) {
		this.metaDesc = metaDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public ArrayList<ProductsModel> getBreadCrumbList() {
		return breadCrumbList;
	}

	public void setBreadCrumbList(ArrayList<ProductsModel> breadCrumbList) {
		this.breadCrumbList = breadCrumbList;
	}

	public String staticPage() {
		System.out.println("Calling Static Page : " + ID);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String headerHtml = null;
		String footerHtml = null;
		boolean continueSearch = false;
		String pageNameCustomUrl = "";

		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();

		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<>();
		LinkedHashMap<String, ArrayList<MenuAndBannersModal>> staticBannersList = new LinkedHashMap<>();
		ArrayList<ProductsModel> staticPageBreadCrumbSolr = new ArrayList<ProductsModel>();
		ProductsModel staticPageDetailsSolrModel = new ProductsModel();
		String fPageId = "";
		String reqType = request.getParameter("reqType");

		String browseType = (String) session.getAttribute("browseType");
		String tempSubset = (String) session.getAttribute("userSubsetId");
		String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		int subsetId = CommonUtility.validateNumber(tempSubset);

		if (request.getParameter("pId") != null) {
			pId = CommonUtility.validateNumber(request.getParameter("pId"));
		}
		String tempLevel = request.getParameter("levelNo");
		int level = 0;
		if (tempLevel != null && !tempLevel.trim().equalsIgnoreCase("")) {
			level = CommonUtility.validateNumber(tempLevel);
		}

		String sql = "with t1 as (select page_name,static_page_id,header,footer from static_pages where UPPER(page_name)  = UPPER(?) AND (NVL(STATUS,'Y')='Y' OR NVL(STATUS,'A')='A') AND SITE_ID =?),t2 as (select level2 from web_static_links_tree wst,t1 where wst.static_page_id = t1.static_page_id),T3 AS (SELECT STATIC_PAGE_ID FROM WEB_STATIC_LINKS_TREE  WST,T2 WHERE WST.LEVEL2 = T2.LEVEL2 AND WST.LEVEL_NUMBER = 2)select distinct(t1.page_name),t1.static_page_id,t3.static_page_id parent_id, t1.header,t1.footer from t1,t2,t3";

		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);

			if (ID != null) {
				ConnectionManager.closeDBPreparedStatement(pstmt);
				sql = "SELECT page_name, static_page_id,header,footer FROM STATIC_PAGES WHERE static_page_id = ? AND (NVL(STATUS,'Y')='Y' OR NVL(STATUS,'A')='A')";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, CommonUtility.validateNumber(ID));
			} else if (pageName != null) {
				pageNameCustomUrl = CommonUtility.validateString(pageName);
				pageName = pageName.replace("--", "-");
				pageName = pageName.replace("-", " ");
				pstmt.setString(1, pageName);
				pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
				continueSearch = true;
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ID = rs.getString("STATIC_PAGE_ID");
				if (rs.getString("HEADER") != null && !rs.getString("HEADER").trim().equalsIgnoreCase("HEADER")) {
					headerHtml = rs.getString("HEADER");
				}

				if (rs.getString("FOOTER") != null && !rs.getString("FOOTER").trim().equalsIgnoreCase("FOOTER")) {
					footerHtml = rs.getString("FOOTER");
				}
				continueSearch = false;
			}
			if (continueSearch) {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				sql = "SELECT page_name, static_page_id,header,footer FROM STATIC_PAGES WHERE UPPER(CUSTOM_URL)  = UPPER(?) AND (NVL(STATUS,'Y')='Y' OR NVL(STATUS,'A')='A') AND SITE_ID =?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, pageNameCustomUrl);
				pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ID = rs.getString("STATIC_PAGE_ID");
					if (rs.getString("HEADER") != null && !rs.getString("HEADER").trim().equalsIgnoreCase("HEADER")) {
						headerHtml = rs.getString("HEADER");
					}

					if (rs.getString("FOOTER") != null && !rs.getString("FOOTER").trim().equalsIgnoreCase("FOOTER")) {
						footerHtml = rs.getString("FOOTER");
					}

					continueSearch = false;
				}
			}
			if (continueSearch) {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				sql = "SELECT page_name, static_page_id,header,footer FROM STATIC_PAGES WHERE UPPER(PAGE_TITLE)  = UPPER(?) AND (NVL(STATUS,'Y')='Y' OR NVL(STATUS,'A')='A') AND SITE_ID =?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, pageName);
				pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ID = rs.getString("STATIC_PAGE_ID");
					if (rs.getString("HEADER") != null && !rs.getString("HEADER").trim().equalsIgnoreCase("HEADER")) {
						headerHtml = rs.getString("HEADER");
					}
					if (rs.getString("FOOTER") != null && !rs.getString("FOOTER").trim().equalsIgnoreCase("FOOTER")) {
						footerHtml = rs.getString("FOOTER");
					}
					continueSearch = false;
				}
			}

			// Page Name
			if (continueSearch) {
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				sql = "SELECT page_name, static_page_id,header,footer FROM STATIC_PAGES WHERE UPPER(PAGE_NAME)  = UPPER(?) AND (NVL(STATUS,'Y')='Y' OR NVL(STATUS,'A')='A') AND SITE_ID =?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, pageName);
				pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ID = rs.getString("STATIC_PAGE_ID");
					if (rs.getString("HEADER") != null && !rs.getString("HEADER").trim().equalsIgnoreCase("HEADER")) {
						headerHtml = rs.getString("HEADER");
					}

					if (rs.getString("FOOTER") != null && !rs.getString("FOOTER").trim().equalsIgnoreCase("FOOTER")) {
						footerHtml = rs.getString("FOOTER");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}

		if (tempLevel != null && !tempLevel.trim().equalsIgnoreCase("")) {
			level = CommonUtility.validateNumber(tempLevel);
		}

		if (ID != null) {
			UsersDAO dao = new UsersDAO();
			staticBannersList = dao.getStaticBanners(ID);
			topBanners = staticBannersList.get("top");
			leftBanners = staticBannersList.get("left");
			rightBanners = staticBannersList.get("right");
			bottomBanners = staticBannersList.get("bottom");
		}

		if (ID != null && pId == 0)
			pId = CommonUtility.validateNumber(ID);

		boolean getParentMenu = true;
		subMenuList = new ArrayList<>();
		int staticLinkId = 0;

		if (MenuAndBannersDAO.getStaticLinkId().get(CommonUtility.validateNumber(ID)) != null) {
			staticLinkId = MenuAndBannersDAO.getStaticLinkId().get(CommonUtility.validateNumber(ID));
			if (staticLinkId > 0) {
				if (MenuAndBannersDAO.getStaticSubmenuList().get(staticLinkId) != null) {
					subMenuList = MenuAndBannersDAO.getStaticSubmenuList().get(staticLinkId);
					getParentMenu = false;
				}
			}
		}
		if (getParentMenu) {
			int staticParentId = 0;
			if (MenuAndBannersDAO.getStaticLinkParentId().get(CommonUtility.validateNumber(ID)) != null) {
				staticParentId = MenuAndBannersDAO.getStaticLinkParentId().get(CommonUtility.validateNumber(ID));
				if (staticParentId > 0) {
					if(MenuAndBannersDAO.getStaticSubmenuList().get(staticParentId) != null) {
						subMenuList = MenuAndBannersDAO.getStaticSubmenuList().get(staticParentId);
					}

				}
			}

		}
		parentID = Integer.toString(pId);
		if (ID != null && !ID.trim().equalsIgnoreCase("")) {
			if (level > 1) {
				setBreadCrumbList(UsersDAO.getStaticBreadCrumb(ID, level));
			}
			fPageId = ID;
			ID = ID + ".xml";
		}
		String staticPagePath = CommonDBQuery.getSystemParamtersList().get("STATICPAGESPATH");
        boolean foundPage = false;
		try {


			SAXBuilder builder = new SAXBuilder();
			File file = new File(staticPagePath + ID);
			if (!file.exists()) {
				if (CommonUtility.validateString(fPageId).length() > 0 && !CommonUtility.validateString(fPageId).equalsIgnoreCase("NULL") && session.getAttribute("localeCode") != null && CommonUtility.validateString(session.getAttribute("localeCode").toString()).length() > 0) {
					LinkedHashMap<String, LocaleModel> localeList = CommonDBQuery.getLocaleList();
					if (localeList != null && !localeList.isEmpty()) {
						LocaleModel localeModel = localeList.get(CommonUtility.validateString(session.getAttribute("localeCode").toString()));
						if (localeModel != null && localeModel.getLocaleId() > 0) {
							File fileCheck = new File(staticPagePath + fPageId + "_" + localeModel.getLocaleId() + ".xml");
							if (fileCheck.exists()) {
								file = new File(staticPagePath + fPageId + "_" + localeModel.getLocaleId() + ".xml");
								foundPage = true;
							}
						}
					}
				} else {
					file = new File(staticPagePath + CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAGE_NOT_FOUND_STATIC_PAGE_ID"))+ ".xml");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					foundPage = false;
				}
			} else if (file.exists()) {
				foundPage = true;
			}
			if(foundPage) {
				fullPageLayout = UsersDAO.getLayoutType(fPageId);
				org.jdom.Document dom = builder.build(file);
				pageName = dom.getRootElement().getChildText("pageName");
				pageTitle = dom.getRootElement().getChildText("pageTitle");
				pageContent = dom.getRootElement().getChildText("pageContent");
				metaKeywords = dom.getRootElement().getChildText("metaKeywords");
				metaDesc = dom.getRootElement().getChildText("metaDesc");
				
				if(CommonUtility.validateNumber(fPageId)>0) {
					ArrayList<ProductsModel> staticPageDetailsSolrList = ProductHunterSolr.getStaticPageDetailsById(CommonUtility.validateNumber(fPageId));
					if(staticPageDetailsSolrList!=null && staticPageDetailsSolrList.size()>0) {
						staticPageDetailsSolrModel = staticPageDetailsSolrList.get(0);
						if(staticPageDetailsSolrModel!=null) {
							staticPageBreadCrumbSolr = staticPageDetailsSolrModel.getStaticpageBreadCrumb();
						}
					}
					
				}
				
			}
		
			if (foundPage) {
				String pricePrecision = "";
				String pricePrecisionFormate = "#0.00";
				if (session != null && session.getAttribute("pricePrecision") != null && CommonUtility.validateString((String) session.getAttribute("pricePrecision")).length() > 0) {
					pricePrecision = CommonUtility.validateString((String) session.getAttribute("pricePrecision"));
				} else if (CommonUtility.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision")).length() > 0) {
					pricePrecision = CommonUtility.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision"));
				} else {
					pricePrecision = "2";
				}
				
				if (CommonUtility.validateString(pricePrecision).equalsIgnoreCase("5")) {
					pricePrecisionFormate = "#0.00000";
				} else if (CommonUtility.validateString(pricePrecision).equalsIgnoreCase("4")) {
					pricePrecisionFormate = "#0.0000";
				} else if (CommonUtility.validateString(pricePrecision).equalsIgnoreCase("3")) {
					pricePrecisionFormate = "#0.000";
				} else {
					pricePrecisionFormate = "#0.00";
				}

				ToolManager velocityToolManager = new ToolManager();
				velocityToolManager.configure("velocity-tools.xml");
				VelocityContext context = new VelocityContext(velocityToolManager.createContext());
				VelocityEngine velocityTemplateEngine = new VelocityEngine();
				String templatePath = CommonDBQuery.getSystemParamtersList().get("SITE_TEMPLATE_PATH")
						+ CommonDBQuery.getSystemParamtersList().get("SITE_NAME")
						+ CommonDBQuery.getSystemParamtersList().get("MACROS_FOLDER");
				velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
				velocityTemplateEngine.setProperty("velocimacro.library.autoreload", true);
				velocityTemplateEngine.setProperty("velocimacro.library", "defaultMacro.vm");
				velocityTemplateEngine.init();
				context.put("session", session);
				context.put("generalSubset", generalSubset);
				context.put("subsetId", subsetId);
				context.put("CIMM2VelocityTool", CIMM2VelocityTool.getInstance());
				context.put("CIMMUtility", CIMM2VelocityTool.getInstance());
				String refreshVersion = "rv="+session.getId();
				context.put("refreshVersion",CommonUtility.validateString(refreshVersion));
				context.put("webThemes",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES")));
				context.put("siteName",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_NAME")));
				context.put("siteId",
						CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
				context.put("siteDisplayName",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")));
				context.put("internationalUser", CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_INTERNATIONAL_USER")));
				context.put("eclipseIsDownMessage", CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("ECLIPSEISDOWNMESSAGE")));
				context.put("eclipseDownCartMessage", CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNCARTMESSAGE")));
				context.put("thumbNail",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("THUMBNAIL")));
				context.put("itemImage",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMIMAGE")));
				context.put("detailImage",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DETAILIMAGE")));
				context.put("enlargedImage",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENLARGEIMAGE")));
				context.put("taxonomyImage",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TAXONOMYIMAGEPATH")));
				context.put("documentPath",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DOCUMENTS")));
				context.put("brandLogoPath",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRANDLOGO")));
				context.put("buyingCompanyLogoPath",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BUYINGCOMPANYLOGO")));
				context.put("bannerLogoPath",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BANNERLOGO")));
				context.put("userProfileImagePath", CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("USER_PROFILE_IMAGE_PATH")));
				context.put("userProfileThumbImagePath", CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("USER_PROFILE_THUMB_IMAGE_PATH")));
				context.put("manufacturerLogoPath",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MNF_IMAGES")));
				context.put("checkCVVandCardHolderName", CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_CVV_AND_CARDHOLDER_NAME")));
				context.put("attributeSwatchImagePath",
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SWATCH")));
				context.put("pricePrecision", CommonUtility.validateString(pricePrecision));
				context.put("pricePrecisionFormate", CommonUtility.validateString(pricePrecisionFormate));
				if (session.getAttribute("localeCode") != null) {
					context.put("locale", LayoutLoader.getMessageProperties()
							.get(session.getAttribute("localeCode").toString().toUpperCase()));
					context.put("localeLang", (String) session.getAttribute("sessionLocale"));
				} else {
					context.put("locale", LayoutLoader.getMessageProperties().get("EN"));
					context.put("localeLang", "1_en");

				}
				StringWriter writer = new StringWriter();
				velocityTemplateEngine.evaluate(context, writer, "", pageContent);
				StringBuilder finalMessage = new StringBuilder();
				finalMessage.append(writer.toString());
				pageContent = finalMessage.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		contentObject.put("generalSubset", generalSubset);
		contentObject.put("subsetId", subsetId);
		contentObject.put("CIMM2VelocityTool", CIMM2VelocityTool.getInstance());
		contentObject.put("CIMMUtility", CIMM2VelocityTool.getInstance());
		contentObject.put("pageTypeReq", "staticPage");
		contentObject.put("pId", pId);
		contentObject.put("topBanners", topBanners);
		contentObject.put("leftBanners", leftBanners);
		contentObject.put("rightBanners", rightBanners);
		contentObject.put("bottomBanners", bottomBanners);
		contentObject.put("ID", ID);
		contentObject.put("subMenuList", subMenuList);
		contentObject.put("parentID", parentID);
		contentObject.put("fullPageLayout", fullPageLayout);
		contentObject.put("pageName", CommonUtility.validateString(pageName));
		contentObject.put("pageTitle", CommonUtility.validateString(pageTitle));
		contentObject.put("pageContent", pageContent);
		contentObject.put("metaTagString", metaKeywords);
		contentObject.put("metaDescription", metaDesc);
		contentObject.put("metaDesc", metaDesc);
		contentObject.put("staticPageBreadCrumbSolr",staticPageBreadCrumbSolr);
		contentObject.put("staticPageDetailsSolrModel",staticPageDetailsSolrModel);
		if (headerHtml != null && !headerHtml.isEmpty())
			contentObject.put("headerHtml", headerHtml);
		if (footerHtml != null && !footerHtml.isEmpty())
			contentObject.put("footerHtml", footerHtml);
		if (reqType != null && reqType.trim().equalsIgnoreCase("Body")) {
			renderContent = pageContent;
		} else {

			if (fullPageLayout != null && fullPageLayout.trim().equalsIgnoreCase("Y")) {
				renderContent = LayoutGenerator.templateLoader("StaticPageFullPageLayout", contentObject, null, null,
						null);
			} else {
				renderContent = LayoutGenerator.templateLoader("StaticPage", contentObject, null, null, null);
			}

		}
		return SUCCESS;
	}
	
}