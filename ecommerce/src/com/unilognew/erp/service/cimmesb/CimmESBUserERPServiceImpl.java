package com.unilognew.erp.service.cimmesb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpStatus;
import org.apache.struts2.ServletActionContext;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimmesb.utils.CimmESBServiceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unilog.cimmesb.client.request.RestRequest;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCResponse;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCustomer;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.security.SecureData;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilognew.erp.service.IUserERPService;
import com.unilognew.erp.service.cimm2bcentral.AbstractCimm2BCentralERPServiceImpl;
import com.unilognew.exception.ERPServiceException;
import com.unilognew.model.BuyingCompanyAddressBook;
import com.unilognew.model.Cimm2BCentralGetCustomerRequest;
import com.unilognew.model.ERPServiceRequest;
import com.unilognew.util.ECommerceEnumType.AddressType;

public class CimmESBUserERPServiceImpl extends AbstractCimm2BCentralERPServiceImpl implements IUserERPService{

	public static CimmESBUserERPServiceImpl cimm2bCentralUserERPServiceImpl;
	private HttpServletRequest request;

	private Logger logger = LoggerFactory.getLogger(CimmESBUserERPServiceImpl.class);


	private CimmESBUserERPServiceImpl() {
		super();
	}
	public static CimmESBUserERPServiceImpl getInstance() {
		synchronized (CimmESBUserERPServiceImpl.class) {
			if (cimm2bCentralUserERPServiceImpl == null) {
				cimm2bCentralUserERPServiceImpl = new CimmESBUserERPServiceImpl();
			}
		}
		cimm2bCentralUserERPServiceImpl.init();
		return cimm2bCentralUserERPServiceImpl;
	}

