package com.unilog.products;

import static com.unilog.defaults.Global.context;
import static com.unilog.defaults.Global.find;
import static com.unilog.defaults.Global.staticPage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.service.ProductManagement;
import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.service.UpsFreightService;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.impl.ProductManagementImpl;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.ProductManagementModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.interactiveadvisor.epiphany.rpserviceimpl.Rpserviceimpl;
import com.opensymphony.xwork2.ActionSupport;
import com.shippingcarrier.dao.UpsCarrier;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.VelocityTemplateEngine.TemplateModel;
import com.unilog.api.bronto.BrontoDAO;
import com.unilog.autocomplete.AutoCompleteDataTableModel;
import com.unilog.autocomplete.CustomAutoComplete;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.custommodule.model.FreightCalculatorModel;
import com.unilog.custommodule.utility.FreightCalculator;
import com.unilog.custommodule.utility.WillCall;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.datasmart.DataSmartController;
import com.unilog.datasmart.DatasmartConstantVariables;
import com.unilog.datasmart.model.DataSmartsSearchModel;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.ecommerce.validataion.ValidationStatus;
import com.unilog.logocustomization.CspPriceTable;
import com.unilog.logocustomization.CspPricingModel;
import com.unilog.logocustomization.CspProductModel;
import com.unilog.logocustomization.LogoCustomizationModule;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.products.model.AssociateItemsModel;
import com.unilog.products.model.LineItemsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.SalesDAO;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.servlets.SchedulerNotification;
import com.unilog.supplier.DAO.SupplierDAO;
import com.unilog.supplier.model.SupplierModel;
import com.unilog.users.UsersAction;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.users.WebLogin;
import com.unilog.utility.CIMMTouchUtility;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;


public class ProductsAction  extends ActionSupport {
	private static final long serialVersionUID = -2167590852259453564L;
	private static final Logger logger = LoggerFactory.getLogger(ProductsAction.class);
	protected String target = ERROR;
	private ArrayList<Integer> idList;
	private String alwaysApprover;
	private ArrayList<Integer> lineItemIdList;
	private ArrayList<Integer> userTopTab;
	private ArrayList<ProductsModel> approveCartData=null;
	private ArrayList<ProductsModel> approveCartGenUserData=null;
	private ArrayList<ProductsModel> brandMenu=null;
	private ArrayList<ProductsModel> manufacturerMenu=null;
	private String navigationType;
	private ArrayList<ProductsModel> categMenu;
	private LinkedHashMap<String, ArrayList<MenuAndBannersModal>> secondLevelMenu;
	private LinkedHashMap<String, ArrayList<MenuAndBannersModal>> thirdLevelMenu;
	private ArrayList<MenuAndBannersModal> brandListInfo;
	private ArrayList<MenuAndBannersModal> manufacturerListInfo;
	private ArrayList<ProductsModel> groupListData=null;
	private ArrayList<ProductsModel> manufacturerList=null;
	private ArrayList<ProductsModel> brandList=null;
	private ArrayList<ProductsModel> productGroupData=null;
	private ArrayList<ProductsModel> productListData=null;
	private ArrayList<ProductsModel> quickOrderPadSel = null;
	private ArrayList<ProductsModel> rfqRefKeyList = null;
	private ArrayList<ProductsModel> savedCartData=null;
	private ArrayList<String> manufacturerIndex;
	private ArrayList<UsersModel> agentUserList = null;
	private ArrayList<UsersModel> branchList;
	private ArrayList<UsersModel> userList = new ArrayList<UsersModel>();
	private boolean flag = false;
	private double cartTotal;
	private String rating;
	private ProductsModel categoryDescription;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int assignedShipTo;
	private int checkOciUser;
	private int itemIdRev;
	private int itemQty = 0;
	private String vpsid = "0";
	private String brandId = "0";
	private String manfId = "0";
	private String metaTagString;
	private int varPsid;
	private String homeTeritory;
	private int listId; //Saved Cart id or Saved list Id
	private int paginate;
	private int resultCount;
	private String srchTyp;
	private int savedGroupId;
	private int savedGroupItemId;
	private int validateResponse = 0;
	private int cartItemQuantitySum = 0;
	private String codeId;
	private String keyWord;
	private String keyWordTxt;
	private int requestTokenId;
	private String narrowKeyword;
	private String levelNo;
	private String codeIdSkip;
	private String levelNoSkip;
	private String afterLoginAvaiabilitycart;
	private String afterLoginAvaiabilityProductGroup;
	private String afterLoginAvaiabilityProductList;
	private String afterLoginAvaiabilitySavedCart;
	private String allowLiveChat;
	private String approveSenderid;
	private String bannerMessage;
	private String beforeLoginAvaiabilitycart;
	private String beforeLoginAvaiabilityProductList;
	private String canSubmitNPR;
	private String canSubmitPO;
	private String canSubmitRFQ;
	private String comments;
	private String deleteId;
	private String displayCustPartNum;
	private String eclipseErrorMassage;
	private String errorMsg;
	private String groupType;
	private String hookUrl;
	private String isEclipse;
	private int itemPriceId;
	private String itemPriceIdList[];
	private String keyword;
	private String lineItemComment[];
	private String listName; //Saved Cart Name or Saved list Name
	private String loginUserId;
	private String morePageId;
	private String newsContent;
	private String ociSession;
	private String pageNo = "0";
	private String partNumber;
	private String priceAvailbleList;
	private String productIdList;
	private String rejectReason;
	private String result;
	private String resultPage = "0";
	private String savedGroupName;
	private String sharedWithUserId;
	private String price[];
	private String shoppingCartId[];
	private String shoppingCartQty[];
	private String partnumberStrArray[];
	private String minmunOrderQuantity[];
	private String orderQuantityInterval[];
	private String slUserName;
	private String swithOnEclipse;
	private String title;
	private String updatedDate;
	private String updateId;
	private String userName;
	private String attrFilterList;
	private String filterList;
	private String[]  itemPriceIDList;
	private String[]  prodIdList;
	private String[] itemQtyList;
	private UsersModel addressList;
	private ArrayList<ProductsModel> taxonomyLevelFilterData=null;
	private ArrayList<ProductsModel> staticContentData=null;
	private ArrayList<ProductsModel> eventSearchData=null;
	private ArrayList<ProductsModel> reviewList=null;
	private ArrayList<ProductsModel> reviewListCount=null;
	LinkedHashMap<String,ArrayList<MenuAndBannersModal>> categoryBannersList = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
	private ArrayList<MenuAndBannersModal> topBanners = new ArrayList<MenuAndBannersModal>();
	private ArrayList<MenuAndBannersModal> leftBanners = new ArrayList<MenuAndBannersModal>();
	private ArrayList<MenuAndBannersModal> rightBanners = new ArrayList<MenuAndBannersModal>();
	private ArrayList<MenuAndBannersModal> bottomBanners = new ArrayList<MenuAndBannersModal>();
	private ArrayList<ProductsModel> breadCrumbList=null;
	private ArrayList<ProductsModel> itemLevelFilterData=null;
	private ArrayList<ProductsModel> alternativeItems=null;
	private ArrayList<ProductsModel> categoryFilterData = null;
	private ArrayList<ProductsModel> attrFilterData = null;
	private ArrayList<ProductsModel> attrFilteredList = null;
	private ArrayList<ProductsModel> itemImagesList = null;
	private ArrayList<ProductsModel> itemAttributesList = null;
	private ArrayList<ProductsModel> itemDocumentsList = null;
	private ArrayList<ProductsModel> productDataList = null;
	private ArrayList<ProductsModel> linkedItemsList = null;
	private ArrayList<ProductsModel> recommendedItemsList = null;
	private ArrayList<ProductsModel> customerPartNumberList = null;
	private ArrayList<ProductsModel> relatedItemsList = null;
	private String itemUrlSingle;
	private String sortBy;
	private String siteSearch;
	private String renderContent;
	private LinkedHashMap<String, ArrayList<ProductsModel>> compareList;
	private String compareId;
	private int gallery;
	private String shipVia;
	private String deleteFlag;
	private String itemSuggest;
	private String mftName;
	private LinkedHashMap<String, ArrayList<ProductsModel>> reviewDataList=null;
	private LinkedHashMap<String, ArrayList<ProductsModel>> linkedItemGroup = null;
	private float overAllRate;
	private ArrayList<ProductsModel> recommendedItem = null;
	private String useBillToEntity;
	private String isReOrder;
	private String reqType;
	public static ArrayList<MenuAndBannersModal> topStaticMenu = null;
	public static ArrayList<MenuAndBannersModal> footerStaticMenu = null;
	public String custPnum;
	public int minimumOrderQty;
	public int orderQtyInterval;
	public int parentGroupId;
	private int readQuoteListCount;
	private String resultMsg;
	private String shoppingListLoaderMessage;
	private LinkedHashMap<String, Integer> shoppingListLoaderCount;
	private ArrayList<MarketBasketData> shoppingListLoaderDetails;
	private String notValidFile;
	private String type;
	private ArrayList<String> cpnAddMinQtyNotMatchList = new ArrayList<String>();
	private int parentId;
	private String lineItemList,cartItemList;
	private FreightCalculatorModel freightValue = null;
	//zeroin search
	private String category;
	private String brand;
	private String manf;
	private String searchkeyWord;
	private String pageViewMode;
	private String mpnStrArray[];
	private String ariPageHeading;
	private String altPartNumber;
	private String qty;
	private String quotePartNumberSelected[];
	private boolean overRideSearchByCategoryFlag = true;
	private String overRideCatId;
	private int numberOfStaticResult;
	private String sessionId;	
	private String seoUrl;
	private String fromProductMode;
	private String viewFrequentlyPurcahsedOnly = "N";
	private String clearanceFlag = "N";
	private String wareHouseItems ="";
	private String pageName ="";
	private String shared;
	private String listWithZeroItems = "";
	
	public String getListWithZeroItems() {
		return listWithZeroItems;
	}
	public void setListWithZeroItems(String listWithZeroItems) {
		this.listWithZeroItems = listWithZeroItems;
	}
	public String getShared() {
		return shared;
	}
	public void setShared(String shared) {
		this.shared = shared;
	}
	public String getAlwaysApprover() {
		return alwaysApprover;
	}
	public void setAlwaysApprover(String alwaysApprover) {
		this.alwaysApprover = alwaysApprover;
	}

	public int getCartItemQuantitySum() {
		return cartItemQuantitySum;
	}
	public void setCartItemQuantitySum(int cartItemQuantitySum) {
		this.cartItemQuantitySum = cartItemQuantitySum;
	}
	public int getSavedGroupItemId() {
		return savedGroupItemId;
	}
	public void setSavedGroupItemId(int savedGroupItemId) {
		this.savedGroupItemId = savedGroupItemId;
	}
	public int getNumberOfStaticResult() {
		return numberOfStaticResult;
	}
	public void setNumberOfStaticResult(int numberOfStaticResult) {
		this.numberOfStaticResult = numberOfStaticResult;
	}
	public String getOverRideCatId() {
		return overRideCatId;
	}
	public void setOverRideCatId(String overRideCatId) {
		this.overRideCatId = overRideCatId;
	}
	public boolean isOverRideSearchByCategoryFlag() {
		return overRideSearchByCategoryFlag;
	}
	public void setOverRideSearchByCategoryFlag(boolean overRideSearchByCategoryFlag) {
		this.overRideSearchByCategoryFlag = overRideSearchByCategoryFlag;
	}
	public String[] getQuotePartNumberSelected() {
		return quotePartNumberSelected;
	}
	public void setQuotePartNumberSelected(String[] quotePartNumberSelected) {
		this.quotePartNumberSelected = quotePartNumberSelected;
	}
	public ArrayList<ProductsModel> getAlternativeItems() {
		return alternativeItems;
	}
	public void setAlternativeItems(ArrayList<ProductsModel> alternativeItems) {
		this.alternativeItems = alternativeItems;
	}
	public String getPageViewMode() {
		return pageViewMode;
	}
	public void setPageViewMode(String pageViewMode) {
		this.pageViewMode = pageViewMode;
	}
	public String getSearchkeyWord() {
		return searchkeyWord;
	}
	public void setSearchkeyWord(String searchkeyWord) {
		this.searchkeyWord = searchkeyWord;
	}
	public LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getThirdLevelMenu() {
		return thirdLevelMenu;
	}
	public void setThirdLevelMenu(
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> thirdLevelMenu) {
		this.thirdLevelMenu = thirdLevelMenu;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getCategory() {
		return category;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getBrand() {
		return brand;
	}
	public void setManf(String manf) {
		this.manf = manf;
	}
	public String getManf() {
		return manf;
	}
	
	public String getNotValidFile() {
		return notValidFile;
	}
	public void setNotValidFile(String notValidFile) {
		this.notValidFile = notValidFile;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<String> getCpnAddMinQtyNotMatchList() {
		return cpnAddMinQtyNotMatchList;
	}
	public void setCpnAddMinQtyNotMatchList(
			ArrayList<String> cpnAddMinQtyNotMatchList) {
		this.cpnAddMinQtyNotMatchList = cpnAddMinQtyNotMatchList;
	}

	private ArrayList<String> cpnAddList = new ArrayList<String>();
	
	public int getParentGroupId() {
		return parentGroupId;
	}
	public void setParentGroupId(int parentGroupId) {
		this.parentGroupId = parentGroupId;
	}
	public static ArrayList<MenuAndBannersModal> getFooterStaticMenu() {
		return footerStaticMenu;
	}
	public static void setFooterStaticMenu(
			ArrayList<MenuAndBannersModal> footerStaticMenu) {
		ProductsAction.footerStaticMenu = footerStaticMenu;
	}
	public String[] getPartnumberStrArray() {
		return partnumberStrArray;
	}
	public void setPartnumberStrArray(String[] partnumberStrArray) {
		this.partnumberStrArray = partnumberStrArray;
	}
	public String[] getMinmunOrderQuantity() {
		return minmunOrderQuantity;
	}
	public void setMinmunOrderQuantity(String[] minmunOrderQuantity) {
		this.minmunOrderQuantity = minmunOrderQuantity;
	}
	public String[] getOrderQuantityInterval() {
		return orderQuantityInterval;
	}
	public void setOrderQuantityInterval(String[] orderQuantityInterval) {
		this.orderQuantityInterval = orderQuantityInterval;
	}
	public int getMinimumOrderQty() {
		return minimumOrderQty;
	}
	public void setMinimumOrderQty(int minimumOrderQty) {
		this.minimumOrderQty = minimumOrderQty;
	}
	public int getOrderQtyInterval() {
		return orderQtyInterval;
	}
	public void setOrderQtyInterval(int orderQtyInterval) {
		this.orderQtyInterval = orderQtyInterval;
	}
	public String getCustPnum() {
		return custPnum;
	}
	public void setCustPnum(String custPnum) {
		this.custPnum = custPnum;
	}
	public static ArrayList<MenuAndBannersModal> getTopStaticMenu() {
		return topStaticMenu;
	}
	public static void setTopStaticMenu(ArrayList<MenuAndBannersModal> topStaticMenu) {
		ProductsAction.topStaticMenu = topStaticMenu;
	}
	public ArrayList<MenuAndBannersModal> getBrandListInfo() {
		return brandListInfo;
	}
	public void setBrandListInfo(ArrayList<MenuAndBannersModal> brandListInfo) {
		this.brandListInfo = brandListInfo;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getIsReOrder() {
		return isReOrder;
	}
	public void setIsReOrder(String isReOrder) {
		this.isReOrder = isReOrder;
	}
	public String getUseBillToEntity() {
		return useBillToEntity;
	}
	public void setUseBillToEntity(String useBillToEntity) {
		this.useBillToEntity = useBillToEntity;
	}
	public LinkedHashMap<String, ArrayList<ProductsModel>> getReviewDataList() {
		return reviewDataList;
	}
	public void setReviewDataList(
			LinkedHashMap<String, ArrayList<ProductsModel>> reviewDataList) {
		this.reviewDataList = reviewDataList;
	}
	public LinkedHashMap<String, ArrayList<ProductsModel>> getLinkedItemGroup() {
		return linkedItemGroup;
	}
	public void setLinkedItemGroup(
			LinkedHashMap<String, ArrayList<ProductsModel>> linkedItemGroup) {
		this.linkedItemGroup = linkedItemGroup;
	}
	public float getOverAllRate() {
		return overAllRate;
	}
	public void setOverAllRate(float overAllRate) {
		this.overAllRate = overAllRate;
	}
	public ArrayList<ProductsModel> getRecommendedItem() {
		return recommendedItem;
	}
	public void setRecommendedItem(ArrayList<ProductsModel> recommendedItem) {
		this.recommendedItem = recommendedItem;
	}
	public ArrayList<ProductsModel> getBrandList() {
		return brandList;
	}
	public void setBrandList(ArrayList<ProductsModel> brandList) {
		this.brandList = brandList;
	}
	public String getMftName() {
		return mftName;
	}
	public void setMftName(String mftName) {
		this.mftName = mftName;
	}
	public String getItemSuggest() {
		return itemSuggest;
	}
	public void setItemSuggest(String itemSuggest) {
		this.itemSuggest = itemSuggest;
	}
	public String getKeyWordTxt() {
		return keyWordTxt;
	}
	public void setKeyWordTxt(String keyWordTxt) {
		this.keyWordTxt = keyWordTxt;
	}
	public String getNarrowKeyword() {
		return narrowKeyword;
	}
	public void setNarrowKeyword(String narrowKeyword) {
		this.narrowKeyword = narrowKeyword;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getShipVia() {
		return shipVia;
	}
	public void setShipVia(String shipVia) {
		this.shipVia = shipVia;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public ArrayList<ProductsModel> getProductListData() {
		return productListData;
	}
	public void setProductListData(ArrayList<ProductsModel> productListData) {
		this.productListData = productListData;
	}
	public ArrayList<ProductsModel> getProductGroupData() {
		return productGroupData;
	}
	public void setProductGroupData(ArrayList<ProductsModel> productGroupData) {
		this.productGroupData = productGroupData;
	}
	public ArrayList<ProductsModel> getSavedCartData() {
		return savedCartData;
	}
	public void setSavedCartData(ArrayList<ProductsModel> savedCartData) {
		this.savedCartData = savedCartData;
	}
	public ArrayList<ProductsModel> getApproveCartData() {
		return approveCartData;
	}
	public void setApproveCartData(ArrayList<ProductsModel> approveCartData) {
		this.approveCartData = approveCartData;
	}
	public ArrayList<ProductsModel> getApproveCartGenUserData() {
		return approveCartGenUserData;
	}
	public void setApproveCartGenUserData(
			ArrayList<ProductsModel> approveCartGenUserData) {
		this.approveCartGenUserData = approveCartGenUserData;
	}
	public ArrayList<ProductsModel> getGroupListData() {
		return groupListData;
	}
	public void setGroupListData(ArrayList<ProductsModel> groupListData) {
		this.groupListData = groupListData;
	}
	public ArrayList<ProductsModel> getQuickOrderPadSel() {
		return quickOrderPadSel;
	}
	public void setQuickOrderPadSel(ArrayList<ProductsModel> quickOrderPadSel) {
		this.quickOrderPadSel = quickOrderPadSel;
	}
	public ArrayList<ProductsModel> getRfqRefKeyList() {
		return rfqRefKeyList;
	}
	public void setRfqRefKeyList(ArrayList<ProductsModel> rfqRefKeyList) {
		this.rfqRefKeyList = rfqRefKeyList;
	}
	public ArrayList<UsersModel> getAgentUserList() {
		return agentUserList;
	}
	public void setAgentUserList(ArrayList<UsersModel> agentUserList) {
		this.agentUserList = agentUserList;
	}
	public UsersModel getAddressList() {
		return addressList;
	}
	public void setAddressList(UsersModel addressList) {
		this.addressList = addressList;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getProductIdList() {
		return productIdList;
	}
	public void setProductIdList(String productIdList) {
		this.productIdList = productIdList;
	}
	public int getListId() {
		return listId;
	}
	public void setListId(int listId) {
		this.listId = listId;
	}
	public int getItemQty() {
		return itemQty;
	}
	public void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public int getValidateResponse() {
		return validateResponse;
	}
	public void setValidateResponse(int validateResponse) {
		this.validateResponse = validateResponse;
	}
	public int getSavedGroupId() {
		return savedGroupId;
	}
	public void setSavedGroupId(int savedGroupId) {
		this.savedGroupId = savedGroupId;
	}
	public String getSavedGroupName() {
		return savedGroupName;
	}
	public void setSavedGroupName(String savedGroupName) {
		this.savedGroupName = savedGroupName;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public double getCartTotal() {
		return cartTotal;
	}
	public void setCartTotal(double cartTotal) {
		this.cartTotal = cartTotal;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public String[] getShoppingCartQty() {
		return shoppingCartQty;
	}
	public void setShoppingCartQty(String[] shoppingCartQty) {
		this.shoppingCartQty = shoppingCartQty;
	}
	public String[] getShoppingCartId() {
		return shoppingCartId;
	}
	public void setShoppingCartId(String[] shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}
	public ArrayList<Integer> getLineItemIdList() {
		return lineItemIdList;
	}
	public void setLineItemIdList(ArrayList<Integer> lineItemIdList) {
		this.lineItemIdList = lineItemIdList;
	}
	public ArrayList<Integer> getIdList() {
		return idList;
	}
	public void setIdList(ArrayList<Integer> idList) {
		this.idList = idList;
	}
	public int getCheckOciUser() {
		return checkOciUser;
	}
	public void setCheckOciUser(int checkOciUser) {
		this.checkOciUser = checkOciUser;
	}
	public String getOciSession() {
		return ociSession;
	}
	public void setOciSession(String ociSession) {
		this.ociSession = ociSession;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHookUrl() {
		return hookUrl;
	}
	public void setHookUrl(String hookUrl) {
		this.hookUrl = hookUrl;
	}
	public String getCanSubmitPO() {
		return canSubmitPO;
	}
	public void setCanSubmitPO(String canSubmitPO) {
		this.canSubmitPO = canSubmitPO;
	}
	public String getCanSubmitRFQ() {
		return canSubmitRFQ;
	}
	public void setCanSubmitRFQ(String canSubmitRFQ) {
		this.canSubmitRFQ = canSubmitRFQ;
	}
	public String getCanSubmitNPR() {
		return canSubmitNPR;
	}
	public void setCanSubmitNPR(String canSubmitNPR) {
		this.canSubmitNPR = canSubmitNPR;
	}
	public String getSwithOnEclipse() {
		return swithOnEclipse;
	}
	public void setSwithOnEclipse(String swithOnEclipse) {
		this.swithOnEclipse = swithOnEclipse;
	}
	public int getResultCount() {
		return resultCount;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	public String getResultPage() {
		return resultPage;
	}
	public void setResultPage(String resultPage) {
		this.resultPage = resultPage;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public int getPaginate() {
		return paginate;
	}
	public void setPaginate(int paginate) {
		this.paginate = paginate;
	}
	public String getSlUserName() {
		return slUserName;
	}
	public void setSlUserName(String slUserName) {
		this.slUserName = slUserName;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String[] getItemPriceIdList() {
		return itemPriceIdList;
	}
	public void setItemPriceIdList(String[] itemPriceIdList) {
		this.itemPriceIdList = itemPriceIdList;
	}
	public String getDisplayCustPartNum() {
		return displayCustPartNum;
	}
	public void setDisplayCustPartNum(String displayCustPartNum) {
		this.displayCustPartNum = displayCustPartNum;
	}
	public String getAllowLiveChat() {
		return allowLiveChat;
	}
	public void setAllowLiveChat(String allowLiveChat) {
		this.allowLiveChat = allowLiveChat;
	}
	public String getBannerMessage() {
		return bannerMessage;
	}
	public void setBannerMessage(String bannerMessage) {
		this.bannerMessage = bannerMessage;
	}
	public String getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}
	public ArrayList<ProductsModel> getBrandMenu() {
		return brandMenu;
	}
	public void setBrandMenu(ArrayList<ProductsModel> brandMenu) {
		this.brandMenu = brandMenu;
	}
	public ArrayList<Integer> getUserTopTab() {
		return userTopTab;
	}
	public void setUserTopTab(ArrayList<Integer> userTopTab) {
		this.userTopTab = userTopTab;
	}
	public ArrayList<ProductsModel> getCategMenu() {
		return categMenu;
	}
	public void setCategMenu(ArrayList<ProductsModel> categMenu) {
		this.categMenu = categMenu;
	}
	public String getNewsContent() {
		return newsContent;
	}
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}
	public String getMorePageId() {
		return morePageId;
	}
	public void setMorePageId(String morePageId) {
		this.morePageId = morePageId;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public ArrayList<UsersModel> getUserList() {
		return userList;
	}
	public void setUserList(ArrayList<UsersModel> userList) {
		this.userList = userList;
	}
	public int getAssignedShipTo() {
		return assignedShipTo;
	}
	public void setAssignedShipTo(int assignedShipTo) {
		this.assignedShipTo = assignedShipTo;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSharedWithUserId() {
		return sharedWithUserId;
	}
	public void setSharedWithUserId(String sharedWithUserId) {
		this.sharedWithUserId = sharedWithUserId;
	}
	public String getBeforeLoginAvaiabilityProductList() {
		return beforeLoginAvaiabilityProductList;
	}
	public void setBeforeLoginAvaiabilityProductList(
			String beforeLoginAvaiabilityProductList) {
		this.beforeLoginAvaiabilityProductList = beforeLoginAvaiabilityProductList;
	}
	public String getBeforeLoginAvaiabilitycart() {
		return beforeLoginAvaiabilitycart;
	}
	public void setBeforeLoginAvaiabilitycart(String beforeLoginAvaiabilitycart) {
		this.beforeLoginAvaiabilitycart = beforeLoginAvaiabilitycart;
	}
	public String getAfterLoginAvaiabilitycart() {
		return afterLoginAvaiabilitycart;
	}
	public void setAfterLoginAvaiabilitycart(String afterLoginAvaiabilitycart) {
		this.afterLoginAvaiabilitycart = afterLoginAvaiabilitycart;
	}
	public String getAfterLoginAvaiabilityProductList() {
		return afterLoginAvaiabilityProductList;
	}
	public void setAfterLoginAvaiabilityProductList(
			String afterLoginAvaiabilityProductList) {
		this.afterLoginAvaiabilityProductList = afterLoginAvaiabilityProductList;
	}
	public String getAfterLoginAvaiabilityProductGroup() {
		return afterLoginAvaiabilityProductGroup;
	}
	public void setAfterLoginAvaiabilityProductGroup(
			String afterLoginAvaiabilityProductGroup) {
		this.afterLoginAvaiabilityProductGroup = afterLoginAvaiabilityProductGroup;
	}
	public String getAfterLoginAvaiabilitySavedCart() {
		return afterLoginAvaiabilitySavedCart;
	}
	public void setAfterLoginAvaiabilitySavedCart(
			String afterLoginAvaiabilitySavedCart) {
		this.afterLoginAvaiabilitySavedCart = afterLoginAvaiabilitySavedCart;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public int getItemIdRev() {
		return itemIdRev;
	}
	public void setItemIdRev(int itemIdRev) {
		this.itemIdRev = itemIdRev;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getApproveSenderid() {
		return approveSenderid;
	}
	public void setApproveSenderid(String approveSenderid) {
		this.approveSenderid = approveSenderid;
	}
	public String[] getLineItemComment() {
		return lineItemComment;
	}
	public void setLineItemComment(String[] lineItemComment) {
		this.lineItemComment = lineItemComment;
	}
	public String getDeleteId() {
		return deleteId;
	}
	public void setDeleteId(String deleteId) {
		this.deleteId = deleteId;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public String getIsEclipse() {
		return isEclipse;
	}
	public void setIsEclipse(String isEclipse) {
		this.isEclipse = isEclipse;
	}
	public ArrayList<ProductsModel> getManufacturerList() {
		return manufacturerList;
	}
	public void setManufacturerList(ArrayList<ProductsModel> manufacturerList) {
		this.manufacturerList = manufacturerList;
	}
	public ArrayList<String> getManufacturerIndex() {
		return manufacturerIndex;
	}
	public void setManufacturerIndex(ArrayList<String> manufacturerIndex) {
		this.manufacturerIndex = manufacturerIndex;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public String getPriceAvailbleList() {
		return priceAvailbleList;
	}
	public void setPriceAvailbleList(String priceAvailbleList) {
		this.priceAvailbleList = priceAvailbleList;
	}
	public String[] getItemQtyList() {
		return itemQtyList;
	}
	public void setItemQtyList(String[] itemQtyList) {
		this.itemQtyList = itemQtyList;
	}
	public String[] getItemPriceIDList() {
		return itemPriceIDList;
	}
	public void setItemPriceIDList(String[] itemPriceIDList) {
		this.itemPriceIDList = itemPriceIDList;
	}
	public String[] getProdIdList() {
		return prodIdList;
	}
	public void setProdIdList(String[] prodIdList) {
		this.prodIdList = prodIdList;
	}
	public ArrayList<UsersModel> getBranchList() {
		return branchList;
	}
	public void setBranchList(ArrayList<UsersModel> branchList) {
		this.branchList = branchList;
	}
	public String getEclipseErrorMassage() {
		return eclipseErrorMassage;
	}
	public void setEclipseErrorMassage(String eclipseErrorMassage) {
		this.eclipseErrorMassage = eclipseErrorMassage;
	}
	public ArrayList<ProductsModel> getTaxonomyLevelFilterData() {
		return taxonomyLevelFilterData;
	}
	public void setTaxonomyLevelFilterData(
			ArrayList<ProductsModel> taxonomyLevelFilterData) {
		this.taxonomyLevelFilterData = taxonomyLevelFilterData;
	}
	public ArrayList<ProductsModel> getItemLevelFilterData() {
		return itemLevelFilterData;
	}
	public void setItemLevelFilterData(ArrayList<ProductsModel> itemLevelFilterData) {
		this.itemLevelFilterData = itemLevelFilterData;
	}
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	public String getLevelNo() {
		return levelNo;
	}
	public void setLevelNo(String levelNo) {
		this.levelNo = levelNo;
	}
	public String getCodeIdSkip() {
		return codeIdSkip;
	}
	public void setCodeIdSkip(String codeIdSkip) {
		this.codeIdSkip = codeIdSkip;
	}
	public String getLevelNoSkip() {
		return levelNoSkip;
	}
	public void setLevelNoSkip(String levelNoSkip) {
		this.levelNoSkip = levelNoSkip;
	}
	public ProductsModel getCategoryDescription() {
		return categoryDescription;
	}
	public void setCategoryDescription(ProductsModel categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getSrchTyp() {
		return srchTyp;
	}
	public void setSrchTyp(String srchTyp) {
		this.srchTyp = srchTyp;
	}
	public LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getCategoryBannersList() {
		return categoryBannersList;
	}
	public void setCategoryBannersList(
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> categoryBannersList) {
		this.categoryBannersList = categoryBannersList;
	}
	public ArrayList<MenuAndBannersModal> getTopBanners() {
		return topBanners;
	}
	public void setTopBanners(ArrayList<MenuAndBannersModal> topBanners) {
		this.topBanners = topBanners;
	}
	public ArrayList<MenuAndBannersModal> getLeftBanners() {
		return leftBanners;
	}
	public void setLeftBanners(ArrayList<MenuAndBannersModal> leftBanners) {
		this.leftBanners = leftBanners;
	}
	public ArrayList<MenuAndBannersModal> getRightBanners() {
		return rightBanners;
	}
	public void setRightBanners(ArrayList<MenuAndBannersModal> rightBanners) {
		this.rightBanners = rightBanners;
	}
	public ArrayList<MenuAndBannersModal> getBottomBanners() {
		return bottomBanners;
	}
	public void setBottomBanners(ArrayList<MenuAndBannersModal> bottomBanners) {
		this.bottomBanners = bottomBanners;
	}
	public ArrayList<ProductsModel> getBreadCrumbList() {
		return breadCrumbList;
	}
	public void setBreadCrumbList(ArrayList<ProductsModel> breadCrumbList) {
		this.breadCrumbList = breadCrumbList;
	}
	public String getNavigationType() {
		return navigationType;
	}
	public void setNavigationType(String navigationType) {
		this.navigationType = navigationType;
	}
	public String getVpsid() {
		return vpsid;
	}
	public void setVpsid(String vpsid) {
		this.vpsid = vpsid;
	}
	public String getAttrFilterList() {
		return attrFilterList;
	}
	public void setAttrFilterList(String attrFilterList) {
		this.attrFilterList = attrFilterList;
	}
	public String getFilterList() {
		return filterList;
	}
	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}
	public int getVarPsid() {
		return varPsid;
	}
	public void setVarPsid(int varPsid) {
		this.varPsid = varPsid;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getMetaTagString() {
		return metaTagString;
	}
	public void setMetaTagString(String metaTagString) {
		this.metaTagString = metaTagString;
	}
	public String getHomeTeritory() {
		return homeTeritory;
	}
	public void setHomeTeritory(String homeTeritory) {
		this.homeTeritory = homeTeritory;
	}
	public ArrayList<ProductsModel> getStaticContentData() {
		return staticContentData;
	}
	public void setStaticContentData(ArrayList<ProductsModel> staticContentData) {
		this.staticContentData = staticContentData;
	}
	public ArrayList<ProductsModel> getEventSearchData() {
		return eventSearchData;
	}
	public void setEventSearchData(ArrayList<ProductsModel> eventSearchData) {
		this.eventSearchData = eventSearchData;
	}
	public ArrayList<ProductsModel> getReviewList() {
		return reviewList;
	}
	public void setReviewList(ArrayList<ProductsModel> reviewList) {
		this.reviewList = reviewList;
	}
	public ArrayList<ProductsModel> getReviewListCount() {
		return reviewListCount;
	}
	public void setReviewListCount(ArrayList<ProductsModel> reviewListCount) {
		this.reviewListCount = reviewListCount;
	}
	public ArrayList<ProductsModel> getCategoryFilterData() {
		return categoryFilterData;
	}
	public void setCategoryFilterData(ArrayList<ProductsModel> categoryFilterData) {
		this.categoryFilterData = categoryFilterData;
	}
	public ArrayList<ProductsModel> getAttrFilterData() {
		return attrFilterData;
	}
	public void setAttrFilterData(ArrayList<ProductsModel> attrFilterData) {
		this.attrFilterData = attrFilterData;
	}
	public ArrayList<ProductsModel> getAttrFilteredList() {
		return attrFilteredList;
	}
	public void setAttrFilteredList(ArrayList<ProductsModel> attrFilteredList) {
		this.attrFilteredList = attrFilteredList;
	}
	public ArrayList<ProductsModel> getItemImagesList() {
		return itemImagesList;
	}
	public void setItemImagesList(ArrayList<ProductsModel> itemImagesList) {
		this.itemImagesList = itemImagesList;
	}
	public ArrayList<ProductsModel> getItemAttributesList() {
		return itemAttributesList;
	}
	public void setItemAttributesList(ArrayList<ProductsModel> itemAttributesList) {
		this.itemAttributesList = itemAttributesList;
	}
	public ArrayList<ProductsModel> getItemDocumentsList() {
		return itemDocumentsList;
	}
	public void setItemDocumentsList(ArrayList<ProductsModel> itemDocumentsList) {
		this.itemDocumentsList = itemDocumentsList;
	}
	public ArrayList<ProductsModel> getProductDataList() {
		return productDataList;
	}
	public void setProductDataList(ArrayList<ProductsModel> productDataList) {
		this.productDataList = productDataList;
	}
	public ArrayList<ProductsModel> getLinkedItemsList() {
		return linkedItemsList;
	}
	public void setLinkedItemsList(ArrayList<ProductsModel> linkedItemsList) {
		this.linkedItemsList = linkedItemsList;
	}
	public ArrayList<ProductsModel> getRecommendedItemsList() {
		return recommendedItemsList;
	}
	public void setRecommendedItemsList(
			ArrayList<ProductsModel> recommendedItemsList) {
		this.recommendedItemsList = recommendedItemsList;
	}
	public ArrayList<ProductsModel> getCustomerPartNumberList() {
		return customerPartNumberList;
	}
	public void setCustomerPartNumberList(
			ArrayList<ProductsModel> customerPartNumberList) {
		this.customerPartNumberList = customerPartNumberList;
	}
	public ArrayList<ProductsModel> getRelatedItemsList() {
		return relatedItemsList;
	}
	public void setRelatedItemsList(ArrayList<ProductsModel> relatedItemsList) {
		this.relatedItemsList = relatedItemsList;
	}
	public String getItemUrlSingle() {
		return itemUrlSingle;
	}
	public void setItemUrlSingle(String itemUrlSingle) {
		this.itemUrlSingle = itemUrlSingle;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public int getItemPriceId() {
		return itemPriceId;
	}
	public void setItemPriceId(int itemPriceId) {
		this.itemPriceId = itemPriceId;
	}
	public String getSiteSearch() {
		return siteSearch;
	}
	public void setSiteSearch(String siteSearch) {
		this.siteSearch = siteSearch;
	}
	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	public String getRenderContent() {
		return renderContent;
	}
	public LinkedHashMap<String, ArrayList<ProductsModel>> getCompareList() {
		return compareList;
	}
	public void setCompareList(
			LinkedHashMap<String, ArrayList<ProductsModel>> compareList) {
		this.compareList = compareList;
	}
	public void setGallery(int gallery) {
		this.gallery = gallery;
	}
	public int getGallery() {
		return gallery;
	}
	
	
	public LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getSecondLevelMenu() {
		return secondLevelMenu;
	}
	public void setSecondLevelMenu(
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> secondLevelMenu) {
		this.secondLevelMenu = secondLevelMenu;
	}
	public String getCompareId() {
		return compareId;
	}
	public void setCompareId(String compareId) {
		this.compareId = compareId;
	}
	public int getReadQuoteListCount() {
		return readQuoteListCount;
	}
	public void setReadQuoteListCount(int readQuoteListCount) {
		this.readQuoteListCount = readQuoteListCount;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getShoppingListLoaderMessage() {
		return shoppingListLoaderMessage;
	}
	public void setShoppingListLoaderMessage(String shoppingListLoaderMessage) {
		this.shoppingListLoaderMessage = shoppingListLoaderMessage;
	}
	public LinkedHashMap<String, Integer> getShoppingListLoaderCount() {
		return shoppingListLoaderCount;
	}
	public void setShoppingListLoaderCount(
			LinkedHashMap<String, Integer> shoppingListLoaderCount) {
		this.shoppingListLoaderCount = shoppingListLoaderCount;
	}
	public ArrayList<MarketBasketData> getShoppingListLoaderDetails() {
		return shoppingListLoaderDetails;
	}
	public void setShoppingListLoaderDetails(
			ArrayList<MarketBasketData> shoppingListLoaderDetails) {
		this.shoppingListLoaderDetails = shoppingListLoaderDetails;
	}
	public ArrayList<String> getCpnAddList() {
		return cpnAddList;
	}
	public void setCpnAddList(ArrayList<String> cpnAddList) {
		this.cpnAddList = cpnAddList;
	}
	public String getLineItemList() {
		return lineItemList;
	}
	public void setLineItemList(String lineItemList) {
		this.lineItemList = lineItemList;
	}
	public String getCartItemList() {
		return cartItemList;
	}
	public void setCartItemList(String cartItemList) {
		this.cartItemList = cartItemList;
	}
	public void setFreightValue(FreightCalculatorModel freightValue) {
		this.freightValue = freightValue;
	}
	public FreightCalculatorModel getFreightValue() {
		return freightValue;
	}
	public void setManufacturerListInfo(ArrayList<MenuAndBannersModal> manufacturerListInfo) {
		this.manufacturerListInfo = manufacturerListInfo;
	}
	public ArrayList<MenuAndBannersModal> getManufacturerListInfo() {
		return manufacturerListInfo;
	}
	public void setManfId(String manfId) {
		this.manfId = manfId;
	}
	public String getManfId() {
		return manfId;
	}
	public void setManufacturerMenu(ArrayList<ProductsModel> manufacturerMenu) {
		this.manufacturerMenu = manufacturerMenu;
	}
	public ArrayList<ProductsModel> getManufacturerMenu() {
		return manufacturerMenu;
	}
	public String getAriPageHeading() {
		return ariPageHeading;
	}
	public void setAriPageHeading(String ariPageHeading) {
		this.ariPageHeading = ariPageHeading;
	}
	public String getAltPartNumber() {
		return altPartNumber;
	}
	public void setAltPartNumber(String altPartNumber) {
		this.altPartNumber = altPartNumber;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String[] getPrice() {
		return price;
	}
	public void setPrice(String price[]) {
		this.price = price;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSeoUrl() {
		return seoUrl;
	}
	public void setSeoUrl(String seoUrl) {
		this.seoUrl = seoUrl;
	}
	public String getWareHouseItems() {
		return wareHouseItems;
	}
	public void setWareHouseItems(String wareHouseItems) {
		this.wareHouseItems = wareHouseItems;
	}
	
	public String productPage(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String isFromHomePage = request.getParameter("PName");
			String tempSubset = (String) session.getAttribute("userSubsetId");
		    String defaultShiptoId = (String) session.getAttribute("defaultShipToId");
		    String entityId = (String) session.getAttribute("entityId");
		    System.out.println(defaultShiptoId);
		    String custName = (String) session.getAttribute("custName");
		    String homeBranchZipCode = (String)session.getAttribute("homeBranchZipCode");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String customerTypeValue = (String)session.getAttribute("customerFlag");
		    String customerTypeName = (String)session.getAttribute("customerTypeName");
		    
					
		    /*if(homeBranchZipCode!=null && CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT")!=null && CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT").trim().equalsIgnoreCase("Y")){
		    	YahooBossSupport getUserLocation = new YahooBossSupport();
		    	getUserLocation.locateUser(homeBranchZipCode.trim());
		        String userLati  = YahooBossSupport.getLatitude();
		        String userLongi = YahooBossSupport.getLongitude();
		        session.setAttribute("homeBranchZipCode", homeBranchZipCode);
		        session.setAttribute("homeBranchName", YahooBossSupport.getCity());
		        homeBranchZipCode = homeBranchZipCode+"|City :"+YahooBossSupport.getCity()+ "|State :"+YahooBossSupport.getState();
		    	session.setAttribute("homeBranchLongi", userLongi);
		    	session.setAttribute("homeBranchLati", userLati);
		    	session.setAttribute("homeBranchDisplay", homeBranchZipCode);
		    }
		    if(defaultShiptoId!=null)
		    {
		    	session.setAttribute("defaultShipToId", defaultShiptoId);
		    	session.setAttribute("entityId", entityId);
		    }
		    int shipBranchId = 0;
	    	String shipBranchName = "";
		    
	    	if(CommonDBQuery.getSystemParamtersList().get("ERP")!=null && CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("eclipse")){
		    	UsersModel branchCheck = new UsersModel();
			    branchCheck=EntityInquiry.eclipseEntityEnquiry((String) session.getAttribute("userToken"), CommonUtility.validateNumber(entityId), (String)session.getAttribute(Global.USERNAME_KEY));
			    if(branchCheck!=null)
				{
					if(branchCheck.getBranchID()!=null)
					{
					shipBranchId = UsersDAO.getShipBranch(branchCheck.getBranchID());
					session.setAttribute("homeBranchId", branchCheck.getBranchID());
					}
					shipBranchName = UsersDAO.getShipBranchName(Integer.toString(shipBranchId));
					System.out.println("User Home Branch : " + branchCheck.getBranchID());
					System.out.println("User Ship Branch : " + shipBranchId);
				}
	    	}
		    session.setAttribute("shipBranchId", Integer.toString(shipBranchId));
		    session.setAttribute("defaultShipToId", defaultShiptoId);
		    session.setAttribute("entityId", entityId);
		    session.setAttribute("custName", custName);
		    session.setAttribute("shipBranchName", shipBranchName);*/
			if(CommonUtility.validateString(sortBy).length()==0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CATEGORY_SORT_BY")).length()>0){
				sortBy = CommonDBQuery.getSystemParamtersList().get("CATEGORY_SORT_BY");
			}
			String searchIndex = "PH_SEARCH_"+subsetId;
			if(generalSubset>0 && generalSubset!=subsetId)
			 searchIndex = "PH_SEARCH_"+generalSubset+"_"+subsetId;
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
				searchIndex="PH_SEARCH_ALL";
			}
			 taxonomyLevelFilterData = new ArrayList<ProductsModel>();	    
			 taxonomyLevelFilterData = MenuAndBannersDAO.getTopMenuListBySubset().get("PH_SEARCH_"+subsetId);
			 HashMap<String, ArrayList<ProductsModel>> categoryData = new HashMap<String, ArrayList<ProductsModel>>();
			 if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y")){
		 		  categoryData = ProductHunterSolr.taxonomyList("", subsetId, generalSubset, 1, 100, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber("0"), CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,0,"",0,"");
		 		  taxonomyLevelFilterData = categoryData.get("categeoryList");
		 		  attrFilterData = categoryData.get("attrList");
		 	 }
			 LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();					
			 contentObject.put("categoryListData", taxonomyLevelFilterData);
			 contentObject.put("attrFilterData", attrFilterData);
			 contentObject.put("navigationType", "NAVIGATION");
			 contentObject.put("sortBy", sortBy);
			 contentObject.put("resultPage", resultPage);
			 if (customerTypeValue!=null && customerTypeValue.trim().equalsIgnoreCase("Y")){
				 ArrayList<ProductsModel> featureProducts = new ArrayList<ProductsModel>();	
				 featureProducts = ProductsDAO.getFeatureProduct(subsetId, generalSubset);
				 contentObject.put("featuredProducts", featureProducts);
				 if(CommonUtility.validateString(customerTypeName).length()>0){
					 String customerName = customerTypeName.replaceAll("[^a-zA-Z0-9 -]", "");
					 customerName = customerName.toLowerCase();
					 renderContent = LayoutGenerator.templateLoader(customerName+"NationalAccountsPage", contentObject , null, null, null); //National accountPage
				 }
			 }else{
				 if(isFromHomePage!=null && isFromHomePage.trim().equalsIgnoreCase("Home")){
					 contentObject.put("blockType", "Category");
					 renderContent = LayoutGenerator.templateLoader("HomePageCategory", contentObject , null, null, null); //CategoryPage 
				 }
				 else if(isFromHomePage!=null && isFromHomePage.trim().equalsIgnoreCase("catlog")){
					 renderContent = LayoutGenerator.templateLoader("MyCatalogPage", contentObject , null, null, null); //CategoryPage 
				 }else{
					 renderContent = LayoutGenerator.templateLoader("ProductPage", contentObject , null, null, null); //CategoryPage
				 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String synchronizeCustomerPartNumberAjax(){
		
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		/*LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();*/
		try {
			
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			
			int userId = CommonUtility.validateNumber(sessionUserId);
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			int buyingCompanyId = CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString());
			LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumberMap = null;
			String itemIdlistString = CommonUtility.validateString(request.getParameter("itemIdlist"));
			
			//String[] itemIdsFromPage = request.getParameterValues("");
			//if(itemIdsFromPage!=null && itemIdsFromPage.length>0){
				/*String delimit ="";
				String itemIdlistString = "";
				for(String itemIdString : itemIdsFromPage){
					itemIdlistString = itemIdlistString+delimit+itemIdString;
					delimit = " OR ";
				}*/
				
				itemLevelFilterData = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId, generalSubset, itemIdlistString, 0, null,"itemid");
				if (itemLevelFilterData != null) {
					for(ProductsModel item : itemLevelFilterData) {
						int itemId = item.getItemId();
						
						if (session.getAttribute("buyingCompanyId") != null) {
							customerPartNumberMap = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemId), buyingCompanyId, buyingCompanyId);
							
							//---- For Customer Part Number From SOLR
							item.setCustomerPartNumberList(customerPartNumberMap.get(itemId));
							//---- For Customer Part Number From SOLR
							
							//---- For Customer Part Number From ERP
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AJAXSYNCHRONIZEWITHERP")).equalsIgnoreCase("Y")){
								synchronizeCustomerPartNumber(session, item.getPartNumber(), itemId, useBillToEntity, item.getCustomerPartNumberList());
								customerPartNumberMap = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemId), buyingCompanyId, buyingCompanyId);
								//item.setCustomerPartNumberList(customerPartNumberMap.get(itemId));
							}
							//---- For Customer Part Number From ERP
						}
					}
				}
				Gson gson = new Gson();
				renderContent = gson.toJson(customerPartNumberMap);
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
		
	}
	
	public String itemDetail(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			String customerId = (String) session.getAttribute("customerId");
			String customerCountry = (String) session.getAttribute("customerCountry");
			System.out.println("at itemDetailFilter wareHouseCode---------------"+wareHousecode+" : "+customerId+" : "+customerCountry);
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String defaultShiptoId = request.getParameter("defaultShipToId");
			String entityId = request.getParameter("entityId");
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			int buyingCompanyId = 0;
			System.out.println(defaultShiptoId);
			if(defaultShiptoId!=null){
				session.setAttribute("defaultShipToId", defaultShiptoId);
				session.setAttribute("entityId", entityId);
			}
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String taxonomyId=codeId;
			String ipaddress = request.getHeader("X-Forwarded-For");
			if (ipaddress  == null){
				ipaddress = request.getRemoteAddr();
			}
			itemLevelFilterData = new ArrayList<ProductsModel>();
			alternativeItems = new ArrayList<ProductsModel>();
			itemImagesList = new ArrayList<ProductsModel>();
			itemAttributesList = new ArrayList<ProductsModel>();
			itemDocumentsList = new ArrayList<ProductsModel>();
			productDataList = new ArrayList<ProductsModel>();
			linkedItemsList = new ArrayList<ProductsModel>();
			ArrayList<ProductsModel> videoLinkList = new ArrayList<ProductsModel>();
			ArrayList<ProductsModel> customerAlsoBought = new ArrayList<ProductsModel>();
			
			homeTeritory = (String) session.getAttribute("shipBranchId");
			
			HashMap<String, ArrayList<ProductsModel>> itemDetailList = new HashMap<String, ArrayList<ProductsModel>>();
			if(CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR").trim().equalsIgnoreCase("Y"))
			{
				itemDetailList = ProductHunterSolr.itemDetailData(taxonomyId, (String) session.getAttribute("userToken"),(String)session.getAttribute(Global.USERNAME_KEY),entityId,homeTeritory,userId,"",wareHousecode,customerId,customerCountry, generalSubset, subsetId);
			}
			else
			{
				itemDetailList = ProductsDAO.itemDetailData(taxonomyId, (String) session.getAttribute("userToken"),(String)session.getAttribute(Global.USERNAME_KEY),entityId,homeTeritory,userId,"",wareHousecode,customerId,customerCountry);
			}
			itemLevelFilterData = itemDetailList.get("itemData");
			ArrayList<Integer> itemList = new ArrayList<Integer>();
			String itemPartNumber = null;
			
			if (itemLevelFilterData == null) {
				itemLevelFilterData = new ArrayList<ProductsModel>();
			}else{
				buyingCompanyId = CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString());
				for(ProductsModel item : itemLevelFilterData) {
					int itemId = item.getItemId();
					itemList.add(itemId);
					itemPartNumber = item.getPartNumber();
					if(CommonDBQuery.getSystemParamtersList().get("GET_CATALOG_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_CATALOG_FROM_SOLR").trim().equalsIgnoreCase("Y"))
					{
					String catalogNumber=ProductHunterSolr.getCatalogNumber(Integer.toString(itemId));
					System.out.println("catalogNumber"+catalogNumber);
					item.setCatalogId(catalogNumber);
					}
					
					if (session.getAttribute("buyingCompanyId") != null) {
						LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemId), buyingCompanyId, buyingCompanyId);
						//buyingCompanyId = CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString());
						//---- For Customer Part Number From SOLR
						item.setCustomerPartNumberList(customerPartNumber.get(itemId));
						//---- For Customer Part Number From SOLR
						//---- For Customer Part Number From ERP
						if(CommonDBQuery.getSystemParamtersList().get("SYNCHRONIZEWITHERP")!=null && CommonDBQuery.getSystemParamtersList().get("SYNCHRONIZEWITHERP").trim().equalsIgnoreCase("Y")){
							synchronizeCustomerPartNumber(session, item.getPartNumber(), itemId, useBillToEntity, item.getCustomerPartNumberList());
							customerPartNumber = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemId), buyingCompanyId, buyingCompanyId);
							item.setCustomerPartNumberList(customerPartNumber.get(itemId));
						}
					}
				}
			}
			
			if(CommonDBQuery.getSystemParamtersList().get("GET_ALTERNATIVE_ITEMS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ALTERNATIVE_ITEMS").trim().equalsIgnoreCase("Y")){
				ProductManagement productOBJ = new ProductManagementImpl();
				ArrayList<String> erpList = new ArrayList<String>();
				ProductManagementModel alternateItemsRequest = new ProductManagementModel();
				alternateItemsRequest.setEntityId(CommonUtility.validateString((String) session.getAttribute("entityId")));
				alternateItemsRequest.setHomeTerritory((String) session.getAttribute("shipBranchId"));
				alternateItemsRequest.setProductNumber(itemPartNumber);
				alternateItemsRequest.setRequiredAvailabilty("Y");
				alternateItemsRequest.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				alternateItemsRequest.setUserToken((String)session.getAttribute("userToken"));
				alternateItemsRequest.setCustomerId((String) session.getAttribute("customerId"));
				ArrayList<ProductsModel> alternativeItemsErp = productOBJ.getAlternateItems(alternateItemsRequest);
				
				if(alternativeItemsErp!=null && !alternativeItemsErp.isEmpty()){
					String buildPartNumberForAlternateItems = "";
					String delimit = "";
					
					for(ProductsModel alternativeItemsErpModel : alternativeItemsErp){
						buildPartNumberForAlternateItems = buildPartNumberForAlternateItems+delimit+alternativeItemsErpModel.getPartNumber();
						delimit = " OR ";
					}
					
					if(CommonUtility.validateString(buildPartNumberForAlternateItems).length()>0){
						alternativeItems = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubset, CommonUtility.validateString(buildPartNumberForAlternateItems),0,null,"partnumber");
					}
					
					if(alternativeItems!=null && !alternativeItems.isEmpty()){
						for(ProductsModel alternativeItemsModelOne : alternativeItems){
							for(ProductsModel alternativeItemsERPModel : alternativeItemsErp){
								if(CommonUtility.validateString(alternativeItemsModelOne.getPartNumber()).equalsIgnoreCase(CommonUtility.validateString(alternativeItemsERPModel.getPartNumber()))){
									alternativeItemsModelOne.setRequestType(alternativeItemsERPModel.getRequestType());
									int itemIdAlt = alternativeItemsModelOne.getItemId();
									itemList.add(itemIdAlt);
								}
							}
						}
					}
					
				}
				
				//alternativeItems = alternativeItemsErp;
			}
			
			
			
			//--------------------------------  INTERACTIVE ADVISOR
			if(CommonDBQuery.getSystemParamtersList().get("INTERACTIVE_ADVISOR")!=null && CommonDBQuery.getSystemParamtersList().get("INTERACTIVE_ADVISOR").trim().equalsIgnoreCase("Y")){
					ArrayList<ProductsModel> interActiveAdvisorItemList = null;
					ArrayList<ProductsModel> interActiveAdvisorItemDetailsList = null;
					if(itemPartNumber!=null && itemPartNumber.trim().length()>0){
						interActiveAdvisorItemList = Rpserviceimpl.callInterActiveAdvisor(itemPartNumber);
						if(interActiveAdvisorItemList!=null && interActiveAdvisorItemList.size()>0){
							String buildPartNumberForInterActiveAdvoisor = "";
							String delimit = "";
							
							for(ProductsModel interActiveModel : interActiveAdvisorItemList){
								buildPartNumberForInterActiveAdvoisor = buildPartNumberForInterActiveAdvoisor+delimit+interActiveModel.getPartNumber();
								delimit = " OR ";
							}
							
							if(buildPartNumberForInterActiveAdvoisor!=null && buildPartNumberForInterActiveAdvoisor.trim().length()>0){
								interActiveAdvisorItemDetailsList = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubset, buildPartNumberForInterActiveAdvoisor,0,null,"partnumber");//buildPartNumberForInterActiveAdvoisor
								if(interActiveAdvisorItemDetailsList!=null && interActiveAdvisorItemDetailsList.size()>0){
									
									for(ProductsModel interActiveAdvisorItemDetailsListModel : interActiveAdvisorItemDetailsList){
										if(interActiveAdvisorItemDetailsListModel!=null){
											for(ProductsModel interActiveAdvisorItemListModel : interActiveAdvisorItemList){
												if(interActiveAdvisorItemDetailsListModel.getPartNumber().equals(interActiveAdvisorItemListModel.getPartNumber().trim())){
													interActiveAdvisorItemDetailsListModel.setInterActiveOfferPosition(interActiveAdvisorItemListModel.getInterActiveOfferPosition());
												}
											}
										}
								   	}
									
									Collections.sort(interActiveAdvisorItemDetailsList, ProductsModel.interActiveOfferPositionComparator);
									for(ProductsModel interActiveAdvisorItemDetailsListModel : interActiveAdvisorItemDetailsList){
										System.out.println(interActiveAdvisorItemDetailsListModel.getInterActiveOfferPosition()+" : "+interActiveAdvisorItemDetailsListModel.getPartNumber());
									}
									contentObject.put("interActiveAdvisorItemDetailsList", interActiveAdvisorItemDetailsList);
								}
							}
						}
					}
			}
			//--------------------------------  INTERACTIVE ADVISOR
			
			
			
			HashMap<String, ArrayList<ProductsModel>> categoryData = null;
			if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y")){
				categoryData = new HashMap<String, ArrayList<ProductsModel>>(); 
				categoryData = ProductHunterSolr.taxonomyList("", subsetId, generalSubset, 1, 100, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber("0"),CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,0,"",0,"");
				//taxonomyLevelFilterData = categoryData.get("categeoryList");
				attrFilterData = categoryData.get("attrList");
				contentObject.put("attrFilterData", attrFilterData);
				contentObject.put("navigationType", "NAVIGATION");
				contentObject.put("sortBy", sortBy);
				contentObject.put("resultPage", resultPage);
			}
			itemImagesList = itemDetailList.get("itemImages");
			itemAttributesList = itemDetailList.get("itemAttributes");
			if(itemDetailList.get("itemDocuments")!=null && itemDetailList.get("itemDocuments").size()>0){
				itemDocumentsList = itemDetailList.get("itemDocuments");
			}
			if(itemDetailList.get("productDataList")!=null && itemDetailList.get("productDataList").size()>0){
				productDataList = itemDetailList.get("productDataList");
			}
			linkedItemsList = itemDetailList.get("linkedItems");
			if(linkedItemsList!=null && linkedItemsList.size()>0){
				linkedItemGroup = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				recommendedItemsList = new ArrayList<ProductsModel>();
				for(ProductsModel lnList:linkedItemsList){
					recommendedItemsList = linkedItemGroup.get(lnList.getLinkTypeName().trim());
					if(recommendedItemsList==null){
						itemList.add(lnList.getItemId());
						recommendedItemsList = new ArrayList<ProductsModel>();
						recommendedItemsList.add(lnList);
						linkedItemGroup.put(lnList.getLinkTypeName().trim(), recommendedItemsList);
					}else{
						itemList.add(lnList.getItemId());
						recommendedItemsList.add(lnList);
						linkedItemGroup.put(lnList.getLinkTypeName().trim(), recommendedItemsList);
					}
				}
				for(String key:linkedItemGroup.keySet()){
					ArrayList<Integer> eachItemList = new ArrayList<Integer>();
					recommendedItemsList = new ArrayList<ProductsModel>();
					recommendedItemsList = linkedItemGroup.get(key);
					for(ProductsModel eachGroupItem:recommendedItemsList){
						eachItemList.add(eachGroupItem.getItemId());
					}
					recommendedItemsList = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubset, StringUtils.join(eachItemList," OR "),0,null,"itemid");
					for(ProductsModel eachGroupItem:recommendedItemsList){
						eachGroupItem.setLinkTypeName(key);
					}
					linkedItemGroup.put(key, recommendedItemsList);
				}
			}
			//--------------------------------  GET ITEM CUSTOM FIELDS
			if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
				if(itemList!=null && itemList.size()>0){
					LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "),"itemid");
					contentObject.put("customFieldVal", customFieldVal);
				}
			}
			//--------------------------------  GET ITEM CUSTOM FIELDS
			
			videoLinkList = itemDetailList.get("videoLinks");
			
		
			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
				
				if(CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR").trim().equalsIgnoreCase("Y")){
					ProductsDAO.writePopularityHits(userId, itemLevelFilterData.get(0).getItemId(), 1, 1);
					ProductsDAO.writeUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Product Detail", session.getId(), ipaddress, "Click", Integer.toString(itemLevelFilterData.get(0).getItemId()));
				}
				else{
					ProductsDAO.updatePopularity(itemLevelFilterData.get(0).getItemId(), 1);
					ProductsDAO.updateHits(itemLevelFilterData.get(0).getItemId());
					UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Product Detail", session.getId(), ipaddress, "Click", Integer.toString(itemLevelFilterData.get(0).getItemId()));
				}
				
				reviewDataList = ProductsDAO.getReviews(itemLevelFilterData.get(0).getItemId());
				reviewList = reviewDataList.get("reviewList");
				reviewListCount = reviewDataList.get("reviewListCount");
				if(reviewList!=null && reviewList.size()>0){
					int reviewSize= reviewList.size();
					if(reviewList.size()>0){
						for(ProductsModel datacount:reviewListCount){
							overAllRate =datacount.getReviewCount()/reviewSize;
							System.out.println( "Over all product Rating is ---"+overAllRate);
						}
					}
				}
				if((itemAttributesList!=null && itemAttributesList.size()>0) || (itemDocumentsList != null && itemDocumentsList.size()>0) && (productDataList != null && productDataList.size()>0) || itemLevelFilterData.get(0).getShortDesc()!=null || itemLevelFilterData.get(0).getLongDesc()!=null){
					flag = true;
				}
			}
			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
				contentObject.put("attributeValueString", itemLevelFilterData.get(0).getAttrValue());
				
				if(CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA")!=null && CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA").trim().equalsIgnoreCase("Y")){
					if(itemList!=null && itemList.size()>0){
						LinkedHashMap<Integer, LinkedHashMap<String, ProductsModel>> shipOrderQtyAndIntervalByItem = ProductHunterSolr.getshipOrderQtyAndIntervalByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "));
						contentObject.put("shipOrderQtyAndInterval", shipOrderQtyAndIntervalByItem);
						
						if(session!=null && session.getAttribute("selectedShipVia")!=null){
							LinkedHashMap<String, ProductsModel> getShipMinOrderAndInterval = shipOrderQtyAndIntervalByItem.get(itemLevelFilterData.get(0).getItemId());
							ProductsModel shipOrderQtyAndIntervalModel = new ProductsModel();
							
							if(getShipMinOrderAndInterval!=null && getShipMinOrderAndInterval.size()>0){
								shipOrderQtyAndIntervalModel = getShipMinOrderAndInterval.get(CommonUtility.validateString(session.getAttribute("selectedShipVia").toString()).trim().toUpperCase());
								
								if(shipOrderQtyAndIntervalModel!=null && shipOrderQtyAndIntervalModel.getMinOrderQty()>0){
									itemLevelFilterData.get(0).setMinOrderQty(shipOrderQtyAndIntervalModel.getMinOrderQty());
								}
								if(shipOrderQtyAndIntervalModel!=null && shipOrderQtyAndIntervalModel.getOrderInterval()>0){
									itemLevelFilterData.get(0).setOrderInterval(shipOrderQtyAndIntervalModel.getOrderInterval());
								}
							}
						}
					}
				}
				
				ProductsModel taxonomyDetail = null;
				String pageTitle = null;
				String metaDescription = null;
				if(itemLevelFilterData.get(0).getPageTitle()!=null && !itemLevelFilterData.get(0).getPageTitle().trim().equalsIgnoreCase("")){
					pageTitle = itemLevelFilterData.get(0).getPageTitle();
				}else{
					
					if(itemLevelFilterData.get(0).getManufacturerPartNumber()!=null){
						pageTitle = itemLevelFilterData.get(0).getBrandName() + " "+itemLevelFilterData.get(0).getManufacturerPartNumber();
					}else{
						pageTitle = itemLevelFilterData.get(0).getBrandName();
					}
				}
				
				if(itemLevelFilterData.get(0).getMetaDesc()!=null && !itemLevelFilterData.get(0).getMetaDesc().trim().equalsIgnoreCase("")){
					metaDescription = itemLevelFilterData.get(0).getMetaDesc();
				}else{
					
					if(itemLevelFilterData.get(0).getManufacturerPartNumber()!=null){
						metaDescription = itemLevelFilterData.get(0).getBrandName() + " "+itemLevelFilterData.get(0).getManufacturerPartNumber();
					}else{
						metaDescription = itemLevelFilterData.get(0).getBrandName();
					}
				}
				
				if(CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR").trim().equalsIgnoreCase("Y")){
					taxonomyDetail = ProductHunterSolr.getTaxonomyDetail(itemLevelFilterData.get(0).getItemId(), subsetId, generalSubset);
				}
				else{
					 taxonomyDetail = ProductsDAO.getTreeIdLevelNo(taxonomyId,CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
				}
	 			LinkedHashMap<String, Object> selectableObject = new LinkedHashMap<String, Object>();
	 			int productId = 0;
	 			int treeId = 0;
	 			if(taxonomyDetail!=null){
					//taxonomyDetail.getCategoryCode()
					productId = itemLevelFilterData.get(0).getProductId();
					treeId = CommonUtility.validateNumber(taxonomyDetail.getCategoryCode());
					if(productId>0 && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y")){
						//selectableObject = ProductHunterSolr.getSelectableItems(productId, subsetId, generalSubset, treeId);
						selectableObject = ProductHunterSolr.getSelectableItemsV2(productId, subsetId, generalSubset, treeId, attrFilterList, 0, 6);
						attrFilterData = (ArrayList<ProductsModel>) selectableObject.get("attrList");
					}
					
					if(CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR").trim().equalsIgnoreCase("Y")){
						breadCrumbList = ProductHunterSolr.breadCrumbList(subsetId, generalSubset, CommonUtility.validateNumber(taxonomyDetail.getCategoryCode()));
	
					}
					else{
						breadCrumbList = ProductsDAO.getBreadCrumb(Integer.toString(taxonomyDetail.getLevelNumber()), taxonomyDetail.getCategoryCode());
					}
					taxonomyDetail = new ProductsModel();
					if(itemLevelFilterData.get(0).getPageTitle()!=null && !itemLevelFilterData.get(0).getPageTitle().trim().equalsIgnoreCase("")){
						taxonomyDetail.setCategoryName(itemLevelFilterData.get(0).getPageTitle());
					}else{
						String categoryName = "";
						if(itemLevelFilterData.get(0).getManufacturerPartNumber()!=null){
							categoryName = itemLevelFilterData.get(0).getBrandName() + " "+itemLevelFilterData.get(0).getManufacturerPartNumber();
						}else{
							categoryName = itemLevelFilterData.get(0).getBrandName();
						}
						taxonomyDetail.setCategoryName(categoryName);
					}
					breadCrumbList.add(taxonomyDetail);
				}
				metaTagString = itemLevelFilterData.get(0).getMetaKeyword();
				if(metaTagString==null){
					metaTagString = itemLevelFilterData.get(0).getBrandName() + " "+itemLevelFilterData.get(0).getManufacturerPartNumber();
				}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CUSTOMER_ALSO_BOUGHT")).equalsIgnoreCase("Y")){
					customerAlsoBought = ProductsDAO.getCustomerAlsoBought(userId, subsetId, generalSubset, itemLevelFilterData.get(0).getItemId(), userId, 1, 2);
					
				}
				
				if(wareHousecode!=null && wareHousecode.trim().length()>0){
					contentObject.put("branchName",UsersDAO.getShipBranchName(wareHousecode));
				}
				
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_ARI_API")).equalsIgnoreCase("Y")){
					contentObject.put("ARIUrl", CommonDBQuery.getSystemParamtersList().get("ARI_SERVICE_URL"));
					contentObject.put("tempSubset", tempSubset);
					contentObject.put("tempGeneralSubset",tempGeneralSubset);
					DataSmartController.getInstance().firstHitCheck();
					List<CustomTable> tableDetails = DataSmartController.getInstance().getCustomTableBrandData();
					if(tableDetails!=null && tableDetails.size()>0){
						for(CustomTable eachBrand:tableDetails){
							if(itemLevelFilterData.get(0).getBrandId()==CommonUtility.validateNumber(eachBrand.getEntityId())){
								contentObject.put(DatasmartConstantVariables.BRAND_NAME,eachBrand.getEntityFieldValue().get("BRAND_NAME"));
								contentObject.put(DatasmartConstantVariables.BRAND_ARI_CODE,eachBrand.getTableDetails().get(0).get("ARI_BRAND_CODE"));
								contentObject.put(DatasmartConstantVariables.BRAND_ID,eachBrand.getEntityId());
								contentObject.put("IS_ARI_BRAND",eachBrand.getTableDetails().get(0).get("IS_ARI_BRAND"));
								break;
							}
						}
					}
					if(CommonUtility.validateString((String)contentObject.get("IS_ARI_BRAND")).equalsIgnoreCase("Y")){
						//var url = "https://weingartzbeta.cimm2.com/cimm2bcentral/ari/searchPartModels/"+brandName+"/"+partNum+"/1/"+NumOfResult;
						String[] urlParam = new String[]{(String)contentObject.get(DatasmartConstantVariables.BRAND_ID),itemLevelFilterData.get(0).getPartNumber().substring(2),"1","10"};
						contentObject.put(DatasmartConstantVariables.URL_PARAM,urlParam);
						contentObject.put(DatasmartConstantVariables.REQ_TYPE,"searchPartModels");
						contentObject = DataSmartController.getInstance().getARIData(contentObject);
					}
					
				}
	
				if(session.getAttribute("DFMMode")!=null && CommonUtility.validateString((String)session.getAttribute("DFMMode")).length()>0){
					contentObject.put("DFMMode",CommonUtility.validateString((String)session.getAttribute("DFMMode")));
				}else{
					contentObject.put("DFMMode","N");
				}
				
				contentObject.put("manufacturerId", itemLevelFilterData.get(0).getManufacturerId());
				contentObject.put("manufacturerName", itemLevelFilterData.get(0).getManufacturerName());
				contentObject.put("customerAlsoBought", customerAlsoBought);
				contentObject.put("pageTitle", pageTitle);
				contentObject.put("metaDescription", metaDescription);
				contentObject.put("facebook",CommonDBQuery.getSystemParamtersList().get("FACEBOOK"));
				contentObject.put("twitter",CommonDBQuery.getSystemParamtersList().get("TWITTER"));
				contentObject.put("appId",CommonDBQuery.getSystemParamtersList().get("FACEBOOK_APP_ID")); 
				contentObject.put("itemMap", selectableObject.get("itemMap"));
				contentObject.put("selectableItemList", selectableObject.get("itemList"));
				contentObject.put("selectableList", selectableObject.get("selectableList"));
				contentObject.put("selecatableJson", selectableObject.get("selecatableJson"));
				contentObject.put("selectableMultiList", selectableObject.get("selectableMultiList"));
				contentObject.put("selecatableMap", selectableObject.get("selecatableMap"));
				
				contentObject.put("itemLevelFilterData", itemLevelFilterData);
				contentObject.put("itemImagesList", itemImagesList);
				contentObject.put("itemAttributesList", itemAttributesList);
				contentObject.put("itemDocumentsList", itemDocumentsList);
				contentObject.put("productDataList", productDataList);
				contentObject.put("linkedItemsList", linkedItemsList);
				contentObject.put("homeTeritory", homeTeritory);
				contentObject.put("linkedItemGroup", linkedItemGroup);
				contentObject.put("recommendedItemsList", recommendedItemsList);
				contentObject.put("reviewDataList", reviewDataList);
				contentObject.put("reviewList", reviewList);
				contentObject.put("reviewListCount", reviewListCount);
				contentObject.put("overAllRate", overAllRate);
				contentObject.put("breadCrumbList", breadCrumbList);
				contentObject.put("metaTagString", metaTagString);
				contentObject.put("alternativeItems", alternativeItems);
				contentObject.put("productAttrFilterData", attrFilterData);
				contentObject.put("codeId", treeId);
				contentObject.put("productId", productId);
			    contentObject.put("attrFilteredList", attrFilteredList);
				
				if(videoLinkList!=null && videoLinkList.size()>0){
					contentObject.put("videoLinkList", videoLinkList);
				}
				if(CommonUtility.validateString(reqType).length()>0){
					contentObject.put("responseType", "ItemDetail");
					if(CommonUtility.validateString(reqType).equalsIgnoreCase("ItemDetail")){
						//renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
						renderContent = LayoutGenerator.templateLoader("ProductModePage", contentObject , null, null, null);
					}else{
						renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject , null, null, null);	
					}
				}else{
					renderContent = LayoutGenerator.templateLoader("ProductDetailPage", contentObject , null, null, null);
					//renderContent =  contentObject.toString();
				}
				target = SUCCESS;
			}else{
				if(CommonUtility.validateString(reqType).length()>0){
					contentObject.put("responseType", "ItemDetailNoResult");
					renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject , null, null, null);	
				}else{
					renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
				}
				
				target=NONE; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	
	public String itemDetailSolr(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("bazaarVoiceLoader", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BAZAARVOICE_LOADER")));
			
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			String customerId = (String) session.getAttribute("customerId");
			String customerCountry = (String) session.getAttribute("customerCountry");
			
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String defaultShiptoId = request.getParameter("defaultShipToId");
			String entityId = request.getParameter("entityId");
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			
			if(CommonUtility.validateString(fromProductMode).isEmpty()) {
				fromProductMode = request.getParameter("fromProductMode");
			}
			
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			int userId = CommonUtility.validateNumber(sessionUserId);
			int buyingCompanyId = 0;
			
			System.out.println("at itemDetailFilter wareHouseCode---------------"+wareHousecode+" : "+customerId+" : "+customerCountry);
			System.out.println(defaultShiptoId);
			
			String taxonomyId=codeId;
			String ipaddress = request.getHeader("X-Forwarded-For");
			
			if (ipaddress  == null){
				ipaddress = request.getRemoteAddr();
			}
			
			if(defaultShiptoId!=null){
				session.setAttribute("defaultShipToId", defaultShiptoId);
				session.setAttribute("entityId", entityId);
			}
			
			int currentItemId = 0;
			itemLevelFilterData = new ArrayList<ProductsModel>();
			alternativeItems = new ArrayList<ProductsModel>();
			itemImagesList = new ArrayList<ProductsModel>();
			itemAttributesList = new ArrayList<ProductsModel>();
			itemDocumentsList = new ArrayList<ProductsModel>();
			productDataList = new ArrayList<ProductsModel>();
			linkedItemsList = new ArrayList<ProductsModel>();
			ArrayList<ProductsModel> videoLinkList = new ArrayList<ProductsModel>();
			ArrayList<ProductsModel> customerAlsoBought = new ArrayList<ProductsModel>();
			
			homeTeritory = (String) session.getAttribute("shipBranchId");
			
			HashMap<String, ArrayList<ProductsModel>> itemDetailList = new HashMap<String, ArrayList<ProductsModel>>();
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableDetailPageFromSolr")).equalsIgnoreCase("Y")){
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEM_DETAIL_DATA_V2")).equalsIgnoreCase("Y")) {
					itemDetailList = ProductHunterSolr.itemDetailDataV2(taxonomyId, (String) session.getAttribute("userToken"),(String)session.getAttribute(Global.USERNAME_KEY),entityId,homeTeritory,userId,"",wareHousecode,customerId,customerCountry, generalSubset, subsetId);
					
				}else {
					itemDetailList = ProductHunterSolr.itemDetailData(taxonomyId, (String) session.getAttribute("userToken"),(String)session.getAttribute(Global.USERNAME_KEY),entityId,homeTeritory,userId,"",wareHousecode,customerId,customerCountry, generalSubset, subsetId);
				}
			}else{
				
				itemDetailList = ProductsDAO.itemDetailData(taxonomyId, (String) session.getAttribute("userToken"),(String)session.getAttribute(Global.USERNAME_KEY),entityId,homeTeritory,userId,"",wareHousecode,customerId,customerCountry);
			}
			itemLevelFilterData = itemDetailList.get("itemData");
			
			ArrayList<Integer> itemList = new ArrayList<Integer>();
			String itemPartNumber = null;
			
			if (itemLevelFilterData == null) {
				itemLevelFilterData = new ArrayList<ProductsModel>();
			}else{
				
				buyingCompanyId = CommonUtility.validateNumber((String)session.getAttribute("buyingCompanyId"));
				
				for(ProductsModel item : itemLevelFilterData) {
					
					int itemId = item.getItemId();
					    currentItemId = itemId;
						itemList.add(itemId);
						itemPartNumber = item.getPartNumber();
					
					if(CommonDBQuery.getSystemParamtersList().get("GET_CATALOG_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_CATALOG_FROM_SOLR").trim().equalsIgnoreCase("Y")){
						String catalogNumber=ProductHunterSolr.getCatalogNumber(Integer.toString(itemId));
						System.out.println("catalogNumber"+catalogNumber);
						item.setCatalogId(catalogNumber);
					}
					
					if (session.getAttribute("buyingCompanyId") != null) {
						LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemId), buyingCompanyId, buyingCompanyId);
						
						//---- For Customer Part Number From SOLR
						item.setCustomerPartNumberList(customerPartNumber.get(itemId));
						//---- For Customer Part Number From SOLR
						
						//---- For Customer Part Number From ERP
						if(CommonDBQuery.getSystemParamtersList().get("SYNCHRONIZEWITHERP")!=null && CommonDBQuery.getSystemParamtersList().get("SYNCHRONIZEWITHERP").trim().equalsIgnoreCase("Y")){
							synchronizeCustomerPartNumber(session, item.getPartNumber(), itemId, useBillToEntity, item.getCustomerPartNumberList());
							customerPartNumber = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemId), buyingCompanyId, buyingCompanyId);
							item.setCustomerPartNumberList(customerPartNumber.get(itemId));
						}
					}
				}
			}
			
			//--------------------------------  alternativeItems
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableAlternateItems")).equalsIgnoreCase("Y")){
				LinkedHashMap<String, Object> productsActionUtilityContentObject = ProductsActionUtility.getAlternateItems(session, itemList, itemPartNumber);
				alternativeItems = (ArrayList<ProductsModel>) productsActionUtilityContentObject.get("alternativeItems");
				itemList = (ArrayList<Integer>) productsActionUtilityContentObject.get("itemList");
			}
			//--------------------------------  alternativeItems
			
			
			//--------------------------------  INTERACTIVE ADVISOR
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableInteractiveAdvisor")).equalsIgnoreCase("Y")){
				LinkedHashMap<String, Object> productsActionUtilityContentObject = ProductsActionUtility.getInteractiveAdvisor(session, contentObject, itemPartNumber);
				ArrayList<ProductsModel> interActiveAdvisorItemDetailsList = (ArrayList<ProductsModel>) productsActionUtilityContentObject.get("interActiveAdvisorItemDetailsList");
				contentObject.put("interActiveAdvisorItemDetailsList", interActiveAdvisorItemDetailsList);
			}
			//--------------------------------  INTERACTIVE ADVISOR
			
			
			
			HashMap<String, ArrayList<ProductsModel>> categoryData = null;
			if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y")){
				categoryData = new HashMap<String, ArrayList<ProductsModel>>(); 
				categoryData = ProductHunterSolr.taxonomyList("", subsetId, generalSubset, 1, 100, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber("0"),CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,0,"",0,"");
				
				attrFilterData = categoryData.get("attrList");
				contentObject.put("attrFilterData", attrFilterData);
				contentObject.put("navigationType", "NAVIGATION");
				contentObject.put("sortBy", sortBy);
				contentObject.put("resultPage", resultPage);
			}
			
			itemImagesList = itemDetailList.get("itemImages");
			itemAttributesList = itemDetailList.get("itemAttributes");
			if(itemDetailList.get("itemDocuments")!=null && itemDetailList.get("itemDocuments").size()>0){
				itemDocumentsList = itemDetailList.get("itemDocuments");
			}
			if(itemDetailList.get("productDataList")!=null && itemDetailList.get("productDataList").size()>0){
				productDataList = itemDetailList.get("productDataList");
			}
			
			linkedItemsList = itemDetailList.get("linkedItems");
			if(linkedItemsList!=null && linkedItemsList.size()>0){
				linkedItemGroup = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				recommendedItemsList = new ArrayList<ProductsModel>();
				
				for(ProductsModel lnList:linkedItemsList){
					recommendedItemsList = linkedItemGroup.get(lnList.getLinkTypeName().trim());
					if(recommendedItemsList==null){
						itemList.add(lnList.getItemId());
						recommendedItemsList = new ArrayList<ProductsModel>();
						recommendedItemsList.add(lnList);
						linkedItemGroup.put(lnList.getLinkTypeName().trim(), recommendedItemsList);
					}else{
						itemList.add(lnList.getItemId());
						recommendedItemsList.add(lnList);
						linkedItemGroup.put(lnList.getLinkTypeName().trim(), recommendedItemsList);
					}
				}
				
				for(String key:linkedItemGroup.keySet()){
					ArrayList<Integer> eachItemList = new ArrayList<Integer>();
					recommendedItemsList = new ArrayList<ProductsModel>();
					recommendedItemsList = linkedItemGroup.get(key);
					for(ProductsModel eachGroupItem:recommendedItemsList){
						eachItemList.add(eachGroupItem.getItemId());
					}
					recommendedItemsList = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId, generalSubset, StringUtils.join(eachItemList," OR "),0,null,"itemid");
					for(ProductsModel eachGroupItem:recommendedItemsList){
						eachGroupItem.setLinkTypeName(key);
					}
					linkedItemGroup.put(key, recommendedItemsList);
				}
			}
			
			//--------------------------------  GET ITEM CUSTOM FIELDS
			if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
				if(itemList!=null && itemList.size()>0){
					LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "),"itemid");
					contentObject.put("customFieldVal", customFieldVal);
				}
			}
			//--------------------------------  GET ITEM CUSTOM FIELDS
			
			videoLinkList = itemDetailList.get("videoLinks");
			
			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
				
				if(CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR").trim().equalsIgnoreCase("Y")){
					ProductsDAO.writePopularityHits(userId, itemLevelFilterData.get(0).getItemId(), 1, 1);
					ProductsDAO.writeUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Product Detail", session.getId(), ipaddress, "Click", Integer.toString(itemLevelFilterData.get(0).getItemId()));
				}else{
					ProductsDAO.updatePopularity(itemLevelFilterData.get(0).getItemId(), 1);
					ProductsDAO.updateHits(itemLevelFilterData.get(0).getItemId());
					UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Product Detail", session.getId(), ipaddress, "Click", Integer.toString(itemLevelFilterData.get(0).getItemId()));
				}
				
				reviewDataList = ProductsDAO.getReviews(itemLevelFilterData.get(0).getItemId());
				reviewList = reviewDataList.get("reviewList");
				reviewListCount = reviewDataList.get("reviewListCount");
				if(reviewList!=null && reviewList.size()>0){
					int reviewSize= reviewList.size();
					if(reviewList.size()>0){
						for(ProductsModel datacount:reviewListCount){
							overAllRate =datacount.getReviewCount()/reviewSize;
							System.out.println( "Over all product Rating is ---"+overAllRate);
						}
					}
				}
				if((itemAttributesList!=null && itemAttributesList.size()>0) || (itemDocumentsList != null && itemDocumentsList.size()>0) && (productDataList != null && productDataList.size()>0) || itemLevelFilterData.get(0).getShortDesc()!=null || itemLevelFilterData.get(0).getLongDesc()!=null){
					flag = true;
				}
			}
			
			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
				
				contentObject.put("attributeValueString", itemLevelFilterData.get(0).getAttrValue());
				
				if(CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA")!=null && CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA").trim().equalsIgnoreCase("Y")){
					if(itemList!=null && itemList.size()>0){
						LinkedHashMap<Integer, LinkedHashMap<String, ProductsModel>> shipOrderQtyAndIntervalByItem = ProductHunterSolr.getshipOrderQtyAndIntervalByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "));
						contentObject.put("shipOrderQtyAndInterval", shipOrderQtyAndIntervalByItem);
						
						if(session!=null && session.getAttribute("selectedShipVia")!=null){
							LinkedHashMap<String, ProductsModel> getShipMinOrderAndInterval = shipOrderQtyAndIntervalByItem.get(itemLevelFilterData.get(0).getItemId());
							ProductsModel shipOrderQtyAndIntervalModel = new ProductsModel();
							
							if(getShipMinOrderAndInterval!=null && getShipMinOrderAndInterval.size()>0){
								shipOrderQtyAndIntervalModel = getShipMinOrderAndInterval.get(CommonUtility.validateString(session.getAttribute("selectedShipVia").toString()).trim().toUpperCase());
								
								if(shipOrderQtyAndIntervalModel!=null && shipOrderQtyAndIntervalModel.getMinOrderQty()>0){
									itemLevelFilterData.get(0).setMinOrderQty(shipOrderQtyAndIntervalModel.getMinOrderQty());
								}
								if(shipOrderQtyAndIntervalModel!=null && shipOrderQtyAndIntervalModel.getOrderInterval()>0){
									itemLevelFilterData.get(0).setOrderInterval(shipOrderQtyAndIntervalModel.getOrderInterval());
								}
							}
						}
					}
				}
				
				ProductsModel taxonomyDetail = null;
				String pageTitle = null;
				String metaDescription = null;
				
				if(itemLevelFilterData.get(0).getPageTitle()!=null && !itemLevelFilterData.get(0).getPageTitle().trim().equalsIgnoreCase("")){
					pageTitle = itemLevelFilterData.get(0).getPageTitle();
				}else{
					
					if(itemLevelFilterData.get(0).getManufacturerPartNumber()!=null){
						pageTitle = itemLevelFilterData.get(0).getBrandName() + " "+itemLevelFilterData.get(0).getManufacturerPartNumber();
					}else{
						pageTitle = itemLevelFilterData.get(0).getBrandName();
					}
				}
				
				if(itemLevelFilterData.get(0).getMetaDesc()!=null && !itemLevelFilterData.get(0).getMetaDesc().trim().equalsIgnoreCase("")){
					metaDescription = itemLevelFilterData.get(0).getMetaDesc();
				}else{
					
					if(itemLevelFilterData.get(0).getManufacturerPartNumber()!=null){
						metaDescription = itemLevelFilterData.get(0).getBrandName() + " "+itemLevelFilterData.get(0).getManufacturerPartNumber();
					}else{
						metaDescription = itemLevelFilterData.get(0).getBrandName();
					}
				}
				
				//if(CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR").trim().equalsIgnoreCase("Y")){
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableDetailPageFromSolr")).equalsIgnoreCase("Y")){
					taxonomyDetail = ProductHunterSolr.getTaxonomyDetail(itemLevelFilterData.get(0).getItemId(), subsetId, generalSubset);
				}else{
					 taxonomyDetail = ProductsDAO.getTreeIdLevelNo(taxonomyId,CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
				}
				
	 			LinkedHashMap<String, Object> selectableObject = new LinkedHashMap<String, Object>();
	 			int productId = 0;
	 			int treeId = 0;
	 			
	 			if(taxonomyDetail!=null){
					productId = itemLevelFilterData.get(0).getProductId();
					treeId = CommonUtility.validateNumber(taxonomyDetail.getCategoryCode());
					if(productId>0 && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCH_BY_PRODUCT").trim().equalsIgnoreCase("Y") && !CommonUtility.validateString(fromProductMode).equalsIgnoreCase("N")){
						//selectableObject = ProductHunterSolr.getSelectableItems(productId, subsetId, generalSubset, treeId);
						selectableObject = ProductHunterSolr.getSelectableItemsV2(productId, subsetId, generalSubset, treeId, attrFilterList, 0, 6);
						attrFilterData = (ArrayList<ProductsModel>) selectableObject.get("attrList");
					}
					
					//if(CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR").trim().equalsIgnoreCase("Y")){
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableDetailPageFromSolr")).equalsIgnoreCase("Y")){
						breadCrumbList = ProductHunterSolr.breadCrumbList(subsetId, generalSubset, CommonUtility.validateNumber(taxonomyDetail.getCategoryCode()));
	
					}else{
						breadCrumbList = ProductsDAO.getBreadCrumb(Integer.toString(taxonomyDetail.getLevelNumber()), taxonomyDetail.getCategoryCode());
					}
					
					taxonomyDetail = new ProductsModel();
					
					if(itemLevelFilterData.get(0).getPageTitle()!=null && !itemLevelFilterData.get(0).getPageTitle().trim().equalsIgnoreCase("")){
						taxonomyDetail.setCategoryName(itemLevelFilterData.get(0).getPageTitle());
					}else{
						
						String categoryName = "";
						
						if(itemLevelFilterData.get(0).getManufacturerPartNumber()!=null){
							categoryName = itemLevelFilterData.get(0).getBrandName() + " "+itemLevelFilterData.get(0).getManufacturerPartNumber();
						}
						if(CommonUtility.customServiceUtility()!=null)
						{
							categoryName =CommonUtility.customServiceUtility().getcategoryName(itemLevelFilterData,categoryName);
						}else{
							categoryName = itemLevelFilterData.get(0).getBrandName();
						}
						taxonomyDetail.setCategoryName(categoryName);
					}
					breadCrumbList.add(taxonomyDetail);
				}
	 			
	 			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCustomerAlsoBought")).equalsIgnoreCase("Y")){
	 				ArrayList<Integer> eachItemList = new ArrayList<Integer>();
	 				eachItemList.add(currentItemId);
	 				
	 				if(selectableObject!=null && selectableObject.get("itemIdAndpriceidMap")!=null){
	 					LinkedHashMap<String, String> itemIdMap = (LinkedHashMap<String, String>) selectableObject.get("itemIdAndpriceidMap");
	 					for (Map.Entry<String, String> entry : itemIdMap.entrySet()) {
	 						eachItemList.add(CommonUtility.validateNumber(entry.getKey()));
	 					}
	 				}
	 				if(eachItemList!=null && !eachItemList.isEmpty()){
						LinkedHashMap<String, Object> productsActionUtilityContentObject = ProductsActionUtility.getCustomerAlsoBoughtItems(session, StringUtils.join(eachItemList," , "));
						if(productsActionUtilityContentObject!=null && productsActionUtilityContentObject.get("customerAlsoBoughtItemArrayList")!=null){
							contentObject.put("customerAlsoBoughtItemArrayList", productsActionUtilityContentObject.get("customerAlsoBoughtItemArrayList"));
						}
					}
	 			}
	 			
	 			
				metaTagString = itemLevelFilterData.get(0).getMetaKeyword();
				
				if(metaTagString==null){
					metaTagString = itemLevelFilterData.get(0).getBrandName() + " "+itemLevelFilterData.get(0).getManufacturerPartNumber();
				}
				
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCustomerAlsoBought")).equalsIgnoreCase("Y")){
					customerAlsoBought = ProductsDAO.getCustomerAlsoBought(userId, subsetId, generalSubset, itemLevelFilterData.get(0).getItemId(), userId, 1, 2);
				}
				
				if(wareHousecode!=null && wareHousecode.trim().length()>0){
					contentObject.put("branchName",UsersDAO.getShipBranchName(wareHousecode));
				}
				
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_ARI_API")).equalsIgnoreCase("Y")){
					contentObject.put("ARIUrl", CommonDBQuery.getSystemParamtersList().get("ARI_SERVICE_URL"));
					contentObject.put("tempSubset", tempSubset);
					contentObject.put("tempGeneralSubset",tempGeneralSubset);
					DataSmartController.getInstance().firstHitCheck();
					List<CustomTable> tableDetails = DataSmartController.getInstance().getCustomTableBrandData();
					if(tableDetails!=null && tableDetails.size()>0){
						for(CustomTable eachBrand:tableDetails){
							if(itemLevelFilterData.get(0).getBrandId()==CommonUtility.validateNumber(eachBrand.getEntityId())){
								contentObject.put(DatasmartConstantVariables.BRAND_NAME,eachBrand.getEntityFieldValue().get("BRAND_NAME"));
								contentObject.put(DatasmartConstantVariables.BRAND_ARI_CODE,eachBrand.getTableDetails().get(0).get("ARI_BRAND_CODE"));
								contentObject.put(DatasmartConstantVariables.BRAND_ID,eachBrand.getEntityId());
								contentObject.put("IS_ARI_BRAND",eachBrand.getTableDetails().get(0).get("IS_ARI_BRAND"));
								break;
							}
						}
					}
					if(CommonUtility.validateString((String)contentObject.get("IS_ARI_BRAND")).equalsIgnoreCase("Y")){
						//var url = "https://weingartzbeta.cimm2.com/cimm2bcentral/ari/searchPartModels/"+brandName+"/"+partNum+"/1/"+NumOfResult;
						String[] urlParam = new String[]{(String)contentObject.get(DatasmartConstantVariables.BRAND_ID),itemLevelFilterData.get(0).getPartNumber().substring(2),"1","10"};
						contentObject.put(DatasmartConstantVariables.URL_PARAM,urlParam);
						contentObject.put(DatasmartConstantVariables.REQ_TYPE,"searchPartModels");
						contentObject = DataSmartController.getInstance().getARIData(contentObject);
					}
					
				}
	
				if(session.getAttribute("DFMMode")!=null && CommonUtility.validateString((String)session.getAttribute("DFMMode")).length()>0){
					contentObject.put("DFMMode",CommonUtility.validateString((String)session.getAttribute("DFMMode")));
				}else{
					contentObject.put("DFMMode","N");
				}
				contentObject.put("fromProductMode", fromProductMode);
				contentObject.put("manufacturerId", itemLevelFilterData.get(0).getManufacturerId());
				contentObject.put("manufacturerPartNo", itemLevelFilterData.get(0).getManufacturerPartNumber());
				contentObject.put("manufacturerName", itemLevelFilterData.get(0).getManufacturerName());
				contentObject.put("customerAlsoBought", customerAlsoBought);
				contentObject.put("pageTitle", pageTitle);
				contentObject.put("metaDescription", metaDescription);
				contentObject.put("facebook",CommonDBQuery.getSystemParamtersList().get("FACEBOOK"));
				contentObject.put("twitter",CommonDBQuery.getSystemParamtersList().get("TWITTER"));
				contentObject.put("appId",CommonDBQuery.getSystemParamtersList().get("FACEBOOK_APP_ID")); 
				contentObject.put("itemMap", selectableObject.get("itemMap"));
				contentObject.put("selectableItemList", selectableObject.get("itemList"));
				contentObject.put("selectableList", selectableObject.get("selectableList"));
				contentObject.put("selecatableJson", selectableObject.get("selecatableJson"));
				contentObject.put("selectableMultiList", selectableObject.get("selectableMultiList"));
				contentObject.put("selecatableMap", selectableObject.get("selecatableMap"));
				contentObject.put("itemLevelFilterData", itemLevelFilterData);
				contentObject.put("itemImagesList", itemImagesList);
				contentObject.put("itemAttributesList", itemAttributesList);
				contentObject.put("itemDocumentsList", itemDocumentsList);
				contentObject.put("productDataList", productDataList);
				contentObject.put("linkedItemsList", linkedItemsList);
				contentObject.put("homeTeritory", homeTeritory);
				contentObject.put("linkedItemGroup", linkedItemGroup);
				contentObject.put("recommendedItemsList", recommendedItemsList);
				contentObject.put("reviewDataList", reviewDataList);
				contentObject.put("reviewList", reviewList);
				contentObject.put("reviewListCount", reviewListCount);
				contentObject.put("overAllRate", overAllRate);
				contentObject.put("breadCrumbList", breadCrumbList);
				contentObject.put("metaTagString", metaTagString);
				contentObject.put("alternativeItems", alternativeItems);
				contentObject.put("productAttrFilterData", attrFilterData);
				contentObject.put("codeId", treeId);
				contentObject.put("itemId", currentItemId);
				contentObject.put("itemPriceId", taxonomyId);
				contentObject.put("productId", productId);
			    contentObject.put("attrFilteredList", attrFilteredList);
			    contentObject.put("siteShipViaList", CommonDBQuery.getSiteShipViaList());
				
				if(videoLinkList!=null && videoLinkList.size()>0){
					contentObject.put("videoLinkList", videoLinkList);
				}
				
				if(CommonUtility.validateString(reqType).length()>0){
					contentObject.put("responseType", "ItemDetail");
					if(CommonUtility.validateString(reqType).equalsIgnoreCase("ItemDetail")){
						renderContent = LayoutGenerator.templateLoader("ProductModePage", contentObject , null, null, null);
					}else{
						renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject , null, null, null);	
					}
					
				}else{
					//Adapt_Pharma multiple shipping address start
					if(CommonUtility.customServiceUtility() != null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MultipleShipToAddressCartAP")).equalsIgnoreCase("Y")) {
						if(CommonUtility.validateString((String)session.getAttribute("multipleShipToAddressAP")).equalsIgnoreCase("Y")) {
							contentObject = CommonUtility.customServiceUtility().getAllShippingAddress(contentObject, buyingCompanyId);
						}
					}
					//Adapt_Pharma multiple shipping address end
					renderContent = LayoutGenerator.templateLoader("ProductDetailPage", contentObject , null, null, null);
				}
				target = SUCCESS;
			}else{
				
				if(CommonUtility.validateString(reqType).length()>0){
					contentObject.put("responseType", "ItemDetailNoResult");
					renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject , null, null, null);	
				}else{
					renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
				}
				
				target=NONE; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}
	
	public String itemLevelFilter()
	{
		long startTimer = CommonUtility.startTimeDispaly();
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			if(codeId==null)
				codeId = "0";
		    String taxonomyId=codeId;
		    String defaultShiptoId = (String) session.getAttribute("defaultShipToId");
		    String entityId = (String) session.getAttribute("entityId");
		    String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
		    String wareHousecode = (String) session.getAttribute("wareHouseCode");
			int userId = CommonUtility.validateNumber(sessionUserId);
			String pageClick = request.getParameter("pageClick");
			if(pageViewMode!=null){
				session.setAttribute("pageViewMode", pageViewMode);
			}
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			if(CommonUtility.validateString(seoUrl).length()>0){
				contentObject.put("seoUrl",seoUrl);
			}else{
				contentObject.put("seoUrl","N");
			}
			boolean isCategoryNav = false;
		    codeId = taxonomyId;
		    codeIdSkip = codeId;
			levelNoSkip = levelNo;
			navigationType = "NAVIGATION";
			
			//To avoid XSS injection - Security_QA
	    	codeId = CommonUtility.sanitizeHtml(codeId);
	    	levelNo = CommonUtility.sanitizeHtml(levelNo);
	    	sortBy = CommonUtility.sanitizeHtml(sortBy);
	    	srchTyp = CommonUtility.sanitizeHtml(srchTyp);
	    	pageNo = CommonUtility.sanitizeHtml(pageNo);
	    	keyWordTxt = CommonUtility.sanitizeHtml(keyWordTxt);
	    	keyWord = CommonUtility.sanitizeHtml(keyWord);
	    	pageViewMode = CommonUtility.sanitizeHtml(pageViewMode);
	    	//attrFilterList = CommonUtility.sanitizeHtml(attrFilterList);
	    	levelNoSkip = CommonUtility.sanitizeHtml(levelNoSkip);
			
		    String tempSubset = (String) session.getAttribute("userSubsetId");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			if(request.getParameter("isPreviouslyPurchased")!=null && request.getParameter("isPreviouslyPurchased").equalsIgnoreCase("Y"))
			{
				viewFrequentlyPurcahsedOnly = "Y";
			}
			if(CommonUtility.validateString(request.getParameter("clearanceItems")).equalsIgnoreCase("Y"))
			{
				clearanceFlag = "Y";
			}
			if(CommonUtility.validateString(request.getParameter("wareHouseItems")).length()>0)
			{
				contentObject.put("wareHouseItems", CommonUtility.validateString(request.getParameter("wareHouseItems")));
				contentObject.put("shipToMe", CommonUtility.validateString(request.getParameter("shipToMe")));
				if(CommonUtility.validateString(request.getParameter("shipToMe")).equalsIgnoreCase("Y")) {
					wareHouseItems = CommonUtility.validateString(request.getParameter("wareHouseItems"))+" AND id:SHIPTOME_*";
				}else if(CommonUtility.validateString(request.getParameter("shipToMe")).equalsIgnoreCase("N")) {
					wareHouseItems = CommonUtility.validateString(request.getParameter("wareHouseItems"))+" AND id:SHIPTOSTORE_*) OR (warehouseCode:NONORGILLSHIPTOSTORE AND id:NONORGILLSHIPTOSTORE_*";
				}else {
					wareHouseItems = CommonUtility.validateString(request.getParameter("wareHouseItems"));
				}
				contentObject.put("wareHouseItemsFlag", "Y");
			}else if(CommonUtility.validateString((String) session.getAttribute("wareHouseItems")).length()>0){
				wareHouseItems = CommonUtility.validateString((String) session.getAttribute("wareHouseItems"));
			}
			
		    int resultPerPage=0;
			if(resultPage!=null && CommonUtility.validateNumber(resultPage) > 0){
				resultPerPage = CommonUtility.validateNumber(resultPage);
			}else{
				resultPerPage = CommonUtility.validateNumber((String)session.getAttribute("resultPage"));
				resultPage = (String)session.getAttribute("resultPage");
			}
		    
		    filterList = attrFilterList; 
		    
		    
			if(srchTyp!=null && srchTyp.equalsIgnoreCase("CATNAV")){
				isCategoryNav = true;
			}
			if(attrFilterList!=null && attrFilterList.trim().equalsIgnoreCase(""))
			    attrFilterList = null;
		    
		    if(filterList!=null && !filterList.trim().equalsIgnoreCase("")){
		    	try {
					filterList = URLEncoder.encode(filterList,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
		    if(resultPerPage==0)
		    {
		    	if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
		    		resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
			    	resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
		    	}else{
		    		resultPage = "12";
			    	resultPerPage = 12;
		    	}
		    }
		    session.setAttribute("resultPage", resultPage);
	
		    
		    if(CommonUtility.validateString(sortBy).length()==0){
		    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_NAVIGATION_SORT_BY")).length()>0){
		    		sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_NAVIGATION_SORT_BY").trim().toLowerCase();
		    	}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_SEARCH_SORT_BY")).length()>0){
		    		sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_SEARCH_SORT_BY").trim().toLowerCase();
		    	}
		    }
		    	
		    System.out.println(attrFilterList);
		    int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
		    int subsetId = CommonUtility.validateNumber(tempSubset);
		    int fromRow =  (noOfPage-1)*resultPerPage + 1;
			int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
			if(vpsid!=null && !vpsid.trim().equalsIgnoreCase("") && !vpsid.trim().equalsIgnoreCase("0"))
				varPsid = CommonUtility.validateNumber(vpsid);
		    itemLevelFilterData = new ArrayList<ProductsModel>();
		    eclipseErrorMassage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNMESSAGE");
		    //itemLevelFilterData = ProductsDAO.itemLevelFilter(taxonomyId,subsetId,generalSubset,fromRow,toRow);
		    HashMap<String, ArrayList<ProductsModel>> navigationResultList = new HashMap<String, ArrayList<ProductsModel>>();
		    
		    String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
			int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
	 	   if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y"))
	 	   {
	 		  homeTeritory = (String) session.getAttribute("shipBranchId");
	 		  fromRow =  (noOfPage-1)*resultPerPage;
	 		// navigationResultList = ProductHunterSolr.searchNavigation("", subsetId, generalSubset, fromRow, toRow, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,"",buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"",0);
	 		
	 		 if(CommonUtility.validateString(keyWordTxt).length()>0) {
			    keyWordTxt = CommonUtility.sanitizeHtml(keyWordTxt);
			 }else{
				keyWordTxt = "";
			 }
	 		 					
	 		if(CommonUtility.validateNumber(taxonomyId)>0 && CommonUtility.validateString(keyWordTxt).length()>0 && fromRow>0 && !CommonUtility.validateString(pageClick).equalsIgnoreCase("Y")){
				fromRow = 0;
				pageNo = "0";
			}
	 		if(CommonDBQuery.getSystemParamtersList().get("NRT")!=null && CommonDBQuery.getSystemParamtersList().get("NRT").trim().equalsIgnoreCase("Y"))
		  	{
	 			navigationResultList = ProductHunterSolrV2.searchNavigation(null, keyWordTxt, subsetId, generalSubset, fromRow, toRow, "NAVIGATION", CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"),  CommonUtility.validateNumber(taxonomyId), attrFilterList, brandId, sortBy, resultPerPage, "", buyingCompanyId, userId, isCategoryNav, false, viewFrequentlyPurcahsedOnly);
		  	}else{
		  		navigationResultList = ProductHunterSolr.searchNavigation(keyWordTxt, subsetId, generalSubset, fromRow, toRow, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,"",buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","","","",isCategoryNav,false,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHouseItems);//wareHousecode, customerId,customerCountry
		  	}
		  		
	 	   }
	 	   else
	 	   {
	 		  navigationResultList = ProductsDAO.searchNavigation("", subsetId, generalSubset, fromRow, toRow, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,true,userId,"",viewFrequentlyPurcahsedOnly);
	 	   }
		    itemLevelFilterData = navigationResultList.get("itemList");
		    
		    categoryFilterData  = navigationResultList.get("categoryList");
		    attrFilterData = navigationResultList.get("attrList");
		    attrFilteredList = navigationResultList.get("filteredList");
		    
		    
		    
		    if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
			    
		    	metaTagString="";
				String s = "";
				for(ProductsModel metaTagKeyword : itemLevelFilterData)
				{
					metaTagString = metaTagString +s+metaTagKeyword.getPartNumber() +" "+ metaTagKeyword.getManufacturerPartNumber();	 	
					s = ",";
				}
				 
			   if(metaTagString!=null && !metaTagString.trim().equals(""))
			   {
				metaTagString = metaTagString.replaceAll("\"", "");
				metaTagString = metaTagString.replaceAll("'", ""); 
			    
			    System.out.println(metaTagString);
			   }
			   if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y"))
	     	   {
				   	breadCrumbList = ProductHunterSolr.breadCrumbList(subsetId, generalSubset, CommonUtility.validateNumber(taxonomyId));
	     	   }
			 else
			 {
		    	breadCrumbList = ProductsDAO.getBreadCrumb(levelNo, taxonomyId);
			 }
		    	resultCount=itemLevelFilterData.get(0).getResultCount();
		    	//CustomServiceProvider
				if(CommonUtility.customServiceUtility()!=null) {
					int resultCount1 = CommonUtility.customServiceUtility().getItemsResultCount(keyWordTxt, subsetId, generalSubset, fromRow, toRow, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,"",buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","","","",isCategoryNav,false,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHousecode);
					contentObject.put("resultCount1", resultCount1);
					int resultCount2 = CommonUtility.customServiceUtility().getItemsResultCount(keyWordTxt, subsetId, generalSubset, fromRow, toRow, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,"",buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","","","",isCategoryNav,false,viewFrequentlyPurcahsedOnly,clearanceFlag,"");
					contentObject.put("resultCount2", resultCount2);
				}
				//CustomServiceProvider
		    	if(CommonUtility.validateString(srchTyp).length()>0 && !isCategoryNav)
		  		 {
		  			 if(breadCrumbList!=null && breadCrumbList.size()>0)
			    		{
			    			keyWord = "";
			    			String c = "";
			    			for(ProductsModel bcList:breadCrumbList)
			    			{
			    				keyWord = keyWord + c + bcList.getCategoryName();
			    				c = " > ";
			    			}
			    		}
			    		 ProductsDAO.writePopularSearchWord(keyWord, userId, subsetId, resultCount, srchTyp);
		  		 }
		    	double disp;
		    	double disp1;
		    	disp = resultCount;
	
		    	if (resultCount > resultPerPage) {
	
		    		paginate = (resultCount / resultPerPage);
		    		disp1 = (disp / resultPerPage);
	
		    	} else {
		    		paginate = (resultCount / resultPerPage);
		    		disp1 = (resultCount / resultPerPage);
		    	}
	
		    	if (disp1 > paginate) {
		    		paginate = paginate + 1;
	
		    	}
		    	
		    	try {
		    		if(attrFilterList!=null){
		    			attrFilterList = attrFilterList.replaceAll("% ", "%25 ");
		    			attrFilterList = URLDecoder.decode(attrFilterList,"UTF-8");
		    		    attrFilterList = attrFilterList.replaceAll("'", "%27");
		    		    String paginateFilter = attrFilterList;
		    		    contentObject.put("paginateFilter",paginateFilter);
		    		    attrFilterList = attrFilterList.replaceAll("&", "&amp;");
	
		    		}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(navigationResultList.get("facetRanges")!=null && navigationResultList.get("facetRanges").size() >0){
					contentObject.put("facetRanges", navigationResultList.get("facetRanges").get(0).getFacetRange());
				}
				levelNo = CommonUtility.sanitizeHtml(levelNo);
				levelNoSkip = CommonUtility.sanitizeHtml(levelNoSkip);
				
				contentObject.put("origin", request.getParameter("origin"));
				contentObject.put("codeId", codeId);
		    	contentObject.put("codeIdSkip", codeIdSkip);
		    	contentObject.put("categoryId", codeIdSkip);
		    	contentObject.put("levelNo", levelNo);
		    	contentObject.put("levelNoSkip", levelNoSkip);
		    	contentObject.put("navigationType", navigationType);
		    	contentObject.put("filterList", filterList);
		    	contentObject.put("attrFilterList", attrFilterList);
		    	contentObject.put("resultPage", resultPage);
		    	contentObject.put("pageNo", pageNo);
		    	contentObject.put("vpsid", vpsid);
		    	contentObject.put("varPsid", varPsid);
		    	contentObject.put("homeTeritory", homeTeritory);
		    	contentObject.put("eclipseErrorMassage", eclipseErrorMassage);
		    	contentObject.put("keyWordTxt", keyWordTxt);
		    	contentObject.put("categoryFilterData", categoryFilterData);
		    	contentObject.put("attrFilterData", attrFilterData);
			    contentObject.put("attrFilteredList", attrFilteredList);
			    contentObject.put("siteShipViaList", CommonDBQuery.getSiteShipViaList());
				
		    	contentObject.put("metaTagString", metaTagString);
		    	contentObject.put("keyWord", keyWord);
		    	contentObject.put("resultCount", resultCount);
		    	contentObject.put("srchTyp", srchTyp);
		    	contentObject.put("gallery", gallery);
		    	contentObject.put("paginate", paginate);
		    	contentObject.put("sortBy", sortBy);
		    	if(pageViewMode==null){
		    		pageViewMode = (String) session.getAttribute("pageViewMode");
		    	}
		    	contentObject.put("pageViewMode", pageViewMode);
		    	/*   	
		    	contentObject.put("keyWord", "");
		    	contentObject.put("srchTyp", srchTyp);
		    	contentObject.put("pageNo", pageNo);
		    	contentObject.put("codeId", taxonomyId);
		    	contentObject.put("codeIdSkip", codeId);
		    	contentObject.put("levelNoSkip", levelNo);
		    	contentObject.put("gallery", gallery);
		    	contentObject.put("levelNo", levelNoSkip);
			    contentObject.put("navigationType", navigationType);
			    contentObject.put("filterList", filterList);
			    contentObject.put("resultPage", resultPage);
			    contentObject.put("attrFilterData", attrFilterData);
			    contentObject.put("attrFilteredList", attrFilteredList);*/
		    	
		    	
		    	if(resultCount>1){
		    		    categoryDescription =new ProductsModel();
		    		    if(CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR").trim().equalsIgnoreCase("Y")){
		    				categoryDescription = ProductHunterSolr.getCategoryDescription(codeId);
		    				categoryBannersList = ProductHunterSolr.getBannerList(codeId);
		    			}else{
		    				categoryDescription = ProductsDAO.getCategoryDescription(codeId);
		    				//categoryBannersList = ProductsDAO.getCategoryBanners(codeId);
		    				categoryBannersList = ProductsDAO.getCategoryBannersV2(CommonUtility.validateNumber(codeId));
		    			}
		    			topBanners=categoryBannersList.get("top");
		    	 	   	leftBanners=categoryBannersList.get("left");
		    	 	   	rightBanners=categoryBannersList.get("right");
		    	 	   	bottomBanners=categoryBannersList.get("bottom");
		    	 	   
		    	 	   if(CommonUtility.validateNumber(pageNo)<1 && categoryDescription!=null && CommonUtility.validateNumber(CommonUtility.validateString(categoryDescription.getStaticPageId()))>0) {
							String staticPageByIdUrl = context+staticPage+find+CommonUtility.validateNumber(CommonUtility.validateString(categoryDescription.getStaticPageId()));
							Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(staticPageByIdUrl, "GET", null, StaticPageData.class);
							if(response!=null && response.getStatus().getCode()==200) {
								StaticPageData staticPageDataModel = (StaticPageData) response.getData();
								if(staticPageDataModel!=null && (staticPageDataModel.getStatus().equalsIgnoreCase("Y") || staticPageDataModel.getStatus().equalsIgnoreCase("A"))) {
									contentObject.put("assignedStaticPageId", staticPageDataModel.getId());
									contentObject.put("assignedStaticPageContent", staticPageDataModel.getPageContent());
									//System.out.println(staticPageDataModel.getPageContent());
								}
							}
		    			}
		    	 	   
		    		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")).equalsIgnoreCase("Y") && CommonUtility.validateNumber(pageNo)<1 && CommonUtility.validateString(siteSearch).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_STATIC_SEARCH")).equalsIgnoreCase("Y")){
				  		 for(ProductsModel tName:breadCrumbList)
				  		 {
				  			 keyWord = tName.getCategoryName();
				  		 }
			 	    	eventSearchData = new ArrayList<ProductsModel>();
			 	    	staticContentData = new ArrayList<ProductsModel>();
			 	    	staticContentData = ProductHunterSolr.SearchStaticContent(keyWord, "",0,2);
			 	    	eventSearchData  = ProductHunterSolr.SearchStaticContent(keyWord, "Event",0,2);
			 	    	contentObject.put("eventSearchData", eventSearchData);
		 	    		contentObject.put("staticContentData", staticContentData);
		 	 	   }
		    		if(breadCrumbList!=null && breadCrumbList.size()>0){
		    			int bI = 1;
		    			for (ProductsModel breadCrumbElement : breadCrumbList) {
		    				if(bI==breadCrumbList.size()){
		    					contentObject.put("heading", breadCrumbElement.getCategoryName());
		    					contentObject.put("pageTitle", breadCrumbElement.getCategoryName());
		    				}
		    				bI++;
						}
		    		}else{
		    			contentObject.put("heading", CommonUtility.validateString(keyWordTxt));
						contentObject.put("pageTitle", CommonUtility.validateString(keyWordTxt));
		    		}
		    		if(wareHousecode!=null && wareHousecode.trim().length()>0){
						contentObject.put("branchName",UsersDAO.getShipBranchName(wareHousecode));
					} 
		    		contentObject.put("breadCrumbList", breadCrumbList);
		    		contentObject.put("topBanners", topBanners); 
		    		contentObject.put("itemListData", itemLevelFilterData);
		    		contentObject.put("categoryDescription", categoryDescription);
		    		contentObject.put("metaDescription", categoryDescription);
		    		contentObject.put("clearanceItems", clearanceFlag);
		    		
		    		if(CommonUtility.validateString(reqType).equalsIgnoreCase("clearanceProducts")){
		    			contentObject.put("responseType", "clearanceProducts");
		    			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		    		}else if(CommonUtility.validateString(reqType).equalsIgnoreCase("loadPage")){
		    	    	contentObject.put("responseType", "loadPage");
		    	    	renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		    		}else{
		    			renderContent = LayoutGenerator.templateLoader("ProductList", contentObject , null, null, null);
		    		}
		    		
		    		target="success";
		    	}else{
		    		itemPriceId = itemLevelFilterData.get(0).getItemPriceId();
		    		itemUrlSingle = itemLevelFilterData.get(0).getItemUrl();
		    		itemUrlSingle = CommonUtility.getUrlString(itemUrlSingle);
		    		fromProductMode = "N";
		    		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRODUCT_MODE_ENABLE_SEARCH")).equalsIgnoreCase("Y")) {
		    			fromProductMode = "Y";
		    		}
		    		target="singleItem";
		    	}
		    }else{
		    	keyWord = CommonUtility.sanitizeHtml(keyWord);
		    	contentObject.put("resultCount", resultCount);
		    	contentObject.put("keyWord", keyWord);
		    	contentObject.put("keyWordTxt", keyWordTxt);
		    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHOW_STATIC_PAGE_FOR_CATEGORY")).equalsIgnoreCase("Y")) {
		    		pageName= ProductsDAO.getCategoryNameFromTaxonomyId(codeId);
		    		pageName = pageName.replaceAll("\"", "");		    		
		    		pageName = pageName.replaceAll(",", "");
		    		pageName = pageName.replaceAll("&", "");
		    		pageName = pageName.replaceAll("[^A-Za-z0-9-]", "");		    				    		
		       	 	target="TostaticPage";
				}else {
		    	renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
	       	 	target=NONE;
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	    return target;
	}
	
	public String taxonomyLevelFilter(){
		try{
			request =ServletActionContext.getRequest();
			HashMap<String, ArrayList<ProductsModel>> categoryData = new HashMap<String, ArrayList<ProductsModel>>();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String defaultShiptoId = (String) session.getAttribute("defaultShipToId");
			String entityId = (String) session.getAttribute("entityId");
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String taxonomyId=codeId;
			
			//To avoid XSS injection - Security_QA
	    	codeId = CommonUtility.sanitizeHtml(codeId);
	    	levelNo = CommonUtility.sanitizeHtml(levelNo);
	    	srchTyp = CommonUtility.sanitizeHtml(srchTyp);
	    	keyWordTxt = CommonUtility.sanitizeHtml(keyWordTxt);
	    	keyWord = CommonUtility.sanitizeHtml(keyWord);
	    	taxonomyId = CommonUtility.sanitizeHtml(taxonomyId);
			
		    String taxonomyLevelNo=levelNo;
		    boolean sortByfromSysParm = false;
		    codeIdSkip = codeId;
		    levelNoSkip = levelNo;
	 	    taxonomyLevelFilterData = new ArrayList<ProductsModel>();	
	 	    eclipseErrorMassage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNMESSAGE");
	
	 	    int resultPerPage=0;
			if(resultPage!=null && CommonUtility.validateNumber(resultPage) > 0){
				resultPerPage = CommonUtility.validateNumber(resultPage);
			}else{
				resultPerPage = CommonUtility.validateNumber((String)session.getAttribute("resultPage"));
				resultPage = (String)session.getAttribute("resultPage");
			}
			
			if(resultPerPage==0)
		    {
		    	if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
		    		resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
			    	resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
		    	}else{
		    		resultPage = "12";
			    	resultPerPage = 12;
		    	}
		    }
		    session.setAttribute("resultPage", resultPage);
		    
		    int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
		    int fromRow =  (noOfPage-1)*resultPerPage + 1;
			int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
		    
		    

	 	   
	 	   //Adapt_Pharma multiple shipping address start
	 	   if(CommonUtility.customServiceUtility() != null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MultipleShipToAddressCartAP")).equalsIgnoreCase("Y")) {
	 		   if(CommonUtility.validateString(request.getParameter("multipleShipToAddressAP")).equalsIgnoreCase("Y")) {
	 			   session.setAttribute("multipleShipToAddressAP", "Y");
	 		   }
	 	   }//Adapt_Pharma multiple shipping address End
	 	  
			HashMap<String, ArrayList<ProductsModel>> navigationResultList = new HashMap<String, ArrayList<ProductsModel>>();
	 	   	if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y")){
	 	   	//For CFM Added Method for getting category and Product items	   	
	 	   		if(CommonDBQuery.getSystemParamtersList().get("ENABLE_CATEGORY_LEVEL_ITEMS")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_CATEGORY_LEVEL_ITEMS").trim().equalsIgnoreCase("Y"))
					   	{
	 	   				if(CommonUtility.validateString(sortBy).length()==0){
	 			    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_NAVIGATION_SORT_BY")).length()>0){
	 			    		sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_NAVIGATION_SORT_BY").trim().toLowerCase();
	 			    	}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_SEARCH_SORT_BY")).length()>0){
	 			    		sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_SEARCH_SORT_BY").trim().toLowerCase();
	 			    		}
	 	   				}
					   		fromRow =  (noOfPage-1)*resultPerPage;
					   		categoryData = ProductHunterSolr.taxonomyListCategory("", subsetId, generalSubset, fromRow, resultPerPage, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,0,"",0,"");
					   	}
					   	else
					   	{
					 	    if(CommonUtility.validateString(sortBy).length()==0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CATEGORY_SORT_BY")).length()>0){
								sortBy = CommonDBQuery.getSystemParamtersList().get("CATEGORY_SORT_BY");
								sortByfromSysParm = true;
							}
					 	   if(CommonUtility.validateString(sortBy).length()==0){
						    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_NAVIGATION_SORT_BY")).length()>0){
						    		sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_NAVIGATION_SORT_BY").trim().toLowerCase();
						    	}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_SEARCH_SORT_BY")).length()>0){
						    		sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_SEARCH_SORT_BY").trim().toLowerCase();
						    	}
						    }
					   		
					   	   categoryData = ProductHunterSolr.taxonomyList("", subsetId, generalSubset, 1, 100, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,0,"",0,"");
					   	}
	 	   	
	 	   		taxonomyLevelFilterData = categoryData.get("categeoryList");
	 	   		attrFilterData = categoryData.get("attrList");
	 	   		itemLevelFilterData = categoryData.get("itemLevelFilterData");
			 	   if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
			 	   		if(itemLevelFilterData.get(0).getItemResultCount()>0){
						resultCount = itemLevelFilterData.get(0).getItemResultCount();
					}else{
						resultCount = itemLevelFilterData.get(0).getResultCount();
					}
			 	  
			 	   	double disp;
			 	    double disp1;
			   		disp = resultCount;
			
			   		if (resultCount > resultPerPage) {
			
			   		paginate = (resultCount / resultPerPage);
			   		disp1 = (disp / resultPerPage);
			
			   		} else {
			   		paginate = (resultCount / resultPerPage);
			   		disp1 = (resultCount / resultPerPage);
			   		}
			
				   	if (disp1 > paginate) {
				   		paginate = paginate + 1;
				
				   	} 
			   	}
	 	   	}else{
		 	 	 navigationResultList = ProductsDAO.searchNavigation("", subsetId, generalSubset, 1, 10, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,false,userId,"",viewFrequentlyPurcahsedOnly);
		 	 	 //taxonomyLevelFilterData = ProductsDAO.taxonomyLevelFilter(taxonomyLevelNo, taxonomyId);
		 	 	 taxonomyLevelFilterData = navigationResultList.get("categoryList");
	 	   	}
	 	   	if(sortByfromSysParm){
	 	   		sortBy = null;
	 	   	}
	 	   	if(taxonomyLevelFilterData!=null && taxonomyLevelFilterData.size()>0){
		    	while(taxonomyLevelFilterData!=null && taxonomyLevelFilterData.size()==1){
		    		taxonomyLevelNo = Integer.toString(taxonomyLevelFilterData.get(0).getLevelNumber());
		    		taxonomyId = taxonomyLevelFilterData.get(0).getCategoryCode();
		    		codeId = taxonomyId;
		    		levelNo = taxonomyLevelNo;
		    		codeIdSkip = codeId;
		    		levelNoSkip = levelNo;
		    		setCodeId(taxonomyId);
		     	   if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y")){
		     		  categoryData = ProductHunterSolr.taxonomyList("", subsetId, generalSubset, 1, 100, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,0,"",0,"");
		     		  taxonomyLevelFilterData = categoryData.get("categeoryList");
		     		  if(attrFilterData==null) {
		     			 attrFilterData = categoryData.get("attrList");
		     		  }
		     	   }else{
		     			navigationResultList = ProductsDAO.searchNavigation("", subsetId, generalSubset, 1, 10, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyLevelFilterData.get(0).getCategoryCode()), taxonomyLevelFilterData.get(0).getLevelNumber(),attrFilterList,brandId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),defaultShiptoId,false,userId,"",viewFrequentlyPurcahsedOnly);
		     			taxonomyLevelFilterData=navigationResultList.get("categoryList");
		     	   }
		    	}
		    	if(taxonomyLevelFilterData!=null && taxonomyLevelFilterData.size()>0){
		    		//topBanners=ProductHunterSolr.getBannerList("TOP",codeId);
	    	 	  /* 	leftBanners=ProductHunterSolr.getBannerList("LEFT",codeId);
	    	 	   	rightBanners=ProductHunterSolr.getBannerList("RIGHT",codeId);
	    	 	   	bottomBanners=ProductHunterSolr.getBannerList("BOTTOM",codeId);*/
		    		if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y")){
		    			breadCrumbList = ProductHunterSolr.breadCrumbList(subsetId, generalSubset, CommonUtility.validateNumber(taxonomyId));
			     	}else{
			     		breadCrumbList = ProductsDAO.getBreadCrumb(taxonomyLevelNo, taxonomyId);
		    		}
		    		if(breadCrumbList!=null && breadCrumbList.size()>0){
		    		contentObject.put("pageTitle", breadCrumbList.get(breadCrumbList.size()-1).getCategoryName());
		    		contentObject.put("metaTagString", breadCrumbList.get(breadCrumbList.size()-1).getCategoryName());
		    		}
		    		if(srchTyp!=null){
		    			if(breadCrumbList!=null && breadCrumbList.size()>0){
		 	    			keyWord = "";
		 	    			String c = "";
		 	    			for(ProductsModel bcList:breadCrumbList){
		 	    				keyWord = keyWord + c + bcList.getCategoryName();
		 	    				c = " > ";
		 	    			}
		 	    		}
		 	    		 ProductsDAO.writePopularSearchWord(keyWord, userId, subsetId, taxonomyLevelFilterData.size(), srchTyp);
		    		 }
		    		categoryDescription =new ProductsModel();
		    		if(CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR").trim().equalsIgnoreCase("Y")){
		    			categoryDescription = ProductHunterSolr.getCategoryDescription(codeId);
		    			categoryBannersList = ProductHunterSolr.getBannerList(codeId);
		    		}else{
		    			categoryDescription = ProductsDAO.getCategoryDescription(codeId);
		    			categoryBannersList = ProductsDAO.getCategoryBannersV2(CommonUtility.validateNumber(codeId));
		    		}
		    		
		    		if(categoryBannersList!=null) {
		    			topBanners = categoryBannersList.get("top");
		    	 	   	leftBanners = categoryBannersList.get("left");
		    	 	   	rightBanners = categoryBannersList.get("right");
		    	 	   	bottomBanners = categoryBannersList.get("bottom");
		    		}
	    			if(CommonUtility.validateNumber(pageNo)<1 && categoryDescription!=null && CommonUtility.validateNumber(CommonUtility.validateString(categoryDescription.getStaticPageId()))>0) {
    					String staticPageByIdUrl = context+staticPage+find+CommonUtility.validateNumber(CommonUtility.validateString(categoryDescription.getStaticPageId()));
    					Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(staticPageByIdUrl, "GET", null, StaticPageData.class);
    					if(response!=null && response.getStatus().getCode()==200) {
    						StaticPageData staticPageDataModel = (StaticPageData) response.getData();
    						if(staticPageDataModel!=null && (staticPageDataModel.getStatus().equalsIgnoreCase("Y") || staticPageDataModel.getStatus().equalsIgnoreCase("A"))) {
    							contentObject.put("assignedStaticPageId", staticPageDataModel.getId());
        						contentObject.put("assignedStaticPageContent", staticPageDataModel.getPageContent());
        						//System.out.println(staticPageDataModel.getPageContent());
							}
    					}
	    			}
	    	 	   	
	    	 	    contentObject.put("paginate", paginate);
	    	 	    contentObject.put("pageNo", pageNo);
	    	 	    contentObject.put("fromRow", fromRow);
	    	 	    contentObject.put("toRow", toRow);
	    	 	    contentObject.put("resultPage", resultPage);
		    		contentObject.put("codeId", codeId);
		    		contentObject.put("codeIdSkip", codeIdSkip);
		    		contentObject.put("levelNo", levelNo);
		    		contentObject.put("levelNoSkip", levelNoSkip);
		    		contentObject.put("categoryId", codeIdSkip);
		    		contentObject.put("gallery", gallery);
		    		contentObject.put("breadCrumbList", breadCrumbList);
		    		contentObject.put("categoryListData", taxonomyLevelFilterData);
		    		contentObject.put("topBanners", topBanners);
		    		contentObject.put("leftBanners", leftBanners);
		    		contentObject.put("rightBanners", rightBanners);
		    		contentObject.put("bottomBanners", bottomBanners);
		    		contentObject.put("attrFilterData", attrFilterData);
		    		contentObject.put("categoryDescription", categoryDescription);
		    		contentObject.put("metaDescription", categoryDescription);
		    		contentObject.put("navigationType", "NAVIGATION");
		    		contentObject.put("sortBy", sortBy);
		    		contentObject.put("itemLevelFilterData", itemLevelFilterData);
		    		contentObject.put("resultPage", resultPage);
		    		contentObject.put("resultCount", resultCount);
		    		
		    		/*
		    		 contentObject.put("filterList", filterList);
			 	     contentObject.put("attrFilterList", attrFilterList);*/
		 	    	/*contentObject.put("vpsid", vpsid);
		 	    	contentObject.put("varPsid", varPsid);
		 	    	contentObject.put("homeTeritory", homeTeritory);
		 	    	contentObject.put("eclipseErrorMassage", eclipseErrorMassage);
		 	    	contentObject.put("keyWordTxt", keyWordTxt);
		 	    	contentObject.put("categoryFilterData", categoryFilterData);
		 		    contentObject.put("attrFilteredList", attrFilteredList);
		 	    	contentObject.put("keyWord", keyWord);
		 	    	contentObject.put("resultCount", resultCount);
		 	    	contentObject.put("srchTyp", srchTyp);
		 	    	contentObject.put("paginate", paginate);*/
		    		if(CommonUtility.validateString(reqType).equalsIgnoreCase("loadPage")){
		    	    	contentObject.put("responseType", "loadPage");
		    	    	renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		    		}
		    		else{
			    		renderContent = LayoutGenerator.templateLoader("SubCategoryPage", contentObject , null, null, null);
		    		}
		    		target=SUCCESS;
		    	}else{
		    		
		          	 target=itemLevelFilter();
		    	}
		    }else{
	       	 target=itemLevelFilter();
	        }
	 	     
		    if(target.equalsIgnoreCase(SUCCESS))
		    {
		    	 if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y") && siteSearch!=null && siteSearch.trim().equalsIgnoreCase("Y") && CommonDBQuery.getSystemParamtersList().get("ENABLE_STATIC_SEARCH")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_STATIC_SEARCH").trim().equalsIgnoreCase("Y"))
			 	   {
		    		 for(ProductsModel tName:breadCrumbList)
		    		 {
		    			 keyWord = tName.getCategoryName();
		    		 }
			    	eventSearchData = new ArrayList<ProductsModel>();
			    	staticContentData = new ArrayList<ProductsModel>();
			    	staticContentData = ProductHunterSolr.SearchStaticContent(keyWord, "",0,2);
			    	eventSearchData  = ProductHunterSolr.SearchStaticContent(keyWord, "Event",0,2);
			    	contentObject.put("eventSearchData", eventSearchData);
	 	    		contentObject.put("staticContentData", staticContentData);
	 	    		if(CommonUtility.validateString(reqType).equalsIgnoreCase("loadPage")){
		    	    	contentObject.put("responseType", "loadPage");
		    	    	renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		    		}
		    		else{
			    		renderContent = LayoutGenerator.templateLoader("SubCategoryPage", contentObject , null, null, null);
		    		}
			 	   }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return target;
	}
	
	public String search(){
		long startTimer = CommonUtility.startTimeDispaly();
		target = NONE;
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			ArrayList<ProductsModel> blogContent = null;
			String pageClick = request.getParameter("pageClick");
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("bazaarVoiceLoader", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BAZAARVOICE_LOADER")));
			String changeRequestHandler = (String)CommonDBQuery.getSystemParamtersList().get("CHANGE_REQUEST_HANDLER");
			//renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
			
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String defaultShiptoId = (String) session.getAttribute("defaultShipToId");
			String entityId = (String) session.getAttribute("entityId");
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			fromProductMode = "Y";
			/*String wareHousecode = (String) session.getAttribute("wareHouseCode");
			String customerId = (String) session.getAttribute("customerId");
			String customerCountry = (String) session.getAttribute("customerCountry");*/
				
			
			
			
			if(request.getParameter("isPreviouslyPurchased")!=null && request.getParameter("isPreviouslyPurchased").equalsIgnoreCase("Y"))
			{
				viewFrequentlyPurcahsedOnly = "Y";
			}
			/**
			 *Below code Written is for Tyndale *Reference- Chetan Sandesh
			 */
			if(CommonUtility.validateString(request.getParameter("clearanceItems")).equalsIgnoreCase("Y"))
			{
				clearanceFlag = "Y";
			}
			if(CommonUtility.validateString(request.getParameter("wareHouseItems")).length()>0)
			{
				contentObject.put("wareHouseItems", CommonUtility.validateString(request.getParameter("wareHouseItems")));
				contentObject.put("shipToMe", CommonUtility.validateString(request.getParameter("shipToMe")));
				if(CommonUtility.validateString(request.getParameter("shipToMe")).equalsIgnoreCase("Y")) {
					wareHouseItems = CommonUtility.validateString(request.getParameter("wareHouseItems"))+" AND id:SHIPTOME_*";
				}else if(CommonUtility.validateString(request.getParameter("shipToMe")).equalsIgnoreCase("N")) {
					wareHouseItems = CommonUtility.validateString(request.getParameter("wareHouseItems"))+" AND id:SHIPTOSTORE_*) OR (warehouseCode:NONORGILLSHIPTOSTORE AND id:NONORGILLSHIPTOSTORE_*";
				}else {
					wareHouseItems = CommonUtility.validateString(request.getParameter("wareHouseItems"));
				}
				contentObject.put("wareHouseItemsFlag", "Y");
			}
			else if(CommonUtility.validateString((String) session.getAttribute("wareHouseItems")).length()>0){
				wareHouseItems = CommonUtility.validateString((String) session.getAttribute("wareHouseItems"));
			}
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = 0;
			String extSearch = request.getParameter("extSearch");
			contentObject.put("extSearch", CommonUtility.validateString(extSearch));
			if (extSearch!=null && CommonUtility.validateString(extSearch).equalsIgnoreCase("Y")){
		    	subsetId = ProductsDAO.getSubsetIdFromName(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME")));
		    }else{
		    	subsetId = Integer.parseInt(tempSubset);	
		    }
			//int resultPerPage = CommonUtility.validateNumber(resultPage);
			int resultPerPage=0;
			if(resultPage!=null && CommonUtility.validateNumber(resultPage) > 0){
				resultPerPage = CommonUtility.validateNumber(resultPage);
			}else{
				resultPerPage = CommonUtility.validateNumber((String)session.getAttribute("resultPage"));
				resultPage = (String)session.getAttribute("resultPage");
			}
			
		    String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			if(pageViewMode!=null){
				session.setAttribute("pageViewMode", pageViewMode);
			}
			
			if(wareHousecode!=null && wareHousecode.trim().length()>0){
				contentObject.put("branchName",UsersDAO.getShipBranchName(wareHousecode));
			}
			
		    //eclipseErrorMassage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNMESSAGE");
		    String requestType = "SEARCH";
		    navigationType = "SEARCH";
		    ProductsModel categorySearch = new ProductsModel();
		    int treeId = 0;
		    filterList = attrFilterList; 
		    if(filterList!=null && !filterList.trim().equalsIgnoreCase(""))
		    {
		    	try {
					filterList = URLEncoder.encode(filterList,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				}
		    }
		    
		    //To avoid XSS injection - Security_QA
	    	keyWord = CommonUtility.sanitizeHtml(keyWord);
	    	codeId = CommonUtility.sanitizeHtml(codeId);
	    	levelNo = CommonUtility.sanitizeHtml(levelNo);
		    
		    String buildKeyWord = "";
		    if(CommonUtility.validateString(keyWord).length()>0) {
		    	buildKeyWord = keyWord;
		    }
		    
		    if(CommonUtility.validateString(changeRequestHandler).trim().equalsIgnoreCase("Y")){
		    	narrowKeyword = keyWordTxt;
		    }else{
		    	if(keyWordTxt!=null && !keyWordTxt.trim().equalsIgnoreCase("")){
			    	buildKeyWord = buildKeyWord +" " + keyWordTxt;
			    	narrowKeyword = keyWordTxt;
			    }
		    }
		    
		    if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRETTY_SEARCH_ENABLED")).equalsIgnoreCase("Y")){
				contentObject.put(PrettySearchConstantVariables.keywordKey,keyWord);
				contentObject.put(PrettySearchConstantVariables.SUBSET_KEY,CommonUtility.validateNumber(tempSubset));
				contentObject.put(PrettySearchConstantVariables.GENERAL_SUBSET_KEY,CommonUtility.validateNumber(tempGeneralSubset));
				contentObject = PrettySearchDAO.getInstance().getPrettySearchData(contentObject);
			}
		    if(srchTyp == null)
		    	srchTyp = "0";
		    if(srchTyp.trim().equalsIgnoreCase("1")){
		    	requestType = "SEARCH-AS01";
		    }else  if(srchTyp.trim().equalsIgnoreCase("2")){
		    	requestType = "SEARCH-AS02";
		    }else  if(srchTyp.trim().equalsIgnoreCase("3")){
		    	requestType = "SEARCH-AS03";
		    }else  if(srchTyp.trim().equalsIgnoreCase("4")){
		    	requestType = "SEARCH-AS04";
		    }else  if(srchTyp.trim().equalsIgnoreCase("5")){
		    	requestType = "SEARCH-AS05";
		    }else  if(srchTyp.trim().equalsIgnoreCase("6")){
		    	requestType = "SEARCH-AS06";
		    }else  if(srchTyp.trim().equalsIgnoreCase("7")){
		    	requestType = "SEARCH-AS07";
		    }else  if(srchTyp.trim().equalsIgnoreCase("8")){
		    	requestType = "SEARCH-AS08";
		    }else  if(srchTyp.trim().equalsIgnoreCase("9")){
		    	requestType = "SEARCH-AS09";
		    }else  if(srchTyp.trim().equalsIgnoreCase("10")){
		    	requestType = "SEARCH-AS10";
		    }else{
		    	srchTyp = "0";
		    	requestType = "SEARCH";
		    }
		  
		    if(resultPerPage==0)
		    {
		    	if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
		    		resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
			    	resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
		    	}else{
		    		resultPage = "12";
			    	resultPerPage = 12;
		    	}
		    }
		    session.setAttribute("resultPage", resultPage);
	
		    
		    if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")).equalsIgnoreCase("Y"))
		 	   {
		    	eventSearchData = new ArrayList<ProductsModel>();
		    	staticContentData = new ArrayList<ProductsModel>();
		    	numberOfStaticResult = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("DEFAULT_NUMBER_OF_STATIC_RESULT"));
		    	if(numberOfStaticResult<1){
		    		numberOfStaticResult = 2;
		    	}
		    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_STATIC_SEARCH")).equalsIgnoreCase("Y")){
		    	staticContentData = ProductHunterSolr.SearchStaticContent(keyWord, "",0,numberOfStaticResult);
		    	eventSearchData  = ProductHunterSolr.SearchStaticContent(keyWord, "Event",0,numberOfStaticResult);
		    	contentObject.put("eventSearchData", eventSearchData);
		    	contentObject.put("staticContentData", staticContentData);
		    	}
		    	
		    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_BLOG_SEARCH")).equalsIgnoreCase("Y")) {
		    		blogContent = ProductHunterSolrUltimate.SearchBlogContent(keyWord,0,5);
		    	}
		    	categorySearch = ProductHunterSolr.searchCategory(keyWord, subsetId, generalSubset);
		    	if(categorySearch!=null)
		    	{
		    		if(categorySearch.getCategoryCode()!=null && !categorySearch.getCategoryCode().trim().equalsIgnoreCase(""))
		    		{
		    			levelNo = Integer.toString(categorySearch.getLevelNumber());
			    		treeId = CommonUtility.validateNumber(categorySearch.getCategoryCode());
		    		}
		    		
		    	}
		    	else
		    	{
		    		levelNo = "0";
		    	}
		 	   }
		    if(CommonUtility.validateString(sortBy).length()==0 &&CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_SEARCH_SORT_BY")).length()>0){
		    	sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_SEARCH_SORT_BY").trim().toLowerCase();
		    }
		    
		    if(CommonUtility.validateString(request.getParameter("overRideCatId")).length()>0){
		    	overRideCatId = CommonUtility.validateString(request.getParameter("overRideCatId"));
		    }
		    
		    if(CommonUtility.customServiceUtility()!=null)
			{
		    	overRideCatId =CommonUtility.customServiceUtility().setOverRideCatId(overRideCatId);
		    			
			}	
		    if(CommonUtility.validateString(overRideCatId).equalsIgnoreCase("N")){
		    	overRideSearchByCategoryFlag = false; 
		    }
		    
		    if(treeId>0 && extSearch==null && overRideSearchByCategoryFlag)
		    {
	
		    	codeId = Integer.toString(treeId);
		    	String taxonomyId=codeId;
		    	String taxonomyLevelNo=levelNo;
		    	codeIdSkip = codeId;
		    	levelNoSkip = levelNo;
		    	taxonomyLevelFilterData = new ArrayList<ProductsModel>();	
		    	//eclipseErrorMassage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNMESSAGE");
	
		    	HashMap<String, ArrayList<ProductsModel>> navigationResultList = new HashMap<String, ArrayList<ProductsModel>>();
		    	if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y")){
	
		    		HashMap<String, ArrayList<ProductsModel>> categoryData = ProductHunterSolr.taxonomyList("", subsetId, generalSubset, 1, 10, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,0,"",0,"");
		    		taxonomyLevelFilterData = categoryData.get("categeoryList");
		    		attrFilterData = categoryData.get("attrList");
		    	}else{
	
		    		navigationResultList = ProductsDAO.searchNavigation("", subsetId, generalSubset, 1, 10, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,false,userId,"",viewFrequentlyPurcahsedOnly);
	
	
		    		//	 	 	    taxonomyLevelFilterData = ProductsDAO.taxonomyLevelFilter(taxonomyLevelNo, taxonomyId);
		    		taxonomyLevelFilterData = navigationResultList.get("categoryList");
		    	}
		    	if(taxonomyLevelFilterData!=null && taxonomyLevelFilterData.size()>0)
		    	{
		    		while(taxonomyLevelFilterData!=null && taxonomyLevelFilterData.size()==1)
		    		{
		    			taxonomyLevelNo = Integer.toString(taxonomyLevelFilterData.get(0).getLevelNumber());
		    			taxonomyId = taxonomyLevelFilterData.get(0).getCategoryCode();
		    			codeId = taxonomyId;
		    			levelNo = taxonomyLevelNo;
		    			codeIdSkip = codeId;
		    			levelNoSkip = levelNo;
		    			setCodeId(taxonomyId);
	
		    			if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y"))
		    			{
		    				HashMap<String, ArrayList<ProductsModel>> categoryData = ProductHunterSolr.taxonomyList("", subsetId, generalSubset, 1, 10, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyId), CommonUtility.validateNumber(levelNo),attrFilterList,brandId,session.getId(),sortBy,0,"",0,"");
		    				taxonomyLevelFilterData = categoryData.get("categeoryList");
		    				attrFilterData = categoryData.get("attrList");
		    			}
		    			else
		    			{
		    				navigationResultList = ProductsDAO.searchNavigation("", subsetId, generalSubset, 1, 10, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber(taxonomyLevelFilterData.get(0).getCategoryCode()), taxonomyLevelFilterData.get(0).getLevelNumber(),attrFilterList,brandId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),defaultShiptoId,false,userId,"",viewFrequentlyPurcahsedOnly);
		    				taxonomyLevelFilterData=navigationResultList.get("categoryList");
		    			}
	
	
		    		}
		    		if(taxonomyLevelFilterData!=null && taxonomyLevelFilterData.size()>0)
		    		{
	
		    			categoryDescription =new ProductsModel();
		    			if(CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR")!=null && CommonDBQuery.getSystemParamtersList().get("GET_DATA_FROM_SOLR").trim().equalsIgnoreCase("Y")){
		    				categoryDescription = ProductHunterSolr.getCategoryDescription(codeId);
		    				categoryBannersList = ProductHunterSolr.getBannerList(codeId);
		    			}else{
		    				categoryDescription = ProductsDAO.getCategoryDescription(codeId);
		    				//categoryBannersList = ProductsDAO.getCategoryBanners(codeId);
		    				categoryBannersList = ProductsDAO.getCategoryBannersV2(CommonUtility.validateNumber(codeId));
		    			}
		    			topBanners=categoryBannersList.get("top");
		    			leftBanners=categoryBannersList.get("left");
		    			rightBanners=categoryBannersList.get("right");
		    			bottomBanners=categoryBannersList.get("bottom");
		    			/*   	leftBanners=ProductHunterSolr.getBannerList("LEFT",codeId);
		    	 	   	rightBanners=ProductHunterSolr.getBannerList("RIGHT",codeId);
		    	 	   	bottomBanners=ProductHunterSolr.getBannerList("BOTTOM",codeId);*/
	
		    			if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y")){
		    				breadCrumbList = ProductHunterSolr.breadCrumbList(subsetId, generalSubset, CommonUtility.validateNumber(taxonomyId));
		    			}else{
		    				breadCrumbList = ProductsDAO.getBreadCrumb(taxonomyLevelNo, taxonomyId);
		    			}
		    			contentObject.put("codeId", codeId);
		    			contentObject.put("levelNo", levelNo);
		    			contentObject.put("categoryId", codeIdSkip);
		    			contentObject.put("gallery", gallery);
		    			contentObject.put("pageNo", pageNo);
		    			contentObject.put("breadCrumbList", breadCrumbList);
		    			contentObject.put("categoryListData", taxonomyLevelFilterData);
		    			contentObject.put("topBanners", topBanners);
		    			contentObject.put("leftBanners", leftBanners);
		    			contentObject.put("rightBanners", rightBanners);
		    			contentObject.put("bottomBanners", bottomBanners);
		    			contentObject.put("attrFilterData", attrFilterData);
		    			contentObject.put("blogContent",blogContent);
		    			contentObject.put("categoryDescription", categoryDescription);
		    			renderContent = LayoutGenerator.templateLoader("SubCategoryPage", contentObject , null, null, null);
		    			target=SUCCESS;
		    		}else{
		    			target=itemLevelFilter();
		    		}
		    	}else{
		    		target=itemLevelFilter();	
		    	}
		    }else{
		    	 int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
		 	    int fromRow =  (noOfPage-1)*resultPerPage + 1;
		 		int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
		 		String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		 		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
		 		HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
		 		 if(attrFilterList!=null && attrFilterList.trim().equalsIgnoreCase(""))
		 	    	attrFilterList = null;
		  	   if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y"))
		  	   {
		  		 homeTeritory = (String) session.getAttribute("shipBranchId");
		  		  fromRow =  (noOfPage-1)*resultPerPage;
		  		if(CommonDBQuery.getSystemParamtersList().get("NRT")!=null && CommonDBQuery.getSystemParamtersList().get("NRT").trim().equalsIgnoreCase("Y"))
			  	{
		  			searchResultList = ProductHunterSolrV2.searchNavigation(null, buildKeyWord, subsetId, generalSubset, fromRow, toRow, requestType, CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"),  0, attrFilterList, brandId, sortBy, resultPerPage, narrowKeyword, buyingCompanyId, userId, false, true, viewFrequentlyPurcahsedOnly);
			  	}else{
			  		 searchResultList = ProductHunterSolr.searchNavigation(buildKeyWord, subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","", "","",false,true,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHouseItems);//wareHousecode, customerId,customerCountry
			  	}
		  		  	
		  		  ArrayList<ProductsModel> itemSugData = searchResultList.get("itemSuggest");
			  		if(itemSugData!=null && itemSugData.size()>0)
			  		{
			  			itemSuggest = itemSugData.get(0).getSuggestedValue();
			  			contentObject.put("itemSuggest", itemSuggest);
			  		}
			  		
			  	  ArrayList<ProductsModel> itemSuggestList = searchResultList.get("itemSuggestValueList");
			  		if(itemSuggestList!=null && itemSuggestList.size()>0){
			  			contentObject.put("itemSuggestList", itemSuggestList.get(0).getSuggestedValueList());
			  		}
		  		
		  		
		  	   }
		  	   else
		  	   {
		  		  if(vpsid!=null && !vpsid.trim().equalsIgnoreCase("") && !vpsid.trim().equalsIgnoreCase("0"))
		  				varPsid = CommonUtility.validateNumber(vpsid);
		  			
		  			
		  		  
		  			if(varPsid==0)
		  				searchResultList = ProductsDAO.searchNavigation(keyWord, subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,true,userId,"",viewFrequentlyPurcahsedOnly);
		  			else
		  				searchResultList = ProductsDAO.searchNavigation(narrowKeyword, subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,true,userId,"",viewFrequentlyPurcahsedOnly);
		  	   }
		 		
		 		
		 		itemLevelFilterData = searchResultList.get("itemList");
		 		attrFilterData = searchResultList.get("attrList");
		 		
		 		
		 		
		 		if(itemLevelFilterData!=null && itemLevelFilterData.size()>0)
		 		{
		 			if(varPsid==0)
		 			varPsid = itemLevelFilterData.get(0).getProductSeardId();
		 		}else{
		 			ArrayList<ProductsModel> itemSuggestList = searchResultList.get("itemSuggestValueList");
			  		if(CommonUtility.validateString(itemSuggest).length()>0) {
			  			buildKeyWord = CommonUtility.validateString(itemSuggest);
			  		}else if(itemSuggestList!=null && itemSuggestList.size()>0){
			  			contentObject.put("itemSuggestList", itemSuggestList.get(0).getSuggestedValueList());
			  			if(itemSuggestList.get(0).getSuggestedValueList()!=null && itemSuggestList.get(0).getSuggestedValueList().size() > 0){
				  			buildKeyWord = itemSuggestList.get(0).getSuggestedValueList().get(0);
				  		}
			  		}
			  		
			  		if(CommonDBQuery.getSystemParamtersList().get("NRT")!=null && CommonDBQuery.getSystemParamtersList().get("NRT").trim().equalsIgnoreCase("Y"))
				  	{
		  				searchResultList = ProductHunterSolrV2.searchNavigation(null, buildKeyWord, subsetId, generalSubset, fromRow, toRow, requestType, CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"),  0, attrFilterList, brandId, sortBy, resultPerPage, narrowKeyword, buyingCompanyId, userId, false, true, viewFrequentlyPurcahsedOnly);
				  	}else{
				  		searchResultList = ProductHunterSolr.searchNavigation(buildKeyWord, subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","", "","",false,true,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHouseItems);//wareHousecode, customerId,customerCountry
				  	}
		 			itemLevelFilterData = searchResultList.get("itemList");
			 		attrFilterData = searchResultList.get("attrList");
			 		if(itemLevelFilterData!=null && itemLevelFilterData.size() > 1){
			 			String validateForSearch[] = buildKeyWord.split("\\s+",-1);
			 			String validateForSearchKeyWord[] = keyWord.split("\\s+",-1);
			 			StringBuilder spellCheckString = new StringBuilder();
			 			if(validateForSearch!=null && validateForSearch.length>1 && validateForSearch.length == validateForSearchKeyWord.length){
			 				String c="";
			 				for(int i=0; i<validateForSearch.length; i++) {
			 					if(!validateForSearchKeyWord[i].equalsIgnoreCase(validateForSearch[i])) {
			 						spellCheckString.append(c).append("<span class='spellCheckHighlight'>").append(validateForSearch[i]).append("</span>");
			 					}else {
			 						spellCheckString.append(c).append(validateForSearch[i]);
			 					}
			 					c = " ";
			 				}

			 			}else{
			 				spellCheckString.append("<span class='spellCheckHighlight'>").append(buildKeyWord).append("</span>");
			 			}
				 		contentObject.put("suggestedSearch",true);
				 		contentObject.put("suggestedSearchKeword",spellCheckString.toString());
				 		contentObject.put("buildKeyWord",buildKeyWord);
			 		}else {
			 			itemLevelFilterData = null;
			 		}
			  		
		 		}
		 		categoryFilterData = searchResultList.get("categoryList");
		 		
		 		if(categoryFilterData!=null && categoryFilterData.size()>0)
		 		{
		 			ArrayList<ProductsModel> tempListGen = new ArrayList<ProductsModel>();
		 			
		 			compareList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
		 			for(ProductsModel categList:categoryFilterData)
		 			{
		 				tempListGen = compareList.get(categList.getParentCategory());
		 				ProductsModel temp = new ProductsModel();
		 				
		 				temp.setCategoryCount(categList.getCategoryCount());
		 				temp.setCategoryCode(categList.getCategoryCode());
		 				temp.setCategoryName(categList.getCategoryName());
		 				if(tempListGen==null)
		 				{
		 					tempListGen = new ArrayList<ProductsModel>();
		 					tempListGen.add(temp);
		 				}
		 				else
		 				{
		 					tempListGen.add(temp);
		 				}
		 				compareList.put(categList.getParentCategory(), tempListGen);
		 			}
		 			
		 		}
		 		
		 		 attrFilteredList = searchResultList.get("filteredList");
		 		if(itemLevelFilterData!=null && itemLevelFilterData.size()>0)
		 		{
		 			
		 			metaTagString="";
		 			String s = ",";
		 			for(ProductsModel metaTagKeyword : itemLevelFilterData)
		 			{
		 				metaTagString = metaTagString +s+metaTagKeyword.getPartNumber() +" "+ metaTagKeyword.getManufacturerPartNumber();
		 				s = ",";
		 			}
		 			if(metaTagString!=null&&!metaTagString.trim().equals(""))
		 			{
		 			metaTagString = metaTagString.replaceAll("\"", "");
		 			metaTagString = metaTagString.replaceAll("'", "");
		 			}
		 			
		 			if(itemLevelFilterData.get(0).getResultCount()>0){
		 				resultCount = itemLevelFilterData.get(0).getResultCount();
		 		    }else if(itemLevelFilterData.get(0).getItemResultCount()>0){
		 		    	resultCount = itemLevelFilterData.get(0).getItemResultCount();
		 		      }
		 			//CustomServiceProvider
					if(CommonUtility.customServiceUtility()!=null) {
						int resultCount1 = CommonUtility.customServiceUtility().getItemsResultCount(buildKeyWord, subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","", "","",false,true,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHousecode);
						contentObject.put("resultCount1", resultCount1);
						int resultCount2 = CommonUtility.customServiceUtility().getItemsResultCount(buildKeyWord, subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","", "","",false,true,viewFrequentlyPurcahsedOnly,clearanceFlag,"");
						contentObject.put("resultCount2", resultCount2);
					}
					//CustomServiceProvider
		 			ProductsDAO.writePopularSearchWord(keyWord, userId, subsetId, resultCount, srchTyp);
		 			double disp;
		 			double disp1;
		 	    	disp = resultCount;
	
		 	    	if (resultCount > resultPerPage) {
	
		 	    		paginate = (resultCount / resultPerPage);
		 	    		disp1 = (disp / resultPerPage);
	
		 	    	} else {
		 	    		paginate = (resultCount / resultPerPage);
		 	    		disp1 = (resultCount / resultPerPage);
		 	    	}
	
		 	    	if (disp1 > paginate) {
		 	    		paginate = paginate + 1;
	
		 	    	}
		 	    	
		 	    	try {
			    		if(attrFilterList!=null){
			    			attrFilterList = attrFilterList.replaceAll("% ", "%25 ");
			    			attrFilterList = URLDecoder.decode(attrFilterList,"UTF-8");
			    		    attrFilterList = attrFilterList.replaceAll("'", "%27");
			    		    String paginateFilter = attrFilterList;
			    		    contentObject.put("paginateFilter",paginateFilter);
			    		    attrFilterList = attrFilterList.replaceAll("&", "&amp;");
	
			    		}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		 	    	if(resultCount>1){
		 	    		if(searchResultList.get("facetRanges")!=null && searchResultList.get("facetRanges").size() >0){
		 	    			contentObject.put("facetRanges", searchResultList.get("facetRanges").get(0).getFacetRange());	
		 	    		}
		 	    		if(CommonUtility.validateString(keyWord).length()>0) {
		 			    	keyWord = CommonUtility.sanitizeHtml(keyWord);
		 			    }
		 			    if(CommonUtility.validateString(keyWordTxt).length()>0) {
		 			    	keyWordTxt = CommonUtility.sanitizeHtml(keyWordTxt);
		 			    }
		 				contentObject.put("paginate", paginate);
		 	    		contentObject.put("keyWord", keyWord);
		 	    		contentObject.put("narrowKeyword", narrowKeyword);
		 	    		contentObject.put("srchTyp", srchTyp);
		 	    		contentObject.put("pageNo", pageNo);
		 	    		contentObject.put("categoryId", codeId);
		 		 		contentObject.put("gallery", gallery);
		 		 		contentObject.put("levelNo", levelNoSkip);
		 		 		contentObject.put("navigationType", navigationType);
		 		 		contentObject.put("attrFilterList",attrFilterList);
		 		 		contentObject.put("filterList", filterList);
		 		 		contentObject.put("resultPage", resultPage);
		 		 		contentObject.put("keyWordTxt", keyWordTxt);
		 		 		contentObject.put("resultCount", resultCount);
		 		 		contentObject.put("attrFilterData", attrFilterData);
		 		 		contentObject.put("attrFilteredList", attrFilteredList);
		 	    		contentObject.put("breadCrumbList", breadCrumbList);
		 	    		contentObject.put("topBanners", topBanners); 
		 	    		contentObject.put("itemListData", itemLevelFilterData);
		 	    		contentObject.put("sortBy", sortBy);
		 	    		contentObject.put("viewFrequentlyPurcahsedOnly", viewFrequentlyPurcahsedOnly);
		 	    		contentObject.put("siteShipViaList", CommonDBQuery.getSiteShipViaList());
		 	    		contentObject.put("clearanceItems", clearanceFlag);
		 	    		if(pageViewMode==null){
		 	    			pageViewMode = (String) session.getAttribute("pageViewMode");
		 	    		}
		 	    		contentObject.put("pageViewMode", pageViewMode);
		 	    		if(CommonUtility.validateString(reqType).length() > 0){
		 	    			contentObject.put("responseType", reqType);
		 	    			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		 	    		}else{
		 	    			renderContent = LayoutGenerator.templateLoader("ProductList", contentObject , null, null, null);
		 	    		}
		 	    		target="success";
		 	          	//target="searchResult";
		 	        }else{
		 	   	    	itemPriceId = itemLevelFilterData.get(0).getItemPriceId();
			    		itemUrlSingle = itemLevelFilterData.get(0).getItemUrl();
			    		itemUrlSingle = CommonUtility.getUrlString(itemUrlSingle);
			    		fromProductMode = "N";
			    		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRODUCT_MODE_ENABLE_SEARCH")).equalsIgnoreCase("Y")) {
			    			fromProductMode = "Y";
			    		}
		 	   	    	target="singleItem";
		 	   	    }
		 		}
		 		else
		 		{
		 			ProductsDAO.writePopularSearchWord(keyWord, userId, subsetId, resultCount, srchTyp);
		 			if(itemLevelFilterData!=null){
		 				contentObject.put("itemListData", itemLevelFilterData);
		 			}else{
		 				itemLevelFilterData = new ArrayList<ProductsModel>();
		 				contentObject.put("itemListData", itemLevelFilterData);
		 			}
		 			
		 			if(CommonUtility.validateString(keyWord).length()>0) {
	 			    	keyWord = CommonUtility.sanitizeHtml(keyWord);
	 			    }
	 			    if(CommonUtility.validateString(keyWordTxt).length()>0) {
	 			    	keyWordTxt = CommonUtility.sanitizeHtml(keyWordTxt);
	 			    }
		 			contentObject.put("navigationType", navigationType);
		 			contentObject.put("resultCount", resultCount);
		 			contentObject.put("keyWord", keyWord);
		 			contentObject.put("narrowKeyword", narrowKeyword);
		 			LinkedHashMap<String, Object> contextLeftContent = new LinkedHashMap<String, Object>();
		 			TemplateModel template = null;
		 			String siteName = CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
		 			template =  new TemplateModel();
		 			LinkedHashMap<String, TemplateModel> layoutList = LayoutLoader.getLayoutModel().getLayoutBySite().get(siteName.trim().toUpperCase());
		 			template.setLeftContent(layoutList.get("HomePage").getLeftContent());
		 			contextLeftContent.put("leftContent", template.getLeftContent());
		 			if(CommonUtility.validateString(reqType).length() > 0){
	 	    			contentObject.put("responseType", reqType);
	 	    			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
	 	    		}else{
	 	    			if(itemLevelFilterData != null && itemLevelFilterData.size() <= 0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_NO_RESULTS_PAGE")).equals("Y")){
	 	    				renderContent = LayoutGenerator.templateLoader("NoResultsPage", contentObject , null, null, null);
	 	    			}else{
	 	    				renderContent = LayoutGenerator.templateLoader("ProductList", contentObject , null, null, null);
	 	    			}
	 	    		}
		 			//renderContent = LayoutGenerator.templateLoader("ProductList", contentObject, contextLeftContent, null, null);
		 			target="success";
		 		}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return target;
	}
	
	public String staticSearch(){
		target = SUCCESS;
		try{
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		    int resultPerPage = CommonUtility.validateNumber(resultPage);
		    if(filterList!=null && !filterList.trim().equalsIgnoreCase(""))
		    {
		    	try {
					filterList = URLEncoder.encode(filterList,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    if(srchTyp == null)
		    	srchTyp = "";
		    if(srchTyp.trim().equalsIgnoreCase("Event"))
		    	srchTyp = "Event";
		    else
		    	srchTyp = "";
		   
		  
		    if(resultPerPage==0)
		    {
		    	if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
		    		resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
			    	resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
		    	}else{
		    		resultPage = "12";
			    	resultPerPage = 12;
		    	}
		    }
		    int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
	 	    int fromRow =  (noOfPage-1)*resultPerPage;
		    if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y"))
		 	   {
		    	itemLevelFilterData = new ArrayList<ProductsModel>();
		    	itemLevelFilterData = ProductHunterSolr.SearchStaticContent(keyWord, srchTyp,fromRow,resultPerPage);
	
		    	
		    	if(itemLevelFilterData!=null && itemLevelFilterData.size()>0)
		 		{
		 			resultCount = itemLevelFilterData.get(0).getResultCount();
		 			double disp;
		 			double disp1;
		 	    	disp = resultCount;
	
		 	    	if (resultCount > resultPerPage) {
	
		 	    		paginate = (resultCount / resultPerPage);
		 	    		disp1 = (disp / resultPerPage);
	
		 	    	} else {
		 	    		paginate = (resultCount / resultPerPage);
		 	    		disp1 = (resultCount / resultPerPage);
		 	    	}
	
		 	    	if (disp1 > paginate) {
		 	    		paginate = paginate + 1;
	
		 	    	}
		 	   
		 		}
		 	   }
		    
		    contentObject.put("paginate", paginate);
	 		contentObject.put("keyWord", keyWord);
	 		contentObject.put("srchTyp", srchTyp);
	 		contentObject.put("pageNo", pageNo);
	 		contentObject.put("resultPage", resultPage);
	 		contentObject.put("resultCount", resultCount);
	 		contentObject.put("breadCrumbList", breadCrumbList);
	 		contentObject.put("staticListData", itemLevelFilterData);
	 		renderContent = LayoutGenerator.templateLoader("StaticSearchPage", contentObject , null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
 		return target;
	}

	public String shopDirectFromManufacturer(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
		   
			session.setAttribute("DFMMode", "Y");
			if(CommonUtility.validateNumber(manfId)>0){
				System.out.println("DFM_MIN_THRESHOLD_TYPE : "+CommonDBQuery.manfCustomField.get(CommonUtility.validateNumber(manfId)+"_DFM_MIN_THRESHOLD_TYPE"));
				System.out.println("MINIMUM_THRESHOLD_LIMIT : "+CommonDBQuery.manfCustomField.get(CommonUtility.validateNumber(manfId)+"_MINIMUM_THRESHOLD_LIMIT"));
				System.out.println("DFM_MAX_THRESHOLD_TYPE : "+CommonDBQuery.manfCustomField.get(CommonUtility.validateNumber(manfId)+"_DFM_MAX_THRESHOLD_TYPE"));
				System.out.println("MAXIMUM_THRESHOLD_LIMIT :"+CommonDBQuery.manfCustomField.get(CommonUtility.validateNumber(manfId)+"_MAXIMUM_THRESHOLD_LIMIT"));
				System.out.println("SHIPPING_AND_HANDLING_FEE :"+CommonDBQuery.manfCustomField.get(CommonUtility.validateNumber(manfId)+"_SHIPPING_AND_HANDLING_FEE"));
				System.out.println("DFM_SUBSET_ID :"+CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID")));
				
				session.setAttribute("DFM_MIN_THRESHOLD_TYPE", CommonDBQuery.manfCustomField.get(CommonUtility.validateNumber(manfId)+"_DFM_MIN_THRESHOLD_TYPE"));
				session.setAttribute("MINIMUM_THRESHOLD_LIMIT", CommonDBQuery.manfCustomField.get(CommonUtility.validateNumber(manfId)+"_MINIMUM_THRESHOLD_LIMIT"));
				session.setAttribute("DFM_MAX_THRESHOLD_TYPE", CommonDBQuery.manfCustomField.get(CommonUtility.validateNumber(manfId)+"_DFM_MAX_THRESHOLD_TYPE"));
				session.setAttribute("MAXIMUM_THRESHOLD_LIMIT", CommonDBQuery.manfCustomField.get(CommonUtility.validateNumber(manfId)+"_MAXIMUM_THRESHOLD_LIMIT"));
				session.setAttribute("DFM_SHIPPING_AND_HANDLING_FEE", CommonDBQuery.manfCustomField.get(CommonUtility.validateNumber(manfId)+"_SHIPPING_AND_HANDLING_FEE"));
				session.setAttribute("DFM_SUBSET_ID", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID")));
				session.setAttribute("currentManufacturerId", manfId);
							
				session.setAttribute("PREVIOUS_USER_SUBSET_ID", CommonUtility.validateString((String) session.getAttribute("userSubsetId")));
				System.out.println("Actual User Subset before Switch :"+CommonUtility.validateString((String) session.getAttribute("userSubsetId")));
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID")).length()>0 && CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID"))>0){
					session.setAttribute("userSubsetId", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID")));
					session.setAttribute("DFM_SUBSET_ID", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DFM_SUBSET_ID")));
				}
				System.out.println("Actual User Subset After Switch : "+CommonUtility.validateString((String) session.getAttribute("userSubsetId")));
				System.out.println("PREVIOUS_USER_SUBSET_ID Subset After Switch : "+CommonUtility.validateString((String) session.getAttribute("PREVIOUS_USER_SUBSET_ID")));
			}
			
			    String tempSubset = (String) session.getAttribute("userSubsetId");
			    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
				int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			    int resultPerPage = CommonUtility.validateNumber(resultPage);
			    String defaultShiptoId = (String) session.getAttribute("defaultShipToId");
				String wareHousecode = (String) session.getAttribute("wareHouseCode");
				String customerId = (String) session.getAttribute("customerId");
				String customerCountry = (String) session.getAttribute("customerCountry");
			    String entityId = (String) session.getAttribute("entityId");
			    String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
				int userId = CommonUtility.validateNumber(sessionUserId);
	
			if(pageViewMode!=null){
				session.setAttribute("pageViewMode", pageViewMode);
			}
			
		    navigationType = "DFMANF"; 
		    filterList = attrFilterList; 
		    if(filterList!=null && !filterList.trim().equalsIgnoreCase(""))
		    {
		    	try {
					filterList = URLEncoder.encode(filterList,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				}
		    }
		    if(resultPerPage==0)
		    {
		    	if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
		    		resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
			    	resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
		    	}else{
		    		resultPage = "12";
			    	resultPerPage = 12;
		    	}
		    }
		    System.out.println(attrFilterList);
		    int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
		    int subsetId = CommonUtility.validateNumber(tempSubset);
		    int fromRow =  (noOfPage-1)*resultPerPage + 1;
			int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
			eclipseErrorMassage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNMESSAGE");
		
		    itemLevelFilterData = new ArrayList<ProductsModel>();	
		    //itemLevelFilterData = ProductsDAO.itemLevelFilter(taxonomyId,subsetId,generalSubset,fromRow,toRow);
		    HashMap<String, ArrayList<ProductsModel>> navigationResultList = new HashMap<String, ArrayList<ProductsModel>>();
		    String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
			int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
			
			if(keyWordTxt==null){
				keyWordTxt="";
			}else{
				keyWordTxt = keyWordTxt.trim();
			}
			
			if(CommonUtility.validateString(sortBy).length()==0 &&CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_NAVIGATION_SORT_BY")).length()>0){
		    	sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_NAVIGATION_SORT_BY").trim().toLowerCase();
		    }
			
	 	   if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y"))
	 	   {
	 		  homeTeritory = (String) session.getAttribute("shipBranchId");
	 		  fromRow =  (noOfPage-1)*resultPerPage;
	  		 navigationResultList = ProductHunterSolr.searchNavigation(keyWordTxt, subsetId, generalSubset, fromRow, toRow, "SHOP_BY_MANF", varPsid, 0, 0, 0,attrFilterList,manfId,session.getId(),sortBy,resultPerPage,"",buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"",wareHousecode, customerId,customerCountry,false,false,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHouseItems);
	 	   }
	 	   else
	 	   {
	  		  navigationResultList = ProductsDAO.searchNavigation("", subsetId, generalSubset, fromRow, toRow, "SHOP_BY_MANF", varPsid, 0, 0, 0,attrFilterList,manfId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,true,userId,"",viewFrequentlyPurcahsedOnly);//,true,wareHousecode, customerId,customerCountry
	 	   }
		    itemLevelFilterData = navigationResultList.get("itemList");
		    categoryFilterData  = navigationResultList.get("categoryList");
		    
		    if(categoryFilterData!=null && categoryFilterData.size()>0)
			{
				ArrayList<ProductsModel> tempListGen = new ArrayList<ProductsModel>();
				
				compareList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				for(ProductsModel categList:categoryFilterData)
				{
					tempListGen = compareList.get(categList.getParentCategory());
					ProductsModel temp = new ProductsModel();
					
					temp.setCategoryCount(categList.getCategoryCount());
					temp.setCategoryCode(categList.getCategoryCode());
					temp.setCategoryName(categList.getCategoryName());
					if(tempListGen==null)
					{
						tempListGen = new ArrayList<ProductsModel>();
						tempListGen.add(temp);
					}
					else
					{
						tempListGen.add(temp);
					}
					compareList.put(categList.getParentCategory(), tempListGen);
				}
				
			}
		    
		    attrFilterData = navigationResultList.get("attrList");
		    attrFilteredList = navigationResultList.get("filteredList");
		    if(itemLevelFilterData!=null && itemLevelFilterData.size()>0)
		    {
		    	mftName = itemLevelFilterData.get(0).getManufacturerName();
		    	String manufacturerLogo = itemLevelFilterData.get(0).getManufacturerLogo();
		    	resultCount=itemLevelFilterData.get(0).getResultCount();
		    	String searchType = "9";
		    	 if(srchTyp!=null)
		  		 {
		    		 searchType = srchTyp;
		  		 }
		    	ProductsDAO.writePopularSearchWord(mftName, userId, subsetId, resultCount, searchType);
		    	double disp;
		    	double disp1;
		    	disp = resultCount;
	
		    	if (resultCount > resultPerPage) {
	
		    		paginate = (resultCount / resultPerPage);
		    		disp1 = (disp / resultPerPage);
	
		    	} else {
		    		paginate = (resultCount / resultPerPage);
		    		disp1 = (resultCount / resultPerPage);
		    	}
	
		    	if (disp1 > paginate) {
		    		paginate = paginate + 1;
	
		    	}
		    	
		    	  if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y") && CommonUtility.validateNumber(pageNo)<1 && siteSearch!=null && siteSearch.trim().equalsIgnoreCase("Y") && CommonDBQuery.getSystemParamtersList().get("ENABLE_STATIC_SEARCH")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_STATIC_SEARCH").trim().equalsIgnoreCase("Y")){
		 			 keyWord = itemLevelFilterData.get(0).getItemUrl();
		 			 eventSearchData = new ArrayList<ProductsModel>();
		 			 staticContentData = new ArrayList<ProductsModel>();
		 			 staticContentData = ProductHunterSolr.SearchStaticContent(keyWord, "",0,2);
		 			 eventSearchData  = ProductHunterSolr.SearchStaticContent(keyWord, "Event",0,2);
			 	   }
		    	  
		    	  
		    	  breadCrumbList = new ArrayList<ProductsModel>();
		    	  String brandIdx = mftName.trim().substring(0, 1);
		    	  
		    	  ProductsModel nameCode= new ProductsModel();
	  		  	  nameCode.setCategoryCode(brandId);
	  		      nameCode.setCategoryName(brandIdx);
	  		      breadCrumbList.add(nameCode);
		    	  
	  		  	  nameCode= new ProductsModel();
			  	  nameCode.setCategoryCode(brandId);
			      nameCode.setCategoryName(mftName);
			  	  breadCrumbList.add(nameCode);
	  		  	  
	  		  	  
			  	  if(resultCount>1){
			  		  if(wareHousecode!=null && wareHousecode.trim().length()>0){
			  			  contentObject.put("branchName",UsersDAO.getShipBranchName(wareHousecode));
			  		  }
			  		  if(navigationResultList.get("facetRanges")!=null && navigationResultList.get("facetRanges").size() >0){
			  			  contentObject.put("facetRanges", navigationResultList.get("facetRanges").get(0).getFacetRange());	
			  		  }
			  		  contentObject.put("manfId", manfId);
			  		  contentObject.put("heading",mftName);
			  		  contentObject.put("manufacturerLogo",manufacturerLogo);
			  		  contentObject.put("paginate", paginate);
			  		  contentObject.put("keyWord", keyWord);
			  		  contentObject.put("narrowKeyword", narrowKeyword);
			  		  contentObject.put("srchTyp", srchTyp);
			  		  contentObject.put("pageNo", pageNo);
			  		  contentObject.put("categoryId", codeId);
			  		  contentObject.put("gallery", gallery);
			  		  contentObject.put("levelNo", levelNoSkip);
			  		  contentObject.put("navigationType", navigationType);
			  		  contentObject.put("attrFilterList",attrFilterList);
			  		  contentObject.put("filterList", filterList);
			  		  contentObject.put("resultPage", resultPage);
			  		  contentObject.put("keyWordTxt", keyWordTxt);
			  		  contentObject.put("resultCount", resultCount);
			  		  contentObject.put("attrFilterData", attrFilterData);
			  		  contentObject.put("attrFilteredList", attrFilteredList);
			  		  contentObject.put("breadCrumbList", breadCrumbList);
			  		  contentObject.put("topBanners", topBanners); 
			  		  contentObject.put("itemListData", itemLevelFilterData);
			  		  contentObject.put("eventSearchData", eventSearchData);
			  		  contentObject.put("staticContentData", staticContentData);
			  		  contentObject.put("sortBy", sortBy);
			  		  contentObject.put("brandIdx", brandIdx);
			  		  contentObject.put("DFMMode", "Y");
			  		  if(pageViewMode==null){
			  			  pageViewMode = (String) session.getAttribute("pageViewMode");
			  		  }
			  		  contentObject.put("pageViewMode", pageViewMode);
			  		  //renderContent = LayoutGenerator.templateLoader("ProductList", contentObject , null, null, null);
	
			  		  if(CommonUtility.validateString(reqType).length() > 0){
			  			  contentObject.put("responseType", reqType);
	
			  			  if(CommonUtility.validateString(reqType).equalsIgnoreCase("ShopByManufacturerCategory")){
			  				  HashMap<String, ArrayList<ProductsModel>> manfCategoryData = ProductHunterSolr.getBrandManufacturerCategory(manfId, "MANUFACTURER", subsetId, generalSubset);
			  				  contentObject.put("categoryListData",manfCategoryData.get("categoryList"));
			  				  renderContent = LayoutGenerator.templateLoader("ManufacturersDetailPage", contentObject , null, null, null);
			  			  }else{
			  				  renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
			  			  }
			  		  }else{
			  			  renderContent = LayoutGenerator.templateLoader("ProductList", contentObject , null, null, null);
			  		  }
			  		  target="success";
			  		  //target="ManufacturerProduct";
			  	  }else{
			   	    		itemPriceId = itemLevelFilterData.get(0).getItemPriceId();
			   	    		itemUrlSingle = itemLevelFilterData.get(0).getItemUrl();
				    		itemUrlSingle = CommonUtility.getUrlString(itemUrlSingle);
			   	    		target="singleItem";
			   	    }
	       	 
		    }else{
	       	 target=NONE;
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return target;
	}
	
	public String switchToUserSubsetFromDB(){
		try {
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			if(session.getAttribute("PREVIOUS_USER_SUBSET_ID")!=null && CommonUtility.validateNumber(session.getAttribute("PREVIOUS_USER_SUBSET_ID").toString())>0){
				session.setAttribute("userSubsetId", CommonUtility.validateString(session.getAttribute("PREVIOUS_USER_SUBSET_ID").toString()));
			}else{
				HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId(CommonUtility.validateString(session.getAttribute(Global.USERNAME_KEY).toString()),"Y");
				if(userDetails!=null && CommonUtility.validateString(userDetails.get("userSubsetId")).length()>0){
					session.setAttribute("userSubsetId", userDetails.get("userSubsetId"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

public String shopByManufacturer(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
		    String tempSubset = (String) session.getAttribute("userSubsetId");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		    int resultPerPage = CommonUtility.validateNumber(resultPage);
		    String defaultShiptoId = (String) session.getAttribute("defaultShipToId");
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			String customerId = (String) session.getAttribute("customerId");
			String customerCountry = (String) session.getAttribute("customerCountry");
		    String entityId = (String) session.getAttribute("entityId");
		    String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String pageClick = request.getParameter("pageClick");
			
			if(request.getParameter("isPreviouslyPurchased")!=null && request.getParameter("isPreviouslyPurchased").equalsIgnoreCase("Y"))
			{
				viewFrequentlyPurcahsedOnly = "Y";
			}
			
			if(pageViewMode!=null){
				session.setAttribute("pageViewMode", pageViewMode);
			}
			
		    navigationType = "MANF"; 
		    filterList = attrFilterList; 
		    if(filterList!=null && !filterList.trim().equalsIgnoreCase(""))
		    {
		    	try {
					filterList = URLEncoder.encode(filterList,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				}
		    }
		    if(resultPerPage==0)
		    {
		    	if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
		    		resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
			    	resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
		    	}else{
		    		resultPage = "12";
			    	resultPerPage = 12;
		    	}
		    }
		    System.out.println(attrFilterList);
		    int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
		    int subsetId = CommonUtility.validateNumber(tempSubset);
		    int fromRow =  (noOfPage-1)*resultPerPage + 1;
			int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
			eclipseErrorMassage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNMESSAGE");
		
		    itemLevelFilterData = new ArrayList<ProductsModel>();	
		    //itemLevelFilterData = ProductsDAO.itemLevelFilter(taxonomyId,subsetId,generalSubset,fromRow,toRow);
		    HashMap<String, ArrayList<ProductsModel>> navigationResultList = new HashMap<String, ArrayList<ProductsModel>>();
		    String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
			int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
			
			if(keyWordTxt==null){
				keyWordTxt="";
			}else{
				keyWordTxt = keyWordTxt.trim();
			}
			
			if(CommonUtility.validateString(sortBy).length()==0 &&CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_NAVIGATION_SORT_BY")).length()>0){
		    	sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_NAVIGATION_SORT_BY").trim().toLowerCase();
		    }
			
	 	   if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y"))
	 	   {
	 		  homeTeritory = (String) session.getAttribute("shipBranchId");
	 		  fromRow =  (noOfPage-1)*resultPerPage;
	 		 
	 		  if(CommonUtility.validateString(keyWordTxt).length()>0 && fromRow>0 && !CommonUtility.validateString(pageClick).equalsIgnoreCase("Y")){
					fromRow = 0;
					pageNo = "0";
	 		 }
	 		 if(CommonDBQuery.getSystemParamtersList().get("NRT")!=null && CommonDBQuery.getSystemParamtersList().get("NRT").trim().equalsIgnoreCase("Y"))
			 {
	 			 navigationResultList = ProductHunterSolrV2.searchNavigation(null, keyWordTxt, subsetId, generalSubset, fromRow, toRow, "SHOP_BY_MANF", CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"),  0, attrFilterList, manfId, sortBy, resultPerPage, "", buyingCompanyId, userId, false, false, viewFrequentlyPurcahsedOnly);
			 }else{
			  		navigationResultList = ProductHunterSolr.searchNavigation(keyWordTxt, subsetId, generalSubset, fromRow, toRow, "SHOP_BY_MANF", varPsid, 0, 0, 0,attrFilterList,manfId,session.getId(),sortBy,resultPerPage,"",buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"",wareHousecode, customerId,customerCountry,false,false,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHouseItems);
			 }
	  		 
	  		
	 	   }
	 	   else
	 	   {
	  		  navigationResultList = ProductsDAO.searchNavigation("", subsetId, generalSubset, fromRow, toRow, "SHOP_BY_MANF", varPsid, 0, 0, 0,attrFilterList,manfId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,true,userId,"",viewFrequentlyPurcahsedOnly);//,true,wareHousecode, customerId,customerCountry
	 	   }
		    itemLevelFilterData = navigationResultList.get("itemList");
		    categoryFilterData  = navigationResultList.get("categoryList");
		    
		    if(CommonUtility.validateNumber(pageNo)<1){
		    	BrandAndManufacturerModel brandAndManufacturerModel = ProductsDAO.getManufacturerDetails(CommonUtility.validateNumber(manfId));
				contentObject.put("brandAndManufacturerModel", brandAndManufacturerModel);
				if(brandAndManufacturerModel!=null){
					if(brandAndManufacturerModel.getStaticPageId()>0) {
						String staticPageByIdUrl = context+staticPage+find+brandAndManufacturerModel.getStaticPageId();
						Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(staticPageByIdUrl, "GET", null, StaticPageData.class);
						if(response!=null && response.getStatus().getCode()==200) {
							StaticPageData staticPageDataModel = (StaticPageData) response.getData();
							if(staticPageDataModel!=null && (staticPageDataModel.getStatus().equalsIgnoreCase("Y") || staticPageDataModel.getStatus().equalsIgnoreCase("A"))) {
								contentObject.put("assignedStaticPageId", staticPageDataModel.getId());
								contentObject.put("assignedStaticPageContent", staticPageDataModel.getPageContent());
								//System.out.println(staticPageDataModel.getPageContent());
							}
						}
					}
				}
		    }
		    
		    
		    if(categoryFilterData!=null && categoryFilterData.size()>0)
			{
				ArrayList<ProductsModel> tempListGen = new ArrayList<ProductsModel>();
				
				compareList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				for(ProductsModel categList:categoryFilterData)
				{
					tempListGen = compareList.get(categList.getParentCategory());
					ProductsModel temp = new ProductsModel();
					
					temp.setCategoryCount(categList.getCategoryCount());
					temp.setCategoryCode(categList.getCategoryCode());
					temp.setCategoryName(categList.getCategoryName());
					if(tempListGen==null)
					{
						tempListGen = new ArrayList<ProductsModel>();
						tempListGen.add(temp);
					}
					else
					{
						tempListGen.add(temp);
					}
					compareList.put(categList.getParentCategory(), tempListGen);
				}
				
			}
		    
		    attrFilterData = navigationResultList.get("attrList");
		    attrFilteredList = navigationResultList.get("filteredList");
		    if(itemLevelFilterData!=null && itemLevelFilterData.size()>0)
		    {
		    	mftName = itemLevelFilterData.get(0).getManufacturerName();
		    	String manufacturerLogo = itemLevelFilterData.get(0).getManufacturerLogo();
		    	resultCount=itemLevelFilterData.get(0).getResultCount();
		    	String searchType = "9";
		    	 if(srchTyp!=null)
		  		 {
		    		 searchType = srchTyp;
		  		 }
		    	 ProductsDAO.writePopularSearchWord(mftName, userId, subsetId, resultCount, searchType);
		    	 double disp;
		    	 double disp1;
		    	disp = resultCount;
	
		    	if (resultCount > resultPerPage) {
	
		    		paginate = (resultCount / resultPerPage);
		    		disp1 = (disp / resultPerPage);
	
		    	} else {
		    		paginate = (resultCount / resultPerPage);
		    		disp1 = (resultCount / resultPerPage);
		    	}
	
		    	if (disp1 > paginate) {
		    		paginate = paginate + 1;
	
		    	}
		    	try {
		    		if(attrFilterList!=null){
		    			attrFilterList = attrFilterList.replaceAll("% ", "%25 ");
		    			attrFilterList = URLDecoder.decode(attrFilterList,"UTF-8");
		    		    attrFilterList = attrFilterList.replaceAll("'", "%27");
		    		    String paginateFilter = attrFilterList;
		    		    contentObject.put("paginateFilter",paginateFilter);
		    		    attrFilterList = attrFilterList.replaceAll("&", "&amp;");
	
		    		}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	  if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y") && CommonUtility.validateNumber(pageNo)<1 && siteSearch!=null && siteSearch.trim().equalsIgnoreCase("Y") && CommonDBQuery.getSystemParamtersList().get("ENABLE_STATIC_SEARCH")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_STATIC_SEARCH").trim().equalsIgnoreCase("Y")){
		 			 keyWord = itemLevelFilterData.get(0).getItemUrl();
		 			 eventSearchData = new ArrayList<ProductsModel>();
		 			 staticContentData = new ArrayList<ProductsModel>();
		 			 staticContentData = ProductHunterSolr.SearchStaticContent(keyWord, "",0,2);
		 			 eventSearchData  = ProductHunterSolr.SearchStaticContent(keyWord, "Event",0,2);
			 	   }
		    	  
		    	  
		    	  breadCrumbList = new ArrayList<ProductsModel>();
		    	  String brandIdx = mftName.trim().substring(0, 1);
		    	  
		    	  ProductsModel nameCode= new ProductsModel();
	  		  	  nameCode.setCategoryCode(brandId);
	  		      nameCode.setCategoryName(brandIdx);
	  		      breadCrumbList.add(nameCode);
		    	  
	  		  	  nameCode= new ProductsModel();
			  	  nameCode.setCategoryCode(brandId);
			      nameCode.setCategoryName(mftName);
			  	  breadCrumbList.add(nameCode);
	  		  	  
			  	if(CommonUtility.customServiceUtility() != null) {
			  		resultCount = CommonUtility.customServiceUtility().resultCountCondition(resultCount);
				}
	  		  	  
			  	  if(resultCount>1){
			  		  if(wareHousecode!=null && wareHousecode.trim().length()>0){
			  			  contentObject.put("branchName",UsersDAO.getShipBranchName(wareHousecode));
			  		  }
			  		  if(navigationResultList.get("facetRanges")!=null && navigationResultList.get("facetRanges").size() >0){
			  			  contentObject.put("facetRanges", navigationResultList.get("facetRanges").get(0).getFacetRange());	
			  		  }
			  		  contentObject.put("manfId", manfId);
			  		  contentObject.put("heading",mftName);
			  		  contentObject.put("manufacturerLogo",manufacturerLogo);
			  		  contentObject.put("paginate", paginate);
			  		  contentObject.put("keyWord", keyWord);
			  		  contentObject.put("narrowKeyword", narrowKeyword);
			  		  contentObject.put("srchTyp", srchTyp);
			  		  contentObject.put("pageNo", pageNo);
			  		  contentObject.put("categoryId", codeId);
			  		  contentObject.put("gallery", gallery);
			  		  contentObject.put("levelNo", levelNoSkip);
			  		  contentObject.put("navigationType", navigationType);
			  		  contentObject.put("attrFilterList",attrFilterList);
			  		  contentObject.put("filterList", filterList);
			  		  contentObject.put("resultPage", resultPage);
			  		  contentObject.put("keyWordTxt", keyWordTxt);
			  		  contentObject.put("resultCount", resultCount);
			  		  contentObject.put("attrFilterData", attrFilterData);
			  		  contentObject.put("attrFilteredList", attrFilteredList);
			  		  contentObject.put("breadCrumbList", breadCrumbList);
			  		  contentObject.put("topBanners", topBanners); 
			  		  contentObject.put("itemListData", itemLevelFilterData);
			  		  contentObject.put("eventSearchData", eventSearchData);
			  		  contentObject.put("staticContentData", staticContentData);
			  		  contentObject.put("sortBy", sortBy);
			  		  contentObject.put("brandIdx", brandIdx);
			  		  if(pageViewMode==null){
			  			  pageViewMode = (String) session.getAttribute("pageViewMode");
			  		  }
			  		  contentObject.put("pageViewMode", pageViewMode);
			  		  //renderContent = LayoutGenerator.templateLoader("ProductList", contentObject , null, null, null);
	
			  		  if(CommonUtility.validateString(reqType).length() > 0){
			  			  contentObject.put("responseType", reqType);
	
			  			  if(CommonUtility.validateString(reqType).equalsIgnoreCase("ShopByManufacturerCategory")){
			  				  HashMap<String, ArrayList<ProductsModel>> manfCategoryData = ProductHunterSolr.getBrandManufacturerCategory(manfId, "MANUFACTURER", subsetId, generalSubset);
			  				  contentObject.put("categoryListData",manfCategoryData.get("categoryList"));
			  				  renderContent = LayoutGenerator.templateLoader("ManufacturersDetailPage", contentObject , null, null, null);
			  			  }else{
			  				  renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
			  			  }
			  		  }else{
			  			  renderContent = LayoutGenerator.templateLoader("ProductList", contentObject , null, null, null);
			  		  }
			  		  target="success";
			  		  //target="ManufacturerProduct";
			  	  }else{
			   	    		itemPriceId = itemLevelFilterData.get(0).getItemPriceId();
			   	    		itemUrlSingle = itemLevelFilterData.get(0).getItemUrl();
				    		itemUrlSingle = CommonUtility.getUrlString(itemUrlSingle);
			   	    		target="singleItem";
			   	    }
	       	 
		    }else{
	       	 target=NONE;
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return target;
	}
	public String shopByBrand(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("keyWordTxt", CommonUtility.validateString(keyWordTxt));
			renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int resultPerPage = CommonUtility.validateNumber(resultPage);
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			String customerId = (String) session.getAttribute("customerId");
			String customerCountry = (String) session.getAttribute("customerCountry");
			String entityId = (String) session.getAttribute("entityId");
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			String pageClick = request.getParameter("pageClick");
			if(request.getParameter("isPreviouslyPurchased")!=null && request.getParameter("isPreviouslyPurchased").equalsIgnoreCase("Y"))
			{
				viewFrequentlyPurcahsedOnly = "Y";
			}
			if(CommonUtility.validateString(request.getParameter("clearanceItems")).equalsIgnoreCase("Y"))
			{
				clearanceFlag = "Y";
			}
			if(CommonUtility.validateString(request.getParameter("wareHouseItems")).length()>0)
			{
				contentObject.put("wareHouseItems", CommonUtility.validateString(request.getParameter("wareHouseItems")));
				contentObject.put("shipToMe", CommonUtility.validateString(request.getParameter("shipToMe")));
				if(CommonUtility.validateString(request.getParameter("shipToMe")).equalsIgnoreCase("Y")) {
					wareHouseItems = CommonUtility.validateString(request.getParameter("wareHouseItems"))+" AND id:SHIPTOME_*";
				}else if(CommonUtility.validateString(request.getParameter("shipToMe")).equalsIgnoreCase("N")) {
					wareHouseItems = CommonUtility.validateString(request.getParameter("wareHouseItems"))+" AND id:SHIPTOSTORE_*) OR (warehouseCode:NONORGILLSHIPTOSTORE AND id:NONORGILLSHIPTOSTORE_*";
				}else {
					wareHouseItems = CommonUtility.validateString(request.getParameter("wareHouseItems"));
				}
				contentObject.put("wareHouseItemsFlag", "Y");
			}else if(CommonUtility.validateString((String) session.getAttribute("wareHouseItems")).length()>0){
				wareHouseItems = CommonUtility.validateString((String) session.getAttribute("wareHouseItems"));
			}
			//To avoid XSS injection - Security_QA
	    	codeId = CommonUtility.sanitizeHtml(codeId);
	    	levelNo = CommonUtility.sanitizeHtml(levelNo);
	    	sortBy = CommonUtility.sanitizeHtml(sortBy);
	    	srchTyp = CommonUtility.sanitizeHtml(srchTyp);
	    	pageNo = CommonUtility.sanitizeHtml(pageNo);
	    	keyWordTxt = CommonUtility.sanitizeHtml(keyWordTxt);
	    	keyWord = CommonUtility.sanitizeHtml(keyWord);
	    	pageViewMode = CommonUtility.sanitizeHtml(pageViewMode);
	    	//attrFilterList = CommonUtility.sanitizeHtml(attrFilterList);
			
			int userId = CommonUtility.validateNumber(sessionUserId);
			if(pageViewMode!=null){
				session.setAttribute("pageViewMode", pageViewMode);
			}
			if(CommonUtility.validateString(sortBy).length()==0 &&CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_SHOPBY_BRAND_SORT_BY")).length()>0){
				sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_SHOPBY_BRAND_SORT_BY").trim().toLowerCase();
			}
			navigationType = "BRAND"; 
			filterList = attrFilterList; 
			if(filterList!=null && !filterList.trim().equalsIgnoreCase("")){
				try {
					filterList = URLEncoder.encode(filterList,"UTF-8");
				} catch (UnsupportedEncodingException e) {
		
					e.printStackTrace();
				}
			}
			if(resultPerPage==0){
				if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
					resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
					resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
				}else{
					resultPage = "12";
					resultPerPage = 12;
				}
			}
			System.out.println(attrFilterList);
			int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
			int subsetId = CommonUtility.validateNumber(tempSubset);
			int fromRow =  (noOfPage-1)*resultPerPage + 1;
			int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
			eclipseErrorMassage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNMESSAGE");
			itemLevelFilterData = new ArrayList<ProductsModel>();	
			HashMap<String, ArrayList<ProductsModel>> navigationResultList = new HashMap<String, ArrayList<ProductsModel>>();
			String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
			int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
			if(keyWordTxt==null){
				keyWordTxt="";
			}else{
				keyWordTxt = keyWordTxt.trim();
			}
			if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y")){
				homeTeritory = (String) session.getAttribute("shipBranchId");
				fromRow =  (noOfPage-1)*resultPerPage;
				sortBy = CommonUtility.sanitizeHtml(sortBy);
				
				if(CommonUtility.validateString(keyWordTxt).length()>0 && fromRow>0 && !CommonUtility.validateString(pageClick).equalsIgnoreCase("Y")){
					fromRow = 0;
					pageNo = "0";
				}
				if(CommonDBQuery.getSystemParamtersList().get("NRT")!=null && CommonDBQuery.getSystemParamtersList().get("NRT").trim().equalsIgnoreCase("Y"))
				 {
					 navigationResultList = ProductHunterSolrV2.searchNavigation(null, keyWordTxt, subsetId, generalSubset, fromRow, toRow, "SHOP_BY_MANF", CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"),  0, attrFilterList, brandId, sortBy, resultPerPage, "", buyingCompanyId, userId, false, false, viewFrequentlyPurcahsedOnly);
				 }else{
					 navigationResultList = ProductHunterSolr.searchNavigation(keyWordTxt, subsetId, generalSubset, fromRow, toRow, "SHOP_BY_BRAND", varPsid, 0, 0, 0,attrFilterList,brandId,session.getId(),sortBy,resultPerPage,"",buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"",wareHousecode, customerId,customerCountry,false,false,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHouseItems);
				 }
				
			}else{
				navigationResultList = ProductsDAO.searchNavigation("", subsetId, generalSubset, fromRow, toRow, "SHOP_BY_BRAND", varPsid, 0, 0, 0,attrFilterList,brandId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,true,userId,"",viewFrequentlyPurcahsedOnly);//,true,wareHousecode, customerId,customerCountry
			}
			itemLevelFilterData = navigationResultList.get("itemList");
			categoryFilterData  = navigationResultList.get("categoryList");
			if(categoryFilterData!=null && categoryFilterData.size()>0){
				ArrayList<ProductsModel> tempListGen = new ArrayList<ProductsModel>();
				compareList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				for(ProductsModel categList:categoryFilterData){
					tempListGen = compareList.get(categList.getParentCategory());
					ProductsModel temp = new ProductsModel();
					temp.setCategoryCount(categList.getCategoryCount());
					temp.setCategoryCode(categList.getCategoryCode());
					temp.setCategoryName(categList.getCategoryName());
					if(tempListGen==null){
						tempListGen = new ArrayList<ProductsModel>();
						tempListGen.add(temp);
					}else{
						tempListGen.add(temp);
					}
					compareList.put(categList.getParentCategory(), tempListGen);
				}
			}
			BrandAndManufacturerModel brandAndManufacturerModel = ProductsDAO.getBrandDetails(CommonUtility.validateNumber(brandId));
			contentObject.put("brandAndManufacturerModel", brandAndManufacturerModel);
			if(CommonUtility.validateNumber(pageNo)<1 && brandAndManufacturerModel!=null){ 
				if(CommonUtility.validateString(brandAndManufacturerModel.getBrandUrl()).length()>0 ){
					contentObject.put("brandUrl", CommonUtility.validateString(brandAndManufacturerModel.getBrandUrl()));
				}
				if(brandAndManufacturerModel.getStaticPageId()>0) {
					String staticPageByIdUrl = context+staticPage+find+brandAndManufacturerModel.getStaticPageId();
					Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(staticPageByIdUrl, "GET", null, StaticPageData.class);
					if(response!=null && response.getStatus().getCode()==200) {
						StaticPageData staticPageDataModel = (StaticPageData) response.getData();
						if(staticPageDataModel!=null && (staticPageDataModel.getStatus().equalsIgnoreCase("Y") || staticPageDataModel.getStatus().equalsIgnoreCase("A"))) {
							contentObject.put("assignedStaticPageId", staticPageDataModel.getId());
							contentObject.put("assignedStaticPageContent", staticPageDataModel.getPageContent());
							//System.out.println(staticPageDataModel.getPageContent());
						}
					}
				}
			}
			attrFilterData = navigationResultList.get("attrList");
			attrFilteredList = navigationResultList.get("filteredList");
			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
				mftName = itemLevelFilterData.get(0).getBrandName();
				resultCount=itemLevelFilterData.get(0).getResultCount();
				String searchType = "9";
				if(srchTyp!=null){
					searchType = srchTyp;
				}
				ProductsDAO.writePopularSearchWord(mftName, userId, subsetId, resultCount, searchType);
				double disp;
				double disp1;
				disp = resultCount;
				if (resultCount > resultPerPage) {
					paginate = (resultCount / resultPerPage);
					disp1 = (disp / resultPerPage);
				} else {
					paginate = (resultCount / resultPerPage);
					disp1 = (resultCount / resultPerPage);
				}
				if (disp1 > paginate) {
					paginate = paginate + 1;
				}
				
				try {
		    		if(attrFilterList!=null){
		    			attrFilterList = attrFilterList.replaceAll("% ", "%25 ");
		    			attrFilterList = URLDecoder.decode(attrFilterList,"UTF-8");
		    		    attrFilterList = attrFilterList.replaceAll("'", "%27");
		    		    String paginateFilter = attrFilterList;
		    		    contentObject.put("paginateFilter",paginateFilter);
		    		    attrFilterList = attrFilterList.replaceAll("&", "&amp;");
	
		    		}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y") && CommonUtility.validateNumber(pageNo)<1 && siteSearch!=null && siteSearch.trim().equalsIgnoreCase("Y") && CommonDBQuery.getSystemParamtersList().get("ENABLE_STATIC_SEARCH")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_STATIC_SEARCH").trim().equalsIgnoreCase("Y")){
					keyWord = itemLevelFilterData.get(0).getItemUrl();
					keyWord = CommonUtility.sanitizeHtml(keyWord);
					eventSearchData = new ArrayList<ProductsModel>();
					staticContentData = new ArrayList<ProductsModel>();
					staticContentData = ProductHunterSolr.SearchStaticContent(keyWord, "",0,2);
					eventSearchData  = ProductHunterSolr.SearchStaticContent(keyWord, "Event",0,2);
				}
				breadCrumbList = new ArrayList<ProductsModel>();
				String brandIdx = mftName.trim().substring(0, 1);
				ProductsModel nameCode= new ProductsModel();
				nameCode.setCategoryCode(brandId);
				nameCode.setCategoryName(brandIdx);
				breadCrumbList.add(nameCode);
				nameCode= new ProductsModel();
				nameCode.setCategoryCode(brandId);
				nameCode.setCategoryName(mftName);
				breadCrumbList.add(nameCode);
				if(resultCount>1){
					if(CommonUtility.customServiceUtility()!=null) {
						int resultCount1 = CommonUtility.customServiceUtility().getItemsResultCount(keyWordTxt, subsetId, generalSubset, fromRow, toRow, "SHOP_BY_BRAND", varPsid, 0, 0, 0,attrFilterList,brandId,session.getId(),sortBy,resultPerPage,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","", "","",false,true,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHousecode);
						contentObject.put("resultCount1", resultCount1);
						int resultCount2 = CommonUtility.customServiceUtility().getItemsResultCount(keyWordTxt, subsetId, generalSubset, fromRow, toRow, "SHOP_BY_BRAND", varPsid, 0, 0, 0,attrFilterList,brandId,session.getId(),sortBy,resultPerPage,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","", "","",false,true,viewFrequentlyPurcahsedOnly,clearanceFlag,"");
						contentObject.put("resultCount2", resultCount2);
					}
					if(wareHousecode!=null && wareHousecode.trim().length()>0){
						contentObject.put("branchName",UsersDAO.getShipBranchName(wareHousecode));
					}
					if(navigationResultList.get("facetRanges")!=null && navigationResultList.get("facetRanges").size() >0){
						contentObject.put("facetRanges", navigationResultList.get("facetRanges").get(0).getFacetRange());
					}
					contentObject.put("brandId", brandId);
					contentObject.put("heading",mftName);
					contentObject.put("paginate", paginate);
					contentObject.put("keyWord", keyWord);
					contentObject.put("narrowKeyword", narrowKeyword);
					contentObject.put("srchTyp", srchTyp);
					contentObject.put("pageNo", pageNo);
					contentObject.put("categoryId", codeId);
					contentObject.put("gallery", gallery);
					contentObject.put("levelNo", levelNoSkip);
					contentObject.put("navigationType", navigationType);
					contentObject.put("attrFilterList",attrFilterList);
					contentObject.put("filterList", filterList);
					contentObject.put("resultPage", resultPage);
					contentObject.put("keyWordTxt", keyWordTxt);
					contentObject.put("resultCount", resultCount);
					contentObject.put("attrFilterData", attrFilterData);
					contentObject.put("attrFilteredList", attrFilteredList);
					contentObject.put("breadCrumbList", breadCrumbList);
					contentObject.put("topBanners", topBanners); 
					contentObject.put("itemListData", itemLevelFilterData);
					contentObject.put("eventSearchData", eventSearchData);
					contentObject.put("staticContentData", staticContentData);
					contentObject.put("sortBy", sortBy);
					contentObject.put("brandIdx", brandIdx);
					contentObject.put("clearanceItems", clearanceFlag);
					if(pageViewMode==null){
						pageViewMode = (String) session.getAttribute("pageViewMode");
					}
					contentObject.put("pageViewMode", pageViewMode);
					if(CommonUtility.validateString(reqType).length() > 0){
						contentObject.put("responseType", reqType);
						if(CommonUtility.validateString(reqType).equalsIgnoreCase("ShopByBrandCategory")){
							HashMap<String, ArrayList<ProductsModel>> manfCategoryData = ProductHunterSolr.getBrandManufacturerCategory(brandId, "BRAND", subsetId, generalSubset);
							contentObject.put("categoryListData",manfCategoryData.get("categoryList"));
							renderContent = LayoutGenerator.templateLoader("BrandsDetailPage", contentObject , null, null, null);
						}else{
							renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
						}
					}else{
						renderContent = LayoutGenerator.templateLoader("ProductList", contentObject , null, null, null);
					}
					target="success";
				}else{
					itemPriceId = itemLevelFilterData.get(0).getItemPriceId();
					itemUrlSingle = itemLevelFilterData.get(0).getItemUrl();
					itemUrlSingle = CommonUtility.getUrlString(itemUrlSingle);
					target="singleItem";
				}
			}else{
				if(CommonUtility.validateString(reqType).equalsIgnoreCase("ShopByBrandCategory")){
					String brandIdx = "";
					if(brandAndManufacturerModel!=null){ 
						if(CommonUtility.validateString(brandAndManufacturerModel.getBrandname()).length()>0 ){
							mftName = CommonUtility.validateString(brandAndManufacturerModel.getBrandname());
						}
					}
					if(CommonUtility.validateString(mftName).length()>0){
						brandIdx = mftName.trim().substring(0, 1);
					}
					System.out.println("mftName : "+mftName+" : brandId : "+brandId);
					contentObject.put("brandId", brandId);
					contentObject.put("heading",mftName);
					contentObject.put("paginate", paginate);
					contentObject.put("keyWord", keyWord);
					contentObject.put("narrowKeyword", narrowKeyword);
					contentObject.put("srchTyp", srchTyp);
					contentObject.put("pageNo", pageNo);
					contentObject.put("categoryId", codeId);
					contentObject.put("gallery", gallery);
					contentObject.put("levelNo", levelNoSkip);
					contentObject.put("navigationType", navigationType);
					contentObject.put("attrFilterList",attrFilterList);
					contentObject.put("filterList", filterList);
					contentObject.put("resultPage", resultPage);
					contentObject.put("keyWordTxt", keyWordTxt);
					contentObject.put("resultCount", resultCount);
					contentObject.put("attrFilterData", attrFilterData);
					contentObject.put("attrFilteredList", attrFilteredList);
					contentObject.put("breadCrumbList", breadCrumbList);
					contentObject.put("topBanners", topBanners); 
					contentObject.put("itemListData", itemLevelFilterData);
					contentObject.put("eventSearchData", eventSearchData);
					contentObject.put("staticContentData", staticContentData);
					contentObject.put("sortBy", sortBy);
					contentObject.put("brandIdx", brandIdx);
					//HashMap<String, ArrayList<ProductsModel>> manfCategoryData = ProductHunterSolr.getBrandManufacturerCategory(brandId, "BRAND", subsetId, generalSubset);
					HashMap<String, ArrayList<ProductsModel>> manfCategoryData = new HashMap<String, ArrayList<ProductsModel>>();
					contentObject.put("categoryListData",manfCategoryData.get("categoryList"));
					renderContent = LayoutGenerator.templateLoader("BrandsDetailPage", contentObject , null, null, null);
					target="success";
				}else{
					target=NONE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}
	
	@SuppressWarnings("unchecked")
	public VelocityContext getCartCount(VelocityContext context, HttpServletRequest request){	
		try{
			System.out.println("cart count called");
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String tempSubset = (String) session.getAttribute("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			String cartCountBySession = CommonDBQuery.getSystemParamtersList().get("CART_COUNT_BY_SESSION");
			
			if(CommonUtility.validateString(cartCountBySession).isEmpty())
				cartCountBySession = null;
			
			System.out.println("generalSubset ----- : "+ generalSubset);
			
			
			String searchIndex = "PH_SEARCH_"+subsetId;
			if(generalSubset>0 && generalSubset!=subsetId)
				searchIndex = "PH_SEARCH_"+generalSubset+"_"+subsetId;
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IGNORE_INDEXTYPE_IN_CORE")).equalsIgnoreCase("Y")) {
				searchIndex="PH_SEARCH_ALL";
			}
			userName = (String) session.getAttribute(Global.USERNAME_KEY);
			slUserName = (String) session.getAttribute(Global.SLUSERNAME_KEY);
			
			if(session.getAttribute("isOciUser")!=null){
				checkOciUser = (Integer) session.getAttribute("isOciUser");
				ociSession = session.getId();
			}
			if(session.getAttribute("isErpDown")!=null){
				isEclipse = (String) session.getAttribute("isErpDown");
			}
			if(session.getAttribute("bannerMessage")!=null){
				bannerMessage = (String) session.getAttribute("bannerMessage");
			}else{
				//bannerMessage = ProductsDAO.getBannerText(subsetId);
				session.setAttribute("bannerMessage",bannerMessage);
			}
			
		    
			if(checkOciUser==1 || ( checkOciUser==2 && CommonUtility.validateString((String)session.getAttribute("SESSION_AUTH_REQUIRED")).equalsIgnoreCase("Y"))){
				  validateResponse = ProductsDAO.getCartCountBySession(userId, session.getId(),subsetId, generalSubset);
				   session.setAttribute("cartCountSession",validateResponse);
				   
				   //need to Re-Work as barath Sir suggest
			  /* if(session.getAttribute("cartCountSession")==null ){
				   validateResponse = ProductsDAO.getCartCountBySession(userId, session.getId(),subsetId, generalSubset);
				   session.setAttribute("cartCountSession",validateResponse);
			   }else{
				   validateResponse = (Integer) session.getAttribute("cartCountSession");
			   }*/
		   
		   }else if (userId>1){
			   
			   if(cartCountBySession!=null && cartCountBySession.trim().equalsIgnoreCase("Y"))
			   {
				   if(session.getAttribute("cartCountSession")==null){
				   	   
				   	   ProductsModel cartCountAndQuantityCountModel = ProductsDAO.getCartCountAndQuantityCount(userId,subsetId, generalSubset);
				   	   if(cartCountAndQuantityCountModel!=null){
				   		 validateResponse = cartCountAndQuantityCountModel.getCartItemCount();
				   		 cartItemQuantitySum = cartCountAndQuantityCountModel.getCartIndividualItemQuantitySum();
				   		 
				   		 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHOW_CART_COUNT_BY")).equalsIgnoreCase("QUANTITY")){
				   			validateResponse = cartCountAndQuantityCountModel.getCartItemCount();
				   		 }
				   		 
				   	   }else{
				   		 validateResponse = ProductsDAO.getCartCount(userId,subsetId, generalSubset);
				   	   }
				   	   session.setAttribute("cartCountSession",validateResponse);
				   	   session.setAttribute("cartItemQuantityCountSession",cartItemQuantitySum);
				   }else{
					   validateResponse = (Integer) session.getAttribute("cartCountSession");
					   if(session.getAttribute("cartItemQuantityCountSession")!=null){
						   cartItemQuantitySum = (Integer) session.getAttribute("cartItemQuantityCountSession");
					   }
				   }
			   }
			   else
			   {
				   ProductsModel cartCountAndQuantityCountModel = ProductsDAO.getCartCountAndQuantityCount(userId,subsetId, generalSubset);
			   	   if(cartCountAndQuantityCountModel!=null){
			   		 validateResponse = cartCountAndQuantityCountModel.getCartItemCount();
			   		 cartItemQuantitySum = cartCountAndQuantityCountModel.getCartIndividualItemQuantitySum();
			   		 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHOW_CART_COUNT_BY")).equalsIgnoreCase("QUANTITY")){
			   			validateResponse = cartCountAndQuantityCountModel.getCartItemCount();
			   		 }
			   	   }else{
			   		 validateResponse = ProductsDAO.getCartCount(userId,subsetId, generalSubset);
			   	   }
				   session.setAttribute("cartCountSession",validateResponse);
				   session.setAttribute("cartItemQuantityCountSession",cartItemQuantitySum);
			   }
			   
				
				  
		   }else{
			   
			   if(cartCountBySession!=null && cartCountBySession.trim().equalsIgnoreCase("Y")){
				   if(session.getAttribute("cartCountSession")==null){
					   	 validateResponse = ProductsDAO.getCartCountBySession(userId,session.getId(),subsetId, generalSubset);
					   	 session.setAttribute("cartCountSession",validateResponse);
					  }else{	
						 validateResponse = (Integer) session.getAttribute("cartCountSession");
					  }
			   }else{
				   validateResponse = ProductsDAO.getCartCountBySession(userId,session.getId(),subsetId, generalSubset);
					 session.setAttribute("cartCountSession",validateResponse);
			   }
				 
		   }
			
			setManufacturerIndex(new ArrayList<String>());
			setManufacturerIndex((ArrayList<String>) session.getAttribute("manufacturerIndex"));
			ArrayList<String> mfrIndexList = (ArrayList<String>) session.getAttribute("manufacturerIndex");
			if(session.getAttribute("manufacturerIndex")!=null && mfrIndexList.size()>0){
				manufacturerIndex = (ArrayList<String>) session.getAttribute("manufacturerIndex");
			}else{
				//manufacturerIndex = ProductsDAO.getbrandListIndex(CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
				if (userId>1){
					manufacturerIndex = ProductsDAO.getbrandListIndex(CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
				}else{
					manufacturerIndex = WebLogin.getBrandList();
				}
				session.setAttribute("manufacturerIndex",manufacturerIndex);
			}
			
			/*setManufacturerList(new ArrayList<ProductsModel>());
			setManufacturerList((ArrayList<ProductsModel>) session.getAttribute("manufacturerList"));
			ArrayList<ProductsModel> mfrNameList = (ArrayList<ProductsModel>) session.getAttribute("manufacturerList");
			
			if(session.getAttribute("manufacturerList")!=null && mfrNameList.size()>0){
				manufacturerList = (ArrayList<ProductsModel>) session.getAttribute("manufacturerList");
			}else{
				//manufacturerList = ProductsDAO.getManufacturerList(manufacturerIndex.get(0),CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")));
				manufacturerList = ProductsDAO.brandList(manufacturerIndex.get(0),CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")));
				session.setAttribute("manufacturerList",manufacturerList);
			}*/
			
			
			/*setBrandList(new ArrayList<ProductsModel>());
			setBrandList((ArrayList<ProductsModel>) session.getAttribute("brandList"));
			ArrayList<ProductsModel> brdNameList = (ArrayList<ProductsModel>) session.getAttribute("brandList");
			if(session.getAttribute("brandList")!=null && brdNameList.size()>0){
				brandList = (ArrayList<ProductsModel>) session.getAttribute("brandList");
			}else{
				if(manufacturerIndex.size()>0){
					brandList = ProductsDAO.brandList(manufacturerIndex.get(0),CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
					session.setAttribute("brandList", brandList);	
				}
			}
			*/
			
		/*	ArrayList<ProductsModel> brandListForPage = ProductsDAO.brandList(manufacturerIndex.get(0),CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
			session.setAttribute("brandListInfo", brandListForPage);
			ArrayList<ArrayList<ProductsModel>> allBrandList = new ArrayList<ArrayList<ProductsModel>>();
			for (int i = 0; i < manufacturerIndex.size(); i++){
				brandList = ProductsDAO.brandList(manufacturerIndex.get(i),CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),generalSubset);
	 			allBrandList.add(brandList);
			}
			session.setAttribute("brandList", allBrandList);*/
			
			//--userTopTab = new ArrayList<Integer>();
			//--userTopTab = (ArrayList<Integer>) session.getAttribute("userTabList");
			categMenu = new ArrayList<ProductsModel>();
			
			//secondLevelMenu = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
			
			//secondLevelMenu = MenuAndBannersDAO.getSecondLevelMenu();
			secondLevelMenu = MenuAndBannersDAO.getAllSecMenuList().get("PH_SEARCH_"+subsetId);
			
			//thirdLevelMenu = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
			thirdLevelMenu = MenuAndBannersDAO.getThirdLevelMenu();
			
			brandListInfo = new ArrayList<MenuAndBannersModal>();
			brandListInfo = MenuAndBannersDAO.getAllBrandData().get("PH_SEARCH_"+subsetId+"_MV");
			
			manufacturerListInfo = new ArrayList<MenuAndBannersModal>();
			manufacturerListInfo = MenuAndBannersDAO.getAllManufactuerData().get("PH_SEARCH_"+subsetId);
			
			LinkedHashMap<Integer, String> selectedManufacturerList = new LinkedHashMap<Integer, String>();
			if(session!=null && session.getAttribute("selectedManufacturerList")!=null && (LinkedHashMap<Integer, String>)session.getAttribute("selectedManufacturerList")!=null){
				selectedManufacturerList = (LinkedHashMap<Integer, String>)session.getAttribute("selectedManufacturerList");
			}else{
				selectedManufacturerList = CommonUtility.sortHashMapByValuesIntegerString(ProductsDAO.getManufacturerCustomFieldDetailsFromView("SELECTED_MANUFACTURER_LIST"));
			}
			session.setAttribute("selectedManufacturerList", selectedManufacturerList);	
			
			
			LinkedHashMap<Integer, ProductsModel> selectedBrandList = new LinkedHashMap<Integer, ProductsModel>();
			if(session!=null && session.getAttribute("selectedBrandList")!=null && (LinkedHashMap<Integer, String>)session.getAttribute("selectedBrandList")!=null){
				selectedBrandList = (LinkedHashMap<Integer, ProductsModel>)session.getAttribute("selectedBrandList");
			}else{
				selectedBrandList = ProductsDAO.getBrandCustomFieldDetailsFromView("selectedBrandList");
			}
			session.setAttribute("selectedBrandList", selectedBrandList);
			
			LinkedHashMap<Integer, String> directFromManufacturer = new LinkedHashMap<Integer, String>();
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DIRECT_FROM_MANUFACTURER")).equalsIgnoreCase("Y")){
				if(session!=null && session.getAttribute("directFromManufacturer")!=null && (LinkedHashMap<Integer, String>)session.getAttribute("directFromManufacturer")!=null){
					directFromManufacturer = (LinkedHashMap<Integer, String>)session.getAttribute("directFromManufacturer");
				}else{
					directFromManufacturer = CommonUtility.sortHashMapByValuesIntegerString(ProductsDAO.getManufacturerCustomFieldDetailsFromView("DIRECT_FROM_MANUFACTURER"));
				}
				session.setAttribute("directFromManufacturer", directFromManufacturer);	
			}
			
		
			categMenu = MenuAndBannersDAO.getTopMenuListBySubset().get("PH_SEARCH_"+subsetId);
			if(categMenu==null || manufacturerListInfo==null || brandListInfo==null)
			{
				MenuAndBannersDAO.getHeaderMenu();
				categMenu = MenuAndBannersDAO.getTopMenuListBySubset().get("PH_SEARCH_"+subsetId);
				manufacturerListInfo = MenuAndBannersDAO.getAllManufactuerData().get("PH_SEARCH_"+subsetId);
				brandListInfo = MenuAndBannersDAO.getAllBrandData().get("PH_SEARCH_"+subsetId+"_MV");
			}
			if(generalSubset>0) {
				if(brandListInfo!=null && brandListInfo.size()>0) {
					brandListInfo.addAll(MenuAndBannersDAO.getAllBrandData().get("PH_SEARCH_"+ generalSubset+"_MV"));	
				}
				else {
					brandListInfo = MenuAndBannersDAO.getAllBrandData().get("PH_SEARCH_"+ generalSubset+"_MV");
				}	
			if(categMenu!=null) {
			categMenu.addAll(MenuAndBannersDAO.getTopMenuListBySubset().get("PH_SEARCH_"+ generalSubset));
			}
			else {
				categMenu = MenuAndBannersDAO.getTopMenuListBySubset().get("PH_SEARCH_"+ generalSubset);	
			}
			for (int i = 0; i < categMenu.size(); i++) {
		        for (int j = i + 1; j < categMenu.size(); j++) {
		            if (categMenu.get(i).getCategoryName().equalsIgnoreCase(categMenu.get(j).getCategoryName())) {
		            	categMenu.remove(j);;
		                j--;
		            }
		        }
		    }
        }
			
			
			session.setAttribute("brandList", brandListInfo);	
			session.setAttribute("manufacturerList", manufacturerListInfo);	
			
			newsContent = UsersDAO.newsContent();
			   
			   topStaticMenu = new ArrayList<MenuAndBannersModal>();
			   topStaticMenu = MenuAndBannersDAO.getTopStaticMenu();
			   footerStaticMenu = new ArrayList<MenuAndBannersModal>();
			   footerStaticMenu = MenuAndBannersDAO.getFooterStaticMenu();
	
			System.out.println("Session Out : " + session.getMaxInactiveInterval());
			loginUserId=sessionUserId;
			
			if(context!=null){
				context.put("userName", userName);
				context.put("userId", userId);
				if(userId>1){
					context.put("isLoggedIn", "Y");
					context.put("afterLoginAvaiabilityProductList",CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_AVAILABILITY_PRODUCT_LISTING"));
				}else{
					context.put("beforeLoginAvaiabilityProductList",CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_AVAILABILITY_PRODUCT_LISTING"));
					context.put("isLoggedIn", "N");
				}
				context.put("userSubsetId", subsetId);
				context.put("generalSubsetId", generalSubset);
				context.put("slUserName", slUserName);
				context.put("checkOciUser", checkOciUser);
				context.put("ociSession", ociSession);
				context.put("isEclipse", isEclipse);
				context.put("bannerMessage", bannerMessage);
				context.put("cartCount", validateResponse);
				context.put("cartItemQuantitySum", cartItemQuantitySum);
				context.put("manufacturerIndex", manufacturerIndex);
				//context.put("manufacturerList", manufacturerList);
				//context.put("brandList", brandList);
				context.put("brandList", brandListInfo);
				context.put("manufacturerList", manufacturerListInfo);
				context.put("selectedManufacturerList", selectedManufacturerList);
				context.put("selectedBrandList", selectedBrandList);
				context.put("directFromManufacturer", directFromManufacturer);
				//--context.put("userTopTab", userTopTab);
				context.put("categMenu", categMenu);
				context.put("secondLevelMenu", secondLevelMenu);
				context.put("thirdLevelMenu", thirdLevelMenu);
				//context.put("brandListInfo", brandListInfo);
				context.put("brandListInfo", session.getAttribute("brandListInfo"));
				context.put("manufacturerListInfo", session.getAttribute("manufacturerListInfo"));
				context.put("newsContent", newsContent);
				context.put("topStaticMenu", topStaticMenu);
				context.put("footerStaticMenu", footerStaticMenu);
				context.put("headerStaticMenu", MenuAndBannersDAO.getHeaderStaticMenu());
				context.put("leftStaticMenu", MenuAndBannersDAO.getLeftStaticMenu());
				context.put("retainUserNameCase", CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE"));
				context.put("submitOrderForZeroAvailability", CommonDBQuery.getSystemParamtersList().get("SUBMIT_ORDER_FOR_ZERO_AVAILABILITY"));
				context.put("isPaymentGateway",CommonDBQuery.getSystemParamtersList().get("ENABLE_PAYMENT_GATEWAY"));
				context.put("isAjaxPriceLoad",CommonDBQuery.getSystemParamtersList().get("AJAX_PRICE_LOAD"));
				context.put("isDisablePriceDisplay",CommonDBQuery.getSystemParamtersList().get("DISABLE_DISPLAY_PRICING"));
				if(CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")!=null && CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS").trim().length()>0){
					context.put("webAddress",CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
				}
				
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AFTER_REGISTRATION_REDIRECT_PAGE")).length()>0){
					context.put("afterRegistrationPage",CommonDBQuery.getSystemParamtersList().get("AFTER_REGISTRATION_REDIRECT_PAGE"));
				}
				
				if(context.get("attrFilterData")==null && CommonDBQuery.getSystemParamtersList().get("ENABLE_MULTIFILTER")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_MULTIFILTER").trim().equalsIgnoreCase("Y")){
					 HashMap<String, ArrayList<ProductsModel>> categoryData = new HashMap<String, ArrayList<ProductsModel>>();
					 if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y")){
				 		 categoryData = ProductHunterSolr.taxonomyList("", subsetId, generalSubset, 1, 100, "NAVIGATION", varPsid, 0, CommonUtility.validateNumber("0"), CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,0,"",0,"");
				 		 //taxonomyLevelFilterData = categoryData.get("categeoryList");
				 		 attrFilterData = categoryData.get("attrList");
				 		 context.put("attrFilterData", attrFilterData);
				 	 }
					
				}
				
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				//if(CommonDBQuery.getSystemParamtersList().get("CART_TOTAL")!=null && CommonDBQuery.getSystemParamtersList().get("CART_TOTAL").trim().equalsIgnoreCase("Y")){
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCartTotalDisplay")).equalsIgnoreCase("Y")){
					contentObject = ProductsDAO.getCartTotal(session, contentObject);
					context.put("cartCountTotal", contentObject.get("cartTotal"));
					//context.put("cartItemsList", contentObject.get("productListData"));
				}
				
				if(session.getAttribute("DFMMode")!=null && CommonUtility.validateString(session.getAttribute("DFMMode").toString()).equalsIgnoreCase("Y")){
					   ProductsModel cartCaseCountModel = ProductsDAO.getCartCaseCount(userId,subsetId,generalSubset);
					   if(cartCaseCountModel!=null){
					   		context.put("cartItemCount",cartCaseCountModel.getCartItemCount());
					   		context.put("cartItemQuantityCount",cartCaseCountModel.getCartIndividualItemQuantitySum());
					   		context.put("cartMinOrderQtyCount",cartCaseCountModel.getMinOrderQty());
					   		context.put("cartCaseCount",cartCaseCountModel.getCartCaseCount());
					   		context.put("cartItemWeightTotal",cartCaseCountModel.getWeight());
					   		context.put("cartItemUnitsCount",cartCaseCountModel.getUnits());
					   }
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return context;
	}
	
	public String BrandList(){
		target = "ResultLoader";
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			String brandAlpha = request.getParameter("idx");
			String resultString = "";
			if(CommonUtility.validateString(brandAlpha).length()>0 && brandAlpha.contains("|")){
				String[] alphaArray = brandAlpha.split("|");
				if(alphaArray!=null && alphaArray.length>0){
					resultString = "";
					for(String singleAlpha : alphaArray){
						if(CommonUtility.validateString(singleAlpha).length()>0 && !CommonUtility.validateString(singleAlpha).equalsIgnoreCase("|")){
							brandMenu = ProductsDAO.brandList( CommonUtility.validateString(singleAlpha), subsetId, generalSubset);
							String deLimit = "";
							for(ProductsModel brandNm : brandMenu){
								resultString = resultString+deLimit+brandNm.getBrandName()+"|"+brandNm.getBrandId()+"|"+CommonUtility.validateString(singleAlpha);
								deLimit="~";
							}
						}
					}
				}
			}else{
				brandMenu = ProductsDAO.brandList( brandAlpha, subsetId, generalSubset);
				String deLimit = "";
				resultString = "";
				for(ProductsModel brandNm : brandMenu){
					resultString = resultString+deLimit+brandNm.getBrandName()+"|"+brandNm.getBrandId()+"|"+brandAlpha;
					deLimit="~";
				}
			}
			renderContent = resultString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;

	}
	public String ManufacturerList(){
		target = "ResultLoader";
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			String manfAlpha = request.getParameter("idx");
			String resultString = "";
			
			if(CommonUtility.validateString(manfAlpha).length()>0 && manfAlpha.contains("|")){
				String[] alphaArray = manfAlpha.split("|");
				if(alphaArray!=null && alphaArray.length>0){
					resultString = "";
					for(String singleAlpha : alphaArray){
						if(CommonUtility.validateString(singleAlpha).length()>0 && !CommonUtility.validateString(singleAlpha).equalsIgnoreCase("|")){
							manufacturerMenu = ProductsDAO.getManufacturerList( CommonUtility.validateString(singleAlpha), subsetId, generalSubset);
							String deLimit = "";
							for(ProductsModel manfNm : manufacturerMenu){
								resultString = resultString+deLimit+manfNm.getManufacturerName()+"|"+manfNm.getManufacturerId()+"|"+CommonUtility.validateString(singleAlpha);
								deLimit="~";
							}
						}
					}
				}
			}else{
				manufacturerMenu = ProductsDAO.getManufacturerList( manfAlpha, subsetId, generalSubset);
				String deLimit = "";
				for(ProductsModel manfNm : manufacturerMenu){
					resultString = resultString+deLimit+manfNm.getManufacturerName()+"|"+manfNm.getManufacturerId()+"|"+manfAlpha;
					deLimit="~";
				}
			}
			renderContent = resultString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}
	
	//----------------------------- New
	public String getUserIP(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String ipaddress = request.getHeader("X-Forwarded-For");
			String remoteAddress = request.getRemoteAddr();
			String remoteHost = request.getRemoteHost();
			String remoteUser = request.getRemoteUser();
			String localAddress = request.getLocalAddr();
			System.out.println("----------------------------- User Access Information ----------------------------");
			if(session.getAttribute(Global.USERID_KEY)==null)
			System.out.println("No User");
			else
				System.out.println((String) session.getAttribute(Global.USERID_KEY));
			if (ipaddress  == null)
				ipaddress = request.getRemoteAddr();
			System.out.println("Login Accessed : " + ipaddress);
			System.out.println("Remote Address : " + remoteAddress);
			System.out.println("Remote Host : " + remoteHost);
			System.out.println("Remote User : " + remoteUser);
			System.out.println("Local Address : " + localAddress);
			System.out.println("----------------------------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String checkCartItems(){	
		target = "ResultLoader";
	    request =ServletActionContext.getRequest();
		
		try{
			HttpSession session = request.getSession();
			String productCode = request.getParameter("partNumber");
			String tempQty = request.getParameter("qty");
			String ipaddress = request.getHeader("X-Forwarded-For");
			String uom = (String) request.getParameter("uom");
			if (ipaddress  == null)
				ipaddress = request.getRemoteAddr();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			ArrayList<Integer> cartItems = new ArrayList<Integer>();
			
			System.out.println("checkCartItems partNumber : "+partNumber);	
			int existingQty = 0;
			int userId = 0;
			if(sessionUserId!=null && sessionUserId.trim().length()>0){
				userId = CommonUtility.validateNumber(sessionUserId);
			}
			if(userId>1){
				cartItems = ProductsDAO.selectFromCart(CommonUtility.validateNumber(sessionUserId), CommonUtility.validateNumber(productIdList),uom);
			}else{
				cartItems = ProductsDAO.selectFromCartBeforeLogin(session.getId(), CommonUtility.validateNumber(productIdList),uom);
			}
			
			if(cartItems!=null && cartItems.size()>0)
			{
				System.out.println("Cart contains Duplicate Items. Do You Want To Add Them Again. |");
				validateResponse = -1;
				setPartNumber(productCode);
				errorMsg = "Item Already Exist.";
				itemQty = CommonUtility.validateNumber(tempQty);
				existingQty = cartItems.get(1);
			}
			else
			{
				validateResponse = 0;
				setPartNumber(productCode);
				errorMsg = "Item Not Exist.";
				itemQty = CommonUtility.validateNumber(tempQty);
			}
			
			renderContent = validateResponse+"|"+itemPriceId+"|"+errorMsg+"||"+partNumber+"|"+existingQty;
			
		}
		 
		catch (Exception e) {
	                  e.printStackTrace();
		}
	
		return target;
	}
	public String addToCartV2(){
		target = "ResultLoader";
		String mobRequestType = "";
		String jsonStringToMobileGson = "Issue while Processing request";
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			LinkedHashMap<Integer,Integer> cartExistDetail = new LinkedHashMap<Integer,Integer>();
			mobRequestType = CommonUtility.validateString(request.getParameter("reqType"));
			String ipaddress = request.getHeader("X-Forwarded-For");
			String isGeneralUser = CommonUtility.validateString((String)session.getAttribute("isGeneralUser"));
			String isAuthPurchaseAgent = CommonUtility.validateString((String)session.getAttribute("isAuthPurchaseAgent"));
			String jsonData = (String)request.getParameter("jsonData");
			int userId = 0;
			int addressBookId = 0;
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			Gson parseInput = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			ProductsModel  parsedData = parseInput.fromJson(jsonData,ProductsModel.class);
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			sessionId  = session.getId();
			int generalSubsetId = CommonUtility.validateNumber(tempGeneralSubset);
			session.removeAttribute("vendorSpecificPurchaseMsg");
			if (ipaddress  == null){
				ipaddress = request.getRemoteAddr();
			}
			if(sessionUserId!=null && sessionUserId.trim().length()>0){
				userId = CommonUtility.validateNumber(sessionUserId);
			}
			String shipViaCode = "";
			if(session!=null && session.getAttribute("selectedShipVia")!=null){
				shipViaCode = (String) session.getAttribute("selectedShipVia");
			}
			boolean isAddToCart = false;
			boolean isMyItem = false;
			LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
			utilityMap.put("considerLineItemComment", false);
			int count = 0;
			String cartCountTotal = "0.00";
			DecimalFormat df = CommonUtility.getPricePrecision(session);
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")).equalsIgnoreCase("Y")){
				if(!CommonUtility.validateString(session.getAttribute("userToken").toString()).equalsIgnoreCase("") && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("defaults")){
					String homeTerritory = (String) session.getAttribute("shipBranchId");
					String entityId = (String) session.getAttribute("entityId");
					ProductManagement priceInquiry = new ProductManagementImpl();
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					priceInquiryInput.setHomeTerritory(homeTerritory);
					priceInquiryInput.setPartIdentifier(parsedData.getItemDataList());
					priceInquiryInput.setRequiredAvailabilty("Y");
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					parsedData.setItemDataList(priceInquiry.priceInquiry(priceInquiryInput , parsedData.getItemDataList()));
				}
			}
			
			ArrayList<Integer> eachItemList = new ArrayList<Integer>();
			LinkedHashMap<String, String> customerCustomField = (LinkedHashMap<String, String>)session.getAttribute("customerCustomFieldValue");
			String myItemView = "N";
			if ((customerCustomField != null) && (CommonUtility.validateString((String)customerCustomField.get("MY_ITEMS_VIEW")).equalsIgnoreCase("Y"))) {
			 
				myItemView = (String)customerCustomField.get("MY_ITEMS_VIEW");
			    }
			LinkedHashMap<String, ProductsModel> myItemListData = (LinkedHashMap<String, ProductsModel>)session.getAttribute("myItemListData");
			for(ProductsModel eachItem:parsedData.getItemDataList()){
				isAddToCart = false;
				count = 0;
				ArrayList<Integer> cartItems = new ArrayList<Integer>();
				eachItemList.add(eachItem.getItemId());
				eachItem.setShipViaCode(shipViaCode);
				if(userId>1){
					//Adapt Service Start
					if(CommonUtility.customServiceUtility() != null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MultipleShipToAddressCartAP")).equalsIgnoreCase("Y")) {
						addressBookId = eachItem.getBcAddressBookId();
						if(addressBookId > 0) {
							utilityMap.put("addToMultipleShippingAddress", addressBookId);
							cartItems = ProductsDAO.selectFromCartAP(CommonUtility.validateNumber(sessionUserId), eachItem.getItemId(),eachItem.getUom(),addressBookId);
						} else {
							cartItems = ProductsDAO.selectFromCart(CommonUtility.validateNumber(sessionUserId), eachItem.getItemId(),eachItem.getUom());
						}
					//Adapt Service End
					} else if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCSPConfigurator")).equalsIgnoreCase("Y") && eachItem.getDesignId() <= 0){
						cartItems = ProductsDAO.selectFromCart(CommonUtility.validateNumber(sessionUserId), eachItem.getItemId(),eachItem.getUom());
					}
				}else{
					cartItems = ProductsDAO.selectFromCartBeforeLogin(session.getId(), eachItem.getItemId(),eachItem.getUom());
				}
				if(cartItems!=null && cartItems.size()>0 && CommonUtility.validateString(eachItem.getRequestType()).equalsIgnoreCase("")){
					cartExistDetail.put(cartItems.get(2), cartItems.get(1));		
					eachItem.setStatus("EXIST");
				}else{
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("QTY_INTERVALSFROM_ERP")).equalsIgnoreCase("Y")){
						eachItem.setMinimumOrderQuantity(eachItem.getMinimumOrderQuantity()!=0?eachItem.getMinimumOrderQuantity():1);
						eachItem.setOrderInterval(eachItem.getOrderInterval()!=0?eachItem.getOrderInterval():1);
					}else{
						eachItem = ProductsDAO.getMinOrderQty(eachItem);
					}
					int itemId = eachItem.getItemId();
					if ((!myItemView.equalsIgnoreCase("Y")) || ((myItemView.equalsIgnoreCase("Y")) && ((isGeneralUser.equalsIgnoreCase("Y")) || (isAuthPurchaseAgent.equalsIgnoreCase("Y"))) && (myItemListData != null) && (myItemListData.containsKey(Integer.valueOf(itemId)))) || ((myItemView.equalsIgnoreCase("Y")) && (!isGeneralUser.equalsIgnoreCase("Y")) && (!isAuthPurchaseAgent.equalsIgnoreCase("Y"))))
					{
						if(eachItem.getMinimumOrderQuantity()>0){
						if(eachItem.getQty() == eachItem.getMinimumOrderQuantity()){
							isAddToCart = true;
						}else if(eachItem.getQty() > eachItem.getMinimumOrderQuantity()){
							double qtyDiff = eachItem.getQty() - eachItem.getMinimumOrderQuantity();
							if(qtyDiff%eachItem.getOrderInterval()==0){
								isAddToCart = true;
							}
						}
					}else{
						isAddToCart = true;
					}
					if(isAddToCart){
						session.removeAttribute("quoteNumber");
						session.removeAttribute("orderNumber");
						if(CommonUtility.validateString(eachItem.getRequestType()).equalsIgnoreCase("update") && cartItems!=null && cartItems.size()>0){
							count = ProductsDAO.updateCart(userId,  cartItems.get(0), eachItem.getQty(), cartItems.get(1),"","",utilityMap);
						}else{
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCSPConfigurator")).equalsIgnoreCase("Y") && eachItem.getDesignId() > 0) {
								utilityMap.put("designId", eachItem.getDesignId());
							}
							utilityMap.put("priceToCart", ""+eachItem.getPrice());
							utilityMap.put("availabilityToCart", CommonUtility.validateParseIntegerToString(eachItem.getAvailQty()));
							if(CommonUtility.validateString(eachItem.getGetPriceFrom()).length() > 0) {
								utilityMap.put("getPriceFrom", eachItem.getGetPriceFrom());
							}
							//in below line eachItem.getUnitPrice() replace by eachItem.getPrice() to store item price in cart
							count = ProductsDAO.insertItemToCart(userId,eachItem.getItemId(), eachItem.getQty(), sessionId,null, eachItem.getCatalogId(),eachItem.getUom(),""+eachItem.getPrice(), eachItem.getMinOrderQty(), eachItem.getWeight(), eachItem.getUnits(),utilityMap);
						}
						ProductsDAO.updatePopularity(eachItem.getItemId(), 2);
						ProductsDAO.updateHits(eachItem.getItemId());
						UsersDAO.updateUserLog(userId, "Add To Cart", session.getId(), ipaddress, "Click", ""+eachItem.getItemId());
						if(count>0){
							eachItem.setStatus("Y");
							session.removeAttribute("cartCountSession");
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID")).length()>0){
								String[] brandIDList = CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID").split(",");
								boolean isExcludeBranch = false;
								if(brandIDList!=null){
									for(String brandId:brandIDList){
										isExcludeBranch = ProductsDAO.getCartBrandExcludeStatus(userId,sessionId,Integer.parseInt(brandId),"cart");
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
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_BRONTO_ABANDONED_CART_EMAIL")).equalsIgnoreCase("Y") && userId>2){
								System.out.println("Adding entry to abandoned cart table...");
								BrontoDAO.addAbandonedCart(subsetId, userId, sessionId, "N");
							}
						}else{
							eachItem.setStatus("Something Went Wrong. Please Try Again");
						}
					}
					}
					
					else{
						if(isMyItem == false ){
							eachItem.setStatus("Item cannot be added to cart. Please order from My Items List");
						} else {
						eachItem.setStatus("Item has to be ordered in multiples of "+eachItem.getOrderInterval()+" and Min. Order Quantity is : "+eachItem.getMinimumOrderQuantity());
						}
					}
				}
			}
			
			if(eachItemList.size()>0){
				LinkedHashMap<Integer, ProductsModel> finalData = new LinkedHashMap<Integer, ProductsModel>(); 
				ArrayList<ProductsModel> itemDetailData = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubsetId, StringUtils.join(eachItemList," OR "),0,null,"itemid");	
				for(ProductsModel eachSearchedData:itemDetailData){																										
					finalData.put(eachSearchedData.getItemId(), eachSearchedData);
				}
				contentObject.put("itemDetailData",finalData);
			}
			if(CommonUtility.validateString(request.getParameter("getCustomFields")).equals("Y")){
				if(eachItemList!=null && eachItemList.size()>0){
					LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubsetId, StringUtils.join(eachItemList," OR "),"itemid");
					contentObject.put("customFieldVal", customFieldVal);
				}
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCartTotalDisplay")).equalsIgnoreCase("Y")){
				contentObject = ProductsDAO.getCartTotal(session, contentObject);
				double cartTotal = (Double) contentObject.get("cartTotal");
				cartCountTotal = df.format(cartTotal);
				//context.put("cartItemsList", contentObject.get("productListData"));
			}
			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT")).length() > 0 && CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT").trim().equalsIgnoreCase("Y"))	
			{
    		  ProductsDAO.resetCartCheckoutType(userId);
    		  session.setAttribute("displayCheckoutCondMsgFlag","Y");
    	    }
			contentObject.put("productListData", parsedData.getItemDataList());
			contentObject.put("cartCountTotal", cartCountTotal);
			contentObject.put("responseType", "MultipleAddToCart");
			contentObject.put("cartExistDetail", cartExistDetail);
			
			if(contentObject!=null) {
				jsonStringToMobileGson = new Gson().toJson(contentObject, Map.class);
				System.out.println("jsonStringToMobileGson: "+CommonUtility.validateString(jsonStringToMobileGson));
			}
			if(request.getHeader("User-Agent").equalsIgnoreCase("WEBVIEW") && CommonUtility.validateString(mobRequestType).equalsIgnoreCase("OFFLINECART")) {
				renderContent = jsonStringToMobileGson;
			}else {
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
			}
		}catch (Exception e) {
	         e.printStackTrace();
		}
		return target;
	}
	public String addToCart(){
		target = "ResultLoader";
		try{
		    request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String ipaddress = request.getHeader("X-Forwarded-For");
			if(ipaddress  == null){
				ipaddress = request.getRemoteAddr();
			}
			String isGeneralUser = CommonUtility.validateString((String)session.getAttribute("isGeneralUser"));
			String isAuthPurchaseAgent = CommonUtility.validateString((String)session.getAttribute("isAuthPurchaseAgent"));
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			String sessionId = session.getId();
			String tempQty = qty;
			String update =  request.getParameter("update");
			String catalogId = (String) request.getParameter("catalogId");
			String uom = (String) request.getParameter("uom");
			String price = (String) request.getParameter("price");
			String availability = (String) request.getParameter("availbility");
			String getPriceFrom = (String) request.getParameter("getPriceFrom");
			int userId = CommonUtility.validateNumber(sessionUserId);
			int qty = CommonUtility.validateNumber(tempQty);
			int minOrderQtyToCart = 1;
			double itemWeight = 0.0;
			double itemUnit = 0.0;
			int count = 0;
			boolean isAddToCart = false;
			double unitPrice = 0;
			boolean considerLineItemComment = false;
			String cartCountTotal = "0.00";
			int availableItemQty = 0;
			String itemLevelShipvia="";
			String itemLevelShipviaDesc="";
			LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
			utilityMap.put("considerLineItemComment", considerLineItemComment);
			utilityMap.put("priceToCart", CommonUtility.validateString(price));
			utilityMap.put("availabilityToCart", CommonUtility.validateString(availability));
			utilityMap.put("getPriceFrom", CommonUtility.validateString(getPriceFrom));
			
			session.removeAttribute("vendorSpecificPurchaseMsg");
			try {
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
					 itemLevelShipvia=request.getParameter("itemLevelShipvia");
					 itemLevelShipviaDesc=request.getParameter("itemLevelShipviaDesc");
				}
				if(CommonUtility.validateString(request.getParameter("itmWeight")).length()>0){
					itemWeight = CommonUtility.validateDoubleNumber(CommonUtility.validateString(request.getParameter("itmWeight")));
				}
				if(CommonUtility.validateString(request.getParameter("itemUnits")).length()>0){
					itemUnit = CommonUtility.validateDoubleNumber(CommonUtility.validateString(request.getParameter("itemUnits")));
				}
				
				DecimalFormat df = CommonUtility.getPricePrecision(session);
				System.out.println("addToCart partNumber : "+partNumber);	
				ProductsModel minOrderQty = new ProductsModel();
				String eclipseStatus = (String)session.getAttribute("connectionError");
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("QTY_INTERVALSFROM_ERP")).equalsIgnoreCase("Y")){
					minimumOrderQty = 1;
					orderQtyInterval = 1;
					String tempMinOrdQty = request.getParameter("minOrdQty");
					String tempOrdQtyInter = request.getParameter("ordQtyInter");
					if(tempMinOrdQty!=null && tempMinOrdQty.trim().length()>0){
						minimumOrderQty = CommonUtility.validateNumber(tempMinOrdQty);
					}
					if(tempOrdQtyInter!=null && tempOrdQtyInter.trim().length()>0){
						orderQtyInterval = CommonUtility.validateNumber(tempOrdQtyInter);
					}
					minOrderQty.setMinOrderQty(minimumOrderQty);
					minOrderQty.setOrderInterval(orderQtyInterval);
				}else{
					//Latest
					String shipViaCode = "";
					if(session!=null && session.getAttribute("selectedShipVia")!=null){
						shipViaCode = (String) session.getAttribute("selectedShipVia");
					}
					ProductsModel productInput = new ProductsModel();
					productInput.setItemPriceId(itemPriceId);
					productInput.setShipViaCode(shipViaCode);
					minOrderQty = ProductsDAO.getMinOrderQty(productInput);
					//Latest
					//minOrderQty = ProductsDAO.getMinOrderQty(itemPriceId);
				}
				LinkedHashMap<String, String> customerCustomField = (LinkedHashMap<String, String>)session.getAttribute("customerCustomFieldValue");
				String myItemView = "N";
				 if ((customerCustomField != null) && (CommonUtility.validateString((String)customerCustomField.get("MY_ITEMS_VIEW")).equalsIgnoreCase("Y"))) {
			     
			    	  myItemView = (String)customerCustomField.get("MY_ITEMS_VIEW");	
			      }
				 LinkedHashMap<String, ProductsModel> myItemListData = (LinkedHashMap<String, ProductsModel>)session.getAttribute("myItemListData");
					// if ((!CommonUtility.validateString((String)customerCustomField.get("MY_ITEMS_VIEW")).equalsIgnoreCase("Y")) || ((CommonUtility.validateString((String)customerCustomField.get("MY_ITEMS_VIEW")).equalsIgnoreCase("Y")) && (CommonUtility.validateString(isGeneralUser).equalsIgnoreCase("Y")) && (myItemListData != null) && (myItemListData.containsKey(this.productIdList))) || ((CommonUtility.validateString((String)customerCustomField.get("MY_ITEMS_VIEW")).equalsIgnoreCase("Y")) && (!CommonUtility.validateString(isGeneralUser).equalsIgnoreCase("Y")))) {
				if ((!myItemView.equalsIgnoreCase("Y")) || ((myItemView.equalsIgnoreCase("Y")) && ((isGeneralUser.equalsIgnoreCase("Y")) || (isAuthPurchaseAgent.equalsIgnoreCase("Y"))) && (myItemListData != null) && (myItemListData.containsKey(this.productIdList))) || ((myItemView.equalsIgnoreCase("Y")) && (!isGeneralUser.equalsIgnoreCase("Y")) && (!isAuthPurchaseAgent.equalsIgnoreCase("Y")))) {

				if(minOrderQty!=null && minOrderQty.getMinOrderQty()>0){
					minOrderQtyToCart = minOrderQty.getMinOrderQty();
					if(qty == minOrderQty.getMinOrderQty()){
						isAddToCart = true;
					}else if(qty > minOrderQty.getMinOrderQty()){
						int qtyDiff = qty - minOrderQty.getMinOrderQty();
						if(qtyDiff%minOrderQty.getOrderInterval()==0){
							isAddToCart = true;
						}
					}
				}else{
					isAddToCart = true;
				}
				if(isAddToCart){
					//count = ProductorItemUtilityDao.addToCart(sessionId, userId, qty, productIdList);
					session.removeAttribute("quoteNumber");
					session.removeAttribute("orderNumber");
					ArrayList<Integer> cartId = new ArrayList<Integer>();
					if(userId>1){
						//Adapt Service Start
						if(CommonUtility.customServiceUtility() != null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MultipleShipToAddressCartAP")).equalsIgnoreCase("Y")) {
							int addressBookId = CommonUtility.validateNumber((String)session.getAttribute("defaultShipToId"));
							if(addressBookId > 0) {
								utilityMap.put("addToMultipleShippingAddress", addressBookId);
								cartId = ProductsDAO.selectFromCartAP(CommonUtility.validateNumber(sessionUserId), CommonUtility.validateNumber(productIdList), uom, addressBookId);
							} else {
								cartId = ProductsDAO.selectFromCart(userId, CommonUtility.validateNumber(productIdList), uom);
							} //Adapt Service End
						} else {
							cartId = ProductsDAO.selectFromCart(userId, CommonUtility.validateNumber(productIdList), uom);
						}

						if(cartId.size() > 1){
							availableItemQty = cartId.get(1);
						}
						
					}else{
						cartId = ProductsDAO.selectFromCartSession(userId, CommonUtility.validateNumber(productIdList), sessionId,uom);
						if(cartId.size() > 1){
							availableItemQty = cartId.get(1);
						}
					}
					if(cartId!=null && cartId.size()>0){
						if(update==null){
							//count = ProductsDAO.insertItemToCart(userId, CommonUtility.validateNumber(productIdList), qty, sessionId,null);
							count = ProductsDAO.insertItemToCart(userId, CommonUtility.validateNumber(productIdList), qty, sessionId,null, catalogId,uom,price,minOrderQtyToCart, itemWeight, itemUnit, utilityMap);
						}else{
							count = ProductsDAO.updateCart(userId,  cartId.get(0), qty, cartId.get(1),"","", utilityMap);
						}
					}else{
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
							String additionalProperties = CommonUtility.validateString(request.getParameter("additionalProperties"));
							count = ProductsDAO.insertIntoCartWithShipVia(userId, CommonUtility.validateNumber(productIdList), qty, sessionId,null,catalogId,uom,price,minOrderQtyToCart, itemWeight, itemUnit,itemLevelShipvia,itemLevelShipviaDesc, utilityMap, additionalProperties);
						if(itemLevelShipvia.contains("STORE")) {
							session.setAttribute("shipViaFlag","Y");
						}
						}else {
						count = ProductsDAO.insertItemToCart(userId, CommonUtility.validateNumber(productIdList), qty, sessionId,null,catalogId,uom,price,minOrderQtyToCart, itemWeight, itemUnit,utilityMap);
						}
					}
					ProductsDAO.updatePopularity(CommonUtility.validateNumber(productIdList), 2);
					ProductsDAO.updateHits(CommonUtility.validateNumber(productIdList));
					UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Add To Cart", session.getId(), ipaddress, "Click", productIdList);
					if(count>0){
						session.removeAttribute("cartCountSession");
						VelocityContext contextTemp = null; 
						getCartCount( contextTemp, request);
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID")).length()>0){
							String[] brandIDList = CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID").split(",");
							boolean isExcludeBranch = false;
							if(brandIDList!=null){
								for(String brandId:brandIDList){
									isExcludeBranch = ProductsDAO.getCartBrandExcludeStatus(userId,sessionId,Integer.parseInt(brandId),"cart");
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
					}else if(eclipseStatus!=null && eclipseStatus.trim().equalsIgnoreCase("Yes")){
						validateResponse = -1;
					}
					try{
						if(qty > 1  && CommonUtility.validateString(partNumber).length()>0  && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")).equalsIgnoreCase("Y")){
							if(session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("defaults")){
								String homeTerritory = (String) session.getAttribute("shipBranchId");
								String entityId = (String) session.getAttribute("entityId");
								ArrayList<ProductsModel> itemDetailObject = new ArrayList<ProductsModel>();
								ProductsModel productsModel = new ProductsModel(); 
								ArrayList<ProductsModel> productsInput = new ArrayList<ProductsModel>();
								ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
								ProductsModel eachProduct = new ProductsModel();
								eachProduct.setPartNumber(partNumber);
								eachProduct.setUom(uom);
								eachProduct.setQty(qty);
								productsInput.add(eachProduct);
								productsModel.setAltPartNumber1(altPartNumber);
								partIdentifierQuantity.add(qty);
								productsModel.setPartNumber(partNumber);
								productsModel.setQty(qty);
								itemDetailObject.add(productsModel);
								ProductManagement priceInquiry = new ProductManagementImpl();
								ProductManagementModel priceInquiryInput = new ProductManagementModel();
								priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
								priceInquiryInput.setHomeTerritory(homeTerritory);
								priceInquiryInput.setPartIdentifier(productsInput);
								priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
								priceInquiryInput.setRequiredAvailabilty("Y");
								priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
								priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
								priceInquiryInput.setSession(session);
								itemDetailObject = priceInquiry.priceInquiry(priceInquiryInput , itemDetailObject);
								unitPrice = itemDetailObject.get(0).getUnitPrice();
								
									
								
							}
						}
						LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
						//if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CART_TOTAL")).equalsIgnoreCase("Y")){
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCartTotalDisplay")).equalsIgnoreCase("Y")){
							contentObject = ProductsDAO.getCartTotal(session, contentObject);
							double cartTotal = (Double) contentObject.get("cartTotal");
							cartCountTotal = df.format(cartTotal);
							//context.put("cartItemsList", contentObject.get("productListData"));
						}
					}catch(Exception e){
						unitPrice = 0;
						e.printStackTrace();
					}
				}else{
					errorMsg = "Item has to be ordered in multiples of "+minOrderQty.getOrderInterval()+" and Min. Order Quantity is : "+minOrderQty.getMinOrderQty();
					validateResponse = -1;
				 }
				}
				else{
					errorMsg = "Item cannot be added to cart. Please order from My Items List";
					validateResponse = -2;
				}


				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_BRONTO_ABANDONED_CART_EMAIL")).equalsIgnoreCase("Y") && userId>2){
					String tempSubset = (String) session.getAttribute("userSubsetId");
					System.out.println("Adding entry to abandoned cart table...");
					int subsetId = CommonUtility.validateNumber(tempSubset);
					BrontoDAO.addAbandonedCart(subsetId, userId, sessionId, "N");
				}
				int totalQtyAvailable = 0;
				if(update!=null && update.equalsIgnoreCase("update")){
					totalQtyAvailable = (qty+availableItemQty);
				}
				else{
					totalQtyAvailable = qty;
				}
				if(CommonUtility.customServiceUtility() != null) {
					totalQtyAvailable = CommonUtility.customServiceUtility().updateCartCountWithSumOfQty(totalQtyAvailable,session);
				}
				
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT")).length() > 0 && CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT").trim().equalsIgnoreCase("Y"))	
    			{
        		  ProductsDAO.resetCartCheckoutType(userId);
        		  session.setAttribute("displayCheckoutCondMsgFlag","Y");
        	    }
				renderContent = validateResponse+"|"+savedGroupId+"|"+errorMsg+"|"+unitPrice+"|"+cartCountTotal+"|"+totalQtyAvailable;
			}catch(Exception e){
		    	e.printStackTrace();
		    }finally{
		    	session.removeAttribute("cartCountSession");
		    }
		}catch (Exception e) {
	        e.printStackTrace();
		}
	   return target;
   }

	public String loadLinkedItems(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = Integer.parseInt(sessionUserId);
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			String customerId = (String) session.getAttribute("customerId");
			String customerCountry = (String) session.getAttribute("customerCountry");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String entityId = (String) session.getAttribute("entityId");
			String homeTerritory = (String) session.getAttribute("shipBranchId");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			linkedItemsList = new ArrayList<ProductsModel>();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			HashMap<String, ArrayList<ProductsModel>> itemDetailList = new HashMap<String, ArrayList<ProductsModel>>();
			String taxonomyId=String.valueOf(itemPriceId);
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableDetailPageFromSolr")).equalsIgnoreCase("Y")){
				
				itemDetailList = ProductHunterSolr.itemDetailData(taxonomyId, (String) session.getAttribute("userToken"),(String)session.getAttribute(Global.USERNAME_KEY),entityId,homeTeritory,userId,"",wareHousecode,customerId,customerCountry, generalSubset, subsetId);
			}else{
				
				itemDetailList = ProductsDAO.itemDetailData(taxonomyId, (String) session.getAttribute("userToken"),(String)session.getAttribute(Global.USERNAME_KEY),entityId,homeTeritory,userId,"",wareHousecode,customerId,customerCountry);
			}
			linkedItemsList = itemDetailList.get("linkedItems");
			if(linkedItemsList!=null && linkedItemsList.size()>0){
				linkedItemGroup = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				recommendedItemsList = new ArrayList<ProductsModel>();
				for(ProductsModel lnList:linkedItemsList){
					recommendedItemsList = linkedItemGroup.get(lnList.getLinkTypeName().trim());
					if(recommendedItemsList==null){
						recommendedItemsList = new ArrayList<ProductsModel>();
						recommendedItemsList.add(lnList);
						linkedItemGroup.put(lnList.getLinkTypeName().trim(), recommendedItemsList);
					}else{
						recommendedItemsList.add(lnList);
						linkedItemGroup.put(lnList.getLinkTypeName().trim(), recommendedItemsList);
					}
				}
				for(String key:linkedItemGroup.keySet()){
					ArrayList<Integer> eachItemList = new ArrayList<Integer>();
					recommendedItemsList = new ArrayList<ProductsModel>();
					recommendedItemsList = linkedItemGroup.get(key);
					for(ProductsModel eachGroupItem:recommendedItemsList){
						eachItemList.add(eachGroupItem.getItemId());
					}
					recommendedItemsList = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId, generalSubset, StringUtils.join(eachItemList," OR "),0,null,"itemid");
					for(ProductsModel eachGroupItem:recommendedItemsList){
						eachGroupItem.setLinkTypeName(key);
					}
					if(recommendedItemsList!=null && recommendedItemsList.size()>0){
						ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
						ArrayList<Integer> itemList = new ArrayList<Integer>();
						for(ProductsModel productsModel : recommendedItemsList){
							itemList.add(productsModel.getItemId());
							partIdentifierQuantity.add(productsModel.getQty());
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_LINKED_ITEMS_IN_GROUPS")).equalsIgnoreCase("Y")){
								productsModel.setLinkedItemsList(ProductsDAO.getLinkedItems(productsModel.getItemId(), subsetId));
							}
						}
						if((recommendedItemsList!=null && recommendedItemsList.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults")))
						{
							ProductManagement priceInquiry = new ProductManagementImpl();
							ProductManagementModel priceInquiryInput = new ProductManagementModel();
							priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
							priceInquiryInput.setHomeTerritory(homeTerritory);
							priceInquiryInput.setPartIdentifier(recommendedItemsList);
							priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
							priceInquiryInput.setRequiredAvailabilty("Y");
							priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
							priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
							priceInquiryInput.setSession(session);
							recommendedItemsList = priceInquiry.priceInquiry(priceInquiryInput , recommendedItemsList);
						}
					}
					linkedItemGroup.put(key, recommendedItemsList);
				}
			}
			contentObject.put("responseType", "loadLinkedItems");
			contentObject.put("linkedItemGroup", linkedItemGroup);
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ResultLoader";
	}
	
	
	public String addToRfqCart(){
		target = "ResultLoader";
		try{
		    request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String ipaddress = request.getHeader("X-Forwarded-For");
			if (ipaddress  == null) {
				ipaddress = request.getRemoteAddr();
			}
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			String sessionId = session.getId();
			String tempQty = request.getParameter("qty");
			int userId = Integer.parseInt(sessionUserId);
			int qty = Integer.parseInt(tempQty);
			int count = 0;
			//boolean isAddToCart = true;
			//if(isAddToCart){
				int ociUser = 0;
				if(session.getAttribute("isOciUser")!=null) {
					ociUser = (Integer) session.getAttribute("isOciUser");
				}
				if(ociUser>0) {
					count = ProductsDAO.addToRfqCartSession(sessionId, userId, qty, productIdList,itemPriceId);
				}else {
					count = ProductsDAO.addToRfqCart(sessionId, userId, qty, productIdList,itemPriceId);
				}
			    UsersDAO.updateUserLog(Integer.parseInt((String)session.getAttribute(Global.USERID_KEY)), "Add To Rfq", session.getId(), ipaddress, "Click", productIdList);
				if(count>0){
		    		renderContent = "1";
		    	}
			/*}else{
				renderContent = "-1";
			}*/
		}catch (Exception e) {
	        e.printStackTrace();
		}
		return target;
	}
	
	public String ReOrderCart(){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		target = "ShoppingCart";

		try {
			String ipaddress = request.getHeader("X-Forwarded-For");
			if (ipaddress  == null){
				ipaddress = request.getRemoteAddr();
			}
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			String sessionId = session.getId();
			//String update =  request.getParameter("update");
			String catalogId = (String) request.getParameter("catalogId");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			List<ProductsModel> itemsNotInCimm = new ArrayList<>();
			int userId = CommonUtility.validateNumber(sessionUserId);
			int count = 0;
			int subsetId = CommonUtility.validateNumber(tempSubset);
			int generalSubsetId = CommonUtility.validateNumber(tempGeneralSubset);
			
			if(ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"))>0){
				generalSubsetId = ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"));
	    	}
			LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
			utilityMap.put("considerLineItemComment", true);
			utilityMap.put("orderOrQuoteNumber", CommonUtility.validateString(request.getParameter("orderOrQuoteNumber")));
					
			
			session.removeAttribute("ReorderCartMessage");
			String reorderCartMessage = "";
			String propertyMainMessage = "";
			String reorderCartMessagebreake = "";
			if(quotePartNumberSelected!=null && quotePartNumberSelected.length > 0){
				ArrayList<SalesModel> productPriceOutput = new ArrayList<SalesModel>();
       	        LinkedHashMap<String, Double> itemPriceList = new LinkedHashMap<String, Double>();
              	for(SalesModel eclipseitemPrice : productPriceOutput){
        		   itemPriceList.put(eclipseitemPrice.getPartNumber().trim(), eclipseitemPrice.getCustomerPrice());
            	}

	       	    for(String qPno:quotePartNumberSelected){
	       			ProductsModel cartVal=null;
					String qty = request.getParameter("qty_"+qPno);
					String partNumber = request.getParameter("partNumber_"+qPno);
					String mpn = request.getParameter("mpn_"+qPno);
					String shortDesc = request.getParameter("shortDesc_"+qPno);
					String stringPrice = request.getParameter("price_"+qPno);
					String stringTotal = request.getParameter("total_"+qPno);
					String uom = request.getParameter("uom_"+qPno);
					String lineItemComment = request.getParameter("lineitemcomment_"+qPno);
					
					int iQty = 1;
					if(qty!=null && !qty.trim().equalsIgnoreCase("")){
						iQty = (int) Double.parseDouble(qty);
					}
					
					 //SalesModel cartVal = SalesDAO.getQuoteItemDetail(partNumber, subsetId, generalSubsetId);
					String rfqSearchField = null;
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RFQ_SEARCH_FIELD")).length()>0){
						rfqSearchField = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RFQ_SEARCH_FIELD"));
					}else{
						rfqSearchField= "partnumber";
					}
					
					String partKeyword = ProductHunterSolr.escapeQueryCulpritsWithoutWhiteSpace(partNumber);
					partKeyword = "\""+partKeyword+"\"";
					
					ArrayList<ProductsModel> itemDetalsFromSOLR = null;
					itemDetalsFromSOLR = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId, generalSubsetId, CommonUtility.validateString(partKeyword),0,null,rfqSearchField);
					if(itemDetalsFromSOLR.size()>0){
						for(ProductsModel itmListSolr : itemDetalsFromSOLR){
								if(CommonUtility.validateString(partNumber).equalsIgnoreCase(CommonUtility.validateString(itmListSolr.getPartNumber())) || CommonUtility.validateString(partNumber).equalsIgnoreCase(CommonUtility.validateString(itmListSolr.getManufacturerPartNumber()))){
									cartVal=new ProductsModel();
									cartVal=itmListSolr;
								}
						}
					}
					
					
					 if(cartVal!=null){
						 ArrayList<Integer> cartId = new ArrayList<Integer>();

						 if(userId>1){
								cartId = ProductsDAO.selectFromCart(userId, cartVal.getItemId(),uom);
						}else{
								cartId = ProductsDAO.selectFromCartSession(userId, cartVal.getItemId(), sessionId,uom);
						}
						if(cartId!=null && cartId.size()>0){
							/*if(update==null){
								count = ProductsDAO.insertItemToCart(userId, cartVal.getItemId(), CommonUtility.validateNumber(qtyStr), sessionId,lineItemComment, catalogId,uom);
							}else{*/
							 count = ProductsDAO.updateCart(userId,  cartId.get(0), iQty, cartId.get(1),lineItemComment,"",utilityMap);
							/*}*/
						}else{
							count = ProductsDAO.insertItemToCart(userId, cartVal.getItemId(), iQty, sessionId,lineItemComment,catalogId,uom,stringPrice,0, 0, 0,utilityMap);
						}
						if(CommonUtility.customServiceUtility() != null) {
							CommonUtility.customServiceUtility().updateQuotesItemsToCart(qPno,cartVal,request);
						}
						 
					 }else{
						 ProductsModel notInCimm = new ProductsModel();
						 notInCimm.setPartNumber(partNumber);
						 notInCimm.setManufacturerPartNumber(mpn);
						 notInCimm.setShortDesc(shortDesc);
						 notInCimm.setPrice(CommonUtility.validateDoubleNumber(stringPrice));
						 notInCimm.setQty(iQty);
						 notInCimm.setUom(uom);
						 notInCimm.setOrderOrQuoteNumber(CommonUtility.validateString(request.getParameter("orderOrQuoteNumber")));
						 itemsNotInCimm.add(notInCimm);
						 if(CommonUtility.validateString(partNumber).length()>0){
							 String propertyMessage = CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("reoder.cart.message.itemnotfound"));
							 propertyMessage = propertyMessage.replaceAll("#", partNumber);
							 reorderCartMessage = reorderCartMessage+reorderCartMessagebreake+propertyMessage;
							 reorderCartMessagebreake = "<br/>";
						 }
						 //part Number Not Available
					 }
				}
			}
			session.setAttribute("itemsNotInCimm", itemsNotInCimm);
			if(CommonUtility.customServiceUtility() != null) {
				CommonUtility.customServiceUtility().addNonCimmItemsToCart(itemsNotInCimm, session);
			}
			
			if(CommonUtility.customServiceUtility()!=null) {
				CommonUtility.customServiceUtility().sendItemNotInCimmMail(itemsNotInCimm,CommonUtility.validateString(request.getParameter("erpQuoteNumber")));
			}
			
			if(CommonUtility.validateString(reorderCartMessage).length()>0){
				propertyMainMessage = CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("reoder.cart.message.header"));
				reorderCartMessage = propertyMainMessage+"<br/>"+reorderCartMessage;
				session.setAttribute("ReorderCartMessage",reorderCartMessage);
			}
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID")).length()>0){
				String[] brandIDList = CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID").split(",");
				boolean isExcludeBranch = false;
				if(brandIDList!=null){
					for(String brandId:brandIDList){
						isExcludeBranch = ProductsDAO.getCartBrandExcludeStatus(userId,session.getId(),Integer.parseInt(brandId),"QuoteCart");
						if(isExcludeBranch){
							break;
						}
					}
					if(isExcludeBranch){
						session.setAttribute("isWillCallExcludeReorder", "Y");
					}else{
						session.setAttribute("isWillCallExcludeReorder", "N");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return target;
	}
	
	public String itemCompare(){
		
		request =ServletActionContext.getRequest();
		try{
			HttpSession session = request.getSession();
			compareId = request.getParameter("compareId");
			
			
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			
			int userId = CommonUtility.validateNumber(sessionUserId);
			int subsetId = CommonUtility.validateNumber(tempSubset);
		    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		    
			ArrayList<Integer> itemList = new ArrayList<Integer>();
			
			compareList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
			compareList = ProductsDAO.getAttributeForCompare(compareId,(String) session.getAttribute("userToken"),(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("entityId"),userId);
			itemLevelFilterData = compareList.get("Detail");
			
			 LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			 
			 if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
				 for(ProductsModel detailData : itemLevelFilterData){
					 itemList.add(detailData.getItemId());
				 }
			 }
			 
			 if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
				 if(itemList!=null && itemList.size()>0){
					LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "),"itemid");
					contentObject.put("customFieldVal", customFieldVal);
				 }
			 }
			 
			 contentObject.put("compareItemData", itemLevelFilterData);
			 contentObject.put("compareList", compareList);
			 renderContent = LayoutGenerator.templateLoader("ComparePage", contentObject , null, null, null);
		}catch (Exception e) {
	        e.printStackTrace();
		}
		return SUCCESS;
	}
	
public String shoppingCart(){
		/* Git Server Config*/
	long startTimer = CommonUtility.startTimeDispaly();
	String sectionName = "CartPage";
	Gson gson = new Gson();
	request =ServletActionContext.getRequest();
	HttpSession session = request.getSession();
	LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	try {
		
		if(session.getAttribute("ReorderCartMessage")!=null){
			contentObject.put("reorderCartMessage", CommonUtility.validateString(session.getAttribute("ReorderCartMessage").toString()));
		}
		if(session.getAttribute("itemsNotInCimm") != null) {
			contentObject.put("itemsNotInCimm", session.getAttribute("itemsNotInCimm"));
			session.removeAttribute("itemsNotInCimm");
		}
		if(session.getAttribute("fromApproveCart")!=null){
			session.removeAttribute("fromApproveCart");
		}
		if(CommonUtility.customServiceUtility()!=null) {
			 CommonUtility.customServiceUtility().removeShipViaFromSession(session);
		}
		if(request!=null) {
			contentObject.put("creditCardCancel", CommonUtility.validateString(request.getParameter("ccCancel")));
		}
		String isPunchoutCart = request.getParameter("isPunchoutCart");
		if(session.getAttribute("isOciUser")!=null){
			checkOciUser = (Integer) session.getAttribute("isOciUser");
			if(CommonUtility.validateString(isPunchoutCart).trim().length()>0){
				if(checkOciUser>0){
					if(checkOciUser==2){
						/*Assigning Blanket PO for CXML user start */ 
						if (CommonUtility.customServiceUtility() != null) {
							String blanketPO = CommonUtility.customServiceUtility().getBlanketPoList(session);
							if(blanketPO!= null) {
							contentObject.put("BlanketPOList", blanketPO);
							}
						
					}/*Assigning Blanket PO for CXML user end */ 
						sectionName = "PunchoutListDisplay";	
					}
					if(checkOciUser==1){
						sectionName = "OciListDisplay";
					}
				}
			}
		}
		String cartQuickView = request.getParameter("quickCartView");
		String wareHousecode = (String) session.getAttribute("wareHouseCode");
		String customerNumber = (String) session.getAttribute("customerId"); //7932
		String customerCountry = (String) session.getAttribute("customerCountry");
		String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		session.removeAttribute("isReOrder");
		session.removeAttribute("cartItemQtyUpdatedMessage");
		if((sortBy==null || CommonUtility.validateString(sortBy).length()<1) && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_SHOPPING_CART_SORT_BY")).equalsIgnoreCase("PART_NUMBER")){
			sortBy = "PartA";
		}
		if(sortBy!=null && sortBy.trim().length()>0){
			session.setAttribute("sortBy",sortBy );
			contentObject.put("sortBy", sortBy);
		}
		String tempSubset = (String) session.getAttribute("userSubsetId");
		int subsetId = CommonUtility.validateNumber(tempSubset);
		int generalSubsetId = CommonUtility.validateNumber(session.getAttribute("generalCatalog").toString());
		if(CommonUtility.validateString(sessionId).length() > 0){
			int count = ProductsDAO.updatecartSessionId(sessionId, session.getId());
			if(count > 0){
				BrontoDAO.updateAbandonedCartSessionId(sessionId, session.getId());
				validateResponse = ProductsDAO.getCartCountBySession(userId,session.getId(),subsetId, generalSubsetId);
				session.setAttribute("cartCountSession",validateResponse);
			}
			
		}		
		contentObject = ProductsDAO.getShoppingCartDao(session, contentObject);
		UsersModel alwaysApprover =  UsersDAO.checkAlwaysApprover(userId);
		contentObject.put("alwaysApprover", alwaysApprover.getAlwaysApprover());
		if(CommonDBQuery.getSystemParamtersList().get("FETCH_WAREHOUSES_IN_SHOPPING_CART") != null && CommonDBQuery.getSystemParamtersList().get("FETCH_WAREHOUSES_IN_SHOPPING_CART").equals("Y")) {
			List<WarehouseModel> wareHouses = UsersDAO.getWareHouses();
			contentObject.put("wareHouses", wareHouses);
			contentObject.put("wareHousesAsJson", gson.toJson(wareHouses));
		}
		LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = (LinkedHashMap<Integer, LinkedHashMap<String, Object>>) contentObject.get("customFieldVal");	
		String cartItemQtyUpdatedMessage = "";
		if(contentObject!=null && contentObject.get("productListData")!=null && CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA")!=null && CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA").trim().equalsIgnoreCase("Y")){
			ArrayList<ProductsModel> itemDetailList = (ArrayList<ProductsModel>) contentObject.get("productListData");
			if(itemDetailList!=null && itemDetailList.size()>0){
				cartItemQtyUpdatedMessage = checkMinOrderQtyAndAutoUpdateQuantity(itemDetailList);
				if(cartItemQtyUpdatedMessage!=null && cartItemQtyUpdatedMessage.trim().length()>0){
					contentObject = ProductsDAO.getShoppingCartDao(session, contentObject);
					contentObject.put("cartItemQtyUpdatedMessage", cartItemQtyUpdatedMessage);
					session.setAttribute("cartItemQtyUpdatedMessage",cartItemQtyUpdatedMessage);
					
				}
			}
		}		 
		contentObject.put("productListJson", gson.toJson(contentObject.get("productListData")));
		if(contentObject!=null && contentObject.get("cartTotal")!=null){
			cartTotal = (Double) contentObject.get("cartTotal");
			if(cartTotal>0){
				FreightCalculatorModel freightInput = new FreightCalculatorModel();
				if(customFieldVal!=null && CommonDBQuery.getSystemParamtersList().get("OVERSIZE_ITEM_FREIGHT_RULE")!=null && CommonDBQuery.getSystemParamtersList().get("OVERSIZE_ITEM_FREIGHT_RULE").trim().equalsIgnoreCase("Y")){
					for(Map.Entry<Integer, LinkedHashMap<String, Object>> entry : customFieldVal.entrySet()){
						if(entry!=null && entry.getValue()!=null && entry.getValue().toString().contains("custom_OverSize=Y")){
							freightInput.setOverSize("Y");
							contentObject.put("overSizeItem",freightInput.getOverSize());
							break;
						}else{
							freightInput.setOverSize("N");
						}
						
						if(entry!=null && entry.getValue()!=null && entry.getValue().toString().contains("custom_Hazmat=Y")){
							freightInput.setHazmat("Y");
							contentObject.put("hazmatItem",freightInput.getHazmat());
							break;
						}else{
							freightInput.setHazmat("N");
						}
					}
				}
				freightInput.setCartTotal(cartTotal);
				freightInput.setCountry(customerCountry);
				freightInput.setCustomerNumber(customerNumber);
				freightInput.setLocale(CommonUtility.validateString(session.getAttribute("localeCode").toString().toUpperCase()));
				freightInput.setShipVia((String)session.getAttribute("selectedShipVia"));
				freightInput.setWareHouseCode(wareHousecode);
				UsersModel shipAddress = null;
				if(session!=null && session.getAttribute("defaultShipAddress")!=null){
					shipAddress = (UsersModel) session.getAttribute("defaultShipAddress");
					if(shipAddress!=null && shipAddress.getState()!=null && shipAddress.getState().trim().length()>0){
						freightInput.setState(shipAddress.getState().trim());
					}
				}
				freightValue = FreightCalculator.getFreightByTotal(freightInput);
				if(freightValue!=null){
					contentObject.put("freightValue", freightValue.getFreightValue());
					contentObject.put("freightMessage", freightValue.getMessage());
					cartTotal = cartTotal+freightValue.getFreightValue();
					contentObject.put("cartTotal", cartTotal);
				}
			}
		}
		if(cartQuickView!=null && cartQuickView.trim().equalsIgnoreCase("Y")){
			String reponseType = request.getParameter("responseType");
			contentObject.put("quickCartView", "Y");
			String selectedBranch = (String)session.getAttribute("selectedBranchWillCall");
			if(selectedBranch!=null && !selectedBranch.trim().equalsIgnoreCase("")){
				if(selectedBranch.contains("Will Call")&& (CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT")!=null && CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT").trim().equalsIgnoreCase("Y"))){
					if(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WILLCALL_SHIP_ID")!=null && !CommonDBQuery.getSystemParamtersList().get("DEFAULT_WILLCALL_SHIP_ID").trim().equalsIgnoreCase("")){
						String branchId=CommonDBQuery.getSystemParamtersList().get("DEFAULT_WILLCALL_SHIP_ID").trim();
						session.setAttribute("shipBranchId", branchId);
					}
					String[] selectArra = selectedBranch.split("\\|");
					contentObject.put("selectedBranch", selectArra[2].toUpperCase());
				}else{	
					String[] selectArra = selectedBranch.split("\\|");
					UsersAction test = new UsersAction();
					String willCallText1 = test.saveSelectedBranch();
					contentObject.put("willCallText1", willCallText1);
					contentObject.put("selectedBranch", selectArra[2].toUpperCase()); /*Due to Removal of Advance Will Call*/
				}
			}
			contentObject.put("responseType", reponseType);
			renderContent = LayoutGenerator.templateLoader("QuickCartViewPage", contentObject , null, null, null);
		}else{
			if(CommonDBQuery.getSystemParamtersList().get("GET_SHIPPING_ADDRESS_IN_CART")!=null && CommonDBQuery.getSystemParamtersList().get("GET_SHIPPING_ADDRESS_IN_CART").trim().equalsIgnoreCase("Y")){
				String tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
				userName = (String) session.getAttribute(Global.USERNAME_KEY);
				UserManagement usersObj = new UserManagementImpl();
				int defaultShipToId=0;
				int defaultBillToId=0;
				if(defaultBillToId==0){
				 	HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(userName);
				 	defaultBillToId = userAddressId.get("Bill");
				}
				if(tempdefaultShipId!=null){
					defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);
				}
				HashMap<String, UsersModel> userAddress = usersObj.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
				contentObject.put("shipAddress", userAddress.get("Ship"));
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_SHIPPING_ADDRESSES_IN_CART")).equalsIgnoreCase("Y")) {
				UserManagement usersObj = new UserManagementImpl();
				HashMap<String, ArrayList<UsersModel>> userAddressList = usersObj.getAddressListFromBCAddressBook(CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString()),userId);
				contentObject.put("billAddressList", userAddressList.get("Bill"));
				contentObject.put("shipAddressList", userAddressList.get("Ship"));
				contentObject.put("shipAddressListAsJson", gson.toJson(userAddressList.get("Ship")));
			}
			String isGeneralUser = (String) session.getAttribute("isGeneralUser");
			if(isGeneralUser!=null && isGeneralUser.trim().equalsIgnoreCase("Y")){
				String messageForGereralUser="";
				String approverID= (String) session.getAttribute("approvalUserId");
				if(approverID==null){
					approverID = "";
				}
				int approveUserId = 0;
				ArrayList<Integer> approverList = UsersDAO.getApprovalUserList(userId);
				if(approverList!=null && approverList.size()>0){
					approveUserId = approverList.get(0);
				}
				approveSenderid="Y";
				if(approveUserId==0){
					approveSenderid="N";
					messageForGereralUser = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("no.agent.assigned.to.genraluser").trim();
				}else if(approveUserId>0){
					approveSenderid="Y";
				}
				contentObject.put("approverID", approverID);
				contentObject.put("approveUserId", approveUserId);
				contentObject.put("approveSenderid", approveSenderid);
				contentObject.put("messageForGereralUser", messageForGereralUser);
			}
			if(CommonUtility.validateString(isReOrder).equalsIgnoreCase("Y") && checkOciUser>0){
				ArrayList<ProductsModel> cartListData = SalesDAO.getOrderDetailsQuote(session);
				contentObject.put("productListData",cartListData);
				contentObject.put("cartTotal", cartListData.get(0).getCartTotal());
			}
			
			if(CommonUtility.customServiceUtility()!=null && session.getAttribute("isTishmanUser") !=null && CommonUtility.validateString((String) session.getAttribute("isTishmanUser")).equalsIgnoreCase("Y"))
			{  
				 CommonUtility.customServiceUtility().getFirstApproverThresholdrange(contentObject,session); 
			}
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_EMAIL_ID")).equalsIgnoreCase("Y")){
				UsersAction uAction = new UsersAction();
				uAction.checkEmailAddress(userId);
				if(CommonUtility.validateString((String) session.getAttribute("emailAddressExits")).equalsIgnoreCase("N")){
					contentObject.put("validEmailAddress", false);
				}
			}
			session.removeAttribute("ReorderCartMessage");
			renderContent = LayoutGenerator.templateLoader(sectionName, contentObject , null, null, null);	
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	return SUCCESS;
}
	
	public String QuoteCart(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			String customerNumber = (String) session.getAttribute("customerId"); //7932
			String customerCountry = (String) session.getAttribute("customerCountry");
			int quoteCartCount =(Integer) session.getAttribute("quoteCartCount");
			session.removeAttribute("isReOrder");
			session.removeAttribute("quoteNumber");
			session.removeAttribute("orderNumber");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			if(ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"))>0){
				tempGeneralSubset = CommonUtility.validateParseIntegerToString(ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME")));
	    	}
			String tempSubset = (String) session.getAttribute("userSubsetId");
			contentObject.put("quoteCartCount", quoteCartCount);
			contentObject.put("subsetId", tempSubset);
			contentObject.put("generalsubsetId", tempGeneralSubset);
			contentObject = ProductsDAO.getQuoteCartDao(session, contentObject);
			
			if(contentObject!=null && contentObject.get("cartTotal")!=null)
			{
				cartTotal = (Double) contentObject.get("cartTotal");
				if(cartTotal>0)
				{
					
					
					FreightCalculatorModel freightInput = new FreightCalculatorModel();
					
					LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = (LinkedHashMap<Integer, LinkedHashMap<String, Object>>) contentObject.get("customFieldVal");
					if(customFieldVal!=null && CommonDBQuery.getSystemParamtersList().get("OVERSIZE_ITEM_FREIGHT_RULE")!=null && CommonDBQuery.getSystemParamtersList().get("OVERSIZE_ITEM_FREIGHT_RULE").trim().equalsIgnoreCase("Y")){
						for(Map.Entry<Integer, LinkedHashMap<String, Object>> entry : customFieldVal.entrySet()){
							if(entry!=null && entry.getValue()!=null && entry.getValue().toString().contains("custom_OverSize=Y")){
								freightInput.setOverSize("Y");
								break;
							}else{
								freightInput.setOverSize("N");
							}
						}
					}
					freightInput.setCartTotal(cartTotal);
					freightInput.setCountry(customerCountry);
					freightInput.setCustomerNumber(customerNumber);
					freightInput.setLocale(CommonUtility.validateString(session.getAttribute("localeCode").toString().toUpperCase()));
					freightInput.setShipVia((String)session.getAttribute("selectedShipVia"));
					freightInput.setWareHouseCode(wareHousecode);
					UsersModel shipAddress = null;
					if(session!=null && session.getAttribute("defaultShipAddress")!=null){
						shipAddress = (UsersModel) session.getAttribute("defaultShipAddress");
						if(shipAddress!=null && shipAddress.getState()!=null && shipAddress.getState().trim().length()>0){
							freightInput.setState(shipAddress.getState().trim());
						}
					}
					freightValue = FreightCalculator.getFreightByTotal(freightInput);
					if(freightValue!=null){
						contentObject.put("freightValue", freightValue.getFreightValue());
						contentObject.put("freightMessage", freightValue.getMessage());
						cartTotal = cartTotal+freightValue.getFreightValue();
						contentObject.put("cartTotal", cartTotal);
					}
				}
				
			}
			if(CommonDBQuery.getSystemParamtersList().get("GET_SHIPPING_ADDRESS_IN_CART")!=null && CommonDBQuery.getSystemParamtersList().get("GET_SHIPPING_ADDRESS_IN_CART").trim().equalsIgnoreCase("Y")){
				String tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
				userName = (String) session.getAttribute(Global.USERNAME_KEY);
				UserManagement usersObj = new UserManagementImpl();
				int defaultShipToId=0;
				int defaultBillToId=0;
				if(defaultBillToId==0){
				 	HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(userName);
				 	defaultBillToId = userAddressId.get("Bill");
				}
				if(tempdefaultShipId!=null){
					defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);
				}
				HashMap<String, UsersModel> userAddress = usersObj.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
				contentObject.put("shipAddress", userAddress.get("Ship"));
				contentObject.put("defaultShipToId",defaultShipToId);
			}
			session.setAttribute("QuoteCartTotal", cartTotal);
			session.setAttribute("QuoteCartCount", contentObject.get("selectQuoteCartCount"));
			
			renderContent = LayoutGenerator.templateLoader("ReorderCartPage", contentObject , null, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String checkMinOrderQtyAndAutoUpdateQuantity(ArrayList<ProductsModel> productListData){
		boolean isAddToCart = false;
		int qty = 0;
		minimumOrderQty = 1;
        orderQtyInterval = 1;
        request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		renderContent = "";
		String note = "";
		try{
				
				String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
				int userId = CommonUtility.validateNumber(sessionUserId);
				session.removeAttribute("cartItemQtyUpdatedMessage");
				if(productListData!=null && productListData.size()>0){
					
					String delimit = "";
					
					for(ProductsModel itemDetails : productListData){
						
						itemDetails.setUserId(userId);
						
						qty = itemDetails.getQty();
						if(qty==0){
							
						}else{
							ProductsModel minOrderQty = new ProductsModel();
							//Latest
				        	String shipViaCode = "";
				        	if(session!=null && session.getAttribute("selectedShipVia")!=null){
				        		shipViaCode = session.getAttribute("selectedShipVia").toString().trim().toUpperCase();
				        	}
				        	ProductsModel productInput = new ProductsModel();
				        	productInput.setItemPriceId(itemDetails.getItemPriceId());
				        	productInput.setShipViaCode(shipViaCode);
				        	minOrderQty = ProductsDAO.getMinOrderQty(productInput);
				        	//Latest
				        	
				        	//minOrderQty = ProductsDAO.getMinOrderQty(itemPriceIdList[i]);
				        	//minOrderQty = ProductsDAO.getMinOrderQty(CommonUtility.validateNumber(itemPriceIdList[i]));
				        	
				        	if(minOrderQty!=null && minOrderQty.getMinOrderQty()>0){
		    					
		    					if(qty == minOrderQty.getMinOrderQty()){
		    						
		    						isAddToCart = true;
		    						
		    					}else if(qty > minOrderQty.getMinOrderQty()){
		    						
		    						int qtyDiff = qty - minOrderQty.getMinOrderQty();
		    						
		    						if(qtyDiff%minOrderQty.getOrderInterval()==0){
		    							isAddToCart = true;
		    						}
		     					}
		    					
		    				}else{
		    					
		    					isAddToCart = true;
		    					
		    				}
				        	
				        	if(!isAddToCart){
				        		note = "<b>Due to your change in shipping method, the following item(s) quantities have been adjusted:</b> <br/>";
				        		renderContent = renderContent+ delimit + itemDetails.getPageTitle();
				        		//renderContent = renderContent+ delimit +"Item "+ itemDetails.getPageTitle() +", has to be ordered in multiples of "+minOrderQty.getOrderInterval()+" and Min. Order Quantity is : "+minOrderQty.getMinOrderQty();
				        		//renderContent = renderContent+ delimit +"Item# "+itemDetails.getPartNumber()+" has to be ordered in multiples of "+minOrderQty.getOrderInterval()+" and Min. Order Quantity is : "+minOrderQty.getMinOrderQty();
		    					delimit = "<br/>";
		    					
		    					if(session!=null && session.getAttribute("setAutoUpdateMinQtyFalse")!=null && session.getAttribute("setAutoUpdateMinQtyFalse").toString().trim().equalsIgnoreCase("Y")){}else{
		    						ProductsDAO.updateCartQuantity(itemDetails);
		    					}
		    				}else{
		    					isAddToCart = false;
		    				}
				        	
				        	
						}
					}
					 
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return note+renderContent;
		//return "ResultLoader";
		
	}
	
	public String checkMinOrderQtyForCartItems(){
		
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		target = "checkout";
		try {
			
			if(session.getAttribute("quoteOrderId")!=null)
    			session.removeAttribute("quoteOrderId");
			if(session.getAttribute("quoteNumber")!=null)
				session.removeAttribute("quoteNumber");
			session.setAttribute("selectedShipVia",shipVia);
			contentObject = ProductsDAO.getShoppingCartDao(session, contentObject);
			String cartItemQtyUpdatedMessage = "";
			if(contentObject!=null && contentObject.get("productListData")!=null && CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA")!=null && CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA").trim().equalsIgnoreCase("Y")){
				ArrayList<ProductsModel> itemDetailList = (ArrayList<ProductsModel>) contentObject.get("productListData");
				if(itemDetailList!=null && itemDetailList.size()>0){
					session.setAttribute("setAutoUpdateMinQtyFalse","Y");
					cartItemQtyUpdatedMessage = checkMinOrderQtyAndAutoUpdateQuantity(itemDetailList);
					if(cartItemQtyUpdatedMessage!=null && cartItemQtyUpdatedMessage.trim().length()>0){
						target = "ShoppingCart";
					}
					session.removeAttribute("setAutoUpdateMinQtyFalse");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return target;
	}
	
	public String checkMinOrderQty(){
		boolean isAddToCart = false;
		int qty = 0;
		minimumOrderQty = 1;
        orderQtyInterval = 1;
        request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		renderContent = "";
		session.removeAttribute("cartItemQtyUpdatedMessage");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		
		try{
				String selectedShipMethod = request.getParameter("selectedShipMethod");
	        	if(CommonUtility.validateString(selectedShipMethod).trim().length()>0){
	        		session.setAttribute("selectedShipVia",selectedShipMethod);
	        	}
	        	String mpnDisplay = "";
	        	if (CommonUtility.validateString(type).length()>0 && (CommonUtility.validateString(type).equalsIgnoreCase("ProductGroup") || CommonUtility.validateString(type).equalsIgnoreCase("savedCart"))) {
	        	      String shoppingcartId = request.getParameter("shoppingCartId");
	        	      String shoppingcartQty = request.getParameter("shoppingCartQty");
	        	      String itemPriceId = request.getParameter("itemPriceIdList");
	        	      String minOrder = request.getParameter("minmunOrderQuantity");
	        	      String orderinterval = request.getParameter("orderQuantityInterval");
	        	      String partnumber = request.getParameter("partnumberStrArray");
	        	      String mpn = request.getParameter("mpnStrArray");
	        	      mpnDisplay = request.getParameter("mpnDisplay");
	        	      if(shoppingcartQty!=null){
	        	    	  shoppingCartQty = shoppingcartQty.split(",");
	        	      }
	        	      if(itemPriceId!=null){
	        	    	  itemPriceIdList = itemPriceId.split(",");
	        	      }
	        	      if(shoppingcartId!=null){
	        	    	  shoppingCartId = shoppingcartId.split(",");
	        	      }
	        	      if(minOrder!=null){
	        	    	  minmunOrderQuantity = minOrder.split(",");
	        	      }
	        	      if(orderinterval!=null){
	        	    	  orderQuantityInterval = orderinterval.split(",");
	        	      }
	        	      if(partnumber!=null){
	        	    	  partnumberStrArray = partnumber.split(",");
	        	      }
	        	      if(mpn!=null){
	        	    	  mpnStrArray = mpn.split(",");
	        	      }
	        	   }
				//int count;
				if(shoppingCartId!=null && shoppingCartId.length>0){
					String c = "";
		    		for(int i=0;i<shoppingCartId.length;i++){
		    			boolean lessThanMinOrder = true;		    			
		    			if(shoppingCartQty !=null && !shoppingCartQty[i].trim().equalsIgnoreCase(""))
		    			qty = CommonUtility.validateNumber(shoppingCartQty[i]);
		    			
		    			if(minmunOrderQuantity != null && !minmunOrderQuantity[i].trim().equalsIgnoreCase(""))
		    				minimumOrderQty = CommonUtility.validateNumber(minmunOrderQuantity[i]);
		    			if(orderQuantityInterval != null && !orderQuantityInterval[i].trim().equalsIgnoreCase(""))
		    				orderQtyInterval = CommonUtility.validateNumber(orderQuantityInterval[i]);
		    			
		    			isAddToCart = false;
		    			if(qty==0){
		    				//count = ProductorItemUtilityDao.deleteFromCart(conn, userId, CommonUtility.validateNumber(shoppingCartId[i]));
		    			}else{
		    				ProductsModel minOrderQty = new ProductsModel();
		    				
		    				if(CommonDBQuery.getSystemParamtersList().get("QTY_INTERVALSFROM_ERP")!=null && CommonDBQuery.getSystemParamtersList().get("QTY_INTERVALSFROM_ERP").trim().equalsIgnoreCase("Y")){
		    		        	minOrderQty.setMinOrderQty(minimumOrderQty);
		    					minOrderQty.setOrderInterval(orderQtyInterval);
		    		        }else{
		    		        	
		    		        	//Latest
		    		        	String shipViaCode = "";
		    		        	if(session!=null && session.getAttribute("selectedShipVia")!=null){
		    		        		shipViaCode = (String) session.getAttribute("selectedShipVia");
		    		        	}
		    		        	ProductsModel productInput = new ProductsModel();
		    		        	productInput.setItemPriceId(CommonUtility.validateNumber(itemPriceIdList[i]));
		    		        	productInput.setShipViaCode(shipViaCode);
		    		        	minOrderQty = ProductsDAO.getMinOrderQty(productInput);
		    		        	//Latest
		    		        	
		    		        	//minOrderQty = ProductsDAO.getMinOrderQty(itemPriceIdList[i]);
		    		        	//minOrderQty = ProductsDAO.getMinOrderQty(CommonUtility.validateNumber(itemPriceIdList[i]));
		    		        }
		    				
		    				if(minOrderQty!=null && minOrderQty.getMinOrderQty()>0){
		    					
		    					if(qty == minOrderQty.getMinOrderQty()){
		    						isAddToCart = true;
		    						lessThanMinOrder = false;
		    					}else if(qty > minOrderQty.getMinOrderQty()){
		    						lessThanMinOrder = false;
		    						int qtyDiff = qty - minOrderQty.getMinOrderQty();
		    						if(qtyDiff%minOrderQty.getOrderInterval()==0){
		    							isAddToCart = true;
		    						}
		     					}
		    				}else{
		    					isAddToCart = true;
		    				}
		    				
		    				if(!isAddToCart){
		    					if(CommonUtility.validateString(mpnDisplay).length()>0){
			    					//renderContent = renderContent+c+"Item# "+mpnStrArray[i]+" is only available in multiples of "+minOrderQty.getOrderInterval();
		    						if(lessThanMinOrder){
		    							renderContent = renderContent+c+"Item# "+mpnStrArray[i]+" is only available in multiples of "+minOrderQty.getOrderInterval() +" and Min. Order Quantity is : "+ minOrderQty.getMinOrderQty();
		    						}else{
		    							renderContent = renderContent+c+"Item# "+mpnStrArray[i]+" is only available in multiples of "+minOrderQty.getOrderInterval();
		    						}
			    				}else{
			    					//renderContent = renderContent+c+"Item# "+partnumberStrArray[i]+" is only available in multiples of "+minOrderQty.getOrderInterval();
			    					if(lessThanMinOrder){
		    							renderContent = renderContent+c+"Item# "+partnumberStrArray[i]+" is only available in multiples of "+minOrderQty.getOrderInterval() +" and Min. Order Quantity is : "+ minOrderQty.getMinOrderQty();
		    						}else{
		    							renderContent = renderContent+c+"Item# "+partnumberStrArray[i]+" is only available in multiples of "+minOrderQty.getOrderInterval();
		    						}
			    				}
		    					//renderContent = renderContent+c+"Item# "+partnumberStrArray[i]+" is only available in multiples of "+minOrderQty.getOrderInterval();
		    					//renderContent = renderContent+c+"Item# "+partnumberStrArray[i]+" has to be ordered in multiples of "+minOrderQty.getOrderInterval()+" and Min. Order Quantity is "+minOrderQty.getMinOrderQty();
		    					//renderContent = renderContent + c + itemPriceIdList[i]+"|Item "+partnumberStrArray[i]+" has to be ordered in multiples of "+minOrderQty.getOrderInterval()+" and Min. Order Quantity is : "+minOrderQty.getMinOrderQty();
		    					c = "|";
		    				}
		    			}
		    				//count = ProductorItemUtilityDao.updateCart(conn, userId, CommonUtility.validateNumber(shoppingCartId[i]), qty, 0);
		    		}
		    	}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "ResultLoader";
	}
	
	public String updateShoppingCart(){
		long startTimer = CommonUtility.startTimeDispaly();
		target = "ShoppingCart";
	    request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String reqType = request.getParameter("reqType");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		
        try{
        	LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
			utilityMap.put("considerLineItemComment", true);
        	session.removeAttribute("cartCountSession");
        	session.removeAttribute("quoteNumber");
        	session.removeAttribute("orderNumber");
        	session.removeAttribute("cartItemQtyUpdatedMessage");
        	String selectedShipMethod = request.getParameter("selectedShipMethod");
        	if(CommonUtility.validateString(selectedShipMethod).trim().length()>0){
        		session.setAttribute("selectedShipVia",selectedShipMethod);
        	}
        	
        	int qty = 1;
        	int count = 0;
        	if(session.getAttribute("quoteOrderId")!=null)
    			session.removeAttribute("quoteOrderId");
        	if(session.getAttribute("editedAddressList")!=null)
    			session.removeAttribute("editedAddressList");
        	if(shoppingCartId!=null && shoppingCartId.length>0){
        		for(int i=0;i<shoppingCartId.length;i++){
        			
        			if(!shoppingCartQty[i].trim().equalsIgnoreCase(""))
        				qty = CommonUtility.validateNumber(shoppingCartQty[i]);
        			/*if(!itemleveShipvia[i].trim().equalsIgnoreCase(""))
        				itemLevelshipvia = CommonUtility.validateString(itemleveShipvia[i]);*/
        			
        			if(qty==0){
        				count = ProductsDAO.deleteFromCart(userId, CommonUtility.validateNumber(shoppingCartId[i]));
        			}else{
        				
        				String lineComment = request.getParameter("lineItemComment_"+shoppingCartId[i]);
        				String itemLevelRequiredDate= request.getParameter("reqDate_"+shoppingCartId[i]);
        				utilityMap.put("orderOrQuoteNumber", request.getParameter("orderOrQuoteNumber_"+shoppingCartId[i]));
        				utilityMap.put("additionalProperties", request.getParameter("additionalProperties_"+shoppingCartId[i]));
        				count = ProductsDAO.updateCart(userId, CommonUtility.validateNumber(shoppingCartId[i]), qty, 0,lineComment,itemLevelRequiredDate,utilityMap);

        				
        			}
        		}
        		
        	}
        	if(reqType!=null && CommonUtility.validateString(reqType).equalsIgnoreCase("updatecartcontent")){
        		renderContent = "success";
        	    target = SUCCESS;
        	}
        	
        	if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT")).length() > 0 && CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT").trim().equalsIgnoreCase("Y"))	
			{
    		  ProductsDAO.resetCartCheckoutType(userId);
    		  session.setAttribute("displayCheckoutCondMsgFlag","Y");
    		  session.removeAttribute("vendorSpecificPurchaseMsg");
    	    }
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
        CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
 		return target;
	}
	
	public String deleteCart(){
		target = "ShoppingCart";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		type = request.getParameter("type");
		String sessionId = session.getId();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		session.removeAttribute("vendorSpecificPurchaseMsg");
		int count = 0;
		int userId = CommonUtility.validateNumber(sessionUserId);
	    try{
	    	session.removeAttribute("cartCountSession");
	       	if(deleteId!=null && CommonUtility.validateNumber(deleteId.trim())>0){
	       		ProductsDAO.deleteFromCart(userId, CommonUtility.validateNumber(deleteId));
	       		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID")).length()>0){
					String[] brandIDList = CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID").split(",");
					boolean isExcludeBranch = false;
					if(brandIDList!=null){
						for(String brandId:brandIDList){
							isExcludeBranch = ProductsDAO.getCartBrandExcludeStatus(userId,sessionId,Integer.parseInt(brandId),"cart");
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
	      	}else if (deleteFlag!=null && deleteFlag.trim().equalsIgnoreCase("Y")) {
	       		if(userId>1){
	       			ProductsDAO.clearCart(userId);
	       		} else if (userId==1) {
	       			ProductsDAO.clearCartBySessionId(userId, session.getId());
				}
	       		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
	       			session.removeAttribute("shipViaFlag");
				}
			}
		}catch(Exception e){
	        e.printStackTrace();
		}
	    
	    if(type!=null && type.trim().equalsIgnoreCase("quickcartscript")){
	    	
	    	renderContent="success";
	    	return renderContent;
	    	
	    }else{
	   return target;
	    }
	}
	
	public String refreshShoppingCart(){
		int count = 0;
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		session.removeAttribute("vendorSpecificPurchaseMsg");
		try{
			LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
			utilityMap.put("considerLineItemComment", true);
        	if(updateId!=null){
    			//String id = updateId.replaceAll(",", "");
    			if(itemQty>0){
    				String lineComment = request.getParameter("lineItemCommentRef");
    				String requiredByDate = request.getParameter("requiredByDateRef");
    				count = ProductsDAO.updateCart(userId, CommonUtility.validateNumber(updateId), itemQty, 0,lineComment,requiredByDate, utilityMap);
    				System.out.println("Updated when refreshed : refreshShoppingCart : "+count);
    			}else{
    				count = ProductsDAO.deleteFromCart(userId, CommonUtility.validateNumber(updateId));
    				System.out.println("Deleted when refreshed : refreshShoppingCart : "+count);
    			}
    		}
        	if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT")).length() > 0 && CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT").trim().equalsIgnoreCase("Y"))	
			{
    		  ProductsDAO.resetCartCheckoutType(userId);
    		  session.setAttribute("displayCheckoutCondMsgFlag","Y");
    	    }
        }catch(Exception e){
        	e.printStackTrace();
        }
        if(count==1){
        	renderContent="success";
		}
        return "ResultLoader";
	}
	

	public String productListIdName(){
		System.out.println("Inside product Group");
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		ArrayList<Integer> eachItemList = null;
		try{
			target = "ResultLoader";
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			String jsonData = (String)request.getParameter("jsonData");
			String fromPage = (String) request.getParameter("fromPage");
			
			int userId = CommonUtility.validateNumber(sessionUserId);
			if(CommonUtility.validateString(jsonData).length()>0){
				Gson parseInput = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				eachItemList = new ArrayList<Integer>();
				ProductsModel  parsedData = parseInput.fromJson(jsonData,ProductsModel.class);
				if(parsedData.getItemDataList().size()>0){
					for(ProductsModel eachItem:parsedData.getItemDataList()){
						eachItemList.add(eachItem.getItemId());
					}	
					contentObject.put("userItemData", parsedData.getItemDataList());
				}
				
			}
			if(eachItemList!=null){
				LinkedHashMap<Integer, ProductsModel> finalData = new LinkedHashMap<Integer, ProductsModel>(); 
				ArrayList<ProductsModel> itemDetailData = ProductHunterSolr.getItemDetailsForGivenPartNumbers( CommonUtility.validateNumber( (String) session.getAttribute("userSubsetId")), CommonUtility.validateNumber((String) session.getAttribute("generalCatalog")), StringUtils.join(eachItemList," OR "),0,null,"itemid");
				for(ProductsModel eachSearchedData:itemDetailData){
					finalData.put(eachSearchedData.getItemId(), eachSearchedData);
				}
				contentObject.put("allItemData", finalData);
			}
			productListData = new ArrayList<ProductsModel>();
			int buyingCompanyId = CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId"));
			productListData = ProductsDAO.productListIdName(userId, groupType, buyingCompanyId);
			if(groupType.equalsIgnoreCase("P")){
				contentObject.put("responseType", "ProductGroupList");
			}else{
				contentObject.put("responseType", "CartGroupList");
			}
			if(fromPage!=null && fromPage.trim().length() > 0){
				contentObject.put("fromPage", fromPage);
			}
			if(CommonUtility.validateString(renderContent).length()>0){
				contentObject.put("RequestType", renderContent);
			}
			contentObject.put("productListData", productListData);
			contentObject.put("saveAsShoppingList", CommonUtility.validateString(request.getParameter("saveAsShoppingList")));
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);

		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("list count called end");
		return target;
	}
	
	public String insertProductListItem(){
		target = "ResultLoader";
		String advancedProductGroup = CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP");
		try{
		    request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String uomValue = CommonUtility.validateString(request.getParameter("uomValue"));
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_QTY_FOR_GROUPS")).length() > 0){
				itemQty = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("DEFAULT_QTY_FOR_GROUPS"));
			}
			
			if(advancedProductGroup!=null && advancedProductGroup.trim().equalsIgnoreCase("Y")){
				String groupListId = request.getParameter("groupListId");
				if(groupListId!=null && !groupListId.trim().equalsIgnoreCase(""))
				{
					String groupListArr[] = groupListId.split(",");
					for(String group:groupListArr)
					{
						listId = CommonUtility.validateNumber(CommonUtility.validateString(group).trim());
						if(listId>0)
						savedGroupId = ProductsDAO.insertAdvancedProductListItem(listId, listName, userId, productIdList, itemQty,levelNo,parentGroupId,CommonUtility.validateNumber(vpsid));
					}
				}
			}else{
				if(CommonUtility.validateString(srchTyp).equalsIgnoreCase("multiple")){
					String jsonData = (String)request.getParameter("jsonData");
					if(CommonUtility.validateString(jsonData).length()>0){
						Gson parseInput = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
						ProductsModel  parsedData = parseInput.fromJson(jsonData,ProductsModel.class);
						for(ProductsModel eachItem:parsedData.getItemDataList()){
							uomValue = CommonUtility.validateString(eachItem.getUom());
							if(itemQty>0){
								savedGroupId = ProductsDAO.insertProductListItem(parsedData.getProductListId(), parsedData.getGroupName(), userId,""+ eachItem.getItemId(),itemQty,uomValue);	
							}else{
								savedGroupId = ProductsDAO.insertProductListItem(parsedData.getProductListId(), parsedData.getGroupName(), userId,""+ eachItem.getItemId(), eachItem.getQty(),uomValue);	
							}
						}
					}
				}else{
					savedGroupId = ProductsDAO.insertProductListItem(listId, listName, userId, productIdList, itemQty,uomValue);	
				}
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		renderContent = validateResponse+"|"+savedGroupId+"|"+errorMsg;
		return target;
	}
	
	public String myProductGroup(){
		target = "ResultLoader"; //ProductListItem
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		String advancedProductGroup = CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		String sessionId = session.getId();
		String entityId = (String) session.getAttribute("entityId");
		String homeTerritory = (String) session.getAttribute("shipBranchId");
		int userId = CommonUtility.validateNumber(sessionUserId);
		int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		int resultPerPage = 0;
		ArrayList<Integer> itemList = new ArrayList<Integer>();
		String toCart = "";
		requestTokenId=CommonUtility.validateNumber(request.getParameter("requestTokenId"));

		try{
			if(CommonUtility.validateString(request.getParameter("savedGroupId")).length() == 0 && CommonUtility.validateString(savedGroupName).length()>0 ) {
				savedGroupId = ProductsDAO.getSavedGroupIDByName(savedGroupName);
			}
			toCart = (String)request.getParameter("toCart");
			if(ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"))>0){
				generalSubset = ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"));
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REDIRECT_TO_LOGIN_PAGE")).equalsIgnoreCase("Y") && !(userId >1)) {
				String url = request.getRequestURL().toString()+"?"+request.getQueryString();
				contentObject.put("fromPageValue", url);
				renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject , null, null, null);
				return SUCCESS;
		}

			reqType = request.getParameter("reqType");
			sortBy = request.getParameter("sortBy");
			
			resultPerPage = CommonUtility.validateNumber(resultPage);
			if(sortBy==null){
				if(session.getAttribute("sortBy")!=null && session.getAttribute("sortBy").toString().trim().length()>0){
					sortBy =  (String)session.getAttribute("sortBy");
				}else if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_GROUP_SORTBY")).equalsIgnoreCase("")){
					sortBy = CommonDBQuery.getSystemParamtersList().get("DEFAULT_GROUP_SORTBY");
					session.setAttribute("sortBy",sortBy);
				}else{
					sortBy="";
				}
			}else{
				session.setAttribute("sortBy",sortBy);
			}

			if(resultPerPage==0)
			{
				if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
					resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
					resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
				}else{
					resultPage = "12";
					resultPerPage = 12;
				}
			}
			if(CommonUtility.validateString(searchkeyWord).length()>0 && CommonUtility.validateString(request.getParameter("hasSearchKeyword")).length() < 0){
				pageNo = "0";
			}
			int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
			int fromRow =  (noOfPage-1)*resultPerPage + 1;
			int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;

			String tempSubset = (String) session.getAttribute("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			//recommendedItem = ProductsDAO.getFeatureProduct(subsetId, 0);
			//savedGroupId = CommonUtility.validateNumber(request.getParameter("savedGroupId"));
			if(request.getParameter("shared")!=null && request.getParameter("shared").trim().length()>0){
				contentObject.put("isShared",request.getParameter("shared"));
			}else{
				contentObject.put("isShared","N");
			}
			System.out.println(request.getParameter("reqType"));

			if(advancedProductGroup!=null && advancedProductGroup.trim().equalsIgnoreCase("Y") && request.getParameter("reqType")!=null && request.getParameter("reqType").trim().equalsIgnoreCase("P")){
				contentObject = ProductsDAO.getAdvancedProductListDataDao(session,contentObject,savedGroupId, assignedShipTo, approveSenderid, updatedDate, savedGroupName);
				contentObject.put("responseType","ProductGroupPage");
			}else if(CommonUtility.validateString(searchkeyWord).length()>0){
				fromRow =  (noOfPage-1)*resultPerPage;
				//String keywordForSearch = CommonUtility.validateString(searchkeyWord).replaceAll("[^a-zA-Z0-9\\s]", "");
				productListData = ProductHunterSolr.myProductGroupSearch(searchkeyWord, savedGroupId, userId, subsetId, generalSubset, fromRow, resultPerPage, sortBy, sessionId, entityId, homeTeritory, type, userName);
				if(reqType!=null){
					String targetPage = "";
					if(reqType.equalsIgnoreCase("C")){
						targetPage="SavedCartGroup";
					}
					if(reqType.equalsIgnoreCase("A")){
						targetPage="ApproveCart";
					}
					if(reqType.equalsIgnoreCase("GP")){
						targetPage="PromotedProductsList";
					}
					contentObject.put("responseType", targetPage);
					contentObject.put("reqType",reqType);
				}
				contentObject.put("savedGroupName", savedGroupName);

			}else if(request.getParameter("reqType")!=null && request.getParameter("reqType").trim().equalsIgnoreCase("A")){
				if(CommonUtility.validateString(alwaysApprover).equalsIgnoreCase("Y")){
					contentObject.put("alwaysApprover", alwaysApprover);  
					  /*if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("Extended_Catalog_Search_Enabled")).equalsIgnoreCase("Y")){
					//contentObject = ProductsDAO.getApproveCartList(session,contentObject,savedGroupId, assignedShipTo, approveSenderid, updatedDate, savedGroupName,fromRow,toRow,sortBy,requestTokenId);
					contentObject = CartProductController.getApproveCartList(session,contentObject,savedGroupId, assignedShipTo, approveSenderid, updatedDate, savedGroupName,fromRow,toRow,sortBy,requestTokenId);
					  }
					  else
					  {
						  contentObject = ProductsDAO.getApproveCartList(session,contentObject,savedGroupId, assignedShipTo, approveSenderid, updatedDate, savedGroupName,fromRow,toRow,sortBy,requestTokenId);  
					  }*/
					  contentObject = ProductsDAO.getApproveCartList(session,contentObject,savedGroupId, assignedShipTo, approveSenderid, updatedDate, savedGroupName,fromRow,toRow,sortBy,requestTokenId);  
					  
				}else{
				
					contentObject = ProductsDAO.getProductListDataDao(session,contentObject,savedGroupId, assignedShipTo, approveSenderid, updatedDate, savedGroupName,fromRow,toRow,sortBy,reqType);
					
				}
				
				productListData = (ArrayList<ProductsModel>) contentObject.get("productListData");
				
			}

			else{
				contentObject = ProductsDAO.getProductListDataDao(session,contentObject,savedGroupId, assignedShipTo, approveSenderid, updatedDate, savedGroupName,fromRow,toRow,sortBy,reqType);
				productListData = (ArrayList<ProductsModel>) contentObject.get("productListData");
			}
			if(productListData!=null && productListData.size()>0){
				resultCount = productListData.get(0).getResultCount();
				ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();

				for(ProductsModel productsModel : productListData){
					itemList.add(productsModel.getItemId());
					partIdentifierQuantity.add(productsModel.getQty());
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_LINKED_ITEMS_IN_GROUPS")).equalsIgnoreCase("Y")){
						productsModel.setLinkedItemsList(ProductsDAO.getLinkedItems(productsModel.getItemId(), subsetId));
					}
				}
				
				if (itemList!=null && itemList.size()>0){
					LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = ProductHunterSolr.getcustomerPartnumber(StringUtils.join(itemList," OR "), CommonUtility.validateNumber(buyingCompanyId), CommonUtility.validateNumber(buyingCompanyId));
					if(customerPartNumber!=null && customerPartNumber.size()>0){
						for(ProductsModel productsModel : productListData) {
							productsModel.setCustomerPartNumberList(customerPartNumber.get(productsModel.getItemId()));
						}
					}
				}
				
				if(CommonUtility.validateString(searchkeyWord).length()>0 && (productListData!=null && productListData.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults")))
				{
					ProductManagement priceInquiry = new ProductManagementImpl();
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					priceInquiryInput.setHomeTerritory(homeTerritory);
					priceInquiryInput.setPartIdentifier(productListData);
					priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
					priceInquiryInput.setRequiredAvailabilty("Y");
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					productListData = priceInquiry.priceInquiry(priceInquiryInput , productListData);
				}
			}
			
			System.out.println("ID List : "+StringUtils.join(itemList," "));
			double disp;
			double disp1;
			disp = resultCount;
			if (resultCount > resultPerPage) {
				paginate = (resultCount / resultPerPage);
				disp1 = (disp / resultPerPage);
			} else {
				paginate = (resultCount / resultPerPage);
				disp1 = (resultCount / resultPerPage);
			}
			if (disp1 > paginate) {
				paginate = paginate + 1;
			}
			LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = null;
			if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
				customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "),"itemid");
			}
			if(productListData!=null && productListData.size()>0 && CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA")!=null && CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA").trim().equalsIgnoreCase("Y")){
				if(itemList!=null && itemList.size()>0){
					LinkedHashMap<Integer, LinkedHashMap<String, ProductsModel>> shipOrderQtyAndIntervalByItem = ProductHunterSolr.getshipOrderQtyAndIntervalByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "));
					contentObject.put("shipOrderQtyAndInterval", shipOrderQtyAndIntervalByItem);
					if(session!=null && session.getAttribute("selectedShipVia")!=null){
						ArrayList<ProductsModel> productListDataTemp = new ArrayList<ProductsModel>();
						for(ProductsModel productModel : productListData){
							LinkedHashMap<String, ProductsModel> getShipMinOrderAndInterval = shipOrderQtyAndIntervalByItem.get(productModel.getItemId());
							ProductsModel shipOrderQtyAndIntervalModel = new ProductsModel();
							if(getShipMinOrderAndInterval!=null && getShipMinOrderAndInterval.size()>0){
								shipOrderQtyAndIntervalModel = getShipMinOrderAndInterval.get(CommonUtility.validateString(session.getAttribute("selectedShipVia").toString()).trim().toUpperCase());
								if(shipOrderQtyAndIntervalModel!=null && shipOrderQtyAndIntervalModel.getMinOrderQty()>0){
									productModel.setMinOrderQty(shipOrderQtyAndIntervalModel.getMinOrderQty());
								}
								if(shipOrderQtyAndIntervalModel!=null && shipOrderQtyAndIntervalModel.getOrderInterval()>0){
									productModel.setOrderInterval(shipOrderQtyAndIntervalModel.getOrderInterval());
								}
							}
							productListDataTemp.add(productModel);
						}
						if(productListDataTemp!=null && productListDataTemp.size()>0){
							productListData = productListDataTemp;
						}
					}
				}
			}

			String cartWareHouseName = "";
			for(ProductsModel productsModel : productListData){
				if(productsModel.getMultipleShipVia() != null && !productsModel.getMultipleShipVia().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.ship.method.name"))){
					if(productsModel.getMultipleShipViaDesc()!=null && productsModel.getMultipleShipViaDesc().contains(",")){
						LinkedHashMap<String,String> list = CommonDBQuery.getWareHouseList();
						if(list != null && list.size() > 0){
							cartWareHouseName = list.get(productsModel.getMultipleShipViaDesc().split(",")[1]);
						}
					}
				}
			}
			
			if(productListData!=null && productListData.size()>1 && sortBy.equalsIgnoreCase("CPNAsc")){
				Collections.sort(productListData, ProductsModel.cpnAscendingComparator);
			}
			if(productListData!=null && productListData.size()>1 &&sortBy.equalsIgnoreCase("CPNDesc")){
				Collections.sort(productListData, ProductsModel.cpnDescendingComparator);
			}
	
			//Adapt_Pharma multiple shipping address start
			if(CommonUtility.customServiceUtility() != null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MultipleShipToAddressCartAP")).equalsIgnoreCase("Y")) {
				contentObject = CommonUtility.customServiceUtility().getAllShippingAddress(contentObject, CommonUtility.validateNumber(buyingCompanyId));
				//contentObject.put("shipAddressList", contentObject.get("shipEntity"));
			}
			//Adapt_Pharma multiple shipping address end
			
			// Turtle Approval Chain- Tishman Speyer start
			if(CommonUtility.customServiceUtility()!=null)
			{
				CommonUtility.customServiceUtility().getSecondApproverThresholdrange(contentObject);	
			}
			// Turtle Approval Chain- Tishman Speyer end
			
			contentObject.put("groupType",request.getParameter("groupType"));
			contentObject.put("savedCartTotal",contentObject.get("Total"));
			contentObject.put("customFieldVal", customFieldVal);
			contentObject.put("requesterTokenId", requestTokenId);
			contentObject.put("recommendedItem", recommendedItem);
			contentObject.put("savedGroupId",savedGroupId);
			contentObject.put("assignedShipTo",assignedShipTo);
			contentObject.put("resultPage", resultPage);
			contentObject.put("pageNo", pageNo);
			contentObject.put("resultCount", resultCount);
			contentObject.put("paginate", paginate);
			contentObject.put("sortBy", sortBy);
			contentObject.put("productListData", productListData);
			contentObject.put("searchkeyWord", searchkeyWord);
			contentObject.put("siteShipViaList", CommonDBQuery.getSiteShipViaList());
			if(contentObject.get("savedGroupName") != null && CommonUtility.validateString((String)contentObject.get("savedGroupName")).length() == 0 && CommonUtility.validateString(savedGroupName).length() > 0) {
				contentObject.put("savedGroupName", savedGroupName);
			}
			if(CommonUtility.validateString(toCart).equalsIgnoreCase("ALL")) {
				//Add to cart
				/*if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("QUICK_ORDER_ADD_ZERO_PRICE_ITEM")).equalsIgnoreCase("Y")){
				
				}*/
				/*ProductsModel addToCartList = new ProductsModel();
				addToCartList.setPartNumber("");
				addToCartList.setItemId(0);
				addToCartList.setItemPriceId(0);
				addToCartList.setQty(0);
				addToCartList.setOrderInterval(0);
				addToCartList.setMinimumOrderQuantity(0);*/
				String jsonResponse = "";
				ArrayList<ProductsModel> productListDataList = new ArrayList<ProductsModel>();
				if(productListData!=null && !productListData.isEmpty()) {
					for(ProductsModel productModel : productListData){
						ProductsModel addToCartList = new ProductsModel();
						addToCartList.setPartNumber(productModel.getPartNumber());
						addToCartList.setItemId(productModel.getItemId());
						addToCartList.setItemPriceId(productModel.getItemPriceId());
						addToCartList.setQty(productModel.getQty());
						addToCartList.setOrderInterval(productModel.getOrderInterval());
						addToCartList.setMinimumOrderQuantity(productModel.getMinimumOrderQuantity());
						addToCartList.setPrice(productModel.getPrice());
						addToCartList.setUom(productModel.getUom());
						if(!(CommonUtility.validateString(productModel.getUom()).trim().length() > 0)){
							addToCartList.setUom(productModel.getUomPack());
						}
						addToCartList.setRequestType("");
						productListDataList.add(addToCartList);
					}
					Gson convertJson = new Gson();
					jsonResponse = convertJson.toJson(productListDataList);
					System.out.println(jsonResponse);
					renderContent = convertJson.toJson(productListDataList);
				}else {
					renderContent = "Cannot process your request !";
				}
				//Add to Cart
			}else if(contentObject.get("responseType")!=null && CommonUtility.validateString(contentObject.get("responseType").toString()).equalsIgnoreCase("SavedCartGroup")){
				renderContent = LayoutGenerator.templateLoader("SavedCartPage", contentObject , null, null, null);
			}else if(contentObject.get("responseType")!=null && CommonUtility.validateString(contentObject.get("responseType").toString()).equalsIgnoreCase("ApproveCart")){
				renderContent = LayoutGenerator.templateLoader("ApproveCart", contentObject , null, null, null);
			}else if((contentObject.get("responseType")!=null && CommonUtility.validateString(contentObject.get("responseType").toString()).equalsIgnoreCase("PromotedProductsList")) || (CommonUtility.validateString(reqType).equalsIgnoreCase("GP"))){
				renderContent = LayoutGenerator.templateLoader("PromotedProductsListPage", contentObject , null, null, null);
			}else if(advancedProductGroup!=null && advancedProductGroup.trim().equalsIgnoreCase("Y") && request.getParameter("reqType")!=null && request.getParameter("reqType").trim().equalsIgnoreCase("P")){
				contentObject.put("levelNo", CommonUtility.validateNumber(levelNo)+1);
				HashMap<String, ArrayList<ProductsModel>> productGroup = ProductHunterSolr.getProductGroupAndItems(savedGroupId, userId,fromRow,resultPerPage,keyWord);
				itemLevelFilterData = productGroup.get("itemList");
				contentObject.put("groupListData", productGroup.get("groupListData"));
				contentObject.put("productListData", itemLevelFilterData);
				contentObject.put("parentId", savedGroupId);
				renderContent = LayoutGenerator.templateLoader("AdvancedProductGroup", contentObject , null, null, null);
				if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
					resultCount = itemLevelFilterData.get(0).getResultCount();
					disp = resultCount;
					if (resultCount > resultPerPage) {
						paginate = (resultCount / resultPerPage);
						disp1 = (disp / resultPerPage);
					} else {
						paginate = (resultCount / resultPerPage);
						disp1 = (resultCount / resultPerPage);
					}
					if (disp1 > paginate) {
						paginate = paginate + 1;
					}
					contentObject.put("resultPage", resultPage);
					contentObject.put("pageNo", pageNo);
					contentObject.put("resultCount", resultCount);
					contentObject.put("paginate", paginate);
				}
			}else{
				renderContent = LayoutGenerator.templateLoader("ProductGroupPage", contentObject , null, null, null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return target;
	}
	public String updateGroupItems(){
		target = "MyProductsPage";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		shared = CommonUtility.validateString(request.getParameter("isSharedShoppingList"));
		boolean login = (Boolean) session.getAttribute("userLogin");
		requestTokenId=CommonUtility.validateNumber(request.getParameter("requestTokenId"));
		
		if(login){
	    try{
        	System.out.println("savedGroupId @ updateGroupItems : "+savedGroupId);
        	String advancedProductGroup = CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP");
        	if(advancedProductGroup!=null && advancedProductGroup.trim().equalsIgnoreCase("Y")){
        		ProductsDAO.updateAdvancedGroupItemsDao(shoppingCartId,shoppingCartQty,idList,savedGroupId);
        	}else if(CommonUtility.validateString(alwaysApprover).length() > 0 && alwaysApprover.equalsIgnoreCase("Y")){
        		request =ServletActionContext.getRequest();
        		
        		ProductsDAO.updateAppCartGroupItemsDao(session,lineItemIdList,shoppingCartQty,idList,savedGroupId,requestTokenId,approveSenderid);
        		target = "success";
        	}        	
        	else{
        		ProductsDAO.updateGroupItemsDao(shoppingCartId,shoppingCartQty,idList,savedGroupId);	
        	}
        }catch(Exception e){
	    	e.printStackTrace();
		}
		}
		return target;
	}
	
	public String deleteSavedCart(int savedGroupId){
		target = "MyProductsPage";
		try{
	    	System.out.println("savedGroupId @ deleteSavedCart : "+savedGroupId);
	    	//String advancedProductGroup = CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP");
        	/*if(CommonUtility.validateString(advancedProductGroup).equalsIgnoreCase("Y")){
        		ProductsDAO.deleteSavedCartDao(savedGroupId);
        	}else{*/
        		ProductsDAO.deleteSavedCartDao(savedGroupId);	
        	//}
		}catch(Exception e){
			e.printStackTrace();
		}
		return target;
	}
	
	public String deleteGroupItem(){
		target = "MyProductsPage";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		boolean login = (Boolean) session.getAttribute("userLogin");
		if(login){
			 try{
				 
				 if(savedGroupItemId>0){
					 idList = new ArrayList<Integer>();
					 idList.add(savedGroupItemId);
				 }
				 
		    	 boolean flag = ProductsDAO.deleteGroupItemDao(shoppingCartId, idList, savedGroupId);
		    	 shared = CommonUtility.validateString(request.getParameter("isSharedShoppingList"));
		    	 if(!flag){
			    	 if(reqType!=null && reqType.trim().length()>0){
			 			if(reqType.trim().equalsIgnoreCase("P")){
			 				if(CommonDBQuery.getSystemParamtersList().get("MYPRODUCTGROUP_DELETE_REDIRECT_PAGE")!=null && CommonDBQuery.getSystemParamtersList().get("MYPRODUCTGROUP_DELETE_REDIRECT_PAGE").trim().equalsIgnoreCase("Y"))
			 					target = "ToProductPage";
			 				else
			 					target = "ToSavedGroup";
			 			}
			 			if(reqType.trim().equalsIgnoreCase("C")){
			 				if(CommonDBQuery.getSystemParamtersList().get("MYPRODUCTGROUP_DELETE_REDIRECT_PAGE")!=null && CommonDBQuery.getSystemParamtersList().get("MYPRODUCTGROUP_DELETE_REDIRECT_PAGE").trim().equalsIgnoreCase("Y"))
			 					target = "ToProductPage";
			 				else
			 					target = "ToSavedGroup";
			 			}
			 			if(reqType.trim().equalsIgnoreCase("A")){
			 				if(CommonDBQuery.getSystemParamtersList().get("MYPRODUCTGROUP_DELETE_REDIRECT_PAGE")!=null && CommonDBQuery.getSystemParamtersList().get("MYPRODUCTGROUP_DELETE_REDIRECT_PAGE").trim().equalsIgnoreCase("Y"))
			 					target = "ToProductPage";
			 				else
			 					target = "ToSavedGroup";
			 			}
			 		}
		    	 }
				}catch(Exception e){
			       	e.printStackTrace();
			    }
			}
			return target;
		}
	public String RejectApprCart(){
		target = "ResultLoader";
		 renderContent = "";
	     try{
	    	 request =ServletActionContext.getRequest();
	    	 HttpSession session = request.getSession();
	    	 result = ProductsDAO.rejectApprCartDao(session, savedGroupId, rejectReason);
			}catch(Exception e){
		       	e.printStackTrace();
		    }
	     	System.out.println("result : "+result);
	     	renderContent = result;
			return target;
	}
	public String addGroupToCart(){
		target = "ShoppingCart";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String sessionId = session.getId();
		int userId = CommonUtility.validateNumber(sessionUserId);
        try
        {
        	ProductsDAO.addGroupToCartDao(shoppingCartId, shoppingCartQty, idList,lineItemIdList,userId, sessionId, price);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	session.removeAttribute("cartCountSession");
        }
       return target;
	}
	public String deleteGroup(){
		try{
			String advancedProductGroup = CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP");
			if(advancedProductGroup!=null && advancedProductGroup.trim().equalsIgnoreCase("Y") && reqType!=null && reqType.trim().equalsIgnoreCase("P")){
				ProductsDAO.deleteSavedGroup(savedGroupId,"AP");
			}else{
				ProductsDAO.deleteSavedGroup(savedGroupId,"");
			}
		}catch(Exception e){
        	e.printStackTrace();
        }
		return "ToSavedGroup";
	}
	public String editGroupName(){
		renderContent = "";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String newName=request.getParameter("newName");
		String groupId = request.getParameter("groupId");
		String oldGroupName = request.getParameter("oldGroupName");
		String GroupType = request.getParameter("GroupType");
		boolean login = (Boolean) session.getAttribute("userLogin");
		int res = 0;
		if(login){
			try{
				String advancedProductGroup = CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP");
				if(advancedProductGroup!=null && advancedProductGroup.trim().equalsIgnoreCase("Y") && GroupType!=null && GroupType.trim().equalsIgnoreCase("P")){
					GroupType = "AP";
					res = ProductsDAO.updateGroupName(groupId, newName,sessionUserId,oldGroupName,GroupType);
				}else{
					res = ProductsDAO.updateGroupName(groupId, newName,sessionUserId,oldGroupName,GroupType);
				}
				if(res==1){
					result = "1";
				}else if(res==3){
					result = "3";
				}else{
					result = "0";
				}
				renderContent = result;
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			renderContent = "-1";
		}
		return "ResultLoader";
	}
	public String addCustomerPartNumber(){
		int entityId = 0;
		int resultInt = -2;
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		String result = "";
		try{
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String shiptoId = "";

			if(session.getAttribute("selectedshipToIdSx")!=null){
				shiptoId = CommonUtility.validateString(session.getAttribute("selectedshipToIdSx").toString());
			}
			//String userEntity = (String) session.getAttribute("customerEntity");
			String userEntity = (String) session.getAttribute("customerId");
			if(userEntity!=null){
				entityId = CommonUtility.validateNumber(userEntity);
			}else{
				entityId = buyingCompanyId;
			}
			//-------------------------- write to ERP
			ProductManagement productOBJ = new ProductManagementImpl();
			String[] wordArray = keyWord.split(",");
			ProductManagementModel customerPartNumberInquiryInput = new ProductManagementModel();
			customerPartNumberInquiryInput.setEntityId(CommonUtility.validateString((String) session.getAttribute("entityId")));
			customerPartNumberInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
			customerPartNumberInquiryInput.setProductNumber(partNumber);
			customerPartNumberInquiryInput.setCustomerPartNumbers(wordArray);
			customerPartNumberInquiryInput.setRequiredAvailabilty("Y");
			customerPartNumberInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
			customerPartNumberInquiryInput.setUserToken((String)session.getAttribute("userToken"));
			customerPartNumberInquiryInput.setCustomerId((String) session.getAttribute("customerId"));
			customerPartNumberInquiryInput.setNewCustomerPartNumber(keyWord);
			ProductManagementModel customerPartNumberInquiryOutPut = productOBJ.customerPartNumberCreate(customerPartNumberInquiryInput);
			//--------------------------
			if(customerPartNumberInquiryOutPut!=null && customerPartNumberInquiryOutPut.getErrorCode()!=null && customerPartNumberInquiryOutPut.getErrorCode().trim().equalsIgnoreCase("0") && customerPartNumberInquiryOutPut.getErrorMessage()==null){
				if(CommonUtility.validateString(customerPartNumberInquiryOutPut.getRecordType()).equalsIgnoreCase("c")){
					shiptoId = "";
				}
				resultInt = ProductHunterSolr.addNewCustomerPartNumber(keyWord, itemPriceId,  userId, entityId, useBillToEntity, buyingCompanyId,shiptoId,customerPartNumberInquiryOutPut.getRecordType(),customerPartNumberInquiryOutPut.getRecordDescription());
			}else if(customerPartNumberInquiryOutPut!=null && customerPartNumberInquiryOutPut.getErrorCode()!=null && customerPartNumberInquiryOutPut.getErrorCode().trim().equalsIgnoreCase("-2")){
				resultInt = -2;
				result = CommonUtility.validateString(customerPartNumberInquiryOutPut.getErrorMessage());
			}
			ArrayList<ProductsModel> products = new ArrayList<ProductsModel>();
			if(resultInt != 0) {
				if(resultInt == -1){
					renderContent = "Please Enter Valid Customer Part Number|";
				}else if(resultInt == -2 && CommonUtility.validateString(result).length()>0 ){
					renderContent = "";
				}else{
					renderContent = "Customer Part Number Added Successfully|";
				}
				LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartnumber = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemPriceId), buyingCompanyId, buyingCompanyId);
				customerPartNumberList = customerPartnumber.get(itemPriceId);
				if(customerPartNumberList!=null){
					for(ProductsModel cc : customerPartNumberList){
						if(renderContent.equalsIgnoreCase("Customer Part Number Added Successfully|")){
							renderContent = renderContent +  cc.getPartNumber() ;
						}else{
							if(CommonUtility.validateString(renderContent).length()>0){
								renderContent = renderContent + ","+ cc.getPartNumber() ;
							}else{
								renderContent = cc.getPartNumber() ;
							}
							
						}
					}
					if(CommonDBQuery.getSystemParamtersList().get("NOTIFY_ON_ADDING_CPN")!=null && CommonDBQuery.getSystemParamtersList().get("NOTIFY_ON_ADDING_CPN").trim().equalsIgnoreCase("Y")){
						contentObject.put("customerPartNumberList", customerPartNumberInquiryInput);
						contentObject.put("renderContent", renderContent);
						SendMailUtility sendMailUtility = new SendMailUtility();
						sendMailUtility.sendCustomNotification(contentObject);
					}
				}
					
			}else{
				renderContent = "Error occured while process your request|";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ResultLoader";
	}
	public String removeCustomerPartNumber(){
		int resultInt = -2;
		String result = "";
		request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
	    int userId = CommonUtility.validateNumber(sessionUserId);
	    String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
		int count = -1;
		try
		{
			if(vpsid!=null && vpsid.length()>0 && !vpsid.trim().equalsIgnoreCase("")){
				String cutomerPartList[] = vpsid.split(",");
				String cutomerPartNumArray[] = custPnum.split(",");
				//-------------------------- write to ERP
				ProductManagement productOBJ = new ProductManagementImpl();
				String entityId = (String) session.getAttribute("entityId");
				ProductManagementModel customerPartNumberInquiryInput = new ProductManagementModel();
				customerPartNumberInquiryInput.setEntityId(CommonUtility.validateString(entityId));
				customerPartNumberInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
				customerPartNumberInquiryInput.setProductNumber(partNumber);
				customerPartNumberInquiryInput.setCustomerPartNumbers(cutomerPartNumArray);
				customerPartNumberInquiryInput.setRequiredAvailabilty("Y");
				customerPartNumberInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				customerPartNumberInquiryInput.setUserToken((String)session.getAttribute("userToken"));
				customerPartNumberInquiryInput.setCustomerId((String) session.getAttribute("customerId"));
				ProductManagementModel customerPartNumberInquiryOutPut = productOBJ.customerPartNumberDelete(customerPartNumberInquiryInput);
				//--------------------------
				if(customerPartNumberInquiryOutPut!=null && customerPartNumberInquiryOutPut.getErrorCode()!=null && customerPartNumberInquiryOutPut.getErrorCode().trim().equalsIgnoreCase("0") && customerPartNumberInquiryOutPut.getErrorMessage()==null){
					resultInt = ProductHunterSolr.removeCustomerPartNumber(cutomerPartList, vpsid);
				}else if(customerPartNumberInquiryOutPut!=null && customerPartNumberInquiryOutPut.getErrorCode()!=null && customerPartNumberInquiryOutPut.getErrorCode().trim().equalsIgnoreCase("-2")){
					resultInt = -2;
					result = CommonUtility.validateString(customerPartNumberInquiryOutPut.getErrorMessage());
				}
			}
		
			if(resultInt != 0) {
					renderContent = "Customer Part Number Removed Successfully|";
					LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartnumber = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemPriceId), buyingCompanyId, buyingCompanyId);
					if(customerPartnumber!=null && customerPartnumber.size()>0){
						customerPartNumberList = customerPartnumber.get(itemPriceId);
						if(customerPartNumberList!=null && customerPartNumberList.size()>0){
							for(ProductsModel cc : customerPartNumberList){
								if(renderContent.equalsIgnoreCase("Customer Part Number Removed Successfully|")){
									renderContent = renderContent +  cc.getPartNumber() ;
								}else{
										renderContent = renderContent + ","+ cc.getPartNumber() ;
								}
							}
						}
					}
					
				
			}else{
				renderContent = "Error occured while process your request";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ResultLoader";
	}
	
	public String customerPartNumbers(){
		System.out.println("Inside product Group");
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		try{
		    request =ServletActionContext.getRequest();
		    HttpSession session = request.getSession();
		    String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		    int userId = CommonUtility.validateNumber(sessionUserId);
		    String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
			int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
			//String tempcustomerBuyingCompanyId = (String) session.getAttribute("customerBuyingCompanyId");
			//int customerBuyingCompanyId = CommonUtility.validateNumber(tempcustomerBuyingCompanyId);
			LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartnumber = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemPriceId), buyingCompanyId, buyingCompanyId);
			customerPartNumberList = customerPartnumber.get(itemPriceId);
			
			//customerPartNumberList = sysncronizeCustomerPartNumber(session, partNumber, itemPriceId, useBillToEntity, customerPartNumberList); Sysncronizeeeee
			
			
			//----------------- Customer Part Number From ERP
			ProductManagement productOBJ = new ProductManagementImpl();
			ArrayList<ProductsModel> customerPartNumberModel = null;
			
			if(customerPartNumberList!=null && customerPartNumberList.size()>0){
				customerPartNumberModel = customerPartNumberList;
			}else{
				customerPartNumberModel = new ArrayList<ProductsModel>();
			}
			ProductManagementModel customerPartNumberInquiryInput = new ProductManagementModel();
			String entityId = (String) session.getAttribute("entityId");
			customerPartNumberInquiryInput.setEntityId((entityId));
			customerPartNumberInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
			customerPartNumberInquiryInput.setProductNumber(partNumber);
			customerPartNumberInquiryInput.setRequiredAvailabilty("Y");
			customerPartNumberInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
			customerPartNumberInquiryInput.setUserToken((String)session.getAttribute("userToken"));
			ArrayList<ProductsModel> customerPartNumberModelErp = productOBJ.customerPartNumberQuery(customerPartNumberInquiryInput);
			/*if(customerPartNumberModelErp!=null && customerPartNumberModelErp.size()>0){
				for(ProductsModel custPartNumModelERP : customerPartNumberModelErp){
					customerPartNumberModel.add(custPartNumModelERP);
					
				}
			}*/
			if(customerPartNumberModelErp!=null && customerPartNumberModelErp.size()>0){
				customerPartnumber = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemPriceId), buyingCompanyId, buyingCompanyId);
				customerPartNumberList = customerPartnumber.get(itemPriceId);
				if(customerPartNumberList!=null && customerPartNumberList.size()>0){
					customerPartNumberModel = customerPartNumberList;
				}
			}
			//--------- Customer Part Number From ERP
				
			contentObject.put("responseType", "CustomerPartNumberList");
			contentObject.put("customerPartNumberList", customerPartNumberList);
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ResultLoader";
	}
	
	public ArrayList<ProductsModel> synchronizeCustomerPartNumber(HttpSession session, String partNumber,int itemPriceId, String useBillToEntity, ArrayList<ProductsModel> customerPartNumberList){
		
		try {
			ProductManagement productOBJ = new ProductManagementImpl();
			ArrayList<String> solrList = new ArrayList<String>();
			ArrayList<String> erpList = new ArrayList<String>();
			ArrayList<String> solrResultList = new ArrayList<String>();
			ArrayList<String> erpResultList = new ArrayList<String>();
			LinkedHashMap<String, ProductsModel> extraDetails = new LinkedHashMap<String, ProductsModel>();
			String shiptoId ="";
			if(session.getAttribute("selectedshipToIdSx")!=null){
				shiptoId = CommonUtility.validateString(session.getAttribute("selectedshipToIdSx").toString());
			}
			
			
			LinkedHashMap<String, String> removeList = new LinkedHashMap<String, String>();
			String solrListIdStr = "";
			String delimit = "";
			
			if(customerPartNumberList!=null && customerPartNumberList.size()>0){
				for(ProductsModel pModel : customerPartNumberList){
					solrList.add(pModel.getPartNumber());
					removeList.put(pModel.getPartNumber(),pModel.getIdList());
				}
			}
			
			ProductManagementModel customerPartNumberInquiryInput = new ProductManagementModel();
			customerPartNumberInquiryInput.setEntityId(CommonUtility.validateString((String) session.getAttribute("entityId")));
			customerPartNumberInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
			customerPartNumberInquiryInput.setProductNumber(partNumber);
			customerPartNumberInquiryInput.setRequiredAvailabilty("Y");
			customerPartNumberInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
			customerPartNumberInquiryInput.setUserToken((String)session.getAttribute("userToken"));
			customerPartNumberInquiryInput.setCustomerId((String) session.getAttribute("customerId"));
			if(session.getAttribute("selectedshipToIdSx")!=null){
				customerPartNumberInquiryInput.setShipToId(CommonUtility.validateString(session.getAttribute("selectedshipToIdSx").toString()));
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_OARTNUMBER_RECORD_TYPE")).length()>0){
				customerPartNumberInquiryInput.setRecordType(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_OARTNUMBER_RECORD_TYPE")));//C refer customer# at Customer level,  H refer customer Part# at ship to level
			}else{
				customerPartNumberInquiryInput.setRecordType("c");
			}
			
			ArrayList<ProductsModel> customerPartNumberModelErp = productOBJ.customerPartNumberQuery(customerPartNumberInquiryInput);
			if(customerPartNumberModelErp!=null && customerPartNumberModelErp.size()>0){
				for(ProductsModel custPartNumModelERP : customerPartNumberModelErp){
					if(custPartNumModelERP.getPartNumber().equals(partNumber)) {
					erpList.add(custPartNumModelERP.getCustomerPartNumber()); // Partnumber
					ProductsModel prodModel = new ProductsModel();
					prodModel.setRecordDescription(custPartNumModelERP.getRecordDescription());
					prodModel.setRecordType(custPartNumModelERP.getRecordType());
					extraDetails.put(custPartNumModelERP.getCustomerPartNumber(), prodModel);
					}
				}
			}
			
			solrResultList = new ArrayList(solrList);
			solrResultList.removeAll(erpList);
			if(solrResultList!=null && solrResultList.size()>0){
				for(int i=0; i<solrResultList.size(); i++){
					System.out.println("solrResultList : "+solrResultList.get(i));
					if(removeList!=null){
						solrListIdStr = solrListIdStr+delimit+removeList.get(solrResultList.get(i));
						delimit = ",";
					}
				}
				System.out.println("solrListIdStr : "+solrListIdStr);
				int removeCustomerPartNumberFmDB = -1;
				if(solrListIdStr!=null && solrListIdStr.trim().length()>0){
					String cutomerPartList[] = solrListIdStr.split(",");
					removeCustomerPartNumberFmDB = ProductHunterSolr.removeCustomerPartNumber(cutomerPartList, solrListIdStr);
					System.out.println("Sync Customer Part#  Remove from DB : "+solrListIdStr+" : "+removeCustomerPartNumberFmDB);
				}
			}
			
			erpResultList = new ArrayList(erpList);
			erpResultList.removeAll(solrList);
			if(erpResultList!=null && erpResultList.size()>0){
				for(int i=0; i<erpResultList.size(); i++){
					int addCustomerPartNumberToDB = -1;
					System.out.println("erpResultList : "+erpResultList.get(i));

					if(CommonUtility.validateString(extraDetails.get(erpResultList.get(i)).getRecordType()).equalsIgnoreCase("c")){
						shiptoId = "";
					}else{
						shiptoId = CommonUtility.validateString(session.getAttribute("selectedshipToIdSx").toString());
					}
					addCustomerPartNumberToDB = ProductHunterSolr.addNewCustomerPartNumber(erpResultList.get(i), itemPriceId, CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY)), CommonUtility.validateNumber((String) session.getAttribute("entityId")), useBillToEntity, CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId")),shiptoId,extraDetails.get(erpResultList.get(i)).getRecordType(),extraDetails.get(erpResultList.get(i)).getRecordDescription());
					System.out.println("Sync Customer Part#  add to DB : "+erpResultList.get(i)+" : "+addCustomerPartNumberToDB);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerPartNumberList;
		
	}
	
	public String addReview()
	{
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			
			int userId = CommonUtility.validateNumber(sessionUserId);
			ProductsModel reviewDetails = new ProductsModel();
			reviewDetails.setComments(comments);
			reviewDetails.setItemId(itemIdRev);
			reviewDetails.setRating(Float.parseFloat(rating));
			reviewDetails.setTitle(title);
			reviewDetails.setUserAdded(Integer.toString(userId));
			boolean flag = ProductsDAO.addItemReview(reviewDetails);
			if(flag){
				result = "0|Success";
			}else{
				result = "1|UnSuccessFull";
			}
			renderContent = result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS; 
		
	}
	
	public String saveCart(){
		target = "ResultLoader";
		try{
		    request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String groupType = CommonUtility.validateString(request.getParameter("groupType"));
			if(CommonUtility.validateString(groupType).length() == 0) {
				groupType = "C";
			}
			savedGroupId = ProductsDAO.saveCartDao(userId, session.getId(), isReOrder, listId, listName, groupType);
		}catch (Exception e) {
			e.printStackTrace();
		}
		renderContent = validateResponse+"|"+savedGroupId+"|"+errorMsg;
		return target;
	}
	public String getMultipleUOM(){
		String jsonResponse = "";
		try{
			if(CommonUtility.validateString(partNumber).length()>0){
				ProductManagement uomInquiry = new ProductManagementImpl();
				ArrayList<ProductsModel>  result = uomInquiry.itemsMultipleUOMList(partNumber);
				if(result!=null){
					Gson convertJson = new Gson();
					jsonResponse = convertJson.toJson(result);
				}
			}
			renderContent = jsonResponse;
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String getPriceDetail()
	{
		long startTimer = CommonUtility.startTimeDispaly();
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		    int userId = CommonUtility.validateNumber(sessionUserId);
		    String uoms = request.getParameter("uom");
		    String loadAllBranchAvailability = request.getParameter("LABAvailability");
		    String customParamter = request.getParameter("customParamter");
		    String loadUomList = request.getParameter("loadUomList");
			String jsonResponse = "";
			if(productIdList!=null && !productIdList.trim().equalsIgnoreCase("")){
				String[] priceIdList = null;
				String[] uom = null;
				UserManagement usrObj = new UserManagementImpl();
				UsersModel usersModel = new UsersModel();
				UsersDAO ne = new UsersDAO();
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTNUMBER_SPLIT_CHARACTER")).length()>0) {
					priceIdList = productIdList.split(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTNUMBER_SPLIT_CHARACTER")));	
				}else {
					priceIdList = productIdList.split(",");
				}
				if(CommonUtility.validateString(uoms).contains(",")){
					uom = uoms.split(",");	
				}else if(CommonUtility.validateString(uoms).length()>0){
					uom = new String[1];
					uom[0]=uoms;
				}
				if(priceIdList!=null && priceIdList.length>0)
				{
					if(session.getAttribute("userToken")==null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("eclipse")){
						ne.checkEclipseSession();
					}
					if(session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
					{
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_ALL_BRANCH_AVAILABILITY")).equalsIgnoreCase("Y") && userId==1){
							String warehouse="";
								if(session.getAttribute("availableWarehouseList")!=null && CommonUtility.validateString(session.getAttribute("availableWarehouseList").toString()).length()>0 ){
									warehouse = CommonUtility.validateString(session.getAttribute("availableWarehouseList").toString());
								}else{
									ArrayList<WarehouseModel> whse = new ArrayList<WarehouseModel>();
									whse = UsersDAO.getWareHouseListByCustomFields();
									String split="";
									for(int i=0;i<whse.size();i++){
									warehouse = warehouse+split+whse.get(i).getWareHouseCode();
									split = ",";
									}
									session.setAttribute("availableWarehouseList", warehouse);
								}
								session.setAttribute("distributionCenter", warehouse);
						}
						if(!CommonUtility.validateString(loadAllBranchAvailability).equalsIgnoreCase("Y")){
							loadAllBranchAvailability = "N";
						}
						usersModel.setSession(session);
						usrObj.checkERPConnection(usersModel);
						ProductManagement priceInquiry = new ProductManagementImpl();
						ProductManagementModel priceInquiryInput = new ProductManagementModel();
						String entityId = (String) session.getAttribute("entityId");
						priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
						priceInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
						ArrayList<ProductsModel> productsInput = new ArrayList<ProductsModel>();
						int i=0;
						String partNumberUomQtySeparator = null;
						if(CommonUtility.customServiceUtility() != null) {
							partNumberUomQtySeparator = CommonUtility.customServiceUtility().getPartnumberUomQtySplitChar();
						}
						 for (String eachPartnumber : priceIdList) {
					            if (CommonUtility.validateString(eachPartnumber).length() > 0) {
					            	String[] prodDesc = null;
					            	if(CommonUtility.validateString(partNumberUomQtySeparator).length()>0) {
					            		prodDesc = eachPartnumber.split(CommonUtility.validateString(partNumberUomQtySeparator));	
									}else {
										prodDesc = eachPartnumber.split(":");
									}
					              ProductsModel eachProduct = new ProductsModel();
					              eachProduct.setPartNumber(prodDesc[0]);
					              if (prodDesc.length > 1) {
					                eachProduct.setUom(prodDesc[1]);
					                eachProduct.setQty(Integer.parseInt(prodDesc[2]));
					                eachProduct.setMinOrderQty(Integer.parseInt(prodDesc[2]));
					              }
	
					              productsInput.add(eachProduct);
					            }
					          }
						 
						priceInquiryInput.setPartIdentifier(productsInput);
						priceInquiryInput.setRequiredAvailabilty("Y");
						priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
						priceInquiryInput.setAllBranchavailabilityRequired(loadAllBranchAvailability);
						priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
						priceInquiryInput.setSession(session);
						priceInquiryInput.setFromDetailPage(true);
						priceInquiryInput.setWareHouse(customParamter);
						priceInquiryInput.setRequestFrom(loadUomList);
						priceInquiryInput.setZipCode(request.getParameter("zipCode"));
						jsonResponse = priceInquiry.ajaxPriceInquiry(priceInquiryInput);
						if(session!=null && session.getAttribute("connectionError")!=null && CommonUtility.validateString((String)session.getAttribute("connectionError")).equalsIgnoreCase("Yes")){
							ne.checkEclipseSession();
							jsonResponse = priceInquiry.ajaxPriceInquiry(priceInquiryInput);
							if(CommonUtility.validateString(jsonResponse).equalsIgnoreCase("")){
								renderContent = "0";	
							}else{
								renderContent = jsonResponse;	
							}
							
							System.out.println("Price Json Response:"+jsonResponse);
							
						}else{
							renderContent = jsonResponse;
						}
						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return SUCCESS;
	}
	
	public String getAvailabilityForInnovo(){
		try{
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String userToken = (String)session.getAttribute("InnovoAccessToken");
			String entityId = (String) session.getAttribute("entityId");
			String shipBranchId = (String)session.getAttribute("shipBranchId");
			String itemDetail = request.getParameter("itemDetail");
			String BranchAvailValues="";
			String pns = productIdList;
			if(entityId!=null && entityId.trim().equalsIgnoreCase("0")){
				entityId = CommonDBQuery.getSystemParamtersList().get("DEFAULT_ENTITY_ID");
			}
			if(itemDetail!=null && itemDetail.trim().equalsIgnoreCase("Y")){
				ProductManagement productObj = new ProductManagementImpl();
				BranchAvailValues = productObj.branchAvailabile(pns,CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_AVAIL_BRANCH"),entityId,userToken);
			}
			renderContent = BranchAvailValues;
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getAdvancedGroups(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			String buyingCompanyId = (String)session.getAttribute("buyingCompanyId");
			int userId = CommonUtility.validateNumber(sessionUserId);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("cartType","Advanced");
			contentObject.put("session", session);
			contentObject = ProductsDAO.getSavedGroupNameByUserIdDao(userId, contentObject, CommonUtility.validateNumber(buyingCompanyId));
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getSavedGroups(){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String buyingCompanyId = (String)session.getAttribute("buyingCompanyId");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String isGeneralUser = (String)session.getAttribute("isGeneralUser");
		try{
			if(userId>1){
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("session", session);
				contentObject.put("listWithZeroItems", listWithZeroItems);
				if(CommonUtility.validateString(reqType).equalsIgnoreCase("A") && CommonUtility.validateString(isGeneralUser).equalsIgnoreCase("Y")){
					contentObject = ProductsDAO.getPendingApproveCart(userId, contentObject, CommonUtility.validateNumber(buyingCompanyId));
				}else{
				contentObject = ProductsDAO.getSavedGroupNameByUserIdDao(userId, contentObject, CommonUtility.validateNumber(buyingCompanyId));
				}
				if(reqType!=null && reqType.trim().equalsIgnoreCase("P")){
					contentObject.put("responseType", "ProductGroupList");
				}
				if(reqType!=null && reqType.trim().equalsIgnoreCase("C")){
					contentObject.put("responseType", "CartGroupList");
				}
				if(reqType!=null && reqType.trim().equalsIgnoreCase("A")){
					contentObject.put("responseType", "ApprovedCart");
				}
				contentObject.put("reqType", reqType);
				if(CommonUtility.customServiceUtility() != null) {
					CommonUtility.customServiceUtility().filterMyCatalogGroups(contentObject, request);
				}
				renderContent = LayoutGenerator.templateLoader("SavedGroupsPage", contentObject , null, null, null);
				target = SUCCESS;
			
			}else{
				target = "SESSIONEXPIRED";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}
	
	public String updateQuoteCart(){
		target = "QuoteCart";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try
        {
			
			session.removeAttribute("quoteNumber");
			session.removeAttribute("orderNumber");
			if(session.getAttribute("quoteOrderId")!=null){
    			session.removeAttribute("quoteOrderId");
			}
        	
			if(shoppingCartId!=null && shoppingCartId.length > 0){
				for(int i=0;i<shoppingCartId.length;i++){
						String qty = shoppingCartQty[i];
						int iQty = 1;
						if(qty!=null && !qty.trim().equalsIgnoreCase(""))
							iQty = CommonUtility.validateNumber(qty);
						
						String selectedShipMethod = request.getParameter("selectedShipMethod");
			        	if(CommonUtility.validateString(selectedShipMethod).trim().length()>0){
			        		session.setAttribute("selectedShipVia",selectedShipMethod);
			        	}
						
						if(iQty>0){
							String lineComment = request.getParameter("lineItemComment_"+shoppingCartId[i]);
							String itemLevelRequiredDate= request.getParameter("reqDate_"+shoppingCartId[i]);

							ProductsDAO.updateQuoteCartDao(CommonUtility.validateNumber(shoppingCartId[i]), iQty, lineComment,itemLevelRequiredDate);

						}else{
							ProductsDAO.deleteFromQuoteCartDao(CommonUtility.validateNumber(shoppingCartId[i]));
						}
					}
				}
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
        return target;
	}
	
	public String deleteQuoteCart(){
		target = "QuoteCart";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		type = request.getParameter("type");
		String sessionId = session.getId();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		
		int userId = CommonUtility.validateNumber(sessionUserId);
	    try{
	    	if(userId>1){
	    		
	    		session.removeAttribute("quoteNumber");
	    		session.removeAttribute("orderNumber");
	    		
		       	if(deleteId!=null && CommonUtility.validateNumber(deleteId.trim())>0){
		       		ProductsDAO.deleteQuoteCartById(CommonUtility.validateNumber(deleteId));
		       		/*if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID")).length()>0){
						String[] brandIDList = CommonDBQuery.getSystemParamtersList().get("WILL_CALL_EXCLUDE_BRAND_ID").split(",");
						boolean isExcludeBranch = false;
						if(brandIDList!=null){
							for(String brandId:brandIDList){
								isExcludeBranch = ProductsDAO.getCartBrandExcludeStatus(userId,sessionId,Integer.parseInt(brandId));
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
					}*/
		      	}else if (deleteFlag!=null && deleteFlag.trim().equalsIgnoreCase("Y")) {
	       			SalesDAO.deleteQuoteCart(sessionId);
 			    }
	       }
		}catch(Exception e){
	        e.printStackTrace();
		}
	   return target;
	}
	
	
	public String RequestForQuote(){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		int ociUserId = 0;
		
		try{
		
			if(session.getAttribute("isOciUser")!=null)
				ociUserId = (Integer) session.getAttribute("isOciUser");
			
				int resultPerPage = 5;
			    int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
			    int fromRow =  (noOfPage-1)*resultPerPage + 1;
				int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
			    //rfqRefKeyList = new ArrayList<ProductsModel>();
			    //rfqRefKeyList = ProductsDAO.getRfqRefKey(userId, fromRow, toRow);  //getNprRefKey(userId,fromRow,toRow);
			    UsersModel user= UsersDAO.getUserEmail(userId);
			    String email  = CommonUtility.validateString(user.getEmailAddress());
			    String firstName= CommonUtility.validateString(user.getFirstName());
			    String lastName= CommonUtility.validateString(user.getLastName());
			    String userName = (String) session.getAttribute(Global.USERNAME_KEY);
			    String jobTitle = CommonUtility.validateString(user.getJobTitle());
			     
			    HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(userName);
				int defaultBillToId = userAddressId.get("Bill");
			     String tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
				int defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);
				HashMap<String, UsersModel> userAddress = UsersDAO.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
				
				UsersModel billAddress = userAddress.get("Bill");
				String phoneNo="";
				if(billAddress!=null){
					phoneNo=billAddress.getPhoneNo();
				}
				
				if(CommonDBQuery.getSystemParamtersList().get("GET_RFQ_ITEM_LIST")!=null && CommonDBQuery.getSystemParamtersList().get("GET_RFQ_ITEM_LIST").trim().equalsIgnoreCase("Y")){
					ArrayList<ProductsModel> rfqItemList = ProductsDAO.getRfqItemList(ociUserId, userId, session.getId());
					contentObject.put("rfqItemList", rfqItemList);
				}
			
			/*if(rfqRefKeyList!=null && rfqRefKeyList.size()>0){
				resultCount = rfqRefKeyList.get(0).getResultCount();
				double disp;
		    	double disp1;
		    	disp = resultCount;
		
		    	if (resultCount > resultPerPage) {
		
		    		paginate = (resultCount / resultPerPage);
		    		disp1 = (disp / resultPerPage);
		
		    	} else {
		    		paginate = (resultCount / resultPerPage);
		    		disp1 = (resultCount / resultPerPage);
		    	}
		
		    	if (disp1 > paginate) {
		    		paginate = paginate + 1;
		
		    	}
		    	
		    	
			}*/
			//contentObject.put("paginate", paginate);
			//contentObject.put("resultCount", resultCount);
			//contentObject.put("rfqRefKeyList", rfqRefKeyList);
			contentObject.put("billAddress", billAddress);
			contentObject.put("phoneNo", phoneNo);
			contentObject.put("email", email);
			contentObject.put("firstName", firstName);
			contentObject.put("lastName", lastName);
			contentObject.put("jobTitle", jobTitle);
			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FETCH_WAREHOUSES_IN_SHOPPING_CART")).equalsIgnoreCase("Y")) {
				List<WarehouseModel> wareHouses = UsersDAO.getWareHouses();
				
				contentObject.put("wareHouses", wareHouses);
			
			}
			contentObject.put("numberOfRecords",CommonDBQuery.getSystemParamtersList().get("NUMBER_OF_RECORDS_IN_RFQ"));
			renderContent = LayoutGenerator.templateLoader("RequestForQuotePage", contentObject , null, null, null);
			target = SUCCESS;
			
		}catch(Exception e){
			e.printStackTrace();
		}
	return target;
	}
	
	public String RequestForQuoteFileUpload(){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		int ociUserId = 0;
		
		try{
		
			if(session.getAttribute("isOciUser")!=null)
				ociUserId = (Integer) session.getAttribute("isOciUser");
				UsersModel user= UsersDAO.getUserEmail(userId);
			    String email  = CommonUtility.validateString(user.getEmailAddress());
			    String firstName= CommonUtility.validateString(user.getFirstName());
			    String lastName= CommonUtility.validateString(user.getLastName());
			    String userName = (String) session.getAttribute(Global.USERNAME_KEY);
			    String jobTitle = CommonUtility.validateString(user.getJobTitle());
			     
			    HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(userName);
				int defaultBillToId = userAddressId.get("Bill");
			     String tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
				int defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);
				HashMap<String, UsersModel> userAddress = UsersDAO.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
				
				UsersModel billAddress = userAddress.get("Bill");
				String phoneNo="";
				if(billAddress!=null){
					phoneNo=billAddress.getPhoneNo();
				}
				
				if(CommonDBQuery.getSystemParamtersList().get("GET_RFQ_ITEM_LIST")!=null && CommonDBQuery.getSystemParamtersList().get("GET_RFQ_ITEM_LIST").trim().equalsIgnoreCase("Y")){
					ArrayList<ProductsModel> rfqItemList = ProductsDAO.getRfqItemList(ociUserId, userId, session.getId());
					contentObject.put("rfqItemList", rfqItemList);
				}
			
			
			contentObject.put("billAddress", billAddress);
			contentObject.put("phoneNo", phoneNo);
			contentObject.put("email", email);
			contentObject.put("firstName", firstName);
			contentObject.put("lastName", lastName);
			contentObject.put("jobTitle", jobTitle);
			contentObject.put("numberOfRecords",CommonDBQuery.getSystemParamtersList().get("NUMBER_OF_RECORDS_IN_RFQ"));
			renderContent = LayoutGenerator.templateLoader("RequestForQuoteUploadPage", contentObject , null, null, null);
			target = SUCCESS;
			
		}catch(Exception e){
			e.printStackTrace();
		}
	return target;
	}
	
	public static String SearchProductForQuickCart(HttpSession session, String heading,String xlValue, String defaultShiptoId2, int subsetId,int generalSubset, String id, int buyingCompanyId, String entityId,int userId,String eclipseSessionId,String userName) {
		ArrayList<ProductsModel> itemLevelFilterData = null;
		String result = null;
		

		String requestType = "SEARCH";
		try{
			//if(heading != null && heading.trim().equalsIgnoreCase("Part_No")){
			if(heading != null && heading.trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.partnumber").replace(" ","_"))){
				requestType = "SEARCH-AS03";
			//}else if(heading != null && heading.trim().equalsIgnoreCase("Manufacturer_Part_No")){
			}else if(heading != null && heading.trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.mfrpartnumber").replace(" ","_"))){
				requestType = "SEARCH-AS03";
			//}else if(heading != null && heading.trim().equalsIgnoreCase("UPC")){
			}else if(heading != null && heading.trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.upc").replace(" ","_"))){
				requestType = "SEARCH-AS02";
				
			}/*else if(heading != null && heading.trim().equalsIgnoreCase("Customer_Part_No")){
				
				//TO do
			}*/
			HashMap<String, ArrayList<ProductsModel>> results1 = ProductHunterSolr.searchNavigation(xlValue, subsetId, generalSubset, 0, 12, requestType, 0, 0, 0, 1, null, "0", id, "", 12, "", buyingCompanyId, "", "", entityId, userId, "", "", "", "", "",false,true,"","","");
			//(String keyWord,int subsetId,int generalSubset,int fromRow,int toRow,String requestType,int psid,int npsid,int treeId,int levelNo,String attrFilterList,String brandId, String sessionId,String sortBy, int resultPerPage, String narrowKeyword, int buyingCompanyId,String userName,String userToken,String entityId,int userId,String homeTeritory,String type, String wareHousecode,String customerId,String customerCountry)
			if(results1!=null && results1.size()>0){
				itemLevelFilterData = new ArrayList<ProductsModel>();
	  			itemLevelFilterData = results1.get("itemList");
	  			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
	  				session.setAttribute("resultValQuickCart", Integer.toString(itemLevelFilterData.get(0).getResultCount()));
	  				Gson priceInquiry = new Gson();
	  	  			result = priceInquiry.toJson(itemLevelFilterData);
	  	  			System.out.println(result);
	  			}
	  		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	//----------------------------- New
	public String performShareCart(){
		try{
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String msg=request.getParameter("message");
			String sharedUserIds[] = request.getParameterValues("sharedUserId");
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String Result = ProductsDAO.performShareCart(sharedUserIds, userId, savedGroupId, savedGroupName, msg);
			renderContent = Result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String myApp() {
		//topStaticMenu = new ArrayList<MenuAndBannersModal>();
		try{
			topStaticMenu = MenuAndBannersDAO.getTopStaticMenu();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("topStaticMenu", topStaticMenu);
			renderContent = LayoutGenerator.templateLoader("MenuForApp",contentObject, null, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;

	}
	
	/* Market Basket*/
	public String marketBasketLoader() {
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			notValidFile = (String) session.getAttribute("InvalidPartList");
			String entityId = (String) session.getAttribute("entityId");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			String customerId = (String) session.getAttribute("customerId");
			String customerCountry = (String) session.getAttribute("customerCountry");
			System.out.println("at itemDetailFilter wareHouseCode---------------"+wareHousecode+" : "+customerId+" : "+customerCountry);
			int userId = CommonUtility.validateNumber(sessionUserId);
			int xlsfile_column_size = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("XLSFILE_COLUMN_SIZE"));
			int uploadItemCnt = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("PARTLIST_LOADER_ITEM_SIZE"));
			int lastRowNum = (Integer)session.getAttribute("lastRowNum");
			boolean lessThanMinOrderQty = false;
			resultMsg = "";
			eclipseErrorMassage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNMESSAGE");
			target="MarketBasketResult";
	
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			int resultPerPage = CommonUtility.validateNumber(resultPage);
			String requestType = "SEARCH";
	
			if(resultPerPage==0)
			{
				resultPage = "10";
				resultPerPage = 10;
			}
			int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
			int fromRow =  (noOfPage-1)*resultPerPage + 1;
			int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
	
			HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
	
			if(notValidFile != null){
				String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
				int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
				if(notValidFile.trim().equalsIgnoreCase("NO")){
	
					LinkedHashMap<String, ArrayList<MarketBasketData>> searchListMap = (LinkedHashMap<String, ArrayList<MarketBasketData>>) session.getAttribute("searchListMap");
					//LinkedHashMap<String, ArrayList<MarketBasketData>> searchListMapTemp = (LinkedHashMap<String, ArrayList<MarketBasketData>>) session.getAttribute("searchListMap");
	
					if(type.equalsIgnoreCase("combineduplicate")){
						for (int row = 1; row <= lastRowNum; row++) {
							ArrayList<MarketBasketData> rowMBDList = searchListMap.get(CommonUtility.validateString(""+row));
							if(rowMBDList != null && rowMBDList.size() > 0){
								ArrayList<MarketBasketData> rowMBDListTemp = new ArrayList<MarketBasketData>();
								for (MarketBasketData MBDTemp : rowMBDList) {
									if(!MBDTemp.isDuplicate()){
										rowMBDListTemp.add(MBDTemp);
									}else{
										String[] duplicateOfRowNumArray = MBDTemp.getDuplicateItemRows().split(",");
										for (int i = 0; i < duplicateOfRowNumArray.length; i++) {
											int duplicateItemIndex = CommonUtility.validateNumber(MBDTemp.getIndex());
											int quantity = searchListMap.get(duplicateOfRowNumArray[i]).get(duplicateItemIndex).getUserQuantity();
											int previousQuantity = MBDTemp.getUserQuantity();
											MBDTemp.setUserQuantity(quantity + previousQuantity);
											searchListMap.get(duplicateOfRowNumArray[i]).set(duplicateItemIndex, MBDTemp);
										}
									}
								}
	
								searchListMap.put(CommonUtility.validateString(""+row), rowMBDListTemp);
							}
						}
					}
	
					if(searchListMap != null && searchListMap.size()> 0){
						if(searchListMap.size() <= uploadItemCnt){
							String countStr = (String)session.getAttribute("colCnt");
	
							if(countStr == null){
								readQuoteListCount = 0;
							}else{
								readQuoteListCount = CommonUtility.validateNumber(countStr);
							}
	
							int colCntInXls = readQuoteListCount;
							if(colCntInXls != xlsfile_column_size){
								session.removeAttribute("InvalidPartList");
								session.removeAttribute("colCnt");
								setShoppingListLoaderMessage("Uploaded file format is not in desired format, Please download a sample file from the above Download Sample File link and try again.");
	
							}else{
								System.out.println("ColumnCnt : "+colCntInXls);
	
								shoppingListLoaderCount = new LinkedHashMap<String, Integer>();
								shoppingListLoaderDetails = new ArrayList<MarketBasketData>();
	
								for (int j = 0; j <= lastRowNum; j++) {
									boolean partNumberFound = false;
									ArrayList<MarketBasketData> MBDList = searchListMap.get(CommonUtility.validateString(""+j));
	
									if(MBDList!=null && MBDList.size() > 0){
										for (int k = 0; k < MBDList.size(); k++) {
	
											MarketBasketData MBD = MBDList.get(k);						
											MarketBasketData MBData = new MarketBasketData();
											boolean keywordLessThanMinQty = false;
											if(MBD.getSearchKeyWord()!=null){
	
												homeTeritory = (String) session.getAttribute("shipBranchId");
												fromRow =  (noOfPage-1)*resultPerPage;
	
												// Check for Exact CPN 
												/* ArrayList<ProductsModel> CPNSearchResult =	ProductHunterSolr.customerPartNumberSearch(MBD.getSearchKeyWord(), 0, 100, subsetId, buyingCompanyId);
	
												  		  if(CPNSearchResult.size() == 1){
												  			  // add to Cart and Remove it from List
	
												  			  ProductsModel item = CPNSearchResult.get(0);
	
												  			  int qty = MBD.getUserQuantity();
	
												  			  int minOrderQty = item.getMinOrderQty();
												  			  int orderInterval = item.getOrderInterval();
	
	
												  			  if(qty < minOrderQty ){
												  				  qty = minOrderQty;
	
												  				cpnAddMinQtyNotMatchList.add(MBD.getSearchKeyWord());
	
												  			  } 
	
												  			  if(qty > minOrderQty){
												  				   int diff = qty - minOrderQty;
	
												  				   if(diff % orderInterval != 0){
												  					   qty = minOrderQty;
												  					 cpnAddMinQtyNotMatchList.add(MBD.getSearchKeyWord());
												  				   }
	
												  			  }
	
												  		 int 	count = ProductsDAO.insertItemToCart(userId, item.getItemId(), qty, sessionId,null);
	
	
												  		cpnAddList.add(MBD.getSearchKeyWord());
	
	
												  			  continue;
	
												  		  }*/
												/*if(MBD.getSearchType()!=null && MBD.getSearchType().trim().equalsIgnoreCase("UPC")){
												  			requestType = "SEARCH-AS10";
												  		}else if(MBD.getSearchType()!=null && MBD.getSearchType().trim().equalsIgnoreCase("Manufacturer_Part_Number")){
												  			requestType = "SEARCH-AS07";
												  		}else if(MBD.getSearchType()!=null && MBD.getSearchType().trim().equalsIgnoreCase("Customer_Part_Number")){
											  				requestType = "SEARCH-AS01";
											  			}else{
												  			requestType = "SEARCH-AS08";
												  		}*/
												if(MBD.getSearchType()!=null && MBD.getSearchType().trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.upc").replace(" ","_"))){
													requestType = "SEARCH-AS10";
												}else if(MBD.getSearchType()!=null && MBD.getSearchType().trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.mfrpartnumber").replace(" ","_"))){
													requestType = "SEARCH-AS07";
												}else if(MBD.getSearchType()!=null && MBD.getSearchType().trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.customerpartnumber").replace(" ","_"))){
													requestType = "SEARCH-AS01";
												}else if(MBD.getSearchType()!=null &&MBD.getSearchType().equalsIgnoreCase("Key_Word")){
													requestType = "SEARCH";
												}else{
													requestType = "SEARCH-AS08";
												}
												
												searchResultList = ProductHunterSolr.searchNavigation(MBD.getSearchKeyWord().trim(), subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"",wareHousecode,customerId,customerCountry,false,true,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHouseItems);
												ArrayList<Integer> itemList = new ArrayList<Integer>();
												LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = new LinkedHashMap<Integer, LinkedHashMap<String,Object>>();
												int count = 0;
												if(searchResultList!=null && searchResultList.size()>0){
													ArrayList<ProductsModel> itemSugData = searchResultList.get("itemSuggest");
													if(itemSugData!=null && itemSugData.size()>0)
													{
														itemSuggest = itemSugData.get(0).getSuggestedValue();
													}
													itemLevelFilterData = new ArrayList<ProductsModel>();
													itemLevelFilterData = searchResultList.get("itemList");
													if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
														partNumberFound = true;
														count = itemLevelFilterData.get(0).getResultCount();
														
														for(ProductsModel item : itemLevelFilterData) {
															int itemId = item.getItemId();
															itemList.add(itemId);
														}
														if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
															if(itemList!=null && itemList.size()>0){
																customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "),"itemid");
																if(customFieldVal!=null && !customFieldVal.isEmpty()){
																	
																	for(ProductsModel item : itemLevelFilterData) {
																		int itemId = item.getItemId();
																		item.setCustomFieldVal(customFieldVal.get(itemId)); 
																	}
																}
															}
														}
													}
													
													
												}
												
												if(partNumberFound || k==MBDList.size()-1){
													shoppingListLoaderCount.put(MBD.getSearchKeyWord(), count);
													MBData.setItemList(itemLevelFilterData);
													MBData.setUserQuantity(MBD.getUserQuantity());
													MBData.setSearchCount(count);
													MBData.setSearchKeyWord(MBD.getSearchKeyWord());
													MBData.setLineItemComment(MBD.getLineItemComment());
													
													if(itemLevelFilterData != null){
														for(int i = 0;i<itemLevelFilterData.size();i++){
															if(itemLevelFilterData.get(i).getMinOrderQty() > MBD.getUserQuantity()){
																lessThanMinOrderQty = true;
																keywordLessThanMinQty = true;
															}
														}
														if(keywordLessThanMinQty){
															if(resultMsg.equalsIgnoreCase("") && !resultMsg.contains(MBD.getSearchKeyWord()))
																resultMsg = "Quantity of Items for keywords " + MBD.getSearchKeyWord();
															else
																resultMsg = resultMsg + "," + MBD.getSearchKeyWord();
															MBData.setInvalidQuantity(true);
														}
													}
													shoppingListLoaderDetails.add(MBData);
												}
											}
											if(partNumberFound){
												break;
											}
										}
									}
								}
	
								session.setAttribute("shoppingListLoader", shoppingListLoaderDetails);
								System.out.println("Final Result : "+shoppingListLoaderDetails.size());
	
							}
						}else{
							session.removeAttribute("InvalidPartList");
							session.removeAttribute("colCnt");
							setShoppingListLoaderMessage("Uploaded list contains more than "+uploadItemCnt+" items, maximum upload item count is "+uploadItemCnt+" or less than "+uploadItemCnt+" items.");
						}
					}else{
						session.removeAttribute("InvalidPartList");
						session.removeAttribute("colCnt");
						setShoppingListLoaderMessage("Upload list is empty or there is problem in the file uploaded.");
					}
				}else{
					setShoppingListLoaderMessage("Upload Process Failed, please check the file you uploaded.");
				}
			}
	
			if(lessThanMinOrderQty){
				resultMsg = resultMsg + " has less than minimum order quantity!! <br /> <b>Yes:</b> To continue with minimum order quantity <br /><b> No:</b> To remove keyword having quantity less than minimum order quantity";
				session.removeAttribute("MarketBasketErrorMsg");
				target = "MinOrderQtyConfirmation";
				//			resultMsg = type;
			}else{
				resultMsg = "getFileUploadData";
				target = "MinOrderQtyConfirmation";
				session.removeAttribute("MarketBasketErrorMsg");
				type = "getData";
			}
	
			session.setAttribute("cpnExactResultList", cpnAddList);
			session.setAttribute("cpnExactResultMinQtyNotMatchList", cpnAddMinQtyNotMatchList);
			Gson g=new Gson();
			System.out.println(g.toJson(shoppingListLoaderDetails));
	
			renderContent = resultMsg;
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String marketBasketGetLoader(){
		try{
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			if(userId>1){
				String orderId = request.getParameter("orderId");
				ArrayList<Integer> skippedRows = new ArrayList<Integer>();
				skippedRows = (ArrayList<Integer>) session.getAttribute("skippedRows");
	
				cpnAddList = (ArrayList<String>) session.getAttribute("cpnExactResultList");
				session.removeAttribute("cpnExactResultList");
	
				cpnAddMinQtyNotMatchList = (ArrayList<String>) session.getAttribute("cpnExactResultMinQtyNotMatchList");
				session.removeAttribute("cpnExactResultMinQtyNotMatchList");
	
				shoppingListLoaderDetails = new ArrayList<MarketBasketData>();
				shoppingListLoaderDetails = (ArrayList<MarketBasketData>) session.getAttribute("shoppingListLoader");
				if (type.equalsIgnoreCase("removeRepeatedKeyword")) {
					int i = -1;
					for (i = 0; i < shoppingListLoaderDetails.size(); i++) {
						if (shoppingListLoaderDetails.get(i).isInvalidQuantity()) {
							break;
						}
					}
					if (i != -1){
						shoppingListLoaderDetails.remove(i);
					}
				}
				session.removeAttribute("MarketBasketErrorMsg");
				Gson g= new Gson();
				System.out.println(g.toJson(shoppingListLoaderDetails));
	
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("cpnAddList", cpnAddList);
				contentObject.put("shoppingListLoaderDetails", shoppingListLoaderDetails);
				contentObject.put("cpnAddMinQtyNotMatchList", cpnAddMinQtyNotMatchList);
				contentObject.put("shoppingListLoaderMessage", shoppingListLoaderMessage);
				if(CommonUtility.validateString(shoppingListLoaderMessage).length() < 1){
					contentObject.put("shoppingListLoaderMessage", "Error while processing your request...!");
				}
				contentObject.put("skippedRows", skippedRows);
	
				target = SUCCESS;
				renderContent = LayoutGenerator.templateLoader("ShoppingListLoaderResultPage", contentObject , null, null, null);
			}else{
				target = "SESSIONEXPIRED";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return target;
	}
	
	
	public String addMarketBasketItemsToCart()
	{

		target = "NavigateToProductGroup";
		String itemsList[] = cartItemList.split(",");
		String lineItemCommentList[]=lineItemList.split(",");
		String itemsID[] = new String[itemsList.length];
		String itemsPriceID[] = new String[itemsList.length];
		String partNumber[] = new String[itemsList.length];
		//String sFPartNumber[] = new String[itemsList.length];
		String itemQty[] = new String[itemsList.length];
		String lineItem[] = new String[lineItemCommentList.length];
		String price[] = new String[itemsList.length];
		for(int i=0;i<itemsList.length;i++){
			itemsID[i] = itemsList[i].split("/")[0].split("-")[0].trim();
			itemsPriceID[i] = itemsList[i].split("/")[0].split("-")[1].trim();
			//sFPartNumber[i] = itemsList[i].split("/")[0].split("-")[2].trim();
			itemQty[i] = itemsList[i].split("/")[1].trim();
			partNumber[i] = itemsList[i].split("/")[0].split("-")[2].trim();
		}
		for(int i=0;i<lineItemCommentList.length;i++){
			itemsID[i] = itemsList[i].split("/")[0].split("-")[0].trim();
			itemsPriceID[i] = itemsList[i].split("/")[0].split("-")[1].trim();
			//sFPartNumber[i] = itemsList[i].split("/")[0].split("-")[2].trim();
			lineItem[i]=lineItemCommentList[i].split("/")[1].trim();
			partNumber[i] = itemsList[i].split("/")[0].split("-")[2].trim();
		}
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String sessionId = session.getId();
		int userId = CommonUtility.validateNumber(sessionUserId);
		Connection  conn = null;
		int count=0;
		try {
			conn = ConnectionManager.getDBConnection();
		} catch (SQLException e) { 
			e.printStackTrace();
		}	
		try {
			
			LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
			utilityMap.put("considerLineItemComment", true);
			
			if (itemsID != null && itemsID.length > 0 && partNumber != null ) {
				for (int i = 0; i < itemsID.length; i++) {
					int qty = 1;
					ProductsModel minOrderQty = new ProductsModel();
					
					//Latest
		        	String shipViaCode = "";
		        	if(session!=null && session.getAttribute("selectedShipVia")!=null){
		        		shipViaCode = (String) session.getAttribute("selectedShipVia");
		        	}
		        	ProductsModel productInput = new ProductsModel();
		        	productInput.setItemPriceId(CommonUtility.validateNumber(itemsPriceID[i]));
		        	productInput.setShipViaCode(shipViaCode);
		        	minOrderQty = ProductsDAO.getMinOrderQty(productInput);
		        	//Latest
					//minOrderQty = ProductsDAO.getMinOrderQty(itemsPriceID[i]);
		        	
					if(minOrderQty!=null && minOrderQty.getMinOrderQty()>0)
					{
						if(CommonUtility.validateNumber(itemQty[i]) == minOrderQty.getMinOrderQty())
						{
							qty  = CommonUtility.validateNumber(itemQty[i]);
						}
						else if(CommonUtility.validateNumber(itemQty[i]) > minOrderQty.getMinOrderQty())
						{
							int qtyDiff = CommonUtility.validateNumber(itemQty[i]) - minOrderQty.getMinOrderQty();
							if(qtyDiff%minOrderQty.getOrderInterval()==0)
							{
								qty  = CommonUtility.validateNumber(itemQty[i]);
							}else {
								qty = minOrderQty.getMinOrderQty();
							}

						}
					}
					else
					{
						qty  = CommonUtility.validateNumber(itemQty[i]);
					}
					ArrayList<Integer> cartId = new ArrayList<Integer>();
					cartId = ProductsDAO.selectFromCart(userId, CommonUtility.validateNumber(itemsID[i]),"");
					if (cartId != null && cartId.size() > 0) {
						count = ProductsDAO.updateCart(userId, cartId.get(0), qty, cartId.get(1),lineItem[i],"",utilityMap);
					} else {
						count = ProductsDAO.insertItemToCart(userId, CommonUtility.validateNumber(itemsID[i]), qty,sessionId,lineItem[i],"","",price[i],0, 0.0, 0.0,utilityMap);
					}
				}

			}
		} catch (Exception e) {
			errorMsg = "Some Error Occurred. Please try again.";
			e.printStackTrace();
		} finally {
			session.removeAttribute("cartCountSession");
			ConnectionManager.closeDBConnection(conn);
		}

		return "ShoppingCart";

	}
	
	public String quickCartFileUpload() {
		try{
			request =ServletActionContext.getRequest();
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
	        LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();// File Upload Max Items 
			contentObject.put("marketBasketErrorMsg", session.getAttribute("MarketBasketErrorMsg"));
			
	        renderContent = LayoutGenerator.templateLoader("ShoppingListLoader", contentObject , null, null, null);
		}catch (Exception e) {
			errorMsg = "Some Error Occurred. Please try again.";
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	//---------Market Basket End----------------------

	
	
	public String zeroInFilterSearch()
	{
		
		target = NONE;
		try {
			request =ServletActionContext.getRequest();
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String entityId = (String) session.getAttribute("entityId");
			String isAttribute = request.getParameter("isAttribute");
			String attrName = request.getParameter("attrName");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		    int subsetId = CommonUtility.validateNumber(tempSubset);
		    int resultPerPage = CommonUtility.validateNumber(resultPage);
		    String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
		    String requestType = "SEARCH";
		    navigationType = "SEARCH";
		    filterList = attrFilterList; 
		    if(filterList!=null && !filterList.trim().equalsIgnoreCase(""))
		    {
		    	try {
					filterList = URLEncoder.encode(filterList,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				}
		    }
		    if(srchTyp == null)
		    	srchTyp = "0";
		    if(srchTyp.trim().equalsIgnoreCase("1"))
		    	requestType = "SEARCH-AS01";
		    else  if(srchTyp.trim().equalsIgnoreCase("2"))
		    	requestType = "SEARCH-AS02";
		    else  if(srchTyp.trim().equalsIgnoreCase("3"))
		    	requestType = "SEARCH-AS03";
		    else  if(srchTyp.trim().equalsIgnoreCase("4"))
		    	requestType = "SEARCH-AS04";
		    else  if(srchTyp.trim().equalsIgnoreCase("5"))
		    	requestType = "SEARCH-AS05";
		    else  if(srchTyp.trim().equalsIgnoreCase("6"))
		    	requestType = "SEARCH-AS06";
		    else{
		    	srchTyp = "0";
		    	requestType = "SEARCH";
		    }
		  
		    if(resultPerPage==0)
		    {
		    	if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
		    		resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
			    	resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
		    	}else{
		    		resultPage = "12";
			    	resultPerPage = 12;
		    	}
		    }
		    
			   	int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
		 	    int fromRow =  (noOfPage-1)*resultPerPage + 1;
		 		int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
		 		String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		 		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
		 		String jsonResponse = "";
		 		if(CommonUtility.validateString(keyWord).trim().equalsIgnoreCase(""))
		 			keyWord = "*:*";
	
		 			  if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y"))
		 		  	   {
		 		  		 homeTeritory = (String) session.getAttribute("shipBranchId");
		 		  		  fromRow =  (noOfPage-1)*resultPerPage;
		 		  		jsonResponse = ZeroInSearch.getFiltersAttribute(keyWord, subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,0,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","", "","", brand, manf, category,isAttribute,attrName);//wareHousecode, customerId,customerCountry
		 		  	
		 		  	   }
		 			 renderContent = jsonResponse;		
	 
					response.getWriter().write(renderContent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		return null;
	}
	

	public String zeroInSearch()
	{

		
		target = NONE;
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			//renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
			
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String defaultShiptoId = (String) session.getAttribute("defaultShipToId");
			String entityId = (String) session.getAttribute("entityId");
			
				
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		    int subsetId = CommonUtility.validateNumber(tempSubset);
		    int resultPerPage = CommonUtility.validateNumber(resultPage);
		    String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
		    //eclipseErrorMassage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNMESSAGE");
		    String requestType = "SEARCH";
		    navigationType = "ZEROINSEARCH";
		   
		    filterList = attrFilterList; 
		    if(filterList!=null && !filterList.trim().equalsIgnoreCase(""))
		    {
		    	try {
					filterList = URLEncoder.encode(filterList,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				}
		    }
		    if(srchTyp == null)
		    	srchTyp = "0";
		    if(srchTyp.trim().equalsIgnoreCase("1"))
		    	requestType = "SEARCH-AS01";
		    else  if(srchTyp.trim().equalsIgnoreCase("2"))
		    	requestType = "SEARCH-AS02";
		    else  if(srchTyp.trim().equalsIgnoreCase("3"))
		    	requestType = "SEARCH-AS03";
		    else  if(srchTyp.trim().equalsIgnoreCase("4"))
		    	requestType = "SEARCH-AS04";
		    else  if(srchTyp.trim().equalsIgnoreCase("5"))
		    	requestType = "SEARCH-AS05";
		    else  if(srchTyp.trim().equalsIgnoreCase("6"))
		    	requestType = "SEARCH-AS06";
		    else{
		    	srchTyp = "0";
		    	requestType = "SEARCH";
		    }
		  
		    if(resultPerPage==0)
		    {
		    	if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
		    		resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
			    	resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
		    	}else{
		    		resultPage = "12";
			    	resultPerPage = 12;
		    	}
		    }
		 
	
		    	 int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
		 	    int fromRow =  (noOfPage-1)*resultPerPage + 1;
		 		int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
		 		String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		 		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
		 		HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
		 		if(!CommonUtility.validateString(keyWord).trim().equalsIgnoreCase("") || !CommonUtility.validateString(attrFilterList).trim().equalsIgnoreCase(""))
		 		{
		 			
		 			  if(CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLESOLRSEARCHENGINE").trim().equalsIgnoreCase("Y"))
		 		  	   {
		 		  		 homeTeritory = (String) session.getAttribute("shipBranchId");
		 		  		  fromRow =  (noOfPage-1)*resultPerPage;
		 		  		  searchResultList = ZeroInSearch.searchNavigation(keyWord, subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","", "","", brand, manf, category);//wareHousecode, customerId,customerCountry
		 		  		 ArrayList<ProductsModel> itemSugData = searchResultList.get("itemSuggest");
		 		  		if(itemSugData!=null && itemSugData.size()>0)
		 		  		{
		 		  			itemSuggest = itemSugData.get(0).getSuggestedValue();
		 		  		}
		 		  	   }
		 		  	   else
		 		  	   {
		 		  		  if(vpsid!=null && !vpsid.trim().equalsIgnoreCase("") && !vpsid.trim().equalsIgnoreCase("0"))
		 		  				varPsid = CommonUtility.validateNumber(vpsid);
		 		  			
		 		  			
		 		  			
		 		  			if(varPsid==0)
		 		  				searchResultList = ProductsDAO.searchNavigation(keyWord, subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,true,userId,"",viewFrequentlyPurcahsedOnly);
		 		  			else
		 		  				searchResultList = ProductsDAO.searchNavigation(narrowKeyword, subsetId, generalSubset, fromRow, toRow, requestType, varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,true,userId,"",viewFrequentlyPurcahsedOnly);
		 		  	   }
		 		 		
		 		 		
		 		 		itemLevelFilterData = searchResultList.get("itemList");
		 		 		attrFilterData = searchResultList.get("attrList");
		 		 		
		 		 		
		 		 		
		 		 		if(itemLevelFilterData!=null && itemLevelFilterData.size()>0)
		 		 		{
		 		 			if(varPsid==0)
		 		 			varPsid = itemLevelFilterData.get(0).getProductSeardId();
		 		 		}
		 		 		
		 		 		itemLevelFilterData = searchResultList.get("itemList");
		 		 		categoryFilterData = searchResultList.get("categoryList");
		 		 		
		 		 		if(categoryFilterData!=null && categoryFilterData.size()>0)
		 		 		{
		 		 			ArrayList<ProductsModel> tempListGen = new ArrayList<ProductsModel>();
		 		 			
		 		 			compareList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
		 		 			for(ProductsModel categList:categoryFilterData)
		 		 			{
		 		 				tempListGen = compareList.get(categList.getParentCategory());
		 		 				ProductsModel temp = new ProductsModel();
		 		 				
		 		 				temp.setCategoryCount(categList.getCategoryCount());
		 		 				temp.setCategoryCode(categList.getCategoryCode());
		 		 				temp.setCategoryName(categList.getCategoryName());
		 		 				if(tempListGen==null)
		 		 				{
		 		 					tempListGen = new ArrayList<ProductsModel>();
		 		 					tempListGen.add(temp);
		 		 				}
		 		 				else
		 		 				{
		 		 					tempListGen.add(temp);
		 		 				}
		 		 				compareList.put(categList.getParentCategory(), tempListGen);
		 		 			}
		 		 			
		 		 		}
		 		 		
		 		 		 attrFilteredList = searchResultList.get("filteredList");
		 		}
		 		
		 		
		 		
		  	 
		 		if(itemLevelFilterData!=null && itemLevelFilterData.size()>0)
		 		{
		 			
		 			metaTagString="";
		 			String s = ",";
		 			for(ProductsModel metaTagKeyword : itemLevelFilterData)
		 			{
		 				metaTagString = metaTagString +s+metaTagKeyword.getPartNumber() +" "+ metaTagKeyword.getManufacturerPartNumber();
		 				s = ",";
		 			}
		 			if(metaTagString!=null&&!metaTagString.trim().equals(""))
		 			{
		 			metaTagString = metaTagString.replaceAll("\"", "");
		 			metaTagString = metaTagString.replaceAll("'", "");
		 			}
		 			resultCount = itemLevelFilterData.get(0).getResultCount();
		 			ProductsDAO.writePopularSearchWord(keyWord, userId, subsetId, resultCount, srchTyp);
		 			double disp;
		 	    	double disp1;
		 	    	disp = resultCount;
	
		 	    	if (resultCount > resultPerPage) {
	
		 	    		paginate = (resultCount / resultPerPage);
		 	    		disp1 = (disp / resultPerPage);
	
		 	    	} else {
		 	    		paginate = (resultCount / resultPerPage);
		 	    		disp1 = (resultCount / resultPerPage);
		 	    	}
	
		 	    	if (disp1 > paginate) {
		 	    		paginate = paginate + 1;
	
		 	    	}
		 	    	if(resultCount>1){
		 	    		contentObject.put("paginate", paginate);
		 	    		contentObject.put("keyWord", keyWord);
		 	    		contentObject.put("narrowKeyword", narrowKeyword);
		 	    		contentObject.put("srchTyp", srchTyp);
		 	    		contentObject.put("pageNo", pageNo);
		 	    		contentObject.put("categoryId", codeId);
		 		 		contentObject.put("gallery", gallery);
		 		 		contentObject.put("levelNo", levelNoSkip);
		 		 		contentObject.put("navigationType", navigationType);
		 		 		contentObject.put("attrFilterList",attrFilterList);
		 		 		contentObject.put("filterList", filterList);
		 		 		contentObject.put("resultPage", resultPage);
		 		 		contentObject.put("keyWordTxt", keyWordTxt);
		 		 		contentObject.put("resultCount", resultCount);
		 		 		contentObject.put("attrFilterData", attrFilterData);
		 		 		contentObject.put("attrFilteredList", attrFilteredList);
		 	    		contentObject.put("breadCrumbList", breadCrumbList);
		 	    		contentObject.put("topBanners", topBanners); 
		 	    		contentObject.put("itemListData", itemLevelFilterData);
		 	    		contentObject.put("sortBy", sortBy);
		 	    		
		 	    		renderContent = LayoutGenerator.templateLoader("ZeroInPage", contentObject , null, null, null);
		 	    		target="success";
		 	          	//target="searchResult";
		 	        }else{
		 	   	    	itemPriceId = itemLevelFilterData.get(0).getItemPriceId();
			    		itemUrlSingle = itemLevelFilterData.get(0).getItemUrl();
			    		itemUrlSingle = CommonUtility.getUrlString(itemUrlSingle);
		 	   	    	target="singleItem";
		 	   	    }
		 		}
		 		else
		 		{
		 			contentObject.put("navigationType", navigationType);
		 			contentObject.put("resultCount", resultCount);
		 			contentObject.put("keyWord", keyWord);
		 			
		 			LinkedHashMap<String, Object> contextLeftContent = new LinkedHashMap<String, Object>();
		 			TemplateModel template = null;
		 			String siteName = CommonDBQuery.getSystemParamtersList().get("SITE_NAME");
		 			template =  new TemplateModel();
		 			LinkedHashMap<String, TemplateModel> layoutList = LayoutLoader.getLayoutModel().getLayoutBySite().get(siteName.trim().toUpperCase());
		 			template.setLeftContent(layoutList.get("HomePage").getLeftContent());
		 			contextLeftContent.put("leftContent", template.getLeftContent());
			    	
		 			renderContent = LayoutGenerator.templateLoader("ZeroInPage", contentObject, contextLeftContent, null, null);
		 			target="success";
		 		}
		}catch (Exception e) {
			e.printStackTrace();
		}

		return target;
	
	}
	
	public static ArrayList<ProductsModel> SearchProductForQuickCartAdvanced(HttpSession session, String heading,String xlValue, String defaultShiptoId2, int subsetId,int generalSubset, String id, int buyingCompanyId, String entityId,int userId,String eclipseSessionId,String userName) {
		ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
		String result = null;
		

		String requestType = "SEARCH";
		
		//if(heading != null && heading.trim().equalsIgnoreCase("Part_No")){
		if(heading != null && heading.trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.partnumber").replace(" ","_"))){
			requestType = "SEARCH-AS03";
		//}else if(heading != null && heading.trim().equalsIgnoreCase("Manufacturer_Part_No")){
		}else if(heading != null && heading.trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.mfrpartnumber").replace(" ","_"))){
			requestType = "SEARCH-AS03";
		//}else if(heading != null && heading.trim().equalsIgnoreCase("UPC")){
		}else if(heading != null && heading.trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.upc").replace(" ","_"))){
			requestType = "SEARCH-AS02";
			
		}/*else if(heading != null && heading.trim().equalsIgnoreCase("Customer_Part_No")){
			
			
		}*/
		HashMap<String, ArrayList<ProductsModel>> results1 = ProductHunterSolr.searchNavigation(xlValue, subsetId, generalSubset, 0, 12, requestType, 0, 0, 0, 1, null, "0", id, "", 12, "", buyingCompanyId, "", "", entityId, userId, "", "", "", "", "",false,true,"","","");
		//(String keyWord,int subsetId,int generalSubset,int fromRow,int toRow,String requestType,int psid,int npsid,int treeId,int levelNo,String attrFilterList,String brandId, String sessionId,String sortBy, int resultPerPage, String narrowKeyword, int buyingCompanyId,String userName,String userToken,String entityId,int userId,String homeTeritory,String type, String wareHousecode,String customerId,String customerCountry)
		if(results1!=null && results1.size()>0){
			itemLevelFilterData = new ArrayList<ProductsModel>();
			if(results1.get("itemList")!=null){
				itemLevelFilterData = results1.get("itemList");
			}
  			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
  				session.setAttribute("resultValQuickCart", Integer.toString(itemLevelFilterData.get(0).getResultCount()));
  				Gson priceInquiry = new Gson();
  	  			result = priceInquiry.toJson(itemLevelFilterData);
  	  			System.out.println(result);
  			}
  		}
		return itemLevelFilterData;
	} 
	public String advancedPG(){
		try
		{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			int productGroupId = CommonUtility.validateNumber(codeId);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			HashMap<String, ArrayList<ProductsModel>> productGroup = ProductHunterSolr.getProductGroupAndItems(productGroupId, userId,0,1,"");
			contentObject.put("groupListData", productGroup.get("groupListData"));
			contentObject.put("parentId", savedGroupId);
			
			renderContent = LayoutGenerator.templateLoader("AdvancedProductGroup", contentObject , null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String addNewAdvancedPG(){
		Connection conn=null;
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try {
			conn = ConnectionManager.getDBConnection();
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			
			int savedListId = CommonDBQuery.getSequenceId("PRODUCT_GROUPS_V2_SEQ");
			int userId = CommonUtility.validateNumber(sessionUserId);
			int count = 0;
			count = ProductsDAO.validateAdvancedProductGroupName(conn, listName, userId, "P", CommonUtility.validateNumber(levelNo));
			if(count>0)
			{
				renderContent = "1|"+2+"|";
			}
			else
			{
				count = ProductsDAO.createAdvancedProductGroupName(conn, savedListId, userId, levelNo, listName,parentId);
				renderContent = "1|"+count+"|";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
        	ConnectionManager.closeDBConnection(conn);
        }
		return SUCCESS;
	}
	public String galleryDefault(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String viewType = request.getParameter("viewType");
			session.setAttribute("viewType",viewType);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
		
	}
	public String productGroupList()
	{
		try
		{
			request =ServletActionContext.getRequest();
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			HttpSession session = request.getSession();
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			renderContent = ProductHunterSolr.productGroupAutoComplete(userId);
			//renderContent = jsonResponse;		
			response.getWriter().write(renderContent);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String getSavedListNames(){
		try{
			request =ServletActionContext.getRequest();
			response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			HttpSession session = request.getSession();
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String buyingCompanyId = (String)session.getAttribute("buyingCompanyId");
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		    if(CommonDBQuery.getSystemParamtersList().get("getGroupNameEverywhere")!=null && CommonDBQuery.getSystemParamtersList().get("getGroupNameEverywhere").trim().equalsIgnoreCase("Y")&& userId>1){
		    	LinkedHashMap<String, Object> contentObjectTemp = new LinkedHashMap<String, Object>();
		    	contentObject.put("session", session);
		    	contentObjectTemp = ProductsDAO.getSavedGroupNameByUserIdDao(userId, contentObjectTemp, CommonUtility.validateNumber(buyingCompanyId));
		    	String advancedProductGroup = CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP");
		    	if(advancedProductGroup!=null && !advancedProductGroup.trim().equalsIgnoreCase("Y")){
		    		contentObject.put("groupListData", contentObjectTemp.get("groupListData"));
		    	}
		    	contentObject.put("savedCartData", contentObjectTemp.get("savedCartData"));
		    	contentObject.put("approveCartData", contentObjectTemp.get("approveCartData"));
		    }
		    renderContent = LayoutGenerator.templateLoader("MyGroupsListPage", contentObject , null, null, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String insertMultipleItemsToProductGroup() throws IOException
	{
		target = "NavigateToProductGroup";
		try{
			System.out.println(listId);
			System.out.println(listName);
			System.out.println(productIdList);
	
			String itemsList[] = productIdList.split(",");
			String itemsID[] = new String[itemsList.length];
			String itemQty[] = new String[itemsList.length];
			for(int i=0;i<itemsList.length;i++){
				itemsID[i] = itemsList[i].split("/")[0].trim();
				itemQty[i] = itemsList[i].split("/")[1].trim();
			}
	
	
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			for(int i=0;i<itemsID.length;i++){
				savedGroupId = ProductsDAO.insertProductListItem(listId,listName, userId, itemsID[i], CommonUtility.validateNumber(itemQty[i]),"");
			}
		//savedGroupId = ProductorItemUtilityDao.insertProductListItem(listId, listName, userId, productIdList, itemQty);
		//response.sendRedirect(response.encodeRedirectURL("myProductGroupPage.action?savedGroupId="+savedGroupId));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "MyProductsPage";

	}
	public String willCall(){
		try{
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String zipCode =  request.getParameter("ZipCode");
			String pns = request.getParameter("partNumber");
			String itemDetail = request.getParameter("itemDetail");
			String trigerPoint = request.getParameter("trigerPoint");
			type = request.getParameter("type");
			
			UsersModel userDetail = new UsersModel();
			ProductsModel test1 = new ProductsModel();
			WillCall test = new WillCall();
			renderContent = test.getWillCallHTML(test1,userDetail);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String advancedSearch(){
		try
		{
			//request =ServletActionContext.getRequest();
			//HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			renderContent = LayoutGenerator.templateLoader("AdvancedSearch", contentObject , null, null, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String recentlyViewed(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			int generalSubsetId = CommonUtility.validateNumber(tempGeneralSubset);
			
			String JsonData=request.getParameter("jsonObj");
			ArrayList<ProductsModel> itemDataList = new ArrayList<ProductsModel>();
			
			ArrayList<ProductsModel> resultWithoutDuplicate = new ArrayList<ProductsModel>();
			Set<Integer> itemSet = new HashSet<Integer>();
			ProductsModel recent=new ProductsModel();
			ProductManagementModel productManagement= new ProductManagementModel();
			ProductManagement priceInquiry = new ProductManagementImpl();
			if(CommonUtility.validateString(JsonData).trim().length()>0)
			{
				JsonObject jsonObject = new JsonObject();
				JsonElement json = new JsonParser().parse(JsonData);
				jsonObject = json.getAsJsonObject();
				ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
				ArrayList<Integer> eachItemList = new ArrayList<Integer>();
				for (Map.Entry<String,JsonElement> entry : jsonObject.entrySet()) {
				    System.out.println(entry.getKey());
				    Gson recentViewObj = new Gson();
				    if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MAKE_PRICE_ENQUIRY_RECENT_VIEWED_ITEMS")).equalsIgnoreCase("Y")) {
				    	recent = extractProductFromJson(entry.getValue());
				    }else {
				    	recent=recentViewObj.fromJson(entry.getValue(), ProductsModel.class);
				    }
				    System.out.println("ItemID: "+recent.getItemId()+"\n"+entry.getValue());
				    eachItemList.add(recent.getItemId());
					itemDataList.add(recent);	
				}
				if(eachItemList.size()>0){
					itemDataList = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubsetId, StringUtils.join(eachItemList," OR "),0,null,"itemid");
				}
				for( ProductsModel itemModel : itemDataList ) {
				    if(itemSet.add(itemModel.getItemId())) {
				    	resultWithoutDuplicate.add(itemModel);
				    	partIdentifierQuantity.add(recent.getQty());
				    }
				}
				itemDataList = resultWithoutDuplicate;
				productManagement.setPartIdentifier(itemDataList);
				productManagement.setPartIdentifierQuantity(partIdentifierQuantity);
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MAKE_PRICE_ENQUIRY_RECENT_VIEWED_ITEMS")).equalsIgnoreCase("Y") && itemDataList.size()>0) {
					itemDataList= priceInquiry.priceInquiry(productManagement, itemDataList);
				}
			}
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("itemDataList",itemDataList);
			contentObject.put("responseType", "recentlyViewed");
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ResultLoader";
	}

	
	private ProductsModel extractProductFromJson(JsonElement data) {
		JsonObject item = data.getAsJsonObject();
		ProductsModel product = new ProductsModel();
		product.setItemId((item.get("itemId") != null) ? Integer.parseInt(item.get("itemId").getAsString()) : 1);
		product.setItemPriceId((item.get("itemPriceId") != null) ? Integer.parseInt(item.get("itemPriceId").getAsString()) : 1);
		product.setQty((item.get("qty") != null) ? Integer.parseInt(item.get("qty").getAsString()) : 1);
		product.setMinOrderQty((item.get("minOrderQty") != null) ? Integer.parseInt(item.get("minOrderQty").getAsString()) : 1);
		product.setOrderInterval((item.get("orderInterval") != null) ? Integer.parseInt(item.get("orderInterval").getAsString()) : 1);
		product.setProductName(item.get("productName").getAsString());
		product.setImageName(item.get("imageName").getAsString());
		product.setImageURL((item.get("imageUrl") != null)?item.get("imageUrl").getAsString():"");
		
		product.setPartNumber(item.get("partNumber").getAsString());
		product.setManufacturerPartNumber((item.get("MPN") != null)?item.get("MPN").getAsString():"");
		return product;
	}
	
	
public String kitItems(){
		
		target="singleItem";
		
		try
		{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			//renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
			
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String entityId = (String) session.getAttribute("entityId");
			String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
	 		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
				
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		    int subsetId = CommonUtility.validateNumber(tempSubset);
		    int resultPerPage = 1;
		    String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			HashMap<String, ArrayList<ProductsModel>> searchResultList = ProductHunterSolr.searchNavigation(partNumber, subsetId, generalSubset, 0, 1, "SEARCH-AS08", varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,resultPerPage,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","", "","",false,true,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHouseItems);
			if(searchResultList!=null)
			itemLevelFilterData = searchResultList.get("itemList");
			
			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0)
			{
				itemPriceId = itemLevelFilterData.get(0).getItemPriceId();
	    		itemUrlSingle = itemLevelFilterData.get(0).getItemUrl();
	    		itemUrlSingle = CommonUtility.getUrlString(itemUrlSingle);
	    		
			}
			else
			{
				itemPriceId = 0;
				itemUrlSingle = "No-Data";
			}
    		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return target;
	}

public String getCustomerPartNumbersFromErp(){

	target = "SESSIONEXPIRED";
	request =ServletActionContext.getRequest();
	HttpSession session = request.getSession(); 

	String partNumber = (String) request.getParameter("partNumber");
	String itemId = (String) request.getParameter("itemId");
	String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
	String entityId = (String)session.getAttribute("billingEntityId");
	int userId = CommonUtility.validateNumber(sessionUserId);
	int buyingCompanyId = 0;
	int count = 0;
	buyingCompanyId = CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString());
	String shiptoId = "";
	if(session.getAttribute("selectedshipToIdSx")!=null){
		shiptoId = CommonUtility.validateString(session.getAttribute("selectedshipToIdSx").toString());
	}
	//ArrayList<ProductsModel> customerPartNumbers = null;
	if(userId>1){
		try
		{
			ProductManagement priceInquiry = new ProductManagementImpl();
			ProductManagementModel customerPartNumberInquiryInput = new ProductManagementModel();
			customerPartNumberInquiryInput.setUserToken((String) session.getAttribute("userToken"));
			customerPartNumberInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
			customerPartNumberInquiryInput.setEntityId(CommonUtility.validateString(entityId));
			ProductManagementModel customerPartNumberInquiryOutPut = priceInquiry.customerPartNumberInquiry(customerPartNumberInquiryInput);
			
			if(customerPartNumberInquiryOutPut!=null && customerPartNumberInquiryOutPut.getErrorCode()!=null && customerPartNumberInquiryOutPut.getErrorCode().trim().equalsIgnoreCase("0") && customerPartNumberInquiryOutPut.getErrorMessage()==null){
				result = "0|";
				LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartnumber = ProductHunterSolr.getcustomerPartnumber(itemId, buyingCompanyId, buyingCompanyId);
				customerPartNumberList = customerPartnumber.get(CommonUtility.validateNumber(itemId));
				if(customerPartNumberList!=null && customerPartNumberList.size() > 0){
				String[] custPartList = new String[customerPartNumberList.size()];
				
				for (int i = 0; i < customerPartNumberList.size(); i++) {
					custPartList[i] = customerPartNumberList.get(i).getIdList();
				}
				
				count = ProductHunterSolr.removeAllCustomerPartNumber(custPartList, CommonUtility.validateNumber(itemId), buyingCompanyId);
				if(count == 0){
					result = "1|";
				}
				}
				ArrayList<ProductsModel> customerPartNumbers = customerPartNumberInquiryOutPut.getItemsCpnList().get(partNumber);
				if(customerPartNumbers!=null && customerPartNumbers.size() > 0){
					for (int i = 0; i < customerPartNumbers .size(); i++) {
						if(i!=customerPartNumbers.size()-1){
							result = result + customerPartNumbers.get(i).getPartNumber() + ", ";
						}else{
							result = result + customerPartNumbers.get(i).getPartNumber();
						}
						
						if(CommonUtility.validateString(customerPartNumbers.get(i).getRecordType()).equalsIgnoreCase("c")){
							shiptoId = "";
						}
						
						count = ProductHunterSolr.addNewCustomerPartNumber(customerPartNumbers.get(i).getPartNumber(), CommonUtility.validateNumber(itemId), userId, CommonUtility.validateNumber(entityId), useBillToEntity, buyingCompanyId, shiptoId, customerPartNumbers.get(i).getRecordType(), customerPartNumbers.get(i).getRecordDescription());
						if(count == 0){
							result = "1|";
						}
					}
				}
				
			}else{
				result = "1|";
			}

			
			
			
			renderContent = result;
			target = SUCCESS;

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}else{
		target = "SESSIONEXPIRED";
	}
	return target;
}


public String getInteractiveAdvoiceItems(){
	try {
		ArrayList<ProductsModel> interActiveAdvisorItemList = null;
		ArrayList<ProductsModel> interActiveAdvisorItemDetailsList = null;
		String itemPartNumber = "";
		String tempSubset = "";
		String tempGeneralSubset = "";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession(); 
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		
		if(CommonUtility.validateString(request.getParameter("partNumber")).length()>0){
			itemPartNumber = CommonUtility.validateString(request.getParameter("partNumber"));
		}
		
		if(session!=null){
			tempSubset = (String) session.getAttribute("userSubsetId");
			tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		}
		
		int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		int subsetId = CommonUtility.validateNumber(tempSubset);
		
		if(CommonUtility.validateString(itemPartNumber).length()>0){
			interActiveAdvisorItemList = Rpserviceimpl.callInterActiveAdvisor(itemPartNumber);
			if(interActiveAdvisorItemList!=null && interActiveAdvisorItemList.size()>0){
				String buildPartNumberForInterActiveAdvoisor = "";
				String delimit = "";
				
				for(ProductsModel interActiveModel : interActiveAdvisorItemList){
					buildPartNumberForInterActiveAdvoisor = buildPartNumberForInterActiveAdvoisor+delimit+interActiveModel.getPartNumber();
					delimit = " OR ";
				}
				
				if(buildPartNumberForInterActiveAdvoisor!=null && buildPartNumberForInterActiveAdvoisor.trim().length()>0){
					interActiveAdvisorItemDetailsList = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId, generalSubset, buildPartNumberForInterActiveAdvoisor,0,null,"partnumber");//buildPartNumberForInterActiveAdvoisor
					if(interActiveAdvisorItemDetailsList!=null && interActiveAdvisorItemDetailsList.size()>0){
						
						for(ProductsModel interActiveAdvisorItemDetailsListModel : interActiveAdvisorItemDetailsList){
							if(interActiveAdvisorItemDetailsListModel!=null){
								for(ProductsModel interActiveAdvisorItemListModel : interActiveAdvisorItemList){
									if(interActiveAdvisorItemDetailsListModel.getPartNumber().equals(interActiveAdvisorItemListModel.getPartNumber().trim())){
										interActiveAdvisorItemDetailsListModel.setInterActiveOfferPosition(interActiveAdvisorItemListModel.getInterActiveOfferPosition());
									}
								}
							}
					   	}
						
						Collections.sort(interActiveAdvisorItemDetailsList, ProductsModel.interActiveOfferPositionComparator);
						/*for(ProductsModel interActiveAdvisorItemDetailsListModel : interActiveAdvisorItemDetailsList){
							System.out.println(interActiveAdvisorItemDetailsListModel.getInterActiveOfferPosition()+" : "+interActiveAdvisorItemDetailsListModel.getPartNumber());
						}*/
						contentObject.put("interActiveAdvisorItemDetailsList", interActiveAdvisorItemDetailsList);
					}
				}
			}
		}
		
		renderContent = LayoutGenerator.templateLoader("InteractiveAdviserPage", contentObject , null, null, null);
		target = SUCCESS;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return target;
}
	
public String getFeatureProduct(){
	try{
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
	
		String tempSubset = (String) session.getAttribute("userSubsetId");
	
	    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		int subsetId = CommonUtility.validateNumber(tempSubset);
	   
		int resultPerPage = CommonUtility.validateNumber(resultPage);
	    if(resultPerPage==0)
	    {
	    	resultPage = "3";
	    	resultPerPage = 3;
	    }
		
		 int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;
	    int fromRow =  (noOfPage-1)*resultPerPage + 1;
	    itemLevelFilterData = new ArrayList<ProductsModel>();
	    itemLevelFilterData = ProductHunterSolr.getFeatureProduct(subsetId,generalSubset,fromRow,resultPerPage, false);
		
	    if(itemLevelFilterData!=null && itemLevelFilterData.size()>0)
	    {
		resultCount = itemLevelFilterData.get(0).getResultCount();
		
		double disp;
		double disp1;
	 	disp = resultCount;
	
	 	if (resultCount > resultPerPage) {
	
	 		paginate = (resultCount / resultPerPage);
	 		disp1 = (disp / resultPerPage);
	
	 	} else {
	 		paginate = (resultCount / resultPerPage);
	 		disp1 = (resultCount / resultPerPage);
	 	}
	
	 	if (disp1 > paginate) {
	 		paginate = paginate + 1;
	
	 	}
	    }
		 LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		 contentObject.put("categoryListData", taxonomyLevelFilterData);
		 contentObject.put("attrFilterData", attrFilterData);
		 contentObject.put("navigationType", "NAVIGATION");
		 contentObject.put("sortBy", sortBy);
		 contentObject.put("resultPage", resultPage);
		 
		 renderContent = LayoutGenerator.templateLoader("FeatureProduct", contentObject , null, null, null);
	}catch (Exception e) {
		e.printStackTrace();
	}
	 
	 return SUCCESS;
}
public void saveItemLevelShipVia(){
	try{
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String savedListId=request.getParameter("savedListId");
		String itemId=request.getParameter("itemId");
		String itemLevelShipvia=request.getParameter("itemLevelShipvia");
		String itemLevelShipviaDesc=request.getParameter("itemLevelShipviaDesc");
		String pageName=request.getParameter("pageName");
		int cartId= CommonUtility.validateNumber((String)request.getParameter("cartId"));
		//ArrayList<Integer> cartId = new ArrayList<Integer>();
		String lineItemComment=request.getParameter("lineItemComment");
		 ProductsDAO product=new ProductsDAO();
		if(pageName.equalsIgnoreCase("cart")){
			//cartId = ProductsDAO.selectFromCart(userId, CommonUtility.validateNumber(itemId));
			if(userId<2)
			{
				product.saveItemLevelShipViaBySession(0,CommonUtility.validateNumber(itemId),itemLevelShipvia,itemLevelShipviaDesc,cartId,pageName,userId,session,lineItemComment);
			}else{
				product.saveItemLevelShipVia(0,CommonUtility.validateNumber(itemId),itemLevelShipvia,itemLevelShipviaDesc,cartId,pageName,userId);
			}
			
		}else if(pageName.equalsIgnoreCase("approveCart")){
			product.saveItemLevelShipVia(CommonUtility.validateNumber(savedListId),CommonUtility.validateNumber(itemId),itemLevelShipvia,itemLevelShipviaDesc,0,pageName,userId);
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
}


public void reorderItemLevelShipVia(){
	try{
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String partNumber=request.getParameter("partNumber");
		String itemLevelShipvia=request.getParameter("itemLevelShipvia");
		String itemLevelShipviaDesc=request.getParameter("itemLevelShipviaDesc");
		String quoteCartId= request.getParameter("quoteCartId");
		String pageName="reOrder";
	      ProductsDAO product=new ProductsDAO();
	      product.reorderItemLevelShipVia(CommonUtility.validateNumber(quoteCartId),CommonUtility.validateNumber(partNumber),itemLevelShipvia,itemLevelShipviaDesc);
	}catch (Exception e) {
		e.printStackTrace();
	}
}

public String getPriceAvailabilityDetail()
{
	try{
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String jsonResponse = "";
		ArrayList<WarehouseModel> whse = new ArrayList<WarehouseModel>();
		String productId = request.getParameter("productId");
		String loadPrice = request.getParameter("loadPrice");
		if(productId!=null && !productId.trim().equalsIgnoreCase("")){
			String[] priceIdList = null;
			priceIdList = productId.split(",");
			if(priceIdList!=null && priceIdList.length>0)
			{
				if(session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
				{
					String wIndex = request.getParameter("idx");
					if(CommonUtility.validateString(loadPrice).trim().equalsIgnoreCase("Yes")){
						whse = UsersDAO.getWareHouseListByCustomFields();
					}else{
						whse = UsersDAO.getWareHouseListFromIndex(wIndex);
					}
					String warehouse="";
					String split="";
					for(int i=0;i<whse.size();i++){
						warehouse = warehouse+split+whse.get(i).getWareHouseCode();
						split = ",";
					}
					ArrayList<ProductsModel> productsInput = new ArrayList<ProductsModel>();
					for(String eachPartnumber:priceIdList){
						ProductsModel eachProduct = new ProductsModel();
						eachProduct.setPartNumber(eachPartnumber);
						productsInput.add(eachProduct);
					}
					UserManagement usrObj = new UserManagementImpl();
					UsersModel usersModel = new UsersModel();
					usersModel.setSession(session);
					usrObj.checkERPConnection(usersModel);
					ProductManagement priceInquiry = new ProductManagementImpl();
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					String entityId = (String) session.getAttribute("entityId");
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					priceInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
					priceInquiryInput.setPartIdentifier(productsInput);
					priceInquiryInput.setAllBranchavailabilityRequired("Y");
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					priceInquiryInput.setFromDetailPage(true);
					priceInquiryInput.setWareHouse(warehouse);
					jsonResponse = priceInquiry.ajaxPriceInquiry(priceInquiryInput);
	
				}
			}
		}
		renderContent = jsonResponse;
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}

public String getAllBranchAvailabilityForProductCode()
{
	try{
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String jsonResponse = "";
		ArrayList<WarehouseModel> whse = new ArrayList<WarehouseModel>();
		LinkedHashMap<String, String> warehouseMap = new LinkedHashMap<String, String>();
		String productId = request.getParameter("productId");
		String loadPrice = request.getParameter("loadPrice");
		if(productId!=null && CommonUtility.validateString(productId).length()>0){
			//String[] priceIdList = null;
			//priceIdList = productId.split(",");
			//if(priceIdList!=null && priceIdList.length>0){
				if(session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
				{
					String wIndex = request.getParameter("idx");
					if(CommonUtility.validateString(loadPrice).trim().equalsIgnoreCase("Yes")){
						whse = UsersDAO.getWareHouseListByCustomFields();
					}else{
						whse = UsersDAO.getWareHouseListFromIndex(wIndex);
					}
					String warehouse="";
					String split="";
					for(int i=0;i<whse.size();i++){
						warehouse = warehouse+split+whse.get(i).getWareHouseCode();
						warehouseMap.put(whse.get(i).getWareHouseCode(), whse.get(i).getWareHouseName());
						split = ",";
					}
	
					UserManagement usrObj = new UserManagementImpl();
					UsersModel usersModel = new UsersModel();
					usersModel.setSession(session);
					usrObj.checkERPConnection(usersModel);
					ProductManagement availabilityInquiry = new ProductManagementImpl();
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					String entityId = (String) session.getAttribute("entityId");
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					priceInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
					//priceInquiryInput.setPartIdentifier(new ArrayList<String>(Arrays.asList(priceIdList)));
					priceInquiryInput.setProductNumber(CommonUtility.validateString(productId));
					//priceInquiryInput.setAllBranchavailabilityRequired("Y");
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					priceInquiryInput.setFromDetailPage(true);
					priceInquiryInput.setWareHouse(warehouse);
					priceInquiryInput.setFlag(true);
					priceInquiryInput.setDefaultHashMap(warehouseMap);
					jsonResponse = availabilityInquiry.getAllBranchAvailabilityForProductCode(priceInquiryInput);
	
				}
			//}
		}
		renderContent = jsonResponse;
	}catch (Exception e) {
		e.printStackTrace();
	}
	return SUCCESS;
}

	public String updateCustomerPartNumber(){
		int entityId = 0;
		int resultInt = -2;
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
		String result = "";
		try{
			String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String shiptoId = "";

			if(session.getAttribute("selectedshipToIdSx")!=null){
					shiptoId = CommonUtility.validateString(session.getAttribute("selectedshipToIdSx").toString());
			}
			//String userEntity = (String) session.getAttribute("customerEntity");
			String userEntity = (String) session.getAttribute("customerId");
			
			if(userEntity!=null){
				entityId = CommonUtility.validateNumber(userEntity);
			}else{
				entityId = buyingCompanyId;
			}
			
			
			//-------------------------- write to ERP
			ProductManagement productOBJ = new ProductManagementImpl();
			String newCpn = request.getParameter("newCpn");
			String oldCpn = request.getParameter("oldCpn");
			ProductManagementModel customerPartNumberInquiryInput = new ProductManagementModel();
			customerPartNumberInquiryInput.setEntityId(CommonUtility.validateString((String) session.getAttribute("entityId")));
			customerPartNumberInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
			customerPartNumberInquiryInput.setProductNumber(partNumber);
			customerPartNumberInquiryInput.setNewCustomerPartNumber(newCpn);
			customerPartNumberInquiryInput.setOldCustomerPartNumber(oldCpn);
			customerPartNumberInquiryInput.setRequiredAvailabilty("Y");
			customerPartNumberInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
			customerPartNumberInquiryInput.setUserToken((String)session.getAttribute("userToken"));
			customerPartNumberInquiryInput.setCustomerId((String) session.getAttribute("customerId"));
			ProductManagementModel customerPartNumberInquiryOutPut = productOBJ.customerPartNumberCreate(customerPartNumberInquiryInput);
			//--------------------------
			if(customerPartNumberInquiryOutPut!=null && customerPartNumberInquiryOutPut.getErrorCode()!=null && customerPartNumberInquiryOutPut.getErrorCode().trim().equalsIgnoreCase("0") && customerPartNumberInquiryOutPut.getErrorMessage()==null){
				
				
				if(CommonUtility.validateString(customerPartNumberInquiryOutPut.getRecordType()).equalsIgnoreCase("c")){
					shiptoId = "";
				}
				
				
				String cutomerPartList[] = vpsid.split(",");
				resultInt = ProductHunterSolr.removeCustomerPartNumber(cutomerPartList, vpsid);
				resultInt = ProductHunterSolr.addNewCustomerPartNumber(newCpn, itemPriceId,  userId, entityId, useBillToEntity, buyingCompanyId,shiptoId,customerPartNumberInquiryOutPut.getRecordType(),customerPartNumberInquiryOutPut.getRecordDescription());
				//resultInt = (ProductHunterSolr.addNewCustomerPartNumber(keyWord, itemPriceId,  userId, entityId, useBillToEntity, buyingCompanyId));
			}else if(customerPartNumberInquiryOutPut!=null && customerPartNumberInquiryOutPut.getErrorCode()!=null && customerPartNumberInquiryOutPut.getErrorCode().trim().equalsIgnoreCase("-2")){
				resultInt = -2;
				result = CommonUtility.validateString(customerPartNumberInquiryOutPut.getErrorMessage());
			}
		
		
			if(resultInt != 0) {
				if(resultInt == -1){
					renderContent = "Please Enter Valid Customer Part Number|";
				}else if(resultInt == -2 && CommonUtility.validateString(result).length()>0 ){
					renderContent = "";
				}else{
					renderContent = "Customer Part Number Added Successfully|";
				}
					
				
				LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartnumber = ProductHunterSolr.getcustomerPartnumber(Integer.toString(itemPriceId), buyingCompanyId, buyingCompanyId);
				customerPartNumberList = customerPartnumber.get(itemPriceId);
				if(customerPartNumberList!=null){
					for(ProductsModel cc : customerPartNumberList){
						if(renderContent.equalsIgnoreCase("Customer Part Number Added Successfully|")){
							renderContent = renderContent +  cc.getPartNumber() ;
						}else{
							if(CommonUtility.validateString(renderContent).length()>0){
								renderContent = renderContent + ","+ cc.getPartNumber() ;
							}else{
								renderContent = cc.getPartNumber() ;
							}
							
						}
					}
				}
			}else{
				renderContent = "Error occured while process your request|";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "ResultLoader";
	}

	public String catalogPdfs(){
		try
		{
			//request =ServletActionContext.getRequest();
			//HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			renderContent = LayoutGenerator.templateLoader("CatalogPage", contentObject , null, null, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String getBlogDetails(){
		String jsonResponse = "";
		try{
			
			Gson gson = new Gson();
			jsonResponse = gson.toJson(CIMMTouchUtility.getInstance().getBlogEntry(listId,srchTyp));
		}catch(Exception e){
			e.printStackTrace();
		}
		renderContent = jsonResponse;
		return SUCCESS;
	}

	public void setMpnStrArray(String mpnStrArray[]) {
		this.mpnStrArray = mpnStrArray;
	}
	public String[] getMpnStrArray() {
		return mpnStrArray;
	}
	public String autoComplete(){
		try{
			request = ServletActionContext.getRequest();
			ProductsModel variables = new ProductsModel();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String serviceName = "";
			if(CommonUtility.validateString(request.getParameter("ModelName")).length()>0){
				variables.setModelName(request.getParameter("ModelName"));
				serviceName = "MODELONLY";
			}else if(CommonUtility.validateString(request.getParameter("PartNumber")).length()>0){
				variables.setPartNumber(request.getParameter("PartNumber"));
				serviceName = "PARTONLY";
			}else if (CommonUtility.validateString(request.getParameter("serviceName")).length()>0){
				serviceName = request.getParameter("serviceName");
				variables.setResultCount(CommonUtility.validateNumber(request.getParameter("totalResult")));
				variables.setQueryString(request.getParameter("q"));
				variables.setSubsetId(CommonUtility.validateNumber(tempSubset));
			}
			variables.setBrandName(request.getParameter("BrandName"));
			renderContent  = CustomAutoComplete.getInstance().CustomAutoCompleteReturn(serviceName, variables);
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String getAutoCompleteResults() {
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String q = request.getParameter("q");
		String serviceName = request.getParameter("serviceName");
		String sStart = request.getParameter("iDisplayStart");
		String sAmount = request.getParameter("iDisplayLength");
		String sEcho = request.getParameter("sEcho");
		String sCol = request.getParameter("iSortCol_0");
		String sdir = request.getParameter("sSortDir_0");
		String searchTerm = request.getParameter("sSearch");
		String engine = request.getParameter("sSearch_0");
		String browser = request.getParameter("sSearch_1");
		String platform = request.getParameter("sSearch_2");
		String version = request.getParameter("sSearch_3");
		String grade = request.getParameter("sSearch_4");
		String responseType = request.getParameter("responseType");

		String subsetId = (String) session.getAttribute("userSubsetId");
		q = ProductHunterSolr.escapeQueryCulpritsWithoutWhiteSpace(q);
		searchTerm = CommonUtility.validateString(searchTerm);
		try{
			AutoCompleteDataTableModel autoCompleteDataTableModel = new AutoCompleteDataTableModel();
			autoCompleteDataTableModel.setsStart(sStart);
			autoCompleteDataTableModel.setSearchTerm(CommonUtility.validateString(searchTerm));
			autoCompleteDataTableModel.setsAmount(sAmount);
			autoCompleteDataTableModel.setQuery(CommonUtility.validateString(q)	);
			autoCompleteDataTableModel.setSubsetId(CommonUtility.validateNumber(subsetId));
			autoCompleteDataTableModel.setSdir(sdir);
			autoCompleteDataTableModel.setResponseType(responseType);
			
			if(CommonUtility.validateString(serviceName).equalsIgnoreCase("modelpart")){
				result = CustomAutoComplete.getInstance().getAllModelParts(autoCompleteDataTableModel);
			}else if(CommonUtility.validateString(serviceName).equalsIgnoreCase("category")){
				result = CustomAutoComplete.getInstance().getAllCategories(autoCompleteDataTableModel);
			}else if(CommonUtility.validateString(serviceName).equalsIgnoreCase("manufacturer")){
				result = CustomAutoComplete.getInstance().getAllManufacturers(autoCompleteDataTableModel);
			}else if(CommonUtility.validateString(serviceName).equalsIgnoreCase("categoryFAYT")){
				autoCompleteDataTableModel.setFayttypeidx("CATEGORY");
				result = CustomAutoComplete.getInstance().getAllFAYT(autoCompleteDataTableModel);
			}else if(CommonUtility.validateString(serviceName).equalsIgnoreCase("brandCategoryFAYT")){
				autoCompleteDataTableModel.setFayttypeidx("BRAND_CATEGORY");
				result = CustomAutoComplete.getInstance().getAllFAYT(autoCompleteDataTableModel);
			}else if(CommonUtility.validateString(serviceName).equalsIgnoreCase("brand")){
				result = CustomAutoComplete.getInstance().getAllFAYT(autoCompleteDataTableModel);
			}else if(CommonUtility.validateString(serviceName).equalsIgnoreCase("attributes")){
				result = CustomAutoComplete.getInstance().getAllAttribute(autoCompleteDataTableModel);
			}else if(CommonUtility.validateString(serviceName).equalsIgnoreCase("partnumber")){
				result = CustomAutoComplete.getInstance().getAllPartNumbers(autoCompleteDataTableModel);
			}else if(CommonUtility.validateString(serviceName).equalsIgnoreCase("userdefinied") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USER_DEFINED_AUTO_COMPLETE")).equalsIgnoreCase("Y")){
				result = CustomAutoComplete.getInstance().getUserDefinied(autoCompleteDataTableModel);
			}else{
				result="";
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		renderContent = result;
		return SUCCESS;
	}
	public String getPromoCode(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String wareHouseCode = (String) session.getAttribute("wareHouseCode");
			String promoCode = request.getParameter("promoCode");
			String cartTotal = request.getParameter("cartTotal");
			String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			String message = "";
			double total = 0.0;
			DecimalFormat df = new DecimalFormat("#.00");
			String isPromoCodeApplied = request.getParameter("isPromoCodeApplied");
			ProductsModel promotion = new ProductsModel();
			ProductsModel couponInfo = new ProductsModel();
			promotion.setWareHouseCode(wareHouseCode);
			promotion.setCartTotal(Double.parseDouble((cartTotal)));
			promotion.setPromoCode(promoCode);
			if(promoCode!=null && promoCode.length() > 0){	
				session.setAttribute("promotionCode",promoCode);
				promotion = ProductsDAO.getPromotionalCode(promotion);
				couponInfo = ProductsDAO.getCouponCounter(buyingCompanyId, promoCode);
				if(couponInfo!=null && couponInfo.getCouponCounter() > promotion.getCouponCounter()){
					message = "Coupon has expired";
				}
			}
			if(promotion!=null && couponInfo!=null && CommonUtility.validateString(promotion.getDiscountType())!="" && CommonUtility.validateString(promotion.getDiscountValue())!="" && couponInfo.getCouponCounter() < promotion.getCouponCounter()){
				if(promotion.getDiscountType().trim().equals("$")){
					total = (Double.parseDouble(cartTotal) - CommonUtility.validateNumber(promotion.getDiscountValue()));
					isPromoCodeApplied = "Y";
					session.setAttribute("discountType", promotion.getDiscountType());
					session.setAttribute("discountValue", promotion.getDiscountValue());
					session.setAttribute("discountValueToERP", promotion.getDiscountValue());
					shoppingCart();
					renderContent = isPromoCodeApplied+"|"+message;
				}
				else if(promotion.getDiscountType().trim().equals("%")){
					double calcDiscount = ((Double.parseDouble(cartTotal) * (Double.parseDouble(promotion.getDiscountValue())))/100);
					total = Double.parseDouble(cartTotal) - calcDiscount;
					isPromoCodeApplied = "Y";
					session.setAttribute("discountType", promotion.getDiscountType());
					session.setAttribute("discountValue", String.valueOf(calcDiscount));
					session.setAttribute("discountValueToERP", promotion.getDiscountValue());
					shoppingCart();
					renderContent = isPromoCodeApplied+"|"+message;
				}
			} 
			else{
				session.removeAttribute("discountType");
				session.removeAttribute("discountValue");
				shoppingCart();
				renderContent = "N|"+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("promotion.error.message");
			}
			session.setAttribute("isPromoCodeApplied", isPromoCodeApplied);
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String ModelPart() {
		try{
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject = DataSmartController.getInstance().getAllBrandsWithARI(contentObject);
			renderContent = LayoutGenerator.templateLoader("ARIMainPage", contentObject , null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String getBrandPrefix() {
		renderContent = DataSmartController.getInstance().getBrandPrefixFromARICode(brand.toUpperCase());
		return SUCCESS;
	}
	
	public String getAlternateItem(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String itemPartNumber = request.getParameter("partNo");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			if(CommonDBQuery.getSystemParamtersList().get("GET_ALTERNATIVE_ITEMS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ALTERNATIVE_ITEMS").trim().equalsIgnoreCase("Y")){
				ProductManagement productOBJ = new ProductManagementImpl();
				ProductManagementModel alternateItemsRequest = new ProductManagementModel();
				alternateItemsRequest.setEntityId(CommonUtility.validateString((String) session.getAttribute("entityId")));
				alternateItemsRequest.setHomeTerritory((String) session.getAttribute("shipBranchId"));
				alternateItemsRequest.setProductNumber(itemPartNumber);
				alternateItemsRequest.setRequiredAvailabilty("Y");
				alternateItemsRequest.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				alternateItemsRequest.setUserToken((String)session.getAttribute("userToken"));
				alternateItemsRequest.setCustomerId((String) session.getAttribute("customerId"));
				ArrayList<ProductsModel> alternativeItemsErp = productOBJ.getAlternateItems(alternateItemsRequest);
				
				if(alternativeItemsErp!=null && !alternativeItemsErp.isEmpty()){
					String buildPartNumberForAlternateItems = "";
					String delimit = "";
					
					for(ProductsModel alternativeItemsErpModel : alternativeItemsErp){
						buildPartNumberForAlternateItems = buildPartNumberForAlternateItems+delimit+alternativeItemsErpModel.getPartNumber();
						delimit = " OR ";
					}
					
					if(CommonUtility.validateString(buildPartNumberForAlternateItems).length()>0){
						alternativeItems = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubset, CommonUtility.validateString(buildPartNumberForAlternateItems),0,null,"partnumber");
					}
					
					if(alternativeItems!=null && !alternativeItems.isEmpty()){
						for(ProductsModel alternativeItemsModelOne : alternativeItems){
							for(ProductsModel alternativeItemsERPModel : alternativeItemsErp){
								if(CommonUtility.validateString(alternativeItemsModelOne.getPartNumber()).equalsIgnoreCase(CommonUtility.validateString(alternativeItemsERPModel.getPartNumber()))){
									alternativeItemsModelOne.setRequestType(alternativeItemsERPModel.getRequestType());
								}
							}
						}
					}
					
				}
				
				contentObject.put("alternativeItems", alternativeItems);
			}
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String BrandCodeARI() {
		try{
			if(CommonUtility.validateString(brand).length()>0){
				renderContent = DataSmartController.getInstance().getARIBrandCode(brand.toUpperCase());
			}else{
				renderContent = "";	
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String AddToCartNCI(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			System.out.println(DataSmartController.getInstance().getARIBrandCode(CommonUtility.validateString(brand).toUpperCase()));
			String brandPrefix = DataSmartController.getInstance().getBrandPrefix(CommonUtility.validateString(brand).toUpperCase());
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String partNumber = request.getParameter("Sku");
			ProductsModel checkPartNumber  = ProductsDAO.checkItemExistInCIMM(brandPrefix+partNumber,tempSubset);
			String price = request.getParameter("MSRP");
			String desc = request.getParameter("Description");
			String qty = request.getParameter("QtyNew");
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			UsersModel userData = null;
			if(checkPartNumber!=null){
				LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
				utilityMap.put("considerLineItemComment", false);
				int msg  = ProductsDAO.insertItemToCart(CommonUtility.validateNumber(sessionUserId),checkPartNumber.getItemId(),CommonUtility.validateNumber(qty),session.getId(),"",null,"",price, 0, 0.0, 0.0, utilityMap);
			}else{
				String ariID = request.getParameter("ARIBrandID");
				brand = DataSmartController.getInstance().getBrandNameFromARICode(ariID.toUpperCase());
				checkPartNumber = new ProductsModel();
				checkPartNumber.setBrandName(brand);
				checkPartNumber.setPartNumber(partNumber);
				checkPartNumber.setPrice(Double.parseDouble(price));
				checkPartNumber.setDescription(desc);
				checkPartNumber.setManufacturerName(brand);
				checkPartNumber.setQty(CommonUtility.validateNumber(qty));
				userData = new UsersModel();
				userData.setUserId(CommonUtility.validateNumber(sessionUserId));
				userData.setSession(session);
				int count = ProductsDAO.insertNonCatalogItemToCart(checkPartNumber,userData);
				if(count>0){
					session.removeAttribute("cartCountSession");
					VelocityContext contextTemp = null; 
					getCartCount( contextTemp, request);
					renderContent = ""+validateResponse;
				}else{
					renderContent = "0";
				}
				  
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String AutoCompleteDetail(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			ArrayList<ProductsModel> itemDetails = ProductHunterSolr.getItemListByItemId(codeId, subsetId, generalSubset, 12, 0, "");
			if(itemDetails!=null && itemDetails.size()==1){
				itemPriceId = itemDetails.get(0).getItemPriceId();
				itemUrlSingle = itemDetails.get(0).getItemUrl();
				itemUrlSingle = CommonUtility.getUrlString(itemUrlSingle);
			    target="singleItem";	
			}else{
				target = "none";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return target;
	}
	
	public String ajaxBasePriceAndLeadTime()
	{
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String jsonResponse = "";
			if(productIdList!=null && !productIdList.trim().equalsIgnoreCase("")){
				String[] priceIdList = null;
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTNUMBER_SPLIT_CHARACTER")).length()>0) {
					priceIdList = productIdList.split(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTNUMBER_SPLIT_CHARACTER")));	
				}else {
					priceIdList = productIdList.split(",");
				}
				if(priceIdList!=null && priceIdList.length>0)
				{
					if(session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
					{
						ArrayList<ProductsModel> productsInput = new ArrayList<ProductsModel>();
						for(String eachPartnumber:priceIdList){
							ProductsModel eachProduct = new ProductsModel();
							eachProduct.setPartNumber(eachPartnumber);
							productsInput.add(eachProduct);
						}
						UserManagement usrObj = new UserManagementImpl();
						UsersModel usersModel = new UsersModel();
						usersModel.setSession(session);
						usrObj.checkERPConnection(usersModel);
						ProductManagement priceInquiry = new ProductManagementImpl();
						ProductManagementModel priceInquiryInput = new ProductManagementModel();
						String entityId = CommonUtility.validateString((String) session.getAttribute("entityId"));
						priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
						priceInquiryInput.setHomeTerritory(CommonUtility.validateString((String) session.getAttribute("shipBranchId")));
						priceInquiryInput.setPartIdentifier(productsInput);
						priceInquiryInput.setRequiredAvailabilty("Y");
						priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
						priceInquiryInput.setUserToken(CommonUtility.validateString((String)session.getAttribute("userToken")));
						priceInquiryInput.setSession(session);
						priceInquiryInput.setFromDetailPage(true);
						priceInquiryInput.setWareHouse(CommonUtility.validateString((String) session.getAttribute("wareHouseCode")));
						jsonResponse = priceInquiry.ajaxBasePriceAndLeadTime(priceInquiryInput);
						if(CommonUtility.validateString((String)session.getAttribute("connectionError")).equalsIgnoreCase("Yes")){
							UsersDAO ne = new UsersDAO();
							ne.checkEclipseSession();
							jsonResponse = priceInquiry.ajaxBasePriceAndLeadTime(priceInquiryInput);
							if(CommonUtility.validateString(jsonResponse).equalsIgnoreCase("")){
								renderContent = "0";	
							}else{
								renderContent = jsonResponse;	
							}
							
							System.out.println("Base Price & Lead time Json Response:"+jsonResponse);
							
						}else{
							renderContent = jsonResponse;
						}
						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String getARIItemDetail(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String brandPrefix = DataSmartController.getInstance().getBrandPrefix(CommonUtility.validateString(brand).toUpperCase());
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			ProductsModel checkPartNumber  = ProductsDAO.checkItemExistInCIMM(brandPrefix+partNumber,tempSubset);
			if(checkPartNumber!=null){
				codeId = ""+checkPartNumber.getItemPriceId();
				itemDetail();
			}else if(CommonUtility.validateString(codeId).length()>0){
				itemDetail();
			}else if(CommonUtility.validateString(partNumber).length()>0 && CommonUtility.validateString(brandId).length()>0){
				DataSmartController.getInstance().firstHitCheck();
				List<CustomTable> tableDetails = DataSmartController.getInstance().getCustomTableBrandData();
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				for(CustomTable eachBrand:tableDetails){
					if(brandId.equalsIgnoreCase(eachBrand.getEntityFieldValue().get("BRAND_ID"))){
						contentObject.put(DatasmartConstantVariables.BRAND_NAME,eachBrand.getEntityFieldValue().get("BRAND_NAME"));
						contentObject.put("ariPageHeading",ariPageHeading);
						contentObject.put("isARIPage","Y");
						contentObject.put(DatasmartConstantVariables.BRAND_ARI_CODE,eachBrand.getTableDetails().get(0).get("ARI_BRAND_CODE"));
						contentObject.put(DatasmartConstantVariables.BRAND_PREFIX_CODE,eachBrand.getTableDetails().get(0).get("SX_BRAND_PREFIX"));
						contentObject.put(DatasmartConstantVariables.BRAND_ID,eachBrand.getEntityId());
						contentObject.put("IS_ARI_BRAND",eachBrand.getTableDetails().get(0).get("IS_ARI_BRAND"));
						break;
					}
				}
				String[] urlParam = new String[]{brandId,partNumber};
				contentObject.put("responseType","ARIPartInfo");
				contentObject.put(DatasmartConstantVariables.REQ_TYPE,"partInfo");
				contentObject.put(DatasmartConstantVariables.URL_PARAM,urlParam);
				contentObject.put(DatasmartConstantVariables.SUBSET,tempSubset);
				contentObject.put(DatasmartConstantVariables.GENERAL_SUBSET,tempGeneralSubset);
				contentObject = DataSmartController.getInstance().getARIData(contentObject);
				renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject , null, null, null);	
				//renderContent = CommonUtility.validateString(partNumber);  
			}else{
				renderContent = "Invalid Request";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String checkNCItems(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String listToCheckItems = request.getParameter("listToCheck");
			ArrayList<ProductsModel> existingItems = ProductHunterSolr.getItemDetailsForGivenPartNumbers(CommonUtility.validateNumber(tempSubset),CommonUtility.validateNumber(tempGeneralSubset), CommonUtility.validateString(listToCheckItems),0,null,"partnumber");
			
			if(existingItems!=null){
				ArrayList<Integer> itemList = new ArrayList<Integer>();
				for(ProductsModel itemId:existingItems){
					itemList.add(itemId.getItemId());
				}
				LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(CommonUtility.validateNumber(tempSubset), CommonUtility.validateNumber(tempGeneralSubset), StringUtils.join(itemList," OR "),"itemid");
				for(ProductsModel itemId:existingItems){
					
					itemId.setCustomFieldVal(customFieldVal.get(itemId.getItemId()));
				}
				Gson convertToJson = new Gson();
				String fullResult = convertToJson.toJson(existingItems);
				renderContent = fullResult;
			}else{
				renderContent = CommonUtility.validateString(partNumber);	
			}
			System.out.println(renderContent);  
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String AriResults(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("ARIUrl", CommonDBQuery.getSystemParamtersList().get("ARI_SERVICE_URL"));
			contentObject.put(DatasmartConstantVariables.SUBSET, tempSubset);
			contentObject.put(DatasmartConstantVariables.GENERAL_SUBSET,tempGeneralSubset); 
			DataSmartController.getInstance().firstHitCheck();
			List<CustomTable> tableDetails = DataSmartController.getInstance().getCustomTableBrandData();
			String urlParams =  request.getParameter(DatasmartConstantVariables.URL_PARAMS);
			if(CommonUtility.validateString(urlParams).length()>0){
				String[] urlParam = request.getParameter(DatasmartConstantVariables.URL_PARAMS).split("\\-");
				if(tableDetails!=null && tableDetails.size()>0){
					for(CustomTable eachBrand:tableDetails){
						if(urlParam[0].equalsIgnoreCase(eachBrand.getEntityFieldValue().get("BRAND_ID"))){
							contentObject.put(DatasmartConstantVariables.BRAND_NAME,eachBrand.getEntityFieldValue().get("BRAND_NAME"));
							contentObject.put(DatasmartConstantVariables.ARI_PAGE_HEADING,ariPageHeading);
							contentObject.put(DatasmartConstantVariables.IS_ARI_PAGE,"Y");
							contentObject.put(DatasmartConstantVariables.BRAND_ARI_CODE,eachBrand.getTableDetails().get(0).get("ARI_BRAND_CODE"));
							contentObject.put(DatasmartConstantVariables.BRAND_PREFIX_CODE,eachBrand.getTableDetails().get(0).get("SX_BRAND_PREFIX"));
							contentObject.put(DatasmartConstantVariables.BRAND_ID,eachBrand.getEntityId());
							contentObject.put(DatasmartConstantVariables.IS_ARI_BRAND,eachBrand.getTableDetails().get(0).get("IS_ARI_BRAND"));
							break;
						}
					}
					String isARIBrand = (String) contentObject.get(DatasmartConstantVariables.IS_ARI_BRAND);
					String[] urlParamsToC2BC = urlParam;//urlParam[1].split("\\~");
					if(CommonUtility.validateString(isARIBrand).equalsIgnoreCase("Y")){
						if(CommonUtility.validateString(reqType).equalsIgnoreCase("searchModelAssemblies")){
							reqType = "searchModel";
							urlParamsToC2BC[1] = ProductsDAO.getARIModelNameFromModelID(urlParamsToC2BC[1],(String)contentObject.get(DatasmartConstantVariables.BRAND_ID));
							contentObject.put(DatasmartConstantVariables.URL_PARAM,urlParamsToC2BC);
							contentObject.put(DatasmartConstantVariables.REQ_TYPE,reqType);
							contentObject = DataSmartController.getInstance().getARIData(contentObject);
							DataSmartsSearchModel modelDetails = (DataSmartsSearchModel) contentObject.get("ObjectResponse");
							reqType = "searchModelAssemblies";
							if(modelDetails!=null && modelDetails.getData().getResults().size()>0){
								urlParamsToC2BC[1] = CommonUtility.validateString(""+modelDetails.getData().getResults().get(0).getModelId());	
							}else{
								urlParamsToC2BC = request.getParameter(DatasmartConstantVariables.URL_PARAMS).split("\\-");//urlParam[1].split("\\~");
							}
						}
						if(request.getParameter(DatasmartConstantVariables.URL_PARAMS)!=null){
							contentObject.put("isError","N");
							contentObject.put(DatasmartConstantVariables.URL_PARAM,urlParamsToC2BC);
							contentObject.put(DatasmartConstantVariables.REQ_TYPE,reqType);
							contentObject = DataSmartController.getInstance().getARIData(contentObject);
						}else{
							contentObject.put("isError","Y");
							contentObject.put("ErrorMsg", "No Brand Selected");
						}
					}else{
						contentObject.put("isError","Y");
						contentObject.put("ErrorMsg", "Brand Not Enabled in PIM");
					}
				}else{
					contentObject.put("isError","Y");
					contentObject.put("ErrorMsg", "Error. Contact Technical Support");
				}
			}else{
				contentObject.put("isError","Y");
				contentObject.put("ErrorMsg", "No Options to Display");
				
			}
			if(CommonUtility.validateString(resultPage).equalsIgnoreCase("image")){
				contentObject.put("responseType", "ARIImage");
				renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
			}else if(CommonUtility.validateString(resultPage).equalsIgnoreCase("json")){
				renderContent = (String)contentObject.get("JsonResponse");
			}else{
				renderContent = LayoutGenerator.templateLoader("ARIMainPage", contentObject , null, null, null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String SearchandAddToCart(){
		String target = SUCCESS;
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String itemPartNumber = request.getParameter("partNo0");
			qty =  request.getParameter("qty0");
			if(CommonUtility.validateString(itemPartNumber).length()>0){
				reqType = "update";
				String tempSubset = (String) session.getAttribute("userSubsetId");
				String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
				int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
				int subsetId = CommonUtility.validateNumber(tempSubset);
				ArrayList<ProductsModel> checkedItems = new ArrayList<ProductsModel>();
				checkedItems = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId,generalSubset,CommonUtility.validateString(itemPartNumber),0,"N","partkeywords");
				if(checkedItems!=null && checkedItems.size()!=0){
					if(checkedItems.size()>1){
						keyWord = itemPartNumber;
						target = "searchResult";
					}else if(checkedItems.size()==1){
						itemPriceId = checkedItems.get(0).getItemPriceId();
						partNumber = checkedItems.get(0).getPartNumber();
						productIdList = Integer.toString(checkedItems.get(0).getItemId());
						addToCart();
						target="ShoppingCart"; 
					}
				}else{
					keyWord = itemPartNumber;
					target = "searchResult";
				}
			}else{
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
				target = "success";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return target;
	}
	public String searchExistingCatalog(){
		try{
			request =ServletActionContext.getRequest();
			String webId = request.getParameter("WEBID");
			if(CommonUtility.validateString(webId).length()>0){
				keyWord = ProductsDAO.redirectCatWebId(webId);
				target = "searchResult";
			}else{
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
				target = "success";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return target;
	}
	public String PrettySearch(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			if(CommonUtility.validateString(keyWord).length()>0){
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put(PrettySearchConstantVariables.keywordKey,keyWord);
				contentObject.put(PrettySearchConstantVariables.SUBSET_KEY,CommonUtility.validateNumber(tempSubset));
				contentObject.put(PrettySearchConstantVariables.GENERAL_SUBSET_KEY,CommonUtility.validateNumber(tempGeneralSubset));
				contentObject = PrettySearchDAO.getInstance().getPrettySearchData(contentObject);
				renderContent = LayoutGenerator.templateLoader("PrettySearchResult", contentObject , null, null, null);
			}else{
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				renderContent = LayoutGenerator.templateLoader("NoDataPage", contentObject , null, null, null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String QuickAddToCart(){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String tempSubset = (String) session.getAttribute("userSubsetId");
		String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		String entityId = (String) session.getAttribute("entityId");
		String homeTeritory = (String) session.getAttribute("shipBranchId");
		int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		int subsetId = CommonUtility.validateNumber(tempSubset);
		String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
		String userToken = (String) session.getAttribute("userToken");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String wareHousecode = (String) session.getAttribute("wareHouseCode");
		String customerId = (String) session.getAttribute("customerId"); //7932
		String customerCountry = (String) session.getAttribute("customerCountry");
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		ArrayList<ProductsModel> updatedResult = new ArrayList<ProductsModel>();
		ArrayList<ProductsModel> updatedResultNew = new ArrayList<ProductsModel>();
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		boolean checkAvailability = false;
		try{
			String quickordertitems = request.getParameter("requestString");
			JSONArray jsonarray = new JSONArray(quickordertitems);
			ArrayList<ProductsModel> cartItems = new ArrayList<ProductsModel>();
			for(int i=0; i<jsonarray.length(); i++){
				ProductsModel quickCartItems= new ProductsModel();
				JSONObject obj = jsonarray.getJSONObject(i);
				if(obj.getString("partNumber")!=null && !obj.getString("QTY").isEmpty()){
					quickCartItems.setSelectedOption(obj.getInt("selectOpp"));
					quickCartItems.setQueryString(obj.getString("partNumber"));	
					quickCartItems.setPartNumber(obj.getString("partNumber"));	
					if(obj.getString("QTY")!=null && !obj.getString("QTY").isEmpty())
					{
						quickCartItems.setQty((CommonUtility.validateNumber(obj.getString("QTY"))>0)?CommonUtility.validateNumber(obj.getString("QTY")):1);
						partIdentifierQuantity.add(quickCartItems.getQty());
					}
					cartItems.add(quickCartItems);
				}
			} 
			String searchType = "";
			ArrayList<ProductsModel> checkedItems = new ArrayList<ProductsModel>();
			ArrayList<ProductsModel> checkedItemsNew = new ArrayList<ProductsModel>();
			for(ProductsModel item : cartItems){
				boolean partials = false;
				if(item.getSelectedOption()==1){
					searchType = "SEARCH-AS08";
					item.setDescription(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.partnumber").trim());
				}else if(item.getSelectedOption()==2){
					searchType = "SEARCH-AS01";
					item.setDescription(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.customerpartnumber").trim());
				}else if(item.getSelectedOption()==4){
					searchType = "SEARCH-AS07";
					partials = true;
					item.setDescription(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.mfrpartnumber").trim());
					
				}else if(item.getSelectedOption()==5){
					searchType = "SEARCH-AS10";
					item.setDescription(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.upc").trim());
					
				}
				//checkedItems = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId,generalSubset,queryKey,0,"N","upc");
				HashMap<String, ArrayList<ProductsModel>> searchResult = ProductHunterSolr.searchNavigation( ProductHunterSolr.escapeQueryCulpritsWithoutWhiteSpace(item.getQueryString()), subsetId, generalSubset, 0, 10, searchType, 0, 0, 0, 0, null, item.getQueryString(), sessionId, null, 12, null, buyingCompanyId, (String) session.getAttribute(Global.USERNAME_KEY), userToken, entityId, userId, homeTeritory, searchType, wareHousecode, customerId, customerCountry, false, partials,"","","");
				checkedItems = searchResult.get("itemList");
				boolean itemFound = true;
				if(checkedItems!=null && !checkedItems.isEmpty()){
					if(checkedItems.size()==1){
						for(ProductsModel eachItem:checkedItems){
							if(item.getSelectedOption()==1 && CommonUtility.validateString(eachItem.getPartNumber()).length()>0 && CommonUtility.validateString(eachItem.getPartNumber()).equalsIgnoreCase(item.getPartNumber())){
								eachItem.setQty(item.getQty());
							}else if(item.getSelectedOption()==4 && CommonUtility.validateString(eachItem.getManufacturerPartNumber()).length()>0 && CommonUtility.validateString(eachItem.getManufacturerPartNumber()).equalsIgnoreCase(item.getPartNumber())){
								eachItem.setQty(item.getQty());
							}else if(item.getSelectedOption()==5 && CommonUtility.validateString(eachItem.getUpc()).length()>0 && CommonUtility.validateString(eachItem.getUpc()).equalsIgnoreCase(item.getPartNumber())){
								eachItem.setQty(item.getQty());
							}else{
								itemFound = false;
							}
						}
						if(itemFound){
							checkedItemsNew.addAll(checkedItems);							
						}else{
							item.setExactMatchNotFound("Y");
						}
					}else{
						item.setExactMatchNotFound("Multiple");	
					}
				}else{
					item.setExactMatchNotFound("Y");
				}
				updatedResult.add(item);
			}
				if(checkedItemsNew!=null && !checkedItemsNew.isEmpty()){

					LinkedHashMap<String, Double> priceCheckInERP = new LinkedHashMap<String, Double>();
					LinkedHashMap<String, Integer> availCheckInERP = new LinkedHashMap<String, Integer>();

					ProductManagement priceInquiry = new ProductManagementImpl();
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					priceInquiryInput.setHomeTerritory(homeTeritory);
					priceInquiryInput.setPartIdentifier(checkedItemsNew);
					priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
					priceInquiryInput.setRequiredAvailabilty("Y");
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					ArrayList<ProductsModel> itemListPriceData = priceInquiry.priceInquiry(priceInquiryInput , checkedItemsNew);

					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("QUICK_ORDER_AVAIL_CHECK")).equalsIgnoreCase("Y")){
						checkAvailability = true;
					}
					for(ProductsModel model : itemListPriceData){
						priceCheckInERP.put(model.getPartNumber().toUpperCase(), model.getTotal());
						if(checkAvailability){
							availCheckInERP.put(model.getPartNumber(), model.getBranchTotalQty());
						}

					}

					for(ProductsModel item3 : checkedItemsNew){
						qty = CommonUtility.validateParseIntegerToString(item3.getQty());
						itemPriceId = item3.getItemPriceId();
						partNumber = item3.getPartNumber();
						productIdList = Integer.toString(item3.getItemId());


						double priceCheck = priceCheckInERP.get(CommonUtility.validateString(item3.getPartNumber()));
						int availCheck = 0;
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("QUICK_ORDER_AVAIL_CHECK")).equalsIgnoreCase("Y")){
							availCheck = availCheckInERP.get(CommonUtility.validateString(item3.getPartNumber()));
						}
						boolean hasPrice = true;
						boolean hasAvailability = true;

						if(priceCheck <= 0){
							hasPrice = false;
						}
						if(checkAvailability && availCheck < 1){
							hasAvailability = false;
						}
						if(hasPrice && hasAvailability){
							addToCart();
							item3.setExactMatchNotFound("N");
						}else{
							if(!hasPrice && item3.getQty()==0){
								item3.setExactMatchNotFound("Y");
							}else if(!hasPrice){
								item3.setExactMatchNotFound("P");
							}
							if(!hasAvailability)
								item3.setExactMatchNotFound("A");
						}
						updatedResultNew.add(item3);
					}
				}
				updatedResult.addAll(updatedResultNew);
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		contentObject.put("updatedResult", updatedResult);
		contentObject.put("responseType","AdhocQuickOrder");
		renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);

		return SUCCESS;
	}
	public String getAllUpsFreights(){
		try{
			request =ServletActionContext.getRequest();
			String tableName = request.getParameter("customParamter");
			HttpSession session = request.getSession();
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			String zipCode=(String) request.getParameter("zipCode");
			String state=(String) request.getParameter("state");
			String city=(String) request.getParameter("city");
			//String itemId=(String) request.getParameter("itemId");
			int qty = CommonUtility.validateNumber((String) request.getParameter("qty"));
			double weight=CommonUtility.validateDoubleNumber((String)request.getParameter("weight"));
			String weightUom=(String) request.getParameter("weightUom");
			LinkedHashMap<Integer,Double> upsDetailFrieghtCharges =new LinkedHashMap<Integer,Double>();
			double totalWeight = 0;
			/**
			 *Below code Written is for Tyndale *Reference- Chetan Sandesh
			 */
			UsersModel shipAddress = null;
			session.removeAttribute("shipViaFreight");
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject = ProductsDAO.getShoppingCartDao(session, contentObject);
			double totalCartFrieght= SalesDAO.getTotalCartWeight((ArrayList<ProductsModel>) contentObject.get("productListData"));
			if(CommonUtility.validateString(tableName).length()>0){

			if(session!=null && session.getAttribute("defaultShipAddress")!=null){
				shipAddress = (UsersModel) session.getAttribute("defaultShipAddress");
			}
			
			if(CommonUtility.validateString(zipCode).length()>0) {
				shipAddress.setZipCode(zipCode);
			}
			if(CommonUtility.validateString(state).length()>0) {
				shipAddress.setState(state);
			}
			if(CommonUtility.validateString(city).length()>0) {
				shipAddress.setCity(city);
			}
			LinkedHashMap<String, Double> shipViaFreight = new LinkedHashMap<String, Double>();	
			int shipViaServiceCode = 0;
			double freightRate =0;
			String shipViaId="";
			String shipViaName="";
			double thresholdWeightLimit = CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("MAXIMUM_CARTWEIGHT_FOR_SHIPPING"));
			WarehouseModel wareHouseDetail = new WarehouseModel();
			wareHouseDetail = UsersDAO.getWareHouseDetailsByCode(wareHousecode);
			List<CustomTable> shipViaTable  = CIMM2VelocityTool.getInstance().getCusomTableData("Website",tableName);
				if(shipViaTable!=null && shipViaTable.size()>0) {
				for(CustomTable eachShipVia:shipViaTable){
					for(Map<String, String> each : eachShipVia.getTableDetails()){
							shipViaId = each.get("UPS_SHIPVIA_CODE");
							shipViaName = each.get("UPS_SHIPVIA_NAME");
							shipViaServiceCode = CommonUtility.validateNumber(each.get("UPS_SERVICE_CODE"));
							
							if(totalCartFrieght <= thresholdWeightLimit){
								//totalCartFrieghtCharges = UpsCarrier.getUPSFreightCharges(totalCartFrieght,wareHouseDetail,shipAddress, shipViaServiceCode);
								freightRate = new UpsFreightService().getFreightCharges(shipAddress, wareHouseDetail, totalCartFrieght, CommonUtility.validateParseIntegerToString(shipViaServiceCode),shipViaName);
							}else{
								int multiple = (int) (totalCartFrieght / thresholdWeightLimit);
								double balanceweight = totalCartFrieght - (thresholdWeightLimit * multiple);
								freightRate = new UpsFreightService().getFreightCharges(shipAddress, wareHouseDetail, thresholdWeightLimit, CommonUtility.validateParseIntegerToString(shipViaServiceCode),shipViaName);
								//totalCartFrieghtCharges = UpsCarrier.getUPSFreightCharges(thresholdWeightLimit,wareHouseDetail, shipAddress, shipViaServiceCode) * multiple;
								if(balanceweight>0){
									freightRate += new UpsFreightService().getFreightCharges(shipAddress, wareHouseDetail, balanceweight,CommonUtility.validateParseIntegerToString(shipViaServiceCode),shipViaName);
									//totalCartFrieghtCharges = totalCartFrieghtCharges+UpsCarrier.getUPSFreightCharges(balanceweight,wareHouseDetail,shipAddress, shipViaServiceCode);
								}
							}
							
							if(freightRate>0) 
							{
								if(freightRate>0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ADDITIONAL_FREIGHT_CHARGES")).length()>0) {
									freightRate += freightRate * CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("ADDITIONAL_FREIGHT_CHARGES"));
								}
								shipViaFreight.put(shipViaId, Double.valueOf(freightRate));
							}
					}
				}
				session.setAttribute("shipViaFreight", shipViaFreight);
				contentObject.put("shipViaFreight", shipViaFreight);
				contentObject.put("responseType", "shipViaCharges");
				}
				renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
			}else {
			if(CommonUtility.validateString(zipCode).length()>0){
				shipAddress.setZipCodeStringFormat(zipCode);
	
				ProductsModel productsModel = new ProductsModel();
				productsModel.setWeight(weight);
				productsModel.setWeightUom(weightUom);
				productsModel.setQty(qty);
				ArrayList<ProductsModel> cartListData = new ArrayList<ProductsModel>();
				cartListData.add(productsModel);
				totalWeight= SalesDAO.getTotalCartWeight(cartListData);		 
				WarehouseModel wareHouseDetail = new WarehouseModel();
				wareHouseDetail = UsersDAO.getWareHouseDetailsByCode(wareHousecode);
				upsDetailFrieghtCharges = UpsCarrier.getUPSCalculatorForDetail(totalWeight,wareHouseDetail,shipAddress);
				Gson g=new Gson();
				System.out.println("ups details:"+g.toJson(upsDetailFrieghtCharges));
				result=g.toJson(upsDetailFrieghtCharges);
	
				renderContent = result;
			}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String deleteRfqCart()
	{
		 	request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = Integer.parseInt(sessionUserId);
		try
		{
			ProductsDAO.deleteFromRfqCart(userId, listId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "ResultLoader";
	}
	
	public String clearUserCartAction(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String customClearCart = request.getParameter("customDeleteCartItems");
			int userId = CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY));
			if(userId>0) {
				if(customClearCart!=null && customClearCart.length()>0) {
					ProductsDAO.clearCartCustom(userId,customClearCart);
					session.removeAttribute("shipViaFlag");
				}else {
					ProductsDAO.clearCart(userId);
				}
			}else {
				if(customClearCart!=null && customClearCart.length()>0) {
					ProductsDAO.clearCartBySessionIdCustom(userId,session.getId(),customClearCart);
					session.removeAttribute("shipViaFlag");
				}else {
					ProductsDAO.clearCartBySessionId(userId,session.getId());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String dfmCartDetails(){
		try {
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("responseType", "DFMMode");
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ResultLoader";
	}
	
	public String getProductModeFilter(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			 String productId = request.getParameter("productId");
			 String tempSubset = (String) session.getAttribute("userSubsetId");
             String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
             int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
           int subsetId = CommonUtility.validateNumber(tempSubset);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			LinkedHashMap<String, Object> selectableObject = new LinkedHashMap<String, Object>();
			int resultPerPage = 6;
			
			 if(resultPerPage==0)
			    {
			    	if(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"))>0){
			    		resultPage = CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE");
				    	resultPerPage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("RESULTS_PER_PAGE"));
			    	}else{
			    		resultPage = "12";
				    	resultPerPage = 12;
			    	}
			    }
				
			 filterList = attrFilterList; 
			 if(attrFilterList!=null && attrFilterList.trim().equalsIgnoreCase(""))
					    attrFilterList = null;
						
						if(filterList!=null && !filterList.trim().equalsIgnoreCase("")){
				    	try {
							filterList = URLEncoder.encode(filterList,"UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
						
				int noOfPage = (CommonUtility.validateNumber(pageNo)/resultPerPage)+1;

			    int fromRow =  (noOfPage-1)*resultPerPage;
				int toRow =  ((noOfPage-1)*resultPerPage) + resultPerPage;
				
				
			selectableObject = ProductHunterSolr.getSelectableItemsV2( CommonUtility.validateNumber(productId), subsetId, generalSubset, CommonUtility.validateNumber(codeId), attrFilterList, fromRow, resultPerPage);
			itemLevelFilterData = (ArrayList<ProductsModel>) selectableObject.get("itemList");
			contentObject.put("responseType", "ProductMode");
			attrFilterData = (ArrayList<ProductsModel>) selectableObject.get("attrList");
			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
				resultCount = itemLevelFilterData.get(0).getResultCount();
			}
			
			double disp;
	    	double disp1;
	    	disp = resultCount;

	    	if (resultCount > resultPerPage) {

	    		paginate = (resultCount / resultPerPage);
	    		disp1 = (disp / resultPerPage);

	    	} else {
	    		paginate = (resultCount / resultPerPage);
	    		disp1 = (resultCount / resultPerPage);
	    	}

	    	if (disp1 > paginate) {
	    		paginate = paginate + 1;

	    	}
			
	    	if(attrFilterList!=null){
    			attrFilterList = attrFilterList.replaceAll("% ", "%25 ");
    			attrFilterList = URLDecoder.decode(attrFilterList,"UTF-8");
    		    attrFilterList = attrFilterList.replaceAll("'", "%27");
    		    String paginateFilter = attrFilterList;
    		    contentObject.put("paginateFilter",paginateFilter);
    		    attrFilterList = attrFilterList.replaceAll("&", "&amp;");

    		}
	    	
			contentObject.put("itemMap", selectableObject.get("itemMap"));
			contentObject.put("selectableItemList", selectableObject.get("itemList"));
			contentObject.put("selectableList", selectableObject.get("selectableList"));
			contentObject.put("selecatableJson", selectableObject.get("selecatableJson"));
			contentObject.put("selectableMultiList", selectableObject.get("selectableMultiList"));
			contentObject.put("selecatableMap", selectableObject.get("selecatableMap"));
			contentObject.put("productAttrFilterData", attrFilterData);
			contentObject.put("codeId", codeId);
			contentObject.put("productId", productId);
		    contentObject.put("attrFilteredList", attrFilteredList);
		    contentObject.put("attrFilterList", attrFilterList);
		    contentObject.put("filterList", filterList);
			//renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
			renderContent = LayoutGenerator.templateLoader("ProductModePage", contentObject , null, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "ResultLoader";
	}
	
	public String QuickOrderPad(){
		
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String requestType = "";
		String mobRequestType = "";
		String srchTyp = request.getParameter("srchTyp");
		if(userId>1 || (request.getHeader("User-Agent")!=null && CommonUtility.validateString(request.getHeader("User-Agent")).equalsIgnoreCase("WEBVIEW"))){
			System.out.println("QuickOrderPad User Agent: "+CommonUtility.validateString(request.getHeader("User-Agent")));
			
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			String jsonStringToMobileGson = "Issue while Processing request";
			try {
				requestType = request.getParameter("reqType");
				String jsonString = request.getParameter("jsonString");
				String processType = request.getParameter("processType");
				String sessionId = session.getId();
				String entityId = (String) session.getAttribute("entityId");
				String homeTeritory = (String) session.getAttribute("shipBranchId");
				String tempSubset = (String) session.getAttribute("userSubsetId");
				String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
				String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
				String wareHousecode = (String) session.getAttribute("wareHouseCode");
				String customerId = (String) session.getAttribute("customerId");
				String customerCountry = (String) session.getAttribute("customerCountry");
				String userToken = (String) session.getAttribute("userToken");
				
				int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
				int subsetId = CommonUtility.validateNumber(tempSubset);
				int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
				
				mobRequestType = CommonUtility.validateString(requestType);
				
				LinkedHashMap<String, ArrayList<ProductsModel>> itemsWithMultipleResultMap = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
				ArrayList<ProductsModel> itemsAddedToCart = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> itemsWithRestriction = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> itemsWithZeroResult = new ArrayList<ProductsModel>();
				List<ProductsModel> existingItems = new ArrayList<>();
				HashMap<String, ArrayList<ProductsModel>> searchResult = new HashMap<String, ArrayList<ProductsModel>>();
				boolean checkAvailability = false;
				boolean addZeroPriceItem = false;
				boolean exactSearch = false;
				boolean customFieldQtyCheck = false;
				if(srchTyp == null)
			    	srchTyp = "0";
			    if(srchTyp.trim().equalsIgnoreCase("1")){
			    	requestType = "SEARCH-AS01";
			    }else  if(srchTyp.trim().equalsIgnoreCase("2")){
			    	requestType = "SEARCH-AS02";
			    }else  if(srchTyp.trim().equalsIgnoreCase("3")){
			    	requestType = "SEARCH-AS03";
			    }else  if(srchTyp.trim().equalsIgnoreCase("4")){
			    	requestType = "SEARCH-AS04";
			    }else  if(srchTyp.trim().equalsIgnoreCase("5")){
			    	requestType = "SEARCH-AS05";
			    }else  if(srchTyp.trim().equalsIgnoreCase("6")){
			    	requestType = "SEARCH-AS06";
			    }else  if(srchTyp.trim().equalsIgnoreCase("7")){
			    	requestType = "SEARCH-AS07";
			    }else  if(srchTyp.trim().equalsIgnoreCase("8")){
			    	requestType = "SEARCH-AS08";
			    }else  if(srchTyp.trim().equalsIgnoreCase("9")){
			    	requestType = "SEARCH-AS09";
			    }else  if(srchTyp.trim().equalsIgnoreCase("10")){
			    	requestType = "SEARCH-AS10";
			    }else{
			    	srchTyp = "0";
			    	requestType = "SEARCH";
			    }
				System.out.println("processType: "+processType);
				System.out.println("jsonString: "+jsonString);
				
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("QUICK_ORDER_AVAIL_CHECK")).equalsIgnoreCase("Y")){
					checkAvailability = true;
				}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("QUICK_ORDER_ADD_ZERO_PRICE_ITEM")).equalsIgnoreCase("Y")){
					addZeroPriceItem = true;
				}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_EXACT_SEARCH_IN_QUICK_ORDER")).equalsIgnoreCase("Y")){
					exactSearch = true;
				}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("QUICK_ORDER_CUSTOMFIELD_QTY_CHECK")).equalsIgnoreCase("Y")){
					customFieldQtyCheck = true;
				}
				
				LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
				utilityMap.put("considerLineItemComment", false);
				
				if(CommonUtility.validateString(jsonString).length()>0){
					Gson parseInput = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
					ProductsModel  parsedData = parseInput.fromJson(jsonString,ProductsModel.class);
					 
					ArrayList<ProductsModel> orderPadRequestList = parsedData.getItemDataList();
					if(orderPadRequestList!=null && orderPadRequestList.size()>0){
						for(ProductsModel orderPadModel : orderPadRequestList){
							String keyWord = CommonUtility.validateString(orderPadModel.getKeyword());
							int qty = 1;
							int customQty=0;
							if(orderPadModel.getQty()>0){
								qty = orderPadModel.getQty();
							}
							
							String solrSearchType = "SEARCH";
							/*if(CommonUtility.validateString(orderPadModel.getSolrSearchField()).equalsIgnoreCase("Part number")){
								solrSearchType = "partnumbersearch";
							}else if(CommonUtility.validateString(orderPadModel.getSolrSearchField()).equalsIgnoreCase("Manufacturer Part Number")){
								solrSearchType = "manfpartnumber";
							}if(CommonUtility.validateString(orderPadModel.getSolrSearchField()).equalsIgnoreCase("Description")){
								solrSearchType = "keywords";
							}if(CommonUtility.validateString(orderPadModel.getSolrSearchField()).equalsIgnoreCase("UPC")){
								solrSearchType = "upc";
							}*/
							
							boolean restrictedItemFlag = false;
							boolean priceRestrictionFlag = false;
							boolean availabilityRestrictionFlag = false;
							boolean customFiledQtyRestrictionFlag = false;
							boolean addItemsToGroupOnly =false;
							//ArrayList<Integer> itemListId = new ArrayList<Integer>();
							//ArrayList<ProductsModel> resultList = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId,generalSubset,CommonUtility.validateString(keyWord),0,"N",solrSearchType);
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_ADVANCE_SEARCH_IN_QUICK_ORDER")).equalsIgnoreCase("Y")){
							     searchResult = ProductHunterSolr.searchNavigation(CommonUtility.validateString(keyWord).toUpperCase(), subsetId, generalSubset, 0, 10, requestType, 0, 0, 0, 0, null, null, sessionId, "score|desc", 15, null, buyingCompanyId, sessionUserId, userToken, entityId, userId, homeTeritory, solrSearchType, wareHousecode, customerId, customerCountry, false, exactSearch,"","","");
							}else{
								 searchResult = ProductHunterSolr.searchNavigation(CommonUtility.validateString(keyWord).toUpperCase(), subsetId, generalSubset, 0, 10, solrSearchType, 0, 0, 0, 0, null, null, sessionId, null, 15, null, buyingCompanyId, sessionUserId, userToken, entityId, userId, homeTeritory, solrSearchType, wareHousecode, customerId, customerCountry, false, exactSearch,"","","");
							}
						
							ArrayList<ProductsModel> resultList = searchResult.get("itemList"); 
							
							ProductsModel  productDataModel = new ProductsModel();
							
							if(resultList!=null && resultList.size()>0){
								
								if(session.getAttribute("userToken")!=null && CommonUtility.validateString(session.getAttribute("userToken").toString()).length()>0 && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("defaults")){
									
									//for(ProductsModel itemResult : resultList){
										partIdentifierQuantity=new ArrayList<Integer>();
										partIdentifierQuantity.add(qty);
										//itemListId.add(itemResult.getItemId());
									//}
									if(resultList!=null)
									{
										for(ProductsModel resultqty:resultList)
										{
											resultqty.setQty(qty);
										}
									}
									ProductManagement priceInquiry = new ProductManagementImpl();
									ProductManagementModel priceInquiryInput = new ProductManagementModel();
									priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
									priceInquiryInput.setHomeTerritory(homeTeritory);
									priceInquiryInput.setPartIdentifier(resultList);
									priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
									priceInquiryInput.setRequiredAvailabilty("Y");
									priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
									priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
									priceInquiryInput.setSession(session);
									priceInquiryInput.setRequestFrom("QuickOrder");
									resultList = priceInquiry.priceInquiry(priceInquiryInput , resultList);
								}
															
								if(resultList.size()==1 && ((!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EXACT_SEARCH_IN_QUICK_ORDER_PAD")).equalsIgnoreCase("Y")) || CommonUtility.validateString(resultList.get(0).getPartNumber()).equalsIgnoreCase(CommonUtility.validateString(keyWord)) || CommonUtility.validateString(resultList.get(0).getUpc()).equalsIgnoreCase(CommonUtility.validateString(keyWord)) || CommonUtility.validateString(resultList.get(0).getManufacturerPartNumber()).equalsIgnoreCase(CommonUtility.validateString(keyWord)))){
									productDataModel = resultList.get(0);
									if(productDataModel!=null){
										
											if(!addZeroPriceItem){
												if(productDataModel.getPrice()>0){
													restrictedItemFlag = false;
												}else{
													restrictedItemFlag = true;
													priceRestrictionFlag = true;
												}
											}
											
											if(checkAvailability){
												if(productDataModel.getBranchTotalQty()<1){
													restrictedItemFlag = true;
													availabilityRestrictionFlag = true;
												}
											}
											
											if(customFieldQtyCheck) {
												LinkedHashMap<String, Object> customFieldVal = productDataModel.getCustomFieldVal();
												if(customFieldVal!=null && !customFieldVal.isEmpty()){
													if(customFieldVal!=null && !customFieldVal.isEmpty() && customFieldVal.containsKey("custom_MAX_ORDER_QTY")){
														customQty = CommonUtility.validateNumber(customFieldVal.get("custom_MAX_ORDER_QTY").toString());
														if(qty>customQty) {
															restrictedItemFlag = true;
															customFiledQtyRestrictionFlag=true;
														}
													}
													if(CommonUtility.customServiceUtility()!=null){
														restrictedItemFlag = CommonUtility.customServiceUtility().restricatingItemRetail(customFieldVal, restrictedItemFlag, session);
													}
														if(restrictedItemFlag) {
															customFiledQtyRestrictionFlag=true;
															}
												}
											}
											if(CommonUtility.validateString(processType).equalsIgnoreCase("addToGroup") && savedGroupId>0) {
												addItemsToGroupOnly = true;
												restrictedItemFlag = true;
											}
										
										if(!restrictedItemFlag){
											utilityMap.put("priceToCart", CommonUtility.validateParseDoubleToString(productDataModel.getPrice()));
											utilityMap.put("availabilityToCart", CommonUtility.validateParseIntegerToString(productDataModel.getAvailQty()));
											int minOrderQty = productDataModel.getMinOrderQty();//rs.getString("MIN_ORDER_QTY");
											int orderQtyIntr = productDataModel.getOrderInterval();
											if(qty>=minOrderQty){
												int qtyDiff = qty - minOrderQty;
												if((qtyDiff%orderQtyIntr)==0){
													boolean isAddedToCart = true;
													ArrayList<Integer> cartIdAndQty = null;
													if(CommonUtility.validateString(processType).equalsIgnoreCase("Separate")){
														cartIdAndQty = null;
													}else{
														if(userId>2) {
															cartIdAndQty = ProductsDAO.selectFromCart(userId, productDataModel.getItemId(),productDataModel.getUom());
														}else {
															cartIdAndQty = ProductsDAO.selectFromCartSession(userId, productDataModel.getItemId(), sessionId, productDataModel.getUom());
														}
													}
													if(cartIdAndQty!=null && cartIdAndQty.size()>0 && CommonUtility.validateString(processType).equalsIgnoreCase("Combine")){
														if(userId>2) {
															ProductsDAO.updateCart(userId, cartIdAndQty.get(0), qty, cartIdAndQty.get(1),"","", utilityMap);
														}else {
															ProductsDAO.updateCartBySessionId(sessionId, cartIdAndQty.get(0), qty, cartIdAndQty.get(1),"","");
														}
													}else if(cartIdAndQty!=null && cartIdAndQty.size()>0 && CommonUtility.validateString(processType).equalsIgnoreCase("Remove")){
														isAddedToCart = false;
													}
													else{
														//ProductsDAO.insertItemToCart(userId, productDataModel.getItemId(), qty, sessionId+".QUICKCART","","","",CommonUtility.validateParseDoubleToString(productDataModel.getPrice()),0, 0, 0);
														ProductsDAO.insertItemToCart(userId, productDataModel.getItemId(), qty, sessionId,"","",productDataModel.getUom(),CommonUtility.validateParseDoubleToString(productDataModel.getPrice()),0, 0, 0, utilityMap);
													}
													productDataModel.setQty(qty);
													productDataModel.setKeyword(keyWord);
													
													if(isAddedToCart) {
														productDataModel.setResultDescription("Item Added to cart");
														itemsAddedToCart.add(productDataModel);
														System.out.println("Item Added to cart: "+keyWord);
													}else {
														productDataModel.setResultDescription("Item Already Exists");
														existingItems.add(productDataModel);
													}
													
													
						    						//-- Add to cart
													
												}else{
													
													productDataModel.setKeyword(keyWord);
													productDataModel.setResultDescription("Quantity should be in multiple of "+orderQtyIntr);
													itemsWithRestriction.add(productDataModel);
													System.out.println("OrderQtyInterval is incorrect.: "+keyWord);
													
												}
											}else{
												
												productDataModel.setKeyword(keyWord);
												productDataModel.setResultDescription("Minimum order quantity is "+minOrderQty);
												itemsWithRestriction.add(productDataModel);
												System.out.println("MinOrderQty is incorrect.: "+keyWord);
												
											}
										}else if(addItemsToGroupOnly) {
											ProductsDAO.insertProductListItem(savedGroupId, savedGroupName, userId, CommonUtility.validateParseIntegerToString(productDataModel.getItemId()), qty,productDataModel.getUom());
									}else{
											
											if(priceRestrictionFlag){
												
												productDataModel.setKeyword(keyWord);
												productDataModel.setResultDescription("Cannot add Call For Price items to cart");
												itemsWithRestriction.add(productDataModel);
												System.out.println("Cannot add Call For Price items to cart: "+keyWord);
												
											}
											
											if(availabilityRestrictionFlag){
												
												productDataModel.setKeyword(keyWord);
												productDataModel.setResultDescription("Cannot add out of stock items to cart");
												itemsWithRestriction.add(productDataModel);
												System.out.println("Cannot add out of stock items to cart: "+keyWord);
												
											}
											
											if(customFiledQtyRestrictionFlag){
												productDataModel.setKeyword(keyWord);
												productDataModel.setResultDescription(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("quickorder.lable.maximumQty")) + customQty + CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("quickorder.lable.maximumQtyErrorMsg")));
												itemsWithRestriction.add(productDataModel);
												System.out.println("Cannot add out of stock items to cart: "+keyWord);
												
											}
										}
									}
								}else{
									int resultCount = 0;
									if(resultList.get(0).getItemResultCount()>0){
						 				resultCount = resultList.get(0).getItemResultCount();
						 			}else{
						 				resultCount = resultList.get(0).getResultCount();
						 			}
																	
									itemsWithMultipleResultMap.put(keyWord, resultList);
									System.out.println("Multiple Results For: "+keyWord+" : "+resultList.size()+" : "+resultCount);
								}
								
							}else{
								
								productDataModel.setKeyword(keyWord);
								productDataModel.setResultDescription("No result available");
								itemsWithZeroResult.add(productDataModel);
								System.out.println("No result available: "+keyWord);
							}
							
						}
					}
					System.out.println(parsedData);
				}
				
				contentObject.put("existingItems", existingItems);
				contentObject.put("itemsAddedToCart", itemsAddedToCart);
				contentObject.put("processType", processType);
				if(CommonUtility.customServiceUtility() != null) {
					CommonUtility.customServiceUtility().addCartDetailsToQuickOrderPad(userId, CommonUtility.mergeLists(existingItems,itemsAddedToCart), contentObject);
				}
				
				contentObject.put("itemsWithRestriction", itemsWithRestriction);
				contentObject.put("itemsWithZeroResult", itemsWithZeroResult);
				contentObject.put("itemsWithMultipleResultMap", itemsWithMultipleResultMap);
				
				if(contentObject!=null) {
					//jsonStringToMobileJson = new JSONObject(contentObject).toString();
					//System.out.println("jsonStringToMobileJson: "+CommonUtility.validateString(jsonStringToMobileJson));
					jsonStringToMobileGson = new Gson().toJson(contentObject, Map.class);
					System.out.println("jsonStringToMobileGson: "+CommonUtility.validateString(jsonStringToMobileGson));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				session.removeAttribute("cartCountSession");
			}
			if(request.getHeader("User-Agent").equalsIgnoreCase("WEBVIEW") && CommonUtility.validateString(mobRequestType).equalsIgnoreCase("OFFLINECART")) {
				renderContent = jsonStringToMobileGson;
			}else {
				renderContent = LayoutGenerator.templateLoader("QuickOrderResultPage", contentObject , null, null, null);
			}
			
			return SUCCESS;
	}
	else
	{
		renderContent = "sessionexpired";
		return SUCCESS;
	}
}
	
public String itemSuggestAutoComplete(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String queryString = request.getParameter("queryString");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String entityId = (String) session.getAttribute("entityId");
			String homeTerritory = (String) session.getAttribute("shipBranchId");
			boolean isCategoryNav = false;
			if(srchTyp!=null && srchTyp.equalsIgnoreCase("CATNAV")){
				isCategoryNav = true;
				navigationType =  "NAVIGATION";
				queryString = "CATNAV";
			}else if(srchTyp!=null && srchTyp.equalsIgnoreCase("BRAND")){
				navigationType = "SHOP_BY_BRAND"; 
			}else if(srchTyp!=null && srchTyp.equalsIgnoreCase("MANF")){
				navigationType = "SHOP_BY_MANF"; 
			}
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			HashMap<String, ArrayList<ProductsModel>> navigationResultList = ProductHunterSolr.itemAutoSuggest(subsetId, generalSubset,0, 5,navigationType,CommonUtility.validateNumber(codeId),brandId, sortBy, 5, isCategoryNav);
			itemLevelFilterData = navigationResultList.get("itemList");
			if(itemLevelFilterData!=null && itemLevelFilterData.size()>0){
				ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
				for(ProductsModel productsModel : itemLevelFilterData){
					partIdentifierQuantity.add(productsModel.getQty());
				}
				if(!CommonUtility.validateString((String)session.getAttribute("userToken")).equalsIgnoreCase("") && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("defaults")){
					ProductManagement priceInquiry = new ProductManagementImpl();
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					priceInquiryInput.setHomeTerritory(homeTerritory);
					priceInquiryInput.setPartIdentifier(itemLevelFilterData);
					priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
					priceInquiryInput.setRequiredAvailabilty("Y");
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					itemLevelFilterData = priceInquiry.priceInquiry(priceInquiryInput , itemLevelFilterData);
				}
			}
			contentObject.put("BrandIDSelected", brandId);		
			contentObject.put("responseType", "itemSuggest");
			contentObject.put("queryString", queryString);
			contentObject.put("itemLevelFilterData", itemLevelFilterData);
			contentObject.put("disableDataLoading", true);
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
			 
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return "ResultLoader";
		
	}
	
	  public String getItemPriceIdByItemId(){
	        try{
	              request =ServletActionContext.getRequest();
	              HttpSession session = request.getSession();
	              //int entityId = CommonUtility.validateNumber((String) session.getAttribute("customerId"));
	              String itemId  = request.getParameter("itemID");
	              String tempSubset = (String) session.getAttribute("userSubsetId");
	              String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	              int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
	            int subsetId = CommonUtility.validateNumber(tempSubset);
	            
	            renderContent = ProductHunterSolr.getItemPriceIdByItemId(CommonUtility.validateNumber(itemId), subsetId, generalSubset);
	        }catch (Exception e) {
	              e.printStackTrace();
	        }
	        return SUCCESS;
	  }
	  
	  public String getItemPriceIdByItemIdforsupercededItem(){
	        try{
	              request =ServletActionContext.getRequest();
	              HttpSession session = request.getSession();
	              //int entityId = CommonUtility.validateNumber((String) session.getAttribute("customerId"));
	              String itemId  = request.getParameter("itemID");
	              String tempSubset = (String) session.getAttribute("userSubsetId");
	              String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	              int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
	            int subsetId = CommonUtility.validateNumber(tempSubset);
	            
	            renderContent = ProductHunterSolr.getItemPriceIdByItemId(CommonUtility.validateNumber(itemId), subsetId, generalSubset);
	        }catch (Exception e) {
	              e.printStackTrace();
	        }
	        return SUCCESS;
	  }
	  
	  public String customerAlsoViewedItems(){
			try{
				request =ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				ArrayList<ProductsModel> customerAlsoViewedItemList = new ArrayList<ProductsModel>();
				int itemId = CommonUtility.validateNumber(request.getParameter("ItemID"));
				String tempSubset = (String) session.getAttribute("userSubsetId");
				String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
				
				int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
				int subsetId = CommonUtility.validateNumber(tempSubset);
				
				ArrayList<Integer> eachItemList = ProductsDAO.getCustomerAlsoViewedItems(itemId);
				
				if(eachItemList!=null && eachItemList.size()>0){
					customerAlsoViewedItemList = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubset, StringUtils.join(eachItemList," OR "),0,null,"itemid");
				}
				contentObject.put("customerAlsoViewedItemList",customerAlsoViewedItemList);
				contentObject.put("responseType", "customerAlsoViewed");
				renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
			}catch(Exception e){
				e.printStackTrace();
			}
			return "ResultLoader";
		}
	public String getFromProductMode() {
		return fromProductMode;
	}
	public void setFromProductMode(String fromProductMode) {
		this.fromProductMode = fromProductMode;
	}
	
	 public String documentSearch(){
		 try{
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			renderContent = LayoutGenerator.templateLoader("documentSearchPage", contentObject , null, null, null);
		 }catch(Exception e){
			e.printStackTrace();
		}
			return SUCCESS;
			
	}
	 
	 public String quickAdd() {
		 request =ServletActionContext.getRequest();
		 HttpSession session = request.getSession();
		 ValidationStatus status = new ValidationStatus();
		 Gson gson = new Gson();
		 String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		 int userId = CommonUtility.validateNumber(sessionUserId);
		 int userSubsetId = Integer.parseInt(session.getAttribute("userSubsetId").toString());
		 int generalSubsetId = Integer.parseInt(session.getAttribute("generalCatalog").toString());
		 String update =  request.getParameter("update");
		 int buyingCompanyId = Integer.parseInt(session.getAttribute("buyingCompanyId").toString());
		 
		 LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
		 utilityMap.put("considerLineItemComment", false);
		 
		 if(partNumber != null && partNumber.trim().length() > 0) {
			 ProductsModel product = ProductHunterSolr.findItemByPartNumber(partNumber, userSubsetId, generalSubsetId, buyingCompanyId);
		
			 if(product != null) {
				 ArrayList<ProductsModel> products = new ArrayList<ProductsModel>();
				 products.add(product);
				if(ProductsDAO.verifyAddToCartRestrictions(product, Integer.parseInt(qty))) {
					if(products!=null && products.size()>0 && session.getAttribute("userToken")!=null && CommonUtility.validateString(session.getAttribute("userToken").toString()).length()>0 && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("defaults")){
						ProductManagement priceInquiry = new ProductManagementImpl();
						ProductManagementModel priceInquiryInput = new ProductManagementModel();
						String entityId = (String) session.getAttribute("entityId");
						priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
						priceInquiryInput.setPartIdentifier(products);
						priceInquiryInput.setRequiredAvailabilty("Y");
						priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
						priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
						priceInquiryInput.setSession(session);
						productListData = priceInquiry.priceInquiry(priceInquiryInput , products);
						double total = productListData.get(0).getCartTotal();
					} 
					if(productListData.get(0).getPrice() > 0) {
						int count = 0;
						boolean isUpdate = false;
						 ArrayList<Integer> cartId = new ArrayList<Integer>();
							if(userId>1){
								cartId = ProductsDAO.selectFromCart(userId, product.getItemId(),product.getUom());
							}else{
								cartId = ProductsDAO.selectFromCartSession(userId, product.getItemId(), session.getId(),product.getUom());
							}
							
							if(cartId != null && cartId.size() > 0){
								if(update != null && update.equals("Y")){
									isUpdate = true;
									count = ProductsDAO.updateCart(userId,  cartId.get(0), Integer.parseInt(qty), cartId.get(1),"","", utilityMap);
								}else {
									count = ProductsDAO.insertItemToCart(userId, product.getItemId(), Integer.parseInt(qty), session.getId(),null, "",product.getUom(),String.valueOf(product.getPrice()),product.getMinOrderQty(), product.getWeight(), 0, utilityMap);
								}
							}else{
								count = ProductsDAO.insertItemToCart(userId, product.getItemId(), Integer.parseInt(qty), session.getId(),null,"",product.getUom(),String.valueOf(product.getPrice()),product.getMinOrderQty(), product.getWeight(), 0, utilityMap);
							}
						 if(count > 0) {
							 status.setValid(true);
							 if(isUpdate) {
								 status.setDescriptions(Arrays.asList(String.format(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("cart.quickadd.update"), partNumber)));
							 }else {
									 status.setDescriptions(Arrays.asList(String.format(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("cart.quickadd.success"), partNumber))); 
							 } 
						 }else {
							 status.setValid(false);
							 status.setDescriptions(Arrays.asList(String.format(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("cart.quickadd.failure"), partNumber)));
						 }
					}
					else {
						 status.setValid(false);
						 status.setDescriptions(Arrays.asList(String.format(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("cart.quickadd.item.with.noprice"), partNumber)));
					}
				}else {
					status.setValid(false);
					status.setDescriptions(Arrays.asList(String.format(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("cart.quickadd.invalid.interval"), partNumber, product.getMinOrderQty(), product.getOrderInterval())));
				}
			 }else {
					 status.setValid(false);
					 status.setDescriptions(Arrays.asList(String.format(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("cart.quickadd.item.notfound"), partNumber))); 
				 }

		 }
		 renderContent = gson.toJson(status);
		 return SUCCESS;
	 }
	 public String removeItems() {
		 request =ServletActionContext.getRequest();
		 HttpSession session = request.getSession();
		 Gson gson = new Gson();
		 ValidationStatus status = new ValidationStatus();
		 String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		 int userId = CommonUtility.validateNumber(sessionUserId);
		 List<Integer> cartItemIds = new ArrayList<>();
		 JsonArray items = gson.fromJson(new JsonParser().parse(request.getParameter("items")), JsonArray.class);
		 for(JsonElement item : items) {
			 JsonObject itemAsObj = item.getAsJsonObject();
			 cartItemIds.add(Integer.parseInt(itemAsObj.get("cartItemId").getAsString()));
		 }
		 if(cartItemIds.size() > 0) {
			 ProductsDAO.removeCartItems(cartItemIds, userId, session.getId());
			 status.setValid(true);
			 status.setDescriptions(Arrays.asList(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("cart.removeall.success")));
		 }else {
			 status.setValid(false);
			 status.setDescriptions(Arrays.asList(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("cart.removeall.noitems")));
		 }
		 renderContent = gson.toJson(status).toString();
		 return SUCCESS;
	 }
	 
	 public String updateCartWithShipVia(){
		 request =ServletActionContext.getRequest();
		 HttpSession session = request.getSession();
		 try{
		 String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		 int userId = CommonUtility.validateNumber(sessionUserId);
		 String shipToMeShipVia = "";
		 if(CommonUtility.validateString(request.getParameter("shipToMeShipVia")).length()>0) {
			 shipToMeShipVia =request.getParameter("shipToMeShipVia");
		 }
		 
		 String cartId = CommonUtility.validateString(request.getParameter("cartId"));
		 String itemIds = CommonUtility.validateString(request.getParameter("itemIds"));
		 ProductsDAO.updateCartWithShipVia(userId,shipToMeShipVia,cartId,itemIds);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 renderContent = "";
		 return SUCCESS;
	 }
	 public String staticFormHandler(){
		 request =ServletActionContext.getRequest();
		 HttpSession session = request.getSession();
		 Map<String, Object> formParameters = request.getParameterMap();
		 LinkedHashMap<String, String> formData = new LinkedHashMap<>();
		 Enumeration<String> parameterNames = request.getParameterNames();

		 while (parameterNames.hasMoreElements()) {
			 String paramName = parameterNames.nextElement();
			 String[] paramValues = request.getParameterValues(paramName);
			 for (int i = 0; i < paramValues.length; i++) {
				 String paramValue = paramValues[i];
				 if(CommonUtility.validateString(paramName).equalsIgnoreCase("textarea")){
					 paramName = "Comments";
				 }
				 formData.put(paramName, paramValue);
				 //System.out.println("Parameter Name : " + paramName + ", ParameterValue : " + paramValue);
			 }
		 }
		 
		 String uploadedFileNames = formData.get("uploadedFileNames");
		 if(request.getParameterValues("signature")!=null){
			 if(CommonUtility.customServiceUtility() != null) {
					uploadedFileNames = CommonUtility.customServiceUtility().convertDigitalSignature(session,request.getParameterValues("signature"),uploadedFileNames);
					formData.remove("uploadedFileNames");
				     formData.put("uploadedFileNames",uploadedFileNames);
			 }
		 }
		 
		 String formName="";
		SaveCustomFormDetails saveForm = new SaveCustomFormDetails();
		 if(request.getParameter("formName")!=null && !request.getParameter("formName").trim().equalsIgnoreCase("")){
			 formName=request.getParameter("formName");
		 }else{
			 formName=request.getParameter("mailSubject");
		 }
		 saveForm.saveToDataBase(formData,formName);
		 boolean staticPageNotification = SendMailUtility.staticPageNotification(formData, session);
		 if(staticPageNotification){
			 renderContent = "1|"+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("staticpage.form.submission.success");
		 }
		 else{
			 renderContent =  "0|"+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("staticpage.form.submission.failure");
		 }

		 return SUCCESS;
	 }
	 public String getFeatureProductsByCategory(){
		response = ServletActionContext.getResponse();
		response.setContentType("application/json");
	    try{
	    	ArrayList<ProductsModel> itemDataList = new ArrayList<ProductsModel>();
	    	ProductManagementModel productManagement= new ProductManagementModel();
	    	ProductManagement priceInquiry = new ProductManagementImpl();
	    	ArrayList<ProductsModel> resultWithoutDuplicate = new ArrayList<ProductsModel>();
	        ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
	        LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	        Set<Integer> itemSet = new HashSet<Integer>();
	        Gson gson = new Gson();
	        request = ServletActionContext.getRequest();
	        HttpSession session = request.getSession();
	        //int entityId = CommonUtility.validateNumber((String) session.getAttribute("customerId"));
	        String categoryId  = request.getParameter("categoryId");
	        String tempSubset = (String) session.getAttribute("userSubsetId");
	        String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	        int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
	        int subsetId = CommonUtility.validateNumber(tempSubset);
	        String catNameForSession = "category_"+categoryId;
	        ProductsModel featureItemList = ProductHunterSolr.getFeatureProductByCategory(CommonUtility.validateNumber(categoryId), subsetId, generalSubset);
	        itemDataList = featureItemList.getItemDataList();
	        if(itemDataList!=null) {
	            for( ProductsModel itemModel : itemDataList ) {
	            	if(itemSet.add(itemModel.getItemId())) {
	            		resultWithoutDuplicate.add(itemModel);
	            		partIdentifierQuantity.add(itemModel.getQty());
	            	}
	            }
	            productManagement.setPartIdentifierQuantity(partIdentifierQuantity);
				productManagement.setPartIdentifier(itemDataList);
				itemDataList= priceInquiry.priceInquiry(productManagement, itemDataList);
				contentObject.put("itemDataList",itemDataList);
				contentObject.put("responseType", "categoryFeaturedProducts");
	            session.setAttribute(catNameForSession, itemDataList);
	            //renderContent = gson.toJson(featureItemList.getItemDataList());
	            renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
	        }
	    }catch (Exception e) {
	          e.printStackTrace();// TODO: handle exception
	    }
	    return SUCCESS;
	  }
	 	public String deleteSelectedItemFromCart() {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			try {
				if (deleteId != null && deleteId.length() > 0) {
					if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT"))
							.length() > 0
							&& CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT").trim()
									.equalsIgnoreCase("Y")) {
						 ProductsDAO.resetCartCheckoutType(userId);
						ProductsDAO.inactiveItemInCartCheckout(userId, deleteId);
						renderContent = "INACTIVECART";
						session.setAttribute("displayCheckoutCondMsgFlag", "N");
					} else {
						ProductsDAO.deleteSelectedItemFromCart(userId, deleteId);
						renderContent = "SUCCESS";
					}
					session.setAttribute("vendorSpecificPurchaseMsg", "");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return SUCCESS;
		}
	 public String checkItemAvailabilityInDB(){
		   request =ServletActionContext.getRequest();
		   HttpSession session = request.getSession();
		   renderContent = "1|success";
		   try {
		    String tempSubset = (String) session.getAttribute("userSubsetId");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		    String reorderCartMessage = "";
		    if(quotePartNumberSelected!=null && quotePartNumberSelected.length > 0){
		              for(String qPno:quotePartNumberSelected){
		      String partNumber = request.getParameter("partNumber_"+qPno);  
		      ArrayList<ProductsModel> existingItems = ProductHunterSolr.getItemDetailsForGivenPartNumbers(CommonUtility.validateNumber(tempSubset),CommonUtility.validateNumber(tempGeneralSubset), CommonUtility.validateString(partNumber),0,null,"partnumber");
		      if(existingItems==null || existingItems.size() == 0){
		       if(CommonUtility.validateString(partNumber).length()>0){
		        reorderCartMessage = reorderCartMessage + "<b>" +partNumber + "</b><br/>";
		       }
		      }
		     }
		           String propertyMessage = CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("reoder.cart.message.itemnotfound"));
		     propertyMessage = propertyMessage.replaceAll("#", reorderCartMessage);
		           renderContent = "0|" + propertyMessage;
		    }
		   } catch (Exception e) {
		    e.printStackTrace();
		   }
		   
		   return SUCCESS;
		  }
	 
	 public String AddToCartNonCatalogItem(){
		 request =ServletActionContext.getRequest();
		 HttpSession session = request.getSession();
		 try{							
			String partNumber = request.getParameter("Sku");
			String price = request.getParameter("MSRP");
			String desc = request.getParameter("Description");
			String qty = request.getParameter("QtyNew");
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			UsersModel userData = null;
			ProductsModel items= new ProductsModel();
			items.setBrandName(brand);
			items.setPartNumber(partNumber);
			items.setPrice(Double.parseDouble(price));
			items.setDescription(desc);
			items.setManufacturerName(brand);
			items.setQty(CommonUtility.validateNumber(qty));
			userData = new UsersModel();
			userData.setUserId(CommonUtility.validateNumber(sessionUserId));
			userData.setSession(session);
			int count = ProductsDAO.insertNonCatalogItemToCart(items,userData);
			if(count>0){
				session.removeAttribute("cartCountSession");
				VelocityContext contextTemp = null; 
				getCartCount( contextTemp, request);
				renderContent = ""+validateResponse;
			}else{
				renderContent = "0";
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			session.removeAttribute("cartCountSession");
		}
		return SUCCESS;
	}
	 
	 public String cspConfigurator() {
			request =ServletActionContext.getRequest();
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
	        LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	        try{
	        	String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
	        	String tempSubset = (String) session.getAttribute("userSubsetId");
			    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			    Gson gson = new Gson();
			    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
				int subsetId = CommonUtility.validateNumber(tempSubset);
				int userId = CommonUtility.validateNumber(sessionUserId);
				String homeTerritory = (String) session.getAttribute("shipBranchId");
				String entityId = (String) session.getAttribute("entityId");
				CspProductModel cspProductDetails = new CspProductModel();
				ArrayList<ProductsModel> itemDetailObject = new ArrayList<ProductsModel>();
	        	String productIdString = request.getParameter("pid");
	        	String itemIdString = request.getParameter("iid");
	        	String itemPriceIdString = request.getParameter("ipid");
	        	String manufacturerPartNumber = request.getParameter("manufacturerPartNumber");
	        	String productFamily = request.getParameter("productFamily");
	        	if(CommonUtility.validateNumber(productIdString)>0) {
	        		LinkedHashMap<String, Object> selectableObject = new LinkedHashMap<String, Object>();
		 			int treeId = 0;
		 			ProductsModel taxonomyDetail = ProductHunterSolr.getTaxonomyDetail(CommonUtility.validateNumber(itemIdString), subsetId, generalSubset);
		 			if(taxonomyDetail!=null){
						treeId = CommonUtility.validateNumber(taxonomyDetail.getCategoryCode());
						selectableObject = ProductHunterSolr.getSelectableItemsV2(CommonUtility.validateNumber(productIdString), subsetId, generalSubset, treeId, attrFilterList, 0, 6);
						attrFilterData = (ArrayList<ProductsModel>) selectableObject.get("attrList");
						contentObject.put("itemMap", selectableObject.get("itemMap"));
						contentObject.put("selectableItemList", selectableObject.get("itemList"));
						contentObject.put("selectableList", selectableObject.get("selectableList"));
						contentObject.put("selecatableJson", selectableObject.get("selecatableJson"));
						contentObject.put("selectableMultiList", selectableObject.get("selectableMultiList"));
						contentObject.put("selecatableMap", selectableObject.get("selecatableMap"));
						itemDetailObject = (ArrayList<ProductsModel>) selectableObject.get("itemList");
						if(itemDetailObject!=null && itemDetailObject.size() > 0) {
							ProductManagement priceInquiry = new ProductManagementImpl();
							ProductManagementModel priceInquiryInput = new ProductManagementModel();
							priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
							priceInquiryInput.setHomeTerritory(homeTerritory);
							priceInquiryInput.setPartIdentifier(itemDetailObject);
							priceInquiryInput.setRequiredAvailabilty("Y");
							priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
							priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
							priceInquiryInput.setSession(session);
							itemDetailObject = priceInquiry.priceInquiry(priceInquiryInput , itemDetailObject);
							ArrayList<CspPricingModel> price = new ArrayList<CspPricingModel>();
							for(ProductsModel item :itemDetailObject) {
								cspProductDetails.setUserId(sessionUserId);
								if(CommonUtility.validateNumber(itemIdString) == item.getItemId()) {
									cspProductDetails.setPackQuantity(item.getMinOrderQty());
								}
								cspProductDetails.setSku(manufacturerPartNumber);
								cspProductDetails.setDecimalPrecision(2);
								cspProductDetails.setProductFamily(productFamily);
								if(item.getPrice() > 0.0) {
									CspPricingModel pricing = new CspPricingModel();
									pricing.setSku(item.getManufacturerPartNumber());
									ArrayList<CspPriceTable> priceTable = new ArrayList<CspPriceTable>();
									if(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")!=null && CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK").trim().equalsIgnoreCase("Y") && item.getQuantityBreakList() !=null && item.getQuantityBreakList().size()>0) {
										for(ProductsModel quntityBreak : item.getQuantityBreakList()) {
											CspPriceTable pricetable = new CspPriceTable();
											pricetable.setQty(CommonUtility.validateInteger(quntityBreak.getMinimumQuantityBreak()));
											pricetable.setUnitPrice(quntityBreak.getCustomerPriceBreak());
											priceTable.add(pricetable);
										}
									}else {
										CspPriceTable pricetable = new CspPriceTable();
										pricetable.setQty(item.getUomQty());
										pricetable.setUnitPrice(item.getPrice());
										priceTable.add(pricetable);
									}
									pricing.setPricingTable(priceTable);
									price.add(pricing);
								}
							}
							cspProductDetails.setPricing(price);
						}
					}
	        	}else if(CommonUtility.validateNumber(itemPriceIdString)>0) {

					ProductsModel productsModel = new ProductsModel(); 
					ArrayList<ProductsModel> productsInput = new ArrayList<ProductsModel>();
					ProductsModel eachProduct = new ProductsModel();
					eachProduct.setPartNumber(partNumber);
					productsInput.add(eachProduct);
					productsModel.setPartNumber(partNumber);
					itemDetailObject.add(productsModel);
					ProductManagement priceInquiry = new ProductManagementImpl();
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					priceInquiryInput.setHomeTerritory(homeTerritory);
					priceInquiryInput.setPartIdentifier(productsInput);
					priceInquiryInput.setRequiredAvailabilty("Y");
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					itemDetailObject = priceInquiry.priceInquiry(priceInquiryInput , itemDetailObject);
					if(itemDetailObject != null && itemDetailObject.size() > 0) {
						ProductsModel item = itemDetailObject.get(0);
						cspProductDetails.setUserId(sessionUserId);
						cspProductDetails.setPackQuantity(item.getMinOrderQty());
						cspProductDetails.setSku(manufacturerPartNumber);
						cspProductDetails.setDecimalPrecision(2);
						cspProductDetails.setProductFamily(productFamily);
						CspPricingModel pricing = new CspPricingModel();
						pricing.setSku(manufacturerPartNumber);
						ArrayList<CspPriceTable> priceTable = new ArrayList<CspPriceTable>();
						if(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")!=null && CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK").trim().equalsIgnoreCase("Y") && item.getQuantityBreakList() !=null && item.getQuantityBreakList().size()>0) {
							for(ProductsModel quntityBreak : item.getQuantityBreakList()) {
								CspPriceTable pricetable = new CspPriceTable();
								pricetable.setQty(CommonUtility.validateInteger(quntityBreak.getMinimumQuantityBreak()));
								pricetable.setUnitPrice(quntityBreak.getCustomerPriceBreak());
								priceTable.add(pricetable);
							}
						}else {
							CspPriceTable pricetable = new CspPriceTable();
							pricetable.setQty(item.getUomQty());
							pricetable.setUnitPrice(item.getPrice());
							priceTable.add(pricetable);
						}
						pricing.setPricingTable(priceTable);
						ArrayList<CspPricingModel> price = new ArrayList<CspPricingModel>();
						price.add(pricing);
						cspProductDetails.setPricing(price);
					}
	        	}
	        	
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCSPConfigurator")).equalsIgnoreCase("Y")) {
					contentObject.put("jsonProductData",  gson.toJson(cspProductDetails));
					contentObject.put("productData",  cspProductDetails);
					renderContent = LayoutGenerator.templateLoader("cspConfiguratorPage", contentObject, null, null, null);
					target = SUCCESS;
				}else {
					target = "SESSIONEXPIRED";
				}
	        }catch (Exception e) {
				target = "SESSIONEXPIRED";
				e.printStackTrace();
			}
			return target;
	 }
	 
	 public String logoCustomization() {
			request =ServletActionContext.getRequest();
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
	        String designId = request.getParameter("designId");
	        LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
	        com.erp.service.cimmesb.action.ProductManagementAction cspInquiry = new com.erp.service.cimmesb.action.ProductManagementAction();
	        Gson gson = new Gson();
	        
	        String tempSubset = (String) session.getAttribute("userSubsetId");
		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String entityId = (String) session.getAttribute("entityId");
	        try{
	        	String seesionBuyingComapnyId = (String)session.getAttribute("buyingCompanyId");
				int buyingCompanyId = CommonUtility.validateNumber(seesionBuyingComapnyId);
				String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
				int userId = CommonUtility.validateNumber(sessionUserId);
				if(!CommonUtility.validateString(designId).equalsIgnoreCase("")) {
					boolean flag = ProductsDAO.checkDesignExits(CommonUtility.validateNumber(designId));
					if(!flag) {
						List<LogoCustomizationModule> designDetails = cspInquiry.getLogoCustomizationDetails(CommonUtility.validateNumber(designId),session);
						if(designDetails != null && designDetails.size() > 0) {
							for(LogoCustomizationModule details : designDetails) {
								String detailsstr = gson.toJson(details);
								int count = ProductsDAO.insertDesignDetails(buyingCompanyId, userId, details.getStatus(), CommonUtility.validateNumber(designId), detailsstr);
							}
						}
					}
				}
				
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCSPConfigurator")).equalsIgnoreCase("Y")) {
					String homeTerritory = (String) session.getAttribute("shipBranchId");
					List<LogoCustomizationModule> designList = ProductsDAO.getDesignDetails(buyingCompanyId, userId);
					for(LogoCustomizationModule design : designList) {
						if(design.getStatus() != 0) {
							List<LogoCustomizationModule> updatedDesignList = cspInquiry.getLogoCustomizationDetails(design.getDesignId(),session);
							if(updatedDesignList != null && updatedDesignList.size() > 0 && updatedDesignList.get(0).getStatus() != design.getStatus()) {
								String detailsstr = gson.toJson(updatedDesignList.get(0));
								int count = ProductsDAO.updateDesignStatus(design.getLogoCustomizationId(), updatedDesignList.get(0).getStatus(), detailsstr);
								design.setStatus(updatedDesignList.get(0).getStatus());
							}
						}
						ArrayList<ProductsModel> itemList = design.getLineItems();
						String buildPartNumberString = "";
						String delimit = "";
						ArrayList<ProductsModel> itemDetalsFromSOLR = new ArrayList<ProductsModel>();
						for(ProductsModel item : itemList) {
							HashMap<String, ArrayList<ProductsModel>> itemDetals = ProductHunterSolr.searchNavigation(CommonUtility.validateString(item.getPartNumber()), subsetId, generalSubset, 0, 1, "SEARCH-AS03", varPsid, 0, 0, CommonUtility.validateNumber("0"),attrFilterList,brandId,session.getId(),sortBy,1,narrowKeyword,buyingCompanyId,(String)session.getAttribute(Global.USERNAME_KEY),(String) session.getAttribute("userToken"),entityId,userId,homeTeritory,"","", "","",false,true,viewFrequentlyPurcahsedOnly,clearanceFlag,wareHouseItems);
							ArrayList<ProductsModel> itemDetail = itemDetals.get("itemList");
							if(itemDetail != null && itemDetail.size() > 0) {
								itemDetalsFromSOLR.add(itemDetail.get(0));
							}
						}
						
						if(itemList!=null && itemDetalsFromSOLR.size()>0){
							for(ProductsModel itmListSolr : itemDetalsFromSOLR){
								for(ProductsModel item : itemList){
									if(CommonUtility.validateString(item.getPartNumber()).equalsIgnoreCase(CommonUtility.validateString(itmListSolr.getManufacturerPartNumber()))){
										itmListSolr.setImageName(item.getImageName());
										itmListSolr.setImageType("http");
										itmListSolr.setDesignId(item.getDesignId());
										itmListSolr.setCalculateTax(item.isCalculateTax());
										itmListSolr.setRushFlag(item.getRushFlag());
										itmListSolr.setQty(item.getQty());
									}
								}
							}
						}
						if(itemDetalsFromSOLR!=null && itemDetalsFromSOLR.size()>0 && session.getAttribute("userToken")!=null && CommonUtility.validateString(session.getAttribute("userToken").toString()).length()>0 && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("defaults")){
							ProductManagement priceInquiry = new ProductManagementImpl();
							ProductManagementModel priceInquiryInput = new ProductManagementModel();
							ArrayList<ProductsModel> productsInput = itemDetalsFromSOLR;
							priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
							priceInquiryInput.setHomeTerritory(homeTerritory);
							priceInquiryInput.setPartIdentifier(productsInput);
							priceInquiryInput.setRequiredAvailabilty("Y");
							priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
							priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
							priceInquiryInput.setSession(session);
							itemDetalsFromSOLR = priceInquiry.priceInquiry(priceInquiryInput , itemDetalsFromSOLR);
							//total = productListData.get(0).getCartTotal();
						}
						design.setLineItems(itemDetalsFromSOLR);
						
					}
					contentObject.put("designId", designId);
					contentObject.put("customizationList",  designList);
					renderContent = LayoutGenerator.templateLoader("customizationListPage", contentObject, null, null, null);
					target = SUCCESS;
				}else {
					target = "SESSIONEXPIRED";
				}
	        }catch (Exception e) {
				target = "SESSIONEXPIRED";
				e.printStackTrace();
			}
			return target;
	 }
	 
	 public String checkDuplicateDesign() {
		 request =ServletActionContext.getRequest();
		 HttpSession session = request.getSession();
		 try{							
			String designId = request.getParameter("designId");
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			boolean flag = ProductsDAO.selectFromCartCsp(CommonUtility.validateNumber(sessionUserId),designId);
			if(flag) {
				renderContent = "1";
			}else {
				renderContent = "0";
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			session.removeAttribute("cartCountSession");
		}
		return SUCCESS;
	 }

	 public String getPageName() {
			return pageName;
		}
		public void setPageName(String pageName) {
			this.pageName = pageName;
		}
	
	public String getItemHistoryDetails() throws JsonProcessingException{
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		ProductsModel productParameters = new ProductsModel();
		productParameters.setPartNumber(CommonUtility.validateString(request.getParameter("partNumber")));
		productParameters.setFromMonth(CommonUtility.validateString(request.getParameter("fromMonth")));
		productParameters.setFromYear(CommonUtility.validateString(request.getParameter("fromYear")));
		productParameters.setToMonth(CommonUtility.validateString(request.getParameter("toMonth")));
		productParameters.setToYear(CommonUtility.validateString(request.getParameter("toYear")));
		//SalesModel salesInputParameter = new SalesModel();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		ProductManagement productObj = new ProductManagementImpl();
		productParameters.setSession(session);
		contentObject = productObj.getItemHistoryDetails(productParameters);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(contentObject.get("response"));
		System.out.println(json);
		if(contentObject!=null) {
			target="success";
		}
		renderContent = json;
		return target;
	}
	
	public String getPromotedProductGroups(){
		 request =ServletActionContext.getRequest();
		 HttpSession session = request.getSession();
		 String buyingCompanyId = (String)session.getAttribute("buyingCompanyId");
		 String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		 int userId = CommonUtility.validateNumber(sessionUserId);
		 try{
			 if(userId>1){
				 LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				 contentObject = ProductsDAO.getPromotedProductGroupNameDAO(userId, contentObject, CommonUtility.validateNumber(buyingCompanyId));
				 if(reqType!=null && reqType.trim().equalsIgnoreCase("GP")) {
					 contentObject.put("responseType", "PromotedProductGroupList");
				 }
				 contentObject.put("reqType", reqType);
				 renderContent = LayoutGenerator.templateLoader("PromotedProductGroupsPage", contentObject , null, null, null);
				 target = SUCCESS;				 
			 }else{
				 target = "SESSIONEXPIRED";
			 }
		 }catch (Exception e) {
			 e.printStackTrace();
		 }
		 return target;
	 }
	
	public static void AbandonedCart() {
		try {
			SchedulerNotification schedulerNotification = new SchedulerNotification();
			schedulerNotification.execute();
		}catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	
	public String supplierDetails(){
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();		 
			String tempItemId = request.getParameter("itemId");
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int buyingCompanyId = CommonUtility.validateNumber(CommonUtility.validateString((String) session.getAttribute("buyingCompanyId")));
			int itemId = CommonUtility.validateNumber(tempItemId);			
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			ArrayList<SupplierModel> supplierDetails = SupplierDAO.getSupplierDetails(itemId, subsetId);
			contentObject.put("supplierDetails", supplierDetails);
			contentObject.put("responseType", "supplierDetails");
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "ResultLoader";
	}
	public String addNonCatalogToRfqCart(){
		target = "ResultLoader";
		try{
		    request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String ipaddress = request.getHeader("X-Forwarded-For");
			if (ipaddress  == null) {
				ipaddress = request.getRemoteAddr();
			}
			ProductsModel itemDetails =null;
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			String sessionId = session.getId();
			String tempQty = request.getParameter("qty");
			int userId = Integer.parseInt(sessionUserId);
			int qty = Integer.parseInt(tempQty);
			String desc = request.getParameter("desc");
			String part = request.getParameter("part");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubsetId = CommonUtility.validateNumber(tempGeneralSubset);
			String part1 = CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_PARTNUMBER");
			itemDetails = ProductsDAO.checkItemExistInCIMM(part1,tempGeneralSubset);
			int count = 0;
			count = CommonUtility.customServiceUtility().addNonCatalogToRfqCart(sessionId, userId, qty, desc, part, itemDetails);
			UsersDAO.updateUserLog(Integer.parseInt((String)session.getAttribute(Global.USERID_KEY)), "Add To Rfq", session.getId(), ipaddress, "Click", productIdList);
			if(count>0){
		    renderContent = "1";
		    }
			
		}catch (Exception e) {
	        e.printStackTrace();
		}
		return target;
	}
		
	public String associateItems() {
		try{
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			AssociateItemsModel supersedeItemsModel = null;
			AssociateItemsModel substituteItemsModel = null;
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<>();			
		    String wareHouseCode = (String) session.getAttribute("wareHouseCode");
		    partNumber = request.getParameter("partNumber");
		    String getFromPartNumber = CommonUtility.validateString(request.getParameter("getFromPartNumber"));
		    String customerErpId = (String) session.getAttribute("customerId");
		    List<ProductsModel> supersededItemsFromSolr = new ArrayList<>();
            List<ProductsModel> substituteItemsFromSolr = new ArrayList<>();
            List<ProductsModel> supersededItemDetails = null;
            List<ProductsModel> substituteItemDetails = null;
		    String tempSubset = (String) session.getAttribute("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			String searchBySolrFieldName = "partnumber";
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ASSOCIATE_ITEM_SEARCH_FIELD")).length()>0){
				searchBySolrFieldName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ASSOCIATE_ITEM_SEARCH_FIELD"));
			}
			logger.info("Item part number : {} ", partNumber);
			logger.info("customer Id : {} ", customerErpId);
			logger.info("warehouse code : {} ", wareHouseCode);
			ProductManagement associateItems = new ProductManagementImpl();
			ProductManagementModel associateInputItems = new ProductManagementModel();
			associateInputItems.setWareHouse(wareHouseCode);
			associateInputItems.setProductNumber(partNumber);
			associateInputItems.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
			associateInputItems.setCustomerId(customerErpId);
			if(!CommonUtility.validateString(partNumber).isEmpty() && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SUPERSEDE_ITEMS")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SUPERSEDE_ITEM_IMPLEMENTATION_TYPE")).equalsIgnoreCase("API") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SUPERSEDE_ITEMS_RECORD_TYPE")).length() > 0) {
				associateInputItems.setRecordType(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SUPERSEDE_ITEMS_RECORD_TYPE")));				
				supersedeItemsModel = associateItems.associateItems(associateInputItems);
				if(supersedeItemsModel.getLineItems() != null && !supersedeItemsModel.getLineItems().isEmpty()) {
					logger.info("superseded line Items size{} ",supersedeItemsModel.getLineItems().size());
					for(LineItemsModel lineItem : supersedeItemsModel.getLineItems()){						
						String customerPartNumber = CommonUtility.validateString(lineItem.getCustomerPartNumber());
						String itemPartNumber = "\""+customerPartNumber+"\"";						
						if(getFromPartNumber.equalsIgnoreCase("Y")){
							String partNumber = CommonUtility.validateString(lineItem.getPartNumber());
							itemPartNumber = "\""+partNumber+"\"";
						}
						supersededItemDetails = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId, generalSubset, itemPartNumber, 0, null, searchBySolrFieldName);
                        if(supersededItemDetails != null && supersededItemDetails.size() > 0) {
                            supersededItemsFromSolr.addAll(supersededItemDetails);
                        }
					}
				}
			}
			if(!CommonUtility.validateString(partNumber).isEmpty() && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SUBSTITUTE_ITEMS")).equalsIgnoreCase("Y") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SUBSTITUTE_ITEM_IMPLEMENTATION_TYPE")).equalsIgnoreCase("API") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SUBSTITUTE_ITEMS_RECORD_TYPE")).length() > 0) {
				associateInputItems.setRecordType(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SUBSTITUTE_ITEMS_RECORD_TYPE")));
				substituteItemsModel = associateItems.associateItems(associateInputItems);
				if(substituteItemsModel.getLineItems() != null && !substituteItemsModel.getLineItems().isEmpty()) {
					logger.info("substitute line Items size{} ",substituteItemsModel.getLineItems().size());
					for(LineItemsModel lineItem : substituteItemsModel.getLineItems()){
						String customerPartNumber = CommonUtility.validateString(lineItem.getCustomerPartNumber());
						String itemPartNumber = "\""+customerPartNumber+"\"";						
						if(getFromPartNumber.equalsIgnoreCase("Y")) {
						String partNumber = CommonUtility.validateString(lineItem.getPartNumber());
						itemPartNumber = "\""+partNumber+"\"";
						}
						substituteItemDetails = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId, generalSubset, itemPartNumber, 0, null, searchBySolrFieldName);
						if(substituteItemDetails !=null && substituteItemDetails.size() > 0) {
                            substituteItemsFromSolr.addAll(substituteItemDetails);
                        }
					}
				}
			}
			contentObject.put("responseType", "associateItems");
			contentObject.put("supersedeItems", supersedeItemsModel);
			contentObject.put("substituteItems", substituteItemsModel);
			contentObject.put("supersededItemsFromSolr", supersededItemsFromSolr);
			contentObject.put("substituteItemsFromSolr", substituteItemsFromSolr);
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
		}catch(Exception e){
			logger.error(e.getMessage());
		}		
		return "ResultLoader";
	}
	public String myItemsList(){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String buyingCompanyId = CommonUtility.validateString((String)session.getAttribute("buyingCompanyId"));
		String sessionUserId = CommonUtility.validateString((String) session.getAttribute(Global.USERID_KEY));
		String tempSubset = CommonUtility.validateString((String) session.getAttribute("userSubsetId"));
		String tempGeneralSubset = CommonUtility.validateString((String) session.getAttribute("generalCatalog"));
		String entityId = CommonUtility.validateString((String) session.getAttribute("entityId"));
		String requestType = CommonUtility.validateString(request.getParameter("requestType"));
		
		if(entityId.isEmpty() || entityId.equalsIgnoreCase("0")) {
			entityId = (String) session.getAttribute("customerId");
		}
		
		String fromPage = request.getParameter("fromPage");
		if(fromPage==null) {
			fromPage="accountdashboard";
		}
		int userId = CommonUtility.validateNumber(sessionUserId);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		if(userId > 1){
			if(requestType.equalsIgnoreCase("print")){
				request.setAttribute("downloadMyItems", "Y");
			}
			contentObject = ProductsDAO.myItemListByBuyingCompanyDao(CommonUtility.validateNumber(tempSubset),CommonUtility.validateNumber(tempGeneralSubset), CommonUtility.validateNumber(buyingCompanyId),entityId);
			LinkedHashMap<String, String> customerCustomField = (LinkedHashMap<String, String>)session.getAttribute("customerCustomFieldValue");
			String myItemView = "N";
			if ((customerCustomField != null) && (CommonUtility.validateString((String)customerCustomField.get("MY_ITEMS_VIEW")).equalsIgnoreCase("Y"))) {
			 
				myItemView = (String)customerCustomField.get("MY_ITEMS_VIEW");
			    }
			contentObject.put("userId", sessionUserId);
			contentObject.put("myItemView",myItemView);
			contentObject.put("reqType","MI");
			contentObject.put("fromPage", fromPage);

			if(requestType.equalsIgnoreCase("print")){
				contentObject.put("responseType", "myItemsPrint");
				renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject , null, null, null);
			} else {
				renderContent = LayoutGenerator.templateLoader("MyItemListPage", contentObject , null, null, null);
			}
			target = SUCCESS;
		}else{
			target = "SESSIONEXPIRED";
		}
		return target;
	}
	
	@SuppressWarnings("unchecked")
	public void downloadMyItemList(){
		request =ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		String sessionUserId = CommonUtility.validateString((String) session.getAttribute(Global.USERID_KEY));
		//String homeTeritory = (String) session.getAttribute("shipBranchId");
		String entityId = CommonUtility.validateString((String) session.getAttribute("entityId"));
		int buyingCompanyId = CommonUtility.validateNumber((String)session.getAttribute("buyingCompanyId"));
		int tempSubset = CommonUtility.validateNumber((String) session.getAttribute("userSubsetId"));
		int tempGeneralSubset = CommonUtility.validateNumber((String) session.getAttribute("generalCatalog"));
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		if(entityId.isEmpty() || entityId.equalsIgnoreCase("0")) {
			entityId = (String) session.getAttribute("customerId");
		}
		String fileName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DOWNLOAD_MYITEMS"))+"_"+entityId+".xlsx";
		if(CommonUtility.validateNumber(sessionUserId) > 1){
			request.setAttribute("downloadMyItems", "Y");
			contentObject = ProductsDAO.myItemListByBuyingCompanyDao(tempSubset, tempGeneralSubset, buyingCompanyId, entityId);
			contentObject.put("userId", sessionUserId);
			contentObject.put("reqType","MI");
		}
		LinkedHashMap<String, Object> myItemList = contentObject;
		if(myItemList.size() >0  && fileName.length() > 0){
			if(myItemList.get("myItemList") != null){
	    		try{
					XSSFWorkbook workbook = new XSSFWorkbook(); 
				    //Create a blank sheet
				    XSSFSheet sheet = workbook.createSheet("My Item List");
				    
				    Row header = sheet.createRow(0);
				    
				    XSSFCellStyle style = workbook.createCellStyle();
				    style.setBorderTop((short) 6); 
				    style.setBorderBottom((short) 1); 
				    XSSFFont font = workbook.createFont();
				    font.setBold(true);
				    style.setFont(font);  
				    
				    Cell partNumber = header.createCell(0);
				    partNumber.setCellValue(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("product.label.partNumber").replace(" ","_"));
				    partNumber.setCellStyle(style);
				    
				    Cell mpn = header.createCell(1);
				    mpn.setCellValue(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("product.label.mpn").replace(" ","_"));
				    mpn.setCellStyle(style);
				    
				   /* Cell brand = header.createCell(2);
				    brand.setCellStyle(style);
				    brand.setCellValue(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.brandname").replace(" ","_"));*/
				    
				    Cell shortDesc = header.createCell(2);
				    shortDesc.setCellStyle(style);
				    shortDesc.setCellValue(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("product.label.shortDescription").replace(" ","_"));
				    
				    Cell category = header.createCell(3);
				    category.setCellValue(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("product.label.category").replace(" ","_"));
				    category.setCellStyle(style);
				    
				    Cell price = header.createCell(4);
				    price.setCellValue(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("product.label.yourprice").replace(" ","_"));
				    price.setCellStyle(style);
				    
				    Cell lastPurchasedQty = header.createCell(5);
				    lastPurchasedQty.setCellValue("Last Purchased Qty");
				    lastPurchasedQty.setCellStyle(style);
				    
				    Cell lastPurchasedUom = header.createCell(6);
				    lastPurchasedUom.setCellValue("Last Purchased UOM");
				    lastPurchasedUom.setCellStyle(style);
				    
				    Cell YTD = header.createCell(7);
				    YTD.setCellValue("YTD");
				    YTD.setCellStyle(style);
				    
				    Cell lastPurchasedDate = header.createCell(8);
				    lastPurchasedDate.setCellValue("Last Purchased Date");
				    lastPurchasedDate.setCellStyle(style);
				    
				   
				    
				    int rowCount =1;
				    
					ArrayList<ProductsModel> itemList = (ArrayList<ProductsModel>)myItemList.get("myItemList");
				    LinkedHashMap<Integer, ProductsModel> allItemDetails = (LinkedHashMap<Integer, ProductsModel>)contentObject.get("allItemDetails");
				    
				    /*ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
				    ProductManagement priceInquiry = new ProductManagementImpl();
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					priceInquiryInput.setHomeTerritory(homeTeritory);
					priceInquiryInput.setPartIdentifier(itemList);
					priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
					priceInquiryInput.setRequiredAvailabilty("Y");
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					itemList = priceInquiry.priceInquiry(priceInquiryInput , itemList);*/
					
					for(ProductsModel myItemListObject : itemList){
				    	Row data = sheet.createRow(rowCount);
				    	
			    		ProductsModel itemDetailsFromSolr = allItemDetails.get(myItemListObject.getItemId());
			    		Cell cellPartNumber=  data.createCell(0);
			    		cellPartNumber.setCellValue(CommonUtility.validateString(myItemListObject.getPartNumber()));
			    	
			    		Cell cellMpn=  data.createCell(1);
			    		cellMpn.setCellValue(CommonUtility.validateString(itemDetailsFromSolr.getManufacturerPartNumber()));
			    		
			    		/*Cell cellBrandName=  data.createCell(2);
			    		cellBrandName.setCellValue(CommonUtility.validateString(itemDetailsFromSolr.getBrandName()));*/
			    		
			    		Cell cellShortDesc=  data.createCell(2);
			    		cellShortDesc.setCellValue(CommonUtility.validateString(itemDetailsFromSolr.getShortDesc()).replaceAll("\\<.*?\\>", ""));
			    		
			    		Cell categoryCell=  data.createCell(3);
			    		categoryCell.setCellValue(CommonUtility.validateString(itemDetailsFromSolr.getCategoryName()));
			    		
			    		Cell priceCell=  data.createCell(4);
			    		priceCell.setCellValue(CommonUtility.validateParseDoubleToString(myItemListObject.getPrice()));
			    		
			    		Cell lastQtyCell=  data.createCell(5);
			    		lastQtyCell.setCellValue(CommonUtility.validateString(myItemListObject.getLastPurchasedQty()));
			    		
			    		Cell lastUOMCell=  data.createCell(6);
			    		lastUOMCell.setCellValue(CommonUtility.validateString(myItemListObject.getLastPurchasedUnit()));
			    		
			    		Cell ytdCell=  data.createCell(7);
			    		ytdCell.setCellValue(CommonUtility.validateString(myItemListObject.getYearToDate()));
			    		
			    		Cell lastPurchasedDateCell=  data.createCell(8);
			    		lastPurchasedDateCell.setCellValue(CommonUtility.validateString(myItemListObject.getLastPurchasedDate()));
			    		
			    		
				    	rowCount++;
				    }
				    
				    //Writing into Excel sheet
					FileOutputStream fileOut = new FileOutputStream(fileName);
				    workbook.write(fileOut);
				    fileOut.close();
				    
				    //Downloading excel sheet
				    ServletOutputStream out = response.getOutputStream();
				    FileInputStream filein = new FileInputStream(fileName);
				    
			        response.setContentType("application/vnd.ms-excel");
			        response.addHeader("content-disposition","attachment;filename=MyitemList_"+entityId+".xlsx");

			        int octet;
			        while((octet = filein.read()) != -1) {
			            out.write(octet);
			        }

			        filein.close();
			        out.close();
				    System.out.println("My Item List has exported.");
				   
				}catch(Exception e){
					logger.error(e.getMessage());
				}
	    	}
		}
	}
	public String updateMyItems(){
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String categoryName  = CommonUtility.validateString(request.getParameter("catName"));
		int lineId  = CommonUtility.validateNumber(request.getParameter("lineId"));
		if(userId > 1 && categoryName.length()>0){
			int count = ProductsDAO.updateMyItemsValue(lineId, categoryName);
			renderContent = ""+count;
			target = SUCCESS;
		}else{
			target = "SESSIONEXPIRED";
		}
		return target;
	}
	public String insertCustomerCategory(){
		  request =ServletActionContext.getRequest();
		  HttpSession session = request.getSession();
		  String jsonData = (String)request.getParameter("jsonData");
		  Gson parseInput = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		  ProductsModel  parsedData = parseInput.fromJson(jsonData,ProductsModel.class);
		  String entityId = (String) session.getAttribute("entityId");
		  int count = 0;
		  if(entityId.equalsIgnoreCase("0")) {
			  entityId = (String) session.getAttribute("customerId");
		  }
		  if(parsedData!=null){
			  if(CommonUtility.validateString(parsedData.getBranchName()).equalsIgnoreCase("Create")){
				  count = ProductsDAO.insertCustomerCategory(parsedData.getCategoryName(), entityId,parsedData.getPartNumber());  
			  }else{
				  count = ProductsDAO.updateCustomerCategory(parsedData.getCategoryName(), entityId,parsedData.getPartNumber());
			  }
			  
			  renderContent = ""+count;
		  }else{
			  renderContent = "0";
		  }
		  
		  return SUCCESS;
	  }
	
}