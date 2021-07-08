package com.erp.service.cimm2bcentral.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.dozer.DozerBeanMapper;

import com.erp.service.cimm2bcentral.models.FedExFreight;
import com.erp.service.cimm2bcentral.models.PackageDimension;
import com.erp.service.cimm2bcentral.models.PackageInfo;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.FreightServiceUtils;
import com.erp.service.cimmesb.utils.CimmESBServiceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier;
import com.unilog.cimmesb.client.request.RestRequest;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCResponse;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.security.SecureData;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

public class FedExFreightSerivce {
	public double getFedExCharges(UsersModel userShipTo, WarehouseModel wareHouseDetails, PackageInfo packageInfo, PackageDimension packageDimension, String dropType, String serviceName, String packageType,List<ProductsModel> cartListdata){
		String fedExApiUrl  = CommonDBQuery.getSystemParamtersList().get("FEDEX_FREIGT_RATE");
		double totalFreightCost = 0;
		try{
			FedExFreight fedExRequest = FreightServiceUtils.getFedExFreightModel(userShipTo, wareHouseDetails, packageInfo, packageDimension, dropType, serviceName, packageType, cartListdata);
			Cimm2BCentralResponseEntity fedExResponse = Cimm2BCentralClient.getInstance().getDataObject(fedExApiUrl, "POST", fedExRequest, JsonObject.class);
			if(fedExResponse!=null && fedExResponse.getData() != null &&  fedExResponse.getStatus().getCode() == 200 ){
				JsonParser parser = new JsonParser();
				String jsonData = fedExResponse.getData().toString();
				JsonObject responseData = (JsonObject) parser.parse(jsonData);
				JsonArray fedExServiceSummary = (JsonArray) responseData.get("serviceSummary");
				for(int i=0; i < fedExServiceSummary.size(); i++){
					JsonObject eachService = (JsonObject) fedExServiceSummary.get(i);
					JsonObject totalCharges = (JsonObject) eachService.get("totalCharges");
					totalFreightCost = Double.parseDouble(totalCharges.get("cost").toString().replaceAll("[^0-9\\.]", " "));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return totalFreightCost;
	}
	public double getFedExChargesEsb(UsersModel userShipTo, WarehouseModel wareHouseDetails, com.unilog.cimmesb.client.ecomm.request.PackageInfo packageInfo, com.unilog.cimmesb.client.ecomm.request.PackageDimension packageDimension, String dropType, String serviceName, String packageType,List<ProductsModel> cartListdata){
		com.unilog.cimmesb.client.ecomm.request.FedExFreight fedExRequest= new com.unilog.cimmesb.client.ecomm.request.FedExFreight();
		double totalFreightCost = 0.0;
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		SalesModel FreightDetailModel = new SalesModel();
				try {
					com.unilog.cimmesb.client.ecomm.request.FedExFreight fedExRequest1= FreightServiceUtils.getFedExFreightModelEsb(userShipTo, wareHouseDetails, packageInfo, packageDimension, dropType, serviceName, packageType, cartListdata);
					SecureData password=new SecureData();
					RestRequest<com.unilog.cimmesb.client.ecomm.request.FedExFreight> request = RestRequest.<com.unilog.cimmesb.client.ecomm.request.FedExFreight>builder()
							.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
							.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
							.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
							.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
							.withBody(fedExRequest1)
							.build();
					ObjectMapper mapper = new ObjectMapper();
					System.out.println("CIMM ESB fedex Request:"+mapper.writeValueAsString(fedExRequest1));

					Cimm2BCResponse<CimmShippingCarrier> response = CimmESBServiceUtils.getFreightService().calculateFreightRate(request);					
					System.out.println("CIMM ESB Frieght Response:"+mapper.writeValueAsString(response));
					
					DozerBeanMapper  dozzer = new DozerBeanMapper();
					CimmShippingCarrier freight = new CimmShippingCarrier();
					if(response!=null && response.getData() != null &&  response.getStatus().getCode() == 200 ){  
					dozzer.map(response.getData(),freight);
						if(freight.getTotalCharges()!=null){
						FreightDetailModel.setCurrency(freight.getTotalCharges().getCurrencyCode()!=null?freight.getTotalCharges().getCurrencyCode():"");
						FreightDetailModel.setDeliveryCharge(freight.getTotalCharges().getCost()!=null?CommonUtility.validateDoubleNumber(freight.getTotalCharges().getCost()):0.0);
						totalFreightCost = FreightDetailModel.getDeliveryCharge();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		return totalFreightCost;
	}
}
