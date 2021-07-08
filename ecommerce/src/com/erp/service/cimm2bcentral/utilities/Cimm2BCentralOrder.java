package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralOrder extends Cimm2BCentralResponseEntity{

	private String customerERPId;
	private String orderedBy;
	private String customerPoNumber;
	private String releaseNumber;
	private String jobId;
	private String requiredDate;
	private Cimm2BCentralBillAndShipToContact billToContact;
	private Cimm2BCentralBillAndShipToContact shipToContact;
	private Cimm2BCentralAddress billingAddress;
	private Cimm2BCentralAddress shippingAddress;
	private String branchCode;
	private String branchName;
	private ArrayList<Cimm2BCentralLineItem> lineItems;
	private ArrayList<Cimm2BCentralShipVia> shipVia;
	private Double freight;
	private Double taxAmount;
	private Double salesTax;
	private Double orderTotal;
	private String orderComment;
	private ArrayList<Cimm2BCentralCoupon> orderLevelCouponList;
	private Cimm2BCentralCustomerCard customerCard;
	private String orderStatus;
	private String shippingInstruction;
	private String orderNumber;
	private String warehouseLocation;
	private Boolean preAuthorize;
	private String salesPersonCode;
	private Integer backOrderSequence;
	private String orderERPId;
	private String contactId;
	private Integer companyId;
	private Double discountAmount;
	private Double otherAmount;
	private Double deliveryCharge;
	private Double orderSubTotal;
	private String gasPoNumber;
	private String customerName;
	private boolean dfmOrder=false;
	private boolean maximumOrderThresholdMet=false;
	private Integer customerId;
	private Cimm2BCentralShipVia shipVias;
	private int uniqueWebReferenceNumber;
	private String freightCode;
	private String anonymous;
	private String shippingBranch;
	private Double freightOut;
	private Boolean createShippingAndHandlingAsNewLineItem;
	private String orderCommentDisplayArea;
	private String salesLocationId;
	private String orderDate;
	private String shipDate;
	private String orderType;
	private double otherCharges;
	private String currentStatus;
	private String userERPId;
	private String orderId;
	private String cartId;
	private String jurisdictionCode;
	private boolean taxable;
	private double backOrderTotal;
	private String orderDisposition;
	public String orderSource;
	private int orderSuffix;
	private String orderNotes;
	private String includeApprovalType;
	private String approvalType; 
	private String orderGenerationNumber;
	private Double roeDiscountAmount;
	private Double discountPercentage;
	private String discountDate;
	private double discount;
	private boolean importAsQuote;
	private boolean includeJobName;
	private String orderStatusCode;
	private Cimm2BCentralDocumentDetail documentDetail;
	private String pricingBranchCode;
	private String shipBranchId;
	private Cimm2BCentralUser userDetails;
	private String acceptBackorders;
	private String referenceId;
	private String agencyName;
	private String user1;
	private String user4;
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
	private String user18;
    private String paymentTermCode;
    private String invoiceNumber;
    private String paidDate;
    private String writtenBy;
    private Double totalInvoiceAmount;

	public String getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}
	public String getWrittenBy() {
		return writtenBy;
	}
	public void setWrittenBy(String writtenBy) {
		this.writtenBy = writtenBy;
	}
	public String getPaymentTermCode() {
		return paymentTermCode;
	}
	public void setPaymentTermCode(String paymentTermCode) {
		this.paymentTermCode = paymentTermCode;
	}
	public String getUser18() {
		return user18;
	}
	public void setUser18(String user18) {
		this.user18 = user18;
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
	private String routeData;
	private boolean anonymousUser;
	private Double InvoiceAmount;
	private String orderTime;

	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public Double getInvoiceAmount() {
		return InvoiceAmount;
	}
	public void setInvoiceAmount(Double invoiceAmount) {
		InvoiceAmount = invoiceAmount;
	}
	public boolean isAnonymousUser() {
		return anonymousUser;
	}
	public void setAnonymousUser(boolean anonymousUser) {
		this.anonymousUser = anonymousUser;
	}
	public String getRouteData() {
		return routeData;
	}
	public void setRouteData(String routeData) {
		this.routeData = routeData;
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
	public String getAcceptBackorders() {
		return acceptBackorders;
	}
	public void setAcceptBackorders(String acceptBackorders) {
		this.acceptBackorders = acceptBackorders;
	}
	public Cimm2BCentralUser getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(Cimm2BCentralUser userDetails) {
		this.userDetails = userDetails;
	}
	public Cimm2BCentralDocumentDetail getDocumentDetail() {
		return documentDetail;
	}
	public void setDocumentDetail(Cimm2BCentralDocumentDetail documentDetail) {
		this.documentDetail = documentDetail;
	}
	public String getPricingBranchCode() {
		return pricingBranchCode;
	}
	public void setPricingBranchCode(String pricingBranchCode) {
		this.pricingBranchCode = pricingBranchCode;
	}
	public String getShipBranchId() {
		return shipBranchId;
	}
	public void setShipBranchId(String shipBranchId) {
		this.shipBranchId = shipBranchId;
	}
	public double getDiscount() {
		return discount;
	}
	public String getOrderStatusCode() {
		return orderStatusCode;
	}
	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	public String getJurisdictionCode() {
		return jurisdictionCode;
	}
	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}
	public boolean isTaxable() {
		return taxable;
	}
	public void setTaxable(boolean taxable) {
		this.taxable = taxable;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Cimm2BCentralShipVia getShipVias() {
		return shipVias;
	}
	public void setShipVias(Cimm2BCentralShipVia shipVias) {
		this.shipVias = shipVias;
	}
	public int getUniqueWebReferenceNumber() {
		return uniqueWebReferenceNumber;
	}
	public void setUniqueWebReferenceNumber(int uniqueWebReferenceNumber) {
		this.uniqueWebReferenceNumber = uniqueWebReferenceNumber;
	}
	public String getFreightCode() {
		return freightCode;
	}
	public void setFreightCode(String freightCode) {
		this.freightCode = freightCode;
	}
	public String getAnonymous() {
		return anonymous;
	}
	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}
	public String getShippingBranch() {
		return shippingBranch;
	}
	public void setShippingBranch(String shippingBranch) {
		this.shippingBranch = shippingBranch;
	}
		public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	
	public String getOrderedBy() {
		return orderedBy;
	}
	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}
	public String getCustomerPoNumber() {
		return customerPoNumber;
	}
	public void setCustomerPoNumber(String customerPoNumber) {
		this.customerPoNumber = customerPoNumber;
	}
	public String getReleaseNumber() {
		return releaseNumber;
	}
	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}
	public String getRequiredDate() {
		return requiredDate;
	}
	public void setRequiredDate(String requiredDate) {
		this.requiredDate = requiredDate;
	}
	public Cimm2BCentralAddress getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(Cimm2BCentralAddress billingAddress) {
		this.billingAddress = billingAddress;
	}
	public Cimm2BCentralAddress getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(Cimm2BCentralAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public ArrayList<Cimm2BCentralLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(ArrayList<Cimm2BCentralLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	public ArrayList<Cimm2BCentralShipVia> getShipVia() {
		return shipVia;
	}
	public void setShipVia(ArrayList<Cimm2BCentralShipVia> shipVia) {
		this.shipVia = shipVia;
	}
	public Double getFreight() {
		return freight;
	}
	public void setFreight(Double freight) {
		this.freight = freight;
	}
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public Double getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
	}
	public String getOrderComment() {
		return orderComment;
	}
	public void setOrderComment(String orderComment) {
		this.orderComment = orderComment;
	}
	public Cimm2BCentralCustomerCard getCustomerCard() {
		return customerCard;
	}
	public void setCustomerCard(Cimm2BCentralCustomerCard customerCard) {
		this.customerCard = customerCard;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getShippingInstruction() {
		return shippingInstruction;
	}
	public void setShippingInstruction(String shippingInstruction) {
		this.shippingInstruction = shippingInstruction;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getWarehouseLocation() {
		return warehouseLocation;
	}
	public void setWarehouseLocation(String warehouseLocation) {
		this.warehouseLocation = warehouseLocation;
	}
	public Boolean isPreAuthorize() {
		return preAuthorize;
	}
	public void setPreAuthorize(Boolean preAuthorize) {
		this.preAuthorize = preAuthorize;
	}
	public String getSalesPersonCode() {
		return salesPersonCode;
	}
	public void setSalesPersonCode(String salesPersonCode) {
		this.salesPersonCode = salesPersonCode;
	}
	public Integer getBackOrderSequence() {
		return backOrderSequence;
	}
	public void setBackOrderSequence(Integer backOrderSequence) {
		this.backOrderSequence = backOrderSequence;
	}
	public String getOrderERPId() {
		return orderERPId;
	}
	public void setOrderERPId(String orderERPId) {
		this.orderERPId = orderERPId;
	}
	public ArrayList<Cimm2BCentralCoupon> getOrderLevelCouponList() {
		return orderLevelCouponList;
	}
	public void setOrderLevelCouponList(ArrayList<Cimm2BCentralCoupon> orderLevelCouponList) {
		this.orderLevelCouponList = orderLevelCouponList;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public Double getOtherAmount() {
		return otherAmount;
	}
	public void setOtherAmount(Double otherAmount) {
		this.otherAmount = otherAmount;
	}
	public Double getDeliveryCharge() {
		return deliveryCharge;
	}
	public void setDeliveryCharge(Double deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}
	public Double getOrderSubTotal() {
		return orderSubTotal;
	}
	public void setOrderSubTotal(Double orderSubTotal) {
		this.orderSubTotal = orderSubTotal;
	}
	public String getGasPoNumber() {
		return gasPoNumber;
	}
	public void setGasPoNumber(String gasPoNumber) {
		this.gasPoNumber = gasPoNumber;
	}
	public Boolean getPreAuthorize() {
		return preAuthorize;
	}
	public Double getFreightOut() {
		return freightOut;
	}
	public void setFreightOut(Double freightOut) {
		this.freightOut = freightOut;
	}
	public Boolean getCreateShippingAndHandlingAsNewLineItem() {
		return createShippingAndHandlingAsNewLineItem;
	}
	public void setCreateShippingAndHandlingAsNewLineItem(
			Boolean createShippingAndHandlingAsNewLineItem) {
		this.createShippingAndHandlingAsNewLineItem = createShippingAndHandlingAsNewLineItem;
	}
	public Cimm2BCentralBillAndShipToContact getBillToContact() {
		return billToContact;
	}
	public void setBillToContact(Cimm2BCentralBillAndShipToContact billToContact) {
		this.billToContact = billToContact;
	}
	public Cimm2BCentralBillAndShipToContact getShipToContact() {
		return shipToContact;
	}
	public void setShipToContact(Cimm2BCentralBillAndShipToContact shipToContact) {
		this.shipToContact = shipToContact;
	}
	public String getOrderCommentDisplayArea() {
		return orderCommentDisplayArea;
	}
	public void setOrderCommentDisplayArea(String orderCommentDisplayArea) {
		this.orderCommentDisplayArea = orderCommentDisplayArea;
	}
	public String getSalesLocationId() {
		return salesLocationId;
	}
	public void setSalesLocationId(String salesLocationId) {
		this.salesLocationId = salesLocationId;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public boolean isDfmOrder() {
		return dfmOrder;
	}
	public void setDfmOrder(boolean dfmOrder) {
		this.dfmOrder = dfmOrder;
	}
	public boolean isMaximumOrderThresholdMet() {
		return maximumOrderThresholdMet;
	}
	public void setMaximumOrderThresholdMet(boolean maximumOrderThresholdMet) {
		this.maximumOrderThresholdMet = maximumOrderThresholdMet;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public double getOtherCharges() {
		return otherCharges;
	}
	public void setOtherCharges(double otherCharges) {
		this.otherCharges = otherCharges;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getUserERPId() {
		return userERPId;
	}
	public void setUserERPId(String userERPId) {
		this.userERPId = userERPId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCartId() {
		return cartId;
	}
	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
	public double getBackOrderTotal() {
		return backOrderTotal;
	}
	public void setBackOrderTotal(double backOrderTotal) {
		this.backOrderTotal = backOrderTotal;
	}
	public Double getSalesTax() {
		return salesTax;
	}
	public void setSalesTax(Double salesTax) {
		this.salesTax = salesTax;
	}
	public String getOrderDisposition() {
		return orderDisposition;
	}
	public void setOrderDisposition(String orderDisposition) {
		this.orderDisposition = orderDisposition;
	}
	public String getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}
	public int getOrderSuffix() {
		return orderSuffix;
	}
	public void setOrderSuffix(int orderSuffix) {
		this.orderSuffix = orderSuffix;
	}
	public String getOrderNotes() {
		return orderNotes;
	}
	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
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
	public String getOrderGenerationNumber() {
		return orderGenerationNumber;
	}
	public void setOrderGenerationNumber(String orderGenerationNumber) {
		this.orderGenerationNumber = orderGenerationNumber;
	}
	public Double getRoeDiscountAmount() {
		return roeDiscountAmount;
	}
	public void setRoeDiscountAmount(Double roeDiscountAmount) {
		this.roeDiscountAmount = roeDiscountAmount;
	}
	public Double getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public String getDiscountDate() {
		return discountDate;
	}
	public void setDiscountDate(String discountDate) {
		this.discountDate = discountDate;
	}
	public boolean isImportAsQuote() {
		return importAsQuote;
	}
	public void setImportAsQuote(boolean importAsQuote) {
		this.importAsQuote = importAsQuote;
	}
	public boolean isIncludeJobName() {
		return includeJobName;
	}
	public void setIncludeJobName(boolean includeJobName) {
		this.includeJobName = includeJobName;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
	public Double getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}
	public void setTotalInvoiceAmount(Double totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}
}
