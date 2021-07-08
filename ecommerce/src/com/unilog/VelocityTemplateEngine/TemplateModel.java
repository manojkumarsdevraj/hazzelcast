package com.unilog.VelocityTemplateEngine;

import java.util.LinkedHashMap;

public class TemplateModel {
	private String LeftContent;
	private String BodyContent;
	private String RightContent;
	private String LayoutName;
	private LinkedHashMap<String, String> layoutObj;

	public void setLeftContent(String leftContent) {
		LeftContent = leftContent;
	}
	public String getLeftContent() {
		return LeftContent;
	}
	public void setBodyContent(String bodyContent) {
		BodyContent = bodyContent;
	}
	public String getBodyContent() {
		return BodyContent;
	}
	public void setRightContent(String rightContent) {
		RightContent = rightContent;
	}
	public String getRightContent() {
		return RightContent;
	}
	public void setLayoutName(String layoutName) {
		LayoutName = layoutName;
	}
	public String getLayoutName() {
		return LayoutName;
	}
	public LinkedHashMap<String, String> getLayoutObj() {
		return layoutObj;
	}
	public void setLayoutObj(LinkedHashMap<String, String> layoutObj) {
		this.layoutObj = layoutObj;
	}

}
