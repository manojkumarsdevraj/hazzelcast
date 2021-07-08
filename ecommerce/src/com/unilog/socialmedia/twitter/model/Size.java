package com.unilog.socialmedia.twitter.model;

import com.google.gson.annotations.Expose;

public class Size {
	
	@Expose private final Integer THUMB = Integer.valueOf(0);
	@Expose private final Integer SMALL = Integer.valueOf(1);
	@Expose private final Integer MEDIUM = Integer.valueOf(2);
	@Expose private final Integer LARGE = Integer.valueOf(3);
	@Expose private final int FIT = 100;
	@Expose private final int CROP = 101;
	@Expose private int width;
	@Expose private int height;
	@Expose private int resize;
	
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getResize() {
		return resize;
	}
	public void setResize(int resize) {
		this.resize = resize;
	}
	
}
