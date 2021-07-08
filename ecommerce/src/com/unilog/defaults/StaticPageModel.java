package com.unilog.defaults;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "staticPage")

public class StaticPageModel {

	private int pageId;
	private String pageName;
	private String pageTitle;
	private String pageContent;
	private String plainText;
	private String metaDesc;
	private String metaKeywords;
	
	public int getPageId() {
		return pageId;
	}
	@XmlElement(name = "pageId")
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public String getPageName() {
		return pageName;
	}
	@XmlElement(name = "pageName")
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	@XmlElement(name = "pageTitle")
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getPageContent() {
		return pageContent;
	}
	@XmlElement(name = "pageContent")
	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}
	public String getPlainText() {
		return plainText;
	}
	
	@XmlElement(name = "plainText")
	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}
	public String getMetaKeywords() {
		return metaKeywords;
	}
	@XmlElement(name = "metaKeywords")
	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}
	public String getMetaDesc() {
		return metaDesc;
	}
	@XmlElement(name = "metaDesc")
	public void setMetaDesc(String metaDesc) {
		this.metaDesc = metaDesc;
	}
}
