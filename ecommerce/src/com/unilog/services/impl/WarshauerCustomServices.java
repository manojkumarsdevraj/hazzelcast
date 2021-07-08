package com.unilog.services.impl;

import java.util.List;
import java.util.Optional;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.utility.CommonUtility;

public class WarshauerCustomServices implements UnilogFactoryInterface  {
	private static UnilogFactoryInterface serviceProvider;
	
	private WarshauerCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (WarshauerCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new WarshauerCustomServices();
				}
			}
		return serviceProvider;
	}

	 public Cimm2BCentralLineItem  disableUmqtyToErp(Cimm2BCentralLineItem cimm2bCentralLineItem){
		 cimm2bCentralLineItem.setUomQty("");
		 return cimm2bCentralLineItem;
	 }
	 
	 
	 public void dontaddfreightcharges(SalesModel quoteInfo,Cimm2BCentralOrder orderResponse) {
		 if(orderResponse.getFreight()!=null && orderResponse.getFreight() > 0) {
			 quoteInfo.setTotal(orderResponse.getOrderTotal());
		 }
	 }
	 
	 public String setFirstLoginTrueForInactiveUser() {
			return "Y";
		}
	 @Override
		public void setPaymentTerm(Cimm2BCentralOrder orderRequest) {
	       if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_TERMS_FOR_CREDITCARD_ORDERS")).length() > 0) {
			String paymentTerms = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_TERMS_FOR_CREDITCARD_ORDERS"));
	    	orderRequest.setPaymentTermCode(paymentTerms);
	       }
		}

	
}
