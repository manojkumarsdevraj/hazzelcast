package com.unilog.defaults;

import java.util.Map;

public class SendMailModel {
	private int userId;
	private String customerName;
	private String userName;
	private String password;
	private String emailAddress;
	private String fromEmailId;
	private String toEmailId;
	private String ccEmailId;
	private String bccEmailId;
	private String firstName;
	private String lastName;
	private String customerServiceEmailId;
	private String siteDisplayName;
	private String webAddress;
	private String siteName;
	private String mailSubject;
	private int punchoutUser;
	private String messageBody;
	private String toName;
	private String fromName;
	private String descPart;
	private String pricePart;
	private String contentPart;
	private String imgPart;
	private String mailTemplateName;
	private String mailMessage;
	private String phone;
	private String mobileNo;
	private String address1;
	private String address2;
	private String jobTitle;
	private String templateName;
	private boolean defaultFalg;
	private String sendQuoteMail;
	private String CustomerID;
	private String additionalName;
	private String additionalPickupPerson;
	private String additionalComments;
	private boolean firstOrderEmail;
	private int subsetId;
	private int generalSubsetId;
	private String companyName;
	private String shipTo;
	private boolean reviewOrderMail;
	private String additionalEmailNotification;
	
	public String getAdditionalEmailNotification() {
		return additionalEmailNotification;
	}
	public void setAdditionalEmailNotification(String additionalEmailNotification) {
		this.additionalEmailNotification = additionalEmailNotification;
	}
	public String getShipTo() {
		return shipTo;
	}
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	private Map<String, String> attachmentDetails;
	
	public int getSubsetId() {
		return subsetId;
	}
	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}
	public int getGeneralSubsetId() {
		return generalSubsetId;
	}
	public void setGeneralSubsetId(int generalSubsetId) {
		this.generalSubsetId = generalSubsetId;
	}
	public boolean isFirstOrderEmail() {
		return firstOrderEmail;
	}
	public void setFirstOrderEmail(boolean firstOrderEmail) {
		this.firstOrderEmail = firstOrderEmail;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getSendQuoteMail() {
		return sendQuoteMail;
	}
	public void setSendQuoteMail(String sendQuoteMail) {
		this.sendQuoteMail = sendQuoteMail;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public boolean isDefaultFalg() {
		return defaultFalg;
	}
	public void setDefaultFalg(boolean defaultFalg) {
		this.defaultFalg = defaultFalg;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMailMessage() {
		return mailMessage;
	}
	public void setMailMessage(String mailMessage) {
		this.mailMessage = mailMessage;
	}
	public String getDescPart() {
		return descPart;
	}
	public void setDescPart(String descPart) {
		this.descPart = descPart;
	}
	public String getPricePart() {
		return pricePart;
	}
	public void setPricePart(String pricePart) {
		this.pricePart = pricePart;
	}
	public String getContentPart() {
		return contentPart;
	}
	public void setContentPart(String contentPart) {
		this.contentPart = contentPart;
	}
	public String getImgPart() {
		return imgPart;
	}
	public void setImgPart(String imgPart) {
		this.imgPart = imgPart;
	}
	public String getMailTemplateName() {
		return mailTemplateName;
	}
	public void setMailTemplateName(String mailTemplateName) {
		this.mailTemplateName = mailTemplateName;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public int getPunchoutUser() {
		return punchoutUser;
	}
	public void setPunchoutUser(int punchoutUser) {
		this.punchoutUser = punchoutUser;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getFromEmailId() {
		return fromEmailId;
	}
	public void setFromEmailId(String fromEmailId) {
		this.fromEmailId = fromEmailId;
	}
	public String getToEmailId() {
		return toEmailId;
	}
	public void setToEmailId(String toEmailId) {
		this.toEmailId = toEmailId;
	}
	public String getCcEmailId() {
		return ccEmailId;
	}
	public void setCcEmailId(String ccEmailId) {
		this.ccEmailId = ccEmailId;
	}
	public String getBccEmailId() {
		return bccEmailId;
	}
	public void setBccEmailId(String bccEmailId) {
		this.bccEmailId = bccEmailId;
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
	public String getCustomerServiceEmailId() {
		return customerServiceEmailId;
	}
	public void setCustomerServiceEmailId(String customerServiceEmailId) {
		this.customerServiceEmailId = customerServiceEmailId;
	}
	public String getSiteDisplayName() {
		return siteDisplayName;
	}
	public void setSiteDisplayName(String siteDisplayName) {
		this.siteDisplayName = siteDisplayName;
	}
	public String getWebAddress() {
		return webAddress;
	}
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplateName() {
		return templateName;
	}
	public String getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}
	public String getAdditionalName() {
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
	public Map<String, String> getAttachmentDetails() {
		return attachmentDetails;
	}
	public void setAttachmentDetails(Map<String, String> attachmentDetails) {
		this.attachmentDetails = attachmentDetails;
	}
	public boolean isReviewOrderMail() {
		return reviewOrderMail;
	}
	public void setReviewOrderMail(boolean reviewOrderMail) {
		this.reviewOrderMail = reviewOrderMail;
	}
}