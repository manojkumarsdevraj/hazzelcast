package com.unilog.users;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.bronto.api.BrontoApi;
import com.erp.eclipse.inquiry.LoginSubmit;
import com.erp.service.LoginSubmitManagement;
import com.erp.service.SalesOrderManagement;
import com.erp.service.UserManagement;
import com.erp.service.impl.LoginSubmitManagementImpl;
import com.erp.service.impl.SalesOrderManagementImpl;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.LoginSubmitManagementModel;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.api.bronto.BrontoDAO;
import com.unilog.api.bronto.BrontoModel;
import com.unilog.api.bronto.BrontoUtility;
import com.unilog.captcha.GoogleRecaptchaV3;
import com.unilog.customfields.CustomFieldUtility;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.misc.YahooBossSupport;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.SalesModel;
import com.unilog.security.SecureData;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.ConvertHtmlToPdf;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class NewUserRegisterUtility extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private String renderContent;
	
	/* 1A I have a Web Order Entry User ID and Password*/
	private String currentUserId1A;
	private String currentPassword1A;
	private String iscurrentUserId1A;
	private String emailAddress1A;
	private String newPassword1A;
	private String newPasswordConfirm1A;
	private String newsLetterSub1A;
	private String currentCountry1A;
	private String isCurrentCountry1A;
	private String currentLastOrder1A;
	private String currentFirstName1A;
	private String currentLastName1A;
	private String contactClassification1A;
	private String cimm_User_Id;
	private String user_Name;
	private String password;
	
	/*1B This is my first time ordering online with North Coast Electric*/
	private String companyName1B;
	private String accountNo1B;
	private String firstName1B;
	private String lastName1B;
	private String emailAddress1B;
	private String newPassword1B;
	private String newPasswordConfirm1B;
	private String requestAuthorization1B;
	private String isAuthorizationRequest;
	private String salesContact1B;
	private String companyBillingAddress1B;
	private String suiteNo1B;
	private String cityName1B;
	private String stateName1B;
	private String zipCode1B;
	private String countryName1B;
	private String phoneNo1B;
	private String applyCreditCard1B;
	private String newsLetterSub1B;
	private String contactClassification1B;
	private String salesRepContact1B;
	private String jobTitle1B;
	private String accountManager1B;
	private String closestBranch1B;
	private String poNumber1b;
	
	/* 1C for Createing contact for existing Acount using entity id/Account number
	 */
	private String accountNumber;
	private String invoiceNumber;
	private String invoiceTotal;
	private String contactFirstName;
	private String contactLastName;
	private String contactemailAddress;
	private String contactPassword;
	private String contactPasswordConfirm;
	private String contactAddress1;
	private String contactAddress2;
	private String contactCity;
	private String contactState;
	private String contactZip;
	private String contactCountry;
	private String contactPhone;
	private String contactCompanyName;
	private String contactFax;
	private String roleAssign;
	private String contactClassification;
	/* 1C for Createing contact for existing Acount using entity id/Account number
	 */
	
	/*2A & 2B I would like to place a credit card order as a retail North Coast Electric Customer*/
	private String firstName2AB;
	private String lastName2AB;
	private String companyName2AB;
	private String emailAddress2AB;
	private String password2AB;
	private String confirmPassword2AB;
	private String billingAddress2AB;
	private String suiteNo2AB;
	private String cityName2AB;
	private String stateName2AB;
	private String zipCode2AB;
	private String countryName2AB;
	private String phoneNo2AB;
	private String creditApplicationAccount2AB;
	private String newsLetterSub2AB;
	private String locUser2AB;
	private String countryFulNm2ABB;
	private String contactClassification2A;
	private String contactClassification2B;
	private String form2APrivacyAndTermsCheckBox;
	private String form2APrivacyAndTermsCheckBoxRequired;
	private String salesRepContact2AB;
	private String jobTitle2AB;
	private String fax2AB;
	private String userStatus;
	private String subsetFlag;
	
	/* if Commercial/Retail user registration have different shipping address, we could use below  fields in the form */
	private String shipFirstName;
	private String shipLastName;
	private String shipCompanyName;
	private String shipAddress1;
	private String shipAddress2;
	private String shipCity;
	private String shipState;
	private String shipCountry;
	private String shipZipCode;
	private String shipPhoneNo;
	private String checkSameAsBilling;
	// On Account user registration if user use customer address as his/he address, setting flag  "Yes" in field.
	private String sameAsCustomerAddress; 
	private String validateZipcode;
	

	/*Required Fields*/
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String result;
	protected String target = ERROR;
	
	private String locUser;
	private String buyingCompanyId;
	private String countryFulNm2AB;
	private String fromForm;
	private String checkAccountNumber;
	private String warehouseId1B;
	private String checkCompanyName;
	private String form1BPrivacyAndTermsCheckBox;
	private String form1BPrivacyAndTermsCheckBoxRequired;
	/* For Anonymous User Registraiton*/
	private String activeCustomerId = "";//Set entity id for logged in user & from Non loggedin user SYS.param BEFORE_LOGIN_ACTIVECUSTOMER_ID
	private String auEmailAddress;
	private String auEmail;
	private String auEmailVerify;
	private String auFirstName;
	private String auLastName;
	private String auPhoneNumber;
	private String auPassword;
	private String auConfirmPassword;
	private String auContactClassification;
	private String auChkSameAsBilling;
	private String auBillFirstName;
	private String auBillLastName;
	private String auBillCompanyName;
	private String auBillAddress1;
	private String auBillAddress2;
	private String auBillCity;
	private String auBillState;
	private String auBillCountry;
	private String auBillZipCode;
	private String auBillPhoneNo;
	private String checkJobTitle;
	
	private String auShipFirstName;
	private String auShipLastName;
	private String auShipCompanyName;
	private String auShipAddress1;
	private String auShipAddress2;
	private String auShipCity;
	private String auShipState;
	private String auShipCountry;
	private String auShipZipCode;
	private String auShipPhoneNo;
	private String auNewsLetterSubscription;
	
	private String checkPhoneFomat;
	private String disableSubmitPO;
	private String disableSubmitPOCC;
	
	//-- Credit Application variables
	private String businessName;
	private String date;
	private String businessPhoneNumber;
	private String businessFaxNumber;
	private String billingAddress2C;
	private String shippingAddress2C;
	private String billingCity2C;
	private String shippingCity2C;
	private String billingState2C;
	private String shippingState2C;
	private String billingZipCode2C;
	private String shippingZipCode2C;
	private String legalStructureRadio;
	private String federalIdNumber;
	private String salesTaxStatusRadio;
	private String salesTaxExemptionCertificateFileName;
	private String divisionOf;
	private String subsidiaryOf;
	private String businessTyp;
	private String invoiceRadio;
	private String invoiceByEmailAddress;
	private String invoiceByFaxNumber;
	private String invoiceByEDIContactName;
	private String invoiceByEDIEmailAddress;
	private String[] principalOfficeName;
	private String[] principalOfficeTitle;
	private String dateBusinessCommenced;
	private int numberOfEmployees;
	private String creditLimitRequest;
	private String apContactPersonEmail;
	private String finacialStatmentAvailableRadio;
	private String finacialStatmentFileName;
	private int tradreferenceCount;
	private String[] tradeReferenceName;
	private String[] tradeReferencePhoneNumber;
	private String[] tradeReferenceFaxNumber;
	private String[] tradeReferenceEmailAddress;
	private String[] bankReferenceName;
	private String[] bankReferencePhoneNumber;
	private String[] bankReferenceFaxNumber;
	private String[] bankReferenceContactName;
	private String[] bankReferenceAccountOrLoanNumber;
	private String declarationName;
	private String declaratioEmailAddress;
	private String declarationTitle;
	private String declarationDate;
	private String invoice1b;
	private String creditline;
	private boolean anonymousUser = false;
	private String billingMode;
	private String signature;
	private String amountOfCredit;
	private String phoneNumber;
	private String[] bankReferenceAddress;
	private String[] bankReferenceCity;
	private String[] bankReferenceZipCode;
	private String[] principalOfficeAddress;
	private String[] principalOfficeCity;
	private String[] principalOfficeZipCode;
	private String[] principalOfficeHomePhone;
	private String[] principalOfficeSocSecNo;
	private String[] principalOfficeSpouseName;
	private String[] realEstateName;
	private String[] realEstateAddress;
	private String[] realEstateValue;
	private String[] realEstateTitle;
	private String[] realEstateBalance;
	private String[] realEstateMortgage;
	private String[] realEstateBusiness;
	private String[] realEstateBusinessAddress;
	private String[] realEstateBusinessValue;
	private String[] realEstateBusinessTitle;
	private String[] realEstateBusinessBalance;
	private String[] realEstateBusinessMortgage;
	private String[] otherStates;
	private String[] otherStatesValues;
	private String[] chargeCardName;
	private String[] chargeCardExpDate;
	private String addtionalInformation;
	private String billShipInstruction;
	private String firstName2A;
	private String lastName2A;
	private String[] tradeReferenceAddress;
	private String[] tradeReferenceCity;
	private String[] tradeReferenceZipCode;
	private String[] tradeReferenceAccountNumber;
	private String billingAddressTwo2C;
	private String grandtype;
	private String shippingOrgType;
	private String avalaraAddressValForm;
	private String others;
	private String salesPersonName;
	private String howDidYouSelect;
	private String istaxable;
	private String birthMonth;
	private String isRewardMember;
	private String enablePO;
	private String creditDate;
	private String contactWebsite;
	private String contactTitle;
	
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
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	public String getSalesPersonName() {
		return salesPersonName;
	}
	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
	}
	public String getHowDidYouSelect() {
		return howDidYouSelect;
	}
	public void setHowDidYouSelect(String howDidYouSelect) {
		this.howDidYouSelect = howDidYouSelect;
	}
	public String getBillingMode() {
		return billingMode;
	}
	public void setBillingMode(String billingMode) {
		this.billingMode = billingMode;
	}
	public String getBillingAddressTwo2C() {
		return billingAddressTwo2C;
	}
	public void setBillingAddressTwo2C(String billingAddressTwo2C) {
		this.billingAddressTwo2C = billingAddressTwo2C;
	}
	public String getFirstName2A() {
		return firstName2A;
	}
	public void setFirstName2A(String firstName2A) {
		this.firstName2A = firstName2A;
	}
	public String getLastName2A() {
		return lastName2A;
	}
	public void setLastName2A(String lastName2A) {
		this.lastName2A = lastName2A;
	}
	public String[] getRealEstateBusinessAddress() {
		return realEstateBusinessAddress;
	}
	public void setRealEstateBusinessAddress(String[] realEstateBusinessAddress) {
		this.realEstateBusinessAddress = realEstateBusinessAddress;
	}
	public String[] getRealEstateBusinessValue() {
		return realEstateBusinessValue;
	}
	public void setRealEstateBusinessValue(String[] realEstateBusinessValue) {
		this.realEstateBusinessValue = realEstateBusinessValue;
	}
	public String[] getRealEstateBusinessTitle() {
		return realEstateBusinessTitle;
	}
	public void setRealEstateBusinessTitle(String[] realEstateBusinessTitle) {
		this.realEstateBusinessTitle = realEstateBusinessTitle;
	}
	public String[] getRealEstateBusinessBalance() {
		return realEstateBusinessBalance;
	}
	public void setRealEstateBusinessBalance(String[] realEstateBusinessBalance) {
		this.realEstateBusinessBalance = realEstateBusinessBalance;
	}
	public String[] getRealEstateBusinessMortgage() {
		return realEstateBusinessMortgage;
	}
	public void setRealEstateBusinessMortgage(String[] realEstateBusinessMortgage) {
		this.realEstateBusinessMortgage = realEstateBusinessMortgage;
	}
	public String[] getRealEstateBusiness() {
		return realEstateBusiness;
	}
	public void setRealEstateBusiness(String[] realEstateBusiness) {
		this.realEstateBusiness = realEstateBusiness;
	}
	public String[] getRealEstateName() {
		return realEstateName;
	}
	public void setRealEstateName(String[] realEstateName) {
		this.realEstateName = realEstateName;
	}
	public String[] getRealEstateAddress() {
		return realEstateAddress;
	}
	public void setRealEstateAddress(String[] realEstateAddress) {
		this.realEstateAddress = realEstateAddress;
	}
	public String[] getRealEstateValue() {
		return realEstateValue;
	}
	public void setRealEstateValue(String[] realEstateValue) {
		this.realEstateValue = realEstateValue;
	}
	public String[] getRealEstateTitle() {
		return realEstateTitle;
	}
	public void setRealEstateTitle(String[] realEstateTitle) {
		this.realEstateTitle = realEstateTitle;
	}
	public String[] getRealEstateBalance() {
		return realEstateBalance;
	}
	public void setRealEstateBalance(String[] realEstateBalance) {
		this.realEstateBalance = realEstateBalance;
	}
	public String[] getRealEstateMortgage() {
		return realEstateMortgage;
	}
	public void setRealEstateMortgage(String[] realEstateMortgage) {
		this.realEstateMortgage = realEstateMortgage;
	}
	//--Credit Application variables
	public String getInvoice1b() {
		return invoice1b;
	}
	public String getCreditline() {
		return creditline;
	}
	public void setCreditline(String creditline) {
		this.creditline = creditline;
	}
	public void setInvoice1b(String invoice1b) {
		this.invoice1b = invoice1b;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCreditApplicationAccount2AB() {
		return creditApplicationAccount2AB;
	}
	public void setCreditApplicationAccount2AB(String creditApplicationAccount2AB) {
		this.creditApplicationAccount2AB = creditApplicationAccount2AB;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public String getActiveCustomerId() {
		return activeCustomerId;
	}
	public void setActiveCustomerId(String activeCustomerId) {
		this.activeCustomerId = activeCustomerId;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getInvoiceTotal() {
		return invoiceTotal;
	}
	public void setInvoiceTotal(String invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}
	public String getContactCompanyName() {
		return contactCompanyName;
	}
	public void setContactCompanyName(String contactCompanyName) {
		this.contactCompanyName = contactCompanyName;
	}
	public String getRoleAssign() {
		return roleAssign;
	}
	public void setRoleAssign(String roleAssign) {
		this.roleAssign = roleAssign;
	}
	public String getContactClassification() {
		return contactClassification;
	}
	public void setContactClassification(String contactClassification) {
		this.contactClassification = contactClassification;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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
	public String getContactemailAddress() {
		return contactemailAddress;
	}
	public boolean isAnonymousUser() {
		return anonymousUser;
	}
	public void setAnonymousUser(boolean anonymousUser) {
		this.anonymousUser = anonymousUser;
	}
	public void setContactemailAddress(String contactemailAddress) {
		this.contactemailAddress = contactemailAddress;
	}
	public String getContactPassword() {
		return contactPassword;
	}
	public void setContactPassword(String contactPassword) {
		this.contactPassword = contactPassword;
	}
	public String getContactPasswordConfirm() {
		return contactPasswordConfirm;
	}
	public void setContactPasswordConfirm(String contactPasswordConfirm) {
		this.contactPasswordConfirm = contactPasswordConfirm;
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
	public String getContactState() {
		return contactState;
	}
	public void setContactState(String contactState) {
		this.contactState = contactState;
	}
	public String getContactZip() {
		return contactZip;
	}
	public void setContactZip(String contactZip) {
		this.contactZip = contactZip;
	}
	public String getContactCountry() {
		return contactCountry;
	}
	public void setContactCountry(String contactCountry) {
		this.contactCountry = contactCountry;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactFax() {
		return contactFax;
	}
	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}
	public String getValidateZipcode() {
		return validateZipcode;
	}
	public void setValidateZipcode(String validateZipcode) {
		this.validateZipcode = validateZipcode;
	}
	public String getSameAsCustomerAddress() {
		return sameAsCustomerAddress;
	}
	public void setSameAsCustomerAddress(String sameAsCustomerAddress) {
		this.sameAsCustomerAddress = sameAsCustomerAddress;
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
	public String getShipCompanyName() {
		return shipCompanyName;
	}
	public void setShipCompanyName(String shipCompanyName) {
		this.shipCompanyName = shipCompanyName;
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
	public String getShipState() {
		return shipState;
	}
	public void setShipState(String shipState) {
		this.shipState = shipState;
	}
	public String getShipCountry() {
		return shipCountry;
	}
	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}
	public String getShipZipCode() {
		return shipZipCode;
	}
	public void setShipZipCode(String shipZipCode) {
		this.shipZipCode = shipZipCode;
	}
	public String getShipPhoneNo() {
		return shipPhoneNo;
	}
	public void setShipPhoneNo(String shipPhoneNo) {
		this.shipPhoneNo = shipPhoneNo;
	}
	public String getCheckSameAsBilling() {
		return checkSameAsBilling;
	}
	public void setCheckSameAsBilling(String checkSameAsBilling) {
		this.checkSameAsBilling = checkSameAsBilling;
	}
	public int getTradreferenceCount() {
		return tradreferenceCount;
	}
	public void setTradreferenceCount(int tradreferenceCount) {
		this.tradreferenceCount = tradreferenceCount;
	}
	public String getFinacialStatmentFileName() {
		return finacialStatmentFileName;
	}
	public void setFinacialStatmentFileName(String finacialStatmentFileName) {
		this.finacialStatmentFileName = finacialStatmentFileName;
	}
	public String getBusinessName() {
		return businessName;
	}
		public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBusinessPhoneNumber() {
		return businessPhoneNumber;
	}
	public void setBusinessPhoneNumber(String businessPhoneNumber) {
		this.businessPhoneNumber = businessPhoneNumber;
	}
	public String getBusinessFaxNumber() {
		return businessFaxNumber;
	}
	public void setBusinessFaxNumber(String businessFaxNumber) {
		this.businessFaxNumber = businessFaxNumber;
	}
	public String getBillingAddress2C() {
		return billingAddress2C;
	}
	public void setBillingAddress2C(String billingAddress2C) {
		this.billingAddress2C = billingAddress2C;
	}
	public String getShippingAddress2C() {
		return shippingAddress2C;
	}
	public void setShippingAddress2C(String shippingAddress2C) {
		this.shippingAddress2C = shippingAddress2C;
	}
	public String getBillingCity2C() {
		return billingCity2C;
	}
	public void setBillingCity2C(String billingCity2C) {
		this.billingCity2C = billingCity2C;
	}
	public String getShippingCity2C() {
		return shippingCity2C;
	}
	public void setShippingCity2C(String shippingCity2C) {
		this.shippingCity2C = shippingCity2C;
	}
	public String getBillingState2C() {
		return billingState2C;
	}
	public void setBillingState2C(String billingState2C) {
		this.billingState2C = billingState2C;
	}
	public String getShippingState2C() {
		return shippingState2C;
	}
	public void setShippingState2C(String shippingState2C) {
		this.shippingState2C = shippingState2C;
	}
	public String getBillingZipCode2C() {
		return billingZipCode2C;
	}
	public void setBillingZipCode2C(String billingZipCode2C) {
		this.billingZipCode2C = billingZipCode2C;
	}
	public String getShippingZipCode2C() {
		return shippingZipCode2C;
	}
	public void setShippingZipCode2C(String shippingZipCode2C) {
		this.shippingZipCode2C = shippingZipCode2C;
	}
	public String getLegalStructureRadio() {
		return legalStructureRadio;
	}
	public void setLegalStructureRadio(String legalStructureRadio) {
		this.legalStructureRadio = legalStructureRadio;
	}
	public String getFederalIdNumber() {
		return federalIdNumber;
	}
	public void setFederalIdNumber(String federalIdNumber) {
		this.federalIdNumber = federalIdNumber;
	}
	public String getSalesTaxStatusRadio() {
		return salesTaxStatusRadio;
	}
	public void setSalesTaxStatusRadio(String salesTaxStatusRadio) {
		this.salesTaxStatusRadio = salesTaxStatusRadio;
	}
	public String getSalesTaxExemptionCertificateFileName() {
		return salesTaxExemptionCertificateFileName;
	}
	public void setSalesTaxExemptionCertificateFileName(
			String salesTaxExemptionCertificateFileName) {
		this.salesTaxExemptionCertificateFileName = salesTaxExemptionCertificateFileName;
	}
	public String getDivisionOf() {
		return divisionOf;
	}
	public void setDivisionOf(String divisionOf) {
		this.divisionOf = divisionOf;
	}
	public String getSubsidiaryOf() {
		return subsidiaryOf;
	}
	public void setSubsidiaryOf(String subsidiaryOf) {
		this.subsidiaryOf = subsidiaryOf;
	}
	public String getBusinessTyp() {
		return businessTyp;
	}
	public void setBusinessTyp(String businessTyp) {
		this.businessTyp = businessTyp;
	}
	public String getInvoiceRadio() {
		return invoiceRadio;
	}
	public void setInvoiceRadio(String invoiceRadio) {
		this.invoiceRadio = invoiceRadio;
	}
	public String getInvoiceByEmailAddress() {
		return invoiceByEmailAddress;
	}
	public void setInvoiceByEmailAddress(String invoiceByEmailAddress) {
		this.invoiceByEmailAddress = invoiceByEmailAddress;
	}
	public String getInvoiceByFaxNumber() {
		return invoiceByFaxNumber;
	}
	public void setInvoiceByFaxNumber(String invoiceByFaxNumber) {
		this.invoiceByFaxNumber = invoiceByFaxNumber;
	}
	public String getInvoiceByEDIContactName() {
		return invoiceByEDIContactName;
	}
	public void setInvoiceByEDIContactName(String invoiceByEDIContactName) {
		this.invoiceByEDIContactName = invoiceByEDIContactName;
	}
	public String getInvoiceByEDIEmailAddress() {
		return invoiceByEDIEmailAddress;
	}
	public void setInvoiceByEDIEmailAddress(String invoiceByEDIEmailAddress) {
		this.invoiceByEDIEmailAddress = invoiceByEDIEmailAddress;
	}
	public String getDateBusinessCommenced() {
		return dateBusinessCommenced;
	}
	public void setDateBusinessCommenced(String dateBusinessCommenced) {
		this.dateBusinessCommenced = dateBusinessCommenced;
	}
	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}
	public void setNumberOfEmployees(int numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}
	
	public String getCreditLimitRequest() {
		return creditLimitRequest;
	}
	public void setCreditLimitRequest(String creditLimitRequest) {
		this.creditLimitRequest = creditLimitRequest;
	}
	public String getApContactPersonEmail() {
		return apContactPersonEmail;
	}
	public void setApContactPersonEmail(String apContactPersonEmail) {
		this.apContactPersonEmail = apContactPersonEmail;
	}
	public String getFinacialStatmentAvailableRadio() {
		return finacialStatmentAvailableRadio;
	}
	public void setFinacialStatmentAvailableRadio(
			String finacialStatmentAvailableRadio) {
		this.finacialStatmentAvailableRadio = finacialStatmentAvailableRadio;
	}
	public String[] getPrincipalOfficeName() {
		return principalOfficeName;
	}
	public void setPrincipalOfficeName(String[] principalOfficeName) {
		this.principalOfficeName = principalOfficeName;
	}
	public String[] getPrincipalOfficeTitle() {
		return principalOfficeTitle;
	}
	public void setPrincipalOfficeTitle(String[] principalOfficeTitle) {
		this.principalOfficeTitle = principalOfficeTitle;
	}
	public String[] getTradeReferenceName() {
		return tradeReferenceName;
	}
	public void setTradeReferenceName(String[] tradeReferenceName) {
		this.tradeReferenceName = tradeReferenceName;
	}
	public String[] getTradeReferencePhoneNumber() {
		return tradeReferencePhoneNumber;
	}
	public void setTradeReferencePhoneNumber(String[] tradeReferencePhoneNumber) {
		this.tradeReferencePhoneNumber = tradeReferencePhoneNumber;
	}
	public String[] getTradeReferenceFaxNumber() {
		return tradeReferenceFaxNumber;
	}
	public void setTradeReferenceFaxNumber(String[] tradeReferenceFaxNumber) {
		this.tradeReferenceFaxNumber = tradeReferenceFaxNumber;
	}
	public String[] getTradeReferenceEmailAddress() {
		return tradeReferenceEmailAddress;
	}
	public void setTradeReferenceEmailAddress(String[] tradeReferenceEmailAddress) {
		this.tradeReferenceEmailAddress = tradeReferenceEmailAddress;
	}
	public String[] getBankReferenceName() {
		return bankReferenceName;
	}
	public void setBankReferenceName(String[] bankReferenceName) {
		this.bankReferenceName = bankReferenceName;
	}
	public String[] getBankReferencePhoneNumber() {
		return bankReferencePhoneNumber;
	}
	public void setBankReferencePhoneNumber(String[] bankReferencePhoneNumber) {
		this.bankReferencePhoneNumber = bankReferencePhoneNumber;
	}
	public String[] getBankReferenceFaxNumber() {
		return bankReferenceFaxNumber;
	}
	public void setBankReferenceFaxNumber(String[] bankReferenceFaxNumber) {
		this.bankReferenceFaxNumber = bankReferenceFaxNumber;
	}
	public String[] getBankReferenceContactName() {
		return bankReferenceContactName;
	}
	public void setBankReferenceContactName(String[] bankReferenceContactName) {
		this.bankReferenceContactName = bankReferenceContactName;
	}
	public String[] getBankReferenceAccountOrLoanNumber() {
		return bankReferenceAccountOrLoanNumber;
	}
	public void setBankReferenceAccountOrLoanNumber(
			String[] bankReferenceAccountOrLoanNumber) {
		this.bankReferenceAccountOrLoanNumber = bankReferenceAccountOrLoanNumber;
	}
	public String getDeclarationName() {
		return declarationName;
	}
	public void setDeclarationName(String declarationName) {
		this.declarationName = declarationName;
	}
	public String getDeclaratioEmailAddress() {
		return declaratioEmailAddress;
	}
	public void setDeclaratioEmailAddress(String declaratioEmailAddress) {
		this.declaratioEmailAddress = declaratioEmailAddress;
	}
	public String getDeclarationTitle() {
		return declarationTitle;
	}
	public void setDeclarationTitle(String declarationTitle) {
		this.declarationTitle = declarationTitle;
	}
	public String getDeclarationDate() {
		return declarationDate;
	}
	public void setDeclarationDate(String declarationDate) {
		this.declarationDate = declarationDate;
	}
	public String getAccountManager1B() {
		return accountManager1B;
	}
	public void setAccountManager1B(String accountManager1B) {
		this.accountManager1B = accountManager1B;
	}
	public String getClosestBranch1B() {
		return closestBranch1B;
	}
	public void setClosestBranch1B(String closestBranch1B) {
		this.closestBranch1B = closestBranch1B;
	}
	private String[] userCustomField;
	
	public String getFax2AB() {
		return fax2AB;
	}
	public void setFax2AB(String fax2ab) {
		fax2AB = fax2ab;
	}
	public String getJobTitle1B() {
		return jobTitle1B;
	}
	public void setJobTitle1B(String jobTitle1B) {
		this.jobTitle1B = jobTitle1B;
	}
	public String getSalesRepContact2AB() {
		return salesRepContact2AB;
	}
	public void setSalesRepContact2AB(String salesRepContact2AB) {
		this.salesRepContact2AB = salesRepContact2AB;
	}
	public String getJobTitle2AB() {
		return jobTitle2AB;
	}
	public void setJobTitle2AB(String jobTitle2AB) {
		this.jobTitle2AB = jobTitle2AB;
	}
	public String getSalesRepContact1B() {
		return salesRepContact1B;
	}
	public void setSalesRepContact1B(String salesRepContact1B) {
		this.salesRepContact1B = salesRepContact1B;
	}
	public String getForm2APrivacyAndTermsCheckBox() {
		return form2APrivacyAndTermsCheckBox;
	}
	public void setForm2APrivacyAndTermsCheckBox(
			String form2aPrivacyAndTermsCheckBox) {
		form2APrivacyAndTermsCheckBox = form2aPrivacyAndTermsCheckBox;
	}
	public String getForm2APrivacyAndTermsCheckBoxRequired() {
		return form2APrivacyAndTermsCheckBoxRequired;
	}
	public void setForm2APrivacyAndTermsCheckBoxRequired(
			String form2aPrivacyAndTermsCheckBoxRequired) {
		form2APrivacyAndTermsCheckBoxRequired = form2aPrivacyAndTermsCheckBoxRequired;
	}
	public String getForm1BPrivacyAndTermsCheckBoxRequired() {
		return form1BPrivacyAndTermsCheckBoxRequired;
	}
	public void setForm1BPrivacyAndTermsCheckBoxRequired(
			String form1bPrivacyAndTermsCheckBoxRequired) {
		form1BPrivacyAndTermsCheckBoxRequired = form1bPrivacyAndTermsCheckBoxRequired;
	}
	public String getForm1BPrivacyAndTermsCheckBox() {
		return form1BPrivacyAndTermsCheckBox;
	}
	public void setForm1BPrivacyAndTermsCheckBox(
			String form1bPrivacyAndTermsCheckBox) {
		form1BPrivacyAndTermsCheckBox = form1bPrivacyAndTermsCheckBox;
	}
	public String getWarehouseId1B() {
		return warehouseId1B;
	}
	public String getCheckCompanyName() {
		return checkCompanyName;
	}
	public void setCheckCompanyName(String checkCompanyName) {
		this.checkCompanyName = checkCompanyName;
	}
	public void setWarehouseId1B(String warehouseId1B) {
		this.warehouseId1B = warehouseId1B;
	}
	public String getCheckAccountNumber() {
		return checkAccountNumber;
	}
	public void setCheckAccountNumber(String checkAccountNumber) {
		this.checkAccountNumber = checkAccountNumber;
	}
	public String getFromForm() {
		return fromForm;
	}
	public void setFromForm(String fromForm) {
		this.fromForm = fromForm;
	}
	public String getContactClassification1A() {
		return contactClassification1A;
	}
	public void setContactClassification1A(String contactClassification1A) {
		this.contactClassification1A = contactClassification1A;
	}
	public String getContactClassification1B() {
		return contactClassification1B;
	}
	public void setContactClassification1B(String contactClassification1B) {
		this.contactClassification1B = contactClassification1B;
	}
	public String getContactClassification2A() {
		return contactClassification2A;
	}
	public void setContactClassification2A(String contactClassification2A) {
		this.contactClassification2A = contactClassification2A;
	}
	public String getContactClassification2B() {
		return contactClassification2B;
	}
	public void setContactClassification2B(String contactClassification2B) {
		this.contactClassification2B = contactClassification2B;
	}
	public String getIsCurrentCountry1A() {
		return isCurrentCountry1A;
	}
	public void setIsCurrentCountry1A(String isCurrentCountry1A) {
		this.isCurrentCountry1A = isCurrentCountry1A;
	}
	public String getIscurrentUserId1A() {
		return iscurrentUserId1A;
	}
	public void setIscurrentUserId1A(String iscurrentUserId1A) {
		this.iscurrentUserId1A = iscurrentUserId1A;
	}
	public String getIsAuthorizationRequest() {
		return isAuthorizationRequest;
	}
	public void setIsAuthorizationRequest(String isAuthorizationRequest) {
		this.isAuthorizationRequest = isAuthorizationRequest;
	}
	public String getCurrentCountry1A() {
		return currentCountry1A;
	}
	public void setCurrentCountry1A(String currentCountry1A) {
		this.currentCountry1A = currentCountry1A;
	}
	public String getCurrentLastOrder1A() {
		return currentLastOrder1A;
	}
	public void setCurrentLastOrder1A(String currentLastOrder1A) {
		this.currentLastOrder1A = currentLastOrder1A;
	}
	public String getCurrentFirstName1A() {
		return currentFirstName1A;
	}
	public void setCurrentFirstName1A(String currentFirstName1A) {
		this.currentFirstName1A = currentFirstName1A;
	}
	public String getCurrentLastName1A() {
		return currentLastName1A;
	}
	public void setCurrentLastName1A(String currentLastName1A) {
		this.currentLastName1A = currentLastName1A;
	}
	public String getCountryFulNm2ABB() {
		return countryFulNm2ABB;
	}
	public void setCountryFulNm2ABB(String countryFulNm2ABB) {
		this.countryFulNm2ABB = countryFulNm2ABB;
	}
	public String getLocUser2AB() {
		return locUser2AB;
	}
	public void setLocUser2AB(String locUser2AB) {
		this.locUser2AB = locUser2AB;
	}
	public String getRenderContent() {
		return renderContent;
	}
	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	public String getLocUser() {
		return locUser;
	}
	public void setLocUser(String locUser) {
		this.locUser = locUser;
	}
	public String getBuyingCompanyId() {
		return buyingCompanyId;
	}
	public void setBuyingCompanyId(String buyingCompanyId) {
		this.buyingCompanyId = buyingCompanyId;
	}
	public String getCountryFulNm2AB() {
		return countryFulNm2AB;
	}
	public void setCountryFulNm2AB(String countryFulNm2AB) {
		this.countryFulNm2AB = countryFulNm2AB;
	}
	public String getCurrentUserId1A() {
		return currentUserId1A;
	}
	public void setCurrentUserId1A(String currentUserId1A) {
		this.currentUserId1A = currentUserId1A;
	}
	public String getCurrentPassword1A() {
		return currentPassword1A;
	}
	public void setCurrentPassword1A(String currentPassword1A) {
		this.currentPassword1A = currentPassword1A;
	}
	public String getEmailAddress1A() {
		return emailAddress1A;
	}
	public void setEmailAddress1A(String emailAddress1A) {
		this.emailAddress1A = emailAddress1A;
	}
	public String getNewPassword1A() {
		return newPassword1A;
	}
	public void setNewPassword1A(String newPassword1A) {
		this.newPassword1A = newPassword1A;
	}
	public String getNewPasswordConfirm1A() {
		return newPasswordConfirm1A;
	}
	public void setNewPasswordConfirm1A(String newPasswordConfirm1A) {
		this.newPasswordConfirm1A = newPasswordConfirm1A;
	}
	public String getNewsLetterSub1A() {
		return newsLetterSub1A;
	}
	public void setNewsLetterSub1A(String newsLetterSub1A) {
		this.newsLetterSub1A = newsLetterSub1A;
	}
	public String getCompanyName1B() {
		return companyName1B;
	}
	public void setCompanyName1B(String companyName1B) {
		this.companyName1B = companyName1B;
	}
	public String getAccountNo1B() {
		return accountNo1B;
	}
	public void setAccountNo1B(String accountNo1B) {
		this.accountNo1B = accountNo1B;
	}
	public String getFirstName1B() {
		return firstName1B;
	}
	public void setFirstName1B(String firstName1B) {
		this.firstName1B = firstName1B;
	}
	public String getLastName1B() {
		return lastName1B;
	}
	public void setLastName1B(String lastName1B) {
		this.lastName1B = lastName1B;
	}
	public String getEmailAddress1B() {
		return emailAddress1B;
	}
	public void setEmailAddress1B(String emailAddress1B) {
		this.emailAddress1B = emailAddress1B;
	}
	public String getNewPassword1B() {
		return newPassword1B;
	}
	public void setNewPassword1B(String newPassword1B) {
		this.newPassword1B = newPassword1B;
	}
	public String getNewPasswordConfirm1B() {
		return newPasswordConfirm1B;
	}
	public void setNewPasswordConfirm1B(String newPasswordConfirm1B) {
		this.newPasswordConfirm1B = newPasswordConfirm1B;
	}
	public String getRequestAuthorization1B() {
		return requestAuthorization1B;
	}
	public void setRequestAuthorization1B(String requestAuthorization1B) {
		this.requestAuthorization1B = requestAuthorization1B;
	}
	public String getSalesContact1B() {
		return salesContact1B;
	}
	public void setSalesContact1B(String salesContact1B) {
		this.salesContact1B = salesContact1B;
	}
	public String getCompanyBillingAddress1B() {
		return companyBillingAddress1B;
	}
	public void setCompanyBillingAddress1B(String companyBillingAddress1B) {
		this.companyBillingAddress1B = companyBillingAddress1B;
	}
	public String getSuiteNo1B() {
		return suiteNo1B;
	}
	public void setSuiteNo1B(String suiteNo1B) {
		this.suiteNo1B = suiteNo1B;
	}
	public String getCityName1B() {
		return cityName1B;
	}
	public void setCityName1B(String cityName1B) {
		this.cityName1B = cityName1B;
	}
	public String getStateName1B() {
		return stateName1B;
	}
	public void setStateName1B(String stateName1B) {
		this.stateName1B = stateName1B;
	}
	public String getZipCode1B() {
		return zipCode1B;
	}
	public void setZipCode1B(String zipCode1B) {
		this.zipCode1B = zipCode1B;
	}
	public String getCountryName1B() {
		return countryName1B;
	}
	public void setCountryName1B(String countryName1B) {
		this.countryName1B = countryName1B;
	}
	public String getPhoneNo1B() {
		return phoneNo1B;
	}
	public void setPhoneNo1B(String phoneNo1B) {
		this.phoneNo1B = phoneNo1B;
	}
	public String getApplyCreditCard1B() {
		return applyCreditCard1B;
	}
	public void setApplyCreditCard1B(String applyCreditCard1B) {
		this.applyCreditCard1B = applyCreditCard1B;
	}
	public String getNewsLetterSub1B() {
		return newsLetterSub1B;
	}
	public void setNewsLetterSub1B(String newsLetterSub1B) {
		this.newsLetterSub1B = newsLetterSub1B;
	}
	public String getFirstName2AB() {
		return firstName2AB;
	}
	public void setFirstName2AB(String firstName2AB) {
		this.firstName2AB = firstName2AB;
	}
	public String getLastName2AB() {
		return lastName2AB;
	}
	public void setLastName2AB(String lastName2AB) {
		this.lastName2AB = lastName2AB;
	}
	public String getCompanyName2AB() {
		return companyName2AB;
	}
	public void setCompanyName2AB(String companyName2AB) {
		this.companyName2AB = companyName2AB;
	}
	public String getEmailAddress2AB() {
		return emailAddress2AB;
	}
	public void setEmailAddress2AB(String emailAddress2AB) {
		this.emailAddress2AB = emailAddress2AB;
	}
	public String getPassword2AB() {
		return password2AB;
	}
	public void setPassword2AB(String password2ab) {
		password2AB = password2ab;
	}
	public String getConfirmPassword2AB() {
		return confirmPassword2AB;
	}
	public void setConfirmPassword2AB(String confirmPassword2AB) {
		this.confirmPassword2AB = confirmPassword2AB;
	}
	public String getBillingAddress2AB() {
		return billingAddress2AB;
	}
	public void setBillingAddress2AB(String billingAddress2AB) {
		this.billingAddress2AB = billingAddress2AB;
	}
	public String getSuiteNo2AB() {
		return suiteNo2AB;
	}
	public void setSuiteNo2AB(String suiteNo2AB) {
		this.suiteNo2AB = suiteNo2AB;
	}
	public String getCityName2AB() {
		return cityName2AB;
	}
	public void setCityName2AB(String cityName2AB) {
		this.cityName2AB = cityName2AB;
	}
	public String getStateName2AB() {
		return stateName2AB;
	}
	public void setStateName2AB(String stateName2AB) {
		this.stateName2AB = stateName2AB;
	}
	public String getZipCode2AB() {
		return zipCode2AB;
	}
	public void setZipCode2AB(String zipCode2AB) {
		this.zipCode2AB = zipCode2AB;
	}
	public String getCountryName2AB() {
		return countryName2AB;
	}
	public void setCountryName2AB(String countryName2AB) {
		this.countryName2AB = countryName2AB;
	}
	public String getPhoneNo2AB() {
		return phoneNo2AB;
	}
	public void setPhoneNo2AB(String phoneNo2AB) {
		this.phoneNo2AB = phoneNo2AB;
	}
	public String getNewsLetterSub2AB() {
		return newsLetterSub2AB;
	}
	public void setNewsLetterSub2AB(String newsLetterSub2AB) {
		this.newsLetterSub2AB = newsLetterSub2AB;
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
	public void setResult(String result) {
		this.result = result;
	}
	public String getResult() {
		return result;
	}
	
	
	public String getAuEmail() {
		return auEmail;
	}
	public void setAuEmail(String auEmail) {
		this.auEmail = auEmail;
	}
	public String getAuFirstName() {
		return auFirstName;
	}
	public void setAuFirstName(String auFirstName) {
		this.auFirstName = auFirstName;
	}
	public String getAuLastName() {
		return auLastName;
	}
	public void setAuLastName(String auLastName) {
		this.auLastName = auLastName;
	}
	public String getAuPhoneNumber() {
		return auPhoneNumber;
	}
	public void setAuPhoneNumber(String auPhoneNumber) {
		this.auPhoneNumber = auPhoneNumber;
	}
	public String getAuPassword() {
		return auPassword;
	}
	public void setAuPassword(String auPassword) {
		this.auPassword = auPassword;
	}
	public String getAuBillFirstName() {
		return auBillFirstName;
	}
	public void setAuBillFirstName(String auBillFirstName) {
		this.auBillFirstName = auBillFirstName;
	}
	public String getAuBillLastName() {
		return auBillLastName;
	}
	public void setAuBillLastName(String auBillLastName) {
		this.auBillLastName = auBillLastName;
	}
	public String getAuBillCompanyName() {
		return auBillCompanyName;
	}
	public void setAuBillCompanyName(String auBillCompanyName) {
		this.auBillCompanyName = auBillCompanyName;
	}
	public String getAuBillAddress1() {
		return auBillAddress1;
	}
	public void setAuBillAddress1(String auBillAddress1) {
		this.auBillAddress1 = auBillAddress1;
	}
	public String getAuBillAddress2() {
		return auBillAddress2;
	}
	public void setAuBillAddress2(String auBillAddress2) {
		this.auBillAddress2 = auBillAddress2;
	}
	public String getAuBillCity() {
		return auBillCity;
	}
	public void setAuBillCity(String auBillCity) {
		this.auBillCity = auBillCity;
	}
	public String getAuBillState() {
		return auBillState;
	}
	public void setAuBillState(String auBillState) {
		this.auBillState = auBillState;
	}
	public String getAuBillCountry() {
		return auBillCountry;
	}
	public void setAuBillCountry(String auBillCountry) {
		this.auBillCountry = auBillCountry;
	}
	public String getAuBillZipCode() {
		return auBillZipCode;
	}
	public void setAuBillZipCode(String auBillZipCode) {
		this.auBillZipCode = auBillZipCode;
	}
	public String getAuBillPhoneNo() {
		return auBillPhoneNo;
	}
	public void setAuBillPhoneNo(String auBillPhoneNo) {
		this.auBillPhoneNo = auBillPhoneNo;
	}
	public String getAuShipFirstName() {
		return auShipFirstName;
	}
	public void setAuShipFirstName(String auShipFirstName) {
		this.auShipFirstName = auShipFirstName;
	}
	public String getAuShipLastName() {
		return auShipLastName;
	}
	public void setAuShipLastName(String auShipLastName) {
		this.auShipLastName = auShipLastName;
	}
	public String getAuShipCompanyName() {
		return auShipCompanyName;
	}
	public void setAuShipCompanyName(String auShipCompanyName) {
		this.auShipCompanyName = auShipCompanyName;
	}
	public String getAuShipAddress1() {
		return auShipAddress1;
	}
	public void setAuShipAddress1(String auShipAddress1) {
		this.auShipAddress1 = auShipAddress1;
	}
	public String getAuShipAddress2() {
		return auShipAddress2;
	}
	public void setAuShipAddress2(String auShipAddress2) {
		this.auShipAddress2 = auShipAddress2;
	}
	public String getAuShipCity() {
		return auShipCity;
	}
	public void setAuShipCity(String auShipCity) {
		this.auShipCity = auShipCity;
	}
	public String getAuShipState() {
		return auShipState;
	}
	public void setAuShipState(String auShipState) {
		this.auShipState = auShipState;
	}
	public String getAuShipCountry() {
		return auShipCountry;
	}
	public void setAuShipCountry(String auShipCountry) {
		this.auShipCountry = auShipCountry;
	}
	public String getAuShipZipCode() {
		return auShipZipCode;
	}
	public void setAuShipZipCode(String auShipZipCode) {
		this.auShipZipCode = auShipZipCode;
	}
	public String getAuShipPhoneNo() {
		return auShipPhoneNo;
	}
	public void setAuShipPhoneNo(String auShipPhoneNo) {
		this.auShipPhoneNo = auShipPhoneNo;
	}
	public String getAuConfirmPassword() {
		return auConfirmPassword;
	}
	public void setAuConfirmPassword(String auConfirmPassword) {
		this.auConfirmPassword = auConfirmPassword;
	}
	public void setAuChkSameAsBilling(String auChkSameAsBilling) {
		this.auChkSameAsBilling = auChkSameAsBilling;
	}
	public String getAuChkSameAsBilling() {
		return auChkSameAsBilling;
	}
	public void setAuContactClassification(String auContactClassification) {
		this.auContactClassification = auContactClassification;
	}
	public String getAuContactClassification() {
		return auContactClassification;
	}
	public String getCheckPhoneFomat() {
		return checkPhoneFomat;
	}
	public void setCheckPhoneFomat(String checkPhoneFomat) {
		this.checkPhoneFomat = checkPhoneFomat;
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
	public void setUserCustomField(String[] userCustomField) {
		this.userCustomField = userCustomField;
	}
	public String[] getUserCustomField() {
		return userCustomField;
	}
	public void setAuEmailVerify(String auEmailVerify) {
		this.auEmailVerify = auEmailVerify;
	}
	public String getAuEmailVerify() {
		return auEmailVerify;
	}
	public void setAuNewsLetterSubscription(String auNewsLetterSubscription) {
		this.auNewsLetterSubscription = auNewsLetterSubscription;
	}
	public String getAuNewsLetterSubscription() {
		return auNewsLetterSubscription;
	}
	public String getAuEmailAddress() {
		return auEmailAddress;
	}
	public void setAuEmailAddress(String auEmailAddress) {
		this.auEmailAddress = auEmailAddress;
	}
	
	public String getPoNumber1b() {
		return poNumber1b;
	}
	public void setPoNumber1b(String poNumber1b) {
		this.poNumber1b = poNumber1b;
	}
	
	public String getSubsetFlag() {
		return subsetFlag;
	}
	public void setSubsetFlag(String subsetFlag) {
		this.subsetFlag = subsetFlag;
	}
	public String getEnablePO() {
		return enablePO;
	}
	public void setEnablePO(String enablePO) {
		this.enablePO = enablePO;
	}
	
	public String RegisterNewContactAccountValidate(){
		result = "";	
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		boolean isValidUser = true;
		try{
			String lastOrderNumber="";
			if(accountNumber.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Account Number.|";}else{
				String userToken="";
				LoginSubmitManagement loginSubmit = new LoginSubmitManagementImpl();
				LoginSubmitManagementModel loginSubmitManagementModel = new LoginSubmitManagementModel();
				loginSubmitManagementModel.setUserName(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_OE_ADMIN_USER_NAME")));
				loginSubmitManagementModel.setPassword(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_OE_ADMIN_PASSWORD")));
				loginSubmitManagementModel.setActiveCustomerId(CommonUtility.validateString(accountNumber));
				userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
				if(CommonUtility.validateString(userToken).length()>0){
					session.setAttribute("userToken",userToken);
					SalesOrderManagement salesObj = new SalesOrderManagementImpl();
					SalesModel salesInputParameter = new SalesModel();
					
					Date today = new Date();
					Format formatter=new SimpleDateFormat("MM/dd/yyyy");
					Calendar cal = new GregorianCalendar();
					cal.setTime(today);
					cal.add(Calendar.DAY_OF_MONTH, -30);
					Date todayMinus30 = cal.getTime();
					String startDate = formatter.format(todayMinus30);
					String endDate = formatter.format(today);
					System.out.println("Start Date : "+formatter.format(todayMinus30)+" : End Date : "+formatter.format(today));
					salesInputParameter.setSession(session);
					salesInputParameter.setUserName(loginSubmitManagementModel.getUserName());
					salesInputParameter.setUserToken(loginSubmitManagementModel.getPassword());
					salesInputParameter.setEntityId(CommonUtility.validateString(accountNumber));
					salesInputParameter.setStartDate(startDate);
					salesInputParameter.setEndDate(endDate);
					ArrayList<SalesModel> orderList = salesObj.OrderHistory(salesInputParameter);
					
					//LinkedHashMap<String, Double> invoiceListDetails = new LinkedHashMap<String, Double>();
					if(orderList!=null && orderList.size()>0){
						lastOrderNumber = orderList.get(1).getOrderID();
						System.out.println(orderList.get(1).getOrderID() +" : "+ orderList.get(1).getTotal());
					}
					if(CommonUtility.validateString(lastOrderNumber).length()<=0){
						isValidUser=false;  result = result + "No orders found for entered account number, Please conatct ETNA Supply.|";
					}
					session.removeAttribute("userToken");
				}else{
					isValidUser=false;  result = result + "Please enter valid Account Number.|";
				}
			}
			
			if(isValidUser){
				result = result + "successfully|"+CommonUtility.validateString(lastOrderNumber);
			}
			renderContent = result;
			
			target = SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
		}
		return target;
	}
	

	public String RegisterNewContact(){
		result = "";	
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		int parentUserId = CommonUtility.validateNumber(sessionUserId);
		AddressModel userRegistrationDetail = new AddressModel();
		UserManagement usersObj = new UserManagementImpl();
		boolean isValidUser = true;
		
		//-- Get a paredt User id
		//Validate for Account number 
		//Validate for order Total
		// Failed # time send a mail or diaplay contact Etna Support
		try{
			int userId = CommonUtility.validateNumber(sessionUserId);
			int registerTryCount = 1;
			String lastOrderNumber = "";
			double lastOrderTotal = 0.0;
			if(accountNumber.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Account Number.|";}else{
				String userToken="";
				LoginSubmitManagement loginSubmit = new LoginSubmitManagementImpl();
				LoginSubmitManagementModel loginSubmitManagementModel = new LoginSubmitManagementModel();
				loginSubmitManagementModel.setUserName(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_OE_ADMIN_USER_NAME")));
				loginSubmitManagementModel.setPassword(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_OE_ADMIN_PASSWORD")));
				loginSubmitManagementModel.setActiveCustomerId(CommonUtility.validateString(accountNumber));
				userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
				if(CommonUtility.validateString(userToken).length()>0){
					session.setAttribute("userToken",userToken);
					SalesOrderManagement salesObj = new SalesOrderManagementImpl();
					SalesModel salesInputParameter = new SalesModel();
					
					Date today = new Date();
					Format formatter=new SimpleDateFormat("MM/dd/yyyy");
					Calendar cal = new GregorianCalendar();
					cal.setTime(today);
					cal.add(Calendar.DAY_OF_MONTH, -30);
					Date todayMinus30 = cal.getTime();
					String startDate = formatter.format(todayMinus30);
					String endDate = formatter.format(today);
					System.out.println("Start Date : "+formatter.format(todayMinus30)+" : End Date : "+formatter.format(today));
					salesInputParameter.setSession(session);
					salesInputParameter.setUserName(loginSubmitManagementModel.getUserName());
					salesInputParameter.setUserToken(loginSubmitManagementModel.getPassword());
					salesInputParameter.setEntityId(CommonUtility.validateString(accountNumber));
					salesInputParameter.setStartDate(startDate);
					salesInputParameter.setEndDate(endDate);
					ArrayList<SalesModel> orderList = salesObj.OrderHistory(salesInputParameter);
					
					//LinkedHashMap<String, Double> invoiceListDetails = new LinkedHashMap<String, Double>();
					if(orderList!=null && orderList.size()>0){
						lastOrderNumber = orderList.get(1).getOrderID();
						lastOrderTotal = orderList.get(1).getTotal();
						System.out.println(orderList.get(1).getOrderID() +" : "+ orderList.get(1).getTotal());
						/*for(SalesModel sModel : orderList){
							invoiceListDetails.put(sModel.getOrderID(), sModel.getTotal());
							System.out.println(sModel.getOrderID() +" : "+ sModel.getTotal());
						}*/
					}
					session.removeAttribute("userToken");
				}else{
					
					isValidUser=false;  result = result + "Please enter valid Account Number.|";
					
					/*if(session.getAttribute(CommonUtility.validateString(accountNumber))!=null){
						registerTryCount = registerTryCount+CommonUtility.validateNumber(session.getAttribute(CommonUtility.validateString(accountNumber)).toString());
					}
					session.setAttribute(CommonUtility.validateString(accountNumber), registerTryCount);*/
					
				}
			}
			if(invoiceNumber.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Order Number.|";}else{
				if(CommonUtility.validateString(lastOrderNumber).length()>0){
					if(!CommonUtility.validateString(lastOrderNumber).equalsIgnoreCase(CommonUtility.validateString(invoiceNumber))){
						isValidUser=false;  result = result + "Entered order number do not match.|";
					}
				}else{
					isValidUser=false;  result = result + "No orders found for entered account number, Please conatct ETNA Supply.|";
					if(session.getAttribute(CommonUtility.validateString(accountNumber))!=null){
						registerTryCount = registerTryCount+CommonUtility.validateNumber(session.getAttribute(CommonUtility.validateString(accountNumber)).toString());
					}
					session.setAttribute(CommonUtility.validateString(accountNumber), registerTryCount);
				}
			}
			if(invoiceTotal.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Order Total.|";}else{
				if(CommonUtility.validateDoubleNumber(invoiceTotal)!=lastOrderTotal){
					isValidUser=false;  result = result + "Please enter valid order total for the order number "+invoiceNumber+".|";
					
					if(session.getAttribute(CommonUtility.validateString(accountNumber))!=null){
						registerTryCount = registerTryCount+CommonUtility.validateNumber(session.getAttribute(CommonUtility.validateString(accountNumber)).toString());
					}
					session.setAttribute(CommonUtility.validateString(accountNumber), registerTryCount);
				}
			}
			//----------------------
			if(contactCompanyName.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Company Name.|";}
			if(contactFirstName.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter First Name.|";}
			if(contactLastName.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Last Name.|";}
			if(contactemailAddress.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Email Address.|";}else{
				if(!CommonUtility.validateEmail(contactemailAddress)){
					isValidUser=false; result = result + "Please enter valid email address.|";
				}else if(UsersDAO.isRegisteredUser(contactemailAddress)){
					isValidUser=false; result = result + "User Already registered with this email.|";
				}
			}
			if(contactPassword.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Password.|";}else{ 
				if(contactPassword.trim().length()<8){isValidUser=false; result = result + "Password should have at least 8 characters.|";}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
					if(!NewUserRegisterUtility.isAlfaNumericOnly(contactPassword)){
						isValidUser=false; result = result + "Special characters are not allowed and password can have alphabets and numbers.|";
					}
				}
			
			}
			if(contactPasswordConfirm.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Confirm Password.|";}
			if(!contactPassword.trim().equalsIgnoreCase("")){
				if(!contactPasswordConfirm.trim().equalsIgnoreCase("")){
					if(contactPassword.trim().compareTo(contactPasswordConfirm)!=0){
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
			if(contactZip.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Zipcode.|";}
			if(contactPhone.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter Phone Number.|";
			}else{
				if(!CommonUtility.validatePhoneNumber(contactPhone)){
					isValidUser=false; result = result + "Please enter valid Phone Number (e.g. 123-456-7896,(954) 555-1234, 4561237896).|";
				}else{
					contactPhone = contactPhone.replaceAll("[^0-9]", "");
				}
			}
			
			if(isValidUser){
				
				//if(parentUserId>1){
					
					//String entityId = (String) session.getAttribute("billingEntityId");
					String entityId = CommonUtility.validateString(accountNumber);
					
					isValidUser = false;
				    isValidUser = UsersDAO.isRegisteredUser(contactemailAddress);
					 
					 if(!isValidUser){
						 	userRegistrationDetail.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
						 	userRegistrationDetail.setFirstName(contactFirstName);
							userRegistrationDetail.setLastName(contactLastName);
							userRegistrationDetail.setEmailAddress(contactemailAddress);
							userRegistrationDetail.setUserPassword(contactPassword);
							userRegistrationDetail.setAddress1(contactAddress1);
							userRegistrationDetail.setAddress2(contactAddress2);
							userRegistrationDetail.setCity(contactCity);
							userRegistrationDetail.setState(contactState);
							userRegistrationDetail.setZipCode(contactZip);
							userRegistrationDetail.setCountry(contactCountry);
							userRegistrationDetail.setPhoneNo(contactPhone);
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
								}else{
									userRegistrationDetail.setRole("Ecomm Customer General User");
								}
							}
							userRegistrationDetail.setContactClassification(contactClassification);
							userRegistrationDetail.setUserStatus("Y");
							userRegistrationDetail.setFormtype("FirstTimeOrdering");
							userRegistrationDetail.setSession(session);
							userRegistrationDetail.setWebOEUserName(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_OE_ADMIN_USER_NAME")));
							userRegistrationDetail.setWebOEPassword(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_OE_ADMIN_PASSWORD")));
							userRegistrationDetail.setActiveCustomerId(""); //Set WEB_OE_ADMIN_USER_NAME_ENTITY ID
						 //result = usersObj.createRetailUser(userRegistrationDetail);
						 result = usersObj.createNewAgent(userRegistrationDetail);
						 userId = UsersDAO.getUserIdFromDB(contactemailAddress,"Y");
						 if(userId>0){
							 result = result+"|"+userId;
							 session.removeAttribute(CommonUtility.validateString(accountNumber));
						 }
						 
					 }else{
						 result = result + "User Already registered in this email. Please use different email.";
					 }
				 /*}else{
					 result = "Parent user not found, Unable to register user.";
				 }*/
			}else{
				
				if(registerTryCount>=3){
					
					String registerTryString = "We are unable to verify your identity at this time. An Etna representative will contact you within one business day to assist you with your registration.";
					String previousResult = result;
					result = registerTryString+"|"+previousResult;
					userRegistrationDetail.setFirstName(contactFirstName);
					userRegistrationDetail.setLastName(contactLastName);
					userRegistrationDetail.setEmailAddress(contactemailAddress);
					userRegistrationDetail.setEntityId(accountNumber);
					userRegistrationDetail.setPhoneNo(contactPhone);
					userRegistrationDetail.setCompanyName(contactCompanyName);
					
					SendMailUtility sendMailUtility = new SendMailUtility();
					
					boolean flagMail = sendMailUtility.sendRegistrationFailAttemptMail(userRegistrationDetail);
					if(flagMail){
						System.out.println(accountNumber+" : Failed Attempt mail sent");
					}else{
						System.out.println(accountNumber+" : Failed Attempt mail not sent");
					}
					//Send Email to ETNA Supply After 3 attempt 
					session.removeAttribute(CommonUtility.validateString(accountNumber));
				}
			}
			renderContent = result;
		}catch(Exception e){
			e.printStackTrace();
		}
		target = SUCCESS;
		return target;
	}
	
	public String commercialRegistrationSaveExit(){ 
	boolean isValidUser = true;
	request = ServletActionContext.getRequest();
	HttpSession session = request.getSession();
	int count=-1;
    result = "";
    renderContent = "";
    String customerType = "";
    UserManagement usersObj = new UserManagementImpl();
    HashMap<String,String> usersDetail = new HashMap<String,String>();
    AddressModel userRegistrationDetail = new AddressModel();
    File fileList[] = null;
	Enumeration files = null;
	String name = "";
	String[] fileToUpload = null;
	String savedFileName = "";
	String folder = "";
	String saveFile = "";
	String path = "";
	  SecureData validUserPass = new SecureData();
    try{		
			boolean userExist = false;
			if(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register.user.status")!=null && 
					userStatus!=null && userStatus.equalsIgnoreCase("partial") && CommonUtility.validateString(userStatus).equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register.user.status")) || userStatus.equalsIgnoreCase("SP")){
				isValidUser=true;
			}else{
				userExist = UsersDAO.checkForUserName(contactemailAddress);
				customerType = UsersDAO.checkForCustomerType(contactemailAddress);
				if(userExist && customerType!=null && !customerType.equalsIgnoreCase("G")){
					 isValidUser=false;
					 result = "0|"+ result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.emailAlreadyExists")+"|";
				 }
				else{
					if(customerType!=null && !customerType.equalsIgnoreCase("") && customerType.equalsIgnoreCase("G"))
					{	
						int buyingCompanyId = UsersDAO.getBuyingCompanyIdByUserName(contactemailAddress);
						UsersDAO.deleteExistingGuestUser(contactemailAddress);
						UsersDAO.deleteExistingCustomer(buyingCompanyId);
					}
				}
			}
			//Backend Validation for Registration
			if(CommonUtility.customServiceUtility() != null && (isValidUser || (result.contains(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.emailAlreadyExists"))))) {
				String validated_result = CommonUtility.customServiceUtility().validateUserRegistrationDetail(this);
				if(!CommonUtility.validateString(validated_result).isEmpty()) {
					/*for(String st:validated_result.split("\\|")) {
						System.out.println(st);
					}*/
					isValidUser = false;
					LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
					contentObject.put("serverValidationError", validated_result);
					renderContent = LayoutGenerator.templateLoader("RegisterPage", contentObject , null, null, null);
					return SUCCESS;
				}
			}
			if(isValidUser){
				
				if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y"))
				 {
					contactemailAddress = contactemailAddress.toLowerCase();
				 }
				UsersModel updateAddress = new UsersModel();
				AddressModel billingAddress = new AddressModel();
				AddressModel shipAddress = new AddressModel();
				 LinkedHashMap<String,String> userRegisteration=new  LinkedHashMap<String,String>();
				 userRegisteration.put("firstName2AB",contactFirstName);
				 userRegisteration.put("lastName2AB",contactLastName);
				 userRegisteration.put("emailAddress2AB",contactemailAddress);
				 userRegisteration.put("password2AB",contactPassword);
				 userRegisteration.put("contactClassification2A", contactClassification1B);
				 userRegisteration.put("contactPhone", contactPhone);
				 SaveCustomFormDetails saveForm = new SaveCustomFormDetails();
				 saveForm.saveToDataBase(userRegisteration,"RegisterationForm2A");
				 usersDetail=UsersDAO.getUserPasswordAndUserId(contactemailAddress,"P");
				 userRegistrationDetail.setFirstName(contactFirstName);
				 userRegistrationDetail.setLastName(contactLastName);
				 userRegistrationDetail.setEmailAddress(contactemailAddress);
				 userRegistrationDetail.setUserPassword(contactPassword);
				 userRegistrationDetail.setPhoneNo(contactPhone);
				 userRegistrationDetail.setCompanyName(companyName1B);
				 userRegistrationDetail.setContactClassification(contactClassification1B);
				 userRegistrationDetail.setCustomFieldData(declarationTitle);
				 //update data to module
				 updateAddress.setFirstName(contactFirstName);
				 updateAddress.setLastName(contactLastName);
				 updateAddress.setEmailAddress(contactemailAddress);
				 updateAddress.setUserName(contactemailAddress);
				 updateAddress.setPhoneNo(contactPhone);
				 updateAddress.setAddress1(contactAddress1);
				 updateAddress.setAddress2(contactAddress2!=null?contactAddress2:"");
				 updateAddress.setCity(cityName1B);
				 updateAddress.setZipCode(zipCode1B);
				 updateAddress.setState(stateName1B);
				 updateAddress.setCountry(countryName1B);
				 updateAddress.setChangedPassword(contactPassword);
				 updateAddress.setUserId((CommonUtility.validateNumber(usersDetail.get("userId"))));
				 updateAddress.setContactClassification(contactClassification1B);
				 billingAddress.setFirstName(contactFirstName);
				 billingAddress.setLastName(contactLastName);
				billingAddress.setAddress1(contactAddress1);
				billingAddress.setAddress2(contactAddress2!=null?contactAddress2:"");
				billingAddress.setCity(cityName1B);
				billingAddress.setZipCode(zipCode1B);
				billingAddress.setState(stateName1B);
				billingAddress.setCountry(countryName1B);
				billingAddress.setPhoneNo(phoneNo1B);
				billingAddress.setEmailAddress(emailAddress1B);
				billingAddress.setCompanyName(companyName1B);
				billingAddress.setAddressBookId(CommonUtility.validateNumber(usersDetail.get("defaultBillingAddressId")));
				userRegistrationDetail.setBillingAddress(billingAddress);
				updateAddress.setBillAddress(billingAddress);
				if(CommonUtility.validateString(checkSameAsBilling).equalsIgnoreCase("No")){	
					userRegistrationDetail.setCheckSameAsBilling(checkSameAsBilling);
					shipAddress.setFirstName(contactFirstName);
					shipAddress.setLastName(contactLastName);
/*					shipAddress.setFirstName(auShipCompanyName);
					shipAddress.setLastName(auShipFirstName);*/
					shipAddress.setAddress1(auShipAddress1);
					shipAddress.setAddress2(auShipAddress2);
					shipAddress.setCity(auShipCity);
					shipAddress.setState(auShipState);
					shipAddress.setCountry(auShipCountry);
					shipAddress.setCompanyName(auShipCompanyName);
					shipAddress.setEmailAddress(emailAddress2AB);
					shipAddress.setAddressBookId(CommonUtility.validateNumber(usersDetail.get("defaultShippingAddressId")));
					if(CommonUtility.validateString(auShipCountry).equalsIgnoreCase("USA")){
						shipAddress.setCountry(CommonUtility.getCountryCode(auShipCountry, "Registration"));
					}else{
						shipAddress.setCountry(auShipCountry);
					}
					shipAddress.setZipCode(auShipZipCode);
					shipAddress.setPhoneNo(auShipPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
				    
				}else{
					shipAddress = billingAddress;
				}
				userRegistrationDetail.setShippingAddress(shipAddress);
				updateAddress.setShipAddress(shipAddress);
				//--------
				if(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationRole")!=null && CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationRole").trim().length()>0){
					userRegistrationDetail.setRole(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationRole"));
				}else{
					userRegistrationDetail.setRole("Ecomm Customer Super User");
				}
				if(CommonUtility.validateString(disableSubmitPO).equalsIgnoreCase("Y") || CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NEW_COMMERCIAL_CUSTOMER_DISABLE_SUBMIT_PO")).equalsIgnoreCase("Y")){
					disableSubmitPO = "Y";
					userRegistrationDetail.setDisableSubmitPO(disableSubmitPO);
				}
				if(CommonUtility.validateString(disableSubmitPOCC).equalsIgnoreCase("Y")){
					userRegistrationDetail.setDisableSubmitPOCC(disableSubmitPOCC);
				}
				String mail="";
				userRegistrationDetail.setFormtype("2A");
				userRegistrationDetail.setUpdateRole(true);
				if(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationStatus")!=null && CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationStatus").trim().length()>0){
				 if(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register.user.status")!=null && 
						 CommonUtility.validateString(userStatus).length()>0 && !userStatus.isEmpty() && CommonUtility.validateString(userStatus).equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register.user.status"))){
					 updateAddress.setUserStatus(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationStatus"));
				}else if(CommonUtility.validateString(userStatus).equalsIgnoreCase("P")){
					userRegistrationDetail.setUserStatus("P");
				}else if(CommonUtility.validateString(userStatus).equalsIgnoreCase("SP")){
					userRegistrationDetail.setUserStatus("P");
					updateAddress.setUserStatus("P");
					userStatus="partial";
					mail="SP";
				}
				else{
					userRegistrationDetail.setUserStatus("I");
				}
			}
				userRegistrationDetail.setContactClassification(contactClassification2A);
				userRegistrationDetail.setTermsType(creditline);
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_NEW_CUSTOMER_REGISTRATION_IN_ERP")).equalsIgnoreCase("Y")){
					
						UsersModel userAddress = new UsersModel();
						userAddress.setFirstName(userRegistrationDetail.getFirstName());
						userAddress.setLastName(userRegistrationDetail.getLastName());
						if(userRegistrationDetail.getCompanyName()!=null && !userRegistrationDetail.getCompanyName().trim().equalsIgnoreCase("")){
							userAddress.setEntityName(userRegistrationDetail.getCompanyName());
						}else{
							userAddress.setEntityName(userRegistrationDetail.getFirstName() +" "+userRegistrationDetail.getLastName());
						}
						userAddress.setAddress1(userRegistrationDetail.getAddress1());
						userAddress.setAddress2(userRegistrationDetail.getAddress2());
						userAddress.setCity(userRegistrationDetail.getCity());
						userAddress.setState(userRegistrationDetail.getState());
						userAddress.setZipCode(userRegistrationDetail.getZipCode());
						  if(userRegistrationDetail.getCountry().equalsIgnoreCase("USA")){
							  userAddress.setCountry(userRegistrationDetail.getCountry());
						  }else{
							  if(userRegistrationDetail.getCountry()!=null && userRegistrationDetail.getCountry().trim().length()>0){
								  userAddress.setCountry(userRegistrationDetail.getCountry());
							  }else{
								  userAddress.setCountry("USA");  
							  }
						  }
						  userAddress.setPhoneNo(userRegistrationDetail.getPhoneNo().replaceAll("[^a-zA-Z0-9]", ""));
						  userAddress.setEmailAddress(userRegistrationDetail.getEmailAddress());
						  userAddress.setNewsLetterSub(newsLetterSub2AB);
						  userAddress.setPassword(validUserPass.validatePassword(userRegistrationDetail.getUserPassword()));
						
					  SendMailUtility sendMailUtility = new SendMailUtility();
					  
					  boolean sentFlag = false;
					  if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_MAIL_TO_CUSTOMER")).equalsIgnoreCase("Y")){
						  sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"webUser","",userRegistrationDetail.getFormtype()); //"2B"
					  }
					  if(CommonUtility.validateString(userRegistrationDetail.getFormtype()).trim().equalsIgnoreCase("2A")){
						   sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"customer","",userRegistrationDetail.getFormtype()); //"2B"
					  }
					  result = "1|Registration request sent successfully";
					
				}else{
					usersDetail=UsersDAO.getUserPasswordAndUserId(contactemailAddress,"P");
					if(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register.user.status")!=null  && userStatus!=null && CommonUtility.validateString(userStatus).equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register.user.status"))){
						//update address
					
						count = UsersDAO.updateUserData(CommonUtility.validateNumber(usersDetail.get("userId")),updateAddress);
						 count = UsersDAO.updateBCAddressBookBS(updateAddress.getBillAddress());
						 count = UsersDAO.updateBCAddressBookBS(updateAddress.getShipAddress());
						 if(mail.equalsIgnoreCase("SP")){
							 result = "Registration partially submitted successfully";
						 }else{
							 result = "Registration request sent successfully for review.";
						 } 
					}else{
						result = usersObj.createCommertialCustomer(userRegistrationDetail);
						usersDetail=UsersDAO.getUserPasswordAndUserId(contactemailAddress,userRegistrationDetail.getUserStatus());
						UsersDAO.insertCustomField("Y", "APPROVAL_STATUS", CommonUtility.validateNumber(usersDetail.get("userId")), CommonUtility.validateNumber(usersDetail.get("defaultShippingAddressId")), CustomFieldUtility.bcAddressBookCustomFieldType);
						if(userRegistrationDetail.getUserStatus().equalsIgnoreCase("I")){
							result = "Registration request sent successfully for review.";
						}else{
							result = "Registration partially submitted successfully";
						}		
					}
					// file Upload
					ArrayList sendMailFileName = new ArrayList();
					ArrayList sendMailFolderPath = new ArrayList();
					String [] filesToBeUploaded = businessTyp.split("/");
					MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper)ServletActionContext.getRequest();
					files = multiWrapper.getFileParameterNames();
					for(String fileSave : filesToBeUploaded){
				      fileList = multiWrapper.getFiles(fileSave);
				      if(fileList!= null &&  fileList.length >0){
				       name = (String) files.nextElement();
				       fileToUpload = multiWrapper.getFileNames(name);
				       savedFileName = CommonUtility.validateString(fileToUpload[0]+"_"+CommonUtility.validateNumber(usersDetail.get("userId")));
				       folder = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH"));
				       saveFile = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH"))+"/"+savedFileName;
				       path = CommonUtility.validateString(folder);
				       sendMailFileName.add(savedFileName);
				       sendMailFolderPath.add(saveFile);
				      File destination = new File(path);
				      if(!destination.exists()){
				       System.out.println(path+" : destination dir doesnt exists");
				       destination.mkdir();
				      }
				      UsersDAO.copyFile(fileList[0], new File(saveFile), session);
				      System.out.println("File "+savedFileName+" is Uploaded");
				      }
					}
					
					if(declarationTitle != null && declarationTitle.length() > 0){
						userCustomField = declarationTitle.split("~");
						for (int i = 0; i < userCustomField.length; i++) {
							String[] fieldNameAndValue = userCustomField[i].split("-");
							if(fieldNameAndValue!=null && fieldNameAndValue.length > 1){
								String customFiledValue = fieldNameAndValue[1];
								String fieldName =fieldNameAndValue[0];
								 if(CommonUtility.validateString(fieldName).length()>0 && fieldName.contains("BILLTO")){
										UsersDAO.insertCustomField(customFiledValue, fieldName, CommonUtility.validateNumber(usersDetail.get("userId")), CommonUtility.validateNumber(usersDetail.get("buyingCompanyId")), CustomFieldUtility.buyingCompanyCustomFieldType);
									}
								 else if(CommonUtility.validateString(fieldName).length()>0 && fieldName.contains("SHIPTO")){
										UsersDAO.insertCustomField(customFiledValue, fieldName, CommonUtility.validateNumber(usersDetail.get("userId")), CommonUtility.validateNumber(usersDetail.get("defaultShippingAddressId")), CustomFieldUtility.bcAddressBookCustomFieldType);
									}else{
										UsersDAO.insertCustomField(customFiledValue, fieldName, CommonUtility.validateNumber(usersDetail.get("userId")), CommonUtility.validateNumber(usersDetail.get("buyingCompanyId")), CustomFieldUtility.userCustomFieldType);
									}
							}
						}
					}
					System.out.println("OOk");
					if(CommonUtility.validateString(result).length()>0 && result.toLowerCase().contains("successfully")){
						
						UsersModel userAddress = new UsersModel();
						userAddress.setFirstName(userRegistrationDetail.getFirstName());
						userAddress.setLastName(userRegistrationDetail.getLastName());
						if(userRegistrationDetail.getCompanyName()!=null && !userRegistrationDetail.getCompanyName().trim().equalsIgnoreCase("")){
							userAddress.setEntityName(userRegistrationDetail.getCompanyName());
						}else{
							userAddress.setEntityName(userRegistrationDetail.getFirstName() +" "+userRegistrationDetail.getLastName());
						}
						userAddress.setAddress1(billingAddress.getAddress1());
						userAddress.setAddress2(billingAddress.getAddress2());
						userAddress.setCity(billingAddress.getCity());
						userAddress.setState(billingAddress.getState());
						userAddress.setZipCode(billingAddress.getZipCode());
						  if(billingAddress.getCountry().equalsIgnoreCase("USA")){
							  userAddress.setCountry(billingAddress.getCountry());
						  }else{
							  if(billingAddress.getCountry()!=null && billingAddress.getCountry().trim().length()>0){
								  userAddress.setCountry(billingAddress.getCountry());
							  }else{
								  userAddress.setCountry("USA");  
							  }
						  }
						  userAddress.setPhoneNo(userRegistrationDetail.getBillingAddress().getPhoneNo().replaceAll("[^a-zA-Z0-9]", ""));
						  userAddress.setEmailAddress(userRegistrationDetail.getEmailAddress());
						  userAddress.setNewsLetterSub(newsLetterSub2AB);
						  userAddress.setPassword(userRegistrationDetail.getUserPassword());
						  userAddress.setUserStatus(userRegistrationDetail.getUserStatus());
						  userAddress.setContactIdList(sendMailFolderPath);
						  userAddress.setContactDescriptionList(sendMailFileName);
						  userAddress.setCompanyName(contactCompanyName);
						  userAddress.setContactClassification(contactClassification1B);

						
					  SendMailUtility sendMailUtility = new SendMailUtility();
					  
					  boolean sentFlag = false;
					  if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_MAIL_TO_CUSTOMER")).equalsIgnoreCase("Y")){
						  sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"webUser","",userRegistrationDetail.getFormtype()); //"2B"
					  }
					  if(CommonUtility.validateString(userRegistrationDetail.getFormtype()).trim().equalsIgnoreCase("2A")){
						   sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"customer","",userRegistrationDetail.getFormtype()); //"2B"
					  }
					
				}
					if(CommonUtility.validateString(result).length()>0 && result.toLowerCase().contains("successfully")){
						 String constantContact = CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_ENABLE");
		                 if(CommonUtility.validateString(constantContact).length()>0 && constantContact.trim().equalsIgnoreCase("Y")){
		                	 UsersModel userDetails = new UsersModel();
		                     userDetails.setFirstName(firstName2AB);
		                     userDetails.setLastName(lastName2AB);
		                     userDetails.setCompanyName(companyName2AB);
		                     userDetails.setEmailAddress(emailAddress2AB);
		                     String response = UsersDAO.constantContactOne(userDetails);
		                     System.out.println("Constant Contact Response:CommertialCustomerRegistration: "+response);
		                 }
					 }
					
				}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_SUCCESS_PAGE")).equalsIgnoreCase("Y")){
					LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();	
				contentObject.put("Result", result);
				renderContent = LayoutGenerator.templateLoader("RegistrationSuccess", contentObject , null, null, null);
				}else{
					renderContent=result;
				}
			}else{
				renderContent=result;
			}

    }catch (Exception e) {
		 e.printStackTrace();
	}
	return SUCCESS;
}
	
	public String RetailCustomerRegistration(){

		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		result = "";
		target = SUCCESS;
		AddressModel userRegistrationDetail = new AddressModel();
		boolean isValidUser = true;
		boolean updateAdhocUser = false;
		request = ServletActionContext.getRequest();
		renderContent = "";
		result = "";
		HashMap<String,String> adhocUserDetails = new HashMap<String,String>();
		UserManagement usersObj = new UserManagementImpl();

		try{
			if(UsersDAO.isRegisteredUser(emailAddress2AB)){
				adhocUserDetails=UsersDAO.getUserPasswordAndUserId(emailAddress2AB,"Y");
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")).length()>0){
					if(CommonUtility.validateString(adhocUserDetails.get("password")).equals(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"))){
						isValidUser=true;
						if(CommonUtility.validateString(password2AB).length()>0 && CommonUtility.validateString(confirmPassword2AB).length()>0){
							if(password2AB.trim().compareTo(confirmPassword2AB)==0){
								updateAdhocUser = true;
								String entityId="0";
								String customerType="";								
								customerType = CommonUtility.validateString(UsersDAO.checkForCustomerType(emailAddress2AB));
							 	entityId = UsersDAO.checkForAnonymous(emailAddress2AB);
							 	if((CommonUtility.validateString(entityId).length()>0 && entityId.equals(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID")))) || (CommonUtility.validateString(entityId).length()>0 && customerType.equalsIgnoreCase("G"))){
							 		int count =UsersDAO.deleteExistingGuestUser(emailAddress2AB);
									if(count>0) {
										updateAdhocUser = false;
									}
								}else {
									UsersModel userUpdateDetail = new  UsersModel();
									userUpdateDetail.setFirstName(firstName2AB);
									userUpdateDetail.setLastName(lastName2AB);
									userUpdateDetail.setCompanyName(companyName2AB);
									userUpdateDetail.setEmailAddress(emailAddress2AB);
									userUpdateDetail.setAddress1(billingAddress2AB);
									userUpdateDetail.setAddress2(suiteNo2AB);
									userUpdateDetail.setCity(cityName2AB);
									userUpdateDetail.setState(stateName2AB);
									userUpdateDetail.setZipCode(zipCode2AB);
									userUpdateDetail.setCountry(countryName2AB);
									userUpdateDetail.setPhoneNo(phoneNo2AB.replaceAll("[^a-zA-Z0-9]", ""));
									userUpdateDetail.setUserName(emailAddress2AB);
									userUpdateDetail.setUserId(CommonUtility.validateNumber(adhocUserDetails.get("userId")));
									if(CommonUtility.validateString(userUpdateDetail.getCompanyName()).length()>0){
									userUpdateDetail.setEntityName(userUpdateDetail.getCompanyName());
									}else{
									       userUpdateDetail.setEntityName(userUpdateDetail.getFirstName() +" "+userUpdateDetail.getLastName());
									}
									userUpdateDetail.setCustomerType("C");
									userUpdateDetail.setSession(session);
									userUpdateDetail.setChangedPassword(password2AB);
									userUpdateDetail.setUserStatus("Y");
									UsersDAO.updateUserData(CommonUtility.validateNumber(adhocUserDetails.get("userId")),userUpdateDetail);
									UsersDAO.updateBCAddressBook(CommonUtility.validateString(adhocUserDetails.get("defaultShippingAddressId")),userUpdateDetail);
									UsersDAO.updateBCAddressBook(CommonUtility.validateString(adhocUserDetails.get("defaultBillingAddressId")),userUpdateDetail);
									UsersDAO.updateBuyingCompany(CommonUtility.validateString(adhocUserDetails.get("buyingCompanyId")),userUpdateDetail);
								}
							}
						}
					}else{
						isValidUser=false; result = result + "0|"+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.userAlreadyRegistered")+"|";
					}
				}else{
					isValidUser=false; result = result + "0|"+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.userAlreadyRegistered")+"|";
				}
			}
/*
			if(firstName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.firstName")+"|";}
			if(lastName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.lastName")+"|";}
			if(checkJobTitle!=null && checkJobTitle.trim().equalsIgnoreCase("N")){}
			else {
			if(request.getParameterMap().containsKey("jobTitle2AB") && (jobTitle2AB==null || jobTitle2AB.trim().equalsIgnoreCase(""))){
				isValidUser=false; result = result+ "Please enter "+SendMailUtility.propertyLoader("form.label.JobTitle","")+"|";
			}
			}

			if(checkCompanyName!=null && checkCompanyName.trim().equalsIgnoreCase("N")){

			}else{
				if(companyName2AB==null || companyName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.companyName")+"|";}
			}
			if(emailAddress2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.emailAddress")+"|";
			}else{
				if(!CommonUtility.validateEmail(emailAddress2AB)){
					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.vaildEmailAddress")+"|";
				}if(UsersDAO.isRegisteredInactiveUser(emailAddress2AB)){
					isValidUser=false; result = result + "0|"+ LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.userAlreadyRegisteredInactive")+"|";
				}else if(UsersDAO.isRegisteredUser(emailAddress2AB)){
						adhocUserDetails=UsersDAO.getUserPasswordAndUserId(emailAddress2AB,"Y");
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")).length()>0){
							if(CommonUtility.validateString(adhocUserDetails.get("password")).equals(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"))){
								isValidUser=true;
								if(CommonUtility.validateString(password2AB).length()>0 && CommonUtility.validateString(confirmPassword2AB).length()>0){
									if(password2AB.trim().compareTo(confirmPassword2AB)==0){
										UsersDAO.updatePassword(CommonUtility.validateNumber(adhocUserDetails.get("userId")),password2AB);
										updateAdhocUser = true;
									}
								}
	
							}else{
								isValidUser=false; result = result + "0|"+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.userAlreadyRegistered")+"|";
							}
						}else{
							isValidUser=false; result = result + "0|"+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.userAlreadyRegistered")+"|";
						}
				}
			}
			if(password2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.password")+"|";}
			else{ 
				int pwdLength =8;
				if(CommonUtility.validateNumber((CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PASSWORD_MINIMUM_CHARECTERS"))))>4){
					pwdLength = CommonUtility.validateNumber((CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PASSWORD_MINIMUM_CHARECTERS"))));
				}
				if(password2AB.trim().length()<pwdLength){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.passwordMinimunCharacters")+"|";}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
					if(!isAlfaNumericOnly(password2AB)){
						isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.passwordCharacters")+"|";
					}
				}

			}
			if(confirmPassword2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.confirmPassword")+"|";}
			if(!password2AB.trim().equalsIgnoreCase("")){

				if(!confirmPassword2AB.trim().equalsIgnoreCase("")){

					if(password2AB.trim().compareTo(confirmPassword2AB)!=0){
						isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.passwordMismatch")+"|";
					}
				}
			}
			//if(companyName2AB.trim().equalsIgnoreCase("")){ isValidUser=false; result = result + "Please enter Company Name.|";}//if(company.trim().equalsIgnoreCase("")){company=username;}
			if(billingAddress2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter "+SendMailUtility.propertyLoader("register.label.address1","")+".|";}
			if(cityName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.enterCity")+"|";}
			if(countryName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.selectCountry")+"|";}
			if(stateName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.selectState")+"|";}
			if(request.getParameterMap().containsKey("zipCode2AB")){
				if(zipCode2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + SendMailUtility.propertyLoader("register.label.zip","")+"|";}	
			}else{
				zipCode2AB = "";
			}

			if(phoneNo2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + SendMailUtility.propertyLoader("register.label.phonenumber","")+"|";
			}else{
				if(!CommonUtility.validatePhoneNumber(phoneNo2AB)){
					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.validPhoneNumber")+"|";
				}else if(CommonUtility.validateString(checkPhoneFomat).length()>0&& checkPhoneFomat.trim().equalsIgnoreCase("N")){
					phoneNo2AB = phoneNo2AB.replaceAll("[^0-9]", "");
					phoneNo2AB = phoneNo2AB.substring(0,3)+"-"+phoneNo2AB.substring(3,6)+"-"+phoneNo2AB.substring(6);
					System.out.println(phoneNo2AB);
				}else{
					phoneNo2AB = phoneNo2AB.replaceAll("[^0-9]", "");
				}
			}

			if(request.getParameterMap().containsKey("fax2AB") && CommonUtility.validateString(fax2AB).length()>0){
				if(!CommonUtility.validatePhoneNumber(fax2AB)){
					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.faxNumber")+"|";
				}else{
					fax2AB = fax2AB.replaceAll("[^0-9]", "");
				}
			}
*/

			if(CommonUtility.customServiceUtility()!=null && isValidUser) {
				isValidUser = CommonUtility.customServiceUtility().validateEmailAddress(isValidUser, emailAddress2AB);
				if(!isValidUser) {
					result = result + "0|" + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.validEmailAddress")+"|";
				}
			 }
			
			if(isValidUser && !updateAdhocUser)
			{
				if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y"))
				{
					emailAddress2AB = emailAddress2AB.toLowerCase();
				}

				LinkedHashMap<String,String> userRegisteration=new  LinkedHashMap<String,String>();
				userRegisteration.put("firstName2AB",firstName2AB);
				userRegisteration.put("lastName2AB",lastName2AB);
				userRegisteration.put("companyName2AB",companyName2AB);
				userRegisteration.put("emailAddress2AB",emailAddress2AB);
				//userRegisteration.put("emailAddress2AB",emailAddress2AB.toLowerCase());
				userRegisteration.put("password2AB",password2AB);
				userRegisteration.put("billingAddress2AB",billingAddress2AB);
				userRegisteration.put("cityName2AB",cityName2AB);
				userRegisteration.put("stateName2AB",stateName2AB);
				userRegisteration.put("zipCode2AB",zipCode2AB);
				userRegisteration.put("countryName2AB",countryName2AB);
				userRegisteration.put("phoneNo2AB",phoneNo2AB);
				userRegisteration.put("newsLetterSub2AB",newsLetterSub2AB);
				userRegisteration.put("contactClassification2B", contactClassification2B);
				if(request.getParameterMap().containsKey("jobTitle2AB") && (jobTitle2AB!=null || !jobTitle2AB.trim().equalsIgnoreCase(""))){
					userRegisteration.put("jobTitle2AB", jobTitle2AB);
				}
				if(request.getParameterMap().containsKey("subsetFlag") && (subsetFlag!=null || !subsetFlag.trim().equalsIgnoreCase(""))){
					userRegisteration.put("subsetFlag", subsetFlag);
				}
				 if(CommonUtility.customServiceUtility()!=null) {
					 CommonUtility.customServiceUtility().setUserInformation(others,salesPersonName,howDidYouSelect,userRegisteration);
				 }
				SaveCustomFormDetails saveForm = new SaveCustomFormDetails();
				saveForm.saveToDataBase(userRegisteration,"RegisterationForm2B"); // Change to Actual account reg Typ
				AddressModel shipAddress = new AddressModel();
				userRegistrationDetail.setErpOverrideFlag(CommonUtility.validateString(request.getParameter("erpOverrideFlag")));
				userRegistrationDetail.setFirstName(firstName2AB);
				userRegistrationDetail.setLastName(lastName2AB);
				userRegistrationDetail.setCompanyName(companyName2AB);
				userRegistrationDetail.setEmailAddress(emailAddress2AB);
				userRegistrationDetail.setUserPassword(password2AB);
				userRegistrationDetail.setAddress1(billingAddress2AB);
				userRegistrationDetail.setAddress2(suiteNo2AB);
				userRegistrationDetail.setCity(cityName2AB);
				userRegistrationDetail.setState(stateName2AB);
				userRegistrationDetail.setZipCode(zipCode2AB);
				userRegistrationDetail.setCountry(countryName2AB);
				userRegistrationDetail.setPhoneNo(phoneNo2AB.replaceAll("[^a-zA-Z0-9]", ""));
				userRegistrationDetail.setFaxNumber(fax2AB);
				userRegistrationDetail.setLocUser(locUser);
				userRegistrationDetail.setRole("Ecomm Retail User");
				userRegistrationDetail.setTermsType(creditline);
				userRegistrationDetail.setIsTaxable(istaxable);
				userRegistrationDetail.setBirthMonth(CommonUtility.validateString(birthMonth));
				userRegistrationDetail.setIsRewardMember(CommonUtility.validateString(isRewardMember));
				if(CommonUtility.validateString(creditApplicationAccount2AB).equalsIgnoreCase("Y")){
					userRegistrationDetail.setCreditApplicationRequest(creditApplicationAccount2AB);
				}
				userRegistrationDetail.setCreditDate(creditDate);
				userRegistrationDetail.setContactWebsite(CommonUtility.validateString(contactWebsite));
				userRegistrationDetail.setContactTitle(CommonUtility.validateString(contactTitle));				
				if(CommonUtility.validateString(checkSameAsBilling).equalsIgnoreCase("No")){	
					//userRegistrationDetail.setCheckSameAsBilling(checkSameAsBilling);
					shipAddress.setFirstName(firstName2AB);
					shipAddress.setLastName(lastName2AB);
					shipAddress.setAddress1(shipAddress1);
					shipAddress.setAddress2(shipAddress2);
					shipAddress.setCity(shipCity);
					shipAddress.setState(shipState);
					shipAddress.setCountry(shipCountry);
					if(CommonUtility.validateString(shipCountry).equalsIgnoreCase("USA")){
						shipAddress.setCountry(CommonUtility.getCountryCode(shipCountry, "Registration"));
					}else{
						shipAddress.setCountry(shipCountry);
					}
					shipAddress.setZipCode(shipZipCode);
					shipAddress.setPhoneNo(shipPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
					shipAddress.setEmailAddress(emailAddress2AB);
				    userRegistrationDetail.setShippingAddress(shipAddress);
				}
				userRegistrationDetail.setCheckSameAsBilling(checkSameAsBilling);
				if(request.getParameterMap().containsKey("subsetFlag") && (subsetFlag!=null || !subsetFlag.trim().equalsIgnoreCase(""))){
					userRegistrationDetail.setSubsetFlag(subsetFlag);
				}
				 if(CommonUtility.customServiceUtility()!=null) {
					 CommonUtility.customServiceUtility().userRegistrationInformation(others,salesPersonName,howDidYouSelect,userRegistrationDetail);
				 }
				if(CommonDBQuery.getSystemParamtersList().get("DISABLE_RETAIL_ERP_REGISTRATION")!=null && CommonDBQuery.getSystemParamtersList().get("DISABLE_RETAIL_ERP_REGISTRATION").trim().equalsIgnoreCase("Y"))
				{
					userRegistrationDetail.setAnonymousUser(anonymousUser);
					userRegistrationDetail.setBuyingComanyIdStr(String.valueOf(UsersDAO.getBuyingCompanyIdByEntityId(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID"))));
					if(CommonUtility.customServiceUtility()!=null) { 	
							CommonUtility.customServiceUtility().CustomizedBuyingCompany(userRegistrationDetail);
					}
				}
				if(fromForm!=null && fromForm.trim().equalsIgnoreCase("secondA")){
					userRegistrationDetail.setFormtype("2A");
				}else{
					userRegistrationDetail.setFormtype("2B");
				}
				if(CommonUtility.validateString(disableSubmitPO).equalsIgnoreCase("Y")){
					userRegistrationDetail.setDisableSubmitPO(disableSubmitPO);
				}
				if(CommonUtility.validateString(disableSubmitPOCC).equalsIgnoreCase("Y")){
					userRegistrationDetail.setDisableSubmitPOCC(disableSubmitPOCC);
				}
				userRegistrationDetail.setUpdateRole(true);
				if(CommonUtility.validateString(userStatus).length()>0){
					userRegistrationDetail.setUserStatus(userStatus);
				}else {
					userRegistrationDetail.setUserStatus("Y");
				}
				userRegistrationDetail.setNewsLetterSub(newsLetterSub2AB);
				userRegistrationDetail.setContactClassification(contactClassification2B);
				if(request.getParameterMap().containsKey("jobTitle2AB") && (jobTitle2AB!=null || !jobTitle2AB.trim().equalsIgnoreCase(""))){
					userRegistrationDetail.setJobTitle(jobTitle2AB);
				}			

				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_BRONTO_USER_REGISTRATION")).equalsIgnoreCase("Y")){
					BrontoModel brontoModel = new BrontoModel();
					brontoModel.setFirstName(firstName2AB);
					brontoModel.setLastName(lastName2AB);
					brontoModel.setCompany(companyName2AB);
					brontoModel.setEmail(emailAddress2AB);
					brontoModel.setAddress1(billingAddress2AB);
					brontoModel.setAddress2(suiteNo2AB);
					brontoModel.setCity(cityName2AB);
					brontoModel.setState(stateName2AB);
					brontoModel.setZipcode(zipCode2AB);
					brontoModel.setCountry(countryName2AB);
					brontoModel.setPhone(phoneNo2AB);
					brontoModel.setFax(fax2AB);

					if(CommonUtility.validateString(newsLetterSub2AB).equalsIgnoreCase("on")){
						brontoModel.setRecieveEmailOffers(true);
					}else{
						brontoModel.setRecieveEmailOffers(false);
					}

					if(userCustomField != null && userCustomField.length > 0){
						for (int i = 0; i < userCustomField.length; i++) {
							String[] value = userCustomField[i].split("\\|");
							if(value.length > 1){
								if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.userName"))){
									brontoModel.setUserName(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.securityQuestion"))){
									brontoModel.setSecurityQuestion(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.securityAnswer"))){
									brontoModel.setSecurityAnswer(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.language"))){
									brontoModel.setLanguage(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.taxId"))){
									brontoModel.setTaxId(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.businessType"))){
									brontoModel.setBusinessType(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.businessTypeOther"))){
									brontoModel.setBusinessTypeOther(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.howDidYouHearAboutUs"))){
									brontoModel.setHowDidYouHearAboutUs(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.brandsCommonlyPurchased"))){
									brontoModel.setBrandsCommonlyPurchased(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.brandsCommonlyPurchasedOthers"))){
									brontoModel.setBrandsCommonlyPurchasedOthers(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.annualPartsPurchase"))){
									brontoModel.setAnnualPartsPurchase(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.commercialDiscount"))){
									brontoModel.setInterestedInCommercialDiscounts(Boolean.parseBoolean(value[0]));
								}
							}
						}
					}

					userRegistrationDetail.setBrontoDetails(brontoModel);
				}

				if(userCustomField != null && userCustomField.length > 0){
					userRegistrationDetail.setUserCustomFields(userCustomField);
				}

				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_NEW_RETAIL_CUSTOMER_REGISTRATION_IN_ERP")).equalsIgnoreCase("Y")){

					UsersModel userAddress = new UsersModel();
					userAddress.setFirstName(userRegistrationDetail.getFirstName());
					userAddress.setLastName(userRegistrationDetail.getLastName());
					if(userRegistrationDetail.getCompanyName()!=null && !userRegistrationDetail.getCompanyName().trim().equalsIgnoreCase("")){
						userAddress.setEntityName(userRegistrationDetail.getCompanyName());
					}else{
						userAddress.setEntityName(userRegistrationDetail.getFirstName() +" "+userRegistrationDetail.getLastName());
					}
					userAddress.setAddress1(userRegistrationDetail.getAddress1());
					userAddress.setAddress2(userRegistrationDetail.getAddress2());
					userAddress.setCity(userRegistrationDetail.getCity());
					userAddress.setState(userRegistrationDetail.getState());
					userAddress.setZipCode(userRegistrationDetail.getZipCode());
					if(userRegistrationDetail.getCountry().equalsIgnoreCase("USA")){
						userAddress.setCountry(userRegistrationDetail.getCountry());
					}else{
						if(userRegistrationDetail.getCountry()!=null && userRegistrationDetail.getCountry().trim().length()>0){
							userAddress.setCountry(userRegistrationDetail.getCountry());
						}else{
							userAddress.setCountry("USA");  
						}
					}
					userAddress.setPhoneNo(userRegistrationDetail.getPhoneNo().replaceAll("[^a-zA-Z0-9]", ""));
					userAddress.setEmailAddress(userRegistrationDetail.getEmailAddress());
					userAddress.setNewsLetterSub(newsLetterSub2AB);
					userAddress.setPassword(userRegistrationDetail.getUserPassword());
					 if(CommonUtility.customServiceUtility()!=null) {
						 CommonUtility.customServiceUtility().setUserDetails(others,salesPersonName,howDidYouSelect,userAddress);
					 }
					SendMailUtility sendMailUtility = new SendMailUtility();

					boolean sentFlag = false;
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_MAIL_TO_CUSTOMER")).equalsIgnoreCase("Y")){
						sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"webUser","",userRegistrationDetail.getFormtype()); //"2B"
					}
					if(CommonUtility.validateString(userRegistrationDetail.getFormtype()).trim().equalsIgnoreCase("2B")){
						sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"customer","",userRegistrationDetail.getFormtype()); //"2B"
					}
					result = "1|Registration request sent successfully";

				}else{
					result = usersObj.createRetailUser(userRegistrationDetail);
					if(CommonUtility.validateString(result).length()>0 && result.toLowerCase().contains("successfully")){
						String contactConstant = CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_ENABLE");
						if(CommonUtility.validateString(contactConstant).length()>0 && contactConstant.trim().equalsIgnoreCase("Y")){
							UsersModel userDetails = new UsersModel();
							userDetails.setFirstName(firstName2AB);
							userDetails.setLastName(lastName2AB);
							userDetails.setCompanyName(companyName2AB);
							userDetails.setEmailAddress(emailAddress2AB);
							String response = UsersDAO.constantContactOne(userDetails);
							System.out.println("Constant Contact Response:RetailCustomerRegistration: "+response);
						}
					}
				}
			}
			if(isValidUser && updateAdhocUser){
				if(userCustomField != null && userCustomField.length > 0){
					for (int i = 0; i < userCustomField.length; i++) {
						String[] fieldNameAndValue = userCustomField[i].split("\\|");
						if(fieldNameAndValue!=null && fieldNameAndValue.length > 1){
							String customFiledValue = fieldNameAndValue[0];
							String fieldName =fieldNameAndValue[1];
							if(fieldNameAndValue != null && fieldNameAndValue.length > 1){
								UsersDAO.insertCustomField(customFiledValue, fieldName, CommonUtility.validateNumber(adhocUserDetails.get("userId")), 0, CustomFieldUtility.userCustomFieldType); // Buying company can be zero because updating users custom field
							}
						}
					}
				}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_BRONTO_USER_REGISTRATION")).equalsIgnoreCase("Y")){
					BrontoModel brontoModel = new BrontoModel();
					brontoModel.setFirstName(firstName2AB);
					brontoModel.setLastName(lastName2AB);
					brontoModel.setCompany(companyName2AB);
					brontoModel.setEmail(emailAddress2AB);
					brontoModel.setAddress1(billingAddress2AB);
					brontoModel.setAddress2(suiteNo2AB);
					brontoModel.setCity(cityName2AB);
					brontoModel.setState(stateName2AB);
					brontoModel.setZipcode(zipCode2AB);
					brontoModel.setCountry(countryName2AB);
					brontoModel.setPhone(phoneNo2AB);
					brontoModel.setFax(fax2AB);

					if(CommonUtility.validateString(newsLetterSub2AB).equalsIgnoreCase("on")){
						brontoModel.setRecieveEmailOffers(true);
					}else{
						brontoModel.setRecieveEmailOffers(false);
					}

					if(userCustomField != null && userCustomField.length > 0){
						for (int i = 0; i < userCustomField.length; i++) {
							String[] value = userCustomField[i].split("\\|");
							if(value.length > 1){
								if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.userName"))){
									brontoModel.setUserName(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.securityQuestion"))){
									brontoModel.setSecurityQuestion(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.securityAnswer"))){
									brontoModel.setSecurityAnswer(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.language"))){
									brontoModel.setLanguage(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.taxId"))){
									brontoModel.setTaxId(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.businessType"))){
									brontoModel.setBusinessType(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.businessTypeOther"))){
									brontoModel.setBusinessTypeOther(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.howDidYouHearAboutUs"))){
									brontoModel.setHowDidYouHearAboutUs(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.brandsCommonlyPurchased"))){
									brontoModel.setBrandsCommonlyPurchased(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.brandsCommonlyPurchasedOthers"))){
									brontoModel.setBrandsCommonlyPurchasedOthers(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.annualPartsPurchase"))){
									brontoModel.setAnnualPartsPurchase(value[0]);
								}else if(value[1].equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.field.commercialDiscount"))){
									brontoModel.setInterestedInCommercialDiscounts(Boolean.parseBoolean(value[0]));
								}
							}
						}
					}

					userRegistrationDetail.setBrontoDetails(brontoModel);
				}
				UsersModel userDetail = new  UsersModel();
				userDetail.setFirstName(firstName2AB);
				userDetail.setLastName(lastName2AB);
				userDetail.setCompanyName(companyName2AB);
				userDetail.setEmailAddress(emailAddress2AB);
				userDetail.setPassword(password2AB);
				userDetail.setAddress1(billingAddress2AB);
				userDetail.setAddress2(suiteNo2AB);
				userDetail.setCity(cityName2AB);
				userDetail.setState(stateName2AB);
				userDetail.setZipCode(zipCode2AB);
				userDetail.setCountry(countryName2AB);
				userDetail.setPhoneNo(phoneNo2AB.replaceAll("[^a-zA-Z0-9]", ""));
				if(CommonUtility.validateString(creditApplicationAccount2AB).equalsIgnoreCase("Y")){
					userDetail.setCreditApplicationRequest(creditApplicationAccount2AB);
				}
				if(fromForm!=null && fromForm.trim().equalsIgnoreCase("secondA")){
					userDetail.setFormtype("2A");
				}else{
					userDetail.setFormtype("2B");
				}
				if(CommonUtility.validateString(disableSubmitPO).equalsIgnoreCase("Y")){
					userDetail.setDisableSubmitPO(disableSubmitPO);
				}
				if(CommonUtility.validateString(disableSubmitPOCC).equalsIgnoreCase("Y")){
					userDetail.setDisableSubmitPOCC(disableSubmitPOCC);
				}
				if(CommonUtility.customServiceUtility()!=null) {
					 CommonUtility.customServiceUtility().setUserDetails(others,salesPersonName,howDidYouSelect,userDetail);
				 }
				SendMailUtility sendMailUtility = new SendMailUtility();
				boolean sentFlag = sendMailUtility.sendRegistrationMail(userDetail,"webUser","","2B");
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_BRONTO_USER_REGISTRATION")).equalsIgnoreCase("Y")){
					BrontoModel brontoModel = userRegistrationDetail.getBrontoDetails();

					if(brontoModel != null){
						brontoModel.setSxCustomerNumber(CommonUtility.validateNumber(adhocUserDetails.get("billingEntityId")));
					}
					String apiToken = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_API_TOKEN"));
					String contactListName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRONTO_REGISTRATION_CONTACT_LIST"));
					BrontoApi client = BrontoUtility.getInstance().brontoLogin(apiToken);
					//List<String> listIds = BrontoUtility.getInstance().getContactListIds(client,contactListName);
					BrontoUtility.getInstance().createOrUpdateContact(client, brontoModel, contactListName, BrontoUtility.REGISTRATION);
					if(brontoModel.isInterestedInCommercialDiscounts()){
						boolean commercialDiscountsFlag = sendMailUtility.CommercialDiscountSubscription(brontoModel); 
					}
				}
				result = "1|"+adhocUserDetails.get("userName")+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.success.message"); 
			}
			renderContent = result;
		}catch (Exception e) {
			e.printStackTrace();
			target = ERROR;
		}
		return target;
	}
	
	public String CommertialCustomerRegistration(){ //existing2A()
		boolean isValidUser = true;
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		boolean isPost = "POST".equals(request.getMethod());
		HttpServletResponse response1 = ServletActionContext.getResponse();
	    result = "";
	    renderContent = "";
	    String customerType = "";
	    UserManagement usersObj = new UserManagementImpl();
	    String erpOverrideFlag = request.getParameter("erpOverrideFlag");
	    String accountType=request.getParameter("accountType");
	    AddressModel userRegistrationDetail = new AddressModel();
	    String URLString=request.getQueryString();
	    try{
	    	if(CommonDBQuery.getSystemParamtersList().get("ENABLE_POST_METHODS_VALIDATION")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_POST_METHODS_VALIDATION").equalsIgnoreCase("Y")) {
	    		if(!isPost || (isPost && URLString!=null)) {
				response1.setStatus(response1.SC_METHOD_NOT_ALLOWED);
				return null;
			}
			String localeCode=CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_LOCALE_CODE"));
			String getPostMethodList=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(localeCode).getProperty("httppostmethodlist.names"));
			if(getPostMethodList!=null && getPostMethodList!="") {
			String [] methodList=getPostMethodList.split(",");
			for(String methodName: methodList) {
				if(!isPost && methodName.equalsIgnoreCase("CommertialCustomerRegistration")) {
					response.setStatus(response.SC_METHOD_NOT_ALLOWED);
								return null;
							}
						}
					}
				}
	    	if(CommonDBQuery.getSystemParamtersList().get("ENABLE_CSRF_TOKEN_FOR_REGISTERPAGE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_CSRF_TOKEN_FOR_REGISTERPAGE").equalsIgnoreCase("Y")) {
	    		
	    		System.out.println("csrftoken: "+request.getParameter("csrftoken"));
				String csrfPreventionSalt =   CommonUtility.validateString((String) session.getAttribute("csrfPreventionSalt"));
				System.out.println("csrfPreventionSalt: "+CommonUtility.validateString(csrfPreventionSalt));
				if(!csrfPreventionSalt.equalsIgnoreCase(CommonUtility.validateString(request.getParameter("csrftoken")))) {
					response1.setStatus(response1.SC_BAD_REQUEST);
					return null;
				}
			}
    	
			    if(firstName2AB==null || firstName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.firstname")+"|";}
				if(lastName2AB==null || lastName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.lastName")+"|";}
				if(checkCompanyName!=null && checkCompanyName.trim().equalsIgnoreCase("N")){
			    	 
			    }else{
			    	if(companyName2AB==null || companyName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.companyName")+"|";}
			    }
				if(emailAddress2AB==null || emailAddress2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.emailAddress")+"|";
				}else{
				    	if(emailAddress2AB!=null && !CommonUtility.validateEmail(emailAddress2AB))
						{				
							isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.validEmailAddress")+"|";
						}
				 }
				if(request.getParameterMap().containsKey("password2AB")){
					if(password2AB==null || password2AB.trim().equalsIgnoreCase("")){
						isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.password")+"|";
					}else{

						if(password2AB.trim().length()<8){
							isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.passwordMinimumCharacters")+"|";
						}
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
							if(!isAlfaNumericOnly(password2AB)){
								isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.passwordCharacters")+"|";
							}
						}
					}
				}
				
				if(request.getParameterMap().containsKey("confirmPassword2AB")){
					if(confirmPassword2AB==null || confirmPassword2AB.trim().equalsIgnoreCase("")){
						isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.passwordConfirmation")+"|";
					}
				}
				if(request.getParameterMap().containsKey("password2AB") && request.getParameterMap().containsKey("confirmPassword2AB") && password2AB!=null && !password2AB.trim().equalsIgnoreCase("") && confirmPassword2AB!=null && !confirmPassword2AB.trim().equalsIgnoreCase("")){

					if(password2AB.trim().compareTo(confirmPassword2AB)!=0){
						isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.passwordMismatch")+"|";
					}
				}
				if(billingAddress2AB==null || billingAddress2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.enterAddress1")+"|";}
				if(cityName2AB==null || cityName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.enterCity")+"|";}
				if(stateName2AB==null || stateName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.selectState")+"|";}
				if(zipCode2AB==null || zipCode2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + SendMailUtility.propertyLoader("register.label.zip","")+"|";}
				if(countryName2AB==null || countryName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.selectCountry")+"|";}
				if(phoneNo2AB==null || phoneNo2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + SendMailUtility.propertyLoader("register.label.phonenumber","")+"|";}
				else
				{
					if(phoneNo2AB!=null && !CommonUtility.validatePhoneNumber(phoneNo2AB))
					{
						isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.validPhoneNumber")+"|";
					}else if(CommonUtility.validateString(checkPhoneFomat).length()>0 && checkPhoneFomat.trim().equalsIgnoreCase("N")){
						phoneNo2AB = phoneNo2AB.replaceAll("[^0-9]", "");
						phoneNo2AB = phoneNo2AB.substring(0,3)+"-"+phoneNo2AB.substring(3,6)+"-"+phoneNo2AB.substring(6);
						System.out.println(phoneNo2AB);
					}else{
						phoneNo2AB = phoneNo2AB.replaceAll("[^0-9]", "");
					}
				}
				

				if(request.getParameterMap().containsKey("checkSameAsBilling") && CommonUtility.validateString(checkSameAsBilling).equalsIgnoreCase("No")){
										
					if(request.getParameterMap().containsKey("shipAddress1") && CommonUtility.validateString(shipAddress1).length()==0){
						isValidUser = false;result = result + "Please enter Ship Address 1.|";
					}
					if(request.getParameterMap().containsKey("shipCity") && CommonUtility.validateString(shipCity).length()==0){
						isValidUser = false;result = result + "Please enter Ship City.|";
					}
					if(request.getParameterMap().containsKey("shipState") && CommonUtility.validateString(shipState).length()==0){
						isValidUser = false;result = result + "Please enter Ship State.|";
					}
					if(request.getParameterMap().containsKey("shipCountry") && CommonUtility.validateString(shipCountry).length()==0){
						isValidUser = false;result = result + "Please enter Ship Country.|";
					}
					if(request.getParameterMap().containsKey("shipZipCode") && CommonUtility.validateString(shipZipCode).length()==0){
						isValidUser = false;result = result + "Please enter Ship Zip Code.|";
					}else if(request.getParameterMap().containsKey("shipZipCode") && !isAlfaNumericOnly(shipZipCode)){
						isValidUser = false;result = result + "Please enter valid Ship Zip Code.|";
					}
					
					if(request.getParameterMap().containsKey("shipPhoneNo") && CommonUtility.validateString(shipPhoneNo).length()==0){
						isValidUser = false;result = result + "Please enter Ship Phone Number.|";
					}else if(request.getParameterMap().containsKey("shipPhoneNo") && !CommonUtility.validatePhoneNumber(shipPhoneNo)){
						isValidUser = false;result = result + "Please enter valid Ship Phone Number.|";
					}
				}
				
				 if(form2APrivacyAndTermsCheckBoxRequired!=null && form2APrivacyAndTermsCheckBoxRequired.trim().equalsIgnoreCase("Y")){
					 if(form2APrivacyAndTermsCheckBox==null || form2APrivacyAndTermsCheckBox.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("registration.Privacy.Policy.and.Terms.and.Conditions")+"|";}
				 }
				
				boolean userExist = false;
				userExist = UsersDAO.checkForUserName(emailAddress2AB);
				customerType = UsersDAO.checkForCustomerType(emailAddress2AB);
				if(userExist && customerType!=null && !customerType.equalsIgnoreCase("G")){
					 isValidUser=false;
					 result = "0|"+ result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2A.error.emailAlreadyExists")+"|";
				 }
				
				else{
					if(customerType!=null && !customerType.equalsIgnoreCase("") && customerType.equalsIgnoreCase("G"))
					{	
						int buyingCompanyId = UsersDAO.getBuyingCompanyIdByUserName(emailAddress2AB);
						UsersDAO.deleteExistingGuestUser(emailAddress2AB);
						UsersDAO.deleteExistingCustomer(buyingCompanyId);
					}
				}
				
				if(isValidUser && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RECAPTCHAV3_REGISTRATIONS")).equalsIgnoreCase("Y")){
		             if(CommonUtility.validateString(request.getParameter("g-recaptcha-response")).length()>0){ 				 
		            	 GoogleRecaptchaV3 v3=new GoogleRecaptchaV3();		
						 if(firstName2AB.toUpperCase().equalsIgnoreCase(lastName2AB.toUpperCase())) {
							 isValidUser=false;
						 }
					 
					 String s= v3.isValid(CommonUtility.validateString(request.getParameter("g-recaptcha-response")));
					 if(s.equalsIgnoreCase("false"))
					 {				
						System.out.println("false");
						isValidUser=false;
					 }
					 if(!isValidUser) {
	                        result = result + "0|" + CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form1B.label.enterCaptcha"))+"|";
	                    }
					}else {
						 isValidUser=false;
						 result = result + "0|" + CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form1B.label.enterCaptcha"))+"|";
		                }
				}
				
				
				if(isValidUser){
					
					if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y"))
					 {
						 emailAddress2AB = emailAddress2AB.toLowerCase();
					 }
					
					AddressModel shipAddress = new AddressModel();
					 LinkedHashMap<String,String> userRegisteration=new  LinkedHashMap<String,String>();
					 userRegisteration.put("firstName2AB",firstName2AB);
					 userRegisteration.put("lastName2AB",lastName2AB);
					 userRegisteration.put("companyName2AB",companyName2AB);
					 userRegisteration.put("emailAddress2AB",emailAddress2AB);
					 //userRegisteration.put("emailAddress2AB",emailAddress2AB.toLowerCase());
					 userRegisteration.put("password2AB",password2AB);
					 userRegisteration.put("billingAddress2AB",billingAddress2AB);
					 userRegisteration.put("cityName2AB",cityName2AB);
					 userRegisteration.put("stateName2AB",stateName2AB);
					 userRegisteration.put("zipCode2AB",zipCode2AB);
					 userRegisteration.put("countryName2AB",countryName2AB);
					 userRegisteration.put("phoneNo2AB",phoneNo2AB);
					 userRegisteration.put("contactClassification2A", contactClassification2A);
					 userRegisteration.put("newsLetterSub2AB",newsLetterSub2AB);
					 userRegisteration.put("creditApplicationAccount2AB",CommonUtility.validateString(creditApplicationAccount2AB));
					 SaveCustomFormDetails saveForm = new SaveCustomFormDetails();
					 saveForm.saveToDataBase(userRegisteration,"RegisterationForm2A");
					
					userRegistrationDetail.setFirstName(firstName2AB);
					userRegistrationDetail.setLastName(lastName2AB);
					userRegistrationDetail.setCompanyName(companyName2AB);
					userRegistrationDetail.setEmailAddress(emailAddress2AB);
					userRegistrationDetail.setUserPassword(password2AB);
					userRegistrationDetail.setAddress1(billingAddress2AB);
					userRegistrationDetail.setAddress2(suiteNo2AB);
					userRegistrationDetail.setCity(cityName2AB);
					userRegistrationDetail.setState(stateName2AB);
					userRegistrationDetail.setZipCode(zipCode2AB);
					userRegistrationDetail.setCountry(CommonUtility.getCountryCode(countryName2AB, "Registration"));
					userRegistrationDetail.setPhoneNo(phoneNo2AB.replaceAll("[^a-zA-Z0-9]", ""));
					userRegistrationDetail.setLocUser(locUser);
					userRegistrationDetail.setNewsLetterSub(newsLetterSub2AB);
					if(CommonUtility.validateString(creditApplicationAccount2AB).equalsIgnoreCase("Y")){
						userRegistrationDetail.setCreditApplicationRequest(CommonUtility.validateString(creditApplicationAccount2AB));
					}
					if(CommonUtility.validateString(checkSameAsBilling).equalsIgnoreCase("No")){	
						//userRegistrationDetail.setCheckSameAsBilling(checkSameAsBilling);
						shipAddress.setFirstName(firstName2AB);
						shipAddress.setLastName(lastName2AB);
						shipAddress.setAddress1(shipAddress1);
						shipAddress.setAddress2(shipAddress2);
						shipAddress.setCity(shipCity);
						shipAddress.setState(shipState);
						shipAddress.setCountry(shipCountry);
						if(CommonUtility.validateString(shipCountry).equalsIgnoreCase("USA")){
							shipAddress.setCountry(CommonUtility.getCountryCode(shipCountry, "Registration"));
						}else{
							shipAddress.setCountry(shipCountry);
						}
						shipAddress.setZipCode(shipZipCode);
						shipAddress.setPhoneNo(shipPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
					    userRegistrationDetail.setShippingAddress(shipAddress);
					}else{
						shipAddress.setFirstName(firstName2AB);
						shipAddress.setLastName(lastName2AB);
						shipAddress.setAddress1(billingAddress2AB);
						shipAddress.setAddress2(suiteNo2AB);
						shipAddress.setCity(cityName2AB);
						shipAddress.setState(stateName2AB);
						shipAddress.setCountry(shipCountry);
						if(CommonUtility.validateString(countryName2AB).equalsIgnoreCase("USA")){
							shipAddress.setCountry(CommonUtility.getCountryCode(countryName2AB, "Registration"));
						}else{
							shipAddress.setCountry(countryName2AB);
						}
						shipAddress.setZipCode(zipCode2AB);
						shipAddress.setPhoneNo(phoneNo2AB.replaceAll("[^a-zA-Z0-9]", ""));
						
						userRegistrationDetail.setShippingAddress(shipAddress);
						
					}
					//--------
					if(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationRole")!=null && CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationRole").trim().length()>0){
						userRegistrationDetail.setRole(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationRole"));
					}else{
						userRegistrationDetail.setRole("Ecomm Customer Super User");
					}
					if(CommonUtility.validateString(disableSubmitPO).equalsIgnoreCase("Y") || CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NEW_COMMERCIAL_CUSTOMER_DISABLE_SUBMIT_PO")).equalsIgnoreCase("Y")){
						disableSubmitPO = "Y";
						userRegistrationDetail.setDisableSubmitPO(disableSubmitPO);
					}
					if(CommonUtility.validateString(disableSubmitPOCC).equalsIgnoreCase("Y")){
						userRegistrationDetail.setDisableSubmitPOCC(disableSubmitPOCC);
					}
					userRegistrationDetail.setFormtype("2A");
					userRegistrationDetail.setUpdateRole(true);
					if(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationStatus")!=null && CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationStatus").trim().length()>0){
						userRegistrationDetail.setUserStatus(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationStatus"));
					}else{
						userRegistrationDetail.setUserStatus("Y");
					}
					userRegistrationDetail.setContactClassification(contactClassification2A);
					userRegistrationDetail.setTermsType(creditline);
					userRegistrationDetail.setErpOverrideFlag(erpOverrideFlag);
					userRegistrationDetail.setIsTaxable(istaxable);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_NEW_CUSTOMER_REGISTRATION_IN_ERP")).equalsIgnoreCase("Y")){
						
							UsersModel userAddress = new UsersModel();
							userAddress.setFirstName(userRegistrationDetail.getFirstName());
							userAddress.setLastName(userRegistrationDetail.getLastName());
							if(userRegistrationDetail.getCompanyName()!=null && !userRegistrationDetail.getCompanyName().trim().equalsIgnoreCase("")){
								userAddress.setEntityName(userRegistrationDetail.getCompanyName());
							}else{
								userAddress.setEntityName(userRegistrationDetail.getFirstName() +" "+userRegistrationDetail.getLastName());
							}
							userAddress.setAddress1(userRegistrationDetail.getAddress1());
							userAddress.setAddress2(userRegistrationDetail.getAddress2());
							userAddress.setCity(userRegistrationDetail.getCity());
							userAddress.setState(userRegistrationDetail.getState());
							userAddress.setZipCode(userRegistrationDetail.getZipCode());
							userAddress.setAccountName(CommonUtility.validateString(accountType));
							  if(userRegistrationDetail.getCountry().equalsIgnoreCase("USA")){
								  userAddress.setCountry(userRegistrationDetail.getCountry());
							  }else{
								  if(userRegistrationDetail.getCountry()!=null && userRegistrationDetail.getCountry().trim().length()>0){
									  userAddress.setCountry(userRegistrationDetail.getCountry());
								  }else{
									  userAddress.setCountry("USA");  
								  }
							  }
							  userAddress.setPhoneNo(userRegistrationDetail.getPhoneNo().replaceAll("[^a-zA-Z0-9]", ""));
							  userAddress.setEmailAddress(userRegistrationDetail.getEmailAddress());
							  userAddress.setNewsLetterSub(newsLetterSub2AB);
							  userAddress.setPassword(userRegistrationDetail.getUserPassword());
							  userAddress.setCreditApplicationRequest(userRegistrationDetail.getCreditApplicationRequest());
							
						  SendMailUtility sendMailUtility = new SendMailUtility();
						  
						  boolean sentFlag = false;
						  if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_MAIL_TO_CUSTOMER")).equalsIgnoreCase("Y")){
							  sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"webUser","",userRegistrationDetail.getFormtype()); //"2B"
						  }
						  if(CommonUtility.validateString(userRegistrationDetail.getFormtype()).trim().equalsIgnoreCase("2A")){
							   sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"customer","",userRegistrationDetail.getFormtype()); //"2B"
						  }
						  result = "1|Registration request sent successfully";
						
					}else{
						
						result = usersObj.createCommertialCustomer(userRegistrationDetail);
						System.out.println("OOk");
						if(CommonUtility.validateString(result).length()>0 && result.toLowerCase().contains("successfully")){
							
							UsersModel userAddress = new UsersModel();
							userAddress.setFirstName(userRegistrationDetail.getFirstName());
							userAddress.setLastName(userRegistrationDetail.getLastName());
							
							if(CommonUtility.validateString(userRegistrationDetail.getEntityId()).length()>0){
								userAddress.setUserERPId(CommonUtility.validateString(userRegistrationDetail.getEntityId()));							
								}
							
							if(userRegistrationDetail.getCompanyName()!=null && !userRegistrationDetail.getCompanyName().trim().equalsIgnoreCase("")){
								userAddress.setEntityName(userRegistrationDetail.getCompanyName());
							}else{
								userAddress.setEntityName(userRegistrationDetail.getFirstName() +" "+userRegistrationDetail.getLastName());
							}
							userAddress.setAddress1(userRegistrationDetail.getAddress1());
							userAddress.setAddress2(userRegistrationDetail.getAddress2());
							userAddress.setCity(userRegistrationDetail.getCity());
							userAddress.setState(userRegistrationDetail.getState());
							userAddress.setZipCode(userRegistrationDetail.getZipCode());
							  if(userRegistrationDetail.getCountry().equalsIgnoreCase("USA")){
								  userAddress.setCountry(userRegistrationDetail.getCountry());
							  }else{
								  if(userRegistrationDetail.getCountry()!=null && userRegistrationDetail.getCountry().trim().length()>0){
									  userAddress.setCountry(userRegistrationDetail.getCountry());
								  }else{
									  userAddress.setCountry("USA");  
								  }
							  }
							  userAddress.setPhoneNo(userRegistrationDetail.getPhoneNo().replaceAll("[^a-zA-Z0-9]", ""));
							  userAddress.setEmailAddress(userRegistrationDetail.getEmailAddress());
							  userAddress.setNewsLetterSub(newsLetterSub2AB);
							  userAddress.setPassword(userRegistrationDetail.getUserPassword());
							
						  SendMailUtility sendMailUtility = new SendMailUtility();
						  
						  boolean sentFlag = false;
						  if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_MAIL_TO_CUSTOMER")).equalsIgnoreCase("Y")){
							  sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"webUser","",userRegistrationDetail.getFormtype()); //"2B"
						  }
						  if(CommonUtility.validateString(userRegistrationDetail.getFormtype()).trim().equalsIgnoreCase("2A")){
							   sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"customer","",userRegistrationDetail.getFormtype()); //"2B"
						  }
						  result = "1|Registration request sent successfully";
						
					}
						if(CommonUtility.validateString(result).length()>0 && result.toLowerCase().contains("successfully")){
							 String constantContact = CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_ENABLE");
			                 if(CommonUtility.validateString(constantContact).length()>0 && constantContact.trim().equalsIgnoreCase("Y")){
			                	 UsersModel userDetails = new UsersModel();
			                     userDetails.setFirstName(firstName2AB);
			                     userDetails.setLastName(lastName2AB);
			                     userDetails.setCompanyName(companyName2AB);
			                     userDetails.setEmailAddress(emailAddress2AB);
			                     String response = UsersDAO.constantContactOne(userDetails);
			                     System.out.println("Constant Contact Response:CommertialCustomerRegistration: "+response);
			                 }
						 }
						
					}
			
				}
		renderContent = result;
	    }catch (Exception e) {
			 e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String OnAccountExistingCustomerRegistration(){ //existing1B()
		boolean isValidUser = true;
		request = ServletActionContext.getRequest();
	    HttpSession session = request.getSession(); 
	    boolean isPost = "POST".equals(request.getMethod());
	    String URLString=request.getQueryString();
	    HttpServletResponse response1 = ServletActionContext.getResponse();
	    result = "";
	    renderContent = "";
	    String customerType = "";
		UsersModel userDetails = new UsersModel();

		ArrayList<UsersModel> superUserStatus = null;
		boolean mail = false;
		try{
			if(CommonDBQuery.getSystemParamtersList().get("ENABLE_POST_METHODS_VALIDATION")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_POST_METHODS_VALIDATION").equalsIgnoreCase("Y")) {
	    		if(!isPost || (isPost && URLString!=null)) {
				response1.setStatus(response1.SC_METHOD_NOT_ALLOWED);
				return null;
			}
			String localeCode=CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_LOCALE_CODE"));
			String getPostMethodList=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(localeCode).getProperty("httppostmethodlist.names"));
			if(getPostMethodList!=null && getPostMethodList!="") {
			String [] methodList=getPostMethodList.split(",");
			for(String methodName: methodList) {
				if(!isPost && methodName.equalsIgnoreCase("OnAccountExistingCustomerRegistration")) {
					response.setStatus(response.SC_METHOD_NOT_ALLOWED);
								return null;
							}
						}
					}
				}
	    	if(CommonDBQuery.getSystemParamtersList().get("ENABLE_CSRF_TOKEN_FOR_REGISTERPAGE")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_CSRF_TOKEN_FOR_REGISTERPAGE").equalsIgnoreCase("Y")) {
	    		
	    		System.out.println("csrftoken: "+request.getParameter("csrftoken"));
				String csrfPreventionSalt =   CommonUtility.validateString((String) session.getAttribute("csrfPreventionSalt"));
				System.out.println("csrfPreventionSalt: "+CommonUtility.validateString(csrfPreventionSalt));
				if(!csrfPreventionSalt.equalsIgnoreCase(CommonUtility.validateString(request.getParameter("csrftoken")))) {
					response1.setStatus(response1.SC_BAD_REQUEST);
					return null;
				}
			}
			
		   /* if(request.getParameterMap().containsKey("companyName1B")){if(companyName1B==null || companyName1B.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.companyName")+"|";}}
			 
			 if(request.getParameterMap().containsKey("accountManager1B") && CommonUtility.validateString(accountManager1B).length()==0){
					isValidUser = false; result =  result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.accountManager")+"|";
			 }
			 
			 if(request.getParameterMap().containsKey("closestBranch1B") && CommonUtility.validateString(closestBranch1B).length()==0){
					isValidUser = false; result =  result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.closestBranch")+"|";
			 }
			 
		     if(checkAccountNumber!=null && checkAccountNumber.trim().equalsIgnoreCase("N")){
		    	 
		     }else{
		     if(accountNo1B==null || accountNo1B.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.accountNumber")+"|";}else{accountNo1B = accountNo1B.toUpperCase();}
		     else{
			 try{  
			    double doubleVar = Double.parseDouble(accountNo1B);  
			  }catch(NumberFormatException nfe){  
				  isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.validAccountNumber")+"|";
			  }  
	 }
		     }
		     if(request.getParameterMap().containsKey("invoice1b")){if(invoice1b==null || accountNo1B.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.invoiceNumber")+"|";}else{invoice1b = invoice1b.toUpperCase();}}
		     if(firstName1B==null || firstName1B.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.firstName")+"|";}
			 if(lastName1B==null || lastName1B.trim().equalsIgnoreCase("")){isValidUser=false; result = result +LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.lastName")+"|";}
			 
			 if(request.getParameterMap().containsKey("jobTitle1B") && (jobTitle1B==null || jobTitle1B.trim().equalsIgnoreCase(""))){
				 isValidUser=false; result = result+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.JobTitle")+"|";
			 }
			 
			 if(emailAddress1B==null || emailAddress1B.trim().equalsIgnoreCase("")){
				 isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.emailAddress")+"|";
			 }else{
			    	if(emailAddress1B!=null && !CommonUtility.validateEmail(emailAddress1B))
					{				
						isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.validEmailAddress")+"|";
					}
			 }
			 if(request.getParameterMap().containsKey("newPassword1B")){
				 if(newPassword1B==null || newPassword1B.trim().equalsIgnoreCase("")){
					 isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.enterPassword")+"|";
				 }
				 else{
					int pwdLength=8;
					if(CommonUtility.validateNumber((CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PASSWORD_MINIMUM_CHARECTERS"))))>4){
						pwdLength = CommonUtility.validateNumber((CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PASSWORD_MINIMUM_CHARECTERS"))));
					}
					 if (newPassword1B.trim().length()<pwdLength){
						 isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.passwordMinimumCharacters")+"|";
					 }
					 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
						 if(!isAlfaNumericOnly(newPassword1B)){
							 isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.passwordCharacters")+"|";
						 }
					 }

				 }
			 }
			 if(request.getParameterMap().containsKey("newPasswordConfirm1B")){
				 if(newPasswordConfirm1B==null || newPasswordConfirm1B.trim().equalsIgnoreCase("")){
					 isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.passwordConfirmation")+"|";
				 }
			 }
			 
			 if(request.getParameterMap().containsKey("newPassword1B") && request.getParameterMap().containsKey("newPasswordConfirm1B") && newPassword1B!=null && !newPassword1B.trim().equalsIgnoreCase("")){
				 if(newPasswordConfirm1B!=null && !newPasswordConfirm1B.trim().equalsIgnoreCase("")){

					 if(newPassword1B.trim().compareTo(newPasswordConfirm1B)!=0){
						 isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.passwordMismatch")+"|";
					 }
				 }
			 }
			 if(isAuthorizationRequest!=null && isAuthorizationRequest.trim().equalsIgnoreCase("N")){
				 requestAuthorization1B = "General User";
				 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("B2B_ON_ACCOUNT_DEFAULT_USER_ROLE")).length()>0){
					requestAuthorization1B = CommonDBQuery.getSystemParamtersList().get("B2B_ON_ACCOUNT_DEFAULT_USER_ROLE");
				 }
			 }else{
				 if(requestAuthorization1B==null || requestAuthorization1B.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.authorizationLevel")+"|";}
			 }
			 if(request.getParameterMap().containsKey("salesRepContact1B") && (salesRepContact1B==null || salesRepContact1B.trim().equalsIgnoreCase(""))){
				 isValidUser=false; result = result+ "Please enter "+SendMailUtility.propertyLoader("form.label.TRCSalesRepContact","")+"|";
			 }
			 
			
			if(request.getParameterMap().containsKey("companyBillingAddress1B")){	 
			 if(companyBillingAddress1B==null || companyBillingAddress1B.trim().equalsIgnoreCase("")){isValidUser=false; result = result + "Please enter "+SendMailUtility.propertyLoader("register.label.address1","")+".|";} //Address 1
			}
			if(request.getParameterMap().containsKey("cityName1B")){
			 if(cityName1B==null || cityName1B.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.enterCity")+"|";}
			}
			if(request.getParameterMap().containsKey("countryName1B")){
				 if(countryName1B==null || countryName1B.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.selectCountry")+"|";}else{
					String defaultCountry = "USA";
		 	    	if(CommonDBQuery.getSystemParamtersList().get("DEFAULT_COUNTRY")!=null && CommonDBQuery.getSystemParamtersList().get("DEFAULT_COUNTRY").trim().length()>0){
		 	    		defaultCountry = CommonDBQuery.getSystemParamtersList().get("DEFAULT_COUNTRY").trim();
		 	    	}
					 if(countryName1B.trim().equalsIgnoreCase(defaultCountry) || countryName1B.trim().equalsIgnoreCase(defaultCountry)){
						 if(stateName2AB==null || stateName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.selectState")+"|";}
					 }else{
						 if(stateName2AB==null || stateName2AB.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.enterState")+"|";}
					 }
				 }
			} 
//			 if(request.getParameterMap().containsKey("zipCode1B") && CommonUtility.validateString(zipCode1B).length()==0){isValidUser=false; result = result + SendMailUtility.propertyLoader("register.label.zip","")+"|";}
			
			 if(request.getParameterMap().containsKey("phoneNo1B")){

				 if(phoneNo1B==null || phoneNo1B.trim().equalsIgnoreCase("")){
					 isValidUser=false; result = result + SendMailUtility.propertyLoader("register.label.phonenumber","")+"|";
				 }
				 else
				 {
					 if(phoneNo1B!=null && !CommonUtility.validatePhoneNumber(phoneNo1B))
					 {
						 isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.validPhonenNumber")+"|";
					 }else if(CommonUtility.validateString(checkPhoneFomat).length()>0&& checkPhoneFomat.trim().equalsIgnoreCase("N")){
						 phoneNo1B = phoneNo1B.replaceAll("[^0-9]", "");
						 phoneNo1B = phoneNo1B.substring(0,3)+"-"+phoneNo1B.substring(3,6)+"-"+phoneNo1B.substring(6);
						 System.out.println(phoneNo1B);
					 }else{
						 phoneNo1B = phoneNo1B.replaceAll("[^0-9]", "");
					 }
				 }
			 }*/
		 if(form1BPrivacyAndTermsCheckBoxRequired!=null && form1BPrivacyAndTermsCheckBoxRequired.trim().equalsIgnoreCase("Y")){
			 if(form1BPrivacyAndTermsCheckBox==null || form1BPrivacyAndTermsCheckBox.trim().equalsIgnoreCase("")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("registration.Privacy.Policy.and.Terms.and.Conditions")+"|";}
		 }
		 
		 
		 if(CommonUtility.customServiceUtility()!=null) {
				isValidUser = CommonUtility.customServiceUtility().validateEmailAddress(isValidUser, emailAddress1B);
				if(!isValidUser) {
					result = result + "0|" + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.validEmailAddress")+"|";
				}
			 }
		 if(isValidUser && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RECAPTCHAV3_REGISTRATIONS")).equalsIgnoreCase("Y")){
			 if(CommonUtility.validateString(request.getParameter("g-recaptcha-response")).length()>0){    
				 GoogleRecaptchaV3 v3=new GoogleRecaptchaV3();		
				 if(firstName1B.toUpperCase().equalsIgnoreCase(lastName1B.toUpperCase())) {
					 isValidUser=false;
				 }
			 
				 String s= v3.isValid(CommonUtility.validateString(request.getParameter("g-recaptcha-response")));
				 if(s.equalsIgnoreCase("false"))
				 {					 
					isValidUser=false;
				 }
				 if(!isValidUser) {
                     result = result + "0|" + CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form1B.label.enterCaptcha"))+"|";
                 }
			 }else {
				 isValidUser=false;
				 result = result + "0|" + CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form1B.label.enterCaptcha"))+"|";
			 }			 
		 }
		 if(CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION")!=null && CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION").trim().equalsIgnoreCase("Y")){
			 if(warehouseId1B==null || warehouseId1B.trim().equalsIgnoreCase("-1")){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.selectBranch")+"|";}
		 }
		 
		 if(isValidUser && request.getParameterMap().containsKey("validateLastOrder")){
			 String validateLastOrder = request.getParameter("validateLastOrder");
			 if(CommonUtility.validateString(validateLastOrder).length()>0 && validateLastOrder.trim().equalsIgnoreCase("Y")){
				 if(request.getParameterMap().containsKey("lastOrderNumber")){
					 String lastOrderNumber = request.getParameter("lastOrderNumber");
					 if(CommonUtility.validateNumber(lastOrderNumber)>0){
						 UsersModel userDet = new UsersModel();
						 userDet.setCountry(countryName1B);
						 userDet.setUserToken(accountNo1B);
						 userDet.setOrderID(lastOrderNumber);
						 UserManagement usersObj = new UserManagementImpl();
						 int lastOrder = usersObj.getLastOrderNumber(userDet);
						 if(lastOrder>0 && CommonUtility.validateNumber(lastOrderNumber)==lastOrder){
							 
						 }else{
							 isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.validLastOrder")+"|";
						 }
					 }else{
						 isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.validLastOrder")+"|";
					 }
				 }else{
					 isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.validLastOrder")+"|";
				 }
			 }
			 
		 }
		 
		 if(CommonDBQuery.getSystemParamtersList().get("CAPTCHA_REQUIRED_ON_REGISTRATION")!=null && CommonDBQuery.getSystemParamtersList().get("CAPTCHA_REQUIRED_ON_REGISTRATION").equalsIgnoreCase("Y")){
			 String userCaptchaResponse = request.getParameter("jcaptcha");
			 if(CommonUtility.validateString(userCaptchaResponse).length()>0){
				String captcha = (String) session.getAttribute("captcha");
					if (captcha != null && userCaptchaResponse != null) {
						if (captcha.equals(userCaptchaResponse)) {
							System.out.println("Captcha Passed");
						}else{
							isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form1B.label.invalidCaptcha")+"|";
					}

					}
				}else{
					isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form1B.label.enterCaptcha")+"|";	
				}
			 }
		 
		 boolean isAccountExist = false;
		 boolean userExist = false;
		
		 if(isValidUser){
			 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SINGLE_USER_UNDER_MULTIPLE_CUSTOMER")).equalsIgnoreCase("Y")){
				 userExist = UsersDAO.checkForUserNameUnderCustomer(emailAddress1B,accountNo1B);
			 }else{
				 userExist = UsersDAO.checkForUserName(emailAddress1B);
			 }
			 
			 isAccountExist = UsersDAO.checkForUsersCustomerId(accountNo1B);
			 customerType = UsersDAO.checkForCustomerType(emailAddress1B);
			 if(userExist && customerType!=null && !customerType.equalsIgnoreCase("G"))
			 {
				 result = result + "0|" + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.emailAddressExists")+"|";
			 }else{
				 if(customerType!=null && !customerType.equalsIgnoreCase("") && customerType.equalsIgnoreCase("G"))
				 {	
					  int buyingCompanyId = UsersDAO.getBuyingCompanyIdByUserName(emailAddress1B);
					  UsersDAO.deleteExistingGuestUser(emailAddress1B);
					  UsersDAO.deleteExistingCustomer(buyingCompanyId);
				 }
					 userDetails.setEntityName(companyName1B);
					 userDetails.setEntityId(CommonUtility.validateString(accountNo1B));
					 userDetails.setAccountName(accountNo1B);
					 userDetails.setFirstName(firstName1B);
					 userDetails.setLastName(lastName1B);
					 userDetails.setEmailAddress(emailAddress1B);
					 userDetails.setPassword(newPassword1B);
					 userDetails.setRequestAuthorizationLevel(requestAuthorization1B);
					 userDetails.setSalesContact(salesContact1B);
					 userDetails.setAddress1(companyBillingAddress1B);
					 userDetails.setAddress2(suiteNo1B);
					 userDetails.setCity(cityName1B);
					 userDetails.setState(stateName2AB); 
					 userDetails.setCountry(CommonUtility.validateString(countryName1B));
					 userDetails.setZipCode(zipCode1B);
					 if(CommonUtility.customServiceUtility()!=null) {
						 CommonUtility.customServiceUtility().setUserDetails(others,salesPersonName,howDidYouSelect,userDetails);
					 }
					 if(CommonUtility.validateString((CommonDBQuery.getSystemParamtersList().get("B2B_ON_ACCOUNT_DEFAULT_USER_ROLE"))).length()>0){
						 userDetails.setUserRole(CommonDBQuery.getSystemParamtersList().get("B2B_ON_ACCOUNT_DEFAULT_USER_ROLE"));
					 }else{
						 userDetails.setUserRole("Ecomm Customer Auth Purchase Agent");
					 }
					 userDetails.setPoNumber(poNumber1b);
					 userDetails.setInvoiceNumber(invoice1b);
					 if(phoneNo1B != null){
						 userDetails.setPhoneNo(CommonUtility.validateString(phoneNo1B.replaceAll("[^a-zA-Z0-9]", "")));
					 }
					 userDetails.setNewsLetterSub(newsLetterSub1B);
					 userDetails.setTermsAndCondition(form1BPrivacyAndTermsCheckBoxRequired);
					 userDetails.setSameAsCustomerAddress(CommonUtility.validateString(sameAsCustomerAddress));
					 userDetails.setValidateZipcode(validateZipcode);
					 
					 if(CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION")!=null && CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION").trim().equalsIgnoreCase("Y")){
						 userDetails.setWareHouseCode(CommonUtility.validateNumber(warehouseId1B));
					 }
					 userDetails.setCustomerAccountExist(isAccountExist);
					 
					 if(CommonDBQuery.getSystemParamtersList().get("OnAccountExistingCustomerRegistrationStatus")!=null && CommonDBQuery.getSystemParamtersList().get("OnAccountExistingCustomerRegistrationStatus").trim().length()>0){
						 userDetails.setUserStatus(CommonDBQuery.getSystemParamtersList().get("OnAccountExistingCustomerRegistrationStatus"));
					 }else{
							userDetails.setUserStatus("Y");
					 }
					 if(request.getParameterMap().containsKey("jobTitle1B") && CommonUtility.validateString(jobTitle1B).length()>0){
						 userDetails.setJobTitle(jobTitle1B);
					 }
					 if(request.getParameterMap().containsKey("salesRepContact1B") && CommonUtility.validateString(salesRepContact1B).length()>0){
						 userDetails.setSalesRepContact(salesRepContact1B);
					 }
					 if(request.getParameterMap().containsKey("accountManager1B") && CommonUtility.validateString(accountManager1B).length()>0){
						 userDetails.setAccountManager(accountManager1B);
					 }
					 if(request.getParameterMap().containsKey("closestBranch1B") && CommonUtility.validateString(closestBranch1B).length()>0){
						 userDetails.setClosestBranch(closestBranch1B);
					 }
					 if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y"))
					 {
						 emailAddress1B = emailAddress1B.toLowerCase();
					 }
					 if(request.getParameterMap().containsKey("subsetFlag") && CommonUtility.validateString(subsetFlag).length()>0){
						 userDetails.setSubsetFlag(subsetFlag);
					 }
					 userDetails.setEnablePO(CommonUtility.validateString(enablePO).length()>0?CommonUtility.validateString(enablePO):"Y");
					 userDetails.setSession(session);
					 userDetails.setContactTitle(contactTitle);
					 LinkedHashMap<String,String> userRegisteration=new  LinkedHashMap<String,String>();
					 userRegisteration.put("companyName1B",companyName1B);
					 userRegisteration.put("accountNo1B",accountNo1B);
					 userRegisteration.put("firstName1B",firstName1B);
					 userRegisteration.put("lastName1B",lastName1B);
					 //userRegisteration.put("emailAddress1B",emailAddress1B.toLowerCase());
					 userRegisteration.put("emailAddress1B",emailAddress1B);
					 userRegisteration.put("newPassword1B",newPassword1B);
					 userRegisteration.put("requestAuthorization1B",requestAuthorization1B);
					 userRegisteration.put("salesContact1B",salesContact1B);
					 userRegisteration.put("companyBillingAddress1B",companyBillingAddress1B);
					 userRegisteration.put("suiteNo1B",suiteNo1B);
					 userRegisteration.put("cityName1B",cityName1B);
					 userRegisteration.put("stateName1B", stateName2AB );
					 userRegisteration.put("countryName1B",countryName1B);
					 userRegisteration.put("zipCode1B", zipCode1B );
					 userRegisteration.put("phoneNo1B",	phoneNo1B );
					 userRegisteration.put("newsLetterSub1B",newsLetterSub1B);
					 userRegisteration.put("form1BPrivacyAndTermsCheckBoxRequired",form1BPrivacyAndTermsCheckBoxRequired);
					 userRegisteration.put("Status", userDetails.getUserStatus());
					 userRegisteration.put("invoice1b",invoice1b);
					 if(CommonUtility.customServiceUtility()!=null) {
						 CommonUtility.customServiceUtility().addUserInformation(userDetails,userRegisteration,"");
					 }
					 if(CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION")!=null && CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION").trim().equalsIgnoreCase("Y")){
						 userRegisteration.put("warehousecode", warehouseId1B);
					 }
					 
					 if(request.getParameterMap().containsKey("jobTitle1B") && (jobTitle1B!=null && jobTitle1B.trim().length()>0)){
						 userRegisteration.put("jobTitle1B",jobTitle1B);
					 }
					 if(request.getParameterMap().containsKey("salesRepContact1B") && (salesRepContact1B!=null && salesRepContact1B.trim().trim().length()>0)){
						 userRegisteration.put("salesRepContact1B",salesRepContact1B);
					 }
					 
					 if(request.getParameterMap().containsKey("closestBranch1B") && CommonUtility.validateString(closestBranch1B).length()>0){
						 userRegisteration.put("closestBranch1B",closestBranch1B);
					 }
					 
					 if(request.getParameterMap().containsKey("accountManager1B") && CommonUtility.validateString(accountManager1B).length()>0){
						 userRegisteration.put("accountManager1B",accountManager1B);
					 }
					 
					 if(CommonUtility.validateString(accountNo1B).length()>0) {
						 superUserStatus = UsersDAO.getSuperUserForAccount(accountNo1B);
					 }
					 else {
						 superUserStatus = UsersDAO.getSuperUserForCompany(companyName1B);
					 }
					
					 UserManagement usersObj = new UserManagementImpl();
					 if(CommonUtility.customServiceUtility()!=null) {
						 CommonUtility.customServiceUtility().setUserStatus(superUserStatus,userDetails,userRegisteration);
					 }
					 
					 SaveCustomFormDetails saveForm = new SaveCustomFormDetails();
					 saveForm.saveToDataBase(userRegisteration,"RegisterationForm1B");					 
					 
					 System.out.println("Before Login request to ERP @OnAccountExistingCustomerRegistration");
					 if(CommonDBQuery.getSystemParamtersList().get("DISABLE_ON_ACCOUNT_REGISTRATION_IN_ERP")!=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_ON_ACCOUNT_REGISTRATION_IN_ERP")).equalsIgnoreCase("Y")){
						 result="successfully";
					 }else{
						 result = usersObj.newOnAccountUserRegistration( userDetails);
					 }
				  
					 System.out.println("After Login request to ERP @OnAccountExistingCustomerRegistration");
					 userDetails.setCompanyName(companyName1B);
					 if(result!=null && result.toLowerCase().contains("successfully")){
                         String constantContact = CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_ENABLE");
                         if(CommonUtility.validateString(constantContact).length()>0 && constantContact.trim().equalsIgnoreCase("Y")){
                                String response = UsersDAO.constantContactOne(userDetails);
                                System.out.println("Constant Contact Response:OnAccountExistingCustomerRegistration: "+response);
                                
                         }
					 }
					 //Not Set Up in Customer Setup
					 if(CommonDBQuery.getSystemParamtersList().get("ONACCOUNT_REGISTRATION_REQUEST")!=null && CommonDBQuery.getSystemParamtersList().get("ONACCOUNT_REGISTRATION_REQUEST").trim().equalsIgnoreCase("Y")  && (result!=null && (result.trim().contains("Not Set Up in Customer Setup") || result.trim().toLowerCase().contains("inactive") || !result.toLowerCase().contains("successfully")))){
						 
						 String successResult = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register.label.onaccountregistration.success");
						 String failureResult = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register.label.onaccountregistration.failure");
						 
						 LinkedHashMap<String,String> onAccountUserRegisteration=new  LinkedHashMap<String,String>();
						 onAccountUserRegisteration.put("Company Name",companyName1B);
						 onAccountUserRegisteration.put("First Name",firstName1B);
						 onAccountUserRegisteration.put("Last Name",lastName1B);
						 onAccountUserRegisteration.put("Email Address",emailAddress1B.toLowerCase());
						 onAccountUserRegisteration.put("New Password",newPassword1B);
						 //onAccountUserRegisteration.put("Request Authorization",requestAuthorization1B);
						 //onAccountUserRegisteration.put("Sales Contact","Y");
						 onAccountUserRegisteration.put("Company Billing Address 1",companyBillingAddress1B);
						 onAccountUserRegisteration.put("Company Billing Address 2",suiteNo1B);
						 onAccountUserRegisteration.put("City Name",cityName1B);
						 onAccountUserRegisteration.put("State Name", stateName2AB );
						 onAccountUserRegisteration.put("Country Name",countryName1B);
						 onAccountUserRegisteration.put("Zip Code", zipCode1B );
						 onAccountUserRegisteration.put("Phone No",	phoneNo1B );
						 onAccountUserRegisteration.put("Status", userDetails.getUserStatus());
						 if(CommonUtility.customServiceUtility()!=null) {
							 CommonUtility.customServiceUtility().addUserInformation(userDetails,onAccountUserRegisteration,CommonDBQuery.getSystemParamtersList().get("ONACCOUNT_REGISTRATION_REQUEST"));
						 }
						 if(result.trim().length()>0){
							onAccountUserRegisteration.put("Account Status", result.trim());
						 }
						 if(CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION")!=null && CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION").trim().equalsIgnoreCase("Y")){
							 onAccountUserRegisteration.put("Warehouse Code", warehouseId1B);
						 }
						 
						 if(request.getParameterMap().containsKey("jobTitle1B") && (jobTitle1B!=null || !jobTitle1B.trim().equalsIgnoreCase(""))){
							 onAccountUserRegisteration.put("jobTitle1B",jobTitle1B);
						 }
						 if(request.getParameterMap().containsKey("salesRepContact1B") && (salesRepContact1B==null || salesRepContact1B.trim().equalsIgnoreCase(""))){
							 onAccountUserRegisteration.put("salesRepContact1B",salesRepContact1B);
						 }
						 
						 if(request.getParameterMap().containsKey("closestBranch1B") && CommonUtility.validateString(closestBranch1B).length()>0){
							 onAccountUserRegisteration.put("closestBranch1B",closestBranch1B);
						 }
						 
						 if(request.getParameterMap().containsKey("accountManager1B") && CommonUtility.validateString(accountManager1B).length()>0){
							 onAccountUserRegisteration.put("accountManager1B",accountManager1B);
						 }
						 
						 //-------------------------------
						 boolean notificationStatus = false;
						 if(CommonDBQuery.getSystemParamtersList().get("ONACCOUNT_REGISTRATION_REQUEST_IN_PROCESS")!=null && CommonDBQuery.getSystemParamtersList().get("ONACCOUNT_REGISTRATION_REQUEST_IN_PROCESS").trim().equalsIgnoreCase("Y")){
							 notificationStatus = SendMailUtility.sendRegistrationNotification(onAccountUserRegisteration);
						 }else{
							 if(result!=null && result.toLowerCase().contains("successfully")){
								 notificationStatus = SendMailUtility.sendRegistrationNotification(onAccountUserRegisteration);
							 }
						 }
						 //-----------------------------
						 
						 if(notificationStatus){
							 result = successResult;
						 }else{
							 result = failureResult;
						 }
						 
					 }else{
						 System.out.println("result From ERP @OnAccountExistingCustomerRegistration - "+ result);
						
						 
						 if(result!=null && result.toLowerCase().contains("successfully")){
							 SendMailUtility sendMail = new SendMailUtility();
							 if (superUserStatus!=null &&  superUserStatus.size()>0){
								 for(UsersModel temp:superUserStatus){
									 UsersDAO.insertRegistrationToCustomTable(userDetails);
									 mail = sendMail.sendRegistrationMail(userDetails,"superUser",temp.getEmailAddress(),"1B");
								 }
							 }
							 
							  //send mail to nce to create customer account
							 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_MAIL_TO_CUSTOMER")).equalsIgnoreCase("Y")){
							  mail = sendMail.sendRegistrationMail(userDetails,"webUser","","1B"); 
							 } 
							 
							 
							  mail = sendMail.sendRegistrationMail(userDetails,"customer","","1B"); 
							 
							  result = "1|Registration request submitted successfully.";
						 }else{
							 if(result!=null && (result.toLowerCase().contains("accountnumber") || result.toLowerCase().contains("noaccountnumber"))) {
								 result = "1|"+result;
							 }
							 else if(result!=null && (result.toLowerCase().contains("invoice") || result.toLowerCase().contains("zipcode"))){
								 result = "0|"+result;
							 }else if(result!=null && (result.toLowerCase().contains("not set up") || result.toLowerCase().contains("invalid") || result.toLowerCase().contains("no information found") || result.toLowerCase().contains("account not found"))){
								 result = "0|Invalid Account number. Please enter valid account number.";
							 }else if(result!=null && result.toLowerCase().contains("entityid")) {
								 result ="0|We are sorry, but the Account Number you entered is not valid.  Please contact your salesman or your local branch for assistance";
							 }else if(result!=null && result.toLowerCase().contains("login")) {
								 result ="0| "+emailAddress1B+" already taken. Please choose a different login.";
							}else if(result!=null && result.equalsIgnoreCase("restrictAccountRegistration")){
								 result ="0|You are attempting to register for a job specific account . Please contact your salesrep or contact customer support for the correct master account number";
								 System.out.println(result);
							}else{ 
								 result = "0|Error while registering. Contact our customer service for further assistance.";
							 }
						 }
						  
					 }
					 /*
					 superUserStatus = UsersDAO.getSuperUserForCompany(companyName1B);
					 
					 
					 UserManagement usersObj = new UserManagementImpl();
					 result = usersObj.newOnAccountUserRegistration( userDetails);
					 
					 
					 SendMailUtility sendMail = new SendMailUtility();
					 if (superUserStatus!=null &&  superUserStatus.size()>0){
						 for(UsersModel temp:superUserStatus){
							 UsersDAO.insertRegistrationToCustomTable(userDetails);
							 mail = sendMail.sendRegistrationMail(userDetails,"superUser",temp.getEmailAddress(),"1B");
						 }
					 }
					 
						 //send mail to nce to create customer account
					  mail = sendMail.sendRegistrationMail(userDetails,"webUser","","1B"); 
					 
					  mail = sendMail.sendRegistrationMail(userDetails,"customer","","1B"); 
					  if(result!=null && result.trim().length()>0){
					  }else{
						  result = "Registration request submitted successfully";
					  }*/
					 
					 /*SendMailUtility sendMail = new SendMailUtility();
					 if (superUserStatus!=null &&  superUserStatus.size()>0){
						 for(UsersModel temp:superUserStatus){
							 UsersDAO.insertRegistrationToCustomTable(userDetails);
							 mail = sendMail.sendRegistrationMail(userDetails,"superUser",temp.getEmailAddress(),"1B");
							 mail = sendMail.sendRegistrationMail(userDetails,"webUser","","1B"); 
						 }
					 }else{
						 //send mail to nce to create customer account
						 //mail = sendMail.sendRegistrationMail(userDetails,"Sales","","1B"); 
					 }
					  mail = sendMail.sendRegistrationMail(userDetails,"customer","","1B"); 
					  result = "Registration request submitted successfully";*/
			}
		}
		 renderContent = result;
		}catch (Exception e) {
			e.printStackTrace();
			target = ERROR;
		}
	    
	    return SUCCESS;
	}
	
	public String ExistingCustomerRegistration(){
		boolean isValidUser = true;
		request = ServletActionContext.getRequest();
	    HttpSession session = request.getSession(); 
	    result = "";
	    renderContent = "";
	    UserManagement usersObj = new UserManagementImpl();
	    try{
	    		String displayStr = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.accountNumberOrcurrentUserID")+".|";
			    if(request.getParameterMap().containsKey("currentUserId1A") &&(currentUserId1A==null || currentUserId1A.trim().equalsIgnoreCase(""))){
			    	isValidUser=false; result = result + "Please enter "+displayStr;
			    }
			    if(iscurrentUserId1A!=null && iscurrentUserId1A.trim().equalsIgnoreCase("N")){}else{
					 if(currentPassword1A==null || currentPassword1A.trim().equalsIgnoreCase("")){
					    	isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.password")+"|";
					    }
				 }
			    
			    if(request.getParameterMap().containsKey("currentCountry1A") &&(currentCountry1A==null || currentCountry1A.trim().equalsIgnoreCase(""))){
			    	isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.selectCountry")+"|";
			    }
			    if(request.getParameterMap().containsKey("currentLastOrder1A") &&(currentLastOrder1A==null || currentLastOrder1A.trim().equalsIgnoreCase(""))){
			    	isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.lastOrder")+"|";
			    }
			    if(request.getParameterMap().containsKey("emailAddress1A")){
			    	if(emailAddress1A==null || emailAddress1A.trim().equalsIgnoreCase("")){
			    		isValidUser=false; result = result +LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.emailAddress")+"|";
			    	}else{
			    		if(emailAddress1A!=null && !CommonUtility.validateEmail(emailAddress1A))
			    		{				
			    			isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.validEmailAddress")+"|";
			    		}
			    	}
			    }
			    if(request.getParameterMap().containsKey("newPassword1A")){
			    	if(newPassword1A==null || newPassword1A.trim().equalsIgnoreCase("")){
			    		isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.newPassword")+"|";
			    	}else{
			    		if(newPassword1A.trim().length()<8){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.passwordMinimumCharacters")+"|";}
			    		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
			    			if(!isAlfaNumericOnly(newPassword1A)){
			    				isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.passwordCharacters")+"|";
			    			}
			    		}
			    	}
			    }
			    if(request.getParameterMap().containsKey("newPasswordConfirm1A") &&(newPasswordConfirm1A.trim().equalsIgnoreCase(""))) {
			    	isValidUser=false;result= result +LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.confirmPassword")+"|";
			    }else if(request.getParameterMap().containsKey("newPasswordConfirm1A") && !newPassword1A.equals(newPasswordConfirm1A)) { 
				  	isValidUser= false; result = result +LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.passwordMismatch")+"|";
				}
			    if(request.getParameterMap().containsKey("currentFirstName1A") &&(currentFirstName1A==null || currentFirstName1A.trim().equalsIgnoreCase(""))){
			    	isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.firstName")+"|";
			    }
			    if(request.getParameterMap().containsKey("currentLastName1A") &&(currentLastName1A==null || currentLastName1A.trim().equalsIgnoreCase(""))){
			    	isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.lastName")+"|";
			    }
			    
			    //--- SX
			    if(isCurrentCountry1A!=null && isCurrentCountry1A.trim().equalsIgnoreCase("Y")){
			    	if(currentCountry1A==null || currentCountry1A.trim().equalsIgnoreCase("")){
			    		isValidUser=false; result = result +LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.selectCountry")+"|";
			    	}
			    }
			    
		
			    boolean isAccountExist = false;
				boolean userExist = false;
				
				if(isValidUser){
				  userExist = UsersDAO.checkForUserName(emailAddress1A);
				  isAccountExist = UsersDAO.checkForUsersCustomerId(currentUserId1A);
				    	 	
				  if(userExist){
				  	 result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.emailIdExists")+"|";
				  }else{
					  
					 LinkedHashMap<String,String> userRegisteration=new  LinkedHashMap<String,String>();
					 userRegisteration.put("currentUserId1A",currentUserId1A);
					 userRegisteration.put("currentCountry1A",currentCountry1A);
					 userRegisteration.put("currentLastOrder1A",currentLastOrder1A);
					 userRegisteration.put("emailAddress1A",emailAddress1A.toLowerCase());
					 userRegisteration.put("newPassword1A",newPassword1A);
					 userRegisteration.put("newPasswordConfirm1A",newPasswordConfirm1A);
					 userRegisteration.put("currentFirstName1A",currentFirstName1A);
					 userRegisteration.put("currentLastName1A",currentLastName1A);
					 userRegisteration.put("newsLetterSub2AB",newsLetterSub1A);
					 SaveCustomFormDetails saveForm = new SaveCustomFormDetails();
					 saveForm.saveToDataBase(userRegisteration,"RegisterationForm1A");
					  
					  
					  UsersModel customerInfoInput = new UsersModel();
					  customerInfoInput.setCustomerAccountExist(isAccountExist);
					  customerInfoInput.setUserToken(currentUserId1A);
					  customerInfoInput.setCurrentPassword(currentPassword1A);
					  customerInfoInput.setPassword(newPassword1A);
					  customerInfoInput.setInvoiceNumber(currentLastOrder1A);
					  customerInfoInput.setEmailAddress(emailAddress1A);
					  customerInfoInput.setFirstName(currentFirstName1A);
					  customerInfoInput.setLastName(currentLastName1A);
					  customerInfoInput.setCountry(currentCountry1A);//SX
					  customerInfoInput.setSession(session);
					  customerInfoInput.setUserRole("Ecomm Customer Auth Purchase Agent");// If required make it Dynamic
					  customerInfoInput.setUserStatus("Y");
					  customerInfoInput.setNewsLetterSub(newsLetterSub1A);
				   	 result = usersObj.existingUserRegistration(customerInfoInput);
				   	 if(CommonUtility.validateString(result).length()>0 && result.toLowerCase().contains("successfully")){
				   		 String constantContact = CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_ENABLE");
				   		 if(CommonUtility.validateString(constantContact).length()>0 && constantContact.trim().equalsIgnoreCase("Y")){
				   			 String response = UsersDAO.constantContactOne(customerInfoInput);
	                         System.out.println("Constant Contact Response:ExistingCustomerRegistration:"+response);
				   		 }
	                 }
				 }
			    }
	    renderContent = result;
		}catch (Exception e) {
			e.printStackTrace();
			target = ERROR;
		}
		return SUCCESS;
	}
	
	public String WithoutLoginCheckoutUserRegistration(){
		boolean isValidUser = true;
		boolean loginSuccess=false;
		UsersModel userDetails = new UsersModel();
		request = ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    SecureData validUserPass = new SecureData();
		result = "";
		try{
			if(request.getParameterMap().containsKey("auFirstName") && CommonUtility.validateString(auFirstName).length()==0){
				isValidUser = false;result =  result + "Please enter First Name.|";
			}
			if(request.getParameterMap().containsKey("auLastName") && CommonUtility.validateString(auLastName).length()==0){
				isValidUser = false;result =  result + "Please enter Last Name.|";
			}
			if(request.getParameterMap().containsKey("auPhoneNumber") && CommonUtility.validateString(auPhoneNumber).length()==0){
				isValidUser = false;result =  result + SendMailUtility.propertyLoader("register.label.phonenumber","")+"|";
			}else if(request.getParameterMap().containsKey("auPhoneNumber") && !CommonUtility.validatePhoneNumber(auPhoneNumber)){
				isValidUser = false;result = result + "Please enter valid Phone Number.|";
			}
			if(request.getParameterMap().containsKey("auEmail") && CommonUtility.validateString(auEmail).length()==0){
				isValidUser = false;result =  result + "Please enter Email.|";
			}else if(request.getParameterMap().containsKey("auEmail") && !CommonUtility.validateEmail(auEmail)){
				isValidUser = false;result = result + "Please enter Valid Email.|";
			}
			if(request.getParameterMap().containsKey("auEmailVerify") && CommonUtility.validateString(auEmailVerify).length()==0){
				isValidUser = false;result =  result + "Please enter Email Address(Verify).|";
			}else if(!CommonUtility.validateString(auEmail).equalsIgnoreCase("")){
			
				if(!CommonUtility.validateString(auEmailVerify).equalsIgnoreCase("")){
					
					if(auEmail.trim().compareTo(auEmailVerify)!=0){
						isValidUser=false; result = result + "Email address does not match.|";
					}
				}
			}
			if(request.getParameterMap().containsKey("auPassword") && CommonUtility.validateString(auPassword).length()==0){
				auPassword = validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
			}else{ 
				if(auPassword.trim().length()<8){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.passwordMinimunCharacters")+"|";}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
					if(!isAlfaNumericOnly(auPassword)){
						isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.passwordCharacters")+"|";
					}
				}
			
			}
			
			if(request.getParameterMap().containsKey("auConfirmPassword") && CommonUtility.validateString(auConfirmPassword).length()==0){
		    	auConfirmPassword = validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
		    }
		    if(request.getParameterMap().containsKey("auPassword") && CommonUtility.validateString(auPassword).trim().length()>0){
				if(CommonUtility.validateString(auConfirmPassword).trim().length()>0){
					if(auPassword.trim().compareTo(auConfirmPassword)!=0){
						isValidUser=false; result = result + "New Password does not match with Password Confirmation.|";
					}
				}
			}
		    if(request.getParameterMap().containsKey("auBillFirstName") && CommonUtility.validateString(auBillFirstName).length()==0){
				isValidUser = false;result = result + "Please enter Bill First Name.|";
			}
			if(request.getParameterMap().containsKey("auBillAddress1") && CommonUtility.validateString(auBillAddress1).length()==0){
				isValidUser = false;result = result + "Please enter Bill Address 1.|";
			}
			if(request.getParameterMap().containsKey("auBillCity") && CommonUtility.validateString(auBillCity).length()==0){
				isValidUser = false;result = result + "Please enter Bill City.|";
			}
			if(request.getParameterMap().containsKey("auBillState") && CommonUtility.validateString(auBillState).length()==0){
				isValidUser = false;result = result + "Please enter Bill State.|";
			}
			if(request.getParameterMap().containsKey("auBillCountry") && CommonUtility.validateString(auBillCountry).length()==0){
				isValidUser = false;result = result + "Please enter Bill Country.|";
			}
			if(request.getParameterMap().containsKey("auBillZipCode") && CommonUtility.validateString(auBillZipCode).length()==0){
				isValidUser = false;result = result + "Please enter Bill Zip Code.|";
			}else if(request.getParameterMap().containsKey("auBillZipCode") && !isAlfaNumericOnly(auBillZipCode)){
				isValidUser = false;result = result + "Please enter valid Bill Zip Code.|";
			}
			if(request.getParameterMap().containsKey("auBillPhoneNo") && CommonUtility.validateString(auBillPhoneNo).length()==0){
				isValidUser = false;result = result + "Please enter Bill Phone Number.|";
			}else if(request.getParameterMap().containsKey("auBillPhoneNo") && !CommonUtility.validatePhoneNumber(auBillPhoneNo)){
				isValidUser = false;result = result + "Please enter valid Bill Phone Number.|";
			}
			if(request.getParameterMap().containsKey("auChkSameAsBilling") && CommonUtility.validateString(auChkSameAsBilling).equalsIgnoreCase("No")){
				if(request.getParameterMap().containsKey("auShipFirstName") && CommonUtility.validateString(auShipFirstName).length()==0){
					isValidUser = false;result = result + "Please enter Ship First Name.|";
				}
				if(request.getParameterMap().containsKey("auShipAddress1") && CommonUtility.validateString(auShipAddress1).length()==0){
					isValidUser = false;result = result + "Please enter Ship Address 1.|";
				}
				if(request.getParameterMap().containsKey("auShipCity") && CommonUtility.validateString(auShipCity).length()==0){
					isValidUser = false;result = result + "Please enter Ship City.|";
				}
				if(request.getParameterMap().containsKey("auShipState") && CommonUtility.validateString(auShipState).length()==0){
					isValidUser = false;result = result + "Please enter Ship State.|";
				}
				if(request.getParameterMap().containsKey("auShipCountry") && CommonUtility.validateString(auShipCountry).length()==0){
					isValidUser = false;result = result + "Please enter Ship Country.|";
				}
				if(request.getParameterMap().containsKey("auShipZipCode") && CommonUtility.validateString(auShipZipCode).length()==0){
					isValidUser = false;result = result + "Please enter Ship Zip Code.|";
				}else if(request.getParameterMap().containsKey("auShipZipCode") && !isAlfaNumericOnly(auShipZipCode)){
					isValidUser = false;result = result + "Please enter valid Ship Zip Code.|";
				}
				
				if(request.getParameterMap().containsKey("auShipPhoneNo") && CommonUtility.validateString(auShipPhoneNo).length()==0){
					isValidUser = false;result = result + "Please enter Ship Phone Number.|";
				}else if(request.getParameterMap().containsKey("auShipPhoneNo") && !CommonUtility.validatePhoneNumber(auShipPhoneNo)){
					isValidUser = false;result = result + "Please enter valid Ship Phone Number.|";
				}
			}
			AddressModel billAddress = new AddressModel();
			AddressModel shipAddress = new AddressModel();
			boolean isUserExistInERP = false;
			String userToken = "";
			if(isValidUser){
					
				if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y"))
				 {
					 auEmail = auEmail.toLowerCase();
				 }
					String erpLogin =  CommonDBQuery.getSystemParamtersList().get("ERPLOGIN");
					String isEclipseDown =  CommonDBQuery.getSystemParamtersList().get("ERPAVAILABLE");
					userDetails.setEmailAddress(auEmail);
					userDetails.setPassword(auPassword);
					userDetails.setFirstName(auFirstName);
					userDetails.setLastName(auLastName);
					userDetails.setPhoneNo(auPhoneNumber.replaceAll("[^a-zA-Z0-9]", "").replaceAll("[^a-zA-Z0-9]", ""));
					userDetails.setNewsLetterSub(auNewsLetterSubscription);
					billAddress.setFirstName(auBillFirstName);
					billAddress.setLastName(auBillLastName);
					if(CommonUtility.validateString(auBillCompanyName).length()>0){
						billAddress.setCompanyName(auBillCompanyName);
					}else{
						if(CommonUtility.validateString(auBillLastName).length()>0){
							billAddress.setCompanyName(auBillFirstName +" "+auBillLastName);
						}else{
							billAddress.setCompanyName(auBillFirstName);
						}
					}
					
					billAddress.setAddress1(auBillAddress1);
					billAddress.setAddress2(auBillAddress2);
					billAddress.setCity(auBillCity);
					billAddress.setState(auBillState);
					billAddress.setCountry(auBillCountry);
					if(CommonUtility.validateString(auBillCountry).equalsIgnoreCase("USA")){
						billAddress.setCountry(CommonUtility.getCountryCode(auBillCountry, "Registration"));
					}else{
						billAddress.setCountry(auBillCountry);
					}
					
					billAddress.setZipCode(auBillZipCode);
					billAddress.setPhoneNo(auBillPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
					
					
					if(CommonUtility.validateString(auChkSameAsBilling).equalsIgnoreCase("Yes")){
						userDetails.setSameAsBillingAddress("Y");
						shipAddress.setFirstName(auBillFirstName);
						shipAddress.setLastName(auBillLastName);
						if(CommonUtility.validateString(auBillCompanyName).length()>0){
							shipAddress.setCompanyName(auBillCompanyName);
						}else{
							if(CommonUtility.validateString(auBillLastName).length()>0){
								shipAddress.setCompanyName(auBillFirstName +" "+auBillLastName);
							}else{
								shipAddress.setCompanyName(auBillFirstName);
							}
						}
						shipAddress.setAddress1(auBillAddress1);
						shipAddress.setAddress2(auBillAddress2);
						shipAddress.setCity(auBillCity);
						shipAddress.setState(auBillState);
						shipAddress.setCountry(auBillCountry);
						if(CommonUtility.validateString(auBillCountry).equalsIgnoreCase("USA")){
							shipAddress.setCountry(CommonUtility.getCountryCode(auBillCountry, "Registration"));
						}else{
							shipAddress.setCountry(auBillCountry);
						}
						
						shipAddress.setZipCode(auBillZipCode);
						shipAddress.setPhoneNo(auBillPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
					}else{
						userDetails.setSameAsBillingAddress("N");
						shipAddress.setFirstName(auShipFirstName);
						shipAddress.setLastName(auShipLastName);
						if(CommonUtility.validateString(auShipCompanyName).length()>0){
							shipAddress.setCompanyName(auShipCompanyName);
						}else{
							if(CommonUtility.validateString(auShipFirstName).length()>0){
								shipAddress.setCompanyName(auShipFirstName +" "+auShipLastName);
							}else{
								shipAddress.setCompanyName(auShipFirstName);
							}
						}
						shipAddress.setAddress1(auShipAddress1);
						shipAddress.setAddress2(auShipAddress2);
						shipAddress.setCity(auShipCity);
						shipAddress.setState(auShipState);
						shipAddress.setCountry(auShipCountry);
						if(CommonUtility.validateString(auShipCountry).equalsIgnoreCase("USA")){
							shipAddress.setCountry(CommonUtility.getCountryCode(auShipCountry, "Registration"));
						}else{
							shipAddress.setCountry(auShipCountry);
						}
						shipAddress.setZipCode(auShipZipCode);
						shipAddress.setPhoneNo(auShipPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
					}
					userDetails.setBillAddress(billAddress);
					userDetails.setShipAddress(shipAddress);
					userDetails.setUserRole("Ecomm Retail User");
					userDetails.setUserStatus("Y");
					userDetails.setSameAsBillingAddress(auChkSameAsBilling);
					userDetails.setContactClassification(auContactClassification);
					
					if(!auPassword.equalsIgnoreCase(validUserPass.validatePassword(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"))))){
						isUserExistInERP = false;
					}else{
						
						if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
							activeCustomerId = "";
							
							userToken = LoginSubmit.ERPLOGIN(auEmail, validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")),CommonUtility.validateString(activeCustomerId));
							String connectionError = (String) session.getAttribute("connectionError");
							if(connectionError.equalsIgnoreCase("No") && isEclipseDown.equalsIgnoreCase("Y")){
					        	if(userToken!=null && !userToken.trim().equalsIgnoreCase("") ){
					        		isUserExistInERP = true;
					        	}else{
					        		isUserExistInERP = false;
					        	}
					        }else{
					        	result = result+ "Server Error. Please Try again later.|";
					        }
						}else{
							
							isUserExistInERP = false;
							try {
								UsersModel userModel = new UsersModel();
								userModel.setUserName(auEmail);
								userModel.setPassword(validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")));
								userModel.setUserStatus("Y");
								HashMap<String, String> checkUserExist = UsersDAO.checkUserExistInDBFromUserNameAndPassword(userModel);
								if(checkUserExist!=null && checkUserExist.size()>0){
									if(CommonUtility.validateString(checkUserExist.get("contactId")).length()>0){
										isUserExistInERP = true;
										userToken = CommonUtility.validateString(checkUserExist.get("contactId"));
									}
								}
							
							} catch (Exception e) {
								e.printStackTrace();
								result = result+ "Server Error. Please Try again later.|";
							}
							
						}
					}
				
				boolean setSession = false;
				if(isUserExistInERP){
					setSession = true;
				}else{
					boolean userExists = UsersDAO.checkForUserName(userDetails.getEmailAddress().toUpperCase());
					if(userExists){
						//result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("AuUser.UserExist.Error");
						result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("AuUser.UserExist.Error").replace("||~~||~~||", auEmail)+"|";
					}else{
						UserManagement userManager = new UserManagementImpl();
						result = userManager.createWLUser(userDetails);
						if(CommonUtility.validateString(result.toLowerCase()).contains("already taken")){
							result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("AnonymousCheckout.userexit.error").replace("||~~||~~||", auEmail)+"|";	
						}else if(!CommonUtility.validateString(result.toLowerCase()).contains("successfully")){
							
						}else{
							setSession = true;
							
							if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
								
								activeCustomerId = "";
								
								//userToken = LoginSubmit.ERPLOGIN(auEmail, CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
								userToken = LoginSubmit.ERPLOGIN(auEmail,auPassword,CommonUtility.validateString(activeCustomerId));
							}
							
							/* for SX if required Discuss and UN-comment the block
							else{
								
								try {
									UsersModel userModel = new UsersModel();
									userModel.setUserName(auEmail);
									userModel.setPassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
									userModel.setUserStatus("Y");
									HashMap<String, String> checkUserExist = UsersDAO.checkUserExistInDBFromUserNameAndPassword(userModel);
									if(checkUserExist!=null && checkUserExist.size()>0){
										if(CommonUtility.validateString(checkUserExist.get("contactId")).length()>0){
											userToken = CommonUtility.validateString(checkUserExist.get("contactId"));
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									result = result+ "Server Error. Please Try again later.|";
								}
								
						   }*/
							
						}
					}
					
				}
				if(setSession){
					UserManagement usersObj = new UserManagementImpl();
					HashMap<String, String> userDetailsFromDB = UsersDAO.getUserPasswordAndUserId(auEmail, "Y");
					String defaultBillID = userDetailsFromDB.get("defaultBillingAddressId");
					String defaultShipID = userDetailsFromDB.get("defaultShippingAddressId");
					HashMap<String, UsersModel> userAddressDB = usersObj.getUserAddressFromBCAddressBook(CommonUtility.validateNumber(defaultBillID), CommonUtility.validateNumber(defaultShipID));
					billAddress.setEntityId(CommonUtility.validateString(userAddressDB.get("Bill").getEntityId()));
					billAddress.setAddressBookId(userAddressDB.get("Bill").getAddressBookId());
					shipAddress.setEntityId(CommonUtility.validateString(userAddressDB.get("Ship").getEntityId()));
					shipAddress.setShipToId(userAddressDB.get("Ship").getShipToId());
					shipAddress.setAddressBookId(userAddressDB.get("Ship").getAddressBookId());
					//shipAddress.setZipCode(userAddressDB.get("Ship").getZipCodeStringFormat());
					userDetails.setBillAddress(billAddress);
					userDetails.setShipAddress(shipAddress);
					userDetails.setUserName(CommonUtility.validateString(auEmail));
					userDetails.setWebsite(CommonDBQuery.webSiteName);
					userDetails.setCustomerName(userDetailsFromDB.get("loginCustomerName"));
					userDetails.setBuyingCompanyId(CommonUtility.validateNumber(userDetailsFromDB.get("buyingCompanyId")));
					userDetails.setSubsetId(CommonUtility.validateNumber(userDetailsFromDB.get("userSubsetId")));
					userDetails.setUserId(CommonUtility.validateNumber(userDetailsFromDB.get("userId")));
					userDetails.setWareHouseCodeStr(CommonUtility.validateString(userDetailsFromDB.get("wareHouseCode")));
					WarehouseModel warehouseDetails = UsersDAO.getWareHouseDetailsByCode(userDetailsFromDB.get("wareHouseCode"));
					if(warehouseDetails != null){
						userDetails.setWareHouseName(warehouseDetails.getWareHouseName());
					}
					userDetails.setCountry(CommonUtility.validateString(userDetailsFromDB.get("customerCountry")));
					userDetails.setEmailAddress(CommonUtility.validateString(userDetailsFromDB.get("userEmailAddress")));
					userDetails.setOfficePhone(CommonUtility.validateString(userDetailsFromDB.get("userOfficePhone")));
					
					if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
						userDetails.setUserToken(CommonUtility.validateString(userToken));
					}else{
						userDetails.setUserToken(CommonUtility.validateString(userDetailsFromDB.get("contactId")));
					}
					if(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT")!=null && CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT").trim().equalsIgnoreCase("Y")){
						String homeBranchZipCode = CommonUtility.validateString(userDetails.getShipAddress().getZipCode());
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
					userDetails.setRequestType("webOrder");
					usersObj.assignErpValues(userDetails);
					session.setAttribute("auUserDetails", userDetails);
					session.setAttribute("auUserLogin", "Y");
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_BRONTO_ABANDONED_CART_EMAIL")).equalsIgnoreCase("Y")){
						System.out.println("Adding entry to abandoned cart table...");
						String tempSubset = (String) session.getAttribute("userSubsetId");
						int subsetId = CommonUtility.validateNumber(tempSubset);
						BrontoDAO.addAbandonedCart(subsetId, CommonUtility.validateNumber(userDetailsFromDB.get("userId")), session.getId(),"Y");
					}
					result = "5|"+auBillFirstName + " "+auBillLastName;
				}
			}
			renderContent = result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String WithoutLoginCheckoutForCashAccount(){

		boolean isValidUser = true;
		boolean loginSuccess=false;
		UsersModel userDetails = new UsersModel();
		request = ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    SecureData validUserPass = new SecureData();
		result = "";
		try{
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GUSTE_CHECKOUT_USER_NAME")).length()>0){
				auEmail = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GUSTE_CHECKOUT_USER_NAME"));
			}
			
			
			
			if(request.getParameterMap().containsKey("auFirstName") && CommonUtility.validateString(auFirstName).length()==0){
				isValidUser = false;result =  result + "Please enter First Name.|";
			}
			if(request.getParameterMap().containsKey("auLastName") && CommonUtility.validateString(auLastName).length()==0){
				isValidUser = false;result =  result + "Please enter Last Name.|";
			}
			if(request.getParameterMap().containsKey("auPhoneNumber") && CommonUtility.validateString(auPhoneNumber).length()==0){
				isValidUser = false;result =  result + SendMailUtility.propertyLoader("register.label.phonenumber","")+"|";
			}else if(request.getParameterMap().containsKey("auPhoneNumber") && !CommonUtility.validatePhoneNumber(auPhoneNumber)){
				isValidUser = false;result = result + "Please enter valid Phone Number.|";
			}
			
			
			if(request.getParameterMap().containsKey("auEmailAddress") && CommonUtility.validateString(auEmailAddress).length()==0){
				isValidUser = false;result =  result + "Please enter Email.|";
			}else if(request.getParameterMap().containsKey("auEmailAddress") && !CommonUtility.validateEmail(auEmailAddress)){
				isValidUser = false;result = result + "Please enter Valid Email.|";
			}
			
			
			if(request.getParameterMap().containsKey("auEmail") && CommonUtility.validateString(auEmail).length()==0){
				isValidUser = false;result =  result + "Please enter Email.|";
			}else if(request.getParameterMap().containsKey("auEmail") && !CommonUtility.validateEmail(auEmail)){
				isValidUser = false;result = result + "Please enter Valid Email.|";
			}
			if(request.getParameterMap().containsKey("auEmailVerify") && CommonUtility.validateString(auEmailVerify).length()==0){
				isValidUser = false;result =  result + "Please enter Email Address(Verify).|";
			}else if(!CommonUtility.validateString(auEmail).equalsIgnoreCase("")){
			
				if(!CommonUtility.validateString(auEmailVerify).equalsIgnoreCase("")){
					
					if(auEmail.trim().compareTo(auEmailVerify)!=0){
						isValidUser=false; result = result + "Email address does not match.|";
					}
				}
			}
			if(request.getParameterMap().containsKey("auPassword") && CommonUtility.validateString(auPassword).length()==0){
				auPassword = validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
			}else{ 
				if(auPassword.trim().length()<8){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.passwordMinimunCharacters")+"|";}
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
					if(!isAlfaNumericOnly(auPassword)){
						isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.passwordCharacters")+"|";
					} 
				}
			
			}
			
			if( request.getParameterMap().containsKey("auConfirmPassword") && CommonUtility.validateString(auConfirmPassword).length()==0){
		    	auConfirmPassword = validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
		    }
		    if(request.getParameterMap().containsKey("auPassword") && CommonUtility.validateString(auPassword).trim().length()>0){
				if(CommonUtility.validateString(auConfirmPassword).trim().length()>0){
					if(auPassword.trim().compareTo(auConfirmPassword)!=0){
						isValidUser=false; result = result + "New Password does not match with Password Confirmation.|";
					}
				}
			}
		    if(request.getParameterMap().containsKey("auBillFirstName") && CommonUtility.validateString(auBillFirstName).length()==0){
				isValidUser = false;result = result + "Please enter Bill First Name.|";
			}
			if(request.getParameterMap().containsKey("auBillAddress1") && CommonUtility.validateString(auBillAddress1).length()==0){
				isValidUser = false;result = result + "Please enter Bill Address 1.|";
			}
			if(request.getParameterMap().containsKey("auBillCity") && CommonUtility.validateString(auBillCity).length()==0){
				isValidUser = false;result = result + "Please enter Bill City.|";
			}
			if(request.getParameterMap().containsKey("auBillState") && CommonUtility.validateString(auBillState).length()==0){
				isValidUser = false;result = result + "Please enter Bill State.|";
			}
			if(request.getParameterMap().containsKey("auBillCountry") && CommonUtility.validateString(auBillCountry).length()==0){
				isValidUser = false;result = result + "Please enter Bill Country.|";
			}
			if(request.getParameterMap().containsKey("auBillZipCode") && CommonUtility.validateString(auBillZipCode).length()==0){
				isValidUser = false;result = result + "Please enter Bill Zip Code.|";
			}else if(request.getParameterMap().containsKey("auBillZipCode") && !isAlfaNumericOnly(auBillZipCode)){
				isValidUser = false;result = result + "Please enter valid Bill Zip Code.|";
			}
			if(request.getParameterMap().containsKey("auBillPhoneNo") && CommonUtility.validateString(auBillPhoneNo).length()==0){
				isValidUser = false;result = result + "Please enter Bill Phone Number.|";
			}else if(request.getParameterMap().containsKey("auBillPhoneNo") && !CommonUtility.validatePhoneNumber(auBillPhoneNo)){
				isValidUser = false;result = result + "Please enter valid Bill Phone Number.|";
			}
			if(request.getParameterMap().containsKey("auChkSameAsBilling") && CommonUtility.validateString(auChkSameAsBilling).equalsIgnoreCase("No")){
				if(request.getParameterMap().containsKey("auShipFirstName") && CommonUtility.validateString(auShipFirstName).length()==0){
					isValidUser = false;result = result + "Please enter Ship First Name.|";
				}
				if(request.getParameterMap().containsKey("auShipAddress1") && CommonUtility.validateString(auShipAddress1).length()==0){
					isValidUser = false;result = result + "Please enter Ship Address 1.|";
				}
				if(request.getParameterMap().containsKey("auShipCity") && CommonUtility.validateString(auShipCity).length()==0){
					isValidUser = false;result = result + "Please enter Ship City.|";
				}
				if(request.getParameterMap().containsKey("auShipState") && CommonUtility.validateString(auShipState).length()==0){
					isValidUser = false;result = result + "Please enter Ship State.|";
				}
				if(request.getParameterMap().containsKey("auShipCountry") && CommonUtility.validateString(auShipCountry).length()==0){
					isValidUser = false;result = result + "Please enter Ship Country.|";
				}
				if(request.getParameterMap().containsKey("auShipZipCode") && CommonUtility.validateString(auShipZipCode).length()==0){
					isValidUser = false;result = result + "Please enter Ship Zip Code.|";
				}else if(request.getParameterMap().containsKey("auShipZipCode") && !isAlfaNumericOnly(auShipZipCode)){
					isValidUser = false;result = result + "Please enter valid Ship Zip Code.|";
				}
				
				if(request.getParameterMap().containsKey("auShipPhoneNo") && CommonUtility.validateString(auShipPhoneNo).length()==0){
					isValidUser = false;result = result + "Please enter Ship Phone Number.|";
				}else if(request.getParameterMap().containsKey("auShipPhoneNo") && !CommonUtility.validatePhoneNumber(auShipPhoneNo)){
					isValidUser = false;result = result + "Please enter valid Ship Phone Number.|";
				}
			}
			AddressModel billAddress = new AddressModel();
			AddressModel shipAddress = new AddressModel();
			boolean isUserExistInERP = false;
			String userToken = "";
			if(isValidUser){
					
				if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y"))
				 {
					 auEmail = auEmail.toLowerCase();
				 }
					String erpLogin =  CommonDBQuery.getSystemParamtersList().get("ERPLOGIN");
					String isEclipseDown =  CommonDBQuery.getSystemParamtersList().get("ERPAVAILABLE");
					userDetails.setUserName(auEmail);
					userDetails.setEmailAddress(auEmailAddress);//auEmail
					userDetails.setPassword(auPassword);
					userDetails.setFirstName(auFirstName);
					userDetails.setLastName(auLastName);
					userDetails.setPhoneNo(auPhoneNumber.replaceAll("[^a-zA-Z0-9]", "").replaceAll("[^a-zA-Z0-9]", ""));
					userDetails.setNewsLetterSub(auNewsLetterSubscription);
					billAddress.setFirstName(auBillFirstName);
					billAddress.setLastName(auBillLastName);
					if(CommonUtility.validateString(auBillCompanyName).length()>0){
						billAddress.setCompanyName(auBillCompanyName);
					}else{
						if(CommonUtility.validateString(auBillLastName).length()>0){
							billAddress.setCompanyName(auBillFirstName +" "+auBillLastName);
						}else{
							billAddress.setCompanyName(auBillFirstName);
						}
					}
					
					billAddress.setAddress1(auBillAddress1);
					billAddress.setAddress2(auBillAddress2);
					billAddress.setCity(auBillCity);
					billAddress.setState(auBillState);
					billAddress.setCountry(auBillCountry);
					if(CommonUtility.validateString(auBillCountry).equalsIgnoreCase("USA")){
						billAddress.setCountry("US");
					}else{
						billAddress.setCountry(auBillCountry);
					}
					
					billAddress.setZipCode(auBillZipCode);
					billAddress.setPhoneNo(auBillPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
					
					
					if(CommonUtility.validateString(auChkSameAsBilling).equalsIgnoreCase("Yes")){
						userDetails.setSameAsBillingAddress("Y");
						shipAddress.setFirstName(auBillFirstName);
						shipAddress.setLastName(auBillLastName);
						if(CommonUtility.validateString(auBillCompanyName).length()>0){
							shipAddress.setCompanyName(auBillCompanyName);
						}else{
							if(CommonUtility.validateString(auBillLastName).length()>0){
								shipAddress.setCompanyName(auBillFirstName +" "+auBillLastName);
							}else{
								shipAddress.setCompanyName(auBillFirstName);
							}
						}
						shipAddress.setAddress1(auBillAddress1);
						shipAddress.setAddress2(auBillAddress2);
						shipAddress.setCity(auBillCity);
						shipAddress.setState(auBillState);
						shipAddress.setCountry(auBillCountry);
						if(CommonUtility.validateString(auBillCountry).equalsIgnoreCase("USA")){
							shipAddress.setCountry("US");
						}else{
							shipAddress.setCountry(auBillCountry);
						}
						
						shipAddress.setZipCode(auBillZipCode);
						shipAddress.setPhoneNo(auBillPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
					}else{
						userDetails.setSameAsBillingAddress("N");
						shipAddress.setFirstName(auShipFirstName);
						shipAddress.setLastName(auShipLastName);
						if(CommonUtility.validateString(auShipCompanyName).length()>0){
							shipAddress.setCompanyName(auShipCompanyName);
						}else{
							if(CommonUtility.validateString(auShipFirstName).length()>0){
								shipAddress.setCompanyName(auShipFirstName +" "+auShipLastName);
							}else{
								shipAddress.setCompanyName(auShipFirstName);
							}
						}
						shipAddress.setAddress1(auShipAddress1);
						shipAddress.setAddress2(auShipAddress2);
						shipAddress.setCity(auShipCity);
						shipAddress.setState(auShipState);
						shipAddress.setCountry(auShipCountry);
						if(CommonUtility.validateString(auShipCountry).equalsIgnoreCase("USA")){
							shipAddress.setCountry("US");
						}else{
							shipAddress.setCountry(auShipCountry);
						}
						shipAddress.setZipCode(auShipZipCode);
						shipAddress.setPhoneNo(auShipPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
					}
					userDetails.setBillAddress(billAddress);
					userDetails.setShipAddress(shipAddress);
					userDetails.setUserRole("Ecomm Retail User");
					userDetails.setUserStatus("Y");
					userDetails.setSameAsBillingAddress(auChkSameAsBilling);
					userDetails.setContactClassification(auContactClassification);
					
					if(!auPassword.equalsIgnoreCase(validUserPass.validatePassword(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"))))){
						isUserExistInERP = false;
					}else{
						
						if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
							
							activeCustomerId = "";
							
							userToken = LoginSubmit.ERPLOGIN(auEmail, validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")),CommonUtility.validateString(activeCustomerId));
							String connectionError = (String) session.getAttribute("connectionError");
							if(connectionError.equalsIgnoreCase("No") && isEclipseDown.equalsIgnoreCase("Y")){
					        	if(userToken!=null && !userToken.trim().equalsIgnoreCase("") ){
					        		isUserExistInERP = true;
					        	}else{
					        		isUserExistInERP = false;
					        	}
					        }else{
					        	result = result+ "Server Error. Please Try again later.|";
					        }
						}else{
							
							isUserExistInERP = false;
							try {
								UsersModel userModel = new UsersModel();
								userModel.setUserName(auEmail);
								userModel.setPassword(validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")));
								userModel.setUserStatus("Y");
								HashMap<String, String> checkUserExist = UsersDAO.checkUserExistInDBFromUserNameAndPassword(userModel);
								if(checkUserExist!=null && checkUserExist.size()>0){
									if(CommonUtility.validateString(checkUserExist.get("contactId")).length()>0){
										isUserExistInERP = true;
										userToken = CommonUtility.validateString(checkUserExist.get("contactId"));
									}
								}
							
							} catch (Exception e) {
								e.printStackTrace();
								result = result+ "Server Error. Please Try again later.|";
							}
							
						}
					}
				
				boolean setSession = false;
				if(isUserExistInERP){
					setSession = true;
				}else{
					boolean userExists = UsersDAO.checkForUserName(userDetails.getEmailAddress().toUpperCase()); // Send User Name if required
					if(userExists){
						//result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("AuUser.UserExist.Error");
						result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("AuUser.UserExist.Error").replace("||~~||~~||", auEmail)+"|";
					}else{
						UserManagement userManager = new UserManagementImpl();
						
						//-- Creating WL checkout User
						result = "";
						//result = userManager.createWLUser(userDetails);
						if(CommonUtility.validateString(result.toLowerCase()).contains("already taken")){
							result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("AnonymousCheckout.userexit.error").replace("||~~||~~||", auEmail)+"|";	
						}else if(!CommonUtility.validateString(result.toLowerCase()).contains("successfully")){
							
						}else{
							setSession = true;
							
							if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
								
								activeCustomerId = "";
								
								//userToken = LoginSubmit.ERPLOGIN(auEmail, CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
								userToken = LoginSubmit.ERPLOGIN(auEmail,auPassword,CommonUtility.validateString(activeCustomerId));
							}
							
							/* for SX if required Discuss and UN-comment the block
							else{
								
								try {
									UsersModel userModel = new UsersModel();
									userModel.setUserName(auEmail);
									userModel.setPassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
									userModel.setUserStatus("Y");
									HashMap<String, String> checkUserExist = UsersDAO.checkUserExistInDBFromUserNameAndPassword(userModel);
									if(checkUserExist!=null && checkUserExist.size()>0){
										if(CommonUtility.validateString(checkUserExist.get("contactId")).length()>0){
											userToken = CommonUtility.validateString(checkUserExist.get("contactId"));
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									result = result+ "Server Error. Please Try again later.|";
								}
								
						   }*/
							
						}
					}
					
				}
				if(setSession){
					UserManagement usersObj = new UserManagementImpl();
					HashMap<String, String> userDetailsFromDB = UsersDAO.getUserPasswordAndUserId(auEmail, "Y");
					String defaultBillID = userDetailsFromDB.get("defaultBillingAddressId");
					String defaultShipID = userDetailsFromDB.get("defaultShippingAddressId");
					HashMap<String, UsersModel> userAddressDB = usersObj.getUserAddressFromBCAddressBook(CommonUtility.validateNumber(defaultBillID), CommonUtility.validateNumber(defaultShipID));
					billAddress.setEntityId(CommonUtility.validateString(userAddressDB.get("Bill").getEntityId()));
					billAddress.setAddressBookId(userAddressDB.get("Bill").getAddressBookId());
					shipAddress.setEntityId(CommonUtility.validateString(userAddressDB.get("Ship").getEntityId()));
					shipAddress.setShipToId(userAddressDB.get("Ship").getShipToId());
					shipAddress.setAddressBookId(userAddressDB.get("Ship").getAddressBookId());
					//shipAddress.setZipCode(userAddressDB.get("Ship").getZipCodeStringFormat());
					userDetails.setBillAddress(billAddress);
					userDetails.setShipAddress(shipAddress);
					userDetails.setUserName(CommonUtility.validateString(auEmail));
					userDetails.setWebsite(CommonDBQuery.webSiteName);
					userDetails.setCustomerName(userDetailsFromDB.get("loginCustomerName"));
					userDetails.setBuyingCompanyId(CommonUtility.validateNumber(userDetailsFromDB.get("buyingCompanyId")));
					userDetails.setSubsetId(CommonUtility.validateNumber(userDetailsFromDB.get("userSubsetId")));
					userDetails.setUserId(CommonUtility.validateNumber(userDetailsFromDB.get("userId")));
					userDetails.setWareHouseCodeStr(CommonUtility.validateString(userDetailsFromDB.get("wareHouseCode")));
					WarehouseModel warehouseDetails = UsersDAO.getWareHouseDetailsByCode(userDetailsFromDB.get("wareHouseCode"));
					userDetails.setWareHouseName(warehouseDetails.getWareHouseName());
					userDetails.setCountry(CommonUtility.validateString(userDetailsFromDB.get("customerCountry")));
					userDetails.setEmailAddress(CommonUtility.validateString(userDetailsFromDB.get("userEmailAddress")));
					userDetails.setOfficePhone(CommonUtility.validateString(userDetailsFromDB.get("userOfficePhone")));
					
					if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
						userDetails.setUserToken(CommonUtility.validateString(userToken));
					}else{
						userDetails.setUserToken(CommonUtility.validateString(userDetailsFromDB.get("contactId")));
						userDetails.setEntityId(CommonUtility.validateString(userDetailsFromDB.get("contactId")));
					}
					if(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT")!=null && CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT").trim().equalsIgnoreCase("Y")){
						String homeBranchZipCode = CommonUtility.validateString(userDetails.getShipAddress().getZipCode());
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
					userDetails.setRequestType("webOrder");
					usersObj.assignErpValues(userDetails);
					session.setAttribute("auUserDetails", userDetails);
					session.setAttribute("auUserLogin", "Y");
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_BRONTO_ABANDONED_CART_EMAIL")).equalsIgnoreCase("Y")){
						System.out.println("Adding entry to abandoned cart table...");
						String tempSubset = (String) session.getAttribute("userSubsetId");
						int subsetId = CommonUtility.validateNumber(tempSubset);
						BrontoDAO.addAbandonedCart(subsetId, CommonUtility.validateNumber(userDetailsFromDB.get("userId")), session.getId(),"Y");
					}
					result = "5|"+auBillFirstName + " "+auBillLastName;
				}
			}
			renderContent = result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	
	}
	
	public String NewCommertialCustomerCreditApplicationRegistrationRequest(){ 
		boolean isValidUser = true;
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionId = session.getId();
	    result = "";
	    renderContent = "";
	    UserManagement usersObj = new UserManagementImpl();
	    AddressModel userRegistrationDetail = new AddressModel();
	    AddressModel shippingAddressDetail = null;
	    try{
			    
	    	if(request.getParameterMap().containsKey("businessName") && CommonUtility.validateString(businessName).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.businessName")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("date") && CommonUtility.validateString(date).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.date")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("businessPhoneNumber") && CommonUtility.validateString(businessPhoneNumber).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.businessPhoneNumber")+"|";
	    	}else{
				if(CommonUtility.validateString(businessPhoneNumber).length()>0 && !CommonUtility.validatePhoneNumber(businessPhoneNumber)){
					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.validPhoneNumber")+"|";
				}else if(CommonUtility.validateString(checkPhoneFomat).length()>0 && checkPhoneFomat.trim().equalsIgnoreCase("N")){
					//businessPhoneNumber = businessPhoneNumber.replaceAll("[^0-9]", "");
					businessPhoneNumber = businessPhoneNumber.substring(0,3)+"-"+businessPhoneNumber.substring(3,6)+"-"+businessPhoneNumber.substring(6);
					System.out.println(businessPhoneNumber);
				}else{
					//businessPhoneNumber = businessPhoneNumber.replaceAll("[^0-9]", "");
				}
			}
	    	if(request.getParameterMap().containsKey("businessFaxNumber") && CommonUtility.validateString(businessFaxNumber).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.businessFaxNumber")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("billingAddress2C") && CommonUtility.validateString(billingAddress2C).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.billingAddress2C")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("shippingAddress2C") && CommonUtility.validateString(shippingAddress2C).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.shippingAddress2C")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("billingCity2C") && CommonUtility.validateString(billingCity2C).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.billingCity2C")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("shippingCity2C") && CommonUtility.validateString(shippingCity2C).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.shippingCity2C")+"|";
	    	} 
	    	if(request.getParameterMap().containsKey("billingState2C") && CommonUtility.validateString(billingState2C).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.billingState2C")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("shippingState2C") && CommonUtility.validateString(shippingState2C).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.shippingState2C")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("billingZipCode2C") && CommonUtility.validateString(billingZipCode2C).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.billingZipCode2C")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("shippingZipCode2C") && CommonUtility.validateString(shippingZipCode2C).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.shippingZipCode2C")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("invoiceByEmailAddress") && CommonUtility.validateString(invoiceByEmailAddress).length()>0){
	    		if(CommonUtility.validateString(invoiceByEmailAddress).length()>0 && !CommonUtility.validateEmail(invoiceByEmailAddress)){				
					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.invoiceByEmailAddress")+"|";
				}
	    	}
	    	if(request.getParameterMap().containsKey("invoiceByFaxNumber") && CommonUtility.validateString(invoiceByFaxNumber).length()>0){
	    		if(CommonUtility.validateString(invoiceByFaxNumber).length()>0 && !CommonUtility.validatePhoneNumber(invoiceByFaxNumber)){
					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.invoiceByFaxNumber")+"|";
				}
	    	}
	    	if(request.getParameterMap().containsKey("invoiceByEDIEmailAddress") && CommonUtility.validateString(invoiceByEDIEmailAddress).length()>0){
	    		if(CommonUtility.validateString(invoiceByEDIEmailAddress).length()>0 && !CommonUtility.validateEmail(invoiceByEDIEmailAddress)){				
					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.invoiceByEDIEmailAddress")+"|";
				}
	    	}
	    	if(request.getParameterMap().containsKey("principalOfficeName")){
	    		principalOfficeName = request.getParameterValues("principalOfficeName");
	    	}
	    	if(request.getParameterMap().containsKey("principalOfficeTitle")){
	    		principalOfficeTitle = request.getParameterValues("principalOfficeTitle");
	    	}
	    	if(request.getParameterMap().containsKey("tradeReferenceName")){
	    		tradeReferenceName = request.getParameterValues("tradeReferenceName");
	    	}
	    	if(request.getParameterMap().containsKey("tradeReferencePhoneNumber")){
	    		tradeReferencePhoneNumber = request.getParameterValues("tradeReferencePhoneNumber");
	    		String[] tradeRefPhoneArray = tradeReferencePhoneNumber; 	    		
	    		if(tradeRefPhoneArray!=null && tradeRefPhoneArray.length>0){
	    			for (String phoneNumber : tradeRefPhoneArray) {
	    				if(CommonUtility.validateString(phoneNumber).length()>0 && !CommonUtility.validatePhoneNumber(phoneNumber)){
	    					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.tradeReferencePhoneNumber")+"|";
	    	    		}
					}
	    		}
	    	}
	    	if(request.getParameterMap().containsKey("tradeReferenceFaxNumber")){
	    		tradeReferenceFaxNumber = request.getParameterValues("tradeReferenceFaxNumber");
	    		String[] tradeRefFaxArray = tradeReferenceFaxNumber; 	    		
	    		if(tradeRefFaxArray!=null && tradeRefFaxArray.length>0){
	    			for (String faxNumber : tradeRefFaxArray) {
	    				if(CommonUtility.validateString(faxNumber).length()>0 && !CommonUtility.validatePhoneNumber(faxNumber)){
	    					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.tradeReferenceFaxNumber")+"|";
	    	    		}
					}
	    		}
	    	}
	    	if(request.getParameterMap().containsKey("tradeReferenceEmailAddress")){
	    		tradeReferenceEmailAddress = request.getParameterValues("tradeReferenceEmailAddress");
	    		String[] tradeRefEmailArray = tradeReferenceEmailAddress; 	    		
	    		if(tradeRefEmailArray!=null && tradeRefEmailArray.length>0){
	    			for (String emailAddress : tradeRefEmailArray) {
	    				if(CommonUtility.validateString(emailAddress).length()>0 && !CommonUtility.validateEmail(emailAddress)){				
	    					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.tradeReferenceEmailAddress")+"|";
	    				}
					}
	    		}
	    	}
	    	if(request.getParameterMap().containsKey("bankReferenceName")){
	    		bankReferenceName = request.getParameterValues("bankReferenceName");
	    	}
	    	if(request.getParameterMap().containsKey("bankReferencePhoneNumber")){
	    		bankReferencePhoneNumber = request.getParameterValues("bankReferencePhoneNumber");
	    		String[] bankRefPhoneArray = bankReferencePhoneNumber; 	    		
	    		if(bankRefPhoneArray!=null && bankRefPhoneArray.length>0){
	    			for (String phoneNumber : bankRefPhoneArray) {
	    				if(CommonUtility.validateString(phoneNumber).length()>0 && !CommonUtility.validatePhoneNumber(phoneNumber)){
	    					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.bankReferencePhoneNumber")+"|";
	    	    		}
					}
	    		}
	    	}
	    	if(request.getParameterMap().containsKey("bankReferenceFaxNumber")){
	    		bankReferenceFaxNumber = request.getParameterValues("bankReferenceFaxNumber");
	    		String[] bankRefFaxArray = bankReferenceFaxNumber; 	    		
	    		if(bankRefFaxArray!=null && bankRefFaxArray.length>0){
	    			for (String faxNumber : bankRefFaxArray) {
	    				if(CommonUtility.validateString(faxNumber).length()>0 && !CommonUtility.validatePhoneNumber(faxNumber)){
	    					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.bankReferenceFaxNumber")+"|";
	    	    		}
					}
	    		}
	    	}
	    	if(request.getParameterMap().containsKey("bankReferenceContactName")){
	    		bankReferenceContactName = request.getParameterValues("bankReferenceContactName");
	    	}
	    	if(request.getParameterMap().containsKey("bankReferenceAccountOrLoanNumber")){
	    		bankReferenceAccountOrLoanNumber = request.getParameterValues("bankReferenceAccountOrLoanNumber");
	    	}
	    	if(request.getParameterMap().containsKey("declarationName") && CommonUtility.validateString(declarationName).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.declarationName")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("declaratioEmailAddress") && CommonUtility.validateString(declaratioEmailAddress).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.declaratioEmailAddress")+"|";
	    	}else{
		    	if(CommonUtility.validateString(declaratioEmailAddress).length()>0 && !CommonUtility.validateEmail(declaratioEmailAddress)){				
					isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.validEmailAddress")+"|";
				}
	    	}
	    	if(request.getParameterMap().containsKey("declarationTitle") && CommonUtility.validateString(declarationTitle).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.declarationTitle")+"|";
	    	}
	    	if(request.getParameterMap().containsKey("declarationDate") && CommonUtility.validateString(declarationDate).length()<1){
	    		isValidUser=false;result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.declarationDate")+"|";
	    	}
	    	
	    	 LinkedHashMap<String, String> formData = new LinkedHashMap<>();
			 Enumeration<String> parameterNames = request.getParameterNames();

			 while (parameterNames.hasMoreElements()) {
				 String paramName = parameterNames.nextElement();
				 String[] paramValues = request.getParameterValues(paramName);
				 for (int i = 0; i < paramValues.length; i++) {
					 String paramValue = paramValues[i];
					 formData.put(paramName, paramValue);
					 System.out.println("Parameter Name : " + paramName + ", ParameterValue : " + paramValue);
				 }
			 }
	    	
	    	//-------------------------------------------
	    		
				boolean userExist = false;
				userExist = UsersDAO.checkForUserName(declaratioEmailAddress);
				isValidUser = true; //------------------------------------------------------------------------------------------------------------ 
				if(userExist){
					 isValidUser=false;
					 result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2C.error.emailAlreadyExists")+"|";
				 }
				if(isValidUser){
					
					if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y") && declaratioEmailAddress!=null){
						declaratioEmailAddress = declaratioEmailAddress.toLowerCase();
					 }
					 LinkedHashMap<String,String> userRegisterationSaveFormDetails=new  LinkedHashMap<String,String>();
					 userRegisterationSaveFormDetails.put("businessName",CommonUtility.validateString(businessName));
					 userRegisterationSaveFormDetails.put("date",CommonUtility.validateString(date));
					 userRegisterationSaveFormDetails.put("businessPhoneNumber",CommonUtility.validateString(businessPhoneNumber));
					 userRegisterationSaveFormDetails.put("businessFaxNumber",CommonUtility.validateString(businessFaxNumber));
					 userRegisterationSaveFormDetails.put("billingAddress2C",CommonUtility.validateString(billingAddress2C));
					 userRegisterationSaveFormDetails.put("shippingAddress2C",CommonUtility.validateString(shippingAddress2C));
					 userRegisterationSaveFormDetails.put("billingCity2C",CommonUtility.validateString(billingCity2C));
					 userRegisterationSaveFormDetails.put("shippingCity2C",CommonUtility.validateString(shippingCity2C));
					 userRegisterationSaveFormDetails.put("billingState2C",CommonUtility.validateString(billingState2C));
					 userRegisterationSaveFormDetails.put("shippingState2C",CommonUtility.validateString(shippingState2C));
					 userRegisterationSaveFormDetails.put("billingZipCode2C",CommonUtility.validateString(billingZipCode2C));
					 userRegisterationSaveFormDetails.put("shippingZipCode2C",CommonUtility.validateString(shippingZipCode2C));
					 userRegisterationSaveFormDetails.put("legalStructureRadio",CommonUtility.validateString(legalStructureRadio));
					 userRegisterationSaveFormDetails.put("federalIdNumber",CommonUtility.validateString(federalIdNumber));
					 userRegisterationSaveFormDetails.put("salesTaxStatusRadio",CommonUtility.validateString(salesTaxStatusRadio));
					 userRegisterationSaveFormDetails.put("salesTaxExemptionCertificateFileName",CommonUtility.validateString(salesTaxExemptionCertificateFileName));
					 userRegisterationSaveFormDetails.put("divisionOf",CommonUtility.validateString(divisionOf));
					 userRegisterationSaveFormDetails.put("subsidiaryOf",CommonUtility.validateString(subsidiaryOf));
					 userRegisterationSaveFormDetails.put("businessTyp",CommonUtility.validateString(businessTyp));
					 userRegisterationSaveFormDetails.put("invoiceRadio",CommonUtility.validateString(invoiceRadio));
					 userRegisterationSaveFormDetails.put("invoiceByEmailAddress",CommonUtility.validateString(invoiceByEmailAddress));
					 userRegisterationSaveFormDetails.put("invoiceByFaxNumber",CommonUtility.validateString(invoiceByFaxNumber));
					 userRegisterationSaveFormDetails.put("invoiceByEDIContactName",CommonUtility.validateString(invoiceByEDIContactName));
					 userRegisterationSaveFormDetails.put("invoiceByEDIEmailAddress",CommonUtility.validateString(invoiceByEDIEmailAddress));
					 if(principalOfficeName!=null) {
						 userRegisterationSaveFormDetails.put("principalOfficeName",principalOfficeName.toString());
					 }
					 if(principalOfficeTitle!=null) {
						 userRegisterationSaveFormDetails.put("principalOfficeTitle",principalOfficeTitle.toString());
					 }
					 userRegisterationSaveFormDetails.put("dateBusinessCommenced",CommonUtility.validateString(dateBusinessCommenced));
					 userRegisterationSaveFormDetails.put("numberOfEmployees",CommonUtility.validateParseIntegerToString(numberOfEmployees));
					 userRegisterationSaveFormDetails.put("creditLimitRequest",CommonUtility.validateString(creditLimitRequest));
					 userRegisterationSaveFormDetails.put("apContactPersonEmail",CommonUtility.validateString(apContactPersonEmail));
					 userRegisterationSaveFormDetails.put("finacialStatmentAvailableRadio",CommonUtility.validateString(finacialStatmentAvailableRadio));
					 userRegisterationSaveFormDetails.put("tradreferenceCount",CommonUtility.validateParseIntegerToString(tradreferenceCount));
					 if(tradeReferenceName!=null) {
						 userRegisterationSaveFormDetails.put("tradeReferenceName",tradeReferenceName.toString());
					 }
					 if(tradeReferencePhoneNumber!=null) {
						 userRegisterationSaveFormDetails.put("tradeReferencePhoneNumber",tradeReferencePhoneNumber.toString());
					 }
					 if(tradeReferenceFaxNumber!=null) {
						 userRegisterationSaveFormDetails.put("tradeReferenceFaxNumber",tradeReferenceFaxNumber.toString());
					 }
					 if(tradeReferenceEmailAddress!=null) {
						 userRegisterationSaveFormDetails.put("tradeReferenceEmailAddress",tradeReferenceEmailAddress.toString());
					 }
					 if(bankReferenceName!=null) {
						 userRegisterationSaveFormDetails.put("bankReferenceName",bankReferenceName.toString());
					 }
					 if(bankReferencePhoneNumber!=null) {
						 userRegisterationSaveFormDetails.put("bankReferencePhoneNumber",bankReferencePhoneNumber.toString());
					 }
					 if(bankReferenceFaxNumber!=null) {
						 userRegisterationSaveFormDetails.put("bankReferenceFaxNumber",bankReferenceFaxNumber.toString());
					 }
					 if(bankReferenceContactName!=null) {
						 userRegisterationSaveFormDetails.put("bankReferenceContactName",bankReferenceContactName.toString());
					 }
					 if(bankReferenceAccountOrLoanNumber!=null) {
						 userRegisterationSaveFormDetails.put("bankReferenceAccountOrLoanNumber",bankReferenceAccountOrLoanNumber.toString());
					 }
					 userRegisterationSaveFormDetails.put("declarationName",CommonUtility.validateString(declarationName));
					 userRegisterationSaveFormDetails.put("declaratioEmailAddress",CommonUtility.validateString(declaratioEmailAddress));
					 userRegisterationSaveFormDetails.put("declarationTitle",CommonUtility.validateString(declarationTitle));
					 userRegisterationSaveFormDetails.put("declarationDate",CommonUtility.validateString(declarationDate));

					 SaveCustomFormDetails saveForm = new SaveCustomFormDetails();
					 saveForm.saveToDataBase(userRegisterationSaveFormDetails,"RegisterationForm2C");
					
					String firstName = "";
					String LastName = "";
					if(CommonUtility.validateString(declarationName).length()>0){
						String[] splitname = declarationName.split(" ");
						if(splitname!=null){
							if(splitname.length>1){
								firstName = splitname[0];
								LastName = splitname[1];
								
								if(splitname.length>2){
									LastName = LastName+splitname[2];
								}
							}else{
								firstName = declarationName;
							}
							
						}
					}
					else {
						if(firstName2AB!=null)
							firstName=firstName2AB;
						if(lastName2AB!=null)
							LastName=lastName2AB;
					}
					userRegistrationDetail.setFirstName(firstName);
					userRegistrationDetail.setLastName(LastName);
					userRegistrationDetail.setCompanyName(businessName);
					userRegistrationDetail.setEmailAddress(declaratioEmailAddress);
					userRegistrationDetail.setUserPassword(password2AB); // ------------------------
					userRegistrationDetail.setAddress1(billingAddress2C);
					userRegistrationDetail.setAddress2(billingAddressTwo2C);//---------------------
					userRegistrationDetail.setCity(billingCity2C);
					userRegistrationDetail.setState(billingState2C);
					userRegistrationDetail.setZipCode(billingZipCode2C);
					userRegistrationDetail.setCountry("US");
					userRegistrationDetail.setPhoneNo(businessPhoneNumber);//.replaceAll("[^a-zA-Z0-9]", "")
					userRegistrationDetail.setLocUser(locUser);
					if(CommonUtility.validateString(newsLetterSub2AB).length()>0) {
						userRegistrationDetail.setNewsLetterSub(newsLetterSub2AB);
					}else {
						userRegistrationDetail.setNewsLetterSub("");
					}
					if(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationRole")!=null && CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationRole").trim().length()>0){
						userRegistrationDetail.setRole(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationRole"));
					}else{
						userRegistrationDetail.setRole("Ecomm Customer Super User");
					}
					if(CommonUtility.validateString(disableSubmitPO).equalsIgnoreCase("Y")){
						userRegistrationDetail.setDisableSubmitPO(disableSubmitPO);
					}
					if(CommonUtility.validateString(disableSubmitPOCC).equalsIgnoreCase("Y")){
						userRegistrationDetail.setDisableSubmitPOCC(disableSubmitPOCC);
					}
					userRegistrationDetail.setFormtype("2C");
					userRegistrationDetail.setUpdateRole(true);
					if(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationStatus")!=null && CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationStatus").trim().length()>0){
						userRegistrationDetail.setUserStatus(CommonDBQuery.getSystemParamtersList().get("CommertialCustomerRegistrationStatus"));
					}else{
						userRegistrationDetail.setUserStatus("Y");
					}
					userRegistrationDetail.setContactClassification("");
					
					if(CommonUtility.validateString(shippingAddress2C).length()>0){
						shippingAddressDetail = new AddressModel();
						shippingAddressDetail.setAddress1(CommonUtility.validateString(shippingAddress2C));
						shippingAddressDetail.setCity(CommonUtility.validateString(shippingCity2C));
						shippingAddressDetail.setState(CommonUtility.validateString(shippingState2C));
						shippingAddressDetail.setZipCode(CommonUtility.validateString(shippingZipCode2C));
						userRegistrationDetail.setShippingAddress(shippingAddressDetail);
					}
					if(request.getParameterMap().containsKey("subsetFlag") && (subsetFlag!=null || !subsetFlag.trim().equalsIgnoreCase(""))){
						userRegistrationDetail.setSubsetFlag(subsetFlag);;
					}
					//--------- Credit Application Model
					CreditApplicationModel creditAplication = new CreditApplicationModel();
						creditAplication.setBusinessName(CommonUtility.validateString(businessName));
						creditAplication.setDate(CommonUtility.validateString(date));
						creditAplication.setBusinessPhoneNumber(CommonUtility.validateString(businessPhoneNumber));
						creditAplication.setBusinessFaxNumber(CommonUtility.validateString(businessFaxNumber));
						creditAplication.setBillingAddress2C(CommonUtility.validateString(billingAddress2C));
						creditAplication.setShippingAddress2C(CommonUtility.validateString(shippingAddress2C));
						creditAplication.setBillingCity2C(CommonUtility.validateString(billingCity2C));
						creditAplication.setShippingCity2C(CommonUtility.validateString(shippingCity2C));
						creditAplication.setBillingState2C(CommonUtility.validateString(billingState2C));
						creditAplication.setShippingState2C(CommonUtility.validateString(shippingState2C));
						creditAplication.setBillingZipCode2C(CommonUtility.validateString(billingZipCode2C));
						creditAplication.setShippingZipCode2C(CommonUtility.validateString(shippingZipCode2C));
						creditAplication.setLegalStructureRadio(CommonUtility.validateString(legalStructureRadio));
						creditAplication.setFederalIdNumber(CommonUtility.validateString(federalIdNumber));
						creditAplication.setSalesTaxStatusRadio(CommonUtility.validateString(salesTaxStatusRadio));
						creditAplication.setSalesTaxExemptionCertificateFileName(CommonUtility.validateString(salesTaxExemptionCertificateFileName));
						creditAplication.setDivisionOf(CommonUtility.validateString(divisionOf));
						creditAplication.setSubsidiaryOf(CommonUtility.validateString(subsidiaryOf));
						creditAplication.setBusinessTyp(CommonUtility.validateString(businessTyp));
						creditAplication.setInvoiceRadio(CommonUtility.validateString(invoiceRadio));
						creditAplication.setInvoiceByEmailAddress(CommonUtility.validateString(invoiceByEmailAddress));
						creditAplication.setInvoiceByFaxNumber(CommonUtility.validateString(invoiceByFaxNumber));
						creditAplication.setInvoiceByEDIContactName(CommonUtility.validateString(invoiceByEDIContactName));
						creditAplication.setInvoiceByEDIEmailAddress(CommonUtility.validateString(invoiceByEDIEmailAddress));
						creditAplication.setPrincipalOfficeName(principalOfficeName);
						creditAplication.setPrincipalOfficeTitle(principalOfficeTitle);
						creditAplication.setPrincipalOfficeAddress(principalOfficeAddress);
						creditAplication.setPrincipalOfficeCity(principalOfficeCity);
						creditAplication.setPrincipalOfficeZipCode(principalOfficeZipCode);
						creditAplication.setPrincipalOfficeHomePhone(principalOfficeHomePhone);
						creditAplication.setPrincipalOfficeSocSecNo(principalOfficeSocSecNo);
						creditAplication.setPrincipalOfficeSpouseName(principalOfficeSpouseName);
						creditAplication.setDateBusinessCommenced(CommonUtility.validateString(dateBusinessCommenced));
						creditAplication.setNumberOfEmployees(numberOfEmployees);
						creditAplication.setCreditLimitRequest(creditLimitRequest);
						creditAplication.setApContactPersonEmail(CommonUtility.validateString(apContactPersonEmail));
						creditAplication.setFinacialStatmentAvailableRadio(CommonUtility.validateString(finacialStatmentAvailableRadio));
						creditAplication.setFinacialStatmentFileName(CommonUtility.validateString(finacialStatmentFileName));
						creditAplication.setTradreferenceCount(tradreferenceCount);
						creditAplication.setTradeReferenceName(tradeReferenceName);
						creditAplication.setTradeReferenceAddress(tradeReferenceAddress);
						creditAplication.setTradeReferenceCity(tradeReferenceCity);
						creditAplication.setTradeReferenceZipCode(tradeReferenceZipCode);
						creditAplication.setTradeReferencePhoneNumber(tradeReferencePhoneNumber);
						creditAplication.setTradeReferenceAccountNumber(tradeReferenceAccountNumber);
						creditAplication.setTradeReferenceFaxNumber(tradeReferenceFaxNumber);
						creditAplication.setTradeReferenceEmailAddress(tradeReferenceEmailAddress);
						creditAplication.setBankReferenceName(bankReferenceName);
						creditAplication.setBankReferenceAddress(bankReferenceAddress);
						creditAplication.setBankReferenceCity(bankReferenceCity);
						creditAplication.setBankReferencePhoneNumber(bankReferencePhoneNumber);
						creditAplication.setBankReferenceFaxNumber(bankReferenceFaxNumber);
						creditAplication.setBankReferenceContactName(bankReferenceContactName);
						creditAplication.setBankReferenceZipCode(bankReferenceZipCode);
						creditAplication.setBankReferenceAccountOrLoanNumber(bankReferenceAccountOrLoanNumber);
						creditAplication.setDeclarationName(CommonUtility.validateString(declarationName));
						creditAplication.setDeclaratioEmailAddress(CommonUtility.validateString(declaratioEmailAddress));
						creditAplication.setDeclarationTitle(CommonUtility.validateString(declarationTitle));
						creditAplication.setDeclarationDate(CommonUtility.validateString(declarationDate));
						creditAplication.setSignature(CommonUtility.validateString(signature));
						creditAplication.setAmountOfCredit(CommonUtility.validateString(amountOfCredit));
						creditAplication.setPhoneNumber(CommonUtility.validateString(phoneNumber));
						creditAplication.setRealEstateName(realEstateName);
						creditAplication.setRealEstateAddress(realEstateAddress);
						creditAplication.setRealEstateValue(realEstateValue);
						creditAplication.setRealEstateTitle(realEstateTitle);
						creditAplication.setRealEstateBalance(realEstateBalance);
						creditAplication.setRealEstateMortgage(realEstateMortgage);
						creditAplication.setRealEstateBusiness(realEstateBusiness);
						creditAplication.setRealEstateBusinessAddress(realEstateBusinessAddress);
						creditAplication.setRealEstateBusinessValue(realEstateBusinessValue);
						creditAplication.setRealEstateBusinessTitle(realEstateBusinessTitle);
						creditAplication.setRealEstateBusinessBalance(realEstateBusinessBalance);
						creditAplication.setRealEstateBusinessMortgage(realEstateBusinessMortgage);
						creditAplication.setOtherStates(otherStates);
						creditAplication.setChargeCardName(chargeCardName);
						creditAplication.setAccountNumber(accountNumber);
						creditAplication.setChargeCardExpDate(chargeCardExpDate);
						creditAplication.setBillShipInstruction(billShipInstruction);
						creditAplication.setBillingMode(billingMode);
						creditAplication.setFormData(formData);
						creditAplication.setSubsetFlag(subsetFlag);
						userRegistrationDetail.setCreditApplicationModel(creditAplication);
						
						/*if(session.getAttribute("creditAplication")==null){
							session.setAttribute("creditAplication", creditAplication);
						}*/
					
					//--------- Credit Application Model
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_NEW_CUSTOMER_REGISTRATION_IN_ERP")).equalsIgnoreCase("Y")){
						
							UsersModel userAddress = new UsersModel();
							userAddress.setFirstName(userRegistrationDetail.getFirstName());
							userAddress.setLastName(userRegistrationDetail.getLastName());
							if(userRegistrationDetail.getCompanyName()!=null && !userRegistrationDetail.getCompanyName().trim().equalsIgnoreCase("")){
								userAddress.setEntityName(userRegistrationDetail.getCompanyName());
							}else{
								userAddress.setEntityName(userRegistrationDetail.getFirstName() +" "+userRegistrationDetail.getLastName());
							}
							userAddress.setAddress1(userRegistrationDetail.getAddress1());
							userAddress.setAddress2(userRegistrationDetail.getAddress2());
							userAddress.setCity(userRegistrationDetail.getCity());
							userAddress.setState(userRegistrationDetail.getState());
							userAddress.setZipCode(userRegistrationDetail.getZipCode());
							  if(userRegistrationDetail.getCountry().equalsIgnoreCase("USA")){
								  userAddress.setCountry(userRegistrationDetail.getCountry());
							  }else{
								  if(userRegistrationDetail.getCountry()!=null && userRegistrationDetail.getCountry().trim().length()>0){
									  userAddress.setCountry(userRegistrationDetail.getCountry());
								  }else{
									  userAddress.setCountry("USA");  
								  }
							  }
							  userAddress.setPhoneNo(userRegistrationDetail.getPhoneNo());//.replaceAll("[^a-zA-Z0-9]", "")
							  userAddress.setEmailAddress(userRegistrationDetail.getEmailAddress());
							  userAddress.setNewsLetterSub("");//--------------------------
							  userAddress.setPassword(userRegistrationDetail.getUserPassword());
							
						  SendMailUtility sendMailUtility = new SendMailUtility();
						  boolean sentFlag = false;
						 
						  //----------------------------------------------------------------------------------------------------------------------------------------
						 /* if(session.getAttribute("creditAplication")!=null){
							  creditAplication = (CreditApplicationModel) session.getAttribute("creditAplication");
						  }*/
						//----------------------------------------------------------------------------------------------------------------------------------------
						  
						  if(CommonUtility.validateString(userRegistrationDetail.getFormtype()).equalsIgnoreCase("2C") && creditAplication!=null){
			        	    	UsersDAO usersDAO = new UsersDAO();
			        	    	int userId = 1;
			        	    	int creditApplicationId = usersDAO.insertCustomerCreditApplicationDetails(userId, creditAplication);
			        	    	
			        	    	//CreditApplicationTemplate
			        	    	userRegistrationDetail.setFormHtmlDetails(sendMailUtility.buildCreditApplictionForm(creditAplication));
			        	    	//------- Generate PDF
			        	    	String creditAppFileName = "CreditApplication_"+creditApplicationId+".pdf";
			        	    	if(userRegistrationDetail!=null && userRegistrationDetail.getFormHtmlDetails()!=null && userRegistrationDetail.getFormHtmlDetails().length()>0){
			        	    		ConvertHtmlToPdf convertHtmlToPdf = new ConvertHtmlToPdf();
			        	    		convertHtmlToPdf.gerratePdfFromHtml(userRegistrationDetail.getFormHtmlDetails(), creditAppFileName);
			        	    	}
			        	    	//-------Generate PDF
			        	    	
			        	    	String filePath = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH"));
			        	    	String[] attachments = new String[4];
			        	    	String[] attachmentsFileName = new String[4];
			        	    	attachments[0] = filePath+"/"+creditAppFileName;
			        	    	attachmentsFileName[0] = creditAppFileName;
			        	    	if(CommonUtility.validateString(salesTaxExemptionCertificateFileName).length()>0 && CommonUtility.validateString(finacialStatmentFileName).length()>0){
			        	    		attachments[1] = filePath+"/"+salesTaxExemptionCertificateFileName;
			        	    		attachments[2] = filePath+"/"+finacialStatmentFileName;
			        	    		
			        	    		attachmentsFileName[1] = salesTaxExemptionCertificateFileName.replaceAll(sessionId+"_", "");
			        	    		attachmentsFileName[2] = finacialStatmentFileName.replaceAll(sessionId+"_", "");
			        	    		
			        	    	}else if(CommonUtility.validateString(salesTaxExemptionCertificateFileName).length()>0){
			        	    		attachments[1] = filePath+"/"+salesTaxExemptionCertificateFileName;
			        	    		attachmentsFileName[1] = salesTaxExemptionCertificateFileName.replaceAll(sessionId+"_", "");
			        	    	}else if(CommonUtility.validateString(finacialStatmentFileName).length()>0){
			        	    		attachments[1] = filePath+"/"+finacialStatmentFileName;
			        	    		attachmentsFileName[2] = finacialStatmentFileName.replaceAll(sessionId+"_", "");
			        	    	}
			        	    	
			        	    	if(CommonUtility.customServiceUtility()!=null) {
									CommonUtility.customServiceUtility().addBankAndTradeReferenceAttachments(attachments, attachmentsFileName, salesTaxExemptionCertificateFileName, 
											CommonUtility.validateString(formData.get("bankReferenceFileName")), CommonUtility.validateString(formData.get("tradeReferenceFileName")), 
											CommonUtility.validateString(filePath), CommonUtility.validateString(sessionId));
			        	    	}
			        	    	
			        	    	LinkedHashMap<String, Object>contentData = new LinkedHashMap<String, Object>();
			        	    	contentData.put("creditAplication", creditAplication);
			        	    	contentData.put("NotificationTemplateName", "CreditApplicationRequestMailToCustomer");//CommonDBQuery.getSystemParamtersList().get("CreditApplicationRequestMailToCustomer"));			        	    	
			        	    	contentData.put("EMAIL_TO", creditAplication.getDeclaratioEmailAddress());
			        	    	contentData.put("session", session);
			        	    	contentData.put("attachments", attachments);
			        	    	contentData.put("attachmentsFileName", attachmentsFileName);
			        	    	sentFlag = sendMailUtility.sendCreditApplicationRequestMail(userAddress, contentData);
			        	    	System.out.println("CreditApplicationRequestMailToCustomer : "+sentFlag);
			        	    	
			        	    	
			        	    	List<CustomTable> creditManagerList = CIMM2VelocityTool.getInstance().getCusomTableDataByFieldValue(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")),"WEBSITE",0,0,"CREDIT_MANAGERS","ZIP_CODE,",CommonUtility.validateString(billingZipCode2C).replaceAll("[^0-9]", ""),"SITE_NAME","WEBSITES");
			        	    	if(creditManagerList!=null && !creditManagerList.isEmpty() && creditManagerList.get(0)!=null && creditManagerList.get(0).getTableDetails().get(0)!=null){
			        	    		contentData.put("EMAIL_TO", creditManagerList.get(0).getTableDetails().get(0).get("EMAIL_ADDRESS"));
			        	    	}else{
			        	    		contentData.put("EMAIL_TO", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CREDIT_MANAGER_CONTACT")));
			        	    	}
			        	    	
			        	    	System.out.println("Email to Credit Application Manager : "+contentData.get("EMAIL_TO"));
			        	    	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("OVERRIDE_DEFAULT_CREDIT_REQUEST_MANAGER_EMAIL")).equalsIgnoreCase("Y")){
			        	    		contentData.put("EMAIL_TO", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("OVERRIDE_DEFAULT_CREDIT_REQUEST_MANAGER_CONTACT"))); //----------- Remove by the Go live Time
			        	    	}
				        	    System.out.println("Email to Credit Application Manager : "+contentData.get("EMAIL_TO"));
				        	    
			        	    	contentData.put("NotificationTemplateName", "CreditApplicationRequestMailToCreditManager");//CommonDBQuery.getSystemParamtersList().get("CreditApplicationRequestMailToCreditManager"));			        	    	
			        	    	sentFlag = sendMailUtility.sendCreditApplicationRequestMail(userAddress, contentData);
			        	    	System.out.println("CreditApplicationRequestMailToCreditManager : "+sentFlag);
			        	    	
			        	  }else{
			        		  if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_MAIL_TO_CUSTOMER")).equalsIgnoreCase("Y")){
								  sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"webUser","",userRegistrationDetail.getFormtype()); //"2B"
							  }
							  if(CommonUtility.validateString(userRegistrationDetail.getFormtype()).trim().equalsIgnoreCase("2C")){
								   sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"customer","",userRegistrationDetail.getFormtype()); //"2B"
							  }
			        	  }
						  
						  result = "1|Registration request sent successfully";
						  
					}else{
						result = usersObj.createCommertialCustomer(userRegistrationDetail);
						if(result.contains("successfully")){
							 if(CommonUtility.customServiceUtility()!=null) {
							result=CommonUtility.customServiceUtility().CreditAppRegistrationAndPdf(userRegistrationDetail,result,creditAplication,session);
						}
				    }
						else if(result.contains("connectionError")){
							result= "0|"+result;
						}
						else
						{
							result= "0|Error while registering the Customer";//For both Credit Application and Customer Registration -Aaron
						}
					}
				}
		renderContent = result;
	    }catch (Exception e) {
			 e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String guestUserRegistration(){

		request = ServletActionContext.getRequest();
		
		boolean isValidUser = true;
	    result = "";
	    HttpSession session = request.getSession();
	    SecureData validUserPass = new SecureData();
	    AddressModel billAddress = new AddressModel();
		AddressModel shipAddress = new AddressModel();
		UsersModel userDetails = new UsersModel();
		try{
			boolean isUserExistInERP = false;
			String userToken = "";
			String guestUserEmail = CommonUtility.validateString(auEmail);
			String guestUserEmailAddress = request.getParameter("guestEmail");
			if(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")!=null && CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD").length()>0){
				auPassword = validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GUSTE_CHECKOUT_USER_NAME")).length()>0){
				auEmail = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GUSTE_CHECKOUT_USER_NAME"));
			}
			
			if(isValidUser){
					
				if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y")){
					 auEmail = auEmail.toLowerCase();
				 }
					String erpLogin =  CommonDBQuery.getSystemParamtersList().get("ERPLOGIN");
					String isEclipseDown =  CommonDBQuery.getSystemParamtersList().get("ERPAVAILABLE");
					
					//---------- Billing Address
					billAddress.setFirstName(auBillFirstName);
					billAddress.setLastName(auBillLastName);
					if(CommonUtility.validateString(auBillCompanyName).length()>0){
						billAddress.setCompanyName(auBillCompanyName);
					}else{
						if(CommonUtility.validateString(auBillLastName).length()>0){
							billAddress.setCompanyName(auBillFirstName +" "+auBillLastName);
						}else{
							billAddress.setCompanyName(auBillFirstName);
						}
					}
					billAddress.setAddress1(auBillAddress1);
					billAddress.setAddress2(auBillAddress2);
					billAddress.setCity(auBillCity);
					billAddress.setState(auBillState);
					billAddress.setCountry(auBillCountry);
					billAddress.setPhoneNo(auBillPhoneNo);
					billAddress.setEmailAddress(auEmail);
					if(CommonUtility.validateString(auBillCountry).equalsIgnoreCase("USA")){
						billAddress.setCountry(CommonUtility.getCountryCode(auBillCountry, "Registration"));
					}else{
						billAddress.setCountry(auBillCountry);
					}
					billAddress.setZipCode(auBillZipCode);
					//billAddress.setPhoneNo(auBillPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
					//---------- Billing Address
					
					
					//---------- Shipping Address
					shipAddress.setFirstName(auShipFirstName);
					shipAddress.setLastName(auShipLastName);
					if(CommonUtility.validateString(auShipCompanyName).length()>0){
						shipAddress.setCompanyName(auShipCompanyName);
					}else{
						if(CommonUtility.validateString(auShipFirstName).length()>0){
							shipAddress.setCompanyName(auShipFirstName +" "+auShipLastName);
						}else{
							shipAddress.setCompanyName(auShipFirstName);
						}
					}
					shipAddress.setAddress1(auShipAddress1);
					shipAddress.setAddress2(auShipAddress2);
					shipAddress.setCity(auShipCity);
					shipAddress.setState(auShipState);
					shipAddress.setCountry(auShipCountry);
					shipAddress.setPhoneNo(auShipPhoneNo);
					if(CommonUtility.validateString(auShipCountry).equalsIgnoreCase("USA")){
						shipAddress.setCountry(CommonUtility.getCountryCode(auShipCountry, "Registration"));
					}else{
						shipAddress.setCountry(auShipCountry);
					}
					shipAddress.setZipCode(auShipZipCode);
					//shipAddress.setPhoneNo(auShipPhoneNo.replaceAll("[^a-zA-Z0-9]", ""));
					//---------- Shipping Address
					
					
					//---------- User Details
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GUSTE_CHECKOUT_USER_NAME")).length()>0){
						userDetails.setUserName(auEmail);
						userDetails.setEmailAddress(guestUserEmail);
					}else{
						userDetails.setUserName(auEmail);
						userDetails.setEmailAddress(auEmail);
					}
					
					if(request.getParameterMap().containsKey("auPassword") && CommonUtility.validateString(auPassword).length()==0){
						auPassword = validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
					}else if(CommonUtility.validateString(auPassword).length()==0){
						auPassword = validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
					}else{ 
						if(CommonUtility.validateString(auPassword).length()<8){isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.passwordMinimunCharacters")+"|";}
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
							if(!isAlfaNumericOnly(auPassword)){
								isValidUser=false; result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register2B.error.passwordCharacters")+"|";
							}
						}
					
					}
					userDetails.setErpOverrideFlag(CommonUtility.validateString(request.getParameter("erpOverrideFlag")));
					userDetails.setPassword(auPassword);
					userDetails.setFirstName(auBillFirstName);
					userDetails.setLastName(auBillLastName);
					userDetails.setPhoneNo(auBillPhoneNo.replaceAll("[^a-zA-Z0-9]", "").replaceAll("[^a-zA-Z0-9]", ""));
					userDetails.setNewsLetterSub(auNewsLetterSubscription);
					if(CommonUtility.validateString(auChkSameAsBilling).equalsIgnoreCase("Yes") || CommonUtility.validateString(auChkSameAsBilling).equalsIgnoreCase("On")){
						userDetails.setSameAsBillingAddress("Y");
					}else{
						userDetails.setSameAsBillingAddress("N");
					}
					userDetails.setBillAddress(billAddress);
					userDetails.setShipAddress(shipAddress);
					userDetails.setUserRole("Ecomm Retail User");
					userDetails.setUserStatus("Y");
					userDetails.setSameAsBillingAddress(auChkSameAsBilling);
					userDetails.setContactClassification(auContactClassification);
					userDetails.setCustomerType("G");
					//---------- User Details
					
					//---------- ERP check
					if(!CommonUtility.validateString(auPassword).equalsIgnoreCase(validUserPass.validatePassword(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"))))){
						isUserExistInERP = false;
					}else{
						if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
							
							activeCustomerId = "";
							
							userToken = LoginSubmit.ERPLOGIN(auEmail, validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")),CommonUtility.validateString(activeCustomerId));
							String connectionError = (String) session.getAttribute("connectionError");
							if(connectionError.equalsIgnoreCase("No") && isEclipseDown.equalsIgnoreCase("Y")){
					        	if(userToken!=null && !userToken.trim().equalsIgnoreCase("") ){
					        		isUserExistInERP = true;
					        	}else{
					        		isUserExistInERP = false;
					        	}
					        }else{
					        	result = result+ "Server Error. Please Try again later.|";
					        }
						}else{
							isUserExistInERP = false;
							try {
								UsersModel userModel = new UsersModel();
								userModel.setUserName(auEmail);
								userModel.setPassword(validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")));
								userModel.setUserStatus("Y");
								HashMap<String, String> checkUserExist = UsersDAO.checkUserExistInDBFromUserNameAndPassword(userModel);
								if(checkUserExist!=null && checkUserExist.size()>0){
									if(CommonUtility.validateString(checkUserExist.get("contactId")).length()>0){
										isUserExistInERP = true;
										userToken = CommonUtility.validateString(checkUserExist.get("contactId"));
									}
								}
							
							} catch (Exception e) {
								e.printStackTrace();
								result = result+ "Server Error. Please Try again later.|";
							}
						}
					}
					//---------- ERP check
				
				boolean setSession = false;
				if(session.getAttribute("erpType")!=null && CommonUtility.validateString(session.getAttribute("erpType").toString()).equalsIgnoreCase("defaults")){
					setSession = true;
				}
				String customerType="";
				boolean userExists = UsersDAO.checkForUserName(userDetails.getEmailAddress().toUpperCase());
				customerType = UsersDAO.checkForCustomerType(auEmail);
				System.out.println("customerType=="+customerType);
				if(isUserExistInERP || (userExists && CommonUtility.validateString(customerType).equalsIgnoreCase("G"))){
					setSession = true;
				}else if(userExists && !CommonUtility.validateString(customerType).equalsIgnoreCase("G")){
					result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("AuUser.UserExist.Error").replace("||~~||~~||", auEmail)+"|";
				}
				else{
						UserManagement userManager = new UserManagementImpl();
						if(CommonUtility.validateString(userDetails.getCustomerType()).equalsIgnoreCase("G") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_RETAIL_ERP_REGISTRATION")).equalsIgnoreCase("Y")){
						      if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID")).length() > 0) {
						      int buyingCompanyid = UsersDAO.getBuyingCompanyIdByEntityId(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID")));
						      userDetails.setBuyingCompanyId(buyingCompanyid);
						       }
						      }
						//-- Create a user for Anonymous Checkout
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GUSTE_CHECKOUT_USER_NAME")).length()<1){
							result = userManager.createWLUser(userDetails);
						}
						//-- Create a user for Anonymous Checkout
						
						if(CommonUtility.validateString(result.toLowerCase()).contains("already taken")){
							result = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("AnonymousCheckout.userexit.error").replace("||~~||~~||", auEmail)+"|";	
						}else if(!CommonUtility.validateString(result.toLowerCase()).contains("successfully")){
							System.out.println("Result Do not Contains success message : "+result);
						}else{
							setSession = true;
							if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
								activeCustomerId = "";
								userToken = LoginSubmit.ERPLOGIN(auEmail,auPassword, CommonUtility.validateString(activeCustomerId));
							}
						}
					
				}
				
				if(setSession){
					
					UserManagement usersObj = new UserManagementImpl();
					
					HashMap<String, String> userDetailsFromDB = UsersDAO.getUserPasswordAndUserId(auEmail, "Y");
					String defaultBillID = userDetailsFromDB.get("defaultBillingAddressId");
					String defaultShipID = userDetailsFromDB.get("defaultShippingAddressId");
					WarehouseModel warehouseDetails = UsersDAO.getWareHouseDetailsByCode(userDetailsFromDB.get("wareHouseCode"));
					HashMap<String, UsersModel> userAddressDB = usersObj.getUserAddressFromBCAddressBook(CommonUtility.validateNumber(defaultBillID), CommonUtility.validateNumber(defaultShipID));
				
					if(userAddressDB!=null){
						if(userAddressDB.get("Bill")!=null){
							billAddress.setEntityId(CommonUtility.validateString(userAddressDB.get("Bill").getEntityId()));
							billAddress.setAddressBookId(userAddressDB.get("Bill").getAddressBookId());
							billAddress.setAddressType(userAddressDB.get("Bill").getAddressType());
						}
						
						if(userAddressDB.get("Ship")!=null){
							shipAddress.setEntityId(CommonUtility.validateString(userAddressDB.get("Ship").getEntityId()));
							shipAddress.setShipToId(userAddressDB.get("Ship").getShipToId());
							shipAddress.setAddressBookId(userAddressDB.get("Ship").getAddressBookId());
							shipAddress.setAddressType(userAddressDB.get("Ship").getAddressType());
							//shipAddress.setZipCode(userAddressDB.get("Ship").getZipCodeStringFormat());
						}
					}
					
					userDetails.setEntityId(userDetailsFromDB.get("billingEntityId"));
					userDetails.setBillAddress(billAddress);
					userDetails.setShipAddress(shipAddress);
					userDetails.setWebsite(CommonDBQuery.webSiteName);
					userDetails.setCustomerName(userDetailsFromDB.get("loginCustomerName"));
					userDetails.setBuyingCompanyId(CommonUtility.validateNumber(userDetailsFromDB.get("buyingCompanyId")));
					userDetails.setSubsetId(CommonUtility.validateNumber(userDetailsFromDB.get("userSubsetId")));
					userDetails.setUserId(CommonUtility.validateNumber(userDetailsFromDB.get("userId")));
					userDetails.setWareHouseCodeStr(CommonUtility.validateString(userDetailsFromDB.get("wareHouseCode")));
					if(warehouseDetails != null){
						userDetails.setWareHouseName(warehouseDetails.getWareHouseName());
					}
					userDetails.setCountry(CommonUtility.validateString(userDetailsFromDB.get("customerCountry")));
					userDetails.setEmailAddress(CommonUtility.validateString(userDetailsFromDB.get("userEmailAddress")));
					userDetails.setOfficePhone(CommonUtility.validateString(userDetailsFromDB.get("userOfficePhone")));
					if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
						userDetails.setUserToken(CommonUtility.validateString(userToken));
					}else{
						userDetails.setUserToken(CommonUtility.validateString(userDetailsFromDB.get("contactId")));
					}
					
					if(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT")!=null && CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT").trim().equalsIgnoreCase("Y")){
						String homeBranchZipCode = CommonUtility.validateString(userDetails.getShipAddress().getZipCode());
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
					
					userDetails.setRequestType("webOrder");
					usersObj.assignErpValues(userDetails);
					if(CommonUtility.validateString(userDetails.getBranchID()).length()<1 && session.getAttribute("homeBranchId")!=null && CommonUtility.validateString(session.getAttribute("homeBranchId").toString()).length()>0){
						userDetails.setBranchID(CommonUtility.validateString(session.getAttribute("homeBranchId").toString()));
					}
					session.setAttribute("auUserDetails", userDetails);
					session.setAttribute("auUserLogin", "Y");
					result = "5|"+auBillFirstName + " "+auBillLastName;
				}
			}
			renderContent = result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public static boolean isAlfaNumericOnly(String value){  
		boolean isValid = false; 
		try{
			String expression = "[A-Za-z0-9]+";
			CharSequence inputStr = value;  
			Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
			Matcher matcher = pattern.matcher(inputStr);  
			if(matcher.matches()){  
				isValid = true;  
			}  
			System.out.println(value+" isAlfaNumericOnly "+isValid);
		}catch(Exception e){
			e.printStackTrace();
		}
		return isValid;  
	}
	
	public String checkUserExistInDataBase(){
		result = "N";
		try {
			SecureData validUserPass = new SecureData();
			UsersModel userModel = new UsersModel();
			String customerType = "";
			String customerId = "";
			userModel.setUserName(CommonUtility.validateString(auEmail));
			userModel.setPassword(validUserPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")));
			userModel.setUserStatus("Y");
			HashMap<String, String> checkUserExist = UsersDAO.checkUserExistInDBFromUserNameAndPassword(userModel);
			if(checkUserExist!=null && checkUserExist.size()>0){
				if(CommonUtility.validateNumber(checkUserExist.get("userId"))>0){
					result = "Y";
				}
				customerType = checkUserExist.get("customertype");
			if(customerType!=null && !customerType.equalsIgnoreCase("") && customerType.equalsIgnoreCase("G")){
				customerId = UsersDAO.getCustomerAccount(CommonUtility.validateString(auEmail));
				result = "Y";
			}
			else if(UsersDAO.isRegisteredUser(CommonUtility.validateString(auEmail).toUpperCase())){
				result = "AR";
			}
			else
			{
				result = "AR";
			}
		} 
		else
		{
			result = "N";
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = "Server Error. Please Try again later.";
		}
		renderContent = CommonUtility.validateString(result);
		return SUCCESS;
	}
	
	public String existingWithoutCustomerNoRegistration() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		result = "";
		renderContent = "";
		UsersModel userDetails = new UsersModel();
		boolean userExist = false;
		String customerType = "";
		boolean mail = false;
		Connection conn = null;
		int buyingCompany = 0;
		int userId = 0;
		try {

			if (CommonDBQuery.getSystemParamtersList().get("CAPTCHA_REQUIRED_ON_REGISTRATION") != null && CommonDBQuery.getSystemParamtersList().get("CAPTCHA_REQUIRED_ON_REGISTRATION").equalsIgnoreCase("Y")) {
				String userCaptchaResponse = request.getParameter("jcaptcha");
				if (CommonUtility.validateString(userCaptchaResponse).length() > 0) {
					String captcha = (String) session.getAttribute("captcha");
					if (captcha != null && userCaptchaResponse != null) {
						if (captcha.equals(userCaptchaResponse)) {
							System.out.println("Captcha Passed");
						} else {
							result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form1B.label.invalidCaptcha") + "|";
						}
					}
				} else {
					result = result + LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("form1B.label.enterCaptcha") + "|";
				}
			}

			userExist = UsersDAO.checkForUserName(emailAddress1B);
			customerType = UsersDAO.checkForCustomerType(emailAddress1B);
			if (userExist && customerType != null && !customerType.equalsIgnoreCase("G")) {
				result = result + "0|"+ LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.emailAddressExists")+ "|";
			} else {
				if (customerType != null && !customerType.equalsIgnoreCase("") && customerType.equalsIgnoreCase("G")) {
					int buyingCompanyId = UsersDAO.getBuyingCompanyIdByUserName(emailAddress1B);
					UsersDAO.deleteExistingGuestUser(emailAddress1B);
					UsersDAO.deleteExistingCustomer(buyingCompanyId);
				}
				userDetails.setEntityName(companyName1B);
				userDetails.setEntityId(CommonUtility.validateString(accountNo1B));
				userDetails.setAccountName(accountNo1B);
				userDetails.setFirstName(firstName1B);
				userDetails.setLastName(lastName1B);
				userDetails.setEmailAddress(emailAddress1B);
				userDetails.setPassword(newPassword1B);
				userDetails.setRequestAuthorizationLevel(requestAuthorization1B);
				userDetails.setSalesContact(salesContact1B);
				if(CommonUtility.validateString(companyBillingAddress1B).equalsIgnoreCase("")) {
					userDetails.setAddress1("No Address");
				}else {
					userDetails.setAddress1(companyBillingAddress1B);
				}
				userDetails.setAddress2(suiteNo1B);
				userDetails.setCity(cityName1B);
				userDetails.setState(stateName2AB);
				userDetails.setCountry(CommonUtility.validateString(countryName1B));
				userDetails.setZipCode(zipCode1B);
				if (CommonUtility.validateString((CommonDBQuery.getSystemParamtersList().get("B2B_ON_ACCOUNT_DEFAULT_USER_ROLE"))).length() > 0) {
					userDetails.setUserRole(CommonDBQuery.getSystemParamtersList().get("B2B_ON_ACCOUNT_DEFAULT_USER_ROLE"));
				} else {
					userDetails.setUserRole("Ecomm Customer Auth Purchase Agent");
				}
				userDetails.setPoNumber(poNumber1b);
				userDetails.setInvoiceNumber(invoice1b);
				if (phoneNo1B != null) {
					userDetails.setPhoneNo(CommonUtility.validateString(phoneNo1B.replaceAll("[^a-zA-Z0-9]", "")));
				}
				userDetails.setNewsLetterSub(newsLetterSub1B);
				userDetails.setTermsAndCondition(form1BPrivacyAndTermsCheckBoxRequired);
				userDetails.setSameAsCustomerAddress(CommonUtility.validateString(sameAsCustomerAddress));
				userDetails.setValidateZipcode(validateZipcode);
				userDetails.setUserStatus("I");
				if (CommonUtility.validateString(jobTitle1B).length() > 0) {
					userDetails.setJobTitle(jobTitle1B);
				}
				if (!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y")) {
					emailAddress1B = emailAddress1B.toLowerCase();
				}

				userDetails.setSession(session);

				LinkedHashMap<String, String> userRegisteration = new LinkedHashMap<String, String>();
				userRegisteration.put("companyName1B", companyName1B);
				userRegisteration.put("accountNo1B", accountNo1B);
				userRegisteration.put("firstName1B", firstName1B);
				userRegisteration.put("lastName1B", lastName1B);
				// userRegisteration.put("emailAddress1B",emailAddress1B.toLowerCase());
				userRegisteration.put("emailAddress1B", emailAddress1B);
				userRegisteration.put("newPassword1B", newPassword1B);
				userRegisteration.put("requestAuthorization1B", requestAuthorization1B);
				userRegisteration.put("salesContact1B", salesContact1B);
				userRegisteration.put("companyBillingAddress1B", companyBillingAddress1B);
				userRegisteration.put("suiteNo1B", suiteNo1B);
				userRegisteration.put("cityName1B", cityName1B);
				userRegisteration.put("stateName1B", stateName2AB);
				userRegisteration.put("countryName1B", countryName1B);
				userRegisteration.put("zipCode1B", zipCode1B);
				userRegisteration.put("phoneNo1B", phoneNo1B);
				userRegisteration.put("newsLetterSub1B", newsLetterSub1B);
				userRegisteration.put("form1BPrivacyAndTermsCheckBoxRequired", form1BPrivacyAndTermsCheckBoxRequired);
				userRegisteration.put("Status", userDetails.getUserStatus());
				userRegisteration.put("invoice1b", invoice1b);

				if (CommonUtility.validateString(jobTitle1B).length() > 0) {
					userRegisteration.put("jobTitle1B", jobTitle1B);
				}
				SaveCustomFormDetails saveForm = new SaveCustomFormDetails();
				saveForm.saveToDataBase(userRegisteration, "RegisterationForm1B");
				try {
					conn = ConnectionManager.getDBConnection();
					conn.setAutoCommit(false);

				} catch (SQLException e) {
					e.printStackTrace();
				}
				buyingCompany = UsersDAO.insertBuyingCompany(conn, userDetails, 0);
				if (buyingCompany > 0) {
					userDetails.setBuyingCompanyId(buyingCompany);
					int defaultBillId = UsersDAO.insertNewAddressintoBCAddressBook(conn, userDetails, "Bill");
					int shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, userDetails, "Ship");
					userId = UsersDAO.insertNewUser(conn, userDetails, emailAddress1B, newPassword1B, buyingCompany,
							"0", true);
					if(userId>0) {
						UsersDAO.updateUserAddressBook(conn,defaultBillId, shipId, userId,false);
						mail = true;
					}
				}
				System.out.println("userId inserted to DB- " + userId);

				if (userId > 0 && buyingCompany > 0 && mail) {
					result = "1|Registration request submitted successfully.";
					SendMailUtility sendMail = new SendMailUtility();
					// send mail to nce to create customer account
					if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_MAIL_TO_CUSTOMER")).equalsIgnoreCase("Y")) {
						mail = sendMail.sendRegistrationMail(userDetails, "webUser", "", "1B");
					}
					mail = sendMail.sendRegistrationMail(userDetails, "customer", "", "1B");
				} else {
					result = "0|Error while registering. Contact our customer service for further assistance.";
				}
			}
			renderContent = result;
		} catch (Exception e) {
			e.printStackTrace();
			target = ERROR;
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			ConnectionManager.closeDBConnection(conn);
		}
		return SUCCESS;
	}
	
	public String validateAvalaraAddress() {
		try {
			request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			UnilogFactoryInterface serviceProvider = UnilogEcommFactory.getInstance()
					.getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
			UsersModel shipAddressInfo = new UsersModel();

			if (CommonUtility.validateString(avalaraAddressValForm).length() > 0
					&& LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode"))
							.getProperty("form.avalara.address.commercial.user").equals(avalaraAddressValForm)) {
				shipAddressInfo.setAddress1(billingAddress2AB);
				shipAddressInfo.setAddress2(suiteNo2AB);
				shipAddressInfo.setCity(cityName2AB);
				shipAddressInfo.setState(stateName2AB);
				shipAddressInfo.setZipCode(zipCode2AB);
				shipAddressInfo.setCountry(countryName2AB);
			} else if (CommonUtility.validateString(avalaraAddressValForm).length() > 0
					&& LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode"))
							.getProperty("form.avalara.address.guest.user").equals(avalaraAddressValForm)) {
				shipAddressInfo.setAddress1(auShipAddress1);
				shipAddressInfo.setAddress2(auShipAddress2);
				shipAddressInfo.setCity(auShipCity);
				shipAddressInfo.setState(auShipState);
				shipAddressInfo.setZipCode(auShipZipCode);
				shipAddressInfo.setCountry(auShipCountry);
			}else if (CommonUtility.validateString(avalaraAddressValForm).length() > 0
					&& LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode"))
					.getProperty("form.avalara.address.retail.user").equals(avalaraAddressValForm)) {
				shipAddressInfo.setAddress1(shipAddress1);
				shipAddressInfo.setAddress2(shipAddress2);
				shipAddressInfo.setCity(shipCity);
				shipAddressInfo.setState(shipState);
				shipAddressInfo.setZipCode(shipZipCode);
				shipAddressInfo.setCountry(shipCountry);
			} else {
				renderContent = "0|Form identifier is missing";
				return SUCCESS;
			}

			if (serviceProvider != null) {
				String validAddress = CommonUtility.customServiceUtility().validateAvaAddress(shipAddressInfo);
				if (validAddress != null && validAddress.length() > 0) {
					renderContent = validAddress;
				} else {
					renderContent = "1|ValidAddress";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getCheckJobTitle() {
		return checkJobTitle;
	}
	public void setCheckJobTitle(String checkJobTitle) {
		this.checkJobTitle = checkJobTitle;
	}

	public String getUserStatus() {
		return userStatus;
	}
	
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	
	public String getCimm_User_Id() {
		return cimm_User_Id;
	}
	
	public void setCimm_User_Id(String cimm_User_Id) {
		this.cimm_User_Id = cimm_User_Id;
	}
	
	public String getUser_Name() {
		return user_Name;
	}
	
	public void setUser_Name(String user_Name) {
		this.user_Name = user_Name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAmountOfCredit() {
		return amountOfCredit;
	}
	public void setAmountOfCredit(String amountOfCredit) {
		this.amountOfCredit = amountOfCredit;
	}
	public String[] getBankReferenceAddress() {
		return bankReferenceAddress;
	}
	public void setBankReferenceAddress(String[] bankReferenceAddress) {
		this.bankReferenceAddress = bankReferenceAddress;
	}
	public String[] getBankReferenceCity() {
		return bankReferenceCity;
	}
	public void setBankReferenceCity(String[] bankReferenceCity) {
		this.bankReferenceCity = bankReferenceCity;
	}
	public String[] getBankReferenceZipCode() {
		return bankReferenceZipCode;
	}
	public void setBankReferenceZipCode(String[] bankReferenceZipCode) {
		this.bankReferenceZipCode = bankReferenceZipCode;
	}
	public String[] getPrincipalOfficeAddress() {
		return principalOfficeAddress;
	}
	public void setPrincipalOfficeAddress(String[] principalOfficeAddress) {
		this.principalOfficeAddress = principalOfficeAddress;
	}
	public String[] getPrincipalOfficeCity() {
		return principalOfficeCity;
	}
	public void setPrincipalOfficeCity(String[] principalOfficeCity) {
		this.principalOfficeCity = principalOfficeCity;
	}
	public String[] getPrincipalOfficeZipCode() {
		return principalOfficeZipCode;
	}
	public void setPrincipalOfficeZipCode(String[] principalOfficeZipCode) {
		this.principalOfficeZipCode = principalOfficeZipCode;
	}
	public String[] getPrincipalOfficeHomePhone() {
		return principalOfficeHomePhone;
	}
	public void setPrincipalOfficeHomePhone(String[] principalOfficeHomePhone) {
		this.principalOfficeHomePhone = principalOfficeHomePhone;
	}
	public String[] getPrincipalOfficeSocSecNo() {
		return principalOfficeSocSecNo;
	}
	public void setPrincipalOfficeSocSecNo(String[] principalOfficeSocSecNo) {
		this.principalOfficeSocSecNo = principalOfficeSocSecNo;
	}
	public String[] getPrincipalOfficeSpouseName() {
		return principalOfficeSpouseName;
	}
	public void setPrincipalOfficeSpouseName(String[] principalOfficeSpouseName) {
		this.principalOfficeSpouseName = principalOfficeSpouseName;
	}
	public String[] getOtherStates() {
		return otherStates;
	}
	public void setOtherStates(String[] otherStates) {
		this.otherStates = otherStates;
	}
	public String[] getOtherStatesValues() {
		return otherStatesValues;
	}
	public void setOtherStatesValues(String[] otherStatesValues) {
		this.otherStatesValues = otherStatesValues;
	}
	public String[] getChargeCardName() {
		return chargeCardName;
	}
	public void setChargeCardName(String[] chargeCardName) {
		this.chargeCardName = chargeCardName;
	}
	public String[] getChargeCardExpDate() {
		return chargeCardExpDate;
	}
	public void setChargeCardExpDate(String[] chargeCardExpDate) {
		this.chargeCardExpDate = chargeCardExpDate;
	}
	public String getAddtionalInformation() {
		return addtionalInformation;
	}
	public void setAddtionalInformation(String addtionalInformation) {
		this.addtionalInformation = addtionalInformation;
	}
	public String getBillShipInstruction() {
		return billShipInstruction;
	}
	public void setBillShipInstruction(String billShipInstruction) {
		this.billShipInstruction = billShipInstruction;
	}
	public String[] getTradeReferenceAddress() {
		return tradeReferenceAddress;
	}
	public void setTradeReferenceAddress(String[] tradeReferenceAddress) {
		this.tradeReferenceAddress = tradeReferenceAddress;
	}
	public String[] getTradeReferenceCity() {
		return tradeReferenceCity;
	}
	public void setTradeReferenceCity(String[] tradeReferenceCity) {
		this.tradeReferenceCity = tradeReferenceCity;
	}
	public String[] getTradeReferenceZipCode() {
		return tradeReferenceZipCode;
	}
	public void setTradeReferenceZipCode(String[] tradeReferenceZipCode) {
		this.tradeReferenceZipCode = tradeReferenceZipCode;
	}
	public String[] getTradeReferenceAccountNumber() {
		return tradeReferenceAccountNumber;
	}
	public void setTradeReferenceAccountNumber(String[] tradeReferenceAccountNumber) {
		this.tradeReferenceAccountNumber = tradeReferenceAccountNumber;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getGrandtype() {
		return grandtype;
	}
	public void setGrandtype(String grandtype) {
		this.grandtype = grandtype;
	}
	public String getShippingOrgType() {
		return shippingOrgType;
	}
	public void setShippingOrgType(String shippingOrgType) {
		this.shippingOrgType = shippingOrgType;
	}
	public String getAvalaraAddressValForm() {
		return avalaraAddressValForm;
	}
	public void setAvalaraAddressValForm(String avalaraAddressValForm) {
		this.avalaraAddressValForm = avalaraAddressValForm;
	}
	public String getIstaxable() {
		return istaxable;
	}
	public void setIstaxable(String istaxable) {
		this.istaxable = istaxable;
	}
	public String getCreditDate() {
		return creditDate;
	}
	public void setCreditDate(String creditDate) {
		this.creditDate = creditDate;
	}
	public String getContactWebsite() {
		return contactWebsite;
	}
	public void setContactWebsite(String contactWebsite) {
		this.contactWebsite = contactWebsite;
	}
	public String getContactTitle() {
		return contactTitle;
	}
	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

}