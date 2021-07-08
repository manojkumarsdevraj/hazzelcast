package com.unilognew.erp.service.provider;

import com.unilognew.erp.service.FreightERPService;
import com.unilognew.erp.service.IProductERPService;
import com.unilognew.erp.service.ISalesOrderERPService;
import com.unilognew.erp.service.IUserERPService;
import com.unilognew.erp.service.cimm2bcentral.Cimm2BCentralUserERPServiceImpl;
import com.unilognew.erp.service.cimm2bcentral.Cimm2bCentralFreightERPServiceImpl;

public class Cimm2BCentralERPServiceProvider implements IERPServiceProvider{

	
	private static Cimm2BCentralERPServiceProvider cimm2bCentralERPServiceProvider;

	private Cimm2BCentralERPServiceProvider() {

	}

	public static Cimm2BCentralERPServiceProvider getInstance() {
		synchronized(Cimm2BCentralERPServiceProvider.class) {
			if(cimm2bCentralERPServiceProvider==null) {
				cimm2bCentralERPServiceProvider = new Cimm2BCentralERPServiceProvider();
			}
		}
		return cimm2bCentralERPServiceProvider;
	}
	@Override
	public IUserERPService getUserERPService() {
		
		return Cimm2BCentralUserERPServiceImpl.getInstance();
	}

	@Override
	public ISalesOrderERPService getSalesOrderERPService() {
		
		return null;
	}

	@Override
	public IProductERPService getProductERPService() {
		
		return null;
	}

	@Override
	public FreightERPService getFreightERPService() {
		
		return Cimm2bCentralFreightERPServiceImpl.getInstance();
	}

}
