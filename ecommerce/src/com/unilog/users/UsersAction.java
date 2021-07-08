package com.unilog.users;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jboss.messaging.core.impl.postoffice.AddAllReplicatedDeliveriesMessage;
import org.jdom.input.SAXBuilder;

import com.bronto.api.BrontoApi;
import com.erp.service.LoginSubmitManagement;
import com.erp.service.ProductManagement;
import com.erp.service.SalesOrderManagement;
import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.action.SalesOrderManagementAction;
import com.erp.service.cimm2bcentral.action.UserManagementAction;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceSummary;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerType;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCylinderBalanceSummary;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.impl.LoginSubmitManagementImpl;
import com.erp.service.impl.ProductManagementImpl;
import com.erp.service.impl.SalesOrderManagementImpl;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.LoginSubmitManagementModel;
import com.erp.service.model.ProductManagementModel;
import com.erp.service.model.SalesOrderManagementModel;
import com.erp.service.model.UserManagementModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.api.bronto.BrontoDAO;
import com.unilog.api.bronto.BrontoModel;
import com.unilog.api.bronto.BrontoUtility;
import com.unilog.captcha.GoogleRecaptchaV3;
import com.unilog.customfields.CustomFieldDAO;
import com.unilog.customfields.CustomModel;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.custommodule.dao.DxFeedDAOService;
import com.unilog.custommodule.model.CustomShipViaTable;
import com.unilog.custommodule.utility.WillCall;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailModel;
import com.unilog.defaults.SendMailUtility;
import com.unilog.geolocator.GeoLocatorEnums;
import com.unilog.geolocator.GeoLocatorRequest;
import com.unilog.geolocator.GeoLocatorResponse;
import com.unilog.geolocator.IGeoLocatorService;
import com.unilog.geolocator.GeoLocatorEnums.GeoLocatorProvider;
import com.unilog.geolocator.service.provider.GeoLocatorServiceProvider;
import com.unilog.misc.EventDetails;
import com.unilog.misc.EventModel;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.misc.YahooBossSupport;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsAction;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.SalesDAO;
import com.unilog.sales.SalesModel;
import com.unilog.security.LoginAuthentication;
import com.unilog.security.SecureData;
import com.unilog.security.TripleDESEncryption;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.utility.CustomTableModel;
import com.unilog.velocitytool.CIMM2VelocityTool;
import com.unilognew.model.BuyingCompanyAddressBook;
import com.unilognew.service.UserFacadeService;
import com.unilognew.util.CimmUtil;
import com.unilognew.util.ECommerceEnumType.AddressType;
import com.unilognew.util.ECommerceEnumType.ErpType;

import facebook4j.internal.org.json.JSONArray;
import test.XLSXReaderWriter;
import test.XLSXReaderWriterP21;
import com.unilog.ecomm.model.Cart;
import com.unilog.ecomm.model.Coupon;
import com.unilog.ecomm.model.Discount;
import com.unilog.ecomm.model.LineItem;
import com.unilog.ecomm.promotion.SalesPromotionService;

public class UsersAction extends ActionSupport {

	private static final long serialVersionUID = 4245602390176404970L;

	private String renderContent;
	ArrayList<String> shipVia = new ArrayList<String>();
	private ArrayList<MenuAndBannersModal> bottomBanners = new ArrayList<MenuAndBannersModal>();
	private ArrayList<ProductsModel> breadCrumbList = null;
	private ArrayList<MenuAndBannersModal> leftBanners = new ArrayList<MenuAndBannersModal>();
	private ArrayList<MenuAndBannersModal> subMenuList = null;
	private ArrayList<MenuAndBannersModal> rightBanners = new ArrayList<MenuAndBannersModal>();
	private ArrayList<MenuAndBannersModal> topBanners = new ArrayList<MenuAndBannersModal>();
	private ArrayList<EventModel> eventCatData;
	private ArrayList<EventModel> eventCatList;
	private ArrayList<EventModel> eventsType;
	private ArrayList<Integer> assignedShipIdList;
	private ArrayList<Integer> shipIdList;
	private ArrayList<UsersModel> billAddressList;
	private ArrayList<UsersModel> shipAddressList;
	private ArrayList<UsersModel> agentUserList = null;
	private ArrayList<UsersModel> locationList = null;
	private ArrayList<UsersModel> userList = new ArrayList<UsersModel>();
	private double price;
	private EventModel eventsList = new EventModel();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String keyword;
	private int assignedShipTo;
	private int bookedSeats;
	private String contactId;
	private int defaultBillToId;
	private int defaultShipToId;
	private int eventCost;
	private int loginCount;
	private int paginate;
	private int pId;
	private int PRODUCTID;
	private int resultCount;
	private int savedGroupId;
	private int taxId;
	private int totalSeats;
	private int urlHit;
	private String approverSequence;
	private String alwaysApprover;
	private String approveLimit;
	private int userId;
	private LinkedHashMap<String, LinkedHashMap<String, ArrayList<EventModel>>> eventsByCategory;
	private String acceptPo;
	private String accountNumber;
	private String accountNumberNCE;
	private String add_info;
	private String addCity;
	private String addCompany;
	private String addCountry;
	private String addEmail;
	private String addFax;
	private String addFirstName;
	private String addLastName;
	private String addPostal;
	private String address1;
	private String address2;
	private String addressBId[];
	private String addressBookId;
	private String addState;
	private String addStreetAddr;
	private String addStreetAddr1;
	private String addWorkPhone;
	private String approveSenderid;
	private String assignedNewId;
	private String authoListAssigned;
	private String AuthSet;
	private String billAddress1;
	private String billAddress2;
	private String billCity;
	private String billFax;
	private String billFirstName;
	private String billLastName;
	private String billPhone;
	private String billPoBox;
	private String billWebsite;
	private String buyingCompanyId;
	private String buyingLevelAutharization;
	private String calanderEvent;
	private String city;
	private String company;
	private String companyName;
	private String confirmEmail;
	private String confirmPass;
	private String contactAddress1;
	private String contactAddress2;
	private String contactCity;
	private String contactCountry;
	private String contactemailAddress;
	private String contactFax;
	private String contactFirstName;
	private String contactLastName;
	private String contactPhone;
	private String contactPoBox;
	private String contactSalutation;
	private String contactState;
	private String contactWebsite;
	private String contentPart;
	private String country;
	private String customerName;
	private String department;
	private String descPart;
	private String email;
	private String emailAddress;
	private String emailAPA;
	private String emailCartId;
	private String enId[];
	private String entityId;
	private String eventID;
	private String eventLoc;
	private String eventTitle;
	private String explainRequest;
	private String faxNumber;
	private String firstName;
	private String fromEmail;
	private String fromName;
	private String fullPageLayout;
	private String FUNCTION;
	private String HOOK_URL;
	private String howCanWeHelp;
	private String howToContact;
	private String ID;
	private String imgPart;
	private String isAuthPurchaseAgent;
	private String isSuperUser;
	private String itemPriceIdList[];
	private String jobShipToName;
	private String jobTitle;
	private String lastName;
	private String locState;
	private String locState1;
	private String newPassword;
	private String lState;
	private String lState1;
	private String mailBody;
	private String mailLink;
	private String mailSubject;
	private String metaDesc;
	private String metaKeywords;
	private String newsLetterReg;
	private String newsLetterSubscription;
	private String notesToAPA;
	private String ociSession;
	private String oldPassword;
	private String pageContent;
	private String pageName;
	private String pageNo = "0";
	private String pageTitle;
	private String parentID;
	private String password;
	private String paymentOption;
	private String phoneAPA;
	private String phoneNo;
	private String pricePart;
	private String primaryVertical;
	private String purchaseOrderNumber;
	private String registrantNote;
	private String res = "-1";
	private String result;
	private String resultPage = "0";
	private String roleAssign;
	private String savedGroupName;
	private String securityAnswer;
	private String securityQuestion;
	private String shipAddress1;
	private String shipAddress2;
	private String shipCity;
	private String shipCompany;
	private String shipCountry;
	private String shipemailAddress;
	private String shipFax;
	private String shipFirstName;
	private String shipLastName;
	private String shipPhone;
	private String shipPoBox;
	private String shipState;
	private String shipWebsite;
	private String shoppingCartId[];
	private String shoppingCartQty[];
	private String sl_user;
	private String sortBy = null;
	private ArrayList<Integer> idList;


	private String approverComment;
	private String state;
	private String title1;
	private String toEmail;
	private String toName;
	private String mailMessage;
	private String txtcontactAptSte;
	private String txtcontactBldgNo;
	private String txtcontactPOBox;
	private String txtcontactStreetName;
	private String type;
	private String useEntityAddress;
	private String userIdentificaion;
	private String username;
	private String userNameAPA;
	private String videoFileName;
	private String videoUrl;
	private String zip;
	private String phone;
	private UsersModel billAddress;
	private UsersModel shipAddress;
	private UsersModel addressList;
	private UsersModel locationDetail = null;
	private UsersModel userAccountDetail = null;
	protected String target = ERROR;
	private String registerType;
	private String isReOrder;
	private String quotePartNumber[];
	private String poNumber;
	private String shippingInstruction;
	private String orderNotes;
	private String reqDate;
	private String customerShipVia;
	private ArrayList<ShipVia> shipViaList;
	private String POValidStatus;
	private String locUser;
	private String confirmPassword;
	private String shipToId;
	private String shipToName;
	private String makeAsDefault;
	private String zipCode;
	private ArrayList<CreditCardModel> cardDetails;
	private String mailTemplateName;
	private String contactClassification;
	private ArrayList<ProductsModel> manufacturersList = null;
	private String selectedShipVia;
	private String selectedShipViaDesc;
	private String disableShip;
	private String disableShipHazmatOnly;
	private String selectedShipMethod;
	private String defaultShipVia;
	private String contactMobilePhone;
	private String newApaPrivacyAndTermsCheckBoxRequired;
	private String newApaPrivacyAndTermsCheckBox;
	private ArrayList<Integer> lineItemIdList;

	private String ARPasswordVerificationType;
	private String ARPassword;
	private String checkShipToId;
	private String itemAvailabilityStatus;
	private String assignedId;
	private String propertyKey;
	private String fromPageValue;
	private String sessionId;
	private String[] eventsLocationFilter;
	private String newsLetterContactUs;
	private String customFieldSubscriptions;
	private int statementMonth;
	private int statementYear;
	private String asOfDate;
	private static Map<Integer, String> statementMonthMap;
	private String jcaptcha;
	private ArrayList<EventModel> eventsCustomFieldList = null;
	public CreditCardModel creditCardValue = null; 
	private EventModel eventCustomFieldList = new EventModel();
	private String requestFrom;
	private String shippingOrgType;
	private String territoryManager;
	private String contactTitle;
	private ArrayList<String> paContactTitles = null;
	private String hidePrice;
	private String invoiceDetails;
	private String invoiceSummary;
	private int requestTokenId;
	
	
	public int getRequestTokenId() {
		return requestTokenId;
	}
	public void setApproverSequence(String approverSequence) {
		this.approverSequence = approverSequence;
	}
	public String getAlwaysApprover() {
		return alwaysApprover;
	}
	public void setAlwaysApprover(String alwaysApprover) {
		this.alwaysApprover = alwaysApprover;
	}
	public String getApproveLimit() {
		return approveLimit;
	}
	public void setApproveLimit(String approveLimit) {
		this.approveLimit = approveLimit;
	}
	
	public String getHidePrice() {
		return hidePrice;
	}
	public void setHidePrice(String hidePrice) {
		this.hidePrice = hidePrice;
	}
	public String getInvoiceDetails() {
		return invoiceDetails;
	}
	public void setInvoiceDetails(String invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}
	public String getInvoiceSummary() {
		return invoiceSummary;
	}
	public void setInvoiceSummary(String invoiceSummary) {
		this.invoiceSummary = invoiceSummary;
	}
	public ArrayList<String> getPaContactTitles() {
		return paContactTitles;
	}
	public void setPaContactTitles(ArrayList<String> paContactTitles) {
		this.paContactTitles = paContactTitles;
	}
	public String getContactTitle() {
		return contactTitle;
	}
	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

	static final Logger logger = Logger.getLogger(UsersAction.class);
	
	public String getTerritoryManager() {
		return territoryManager;
	}
	public void setTerritoryManager(String territoryManager) {
		this.territoryManager = territoryManager;
	}
	public CreditCardModel getCreditCardValue() {
		return creditCardValue;
	}
	public void setCreditCardValue(CreditCardModel creditCardValue) {
		this.creditCardValue = creditCardValue;
	}
	public String getCcNumber() {
		return ccNumber;
	}
	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}
	public String getCcExp() {
		return ccExp;
	}
	public void setCcExp(String ccExp) {
		this.ccExp = ccExp;
	}
	public String getCcType() {
		return ccType;
	}
	public void setCcType(String ccType) {
		this.ccType = ccType;
	}
	public String getCcTransactionId() {
		return ccTransactionId;
	}
	public void setCcTransactionId(String ccTransactionId) {
		this.ccTransactionId = ccTransactionId;
	}
	public String getCcMerchantId() {
		return ccMerchantId;
	}
	public void setCcMerchantId(String ccMerchantId) {
		this.ccMerchantId = ccMerchantId;
	}
	public String getCcListExists() {
		return ccListExists;
	}
	public void setCcListExists(String ccListExists) {
		this.ccListExists = ccListExists;
	}
	public String getCcResponseCode() {
		return ccResponseCode;
	}
	public void setCcResponseCode(String ccResponseCode) {
		this.ccResponseCode = ccResponseCode;
	}
	public String getCcStatus() {
		return ccStatus;
	}
	public void setCcStatus(String ccStatus) {
		this.ccStatus = ccStatus;
	}
	public String getCcHostRefNumber() {
		return ccHostRefNumber;
	}
	public void setCcHostRefNumber(String ccHostRefNumber) {
		this.ccHostRefNumber = ccHostRefNumber;
	}
	public String getCcTaskID() {
		return ccTaskID;
	}
	public void setCcTaskID(String ccTaskID) {
		this.ccTaskID = ccTaskID;
	}
	public String getCcAmount() {
		return ccAmount;
	}
	public void setCcAmount(String ccAmount) {
		this.ccAmount = ccAmount;
	}
	public String getCcDeclineResponseReason() {
		return ccDeclineResponseReason;
	}
	public void setCcDeclineResponseReason(String ccDeclineResponseReason) {
		this.ccDeclineResponseReason = ccDeclineResponseReason;
	}
	public String getCcCvv2VrfyCode() {
		return ccCvv2VrfyCode;
	}
	public void setCcCvv2VrfyCode(String ccCvv2VrfyCode) {
		this.ccCvv2VrfyCode = ccCvv2VrfyCode;
	}
	public String getCcTip() {
		return ccTip;
	}
	public void setCcTip(String ccTip) {
		this.ccTip = ccTip;
	}
	public String getCcTransTimeStamp() {
		return ccTransTimeStamp;
	}
	public void setCcTransTimeStamp(String ccTransTimeStamp) {
		this.ccTransTimeStamp = ccTransTimeStamp;
	}
	public String getCcToken() {
		return ccToken;
	}
	public void setCcToken(String ccToken) {
		this.ccToken = ccToken;
	}
	public String getCcApprovedAmount() {
		return ccApprovedAmount;
	}
	public void setCcApprovedAmount(String ccApprovedAmount) {
		this.ccApprovedAmount = ccApprovedAmount;
	}
	public String getCcRequestedAmount() {
		return ccRequestedAmount;
	}
	public void setCcRequestedAmount(String ccRequestedAmount) {
		this.ccRequestedAmount = ccRequestedAmount;
	}
	public String getCcHostResponseCode() {
		return ccHostResponseCode;
	}
	public void setCcHostResponseCode(String ccHostResponseCode) {
		this.ccHostResponseCode = ccHostResponseCode;
	}
	public String getCcRefrenceCode() {
		return ccRefrenceCode;
	}
	public void setCcRefrenceCode(String ccRefrenceCode) {
		this.ccRefrenceCode = ccRefrenceCode;
	}
	public String getCcInvoice() {
		return ccInvoice;
	}
	public void setCcInvoice(String ccInvoice) {
		this.ccInvoice = ccInvoice;
	}
	public String getCcApprovalCode() {
		return ccApprovalCode;
	}
	public void setCcApprovalCode(String ccApprovalCode) {
		this.ccApprovalCode = ccApprovalCode;
	}
	public String getCcServerTimestamp() {
		return ccServerTimestamp;
	}
	public void setCcServerTimestamp(String ccServerTimestamp) {
		this.ccServerTimestamp = ccServerTimestamp;
	}
	public String getCcFee() {
		return ccFee;
	}
	public void setCcFee(String ccFee) {
		this.ccFee = ccFee;
	}
	public String getCcExternalSessionID() {
		return ccExternalSessionID;
	}
	public void setCcExternalSessionID(String ccExternalSessionID) {
		this.ccExternalSessionID = ccExternalSessionID;
	}
	public String getCcAddVrfyCode() {
		return ccAddVrfyCode;
	}
	public void setCcAddVrfyCode(String ccAddVrfyCode) {
		this.ccAddVrfyCode = ccAddVrfyCode;
	}
	public String getCcTax() {
		return ccTax;
	}
	public void setCcTax(String ccTax) {
		this.ccTax = ccTax;
	}
	public String getCcNewDomainKey() {
		return ccNewDomainKey;
	}
	public void setCcNewDomainKey(String ccNewDomainKey) {
		this.ccNewDomainKey = ccNewDomainKey;
	}
	public String getCreditCardPayment() {
		return creditCardPayment;
	}
	public void setCreditCardPayment(String creditCardPayment) {
		this.creditCardPayment = creditCardPayment;
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
	public String getCardHolder() {
		return cardHolder;
	}
	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
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
	public String getSaveCard() {
		return saveCard;
	}
	public void setSaveCard(String saveCard) {
		this.saveCard = saveCard;
	}
	public String getCcAuthCode() {
		return ccAuthCode;
	}
	public void setCcAuthCode(String ccAuthCode) {
		this.ccAuthCode = ccAuthCode;
	}
	public String getOrderTax() {
		return orderTax;
	}
	public void setOrderTax(String orderTax) {
		this.orderTax = orderTax;
	}
	public String getEventPrice() {
		return eventPrice;
	}
	public void setEventPrice(String eventPrice) {
		this.eventPrice = eventPrice;
	}
	public String getParticipantCount() {
		return participantCount;
	}
	public void setParticipantCount(String participantCount) {
		this.participantCount = participantCount;
	}
	public String getOrderingType() {
		return orderingType;
	}
	public void setOrderingType(String orderingType) {
		this.orderingType = orderingType;
	}
	public String getEventPartNumber() {
		return eventPartNumber;
	}
	public void setEventPartNumber(String eventPartNumber) {
		this.eventPartNumber = eventPartNumber;
	}	
	
	private String discountCoupons;	
	private String ccNumber;
	private String ccExp;
	private String ccType;
	private String ccTransactionId;
	private String ccMerchantId;
	private String ccListExists;
	private String ccResponseCode;
	private String ccStatus;
	private String ccHostRefNumber;
	private String ccTaskID;
	private String ccAmount;
	private String ccDeclineResponseReason;
	private String ccCvv2VrfyCode;
	private String ccTip;
	private String ccTransTimeStamp;
	private String ccToken;
	private String ccApprovedAmount;
	private String ccRequestedAmount;
	private String ccHostResponseCode;
	private String ccRefrenceCode;
	private String ccInvoice;
	private String ccApprovalCode;
	private String ccServerTimestamp;
	private String ccFee;
	private String ccExternalSessionID;
	private String ccAddVrfyCode;
	private String ccTax;
	private String ccNewDomainKey;
	private String creditCardPayment;
	private String payPalToken;
	private String payPalPayerId;
	private String cardHolder;
	private String streetAddress;
	private String postalCode;
	private String actualOrderTotal;
	private String saveCard;
	private String ccAuthCode;
	private String orderTax;
	private String eventPrice;
	private String participantCount;
	private String orderingType;
	private String eventPartNumber;
	private String serachInvoiceByDate;
	private String startDate;
	private String dataValue;
	
	public String getApproverComment() {
		return approverComment;
	}
	public void setApproverComment(String approverComment) {
		this.approverComment = approverComment;
	}
	public ArrayList<Integer> getLineItemIdList() {
		return lineItemIdList;
	}
	public void setLineItemIdList(ArrayList<Integer> lineItemIdList) {
		this.lineItemIdList = lineItemIdList;
	}	
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	public String getSerachInvoiceByDate() {
		return serachInvoiceByDate;
	}
	public void setSerachInvoiceByDate(String serachInvoiceByDate) {
		this.serachInvoiceByDate = serachInvoiceByDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public static Map<Integer, String> getStatementMonthMap() {
		return statementMonthMap;
	}
	public static void setStatementMonthMap(Map<Integer, String> statementMonthMap) {
		UsersAction.statementMonthMap = statementMonthMap;
	}
	public String getDiscountCoupons() {
			return discountCoupons;
		}
	public void setDiscountCoupons(String discountCoupons) {
			this.discountCoupons = discountCoupons;
		}
	
	public String getRequestFrom() {
		return requestFrom;
	}

	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}

	public String getJcaptcha() {
		return jcaptcha;
	}

	public void setJcaptcha(String jcaptcha) {
		this.jcaptcha = jcaptcha;
	}

	public int getStatementMonth() {
		return statementMonth;
	}

	public void setStatementMonth(int statementMonth) {
		this.statementMonth = statementMonth;
	}

	public int getStatementYear() {
		return statementYear;
	}

	public void setStatementYear(int statementYear) {
		this.statementYear = statementYear;
	}

	public String getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}

	public String getNewsLetterContactUs() {
		return newsLetterContactUs;
	}

	public void setNewsLetterContactUs(String newsLetterContactUs) {
		this.newsLetterContactUs = newsLetterContactUs;
	}

	public String[] getEventsLocationFilter() {
		return eventsLocationFilter;
	}

	public void setEventsLocationFilter(String[] eventsLocationFilter) {
		this.eventsLocationFilter = eventsLocationFilter;
	}

	public String[] getEventsCategoryFilter() {
		return eventsCategoryFilter;
	}

	public void setEventsCategoryFilter(String[] eventsCategoryFilter) {
		this.eventsCategoryFilter = eventsCategoryFilter;
	}

	private String[] eventsCategoryFilter;

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getFromPageValue() {
		return fromPageValue;
	}

	public void setFromPageValue(String fromPageValue) {
		this.fromPageValue = fromPageValue;
	}

	public String getAssignedId() {
		return assignedId;
	}

	public void setAssignedId(String assignedId) {
		this.assignedId = assignedId;
	}

	public String getItemAvailabilityStatus() {
		return itemAvailabilityStatus;
	}

	public void setItemAvailabilityStatus(String itemAvailabilityStatus) {
		this.itemAvailabilityStatus = itemAvailabilityStatus;
	}

	public String getDisableShipHazmatOnly() {
		return disableShipHazmatOnly;
	}

	public void setDisableShipHazmatOnly(String disableShipHazmatOnly) {
		this.disableShipHazmatOnly = disableShipHazmatOnly;
	}

	public String getCheckShipToId() {
		return checkShipToId;
	}

	public void setCheckShipToId(String checkShipToId) {
		this.checkShipToId = checkShipToId;
	}

	public String getARPassword() {
		return ARPassword;
	}

	public void setARPassword(String aRPassword) {
		ARPassword = aRPassword;
	}

	public String getARPasswordVerificationType() {
		return ARPasswordVerificationType;
	}

	public void setARPasswordVerificationType(String aRPasswordVerificationType) {
		ARPasswordVerificationType = aRPasswordVerificationType;
	}

	public String getNewApaPrivacyAndTermsCheckBox() {
		return newApaPrivacyAndTermsCheckBox;
	}

	public void setNewApaPrivacyAndTermsCheckBox(String newApaPrivacyAndTermsCheckBox) {
		this.newApaPrivacyAndTermsCheckBox = newApaPrivacyAndTermsCheckBox;
	}

	public String getNewApaPrivacyAndTermsCheckBoxRequired() {
		return newApaPrivacyAndTermsCheckBoxRequired;
	}

	public void setNewApaPrivacyAndTermsCheckBoxRequired(String newApaPrivacyAndTermsCheckBoxRequired) {
		this.newApaPrivacyAndTermsCheckBoxRequired = newApaPrivacyAndTermsCheckBoxRequired;
	}

	public String getContactMobilePhone() {
		return contactMobilePhone;
	}

	public void setContactMobilePhone(String contactMobilePhone) {
		this.contactMobilePhone = contactMobilePhone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHowToContact() {
		return howToContact;
	}

	public void setHowToContact(String howToContact) {
		this.howToContact = howToContact;
	}

	public String getDefaultShipVia() {
		return defaultShipVia;
	}

	public void setDefaultShipVia(String defaultShipVia) {
		this.defaultShipVia = defaultShipVia;
	}

	public String getSelectedShipMethod() {
		return selectedShipMethod;
	}

	public void setSelectedShipMethod(String selectedShipMethod) {
		this.selectedShipMethod = selectedShipMethod;
	}

	public ArrayList<ProductsModel> getManufacturersList() {
		return manufacturersList;
	}

	public void setManufacturersList(ArrayList<ProductsModel> manufacturersList) {
		this.manufacturersList = manufacturersList;
	}

	public String getContactClassification() {
		return contactClassification;
	}

	public void setContactClassification(String contactClassification) {
		this.contactClassification = contactClassification;
	}

	public String getMailMessage() {
		return mailMessage;
	}

	public void setMailMessage(String mailMessage) {
		this.mailMessage = mailMessage;
	}

	public String getMailTemplateName() {
		return mailTemplateName;
	}

	public void setMailTemplateName(String mailTemplateName) {
		this.mailTemplateName = mailTemplateName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	ArrayList<ProductsModel> resultData;

	public ArrayList<ProductsModel> getResultData() {
		return resultData;
	}

	public void setResultData(ArrayList<ProductsModel> resultData) {
		this.resultData = resultData;
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	public String getShippingInstruction() {
		return shippingInstruction;
	}

	public void setShippingInstruction(String shippingInstruction) {
		this.shippingInstruction = shippingInstruction;
	}

	public String getOrderNotes() {
		return orderNotes;
	}

	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}

	public String getReqDate() {
		return reqDate;
	}

	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}

	public String getMakeAsDefault() {
		return makeAsDefault;
	}

	public void setMakeAsDefault(String makeAsDefault) {
		this.makeAsDefault = makeAsDefault;
	}

	public String getShipToId() {
		return shipToId;
	}

	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}

	public ArrayList<MenuAndBannersModal> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(ArrayList<MenuAndBannersModal> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getLocUser() {
		return locUser;
	}

	public void setLocUser(String locUser) {
		this.locUser = locUser;
	}

	public String getRenderContent() {
		return renderContent;
	}

	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}

	public String getPOValidStatus() {
		return POValidStatus;
	}

	public void setPOValidStatus(String pOValidStatus) {
		POValidStatus = pOValidStatus;
	}

	public String getCustomerShipVia() {
		return customerShipVia;
	}

	public ArrayList<ShipVia> getShipViaList() {
		return shipViaList;
	}

	public void setShipViaList(ArrayList<ShipVia> shipViaList) {
		this.shipViaList = shipViaList;
	}

	public void setCustomerShipVia(String customerShipVia) {
		this.customerShipVia = customerShipVia;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String[] getQuotePartNumber() {
		return quotePartNumber;
	}

	public void setQuotePartNumber(String[] quotePartNumber) {
		this.quotePartNumber = quotePartNumber;
	}

	public String getIsReOrder() {
		return isReOrder;
	}

	public void setIsReOrder(String isReOrder) {
		this.isReOrder = isReOrder;
	}

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	public ArrayList<String> getShipVia() {
		return shipVia;
	}

	public void setShipVia(ArrayList<String> shipVia) {
		this.shipVia = shipVia;
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

	public ArrayList<MenuAndBannersModal> getTopBanners() {
		return topBanners;
	}

	public void setTopBanners(ArrayList<MenuAndBannersModal> topBanners) {
		this.topBanners = topBanners;
	}

	public ArrayList<EventModel> getEventCatData() {
		return eventCatData;
	}

	public void setEventCatData(ArrayList<EventModel> eventCatData) {
		this.eventCatData = eventCatData;
	}

	public ArrayList<EventModel> getEventCatList() {
		return eventCatList;
	}

	public void setEventCatList(ArrayList<EventModel> eventCatList) {
		this.eventCatList = eventCatList;
	}

	public ArrayList<EventModel> getEventsType() {
		return eventsType;
	}

	public void setEventsType(ArrayList<EventModel> eventsType) {
		this.eventsType = eventsType;
	}

	public ArrayList<Integer> getAssignedShipIdList() {
		return assignedShipIdList;
	}

	public void setAssignedShipIdList(ArrayList<Integer> assignedShipIdList) {
		this.assignedShipIdList = assignedShipIdList;
	}

	public ArrayList<Integer> getShipIdList() {
		return shipIdList;
	}

	public void setShipIdList(ArrayList<Integer> shipIdList) {
		this.shipIdList = shipIdList;
	}

	public ArrayList<UsersModel> getBillAddressList() {
		return billAddressList;
	}

	public void setBillAddressList(ArrayList<UsersModel> billAddressList) {
		this.billAddressList = billAddressList;
	}

	public ArrayList<UsersModel> getShipAddressList() {
		return shipAddressList;
	}

	public void setShipAddressList(ArrayList<UsersModel> shipAddressList) {
		this.shipAddressList = shipAddressList;
	}

	public ArrayList<UsersModel> getAgentUserList() {
		return agentUserList;
	}

	public void setAgentUserList(ArrayList<UsersModel> agentUserList) {
		this.agentUserList = agentUserList;
	}

	public ArrayList<UsersModel> getLocationList() {
		return locationList;
	}

	public void setLocationList(ArrayList<UsersModel> locationList) {
		this.locationList = locationList;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public EventModel getEventsList() {
		return eventsList;
	}

	public void setEventsList(EventModel eventsList) {
		this.eventsList = eventsList;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public int getAssignedShipTo() {
		return assignedShipTo;
	}

	public void setAssignedShipTo(int assignedShipTo) {
		this.assignedShipTo = assignedShipTo;
	}

	public int getBookedSeats() {
		return bookedSeats;
	}

	public void setBookedSeats(int bookedSeats) {
		this.bookedSeats = bookedSeats;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
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

	public int getEventCost() {
		return eventCost;
	}

	public void setEventCost(int eventCost) {
		this.eventCost = eventCost;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public int getPaginate() {
		return paginate;
	}

	public void setPaginate(int paginate) {
		this.paginate = paginate;
	}

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public int getPRODUCTID() {
		return PRODUCTID;
	}

	public void setPRODUCTID(int pRODUCTID) {
		PRODUCTID = pRODUCTID;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

	public int getSavedGroupId() {
		return savedGroupId;
	}

	public void setSavedGroupId(int savedGroupId) {
		this.savedGroupId = savedGroupId;
	}

	public int getTaxId() {
		return taxId;
	}

	public void setTaxId(int taxId) {
		this.taxId = taxId;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getUrlHit() {
		return urlHit;
	}

	public void setUrlHit(int urlHit) {
		this.urlHit = urlHit;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public LinkedHashMap<String, LinkedHashMap<String, ArrayList<EventModel>>> getEventsByCategory() {
		return eventsByCategory;
	}

	public void setEventsByCategory(
			LinkedHashMap<String, LinkedHashMap<String, ArrayList<EventModel>>> eventsByCategory) {
		this.eventsByCategory = eventsByCategory;
	}

	public String getAcceptPo() {
		return acceptPo;
	}

	public void setAcceptPo(String acceptPo) {
		this.acceptPo = acceptPo;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountNumberNCE() {
		return accountNumberNCE;
	}

	public void setAccountNumberNCE(String accountNumberNCE) {
		this.accountNumberNCE = accountNumberNCE;
	}

	public String getAdd_info() {
		return add_info;
	}

	public void setAdd_info(String add_info) {
		this.add_info = add_info;
	}

	public String getAddCity() {
		return addCity;
	}

	public void setAddCity(String addCity) {
		this.addCity = addCity;
	}

	public String getAddCompany() {
		return addCompany;
	}

	public void setAddCompany(String addCompany) {
		this.addCompany = addCompany;
	}

	public String getAddCountry() {
		return addCountry;
	}

	public void setAddCountry(String addCountry) {
		this.addCountry = addCountry;
	}

	public String getAddEmail() {
		return addEmail;
	}

	public void setAddEmail(String addEmail) {
		this.addEmail = addEmail;
	}

	public String getAddFax() {
		return addFax;
	}

	public void setAddFax(String addFax) {
		this.addFax = addFax;
	}

	public String getAddFirstName() {
		return addFirstName;
	}

	public void setAddFirstName(String addFirstName) {
		this.addFirstName = addFirstName;
	}

	public String getAddLastName() {
		return addLastName;
	}

	public void setAddLastName(String addLastName) {
		this.addLastName = addLastName;
	}

	public String getAddPostal() {
		return addPostal;
	}

	public void setAddPostal(String addPostal) {
		this.addPostal = addPostal;
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

	public String[] getAddressBId() {
		return addressBId;
	}

	public void setAddressBId(String[] addressBId) {
		this.addressBId = addressBId;
	}

	public String getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(String addressBookId) {
		this.addressBookId = addressBookId;
	}

	public String getAddState() {
		return addState;
	}

	public void setAddState(String addState) {
		this.addState = addState;
	}

	public String getAddStreetAddr() {
		return addStreetAddr;
	}

	public void setAddStreetAddr(String addStreetAddr) {
		this.addStreetAddr = addStreetAddr;
	}

	public String getAddStreetAddr1() {
		return addStreetAddr1;
	}

	public void setAddStreetAddr1(String addStreetAddr1) {
		this.addStreetAddr1 = addStreetAddr1;
	}

	public String getAddWorkPhone() {
		return addWorkPhone;
	}

	public void setAddWorkPhone(String addWorkPhone) {
		this.addWorkPhone = addWorkPhone;
	}

	public String getApproveSenderid() {
		return approveSenderid;
	}

	public void setApproveSenderid(String approveSenderid) {
		this.approveSenderid = approveSenderid;
	}

	public String getAssignedNewId() {
		return assignedNewId;
	}

	public void setAssignedNewId(String assignedNewId) {
		this.assignedNewId = assignedNewId;
	}

	public String getAuthoListAssigned() {
		return authoListAssigned;
	}

	public void setAuthoListAssigned(String authoListAssigned) {
		this.authoListAssigned = authoListAssigned;
	}

	public String getAuthSet() {
		return AuthSet;
	}

	public void setAuthSet(String authSet) {
		AuthSet = authSet;
	}

	public String getBillAddress1() {
		return billAddress1;
	}

	public void setBillAddress1(String billAddress1) {
		this.billAddress1 = billAddress1;
	}

	public String getBillAddress2() {
		return billAddress2;
	}

	public void setBillAddress2(String billAddress2) {
		this.billAddress2 = billAddress2;
	}

	public String getBillCity() {
		return billCity;
	}

	public void setBillCity(String billCity) {
		this.billCity = billCity;
	}

	public String getBillFax() {
		return billFax;
	}

	public void setBillFax(String billFax) {
		this.billFax = billFax;
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

	public String getBillPhone() {
		return billPhone;
	}

	public void setBillPhone(String billPhone) {
		this.billPhone = billPhone;
	}

	public String getBillPoBox() {
		return billPoBox;
	}

	public void setBillPoBox(String billPoBox) {
		this.billPoBox = billPoBox;
	}

	public String getBillWebsite() {
		return billWebsite;
	}

	public void setBillWebsite(String billWebsite) {
		this.billWebsite = billWebsite;
	}

	public String getBuyingCompanyId() {
		return buyingCompanyId;
	}

	public void setBuyingCompanyId(String buyingCompanyId) {
		this.buyingCompanyId = buyingCompanyId;
	}

	public String getBuyingLevelAutharization() {
		return buyingLevelAutharization;
	}

	public void setBuyingLevelAutharization(String buyingLevelAutharization) {
		this.buyingLevelAutharization = buyingLevelAutharization;
	}

	public String getCalanderEvent() {
		return calanderEvent;
	}

	public void setCalanderEvent(String calanderEvent) {
		this.calanderEvent = calanderEvent;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	public String getConfirmPass() {
		return confirmPass;
	}

	public void setConfirmPass(String confirmPass) {
		this.confirmPass = confirmPass;
	}

	public String getContactAddress1() {
		return contactAddress1;
	}

	public void setContactAddress1(String contactAddress1) {
		this.contactAddress1 = contactAddress1;
	}

	public String getContactAddress2() {
		return contactAddress2;
	}

	public void setContactAddress2(String contactAddress2) {
		this.contactAddress2 = contactAddress2;
	}

	public String getContactCity() {
		return contactCity;
	}

	public void setContactCity(String contactCity) {
		this.contactCity = contactCity;
	}

	public String getContactCountry() {
		return contactCountry;
	}

	public void setContactCountry(String contactCountry) {
		this.contactCountry = contactCountry;
	}

	public String getContactemailAddress() {
		return contactemailAddress;
	}

	public void setContactemailAddress(String contactemailAddress) {
		this.contactemailAddress = contactemailAddress;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getContactFirstName() {
		return contactFirstName;
	}

	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}

	public String getContactLastName() {
		return contactLastName;
	}

	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactPoBox() {
		return contactPoBox;
	}

	public void setContactPoBox(String contactPoBox) {
		this.contactPoBox = contactPoBox;
	}

	public String getContactSalutation() {
		return contactSalutation;
	}

	public void setContactSalutation(String contactSalutation) {
		this.contactSalutation = contactSalutation;
	}

	public String getContactState() {
		return contactState;
	}

	public void setContactState(String contactState) {
		this.contactState = contactState;
	}

	public String getContactWebsite() {
		return contactWebsite;
	}

	public void setContactWebsite(String contactWebsite) {
		this.contactWebsite = contactWebsite;
	}

	public String getContentPart() {
		return contentPart;
	}

	public void setContentPart(String contentPart) {
		this.contentPart = contentPart;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDescPart() {
		return descPart;
	}

	public void setDescPart(String descPart) {
		this.descPart = descPart;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmailAPA() {
		return emailAPA;
	}

	public void setEmailAPA(String emailAPA) {
		this.emailAPA = emailAPA;
	}

	public String getEmailCartId() {
		return emailCartId;
	}

	public void setEmailCartId(String emailCartId) {
		this.emailCartId = emailCartId;
	}

	public String[] getEnId() {
		return enId;
	}

	public void setEnId(String[] enId) {
		this.enId = enId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getEventLoc() {
		return eventLoc;
	}

	public void setEventLoc(String eventLoc) {
		this.eventLoc = eventLoc;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getExplainRequest() {
		return explainRequest;
	}

	public void setExplainRequest(String explainRequest) {
		this.explainRequest = explainRequest;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFullPageLayout() {
		return fullPageLayout;
	}

	public void setFullPageLayout(String fullPageLayout) {
		this.fullPageLayout = fullPageLayout;
	}

	public String getFUNCTION() {
		return FUNCTION;
	}

	public void setFUNCTION(String fUNCTION) {
		FUNCTION = fUNCTION;
	}

	public String getHOOK_URL() {
		return HOOK_URL;
	}

	public void setHOOK_URL(String hOOK_URL) {
		HOOK_URL = hOOK_URL;
	}

	public String getHowCanWeHelp() {
		return howCanWeHelp;
	}

	public void setHowCanWeHelp(String howCanWeHelp) {
		this.howCanWeHelp = howCanWeHelp;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getImgPart() {
		return imgPart;
	}

	public void setImgPart(String imgPart) {
		this.imgPart = imgPart;
	}

	public String getIsAuthPurchaseAgent() {
		return isAuthPurchaseAgent;
	}

	public void setIsAuthPurchaseAgent(String isAuthPurchaseAgent) {
		this.isAuthPurchaseAgent = isAuthPurchaseAgent;
	}

	public String getIsSuperUser() {
		return isSuperUser;
	}

	public void setIsSuperUser(String isSuperUser) {
		this.isSuperUser = isSuperUser;
	}

	public String[] getItemPriceIdList() {
		return itemPriceIdList;
	}

	public void setItemPriceIdList(String[] itemPriceIdList) {
		this.itemPriceIdList = itemPriceIdList;
	}

	public String getJobShipToName() {
		return jobShipToName;
	}

	public void setJobShipToName(String jobShipToName) {
		this.jobShipToName = jobShipToName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLocState() {
		return locState;
	}

	public void setLocState(String locState) {
		this.locState = locState;
	}

	public String getLocState1() {
		return locState1;
	}

	public void setLocState1(String locState1) {
		this.locState1 = locState1;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getlState() {
		return lState;
	}

	public void setlState(String lState) {
		this.lState = lState;
	}

	public String getlState1() {
		return lState1;
	}

	public void setlState1(String lState1) {
		this.lState1 = lState1;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public String getMailLink() {
		return mailLink;
	}

	public void setMailLink(String mailLink) {
		this.mailLink = mailLink;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMetaDesc() {
		return metaDesc;
	}

	public void setMetaDesc(String metaDesc) {
		this.metaDesc = metaDesc;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public String getNewsLetterReg() {
		return newsLetterReg;
	}

	public void setNewsLetterReg(String newsLetterReg) {
		this.newsLetterReg = newsLetterReg;
	}

	public String getNewsLetterSubscription() {
		return newsLetterSubscription;
	}

	public void setNewsLetterSubscription(String newsLetterSubscription) {
		this.newsLetterSubscription = newsLetterSubscription;
	}

	public String getNotesToAPA() {
		return notesToAPA;
	}

	public void setNotesToAPA(String notesToAPA) {
		this.notesToAPA = notesToAPA;
	}

	public String getOciSession() {
		return ociSession;
	}

	public void setOciSession(String ociSession) {
		this.ociSession = ociSession;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPaymentOption() {
		return paymentOption;
	}

	public void setPaymentOption(String paymentOption) {
		this.paymentOption = paymentOption;
	}

	public String getPhoneAPA() {
		return phoneAPA;
	}

	public void setPhoneAPA(String phoneAPA) {
		this.phoneAPA = phoneAPA;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPricePart() {
		return pricePart;
	}

	public void setPricePart(String pricePart) {
		this.pricePart = pricePart;
	}

	public String getPrimaryVertical() {
		return primaryVertical;
	}

	public void setPrimaryVertical(String primaryVertical) {
		this.primaryVertical = primaryVertical;
	}

	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	public String getRegistrantNote() {
		return registrantNote;
	}

	public void setRegistrantNote(String registrantNote) {
		this.registrantNote = registrantNote;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultPage() {
		return resultPage;
	}

	public void setResultPage(String resultPage) {
		this.resultPage = resultPage;
	}

	public String getRoleAssign() {
		return roleAssign;
	}

	public void setRoleAssign(String roleAssign) {
		this.roleAssign = roleAssign;
	}

	public String getSavedGroupName() {
		return savedGroupName;
	}

	public void setSavedGroupName(String savedGroupName) {
		this.savedGroupName = savedGroupName;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getShipAddress1() {
		return shipAddress1;
	}

	public void setShipAddress1(String shipAddress1) {
		this.shipAddress1 = shipAddress1;
	}

	public String getShipAddress2() {
		return shipAddress2;
	}

	public void setShipAddress2(String shipAddress2) {
		this.shipAddress2 = shipAddress2;
	}

	public String getShipCity() {
		return shipCity;
	}

	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	public String getShipCompany() {
		return shipCompany;
	}

	public void setShipCompany(String shipCompany) {
		this.shipCompany = shipCompany;
	}

	public String getShipCountry() {
		return shipCountry;
	}

	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}

	public String getShipemailAddress() {
		return shipemailAddress;
	}

	public void setShipemailAddress(String shipemailAddress) {
		this.shipemailAddress = shipemailAddress;
	}

	public String getShipFax() {
		return shipFax;
	}

	public void setShipFax(String shipFax) {
		this.shipFax = shipFax;
	}

	public String getShipFirstName() {
		return shipFirstName;
	}

	public void setShipFirstName(String shipFirstName) {
		this.shipFirstName = shipFirstName;
	}

	public String getShipLastName() {
		return shipLastName;
	}

	public void setShipLastName(String shipLastName) {
		this.shipLastName = shipLastName;
	}

	public String getShipPhone() {
		return shipPhone;
	}

	public void setShipPhone(String shipPhone) {
		this.shipPhone = shipPhone;
	}

	public String getShipPoBox() {
		return shipPoBox;
	}

	public void setShipPoBox(String shipPoBox) {
		this.shipPoBox = shipPoBox;
	}

	public String getShipState() {
		return shipState;
	}

	public void setShipState(String shipState) {
		this.shipState = shipState;
	}

	public String getShipWebsite() {
		return shipWebsite;
	}

	public void setShipWebsite(String shipWebsite) {
		this.shipWebsite = shipWebsite;
	}

	public String[] getShoppingCartId() {
		return shoppingCartId;
	}

	public void setShoppingCartId(String[] shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	public String[] getShoppingCartQty() {
		return shoppingCartQty;
	}

	public void setShoppingCartQty(String[] shoppingCartQty) {
		this.shoppingCartQty = shoppingCartQty;
	}

	public String getSl_user() {
		return sl_user;
	}

	public void setSl_user(String sl_user) {
		this.sl_user = sl_user;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getTxtcontactAptSte() {
		return txtcontactAptSte;
	}

	public void setTxtcontactAptSte(String txtcontactAptSte) {
		this.txtcontactAptSte = txtcontactAptSte;
	}

	public String getTxtcontactBldgNo() {
		return txtcontactBldgNo;
	}

	public void setTxtcontactBldgNo(String txtcontactBldgNo) {
		this.txtcontactBldgNo = txtcontactBldgNo;
	}

	public String getTxtcontactPOBox() {
		return txtcontactPOBox;
	}

	public void setTxtcontactPOBox(String txtcontactPOBox) {
		this.txtcontactPOBox = txtcontactPOBox;
	}

	public String getTxtcontactStreetName() {
		return txtcontactStreetName;
	}

	public void setTxtcontactStreetName(String txtcontactStreetName) {
		this.txtcontactStreetName = txtcontactStreetName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUseEntityAddress() {
		return useEntityAddress;
	}

	public void setUseEntityAddress(String useEntityAddress) {
		this.useEntityAddress = useEntityAddress;
	}

	public String getUserIdentificaion() {
		return userIdentificaion;
	}

	public void setUserIdentificaion(String userIdentificaion) {
		this.userIdentificaion = userIdentificaion;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserNameAPA() {
		return userNameAPA;
	}

	public void setUserNameAPA(String userNameAPA) {
		this.userNameAPA = userNameAPA;
	}

	public String getVideoFileName() {
		return videoFileName;
	}

	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public UsersModel getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(UsersModel billAddress) {
		this.billAddress = billAddress;
	}

	public UsersModel getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(UsersModel shipAddress) {
		this.shipAddress = shipAddress;
	}

	public UsersModel getAddressList() {
		return addressList;
	}

	public void setAddressList(UsersModel addressList) {
		this.addressList = addressList;
	}

	public UsersModel getLocationDetail() {
		return locationDetail;
	}

	public void setLocationDetail(UsersModel locationDetail) {
		this.locationDetail = locationDetail;
	}

	public UsersModel getUserAccountDetail() {
		return userAccountDetail;
	}

	public void setUserAccountDetail(UsersModel userAccountDetail) {
		this.userAccountDetail = userAccountDetail;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public ArrayList<UsersModel> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<UsersModel> userList) {
		this.userList = userList;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setCardDetails(ArrayList<CreditCardModel> cardDetails) {
		this.cardDetails = cardDetails;
	}

	public ArrayList<CreditCardModel> getCardDetails() {
		return cardDetails;
	}

	public void setSelectedShipVia(String selectedShipVia) {
		this.selectedShipVia = selectedShipVia;
	}

	public String getSelectedShipVia() {
		return selectedShipVia;
	}

	public void setSelectedShipViaDesc(String selectedShipViaDesc) {
		this.selectedShipViaDesc = selectedShipViaDesc;
	}

	public String getSelectedShipViaDesc() {
		return selectedShipViaDesc;
	}

	public void setDisableShip(String disableShip) {
		this.disableShip = disableShip;
	}

	public String getDisableShip() {
		return disableShip;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCustomFieldSubscriptions() {
		return customFieldSubscriptions;
	}

	public void setCustomFieldSubscriptions(String customFieldSubscriptions) {
		this.customFieldSubscriptions = customFieldSubscriptions;
	}

	public String getShipToName() {
		return shipToName;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}

	public String changeLanguage() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			if (registerType != null && !registerType.trim().equalsIgnoreCase("")) {
				String[] arrVal = registerType.split("_");
				if (arrVal.length > 1) {
					if (arrVal[1] != null && arrVal[1].trim().equalsIgnoreCase("EN")) {
						session.setAttribute("localeId", arrVal[0]);
						session.setAttribute("localeCode", arrVal[1]);
					} else {
						session.setAttribute("localeId", arrVal[0]);
						session.setAttribute("localeCode", arrVal[1]);
					}

				}
			}

			String sessionLocale = (String) session.getAttribute("localeId");
			if (session.getAttribute("localeCode") != null
					&& session.getAttribute("localeCode").toString().trim().length() > 0) {
				sessionLocale = sessionLocale + "_" + (String) session.getAttribute("localeCode");
			}
			session.setAttribute("sessionLocale", sessionLocale);
			/*
			 * LinkedHashMap<String, String> contentObject = new LinkedHashMap<String,
			 * String>(); contentObject.put("localeId", (String)
			 * session.getAttribute("localeId")); contentObject.put("localeCode", (String)
			 * session.getAttribute("localeCode")); contentObject.put("sessionLocale",
			 * sessionLocale);
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ResultLoader";
	}

	public String getShippingInfoAddressSync() {
		UserFacadeService userFacadeService;
		target = SUCCESS;
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();

			String fromPage = CommonUtility.validateString(request.getParameter("frPage"));
			if(CommonUtility.validateString((String) session.getAttribute("frPage")).length()>0){
				fromPage = CommonUtility.validateString((String) session.getAttribute("frPage"));
			}
			String ipaddress = request.getHeader("X-Forwarded-For");
			if (ipaddress  == null){
				ipaddress = request.getRemoteAddr();	
			}

			ArrayList<UsersModel> accountList = new ArrayList<UsersModel>();
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SINGLE_USER_UNDER_MULTIPLE_CUSTOMER")).equalsIgnoreCase("Y") && request.getParameterMap().containsKey("checkAccount")){
				accountList =  UsersDAO.getUserAccountList((String) session.getAttribute(Global.USERNAME_KEY));
			}
			if(request.getParameterMap().containsKey("checkAccount") && accountList.size() > 1){
				String requestType = request.getParameter("checkAccount");
				session.setAttribute("multipleAccountsAvailable", "Y");
				if(CommonUtility.validateString(requestType).length()  >0 ){
					LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
					contentObject.put("accountList",accountList);
					contentObject.put("totalAccount",accountList.size());
					contentObject.put("frPage",request.getParameter("frPage"));
					renderContent = LayoutGenerator.templateLoader("ShipEntityPage", contentObject  , null, null, null);
				}
				return target;
			}else{
				if(request.getParameterMap().containsKey("requestType")){
					String requestType = request.getParameter("requestType");
					if(CommonUtility.validateString(requestType).length() > 0 && requestType.equals("MultipleAccounts")){
						UsersModel userModel = new UsersDAO().getUserStatus((String) session.getAttribute(Global.USERNAME_KEY), false);
						session.setAttribute("selectedUserId", CommonUtility.validateParseIntegerToString(userModel.getUserId()));
						SecureData validUserPass = new SecureData();
		                new LoginAuthentication().authenticate(userModel.getUserName(), validUserPass.validatePassword(userModel.getPassword()), session);
						UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Login", session.getId(), ipaddress, "Login", "NA");
					}
				}
	
			String sSessionUserId = (String) session.getAttribute(Global.USERID_KEY);

			String erp = (String) session.getAttribute("erpType");

			ErpType erpType = CimmUtil.getErpType(erp);

			List<BuyingCompanyAddressBook> buyingCompanyAddressBookList = null;

			UserManagement usersObj = new UserManagementImpl();

			buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			String sSubsetId = (String) session.getAttribute("userSubsetId");
			String sGeneralCatalog = (String) session.getAttribute("generalCatalog");
			String iCustomerId = (String) session.getAttribute("customerId");
			int iSessionUserId = CommonUtility.validateNumber(sSessionUserId);
			int iBuyingCompanyId = CommonUtility.validateNumber(buyingCompanyId);
			String sWareHouseCode = (String) session.getAttribute("wareHouseCode");
			int iWareHouseCodeId = CommonUtility.validateNumber((String) session.getAttribute("wareHouseCodeID"));
			if (CommonUtility.validateNumber(sGeneralCatalog) > 0) {
				sGeneralCatalog = "Y";
			} else {
				sGeneralCatalog = "N";
			}
			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_LOGIN_MESSAGE")).equalsIgnoreCase("Y")) {
				if (session.getAttribute("defaultBillToId") != null && session.getAttribute("defaultBillToId").toString().length() > 0) {
					defaultBillToId = CommonUtility.validateNumber((String) session.getAttribute("defaultBillToId"));
				}

				if (defaultBillToId == 0) {
					HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook((String) session.getAttribute(Global.USERNAME_KEY));
					// UsersDAO.getDefaultAddressId(userId);
					defaultBillToId = userAddressId.get("Bill");
					// defaultShipToId = userAddressId.get("Ship");
				}

				HashMap<String, UsersModel> userAddress = usersObj.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
				// UsersDAO.getUserAddress(userId, defaultBillToId, defaultShipToId);
				billAddress = userAddress.get("Bill");
			}
			if (fromPage != null && !(fromPage.trim().equalsIgnoreCase("ajaxLoad") || fromPage.equalsIgnoreCase("2"))) {

				userFacadeService = UserFacadeService.getInstance();

				if (userFacadeService.getExecutorService() == null) {
					// ExecutorService is created in the CimmExecutorContextListener
					ExecutorService executorService = (ExecutorService) ServletActionContext.getServletContext().getAttribute("CIMM_EXECUTOR");
					userFacadeService.setExecutorService(executorService);
				}

				Map<String, Object> addessSyncParameters = new HashMap<String, Object>();
				addessSyncParameters.put(UserFacadeService.MAP_KEY_USER_ID, iSessionUserId);
				addessSyncParameters.put(UserFacadeService.MAP_KEY_CUSTOMER_ID, iCustomerId);
				addessSyncParameters.put(UserFacadeService.MAP_KEY_BUYING_COMPANY_ID, iBuyingCompanyId);
				addessSyncParameters.put(UserFacadeService.MAP_KEY_GENERAL_CATALOG_ACCESS, sGeneralCatalog);
				addessSyncParameters.put(UserFacadeService.MAP_KEY_SUBSET_ID, sSubsetId);
				addessSyncParameters.put(UserFacadeService.MAP_KEY_WAREHOUSECODE, sWareHouseCode);
				addessSyncParameters.put(UserFacadeService.MAP_KEY_WAREHOUSECODEID, iWareHouseCodeId);
				String childBuyingCompanyCreation = CommonDBQuery.getSystemParamtersList().get("ADDRESS_SYNC_CHILD_BC_CREATION_FLAG");
				String singleShipTo = CommonDBQuery.getSystemParamtersList().get("ADDRESS_SYNC_SINGLE_SHIP_TO");
				boolean bChildBuyingCompanyCreation = CommonUtility.convertToBoolean(childBuyingCompanyCreation);
				addessSyncParameters.put(UserFacadeService.MAP_KEY_CHILD_BUYING_COMPANY_CREATION, bChildBuyingCompanyCreation);
				boolean bSingleShipTo = CommonUtility.convertToBoolean(singleShipTo);
				addessSyncParameters.put(UserFacadeService.MAP_KEY_SINGLE_SHIP_TO, bSingleShipTo);

				buyingCompanyAddressBookList = userFacadeService.getShipToAddressList(addessSyncParameters, erpType);

				shipAddressList = new ArrayList<UsersModel>();
				UsersModel userModel = null;
				for (BuyingCompanyAddressBook buyingCompanyAddressBook : buyingCompanyAddressBookList) {
					userModel = new UsersModel();
					if (!CommonUtility.validateString(buyingCompanyAddressBook.getShipToName()).isEmpty()) {
						userModel.setCustomerName(buyingCompanyAddressBook.getShipToName());
					} else if (!CommonUtility.validateString(buyingCompanyAddressBook.getFirstName()).isEmpty()) {
						userModel.setCustomerName(buyingCompanyAddressBook.getFirstName());
					}
					userModel.setFirstName(buyingCompanyAddressBook.getFirstName());
					userModel.setLastName(buyingCompanyAddressBook.getLastName()); 
					userModel.setCustomerId(iCustomerId);
					userModel.setBillEntityId(buyingCompanyAddressBook.getEntityId());
					userModel.setAddress1(buyingCompanyAddressBook.getAddress1());
					userModel.setAddress2(buyingCompanyAddressBook.getAddress2());
					userModel.setCity(buyingCompanyAddressBook.getCity());
					userModel.setState(buyingCompanyAddressBook.getState());
					userModel.setCountry(buyingCompanyAddressBook.getCountry());
					userModel.setZipCode(buyingCompanyAddressBook.getZipcode());
					userModel.setZipCodeStringFormat(buyingCompanyAddressBook.getZipcode());
					userModel.setPhoneNo(buyingCompanyAddressBook.getPhone());
					userModel.setShipToId(buyingCompanyAddressBook.getShipToId());
					userModel.setEntityId(buyingCompanyAddressBook.getEntityId());
					userModel.setWareHouseCodeStr(buyingCompanyAddressBook.getWareHouseCodeStr());
					if (buyingCompanyAddressBook.getAddressBookId() != 0) {
						userModel.setAddressBookId(buyingCompanyAddressBook.getAddressBookId());
					}

					if (buyingCompanyAddressBook.getBuyingCompanyId() != 0) {
						userModel.setBuyingCompanyId(buyingCompanyAddressBook.getBuyingCompanyId());
					}
					userModel.setCompanyName(buyingCompanyAddressBook.getCompanyName());
					shipAddressList.add(userModel);
					session.setAttribute("SHIP_ADDRESS_LIST", shipAddressList);
				}
			} else {
				shipAddressList = (ArrayList<UsersModel>) session.getAttribute("SHIP_ADDRESS_LIST");
			}

			if (shipAddressList != null && shipAddressList.size() > 1) {
				session.setAttribute("multipleShipAddressAvailable", "Y");
			} else {
				session.setAttribute("multipleShipAddressAvailable", "N");
			}

			String sTempShipToId = (String) session.getAttribute("defaultShipToId");

			if (sTempShipToId != null && !sTempShipToId.isEmpty()) {
				defaultShipToId = CommonUtility.validateNumber(sTempShipToId.trim());
			}

			String sEntityId = (String) session.getAttribute("entityId");

			if (sEntityId != null && !sEntityId.isEmpty()) {
				entityId = sEntityId;
			}

			String checkEmailId = CommonDBQuery.getSystemParamtersList().get("CHECK_EMAIL_ID");
			checkEmailId = CommonUtility.validateString(checkEmailId);
			if (checkEmailId.equalsIgnoreCase("Y")) {
				checkEmailAddress(iSessionUserId);
			}
			String emailAddressExits = CommonUtility.validateString((String) session.getAttribute("emailAddressExits"));
			if (emailAddressExits.equalsIgnoreCase("N")) {
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("defaultShipToId", defaultShipToId);
				contentObject.put("entityId", entityId);
				contentObject.put("shipEntity", shipAddressList);
				contentObject.put("validEmailAddress", false);

				renderContent = LayoutGenerator.templateLoader("ShipEntityPage", contentObject, null, null, null);
			} else if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ASSIGN_SHIP_TO"))
					.equalsIgnoreCase("Y") && shipAddressList != null && shipAddressList.size() > 1 && fromPage != null
					&& fromPage.trim().equalsIgnoreCase("loginPage")) {
				String contactId = (String) session.getAttribute("customerId");
				UserManagement userObj = new UserManagementImpl();
				UsersModel userModelForERP = new UsersModel();
				userModelForERP.setCustomerId(contactId);
				userModelForERP.setSession(session);
				userObj.getCustomerDataFromERP(userModelForERP);
				String defaultShipTo = (String) session.getAttribute("defaultShipTo");
				Boolean shipToRequired = (Boolean) session.getAttribute("shipToRequired");
				long shipEntitySize = shipAddressList.size();
				session.setAttribute("shipEntitySize", shipEntitySize);
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("defaultShipToId", defaultShipToId);
				contentObject.put("entityId", entityId);
				contentObject.put("shipEntity", shipAddressList);

				if (CommonUtility.validateString(defaultShipTo).length() > 0 && shipToRequired) {
					for (UsersModel usersModelObject : shipAddressList) {

						if (defaultShipTo.equalsIgnoreCase(usersModelObject.getShipToId())) {

							UsersModel usersModel = new UsersModel();
							usersModel.setAddressBookId(usersModelObject.getAddressBookId());
							usersModel.setBuyingCompanyId(usersModelObject.getBuyingCompanyId());
							usersModel.setEntityId(usersModelObject.getEntityId());
							usersModel.setCustomerName(usersModelObject.getCustomerName());
							usersModel.setZipCodeStringFormat(usersModelObject.getZipCodeStringFormat());
							usersModel.setShipToId(usersModelObject.getShipToId());
							assignShipEntity(usersModel);
							target = "Products";
						}
					}
				} else if (shipToRequired) {
					renderContent = LayoutGenerator.templateLoader("ShipEntityPage", contentObject, null, null, null);
				} else {
					session.setAttribute("MainAccount", "Y");
					for (UsersModel usersModelObject : shipAddressList) {
						UsersModel usersModel = new UsersModel();
						usersModel.setAddressBookId(usersModelObject.getAddressBookId());
						usersModel.setBuyingCompanyId(usersModelObject.getBuyingCompanyId());
						usersModel.setEntityId(usersModelObject.getEntityId());
						usersModel.setCustomerName(usersModelObject.getCustomerName());
						usersModel.setZipCodeStringFormat(usersModelObject.getZipCodeStringFormat());
						usersModel.setShipToId(usersModelObject.getShipToId());
						assignShipEntity(usersModel);
						target = "Products";
					}
				}
			} else if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_SHIPTO_PAGE"))
					.equalsIgnoreCase("Y") && shipAddressList != null && !shipAddressList.isEmpty()
					&& fromPage.equalsIgnoreCase("loginPage")) {
				for (UsersModel usersModelObject : shipAddressList) {

					if (defaultShipToId == usersModelObject.getAddressBookId()) {

						UsersModel usersModel = new UsersModel();
						usersModel.setAddressBookId(usersModelObject.getAddressBookId());
						usersModel.setBuyingCompanyId(usersModelObject.getBuyingCompanyId());
						usersModel.setEntityId(usersModelObject.getEntityId());
						usersModel.setCustomerName(usersModelObject.getCustomerName());
						usersModel.setZipCodeStringFormat(usersModelObject.getZipCodeStringFormat());
						usersModel.setShipToId(usersModelObject.getShipToId());
						assignShipEntity(usersModel);

						target = "Products";
						break;
					}
				}
			} else if (shipAddressList != null && !shipAddressList.isEmpty() && shipAddressList.size() > 1) {

				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("defaultShipToId", defaultShipToId);
				contentObject.put("entityId", entityId);
				contentObject.put("shipEntity", shipAddressList);

				if (fromPage.equalsIgnoreCase("2")) {
					renderContent = LayoutGenerator.templateLoader("ChangeShippingAddress", contentObject, null, null,
							null);
				} else if (fromPage.equalsIgnoreCase("ajaxLoad")) {
					contentObject.put("responseType", "shipAddressList");
					renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject, null, null, null);
				} else {
					renderContent = LayoutGenerator.templateLoader("ShipEntityPage", contentObject, null, null, null);
				}

			} else {

				if (shipAddressList != null && !shipAddressList.isEmpty() && shipAddressList.size() > 0) {
					UsersModel usersModel = new UsersModel();

					usersModel.setAddressBookId(shipAddressList.get(0).getAddressBookId());
					usersModel.setBuyingCompanyId(shipAddressList.get(0).getBuyingCompanyId());
					usersModel.setEntityId(shipAddressList.get(0).getEntityId());
					usersModel.setCustomerName(shipAddressList.get(0).getCustomerName());
					usersModel.setZipCodeStringFormat(shipAddressList.get(0).getZipCodeStringFormat());
					usersModel.setShipToId(shipAddressList.get(0).getShipToId());
					assignShipEntity(usersModel);
				}
				if (CommonUtility.validateString(fromPage).equalsIgnoreCase("wlCheckout")
						|| CommonUtility.validateString(fromPage).equalsIgnoreCase("abandonedCart")) {
					target = "shoppingCart";
				} else if (CommonUtility.validateURl(CommonUtility.validateString(fromPage))) {
					HOOK_URL = fromPage;
					target = "redirectUrl";
				} else if (CommonUtility.validateString(fromPage).equalsIgnoreCase("popLogin") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_SHIP_TO_POPUP")).equalsIgnoreCase("Y")) {
					LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
					contentObject.put("defaultShipToId", defaultShipToId);
					contentObject.put("entityId", entityId); 
					contentObject.put("shipEntity", shipAddressList);
					contentObject.put("fromPage", fromPage);
					renderContent = LayoutGenerator.templateLoader("ShipEntityPage", contentObject, null, null, null);
				} else {
					target = "Products";
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String getShippingInfo() {
		long startTimer = CommonUtility.startTimeDispaly();
		target = SUCCESS;
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			UserManagement usersObj = new UserManagementImpl();
			String fromPage = CommonUtility.validateString(request.getParameter("frPage"));
			String ipaddress = request.getHeader("X-Forwarded-For");
			if (ipaddress  == null){
				ipaddress = request.getRemoteAddr();	
			}

			ArrayList<UsersModel> accountList = new ArrayList<UsersModel>();
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SINGLE_USER_UNDER_MULTIPLE_CUSTOMER")).equalsIgnoreCase("Y") && request.getParameterMap().containsKey("checkAccount")){
				accountList =  UsersDAO.getUserAccountList((String) session.getAttribute(Global.USERNAME_KEY));
			}
			if(request.getParameterMap().containsKey("checkAccount") && accountList.size() > 1){
				String requestType = request.getParameter("checkAccount");
				session.setAttribute("multipleAccountsAvailable", "Y");
				if(CommonUtility.validateString(requestType).length()  >0 ){
					LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
					contentObject.put("accountList",accountList);
					contentObject.put("totalAccount",accountList.size());
					contentObject.put("frPage",request.getParameter("frPage"));
					renderContent = LayoutGenerator.templateLoader("ShipEntityPage", contentObject  , null, null, null);
				}
				return target;
			}else{
				if(request.getParameterMap().containsKey("requestType")){
					String requestType = request.getParameter("requestType");
					if(CommonUtility.validateString(requestType).length() > 0 && requestType.equals("MultipleAccounts")){
						UsersModel userModel = new UsersDAO().getUserStatus((String) session.getAttribute(Global.USERNAME_KEY), false);
						session.setAttribute("selectedUserId", CommonUtility.validateParseIntegerToString(userModel.getUserId()));
			            SecureData validUserPass = new SecureData();
	                    new LoginAuthentication().authenticate(userModel.getUserName(), validUserPass.validatePassword(userModel.getPassword()), session);
						UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Login", session.getId(), ipaddress, "Login", "NA");
					}
				}

			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			String parentId = (String) session.getAttribute("parentUserId");
			String sessionRedirectURL = (String) session.getAttribute("redirectURL");
			String showpopUp = CommonUtility.validateString(request.getParameter("showpopUp"));
			if (CommonUtility.validateURl(CommonUtility.validateString(sessionRedirectURL))) {
				HOOK_URL = sessionRedirectURL;
				fromPage = sessionRedirectURL;
			} else if (CommonUtility.validateURl(CommonUtility.validateString(fromPage))
					&& CommonUtility.validateString(sessionRedirectURL).length() == 0) {
				HOOK_URL = fromPage;
				session.setAttribute("redirectURL", fromPage);
			}
			shipAddressList = new ArrayList<UsersModel>();
			String billEntityId = "0";
			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_LOGIN_MESSAGE"))
					.equalsIgnoreCase("Y")) {
				if (session.getAttribute("defaultBillToId") != null
						&& session.getAttribute("defaultBillToId").toString().length() > 0) {
					defaultBillToId = CommonUtility.validateNumber((String) session.getAttribute("defaultBillToId"));
				}

				if (defaultBillToId == 0) {
					HashMap<String, Integer> userAddressId = UsersDAO
							.getDefaultAddressIdForBCAddressBook((String) session.getAttribute(Global.USERNAME_KEY));
					// UsersDAO.getDefaultAddressId(userId);
					defaultBillToId = userAddressId.get("Bill");
					// defaultShipToId = userAddressId.get("Ship");
				}

				HashMap<String, UsersModel> userAddress = usersObj.getUserAddressFromBCAddressBook(defaultBillToId,
						defaultShipToId);
				// UsersDAO.getUserAddress(userId, defaultBillToId, defaultShipToId);
				billAddress = userAddress.get("Bill");
				billEntityId = billAddress.getEntityId();
			}
			// shipAddressList = getShipAddressList(userId);
			HashMap<String, ArrayList<UsersModel>> templist = usersObj.getAddressListFromBCAddressBook(
					CommonUtility.validateNumber(buyingCompanyId), CommonUtility.validateNumber(sessionUserId));
			shipAddressList = templist.get("Ship");
			if (parentId != null && parentId.trim().length() > 0 && CommonUtility.validateNumber(parentId) > 0) {
				// HashMap<String, ArrayList<UsersModel>> shipList =
				// UsersDAO.getAgentAddressListFromBCAddressBook(CommonUtility.validateNumber(sessionUserId),"");
				UsersModel model = new UsersModel();
				model.setUserId(CommonUtility.validateNumber(sessionUserId));
				model.setSession(session);
				HashMap<String, ArrayList<UsersModel>> shipList = usersObj.getAgentAddressListFromBCAddressBook(model);
				if (shipList.size() != 0) {
					shipAddressList = new ArrayList<UsersModel>();
					shipAddressList = shipList.get("Ship");
				}

			}
			// Adapt Service
			UnilogFactoryInterface serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
			if (serviceClass != null && shipAddressList != null) {
				ArrayList<UsersModel> shipList = serviceClass.addShipAddressList(shipAddressList);
				if (shipList != null) {
					shipAddressList = shipList;
				}
			}
			// End Service

			if (shipAddressList != null && shipAddressList.size() > 1) {
				session.setAttribute("multipleShipAddressAvailable", "Y");
			} else {
				session.setAttribute("multipleShipAddressAvailable", "N");
			}
			if (session.getAttribute("defaultShipToId") != null
					&& session.getAttribute("defaultShipToId").toString().length() > 0) {
				defaultShipToId = CommonUtility.validateNumber((String) session.getAttribute("defaultShipToId"));
			}
			if (session.getAttribute("entityId") != null && session.getAttribute("entityId").toString().length() > 0) {
				entityId = (String) session.getAttribute("entityId");
			}

			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_EMAIL_ID"))
					.equalsIgnoreCase("Y")) {
				checkEmailAddress(CommonUtility.validateNumber(sessionUserId));
			}
			String emailAddressExits = (String) session.getAttribute("emailAddressExits");
			if (CommonUtility.validateString(emailAddressExits).equalsIgnoreCase("N")) {
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				defaultShipToId = CommonUtility.validateNumber((String) session.getAttribute("defaultShipToId"));
				int bcAddressbookId = 0;
				if (defaultShipToId == 0) {
					defaultShipToId = UsersDAO.getBcAddressBookShipBillId(
							CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId")), entityId,
							AddressType.Ship);
					bcAddressbookId = UsersDAO.checkDefaultBillShipExist(userId, AddressType.Ship);

					if (bcAddressbookId == 0) {
						UsersDAO.updateDefaultShiptoBillSupp(defaultShipToId, userId, AddressType.Ship);
					}
					// defaultShiptoId =
					// CommonUtility.validateParseIntegerToString(UsersDAO.getBcAddressBookId(CommonUtility.validateNumber((String)
					// session.getAttribute("buyingCompanyId")), entityId));
					System.out.println("ship Address book ID updated");
				}
				contentObject.put("defaultShipToId", defaultShipToId);
				contentObject.put("entityId", entityId);
				contentObject.put("shipEntity", shipAddressList);
				contentObject.put("validEmailAddress", false);

				renderContent = LayoutGenerator.templateLoader("ShipEntityPage", contentObject, null, null, null);
			} else if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ASSIGN_SHIP_TO"))
					.equalsIgnoreCase("Y") && shipAddressList != null && shipAddressList.size() > 1 && fromPage != null
					&& fromPage.trim().equalsIgnoreCase("loginPage")) {
				String contactId = (String) session.getAttribute("customerId");
				UserManagement userObj = new UserManagementImpl();
				UsersModel userModelForERP = new UsersModel();
				userModelForERP.setCustomerId(CommonUtility.validateString(contactId));
				userModelForERP.setSession(session);
				userObj.getCustomerDataFromERP(userModelForERP);
				String defaultShipTo = (String) session.getAttribute("defaultShipTo");
				Boolean shipToRequired = (Boolean) session.getAttribute("shipToRequired");
				long shipEntitySize = shipAddressList.size();
				session.setAttribute("shipEntitySize", shipEntitySize);
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("defaultShipToId", defaultShipToId);
				contentObject.put("entityId", entityId);
				contentObject.put("shipEntity", shipAddressList);

				if (CommonUtility.validateString(defaultShipTo).length() > 0 && shipToRequired) {
					for (UsersModel usersModelObject : shipAddressList) {

						if (defaultShipTo.equalsIgnoreCase(usersModelObject.getShipToId())) {

							UsersModel usersModel = new UsersModel();
							usersModel.setAddressBookId(usersModelObject.getAddressBookId());
							usersModel.setBuyingCompanyId(usersModelObject.getBuyingCompanyId());
							usersModel.setEntityId(usersModelObject.getEntityId());
							usersModel.setCustomerName(usersModelObject.getCustomerName());
							usersModel.setZipCodeStringFormat(usersModelObject.getZipCodeStringFormat());
							usersModel.setShipToId(usersModelObject.getShipToId());
							assignShipEntity(usersModel);
							target = "Products";
						}
					}
				} else if (shipToRequired) {
					renderContent = LayoutGenerator.templateLoader("ShipEntityPage", contentObject, null, null, null);
				} else {
					session.setAttribute("MainAccount", "Y");
					for (UsersModel usersModelObject : shipAddressList) {
						UsersModel usersModel = new UsersModel();
						usersModel.setAddressBookId(usersModelObject.getAddressBookId());
						usersModel.setBuyingCompanyId(usersModelObject.getBuyingCompanyId());
						usersModel.setEntityId(usersModelObject.getEntityId());
						usersModel.setCustomerName(usersModelObject.getCustomerName());
						usersModel.setZipCodeStringFormat(usersModelObject.getZipCodeStringFormat());
						usersModel.setShipToId(usersModelObject.getShipToId());
						assignShipEntity(usersModel);
						target = "Products";
					}
				}
			} else if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_SHIPTO_PAGE"))
					.equalsIgnoreCase("Y") && shipAddressList != null && shipAddressList.size() > 1 && fromPage != null
					&& (fromPage.trim().equalsIgnoreCase("loginPage"))) {
				for (UsersModel usersModelObject : shipAddressList) {

					if (defaultShipToId == usersModelObject.getAddressBookId()) {

						UsersModel usersModel = new UsersModel();
						usersModel.setAddressBookId(usersModelObject.getAddressBookId());
						usersModel.setBuyingCompanyId(usersModelObject.getBuyingCompanyId());
						usersModel.setEntityId(usersModelObject.getEntityId());
						usersModel.setCustomerName(usersModelObject.getCustomerName());
						usersModel.setZipCodeStringFormat(usersModelObject.getZipCodeStringFormat());
						usersModel.setShipToId(usersModelObject.getShipToId());
						usersModel.setBillEntityId(billEntityId);
						usersModel.setWareHouseCodeStr(usersModelObject.getWareHouseCodeStr());
						session.setAttribute("homeBranchCity",usersModelObject.getCity());
						assignShipEntity(usersModel);

						target = "Products";
						break;
					}
				}
			} else if (shipAddressList != null && shipAddressList.size() > 1 && fromPage != null
					&& (fromPage.trim().equalsIgnoreCase("loginPage") || fromPage.trim().equalsIgnoreCase("popLogin"))
					&& !CommonUtility.validateString((String) session.getAttribute("firstLogin")).equalsIgnoreCase("Y")
					&& CommonUtility.validateString(showpopUp).equalsIgnoreCase("N")) {
				for (UsersModel usersModelObject : shipAddressList) {

					if (defaultShipToId == usersModelObject.getAddressBookId()) {

						UsersModel usersModel = new UsersModel();
						usersModel.setAddressBookId(usersModelObject.getAddressBookId());
						usersModel.setBuyingCompanyId(usersModelObject.getBuyingCompanyId());
						usersModel.setEntityId(usersModelObject.getEntityId());
						usersModel.setCustomerName(usersModelObject.getCustomerName());
						usersModel.setZipCodeStringFormat(usersModelObject.getZipCodeStringFormat());
						usersModel.setShipToId(usersModelObject.getShipToId());
						usersModel.setBillEntityId(billEntityId);
						usersModel.setWareHouseCodeStr(usersModelObject.getWareHouseCodeStr());
						session.setAttribute("homeBranchCity",usersModelObject.getCity());
						assignShipEntity(usersModel);

						target = "Products";
						break;
					}
				}
			} else if (shipAddressList != null && shipAddressList.size() > 1) {

				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("defaultShipToId", defaultShipToId);
				contentObject.put("entityId", entityId);
				contentObject.put("shipEntity", shipAddressList);
				contentObject.put("billEntityId", billEntityId);
				contentObject.put("fromPage", fromPage);
				if (fromPage != null && fromPage.trim().equalsIgnoreCase("2")) {
					renderContent = LayoutGenerator.templateLoader("ChangeShippingAddress", contentObject, null, null,
							null);
				} else if (fromPage != null && fromPage.trim().equalsIgnoreCase("ajaxLoad")) {
					contentObject.put("responseType", "shipAddressList");
					renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject, null, null, null);
				} else {
					renderContent = LayoutGenerator.templateLoader("ShipEntityPage", contentObject, null, null, null);
				}

			} else {

				if (shipAddressList != null && shipAddressList.size() > 0) {
					// $shippAddressList.addressBookId,$shippAddressList.buyingCompanyId,$shippAddressList.entityId,'$shippAddressList.customerName','$shippAddressList.zipCodeStringFormat'
					UsersModel usersModel = new UsersModel();
					usersModel.setAddressBookId(shipAddressList.get(0).getAddressBookId());
					usersModel.setBuyingCompanyId(shipAddressList.get(0).getBuyingCompanyId());
					usersModel.setEntityId(shipAddressList.get(0).getEntityId());
					usersModel.setCustomerName(shipAddressList.get(0).getCustomerName());
					usersModel.setZipCodeStringFormat(shipAddressList.get(0).getZipCodeStringFormat());
					usersModel.setShipToId(shipAddressList.get(0).getShipToId());
					usersModel.setBillEntityId(billEntityId);
					if(CommonUtility.customServiceUtility() != null) {
						CommonUtility.customServiceUtility().assignEntityIdForShip(usersModel, session); // Wallace HardWare Custom Service
					}
					assignShipEntity(usersModel);
				}
				if (CommonUtility.validateString(fromPage).equalsIgnoreCase("wlCheckout")
						|| CommonUtility.validateString(fromPage).equalsIgnoreCase("abandonedCart")) {
					target = "shoppingCart";
				} else if (CommonUtility.validateString(fromPage).equalsIgnoreCase("2")) {
					target = "UserAdress";
				} else if (CommonUtility.validateURl(CommonUtility.validateString(fromPage))) {
					HOOK_URL = fromPage;
					target = "redirectUrl";
				} else if (CommonUtility.validateString(fromPage).equalsIgnoreCase("popLogin")) {
					LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
					contentObject.put("defaultShipToId", defaultShipToId);
					contentObject.put("entityId", entityId);
					contentObject.put("shipEntity", shipAddressList);
					contentObject.put("billEntityId", billEntityId);
					contentObject.put("fromPage", fromPage);
					renderContent = LayoutGenerator.templateLoader("ShipEntityPage", contentObject, null, null, null);
				} else {
					target = "Products";
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return target;
	}

	public String assignShipEntity() {
		long startTimer = CommonUtility.startTimeDispaly();
		target = SUCCESS;
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(userId==0){
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			userId = CommonUtility.validateNumber(sessionUserId);
		}
		//buyingCompanyId = (String) session.getAttribute("buyingCompanyId"); 
		try
		{
			 
		    String defaultShiptoId = CommonUtility.validateString(request.getParameter("defaultShipToId"));
		    String entityId = CommonUtility.validateString(request.getParameter("entityId"));
		    String custName = CommonUtility.validateString(request.getParameter("custName"));
		    String homeBranchZipCode = CommonUtility.validateString(request.getParameter("homeBranchZipCode"));
		    String shipToIdSx = CommonUtility.validateString(request.getParameter("shipToIdSx"));
		    String firstName = CommonUtility.validateString(request.getParameter("firstName"));
		    String lastName = CommonUtility.validateString(request.getParameter("lastName"));
		    String homeBranchCity = CommonUtility.validateString(request.getParameter("homeBranchCity"));
		    int subsetId = CommonUtility.validateNumber(request.getParameter("subsetId"));
		    /*
		    int bcAddressbookId = 0;
		    String setDefaultShipTo = CommonUtility.validateString(request.getParameter("setDefaultShipTo"));
			if(CommonUtility.validateNumber(defaultShiptoId) == 0){
				defaultShiptoId = CommonUtility.validateParseIntegerToString(UsersDAO.getBcAddressBookShipBillId(CommonUtility.validateNumber((String)session.getAttribute("buyingCompanyId")),entityId,AddressType.Ship));
	            bcAddressbookId = UsersDAO.checkDefaultBillShipExist(userId,AddressType.Ship);
				
				if(bcAddressbookId == 0){
					UsersDAO.updateDefaultShiptoBillSupp(CommonUtility.validateNumber(defaultShiptoId),userId,AddressType.Ship);
				}
				//defaultShiptoId = CommonUtility.validateParseIntegerToString(UsersDAO.getBcAddressBookId(CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId")), entityId));
			}else if(CommonUtility.validateString(setDefaultShipTo).equalsIgnoreCase("Y")){
			       UsersDAO.updateDefaultShiptoId(userId,CommonUtility.validateNumber(defaultShiptoId));
			}	
			
			 bcAddressbookId = UsersDAO.checkDefaultBillShipExist(userId,AddressType.Bill);
				
				if(bcAddressbookId == 0){
					UserManagement usersObj = new UserManagementImpl();							
					HashMap<String, ArrayList<UsersModel>> userAddressList =usersObj.getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId),userId);
					//UsersDAO.getAddressList(userId);
					billAddressList = userAddressList.get("Bill");
					if(billAddressList!=null && billAddressList.get(0).getAddressBookId()>0) {
						UsersDAO.updateDefaultShiptoBillSupp(billAddressList.get(0).getAddressBookId(),userId,AddressType.Bill);
					}
				}
			if(CommonUtility.validateNumber(defaultShiptoId) == 0){
				defaultShiptoId = CommonUtility.validateParseIntegerToString(UsersDAO.getBcAddressBookShipBillId(CommonUtility.validateNumber((String)session.getAttribute("buyingCompanyId")),entityId,AddressType.Ship));
	            bcAddressbookId = UsersDAO.checkDefaultBillShipExist(userId,AddressType.Ship);
				
				if(bcAddressbookId == 0){
					UsersDAO.updateDefaultShiptoBillSupp(CommonUtility.validateNumber(defaultShiptoId),userId,AddressType.Ship);
				}
				//defaultShiptoId = CommonUtility.validateParseIntegerToString(UsersDAO.getBcAddressBookId(CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId")), entityId));
			}
			*/
		    if(CommonUtility.customServiceUtility() != null) {
		    	defaultShiptoId = CommonUtility.customServiceUtility().getEntityShipToId(session, defaultShiptoId, shipToIdSx);
		    }
			session.setAttribute("defaultShipToId",defaultShiptoId);
			session.setAttribute("homeBranchCity",homeBranchCity);
			session.setAttribute("selectedShipName",firstName+""+lastName);
			
			if(CommonUtility.customServiceUtility() != null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MultipleShipToAddressCartAP")).equalsIgnoreCase("Y")) {
				session.setAttribute("multipleShipToAddressAP", "N");
			}
			
		    UsersModel usersModel = new UsersModel();
		    usersModel.setAddressBookId(CommonUtility.validateNumber(defaultShiptoId));
			usersModel.setEntityId(CommonUtility.validateString(entityId));
			usersModel.setCustomerName(custName);
			usersModel.setZipCodeStringFormat(homeBranchZipCode);
			usersModel.setShipToId(shipToIdSx);
			usersModel.setFirstName(firstName);
			usersModel.setLastName(lastName);
			usersModel.setSubsetId(subsetId);
			usersModel.setBuyingCompanyId(CommonUtility.validateNumber(request.getParameter("buyingCompanyId")));
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_LOGIN_MESSAGE")).equalsIgnoreCase("Y")){
				String billEntityId = CommonUtility.validateString((String) request.getParameter("billEntityId"));
				usersModel.setBillEntityId(billEntityId);;
			}

			if(CommonDBQuery.getSystemParamtersList().get("ASYNC_PROCESS")!=null && CommonDBQuery.getSystemParamtersList().get("ASYNC_PROCESS").trim().equalsIgnoreCase("Y")) {
				String shipToWarehouseCode = CommonUtility.validateString(request.getParameter("shipToWarehouseCode"));
				new Thread(new AsyncShipEntity(session, usersModel, shipToWarehouseCode)).start();
			}else {
				assignShipEntity(usersModel); 
			}
			
			session.setAttribute("MainAccount", "");
			String emailAddressExits = (String) session.getAttribute("emailAddressExits");
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_EMAIL_ID")).equalsIgnoreCase("Y") && CommonUtility.validateString(emailAddressExits).equalsIgnoreCase("N")){
			   target = "ResultLoader";
			   result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("checkemailaddress.status.noemailaddress");
			   renderContent = result;
			}else {
				target = SUCCESS;
		    }
			if(CommonUtility.customServiceUtility() != null) {
				CommonUtility.customServiceUtility().checkShipToLevelCustomFieldsOnAssignShipEntity(request,session,subsetId);//Electrozad Custom Service
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return target;
	
	}
	
	public String assignShipEntity(UsersModel usersModel) {
		long startTimer = CommonUtility.startTimeDispaly();
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
	    String shipToIdSx = CommonUtility.validateString(usersModel.getShipToId());

		String shipToWarehouseCode = CommonUtility.validateString(request.getParameter("shipToWarehouseCode"));
		try{
			//assiging sessionvalue for selectedshipToIdSx before calling asyncAssignShipEntity as per shashi sir suggest (MC-927) 
			  session.setAttribute("selectedshipToIdSx", CommonUtility.validateString(shipToIdSx));
              asyncAssignShipEntity(usersModel, session,shipToWarehouseCode);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return "";
	}

	public static String asyncAssignShipEntity(UsersModel usersModel,HttpSession session,String shipToWarehouseCode){
		long startTimer = CommonUtility.startTimeDispaly();
		try{
			String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			
			String customerNumber = CommonUtility.validateString((String) session.getAttribute("customerId"));
		    String defaultShiptoId = CommonUtility.validateString(Integer.toString(usersModel.getAddressBookId()));
		    String entityId = CommonUtility.validateString(usersModel.getEntityId());
		    String custName = CommonUtility.validateString(usersModel.getCustomerName());
		    String homeBranchZipCode = CommonUtility.validateString(usersModel.getZipCodeStringFormat());
		    String shipToIdSx = CommonUtility.validateString(usersModel.getShipToId());
		    //String defaultBilltoId = (String)session.getAttribute("defaultBillToId");     
	        //String billingEntityId = (String)session.getAttribute("billingEntityId");
	          
	        if(CommonUtility.validateString(shipToWarehouseCode).length()>0){
	          session.setAttribute("wareHouseCode", CommonUtility.validateString(shipToWarehouseCode).trim());
	        }else if(CommonUtility.validateString(usersModel.getWareHouseCodeStr()).trim().length()>0){
	          session.setAttribute("wareHouseCode", CommonUtility.validateString(usersModel.getWareHouseCodeStr()).trim());
	        }
	          
	        if(session!=null && CommonUtility.validateString((String)session.getAttribute("wareHouseCode")).length() > 0 && CommonUtility.customServiceUtility()!=null){
	            String networkWarehouse =  CommonUtility.customServiceUtility().getNetworkWarehouseCode(CommonUtility.validateString((String)session.getAttribute("wareHouseCode")));
		  		if(networkWarehouse != null){
		  			session.setAttribute("wareHouseCode",CommonUtility.validateString(networkWarehouse));
		  		}
	        }
	          
	        //macomb assign ship to id
	        int bcAddressbookId = 0;
			if(CommonUtility.validateNumber(defaultShiptoId) == 0){
				defaultShiptoId = CommonUtility.validateParseIntegerToString(UsersDAO.getBcAddressBookShipBillId(CommonUtility.validateNumber((String)session.getAttribute("buyingCompanyId")),entityId,AddressType.Ship));
                bcAddressbookId = UsersDAO.checkDefaultBillShipExist(userId,AddressType.Ship);
					
					if(bcAddressbookId == 0){
						UsersDAO.updateDefaultShiptoBillSupp(CommonUtility.validateNumber(defaultShiptoId),userId,AddressType.Ship);
					}
					//defaultShiptoId = CommonUtility.validateParseIntegerToString(UsersDAO.getBcAddressBookId(CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId")), entityId));
					System.out.println("ship Address book ID updated");
				}else{
					UsersDAO.updateDefaultShiptoBillSupp(CommonUtility.validateNumber(defaultShiptoId),userId,AddressType.Ship);
				}
				
				 bcAddressbookId = UsersDAO.checkDefaultBillShipExist(userId,AddressType.Bill);
					
				if(bcAddressbookId == 0){
					UserManagement usersObj = new UserManagementImpl();							
					HashMap<String, ArrayList<UsersModel>> userAddressList =usersObj.getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId),userId);
					//UsersDAO.getAddressList(userId);
					ArrayList<UsersModel> billAddressList = userAddressList.get("Bill");
					if(billAddressList!=null && billAddressList.get(0).getAddressBookId()>0) {
						UsersDAO.updateDefaultShiptoBillSupp(billAddressList.get(0).getAddressBookId(),userId,AddressType.Bill);
						System.out.println("bill Address book ID updated");
					}else {
						System.out.println("bill Address book ID NOT Updated");
					}
					
				}else{
					UsersDAO.updateDefaultShiptoBillSupp(bcAddressbookId,userId,AddressType.Bill);
				}
            session.setAttribute("defaultShipToId",defaultShiptoId);
            
		    UsersModel uModel = new UsersModel();
		    if(entityId!=null && !entityId.trim().equalsIgnoreCase("")){
		    	uModel.setEntityId(CommonUtility.validateString(entityId));
			    uModel.setUserToken((String)session.getAttribute("userToken"));
			    uModel.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
			    if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_LOGIN_MESSAGE")).equalsIgnoreCase("Y")){
			    	uModel.setBillEntityId(CommonUtility.validateString(usersModel.getBillEntityId()));
			    }
			    
			    if(usersModel.getBuyingCompanyId() > 0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHIP_TO_LEVEL_CUSTOM_FIELD")).equalsIgnoreCase("Y")){
					LinkedHashMap<String, String> customerCustomFieldValue = UsersDAO.getAllCustomerCustomFieldValue(usersModel.getBuyingCompanyId());
					if(customerCustomFieldValue!=null && customerCustomFieldValue.size()>0){
					session.setAttribute("customerCustomFieldValue",customerCustomFieldValue);
					}
				}
			    System.out.println(defaultShiptoId);
			    if(defaultShiptoId!=null)
			    {	
			    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SEARCHBY_USER_ENTITY")).equalsIgnoreCase("N")){
			    		session.setAttribute("customerBuyingCompanyId",UserRegisterUtility.checkEntityId(CommonUtility.validateString(entityId)));
				    }
		    	 	session.setAttribute("onAccountCustomer", "N");
					session.setAttribute("userToken", CommonUtility.validateString(uModel.getEntityId()));
			    	session.setAttribute("defaultShipToId", defaultShiptoId);
			    	session.setAttribute("entityId", entityId);
			    	session.setAttribute("custName", custName);
			    	if(usersModel.getSubsetId()>0)
			    		session.setAttribute("userSubsetId", usersModel.getSubsetId());
			    	if(CommonUtility.customServiceUtility()!=null) {
					CommonUtility.customServiceUtility().setSubsetViaZipCode(uModel, usersModel);
		  			}
			    	if(homeBranchZipCode!=null && CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT")!=null && CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT").trim().equalsIgnoreCase("Y")){
			    		 if(homeBranchZipCode!=null){
			    		    	YahooBossSupport getUserLocation = new YahooBossSupport();
			    		    	UsersModel locaDetail = new UsersModel();
			    		    	locaDetail.setZipCode(homeBranchZipCode);
			    		    	locaDetail = getUserLocation.locateUser(locaDetail);
			    		        String userLati  = locaDetail.getLatitude();
			    		        String userLongi = locaDetail.getLongitude();
			    		        session.setAttribute("homeBranchZipCode", homeBranchZipCode);
			    		        session.setAttribute("homeBranchName", locaDetail.getCity());
			    		        homeBranchZipCode = homeBranchZipCode+"|City: "+locaDetail.getCity()+ "|State: "+locaDetail.getState();
			    		    	session.setAttribute("homeBranchLongi", userLongi);
			    		    	session.setAttribute("homeBranchLati", userLati);
			    		    	session.setAttribute("homeBranchDisplay", homeBranchZipCode);
			    		    }
			    		    if(defaultShiptoId!=null)
			    		    {
			    		    	session.setAttribute("defaultShipToId", defaultShiptoId);
			    		    	session.setAttribute("entityId", entityId);
			    		    }
			    		    String shipBranchId = "";
			    	    	String shipBranchName = "";
			    	    	String homeBranchId = (String) session.getAttribute("homeBranchId");;
			    		    
			    				if(homeBranchId!=null)
			    				{
			    					shipBranchId = UsersDAO.getShipBranch(homeBranchId);
			    				}
			    				shipBranchName = UsersDAO.getShipBranchName(shipBranchId);
			    				System.out.println("User Home Branch : " + homeBranchId);
			    				System.out.println("User Ship Branch : " + shipBranchId);
			    			
			    		    session.setAttribute("shipBranchId", shipBranchId);
			    		    session.setAttribute("defaultShipToId", defaultShiptoId);
			    		    session.setAttribute("entityId", entityId);
			    		    session.setAttribute("custName", custName);
			    		    session.setAttribute("shipBranchName", shipBranchName);
				    }
			    	
			    	String storeShipVia = CommonDBQuery.getSystemParamtersList().get("STORE_SHIPVIA_AT_LOGIN");
					if(storeShipVia!=null && storeShipVia.trim().equalsIgnoreCase("Y")){
						UsersModel customerInfoInput = new UsersModel();
						customerInfoInput.setUserToken((String)session.getAttribute("userToken"));
						customerInfoInput.setEntityId(CommonUtility.validateString(entityId));
						customerInfoInput.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
						ArrayList<ShipVia> shipViaList = (ArrayList<ShipVia>) session.getAttribute("shipViaList");//customerInfoInput.getShipViaList();//usersObj.getShipViaList(customerInfoInput); //UsersDAO.getShipViaList() //https://api.gerrie.com:8443/Help/Api/POST-api-1-Customer-Query
						session.setAttribute("shipViaList", shipViaList);
						
					}
					setBillToAndShipToInSession(0,CommonUtility.validateNumber(defaultShiptoId), session); //Set Addess in Session 
		    	}
			    //-----------
			    UsersModel userModelForERP = new UsersModel();
			    userModelForERP.setUserToken(customerNumber);
			    userModelForERP.setSession(session);
			    userModelForERP.setCustomerId(CommonUtility.validateString(customerNumber));
			    userModelForERP.setShipToId(shipToIdSx);
			    userModelForERP.setShipToChanged("Y");
			    userModelForERP.setSession(session);
			    userModelForERP.setShipToWarehouseCode(shipToWarehouseCode);
			    session.removeAttribute("salesRepIn");
			    session.removeAttribute("salesRepOut");
			    
			    //Cannot use reflection as it is called by async thread check with Shashi or bharath
			    if(session.getAttribute("erpType")!=null && CommonUtility.validateString((String)session.getAttribute("erpType")).length()>0){
					if(CommonUtility.validateString((String)session.getAttribute("erpType")).equalsIgnoreCase("cimm2bcentral")) {
						com.erp.service.cimm2bcentral.action.UserManagementAction userAction = new com.erp.service.cimm2bcentral.action.UserManagementAction(); 
						userAction.getCustomerDataFromERP(userModelForERP);
					}else if(CommonUtility.validateString((String)session.getAttribute("erpType")).equalsIgnoreCase("cimmesb")) {
						com.erp.service.cimmesb.action.UserManagementAction userAction = new com.erp.service.cimmesb.action.UserManagementAction(); 
						userAction.getCustomerDataFromERP(userModelForERP);
					}
				}
			  //Cannot use reflection as it is called by async thread check with Shashi or bharath
			    
		    }else{
		    	System.out.println("No Entity ID");
		    }
		    
		    if(userId > 1 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInShoppingCart")).equalsIgnoreCase("Y")) {
				// "SavePriceInShoppingCart" below line to sync price stored in cart with ERP
				new Thread(new SyncUserCartWithErpThread(session)).start();
				// "SavePriceInShoppingCart" below line to sync price stored in cart with ERP
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return null;
	}

	public String getUserAddress() {
		long startTimer = CommonUtility.startTimeDispaly();
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Gson gson = new Gson();
		String tempdefaultShipId = null;
		String userName = null;
		String sessionUserId = null;
		target = "SESSIONEXPIRED";

		try {

			String customerNumber = (String) session.getAttribute("customerId");
			String isCreditCardOnly = (String) session.getAttribute("isCreditCardOnly");
			String selectedShipCode = (String) session.getAttribute("selectedShipCode");// WG
			UserManagement usersObj = new UserManagementImpl();
			ArrayList<UsersModel> orderStatusList = new ArrayList<UsersModel>();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			String shipToChanged = CommonUtility.validateString(request.getParameter("shipToChanged"));
			String shipToWarehouseCode = CommonUtility.validateString(request.getParameter("shipToWarehouseCode"));
			contentObject.put("partialCartDisplay", "N");
			Map<Integer, String> vendorSpecificPurchaseMsg=new HashMap<>();
			if(CommonUtility.validateString(type).equalsIgnoreCase("mobile")){
				session.setAttribute("type", "mobile");
				String id = request.getParameter("uId");
				userName = request.getParameter("userName");
				System.out.println(userName);
				userId = CommonUtility.validateNumber(id);
				HashMap<String, String> userDetails = UsersDAO.getUserPasswordAndUserId(userName, "Y");
				SecureData getPassword = new SecureData();
				String userPassword = getPassword.validatePassword(userDetails.get("password"));
				LoginAuthentication loginAuthentication = new LoginAuthentication();
				loginAuthentication.authenticate(userName, userPassword, session);
				buyingCompanyId = request.getParameter("buyingCompanyId");
				entityId = request.getParameter("entityId");
			} else {
				if (shipToChanged != null && shipToChanged.trim().equalsIgnoreCase("Y")) {
					if (session.getAttribute("quoteOrderId") != null)
						session.removeAttribute("quoteOrderId");
					if (session.getAttribute("quoteNumber") != null)
						session.removeAttribute("quoteNumber");
				}
				if (isReOrder != null && isReOrder.trim().equalsIgnoreCase("Y")) {
					ProductsAction priceUpdate = new ProductsAction();
					priceUpdate.setShoppingCartQty(shoppingCartQty);
					priceUpdate.setShoppingCartId(shoppingCartId);
					priceUpdate.updateQuoteCart();

					String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
					String tempSubset = (String) session.getAttribute("userSubsetId");
					contentObject.put("subsetId", tempSubset);
					contentObject.put("generalsubsetId", tempGeneralSubset);
					contentObject.put("getReorderCountAndTotal", "Y");
					contentObject = ProductsDAO.getQuoteCartDao(session, contentObject);
					contentObject.put("cartCountReorder", contentObject.get("selectQuoteCartCount"));
					contentObject.put("cartCountTotalReorder", contentObject.get("cartTotal"));
				} else {
					ProductsAction productUpdate = new ProductsAction();
					productUpdate.setShoppingCartQty(shoppingCartQty);
					productUpdate.setShoppingCartId(shoppingCartId);
					if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT")).length() > 0 && CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT").trim().equalsIgnoreCase("Y")) {
						session.removeAttribute("vendorSpecificPurchaseMsg");
					}else {
						productUpdate.updateShoppingCart();
					}
				}

				sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
				buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
				entityId = (String) session.getAttribute("billingEntityId");
				userId = CommonUtility.validateNumber(sessionUserId);
				userName = (String) session.getAttribute(Global.USERNAME_KEY);

			}
			String contactEmail=null;
			if(CommonUtility.customServiceUtility()!=null) {
				contactEmail=CommonUtility.customServiceUtility().setContactEmailToOrder(userId,CommonUtility.validateString((String)session.getAttribute("loginUserERPId")));
				contentObject.put("contactEmail", CommonUtility.validateString(contactEmail));
			}
			
			defaultShipVia = (String) session.getAttribute("defaultShipVia");
			if(CommonUtility.customServiceUtility() != null) {
				defaultShipToId = CommonUtility.validateNumber(CommonUtility.customServiceUtility().getEntityShipToId(session, CommonUtility.validateParseIntegerToString(defaultShipToId), (String)session.getAttribute("shipEntityId")));
			}
			if (defaultShipToId > 0) {
				session.setAttribute("defaultShipToId", Integer.toString(defaultShipToId));
			} else if (CommonUtility.validateString((String) session.getAttribute("defaultShipToId")).length() == 0) {
				HashMap<String, Integer> defaultAddresssId = UsersDAO.getDefaultAddressId(userId);
				int currentDefaultId = defaultAddresssId.get("Ship");
				session.setAttribute("defaultShipToId", Integer.toString(currentDefaultId));
			}
			tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
			String wareHousecode = (String) session.getAttribute("wareHouseCode");
			if (userId > 1) {
				try {
					if (CommonDBQuery.getSystemParamtersList().get("ENABLE_MANUFACTURERS_THRESHOLD_CHECKOUT") != null && CommonDBQuery.getSystemParamtersList().get("ENABLE_MANUFACTURERS_THRESHOLD_CHECKOUT").trim().equalsIgnoreCase("Y")) {
						LinkedHashMap<String, Object> innerContentObject = new LinkedHashMap<String, Object>();
						LinkedHashMap<String, String> userCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("userCustomFieldValue");
						if(userCustomFieldValue!=null && CommonUtility.validateString(userCustomFieldValue.get("DISPLAY_MIN_DOLLAR_MSG")).length()>0 && userCustomFieldValue.get("DISPLAY_MIN_DOLLAR_MSG").equalsIgnoreCase("Y")){
							innerContentObject = ProductsDAO.getShoppingCartDao(session, innerContentObject);
							vendorSpecificPurchaseMsg = ProductsDAO.checkManufacturerMinDollarDAO(innerContentObject);
							if (vendorSpecificPurchaseMsg != null && !vendorSpecificPurchaseMsg.isEmpty()) {
								session.setAttribute("vendorSpecificPurchaseMsg", vendorSpecificPurchaseMsg);
								if(session.getAttribute("displayCheckoutCondMsgFlag")== null || (String)session.getAttribute("displayCheckoutCondMsgFlag")!="N"){
								  target = "redirectCart";
								  return target;
								}
							} else {
								 session.setAttribute("vendorSpecificPurchaseMsg", "");
								  ProductsDAO.resetCartCheckoutType(userId);
				        		  session.setAttribute("displayCheckoutCondMsgFlag","Y");
							}
						}
					}
					poNumber = CommonUtility.validateString((String)session.getAttribute("poNumber"));
					shippingInstruction = CommonUtility.validateString((String) session.getAttribute("shippingInstruction"));
					orderNotes = CommonUtility.validateString((String) session.getAttribute("orderNotes"));
					reqDate = CommonUtility.validateString((String) session.getAttribute("reqDate"));
					String orderedBy = CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY));
					
					if (CommonUtility.validateString(selectedShipMethod).length() > 0) {
						session.setAttribute("selectedShipVia", CommonUtility.validateString(selectedShipMethod));
						session.setAttribute("customerShipVia", CommonUtility.validateString(selectedShipMethod));
					} else {
						if (CommonUtility.validateString((String)session.getAttribute("selectedBranchWillCall")).length() > 0) {
							String tempShip = (String) session.getAttribute("selectedBranchWillCall");
							String[] arry = tempShip.split("\\|");
							if (arry != null && arry[1] != null) {
								selectedShipMethod = arry[1].toString();
								// shipViaDisplay = arry[2].toString();
								session.setAttribute("selectedShipVia", selectedShipMethod);
								session.setAttribute("customerShipVia", selectedShipMethod);
							}
						}
					}
					customerShipVia = CommonUtility.validateString((String) session.getAttribute("customerShipVia"));
					defaultShipToId = CommonUtility.validateNumber(CommonUtility.validateString(tempdefaultShipId));
					defaultBillToId = CommonUtility.validateNumber(CommonUtility.validateString((String) session.getAttribute("defaultBillToId")));
					
					if (defaultBillToId == 0) {
						HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(userName);
						defaultBillToId = userAddressId.get("Bill");
					}
					
					//QUESTION: In reflection every call is going to same DAO method, can we call dao directly here instead of going through Reflection
					//HashMap<String, ArrayList<UsersModel>> userAddressList = usersObj.getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId), userId); 
					HashMap<String, ArrayList<UsersModel>> userAddressList = UsersDAO.getAddressListFromBCAddressBookDefault(CommonUtility.validateNumber(buyingCompanyId), userId);
					billAddressList = userAddressList.get("Bill");
					shipAddressList = userAddressList.get("Ship");
					if (CommonUtility.customServiceUtility()!=null && shipAddressList != null) {
						ArrayList<UsersModel> shipList = CommonUtility.customServiceUtility().addShipAddressList(shipAddressList);
						if (shipList != null) {
							shipAddressList = shipList;
						}
					}
					
					//QUESTION: In reflection every call is going to same DAO method, can we call dao directly here instead of going through Reflection
					//HashMap<String, UsersModel> userAddress = usersObj.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
					HashMap<String, UsersModel> userAddress = UsersDAO.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
					billAddress = userAddress.get("Bill");
					shipAddress = userAddress.get("Ship");
					if (shipAddress != null) {
						shipAddress.setOrderedBy(orderedBy);
					}else{
						shipAddress = selectShipAddressAsDefault(shipAddressList,session,shipAddress);
					}
					
					if (CommonUtility.customServiceUtility() != null && shipAddress != null) {
						String Shipvia = CommonUtility.customServiceUtility().getShipviaDetail(shipAddress.getZipCodeStringFormat());
						if (Shipvia != null) {
							shipAddress.setShipVia(Shipvia);
						}
					}// CustomServiceProvider
					
					String storeShipVia = CommonDBQuery.getSystemParamtersList().get("STORE_SHIPVIA_AT_LOGIN");
					if (CommonUtility.validateString(storeShipVia).equalsIgnoreCase("Y")) {
						shipViaList = (ArrayList<ShipVia>) session.getAttribute("shipViaList");
					} else {
						// QUESTION: this is available in commondbQuery from SHIPVIA_COST_TABLE why can't use same here 
						/*UsersModel customerInfoInput = new UsersModel();
						customerInfoInput.setUserToken(CommonUtility.validateString((String) session.getAttribute("userToken")));
						customerInfoInput.setUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY)));
						customerInfoInput.setBuyingCompanyId(CommonUtility.validateNumber((String) session.getAttribute("userBuyingCompanyId")));
						if (shipAddress != null) {
							customerInfoInput.setEntityId(CommonUtility.validateString(shipAddress.getEntityId()));
							customerInfoInput.setShipToId(CommonUtility.validateString(shipAddress.getShipToId()));
							customerInfoInput.setShipToName(CommonUtility.validateString(shipAddress.getShipToName()));
						}
						customerInfoInput.setShipToChanged(shipToChanged); // -------------- For ship default
						customerInfoInput.setSession(session);
						shipViaList = usersObj.getUserShipViaList(customerInfoInput); */
						//customerInfoInput.getShipViaList();//usersObj.getShipViaList(customerInfoInput);// //UsersDAO.getShipViaList()// //https://api.gerrie.com:8443/Help/Api/POST-api-1-Customer-Query
						shipViaList = CommonDBQuery.getSiteShipViaList();
						session.setAttribute("shipViaList", shipViaList);
					}

					if (shipAddress != null && shipAddress.getShipToId() != null && shipAddress.getShipToId().length() > 0) {
						session.setAttribute("selectedShipToId", shipAddress.getShipToId());
						UserManagement userObj = new UserManagementImpl();
						UsersModel userModelForERP = new UsersModel();
						userModelForERP.setSession(session);
						userModelForERP.setCustomerId(CommonUtility.validateString(customerNumber));
						userModelForERP.setShipToId(shipAddress.getShipToId());
						userModelForERP.setShipToChanged(shipToChanged); // -------------- For ship default
						userModelForERP.setSession(session);
						userModelForERP.setShipToWarehouseCode(shipToWarehouseCode);
						session.removeAttribute("salesRepIn");
						session.removeAttribute("salesRepOut");
						userObj.getCustomerDataFromERP(userModelForERP);
					}
					
					if(CommonUtility.customServiceUtility()!=null) {
						addressList = CommonUtility.customServiceUtility().getUserContactAddress(userId, session);
					}
					
					// taxId = UsersDAO.getTaxInfo(shipAddress.getEntityId());
					//acceptPo = UsersDAO.getAcceptPo(userId); //-- Commenting as this is not used in checkout page contentObject also been commented below
					//POValidStatus = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_PO_MANDATORY")); //-- Commenting as value has been directly assigned to contentObject 
					//Not used in checkout page hence commented
					//newsLetterSubscription = UsersDAO.getNewsLetterSubScripitonStatusCustomField(sessionUserId);
					//if (newsLetterSubscription != null) {
					//	session.setAttribute("newsLetter", newsLetterSubscription);
					//}

					if (session != null && session.getAttribute("orderStatusList") != null) {
						orderStatusList = (ArrayList<UsersModel>) session.getAttribute("orderStatusList");
					}
					session.setAttribute("defaultBillAddress", billAddress);
					session.setAttribute("defaultShipAddress", shipAddress);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (CommonUtility.validateString(type).equalsIgnoreCase("mobile")) {
					target = "CheckoutApp";
				} else {
					target = SUCCESS;
				}
			} else {
				target = "SESSIONEXPIRED";
			}
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_GUIDED_CHECKOUT")).equalsIgnoreCase("Y")){
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT")).equalsIgnoreCase("Y")) {
					contentObject.put("partialCartDisplay", "Y");
					contentObject = ProductsDAO.getShoppingCartDao(session, contentObject);
					if((Double)contentObject.get("cartTotal")==0.0) {
						 target = "redirectCart";
						 return target;
					}
					session.setAttribute("displayCheckoutCondMsgFlag","Y");
					session.setAttribute("vendorSpecificPurchaseMsg", "");
				}else {
					contentObject = ProductsDAO.getShoppingCartDao(session, contentObject);
				}
				
				contentObject.put("itemLevelShipSelected", contentObject.get("productListData"));
				contentObject.put("checkoutType", (String) request.getParameter("checkoutType"));
				cardDetails = SalesDAO.getCreditCardDetails(userId);
				contentObject.put("cardDetails", cardDetails);
				if (CommonUtility.validateString(isReOrder).equalsIgnoreCase("Y")) {
					contentObject = ProductsDAO.getQuoteCartDao(session, contentObject);
					contentObject.put("reorderitemLevelShipSelected", contentObject.get("productListData"));
				}
			}
			
			if (CommonUtility.validateString(wareHousecode).length() > 0) {
				contentObject.put("branchName", UsersDAO.getShipBranchName(wareHousecode));
			}

			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FETCH_WAREHOUSES_IN_SHOPPING_CART")).equalsIgnoreCase("Y")) {
				List<WarehouseModel> wareHouses = UsersDAO.getWareHouses();
				if(CommonUtility.customServiceUtility()!=null) {
					List<WarehouseModel> wareHousesTemp = CommonUtility.customServiceUtility().getGroupedWareHouses();
					if(wareHousesTemp!=null) {
						wareHouses = wareHousesTemp;
					}
				}
				contentObject.put("wareHouses", wareHouses);
				contentObject.put("wareHousesAsJson", gson.toJson(wareHouses));
			}

			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FETCH_TAX_IN_SHOPPING_CART")).equalsIgnoreCase("Y")) {
				Map<String, Object> details = new LinkedHashMap<>();
				details.put("wareHouseCode", (String) session.getAttribute("wareHouseCode")); // request.getParameter("pickUpWareHouseCode")
				details.put("customerERPId", customerNumber);
				UsersModel selectedShipAddr = null;
				if (request.getParameter("shipAddressBookId") != null) {
					int shipAddressBookId = Integer.parseInt(request.getParameter("shipAddressBookId"));
					for (UsersModel shipAddr : shipAddressList) {
						if (shipAddressBookId == shipAddr.getAddressBookId()) {
							selectedShipAddr = shipAddr;
							break;
						}
					}
				}
				
				if (selectedShipAddr == null) {
					selectedShipAddr = shipAddress;
				}
				
				List<ProductsModel> cartDetails = (ArrayList<ProductsModel>) contentObject.get("productListData");
				if(CommonUtility.customServiceUtility()!=null) {
					CommonUtility.customServiceUtility().enableDiscountCoupon(session,contentObject,shipVia,discountCoupons,details,cartDetails);
				}
				
				SalesModel createdShellOrder = SalesOrderManagementAction.createShellOrder(selectedShipAddr, billAddress, cartDetails, details);
				contentObject.put("createdShellOrder", createdShellOrder);
				contentObject.put("orderTax", createdShellOrder.getTax());	
				
				double orderDiscountVal =0.0D;
				if(details.get("totalSavingOnOrder") != null && CommonUtility.validateString(details.get("totalSavingOnOrder").toString()).length() >0) {
					contentObject.put("orderDiscount",details.get("totalSavingOnOrder"));
					orderDiscountVal = Double.valueOf(details.get("totalSavingOnOrder").toString());
				}
				if (cartDetails.size() > 0) {
					contentObject.put("grandTotal", ((createdShellOrder.getFreight()+createdShellOrder.getTax() + cartDetails.get(0).getCartTotal()) - orderDiscountVal));
				}
			}
			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("APPLY_RESTRICTION_OVER_SHIPVIAS")).equalsIgnoreCase("Y") && CommonUtility.customServiceUtility() != null) {
				// CustomServiceProvider
				List<ProductsModel> cartDetails = (ArrayList<ProductsModel>) contentObject.get("productListData");
				int generalSubsetId = Integer.parseInt(session.getAttribute("generalCatalog").toString());
				int subsetId = Integer.parseInt(session.getAttribute("userSubsetId").toString());
				CommonUtility.mergeCustomFields(cartDetails, subsetId, generalSubsetId);
				CommonUtility.customServiceUtility().applyShipViaRestriction(cartDetails, contentObject);
				// CustomServiceProvider
			} else {
				contentObject.put("SiteShipViaList", CommonDBQuery.getSiteShipViaList());
			}

			contentObject.put("shipAddressBookId", request.getParameter("shipAddressBookId"));
			contentObject.put("pickUpWareHouseCode", request.getParameter("pickUpWareHouseCode"));
			contentObject.put("selectedShipType", request.getParameter("selectedShipType"));
			contentObject.put("defaultShipVia", defaultShipVia);
			contentObject.put("addressList", addressList);
			contentObject.put("billAddressList", billAddressList);
			contentObject.put("billAddress", billAddress);
			contentObject.put("shipAddressList", shipAddressList);
			contentObject.put("shipAddressListAsJson", gson.toJson(shipAddressList));
			contentObject.put("shipAddress", shipAddress);
			contentObject.put("POValidStatus", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_PO_MANDATORY")));
			contentObject.put("poNumber", poNumber);
			contentObject.put("orderNotes", orderNotes);
			contentObject.put("shippingInstruction", shippingInstruction);
			contentObject.put("reqDate", reqDate);
			contentObject.put("shipViaList", shipViaList);
			contentObject.put("customerShipVia", customerShipVia);
			if (CommonUtility.validateString((String) session.getAttribute("customerShipViaDesc")).length()>0) {
				contentObject.put("customerShipViaDesc", CommonUtility.validateString((String) session.getAttribute("customerShipViaDesc")));
			}
			contentObject.put("savedGroupId", savedGroupId);
			contentObject.put("taxId", taxId);
			contentObject.put("defaultShipToId", defaultShipToId);
			contentObject.put("defaultBillToId", defaultBillToId);
			contentObject.put("orderStatusList", orderStatusList);
			contentObject.put("isCreditCardOnly", isCreditCardOnly);
			contentObject.put("itemAvailabilityStatus", itemAvailabilityStatus);
			//contentObject.put("acceptPo", acceptPo); //-- Commenting as this is not used in checkout page 
			contentObject.put("selectedShipCode", selectedShipCode);
			contentObject = CommonUtility.loadCenPOSParams(contentObject);
			if (CommonUtility.validateString(selectedShipCode).length() > 0 && shipViaList != null && !shipViaList.isEmpty()) {
				gson = new Gson();
				String shipViaJson = gson.toJson(shipViaList);
				session.setAttribute("customerShipViaListJson", shipViaJson);
			}
			
			/*if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_EMAIL_ID")).equalsIgnoreCase("Y")){
				checkEmailAddress(userId);
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_EMAIL_ID")).equalsIgnoreCase("Y")){
					UsersAction uAction = new UsersAction();
					uAction.checkEmailAddress(userId);
					if(CommonUtility.validateString((String) session.getAttribute("emailAddressExits")).equalsIgnoreCase("N")){
						contentObject.put("validEmailAddress", false);
					}
				}
			}*/
			
			contentObject.put("productListJson", gson.toJson(contentObject.get("productListData")));
			if(CommonUtility.validateString(isReOrder).length() < 1 && CommonUtility.validateString((String)session.getAttribute("isReOrder")).length()>0){
				isReOrder = CommonUtility.validateString((String) session.getAttribute("isReOrder"));
			}
			if (CommonUtility.validateString(isReOrder).equalsIgnoreCase("Y")) {
				session.setAttribute("isReOrder", isReOrder);
				contentObject.put("isReOrder", isReOrder);
				renderContent = LayoutGenerator.templateLoader("CheckOutQuotePage", contentObject, null, null, null);
			} else {
				session.setAttribute("isReOrder", "N");
				renderContent = LayoutGenerator.templateLoader("CheckOutPage", contentObject, null, null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return target;
	}

	public String updateQuoteCart() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try {
			if (session.getAttribute("quoteOrderId") != null)
				session.removeAttribute("quoteOrderId");
			/*
			 * if(session.getAttribute("editedAddressList")!=null)
			 * session.removeAttribute("editedAddressList");
			 */

			if (quotePartNumber != null && quotePartNumber.length > 0) {

				for (String qPno : quotePartNumber) {
					String qty = request.getParameter("qty_" + qPno);
					int iQty = 1;
					if (qty != null && !qty.trim().equalsIgnoreCase(""))
						iQty = CommonUtility.validateNumber(qty);

					if (iQty > 0) {
						UsersDAO.updateQuoteCartDao(iQty, qPno);
					} else {
						UsersDAO.deleteQuoteCartById(CommonUtility.validateNumber(qPno));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String approveAndSubmitCart() {
		
		 request =ServletActionContext.getRequest();
		 HttpSession session = request.getSession();
		
		 String listUserName = (String) session.getAttribute("listUserName");
		 HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId(listUserName,"Y");

		 String reqType= (String) request.getParameter("reqType");
		 String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		 int userId = CommonUtility.validateNumber(sessionUserId);
		 entityId = (String)session.getAttribute("billingEntityId");
		 String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		 String tempSubset = (String) session.getAttribute("userSubsetId");
		 if(userId>1){
				 
			 	try{
			 			
			 			System.out.println("assignedShipTo : "+request.getParameter("assignedShipTo"));
			 			UserManagement usersObj = new UserManagementImpl();
			 			/*SecureData validUserPass = new SecureData();
			 			UsersModel userInfo = new UsersModel();
			 			userInfo.setUserName(listUserName);
			 			userInfo.setPassword(validUserPass.validatePassword(password));
			 			userInfo.setSession(session);
			 			userInfo.setUserToken((String)session.getAttribute("userToken"));*/
			 			buyingCompanyId = (String) session.getAttribute("userBuyingCompanyId"); 
			 			/*String storeShipVia = CommonDBQuery.getSystemParamtersList().get("STORE_SHIPVIA_AT_LOGIN");	
						if(storeShipVia!=null && storeShipVia.trim().equalsIgnoreCase("Y")){
							shipViaList = (ArrayList<ShipVia>) session.getAttribute("shipViaList");
						}else{
							UsersModel customerInfoInput = new UsersModel();
							customerInfoInput.setUserToken((String)session.getAttribute("userToken"));
							customerInfoInput.setEntityId(CommonUtility.validateNumber(entityId));
							customerInfoInput.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
							customerInfoInput.setBuyingCompanyId(CommonUtility.validateNumber(buyingCompanyId));
							shipViaList = usersObj.getUserShipViaList(customerInfoInput);
						}
			 			*/
						
						//String shipViaDisplay = "";
						if(selectedShipMethod!=null && selectedShipMethod.trim().length()>0){
							session.setAttribute("selectedShipVia", selectedShipMethod);
							session.setAttribute("customerShipVia", selectedShipMethod);
					    }else{
					    	if(session.getAttribute("selectedBranchWillCall")!=null && session.getAttribute("selectedBranchWillCall").toString().trim().length()>0){
					    		String tempShip = (String) session.getAttribute("selectedBranchWillCall");
					    				String [] arry = tempShip.split("\\|");
					    				if(arry!=null && arry[1]!=null){
					    					selectedShipMethod = arry[1].toString();
					    					//shipViaDisplay = arry[2].toString();
					    					session.setAttribute("selectedShipVia", selectedShipMethod);
					    					session.setAttribute("customerShipVia", selectedShipMethod);
					    				}
					    		
					    	}
					    }
						
						customerShipVia = (String) session.getAttribute("customerShipVia");
						if(customerShipVia==null){
							customerShipVia="";
						}
						String storeShipVia = CommonDBQuery.getSystemParamtersList().get("STORE_SHIPVIA_AT_LOGIN");	
						if(storeShipVia!=null && storeShipVia.trim().equalsIgnoreCase("Y")){
							shipViaList = (ArrayList<ShipVia>) session.getAttribute("shipViaList");
						}else{
							UsersModel customerInfoInput = new UsersModel();
							customerInfoInput.setUserToken((String)session.getAttribute("userToken"));
							customerInfoInput.setEntityId(CommonUtility.validateString(entityId));
							customerInfoInput.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
							customerInfoInput.setBuyingCompanyId(CommonUtility.validateNumber((String) session.getAttribute("userBuyingCompanyId")));
							customerInfoInput.setSession(session);
							shipViaList = usersObj.getUserShipViaList(customerInfoInput);//customerInfoInput.getShipViaList();//usersObj.getShipViaList(customerInfoInput); //UsersDAO.getShipViaList() //https://api.gerrie.com:8443/Help/Api/POST-api-1-Customer-Query
							session.setAttribute("shipViaList", shipViaList);
						}
						
						if(assignedShipTo < 1 && session.getAttribute("assignedShipTo")!=null){
							assignedShipTo = (Integer)session.getAttribute("assignedShipTo");
						}
						if(assignedShipTo < 1 && session.getAttribute("defaultShipToId")!=null){
							assignedShipTo = CommonUtility.validateNumber((String)(session.getAttribute("defaultShipToId")));
						}
						
					 	defaultShipToId = assignedShipTo;
					    HashMap<String, Integer> userAddressId = null;
					 	if(defaultBillToId==0){
					 		userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook((String) session.getAttribute(Global.USERNAME_KEY));
					 		defaultBillToId = userAddressId.get("Bill");
					 	}
					 						 	
					 	if(defaultShipToId==0 && userAddressId!=null && userAddressId.get("Ship") != null){
					 		defaultShipToId = userAddressId.get("Ship");
					 	}else if (defaultShipToId==0 ) {
					 		userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook((String) session.getAttribute(Global.USERNAME_KEY));
					 		defaultShipToId = userAddressId.get("Ship");
						}
					 	session.setAttribute("assignedShipTo", assignedShipTo);
					 	
					 	
					 	HashMap<String, ArrayList<UsersModel>> userAddressList =UsersDAO.getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId), userId);
					 	billAddressList = userAddressList.get("Bill");
					 	shipAddressList = userAddressList.get("Ship");
						
					 	//AdaptPharma shipping address list start
					 	if(shipAddressList.size() > 1 && CommonUtility.customServiceUtility() !=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MultipleShipToAddressCartAP")).equalsIgnoreCase("Y")) {
					 		shipAddressList = CommonUtility.customServiceUtility().addShipAddressList(shipAddressList);
					 	}
					 	//AdaptPharma shipping address list end
					 	
					 	HashMap<String, UsersModel> userAddress = UsersDAO.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
						billAddress = userAddress.get("Bill");
						shipAddress = userAddress.get("Ship");
						
						if(shipAddress!= null) {
							taxId = UsersDAO.getTaxInfo(shipAddress.getEntityId());
						}else{
							shipAddress = selectShipAddressAsDefault(shipAddressList,session,shipAddress);
						}
						acceptPo = UsersDAO.getAcceptPo(userId);
						
						newsLetterSubscription = UsersDAO.getNewsLetterSubScripitonStatusCustomField(Integer.toString(userId));
						
						System.out.println("News Letter subscrption ::--------------------" +newsLetterSubscription);
						
						if(newsLetterSubscription!=null )
						{
							session.setAttribute("newsLetter", newsLetterSubscription);
						}
						LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
						if(CommonDBQuery.getSystemParamtersList().get("ENABLE_GUIDED_CHECKOUT")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_GUIDED_CHECKOUT").trim().equalsIgnoreCase("Y")){
					 		cardDetails = SalesDAO.getCreditCardDetails(userId);
					 		contentObject.put("cardDetails", cardDetails);
						}
						Gson gson = new Gson();
						contentObject.put("shipAddressBookId", request.getParameter("shipAddressBookId"));
						 contentObject.put("assignedShipTo", assignedShipTo);
						 contentObject.put("defaultShipToId", defaultShipToId);
						 contentObject.put("defaultBillToId", defaultBillToId);
						 contentObject.put("billAddressList", billAddressList);
						 contentObject.put("billAddress", billAddress);
						 contentObject.put("shipAddressList", shipAddressList);
						 contentObject.put("shipAddressListAsJson", gson.toJson(shipAddressList));
						 contentObject.put("shipAddress", shipAddress);
						 contentObject.put("POValidStatus", POValidStatus);
						 contentObject.put("poNumber", poNumber);
						 contentObject.put("orderNotes", orderNotes);
						 contentObject.put("shippingInstruction", shippingInstruction);
						 contentObject.put("reqDate", reqDate);
						 contentObject.put("shipViaList", shipViaList);
						 contentObject.put("customerShipVia", customerShipVia);
						 if(session.getAttribute("customerShipViaDesc")!=null){
							 contentObject.put("customerShipViaDesc", CommonUtility.validateString(session.getAttribute("customerShipViaDesc").toString()));
						 }
						 contentObject.put("savedGroupId", savedGroupId);
						 contentObject.put("taxId", taxId);
						 contentObject.put("reqType", reqType);
						 contentObject.put("subsetId", tempSubset);
						 contentObject.put("generalsubsetId", tempGeneralSubset);
						 contentObject.put("savedGroupId", savedGroupId);
						 contentObject.put("userId", userId);
						 contentObject.put("buyingCompanyId", buyingCompanyId);
						 contentObject = ProductsDAO.getApproveCartDao(session, contentObject);
						
						if(CommonDBQuery.getSystemParamtersList().get("FETCH_WAREHOUSES_IN_SHOPPING_CART") != null && CommonDBQuery.getSystemParamtersList().get("FETCH_WAREHOUSES_IN_SHOPPING_CART").equals("Y")) {
							List<WarehouseModel> wareHouses = UsersDAO.getWareHouses();
							contentObject.put("wareHouses", wareHouses);
							contentObject.put("wareHousesAsJson", gson.toJson(wareHouses));
						}
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FETCH_TAX_IN_SHOPPING_CART")).equals("Y")) {
							Map<String, Object> details = new LinkedHashMap<>();
							details.put("wareHouseCode", session.getAttribute("wareHouseCode").toString());  //request.getParameter("pickUpWareHouseCode")
							details.put("customerERPId", session.getAttribute("customerId").toString());
							UsersModel selectedShipAddr = null;
							if(request.getParameter("shipAddressBookId") != null) {
								int shipAddressBookId = Integer.parseInt(request.getParameter("shipAddressBookId"));
								for(UsersModel shipAddr : shipAddressList) {
									if(shipAddressBookId == shipAddr.getAddressBookId()){
										selectedShipAddr = shipAddr;
										break;
									}
								}
							}
							if(shipAddress == null){
								shipAddress = shipAddressList.get(0);
							}

							List<ProductsModel> cartDetails = (ArrayList<ProductsModel>) contentObject.get("productListData");
							if(CommonUtility.customServiceUtility()!=null) {
								CommonUtility.customServiceUtility().enableDiscountCoupon(session,contentObject,shipVia,discountCoupons,details,cartDetails);
								CommonUtility.customServiceUtility().orderFreightcalculation(details,cartDetails);
								if(details.get("otherCharges") != null && CommonUtility.validateString(details.get("otherCharges").toString()).length() >0) {
										contentObject.put("freight", details.get("otherCharges"));
									}
								} 
								SalesModel createdShellOrder = SalesOrderManagementAction.createShellOrder(selectedShipAddr,billAddress, cartDetails, details);
								contentObject.put("createdShellOrder", createdShellOrder);
								contentObject.put("orderTax", createdShellOrder.getTax());	
								double orderDiscountVal =0.0D;
								if(details.get("totalSavingOnOrder") != null && CommonUtility.validateString(details.get("totalSavingOnOrder").toString()).length() >0) {
									contentObject.put("orderDiscount",details.get("totalSavingOnOrder"));
									orderDiscountVal = Double.valueOf(details.get("totalSavingOnOrder").toString());
								}
								if (cartDetails.size() > 0) {
								contentObject.put("grandTotal", ((createdShellOrder.getFreight()+createdShellOrder.getTax() + cartDetails.get(0).getCartTotal()) - orderDiscountVal));
								}
						}
						
						
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("APPLY_RESTRICTION_OVER_SHIPVIAS")).equals("Y")) {
							//CustomServiceProvider
							if(CommonUtility.customServiceUtility()!=null) {
								List<ProductsModel> cartDetails = (ArrayList<ProductsModel>)contentObject.get("productListData");
								int generalSubsetId = Integer.parseInt(session.getAttribute("generalCatalog").toString()); 
								int subsetId = Integer.parseInt(session.getAttribute("userSubsetId").toString());
								CommonUtility.mergeCustomFields(cartDetails, subsetId, generalSubsetId);
								CommonUtility.customServiceUtility().applyShipViaRestriction(cartDetails, contentObject);
							}
							//CustomServiceProvider
						}else {
							contentObject.put("SiteShipViaList", CommonDBQuery.getSiteShipViaList());
						}
						 contentObject.put("productListJson", gson.toJson(contentObject.get("productListData")));
						 contentObject.put("approveCartCount", contentObject.get("selectQuoteCartCount"));
						 contentObject.put("approveCartCountTotal", contentObject.get("cartTotal"));
						 contentObject.put("itemLevelShipSelected", contentObject.get("productListData"));
						 contentObject.put("generalUserDetails", userDetails);
						 contentObject.put("acceptPo", acceptPo);
						 contentObject.put("itemAvailabilityStatus",itemAvailabilityStatus);
					     renderContent = LayoutGenerator.templateLoader("CheckOutPage", contentObject , null, null, null);
					}
			catch(Exception e){
				e.printStackTrace();
			}
		      return SUCCESS;
		}else{
			 return "SESSIONEXPIRED";
		 }
	}

	public String approveCart(){
		target = "ResultLoader";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String result = "0";
		requestTokenId=CommonUtility.validateNumber(request.getParameter("requestTokenId"));
		if(CommonUtility.validateString(alwaysApprover).length()  >0){
			ArrayList<UsersModel> approverGroupList = UsersDAO.getApproverGroupList(requestTokenId, CommonUtility.validateNumber(approveSenderid));
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			if(approverGroupList.size() > 0){
				int loginUserDeq = 0;
				/*ArrayList<Integer> listofPendingAppSequence = new ArrayList<Integer>();*/
				// Finding the login User Sequence Level 
				for(UsersModel obj : approverGroupList ){
					if(obj.getUserId() == CommonUtility.validateNumber(sessionUserId)){
						loginUserDeq =  CommonUtility.validateNumber(obj.getApproverSequence());
					}
				}
				int nextLevelApprover = 0;
				//finding Next level of approvers. For example : Sequence : 1,2 6,5,7  - 
				//if login user sequence 5, and next nearest approver is 6. for that particular user, sending reminder mail. 
				for(UsersModel obj : approverGroupList ){
					int sequenceLevel = CommonUtility.validateNumber(obj.getApproverSequence());
					if(loginUserDeq != 0 && sequenceLevel > loginUserDeq && obj.getUserId() != CommonUtility.validateNumber(sessionUserId)){
						if(nextLevelApprover ==0 ){
							if(loginUserDeq < sequenceLevel){
								nextLevelApprover = sequenceLevel;
							}
						}else if(nextLevelApprover  != 0){
							if(nextLevelApprover > sequenceLevel ){
								nextLevelApprover = sequenceLevel;
							}
						}
						/*listofPendingAppSequence.add(obj.getApproveCartGroupId());*/
					}
				}
				//Sending mail for  next level approver to approve the cart
				if(nextLevelApprover  != 0){
					System.out.println("Next Level Approver : " + nextLevelApprover);
					ProductsDAO.updateNextLevelAppCartGroupItems(session, lineItemIdList, shoppingCartQty, idList, savedGroupId, requestTokenId, approveSenderid);
					for(UsersModel obj : approverGroupList ){
						int sequenceLevel = CommonUtility.validateNumber(obj.getApproverSequence());
						if(nextLevelApprover == sequenceLevel){
							result = SalesDAO.approveCartBySequenceLevelDao(session, requestTokenId, approveSenderid, nextLevelApprover,loginUserDeq,approverComment);
							renderContent =result;
							System.out.println("Result : " + result);
						}

					}
					if(result== "" || CommonUtility.validateString(result).length() == 0){
						renderContent = "0|";
					}
				}

			}
		}
		renderContent = result;
		return target;
	}

	public String ManageUser() {
		try {
			System.out.println(userId);
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			agentUserList = new ArrayList<UsersModel>();
			String ListofAssigned = UsersDAO.getListAssignedAPAtoUser(Integer.toString(userId));
			if (ListofAssigned.equalsIgnoreCase("0")) {
				authoListAssigned = ListofAssigned;
			} else {
				authoListAssigned = "1";
			}
			buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			addressList = new UsersModel();
			addressList = UsersDAO.getEntityDetailsByUserId(userId);
			String userRole = UsersDAO.getUserRole(userId);
			isSuperUser = "N";
			isAuthPurchaseAgent = "N";
			if (userRole != null && userRole.trim().equalsIgnoreCase("Ecomm Customer Super User")) {
				isSuperUser = "Y";
			}

			else if (userRole != null && userRole.trim().equalsIgnoreCase("Ecomm Customer Auth Purchase Agent")) {
				isAuthPurchaseAgent = "Y";
			}
			shipAddressList = new ArrayList<UsersModel>();

			// HashMap<String, ArrayList<UsersModel>> templist =
			// UsersDAO.getAgentAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId),"");
			// // Before copying from NCE May 09
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			HashMap<String, ArrayList<UsersModel>> templist = UsersDAO
					.getAgentAddressListFromBCAddressBook(CommonUtility.validateNumber(sessionUserId), "");

			shipAddressList = templist.get("Ship");
			assignedShipIdList = new ArrayList<Integer>();
			assignedShipIdList = UsersDAO.getAssignedShipToList(String.valueOf(userId));
			agentUserList = UsersDAO.getAgentDetailsForAPA(ListofAssigned, userId, userRole);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("agentUserList", agentUserList);
			contentObject.put("isSuperUser", isSuperUser);
			contentObject.put("isAuthPurchaseAgent", isAuthPurchaseAgent);
			contentObject.put("shipAddressList", shipAddressList);
			contentObject.put("assignedShipIdList", assignedShipIdList);
			contentObject.put("buyingCompanyId", buyingCompanyId);
			contentObject.put("addressList", addressList);
			renderContent = LayoutGenerator.templateLoader("ManageContact", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;

	}

	public String loadMPA() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			ArrayList<UsersModel> agentUserList = UsersDAO.getDataForMPA(CommonUtility.validateNumber(buyingCompanyId),
					authoListAssigned);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("agentUserList", agentUserList);
			renderContent = LayoutGenerator.templateLoader("ManageContact", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String DashBoard() {
		long startTimer = CommonUtility.startTimeDispaly();
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String sSubsetId = (String) session.getAttribute("userSubsetId");
		String parentId = (String) session.getAttribute("parentUserId");
		UserManagement usersObj = new UserManagementImpl();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		String tempSubset = (String) session.getAttribute("userSubsetId");
		String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		int subsetId = Integer.parseInt(tempSubset);
		int generalSubsetId = Integer.parseInt(tempGeneralSubset);
		String isPunchoutUser =  CommonUtility.validateString((String) session.getAttribute("isPunchoutUser"));
		String dashboadForPunchoutUsers = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_DASHBOARD_FOR_PUNCHOUT_USERS"));


		try {
			if (userId > 1 && (!isPunchoutUser.equalsIgnoreCase("Y") || dashboadForPunchoutUsers.equalsIgnoreCase("Y"))){
				if(CommonUtility.customServiceUtility() != null) {
					defaultShipToId = CommonUtility.validateNumber(CommonUtility.customServiceUtility().getEntityShipToId(session, CommonUtility.validateParseIntegerToString(defaultShipToId), (String)session.getAttribute("shipEntityId")));
				}
				if (defaultShipToId > 0) {
					session.setAttribute("defaultShipToId", Integer.toString(defaultShipToId));
				}
				String tempdefaultShipId = (String)session.getAttribute("defaultShipToId");
				String tempdefaultBillToId = (String)session.getAttribute("defaultBillToId");
				buyingCompanyId = (String) session.getAttribute("buyingCompanyId");

				addressList = new UsersModel();
				addressList = UsersDAO.getEntityDetailsByUserId(userId);
				System.out.println("check:::" + addressList.getCountry());

				if (tempdefaultBillToId != null && tempdefaultBillToId.trim().length() > 0)
					defaultBillToId = (CommonUtility.validateNumber(tempdefaultBillToId));
				if (tempdefaultShipId != null && tempdefaultShipId.trim().length() > 0)
					defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);

				if (defaultBillToId == 0 || defaultShipToId == 0) {
					HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook((String) session.getAttribute(Global.USERNAME_KEY));
					// UsersDAO.getDefaultAddressId(userId);
					defaultBillToId = userAddressId.get("Bill");
					defaultShipToId = userAddressId.get("Ship");
				}

				HashMap<String, ArrayList<UsersModel>> userAddressList = usersObj.getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId), userId);
				// UsersDAO.getAddressList(userId);
				billAddressList = userAddressList.get("Bill");
				shipAddressList = userAddressList.get("Ship");
				if (parentId != null && parentId.trim().length() > 0 && CommonUtility.validateNumber(parentId) > 0) {
					// HashMap<String, ArrayList<UsersModel>> shipList =
					// UsersDAO.getAgentAddressListFromBCAddressBook(CommonUtility.validateNumber(sessionUserId),"");
					UsersModel model = new UsersModel();
					model.setUserId(CommonUtility.validateNumber(sessionUserId));
					model.setSession(session);
					HashMap<String, ArrayList<UsersModel>> shipList = usersObj.getAgentAddressListFromBCAddressBook(model);
					if (shipList.size() != 0) {
						shipAddressList = new ArrayList<UsersModel>();
						shipAddressList = shipList.get("Ship");
					}

				}
				UnilogFactoryInterface serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
				if (serviceClass != null && shipAddressList != null) {
					ArrayList<UsersModel> shipList = serviceClass.addShipAddressList(shipAddressList);
					if (shipList != null) {
						shipAddressList = shipList;
					}
				}
				// HashMap<String, UsersModel> userAddress =
				// usersObj.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
				UserManagement userObj = new UserManagementImpl();
				HashMap<String, UsersModel> userAddress = userObj.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
				billAddress = userAddress.get("Bill");
				shipAddress = userAddress.get("Ship");
				userAccountDetail = new UsersModel();
				LinkedHashMap<String, String> accountInquiryInput = new LinkedHashMap<String, String>();
				accountInquiryInput.put("userToken", (String) session.getAttribute("userToken"));
				accountInquiryInput.put("userName", (String) session.getAttribute(Global.USERNAME_KEY));
				accountInquiryInput.put("entityId", userAddress.get("Bill") != null ? userAddress.get("Bill").getEntityId() : "");
				UserManagement userManagement = new UserManagementImpl();
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ACCOUNT_RECEIVABLE_BALANCE_SUMMARY")).length() > 0) {
					userAccountDetail = userManagement.getAccountDetail(accountInquiryInput);
				}
				System.out.println(userAccountDetail.getArCreditAvail() + "****" + userAccountDetail.getArCreditLimit());
				
				//=================================
				newsLetterSubscription = UsersDAO.getNewsLetterSubScripitonStatusCustomField(sessionUserId);
				System.out.println("News Letter subscrption ::--------------------" + newsLetterSubscription);
				if (newsLetterSubscription != null) {
					session.setAttribute("newsLetter", newsLetterSubscription);
				}
				//=================================
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DashboardEnableLastLoginDetails")).equalsIgnoreCase("Y")) {
					LinkedHashMap<String, String> lastLogin = UsersDAO.lastLogin(userId);
					contentObject.put("lastLogin", lastLogin);
				}
				//=================================
				//LinkedHashMap<String, String> LoggedInLoggedoutcount = UsersDAO.numberOfTimesLoggedInLoggedout(userId);
				//=================================
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DashboardRecentlyOrderedItems")).equalsIgnoreCase("Y")) {
					// updated for subset and general subset id
					ArrayList<ProductsModel> recentlyOrderedItems = ProductsDAO.getRecentlyOrderedItems(userId, subsetId, generalSubsetId);
					if (recentlyOrderedItems != null && recentlyOrderedItems.size() > 0) {
						ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
						ArrayList<String> itemIds = new ArrayList<>();
						for (ProductsModel eachProduct : recentlyOrderedItems) {
							itemIds.add(String.valueOf(eachProduct.getItemId()));
							partIdentifierQuantity.add(eachProduct.getItemId());
						}
						if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CPN_FOR_RECENTLY_PURCHASED_ITEMS")).equalsIgnoreCase("Y")) {
							LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumbers = ProductHunterSolr.getcustomerPartnumber(StringUtils.join(itemIds, " OR "), Integer.parseInt(buyingCompanyId), Integer.parseInt(buyingCompanyId));
							contentObject.put("customerPartNumbers", customerPartNumbers);
						}
						if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PRICE_ENQUIRY_FOR_RECENT_ORDERED_ITEMS")).equalsIgnoreCase("Y")) {
							ProductManagementModel productManagement = new ProductManagementModel();
							ProductManagement priceInquiry = new ProductManagementImpl();
							productManagement.setPartIdentifier(recentlyOrderedItems);
							productManagement.setPartIdentifierQuantity(partIdentifierQuantity);
							recentlyOrderedItems = priceInquiry.priceInquiry(productManagement, recentlyOrderedItems);
						}
					}
					contentObject.put("recentlyOrderedItems", recentlyOrderedItems);
				}
				//=================================
				contentObject.put("session", session);
				contentObject = ProductsDAO.getSavedGroupNameByUserIdDao(userId, contentObject,CommonUtility.validateNumber(buyingCompanyId));
				//contentObject.put("LoggedInLoggedoutcount", LoggedInLoggedoutcount);
				contentObject.put("addressList", addressList);
				contentObject.put("defaultShipToId", defaultShipToId);
				contentObject.put("defaultBillToId", defaultBillToId);
				contentObject.put("billAddressList", billAddressList);
				contentObject.put("shipAddressList", shipAddressList);
				contentObject.put("billAddress", billAddress);
				contentObject.put("shipAddress", shipAddress);
				contentObject.put("userAccountDetail", userAccountDetail);
				contentObject.put("newsLetterSubscription", newsLetterSubscription);
				renderContent = LayoutGenerator.templateLoader("DashBoardPage", contentObject, null, null, null);

				target = SUCCESS;

			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return target;
	}

	public String UserAdress() {

		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		// String customerId = (String)session.getAttribute("custId");
		String userName = (String) session.getAttribute(Global.USERNAME_KEY);

		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		UserManagement usersObj = new UserManagementImpl();

		int userId = CommonUtility.validateNumber(sessionUserId);

		try {

			if (userId > 1) {

				if (defaultBillToId == 0) {
					HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(userName);// customerId
					defaultBillToId = userAddressId.get("Bill");
					defaultShipToId = userAddressId.get("Ship");
				}
				HashMap<String, ArrayList<UsersModel>> userAddressList = usersObj
						.getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId), userId);
				billAddressList = userAddressList.get("Bill");
				shipAddressList = userAddressList.get("Ship");

				HashMap<String, UsersModel> userAddress = usersObj.getUserAddressFromBCAddressBook(defaultBillToId,defaultShipToId);
				if(billAddressList!=null) {
					billAddress = billAddressList.get(0);
				}
				if(userAddress!=null) {
					shipAddress = userAddress.get("Ship");
				}

				String loginMessage = "";
				if (session.getAttribute("userRegMessage") != null
						&& session.getAttribute("userRegMessage").toString().trim().equalsIgnoreCase("true")) {
					loginMessage = CommonDBQuery.getSystemParamtersList().get("USER_REGISTRATION_MESSAGE");
				}
				if (!CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("ASSIGN_USEREMAIL_TOBILLINGADDRESS"))
						.equalsIgnoreCase("N") && billAddress!=null) {
					UsersModel userDetail = UsersDAO.getUserEmail(userId);
					billAddress.setEmailAddress(userDetail.getEmailAddress());
				}
				addressList = new UsersModel();
				addressList = UsersDAO.getEntityDetailsByUserId(userId);

				newsLetterSubscription = UsersDAO.getNewsLetterSubScripitonStatusCustomField(sessionUserId);
				if (newsLetterSubscription != null) {
					session.setAttribute("newsLetter", newsLetterSubscription);
				}
				System.out.println("News Letter subscrption ::--------------------" + newsLetterSubscription);

				contentObject.put("buyingCompanyId", buyingCompanyId);
				contentObject.put("addressList", addressList);
				contentObject.put("defaultBillToId", defaultBillToId);
				contentObject.put("defaultShipToId", defaultShipToId);
				contentObject.put("billAddressList", billAddressList);
				contentObject.put("shipAddressList", shipAddressList);
				contentObject.put("billAddress", billAddress);
				contentObject.put("shipAddress", shipAddress);
				contentObject.put("loginMessage", loginMessage);
				contentObject.put("newsLetterSubscription", newsLetterSubscription);

				if (type != null && (type.trim().equalsIgnoreCase("bill") || type.trim().equalsIgnoreCase("ship"))) {
					contentObject.put("type", type);
					contentObject.put("addressBookId", addressBookId);
					renderContent = LayoutGenerator.templateLoader("EditAddressPage", contentObject, null, null, null);
				} else {
					renderContent = LayoutGenerator.templateLoader("AddresssesPage", contentObject, null, null, null);
				}
				target = SUCCESS;

			} else {

				target = "SESSIONEXPIRED";

			}

		} catch (Exception e) {
			e.printStackTrace();
			target = "SESSIONEXPIRED";
		}
		return target;
	}

	public String UserContact() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		int userId = CommonUtility.validateNumber(sessionUserId);
		try {
			if (userId > 1) {
				UsersModel userContactInfo = UsersDAO
						.getUserContactInformation(CommonUtility.validateNumber(sessionUserId));
				newsLetterSubscription = UsersDAO.getNewsLetterSubScripitonStatusCustomField(sessionUserId);
				if (newsLetterSubscription != null) {
					session.setAttribute("newsLetter", newsLetterSubscription);
				}
				System.out.println("News Letter subscrption ::--------------------" + newsLetterSubscription);
				contentObject.put("buyingCompanyId", buyingCompanyId);
				contentObject.put("userContactInfo", userContactInfo);
				contentObject.put("newsLetterSubscription", newsLetterSubscription);
				renderContent = LayoutGenerator.templateLoader("ContactInformationPage", contentObject, null, null,
						null);
				target = SUCCESS;
			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
			target = "SESSIONEXPIRED";
		}
		return target;
	}

	public String ChangePassword() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		target = SUCCESS;
		try {

			if (userId > 1) {
				addressList = new UsersModel();
				addressList = UsersDAO.getEntityDetailsByUserId(userId);
				if (oldPassword.equals(addressList.getPassword())) {
					if (newPassword != null && !newPassword.trim().equalsIgnoreCase("")) {
						if (oldPassword.equals(newPassword)) {
							result = "0|Old Password and New password are similar";
						} else {
							ArrayList<CreditCardModel> creditCardList = new ArrayList<CreditCardModel>();
							UserManagement usersObj = new UserManagementImpl();
							SendMailUtility sendMailUtility = new SendMailUtility();
							SendMailModel sendMailModel = new SendMailModel();
							sendMailModel.setUserName(addressList.getUserName());
							sendMailModel.setPassword(newPassword);
							sendMailModel.setToEmailId(addressList.getEmailAddress());
							sendMailModel.setFirstName(addressList.getFirstName());
							sendMailModel.setLastName(addressList.getLastName());
							creditCardList = SalesDAO.getCreditCardDetails(userId);
							UsersModel userInfo = new UsersModel();
							userInfo.setUserName(addressList.getUserName());
							userInfo.setPassword(newPassword);
							userInfo.setIsCreditCard("No");
							userInfo.setCurrentPassword(SecureData.getPunchoutSecurePassword(oldPassword));
							userInfo.setCreditCardList(creditCardList);
							userInfo.setErpOverrideFlag(CommonUtility.validateString(request.getParameter("erpOverrideFlag")));
							userInfo.setUserId(userId);
							if(CommonUtility.validateString(request.getParameter("cimmesbOverrideFlag")).equalsIgnoreCase("Y")) {
								result = usersObj.updatePassword(userInfo, session);
							}
							else {
							result = usersObj.contactUpdate(userInfo, session);
							}
							if (result.contains("successfully") || result.equalsIgnoreCase(newPassword)) {
                                int count=0;
                                if( CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SINGLE_USER_UNDER_MULTIPLE_CUSTOMER")).equalsIgnoreCase("Y")) {
                                     count = UsersDAO.updatePasswordWithUserName(addressList.getUserName(), newPassword);
                                }else {
                                     count = UsersDAO.updatePassword(userId, newPassword);
                                }								
								if (count >= 1) {
									session.setAttribute("password", SecureData.getPunchoutSecurePassword(newPassword));
									boolean flag = sendMailUtility.buildChangePasswordMail(sendMailModel);
									result = "1|Password Updated Successfully";
								}
							} else {
								result = "0|Password Updated Failed.";
							}
						}
					} else {
						result = "0|Password Updated Failed.";
					}
				} else {
					result = "0|Invalid Old Password.";
				}
				renderContent = result;
			} else {
				target = "SESSIONEXPIRED";
			}

		} catch (Exception e) {
			e.printStackTrace();
			target = "SESSIONEXPIRED";
		}
		return target;

	}

	public String PurchaseAgent() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			int userId = CommonUtility.validateNumber(sessionUserId);
			if (userId > 1) {
				renderContent = LayoutGenerator.templateLoader("AddNewPurchaseAgentPage", null, null, null, null);
				target = SUCCESS;
			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String ManageAgent() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			ArrayList<String> paContactTitles = new ArrayList<String>();

			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);

			if (userId > 1) {
				contentObject = UsersDAO.agentListDAO(session, contentObject);
				renderContent = LayoutGenerator.templateLoader("ManagePurchaseAgentPage", contentObject, null, null,null);
				target = SUCCESS;

			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String DisableList() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);

			if (userId > 1) {

				contentObject = UsersDAO.disableListDAO(userId, session, contentObject);
				renderContent = LayoutGenerator.templateLoader("DisablePurchaseAgentPage", contentObject, null, null,
						null);
				target = SUCCESS;

			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String AgentDetails() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userIdCurrent = CommonUtility.validateNumber(sessionUserId);
			
			if (userIdCurrent > 1) {

				contentObject = UsersDAO.manageAgentDAO(userId, session, contentObject);
				List<String> userRoles=UsersDAO.getAllUserRoles();
				contentObject.put("userRoles", userRoles);
				renderContent = LayoutGenerator.templateLoader("PurchaseAgentDetailPage", contentObject, null, null,null);
				target = SUCCESS;

			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String DisableAgent() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userIdCurrent = CommonUtility.validateNumber(sessionUserId);
			String userIdString = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(request.getParameter("userId"));
			if (userId > 1) {
				if (userIdCurrent > 1) {
					renderContent = UsersDAO.disableAgentDAO(userId);
					target = "ResultLoader";
				} else {
					target = "SESSIONEXPIRED";
				}
				return target;
			} else {
				renderContent = "sessionexpired";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String EnableAgent() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userIdCurrent = CommonUtility.validateNumber(sessionUserId);
			String userIdString = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(request.getParameter("userId"));
			if (userId == 0) {
				userId = CommonUtility.validateNumber(userIdString);
			}
			if (userId > 1) {
				if (userIdCurrent > 1) {
					renderContent = UsersDAO.enableAgentDAO(userId);
					target = "ResultLoader";
				} else {
					target = "SESSIONEXPIRED";
				}
				return target;
			} else {
				renderContent = "sessionexpired";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String DisableAgentAuthorization() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();

			UsersModel user = UsersDAO.getEntityDetailsByUserId(userId);
			ArrayList<UsersModel> assignedShipList = new ArrayList<UsersModel>();
			ArrayList<String> status = new ArrayList<String>();
			UsersDAO.checkAndUpdateRoleToUser(String.valueOf(userId), "N", "Ecomm Customer General User");
			int update = 1;
			if (update > 0) {

				status.add(LayoutLoader.getMessageProperties()
						.get(session.getAttribute("localeCode").toString().toUpperCase())
						.getProperty("purchaseagent.status.privilegeremoved"));
				SendMailUtility sendMailObj = new SendMailUtility();
				boolean flagsent = sendMailObj.sendChangedPrivilegsMail(user.getEmailAddress(), user.getFirstName(),
						user.getLastName(), status, assignedShipList, assignedShipList);

				if (flagsent) {
					System.out.println("mail sent successfully");
				}
				result = LayoutLoader.getMessageProperties()
						.get(session.getAttribute("localeCode").toString().toUpperCase())
						.getProperty("purchaseagent.status.privilegeremovedfor") + " " + user.getFirstName() + " "
						+ CommonUtility.validateString(user.getLastName());

			} else {
				result = LayoutLoader.getMessageProperties()
						.get(session.getAttribute("localeCode").toString().toUpperCase())
						.getProperty("purchaseagent.status.updateunsuccessful");
			}
			renderContent = result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ResultLoader";
	}

	public String AddNewPurchaseAgent() {		
		result = "";	
	try{
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		int parentUserId = CommonUtility.validateNumber(sessionUserId);
		AddressModel userRegistrationDetail = new AddressModel();
		UserManagement usersObj = new UserManagementImpl();
		boolean isValidUser = true;
		String emailId = contactemailAddress;
		
		int userId = CommonUtility.validateNumber(sessionUserId);
		if(userId>1){
			
			//Backend Validation for add new user
			if(CommonUtility.customServiceUtility() != null) {
				String validated_result = CommonUtility.customServiceUtility().validateNewPurchaseAgent(this);
				if(!CommonUtility.validateString(validated_result).isEmpty()) {
					/*for(String st:validated_result.split("\\|")) {
						System.out.println(st);
					}*/
					result = validated_result;
					isValidUser = false;
				}
			}
			
		/*if(contactemailAddress.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Email Address.|";
		}else{
			if(!CommonUtility.validateEmail(contactemailAddress)){
				isValidUser=false; result = result + "Please enter valid email address.|";
			}/*else if(UsersDAO.isRegisteredUser(contactemailAddress)){
				isValidUser=false; result = result + "User Already registered with this email.|";
			}
		}
		if(contactFirstName.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter First Name.|";}
		if(contactLastName.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Last Name.|";}
		
		if(password.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Password.|";
		}else{ 
			if(password.trim().length()<8){isValidUser=false; result = result + "Password should have at least 8 characters.|";}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
				if(!NewUserRegisterUtility.isAlfaNumericOnly(password)){
					isValidUser=false; result = result + "Special characters are not allowed and password can have alphabets and numbers.|";
				}
			}
		
		}
		if(confirmPassword.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Confirm Password.|";}
		if(!password.trim().equalsIgnoreCase("")){
		
			if(!confirmPassword.trim().equalsIgnoreCase("")){
				
				if(password.trim().compareTo(confirmPassword)!=0){
					isValidUser=false; result = result + "Password does not match with Confirm Password.|";
				}
			}
		}
		if(contactAddress1.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Address 1|";}
		if(contactCity.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter City.|";}
		if(contactCountry.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please select Country.|";}
		
		if(request.getParameterMap().containsKey("contactState") && CommonUtility.validateString(contactState).length()==0){
			isValidUser=false; result = result + "Please select/enter State.|";
		}
		if(request.getParameterMap().containsKey("state") && CommonUtility.validateString(state).length()==0){
			isValidUser=false; result = result + "Please select/enter State.|";
		}
		
		
		if(zip.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Zipcode.|";}
		if(contactPhone.trim().equalsIgnoreCase("")){
			isValidUser=false; result = result + "Please enter Phone Number.|";
		}else{
			if(!CommonUtility.validatePhoneNumber(contactPhone)){
				isValidUser=false; result = result + "Please enter valid Phone Number (e.g. 123-456-7896,(954) 555-1234, 4561237896).|";
			}else{
				contactPhone = contactPhone.replaceAll("[^0-9]", "");
			}
		}
		
		if(newApaPrivacyAndTermsCheckBoxRequired!=null && newApaPrivacyAndTermsCheckBoxRequired.trim().equalsIgnoreCase("Y")){
			 if(newApaPrivacyAndTermsCheckBox==null || newApaPrivacyAndTermsCheckBox.trim().equalsIgnoreCase("")){
				 isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("registration.Privacy.Policy.and.Terms.and.Conditions")+"|";}
		 }*/
			if(CommonUtility.customServiceUtility()!=null) {
				isValidUser = CommonUtility.customServiceUtility().validateEmailAddress(isValidUser, contactemailAddress);
				if(!isValidUser) {
					result = result + "0|" + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.validEmailAddress")+"|";
				}
			 }		
			
		if(isValidUser){
			
			if(parentUserId>1){
				
				String entityId = null;
				if(session!=null && session.getAttribute("entityId")!=null){
					entityId =(String) session.getAttribute("entityId");
				}
				
				entityId = (entityId != null) ? entityId : (session.getAttribute("contactId")!=null) ? (String)session.getAttribute("contactId") : "0" ;
				isValidUser = false;
			    isValidUser = UsersDAO.isRegisteredUser(contactemailAddress);
				 
				 if(!isValidUser){

					 	userRegistrationDetail.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
					 	userRegistrationDetail.setFirstName(contactFirstName);
						userRegistrationDetail.setLastName(contactLastName);
						userRegistrationDetail.setEmailAddress(contactemailAddress);
						userRegistrationDetail.setContactTitle(CommonUtility.validateString(contactTitle));
			            userRegistrationDetail.setContactWebsite(CommonUtility.validateString(contactWebsite));
						userRegistrationDetail.setUserPassword(password);
						userRegistrationDetail.setAddress1(contactAddress1);
						userRegistrationDetail.setAddress2(contactAddress2);
						userRegistrationDetail.setCity(contactCity);
						userRegistrationDetail.setState(contactState);
						userRegistrationDetail.setZipCode(zip);
						userRegistrationDetail.setCountry(contactCountry);
						userRegistrationDetail.setPhoneNo(contactPhone);
						userRegistrationDetail.setPhoneCode(contactFax);
						userRegistrationDetail.setLocUser(locUser);
						userRegistrationDetail.setParentUserId(""+parentUserId);
						userRegistrationDetail.setBuyingComanyIdStr(buyingCompanyId);
						userRegistrationDetail.setEntityId(entityId);
						userRegistrationDetail.setRole(roleAssign);
						if(roleAssign!=null){
							if(roleAssign.trim().contains("Authorized Purchasing Agent")){
								userRegistrationDetail.setRole("Ecomm Customer Auth Purchase Agent");
							}else if (roleAssign.trim().contains("Super User")) {
								userRegistrationDetail.setRole("Ecomm Customer Super User");
							}
							else if (roleAssign.trim().contains("Ecomm Authorized Technician")) {
								userRegistrationDetail.setRole("Ecomm Auth Technician");
							}else if (CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE")!=null && CommonUtility.validateString(roleAssign).equalsIgnoreCase(CommonDBQuery.getSystemParamtersList().get("SPECIAL_USER_ROLE").trim())) {
								userRegistrationDetail.setRole(roleAssign);
							}else{
								userRegistrationDetail.setRole("Ecomm Customer General User");
							}
						}
						String status = CommonUtility.validateString(request.getParameter("status"));
						userRegistrationDetail.setContactClassification(contactClassification);
						userRegistrationDetail.setUserStatus(status.length() > 0 ?  status : "Y");
						userRegistrationDetail.setSession(session);
						userRegistrationDetail.setActiveCustomerId(""); //Set WEB_OE_ADMIN_USER_NAME_ENTITY ID Or Entiti id of the logged in user
					 
					//result = usersObj.createRetailUser(userRegistrationDetail);
						
					 result = usersObj.createNewAgent(userRegistrationDetail);
					 userId = UsersDAO.getUserIdFromDB(contactemailAddress,"Y");
					 if (CommonUtility.customServiceUtility()!=null) {							
							CommonUtility.customServiceUtility().updateUserCustomFields(hidePrice, invoiceDetails, invoiceSummary, buyingCompanyId, entityId, userId);			
					 }
					 if(userId>0){
						 if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("managepurchaseagent.status.UserCreated")).length()>0){
							 result = result +" "+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("managepurchaseagent.status.UserCreated");
						 }else{
						 result = result+"|"+emailId;
						 }
					 }
					 
				 }else{
					 result = result + "0 | User Already registered in this email. Please use different email.";
				 }
			 }else{
				 result = "0 | Parent user not found, Unable to register user.";
			 }
		}
		renderContent = result;
		
		target = SUCCESS;
		
		}else{
			target = "SESSIONEXPIRED";
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return target;

	}

	public String autoFillAddress() {
		result = "";
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			String parentId = (String) session.getAttribute("parentUserId");
			String tempShipID = (String) session.getAttribute("defaultShipToId");
			int tempBillId = CommonUtility.validateNumber((String) session.getAttribute("defaultBillToId"));
			int defaultShipID = 0;
			if (tempShipID != null) {
				defaultShipID = CommonUtility.validateNumber(tempShipID);
			}

			HashMap<String, ArrayList<UsersModel>> templist = UsersDAO.getAddressListFromBCAddressBook(
					CommonUtility.validateNumber(buyingCompanyId),
					CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY)));
			if (templist != null && !templist.isEmpty()) {
				if (CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("EnableShipToAsEntityAddress"))
						.equalsIgnoreCase("Y")) {
					ArrayList<UsersModel> shipAddressListTemp = templist.get("Ship");
					if (parentId != null && shipAddressListTemp != null && shipAddressListTemp.size() > 0) {
						for (UsersModel temp : shipAddressListTemp) {
							if (temp.getAddressBookId() == defaultShipID) {
								result = temp.getAddress1() + "|" + temp.getAddress2() + "|" + temp.getCity() + "|"
										+ temp.getState() + "|" + temp.getZipCodeStringFormat() + "|"
										+ temp.getCountry() + "|" + temp.getPhoneNo();
								break;
							}
						}
					}
				} else {
					ArrayList<UsersModel> billAddressListTemp = templist.get("Bill");
					if (billAddressListTemp != null && !billAddressListTemp.isEmpty()) {
						for (UsersModel temp : billAddressListTemp) {
							if (temp.getAddressBookId() == tempBillId) {
								result = temp.getAddress1() + "|" + temp.getAddress2() + "|" + temp.getCity() + "|"
										+ temp.getState() + "|" + temp.getZipCodeStringFormat() + "|"
										+ temp.getCountry() + "|" + temp.getPhoneNo();
								break;
							}
						}

					}
				}
			}
			renderContent = result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String autoFillBillAddress() {
		result = "";
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			String parentId = (String) session.getAttribute("parentUserId");
			String tempBillID = (String) session.getAttribute("defaultBillToId");
			int defaultBillID = 0;
			if (tempBillID != null) {
				defaultBillID = CommonUtility.validateNumber(tempBillID);
			}
			ArrayList<UsersModel> billAddressListTemp = new ArrayList<UsersModel>();
			HashMap<String, ArrayList<UsersModel>> templist = UsersDAO.getAddressListFromBCAddressBook(
					CommonUtility.validateNumber(buyingCompanyId),
					CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY)));
			billAddressListTemp = templist.get("Bill");
			if (parentId != null && billAddressListTemp != null && billAddressListTemp.size() > 0) {
				for (UsersModel temp : billAddressListTemp) {
					if (temp.getAddressBookId() == defaultBillID) {
						result = temp.getAddress1() + "|" + temp.getAddress2() + "|" + temp.getCity() + "|"
								+ temp.getState() + "|" + temp.getZipCodeStringFormat() + "|" + temp.getCountry() + "|"
								+ temp.getPhoneNo();
					}
				}
			}
			renderContent = result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String AssignShipToForAgent() {
		request = ServletActionContext.getRequest();
		result = "";
		try {
			String assignedList = "[]";
			if (request.getParameter("assignedShipIdListParam") != null
					&& request.getParameter("assignedShipIdListParam").trim().length() > 0) {
				assignedList = request.getParameter("assignedShipIdListParam");
			}
			String currentSUState = request.getParameter("isSuperUserCurrent");
			String userPermission = request.getParameter("userRole");
			System.out.println("approverSequence:" + approverSequence);
	    	System.out.println("approverSequence:" + alwaysApprover);
	    	
	    	boolean updatePrevilage = true;
	    	if (userPermission != null && alwaysApprover != null && alwaysApprover.equals("on")) {
				if (!approverSequence.equalsIgnoreCase("") && (assignedNewId != null && assignedNewId.trim().length() > 0)) {
					updatePrevilage = true;
				} else {
					updatePrevilage = false;
					result = "0|" + LayoutLoader.getMessageProperties().get(request.getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("product.label.submitCartApproval");
				}
			}

		
			/*
			 * if (userPermission != null &&
			 * userPermission.trim().equalsIgnoreCase("generalUser")) { if (assignedNewId !=
			 * null && assignedNewId.trim().length() > 0) { updatePrevilage = true; } else {
			 * updatePrevilage = false; result =
			 * "0|Please assign a Purchase Agent to submit cart for Approval"; } }
			 */
			UsersDAO userDaoObj = new UsersDAO();
			/*
			 * if(updatePrevilage){ result = userDaoObj.assignShipToForAgentDAO(request,
			 * assignedNewId, userId, shipIdList, addressBId, enId, assignedList,
			 * currentSUState, userPermission); }
			 */
			if (updatePrevilage) {
				result = userDaoObj.assignShipToForAgentDAO(request, assignedNewId, userId, shipIdList, addressBId,
						enId, assignedList, currentSUState, userPermission, assignedId,approveLimit,alwaysApprover,approverSequence);
			}
			renderContent = result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public static boolean clearCheckoutSession(String checkoutUserId, HttpSession session) {
		boolean returnStat = false;
		int updated = -1;
		try {
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int actualuserId = CommonUtility.validateNumber(sessionUserId);
			String surSessionuserid = checkoutUserId;
			int cussentUserId = CommonUtility.validateNumber(surSessionuserid);
			updated = ProductsDAO.rolbackCheckOutCart(cussentUserId, session.getId(), actualuserId);
			session.removeAttribute("CheckoutUserId");
			session.removeAttribute("CheckoutCustomerId");
			session.removeAttribute("CheckoutBillToId");
			session.removeAttribute("CheckoutShipToId");
			session.removeAttribute("CheckoutCountry");
			session.removeAttribute("CheckoutUserName");
			if (updated > 0) {
				System.out.println("Updated and checkout session removed");
				returnStat = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStat;
	}

	public String staticPage() {
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, ArrayList<MenuAndBannersModal>> staticBannersList = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
		request = ServletActionContext.getRequest();
		String fPageId = "";
		try {
			if (request.getParameter("pId") != null)
				pId = CommonUtility.validateNumber(request.getParameter("pId"));
			String tempLevel = request.getParameter("levelNo");
			int level = 0;
			if (CommonUtility.validateString(tempLevel).equalsIgnoreCase("")) {
				level = CommonUtility.validateNumber(tempLevel);
			}
			HttpSession session = request.getSession();
			if (session.getAttribute("CheckoutUserId") != null) {
				String curUid = "" + (Integer) session.getAttribute("CheckoutUserId");
				clearCheckoutSession(curUid, session);
			}
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			System.out.println("Parent Menu Id : " + username + " - " + parentID);
			if (ID != null) {
				UsersDAO dao = new UsersDAO();
				staticBannersList = dao.getStaticBanners(ID);
				topBanners = staticBannersList.get("top");
				leftBanners = staticBannersList.get("left");
				rightBanners = staticBannersList.get("right");
				bottomBanners = staticBannersList.get("bottom");
			}
			if (ID != null && pId == 0) {
				pId = CommonUtility.validateNumber(ID);
			}
			boolean getParentMenu = true;
			subMenuList = new ArrayList<MenuAndBannersModal>();
			int staticLinkId = 0;
			if (MenuAndBannersDAO.getStaticLinkId().get(CommonUtility.validateNumber(ID)) != null) {
				staticLinkId = MenuAndBannersDAO.getStaticLinkId().get(CommonUtility.validateNumber(ID));
				if (staticLinkId > 0) {
					if (MenuAndBannersDAO.getStaticSubmenuList().get(staticLinkId) != null) {
						subMenuList = MenuAndBannersDAO.getStaticSubmenuList().get(staticLinkId);
						getParentMenu = false;
					}
				}
			}
			if (getParentMenu) {
				int staticParentId = 0;
				if (MenuAndBannersDAO.getStaticLinkParentId().get(CommonUtility.validateNumber(ID)) != null) {
					staticParentId = MenuAndBannersDAO.getStaticLinkParentId().get(CommonUtility.validateNumber(ID));
					if (staticParentId > 0) {
						if (MenuAndBannersDAO.getStaticSubmenuList().get(staticParentId) != null)
							subMenuList = MenuAndBannersDAO.getStaticSubmenuList().get(staticParentId);
					}
				}

			}

			parentID = Integer.toString(pId);

			if (ID != null && !ID.trim().equalsIgnoreCase("")) {
				if (level > 1) {
					setBreadCrumbList(UsersDAO.getStaticBreadCrumb(ID, level));
				}
				fPageId = ID;
				ID = ID + ".xml";
			}
			if (sessionUserId == null) {
				result = SUCCESS;
			}
			String staticPagePath = CommonDBQuery.getSystemParamtersList().get("STATICPAGESPATH");
			try {
				SAXBuilder builder = new SAXBuilder();
				File file = new File(staticPagePath + ID);
				if (file.exists()) {
					fullPageLayout = UsersDAO.getLayoutType(fPageId);
					org.jdom.Document dom = builder.build(file);
					pageName = dom.getRootElement().getChildText("pageName");
					pageTitle = dom.getRootElement().getChildText("pageTitle");
					pageContent = dom.getRootElement().getChildText("pageContent");
					metaKeywords = dom.getRootElement().getChildText("metaKeywords");
					metaDesc = dom.getRootElement().getChildText("metaDesc");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * if(type == null) type=""; if(type != null &&
			 * type.equalsIgnoreCase("mobile")){ return "StaticPageApp"; }else { return
			 * result; }
			 */
			contentObject.put("pId", pId);
			contentObject.put("topBanners", topBanners);
			contentObject.put("leftBanners", leftBanners);
			contentObject.put("rightBanners", rightBanners);
			contentObject.put("bottomBanners", bottomBanners);
			contentObject.put("ID", ID);
			contentObject.put("subMenuList", subMenuList);
			contentObject.put("parentID", parentID);
			contentObject.put("fullPageLayout", fullPageLayout);
			contentObject.put("pageName", pageName);
			contentObject.put("pageTitle", pageTitle);
			contentObject.put("pageContent", pageContent);
			contentObject.put("metaKeywords", metaKeywords);
			contentObject.put("metaDesc", metaDesc);
			renderContent = LayoutGenerator.templateLoader("StaticPage", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;

	}

	public String editContactInformation() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		renderContent = "";
		if (userId > 1 && !CommonUtility.validateString((String) session.getAttribute("isPunchoutUser"))
				.equalsIgnoreCase("Y")) {
			try {
				UsersModel userInfo = new UsersModel();
				userInfo.setFirstName(firstName.trim());
				userInfo.setLastName(lastName.trim());
				userInfo.setAddress1(address1);
				if (address2 != null && !address2.trim().equalsIgnoreCase("")) {
					userInfo.setAddress2(address2);
				} else {
					userInfo.setAddress2("");
				}
				userInfo.setCity(city);
				userInfo.setCountry(country);
				if (country != null && (country.equals("US") || country.equals("USA"))) {
					userInfo.getCountryModel().setCountryCode("US");
				}
				userInfo.setState(state);
				userInfo.setEmailAddress(email);
				userInfo.setZipCode(zip);
				userInfo.setPhoneNo(phone);
				userInfo.setUserId(userId);
				userInfo.setSession(session);
				userInfo.setUserToken((String) session.getAttribute("userToken"));
				result = UsersDAO.updateContactInformation(userInfo);
				renderContent = result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "ResultLoader";
		} else {
			return "SESSIONEXPIRED";
		}
	}

	public String editBillAddress() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String erpOverrideFlag = request.getParameter("erpOverrideFlag");
		renderContent = "";
		if (userId > 1 && !CommonUtility.validateString((String) session.getAttribute("isPunchoutUser"))
				.equalsIgnoreCase("Y")) {
			try {
				UserManagement usersObj = new UserManagementImpl();
				String firtName = "";
				String lastName = "";
				// String customerName = customerName;
				if (CommonUtility.validateString(customerName).length() > 0) {
					int idx = customerName.trim().lastIndexOf(" ");
					if (idx > 0) {
						firtName = customerName.substring(0, idx).trim();
						lastName = customerName.substring(idx).trim();
					} else {
						firtName = customerName;
					}
				}

				ArrayList<AddressModel> contactShortList = new ArrayList<AddressModel>();

				AddressModel phone = new AddressModel();
				phone.setPhoneNo(billPhone);
				phone.setPhoneDescription("Business");
				contactShortList.add(phone);

				UsersModel userInfo = new UsersModel();
				int addressId = 0;
				userInfo.setCustomerName(customerName);
				if (addressBookId != null && addressBookId.trim().length() > 0) {
					addressId = CommonUtility.validateNumber(addressBookId);
				}
				userInfo.setAddressBookId(addressId);
				userInfo.setFirstName(firtName.trim());
				userInfo.setLastName(lastName.trim());
				userInfo.setAddress1(address1);
				if (address2 != null && !address2.trim().equalsIgnoreCase("")) {
					userInfo.setAddress2(address2);
				} else {
					userInfo.setAddress2("");
				}
				userInfo.setCity(city);
				userInfo.setCountry(country);
				if (country != null && (country.equals("US") || country.equals("USA"))) {
					userInfo.getCountryModel().setCountryCode("US");
				}

				userInfo.setState(state);
				userInfo.setUseEntityAddress(useEntityAddress);
				userInfo.setEmailAddress(email);
				userInfo.setZipCode(zip);
				userInfo.setPhoneNo(billPhone);
				userInfo.setContactId(contactId);
				userInfo.setEntityId(CommonUtility.validateString(entityId));
				userInfo.setUserId(userId);
				userInfo.setSession(session);
				userInfo.setUserToken((String) session.getAttribute("userToken"));
				userInfo.setContactShortList(contactShortList);
				userInfo.setErpOverrideFlag(erpOverrideFlag);
				result = usersObj.editBillingAddress(userInfo);
				renderContent = result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "ResultLoader";
		} else {
			return "SESSIONEXPIRED";
		}
	}

	public String editShipAddress() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String erpOverrideFlag = request.getParameter("erpOverrideFlag");
		renderContent = "";
		if (userId > 1 && !CommonUtility.validateString((String) session.getAttribute("isPunchoutUser"))
				.equalsIgnoreCase("Y")) {
			try {
				UserManagement usersObj = new UserManagementImpl();
				String firtName = "";
				String lastName = "";
				// String customerName = customerName;
				if (customerName != null && !customerName.trim().equalsIgnoreCase("")) {
					int idx = customerName.trim().lastIndexOf(" ");
					if (idx > 0)
						firtName = customerName.substring(0, idx).trim();
					if (idx > 0)
						lastName = customerName.substring(idx).trim();
				}
				ArrayList<AddressModel> contactShortList = new ArrayList<AddressModel>();
				AddressModel phone = new AddressModel();
				phone.setPhoneNo(phoneNo);
				phone.setPhoneDescription("Business");
				contactShortList.add(phone);

				UsersModel userInfo = new UsersModel();
				int addressId = 0;
				userInfo.setCustomerName(customerName);
				if (addressBookId != null && addressBookId.trim().length() > 0) {
					addressId = CommonUtility.validateNumber(addressBookId);
				}
				userInfo.setAddressBookId(addressId);
				userInfo.setFirstName(firtName.trim());
				userInfo.setLastName(lastName.trim());
				userInfo.setAddress1(address1);
				if (address2 != null && !address2.trim().equalsIgnoreCase("")) {
					userInfo.setAddress2(address2);
				} else {
					userInfo.setAddress2("");
				}
				userInfo.setCity(city);
				userInfo.setCountry(country);
				userInfo.setCountry(country);

				if (country != null && (country.equals("US") || country.equals("USA"))) {
					userInfo.getCountryModel().setCountryCode("US");
				}
				if (makeAsDefault.contains("Yes")) {
					session.setAttribute("defaultShipToId", addressBookId);
				}
				userInfo.setState(state);
				userInfo.setUseEntityAddress(useEntityAddress);
				userInfo.setEmailAddress(email);
				userInfo.setPhoneNo(phoneNo);
				userInfo.setZipCode(zip);
				userInfo.setContactId(contactId);
				userInfo.setMakeAsDefault(makeAsDefault);
				userInfo.setUserId(userId);
				userInfo.setSession(session);
				userInfo.setEntityId(CommonUtility.validateString(entityId));
				userInfo.setUserToken((String) session.getAttribute("userToken"));
				userInfo.setContactShortList(contactShortList);
				userInfo.setShipToId(shipToId);
				userInfo.setErpOverrideFlag(erpOverrideFlag);
				result = usersObj.editShippingAddress(userInfo);
				renderContent = result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "ResultLoader";
		} else {
			return "SESSIONEXPIRED";
		}
	}

	public String addNewShippingAddress() {

		target = SUCCESS;
		boolean isValidUser = true;
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		result = "";
		renderContent = "";
		String customerId = (String) session.getAttribute("customerId");
		String billEntityId = (String) session.getAttribute("billingEntityId");
		if (customerId == null) {
			customerId = "0";
		}
		buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		UnilogFactoryInterface serviceProvider = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
		if (serviceProvider != null) {
			String poNumberSeq = serviceProvider.getPoSequenceId("SHIP_TO_SEQ");
			if (poNumberSeq != null) {
				shipToId = poNumberSeq;
				shipToName = shipFirstName;
			}
		}

		try {

			if (shipFirstName == null || shipFirstName.trim().equalsIgnoreCase("")) {
				isValidUser = false;
				result = result + "Please enter First Name.|";
			}
			if (shipLastName == null || shipLastName.trim().equalsIgnoreCase("")) {
				isValidUser = false;
				result = result + "Please enter Last Name.|";
			}
			if (address1 == null || address1.trim().equalsIgnoreCase("")) {
				isValidUser = false;
				result = result + "Please enter Address1.|";
			}
			if (city == null || city.trim().equalsIgnoreCase("")) {
				isValidUser = false;
				result = result + "Please enter City.|";
			}
			if (country == null || country.trim().equalsIgnoreCase("")) {
				isValidUser = false;
				result = result + "Please select Country.|";
			}
			if (state == null || state.trim().equalsIgnoreCase("")) {
				isValidUser = false;
				result = result + "Please select State.|";
			}
			if (zip == null || zip.trim().equalsIgnoreCase("")) {
				isValidUser = false;
				result = result + SendMailUtility.propertyLoader("register.label.zip", "") + "|";
			}
			if (shipPhone == null || shipPhone.trim().equalsIgnoreCase("")) {
				isValidUser = false;
				result = result + "Please enter Phone Number.|";
			} else {
				if (!CommonUtility.validatePhoneNumber(shipPhone)) {
					isValidUser = false;
					result = result + "Please enter valid Phone Number.|";
				} else {
					shipPhone = shipPhone.replaceAll("[^0-9]", "");
				}
			}

			if (emailAddress.trim().equalsIgnoreCase("")) {
				isValidUser = false;
				result = result + "Please enter Email Address.|";
			} else {

				if (!CommonUtility.validateEmail(emailAddress)) {
					isValidUser = false;
					result = result + "Please enter valid Email Address.|";
				}
			}
			if (checkShipToId != null && checkShipToId.trim().equalsIgnoreCase("N")) {
			} else {
				if (CommonUtility.validateString(shipToId).equalsIgnoreCase("")) {
					isValidUser = false;
					result = result + "Please enter Ship To ID.|";
				}
			}
			if (isValidUser) {
				UserManagement usersObj = new UserManagementImpl();

				String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
				int userId = CommonUtility.validateNumber(sessionUserId);
				ArrayList<AddressModel> contactShortList = new ArrayList<AddressModel>();
				AddressModel phone = new AddressModel();
				phone.setPhoneNo(shipPhone);
				phone.setPhoneDescription("Business");
				contactShortList.add(phone);
				UsersModel shipAddressInfo = new UsersModel();
				shipAddressInfo = new UsersModel();
				shipAddressInfo.setFirstName(shipFirstName);
				shipAddressInfo.setLastName(shipLastName);
				if (shipLastName != null && !shipLastName.trim().equalsIgnoreCase("")) {
					shipAddressInfo.setEntityName(shipFirstName + " " + shipLastName);
				} else {
					shipAddressInfo.setEntityName(shipFirstName);
				}
				shipAddressInfo.setEntityId(customerId);
				shipAddressInfo.setShipEntityId(customerId);
				shipAddressInfo.setBillEntityId(CommonUtility.validateString(billEntityId));
				shipAddressInfo.setAddress1(address1);
				shipAddressInfo.setAddress2(address2);
				shipAddressInfo.setCity(city);
				shipAddressInfo.setState(state);
				shipAddressInfo.setZipCode(zip);
				shipAddressInfo.setCountry(country);
				if (country != null && (country.equals("US") || country.equals("USA"))) {
					country = "US";
					shipAddressInfo.getCountryModel().setCountryCode("US");
				}
				shipAddressInfo.setPhoneNo(shipPhone);
				shipAddressInfo.setBuyingCompanyId(CommonUtility.validateNumber(buyingCompanyId));
				shipAddressInfo.setEmailAddress(emailAddress);
				shipAddressInfo.setShipToId(shipToId);
				shipAddressInfo.setShipToName(shipToName);
				shipAddressInfo.setMakeAsDefault(makeAsDefault);
				shipAddressInfo.setUserId(userId);
				shipAddressInfo.setSession(session);
				shipAddressInfo.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
				shipAddressInfo.setUserToken((String) session.getAttribute("userToken"));
				shipAddressInfo.setContactShortList(contactShortList);
				if (serviceProvider != null && shipAddressInfo != null) {
					String validAddress = CommonUtility.customServiceUtility().validateAvaAddress(shipAddressInfo);
					if (validAddress!=null && validAddress.length() > 0) {
						renderContent = validAddress;		
					}else {
						result = usersObj.addNewShippingAddress(shipAddressInfo);
						renderContent = result;
						String message = serviceProvider.sendShipAddressFile(shipAddressInfo, result, contactWebsite, addFirstName);
						if(CommonUtility.validateString(message).length()>0){
							renderContent = message;
						}
					}
				} else {
					result = usersObj.addNewShippingAddress(shipAddressInfo);
					renderContent = result;
				}
				
			} else {
				renderContent = result;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ResultLoader";
	}

	public String AdvForgotPasswordDisable() {
		UsersModel userInfo = new UsersModel();
		try {
			userInfo.setaRPassword(CommonUtility.validateString(mailMessage));
			userInfo = UsersDAO.retrieveAdvancedForgorPassword(userInfo);
			userInfo.setStatusDescription("NA");
			int count = UsersDAO.advancedForgotPasswordDisable(userInfo);

			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			if (count > 0) {
				contentObject.put("accessDisabled", "Y");
			} else {
				contentObject.put("accessDisabled", "N");
			}

			renderContent = LayoutGenerator.templateLoader("AdvForgotPassword", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String RetrieveAdvanceForgotPassword() {
		UsersModel userInfo = new UsersModel();
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		boolean isValidUser = true;
		boolean userStatus=true;
		result = "";
		target = SUCCESS;
		try {
			if (CommonUtility.validateString(password).length() == 0) {
				isValidUser = false;
				result = result + LayoutLoader.getMessageProperties()
						.get(session.getAttribute("localeCode").toString().toUpperCase())
						.getProperty("retrievepassword.error.password") + "|";
			} else {
				if (password.trim().length() < CommonUtility
						.validateNumber(CommonDBQuery.getSystemParamtersList().get("PASSWORD_MINIMUM_CHARECTERS"))) {
					isValidUser = false;
					result = result + CommonUtility.validateString(LayoutLoader.getMessageProperties()
							.get(session.getAttribute("localeCode").toString().toUpperCase())
							.getProperty("retrievepassword.error.passwordMinimunCharacters")) + "|";
				} else if (CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR"))
						.equalsIgnoreCase("N")) {
					if (!NewUserRegisterUtility.isAlfaNumericOnly(password)) {
						isValidUser = false;
						result = result + CommonUtility.validateString(LayoutLoader.getMessageProperties()
								.get(session.getAttribute("localeCode").toString().toUpperCase())
								.getProperty("retrievepassword.error.passwordCharacters")) + "|";
					}
				}

			}
			if (confirmPassword.trim().equalsIgnoreCase("")) {
				isValidUser = false;
				result = result + LayoutLoader.getMessageProperties()
						.get(session.getAttribute("localeCode").toString().toUpperCase())
						.getProperty("retrievepassword.error.confirmPassword") + "|";
			}
			if (isValidUser) {
				if (CommonUtility.validateString(password).length() > 0
						&& CommonUtility.validateString(confirmPassword).length() > 0) {
					if (password.equals(confirmPassword)) {
						if (CommonUtility.validateString(mailMessage).length() > 0) {
							userInfo.setaRPassword(mailMessage);
							userInfo = UsersDAO.retrieveAdvancedForgorPassword(userInfo);
							if(CommonUtility.customServiceUtility() != null) {
								userStatus=CommonUtility.customServiceUtility().validateUserName(username,userInfo,userStatus);
							}
							ArrayList<CreditCardModel> creditCardList = new ArrayList<CreditCardModel>();						
							creditCardList = SalesDAO.getCreditCardDetails(userInfo.getUserId());							
							if(CommonUtility.validateNumber(""+userInfo.getUserId())>0){
								UserManagement usersObj = new UserManagementImpl();
								userInfo.setUserName(userInfo.getUserName());
								userInfo.setPassword(password);
								userInfo.setIsCreditCard("No");
								userInfo.setBranchName("AFP");
								userInfo.setCreditCardList(creditCardList);
								userInfo.setErpOverrideFlag(CommonUtility.validateString(request.getParameter("erpOverrideFlag")));
								if(CommonUtility.validateString(request.getParameter("cimmesbOverrideFlag")).equalsIgnoreCase("Y")) {
									result = usersObj.updatePassword(userInfo, session);
								}
								else {
								userInfo.setCurrentPassword(SecureData.getPunchoutSecurePassword(userInfo.getCurrentPassword()));
								result	= usersObj.contactUpdate(userInfo,session);
								}
								if(CommonUtility.validateString(result).equals(password) || CommonUtility.validateString(result).contains("successfully")){
									int cou  = UsersDAO.updatePasswordWithUserName(userInfo.getUserName(),password);
									if(cou>0){
										userInfo.setStatusDescription("D");
										cou = UsersDAO.advancedForgotPasswordDisable(userInfo);
										if (CommonUtility.validateString(requestFrom).equalsIgnoreCase("cpol")) {
											userInfo.setChangePasswordOnLogin(0);
											UsersDAO.updateChangePasswordOnLogin(userInfo);
											// result =
											// CommonUtility.validateString(requestFrom)+"|"+CommonUtility.validateString(userInfo.getUserName())+"|"+password;//SecureData.getPunchoutSecurePassword(password);
										}
										result = LayoutLoader.getMessageProperties()
												.get(session.getAttribute("localeCode").toString().toUpperCase())
												.getProperty("retrievepassword.status.success");
									} else {
										result = LayoutLoader.getMessageProperties()
												.get(session.getAttribute("localeCode").toString().toUpperCase())
												.getProperty("retrievepassword.status.failed");
									}
								} else {
									result = LayoutLoader.getMessageProperties()
											.get(session.getAttribute("localeCode").toString().toUpperCase())
											.getProperty("retrievepassword.status.invalidkey");
								}
								// update user password
							} else {
								result = LayoutLoader.getMessageProperties()
										.get(session.getAttribute("localeCode").toString().toUpperCase())
										.getProperty("retrievepassword.status.invalidkey");
								if(CommonUtility.customServiceUtility() != null) {
									result=CommonUtility.customServiceUtility().setResult(userStatus,result);
								}
							}
						} else {
							result = LayoutLoader.getMessageProperties()
									.get(session.getAttribute("localeCode").toString().toUpperCase())
									.getProperty("retrievepassword.status.nopasswordkey");
						}
					} else {
						result = LayoutLoader.getMessageProperties()
								.get(session.getAttribute("localeCode").toString().toUpperCase())
								.getProperty("retrievepassword.status.passworddidnotmatch");
					}

				}
			}
			renderContent = result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String ForgotPassword() {
		boolean checkInactiveFlag = false;
		UsersModel userInfo = UsersDAO.forgotPassword(username, emailAddress);
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_POS_INACTIVE_CUSTOMER")).equalsIgnoreCase("Y") && userInfo!=null) {
			if(CommonUtility.customServiceUtility() != null) {
				checkInactiveFlag = CommonUtility.customServiceUtility().checkInactiveStatus(CommonUtility.validateParseIntegerToString(userInfo.getBuyingCompanyId()));
			}
		}
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		boolean flag = false;
		boolean isValidUser = true;
		try {
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RECAPTCHAV3_FORGOTPASSWORD")).equalsIgnoreCase("Y")){
				if(CommonUtility.validateString(request.getParameter("g-recaptcha-response")).length()>0) {
					GoogleRecaptchaV3 captchav3=new GoogleRecaptchaV3();
					 String  responseV3= captchav3.isValid(request.getParameter("g-recaptcha-response"));
					 	if(responseV3.equalsIgnoreCase("false")){				 
					 		isValidUser=false;
					 	}
					 if(!isValidUser) {
							result = CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.enterCaptcha"));
					  }
				 }else {
					 isValidUser=false;
					 result = CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.enterCaptcha"));
				 }
			}
			if(isValidUser) {
			if (userInfo == null) {
				result = LayoutLoader.getMessageProperties()
						.get(session.getAttribute("localeCode").toString().toUpperCase())
						.getProperty("forgot.Password.account.dosenotexist.message");
			} else if(checkInactiveFlag) {
				result = LayoutLoader.getMessageProperties()
						.get(session.getAttribute("localeCode").toString().toUpperCase())
						.getProperty("form.label.posinactive");
			}
			else {
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ADVANCED_FORGOT_PASSWORD"))
						.equalsIgnoreCase("Y")) {
					Date timeNow = new Date();
					userInfo.setaRPassword(SecureData.getPunchoutSecurePassword(timeNow.toString()));
					userInfo.setEmailAddress(emailAddress);
					int results = UsersDAO.advancedForgotPasswordInsert(userInfo);
					if (results > 0) {
						SendMailUtility sendMailUtility = new SendMailUtility();
						SendMailModel sendMailModel = new SendMailModel();
						sendMailModel.setUserName(username);
						sendMailModel.setPassword(SecureData.getPunchoutSecurePassword(timeNow.toString()));
						sendMailModel.setToEmailId(emailAddress);
						sendMailModel.setFirstName(userInfo.getFirstName());
						sendMailModel.setLastName(userInfo.getLastName());
						sendMailModel.setTemplateName("AdvancedForgotPassword");
						flag = sendMailUtility.buildForgotPasswordMail(sendMailModel);
					} else {
						result = LayoutLoader.getMessageProperties()
								.get(session.getAttribute("localeCode").toString().toUpperCase())
								.getProperty("forgot.Password.errorContactCustomercare.message");
						// result = "Failed to send password<br/> kindly contact our Customer Service";
					}
				} else if (CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("FORGOT_PASSWORD_ERP"))
						.equalsIgnoreCase("Y")) {
					LoginSubmitManagement loginSubmit = new LoginSubmitManagementImpl();
					LoginSubmitManagementModel loginSubmitManagementModel = new LoginSubmitManagementModel();

					loginSubmitManagementModel.setUserName(CommonUtility.validateString(username));
					loginSubmitManagementModel.setPassword(null);
					loginSubmitManagementModel.setForgotPassword(true);

					String responseFromERP = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
					if (CommonUtility.validateString(responseFromERP).equalsIgnoreCase("Y")) {
						flag = true;
					}

				} else {
					SecureData getPassword = new SecureData();
					String userPassword = getPassword.validatePassword(userInfo.getPassword());
					SendMailUtility sendMailUtility = new SendMailUtility();
					SendMailModel sendMailModel = new SendMailModel();
					sendMailModel.setUserName(username);
					sendMailModel.setPassword(userPassword);
					sendMailModel.setToEmailId(emailAddress);
					sendMailModel.setFirstName(userInfo.getFirstName());
					sendMailModel.setLastName(userInfo.getLastName());
					flag = sendMailUtility.buildForgotPasswordMail(sendMailModel);
				}
				if (flag) {
					result = LayoutLoader.getMessageProperties()
							.get(session.getAttribute("localeCode").toString().toUpperCase())
							.getProperty("forgot.Password.successmailsent.message") + " "
							+ CommonUtility.validateString(emailAddress);
					// result = "The mail containing password has been sent to "+emailAddress;
				} else {
					result = "Failed to send password<br/> kindly contact our Customer Service";
				}
			}
		}
			renderContent = result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String QuickOrder() {
		target = SUCCESS;
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		String tempdefaultShipId = null;
		String userName = null;
		String sessionUserId = null;
		sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		ArrayList<ProductsModel> quickOrderPadSel = new ArrayList<ProductsModel>();
		String quickOrderType = request.getParameter("reqType");
		try {
			int userId = CommonUtility.validateNumber(sessionUserId);
			ProductsModel selListVal = null;
			selListVal = new ProductsModel();
			selListVal.setProductListId(4);
			selListVal.setProductListName(
					LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase())
							.getProperty("label.mfrpartnumber"));
			quickOrderPadSel.add(selListVal);
			selListVal = new ProductsModel();
			selListVal.setProductListId(5);
			selListVal.setProductListName(LayoutLoader.getMessageProperties()
					.get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.upc"));
			quickOrderPadSel.add(selListVal);
			selListVal = new ProductsModel();
			selListVal.setProductListId(1);
			selListVal.setProductListName(LayoutLoader.getMessageProperties()
					.get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.partnumber"));
			quickOrderPadSel.add(selListVal);
			selListVal = new ProductsModel();
			selListVal.setProductListId(2);
			selListVal.setProductListName(
					LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase())
							.getProperty("label.customerpartnumber"));
			quickOrderPadSel.add(selListVal);
			contentObject.put("quickOrderPadSel", quickOrderPadSel);
			if (userId > 1) {
				userId = CommonUtility.validateNumber(sessionUserId);
				userName = (String) session.getAttribute(Global.USERNAME_KEY);
				entityId = (String) session.getAttribute("billingEntityId");
				tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
				buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
				defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);
				if (defaultBillToId == 0) {
					HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(userName);
					defaultBillToId = userAddressId.get("Bill");
				}
				HashMap<String, ArrayList<UsersModel>> userAddressList = UsersDAO
						.getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId), userId);
				billAddressList = userAddressList.get("Bill");
				shipAddressList = userAddressList.get("Ship");
				UserManagement userObj = new UserManagementImpl();
				HashMap<String, UsersModel> userAddress = userObj.getUserAddressFromBCAddressBook(defaultBillToId,
						defaultShipToId);
				billAddress = userAddress.get("Bill");
				shipAddress = userAddress.get("Ship");
				if (shipAddress != null && shipAddress.getEntityId() != null) {
					taxId = UsersDAO.getTaxInfo(shipAddress.getEntityId());
				}
				acceptPo = UsersDAO.getAcceptPo(userId);
				POValidStatus = CommonDBQuery.getSystemParamtersList().get("IS_PO_MANDATORY");
				manufacturersList = ProductsDAO.getManufacturersListForQuickOrder(
						CommonUtility.validateNumber((String) session.getAttribute("userSubsetId")),
						CommonUtility.validateNumber((String) session.getAttribute("generalCatalog")));
				username = (String) session.getAttribute(Global.USERNAME_KEY);
				contentObject.put("userId", userId);
				contentObject.put("shipVia", shipVia);
				contentObject.put("defaultShipToId", defaultShipToId);
				contentObject.put("defaultBillToId", defaultBillToId);
				contentObject.put("billAddressList", billAddressList);
				contentObject.put("shipAddressList", shipAddressList);
				contentObject.put("billAddress", billAddress);
				contentObject.put("shipAddress", shipAddress);
				contentObject.put("taxId", taxId);
				contentObject.put("acceptPo", acceptPo);
				contentObject.put("POValidStatus", POValidStatus);
				contentObject.put("username", username);
				contentObject.put("buyingCompanyId", buyingCompanyId);
				contentObject.put("manufacturersList", manufacturersList);
				if (quickOrderType != null && quickOrderType.trim().equalsIgnoreCase("STD")) {
					contentObject.put("responseType", quickOrderType);
					renderContent = LayoutGenerator.templateLoader("QuickOrderStdPage", contentObject, null, null,
							null);
				} else {
					contentObject.put("responseType", "quickOrder");
					renderContent = LayoutGenerator.templateLoader("QuickOrderPage", contentObject, null, null, null);

				}
			} else {
				if (quickOrderType != null && quickOrderType.trim().equalsIgnoreCase("STD")) {
					contentObject.put("responseType", quickOrderType);
					renderContent = LayoutGenerator.templateLoader("QuickOrderStdPage", contentObject, null, null,
							null);
				} else {
					contentObject.put("responseType", "quickOrder");
					renderContent = LayoutGenerator.templateLoader("QuickOrderPage", contentObject, null, null, null);

				}
			}

		} catch (Exception e) {
			target = "SESSIONEXPIRED";
			e.printStackTrace();
		}
		return target;
	}

	public String QuickOrderFileUpload() {
		target = SUCCESS;
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = null;
		try {
			sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String userName = (String) session.getAttribute(Global.USERNAME_KEY);
			entityId = (String) session.getAttribute("billingEntityId");
			String tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
			buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
			String erp = "defaults";
			;
			if (CommonDBQuery.getSystemParamtersList().get("ERP") != null
					&& CommonDBQuery.getSystemParamtersList().get("ERP").trim().length() > 0) {
				erp = CommonDBQuery.getSystemParamtersList().get("ERP").trim();
			}
			if (userId > 1) {
				defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);
				if (defaultBillToId == 0) {
					HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(userName);
					defaultBillToId = userAddressId.get("Bill");
				}
				HashMap<String, ArrayList<UsersModel>> userAddressList = UsersDAO
						.getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId), userId);
				billAddressList = userAddressList.get("Bill");
				shipAddressList = userAddressList.get("Ship");
				UserManagement userObj = new UserManagementImpl();
				HashMap<String, UsersModel> userAddress = userObj.getUserAddressFromBCAddressBook(defaultBillToId,
						defaultShipToId);
				billAddress = userAddress.get("Bill");
				shipAddress = userAddress.get("Ship");
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();// File Upload Max
																									// Items
				contentObject.put("userId", userId);
				contentObject.put("shipVia", shipVia);
				contentObject.put("defaultShipToId", defaultShipToId);
				contentObject.put("defaultBillToId", defaultBillToId);
				contentObject.put("billAddressList", billAddressList);
				contentObject.put("shipAddressList", shipAddressList);
				contentObject.put("billAddress", billAddress);
				contentObject.put("shipAddress", shipAddress);
				contentObject.put("taxId", taxId);
				contentObject.put("acceptPo", acceptPo);
				contentObject.put("POValidStatus", POValidStatus);
				contentObject.put("username", username);
				contentObject.put("buyingCompanyId", buyingCompanyId);
				renderContent = LayoutGenerator.templateLoader("QuickOrderFileUpload", contentObject, null, null, null);
			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return target;
	}

	public String getUsersForSharedCart() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String reqType = (String) request.getParameter("reqType");
			String defaultBillId = (String) session.getAttribute("defaultBillingAddressId");
			userList = new ArrayList<UsersModel>();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String keyWord = keyword;
			if (CommonUtility.customServiceUtility()!=null) {
				buyingCompanyId=((String)session.getAttribute("buyingCompanyId"));
				userList = CommonUtility.customServiceUtility().userSharedCart(buyingCompanyId,userList);			
			}
			if (keyWord != null && !keyWord.trim().equalsIgnoreCase("")) {
				userList = UsersDAO.getUsersForSharedCartDAO(keyWord, userId, defaultBillId);
			}
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("userList", userList);
			contentObject.put("savedGroupId", savedGroupId);
			contentObject.put("savedGroupName", savedGroupName);
			contentObject.put("reqType", reqType);
			renderContent = LayoutGenerator.templateLoader("UserDetailsForSharedCart", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/*
	 * public String sendPageMail(){ SendMailUtility sendObj = new
	 * SendMailUtility(); boolean flag = sendObj.sendProductMail(toName, toEmail,
	 * mailSubject, fromName,
	 * fromEmail,mailBody,descPart,pricePart,contentPart,imgPart,mailLink);
	 * if(flag){ renderContent = "0|success"; }else{ renderContent = "1|Fail"; }
	 * return SUCCESS; }
	 */
	public String sendPageMail() {
		SendMailUtility sendObj = new SendMailUtility();
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		/* spam email changes start */
		int requestCount = 0;
		try {
			int ipMailLimit = CommonUtility
					.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAX_MAIL_PERIP"));
			String captchaRequired = "N";
			String ipaddress = request.getHeader("X-Forwarded-For");
			if (ipaddress == null)
				ipaddress = request.getRemoteAddr();// userDefaultAddress
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			requestCount = UsersDAO.getSendMailCount(ipaddress);
			if (requestCount < 1) {
				session.setAttribute("captcha_required", "N");
				int count = UsersDAO.saveSendPageMail(ipaddress, 1, sessionId);
			} else {
				requestCount++;
				UsersDAO.updateSendPageMail(requestCount, ipaddress);
			}
			if (userId < 2 && requestCount > ipMailLimit) {
				session.setAttribute("captcha_required", "Y");
				captchaRequired = "Y";
			}
			String userCaptchaResponse = jcaptcha;
			if (!CommonUtility.validateString((String) session.getAttribute("captcha_required")).equalsIgnoreCase("")) {
				captchaRequired = (String) session.getAttribute("captcha_required");
			}
			boolean captchaPassed = false;
			if (CommonUtility.validateString(captchaRequired).equalsIgnoreCase("N")) {
				captchaPassed = true;
			} else {
				String captcha = (String) session.getAttribute("captcha");
				if (captcha != null && userCaptchaResponse != null) {

					if (captcha.equals(userCaptchaResponse)) {
						captchaPassed = true;
					}
				}
			}
			if (captchaPassed) {
				/* spam email changes end */
				boolean sendMail = true;
				if (CommonUtility.validateString(toName).length() == 0) {
					sendMail = false;
				}
				if (!CommonUtility.validateEmail(toEmail)) {
					sendMail = false;
				}
				if (CommonUtility.validateString(mailSubject).length() == 0) {
					sendMail = false;
				}
				if (CommonUtility.validateString(fromName).length() == 0) {
					sendMail = false;
				}
				if (!CommonUtility.validateEmail(fromEmail)) {
					sendMail = false;
				}
				if (CommonUtility.validateString(mailBody).length() == 0) {
					sendMail = false;
				}
				if (CommonUtility.validateString(descPart).length() == 0) {
					sendMail = false;
				}
				if (CommonUtility.validateString(contentPart).length() == 0) {
					sendMail = false;
				}
				if (CommonUtility.validateString(imgPart).length() == 0) {
					sendMail = false;
				}
				if (CommonUtility.validateString(mailLink).length() == 0) {
					sendMail = false;
				}
				boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
				if (sendMail && ajax) {
					boolean flag = sendObj.sendProductMail(toName, toEmail, mailSubject, fromName, fromEmail, mailBody,
							descPart, pricePart, contentPart, imgPart, mailLink);
					if (flag) {
						renderContent = "0|success";
					} else {
						renderContent = "1|Fail";
					}
				} else {
					response = ServletActionContext.getResponse();
					response.setStatus(404);
					renderContent = "1|Fail";
				} /* spam email changes start */
			} else {
				renderContent = "2|Invalid Captcha. Please try again.";
			}
			/* spam email changes end */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String sendPageMailForAll() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try {
			/* spam email changes start */
			int requestCount = 0;
			int ipMailLimit = CommonUtility
					.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAX_MAIL_PERIP"));
			String captchaRequired = "N";
			String ipaddress = request.getHeader("X-Forwarded-For");
			if (ipaddress == null)
				ipaddress = request.getRemoteAddr();// userDefaultAddress
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			requestCount = UsersDAO.getSendMailCount(ipaddress);
			if (requestCount < 1) {
				session.setAttribute("captcha_required", "N");
				int count = UsersDAO.saveSendPageMail(ipaddress, 1, sessionId);
			} else {
				requestCount++;
				UsersDAO.updateSendPageMail(requestCount, ipaddress);
			}
			if (userId < 2 && requestCount > ipMailLimit) {
				session.setAttribute("captcha_required", "Y");
				captchaRequired = "Y";
			}
			String userCaptchaResponse = jcaptcha;
			if (!CommonUtility.validateString((String) session.getAttribute("captcha_required")).equalsIgnoreCase("")) {
				captchaRequired = (String) session.getAttribute("captcha_required");
			}
			if (userId < 2) {
				captchaRequired = (String) CommonDBQuery.getSystemParamtersList().get("CAPTCHA_REQUIRED");
			} else {
				captchaRequired = "N";
			}
			boolean captchaPassed = false;
			if (CommonUtility.validateString(captchaRequired).equalsIgnoreCase("N")) {
				captchaPassed = true;
			} else {
				String captcha = (String) session.getAttribute("captcha");
				if (captcha != null && userCaptchaResponse != null) {

					if (captcha.equals(userCaptchaResponse)) {
						captchaPassed = true;
					}
				}
			}
			if (captchaPassed) {
				SendMailUtility sendObj = new SendMailUtility();
				SendMailModel sendMailObj = new SendMailModel();
				sendMailObj.setToName(toName);
				sendMailObj.setToEmailId(toEmail);
				sendMailObj.setMailSubject(mailSubject);
				sendMailObj.setFromName(fromName);
				sendMailObj.setFromEmailId(fromEmail);
				sendMailObj.setMessageBody(mailBody);
				sendMailObj.setDescPart(descPart);
				sendMailObj.setPricePart(pricePart);
				sendMailObj.setContentPart(contentPart);
				sendMailObj.setImgPart(imgPart);
				sendMailObj.setMailTemplateName(mailTemplateName);
				sendMailObj.setMailMessage(mailMessage);
				boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
				if (ajax) {
					boolean flag = sendObj.sendMailForAll(sendMailObj);
					if (flag) {
						renderContent = "0|success";
					} else {
						renderContent = "1|Fail";
					}
				} else {
					response = ServletActionContext.getResponse();
					response.setStatus(404);
					renderContent = "1|Fail";
				} /* spam email changes start */
			} else {
				renderContent = "2|Invalid Captcha. Please try again.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String scynEntityAddress() {
		target = "success";
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			if (session.getAttribute("scynEntityStatus") == null) {
				session.setAttribute("scynEntityStatus",
						LayoutLoader.getMessageProperties()
								.get(session.getAttribute("localeCode").toString().toUpperCase())
								.getProperty("refreshshippingaddress.status.complete"));
			}
			System.out.println(CommonUtility.validateString((String) session.getAttribute("scynEntityStatus")));
			String scynEntityStatus = CommonUtility.validateString((String) session.getAttribute("scynEntityStatus"));
			String locale = session.getAttribute("localeCode").toString().toUpperCase();
			String insertedSucc = LayoutLoader.getMessageProperties().get(locale)
					.getProperty("refreshshippingaddress.status.completeInserted");
			String updatedSucc = LayoutLoader.getMessageProperties().get(locale)
					.getProperty("refreshshippingaddress.status.completeUpdated");
			String errorMsg = LayoutLoader.getMessageProperties().get(locale)
					.getProperty("refreshshippingaddress.status.error");
			if (scynEntityStatus != null && (scynEntityStatus.contains(insertedSucc)
					|| scynEntityStatus.contains(updatedSucc) || scynEntityStatus.contains(errorMsg))) {
				UserManagement userObj = new UserManagementImpl();
				UsersModel customerInfoInput = new UsersModel();
				customerInfoInput.setSession(session);
				customerInfoInput.setUserToken((String) session.getAttribute("userToken"));
				customerInfoInput
						.setCustomerId(CommonUtility.validateString((String) session.getAttribute("customerId")));
				userObj.scynEntityAddress(customerInfoInput);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String scynEntityAddressStatus() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String syncStatus = "";
			if ((String) session.getAttribute("scynEntityStatus") != null) {
				syncStatus = (String) session.getAttribute("scynEntityStatus");
			}
			renderContent = syncStatus;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String saveThemeFor() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String siteTheme = (String) session.getAttribute("siteThemeCSSFolder");
			if (siteTheme != null && siteTheme.trim().equalsIgnoreCase("warm_css")) {
				session.setAttribute("siteThemeCSSFolder", "css");
			} else {
				session.setAttribute("siteThemeCSSFolder", "warm_css");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String geoBranchLocator() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		zipCode = request.getParameter("ZipCode");
		String userLati = "";
		String userLongi = "";
		type = request.getParameter("type");
		String homeBranch = (String) session.getAttribute("wareHouseCode");
		String branchIdValue = "";
		String trigerPoint = request.getParameter("trigerPoint");
		String selectedBranch = (String) session.getAttribute("selectedBranchWillCall");
		if (selectedBranch != null && !selectedBranch.trim().equalsIgnoreCase("")) {
			if (selectedBranch.contains("Will Call") || selectedBranch.contains("Pickup")) {
				String[] selectArra = selectedBranch.split("\\|");
				branchIdValue = selectArra[3]; // Removed due to Wil call removal
			}
		}
		if(CommonUtility.validateString(zipCode).length()==0){
			zipCode = (String)session.getAttribute("homeBranchZipCode");
			userLati = (String)session.getAttribute("homeBranchLati");
			userLongi = (String)session.getAttribute("homeBranchLongi");
		}else{
			if(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT")!=null && CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT").trim().equalsIgnoreCase("Y")){
				GeoLocatorProvider geoLocatorProvider = GeoLocatorEnums.getGeoLocatorType(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GEO_LOCATOR")));
				IGeoLocatorService geoLocatorService = GeoLocatorServiceProvider.getServiceProvider(geoLocatorProvider);
				GeoLocatorRequest geoLocatorRequest = new GeoLocatorRequest(CommonUtility.validateString(zipCode));
				GeoLocatorResponse geoLocatorResponse = geoLocatorService.locateUser(geoLocatorRequest);				
				userLati  = geoLocatorResponse != null ? geoLocatorResponse.getLatitude() : "";
				userLongi = geoLocatorResponse != null ? geoLocatorResponse.getLongitude() : "";
				session.setAttribute("homeBranchLati", userLati);
				session.setAttribute("homeBranchLongi", userLongi);
				session.setAttribute("homeBranchZipCode", zipCode);
				zipCode = zipCode+"|City: "+geoLocatorResponse.getCity()+ "|State: "+geoLocatorResponse.getState(); 
				session.setAttribute("homeBranchDisplay", zipCode);
			}

		}

		resultData = new ArrayList<ProductsModel>();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			double distance = 0;
			LinkedHashMap<String, ProductsModel> BranchList = null;
			// CustomServiceProvider
			if (CommonUtility.customServiceUtility() != null) {
				BranchList = CommonUtility.customServiceUtility().activeWarehouseList();
			}
			// CustomServiceProvider
			if (BranchList == null) {
				BranchList = CommonDBQuery.branchDetailData;
			}
			for (Entry<String, ProductsModel> entry : BranchList.entrySet()) {
				ProductsModel temp = entry.getValue();
				if (userLongi != null && temp.getBranchLatitude() != null && temp.getBranchLongitude() != null) {
					distance = distanceCalculatorBetweenTwoPoints(Double.parseDouble(userLati),
							Double.parseDouble(userLongi), Double.parseDouble(temp.getBranchLatitude()),
							Double.parseDouble(temp.getBranchLongitude()), "M");
				}
				ProductsModel temp1 = new ProductsModel();
				temp1.setBranchName(temp.getBranchName());
				temp1.setBranchID(temp.getBranchID());
				temp1.setBranchAddress1(temp.getBranchAddress1());
				temp1.setBranchAddress2(temp.getBranchAddress2());
				temp1.setBranchCity(temp.getBranchCity());
				temp1.setBranchPostalCode(temp.getBranchPostalCode());
				temp1.setBranchState(temp.getBranchState());
				temp1.setBranchDistance(distance);
				temp1.setBranchWorkingHours(temp.getBranchWorkingHours());
				temp1.setStatus("Y");
				if (branchIdValue != null && !branchIdValue.trim().equalsIgnoreCase("")) {
					if (temp1.getBranchID().equalsIgnoreCase(branchIdValue)) {
						temp1.setSuggestedValue("Y");
					}
				}
				resultData.add(temp1);
			}
			zipCode = (String) session.getAttribute("homeBranchDisplay");
			Collections.sort(resultData, new ProductsModel());
			ArrayList<ProductsModel> branchlist = new ArrayList<ProductsModel>();
			if (resultData.size() > 0) { // >3
				String branchId = "";
				for (int i = 0; i < resultData.size(); i++) { // 3
					ProductsModel bList = new ProductsModel();
					bList.setBranchID(resultData.get(i).getBranchID());
					bList.setBranchName(resultData.get(i).getBranchName());
					bList.setBranchAvailQty("Call For Availability");
					bList.setBranchDistance(resultData.get(i).getBranchDistance());
					bList.setBranchWorkingHours(resultData.get(i).getBranchWorkingHours());
					bList.setBranchAddress1(resultData.get(i).getBranchAddress1());
					bList.setBranchAddress2(resultData.get(i).getBranchAddress2());
					bList.setBranchCity(resultData.get(i).getBranchCity());
					bList.setBranchCountry(resultData.get(i).getBranchCountry());
					bList.setBranchState(resultData.get(i).getBranchState());
					bList.setBranchPostalCode(resultData.get(i).getBranchPostalCode());
					bList.setSuggestedValue(resultData.get(i).getSuggestedValue());
					branchlist.add(bList);
					branchId = branchId + resultData.get(i).getBranchID() + ",";
					if (CommonUtility.validateString(homeBranch).equalsIgnoreCase(resultData.get(i).getBranchID())) {
						session.setAttribute("homeBranchName", resultData.get(i).getBranchName());
						session.setAttribute("homeBranchAddress1", resultData.get(i).getBranchAddress1());
						session.setAttribute("homeBranchAddress2", resultData.get(i).getBranchAddress2());
						session.setAttribute("homeBranchCity", resultData.get(i).getBranchCity());
						session.setAttribute("homeBranchState", resultData.get(i).getBranchState());
						session.setAttribute("homeBranchPostalCode", resultData.get(i).getBranchPostalCode());
						session.setAttribute("homeBranchWorkingHours", resultData.get(i).getBranchWorkingHours());
					}
				}
				resultData = branchlist;
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("responseType", "GeoLocationBranch");
				contentObject.put("resultListData", resultData);
				if (trigerPoint != null && trigerPoint.trim().length() > 0) {
					contentObject.put("trigerPoint", trigerPoint);
				}
				renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject, null, null, null);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		if (type != null && type.trim().equalsIgnoreCase("frmscript")) {
			return SUCCESS;
		} else {
			return renderContent;
		}
	}

	public String seperateWillCall() {
		try {
			request = ServletActionContext.getRequest();
			WillCall test = new WillCall();
			ProductsModel itemInfo = new ProductsModel();
			UsersModel userInfo = new UsersModel();
			itemInfo.setPartNumber(CommonUtility.validateString(request.getParameter("partNumber")));
			itemInfo.setItemId(CommonUtility.validateNumber(request.getParameter("itemId")));
			userInfo.setZipCode(zipCode);
			renderContent = test.getWillCallHTML(itemInfo, userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public double geoYahooLocation(String address, String userLati, String userLongi) {
		YahooBossSupport getUserLocation = new YahooBossSupport();
		UsersModel locaDetail = new UsersModel();
		locaDetail.setZipCode(address);
		locaDetail = getUserLocation.locateUser(locaDetail);
		String latitude1 = locaDetail.getLatitude();
		String longitude1 = locaDetail.getLongitude();
		double distance;
		distance = distanceCalculatorBetweenTwoPoints(Double.parseDouble(userLati), Double.parseDouble(userLongi),
				Double.parseDouble(latitude1), Double.parseDouble(longitude1), "M");
		return distance;
	}

	private double distanceCalculatorBetweenTwoPoints(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		final double RADIUS = 6371.01;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		double finalValue = Math.round(dist * 100.0) / 100.0;
		return (finalValue);
	}

	public String getNearestWareHouse() {
		WarehouseModel warehouseModel = new WarehouseModel();
		Gson gson = new Gson();
		request = ServletActionContext.getRequest();
		try {
			String latitude = "0";
			String longitude = "0";

			if (request.getParameter("latitude") != null && request.getParameter("latitude").trim().length() > 0) {
				latitude = request.getParameter("latitude");
			}

			if (request.getParameter("longitude") != null && request.getParameter("longitude").trim().length() > 0) {
				longitude = request.getParameter("longitude");
			}

			warehouseModel.setLatitude(latitude);
			warehouseModel.setLongitude(longitude);

			ArrayList<WarehouseModel> wareHouseListWithDistance = getWareHouseListAndDistanceToUsersLocation(
					warehouseModel);
			renderContent = gson.toJson(wareHouseListWithDistance);
			System.out.println("getNearestWareHouse \n :" + renderContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public ArrayList<WarehouseModel> getWareHouseListAndDistanceToUsersLocation(WarehouseModel warehouseModel) {

		List<WarehouseModel> wareHouseList = new ArrayList<WarehouseModel>();
		ArrayList<WarehouseModel> wareHouseListWithDistance = new ArrayList<WarehouseModel>();

		try {

			wareHouseList = UsersDAO.getWareHouses();
			if (wareHouseList != null && wareHouseList.size() > 0) {
				for (WarehouseModel wModel : wareHouseList) {
					double distance;
					distance = distanceCalculatorBetweenTwoPoints(
							Double.parseDouble(CommonUtility.validateString(warehouseModel.getLatitude())),
							Double.parseDouble(CommonUtility.validateString(warehouseModel.getLongitude())),
							Double.parseDouble(CommonUtility.validateString(wModel.getLatitude())),
							Double.parseDouble(CommonUtility.validateString(wModel.getLongitude())), "M");
					wModel.setDistance(distance);
					wareHouseListWithDistance.add(wModel);
				}
			}
			System.out.println("---------------------------------------------------");
			if (wareHouseListWithDistance != null && wareHouseListWithDistance.size() > 0) {
				Collections.sort(wareHouseListWithDistance);
				/*
				 * for(WarehouseModel str: wareHouseListWithDistance){
				 * //System.out.println(str);
				 * System.out.println(str.getDistance()+"    :    "+str.getWareHouseName()+" : "
				 * +str.getLatitude()+" : "+str.getLongitude()); }
				 */
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return wareHouseListWithDistance;
	}

	public String saveSelectedBranch() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String selectedBranch = (String) request.getParameter("branchNameSelected");
			String userToken = (String) session.getAttribute("InnovoAccessToken");
			entityId = (String) session.getAttribute("entityId");
			String branchId = "";
			String branchName = "";
			if (selectedBranch != null && !selectedBranch.trim().equalsIgnoreCase("")) {
				if (selectedBranch.toUpperCase()
						.contains(LayoutLoader.getMessageProperties()
								.get(session.getAttribute("localeCode").toString().toUpperCase())
								.getProperty("shipvia.label.willcallerpcode").toUpperCase())) {
					String[] ship = selectedBranch.split("\\|");
					branchName = ship[2];
					branchId = ship[3];
				} else {
					UsersModel userInputParam = new UsersModel();
					userInputParam.setUserToken(userToken);
					userInputParam.setEntityId(CommonUtility.validateString(entityId));
					userInputParam.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
					userInputParam.setSession(session);
					ProductManagement prodObj = new ProductManagementImpl();
					UsersModel userModel = prodObj.getCustomerDetail(userInputParam);
					branchId = userModel.getBranchID();
					branchName = userModel.getBranchName();
				}
			}
			session.setAttribute("shipBranchName", branchName);
			session.setAttribute("shipBranchId", branchId);
			if (selectedBranch != null) {
				session.setAttribute("selectedBranchWillCall", selectedBranch);
				result = "0|" + selectedBranch;
			} else {
				result = "1|1";
			}
			renderContent = result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String editUser() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			if (userId > 1) {
				addressList = UsersDAO.getEntityDetailsByUserId(userId);
				System.out.println("check:::" + addressList.getEmailAddress());
				session.setAttribute("type", "web");

				/*
				 * address = new UsersModel(); address.setFirstName(rs.getString("FIRST_NAME"));
				 * address.setLastName(rs.getString("LAST_NAME"));
				 * address.setAddress1(rs.getString("ADDRESS1"));
				 * address.setAddress2(rs.getString("ADDRESS2"));
				 * address.setCity(rs.getString("CITY"));
				 * address.setState(rs.getString("STATE"));
				 * address.setCountry(rs.getString("COUNTRY"));
				 * address.setEmailAddress(rs.getString("EMAIL"));
				 * address.setZipCode(rs.getString("ZIP"));
				 * address.setUserName(rs.getString("USER_NAME")); SecureData getPassword = new
				 * SecureData(); String
				 * userPassword=getPassword.validatePassword(rs.getString("PASSWORD"));
				 * address.setPassword(userPassword);
				 * address.setContactId(rs.getInt("ECLIPSE_CONTACT_ID"));
				 */

				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();

				if (CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("FETCH_BILLING_ADDRESS_EDIT_USER"))
						.equals("Y")) {
					defaultBillToId = Integer.parseInt(session.getAttribute("defaultBillToId").toString());
					defaultShipToId = Integer.parseInt(session.getAttribute("defaultShipToId").toString());
					HashMap<String, UsersModel> userAddress = new UserManagementImpl()
							.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
					contentObject.put("userAddress", userAddress);
				}

				contentObject.put("buyingCompanyId",
						Integer.parseInt(session.getAttribute("buyingCompanyId").toString()));
				contentObject.put("billAddress", addressList);
				contentObject.put("type", "bill");
				renderContent = LayoutGenerator.templateLoader("EditContactPage", contentObject, null, null, null);

				target = SUCCESS;
			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String editContactAddress() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String erpOverrideFlag = request.getParameter("erpOverrideFlag");
		try {
			if (userId > 1 && !CommonUtility.validateString((String) session.getAttribute("isPunchoutUser"))
					.equalsIgnoreCase("Y")) {
				UsersModel addressDetail = new UsersModel();

				addressDetail.setAddress1(CommonUtility.validateString(address1));
				addressDetail.setAddress2(CommonUtility.validateString(address2));
				addressDetail.setCity(CommonUtility.validateString(city));
				addressDetail.setState(CommonUtility.validateString(state));
				addressDetail.setZipCode(CommonUtility.validateString(zip));
				addressDetail.setCountry(CommonUtility.validateString(country));
				addressDetail.setFirstName(CommonUtility.validateString(firstName));
				addressDetail.setLastName(CommonUtility.validateString(lastName));
				addressDetail.setContactId(CommonUtility.validateString(contactId));
				addressDetail.setUseEntityAddress(CommonUtility.validateString(useEntityAddress));
				if(!CommonUtility.validateString(request.getParameter("setEmailAddress")).equalsIgnoreCase("N")) {
					addressDetail.setEmailAddress(CommonUtility.validateString(email));
				}
				addressDetail.setPhoneNo(CommonUtility.validateString(billPhone));
				addressDetail.setJobTitle(CommonUtility.validateString(jobTitle));
				addressDetail.setOfficePhone(CommonUtility.validateString(phoneNo));
				addressDetail.setUserId(userId);
				addressDetail.setSession(session);
				addressDetail.setErpOverrideFlag(erpOverrideFlag);
				addressDetail.setCountryCode(CommonUtility.validateString(country));
				addressDetail.setCustomerType(CommonUtility.validateString(request.getParameter("customerType")));
				UserManagement usersObj = new UserManagementImpl();
				UsersModel customerInfoInput = new UsersModel();
				customerInfoInput.setUserToken((String) session.getAttribute("userToken"));
				if(!CommonUtility.validateString(request.getParameter("setEmailAddress")).equalsIgnoreCase("N")) {
				customerInfoInput.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
				}
				customerInfoInput.setSession(session);
				customerInfoInput.setJobTitle(CommonUtility.validateString(jobTitle));
				ArrayList<CreditCardModel> creditCardList = new ArrayList<CreditCardModel>();
				creditCardList = SalesDAO.getCreditCardDetails(userId);
				if (creditCardList != null && creditCardList.size() > 0) {
					customerInfoInput.setCreditCardList(creditCardList);
				}

				usersObj.checkERPConnection(customerInfoInput);
				UsersModel customerInfoOutPut = usersObj.contactEdit(customerInfoInput, addressDetail);

				renderContent = customerInfoOutPut.getResult();
				if (CommonUtility.validateString(renderContent).toLowerCase().contains("success")) {
					session.setAttribute("userFirstName", CommonUtility.validateString(firstName));
					session.setAttribute("userLastName", CommonUtility.validateString(lastName));
				}

			} else {
				renderContent = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String UpdateNewsLetterSubscription() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String customFieldName = (String) request.getParameter("customFieldName");
		String newsLetterSub = (String) request.getParameter("newsLetterSub");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);

		if (userId > 1) {
			try {
				UsersModel userDetail = new UsersModel();

				userDetail.setNewsLetterSub(newsLetterSub);
				userDetail.setUserId(userId);
				userDetail.setCustomFieldName(customFieldName);

				int count = UsersDAO.updateNewsletterSubscription(userDetail);

				if (count > 0) {
					result = "1|Updated Successfully";
					session.setAttribute("newsLetterSub", newsLetterSub);
				} else {
					result = "0|Update failed";
				}

				renderContent = result;
			} catch (Exception e) {
				e.printStackTrace();
				return "SESSIONEXPIRED";
			}
			return SUCCESS;
		} else {
			return "SESSIONEXPIRED";
		}
	}

	public String shipAddressNewRequest() {
		try {
			UsersModel addressDetail = new UsersModel();
			SendMailUtility sendMailUtility = new SendMailUtility();
			String approverEmail = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
			addressDetail.setFirstName(firstName);
			addressDetail.setLastName(lastName);
			addressDetail.setCompanyName(companyName);
			addressDetail.setAddress1(address1);
			addressDetail.setAddress2(address2);
			addressDetail.setCity(city);
			addressDetail.setState(state);
			addressDetail.setZipCode(zip);
			addressDetail.setCountry(country);
			addressDetail.setPhoneNo(phone);
			addressDetail.setEmailAddress(email);
			addressDetail.setApproverEmail(approverEmail);
			boolean shipAddressFlag = sendMailUtility.requestForNewShippingAddressMail(addressDetail);
			if (shipAddressFlag) {
				boolean shipAddressFlagtoApprover = sendMailUtility.requestForNewShippingAddressMailtoApprover(addressDetail);
				if (shipAddressFlagtoApprover) {
					result = "1|Sent Successfully";
				}
			} else {
				result = "0|Request not sent";
			}
			renderContent = result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public UsersModel getUserFNameLname() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		if (session.getAttribute("CheckoutUserId") != null
				&& session.getAttribute("CheckoutUserId").toString().length() > 0) {
			int cUid = (Integer) session.getAttribute("CheckoutUserId");
			sessionUserId = "" + cUid;
		}
		userId = CommonUtility.validateNumber(sessionUserId);
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		UsersModel address = null;
		try {
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {

			String sql = "select * from cimm_users where user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);

			rs = pstmt.executeQuery();
			// ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,EMAIL,ENTITY_ID;
			while (rs.next()) {
				address = new UsersModel();
				address.setFirstName(rs.getString("FIRST_NAME") != null ? rs.getString("FIRST_NAME") : "");
				address.setLastName(rs.getString("LAST_NAME") != null ? rs.getString("LAST_NAME") : "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return address;

	}

	public String assignShipVia() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			session.setAttribute("selectedShipVia", selectedShipVia);
			session.setAttribute("disableShip", disableShip);
			if (disableShip != null && disableShip.trim().equalsIgnoreCase("N")) {
				disableShipHazmatOnly = "N";
			}
			session.setAttribute("disableShipHazmatOnly", disableShipHazmatOnly);
		} catch (Exception e) {
			e.printStackTrace();
			return "SESSIONEXPIRED";
		}
		return null;
	}

	public String sameAsBilling() {

		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		userId = CommonUtility.validateNumber(sessionUserId);
		String entityidBillTo = "0";
		if (userId > 1) {
			try {
				String sameAsBillingAddress = (String) request.getParameter("sameAsBillingAddress");
				defaultShipToId = CommonUtility.validateNumber((String) request.getParameter("defaultShipToId"));
				defaultBillToId = CommonUtility.validateNumber((String) request.getParameter("defaultBillToId"));
				buyingCompanyId = CommonUtility.validateString((String) session.getAttribute("buyingCompanyId"));

				UsersModel shipAddressInfo = new UsersModel();
				shipAddressInfo.setSameAsBillingAddress(sameAsBillingAddress);
				shipAddressInfo.setUserId(userId);
				shipAddressInfo.setBuyingCompanyId(CommonUtility.validateNumber(buyingCompanyId));
				shipAddressInfo.setSession(session);
				shipAddressInfo.setRequestType("ADDRESS_BOOK_ID");
				shipAddressInfo.setAddressType("Bill");

				UsersModel userShipInfo = UsersDAO.getEntityIdFromBCAddressBook(defaultBillToId, shipAddressInfo);

				if (userShipInfo != null && !CommonUtility.validateString(userShipInfo.getEntityId()).equals("0")) {
					entityidBillTo = userShipInfo.getEntityId();

					shipAddressInfo.setEntityId(entityidBillTo);
					shipAddressInfo.setRequestType("ENTITY_ID");
					shipAddressInfo.setAddressType("Ship");
					shipAddressInfo.setFirstName(userShipInfo.getFirstName());
					shipAddressInfo.setLastName(userShipInfo.getLastName());
					shipAddressInfo.setEntityId(entityidBillTo);
					shipAddressInfo.setAddress1(userShipInfo.getAddress1());
					shipAddressInfo.setAddress2(userShipInfo.getAddress2());
					shipAddressInfo.setCity(userShipInfo.getCity());
					shipAddressInfo.setState(userShipInfo.getState());
					shipAddressInfo.setZipCode(userShipInfo.getZipCode());
					shipAddressInfo.setCountry(userShipInfo.getCountry());
					if (userShipInfo.getCountry() != null && (userShipInfo.getCountry().trim().equalsIgnoreCase("US")
							|| userShipInfo.getCountry().trim().equalsIgnoreCase("USA"))) {
						shipAddressInfo.setCountry("US");
						shipAddressInfo.getCountryModel().setCountryCode("US");
					}

					shipAddressInfo.setPhoneNo(userShipInfo.getPhoneNo());
					shipAddressInfo.setBuyingCompanyId(CommonUtility.validateNumber(buyingCompanyId));
					shipAddressInfo.setShipToId(userShipInfo.getShipToId());
					UsersModel userShipInfoByEntity = UsersDAO.getEntityIdFromBCAddressBook(defaultBillToId,
							shipAddressInfo);
					if (userShipInfoByEntity != null && userShipInfoByEntity.getDefaultShipToId() > 0) {
						defaultShipToId = userShipInfoByEntity.getDefaultShipToId();
						session.setAttribute("defaultShipToId", Integer.toString(defaultShipToId));
					} else {
						shipAddressInfo = new UsersModel();
						shipAddressInfo.setFirstName(userShipInfo.getFirstName());
						shipAddressInfo.setLastName(userShipInfo.getLastName());
						shipAddressInfo.setEntityId(entityidBillTo);
						shipAddressInfo.setAddress1(userShipInfo.getAddress1());
						shipAddressInfo.setAddress2(userShipInfo.getAddress2());
						shipAddressInfo.setCity(userShipInfo.getCity());
						shipAddressInfo.setState(userShipInfo.getState());
						shipAddressInfo.setZipCode(userShipInfo.getZipCode());
						shipAddressInfo.setCountry(userShipInfo.getCountry());
						if (userShipInfo.getCountry() != null
								&& (userShipInfo.getCountry().trim().equalsIgnoreCase("US")
										|| userShipInfo.getCountry().trim().equalsIgnoreCase("USA"))) {
							shipAddressInfo.setCountry("US");
							shipAddressInfo.getCountryModel().setCountryCode("US");
						}

						shipAddressInfo.setPhoneNo(userShipInfo.getPhoneNo());
						shipAddressInfo.setBuyingCompanyId(CommonUtility.validateNumber(buyingCompanyId));
						shipAddressInfo.setShipToId(userShipInfo.getShipToId());
						shipAddressInfo.setMakeAsDefault(userShipInfo.getMakeAsDefault());
						shipAddressInfo.setUserId(userId);
						shipAddressInfo.setSession(session);
						shipAddressInfo.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
						shipAddressInfo.setUserToken((String) session.getAttribute("userToken"));

						UserManagement usersObj = new UserManagementImpl();
						result = usersObj.addNewShippingAddress(shipAddressInfo);
						if (session.getAttribute("sameAsBillShipId") != null
								&& session.getAttribute("sameAsBillShipId").toString().trim().length() > 0) {
							defaultShipToId = CommonUtility
									.validateNumber((String) session.getAttribute("sameAsBillShipId"));
							session.setAttribute("defaultShipToId", Integer.toString(defaultShipToId));
						}

					}
				}
				renderContent = "Checkout";
			} catch (Exception e) {
				e.printStackTrace();
				return "SESSIONEXPIRED";
			}
			return SUCCESS;
		} else {
			return "SESSIONEXPIRED";
		}
	}

	/*
	 * public final String events() { System.out.println(sortBy); if(sortBy==null){
	 * sortBy="ORDER BY START_DATE ASC"; }else
	 * if(sortBy.trim().equalsIgnoreCase("NASC")){ sortBy =
	 * "ORDER BY EVENT_TITLE ASC"; }else
	 * if(sortBy.trim().equalsIgnoreCase("NDESC")){ sortBy =
	 * "ORDER BY EVENT_TITLE DESC"; }else
	 * if(sortBy.trim().equalsIgnoreCase("DASC")){ sortBy =
	 * "ORDER BY START_DATE ASC"; }else if(sortBy.trim().equalsIgnoreCase("DDESC")){
	 * sortBy = "ORDER BY START_DATE DESC"; }else
	 * if(sortBy.trim().equalsIgnoreCase("LASC")){ sortBy = "ORDER BY LOCATION ASC";
	 * }else if(sortBy.trim().equalsIgnoreCase("LDESC")){ sortBy =
	 * "ORDER BY LOCATION DESC"; }else if(sortBy.trim().equalsIgnoreCase("CASC")){
	 * sortBy = "ORDER BY EVENT_CATEGORY ASC"; }else
	 * if(sortBy.trim().equalsIgnoreCase("CDESC")){ sortBy =
	 * "ORDER BY EVENT_CATEGORY DESC"; }else
	 * if(sortBy.trim().equalsIgnoreCase("TASC")){ sortBy =
	 * "ORDER BY EVENT_TYPE ASC"; }else if(sortBy.trim().equalsIgnoreCase("TDESC")){
	 * sortBy = "ORDER BY EVENT_TYPE DESC"; } System.out.println(sortBy);
	 * setEventsByCategory(new
	 * LinkedHashMap<String,LinkedHashMap<String,ArrayList<EventModel>>>());
	 * if(eventsLocationFilter==null ||eventsLocationFilter.equalsIgnoreCase("")) {
	 * setEventsByCategory(UsersDAO.getEventList("",sortBy)); } else {
	 * setEventsByCategory(UsersDAO.getEventList(eventsLocationFilter,sortBy)); }
	 * 
	 * eventsType = UsersDAO.getEventType(); eventCatList=
	 * UsersDAO.getEventCategories();
	 * 
	 * LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String,
	 * Object>(); contentObject.put("eventCatList", eventCatList);
	 * contentObject.put("eventsType", eventsType);
	 * contentObject.put("eventsByCategory", eventsByCategory);
	 * 
	 * renderContent = LayoutGenerator.templateLoader("EventsPage", contentObject ,
	 * null, null, null); return SUCCESS; }
	 */

	public final String events() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try {

			logger.info(sortBy);
			if (sortBy == null) {
				sortBy = "ORDER BY START_DATE ASC";
			} else if (sortBy.trim().equalsIgnoreCase("NASC")) {
				sortBy = "ORDER BY EVENT_TITLE ASC";
			} else if (sortBy.trim().equalsIgnoreCase("NDESC")) {
				sortBy = "ORDER BY EVENT_TITLE DESC";
			} else if (sortBy.trim().equalsIgnoreCase("DASC")) {
				sortBy = "ORDER BY START_DATE ASC";
			} else if (sortBy.trim().equalsIgnoreCase("DDESC")) {
				sortBy = "ORDER BY START_DATE DESC";
			} else if (sortBy.trim().equalsIgnoreCase("LASC")) {
				sortBy = "ORDER BY LOCATION ASC";
			} else if (sortBy.trim().equalsIgnoreCase("LDESC")) {
				sortBy = "ORDER BY LOCATION DESC";
			} else if (sortBy.trim().equalsIgnoreCase("CASC")) {
				sortBy = "ORDER BY EVENT_CATEGORY ASC";
			} else if (sortBy.trim().equalsIgnoreCase("CDESC")) {
				sortBy = "ORDER BY EVENT_CATEGORY DESC";
			} else if (sortBy.trim().equalsIgnoreCase("TASC")) {
				sortBy = "ORDER BY EVENT_TYPE ASC";
			} else if (sortBy.trim().equalsIgnoreCase("TDESC")) {
				sortBy = "ORDER BY EVENT_TYPE DESC";
			}
			System.out.println(sortBy);
			setEventsByCategory(new LinkedHashMap<String, LinkedHashMap<String, ArrayList<EventModel>>>());

			EventModel eventModel = new EventModel();
			eventsCustomFieldList = UsersDAO.getEventCustomFields();
			eventModel.setEventCategoryFilter(eventsCategoryFilter);
			eventModel.setEventLocationFilter(eventsLocationFilter);

			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();

			if (CommonUtility.validateString(request.getParameter("eventV2")).length() > 0) {

				String date = request.getParameter("date");
				String category = request.getParameter("category");
				String type = request.getParameter("type");
				String location = request.getParameter("location");
				String month = request.getParameter("month");
				String event = request.getParameter("event");
				Date currentDate = new Date();
				String yearMonth = new SimpleDateFormat("yyyy-MM").format(currentDate);
				if (CommonUtility.validateString(month).length() > 0) {
					yearMonth = CommonUtility.validateString(month);
				}
				String dateMonth[] = yearMonth.split("-");
				int currentYear = CommonUtility.validateNumber(dateMonth[0]);
				int currentMonth = CommonUtility.validateNumber(dateMonth[1]);

				if (!CommonUtility.validateString(month).trim().equalsIgnoreCase("") && month.split("-").length > 0) {
					String selectedMonth[] = month.split("-");
					currentYear = CommonUtility.validateNumber(selectedMonth[0]);
					currentMonth = CommonUtility.validateNumber(selectedMonth[1]);

				}

				eventModel.setEventCategory(category);
				eventModel.setDate(date);
				eventModel.setLocation(location);
				eventModel.setEventType(type);
				eventModel.setMonth(month);

				LinkedHashMap<String, Object> eventObject = UsersDAO.getEventListV2(eventModel, sortBy);
				ArrayList<EventModel> eventList = (ArrayList<EventModel>) eventObject.get("eventList");
				LinkedHashMap<Integer, String> dateList = (LinkedHashMap<Integer, String>) eventObject.get("dateList");
				contentObject.put("eventCatList", CommonDBQuery.getEventCategories());
				contentObject.put("eventList", eventList);
				contentObject.put("dateList", dateList);
				contentObject.put("category", CommonUtility.validateNumber(category));
				contentObject.put("location", CommonUtility.validateNumber(location));
				contentObject.put("type", CommonUtility.validateNumber(type));

				if (CommonUtility.validateString(date).length() > 0
						&& CommonUtility.validateString(date).split("-").length > 0) {
					String selectedMonth[] = CommonUtility.validateString(date).split("-");
					currentYear = CommonUtility.validateNumber(selectedMonth[0]);
					currentMonth = CommonUtility.validateNumber(selectedMonth[1]);
				}
				contentObject.put("year", currentYear);
				contentObject.put("month", currentMonth);
				contentObject.put("yearMonth", CommonUtility.validateString(yearMonth));
				contentObject.put("eventsByCategory", CommonDBQuery.getEventCategories());
				contentObject.put("eventsLocations", CommonDBQuery.getEventLocations());
				contentObject.put("eventType", CommonDBQuery.getEventTypes());
			} else {
				setEventsByCategory(UsersDAO.getEventList(eventModel, sortBy));
				eventsType = UsersDAO.getEventType();
				eventCatList = UsersDAO.getEventCategories();
				contentObject.put("eventCatList", eventCatList);
				contentObject.put("eventsLocationFilter", eventsLocationFilter);
				contentObject.put("eventsCategoryFilter", eventsCategoryFilter);
				contentObject.put("eventsType", eventsType);
				contentObject.put("eventsByCategory", eventsByCategory);
				contentObject.put("eventsLocations", UsersDAO.getEventLocation());
				contentObject.put("eventsCustomFieldList", eventsCustomFieldList);
			}
			if (eventsCategoryFilter != null && eventsCategoryFilter.length > 0) {
				contentObject.put("selectedCategoryFilter", CommonUtility.validateString(eventsCategoryFilter[0]));
			}
			if (eventsLocationFilter != null && eventsLocationFilter.length > 0) {
				contentObject.put("selectedLocationFilter", CommonUtility.validateString(eventsLocationFilter[0]));
			}
			String fromPage = CommonUtility.validateString(request.getParameter("frPage"));

			if (CommonUtility.validateString(fromPage).equalsIgnoreCase("ajaxLoad")) {
				contentObject.put("responseType", "eventCategory");
				renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject, null, null, null);
			} else {
				renderContent = LayoutGenerator.templateLoader("EventsPage", contentObject, null, null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public final String eventDetails() {
		try {
			eventsList = UsersDAO.getEventDetails(Integer.parseInt(eventID));
			eventsList.setAvailableSeats(UsersDAO.getTotalAvailableSeats(Integer.parseInt(eventID)));
			ArrayList<EventModel> eventCustomFieldList = UsersDAO.getSingleEventCustomFields(Integer.parseInt(eventID));
			eventCost = 0;
			if (eventsList != null && eventsList.getCost() != null) {// This variable is added since the requirement was
				eventCost = (int) Math.round(eventsList.getCost());// to display the cost only in integer																	
			}
			eventCatList = UsersDAO.getEventCategories();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("eventCatList", eventCatList);
			contentObject.put("eventsList", eventsList);
			contentObject.put("eventCost", eventCost);
			contentObject.put("eventsByCategory", eventsByCategory);
			contentObject.put("eventsLocations", UsersDAO.getEventLocation());
			contentObject.put("eventCustomFieldList", eventCustomFieldList);
			renderContent = LayoutGenerator.templateLoader("EventDetailPage", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public final String eventRegister() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = Integer.parseInt(sessionUserId);
			addressList = new UsersModel();

			if (userId > 1) {
				addressList = UsersDAO.getEntityDetailsByUserId(userId);
				if (addressList.getCustomerName() == null) {
					if (addressList.getLastName() == null) {
						addressList.setCustomerName(addressList.getFirstName());
					} else {
						String custName = addressList.getFirstName() + " " + addressList.getLastName();
						addressList.setCustomerName(custName);
					}
				}
				String userName = (String) session.getAttribute(Global.USERNAME_KEY);
				String tempdefaultShipId = "";
				HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(userName);
				int defaultBillToId = userAddressId.get("Bill");
				if (session.getAttribute("defaultShipToId") == null) {
					tempdefaultShipId = "0";
				} else {
					tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
				}
				int defaultShipToId = Integer.parseInt(tempdefaultShipId);
				HashMap<String, UsersModel> userAddress = UsersDAO.getUserAddressFromBCAddressBook(defaultBillToId,
						defaultShipToId);
				if (userAddress != null && userAddress.size() != 0) {
					UsersModel billAddress = userAddress.get("Bill");
					addressList.setPhoneNo(billAddress.getPhoneNo());
				}
			}

			eventsList = UsersDAO.getEventDetails(Integer.parseInt(eventID));

			if (eventsList != null && eventsList.getCost() != null) {
				eventCost = (int) Math.round(eventsList.getCost());// This variable is added since the requirement was
																	// to display the cost only in integer
			}

			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("eventsList", eventsList);
			contentObject.put("eventCost", eventCost);
			contentObject.put("addressList", addressList);
			if(eventCost > 0) { 
			contentObject = CommonUtility.loadCenPOSParams(contentObject); 
		   }
			renderContent = LayoutGenerator.templateLoader("EventRegisterPage", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return "SESSIONEXPIRED";
		}
		return SUCCESS;
	}

	public final String RegisterToEvent() {


		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		EventDetails eventsDetails = new EventDetails();
		String registerationStatus = "Unconfirmed";
		int count = 0;
		

		String userCaptchaResponse = request.getParameter("jcaptcha");
		String captchaRequired = request.getParameter("captchaRequired");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		userId = CommonUtility.validateNumber(sessionUserId);	
		if(contactCountry!=null && CommonUtility.validateString(contactCountry).equalsIgnoreCase("USA")) {
			contactCountry ="US";
		}
		
		boolean captchaPassed = false;
		if(userId>1){
			captchaPassed = true;
		}else{
			if(CommonUtility.validateString(captchaRequired).equalsIgnoreCase("N")){
				captchaPassed = true;
			}else{
				String captcha = (String) session.getAttribute("captcha");
				if (captcha != null && userCaptchaResponse != null) {
	
					if (captcha.equals(userCaptchaResponse)) {
						captchaPassed = true;
						session.removeAttribute("captcha");
					} 
				}
			}

		}
		if(userId<2 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RECAPTCHAV3_EVENTPAGES")).equalsIgnoreCase("Y")){
			if(CommonUtility.validateString(request.getParameter("g-recaptcha-response")).length()>0){
				GoogleRecaptchaV3 reCaptcha =new GoogleRecaptchaV3();
				String captchToken= reCaptcha.isValid(request.getParameter("g-recaptcha-response"));
                 if(captchToken.equalsIgnoreCase("true"))
                 {
                	 captchaPassed = true;
                 }
			}
		}
	
		if(captchaPassed){
		try
		{
			conn = ConnectionManager.getDBConnection();
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTERATION_DEFAULT_STATUS")).length()>0){
				registerationStatus = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTERATION_DEFAULT_STATUS"));
			}
		
			eventsList = UsersDAO.getEventDetails(Integer.parseInt(eventID));
			String sql ="INSERT INTO EVENT_REGISTRATION(EVENT_REG_ID,EVENT_ID,FIRST_NAME,LAST_NAME,ADDRESS,EMAIL,PHONE_NUMBER,EVENT_SUBSCRIPTION, PAYMENT_MODE,EVENT_SUBSCRIPTION_NOTE,COMPANY_NAME,JOB_TITLE,DEPARTMENT,FAX_NUMBER,REGISTRATION_STATUS,PARENT_ID,ADDRESS2,CITY,STATE,COUNTRY,ZIPCODE,PO_NUMBER,REGISTERED_DATE,UPDATED_DATETIME, CONTACT_MOBILE_PHONE, NEWS_LETTER,ACCOUNT_NUMBER,TERRITORY_MANAGER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE,?,?,?,?)";
			int eventRegId = CommonDBQuery.getSequenceId("EVENT_REG_ID_SEQ");

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventRegId);
			pstmt.setInt(2, Integer.parseInt(eventID));
			pstmt.setString(3, contactFirstName);
			pstmt.setString(4, contactLastName);
			String address = contactAddress1+" "+contactAddress2+" "+contactCity+" "+contactState+" "+contactCountry+"-"+contactPoBox;

			pstmt.setString(5, contactAddress1);
			pstmt.setString(6, contactemailAddress);
			pstmt.setString(7, contactPhone);
			if(howToContact!=null){
				pstmt.setString(8, howToContact);
			}else{
				pstmt.setString(8, "N");
			}
			
			if(paymentOption!=null && !paymentOption.trim().equalsIgnoreCase(""))
				pstmt.setString(9, paymentOption);
			else
				pstmt.setString(9, "Not Required");
			/*if(paymentOption!=null && paymentOption.trim().equalsIgnoreCase("BillMe")){
				pstmt.setString(9, "Bill Me Later");
			}else if(paymentOption!=null && paymentOption.trim().equalsIgnoreCase("PurchaseOrder")){
				pstmt.setString(9, "Purchase Order");
				pstmt.setString(9, "Not Required");
			}else if(paymentOption==null){
			}*/

			pstmt.setString(10, registrantNote);
			pstmt.setString(11, companyName);
			pstmt.setString(12, jobTitle);
			pstmt.setString(13, department);
			pstmt.setString(14, faxNumber);
			pstmt.setString(15, CommonUtility.validateString(registerationStatus));
			pstmt.setInt(16, 0);
			pstmt.setString(17, contactAddress2);
			pstmt.setString(18, contactCity);
			pstmt.setString(19, contactState);
			pstmt.setString(20, contactCountry);
			pstmt.setString(21, contactPoBox);
			if(paymentOption!=null && paymentOption.trim().equalsIgnoreCase("Purchase Order")){
				pstmt.setString(22, purchaseOrderNumber);
			}else{
				pstmt.setString(22, "");
			}
			pstmt.setString(23, contactMobilePhone);
			pstmt.setString(24, newsLetterContactUs);
			pstmt.setString(25, accountNumber);
			pstmt.setString(26, territoryManager);

			count = pstmt.executeUpdate();
			ConnectionManager.closeDBConnection(conn);	
			ConnectionManager.closeDBPreparedStatement(pstmt);
			if(count > 0){
				session.setAttribute("eventRegId", eventRegId);
				if(add_info.equalsIgnoreCase("1")){
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EVENT_REGISTRATION_FOR_PARTICIPANT")).length()>0 && CommonDBQuery.getSystemParamtersList().get("EVENTS_MAIL_TO_REGISTERER").equalsIgnoreCase("Y")){
						insertStudentInfo(0);
					}
					int cnt = insertStudentInfo(eventRegId);
					if(cnt>0){
						int result = insertAdditionalInformation(eventRegId,cnt);
						if(result>0) {
							System.out.println("Student Registered Successfully");
						}
					}
				}
				if(CommonUtility.validateString(registerationStatus).equalsIgnoreCase("Confirmed")){
					increaseEventBookedSeatCount(CommonUtility.validateNumber(eventID));
				}
				
				if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventregistrationmessage")).length()>0){
					result= "0|"+CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventregistrationmessage"));
				}else{
					result= "0|Thank you for your registration. You will receive an email shortly with additional information.";
				}
				UsersDAO.getEventCal("");
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm"); 
				SimpleDateFormat time = new SimpleDateFormat("HH:mm");
				String date = sdf.format(new Date());
				eventsDetails.setFirstName(contactFirstName);
				eventsDetails.setLastName(contactLastName);
				eventsDetails.setToday(date);
				eventsDetails.setAddress(address);
				eventsDetails.setStudentFirstName(addFirstName);
				eventsDetails.setStudentLastName(addLastName);
				EventModel event = UsersDAO.getEventDetails(Integer.parseInt(eventID));
				eventsDetails.setTitle(event.getTitle());
				Date startDate = dateFormat.parse(event.getDate());
				eventsDetails.setEventDate(sdf.format(startDate));
				Date t1 = dateFormat.parse(event.getDate());
				Date t2 = dateFormat.parse(event.getEnd());			 
				eventsDetails.setEventTime(time.format(t1)+" AM - "+time.format(t2)+" PM");
				eventsDetails.setPaymentMode(paymentOption);
				if(paymentOption!=null && paymentOption.trim().equalsIgnoreCase("Purchase Order")){
				eventsDetails.setPoNum(purchaseOrderNumber);
				}
				eventsDetails.setCost(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(event.getCost())));
				eventsDetails.setCourseLocation(event.getLocation());
				eventsDetails.setContactEmail(contactemailAddress);
				eventsDetails.setStudentEmail(addEmail);
				eventsDetails.setCompanyName(companyName);
				eventsDetails.setStudentPhone(addWorkPhone);
				eventsDetails.setContactPhone(contactPhone);
				eventsDetails.setContactMobilePhone(contactMobilePhone);
				eventsDetails.setStudentCompany(addCompany);
				eventsDetails.setAccountNumber(accountNumber);
				eventsDetails.setTerritoryManager(territoryManager);
				eventsDetails.setRegistrantNote(registrantNote);
				System.out.println(eventTitle);
				SendMailUtility sendMailUtility = new SendMailUtility();
				boolean flag = sendMailUtility.sendEventMailToCoOrdinator(Integer.parseInt(eventID),eventsDetails);
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EVENTS_MAIL_TO_REGISTERER")).length()>0 && CommonDBQuery.getSystemParamtersList().get("EVENTS_MAIL_TO_REGISTERER").equalsIgnoreCase("Y")){
					flag = sendMailUtility.sendEventMailToRegisterer(Integer.parseInt(eventID),eventsDetails);
				}
				if(flag){
					int cnt = UsersDAO.updateMailSentStatus(eventRegId);
					if(cnt>0){
						System.out.println("Event Registration Mail Sent Successfully!");
						if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventregistrationmessage")).length()>0){
							result= "0|"+CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventregistrationmessage"));
						}else{
							result= "0|Thank you for your registration. You will receive an email shortly with additional information.";
						}
					}
				}else{
					result= "0|Mail not sent successfully!";
					System.out.println("Mail not sent successfully!");
				}

				/* SendMailUtility.sendEventMail(contactemailAddress, contactFirstName, contactLastName,eventTitle);
				 */
			}else{
				if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventcancelmessage")).length()>0){
					result= "0|"+CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventcancelmessage"));
				
				}else{
					result= "1|Registration to Event Failed! Please try again Later";
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			result= "1|Something went wrong. Please try again.";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result= "1|Something went wrong. Please try again.";
		}
		finally
		{
			ConnectionManager.closeDBConnection(conn);	
			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}
		}else{
			result = "2|Invalid Captcha. Please try again.";
		}
		renderContent = result;
		return SUCCESS;
	
			}


	public final String RegisterToEventAndPayment() {


		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection conn = null;
		PreparedStatement pstmt = null;
		EventDetails eventsDetails = new EventDetails();
		SalesModel erpOrderDetail = new SalesModel();
		String registerationStatus = "Unconfirmed";
		int count = 0;
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();

		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		userId = CommonUtility.validateNumber(sessionUserId);	
		if(contactCountry!=null && CommonUtility.validateString(contactCountry).equalsIgnoreCase("USA")) {
			contactCountry ="US";
		}
		String userToken = (String) session.getAttribute("userToken"); 
        String wareHousecode = (String) session.getAttribute("wareHouseCode"); 
        Double approAmount = 0.0;
        String createEventOrder = request.getParameter("createEventOrder");
        String paymentOption  = request.getParameter("paymentMethod");
        String eventTitle  = request.getParameter("eventTitle");
        String eventID  = request.getParameter("eventID");
		try
		{
			conn = ConnectionManager.getDBConnection();
			
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTERATION_DEFAULT_STATUS")).length()>0){
				registerationStatus = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTERATION_DEFAULT_STATUS"));
			}
		
			eventsList = UsersDAO.getEventDetails(Integer.parseInt(eventID));
			String sql ="INSERT INTO EVENT_REGISTRATION(EVENT_REG_ID,EVENT_ID,FIRST_NAME,LAST_NAME,ADDRESS,EMAIL,PHONE_NUMBER,EVENT_SUBSCRIPTION, PAYMENT_MODE,EVENT_SUBSCRIPTION_NOTE,COMPANY_NAME,JOB_TITLE,DEPARTMENT,FAX_NUMBER,REGISTRATION_STATUS,PARENT_ID,ADDRESS2,CITY,STATE,COUNTRY,ZIPCODE,PO_NUMBER,REGISTERED_DATE,UPDATED_DATETIME, CONTACT_MOBILE_PHONE, NEWS_LETTER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE,?,?)";
			int eventRegId = CommonDBQuery.getSequenceId("EVENT_REG_ID_SEQ");

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventRegId);
			pstmt.setInt(2, Integer.parseInt(eventID));
			pstmt.setString(3, contactFirstName);
			pstmt.setString(4, contactLastName);
			String address = contactAddress1+" "+contactAddress2+" "+contactCity+" "+contactState+" "+contactCountry+"-"+contactPoBox;

			pstmt.setString(5, contactAddress1);
			pstmt.setString(6, contactemailAddress);
			pstmt.setString(7, contactPhone);
			if(howToContact!=null){
				pstmt.setString(8, howToContact);
			}else{
				pstmt.setString(8, "N");
			}
			
			if(paymentOption!=null && !paymentOption.trim().equalsIgnoreCase(""))
				pstmt.setString(9, paymentOption);
			else
			pstmt.setString(9, "Not Required");
			pstmt.setString(10, registrantNote);
			pstmt.setString(11, companyName);
			pstmt.setString(12, jobTitle);
			pstmt.setString(13, department);
			pstmt.setString(14, faxNumber);
			pstmt.setString(15, CommonUtility.validateString(registerationStatus));
			pstmt.setInt(16, 0);
			pstmt.setString(17, contactAddress2);
			pstmt.setString(18, contactCity);
			pstmt.setString(19, contactState);
			pstmt.setString(20, contactCountry);
			pstmt.setString(21, contactPoBox);
			if(paymentOption!=null && paymentOption.trim().equalsIgnoreCase("Purchase Order")){
				pstmt.setString(22, purchaseOrderNumber);
			}else{
				pstmt.setString(22, "");
			}
			pstmt.setString(23, contactMobilePhone);
			pstmt.setString(24, newsLetterContactUs);

			count = pstmt.executeUpdate();
			ConnectionManager.closeDBConnection(conn);	
			ConnectionManager.closeDBPreparedStatement(pstmt);
			if(count > 0){
				if(createEventOrder != null && CommonUtility.validateString(createEventOrder).equalsIgnoreCase("Y")) {
					if((CommonUtility.validateString(payPalToken).length()>0 && CommonUtility.validateString(payPalPayerId).length()>0) ||(CommonUtility.validateString(creditCardPayment).equalsIgnoreCase("Y") && CommonUtility.validateString(ccTransactionId).length()>0))
					{
						String paymentGatewayType = "";
						if(session.getAttribute("PAYMENT_GATEWAY")!=null && session.getAttribute("PAYMENT_GATEWAY").toString().length()>0){
							paymentGatewayType = (String)session.getAttribute("PAYMENT_GATEWAY");
						}else{
							paymentGatewayType = CommonDBQuery.getSystemParamtersList().get("PAYMENT_GATEWAY");
						}
	
						if(CommonUtility.validateString(payPalToken).length()>0 && CommonUtility.validateString(payPalPayerId).length()>0){
							creditCardValue = new CreditCardModel();
							creditCardValue.setPayPalPayerId(CommonUtility.validateString(payPalPayerId));
							creditCardValue.setPayPalToken(CommonUtility.validateString(payPalToken));
						}else{
							ccNumber = ccNumber.replaceAll("XXXX-", "");
							ccNumber = ccNumber.replaceAll("\\*", "");
							ccNumber = ccNumber.replaceAll("#", "");
							ccNumber = ccNumber.replaceAll("-", "");
							creditCardValue = new CreditCardModel();
							creditCardValue.setCardHolder(cardHolder);
							creditCardValue.setDate(ccExp);
							creditCardValue.setElementPaymentAccountId(ccTransactionId);
							creditCardValue.setAddress1(streetAddress);
							creditCardValue.setZipCode(postalCode);
	
							creditCardValue.setPaymentGatewayType(paymentGatewayType);
	
							creditCardValue.setCreditCardResponseCode(ccResponseCode);
							creditCardValue.setCreditCardStatus(ccStatus);
							creditCardValue.setCreditCardHostRefNumber(ccHostRefNumber);
							creditCardValue.setCreditCardTaskID(ccTaskID);
							creditCardValue.setCreditCardAmount(ccAmount);
							creditCardValue.setCreditCardDeclineResponseReason(ccDeclineResponseReason);
							creditCardValue.setCreditCardCvv2VrfyCode(ccCvv2VrfyCode);
							creditCardValue.setCreditCardTip(ccTip);
							creditCardValue.setCreditCardTransTimeStamp(ccTransTimeStamp);
							creditCardValue.setCreditCardToken(ccToken);
							creditCardValue.setCreditCardApprovedAmount(ccApprovedAmount);
							creditCardValue.setCreditCardRequestedAmount(ccRequestedAmount);
							creditCardValue.setCreditCardHostResponseCode(ccHostResponseCode);
							creditCardValue.setCreditCardInvoice(ccInvoice);
							creditCardValue.setCreditCardApprovalCode(ccApprovalCode);
							creditCardValue.setCreditCardTransactionID(ccTransactionId);
							creditCardValue.setCreditCardServerTimestamp(ccServerTimestamp);
							creditCardValue.setDataValue(dataValue);
							if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("P21_CREDIT_CARD_TYPES")).equalsIgnoreCase("Y")){
								if(!CommonUtility.validateString(ccType).equalsIgnoreCase("")){
									List<CustomTable> creditCardType = CIMM2VelocityTool.getInstance().getCusomTableData("Website", "CREDIT_CARD_TYPE");
									String credictCardCode = CIMM2VelocityTool.getInstance().getCreditCardCode(creditCardType, ccType);
									if(credictCardCode!=null){
										creditCardValue.setCreditCardType(credictCardCode);
									}else{
										creditCardValue.setCreditCardType(ccType);
									}
								}
							}else{
								creditCardValue.setCreditCardType(ccType);
							}
							System.out.println("Credit card Type--------:"+ccType);
							creditCardValue.setCreditCardFee(ccFee);
							creditCardValue.setCreditCardExternalSessionID(ccExternalSessionID);
							creditCardValue.setCreditCardAddVrfyCode(ccAddVrfyCode);
							creditCardValue.setCreditCardTax(ccTax);
							creditCardValue.setCreditCardNewDomainKey(ccNewDomainKey);
							creditCardValue.setCreditCardNumber(ccNumber);
							creditCardValue.setCreditCardRefNumber(ccRefrenceCode);
							creditCardValue.setCreditCardActualOrderTotal(actualOrderTotal);
							creditCardValue.setCreditCardMerchantId(ccMerchantId);
							creditCardValue.setSaveCard(saveCard);
						}
						session.setAttribute("creditCardFlag", "true");
					}
					
					HashMap<String, Integer> userAddressId = new HashMap<String, Integer>();
					if(session != null && session.getAttribute("defaultBillToId") != null && !CommonUtility.validateString(session.getAttribute("defaultBillToId").toString()).equals("") && session.getAttribute("defaultShipToId") != null && !CommonUtility.validateString(session.getAttribute("defaultShipToId").toString()).equals("")) {
						defaultBillToId = CommonUtility.validateNumber(session.getAttribute("defaultBillToId").toString());
						defaultShipToId = CommonUtility.validateNumber(session.getAttribute("defaultShipToId").toString());
					}else {
						userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook((String)session.getAttribute(Global.USERNAME_KEY));
						defaultBillToId = userAddressId.get("Bill");
						defaultShipToId = userAddressId.get("Ship");
					}
					session.setAttribute("defaultBillToId", ""+defaultBillToId);
					HashMap<String, UsersModel> userAddress = UsersDAO.getUserAddressFromBCAddressBook(defaultBillToId,defaultShipToId);
					UsersModel userBillAddress = userAddress.get("Bill");
					userBillAddress.setShippingInstruction(registrantNote);
					userBillAddress.setReqDate(reqDate);
					
					UsersModel userShipAddress = userAddress.get("Ship");
					String shipTofirstName=CommonUtility.validateString(userShipAddress.getFirstName());
					String shipToLastName=CommonUtility.validateString(userShipAddress.getLastName());
					session.setAttribute("shipTofirstName", shipTofirstName);
					session.setAttribute("shipToLastName", shipToLastName);
					
					String billEntityId = userAddress.get("Bill").getEntityId();
					String shipEntityId = userAddress.get("Ship").getEntityId();
					
					if(userId<=1){
						userShipAddress.setCompanyName(companyName);
						userShipAddress.setFirstName(contactFirstName);
						userShipAddress.setLastName(contactLastName);
						userShipAddress.setAddress1(contactAddress1);
						userShipAddress.setAddress2(contactAddress2);
						userShipAddress.setCity(contactCity);
						userShipAddress.setState(contactState);
						userShipAddress.setCountry(contactCountry);
						userShipAddress.setZipCode(contactPoBox);
						userShipAddress.setFaxNumber(faxNumber);
						userShipAddress.setPhoneNo(contactPhone);
						userShipAddress.setEmailAddress(contactemailAddress);
						userShipAddress.setEmailId(contactemailAddress);
						
						userBillAddress.setCompanyName(companyName);
						userBillAddress.setFirstName(contactFirstName);
						userBillAddress.setLastName(contactLastName);
						userBillAddress.setAddress1(contactAddress1);
						userBillAddress.setAddress2(contactAddress2);
						userBillAddress.setCity(contactCity);
						userBillAddress.setState(contactState);
						userBillAddress.setCountry(contactCountry);
						userBillAddress.setZipCode(contactPoBox);
						userBillAddress.setFaxNumber(faxNumber);
						userBillAddress.setPhoneNo(contactPhone);
						userBillAddress.setEmailAddress(contactemailAddress);
						userBillAddress.setEmailId(contactemailAddress);
					}
					
					ProductsModel itmVal = new ProductsModel();
					ArrayList<ProductsModel> itemDetailObject = new ArrayList<ProductsModel>();
					Double evtPrice = CommonUtility.validateDoubleNumber(eventPrice);
					itmVal.setPartNumber(CommonUtility.validateString(eventPartNumber));
					itmVal.setPrice(evtPrice);
					itmVal.setCustomerPrice(evtPrice);
					itmVal.setUnitPrice(evtPrice);
					itmVal.setExtendedPrice(evtPrice * CommonUtility.validateNumber(participantCount));
					itmVal.setQty(CommonUtility.validateNumber(participantCount));
	                itmVal.setLineItemComment(eventTitle);
					itemDetailObject.add(itmVal);
					
					SalesOrderManagementModel salesOrderInput = new SalesOrderManagementModel();
					salesOrderInput.setOrderItems(itemDetailObject);
					salesOrderInput.setShipAddress(Cimm2BCentralClient.getInstance().userModelToAddressModel(userShipAddress));
					salesOrderInput.setBillAddress(Cimm2BCentralClient.getInstance().userModelToAddressModel(userBillAddress));
					salesOrderInput.setOrderType(orderingType);
					salesOrderInput.setShipVia("");
					salesOrderInput.setShipViaDescription("");
					salesOrderInput.setShippingInstruction(shippingInstruction);
					salesOrderInput.setOrderNotes(orderNotes);
					salesOrderInput.setOrderedBy(contactFirstName + contactLastName);
					salesOrderInput.setReqDate(reqDate);
					salesOrderInput.setSession(session);
					salesOrderInput.setBillEntityId(billEntityId);
					salesOrderInput.setShipEntityId(shipEntityId);
					salesOrderInput.setUserToken(userToken);
					salesOrderInput.setUserName(contactemailAddress);
					salesOrderInput.setCreditCardValue(creditCardValue);
					salesOrderInput.setPurchaseOrderNumber(purchaseOrderNumber);
					salesOrderInput.setSelectedBranch(wareHousecode);
					salesOrderInput.setCountry(country);
					salesOrderInput.setCCType(ccType);
					salesOrderInput.setCCAuth(ccAuthCode);
					salesOrderInput.setDataValue(dataValue);
					salesOrderInput.setOrderTax(CommonUtility.validateDoubleNumber(orderTax));
					if(CommonUtility.validateString(ccApprovedAmount).length()>0){
						approAmount = Double.parseDouble(ccApprovedAmount);
					}
					salesOrderInput.setCCAmount(approAmount);
					salesOrderInput.setCCToken(ccToken);
					salesOrderInput.setErpOrderType(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ORDERING_DEFAULT_ORDER_TYPE")));
					SalesOrderManagement salesOrderSubmit = new SalesOrderManagementImpl();					
					erpOrderDetail = salesOrderSubmit.submitEventOrderToERP(salesOrderInput);
					contentObject.put("orderDetail", erpOrderDetail);
					contentObject.put("confirmationId", erpOrderDetail.getOrderID());					
					contentObject.put("eventCost", eventPrice);					
					contentObject.put("purchaseOrderNumber", erpOrderDetail.getPoNumber());
					contentObject.put("paymentMode",paymentOption);
					contentObject.put("externalSystemId", erpOrderDetail.getExternalSystemId());
				}
				contentObject.put("eventTitle", eventTitle);
				contentObject.put("firstName", contactFirstName);
				contentObject.put("lastName", contactLastName);
				contentObject.put("eventRegistrationId", eventRegId);
				contentObject.put("eventId", eventID);
				session.setAttribute("eventRegId", eventRegId);
				if(CommonUtility.validateString(add_info).equalsIgnoreCase("1")){
					int cnt = insertStudentInfo(eventRegId);
					if(cnt>0){
						int result = insertAdditionalInformation(eventRegId,cnt);
						if(result>0) {
							System.out.println("Student Registered Successfully");
						}
					}
				}
				if(CommonUtility.validateString(registerationStatus).equalsIgnoreCase("Confirmed")){
					increaseEventBookedSeatCount(CommonUtility.validateNumber(eventID));
				}
				
				if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventregistrationmessage")).length()>0){
					result= "0|"+CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventregistrationmessage"));
				}else{
					result= "0|Thank you for your registration. You will receive an email shortly with additional information.";
				}
				UsersDAO.getEventCal("");
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm"); 
				SimpleDateFormat time = new SimpleDateFormat("HH:mm");
				String date = sdf.format(new Date());
				eventsDetails.setFirstName(contactFirstName);
				eventsDetails.setLastName(contactLastName);
				eventsDetails.setToday(date);
				eventsDetails.setAddress(address);
				eventsDetails.setStudentFirstName(addFirstName);
				eventsDetails.setStudentLastName(addLastName);
				EventModel event = UsersDAO.getEventDetails(Integer.parseInt(eventID));
				eventsDetails.setTitle(event.getTitle());
				Date startDate = dateFormat.parse(event.getDate());
				eventsDetails.setEventDate(sdf.format(startDate));
				Date t1 = dateFormat.parse(event.getDate());
				Date t2 = dateFormat.parse(event.getEnd());			 
				eventsDetails.setEventTime(time.format(t1)+" AM - "+time.format(t2)+" PM");
				eventsDetails.setPaymentMode(paymentOption);
				if(paymentOption!=null && paymentOption.trim().equalsIgnoreCase("Purchase Order")){
				eventsDetails.setPoNum(purchaseOrderNumber);
				};
				eventsDetails.setCost(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(event.getCost())));
				eventsDetails.setCourseLocation(event.getLocation());
				eventsDetails.setContactEmail(contactemailAddress);
				eventsDetails.setStudentEmail(addEmail);
				eventsDetails.setCompanyName(companyName);
				eventsDetails.setStudentPhone(addWorkPhone);
				eventsDetails.setContactPhone(contactPhone);
				eventsDetails.setContactMobilePhone(contactMobilePhone);
				eventsDetails.setStudentCompany(addCompany);
				eventsDetails.setErpOrderNumber(erpOrderDetail.getErpOrderNumber());

				System.out.println(eventTitle);
				SendMailUtility sendMailUtility = new SendMailUtility();
				boolean flag = sendMailUtility.sendEventMailToCoOrdinator(Integer.parseInt(eventID),eventsDetails);
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EVENTS_MAIL_TO_REGISTERER")).length()>0 && CommonDBQuery.getSystemParamtersList().get("EVENTS_MAIL_TO_REGISTERER").equalsIgnoreCase("Y")){
					flag = sendMailUtility.sendEventMailToRegisterer(Integer.parseInt(eventID),eventsDetails);
				}
				if(flag){
					int cnt = UsersDAO.updateMailSentStatus(eventRegId);
					if(cnt>0){
						System.out.println("Event Registration Mail Sent Successfully!");
						/*if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventregistrationmessage")).length()>0){
							result= "0|"+CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventregistrationmessage"));
						}else{
							result= "0|Thank you for your registration. You will receive an email shortly with additional information.";
						}*/
					}
					String eventMessage = "Thank you for your registration. You will receive an email shortly with additional information.";
					contentObject.put("createEventOrder", createEventOrder);	
					contentObject.put("createEventMessage", eventMessage);	
					renderContent = LayoutGenerator.templateLoader("EventPaymentResponse", contentObject, null, null, null);
				}else{
					/*result= "0|Mail not sent successfully!";
					System.out.println("Mail not sent successfully!");*/
					String eventMessage = "Mail not sent successfully! Please try again Later.";
					contentObject.put("createEventOrder", createEventOrder);	
					contentObject.put("createEventMessage", eventMessage);	
					contentObject.put("isEventCancelled", "Y");					
					renderContent = LayoutGenerator.templateLoader("EventPaymentResponse", contentObject, null, null, null);
				}

				/* SendMailUtility.sendEventMail(contactemailAddress, contactFirstName, contactLastName,eventTitle);
				 */
			}else{
				/*if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventcancelmessage")).length()>0){
					result= "0|"+CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("event.label.eventcancelmessage"));
				
				}else{
					result= "1|Registration to Event Failed! Please try again Later";
				}*/			
				String eventMessage = "Registration to Event Failed! Please try again Later.";
				contentObject.put("createEventOrder", createEventOrder);
				contentObject.put("isEventCancelled", "Y");
				contentObject.put("createEventMessage", eventMessage);
				renderContent = LayoutGenerator.templateLoader("EventPaymentResponse", contentObject, null, null, null);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			String eventMessage = "Something went wrong. Please try again.";
			contentObject.put("createEventOrder", createEventOrder);
			contentObject.put("isEventCancelled", "Y");
			contentObject.put("createEventMessage", eventMessage);
			renderContent = LayoutGenerator.templateLoader("EventPaymentResponse", contentObject, null, null, null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			String eventMessage = "Something went wrong. Please try again.";
			contentObject.put("createEventOrder", createEventOrder);
			contentObject.put("isEventCancelled", "Y");
			contentObject.put("createEventMessage", eventMessage);
			renderContent = LayoutGenerator.templateLoader("EventPaymentResponse", contentObject, null, null, null);
		}
		finally
		{
			ConnectionManager.closeDBConnection(conn);	
			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}
		
		return SUCCESS;
	
		}

	public final int insertAdditionalInformation(final int parent, final int child) {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnectionManager.getDBConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String sql = "INSERT INTO EVENT_REGISTRATION_EXTRA_INFO (EVENT_EXTRA_INFO_ID,PARENT_ID,CHILD_ID,UPDATED_DATETIME)VALUES(?,?,?,SYSDATE)";

			int eventExtraInfoID = CommonDBQuery.getSequenceId("EVENT_EXTRA_INFO_ID_SEQ");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventExtraInfoID);
			pstmt.setInt(2, parent);
			pstmt.setInt(3, child);

			count = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);

		}

		return count;
	}

	public final int insertStudentInfo(final int parentID) {
		int count = 0;
		int eventRegId = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnectionManager.getDBConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			eventsList = UsersDAO.getEventDetails(Integer.parseInt(eventID));
			String sql = "INSERT INTO EVENT_REGISTRATION(EVENT_REG_ID,EVENT_ID,FIRST_NAME,LAST_NAME,ADDRESS,EMAIL,PHONE_NUMBER,EVENT_SUBSCRIPTION, PAYMENT_MODE,EVENT_SUBSCRIPTION_NOTE,COMPANY_NAME,JOB_TITLE,DEPARTMENT,FAX_NUMBER,REGISTRATION_STATUS,PARENT_ID,ADDRESS2,CITY,STATE,COUNTRY,ZIPCODE,PO_NUMBER,REGISTERED_DATE,UPDATED_DATETIME) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE)";

			eventRegId = CommonDBQuery.getSequenceId("EVENT_REG_ID_SEQ");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventRegId);
			pstmt.setInt(2, Integer.parseInt(eventID));
			pstmt.setString(3, addFirstName);
			pstmt.setString(4, addLastName);
			String address = addStreetAddr + " " + addStreetAddr1 + " " + addCity + " " + addState + " " + addCountry
					+ "-" + addPostal;

			pstmt.setString(5, addStreetAddr);
			pstmt.setString(6, addEmail);
			pstmt.setString(7, addWorkPhone);
			if(howToContact!=null){
				pstmt.setString(8, howToContact);
			}else{
				pstmt.setString(8, "N");
			}
			pstmt.setString(9, paymentOption);
			pstmt.setString(10, registrantNote);
			pstmt.setString(11, addCompany);
			pstmt.setString(12, title1);

			pstmt.setString(13, department);

			pstmt.setString(14, addFax);
			pstmt.setString(15, "Unconfirmed");
			pstmt.setInt(16, parentID);
			pstmt.setString(17, addStreetAddr1);
			pstmt.setString(18, addCity);
			pstmt.setString(19, addState);
			pstmt.setString(20, addCountry);
			pstmt.setString(21, addPostal);
			pstmt.setString(22, purchaseOrderNumber);

			count = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);

		}
		if (count > 0) {
			return eventRegId;
		} else {
			return count;
		}
	}

	public final int increaseEventBookedSeatCount(int eventId) {
		int count = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnectionManager.getDBConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String sql = "UPDATE EVENT_MANAGER SET BOOKED_SEATS=BOOKED_SEATS+1  WHERE EVENT_ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventId);
			count = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);

		}
		System.out.println("increaseEventBookedSeatCount : " + count);
		return count;
	}

	public String calEvents() {
		try {
			eventCatList = UsersDAO.getEventCategories();
			eventsCustomFieldList = UsersDAO.getEventCustomFields();
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("eventCatList", eventCatList);
			contentObject.put("eventsCustomFieldList", eventsCustomFieldList);
			contentObject.put("eventsLocations", UsersDAO.getEventLocation());
			renderContent = LayoutGenerator.templateLoader("CalendarEventsPage", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return "SESSIONEXPIRED";
		}
		return SUCCESS;
	}
	public String calendarEvent() {
		request = ServletActionContext.getRequest();
		String upcomingEvents = (String) request.getParameter("upcomingEvents");
		String staticPagePath = CommonDBQuery.getSystemParamtersList().get("EVENTSDOC");
		String FirstLoad = "";
		String reqType="";
		reqType= request.getParameter("reqType");
		FirstLoad = request.getParameter("FirstLoadEvent");
		File file = new File(staticPagePath);
		SAXBuilder builder = new SAXBuilder();
		Gson gson = new Gson();
		try {

			if (file.exists()) {

				if (CommonUtility.validateString(FirstLoad).equalsIgnoreCase("Y")) {
					UsersDAO.getEventCal(upcomingEvents);
				}
				org.jdom.Document dom = builder.build(file);
				// newsPageContent=dom.getRootElement().getChildText("CalendarEvent");
				result = dom.getRootElement().getChildText("CalendarEvent");
			} else {
				UsersDAO.getEventCal(upcomingEvents);
				org.jdom.Document dom = builder.build(file);
				// newsPageContent=dom.getRootElement().getChildText("CalendarEvent");
				result = dom.getRootElement().getChildText("CalendarEvent");

			}
		}

		catch (Exception e) {

			try {
				// creating Events new File if any exception. To recover automatically.
				UsersDAO.getEventCal(upcomingEvents);
				org.jdom.Document dom = builder.build(file);
				// newsPageContent=dom.getRootElement().getChildText("CalendarEvent");
				result = dom.getRootElement().getChildText("CalendarEvent");
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();

		}
		if(CommonUtility.validateString(reqType).equalsIgnoreCase("widget")){
			 String eventJson= "{\"data\":{\"eventsList\":"+result+"}}";
			 result = gson.toJson(eventJson);
		}
		renderContent = result;
		return SUCCESS;
	}

	public static void setBillToAndShipToInSession(int defaultBillToId, int defaultShipToId, HttpSession session) {
		long startTimer = CommonUtility.startTimeDispaly();
		try {
			String userName = (String) session.getAttribute(Global.USERNAME_KEY);
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			if (defaultShipToId == 0) {
				if (CommonUtility.validateString((String) session.getAttribute("defaultShipToId")).length() == 0) {
					HashMap<String, Integer> defaultAddresssId = UsersDAO.getDefaultAddressId(userId);
					int currentDefaultId = defaultAddresssId.get("Ship");
					defaultBillToId = defaultAddresssId.get("Bill");
					session.setAttribute("defaultShipToId", Integer.toString(currentDefaultId));
				} else {
					defaultShipToId = CommonUtility.validateNumber((String) session.getAttribute("defaultShipToId"));
				}
			}

			if (defaultBillToId == 0) {
				HashMap<String, Integer> userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook(userName);
				defaultBillToId = userAddressId.get("Bill");
			}

			UserManagement usersObj = new UserManagementImpl();
			HashMap<String, UsersModel> userAddress = usersObj.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
			UsersModel billAddress = userAddress.get("Bill");
			UsersModel shipAddress = userAddress.get("Ship");

			session.setAttribute("defaultBillAddress", billAddress);
			session.setAttribute("defaultShipAddress", shipAddress);
			if (shipAddress != null) {
				session.setAttribute("selectedShipToId", shipAddress.getShipToId());
			} else {
				session.setAttribute("selectedShipToId", 0);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	}

	public String updateUserContactEmail() {

		target = SUCCESS;

		try {

			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			boolean isValidUser = true;
			result = "";
			if (userId > 1) {

				emailAddress = request.getParameter("emailAddress");
				if (request.getParameterMap().containsKey("emailAddress")
						&& (emailAddress == null || emailAddress.trim().equalsIgnoreCase(""))) {
					isValidUser = false;
					result = result + "Please Enter Email Address.|";
				} else {
					if (emailAddress != null && !CommonUtility.validateEmail(emailAddress)) {
						isValidUser = false;
						result = result + "Please Enter valid Email Address.|";
					}
				}

				if (isValidUser) {
					UsersModel userModel = new UsersModel();
					userModel.setUserId(userId);
					userModel.setEmailAddress(emailAddress);
					int count = UsersDAO.updateUserContactEmail(userModel);
					if (count > 0) {
						session.setAttribute("userEmailAddress", emailAddress);
						// result = "Successfully Updated Email Address";
						result = "Successfully Updated Email Address.\nPlease note:  Your original email address will still serve as your User Name.";

					} else {
						result = "Failed to Update Email Address";
					}

				}

				renderContent = result;
			} else {
				target = "SESSIONEXPIRED";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;

	}

	public String aRPasswordValidation() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			result = (String) session.getAttribute("ARPassword");
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			userId = Integer.parseInt(sessionUserId);
			target = SUCCESS;
			if (userId > 1) {
				if (result.equals(ARPassword)) {
					if (CommonUtility.validateString(ARPasswordVerificationType).length() > 0
							&& ARPasswordVerificationType.equalsIgnoreCase("ship")) {
						result = "1|Password matched.";
						session.setAttribute("ARShipPassword", null);
					} else if (CommonUtility.validateString(ARPasswordVerificationType).length() > 0
							&& ARPasswordVerificationType.equalsIgnoreCase("bill")) {
						result = "1|Password matched.";
						session.setAttribute("ARBillPassword", null);
					} else {
						result = "0|Failed, Password did not match.";
					}
				}
				renderContent = result;

			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "SESSIONEXPIRED";
		}
		return target;
	}

	public String copperFeed() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String symbol = "/" + CommonDBQuery.getSystemParamtersList().get("COPPERGRAPH_SYMBOL");
			String locale = session.getAttribute("localeCode").toString().toUpperCase();
			renderContent = DxFeedDAOService.getInstance().loadCopperFeedDataAndReturn(resultPage, symbol, locale);
		} catch (Exception e) {
			e.printStackTrace();
			return "SESSIONEXPIRED";
		}
		return SUCCESS;
	}

	public final String validateCxmlUser() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String uId = request.getParameter("punchoutAuthenticationId");
		int userId = UsersDAO.getLogidDetail(uId);
		/*
		 * System.out.println("session id at validation : " + session.getId());
		 * session.invalidate(); session = request.getSession();
		 * System.out.println("session id New validation : " + session.getId());
		 */
		// Please verify this
		target = "welcome";
		if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CXML_REDIRECT_TO")).length() > 0) {
			target = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CXML_REDIRECT_TO"));
		}
		if (userId > 0) {
			try {
				ociSession = session.getId();
				session.removeAttribute("cartCountSession");
				session.setAttribute(Global.USERID_KEY, Integer.toString(userId));
				session.setAttribute("canSubmitPO", "Y");
				session.setAttribute("canSubmitNPR", "Y");
				session.setAttribute("canSubmitRFQ", "Y");
				session.setAttribute("isOciUser", 2);
				session.setAttribute("isPunchoutUser", "Y");
				session.setAttribute("isAuthPurchaseAgent", "Y");
				session.setAttribute("isGeneralUser", "N");
				String ipaddress = request.getHeader("X-Forwarded-For");
				if (ipaddress == null) {
					ipaddress = request.getRemoteAddr();
				}
				// loadShipDetailsForUser(); For Shipvia list
				System.out.println("At Login Punchout user Ip : " + ipaddress);
				UsersDAO.updateUserLog(userId, "Punchout Login", session.getId(), ipaddress, "Punchout Login", "NA");
			} catch (Exception e) {
				e.printStackTrace();
				target = "SESSIONEXPIRED";
			}
		} else {
			renderContent = "Unauthorized : Access is denied due to invalid credentials";
			target = NONE;
		}
		return target;
	}

	public final String validateSapUser() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		target = "welcome";
		if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CXML_REDIRECT_TO")).length() > 0) {
			target = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CXML_REDIRECT_TO"));
		}
		if (username == null) {
			username = request.getParameter("USERNAME");
		}
		if (password == null) {
			password = request.getParameter("PASSWORD");
		}

		if (username == null) {
			username = request.getParameter("UserName");
		}
		if (password == null) {
			password = request.getParameter("Password");
		}

		if (username == null) {
			username = request.getParameter("Username");
		}

		String reqUri = request.getRequestURI().toString();
		String queryString = request.getQueryString();
		System.out.println("Request Url : " + reqUri + "?" + queryString);
		System.out.println("User @ OCI : " + username);
		session.setAttribute("hookUrl", HOOK_URL);
		int userId = UsersDAO.getSapUser(username, password, HOOK_URL, sl_user);
		session.setAttribute(Global.USERID_KEY, Integer.toString(userId));
		String path = request.getRequestURI();
		int ociType = 1;
		if (userId > 0) {
			session.setAttribute("isAuthPurchaseAgent", "Y");
			session.setAttribute("isGeneralUser", "N");
			loginCount++;
			session.setAttribute(Global.USERID_KEY, Integer.toString(userId));
			session.removeAttribute("cartCountSession");
			ociSession = session.getId();
			if (FUNCTION != null && FUNCTION.trim().equalsIgnoreCase("Validate") && PRODUCTID != 0) {
				// price = UsersDAO.priceValidation(PRODUCTID);
				session.setAttribute("isOciUser", 1);
				session.setAttribute("isPunchoutUser", "Y");
				session.setAttribute("canSubmitPO", "Y");
				target = "Validate";
			} else {
				session.setAttribute("canSubmitPO", "Y");
				session.setAttribute("isOciUser", 1);
				session.setAttribute("isPunchoutUser", "Y");
				// target = SUCCESS;
			}
		} else {
			renderContent = "Unauthorized : Access is denied due to invalid credentials";
			target = NONE;
		}
		return target;
	}

	public boolean checkEmailAddress(int userId) {
		boolean isValid = false;
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			/*
			 * String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			 * userId = CommonUtility.validateNumber(sessionUserId);
			 */

			UsersModel userDetail = UsersDAO.getUserEmail(userId);

			if (CommonUtility.validateString(userDetail.getEmailAddress()).length() > 0) {

				String email = userDetail.getEmailAddress();
				isValid = CommonUtility.validateEmail(email);
			}

			/*
			 * if(!isValid){ renderContent = "1|Email address not available"; }else{
			 * renderContent = "0|Success"; }
			 */

			if (isValid) {
				session.setAttribute("emailAddressExits", "Y");
			} else {
				session.setAttribute("emailAddressExits", "N");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValid;
	}

	public String updateContactEmailAddress() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		renderContent = "";
		if (userId > 1) {
			try {
				UsersModel userInfo = new UsersModel();

				userInfo.setEmailAddress(email);
				userInfo.setUserId(userId);
				userInfo.setSession(session);
				userInfo.setUserToken((String) session.getAttribute("userToken"));
				result = UsersDAO.updateContactEmailAddress(userInfo);
				renderContent = result;
			} catch (Exception e) {
				e.printStackTrace();
				return "SESSIONEXPIRED";
			}
			return "ResultLoader";
		} else {
			return "SESSIONEXPIRED";
		}
	}

	public String validateZipCode() {
		boolean zipCodeStatus = false;
		if (CommonUtility.validateString(zipCode).length() > 0) {
			zipCodeStatus = UsersDAO.getZipcode(zipCode);
		}
		if (!zipCodeStatus) {
			result = "0";
		} else {
			result = "1";
		}
		renderContent = result;
		return SUCCESS;
	}

	public String brontoAddContactToList() {

		try {
			request = ServletActionContext.getRequest();
			String emailAddress = (String) request.getParameter("email");

			// List<String> listIds = null;
			String apiToken = CommonUtility
					.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_API_TOKEN"));
			BrontoApi client = BrontoUtility.getInstance().brontoLogin(apiToken);
			BrontoModel brontoModel = new BrontoModel();
			// brontoModel.setInterestedInCommercialDiscounts(true);
			brontoModel.setEmail(emailAddress);
			String contactListName = CommonUtility
					.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_EMAIL_SIGNUP_CONTACT_LIST"));
			if (contactListName.length() > 0) {
				// listIds =
				// BrontoUtility.getInstance().getContactListIds(client,contactListName);
				result = BrontoUtility.getInstance().createOrUpdateContact(client, brontoModel, contactListName,
						BrontoUtility.EMAIL_SIGNUP);
			} else {
				result = "0|Contact List name is not configured.";
			}
			renderContent = result;
		} catch (Exception e) {
			e.printStackTrace();
			return "SESSIONEXPIRED";
		}
		return SUCCESS;
	}

	public String brontoCommercialDiscount() {
		try {
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("responseType", "bronto");
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String billTrust() {

		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		try {

			if (userId > 1) {
				buyingCompanyId = (String) session.getAttribute("buyingCompanyId");

				if (defaultShipToId > 0) {
					session.setAttribute("defaultShipToId", Integer.toString(defaultShipToId));
				}

				String tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
				String tempdefaultBillToId = (String) session.getAttribute("defaultBillToId");

				buyingCompanyId = (String) session.getAttribute("buyingCompanyId");

				if (tempdefaultBillToId != null && tempdefaultBillToId.trim().length() > 0)
					defaultBillToId = (CommonUtility.validateNumber(tempdefaultBillToId));
				if (tempdefaultShipId != null && tempdefaultShipId.trim().length() > 0)
					defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);

				if (defaultBillToId == 0 || defaultShipToId == 0) {
					HashMap<String, Integer> userAddressId = UsersDAO
							.getDefaultAddressIdForBCAddressBook((String) session.getAttribute(Global.USERNAME_KEY));
					// UsersDAO.getDefaultAddressId(userId);
					defaultBillToId = userAddressId.get("Bill");
					defaultShipToId = userAddressId.get("Ship");
				}

				UserManagement userObj = new UserManagementImpl();
				HashMap<String, UsersModel> userAddress = userObj.getUserAddressFromBCAddressBook(defaultBillToId,
						defaultShipToId);

				billAddress = userAddress.get("Bill");
				shipAddress = userAddress.get("Ship");

				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BILLTRUST_INTEGRATION"))
						.equalsIgnoreCase("Y")) {
					LinkedHashMap<String, String> customFieldValuesList = UsersDAO.getAllUserCustomFieldValue(userId);
					if (customFieldValuesList.size() > 0 && CommonUtility
							.validateString(customFieldValuesList.get("ENABLE_BILLTRUST")).equalsIgnoreCase("Y")) {
						String billTrustAccountNumber = UsersDAO.getCustomerCustomTextFieldValue(
								CommonUtility.validateNumber(buyingCompanyId), "BILL_TRUST_ACCOUNT_NUMBER");
						if(billTrustAccountNumber==null && CommonUtility.validateString(billTrustAccountNumber).length() == 0){
							billTrustAccountNumber=(String) session.getAttribute("customerId");
						}
						if (CommonUtility.validateString(billTrustAccountNumber).length() > 0) {
							customFieldValuesList.put("emailAddress", billAddress.getEmailAddress());
							String billTrustURL = TripleDESEncryption.getInstance()
									.getBillTrustURL(billTrustAccountNumber, customFieldValuesList);
							contentObject.put("billTrustExist", "Y");
							contentObject.put("billTrustURL", billTrustURL);
						}
					}
				}
				renderContent = LayoutGenerator.templateLoader("BillTrustPage", contentObject, null, null, null);
				target = SUCCESS;

			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String abandonedCart() {
		target = "abandonedCartLogin";
		try {
			int userId = BrontoDAO.getAbandonedCartUserIdFromKey(propertyKey, sessionId);
			if (userId > 0) {
				// BrontoDAO.abandonedCartLoginDisable(userId, propertyKey, sessionId);
				UsersModel usersModel = UsersDAO.getUserDetailById(userId);
				if (usersModel != null) {
					username = usersModel.getUserName();
					SecureData secureData = new SecureData();
					password = secureData.validatePassword(usersModel.getPassword());
					fromPageValue = "abandonedCart";
				}
			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String myAccountLink() {

		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String customerId = (String) session.getAttribute("customerId");
		// String shipToId = (String)session.getAttribute("selectedShipToId");

		UserManagement usersObj = new UserManagementImpl();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		UsersModel customerInfoInput = new UsersModel();
		try {

			LinkedHashMap<String, String> accountInquiryInput = new LinkedHashMap<String, String>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			accountInquiryInput.put("customerERPId", customerId);
			accountInquiryInput.put("agingDate", dateFormat.format(new Date()));

			customerInfoInput.setCustomerId(CommonUtility.validateString(customerId));
			// customerInfoInput.setShipToId(CommonUtility.validateString(shipToId));

			UserManagementModel customerDataCredit = usersObj.getCustomerDataCredit(customerInfoInput);
			UserManagementModel customerBalanceV2 = usersObj.getCustomerBalanceV2(customerInfoInput);
			UserManagementModel customerDataGeneralV2 = usersObj.getCustomerDataGeneralV2(customerInfoInput);
			UsersModel customerAccountDetail = usersObj.getAccountDetail(accountInquiryInput);

			contentObject.put("customerAccountDetail", customerAccountDetail);
			contentObject.put("customerDataCredit", customerDataCredit);
			contentObject.put("customerBalanceV2", customerBalanceV2);
			contentObject.put("customerDataGeneralV2", customerDataGeneralV2);

			renderContent = LayoutGenerator.templateLoader("MyAccountLinkPage", contentObject, null, null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String InvoiceList() {

		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String customerId = (String) session.getAttribute("customerId");
		String invoiceNumber = CommonUtility.validateString(request.getParameter("invNo"));
		ArrayList<UserManagementModel> customerArInvoiceList = null;
		UserManagement usersObj = new UserManagementImpl();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		UsersModel customerInfoInput = new UsersModel();
		try {

			customerInfoInput.setCustomerId(CommonUtility.validateString(customerId));
			customerInfoInput.setInvoiceNumber(CommonUtility.validateString(invoiceNumber));
			customerArInvoiceList = usersObj.getARGetInvoiceListV2(customerInfoInput);

			contentObject.put("customerArInvoiceList", customerArInvoiceList);

			renderContent = LayoutGenerator.templateLoader("InvoiceListPage", contentObject, null, null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String InvoiceForPayment() {

		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String customerId = (String) session.getAttribute("customerId");
		double totalInvoiceAmount = 0.0;
		double totalInvoiceAmountDue = 0.0;
		ArrayList<UserManagementModel> customerArInvoiceList = null;
		ArrayList<UserManagementModel> customerArInvoiceListOne = null;
		UserManagement usersObj = new UserManagementImpl();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		UsersModel customerInfoInput = new UsersModel();
		try {

			String[] selectedInvoiceForPayment = request.getParameterValues("invoiceListId");
			if (selectedInvoiceForPayment != null && selectedInvoiceForPayment.length > 0) {
				for (String invoiceNumber : selectedInvoiceForPayment) {
					if (CommonUtility.validateNumber(invoiceNumber) > 0) {
						customerInfoInput.setCustomerId(CommonUtility.validateString(customerId));
						customerInfoInput.setInvoiceNumber(invoiceNumber);
						customerArInvoiceListOne = usersObj.getARGetInvoiceListV2(customerInfoInput);
					}
				}
			}

			if (customerArInvoiceListOne != null && customerArInvoiceListOne.size() > 0) {
				customerArInvoiceList = new ArrayList<UserManagementModel>();
				for (UserManagementModel customerArInvoiceModel : customerArInvoiceListOne) {
					if (CommonUtility.validateString(customerArInvoiceModel.getStatusType()).equalsIgnoreCase("Opn")
							&& CommonUtility.validateDoubleNumber(customerArInvoiceModel.getAmount()) > 0
							&& CommonUtility.validateDoubleNumber(customerArInvoiceModel.getAmountDue()) > 0) {
						totalInvoiceAmount = totalInvoiceAmount
								+ CommonUtility.validateDoubleNumber(customerArInvoiceModel.getAmount());
						totalInvoiceAmountDue = totalInvoiceAmountDue
								+ CommonUtility.validateDoubleNumber(customerArInvoiceModel.getAmountDue());
						customerArInvoiceList.add(customerArInvoiceModel);
					}
				}
			}

			contentObject.put("customerArInvoiceList", customerArInvoiceList);
			contentObject.put("customerArInvoiceListOne", customerArInvoiceListOne);
			contentObject.put("totalInvoiceAmount", totalInvoiceAmount);
			contentObject.put("totalInvoiceAmountDue", totalInvoiceAmountDue);

			renderContent = LayoutGenerator.templateLoader("InvoiceDetailPage", contentObject, null, null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public void loadShipDetailsForUser() {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		UserManagement usersObj = new UserManagementImpl();
		try {
			UsersModel customerInfoInput = new UsersModel();
			ArrayList<ShipVia> shipViaList = new ArrayList<ShipVia>();
			customerInfoInput.setUserToken(CommonUtility.validateString((String) session.getAttribute("userToken")));
			customerInfoInput.setEntityId(CommonUtility.validateString((String) session.getAttribute("entityId")));
			customerInfoInput.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
			customerInfoInput
					.setBuyingCompanyId(CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId")));
			customerInfoInput.setSession(session);
			shipViaList = usersObj.getUserShipViaList(customerInfoInput);
			if (shipViaList != null && !shipViaList.isEmpty()) {
				Gson gson = new Gson();
				String shipViaJson = gson.toJson(shipViaList);
				session.setAttribute("customerShipViaListJson", shipViaJson);
			}
			session.setAttribute("customerShipViaList", shipViaList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String ContactUsForm() {
		request = ServletActionContext.getRequest();
		try {
			String locationEmail = CommonUtility.validateString(request.getParameter("locationEmail"));
			String locationName = CommonUtility.validateString(request.getParameter("locName"));
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("locationEmail", locationEmail);
			contentObject.put("locationName", locationName);
			renderContent = LayoutGenerator.templateLoader("ContactUsForm", contentObject, null, null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;

	}

	public String ContactUs() {

		request = ServletActionContext.getRequest();

		try {
			String firstName = CommonUtility.validateString(request.getParameter("firstName"));
			String lastName = CommonUtility.validateString(request.getParameter("lastName"));
			String email = CommonUtility.validateString(request.getParameter("email"));
			String companyName = CommonUtility.validateString(request.getParameter("companyName"));
			String phoneNo = CommonUtility.validateString(request.getParameter("phoneNo"));
			String comments = CommonUtility.validateString(request.getParameter("comments"));
			String toEmail = CommonUtility.validateString(request.getParameter("toEmail"));
			String loacationName = CommonUtility.validateString(request.getParameter("locName"));
			String pageUrl = CommonUtility.validateString(request.getParameter("pageUrl"));

			LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
			parameters.put("First Name", firstName);
			parameters.put("Last Name", lastName);
			parameters.put("Email", email);
			parameters.put("Company Name", companyName);
			parameters.put("Phone No", phoneNo);
			parameters.put("Location Name", loacationName);
			parameters.put("Comments", comments);
			parameters.put("pageUrl", pageUrl);
			SaveCustomFormDetails saveFields = new SaveCustomFormDetails();
			int count = saveFields.saveToDataBase(parameters, "Contact Us Form");
			if (count > 0) {
				System.out.println("Contact Us Form details Saved To DB : ");
			}
			boolean flag = false;
			SendMailUtility sendMailUtility = new SendMailUtility();
			flag = sendMailUtility.sendMailForContactUsForm(firstName, lastName, email, companyName, phoneNo, comments,
					loacationName, toEmail, pageUrl);
			if (flag) {
				renderContent = "Y";
			} else {
				renderContent = "N";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String NewsLetterSubscription() {

		request = ServletActionContext.getRequest();

		try {

			HttpSession session = request.getSession();
			String firstName = CommonUtility.validateString(request.getParameter("firstName"));
			String lastName = CommonUtility.validateString(request.getParameter("lastName"));
			String email = CommonUtility.validateString(request.getParameter("email"));
			String companyName = CommonUtility.validateString(request.getParameter("companyName"));
			String nearestBranch = CommonUtility.validateString(request.getParameter("nearestBranch"));
			String toEmail = CommonUtility.validateString(request.getParameter("toEmail"));
			String comments = CommonUtility.validateString(request.getParameter("comments"));
			String jobTitle = CommonUtility.validateString(request.getParameter("jobTitle"));			
			
			LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
			parameters.put("First Name", firstName);
			parameters.put("Last Name", lastName);
			parameters.put("Email", email);
			parameters.put("Company Name", companyName);
			parameters.put("Nearest Branch location", nearestBranch);
			parameters.put("Comments", comments);
			parameters.put("jobTitle", jobTitle);
			SaveCustomFormDetails saveFields = new SaveCustomFormDetails();
			int count = saveFields.saveToDataBase(parameters,
					LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase())
							.getProperty("sentmailconfig.newslettersubscriptionform.subject"));
			if (count > 0) {
				System.out.println("News Letter Subscription details Saved To DB : ");
			}
			boolean flag = false;
			SendMailUtility sendMailUtility = new SendMailUtility();
			flag = sendMailUtility.sendMailForSubscriptionForm(firstName, lastName, email, companyName, nearestBranch, toEmail, comments, jobTitle);
			if(flag){
				renderContent = "Y";
			} else {
				renderContent = "N";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String getCustomerBalanceDetails() {

		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String customerId = (String) session.getAttribute("customerId");
		String includePeriod = (String) request.getParameter("period");
		UserManagement usersObj = new UserManagementImpl();
		UsersModel customerInfoInput = new UsersModel();
		try {
			customerInfoInput.setCustomerId(CommonUtility.validateString(customerId));
			customerInfoInput.setIncludePeriod(includePeriod);
			ArrayList<UserManagementModel> customerBalanceDetails = usersObj.getARGetInvoiceListV2(customerInfoInput);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("customerBalanceDetails", customerBalanceDetails);
			contentObject.put("customerId", customerId);
			contentObject.put("includePeriod", includePeriod);
			renderContent = LayoutGenerator.templateLoader("MyAccountBalanceDetailsPage", contentObject, null, null,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String GlobalCall() {
		request = ServletActionContext.getRequest();
		target = SUCCESS;

		try {
			String sectionName = CommonUtility.validateString(request.getParameter("sectionName"));
			String toEmail = CommonUtility.validateString(request.getParameter("toEmail"));

			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("toEmail", toEmail);

			if (CommonUtility.validateString(sectionName).length() > 0) {
				renderContent = LayoutGenerator.templateLoader(sectionName, contentObject, null, null, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String getFinalShipVias() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try {
			Gson gson = new Gson();
			Collection<String> itemLevelShipVia = new ArrayList<String>();
			Collection<String> customerLevelShipVia = new ArrayList<String>();
			ArrayList<CustomShipViaTable> ShipingTable = new ArrayList<CustomShipViaTable>();

			int buyingCompanyId = CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId"));
			String itemId = request.getParameter("itemID");
			String overSizeItem = request.getParameter("overSizeItemFlag");
			String storePickupOnly = request.getParameter("storePickupOnlyFlag");
			String jsonObject = "";
			CustomModel modelObj = new CustomModel();
			modelObj.setCustomFieldEntityType("BUYING_COMPANY");
			modelObj.setCustomFieldEntityId(buyingCompanyId);
			modelObj.setCustomFieldName("CUSTOMER_SHIPVIA_CODE");
			modelObj.setCustomFieldResultType("JSON");
			if (buyingCompanyId > 1) {
				CustomFieldDAO daoObj = new CustomFieldDAO();
				gson = new Gson();
				jsonObject = CommonUtility.validateString(daoObj.getCustomFieldTableDetailsInJson(modelObj));
				jsonObject = "{\"customTableObject\":" + jsonObject + "}";
				CustomTableModel output = gson.fromJson(jsonObject, CustomTableModel.class);
				List<CustomTable> customTableout = output.getCustomTableObject();
				for (CustomTable custShips : customTableout) {
					for (Map<String, String> eachShips : custShips.getTableDetails()) {
						customerLevelShipVia.add(eachShips.get("CUSTOMER_SHIP_CODE"));
					}
				}
			}
			if (CommonUtility.validateString(itemId).length() > 0) {
				if (itemId.contains(",")) {
					String[] itemIDs = itemId.split("\\,");
					for (String item : itemIDs) {
						modelObj = new CustomModel();
						modelObj.setCustomFieldEntityType("ITEM");
						modelObj.setCustomFieldEntityId(CommonUtility.validateNumber(item));
						modelObj.setCustomFieldName("ITEM_SHIPVIA_CODE");
						modelObj.setCustomFieldResultType("JSON");
						CustomFieldDAO daoObj = new CustomFieldDAO();
						jsonObject = CommonUtility.validateString(daoObj.getCustomFieldTableDetailsInJson(modelObj));
						jsonObject = "{\"customTableObject\":" + jsonObject + "}";
						gson = new Gson();
						CustomTableModel output = gson.fromJson(jsonObject, CustomTableModel.class);
						List<CustomTable> customTableout = output.getCustomTableObject();
						Collection<String> eachItem = new ArrayList<String>();
						for (CustomTable itemShips : customTableout) {
							for (Map<String, String> eachShips : itemShips.getTableDetails()) {
								eachItem.add(eachShips.get("ITEM_SHIP_CODE"));
							}
						}
						if (itemLevelShipVia.size() > 0) {
							itemLevelShipVia.retainAll(eachItem);
						} else {
							itemLevelShipVia.addAll(eachItem);
						}
					}
				} else {
					modelObj = new CustomModel();
					modelObj.setCustomFieldEntityType("ITEM");
					modelObj.setCustomFieldEntityId(CommonUtility.validateNumber(itemId));
					modelObj.setCustomFieldName("ITEM_SHIPVIA_CODE");
					modelObj.setCustomFieldResultType("JSON");
					CustomFieldDAO daoObj = new CustomFieldDAO();
					jsonObject = CommonUtility.validateString(daoObj.getCustomFieldTableDetailsInJson(modelObj));
					jsonObject = "{\"customTableObject\":" + jsonObject + "}";
					gson = new Gson();
					CustomTableModel output = gson.fromJson(jsonObject, CustomTableModel.class);
					List<CustomTable> customTableout = output.getCustomTableObject();
					Collection<String> eachItem = new ArrayList<String>();
					for (CustomTable itemShips : customTableout) {
						for (Map<String, String> eachShips : itemShips.getTableDetails()) {
							eachItem.add(eachShips.get("ITEM_SHIP_CODE"));
						}
					}
					itemLevelShipVia.addAll(eachItem);
				}
			}

			itemLevelShipVia.addAll(customerLevelShipVia);
			List<CustomTable> websiteShipvias = CIMM2VelocityTool.getInstance().getCusomTableData("WEBSITE",
					"SHIPVIA_COST_TABLE");
			LinkedHashMap<String, CustomShipViaTable> finalList = new LinkedHashMap<String, CustomShipViaTable>();
			for (CustomTable siteShips : websiteShipvias) {
				for (Map<String, String> eachShips : siteShips.getTableDetails()) {
					boolean add = false;
					if (CommonUtility.validateString(eachShips.get("SITE_NAME"))
							.equalsIgnoreCase(CommonDBQuery.webSiteName)) {
						if (itemLevelShipVia.contains(eachShips.get("SHIP_CODE"))) {
							if (CommonUtility.validateString(storePickupOnly).equalsIgnoreCase("Y")
									&& CommonUtility.validateString(eachShips.get("SHIP_VIA_CODE")).startsWith("PK")) {
								add = true;
							} else if (CommonUtility.validateString(overSizeItem).equalsIgnoreCase("Y")
									&& (CommonUtility.validateString(eachShips.get("SHIP_VIA_CODE")).startsWith("PK")
											|| CommonUtility.validateString(eachShips.get("SHIP_VIA_CODE"))
													.startsWith("FRT"))) {
								add = true;
							} else {
								if (!CommonUtility.validateString(overSizeItem).equalsIgnoreCase("Y")
										&& !CommonUtility.validateString(storePickupOnly).equalsIgnoreCase("Y")) {
									add = true;
								}
							}
						}
					}
					if (add) {
						CustomShipViaTable eachShipVia = new CustomShipViaTable();
						eachShipVia.setDisplaySeq(eachShips.get("DISPLAY_SEQ"));
						eachShipVia.setShipCode(eachShips.get("SHIP_CODE"));
						eachShipVia.setShippingCost(eachShips.get("SHIPPING_COST"));
						eachShipVia.setShipViaCode(eachShips.get("SHIP_VIA_CODE"));
						eachShipVia.setShipViaName(eachShips.get("SHIP_VIA_NAME"));
						ShipingTable.add(eachShipVia);
						if (finalList.size() > 0) {
							if (finalList.containsKey(eachShips.get("SHIP_VIA_CODE"))) {
								CustomShipViaTable oldShipVia = finalList.get(eachShips.get("SHIP_VIA_CODE"));
								if (CommonUtility.validateDoubleNumber(oldShipVia.getShippingCost()) < CommonUtility
										.validateDoubleNumber(eachShips.get("SHIPPING_COST"))) {
									finalList.put(eachShips.get("SHIP_VIA_CODE"), eachShipVia);
								}
							} else {
								finalList.put(eachShips.get("SHIP_VIA_CODE"), eachShipVia);
							}
						} else {
							finalList.put(eachShips.get("SHIP_VIA_CODE"), eachShipVia);
						}

					}
				}
			}
			renderContent = gson.toJson(finalList);

			// renderContent = FinalList.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String abandonedCartWL() {
		target = "abandonedCartWLCart";
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			WebLogin.loadWebUser();
			WebLogin.webUserSession(session.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public void call() {

		request = ServletActionContext.getRequest();
		try {
			String filePath = request.getParameter("filePath");
			String javaClass = request.getParameter("JC");
			String errorFilePath = request.getParameter("ERR");
			if (CommonUtility.validateString(filePath).length() > 0) {
				if (CommonUtility.validateString(javaClass).equalsIgnoreCase("P21")) {
					XLSXReaderWriterP21.readRecordsFromFile(filePath, errorFilePath);
					// http://122.166.57.30/callUnit.action?JC=P21&filePath=D:\\Projects\\PSS\\testuserlogins.xlsx&ERR=D:\\Projects\\PSS\\
				} else {
					XLSXReaderWriter.readRecordsFromFile(filePath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String arBalanceForCimm2BCentral() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();

		String dataUpToNumberOfDays = CommonUtility.validateString(request.getParameter("dataUpToNumberOfDays"));
		String reqType = (String) request.getParameter("reqType");
		String customerId = (String) session.getAttribute("customerId");
		String tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
		String tempdefaultBillToId = (String) session.getAttribute("defaultBillToId");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		int userId = CommonUtility.validateNumber(sessionUserId);
		try {
			if (userId > 1) {
				if (tempdefaultBillToId != null && tempdefaultBillToId.trim().length() > 0) {
					defaultBillToId = (CommonUtility.validateNumber(tempdefaultBillToId));
				}
				if (tempdefaultShipId != null && tempdefaultShipId.trim().length() > 0) {
					defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);
				}
				UserManagement userObj = new UserManagementImpl();
				HashMap<String, UsersModel> userAddress = userObj.getUserAddressFromBCAddressBook(defaultBillToId,
						defaultShipToId);
				billAddress = userAddress.get("Bill");
				shipAddress = userAddress.get("Ship");
				userAccountDetail = new UsersModel();
				UsersModel customerInfoInput = new UsersModel();
				try {
					customerInfoInput.setCustomerId(CommonUtility.validateString(customerId));
					customerInfoInput.setRequestType(reqType);
					if (CommonUtility.validateNumber(dataUpToNumberOfDays) == 30) {
						customerInfoInput.setDataUpTo30Days(true);
					}
					if (CommonUtility.validateNumber(dataUpToNumberOfDays) == 60) {
						customerInfoInput.setDataUpTo60Days(true);
					}
					if (CommonUtility.validateNumber(dataUpToNumberOfDays) == 90) {
						customerInfoInput.setDataUpTo90Days(true);
					}
					if (CommonUtility.validateNumber(dataUpToNumberOfDays) == 120) {
						customerInfoInput.setDataUpTo120Days(true);
					}

					Cimm2BCentralARBalanceSummary arBalanceSummary = userObj
							.getARBalanceForCimm2BCentral(customerInfoInput);
					LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
					contentObject.put("arBalanceSummary", arBalanceSummary);
					contentObject.put("billAddress", billAddress);
					contentObject.put("shipAddress", shipAddress);
					contentObject.put("shipAddressList", shipAddressList);
					contentObject.put("dataUpToNumberOfDays", dataUpToNumberOfDays);
					renderContent = LayoutGenerator.templateLoader("ARBalancePage", contentObject, null, null, null);
					target = SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "SESSIONEXPIRED";
		}
		return target;
	}

	public String cylinderBalanceForCimm2BCentral() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String reqType = (String) request.getParameter("reqType");
		String customerId = (String) session.getAttribute("customerId");
		String tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
		String tempdefaultBillToId = (String) session.getAttribute("defaultBillToId");
		if (tempdefaultBillToId != null && tempdefaultBillToId.trim().length() > 0) {
			defaultBillToId = (CommonUtility.validateNumber(tempdefaultBillToId));
		}
		if (tempdefaultShipId != null && tempdefaultShipId.trim().length() > 0) {
			defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);
		}
		UserManagement userObj = new UserManagementImpl();
		HashMap<String, UsersModel> userAddress = userObj.getUserAddressFromBCAddressBook(defaultBillToId,
				defaultShipToId);
		billAddress = userAddress.get("Bill");
		shipAddress = userAddress.get("Ship");
		userAccountDetail = new UsersModel();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		contentObject.put("billAddress", billAddress);
		contentObject.put("shipAddress", shipAddress);
		UsersModel customerInfoInput = new UsersModel();
		try {
			customerInfoInput.setCustomerId(CommonUtility.validateString(customerId));
			customerInfoInput.setRequestType(reqType);
			Cimm2BCentralCylinderBalanceSummary cylinderBalanceSummary = userObj
					.getCylinderBalanceForCimm2BCentral(customerInfoInput);
			contentObject.put("cylinderBalanceSummary", cylinderBalanceSummary);
			System.out.println("cylinderBalanceSummary -- " + new Gson().toJson(cylinderBalanceSummary));
			renderContent = LayoutGenerator.templateLoader("CylinderBalance", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String updateCustomFields() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String fieldName = request.getParameter("fieldName");
			String fieldValue = request.getParameter("fieldValue");
			if (userId > 1) {
				renderContent = "" + UsersDAO.insertCustomField(fieldValue, fieldName, userId, 0, "USER");
				LinkedHashMap<String, String> userCustomFieldValue = new LinkedHashMap<String, String>();
				userCustomFieldValue = UsersDAO.getAllUserCustomFieldValue(userId);
				if (userCustomFieldValue != null) {
					session.setAttribute("userCustomFieldValue", userCustomFieldValue);
				}
			} else {
				renderContent = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String AccountInquiry() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		buyingCompanyId = (String) session.getAttribute("buyingCompanyId");

		try {

			if (userId > 1) {
				String reqType = CommonUtility.validateString(request.getParameter("reqType"));
				String searchBy = CommonUtility.validateString(request.getParameter("searchBy"));
				String additionalInfo = CommonUtility.validateString(request.getParameter("additionalInfo"));
				int statementDate = 0;

				if (CommonUtility.validateString(reqType).equalsIgnoreCase("MStat") && statementMonth > 0
						&& statementYear > 0) {
					Calendar c = new GregorianCalendar(statementYear, statementMonth - 1, 1);
					Date d = new Date();
					if (statementMonth - 1 < d.getMonth()) {
						statementDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
						if (CommonUtility.validateNumber(CommonUtility.validateString(
								CommonDBQuery.getSystemParamtersList().get("ACCOUNT_INQUIRY_FISCAL_DATE"))) > 0) {
							statementDate = CommonUtility.validateNumber(CommonUtility.validateString(
									CommonDBQuery.getSystemParamtersList().get("ACCOUNT_INQUIRY_FISCAL_DATE")));
						}
						asOfDate = statementMonth + "/" + statementDate + "/" + statementYear;
					}
				} else if (statementMonth > 0 && statementYear > 0) {
					Calendar c = new GregorianCalendar(statementYear, (statementMonth - 1), 1);
					Date d = new Date();
					if (((statementMonth - 1) < d.getMonth()) || (statementYear < (d.getYear() + 1900))) {
						statementDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
						if (CommonUtility.validateNumber(CommonUtility.validateString(
								CommonDBQuery.getSystemParamtersList().get("ACCOUNT_INQUIRY_FISCAL_DATE"))) > 0) {
							statementDate = CommonUtility.validateNumber(CommonUtility.validateString(
									CommonDBQuery.getSystemParamtersList().get("ACCOUNT_INQUIRY_FISCAL_DATE")));
						}
						asOfDate = statementMonth + "/" + statementDate + "/" + statementYear;
					}
				}

				/*
				 * if(defaultShipToId>0){
				 * session.setAttribute("defaultShipToId",Integer.toString(defaultShipToId)); }
				 */

				String tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
				String tempdefaultBillToId = (String) session.getAttribute("defaultBillToId");
				if (tempdefaultBillToId != null && tempdefaultBillToId.trim().length() > 0)
					defaultBillToId = (CommonUtility.validateNumber(tempdefaultBillToId));
				if (tempdefaultShipId != null && tempdefaultShipId.trim().length() > 0)
					defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);

				if (defaultBillToId == 0 || defaultShipToId == 0) {
					HashMap<String, Integer> userAddressId = UsersDAO
							.getDefaultAddressIdForBCAddressBook((String) session.getAttribute(Global.USERNAME_KEY));
					// UsersDAO.getDefaultAddressId(userId);
					defaultBillToId = userAddressId.get("Bill");
					defaultShipToId = userAddressId.get("Ship");
				}
				// HashMap<String, UsersModel> userAddress =
				// usersObj.getUserAddressFromBCAddressBook(defaultBillToId, defaultShipToId);
				UserManagement userObj = new UserManagementImpl();
				HashMap<String, UsersModel> userAddress = userObj.getUserAddressFromBCAddressBook(defaultBillToId,
						defaultShipToId);
				billAddress = userAddress.get("Bill");
				shipAddress = userAddress.get("Ship");

				HashMap<String, ArrayList<UsersModel>> userAddressList = userObj
						.getAddressListFromBCAddressBook(CommonUtility.validateNumber(buyingCompanyId), userId);
				billAddressList = userAddressList.get("Bill");
				shipAddressList = userAddressList.get("Ship");

				if (CommonUtility.validateString(entityId).length() < 1) {
					entityId = userAddress.get("Bill").getEntityId();
				}

				HashMap<String, UsersModel> shipToList = new HashMap<String, UsersModel>();
				for (UsersModel shipTo : shipAddressList) {
					shipToList.put(String.valueOf(shipTo.getEntityId()), shipTo);
				}
				String entityName = shipToList.get(entityId).getCustomerName();

				userAccountDetail = new UsersModel();
				LinkedHashMap<String, String> accountInquiryInput = new LinkedHashMap<String, String>();
				accountInquiryInput.put("userToken", (String) session.getAttribute("userToken"));
				accountInquiryInput.put("userName", (String) session.getAttribute(Global.USERNAME_KEY));
				accountInquiryInput.put("entityId", entityId);
				if(serachInvoiceByDate !=null  && serachInvoiceByDate.equalsIgnoreCase("Y")){
					accountInquiryInput.put("asOfDate", startDate);
				}else{
				accountInquiryInput.put("asOfDate", asOfDate);
				}
				accountInquiryInput.put("searchString", searchBy);
				accountInquiryInput.put("additionalInfo", additionalInfo);

				UserManagement userManagement = new UserManagementImpl();
				userAccountDetail = userManagement.getAccountDetail(accountInquiryInput);

				System.out
						.println(userAccountDetail.getArCreditAvail() + "****" + userAccountDetail.getArCreditLimit());

				/*
				 * if(CommonUtility.validateString(reqType).equalsIgnoreCase("MStat") &&
				 * statementMonth==0 && statementYear==0){ String tempdate = "";
				 * if(CommonUtility.validateString(userAccountDetail.getLastSaleDate()).length()
				 * >0){ tempdate =
				 * CommonUtility.validateString(userAccountDetail.getLastSaleDate()); }else if
				 * (CommonUtility.validateString(userAccountDetail.getLastPaymentDate()).length(
				 * )>0) { tempdate =
				 * CommonUtility.validateString(userAccountDetail.getLastPaymentDate()); } Date
				 * actualDate = null; SimpleDateFormat yy = new SimpleDateFormat( "MM/dd/yy" );
				 * SimpleDateFormat yyyy = new SimpleDateFormat( "MM/dd/yyyy" );
				 * if(CommonUtility.validateString(tempdate).length()>0){ actualDate =
				 * yy.parse(tempdate);
				 * userAccountDetail.setAsOfDate(CommonUtility.validateString(yyyy.format(
				 * actualDate))); } }
				 */

				if (CommonUtility.validateString(userAccountDetail.getAsOfDate()).length() > 0) {
					asOfDate = userAccountDetail.getAsOfDate();
				} else {
					userAccountDetail.setAsOfDate(asOfDate);
				}

				SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
				if (asOfDate != null) {
					Date d = sd.parse(asOfDate);
					Calendar c = new GregorianCalendar(statementYear, statementMonth - 1, 1);
					statementMonth = d.getMonth() + 1;
					statementYear = d.getYear() + 1900;
				}
				// previous balance and current balance
				userAccountDetail.setPreviousBalance(
						(userAccountDetail.getArCreditLimit() - userAccountDetail.getArCreditAvail())
								+ userAccountDetail.getaRDeposits());
				userAccountDetail
						.setCurrentBalance(userAccountDetail.getArCreditLimit() - userAccountDetail.getArCreditAvail());
				if (statementMonthMap == null) {
					statementMonthMap = getMontsMap();
				}

				contentObject.put("statementMonth", statementMonth);
				contentObject.put("statementYear", statementYear);
				contentObject.put("searchBy", searchBy);
				contentObject.put("userAccountDetail", userAccountDetail);
				contentObject.put("billAddressList", billAddressList);
				contentObject.put("shipAddressList", shipAddressList);
				contentObject.put("entityId", entityId);
				contentObject.put("entityName", entityName);
				contentObject.put("shipToList", shipToList);
				contentObject.put("statementMonthMap", statementMonthMap);

				if (CommonUtility.validateString(reqType).equalsIgnoreCase("MStat")) {
					renderContent = LayoutGenerator.templateLoader("AccountInquiryMonthlyPage", contentObject, null,
							null, null);
				} else {
					renderContent = LayoutGenerator.templateLoader("AccountInquiryPage", contentObject, null, null,
							null);
				}

				target = SUCCESS;

			} else {
				target = "SESSIONEXPIRED";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	private Map<Integer, String> getMontsMap() {
		Map<Integer, String> monthMap = new HashMap<Integer, String>();
		monthMap.put(1, "January");
		monthMap.put(2, "February");
		monthMap.put(3, "March");
		monthMap.put(4, "April");
		monthMap.put(5, "May");
		monthMap.put(6, "June");
		monthMap.put(7, "July");
		monthMap.put(8, "August");
		monthMap.put(9, "September");
		monthMap.put(10, "October");
		monthMap.put(11, "November");
		monthMap.put(12, "December");

		return monthMap;
	}

	public String getSessionShipMethod() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			ArrayList<ShipVia> shipViaListTemp = (ArrayList<ShipVia>) session.getAttribute("shipViaList");
			if (shipViaListTemp != null) {
				JSONArray jsonAraay = new JSONArray(shipViaListTemp);
				System.out.println(jsonAraay.toString());
				renderContent = jsonAraay.toString();
			} else {
				renderContent = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String OpenPayables() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String reqType = (String) request.getParameter("reqType");
		String customerId = (String) session.getAttribute("customerId");
		String tempdefaultShipId = (String) session.getAttribute("defaultShipToId");
		String tempdefaultBillToId = (String) session.getAttribute("defaultBillToId");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		double fullPay = 0.00;
		String isFullPay = "N";
		if (userId > 1) {
			if (CommonUtility.validateString(request.getParameter("full")).equalsIgnoreCase("Y")) {
				isFullPay = "Y";
				fullPay = CommonUtility.validateDoubleNumber(request.getParameter("amt"));
			}
			if(CommonUtility.customServiceUtility() != null) {
				defaultShipToId = CommonUtility.validateNumber(CommonUtility.customServiceUtility().getEntityShipToId(session, CommonUtility.validateParseIntegerToString(defaultShipToId), (String)session.getAttribute("shipEntityId")));
			}
			if (tempdefaultBillToId != null && tempdefaultBillToId.trim().length() > 0)
				defaultBillToId = (CommonUtility.validateNumber(tempdefaultBillToId));
			if (tempdefaultShipId != null && tempdefaultShipId.trim().length() > 0)
				defaultShipToId = CommonUtility.validateNumber(tempdefaultShipId);

			UserManagement userObj = new UserManagementImpl();
			HashMap<String, UsersModel> userAddress = userObj.getUserAddressFromBCAddressBook(defaultBillToId,
					defaultShipToId);

			billAddress = userAddress.get("Bill");
			shipAddress = userAddress.get("Ship");
			userAccountDetail = new UsersModel();

			UsersModel customerInfoInput = new UsersModel();
			try {

				customerInfoInput.setCustomerId(CommonUtility.validateString(customerId));
				customerInfoInput.setRequestType(reqType);
				ArrayList<UserManagementModel> arBalanceDetails = userObj
						.getARBalanceDetailsForCimm2BCentral(customerInfoInput);

				session.removeAttribute("OutStandingOrderPaymentBillAddress");
				session.removeAttribute("OutStandingOrderPaymentShipAddress");
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("arBalanceDetail", arBalanceDetails);
				contentObject.put("billAddress", billAddress);
				contentObject.put("shipAddress", shipAddress);
				contentObject.put("isFullPay", isFullPay);
				contentObject.put("fullPay", fullPay);
				session.setAttribute("OutStandingOrderPaymentBillAddress", billAddress);
				session.setAttribute("OutStandingOrderPaymentShipAddress", shipAddress);
				renderContent = LayoutGenerator.templateLoader("OpenPayablesPage", contentObject, null, null, null);
				target = SUCCESS;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			target = "SESSIONEXPIRED";

		}
		return target;
	}

	public String getJobAccounts() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		ArrayList<String> subAccounts = new ArrayList<String>();
		ArrayList<UsersModel> customerAccounts = new ArrayList<UsersModel>();
		ArrayList<UsersModel> JobAddressList = new ArrayList<UsersModel>();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		int count = 0;
		int countValue = 0;
		int jobCount = 0;
		String customerNumber = (String) session.getAttribute("customerId");
		int buyingCompanyId = CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId"));
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		try {

			if (sessionUserId.trim().length() > 0) {
				JobAddressList = UsersDAO.getListOfJobAccount(CommonUtility.validateNumber(sessionUserId));
			}
			if (JobAddressList.size() == 0) {
				String GET_CUSTOMER_URL = CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API")) + "?"
						+ Cimm2BCentralRequestParams.customerERPId + "=" + customerNumber;
				Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance()
						.getDataObject(GET_CUSTOMER_URL, HttpMethod.GET, null, Cimm2BCentralCustomer.class);
				Cimm2BCentralCustomer customerDetails = null;

				if (customerDetailsResponseEntity != null && customerDetailsResponseEntity.getData() != null
						&& customerDetailsResponseEntity.getStatus().getCode() == 200) {

					customerDetails = (Cimm2BCentralCustomer) customerDetailsResponseEntity.getData();
					Cimm2BCentralCustomerType customerType = customerDetails.getCustomerType();
					if (customerDetails != null && customerType != null && customerType.getType() != null
							&& customerType.getType().equalsIgnoreCase("M")) {
						subAccounts = customerDetails.getCustomerType().getAccounts();
						for (String Accounts : subAccounts) {
							String[] accountArray = Accounts.split(":");
							if (CommonUtility.validateString(accountArray[2]).equalsIgnoreCase("JOB")) {
								UsersModel usersModel = new UsersModel();
								usersModel.setCustomerId(accountArray[0]);
								usersModel.setCustomerName(accountArray[1]);
								usersModel.setRequestType(accountArray[2]);
								customerAccounts.add(usersModel);
							}

							// insert into buying company
							if (CommonUtility.validateString(accountArray[2]).equalsIgnoreCase("JOB")) {
								if (accountArray[1] != null || accountArray[1].trim().length() > 0) {
									count = UsersDAO.getBcAddressBookJob(accountArray[0], buyingCompanyId,
											accountArray[2]);
								}
								if (count == 0) {
									// insert into buying company
									countValue = UsersDAO.addBuyingCompanyJobEntityId(accountArray[0], accountArray[1],
											accountArray[2], buyingCompanyId);
									// insert into bc address book
									int countValue1 = UsersDAO.addBcAddressBookJobEntityId(accountArray[0],
											accountArray[1], accountArray[2], buyingCompanyId);
								}
								if (countValue > 0) {
									System.out.println("Inserted Successfully");
								}

								// defaultShiptoId =
								// CommonUtility.validateParseIntegerToString(UsersDAO.getBcAddressBookId(CommonUtility.validateNumber((String)
								// session.getAttribute("buyingCompanyId")), entityId));
							}
						}

						if (customerAccounts.size() > 0) {
							session.setAttribute("containSubAcc", "Y");
						}

						contentObject.put("subAccounts", customerAccounts);
						renderContent = LayoutGenerator.templateLoader("accountList", contentObject, null, null, null);
						target = SUCCESS;
					} else {
						target = "addressSync";
					}

				}
			} else {
				// JobAddressList=UsersDAO.getListOfJobAccount(CommonUtility.validateNumber(sessionUserId));
				if (JobAddressList.size() > 0) {
					session.setAttribute("containSubAcc", "Y");
				}
				contentObject.put("subAccounts", JobAddressList);
				renderContent = LayoutGenerator.templateLoader("accountList", contentObject, null, null, null);
				target = SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public String selectjobAccount() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String parentCustomerNumber = (String) session.getAttribute("customerId");
			int parentId = UserRegisterUtility.checkEntityId(parentCustomerNumber);
			String selectectedCustomerNumber = request.getParameter("customerNumber");

			UsersModel catalog = new UsersModel();
			catalog.setCustomerId(selectectedCustomerNumber);

			if (catalog != null && catalog.getCatalogName() != null) {
				if (!CommonUtility.validateParseIntegerToString(catalog.getSubsetId())
						.equalsIgnoreCase((String) session.getAttribute("userSubsetId"))) {
					UsersDAO.updateSubsetIdFromERP(
							CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId")),
							catalog.getSubsetId());
				}
				session.setAttribute("userSubsetId", CommonUtility.validateParseIntegerToString(catalog.getSubsetId()));
			}
			if (!CommonUtility.validateString(catalog.getWareHouseCodeStr()).isEmpty()) {
				session.setAttribute("wareHouseCode", catalog.getWareHouseCodeStr());
			}

			/*
			 * int childbuyingCompanyId =
			 * UserRegisterUtility.checkEntityId(selectectedCustomerNumber);
			 * if(childbuyingCompanyId == 0){ childbuyingCompanyId =
			 * CustomersUtility.inserChildAccounts(selectectedCustomerNumber,
			 * parentCustomerNumber, parentId, (String)
			 * session.getAttribute("userEmailAddress")); }
			 * session.setAttribute("buyingCompanyId",
			 * CommonUtility.validateParseIntegerToString(childbuyingCompanyId));
			 */
			session.setAttribute("customerId", selectectedCustomerNumber);
			renderContent = "";
		} catch (Exception e) {
			e.printStackTrace();
			return "SESSIONEXPIRED";
		}
		return SUCCESS;
	}

	public String clearProfileImage() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String userIdString = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(userIdString);
			String savedFileName = "default-profile.png";
			if (userId > 1) {
				UsersDAO.updateUserProfileImage(savedFileName, userId);
				session.setAttribute("userProfileImage", savedFileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "SESSIONEXPIRED";
		}
		return SUCCESS;
	}

	public String statementYears() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
		    String AjaxRequest = request.getParameter("AjaxRequest");
			String customerErpId = (String) session.getAttribute("customerId");
			UsersModel user = new UsersModel();
			ArrayList<SalesModel> maxList=new ArrayList<SalesModel>();
			SalesModel salesInputParameter = new SalesModel();
			user.setCustomerId(customerErpId);
			ArrayList<UserManagementModel> arBalanceDetails = UserManagementAction
					.getARBalanceDetailForCimm2BCentral(user);
			List<String> arYears = UserManagementAction.arYears(customerErpId);
			List<String> arMonths = null;
			String statementYear = null;
			if (arYears != null && arYears.size() > 0) {
				statementYear = arYears.get(arYears.size() - 1);
				arMonths = UserManagementAction.arMonths(customerErpId, statementYear);
			}
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			if (arBalanceDetails != null && arBalanceDetails.size() > 0) {
				session.setAttribute("creditLimitbalance",
						arBalanceDetails.get(0).getBalance() != null ? arBalanceDetails.get(0).getBalance().toString()
								: "0");
				session.setAttribute("creditLimitdueBalance",
						arBalanceDetails.get(0).getAmountDue() != null
								? arBalanceDetails.get(0).getAmountDue().toString()
								: "0");
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_MAXRECALL_LIST")).equalsIgnoreCase("Y"))
			{
				SalesOrderManagement salesObj = new SalesOrderManagementImpl();
				salesInputParameter.setSearchKeyword(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SEARCH_KEYWORD_MAX_RECALL1")));
				salesInputParameter.setUserValue(customerErpId);
				salesInputParameter.setDocumentId(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DOCUMENT_ID_MAX_RECALL2")));
				salesInputParameter.setOrderSuffix(CommonUtility.validateNumber(" "));
				salesInputParameter.setSearchKeywordForOrderSuffix("");
				salesInputParameter.setExcludePDFDocument(true); 
				SalesModel salesOrderOutput = salesObj.getmaxRecallInformation(salesInputParameter);
				if(salesOrderOutput!=null){
					maxList = salesOrderOutput.getMaxRecallList();
				}
			}
			contentObject.put("arBalanceDetails", arBalanceDetails);
			contentObject.put("statementYear", statementYear);
			contentObject.put("arYears", arYears);
			contentObject.put("arMonths", arMonths);
			contentObject.put("includeYearFilter", "Y");
			contentObject.put("AjaxRequest", AjaxRequest);
			contentObject.put("maxRecallList", maxList);
			renderContent = LayoutGenerator.templateLoader("ArStatementsPage", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String statementMonths() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String customerErpId = (String) session.getAttribute("customerId");
			String statementYear = request.getParameter("year");
			List<String> arMonths = null;
			arMonths = UserManagementAction.arMonths(customerErpId, statementYear);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("arMonths", arMonths);
			contentObject.put("statementYear", statementYear);
			contentObject.put("requestType", "statementMonths");
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String statementFilePath() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String customerErpId = (String) session.getAttribute("customerId");
			String statementYear = request.getParameter("year");
			String month = request.getParameter("month");
			month = CommonUtility.Months.valueOf(month.toUpperCase()).getMonth();
			renderContent = UserManagementAction.arSummaryFilePath(customerErpId, month, statementYear);
			if(renderContent!=null) {
				Base64 b = new Base64();
				
				byte[] imageBytes = b.decode(renderContent);
				
				FileOutputStream file= new FileOutputStream(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH"))+"\\"+customerErpId+statementYear+month+".pdf");
				if (CommonUtility.customServiceUtility() != null) {
					file=CommonUtility.customServiceUtility().setfilePath(file,customerErpId,statementYear,CommonUtility.validateString(request.getParameter("month")));
				}
				file.write(imageBytes);
				file.close();      				
				}
				
		} catch (Exception e) {
			logger.error(e);
		}
		return SUCCESS;
	}
	
	public String notificationCount() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		UserManagement userManagement = new UserManagementImpl();
		target = SUCCESS;
		try {
			renderContent = "";
			if (userId > 1) {
				// Get count of open orders
				int openOrdersCount = 0;
				String orderListjson = "";
				// Get count of open orders
				if (CommonUtility
						.validateString(CommonDBQuery.getSystemParamtersList().get("GET_OPEN_ORDERS_COUNT_HEAD"))
						.equalsIgnoreCase("Y")) {
					SalesOrderManagement salesObj = new SalesOrderManagementImpl();
					SalesModel salesInputParameter = new SalesModel();
					salesInputParameter.setCustomerNumber(
							session.getAttribute("customerId") != null ? (String) session.getAttribute("customerId")
									: "0");
					ArrayList<SalesModel> orderList = salesObj.OpenOrders(salesInputParameter);
					for (SalesModel each : orderList) {
						if (each.getOrderStatus() != null && (!each.getOrderStatus().equalsIgnoreCase("B"))) {
							openOrdersCount += 1;
						}

					}
					if (orderList.size() > 0) {
						List<SalesModel> list = orderList.stream().limit(5).collect(Collectors.toList());
						orderListjson = new Gson().toJson(list);
					}
				}
				buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("session", session);
				contentObject = ProductsDAO.getSavedGroupNameByUserIdDao(userId, contentObject,	CommonUtility.validateNumber(buyingCompanyId));
				ArrayList<ProductsModel> groupListData = (ArrayList<ProductsModel>) contentObject.get("groupListData");
				Collections.sort(groupListData, ProductsModel.groupNameAscendingComparator);
				int productGroupsCount = groupListData != null ? groupListData.size() : 0;
				String groupListjson = null;
				if (groupListData.size() > 0) {
					List<ProductsModel> grouplist = groupListData.stream().limit(5).collect(Collectors.toList());
					groupListjson = new Gson().toJson(grouplist);
				}
				ArrayList<ProductsModel> approveCartData = (ArrayList<ProductsModel>) contentObject
						.get("approveCartData");
				int approveCartsCount = approveCartData != null ? approveCartData.size() : 0;
				String approveCartjson = null;
				if (approveCartData.size() > 0) {
					List<ProductsModel> approvelist = approveCartData.stream().limit(5).collect(Collectors.toList());
					approveCartjson = new Gson().toJson(approvelist);
				}
				ArrayList<ProductsModel> savedCartListData = (ArrayList<ProductsModel>) contentObject
						.get("savedCartData");
				int savedCartCount = savedCartListData != null ? savedCartListData.size() : 0;
				String savedListjson = null;
				if (savedCartListData.size() > 0) {
					List<ProductsModel> savedlist = savedCartListData.stream().limit(5).collect(Collectors.toList());
					savedListjson = new Gson().toJson(savedlist);
				}
				int historycount = 0;
				List<Cimm2BCentralARBalanceSummary> arBalanceSummary = new ArrayList<Cimm2BCentralARBalanceSummary>();
				LinkedHashMap<String, String> accountInquiryInput = new LinkedHashMap<String, String>();
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ACCOUNT_RECEIVABLE_BALANCE_SUMMARY")).length() > 0) {
				 accountInquiryInput.put("userToken",
				  (String)session.getAttribute("userToken"));
				  accountInquiryInput.put("userName", (String)
				  session.getAttribute(Global.USERNAME_KEY));
				  accountInquiryInput.put("entityId",
				  (String)session.getAttribute("userToken")); UsersModel orderHistoryList =
				  userManagement.getAccountDetail(accountInquiryInput); arBalanceSummary =
				  orderHistoryList.getArBalanceSummaryList(); if(arBalanceSummary!=null){
				  for(Cimm2BCentralARBalanceSummary cimm2bcARBalanceSummary :arBalanceSummary
				  ){ if(!cimm2bcARBalanceSummary.getAge().equalsIgnoreCase("future")) {
				  historycount = historycount + 1; } } }
				}


				  // open orders count | products groups count | approve carts count
				renderContent = openOrdersCount + "|" + productGroupsCount + "|" + approveCartsCount + "|"
						+ historycount + "|" + orderListjson + "|" + groupListjson + "|" + approveCartjson + "|"
						+ savedCartCount;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}
	
	
	
	public String photoInquiry(){
	
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			try{
				
				renderContent = LayoutGenerator.templateLoader("PhotoInquiryPage", null, null, null, null);
			}
			catch(Exception e){
				e.printStackTrace();
				}
			return SUCCESS;
		}

	


	public String notifySalesRep() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			LinkedHashMap<String, Object> contentObject = ProductsDAO.getShoppingCartDao(session,
					new LinkedHashMap<String, Object>());
			if (CommonUtility.customServiceUtility() != null) {
				CommonUtility.customServiceUtility().notifyNonCatalogItemsToSalesRep(contentObject, session);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public String getCustomers() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			List<JsonObject> customers = UsersDAO.customers(session);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("customers", customers);
			contentObject.put("requestType", "CustomersForSalesUser");
			
			if(CommonUtility.validateString(request.getParameter("requestType")).equalsIgnoreCase("JSON")){				
				renderContent = customers.toString();
			}else{
				renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject, null, null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String getUsersByCustomer() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			List<JsonObject> users = UsersDAO.users(request);
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("users", users);
			contentObject.put("requestType", "UsersByCustomer");
			session.removeAttribute("multipleAccountsAvailable");
			session.removeAttribute("selectedUserId");
			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String setSelectedUserDetails() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String userName = request.getParameter("userName");
			HashMap<String, String> userDetails = UsersDAO.getUserPasswordAndUserId(userName, "Y");
			if (userDetails != null && userDetails.size() > 0) {
				if(CommonDBQuery.getSystemParamtersList() !=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CART_CLEAR")).equalsIgnoreCase("Y")){
					 ProductsDAO.clearCart(CommonUtility.validateNumber(userDetails.get("userId")));
				}
				UsersDAO.updateUserPermission(userDetails);
				LoginUserSessionObject.setUserSessionObject(session,userDetails);
				session.setAttribute("entityId", userDetails.get("contactId"));
				renderContent = "SUCCESS";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String makeAsDefaultShipTo() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String locale = session.getAttribute("localeCode").toString().toUpperCase();
			if (CommonUtility.customServiceUtility() != null
					&& CommonUtility.customServiceUtility().makeAsDefaultShipTo(request)) {
				renderContent = LayoutLoader.getMessageProperties().get(locale)
						.getProperty("user.shipto.saveasdefault.success");
			} else {
				renderContent = LayoutLoader.getMessageProperties().get(locale)
						.getProperty("user.shipto.saveasdefault.failure");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String UpdateMarketingMaterialSubscription() {
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String customFieldName = (String) request.getParameter("customFieldName");
		String marketingMaterialSub = (String) request.getParameter("marketingMaterialSub");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		if(userId>1) {	   
			try {
				UsersModel userDetail = new UsersModel();
				userDetail.setNewsLetterSub(marketingMaterialSub);
				userDetail.setUserId(userId);
				userDetail.setCustomFieldName(customFieldName);
				int count = UsersDAO.updateNewsletterSubscription(userDetail);
				if(count>0) {
					result = "1|Updated Successfully";
					session.setAttribute("marketingMaterialSub", marketingMaterialSub);
				}
				else {
					result = "0|Update failed";
				}
				renderContent = result;																																																																												  
			}
			catch(Exception e){
				e.printStackTrace();
				return "SESSIONEXPIRED"; 
			}
			return SUCCESS;   
		}
		else {
			return "SESSIONEXPIRED";	   
		}	 
	}
	
	public String salesRepPortal() {
		result = "SESSIONEXPIRED";	
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		try {
			if(userId>1) {	
				result = "success";   
			}
		}catch(Exception e) {
			e.printStackTrace();
			result = "SESSIONEXPIRED";
		}
		renderContent = LayoutGenerator.templateLoader("salesAdminPanelPage", contentObject, null, null, null);
		return result;
	}
	
	public ArrayList<EventModel> getEventsCustomFieldList() {
		return eventsCustomFieldList;
	}

	public void setEventsCustomFieldList(ArrayList<EventModel> eventsCustomFieldList) {
		this.eventsCustomFieldList = eventsCustomFieldList;
	}

	public EventModel getEventCustomFieldList() {
		return eventCustomFieldList;
	}

	public void setEventCustomFieldList(EventModel eventCustomFieldList) {
		this.eventCustomFieldList = eventCustomFieldList;
	}

	public String getShippingOrgType() {
		return shippingOrgType;
	}

	public void setShippingOrgType(String shippingOrgType) {
		this.shippingOrgType = shippingOrgType;
	}

	
	public String AssetsList() {
		request = ServletActionContext.getRequest();
		String catagoryFilter = request.getParameter("catagoryFilter");
		HttpSession session = request.getSession();
		List<JsonObject> assets = UsersDAO.getAssetsByWareHouse(session , catagoryFilter);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		contentObject.put("assets", assets);
		contentObject.put("selectedCatagoryFilter", catagoryFilter);
		if(CommonUtility.validateString(request.getParameter("requestType")).equalsIgnoreCase("JSON")){				
			renderContent = assets.toString();
		}else{
			renderContent = LayoutGenerator.templateLoader("AssetsListPage", contentObject, null, null, null);
		}
		return SUCCESS;
	}
	
public String getShipToIdShipAddresses(){
		
		request =ServletActionContext.getRequest();
		try {
		ArrayList<AddressModel> shipToIdShipAddresses = UsersDAO.getShipToIdShipAddresses(CommonUtility.validateNumber((shipToId)));
			
			Gson gson = new Gson();
			String shipToIdShipAddressesJsonResponse = gson.toJson(shipToIdShipAddresses);
			
			renderContent = shipToIdShipAddressesJsonResponse;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

public String getbranchDetails(){
	
	request =ServletActionContext.getRequest();
	LinkedHashMap<String, ProductsModel> branchDetails = CommonDBQuery.branchDetailData;
		Gson gson = new Gson();
		String branchDetailsJson = gson.toJson(branchDetails);
		renderContent = branchDetailsJson;
	return SUCCESS;
}
public String updateUserContactAddress() {
	request =ServletActionContext.getRequest();
	HttpSession session = request.getSession();
	UserManagement userObj = new UserManagementImpl();
	String result="";
	if (userId == 0) {
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		userId = CommonUtility.validateNumber(sessionUserId);
	}
	if (userId > 1 && !CommonUtility.validateString((String) session.getAttribute("isPunchoutUser")).equalsIgnoreCase("Y"))
	{
		long startTimer = CommonUtility.startTimeDispaly();
		try
		{
			UsersModel userDetails=userObj.contactInquiry(CommonUtility.validateString((String)session.getAttribute("loginUserERPId")));
			userDetails.setSession(session);
			renderContent=UsersDAO.updateEclipseUserContact(userDetails);
		}catch (Exception e) {
			e.printStackTrace();
		}

		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return "ResultLoader";
	}else {
	return "SESSIONEXPIRED";
	}

}

	public String customerPortalLogin() {

		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			userId = CommonUtility.validateNumber(sessionUserId);
			String iframeCustomPortalToken = "";
			if (userId > 1) {
				if(session.getAttribute("getTokenFlag")!="Y") {
				UsersModel tokenInputParameters = new UsersModel();
				UsersModel outputRespose = new UsersModel();
				SecureData userPassword=new SecureData();
				String password= userPassword.validatePassword(session.getAttribute("securedPassword").toString());
				String useraName = CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY));
				tokenInputParameters.setUserName(useraName);
				tokenInputParameters.setPassword(password);
				tokenInputParameters.setErpOverrideFlag(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode")).getProperty("customerPortal.overrideFlad")));
				UserManagement usersObj = new UserManagementImpl();
				outputRespose = usersObj.getIframeAccessToken(tokenInputParameters);
				iframeCustomPortalToken = outputRespose.getUserToken();
				if (iframeCustomPortalToken != null) {
					session.setAttribute("iframeCustomToken", iframeCustomPortalToken);
				}
				session.setAttribute("getTokenFlag", "Y");
				}

				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("portalUrl", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_PORTAL_URL")));
				renderContent = LayoutGenerator.templateLoader("CustomerPortalPage", contentObject, null, null, null);
				target = SUCCESS;
			} else {
				target = "SESSIONEXPIRED";
			}

		} catch (Exception e) {
			e.printStackTrace();
			target = "SESSIONEXPIRED";
		}
		return target;
	}
	
	public String initiateChatBot() {
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if (userId == 0) {
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			userId = CommonUtility.validateNumber(sessionUserId);
		}
		if (userId != 0 && !CommonUtility.validateString((String) session.getAttribute("isPunchoutUser")).equalsIgnoreCase("Y"))
		{
			try
			{
				if (CommonUtility.customServiceUtility() != null) {
					String chatToken = CommonUtility.customServiceUtility().getChatbotExternalToken(session);
					if(chatToken!= null) {
						renderContent=chatToken;
					}			
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return "ResultLoader";
		}
		else {
			return "SESSIONEXPIRED";
		}
	}
	
	public String woeLogin(){
		String userToken = null;
		String result = "";
		request =ServletActionContext.getRequest();
        response = ServletActionContext.getResponse();
        HttpSession session = request.getSession();
        String isEclipseDown = CommonDBQuery.getSystemParamtersList().get("ERPAVAILABLE");
		String erpLogin =  CommonDBQuery.getSystemParamtersList().get("ERPLOGIN");
		LoginAuthentication loginAuthentication = new LoginAuthentication();
		try {
		LoginSubmitManagement loginSubmit = new LoginSubmitManagementImpl();
		LoginSubmitManagementModel loginSubmitManagementModel = new LoginSubmitManagementModel();
		UserManagementImpl userContact = new UserManagementImpl();
		UsersDAO userUtilityDAO = new UsersDAO();
		UsersModel enteredUserNameInfo = new UsersModel(); 
	    enteredUserNameInfo = userUtilityDAO.getUserStatus((String) request.getParameter("loginId"),true);
    	System.out.println("-------------->"+enteredUserNameInfo.getStatusDescription());
        
    	loginSubmitManagementModel.setUserName(CommonUtility.validateString((String) request.getParameter("loginId")));
    	loginSubmitManagementModel.setPassword(CommonUtility.validateString((String) request.getParameter("password")));
    	
    	if(enteredUserNameInfo.getStatusDescription()==null ||enteredUserNameInfo.getStatusDescription().equalsIgnoreCase("P") ||enteredUserNameInfo.getStatusDescription().equalsIgnoreCase("Y")||enteredUserNameInfo.getStatusDescription().equalsIgnoreCase("")){
        	loginAuthentication = new LoginAuthentication();
        	
        	if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){       		   				
    				userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
    				if(CommonUtility.validateString(userToken).length()>0){
    					UsersModel userDetails = 	userContact.contactInquiry(session.getAttribute("loginUserERPId").toString());
    					userDetails.setUserName((String) request.getParameter("loginId"));
    					userDetails.setPassword((String) request.getParameter("woePassword"));
    					userDetails.setWoeERPUserName((String) request.getParameter("woeUserName"));
    					userDetails.setIsCreditCard("No");
    					renderContent = userContact.woeContactUpdate(userDetails, session);
	    		       }else {
	    		    	   renderContent = "0|Request failed due to invalid session or login information. The invalid information was: Read Fail: WC.PASSWORD.";
	    		       }
        		}else{
        			renderContent = "0|connectionError.";
        		}
    	}else{
    		renderContent = "0|User Already Exist. Please Try with Other User.";
    		}
		}catch (Exception e) {
			renderContent = "0|Error While Login. Contact our Customer Service for Further Assistance.";
			e.printStackTrace();
		}
		return SUCCESS;    	
	}	
	
	public UsersModel selectShipAddressAsDefault(ArrayList<UsersModel> shipAddressList,HttpSession session,UsersModel shipAddress) {
		if(shipAddressList!=null && shipAddressList.get(0)!=null) {
			session.setAttribute("defaultShipToId", CommonUtility.validateParseIntegerToString(shipAddressList.get(0).getAddressBookId()));
			UsersDAO.updateDefaultShipTo(userId,shipAddressList.get(0).getAddressBookId());
			shipAddress = shipAddressList.get(0);
		}
		return shipAddress;
	}	
	
	public String changeEmail() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		target = SUCCESS;
		try {
		if (userId > 1) {
			addressList = new UsersModel();
			addressList = UsersDAO.getEntityDetailsByUserId(userId);
			if (password.equals(addressList.getPassword())) {
				boolean userExist = UsersDAO.checkForUserName(CommonUtility.validateString(request.getParameter("emailAddress")));
				if(userExist) {
					result = "0|"+ LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.emailalreadyregistered");
				}
				else {
					UserManagement usersObj = new UserManagementImpl();
					UsersModel inputDetails = new UsersModel();
					inputDetails.setUserName(addressList.getUserName());
					inputDetails.setErpOverrideFlag(CommonUtility.validateString(request.getParameter("erpOverrideFlag")));
					inputDetails.setFirstName(addressList.getFirstName());
					inputDetails.setLastName(addressList.getLastName());
					inputDetails.setNewUserName(CommonUtility.validateString(request.getParameter("emailAddress")));
					String responseOutput = usersObj.checkforUserNameInERP(inputDetails);
					if(responseOutput != null && responseOutput.contains("successful")) {
					String[] splitResponse = responseOutput.split("\\|");
					if(CommonUtility.validateNumber(splitResponse[1])>0) {
						result = "0|"+ LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.emailalreadyregisteredinpos");
					}else {
					result = "1| success";
					}
					}else {
						result = "0| Something went wrong. Please try again or contact administartion.";
					}
				}
			} else {
				result = "0| Invalid Password entered.";
			}
			renderContent = result;
		} else {
			target = "SESSIONEXPIRED";
		}

	} catch (Exception e) {
		logger.error(e);
		target = "SESSIONEXPIRED";
	}
	return target;
	}
	
	
	public String authChangeEmail() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String Flag = CommonUtility.validateString(request.getParameter("flag"));
			String authCode = CommonUtility.validateString(request.getParameter("authcode"));
			String oldEmail = CommonUtility.validateString(request.getParameter("oldEmail"));
			String newEmail = CommonUtility.validateString(request.getParameter("newEmail"));
			String erpOveerideFlag = CommonUtility.validateString(request.getParameter("erp"));
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			contentObject.put("authCode", authCode);
			contentObject.put("newEmail", newEmail);
			contentObject.put("oldEmail", oldEmail);
			contentObject.put("userId", userId);
			System.out.println("@authCode:  "+authCode+"@old email: "+oldEmail+"new eamail : "+newEmail);
			UserManagement usersObj = new UserManagementImpl();
			String response = usersObj.validateEmailfromESB(oldEmail, authCode, erpOveerideFlag);
			if(response != null ) {
				if(Flag.equalsIgnoreCase("N")) {
					contentObject.put("flag", Flag);
				}
			}else {
				contentObject.put("validationFailed", "Y");
			}
			renderContent = LayoutGenerator.templateLoader("Authenticateuseraccount", contentObject, null, null, null);
		}catch (Exception e) {
			logger.error(e);
		}
		return SUCCESS;
	}
	
	public String updateEmail() {
		result = "";
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		HashMap<String,String> userDetails = new HashMap<String,String>();
		userDetails = UsersDAO.getUserPasswordAndUserId(CommonUtility.validateString(request.getParameter("oldEmail")),"Y");
		SecureData password = new SecureData();
		try {
			if(password.validatePassword(userDetails.get("password")).equals(CommonUtility.validateString(request.getParameter("password")))){
				UserManagement usersObj = new UserManagementImpl();
				UsersModel userInfo = new UsersModel();
				userInfo.setUserName(CommonUtility.validateString(request.getParameter("newEmail")));
				userInfo.setErpOverrideFlag(CommonUtility.validateString(request.getParameter("erpOverrideFlag")));
				userInfo.setBuyingCompanyId(CommonUtility.validateNumber(userDetails.get("buyingCompanyId")));
				userInfo.setUserId(CommonUtility.validateNumber(userDetails.get("userId")));
				userInfo.setEmailAddress(CommonUtility.validateString(request.getParameter("newEmail")));
				result = usersObj.changeEmail(userInfo, session);
				if(result.contains("successful")) {
				SendMailModel sendMailModel = new SendMailModel();
				SendMailUtility sendMailUtility = new SendMailUtility();
				sendMailModel.setToEmailId(CommonUtility.validateString(request.getParameter("newEmail")));
				sendMailModel.setFirstName(userDetails.get("firstName"));
				sendMailModel.setLastName(CommonUtility.validateString(userDetails.get("lastName")));
				boolean flag = sendMailUtility.buildChangeEmailMail(sendMailModel);
				if(flag) {
					logger.info("Email sent successfully");
				}
				}
			}else {
				result = "0 | invalid password";
			}
		}catch (Exception e) {
			logger.error(e);
		}
		renderContent = result;
		return SUCCESS;
	}
	public String generateSalt() {
		result = CIMM2VelocityTool.getInstance().generateSalt();
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		contentObject.put("responseType", "TokenValue");
		contentObject.put("resultData", result);
		renderContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject, null, null, null);
		return SUCCESS;
	}
}