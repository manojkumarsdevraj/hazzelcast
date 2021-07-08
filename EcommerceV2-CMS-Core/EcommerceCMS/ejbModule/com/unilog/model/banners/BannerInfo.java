package com.unilog.model.banners;

public class BannerInfo {

	private int id;
	private String sliderEffect;
	private String captionText;
	private int captionTopPos;
	private int captionLeftPos;
	private Banner banner;
	private String bannerUrl;
	private int displaySequence;
	private DynamicProperties dynamicProperties;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSliderEffect() {
		return sliderEffect;
	}
	public void setSliderEffect(String sliderEffect) {
		this.sliderEffect = sliderEffect;
	}
	public String getCaptionText() {
		return captionText;
	}
	public void setCaptionText(String captionText) {
		this.captionText = captionText;
	}
	public int getCaptionTopPos() {
		return captionTopPos;
	}
	public void setCaptionTopPos(int captionTopPos) {
		this.captionTopPos = captionTopPos;
	}
	public int getCaptionLeftPos() {
		return captionLeftPos;
	}
	public void setCaptionLeftPos(int captionLeftPos) {
		this.captionLeftPos = captionLeftPos;
	}
	public Banner getBanner() {
		return banner;
	}
	public void setBanner(Banner banner) {
		this.banner = banner;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public int getDisplaySequence() {
		return displaySequence;
	}
	public void setDisplaySequence(int displaySequence) {
		this.displaySequence = displaySequence;
	}
	public DynamicProperties getDynamicProperties() {
		return dynamicProperties;
	}
	public void setDynamicProperties(DynamicProperties dynamicProperties) {
		this.dynamicProperties = dynamicProperties;
	}
}
