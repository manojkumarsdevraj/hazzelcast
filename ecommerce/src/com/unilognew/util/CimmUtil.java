package com.unilognew.util;

import com.unilognew.util.ECommerceEnumType.AddressType;
import com.unilognew.util.ECommerceEnumType.ErpType;

public class CimmUtil {
	
	public static ErpType getErpType(String erp) {
		ErpType erpType=null;
		try{
			if(erp!=null && !erp.trim().isEmpty()) {
	
				if(erp.equalsIgnoreCase(ErpType.SX.name())) {
					erpType = ErpType.SX;
				} else if(erp.equalsIgnoreCase(ErpType.SXV10.name())) {
					erpType = ErpType.SXV10;
				} else if(erp.equalsIgnoreCase(ErpType.SXV6191.name())) {
					erpType = ErpType.SXV6191;
				} else if(erp.equalsIgnoreCase(ErpType.ECLIPSE.name())) {
					erpType = ErpType.ECLIPSE;
				} else if(erp.equalsIgnoreCase(ErpType.CIMM2BCENTRAL.name())) {
					erpType = ErpType.CIMM2BCENTRAL;
				}else if(erp.equalsIgnoreCase(ErpType.CIMMESB.name())) {
					erpType = ErpType.CIMMESB;
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return erpType;
	}
	
	public static AddressType getAddressType(String sAddressType) {
		AddressType addressType=null;
		try{
			if(sAddressType!=null && !sAddressType.trim().isEmpty()) {
				
				if(sAddressType.equalsIgnoreCase(AddressType.Ship.name())) {
					addressType = AddressType.Ship;
				} else if(sAddressType.equalsIgnoreCase(AddressType.Bill.name())) {
					addressType = AddressType.Bill;
				} 		
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return addressType;
	}
	

	public static String replaceSingleQuote(String inputString){
		try{
			if(inputString!=null && !inputString.isEmpty()){
				inputString=inputString.replace("'" ,"''");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return inputString;
	}
	
	public static String generateGUID(){
		  return java.util.UUID.randomUUID().toString();
	}

}
