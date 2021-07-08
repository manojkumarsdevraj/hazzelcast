package com.shippingcarrier.fedExModel;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.axis.types.NonNegativeInteger;

import com.fedex.rate.stub.*;
import com.unilog.database.CommonDBQuery;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;


/** 
 * Sample code to call Rate Web Service with Axis 
 * <p>
 * com.fedex.rate.stub is generated via WSDL2Java, like this:<br>
 * <pre>
 * java org.apache.axis.wsdl.WSDL2Java -w -p com.fedex.rate.stub http://www.fedex.com/...../RateService?wsdl
 * </pre>
 * 
 * This sample code has been tested with JDK 5 and Apache Axis 1.4
 */
public class RateWebServiceClient {  
	static double freight;
	static RateReply reply;
	
	public static double getFedExFreightCharges(double totalWeight, int totalLength, int totalHeight, int totalWidth ,WarehouseModel wareHouseDetail,UsersModel shipAddress,String ShipMethod)
	{
		
		boolean getAllRatesFlag = false; // set to true to get the rates for different service types
	    RateRequest request = new RateRequest();
	    request.setClientDetail(createClientDetail());
        request.setWebAuthenticationDetail(createWebAuthenticationDetail());
        request.setReturnTransitAndCommit(true);

	    TransactionDetail transactionDetail = new TransactionDetail();
	    transactionDetail.setCustomerTransactionId("java sample - Rate Request"); // The client will get the same value back in the response
	    request.setTransactionDetail(transactionDetail);

        VersionId versionId = new VersionId("crs", 16, 0, 0);
        request.setVersion(versionId);
        
        RequestedShipment requestedShipment = new RequestedShipment();
        
        requestedShipment.setShipTimestamp(Calendar.getInstance());
        requestedShipment.setDropoffType(DropoffType.REGULAR_PICKUP);
        if (!getAllRatesFlag) {
        	if(CommonUtility.validateString(ShipMethod).length()>0 && CommonUtility.validateString(ShipMethod).equalsIgnoreCase("FEDP1")){
        		requestedShipment.setServiceType(ServiceType.PRIORITY_OVERNIGHT);
        	}else if(CommonUtility.validateString(ShipMethod).length()>0 && CommonUtility.validateString(ShipMethod).equalsIgnoreCase("FEDX")){
        		requestedShipment.setServiceType(ServiceType.STANDARD_OVERNIGHT);
        	}else if(CommonUtility.validateString(ShipMethod).length()>0 && (CommonUtility.validateString(ShipMethod).equalsIgnoreCase("FEDXC") || CommonUtility.validateString(ShipMethod).equalsIgnoreCase("FEDIG"))){
        		requestedShipment.setServiceType(ServiceType.FEDEX_GROUND);
        	}else if(CommonUtility.validateString(ShipMethod).length()>0 && (CommonUtility.validateString(ShipMethod).equalsIgnoreCase("FEDX2") || CommonUtility.validateString(ShipMethod).equalsIgnoreCase("FEIL2"))){
        		requestedShipment.setServiceType(ServiceType.FEDEX_2_DAY);
        	}else if(CommonUtility.validateString(ShipMethod).length()>0 && CommonUtility.validateString(ShipMethod).equalsIgnoreCase("FEDIN")){
        		requestedShipment.setServiceType(ServiceType.INTERNATIONAL_ECONOMY);
        	}else if(CommonUtility.validateString(ShipMethod).length()>0 && CommonUtility.validateString(ShipMethod).equalsIgnoreCase("FEDIP")){
        		requestedShipment.setServiceType(ServiceType.INTERNATIONAL_PRIORITY);
        	}else{
        		requestedShipment.setServiceType(ServiceType.FEDEX_GROUND);
        	}
        	requestedShipment.setPackagingType(PackagingType.YOUR_PACKAGING);
        }
        
        Party shipper = new Party();
	    Address shipperAddress = new Address(); // Origin information
	    shipperAddress.setStreetLines(new String[] {wareHouseDetail.getAddress1()});
		shipperAddress.setCity(wareHouseDetail.getCity());
		shipperAddress.setStateOrProvinceCode(wareHouseDetail.getState());
		shipperAddress.setPostalCode(wareHouseDetail.getZip());
		shipperAddress.setCountryCode(wareHouseDetail.getCountry());
        shipper.setAddress(shipperAddress);
        requestedShipment.setShipper(shipper);

        Party recipient = new Party();
	    Address recipientAddress = new Address(); // Destination information
	    recipientAddress.setStreetLines(new String[] {shipAddress.getAddress1()});
	    recipientAddress.setCity(shipAddress.getCity());
	    recipientAddress.setStateOrProvinceCode(shipAddress.getState());
	    recipientAddress.setPostalCode(shipAddress.getZipCodeStringFormat());
	    recipientAddress.setCountryCode(shipAddress.getCountry());
	    recipient.setAddress(recipientAddress);
	    requestedShipment.setRecipient(recipient);

	    //
	    Payment shippingChargesPayment = new Payment();
	    shippingChargesPayment.setPaymentType(PaymentType.SENDER);
	    requestedShipment.setShippingChargesPayment(shippingChargesPayment);

	    RequestedPackageLineItem rp = new RequestedPackageLineItem();
	    rp.setGroupPackageCount(new NonNegativeInteger("1"));
	    rp.setWeight(new Weight(WeightUnits.LB, new BigDecimal(String.valueOf(totalWeight))));
	    //
	    rp.setInsuredValue(new Money("USD", new BigDecimal("0.00")));
	    //
	    rp.setDimensions(new Dimensions(new NonNegativeInteger(String.valueOf(totalLength)), new NonNegativeInteger(String.valueOf(totalWidth)), new NonNegativeInteger(String.valueOf(totalHeight)), LinearUnits.IN));
	    PackageSpecialServicesRequested pssr = new PackageSpecialServicesRequested();
	    rp.setSpecialServicesRequested(pssr);
	    requestedShipment.setRequestedPackageLineItems(new RequestedPackageLineItem[] {rp});

	    
	    requestedShipment.setPackageCount(new NonNegativeInteger("1"));
	    request.setRequestedShipment(requestedShipment);
	    //request.setRequestedShipment(addRequestedShipment(totalWeight, totalLength, totalHeight, totalWidth ,warehouseAddress,shipAddress)); //mahesh
	    
	    //
		try {
			// Initialize the service
			RateServiceLocator service;
			RatePortType port;
			//
			service = new RateServiceLocator();
			updateEndPoint(service);
			port = service.getRateServicePort();
			// This is the call to the web service passing in a RateRequest and returning a RateReply
			RateReply reply = port.getRates(request); // Service call
			if (isResponseOk(reply.getHighestSeverity())) {
				freight=writeServiceOutput(reply);
			} 
			printNotifications(reply.getNotifications());

		} catch (Exception e) {
		    e.printStackTrace();
		} 
		
		return freight;
	}
	