	@Override
	public Map<AddressType,List<BuyingCompanyAddressBook>> getBillToAndShipToAddressForSync(ERPServiceRequest billToShipToAddressRequest,ERPServiceRequest shipToAddressRequest) throws ERPServiceException {

		List<BuyingCompanyAddressBook> shipToAddressList = null;
		List<BuyingCompanyAddressBook> billToAddressList = null;
		BuyingCompanyAddressBook billToAddress = null;
		Map<AddressType,List<BuyingCompanyAddressBook>> allAddressesMap = null;
		boolean noShipTo=false;
		Cimm2BCentralGetCustomerRequest cimm2bCentralGetCustomerRequest = (Cimm2BCentralGetCustomerRequest) billToShipToAddressRequest;
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try{
			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(new SecureData().validatePassword(session.getAttribute("securedPassword").toString())) // web user name as the password.
					.build();
					
			@SuppressWarnings("rawtypes")
			Cimm2BCResponse<Cimm2BCustomer>  response = CimmESBServiceUtils.getCustomerService().getCustomerByErpId(request,cimm2bCentralGetCustomerRequest.getCustomerNumber());
			DozerBeanMapper  dozzer = new DozerBeanMapper();
			Cimm2BCentralCustomer customerDetails = new Cimm2BCentralCustomer();
			dozzer.map(response.getData(),customerDetails);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("Customer Response from CIMMESB:"+mapper.writeValueAsString(response));
			if(response != null && response.getStatus().getCode() == HttpStatus.SC_OK){  
				UsersModel customerinfo = null;
	
				if(customerDetails != null){
					customerinfo = new UsersModel();
	
					String warehouse ="";
					if(customerDetails.getDefaultShipLocationId()!=null && customerDetails.getDefaultShipLocationId().length()>0){
						warehouse =customerDetails.getDefaultShipLocationId();
					}else if(customerDetails.getHomeBranch()!=null && customerDetails.getHomeBranch().length()>0){
						warehouse =customerDetails.getHomeBranch();
					}else{
						warehouse= CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR"));
					}
					customerinfo.setEntityName(CommonUtility.validateString(customerDetails.getCustomerName()));
					customerinfo.setCustomerName(CommonUtility.validateString(customerDetails.getCustomerName()));
					customerinfo.setFirstName(CommonUtility.validateString(customerDetails.getCustomerName()));
					
					session.setAttribute("salesLocationId", warehouse);
					Cimm2BCentralAddress address = customerDetails.getAddress();
					
					if(!CommonUtility.doesBillAddressExist(address)) {
						if(customerDetails.getCustomerLocations() != null && customerDetails.getCustomerLocations().size() > 0) {
							address = customerDetails.getCustomerLocations().get(0).getAddress();
						}
						
					}
					if(address != null){
	
						if(CommonUtility.validateString(address.getAddressLine1()).length() > 0){
							customerinfo.setAddress1(CommonUtility.validateString(address.getAddressLine1()));
						}else{
							customerinfo.setAddress1("No Address");
						}
	
						if(CommonUtility.validateString(address.getAddressLine2()).length() > 0){
							customerinfo.setAddress2(CommonUtility.validateString(address.getAddressLine2()));
						}
						customerinfo.setCity(CommonUtility.validateString(address.getCity()));
						//customerinfo.setPhoneNo(address.getPhone());
						customerinfo.setState(CommonUtility.validateString(address.getState()));
						customerinfo.setZipCode(CommonUtility.validateString(address.getZipCode()));
	
						if(CommonUtility.validateString(address.getCountry()).length() > 0){
							if(CommonUtility.validateString(address.getCountry()).equalsIgnoreCase("USA")){
							customerinfo.setCountry("US");
							}else{
								customerinfo.setCountry(CommonUtility.validateString(address.getCountry()));
							}
						}else{
							customerinfo.setCountry("US");
						}
	
						if(customerDetails.getContacts() != null && customerDetails.getContacts().size() > 0){
							if(CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
								customerinfo.setPhoneNo(CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryPhoneNumber()));
							}else if(CommonUtility.validateString(customerDetails.getContacts().get(0).getAlternatePhoneNumber()).length() > 0){
								customerinfo.setPhoneNo(CommonUtility.validateString(customerDetails.getContacts().get(0).getAlternatePhoneNumber()));
							}
	
							if(CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryEmailAddress()).length() > 0){
								customerinfo.setEmailAddress(CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryEmailAddress()));
							}
						}
					}
	
					customerinfo.setTermsType(CommonUtility.validateString(customerDetails.getTermsType()));
					customerinfo.setTermsTypeDesc(CommonUtility.validateString(customerDetails.getTermsTypeDescription()));
					customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(warehouse).replaceFirst("^0+(?!$)", "")));
					customerinfo.setWareHouseCodeStr(CommonUtility.validateString(warehouse).replaceFirst("^0+(?!$)", ""));
					customerinfo.setCodFlag(customerDetails.getCodFlag()!=null?customerDetails.getCodFlag():false);
					customerinfo.setPoRequired(customerDetails.getPoRequired()!=null?customerDetails.getPoRequired():false);
	
					billToAddress = new BuyingCompanyAddressBook();
					billToAddress.setEntityId(CommonUtility.validateString(cimm2bCentralGetCustomerRequest.getCustomerNumber()));
					billToAddress.setFirstName(CommonUtility.validateString(customerinfo.getFirstName()));
					billToAddress.setLastName(CommonUtility.validateString(customerinfo.getLastName()));
					billToAddress.setAddress1(CommonUtility.validateString(customerinfo.getAddress1()));
					billToAddress.setAddress2(CommonUtility.validateString(customerinfo.getAddress2()));
					billToAddress.setCity(CommonUtility.validateString(customerinfo.getCity()));
					billToAddress.setPhone(CommonUtility.validateString(customerinfo.getPhoneNo()));
					billToAddress.setState(CommonUtility.validateString(customerinfo.getState()));
					billToAddress.setCountry(CommonUtility.validateString(customerinfo.getCountry()));
					billToAddress.setZipcode(CommonUtility.validateString(customerinfo.getZipCode()));
					billToAddress.setAddressType(AddressType.Bill);
					billToAddress.setCompanyName(CommonUtility.validateString(customerDetails.getCustomerName()));
					//billToAddressList.add(buyingCompanyAddressBook);
	
					ArrayList<Cimm2BCentralCustomer> customerLocations = customerDetails.getCustomerLocations();
	
					if(customerLocations != null && customerLocations.size() > 0){
						shipToAddressList = new ArrayList<BuyingCompanyAddressBook>();
						for(Cimm2BCentralCustomer shipTo : customerLocations){
							
							if(shipTo!=null){
								BuyingCompanyAddressBook shipAddressModel = new BuyingCompanyAddressBook();	
								Cimm2BCentralAddress shipAddress = shipTo.getAddress();
								
								if(shipAddress!=null){
									shipAddressModel.setAddress1(CommonUtility.validateString(shipAddress.getAddressLine1()));
									if(CommonUtility.validateString(shipAddress.getAddressLine2()).length() > 0){
										shipAddressModel.setAddress2(CommonUtility.validateString(shipAddress.getAddressLine2()));
									}
									shipAddressModel.setCity(CommonUtility.validateString(shipAddress.getCity()));
									if(CommonUtility.validateString(shipAddress.getCountry()).length() > 0){
										if(CommonUtility.validateString(shipAddress.getCountry()).equalsIgnoreCase("USA")){
											customerinfo.setCountry("US");
											shipAddressModel.setCountry("US");
											}else{
												customerinfo.setCountry(CommonUtility.validateString(shipAddress.getCountry()));
												shipAddressModel.setCountry(CommonUtility.validateString(shipAddress.getCountry()));
	
											}
									}else{
										shipAddressModel.setCountry("US");
									}
									shipAddressModel.setState(CommonUtility.validateString(shipAddress.getState()));
	 								shipAddressModel.setZipcode(CommonUtility.validateString(shipAddress.getZipCode()));
	 								shipAddressModel.setCompanyName(customerinfo.getCustomerName());
	 								shipAddressModel.setShipToName(customerinfo.getCustomerName());
	 								shipAddressModel.setShipToId(shipAddress.getAddressERPId());
		
								if(shipTo!=null && shipTo.getContacts() != null && shipTo.getContacts().size() > 0 && CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
									shipAddressModel.setPhone(CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()));
								}
								
								if(shipTo!=null && shipTo.getContacts() != null && shipTo.getContacts().size() > 0 && CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryEmailAddress()).length() > 0){
									shipAddressModel.setEmailAddress(CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryEmailAddress()));
								}
	 							if(CommonUtility.validateString(shipTo.getCustomerName()).length() > 0){
									shipAddressModel.setFirstName(CommonUtility.validateString(shipTo.getCustomerName()));
								}
								shipAddressModel.setAddressType(AddressType.Ship);
								shipAddressModel.setEntityId(shipTo.getCustomerERPId() != null ? shipTo.getCustomerERPId() : cimm2bCentralGetCustomerRequest.getCustomerNumber());
								shipToAddressList.add(shipAddressModel);
								}
							}
						}
	
					}
					
					/*
					String codCustomFiledValue = "N";
					String poRequiredCustomFiledValue = "N";
					String gasPoRequiredCustomFiledValue = "N";
					String fuelSurcharge="N";
					double minimumOrderAmount=0.0;
					double prepaidFreightAmount=0.0;
					String defaultShipVia=CommonUtility.validateString(customerDetails.getDefaultShipVia());
					String priceListPath="";
					String fullPriceListPath="";
					
					if(CommonUtility.validateString(customerDetails.getPriceListPath()).length()>0){
						priceListPath=CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MY_PRICING_HOST_URL"))+CommonUtility.validateString(customerDetails.getPriceListPath());
					}
					if(CommonUtility.validateString(customerDetails.getFullPriceListPath()).length()>0){
						fullPriceListPath=CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MY_PRICING_HOST_URL"))+CommonUtility.validateString(customerDetails.getFullPriceListPath());
					}
					if(customerDetails.getCodFlag()!=null && customerDetails.getCodFlag()){
						codCustomFiledValue = "Y";
					}
					if(customerDetails.getPoRequired()!=null && customerDetails.getPoRequired()){
						poRequiredCustomFiledValue = "Y";
					}
					if(customerDetails.getGasPoRequired()!=null && customerDetails.getGasPoRequired()){
						gasPoRequiredCustomFiledValue = "Y";
					}
					if(customerDetails.getFuelSurcharge()!=null && customerDetails.getFuelSurcharge()){
						fuelSurcharge="Y";
					}
					if(customerDetails.getMinimumOrderAmount()>0){
						minimumOrderAmount = customerDetails.getMinimumOrderAmount();
					}
					if(customerDetails.getPrepaidFreightAmount()>0){
						prepaidFreightAmount = customerDetails.getPrepaidFreightAmount();
					}
					*/
					/*Please use the services to inser custom field data
					 * 
					UsersDAO.insertCustomField(codCustomFiledValue, "COD", cimm2bCentralGetCustomerRequest.getUserId(), cimm2bCentralGetCustomerRequest.getBuyingCompanyId(), "BUYING_COMPANY");
					UsersDAO.insertCustomField(poRequiredCustomFiledValue, "PO_REQUIRED", cimm2bCentralGetCustomerRequest.getUserId(), cimm2bCentralGetCustomerRequest.getBuyingCompanyId(), "BUYING_COMPANY");
					UsersDAO.insertCustomField(gasPoRequiredCustomFiledValue, "GAS_PO_REQUIRED", cimm2bCentralGetCustomerRequest.getUserId(), cimm2bCentralGetCustomerRequest.getBuyingCompanyId(), "BUYING_COMPANY");
					UsersDAO.insertCustomField(CommonUtility.validateString(defaultShipVia), "SHIPPING_METHOD", cimm2bCentralGetCustomerRequest.getUserId(), cimm2bCentralGetCustomerRequest.getBuyingCompanyId(), "BUYING_COMPANY");
					UsersDAO.insertCustomField(CommonUtility.validateString(fuelSurcharge), "FUEL_SURCHARGE", cimm2bCentralGetCustomerRequest.getUserId(), cimm2bCentralGetCustomerRequest.getBuyingCompanyId(), "BUYING_COMPANY");
					UsersDAO.insertCustomField(CommonUtility.validateParseDoubleToString(minimumOrderAmount), "MINIMUM_ORDER_AMOUNT", cimm2bCentralGetCustomerRequest.getUserId(), cimm2bCentralGetCustomerRequest.getBuyingCompanyId(), "BUYING_COMPANY");
					UsersDAO.insertCustomField(CommonUtility.validateParseDoubleToString(prepaidFreightAmount), "PREPAID_FREIGHT_TARGET_AMOUNT", cimm2bCentralGetCustomerRequest.getUserId(), cimm2bCentralGetCustomerRequest.getBuyingCompanyId(), "BUYING_COMPANY");
					UsersDAO.insertCustomField(CommonUtility.validateString(priceListPath), "MY_PRICING_LINK_ONE", cimm2bCentralGetCustomerRequest.getUserId(), cimm2bCentralGetCustomerRequest.getBuyingCompanyId(), "BUYING_COMPANY");
					UsersDAO.insertCustomField(CommonUtility.validateString(fullPriceListPath), "MY_PRICING_LINK_TWO", cimm2bCentralGetCustomerRequest.getUserId(), cimm2bCentralGetCustomerRequest.getBuyingCompanyId(), "BUYING_COMPANY");
					*/
				}
			}
	
			allAddressesMap = new HashMap<AddressType,List<BuyingCompanyAddressBook>>();
	
			if(shipToAddressList==null) {
				shipToAddressList = new ArrayList<BuyingCompanyAddressBook>();
				noShipTo=true;
			} else if (shipToAddressList.isEmpty()) {
				noShipTo=true;
			}
	
			if(noShipTo) {
				BuyingCompanyAddressBook shipToAddress = billToAddress.clone();
				shipToAddress.setAddressType(AddressType.Ship);
				shipToAddress.setShipToName(CommonUtility.validateString(shipToAddress.getFirstName()));
				shipToAddressList.add(shipToAddress);
			}
	
			billToAddressList = new ArrayList<BuyingCompanyAddressBook>();				
			billToAddressList.add(billToAddress);
	
			allAddressesMap.put(AddressType.Bill, billToAddressList);
			allAddressesMap.put(AddressType.Ship, shipToAddressList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return allAddressesMap;
	}


	@Override
	public List<BuyingCompanyAddressBook> getCustomerShipToAddressList(
			ERPServiceRequest shipToAddressRequest) throws ERPServiceException {
		
		return null;
	}

	@Override
	public BuyingCompanyAddressBook getCustomerBillToAddress(
			ERPServiceRequest billToAddressRequest) throws ERPServiceException {
		
		return null;
	}
}
