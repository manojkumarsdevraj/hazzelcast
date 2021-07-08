package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;

public class Cimm2BCentralPeriod {

	private Double totalAmountForPeriod;
	private ArrayList<Cimm2BCentralPeriodDetail> detailsList;
	
	
	
	public Double getTotalAmountForPeriod() {
		return totalAmountForPeriod;
	}
	public void setTotalAmountForPeriod(Double totalAmountForPeriod) {
		this.totalAmountForPeriod = totalAmountForPeriod;
	}
	public ArrayList<Cimm2BCentralPeriodDetail> getDetailsList() {
		return detailsList;
	}
	public void setDetailsList(ArrayList<Cimm2BCentralPeriodDetail> detailsList) {
		this.detailsList = detailsList;
	}
	
	
	
	
	
}
