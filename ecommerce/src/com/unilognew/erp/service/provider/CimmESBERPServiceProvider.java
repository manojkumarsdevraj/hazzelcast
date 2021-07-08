package com.unilognew.erp.service.provider;

import com.unilognew.erp.service.FreightERPService;
import com.unilognew.erp.service.IProductERPService;
import com.unilognew.erp.service.ISalesOrderERPService;
import com.unilognew.erp.service.IUserERPService;
import com.unilognew.erp.service.cimmesb.CimmESBFreightERPServiceImpl;
import com.unilognew.erp.service.cimmesb.CimmESBUserERPServiceImpl;

public class CimmESBERPServiceProvider implements IERPServiceProvider{

	
	private static CimmESBERPServiceProvider cimm2bCentralERPServiceProvider;

	private CimmESBERPServiceProvider() {

	}

	public static CimmESBERPServiceProvider getInstance() {
		synchronized(CimmESBERPServiceProvider.class) {
			if(cimm2bCentralERPServiceProvider==null) {
				cimm2bCentralERPServiceProvider = new CimmESBERPServiceProvider();
			}
		}
		return cimm2bCentralERPServiceProvider;
	}
	@Override
	public IUserERPService getUserERPService() {
		
		return CimmESBUserERPServiceImpl.getInstance();
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
		
		return CimmESBFreightERPServiceImpl.getInstance();
	}

}
