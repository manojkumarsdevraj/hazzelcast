package com.erp.service.cimmesb.action;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.dozer.DozerBeanMapper;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalance;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceData;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceSummary;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceSummaryRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAcceptHostedPageSettings;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAcceptHostedTokenRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralContact;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCylinderBalanceSummary;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralGetInvoiceList;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralNearestWareHouseResponse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipToStoreWarehouseCode;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralUser;
import com.erp.service.cimmesb.utils.CimmESBServiceUtils;
import com.erp.service.model.UserManagementModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unilog.cimmesb.client.ecomm.request.ChangePasswordRequest;
import com.unilog.cimmesb.client.ecomm.request.CimmAddressRequest;
import com.unilog.cimmesb.client.ecomm.request.CimmContactRequest;
import com.unilog.cimmesb.client.ecomm.request.CimmCustomerRequest;
import com.unilog.cimmesb.client.ecomm.request.ECommCimmUserRequest;
import com.unilog.cimmesb.client.ecomm.response.ECommUserResponse;
import com.unilog.cimmesb.client.ecomm.response.ExternalTokenResponse;
import com.unilog.cimmesb.client.exception.RestClientException;
import com.unilog.cimmesb.client.request.RestRequest;
import com.unilog.cimmesb.client.response.CimmCustomerResponse;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCResponse;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCustomer;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BPagedResponse;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BShippingMethod;
import com.unilog.cimmesb.ecomm.cimm2bc.model.CimmARBalanceDetails;
import com.unilog.cimmesb.ecomm.cimm2bc.model.CimmARBalanceSummaryResponse;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.CustomFieldModel;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailModel;
import com.unilog.defaults.SendMailUtility;
import com.unilog.security.LoginAuthentication;
import com.unilog.security.SecureData;
import com.unilog.users.AddressModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UserRegisterUtility;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class UserManagementAction {
	static final Logger logger = Logger.getLogger(UserManagementAction.class);
	public static String createCommertialCustomer(AddressModel addressModel)
	{
		HttpServletRequest request;
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		String result = "User Registration failed";
		try
		{
			boolean isValidUser = true;
			String locUser = "N";
			String userRole = null;
			String buyingComanyIdStr = null;
			String parentUserId = "0";
			String custNumber = null;
			UsersModel erpIds = null;

			if(addressModel.getLocUser()!=null && addressModel.getLocUser().trim().length()>0){
				locUser = addressModel.getLocUser();
			}

			if(addressModel.getRole()!=null && addressModel.getRole().trim().length()>0){
				userRole = addressModel.getRole();
			}

			if(addressModel.getBuyingComanyIdStr()!=null && addressModel.getBuyingComanyIdStr().trim().length()>0){
				buyingComanyIdStr = addressModel.getBuyingComanyIdStr();
			}

			if(addressModel.getBuyingComanyIdStr()!=null && addressModel.getBuyingComanyIdStr().trim().length()>0){
				buyingComanyIdStr = addressModel.getBuyingComanyIdStr();
			}

			if(addressModel.getParentUserId()!=null && addressModel.getParentUserId().trim().length()>0){
				parentUserId = addressModel.getParentUserId();
			}

			String buyingCompanyId = "";


			if(isValidUser){
				// new user registration process.
				UsersModel billingAddress = new UsersModel();
				UsersModel shippingAddress = null;
				UsersModel contactInformation = new UsersModel();



				if(addressModel.getCompanyName()!=null && addressModel.getCompanyName().trim().length()>0){
					billingAddress.setCustomerName(addressModel.getCompanyName());//billingAddress.setCustomerName(company);
					contactInformation.setCustomerName(addressModel.getCompanyName());
				}else{
					billingAddress.setCustomerName(addressModel.getFirstName()+" "+addressModel.getLastName());//billingAddress.setCustomerName(company);
					contactInformation.setCustomerName(addressModel.getFirstName()+" "+addressModel.getLastName());
				}

				billingAddress.setFirstName(addressModel.getFirstName());
				billingAddress.setLastName(addressModel.getLastName());
				billingAddress.setEntityName(buyingCompanyId); // 
				billingAddress.setAddress1(addressModel.getAddress1());
				billingAddress.setAddress2(addressModel.getAddress2());
				billingAddress.setCity(addressModel.getCity());
				billingAddress.setState(addressModel.getState());
				billingAddress.setPobox("");
				if(addressModel.getCountry().equalsIgnoreCase("USA")){
					billingAddress.setCountry("US");
				}else{
					billingAddress.setCountry(addressModel.getCountry());
				}
				billingAddress.setCountryFullName("");
				billingAddress.setPhoneNo(addressModel.getPhoneNo());
				billingAddress.setFaxNo("");
				billingAddress.setEmailAddress(addressModel.getEmailAddress());
				billingAddress.setWebAddress("");
				billingAddress.setZipCode(addressModel.getZipCode());
				billingAddress.setHomeBranch("");
				billingAddress.setTermsType(addressModel.getTermsType());


				contactInformation.setFirstName(addressModel.getFirstName());
				contactInformation.setLastName(addressModel.getLastName());
				contactInformation.setEntityName(buyingCompanyId);
				contactInformation.setPassword(addressModel.getUserPassword());
				contactInformation.setAddress1(addressModel.getAddress1());
				contactInformation.setAddress2(addressModel.getAddress2());
				contactInformation.setCity(addressModel.getCity());
				contactInformation.setState(addressModel.getState());
				contactInformation.setPobox("");
				if(addressModel.getCountry().equalsIgnoreCase("USA")){
					contactInformation.setCountry("US");
				}else{
					contactInformation.setCountry(addressModel.getCountry());
				}
				contactInformation.setCountryFullName("");
				contactInformation.setPhoneNo(addressModel.getPhoneNo());
				contactInformation.setFaxNo("");
				contactInformation.setEmailAddress(addressModel.getEmailAddress());
				contactInformation.setWebAddress("");
				contactInformation.setZipCode(addressModel.getZipCode());
				contactInformation.setHomeBranch("");
				contactInformation.setDisableSubmitPOCC(addressModel.getDisableSubmitPOCC());
				//shippingAddress = new UsersModel();
				shippingAddress = billingAddress;
				if(session!=null) {
				billingAddress.setSession(session);
				}
				if(addressModel.getEntityId()!=null && addressModel.getEntityId().trim().length()>0){
					custNumber = addressModel.getEntityId();
				}else{
					erpIds = createCustomerInERP(billingAddress,shippingAddress, CommonUtility.validateString(addressModel.getUserPassword()));
					custNumber = erpIds.getCustomerId();
					contactInformation.setUserERPId(CommonUtility.validateString(erpIds.getContactId()));
					contactInformation.setShipToId(erpIds.getShipToId());
				}

				if(custNumber==null || custNumber=="0"){
					result = "0|User Registration Failed. Please Contact Customer Service.";

				}else{
					result = newRetailUserRegistration(custNumber, billingAddress.getCountry().trim(), addressModel.getUserPassword(), locUser, userRole, buyingComanyIdStr, parentUserId,addressModel.getFirstName(),addressModel.getLastName(),addressModel.getUserStatus(), null, true, true, contactInformation);
					if(result!=null){
						String resArr[] = result.split("\\|");
						if(resArr.length>0){
							if(resArr[0].trim().equalsIgnoreCase("0") || result.toLowerCase().contains("successfully")){
								//SendMailUtility.buildNewUser(custNumber, addressModel.getEmailAddress(), addressModel.getUserPassword(),addressModel.getEmailAddress(),addressModel.getFirstName(),addressModel.getLastName());

								//SendMailUtility.buildNewUser(custNumber, addressModel.getEmailAddress(), addressModel.getUserPassword(),addressModel.getEmailAddress(),addressModel.getFirstName(),addressModel.getLastName());

								UsersModel userAddress = new UsersModel();
								userAddress.setFirstName(addressModel.getFirstName());
								userAddress.setLastName(addressModel.getLastName());
								if(addressModel.getCompanyName()!=null && !addressModel.getCompanyName().trim().equalsIgnoreCase("")){
									userAddress.setEntityName(addressModel.getCompanyName());
								}else{
									userAddress.setEntityName(addressModel.getFirstName() +" "+addressModel.getLastName());
								}
								userAddress.setAddress1(addressModel.getAddress1());
								userAddress.setAddress2(addressModel.getAddress2());
								userAddress.setCity(addressModel.getCity());
								userAddress.setState(addressModel.getState());
								userAddress.setZipCode(addressModel.getZipCode());
								if(addressModel.getCountry()==null || addressModel.getCountry().equalsIgnoreCase("USA")){
									userAddress.setCountry(addressModel.getCountry());
								}else{
									userAddress.setCountry("USA"); 
								}
								userAddress.setPhoneNo(addressModel.getPhoneNo());
								userAddress.setEmailAddress(addressModel.getEmailAddress());
								userAddress.setNewsLetterSub("N");
								userAddress.setPassword(addressModel.getUserPassword());

								/*SendMailUtility sendMailUtility = new SendMailUtility();
								//boolean sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"webUser","",addressModel.getFormtype()); //"2B" on 08 Dex 2014
								boolean sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"customer","",addressModel.getFormtype()); //"2B"
*/
							}
						}
					}
				}

			}
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;

	}

	public static String createRetailUser(AddressModel addressModel)
	{

		String result = "User Registration failed";
		try
		{
			boolean isValidUser = true;
			String locUser = "N";
			String userRole = null;
			String buyingComanyIdStr = null;
			String parentUserId = "0";
			String custNumber = null;
			UsersModel erpIds = null;
			Cimm2BCentralUser userInfo=new Cimm2BCentralUser();
			//Cimm2BCentralUser customerLocation=new Cimm2BCentralUser();
			Cimm2BCentralAddress billToAddress=new Cimm2BCentralAddress();
			//Cimm2BCentralAddress shipToAddress=new Cimm2BCentralAddress();

			if(addressModel.getLocUser()!=null && addressModel.getLocUser().trim().length()>0){
				locUser = addressModel.getLocUser();
			}

			if(addressModel.getRole()!=null && addressModel.getRole().trim().length()>0){
				userRole = addressModel.getRole();
			}

			if(addressModel.getBuyingComanyIdStr()!=null && addressModel.getBuyingComanyIdStr().trim().length()>0){
				buyingComanyIdStr = addressModel.getBuyingComanyIdStr();
			}

			if(addressModel.getBuyingComanyIdStr()!=null && addressModel.getBuyingComanyIdStr().trim().length()>0){
				buyingComanyIdStr = addressModel.getBuyingComanyIdStr();
			}

			if(addressModel.getParentUserId()!=null && addressModel.getParentUserId().trim().length()>0){
				parentUserId = addressModel.getParentUserId();
			}

			String buyingCompanyId = "";


			if(isValidUser){
				// new user registration process.
				UsersModel billingAddress = new UsersModel();
				UsersModel shippingAddress = null;
				UsersModel contactInformation = new UsersModel();



				if(addressModel.getCompanyName()!=null && addressModel.getCompanyName().trim().length()>0){
					billingAddress.setCustomerName(addressModel.getCompanyName());//billingAddress.setCustomerName(company);
					contactInformation.setCustomerName(addressModel.getCompanyName());
				}else{
					billingAddress.setCustomerName(addressModel.getFirstName()+" "+addressModel.getLastName());//billingAddress.setCustomerName(company);
					contactInformation.setCustomerName(addressModel.getFirstName()+" "+addressModel.getLastName());
				}

				billingAddress.setFirstName(addressModel.getFirstName());
				billingAddress.setLastName(addressModel.getLastName());
				billingAddress.setEntityName(buyingCompanyId); // 
				billingAddress.setAddress1(addressModel.getAddress1());
				billingAddress.setAddress2(addressModel.getAddress2());
				billingAddress.setCity(addressModel.getCity());
				billingAddress.setState(addressModel.getState());
				billingAddress.setPobox("");
				if(addressModel.getCountry()!=null && addressModel.getCountry().equalsIgnoreCase("USA")){
					billingAddress.setCountry("US");
				}else{
					billingAddress.setCountry(addressModel.getCountry()!=null?addressModel.getCountry():"US");
				}
				billingAddress.setCountryFullName("");
				billingAddress.setPhoneNo(addressModel.getPhoneNo());
				billingAddress.setFaxNo("");
				billingAddress.setEmailAddress(addressModel.getEmailAddress());
				billingAddress.setWebAddress("");
				billingAddress.setZipCode(addressModel.getZipCode());
				billingAddress.setHomeBranch("");
				billingAddress.setBirthMonth(CommonUtility.validateString(addressModel.getBirthMonth()));
				billingAddress.setIsRewardMember(CommonUtility.validateString(addressModel.getIsRewardMember()));
				
				contactInformation.setFirstName(addressModel.getFirstName());
				contactInformation.setLastName(addressModel.getLastName());
				contactInformation.setEntityName(buyingCompanyId);
				contactInformation.setPassword(addressModel.getUserPassword());
				contactInformation.setAddress1(addressModel.getAddress1());
				contactInformation.setAddress2(addressModel.getAddress2());
				contactInformation.setCity(addressModel.getCity());
				contactInformation.setState(addressModel.getState());
				contactInformation.setPobox("");
				if(addressModel.getCountry()!=null && addressModel.getCountry().equalsIgnoreCase("USA")){
					contactInformation.setCountry("US");
				}else{
					contactInformation.setCountry(addressModel.getCountry());
				}
				contactInformation.setCountryFullName("");
				contactInformation.setPhoneNo(addressModel.getPhoneNo());
				contactInformation.setFaxNo("");
				contactInformation.setEmailAddress(addressModel.getEmailAddress());
				contactInformation.setWebAddress("");
				contactInformation.setZipCode(addressModel.getZipCode());
				contactInformation.setHomeBranch("");
				contactInformation.setCustomerType(addressModel.getCustomerType());
				//shippingAddress = new UsersModel();
				shippingAddress = billingAddress;

				String userID = "";
					billToAddress.setAddressLine1(addressModel.getAddress1());
					billToAddress.setAddressLine2(addressModel.getAddress2());
					billToAddress.setCity(addressModel.getCity());
					billToAddress.setState(addressModel.getState());
					billToAddress.setCountry(addressModel.getCountry());
					billToAddress.setZipCode(addressModel.getZipCode());
					billToAddress.setCounty(null);
					
					ArrayList<Cimm2BCentralContact> billContacts=new ArrayList<Cimm2BCentralContact>();
					Cimm2BCentralContact billToContact=new Cimm2BCentralContact();
				
					
					billToContact.setFirstName(addressModel.getFirstName());
					billToContact.setLastName(addressModel.getLastName());
					billToContact.setPrimaryEmailAddress(addressModel.getEmailAddress());
					billToContact.setPrimaryPhoneNumber(addressModel.getPhoneNo());
					billContacts.add(billToContact);
			
					
					userInfo.setCustomerERPId(CommonUtility.validateString(addressModel.getEntityId()));
					userInfo.setAddress(billToAddress);
					userInfo.setContacts(billContacts);
					userInfo.setPassword(addressModel.getUserPassword());
				
				if(addressModel.getEntityId()!=null && addressModel.getEntityId().trim().length()>0){
					custNumber = addressModel.getEntityId();
				}else if(addressModel.isAnonymousUser()==true && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_RETAIL_ERP_REGISTRATION")).equalsIgnoreCase("Y")){
					custNumber = CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID");
				}else{
					erpIds = createCustomerInERP(billingAddress,shippingAddress, CommonUtility.validateString(addressModel.getUserPassword()));
					custNumber = CommonUtility.validateString(erpIds.getCustomerId());
					contactInformation.setUserERPId(CommonUtility.validateString(erpIds.getContactId()));
				}
				if(custNumber==null){
					result = "0|User Registration Failed. Please Contact Customer Service.";

				}else{
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API")).length()>0){
						String CREATE_USER_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API"));
						Cimm2BCentralResponseEntity userDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_USER_API, "POST", userInfo, String.class);
						if(userDetailsResponseEntity!=null && userDetailsResponseEntity.getData() != null &&  userDetailsResponseEntity.getStatus().getCode() == 200 ){  
							userID=	userDetailsResponseEntity.getData().toString();
							contactInformation.setUserERPId(CommonUtility.validateString(userID));
							logger.info("User created:"+userID);
						}
					}
					//condition to avoid user registration from e-commerce
					if(CommonUtility.validateString(addressModel.getErpOverrideFlag()).equalsIgnoreCase("cimmesb"))
					{
						result = "1|"+addressModel.getEmailAddress()+" has been successfully registered";
					}
					
					else {
					result = newRetailUserRegistration(custNumber, billingAddress.getCountry().trim(), addressModel.getUserPassword(), locUser, userRole, buyingComanyIdStr, parentUserId,addressModel.getFirstName(),addressModel.getLastName(),"Y", null, true, true, contactInformation);
						}					
					if(result!=null){
						String resArr[] = result.split("\\|");
						if(resArr.length>0){
							if(resArr[0].trim().equalsIgnoreCase("0") || result.toLowerCase().contains("successfully")){
								//SendMailUtility.buildNewUser(custNumber, addressModel.getEmailAddress(), addressModel.getUserPassword(),addressModel.getEmailAddress(),addressModel.getFirstName(),addressModel.getLastName());

								//SendMailUtility.buildNewUser(custNumber, addressModel.getEmailAddress(), addressModel.getUserPassword(),addressModel.getEmailAddress(),addressModel.getFirstName(),addressModel.getLastName());

								UsersModel userAddress = new UsersModel();
								userAddress.setFirstName(addressModel.getFirstName());
								userAddress.setLastName(addressModel.getLastName());
								if(addressModel.getCompanyName()!=null && !addressModel.getCompanyName().trim().equalsIgnoreCase("")){
									userAddress.setEntityName(addressModel.getCompanyName());
								}else{
									userAddress.setEntityName(addressModel.getFirstName() +" "+addressModel.getLastName());
								}
								userAddress.setAddress1(addressModel.getAddress1());
								userAddress.setAddress2(addressModel.getAddress2());
								userAddress.setCity(addressModel.getCity());
								userAddress.setState(addressModel.getState());
								userAddress.setZipCode(addressModel.getZipCode());
								if(addressModel.getCountry()!=null && addressModel.getCountry().equalsIgnoreCase("USA")){
									userAddress.setCountry(addressModel.getCountry());
								}else{
									userAddress.setCountry("USA"); 
								}
								userAddress.setPhoneNo(addressModel.getPhoneNo());
								userAddress.setEmailAddress(addressModel.getEmailAddress());
								userAddress.setNewsLetterSub(CommonUtility.validateString(addressModel.getNewsLetterSub()).length()>0?CommonUtility.validateString(addressModel.getNewsLetterSub()):"N");
								userAddress.setPassword(addressModel.getUserPassword());

								SendMailUtility sendMailUtility = new SendMailUtility();
								//boolean sentFlag = sendMailUtility.sendRegistrationMail(userAddress,"webUser","",addressModel.getFormtype()); //"2B" on 08 Dex 2014
								sendMailUtility.sendRegistrationMail(userAddress,"customer","",addressModel.getFormtype()); //"2B"

							}
						}
					}
				}

			}
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;

	}


	public static UsersModel createCustomerInERP(UsersModel billAddressList, UsersModel shipAddressList, String password) throws JsonProcessingException{
		HttpServletRequest request;
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		CimmCustomerRequest cimmCustomerRequest=new CimmCustomerRequest();
		UsersModel customerinfo = new UsersModel();
		cimmCustomerRequest.setCustomerName(billAddressList.getFirstName()+" "+billAddressList.getLastName());
		cimmCustomerRequest.setCustomerType(CommonUtility.validateString(request.getParameter("customerType")));
		CimmAddressRequest cimmAddressRequest=new CimmAddressRequest();
		cimmAddressRequest.setAddressLine1(billAddressList.getAddress1());
		cimmAddressRequest.setAddressLine2(billAddressList.getAddress2());
		cimmAddressRequest.setCity(billAddressList.getCity());
		cimmAddressRequest.setState(billAddressList.getState());
		cimmAddressRequest.setZipCode(billAddressList.getZipCode());
		cimmAddressRequest.setCountry(billAddressList.getCountry());
		cimmAddressRequest.setCountryCode(billAddressList.getCountry());
		cimmCustomerRequest.setAddress(cimmAddressRequest);
		CimmContactRequest cimmContactRequest=new CimmContactRequest();
		cimmContactRequest.setPrimaryEmailAddress(billAddressList.getEmailAddress());
		cimmContactRequest.setFirstName(billAddressList.getFirstName());
		cimmContactRequest.setLastName(billAddressList.getLastName());
		cimmContactRequest.setPrimaryPhoneNumber(billAddressList.getPhoneNo());
		cimmContactRequest.setBirthMonth(CommonUtility.validateString(billAddressList.getBirthMonth()));
		if(CommonUtility.validateString(billAddressList.getIsRewardMember()).length()>0)
		{
			cimmContactRequest.setMobileCanText(true);
		}
		else
		{
			cimmContactRequest.setMobileCanText(false);
		}
		List<CimmContactRequest> contacts=new ArrayList<>();
		contacts.add(cimmContactRequest);
		if(!CommonUtility.validateString(request.getParameter("customerLocationFlag")).equalsIgnoreCase("N")){
			CimmCustomerRequest defaultCustomerRequest= new CimmCustomerRequest();
			CimmAddressRequest shippingAddress = new CimmAddressRequest();
			shippingAddress.setAddressLine1(billAddressList.getAddress1());
			shippingAddress.setAddressLine2(billAddressList.getAddress2());
			shippingAddress.setCity(billAddressList.getCity());
			shippingAddress.setState(billAddressList.getState());
			shippingAddress.setZipCode(billAddressList.getZipCode());
			shippingAddress.setCountry(billAddressList.getCountry());
			shippingAddress.setCountryCode(billAddressList.getCountry());
			shippingAddress.setAddressERPId(CommonUtility.validateParseIntegerToString(CommonDBQuery.getSequenceId("BC_ADDRESS_BOOK_ID_SEQ")));
			defaultCustomerRequest.setAddress(shippingAddress);
			List<CimmCustomerRequest> customerLocation=new ArrayList<>();
			customerLocation.add(defaultCustomerRequest);
			cimmCustomerRequest.setCustomerLocations(customerLocation);
		}
		cimmCustomerRequest.setContacts(contacts);
		cimmCustomerRequest.setUsername(billAddressList.getEmailAddress());
		cimmCustomerRequest.setPassword(password);
		cimmCustomerRequest.setWarehouseLocation(CommonUtility.validateString(request.getParameter("warehouseLocationCode")));
		SecureData userPassword=new SecureData();
		RestRequest<CimmCustomerRequest> retailUserRequest = RestRequest.<CimmCustomerRequest>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY)))
				.withUserSecret(userPassword.validatePassword(session.getAttribute("securedPassword").toString())).withBody(cimmCustomerRequest)
				.build();
			Cimm2BCResponse<Cimm2BCustomer> response = CimmESBServiceUtils.getCustomerService().createCustomer(retailUserRequest);
			DozerBeanMapper  dozzer = new DozerBeanMapper();
			ObjectMapper mapper = new ObjectMapper();
			Cimm2BCentralCustomer customerDetails = new Cimm2BCentralCustomer();
			System.out.println(mapper.writeValueAsString(response));
			dozzer.map(response.getData(),customerDetails);
			if(customerDetails!=null && response.getStatus().getCode()==200){  
				customerinfo.setCustomerName(customerDetails.getCustomerName());
				customerinfo.setCustomerId(customerDetails.getCustomerERPId());
			}
			
			Cimm2BCentralAddress address = customerDetails.getAddress();

			if(address != null){
				if(CommonUtility.validateString(address.getAddressLine1()).length() > 0){
					customerinfo.setAddress1(address.getAddressLine1());
				}else{
					customerinfo.setAddress1("No Address");
				}

				if(CommonUtility.validateString(address.getAddressLine2()).length() > 0){
					customerinfo.setAddress2(address.getAddressLine2());
				}
				customerinfo.setCity(address.getCity());
				customerinfo.setState(address.getState());
				customerinfo.setZipCode(address.getZipCode());

				if(CommonUtility.validateString(address.getCountry()).length() > 0){
					customerinfo.setCountry(address.getCountry());
				}else{
					customerinfo.setCountry(billAddressList.getCountry());
				}
				
			}
			if(customerDetails.getContacts() != null && customerDetails.getContacts().size() > 0){
				if(CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
					customerinfo.setPhoneNo(customerDetails.getContacts().get(0).getPrimaryPhoneNumber());
				}else if(CommonUtility.validateString(customerDetails.getContacts().get(0).getAlternatePhoneNumber()).length() > 0){
					customerinfo.setPhoneNo(customerDetails.getContacts().get(0).getAlternatePhoneNumber());
				}

				if(CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryEmailAddress()).length() > 0){
					customerinfo.setEmailAddress(customerDetails.getContacts().get(0).getPrimaryEmailAddress());
				}else{
					customerinfo.setEmailAddress(billAddressList.getEmailAddress());
				}
			}
			
		return customerinfo;
	}
	
	public static UsersModel contactInquiry(String contactId)
	{
		UsersModel userDetailList = new UsersModel();
		try{
			String CONTACT_INQUIRY_API=CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONTACT_INQUIRY_API"))+"?"+Cimm2BCentralRequestParams.userERPId+"="+contactId;
			Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CONTACT_INQUIRY_API, HttpMethod.GET, null, Cimm2BCentralCustomer.class);
			
			Cimm2BCentralCustomer contactResponse = null;
			if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  
				contactResponse =(Cimm2BCentralCustomer)customerDetailsResponseEntity.getData();
				if(contactResponse!=null)
				{
					userDetailList.setEntityId(contactResponse.getCustomerERPId());
					userDetailList.setContactId(contactResponse.getUserERPId());
					userDetailList.setPassword(contactResponse.getPassword());
					userDetailList.setLoginID(contactResponse.getLoginId());
					userDetailList.setSuperuser(contactResponse.getIsSuperuser());
					if(contactResponse.getContacts() != null  && contactResponse.getContacts().size() > 0){
						userDetailList.setFirstName(contactResponse.getContacts().get(0).getFirstName());
						userDetailList.setLastName(contactResponse.getContacts().get(0).getLastName());
						if(CommonUtility.validateString(contactResponse.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
							userDetailList.setPhoneNo(contactResponse.getContacts().get(0).getPrimaryPhoneNumber());	
						}else if(CommonUtility.validateString(contactResponse.getContacts().get(0).getAlternatePhoneNumber()).length() > 0){
							userDetailList.setPhoneNo(contactResponse.getContacts().get(0).getAlternatePhoneNumber());
						}
						if(CommonUtility.validateString(contactResponse.getContacts().get(0).getPrimaryEmailAddress()).length() > 0){
							userDetailList.setEmailAddress(contactResponse.getContacts().get(0).getPrimaryEmailAddress());
						}
						
				   }
					Cimm2BCentralAddress address = contactResponse.getAddress();
					if(address!=null)
					{
						if(address.getAddressLine1()!=null){
						userDetailList.setAddress1(address.getAddressLine1());}
						if(address.getAddressLine2()!=null){
							userDetailList.setAddress2(address.getAddressLine2());}
						if(address.getCity()!=null){
							userDetailList.setCity(address.getCity());}
						if(address.getState() !=null){
							userDetailList.setState(address.getState());}
						if(address.getZipCode() !=null){
							userDetailList.setZipCode(address.getZipCode());}
						if(address.getCountry() !=null){
							userDetailList.setCountry(address.getCountry());
						}
						
					}
				}
			}
			else
			{
				logger.info("Contact Inquiry Failed");
			}
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return userDetailList;
		
		
	}

	public static String newRetailUserRegistration(String customerNumber, String country,String customerPass, String locUser, String userRole, String buyingCompanyIdStr, String parentUserId, String firstName, String lstName, String status,UsersModel contactInformation, boolean isFrmRegPage, boolean updateRole, UsersModel customerInfo){
		String resultVal = "";
		Connection conn=null;
		try {
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);
		} catch (SQLException e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		try{
			int defaultBillId = 0;
			boolean isAccountExist = false;

			UsersModel customerinfo = customerInfo;
			UsersModel contactINFO = customerInfo;

			String taxable = null;
			String entityid = "0";
			if(customerNumber!=null && customerNumber!="0" && customerNumber!=""){
				entityid= customerNumber;
			}
			if(taxable==null)
				taxable = "N";

			if(taxable.trim().equalsIgnoreCase("N")){
				customerinfo.setTaxCode(2);
				contactINFO.setTaxCode(2);
			}else{
				customerinfo.setTaxCode(1);
				contactINFO.setTaxCode(1);
			}

			customerinfo.setEntityId(entityid);
			customerInfo.setWareHouseCode(CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE")));
			customerInfo.setWareHouseCodeStr(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR")));

			int userId =0;
			//Buying Company
			int buyingCompanyid = 0;
			if(!isAccountExist && (buyingCompanyIdStr == null || buyingCompanyIdStr.trim().equalsIgnoreCase(""))){
				buyingCompanyid = UsersDAO.insertBuyingCompany(conn,customerinfo,0);
			}else{
				buyingCompanyid = CommonUtility.validateNumber(buyingCompanyIdStr);
			}

			customerinfo.setUserStatus(status);
			contactINFO.setUserStatus(status);
			if(buyingCompanyid > 0){						           
				customerinfo.setBuyingCompanyId(buyingCompanyid);
				customerinfo.setContactId(CommonUtility.validateString(customerNumber));
				contactINFO.setBuyingCompanyId(buyingCompanyid);
				if( CommonUtility.validateString(customerinfo.getCustomerType()).length()>0 && CommonUtility.validateString(customerinfo.getCustomerType()).equalsIgnoreCase("G")) {
					userId = UsersDAO.insertGuestUser(conn, contactINFO, customerNumber, customerPass, buyingCompanyid, parentUserId,true);
				}else {
					userId = UsersDAO.insertNewUser(conn, contactINFO, customerNumber, customerPass, buyingCompanyid, parentUserId,true);
				}
			}else{
				buyingCompanyid = 2;
				customerinfo.setBuyingCompanyId(buyingCompanyid);
				customerinfo.setContactId(CommonUtility.validateString(customerNumber));
				contactINFO.setBuyingCompanyId(buyingCompanyid);
				userId = UsersDAO.insertNewUser(conn, contactINFO, customerNumber, customerPass, buyingCompanyid, parentUserId,true);
			}


			if(userId>0){    
				if(updateRole){
					if(userRole==null || userRole.trim().equalsIgnoreCase("")){
						userRole =  CommonDBQuery.getSystemParamtersList().get("DEFAULT_USER_ROLE");
						UsersDAO.assignRoleToUser(conn,Integer.toString(userId),userRole);
					}else if(updateRole){
						UsersDAO.assignRoleToUser(conn,Integer.toString(userId),userRole); 
					}
				}

				int shipId= 0;
				logger.info("customer inserted into database successfully");
				if(!isAccountExist && (buyingCompanyIdStr == null || buyingCompanyIdStr.trim().equalsIgnoreCase(""))){
					defaultBillId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Bill",true);
					shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Ship",true);
				}else{
					UsersModel uModel =  UsersDAO.getDefaultShipNbill(customerNumber);
					if(uModel!=null){
						defaultBillId = CommonUtility.validateNumber(uModel.getBillToId());
						shipId = CommonUtility.validateNumber(uModel.getShipToId());
					}
					if(defaultBillId == 0 || shipId == 0){ 						
					defaultBillId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Bill",true); 						
					shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Ship",true); 					}
				}

				//resultVal = "0|"+customerInfo.getEmailAddress()+" has been successfully registered"; 
				resultVal = "1|"+customerInfo.getEmailAddress()+" has been successfully registered";       
				int count = UsersDAO.updateUserAddressBook(conn, defaultBillId, shipId, userId,true); 

				if(userId>0 && locUser!=null && locUser.trim().length()>0){
					CustomFieldModel customFieldVal = UsersDAO.getCustomIDs(locUser, "IS_INTERNATIONAL_USER");
					if(customFieldVal!=null){
						UsersDAO.isInternationalUser(conn, userId, customFieldVal.getCustomFieldID(), customFieldVal.getLocCustomFieldValueID());
					}
				}


				if(CommonDBQuery.getSystemParamtersList().get("CALL_FOR_KEYWORD_BUILDER")!=null && CommonDBQuery.getSystemParamtersList().get("CALL_FOR_KEYWORD_BUILDER").trim().equalsIgnoreCase("Y")){
					if(CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD")!=null && CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD").trim().equalsIgnoreCase("Y")){
						UsersDAO.buildKeyWord("BUYINGCOMPANY", buyingCompanyid);
					}else{
						UsersDAO.buildUserKeyWord("BUYINGCOMPANY", buyingCompanyid);
					}

					if(CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD")!=null && CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD").trim().equalsIgnoreCase("Y")){
						UsersDAO.buildKeyWord("USER", userId);
					}else{
						UsersDAO.buildUserKeyWord("USER", userId);
					}
				}

				conn.commit(); 
			}else{
				resultVal = "0|User Registration Failed.";
			}

		}catch (Exception e) {
			resultVal = "0|User Registration Failed.";
			logger.error(e.getMessage());
			e.printStackTrace();

		}
		finally{
			ConnectionManager.closeDBConnection(conn);
		}
		return resultVal;
	}

	public HashMap<String, ArrayList<UsersModel>> getAddressListFromBCAddressBook(int buyingCompanyId, int userId){
		HashMap<String, ArrayList<UsersModel>> result = null;
		try{
			result =  UsersDAO.getAddressListFromBCAddressBookDefault(buyingCompanyId, userId);
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public HashMap<String, UsersModel> getUserAddressFromBCAddressBook(int billId,int shipId){
		HashMap<String, UsersModel> result = null;
		try{
			result = UsersDAO.getUserAddressFromBCAddressBook(billId, shipId);
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public String editBillingAddress(UsersModel userInfo){
		int count =-1;
		String result = "Billing Address Update Failed";
		Connection conn = null;
		try{
			HttpSession session = userInfo.getSession();
			HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId((String) session.getAttribute(Global.USERNAME_KEY),"Y");
			String email= userDetails.get("userEmailAddress");
			SecureData userPassword=new SecureData();
						
			CimmCustomerRequest cimmCustomerRequest=new CimmCustomerRequest();
			cimmCustomerRequest.setCustomerName(userInfo.getCustomerName());
			
			CimmAddressRequest cimmAddressRequest=new CimmAddressRequest();		
			cimmAddressRequest.setAddressLine1(userInfo.getAddress1());
			cimmAddressRequest.setAddressLine2(userInfo.getAddress2());
			cimmAddressRequest.setCity(userInfo.getCity());
			cimmAddressRequest.setState(userInfo.getState());
			cimmAddressRequest.setZipCode(userInfo.getZipCode());
			cimmAddressRequest.setCountry(userInfo.getCountry());
			cimmCustomerRequest.setAddress(cimmAddressRequest);
			
			CimmContactRequest cimmContactRequest=new CimmContactRequest();
			cimmContactRequest.setPrimaryEmailAddress(email);
			
			cimmCustomerRequest.setContact(cimmContactRequest);
			
			RestRequest<CimmCustomerRequest> request = RestRequest.<CimmCustomerRequest>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY))).withBody(cimmCustomerRequest)
					.withUserSecret(userPassword.validatePassword(session.getAttribute("securedPassword").toString())).build();
			ObjectMapper mapper1 = new ObjectMapper();
			System.out.println(mapper1.writeValueAsString(cimmCustomerRequest));
			Cimm2BCResponse<Cimm2BCustomer> response = CimmESBServiceUtils.getCustomerService().createCustomer(request);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.writeValueAsString(response));
			
			if(response!=null && response.getData() != null &&  response.getStatus().getCode() == 200){  
					count = UsersDAO.updateBillAddressBCAddressBook(userInfo);
			}
			if(count>0){
				result = "1 | Billing Address Updated Successfully";
			}else{
				result = "0 | Billing Address Update Failed";
			}
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public String editShippingAddress(UsersModel shipInfo){
		int count =-1;
		String result = "Shipping Address Update Failed";
		try{
			count = UsersDAO.updateBCAddressBook(shipInfo);
			if(shipInfo.getMakeAsDefault()!=null && shipInfo.getMakeAsDefault().trim().equalsIgnoreCase("Yes")){
				count = UsersDAO.updateDefaultShipTo(shipInfo.getUserId(), shipInfo.getAddressBookId());
			}
			if(count>0){
				result = "1|"+"Shipping Address Updated Successfully";
			}else{
				result = "0|"+"Shipping Address Update Failed";
			}
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public String addNewShippingAddress(UsersModel shipInfo){
		int count =-1;
		String shipToID = "0";
		String result = "Failed to add New Shipping Address";
		Connection conn = null;
		int userId = 0;
		try{
			HttpSession session = shipInfo.getSession();
			List<CimmCustomerRequest> shipToInfo=new ArrayList<>();
			CimmCustomerRequest cimmCustomerRequest=new CimmCustomerRequest();
			String custNumber = (String) session.getAttribute("customerId");
			//Cimm2BCentralCustomer shipToInfo=new Cimm2BCentralCustomer();
			cimmCustomerRequest.setCustomerName(shipInfo.getCustomerName());
			CimmAddressRequest cimmAddressRequest=new CimmAddressRequest();
			cimmAddressRequest.setAddressLine1(shipInfo.getAddress1());
			cimmAddressRequest.setAddressLine2(shipInfo.getAddress2());
			cimmAddressRequest.setCity(shipInfo.getCity());
			cimmAddressRequest.setState(shipInfo.getState());
			cimmAddressRequest.setCountry(shipInfo.getCountry());
			cimmAddressRequest.setZipCode(shipInfo.getZipCode());
			cimmCustomerRequest.setAddress(cimmAddressRequest);
			
			CimmContactRequest cimmContactRequest=new CimmContactRequest();
			cimmContactRequest.setPrimaryEmailAddress(shipInfo.getEmailAddress());
			cimmContactRequest.setPrimaryPhoneNumber(shipInfo.getPhoneNo());
			SecureData userPassword=new SecureData();
			List<CimmContactRequest> contacts=new ArrayList<>();
			contacts.add(cimmContactRequest);
			cimmCustomerRequest.setContacts(contacts);
			shipToInfo.add(cimmCustomerRequest);
			CimmCustomerRequest cimmCustomerRequest1=new CimmCustomerRequest();
			cimmCustomerRequest1.setCustomerLocations(shipToInfo);
			RestRequest<CimmCustomerRequest> request = RestRequest.<CimmCustomerRequest>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY))).withBody(cimmCustomerRequest1)
					.withUserSecret(userPassword.validatePassword(session.getAttribute("securedPassword").toString())).build();
			ObjectMapper mapper1 = new ObjectMapper();
			System.out.println(mapper1.writeValueAsString(cimmCustomerRequest1));
			String customerErpId=shipInfo.getEntityId();
			Cimm2BCResponse<Cimm2BCustomer> response = CimmESBServiceUtils.getCustomerService().createCustomerShipTo(request, customerErpId);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.writeValueAsString(response));
			int defaultShipId = 0;
			if(response!=null && response.getData() != null &&  response.getStatus().getCode() == 200 ){  
				shipToID = response.getData().toString();
				List<Cimm2BShippingMethod> cimm2BCentralShipMethod = response.getData().getShippingMethods();
				for(Cimm2BShippingMethod shipMethod:cimm2BCentralShipMethod) {
					shipInfo.setShipToId(shipMethod.getShipViaErpId());
				}
				userId = shipInfo.getUserId();
				conn = ConnectionManager.getDBConnection();
				defaultShipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, shipInfo,"Ship",false);
			}
			if(defaultShipId>0){
				count=1;
				if(shipInfo.getMakeAsDefault()!=null && shipInfo.getMakeAsDefault().trim().equalsIgnoreCase("Yes")){
					count =-1;
					count = UsersDAO.updateDefaultShipTo(userId, defaultShipId);
				}
			}
			if(count>0){
				result = "1|"+"Shipping Address Inserted Successfully";
			}else{
				result = "0|"+"Failed to add New Shipping Address";
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBConnection(conn);
		}
		return result;
	}


	public static UsersModel getRoleFromErp(UsersModel userInfo) {
		long startTimer = CommonUtility.startTimeDispaly();
		UsersModel defaultContact = new UsersModel();
		try
		{
			String userRole = UsersDAO.getUserRoleByUserName(userInfo.getUserName());

			defaultContact.setUserRole(userRole);

			if(userRole!=null && userRole.trim().equalsIgnoreCase("Ecomm Customer Super User")){
				defaultContact.setIsSuperUser("Yes");
			}else{
				defaultContact.setIsSuperUser("No");
			}

		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return defaultContact;
	}

	public static String updatePrevilege(UsersModel userInfo)
	{
		return "";
	}

	public static UsersModel accountDetail(LinkedHashMap<String, String> accountInputValue){
		HttpServletRequest httpRequest=ServletActionContext.getRequest();;
		UsersModel userDetailList = new UsersModel();
		Cimm2BCentralARBalanceData arBalanceDataRequest=null;
		String url = "";
		Cimm2BCentralResponseEntity arBalanceSummaryEntity = null;
		SecureData password=new SecureData();
		List<Cimm2BCentralARBalanceSummary> arBalanceSummaryList = new ArrayList<Cimm2BCentralARBalanceSummary>();
		Cimm2BCentralARBalanceSummaryRequest arBalanceSummaryRequest = new Cimm2BCentralARBalanceSummaryRequest();
		arBalanceSummaryRequest.setCustomerERPId(accountInputValue.get("entityId"));
		List<Cimm2BCentralARBalanceDetails> arBalanceSummary = null;
		try{
			HttpSession session = httpRequest.getSession();
		RestRequest<Void> request = RestRequest.<Void>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
				.build();
		Cimm2BCResponse<CimmARBalanceSummaryResponse> response = CimmESBServiceUtils.getCustomerService().getCustomerARBalance(request);
		DozerBeanMapper  dozzer = new DozerBeanMapper();
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(response));
		CimmARBalanceSummaryResponse arBalanceDataResponse =response.getData();
					if(arBalanceDataResponse!=null){
					userDetailList.setBalance(arBalanceDataResponse.getBalance());
					userDetailList.setPeriod1(arBalanceDataResponse.getPeriod1());
					userDetailList.setPeriod2(arBalanceDataResponse.getPeriod2());
					userDetailList.setPeriod3(arBalanceDataResponse.getPeriod3());
					userDetailList.setPeriod4(arBalanceDataResponse.getPeriod4());
					userDetailList.setOpenOrderBalance(arBalanceDataResponse.getOpenOrderBalance());
					userDetailList.setFutureBalance(arBalanceDataResponse.getFutureBalance());
					userDetailList.setSalesMTD(arBalanceDataResponse.getSalesMTD());
					userDetailList.setSalesYTD(arBalanceDataResponse.getSalesYTD());
					if(arBalanceDataResponse.getLastSaleDate()!=null){
						userDetailList.setLastSaleDate(CommonUtility.convertToStringFromDate(arBalanceDataResponse.getLastSaleDate()));
					}
					userDetailList.setTermsAndCondition(arBalanceDataResponse.getTermsAndCondition());
					if(arBalanceDataResponse.getTotal()!=null){
					userDetailList.setTotal(arBalanceDataResponse.getTotal());}
					if(arBalanceDataResponse.getSixMonthHigh()!=null){
					userDetailList.setSixMonthHigh(arBalanceDataResponse.getSixMonthHigh());}
					if(arBalanceDataResponse.getSixMonthAverage()!=null){
					userDetailList.setSixMonthAverage(arBalanceDataResponse.getSixMonthAverage());}
					if(arBalanceDataResponse.getArCreditLimit()!=null){
					userDetailList.setArCreditLimit(arBalanceDataResponse.getArCreditLimit());}
					if(arBalanceDataResponse.getArCreditAvail()!=null){
					userDetailList.setArCreditAvail(arBalanceDataResponse.getArCreditAvail());}
					if(arBalanceDataResponse.getPaymentDays()!=null){
					userDetailList.setPaymentDays(arBalanceDataResponse.getPaymentDays());}
					if(arBalanceDataResponse.getLastSaleAmount()!=null){
					userDetailList.setLastSaleAmount(arBalanceDataResponse.getLastSaleAmount());}
					if(arBalanceDataResponse.getLastPaymentAmount()!=null){
					userDetailList.setLastPaymentAmount(arBalanceDataResponse.getLastPaymentAmount());}
					if(arBalanceDataResponse.getInvoiceNumber()!=null)
					{
						userDetailList.setInvoiceNumber(arBalanceDataResponse.getInvoiceNumber());
					}
					if(arBalanceDataResponse.getAvgPaymentDays() != null) {
						userDetailList.setAvgPaymentDays(arBalanceDataResponse.getAvgPaymentDays());
					}
					if(arBalanceDataResponse.getPastDue() != null) {
						userDetailList.setPastDues(arBalanceDataResponse.getPastDue());
					}
					if(arBalanceDataResponse.getDueDate()!=null){
						userDetailList.setExpDate(CommonUtility.convertToStringFromDate(arBalanceDataResponse.getDueDate()));}
					if(arBalanceDataResponse.getInvoiceDate()!=null){
						userDetailList.setDate(CommonUtility.convertToStringFromDate(arBalanceDataResponse.getDueDate()));}
					if(arBalanceDataResponse.getEndDate()!=null){
					userDetailList.setEndDate(CommonUtility.convertToStringFromDate((arBalanceDataResponse.getEndDate())));}
					if(arBalanceDataResponse.getCimm2BARBalanceDetails()!=null && arBalanceDataResponse.getCimm2BARBalanceDetails().size()>0){
						for(CimmARBalanceDetails cimm2bcARBalanceSummary :arBalanceDataResponse.getCimm2BARBalanceDetails() ){
							Cimm2BCentralARBalanceSummary arBalanceSummaryDetails = new Cimm2BCentralARBalanceSummary();
							arBalanceSummaryDetails.setLastSaleDate(CommonUtility.convertToStringFromDate(cimm2bcARBalanceSummary.getDocumentDate()));
							if(cimm2bcARBalanceSummary.getInvoiceNumber()!=null){
								arBalanceSummaryDetails.setInvoiceNumber(cimm2bcARBalanceSummary.getInvoiceNumber());}
							
							arBalanceSummaryDetails.setCustomerPONumber(cimm2bcARBalanceSummary.getCustomerPONumber());
							arBalanceSummaryDetails.setLastSaleAmount(cimm2bcARBalanceSummary.getSalesTotal());
							arBalanceSummaryDetails.setLastPaymentAmount(cimm2bcARBalanceSummary.getDueBalance());
							arBalanceSummaryDetails.setBalance(cimm2bcARBalanceSummary.getBalance());
							arBalanceSummaryDetails.setAge(Long.toString((cimm2bcARBalanceSummary.getDocumentAge())));
							arBalanceSummaryDetails.setOrderNumber(cimm2bcARBalanceSummary.getOrderNumber());
							arBalanceSummaryList.add(arBalanceSummaryDetails);
					}
					userDetailList.setArBalanceSummaryList(arBalanceSummaryList);
				}
			}
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}	
		return userDetailList;
	}

	public static LinkedHashMap<String, String> getErpData(UsersModel userInfo,
			LinkedHashMap<String, String> erpData)
			{
				return erpData;
			}

	public static ArrayList<ShipVia> getShipViaList(UsersModel customerInfoInput) {

		ArrayList<ShipVia> shipVia = new ArrayList<ShipVia>();
		try
		{
			//shipVia = CustomersUtility.shipViaWrapper(reponse);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return shipVia;
	}

	public static void assignErpValues(UsersModel userInfo)
	{
		try
		{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String userToken = userInfo.getEntityId();
			session.setAttribute("userToken", userToken);

		}
		catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public static String contactUpdate(UsersModel userInfo,HttpSession session) {
		String response = "";
		SecureData password=new SecureData();
		ChangePasswordRequest requestBody = ChangePasswordRequest.builder()
				.newPassword(userInfo.getPassword())
				.oldPassword(password.validatePassword(userInfo.getCurrentPassword())).build();
		RestRequest<ChangePasswordRequest> request = RestRequest.<ChangePasswordRequest>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(userInfo.getUserName())
				.withUserSecret(password.validatePassword(userInfo.getCurrentPassword())).withBody(requestBody)
				.build();
		try {
			response = CimmESBServiceUtils.getChangePasswordUserService().changePassword(request);
			if(response != null && response.toUpperCase().contains("SUCCESS")) {
				session.setAttribute("securedPassword", SecureData.getsecurePassword(userInfo.getPassword()));
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return userInfo.getPassword();
	}

		


	public static ArrayList<ShipVia> getUserShipViaList(UsersModel customerInfoInput) {
		long startTimer = CommonUtility.startTimeDispaly();
		ArrayList<ShipVia> shipVia = new ArrayList<ShipVia>();
		try
		{
			if(CommonDBQuery.getSystemParamtersList().get("LOAD_SHIP_FROM_DB")!=null && CommonDBQuery.getSystemParamtersList().get("LOAD_SHIP_FROM_DB").trim().equalsIgnoreCase("Y")){
				shipVia = UsersDAO.getUserShipViaList();//Passing null to get ship via from SHIP_VIA table
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return shipVia;
	}

	/*public static UsersModel contactEdit(UsersModel customerInfoInput,UsersModel addressDetail) {

		UsersModel customerInfoOutput =new UsersModel();;
		HttpSession session =  customerInfoInput.getSession();
		
		ECommCimmUserRequest requestBody = new ECommCimmUserRequest();
		requestBody.setFirstName(addressDetail.getFirstName());
		requestBody.setLastName(addressDetail.getLastName());
		requestBody.setAddress1(addressDetail.getAddress1());
		requestBody.setAddress2(addressDetail.getAddress2());
		requestBody.setCity(addressDetail.getCity());
		requestBody.setState(addressDetail.getState());
		requestBody.setCountry(addressDetail.getCountry());
		
		System.out.println(new Gson().toJson(requestBody));
		SecureData password=new SecureData();
		RestRequest<ECommCimmUserRequest> request = RestRequest.<ECommCimmUserRequest>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString()).withBody(requestBody)
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString()))
				.build();
		try {
			Cimm2BCResponse<ECommUserResponse> response = CimmESBServiceUtils.getUpdateUserService().updateUserInformatiom(request, String.valueOf(addressDetail.getBuyingCompanyId()), String.valueOf(addressDetail.getUserId()));
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.writeValueAsString(response));
			if(response != null && response.getStatus().getCode() == HttpStatus.SC_OK) {
				customerInfoOutput.setResult(response.getStatus().getMessage());
			}
		} catch (RestClientException | JsonProcessingException e) {
			e.printStackTrace();
		}
			
		return customerInfoOutput;
	}*/
	
	public static UsersModel contactEdit(UsersModel customerInfoInput,UsersModel addressDetail) {
		
		UsersModel customerInfoOutput =new UsersModel();;
		HttpSession session =  customerInfoInput.getSession();
		String pass = null;
		String result = "";
		
			try 
	 		{
				Cimm2BCentralCustomer customerInfo=new Cimm2BCentralCustomer();
				HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId(customerInfoInput.getUserName(),"Y");
				addressDetail.setUserName(customerInfoInput.getUserName());
				addressDetail.setUserStatus("A");
				addressDetail.setBuyingCompanyId(CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString()));
				String customerERPId = userDetails.get("contactId");
				String userERPId = userDetails.get("userErpId"); 
				String password = userDetails.get("password");
				SecureData validUserPass = new SecureData();
				pass = validUserPass.validatePassword(password);
				
				
				Cimm2BCentralAddress address=new Cimm2BCentralAddress();
				address.setAddressLine1(addressDetail.getAddress1());
				address.setAddressLine2(addressDetail.getAddress2());
				address.setCity(addressDetail.getCity());
				address.setState(addressDetail.getState());
				address.setZipCode(addressDetail.getZipCode());
				
				
				ArrayList<Cimm2BCentralContact> contacts=new ArrayList<Cimm2BCentralContact>();
				Cimm2BCentralContact userContact=new Cimm2BCentralContact();
				
				userContact.setFirstName(addressDetail.getFirstName());
				userContact.setLastName(addressDetail.getLastName());
				userContact.setPrimaryEmailAddress(addressDetail.getEmailAddress());
				userContact.setPrimaryPhoneNumber(addressDetail.getPhoneNo());
				contacts.add(userContact);
				customerInfo.setUserERPId(userERPId);
				customerInfo.setCustomerERPId(customerERPId);
				customerInfo.setAddress(address);
				customerInfo.setContacts(contacts);
				customerInfo.setPassword(pass);
				customerInfo.setDepartment(customerInfoInput.getJobTitle());
				
				
				ECommCimmUserRequest requestBody = new ECommCimmUserRequest();
				requestBody.setUsername(addressDetail.getUserName());
				requestBody.setFirstName(addressDetail.getFirstName());
				requestBody.setLastName(addressDetail.getLastName());
				requestBody.setAddress1(addressDetail.getAddress1());
				requestBody.setAddress2(addressDetail.getAddress2());
				requestBody.setCity(addressDetail.getCity());
				requestBody.setState(addressDetail.getState());
				requestBody.setCountry(addressDetail.getCountry());
				requestBody.setEmailAddress(addressDetail.getEmailAddress());
				requestBody.setCellPhone(addressDetail.getPhoneNo());
				requestBody.setZipCode(addressDetail.getZipCode());
				requestBody.setJobTitle(addressDetail.getJobTitle());
				requestBody.setOfficePhone(addressDetail.getOfficePhone());
				requestBody.setCountryCode(addressDetail.getCountryCode());
				requestBody.setCustomerType(addressDetail.getCustomerType());
				
				SecureData userPassword=new SecureData();
				RestRequest<ECommCimmUserRequest> request = RestRequest.<ECommCimmUserRequest>builder()
						.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
						.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
						.withUserName(session.getAttribute(Global.USERNAME_KEY).toString()).withBody(requestBody)
						.withUserSecret(userPassword.validatePassword(session.getAttribute("securedPassword").toString()))
						.build();
				ObjectMapper mapper = new ObjectMapper();
				System.out.println("CIMM ESB contact Request:"+mapper.writeValueAsString(requestBody));
				
				ECommUserResponse userDetailsResponseEntity = new ECommUserResponse();
				Cimm2BCResponse<ECommUserResponse> response = CimmESBServiceUtils.getUpdateUserService().updateUserInformatiom(request, String.valueOf(addressDetail.getBuyingCompanyId()), String.valueOf(addressDetail.getUserId()));
				DozerBeanMapper  dozzer = new DozerBeanMapper();
				dozzer.map(response.getData(),userDetailsResponseEntity); // Dozzer Mapper
				
				if(userDetailsResponseEntity!=null  &&  response.getStatus().getCode() == 200 ){
					result = response.getStatus().getMessage();
					customerInfoOutput = addressDetail;
					customerInfoOutput.setStatusDescription(result);
					logger.info(result);
				}
				else{
					logger.info("0|Update Failed. Please Try Again.");
				}
				if(customerInfoOutput!=null && customerInfoOutput.getStatusDescription()!=null && customerInfoOutput.getStatusDescription().trim().contains("successful"))
				{
					customerInfoOutput.setResult("1|"+addressDetail.getStatusDescription());
				}
				else
					{
					 customerInfoOutput.setResult("0|Update Failed. Please Try Again.");
					}	
	 			} catch (ResourceNotFoundException ex) {
	 				ex.printStackTrace();
	 			} catch (ParseErrorException ex) {
	 				ex.printStackTrace();
	 			} catch (Exception ex) {
	 				ex.printStackTrace();
	 			} finally
	         		{
	 					logger.info("Clossing url connection");
	         		}
		return customerInfoOutput;
	}
	public static void checkERPConnection(){
		try{
			//UsersDAO erpConn = new UsersDAO();
			//erpConn.checkEclipseSession();
		}catch (Exception ex){
			ex.printStackTrace();
		} 
	}

	public static HashMap<String, ArrayList<UsersModel>> getAgentAddressListFromBCAddressBook(UsersModel customerInfoInput) {
		HashMap<String, ArrayList<UsersModel>> shipList = null;
		try{
			shipList = UsersDAO.getAgentAddressListFromBCAddressBook(customerInfoInput.getUserId(),"");
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return shipList;
	}

	public static String newOnAccountUserRegistration(UsersModel userDetailsInput) throws RestClientException{

		String result = "";
		Connection conn=null;
		String userNumber=null;
		try {
			HttpSession session =userDetailsInput.getSession();
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);
			UsersModel customerinfo = new UsersModel();
			int defaultBillId = 0;
			String country = CommonUtility.validateString(userDetailsInput.getCountry());
			String account = CommonUtility.validateString(userDetailsInput.getAccountName());
			String firstName = CommonUtility.validateString(userDetailsInput.getFirstName());
			String lastName = CommonUtility.validateString(userDetailsInput.getLastName());
			String password = CommonUtility.validateString(userDetailsInput.getPassword());
			String emailId = CommonUtility.validateString(userDetailsInput.getEmailAddress());
			String phoneNo = CommonUtility.validateString(userDetailsInput.getPhoneNo());
			int buyingCompanyid = 0;
			boolean validInvoice = false;
			String invoiceNo = userDetailsInput.getInvoiceNumber();
			String poNumber = userDetailsInput.getPoNumber()!=null?userDetailsInput.getPoNumber():"";
			String zipCode = userDetailsInput.getZipCode();
			ArrayList<AddressModel> shipToList = new ArrayList<AddressModel>();
			buyingCompanyid = UsersDAO.getBuyingCompanyIdByEntityId(account);

			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(session.getAttribute(Global.USERNAME_KEY).toString()) // web user name as the password.
					.build();
					
			@SuppressWarnings("rawtypes")
			Cimm2BCResponse<Cimm2BCustomer>  response = CimmESBServiceUtils.getCustomerService().getCustomerByErpId(request,account);
			DozerBeanMapper  dozzer = new DozerBeanMapper();
			Cimm2BCentralCustomer customerDetails = new Cimm2BCentralCustomer();
			dozzer.map(response.getData(),customerDetails);
		
			if(customerDetails!=null && response.getStatus().getCode()==200){  
				
					validInvoice=true;
					String entityid="0";
					String billEntityId = "0";
					String warehouse = customerDetails.getHomeBranch()!=null?customerDetails.getHomeBranch():"";
					String defaultShipVia=CommonUtility.validateString(customerDetails.getDefaultShipVia());
					customerinfo.setEntityName(customerDetails.getCustomerName());
					customerinfo.setFirstName(firstName);
					customerinfo.setLastName(lastName);
					customerinfo.setUserName(account);
					customerinfo.setPassword(password);
					customerinfo.setEmailId(emailId);
					customerinfo.setPhoneNo(phoneNo);
					ArrayList<Cimm2BCentralGetInvoiceList> cimm2BInvoices = customerDetails.getCimm2BInvoices();
					if(cimm2BInvoices!=null && cimm2BInvoices.size()>0){
						for(Cimm2BCentralGetInvoiceList invoiceList : cimm2BInvoices){
							UsersModel invoices = new UsersModel();
							invoices.setInvoiceNumber(invoiceList.getInvoiceNumber());
							if(invoiceNo.equalsIgnoreCase(invoices.getInvoiceNumber())){
								validInvoice = true;
								break;
							}
						}
					}
					if(CommonUtility.customServiceUtility()!=null && !validInvoice) {
						validInvoice = CommonUtility.customServiceUtility().doTwoStepValidation(customerDetails, invoiceNo, poNumber, zipCode);
					}
					logger.info("validInvoice"+validInvoice);
					if(validInvoice){

					Cimm2BCentralAddress address = customerDetails.getAddress();

					if(address != null){
						if(CommonUtility.validateString(address.getAddressLine1()).length() > 0){
							customerinfo.setAddress1(address.getAddressLine1());
						}else{
							customerinfo.setAddress1("No Address");
						}

						if(CommonUtility.validateString(address.getAddressLine2()).length() > 0){
							customerinfo.setAddress2(address.getAddressLine2());
						}
						customerinfo.setCity(address.getCity());
						customerinfo.setState(address.getState());
						customerinfo.setZipCode(address.getZipCode());

						if(CommonUtility.validateString(address.getCountry()).length() > 0){
							customerinfo.setCountry(address.getCountry());
						}else{
							customerinfo.setCountry(country);
						}
						
						if(customerDetails.getContacts() != null && customerDetails.getContacts().size() > 0){
							if(CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
								customerinfo.setPhoneNo(customerDetails.getContacts().get(0).getPrimaryPhoneNumber());
							}else if(CommonUtility.validateString(customerDetails.getContacts().get(0).getAlternatePhoneNumber()).length() > 0){
								customerinfo.setPhoneNo(customerDetails.getContacts().get(0).getAlternatePhoneNumber());
							}

							if(CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryEmailAddress()).length() > 0){
								customerinfo.setEmailAddress(customerDetails.getContacts().get(0).getPrimaryEmailAddress());
							}else{
								customerinfo.setEmailAddress(userDetailsInput.getEmailAddress());
							}
						}
					}

					if(userDetailsInput!=null){
						customerinfo.setEmailAddress(CommonUtility.validateString(userDetailsInput.getEmailAddress()));
					}
					customerinfo.setTermsType(customerDetails.getTermsType());
					customerinfo.setTermsTypeDesc(customerDetails.getTermsTypeDescription());
					customerinfo.setShipViaMethod(customerDetails.getDefaultShipVia()!=null?customerDetails.getDefaultShipVia():"");
					customerinfo.setShipViaID(customerDetails.getDefaultShipVia()!=null?customerDetails.getDefaultShipVia():"");
					if(customerDetails.getDefaultShipLocationId()!=null && customerDetails.getDefaultShipLocationId().length()>0){
						customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(customerDetails.getDefaultShipLocationId())));
						customerinfo.setWareHouseCodeStr(CommonUtility.validateString(customerDetails.getDefaultShipLocationId()));
					}else if(customerDetails.getHomeBranch()!=null && customerDetails.getHomeBranch().length()>0){
						customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(customerDetails.getHomeBranch())));
						customerinfo.setWareHouseCodeStr(CommonUtility.validateString(customerDetails.getHomeBranch()));
					}else{
						customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR"))));
						customerinfo.setWareHouseCodeStr(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR")));	
					}
					if(CommonUtility.customServiceUtility()!=null) {
						CommonUtility.customServiceUtility().setDefaultWareHouseFromERP(customerinfo,customerDetails);
					}
					if(customerDetails.getCodFlag()!=null){
						customerinfo.setCodFlag(customerDetails.getCodFlag());	
					}
					if(customerDetails.getPoRequired()!=null){
						customerinfo.setPoRequired(customerDetails.getPoRequired());	
					}
					billEntityId = CommonUtility.validateString(customerDetails.getBillToCustomerERPId());
					entityid = CommonUtility.validateString(account); 

					ArrayList<Cimm2BCentralCustomer> customerLocations = customerDetails.getCustomerLocations();

					if(customerLocations != null && customerLocations.size() > 0){
						for(Cimm2BCentralCustomer shipTo : customerLocations){
							AddressModel shipAddressModel = new AddressModel();	

							Cimm2BCentralAddress shipAddress = shipTo.getAddress();
							if(shipAddress!=null){
								
								if(CommonUtility.validateString(shipAddress.getAddressLine1()).length() > 0){
									shipAddressModel.setAddress1(CommonUtility.validateString(shipAddress.getAddressLine1()));
								}else{
									shipAddressModel.setAddress1("N/A");
								}
								
								if(CommonUtility.validateString(shipAddress.getAddressLine2()).length() > 0){
									shipAddressModel.setAddress2(CommonUtility.validateString(shipAddress.getAddressLine2()));
								}
								
								shipAddressModel.setCity(CommonUtility.validateString(shipAddress.getCity()));
								
								if(CommonUtility.validateString(shipAddress.getCountry()).length() > 0){
									shipAddressModel.setCountry(shipAddress.getCountry());
								}else{
									shipAddressModel.setCountry(" ");
								}
								shipAddressModel.setShipToId(shipAddress.getAddressERPId());
								shipAddressModel.setState(CommonUtility.validateString(shipAddress.getState()));
								shipAddressModel.setZipCode(CommonUtility.validateString(shipAddress.getZipCode()));
								shipAddressModel.setCompanyName(shipTo.getCustomerName());
	
								// Check if we get phone number in getContacts list of customerDetails object
								/*if(shipTo.getContacts() != null && shipTo.getContacts().size() > 0  && CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
									shipAddressModel.setPhoneNo(CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()));
								}*/
								if(shipTo.getContacts() != null && shipTo.getContacts().size() > 0  && CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
									shipAddressModel.setPhoneNo(CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()));
								}
								
								if(CommonUtility.validateString(shipTo.getHomeBranch()).trim().length() > 0 && shipTo.getHomeBranch()!="0"){
									shipAddressModel.setWareHouseCode(shipTo.getHomeBranch());
								}
								shipToList.add(shipAddressModel);
							}
						}

					}


					if(!CommonUtility.validateString(entityid).isEmpty() && !CommonUtility.validateString(entityid).equals("0"))
					{
						customerinfo.setEntityId(entityid);
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API")).length()>0){
						userNumber = createContactInERP(customerinfo);
						}else {
							userNumber="-1";
						}
					 if(!CommonUtility.validateString(userNumber).isEmpty() && !CommonUtility.validateString(userNumber).equals("0"))
					 {
						customerinfo.setEntityId(entityid);

						int userId =0;	   
						boolean insertAddress = false;
						if(buyingCompanyid==0){
							buyingCompanyid = UsersDAO.insertBuyingCompany(conn,customerinfo,0);
							insertAddress = true;
						}
						UsersModel userDetailList = new UsersModel();
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONTACT_INQUIRY_API")).length()>0){
						userDetailList=contactInquiry(userNumber);
						
						}
						if(buyingCompanyid > 0){						           
							customerinfo.setBuyingCompanyId(buyingCompanyid);
							customerinfo.setContactId(CommonUtility.validateString(account));
							customerinfo.setUserStatus(userDetailsInput.getUserStatus());
							userDetailsInput.setBuyingCompanyId(buyingCompanyid);
							userDetailsInput.setContactId(CommonUtility.validateString(account));
							userDetailsInput.setUserStatus(userDetailsInput.getUserStatus());
							userDetailsInput.setTermsType(CommonUtility.validateString(customerDetails.getTermsType()));
							userDetailsInput.setTermsTypeDesc(CommonUtility.validateString(customerDetails.getTermsTypeDescription()));
							userDetailsInput.setAddress1(userDetailList.getAddress1()!=null?userDetailList.getAddress1():"");
							userDetailsInput.setAddress2(userDetailList.getAddress2()!=null?userDetailList.getAddress2():"");
							userDetailsInput.setCity(userDetailList.getCity()!=null?userDetailList.getCity():"");
							userDetailsInput.setState(userDetailList.getState()!=null?userDetailList.getState():"");
							userDetailsInput.setZipCode(userDetailList.getZipCode()!=null?userDetailList.getZipCode():"");
							userDetailsInput.setCountry(userDetailList.getCountry()!=null?userDetailList.getCountry():"US");
							userDetailsInput.setShipViaID(customerinfo.getShipViaID());
							if(userNumber!=null && CommonUtility.validateString(userNumber).length()>0){
							userDetailsInput.setUserERPId(CommonUtility.validateString(userNumber));
							}
							userId = UsersDAO.insertNewUser(conn,userDetailsInput, account, password, buyingCompanyid, "0",false);
						}else{
							result = "Warehouse not found. Error While Registering. Contact our Customer Service for Further Assistance.|";
							logger.info("Warehouse not found block");
						}
						if(userId>0){    
							int shipId= 0;  
							logger.info("customer inserted into database successfully");

							if(insertAddress){
								defaultBillId = UsersDAO.insertNewAddressintoBCAddressBook(conn,customerinfo, "Bill", false);
								UsersModel shipaddressList = new UsersModel();
								if(shipToList.size()>0){
									for(AddressModel getShipList : shipToList){
										shipaddressList = new UsersModel();
										shipaddressList.setFirstName(getShipList.getCompanyName());
										shipaddressList.setAddress1(getShipList.getAddress1());
										shipaddressList.setAddress2(getShipList.getAddress2());
										shipaddressList.setCity(getShipList.getCity());
										shipaddressList.setState(getShipList.getState());
										shipaddressList.setCountryCode(getShipList.getCountry());
										shipaddressList.setCountry(getShipList.getCountry());
										shipaddressList.setShipToId(getShipList.getShipToId());
										shipaddressList.setPhoneNo(getShipList.getPhoneNo());
										shipaddressList.setEntityId(entityid);
										shipaddressList.setZipCode(getShipList.getZipCode());
										shipaddressList.setBuyingCompanyId(buyingCompanyid);
										shipaddressList.setWareHouseCodeStr(getShipList.getWareHouseCode());
										shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn,shipaddressList,"Ship",false);
									}
								}else{
									shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn,customerinfo,"Ship",false);
								}
							}else{
								UsersModel uModel =  UsersDAO.getDefaultShipNbill(account);
								if(uModel!=null){
									defaultBillId = CommonUtility.validateNumber(uModel.getBillToId());
									shipId = CommonUtility.validateNumber(uModel.getShipToId());
								}
							}
							//Need to check
							String userRole = null;
							if(CommonUtility.validateString(userDetailsInput.getUserRole()).length()>0){
								userRole = CommonUtility.validateString(userDetailsInput.getUserRole());
							}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_EXISTING_USER_ROLE")).length()>0){
								userRole =  CommonDBQuery.getSystemParamtersList().get("DEFAULT_EXISTING_USER_ROLE");
							}
							UsersDAO.assignRoleToUser(conn,Integer.toString(userId),userRole);

							result ="";//result = "0|User Registered Successfully.";       
							int count = UsersDAO.updateUserAddressBook(conn,defaultBillId, shipId, userId,false);


							if(userId>0 && customerinfo.getCountryCode()!=null){
								String locUser = "N";
								String defaultCountry = "USA";
								if(CommonDBQuery.getSystemParamtersList().get("DEFAULT_COUNTRY")!=null && CommonDBQuery.getSystemParamtersList().get("DEFAULT_COUNTRY").trim().length()>0){
									defaultCountry = CommonDBQuery.getSystemParamtersList().get("DEFAULT_COUNTRY").trim();
								}
								if(customerinfo.getCountryCode().trim().equalsIgnoreCase(defaultCountry)){
									locUser = "N";
								}else{
									locUser = "Y";
								}
								CustomFieldModel customFieldVal = UsersDAO.getCustomIDs(locUser, "IS_INTERNATIONAL_USER");
								if(customFieldVal!=null){
									UsersDAO.isInternationalUser(conn,userId, customFieldVal.getCustomFieldID(), customFieldVal.getLocCustomFieldValueID());
								}
							}

							result = "0|"+emailId+" has been successfully registered";

							//-------------------------
							ArrayList<UsersModel> superUserStatus = null;
							String buyCompStr = ""+buyingCompanyid;
							superUserStatus = UsersDAO.getSuperUserForCompany(buyCompStr);

							userDetailsInput.setEntityName(customerDetails.getCustomerName());
							userDetailsInput.setEntityId(CommonUtility.validateString(account));
							conn.commit();
							if(CommonDBQuery.getSystemParamtersList().get("CALL_FOR_KEYWORD_BUILDER")!=null && CommonDBQuery.getSystemParamtersList().get("CALL_FOR_KEYWORD_BUILDER").trim().equalsIgnoreCase("Y")){
								if(CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD")!=null && CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD").trim().equalsIgnoreCase("Y")){
									UsersDAO.buildKeyWord("BUYINGCOMPANY", buyingCompanyid);
								}else{
									UsersDAO.buildUserKeyWord("BUYINGCOMPANY", buyingCompanyid);
								}

								if(CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD")!=null && CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD").trim().equalsIgnoreCase("Y")){
									UsersDAO.buildKeyWord("USER", userId);
								}else{
									UsersDAO.buildUserKeyWord("USER", userId);
								}
							}

							if(CommonDBQuery.getSystemParamtersList().get("EXISTING_USER_AUTO_LOGIN")!=null && CommonDBQuery.getSystemParamtersList().get("EXISTING_USER_AUTO_LOGIN").trim().equalsIgnoreCase("Y")){
								LoginAuthentication getWebUser = new LoginAuthentication();
								getWebUser.authenticate(emailId, password, userDetailsInput.getSession());//,ipaddress
								userDetailsInput.getSession().setAttribute("userRegMessage", "true");
							}
							/*
							String codCustomFiledValue = "N";
							String poRequiredCustomFiledValue = "N";
							String gasPoRequiredCustomFiledValue = "N";
							
							String fuelSurcharge="N";
							double minimumOrderAmount=0.0;
							double prepaidFreightAmount=0.0;
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
							
							UsersDAO.insertCustomField(CommonUtility.validateString(codCustomFiledValue), "COD", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(poRequiredCustomFiledValue), "PO_REQUIRED", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(gasPoRequiredCustomFiledValue), "GAS_PO_REQUIRED", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(defaultShipVia), "SHIPPING_METHOD", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(fuelSurcharge), "FUEL_SURCHARGE", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateParseDoubleToString(minimumOrderAmount), "MINIMUM_ORDER_AMOUNT", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateParseDoubleToString(prepaidFreightAmount), "PREPAID_FREIGHT_TARGET_AMOUNT", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(priceListPath), "MY_PRICING_LINK_ONE", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(fullPriceListPath), "MY_PRICING_LINK_TWO",userId, buyingCompanyid, "BUYING_COMPANY");
							*/
							if(CommonUtility.customServiceUtility()!=null) { 
								CommonUtility.customServiceUtility().insertCustomertarget(warehouse,userId,buyingCompanyid); 
								CommonUtility.customServiceUtility().insertCustomFields(warehouse, userId, buyingCompanyid, userDetailsInput);
							} //CustomServiceProvider
							
						}else{
							result = "Error While Registering. Contact our Customer Service for Further Assistance.|";
						}
					}
				}
				}else{
					result="Invalid Invoice, Zip code and PO Number. Please add valid invoice number or zip code or PO Number.";
				}
				
			}else{
				result = response.getStatus().getMessage();
				logger.info(result);
			}
		}catch (Exception e) {
			result = "Invalid Account number. Please enter valid account number.";
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBConnection(conn);
		}
		return result;
	}

	//<------------------------->
	public static UsersModel getcatalogfromerp(UsersModel customerInfoInput){
		try{
			/*CustomerQueryResponse reponse = CustomersUtility.customerQueryResponse(customerInfoInput);
					String catalogname = reponse.getCatalogName();
					int subsetId = UsersDAO.getSubsetId(catalogname);
					customerInfoInput.setCatalogName(catalogname);
					customerInfoInput.setSubsetId(subsetId);*/
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return customerInfoInput; 
	}
	//<------------------------->

	public static void scynEntityAddress(UsersModel customerInfoInput){

		HttpSession session = customerInfoInput.getSession();		
		String lastId = "";
		Connection conn = null;
		try {
			conn = ConnectionManager.getDBConnection();
			session.setAttribute("scynEntityStatus", "Status: Connecting");

			session.setAttribute("scynEntityStatus", "Started");
			String wareHouseCode = "";
			String userName = (String) session.getAttribute(Global.USERNAME_KEY);
			boolean assignDefaultShipTo=true;
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int buyingCompanyId = CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId")); 
			String contactId = (String) session.getAttribute("contactId");
			int defaultShipToId=0;
			int i=0;
			String customerNumber = customerInfoInput.getCustomerId();
			//String userSessionId = (String) session.getAttribute("userToken");

			if(userName!=null && userName.trim().length()>0 && customerNumber!=null && customerNumber.trim().length()>0){

				session.setAttribute("scynEntityStatus", "Status: Getting Address");

				ArrayList<UsersModel> shipToListFromDB = new ArrayList<UsersModel>();
				ArrayList<UsersModel> billToListFromDB = new ArrayList<UsersModel>();
				HashMap<String, ArrayList<UsersModel>> templist = UsersDAO.getAddressListFromBCAddressBook(buyingCompanyId,CommonUtility.validateNumber(sessionUserId));
				shipToListFromDB = templist.get("Ship");
				billToListFromDB = templist.get("Bill");
				String warehouse="";
				UsersModel customerinfo = new UsersModel();

				session.setAttribute("scynEntityStatus", "Status: Getting Address");
				//userDetailList = EntityInquiry.eclipseEntityEnquiry(userSessionId,entityId,userName);

				UsersModel customerQueryInfoInput = new UsersModel();
				customerQueryInfoInput.setUserToken(customerNumber);

				String GET_CUSTOMER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  customerNumber;
				Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_CUSTOMER_URL, HttpMethod.GET, null, Cimm2BCentralCustomer.class);
				Cimm2BCentralCustomer customerDetails = new Cimm2BCentralCustomer();

				ArrayList<AddressModel> shiptoListFromErp = new ArrayList<AddressModel>();
				if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == HttpStatus.SC_OK ){  

					customerDetails = (Cimm2BCentralCustomer) customerDetailsResponseEntity.getData();

					if(customerDetails != null){
						if(billToListFromDB!=null)
						{
						customerinfo = billToListFromDB.get(0);
						}
						warehouse = customerDetails.getHomeBranch()!=null?customerDetails.getHomeBranch():"";
						customerinfo.setEntityName(customerDetails.getCustomerName());

						Cimm2BCentralAddress address = customerDetails.getAddress();

						if(address != null){

							if(CommonUtility.validateString(address.getAddressLine1()).length() > 0){
								customerinfo.setAddress1(address.getAddressLine1());
							}else{
								customerinfo.setAddress1("No Address");
							}

							if(CommonUtility.validateString(address.getAddressLine2()).length() > 0){
								customerinfo.setAddress2(address.getAddressLine2());
							}
							customerinfo.setCity(address.getCity());
							//customerinfo.setPhoneNo(address.getPhone());
							customerinfo.setState(address.getState());
							customerinfo.setZipCode(address.getZipCode());

							if(CommonUtility.validateString(address.getCountry()).length() > 0){
								customerinfo.setCountry(address.getCountry());
							}else{
								customerinfo.setCountry("US");
							}

							if(customerDetails.getContacts() != null && customerDetails.getContacts().size() > 0){
								if(CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
									customerinfo.setPhoneNo(customerDetails.getContacts().get(0).getPrimaryPhoneNumber());
								}else if(CommonUtility.validateString(customerDetails.getContacts().get(0).getAlternatePhoneNumber()).length() > 0){
									customerinfo.setPhoneNo(customerDetails.getContacts().get(0).getAlternatePhoneNumber());
								}

								if(CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryEmailAddress()).length() > 0){
									customerinfo.setEmailAddress(customerDetails.getContacts().get(0).getPrimaryEmailAddress());
								}
							}
						}

						customerinfo.setTermsType(customerDetails.getTermsType());
						customerinfo.setTermsTypeDesc(customerDetails.getTermsTypeDescription());
						customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(warehouse.replaceFirst("^0+(?!$)", "")));
						customerinfo.setWareHouseCodeStr(warehouse.replaceFirst("^0+(?!$)", ""));
						if(customerDetails.getCodFlag()!=null){
						customerinfo.setCodFlag(customerDetails.getCodFlag());}
						if(customerDetails.getPoRequired()!=null){
						customerinfo.setPoRequired(customerDetails.getPoRequired());}
						customerinfo.setBuyingCompanyId(buyingCompanyId);



						ArrayList<Cimm2BCentralCustomer> customerLocations = customerDetails.getCustomerLocations();

						if(customerLocations != null && customerLocations.size() > 0){
							if(address.getAddressLine1() == null || address.getZipCode() == null) {
								customerinfo.setAddress1(customerLocations.get(0).getAddress().getAddressLine1());
								customerinfo.setCity(customerLocations.get(0).getAddress().getCity());
								customerinfo.setState(customerLocations.get(0).getAddress().getState());
								customerinfo.setCountry(customerLocations.get(0).getAddress().getCountry());
								customerinfo.setCountryCode(customerLocations.get(0).getAddress().getCountry());
								customerinfo.setZipCode(customerLocations.get(0).getAddress().getZipCode());
							}
							for(Cimm2BCentralCustomer shipTo : customerLocations){
								AddressModel shipAddressModel = new AddressModel();	

								Cimm2BCentralAddress shipAddress = shipTo.getAddress();
								shipAddressModel.setAddress1(shipAddress.getAddressLine1());
								if(CommonUtility.validateString(shipAddress.getAddressLine2()).length() > 0){
									shipAddressModel.setAddress2(shipAddress.getAddressLine2());
								}
								shipAddressModel.setCity(shipAddress.getCity());
								if(CommonUtility.validateString(shipAddress.getCountry()).length() > 0){
									shipAddressModel.setCountry(shipAddress.getCountry());
								}else{
									shipAddressModel.setCountry("US");
								}
								shipAddressModel.setState(shipAddress.getState());
								shipAddressModel.setZipCode(shipAddress.getZipCode());
								shipAddressModel.setCompanyName(shipTo.getCustomerName());
								shipAddressModel.setShipToId(shipAddress.getAddressERPId());
								if(shipTo.getContacts() != null && customerDetails.getContacts().size() > 0 && CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
									shipAddressModel.setPhoneNo(shipTo.getContacts().get(0).getPrimaryPhoneNumber());
								}
								if(CommonUtility.validateString(shipTo.getHomeBranch()).trim().length() > 0){
									shipAddressModel.setWareHouseCode(shipTo.getHomeBranch());
								}
								shiptoListFromErp.add(shipAddressModel);
							}

						}
					}
				}

				if(shiptoListFromErp!=null && !shiptoListFromErp.isEmpty()) {

					conn.setAutoCommit(false);
					int defaultBillId = 0;
					int shipId = 0;

					int addressDeleted = UsersDAO.deleteBuyingCompanyAddressFromAddressBook(conn, buyingCompanyId);

					if(addressDeleted > 0){
						conn.commit();
						defaultBillId = UsersDAO.insertNewAddressintoBCAddressBook(conn,customerinfo, "Bill", false);

						UsersModel shipaddressList = new UsersModel();
						if(shiptoListFromErp.size()>0){
							for(AddressModel getShipList : shiptoListFromErp){
								shipaddressList = new UsersModel();
								shipaddressList.setFirstName(getShipList.getCompanyName());
								shipaddressList.setAddress1(getShipList.getAddress1());
								shipaddressList.setAddress2(getShipList.getAddress2());
								shipaddressList.setCity(getShipList.getCity());
								shipaddressList.setState(getShipList.getState());
								shipaddressList.setCountryCode(getShipList.getCountry());
								shipaddressList.setCountry(getShipList.getCountry());
								shipaddressList.setShipToId(getShipList.getShipToId());
								shipaddressList.setPhoneNo(getShipList.getPhoneNo());
								shipaddressList.setEntityId(contactId);
								shipaddressList.setZipCode(getShipList.getZipCode());
								shipaddressList.setBuyingCompanyId(buyingCompanyId);
								shipaddressList.setWareHouseCodeStr(getShipList.getWareHouseCode());
								shipaddressList.setWareHouseName(getShipList.getWareHouseName());
                                shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn,shipaddressList,"Ship",false);
                                if(!customerDetails.getDefaultShipLocationId().equalsIgnoreCase("") && assignDefaultShipTo)
                                {
                                if(getShipList.getShipToId().equalsIgnoreCase(customerDetails.getDefaultShipLocationId()))
								{
                                	defaultShipToId=shipId;
                                	assignDefaultShipTo=false;
								}
                                }
							}
						}else{
							shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn,customerinfo,"Ship",false);
						}

					}
					if(defaultShipToId!=0)
					{
						shipId =defaultShipToId;
					}
					int count = UsersDAO.updateBuyingComapnyAddressBook(conn,defaultBillId, shipId, buyingCompanyId,false);

					if(count > 0){
						session.setAttribute("defaultShipToId",CommonUtility.validateParseIntegerToString(shipId));
						session.setAttribute("defaultShippingAddressId",CommonUtility.validateParseIntegerToString(shipId));
						session.setAttribute("defaultBillToId",CommonUtility.validateParseIntegerToString(defaultBillId));
						session.setAttribute("defaultBillingAddressId",CommonUtility.validateParseIntegerToString(defaultBillId));

						conn.commit();
						session.setAttribute("scynEntityStatus","Status: ShipTo Addresses Update Completed Successfully.");
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
				if(customerDetails.getCodFlag()!=null && customerDetails.getCodFlag()) {
					codCustomFiledValue = "Y";
				}
				if(customerDetails.getPoRequired()!=null && customerDetails.getPoRequired()){
					poRequiredCustomFiledValue = "Y";
				}
				if(customerDetails.getGasPoRequired()!=null){
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
				UsersDAO.insertCustomField(codCustomFiledValue, "COD", CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				UsersDAO.insertCustomField(poRequiredCustomFiledValue, "PO_REQUIRED", CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				UsersDAO.insertCustomField(gasPoRequiredCustomFiledValue, "GAS_PO_REQUIRED", CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				UsersDAO.insertCustomField(CommonUtility.validateString(defaultShipVia), "SHIPPING_METHOD", CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				UsersDAO.insertCustomField(CommonUtility.validateString(fuelSurcharge), "FUEL_SURCHARGE", CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				UsersDAO.insertCustomField(CommonUtility.validateParseDoubleToString(minimumOrderAmount), "MINIMUM_ORDER_AMOUNT", CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				UsersDAO.insertCustomField(CommonUtility.validateParseDoubleToString(prepaidFreightAmount), "PREPAID_FREIGHT_TARGET_AMOUNT", CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				UsersDAO.insertCustomField(CommonUtility.validateString(priceListPath), "MY_PRICING_LINK_ONE", CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				UsersDAO.insertCustomField(CommonUtility.validateString(fullPriceListPath), "MY_PRICING_LINK_TWO",CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				*/
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ALL_USER_CUSTOMER_CUSTOM_FIELDS")).equalsIgnoreCase("Y")){
					LinkedHashMap<String, String> userCustomFieldValue = new LinkedHashMap<String, String>();
					LinkedHashMap<String, String> customerCustomFieldValue = new LinkedHashMap<String, String>();
					if(CommonUtility.validateNumber(sessionUserId) > 0 ){
						userCustomFieldValue = UsersDAO.getAllUserCustomFieldValue(CommonUtility.validateNumber(sessionUserId));
						if(userCustomFieldValue!=null){
							session.setAttribute("userCustomFieldValue",userCustomFieldValue);
						}
					}
					if(buyingCompanyId > 0){
						customerCustomFieldValue = UsersDAO.getAllCustomerCustomFieldValue(buyingCompanyId);
						if(customerCustomFieldValue!=null && customerCustomFieldValue.size()>0){
							session.setAttribute("customerCustomFieldValue",customerCustomFieldValue);
						}
					}
				}

			}

		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			session.setAttribute("scynEntityStatus","Ship Addresses Status: Something went wrong. Please Try Again");
			session.setAttribute("resumeScyn",lastId);
		}finally{
			ConnectionManager.closeDBConnection(conn);
		}
	}
	public String createWLUser(UsersModel userDetails){
		
		AddressModel userRegistrationDetail = null;
		AddressModel shipAddress = null;
		String result = "Server Error. Please Try again later.|";
		try {
			userRegistrationDetail = new AddressModel();
			userRegistrationDetail.setFirstName(userDetails.getFirstName());
			userRegistrationDetail.setLastName(userDetails.getLastName());
			userRegistrationDetail.setEmailAddress(userDetails.getEmailAddress());
			userRegistrationDetail.setUserPassword(userDetails.getPassword());
			userRegistrationDetail.setCompanyName(userDetails.getBillAddress().getCompanyName());
			userRegistrationDetail.setAddress1(userDetails.getBillAddress().getAddress1());
			userRegistrationDetail.setAddress2(userDetails.getBillAddress().getAddress2());
			userRegistrationDetail.setCity(userDetails.getBillAddress().getCity());
			userRegistrationDetail.setState(userDetails.getBillAddress().getState());
			userRegistrationDetail.setZipCode(userDetails.getBillAddress().getZipCode());
			userRegistrationDetail.setCountry(userDetails.getBillAddress().getCountry());
			userRegistrationDetail.setPhoneNo(userDetails.getBillAddress().getPhoneNo());
			userRegistrationDetail.setCustomerType(userDetails.getCustomerType());
			userRegistrationDetail.setFaxNumber("");
			//userRegistrationDetail.setLocUser(locUser);
			userRegistrationDetail.setRole("Ecomm Retail User");
			userRegistrationDetail.setUpdateRole(true);
			userRegistrationDetail.setUserStatus("Y");
			userRegistrationDetail.setNewsLetterSub("N");
			userRegistrationDetail.setAnonymousUser(true);
			if(userDetails.getBuyingCompanyId()>0) {
				userRegistrationDetail.setBuyingComanyIdStr(CommonUtility.validateParseIntegerToString(userDetails.getBuyingCompanyId())); //guest checkout
			}
			
			if(CommonUtility.validateString(userDetails.getSameAsBillingAddress()).equalsIgnoreCase("N")){
				shipAddress = userDetails.getShipAddress();
				userRegistrationDetail.setShippingAddress(shipAddress);
			}
			/*if(userDetails.getCustomerType().equalsIgnoreCase("G"))
			{
			userRegistrationDetail.setGuestRegistrationRequired(true);	
			}
			else
			{
			userRegistrationDetail.setGuestRegistrationRequired(false);	
			}
			if(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID").length()>0 && userRegistrationDetail.isGuestRegistrationRequired())
			{   
				userRegistrationDetail.setAccountName(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID"));
				result=newOnAccountUserRegistration(Cimm2BCentralClient.getInstance().addressModelToUsersModel(userRegistrationDetail));
			}
			else
			{*/
			result = createRetailUser(userRegistrationDetail);
			/*}*/
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			result = "Error while placing order. Please Try again later|";
		}
		return result;
	}

	public String getCustomerDataFromERP(UsersModel userModel){
		String jsonResponse =  "";
		Gson gson = new Gson();
		try {
			Cimm2BCustomer customerDetails = getCustomer(userModel);
			jsonResponse = gson.toJson(customerDetails);
			
		} catch (Exception e) {
			jsonResponse = "";
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return jsonResponse;
	}
	public static int getLastOrderDetails(UsersModel userDetails){

		int lastOrderNumber = 0;
		try{
			String userEnteredCustomerId = userDetails.getUserToken();
	
			String GET_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_ORDER_API")) + "?" + Cimm2BCentralRequestParams.orderNumber+ "=" + userDetails.getOrderID() + "&" + Cimm2BCentralRequestParams.sequenceNumber + "=" + "";
			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_ORDER_URL, HttpMethod.GET, null, Cimm2BCentralOrder.class);
	
			Cimm2BCentralOrder orderResponse = null;
			if(orderResponseEntity!=null && orderResponseEntity.getData() != null && orderResponseEntity.getStatus() != null && orderResponseEntity.getStatus().getCode() == HttpStatus.SC_OK){
				orderResponse = (Cimm2BCentralOrder) orderResponseEntity.getData();
				orderResponse.setStatus(orderResponseEntity.getStatus());
	
				if(orderResponse != null && CommonUtility.validateString(orderResponse.getCustomerERPId()).equalsIgnoreCase(userEnteredCustomerId)){
					lastOrderNumber = CommonUtility.validateNumber(userDetails.getOrderID());
				}
	
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return lastOrderNumber;
	}
	public UsersModel getTravelPoint(UsersModel userModel){
		return userModel;
	}
	public static String createNewAgent(AddressModel userInfo)
	{
		String result = "";
		String roleName = "";
		int userId;
		String userNumber;
		String locUser=null;
		String buyingComanyIdStr=null;
		String parentUserId=null;
		Connection  conn = null;
		try{
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API")).length()>0) {
				UsersModel contactInfo=new UsersModel();
				contactInfo.setFirstName(userInfo.getFirstName());
				contactInfo.setLastName(userInfo.getLastName());
				contactInfo.setEmailId(userInfo.getEmailAddress());
				contactInfo.setPhoneNo(userInfo.getPhoneNo());
				contactInfo.setPassword(userInfo.getUserPassword());
				contactInfo.setUserRole(userInfo.getRole());
				contactInfo.setAddress1(userInfo.getAddress1());
				contactInfo.setAddress2(userInfo.getAddress2());
				contactInfo.setCity(userInfo.getCity());
				contactInfo.setState(userInfo.getState());
				contactInfo.setZipCode(userInfo.getZipCode());
				contactInfo.setCountry(userInfo.getCountry());
				contactInfo.setUserName(userInfo.getEmailAddress());
				contactInfo.setEmailAddress(userInfo.getEmailAddress());
				contactInfo.setEntityId(userInfo.getEntityId());
			    buyingComanyIdStr=userInfo.getBuyingComanyIdStr();
				userNumber = createContactInERP(contactInfo);
				userId = CommonUtility.validateNumber(userNumber);
				if(userId>0){
				result = newRetailUserRegistration(userNumber, userInfo.getCountry(), userInfo.getUserPassword(), locUser,userInfo.getRole(), buyingComanyIdStr, userInfo.getParentUserId(),userInfo.getFirstName(),userInfo.getLastName(),userInfo.getUserStatus(), null, true, true, contactInfo);
			}
			}else {
				 userId = UserRegisterUtility.registerInforSXUsertoDB(userInfo);
			}
			if(userId>0)
			{
					conn = ConnectionManager.getDBConnection();
					ArrayList<Integer> approverId = new ArrayList<Integer>();
					approverId.add(CommonUtility.validateNumber(userInfo.getParentUserId()));
					UsersDAO.assignApprover(userId, approverId, userId);
					if(userInfo.getRole()!=null && userInfo.getRole().trim().length()>0){
						roleName = userInfo.getRole().trim();
					}
					UsersDAO.assignRoleToUser(conn,Integer.toString(userId), roleName);
					result = "1|"+userInfo.getEmailAddress();
					SendMailModel sendMailModel = new SendMailModel();
					sendMailModel.setFirstName(userInfo.getFirstName());
					sendMailModel.setToEmailId(userInfo.getEmailAddress());
					sendMailModel.setLastName(userInfo.getLastName());
					sendMailModel.setUserName(userInfo.getEmailAddress());
					sendMailModel.setPassword(userInfo.getUserPassword());
					SendMailUtility sendMailUtility = new SendMailUtility();
					sendMailUtility.newUserMail(sendMailModel);
				} else
				{
					result = "0|"+result + "Error while registering new user. Contact our customer service for further assistance.|";
				}
		}catch (SQLException e) { 
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBConnection(conn);
		}
		return result;
	}



	/*public static String existingUserRegistration(UsersModel customerInfoInput){

				boolean loginSuccess= false;
				boolean validOrderNumber = false;
				SecureData Decryptpassword = new SecureData();
				String result = "";
				int count = 0;
				SendMailUtility sendMailUtility = new SendMailUtility();
				HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId(customerInfoInput.getUserToken(),"Y");

				if(CommonUtility.validateNumber(userDetails.get("userId")) > 2 && Decryptpassword.validatePassword(userDetails.get("password")).equals(CommonUtility.validateString(customerInfoInput.getCurrentPassword()))){
					Cimm2BCentralCustomerModel cimm2bCentralCustomerModel =  Cimm2BCentralUtility.getInstance().getCustomer(userDetails.get("billingEntityId"));

					if(cimm2bCentralCustomerModel != null){
						loginSuccess = true;
					}else{
						loginSuccess = false;
					}
					validOrderNumber = Cimm2BCentralUtility.getInstance().getOrderNumber(customerInfoInput.getInvoiceNumber());
					if(!loginSuccess){
						result = "Invalid Current User ID or Current Password.|";
					}else if(!validOrderNumber){
						result = "Entered Order number was not found|";
					}else{
						count = UsersDAO.updateUserInDBforcimm2bCentral(customerInfoInput);
						if(count > 0){
							result = "Registration request submitted successfully";
							sendMailUtility.sendRegistrationMail(customerInfoInput,"customer","","1A");  
						}
					}

				}else{
					result = "Invalid Current User ID or Current Password.|";
				}

				return result;
			}*/

	/*public static String existingUserRegistration(UsersModel customerInfoInput){

		boolean loginSuccess= false;
		boolean validOrderNumber = false;
		SecureData Decryptpassword = new SecureData();
		String result = "";
		int count = 0;
		SendMailUtility sendMailUtility = new SendMailUtility();
		HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId(customerInfoInput.getUserToken(),"Y");

		if(CommonUtility.validateNumber(userDetails.get("userId")) > 2 && Decryptpassword.validatePassword(userDetails.get("password")).equals(CommonUtility.validateString(customerInfoInput.getCurrentPassword()))){
			Cimm2BCentralCustomerModel cimm2bCentralCustomerModel =  Cimm2BCentralUtility.getInstance().getCustomer(userDetails.get("billingEntityId"));

			if(cimm2bCentralCustomerModel != null){
				loginSuccess = true;
			}else{
				loginSuccess = false;
			}
			String orderCustomerErpId = Cimm2BCentralUtility.getInstance().getOrderNumber(customerInfoInput.getInvoiceNumber());
			if(CommonUtility.validateString(orderCustomerErpId).equalsIgnoreCase(userDetails.get("billingEntityId"))){
				validOrderNumber = true;
			}
			if(!loginSuccess){
				result = "Invalid Current User ID or Current Password.|";
			}else if(!validOrderNumber){
				result = "Entered Order number was not found|";
			}else{
				count = UsersDAO.updateUserInDBforcimm2bCentral(customerInfoInput);
				if(count > 0){
					result = "Registration request submitted successfully";
					sendMailUtility.sendRegistrationMail(customerInfoInput,"customer","","1A");  
				}
			}

		}else{
			result = "Invalid Current User ID or Current Password.|";
		}

		return result;
	}*/

	public static UserManagementModel arGetCustomerDataCreditResponse(UsersModel usersModel){
		UserManagementModel  customerDataCreditOutput = null;
		try{
			customerDataCreditOutput = new UserManagementModel();
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return customerDataCreditOutput;	
	}

	public static UserManagementModel arGetCustomerBalanceV2Response(UsersModel usersModel){
		UserManagementModel  customerDataCreditOutput = null;
		try{
			customerDataCreditOutput = new UserManagementModel();
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return customerDataCreditOutput;	
	}

	public static UserManagementModel arGetCustomerDataGeneralV2Response(UsersModel usersModel){
		UserManagementModel  userModelOutPut = null;
		try{
		  userModelOutPut = new UserManagementModel();
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return userModelOutPut;
	}

	public static Cimm2BCentralARBalanceSummary getARBalanceForCimm2BCentral(UsersModel usersModel){
		Cimm2BCentralARBalanceSummary arBalanceSummary = null;
		String url = "";
		Cimm2BCentralResponseEntity arBalanceSummaryEntity = null;
		try{
			Cimm2BCentralARBalanceSummaryRequest arBalanceSummaryRequest = new Cimm2BCentralARBalanceSummaryRequest();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
			arBalanceSummaryRequest.setCustomerERPId(usersModel.getCustomerId());
			arBalanceSummaryRequest.setAgingDate(dateFormat.format(new Date()));
			
			if(usersModel.isDataUpTo30Days()){
				arBalanceSummaryRequest.setDataUpto30Days(true);
			}else{
				arBalanceSummaryRequest.setDataUpto30Days(false);
			}
			if(usersModel.isDataUpTo60Days()){
				arBalanceSummaryRequest.setDataUpto60Days(true);
			}else{
				arBalanceSummaryRequest.setDataUpto60Days(false);
			}
			if(usersModel.isDataUpTo90Days()){
				arBalanceSummaryRequest.setDataUpto90Days(true);
			}else{
				arBalanceSummaryRequest.setDataUpto90Days(false);
			}
			if(usersModel.isDataUpTo120Days()){
				arBalanceSummaryRequest.setDataUpto120Days(true);
			}else{
				arBalanceSummaryRequest.setDataUpto120Days(false);
			}
			
			if(!usersModel.isDataUpTo30Days() && !usersModel.isDataUpTo60Days() && !usersModel.isDataUpTo90Days() && !usersModel.isDataUpTo120Days()){
				arBalanceSummaryRequest.setDataUpto90Days(true);
			}
			
			url = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ACCOUNT_RECEIVABLE_BALANCE_SUMMARY"));
			arBalanceSummaryEntity = Cimm2BCentralClient.getInstance().getDataObject(url, HttpMethod.POST, arBalanceSummaryRequest, Cimm2BCentralARBalanceSummary.class);
	
			if(arBalanceSummaryEntity!=null && arBalanceSummaryEntity.getData() != null && arBalanceSummaryEntity.getStatus() != null && arBalanceSummaryEntity.getStatus().getCode() == HttpStatus.SC_OK){
				arBalanceSummary = (Cimm2BCentralARBalanceSummary) arBalanceSummaryEntity.getData();
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return arBalanceSummary;
	}

	public static  Cimm2BCentralCylinderBalanceSummary getCylinderBalanceForCimm2BCentral(UsersModel usersModel){

		Cimm2BCentralCylinderBalanceSummary cylinderBalanceDetailList = null;
		try{
			String CYLINDER_BALANCE_DETAILS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CONSOLIDATED_CYLINDERBALANCES_URL")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" + usersModel.getCustomerId();
			Cimm2BCentralResponseEntity cylinderBalanceResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CYLINDER_BALANCE_DETAILS, HttpMethod.GET, null, Cimm2BCentralCylinderBalanceSummary.class);
	
			if(cylinderBalanceResponseEntity!=null && cylinderBalanceResponseEntity.getData() != null && cylinderBalanceResponseEntity.getStatus() != null && cylinderBalanceResponseEntity.getStatus().getCode() == HttpStatus.SC_OK){
				cylinderBalanceDetailList = (Cimm2BCentralCylinderBalanceSummary) cylinderBalanceResponseEntity.getData();
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return cylinderBalanceDetailList;
	}

	public  static  ArrayList<UserManagementModel> ARGetInvoiceListV2(UsersModel userModel){

		ArrayList<UserManagementModel> invoiceList = new ArrayList<UserManagementModel>();
		try{
			List<Cimm2BCentralARBalanceDetails> arBalanceDetails = null;
			String url = "";
			Cimm2BCentralResponseEntity arBalanceSummaryEntity = null;
			Cimm2BCentralARBalance arBalance=null;
			Cimm2BCentralARBalanceSummaryRequest arBalanceSummaryRequest = new Cimm2BCentralARBalanceSummaryRequest();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
			arBalanceSummaryRequest.setCustomerERPId(userModel.getCustomerId());
			arBalanceSummaryRequest.setAgingDate(dateFormat.format(new Date()));
			arBalanceSummaryRequest.setInvoiceNumber(CommonUtility.validateString(userModel.getInvoiceNumber()));
			url = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ACCOUNT_RECEIVABLE_BALANCE_DETAILS"));
			arBalanceSummaryEntity = Cimm2BCentralClient.getInstance().getDataObject(url, HttpMethod.POST, arBalanceSummaryRequest, Cimm2BCentralARBalance.class);
	
			if(arBalanceSummaryEntity!=null && arBalanceSummaryEntity.getData() != null && arBalanceSummaryEntity.getStatus() != null && arBalanceSummaryEntity.getStatus().getCode() == HttpStatus.SC_OK){
				arBalance=(Cimm2BCentralARBalance)arBalanceSummaryEntity.getData();
				if(arBalance!=null){
				arBalanceDetails = arBalance.getDetails();
	
				if(arBalanceDetails!=null && arBalanceDetails.size()>0){
				for(Cimm2BCentralARBalanceDetails balanceDetail : arBalanceDetails){
					UserManagementModel userManagementModel = new UserManagementModel();
					userManagementModel.setCustomerERPId(balanceDetail.getCustomerERPId());
					userManagementModel.setDocumentNumber(balanceDetail.getDocumentNumber());
					userManagementModel.setApplyToNumber(balanceDetail.getApplyToNumber());
					userManagementModel.setLineNumber(balanceDetail.getLineNumber());
					userManagementModel.setBranch(balanceDetail.getBranch());
					userManagementModel.setBalance(balanceDetail.getBalance());
					userManagementModel.setDebit(balanceDetail.getDebit());
					userManagementModel.setCredit(balanceDetail.getCredit());
					userManagementModel.setDocumentAge(balanceDetail.getDocumentAge());
					userManagementModel.setPastDue(balanceDetail.getPastDue());
					userManagementModel.setCashBatch(balanceDetail.getCashBatch());
					userManagementModel.setUniqueId(balanceDetail.getUniqueId());
					userManagementModel.setDueDate(balanceDetail.getDueDate());
					userManagementModel.setDocumentDate(balanceDetail.getDocumentDate());
					//--Norco
					userManagementModel.setInvoiceNumber(balanceDetail.getDocumentNumber());
					userManagementModel.setInvoiceDate(balanceDetail.getDocumentDate());
					if(balanceDetail.getDebit()!=null)
						userManagementModel.setAmount(CommonUtility.validateParseDoubleToString(balanceDetail.getDebit()));
					if(balanceDetail.getBalance()!=null)
						userManagementModel.setAmountDue(CommonUtility.validateParseDoubleToString(balanceDetail.getBalance()));
					userManagementModel.setPastDue(balanceDetail.getPastDue());
					//--Norco
					invoiceList.add(userManagementModel);
				}
				}}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return invoiceList;
	}
	public static ArrayList<UserManagementModel> getARBalanceDetailForCimm2BCentral(UsersModel usersModel){
		ArrayList<UserManagementModel> ARBalanceList = new ArrayList<UserManagementModel>();
		Cimm2BCentralARBalance Cimm2BCentralARBalance = null;
		List<Cimm2BCentralARBalanceDetails> arBalanceDetails = null;
		String url = "";
		Cimm2BCentralResponseEntity arBalanceSummaryEntity = null;
		try{
			Cimm2BCentralARBalanceSummaryRequest arBalanceSummaryRequest = new Cimm2BCentralARBalanceSummaryRequest();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
			arBalanceSummaryRequest.setCustomerERPId(usersModel.getCustomerId());
			arBalanceSummaryRequest.setAgingDate(dateFormat.format(new Date()));
			//url = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ACCOUNT_RECEIVABLE_BALANCE_SUMMARY"));
			url = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ACCOUNT_RECEIVABLE_BALANCE_DETAILS"));
			arBalanceSummaryEntity = Cimm2BCentralClient.getInstance().getDataObject(url, "POST", arBalanceSummaryRequest, Cimm2BCentralARBalance.class);
	
			if(arBalanceSummaryEntity!=null && arBalanceSummaryEntity.getData() != null && arBalanceSummaryEntity.getStatus() != null && arBalanceSummaryEntity.getStatus().getCode() == HttpStatus.SC_OK){
				Cimm2BCentralARBalance = (Cimm2BCentralARBalance) arBalanceSummaryEntity.getData();
	
				if(Cimm2BCentralARBalance != null) {
					arBalanceDetails= Cimm2BCentralARBalance.getDetails();
				for(Cimm2BCentralARBalanceDetails balanceDetail : arBalanceDetails){
					UserManagementModel userManagementModel = new UserManagementModel();
					userManagementModel.setCustomerERPId(balanceDetail.getCustomerERPId());
					userManagementModel.setOrderNumber(CommonUtility.validateNumber(balanceDetail.getOrderNumber()));
					userManagementModel.setDocumentNumber(balanceDetail.getDocumentNumber());
					userManagementModel.setApplyToNumber(balanceDetail.getApplyToNumber());
					userManagementModel.setLineNumber(balanceDetail.getLineNumber());
					userManagementModel.setBranch(balanceDetail.getBranch());
					userManagementModel.setBalance(balanceDetail.getBalance());
					userManagementModel.setTotal2Balance(balanceDetail.getHighBalance());
					userManagementModel.setDebit(balanceDetail.getDebit());
					userManagementModel.setCredit(balanceDetail.getCredit());
					userManagementModel.setDocumentAge(balanceDetail.getDocumentAge());
					userManagementModel.setPastDue(balanceDetail.getPastDue());
					userManagementModel.setCashBatch(balanceDetail.getCashBatch());
					userManagementModel.setUniqueId(balanceDetail.getUniqueId());
					userManagementModel.setDueDate(balanceDetail.getDueDate());
					userManagementModel.setDocumentDate(balanceDetail.getDocumentDate());
					
					userManagementModel.setInvoiceNumber(balanceDetail.getInvoiceNumber());
					userManagementModel.setInvoiceDate(balanceDetail.getInvoicedDate());
					if(balanceDetail.getSalesTotal()!=null)
					 userManagementModel.setBalance(balanceDetail.getSalesTotal());
					userManagementModel.setTermsTypeDescription(balanceDetail.getTermsTypeDescription());
					
					if(balanceDetail.getDebit()!=null)
						userManagementModel.setAmount(CommonUtility.validateParseDoubleToString(balanceDetail.getDebit()));
					
					userManagementModel.setAmountDue(CommonUtility.validateParseDoubleToString(balanceDetail.getDueBalance()));
					userManagementModel.setPastDue(balanceDetail.getPastDue());
					if(balanceDetail.getArBalanceSummary() != null) {
						Gson gson = new Gson();
						String jsonStr = gson.toJson(balanceDetail.getArBalanceSummary());
						userManagementModel.setArSummaryDetails(gson.fromJson(jsonStr, JsonArray.class));
					}
					ARBalanceList.add(userManagementModel);
				}
			}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return ARBalanceList;
	}
	public static String createContactInERP(UsersModel usersInfo){
		
		String userID = "";
		try{
			Cimm2BCentralUser userInfo=new Cimm2BCentralUser();
			//Cimm2BCentralUser customerLocation=new Cimm2BCentralUser();
			Cimm2BCentralAddress billToAddress=new Cimm2BCentralAddress();
			//Cimm2BCentralAddress shipToAddress=new Cimm2BCentralAddress();
			
			billToAddress.setAddressLine1(usersInfo.getAddress1());
			billToAddress.setAddressLine2(usersInfo.getAddress2());
			billToAddress.setCity(usersInfo.getCity());
			billToAddress.setState(usersInfo.getState());
			billToAddress.setCountry(usersInfo.getCountry());
			billToAddress.setZipCode(usersInfo.getZipCode());
			billToAddress.setCounty(null);
			
			ArrayList<Cimm2BCentralContact> billContacts=new ArrayList<Cimm2BCentralContact>();
			Cimm2BCentralContact billToContact=new Cimm2BCentralContact();
		
			
			billToContact.setFirstName(usersInfo.getFirstName());
			billToContact.setLastName(usersInfo.getLastName());
			billToContact.setPrimaryEmailAddress(usersInfo.getEmailId());
			billToContact.setPrimaryPhoneNumber(usersInfo.getPhoneNo());
			billContacts.add(billToContact);
	
			
			userInfo.setCustomerERPId(CommonUtility.validateString(usersInfo.getEntityId()));
			userInfo.setAddress(billToAddress);
			userInfo.setContacts(billContacts);
			userInfo.setPassword(usersInfo.getPassword());
		
			String CREATE_USER_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API"));
			Cimm2BCentralResponseEntity userDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_USER_API, "POST", userInfo, String.class);
			if(userDetailsResponseEntity!=null && userDetailsResponseEntity.getData() != null &&  userDetailsResponseEntity.getStatus().getCode() == 200 ){  
				userID=	userDetailsResponseEntity.getData().toString();
				logger.info("User created:"+userID);
			}
			else{
				userID="";
				logger.info("User not created:"+userID);
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return userID;
	}
	
	public static Cimm2BCentralNearestWareHouseResponse getWareHouseBasedOnZipCode(String zipCode){
		Cimm2BCentralNearestWareHouseResponse Response = null;
		int perpage = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("WAREHOUSE_PER_PAGE")); 
		try{
			String GET_ORDER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMM2BC_NEARESTWAREHOUSE_API")) + "/" + zipCode +"/" + perpage;
			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_ORDER_URL, HttpMethod.GET, null, Cimm2BCentralNearestWareHouseResponse.class);
			
			if(orderResponseEntity!=null && orderResponseEntity.getData() != null && orderResponseEntity.getStatus() != null && orderResponseEntity.getStatus().getCode() == HttpStatus.SC_OK){
				Response = (Cimm2BCentralNearestWareHouseResponse) orderResponseEntity.getData();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return Response;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> arMonths(String customerErpId, String year){
		List<String> months = null;
		String url = CommonDBQuery.getSystemParamtersList().get("AR_MONTHS_LIST_API");
		url += "?" + Cimm2BCentralRequestParams.customerERPId + "=" + customerErpId +"&" + Cimm2BCentralRequestParams.statementYear +"=" + year;
		Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(url, HttpMethod.GET, null, String.class);
		
		if(response!=null && response.getData() != null &&  response.getStatus().getCode() == 200 ) {
			months = (List<String>) response.getData();
		}
		return months;
	}
	
	public static String arSummaryFilePath(String customerErpId, String month, String year) {
		String filePath = null;
		String url = CommonDBQuery.getSystemParamtersList().get("AR_SUMMARY_FILE_PATH");
		Cimm2BCentralARBalanceSummaryRequest request = new Cimm2BCentralARBalanceSummaryRequest();
		request.setCustomerERPId(customerErpId);
		request.setSummaryMonth(month);
		request.setSummaryYear(year);
		
		Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(url, HttpMethod.POST, request, JsonObject.class);
		if(response!=null && response.getData() != null &&  response.getStatus().getCode() == 200 ) {
			JsonObject responseObj = (JsonObject) response.getData();
			if(responseObj != null && responseObj.get("encodedString") != null) {
				filePath = responseObj.get("encodedString").getAsString();
			}
		}
		return filePath;
	}

	public static List<String> arYears(String customerErpId) {
		List<String> years = null;
		String url = CommonDBQuery.getSystemParamtersList().get("AR_YEARS_API");
		if(url != null){
			url += "?" + Cimm2BCentralRequestParams.customerERPId + "=" + customerErpId;
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(url, HttpMethod.GET, null, String.class);
			
			if(response!=null && response.getData() != null &&  response.getStatus().getCode() == 200 ) {
				years = (List<String>) response.getData();
			}
		}
		
		return years;
	}

	public static String getShipToStoreWarehouseCode(String warehouse) {
		String dcNumber = "";
		try{
			String GET_SHIP_TO_STORE_WAREHOUSE_CODE_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_SHIP_TO_STORE_WAREHOUSE_CODE_API")) + warehouse;
			Cimm2BCentralResponseEntity responseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_SHIP_TO_STORE_WAREHOUSE_CODE_API, HttpMethod.GET, null, Cimm2BCentralShipToStoreWarehouseCode.class);
	
			Cimm2BCentralShipToStoreWarehouseCode response = null;
			if(responseEntity!=null && responseEntity.getData() != null && responseEntity.getStatus() != null && responseEntity.getStatus().getCode() == HttpStatus.SC_OK){
				response = (Cimm2BCentralShipToStoreWarehouseCode) responseEntity.getData();
				dcNumber = response.getDcNumber();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return dcNumber;
	}
	
	public static Cimm2BCustomer getCustomer(UsersModel userDetails) {
		HttpSession session = userDetails.getSession();
		SecureData password=new SecureData();
		RestRequest<Void> request = RestRequest.<Void>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY)))
				.withUserSecret(password.validatePassword((String)session.getAttribute("securedPassword")))
				.build();
		
		try {
			Cimm2BCResponse<Cimm2BCustomer> response = CimmESBServiceUtils.getCustomerService().getCustomerByErpId(request, userDetails.getCustomerId());
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.writeValueAsString(response));
			if(response != null && response.getStatus().getCode() == HttpStatus.SC_OK) {
				if(CommonUtility.validateString(response.getData().getDefaultShipVia()).length()>0){
					session.setAttribute("customerDefaultShipVia", response.getData().getDefaultShipVia());
					session.setAttribute("customerDefaultShipViaDesc", response.getData().getShipViaDescription());
				}
				return response.getData();
			}
		} catch (RestClientException | JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String requestAcceptHostedFormToken(LinkedHashMap<String, String> tokenRequest) {
		String token = "";
		Cimm2BCentralAcceptHostedTokenRequest acceptHostedTokenRequest = new Cimm2BCentralAcceptHostedTokenRequest();
		try{
			acceptHostedTokenRequest.setAmount(Double.parseDouble(tokenRequest.get("orderTotal")));
			acceptHostedTokenRequest.setCustomerProfileId(tokenRequest.get("customerProfileId"));
			acceptHostedTokenRequest.setCardHolderFirstName(tokenRequest.get("cardHolderFirstName"));
			acceptHostedTokenRequest.setCardHolderLastName(tokenRequest.get("cardHolderLastName"));
			Cimm2BCentralAddress address = new Cimm2BCentralAddress();
			address.setAddressLine1(tokenRequest.get("billAddress1"));
			address.setAddressLine2(tokenRequest.get("billAddress2"));
			address.setCompanyName(tokenRequest.get("companyName"));
			address.setCity(tokenRequest.get("billCity"));
			address.setState(tokenRequest.get("billState"));
			address.setCountry(tokenRequest.get("billCountry"));
			address.setZipCode(tokenRequest.get("billZipcode"));
			if(!tokenRequest.get("excludeAddress").equalsIgnoreCase("Y")) {
				acceptHostedTokenRequest.setAddress(address);
			}
			Cimm2BCentralAcceptHostedPageSettings hostedPageSettings = new Cimm2BCentralAcceptHostedPageSettings();
			hostedPageSettings.setPageReturnOptions(tokenRequest.get("pageReturnOptions"));
			hostedPageSettings.setPageButtonOptions(tokenRequest.get("pageButtonOptions"));
			hostedPageSettings.setPageStyleOptions(tokenRequest.get("pageStyleOptions"));
			hostedPageSettings.setPagePaymentOptions(tokenRequest.get("pagePaymentOptions"));
			hostedPageSettings.setPageSecurityOptions(tokenRequest.get("pageSecurityOptions"));
			hostedPageSettings.setPageShippingAddressOptions(tokenRequest.get("pageShippingAddressOptions"));
			hostedPageSettings.setPageBillingAddressOptions(tokenRequest.get("pageBillingAddressOptions"));
			hostedPageSettings.setPageCustomerOptions(tokenRequest.get("pageCustomerOptions"));
			hostedPageSettings.setPageOrderOptions(tokenRequest.get("pageOrderOptions"));
			hostedPageSettings.setPageIFrameCommunicatorUrl(tokenRequest.get("pageIFrameCommunicatorUrl"));
			acceptHostedTokenRequest.setHostedPageSettings(hostedPageSettings);
			String ACCEPT_HOSTED_FORM_TOKEN_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ACCEPT_HOSTED_FORM_TOKEN_API"));
			Cimm2BCentralResponseEntity responseEntity = Cimm2BCentralClient.getInstance().getDataObject(ACCEPT_HOSTED_FORM_TOKEN_API, "POST", acceptHostedTokenRequest, String.class);
			if(responseEntity!=null && responseEntity.getData() != null && responseEntity.getStatus() != null &&  responseEntity.getStatus().getCode() == HttpStatus.SC_OK){  
				token =	responseEntity.getData().toString();
			}
			else{
				//to-do set error message and status
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return token;
	}
	public static UsersModel getIframeAccessToken(UsersModel userDetails) {
		UsersModel outputUserDetail = new UsersModel();
		try {
			HttpServletRequest requestSession = ServletActionContext.getRequest();
			HttpSession session = requestSession.getSession();
			SecureData password = new SecureData();
			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY)))
					.withUserSecret(password.validatePassword((String)session.getAttribute("securedPassword"))).build();
	
			Cimm2BCResponse<ExternalTokenResponse> response = CimmESBServiceUtils.getExteranalToken().externalToken(request, userDetails.getUserName(), userDetails.getPassword());
	
			if (response.getData() != null && response.getStatus().getCode() == 200) {
				ExternalTokenResponse externalTokenResponce = response.getData();
				outputUserDetail.setUserToken(externalTokenResponce.getSecureToken());
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return outputUserDetail;
	}
	public static String checkForEmailInERP(UsersModel inputData) throws JsonProcessingException {
		try {
			HttpServletRequest requestSession = ServletActionContext.getRequest();
			HttpSession session = requestSession.getSession();
			SecureData password = new SecureData();
			CimmCustomerRequest requestBody=new CimmCustomerRequest();
			CimmContactRequest contact=new CimmContactRequest();
			contact.setFirstName(inputData.getFirstName());
			contact.setLastName(inputData.getLastName());
			List<CimmContactRequest> contacts=new ArrayList<>();
			contacts.add(contact);
			requestBody.setContacts(contacts);
			requestBody.setUsername(inputData.getUserName());
			if(CommonUtility.validateString(inputData.getNewUserName()).length() > 0){
				requestBody.setNewUsername(inputData.getNewUserName());
			}
				 
		    RestRequest<CimmCustomerRequest> request =  RestRequest.<CimmCustomerRequest>builder()
											    		.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
														.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
														.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY))).withBody(requestBody)
														.withUserSecret(password.validatePassword((String)session.getAttribute("securedPassword")))
														.build();
		  Cimm2BCResponse<CimmCustomerResponse> response = CimmESBServiceUtils.getCustomerService().updateCustomersInfo(request);
		  ObjectMapper mapper = new ObjectMapper(); 
		  System.out.println(mapper.writeValueAsString(response));
			if(response.getData() != null && response.getStatus().getCode() == 200)
			{
				CimmCustomerResponse responseData = response.getData();
				return response.getStatus().getMessage()+"|"+responseData.getCustomerERPId();
			}
	
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	public static String validateEmailAuthcode(String userName, String Authcode) throws RestClientException {
		try {
			String jsonResponse =  "";
			Gson gson = new Gson();
			HttpServletRequest requestSession = ServletActionContext.getRequest();
			HttpSession session = requestSession.getSession();
			SecureData password = new SecureData();
			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY)))
					.withUserSecret(password.validatePassword((String)session.getAttribute("securedPassword"))).build();
			logger.info("@validateEmaiAuthcode-userName: "+userName+":Authcode:"+Authcode);
			Cimm2BCResponse<CimmCustomerResponse> response =  CimmESBServiceUtils.getExteranalToken().validateEmail(request, userName, Authcode);
			if(response.getData() != null && response.getStatus().getCode() == 200)
			{
				CimmCustomerResponse dataOutput= response.getData();
				jsonResponse = gson.toJson(dataOutput);
				return jsonResponse;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	public String updatePassword(UsersModel userInfo,HttpSession session) throws RestClientException, JsonProcessingException {
		try {
			SecureData password=new SecureData();
			ECommCimmUserRequest requestBody = new ECommCimmUserRequest();
				if(CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId")) > 1) {
					userInfo.setBuyingCompanyId(CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId")));
					requestBody.setOldPassword(password.validatePassword(userInfo.getCurrentPassword()));
					}
					requestBody.setPassword(userInfo.getPassword());
			
			RestRequest<ECommCimmUserRequest> request = RestRequest.<ECommCimmUserRequest>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY))).withBody(requestBody)
					.withUserSecret(password.validatePassword((String)session.getAttribute("securedPassword")))
					.build();
			Cimm2BCResponse<ECommUserResponse> response = CimmESBServiceUtils.getUpdateUserService().updateUserInformatiom(request, String.valueOf(userInfo.getBuyingCompanyId()), String.valueOf(userInfo.getUserId()));
			if(response!= null && response.getStatus().getMessage().contains("success") && response.getStatus().getCode() == 200) {
				return userInfo.getPassword();	
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;	 		
		}
	
	public String changeEmail(UsersModel userInfo,HttpSession session) throws RestClientException, JsonProcessingException {
		try {
			SecureData password=new SecureData();
			ECommCimmUserRequest requestBody = new ECommCimmUserRequest();
			
			requestBody.setUsername(userInfo.getUserName());
			if(CommonUtility.validateString(userInfo.getEmailAddress()).length() >0) {
				requestBody.setEmailAddress(userInfo.getEmailAddress());
			}
			
			RestRequest<ECommCimmUserRequest> request = RestRequest.<ECommCimmUserRequest>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY))).withBody(requestBody)
					.withUserSecret(password.validatePassword((String)session.getAttribute("securedPassword")))
					.build();
			Cimm2BCResponse<ECommUserResponse> response = CimmESBServiceUtils.getUpdateUserService().updateUserInformatiom(request, String.valueOf(userInfo.getBuyingCompanyId()), String.valueOf(userInfo.getUserId()));
			if( response.getStatus().getCode() == 200) {
					return response.getStatus().getMessage();
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;	 		
		}

	public Cimm2BCustomer getUserInfoFromErp(UsersModel userInfo) {
		try {
		SecureData password=new SecureData();
		HttpSession session = userInfo.getSession();
		RestRequest<Void> request = RestRequest.<Void>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY))).withCustomerErpId(CommonUtility.validateString(userInfo.getCustomerId()))
				.withUserSecret(password.validatePassword((String)session.getAttribute("securedPassword")))
				.build();
		
		Cimm2BCResponse<Cimm2BCustomer> response = CimmESBServiceUtils.getCustomerService().getCustomerByErpId(request, userInfo.getCustomerId());
		
		if( response.getStatus().getCode() == 200) {
			return response.getData();
	}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
}