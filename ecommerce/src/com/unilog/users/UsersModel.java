package com.unilog.users;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceSummary;
import com.google.gson.annotations.Expose;
import com.unilog.products.ProductsModel;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.SalesModel;

public class UsersModel {
	@Expose private String loginID;
	@Expose private String loginPassword;
	@Expose private String sessionId;
	@Expose private String userToken;
	@Expose private String isCreditCard;
	@Expose private ArrayList<ProductsModel> branchAvail = new ArrayList<ProductsModel>();
	@Expose private ArrayList<String> contactIdList;
	@Expose private ArrayList<String> shipIdList;
	@Expose private ArrayList<String> contactDescriptionList;
	@Expose private ArrayList<AddressModel> contactShortList;
	@Expose private ArrayList<String> descriptionList;
	@Expose private ArrayList<String> erpPartNumberList;
	@Expose private ArrayList<CreditCardModel> creditCardDetailList = new ArrayList<CreditCardModel>();
	@Expose private String streetAddress;
	@Expose private String alwaysApprover;
	@Expose private String approverComments;
	@Expose private Integer approveCartGroupId;
	@Expose private String approvecartGroupName;
	@Expose private String approverSequence; 
	
	@Expose private String approvalStatus; 
	@Expose private String approveLimit;
	@Expose private Integer approverId;
	
	
	
	
	
	public Integer getApproverId() {
		return approverId;
	}
	public void setApproverId(Integer approverId) {
		this.approverId = approverId;
	}
	public String getApproveLimit() {
		return approveLimit;
	}
	public void setApproveLimit(String approveLimit) {
		this.approveLimit = approveLimit;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getAlwaysApprover() {
		return alwaysApprover;
	}
	public String getApproverComments() {
		return approverComments;
	}
	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}
	public String getApproverSequence() {
		return approverSequence;
	}
	public void setApproverSequence(String approverSequence) {
		this.approverSequence = approverSequence;
	}
	public Integer getApproveCartGroupId() {
		return approveCartGroupId;
	}
	public void setApproveCartGroupId(Integer approveCartGroupId) {
		this.approveCartGroupId = approveCartGroupId;
	}
	public String getApprovecartGroupName() {
		return approvecartGroupName;
	}
	public void setApprovecartGroupName(String approvecartGroupName) {
		this.approvecartGroupName = approvecartGroupName;
	}
	public void setAlwaysApprover(String alwaysApprover) {
		this.alwaysApprover = alwaysApprover;
	}

	@Expose private String postalCode;
	@Expose private ArrayList<ShipVia> shipViaList = new ArrayList<ShipVia>();
	@Expose private boolean isValidBuyer = false;
	@Expose private double arCreditAvail;
	@Expose private double arCreditLimit;
	@Expose private String arCreditAvailString;
	@Expose private String arCreditLimitString;
	@Expose private double aRDeposits;
	@Expose private double aROrders;
	@Expose private double aRTotal;
	@Expose private double cashDiscountAmount;
	@Expose private double cashDiscountPercentage;
	@Expose private double creditLimit;
	@Expose private double current;
	@Expose private double federalExciseTax;
	@Expose private double freight;
	@Expose private String freightDesc;
	@Expose private double freightIn;
	@Expose private String freightInDesc;
	@Expose private double future;
	@Expose private double handling;
	@Expose private double lastPaymentAmount;
	@Expose private double lastSaleAmount;
	@Expose private double mTDSales;
	@Expose private double ninety;
	@Expose private double oneTwenty;
	@Expose private double paymentAmount;
	@Expose private double paymentDays;
	@Expose private double rOEDiscount;
	@Expose private double sixMonthAverage;
	@Expose private double sixMonthHigh;
	@Expose private double sixty;
	@Expose private double yTDSales;
	@Expose private String billEntityId;
	@Expose private int branchTotal;
	@Expose private int buyingCompanyId;
	@Expose private String contactId;
	@Expose private String entityId;
	@Expose private int pcardId;
	@Expose private int resultCount;
	@Expose private int row;
	@Expose private String shipEntityId;
	@Expose private int userId;
	@Expose private LinkedHashMap<String, ProductsModel> branchTeritory = null;
	@Expose private String accountManager;
	@Expose private String closestBranch;
	@Expose private String accountName;
	@Expose private String address1;
	@Expose private String address2;
	@Expose private String addressType;
	@Expose private String apartment;
	@Expose private String approverAgentAssignStatus;
	@Expose private String approverEmail;
	@Expose private String arTerms;
	@Expose private String asOfDate;
	@Expose private String branchID;
	@Expose private String branchName;
	@Expose private String buildingNumber;
	@Expose private String CardHolder;
	@Expose private String cardResponse;
	@Expose private String changedPassword;
	@Expose private String city;
	@Expose private String connectionError;
	@Expose private String country;
	@Expose private String creditCardNumber;
	@Expose private String creditCardType;
	@Expose private String creditCardExpDate;
	@Expose private String creditCardAuthCode;
	@Expose private String creditCardRefrenceNumber;
	@Expose private String creditCardTransactionId;
	@Expose private String creditCardMerchantId;
	@Expose private String creditCardTransactionAmount;
	@Expose private String currency;
	@Expose private String currentPassword;
	@Expose private String currentActiveCustomerId;
	@Expose private String customerName;
	@Expose private String customerType;
	@Expose private String customerShortName;
	@Expose private String date;
	@Expose private String description;
	@Expose private String erpLoginId;
	@Expose private String erpPartNumber;
	@Expose private String elementPaymentAccountId;
	@Expose private String elementSetupId;
	@Expose private String elementSetupUrl;
	@Expose private String emailAddress;
	@Expose private String entityName;
	@Expose private String entityType;
	@Expose private String expDate;
	@Expose private String extrensicType;
	@Expose private String faxNo;
	@Expose private String firstName;
	@Expose private String homeBranch;
	@Expose private String homeBranchavailablity;
	@Expose private String isAuthPurchaseAgent;
	@Expose private String isBusyUser;
	@Expose private String isSuperUser;
	@Expose private String isAuthTechnician;
	@Expose private String isSpecialUser;
	@Expose private String lastName;
	@Expose private String lastPaymentDate;
	@Expose private String lastSaleDate;
	@Expose private String newsLetterSub;
	@Expose private String nickName;
	@Expose private String orderedBy;
	@Expose private String password;
	@Expose private String paymentDate;
	@Expose private String phoneNo;
	@Expose private String pobox;
	@Expose private String requestAuthorizationLevel;
	@Expose private String salesContact;
	@Expose private String SessionID;
	@Expose private String shipDate;
	@Expose private String shipToId;
	@Expose private String shipToName;
	@Expose private String ShipViaID;
	@Expose private String ShipViaMethod;
	@Expose private String state;
	@Expose private String statusDescription;
	@Expose private String streetName;
	@Expose private String uom;
	@Expose private String useEntityAddress;
	@Expose private String userName;
	@Expose private String vertical;
	@Expose private String webAddress;
	@Expose private String website;
	@Expose private String zipCode;
	@Expose private String zipCodeStringFormat;
	@Expose private String orderEntryOk;
	@Expose private int addressBookId;
	@Expose private int parentId;
	@Expose private boolean isAddressActive;
	@Expose private String addressTitle;
	@Expose private String addressRegisteredDate;
	@Expose private Date addressRegisteredDateDateFormat;
	@Expose private String reqDate;
	@Expose private String shippingInstruction;
	@Expose private String shipVia;
	@Expose private String orderNotes;
	@Expose private String countryFullName;
	@Expose private int taxCode;
	@Expose private String userStatus;
	@Expose private double thirty;
	@Expose private double subtotal;
	@Expose private double subtotalV2;
	@Expose private double tax;
	@Expose private double total;
	@Expose private String invoiceNumber;
	//--- For SX
	@Expose private String wareHouseCodeStr;
	@Expose private String wareHouseName;
	@Expose private String creditManagerStr;
	@Expose private String salesRepIn;
	@Expose private String salesRepOut;
	@Expose private int divisionNo;
	@Expose private int wareHouseCode;
	@Expose private String termsTypeDesc;
	@Expose private String termsType;
	@Expose private String updateMode;
	@Expose private String paymentTerm;
	//--- For SX
	@Expose private String countryCode;
	@Expose private String billToId;
	@Expose private String bannerScroll;
	@Expose private String bannerDelay;
	@Expose private String bannerNumberofItem;
	@Expose private String bannerDirection;
	@Expose private String imageName;
	@Expose private String imagePosition;
	@Expose private String imageType;
	@Expose private String imageURL;
	@Expose private String officePhone;
	@Expose private String makeAsDefault;
	@Expose private HttpSession session;
	@Expose private String userRole;
	@Expose private boolean customerAccountExist;
	@Expose private int editedShipId;
	@Expose private String bankTransactionId;
	@Expose private String bankApprovalCode;
	@Expose private String orderID;
	@Expose private String orderSuffix;
	//-- For Eclipse Entity Update
	@Expose private ArrayList<String> eCommerceIDList=null;
	@Expose private ArrayList<CreditCardModel> creditCardList=null;
	@Expose private String loginMessage;
	@Expose private ArrayList<String> customerTypeList;
	@Expose private String id;
	@Expose private String middleName;
	@Expose private String title;
	@Expose private String customerId;
	@Expose private boolean superuser;
	@Expose private boolean hideHistory;
	@Expose private boolean hideAccountInquiry;
	@Expose private String result;
	@Expose private int subsetId;
	@Expose private String customFieldName;
	@Expose private String catalogName;
	@Expose private Country countryModel = new Country();
	@Expose private String companyName;
	@Expose private String[] CustomerNumbers;
    @Expose private String sameAsBillingAddress;
    @Expose private int defaultBillToId;
	@Expose private int defaultShipToId;
    @Expose private String requestType;
    @Expose private String defaultShipVia;
    @Expose private AddressModel billAddress;
    @Expose private AddressModel shipAddress;
    @Expose private String contactClassification;
    @Expose private String latitude;
    @Expose private String longitude;
    @Expose private String pricePrecision;
    @Expose private double additionalCharges2;
	@Expose private double additionalCharges3;
	@Expose private double additionalCharges4;
	@Expose private double additionalCharges5;
	@Expose private double additionalCharges6;
	@Expose private double additionalCharges7;
	@Expose private double additionalCharges8;
	@Expose private double additionalCharges9;
	@Expose private double additionalCharges10;
	@Expose private double additionalCharges11;
	@Expose private double additionalCharges12;
	@Expose private double additionalCharges13;
	@Expose private double additionalCharges14;
	@Expose private double additionalCharges15;
	@Expose private double additionalCharges16;
	@Expose private double additionalCharges17;
	@Expose private double additionalCharges18;
	@Expose private double additionalCharges19;
	@Expose private double additionalCharges20;
	@Expose private double additionalCharges21;
	@Expose private double additionalCharges22;
	@Expose private double additionalCharges23;
	@Expose private double additionalCharges24;
	@Expose private double additionalCharges25;
	@Expose private double additionalCharges26;
	@Expose private double additionalCharges27;
	@Expose private double additionalCharges28;
	@Expose private double additionalCharges29;
	@Expose private double additionalCharges30;
	@Expose private double additionalCharges31;
	@Expose private double additionalCharges32;
	@Expose private double additionalCharges33;
	@Expose private double additionalCharges34;
	@Expose private double additionalCharges35;
	@Expose private double additionalCharges36;
	@Expose private double additionalCharges37;

