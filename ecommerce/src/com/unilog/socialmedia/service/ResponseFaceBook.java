package com.unilog.socialmedia.service;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class ResponseFaceBook extends ResponseStatusSocialMedia {
	
	@Expose private ArrayList<ResponseDataFaceBooK> data;

	public ArrayList<ResponseDataFaceBooK> getData() {
		return data;
	}

	public void setData(ArrayList<ResponseDataFaceBooK> data) {
		this.data = data;
	}

	
}
