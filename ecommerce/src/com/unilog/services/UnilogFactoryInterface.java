package com.unilog.services;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.velocity.VelocityContext;

import com.erp.service.cimm2bcentral.models.PackageInfo;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthorizedSaveCreditCardRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerCard;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerType;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPriceAndAvailability;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPriceBreaks;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSalesOrderHistoryRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipVia;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralWarehouse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BPriceAndAvailabilityRequest;
import com.erp.service.model.ProductManagementModel;
import com.erp.service.model.SalesOrderManagementModel;
import com.erp.service.model.UserManagementModel;
import com.paymentgateway.elementexpress.transaction.Address;
import com.unilog.cimmesb.client.response.CimmLineItem;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.custommodule.model.FreightCalculatorModel;
import com.unilog.defaults.SendMailModel;
import com.unilog.defaults.SendMailUtility;
import com.unilog.maplocation.LocationModel;
import com.unilog.misc.EventModel;
import com.unilog.products.ProductsModel;
import com.unilog.punchout.model.PunchoutRequestModel;
import com.unilog.sales.CIMMTaxModel;
import com.unilog.sales.SalesModel;
import com.unilog.singlesignon.OAuth2Details;
import com.unilog.users.AddressModel;
import com.unilog.users.CreditApplicationModel;
import com.unilog.users.NewUserRegisterUtility;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersAction;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CustomTable;
import com.unilog.cimmesb.client.ecomm.request.ErpItemSearchRequest;

/**
 * Below code Written is for Tyndale. *Reference- Chetan Sandesh
 */
public interface UnilogFactoryInterface {
	
	default void filterMyCatalogGroups(Map<String, Object> contentObject, HttpServletRequest request) {}
	
	default void applyShipViaRestriction(List<ProductsModel> products, Map<String, Object> contentObject) {
	}
	// Town&Country
	default public LinkedHashMap<String, ArrayList<ProductsModel>> getGroupedItemsInProductsData(
			ArrayList<ProductsModel> productListData) {
		return null;
	}

	// Town&Country
	default public LinkedHashMap<String, ArrayList<SalesModel>> getGroupedItemsInSalesData(
			ArrayList<SalesModel> productListData) {
		return null;
	}

	// Town&Country
	default public double getGroupOfTax(LinkedHashMap<String, Object> salesInputParameter, String customParamter) {
		return 0;
	}

	// Town&Country
	default public LinkedHashMap<String, Cimm2BCentralShipVia> getCustomShipViaData(
			ArrayList<ProductsModel> orderDetails) {
		return null;
	}

	// Purvis Industries

	// Purvis Industries
	default double verifyAndExtractShippingCost(ShipVia shipVia, String shipToZipCode, String billToZipCode) {
		return 0;
	}

	// APR
	default List<CustomTable> getOrderModeList() {
		return null;
	}

	// APR
	default void sessionValue() {
	}

	// APR
	default public void getClusterdata() {
	}

	// APR
	default public void getShipviaZipList() {
	}

	// APR
	default public String getShipviaDetail(String zipcode) {
		return null;
	}

	// APR
	default public void getSalesRepDetails() {
	}

	// APR
	default public String getSalesRepEmailDetail(String SManID) {
		return null;
	}

	// APR
	default public List<String> getSalesRepDetailbywarehouse(String branchcode) {
		return null;
	}

	// APR
	default public void insertCustomertarget(String brabchid, int userid, int buyingCompanyid) {
	}
	
	//APR
	default public ProductsModel getwareHouseDetailsforordeoptions(String targetbranch) { 
		return  null;
	}
	
	//APR
	default public Map<String,Double> updatediscount(double discount,double roeDiscount){
		return Collections.emptyMap() ;
	}
	
	//APR
	default public String getwareHouseDetail(String targetbranch){ 
		return "";
	}
	
	default public String getwareHouseDetailforanonymous_rep(String targetid){
		return "";
	}
	
	// Purvis Industries
	default void getOrderedItemsDetails(Map<String, Object> contentObject) {
	}

	// Purvis Industries
	default Map<String, ProductsModel> getRequestForQuoteDetails(List<ProductsModel> products, int subsetId,
			int generalSubsetId) {
		return Collections.emptyMap();
	}

	// Purvis Industries
	default List<PackageInfo> getItemWeights(List<ProductsModel> products) {
		return Collections.emptyList();
	}

	// Purvis Industries
	default Map<String, ProductsModel> loadPrice(List<SalesModel> salesItems, HttpSession session) {
		return Collections.emptyMap();
	}

	// Purvis Industries
	default void configureCreditCardDetails(Cimm2BCentralCustomerCard creditCardDetails) {
	}

	// Purvis Industries
	default void addCartDetailsToQuickOrderPad(int userId, List<ProductsModel> products,
			Map<String, Object> contentObject) {
	}

	// Purvis Industries
	default void addNonCimmItemsToCart(List<ProductsModel> items, HttpSession session) {
	}

	// Purvis Industries
	default void mergeRequiredFields(ProductsModel item, ResultSet rs) throws SQLException {
	}

