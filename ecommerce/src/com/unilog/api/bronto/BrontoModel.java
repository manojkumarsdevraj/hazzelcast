package com.unilog.api.bronto;

import java.util.ArrayList;
import java.util.Date;

import com.unilog.sales.SalesModel;

public class BrontoModel {

	private String firstName; 
	private String lastName;
	private String userName; 
	private String address1; 
	private String address2; 
	private String city; 
	private String state; 
	private String zipcode; 
	private String password; 
	private String company; 
	private String securityQuestion; 
	private String securityAnswer; 
	private String email; 
	private String phone; 
	private String language; 
	private String country; 
	private boolean interestedInCommercialDiscounts;  
	private String taxId; 
	private String businessType; 
	private String businessTypeOther; 
	private String fax; 
	private String howDidYouHearAboutUs; 
	private String brandsCommonlyPurchased; 
	private String brandsCommonlyPurchasedOthers; 
	private String annualPartsPurchase;
	private int sxCustomerNumber;
	private SalesModel orderDetail;
	private ArrayList<SalesModel> orderItemList;
	private String emailType;
	private int userId;
	private int subsetId;
	private String secretKey;
	private boolean recieveEmailOffers;
	private String sessionId;
	private boolean beforeLoginUser;
	private String ContentGroup;
	private String Source;
	private boolean HasBought;
	private String CouponTotalDiscounts;
	private Date OrderFirstDate;
	private String OrderMostRecentShipDate;
	private Date OrderLastDate;
	private String OrderTotalNumber;
	private String CouponFirstCode;
	private String CouponTotalNumber;
	private String OrderAverageTotal;
	private String OrderLastTotal;
	private String OrderTotalRevenue;
	private String CouponLastCode;
	
	public SalesModel getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(SalesModel orderDetail) {
		this.orderDetail = orderDetail;
	}
	public ArrayList<SalesModel> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(ArrayList<SalesModel> orderItemList) {
		this.orderItemList = orderItemList;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getBusinessTypeOther() {
		return businessTypeOther;
	}
	public void setBusinessTypeOther(String businessTypeOther) {
		this.businessTypeOther = businessTypeOther;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getHowDidYouHearAboutUs() {
		return howDidYouHearAboutUs;
	}
	public void setHowDidYouHearAboutUs(String howDidYouHearAboutUs) {
		this.howDidYouHearAboutUs = howDidYouHearAboutUs;
	}
	public String getBrandsCommonlyPurchased() {
		return brandsCommonlyPurchased;
	}
	public void setBrandsCommonlyPurchased(String brandsCommonlyPurchased) {
		this.brandsCommonlyPurchased = brandsCommonlyPurchased;
	}
	public String getBrandsCommonlyPurchasedOthers() {
		return brandsCommonlyPurchasedOthers;
	}
	public void setBrandsCommonlyPurchasedOthers(
			String brandsCommonlyPurchasedOthers) {
		this.brandsCommonlyPurchasedOthers = brandsCommonlyPurchasedOthers;
	}
	public String getAnnualPartsPurchase() {
		return annualPartsPurchase;
	}
	public void setAnnualPartsPurchase(String annualPartsPurchase) {
		this.annualPartsPurchase = annualPartsPurchase;
	}
	public int getSxCustomerNumber() {
		return sxCustomerNumber;
	}
	public void setSxCustomerNumber(int sxCustomerNumber) {
		this.sxCustomerNumber = sxCustomerNumber;
	}
	public String getEmailType() {
		return emailType;
	}
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getSubsetId() {
		return subsetId;
	}
	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public boolean isInterestedInCommercialDiscounts() {
		return interestedInCommercialDiscounts;
	}
	public void setInterestedInCommercialDiscounts(
			boolean interestedInCommercialDiscounts) {
		this.interestedInCommercialDiscounts = interestedInCommercialDiscounts;
	}
	public boolean isRecieveEmailOffers() {
		return recieveEmailOffers;
	}
	public void setRecieveEmailOffers(boolean recieveEmailOffers) {
		this.recieveEmailOffers = recieveEmailOffers;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public boolean isBeforeLoginUser() {
		return beforeLoginUser;
	}
	public void setBeforeLoginUser(boolean beforeLoginUser) {
		this.beforeLoginUser = beforeLoginUser;
	}
	public String getContentGroup() {
		return ContentGroup;
	}
	public void setContentGroup(String contentGroup) {
		ContentGroup = contentGroup;
	}
	public String getSource() {
		return Source;
	}
	public void setSource(String source) {
		Source = source;
	}
	public boolean isHasBought() {
		return HasBought;
	}
	public void setHasBought(boolean hasBought) {
		HasBought = hasBought;
	}
	public String getCouponTotalDiscounts() {
		return CouponTotalDiscounts;
	}
	public void setCouponTotalDiscounts(String couponTotalDiscounts) {
		CouponTotalDiscounts = couponTotalDiscounts;
	}
	public Date getOrderFirstDate() {
		return OrderFirstDate;
	}
	public void setOrderFirstDate(Date orderFirstDate) {
		OrderFirstDate = orderFirstDate;
	}
	public String getOrderMostRecentShipDate() {
		return OrderMostRecentShipDate;
	}
	public void setOrderMostRecentShipDate(String orderMostRecentShipDate) {
		OrderMostRecentShipDate = orderMostRecentShipDate;
	}
	public Date getOrderLastDate() {
		return OrderLastDate;
	}
	public void setOrderLastDate(Date orderLastDate) {
		OrderLastDate = orderLastDate;
	}
	public String getOrderTotalNumber() {
		return OrderTotalNumber;
	}
	public void setOrderTotalNumber(String orderTotalNumber) {
		OrderTotalNumber = orderTotalNumber;
	}
	public String getCouponFirstCode() {
		return CouponFirstCode;
	}
	public void setCouponFirstCode(String couponFirstCode) {
		CouponFirstCode = couponFirstCode;
	}
	public String getCouponTotalNumber() {
		return CouponTotalNumber;
	}
	public void setCouponTotalNumber(String couponTotalNumber) {
		CouponTotalNumber = couponTotalNumber;
	}
	public String getOrderAverageTotal() {
		return OrderAverageTotal;
	}
	public void setOrderAverageTotal(String orderAverageTotal) {
		OrderAverageTotal = orderAverageTotal;
	}
	public String getOrderLastTotal() {
		return OrderLastTotal;
	}
	public void setOrderLastTotal(String orderLastTotal) {
		OrderLastTotal = orderLastTotal;
	}
	public String getOrderTotalRevenue() {
		return OrderTotalRevenue;
	}
	public void setOrderTotalRevenue(String orderTotalRevenue) {
		OrderTotalRevenue = orderTotalRevenue;
	}
	public String getCouponLastCode() {
		return CouponLastCode;
	}
	public void setCouponLastCode(String couponLastCode) {
		CouponLastCode = couponLastCode;
	} 
}
