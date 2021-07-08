package com.unilog.defaults;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.VelocityTemplateEngine.TemplateEngine;
import com.unilog.api.widget.WidgetUtility;
import com.unilog.autocomplete.CustomAutoComplete;
import com.unilog.custommodule.dao.DxFeedDAOService;
import com.unilog.database.CommonDBQuery;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.products.ProductHunterSolr;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.security.TripleDESEncryption;
import com.unilog.users.UsersDAO;
import com.unilog.users.WebLogin;
import com.unilog.utility.CommonUtility;
import com.unilog.velocitytool.CIMM2VelocityTool;
import com.unilognew.erp.service.cimm2bcentral.Cimm2BCentralUserERPServiceImpl;

/**
 * Servlet implementation class ReloadSystemConfig
 */
public class ReloadSystemConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReloadSystemConfig() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try
		{
			System.out.println("Reloading System Config...");
			String reload = request.getParameter("r");
			if(CommonUtility.validateString(reload).length()>0){
				if(reload.equalsIgnoreCase("sysparam")){
					CommonDBQuery.getSysParamServlet(request);
				}else if(reload.equalsIgnoreCase("branchStorage")){
					CommonDBQuery.getBranchStorage();
				}else if(reload.equalsIgnoreCase("basicConfig")){
					PropertyAction.loadBasicConfigs();
				}else if(reload.equalsIgnoreCase("layout")){
					TemplateEngine.initiateTemplateEngine();
					LayoutLoader.generateLayout();
					LayoutLoader.generateLocale();
				}else if(reload.equalsIgnoreCase("staticMenu")){
					MenuAndBannersDAO.getStaticMenu();
				}else if(reload.equalsIgnoreCase("autoCompleteConfig")){
					CommonDBQuery.getAutocompleteConfig();
				}else if(reload.equalsIgnoreCase("abandonedCart")){
					CommonDBQuery.startAbandonedCartTimer();
				}else if(reload.equalsIgnoreCase("headerMenu")){
					MenuAndBannersDAO.getHeaderMenu();
				}
			}else{
				CommonDBQuery.getSysParamServlet(request);
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
				CIMM2VelocityTool.featuredBrands = null;
				CIMM2VelocityTool.wareHouseCustomFields = null;
				CIMM2VelocityTool.customTableData = null;
				if(CommonDBQuery.getSystemParamtersList() !=null && 
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_HOMEPAGE_FROM_CMS")).equalsIgnoreCase("Y") && 
						CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID").trim()) > 0 && 
						CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_PREBUILT_CMS_HOMEPAGE")).equalsIgnoreCase("Y")) {
					System.out.println("Initiate - write to CMS html file at ReloadSystemConfig slt");
					CommonDBQuery.copyCMSPageContentToFile(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID")));
					System.out.println("Completed - write to CMS html file at ReloadSystemConfig slt");
				}
				
				//CommonDBQuery.startAbandonedCartTimer();
				//CommonDBQuery.getDefaultCategoryBanners();
				//CommonDBQuery.getSwatchList();
				//YahooBossSupport.setYahooServer(CommonDBQuery.getSystemParamtersList().get("GEO_API_URL"));
				//YahooBossSupport.setConsumer_key(CommonDBQuery.getSystemParamtersList().get("GEO_API_KEY"));
				//YahooBossSupport.setConsumer_secret(CommonDBQuery.getSystemParamtersList().get("GEO_API_SECRET"));
				//SXV6191UserERPServiceImpl.getInstance().setInitRequired(true);
				//if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_ARI_API")).equalsIgnoreCase("Y")){ DataSmartController.getInstance().setReInitializeClient(true); }// ARI DataSmart controller
				//SXV6191UserERPServiceImpl.getInstance().setInitRequired(true);
				//PrettySearchDAO.getInstance().setReInitializeClient(true);
			}
			Cimm2BCentralUserERPServiceImpl.getInstance().setInitRequired(true);
			if(CommonUtility.customServiceUtility()!=null) { CommonUtility.customServiceUtility().getOrderModeList(); } //CustomServiceProvider
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
