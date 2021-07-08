package com.unilog.datasmart.model;

import java.util.LinkedList;


public class AssemblyInfo {
	private LinkedList<Part> Parts;
	private String ParentName;
	private int ParentId;
	private LinkedList <HotSpot> HotSpots;
	private int AssemblyId;
	private String ImageUrl;
	private String Name;
	private Status message;
	private AssemblyInfoRuntime runtime;
	public int getRuntime()
    {
        return runtime.getValue();
    }
	public LinkedList<Part> getParts() {
		return Parts;
	}
	public void setParts(LinkedList<Part> parts) {
		Parts = parts;
	}
	public String getParentName() {
		return ParentName;
	}
	public void setParentName(String parentName) {
		ParentName = parentName;
	}
	public LinkedList<HotSpot> getHotSpots() {
		return HotSpots;
	}
	public void setHotSpots(LinkedList<HotSpot> hotSpots) {
		HotSpots = hotSpots;
	}
	public int getAssemblyId() {
		return AssemblyId;
	}
	public void setAssemblyId(int assemblyId) {
		AssemblyId = assemblyId;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Status getMessage() {
		return message;
	}
	public void setMessage(Status message) {
		this.message = message;
	}
	public int getParentId() {
		return ParentId;
	}
	public void setParentId(int parentId) {
		ParentId = parentId;
	}
}
