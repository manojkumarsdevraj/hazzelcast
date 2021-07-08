package com.unilognew.erp.service.provider;

import com.unilognew.erp.service.FreightERPService;
import com.unilognew.erp.service.IProductERPService;
import com.unilognew.erp.service.ISalesOrderERPService;
import com.unilognew.erp.service.IUserERPService;
import com.unilognew.util.ECommerceEnumType.ErpType;

public class ERPServicesFactory{

	
	private ERPServicesFactory() {
		//avoid instantiation instead use the factory method
	}	

	private static IERPServiceProvider getServiceProvider(ErpType erpType) {
		
		IERPServiceProvider erpServiceProvider = null;
		switch(erpType) {
			case CIMM2BCENTRAL:
				erpServiceProvider = Cimm2BCentralERPServiceProvider.getInstance();
			break;
			case CIMMESB :
				erpServiceProvider = CimmESBERPServiceProvider.getInstance();
				break;
			default:
				throw new IllegalArgumentException("No ERP Service Provider registered with name: " + erpType.name());
		}
		return erpServiceProvider;
	}
	
	public static IUserERPService getUserERPService(ErpType erpType) {
		IERPServiceProvider erpServiceProvider = getServiceProvider(erpType);		
		return erpServiceProvider.getUserERPService();
	}
	
	public static IProductERPService getProductERPService(ErpType erpType) {
		IERPServiceProvider erpServiceProvider = getServiceProvider(erpType);		
		return erpServiceProvider.getProductERPService();
	}
	
	public static ISalesOrderERPService getSalesOrderERPService(ErpType erpType) {
		IERPServiceProvider erpServiceProvider = getServiceProvider(erpType);		
		return erpServiceProvider.getSalesOrderERPService();
	}
	public static FreightERPService getFreightERPService(ErpType erpType) {
		IERPServiceProvider erpServiceProvider = getServiceProvider(erpType);		
		return erpServiceProvider.getFreightERPService();
	}
}
