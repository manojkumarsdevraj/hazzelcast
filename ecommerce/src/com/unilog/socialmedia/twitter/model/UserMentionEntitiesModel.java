package com.unilog.socialmedia.twitter.model;

import com.google.gson.annotations.Expose;

public class UserMentionEntitiesModel {

	@Expose private long start;
	
	@Expose private long end;
	
	@Expose private long id;
	
	@Expose private String name;
	
	@Expose private String screenName;
	
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