	// Purvis Industries
	default boolean isRequired(ProductsModel item, int orderId) {
		return true;
	} // always return true if it not required

	// Purvis Industries
	default void cartRelatedAddOns(List<ProductsModel> items, Map<String, Object> contentObject) {
	}

	// Purvis Industries
	default ArrayList<ProductsModel> extractCatalogOrCimmItems(ArrayList<ProductsModel> items) {
		return items;
	}

	// Purvis Industries
	default List<ProductsModel> extractNonCatalogItems(List<ProductsModel> items) {
		return items;
	}

	// Purvis Industries
	default void notifyNonCatalogItemsToSalesRep(List<ProductsModel> items, HttpSession session,
			SalesModel externalOrderDetails) {
	}

	// Purvis Industries
	default void notifyNonCatalogItemsToSalesRep(Map<String, Object> contentObject, HttpSession session) {
	}

	// Purvis Industries
	default boolean makeAsDefaultShipTo(HttpServletRequest request) {
		return false;
	}

	// Purvis Industries
	default String setCatalogIdForQuotedItem(HttpServletRequest request) {
		return request.getParameter("catalogId");
	}

	// Purvis Industries
	default String extractAssignedRole(List<String> descriptions) {
		return "";
	}

	// AdaptPharmaCustomServies
	default public int getFirstOrders(SalesModel salesInputParameter) {
		return 0;
	}

	// AdaptPharmaCustomServies
	default public boolean sendFirstOrderMail(SalesModel erpOrderDetail, SendMailModel sendMailModel, int firstOrder) {
		return false;
	}

	// AdaptPharmaCustomServies
	default public Cimm2BCentralLineItem submitSalesOrderItemList(Cimm2BCentralLineItem cimm2bCentralLineItem) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public String getExpectedDeliveryDate(String date) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public Cimm2BCentralAddress addcustomFieldData(String userName, AddressModel shipAddress,
			AddressModel billAddress) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public ArrayList<UsersModel> addShipAddressList(ArrayList<UsersModel> shipAddressList) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public SalesModel orderdetail(ArrayList<Cimm2BCentralLineItem> ordersList) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public UsersModel contactInfo(AddressModel addressInfo, UsersModel billingAddress,
			UsersModel contactInformation, UsersModel shippingAddress) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public UsersModel billShipAddressInsert(UsersModel billShipInfo, String type) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public String fetchItemId(int subset) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public SalesOrderManagementModel salesOrderdetail(SalesOrderManagementModel salesOrderInput,
			String shipTofirstName, String shipToLastName, String pageTitle, String sequencePoNum,
			UsersModel userShipAddress) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public ProductsModel orderItem(ProductsModel itmVal, String pageTitle, String listPrice, String carton) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public String sendShipAddressFile(UsersModel shipAddressInfo, String result, String files,
			String addFirstName) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public String setDefaultAddress(String shipToName, String addressType, UsersModel userDefaultAddress) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public String partialUserLogin(UsersModel enteredUserNameInfo) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public String getPoSequenceId(String seqName) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public boolean sendMailRegistration(UsersModel userDetailsAddress, String formtype, String whomTo,
			String SuperUserEmail) {
		return false;
	}

	// AdaptPharmaCustomServies
	default public LinkedHashMap<String, String> getFirstOrderNotification() {
		return null;
	}

	// AdaptPharmaCustomServies
	default public String addContextObject(SalesModel orderDetail, VelocityContext context) {
		return null;
	}
	
	// AdaptPharmaCustomServies
	default public LinkedHashMap<String, Object> getAllShippingAddress(LinkedHashMap<String, Object> contentObject, int buyingCompanyId) {
		return null;
	}
	
	// AdaptPharmaCustomServies
	default public String orderConfirmationV2AP(ArrayList<SalesModel> erpOrderDetailList) {
		return null;
	}
	
	// AdaptPharmaCustomServies
	default public boolean sendMultiOrderMail(ArrayList<SalesModel> erpOrderDetailList, SendMailModel sendMailModel, boolean massOrderEmail) {
		return false;
	}
	
	// AdaptPharmaCustomServies
	default public boolean sendFirstMultiOrderMail(ArrayList<SalesModel> erpOrderDetailList, SendMailModel sendMailModel) {
		return false;
	}
	
	// AdaptPharmaCustomServies
	default public String validateNewPurchaseAgent(UsersAction usersAction) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public String validateUserRegistrationDetail(NewUserRegisterUtility user) {
		return null;
	}

	// AdaptPharmaCustomServies
	default public ArrayList<SalesModel> getCustomerOrderHistory(Map<String, String> inputParam) {
		return null;
	}

	// Turtle&Hughes
	default public LinkedHashMap<String, String> redefinedWarehouseList() {
		return null;
	}

	// Turtle&Hughes
	default public LinkedHashMap<String, ProductsModel> activeWarehouseList() {
		return null;
	}

	// Turtle&Hughes
	default public String getNetworkWarehouseCode(String warehouseCode) {
		return null;
	}

	// Turtle&Hughes
	default public String getUom(double salesQty) {
		return null;
	}

