package com.unilog.socialmedia.twitter.model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class Status {
	
	@Expose private  long createdAt;

	@Expose private  long id;

	@Expose private  String text;

	@Expose private  String source;
	
	@Expose private long inReplyToStatusId;

	@Expose private long inReplyToUserId;
	
	@Expose private int favoriteCount;
	
	@Expose private String inReplyToScreenName;
	
	@Expose private int retweetCount;
	
	@Expose private String lang;
	
	@Expose private long currentUserRetweetId;
	
	@Expose private boolean favorited;
	
	@Expose private boolean retweeted;
	
	@Expose private boolean retweet;
	
	@Expose private boolean truncated;
	
	@Expose private boolean retweetedByMe;
	
	@Expose private boolean possiblySensitive;
	
	@Expose private ArrayList<UserMentionEntitiesModel> userMentionEntities;
	
	@Expose private ArrayList<HashtagEntityModel> hashtagEntities;
	
	@Expose private ArrayList<MediaEntitiesModel> mediaEntities;
	
	@Expose private ArrayList<MediaEntitiesModel> extendedMediaEntities;
	
	@Expose private ArrayList<SymbolEntityModel> symbolEntities;
	
	@Expose private ScopesModel scopes;
	
	@Expose private UserModel user;
	
	@Expose private ArrayList<URLEntityModel> urlentities;
	
	
	
	
	public ArrayList<HashtagEntityModel> getHashtagEntities() {
		return hashtagEntities;
	}

	public void setHashtagEntities(ArrayList<HashtagEntityModel> hashtagEntities) {
		this.hashtagEntities = hashtagEntities;
	}

	public ArrayList<MediaEntitiesModel> getMediaEntities() {
		return mediaEntities;
	}

	public void setMediaEntities(ArrayList<MediaEntitiesModel> mediaEntities) {
		this.mediaEntities = mediaEntities;
	}

	public ArrayList<MediaEntitiesModel> getExtendedMediaEntities() {
		return extendedMediaEntities;
	}

	public void setExtendedMediaEntities(
			ArrayList<MediaEntitiesModel> extendedMediaEntities) {
		this.extendedMediaEntities = extendedMediaEntities;
	}

	public ArrayList<SymbolEntityModel> getSymbolEntities() {
		return symbolEntities;
	}

	public void setSymbolEntities(ArrayList<SymbolEntityModel> symbolEntities) {
		this.symbolEntities = symbolEntities;
	}

	public ScopesModel getScopes() {
		return scopes;
	}

	public void setScopes(ScopesModel scopes) {
		this.scopes = scopes;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public ArrayList<URLEntityModel> getUrlentities() {
		return urlentities;
	}

	public void setUrlentities(ArrayList<URLEntityModel> urlentities) {
		this.urlentities = urlentities;
	}

	public long getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public void setInReplyToStatusId(long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public long getInReplyToUserId() {
		return inReplyToUserId;
	}

	public void setInReplyToUserId(long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public long getCurrentUserRetweetId() {
		return currentUserRetweetId;
	}

	public void setCurrentUserRetweetId(long currentUserRetweetId) {
		this.currentUserRetweetId = currentUserRetweetId;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public boolean isRetweeted() {
		return retweeted;
	}

	public void setRetweeted(boolean retweeted) {
		this.retweeted = retweeted;
	}

	public boolean isRetweet() {
		return retweet;
	}

	public void setRetweet(boolean retweet) {
		this.retweet = retweet;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public boolean isRetweetedByMe() {
		return retweetedByMe;
	}

	public void setRetweetedByMe(boolean retweetedByMe) {
		this.retweetedByMe = retweetedByMe;
	}

	public boolean isPossiblySensitive() {
		return possiblySensitive;
	}

	public void setPossiblySensitive(boolean possiblySensitive) {
		this.possiblySensitive = possiblySensitive;
	}

	public ArrayList<UserMentionEntitiesModel> getUserMentionEntities() {
		return userMentionEntities;
	}

	public void setUserMentionEntities(
			ArrayList<UserMentionEntitiesModel> userMentionEntities) {
		this.userMentionEntities = userMentionEntities;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	
}
