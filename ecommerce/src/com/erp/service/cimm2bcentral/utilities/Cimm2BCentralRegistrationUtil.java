package com.erp.service.cimm2bcentral.utilities;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.HttpMethod;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.security.SecureData;
import com.unilog.users.AddressModel;
import com.unilog.users.UserRegisterUtility;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class Cimm2BCentralRegistrationUtil extends SecureData{

	public static String registerUserInDB(AddressModel addressModel, Cimm2BCentralCustomer customerDetails) 
	{
		Connection conn = null;
		CallableStatement stmt = null;
		int buyingCompanyId = 0;
		int userBuyingCompanyId = 0;
		String billEntityId = "0";
		int userId = 0;
		int billAddressId = 0;
		int shipAddressId = 0;
		int parentId =0;
		String entityId = "0";
		String msg = "User Not Registered. Please Contact Customer service.";
		String userEmailAddress = null;
		String result = "";

		try {
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);

		} catch (SQLException e) { 
			e.printStackTrace();
		}
		try
		{
			if(CommonUtility.validateString(addressModel.getUserToken()).length()>0){

				UsersModel userDetailList = new UsersModel();
				UsersModel defaultContact = new UsersModel();
				ArrayList<UsersModel> shippAddressList = new ArrayList<UsersModel>();

				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y"))
				{
					userEmailAddress = addressModel.getEmailAddress();
				}else{
					userEmailAddress = addressModel.getEmailAddress().toLowerCase();
				}
				userDetailList = Cimm2BCentralClient.getInstance().getCustomerInfo(customerDetails);
				if(CommonUtility.validateString(addressModel.getDisableSubmitPOCC()).equalsIgnoreCase("Y")){
					userDetailList.setDisableSubmitPOCC(addressModel.getDisableSubmitPOCC());
				}
				if(CommonUtility.validateString(addressModel.getDisableSubmitPO()).equalsIgnoreCase("Y")){
					userDetailList.setDisableSubmitPO(addressModel.getDisableSubmitPO());
				}
				if(customerDetails.getStatus() != null && CommonUtility.validateString(customerDetails.getStatus().getMessage()).contains("success"))
				{
					entityId = userDetailList.getEntityId();
					billEntityId = userDetailList.getBillEntityId();

					if(!CommonUtility.validateString(billEntityId).equals("0"))
					{
						parentId = UserRegisterUtility.checkEntityId(billEntityId);
						String GET_CUSTOMER_URL = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_CUSTOMER_API")) + "?" + Cimm2BCentralRequestParams.customerERPId + "=" +  billEntityId;
						Cimm2BCentralResponseEntity customerDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(GET_CUSTOMER_URL, HttpMethod.GET, null, Cimm2BCentralCustomer.class);
						customerDetails = (Cimm2BCentralCustomer) customerDetailsResponseEntity.getData();
						userDetailList =  Cimm2BCentralClient.getInstance().getCustomerInfo(customerDetails);
					}
					else
					{
						billEntityId = entityId;
						parentId = UserRegisterUtility.checkEntityId(billEntityId);
					}

					ArrayList<UsersModel> shipToList = new ArrayList<UsersModel>();
					ArrayList<Cimm2BCentralCustomer> customerLocations = customerDetails.getCustomerLocations();
					if(customerLocations != null && customerLocations.size() > 0){
						for(Cimm2BCentralCustomer shipTo : customerLocations){
							UsersModel shipAddressModel = new UsersModel();	

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

							if(shipTo.getContacts() != null && customerDetails.getContacts().size() > 0 && CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
								shipAddressModel.setPhoneNo(customerDetails.getContacts().get(0).getPrimaryPhoneNumber());
							}
							shipAddressModel.setEntityId(CommonUtility.validateString(shipAddress.getAddressERPId()));
							shipToList.add(shipAddressModel);
						}

					}

					if(shipToList!=null && shipToList.size()>0){

						for(UsersModel shipAddress:shipToList)
						{

							int count = UserRegisterUtility.checkEntityId(shipAddress.getEntityId());
							if(count==0){
								shippAddressList.add(shipAddress);
							}
						}
					}

					if(parentId==0)
					{
						parentId = UserRegisterUtility.insertBuyingCompany(conn,userDetailList,0,"");
						userDetailList.setBuyingCompanyId(parentId);
						if(defaultContact.getFirstName()!=null){
							userDetailList.setFirstName(defaultContact.getFirstName());
						}else{
							userDetailList.setFirstName(" ");	
						}
						if(defaultContact.getLastName()!=null){
							userDetailList.setLastName(defaultContact.getLastName());	
						}else{
							userDetailList.setLastName(" ");
						}
						billAddressId = UsersDAO.insertNewAddressintoBCAddressBook(conn, userDetailList, "Bill");
					}
					if(parentId>0)
					{
						conn.commit();
						if(billAddressId==0)
							billAddressId = UserRegisterUtility.getBCAddressBookId(billEntityId, "Bill");
						if(billAddressId==0)
						{
							userDetailList.setBuyingCompanyId(parentId);
							if(defaultContact.getFirstName()!=null)
								userDetailList.setFirstName(defaultContact.getFirstName());
							else
								userDetailList.setFirstName(" ");
							if(defaultContact.getLastName()!=null)
								userDetailList.setLastName(defaultContact.getLastName());
							else
								userDetailList.setLastName(" ");
							billAddressId = UsersDAO.insertNewAddressintoBCAddressBook(conn, userDetailList, "Bill");
						}

						if(shippAddressList!=null && shippAddressList.size()>0)
						{
							for(UsersModel shipAddress:shippAddressList)
							{
								if(userDetailList.getEntityId()!=shipAddress.getEntityId())
								{
									buyingCompanyId = UserRegisterUtility.insertBuyingCompany(conn,shipAddress,parentId,"");
									if(buyingCompanyId == 0)
									{
										conn.rollback();
										return msg;
									}
									else
									{
										conn.commit();
									}
								}
								shipAddress.setBuyingCompanyId(parentId);
								shipAddress.setFirstName(defaultContact.getFirstName());
								shipAddress.setLastName(defaultContact.getLastName());
								int count = UsersDAO.insertNewAddressintoBCAddressBook(conn, shipAddress, "Ship");
								if(shipAddress.getEntityId()==entityId)
									shipAddressId = count;
								if(count == 0)
								{
									conn.rollback();
									return msg;
								}
								else
								{
									conn.commit();
								}
							}
							if(shipAddressId==0)
								shipAddressId = UserRegisterUtility.getBCAddressBookId(entityId, "Ship");

						}
						else
						{
							if(shipAddressId==0)
								shipAddressId = UserRegisterUtility.getBCAddressBookId(entityId, "Ship");

							if(shipAddressId==0)
							{
								userDetailList.setFirstName(defaultContact.getFirstName());
								userDetailList.setLastName(defaultContact.getLastName());
								shipAddressId = UsersDAO.insertNewAddressintoBCAddressBook(conn, userDetailList, "Ship");
							}
							if(shipAddressId>0)
							{
								conn.commit();
							}
							else
							{
								conn.rollback();
								return msg;
							}
						}
					}
					else
					{
						conn.rollback();
						return msg;
					}
					userBuyingCompanyId = UserRegisterUtility.checkEntityId(entityId);
					int parentCompanyId = UserRegisterUtility.checkEntityId(billEntityId);

					if(userBuyingCompanyId >0)
					{
						defaultContact.setEmailAddress(userEmailAddress);
						defaultContact.setPassword(addressModel.getUserPassword());
						defaultContact.setBuyingCompanyId(userBuyingCompanyId);
						defaultContact.setParentId(CommonUtility.validateNumber(addressModel.getParentUserId()));
						defaultContact.setExistingCustomer(addressModel.getExistingUser());
						defaultContact.setUserStatus(addressModel.getUserStatus());
						defaultContact.setTermsType(null);
						defaultContact.setTermsTypeDesc(null);
						defaultContact.setShipViaID(null);
						defaultContact.setShipViaMethod(null);
						userId = UserRegisterUtility.insertNewUser(conn,defaultContact);
					}
					System.out.println("User Id : " + userId);
					if(userId > 0){
						UserRegisterUtility.getEntityAddressByCompanyId(parentCompanyId);
						if(billAddressId>0 && shipAddressId > 0)
						{
							int count = UserRegisterUtility.updateUserAddressBook(conn, billAddressId, shipAddressId, userId);
							if(count > 0){
								result = "User registered successfully.";
								conn.commit();
							}else
							{
								conn.rollback();
								return msg;
							}
						}
						else
						{
							conn.rollback();
							return msg;
						}
					}
					else
					{
						conn.rollback();
						return msg;
					}

					conn.commit(); // remove
					stmt = conn.prepareCall("{call USER_KEYWORD_PROC(?,?)}");
					stmt.setString(1,"BUYINGCOMPANYALL");
					stmt.setInt(2,0);
					stmt.execute();

					ConnectionManager.closeDBStatement(stmt);

					stmt = conn.prepareCall("{call USER_KEYWORD_PROC(?,?)}");
					stmt.setString(1,"USER");
					stmt.setInt(2,userId);
					stmt.execute();
				}
			}
		}
		catch(Exception e)
		{
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			result = msg;
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return result;

	}
}