	// Turtle&Hughes
	default public double getExtendedPrice(double price, int orderqty, int salesQty, double salesQuntityUom) {
		return 0;
	}

	// Turtle&Hughes
	default public boolean doTwoStepValidation(Cimm2BCentralCustomer customerDetails, String invoiceNo, String poNumber,
			String zipCode) {
		return false;
	}

	// Turtle&Hughes
	default public LinkedHashMap<String, String> LoadNetworkWareHouseList() {
		return null;
	}

	// Turtle&Hughes
	default public LinkedHashMap<String, String> LoadactiveWareHouseList() {
		return null;
	}

	// Turtle&Hughes
	default public String validateAvaAddress(UsersModel shipAddressInfo){
		return null;
		}

	
	// Turtle&Hughes
		default public String getBlanketPoList(HttpSession session) {
			return null;
	}
		// Turtle&Hughes
		default public String formAdditionalInfo(PunchoutRequestModel punchoutModel) {
			return "";
		}
	// Town&Country
	default public String getShipToStoreWarehouseCode(String warehouse) {
		return null;
	}

	// Town&Country
	default public String getCustomShipViaDescription(HttpSession session,
			Cimm2BCentralShipVia orderResponseGetShipVia) {
		return null;
	}

	// Town&Country
	default public int getItemsResultCount(String keyWord, int subsetId, int generalSubset, int fromRow, int toRow,
			String requestType, int psid, int npsid, int treeId, int levelNo, String attrFilterList, String brandId,
			String sessionId, String sortBy, int resultPerPage, String narrowKeyword, int buyingCompanyId,
			String userName, String userToken, String entityId, int userId, String homeTeritory, String type,
			String wareHousecode, String customerId, String customerCountry, boolean isCategoryNav, boolean exactSearch,
			String viewFrequentlyPurcahsedOnly, String clearanceFlag, String wareHouseItems) {
		return 0;
	}

	// Town&Country
	default public int checkItemsFordifferentLocation(int userId, String sessionId, HttpSession session, String flag) {
		return 0;
	}

	// Town&Country
	default public String addressLine1Combined(AddressModel address) {
		return null;
	}

	// Electrozad Supply
	default public void warehouseLevelCatalog(UsersModel catalog, HashMap<String, String> userDetails,
			HttpSession session) {
	}

	// Electrozad Supply
	default public UsersModel getCustomer(UsersModel userDetailList) {
		return null;
	}

	// Electrozad Supply
	default public EventModel getEventRegistrationDetails(int eventRegId) {
		return null;
	}

	// Electrozad Supply
	default public HashMap<String, String> getBCAddressBookCustomFields(int selectedShippingAddressId) {
		return null;
	}

	// Electrozad Supply
	default public void getBCAddressBookCustomFieldDetails(UsersModel getBCAddressBookCustomFieldDetails) {
	}

	// Electrozad Supply
	default public void checkShipToLevelCustomFieldsOnAssignShipEntity(HttpServletRequest request, HttpSession session,
			int subsetId) {
	}

	// Electrozad Supply
	default public void checkBCAddressCustomFieldsAfterAutoSync(HttpSession session) {
	}

	// Electrozad Supply
	default public void checkOversizeAndHazmatFreightRule(
			LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal, SalesModel salesOrderDetailList) {
	}

	// Electrozad Supply
	default public void addExchangeRateToTax(HttpSession session, SalesModel quoteInfo,
			Cimm2BCentralOrder orderResponse) {
	}

	// Electrozad Supply
	default public void addExchangeRateToTotal(HttpSession session, SalesModel quoteInfo,
			Cimm2BCentralOrder orderResponse, double freightCharges) {
	}

	// Electrozad Supply
	default public void checkWebCurrency(Cimm2BCentralCustomer customerDetails, boolean WebCurrency) {
	}

	// Electrozad Supply
	default public void setCurrencyCodeDetails(Cimm2BCentralCustomer shipTo, AddressModel shipAddressModel) {
	}

	// Electrozad Supply
	default public void deleteBCAddressBookDetailsFromBCAddressBookCustomTable(Connection conn, HttpSession session) {
	}

	// Electrozad Supply
	default public void insertBCAddressBookCustomFields(AddressModel getShipList, int userId, int shipId) {
	}

	// Electrozad Supply
	default public void insertDefaultValueToBCAddressBookCustomFields(boolean webCurrency, int userId, int shipId) {
	}

	// Electrozad Supply
	default public int getUserId(Connection conn, UsersModel contactInfo, int userId) {
		return 0;
	}

	// Electrozad Supply
	default public ArrayList<ProductsModel> getEventOrderDetails(SalesOrderManagementModel salesOrderInput) {
		return null;
	}

	// Electrozad Supply
	default public void setOversizeAndHazmatFreightRule(
			LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal,
			LinkedHashMap<String, Object> contentObject) {
	}

	// Electrozad Supply
	default public double getFrieghtCharges(String shipViaDisplay, HttpSession session,
			LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal, double totalCartFrieghtCharges, FreightCalculatorModel freightValue) {
		return totalCartFrieghtCharges;
	}

