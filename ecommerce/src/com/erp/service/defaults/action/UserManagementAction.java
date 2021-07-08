package com.erp.service.defaults.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.model.UserManagementModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.CustomFieldModel;
import com.unilog.defaults.SendMailModel;
import com.unilog.defaults.SendMailUtility;
import com.unilog.users.AddressModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UserRegisterUtility;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;

public class UserManagementAction {
	
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
			  billingAddress.setEntityName(buyingCompanyId); // 
			  billingAddress.setAddress1(addressModel.getAddress1());
			  billingAddress.setAddress2(addressModel.getAddress2());
			  billingAddress.setCity(addressModel.getCity());
			  billingAddress.setState(addressModel.getState());
			  billingAddress.setPobox("");
			  if(CommonUtility.validateString(addressModel.getCountry()).equalsIgnoreCase("USA")){
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
			  
			  
			  contactInformation.setFirstName(addressModel.getFirstName());
			  contactInformation.setLastName(addressModel.getLastName());
			  contactInformation.setEntityName(buyingCompanyId);
			  contactInformation.setPassword(addressModel.getUserPassword());
			  contactInformation.setAddress1(addressModel.getAddress1());
			  contactInformation.setAddress2(addressModel.getAddress2());
			  contactInformation.setCity(addressModel.getCity());
			  contactInformation.setState(addressModel.getState());
			  contactInformation.setPobox("");
			  if(CommonUtility.validateString(addressModel.getCountry()).equalsIgnoreCase("USA")){
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
			 
			  
			  if(CommonUtility.customServiceUtility()!=null) {
				  CommonUtility.customServiceUtility().contactInfo(addressModel, billingAddress, contactInformation, shippingAddress);
			  }

			  
			  if(CommonUtility.validateString(addressModel.getCheckSameAsBilling()).equalsIgnoreCase("No")){
				  shippingAddress.setFirstName(addressModel.getShippingAddress().getFirstName()!=null?addressModel.getShippingAddress().getFirstName():"");
				  shippingAddress.setLastName(addressModel.getShippingAddress().getLastName()!=null?addressModel.getShippingAddress().getLastName():"");
				  shippingAddress.setAddress1(addressModel.getShippingAddress().getAddress1()!=null?addressModel.getShippingAddress().getAddress1():"");
				  shippingAddress.setAddress2(addressModel.getShippingAddress().getAddress2()!=null?addressModel.getShippingAddress().getAddress2():"");
				  shippingAddress.setCity(addressModel.getShippingAddress().getCity()!=null?addressModel.getShippingAddress().getCity():"");
				  shippingAddress.setState(addressModel.getShippingAddress().getState()!=null?addressModel.getShippingAddress().getState():"");
				  shippingAddress.setCountry(addressModel.getShippingAddress().getCountry()!=null?addressModel.getShippingAddress().getCountry():"");
				  shippingAddress.setCompanyName(addressModel.getShippingAddress().getCompanyName()!=null?addressModel.getShippingAddress().getCompanyName():"");
				  shippingAddress.setShipToName(addressModel.getShippingAddress().getCompanyName()!=null?addressModel.getShippingAddress().getCompanyName():"");
				  shippingAddress.setPhoneNo(addressModel.getShippingAddress().getPhoneNo()!=null?addressModel.getShippingAddress().getPhoneNo():"");
				  shippingAddress.setZipCode(addressModel.getShippingAddress().getZipCode()!=null?addressModel.getShippingAddress().getZipCode():"");
				  shippingAddress.setEmailAddress(addressModel.getShippingAddress().getEmailAddress()!=null?addressModel.getShippingAddress().getEmailAddress():"");
			  }else{
				  shippingAddress = billingAddress;
			  }
			  
			  contactInformation.setBillAddress(addressModel.getBillingAddress());
			  contactInformation.setShipAddress(addressModel.getShippingAddress());
			  
			  	if(addressModel.getEntityId()!=null && addressModel.getEntityId().trim().length()>0){
			  		custNumber = addressModel.getEntityId();
			    }else{
					custNumber = insertUser(billingAddress,shippingAddress, null);
				}
			  
			  	if(custNumber==null){
					result = "User Registration Failed. Please Contact Customer Service.|";
					
				}else{
					result = newRetailUserRegistration(custNumber, billingAddress.getCountry().trim(), addressModel.getUserPassword(), locUser, userRole, buyingComanyIdStr, parentUserId,addressModel.getFirstName(),addressModel.getLastName(),addressModel.getUserStatus(), null, true, true, contactInformation);
					if(result!=null){
						String resArr[] = result.split("\\|");
						if(resArr.length>0){
							if(resArr[0].trim().equalsIgnoreCase("0") || result.toLowerCase().contains("successfully")){
							  SendMailUtility.buildNewUser(custNumber, addressModel.getEmailAddress(), addressModel.getUserPassword(),addressModel.getEmailAddress(),addressModel.getFirstName(),addressModel.getLastName());
							}
						}
					}
				}
			
 		 }
		}
		catch (Exception e) {
			
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
			  
			  
			  if(addressModel.getCustomerType()!=null && addressModel.getCustomerType().trim().length()>0){
				  contactInformation.setCustomerType(addressModel.getCustomerType());
			  }

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

			  
			  if(CommonUtility.validateString(addressModel.getCheckSameAsBilling()).equalsIgnoreCase("No")){
				  shippingAddress.setFirstName(addressModel.getShippingAddress().getFirstName()!=null?addressModel.getShippingAddress().getFirstName():"");
				  shippingAddress.setLastName(addressModel.getShippingAddress().getLastName()!=null?addressModel.getShippingAddress().getLastName():"");
				  shippingAddress.setAddress1(addressModel.getShippingAddress().getAddress1()!=null?addressModel.getShippingAddress().getAddress1():"");
				  shippingAddress.setAddress2(addressModel.getShippingAddress().getAddress2()!=null?addressModel.getShippingAddress().getAddress2():"");
				  shippingAddress.setCity(addressModel.getShippingAddress().getCity()!=null?addressModel.getShippingAddress().getCity():"");
				  shippingAddress.setState(addressModel.getShippingAddress().getState()!=null?addressModel.getShippingAddress().getState():"");
				  shippingAddress.setCountry(addressModel.getShippingAddress().getCountry()!=null?addressModel.getShippingAddress().getCountry():"");
				  shippingAddress.setCompanyName(addressModel.getShippingAddress().getCompanyName()!=null?addressModel.getShippingAddress().getCompanyName():"");
				  shippingAddress.setPhoneNo(addressModel.getShippingAddress().getPhoneNo()!=null?addressModel.getShippingAddress().getPhoneNo():"");
				  shippingAddress.setZipCode(addressModel.getShippingAddress().getZipCode()!=null?addressModel.getShippingAddress().getZipCode():"");
				  shippingAddress.setEmailAddress(addressModel.getShippingAddress().getEmailAddress()!=null?addressModel.getShippingAddress().getEmailAddress():"");
			  }else{
			  shippingAddress = billingAddress;
			  }
			  
			  contactInformation.setBillAddress(Cimm2BCentralClient.getInstance().userModelToAddressModel(billingAddress));
			  contactInformation.setShipAddress(Cimm2BCentralClient.getInstance().userModelToAddressModel(shippingAddress));
			  
			  
			  	if(addressModel.getEntityId()!=null && addressModel.getEntityId().trim().length()>0){
			  		custNumber = addressModel.getEntityId();
			    }else{
					custNumber = insertUser(billingAddress,shippingAddress, null);
				}
			  
			  	if(custNumber==null){
					result = "User Registration Failed. Please Contact Customer Service.|";
					
				}else{
					result = newRetailUserRegistration(custNumber, billingAddress.getCountry().trim(), addressModel.getUserPassword(), locUser, userRole, buyingComanyIdStr, parentUserId,addressModel.getFirstName(),addressModel.getLastName(),"Y", null, true, true, contactInformation);
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
									  userAddress.setNewsLetterSub("N");
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
			
			e.printStackTrace();
		}
		return result;
	
	}
	
	
	public static String insertUser(UsersModel billAddressList, UsersModel shipAddressList, String shipvarity){
		String custNumber = "0";
		return custNumber;
	}
	
