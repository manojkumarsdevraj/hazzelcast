package com.erp.service.cimm2bcentral.action;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCreditCardDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalance;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceData;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceSummary;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceSummaryRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAcceptHostedPageSettings;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAcceptHostedTokenRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthenticationRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralContact;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralContracts;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCreditCardDetails;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerData;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerType;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCylinderBalanceSummary;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralGetInvoiceList;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipToStoreWarehouseCode;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralUser;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralNearestWareHouseResponse;
import com.erp.service.model.UserManagementModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.CustomFieldModel;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailModel;
import com.unilog.defaults.SendMailUtility;
import com.unilog.products.ProductsDAO;
import com.unilog.sales.CreditCardModel;
import com.unilog.security.LoginAuthentication;
import com.unilog.security.SecureData;
import com.unilog.users.AddressModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UserLogin;
import com.unilog.users.UserRegisterUtility;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.erp.service.UserManagement;
import com.erp.service.impl.UserManagementImpl;

import WS.Nxtrend.ARGetCustomerDataOrderingResponse;

public class UserManagementAction {
	static final Logger logger = Logger.getLogger(UserManagementAction.class);
	public static String createCommertialCustomer(AddressModel addressModel)
	{

		String result = "0|User Registration failed";
		try
		{
			boolean isValidUser = true;
			String locUser = "N";
			String userRole = null;
			String buyingComanyIdStr = null;
			String parentUserId = "0";
			String custNumber = null;
			UsersModel erpIds = new UsersModel();

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
					billingAddress.setEntityName(addressModel.getCompanyName());
					contactInformation.setEntityName(addressModel.getCompanyName());
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
				billingAddress.setIsTaxable(addressModel.getIsTaxable());


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
				contactInformation.setJobTitle(addressModel.getJobTitle());
				contactInformation.setDisableSubmitPO(addressModel.getDisableSubmitPO());
				contactInformation.setSubsetFlag(addressModel.getSubsetFlag());
				contactInformation.setCompanyName(addressModel.getCompanyName());
				contactInformation.setNewsLetterSub(addressModel.getNewsLetterSub());
				//shippingAddress = new UsersModel();
				shippingAddress = billingAddress;

				if(addressModel.getEntityId()!=null && addressModel.getEntityId().trim().length()>0){
					custNumber = addressModel.getEntityId();
				}else{
					erpIds = createCustomerInERP(billingAddress,shippingAddress, CommonUtility.validateString(addressModel.getUserPassword()));
					custNumber = erpIds.getCustomerId();
					contactInformation.setUserERPId(CommonUtility.validateString(erpIds.getContactId()));
					contactInformation.setShipToId(erpIds.getShipToId());
				}
				if(CommonUtility.validateString(erpIds.getConnectionError()).length()>0){
					result = "connectionError|"+erpIds.getConnectionError();
				}
				else if(custNumber==null || custNumber=="0"){
					result = "0|User Registration Failed. Please Contact Customer Service.";

				}else{
					result = newRetailUserRegistration(custNumber, billingAddress.getCountry().trim(), addressModel.getUserPassword(), locUser, userRole, buyingComanyIdStr, parentUserId,addressModel.getFirstName(),addressModel.getLastName(),addressModel.getUserStatus(), null, true, true, contactInformation);
					if(result!=null){
						String resArr[] = result.split("\\|");
						if(resArr.length>0){
							if(resArr[0].trim().equalsIgnoreCase("0") || result.toLowerCase().contains("successfully")){
								//SendMailUtility.buildNewUser(custNumber, addressModel.getEmailAddress(), addressModel.getUserPassword(),addressModel.getEmailAddress(),addressModel.getFirstName(),addressModel.getLastName());

								//SendMailUtility.buildNewUser(custNumber, addressModel.getEmailAddress(), addressModel.getUserPassword(),addressModel.getEmailAddress(),addressModel.getFirstName(),addressModel.getLastName());
								if(CommonUtility.validateString(custNumber).length()>0){
									addressModel.setEntityId(CommonUtility.validateString(custNumber));
									}
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

		String result = "0|User Registration failed";
		try
		{
			boolean isValidUser = true;
			String locUser = "N";
			String userRole = null;
			String buyingComanyIdStr = null;
			String parentUserId = "0";
			String custNumber = null;
			UsersModel erpIds = new UsersModel();
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
				UsersModel shippingAddress = new UsersModel();
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
				billingAddress.setEntityName(addressModel.getCompanyName()!=null?addressModel.getCompanyName():buyingCompanyId);
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
				billingAddress.setJobTitle(addressModel.getJobTitle());
				billingAddress.setTermsType(addressModel.getTermsType());
				billingAddress.setCreditDate(addressModel.getCreditDate());
				billingAddress.setContactTitle(addressModel.getContactTitle());
				billingAddress.setContactWebsite(addressModel.getContactWebsite());


				contactInformation.setFirstName(addressModel.getFirstName());
				contactInformation.setLastName(addressModel.getLastName());
				contactInformation.setEntityName(addressModel.getCompanyName()!=null?addressModel.getCompanyName():buyingCompanyId);
				contactInformation.setPassword(addressModel.getUserPassword());
				contactInformation.setAddress1(addressModel.getAddress1());
				contactInformation.setAddress2(addressModel.getAddress2());
				contactInformation.setCity(addressModel.getCity());
				contactInformation.setState(addressModel.getState());
				contactInformation.setPobox("");
				contactInformation.setNewsLetterSub(addressModel.getNewsLetterSub());
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
				contactInformation.setCustomerType(addressModel.getCustomerType());
				contactInformation.setJobTitle(addressModel.getJobTitle());
				contactInformation.setSubsetFlag(addressModel.getSubsetFlag());
				contactInformation.setSameAsBillingAddress(addressModel.getCheckSameAsBilling());
				contactInformation.setTermsType(addressModel.getTermsType());
				contactInformation.setIsTaxable(addressModel.getIsTaxable());
				//shippingAddress = new UsersModel();
				if(CommonUtility.validateString(addressModel.getCheckSameAsBilling()).equalsIgnoreCase("No")){	
					//userRegistrationDetail.setCheckSameAsBilling(checkSameAsBilling);
					shippingAddress.setFirstName(addressModel.getShippingAddress().getFirstName());
					shippingAddress.setLastName(addressModel.getShippingAddress().getLastName());
					shippingAddress.setAddress1(addressModel.getShippingAddress().getAddress1());
					shippingAddress.setAddress2(addressModel.getShippingAddress().getAddress2());
					shippingAddress.setCity(addressModel.getShippingAddress().getCity());
					shippingAddress.setState(addressModel.getShippingAddress().getState());
					shippingAddress.setCountry(addressModel.getShippingAddress().getCountry());
					shippingAddress.setEmailAddress(addressModel.getEmailAddress());
					if(CommonUtility.validateString(addressModel.getShippingAddress().getCountry()).equalsIgnoreCase("USA")){
						shippingAddress.setCountry(CommonUtility.getCountryCode(addressModel.getShippingAddress().getCountry(), "Registration"));
					}else{
						shippingAddress.setCountry(addressModel.getShippingAddress().getCountry());
					}
					shippingAddress.setZipCode(addressModel.getShippingAddress().getZipCode());
					shippingAddress.setPhoneNo(addressModel.getShippingAddress().getPhoneNo().replaceAll("[^a-zA-Z0-9]", ""));
					contactInformation.setShippingAddress(addressModel.getShippingAddress());
					
				}
				else {
				shippingAddress = billingAddress;
				}

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
				
					if(CommonUtility.customServiceUtility()!=null) {
						 CommonUtility.customServiceUtility().userRegistrationInformation(addressModel,contactInformation);
					 }
					
				if(addressModel.getEntityId()!=null && addressModel.getEntityId().trim().length()>0){
					custNumber = addressModel.getEntityId();
				}else if(addressModel.isAnonymousUser()==true && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_RETAIL_ERP_REGISTRATION")).equalsIgnoreCase("Y")){
					custNumber = CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID");
					if(CommonUtility.customServiceUtility()!=null){
						CommonUtility.customServiceUtility().getRetailCustomerUserERPId(contactInformation,custNumber);
						custNumber = CommonUtility.customServiceUtility().customizedCustomerNumber(contactInformation,custNumber);
					}
				}else{
					erpIds = createCustomerInERP(billingAddress,shippingAddress, CommonUtility.validateString(addressModel.getUserPassword()));
					if(!CommonUtility.validateString(erpIds.getCustomerId()).equalsIgnoreCase("0") && !(CommonUtility.validateString(erpIds.getConnectionError()).length()>0)) {
						custNumber = CommonUtility.validateString(erpIds.getCustomerId());
						contactInformation.setUserERPId(CommonUtility.validateString(erpIds.getContactId()));
						userInfo.setCustomerERPId(custNumber);
					}else {
						result = erpIds.getConnectionError();
					}
				}
				if(custNumber==null){
						if(result!=null && (result.toLowerCase().contains("invoice") || result.toLowerCase().contains("postal"))){
							result = result.trim().replace("\\n", "");
							 result = "0|"+result;
					}else {
						result = "0|User Registration Failed. Please Contact Customer Service.";
					}

				}else{
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API")).length()>0){
						String CREATE_USER_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API"));
						userInfo.setCustomerERPId(custNumber);
						Cimm2BCentralResponseEntity userDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_USER_API, "POST", userInfo, String.class);
						if(userDetailsResponseEntity!=null && userDetailsResponseEntity.getData() != null &&  userDetailsResponseEntity.getStatus().getCode() == 200 ){  
							userID=	userDetailsResponseEntity.getData().toString();
							contactInformation.setUserERPId(CommonUtility.validateString(userID));
							logger.info("User created:"+userID);
						}else{
							 if(userDetailsResponseEntity.getStatus().getMessage()!=null && (userDetailsResponseEntity.getStatus().getMessage().toLowerCase().contains("invoice") || userDetailsResponseEntity.getStatus().getMessage().toLowerCase().contains("zipcode"))){
								 result = "0|"+userDetailsResponseEntity.getStatus().getMessage();
							 }
						}
					}
					
					
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
								if(addressModel.getCountry().equalsIgnoreCase("USA")){
									userAddress.setCountry(addressModel.getCountry());
								}else{
									userAddress.setCountry("USA"); 
								}
								userAddress.setPhoneNo(addressModel.getPhoneNo());
								userAddress.setEmailAddress(addressModel.getEmailAddress());
								userAddress.setNewsLetterSub(addressModel.getNewsLetterSub()!=null?addressModel.getNewsLetterSub():"N");
								userAddress.setPassword(addressModel.getUserPassword());
								userAddress.setCreditApplicationRequest(addressModel.getCreditApplicationRequest());
								if(CommonUtility.customServiceUtility()!=null) {
									 CommonUtility.customServiceUtility().userRegistrationInformation(addressModel,userAddress);
								 }
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


	public static UsersModel createCustomerInERP(UsersModel billAddressList, UsersModel shipAddressList, String password){

		UsersModel erpIds = new UsersModel();
		Cimm2BCentralCustomer customerInfo=new Cimm2BCentralCustomer();
		Cimm2BCentralCustomer customerLocation=new Cimm2BCentralCustomer();
				
		Cimm2BCentralAddress billToAddress=new Cimm2BCentralAddress();
		Cimm2BCentralAddress shipToAddress=new Cimm2BCentralAddress();
		
		ArrayList<Cimm2BCentralCustomer> customerLocations=new 	ArrayList<Cimm2BCentralCustomer>();
		
		HttpServletRequest request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		try{
			
			//billToAddress.setCustomerName(shipAddressList.getFirstName()+" "+shipAddressList.getLastName());
			if(CommonUtility.validateString(billAddressList.getCustomerName()).length()>0){
				billToAddress.setCustomerName(billAddressList.getCustomerName());//billingAddress.setCustomerName(company);
				customerLocation.setCustomerName(billAddressList.getCustomerName());
			}else{
				billToAddress.setCustomerName(billAddressList.getFirstName()+" "+billAddressList.getLastName());//billingAddress.setCustomerName(company);
				customerLocation.setCustomerName(billAddressList.getFirstName()+" "+billAddressList.getLastName());
			}
			billToAddress.setAddressLine1(billAddressList.getAddress1());
			billToAddress.setAddressLine2(billAddressList.getAddress2());
			billToAddress.setCity(billAddressList.getCity());
			billToAddress.setState(billAddressList.getState());
			billToAddress.setCountry(billAddressList.getCountry());
			billToAddress.setZipCode(billAddressList.getZipCode());
			billToAddress.setCompanyName(billAddressList.getCustomerName());
			billToAddress.setCounty(null);
			
			ArrayList<Cimm2BCentralContact> billContacts=new ArrayList<Cimm2BCentralContact>();
			Cimm2BCentralContact billToContact=new Cimm2BCentralContact();
		
			
			billToContact.setFirstName(billAddressList.getFirstName());
			billToContact.setLastName(billAddressList.getLastName());
			billToContact.setPrimaryEmailAddress(billAddressList.getEmailAddress());
			billToContact.setPrimaryPhoneNumber(billAddressList.getPhoneNo());
			billContacts.add(billToContact);
	
			if(CommonDBQuery.getSequenceId("SHIPPING_ADDRESS_SQU")>0){
			shipToAddress.setAddressERPId(CommonUtility.validateParseIntegerToString(CommonDBQuery.getSequenceId("SHIPPING_ADDRESS_SQU")));
			if(CommonUtility.customServiceUtility() != null) {
				CommonUtility.customServiceUtility().shipAddressERPIdCity(shipToAddress, shipAddressList);
				}
			}
			shipToAddress.setAddressLine1(shipAddressList.getAddress1());
			shipToAddress.setAddressLine2(shipAddressList.getAddress2());
			shipToAddress.setCity(shipAddressList.getCity());
			shipToAddress.setState(shipAddressList.getState());
			shipToAddress.setCountry(shipAddressList.getCountry());
			shipToAddress.setZipCode(shipAddressList.getZipCode());
			shipToAddress.setCounty(null);
			
			
			ArrayList<Cimm2BCentralContact> shipContacts=new ArrayList<Cimm2BCentralContact>();
			
			Cimm2BCentralContact shipToContact=new Cimm2BCentralContact();
			
			shipToContact.setFirstName(shipAddressList.getFirstName());
			shipToContact.setLastName(shipAddressList.getLastName());
			shipToContact.setPrimaryEmailAddress(shipAddressList.getEmailAddress());
			shipToContact.setPrimaryPhoneNumber(shipAddressList.getPhoneNo());
			
			shipContacts.add(shipToContact);
			if(CommonUtility.validateString(shipAddressList.getCustomerName())!=null && CommonUtility.validateString(shipAddressList.getCustomerName()).trim().length()>0){
				customerInfo.setCustomerName(shipAddressList.getCustomerName());	
				
			}else{
				customerInfo.setCustomerName(shipAddressList.getFirstName()+" "+shipAddressList.getLastName());//billingAddress.setCustomerName(company);
			}
			customerLocation.setAddress(shipToAddress);
			customerLocation.setContacts(shipContacts);
			if((CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_ERP_TEMPLATE_OVERRIDE")).length() >0) && (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_ERP_TEMPLATE_OVERRIDE")).equalsIgnoreCase("Y"))) {
				customerLocation.setCustomerTemplateOverride(true);
			   }
			
			if(session != null && session.getAttribute("localeCode")!= null && CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.priceType")).length()>0){
				customerInfo.setPriceType(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.priceType")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.priceType"):"");
			}
			
			if(session != null && session.getAttribute("localeCode")!= null && CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.shipVia")).length()>0){
				customerInfo.setDefaultShipVia(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.shipVia")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.shipVia"):"");
			}
			if(session != null && session.getAttribute("localeCode")!= null && CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.addOnNumber4")).length()>0){
				customerInfo.setAddOnNumber4(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.addOnNumber4")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.addOnNumber4"):"");
			}
			if(session != null && session.getAttribute("localeCode")!= null && CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.addOnNumber2")).length()>0){
				customerInfo.setAddOnNumber2(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.addOnNumber2")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.addOnNumber2"):"");
			}
			if(session != null && session.getAttribute("localeCode")!= null && CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.taxable")).length()>0){
				customerInfo.setTaxable(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.taxable")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("customer.taxable"):"");
			}
			
			customerLocations.add(customerLocation);
			
			if(CommonUtility.validateString(shipAddressList.getCustomerName()).length()>0){
				customerInfo.setCustomerName(shipAddressList.getCustomerName());
			}else{
			customerInfo.setCustomerName(billAddressList.getFirstName()+" "+billAddressList.getLastName());
			}
			customerInfo.setHomeBranch(CommonDBQuery.getSystemParamtersList().get("DEFAULT_BRANCH_ID"));
			customerInfo.setDefaultShipLocationId(CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE_STR"));
			customerInfo.setDefaultBranchId(CommonDBQuery.getSystemParamtersList().get("DEFAULT_BRANCH_ID"));
			customerInfo.setSalesRepId(CommonDBQuery.getSystemParamtersList().get("DEFAULT_SALES_REP_IN"));
			customerInfo.setPrimarySalesRepId(CommonDBQuery.getSystemParamtersList().get("DEFAULT_SALES_REP_IN"));
			customerInfo.setAddress(billToAddress);
			customerInfo.setContacts(billContacts);
			customerInfo.setCustomerLocations(customerLocations);
			customerInfo.setPassword(password);
			customerInfo.setTermsType(billAddressList.getTermsType());
			customerInfo.setDepartment(billAddressList.getJobTitle());
			customerInfo.setCreditDate(billAddressList.getCreditDate());
			customerInfo.setContactTitle(billAddressList.getContactTitle());
			customerInfo.setWebAddress(billAddressList.getContactWebsite());
			if((CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_ERP_TEMPLATE_OVERRIDE")).length() >0) && (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_ERP_TEMPLATE_OVERRIDE")).equalsIgnoreCase("Y"))) {
				customerInfo.setCustomerTemplateOverride(true);
			   }
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP_TEMPLATE_CUSTOMER_CODE")).length() >0) {
				customerInfo.setTemplateCustomerCode(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP_TEMPLATE_CUSTOMER_CODE")));
			   }
		
			
			String CREATE_CUSTOMER_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_CUSTOMER_API")); 
			Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_CUSTOMER_API, "POST", customerInfo, Cimm2BCentralContact.class);
			
			Cimm2BCentralContact orderResponse = null;
			if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  
				orderResponse = (Cimm2BCentralContact)customerDetailsResponseEntity.getData();
				orderResponse.setStatus(customerDetailsResponseEntity.getStatus());
				if(orderResponse != null && CommonUtility.validateString(orderResponse.getCustomerERPId()).length() > 0 || CommonUtility.validateString(orderResponse.getUserERPId()).length() > 0){
					erpIds.setCustomerId(CommonUtility.validateString(orderResponse.getCustomerERPId()));
					erpIds.setContactId(CommonUtility.validateString(orderResponse.getUserERPId()).length()>0?CommonUtility.validateString(orderResponse.getUserERPId()):CommonUtility.validateString(orderResponse.getContactId()));
					erpIds.setUserRole(CommonUtility.validateString(orderResponse.getUserRoleType()));
					if(orderResponse.getCustomerLocations() != null && orderResponse.getCustomerLocations().size() > 0){
						Cimm2BCentralCustomer location = orderResponse.getCustomerLocations().get(0);
						if(location.getAddress() != null){
							erpIds.setShipToId(CommonUtility.validateString(location.getAddress().getAddressERPId()));
						}
					}
				}
				logger.info("customer created:"+CommonUtility.validateString(orderResponse.getCustomerERPId()));
				logger.info("contact created:" +CommonUtility.validateString(orderResponse.getUserERPId()));
				logger.info("contact role:"+ CommonUtility.validateString(orderResponse.getUserRoleType()));
			}else{
				if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getStatus()!=null) {
					erpIds.setConnectionError(customerDetailsResponseEntity.getStatus().getMessage());
				}
				erpIds.setCustomerId("0");
			}
			
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return erpIds;
	}
	
	public static UsersModel contactInquiry(String contactId)
	{
		UsersModel userDetailList = new UsersModel();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try{
			String CONTACT_INQUIRY_API=CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONTACT_INQUIRY_API"))+"?"+Cimm2BCentralRequestParams.userERPId+"="+contactId+"&"+Cimm2BCentralRequestParams.customerERPId+"="+CommonUtility.validateNumber((String) session.getAttribute("loginCustomerERPId"));
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
					userDetailList.setJobTitle(contactResponse.getDepartment());
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
					ArrayList<Cimm2BCentralCreditCardDetails> creditCardDetails = contactResponse.getCreditCard();		
					ArrayList<CreditCardModel> creditCardItems = new ArrayList<CreditCardModel>();
					if(creditCardDetails !=null) {						
					for(Cimm2BCentralCreditCardDetails cardDetails : creditCardDetails){
						CreditCardModel userCardDetails = new CreditCardModel();
						userCardDetails.setCreditCardNumber(cardDetails.getCreditCardNumber());
						userCardDetails.setCreditCardType(cardDetails.getCreditCardType());
						//userCardDetails.setElementPaymentAccountId(cardDetails.getPaymentAccountId());
						userCardDetails.setDate(cardDetails.getExpiryDate());
						userCardDetails.setCardHolder(cardDetails.getCardHolderName());
						userCardDetails.setAddress1(cardDetails.getAddress().getAddressLine1());
						userCardDetails.setZipCode(cardDetails.getAddress().getZipCode());
						userCardDetails.setElementPaymentAccountId(cardDetails.getElementProcessorId());
						userCardDetails.setExpDate(cardDetails.getExpiryDate());
						creditCardItems.add(userCardDetails);
					}
					userDetailList.setCreditCardList(creditCardItems);
				 
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
			HttpServletRequest request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
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
					if( CommonUtility.validateString(customerinfo.getSameAsBillingAddress()).length()>0 && CommonUtility.validateString(customerinfo.getSameAsBillingAddress()).equalsIgnoreCase("No")) {
						customerinfo.setFirstName(customerInfo.getShippingAddress().getFirstName());
						customerinfo.setLastName(customerInfo.getShippingAddress().getLastName());
						customerinfo.setAddress1(customerInfo.getShippingAddress().getAddress1());
						customerinfo.setAddress2(customerInfo.getShippingAddress().getAddress2());
						customerinfo.setCity(customerInfo.getShippingAddress().getCity());
						customerinfo.setState(customerInfo.getShippingAddress().getState());										
						customerinfo.setCountry(customerInfo.getShippingAddress().getCountry());
						customerinfo.setPhoneNo(customerInfo.getShippingAddress().getPhoneNo());						
						customerinfo.setEmailAddress(customerInfo.getShippingAddress().getEmailAddress());						
						customerinfo.setZipCode(customerInfo.getShippingAddress().getZipCode());		
						shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Ship",true);
					}
					else {
					shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Ship",true);
					}
				}else{
					UsersModel uModel =  UsersDAO.getDefaultShipNbill(customerNumber);
					if(uModel!=null){
						defaultBillId = CommonUtility.validateNumber(uModel.getBillToId());
						shipId = CommonUtility.validateNumber(uModel.getShipToId());
					}
					if(defaultBillId == 0 || shipId == 0){
						defaultBillId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Bill",true); 	
						if( CommonUtility.validateString(customerinfo.getSameAsBillingAddress()).length()>0 && CommonUtility.validateString(customerinfo.getSameAsBillingAddress()).equalsIgnoreCase("No")) {
							customerinfo.setFirstName(customerInfo.getShippingAddress().getFirstName());
							customerinfo.setLastName(customerInfo.getShippingAddress().getLastName());
							customerinfo.setAddress1(customerInfo.getShippingAddress().getAddress1());
							customerinfo.setAddress2(customerInfo.getShippingAddress().getAddress2());
							customerinfo.setCity(customerInfo.getShippingAddress().getCity());
							customerinfo.setState(customerInfo.getShippingAddress().getState());										
							customerinfo.setCountry(customerInfo.getShippingAddress().getCountry());
							customerinfo.setPhoneNo(customerInfo.getShippingAddress().getPhoneNo());						
							customerinfo.setEmailAddress(customerInfo.getShippingAddress().getEmailAddress());						
							customerinfo.setZipCode(customerInfo.getShippingAddress().getZipCode());		
							shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Ship",true);
						}
						else {
						shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Ship",true);
						}
									
					}
				}


				//resultVal = "0|"+customerInfo.getEmailAddress()+" has been successfully registered"; 
				resultVal = "1|"+customerInfo.getEmailAddress()+" has been successfully registered|"+userId;       
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

				if(CommonUtility.customServiceUtility()!=null) { 
						String warehouse = "";
						Cookie[] cookie_jar = request.getCookies();
						if (cookie_jar != null){
							for (int i =0; i< cookie_jar.length; i++){
								Cookie aCookie = cookie_jar[i];
								if(aCookie!=null && aCookie.getName()!=null && aCookie.getName().trim().equalsIgnoreCase("beforeloginbrach")){
									warehouse = aCookie.getValue();
								}
							}	
					CommonUtility.customServiceUtility().insertCustomertarget(warehouse,userId,buyingCompanyid); 
				} 
					CommonUtility.customServiceUtility().insertCustomFields(warehouse, userId, buyingCompanyid, customerInfo);
			}//CustomServiceProvider
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
		long startTimer = CommonUtility.startTimeDispaly();
		HashMap<String, UsersModel> result = null;
		try{
			result = UsersDAO.getUserAddressFromBCAddressBook(billId, shipId);
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return result;
	}

	public String editBillingAddress(UsersModel userInfo){
		int count =-1;
		String result = "Billing Address Update Failed";
		Connection conn = null;
		try{
			HttpSession session = userInfo.getSession();
			HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId((String) session.getAttribute(Global.USERNAME_KEY),"Y");
			String firstName=userDetails.get("firstName");
			String lastName=userDetails.get("lastName");
			String phoneNumber=userDetails.get("userOfficePhone");
			String email= userDetails.get("userEmailAddress");
			
			String custNumber = (String) session.getAttribute("customerId");
			Cimm2BCentralCustomer customerInfo=new Cimm2BCentralCustomer();
			Cimm2BCentralAddress customerAddress=new Cimm2BCentralAddress();
			
			customerAddress.setCompanyName("");
			customerAddress.setCustomerName(userInfo.getCustomerName());
			customerAddress.setAddressLine1(userInfo.getAddress1());
			customerAddress.setAddressLine2(userInfo.getAddress2());
			customerAddress.setCity(userInfo.getCity());
			/**
			 * Below code is written for Electrozad to change country name
			 */
			if(CommonUtility.customServiceUtility() != null) {
				String countryName=CommonUtility.customServiceUtility().setCountryName(userInfo);//Electrozad Custom Service
				if(countryName!=null) {
					userInfo.setCountry(countryName);
				}
			}
			customerAddress.setCountry(userInfo.getCountry());
			customerAddress.setState(userInfo.getState());	
			customerAddress.setZipCode(userInfo.getZipCode());
			
			ArrayList<Cimm2BCentralContact> customerContacts=new ArrayList<Cimm2BCentralContact>();
			
			Cimm2BCentralContact customerContact=new Cimm2BCentralContact();
			customerContact.setFirstName(firstName);
			customerContact.setLastName(lastName);
			customerContact.setPrimaryPhoneNumber(userInfo.getPhoneNo()!=null?userInfo.getPhoneNo():phoneNumber);
			customerContact.setFaxNumber("");
			customerContact.setPrimaryEmailAddress(email);
			customerContacts.add(customerContact);
			
			
			customerInfo.setCustomerERPId(custNumber);
			customerInfo.setCustomerName(userInfo.getCustomerName());
			customerInfo.setAddress(customerAddress);
			customerInfo.setContacts(customerContacts);
			String UPDATE_CUSTOMER_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPDATE_CUSTOMER_API"));
			Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(UPDATE_CUSTOMER_API, "PUT", customerInfo, String.class);
			
			if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 &&  customerDetailsResponseEntity.getData().equals(custNumber)){  
				/**
				 * Below code is written for Electrozad to change country name
				 */
				if(CommonUtility.customServiceUtility() != null) {
					String country=CommonUtility.customServiceUtility().setCountryName(userInfo);//Electrozad Custom Service
					if(country!=null) {
						userInfo.setCountry(country);
					}
				}
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
			String custNumber = (String) session.getAttribute("customerId");
			Cimm2BCentralCustomer shipToInfo=new Cimm2BCentralCustomer();
			Cimm2BCentralAddress shipToAddress=new Cimm2BCentralAddress();
			shipToAddress.setAddressId(CommonUtility.validateParseIntegerToString(shipInfo.getAddressBookId()));
			if(CommonDBQuery.getSequenceId("SHIPPING_ADDRESS_SQU")>0){
				shipToAddress.setAddressERPId(CommonUtility.validateParseIntegerToString(CommonDBQuery.getSequenceId("SHIPPING_ADDRESS_SQU")));
			}else{
				shipToAddress.setAddressERPId(shipInfo.getShipEntityId());
			}
			shipToAddress.setAddressLine1(shipInfo.getAddress1());
			shipToAddress.setAddressLine2(shipInfo.getAddress2());
			shipToAddress.setCity(shipInfo.getCity());
			shipToAddress.setState(shipInfo.getState());
			shipToAddress.setCountry(shipInfo.getCountry());
			shipToAddress.setZipCode(shipInfo.getZipCode());
			shipToAddress.setCompanyName(shipInfo.getCompanyName());
			shipToAddress.setAddressType(shipInfo.getAddressType());
			
			ArrayList<Cimm2BCentralContact> shipContacts=new ArrayList<Cimm2BCentralContact>();
			
			Cimm2BCentralContact shipToContact=new Cimm2BCentralContact();
			
			shipToContact.setFirstName(shipInfo.getFirstName());
			shipToContact.setLastName(shipInfo.getLastName());
			shipToContact.setPrimaryEmailAddress(shipInfo.getEmailAddress());
			shipToContact.setPrimaryPhoneNumber(shipInfo.getPhoneNo());
			shipToContact.setContactId(shipInfo.getContactId());
			shipContacts.add(shipToContact);
		
			shipToInfo.setAddress(shipToAddress);
			shipToInfo.setContacts(shipContacts);
			shipToInfo.setCustomerPoNumber(shipInfo.getPoNumber());
			shipToInfo.setCustomerERPId(custNumber);
			shipToInfo.setCustomerName(shipInfo.getFirstName()+" "+shipInfo.getLastName());
			ArrayList<Cimm2BCentralCustomer> customerLocations=new ArrayList<Cimm2BCentralCustomer>();
			Cimm2BCentralCustomer cLocation = new Cimm2BCentralCustomer();
			cLocation.setAddress(shipToAddress);
			cLocation.setCustomerName(shipInfo.getFirstName()+" "+shipInfo.getLastName());
			customerLocations.add(cLocation);
			shipToInfo.setCustomerLocations(customerLocations);
			if((CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_ERP_TEMPLATE_OVERRIDE")).length() >0) && (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_ERP_TEMPLATE_OVERRIDE")).equalsIgnoreCase("Y"))) {
				shipToInfo.setCustomerTemplateOverride(true);
			   }
		
			
			String CREATE_NEWSHIPTO_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_NEWSHIPTO_API")); 
			Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_NEWSHIPTO_API, "POST", shipToInfo, String.class);
			
	
			if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  
	
				shipToID = customerDetailsResponseEntity.getData().toString();
				shipInfo.setShipToId(shipToID);
			}
			userId = shipInfo.getUserId();
			int defaultShipId = 0;
			conn = ConnectionManager.getDBConnection();
			defaultShipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, shipInfo,"Ship",false);
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
		long startTimer = CommonUtility.startTimeDispaly();
		UsersModel userDetailList = new UsersModel();
		Cimm2BCentralARBalanceData arBalanceDataRequest=null;
		String url = "";
		Cimm2BCentralResponseEntity arBalanceSummaryEntity = null;
		try{
			ArrayList<Cimm2BCentralARBalanceSummary> arBalanceSummary = new ArrayList<Cimm2BCentralARBalanceSummary>();
			List<Cimm2BCentralARBalanceSummary> arBalanceSummaryList = new ArrayList<Cimm2BCentralARBalanceSummary>();
			Cimm2BCentralARBalanceSummaryRequest arBalanceSummaryRequest = new Cimm2BCentralARBalanceSummaryRequest();
			arBalanceSummaryRequest.setCustomerERPId(accountInputValue.get("entityId"));
			if(CommonUtility.validateString(accountInputValue.get("asOfDate")).length()>0)
				arBalanceSummaryRequest.setAsOfDate(accountInputValue.get("asOfDate"));
			if(CommonUtility.validateString(accountInputValue.get("searchString")).length()>0)
				arBalanceSummaryRequest.setTextSearch(accountInputValue.get("searchString"));
			if(CommonUtility.validateString(accountInputValue.get("additionalInfo")).equalsIgnoreCase("Y"))
				arBalanceSummaryRequest.setIncludeARBalanceAdditionalInfo(true);
			
			url = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ACCOUNT_RECEIVABLE_BALANCE_SUMMARY"));
			arBalanceSummaryEntity = Cimm2BCentralClient.getInstance().getDataObject(url, HttpMethod.POST, arBalanceSummaryRequest, Cimm2BCentralARBalanceData.class);
	
			if(arBalanceSummaryEntity!=null && arBalanceSummaryEntity.getData() != null && arBalanceSummaryEntity.getStatus() != null && arBalanceSummaryEntity.getStatus().getCode() == HttpStatus.SC_OK){
				Cimm2BCentralARBalanceData arBalanceDataResponse = null;
				arBalanceDataResponse = (Cimm2BCentralARBalanceData) arBalanceSummaryEntity.getData();
				if(arBalanceDataResponse!=null){
					userDetailList.setBalance(arBalanceDataResponse.getBalance()!=null?arBalanceDataResponse.getBalance():0.0);
					userDetailList.setPeriod1(arBalanceDataResponse.getPeriod1()!=null?arBalanceDataResponse.getPeriod1():0.0);
					userDetailList.setPeriod2(arBalanceDataResponse.getPeriod2()!=null?arBalanceDataResponse.getPeriod2():0.0);
					userDetailList.setPeriod3(arBalanceDataResponse.getPeriod3()!=null?arBalanceDataResponse.getPeriod3():0.0);
					userDetailList.setPeriod4(arBalanceDataResponse.getPeriod4()!=null?arBalanceDataResponse.getPeriod4():0.0);
					userDetailList.setPeriod5(arBalanceDataResponse.getPeriod5()!=null?arBalanceDataResponse.getPeriod5():0.0);
					userDetailList.setPeriod6(arBalanceDataResponse.getPeriod6()!=null?arBalanceDataResponse.getPeriod6():0.0);
					userDetailList.setPeriod7(arBalanceDataResponse.getPeriod7()!=null?arBalanceDataResponse.getPeriod7():0.0);
					userDetailList.setOpenOrderBalance(arBalanceDataResponse.getOpenOrderBalance()!=null?arBalanceDataResponse.getOpenOrderBalance():0.0);
					userDetailList.setFutureBalance(arBalanceDataResponse.getFutureBalance()!=null?arBalanceDataResponse.getFutureBalance():0.0);
					userDetailList.setSalesMTD(arBalanceDataResponse.getSalesMTD()!=null?arBalanceDataResponse.getSalesMTD():00);
					userDetailList.setSalesYTD(arBalanceDataResponse.getSalesYTD()!=null?arBalanceDataResponse.getSalesYTD():0.0);
					userDetailList.setLastSaleDate(arBalanceDataResponse.getLastSaleDate()!=null?arBalanceDataResponse.getLastSaleDate():"");
					userDetailList.setLastPaymentDate(arBalanceDataResponse.getLastPaymentDate()!=null?arBalanceDataResponse.getLastPaymentDate():"");
					userDetailList.setTermsAndCondition(arBalanceDataResponse.getTermsAndCondition()!=null?arBalanceDataResponse.getTermsAndCondition():"");
					userDetailList.setTotal(arBalanceDataResponse.getTotal()!=null?arBalanceDataResponse.getTotal():0.0);
					userDetailList.setSixMonthHigh(arBalanceDataResponse.getSixMonthHigh()!=null?arBalanceDataResponse.getSixMonthHigh():0.0);
					userDetailList.setSixMonthAverage(arBalanceDataResponse.getSixMonthAverage()!=null?arBalanceDataResponse.getSixMonthAverage():0);
					userDetailList.setArCreditLimit(arBalanceDataResponse.getArCreditLimit()!=null?arBalanceDataResponse.getArCreditLimit():0);
					userDetailList.setArCreditAvail(arBalanceDataResponse.getArCreditAvail()!=null?arBalanceDataResponse.getArCreditAvail():0);
					userDetailList.setPaymentDays(arBalanceDataResponse.getPaymentDays()!=null?arBalanceDataResponse.getPaymentDays():0);
					userDetailList.setLastSaleAmount(arBalanceDataResponse.getLastSaleAmount()!=null?arBalanceDataResponse.getLastSaleAmount():0);
					userDetailList.setLastPaymentAmount(arBalanceDataResponse.getLastPaymentAmount()!=null?arBalanceDataResponse.getLastPaymentAmount():0);
					userDetailList.setaRTotal(arBalanceDataResponse.getArTotal()!=null?arBalanceDataResponse.getArTotal():0);
					userDetailList.setEndDate(arBalanceDataResponse.getEndDate()!=null?arBalanceDataResponse.getEndDate():"");
					userDetailList.setPaymentDate(arBalanceDataResponse.getLastPaymentDate());
					userDetailList.setDateOfFirstSale(arBalanceDataResponse.getDateOfFirstSale()!=null?arBalanceDataResponse.getDateOfFirstSale():"");
					arBalanceSummary = (ArrayList<Cimm2BCentralARBalanceSummary>) arBalanceDataResponse.getArBalanceSummary();
					if(arBalanceSummary!=null){
						for(Cimm2BCentralARBalanceSummary cimm2bcARBalanceSummary :arBalanceSummary ){
							Cimm2BCentralARBalanceSummary arBalanceSummaryDetails = new Cimm2BCentralARBalanceSummary();
							arBalanceSummaryDetails.setLastSaleDate(cimm2bcARBalanceSummary.getLastSaleDate());
							arBalanceSummaryDetails.setAccountId(cimm2bcARBalanceSummary.getAccountId());
							arBalanceSummaryDetails.setCustomerPONumber(cimm2bcARBalanceSummary.getCustomerPONumber());
							arBalanceSummaryDetails.setLastSaleAmount(cimm2bcARBalanceSummary.getLastSaleAmount());
							arBalanceSummaryDetails.setLastPaymentAmount(cimm2bcARBalanceSummary.getLastPaymentAmount());
							arBalanceSummaryDetails.setBalance(cimm2bcARBalanceSummary.getBalance());
							arBalanceSummaryDetails.setAge(cimm2bcARBalanceSummary.getAge());
							arBalanceSummaryList.add(arBalanceSummaryDetails);
						}
					}
					userDetailList.setArBalanceSummaryList(arBalanceSummaryList);
				}
			}
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
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

	@SuppressWarnings("null")
	public static String contactUpdate(UsersModel userInfo,HttpSession session) {
		String message = "failed";
		String userName = userInfo.getUserName();
		ArrayList<CreditCardModel> creditCardsList = userInfo.getCreditCardList();
		HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId(userName,"Y");
	    String password = userInfo.getPassword();
		ArrayList<Cimm2BCentralCreditCardDetails> creditCardDetailsList = new ArrayList<Cimm2BCentralCreditCardDetails>();
		try{
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONTACT_UPDATE_SUBMIT")).length()>0) {
			Cimm2BCentralCustomer customerInfo=new Cimm2BCentralCustomer();
					
			Cimm2BCentralAddress userContactAddress=new Cimm2BCentralAddress();
			Cimm2BCentralAddress creditCardAddress=new Cimm2BCentralAddress();
			
			if(creditCardsList !=null && creditCardsList.size()>0 ){
			for(CreditCardModel creditCardList:creditCardsList){
				Cimm2BCentralCreditCardDetails creditCardDetails=new Cimm2BCentralCreditCardDetails();
				creditCardDetails.setCardHolderName(CommonUtility.validateString(creditCardList.getCardHolder()));
				creditCardDetails.setCreditCardNumber(CommonUtility.validateString(creditCardList.getCreditCardNumber()));
				creditCardDetails.setCreditCardType(CommonUtility.validateString(creditCardList.getCreditCardType()));
				creditCardDetails.setExpiryDate(CommonUtility.validateString(creditCardList.getDate()));
				creditCardDetails.setPaymentAccountId(CommonUtility.validateString(creditCardList.getElementPaymentAccountId()));
					
				creditCardAddress.setAddressLine1(CommonUtility.validateString(creditCardList.getAddress1()));
				creditCardAddress.setZipCode(CommonUtility.validateString(creditCardList.getZipCode()));
				creditCardDetails.setAddress(creditCardAddress);
				
				creditCardDetails.setCustomerERPId(CommonUtility.validateString(userDetails.get("contactId")));
				creditCardDetailsList.add(creditCardDetails);
				}
			}
			userContactAddress.setAddressLine1(CommonUtility.validateString(userDetails.get("address")));
			userContactAddress.setCity(CommonUtility.validateString(userDetails.get("city")));
			userContactAddress.setState(CommonUtility.validateString(userDetails.get("state")));
			userContactAddress.setZipCode(CommonUtility.validateString(userDetails.get("zip")));
			userContactAddress.setCounty(CommonUtility.validateString(userDetails.get("customerCountry")));
			
			ArrayList<Cimm2BCentralContact> userContacts=new ArrayList<Cimm2BCentralContact>();
			Cimm2BCentralContact userContact=new Cimm2BCentralContact();
		
			
			userContact.setFirstName(CommonUtility.validateString(userDetails.get("firstName")));
			userContact.setLastName(CommonUtility.validateString(userDetails.get("lastName")));
			userContact.setPrimaryEmailAddress(CommonUtility.validateString(userDetails.get("userEmailAddress")));
			userContact.setPrimaryPhoneNumber(CommonUtility.validateString(userDetails.get("userOfficePhone")));
			userContacts.add(userContact);
			
			
			customerInfo.setUserERPId(CommonUtility.validateString(userDetails.get("userErpId")));
			customerInfo.setCustomerERPId(CommonUtility.validateString(userDetails.get("contactId")));
			customerInfo.setCustomerName(CommonUtility.validateString(userDetails.get("loginCustomerName")));
			customerInfo.setUserName(userInfo.getUserName());
			customerInfo.setAddress(userContactAddress);
			customerInfo.setContacts(userContacts);
			customerInfo.setCreditCard(creditCardDetailsList);
			customerInfo.setPassword(password);
			customerInfo.setDepartment(userInfo.getJobTitle());
			//customerInfo.setClassificationId("eCommerce");
		
			
			String CONTACT_UPDATE_SUBMIT = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONTACT_UPDATE_SUBMIT"));
			Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CONTACT_UPDATE_SUBMIT, "PUT", customerInfo, Cimm2BCentralContact.class);
			
			Cimm2BCentralContact orderResponse = null;
			if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  
				/*orderResponse = (Cimm2BCentralContact)customerDetailsResponseEntity.getData();
				orderResponse.setStatus(customerDetailsResponseEntity.getStatus());*/
				message = "successfully";
			}
			return message;
		}else if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RESET_PASSWORD_API")).length()>0){
			
			Cimm2BCentralAuthenticationRequest requestObj = new Cimm2BCentralAuthenticationRequest();
			requestObj.setUsername(userName);
			SecureData validUserPass = new SecureData();
			requestObj.setPassword(validUserPass.validatePassword(userInfo.getCurrentPassword()));
			requestObj.setNewPassword(password);
			Cimm2BCentralContact contact=new Cimm2BCentralContact();
			contact.setPrimaryEmailAddress(userName);
			requestObj.setContact(contact);

			String RESET_PASSWORD_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RESET_PASSWORD_API"));
			Cimm2BCentralResponseEntity responseEntity = Cimm2BCentralClient.getInstance().getDataObject(RESET_PASSWORD_API, HttpMethod.POST, requestObj, Cimm2BCentralContact.class);
			
			if(responseEntity!=null && responseEntity.getData() != null &&  responseEntity.getStatus().getCode() == 200 ){  
				message = responseEntity.getData().toString();
			}
		}
		else {
			message = userInfo.getPassword();
		}
		}catch(Exception e){
			e.printStackTrace();
			}
		return message;
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

	public static UsersModel contactEdit(UsersModel customerInfoInput,UsersModel addressDetail) {

		UsersModel customerInfoOutput =new UsersModel();;
		HttpSession session =  customerInfoInput.getSession();
		String pass = null;
		String result = "";
		ArrayList<CreditCardModel> creditCardsList = new ArrayList<CreditCardModel>(); 		
		 if(customerInfoInput.getCreditCardList() !=null && customerInfoInput.getCreditCardList().size()>0) {
			 creditCardsList =  customerInfoInput.getCreditCardList();
		 }
		ArrayList<Cimm2BCentralCreditCardDetails> creditCardDetailsList = new ArrayList<Cimm2BCentralCreditCardDetails>();
			try 
	 		{
				Cimm2BCentralCustomer customerInfo=new Cimm2BCentralCustomer();
				HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId(customerInfoInput.getUserName(),"Y");
				addressDetail.setUserName(customerInfoInput.getUserName());
				addressDetail.setUserStatus("Y");
				addressDetail.setJobTitle(customerInfoInput.getJobTitle());
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
				customerInfo.setUserName(customerInfoInput.getUserName());
				customerInfo.setDepartment(customerInfoInput.getJobTitle());
				Cimm2BCentralAddress creditCardAddress=new Cimm2BCentralAddress();			
				if(creditCardsList!=null && creditCardsList.size()>0) {
				for(CreditCardModel creditCardList:creditCardsList){
					Cimm2BCentralCreditCardDetails creditCardDetails=new Cimm2BCentralCreditCardDetails();
					creditCardDetails.setCardHolderName(CommonUtility.validateString(creditCardList.getCardHolder()));
					creditCardDetails.setCreditCardNumber(CommonUtility.validateString(creditCardList.getCreditCardNumber()));
					creditCardDetails.setCreditCardType(CommonUtility.validateString(creditCardList.getCreditCardType()));
					creditCardDetails.setExpiryDate(CommonUtility.validateString(creditCardList.getDate()));
					creditCardDetails.setPaymentAccountId(CommonUtility.validateString(creditCardList.getElementPaymentAccountId()));
						
					creditCardAddress.setAddressLine1(CommonUtility.validateString(creditCardList.getAddress1()));
					creditCardAddress.setZipCode(CommonUtility.validateString(creditCardList.getZipCode()));
					creditCardDetails.setAddress(creditCardAddress);
					
					creditCardDetails.setCustomerERPId(CommonUtility.validateString(userDetails.get("contactId")));
					creditCardDetailsList.add(creditCardDetails);
				}
				customerInfo.setCreditCard(creditCardDetailsList);
				}
				
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPDATE_USER_API")).length()>0) {
					String UPDATE_USER_URL=CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPDATE_USER_API"));
					Cimm2BCentralResponseEntity userDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(UPDATE_USER_URL, HttpMethod.PUT, customerInfo, Cimm2BCentralCustomer.class);	   
					
					if(userDetailsResponseEntity!=null && userDetailsResponseEntity.getData() != null &&  userDetailsResponseEntity.getStatus().getCode() == 200 && userDetailsResponseEntity.getData().equals(userERPId)){
						result = userDetailsResponseEntity.getStatus().getMessage();
						customerInfoOutput = addressDetail;
						customerInfoOutput.setStatusDescription(result);
						logger.info(result);
					}else{
						logger.info("0|Update Failed. Please Try Again.");
					}
				}else {
					customerInfoOutput = addressDetail;
					result = "Update successful";
					customerInfoOutput.setStatusDescription("successful");
				}
				
				if(customerInfoOutput!=null && customerInfoOutput.getStatusDescription()!=null && customerInfoOutput.getStatusDescription().trim().contains("successful"))
				{
	 				String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
	 				int userId = CommonUtility.validateNumber(sessionUserId);
	 				addressDetail.setChangedPassword(pass);
	 				addressDetail.setStatusDescription(result);//addressDetail.setStatusDescription("1|"+result);
	 				addressDetail.setUserStatus("Y");
					int count = UsersDAO.updateUserData(userId,addressDetail);
					
					 if(count==1)
					 {
						 logger.info("Sending mail...........");
						 customerInfoOutput.setResult("1|"+addressDetail.getStatusDescription());
					 }
					 else
					 {
						 customerInfoOutput.setResult("0|Update Failed. Please Try Again.");
					 }
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
		long startTimer = CommonUtility.startTimeDispaly();
		HashMap<String, ArrayList<UsersModel>> shipList = null;
		try{
			shipList = UsersDAO.getAgentAddressListFromBCAddressBook(customerInfoInput.getUserId(),"");
		}catch (Exception e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return shipList;
	}

	public static String newOnAccountUserRegistration(UsersModel userDetailsInput){

		String result = "";
		Connection conn=null;
		String userNumber=null;
		try {
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);
			UsersModel customerinfo = new UsersModel();
			UsersModel userdetails = new  UsersModel();
			int defaultBillId = 0;
			String country = CommonUtility.validateString(userDetailsInput.getCountry());
			String account = CommonUtility.validateString(userDetailsInput.getAccountName());
			String firstName = CommonUtility.validateString(userDetailsInput.getFirstName());
			String lastName = CommonUtility.validateString(userDetailsInput.getLastName());
			String password = CommonUtility.validateString(userDetailsInput.getPassword());
			String emailId = CommonUtility.validateString(userDetailsInput.getEmailAddress());
			String phoneNo = CommonUtility.validateString(userDetailsInput.getPhoneNo());
			int buyingCompanyid = 0;
			String CompanyName=null;
			boolean userExist = false;
			boolean validInvoice = false;
			String invoiceNo = CommonUtility.validateString(userDetailsInput.getInvoiceNumber());
			String poNumber = userDetailsInput.getPoNumber()!=null?userDetailsInput.getPoNumber():"";
			String zipCode = CommonUtility.validateString(userDetailsInput.getZipCode());
			ArrayList<AddressModel> shipToList = new ArrayList<AddressModel>();
			buyingCompanyid = UsersDAO.getBuyingCompanyIdByEntityId(account);
			boolean invoiceRequired = false;
			
				String GET_CUSTOMER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  account;
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("INVOICE_REQUIRED")).equalsIgnoreCase("Y")){
					 invoiceRequired = true;
					 GET_CUSTOMER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  account+"&invoiceRequired=true";
				}else{
					 GET_CUSTOMER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  account;
					 validInvoice = true;
				}
				Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_CUSTOMER_URL, HttpMethod.GET, null, Cimm2BCentralCustomer.class);
			
			
			Cimm2BCentralCustomer customerDetails = null;
			boolean continuereg = true;
			
			if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  

				customerDetails = (Cimm2BCentralCustomer) customerDetailsResponseEntity.getData();
				if(CommonUtility.customServiceUtility()!=null) {
					continuereg = CommonUtility.customServiceUtility().setCustomerType(customerDetails, continuereg);
				}
				
				if(customerDetails != null && continuereg){
					String entityid="0";
					String billEntityId = "0";
					
					if(CommonUtility.customServiceUtility()!=null) {
						CommonUtility.customServiceUtility().setCustomerHomeBranch(customerDetails);
					}
					String warehouse = customerDetails.getHomeBranch()!=null?customerDetails.getHomeBranch():"";
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
						//------- Remove after clarification
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
						//------- Remove after clarification
					}

					if(userDetailsInput!=null){
						customerinfo.setEmailAddress(CommonUtility.validateString(userDetailsInput.getEmailAddress()));
					}
					customerinfo.setTermsType(customerDetails.getTermsType());
					customerinfo.setTermsTypeDesc(customerDetails.getTermsTypeDescription());
					customerinfo.setJobTitle(userDetailsInput.getJobTitle());
					if(CommonUtility.validateString(customerDetails.getSubsetName()).length()>0) {
						customerinfo.setSubsetId(ProductsDAO.getSubsetIdFromName(customerDetails.getSubsetName()));
					}
					//customerinfo.setWareHouseCode(UsersDAO.getCustomerWareHouseID(warehouse.replaceFirst("^0+(?!$)", "")));
					//customerinfo.setWareHouseCodeStr(warehouse.replaceFirst("^0+(?!$)", ""));
					
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
					customerinfo.setJobTitle(userDetailsInput.getJobTitle());
					customerinfo.setSubsetFlag(userDetailsInput.getSubsetFlag());
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
	
								if(shipTo.getContacts() != null && shipTo.getContacts().size() > 0 && CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
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
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API")).length()>0 && (userDetailsInput.getSession().getAttribute("loginUserERPId")==null)){
						userdetails = createContactInERP(customerinfo);
						userNumber=CommonUtility.validateParseIntegerToString(userdetails.getUserId());
						}else if(CommonUtility.validateString(userDetailsInput.getErpLoginId()).length()>0) {
							userNumber = userDetailsInput.getErpLoginId();
						}
						else {
							userNumber=userDetailsInput.getSession().getAttribute("loginUserERPId")!=null?userDetailsInput.getSession().getAttribute("loginUserERPId").toString():"-1";
							
						}
					 if(!CommonUtility.validateString(userNumber).isEmpty() && !CommonUtility.validateString(userNumber).equals("0"))
					 {
						customerinfo.setEntityId(entityid);
						boolean changeUserRole = false;
						int userId =0;	   
						boolean insertAddress = false;
						boolean mail = false;
						if(buyingCompanyid==0){
							buyingCompanyid = UsersDAO.insertBuyingCompany(conn,customerinfo,0);
							insertAddress = true;
							changeUserRole = true;
						}
						UsersModel userDetailList = null;
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONTACT_INQUIRY_API")).length()>0){
							 userDetailList = new UsersModel();
							if(userdetails.getUserId() != 0){
								userDetailList=contactInquiry(Integer.toString(userdetails.getUserId()));
							}else {
								userDetailList=contactInquiry(userNumber);
							}
						
						
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
							if(userDetailList!=null) {
							userDetailsInput.setFirstName(userDetailList.getFirstName()!=null?userDetailList.getFirstName():"");
							userDetailsInput.setLastName(userDetailList.getLastName()!=null?userDetailList.getLastName():"");
							userDetailsInput.setPhoneNo(userDetailList.getPhoneNo()!=null?userDetailList.getPhoneNo():"");
							userDetailsInput.setAddress1(userDetailList.getAddress1()!=null?userDetailList.getAddress1():"");
							userDetailsInput.setAddress2(userDetailList.getAddress2()!=null?userDetailList.getAddress2():"");
							userDetailsInput.setCity(userDetailList.getCity()!=null?userDetailList.getCity():"");
							userDetailsInput.setState(userDetailList.getState()!=null?userDetailList.getState():"");
							userDetailsInput.setZipCode(userDetailList.getZipCode()!=null?userDetailList.getZipCode():"");
							userDetailsInput.setCountry(userDetailList.getCountry()!=null?userDetailList.getCountry():"US");
							userDetailsInput.setJobTitle(userDetailList.getJobTitle());
							userDetailsInput.setUserName(emailId);
							userDetailsInput.setEmailAddress(userDetailList.getEmailAddress()!=null?userDetailList.getEmailAddress():emailId);
							}
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
							//defaultBillId = UsersDAO.insertNewAddress(conn, customerinfo, userId, "Bill");

							if(insertAddress){
								defaultBillId = UsersDAO.insertNewAddressintoBCAddressBook(conn,customerinfo, "Bill", false);
								UsersModel shipaddressList = new UsersModel();
								if(shipToList.size()>0){
									for(AddressModel getShipList : shipToList){
										shipaddressList = new UsersModel();
										shipaddressList.setFirstName(getShipList.getCompanyName());
										shipaddressList.setLastName(getShipList.getLastName()!=null?getShipList.getLastName():"");
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
										if(CommonUtility.customServiceUtility()!=null) {
											CommonUtility.customServiceUtility().setDefaultWareHouseFromERP(shipaddressList,customerDetails);
										}
										shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn,shipaddressList,"Ship",false);
									}
								}else{
									//shipId = UsersDAO.insertNewAddress(conn, customerinfo, userId, "Ship");
									if(CommonUtility.customServiceUtility()!=null){
										shipId = CommonUtility.customServiceUtility().setBillAddressToShippAddress(conn,customerinfo,shipId);
									}
									if(shipId==0) {
										shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn,customerinfo,"Ship",false);
									}
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
							if(userRole == null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_EXISTING_USER_ROLE")).length()>0){
								userRole =  CommonDBQuery.getSystemParamtersList().get("DEFAULT_EXISTING_USER_ROLE");
								UsersDAO.assignRoleToUser(conn,Integer.toString(userId),userRole);
							}
							
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
							//conn.commit();

							result = "0|"+emailId+" has been successfully registered";

							//-------------------------
							ArrayList<UsersModel> superUserStatus = null;
							String buyCompStr = ""+buyingCompanyid;
							superUserStatus = UsersDAO.getSuperUserForCompany(buyCompStr);

							userDetailsInput.setEntityName(customerDetails.getCustomerName());
							userDetailsInput.setEntityId(CommonUtility.validateString(account));
							conn.commit();
							if(changeUserRole && CommonUtility.customServiceUtility()!=null) {
								CommonUtility.customServiceUtility().updateRoleForFirstUser(userId,userDetailsInput); //Geary Pacific
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

							if(CommonDBQuery.getSystemParamtersList().get("EXISTING_USER_AUTO_LOGIN")!=null && CommonDBQuery.getSystemParamtersList().get("EXISTING_USER_AUTO_LOGIN").trim().equalsIgnoreCase("Y")){
								LoginAuthentication getWebUser = new LoginAuthentication();
								getWebUser.authenticate(emailId, password, userDetailsInput.getSession());//,ipaddress
								userDetailsInput.getSession().setAttribute("userRegMessage", "true");
							}
							if(userDetailsInput.getWoeLogin()!=null && userDetailsInput.getWoeLogin().equalsIgnoreCase("Y")){
								UsersModel userInfo = new UsersModel();
								UserManagement usersObj = new UserManagementImpl();
								HttpServletRequest request = ServletActionContext.getRequest();
								HttpSession session = request.getSession();
								if(userDetailsInput.getWoePassword()!=null && !userDetailsInput.getWoePassword().equalsIgnoreCase("")) {
									userInfo.setUserName(userDetailsInput.getWoeUserName());
									userInfo.setPassword(userDetailsInput.getWoePassword());
									userInfo.setIsCreditCard("No");
									result	= usersObj.contactUpdate(userInfo,session);
									count = UsersDAO.updatePassword(userId, userDetailsInput.getWoePassword());	
									System.out.println("WOE password updated succesfully"+count);
								}
								
							}
							
							/* Narco & PSS specific code
							String codCustomFiledValue = "N";
							String poRequiredCustomFiledValue = "N";
							String gasPoRequiredCustomFiledValue = "N";
							
							String fuelSurcharge="N";
							double minimumOrderAmount=0.0;
							double prepaidFreightAmount=0.0;
							String defaultShipVia=CommonUtility.validateString(customerDetails.getDefaultShipVia());
							String priceListPath="";
							String fullPriceListPath="";
							String newsLetterCustomFiledValue="";
							
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
							if(userDetailsInput.getNewsLetterSub()!=null && userDetailsInput.getNewsLetterSub().equals("Y")){
								newsLetterCustomFiledValue="Y";
							}else{
								newsLetterCustomFiledValue="N";
							}
							*/
							/*Please use the services to insert custom field data
							 * 
							UsersDAO.insertCustomField(CommonUtility.validateString(codCustomFiledValue), "COD", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(poRequiredCustomFiledValue), "PO_REQUIRED", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(gasPoRequiredCustomFiledValue), "GAS_PO_REQUIRED", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(defaultShipVia), "SHIPPING_METHOD", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(fuelSurcharge), "FUEL_SURCHARGE", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateParseDoubleToString(minimumOrderAmount), "MINIMUM_ORDER_AMOUNT", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateParseDoubleToString(prepaidFreightAmount), "PREPAID_FREIGHT_TARGET_AMOUNT", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(priceListPath), "MY_PRICING_LINK_ONE", userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(fullPriceListPath), "MY_PRICING_LINK_TWO",userId, buyingCompanyid, "BUYING_COMPANY");
							UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue), "NEWSLETTER",userId, buyingCompanyid, "USER");
							*/
							if(CommonUtility.customServiceUtility()!=null) { 
								CommonUtility.customServiceUtility().insertCustomertarget(warehouse,userId,buyingCompanyid);
								CommonUtility.customServiceUtility().insertCustomFields(warehouse, userId, buyingCompanyid, userDetailsInput);
							} //CustomServiceProvider
							
							 if(result!=null && result.toLowerCase().contains("successfully")){
								 SendMailUtility sendMail = new SendMailUtility();
								 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_MAIL_TO_CUSTOMER")).equalsIgnoreCase("Y")){
		   							  mail = sendMail.sendRegistrationMail(userDetailsInput,"customer","","1B"); 
		   					 } 
							 }
							
						}else{
							result = "Error While Registering. Contact our Customer Service for Further Assistance.|";
						}
					}else {
						result = userdetails.getLoginMessage();
					}
				}
				}else{
					result="Invalid Invoice, Zip code and PO Number. Please add valid invoice number or zip code or PO Number.";
				}
				}
				else if(!continuereg) {
					result="restrictAccountRegistration";
				}
				else{
					result="Invalid Invoice or invoice does not match in the ERP. Please add the recent invoice.";
				}
			}else{
				if(CommonUtility.customServiceUtility()!=null) {
					result =  CommonUtility.customServiceUtility().sendmailWithAccountNumber(userDetailsInput);
				}
				if(CommonUtility.validateString(result).length()==0){
					result = customerDetailsResponseEntity.getStatus().getMessage();
				}
				
				logger.info(result);
			}
		}catch (Exception e) {
			result = "Error While Registering. Contact our Customer Service for Further Assistance.|";
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
			int userId = CommonUtility.validateNumber(sessionUserId);
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
				
					String GET_CUSTOMER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  customerNumber;
					Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_CUSTOMER_URL, HttpMethod.GET, null, Cimm2BCentralCustomer.class);
				
				Cimm2BCentralCustomer customerDetails = new Cimm2BCentralCustomer();
				boolean webCurrency = false;

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
						if(CommonUtility.customServiceUtility() != null) {
							CommonUtility.customServiceUtility().checkWebCurrency(customerDetails,webCurrency);//Electrozad Custom Service
							CommonUtility.customServiceUtility().setDefaultWareHouseFromERP(customerinfo, customerDetails);
						}
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
								if(shipTo.getContacts() != null && shipTo.getContacts().size() > 0 && CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
									shipAddressModel.setPhoneNo(shipTo.getContacts().get(0).getPrimaryPhoneNumber());
								}
								if(shipTo.getContacts() != null && shipTo.getContacts().size() > 0 && CommonUtility.validateString(shipTo.getContacts().get(0).getPrimaryEmailAddress()).length() > 0){
									shipAddressModel.setEmailAddress(shipTo.getContacts().get(0).getPrimaryEmailAddress());
								}
								if(CommonUtility.validateString(shipTo.getHomeBranch()).trim().length() > 0){
									shipAddressModel.setWareHouseCode(shipTo.getHomeBranch());
								}
								if(CommonUtility.customServiceUtility() != null) {
									CommonUtility.customServiceUtility().setCurrencyCodeDetails(shipTo,shipAddressModel);//Electrozad Custom Service
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

					if(CommonUtility.customServiceUtility() != null) {
						CommonUtility.customServiceUtility().deleteBCAddressBookDetailsFromBCAddressBookCustomTable(conn,session);//Electrozad Custom Service
					}
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
								shipaddressList.setEmailAddress(getShipList.getEmailAddress());
								shipaddressList.setEntityId(contactId);
								shipaddressList.setZipCode(getShipList.getZipCode());
								shipaddressList.setBuyingCompanyId(buyingCompanyId);
								shipaddressList.setWareHouseCodeStr(getShipList.getWareHouseCode());
								shipaddressList.setWareHouseName(getShipList.getWareHouseName());
								shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn,shipaddressList,"Ship",false);
                                if(CommonUtility.validateString(customerDetails.getDefaultShipLocationId()).length()>0 && assignDefaultShipTo){
                                    if(getShipList.getShipToId().equalsIgnoreCase(customerDetails.getDefaultShipLocationId())){
                                     defaultShipToId=shipId;
                                     assignDefaultShipTo=false;
                                    }
                                }
                                if(CommonUtility.customServiceUtility() != null) {
									CommonUtility.customServiceUtility().insertBCAddressBookCustomFields(getShipList,userId,shipId);//Electrozad Custom Service																																																		
								}
							}
						}else{
							shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn,customerinfo,"Ship",false);
							if(CommonUtility.customServiceUtility() != null) {
								CommonUtility.customServiceUtility().insertDefaultValueToBCAddressBookCustomFields(webCurrency,userId,shipId);//Electrozad Custom Service
							}
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
				if(CommonUtility.validateString(priceListPath).length()>0) {
				UsersDAO.insertCustomField(CommonUtility.validateString(priceListPath), "MY_PRICING_LINK_ONE", CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				}
				if(CommonUtility.validateString(fullPriceListPath).length()>0) {
				UsersDAO.insertCustomField(CommonUtility.validateString(fullPriceListPath), "MY_PRICING_LINK_TWO",CommonUtility.validateNumber(sessionUserId), buyingCompanyId, "BUYING_COMPANY");
				}
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
		try {
			HttpSession session = userModel.getSession();
			session.removeAttribute("customerDetails");
			String customerNumber = userModel.getCustomerId();
			
				String GET_CUSTOMER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  customerNumber + "&" + Cimm2BCentralRequestParams.shipToListRequired + "="+true;
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_CONTRACTS_REQUIRED")).equalsIgnoreCase("Y")) {
					GET_CUSTOMER_URL = GET_CUSTOMER_URL + "&" + Cimm2BCentralRequestParams.contractsListRequired + "="+true;
				}
				Cimm2BCentralResponseEntity	customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObjectWithSession(GET_CUSTOMER_URL, HttpMethod.GET, null, Cimm2BCentralCustomer.class, session);
			
			Cimm2BCentralCustomer customerDetails = null;

			if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  

				customerDetails = (Cimm2BCentralCustomer) customerDetailsResponseEntity.getData();
				Cimm2BCentralCustomerType customerType = customerDetails.getCustomerType();
				if (customerType != null && customerType.getType() != null && (customerType.getType().equalsIgnoreCase("B"))) {
					session.setAttribute("containSubAcc", "N");
				}
				userModel.setPaymentTerm(customerDetails.getPaymentTerm());
					if(customerDetails!=null && customerDetails.getCustomerERPId()!=null) {
						if(customerDetails.getCustomerERPId()!=null) {
							session.setAttribute("customerDetails", customerDetails);
							session.setAttribute("customerDefaultShipVia", CommonUtility.validateString(customerDetails.getDefaultShipVia()!=null?customerDetails.getDefaultShipVia():""));
						}
						if(CommonUtility.validateString(customerDetails.getHomeBranch()).length()>0) {
							if(CommonUtility.customServiceUtility()!=null) {
								CommonUtility.customServiceUtility().setCustomerHomeBranch(customerDetails);
							}
							session.setAttribute("wareHouseCode", CommonUtility.validateString(customerDetails.getHomeBranch()));
						}
					}
					
					if(CommonUtility.validateString(userModel.getShipToWarehouseCode()).length()>0) {
						session.setAttribute("wareHouseCode",  CommonUtility.validateString(userModel.getShipToWarehouseCode()));//To override warehouse code with shipToWareousecode
					}
					if(CommonUtility.customServiceUtility()!=null && customerDetails!=null && customerDetails.getContracts() != null) {
						CommonUtility.customServiceUtility().setContractIdToSession(customerDetails, session);
					}
					if(CommonUtility.customServiceUtility()!=null && customerDetails!=null) {
						CommonUtility.customServiceUtility().setCurrencyCodeToSession(customerDetails, session);
					}
					//Code to override the warhouse details for customer who has the network warehouse mapping
					
					  if(session!=null && CommonUtility.validateString((String)session.getAttribute("wareHouseCode")).length() > 0){
			        	    String networkWarehouse = null;
			        	  //CustomServiceProvider
			        	    if(CommonUtility.customServiceUtility()!=null) {
			        	    	networkWarehouse= CommonUtility.customServiceUtility().getNetworkWarehouseCode(CommonUtility.validateString(session.getAttribute("wareHouseCode").toString()));
							}
			        	  //CustomServiceProvider
				  			if(networkWarehouse != null){
				  				session.setAttribute("wareHouseCode",CommonUtility.validateString(networkWarehouse).trim());
				  			}
			          }
				}
				
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return "";
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
		UsersModel userdetail = new UsersModel();
		String userNumber = null;
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
				contactInfo.setContactTitle(userInfo.getContactTitle());
				contactInfo.setContactWebsite(userInfo.getContactWebsite());
			    buyingComanyIdStr=userInfo.getBuyingComanyIdStr();
			    userdetail = createContactInERP(contactInfo);
				userId = userdetail.getUserId();
				if(CommonUtility.customServiceUtility()!=null) {
				String setUserId = CommonUtility.customServiceUtility().userIdSetUp();
				if(setUserId!= null) {userNumber = setUserId;}
				}
				if(CommonUtility.customServiceUtility()!=null) {
				String setUserId = CommonUtility.customServiceUtility().userIdSetUp();
				if(setUserId!= null) {userNumber = setUserId;}
				}
				if(userId>0){
					contactInfo.setUserERPId(CommonUtility.validateParseIntegerToString(userId)!=null?CommonUtility.validateParseIntegerToString(userId):"");
				result = newRetailUserRegistration(userInfo.getEntityId() , userInfo.getCountry(), userInfo.getUserPassword(), locUser,userInfo.getRole(), buyingComanyIdStr, userInfo.getParentUserId(),userInfo.getFirstName(),userInfo.getLastName(),userInfo.getUserStatus(), null, true, true, contactInfo);
				String[] resultData = result.split("\\|");
				if(resultData.length >= 3){
					userId = Integer.parseInt(resultData[2]);
				}else{
					userId = 0;
				}
				
				}else {
				result = userdetail.getLoginMessage();
			}
			}else {
				 userId = UserRegisterUtility.registerInforSXUsertoDB(userInfo);
			}
			if(userId>0)
			{
					conn = ConnectionManager.getDBConnection();
					ArrayList<Integer> approverId = new ArrayList<Integer>();
					approverId.add(CommonUtility.validateNumber(userInfo.getParentUserId()));
					//UsersDAO.assignApprover(userId, approverId, userId);
					if(userInfo.getRole()!=null && userInfo.getRole().trim().length()>0){
						roleName = userInfo.getRole().trim();
					}
					//UsersDAO.assignRoleToUser(conn,Integer.toString(userId), roleName);
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API")).length()<=0) {
						UsersDAO.assignRoleToUser(conn,Integer.toString(userId), roleName);
					}
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
					userManagementModel.setTotalInvoiceAmount(balanceDetail.getSalesTotal());
					userManagementModel.setDiscountDate(balanceDetail.getDocumentDiscountDate());
					userManagementModel.setCustomerPurchaseOrder(balanceDetail.getCustomerPONumber());
					//--Norco
					userManagementModel.setInvoiceNumber(balanceDetail.getInvoiceNumber());
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
					userManagementModel.setTotal1Balance(balanceDetail.getBalance());
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
	public static UsersModel createContactInERP(UsersModel usersInfo){
		
		UsersModel userdetails = new  UsersModel();
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
			userInfo.setDepartment(usersInfo.getJobTitle());
			userInfo.setContactTitle(usersInfo.getContactTitle());
			userInfo.setContactWebsite(usersInfo.getContactWebsite());
		
			String CREATE_USER_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API"));
			Cimm2BCentralResponseEntity userDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CREATE_USER_API, "POST", userInfo, String.class);
			if(userDetailsResponseEntity!=null && userDetailsResponseEntity.getData() != null &&  userDetailsResponseEntity.getStatus().getCode() == 200 ){  
				userID =	userDetailsResponseEntity.getData().toString();
				userdetails.setUserId(Integer.parseInt(userID));
				logger.info("User created:"+userID);
			}
			else{
				userID= "";
				userdetails.setLoginMessage(userDetailsResponseEntity.getStatus().getMessage());
				logger.info("User not created:"+userID);
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return userdetails;
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
	
public static ArrayList<UsersModel> ElementSetupRequest(UsersModel userDet) {
		
		ArrayList<UsersModel> userDetailList = new ArrayList<UsersModel>();
		Cimm2BCentralCreditCardDetails userCredentials = new Cimm2BCentralCreditCardDetails();
		userCredentials.setUserName(userDet.getUserName());
		userCredentials.setPassword(userDet.getPassword());
		String elementSetupId = userDet.getElementSetupId();
		try 
		{
			String GET_CUSTOMER_CARDS = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_CARDS")) + "?" + Cimm2BCentralRequestParams.elementSetupId + "=" + elementSetupId;
			Cimm2BCentralResponseEntity orderResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_CUSTOMER_CARDS, "GET", userCredentials, Cimm2BCentralCreditCardDetails.class);
			
			List<Cimm2BCentralCreditCardDetails> cimm2BCentralCreditCardResponse = null;
			if(orderResponseEntity!=null && orderResponseEntity.getData() != null && orderResponseEntity.getStatus().getCode() == 200){
				cimm2BCentralCreditCardResponse =  (List<Cimm2BCentralCreditCardDetails>) orderResponseEntity.getData();
				if(cimm2BCentralCreditCardResponse != null){
					for(Cimm2BCentralCreditCardDetails cardDetails : cimm2BCentralCreditCardResponse){
						UsersModel userCardDetails = new UsersModel();
						userCardDetails.setCreditCardNumber(cardDetails.getCreditCardNumber());
						userCardDetails.setCreditCardType(cardDetails.getCreditCardType());
						userCardDetails.setElementPaymentAccountId(cardDetails.getPaymentAccountId());
						userCardDetails.setDate(cardDetails.getExpiryDate());
						userCardDetails.setCardHolder(cardDetails.getCardHolderName());
						userCardDetails.setAddress1(cardDetails.getAddress().getAddressLine1());
						userCardDetails.setZipCode(cardDetails.getAddress().getZipCode());
						userCardDetails.setElementSetupId(elementSetupId);
						userDetailList.add(userCardDetails);
					}
				}
			}	
		}catch (Exception ex) {
	          ex.printStackTrace();
	    }
	    
	    return userDetailList;
	}
	