	// Electrozad Supply
	default public void getCreditBalanceDetails(HttpSession session, HashMap<String, UsersModel> userAddress,
			LinkedHashMap<String, Object> contentObject) {
	}

	// Electrozad Supply
	default public void checkCurrencyConversion(HttpSession session, Cimm2BCentralItem item) {
	}

	// Electrozad Supply
	default public void addExchangeRateToQuantityBreakItems(Cimm2BCentralItem item, ProductsModel productsModel,
			Cimm2BCentralWarehouse wareHouseDetail) {
	}

	// Electrozad Supply
	default public void setQuantityBreakItemsPrice(Cimm2BCentralItem item, ProductsModel productsModel,
			ProductManagementModel priceInquiryInput) {
	}

	// Electrozad Supply
	default public void insertOrderStatus(LinkedHashMap<String, Object> orderDetails, String orderStatus) {
	}

	// Electrozad Supply
	default public void setOrderSatusToOrderDetail(SalesModel erpOrderDetail, SalesModel defaultOrderDetail,
			String orderStatus) {
	}

	// Electrozad Supply
	default public String getOrderStatus(LinkedHashMap<String, Object> orderDetails) {
		return null;
	}

	// Electrozad Supply
	default public String setFirstLoginTrueForInactiveUser() {
		return null;
	}

	// Electrozad Supply
	default public String setCountryName(UsersModel userInfo) {
		return null;
	}

	// Geary Pacific
	default void getAuthAmount(Cimm2BCentralCustomerCard customerCard, SalesOrderManagementModel salesOrderInput) {
	}

	// Geary Pacific
	default void getBranchCode(Cimm2BCentralOrder order, String warehouseCode) {
	}

	// Geary Pacific
	default String getBrandPrefix() {
		return "";
	}

	default void configCartListValues(ProductsModel cartListVal, ResultSet rs) { } // Geary Pacific
	
	default void configOrderItemValues(ProductsModel orderItemVal, ResultSet rs) { } // Safeware
	
	//BraasCo Services
	default void getPrice(Cimm2BCentralItem item,ProductsModel productsModel){}

	default void getUnitPrice(ProductsModel eclipseitemPrice, ProductsModel itemPrice){}

	default double getdefaultCustomerPrice(ProductsModel eclipseitemPrice, ProductsModel itemPrice,double price){
		return price;}

	default void getTotalForDefault(ProductsModel itemPrice, double price){}

	default void overRideERPID(UsersModel addressModel, Cimm2BCentralAddress cimm2bCentralBillingAddress){}

	default void overRideERPIDRequired(AddressModel addressModel, Cimm2BCentralAddress cimm2bCentralBillingAddress){}

	default void setDefaultWareHouseFromERP(UsersModel customerinfo, Cimm2BCentralCustomer customerDetails){};
	
	default void getUnitPriceofOrders(SalesModel sales,Cimm2BCentralLineItem item,Cimm2BCentralOrder order){}

	default void UniquePoNumberHide(Cimm2BCentralOrder order){}

	default String  getcategoryName(ArrayList<ProductsModel> itemLevelFilterData,String categoryName){
		return categoryName;}
	default void overRideAddressID(UsersModel addressModel, Cimm2BCentralAddress cimm2bCentralBillingAddress){}
	
	default void overRideAddressID(AddressModel addressModel, Cimm2BCentralAddress cimm2bCentralBillingAddress){};

	// Etna Supply
	default Object getCustomerAlsoBoughtCustom(int userId,int subsetId, int generalSubset, ArrayList<SalesModel> salesOrderItem) {return null;}
	
	default void customizeOrderResponse(List<ProductsModel> lineItems, List<Cimm2BCentralLineItem> orderedItems) {}

	default String sendmailWithAccountNumber(UsersModel userDetailsInput){
		return null;}
		
	// Werner Redesign
	default public String userIdSetUp() {return null;}
	
	// Werner Redesign
	default void  setSubsetViaZipCode(UsersModel uModel, UsersModel usersModel) {}
		
	//Werner Design
	default public List<CustomTable> getWebsiteZipCodeCustomTable(){return null;}
	
	default public void getListPrice(ProductsModel itemModel, Cimm2BCentralLineItem lineItem) { } // Geary Pacific Supply	

	//Habegger Corp	
	default void connectToSso(HttpServletResponse response,OAuth2Details oauthDetails) {}

	default public HashMap<String, String> registerSsoUserInDb(Map<String, String> ssoUserDetail) {
			return null;
		}
	
	default public void removesessionvalues(HttpSession session) {
	}

	default public  Map<String,List<String>> getClusterdatabeforelogin() {
		return null;
	}
	
	default public ArrayList<LocationModel> loadServices(){
		return null;
	}

	default public ArrayList<LocationModel> loadnearestwarehouses(ArrayList<LocationModel> locationList){
		return null;
	}
	
	default void customizeOrderLineItem(Cimm2BCentralLineItem lineItem, ProductsModel cartItem) {}

	default List<CustomTable> getpromotional_image() {return null;}

	default double addtoCartCustomerPrice(ProductsModel itemPrice,ProductsModel eclipseitemPrice) { return 0;}

	default List<CustomTable> getPromotional_Image() {return null;}

