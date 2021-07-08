package com.unilognew.erp.service.cimm2bcentral;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.unilog.sales.SalesModel;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;
import com.unilognew.erp.service.FreightERPService;
import com.unilognew.erp.service.cimm2bcentral.AbstractCimm2BCentralERPServiceImpl;
import com.unilognew.exception.ERPServiceException;

public class Cimm2bCentralFreightERPServiceImpl extends AbstractCimm2BCentralERPServiceImpl implements FreightERPService {
	public static Cimm2bCentralFreightERPServiceImpl Cimm2bCentralFreightERPServiceImpl;
	private HttpServletRequest request;

	private Logger logger = LoggerFactory.getLogger(Cimm2bCentralFreightERPServiceImpl.class);


	private Cimm2bCentralFreightERPServiceImpl() {
		super();
	}
	public static Cimm2bCentralFreightERPServiceImpl getInstance() {
		synchronized (Cimm2bCentralFreightERPServiceImpl.class) {
			if (Cimm2bCentralFreightERPServiceImpl == null) {
				Cimm2bCentralFreightERPServiceImpl = new Cimm2bCentralFreightERPServiceImpl();
			}
		}
		Cimm2bCentralFreightERPServiceImpl.init();
		return Cimm2bCentralFreightERPServiceImpl;
	}
	public ArrayList<SalesModel> getUpsFreightCharges(SalesModel freightParameters){	
		ArrayList<SalesModel> FreightResponseData = new ArrayList<SalesModel>();
		SalesModel FreightDetailModel = new SalesModel();
		try{
			String upsFreightChargesUrl = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPS_FREIGHT_RATE_URL"));
			UpsFreight upsFrieghtModel = FreightServiceUtils.getUpsFrieghtModelData(freightParameters);
			Cimm2BCentralResponseEntity upsFreightResponse = Cimm2BCentralClient.getInstance().getDataObject(upsFreightChargesUrl, "POST", upsFrieghtModel, JsonObject.class);
			/*if(upsFreightResponse!=null && upsFreightResponse.getData() != null &&  upsFreightResponse.getStatus().getCode() == 200 ){
				if(upsFreightResponse.getTransportationCharges()!=null){
					upsFreightResponse.setTransportationCharges(upsFreightResponse.getTransportationCharges());
					}
					if(upsFreightResponse.getServiceCharges()!=null){
					FreightDetailModel.setServiceCharges(upsFreightResponse.getServiceCharges());
					}
					if(upsFreightResponse.getTotalCharges()!=null){
					FreightDetailModel.setTotalCharges(upsFreightResponse.getTotalCharges());
					}
					if(upsFreightResponse.getTotalCharges().getCurrencyCode()!=null){
					FreightDetailModel.setCurrency(upsFreightResponse.getTotalCharges().getCurrencyCode());
					}
					if(upsFreightResponse.getTotalCharges().getCost()!=null){
					FreightDetailModel.setDeliveryCharge(CommonUtility.validateDoubleNumber(upsFreightResponse.getTotalCharges().getCost()));
					}
					FreightResponseData.add(FreightDetailModel);
				
				
			}*/
		}catch(Exception e){
			e.printStackTrace();
		}
		return FreightResponseData;
	}
	public double getFreightChargesFromERP(UsersModel userShipTo, WarehouseModel wareHouseDetails, List<ProductsModel> products, String serviceCode, String serviceName){
		double totalFreightCharge = 0;
		try{
			String upsFreightChargesUrl = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPS_FREIGHT_RATE_URL"));
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
			String timeInTrasitUrl = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPS_TIME_IN_TRABSIT_URL"));
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
			String addressValidationUrl = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPS_ADDRESS_VALIDATION_URL"));
			Cimm2BCentralAddress cimm2bcAddressModel = FreightServiceUtils.getCIMM2BCAddressModel(address);
			Cimm2BCentralResponseEntity addressValidationResponse = Cimm2BCentralClient.getInstance().getDataObject(addressValidationUrl, "POST", cimm2bcAddressModel, JsonObject.class);
			if(addressValidationResponse!=null && addressValidationResponse.getData() != null &&  addressValidationResponse.getStatus().getCode() == 200 ){
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public ArrayList<SalesModel> getFedexFreightCharges(SalesModel freightParameters) throws ERPServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
