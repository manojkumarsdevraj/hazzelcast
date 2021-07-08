package com.unilog.socialmedia.twitter.model;

import com.google.gson.annotations.Expose;

public class URLEntityModel {
	
	@Expose private String text;

	@Expose private String url;

	@Expose private String expandedURL;

	@Expose private String displayURL;

	@Expose private int start;

	@Expose private int end;

	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getExpandedURL() {
		return expandedURL;
	}

	public void setExpandedURL(String expandedURL) {
		this.expandedURL = expandedURL;
	}

	public String getDisplayURL() {
		return displayURL;
	}

	public void setDisplayURL(String displayURL) {
		this.displayURL = displayURL;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	

}
