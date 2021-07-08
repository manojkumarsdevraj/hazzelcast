package com.unilog.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCoupon;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralDocumentDetail;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipVia;
import com.unilog.cimmesb.client.ecomm.request.CimmShippingCharges;
import com.unilog.ecomm.model.DiscountType;
import com.unilog.logocustomization.CustomizationCharges;
import com.unilog.products.ProductsModel;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;


public class SalesModel {

	private String accountRegId;
	private double additionalCharges10;
	private double additionalCharges11;
	private double additionalCharges12;
	private double additionalCharges13;
	private double additionalCharges14;
	private double additionalCharges15;
	private double additionalCharges16;
	private double additionalCharges17;
	private double additionalCharges18;
	private double additionalCharges19;
	private double additionalCharges2;
	private double additionalCharges20;
	private double additionalCharges21;
	private double additionalCharges22;
	private double additionalCharges23;
	private double additionalCharges24;
	private double additionalCharges25;
	private double additionalCharges26;
	private double additionalCharges27;
	private double additionalCharges28;
	private double additionalCharges29;
	private double additionalCharges3;
	private double additionalCharges30;
	private double additionalCharges31;
	private double additionalCharges32;
	private double additionalCharges33;
	private double additionalCharges34;
	private double additionalCharges35;
	private double additionalCharges36;
	private double additionalCharges37;
	private double additionalCharges4;
	private double additionalCharges5;
	private double additionalCharges6;
	private double additionalCharges7;
	private double additionalCharges8;
	private double additionalCharges9;
	private String additionalChargesDescription10;
	private String additionalChargesDescription11;
	private String additionalChargesDescription12;
	private String additionalChargesDescription13;
	private String additionalChargesDescription14;
	private String additionalChargesDescription15;
	private String additionalChargesDescription16;
	private String additionalChargesDescription17;
	private String additionalChargesDescription18;
	private String additionalChargesDescription19;
	private String additionalChargesDescription2;
	private String additionalChargesDescription20;
	private String additionalChargesDescription21;
	private String additionalChargesDescription22;
	private String additionalChargesDescription23;
	private String additionalChargesDescription24;
	private String additionalChargesDescription25;
	private String additionalChargesDescription26;
	private String additionalChargesDescription27;
	private String additionalChargesDescription28;
	private String additionalChargesDescription29;
	private String additionalChargesDescription3;
	private String additionalChargesDescription30;
	private String additionalChargesDescription31;
	private String additionalChargesDescription32;
	private String additionalChargesDescription33;
	private String additionalChargesDescription34;
	private String additionalChargesDescription35;
	private String additionalChargesDescription36;
	private String additionalChargesDescription37;
	private String additionalChargesDescription4;
	private String additionalChargesDescription5;
	private String additionalChargesDescription6;
	private String additionalChargesDescription7;
	private String additionalChargesDescription8;
	private String additionalChargesDescription9;
	private AddressModel billAddress;
	private String billAddress1;
	private String billAddress2;
	private String billCity;
	private String billCountry;
	private String billEmailAddress;
	private String billPhone;
	private String billState;
	private String billToEntityId;
	private String billToName;
	private String billZipCode;
	private String cardholder;
	private double cashDiscountAmount;
	private double cashDiscountPercentage;
	private String catalogId;
	private String catalogNumber;
	private String categoryName;
	private String creditCardExpDate;
	private String creditCardHolderName;
	private ArrayList<CreditCardModel> creditCardList;
	private String creditCardNumber;
	private String customerCountry;
	private String customerName;
	private String customerNumber;
	private String customerPartNumber;
	private ArrayList<ProductsModel> customerPartNumberList;
	private double customerPrice;
	private String customerReleaseNumber;
	private LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal;
	private double deliveryCharge;
	private String Description;
	private String description1;
	private String description2;
	private boolean disableAddToCart;
	private double discount;

	private String discountCouponCode;
	private DiscountType discountType;
	private String endDate;
	private String entityId;
	private String entityName;
	private String erpOrderMessage;
	private String erpOrderNumber;
	private int erpQty;
	private String externalSysError;
	private String externalSystemId;
	private double extPrice;
	private double federalExciseTax;
	private String fieldname;
	private String fieldvalue;
	private boolean freeShipping;
	private double freight;
	private double FreightAmount;
	private String freightDesc;
	private double freightIn;
	private double freightOut;
	private String freightInDesc;
	private String gasPoNumber;
	private String geneId;
	private double handling;
	private String hazardiousMaterial;
	private String homeBranchName;
	private String imageName;
	private String imageType;
	private String internalNotes;
	private String invoiceDate;
	private String invoiceNumber;
	private double invoiceAmount;
	private String isOverSize;
	private int itemId;
	private String itemLevelRequiredByDate;
	private String requiredByDate;
	private String paidDate;
	private ArrayList<Cimm2BCentralCoupon> orderLevelCouponList;
	private String subOrderCriteriaSupplierValue;
	private ArrayList<Cimm2BCentralCoupon> itemLevelCouponList;
	
