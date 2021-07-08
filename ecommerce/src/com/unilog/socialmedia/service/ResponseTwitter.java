package com.unilog.socialmedia.service;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.unilog.socialmedia.twitter.model.Status;



public class ResponseTwitter extends ResponseStatusSocialMedia  {
	
	@Expose private ArrayList<Status> data;

	public ArrayList<Status> getData() {
		return data;
	}

	public void setData(ArrayList<Status> data) {
		this.data = data;
	}
	
}