	public static String newRetailUserRegistration(String customerNumber, String country,String customerPass, String locUser, String userRole, String buyingCompanyIdStr, String parentUserId, String firstName, String lstName, String status,UsersModel contactInformation, boolean isFrmRegPage, boolean updateRole, UsersModel customerInfo){
		String resultVal = "";
		Connection conn=null;
	    try {
            conn = ConnectionManager.getDBConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) { 
            e.printStackTrace();
        }
		
		try{
		    int defaultBillId = 0;
			boolean isAccountExist = false;

				   UsersModel customerinfo = customerInfo;
				   UsersModel contactINFO = customerInfo;
				  	           
					        String taxable = null;
					        String entityid = "0";
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
						           customerinfo.setEntityId(CommonUtility.validateParseIntegerToString(buyingCompanyid));
						           contactINFO.setBuyingCompanyId(buyingCompanyid);
						           if(CommonUtility.validateString(customerNumber).length()>0 && !CommonUtility.validateString(customerNumber).equalsIgnoreCase("0")){
						           }else{
						        	   customerNumber = CommonUtility.validateParseIntegerToString(buyingCompanyid);
						           }
						           userId = UsersDAO.insertNewUser(conn, contactINFO, customerNumber, customerPass, buyingCompanyid, parentUserId,true);
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
						    System.out.println("customer inserted into database successfully");
						    if(!isAccountExist && (buyingCompanyIdStr == null || buyingCompanyIdStr.trim().equalsIgnoreCase(""))){
						    	/**
								 *Below code Written is for Adapt Pharma. *Reference- Prashanth GM
								 */								
								UnilogFactoryInterface billShipserviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
								if(billShipserviceClass!=null && customerinfo!=null) {
									UsersModel billAddressinfo = billShipserviceClass.billShipAddressInsert(customerinfo,"BILL");
									UsersModel shipAddressinfo = billShipserviceClass.billShipAddressInsert(customerinfo,"SHIP");
									if(billAddressinfo!=null && shipAddressinfo!=null){
										 defaultBillId = UsersDAO.insertNewAddressintoBCAddressBook(conn, billAddressinfo,"Bill",true);
										 shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, shipAddressinfo,"Ship",true);
									}else {
										defaultBillId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Bill",true);
										 shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Ship",true);
									}
								}else{
									 defaultBillId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Bill",true);
									 shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn, customerinfo,"Ship",true);
								}
						    }else{
						        UsersModel uModel =  UsersDAO.getDefaultShipNbill(customerNumber);
						        if(uModel!=null){
						        	defaultBillId = CommonUtility.validateNumber(uModel.getBillToId());
						        	shipId = CommonUtility.validateNumber(uModel.getShipToId());
						        }
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
					resultVal = "User Registration Failed.|";
				}
					          
		  }catch (Exception e) {
			 resultVal = "User Registration Failed.|";
			 e.printStackTrace();
		
		}
		 finally{
			 ConnectionManager.closeDBConnection(conn);
		 }
		return resultVal;
	}
	
	public HashMap<String, ArrayList<UsersModel>> getAddressListFromBCAddressBook(int buyingCompanyId, int userId){
		return UsersDAO.getAddressListFromBCAddressBookDefault(buyingCompanyId, userId);
	}
	
	public HashMap<String, UsersModel> getUserAddressFromBCAddressBook(int billId,int shipId){
		return UsersDAO.getUserAddressFromBCAddressBook(billId, shipId);
	}
	
	public String editBillingAddress(UsersModel userInfo){
		int count =-1;
		String result = "";
		count = UsersDAO.updateBillAddressBCAddressBook(userInfo);
		
		if(count>0){
			result = "1 | Billing Address Updated Successfully";
		}else{
			result = "0 | Billing Address Update Failed";
		}
		
		return result;
	}
	
	public String editShippingAddress(UsersModel shipInfo){
		int count =-1;
		String result = "";
		count = UsersDAO.updateBCAddressBook(shipInfo);
		if(shipInfo.getMakeAsDefault()!=null && shipInfo.getMakeAsDefault().trim().equalsIgnoreCase("Yes")){
		   count = UsersDAO.updateDefaultShipTo(shipInfo.getUserId(), shipInfo.getAddressBookId());
		}
		if(count>0){
			result = "1|"+"Shipping Address Updated Successfully";
		}else{
			result = "0|"+"Shipping Address Update Failed";
		}
			
		return result;
	}
	
	public String addNewShippingAddress(UsersModel shipInfo){
		int count =-1;
		String result = "Failed to add New Shipping Address";
		Connection conn = null;
		int userId = 0;
		
		try{
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
				result = "1|"+"Shipping Address Inserted Successfully"+"~"+defaultShipId;
			}else{
				result = "0|"+"Failed to add New Shipping Address";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBConnection(conn);
		}
		return result;
	}
	
	
	public static UsersModel getRoleFromErp(UsersModel userInfo) {
		
		UsersModel defaultContact = new UsersModel();
		try
		{
		// defaultContact = 
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return defaultContact;
	}
	
	public static String updatePrevilege(UsersModel userInfo)
	{
		return "";
	}
	
	public static UsersModel accountDetail(LinkedHashMap<String, String> accountInputValue){
		UsersModel userDetailList = new UsersModel();
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
			e.printStackTrace();
		}
	}
	
	public static String contactUpdate(UsersModel userInfo,HttpSession session) {
		String rPassword = userInfo.getPassword();
	         return rPassword;
	}
	
	public static ArrayList<ShipVia> getUserShipViaList(UsersModel customerInfoInput) {
		ArrayList<ShipVia> shipVia = new ArrayList<ShipVia>();
		try
		{
			shipVia = UsersDAO.getUserShipViaList();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return shipVia;
	}
	
	public static UsersModel contactEdit(UsersModel customerInfoInput,UsersModel addressDetail) {
		UsersModel customerInfoOutput = new UsersModel();
		customerInfoOutput.setResult(UsersDAO.updateContactInformation(addressDetail));
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
		HashMap<String, ArrayList<UsersModel>> shipList = UsersDAO.getAgentAddressListFromBCAddressBook(customerInfoInput.getUserId(),"");
		return shipList;
	}
	
	public static String newOnAccountUserRegistration(UsersModel userDetails){

		ArrayList<UsersModel> superUserStatus = null;
		boolean mail = false;
		String result = "";
		/*SendMailUtility sendMail = new SendMailUtility();
		
		superUserStatus = UsersDAO.getSuperUserForCompany(userDetails.getCompanyName());
		 if (superUserStatus!=null &&  superUserStatus.size()>0){
			 for(UsersModel temp:superUserStatus){
				 UsersDAO.insertRegistrationToCustomTable(userDetails);
				 mail = sendMail.sendRegistrationMail(userDetails,"superUser",temp.getEmailAddress(),"1B");
				 mail = sendMail.sendRegistrationMail(userDetails,"webUser","","1B"); 
			 }
		 }else{
			 //send mail to nce to create customer account
			 //mail = sendMail.sendRegistrationMail(userDetails,"Sales","","1B"); 
		 }
		  mail = sendMail.sendRegistrationMail(userDetails,"customer","","1B"); 
		  result = "Registration request submitted successfully";*/
		 result = "Registration request submitted successfully";
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
				
				
			}
			public String createWLUser(UsersModel userDetails){
				if(CommonUtility.customServiceUtility()!=null) {
					AddressModel userRegistrationDetail = CommonUtility.customServiceUtility().createRetailUser(userDetails);
					if(userRegistrationDetail!=null) {
						String result = "Server Error. Please Try again later.|";
						result = createRetailUser(userRegistrationDetail);
						return result;
					}
				}
				return "";
			}
			
			public String getCustomerDataFromERP(UsersModel userModel){
				try {
					// not required
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "";
			}
			public static int getLastOrderDetails(UsersModel userDetails){
				return 0;
			}
			public UsersModel getTravelPoint(UsersModel userModel){
				return userModel;
			}
			public static String createNewAgent(AddressModel userInfo)
			{
				String result = "";
				String roleName = "";
				int userId = UserRegisterUtility.registerInforSXUsertoDB(userInfo);
	        	
	 			if(userId>0)
	 			{
	 				Connection  conn = null;
	 				
	 				try {
	 			       
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
		 				
	 			    } catch (SQLException e) { 
	 			        e.printStackTrace();
	 			    }
	 			    finally
	 			    {
	 			    	ConnectionManager.closeDBConnection(conn);
	 			    }
	 		
	 			}
	 			else
	 			{
	 				result = result + "Error while registering new user. Contact our customer service for further assistance.|";
	 			}
				return result;
			}
			
			public static UserManagementModel arGetCustomerDataGeneralV2Response(UsersModel usersModel){
				UserManagementModel  userModelOutPut = new UserManagementModel();
			
				return userModelOutPut;
			}
			
			public static UserManagementModel arGetCustomerDataCreditResponse(UsersModel usersModel){
				

				UserManagementModel  customerDataCreditOutput = new UserManagementModel();

				try{
				
					customerDataCreditOutput = new UserManagementModel();
				}catch (Exception e) {
					e.printStackTrace();
				}
				return customerDataCreditOutput;	
			}
			
			public static UserManagementModel arGetCustomerBalanceResponse(UsersModel usersModel){
				UserManagementModel  customerDataCreditOutput = new UserManagementModel();
				try{
					customerDataCreditOutput = new UserManagementModel();
				}catch (Exception e) {
					e.printStackTrace();
				}
				return customerDataCreditOutput;	
			}
			
			public static UserManagementModel arGetCustomerBalanceV2Response(UsersModel usersModel){
					
				UserManagementModel  customerDataCreditOutput = new UserManagementModel();
				try{
					customerDataCreditOutput = new UserManagementModel();
				}	
				catch (Exception e) {
					e.printStackTrace();
				}
				return customerDataCreditOutput;	
			}
			
			public  static  ArrayList<UserManagementModel> ARGetInvoiceListV2(UsersModel userModel){ 
				ArrayList<UserManagementModel> arInvoiceList = null;
				try{
				 arInvoiceList = new ArrayList<UserManagementModel>();
				}catch (Exception e) {
					e.printStackTrace();
				}
				return arInvoiceList;	 
				
			}
			
			public static String getShipToStoreWarehouseCode(String warehouse) {
				return "";
			}
}