	public ArrayList<Cimm2BCentralCoupon> getItemLevelCouponList() {
		return itemLevelCouponList;
	}
	public void setItemLevelCouponList(ArrayList<Cimm2BCentralCoupon> itemLevelCouponList) {
		this.itemLevelCouponList = itemLevelCouponList;
	}
	public String getSubOrderCriteriaSupplierValue() {
		return subOrderCriteriaSupplierValue;
	}
	public void setSubOrderCriteriaSupplierValue(String subOrderCriteriaSupplierValue) {
		this.subOrderCriteriaSupplierValue = subOrderCriteriaSupplierValue;
	}
	public String getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}
	public ArrayList<Cimm2BCentralCoupon> getOrderLevelCouponList() {
		return orderLevelCouponList;
	}
	public void setOrderLevelCouponList(ArrayList<Cimm2BCentralCoupon> orderLevelCouponList) {
		this.orderLevelCouponList = orderLevelCouponList;
	}
	private int itemPriceId;
	private String leadTime;
	private String lineItemComment;
	private int lineNumber;

	private double listPrice;
	private String manufacturer;
	private String manufacturerPartNumber;
	private String materialGroup;
	private String multipleShipVia;
	private String multipleShipViaDesc;
	private String name;
	private double netAmount;

	private double netOrdered;
	private ArrayList<ProductsModel> nonCatalogItem;
	private double openOrderAmount;
	private String orderComment;
	private String orderDate;
	private String orderedBy;
	private String orderEnterDate;
	private int orderId;
	private String orderID;
	private ArrayList<SalesModel> orderItem = new ArrayList<SalesModel>();
	private int orderItemId;
	private double orderItemsDiscount;
	private ArrayList<SalesModel> orderList = new ArrayList<SalesModel>();
	private String orderNotes;
	private String orderNum;
	private double orderPrice;
	private String orderPriceStr;
	private int orderQty;
	private String orderStatus;
	private String OrderStatusCode;
	private String orderStatusDesc;
	private String orderStatusType;
	private int orderSuffix;
	private String orderUom;
	private String partNumber;
	private String altPartNumber;
	private double paymentAmount;
	private String paymentAuthCode;
	private String paymentDate;
	private String paymentMethod;
	private String paymentRefrenceId;
	private int perQty;
	private String phone;
	private String poNumber;
	private String postalCode;
	private String postDate;
	private Boolean preAuthorize;

	private double price;
	private String getPriceFrom;
	private String priceString;
	private String printStatus;
	private String productcode;
	private String productKeywords;
	private String promiseDate;
	private int qtyordered;
	private int qtyShipped;
	private String qtyUom;
	private double quantityOrdered;
	private double quantityShipped;
	private String quoteNumber;
	private String refrenceKey;
	private String reqDate;
	private String reqType;
	private String returnUrl;
	private double rOEDiscount;
	private String salesPersonCode;
	private String salesUom;
	private String searchString;
	private boolean sendMailFlag = true;
	private String sendmailToSalesRepOnly;
	private String sendQuoteMail;
	private int seqnum;
	private HttpSession session;
	private String sessionId;
	private AddressModel shipAddress;
	private UsersModel shippingAddress;
	private String shipAddress1;
	private String shipAddress2;
	private String shipCity;
	private String shipCountry;
	private String shipDate;
	private String shipEmailAddress;
	private String shipFirstName;
	private String shipLastName;
	private String shipMethod;

	private String shipMethodId;
	private AddressModel shippedAddress;
	private String shipPhone;
	private String shippingCourier;
	private String shippingInstruction;
	private String shipState;
	private String shipToEntityId;
	private String shipToId;
	private String shipToName;
	private String shipViaErpId;

	private String ShipViaID;
	private String ShipViaMethod;
	private String shipViaDescription;
	private String shipZipCode;
	private String shortDesc;
	private String slsrepin;
	private String shipViaServiceName;

	private String slsrepout;

	private String stage;
	private int stageCode;
	private String stageCodeString;
	private String startDate;
	private String status;
	private String statusDescription;

	private String streetAddress;
	private String stringPrice;
	private String stringTotal;
	private double subtotal;
	private double subtotalV2;
	private String takenBy;

	private double tax;
	private String termsdiscamt;
	private String termstypedesc;
	private double total;
	private double totalLineAmount;
	private double totalSavingsOnOrder;
	private String totalStr;
	private String totalStrV2;
	private double totalV2;
	private double totinvamt;
	private String trackingInfo;
	private String transactionId;
	private String transtype;
	private double unitPrice;
	private double unitsPerStocking;
	private String unitsPerStockingString;
	private String unspc;
	private String uom;
	private ArrayList<String> uomList;
	private String upc;
	private HashMap<String, String> upcList;
	private HashMap<String, String> trackingNumberList;
	// private ArrayList<SalesModel> orderPriceList = new
	// ArrayList<SalesModel>();
	private int userId;
	private String userName;
	private String userNote;
	private String userToken;
	private String wareHouseCode;
	private String WarehouseLocation;
	private String wholeOrderDiscountAmount;
	private String writtenBy;
	
	private ArrayList<String> lineItemCommentsList;
	private String balanceAmount;
	private String invoiceAge;
	private String sPaymentAmount;
	private String currency;
	private double netDue;
	private String carrierTrackingNumber;
	private String accountNumber;
	private double amountPayed;
	private String dueDate;
	private String documentLinks;
	private int totalPages;
	private int pageNumber;
	private ArrayList<ProductsModel> cartData;
	private String accountId;
	private String profileID;
	private String dataDescriptor;
	private String dataValue;
	private double amount;
	private String customerERPId; 
	private String authorizationCode;
	private String avsResultCode;
	private String cvvResultCode;
	private String cavvResultCode;
	private String transactionHash;
	private String paymentStatus;
	private String customerProfileId;
	private List<String> customerPaymentIdList;
	private String customerPaymentId;
	private String paymentAccountId;
	private String cvv;
	private boolean saveCard;
	private String erpOverrideFlag;
	private String brandName;
	private WarehouseModel wareHouseDetails;
	private String orderType;
	private String currentStatus;
	private String externalCartId;
	private boolean taxable;
	private String jurisdictionCode;
	private String jobId;
	private double otherCharges;
	private String shippingBranchId;
	private String pageTitle;
	private String orderSource;
	private String altPartNumber1;
	private String altPartNumber2;
	private ArrayList<Cimm2BCentralShipVia> cimm2BCentralShipVia;
	private String orderLastDate;
	private Integer orderLastQty;
	private String productStatus;
	private String orderGenerationNumber;
	private int bcAddressBookId;
	private int firstOrder;
	private String discountDate;
	private String shippingOrgType;
	private String includeApprovalType;
	private String approvalType;
	private String primaryOrderNumber;
	private String companyName;
	private String secureToken;
	private String secureTokenId;
	private String rushFlag;
	private Cimm2BCentralDocumentDetail documentDetail;
	private int backOrderQty;

	private int designId;
	private boolean designFees;
	private HashMap<Integer,CustomizationCharges> designFeesModel = new HashMap<Integer,CustomizationCharges>();
	private int uniqueWebReferenceNumber;
	private String shippingBranchName;
	private String returnReason;
	private int returnQty;
	private String backorderType;
	private String acceptBackorders;
	private int recordLimit;
	private String sortDirection;
	private double orderTotal;
	private String searchKeyword;
	private String userValue;
	private String documentId;
	private ArrayList<SalesModel> maxRecallList;
	private String maxData;
	private String maxName;
	private String maxTitle;
	private String maxDate;
	private String maxUrl;
	private String shipViaServiceCode;
	private double totalCartFrieght;
	private CimmShippingCharges transportationCharges;
	private CimmShippingCharges serviceCharges;
	private CimmShippingCharges totalCharges;
	private ProductsModel totalCartDimensions;
	private String searchKeywordForOrderSuffix;
	private int qtyOpen;
	private String dispositionDescription;
	private double openItemsNumber;
	private int pageSize;
	private boolean rowDetails;
	private String startRowId;
	private ArrayList<String> rowIds = new ArrayList<String>();
	private String returnComment;
	private String deliveryDate;
	private String deliveryTime;
	private double addOnNumber3;
	private double addOnNumber4;
	private double addOnNumber6;
	private double addOnNumber7;
	private double addOnNumber8;
	private double addOnNumber11;
	private double addOnNumber12;
	private double addOnNumber44;
	private double totalFreightAddOns;
	private ArrayList<String> multipleTrackingIds = new ArrayList<String>();
	private String user18;
	private String transactionCode;
	private String transactionType;
	private String routeData;
	private String orderTime;
	private String billFirstName;
	private String billLastName;
	private String pdfDocumentId;
	private boolean excludePDFDocument; 
	LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumberMap;
	private boolean localDelivery;
	
	public LinkedHashMap<Integer, ArrayList<ProductsModel>> getCustomerPartNumberMap() {
		return customerPartNumberMap;
	}
	public void setCustomerPartNumberMap(LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumberMap) {
		this.customerPartNumberMap = customerPartNumberMap;
	}
	public String getPdfDocumentId() {
		return pdfDocumentId;
	}
	public void setPdfDocumentId(String pdfDocumentId) {
		this.pdfDocumentId = pdfDocumentId;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getUser18() {
		return user18;
	}
	public void setUser18(String user18) {
		this.user18 = user18;
	}

	public ArrayList<String> getMultipleTrackingIds() {
		return multipleTrackingIds;
	}
	public void setMultipleTrackingIds(ArrayList<String> multipleTrackingIds) {
		this.multipleTrackingIds = multipleTrackingIds;
	}
	public double getAddOnNumber3() {
		return addOnNumber3;
	}
	public void setAddOnNumber3(double addOnNumber3) {
		this.addOnNumber3 = addOnNumber3;
	}
	public double getAddOnNumber4() {
		return addOnNumber4;
	}
	public void setAddOnNumber4(double addOnNumber4) {
		this.addOnNumber4 = addOnNumber4;
	}
	public double getAddOnNumber6() {
		return addOnNumber6;
	}
	public void setAddOnNumber6(double addOnNumber6) {
		this.addOnNumber6 = addOnNumber6;
	}
	public double getAddOnNumber7() {
		return addOnNumber7;
	}
	public void setAddOnNumber7(double addOnNumber7) {
		this.addOnNumber7 = addOnNumber7;
	}
	public double getAddOnNumber8() {
		return addOnNumber8;
	}
	public void setAddOnNumber8(double addOnNumber8) {
		this.addOnNumber8 = addOnNumber8;
	}
	public double getAddOnNumber11() {
		return addOnNumber11;
	}
	public void setAddOnNumber11(double addOnNumber11) {
		this.addOnNumber11 = addOnNumber11;
	}
	public double getAddOnNumber12() {
		return addOnNumber12;
	}
	public void setAddOnNumber12(double addOnNumber12) {
		this.addOnNumber12 = addOnNumber12;
	}
	public double getAddOnNumber44() {
		return addOnNumber44;
	}
	public void setAddOnNumber44(double addOnNumber44) {
		this.addOnNumber44 = addOnNumber44;
	}
	public double getTotalFreightAddOns() {
		return totalFreightAddOns;
	}
	public void setTotalFreightAddOns(double totalFreightAddOns) {
		this.totalFreightAddOns = totalFreightAddOns;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getRouteData() {
		return routeData;
	}
	public void setRouteData(String routeData) {
		this.routeData = routeData;
	}
	public double getFreightOut() {
		return freightOut;
	}
	public void setFreightOut(double freightOut) {
		this.freightOut = freightOut;
	}
	public String getReturnComment() {
		return returnComment;
	}
	public void setReturnComment(String returnComment) {
		this.returnComment = returnComment;
	}
	public double getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}
	public int getRecordLimit() {
		return recordLimit;
	}
	public void setRecordLimit(int recordLimit) {
		this.recordLimit = recordLimit;
	}
	public String getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	public String getBackorderType() {
		return backorderType;
	}
	public void setBackorderType(String backorderType) {
		this.backorderType = backorderType;
	}
	public String getAcceptBackorders() {
		return acceptBackorders;
	}
	public void setAcceptBackorders(String acceptBackorders) {
		this.acceptBackorders = acceptBackorders;
	}
	public int getReturnQty() {
		return returnQty;
	}
	public void setReturnQty(int returnQty) {
		this.returnQty = returnQty;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public String getShippingBranchName() {
		return shippingBranchName;
	}
	public void setShippingBranchName(String shippingBranchName) {
		this.shippingBranchName = shippingBranchName;
	}
	public String getSecureToken() {
		return secureToken;
	}
	public void setSecureToken(String secureToken) {
		this.secureToken = secureToken;
	}
	public String getSecureTokenId() {
		return secureTokenId;
	}
	public void setSecureTokenId(String secureTokenId) {
		this.secureTokenId = secureTokenId;
	}
	public String getShippingOrgType() {
		return shippingOrgType;
	}
	public void setShippingOrgType(String shippingOrgType) {
		this.shippingOrgType = shippingOrgType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getCustomerProfileId() {
		return customerProfileId;
	}

	public void setCustomerProfileId(String customerProfileId) {
		this.customerProfileId = customerProfileId;
	}

	public List<String> getCustomerPaymentIdList() {
		return customerPaymentIdList;
	}

	public void setCustomerPaymentIdList(List<String> customerPaymentIdList) {
		this.customerPaymentIdList = customerPaymentIdList;
	}

	public String getCustomerPaymentId() {
		return customerPaymentId;
	}

	public void setCustomerPaymentId(String customerPaymentId) {
		this.customerPaymentId = customerPaymentId;
	}

	public String getPaymentAccountId() {
		return paymentAccountId;
	}

	public void setPaymentAccountId(String paymentAccountId) {
		this.paymentAccountId = paymentAccountId;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getProfileID() {
		return profileID;
	}

	public void setProfileID(String profileID) {
		this.profileID = profileID;
	}

	public String getDataDescriptor() {
		return dataDescriptor;
	}

	public void setDataDescriptor(String dataDescriptor) {
		this.dataDescriptor = dataDescriptor;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCustomerERPId() {
		return customerERPId;
	}

	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getAvsResultCode() {
		return avsResultCode;
	}

	public void setAvsResultCode(String avsResultCode) {
		this.avsResultCode = avsResultCode;
	}

	public String getCvvResultCode() {
		return cvvResultCode;
	}

	public void setCvvResultCode(String cvvResultCode) {
		this.cvvResultCode = cvvResultCode;
	}

	public String getCavvResultCode() {
		return cavvResultCode;
	}

	public void setCavvResultCode(String cavvResultCode) {
		this.cavvResultCode = cavvResultCode;
	}

	public String getTransactionHash() {
		return transactionHash;
	}

	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getInvoiceAge() {
		return invoiceAge;
	}

	public void setInvoiceAge(String invoiceAge) {
		this.invoiceAge = invoiceAge;
	}

	public String getsPaymentAmount() {
		return sPaymentAmount;
	}

	public void setsPaymentAmount(String sPaymentAmount) {
		this.sPaymentAmount = sPaymentAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getNetDue() {
		return netDue;
	}

	public void setNetDue(double netDue) {
		this.netDue = netDue;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public String getShipViaDescription() {
		return shipViaDescription;
	}

	public void setShipViaDescription(String shipViaDescription) {
		this.shipViaDescription = shipViaDescription;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	
	public HashMap<String, String> getTrackingNumberList() {
		return trackingNumberList;
	}

	public void setTrackingNumberList(HashMap<String, String> trackingNumberList) {
		this.trackingNumberList = trackingNumberList;
	}

	public String getAccountRegId() {
		return accountRegId;
	}

	public double getAdditionalCharges10() {
		return additionalCharges10;
	}

	public double getAdditionalCharges11() {
		return additionalCharges11;
	}

	public double getAdditionalCharges12() {
		return additionalCharges12;
	}

	public double getAdditionalCharges13() {
		return additionalCharges13;
	}

	public double getAdditionalCharges14() {
		return additionalCharges14;
	}

	public double getAdditionalCharges15() {
		return additionalCharges15;
	}

	public double getAdditionalCharges16() {
		return additionalCharges16;
	}

	public double getAdditionalCharges17() {
		return additionalCharges17;
	}

	public double getAdditionalCharges18() {
		return additionalCharges18;
	}

	public double getAdditionalCharges19() {
		return additionalCharges19;
	}

	public double getAdditionalCharges2() {
		return additionalCharges2;
	}

	public double getAdditionalCharges20() {
		return additionalCharges20;
	}

	public double getAdditionalCharges21() {
		return additionalCharges21;
	}

	public double getAdditionalCharges22() {
		return additionalCharges22;
	}

	public double getAdditionalCharges23() {
		return additionalCharges23;
	}

	public double getAdditionalCharges24() {
		return additionalCharges24;
	}

	public double getAdditionalCharges25() {
		return additionalCharges25;
	}

	public double getAdditionalCharges26() {
		return additionalCharges26;
	}

	public double getAdditionalCharges27() {
		return additionalCharges27;
	}

	public double getAdditionalCharges28() {
		return additionalCharges28;
	}

	public double getAdditionalCharges29() {
		return additionalCharges29;
	}

	public double getAdditionalCharges3() {
		return additionalCharges3;
	}

	public double getAdditionalCharges30() {
		return additionalCharges30;
	}

	public double getAdditionalCharges31() {
		return additionalCharges31;
	}

	public double getAdditionalCharges32() {
		return additionalCharges32;
	}

	public double getAdditionalCharges33() {
		return additionalCharges33;
	}

	public double getAdditionalCharges34() {
		return additionalCharges34;
	}

	public double getAdditionalCharges35() {
		return additionalCharges35;
	}

	public double getAdditionalCharges36() {
		return additionalCharges36;
	}

	public double getAdditionalCharges37() {
		return additionalCharges37;
	}

	public double getAdditionalCharges4() {
		return additionalCharges4;
	}

	public double getAdditionalCharges5() {
		return additionalCharges5;
	}

	public double getAdditionalCharges6() {
		return additionalCharges6;
	}

	public double getAdditionalCharges7() {
		return additionalCharges7;
	}

	public double getAdditionalCharges8() {
		return additionalCharges8;
	}

	public double getAdditionalCharges9() {
		return additionalCharges9;
	}

	public String getAdditionalChargesDescription10() {
		return additionalChargesDescription10;
	}

	public String getAdditionalChargesDescription11() {
		return additionalChargesDescription11;
	}

	public String getAdditionalChargesDescription12() {
		return additionalChargesDescription12;
	}

	public String getAdditionalChargesDescription13() {
		return additionalChargesDescription13;
	}

	public String getAdditionalChargesDescription14() {
		return additionalChargesDescription14;
	}

	public String getAdditionalChargesDescription15() {
		return additionalChargesDescription15;
	}

	public String getAdditionalChargesDescription16() {
		return additionalChargesDescription16;
	}

	public String getAdditionalChargesDescription17() {
		return additionalChargesDescription17;
	}

	public String getAdditionalChargesDescription18() {
		return additionalChargesDescription18;
	}

	public String getAdditionalChargesDescription19() {
		return additionalChargesDescription19;
	}

	public String getAdditionalChargesDescription2() {
		return additionalChargesDescription2;
	}

	public String getAdditionalChargesDescription20() {
		return additionalChargesDescription20;
	}

	public String getAdditionalChargesDescription21() {
		return additionalChargesDescription21;
	}

	public String getAdditionalChargesDescription22() {
		return additionalChargesDescription22;
	}

	public String getAdditionalChargesDescription23() {
		return additionalChargesDescription23;
	}

	public String getAdditionalChargesDescription24() {
		return additionalChargesDescription24;
	}

	public String getAdditionalChargesDescription25() {
		return additionalChargesDescription25;
	}

	public String getAdditionalChargesDescription26() {
		return additionalChargesDescription26;
	}

	public String getAdditionalChargesDescription27() {
		return additionalChargesDescription27;
	}

	public String getAdditionalChargesDescription28() {
		return additionalChargesDescription28;
	}

	public String getAdditionalChargesDescription29() {
		return additionalChargesDescription29;
	}

	public String getAdditionalChargesDescription3() {
		return additionalChargesDescription3;
	}

	public String getAdditionalChargesDescription30() {
		return additionalChargesDescription30;
	}

	public String getAdditionalChargesDescription31() {
		return additionalChargesDescription31;
	}

	public String getAdditionalChargesDescription32() {
		return additionalChargesDescription32;
	}

	public String getAdditionalChargesDescription33() {
		return additionalChargesDescription33;
	}

	public String getAdditionalChargesDescription34() {
		return additionalChargesDescription34;
	}

	public String getAdditionalChargesDescription35() {
		return additionalChargesDescription35;
	}

	public String getAdditionalChargesDescription36() {
		return additionalChargesDescription36;
	}

	public String getAdditionalChargesDescription37() {
		return additionalChargesDescription37;
	}

	public String getAdditionalChargesDescription4() {
		return additionalChargesDescription4;
	}

	public String getAdditionalChargesDescription5() {
		return additionalChargesDescription5;
	}

	public String getAdditionalChargesDescription6() {
		return additionalChargesDescription6;
	}

	public String getAdditionalChargesDescription7() {
		return additionalChargesDescription7;
	}

	public String getAdditionalChargesDescription8() {
		return additionalChargesDescription8;
	}

	public String getAdditionalChargesDescription9() {
		return additionalChargesDescription9;
	}

	public AddressModel getBillAddress() {
		return billAddress;
	}

	/**
	 * @return the billAddress1
	 */
	public String getBillAddress1() {
		return billAddress1;
	}

	/**
	 * @return the billAddress2
	 */
	public String getBillAddress2() {
		return billAddress2;
	}

	/**
	 * @return the billCity
	 */
	public String getBillCity() {
		return billCity;
	}

	/**
	 * @return the billCountry
	 */
	public String getBillCountry() {
		return billCountry;
	}

	public String getBillEmailAddress() {
		return billEmailAddress;
	}

	/**
	 * @return the billPhone
	 */
	public String getBillPhone() {
		return billPhone;
	}

	/**
	 * @return the billState
	 */
	public String getBillState() {
		return billState;
	}

	public String getBillToEntityId() {
		return billToEntityId;
	}

	public String getBillToName() {
		return billToName;
	}

	/**
	 * @return the billZipCode
	 */
	public String getBillZipCode() {
		return billZipCode;
	}

	public String getCardholder() {
		return cardholder;
	}

	public double getCashDiscountAmount() {
		return cashDiscountAmount;
	}

	public double getCashDiscountPercentage() {
		return cashDiscountPercentage;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public String getCatalogNumber() {
		return catalogNumber;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getCreditCardExpDate() {
		return creditCardExpDate;
	}

	public String getCreditCardHolderName() {
		return creditCardHolderName;
	}

	public ArrayList<CreditCardModel> getCreditCardList() {
		return creditCardList;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public String getCustomerCountry() {
		return customerCountry;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public String getCustomerPartNumber() {
		return customerPartNumber;
	}

	public ArrayList<ProductsModel> getCustomerPartNumberList() {
		return customerPartNumberList;
	}

	public double getCustomerPrice() {
		return customerPrice;
	}

	public String getCustomerReleaseNumber() {
		return customerReleaseNumber;
	}

	public LinkedHashMap<Integer, LinkedHashMap<String, Object>> getCustomFieldVal() {
		return customFieldVal;
	}

	public double getDeliveryCharge() {
		return deliveryCharge;
	}

	public String getDescription() {
		return Description;
	}

	public String getDescription1() {
		return description1;
	}

	public String getDescription2() {
		return description2;
	}

	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}

	public String getDiscountCouponCode() {
		return discountCouponCode;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getEntityId() {
		return entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getErpOrderMessage() {
		return erpOrderMessage;
	}

	public String getErpOrderNumber() {
		return erpOrderNumber;
	}

	public int getErpQty() {
		return erpQty;
	}

	public String getExternalSysError() {
		return externalSysError;
	}

	public String getExternalSystemId() {
		return externalSystemId;
	}

	public double getExtPrice() {
		return extPrice;
	}

	public double getFederalExciseTax() {
		return federalExciseTax;
	}

	public String getFieldname() {
		return fieldname;
	}

	public String getFieldvalue() {
		return fieldvalue;
	}

	/**
	 * @return the freight
	 */
	public double getFreight() {
		return freight;
	}

	public String getFreightDesc() {
		return freightDesc;
	}

	public double getFreightIn() {
		return freightIn;
	}

	public String getFreightInDesc() {
		return freightInDesc;
	}

	public String getGasPoNumber() {
		return gasPoNumber;
	}

	/**
	 * @return the geneId
	 */
	public String getGeneId() {
		return geneId;
	}

	/**
	 * @return the handling
	 */
	public double getHandling() {
		return handling;
	}

	public String getHazardiousMaterial() {
		return hazardiousMaterial;
	}

	public String getHomeBranchName() {
		return homeBranchName;
	}

	public String getImageName() {
		return imageName;
	}

	public String getImageType() {
		return imageType;
	}

	public String getInternalNotes() {
		return internalNotes;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public String getIsOverSize() {
		return isOverSize;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	public String getItemLevelRequiredByDate() {
		return itemLevelRequiredByDate;
	}

	public int getItemPriceId() {
		return itemPriceId;
	}

	public String getLeadTime() {
		return leadTime;
	}

	public String getLineItemComment() {
		return lineItemComment;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public double getListPrice() {
		return listPrice;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}

	public String getMaterialGroup() {
		return materialGroup;
	}

	public String getMultipleShipVia() {
		return multipleShipVia;
	}

	public String getMultipleShipViaDesc() {
		return multipleShipViaDesc;
	}

	public String getName() {
		return name;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public double getNetOrdered() {
		return netOrdered;
	}

	public ArrayList<ProductsModel> getNonCatalogItem() {
		return nonCatalogItem;
	}

	public double getOpenOrderAmount() {
		return openOrderAmount;
	}

	public String getOrderComment() {
		return orderComment;
	}

	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public String getOrderEnterDate() {
		return orderEnterDate;
	}

	/**
	 * @return the orderId
	 */
	public int getOrderId() {
		return orderId;
	}

	public String getOrderID() {
		return orderID;
	}

	public ArrayList<SalesModel> getOrderItem() {
		return orderItem;
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public double getOrderItemsDiscount() {
		return orderItemsDiscount;
	}

	public ArrayList<SalesModel> getOrderList() {
		return orderList;
	}

	public String getOrderNotes() {
		return orderNotes;
	}

	/**
	 * @return the orderNum
	 */
	public String getOrderNum() {
		return orderNum;
	}

	/**
	 * @return the orderPrice
	 */
	public double getOrderPrice() {
		return orderPrice;
	}

	public String getOrderPriceStr() {
		return orderPriceStr;
	}

	/**
	 * @return the orderQty
	 */
	public int getOrderQty() {
		return orderQty;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	public String getOrderStatusCode() {
		return OrderStatusCode;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public String getOrderStatusType() {
		return orderStatusType;
	}

	public int getOrderSuffix() {
		return orderSuffix;
	}

	/**
	 * @return the orderUom
	 */
	public String getOrderUom() {
		return orderUom;
	}

	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public String getPaymentAuthCode() {
		return paymentAuthCode;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public String getPaymentRefrenceId() {
		return paymentRefrenceId;
	}

	public int getPerQty() {
		return perQty;
	}

	public String getPhone() {
		return phone;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getPostDate() {
		return postDate;
	}

	public Boolean getPreAuthorize() {
		return preAuthorize;
	}

	public double getPrice() {
		return price;
	}

	public String getPriceString() {
		return priceString;
	}

	public String getPrintStatus() {
		return printStatus;
	}

	public String getProductcode() {
		return productcode;
	}

	public String getProductKeywords() {
		return productKeywords;
	}

	public String getPromiseDate() {
		return promiseDate;
	}

	public int getQtyordered() {
		return qtyordered;
	}

	public int getQtyShipped() {
		return qtyShipped;
	}

	public String getQtyUom() {
		return qtyUom;
	}

	public double getQuantityOrdered() {
		return quantityOrdered;
	}

	public double getQuantityShipped() {
		return quantityShipped;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public String getRefrenceKey() {
		return refrenceKey;
	}

	public String getReqDate() {
		return reqDate;
	}

	public String getReqType() {
		return reqType;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public double getrOEDiscount() {
		return rOEDiscount;
	}

	public String getSalesPersonCode() {
		return salesPersonCode;
	}

	public String getSalesUom() {
		return salesUom;
	}

	public String getSearchString() {
		return searchString;
	}

	public String getSendmailToSalesRepOnly() {
		return sendmailToSalesRepOnly;
	}

	public String getSendQuoteMail() {
		return sendQuoteMail;
	}

	public int getSeqnum() {
		return seqnum;
	}

	public HttpSession getSession() {
		return session;
	}

	public String getSessionId() {
		return sessionId;
	}

	public AddressModel getShipAddress() {
		return shipAddress;
	}

	/**
	 * @return the shipAddress1
	 */
	public String getShipAddress1() {
		return shipAddress1;
	}

	/**
	 * @return the shipAddress2
	 */
	public String getShipAddress2() {
		return shipAddress2;
	}

	/**
	 * @return the shipCity
	 */
	public String getShipCity() {
		return shipCity;
	}

	/**
	 * @return the shipCountry
	 */
	public String getShipCountry() {
		return shipCountry;
	}

	public String getShipDate() {
		return shipDate;
	}

	public String getShipEmailAddress() {
		return shipEmailAddress;
	}

	public String getShipFirstName() {
		return shipFirstName;
	}

	public String getShipLastName() {
		return shipLastName;
	}

	/**
	 * @return the shipMethod
	 */
	public String getShipMethod() {
		return shipMethod;
	}

	public String getShipMethodId() {
		return shipMethodId;
	}

	public AddressModel getShippedAddress() {
		return shippedAddress;
	}

	/**
	 * @return the shipPhone
	 */
	public String getShipPhone() {
		return shipPhone;
	}

	public String getShippingCourier() {
		return shippingCourier;
	}

	public String getShippingInstruction() {
		return shippingInstruction;
	}

	/**
	 * @return the shipState
	 */
	public String getShipState() {
		return shipState;
	}

	public String getShipToEntityId() {
		return shipToEntityId;
	}

	public String getShipToId() {
		return shipToId;
	}

	public String getShipToName() {
		return shipToName;
	}

	public String getShipViaErpId() {
		return shipViaErpId;
	}

	public String getShipViaID() {
		return ShipViaID;
	}

	public String getShipViaMethod() {
		return ShipViaMethod;
	}

	/**
	 * @return the shipZipCode
	 */
	public String getShipZipCode() {
		return shipZipCode;
	}

	/**
	 * @return the shortDesc
	 */
	public String getShortDesc() {
		return shortDesc;
	}

	public String getSlsrepin() {
		return slsrepin;
	}

	public String getSlsrepout() {
		return slsrepout;
	}

	public String getStage() {
		return stage;
	}

	public int getStageCode() {
		return stageCode;
	}

	public String getStageCodeString() {
		return stageCodeString;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getStringPrice() {
		return stringPrice;
	}

	public String getStringTotal() {
		return stringTotal;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public double getSubtotalV2() {
		return subtotalV2;
	}

	public String getTakenBy() {
		return takenBy;
	}

	/**
	 * @return the tax
	 */
	public double getTax() {
		return tax;
	}

	public String getTermsdiscamt() {
		return termsdiscamt;
	}

	public String getTermstypedesc() {
		return termstypedesc;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	public double getTotalLineAmount() {
		return totalLineAmount;
	}

	public double getTotalSavingsOnOrder() {
		return totalSavingsOnOrder;
	}

	public String getTotalStr() {
		return totalStr;
	}

	public String getTotalStrV2() {
		return totalStrV2;
	}

	public double getTotalV2() {
		return totalV2;
	}

	public double getTotinvamt() {
		return totinvamt;
	}

	public String getTrackingInfo() {
		return trackingInfo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getTranstype() {
		return transtype;
	}

	public double getUnitPrice() {
		return unitPrice;
	}
	

	public double getUnitsPerStocking() {
		return unitsPerStocking;
	}

	public String getUnitsPerStockingString() {
		return unitsPerStockingString;
	}

	public String getUnspc() {
		return unspc;
	}

	public String getUom() {
		return uom;
	}

	public ArrayList<String> getUomList() {
		return uomList;
	}

	public String getUpc() {
		return upc;
	}

	public HashMap<String, String> getUpcList() {
		return upcList;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserNote() {
		return userNote;
	}

	public String getUserToken() {
		return userToken;
	}

	public String getWareHouseCode() {
		return wareHouseCode;
	}

	public String getWarehouseLocation() {
		return WarehouseLocation;
	}

	public String getWholeOrderDiscountAmount() {
		return wholeOrderDiscountAmount;
	}

	public String getWrittenBy() {
		return writtenBy;
	}

	public boolean isDisableAddToCart() {
		return disableAddToCart;
	}

	public boolean isFreeShipping() {
		return freeShipping;
	}

	public boolean isSendMailFlag() {
		return sendMailFlag;
	}

	public void setAccountRegId(String accountRegId) {
		this.accountRegId = accountRegId;
	}

	public void setAdditionalCharges10(double additionalCharges10) {
		this.additionalCharges10 = additionalCharges10;
	}

	public void setAdditionalCharges11(double additionalCharges11) {
		this.additionalCharges11 = additionalCharges11;
	}

	public void setAdditionalCharges12(double additionalCharges12) {
		this.additionalCharges12 = additionalCharges12;
	}

	public void setAdditionalCharges13(double additionalCharges13) {
		this.additionalCharges13 = additionalCharges13;
	}

	public void setAdditionalCharges14(double additionalCharges14) {
		this.additionalCharges14 = additionalCharges14;
	}

	public void setAdditionalCharges15(double additionalCharges15) {
		this.additionalCharges15 = additionalCharges15;
	}

	public void setAdditionalCharges16(double additionalCharges16) {
		this.additionalCharges16 = additionalCharges16;
	}

	public void setAdditionalCharges17(double additionalCharges17) {
		this.additionalCharges17 = additionalCharges17;
	}

	public void setAdditionalCharges18(double additionalCharges18) {
		this.additionalCharges18 = additionalCharges18;
	}

	public void setAdditionalCharges19(double additionalCharges19) {
		this.additionalCharges19 = additionalCharges19;
	}

	public void setAdditionalCharges2(double additionalCharges2) {
		this.additionalCharges2 = additionalCharges2;
	}

	public void setAdditionalCharges20(double additionalCharges20) {
		this.additionalCharges20 = additionalCharges20;
	}

	public void setAdditionalCharges21(double additionalCharges21) {
		this.additionalCharges21 = additionalCharges21;
	}

	public void setAdditionalCharges22(double additionalCharges22) {
		this.additionalCharges22 = additionalCharges22;
	}

	public void setAdditionalCharges23(double additionalCharges23) {
		this.additionalCharges23 = additionalCharges23;
	}

	public void setAdditionalCharges24(double additionalCharges24) {
		this.additionalCharges24 = additionalCharges24;
	}

	public void setAdditionalCharges25(double additionalCharges25) {
		this.additionalCharges25 = additionalCharges25;
	}

	public void setAdditionalCharges26(double additionalCharges26) {
		this.additionalCharges26 = additionalCharges26;
	}

	public void setAdditionalCharges27(double additionalCharges27) {
		this.additionalCharges27 = additionalCharges27;
	}

	public void setAdditionalCharges28(double additionalCharges28) {
		this.additionalCharges28 = additionalCharges28;
	}

	public void setAdditionalCharges29(double additionalCharges29) {
		this.additionalCharges29 = additionalCharges29;
	}

	public void setAdditionalCharges3(double additionalCharges3) {
		this.additionalCharges3 = additionalCharges3;
	}

	public void setAdditionalCharges30(double additionalCharges30) {
		this.additionalCharges30 = additionalCharges30;
	}

	public void setAdditionalCharges31(double additionalCharges31) {
		this.additionalCharges31 = additionalCharges31;
	}

	public void setAdditionalCharges32(double additionalCharges32) {
		this.additionalCharges32 = additionalCharges32;
	}

	public void setAdditionalCharges33(double additionalCharges33) {
		this.additionalCharges33 = additionalCharges33;
	}

	public void setAdditionalCharges34(double additionalCharges34) {
		this.additionalCharges34 = additionalCharges34;
	}

	public void setAdditionalCharges35(double additionalCharges35) {
		this.additionalCharges35 = additionalCharges35;
	}

	public void setAdditionalCharges36(double additionalCharges36) {
		this.additionalCharges36 = additionalCharges36;
	}

	public void setAdditionalCharges37(double additionalCharges37) {
		this.additionalCharges37 = additionalCharges37;
	}

	public void setAdditionalCharges4(double additionalCharges4) {
		this.additionalCharges4 = additionalCharges4;
	}

	public void setAdditionalCharges5(double additionalCharges5) {
		this.additionalCharges5 = additionalCharges5;
	}

	public void setAdditionalCharges6(double additionalCharges6) {
		this.additionalCharges6 = additionalCharges6;
	}

	public void setAdditionalCharges7(double additionalCharges7) {
		this.additionalCharges7 = additionalCharges7;
	}

	public void setAdditionalCharges8(double additionalCharges8) {
		this.additionalCharges8 = additionalCharges8;
	}

	public void setAdditionalCharges9(double additionalCharges9) {
		this.additionalCharges9 = additionalCharges9;
	}

	public void setAdditionalChargesDescription10(String additionalChargesDescription10) {
		this.additionalChargesDescription10 = additionalChargesDescription10;
	}

	public void setAdditionalChargesDescription11(String additionalChargesDescription11) {
		this.additionalChargesDescription11 = additionalChargesDescription11;
	}

	public void setAdditionalChargesDescription12(String additionalChargesDescription12) {
		this.additionalChargesDescription12 = additionalChargesDescription12;
	}

	public void setAdditionalChargesDescription13(String additionalChargesDescription13) {
		this.additionalChargesDescription13 = additionalChargesDescription13;
	}

	public void setAdditionalChargesDescription14(String additionalChargesDescription14) {
		this.additionalChargesDescription14 = additionalChargesDescription14;
	}

	public void setAdditionalChargesDescription15(String additionalChargesDescription15) {
		this.additionalChargesDescription15 = additionalChargesDescription15;
	}

	public void setAdditionalChargesDescription16(String additionalChargesDescription16) {
		this.additionalChargesDescription16 = additionalChargesDescription16;
	}

	public void setAdditionalChargesDescription17(String additionalChargesDescription17) {
		this.additionalChargesDescription17 = additionalChargesDescription17;
	}

	public void setAdditionalChargesDescription18(String additionalChargesDescription18) {
		this.additionalChargesDescription18 = additionalChargesDescription18;
	}

	public void setAdditionalChargesDescription19(String additionalChargesDescription19) {
		this.additionalChargesDescription19 = additionalChargesDescription19;
	}

	public void setAdditionalChargesDescription2(String additionalChargesDescription2) {
		this.additionalChargesDescription2 = additionalChargesDescription2;
	}

	public void setAdditionalChargesDescription20(String additionalChargesDescription20) {
		this.additionalChargesDescription20 = additionalChargesDescription20;
	}

	public void setAdditionalChargesDescription21(String additionalChargesDescription21) {
		this.additionalChargesDescription21 = additionalChargesDescription21;
	}

	public void setAdditionalChargesDescription22(String additionalChargesDescription22) {
		this.additionalChargesDescription22 = additionalChargesDescription22;
	}

	public void setAdditionalChargesDescription23(String additionalChargesDescription23) {
		this.additionalChargesDescription23 = additionalChargesDescription23;
	}

	public void setAdditionalChargesDescription24(String additionalChargesDescription24) {
		this.additionalChargesDescription24 = additionalChargesDescription24;
	}

	public void setAdditionalChargesDescription25(String additionalChargesDescription25) {
		this.additionalChargesDescription25 = additionalChargesDescription25;
	}

	public void setAdditionalChargesDescription26(String additionalChargesDescription26) {
		this.additionalChargesDescription26 = additionalChargesDescription26;
	}

	public void setAdditionalChargesDescription27(String additionalChargesDescription27) {
		this.additionalChargesDescription27 = additionalChargesDescription27;
	}

	public void setAdditionalChargesDescription28(String additionalChargesDescription28) {
		this.additionalChargesDescription28 = additionalChargesDescription28;
	}

	public void setAdditionalChargesDescription29(String additionalChargesDescription29) {
		this.additionalChargesDescription29 = additionalChargesDescription29;
	}

	public void setAdditionalChargesDescription3(String additionalChargesDescription3) {
		this.additionalChargesDescription3 = additionalChargesDescription3;
	}

	public void setAdditionalChargesDescription30(String additionalChargesDescription30) {
		this.additionalChargesDescription30 = additionalChargesDescription30;
	}

	public void setAdditionalChargesDescription31(String additionalChargesDescription31) {
		this.additionalChargesDescription31 = additionalChargesDescription31;
	}

	public void setAdditionalChargesDescription32(String additionalChargesDescription32) {
		this.additionalChargesDescription32 = additionalChargesDescription32;
	}

	public void setAdditionalChargesDescription33(String additionalChargesDescription33) {
		this.additionalChargesDescription33 = additionalChargesDescription33;
	}

	public void setAdditionalChargesDescription34(String additionalChargesDescription34) {
		this.additionalChargesDescription34 = additionalChargesDescription34;
	}

	public void setAdditionalChargesDescription35(String additionalChargesDescription35) {
		this.additionalChargesDescription35 = additionalChargesDescription35;
	}

	public void setAdditionalChargesDescription36(String additionalChargesDescription36) {
		this.additionalChargesDescription36 = additionalChargesDescription36;
	}

	public void setAdditionalChargesDescription37(String additionalChargesDescription37) {
		this.additionalChargesDescription37 = additionalChargesDescription37;
	}

	public void setAdditionalChargesDescription4(String additionalChargesDescription4) {
		this.additionalChargesDescription4 = additionalChargesDescription4;
	}

	public void setAdditionalChargesDescription5(String additionalChargesDescription5) {
		this.additionalChargesDescription5 = additionalChargesDescription5;
	}

	public void setAdditionalChargesDescription6(String additionalChargesDescription6) {
		this.additionalChargesDescription6 = additionalChargesDescription6;
	}

	public void setAdditionalChargesDescription7(String additionalChargesDescription7) {
		this.additionalChargesDescription7 = additionalChargesDescription7;
	}

	public void setAdditionalChargesDescription8(String additionalChargesDescription8) {
		this.additionalChargesDescription8 = additionalChargesDescription8;
	}

	public void setAdditionalChargesDescription9(String additionalChargesDescription9) {
		this.additionalChargesDescription9 = additionalChargesDescription9;
	}

	public void setBillAddress(AddressModel billAddress) {
		this.billAddress = billAddress;
	}

	/**
	 * @param billAddress1
	 *            the billAddress1 to set
	 */
	public void setBillAddress1(String billAddress1) {
		this.billAddress1 = billAddress1;
	}

	/**
	 * @param billAddress2
	 *            the billAddress2 to set
	 */
	public void setBillAddress2(String billAddress2) {
		this.billAddress2 = billAddress2;
	}

	/**
	 * @param billCity
	 *            the billCity to set
	 */
	public void setBillCity(String billCity) {
		this.billCity = billCity;
	}

	/**
	 * @param billCountry
	 *            the billCountry to set
	 */
	public void setBillCountry(String billCountry) {
		this.billCountry = billCountry;
	}

	public void setBillEmailAddress(String billEmailAddress) {
		this.billEmailAddress = billEmailAddress;
	}

	/**
	 * @param billPhone
	 *            the billPhone to set
	 */
	public void setBillPhone(String billPhone) {
		this.billPhone = billPhone;
	}

	/**
	 * @param billState
	 *            the billState to set
	 */
	public void setBillState(String billState) {
		this.billState = billState;
	}

	public void setBillToEntityId(String billToEntityId) {
		this.billToEntityId = billToEntityId;
	}

	public void setBillToName(String billToName) {
		this.billToName = billToName;
	}

	/**
	 * @param billZipCode
	 *            the billZipCode to set
	 */
	public void setBillZipCode(String billZipCode) {
		this.billZipCode = billZipCode;
	}

	public void setCardholder(String cardholder) {
		this.cardholder = cardholder;
	}

	public void setCashDiscountAmount(double cashDiscountAmount) {
		this.cashDiscountAmount = cashDiscountAmount;
	}

	public void setCashDiscountPercentage(double cashDiscountPercentage) {
		this.cashDiscountPercentage = cashDiscountPercentage;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setCreditCardExpDate(String creditCardExpDate) {
		this.creditCardExpDate = creditCardExpDate;
	}

	public void setCreditCardHolderName(String creditCardHolderName) {
		this.creditCardHolderName = creditCardHolderName;
	}

	public void setCreditCardList(ArrayList<CreditCardModel> creditCardList) {
		this.creditCardList = creditCardList;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public void setCustomerCountry(String customerCountry) {
		this.customerCountry = customerCountry;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public void setCustomerPartNumber(String customerPartNumber) {
		this.customerPartNumber = customerPartNumber;
	}

	public void setCustomerPartNumberList(ArrayList<ProductsModel> customerPartNumberList) {
		this.customerPartNumberList = customerPartNumberList;
	}

	public void setCustomerPrice(double customerPrice) {
		this.customerPrice = customerPrice;
	}

	public void setCustomerReleaseNumber(String customerReleaseNumber) {
		this.customerReleaseNumber = customerReleaseNumber;
	}

	public void setCustomFieldVal(LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal) {
		this.customFieldVal = customFieldVal;
	}

	public void setDeliveryCharge(double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public void setDisableAddToCart(boolean disableAddToCart) {
		this.disableAddToCart = disableAddToCart;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public void setDiscountCouponCode(String discountCouponCode) {
		this.discountCouponCode = discountCouponCode;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setErpOrderMessage(String erpOrderMessage) {
		this.erpOrderMessage = erpOrderMessage;
	}

	public void setErpOrderNumber(String erpOrderNumber) {
		this.erpOrderNumber = erpOrderNumber;
	}

	public void setErpQty(int erpQty) {
		this.erpQty = erpQty;
	}

	public void setExternalSysError(String externalSysError) {
		this.externalSysError = externalSysError;
	}

	public void setExternalSystemId(String externalSystemId) {
		this.externalSystemId = externalSystemId;
	}

	public void setExtPrice(double extPrice) {
		this.extPrice = extPrice;
	}

	public void setFederalExciseTax(double federalExciseTax) {
		this.federalExciseTax = federalExciseTax;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public void setFieldvalue(String fieldvalue) {
		this.fieldvalue = fieldvalue;
	}

	public void setFreeShipping(boolean freeShipping) {
		this.freeShipping = freeShipping;
	}

	/**
	 * @param freight
	 *            the freight to set
	 */
	public void setFreight(double freight) {
		this.freight = freight;
	}

	public void setFreightDesc(String freightDesc) {
		this.freightDesc = freightDesc;
	}

	public void setFreightIn(double freightIn) {
		this.freightIn = freightIn;
	}

	public void setFreightInDesc(String freightInDesc) {
		this.freightInDesc = freightInDesc;
	}

	public void setGasPoNumber(String gasPoNumber) {
		this.gasPoNumber = gasPoNumber;
	}

	/**
	 * @param geneId
	 *            the geneId to set
	 */
	public void setGeneId(String geneId) {
		this.geneId = geneId;
	}

	/**
	 * @param handling
	 *            the handling to set
	 */
	public void setHandling(double handling) {
		this.handling = handling;
	}

	public void setHazardiousMaterial(String hazardiousMaterial) {
		this.hazardiousMaterial = hazardiousMaterial;
	}

	public void setHomeBranchName(String homeBranchName) {
		this.homeBranchName = homeBranchName;
	}

	/**
	 * @param subTotal
	 *            the subTotal to set
	 */

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public void setIsOverSize(String isOverSize) {
		this.isOverSize = isOverSize;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void setItemLevelRequiredByDate(String itemLevelRequiredByDate) {
		this.itemLevelRequiredByDate = itemLevelRequiredByDate;
	}

	public void setItemPriceId(int itemPriceId) {
		this.itemPriceId = itemPriceId;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	public void setLineItemComment(String lineItemComment) {
		this.lineItemComment = lineItemComment;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}

	public void setMaterialGroup(String materialGroup) {
		this.materialGroup = materialGroup;
	}

	public void setMultipleShipVia(String multipleShipVia) {
		this.multipleShipVia = multipleShipVia;
	}

	public void setMultipleShipViaDesc(String multipleShipViaDesc) {
		this.multipleShipViaDesc = multipleShipViaDesc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public void setNetOrdered(double netOrdered) {
		this.netOrdered = netOrdered;
	}

	public void setNonCatalogItem(ArrayList<ProductsModel> nonCatalogItem) {
		this.nonCatalogItem = nonCatalogItem;
	}

	public void setOpenOrderAmount(double openOrderAmount) {
		this.openOrderAmount = openOrderAmount;
	}

	public void setOrderComment(String orderComment) {
		this.orderComment = orderComment;
	}

	/**
	 * @param orderDate
	 *            the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	public void setOrderEnterDate(String orderEnterDate) {
		this.orderEnterDate = orderEnterDate;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public void setOrderItem(ArrayList<SalesModel> orderItem) {
		this.orderItem = orderItem;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public void setOrderItemsDiscount(double orderItemsDiscount) {
		this.orderItemsDiscount = orderItemsDiscount;
	}

	public void setOrderList(ArrayList<SalesModel> orderList) {
		this.orderList = orderList;
	}

	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}

	/**
	 * @param orderNum
	 *            the orderNum to set
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * @return the eclipseOrderNum
	 */
	/**
	 * @param orderPrice
	 *            the orderPrice to set
	 */
	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public void setOrderPriceStr(String orderPriceStr) {
		this.orderPriceStr = orderPriceStr;
	}

	/**
	 * @param orderQty
	 *            the orderQty to set
	 */
	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}

	/**
	 * @param orderStatus
	 *            the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setOrderStatusCode(String orderStatusCode) {
		OrderStatusCode = orderStatusCode;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	public void setOrderStatusType(String orderStatusType) {
		this.orderStatusType = orderStatusType;
	}

	public void setOrderSuffix(int orderSuffix) {
		this.orderSuffix = orderSuffix;
	}

	/**
	 * @param orderUom
	 *            the orderUom to set
	 */
	public void setOrderUom(String orderUom) {
		this.orderUom = orderUom;
	}

	/**
	 * @param partNumber
	 *            the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public void setPaymentAuthCode(String paymentAuthCode) {
		this.paymentAuthCode = paymentAuthCode;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setPaymentRefrenceId(String paymentRefrenceId) {
		this.paymentRefrenceId = paymentRefrenceId;
	}

	/*
	 * public void setOrderPriceList(ArrayList<SalesModel> orderPriceList) {
	 * this.orderPriceList = orderPriceList; } public ArrayList<SalesModel>
	 * getOrderPriceList() { return orderPriceList; }
	 */
	public void setPerQty(int perQty) {
		this.perQty = perQty;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public void setPreAuthorize(Boolean preAuthorize) {
		this.preAuthorize = preAuthorize;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setPriceString(String priceString) {
		this.priceString = priceString;
	}

	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public void setProductKeywords(String productKeywords) {
		this.productKeywords = productKeywords;
	}

	public void setPromiseDate(String promiseDate) {
		this.promiseDate = promiseDate;
	}

	public void setQtyordered(int qtyordered) {
		this.qtyordered = qtyordered;
	}

	public void setQtyShipped(int qtyShipped) {
		this.qtyShipped = qtyShipped;
	}

	public void setQtyUom(String qtyUom) {
		this.qtyUom = qtyUom;
	}

	public void setQuantityOrdered(double quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public void setQuantityShipped(double quantityShipped) {
		this.quantityShipped = quantityShipped;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public void setRefrenceKey(String refrenceKey) {
		this.refrenceKey = refrenceKey;
	}

	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public void setrOEDiscount(double rOEDiscount) {
		this.rOEDiscount = rOEDiscount;
	}

	public void setSalesPersonCode(String salesPersonCode) {
		this.salesPersonCode = salesPersonCode;
	}

	public void setSalesUom(String salesUom) {
		this.salesUom = salesUom;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public void setSendMailFlag(boolean sendMailFlag) {
		this.sendMailFlag = sendMailFlag;
	}

	public void setSendmailToSalesRepOnly(String sendmailToSalesRepOnly) {
		this.sendmailToSalesRepOnly = sendmailToSalesRepOnly;
	}

	public void setSendQuoteMail(String sendQuoteMail) {
		this.sendQuoteMail = sendQuoteMail;
	}

	public void setSeqnum(int seqnum) {
		this.seqnum = seqnum;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setShipAddress(AddressModel shipAddress) {
		this.shipAddress = shipAddress;
	}

	/**
	 * @param shipAddress1
	 *            the shipAddress1 to set
	 */
	public void setShipAddress1(String shipAddress1) {
		this.shipAddress1 = shipAddress1;
	}

	/**
	 * @param shipAddress2
	 *            the shipAddress2 to set
	 */
	public void setShipAddress2(String shipAddress2) {
		this.shipAddress2 = shipAddress2;
	}

	/**
	 * @param shipCity
	 *            the shipCity to set
	 */
	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	/**
	 * @param shipCountry
	 *            the shipCountry to set
	 */
	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	public void setShipEmailAddress(String shipEmailAddress) {
		this.shipEmailAddress = shipEmailAddress;
	}

	public void setShipFirstName(String shipFirstName) {
		this.shipFirstName = shipFirstName;
	}

	public void setShipLastName(String shipLastName) {
		this.shipLastName = shipLastName;
	}

	/**
	 * @param shipMethod
	 *            the shipMethod to set
	 */
	public void setShipMethod(String shipMethod) {
		this.shipMethod = shipMethod;
	}

	public void setShipMethodId(String shipMethodId) {
		this.shipMethodId = shipMethodId;
	}

	public void setShippedAddress(AddressModel shippedAddress) {
		this.shippedAddress = shippedAddress;
	}

	/**
	 * @param shipPhone
	 *            the shipPhone to set
	 */
	public void setShipPhone(String shipPhone) {
		this.shipPhone = shipPhone;
	}

	public void setShippingCourier(String shippingCourier) {
		this.shippingCourier = shippingCourier;
	}

	public void setShippingInstruction(String shippingInstruction) {
		this.shippingInstruction = shippingInstruction;
	}

	/**
	 * @param shipState
	 *            the shipState to set
	 */
	public void setShipState(String shipState) {
		this.shipState = shipState;
	}

	public void setShipToEntityId(String shipToEntityId) {
		this.shipToEntityId = shipToEntityId;
	}

	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}

	public void setShipViaErpId(String shipViaErpId) {
		this.shipViaErpId = shipViaErpId;
	}

	public void setShipViaID(String shipViaID) {
		ShipViaID = shipViaID;
	}

	public void setShipViaMethod(String shipViaMethod) {
		ShipViaMethod = shipViaMethod;
	}

	/**
	 * @param shipZipCode
	 *            the shipZipCode to set
	 */
	public void setShipZipCode(String shipZipCode) {
		this.shipZipCode = shipZipCode;
	}

	/**
	 * @param shortDesc
	 *            the shortDesc to set
	 */
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public void setSlsrepin(String slsrepin) {
		this.slsrepin = slsrepin;
	}

	public void setSlsrepout(String slsrepout) {
		this.slsrepout = slsrepout;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public void setStageCode(int stageCode) {
		this.stageCode = stageCode;
	}

	public void setStageCodeString(String stageCodeString) {
		this.stageCodeString = stageCodeString;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public void setStringPrice(String stringPrice) {
		this.stringPrice = stringPrice;
	}

	public void setStringTotal(String stringTotal) {
		this.stringTotal = stringTotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public void setSubtotalV2(double subtotalV2) {
		this.subtotalV2 = subtotalV2;
	}

	public void setTakenBy(String takenBy) {
		this.takenBy = takenBy;
	}

	/**
	 * @param tax
	 *            the tax to set
	 */
	public void setTax(double tax) {
		this.tax = tax;
	}

	public void setTermsdiscamt(String termsdiscamt) {
		this.termsdiscamt = termsdiscamt;
	}

	public void setTermstypedesc(String termstypedesc) {
		this.termstypedesc = termstypedesc;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	public void setTotalLineAmount(double totalLineAmount) {
		this.totalLineAmount = totalLineAmount;
	}

	public void setTotalSavingsOnOrder(double totalSavingsOnOrder) {
		this.totalSavingsOnOrder = totalSavingsOnOrder;
	}

	public void setTotalStr(String totalStr) {
		this.totalStr = totalStr;
	}

	public void setTotalStrV2(String totalStrV2) {
		this.totalStrV2 = totalStrV2;
	}

	public void setTotalV2(double totalV2) {
		this.totalV2 = totalV2;
	}

	public void setTotinvamt(double totinvamt) {
		this.totinvamt = totinvamt;
	}

	public void setTrackingInfo(String trackingInfo) {
		this.trackingInfo = trackingInfo;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setUnitsPerStocking(double unitsPerStocking) {
		this.unitsPerStocking = unitsPerStocking;
	}

	public void setUnitsPerStockingString(String unitsPerStockingString) {
		this.unitsPerStockingString = unitsPerStockingString;
	}

	public void setUnspc(String unspc) {
		this.unspc = unspc;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public void setUomList(ArrayList<String> uomList) {
		this.uomList = uomList;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public void setUpcList(HashMap<String, String> upcList) {
		this.upcList = upcList;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getRequiredByDate() {
		return requiredByDate;
	}
	public void setRequiredByDate(String requiredByDate) {
		this.requiredByDate = requiredByDate;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserNote(String userNote) {
		this.userNote = userNote;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}

	public void setWarehouseLocation(String warehouseLocation) {
		WarehouseLocation = warehouseLocation;
	}

	public void setWholeOrderDiscountAmount(String wholeOrderDiscountAmount) {
		this.wholeOrderDiscountAmount = wholeOrderDiscountAmount;
	}

	public void setWrittenBy(String writtenBy) {
		this.writtenBy = writtenBy;
	}

	public ArrayList<String> getLineItemCommentsList() {
		return lineItemCommentsList;
	}

	public void setLineItemCommentsList(ArrayList<String> lineItemCommentsList) {
		this.lineItemCommentsList = lineItemCommentsList;
	}

	public String getGetPriceFrom() {
		return getPriceFrom;
	}

	public void setGetPriceFrom(String getPriceFrom) {
		this.getPriceFrom = getPriceFrom;
	}
	public String getCarrierTrackingNumber() {
		return carrierTrackingNumber;
	}

	public void setCarrierTrackingNumber(String carrierTrackingNumber) {
		this.carrierTrackingNumber = carrierTrackingNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getAmountPayed() {
		return amountPayed;
	}

	public void setAmountPayed(double amountPayed) {
		this.amountPayed = amountPayed;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getDocumentLinks() {
		return documentLinks;
	}

	public void setDocumentLinks(String documentLinks) {
		this.documentLinks = documentLinks;
	}

	public ArrayList<ProductsModel> getCartData() {
		return cartData;
	}

	public void setCartData(ArrayList<ProductsModel> cartData) {
		this.cartData = cartData;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public boolean isSaveCard() {
		return saveCard;
	}

	public void setSaveCard(boolean saveCard) {
		this.saveCard = saveCard;
	}

	public String getErpOverrideFlag() {
		return erpOverrideFlag;
	}

	public void setErpOverrideFlag(String erpOverrideFlag) {
		this.erpOverrideFlag = erpOverrideFlag;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getExternalCartId() {
		return externalCartId;
	}

	public void setExternalCartId(String externalCartId) {
		this.externalCartId = externalCartId;
	}

	public boolean isTaxable() {
		return taxable;
	}

	public void setTaxable(boolean taxable) {
		this.taxable = taxable;
	}

	public String getJurisdictionCode() {
		return jurisdictionCode;
	}

	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}

	public String getAltPartNumber() {
		return altPartNumber;
	}

	public void setAltPartNumber(String altPartNumber) {
		this.altPartNumber = altPartNumber;
	}

	public double getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(double otherCharges) {
		this.otherCharges = otherCharges;
	}

	public WarehouseModel getWareHouseDetails() {
		return wareHouseDetails;
	}

	public void setWareHouseDetails(WarehouseModel wareHouseDetail) {
		this.wareHouseDetails = wareHouseDetail;
	}

	public String getShippingBranchId() {
		return shippingBranchId;
	}

	public void setShippingBranchId(String shippingBranchId) {
		this.shippingBranchId = shippingBranchId;
	}
	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getAltPartNumber1() {
		return altPartNumber1;
	}

	public void setAltPartNumber1(String altPartNumber1) {
		this.altPartNumber1 = altPartNumber1;
	}

	public String getAltPartNumber2() {
		return altPartNumber2;
	}

	public void setAltPartNumber2(String altPartNumber2) {
		this.altPartNumber2 = altPartNumber2;
	}

	public ArrayList<Cimm2BCentralShipVia> getCimm2BCentralShipVia() {
		return cimm2BCentralShipVia;
	}

	public void setCimm2BCentralShipVia(ArrayList<Cimm2BCentralShipVia> cimm2bCentralShipVia) {
		cimm2BCentralShipVia = cimm2bCentralShipVia;
	}

	public String getOrderLastDate() {
		return orderLastDate;
	}

	public void setOrderLastDate(String orderLastDate) {
		this.orderLastDate = orderLastDate;
	}

	public Integer getOrderLastQty() {
		return orderLastQty;
	}

	public void setOrderLastQty(Integer orderLastQty) {
		this.orderLastQty = orderLastQty;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getOrderGenerationNumber() {
		return orderGenerationNumber;
	}

	public void setOrderGenerationNumber(String orderGenerationNumber) {
		this.orderGenerationNumber = orderGenerationNumber;
	}
	public int getBcAddressBookId() {
		return bcAddressBookId;
	}

	public void setBcAddressBookId(int bcAddressBookId) {
		this.bcAddressBookId = bcAddressBookId;
	}

	public int getFirstOrder() {
		return firstOrder;
	}

	public void setFirstOrder(int firstOrder) {
		this.firstOrder = firstOrder;
	}
	public String getDiscountDate() {
		return discountDate;
	}

	public void setDiscountDate(String discountDate) {
		this.discountDate = discountDate;
	}

	public String getPrimaryOrderNumber() {
		return primaryOrderNumber;
	}

	public void setPrimaryOrderNumber(String primaryOrderNumber) {
		this.primaryOrderNumber = primaryOrderNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRushFlag() {
		return rushFlag;
	}
	public void setRushFlag(String rushFlag) {
		this.rushFlag = rushFlag;
	}
	public String getIncludeApprovalType() {
		return includeApprovalType;
	}
	public void setIncludeApprovalType(String includeApprovalType) {
		this.includeApprovalType = includeApprovalType;
	}
	public String getApprovalType() {
		return approvalType;
	}
	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}
	public int getBackOrderQty() {
		return backOrderQty;
	}
	public void setBackOrderQty(int backOrderQty) {
		this.backOrderQty = backOrderQty;
	}
	public Cimm2BCentralDocumentDetail getDocumentDetail() {
		return documentDetail;
	}
	public void setDocumentDetail(Cimm2BCentralDocumentDetail documentDetail) {
		this.documentDetail = documentDetail;
	}
	
	public int getDesignId() {
		return designId;
	}
	public void setDesignId(int designId) {
		this.designId = designId;
	}
	public boolean isDesignFees() {
		return designFees;
	}
	public void setDesignFees(boolean designFees) {
		this.designFees = designFees;
	}
	public HashMap<Integer, CustomizationCharges> getDesignFeesModel() {
		return designFeesModel;
	}
	public void setDesignFeesModel(HashMap<Integer, CustomizationCharges> designFeesModel) {
		this.designFeesModel = designFeesModel;
	}
	public int getUniqueWebReferenceNumber() {
		return uniqueWebReferenceNumber;
	}
	public void setUniqueWebReferenceNumber(int uniqueWebReferenceNumber) {
		this.uniqueWebReferenceNumber = uniqueWebReferenceNumber;
	}
	public double getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public double getFreightAmount() {
		return FreightAmount;
	}
	public void setFreightAmount(double freightAmount) {
		this.FreightAmount = freightAmount;
	}
	public UsersModel getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(UsersModel shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getShipViaServiceName() {
		return shipViaServiceName;
	}
	public void setShipViaServiceName(String shipViaServiceName) {
		this.shipViaServiceName = shipViaServiceName;
	}	
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public String getUserValue() {
		return userValue;
	}
	public void setUserValue(String userValue) {
		this.userValue = userValue;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public ArrayList<SalesModel> getMaxRecallList() {
		return maxRecallList;
	}
	public void setMaxRecallList(ArrayList<SalesModel> maxRecallList) {
		this.maxRecallList = maxRecallList;
	}
	public String getMaxData() {
		return maxData;
	}
	public void setMaxData(String maxData) {
		this.maxData = maxData;
	}
	public String getMaxName() {
		return maxName;
	}
	public void setMaxName(String maxName) {
		this.maxName = maxName;
	}
	public String getMaxTitle() {
		return maxTitle;
	}
	public void setMaxTitle(String maxTitle) {
		this.maxTitle = maxTitle;
	}
	public String getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}
	public String getMaxUrl() {
		return maxUrl;
	}
	public void setMaxUrl(String maxUrl) {
		this.maxUrl = maxUrl;
	}
	public String getShipViaServiceCode() {
		return shipViaServiceCode;
	}
	public void setShipViaServiceCode(String shipViaServiceCode) {
		this.shipViaServiceCode = shipViaServiceCode;
	}
	public double getTotalCartFrieght() {
		return totalCartFrieght;
	}
	public void setTotalCartFrieght(double totalCartFrieght) {
		this.totalCartFrieght = totalCartFrieght;
	}
	public CimmShippingCharges getTransportationCharges() {
		return transportationCharges;
	}
	public void setTransportationCharges(CimmShippingCharges transportationCharges) {
		this.transportationCharges = transportationCharges;
	}
	public CimmShippingCharges getServiceCharges() {
		return serviceCharges;
	}
	public void setServiceCharges(CimmShippingCharges serviceCharges) {
		this.serviceCharges = serviceCharges;
	}
	public CimmShippingCharges getTotalCharges() {
		return totalCharges;
	}
	public void setTotalCharges(CimmShippingCharges totalCharges) {
		this.totalCharges = totalCharges;
	}
	public ProductsModel getTotalCartDimensions() {
		return totalCartDimensions;
	}
	public void setTotalCartDimensions(ProductsModel totalCartDimensions) {
		this.totalCartDimensions = totalCartDimensions;
	}
	public String getSearchKeywordForOrderSuffix() {
		return searchKeywordForOrderSuffix;
	}
	public void setSearchKeywordForOrderSuffix(String searchKeywordForOrderSuffix) {
		this.searchKeywordForOrderSuffix = searchKeywordForOrderSuffix;
	}
	public int getQtyOpen() {
		return qtyOpen;
	}
	public void setQtyOpen(int qtyOpen) {
		this.qtyOpen = qtyOpen;
	}
	public String getDispositionDescription() {
		return dispositionDescription;
	}
	public void setDispositionDescription(String dispositionDescription) {
		this.dispositionDescription = dispositionDescription;
	}
	public double getOpenItemsNumber() {
		return openItemsNumber;
	}
	public void setOpenItemsNumber(double openItemsNumber) {
		this.openItemsNumber = openItemsNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public boolean isRowDetails() {
		return rowDetails;
	}
	public void setRowDetails(boolean rowDetails) {
		this.rowDetails = rowDetails;
	}
	public String getStartRowId() {
		return startRowId;
	}
	public void setStartRowId(String startRowId) {
		this.startRowId = startRowId;
	}
	public ArrayList<String> getRowIds() {
		return rowIds;
	}
	public void setRowIds(ArrayList<String> rowIds) {
		this.rowIds = rowIds;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getBillFirstName() {
		return billFirstName;
	}
	public void setBillFirstName(String billFirstName) {
		this.billFirstName = billFirstName;
	}
	public String getBillLastName() {
		return billLastName;
	}
	public void setBillLastName(String billLastName) {
		this.billLastName = billLastName;
	}
		
	private String guestFlag;		
	
	public String getGuestFlag() {		
		return guestFlag;		
	}		
	public void setGuestFlag(String guestFlag) {		
		this.guestFlag = guestFlag;		
	}
	public boolean isExcludePDFDocument() {
		return excludePDFDocument;
	}
	public void setExcludePDFDocument(boolean excludePDFDocument) {
		this.excludePDFDocument = excludePDFDocument;
	}
	public boolean isLocalDelivery() {
		return localDelivery;
	}
	public void setLocalDelivery(boolean localDelivery) {
		this.localDelivery = localDelivery;
	}
}