	default List<CustomTable> getDropDownBanner() {return null;}

	default List<CustomTable> getCartPopUpPromoImages() {return null;}
	//EIA
	default public String getRetailUserOrdersFromCIMM(String userRole) { return null; }
	//APR
	default public String changeFromEmailaddress(SendMailModel sendMailModel) {return null;}
	
	//Aaron and Company Services
	default String CreditAppRegistrationAndPdf(AddressModel userRegistrationDetail, String result,CreditApplicationModel creditAplication,HttpSession session) { return result; }

	//Dave Carter
	default String creditApplication(HttpSession session,Map<String, String> formParameters, SendMailUtility sendMailUtility, VelocityContext context, SaveCustomFormDetails getNotificationDetail, String upload) {return upload;}
	
	default public void setQuoteTotal(SalesModel modelData, double total){}
	
	default void addDiscountToLineItems(ArrayList<Cimm2BCentralLineItem> lineItems,double discountAmount, HttpSession session) {}
	
	default void setRoundPrice(Cimm2BCentralItem item,ProductsModel productsModel){}
	
	default void setQtyOnERP(SalesModel sales,Cimm2BCentralLineItem item){}
	
	default public double setroundOfDiscount(double discountAmount) {return discountAmount;	}
	//APR
	default List<CustomTable> getSales_Rep_Details() {return null;}

	default public ArrayList<WarehouseModel> getGroupedWareHouses() {return null;}

	default public ArrayList<Cimm2BCentralLineItem> setlineItemComments(ArrayList<Cimm2BCentralLineItem> lineItems,
			SalesOrderManagementModel salesOrderInput){return null;}
	
	//purvis Industries
	default Cimm2BCentralAddress billAddressExist(Cimm2BCentralAddress address,Cimm2BCentralCustomer customerDetails){return address;}
			
	default void setShipViaDescription(SalesOrderManagementModel salesOrderInput,Cimm2BCentralShipVia cimm2bCentralShipVia){}

	default public void erpQuanitityBreakUpdate(ProductManagementModel priceInquiryInput,ProductsModel product, ArrayList<ProductsModel> quantityBreak) {}

	default public void getPackDesc(ProductsModel saveItems) {}

	default public SalesOrderManagementModel orderFreightcalculation(SalesOrderManagementModel salesOrderInput) { return null;}
	//Aaron Company	
	default void addDiscountedItemsToLineItems(ArrayList<Cimm2BCentralLineItem> lineItems,double discountAmount, HttpSession session) {}

	default void enableDiscountCoupon(HttpSession session,LinkedHashMap<String, Object> contentObject,ArrayList<String> shipVia,String discountCoupons,Map<String, Object> details,List<ProductsModel> cartDetails) {}
	//Habegger Wild card search custom
	default public String modifySearchString(String queryString,String origKeyWord) {return null;}

	default SolrQuery getquery(String queryString, SolrQuery query) {return query;}

	// Town&Country
	default Cimm2BCentralAuthorizedSaveCreditCardRequest prepareCreateProfileRequest(SalesModel salesInputParameter) {return null;}
		
	// Town&Country
	default SalesModel createQuote(LinkedHashMap<String, Object> createQuoteInput) {return null;}
		
		// Town&Country
	default SalesModel submitOrderToERP(SalesOrderManagementModel salesOrderInput) {return null;}
		
		// Town&Country
	default void setOrderValuesFromQuoteResponse(SalesModel quoteResponse,LinkedHashMap<String, Object> contentObject){}
		
		// Town&Country
	default Cimm2BCentralPriceAndAvailability getAllStoreAvailability(String[] partNumber) {return null;}
		
		// Town&Country
	default AddressModel createRetailUser(UsersModel userDetails) {return null;}
		
		// Town&Country
	default void overrideAuAddress(UsersModel userDetail,UsersModel billAddress,UsersModel shipAddress) {}
	
	// Town&Country
	default double excludeItemWeight(String selectedShipMethod, double weight) {return weight;}
	
	//Turtle
	default String convertDigitalSignature(HttpSession session,String[] signatures, String upload) {return upload;}
	
	default String getShipDetails(String result, HttpSession session) { return result; }
	//Mallory
	default Cimm2BPriceAndAvailabilityRequest priceAndAvailIncludeUomList(Cimm2BPriceAndAvailabilityRequest priceAndAvailabilityRequest,ProductManagementModel priceInquiryInput) {return priceAndAvailabilityRequest;}

	//EIA
	default UsersModel getUserContactAddress(int userId,  HttpSession session) {return null;}
		
	default void getRetailCustomerUserERPId(UsersModel contactInformation, String custNumber) {}
	
	//Electrozad
	default void insertCustomFieldValues(String validateString, int userId, int buyingCompanyid) {}

	//APR
	default Cimm2BCentralOrder disableInteralNoteToERP(Cimm2BCentralOrder orderrequest){ return orderrequest;}
	
	default public void setUserStatus(ArrayList<UsersModel> superUserStatus ,UsersModel userDetails, LinkedHashMap<String,String> userRegisteration) {} // Geary Pacific Supply
		
