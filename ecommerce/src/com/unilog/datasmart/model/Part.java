package com.unilog.datasmart.model;

public class Part {
	private String Description;
	private String Tag;
	private String Context;
	private String ImageUrl;
	private int PartId;
	private String Sku;
	private double MSRP;
	private boolean IsSuperseded;
	private String OrgSku;
	private String OrgDescription;
	private String OrgMSRP;
	private PartQtyRuntime Qty;
	private String SortTag;
	private boolean NLA;
	
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public String getContext() {
		return Context;
	}
	public void setContext(String context) {
		Context = context;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public int getPartId() {
		return PartId;
	}
	public void setPartId(int partId) {
		PartId = partId;
	}
	public String getSku() {
		return Sku;
	}
	public void setSku(String sku) {
		Sku = sku;
	}
	public double getMSRP() {
		return MSRP;
	}
	public void setMSRP(double mSRP) {
		MSRP = mSRP;
	}
	public boolean isIsSuperseded() {
		return IsSuperseded;
	}
	public void setIsSuperseded(boolean isSuperseded) {
		IsSuperseded = isSuperseded;
	}
	public String getOrgSku() {
		return OrgSku;
	}
	public void setOrgSku(String orgSku) {
		OrgSku = orgSku;
	}
	public String getOrgDescription() {
		return OrgDescription;
	}
	public void setOrgDescription(String orgDescription) {
		OrgDescription = orgDescription;
	}
	public String getOrgMSRP() {
		return OrgMSRP;
	}
	public void setOrgMSRP(String orgMSRP) {
		OrgMSRP = orgMSRP;
	}
	public String getSortTag() {
		return SortTag;
	}
	public void setSortTag(String sortTag) {
		SortTag = sortTag;
	}
	public boolean isNLA() {
		return NLA;
	}
	public void setNLA(boolean nLA) {
		NLA = nLA;
	}
	public PartQtyRuntime getQty() {
		return Qty;
	}
	public void setQty(PartQtyRuntime qty) {
		Qty = qty;
	}
	
}
