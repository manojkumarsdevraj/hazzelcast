package com.unilognew.service;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unilog.database.CommonDBQuery;
import com.unilog.sales.SalesModel;
import com.unilog.utility.CommonUtility;
import com.unilognew.erp.service.FreightERPService;
import com.unilognew.erp.service.provider.ERPServicesFactory;
import com.unilognew.exception.ERPServiceException;
import com.unilognew.util.ECommerceEnumType.ErpType;

public class FreightService {
	private static Logger logger = LoggerFactory.getLogger(UserFacadeService.class);
	private static FreightService freightService;
	private ExecutorService executorService;

	private FreightService() {
		//
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public static FreightService getInstance() {
		synchronized (FreightService.class) {
			if (freightService == null) {
				freightService = new FreightService();
			}
		}

		return freightService;
	}

	public static Double getFreightData(SalesModel freightParameters, ErpType erpType) throws ERPServiceException {

		FreightERPService freightERPService = null;
		ArrayList<SalesModel> freightDataList = null;
		double totalCartFrieghtCharges = 0;
		String shipViaDisplay = freightParameters.getShipViaMethod();
		String shipViaServiceName = freightParameters.getShipViaServiceName();
		double totalCartFrieght = freightParameters.getTotalCartFrieght();
		freightERPService = ERPServicesFactory.getFreightERPService(erpType);

		try {

			switch (erpType) {

			case CIMM2BCENTRAL:
				if (CommonUtility.validateString(shipViaDisplay).toUpperCase(Locale.US).contains(shipViaServiceName.toUpperCase(Locale.US))) {

					double thresholdWeightLimit = CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("MAXIMUM_CARTWEIGHT_FOR_SHIPPING"));
					if (totalCartFrieght <= thresholdWeightLimit) {
						freightDataList = freightERPService.getUpsFreightCharges(freightParameters);
					} else {
						int multiple = (int) (totalCartFrieght / thresholdWeightLimit);
						double balanceweight = totalCartFrieght - (thresholdWeightLimit * multiple);
						freightParameters.setTotalCartFrieght(thresholdWeightLimit);
						freightDataList = freightERPService.getUpsFreightCharges(freightParameters);
						if (balanceweight > 0) {
							freightParameters.setTotalCartFrieght(balanceweight);
							freightDataList = freightERPService.getUpsFreightCharges(freightParameters);
						}
					}
					if (freightDataList != null && !freightDataList.isEmpty()) {
						totalCartFrieghtCharges = freightDataList.get(0).getDeliveryCharge();
					}
				} else if (CommonUtility.validateString(shipViaDisplay).toUpperCase(Locale.US).contains("FEDEX")) {

				}
				break;

			case CIMMESB:

				if(CommonUtility.validateString(shipViaDisplay).toUpperCase(Locale.US).contains(shipViaServiceName.toUpperCase(Locale.US))) {

					double thresholdWeightLimit = CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("MAXIMUM_CARTWEIGHT_FOR_SHIPPING"));
					if (totalCartFrieght <= thresholdWeightLimit) {
						freightDataList = freightERPService.getUpsFreightCharges(freightParameters);
					} else {
						int multiple = (int) (totalCartFrieght / thresholdWeightLimit);
						double balanceweight = totalCartFrieght - (thresholdWeightLimit * multiple);
						freightParameters.setTotalCartFrieght(thresholdWeightLimit);
						freightDataList = freightERPService.getUpsFreightCharges(freightParameters);
						if (balanceweight > 0) {
							freightParameters.setTotalCartFrieght(balanceweight);
							freightDataList = freightERPService.getUpsFreightCharges(freightParameters);
						}
					}
					if (freightDataList != null && !freightDataList.isEmpty()) {
						totalCartFrieghtCharges = freightDataList.get(0).getDeliveryCharge();
					}
				} else if (CommonUtility.validateString(shipViaDisplay).toUpperCase(Locale.US).contains("FEDEX")) {
					freightDataList = freightERPService.getFedexFreightCharges(freightParameters);
					if (freightDataList != null && !freightDataList.isEmpty()) {
						totalCartFrieghtCharges = freightDataList.get(0).getDeliveryCharge();
					}
				}
				break;

			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return totalCartFrieghtCharges;
	}

}