	default public void insertCustomFields(String warehouse, int userId, int buyingCompanyid, UsersModel userDetailsInput) {} // Wallace Hardware
	
	default public void quanitityBreakCalculation(ProductsModel productsModel, Cimm2BCentralPriceBreaks priceBreaks) {} // Wallace Hardware
	
	default void erpQuanitityBreakUpdate(ProductsModel productsModel, ProductsModel pricingResponse) {}  //DaveCarter

	default void quanitityBreakCalculationwithDiscount(Cimm2BCentralItem item, ProductsModel productsModel,
			Cimm2BCentralPriceBreaks priceBreaks) {}
	
	default public String setPricingDefaultWareHouseCode(String warehouseCode) {return warehouseCode;} // EIA
	
	default public void setBillToShipToAddressFields(String firstName, UsersModel userBillAddress, AddressModel selectedShipAddress, HttpSession session) {} // EIA
	
	default void getDiscountPrice(SalesModel orderDetail,VelocityContext context) {} // Aaron and Company
	
	default void updateQuotesItemsToCart(String qPno,ProductsModel quotesCart,HttpServletRequest request) { } // Supplyforce
	
	default int getUserBuyingCompanyId(int buyingCompanyId, int userBuyingCompanyId)
	{ 
		return buyingCompanyId;
	}   //supplyforce
	
	default void setOrderComment(Cimm2BCentralCustomerCard customerCard, Cimm2BCentralOrder salesOrderInput) {} //Turtle
	
	default void setWareHousePrice(Cimm2BCentralWarehouse wareHouseDetail, ProductsModel branchModel) {	}

	 //APR Supply
	 default Cimm2BCentralLineItem  disableUmqtyToErp(Cimm2BCentralLineItem cimm2bCentralLineItem){ return cimm2bCentralLineItem;}
	 
	 default void setUserToken(HashMap<String, String> userDetails, HttpSession session){} // Werner Redesign 
	 
	 default int CustomizatedSubsetId( UsersModel userDetailList, int subsetId) {return subsetId;}

	 default boolean disableSendMailApprove(SalesModel erpOrderDetail) { return true; }
	 
	 default List<CIMMTaxModel> loadCIMMTaxTable() {return null;}

	default void removeShipViaFromSession() {}

	default void removeShipViaFromSession(HttpSession session) {}
	
	default void availWarehouse(String warehouseCode, Set<String> allWarehouseCodes) {}

	default void setAvailQty(String warehouseCode, Cimm2BCentralWarehouse wareHouseDetail, ProductsModel productsModel) {	}

	default void availQtyUpdate(String warehouseCode, Cimm2BCentralWarehouse wareHouseDetail, ProductsModel productsModel,Cimm2BCentralItem item, String currentWareHouseCode) {}

	default String getWillCallBranchSelected(String willCallBranchCode, HttpSession session) { return willCallBranchCode; }

	default void addDefaultWarehouseToItems(Cimm2BCentralLineItem cimm2bCentralLineItem, HttpSession session, SalesOrderManagementModel salesOrderInput, String wareHousecode, String salesLocationId) {}

	default void addDefaultWarehouseToOrderRequest(Cimm2BCentralOrder orderRequest, HttpSession session, SalesOrderManagementModel salesOrderInput, String wareHousecode, String salesLocationId) {}

	default void setUserDetails(String others1b, String salesPersonName1B, String howDidYouSelect1B, UsersModel userDetails) {}

	default void addUserInformation(UsersModel userDetails, LinkedHashMap<String, String> userRegisteration, String onAccountSetUp) {}

	default void setUserInformation(String others, String salesPersonName, String howDidYouSelect, LinkedHashMap<String, String> userRegisteration) {}

	default void userRegistrationInformation(String others, String salesPersonName, String howDidYouSelect,	AddressModel userRegistrationDetail) {}

	default void dontaddfreightcharges(SalesModel quoteInfo, Cimm2BCentralOrder orderResponse) {}

	default boolean validateEmailAddress(boolean isValidUser, String emailAddress1B) { return isValidUser;}
	
	default void pricingBranchCode(Cimm2BCentralOrder order,Map<String, Object> otherDetails){} // Aaron Company
	 
	default void pricingBranchId(Cimm2BCentralOrder orderRequest){} // Aaron Company
	 
	default void pickUpSelectedBranch(String pickUpWareHouseCode){} // Aaron Company

	default void cartJsonData(ArrayList<ProductsModel> productListData, LinkedHashMap<String, Object> contentObject) {}

	default void setAllBranchAvailValue(ProductManagementModel priceInquiryInput) {}
		
	default double getTaxFromCIMM(UsersModel shipAddress, double taxFromErp, double total){	return taxFromErp;}
	
	default void getPickUpBranchCode(Cimm2BCentralOrder orderRequest, String salesLocationId){} // Aaron Company
	
	default void setHeaderLevelAuthDetails(String userName , String password){} // Shearer
	
	default void setCustomerHomeBranch(Cimm2BCentralCustomer customerInfo){} // Shearer 
	
	default void setAllBranchAvailValueInGroupAndCart(ProductManagementModel priceInquiryInput) {} //Fromm Electric

