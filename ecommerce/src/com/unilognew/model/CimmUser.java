package com.unilognew.model;

import java.io.Serializable;
import java.util.Date;

public class CimmUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5442182455527519443L;
	
	private int userId;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String middleName;
	private int buyingCompanyId;
	private String officePhone;
	private String cellPhone;
	private String fax;
	private String email;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String country;
	private String zip;
	private int eclipseContactId;
	private int externalSystemsUserId;
	private Date registeredDate;
	private int defaultBillingAddressId;
	private int defaultShippingAddressId;
	private String isTaxable;
	private int userEdited;
	private Date updatedDateTime;
	private String punchoutUser;
	private String firstLogin;
	private String status;
	private String acceptOrderByPONumber;
	private String keywords;
	private String supplierSpecific;
	private int parentUserId;
	private String existingCustomer;
	private String webservice;
	private String approvalUserLogin;
	private int approvalUserId;   
	private int siteId;  
	private String termsType;
	private String termsTypeDescription;
	private String shipVia;
	private String shipViaDescription;
	private int wflPhaseId;
	private String jobTitle;
	private String salesRepContact;
	
	
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the buyingCompanyId
	 */
	public int getBuyingCompanyId() {
		return buyingCompanyId;
	}
	/**
	 * @param buyingCompanyId the buyingCompanyId to set
	 */
	public void setBuyingCompanyId(int buyingCompanyId) {
		this.buyingCompanyId = buyingCompanyId;
	}
	/**
	 * @return the officePhone
	 */
	public String getOfficePhone() {
		return officePhone;
	}
	/**
	 * @param officePhone the officePhone to set
	 */
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	/**
	 * @return the cellPhone
	 */
	public String getCellPhone() {
		return cellPhone;
	}
	/**
	 * @param cellPhone the cellPhone to set
	 */
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}
	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}
	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the eclipseContactId
	 */
	public int getEclipseContactId() {
		return eclipseContactId;
	}
	/**
	 * @param eclipseContactId the eclipseContactId to set
	 */
	public void setEclipseContactId(int eclipseContactId) {
		this.eclipseContactId = eclipseContactId;
	}
	/**
	 * @return the externalSystemsUserId
	 */
	public int getExternalSystemsUserId() {
		return externalSystemsUserId;
	}
	/**
	 * @param externalSystemsUserId the externalSystemsUserId to set
	 */
	public void setExternalSystemsUserId(int externalSystemsUserId) {
		this.externalSystemsUserId = externalSystemsUserId;
	}
	/**
	 * @return the registeredDate
	 */
	public Date getRegisteredDate() {
		return registeredDate;
	}
	/**
	 * @param registeredDate the registeredDate to set
	 */
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	/**
	 * @return the defaultBillingAddressId
	 */
	public int getDefaultBillingAddressId() {
		return defaultBillingAddressId;
	}
	/**
	 * @param defaultBillingAddressId the defaultBillingAddressId to set
	 */
	public void setDefaultBillingAddressId(int defaultBillingAddressId) {
		this.defaultBillingAddressId = defaultBillingAddressId;
	}
	/**
	 * @return the defaultShippingAddressId
	 */
	public int getDefaultShippingAddressId() {
		return defaultShippingAddressId;
	}
	/**
	 * @param defaultShippingAddressId the defaultShippingAddressId to set
	 */
	public void setDefaultShippingAddressId(int defaultShippingAddressId) {
		this.defaultShippingAddressId = defaultShippingAddressId;
	}
	/**
	 * @return the isTaxable
	 */
	public String getIsTaxable() {
		return isTaxable;
	}
	/**
	 * @param isTaxable the isTaxable to set
	 */
	public void setIsTaxable(String isTaxable) {
		this.isTaxable = isTaxable;
	}
	/**
	 * @return the userEdited
	 */
	public int getUserEdited() {
		return userEdited;
	}
	/**
	 * @param userEdited the userEdited to set
	 */
	public void setUserEdited(int userEdited) {
		this.userEdited = userEdited;
	}
	/**
	 * @return the updatedDateTime
	 */
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	/**
	 * @param updatedDateTime the updatedDateTime to set
	 */
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	/**
	 * @return the punchoutUser
	 */
	public String getPunchoutUser() {
		return punchoutUser;
	}
	/**
	 * @param punchoutUser the punchoutUser to set
	 */
	public void setPunchoutUser(String punchoutUser) {
		this.punchoutUser = punchoutUser;
	}
	/**
	 * @return the firstLogin
	 */
	public String getFirstLogin() {
		return firstLogin;
	}
	/**
	 * @param firstLogin the firstLogin to set
	 */
	public void setFirstLogin(String firstLogin) {
		this.firstLogin = firstLogin;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the acceptOrderByPONumber
	 */
	public String getAcceptOrderByPONumber() {
		return acceptOrderByPONumber;
	}
	/**
	 * @param acceptOrderByPONumber the acceptOrderByPONumber to set
	 */
	public void setAcceptOrderByPONumber(String acceptOrderByPONumber) {
		this.acceptOrderByPONumber = acceptOrderByPONumber;
	}
	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/**
	 * @return the supplierSpecific
	 */
	public String getSupplierSpecific() {
		return supplierSpecific;
	}
	/**
	 * @param supplierSpecific the supplierSpecific to set
	 */
	public void setSupplierSpecific(String supplierSpecific) {
		this.supplierSpecific = supplierSpecific;
	}
	/**
	 * @return the parentUserId
	 */
	public int getParentUserId() {
		return parentUserId;
	}
	/**
	 * @param parentUserId the parentUserId to set
	 */
	public void setParentUserId(int parentUserId) {
		this.parentUserId = parentUserId;
	}
	/**
	 * @return the existingCustomer
	 */
	public String getExistingCustomer() {
		return existingCustomer;
	}
	/**
	 * @param existingCustomer the existingCustomer to set
	 */
	public void setExistingCustomer(String existingCustomer) {
		this.existingCustomer = existingCustomer;
	}
	/**
	 * @return the webservice
	 */
	public String getWebservice() {
		return webservice;
	}
	/**
	 * @param webservice the webservice to set
	 */
	public void setWebservice(String webservice) {
		this.webservice = webservice;
	}
	/**
	 * @return the approvalUserLogin
	 */
	public String getApprovalUserLogin() {
		return approvalUserLogin;
	}
	/**
	 * @param approvalUserLogin the approvalUserLogin to set
	 */
	public void setApprovalUserLogin(String approvalUserLogin) {
		this.approvalUserLogin = approvalUserLogin;
	}
	/**
	 * @return the approvalUserId
	 */
	public int getApprovalUserId() {
		return approvalUserId;
	}
	/**
	 * @param approvalUserId the approvalUserId to set
	 */
	public void setApprovalUserId(int approvalUserId) {
		this.approvalUserId = approvalUserId;
	}
	/**
	 * @return the siteId
	 */
	public int getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the termsType
	 */
	public String getTermsType() {
		return termsType;
	}
	/**
	 * @param termsType the termsType to set
	 */
	public void setTermsType(String termsType) {
		this.termsType = termsType;
	}
	/**
	 * @return the termsTypeDescription
	 */
	public String getTermsTypeDescription() {
		return termsTypeDescription;
	}
	/**
	 * @param termsTypeDescription the termsTypeDescription to set
	 */
	public void setTermsTypeDescription(String termsTypeDescription) {
		this.termsTypeDescription = termsTypeDescription;
	}
	/**
	 * @return the shipVia
	 */
	public String getShipVia() {
		return shipVia;
	}
	/**
	 * @param shipVia the shipVia to set
	 */
	public void setShipVia(String shipVia) {
		this.shipVia = shipVia;
	}
	/**
	 * @return the shipViaDescription
	 */
	public String getShipViaDescription() {
		return shipViaDescription;
	}
	/**
	 * @param shipViaDescription the shipViaDescription to set
	 */
	public void setShipViaDescription(String shipViaDescription) {
		this.shipViaDescription = shipViaDescription;
	}
	/**
	 * @return the wflPhaseId
	 */
	public int getWflPhaseId() {
		return wflPhaseId;
	}
	/**
	 * @param wflPhaseId the wflPhaseId to set
	 */
	public void setWflPhaseId(int wflPhaseId) {
		this.wflPhaseId = wflPhaseId;
	}
	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}
	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	/**
	 * @return the salesRepContact
	 */
	public String getSalesRepContact() {
		return salesRepContact;
	}
	/**
	 * @param salesRepContact the salesRepContact to set
	 */
	public void setSalesRepContact(String salesRepContact) {
		this.salesRepContact = salesRepContact;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CimmUser)) {
			return false;
		}
		CimmUser other = (CimmUser) obj;
		if (userId != other.userId) {
			return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CimmUser [userId=" + userId + ", userName=" + userName
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", buyingCompanyId=" + buyingCompanyId + ", email=" + email
				+ ", punchoutUser=" + punchoutUser + "]";
	}
	
}
