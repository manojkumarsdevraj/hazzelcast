package com.erp.service.cimm2bcentral.service;

import java.util.List;

import com.erp.service.cimm2bcentral.models.TimeInTransit;
import com.erp.service.cimm2bcentral.models.UpsFreight;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.FreightServiceUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;

public class UpsFreightService {
	
	public double getFreightCharges(UsersModel userShipTo, WarehouseModel wareHouseDetails, double totalCartWeight, String serviceCode, String serviceName){
		double totalFreightCharge = 0;
		try{
			String upsFreightChargesUrl = CommonDBQuery.getSystemParamtersList().get("UPS_FREIGHT_RATE_URL");
			UpsFreight upsFrieghtModel = FreightServiceUtils.getUpsFrieghtModel(userShipTo, wareHouseDetails, totalCartWeight, serviceCode, serviceName);
			Cimm2BCentralResponseEntity upsFreightResponse = Cimm2BCentralClient.getInstance().getDataObject(upsFreightChargesUrl, "POST", upsFrieghtModel, JsonObject.class);
			if(upsFreightResponse!=null && upsFreightResponse.getData() != null &&  upsFreightResponse.getStatus().getCode() == 200 ){
				JsonParser parser = new JsonParser();
				String jsonData = upsFreightResponse.getData().toString();
				JsonObject responseData = (JsonObject) parser.parse(jsonData);
				
				JsonObject totalCharges = (JsonObject) responseData.get("totalCharges");
				
				if(totalCharges != null){
					String freightCoststr = totalCharges.get("cost").toString().replaceAll("[^0-9\\.]", " ");
					
					totalFreightCharge = Double.parseDouble(freightCoststr);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return totalFreightCharge;
	}
	public double getFreightCharges(UsersModel userShipTo, WarehouseModel wareHouseDetails, List<ProductsModel> products, String serviceCode, String serviceName){
		double totalFreightCharge = 0;
		try{
			String upsFreightChargesUrl = CommonDBQuery.getSystemParamtersList().get("UPS_FREIGHT_RATE_URL");
			UpsFreight upsFrieghtModel = FreightServiceUtils.getUpsFrieghtModelByItems(userShipTo, wareHouseDetails, products, serviceCode, serviceName);
			Cimm2BCentralResponseEntity upsFreightResponse = Cimm2BCentralClient.getInstance().getDataObject(upsFreightChargesUrl, "POST", upsFrieghtModel, JsonObject.class);
			if(upsFreightResponse!=null && upsFreightResponse.getData() != null &&  upsFreightResponse.getStatus().getCode() == 200 ){
				JsonParser parser = new JsonParser();
				String jsonData = upsFreightResponse.getData().toString();
				JsonObject responseData = (JsonObject) parser.parse(jsonData);
				
				JsonObject totalCharges = (JsonObject) responseData.get("totalCharges");
				
				if(totalCharges != null){
					
					totalFreightCharge = Double.parseDouble(totalCharges.get("cost").getAsString());
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return totalFreightCharge;
	}
	
	public JsonObject getTimeInTransit(UsersModel userShipTo, WarehouseModel wareHouseDetails,double weight, String pickUpDate){
		try{
			String timeInTrasitUrl = CommonDBQuery.getSystemParamtersList().get("UPS_TIME_IN_TRABSIT_URL");
			TimeInTransit timeInTransitModel = FreightServiceUtils.getTimeInTransitModel(userShipTo, wareHouseDetails, weight, pickUpDate);
			Cimm2BCentralResponseEntity timeInTransitResponse = Cimm2BCentralClient.getInstance().getDataObject(timeInTrasitUrl, "POST", timeInTransitModel, JsonObject.class);
			if(timeInTransitResponse!=null && timeInTransitResponse.getData() != null &&  timeInTransitResponse.getStatus().getCode() == 200 ){
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public JsonObject validateAddress(AddressModel address){
		try{
			String addressValidationUrl = CommonDBQuery.getSystemParamtersList().get("UPS_ADDRESS_VALIDATION_URL");
			Cimm2BCentralAddress cimm2bcAddressModel = FreightServiceUtils.getCIMM2BCAddressModel(address);
			Cimm2BCentralResponseEntity addressValidationResponse = Cimm2BCentralClient.getInstance().getDataObject(addressValidationUrl, "POST", cimm2bcAddressModel, JsonObject.class);
			if(addressValidationResponse!=null && addressValidationResponse.getData() != null &&  addressValidationResponse.getStatus().getCode() == 200 ){
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
