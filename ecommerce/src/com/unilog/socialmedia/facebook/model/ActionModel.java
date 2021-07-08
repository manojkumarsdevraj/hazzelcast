package com.unilog.socialmedia.facebook.model;

import com.google.gson.annotations.Expose;

public class ActionModel {

	@Expose private String name;

    @Expose private String link;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
    
    
}
