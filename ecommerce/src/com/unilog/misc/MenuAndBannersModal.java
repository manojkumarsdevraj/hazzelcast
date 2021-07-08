package com.unilog.misc;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuAndBannersModal implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imageName;
	private String imageType;
	private String imageURL;
	private String itemUrl;
	private String categoryName;
	private String categoryCode;
	private int levelNumber;
	private int brandId;
	private String brandName;
	private String menuName;
	private String menuId;
	private String level1Menu;
	private String level2Menu;
	private String level3Menu;
	private String level4Menu;
	private String level5Menu;
	private String level6Menu;
	private String level7Menu;
	private int parentId;
	private ArrayList<MenuAndBannersModal> menuList = new ArrayList<MenuAndBannersModal>();
	private String staticPageType;
	private String StaticPageUrl;
	private String imagePosition;
	private String bannerScroll;
	private String bannerDelay;
	private String bannerNumberofItem;
	private String bannerDirection;
	private int subsetId;
	private int resultCount;
	private String manufacturerName;
	private int manufacturerId;
	private String openPageIn;
	private String bannerName;
	private String bannerType;
	private String bannerIsScrollable;
	private String bannerScrollDelay;
	private String topBannerType;
	private String leftBannerType;
	private String rightBannerType;
	private String bottomBannerType;
	private int topBannerId;
	private int leftBannerId;
	private int rightBannerId;
	private int bottomBannerId;
	public enum bannerLocation { TOP, BOTTOM, LEFT, RIGHT; }
	private String navingationMenu[];
	
	
	public String[] getNavingationMenu() {
		return navingationMenu;
	}
	public void setNavingationMenu(String[] navingationMenu) {
		this.navingationMenu = navingationMenu;
	}
	public String getBannerName() {
		return bannerName;
	}
	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}
	public String getBannerType() {
		return bannerType;
	}
	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}
	public String getBannerIsScrollable() {
		return bannerIsScrollable;
	}
	public void setBannerIsScrollable(String bannerIsScrollable) {
		this.bannerIsScrollable = bannerIsScrollable;
	}
	public String getBannerScrollDelay() {
		return bannerScrollDelay;
	}
	public void setBannerScrollDelay(String bannerScrollDelay) {
		this.bannerScrollDelay = bannerScrollDelay;
	}
	public String getOpenPageIn() {
		return openPageIn;
	}
	public void setOpenPageIn(String openPageIn) {
		this.openPageIn = openPageIn;
	}
	public int getSubsetId() {
		return subsetId;
	}
	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}
	public String getStaticPageType() {
		return staticPageType;
	}
	public void setStaticPageType(String staticPageType) {
		this.staticPageType = staticPageType;
	}
	public String getStaticPageUrl() {
		return StaticPageUrl;
	}
	public void setStaticPageUrl(String staticPageUrl) {
		StaticPageUrl = staticPageUrl;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getLevel1Menu() {
		return level1Menu;
	}
	public void setLevel1Menu(String level1Menu) {
		this.level1Menu = level1Menu;
	}
	public String getLevel2Menu() {
		return level2Menu;
	}
	public void setLevel2Menu(String level2Menu) {
		this.level2Menu = level2Menu;
	}
	public String getLevel3Menu() {
		return level3Menu;
	}
	public void setLevel3Menu(String level3Menu) {
		this.level3Menu = level3Menu;
	}
	public String getLevel4Menu() {
		return level4Menu;
	}
	public void setLevel4Menu(String level4Menu) {
		this.level4Menu = level4Menu;
	}
	public String getLevel5Menu() {
		return level5Menu;
	}
	public void setLevel5Menu(String level5Menu) {
		this.level5Menu = level5Menu;
	}
	public String getLevel6Menu() {
		return level6Menu;
	}
	public void setLevel6Menu(String level6Menu) {
		this.level6Menu = level6Menu;
	}
	public String getLevel7Menu() {
		return level7Menu;
	}
	public void setLevel7Menu(String level7Menu) {
		this.level7Menu = level7Menu;
	}
	public String getItemUrl() {
		return itemUrl;
	}
	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
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
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}
	public int getLevelNumber() {
		return levelNumber;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public int getBrandId() {
		return brandId;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setMenuList(ArrayList<MenuAndBannersModal> menuList) {
		this.menuList = menuList;
	}
	public ArrayList<MenuAndBannersModal> getMenuList() {
		return menuList;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getParentId() {
		return parentId;
	}
	public String getImagePosition() {
		return imagePosition;
	}
	public void setImagePosition(String imagePosition) {
		this.imagePosition = imagePosition;
	}
	public String getBannerScroll() {
		return bannerScroll;
	}
	public void setBannerScroll(String bannerScroll) {
		this.bannerScroll = bannerScroll;
	}
	public String getBannerDelay() {
		return bannerDelay;
	}
	public void setBannerDelay(String bannerDelay) {
		this.bannerDelay = bannerDelay;
	}
	public String getBannerNumberofItem() {
		return bannerNumberofItem;
	}
	public void setBannerNumberofItem(String bannerNumberofItem) {
		this.bannerNumberofItem = bannerNumberofItem;
	}
	public String getBannerDirection() {
		return bannerDirection;
	}
	public void setBannerDirection(String bannerDirection) {
		this.bannerDirection = bannerDirection;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	public int getResultCount() {
		return resultCount;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public int getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(int manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public String getTopBannerType() {
		return topBannerType;
	}
	public void setTopBannerType(String topBannerType) {
		this.topBannerType = topBannerType;
	}
	public String getLeftBannerType() {
		return leftBannerType;
	}
	public void setLeftBannerType(String leftBannerType) {
		this.leftBannerType = leftBannerType;
	}
	public String getRightBannerType() {
		return rightBannerType;
	}
	public void setRightBannerType(String rightBannerType) {
		this.rightBannerType = rightBannerType;
	}
	public String getBottomBannerType() {
		return bottomBannerType;
	}
	public void setBottomBannerType(String bottomBannerType) {
		this.bottomBannerType = bottomBannerType;
	}
	public int getTopBannerId() {
		return topBannerId;
	}
	public void setTopBannerId(int topBannerId) {
		this.topBannerId = topBannerId;
	}
	public int getLeftBannerId() {
		return leftBannerId;
	}
	public void setLeftBannerId(int leftBannerId) {
		this.leftBannerId = leftBannerId;
	}
	public int getRightBannerId() {
		return rightBannerId;
	}
	public void setRightBannerId(int rightBannerId) {
		this.rightBannerId = rightBannerId;
	}
	public int getBottomBannerId() {
		return bottomBannerId;
	}
	public void setBottomBannerId(int bottomBannerId) {
		this.bottomBannerId = bottomBannerId;
	}
}