	@Expose private String additionalChargesDescription2;
	@Expose private String additionalChargesDescription3;
	@Expose private String additionalChargesDescription4;
	@Expose private String additionalChargesDescription5;
	@Expose private String additionalChargesDescription6;
	@Expose private String additionalChargesDescription7;
	@Expose private String additionalChargesDescription8;
	@Expose private String additionalChargesDescription9;
	@Expose private String additionalChargesDescription10;
	@Expose private String additionalChargesDescription11;
	@Expose private String additionalChargesDescription12;
	@Expose private String additionalChargesDescription13;
	@Expose private String additionalChargesDescription14;
	@Expose private String additionalChargesDescription15;
	@Expose private String additionalChargesDescription16;
	@Expose private String additionalChargesDescription17;
	@Expose private String additionalChargesDescription18;
	@Expose private String additionalChargesDescription19;
	@Expose private String additionalChargesDescription20;
	@Expose private String additionalChargesDescription21;
	@Expose private String additionalChargesDescription22;
	@Expose private String additionalChargesDescription23;
	@Expose private String additionalChargesDescription24;
	@Expose private String additionalChargesDescription25;
	@Expose private String additionalChargesDescription26;
	@Expose private String additionalChargesDescription27;
	@Expose private String additionalChargesDescription28;
	@Expose private String additionalChargesDescription29;
	@Expose private String additionalChargesDescription30;
	@Expose private String additionalChargesDescription31;
	@Expose private String additionalChargesDescription32;
	@Expose private String additionalChargesDescription33;
	@Expose private String additionalChargesDescription34;
	@Expose private String additionalChargesDescription35;
	@Expose private String additionalChargesDescription36;
	@Expose private String additionalChargesDescription37;
    
	@Expose private String ExistingCustomer;
	@Expose private String shipViaDescription;
	@Expose private String salesRepContact;
	@Expose private String jobTitle;
	@Expose private String orderStatusCode;
	@Expose private String orderStatus;
	@Expose private ArrayList<UsersModel> orderStatusList = new ArrayList<UsersModel>();
	@Expose private String customerReleaseNumber;
	@Expose private String aRPassword;
	@Expose private String shipToChanged;
	@Expose private boolean Ispickup;
	@Expose private String poNumber;
	
	@Expose private String disableSubmitPO;
	@Expose private String disableSubmitPOCC;
	@Expose private String customFieldValue;
	@Expose private String extraParam;
	
	@Expose private String formtype;
	@Expose private CreditApplicationModel creditApplicationModel;
	@Expose private StringBuilder formHtmlDetails;
	
	@Expose private Cimm2BCentralARBalanceSummary arBalanceSummary;
	@Expose private Cimm2BCentralARBalanceDetails arBalanceDetails;
	@Expose private String includePeriod;
	@Expose private String payPalToken;
	@Expose private String payPalPayerId;
	@Expose private String creditCardToken;
	@Expose private boolean codFlag;
	@Expose private ArrayList<UsersModel> shipToList;	
	@Expose private boolean poRequired;
	@Expose private boolean dataUpTo30Days;
	@Expose private boolean dataUpTo60Days;
	@Expose private boolean dataUpTo90Days;
	@Expose private boolean dataUpTo120Days;
	
	@Expose private String sameAsCustomerAddress;
	@Expose private String validateZipcode;
	@Expose private String validateOrderNum;
	
	@Expose private List<SalesModel> accountInquiryItemList;
	@Expose private double previousBalance;
	@Expose private double currentBalance;
	@Expose private String creditApplicationRequest="N";
	@Expose private String department2AB;
	@Expose private String userERPId;
	@Expose private String emailId;
	@Expose private Double balance;
	@Expose private Double period1;
	@Expose private Double period2;
	@Expose private Double period3;
	@Expose private Double period4;
	@Expose private Double period5;
	@Expose private Double period6;
	@Expose private Double period7;
	@Expose private Double openOrderBalance;
	@Expose private Double futureBalance;
	@Expose private Double salesMTD;
	@Expose private Double salesYTD;
	@Expose private String termsAndCondition;
	@Expose private String endDate;
	@Expose private String erpOverrideFlag;
	@Expose private int changePasswordOnLogin;
	@Expose private List<Cimm2BCentralARBalanceSummary> arBalanceSummaryList;
	@Expose private Integer pastDue;
	@Expose private Double highBalance;
	@Expose private String isPunchoutUser;
	@Expose private boolean anonymousUser;
	@Expose private String faxNumber;
	@Expose private String role;
	@Expose private boolean isUpdateRole;
	@Expose private AddressModel shippingAddress;
	@Expose private String DefaultShipLocationId;
	@Expose private String woeUserName;
	@Expose private String woePassword;
	@Expose private String woeConfirmPassword;
	@Expose private String woeLogin;
	@Expose private String subsetFlag;
	@Expose private String orderType;
	@Expose private String enablePO;
	
	// For Electrozad
	@Expose private String currencyCode;
	@Expose private String shippingOrgType;
	private String howWebsiteContact;
	private String isTaxable;
	private String birthMonth;
	private String isRewardMember;
	@Expose private String shipToWarehouseCode;
	private String dateOfFirstSale;
	private String woeERPUserName;
	private String creditDate;
	private String newUserName;
	private String contactTitle;
	private String contactWebsite;
	
	
	
