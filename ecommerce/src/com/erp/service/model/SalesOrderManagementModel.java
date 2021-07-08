package com.erp.service.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.unilog.products.ProductsModel;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.SalesModel;
import com.unilog.users.AddressModel;

public class SalesOrderManagementModel {

	private HttpSession session;
	private int orderId;
	private String billEntityId;
	private String shipEntityId;
	private String note;
	private String userToken;
	private  AddressModel billAddress;
	private  AddressModel shipAddress;
	private String taxExempt;
	private String userName;
	private CreditCardModel creditCardValue;
	private String purchaseOrderNumber;
	private String selectedBranch;
	private  LinkedHashMap<String,String> lineItemCommentList;
	private  LinkedHashMap<String,Integer> uOMQTY;
	private String comments;
	private String shipVia;
	private String shipViaDescription;
	private String shippingInstruction;
	private String orderNotes;
	private String orderedBy;
	private String orderStatus;
	private String orderStatusCode;
	private String customerReleaseNumber;
	private String jobId;
	private String reqDate;
	private String quoteNumber;
	private SalesModel quoteResponse;
	private String country;
	private String CCType;
	private String CCAuth;
	private double CCAmount;
	private String CCToken;
	private String paymentTerms;
	private String erpOrderType;
	private LinkedHashMap<String, Object> salesOrderInput;
	private LinkedHashMap<Integer,ProductsModel> allItemData;
	private ArrayList<ProductsModel> orderItems;
	private String frieghtCharges;
	private String orderERPId;
	private String gasPoNumber; 
	private String shippingAccountNumber;
	private String shippingAndHandlingFee;
	private AddressModel firstName;
	private double orderTax;
	private AddressModel lastName;
	private String shipViaMethod;
	private String anonymous;
	private String userSelectedLocation;
	private String erpUserContactId;
	private String externalCartId;
	private String jurisdictionCode;
	private String freightCode;
	private String orderType;
	private String orderDisposition;
	private String additionalName;
	private String additionalPickupPerson;
	private String additionalComments;
	private String additionalCommentsShipToStore;
	private double subTotal;
	private ArrayList<Cimm2BCentralLineItem> lineItems;
	private String partNumberForEventpayment;
	private double discountAmount;
	private String shipViaServiceCode;
	private String includeApprovalType;
	private String approvalType;
	private String orderSource;
	private String backorderType;
	private String acceptBackorders;
	private String user1;
	private String user4;
	private String notesIndicator;
	private String dataValue;
	private String transactionType;
	private String couponInfo;
	private boolean isLocalDelivery;
	
