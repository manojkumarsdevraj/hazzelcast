package com.unilog.datasmart.model;

public class SearchModelAssemblyInfo {
	
	private int Id;
	private String ParentName;
	private Boolean IsParent;
	private int ParentId;
	private String AssemblyInformation;
	private String Name;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public Boolean getIsParent() {
		return IsParent;
	}
	public void setIsParent(Boolean isParent) {
		IsParent = isParent;
	}
	public int getParentId() {
		return ParentId;
	}
	public void setParentId(int parentId) {
		ParentId = parentId;
	}
	public String getAssemblyInformation() {
		return AssemblyInformation;
	}
	public void setAssemblyInformation(String assemblyInformation) {
		AssemblyInformation = assemblyInformation;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getParentName() {
		return ParentName;
	}
	public void setParentName(String parentName) {
		ParentName = parentName;
	}
	

}