	public static RateReply calculateRate(int totalWeight, int totalLength, int totalHeight, int totalWidth ,WarehouseModel wareHouseDetail,UsersModel shipAddress)
	{
		RateRequest request = buildRateRequest(totalWeight, totalLength, totalHeight, totalWidth ,wareHouseDetail,shipAddress);
		//
		try {
			// Initialize the service
			RateServiceLocator service;
			RatePortType port;
			//
			service = new RateServiceLocator();
			updateEndPoint(service);
			port = service.getRateServicePort();
			//
			reply = port.getRates(request); // This is the call to the ship web service passing in a request object and returning a reply object
			//
			if (!isResponseOk(reply.getHighestSeverity())) // check if the call was successful
			{
				//writeServiceOutput(reply);
				
			}
			//printNotifications(reply.getNotifications());
			
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			//
		}
		return reply;
	}

//
	private static RateRequest buildRateRequest(int totalWeight, int totalLength, int totalHeight, int totalWidth ,WarehouseModel wareHouseDetail,UsersModel shipAddress)
	{
		RateRequest request = new RateRequest(); // Build a request object

request.setClientDetail(createClientDetail());
request.setWebAuthenticationDetail(createWebAuthenticationDetail());
// 
	TransactionDetail transactionDetail = new TransactionDetail();
	transactionDetail.setCustomerTransactionId("java sample - Domestic Freight Rate Request"); // The client will get the same value back in the response
	request.setTransactionDetail(transactionDetail);
//
VersionId versionId = new VersionId("crs", 16, 0, 0);
request.setVersion(versionId);
request.setReturnTransitAndCommit(true);
//
	request.setRequestedShipment(addRequestedShipment(totalWeight, totalLength, totalHeight, totalWidth ,wareHouseDetail,shipAddress));
		return request;
	}
	