	default AddressModel CustomizedBuyingCompany(AddressModel userRegistrationDetail){ return userRegistrationDetail;}//crane 

	default String customizedCustomerNumber(UsersModel contactInformation, String custNumber){ return custNumber;}//crane 

	default boolean setCustomerType(Cimm2BCentralCustomer customerDetails, boolean continuereg) { return continuereg;}

	default void orderFreightcalculation(Map<String, Object> details, List<ProductsModel> cartDetails) {}

	default void updateUom(Cimm2BCentralLineItem cimm2bCentralLineItem ,ProductsModel item) {}
	default public SalesModel substractingDiscountToCartItem(SalesModel salesInputParameter) {return salesInputParameter;}
	
	default int resultCountCondition(int resultCount) {return resultCount;}
	
	default void setContractIdToSession(Cimm2BCentralCustomer customerDetails, HttpSession session) {} //Mallory
	
	default ArrayList<UsersModel> userSharedCart(String buyingCompanyId, ArrayList<UsersModel> userList){
		return userList;
	}

	default String sendSharedCart(String savedGroupName) {return "";}
	
	default String setContactEmailToOrder(int userId, String userERPId){return "";} 
	
	default int updateCartCountWithSumOfQty(int totalQtyAvailable, HttpSession session){ return totalQtyAvailable; }
	
	default double calculatingOrderSubTotal(ArrayList<ProductsModel> itemDetailObject, double orderSubTotal) {return orderSubTotal;}

	default double assigningTotal(double orderGrandTotal, double orderSubTotal, String orderTax, String orderFreight) {return orderGrandTotal;}
	
	default void getItemLevelPrice(Cimm2BCentralItem item,ProductsModel productsModel){} //Geary Pacific
	 
	default int setBillAddressToShippAddress(Connection conn, UsersModel customerinfo, int shipId) { return shipId; } // Wallace Hardware
		
	default void setBackOrdersFlag(Cimm2BCentralOrder orderRequest, SalesOrderManagementModel salesOrderInput) {}  // Wallace Hardware
	 
	default void setBackOrderTypeToLineItems(Cimm2BCentralLineItem lineItem, ProductsModel cartItem, SalesOrderManagementModel salesOrderInput) {} // Wallace Hardware
	 
	default void assignEntityIdForShip(UsersModel usersModel, HttpSession session) {} // Wallace HardWare
	 
	default void setOpenOrderSalesInputParams(SalesModel salesInputParameter, HttpServletRequest request) {} // Geary Pacific
	 
	default String getOpenOrdersRequestParams(SalesModel salesInputParameter, String GET_OPEN_ORDERS) {return "";} // Geary Pacific
	 
	default void setOrderHistorySalesInputParams(SalesModel salesInputParameter, HttpServletRequest request) {} // Geary Pacific
	 
	default void getOrderHistoryRequestParams(SalesModel salesInputParameter, Cimm2BCentralSalesOrderHistoryRequest salesOrderHistoryRequest) {} // Geary Pacific

	default void setAdditionalInfo(ProductsModel itemPrice,ProductsModel eclipseitemPrice) {}

	default void changeCustomerName(Cimm2BCentralOrder orderRequest, SalesOrderManagementModel salesOrderInput) {}

	default void addfirstNameAndlastNameToshipAddress(AddressModel selectedShipAddress, UsersModel userShipAddress,HttpSession session) {}
	
	default void setCustomSalesOrderInputValues(SalesOrderManagementModel salesOrderInput, HttpServletRequest request) {}; //Wallace Hardware
	
	default boolean verifyingWareHouse(String warehouseCode, String currentWareHouseCode, boolean wareHouseCustomerPrice) {return wareHouseCustomerPrice;}

	default double dontAddFreightoTotal(double subTotal, double taxFromErp, double totalCartFrieghtCharges, double orderTotal) {return orderTotal;}

	default void getUomPackAsUom(Cimm2BCentralItem item, ProductsModel productsModel){}; // Billows Electric
	
	default String getPartnumberUomQtySplitChar() {return null;} //EIA

	default double getPriceCustomService(double price, ProductsModel eclipseitemPrice, boolean quantityBreakFlag) {return price;} //Seco
	
	default void userRegistrationInformation(AddressModel addressModel, UsersModel contactInformation) {}

	default void addCustomerName(UsersModel billAddress, UsersModel userDetail) {}

	default void setOrderNotes(Cimm2BCentralOrder orderRequest){}; // Mallory Comapny
	
	default void updateRoleForFirstUser(Integer userId,UsersModel userDetailsInput) {}; // Geary Pacific
	
    default void getFirstApproverThresholdrange(LinkedHashMap<String, Object> contentObject,HttpSession session) {} // turtle
	
	default void getSecondApproverThresholdrange(LinkedHashMap<String, Object> contentObject) {} // turtle
	
	default boolean setSendMailToSalesRep(SalesModel erpOrderDetail, SendMailModel sendMailModel, boolean mailsent) {return true;}

	default boolean restricatingItemRetail(LinkedHashMap<String, Object> customFieldVal, boolean restrictedItemFlag, HttpSession session) { return restrictedItemFlag;}
	
