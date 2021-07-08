package com.unilog.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrQuery;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerType;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipVia;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralWarehouse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BPriceAndAvailabilityRequest;
import com.erp.service.model.ProductManagementModel;
import com.erp.service.model.SalesOrderManagementModel;
import com.erp.service.model.UserManagementModel;
import com.paymentgateway.elementexpress.transaction.Address;
import com.unilog.custommodule.model.FreightCalculatorModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.sales.CIMMTaxModel;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;
import com.unilognew.util.ECommerceEnumType.AddressType;
import com.unilognew.util.ECommerceEnumType.RequestHandlers;

public class MacombGroupCustomServices implements UnilogFactoryInterface{
	
	private static UnilogFactoryInterface serviceProvider;
	private static List<CIMMTaxModel> cimmTaxTable = null;
	
	private MacombGroupCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (MacombGroupCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new MacombGroupCustomServices();
				}
			}
		return serviceProvider;
	}
	
	public static List<CIMMTaxModel> getCimmTaxTable() {
		return cimmTaxTable;
	}

	public static void setCimmTaxTable(List<CIMMTaxModel> cimmTaxTable) {
		MacombGroupCustomServices.cimmTaxTable = cimmTaxTable;
	}
	
	public List<CIMMTaxModel>  loadCIMMTaxTable(){
		List<CustomTable> customTable = null;
		List<Map<String, String>> cimmTaxTab = null;
		List<CIMMTaxModel> cimmTaxTable = null;
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TAX_FROM_CIMM")).equalsIgnoreCase("Y"))
		{
			cimmTaxTable = new ArrayList<CIMMTaxModel>();
			customTable = CIMM2VelocityTool.getInstance().getCusomTableData("Website","WEBSITE_TAX");	
		}
		if(customTable!=null && !customTable.isEmpty() && customTable.size() > 0){
			cimmTaxTab = new ArrayList<Map<String,String>>();
			cimmTaxTab = customTable.get(0).getTableDetails();				
		
		for (Map<String, String> taxTable : cimmTaxTab) {
			CIMMTaxModel model = new CIMMTaxModel();
			for(Map.Entry<String, String> entry : taxTable.entrySet()){
				
				
				if(entry.getKey().equalsIgnoreCase("START_ZIP")){
					model.setStartZip(entry.getValue());
				}
				if(entry.getKey().equalsIgnoreCase("END_ZIP")){
					model.setEndZip(entry.getValue());
				}
				if(entry.getKey().equalsIgnoreCase("JURISDICTION_IDS")){
					model.setJurisdictionId(entry.getValue());
				}
				if(entry.getKey().equalsIgnoreCase("TAX_RATE_PERCENTAGE")){
					model.setTaxPercentage(Double.parseDouble(entry.getValue()));
				}
			}
			cimmTaxTable.add(model);
	}
		}
		return cimmTaxTable;
	}
	
	public double getFrieghtCharges(String shipViaDisplay, HttpSession session, LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal, double totalCartFrieghtCharges, FreightCalculatorModel freightValue)
	{
		if(freightValue!=null && freightValue.getFreightValue()>0){
			totalCartFrieghtCharges =  totalCartFrieghtCharges + freightValue.getFreightValue();
		}
		return totalCartFrieghtCharges;
	}
	
	public void removeShipViaFromSession(HttpSession session) {
		if(session!=null && session.getAttribute("selectedShipVia")!=null) {
			session.removeAttribute("selectedShipVia");
		}
	}
	
	public void availWarehouse(String warehouseCode, Set<String> allWarehouseCodes) {
		if(warehouseCode!=null && CommonUtility.validateString(warehouseCode).equalsIgnoreCase("30300"))
		{
			allWarehouseCodes = new HashSet<>(allWarehouseCodes);   
			allWarehouseCodes.add(CommonDBQuery.getSystemParamtersList().get("AVAILABILITY_WAREHOUSE_FOR_30300"));
		}
	}
	
	public void setAvailQty(String warehouseCode, Cimm2BCentralWarehouse wareHouseDetail, ProductsModel productsModel) {	
		if(!CommonUtility.validateString(warehouseCode).equalsIgnoreCase("30300"))
		{
			productsModel.setBranchTotalQty(wareHouseDetail.getQuantityAvailable()!=null?wareHouseDetail.getQuantityAvailable().intValue():0);
		}
	}
	
	 public void availQtyUpdate(String warehouseCode, Cimm2BCentralWarehouse wareHouseDetail, ProductsModel productsModel,Cimm2BCentralItem item) {
		 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AVAILABILITY_WAREHOUSE_FOR_30300")).length()>0 
					&& item.getPricingWarehouse().getWarehouseCode().equals("30300")
					&& wareHouseDetail.getWarehouseCode().equals(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AVAILABILITY_WAREHOUSE_FOR_30300"))))
			{
			 productsModel.setBranchTotalQty(wareHouseDetail.getQuantityAvailable()!=null?wareHouseDetail.getQuantityAvailable().intValue():0);
			}
	 }
	 
	 public String getWillCallBranchSelected(String willCallBranchCode, HttpSession session) {
		 String willCallBranch="";
		 String willCallInfor="";
		 if(session.getAttribute("selectedBranchWillCall")!=null && session.getAttribute("selectedBranchWillCall").toString().trim().length()>0){
				String tempShip = (String) session.getAttribute("selectedBranchWillCall");
				String [] arry = tempShip.split("\\|");
				System.out.println("--array--"+arry.length);
				willCallBranch =arry[2];
				if(arry!=null && arry.length>3)
				willCallBranchCode=arry[3];
				willCallInfor =willCallBranchCode+":"+willCallBranch;
		 }
		 return willCallInfor; 
		 }
	 
	 public String setPricingDefaultWareHouseCode(String warehouseCode) {
			if(warehouseCode!=null && CommonUtility.validateString(warehouseCode).equalsIgnoreCase("30300")) {
				warehouseCode = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AVAILABILITY_WAREHOUSE_FOR_30300"));
			}
			return warehouseCode;
		}
	 
	 public void addDefaultWarehouseToItems(Cimm2BCentralLineItem cimm2bCentralLineItem, HttpSession session, SalesOrderManagementModel salesOrderInput, String wareHousecode, String salesLocationId) {
		 String willCallBranchCode = "";
		 String willCallBranch="";
		 String shipViaProperty = "Will Call".toUpperCase();
		 String branchNotAvailable = CommonDBQuery.getSystemParamtersList().get("AVAILABILITY_WAREHOUSE"); 
		 willCallBranch = CommonUtility.customServiceUtility().getWillCallBranchSelected(willCallBranchCode,session);
		 if(CommonUtility.validateString(willCallBranch)!=null && CommonUtility.validateString(willCallBranch).length()>0) {
				try {
					 String[] wareHouseDetails = willCallBranch.split(":");
					 if(wareHouseDetails!=null && wareHouseDetails.length>0) {
						willCallBranchCode = CommonUtility.validateString(wareHouseDetails[0])!=null?wareHouseDetails[0]:"";
						willCallBranch = CommonUtility.validateString(wareHouseDetails[1])!=null?wareHouseDetails[1]:"";
					 }
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		 if(willCallBranchCode!=null && willCallBranchCode.length()>0 && CommonUtility.validateString(salesOrderInput.getShipViaMethod()).equalsIgnoreCase(shipViaProperty)){
			 if (branchNotAvailable!=null && CommonUtility.validateString(wareHousecode).equalsIgnoreCase(branchNotAvailable))
              {
                cimm2bCentralLineItem.setShippingBranch(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AVAILABILITY_WAREHOUSE_FOR_30300")));
                System.out.println("Shipping Branch" + cimm2bCentralLineItem.getShippingBranch());
              }
              else
              {
            	  cimm2bCentralLineItem.setShippingBranch(willCallBranchCode);
              }
        } 
		else if(CommonUtility.validateString(salesLocationId).length()>0 && CommonUtility.validateString(salesLocationId)!="0"){
			
			if (branchNotAvailable!=null && CommonUtility.validateString(wareHousecode).equalsIgnoreCase(branchNotAvailable))
              {
                cimm2bCentralLineItem.setShippingBranch(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AVAILABILITY_WAREHOUSE_FOR_30300")));
                System.out.println("Shipping Branch" + cimm2bCentralLineItem.getShippingBranch());
              }
              else
              {
                cimm2bCentralLineItem.setShippingBranch(salesLocationId);
              }
		}
		else {
            cimm2bCentralLineItem.setShippingBranch(wareHousecode);
          }
	 }
	 
	 public void addDefaultWarehouseToOrderRequest(Cimm2BCentralOrder orderRequest, HttpSession session,SalesOrderManagementModel salesOrderInput, String wareHousecode, String salesLocationId) {
		 String shipViaProperty = "Will Call".toUpperCase();
		 String willCallBranchCode = "";
		 String contactName ="";
		 String contactPHNumber="";
		 String willCallBranch="";
		
		 /* po number default value*/
		 orderRequest.setCustomerPoNumber(CommonUtility.validateString(orderRequest.getCustomerPoNumber()).length()>0?orderRequest.getCustomerPoNumber():"TESTPONUMBER");
		 		 
		 AddressModel billAddress = salesOrderInput.getBillAddress();
		 if(session.getAttribute("userFirstName")!=null){
				contactName = (String) session.getAttribute("userFirstName") +" "+ session.getAttribute("userLastName");
			}else{
				contactName = billAddress.getFirstName()+" "+billAddress.getLastName();
			}
			if(session.getAttribute("userOfficePhone")!=null){
				contactPHNumber = (String) session.getAttribute("userOfficePhone");
			}else{
				contactPHNumber = billAddress.getPhoneNo();
			}
			
			willCallBranch = CommonUtility.customServiceUtility().getWillCallBranchSelected(willCallBranchCode,session);
			if(CommonUtility.validateString(willCallBranch)!=null && CommonUtility.validateString(willCallBranch).length()>0) {
				try {
					 String[] wareHouseDetails = willCallBranch.split(":");
					 if(wareHouseDetails!=null && wareHouseDetails.length>0) {
				willCallBranchCode = CommonUtility.validateString(wareHouseDetails[0])!=null?wareHouseDetails[0]:"";
				willCallBranch = CommonUtility.validateString(wareHouseDetails[1])!=null?wareHouseDetails[1]:"";
				}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		 if(CommonUtility.validateString(salesOrderInput.getShipViaMethod()).equalsIgnoreCase(shipViaProperty) && willCallBranchCode!=null && willCallBranchCode.length()>0){
				if (CommonUtility.validateString(wareHousecode).equalsIgnoreCase("30300"))
	              {
					orderRequest.setSalesLocationId(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AVAILABILITY_WAREHOUSE")));
	                System.out.println("Warehouse Will Call Branch" +orderRequest.getSalesLocationId());
	              }
	              else
	              {
	            	  orderRequest.setSalesLocationId(willCallBranchCode);
	            	  System.out.println("Warehouse Our Truck Branch" +orderRequest.getSalesLocationId());
	              }	

			}		
			else if(CommonUtility.validateString(salesLocationId).length()>0){
					orderRequest.setSalesLocationId(salesLocationId);
			}else{
				orderRequest.setSalesLocationId(wareHousecode);	
			}
		 if(CommonUtility.validateString(salesOrderInput.getShipViaMethod()).equalsIgnoreCase(shipViaProperty) && willCallBranch!=null && willCallBranch.length()>0){
				orderRequest.setOrderComment("User Name: "+contactName.toUpperCase()+"\n"+"Phone Number: " +contactPHNumber +"\n"+ "Order Notes: "+CommonUtility.validateString(salesOrderInput.getOrderNotes()) +" "+"Pick Up Location : "+"<"+ willCallBranch+">");
			}else{
				orderRequest.setOrderComment("User Name: "+contactName.toUpperCase()+"\n"+"Phone Number: " +contactPHNumber +"\n"+ "Order Notes: "+CommonUtility.validateString(salesOrderInput.getOrderNotes()));
			}

	 }
	 
	 public void setUserDetails(String others1B, String salesPersonName1B, String howDidYouSelect1B, UsersModel userDetails) {
		
		 if(others1B==null || salesPersonName1B==null)
		 {
			 userDetails.setHowWebsiteContact(howDidYouSelect1B);
		 }
		 if(salesPersonName1B!=null)
		 {
			 userDetails.setHowWebsiteContact(salesPersonName1B);
		 }
		 if(others1B!=null){
			 userDetails.setHowWebsiteContact(others1B);
		 }	
	 }
	 
	 public void addUserInformation(UsersModel userDetails, LinkedHashMap<String, String> userRegisteration, String OnAccountNotSetUp) {
		 if(OnAccountNotSetUp!=null && OnAccountNotSetUp.equalsIgnoreCase("Y")) {
			 if(userDetails.getHowWebsiteContact()!=null)
			 {
				 userRegisteration.put("How did you hear about our site?",userDetails.getHowWebsiteContact());
			 }
		 }
		 else {
			 if(userDetails.getHowWebsiteContact()!=null)
			 {
				 userRegisteration.put("How did you hear about our site?",userDetails.getHowWebsiteContact());
			 }
		 }
	 }
	 
	 public void setUserInformation(String others, String salesPersonName, String howDidYouSelect, LinkedHashMap<String, String> userRegisteration) {
		 if(others==null || salesPersonName==null)
		 {
			 userRegisteration.put("howDidYouSelect2AB", howDidYouSelect);
		 }
		 if(salesPersonName!=null)
		 {
			 userRegisteration.put("howDidYouSelect2AB",salesPersonName);
		 }
		 if(others!=null){
			 userRegisteration.put("howDidYouSelect2AB",others);
		 }	
	 }
	 
	 public void userRegistrationInformation(String others, String salesPersonName, String howDidYouSelect,	AddressModel userRegistrationDetail) {
		 if(others==null || salesPersonName==null)
		 {
			userRegistrationDetail.setHowWebsiteContact(howDidYouSelect);
		 }
		 if(salesPersonName!=null)
		 {
			 userRegistrationDetail.setHowWebsiteContact(salesPersonName);
		 }
		 if(others!=null){
			 userRegistrationDetail.setHowWebsiteContact(others);
		 }

	 }
	 
	 public boolean validateEmailAddress(boolean isValidUser, String emailAddress1B) { 
		 if(emailAddress1B!=null && !CommonUtility.validateEmail(emailAddress1B))
			{				
				isValidUser=false;;
			}
		 return isValidUser;
		 }
	 
	 public void getListPrice(ProductsModel itemModel, Cimm2BCentralLineItem lineItem) {
			if(itemModel.getUnitPrice()>0) {
				lineItem.setListPrice(itemModel.getUnitPrice());
			}else if(itemModel.getPrice()>0) {
				lineItem.setListPrice(itemModel.getPrice());
			}else {
				lineItem.setListPrice(0);
			}
		}
	 
	 public void setShipViaDescription(SalesOrderManagementModel salesOrderInput,Cimm2BCentralShipVia cimm2bCentralShipVia){
			cimm2bCentralShipVia.setShipViaCode(salesOrderInput.getShipViaDescription());
		}
	 
	 public void setAllBranchAvailValue(ProductManagementModel priceInquiryInput) {
		 priceInquiryInput.setAllBranchavailabilityRequired("N");
	 }
	 
	 public double getTaxFromCIMM(UsersModel shipAddress, double taxFromErp, double total) {
		 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TAX_FROM_CIMM")).equalsIgnoreCase("Y")){
				List<CIMMTaxModel> cimmTaxTable = CommonDBQuery.getCimmTaxTable();
				double taxPercentage = 0.00;
				boolean applyTax = false;
				double zipCode=CommonUtility.validateDoubleNumber(shipAddress.getZipCodeStringFormat().replaceAll("-", "."));
				System.out.println("zipcodeTax:"+zipCode);
				for(CIMMTaxModel taxData : cimmTaxTable){
						if(zipCode>=CommonUtility.validateDoubleNumber(taxData.getStartZip()) && zipCode<=CommonUtility.validateDoubleNumber(taxData.getEndZip())){
						applyTax = true;
						taxPercentage = taxData.getTaxPercentage();
						break;
					}
				}
				if(applyTax && taxPercentage > 0){
					taxFromErp = total * taxPercentage;
					System.out.println("Custom Tax"+taxFromErp);
					//orderTotal = orderTotal + taxFromErp;
				}
			}
		return taxFromErp;
		}
	 
	 public boolean setCustomerType(Cimm2BCentralCustomer customerDetails, boolean continuereg) {
		 Cimm2BCentralCustomerType customerType = customerDetails.getCustomerType();
		 if (customerType != null && customerType.getType() != null && (customerType.getType().equalsIgnoreCase("S") || customerType.getType().equalsIgnoreCase("B"))) {
			 continuereg = false;
			}
		 return continuereg;
		 }
	 
	 public int resultCountCondition(int resultCount) {
		 if(resultCount>1){
			 return resultCount;
		 }else {
			 return resultCount = resultCount+1;
		 }
	 }
	 
	 public double calculatingOrderSubTotal(ArrayList<ProductsModel> itemDetailObject, double orderSubTotal) {
		 orderSubTotal = 0.0;
		 for(ProductsModel eachItem:itemDetailObject){
			eachItem.setNetPrice(eachItem.getPrice());
			orderSubTotal = orderSubTotal+eachItem.getNetPrice();
		}
		 return orderSubTotal;
		 }
	 
	 public double assigningTotal(double orderGrandTotal, double orderSubTotal, String orderTax, String orderFreight) {
		 orderGrandTotal = orderSubTotal + CommonUtility.validateDoubleNumber(orderTax) + CommonUtility.validateDoubleNumber(orderFreight);
		 return orderGrandTotal;
		 }
	 
	public boolean verifyingWareHouse(String warehouseCode, String currentWareHouseCode, boolean wareHouseCustomerPrice) {
		if(warehouseCode!=null && currentWareHouseCode!=null) {
			wareHouseCustomerPrice = false;
		}
		return wareHouseCustomerPrice;
		}
	
	public double dontAddFreightoTotal(double subTotal, double taxFromErp, double totalCartFrieghtCharges, double orderTotal) {
		orderTotal = subTotal+taxFromErp + totalCartFrieghtCharges;
		return orderTotal;
		}
	
	 public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
		{	
		 String customFiledValue = CommonUtility.validateString(userDetailsInput.getHowWebsiteContact())!=null?userDetailsInput.getHowWebsiteContact():"";
		 
			if(CommonUtility.validateString(customFiledValue)!=null && CommonUtility.validateString(customFiledValue).length()>0){
				UsersDAO.insertCustomField(CommonUtility.validateString(customFiledValue),"HOW_DID_YOU_HEAR_ABOUT_OUR_SITE",userid, 0,"USER");
			}
			
		}
	 
	 public AddressModel CustomizedBuyingCompany(AddressModel userRegistrationDetail) {
			
			if(userRegistrationDetail.isAnonymousUser()) {
				userRegistrationDetail.setBuyingComanyIdStr(CommonUtility.validateParseIntegerToString(UsersDAO.getBuyingCompanyIdByEntityId(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID")))));
				}else{
					userRegistrationDetail.setBuyingComanyIdStr("");
				}
			return userRegistrationDetail;
		}
	 
	 public void userRegistrationInformation(AddressModel addressModel, UsersModel contactInformation) {
		 if(addressModel!=null && contactInformation!=null) {
		  contactInformation.setHowWebsiteContact(CommonUtility.validateString(addressModel.getHowWebsiteContact()));
		 }
	 }
	 
	 public void addCustomerName(UsersModel billAddress, UsersModel userDetail) {
		 try {
			 if(billAddress!=null && userDetail!=null && userDetail.getBillAddress()!=null)
			 {
				 billAddress.setCustomerName(CommonUtility.validateString(userDetail.getBillAddress().getCompanyName()));
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	 public boolean restricatingItemRetail(LinkedHashMap<String, Object> customFieldVal, boolean restrictedItemFlag, HttpSession session) { 
		 String Hazmat = CommonUtility.validateString((String)customFieldVal.get("custom_Hazmat"));
		 String OverSize = CommonUtility.validateString((String)customFieldVal.get("custom_OverSize"));
		 String fragile = CommonUtility.validateString((String)customFieldVal.get("custom_Fragile"));
				if((Hazmat.equalsIgnoreCase("Y") || OverSize.equalsIgnoreCase("Y") || fragile.equalsIgnoreCase("Y")) &&  CommonUtility.validateString((String)session.getAttribute("isRetailUser")).equalsIgnoreCase("Y")) {
					restrictedItemFlag = true;	
				}
			return restrictedItemFlag;
		 }
	 
	 public Cimm2BPriceAndAvailabilityRequest priceAndAvailIncludeUomList(Cimm2BPriceAndAvailabilityRequest priceAndAvailabilityRequest,ProductManagementModel priceInquiryInput) {
			try {
				if(CommonUtility.validateString(priceInquiryInput.getAllBranchavailabilityRequired()).equalsIgnoreCase("Y")) {
					priceAndAvailabilityRequest.setIncludeAllBranchAvailability(true);
					priceAndAvailabilityRequest.setIncludeCompanyAvailability(true);
					Set<String> allWarehouseCodes = new HashSet<String>();
					allWarehouseCodes.add(priceAndAvailabilityRequest.getPricingWarehouseCode());
					priceAndAvailabilityRequest.setWarehouseCodes(allWarehouseCodes);
				}else {
					priceAndAvailabilityRequest.setIncludeAllBranchAvailability(false);
					priceAndAvailabilityRequest.setIncludeCompanyAvailability(false);
				}
				priceAndAvailabilityRequest.setIncludeExcludeWarehouse("INCLUDE");
			}catch(Exception e) {
				e.printStackTrace();
			}
			return priceAndAvailabilityRequest;
		}
	 
	 public void setHomeBranchName(ProductsModel productsModel, String warehouseCode) {
		 productsModel.setBranchName(CommonUtility.validateString(CommonDBQuery.getWareHouseList().get(warehouseCode)));
	 }
	 public String getCustomerDefaultEntityId(int buyingCompanyId, String entityId, HttpSession session) {
		  entityId = CommonUtility.validateString((String)session.getAttribute("contactId"));
		 return entityId;
		 }
	 
	 public void addfirstNameAndlastNameToshipAddress(AddressModel selectedShipAddress, UsersModel userShipAddress,HttpSession session) {
			selectedShipAddress.setFirstName(CommonUtility.validateString(userShipAddress.getFirstName()));
			selectedShipAddress.setLastName(CommonUtility.validateString(userShipAddress.getLastName()));
	 }
	 
	public void addfirstNameAndlastNameToBillAddress(Address address, UsersModel billAddress) {
		if(CommonUtility.validateString(billAddress.getFirstName()).length()>0)
			address.setBillingName(billAddress.getFirstName().replaceAll("([^a-zA-Z]|\\s)+", " ") + " " +CommonUtility.validateString(billAddress.getLastName()));
		}
	
	public String getEntityShipToId(HttpSession session, String defaultShiptoId, String shipToId) {
		 String entityId = CommonUtility.validateString((String)session.getAttribute("entityId")); //if single shipAddress considering customer ERPId
		 if(CommonUtility.validateString(shipToId).length()>0) {
			 entityId = CommonUtility.validateString(shipToId);
			 session.setAttribute("shipEntityId",shipToId);
		 }
		 if(CommonUtility.validateString(entityId).length()>0) {
		 defaultShiptoId = CommonUtility.validateParseIntegerToString(UsersDAO.getBcAddressBookShipBillId(CommonUtility.validateNumber((String)session.getAttribute("buyingCompanyId")),entityId,AddressType.Ship));	
		 }
		 return defaultShiptoId;
		 }
	
	public boolean enableMainItemWPEnable(String[] attrFtrGlobal, boolean isProductGroup) {
		if(attrFtrGlobal!=null && attrFtrGlobal.length>0) {
			isProductGroup = true;
		}
		return isProductGroup;
		}
	
	public SolrQuery setRequestHandler(String[] attrFtrGlobal, SolrQuery query) {
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHANGE_REQUEST_HANDLER")).equalsIgnoreCase("Y") && !CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE).equalsIgnoreCase("/mainitem_keywordsearch") && attrFtrGlobal!=null && attrFtrGlobal.length>0)
			{
			 query.setRequestHandler(CommonUtility.getRequestHandler(RequestHandlers.ATTRIBUTE));
			}
		return query;
		}
}