	private static RequestedShipment addRequestedShipment(int totalWeight, int totalLength, int totalHeight, int totalWidth ,WarehouseModel wareHouseDetail,UsersModel shipAddress){
	RequestedShipment requestedShipment = new RequestedShipment();
	requestedShipment.setShipTimestamp(Calendar.getInstance()); // Ship date and time
	requestedShipment.setDropoffType(DropoffType.REGULAR_PICKUP); // Dropoff Types are BUSINESS_SERVICE_CENTER, DROP_BOX, REGULAR_PICKUP, REQUEST_COURIER, STATION
	//requestedShipment.setServiceType(ServiceType.FEDEX_FREIGHT_PRIORITY); // Service types are STANDARD_OVERNIGHT, PRIORITY_OVERNIGHT, FEDEX_GROUND ...
	requestedShipment.setPackagingType(PackagingType.YOUR_PACKAGING); // Packaging type FEDEX_BOX, FEDEX_PAK, FEDEX_TUBE, YOUR_PACKAGING, ...
	requestedShipment.setShipper(addShipper(wareHouseDetail));
	requestedShipment.setRecipient(addRecipient(shipAddress));
	requestedShipment.setShippingChargesPayment(addShippingChargesPayment());
		requestedShipment.setFreightShipmentDetail(addFreightShipmentDetail(totalWeight,totalLength,totalWidth,totalHeight));
		requestedShipment.setDeliveryInstructions("FreightDeliveryInstructions");
	requestedShipment.setPackageCount(new NonNegativeInteger("1"));
	return requestedShipment;
	}
	
	public static double writeServiceOutput(RateReply reply) {
		double freight=0.0;
		RateReplyDetail[] rrds = reply.getRateReplyDetails();
		for (int i = 0; i < rrds.length; i++) {
			RateReplyDetail rrd = rrds[i];
			print("\nService type", rrd.getServiceType());
			print("Packaging type", rrd.getPackagingType());
			/*print("Delivery DOW", rrd.getDeliveryDayOfWeek());
			int month = rrd.getDeliveryTimestamp().get(Calendar.MONTH)+1;
			int date = rrd.getDeliveryTimestamp().get(Calendar.DAY_OF_MONTH);
			int year = rrd.getDeliveryTimestamp().get(Calendar.YEAR);
			String delDate = new String(month + "/" + date + "/" + year);
			print("Delivery date", delDate);
			print("Calendar DOW", rrd.getDeliveryTimestamp().get(Calendar.DAY_OF_WEEK));
			print("Calendar Tuesday", Calendar.TUESDAY);*/
			RatedShipmentDetail[] rsds = rrd.getRatedShipmentDetails();
			for (int j = 0; j < rsds.length; j++) {
				print("RatedShipmentDetail " + j, "");
				RatedShipmentDetail rsd = rsds[j];
				ShipmentRateDetail srd = rsd.getShipmentRateDetail();
				print("  Rate type", srd.getRateType());
				printWeight("  Total Billing weight", srd.getTotalBillingWeight());
				printMoney("  Base Charge", srd.getTotalBaseCharge());
				printMoney("  Total discounts", srd.getTotalFreightDiscounts());
				displayDiscounts(srd);
				printMoney("  Total surcharges", srd.getTotalSurcharges());
				displaySurcharges(srd);
				printMoney("  Total net charge", srd.getTotalNetCharge());
				freight=Double.parseDouble(srd.getTotalNetCharge().getAmount().toString());
				RatedPackageDetail[] rpds = rsd.getRatedPackages();
				if (rpds != null && rpds.length > 0) {
					print("  RatedPackageDetails", "");
					for (int k = 0; k < rpds.length; k++) {
						print("  RatedPackageDetail " + i, "");
						RatedPackageDetail rpd = rpds[k];
						PackageRateDetail prd = rpd.getPackageRateDetail();
						if (prd != null) {
							printWeight("Billing weight", prd.getBillingWeight());
							printMoney("Base charge", prd.getBaseCharge());
							Surcharge[] surcharges = prd.getSurcharges();
							if (surcharges != null && surcharges.length > 0) {
								for (int m = 0; m < surcharges.length; m++) {
									Surcharge surcharge = surcharges[m];
									printMoney("" + surcharge.getDescription() + " surcharge", surcharge.getAmount());
								}
							}
						}
					}
				}
				displayFreightRateDetail(srd);
				
			}
			System.out.println("");
		}
		return freight;
	}
	
