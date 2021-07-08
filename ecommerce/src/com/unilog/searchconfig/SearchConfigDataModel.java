package com.unilog.searchconfig;

import java.util.List;

public class SearchConfigDataModel {
	private String configName;
	private int configId;
	private List<SearchConfigModel> configData;
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public List<SearchConfigModel> getConfigData() {
		return configData;
	}
	public void setConfigData(List<SearchConfigModel> configData) {
		this.configData = configData;
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	
}
