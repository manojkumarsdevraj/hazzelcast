package com.unilog.VelocityTemplateEngine.Model;

import java.util.LinkedHashMap;

import com.unilog.VelocityTemplateEngine.TemplateModel;

public class LayoutModel {
	private LinkedHashMap<String, TemplateModel> layoutList;
	private LinkedHashMap<String, LinkedHashMap<String, TemplateModel>> layoutBySite;

	public void setLayoutList(LinkedHashMap<String, TemplateModel> layoutList) {
		this.layoutList = layoutList;
	}

	public LinkedHashMap<String, TemplateModel> getLayoutList() {
		return layoutList;
	}

	public void setLayoutBySite(LinkedHashMap<String, LinkedHashMap<String, TemplateModel>> layoutBySite) {
		this.layoutBySite = layoutBySite;
	}

	public LinkedHashMap<String, LinkedHashMap<String, TemplateModel>> getLayoutBySite() {
		return layoutBySite;
	}
}