	private static void print(String msg, Object obj) {
		if (msg == null || obj == null) {
			return;
		}
		System.out.println(msg + ": " + obj.toString());
	}
	
	private static void printMoney(String msg, Money money) {
		if (msg == null || money == null) {
			return;
		}
		System.out.println(msg + ": " + money.getAmount() + " " + money.getCurrency());
	}
	
	private static void printWeight(String msg, Weight weight) {
		if (msg == null || weight == null) {
			return;
		}
		System.out.println(msg + ": " + weight.getValue() + " " + weight.getUnits());
	}
	  	 	
	private static boolean isResponseOk(NotificationSeverityType notificationSeverityType) {
		if (notificationSeverityType == null) {
			return false;
		}
		if (notificationSeverityType.equals(NotificationSeverityType.WARNING) ||
			notificationSeverityType.equals(NotificationSeverityType.NOTE)||
			notificationSeverityType.equals(NotificationSeverityType.SUCCESS)) {
			return true;
		}
 		return false;
	}
	
	private static ClientDetail createClientDetail() {
ClientDetail clientDetail = new ClientDetail();
String accountNumber = System.getProperty("accountNumber");
String meterNumber = System.getProperty("meterNumber");

//
// See if the accountNumber and meterNumber properties are set,
// if set use those values, otherwise default them to "XXX"
//
if (accountNumber == null) {
	accountNumber = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FEDEX_ACCOUNT_NUMBER")); // Replace "XXX" with clients account number
}
if (meterNumber == null) {
	meterNumber = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FEDEX_METER_NUMBER")); // Replace "XXX" with clients meter number
}
clientDetail.setAccountNumber(accountNumber);
clientDetail.setMeterNumber(meterNumber);
return clientDetail;
	}
	
	private static WebAuthenticationDetail createWebAuthenticationDetail() {
WebAuthenticationCredential wac = new WebAuthenticationCredential();
String key = System.getProperty("key");
String password = System.getProperty("password");

//
// See if the key and password properties are set,
// if set use those values, otherwise default them to "XXX"
//
if (key == null) {
	key = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FEDEX_CLIENT_KEY")); // Replace "XXX" with clients key
}
if (password == null) {
	password = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FEDEX_PASSWORD")); // Replace "XXX" with clients password
}
wac.setKey(key);
wac.setPassword(password);
		return new WebAuthenticationDetail(wac);
	}
	
	private static void printNotifications(Notification[] notifications) {
		System.out.println("\nNotifications");
		if (notifications == null || notifications.length == 0) {
			System.out.println("  No notifications returned");
		}
		for (int i=0; i < notifications.length; i++){
			Notification n = notifications[i];
			System.out.print("  Notification no. " + i + ": ");
			if (n == null) {
				System.out.println("null");
				continue;
			} else {
				System.out.println("");
			}
			NotificationSeverityType nst = n.getSeverity();

			System.out.println("Severity: " + (nst == null ? "null" : nst.getValue()));
			System.out.println("Code: " + n.getCode());
			System.out.println("Message: " + n.getMessage());
			System.out.println("Source: " + n.getSource());
		}
	}
	
	private static Payment addShippingChargesPayment(){
	Payment payment = new Payment(); // Payment information
	payment.setPaymentType(PaymentType.SENDER);
	Payor payor = new Payor();
	Party responsibleParty = new Party();  
	String freightAccountNumber = System.getProperty("freightAccountNumber");
	if(freightAccountNumber == null){
		freightAccountNumber = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FEDEX_FRIEGHT_ACCOUNT_NUMBER"));  //Replace "XXX" with the Freight Account Number.
	}
	responsibleParty.setAccountNumber(freightAccountNumber);
	Address responsiblePartyAddress = new Address();
	responsiblePartyAddress.setCountryCode("US");
	responsibleParty.setAddress(responsiblePartyAddress);
	responsibleParty.setContact(new Contact());
		payor.setResponsibleParty(responsibleParty);
	payment.setPayor(payor);
	return payment;
	}
	
