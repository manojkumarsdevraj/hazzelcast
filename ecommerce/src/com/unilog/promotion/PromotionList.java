package com.unilog.promotion;

public class PromotionList {
	private int id;
	  private String startDate;
	  private String endDate;
	  private int bannerId;
	  private String keywords;
	  private String documents;
	  private String excludeDocuments;
	  private BannerEntity bannerEntity;
	  private String brands;

	  public int getId()
	  {
	    return this.id;
	  }
	  public void setId(int id) {
	    this.id = id;
	  }

	  public int getBannerId() {
	    return this.bannerId;
	  }
	  public void setBannerId(int bannerId) {
	    this.bannerId = bannerId;
	  }
	  public String getKeywords() {
	    return this.keywords;
	  }
	  public void setKeywords(String keywords) {
	    this.keywords = keywords;
	  }
	  public String getDocuments() {
	    return this.documents;
	  }
	  public void setDocuments(String documents) {
	    this.documents = documents;
	  }
	  public String getExcludeDocuments() {
	    return this.excludeDocuments;
	  }
	  public void setExcludeDocuments(String excludeDocuments) {
	    this.excludeDocuments = excludeDocuments;
	  }
	  public void setStartDate(String startDate) {
	    this.startDate = startDate;
	  }
	  public String getStartDate() {
	    return this.startDate;
	  }
	  public void setEndDate(String endDate) {
	    this.endDate = endDate;
	  }
	  public String getEndDate() {
	    return this.endDate;
	  }
	  public void setBannerEntity(BannerEntity bannerEntity) {
	    this.bannerEntity = bannerEntity;
	  }
	  public BannerEntity getBannerEntity() {
	    return this.bannerEntity;
	  }
	public String getBrands() {
		return brands;
	}
	public void setBrands(String brands) {
		this.brands = brands;
	}
}
