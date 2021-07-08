package com.unilog.model;

import java.util.LinkedHashMap;

import com.unilog.VelocityTemplateEngine.TemplateModel;

public class CmsLayoutModel {
	private LinkedHashMap<String, TemplateModel> layoutList;

	public void setLayoutList(LinkedHashMap<String, TemplateModel> layoutList) {
		this.layoutList = layoutList;
	}

	public LinkedHashMap<String, TemplateModel> getLayoutList() {
		return layoutList;
	}
}
