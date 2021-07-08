package com.unilog.services.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;

import com.unilog.services.*;
import com.erp.service.cimm2bcentral.models.PackageInfo;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerCard;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipVia;
import com.erp.service.model.ProductManagementModel;
import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.custommodule.model.FreightCalculatorModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.SendMailModel;
import com.unilog.defaults.SendMailUtility;
import com.unilog.misc.EventModel;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.users.AddressModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPriceBreaks;

public class BraasCoCustomServices implements UnilogFactoryInterface {

private static UnilogFactoryInterface serviceProvider;
	
	private BraasCoCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (BraasCoCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new BraasCoCustomServices();
				}
			}
		return serviceProvider;
	}
	
	
	
	@Override
	public LinkedHashMap<String, ArrayList<ProductsModel>> getGroupedItemsInProductsData(
			ArrayList<ProductsModel> productListData) {
		
		return null;
	}

	@Override
	public LinkedHashMap<String, ArrayList<SalesModel>> getGroupedItemsInSalesData(
			ArrayList<SalesModel> productListData) {
		
		return null;
	}

	@Override
	public double getGroupOfTax(LinkedHashMap<String, Object> salesInputParameter, String customParamter) {
		
		return 0;
	}

	@Override
	public LinkedHashMap<String, Cimm2BCentralShipVia> getCustomShipViaData(ArrayList<ProductsModel> orderDetails) {
		
		return null;
	}

	@Override
	public void applyShipViaRestriction(List<ProductsModel> products, Map<String, Object> contentObject) {
		
		
	}

	@Override
	public double verifyAndExtractShippingCost(ShipVia shipVia, String shipToZipCode, String billToZipCode) {
		
		return 0;
	}

	@Override
	public void sessionValue() {
		
		
	}

	@Override
	public void getOrderedItemsDetails(Map<String, Object> contentObject) {
		
		
	}

	@Override
	public Map<String, ProductsModel> getRequestForQuoteDetails(List<ProductsModel> products, int subsetId,
			int generalSubsetId) {
		
		return null;
	}

	@Override
	public List<PackageInfo> getItemWeights(List<ProductsModel> products) {
		
		return null;
	}

	@Override
	public Map<String, ProductsModel> loadPrice(List<SalesModel> salesItems, HttpSession session) {
		
		return null;
	}

	@Override
	public void configureCreditCardDetails(Cimm2BCentralCustomerCard creditCardDetails) {
		
		
	}

	@Override
	public int getFirstOrders(SalesModel salesInputParameter) {
		
		return 0;
	}

	@Override
	public boolean sendFirstOrderMail(SalesModel erpOrderDetail, SendMailModel sendMailModel, int firstOrder) {
		
		return false;
	}

	@Override
	public Cimm2BCentralLineItem submitSalesOrderItemList(Cimm2BCentralLineItem cimm2bCentralLineItem) {
		
		return null;
	}

	@Override
	public String getExpectedDeliveryDate(String date) {
		
		return null;
	}

	@Override
	public Cimm2BCentralAddress addcustomFieldData(String userName, AddressModel shipAddress,
			AddressModel billAddress) {
		
		return null;
	}

	@Override
	public ArrayList<UsersModel> addShipAddressList(ArrayList<UsersModel> shipAddressList) {
		
		return null;
	}

	@Override
	public SalesModel orderdetail(ArrayList<Cimm2BCentralLineItem> ordersList) {
		
		return null;
	}

	@Override
	public UsersModel contactInfo(AddressModel addressInfo, UsersModel billingAddress, UsersModel contactInformation,
			UsersModel shippingAddress) {
		
		return null;
	}

	@Override
	public UsersModel billShipAddressInsert(UsersModel billShipInfo, String type) {
		
		return null;
	}

	@Override
	public String fetchItemId(int subset) {
		
		return null;
	}

	@Override
	public SalesOrderManagementModel salesOrderdetail(SalesOrderManagementModel salesOrderInput, String shipTofirstName,
			String shipToLastName, String pageTitle, String sequencePoNum, UsersModel userShipAddress) {
		
		return null;
	}

	@Override
	public ProductsModel orderItem(ProductsModel itmVal, String pageTitle, String listPrice, String carton) {
		
		return null;
	}

	@Override
	public String sendShipAddressFile(UsersModel shipAddressInfo, String result, String files, String addFirstName) {
		
		return null;
	}

	@Override
	public String setDefaultAddress(String shipToName, String addressType, UsersModel userDefaultAddress) {
		
		return null;
	}

	@Override
	public String partialUserLogin(UsersModel enteredUserNameInfo) {
		
		return null;
	}

	@Override
	public String getPoSequenceId(String seqName) {
		
		return null;
	}

	@Override
	public boolean sendMailRegistration(UsersModel userDetailsAddress, String formtype, String whomTo,
			String SuperUserEmail) {
		
		return false;
	}

	@Override
	public LinkedHashMap<String, String> getFirstOrderNotification() {
		
		return null;
	}

	@Override
	public String addContextObject(SalesModel orderDetail, VelocityContext context) {
		return null;
	}

	@Override
	public LinkedHashMap<String, String> redefinedWarehouseList() {
		LinkedHashMap<String,String> wareHouseList  = CommonDBQuery.getActivewareHouseList();
		LinkedHashMap<String,String> activeWarehouseCodes = new LinkedHashMap<String,String>();
		if(wareHouseList!=null && wareHouseList.size()>0){
			for(Map.Entry<String,String> entry : wareHouseList.entrySet()){
				activeWarehouseCodes.put(entry.getKey(), entry.getValue());
			}
		}
		return activeWarehouseCodes;		
	}

	@Override
	public LinkedHashMap<String, ProductsModel> activeWarehouseList() {
		
		return null;
	}

	@Override
	public String getNetworkWarehouseCode(String warehouseCode) {
		
		return null;
	}

	@Override
	public String getUom(double salesQty) {
		
		return null;
	}

	@Override
	public double getExtendedPrice(double price, int orderqty, int salesQty, double salesQuntityUom) {
		
		return 0;
	}

	@Override
	public boolean doTwoStepValidation(Cimm2BCentralCustomer customerDetails, String invoiceNo, String poNumber,
			String zipCode) {
		
		return false;
	}

	@Override
	public LinkedHashMap<String, String> LoadNetworkWareHouseList() {
		
		return null;
	}

	@Override
	public LinkedHashMap<String, String> LoadactiveWareHouseList() {
		
				LinkedHashMap<String, String> activewareHouseList= new LinkedHashMap<String, String>();
				List<CustomTable> wareHouseList  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","ACTIVE_WAREHOUSE");
				if(wareHouseList!=null && wareHouseList.size()>0){
				for(CustomTable warehouse:wareHouseList){
						for(Map<String, String> each : warehouse.getTableDetails()){
								activewareHouseList.put( CommonUtility.validateString(each.get("WAREHOUSE_CODE")), CommonUtility.validateString(each.get("WAREHOUSE_NAME")));
						}
					}
				}
				return activewareHouseList;
	}

	@Override
	public void getClusterdata() {
		
		
	}

	@Override
	public void getShipviaZipList() {
		
		
	}

	@Override
	public void getSalesRepDetails() {
		
		
	}

	@Override
	public String getSalesRepEmailDetail(String SManID) {
		
		return null;
	}

	@Override
	public List<String> getSalesRepDetailbywarehouse(String branchcode) {
		
		return null;
	}

	@Override
	public void insertCustomertarget(String brabchid, int userid, int buyingCompanyid) {
		
		
	}

	@Override
	public String getShipToStoreWarehouseCode(String warehouse) {
		
		return null;
	}

	@Override
	public String getCustomShipViaDescription(HttpSession session, Cimm2BCentralShipVia orderResponseGetShipVia) {
		
		return null;
	}

	@Override
	public int getItemsResultCount(String keyWord, int subsetId, int generalSubset, int fromRow, int toRow,
			String requestType, int psid, int npsid, int treeId, int levelNo, String attrFilterList, String brandId,
			String sessionId, String sortBy, int resultPerPage, String narrowKeyword, int buyingCompanyId,
			String userName, String userToken, String entityId, int userId, String homeTeritory, String type,
			String wareHousecode, String customerId, String customerCountry, boolean isCategoryNav, boolean exactSearch,
			String viewFrequentlyPurcahsedOnly, String clearanceFlag, String wareHouseItems) {
		
		return 0;
	}

	@Override
	public int checkItemsFordifferentLocation(int userId, String sessionId, HttpSession session, String flag) {
		
		return 0;
	}

	@Override
	public String addressLine1Combined(AddressModel address) {
		
		return null;
	}

	@Override
	public void getPrice(Cimm2BCentralItem item,ProductsModel product) {
		double price=0.0;
		if(item!=null && item.getPricingWarehouse()!=null) {
			if(CommonUtility.validateInteger(product.getPrice())<item.getPricingWarehouse().getCustomerPrice()) {
				price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(product.getPrice()));
			}
			else {
				price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(item.getPricingWarehouse().getCustomerPrice() - (item.getPricingWarehouse().getCustomerPrice()*item.getPricingWarehouse().getDiscountAmount()/100)));
			}
			product.setPrice(price);
		}
	}
	public void getUnitPrice(ProductsModel eclipseitemPrice,ProductsModel itemPrice) {
		itemPrice.setUnitPrice(eclipseitemPrice.getPrice());
	}
	public double getdefaultCustomerPrice(ProductsModel eclipseitemPrice,ProductsModel itemPrice,double price) {
		double defaultCustprice = price + eclipseitemPrice.getPrice();
		return defaultCustprice;
	}
	public void getTotalForDefault(ProductsModel itemPrice,double price) {}
	
	public void overRideERPID(UsersModel addressModel, Cimm2BCentralAddress cimm2bCentralBillingAddress)
	{
		cimm2bCentralBillingAddress.setAddressERPId(addressModel.getShipToId());
	}
	public void overRideERPIDRequired(AddressModel addressModel, Cimm2BCentralAddress cimm2bCentralBillingAddress)
	{
		cimm2bCentralBillingAddress.setAddressERPId(addressModel.getShipToId());
	}
	public void setDefaultWareHouseFromERP(UsersModel customerinfo, Cimm2BCentralCustomer customerDetails)
	{
		if(customerDetails.getDefaultShipLocationId()!=null && customerDetails.getDefaultShipLocationId().length()>0){
			customerinfo.setDefaultShipLocationId(customerDetails.getDefaultShipLocationId());
		}
		if(customerDetails.getDefaultShipVia()!=null && customerDetails.getDefaultShipVia().length()>0){
			customerinfo.setDefaultShipVia(customerDetails.getDefaultShipVia());
		}
		if(customerDetails.getHomeBranch()!=null && customerDetails.getHomeBranch().length()>0){
			customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(customerDetails.getHomeBranch())));
			customerinfo.setWareHouseCodeStr(CommonUtility.validateString(customerDetails.getHomeBranch()));
		}else{
			customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR"))));
			customerinfo.setWareHouseCodeStr(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR")));	
		}
	}
	public void getUnitPriceofOrders(SalesModel sales,Cimm2BCentralLineItem item,Cimm2BCentralOrder order)
	{
		if(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID")!=null && order.getCustomerERPId()!="" && order.getCustomerERPId().equalsIgnoreCase(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID")))
		{
			sales.setUnitPrice(item.getUnitPrice());
		}else{
			if(item.getQty()>1)
			{
				sales.setUnitPrice(item.getExtendedPrice()/item.getQty());
			}else{
				sales.setUnitPrice(item.getExtendedPrice());
			}
		}
	}
	public void UniquePoNumberHide(Cimm2BCentralOrder order)
	{
	}
	public String  getcategoryName(ArrayList<ProductsModel> itemLevelFilterData,String categoryName)
	{
		categoryName="";
		categoryName = itemLevelFilterData.get(0).getManufacturerPartNumber()+" "+itemLevelFilterData.get(0).getBrandName();
        return categoryName;
	}
	public void overRideAddressID(UsersModel addressModel, Cimm2BCentralAddress cimm2bCentralBillingAddress){
		cimm2bCentralBillingAddress.setAddressId("");
		}
	public void overRideAddressID(AddressModel addressModel, Cimm2BCentralAddress cimm2bCentralBillingAddress){
		cimm2bCentralBillingAddress.setAddressId("");

	};	
	public void getPriceBranch(Cimm2BCentralItem item,ProductsModel branchModel) {
		String entityID = "";
		HttpServletRequest request =ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        if(session!=null && (String)session.getAttribute("customerId")!=null) {
			entityID= (String)session.getAttribute("customerId");
		}
		double price=0.0;
		if(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID")!=null && entityID!=null && entityID.equalsIgnoreCase(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID"))){
			price=item.getPricingWarehouse().getCustomerPrice()!=null?item.getPricingWarehouse().getCustomerPrice():0.0;
		}else{
			price=item.getPricingWarehouse().getExtendedPrice()!=null?item.getPricingWarehouse().getExtendedPrice():0.0;
		}
		branchModel.setPrice(price);
	}
	private void setMaxQtyBreak(List<ProductsModel> qtyBreaks) {
		int noOfBreaks = qtyBreaks.size();
		for(int i = 0; i < qtyBreaks.size(); i++) {
			ProductsModel eachBreak = qtyBreaks.get(i);
			if((i + 1) < noOfBreaks) {
				eachBreak.setMaximumQuantityBreak(qtyBreaks.get(i+1).getMinimumQuantityBreak() - 1);
			}
		}
	}
	
	public void erpQuanitityBreakUpdate(ProductsModel productsModel, ProductsModel pricingResponse) {
		if(pricingResponse.getQuantityBreakList()!=null && pricingResponse.getQuantityBreakList().size()>0) {
		setMaxQtyBreak(pricingResponse.getQuantityBreakList());
		Optional<ProductsModel> quantityBreakPrice = pricingResponse.getQuantityBreakList().stream()
				.filter(qb -> quantityBreakRange(productsModel.getQty(), qb))
				.findFirst();
		if (quantityBreakPrice.isPresent() &&  quantityBreakPrice.get().getCustomerPriceBreak() > 0) {
			double price = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(quantityBreakPrice.get().getCustomerPriceBreak()));
			productsModel.setPrice(price);
			pricingResponse.setPrice(price);
		}else {
			double price = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(pricingResponse.getCimm2BCentralPricingWarehouse().getCustomerPrice()));
			productsModel.setPrice(price);
			pricingResponse.setPrice(price);
		}
		}
	}
	private boolean quantityBreakRange(int qty, ProductsModel quantityBreak) {
		boolean status = false;
		if (quantityBreak.getMaximumQuantityBreak() > 0) {
            if (qty >= quantityBreak.getMinimumQuantityBreak() && qty <= quantityBreak.getMaximumQuantityBreak()) {
            	status = true;
            }
        } else {
            if (qty >= quantityBreak.getMinimumQuantityBreak()) {
            	status = true;
            }
        }
		return status;
	}
	@Override
	public void quanitityBreakCalculation(ProductsModel productsModel, Cimm2BCentralPriceBreaks priceBreaks) {}
	@Override
	public void quanitityBreakCalculationwithDiscount(Cimm2BCentralItem item, ProductsModel productsModel, Cimm2BCentralPriceBreaks priceBreaks) {
		if(priceBreaks.getDiscountAmount()>0) {	
		Double price = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(item.getPricingWarehouse().getCustomerPrice()- (item.getPricingWarehouse().getCustomerPrice()*(priceBreaks.getDiscountAmount()/100))));
		priceBreaks.setCustomerPrice(price);
		}
		else {
			Double price = productsModel.getPrice();
			priceBreaks.setCustomerPrice(price);	
		}		
	}
	public EventModel getEventRegistrationDetails(int eventRegId){
		String sql = "select EM.EVENT_TITLE,EM.COST,EM.EVENT_CATEGORY, ER.FIRST_NAME,ER.EVENT_ID,ER.LAST_NAME,ER.COMPANY_NAME,ER.EMAIL,ER.CONTACT_MOBILE_PHONE,ER.PO_NUMBER,ER.PAYMENT_MODE from EVENT_MANAGER EM, EVENT_REGISTRATION ER where EM.EVENT_ID = ER.EVENT_ID AND EVENT_REG_ID=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		EventModel event = new EventModel();
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventRegId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				event.setTitle(rs.getString("EVENT_TITLE"));
	        	event.setCost(rs.getDouble("COST"));
	        	event.setFirstName(rs.getString("FIRST_NAME"));                      
	        	event.setEventId(rs.getInt("EVENT_ID"));                    
	        	event.setLastName(rs.getString("LAST_NAME")); 
	        	event.setCompanyName(rs.getString("COMPANY_NAME"));
	        	event.setEmail(rs.getString("EMAIL"));                            
	        	event.setPhoneNumber(rs.getString("CONTACT_MOBILE_PHONE")); 
	        	event.setPoNumber(rs.getString("PO_NUMBER"));
	        	event.setPaymentMode(rs.getString("PAYMENT_MODE"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
		}
		return event;
	}
	@Override
	public ArrayList<ProductsModel> getEventOrderDetails(SalesOrderManagementModel salesOrderInput) {
		ArrayList<Cimm2BCentralLineItem> itemDetails = salesOrderInput.getLineItems();
	    ArrayList<ProductsModel> orderDetails = new ArrayList<ProductsModel>();	
		ProductsModel orderItemVal = new ProductsModel();
		try {
			orderItemVal.setItemId(CommonUtility.validateNumber(itemDetails.get(0).getItemId()));
			orderItemVal.setPartNumber(itemDetails.get(0).getPartNumber());
			orderItemVal.setShortDesc(itemDetails.get(0).getShortDescription());
			orderItemVal.setQty(itemDetails.get(0).getQty());
			orderItemVal.setPrice(itemDetails.get(0).getUnitPrice());
			orderItemVal.setListPrice(itemDetails.get(0).getListPrice());
			orderItemVal.setUom(itemDetails.get(0).getUom());
			orderItemVal.setLineItemComment(itemDetails.get(0).getLineItemComment());
			orderItemVal.setUomQty(CommonUtility.validateNumber(itemDetails.get(0).getUomQty()));				
			orderDetails.add(orderItemVal);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return orderDetails;		
	}
	public void dontaddfreightcharges(SalesModel quoteInfo,Cimm2BCentralOrder orderResponse) {
			 quoteInfo.setTotal(orderResponse.getOrderTotal());		 
	 }
	public boolean setSendMailToSalesRep(SalesModel erpOrderDetail, SendMailModel sendMailModel, boolean mailsent) {
		SendMailUtility sendMailUtility = new SendMailUtility();
		String mailSubject = "Review Order Mail";
		if(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString()).getProperty("review.orderMail.subject")!=null && LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString()).getProperty("review.orderMail.subject").length()>0) {
			mailSubject = LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString()).getProperty("review.orderMail.subject");
		}		
		sendMailModel.setMailSubject(mailSubject);
		sendMailModel.setReviewOrderMail(true);
		mailsent = sendMailUtility.sendOrderMail(erpOrderDetail, sendMailModel);
		sendMailModel.setReviewOrderMail(false);
		return mailsent;
	}
	@Override
	public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
	{	
		String newsLetterCustomFiledValue="";
		if(userDetailsInput.getNewsLetterSub()!=null && userDetailsInput.getNewsLetterSub().equals("Y")){
			newsLetterCustomFiledValue="Y";
		}else{
			newsLetterCustomFiledValue="N";
		}
		UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
	}
	public void shipAddressERPIdCity(Cimm2BCentralAddress shipToAddress, UsersModel shipAddressList) {
		shipToAddress.setAddressERPId(shipAddressList.getCity());
	}
	public void setOrderValuesFromQuoteResponse(SalesModel quoteResponse,LinkedHashMap<String, Object> contentObject){		
		double totalCartFrieghtCharges = quoteResponse.getFreight();
		double otherCharges = quoteResponse.getOtherCharges() + totalCartFrieghtCharges;
		totalCartFrieghtCharges = totalCartFrieghtCharges + otherCharges;		
		contentObject.put("totalCartFrieghtCharges",totalCartFrieghtCharges);		
	}
	
}