	private static FreightShipmentDetail addFreightShipmentDetail(int weight, int length, int width, int height) {
		FreightShipmentDetail freightShipmentDetail = new FreightShipmentDetail(); // Freight Detail
	String freightAccountNumber = System.getProperty("freightAccountNumber");
	if(freightAccountNumber == null){
		freightAccountNumber = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FEDEX_FRIEGHT_ACCOUNT_NUMBER")); //Replace "XXX" with the Freight Account Number.
	}
		freightShipmentDetail.setFedExFreightAccountNumber(freightAccountNumber);
   		freightShipmentDetail.setFedExFreightBillingContactAndAddress(addFedExFreightBillingContactAndAddress());
	freightShipmentDetail.setRole(FreightShipmentRoleType.SHIPPER);
	//freightShipmentDetail.setPaymentType(FreightAccountPaymentType.PREPAID);
	freightShipmentDetail.setDeclaredValuePerUnit(new Money("USD", new BigDecimal(0.0)));
	freightShipmentDetail.setLiabilityCoverageDetail(addLiabilityCoverageDetail());
	freightShipmentDetail.setTotalHandlingUnits(new NonNegativeInteger("1"));
	freightShipmentDetail.setShipmentDimensions(addShipmentDimensions(length, width, height));
	freightShipmentDetail.setPalletWeight(new Weight(WeightUnits.LB, new BigDecimal(weight)));
		freightShipmentDetail.setComment("FreightShipmentComment");
		freightShipmentDetail.setLineItems(new FreightShipmentLineItem[] {addFreightShipmentLineItem(weight, length, width, height)});
		return freightShipmentDetail;
	}
	
	private static ContactAndAddress addFedExFreightBillingContactAndAddress(){
		
		Contact freightContact = new Contact();
		freightContact.setPersonName("Freight Billing Contact");
		freightContact.setCompanyName("Freight Billing Company");
		//freightContact.setPagerNumber("1234567890");
		Address freightAddress = new Address();
		freightAddress.setStreetLines(new String[] {"1202 Chalet Ln", "Do Not Delete - Test Account"});
		freightAddress.setCity("Harrison");
		freightAddress.setStateOrProvinceCode("AR");
		freightAddress.setPostalCode("72601-6353");
		freightAddress.setCountryCode("US");
		return new ContactAndAddress(freightContact, freightAddress);
	}
	
	private static LiabilityCoverageDetail addLiabilityCoverageDetail(){
		LiabilityCoverageDetail liability = new LiabilityCoverageDetail();
		liability.setCoverageType(LiabilityCoverageType.NEW);
		liability.setCoverageAmount(new Money("USD", new BigDecimal(0.0)));
		return liability;
	}
	
	private static Dimensions addShipmentDimensions(int width, int length, int height){
		Dimensions dimensions = new Dimensions();
		dimensions.setLength(new NonNegativeInteger(String.valueOf(length)));
		dimensions.setWidth(new NonNegativeInteger(String.valueOf(width)));
		dimensions.setHeight(new NonNegativeInteger(String.valueOf(height)));
		dimensions.setUnits(LinearUnits.IN);
		return dimensions;
	}
	
	private static FreightShipmentLineItem addFreightShipmentLineItem(int weight, int length, int width, int height){
		FreightShipmentLineItem lineItem = new FreightShipmentLineItem();
		lineItem.setFreightClass(FreightClassType.CLASS_050);
		lineItem.setPackaging(PhysicalPackagingType.BOX);
		lineItem.setDescription("lineitemdesc");
		lineItem.setWeight(new Weight(WeightUnits.LB, new BigDecimal (String.valueOf(weight))));
		Dimensions itemDimensions = new Dimensions();
		itemDimensions.setLength(new NonNegativeInteger(String.valueOf(length)));
		itemDimensions.setWidth(new NonNegativeInteger(String.valueOf(width)));
		itemDimensions.setHeight(new NonNegativeInteger(String.valueOf(height)));
		itemDimensions.setUnits(LinearUnits.IN);
		lineItem.setDimensions(itemDimensions);
		//lineItem.setVolume(new Volume(VolumeUnits.CUBIC_FT, new BigDecimal("1.0")));
		return lineItem;
	}
	
