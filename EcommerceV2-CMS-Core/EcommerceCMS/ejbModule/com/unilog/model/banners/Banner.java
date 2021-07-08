package com.unilog.model.banners;

public class Banner {
	private int id;
	private String name;
	private String description;
	private String imageName;
	private String imageType;
	private int fixedWidth;
	private int fixedHeight;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public int getFixedWidth() {
		return fixedWidth;
	}
	public void setFixedWidth(int fixedWidth) {
		this.fixedWidth = fixedWidth;
	}
	public int getFixedHeight() {
		return fixedHeight;
	}
	public void setFixedHeight(int fixedHeight) {
		this.fixedHeight = fixedHeight;
	}
}
