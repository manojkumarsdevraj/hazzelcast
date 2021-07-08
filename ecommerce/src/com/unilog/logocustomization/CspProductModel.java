package com.unilog.logocustomization;

import java.util.ArrayList;

public class CspProductModel {
	private String userId;
	private String productFamily;
	private int packQuantity;	
	private int decimalPrecision;
	private String signTemplate;
	private String signTemplateOption;
	private String bradyHeaderCode;
	private String colorName;
	private String sku;
	private String text;
	private String pictoId;
	private ArrayList<CspPricingModel> Pricing = new ArrayList<CspPricingModel>();
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProductFamily() {
		return productFamily;
	}
	public void setProductFamily(String productFamily) {
		this.productFamily = productFamily;
	}
	public int getPackQuantity() {
		return packQuantity;
	}
	public void setPackQuantity(int packQuantity) {
		this.packQuantity = packQuantity;
	}
	public int getDecimalPrecision() {
		return decimalPrecision;
	}
	public void setDecimalPrecision(int decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}
	public String getSignTemplate() {
		return signTemplate;
	}
	public void setSignTemplate(String signTemplate) {
		this.signTemplate = signTemplate;
	}
	public String getSignTemplateOption() {
		return signTemplateOption;
	}
	public void setSignTemplateOption(String signTemplateOption) {
		this.signTemplateOption = signTemplateOption;
	}
	public String getBradyHeaderCode() {
		return bradyHeaderCode;
	}
	public void setBradyHeaderCode(String bradyHeaderCode) {
		this.bradyHeaderCode = bradyHeaderCode;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getPictoId() {
		return pictoId;
	}
	public void setPictoId(String pictoId) {
		this.pictoId = pictoId;
	}
	public ArrayList<CspPricingModel> getPricing() {
		return Pricing;
	}
	public void setPricing(ArrayList<CspPricingModel> pricing) {
		Pricing = pricing;
	}
	
}
