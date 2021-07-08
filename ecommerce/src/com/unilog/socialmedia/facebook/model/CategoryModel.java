package com.unilog.socialmedia.facebook.model;

import com.google.gson.annotations.Expose;

public class CategoryModel {
	@Expose private String metadata;
	@Expose private String id;
	@Expose private String name;
	@Expose private String category;
	@Expose private String createdTime;
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	
	
	
}