	public String getNewUserName() {
		return newUserName;
	}
	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}
	public String getWoeERPUserName() {
		return woeERPUserName;
	}
	public void setWoeERPUserName(String woeERPUserName) {
		this.woeERPUserName = woeERPUserName;
	}
	public String getIsRewardMember() {
		return isRewardMember;
	}
	public void setIsRewardMember(String isRewardMember) {
		this.isRewardMember = isRewardMember;
	}
	public String getBirthMonth() {
		return birthMonth;
	}
	public void setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
	}

	public String getSubsetFlag() {
		return subsetFlag;
	}
	public void setSubsetFlag(String subsetFlag) {
		this.subsetFlag = subsetFlag;
	}
	public String getHowWebsiteContact() {
		return howWebsiteContact;
	}
	public void setHowWebsiteContact(String howWebsiteContact) {
		this.howWebsiteContact = howWebsiteContact;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getIsPunchoutUser() {
		return isPunchoutUser;
	}
	public void setIsPunchoutUser(String isPunchoutUser) {
		this.isPunchoutUser = isPunchoutUser;
	}
	public int getChangePasswordOnLogin() {
		return changePasswordOnLogin;
	}
	public void setChangePasswordOnLogin(int changePasswordOnLogin) {
		this.changePasswordOnLogin = changePasswordOnLogin;
	}
	
	public Integer getPastDue() {
		return pastDue;
	}
	public void setPastDue(Integer pastDue) {
		this.pastDue = pastDue;
	}
	public Double getHighBalance() {
		return highBalance;
	}
	public void setHighBalance(Double highBalance) {
		this.highBalance = highBalance;
	}
	public String getErpOverrideFlag() {
		return erpOverrideFlag;
	}
	public void setErpOverrideFlag(String erpOverrideFlag) {
		this.erpOverrideFlag = erpOverrideFlag;
	}
	public String getDefaultShipLocationId() {
		return DefaultShipLocationId;
	}
	public void setDefaultShipLocationId(String defaultShipLocationId) {
		DefaultShipLocationId = defaultShipLocationId;
	}
	public Double getBalance() {
		return balance;
	}
	public String getEndDate() {
		return endDate;
	}
	public List<Cimm2BCentralARBalanceSummary> getArBalanceSummaryList() {
		return arBalanceSummaryList;
	}
	public void setArBalanceSummaryList(List<Cimm2BCentralARBalanceSummary> arBalanceSummaryList) {
		this.arBalanceSummaryList = arBalanceSummaryList;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getPeriod1() {
		return period1;
	}
	public void setPeriod1(Double period1) {
		this.period1 = period1;
	}
	public Double getPeriod2() {
		return period2;
	}
	public void setPeriod2(Double period2) {
		this.period2 = period2;
	}
	public Double getPeriod3() {
		return period3;
	}
	public void setPeriod3(Double period3) {
		this.period3 = period3;
	}
	public Double getPeriod4() {
		return period4;
	}
	public void setPeriod4(Double period4) {
		this.period4 = period4;
	}
	public Double getOpenOrderBalance() {
		return openOrderBalance;
	}
	public void setOpenOrderBalance(Double openOrderBalance) {
		this.openOrderBalance = openOrderBalance;
	}
	public Double getFutureBalance() {
		return futureBalance;
	}
	public void setFutureBalance(Double futureBalance) {
		this.futureBalance = futureBalance;
	}
	public Double getSalesMTD() {
		return salesMTD;
	}
	public void setSalesMTD(Double salesMTD) {
		this.salesMTD = salesMTD;
	}
	public Double getSalesYTD() {
		return salesYTD;
	}
	public void setSalesYTD(Double salesYTD) {
		this.salesYTD = salesYTD;
	}
	public String getTermsAndCondition() {
		return termsAndCondition;
	}
	public void setTermsAndCondition(String termsAndCondition) {
		this.termsAndCondition = termsAndCondition;
	}
	public String getCreditApplicationRequest() {
		return creditApplicationRequest;
	}
	public void setCreditApplicationRequest(String creditApplicationRequest) {
		this.creditApplicationRequest = creditApplicationRequest;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCurrentActiveCustomerId() {
		return currentActiveCustomerId;
	}
	public void setCurrentActiveCustomerId(String currentActiveCustomerId) {
		this.currentActiveCustomerId = currentActiveCustomerId;
	}
	public String getArCreditAvailString() {
		return arCreditAvailString;
	}
	public void setArCreditAvailString(String arCreditAvailString) {
		this.arCreditAvailString = arCreditAvailString;
	}
	public String getArCreditLimitString() {
		return arCreditLimitString;
	}
	public void setArCreditLimitString(String arCreditLimitString) {
		this.arCreditLimitString = arCreditLimitString;
	}
	public List<SalesModel> getAccountInquiryItemList() {
		return accountInquiryItemList;
	}
	public void setAccountInquiryItemList(List<SalesModel> accountInquiryItemList) {
		this.accountInquiryItemList = accountInquiryItemList;
	}
	public double getPreviousBalance() {
		return previousBalance;
	}
	public void setPreviousBalance(double previousBalance) {
		this.previousBalance = previousBalance;
	}
	public double getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getValidateZipcode() {
		return validateZipcode;
	}
	public void setValidateZipcode(String validateZipcode) {
		this.validateZipcode = validateZipcode;
	}
	public String getValidateOrderNum() {
		return validateOrderNum;
	}
	public void setValidateOrderNum(String validateOrderNum) {
		this.validateOrderNum = validateOrderNum;
	}
	public String getSameAsCustomerAddress() {
		return sameAsCustomerAddress;
	}
	public void setSameAsCustomerAddress(String sameAsCustomerAddress) {
		this.sameAsCustomerAddress = sameAsCustomerAddress;
	}
	public boolean isDataUpTo30Days() {
		return dataUpTo30Days;
	}
	public void setDataUpTo30Days(boolean dataUpTo30Days) {
		this.dataUpTo30Days = dataUpTo30Days;
	}
	public boolean isDataUpTo60Days() {
		return dataUpTo60Days;
	}
	public void setDataUpTo60Days(boolean dataUpTo60Days) {
		this.dataUpTo60Days = dataUpTo60Days;
	}
	public boolean isDataUpTo90Days() {
		return dataUpTo90Days;
	}
	public void setDataUpTo90Days(boolean dataUpTo90Days) {
		this.dataUpTo90Days = dataUpTo90Days;
	}
	public boolean isDataUpTo120Days() {
		return dataUpTo120Days;
	}
	public void setDataUpTo120Days(boolean dataUpTo120Days) {
		this.dataUpTo120Days = dataUpTo120Days;
	}
	public String getCreditCardToken() {
		return creditCardToken;
	}
	public void setCreditCardToken(String creditCardToken) {
		this.creditCardToken = creditCardToken;
	}
	public String getCreditCardMerchantId() {
		return creditCardMerchantId;
	}
	public void setCreditCardMerchantId(String creditCardMerchantId) {
		this.creditCardMerchantId = creditCardMerchantId;
	}
	public String getPayPalToken() {
		return payPalToken;
	}
	public void setPayPalToken(String payPalToken) {
		this.payPalToken = payPalToken;
	}
	public String getPayPalPayerId() {
		return payPalPayerId;
	}
	public void setPayPalPayerId(String payPalPayerId) {
		this.payPalPayerId = payPalPayerId;
	}
	public StringBuilder getFormHtmlDetails() {
		return formHtmlDetails;
	}
	public void setFormHtmlDetails(StringBuilder formHtmlDetails) {
		this.formHtmlDetails = formHtmlDetails;
	}
	public CreditApplicationModel getCreditApplicationModel() {
		return creditApplicationModel;
	}
	public void setCreditApplicationModel(
			CreditApplicationModel creditApplicationModel) {
		this.creditApplicationModel = creditApplicationModel;
	}
	public String getFormtype() {
		return formtype;
	}
	public void setFormtype(String formtype) {
		this.formtype = formtype;
	}
	public String getAccountManager() {
		return accountManager;
	}
	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}
	public String getClosestBranch() {
		return closestBranch;
	}
	public void setClosestBranch(String closestBranch) {
		this.closestBranch = closestBranch;
	}
	public String getExtraParam() {
		return extraParam;
	}
	public void setExtraParam(String extraParam) {
		this.extraParam = extraParam;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public boolean isIspickup() {
		return Ispickup;
	}
	public void setIspickup(boolean ispickup) {
		Ispickup = ispickup;
	}
	public String getShipToChanged() {
		return shipToChanged;
	}
	public void setShipToChanged(String shipToChanged) {
		this.shipToChanged = shipToChanged;
	}
	public String getCreditCardTransactionAmount() {
		return creditCardTransactionAmount;
	}
	public void setCreditCardTransactionAmount(String creditCardTransactionAmount) {
		this.creditCardTransactionAmount = creditCardTransactionAmount;
	}
	public String getCreditCardExpDate() {
		return creditCardExpDate;
	}
	public void setCreditCardExpDate(String creditCardExpDate) {
		this.creditCardExpDate = creditCardExpDate;
	}
	public String getCreditCardAuthCode() {
		return creditCardAuthCode;
	}
	public void setCreditCardAuthCode(String creditCardAuthCode) {
		this.creditCardAuthCode = creditCardAuthCode;
	}
	public String getCreditCardRefrenceNumber() {
		return creditCardRefrenceNumber;
	}
	public void setCreditCardRefrenceNumber(String creditCardRefrenceNumber) {
		this.creditCardRefrenceNumber = creditCardRefrenceNumber;
	}
	public String getCreditCardTransactionId() {
		return creditCardTransactionId;
	}
	public void setCreditCardTransactionId(String creditCardTransactionId) {
		this.creditCardTransactionId = creditCardTransactionId;
	}
	public String getaRPassword() {
		return aRPassword;
	}
	public void setaRPassword(String aRPassword) {
		this.aRPassword = aRPassword;
	}
	public String getSalesRepContact() {
		return salesRepContact;
	}
	public void setSalesRepContact(String salesRepContact) {
		this.salesRepContact = salesRepContact;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getShipViaDescription() {
		return shipViaDescription;
	}
	public void setShipViaDescription(String shipViaDescription) {
		this.shipViaDescription = shipViaDescription;
	}
	public String getExistingCustomer() {
		return ExistingCustomer;
	}
	public void setExistingCustomer(String existingCustomer) {
		ExistingCustomer = existingCustomer;
	}
	public double getAdditionalCharges2() {
		return additionalCharges2;
	}
	public void setAdditionalCharges2(double additionalCharges2) {
		this.additionalCharges2 = additionalCharges2;
	}
	public double getAdditionalCharges3() {
		return additionalCharges3;
	}
	public void setAdditionalCharges3(double additionalCharges3) {
		this.additionalCharges3 = additionalCharges3;
	}
	public double getAdditionalCharges4() {
		return additionalCharges4;
	}
	public void setAdditionalCharges4(double additionalCharges4) {
		this.additionalCharges4 = additionalCharges4;
	}
	public double getAdditionalCharges5() {
		return additionalCharges5;
	}
	public void setAdditionalCharges5(double additionalCharges5) {
		this.additionalCharges5 = additionalCharges5;
	}
	public double getAdditionalCharges6() {
		return additionalCharges6;
	}
	public void setAdditionalCharges6(double additionalCharges6) {
		this.additionalCharges6 = additionalCharges6;
	}
	public double getAdditionalCharges7() {
		return additionalCharges7;
	}
	public void setAdditionalCharges7(double additionalCharges7) {
		this.additionalCharges7 = additionalCharges7;
	}
	public double getAdditionalCharges8() {
		return additionalCharges8;
	}
	public void setAdditionalCharges8(double additionalCharges8) {
		this.additionalCharges8 = additionalCharges8;
	}
	public double getAdditionalCharges9() {
		return additionalCharges9;
	}
	public void setAdditionalCharges9(double additionalCharges9) {
		this.additionalCharges9 = additionalCharges9;
	}
	public double getAdditionalCharges10() {
		return additionalCharges10;
	}
	public void setAdditionalCharges10(double additionalCharges10) {
		this.additionalCharges10 = additionalCharges10;
	}
	public double getAdditionalCharges11() {
		return additionalCharges11;
	}
	public void setAdditionalCharges11(double additionalCharges11) {
		this.additionalCharges11 = additionalCharges11;
	}
	public double getAdditionalCharges12() {
		return additionalCharges12;
	}
	public void setAdditionalCharges12(double additionalCharges12) {
		this.additionalCharges12 = additionalCharges12;
	}
	public double getAdditionalCharges13() {
		return additionalCharges13;
	}
	public void setAdditionalCharges13(double additionalCharges13) {
		this.additionalCharges13 = additionalCharges13;
	}
	public double getAdditionalCharges14() {
		return additionalCharges14;
	}
	public void setAdditionalCharges14(double additionalCharges14) {
		this.additionalCharges14 = additionalCharges14;
	}
	public double getAdditionalCharges15() {
		return additionalCharges15;
	}
	public void setAdditionalCharges15(double additionalCharges15) {
		this.additionalCharges15 = additionalCharges15;
	}
	public double getAdditionalCharges16() {
		return additionalCharges16;
	}
	public void setAdditionalCharges16(double additionalCharges16) {
		this.additionalCharges16 = additionalCharges16;
	}
	public double getAdditionalCharges17() {
		return additionalCharges17;
	}
	public void setAdditionalCharges17(double additionalCharges17) {
		this.additionalCharges17 = additionalCharges17;
	}
	public double getAdditionalCharges18() {
		return additionalCharges18;
	}
	public void setAdditionalCharges18(double additionalCharges18) {
		this.additionalCharges18 = additionalCharges18;
	}
	public double getAdditionalCharges19() {
		return additionalCharges19;
	}
	public void setAdditionalCharges19(double additionalCharges19) {
		this.additionalCharges19 = additionalCharges19;
	}
	public double getAdditionalCharges20() {
		return additionalCharges20;
	}
	public void setAdditionalCharges20(double additionalCharges20) {
		this.additionalCharges20 = additionalCharges20;
	}
	public double getAdditionalCharges21() {
		return additionalCharges21;
	}
	public void setAdditionalCharges21(double additionalCharges21) {
		this.additionalCharges21 = additionalCharges21;
	}
	public double getAdditionalCharges22() {
		return additionalCharges22;
	}
	public void setAdditionalCharges22(double additionalCharges22) {
		this.additionalCharges22 = additionalCharges22;
	}
	public double getAdditionalCharges23() {
		return additionalCharges23;
	}
	public void setAdditionalCharges23(double additionalCharges23) {
		this.additionalCharges23 = additionalCharges23;
	}
	public double getAdditionalCharges24() {
		return additionalCharges24;
	}
	public void setAdditionalCharges24(double additionalCharges24) {
		this.additionalCharges24 = additionalCharges24;
	}
	public double getAdditionalCharges25() {
		return additionalCharges25;
	}
	public void setAdditionalCharges25(double additionalCharges25) {
		this.additionalCharges25 = additionalCharges25;
	}
	public double getAdditionalCharges26() {
		return additionalCharges26;
	}
	public void setAdditionalCharges26(double additionalCharges26) {
		this.additionalCharges26 = additionalCharges26;
	}
	public double getAdditionalCharges27() {
		return additionalCharges27;
	}
	public void setAdditionalCharges27(double additionalCharges27) {
		this.additionalCharges27 = additionalCharges27;
	}
	public double getAdditionalCharges28() {
		return additionalCharges28;
	}
	public void setAdditionalCharges28(double additionalCharges28) {
		this.additionalCharges28 = additionalCharges28;
	}
	public double getAdditionalCharges29() {
		return additionalCharges29;
	}
	public void setAdditionalCharges29(double additionalCharges29) {
		this.additionalCharges29 = additionalCharges29;
	}
	public double getAdditionalCharges30() {
		return additionalCharges30;
	}
	public void setAdditionalCharges30(double additionalCharges30) {
		this.additionalCharges30 = additionalCharges30;
	}
	public double getAdditionalCharges31() {
		return additionalCharges31;
	}
	public void setAdditionalCharges31(double additionalCharges31) {
		this.additionalCharges31 = additionalCharges31;
	}
	public double getAdditionalCharges32() {
		return additionalCharges32;
	}
	public void setAdditionalCharges32(double additionalCharges32) {
		this.additionalCharges32 = additionalCharges32;
	}
	public double getAdditionalCharges33() {
		return additionalCharges33;
	}
	public void setAdditionalCharges33(double additionalCharges33) {
		this.additionalCharges33 = additionalCharges33;
	}
	public double getAdditionalCharges34() {
		return additionalCharges34;
	}
	public void setAdditionalCharges34(double additionalCharges34) {
		this.additionalCharges34 = additionalCharges34;
	}
	public double getAdditionalCharges35() {
		return additionalCharges35;
	}
	public void setAdditionalCharges35(double additionalCharges35) {
		this.additionalCharges35 = additionalCharges35;
	}
	public double getAdditionalCharges36() {
		return additionalCharges36;
	}
	public void setAdditionalCharges36(double additionalCharges36) {
		this.additionalCharges36 = additionalCharges36;
	}
	public double getAdditionalCharges37() {
		return additionalCharges37;
	}
	public void setAdditionalCharges37(double additionalCharges37) {
		this.additionalCharges37 = additionalCharges37;
	}
	public String getAdditionalChargesDescription2() {
		return additionalChargesDescription2;
	}
	public void setAdditionalChargesDescription2(
			String additionalChargesDescription2) {
		this.additionalChargesDescription2 = additionalChargesDescription2;
	}
	public String getAdditionalChargesDescription3() {
		return additionalChargesDescription3;
	}
	public void setAdditionalChargesDescription3(
			String additionalChargesDescription3) {
		this.additionalChargesDescription3 = additionalChargesDescription3;
	}
	public String getAdditionalChargesDescription4() {
		return additionalChargesDescription4;
	}
	public void setAdditionalChargesDescription4(
			String additionalChargesDescription4) {
		this.additionalChargesDescription4 = additionalChargesDescription4;
	}
	public String getAdditionalChargesDescription5() {
		return additionalChargesDescription5;
	}
	public void setAdditionalChargesDescription5(
			String additionalChargesDescription5) {
		this.additionalChargesDescription5 = additionalChargesDescription5;
	}
	public String getAdditionalChargesDescription6() {
		return additionalChargesDescription6;
	}
	public void setAdditionalChargesDescription6(
			String additionalChargesDescription6) {
		this.additionalChargesDescription6 = additionalChargesDescription6;
	}
	public String getAdditionalChargesDescription7() {
		return additionalChargesDescription7;
	}
	public void setAdditionalChargesDescription7(
			String additionalChargesDescription7) {
		this.additionalChargesDescription7 = additionalChargesDescription7;
	}
	public String getAdditionalChargesDescription8() {
		return additionalChargesDescription8;
	}
	public void setAdditionalChargesDescription8(
			String additionalChargesDescription8) {
		this.additionalChargesDescription8 = additionalChargesDescription8;
	}
	public String getAdditionalChargesDescription9() {
		return additionalChargesDescription9;
	}
	public void setAdditionalChargesDescription9(
			String additionalChargesDescription9) {
		this.additionalChargesDescription9 = additionalChargesDescription9;
	}
	public String getAdditionalChargesDescription10() {
		return additionalChargesDescription10;
	}
	public void setAdditionalChargesDescription10(
			String additionalChargesDescription10) {
		this.additionalChargesDescription10 = additionalChargesDescription10;
	}
	public String getAdditionalChargesDescription11() {
		return additionalChargesDescription11;
	}
	public void setAdditionalChargesDescription11(
			String additionalChargesDescription11) {
		this.additionalChargesDescription11 = additionalChargesDescription11;
	}
	public String getAdditionalChargesDescription12() {
		return additionalChargesDescription12;
	}
	public void setAdditionalChargesDescription12(
			String additionalChargesDescription12) {
		this.additionalChargesDescription12 = additionalChargesDescription12;
	}
	public String getAdditionalChargesDescription13() {
		return additionalChargesDescription13;
	}
	public void setAdditionalChargesDescription13(
			String additionalChargesDescription13) {
		this.additionalChargesDescription13 = additionalChargesDescription13;
	}
	public String getAdditionalChargesDescription14() {
		return additionalChargesDescription14;
	}
	public void setAdditionalChargesDescription14(
			String additionalChargesDescription14) {
		this.additionalChargesDescription14 = additionalChargesDescription14;
	}
	public String getAdditionalChargesDescription15() {
		return additionalChargesDescription15;
	}
	public void setAdditionalChargesDescription15(
			String additionalChargesDescription15) {
		this.additionalChargesDescription15 = additionalChargesDescription15;
	}
	public String getAdditionalChargesDescription16() {
		return additionalChargesDescription16;
	}
	public void setAdditionalChargesDescription16(
			String additionalChargesDescription16) {
		this.additionalChargesDescription16 = additionalChargesDescription16;
	}
	public String getAdditionalChargesDescription17() {
		return additionalChargesDescription17;
	}
	public void setAdditionalChargesDescription17(
			String additionalChargesDescription17) {
		this.additionalChargesDescription17 = additionalChargesDescription17;
	}
	public String getAdditionalChargesDescription18() {
		return additionalChargesDescription18;
	}
	public void setAdditionalChargesDescription18(
			String additionalChargesDescription18) {
		this.additionalChargesDescription18 = additionalChargesDescription18;
	}
	public String getAdditionalChargesDescription19() {
		return additionalChargesDescription19;
	}
	public void setAdditionalChargesDescription19(
			String additionalChargesDescription19) {
		this.additionalChargesDescription19 = additionalChargesDescription19;
	}
	public String getAdditionalChargesDescription20() {
		return additionalChargesDescription20;
	}
	public void setAdditionalChargesDescription20(
			String additionalChargesDescription20) {
		this.additionalChargesDescription20 = additionalChargesDescription20;
	}
	public String getAdditionalChargesDescription21() {
		return additionalChargesDescription21;
	}
	public void setAdditionalChargesDescription21(
			String additionalChargesDescription21) {
		this.additionalChargesDescription21 = additionalChargesDescription21;
	}
	public String getAdditionalChargesDescription22() {
		return additionalChargesDescription22;
	}
	public void setAdditionalChargesDescription22(
			String additionalChargesDescription22) {
		this.additionalChargesDescription22 = additionalChargesDescription22;
	}
	public String getAdditionalChargesDescription23() {
		return additionalChargesDescription23;
	}
	public void setAdditionalChargesDescription23(
			String additionalChargesDescription23) {
		this.additionalChargesDescription23 = additionalChargesDescription23;
	}
	public String getAdditionalChargesDescription24() {
		return additionalChargesDescription24;
	}
	public void setAdditionalChargesDescription24(
			String additionalChargesDescription24) {
		this.additionalChargesDescription24 = additionalChargesDescription24;
	}
	public String getAdditionalChargesDescription25() {
		return additionalChargesDescription25;
	}
	public void setAdditionalChargesDescription25(
			String additionalChargesDescription25) {
		this.additionalChargesDescription25 = additionalChargesDescription25;
	}
	public String getAdditionalChargesDescription26() {
		return additionalChargesDescription26;
	}
	public void setAdditionalChargesDescription26(
			String additionalChargesDescription26) {
		this.additionalChargesDescription26 = additionalChargesDescription26;
	}
	public String getAdditionalChargesDescription27() {
		return additionalChargesDescription27;
	}
	public void setAdditionalChargesDescription27(
			String additionalChargesDescription27) {
		this.additionalChargesDescription27 = additionalChargesDescription27;
	}
	public String getAdditionalChargesDescription28() {
		return additionalChargesDescription28;
	}
	public void setAdditionalChargesDescription28(
			String additionalChargesDescription28) {
		this.additionalChargesDescription28 = additionalChargesDescription28;
	}
	public String getAdditionalChargesDescription29() {
		return additionalChargesDescription29;
	}
	public void setAdditionalChargesDescription29(
			String additionalChargesDescription29) {
		this.additionalChargesDescription29 = additionalChargesDescription29;
	}
	public String getAdditionalChargesDescription30() {
		return additionalChargesDescription30;
	}
	public void setAdditionalChargesDescription30(
			String additionalChargesDescription30) {
		this.additionalChargesDescription30 = additionalChargesDescription30;
	}
	public String getAdditionalChargesDescription31() {
		return additionalChargesDescription31;
	}
	public void setAdditionalChargesDescription31(
			String additionalChargesDescription31) {
		this.additionalChargesDescription31 = additionalChargesDescription31;
	}
	public String getAdditionalChargesDescription32() {
		return additionalChargesDescription32;
	}
	public void setAdditionalChargesDescription32(
			String additionalChargesDescription32) {
		this.additionalChargesDescription32 = additionalChargesDescription32;
	}
	public String getAdditionalChargesDescription33() {
		return additionalChargesDescription33;
	}
	public void setAdditionalChargesDescription33(
			String additionalChargesDescription33) {
		this.additionalChargesDescription33 = additionalChargesDescription33;
	}
	public String getAdditionalChargesDescription34() {
		return additionalChargesDescription34;
	}
	public void setAdditionalChargesDescription34(
			String additionalChargesDescription34) {
		this.additionalChargesDescription34 = additionalChargesDescription34;
	}
	public String getAdditionalChargesDescription35() {
		return additionalChargesDescription35;
	}
	public void setAdditionalChargesDescription35(
			String additionalChargesDescription35) {
		this.additionalChargesDescription35 = additionalChargesDescription35;
	}
	public String getAdditionalChargesDescription36() {
		return additionalChargesDescription36;
	}
	public void setAdditionalChargesDescription36(
			String additionalChargesDescription36) {
		this.additionalChargesDescription36 = additionalChargesDescription36;
	}
	public String getAdditionalChargesDescription37() {
		return additionalChargesDescription37;
	}
	public void setAdditionalChargesDescription37(
			String additionalChargesDescription37) {
		this.additionalChargesDescription37 = additionalChargesDescription37;
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
	public String getDefaultShipVia() {
		return defaultShipVia;
	}
	public void setDefaultShipVia(String defaultShipVia) {
		this.defaultShipVia = defaultShipVia;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public int getDefaultBillToId() {
		return defaultBillToId;
	}
	public void setDefaultBillToId(int defaultBillToId) {
		this.defaultBillToId = defaultBillToId;
	}
	public int getDefaultShipToId() {
		return defaultShipToId;
	}
	public void setDefaultShipToId(int defaultShipToId) {
		this.defaultShipToId = defaultShipToId;
	}
	public String getSameAsBillingAddress() {
		return sameAsBillingAddress;
	}
	public void setSameAsBillingAddress(String sameAsBillingAddress) {
		this.sameAsBillingAddress = sameAsBillingAddress;
	}
	public String[] getCustomerNumbers() {
		return CustomerNumbers;
	}
	public void setCustomerNumbers(String[] customerNumbers) {
		CustomerNumbers = customerNumbers;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getUpdateMode() {
		return updateMode;
	}
	public void setUpdateMode(String updateMode) {
		this.updateMode = updateMode;
	}
	public Country getCountryModel() {
		return countryModel;
	}
	public void setCountryModel(Country countryModel) {
		this.countryModel = countryModel;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public String getCustomFieldName() {
		return customFieldName;
	}
	public void setCustomFieldName(String customFieldName) {
		this.customFieldName = customFieldName;
	}
	public int getSubsetId() {
		return subsetId;
	}
	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}
	public boolean isSuperuser() {
		return superuser;
	}
	public void setSuperuser(boolean superuser) {
		this.superuser = superuser;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isHideHistory() {
		return hideHistory;
	}
	public void setHideHistory(boolean hideHistory) {
		this.hideHistory = hideHistory;
	}
	public boolean isHideAccountInquiry() {
		return hideAccountInquiry;
	}
	public void setHideAccountInquiry(boolean hideAccountInquiry) {
		this.hideAccountInquiry = hideAccountInquiry;
	}
	public String getIsCreditCard() {
		return isCreditCard;
	}
	public void setIsCreditCard(String isCreditCard) {
		this.isCreditCard = isCreditCard;
	}
	public ArrayList<String> geteCommerceIDList() {
		return eCommerceIDList;
	}
	public void seteCommerceIDList(ArrayList<String> eCommerceIDList) {
		this.eCommerceIDList = eCommerceIDList;
	}
	public ArrayList<CreditCardModel> getCreditCardList() {
		return creditCardList;
	}
	public void setCreditCardList(ArrayList<CreditCardModel> creditCardList) {
		this.creditCardList = creditCardList;
	}
	public boolean isCustomerAccountExist() {
		return customerAccountExist;
	}
	public void setCustomerAccountExist(boolean customerAccountExist) {
		this.customerAccountExist = customerAccountExist;
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public String getMakeAsDefault() {
		return makeAsDefault;
	}
	public void setMakeAsDefault(String makeAsDefault) {
		this.makeAsDefault = makeAsDefault;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getImagePosition() {
		return imagePosition;
	}
	public void setImagePosition(String imagePosition) {
		this.imagePosition = imagePosition;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getBannerScroll() {
		return bannerScroll;
	}
	public void setBannerScroll(String bannerScroll) {
		this.bannerScroll = bannerScroll;
	}
	public String getBannerDelay() {
		return bannerDelay;
	}
	public void setBannerDelay(String bannerDelay) {
		this.bannerDelay = bannerDelay;
	}
	public String getBannerNumberofItem() {
		return bannerNumberofItem;
	}
	public void setBannerNumberofItem(String bannerNumberofItem) {
		this.bannerNumberofItem = bannerNumberofItem;
	}
	public String getBannerDirection() {
		return bannerDirection;
	}
	public void setBannerDirection(String bannerDirection) {
		this.bannerDirection = bannerDirection;
	}
	public String getZipCodeStringFormat() {
		return zipCodeStringFormat;
	}
	public void setZipCodeStringFormat(String zipCodeStringFormat) {
		this.zipCodeStringFormat = zipCodeStringFormat;
	}
	public String getCustomerShortName() {
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}
	public String getHomeBranch() {
		return homeBranch;
	}
	public void setHomeBranch(String homeBranch) {
		this.homeBranch = homeBranch;
	}
	public String getBillToId() {
		return billToId;
	}
	public void setBillToId(String billToId) {
		this.billToId = billToId;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getTermsType() {
		return termsType;
	}
	public void setTermsType(String termsType) {
		this.termsType = termsType;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public int getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(int taxCode) {
		this.taxCode = taxCode;
	}
	public String getCountryFullName() {
		return countryFullName;
	}
	public void setCountryFullName(String countryFullName) {
		this.countryFullName = countryFullName;
	}
	public int getWareHouseCode() {
		return wareHouseCode;
	}
	public void setWareHouseCode(int wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}
	public String getWareHouseCodeStr() {
		return wareHouseCodeStr;
	}
	public void setWareHouseCodeStr(String wareHouseCodeStr) {
		this.wareHouseCodeStr = wareHouseCodeStr;
	}
	public String getCreditManagerStr() {
		return creditManagerStr;
	}
	public void setCreditManagerStr(String creditManagerStr) {
		this.creditManagerStr = creditManagerStr;
	}
	public String getSalesRepIn() {
		return salesRepIn;
	}
	public void setSalesRepIn(String salesRepIn) {
		this.salesRepIn = salesRepIn;
	}
	public String getSalesRepOut() {
		return salesRepOut;
	}
	public void setSalesRepOut(String salesRepOut) {
		this.salesRepOut = salesRepOut;
	}
	public int getDivisionNo() {
		return divisionNo;
	}
	public void setDivisionNo(int divisionNo) {
		this.divisionNo = divisionNo;
	}
	public int getAddressBookId() {
		return addressBookId;
	}
	public void setAddressBookId(int addressBookId) {
		this.addressBookId = addressBookId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public boolean isAddressActive() {
		return isAddressActive;
	}
	public void setAddressActive(boolean isAddressActive) {
		this.isAddressActive = isAddressActive;
	}
	public String getAddressTitle() {
		return addressTitle;
	}
	public void setAddressTitle(String addressTitle) {
		this.addressTitle = addressTitle;
	}
	public String getAddressRegisteredDate() {
		return addressRegisteredDate;
	}
	public void setAddressRegisteredDate(String addressRegisteredDate) {
		this.addressRegisteredDate = addressRegisteredDate;
	}
	public Date getAddressRegisteredDateDateFormat() {
		return addressRegisteredDateDateFormat;
	}
	public void setAddressRegisteredDateDateFormat(
			Date addressRegisteredDateDateFormat) {
		this.addressRegisteredDateDateFormat = addressRegisteredDateDateFormat;
	}
	public String getReqDate() {
		return reqDate;
	}
	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}
	public String getShippingInstruction() {
		return shippingInstruction;
	}
	public void setShippingInstruction(String shippingInstruction) {
		this.shippingInstruction = shippingInstruction;
	}
	public String getShipVia() {
		return shipVia;
	}
	public void setShipVia(String shipVia) {
		this.shipVia = shipVia;
	}
	public String getOrderNotes() {
		return orderNotes;
	}
	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}
	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public ArrayList<ProductsModel> getBranchAvail() {
		return branchAvail;
	}
	public void setBranchAvail(ArrayList<ProductsModel> branchAvail) {
		this.branchAvail = branchAvail;
	}
	public ArrayList<String> getContactDescriptionList() {
		return contactDescriptionList;
	}
	public void setContactDescriptionList(ArrayList<String> contactDescriptionList) {
		this.contactDescriptionList = contactDescriptionList;
	}
	public ArrayList<AddressModel> getContactShortList() {
		return contactShortList;
	}
	public void setContactShortList(ArrayList<AddressModel> contactShortList) {
		this.contactShortList = contactShortList;
	}
	public ArrayList<String> getDescriptionList() {
		return descriptionList;
	}
	public void setDescriptionList(ArrayList<String> descriptionList) {
		this.descriptionList = descriptionList;
	}
	public ArrayList<String> getErpPartNumberList() {
		return erpPartNumberList;
	}
	public void setErpPartNumberList(ArrayList<String> erpPartNumberList) {
		this.erpPartNumberList = erpPartNumberList;
	}
	public boolean isValidBuyer() {
		return isValidBuyer;
	}
	public void setValidBuyer(boolean isValidBuyer) {
		this.isValidBuyer = isValidBuyer;
	}
	public double getArCreditAvail() {
		return arCreditAvail;
	}
	public void setArCreditAvail(double arCreditAvail) {
		this.arCreditAvail = arCreditAvail;
	}
	public double getArCreditLimit() {
		return arCreditLimit;
	}
	public void setArCreditLimit(double arCreditLimit) {
		this.arCreditLimit = arCreditLimit;
	}
	public double getaRDeposits() {
		return aRDeposits;
	}
	public void setaRDeposits(double aRDeposits) {
		this.aRDeposits = aRDeposits;
	}
	public double getaROrders() {
		return aROrders;
	}
	public void setaROrders(double aROrders) {
		this.aROrders = aROrders;
	}
	public double getaRTotal() {
		return aRTotal;
	}
	public void setaRTotal(double aRTotal) {
		this.aRTotal = aRTotal;
	}
	public double getCashDiscountAmount() {
		return cashDiscountAmount;
	}
	public void setCashDiscountAmount(double cashDiscountAmount) {
		this.cashDiscountAmount = cashDiscountAmount;
	}
	public double getCashDiscountPercentage() {
		return cashDiscountPercentage;
	}
	public void setCashDiscountPercentage(double cashDiscountPercentage) {
		this.cashDiscountPercentage = cashDiscountPercentage;
	}
	public double getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}
	public double getCurrent() {
		return current;
	}
	public void setCurrent(double current) {
		this.current = current;
	}
	public double getFederalExciseTax() {
		return federalExciseTax;
	}
	public void setFederalExciseTax(double federalExciseTax) {
		this.federalExciseTax = federalExciseTax;
	}
	public double getFreight() {
		return freight;
	}
	public void setFreight(double freight) {
		this.freight = freight;
	}
	public double getFuture() {
		return future;
	}
	public void setFuture(double future) {
		this.future = future;
	}
	public double getHandling() {
		return handling;
	}
	public void setHandling(double handling) {
		this.handling = handling;
	}
	public double getLastPaymentAmount() {
		return lastPaymentAmount;
	}
	public void setLastPaymentAmount(double lastPaymentAmount) {
		this.lastPaymentAmount = lastPaymentAmount;
	}
	public double getLastSaleAmount() {
		return lastSaleAmount;
	}
	public void setLastSaleAmount(double lastSaleAmount) {
		this.lastSaleAmount = lastSaleAmount;
	}
	public double getmTDSales() {
		return mTDSales;
	}
	public void setmTDSales(double mTDSales) {
		this.mTDSales = mTDSales;
	}
	public double getNinety() {
		return ninety;
	}
	public void setNinety(double ninety) {
		this.ninety = ninety;
	}
	public double getOneTwenty() {
		return oneTwenty;
	}
	public void setOneTwenty(double oneTwenty) {
		this.oneTwenty = oneTwenty;
	}
	public double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public double getPaymentDays() {
		return paymentDays;
	}
	public void setPaymentDays(double paymentDays) {
		this.paymentDays = paymentDays;
	}
	public double getrOEDiscount() {
		return rOEDiscount;
	}
	public void setrOEDiscount(double rOEDiscount) {
		this.rOEDiscount = rOEDiscount;
	}
	public double getSixMonthAverage() {
		return sixMonthAverage;
	}
	public void setSixMonthAverage(double sixMonthAverage) {
		this.sixMonthAverage = sixMonthAverage;
	}
	public double getSixMonthHigh() {
		return sixMonthHigh;
	}
	public void setSixMonthHigh(double sixMonthHigh) {
		this.sixMonthHigh = sixMonthHigh;
	}
	public double getSixty() {
		return sixty;
	}
	public void setSixty(double sixty) {
		this.sixty = sixty;
	}
	public double getyTDSales() {
		return yTDSales;
	}
	public void setyTDSales(double yTDSales) {
		this.yTDSales = yTDSales;
	}
	public int getBranchTotal() {
		return branchTotal;
	}
	public void setBranchTotal(int branchTotal) {
		this.branchTotal = branchTotal;
	}
	public int getBuyingCompanyId() {
		return buyingCompanyId;
	}
	public void setBuyingCompanyId(int buyingCompanyId) {
		this.buyingCompanyId = buyingCompanyId;
	}
	public int getPcardId() {
		return pcardId;
	}
	public void setPcardId(int pcardId) {
		this.pcardId = pcardId;
	}
	public int getResultCount() {
		return resultCount;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public LinkedHashMap<String, ProductsModel> getBranchTeritory() {
		return branchTeritory;
	}
	public void setBranchTeritory(
			LinkedHashMap<String, ProductsModel> branchTeritory) {
		this.branchTeritory = branchTeritory;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
	public String getApproverAgentAssignStatus() {
		return approverAgentAssignStatus;
	}
	public void setApproverAgentAssignStatus(String approverAgentAssignStatus) {
		this.approverAgentAssignStatus = approverAgentAssignStatus;
	}
	public String getApproverEmail() {
		return approverEmail;
	}
	public void setApproverEmail(String approverEmail) {
		this.approverEmail = approverEmail;
	}
	public String getArTerms() {
		return arTerms;
	}
	public void setArTerms(String arTerms) {
		this.arTerms = arTerms;
	}
	public String getAsOfDate() {
		return asOfDate;
	}
	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}
	public String getBranchID() {
		return branchID;
	}
	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBuildingNumber() {
		return buildingNumber;
	}
	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}
	public String getCardHolder() {
		return CardHolder;
	}
	public void setCardHolder(String cardHolder) {
		CardHolder = cardHolder;
	}
	public String getCardResponse() {
		return cardResponse;
	}
	public void setCardResponse(String cardResponse) {
		this.cardResponse = cardResponse;
	}
	public String getChangedPassword() {
		return changedPassword;
	}
	public void setChangedPassword(String changedPassword) {
		this.changedPassword = changedPassword;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getConnectionError() {
		return connectionError;
	}
	public void setConnectionError(String connectionError) {
		this.connectionError = connectionError;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public String getCreditCardType() {
		return creditCardType;
	}
	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getErpLoginId() {
		return erpLoginId;
	}
	public void setErpLoginId(String erpLoginId) {
		this.erpLoginId = erpLoginId;
	}
	public String getErpPartNumber() {
		return erpPartNumber;
	}
	public void setErpPartNumber(String erpPartNumber) {
		this.erpPartNumber = erpPartNumber;
	}
	public String getElementPaymentAccountId() {
		return elementPaymentAccountId;
	}
	public void setElementPaymentAccountId(String elementPaymentAccountId) {
		this.elementPaymentAccountId = elementPaymentAccountId;
	}
	public String getElementSetupId() {
		return elementSetupId;
	}
	public void setElementSetupId(String elementSetupId) {
		this.elementSetupId = elementSetupId;
	}
	public String getElementSetupUrl() {
		return elementSetupUrl;
	}
	public void setElementSetupUrl(String elementSetupUrl) {
		this.elementSetupUrl = elementSetupUrl;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getExtrensicType() {
		return extrensicType;
	}
	public void setExtrensicType(String extrensicType) {
		this.extrensicType = extrensicType;
	}
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getHomeBranchavailablity() {
		return homeBranchavailablity;
	}
	public void setHomeBranchavailablity(String homeBranchavailablity) {
		this.homeBranchavailablity = homeBranchavailablity;
	}
	public String getIsAuthPurchaseAgent() {
		return isAuthPurchaseAgent;
	}
	public void setIsAuthPurchaseAgent(String isAuthPurchaseAgent) {
		this.isAuthPurchaseAgent = isAuthPurchaseAgent;
	}
	public String getIsBusyUser() {
		return isBusyUser;
	}
	public void setIsBusyUser(String isBusyUser) {
		this.isBusyUser = isBusyUser;
	}
	public String getIsSuperUser() {
		return isSuperUser;
	}
	public void setIsSuperUser(String isSuperUser) {
		this.isSuperUser = isSuperUser;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLastPaymentDate() {
		return lastPaymentDate;
	}
	public void setLastPaymentDate(String lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}
	public String getLastSaleDate() {
		return lastSaleDate;
	}
	public void setLastSaleDate(String lastSaleDate) {
		this.lastSaleDate = lastSaleDate;
	}
	public String getNewsLetterSub() {
		return newsLetterSub;
	}
	public void setNewsLetterSub(String newsLetterSub) {
		this.newsLetterSub = newsLetterSub;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getOrderedBy() {
		return orderedBy;
	}
	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getPobox() {
		return pobox;
	}
	public void setPobox(String pobox) {
		this.pobox = pobox;
	}
	public String getRequestAuthorizationLevel() {
		return requestAuthorizationLevel;
	}
	public void setRequestAuthorizationLevel(String requestAuthorizationLevel) {
		this.requestAuthorizationLevel = requestAuthorizationLevel;
	}
	public String getSalesContact() {
		return salesContact;
	}
	public void setSalesContact(String salesContact) {
		this.salesContact = salesContact;
	}
	public String getSessionID() {
		return SessionID;
	}
	public void setSessionID(String sessionID) {
		SessionID = sessionID;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getShipToId() {
		return shipToId;
	}
	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}
	public String getShipViaID() {
		return ShipViaID;
	}
	public void setShipViaID(String shipViaID) {
		ShipViaID = shipViaID;
	}
	public String getShipViaMethod() {
		return ShipViaMethod;
	}
	public void setShipViaMethod(String shipViaMethod) {
		ShipViaMethod = shipViaMethod;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getUseEntityAddress() {
		return useEntityAddress;
	}
	public void setUseEntityAddress(String useEntityAddress) {
		this.useEntityAddress = useEntityAddress;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getVertical() {
		return vertical;
	}
	public void setVertical(String vertical) {
		this.vertical = vertical;
	}
	public String getWebAddress() {
		return webAddress;
	}
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public void setOrderEntryOk(String orderEntryOk) {
		this.orderEntryOk = orderEntryOk;
	}
	public String getOrderEntryOk() {
		return orderEntryOk;
	}
	public String getTermsTypeDesc() {
		return termsTypeDesc;
	}
	public void setTermsTypeDesc(String termsTypeDesc) {
		this.termsTypeDesc = termsTypeDesc;
	}

	public void setThirty(double thirty) {
		this.thirty = thirty;
	}
	public double getThirty() {
		return thirty;
	}
	
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getTax() {
		return tax;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getTotal() {
		return total;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setShipViaList(ArrayList<ShipVia> shipViaList) {
		this.shipViaList = shipViaList;
	}
	public ArrayList<ShipVia> getShipViaList() {
		return shipViaList;
	}
	public ArrayList<CreditCardModel> getCreditCardDetailList() {
		return creditCardDetailList;
	}
	public void setCreditCardDetailList(
			ArrayList<CreditCardModel> creditCardDetailList) {
		this.creditCardDetailList = creditCardDetailList;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public void setEditedShipId(int editedShipId) {
		this.editedShipId = editedShipId;
	}
	public int getEditedShipId() {
		return editedShipId;
	}
	public void setBankTransactionId(String bankTransactionId) {
		this.bankTransactionId = bankTransactionId;
	}
	public String getBankTransactionId() {
		return bankTransactionId;
	}
	public void setBankApprovalCode(String bankApprovalCode) {
		this.bankApprovalCode = bankApprovalCode;
	}
	public String getBankApprovalCode() {
		return bankApprovalCode;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setContactClassification(String contactClassification) {
		this.contactClassification = contactClassification;
	}
	public String getContactClassification() {
		return contactClassification;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public ArrayList<UsersModel> getOrderStatusList() {
		return orderStatusList;
	}
	public void setOrderStatusList(ArrayList<UsersModel> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}
	public String getOrderStatusCode() {
		return orderStatusCode;
	}
	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public String getCustomerReleaseNumber() {
		return customerReleaseNumber;
	}
	public void setCustomerReleaseNumber(String customerReleaseNumber) {
		this.customerReleaseNumber = customerReleaseNumber;
	}
	public void setFreightIn(double freightIn) {
		this.freightIn = freightIn;
	}
	public double getFreightIn() {
		return freightIn;
	}
	public String getFreightDesc() {
		return freightDesc;
	}
	public void setFreightDesc(String freightDesc) {
		this.freightDesc = freightDesc;
	}
	public String getFreightInDesc() {
		return freightInDesc;
	}
	public void setFreightInDesc(String freightInDesc) {
		this.freightInDesc = freightInDesc;
	}
	public String getShipToName() {
		return shipToName;
	}
	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}
	public double getSubtotalV2() {
		return subtotalV2;
	}
	public void setSubtotalV2(double subtotalV2) {
		this.subtotalV2 = subtotalV2;
	}
	public void setOrderSuffix(String orderSuffix) {
		this.orderSuffix = orderSuffix;
	}
	public String getOrderSuffix() {
		return orderSuffix;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getDisableSubmitPO() {
		return disableSubmitPO;
	}
	public void setDisableSubmitPO(String disableSubmitPO) {
		this.disableSubmitPO = disableSubmitPO;
	}
	public String getDisableSubmitPOCC() {
		return disableSubmitPOCC;
	}
	public void setDisableSubmitPOCC(String disableSubmitPOCC) {
		this.disableSubmitPOCC = disableSubmitPOCC;
	}
	public ArrayList<String> getCustomerTypeList() {
		return customerTypeList;
	}
	public void setCustomerTypeList(ArrayList<String> customerTypeList) {
		this.customerTypeList = customerTypeList;
	}
	public void setLoginMessage(String loginMessage) {
		this.loginMessage = loginMessage;
	}
	public String getLoginMessage() {
		return loginMessage;
	}
	public void setCustomFieldValue(String customFieldValue) {
		this.customFieldValue = customFieldValue;
	}
	public String getCustomFieldValue() {
		return customFieldValue;
	}
	public Cimm2BCentralARBalanceDetails getArBalanceDetails() {
		return arBalanceDetails;
	}
	public void setArBalanceDetails(Cimm2BCentralARBalanceDetails arBalanceDetails) {
		this.arBalanceDetails = arBalanceDetails;
	}
	public Cimm2BCentralARBalanceSummary getArBalanceSummary() {
		return arBalanceSummary;
	}
	public void setArBalanceSummary(Cimm2BCentralARBalanceSummary arBalanceSummary) {
		this.arBalanceSummary = arBalanceSummary;
	}
	public String getWareHouseName() {
		return wareHouseName;
	}
	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}
	public String getIncludePeriod() {
		return includePeriod;
	}
	public void setIncludePeriod(String includePeriod) {
		this.includePeriod = includePeriod;
	}
	public ArrayList<String> getContactIdList() {
		return contactIdList;
	}
	public void setContactIdList(ArrayList<String> contactIdList) {
		this.contactIdList = contactIdList;
	}
	public ArrayList<String> getShipIdList() {
		return shipIdList;
	}
	public void setShipIdList(ArrayList<String> shipIdList) {
		this.shipIdList = shipIdList;
	}
	public String getBillEntityId() {
		return billEntityId;
	}
	public void setBillEntityId(String billEntityId) {
		this.billEntityId = billEntityId;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getShipEntityId() {
		return shipEntityId;
	}
	public void setShipEntityId(String shipEntityId) {
		this.shipEntityId = shipEntityId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public boolean isCodFlag() {
		return codFlag;
	}
	public void setCodFlag(boolean codFlag) {
		this.codFlag = codFlag;
	}
	public ArrayList<UsersModel> getShipToList() {
		return shipToList;
	}
	public void setShipToList(ArrayList<UsersModel> shipToList) {
		this.shipToList = shipToList;
	}
	public boolean isPoRequired() {
		return poRequired;
	}
	public void setPoRequired(boolean poRequired) {
		this.poRequired = poRequired;
	}
	public String getPricePrecision() {
		return pricePrecision;
	}
	public void setPricePrecision(String pricePrecision) {
		this.pricePrecision = pricePrecision;
	}
	public String getDepartment2AB() {
		return department2AB;
	}
	public void setDepartment2AB(String department2ab) {
		department2AB = department2ab;
	}
	public String getUserERPId() {
		return userERPId;
	}
	public void setUserERPId(String userERPId) {
		this.userERPId = userERPId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPaymentTerm() {
		return paymentTerm;
	}
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}
	public boolean isAnonymousUser() {
		return anonymousUser;
	}
	public void setAnonymousUser(boolean anonymousUser) {
		this.anonymousUser = anonymousUser;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isUpdateRole() {
		return isUpdateRole;
	}
	public void setUpdateRole(boolean isUpdateRole) {
		this.isUpdateRole = isUpdateRole;
	}
	public AddressModel getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(AddressModel shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getIsSpecialUser() {
		return isSpecialUser;
	}
	public void setIsSpecialUser(String isSpecialUser) {
		this.isSpecialUser = isSpecialUser;
	}
	public String getWoeUserName() {
		return woeUserName;
	}
	public void setWoeUserName(String woeUserName) {
		this.woeUserName = woeUserName;
	}
	public String getWoeLogin() {
		return woeLogin;
	}
	public void setWoeLogin(String woeLogin) {
		this.woeLogin = woeLogin;
	}
	public String getWoePassword() {
		return woePassword;
	}
	public void setWoePassword(String woePassword) {
		this.woePassword = woePassword;
	}
	public String getWoeConfirmPassword() {
		return woeConfirmPassword;
	}
	public void setWoeConfirmPassword(String woeConfirmPassword) {
		this.woeConfirmPassword = woeConfirmPassword;
	}
	public String getIsAuthTechnician() {
		return isAuthTechnician;
	}
	public void setIsAuthTechnician(String isAuthTechnician) {
		this.isAuthTechnician = isAuthTechnician;
	}
	public String getShippingOrgType() {
		return shippingOrgType;
	}
	public void setShippingOrgType(String shippingOrgType) {
		this.shippingOrgType = shippingOrgType;
	}
	public String getIsTaxable() {
		return isTaxable;
	}
	public void setIsTaxable(String isTaxable) {
		this.isTaxable = isTaxable;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getEnablePO() {
		return enablePO;
	}
	public void setEnablePO(String enablePO) {
		this.enablePO = enablePO;
	}
	public String getShipToWarehouseCode() {
		return shipToWarehouseCode;
	}
	public void setShipToWarehouseCode(String shipToWarehouseCode) {
		this.shipToWarehouseCode = shipToWarehouseCode;
	}	
	public String getDateOfFirstSale() {
		return dateOfFirstSale;
	}
	public void setDateOfFirstSale(String dateOfFirstSale) {
		this.dateOfFirstSale = dateOfFirstSale;
	}
	public Double getPeriod5() {
		return period5;
	}
	public void setPeriod5(Double period5) {
		this.period5 = period5;
	}
	public Double getPeriod6() {
		return period6;
	}
	public void setPeriod6(Double period6) {
		this.period6 = period6;
	}
	public Double getPeriod7() {
		return period7;
	}
	public void setPeriod7(Double period7) {
		this.period7 = period7;
	}
	
	@Expose private double avgPaymentDays;
	@Expose private double pastDues;

	public double getAvgPaymentDays() {
		return avgPaymentDays;
	}
	public void setAvgPaymentDays(double avgPaymentDays) {
		this.avgPaymentDays = avgPaymentDays;
	}
	public double getPastDues() {
		return pastDues;
	}
	public void setPastDues(double pastDues) {
		this.pastDues = pastDues;
	}	
	public String getCreditDate() {
		return creditDate;
	}
	public void setCreditDate(String creditDate) {
		this.creditDate = creditDate;
	}
	public String getContactTitle() {
		return contactTitle;
	}
	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}
	public String getContactWebsite() {
		return contactWebsite;
	}
	public void setContactWebsite(String contactWebsite) {
		this.contactWebsite = contactWebsite;
	}
	
}
