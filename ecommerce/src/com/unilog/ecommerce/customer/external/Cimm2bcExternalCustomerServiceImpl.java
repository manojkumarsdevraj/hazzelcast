package com.unilog.ecommerce.customer.external;

import javax.ws.rs.HttpMethod;

import org.apache.log4j.Logger;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralContact;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.unilog.database.CommonDBQuery;
import com.unilog.ecommerce.customer.model.Customer;
import com.unilog.ecommerce.user.model.User;

public class Cimm2bcExternalCustomerServiceImpl implements ExternalCustomerService{
	protected static ExternalCustomerService customerExternalService;
	
	private static final Logger logger = Logger.getLogger(Cimm2bcExternalCustomerServiceImpl.class);
	
	public static ExternalCustomerService getInstance() {
		synchronized (Cimm2bcExternalCustomerServiceImpl.class) {
			if(customerExternalService == null) {
				customerExternalService = new Cimm2bcExternalCustomerServiceImpl();
			}
		}
		return customerExternalService;
	}
	
	@Override
	public Customer getCustomer(String accountNumber) {
		Customer customer = null;
		try {
			String getCustomerApi = CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API") + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  accountNumber;
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(getCustomerApi, HttpMethod.GET, null, Cimm2BCentralCustomer.class);
			if(response != null && response.getData() != null &&  response.getStatus().getCode() == 200 ) {
				Cimm2BCentralCustomer rawDetails = (Cimm2BCentralCustomer) response.getData();
				customer = CustomerMappingUtil.extractGetCustomerDetails(rawDetails);
			}
		}catch (Exception e) {
			logger.error("Exception while fetching customer details", e);
		}
		return customer;
	}

	@Override
	public Customer createCustomer(Customer customer) {
		try {
			String createCustomerApi = CommonDBQuery.getSystemParamtersList().get("CREATE_CUSTOMER_API");
			Cimm2BCentralCustomer rawCustomer = CustomerMappingUtil.mapCustomerToRequestBody(customer);
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(createCustomerApi, HttpMethod.POST, rawCustomer, Cimm2BCentralContact.class);
			if(response != null && response.getData() != null &&  response.getStatus().getCode() == 200 ) {
				customer = CustomerMappingUtil.extractCreateCustomerDetails(customer, (Cimm2BCentralContact)response.getData());
			}else {
				customer = null;
			}
		}catch (Exception e) {
			customer = null;
			logger.error("Exception occred while create customer in external system ", e);
		}
		return customer;
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		
		return null;
	}

	@Override
	public int addShipAddress(User user) {
		
		return 0;
	}

}
