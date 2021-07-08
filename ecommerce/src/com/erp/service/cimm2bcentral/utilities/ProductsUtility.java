package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsModel;
import com.unilog.utility.CommonUtility;

public class ProductsUtility {


	public static String syncCPN(int itemId, int buyingCompanyId, int userId, ArrayList<ProductsModel> customerPartNumbers, String entityId, String useBillToEntity, String partNumber, String shiptoId){
		String result = "0|";
		int count = 0;
		LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartnumber = ProductHunterSolr.getcustomerPartnumber(CommonUtility.validateParseIntegerToString(itemId), buyingCompanyId, buyingCompanyId);
		ArrayList<ProductsModel> customerPartNumberList = customerPartnumber.get(itemId);
		if(customerPartNumberList!=null && customerPartNumberList.size() > 0){
			String[] custPartList = new String[customerPartNumberList.size()];

			for (int i = 0; i < customerPartNumberList.size(); i++) {
				custPartList[i] = customerPartNumberList.get(i).getIdList();
			}

			count = ProductHunterSolr.removeAllCustomerPartNumber(custPartList, itemId, buyingCompanyId);
			if(count == 0){
				result = "1|";
			}
		}
		if(customerPartNumbers!=null && customerPartNumbers.size() > 0){
			for (int i = 0; i < customerPartNumbers .size(); i++) {
				if(i!=customerPartNumbers.size()-1){
					result = result + customerPartNumbers.get(i).getPartNumber() + ", ";
				}else{
					result = result + customerPartNumbers.get(i).getPartNumber();
				}

				if(CommonUtility.validateString(customerPartNumbers.get(i).getRecordType()).equalsIgnoreCase("c")){
					shiptoId = "";
				}

				count = ProductHunterSolr.addNewCustomerPartNumber(customerPartNumbers.get(i).getPartNumber(), itemId, userId, CommonUtility.validateNumber(entityId), useBillToEntity, buyingCompanyId, shiptoId, customerPartNumbers.get(i).getRecordType(), customerPartNumbers.get(i).getRecordDescription());
				if(count == 0){
					result = "1|";
				}
			}
		}

		return result;
	}
}
