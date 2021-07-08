package com.unilog.socialmedia.facebook.model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class PlaceModel {
	
	  @Expose private String id;

	  @Expose private String name;

	  @Expose private ArrayList<CategoryModel> categories;

	  @Expose private LocationModel location;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<CategoryModel> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<CategoryModel> categories) {
		this.categories = categories;
	}

	public LocationModel getLocation() {
		return location;
	}

	public void setLocation(LocationModel location) {
		this.location = location;
	}

	  
	  
}
