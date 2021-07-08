package com.unilog.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.app.VelocityEngine;
import org.jboss.security.Base64Encoder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;

import com.unilog.api.bronto.BrontoAbandonedCartTimerTask;
import com.unilog.autocomplete.AutoComplete;
import com.unilog.customfields.CustomFieldDAO;
import com.unilog.customfields.CustomModel;
import com.unilog.defaults.CustomFieldModel;
import com.unilog.defaults.ULLog;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.misc.YahooBossSupport;
import com.unilog.model.WebsiteModel;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.CIMMTaxModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CIMMTouchUtility;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.utility.ThemeGeneratorScheduledCheck;
import com.unilog.utility.dao.ThemesDao;
import com.unilog.utility.dao.ValueListDao;
import com.unilog.utility.model.LocaleModel;
import com.unilog.utility.model.SwatchModel;
import com.unilog.utility.model.Themes;
import com.unilog.utility.model.ValueListModel;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class CommonDBQuery {

	private static LinkedHashMap<String, String> systemParametersList = null;
	private static LinkedHashMap<String, String> wareHouseList = null;
	private static LinkedHashMap<String, String> netWorkwareHouseList = null;
	private static LinkedHashMap<String, String> activewareHouseList = null;
	public static LinkedHashMap<String, String> systemParametersListVal = null;
	public static LinkedHashMap<String, String> systemParameterConfigKeyAndParamIdList = null;
	public static LinkedHashMap<String, String> manfCustomField = null;
	private static LinkedHashMap<String, SwatchModel> swatchValueList = null;
	private static LinkedHashMap<String, String> customerDynamicPagesList = null;
	//private static Properties props = null;
	private static String popularSearchFileName = null;
	private static LinkedHashMap<String, ArrayList<CustomFieldModel>> wareHouseCustomFieldsList = null;
	public static LinkedHashMap<String, ArrayList<ProductsModel>> newProductList = null;
	public static LinkedHashMap<String, ArrayList<ProductsModel>> hotDealsList = null;
	public static LinkedHashMap<String, ArrayList<ProductsModel>> topSellersList = null;
	public static LinkedHashMap<String, ProductsModel> branchDetailData = null;
	private static ArrayList<String> defaultFacet = null;
	private static ArrayList<ValueListModel> eventLocations = null;
	private static ArrayList<ValueListModel> eventCategories = null;
	private static ArrayList<ValueListModel> eventTypes = null;
	private static String popularityHitsFileName = null;
	private static String userLogFileName = null;
	private static String cimm2bCentralAuthorization = null;
	private static int recordCount;
	private static int userLogRecordCount;
	private static int globalSiteId = 0; 
	public static String webSiteName;
	public static ArrayList<WebsiteModel> websiteListArray = null;
	private static HttpServletRequest request;
	private static Timer time = null;
	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> defaultCategoryBanners = null;
	private static LinkedHashMap<String, String> creditCardErpCode = null;
	private static LinkedHashMap<String, LocaleModel> localeList = null;
	private static String elementURL = null;
	private static ArrayList<ShipVia> SiteShipViaList = null;
	private static String localSiteURL = "com";
	private static String localDirectory = "D:\\EcommerceProject\\EcommerceRepository\\";
	private static String localEnvironmentIPAddress = "172.16.2.57";
	private static String localEnvironmentPublicIPAddress = "182.72.169.36";
	private static boolean localDevelopmentEnvironment = false;
	public static List<CustomTable> zipCodeShipBranches = null;
	private static String themeDate = null;
	private static List<CustomTable> orderModeList = null;
	private static List<CustomTable> promoBannerList = null;
	private static List<CustomTable> sitePromoBannerList = null;
	private static List<CustomTable> dropPromoBannerList = null;
	private static List<CustomTable> accRepList = null;
	private static String siteLogo;
	private static List<CIMMTaxModel> cimmTaxTable = null;
	private static LinkedHashMap<String, String> shipViaMapDetailsList = null;
	public static String jiraKey;
	
	static {
		getSystemParameters();
		getWareHouseParameters();
		getWareHouseCustomFields();
		getBranchStorage();
		getDefaultFacetField();
		getAutocompleteConfig();
		getValueList();
		getLocaleListFromDatabase();
		getShipViaList();
		getCreditCardTypeCode();
		UsersDAO.setWareHouses(UsersDAO.getWareHouseList());
		getCustomServiceUtilities();
		checkForTheme();
		startTimerForScheduledTheme();
		customerDynamicPages();
		if(CommonDBQuery.getSystemParamtersList() !=null && 
				CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_HOMEPAGE_FROM_CMS")).equalsIgnoreCase("Y") && 
				CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID").trim()) > 0 && 
				CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_PREBUILT_CMS_HOMEPAGE")).equalsIgnoreCase("Y")) {
			System.out.println("Initiate - write to CMS html file at CommonDB static");
			CommonDBQuery.copyCMSPageContentToFile(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CMS_HOMEPAGE_ID")));
			System.out.println("Completed - write to CMS html file at CommonDB static");
		}
		// startAbandonedCartTimer();
		// getSwatchList();
		// getProductPromotionList();
		// getDefaultCategoryBanners();
	}

	public static void getCustomServiceUtilities() {
		try {
			if (CommonUtility.customServiceUtility() != null) {
				CommonUtility.customServiceUtility().getShipviaZipList();
				CommonUtility.customServiceUtility().getSalesRepDetails();
				promoBannerList=CommonUtility.customServiceUtility().getPromotional_Image();
				orderModeList =CommonUtility.customServiceUtility().getOrderModeList();
				sitePromoBannerList =CommonUtility.customServiceUtility().getDropDownBanner();
				dropPromoBannerList=CommonUtility.customServiceUtility().getCartPopUpPromoImages();
				CommonUtility.customServiceUtility().getOrderModeList();
				netWorkwareHouseList = CommonUtility.customServiceUtility().LoadNetworkWareHouseList();
				activewareHouseList = CommonUtility.customServiceUtility().LoadactiveWareHouseList();
				zipCodeShipBranches = CommonUtility.customServiceUtility().getWebsiteZipCodeCustomTable();
				accRepList = CommonUtility.customServiceUtility().getSales_Rep_Details();
				cimmTaxTable = CommonUtility.customServiceUtility().loadCIMMTaxTable();
				shipViaMapDetailsList = CommonUtility.customServiceUtility().LoadShipViaMapDetails();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String checkForTheme() {
		String themeName = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
			SimpleDateFormat formatterTwo = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date date = new Date();
			String currentDay = CommonUtility.validateString(formatter.format(date));
			String forDateParameter = CommonUtility.validateString(formatterTwo.format(date));
			String domainName = request.getScheme() + "://" + request.getServerName();
			String webthemePath = CommonUtility
					.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES"));
			String siteName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
			String folderName = "/css/";
			String filePathURL = CommonUtility.validateString(domainName) + "/" + webthemePath + "/" + siteName
					+ folderName;
			System.out.println(domainName + " : checkForTheme Today : " + formatter.format(date));

			if (!CommonUtility.validateString(themeDate).equalsIgnoreCase(currentDay)) {
				Themes themes = ThemesDao.getScheduledTheme(currentDay);

				if (themes != null && CommonUtility.validateString(themes.getCssName()).toLowerCase().contains(".css")
						&& !CommonUtility.validateString(themes.getCssName()).toLowerCase().contains("default")) {
					filePathURL = filePathURL + CommonUtility.validateString(themes.getCssName());
					if (CommonUtility.validateHttpUrlResponse(filePathURL)) {
						String scheduledThemeFileName = CommonUtility
								.validateString(CommonUtility.validateString(themes.getCssName())) + "?dt="
								+ CommonUtility.validateString(forDateParameter);
						CommonDBQuery.getSystemParamtersList().put("ScheduledThemeFileName",
								CommonUtility.validateString(scheduledThemeFileName));
					} else {
						CommonDBQuery.getSystemParamtersList().put("ScheduledThemeFileName", null);
					}
				} else {
					CommonDBQuery.getSystemParamtersList().put("ScheduledThemeFileName", null);
				}
				themeDate = CommonUtility.validateString(currentDay);
			}

			themeName = CommonDBQuery.getSystemParamtersList().get("ScheduledThemeFileName");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return themeName;
	}

	public static void getValueList() {
		try {
			eventLocations = ValueListDao.getValueList("$EventLocations");
			eventCategories = ValueListDao.getValueList("$EventCategories");
			eventTypes = ValueListDao.getValueList("$EventType");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getProductPromotionList() {
		System.out.println("Getting Product Promotion");
		try {
			hotDealsList = new LinkedHashMap<>();
			hotDealsList = ProductHunterSolr.getPromotionDeals("isHotDeal");
			newProductList = new LinkedHashMap<>();
			newProductList = ProductHunterSolr.getPromotionDeals("isNewProduct");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void getDefaultCategoryBanners() {

		if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DefaultCategoryBanners"))
				.equalsIgnoreCase("Y")) {
			List<CustomTable> defaultBanners = CIMM2VelocityTool.getInstance().getCusomTableData("Website","DEFAULT_BANNER_CATEGORY");
			LinkedHashMap<String, String> defaultBanner = new LinkedHashMap<>();
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> allBanners = new LinkedHashMap<>();
			for (CustomTable eachBanner : defaultBanners) {
				for (Map<String, String> each : eachBanner.getTableDetails()) {
					defaultBanner.put("TOP", each.get("TOP_BANNER"));
					defaultBanner.put("LEFT", each.get("LEFT_BANNER"));
					defaultBanner.put("RIGHT", each.get("RIGHT_BANNER"));
					defaultBanner.put("BOTTOM", each.get("BOTTOM_BANNER"));
				}
			}
			if (CommonUtility.validateString(defaultBanner.get("TOP")).length() > 0) {
				allBanners.put("TOP", ProductsDAO.getBannerDetails(defaultBanner.get("TOP"), "BANNER_NAME"));
			}
			if (CommonUtility.validateString(defaultBanner.get("LEFT")).length() > 0) {
				allBanners.put("LEFT", ProductsDAO.getBannerDetails(defaultBanner.get("LEFT"), "BANNER_NAME"));
			}
			if (CommonUtility.validateString(defaultBanner.get("RIGHT")).length() > 0) {
				allBanners.put("RIGHT", ProductsDAO.getBannerDetails(defaultBanner.get("RIGHT"), "BANNER_NAME"));
			}
			if (CommonUtility.validateString(defaultBanner.get("BOTTOM")).length() > 0) {
				allBanners.put("BOTTOM", ProductsDAO.getBannerDetails(defaultBanner.get("BOTTOM"), "BANNER_NAME"));
			}
			defaultCategoryBanners = new LinkedHashMap<>();
			defaultCategoryBanners = allBanners;
		}
	}

	public static void getShipViaList() {
		try {
			CustomModel customFieldModel = new CustomModel();
			customFieldModel.setCustomFieldEntityType("WEBSITE");// Type
			customFieldModel.setCustomFieldEntityId(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));//Site ID
			customFieldModel.setCustomFieldName("SHIPVIA_COST_TABLE");// Custom Field Table Name
			ArrayList<ShipVia> shipViaList = new ArrayList<>();
			shipViaList = CustomFieldDAO.getShipViaDetailsFromCustomFieldTable(customFieldModel);
			SiteShipViaList = shipViaList;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getSwatchList() {

		try {
			List<CustomTable> val = CIMM2VelocityTool.getInstance().getCusomTableData("Website", "COLOR_FACET_DISPLAY");
			if (!val.isEmpty()) {
				SwatchModel swatch = null;
				swatchValueList = new LinkedHashMap<>();
				List<Map<String, String>> tableDetails = val.get(0).getTableDetails();
				for (Map<String, String> swatchList : tableDetails) {
					swatch = new SwatchModel();
					swatch.setColorCode(swatchList.get("COLOR_CODE"));
					swatch.setSwathcImage(swatchList.get("IMAGE_NAME"));
					swatchValueList.put(swatchList.get("COLOR_VALUE").toUpperCase(), swatch);
					System.out.println(swatchList.get("COLOR_VALUE") + " - " + swatchList.get("COLOR_CODE"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void getWareHouseParameters() {
		System.out.println("Getting Ware House Parameters");
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = ConnectionManager.getDBConnection();
			stmt = conn.createStatement();
			String sql = "SELECT WAREHOUSE_ID,WAREHOUSE_CODE,WAREHOUSE_NAME FROM WAREHOUSE WHERE STATUS = 'Y'";
			rs = stmt.executeQuery(sql);
			wareHouseList = new LinkedHashMap<>();
			while (rs.next()) {
				wareHouseList.put(CommonUtility.validateString(rs.getString("WAREHOUSE_CODE")),CommonUtility.validateString(rs.getString("WAREHOUSE_NAME")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
	}

	public static void getWareHouseCustomFields() {
		System.out.println("Getting Ware House Custom fields");
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = ConnectionManager.getDBConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM WAREHOUSE_CF_VALUES_VIEW ORDER BY WAREHOUSE_ID";
			rs = stmt.executeQuery(sql);
			wareHouseCustomFieldsList = new LinkedHashMap<>();

			while (rs.next()) {
				ArrayList<CustomFieldModel> customFieldsList = new ArrayList<>();
				CustomFieldModel customFieldModel = new CustomFieldModel(rs.getInt("WAREHOUSE_ID"),
						rs.getString("FIELD_NAME"), rs.getString("FIELD_VALUE"));
				if (wareHouseCustomFieldsList.containsKey(rs.getString("WAREHOUSE_ID"))) {
					wareHouseCustomFieldsList.get(rs.getString("WAREHOUSE_ID")).add(customFieldModel);
				} else {
					customFieldsList.add(customFieldModel);
					wareHouseCustomFieldsList.put(rs.getString("WAREHOUSE_ID").trim(), customFieldsList);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
	}

	public static void getCreditCardTypeCode() {
		System.out.println("Getting Credit Card Type Code");
		try {
			creditCardErpCode = new LinkedHashMap<>();
			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CreditCardTypeCode")).equalsIgnoreCase("Y")) {
				List<CustomTable> websiteCardTypeCode = CIMM2VelocityTool.getInstance().getCusomTableData("WEBSITE","CREDIT_CARD_ERP_CODE");
				for (CustomTable eachCode : websiteCardTypeCode) {
					for (Map<String, String> each : eachCode.getTableDetails()) {
						creditCardErpCode.put(CommonUtility.validateString(each.get("CREDIT_CARD_CODE")), CommonUtility.validateString(each.get("CREDIT_CARD_TYPE")));						
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getLocaleListFromDatabase() {
		System.out.println("Getting Locale detailes");
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {

			conn = ConnectionManager.getDBConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM LOCALE";
			rs = stmt.executeQuery(sql);
			localeList = new LinkedHashMap<>();

			while (rs.next()) {
				LocaleModel locale = new LocaleModel();
				locale.setLocaleId(rs.getInt("LOCALE_ID"));
				locale.setCountryCode(CommonUtility.validateString(rs.getString("COUNTRY_CODE")));
				locale.setCountryName(CommonUtility.validateString(rs.getString("COUNTRY_NAME")));
				locale.setLanguageCode(CommonUtility.validateString(rs.getString("LANGUAGE_CODE")));
				locale.setLanguageName(CommonUtility.validateString(rs.getString("LANGUAGE_NAME")));
				locale.setVariant(CommonUtility.validateString(rs.getString("VARIANT")));
				locale.setImageName(CommonUtility.validateString(rs.getString("IMAGE_NAME")));
				locale.setStatus(CommonUtility.validateString(rs.getString("STATUS")));

				localeList.put(CommonUtility.validateString(rs.getString("LANGUAGE_CODE")), locale);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
	}

	public static void getSystemParameters() {
		System.out.println("------------- Getting SysParamter -------------------");
		PreparedStatement preStat = null;
		ResultSet rs = null;
		Connection conn = null;
		String siteName = null;
		String domainName = null;
		
		if (request != null) {
			System.out.println("-------------------------request was not null----------------------");
			siteName = request.getServerName();
			domainName = request.getScheme() + "://" + request.getServerName();
			System.out.println("getServerName :"+siteName+"  ------ domainName :"+domainName);
			if(CommonUtility.validateString(siteName).length()<1 || CommonUtility.validateString(request.getScheme()).length()<1) {
				request = ServletActionContext.getRequest();
				siteName = request.getServerName();
				domainName = request.getScheme() + "://" + request.getServerName();
				System.out.println("getServerName :"+siteName+"  ------ domainName :"+domainName);
			}
		} else {
			System.out.println("--------------------------request was null--------------------------");
			request = ServletActionContext.getRequest();
			siteName = request.getServerName();
			domainName = request.getScheme() + "://" + request.getServerName();
			System.out.println("getServerName :"+siteName+"  ------ domainName :"+domainName);
		}
		System.out.println("domainName : " + domainName);
		if (InetAddressUtils.isIPv4Address(CommonUtility.validateString(siteName))
				|| CommonUtility.validateString(siteName).contains("localhost")
				|| CommonUtility.validateString(siteName).contains(localEnvironmentIPAddress)
				|| CommonUtility.validateString(siteName).contains(localEnvironmentPublicIPAddress)) {
			System.out.println("Inside Local System Config Block");
			siteName = localSiteURL;
			setLocalDevelopmentEnvironment(true);
		}
		System.out.println("CommonDBQuery siteName @getSystemParameters:	" + CommonUtility.validateString(siteName));
		int siteID = getSiteID(siteName);
		globalSiteId = siteID;
		System.out.println("siteID @ CommonDBQuery" + siteID);

		LinkedHashMap<String, String> basicConfigs = checkMultiSite();
		FileInputStream fis = null;
		try {
			systemParametersList = new LinkedHashMap<>();
			systemParameterConfigKeyAndParamIdList = new LinkedHashMap<String, String>();
			conn = ConnectionManager.getDBConnection();
			if (CommonUtility.validateString(basicConfigs.get("MULTI_SITE")).length() > 0 && basicConfigs.get("MULTI_SITE").trim().equalsIgnoreCase("Y")) {
				preStat = conn.prepareStatement("SELECT CONFIG_KEY, CONFIG_VALUE, SYSTEM_PARAMETER_ID FROM SYSTEM_PARAMETERS_VIEW WHERE SITE_ID=" + siteID);
				systemParametersList.put("MULTI_SITE", "Y");
			} else {
				systemParametersList.put("MULTI_SITE", "N");
				preStat = conn.prepareStatement("SELECT CONFIG_KEY, CONFIG_VALUE, SYSTEM_PARAMETER_ID FROM SYSTEM_PARAMETERS");
			}
			rs = preStat.executeQuery();
			while (rs.next()) {
				systemParametersList.put(rs.getString("CONFIG_KEY"), rs.getString("CONFIG_VALUE"));
				systemParameterConfigKeyAndParamIdList.put(rs.getString("CONFIG_KEY"), rs.getString("SYSTEM_PARAMETER_ID"));
				System.out.println(rs.getString("CONFIG_KEY") + " : " + rs.getString("CONFIG_VALUE"));
			}
			systemParametersList.put("DOMAIN_NAME", domainName);
			systemParametersList.put("SITE_ID", "" + siteID);
			if (systemParametersList.get("ACTIVE_TAXONOMY_VERSION") != null && !systemParametersList.get("ACTIVE_TAXONOMY_VERSION").trim().equalsIgnoreCase("")) {
				systemParametersList.put("TAXONOMY_ID", UsersDAO.getTaxonomyByActiveMenu(systemParametersList.get("ACTIVE_TAXONOMY_VERSION")));
			}
			systemParametersList.put("MOOD_NAME", "default");
			if (systemParametersList.get("WILL_CALL_EXCLUDE_BRAND_NAME") != null && !systemParametersList.get("WILL_CALL_EXCLUDE_BRAND_NAME").trim().equalsIgnoreCase("")) {
				String brandId = "";
				String brandIdDelimiter = "";
				String[] brandSplit = systemParametersList.get("WILL_CALL_EXCLUDE_BRAND_NAME").split(",");
				for (String brandName : brandSplit) {
					brandId = brandId + brandIdDelimiter + getBrandIdFromBrandName(brandName);
					brandIdDelimiter = ",";
				}
				systemParametersList.put("WILL_CALL_EXCLUDE_BRAND_ID", brandId);
			}
			String cimm2BCentralUserName = systemParametersList.get("CIMM2BCENTRAL_USERNAME");
			String cimm2BCentralPassword = systemParametersList.get("CIMM2BCENTRAL_PASSWORD");
			if (CommonUtility.validateString(cimm2BCentralUserName).length() > 0 && CommonUtility.validateString(cimm2BCentralPassword).length() > 0) {
				cimm2bCentralAuthorization = "Basic " + Base64Encoder.encode(cimm2BCentralUserName + ":" + cimm2BCentralPassword);
			}
			setElementURL(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_TRANSACTION_URL")));
			systemParametersList = getSiteMood(siteName, systemParametersList);
			if (InetAddressUtils.isIPv4Address(CommonUtility.validateString(request.getServerName()))
					|| CommonUtility.validateString(request.getServerName()).contains("localhost")
					|| CommonUtility.validateString(siteName).contains(localEnvironmentIPAddress)
					|| CommonUtility.validateString(siteName).contains(localEnvironmentPublicIPAddress)) {
				// ----------------------
				systemParametersList = localSystemParameter(systemParametersList);
				// ----------------------
			}
			if (CommonUtility.validateString(systemParametersList.get("STORE_MANUFACTURER_CUSTOM_FIELDS"))
					.equalsIgnoreCase("Y")) {
				manfCustomField = getManufacturerCustomFields();
			}
			System.out.println(systemParametersList.get("MOOD_NAME"));
			if(PropertyAction.getVersionControl()!=null) {
				systemParametersList.putAll(PropertyAction.getVersionControl());
			}
			systemParametersListVal = new LinkedHashMap<String, String>();
			systemParametersListVal = systemParametersList;
			/*fis = new FileInputStream(systemParametersList.get("ERP_API_PROPERTIES_FILE"));
			props = new Properties();
			props.load(fis);*/
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(preStat);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeInputStream(fis);
		}
		System.out.println("------------- Getting SysParamter -------------------");
	}

	public static void getSysParamServlet(HttpServletRequest requestSer) {
		request = requestSer;
		getSystemParameters();
	}

	public static LinkedHashMap<String, String> getManufacturerCustomFields() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = null;
		Connection conn = null;

		LinkedHashMap<String, String> manufacturerCustomField = new LinkedHashMap<String, String>();
		try {

			strQuery = "select * from MFR_CF_VALUES_VIEW";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				manufacturerCustomField.put(
						rs.getInt("MANUFACTURER_ID") + "_" + rs.getString("FIELD_NAME").replaceAll("\\s+", "_"),
						rs.getString("FIELD_VALUE"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return manufacturerCustomField;
	}

	public static int getSiteID(String siteUrl) {
		int siteID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = null;
		Connection conn = null;

		try {

			strQuery = "SELECT * FROM WEBSITES WHERE STATUS='Y'";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			websiteListArray = new ArrayList<WebsiteModel>();
			while (rs.next()) {
				if (CommonUtility.validateString(rs.getString("SITE_URL")).contains(siteUrl)) {
					siteID = rs.getInt("SITE_ID");
					webSiteName = rs.getString("SITE_NAME");
					jiraKey = rs.getString("JIRA_KEY");
					setSiteLogo(rs.getString("SITE_LOGO"));
				}
				WebsiteModel websiteModelObj = new WebsiteModel();
				websiteModelObj.setSiteId(rs.getInt("SITE_ID")); 
				websiteModelObj.setSiteName(rs.getString("SITE_NAME"));
				websiteModelObj.setSiteUrl(rs.getString("SITE_URL"));
				websiteModelObj.setUserEdited(rs.getInt("USER_EDITED"));
				websiteModelObj.setStatus(rs.getString("STATUS"));
				websiteModelObj.setSiteDesignId(rs.getInt("SITE_DESIGN_ID"));
				websiteModelObj.setSiteMoodId(rs.getInt("MOOD_ID"));
				websiteModelObj.setPrimarySite(rs.getString("PRIMARY_SITE"));
				websiteModelObj.setWflPhaseId(rs.getInt("WFL_PHASE_ID"));
				websiteModelObj.setSiteLogo(rs.getString("SITE_LOGO"));
				websiteModelObj.setSiteClientId(rs.getInt("CLIENT_ID"));
				websiteModelObj.setSiteTaxonomyId(rs.getInt("TAXONOMY_ID"));
				websiteModelObj.setSiteSubset(rs.getInt("SUBSET_ID"));
				websiteListArray.add(websiteModelObj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return siteID;
	}

	public static LinkedHashMap<String, String> getSiteMood(String siteUrl,
			LinkedHashMap<String, String> systemParametersList) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = null;
		Connection conn = null;

		try {

			strQuery = "SELECT SD.SITE_DESIGN_NAME,SD.SITE_DESIGN_ID,SM.MOOD_NAME,SM.MOOD_ID FROM WEBSITES WS, SITE_MOOD SM,SITE_DESIGNS SD WHERE WS.STATUS !='D' AND UPPER(SITE_URL) LIKE UPPER('%"
					+ siteUrl + "%') AND SM.MOOD_ID(+) = WS.MOOD_ID AND SD.SITE_DESIGN_ID(+)=WS.SITE_DESIGN_ID";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("SiteMood Name :" + rs.getString("MOOD_NAME"));
				System.out.println("SiteDesign Name :" + rs.getString("SITE_DESIGN_NAME"));
				if (rs.getInt("SITE_DESIGN_ID") > 0 && rs.getString("SITE_DESIGN_NAME") != null
						&& !rs.getString("SITE_DESIGN_NAME").isEmpty()) {
					systemParametersList.put("SITE_NAME", rs.getString("SITE_DESIGN_NAME"));
				}

				if (rs.getInt("MOOD_ID") > 0 && rs.getString("MOOD_NAME") != null
						&& !rs.getString("MOOD_NAME").isEmpty()) {
					systemParametersList.put("MOOD_NAME", rs.getString("MOOD_NAME"));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return systemParametersList;
	}

	public static int getSequenceId(String seqName) {
		int sequenceId = 0;
		Statement stmt = null;
		ResultSet rs = null;
		String strQuery = null;
		Connection conn = null;
		try {
			conn = ConnectionManager.getDBConnection();
			stmt = conn.createStatement();
			strQuery = PropertyAction.SqlContainer.get("getNextSequenceId");
			strQuery = strQuery.replace("^SEQUENCENAME^", seqName);
			rs = stmt.executeQuery(strQuery);
			if (rs.next())
				sequenceId = rs.getInt("SEQVAL");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return sequenceId;
	}

	public static void getBranchStorage() {
		System.out.println("Getting Branch Data");
		branchDetailData = new LinkedHashMap<String, ProductsModel>();
		Statement stmt = null;
		PreparedStatement pstmt0 = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			String branchCheckQuery = "SELECT * FROM WAREHOUSE WHERE WAREHOUSE_CODE NOT IN (SELECT BRANCH_ID FROM BRANCH_LIST)";
			conn = ConnectionManager.getDBConnection();
			pstmt0 = conn.prepareStatement(branchCheckQuery);
			rs = pstmt0.executeQuery();
			while (rs.next()) {
				ConnectionManager.closeDBPreparedStatement(pstmt);
				int addressBookId = CommonDBQuery.getSequenceId("BRANCH_LIST_ID_SEQ");
				String insertQuery = "INSERT INTO BRANCH_LIST (BRANCH_LIST_ID,BRANCH_ID,BRANCH_NAME,ADDRESS,CITY,STATE,COUNTRY,POSTAL_CODE,LONGITUDE,LATITUDE,UPDATED_DATETIME)VALUES(?,?,?,?,?,?,?,?,?,?,SYSDATE)";
				pstmt = conn.prepareStatement(insertQuery);
				pstmt.setInt(1, addressBookId);
				pstmt.setString(2, rs.getString("WAREHOUSE_CODE"));
				pstmt.setString(3, rs.getString("WAREHOUSE_NAME"));
				pstmt.setString(4, rs.getString("ADDRESS1") + " " + rs.getString("ADDRESS2"));
				pstmt.setString(5, rs.getString("CITY"));
				pstmt.setString(6, rs.getString("STATE"));
				pstmt.setString(7, rs.getString("COUNTRY"));
				pstmt.setString(8, rs.getString("ZIP"));
				YahooBossSupport getUserLocation = new YahooBossSupport();
				UsersModel locaDetail = new UsersModel();
				locaDetail.setZipCode(rs.getString("ZIP").trim());
				locaDetail.setCity(rs.getString("CITY"));
				locaDetail = getUserLocation.locateUser(locaDetail);
				String userLati = locaDetail.getLatitude();
				String userLongi = locaDetail.getLongitude();
				pstmt.setString(9, userLongi);
				pstmt.setString(10, userLati);
				pstmt.executeUpdate();
			}
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);

			stmt = conn.createStatement();
			String sql = "SELECT WI.BRANCH_ID, WI.WAREHOUSE_ID,  WI.WAREHOUSE_CODE,  WI.WAREHOUSE_NAME,  WI.ADDRESS1,  WI.ADDRESS2,  WI.COUNTRY, WI.EMAIL,  WI.CITY,  WI.STATE,  WI.ZIP,  WI.LONGITUDE,  WI.LATITUDE,  WI.PHONE_NUMBER, WI.TOLL_FREE_NUMBER, NVL(CFV.TEXT_FIELD_VALUE,CF.DEFAULT_VALUE) WORKING_HOURS,  WI.WORK_HOUR, WI.SUBSET_ID, WI.NOTE FROM  (SELECT W.WAREHOUSE_ID,    W.WAREHOUSE_CODE,    W.WORK_HOUR,    W.WAREHOUSE_NAME,    W.ADDRESS1,    W.ADDRESS2,    W.COUNTRY,W.EMAIL,    W.CITY,    W.STATE,    W.ZIP,    BL.BRANCH_ID,    W.LATTITUDE LATITUDE,    W.LONGITUDE,    W.PHONE_NUMBER,  W.TOLL_FREE_NUMBER, W.SUBSET_ID, W.NOTE FROM BRANCH_LIST BL,    WAREHOUSE W  WHERE BL.BRANCH_ID=W.WAREHOUSE_CODE AND W.STATUS = 'Y') WI,  (SELECT LCV.TEXT_FIELD_VALUE,    WV.WAREHOUSE_ID  FROM WAREHOUSE_CUSTOM_FIELD_VALUES WV,    LOC_CUSTOM_FIELD_VALUES LCV,    CUSTOM_FIELDS CF  WHERE CF.FIELD_NAME                 = 'WorkingHours'  AND LCV.LOC_CUSTOM_FIELD_VALUE_ID(+)=WV.LOC_CUSTOM_FIELD_VALUE_ID  AND CF.CUSTOM_FIELD_ID(+)           =WV.CUSTOM_FIELD_ID  ) CFV,  (SELECT * FROM CUSTOM_FIELDS WHERE FIELD_NAME='WorkingHours'  ) CF WHERE WI.WAREHOUSE_ID=CFV.WAREHOUSE_ID(+)";
			ULLog.debug(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ProductsModel branchDataPresent = new ProductsModel();
				branchDataPresent.setBranchID(rs.getString("BRANCH_ID").trim());
				branchDataPresent.setWareHouseCode(rs.getString("WAREHOUSE_ID"));
				branchDataPresent.setBranchName(rs.getString("WAREHOUSE_NAME").trim());
				branchDataPresent.setBranchAddress1(rs.getString("ADDRESS1"));
				branchDataPresent.setBranchAddress2(rs.getString("ADDRESS2"));
				branchDataPresent.setBranchCity(rs.getString("CITY"));
				branchDataPresent.setBranchState(rs.getString("STATE"));
				branchDataPresent.setBranchCountry(rs.getString("COUNTRY"));
				branchDataPresent.setBranchPostalCode(rs.getString("ZIP"));
				branchDataPresent.setBranchLongitude(rs.getString("LONGITUDE"));
				branchDataPresent.setBranchLatitude(rs.getString("LATITUDE"));
				branchDataPresent.setBranchEmail(rs.getString("EMAIL"));
				branchDataPresent.setSubsetId(CommonUtility.validateNumber(rs.getString("SUBSET_ID")));
				if (CommonUtility.validateString(rs.getString("WORK_HOUR")).length() > 0) {
					branchDataPresent.setBranchWorkingHours(rs.getString("WORK_HOUR"));
				} else {
					branchDataPresent.setBranchWorkingHours(rs.getString("WORKING_HOURS"));
				}
				branchDataPresent.setBranchTollFreeNumber(rs.getString("TOLL_FREE_NUMBER"));
				branchDataPresent.setBranchPhoneNumber(rs.getString("PHONE_NUMBER"));
				branchDataPresent.setNotes(rs.getString("NOTE"));
				if (rs.getString("BRANCH_ID") != null) {
					branchDetailData.put(rs.getString("BRANCH_ID"), branchDataPresent);
				}

			}
		} catch (SQLException e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} catch (Exception e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBPreparedStatement(pstmt0);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
	}

	public static void getDefaultFacetField() {
		System.out.println("Getting Default Facet");
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			VelocityEngine velocityTemplateEngine = new VelocityEngine();
			String templatePath = CommonDBQuery.getSystemParamtersList().get("SITE_TEMPLATE_PATH")
					+ CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
			System.out.println("templatePath : " + templatePath);
			velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
			velocityTemplateEngine.init();

			conn = ConnectionManager.getDBConnection();
			stmt = conn.createStatement();
			String sql = "SELECT ATTR_NAME FROM DEFAULT_FILTER_ATTRIBUTE WHERE STATUS='A' ORDER BY DISPLAY_SEQ ";
			rs = stmt.executeQuery(sql);
			defaultFacet = new ArrayList<String>();
			while (rs.next()) {
				defaultFacet.add(rs.getString("ATTR_NAME"));
			}
		} catch (SQLException e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} catch (Exception e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
	}

	public static String getAutocompleteConfig() {
		System.out.println("Auto Complete Configurations Loaded");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		String brandID = "";
		AutoComplete.manufacturerAuto = "N";
		AutoComplete.brandAuto = "N";
		AutoComplete.attributesAuto = "N";
		AutoComplete.eventsAuto = "N";
		AutoComplete.staticAuto = "N";
		try {
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT FIELD_ID, REQUIRED_FIELD_NAME FROM AUTOCOMPLETE_CONFIG ORDER BY FIELD_ID ASC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			LinkedHashMap<Integer, String> sorting = new LinkedHashMap<Integer, String>();
			while (rs.next()) {
				sorting.put(rs.getInt("FIELD_ID"), rs.getString("REQUIRED_FIELD_NAME"));
				if (CommonUtility.validateString(rs.getString("REQUIRED_FIELD_NAME")).length() > 0) {
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("MANUFACTURER")) {
						AutoComplete.manufacturerAuto = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("BRAND")) {
						AutoComplete.brandAuto = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("ATTRIBUTES")) {
						AutoComplete.attributesAuto = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("EVENTS")) {
						AutoComplete.eventsAuto = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("STATICPAGES")) {
						AutoComplete.staticAuto = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("MODELPART")) {
						AutoComplete.modelPart = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("EQUIPMENT")) {
						AutoComplete.newEquipmentAuto = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("PARTS")) {
						AutoComplete.partsAuto = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("CATEGORY_FAYT")) {
						AutoComplete.categoryFAYT = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("BRAND_FAYT")) {
						AutoComplete.brandFAYT = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("ATTRIBUTE_CATEGORY_FAYT")) {
						AutoComplete.attributeCategoryFAYT = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("BRAND_CATEGORY_FAYT")) {
						AutoComplete.brandCategoryFAYT = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("ATTRIBUTE_CATEGORY_BRAND_FAYT")) {
						AutoComplete.attributeCategoryBrandFAYT = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("CATEGORY")) {
						AutoComplete.categoryAuto = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("PART_NUMBER")) {
						AutoComplete.partNumberAutoSuggest = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("CUSTOMER_PART_NUMBER")) {
						AutoComplete.customerNumberAutoSuggest = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("MANUFACTURER_PART_NUMBER")) {
						AutoComplete.manufacturerPartNumberAuto = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("UPC")) {
						AutoComplete.upcAutoSuggest = "Y";
					}
					if (rs.getString("REQUIRED_FIELD_NAME").trim().equalsIgnoreCase("ITEM")) {
						AutoComplete.itemPartNumberAutoSuggest = "Y";
					}

				}
			}
			AutoComplete.displaySeq = new LinkedHashMap<Integer, String>();
			AutoComplete.displaySeq = sorting;
		} catch (SQLException e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} catch (Exception e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return brandID;
	}

	public static String getBrandIdFromBrandName(String brandName) {
		System.out.println("Brand ID From Brand Name for Will Call");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		String brandID = "";
		try {
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT BRAND_ID FROM BRANDS WHERE UPPER(BRAND_NAME) =UPPER(?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, brandName);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				brandID = rs.getString("BRAND_ID");
			}
		} catch (SQLException e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} catch (Exception e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return brandID;
	}

	public static LinkedHashMap<String, String> checkMultiSite() {
		System.out.println("Checking Multi Site Config");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		LinkedHashMap<String, String> fullValue = new LinkedHashMap<String, String>();
		try {
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT MULTI_SITE_SUPPORT FROM CIMM_VERSION_INFO";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fullValue.put("MULTI_SITE", rs.getString("MULTI_SITE_SUPPORT"));
			}
			if (CommonUtility.validateString(fullValue.get("MULTI_SITE")).length() > 0) {

			}
		} catch (SQLException e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} catch (Exception e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return fullValue;
	}

	public static LinkedHashMap<String, String> getSystemParamtersList() {
		return systemParametersList;
	}

	public static void setSystemParamtersList(LinkedHashMap<String, String> systemParametersList) {
		CommonDBQuery.systemParametersList = systemParametersList;
	}

	public static void setWareHouseList(LinkedHashMap<String, String> wareHouseList) {
		CommonDBQuery.wareHouseList = wareHouseList;
	}

	public static LinkedHashMap<String, String> getWareHouseList() {
		return wareHouseList;
	}

	public static void setPopularSearchFileName(String popularSearchFileName) {
		CommonDBQuery.popularSearchFileName = popularSearchFileName;
	}

	public static String getPopularSearchFileName() {
		return popularSearchFileName;
	}

	/*public static void setProps(Properties props) {
		CommonDBQuery.props = props;
	}

	public static Properties getProps() {
		return props;
	}*/

	public static ArrayList<String> getDefaultFacet() {
		return defaultFacet;
	}

	public static void setDefaultFacet(ArrayList<String> defaultFacet) {
		CommonDBQuery.defaultFacet = defaultFacet;
	}

	public static void startAbandonedCartTimer() {
		if (CommonUtility.validateString(systemParametersList.get("ENABLE_BRONTO_ABANDONED_CART_EMAIL"))
				.equalsIgnoreCase("Y")) {
			if (time != null) {
				time.cancel();
				time.purge();
			}
			int interval = CommonUtility.validateNumber(
					CommonDBQuery.getSystemParamtersList().get("BRONTO_ABANDAONEDCART_CHECK_INTERVAL") + "");
			time = new Timer(); // Instantiate Timer Object
			BrontoAbandonedCartTimerTask abandonedCartTimerTask = new BrontoAbandonedCartTimerTask(); // Instantiate
																										// SheduledTask
																										// class
			time.schedule(abandonedCartTimerTask, 0, interval * 60 * 1000);

		} else {
			if (time != null) {
				time.cancel();
				time.purge();
			}
		}
	}

	public static void startTimerForScheduledTheme() {
		try {
			if (CommonUtility
					.validateString(CommonDBQuery.getSystemParamtersList().get("START_TIMER_FOR_SCHEDULED_THEME"))
					.equalsIgnoreCase("Y")) {
				String zoneIdOffSet = "US/Central";
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ZONE_ID_OFF_SET"))
						.length() > 0) {
					zoneIdOffSet = CommonUtility
							.validateString(CommonDBQuery.getSystemParamtersList().get("ZONE_ID_OFF_SET"));
				}
				LocalDateTime localNow = LocalDateTime.now();
				System.out
						.println("-------------------- startTimerForScheduledTheme Initiated ------------------------");
				System.out.println("getDayOfMonth : " + localNow.getDayOfMonth());
				System.out.println("getDayOfWeek : " + localNow.getDayOfWeek());
				System.out.println("getMonth : " + localNow.getMonth());
				System.out.println("getMonth : " + localNow.getMonth());
				System.out.println("getMinute : " + localNow.getMinute());
				System.out.println("getSecond : " + localNow.getSecond());
				System.out.println("getMonthValue : " + localNow.getMonthValue());
				System.out.println("---------------------- startTimerForScheduledTheme ----------------------");
				ZoneId currentZone = ZoneId.of(zoneIdOffSet);
				ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
				ZonedDateTime zonedNext00;
				zonedNext00 = zonedNow.withHour(0).withMinute(0).withSecond(10);
				if (zonedNow.compareTo(zonedNext00) > 0) {
					zonedNext00 = zonedNext00.plusDays(1);
				}

				Duration duration = Duration.between(zonedNow, zonedNext00);
				long initalDelay = duration.getSeconds();

				ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
				scheduler.scheduleAtFixedRate(new ThemeGeneratorScheduledCheck(), initalDelay, 24 * 60 * 60,
						TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copyCMSPageContentToFile(String staticPageId) {
		try {
			String htmlString = "";
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			CIMMTouchUtility.getInstance().getXmlContent(staticPageId, contentObject);
			
			if(contentObject!=null && contentObject.get("pageContent")!=null && CommonUtility.validateString(contentObject.get("pageContent").toString()).length()>0) {
				htmlString = CommonUtility.validateString(contentObject.get("pageContent").toString());
			}
			Document doc = Jsoup.parse(htmlString);
			if(doc!=null && CommonUtility.validateString(doc.toString()).length()>0) {
				Elements bannerBlockList = doc.select("[data-select=\"bannerBlock\"]");
				for (Element bannerBlock : bannerBlockList) {
					String bannerID = bannerBlock.attr("data-bannerid");
					
					HttpURLConnection httpConnection = (HttpURLConnection) new URL(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"))+"/bannerApi.action?bannerListId="+bannerID).openConnection();
					httpConnection.setDoOutput(true);
					BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
					String line = null;
					StringBuffer responseData = new StringBuffer();
					while((line = in.readLine()) != null) {
						responseData.append(line);
					}
					
					String htmlInput = responseData.toString().replaceAll("&([^;]+?);", "**$1;");
					Document docBannerBlock = Jsoup.parse(htmlInput);
					docBannerBlock.outputSettings().prettyPrint(false).escapeMode(EscapeMode.extended);
					JSONParser parser = new JSONParser();
					JSONObject json = (JSONObject) parser.parse(docBannerBlock.body().html().replaceAll("\\*\\*([^;]+?);", "&amp;$1;"));
					
					String listStartDate = (String)json.get("startDate");
					String listEndDate = (String)json.get("expiryDate");
					String listStatus = (String)json.get("status");
					
					bannerBlock.attr("data-liststartdate", CommonUtility.validateString(listStartDate));
					bannerBlock.attr("data-listenddate", CommonUtility.validateString(listEndDate));
					bannerBlock.attr("data-liststatus", CommonUtility.validateString(listStatus));
					//bannerBlock.attr("data-liststartdate", CommonUtility.validateString("05/22/2019"));
					//bannerBlock.attr("data-listenddate", CommonUtility.validateString("05/22/2019"));
					//bannerBlock.attr("data-liststatus", CommonUtility.validateString("A"));
					
					JSONObject jsonProperties = (JSONObject) json.get("dynamicProperties");
					if(jsonProperties!=null) {
						//String jsonBannerType = (jsonProperties.get("bannerType")!=null?(String)jsonProperties.get("bannerType"):"");
						//String jsonTemplateName = (jsonProperties.get("templateName")!=null?(String)jsonProperties.get("templateName"):"");
						//String jsonCarouselSetting = (jsonProperties.get("carouselSetting")!=null?(String)jsonProperties.get("carouselSetting"):"");
						String jsonbannerTransistion = (jsonProperties.get("bannerTransistion")!=null?(String)jsonProperties.get("bannerTransistion"):"");
						if(CommonUtility.validateString(jsonbannerTransistion).length()>0) {
							bannerBlock.attr("data-transistion", jsonbannerTransistion);
						}else{
							bannerBlock.attr("data-transistion", "");
						}
						String jsonbannerTransistionDelay = (jsonProperties.get("bannerTransistionDelay")!=null?jsonProperties.get("bannerTransistionDelay").toString():"");
						if(CommonUtility.validateString(jsonbannerTransistionDelay).length()>0) {
							bannerBlock.attr("data-transistiondelay", jsonbannerTransistionDelay);
						}else{
							bannerBlock.attr("data-transistiondelay", "");
						}
						String jsonbannerTransistionAutoPlay = (jsonProperties.get("bannerTransistionAutoPlay")!=null?jsonProperties.get("bannerTransistionAutoPlay").toString():"1");
						if(CommonUtility.validateString(jsonbannerTransistionAutoPlay).length()>0) {
							bannerBlock.attr("data-transistionautoplay", jsonbannerTransistionAutoPlay);
						}else{
							bannerBlock.attr("data-transistionautoplay", "1");
						}
					}
					String docOutput = json.get("bannerTemplate").toString();
					bannerBlock.append(docOutput);
				}
				Elements widgetBlockList = doc.select("[data-select=\"widget\"]");
				for (Element widgetBlock : widgetBlockList) {
					String widgetID = widgetBlock.attr("data-widget");
					
					HttpURLConnection httpConnection = (HttpURLConnection) new URL(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"))+"/generateTemplateWidget.action?widgetId="+widgetID).openConnection();
					httpConnection.setDoOutput(true);
					BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
					String line = null;
					StringBuffer responseData = new StringBuffer();
					while((line = in.readLine()) != null) {
						//System.out.println(line);
						responseData.append(line);
					}
					String htmlInput = responseData.toString().replaceAll("&([^;]+?);", "**$1;");
					Document docWidgetBlock = Jsoup.parse(htmlInput);
					docWidgetBlock.outputSettings().prettyPrint(false).escapeMode(EscapeMode.extended);
					widgetBlock.append(docWidgetBlock.body().html().replaceAll("\\*\\*([^;]+?);", "&amp;$1;"));
				}
				String templatePath = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("STATICPAGESPATH"))+staticPageId+".html";
				File fileCheck = new File(templatePath);
				if (fileCheck.exists()) {
					System.out.println("File path exist at copyCMSPageContentToFile");
					if(doc !=null && CommonUtility.validateString(doc.body().html().toString()).length()>0) {
						File newHtmlFile = new File(templatePath);
						FileUtils.writeStringToFile(newHtmlFile, doc.body().html().toString(), "UTF-8");
						System.out.println("Write Complete");
					}else {
						System.out.println("Write Not Complete");
					}
				}else {
					System.out.println("File do not exist at copyCMSPageContentToFile: "+templatePath);
					File file = new File(templatePath);
			        if(file.createNewFile()){
			        	System.out.println("File Created at copyCMSPageContentToFile: "+templatePath);
						if(doc !=null && CommonUtility.validateString(doc.body().html().toString()).length()>0) {
							File newHtmlFile = new File(templatePath);
							FileUtils.writeStringToFile(newHtmlFile, doc.body().html().toString(), "UTF-8");
							System.out.println("Write Complete");
						}else {
							System.out.println("Write Not Complete");
						}
			        }else {
			        	System.out.println("File Create failed at copyCMSPageContentToFile: "+templatePath);
			        }
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void customerDynamicPages() {
		System.out.println("Checking Multi Site Config");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		customerDynamicPagesList = new LinkedHashMap<String, String>();
		try {
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT PAGE_ID, PAGE_NAME, URL FROM CUSTOMER_DYNAMIC_PAGES";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				customerDynamicPagesList.put(CommonUtility.validateString(rs.getString("PAGE_ID")), CommonUtility.validateString(rs.getString("URL")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
	}
	
	public static LinkedHashMap<String, String> localSystemParameter(LinkedHashMap<String, String> systemParametersList) {
		String containerPath = System.getenv("ECOMMERCE_REPO_PATH");
		if(CommonUtility.validateString(containerPath).length()>0) {
			// - leave this block as is 
		}else {
			//systemParametersList.put("WEB_ADDRESS", "http://localhost");
			systemParametersList.put("CDN_URL", "");
			systemParametersList.put("VIRTUALFOLDERPATH", "D:\\VirtualFolder\\deploy\\");
			//systemParametersList.put("APPLICATION_SESSION_TIME_OUT","0");
			systemParametersList.put("VERSION_CONTROL_PATH", localDirectory + "\\Template\\" + (String) systemParametersList.get("SITE_NAME") + "\\PropertiesFiles\\");
			systemParametersList.put("SOLR_URL", "http://localhost:8983/solr");
			//systemParametersList.put("SITE_NAME", "ECOMMERCE_STD_TEMPLATE_V2");
			// systemParametersList.put("ERP","defaults");
			systemParametersList.put("ENABLE_LOG_PRINT_FOR_TIME_ELAPSED", "Y");
			systemParametersList.put("CHECK_EMAIL_ID", "Y");
			systemParametersList.put("SHIPADDRESS_FOR_PUNCHOUT_USER", "N");
			systemParametersList.put("SMC_FILE_PATH", "D:/VirtualFolder/deploy/SMC/");
			systemParametersList.put("DISCOUNT_RULE_URL","https://973ecb.cimm2.com/kiewbwildfly862cr4/maven2/com/unilog/salespromov2/1.0/salespromov2-1.0.jar");
			// systemParametersList.put("DISCOUNT_RULE_URL","https://hermes.cimm2.com/kiewbwildfly862cr4/maven2/com/unilog/salespromov2/1.0/salespromov2-1.0.jar");
			// systemParametersList.put("SSO_ENABLED", "Y");
			systemParametersList.put("SSO_CONFIG_FILE_PATH",localDirectory + "WEB_THEMES.war\\" + (String) systemParametersList.get("SITE_NAME") + "\\files\\SSOConfig\\Oauth2Client.config");
			systemParametersList.put("CREATE_ORDER_API", "/erp/order/createOrder");
			systemParametersList.put("CIMM2BCENTRAL_ACCESS_URL", "https://stage-5400hmi.cimm2.com/cimm2bcentral");
			systemParametersList.put("WL_CHECHOUT_USER_DEFAULT_PASSWORD", "3wFzlOFbeAbw6YN8goC_idg3EFFHtFL92c7jv4pOZ9Y");
			systemParametersList.put("PATH_TO_SAVE_PDF_FILE", "E:\\CreditApplication\\");
			//systemParametersList.put("EMAILRELAY", "3wFzlOFbeAbw6YN8goC_idg3EFFHtFL92c7jv4pOZ9Y");
			systemParametersList.put("EVENT_FROM_MAIL_ID", "webmail@cimm2.com");
			//systemParametersList.put("MAILSELECT", "MODIFIED");
			//systemParametersList.put("FROMMAILID", "webmail@cimm2.com");
			//systemParametersList.put("MAILSMTPPORT", "25");
			systemParametersList.put("ENABLE_PAYMENT_GATEWAY", "Y");
			systemParametersList.put("LOCALE_LIST", "EN");
			systemParametersList.put("ERPTEMPLATEPATH", "D:/ERPWSTEMPLATE");
			systemParametersList.put("DTD_FILE_PATH", "D:/ERPWSTEMPLATE/IDMS-XML.dtd");
			systemParametersList.put("SITE_BUILDER_PROPERTIES_FILE_PATH", localDirectory + "Template\\" + (String) systemParametersList.get("SITE_NAME") + "\\locale");
			systemParametersList.put("SITE_BUILDER_REFRESH_PATH", "http://localhost/ReloadSystemConfig.slt");
			systemParametersList.put("WEB_THEMES", "/WEB_THEMES");
			systemParametersList.put("SITE_TEMPLATE_PATH", localDirectory + "Template\\");
			systemParametersList.put("MACROS_FOLDER", "\\Macros\\");
			systemParametersList.put("BASIC_CONFIG_FILE",localDirectory + "\\Template\\" + (String) systemParametersList.get("SITE_NAME") + "\\PropertiesFiles\\BasicConfiguration.properties");
			systemParametersList.put("WIDGET_PROPERTIES_FILE","D:\\VirtualFolder\\deploy\\WIDGET_DATA.war\\widget.properties");
			systemParametersList.put("BANNER_TEMPLATE_PATH", "D:\\VirtualFolder\\deploy\\BANNERTEMPLATE\\");
			systemParametersList.put("STATICPAGESPATH", "D:/VirtualFolder/deploy/StaticPages/");
			systemParametersList.put("TEMPUPLOADDIRECTORYPATH", "D:/TemporaryUploadContainer");
			systemParametersList.put("KEYWORD_FILE_PATH", "D:/TemporaryUploadContainer/");
			systemParametersList.put("PAYMENT_ACCOUNT_RESPONSE_PAGE", "http://localhost/PaymentResponseSale.action");
			systemParametersList.put("DISABLE_RETAIL_ERP_REGISTRATION", "N");
			systemParametersList.put("ENABLE_CATEGORY_LEVEL_ATTRIBUTES", "Y");
			systemParametersList.put("EnableShipToAsEntityAddress", "N");
			systemParametersList.put("USER_PROFILE_IMAGE_UPLOAD_DIRECTORY","D:\\VirtualFolder\\deploy\\ASSETS.war\\IMAGES\\USER\\DISPLAY_IMAGE");
		}
		return systemParametersList;
	}

public static List<CIMMTaxModel> getCimmTaxTable() {
		return cimmTaxTable;
	}

	public static void setCimmTaxTable(List<CIMMTaxModel> cimmTaxTable) {
		CommonDBQuery.cimmTaxTable = cimmTaxTable;
	}

	public static LinkedHashMap<String, LocaleModel> getLocaleList() {
		return localeList;
	}

	public static void setLocaleList(LinkedHashMap<String, LocaleModel> localeList) {
		CommonDBQuery.localeList = localeList;
	}

	public static LinkedHashMap<String, String> getCreditCardErpCode() {
		return creditCardErpCode;
	}

	public static void setCreditCardErpCode(LinkedHashMap<String, String> creditCardErpCode) {
		CommonDBQuery.creditCardErpCode = creditCardErpCode;
	}

	public static String getPopularityHitsFileName() {
		return popularityHitsFileName;
	}

	public static void setPopularityHitsFileName(String popularityHitsFileName) {
		CommonDBQuery.popularityHitsFileName = popularityHitsFileName;
	}

	public static String getUserLogFileName() {
		return userLogFileName;
	}

	public static void setUserLogFileName(String userLogFileName) {
		CommonDBQuery.userLogFileName = userLogFileName;
	}

	public static int getRecordCount() {
		return recordCount;
	}

	public static void setRecordCount(int recordCount) {
		CommonDBQuery.recordCount = recordCount;
	}

	public static int getUserLogRecordCount() {
		return userLogRecordCount;
	}

	public static void setUserLogRecordCount(int userLogRecordCount) {
		CommonDBQuery.userLogRecordCount = userLogRecordCount;
	}

	public static void setRequest(HttpServletRequest request) {
		CommonDBQuery.request = request;
	}

	public static HttpServletRequest getRequest() {
		return request;
	}

	public static void setGlobalSiteId(int globalSiteId) {
		CommonDBQuery.globalSiteId = globalSiteId;
	}

	public static int getGlobalSiteId() {
		return globalSiteId;
	}

	public static String getCimm2bCentralAuthorization() {
		return cimm2bCentralAuthorization;
	}

	public static void setCimm2bCentralAuthorization(String cimm2bCentralAuthorization) {
		CommonDBQuery.cimm2bCentralAuthorization = cimm2bCentralAuthorization;
	}

	public static void setSwatchValueList(LinkedHashMap<String, SwatchModel> swatchValueList) {
		CommonDBQuery.swatchValueList = swatchValueList;
	}

	public static LinkedHashMap<String, SwatchModel> getSwatchValueList() {
		return swatchValueList;
	}

	public static LinkedHashMap<String, ArrayList<CustomFieldModel>> getWareHouseCustomFieldsList() {
		return wareHouseCustomFieldsList;
	}

	public static void setWareHouseCustomFieldsList(
			LinkedHashMap<String, ArrayList<CustomFieldModel>> wareHouseCustomFieldsList) {
		CommonDBQuery.wareHouseCustomFieldsList = wareHouseCustomFieldsList;
	}

	public static void setEventCategories(ArrayList<ValueListModel> eventCategories) {
		CommonDBQuery.eventCategories = eventCategories;
	}

	public static ArrayList<ValueListModel> getEventCategories() {
		return eventCategories;
	}

	public static void setEventLocations(ArrayList<ValueListModel> eventLocations) {
		CommonDBQuery.eventLocations = eventLocations;
	}

	public static ArrayList<ValueListModel> getEventLocations() {
		return eventLocations;
	}

	public static void setEventTypes(ArrayList<ValueListModel> eventTypes) {
		CommonDBQuery.eventTypes = eventTypes;
	}

	public static ArrayList<ValueListModel> getEventTypes() {
		return eventTypes;
	}

	public static LinkedHashMap<String, ProductsModel> getBranchDetailData() {
		return branchDetailData;
	}

	public static void setBranchDetailData(LinkedHashMap<String, ProductsModel> branchDetailData) {
		CommonDBQuery.branchDetailData = branchDetailData;
	}

	public static ArrayList<ShipVia> getSiteShipViaList() {
		return SiteShipViaList;
	}

	public static void setSiteShipViaList(ArrayList<ShipVia> siteShipViaList) {
		SiteShipViaList = siteShipViaList;
	}

	private static void setElementURL(String validateString) {

	}

	public static String getLocalSiteURL() {
		return localSiteURL;
	}

	public static void setLocalSiteURL(String localSiteURL) {
		CommonDBQuery.localSiteURL = localSiteURL;
	}

	public static String getLocalEnvironmentIPAddress() {
		return localEnvironmentIPAddress;
	}

	public static void setLocalEnvironmentIPAddress(String localEnvironmentIPAddress) {
		CommonDBQuery.localEnvironmentIPAddress = localEnvironmentIPAddress;
	}

	public static String getLocalEnvironmentPublicIPAddress() {
		return localEnvironmentPublicIPAddress;
	}

	public static void setLocalEnvironmentPublicIPAddress(String localEnvironmentPublicIPAddress) {
		CommonDBQuery.localEnvironmentPublicIPAddress = localEnvironmentPublicIPAddress;
	}

	public static LinkedHashMap<String, String> getNetWorkwareHouseList() {
		return netWorkwareHouseList;
	}

	public static void setNetWorkwareHouseList(LinkedHashMap<String, String> netWorkwareHouseList) {
		CommonDBQuery.netWorkwareHouseList = netWorkwareHouseList;
	}

	public static LinkedHashMap<String, String> getActivewareHouseList() {
		return activewareHouseList;
	}

	public static void setActivewareHouseList(LinkedHashMap<String, String> activewareHouseList) {
		CommonDBQuery.activewareHouseList = activewareHouseList;
	}

	public static String getLocalDirectory() {
		return localDirectory;
	}

	public static void setLocalDirectory(String localDirectory) {
		CommonDBQuery.localDirectory = localDirectory;
	}

	public static boolean isLocalDevelopmentEnvironment() {
		return localDevelopmentEnvironment;
	}

	public static void setLocalDevelopmentEnvironment(boolean localDevelopmentEnvironment) {
		CommonDBQuery.localDevelopmentEnvironment = localDevelopmentEnvironment;
	}

	public static String getThemeDate() {
		return themeDate;
	}

	public static void setThemeDate(String themeDate) {
		CommonDBQuery.themeDate = themeDate;
	}
	public static List<CustomTable> getZipCodeShipBranches() {
		return zipCodeShipBranches;
	}
	public static void setZipCodeShipBranches(List<CustomTable> zipCodeShipBranches) {
		CommonDBQuery.zipCodeShipBranches = zipCodeShipBranches;
	}
	public static List<CustomTable> getOrderModeList() {
		return orderModeList;
	}
	public static void setOrderModeList(List<CustomTable> orderModeList) {
		CommonDBQuery.orderModeList = orderModeList;
	}
	public static List<CustomTable> getPromoBannerList() {
		return promoBannerList;
	}
	public static void setPromoBannerList(List<CustomTable> promoBannerList) {
		CommonDBQuery.promoBannerList = promoBannerList;
	}
	public static List<CustomTable> getSitePromoBannerList() {
		return sitePromoBannerList;
	}
	public static void setSitePromoBannerList(List<CustomTable> sitePromoBannerList) {
		CommonDBQuery.sitePromoBannerList = sitePromoBannerList;
	}
	public static List<CustomTable> getDropPromoBannerList() {
		return dropPromoBannerList;
	}
	public static void setDropPromoBannerList(List<CustomTable> dropPromoBannerList) {
		CommonDBQuery.dropPromoBannerList = dropPromoBannerList;
	}

	public static List<CustomTable> getAccRepList() {
		return accRepList;
	}

	public static void setAccRepList(List<CustomTable> accRepList) {
		CommonDBQuery.accRepList = accRepList;
	}

	public static String getSiteLogo() {
		return siteLogo;
	}

	public static void setSiteLogo(String siteLogo) {
		CommonDBQuery.siteLogo = siteLogo;
	}

	public static LinkedHashMap<String, String> getSystemParameterConfigKeyAndParamIdList() {
		return systemParameterConfigKeyAndParamIdList;
	}

	public static void setSystemParameterConfigKeyAndParamIdList(
			LinkedHashMap<String, String> systemParameterConfigKeyAndParamIdList) {
		CommonDBQuery.systemParameterConfigKeyAndParamIdList = systemParameterConfigKeyAndParamIdList;
	}

	public static ArrayList<WebsiteModel> getWebsiteListArray() {
		return websiteListArray;
	}

	public static void setWebsiteListArray(ArrayList<WebsiteModel> websiteListArray) {
		CommonDBQuery.websiteListArray = websiteListArray;
	}

	public static LinkedHashMap<String, String> getCustomerDynamicPagesList() {
		return customerDynamicPagesList;
	}

	public static void setCustomerDynamicPagesList(LinkedHashMap<String, String> customerDynamicPagesList) {
		CommonDBQuery.customerDynamicPagesList = customerDynamicPagesList;
	}

	public static LinkedHashMap<String, String> getShipViaMapDetailsList() {
		return shipViaMapDetailsList;
	}

	public static void setShipViaMapDetailsList(LinkedHashMap<String, String> shipViaMapDetailsList) {
		CommonDBQuery.shipViaMapDetailsList = shipViaMapDetailsList;
	}
	
	public static String getJiraKey() {
		return jiraKey;
	}
	
	public static void setJiraKey(String jiraKey) {
		CommonDBQuery.jiraKey = jiraKey;
	}
	

	
	

}