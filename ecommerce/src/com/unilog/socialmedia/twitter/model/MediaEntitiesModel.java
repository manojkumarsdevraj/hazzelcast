package com.unilog.socialmedia.twitter.model;

import java.util.Map;

import com.google.gson.annotations.Expose;

public class MediaEntitiesModel {
	
	@Expose private long start;

	@Expose private long end;
	
	@Expose private long id;
	
	@Expose private String url;

	@Expose private String mediaURL;
	
	@Expose private String mediaURLHttps;
	
	@Expose private String expandedURL;
	
	@Expose private String displayURL;
	
	@Expose private Map<Integer, Size> sizes;
	
	@Expose private String type;
	
	@Expose private String text;

	
	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMediaURL() {
		return mediaURL;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public String getMediaURLHttps() {
		return mediaURLHttps;
	}

	public void setMediaURLHttps(String mediaURLHttps) {
		this.mediaURLHttps = mediaURLHttps;
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

	public Map<Integer, Size> getSizes() {
		return sizes;
	}

	public void setSizes(Map<Integer, Size> sizes) {
		this.sizes = sizes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
