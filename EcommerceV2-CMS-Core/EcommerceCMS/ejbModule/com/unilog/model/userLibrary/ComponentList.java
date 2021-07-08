package com.unilog.model.userLibrary;

import java.util.List;

public class ComponentList {
	private int id;
	private String userLibraryGroupName;
	private String userLibraryDescription;
	private List<ComponentData> cimmUserLibraryDTOs;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserLibraryGroupName() {
		return userLibraryGroupName;
	}
	public void setUserLibraryGroupName(String userLibraryGroupName) {
		this.userLibraryGroupName = userLibraryGroupName;
	}
	public String getUserLibraryDescription() {
		return userLibraryDescription;
	}
	public void setUserLibraryDescription(String userLibraryDescription) {
		this.userLibraryDescription = userLibraryDescription;
	}
	public List<ComponentData> getCimmUserLibraryDTOs() {
		return cimmUserLibraryDTOs;
	}
	public void setCimmUserLibraryDTOs(List<ComponentData> cimmUserLibraryDTOs) {
		this.cimmUserLibraryDTOs = cimmUserLibraryDTOs;
	}
	
	
}
