package com.unilog.socialmedia.twitter.model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class UserModel {
	
	@Expose private long id;
	
	@Expose private String name;
	
	@Expose private String screenName;
	
	@Expose private String location;
	
	@Expose private String description;
	
	@Expose private String url;
	
	@Expose private long followersCount;
	
	@Expose private String profileImageURL;
	
	@Expose private String biggerProfileImageURL;
	
	@Expose private String miniProfileImageURL;
	
	@Expose private String originalProfileImageURL;
	
	@Expose private String profileImageURLHttps;
	
	@Expose private String biggerProfileImageURLHttps;
	
	@Expose private String miniProfileImageURLHttps;
	
	@Expose private String originalProfileImageURLHttps;
	
	@Expose private URLEntityModel urlentity;
	
	

	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(long followersCount) {
		this.followersCount = followersCount;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	public String getBiggerProfileImageURL() {
		return biggerProfileImageURL;
	}

	public void setBiggerProfileImageURL(String biggerProfileImageURL) {
		this.biggerProfileImageURL = biggerProfileImageURL;
	}

	public String getMiniProfileImageURL() {
		return miniProfileImageURL;
	}

	public void setMiniProfileImageURL(String miniProfileImageURL) {
		this.miniProfileImageURL = miniProfileImageURL;
	}

	public String getOriginalProfileImageURL() {
		return originalProfileImageURL;
	}

	public void setOriginalProfileImageURL(String originalProfileImageURL) {
		this.originalProfileImageURL = originalProfileImageURL;
	}

	public String getProfileImageURLHttps() {
		return profileImageURLHttps;
	}

	public void setProfileImageURLHttps(String profileImageURLHttps) {
		this.profileImageURLHttps = profileImageURLHttps;
	}

	public String getBiggerProfileImageURLHttps() {
		return biggerProfileImageURLHttps;
	}

	public void setBiggerProfileImageURLHttps(String biggerProfileImageURLHttps) {
		this.biggerProfileImageURLHttps = biggerProfileImageURLHttps;
	}

	public String getMiniProfileImageURLHttps() {
		return miniProfileImageURLHttps;
	}

	public void setMiniProfileImageURLHttps(String miniProfileImageURLHttps) {
		this.miniProfileImageURLHttps = miniProfileImageURLHttps;
	}

	public String getOriginalProfileImageURLHttps() {
		return originalProfileImageURLHttps;
	}

	public void setOriginalProfileImageURLHttps(String originalProfileImageURLHttps) {
		this.originalProfileImageURLHttps = originalProfileImageURLHttps;
	}

	public URLEntityModel getUrlentity() {
		return urlentity;
	}

	public void setUrlentity(URLEntityModel urlentity) {
		this.urlentity = urlentity;
	}

	

}
