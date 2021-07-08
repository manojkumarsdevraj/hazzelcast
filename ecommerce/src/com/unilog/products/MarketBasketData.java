package com.unilog.products;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MarketBasketData {
	private String searchKeyWord;
	private int userQuantity;
	private int searchCount;
	private String lineItemComment;
	private ArrayList<ProductsModel> itemList;
	private String searchType;
	private int rowNum;
	private boolean isDuplicate;
	private String duplicateItemRows;
	private String index;
	private boolean isInvalidQuantity;
	private LinkedHashMap<String, Object> customFieldVal = null;
	
	
	
	public LinkedHashMap<String, Object> getCustomFieldVal() {
		return customFieldVal;
	}
	public void setCustomFieldVal(LinkedHashMap<String, Object> customFieldVal) {
		this.customFieldVal = customFieldVal;
	}
	public String getSearchKeyWord() {
		return searchKeyWord;
	}
	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}
	public int getUserQuantity() {
		return userQuantity;
	}
	public void setUserQuantity(int userQuantity) {
		this.userQuantity = userQuantity;
	}
	public int getSearchCount() {
		return searchCount;
	}
	public void setSearchCount(int searchCount) {
		this.searchCount = searchCount;
	}
	public String getLineItemComment() {
		return lineItemComment;
	}
	public void setLineItemComment(String lineItemComment) {
		this.lineItemComment = lineItemComment;
	}
	public ArrayList<ProductsModel> getItemList() {
		return itemList;
	}
	public void setItemList(ArrayList<ProductsModel> itemList) {
		this.itemList = itemList;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public boolean isDuplicate() {
		return isDuplicate;
	}
	public void setDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}
	public String getDuplicateItemRows() {
		return duplicateItemRows;
	}
	public void setDuplicateItemRows(String duplicateItemRows) {
		this.duplicateItemRows = duplicateItemRows;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public void setInvalidQuantity(boolean isInvalidQuantity) {
		this.isInvalidQuantity = isInvalidQuantity;
	}
	public boolean isInvalidQuantity() {
		return isInvalidQuantity;
	}

}