	default String getCustomerDefaultEntityId(int buyingCompanyId, String entityId, HttpSession session) {return entityId;}

	default void setHomeBranchName(ProductsModel productsModel, String warehouseCode) {}
	
	default String setRushFlagToLineItems(CimmLineItem lineItem, ProductsModel item) {return "N";}
	
	default public String getChatbotExternalToken(HttpSession session) { return null; } // Turtle
	
	default public LinkedHashMap<String, ArrayList<SalesModel>> getTrackOrderInfo(SalesModel salesInputParameter, HashMap<String,String> userDetails, HttpSession session, String orderId, LinkedHashMap<String, Object> contentObject){ return null; } //Turtle
	
	default void getTrackOrderRequestParams(SalesModel salesInputParameter, Cimm2BCentralSalesOrderHistoryRequest salesOrderHistoryRequest) {} // Turtle

	default void getTotalFrieghtAddonCharges(Cimm2BCentralOrder order, SalesModel orderDetailModel) {} // Turtle
	// Turtle&Hughes
	default public LinkedHashMap<String, String> LoadShipViaMapDetails() {
		return null;
	}
	
	default void getMultipleCarrierTrackingNumber(ArrayList<Cimm2BCentralShipVia> shipMethod, SalesModel orderDetailModel) {} // Turtle

	default String customizedSubjectMailToCustomerAndCreditManager(LinkedHashMap<String, Object> contentData, String subject){return subject;}
	
	default String setShipViaCodeToShipVia(String shipVia, HttpServletRequest request) {return shipVia;}
	
	default void setCurrencyCodeToSession(Cimm2BCentralCustomer customerDetails, HttpSession session) {}

	default void setPaymentTerm(Cimm2BCentralOrder orderRequest){} //Warshauer

	default void addfirstNameAndlastNameToBillAddress(Address address, UsersModel billAddress) {}
	
	default int addNonCatalogToRfqCart(String sessionId, int userId, int qty, String desc, String part,	ProductsModel itemDetails) {return userId;} //Hubbard
	
	default void setMinOrdQtyandQtyIntForMultpleUOM(ProductsModel eclipseitemPrice,ProductsModel itemPrice) {} //Hubbard
	
	default void shipAddressERPIdCity(Cimm2BCentralAddress shipToAddress, UsersModel shipAddressList) {} // Braas
	
	default void addBankAndTradeReferenceAttachments(String [] attachments, String [] attachmentsFileName, String salesTaxExemptionCertificateFileName, String bankReferenceFileName, String tradeReferenceFileName, String filePath, String sessionId) {}  //Fromm Electric
	
	default String getModifiedOrderMailSubject(String orderMailSubject, SalesModel orderDetail, String mailSubject) {return orderMailSubject;}//Mallory Company
	
	default ErpItemSearchRequest buildCustomPriceEnquiryRequestBody(ErpItemSearchRequest requestBody, ProductManagementModel priceInquiryInput, HttpSession session){
		return requestBody;
	}
	
	default String getEntityShipToId(HttpSession session, String defaultShiptoId, String shipToIdSx) { return defaultShiptoId;}

	default boolean enableMainItemWPEnable(String[] attrFtrGlobal, boolean isProductGroup) {return isProductGroup;}

	default SolrQuery setRequestHandler(String[] attrFtrGlobal, SolrQuery query) {return query;}
	default double getERPPrice(ProductsModel eclipseitemPrice, double unitPriceFromEcommerce){
		return unitPriceFromEcommerce;}
	
	default String  setOverRideCatId(String overRideCatId){return overRideCatId;}

	default void updateUserCustomFields(String hidePrice, String invoiceDetails, String invoiceSummary,String buyingCompanyId, String entityId, int userId) {}

	default public Cimm2BCentralLineItem disablePriceToErp(Cimm2BCentralLineItem cimm2bCentralLineItem, ProductsModel item){ return cimm2bCentralLineItem;}

	default void setWrittenBy(Cimm2BCentralOrder orderRequest) {}

	default String setRejectOrderMailSubject(String orderStatus,String cartApprovalMailSubject) {return cartApprovalMailSubject;}
	
	default boolean checkInactiveStatus(String systemCustomerId) {return false;}
	
	default void getUserInfoFromErp(HttpSession session) {};
	
	default public boolean sendItemNotInCimmMail(List<ProductsModel> itemsNotInCimm,String orderNumber) {return false;}
	
	default void setFromMailAddress(String fromEmail, LinkedHashMap<String, String> notificationDetail) {}
	
	default String validateCoupons(SalesModel couponsInput) {return null;}
	
	default FileOutputStream setfilePath(FileOutputStream file,String customerErpId,String statementYear,String month) {return file;}
	
	default boolean validateUserName(String username, UsersModel userInfo,boolean userStatus) {return userStatus;}

	default String setResult(boolean userStatus, String result) {return result;}

	default void setOrderNumber(VelocityContext context, int orderId) {}
	
	default void setMailSubject(SalesModel erpOrderDetail, SendMailModel sendMailModel, HttpSession session) {}
}


