package com.unilog.socialmedia.twitter.model;

import com.google.gson.annotations.Expose;

 public class HashtagEntityModel {
	 
	@Expose private  String text;

	@Expose private  int start;

	@Expose private  int end;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
