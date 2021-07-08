package com.unilog.socialmedia.facebook.model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class TargetingModel {
	 @Expose private ArrayList<String> Countries;

	  @Expose private ArrayList<String> Cities;

	  @Expose private ArrayList<String> Regions;

	  @Expose private ArrayList<String> Locales;

	public ArrayList<String> getCountries() {
		return Countries;
	}

	public void setCountries(ArrayList<String> countries) {
		Countries = countries;
	}

	public ArrayList<String> getCities() {
		return Cities;
	}

	public void setCities(ArrayList<String> cities) {
		Cities = cities;
	}

	public ArrayList<String> getRegions() {
		return Regions;
	}

	public void setRegions(ArrayList<String> regions) {
		Regions = regions;
	}

	public ArrayList<String> getLocales() {
		return Locales;
	}

	public void setLocales(ArrayList<String> locales) {
		Locales = locales;
	}
	  
	  
}
