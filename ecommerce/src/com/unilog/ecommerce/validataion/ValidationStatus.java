package com.unilog.ecommerce.validataion;

import java.util.ArrayList;
import java.util.List;

public class ValidationStatus {
	
	private boolean valid;
	private String sourceFormId;
	private String sourceObj;
	private List<String> descriptions = new ArrayList<>();
	
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public List<String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}
	public String getSourceFormId() {
		return sourceFormId;
	}
	public void setSourceFormId(String sourceFormId) {
		this.sourceFormId = sourceFormId;
	}
	public String getSourceObj() {
		return sourceObj;
	}
	public void setSourceObj(String sourceObj) {
		this.sourceObj = sourceObj;
	}
}
