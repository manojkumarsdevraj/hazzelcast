package com.unilog.autocomplete;

public class AutoCompleteDataTableModel {

	private String query;
	private String sStart;
	private String sAmount;
	private String sEcho;
	private String sCol;
	private String sdir;
	private String searchTerm;
	private String engine;
	private String browser;
	private String platform;
	private String version;
	private String grade;
	private int subsetId;
	private String fayttypeidx;
	private String responseType;

	public String getsStart() {
		return sStart;
	}
	public void setsStart(String sStart) {
		this.sStart = sStart;
	}
	public String getsAmount() {
		return sAmount;
	}
	public void setsAmount(String sAmount) {
		this.sAmount = sAmount;
	}
	public String getsEcho() {
		return sEcho;
	}
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
	public String getsCol() {
		return sCol;
	}
	public void setsCol(String sCol) {
		this.sCol = sCol;
	}
	public String getSdir() {
		return sdir;
	}
	public void setSdir(String sdir) {
		this.sdir = sdir;
	}
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public int getSubsetId() {
		return subsetId;
	}
	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}
	public String getFayttypeidx() {
		return fayttypeidx;
	}
	public void setFayttypeidx(String fayttypeidx) {
		this.fayttypeidx = fayttypeidx;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
}