public static String creditCardUpdate(String sessionId,String userName, ArrayList<CreditCardModel> creditCardList) {
	String message = "";
	HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId(userName,"Y"); 
	SecureData validUserPass = new SecureData();
    String password = userDetails.get("password");
	String newPassword=validUserPass.validatePassword(password);
	try{
		Cimm2BCentralCustomer customerInfo=new Cimm2BCentralCustomer();
				
		Cimm2BCentralAddress userContactAddress=new Cimm2BCentralAddress();
		Cimm2BCentralAddress creditCardAddress=new Cimm2BCentralAddress();
		ArrayList<Cimm2BCentralCreditCardDetails> creditCardDetailsList = new ArrayList<Cimm2BCentralCreditCardDetails>();
		
		for(CreditCardModel creditCardListdetails:creditCardList){
		Cimm2BCentralCreditCardDetails creditCardDetails=new Cimm2BCentralCreditCardDetails();
		creditCardDetails.setCustomerERPId(CommonUtility.validateString(userDetails.get("billingEntityId")));
		creditCardDetails.setCardHolderName(CommonUtility.validateString(creditCardListdetails.getCardHolder()));
		creditCardDetails.setCreditCardNumber(CommonUtility.validateString(creditCardListdetails.getCreditCardNumber()));
		creditCardDetails.setCreditCardType(CommonUtility.validateString(creditCardListdetails.getCreditCardType()));
		creditCardDetails.setExpiryDate(CommonUtility.validateString(creditCardListdetails.getDate()));
		creditCardDetails.setPaymentAccountId(CommonUtility.validateString(creditCardListdetails.getElementPaymentAccountId()));
		
		creditCardAddress.setAddressLine1(CommonUtility.validateString(creditCardListdetails.getAddress1()));
		creditCardAddress.setZipCode(CommonUtility.validateString(creditCardListdetails.getZipCode()));
		
		creditCardDetails.setAddress(creditCardAddress);
		creditCardDetailsList.add(creditCardDetails);
		}
		userContactAddress.setAddressLine1(CommonUtility.validateString(userDetails.get("address1")));
		userContactAddress.setAddressLine2(CommonUtility.validateString(userDetails.get("address2")));
		userContactAddress.setCity(CommonUtility.validateString(userDetails.get("city")));
		userContactAddress.setState(CommonUtility.validateString(userDetails.get("state")));
		userContactAddress.setZipCode(CommonUtility.validateString(userDetails.get("zip")));
		userContactAddress.setCounty(CommonUtility.validateString(userDetails.get("customerCountry")!=null ? userDetails.get("customerCountry"):"US"));
		
		ArrayList<Cimm2BCentralContact> userContacts=new ArrayList<Cimm2BCentralContact>();
		Cimm2BCentralContact userContact=new Cimm2BCentralContact();
	
		
		userContact.setFirstName(CommonUtility.validateString(userDetails.get("firstName")));
		userContact.setLastName(CommonUtility.validateString(userDetails.get("lastName")));
		userContact.setPrimaryEmailAddress(CommonUtility.validateString(userDetails.get("userEmailAddress")));
		userContact.setPrimaryPhoneNumber(CommonUtility.validateString(userDetails.get("userOfficePhone")));
		userContacts.add(userContact);
		
		customerInfo.setUserName(userDetails.get("userName"));
		customerInfo.setUserERPId(CommonUtility.validateString(userDetails.get("userErpId")));
		customerInfo.setCustomerERPId(CommonUtility.validateString(userDetails.get("billingEntityId")));
		customerInfo.setCustomerName(CommonUtility.validateString(userDetails.get("userName")));
		customerInfo.setAddress(userContactAddress);
		customerInfo.setContacts(userContacts);
		customerInfo.setCreditCard(creditCardDetailsList);
		customerInfo.setPassword(newPassword);
		//customerInfo.setClassificationId("eCommerce");
	
		
		String CONTACT_UPDATE_SUBMIT = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONTACT_UPDATE_SUBMIT"));
		Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CONTACT_UPDATE_SUBMIT, "PUT", customerInfo, Cimm2BCentralContact.class);
		
		Cimm2BCentralContact orderResponse = null;
		if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  
			/*orderResponse = (Cimm2BCentralContact)customerDetailsResponseEntity.getData();
			orderResponse.setStatus(customerDetailsResponseEntity.getStatus());*/
			message = "successfully";
		}
		return message;
	}catch(Exception e){
		e.printStackTrace();
		}
	return message;
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
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return token;
	}
	
	public static Cimm2BCentralCustomerData searchCustomersInERP(UsersModel customerSerachRequest) {
		String token = "";
		Cimm2BCentralCustomer serachRequest = new Cimm2BCentralCustomer();
		Cimm2BCentralCustomerData customerData = null;
		try{
			serachRequest.setCustomerERPId(customerSerachRequest.getCustomerId());
			serachRequest.setDescription(customerSerachRequest.getDescription());
			serachRequest.setCustomerName(customerSerachRequest.getCustomerName());
			serachRequest.setCustomerTypeFlag(customerSerachRequest.isCustomerAccountExist());
			Cimm2BCentralAddress address = new Cimm2BCentralAddress();
			address.setCity(customerSerachRequest.getCity());
			address.setState(customerSerachRequest.getState());
			address.setZipCode(customerSerachRequest.getZipCode());
			serachRequest.setAddress(address);
			
			ArrayList<Cimm2BCentralContact> contacts = new ArrayList<Cimm2BCentralContact>();
			Cimm2BCentralContact contact = new Cimm2BCentralContact();
			contact.setPrimaryPhoneNumber(customerSerachRequest.getPhoneNo());
			contacts.add(contact);
			serachRequest.setContacts(contacts);
			
			String CUSTOMER_SEARCH_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SEARCH_API"));
			Cimm2BCentralResponseEntity responseEntity = Cimm2BCentralClient.getInstance().getDataObject(CUSTOMER_SEARCH_API, "POST", serachRequest, Cimm2BCentralCustomerData.class);
			
			if(responseEntity!=null && responseEntity.getData() != null && responseEntity.getStatus().getCode() == 200){
				customerData = (Cimm2BCentralCustomerData) responseEntity.getData();
			}
	} catch (Exception e) {
		logger.error(e.getMessage());
		e.printStackTrace();
	}
	return customerData;
}
	
	@SuppressWarnings("null")
	public static String woeContactUpdate(UsersModel userInfo,HttpSession session) {
		String message = "failed";
		String userName = userInfo.getUserName();
		ArrayList<CreditCardModel> creditCardsList = userInfo.getCreditCardList();
		ArrayList<Cimm2BCentralCreditCardDetails> creditCardDetailsList = new ArrayList<Cimm2BCentralCreditCardDetails>();
		try{
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONTACT_UPDATE_SUBMIT")).length()>0) {
			Cimm2BCentralCustomer customerInfo=new Cimm2BCentralCustomer();
					
			Cimm2BCentralAddress userContactAddress=new Cimm2BCentralAddress();
			Cimm2BCentralAddress creditCardAddress=new Cimm2BCentralAddress();
			userContactAddress.setAddressLine1(CommonUtility.validateString(userInfo.getAddress1()));
			userContactAddress.setCity(CommonUtility.validateString(userInfo.getCity()));
			userContactAddress.setState(CommonUtility.validateString(userInfo.getState()));
			userContactAddress.setZipCode(CommonUtility.validateString(userInfo.getZipCode()));
			userContactAddress.setCounty(CommonUtility.validateString(userInfo.getCountry()));
			
			ArrayList<Cimm2BCentralContact> userContacts=new ArrayList<Cimm2BCentralContact>();
			Cimm2BCentralContact userContact=new Cimm2BCentralContact();
		
			
			userContact.setFirstName(CommonUtility.validateString(userInfo.getFirstName()));
			userContact.setLastName(CommonUtility.validateString(userInfo.getLastName()));
			userContact.setPrimaryEmailAddress(CommonUtility.validateString(userInfo.getEmailAddress()));
			userContact.setPrimaryPhoneNumber(CommonUtility.validateString(userInfo.getPhoneNo()));
			userContacts.add(userContact);
			
			
			customerInfo.setUserERPId(CommonUtility.validateString(userInfo.getContactId()));
			customerInfo.setCustomerERPId(CommonUtility.validateString(userInfo.getEntityId()));
			//customerInfo.setCustomerName(CommonUtility.validateString(userInfo.getFirstName()+""+userInfo.getLastName()));
			customerInfo.setUserName(userInfo.getWoeERPUserName());
			customerInfo.setAddress(userContactAddress);
			customerInfo.setContacts(userContacts);
			customerInfo.setCreditCard(creditCardDetailsList);
			customerInfo.setPassword(userInfo.getPassword());
			customerInfo.setDepartment(userInfo.getJobTitle());
			//customerInfo.setClassificationId("eCommerce");
		
			
			String CONTACT_UPDATE_SUBMIT = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CONTACT_UPDATE_SUBMIT"));
			Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(CONTACT_UPDATE_SUBMIT, "PUT", customerInfo, Cimm2BCentralContact.class);
			
			Cimm2BCentralContact orderResponse = null;
			if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  
				message = "1|successfully";
			}
			
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return message;
	}
}

