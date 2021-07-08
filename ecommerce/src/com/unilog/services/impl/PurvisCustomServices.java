package com.unilog.services.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.erp.service.ProductManagement;
import com.erp.service.cimm2bcentral.models.PackageInfo;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerCard;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipVia;
import com.erp.service.impl.ProductManagementImpl;
import com.erp.service.model.ProductManagementModel;
import com.erp.service.model.SalesOrderManagementModel;
import com.google.gson.JsonObject;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.ecommerce.user.validation.util.RegistrationUtils;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesDAO;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.ShipVia;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class PurvisCustomServices implements UnilogFactoryInterface{
	
	private static List<String> restrictedZipCodes;
	private static UnilogFactoryInterface serviceProvider;
	
	private PurvisCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (PurvisCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new PurvisCustomServices();
				}
			}
		return serviceProvider;
	}
	{
		loadResctionZipCodeTable();
	}
	
	public static void loadResctionZipCodeTable() {
		List<CustomTable> customTables  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","SHIPPING_RESTRICTED_ZIPCODES");
		if(customTables != null && customTables.size() > 0) {
			List<String> zipCodes = customTables.stream().map(table -> table.getTableDetails())
	            .flatMap(rows -> rows.stream())
	            .map(row -> row.get("ZIP_CODE")).sorted()
	            .collect(toList());
			setRestrictedZipCodes(zipCodes);
		}
	}
	@Override
	public void applyShipViaRestriction(List<ProductsModel> products, Map<String, Object> contentObject){
		List<ShipVia> shipVias = CommonDBQuery.getSiteShipViaList();
		
		boolean hasRectrictedItems =  doesCartHasRestrcitedItems(products);
		if(hasRectrictedItems) {
			shipVias = filter(shipVias);
		}
		contentObject.put("SiteShipViaList", shipVias);
		if(CommonDBQuery.getSystemParamtersList().get("CUSTOM_FIELDS_TO_BE_VERIFIED") != null) {
			String customToBeVerified = CommonDBQuery.getSystemParamtersList().get("CUSTOM_FIELDS_TO_BE_VERIFIED");
			String customToBeVerifiedList[] = customToBeVerified.split(",");
			Map<String, Boolean> verifiedCustomFields = new HashMap<>(); 
			for(String eachCustomField : customToBeVerifiedList) {
				verifiedCustomFields.put(eachCustomField, hasCustomFieldSet(products, eachCustomField));
			}
			contentObject.put("verifiedCustomFields", verifiedCustomFields);
		}
	}
	
	@Override
	public double verifyAndExtractShippingCost(ShipVia shipVia, String shipToZipCode, String billToZipCode) {
		int index = Collections.binarySearch(getRestrictedZipCodes(), shipToZipCode);
		if(index >= 0) {
			return shipVia.getShipCost();
		}
		return 0;
	}

	public static List<String> getRestrictedZipCodes() {
		return restrictedZipCodes;
	}

	public static void setRestrictedZipCodes(List<String> restrictedZipCodes) {
		PurvisCustomServices.restrictedZipCodes = restrictedZipCodes;
	}
	
	@Override
	public void getOrderedItemsDetails(Map<String, Object> contentObject) {
		String tempSubset = (String) contentObject.get("userSubsetId");
		int subsetId = CommonUtility.validateNumber(tempSubset);
		String tempGeneralSubset = (String) contentObject.get("generalCatalog");
		int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		int buyingCompanyId = Integer.parseInt( contentObject.get("buyingCompanyId").toString());
		
		List<SalesModel> orderedItems = (ArrayList<SalesModel>) contentObject.get("orderItemList");
		List<String> partNumbers = extractPartsFromSalesModel(orderedItems);
		
		List<ProductsModel> itemsDetails =  ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubset, StringUtils.join(partNumbers, " OR "),0,null,"partnumber");
		List<String> itemIds = CommonUtility.getItemIds(itemsDetails);
		
		Map<Integer, ArrayList<ProductsModel>> customerPartNumbers = ProductHunterSolr.getcustomerPartnumber(StringUtils.join(itemIds, " OR "), buyingCompanyId, buyingCompanyId);
		contentObject.put("customerPartNumbers", combineCustomerPartNumbers(customerPartNumbers));
		contentObject.put("productDetailsAsMap", getProductsAsMap(itemsDetails));
		
	}
	
	@Override
	public Map<String, ProductsModel> getRequestForQuoteDetails(List<ProductsModel> products, int subsetId, int generalSubsetId) {
		List<String> partNumbers = extractPartsFromProductsModel(products);
		ArrayList<ProductsModel> itemDetails = ProductHunterSolr.getItemDetailsForGivenPartNumbers( subsetId, generalSubsetId, StringUtils.join(partNumbers, " OR "),0,null,"partnumber");
		return getProductsAsMap(itemDetails);
	}

	@Override
	public List<PackageInfo> getItemWeights(List<ProductsModel> products) {
		String defaultUom = CommonDBQuery.getSystemParamtersList().get("DEFAULT_CART_WEIGHT_UOM");
		double defaultItemWeight = CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("DEFAULT_ITEM_WEIGHT"));
		List<PackageInfo> listShippingWeights = new ArrayList<PackageInfo>();
		/*for(ProductsModel eachItem : products) {
			double weight = 0;
			if(eachItem.getWeight() > 0.0){
				weight = eachItem.getWeight();
			}else{
				weight = defaultItemWeight;
			}
			for(int i = 0; i < (eachItem.getQty()/eachItem.getMinOrderQty()); i++) {
				PackageInfo packageInfoDetails = new PackageInfo();
				packageInfoDetails.setWeight(CommonUtility.validateParseDoubleToString(weight));
				packageInfoDetails.setUom(defaultUom);
				packageInfoDetails.setPackageCode("02");
				packageInfoDetails.setPackageInstruction("Package");
				listShippingWeights.add(packageInfoDetails);
			}
		}*/
		double totalCartFrieght= SalesDAO.getTotalCartWeight((ArrayList<ProductsModel>)products);
		double thresholdWeightLimit = CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("MAXIMUM_CARTWEIGHT_FOR_SHIPPING"));
		int multiple = (int) (totalCartFrieght / thresholdWeightLimit);
		double balanceweight = totalCartFrieght - (thresholdWeightLimit * multiple);
		if(totalCartFrieght <= thresholdWeightLimit){
			PackageInfo packageInfoDetails = new PackageInfo();
			packageInfoDetails.setWeight(CommonUtility.validateParseDoubleToString(totalCartFrieght));
			packageInfoDetails.setUom(defaultUom);
			packageInfoDetails.setPackageCode("02");
			packageInfoDetails.setPackageInstruction("Package");
			listShippingWeights.add(packageInfoDetails);
		}else {
			for(int i = 0; i < multiple; i++) {
				PackageInfo packageInfoDetails = new PackageInfo();
				packageInfoDetails.setWeight(CommonUtility.validateParseDoubleToString(thresholdWeightLimit));
				packageInfoDetails.setUom(defaultUom);
				packageInfoDetails.setPackageCode("02");
				packageInfoDetails.setPackageInstruction("Package");
				listShippingWeights.add(packageInfoDetails);
			}
			if(balanceweight>0.0) {
				PackageInfo packageInfoDetails = new PackageInfo();
				packageInfoDetails.setWeight(CommonUtility.validateParseDoubleToString(balanceweight));
				packageInfoDetails.setUom(defaultUom);
				packageInfoDetails.setPackageCode("02");
				packageInfoDetails.setPackageInstruction("Package");
				listShippingWeights.add(packageInfoDetails);
			}
		}
		return listShippingWeights;
	}
	@Override
	public Map<String, ProductsModel> loadPrice(List<SalesModel> salesItems, HttpSession session){
		List<ProductsModel> products = transformSalesToProductsModel(salesItems);
		
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<>();
		for(ProductsModel product : products) {
			partIdentifierQuantity.add(product.getQty());
		}
		ProductManagement priceInquiry = new ProductManagementImpl();
		ProductManagementModel priceInquiryInput = new ProductManagementModel();
		String entityId = (String) session.getAttribute("entityId");
		priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
		priceInquiryInput.setHomeTerritory("");
		priceInquiryInput.setPartIdentifier((ArrayList<ProductsModel>)products);
		priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
		priceInquiryInput.setRequiredAvailabilty("Y");
		priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
		priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
		priceInquiryInput.setSession(session);
		products = priceInquiry.priceInquiry(priceInquiryInput , (ArrayList<ProductsModel>)products);
		return getProductsAsMap(products);		
	}
	@Override	
	public void addNonCimmItemsToCart(List<ProductsModel> items, HttpSession session) {
		if(items != null && items.size() > 0) {
			int UserId = CommonUtility.validateNumber(session.getAttribute(Global.USERID_KEY).toString());
			persistNonCimmItems(UserId, session.getId(), items);
		}
	}
	
	@Override
	public void configureCreditCardDetails(Cimm2BCentralCustomerCard creditCardDetails) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREDIT_CARD_FLAG")).trim().length() > 0) {
			creditCardDetails.setCreditCardNumber(CommonDBQuery.getSystemParamtersList().get("CREDIT_CARD_FLAG"));
		}
	}
	
	@Override
	public void mergeRequiredFields(ProductsModel item,ResultSet rs) throws SQLException {
		
		if(CommonUtility.validateString(item.getCatalogId()).equals("NPNS")) {
			item.setPartNumber(rs.getString("NC_PART_NUMBER"));
			item.setManufacturerPartNumber(rs.getString("MANUFACTURER"));
			item.setManufacturerName(rs.getString("MANUFACTURER"));
		}else if(CommonUtility.validateString(item.getCatalogId()).equals("QUOTE_ITEM")) {
			item.setOverRidePriceRule("Y");
			item.setPartNumber(rs.getString("PART_NUMBER"));
			item.setUnitPrice(rs.getDouble("PRICE"));
			item.setPrice(rs.getDouble("PRICE"));
			item.setExtendedPrice(rs.getDouble("PRICE") * rs.getDouble("QTY"));
			item.setTotal(item.getExtendedPrice());
		}
		try {
			if(CommonUtility.validateString(item.getShortDesc()).length() <= 0) {
				item.setShortDesc(rs.getString("SHORT_DESCRIPTION"));
			}
			if(rs.findColumn("ORDER_QUOTE_NUMBER")>0) {
				item.setOrderOrQuoteNumber(rs.getString("ORDER_QUOTE_NUMBER"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean isRequired(ProductsModel item, int orderId) {
		if(CommonUtility.validateString(item.getPartNumber()).startsWith("00000") && (CommonUtility.validateString(item.getCatalogId()).equals("NPNS") || (CommonUtility.validateString(item.getCatalogId()).equals("QUOTE_ITEM") && orderId <= 0))) {
			return false;
		}
		return true;
	}
	
	@Override
	public void addCartDetailsToQuickOrderPad(int userId, List<ProductsModel> products, Map<String, Object> contentObject) {
		if(products!=null && products.size() > 0) {
			contentObject.put("cartItemsAsMap", getCartItemsId(userId));
		}
	}
	
	@Override
	public void cartRelatedAddOns(List<ProductsModel> items, Map<String, Object> contentObject) {
		contentObject.put("hasCatalogItems", hasCataLogItems(items));
	}
	
	@Override
	public ArrayList<ProductsModel> extractCatalogOrCimmItems(ArrayList<ProductsModel> items) {
		ArrayList<ProductsModel> catalogItems = new ArrayList<>();
		for(ProductsModel item : items) {
			if(!CommonUtility.validateString(item.getCatalogId()).equals("NPNS") || !item.getPartNumber().startsWith("00000")) {
				catalogItems.add(item);
			}
		}
		return catalogItems;
	}
	
	@Override
	public void notifyNonCatalogItemsToSalesRep(List<ProductsModel> nonCatalogItems, HttpSession session, SalesModel externalOrderDetails) {
		String salesRepId = "";
		if(session.getAttribute("salesRepEmailId") != null) {
			salesRepId = session.getAttribute("salesRepEmailId").toString();
		}
		if(nonCatalogItems!= null && nonCatalogItems.size() > 0) {
			try {
					String subject;
					LinkedHashMap<String, String> notificationDetails = new SaveCustomFormDetails().getNotificationDetails("NonCatalogOrderDetailsToSalesRep");
					String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					if(notificationDetails!=null && notificationDetails.get("FROM_EMAIL")!=null){
						fromEmail = notificationDetails.get("FROM_EMAIL");
					}
					VelocityContext context = new VelocityContext();
					subject = SendMailUtility.propertyLoader("order.nonstock.noncimm.subject.notify.salesrep", "") +"- #"+externalOrderDetails.getOrderNum();
					context.put("WelcomeText",CommonDBQuery.getSystemParamtersList().get("MAIL_WELCOME_MSG"));
					context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
					context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
					context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
					context.put("locale",LayoutLoader.getMessageProperties().get("EN"));
					context.put("nonCatalogItems", nonCatalogItems);
					context.put("salesRepDetails", session.getAttribute("salesRepDetails"));
					context.put("session", session);
					context.put("externalOrderDetails", externalOrderDetails);
					StringWriter writer = new StringWriter();
			        if(notificationDetails!=null && notificationDetails.get("DESCRIPTION")!=null) {
			        	try {
							Velocity.evaluate(context, writer, "", notificationDetails.get("DESCRIPTION"));
						} catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException | IOException e) {
							e.printStackTrace();
						}
			        }
			        StringBuilder finalMessage= new StringBuilder();
			        finalMessage.append(writer.toString());
			        Map<String, List<String>> allRecipients = RegistrationUtils.extractRecipientsFromNotification(notificationDetails);
			        if(allRecipients != null) {
			        	List<String> toMailIds = allRecipients.get("TO");
			        	if(toMailIds != null && CommonUtility.validateString(salesRepId).length() > 0) {
			        		toMailIds.add(salesRepId);
			        	}
			        	else if(CommonUtility.validateString(salesRepId).length() > 0){
			        		toMailIds = new ArrayList<>();
			        		toMailIds.add(salesRepId);
			        		allRecipients.put("TO", toMailIds);
			        	}
			        		
			        }
			        RegistrationUtils.sendMail(notificationDetails, subject, fromEmail, finalMessage.toString(), allRecipients);
				}catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	@Override
	public void notifyNonCatalogItemsToSalesRep(Map<String, Object> contentObject, HttpSession session) {
		try {
			int userId = CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY));
			List<ProductsModel> items = (ArrayList<ProductsModel>) contentObject.get("productListData");
			notifyNonCatalogItemsToSalesRep(items,session, null);
			ProductsDAO.clearCart(userId);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<ProductsModel> extractNonCatalogItems(List<ProductsModel> items){
		return items.stream().filter(item -> CommonUtility.validateString(item.getCatalogId()).equals("NPNS")).collect(toList());
	}
	
	private boolean hasCataLogItems(List<ProductsModel> items) {
		boolean hasCatalogItems = false;
		if(extractCatalogOrCimmItems((ArrayList<ProductsModel>) items).size() > 0) {
			hasCatalogItems = true;
		}
		return hasCatalogItems;
	}
	
	private boolean hasCustomFieldSet(List<ProductsModel> products, String fieldName) {
		boolean status = false;
		for(ProductsModel product : products) {
			Map<String, Object> customFieldVal = product.getCustomFieldVal();
			if(customFieldVal !=null && customFieldVal.containsKey(fieldName)){
				status = true;
				break;
			}
		}
		return status;
	}
	
	private boolean doesCartHasRestrcitedItems(List<ProductsModel> products) {
		double maxCartWeight = Double.parseDouble(CommonDBQuery.getSystemParamtersList().get("MAXIMUM_CART_WEIGHT_UPS"));
		boolean hasRectricted = false;
		for(ProductsModel product : products){
			if(product.getWeight() > maxCartWeight) {
				hasRectricted = true;
				break;
			}else {
				Map<String, Object> customFieldVal = product.getCustomFieldVal();
				if(customFieldVal !=null && (customFieldVal.containsKey("custom_FEDEX_RESTRICTED") || customFieldVal.containsKey("custom_UPS_RESTRICTED"))) {
					if(customFieldVal.get("custom_FEDEX_RESTRICTED") != null && customFieldVal.get("custom_FEDEX_RESTRICTED").toString().equals("Y")){
						hasRectricted = true;
					}
					else if(customFieldVal.get("custom_UPS_RESTRICTED") != null && customFieldVal.get("custom_UPS_RESTRICTED").toString().equals("Y")){
						hasRectricted = true;
					}
					if(hasRectricted) {
						break;
					}
				}
			}
			
		}
		return hasRectricted;
	}
	
	private List<ShipVia> filter(List<ShipVia> shipVias){
		List<ShipVia> eligibleShipVias = new ArrayList<>();
		for(ShipVia shipVia : shipVias) {
			if(!shipVia.getServiceProvider().equalsIgnoreCase("UPS") && !shipVia.getServiceProvider().equalsIgnoreCase("FEDEX")) {
				eligibleShipVias.add(shipVia);
			}
		}
		return eligibleShipVias;
	}
	
	private static Map<Integer, String> combineCustomerPartNumbers(Map<Integer, ArrayList<ProductsModel>> customerPartNumbers){
		Map<Integer, String> combinedCpnList = new HashMap<>();
		for(Map.Entry<Integer, ArrayList<ProductsModel>> entry : customerPartNumbers.entrySet()) {
			combinedCpnList.put(entry.getKey(), StringUtils.join(extractPartsFromProductsModel(entry.getValue()), ","));
		}
		return combinedCpnList;
	}
	
	private static List<String> extractPartsFromSalesModel(List<SalesModel> orderedItems){
		return orderedItems.stream().map(
                item -> item.getAltPartNumber() != null ? item.getAltPartNumber() : item.getPartNumber()
            ).collect(toList());
	}
	
	private static List<String> extractPartsFromProductsModel(List<ProductsModel> products){
		List<String> partNumbers = new ArrayList<>();
		for(ProductsModel item : products) {
			partNumbers.add(item.getPartNumber());
		}
		return partNumbers;
	}
	
	private static Map<String, ProductsModel> getProductsAsMap(List<ProductsModel> products){
		Map<String, ProductsModel> productsAsMap = new HashMap<>();
		for(ProductsModel product : products) {
			productsAsMap.put(product.getPartNumber(), product);
		}
		return productsAsMap;
	}
	
	private static List<ProductsModel> transformSalesToProductsModel(List<SalesModel> salesItems){
		List<ProductsModel> products = new ArrayList<>();
		for(SalesModel salesItem : salesItems) {
			ProductsModel product = new ProductsModel();
			product.setPartNumber(salesItem.getPartNumber());
			product.setQty(salesItem.getOrderQty());
			products.add(product);
		}
		return products;
	}
	
	private static int persistNonCimmItems(int userId, String sessionId, List<ProductsModel> items) {
		int count = -1;
		int siteId = 0;
		int defaultCimmItemId = Integer.parseInt(CommonDBQuery.getSystemParamtersList().get("DEFAULT_ITEM_ID"));
		String query = "INSERT INTO CART (CART_ID,ITEM_ID,QTY,SESSIONID,USER_ID,UPDATED_DATETIME,SITE_ID,LINE_ITEM_COMMENT, CATALOG_ID,UOM, PRICE, MIN_ORDER_QTY, ITEM_WEIGHT, UNITS,MANUFACTURER,PART_NUMBER, ORDER_QUOTE_NUMBER, SHORT_DESCRIPTION) VALUES(CART_ID_SEQ.NEXTVAL,?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection  conn = null;
		PreparedStatement pstmt = null;
		
		if(CommonDBQuery.getGlobalSiteId()>0){
    		siteId = CommonDBQuery.getGlobalSiteId();
		}
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(query);
			
			for(ProductsModel item : items) {
				pstmt.setInt(1, defaultCimmItemId);
				pstmt.setInt(2, item.getQty());
				pstmt.setString(3, sessionId);
				pstmt.setInt(4, userId);
				pstmt.setInt(5, siteId);
				pstmt.setString(6, item.getLineItemComment());
				pstmt.setString(7, "NPNS");
				if(CommonUtility.validateString(item.getUom()).length()==0){
					pstmt.setString(8," ");	
				}else{
					pstmt.setString(8, CommonUtility.validateString(item.getUom()));	
				}
				pstmt.setString(9, String.valueOf(item.getPrice()));
				pstmt.setInt(10, item.getMinOrderQty() > 0 ? item.getMinOrderQty() : 1);
				pstmt.setDouble(11, item.getWeight());
				pstmt.setDouble(12, 0);
				pstmt.setString(13, item.getManufacturerPartNumber());
				pstmt.setString(14, item.getPartNumber());
				pstmt.setString(15, item.getOrderOrQuoteNumber());
				pstmt.setString(16, item.getShortDesc());
				pstmt.addBatch();
			}
			int[] executeBatch = pstmt.executeBatch();
			if(executeBatch.length >= 0) {
				count = 1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}
	
	private static Map<Integer, JsonObject> getCartItemsId(int userId) {
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    Map<Integer, JsonObject> cartItems = new HashMap<>();
        try
        {
        	int siteId = 0;
    		if(CommonDBQuery.getGlobalSiteId()>0){
        		siteId = CommonDBQuery.getGlobalSiteId();
    		}
        	conn = ConnectionManager.getDBConnection();
			String query = "SELECT CART_ID, ITEM_ID, QTY from CART where USER_ID = ? AND SITE_ID = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, siteId);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				JsonObject obj = new JsonObject();
				obj.addProperty("cartItemId", rs.getInt("CART_ID"));
				obj.addProperty("itemId", rs.getInt("ITEM_ID"));
				obj.addProperty("qty", rs.getInt("QTY"));
				cartItems.put(rs.getInt("ITEM_ID"), obj);
			}
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
        	ConnectionManager.closeDBConnection(conn);
        }
        return cartItems;
	}
			
	@SuppressWarnings("unchecked")
	private Map<String, String> extractSalesUserDetails(HttpSession session){
		return (Map<String, String>) session.getAttribute("salesUserDetails");
	}
	
	private List<JsonObject> getCustomers(int userId, Map<String, String> saleUserDetails){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		boolean isSalesAdmin = false;
		int excludedCustomerId = Integer.parseInt(saleUserDetails.get("buyingCompanyId"));
		String query = "";
		if(saleUserDetails.get("isSalesAdmin") != null && CommonUtility.validateString(saleUserDetails.get("isSalesAdmin")).equals("Y")) {
			isSalesAdmin = true;
		}
		if(isSalesAdmin) {
			query = "select BUYING_COMPANY_ID, CUSTOMER_NAME, ENTITY_ID, ADDRESS1, STATE, CITY, ZIP, COUNTRY from BUYING_COMPANY where STATUS = 'A' AND BUYING_COMPANY_ID != 1 AND BUYING_COMPANY_ID !=? AND ENTITY_ID IS NOT NULL";
		}else {
			query = "SELECT BUYING_COMPANY_ID, CUSTOMER_NAME, ENTITY_ID, ADDRESS1, STATE, CITY, ZIP, COUNTRY FROM BUYING_COMPANY BC, SALES_REP_COMPANIES SRC WHERE BC.STATUS = 'A' AND BC.BUYING_COMPANY_ID = SRC.CUSTOMER_ID AND SRC.USER_ID = ? AND BC.BUYING_COMPANY_ID != 1 AND BC.BUYING_COMPANY_ID != ? AND BC.ENTITY_ID IS NOT NULL";
		}
		
		List<JsonObject> customers = new ArrayList<>();
		try {
			connection = ConnectionManager.getDBConnection();
			statement = connection.prepareStatement(query);
			
			if(isSalesAdmin) {
				statement.setInt(1, excludedCustomerId);
			}else {
				statement.setInt(1, userId);
				statement.setInt(2, excludedCustomerId);
			}
			
			results = statement.executeQuery();
			while(results.next()) {
				JsonObject customer = new JsonObject();
				customer.addProperty("customerId", results.getInt("BUYING_COMPANY_ID"));
				customer.addProperty("customerName", results.getString("CUSTOMER_NAME"));
				customer.addProperty("accountNumber", results.getString("ENTITY_ID"));
				customer.addProperty("address1", results.getString("ADDRESS1"));
				customer.addProperty("state", results.getString("STATE"));
				customer.addProperty("city", results.getString("CITY"));
				customer.addProperty("zipCode", results.getString("ZIP"));
				customer.addProperty("country", results.getString("COUNTRY"));
				customers.add(customer);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
        	ConnectionManager.closeDBPreparedStatement(statement);	
        	ConnectionManager.closeDBResultSet(results);
        	ConnectionManager.closeDBConnection(connection);
        }
		return customers;
	}
	
	private List<JsonObject> getUsersByCustomer(int customerId, String accountNumber){
		List<JsonObject> users = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		try {
			String query = "select USER_ID, USER_NAME, FIRST_NAME, LAST_NAME, ADDRESS1, ADDRESS2, CITY, STATE, COUNTRY, ZIP, EMAIL, ECLIPSE_CONTACT_ID, BUYING_COMPANY_ID from CIMM_USERS WHERE BUYING_COMPANY_ID = ? and status = 'Y'";
			connection = ConnectionManager.getDBConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, customerId);
			results = statement.executeQuery();
			while(results.next()) {
				JsonObject user = new JsonObject();
				user.addProperty("userId", results.getInt("USER_ID"));
				user.addProperty("customerId", results.getInt("BUYING_COMPANY_ID"));
				user.addProperty("userName", results.getString("USER_NAME"));
				user.addProperty("firstName", results.getString("FIRST_NAME"));
				user.addProperty("lastName", results.getString("LAST_NAME"));
				user.addProperty("address1", results.getString("ADDRESS1"));
				user.addProperty("address2", results.getString("ADDRESS2"));
				user.addProperty("city", results.getString("CITY"));
				user.addProperty("state", results.getString("STATE"));
				user.addProperty("country", results.getString("COUNTRY"));
				user.addProperty("zipCode", results.getString("ZIP"));
				user.addProperty("email", results.getString("EMAIL"));
				user.addProperty("accountNumber", results.getString("ECLIPSE_CONTACT_ID"));
				users.add(user);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
        	ConnectionManager.closeDBPreparedStatement(statement);	
        	ConnectionManager.closeDBResultSet(results);
        	ConnectionManager.closeDBConnection(connection);
        }
		return users;
	}
	
	@Override
	public boolean makeAsDefaultShipTo(HttpServletRequest request) {
		boolean status = false;
		HttpSession session = request.getSession();
		int userId = Integer.parseInt(session.getAttribute(Global.USERID_KEY).toString());
		int shipToAddressBookId = Integer.parseInt(request.getParameter("shipToAddressBookId"));
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			String query = "UPDATE CIMM_USERS SET DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE USER_ID=?";
			connection = ConnectionManager.getDBConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, shipToAddressBookId);
			statement.setInt(2, userId);
			int count = statement.executeUpdate();
			if(count > 0) {
				status = true;
				session.setAttribute("defaultShippingAddressId", CommonUtility.validateParseIntegerToString(shipToAddressBookId));
				session.setAttribute("defaultShipToId", CommonUtility.validateParseIntegerToString(shipToAddressBookId));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.closeDBPreparedStatement(statement);
			ConnectionManager.closeDBConnection(connection);
		}
		return status;
	}
	
	private boolean validateQuoteEligibility(HttpServletRequest request) {
		int eligibleDays = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("QUOTE_ELIGIBLE_DAYS"));
		boolean status = false;
		String quotedDate = request.getParameter("orderedDate");
		String orderType = request.getParameter("orderType");
		if(CommonUtility.validateString(orderType).equalsIgnoreCase("QUOTE")) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				long diffInDays = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(quotedDate, formatter));
				if(Math.abs(diffInDays) <= eligibleDays) {
					status = true;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return status;
	}
	
	public void filterMyCatalogGroups(Map<String, Object> contentObject, HttpServletRequest request) {
		
		ArrayList<ProductsModel> groupListData = (ArrayList<ProductsModel>) contentObject.get("groupListData");
		
		if(CommonUtility.validateString(request.getParameter("groupType")).equals("MC")) {
			contentObject.put("groupType", "MC");
			groupListData = groupListData.stream()
					.filter(g -> g.getProductListName().toUpperCase().startsWith("MYCATALOG_"))
					.collect(toCollection(ArrayList::new));
		}else {
			groupListData = groupListData.stream()
			.filter(g -> !g.getProductListName().toUpperCase().startsWith("MYCATALOG_"))
			.collect(toCollection(ArrayList::new));
			
		}
		contentObject.put("groupListData", groupListData);
	}
	
	@Override
	public String setCatalogIdForQuotedItem(HttpServletRequest request) {
		String catalogId = "";
		if(validateQuoteEligibility(request)) {
			catalogId = "QUOTE_ITEM";
		}
		return catalogId;
	}
	
	public String extractAssignedRole(List<String> descriptions) { 
		String currentRole = "";
		for(String desc : descriptions) {
			int beginIndex = desc.indexOf("Ecomm");
			if(beginIndex >= 0) {
				currentRole = desc.substring(beginIndex);
				break;
			}
		}
		return currentRole; 
	}
	
	public Cimm2BCentralAddress billAddressExist(Cimm2BCentralAddress address,Cimm2BCentralCustomer customerDetails){
		if(!CommonUtility.doesBillAddressExist(address)) {
			if(customerDetails.getCustomerLocations() != null && customerDetails.getCustomerLocations().size() > 0) {
				address = customerDetails.getCustomerLocations().get(0).getAddress();
			}
			
		}
		return address;
	}
	
	public void setShipViaDescription(SalesOrderManagementModel salesOrderInput,Cimm2BCentralShipVia cimm2bCentralShipVia){
		cimm2bCentralShipVia.setShipViaDescription(salesOrderInput.getShipViaMethod());
	}
	public String setRejectOrderMailSubject(String orderStatus,String cartApprovalMailSubject) {
		if(CommonUtility.validateString(orderStatus).length()>0 && orderStatus.toUpperCase().equalsIgnoreCase("REJECTED")) {
			cartApprovalMailSubject="sentmailconfig.sendApprovalRejectedMail.subject"; 
		}
		return cartApprovalMailSubject;
	}
}
