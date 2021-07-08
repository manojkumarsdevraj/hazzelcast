package com.unilog.users;

import javax.servlet.http.HttpSession;

import com.unilog.api.bronto.BrontoModel;

public class AddressModel {

	private String firstName;
	private String lastName;
	private String userPassword;
	private String companyName;
	private String address1;
	private String address2;
	private String addressType;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String phoneNo;
	private String emailAddress;
	private String locUser;
	private String role;
	private String buyingComanyIdStr;
	private String parentUserId;
	private String entityId;
	private String userName;
	private String formtype;
	private boolean isUpdateRole;
	private String userStatus;
	private String newsLetterSub;
	private String contactClassification;
	private int addressBookId;
	private HttpSession session;
	private String salesRepContact;
	private String jobTitle;
	private String phoneDescription;
	private String phoneCode;
	private String faxNumber;
	private String shipToId;
	private String shipToName;
	private boolean anonymousUser;
	private AddressModel shippingAddress;
	private String disableSubmitPO;
	private String disableSubmitPOCC;
	private String existingUser;
	private String userToken;
	private BrontoModel brontoDetails;
	private String[] userCustomFields;
	private CreditApplicationModel creditApplicationModel;
	private StringBuilder formHtmlDetails;
	private String webOEUserName;
	private String webOEPassword;
	private String activeCustomerId;
	private String creditApplicationRequest="N";
	private int shippingBranch;
	private String termsType;
	private String customerType;
	private String erpOverrideFlag;
	private String checkSameAsBilling;
	private String customFieldData;
	private AddressModel billingAddress;
	private String primaryAddress;
	private String wareHouseCode;
	private String accountName;
	private boolean guestRegistrationRequired;
	private String wareHouseName;
	private String currencyCode;
	private String addressERPId;
	private String countryCode;
	private String subsetFlag;
	private String howWebsiteContact;
	private String isTaxable;
	private String birthMonth;
	private String isRewardMember;
	private String address3;
	private String creditDate;
	private String contactTitle;
	private String contactWebsite;
	
	
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
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
	public String getHowWebsiteContact() {
		return howWebsiteContact;
	}
	public void setHowWebsiteContact(String howWebsiteContact) {
		this.howWebsiteContact = howWebsiteContact;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCheckSameAsBilling() {
		return checkSameAsBilling;
	}
	public void setCheckSameAsBilling(String checkSameAsBilling) {
		this.checkSameAsBilling = checkSameAsBilling;
	}
	public AddressModel getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(AddressModel billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getCreditApplicationRequest() {
		return creditApplicationRequest;
	}
	public void setCreditApplicationRequest(String creditApplicationRequest) {
		this.creditApplicationRequest = creditApplicationRequest;
	}
	public String getActiveCustomerId() {
		return activeCustomerId;
	}
	public void setActiveCustomerId(String activeCustomerId) {
		this.activeCustomerId = activeCustomerId;
	}
	public String getWebOEUserName() {
		return webOEUserName;
	}
	public void setWebOEUserName(String webOEUserName) {
		this.webOEUserName = webOEUserName;
	}
	public String getWebOEPassword() {
		return webOEPassword;
	}
	public void setWebOEPassword(String webOEPassword) {
		this.webOEPassword = webOEPassword;
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
	public BrontoModel getBrontoDetails() {
		return brontoDetails;
	}
	public void setBrontoDetails(BrontoModel brontoDetails) {
		this.brontoDetails = brontoDetails;
	}
	public String[] getUserCustomFields() {
		return userCustomFields;
	}
	public void setUserCustomFields(String[] userCustomFields) {
		this.userCustomFields = userCustomFields;
	}
	public AddressModel getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(AddressModel shippingAddress) {
		this.shippingAddress = shippingAddress;
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
	public String getPhoneDescription() {
		return phoneDescription;
	}
	public void setPhoneDescription(String phoneDescription) {
		this.phoneDescription = phoneDescription;
	}
	public String getPhoneCode() {
		return phoneCode;
	}
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
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
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public String getContactClassification() {
		return contactClassification;
	}
	public void setContactClassification(String contactClassification) {
		this.contactClassification = contactClassification;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public boolean isUpdateRole() {
		return isUpdateRole;
	}
	public void setUpdateRole(boolean isUpdateRole) {
		this.isUpdateRole = isUpdateRole;
	}
	public String getFormtype() {
		return formtype;
	}
	public void setFormtype(String formtype) {
		this.formtype = formtype;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getParentUserId() {
		return parentUserId;
	}
	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}
	public String getBuyingComanyIdStr() {
		return buyingComanyIdStr;
	}
	public void setBuyingComanyIdStr(String buyingComanyIdStr) {
		this.buyingComanyIdStr = buyingComanyIdStr;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getLocUser() {
		return locUser;
	}
	public void setLocUser(String locUser) {
		this.locUser = locUser;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setNewsLetterSub(String newsLetterSub) {
		this.newsLetterSub = newsLetterSub;
	}
	public String getNewsLetterSub() {
		return newsLetterSub;
	}
	public void setAddressBookId(int addressBookId) {
		this.addressBookId = addressBookId;
	}
	public int getAddressBookId() {
		return addressBookId;
	}
	public String getShipToId() {
		return shipToId;
	}
	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}
	public String getShipToName() {
		return shipToName;
	}
	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
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
	public String getExistingUser() {
		return existingUser;
	}
	public void setExistingUser(String existingUser) {
		this.existingUser = existingUser;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getUserToken() {
		return userToken;
	}
	public int getShippingBranch() {
		return shippingBranch;
	}
	public void setShippingBranch(int shippingBranch) {
		this.shippingBranch = shippingBranch;
	}
	public String getTermsType() {
		return termsType;
	}
	public void setTermsType(String termsType) {
		this.termsType = termsType;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getErpOverrideFlag() {
		return erpOverrideFlag;
	}
	public void setErpOverrideFlag(String erpOverrideFlag) {
		this.erpOverrideFlag = erpOverrideFlag;
	}
	public String getCustomFieldData() {
		return customFieldData;
	}
	
	public void setCustomFieldData(String customFieldData) {
		this.customFieldData = customFieldData;
	}
	public String getPrimaryAddress() {
		return primaryAddress;
	}
	public void setPrimaryAddress(String primaryAddress) {
		this.primaryAddress = primaryAddress;
	}
	public String getWareHouseCode() {
		return wareHouseCode;
	}
	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}
	public boolean isGuestRegistrationRequired() {
		return guestRegistrationRequired;
	}
	public void setGuestRegistrationRequired(boolean guestRegistrationRequired) {
		this.guestRegistrationRequired = guestRegistrationRequired;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getWareHouseName() {
		return wareHouseName;
	}
	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}	
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getAddressERPId() {
		return addressERPId;
	}
	public void setAddressERPId(String addressERPId) {
		this.addressERPId = addressERPId;
	}
	public String getSubsetFlag() {
		return subsetFlag;
	}
	public void setSubsetFlag(String subsetFlag) {
		this.subsetFlag = subsetFlag;
	}
	public String getIsTaxable() {
		return isTaxable;
	}
	public void setIsTaxable(String isTaxable) {
		this.isTaxable = isTaxable;
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
