package com.unilog.avalara.model;

public class TaxAuthority {

	private String avalaraId;
	private String jurisdictionName;
	private String jurisdictionType;
	private String signatureCode;

	public String getAvalaraId() {
		return avalaraId;
	}

	public void setAvalaraId(String avalaraId) {
		this.avalaraId = avalaraId;
	}

	public String getJurisdictionName() {
		return jurisdictionName;
	}

	public void setJurisdictionName(String jurisdictionName) {
		this.jurisdictionName = jurisdictionName;
	}

	public String getJurisdictionType() {
		return jurisdictionType;
	}

	public void setJurisdictionType(String jurisdictionType) {
		this.jurisdictionType = jurisdictionType;
	}

	public String getSignatureCode() {
		return signatureCode;
	}

	public void setSignatureCode(String signatureCode) {
		this.signatureCode = signatureCode;
	}

}
