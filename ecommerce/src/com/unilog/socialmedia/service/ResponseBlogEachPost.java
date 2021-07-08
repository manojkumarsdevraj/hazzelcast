package com.unilog.socialmedia.service;

import java.util.ArrayList;

public class ResponseBlogEachPost extends ResponseStatusSocialMedia {
	
	private ArrayList<ResponseDataBlogEachPost> data;

	public ArrayList<ResponseDataBlogEachPost> getData() {
		return data;
	}

	public void setData(ArrayList<ResponseDataBlogEachPost> data) {
		this.data = data;
	}
	
}
