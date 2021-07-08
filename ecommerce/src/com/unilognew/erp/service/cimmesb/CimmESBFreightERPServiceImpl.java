package com.unilognew.erp.service.cimmesb;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.service.cimmesb.utils.CimmESBServiceUtils;
import com.erp.service.cimmesb.utils.FreightServiceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier;
import com.unilog.cimmesb.client.request.RestRequest;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCResponse;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.sales.SalesModel;
import com.unilog.security.SecureData;
import com.unilog.utility.CommonUtility;
import com.unilognew.erp.service.FreightERPService;
import com.unilognew.erp.service.cimm2bcentral.AbstractCimm2BCentralERPServiceImpl;

public class CimmESBFreightERPServiceImpl extends AbstractCimm2BCentralERPServiceImpl implements FreightERPService {
	public static CimmESBFreightERPServiceImpl cimmEsbFreightERPServiceImpl;
	private HttpServletRequest request;

	private Logger logger = LoggerFactory.getLogger(CimmESBFreightERPServiceImpl.class);


	private CimmESBFreightERPServiceImpl() {
		super();
	}
	public static CimmESBFreightERPServiceImpl getInstance() {
		synchronized (CimmESBFreightERPServiceImpl.class) {
			if (cimmEsbFreightERPServiceImpl == null) {
				cimmEsbFreightERPServiceImpl = new CimmESBFreightERPServiceImpl();
			}
		}
		cimmEsbFreightERPServiceImpl.init();
		return cimmEsbFreightERPServiceImpl;
	}

	public ArrayList<SalesModel> getUpsFreightCharges(SalesModel freightParameters){
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		ArrayList<SalesModel> FreightResponseData = new ArrayList<SalesModel>();
		SalesModel FreightDetailModel = new SalesModel();
		try{
			com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier FrieghtRequest = FreightServiceUtils.getUpsFrieghtModelEsb(freightParameters);
			SecureData password=new SecureData();
			RestRequest<com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier> request = RestRequest.<com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
					.withBody(FrieghtRequest)
					.build();
			
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("CIMM ESB UPS Frieght Request:"+mapper.writeValueAsString(FrieghtRequest));
				
			Cimm2BCResponse<CimmShippingCarrier> response = CimmESBServiceUtils.getFreightService().calculateUPStRate(request);
			System.out.println("CIMM ESB UPS Frieght Response:"+mapper.writeValueAsString(response));
			CimmShippingCarrier freight=null;
			if(response!=null && response.getData() != null &&  response.getStatus().getCode() == 200 ){
				freight = new CimmShippingCarrier();
				freight = response.getData();
				if(freight.getTransportationCharges()!=null){
				FreightDetailModel.setTransportationCharges(freight.getTransportationCharges());
				}
				if(freight.getServiceCharges()!=null){
				FreightDetailModel.setServiceCharges(freight.getServiceCharges());
				}
				if(freight.getTotalCharges()!=null){
				FreightDetailModel.setTotalCharges(freight.getTotalCharges());
				FreightDetailModel.setCurrency(freight.getTotalCharges().getCurrencyCode()!=null?freight.getTotalCharges().getCurrencyCode():"");
				FreightDetailModel.setDeliveryCharge(freight.getTotalCharges().getCost()!=null?CommonUtility.validateDoubleNumber(freight.getTotalCharges().getCost()):0.0);
				}
				FreightResponseData.add(FreightDetailModel);
			
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return FreightResponseData;
	}
	
	public ArrayList<SalesModel> getFedexFreightCharges(SalesModel freightParameters){
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		ArrayList<SalesModel> FreightResponseData = new ArrayList<SalesModel>();
		SalesModel FreightDetailModel = new SalesModel();
		try{
			com.unilog.cimmesb.client.ecomm.request.FedExFreight FrieghtRequest = FreightServiceUtils.getFedexFreightModelEsb(freightParameters);
			SecureData password=new SecureData();
			RestRequest<com.unilog.cimmesb.client.ecomm.request.FedExFreight> request = RestRequest.<com.unilog.cimmesb.client.ecomm.request.FedExFreight>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
					.withBody(FrieghtRequest)
					.build();
			
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("CIMM ESB FEDEX Frieght Request:"+mapper.writeValueAsString(FrieghtRequest));
				
			Cimm2BCResponse<CimmShippingCarrier> response = CimmESBServiceUtils.getFreightService().calculateFreightRate(request); // Fedex
			System.out.println("CIMM ESB FEDEX Frieght Response:"+mapper.writeValueAsString(response));
			CimmShippingCarrier freight=null;
			if(response!=null && response.getData() != null &&  response.getStatus().getCode() == 200 ){
				freight = new CimmShippingCarrier();
				freight = response.getData();
				if(freight.getServiceSummary()!=null){
				FreightDetailModel.setTotalCharges(freight.getServiceSummary().get(0).getTotalCharges());
				FreightDetailModel.setCurrency(freight.getServiceSummary().get(0).getTotalCharges().getCurrencyCode()!=null?freight.getServiceSummary().get(0).getTotalCharges().getCurrencyCode():"");
				FreightDetailModel.setDeliveryCharge(freight.getServiceSummary().get(0).getTotalCharges().getCost()!=null?CommonUtility.validateDoubleNumber(freight.getServiceSummary().get(0).getTotalCharges().getCost()):0.0);
				}
				FreightResponseData.add(FreightDetailModel);
			
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return FreightResponseData;
	}
	
}
	

