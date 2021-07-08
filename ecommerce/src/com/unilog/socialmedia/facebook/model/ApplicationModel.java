package com.unilog.socialmedia.facebook.model;

import java.util.*;

import com.google.gson.annotations.Expose;

public class ApplicationModel {
	  
	  @Expose private String id;

	  @Expose private String name;

	  @Expose private String description;

	  @Expose private String category;

	  @Expose private String company;

	  @Expose private String iconurl;

	  @Expose private String subcategory;

	  @Expose private String link;

	  @Expose private String logourl;

	  @Expose private String dailyactiveusers;

	  @Expose private String weeklyactiveusers;

	  @Expose private String monthlyactiveusers;

	  @Expose private Map<String, Boolean> migrations;

	  @Expose private String namespace;

	  @Expose private Map<String, String> restrictions;

	  @Expose private ArrayList<String> appdomains;

	  @Expose private String authdialogdatahelpurl;

	  @Expose private String authdialogdescription;

	  @Expose private String authdialogheadline;

	  @Expose private String authdialogpermsexplanation;

	  @Expose private ArrayList<String> authreferraluserperms;

	  @Expose private ArrayList<String> authreferralfriendperms;

	  @Expose private String authreferraldefaultactivityprivacy;

	  @Expose private boolean authreferralenabled;

	  @Expose private ArrayList<String> authreferralextendedperms;

	  @Expose private String authreferralresponsetype;

	  @Expose private boolean canvasfluidheight;

	  @Expose private boolean canvasfluidwidth;

	  @Expose private String canvasurl;

	  @Expose private String contactemail;

	  @Expose private long createdtime;

	  @Expose private long creatoruid;

	  @Expose private String deauthcallbackurl;

	  @Expose private String iphoneappstoreid;

	  @Expose private String hostingurl;

	  @Expose private String mobileweburl;

	  @Expose private String pagetabdefaultname;

	  @Expose private String pagetaburl;

	  @Expose private String privacypolicyurl;

	  @Expose private String securecanvasurl;

	  @Expose private String securepagetaburl;

	  @Expose private String serveripwhitelist;

	  @Expose private boolean socialdiscovery;

	  @Expose private String termsofserviceurl;

	  @Expose private String usersupportemail;

	  @Expose private String usersupporturl;

	  @Expose private String websiteurl;

	  @Expose private String canvasname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getIconurl() {
		return iconurl;
	}

	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public String getDailyactiveusers() {
		return dailyactiveusers;
	}

	public void setDailyactiveusers(String dailyactiveusers) {
		this.dailyactiveusers = dailyactiveusers;
	}

	public String getWeeklyactiveusers() {
		return weeklyactiveusers;
	}

	public void setWeeklyactiveusers(String weeklyactiveusers) {
		this.weeklyactiveusers = weeklyactiveusers;
	}

	public String getMonthlyactiveusers() {
		return monthlyactiveusers;
	}

	public void setMonthlyactiveusers(String monthlyactiveusers) {
		this.monthlyactiveusers = monthlyactiveusers;
	}

	public Map<String, Boolean> getMigrations() {
		return migrations;
	}

