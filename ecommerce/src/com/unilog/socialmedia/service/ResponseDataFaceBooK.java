package com.unilog.socialmedia.service;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.unilog.socialmedia.facebook.model.*;



public class ResponseDataFaceBooK {
	
	@Expose private String metadata;
	@Expose private String id;
	@Expose private String message;
	@Expose private String picture;
	@Expose private String fullPicture;
	@Expose private String link;
	@Expose private String name;
	@Expose private String caption;
	@Expose private String description;
	@Expose private String source;
	@Expose private String type;
	@Expose private Integer sharesCount;
	@Expose private PlaceModel place;
	@Expose private String statusType;
	@Expose private String story;
	@Expose private String objectId;
	@Expose private ApplicationModel application;
	@Expose private String createdTime;
	@Expose private String updatedTime;
	@Expose private String scheduledPublishTime;
	@Expose private TargetingModel targeting;
	@Expose private String published;
	
	@Expose private CategoryModel from;
	@Expose private ArrayList<IdNameEntityModel> to;
	@Expose private ArrayList<TagModel> messageTags;
	@Expose private ArrayList<PropertyModel> properties;
	@Expose private ArrayList<ActionModel> actions;
	@Expose private PrivacyModel privacy;
	@Expose private ArrayList<IdNameEntityModel> withTags;
	
	
	public String getMetadata() {
		return metadata;
	}
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getFullPicture() {
		return fullPicture;
	}
	public void setFullPicture(String fullPicture) {
		this.fullPicture = fullPicture;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getSharesCount() {
		return sharesCount;
	}
	public void setSharesCount(Integer sharesCount) {
		this.sharesCount = sharesCount;
	}
	public PlaceModel getPlace() {
		return place;
	}
	public void setPlace(PlaceModel place) {
		this.place = place;
	}
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	public String getStory() {
		return story;
	}
	public void setStory(String story) {
		this.story = story;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public ApplicationModel getApplication() {
		return application;
	}
	public void setApplication(ApplicationModel application) {
		this.application = application;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getScheduledPublishTime() {
		return scheduledPublishTime;
	}
	public void setScheduledPublishTime(String scheduledPublishTime) {
		this.scheduledPublishTime = scheduledPublishTime;
	}
	public TargetingModel getTargeting() {
		return targeting;
	}
	public void setTargeting(TargetingModel targeting) {
		this.targeting = targeting;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public CategoryModel getFrom() {
		return from;
	}
	public void setFrom(CategoryModel from) {
		this.from = from;
	}
	public ArrayList<IdNameEntityModel> getTo() {
		return to;
	}
	public void setTo(ArrayList<IdNameEntityModel> to) {
		this.to = to;
	}
	public ArrayList<TagModel> getMessageTags() {
		return messageTags;
	}
	public void setMessageTags(ArrayList<TagModel> messageTags) {
		this.messageTags = messageTags;
	}
	public ArrayList<PropertyModel> getProperties() {
		return properties;
	}
	public void setProperties(ArrayList<PropertyModel> properties) {
		this.properties = properties;
	}
	public ArrayList<ActionModel> getActions() {
		return actions;
	}
	public void setActions(ArrayList<ActionModel> actions) {
		this.actions = actions;
	}
	public PrivacyModel getPrivacy() {
		return privacy;
	}
	public void setPrivacy(PrivacyModel privacy) {
		this.privacy = privacy;
	}
	public ArrayList<IdNameEntityModel> getWithTags() {
		return withTags;
	}
	public void setWithTags(ArrayList<IdNameEntityModel> withTags) {
		this.withTags = withTags;
	}
	

}
