package com.unilog.model;

public class CategoryDataModel {
	
	private int id;
	private String status;
	private int categoryCode;
	private String categoryName;
	private int level1;
	private int level2;
	private int level3;
	private int level4;
	private int level5;
	private int level6;
	private int levelNumber;
	private int displaySequence;
	private String imageName;
	private String categoryDescription;
	private int cneBatchId;
	private int parentTaxonomyTreeId;
	private String imageType;
	private int staticPageId;
	private int valueListId;
	private int topBannerId;
	private int leftBannerId;
	private int rightBannerId;
	private int bottomBannerId;
	private TaxonomyDataModel taxonomyDTO;
	
	
	
	public TaxonomyDataModel getTaxonomyDTO() {
		return taxonomyDTO;
	}
	public void setTaxonomyDTO(TaxonomyDataModel taxonomyDTO) {
		this.taxonomyDTO = taxonomyDTO;
	}
	public int getValueListId() {
		return valueListId;
	}
	public void setValueListId(int valueListId) {
		this.valueListId = valueListId;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(int categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getLevel1() {
		return level1;
	}
	public void setLevel1(int level1) {
		this.level1 = level1;
	}
	public int getLevel2() {
		return level2;
	}
	public void setLevel2(int level2) {
		this.level2 = level2;
	}
	public int getLevel3() {
		return level3;
	}
	public void setLevel3(int level3) {
		this.level3 = level3;
	}
	public int getLevel4() {
		return level4;
	}
	public void setLevel4(int level4) {
		this.level4 = level4;
	}
	public int getLevel5() {
		return level5;
	}
	public void setLevel5(int level5) {
		this.level5 = level5;
	}
	public int getLevel6() {
		return level6;
	}
	public void setLevel6(int level6) {
		this.level6 = level6;
	}
	public int getLevelNumber() {
		return levelNumber;
	}
	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}
	public int getDisplaySequence() {
		return displaySequence;
	}
	public void setDisplaySequence(int displaySequence) {
		this.displaySequence = displaySequence;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getCategoryDescription() {
		return categoryDescription;
	}
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
	public int getCneBatchId() {
		return cneBatchId;
	}
	public void setCneBatchId(int cneBatchId) {
		this.cneBatchId = cneBatchId;
	}
	public int getParentTaxonomyTreeId() {
		return parentTaxonomyTreeId;
	}
	public void setParentTaxonomyTreeId(int parentTaxonomyTreeId) {
		this.parentTaxonomyTreeId = parentTaxonomyTreeId;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public int getStaticPageId() {
		return staticPageId;
	}
	public void setStaticPageId(int staticPageId) {
		this.staticPageId = staticPageId;
	}
}
