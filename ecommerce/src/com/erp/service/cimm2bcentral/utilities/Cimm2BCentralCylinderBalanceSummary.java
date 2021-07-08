package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralCylinderBalanceSummary extends Cimm2BCentralResponseEntity{

	private String supplier;
	private String part;
	private Integer balanceForward;
	private Integer shipped;
	private Integer returned;
	private Integer leased;
	private String description1;
	private String description2;
	private String rentalGroup;
	private ArrayList<Cimm2BCentralCylinderBalanceSummary> cylinderBalanceConsolidatedList;
	private String cylinderSize;
	private Integer endBalance;
	private Integer totalBalanceForward;
	private Integer totalShipped;
	private Integer totalReturned;
	private Integer totalLeased;
	private Integer totalEndBalance;
	
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
	}
	public Integer getBalanceForward() {
		return balanceForward;
	}
	public void setBalanceForward(Integer balanceForward) {
		this.balanceForward = balanceForward;
	}
	public Integer getShipped() {
		return shipped;
	}
	public void setShipped(Integer shipped) {
		this.shipped = shipped;
	}
	public Integer getReturned() {
		return returned;
	}
	public void setReturned(Integer returned) {
		this.returned = returned;
	}
	public Integer getLeased() {
		return leased;
	}
	public void setLeased(Integer leased) {
		this.leased = leased;
	}
	public String getDescription1() {
		return description1;
	}
	public void setDescription1(String description1) {
		this.description1 = description1;
	}
	public String getDescription2() {
		return description2;
	}
	public void setDescription2(String description2) {
		this.description2 = description2;
	}
	public String getRentalGroup() {
		return rentalGroup;
	}
	public void setRentalGroup(String rentalGroup) {
		this.rentalGroup = rentalGroup;
	}
	public String getCylinderSize() {
		return cylinderSize;
	}
	public void setCylinderSize(String cylinderSize) {
		this.cylinderSize = cylinderSize;
	}
	public Integer getEndBalance() {
		return endBalance;
	}
	public void setEndBalance(Integer endBalance) {
		this.endBalance = endBalance;
	}
	public ArrayList<Cimm2BCentralCylinderBalanceSummary> getCylinderBalanceConsolidatedList() {
		return cylinderBalanceConsolidatedList;
	}
	public void setCylinderBalanceConsolidatedList(
			ArrayList<Cimm2BCentralCylinderBalanceSummary> cylinderBalanceConsolidatedList) {
		this.cylinderBalanceConsolidatedList = cylinderBalanceConsolidatedList;
	}
	public Integer getTotalBalanceForward() {
		return totalBalanceForward;
	}
	public void setTotalBalanceForward(Integer totalBalanceForward) {
		this.totalBalanceForward = totalBalanceForward;
	}
	public Integer getTotalShipped() {
		return totalShipped;
	}
	public void setTotalShipped(Integer totalShipped) {
		this.totalShipped = totalShipped;
	}
	public Integer getTotalReturned() {
		return totalReturned;
	}
	public void setTotalReturned(Integer totalReturned) {
		this.totalReturned = totalReturned;
	}
	public Integer getTotalLeased() {
		return totalLeased;
	}
	public void setTotalLeased(Integer totalLeased) {
		this.totalLeased = totalLeased;
	}
	public Integer getTotalEndBalance() {
		return totalEndBalance;
	}
	public void setTotalEndBalance(Integer totalEndBalance) {
		this.totalEndBalance = totalEndBalance;
	}
	
	
}