	private static Party addShipper(WarehouseModel wareHouseDetail){
	Party party = new Party(); // Sender information
	Address shipperAddress = new Address();
	shipperAddress.setStreetLines(new String[] {wareHouseDetail.getAddress1()});
	shipperAddress.setCity(wareHouseDetail.getCity());
	shipperAddress.setStateOrProvinceCode(wareHouseDetail.getState());
	shipperAddress.setPostalCode(wareHouseDetail.getZip());
	shipperAddress.setCountryCode(wareHouseDetail.getCountry());
	party.setAddress(shipperAddress);
	return party;
	}

	private static Party addRecipient(UsersModel shipAddress){
	Party party = new Party(); // Recipient information
	Address recipientAddress = new Address();
	recipientAddress.setStreetLines(new String[] {shipAddress.getAddress1()});
	recipientAddress.setCity(shipAddress.getCity());
	recipientAddress.setStateOrProvinceCode(shipAddress.getState());
	recipientAddress.setPostalCode(shipAddress.getZipCodeStringFormat());
	recipientAddress.setCountryCode(shipAddress.getCountry());   
	party.setAddress(recipientAddress);
	return party;
	}
	   	   	   	
	private static void displayDiscounts(ShipmentRateDetail srd){
		RateDiscount[] discount = srd.getFreightDiscounts();
		if(null!=discount){
		for(int i=0; i<discount.length; i++){
			System.out.println("" 
					+" " +discount[i].getAmount().getAmount()
					+" " +discount[i].getAmount().getCurrency()
					+" " +discount[i].getRateDiscountType());
		}
		}
	}
	
	private static void displaySurcharges(ShipmentRateDetail srd){
		if (null != srd.getSurcharges()){
			Surcharge[] s = srd.getSurcharges();
			if(null!=s){
				for(int i=0; i < s.length; i++){
					if (null != s[i].getSurchargeType())
						System.out.println("" + s[i].getSurchargeType() + " surcharge: " + 
								s[i].getAmount().getAmount() + " " + s[i].getAmount().getCurrency());
				}
			}
		}
	}
	
	private static void displayFreightRateDetail(ShipmentRateDetail srd){
		if(null != srd.getFreightRateDetail()){
			System.out.println("Freight Rate Details");
			FreightRateDetail freightRate = srd.getFreightRateDetail();
			print("  Quote Number", freightRate.getQuoteNumber());
			print("  Base Charge Calculation", freightRate.getBaseChargeCalculation().getValue());
			displayFreightBaseCharges(freightRate);
			displayNotations(freightRate);			
		}
	}
	
	private static void displayNotations(FreightRateDetail frd){
		if(null != frd.getNotations()){
			System.out.println("  Notations");
			FreightRateNotation notations[] = frd.getNotations();
			for(int n=0; n< notations.length; n++){
				print("Code",notations[n].getCode());
			print("Descriptions",notations[n].getDescription());
			}
		}
	}
	
	private static void displayFreightBaseCharges(FreightRateDetail frd){
		if(null != frd.getBaseCharges()){
			FreightBaseCharge baseCharges[] = frd.getBaseCharges();
			for(int i=0; i < baseCharges.length; i++){
				print("Description",baseCharges[i].getDescription());
				print("Freight Class",baseCharges[i].getFreightClass());
				print("Rated Class",baseCharges[i].getRatedAsClass());
				printWeight("Weight",baseCharges[i].getWeight());
				print("Charge Basis",baseCharges[i].getChargeBasis());
				printMoney("Charge Rate",baseCharges[i].getChargeRate());
				printMoney("Extended Amount",baseCharges[i].getExtendedAmount());
				print("NMFC Code",baseCharges[i].getNmfcCode());
			}
		}
	}
	
	private static void updateEndPoint(RateServiceLocator serviceLocator) {
		String endPoint = System.getProperty("endPoint");
		if (endPoint != null) {
			serviceLocator.setRateServicePortEndpointAddress(endPoint);
		}
	}
}
