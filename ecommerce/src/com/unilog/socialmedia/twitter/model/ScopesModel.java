package com.unilog.socialmedia.twitter.model;

import com.google.gson.annotations.Expose;

public class ScopesModel {
	@Expose private String[] placeIds;

	public String[] getPlaceIds() {
		return placeIds;
	}

	public void setPlaceIds(String[] placeIds) {
		this.placeIds = placeIds;
	}
	
	
}
