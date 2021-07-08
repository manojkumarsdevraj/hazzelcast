package com.unilog.socialmedia.facebook.model;

import com.google.gson.annotations.Expose;

public class IdNameEntityModel {
	@Expose private String id;
	@Expose private String name;
	
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
	
	
}
