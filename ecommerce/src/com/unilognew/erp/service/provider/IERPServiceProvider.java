package com.unilognew.erp.service.provider;

import com.unilognew.erp.service.FreightERPService;
import com.unilognew.erp.service.IProductERPService;
import com.unilognew.erp.service.ISalesOrderERPService;
import com.unilognew.erp.service.IUserERPService;

public interface IERPServiceProvider {
	
	public IUserERPService getUserERPService();
	public ISalesOrderERPService getSalesOrderERPService();
	public IProductERPService getProductERPService();
	public FreightERPService getFreightERPService();
}
