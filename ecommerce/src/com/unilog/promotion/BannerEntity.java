package com.unilog.promotion;

public class BannerEntity {
	 private int id;
	  private String name;
	  private String imageName;
	  private String imageType;

	  public int getId()
	  {
	    return this.id;
	  }
	  public void setId(int id) {
	    this.id = id;
	  }
	  public String getName() {
	    return this.name;
	  }
	  public void setName(String name) {
	    this.name = name;
	  }
	  public String getImageName() {
	    return this.imageName;
	  }
	  public void setImageName(String imageName) {
	    this.imageName = imageName;
	  }
	  public String getImageType() {
	    return this.imageType;
	  }
	  public void setImageType(String imageType) {
	    this.imageType = imageType;
	  }
}
