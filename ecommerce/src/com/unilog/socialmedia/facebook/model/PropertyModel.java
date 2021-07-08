package com.unilog.socialmedia.facebook.model;

import com.google.gson.annotations.Expose;

public class PropertyModel {
	
	@Expose private String name;

    @Expose private String text;

    @Expose private String href;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
    
    

}
