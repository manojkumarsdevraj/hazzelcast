package com.unilog.socialmedia.service;

import com.unilog.socialmedia.blog.model.WebLogModel;

public class ResponseDataBlogEachPost {
	
	private String id;
	private String archor;
	private String creatorUserName;
	private String title;
	private String text;
	private String pubTime;
	private String updateTime;
	private WebLogModel weblog;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getArchor() {
		return archor;
	}
	public void setArchor(String archor) {
		this.archor = archor;
	}
	public String getCreatorUserName() {
		return creatorUserName;
	}
	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public WebLogModel getWeblog() {
		return weblog;
	}
	public void setWeblog(WebLogModel weblog) {
		this.weblog = weblog;
	}
	
	
	
}
