package com.unilog.logocustomization;

import java.util.ArrayList;

public class CspPricingModel {
	private String sku;
	private ArrayList<CspPriceTable> PricingTable = new ArrayList<CspPriceTable>();
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public ArrayList<CspPriceTable> getPricingTable() {
		return PricingTable;
	}
	public void setPricingTable(ArrayList<CspPriceTable> pricingTable) {
		PricingTable = pricingTable;
	}
	
}