	public String getCouponInfo() {
		return couponInfo;
	}
	public void setCouponInfo(String couponInfo) {
		this.couponInfo = couponInfo;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getUser1() {
		return user1;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public String getUser4() {
		return user4;
	}
	public void setUser4(String user4) {
		this.user4 = user4;
	}
	public String getNotesIndicator() {
		return notesIndicator;
	}
	public void setNotesIndicator(String notesIndicator) {
		this.notesIndicator = notesIndicator;
	}

	public String getAcceptBackorders() {
		return acceptBackorders;
	}
	public void setAcceptBackorders(String acceptBackorders) {
		this.acceptBackorders = acceptBackorders;
	}
	public String getBackorderType() {
		return backorderType;
	}
	public void setBackorderType(String backorderType) {
		this.backorderType = backorderType;
	}
	public double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getExternalCartId() {
		return externalCartId;
	}
	public void setExternalCartId(String externalCartId) {
		this.externalCartId = externalCartId;
	}
	public String getJurisdictionCode() {
		return jurisdictionCode;
	}
	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}
	
	
	public String getShipViaDescription() {
		return shipViaDescription;
	}
	public void setShipViaDescription(String shipViaDescription) {
		this.shipViaDescription = shipViaDescription;
	}
	public AddressModel getFirstName() {
		return firstName;
	}
	public void setFirstName(AddressModel firstName) {
		this.firstName = firstName;
	}
	public AddressModel getLastName() {
		return lastName;
	}
	public void setLastName(AddressModel lastName) {
		this.lastName = lastName;
	}
	public String getShipViaMethod() {
		return shipViaMethod;
	}
	public void setShipViaMethod(String shipViaMethod) {
		this.shipViaMethod = shipViaMethod;
	}
	public String getAnonymous() {
		return anonymous;
	}
	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}
	public LinkedHashMap<String, Object> getSalesOrderInput() {
		return salesOrderInput;
	}
	public void setSalesOrderInput(LinkedHashMap<String, Object> salesOrderInput) {
		this.salesOrderInput = salesOrderInput;
	}
	public String getErpOrderType() {
		return erpOrderType;
	}
	public void setErpOrderType(String erpOrderType) {
		this.erpOrderType = erpOrderType;
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public AddressModel getBillAddress() {
		return billAddress;
	}
	public void setBillAddress(AddressModel billAddress) {
		this.billAddress = billAddress;
	}
	public AddressModel getShipAddress() {
		return shipAddress;
	}
	public void setShipAddress(AddressModel shipAddress) {
		this.shipAddress = shipAddress;
	}
	public String getTaxExempt() {
		return taxExempt;
	}
	public void setTaxExempt(String taxExempt) {
		this.taxExempt = taxExempt;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public CreditCardModel getCreditCardValue() {
		return creditCardValue;
	}
	public void setCreditCardValue(CreditCardModel creditCardValue) {
		this.creditCardValue = creditCardValue;
	}
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}
	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}
	public String getSelectedBranch() {
		return selectedBranch;
	}
	public void setSelectedBranch(String selectedBranch) {
		this.selectedBranch = selectedBranch;
	}
	public LinkedHashMap<String, String> getLineItemCommentList() {
		return lineItemCommentList;
	}
	public void setLineItemCommentList(
			LinkedHashMap<String, String> lineItemCommentList) {
		this.lineItemCommentList = lineItemCommentList;
	}
	public LinkedHashMap<String, Integer> getuOMQTY() {
		return uOMQTY;
	}
	public void setuOMQTY(LinkedHashMap<String, Integer> uOMQTY) {
		this.uOMQTY = uOMQTY;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public void setShipVia(String shipVia) {
		this.shipVia = shipVia;
	}
	public String getShipVia() {
		return shipVia;
	}
	
	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}
	public String getOrderNotes() {
		return orderNotes;
	}
	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}
	public String getOrderedBy() {
		return orderedBy;
	}
	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}
	public String getReqDate() {
		return reqDate;
	}
	public void setShippingInstruction(String shippingInstruction) {
		this.shippingInstruction = shippingInstruction;
	}
	public String getShippingInstruction() {
		return shippingInstruction;
	}
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}
	public String getQuoteNumber() {
		return quoteNumber;
	}
	public void setQuoteResponse(SalesModel quoteResponse) {
		this.quoteResponse = quoteResponse;
	}
	public SalesModel getQuoteResponse() {
		return quoteResponse;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return country;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderStatusCode() {
		return orderStatusCode;
	}
	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}
	public String getCustomerReleaseNumber() {
		return customerReleaseNumber;
	}
	public void setCustomerReleaseNumber(String customerReleaseNumber) {
		this.customerReleaseNumber = customerReleaseNumber;
	}
	public String getCCType() {
		return CCType;
	}
	public void setCCType(String cCType) {
		CCType = cCType;
	}
	public String getCCAuth() {
		return CCAuth;
	}
	public void setCCAuth(String cCAuth) {
		CCAuth = cCAuth;
	}
	public double getCCAmount() {
		return CCAmount;
	}
	public void setCCAmount(double cCAmount) {
		CCAmount = cCAmount;
	}
	public String getCCToken() {
		return CCToken;
	}
	public void setCCToken(String cCToken) {
		CCToken = cCToken;
	}
	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	public String getPaymentTerms() {
		return paymentTerms;
	}
	public void setOrderItems(ArrayList<ProductsModel> orderItems) {
		this.orderItems = orderItems;
	}
	public ArrayList<ProductsModel> getOrderItems() {
		return orderItems;
	}
	public String getFrieghtCharges() {
		return frieghtCharges;
	}
	public void setFrieghtCharges(String frieghtCharges) {
		this.frieghtCharges = frieghtCharges;
	}
	public String getBillEntityId() {
		return billEntityId;
	}
	public void setBillEntityId(String billEntityId) {
		this.billEntityId = billEntityId;
	}
	public String getShipEntityId() {
		return shipEntityId;
	}
	public void setShipEntityId(String shipEntityId) {
		this.shipEntityId = shipEntityId;
	}
	public String getOrderERPId() {
		return orderERPId;
	}
	public void setOrderERPId(String orderERPId) {
		this.orderERPId = orderERPId;
	}
	public String getGasPoNumber() {
		return gasPoNumber;
	}
	public void setGasPoNumber(String gasPoNumber) {
		this.gasPoNumber = gasPoNumber;
	}
	public String getShippingAccountNumber() {
		return shippingAccountNumber;
	}
	public void setShippingAccountNumber(String shippingAccountNumber) {
		this.shippingAccountNumber = shippingAccountNumber;
	}
	public LinkedHashMap<Integer,ProductsModel> getAllItemData() {
		return allItemData;
	}
	public void setAllItemData(LinkedHashMap<Integer,ProductsModel> allItemData) {
		this.allItemData = allItemData;
	}
	public double getOrderTax() {
		return orderTax;
	}
	public void setOrderTax(double orderTax) {
		this.orderTax = orderTax;
	}
	public String getUserSelectedLocation() {
		return userSelectedLocation;
	}
	public void setUserSelectedLocation(String userSelectedLocation) {
		this.userSelectedLocation = userSelectedLocation;
	}
	public String getErpUserContactId() {
		return erpUserContactId;
	}
	public void setErpUserContactId(String erpUserContactId) {
		this.erpUserContactId = erpUserContactId;
	}
	public String getShippingAndHandlingFee() {
		return shippingAndHandlingFee;
	}
	public void setShippingAndHandlingFee(String shippingAndHandlingFee) {
		this.shippingAndHandlingFee = shippingAndHandlingFee;
	}
	public String getFreightCode() {
		return freightCode;
	}
	public void setFreightCode(String freightCode) {
		this.freightCode = freightCode;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderDisposition() {
		return orderDisposition;
	}
	public void setOrderDisposition(String orderDisposition) {
		this.orderDisposition = orderDisposition;
	}public String getAdditionalName() {
		return additionalName;
	}
	public void setAdditionalName(String additionalName) {
		this.additionalName = additionalName;
	}
	public String getAdditionalPickupPerson() {
		return additionalPickupPerson;
	}
	public void setAdditionalPickupPerson(String additionalPickupPerson) {
		this.additionalPickupPerson = additionalPickupPerson;
	}
	public String getAdditionalComments() {
		return additionalComments;
	}
	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}
	public String getAdditionalCommentsShipToStore() {
		return additionalCommentsShipToStore;
	}
	public void setAdditionalCommentsShipToStore(String additionalCommentsShipToStore) {
		this.additionalCommentsShipToStore = additionalCommentsShipToStore;
	}
	public ArrayList<Cimm2BCentralLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(ArrayList<Cimm2BCentralLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	public String getPartNumberForEventpayment() {
		return partNumberForEventpayment;
	}
	public void setPartNumberForEventpayment(String partNumberForEventpayment) {
		this.partNumberForEventpayment = partNumberForEventpayment;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public String getShipViaServiceCode() {
		return shipViaServiceCode;
	}
	public void setShipViaServiceCode(String shipViaServiceCode) {
		this.shipViaServiceCode = shipViaServiceCode;
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
	public String getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	
	private String guestFlag;				
	
	public String getGuestFlag() {				
		return guestFlag;				
	}				
	public void setGuestFlag(String guestFlag) {				
		this.guestFlag = guestFlag;				
	}
	public boolean isLocalDelivery() {
		return isLocalDelivery;
	}
	public void setLocalDelivery(boolean isLocalDelivery) {
		this.isLocalDelivery = isLocalDelivery;
	}
}
