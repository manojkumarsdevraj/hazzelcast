package com.unilog.services.impl;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.unilog.products.ProductsModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class BillowsElectricCustomServices implements UnilogFactoryInterface{

	private static UnilogFactoryInterface serviceProvider;
	
	private BillowsElectricCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (PurvisCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new BillowsElectricCustomServices();
				}
			}
		return serviceProvider;
	}
	
	@Override
	public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
	{	
		String newsLetterCustomFiledValue="";
		if(userDetailsInput.getNewsLetterSub()!=null && userDetailsInput.getNewsLetterSub().equalsIgnoreCase("on")){
			newsLetterCustomFiledValue="Y";
		}else{
			newsLetterCustomFiledValue="N";
		}
		UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
	}
	
	@Override
	public void getUomPackAsUom(Cimm2BCentralItem item, ProductsModel productsModel)
	{	
		productsModel.setUom(item.getPricingWarehouse().getUom()!=null?item.getPricingWarehouse().getUom():"");
	}
	
	@Override
	public double getExtendedPrice(double price, int orderqty, int salesQty, double salesQuntityUom) {
		double extendedPrice = 0.0;
		try {
			if(!(salesQty>0)){
				salesQty = 1;
			}
			extendedPrice = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString((price * orderqty)/(salesQty/salesQuntityUom)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return extendedPrice;
	}
	
	@Override
	public void updateUom(Cimm2BCentralLineItem cimm2bCentralLineItem ,ProductsModel item) {
		
		try {
			cimm2bCentralLineItem.setUom(item.getUom());
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