	public void setMigrations(Map<String, Boolean> migrations) {
		this.migrations = migrations;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public Map<String, String> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(Map<String, String> restrictions) {
		this.restrictions = restrictions;
	}

	public ArrayList<String> getAppdomains() {
		return appdomains;
	}

	public void setAppdomains(ArrayList<String> appdomains) {
		this.appdomains = appdomains;
	}

	public String getAuthdialogdatahelpurl() {
		return authdialogdatahelpurl;
	}

	public void setAuthdialogdatahelpurl(String authdialogdatahelpurl) {
		this.authdialogdatahelpurl = authdialogdatahelpurl;
	}

	public String getAuthdialogdescription() {
		return authdialogdescription;
	}

	public void setAuthdialogdescription(String authdialogdescription) {
		this.authdialogdescription = authdialogdescription;
	}

	public String getAuthdialogheadline() {
		return authdialogheadline;
	}

	public void setAuthdialogheadline(String authdialogheadline) {
		this.authdialogheadline = authdialogheadline;
	}

	public String getAuthdialogpermsexplanation() {
		return authdialogpermsexplanation;
	}

	public void setAuthdialogpermsexplanation(String authdialogpermsexplanation) {
		this.authdialogpermsexplanation = authdialogpermsexplanation;
	}

	public ArrayList<String> getAuthreferraluserperms() {
		return authreferraluserperms;
	}

	public void setAuthreferraluserperms(ArrayList<String> authreferraluserperms) {
		this.authreferraluserperms = authreferraluserperms;
	}

	public ArrayList<String> getAuthreferralfriendperms() {
		return authreferralfriendperms;
	}

	public void setAuthreferralfriendperms(ArrayList<String> authreferralfriendperms) {
		this.authreferralfriendperms = authreferralfriendperms;
	}

	public String getAuthreferraldefaultactivityprivacy() {
		return authreferraldefaultactivityprivacy;
	}

	public void setAuthreferraldefaultactivityprivacy(
			String authreferraldefaultactivityprivacy) {
		this.authreferraldefaultactivityprivacy = authreferraldefaultactivityprivacy;
	}

	public boolean isAuthreferralenabled() {
		return authreferralenabled;
	}

	public void setAuthreferralenabled(boolean authreferralenabled) {
		this.authreferralenabled = authreferralenabled;
	}

	public ArrayList<String> getAuthreferralextendedperms() {
		return authreferralextendedperms;
	}

	public void setAuthreferralextendedperms(
			ArrayList<String> authreferralextendedperms) {
		this.authreferralextendedperms = authreferralextendedperms;
	}

	public String getAuthreferralresponsetype() {
		return authreferralresponsetype;
	}

	public void setAuthreferralresponsetype(String authreferralresponsetype) {
		this.authreferralresponsetype = authreferralresponsetype;
	}

	public boolean isCanvasfluidheight() {
		return canvasfluidheight;
	}

	public void setCanvasfluidheight(boolean canvasfluidheight) {
		this.canvasfluidheight = canvasfluidheight;
	}

	public boolean isCanvasfluidwidth() {
		return canvasfluidwidth;
	}

	public void setCanvasfluidwidth(boolean canvasfluidwidth) {
		this.canvasfluidwidth = canvasfluidwidth;
	}

	public String getCanvasurl() {
		return canvasurl;
	}

	public void setCanvasurl(String canvasurl) {
		this.canvasurl = canvasurl;
	}

	public String getContactemail() {
		return contactemail;
	}

	public void setContactemail(String contactemail) {
		this.contactemail = contactemail;
	}

	public long getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(long createdtime) {
		this.createdtime = createdtime;
	}

	public long getCreatoruid() {
		return creatoruid;
	}

	public void setCreatoruid(long creatoruid) {
		this.creatoruid = creatoruid;
	}

	public String getDeauthcallbackurl() {
		return deauthcallbackurl;
	}

	public void setDeauthcallbackurl(String deauthcallbackurl) {
		this.deauthcallbackurl = deauthcallbackurl;
	}

	public String getIphoneappstoreid() {
		return iphoneappstoreid;
	}

	public void setIphoneappstoreid(String iphoneappstoreid) {
		this.iphoneappstoreid = iphoneappstoreid;
	}

	public String getHostingurl() {
		return hostingurl;
	}

	public void setHostingurl(String hostingurl) {
		this.hostingurl = hostingurl;
	}

	public String getMobileweburl() {
		return mobileweburl;
	}

	public void setMobileweburl(String mobileweburl) {
		this.mobileweburl = mobileweburl;
	}

	public String getPagetabdefaultname() {
		return pagetabdefaultname;
	}

	public void setPagetabdefaultname(String pagetabdefaultname) {
		this.pagetabdefaultname = pagetabdefaultname;
	}

	public String getPagetaburl() {
		return pagetaburl;
	}

	public void setPagetaburl(String pagetaburl) {
		this.pagetaburl = pagetaburl;
	}

	public String getPrivacypolicyurl() {
		return privacypolicyurl;
	}

	public void setPrivacypolicyurl(String privacypolicyurl) {
		this.privacypolicyurl = privacypolicyurl;
	}

	public String getSecurecanvasurl() {
		return securecanvasurl;
	}

	public void setSecurecanvasurl(String securecanvasurl) {
		this.securecanvasurl = securecanvasurl;
	}

	public String getSecurepagetaburl() {
		return securepagetaburl;
	}

	public void setSecurepagetaburl(String securepagetaburl) {
		this.securepagetaburl = securepagetaburl;
	}

	public String getServeripwhitelist() {
		return serveripwhitelist;
	}

	public void setServeripwhitelist(String serveripwhitelist) {
		this.serveripwhitelist = serveripwhitelist;
	}

	public boolean isSocialdiscovery() {
		return socialdiscovery;
	}

	public void setSocialdiscovery(boolean socialdiscovery) {
		this.socialdiscovery = socialdiscovery;
	}

	public String getTermsofserviceurl() {
		return termsofserviceurl;
	}

	public void setTermsofserviceurl(String termsofserviceurl) {
		this.termsofserviceurl = termsofserviceurl;
	}

	public String getUsersupportemail() {
		return usersupportemail;
	}

	public void setUsersupportemail(String usersupportemail) {
		this.usersupportemail = usersupportemail;
	}

	public String getUsersupporturl() {
		return usersupporturl;
	}

	public void setUsersupporturl(String usersupporturl) {
		this.usersupporturl = usersupporturl;
	}

	public String getWebsiteurl() {
		return websiteurl;
	}

	public void setWebsiteurl(String websiteurl) {
		this.websiteurl = websiteurl;
	}

	public String getCanvasname() {
		return canvasname;
	}

	public void setCanvasname(String canvasname) {
		this.canvasname = canvasname;
	}
	  
	  

}
