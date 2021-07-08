package com.unilog.socialmedia.service;

import java.util.ArrayList;

public class ResponseBlog extends ResponseStatusSocialMedia {
	
	private ArrayList<ResponseDataBlog> data;

	public ArrayList<ResponseDataBlog> getData() {
		return data;
	}

	public void setData(ArrayList<ResponseDataBlog> data) {
		this.data = data;
	}
}
