package com.unilog.products;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.unilog.promotion.BannerEntity;

public class ProductHunterModel {
	private String attrFtr[];
	private String filterQuery[];
	private ArrayList<String> removeAttributeList;
	private ArrayList<ProductsModel> refinedList;
	private LinkedHashMap<String, ArrayList<String>> filteredMultList;
	private String[] facetField;
	private String keyWord;
	private int subsetId;
	private int generalSubset;
	private int fromRow;
	private int toRow;
	private String requestType;
	private String taxonomyId;
	private int treeId;
	private String attrFilterList;
	private String brandId;
	private String sortBy;
	int resultPerPage;
	private String narrowKeyword;
	private int buyingCompanyId;
	private int userId;
	private boolean isCategoryNav;
	private boolean exactSearch;
	private String viewFrequentlyPurcahsedOnly;
	private String fq;
	private BannerEntity bannerEntity;
	
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public int getSubsetId() {
		return subsetId;
	}
	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}
	public int getGeneralSubset() {
		return generalSubset;
	}
	public void setGeneralSubset(int generalSubset) {
		this.generalSubset = generalSubset;
	}
	public int getFromRow() {
		return fromRow;
	}
	public void setFromRow(int fromRow) {
		this.fromRow = fromRow;
	}
	public int getToRow() {
		return toRow;
	}
	public void setToRow(int toRow) {
		this.toRow = toRow;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getTaxonomyId() {
		return taxonomyId;
	}
	public void setTaxonomyId(String taxonomyId) {
		this.taxonomyId = taxonomyId;
	}
	public int getTreeId() {
		return treeId;
	}
	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}
	public String getAttrFilterList() {
		return attrFilterList;
	}
	public void setAttrFilterList(String attrFilterList) {
		this.attrFilterList = attrFilterList;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public int getResultPerPage() {
		return resultPerPage;
	}
	public void setResultPerPage(int resultPerPage) {
		this.resultPerPage = resultPerPage;
	}
	public String getNarrowKeyword() {
		return narrowKeyword;
	}
	public void setNarrowKeyword(String narrowKeyword) {
		this.narrowKeyword = narrowKeyword;
	}
	public int getBuyingCompanyId() {
		return buyingCompanyId;
	}
	public void setBuyingCompanyId(int buyingCompanyId) {
		this.buyingCompanyId = buyingCompanyId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public boolean isCategoryNav() {
		return isCategoryNav;
	}
	public void setCategoryNav(boolean isCategoryNav) {
		this.isCategoryNav = isCategoryNav;
	}
	public boolean isExactSearch() {
		return exactSearch;
	}
	public void setExactSearch(boolean exactSearch) {
		this.exactSearch = exactSearch;
	}
	public String getViewFrequentlyPurcahsedOnly() {
		return viewFrequentlyPurcahsedOnly;
	}
	public void setViewFrequentlyPurcahsedOnly(String viewFrequentlyPurcahsedOnly) {
		this.viewFrequentlyPurcahsedOnly = viewFrequentlyPurcahsedOnly;
	}
	public String[] getFilterQuery() {
		return filterQuery;
	}
	public void setFilterQuery(String filterQuery[]) {
		this.filterQuery = filterQuery;
	}
	public String[] getAttrFtr() {
		return attrFtr;
	}
	public void setAttrFtr(String attrFtr[]) {
		this.attrFtr = attrFtr;
	}
	public ArrayList<String> getRemoveAttributeList() {
		return removeAttributeList;
	}
	public void setRemoveAttributeList(ArrayList<String> removeAttributeList) {
		this.removeAttributeList = removeAttributeList;
	}
	public ArrayList<ProductsModel> getRefinedList() {
		return refinedList;
	}
	public void setRefinedList(ArrayList<ProductsModel> refinedList) {
		this.refinedList = refinedList;
	}
	public LinkedHashMap<String, ArrayList<String>> getFilteredMultList() {
		return filteredMultList;
	}
	public void setFilteredMultList(LinkedHashMap<String, ArrayList<String>> filteredMultList) {
		this.filteredMultList = filteredMultList;
	}
	public String[] getFacetField() {
		return facetField;
	}
	public void setFacetField(String[] facetField) {
		this.facetField = facetField;
	}
	public String getFq() {
		return fq;
	}
	public void setFq(String fq) {
		this.fq = fq;
	}
	public BannerEntity getBannerEntity() {
		return bannerEntity;
	}
	public void setBannerEntity(BannerEntity bannerEntity) {
		this.bannerEntity = bannerEntity;
	}
	
}
