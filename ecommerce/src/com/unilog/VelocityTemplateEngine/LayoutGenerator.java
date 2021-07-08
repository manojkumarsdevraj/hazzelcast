package com.unilog.VelocityTemplateEngine;

import java.io.StringWriter;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.ConversionTool;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.DisplayTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.apache.velocity.tools.generic.SortTool;
import org.apache.velocity.tools.view.tools.LinkTool;

import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.datasmart.DataSmartController;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsAction;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;

import com.unilog.utility.CommonUtility;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class LayoutGenerator {

	public static String templateLoader(String layoutName, LinkedHashMap<String, Object> contentObject,
			LinkedHashMap<String, Object> leftContentObject, LinkedHashMap<String, Object> rightContentObject,
			LinkedHashMap<String, Object> footerContentObject) {
		String templateOutput = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String responseType = (String) request.getParameter("rt");
		try {
			String browseType = "";
			String siteName = "";
			String browseString = (String) session.getAttribute("browseType");
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			String ajaxCall = (String) request.getParameter("AjaxRequest");

			if (CommonUtility.validateString(ajaxCall).length() == 0) {
				ajaxCall = "N";
			}
			int userId = CommonUtility.validateNumber(sessionUserId);
			if (browseString != null && browseString.trim().equalsIgnoreCase("Mobile")) {
				browseType = "Mobile";
			} else {
				browseType = "";
			}
			siteName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
			System.out.println("siteName @ templateLoader : " + siteName);
			TemplateModel template = null;
			LinkedHashMap<String, TemplateModel> layoutList = LayoutLoader.getLayoutModel().getLayoutBySite()
					.get(CommonUtility.validateString(siteName).toUpperCase());

			if (layoutList != null) {
				// template = new TemplateModel();
				template = layoutList.get(layoutName);
			}
			if (CommonUtility.validateString(responseType).equalsIgnoreCase("json")) {
				Gson createJsonData = new Gson();
				templateOutput = createJsonData.toJson(contentObject);
			} else if (CommonUtility.validateString(responseType).equalsIgnoreCase("jsonv2")){
                Gson createJsonData = new Gson();
                LinkedHashMap<String, Object> jsonResponseObj = new LinkedHashMap<>();
                jsonResponseObj.put("data",contentObject);
                templateOutput = createJsonData.toJson(jsonResponseObj);
			} else if (template != null) {
				System.out.println("Mood name at Loader : " + CommonDBQuery.getSystemParamtersList().get("MOOD_NAME"));

				VelocityEngine velocityTemplateEngine = new VelocityEngine();
				String templatePath = CommonDBQuery.getSystemParamtersList().get("SITE_TEMPLATE_PATH")+ CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
				System.out.println("templatePath @ templateLoader : " + templatePath);
				velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
				velocityTemplateEngine.setProperty("velocimacro.library.autoreload", true);
				velocityTemplateEngine.init();
				String layoutTemplateName = "";
				if (browseType != null && CommonUtility.validateString(browseType).equalsIgnoreCase("Mobile")) {
					layoutTemplateName = "Layout_LayoutApp";
				} else {
					layoutTemplateName = template.getLayoutName();
				}
				Template t = velocityTemplateEngine.getTemplate(layoutTemplateName + ".html");// TemplateEngine.getVelocityTemplateEngine().getTemplate(template.getLayoutName()+".html");
				VelocityContext context = new VelocityContext();
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				context.put("commonUtility", CommonUtility.class);
				context.put("numberTool", new NumberTool());
				context.put("escapeTool", new EscapeTool());
				context.put("javaURLDecoder", new URLDecoder());
				context.put("math", new MathTool());
				context.put("dispalyTool", new DisplayTool());
				context.put("convert", new ConversionTool());
				context.put("dateTool", new ComparisonDateTool());
				context.put(Integer.class.getSimpleName(), Integer.class);
				context.put("session", session);
				context.put("timeStamp", timestamp.getTime());
				context.put("CIMMUtility", CIMM2VelocityTool.getInstance());
				context.put("CIMM2VelocityTool", CIMM2VelocityTool.getInstance());
				context.put("linkTool", new LinkTool());
				context.put("sortTool", new SortTool());
				context.put("CommonDBQueryWareHouseList", CommonDBQuery.getWareHouseList());
				context.put("CommonDBQuerySystemParameter", CommonDBQuery.getSystemParamtersList());
				if(CommonDBQuery.getShipViaMapDetailsList()!=null) {
				context.put("CommonDBQueryShipViaMapDetails", CommonDBQuery.getShipViaMapDetailsList());
				}
				context.put("localeList", CommonDBQuery.getLocaleList());
				context.put("request", request);
				context.put("orderModeList", CommonDBQuery.getOrderModeList());
		    	context.put("sitePromoBannerList", CommonDBQuery.getSitePromoBannerList());
		    	context.put("promoBannerList",  CommonDBQuery.getPromoBannerList());
		    	context.put("dropPromoBannerList", CommonDBQuery.getDropPromoBannerList());
		    	context.put("accRepList", CommonDBQuery.getAccRepList());
		    	
				String protocol = request.getScheme();
				context.put("hostName", protocol + "://" + request.getServerName() + "/");
				context.put("moodname", CommonDBQuery.getSystemParamtersList().get("MOOD_NAME"));
				if (template.getLayoutObj() != null && template.getLayoutObj().size() > 0) {
					for (Entry<String, String> sectionList : template.getLayoutObj().entrySet()) {
						context.put(sectionList.getKey(), sectionList.getValue());
					}
				}
				if (session.getAttribute("localeCode") != null) {
					context.put("locale", LayoutLoader.getMessageProperties()
							.get(session.getAttribute("localeCode").toString().toUpperCase()));
					context.put("localeLang", (String) session.getAttribute("sessionLocale"));
				} else {
					context.put("locale", LayoutLoader.getMessageProperties().get("EN"));
					context.put("localeLang", "1_en");
					session.setAttribute("localeId", "1");
					session.setAttribute("localeCode", "EN");
					session.setAttribute("sessionLocale", "1_en");
				}
				
				HeaderLayout.generateHeaderContent(context);

				if (leftContentObject != null && leftContentObject.size() > 0) {
					LeftLayout.generateLeftContent(context, leftContentObject);

				}
				if (contentObject != null && contentObject.size() > 0) {
					ContentLayout.generateBodyContent(context, contentObject);
				}
				if (rightContentObject != null && rightContentObject.size() > 0) {
					context.put("rightContent",
							RightLayout.generateRightContent(template.getRightContent(), rightContentObject));
				}
				if (contentObject != null && contentObject.get("pageTypeReq") != null
						&& contentObject.get("pageTypeReq").toString().trim().equalsIgnoreCase("staticPage")) {
					System.out.println(
							"******************************CIMM Touch Static Page****************************************");
					if (contentObject.get("headerHtml") != null
							&& !contentObject.get("headerHtml").toString().isEmpty()) {
						context.put("headerHtml", contentObject.get("headerHtml").toString());
					}
					if (contentObject.get("footerHtml") != null
							&& !contentObject.get("footerHtml").toString().isEmpty()) {
						context.put("footerHtml", contentObject.get("footerHtml").toString());
					}

				}
				ProductsAction productsAction = new ProductsAction();
				context = productsAction.getCartCount(context, request);
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_ARI_API"))
						.equalsIgnoreCase("Y")) {
					context.put("ARIData", DataSmartController.getInstance());
				}
				if (CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID"))
						.length() > 0) {
					String sessionId = session.getId();
					String[] brandIDList = CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID")
							.split(",");
					boolean isExcludeBranch = false;
					if (brandIDList != null) {
						for (String brandId : brandIDList) {
							if (userId > 1) {
								isExcludeBranch = ProductsDAO.getCartBrandExcludeStatus(userId, null,
										Integer.parseInt(brandId), "cart");
							} else {
								isExcludeBranch = ProductsDAO.getCartBrandExcludeStatus(userId, sessionId,
										Integer.parseInt(brandId), "cart");
							}

							if (isExcludeBranch) {
								break;
							}
						}
						if (isExcludeBranch) {
							session.setAttribute("isWillCallExclude", "Y");
						} else {
							session.setAttribute("isWillCallExclude", "N");
						}
					}
				}

				String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getGroupNameEverywhere")).equalsIgnoreCase("Y") && userId > 1) {
					contentObject.put("session", session);
					contentObject = ProductsDAO.getSavedGroupNameByUserIdDao(userId, contentObject, CommonUtility.validateNumber(buyingCompanyId));
					String advancedProductGroup = CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP");
					if (!CommonUtility.validateString(advancedProductGroup).equalsIgnoreCase("Y")) {
						context.put("groupListData", contentObject.get("groupListData"));
					}
					context.put("savedCartData", contentObject.get("savedCartData"));
					context.put("approveCartData", contentObject.get("approveCartData"));
				}
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SET_HOMEBRANCH_DETAILS"))
						.equalsIgnoreCase("Y") && userId > 1) {
					String homeBranchId = (String) session.getAttribute("homeBranchId");
					LinkedHashMap<String, ProductsModel> branchDetail = CommonDBQuery.branchDetailData;
					ProductsModel branch = branchDetail.get(homeBranchId);
					context.put("homeBranchDetails", branch);
					context.put("allWarehouseDetail", CommonDBQuery.branchDetailData);
				}
				if (CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("STORE_MANUFACTURER_CUSTOM_FIELDS"))
						.equalsIgnoreCase("Y")) {
					context.put("manfCustomField", CommonDBQuery.manfCustomField);
					;
				}
				String POValidStatus = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_PO_MANDATORY"));
				String GasPOValidStatus = "N";
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ALL_USER_CUSTOMER_CUSTOM_FIELDS")).equalsIgnoreCase("Y") && session.getAttribute("customerCustomFieldValue") != null) {
					LinkedHashMap<String, String> customerCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("customerCustomFieldValue");
					if (CommonUtility.validateString(customerCustomFieldValue.get("PO_REQUIRED")).length() > 0) {
						POValidStatus = customerCustomFieldValue.get("PO_REQUIRED");
					}
					if (CommonUtility.validateString(customerCustomFieldValue.get("GAS_PO_REQUIRED")).length() > 0) {
						GasPOValidStatus = customerCustomFieldValue.get("GAS_PO_REQUIRED");
					}
				}

				String pricePrecision = "";
				String pricePrecisionFormate = "";
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
				String refreshVersion = "rv="+session.getId();
				context.put("refreshVersion",CommonUtility.validateString(refreshVersion));
				context.put("ajaxCall", ajaxCall);
				context.put("LayoutName", CommonUtility.validateString(layoutName));
				context.put("webThemes", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES")));
				context.put("siteName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_NAME")));
				context.put("siteId", CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
				context.put("siteDisplayName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")));
				context.put("internationalUser", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_INTERNATIONAL_USER")));
				context.put("eclipseIsDownMessage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ECLIPSEISDOWNMESSAGE")));
				context.put("eclipseDownCartMessage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNCARTMESSAGE")));
				context.put("POValidStatus", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_PO_MANDATORY")));
				context.put("GasPOValidStatus", GasPOValidStatus);
				context.put("POValidStatus", POValidStatus);
				context.put("thumbNail", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("THUMBNAIL")));
				context.put("itemImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMIMAGE")));
				context.put("detailImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DETAILIMAGE")));
				context.put("enlargedImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENLARGEIMAGE")));
				context.put("productThumbNail", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRODUCTMODETHUMBNAIL")));
				context.put("productListImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRODUCTMODEIMAGE")));
				context.put("productDetailImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRODUCTMODEDETAILIMAGE")));
				context.put("ProductEnlargedImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRODUCTMODEENLARGEDIMAGE")));
				context.put("taxonomyImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TAXONOMYIMAGEPATH")));
				context.put("documentPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DOCUMENTS")));
				context.put("brandLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRANDLOGO")));
				context.put("buyingCompanyLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BUYINGCOMPANYLOGO")));
				context.put("bannerLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BANNERLOGO")));
				context.put("userProfileImagePath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USER_PROFILE_IMAGE_PATH")));
				context.put("userProfileThumbImagePath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USER_PROFILE_THUMB_IMAGE_PATH")));
				context.put("manufacturerLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MNF_IMAGES")));
				context.put("checkCVVandCardHolderName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_CVV_AND_CARDHOLDER_NAME")));
				context.put("invoiceSignatureImagePath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("INVOICE_SIGNATURE_IMAGE_PATH")));
				context.put("pageTitleByLayoutName", StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(layoutName.replace("Page", "")), ' '));
				context.put("attributeSwatchImagePath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SWATCH")));
				context.put("pricePrecision", CommonUtility.validateString(pricePrecision));
				context.put("pricePrecisionFormate",CommonUtility.validateString(pricePrecisionFormate));
				context.put("userAgent", CommonUtility.validateString(request.getHeader("User-Agent")));
				context.put("siteLogo", CommonUtility.validateString(CommonDBQuery.getSiteLogo()));
				context.put("date", new DateTool());
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FETCH_ALL_WAREHOUSE_DETAILS")).equalsIgnoreCase("Y")){
					context.put("allWarehouseDetail", CommonDBQuery.branchDetailData);
				}
				if (session.getAttribute("userLogin") != null) {
					context.put("userLogin", (Boolean) session.getAttribute("userLogin"));
				}
				if (session.getAttribute("userEmailAddress") != null) {
					context.put("userEmailAddress", CommonUtility.validateString((String) session.getAttribute("userEmailAddress")));
				}
				context.put("cdnUrl", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CDN_URL")));
				StringWriter writer = new StringWriter();
				t.merge(context, writer);
				templateOutput = writer.toString();
				//System.out.println("--------------------- templateOutput --------> Start \n"+templateOutput);
				//System.out.println("--------------------- templateOutput --------> End \n");
			} else {
				VelocityContext context = new VelocityContext();
				context.put("session", session);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Template Loader Error");
		}
		return templateOutput;
	}
}