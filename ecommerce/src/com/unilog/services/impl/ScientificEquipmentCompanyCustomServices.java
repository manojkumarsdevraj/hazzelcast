package com.unilog.services.impl;

import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.utility.CommonUtility;

public class ScientificEquipmentCompanyCustomServices implements UnilogFactoryInterface {
	private static UnilogFactoryInterface serviceProvider;
	private ScientificEquipmentCompanyCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (ScientificEquipmentCompanyCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new ScientificEquipmentCompanyCustomServices();
				}
			}
		return serviceProvider;
	}
	
	public double getPriceCustomService(double itemPrice, ProductsModel erpResponseDetails, boolean quantityBreakFlag) {
		double price = 0.0;
		try {
			if (erpResponseDetails != null) {
				price = erpResponseDetails.getPrice();
				if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_QUANTITY_BREAK")).equalsIgnoreCase("Y")){
					if (erpResponseDetails.getQuantityBreakList() != null && erpResponseDetails.getQuantityBreakList().size() > 0) {
						for (int i = 0; i < erpResponseDetails.getQuantityBreakList().size(); i++) {
							if (erpResponseDetails.getQty() >= erpResponseDetails.getQuantityBreakList().get(i).getMinimumQuantityBreak()) {
								price = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(erpResponseDetails.getQuantityBreakList().get(i).getPrice()));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return price;
	}
	
}