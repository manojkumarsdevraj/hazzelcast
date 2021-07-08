package com.unilognew.erp.service;

import java.util.ArrayList;

import com.unilog.sales.SalesModel;
import com.unilognew.exception.ERPServiceException;
 
public interface FreightERPService {
	public ArrayList<SalesModel> getUpsFreightCharges(SalesModel freightParameters)  throws ERPServiceException;
	public ArrayList<SalesModel> getFedexFreightCharges(SalesModel freightParameters)  throws ERPServiceException;
	
}
