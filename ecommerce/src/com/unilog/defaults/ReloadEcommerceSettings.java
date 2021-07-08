package com.unilog.defaults;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.VelocityTemplateEngine.TemplateEngine;
import com.unilog.api.widget.WidgetUtility;
import com.unilog.autocomplete.CustomAutoComplete;
import com.unilog.custommodule.dao.DxFeedDAOService;
import com.unilog.database.CommonDBQuery;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.products.ProductHunterSolr;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.searchconfig.SearchConfigUtility;
import com.unilog.security.TripleDESEncryption;
import com.unilog.users.UsersDAO;
import com.unilog.users.WebLogin;
import com.unilog.utility.CommonUtility;
import com.unilog.velocitytool.CIMM2VelocityTool;
import com.unilognew.erp.service.cimm2bcentral.Cimm2BCentralUserERPServiceImpl;

public class ReloadEcommerceSettings extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String renderContent;

	public String ReloadSystemConfig() {
		try {
			request = ServletActionContext.getRequest();
			response = ServletActionContext.getResponse();
			response.addHeader("Access-Control-Allow-Headers","content-type, authorization");
			System.out.println("Reloading System Config...");
			String reload = request.getParameter("r");
			if (CommonUtility.validateString(reload).length() > 0) {
				if (reload.equalsIgnoreCase("sysparam")) {
					CommonDBQuery.getSysParamServlet(request);
					renderContent = "System Settings Refresh Successful";
				} else if (reload.equalsIgnoreCase("branchStorage")) {
					CommonDBQuery.getBranchStorage();
					renderContent = "Branch Storage Refresh Successful";
				} else if (reload.equalsIgnoreCase("basicConfig")) {
					PropertyAction.loadBasicConfigs();
					renderContent = "Basic Config Refresh Successful";
				} else if (reload.equalsIgnoreCase("layout")) {
					TemplateEngine.initiateTemplateEngine();
					LayoutLoader.generateLayout();
					LayoutLoader.generateLocale();
					renderContent = "Layout Refresh Successful";
				} else if (reload.equalsIgnoreCase("staticMenu")) {
					MenuAndBannersDAO.getStaticMenu();
					renderContent = "Static Menu Refresh Successful";
				} else if (reload.equalsIgnoreCase("autoCompleteConfig")) {
					CommonDBQuery.getAutocompleteConfig();
					renderContent = "Auto Complete Config Refresh Successful";
				} else if (reload.equalsIgnoreCase("abandonedCart")) {
					CommonDBQuery.startAbandonedCartTimer();
					renderContent = "Abandoned Cart Timer Refresh Successful";
				} else if (reload.equalsIgnoreCase("headerMenu")) {
					MenuAndBannersDAO.getHeaderMenu();
					renderContent = "Header Menu Refresh Successful";
				} else if (reload.equalsIgnoreCase("homepage")) {
					if (CommonDBQuery.getSystemParamtersList() != null
							&& CommonUtility
									.validateString(CommonDBQuery.getSystemParamtersList().get("GET_HOMEPAGE_FROM_CMS"))
									.equalsIgnoreCase("Y")
							&& CommonUtility.validateNumber(
									CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID").trim()) > 0
							&& CommonUtility
									.validateString(
											CommonDBQuery.getSystemParamtersList().get("ENABLE_PREBUILT_CMS_HOMEPAGE"))
									.equalsIgnoreCase("Y")) {
						System.out.println("Initiate - write to CMS html file at ReloadSystemConfig action");
						CommonDBQuery.copyCMSPageContentToFile(CommonUtility
								.validateString(CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID")));
						renderContent = "Home Page Load complated";
						System.out.println("Completed - write to CMS html file at ReloadSystemConfig action");
					}
				}else if (reload.equalsIgnoreCase("searchconfig")) {
					SearchConfigUtility.searchQueryFields();
					renderContent = "Search config reload Successful";
				} else if (reload.equalsIgnoreCase("facetfield")) {
					CommonDBQuery.getDefaultFacetField();
					renderContent = "Facet field reload Successful";
				} 
			} else {
				CommonDBQuery.getSystemParameters();
				CommonDBQuery.setThemeDate(null);
				CommonDBQuery.getShipViaList();
				CommonDBQuery.getWareHouseParameters();
				CommonDBQuery.getWareHouseCustomFields();
				CommonDBQuery.getBranchStorage();
				CommonDBQuery.getDefaultFacetField();
				CommonDBQuery.getAutocompleteConfig();
				CommonDBQuery.checkForTheme();
				CommonDBQuery.getCustomServiceUtilities();
				CommonDBQuery.startTimerForScheduledTheme();
				PropertyAction.loadBasicConfigs();
				TemplateEngine.initiateTemplateEngine();
				ProductHunterSolr.reloadSolrUrl();
				LayoutLoader.generateLayout();
				LayoutLoader.generateLocale();
				WebLogin.loadWebUser();
				SearchConfigUtility.searchQueryFields();
				MenuAndBannersDAO.getStaticMenu();
				MenuAndBannersDAO.getHeaderMenu();
				MenuAndBannersDAO.getHomePageBanners();
				MenuAndBannersDAO.getStaticSubmenu();
				UniversalLinks.setReInitHomeBanners(true);
				WidgetUtility.loadProperties();
				CIMM2VelocityTool.getInstance().setReInitCategoryBrandType(true);
				DxFeedDAOService.getInstance().setReInitializeClient(true);
				CustomAutoComplete.getInstance().setReInitializeClient(true);
				TripleDESEncryption.getInstance().setReInitializeClient(true);
				CommonDBQuery.getValueList();
				UsersDAO.setWareHouses(UsersDAO.getWareHouseList());
				CommonDBQuery.customerDynamicPages();
				CIMM2VelocityTool.featuredBrands = null;
				CIMM2VelocityTool.wareHouseCustomFields = null;
				CIMM2VelocityTool.customTableData = null;
				if (CommonDBQuery.getSystemParamtersList() != null
						&& CommonUtility
								.validateString(CommonDBQuery.getSystemParamtersList().get("GET_HOMEPAGE_FROM_CMS"))
								.equalsIgnoreCase("Y")
						&& CommonUtility.validateNumber(
								CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID").trim()) > 0
						&& CommonUtility
								.validateString(
										CommonDBQuery.getSystemParamtersList().get("ENABLE_PREBUILT_CMS_HOMEPAGE"))
								.equalsIgnoreCase("Y")) {
					System.out.println("Initiate - write to CMS html file at ReloadSystemConfig action");
					CommonDBQuery.copyCMSPageContentToFile(CommonUtility
							.validateString(CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID")));
					System.out.println("Completed - write to CMS html file at ReloadSystemConfig action");
				}

				// CommonDBQuery.startAbandonedCartTimer();
				// CommonDBQuery.getDefaultCategoryBanners();
				// CommonDBQuery.getSwatchList();
				// YahooBossSupport.setYahooServer(CommonDBQuery.getSystemParamtersList().get("GEO_API_URL"));
				// YahooBossSupport.setConsumer_key(CommonDBQuery.getSystemParamtersList().get("GEO_API_KEY"));
				// YahooBossSupport.setConsumer_secret(CommonDBQuery.getSystemParamtersList().get("GEO_API_SECRET"));
				// SXV6191UserERPServiceImpl.getInstance().setInitRequired(true);
				// if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_ARI_API")).equalsIgnoreCase("Y")){
				// DataSmartController.getInstance().setReInitializeClient(true); }// ARI
				// DataSmart controller
				// SXV6191UserERPServiceImpl.getInstance().setInitRequired(true);
				// PrettySearchDAO.getInstance().setReInitializeClient(true);

				renderContent = "Complete Refresh Successful";
			}
			Cimm2BCentralUserERPServiceImpl.getInstance().setInitRequired(true);
			if (CommonUtility.customServiceUtility() != null) {
				CommonUtility.customServiceUtility().getOrderModeList();
			} // CustomServiceProvider

		} catch (Exception e) {
			renderContent = "Error while refreshing";
			e.printStackTrace();
		}
		return SUCCESS;
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

	public String getRenderContent() {
		return renderContent;
	}

	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}

}
