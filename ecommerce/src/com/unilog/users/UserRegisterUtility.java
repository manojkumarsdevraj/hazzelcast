package com.unilog.users;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.erp.eclipse.inquiry.ContactInquiry;
import com.erp.eclipse.inquiry.EntityInquiry;
import com.erp.service.UserManagement;
import com.erp.service.impl.UserManagementImpl;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.security.SecureData;
import com.unilog.utility.CommonUtility;


public class UserRegisterUtility extends SecureData {

	public static String registerUserInDB(AddressModel addressModel) 
	{

		Connection conn = null;
		CallableStatement stmt = null;
		int buyingCompanyId = 0;
		int userBuyingCompanyId = 0;
		String billEntityId = "0";
		int userId = 0;
		int billAddressId = 0;
		int shipAddressId = 0;
		int parentId = 0;
		String entityId = "0";
		String status = null;
		String msg = "User Not Registered. Please Contact Customer service.";
		String eclipseMsg = "";
		String userEmailAddress = null;

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
				UsersModel defaultBillList = new UsersModel();
				UsersModel defaultContact = new UsersModel();
				UserManagement usersObj = new UserManagementImpl();
				UsersModel shipList = null;
				ArrayList<UsersModel> shippAddressList = new ArrayList<UsersModel>();

				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y"))
				{
					userEmailAddress = addressModel.getEmailAddress();
				}else{
					userEmailAddress = addressModel.getEmailAddress().toLowerCase();
				}

				userDetailList = EntityInquiry.eclipseEntityEnquiry(addressModel.getUserToken(),entityId,userEmailAddress);
				if(userDetailList!=null)
				{
					eclipseMsg = userDetailList.getStatusDescription();
					status = userDetailList.getOrderEntryOk();
					ArrayList<AddressModel> contactValue = userDetailList.getContactShortList();
					if(contactValue!=null && contactValue.size()>1){
						/*userDetailList.setPhoneNo(contactValue.get(1).getPhoneNo());*/
						String phoneNumber = "";
						for(AddressModel phone : userDetailList.getContactShortList()){
							if(phone.getPhoneDescription().trim().equalsIgnoreCase("Business")){
								phoneNumber=phone.getPhoneNo();
							}
						}
						userDetailList.setPhoneNo(phoneNumber);
					}
				}

				if(CommonUtility.validateString(addressModel.getDisableSubmitPOCC()).equalsIgnoreCase("Y")){
					userDetailList.setDisableSubmitPOCC(addressModel.getDisableSubmitPOCC());
				}

				if(CommonUtility.validateString(addressModel.getDisableSubmitPO()).equalsIgnoreCase("Y")){
					userDetailList.setDisableSubmitPO(addressModel.getDisableSubmitPO());
				}
				if(eclipseMsg!=null && eclipseMsg.trim().contains("successfully"))
				{
					System.out.println("Entity Type : " + userDetailList.getEntityType());
					if(userDetailList.getEntityType()!=null && userDetailList.getEntityType().trim().contains("Vendor"))
					{
						eclipseMsg = "Login Failed. User Member Login.";
					}
					else
					{
						ArrayList<String> contactIdList = userDetailList.getContactIdList();
					/*	if(addressModel.getContactId()==null)
						{
							HttpServletRequest request = ServletActionContext.getRequest();
							HttpSession session = request.getSession();
							String contactId=(String) session.getAttribute("contactID");
							addressModel.setContactId(contactId);
						}*/
						if(contactIdList!=null && contactIdList.size()>0)
						{
							for(String contactId:contactIdList)
							{

								defaultContact = usersObj.contactInquiry(contactId);
								if(defaultContact.getContactIdList()!=null)
								{
									if(defaultContact.getErpLoginId()!=null && defaultContact.getErpLoginId().trim().equalsIgnoreCase(userEmailAddress))
										break;
								}


							}
						}

						entityId = userDetailList.getEntityId();
						billEntityId = userDetailList.getBillEntityId();


						if(!CommonUtility.validateString(billEntityId).isEmpty() && !CommonUtility.validateString(billEntityId).equals("0")){
							parentId = checkEntityId(billEntityId);
							userDetailList = EntityInquiry.eclipseEntityEnquiry(addressModel.getUserToken(),billEntityId,userEmailAddress);
						}else{
							billEntityId = entityId;
							parentId = checkEntityId(billEntityId);
						}
						ArrayList<String> shipIdList = userDetailList.getShipIdList();
						if(shipIdList!=null && shipIdList.size()>0){
							for(String shipId:shipIdList)
							{
								int count = checkEntityId(shipId);
								if(count==0){
									shipList = new UsersModel();
									shipList = EntityInquiry.eclipseEntityEnquiry(addressModel.getUserToken(),shipId,userEmailAddress);
									if(shipList!=null && shipList.getOrderEntryOk()!=null && shipList.getOrderEntryOk().trim().equalsIgnoreCase("YES")){
										shippAddressList.add(shipList);
									}
								}
							}
						}
						if(parentId==0){
							if(status!=null && status.trim().equalsIgnoreCase("Yes")){
								parentId = insertBuyingCompany(conn,userDetailList,0,status);
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
						}

						if(parentId>0)
						{
							conn.commit();

							if(billAddressId==0)
								billAddressId = getBCAddressBookId(billEntityId, "Bill");

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
									if(!CommonUtility.validateString(userDetailList.getEntityId()).equalsIgnoreCase(CommonUtility.validateString(shipAddress.getEntityId())))
									{
										if(status!=null && status.trim().equalsIgnoreCase("Yes"))
											buyingCompanyId = insertBuyingCompany(conn,shipAddress,parentId,status);
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
									shipAddressId = getBCAddressBookId(entityId, "Ship");

							}
							else
							{
								if(shipAddressId==0)
									shipAddressId = getBCAddressBookId(entityId, "Ship");

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
						userBuyingCompanyId = checkEntityId(entityId);
						int parentCompanyId = checkEntityId(billEntityId);


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
							//userId = insertNewUser(conn,defaultContact,userName, password,userBuyingCompanyId,existingUser,parentUserId,userStatus);
							userId = insertNewUser(conn,defaultContact);
						}
						System.out.println("User Id : " + userId);
						if(userId > 0){
							
							if(billAddressId>0 && shipAddressId > 0)
							{
								updateUserAddressBook(conn, billAddressId, shipAddressId, userId);
								conn.commit();
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
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			eclipseMsg = msg;
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return eclipseMsg;

	}

	public static String registerUserInDBFromCimm2BCentral(AddressModel addressModel){

		Connection conn = null;
		CallableStatement stmt = null;
		boolean isRegistered = false;
		int buyingCompanyId = 0;
		int userBuyingCompanyId = 0;
		String billEntityId = "0";
		int userId = 0;
		int billAddressId = 0;
		int shipAddressId = 0;
		String defaultEntityId = "0";
		int parentId = 0;
		String entityId = "0";
		String status = null;
		String msg = "User Not Registered. Please Contact Customer service.";
		String eclipseMsg = "";
		String userEmailAddress = null;

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
		UsersModel defaultBillList = new UsersModel();
		UsersModel defaultContact = new UsersModel();
		
		UsersModel shipList = null;
		ArrayList<UsersModel> shippAddressList = new ArrayList<UsersModel>();
		
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y"))
		 {
			userEmailAddress = addressModel.getEmailAddress();
		 }else{
			 userEmailAddress = addressModel.getEmailAddress().toLowerCase();
		 }
		
		userDetailList = EntityInquiry.eclipseEntityEnquiry(addressModel.getUserToken(),entityId,userEmailAddress);
		if(userDetailList!=null)
		{
			eclipseMsg = userDetailList.getStatusDescription();
			status = userDetailList.getOrderEntryOk();
			ArrayList<AddressModel> contactValue = userDetailList.getContactShortList();
			if(contactValue!=null && contactValue.size()>1){
				/*userDetailList.setPhoneNo(contactValue.get(1).getPhoneNo());*/
				String phoneNumber = "";
				for(AddressModel phone : userDetailList.getContactShortList()){
					if(phone.getPhoneDescription().trim().equalsIgnoreCase("Business")){
						phoneNumber=phone.getPhoneNo();
					}
				}
				userDetailList.setPhoneNo(phoneNumber);
			}
		}
		
		if(CommonUtility.validateString(addressModel.getDisableSubmitPOCC()).equalsIgnoreCase("Y")){
			userDetailList.setDisableSubmitPOCC(addressModel.getDisableSubmitPOCC());
		}

		if(CommonUtility.validateString(addressModel.getDisableSubmitPO()).equalsIgnoreCase("Y")){
			userDetailList.setDisableSubmitPO(addressModel.getDisableSubmitPO());
		}
		if(eclipseMsg!=null && eclipseMsg.trim().contains("successfully"))
		{
			System.out.println("Entity Type : " + userDetailList.getEntityType());
			if(userDetailList.getEntityType()!=null && userDetailList.getEntityType().trim().contains("Vendor"))
			{
				eclipseMsg = "Login Failed. User Member Login.";
			}
			else
			{
			ArrayList<String> contactIdList = userDetailList.getContactIdList();
			if(contactIdList!=null && contactIdList.size()>0)
			{
			for(String contactId:contactIdList)
			{
				
				defaultContact = ContactInquiry.ERPContactEnqiry(addressModel.getUserToken(), contactId,userEmailAddress);
				if(defaultContact.getContactIdList()!=null)
				{
					if(defaultContact.getErpLoginId()!=null && defaultContact.getErpLoginId().trim().equalsIgnoreCase(userEmailAddress))
					break;
				}
					
				
			}
			}
			
			entityId = userDetailList.getEntityId();
			billEntityId = userDetailList.getBillEntityId();
			
			
			if(!CommonUtility.validateString(billEntityId).isEmpty() && !CommonUtility.validateString(billEntityId).equals("0"))
			{
				parentId = checkEntityId(billEntityId);
				userDetailList = EntityInquiry.eclipseEntityEnquiry(addressModel.getUserToken(),billEntityId,userEmailAddress);
				defaultEntityId = userDetailList.getEntityId();
			}
			else
			{
				billEntityId = entityId;
				parentId = checkEntityId(billEntityId);
				defaultEntityId = entityId;
			}
			
			ArrayList<String> shipIdList = userDetailList.getShipIdList();
			
			if(shipIdList!=null && shipIdList.size()>0){
				
			for(String shipId:shipIdList)
			{
				
						int count = checkEntityId(shipId);
						if(count==0){
						shipList = new UsersModel();
						shipList = EntityInquiry.eclipseEntityEnquiry(addressModel.getUserToken(),shipId,userEmailAddress);
							if(shipList!=null && shipList.getOrderEntryOk()!=null && shipList.getOrderEntryOk().trim().equalsIgnoreCase("YES")){
								shippAddressList.add(shipList);
							}
						}
				
			}
			}
			
			if(parentId==0)
			{
				if(status!=null && status.trim().equalsIgnoreCase("Yes")){
					parentId = insertBuyingCompany(conn,userDetailList,0,status);
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
			}
			
			if(parentId>0)
			{
				conn.commit();
				
				if(billAddressId==0)
				billAddressId = getBCAddressBookId(billEntityId, "Bill");
				
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
						if(status!=null && status.trim().equalsIgnoreCase("Yes"))
							buyingCompanyId = insertBuyingCompany(conn,shipAddress,parentId,status);
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
					shipAddressId = getBCAddressBookId(entityId, "Ship");
					
			}
			else
			{
				if(shipAddressId==0)
					shipAddressId = getBCAddressBookId(entityId, "Ship");
				
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
			userBuyingCompanyId = checkEntityId(entityId);
			int parentCompanyId = checkEntityId(billEntityId);
			
			
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
				//userId = insertNewUser(conn,defaultContact,userName, password,userBuyingCompanyId,existingUser,parentUserId,userStatus);
				userId = insertNewUser(conn,defaultContact);
			}
			System.out.println("User Id : " + userId);
			if(userId > 0){
				

				defaultBillList = getEntityAddressByCompanyId(parentCompanyId);
				//billAddressId = insertNewAddress(conn,defaultBillList, userId, "Bill");
				//shipAddressId = insertNewAddress(conn,defaultBillList, userId, "Ship");
				
				
				/*shippAddressList = getEntityAddressById(userBuyingCompanyId);
				if(shippAddressList!=null)
				{
					for(UsersModel shippingAddress:shippAddressList)
					{
						int count = UserUtilityDAO.insertNewAddressintoBCAddressBook(conn, shippingAddress, "Ship");
							//insertNewAddress(conn,shippingAddress, userId, "Ship");
						if(shippingAddress.getEntityId()==entityId)
						shipAddressId = count;
					}
				}*/
				if(billAddressId>0 && shipAddressId > 0)
				{
					int count = updateUserAddressBook(conn, billAddressId, shipAddressId, userId);
					conn.commit();
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
			
			isRegistered = true;
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
		
		}
		catch(Exception e)
		{
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			eclipseMsg = msg;
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return eclipseMsg;
	
	}
	
	public static int insertBuyingCompany(Connection conn,UsersModel userDetailList,int parentId,String status)
	{
		int buyingCompanyId=0;
		PreparedStatement pstmt = null;
		int subsetId = 0;
		String sql = null;
		int count = 0;

		try
		{
			if(CommonDBQuery.getSystemParamtersList().get("SUBSETID")!=null && CommonDBQuery.getSystemParamtersList().get("SUBSETID").trim().length()>0){
				subsetId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SUBSETID").trim());
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_WAREHOUSE_SUBSET_ID")).equalsIgnoreCase("Y")){
				WarehouseModel warehouseModel = UsersDAO.getWareHouseDetailsByCode(CommonUtility.validateString(userDetailList.getWareHouseCodeStr()));
				if(warehouseModel!=null && warehouseModel.getSubsetId()>0){
					subsetId = warehouseModel.getSubsetId();
				}
			}
			int siteId = 0;
			if(CommonDBQuery.getGlobalSiteId()>0){
				siteId = CommonDBQuery.getGlobalSiteId();
			}

			if(CommonUtility.validateString(userDetailList.getDisableSubmitPOCC()).trim().length()>0 && CommonUtility.validateString(userDetailList.getDisableSubmitPOCC()).equalsIgnoreCase("Y")){
				sql = PropertyAction.SqlContainer.get("inserBuyingCompanyDisableSubmitPOCC");	
			}else if(CommonUtility.validateString(userDetailList.getDisableSubmitPO()).trim().length()>0 && CommonUtility.validateString(userDetailList.getDisableSubmitPO()).equalsIgnoreCase("Y")){
				sql = PropertyAction.SqlContainer.get("inserBuyingCompanyDisableSubmitPO");	
			}else{
				sql = PropertyAction.SqlContainer.get("insertBuyingCompany");
			}
			String shortNameVal = "No Entity Name";
			if(userDetailList.getEntityName()!=null)
			{
				shortNameVal = userDetailList.getEntityName();
			}
			String shortNameArr[] = shortNameVal.trim().split("\\s+");
			String shortName = "";

			if(shortNameArr!=null && shortNameArr.length>0)
			{
				for(String sName:shortNameArr)
				{
					shortName = shortName + sName.substring(0, 1);
				}
			}

			//BUYING_COMPANY_ID,CUSTOMER_NAME,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,EMAIL,URL,SUBSET_ID,STATUS,UPDATED_DATETIME,ENTITY_ID
			buyingCompanyId = CommonDBQuery.getSequenceId("BUYING_COMPANY_ID_SEQ");
			System.out.println("userDetailList.getCountry() : " + userDetailList.getCountry());
			String country = userDetailList.getCountry();
			if(country !=null){
				String ecCountry = UsersDAO.getCountryCode(country);
				if(ecCountry!=null && !ecCountry.equalsIgnoreCase("")){
					country = ecCountry;
				}else{
					country = "US";	
				}

			}else{
				country = "";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, buyingCompanyId);
			pstmt.setString(2, userDetailList.getEntityName());
			if(userDetailList.getAddress1()==null){
				pstmt.setString(3, "No Address");	
			}else{
				pstmt.setString(3, userDetailList.getAddress1());	
			}
			pstmt.setString(4, userDetailList.getAddress2());
			pstmt.setString(5, userDetailList.getCity());
			pstmt.setString(6, userDetailList.getState());
			pstmt.setString(7, userDetailList.getZipCode());
			pstmt.setString(8, country);
			pstmt.setString(9, userDetailList.getEmailAddress());
			pstmt.setString(10, userDetailList.getWebAddress());
			pstmt.setInt(11, subsetId);
			pstmt.setString(12, userDetailList.getEntityId());
			if(parentId>0){
				pstmt.setString(13, Integer.toString(parentId));	
			}else{
				pstmt.setString(13, "");
			}
			pstmt.setString(14, shortName);
			pstmt.setString(15, userDetailList.getCurrency());
			pstmt.setString(16, "");
			pstmt.setString(17, "");
			pstmt.setInt(18, siteId);
			if(CommonUtility.validateString(userDetailList.getDisableSubmitPOCC()).trim().length()>0 && CommonUtility.validateString(userDetailList.getDisableSubmitPOCC()).equalsIgnoreCase("Y")){
				pstmt.setString(19, "N");
				pstmt.setString(20, "N");
			}
			if(CommonUtility.validateString(userDetailList.getDisableSubmitPO()).trim().length()>0 && CommonUtility.validateString(userDetailList.getDisableSubmitPO()).equalsIgnoreCase("Y")){
				pstmt.setString(19, "N");
			}

			count = pstmt.executeUpdate();

			if(count<1){
				buyingCompanyId = 0;
			}else{
				if(CommonDBQuery.getSystemParamtersList().get("ENABLE_GENERAL_CATALOG_ACCESS")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_GENERAL_CATALOG_ACCESS").trim().equalsIgnoreCase("Y")){
					UsersDAO.grantGenralCatalogAccess(conn,buyingCompanyId);
				}
			}
		}catch(SQLException e){
			buyingCompanyId =0;
			e.printStackTrace();
		}catch(Exception e){
			buyingCompanyId =0;
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);	
		}
		return buyingCompanyId;
	}

	public static int insertNewUser(Connection conn,UsersModel userDetailList)
	{
		int userId=0;

		PreparedStatement pstmt = null;
		int count = 0;

		
		try
		{
			/*String phoneNumber = "";
        	if(userDetailList.getContactShortList()!=null && userDetailList.getContactShortList().size() > 0){
				for(AddressModel phone : userDetailList.getContactShortList()){
					if(phone.getPhoneDescription().trim().equalsIgnoreCase("Home")){
						phoneNumber=phone.getPhoneNo();
					}
				}
			}*/
			if(CommonUtility.validateString(userDetailList.getUserStatus()).trim().length()==0){
				userDetailList.setUserStatus("I");
			}
			int siteId = 0;
			if(CommonDBQuery.getGlobalSiteId()>0){
				siteId = CommonDBQuery.getGlobalSiteId();
			}

			String sql = PropertyAction.SqlContainer.get("insertNewUser");
			String securePassword = getsecurePassword(userDetailList.getPassword());
			//USER_ID,USER_NAME,PASSWORD,FIRST_NAME,LAST_NAME,BUYING_COMPANY_ID,OFFICE_PHONE,CELL_PHONE,FAX,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,ECLIPSE_CONTACT_ID,EMAIL,UPDATED_DATETIME,FIRST_LOGIN,PARENT_USER_ID,EXISTING_CUSTOMER,STATUS,TERMS_TYPE,TERMS_TYPE_DESC,SHIP_VIA,SHIP_VIA_DESC,REGISTERED_DATE
			userId = CommonDBQuery.getSequenceId("USER_ID_SEQUENCE");
			String country = userDetailList.getCountry();
			if(country !=null){
				String ecCountry = UsersDAO.getCountryCode(country);
				if(ecCountry!=null && !ecCountry.equalsIgnoreCase("")){
					country = ecCountry;
				}else{
					country = "US";
				}
			}else{
				country = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_COUNTRY"));
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setString(2, CommonUtility.validateString(userDetailList.getEmailAddress()));
			pstmt.setString(3, securePassword);
			pstmt.setString(4, userDetailList.getFirstName());
			pstmt.setString(5, userDetailList.getLastName());
			pstmt.setInt(6, userDetailList.getBuyingCompanyId());
			pstmt.setString(7, userDetailList.getPhoneNo());//pstmt.setString(7, phoneNumber);
			pstmt.setString(8, userDetailList.getOfficePhone()!=null?userDetailList.getOfficePhone():"");
			pstmt.setString(9, userDetailList.getFaxNo());
			pstmt.setString(10, userDetailList.getAddress1());
			pstmt.setString(11, userDetailList.getAddress2());
			pstmt.setString(12, userDetailList.getCity());
			pstmt.setString(13, userDetailList.getState());
			pstmt.setString(14, userDetailList.getZipCode());
			pstmt.setString(15, country);
			pstmt.setString(16, userDetailList.getContactIdList().get(0));
			pstmt.setString(17, CommonUtility.validateString(userDetailList.getEmailAddress()));
			pstmt.setString(18, "Y");
			pstmt.setInt(19, userDetailList.getParentId());
			pstmt.setString(20, userDetailList.getExistingCustomer());
			pstmt.setString(21, userDetailList.getUserStatus());
			pstmt.setString(22, userDetailList.getTermsType());
			pstmt.setString(23, userDetailList.getTermsTypeDesc());
			pstmt.setString(24, userDetailList.getShipViaID());
			pstmt.setString(25, userDetailList.getShipViaMethod());
			pstmt.setInt(26, siteId);

			count = pstmt.executeUpdate();
			if(count<1)
				userId = 0;
		}
		catch(SQLException e)
		{
			userId = 0;
			e.printStackTrace();
		}
		catch(Exception e)
		{
			userId = 0;
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}
		return userId;
	}

	public static int insertNewAddress(Connection conn,UsersModel userDetailList,int userId,String type)
	{
		int addressBookId=0;
		int count = 0;

		PreparedStatement pstmt = null;

		try
		{
			//USER_ID,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,ADDRESS_TYPE;

			System.out.println("Ship UserId : "+userId+" | Address1 : "+userDetailList.getAddress1()+" | Address2 : "+userDetailList.getAddress2()+" | City : "+userDetailList.getCity()+" | State : "+userDetailList.getState()+" | ZipCode : "+userDetailList.getZipCode()+" | Type : "+type);

			String sql = PropertyAction.SqlContainer.get("insertAddressBook");
			addressBookId = CommonDBQuery.getSequenceId("ADDRESS_BOOK_ID_SEQ");
			//ADDRESS_BOOK_ID,USER_ID,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,COUNTRY,PHONE,ADDRESS_TYPE
			String country = userDetailList.getCountry();
			if(country !=null){
				String ecCountry = UsersDAO.getCountryCode(country);
				if(ecCountry!=null && !ecCountry.equalsIgnoreCase(""))
					country = ecCountry;
				else
					country = "US";
			}
			else
				country = "US";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, addressBookId);
			pstmt.setInt(2, userId);
			pstmt.setString(3, userDetailList.getAddress1());
			pstmt.setString(4, userDetailList.getAddress2());
			pstmt.setString(5, userDetailList.getCity());
			pstmt.setString(6, userDetailList.getState());
			pstmt.setString(7, userDetailList.getZipCode());
			pstmt.setString(8, country);
			pstmt.setString(9, userDetailList.getPhoneNo());
			pstmt.setString(10, type);
			pstmt.setString(11, userDetailList.getEntityId());
			pstmt.setInt(12, userDetailList.getBuyingCompanyId());
			count = pstmt.executeUpdate();

			if(count<1)
				addressBookId = 0;

		}
		catch(SQLException e)
		{
			addressBookId = 0;
			e.printStackTrace();
		}
		catch(Exception e)
		{
			addressBookId = 0;
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}

		return addressBookId;
	}

	public static int updateUserAddressBook(Connection conn,int billAddressId,int shipAddressId,int userId)
	{
		int count = 0;
		PreparedStatement pstmt = null;

		try
		{
			String sql = PropertyAction.SqlContainer.get("updateUserAddressId");
			//DEFAULT_BILLING_ADDRESS_ID=? AND DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE USER_ID=?
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, billAddressId);
			pstmt.setInt(2, shipAddressId);
			pstmt.setInt(3, userId);
			count = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			count =0;
			e.printStackTrace();
		}
		catch(Exception e)
		{
			count =0;
			e.printStackTrace();
		}
		finally
		{

			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}

		return count;
	}

	public static int checkEntityId(String entityId)
	{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		int parentId = 0;


		try
		{
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);
			String sql = PropertyAction.SqlContainer.get("checkEntityId");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, entityId);
			rs = pstmt.executeQuery();
			if(rs.next())
				parentId = rs.getInt("BUYING_COMPANY_ID");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);	
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
		return parentId;
	}

	public static int getBCAddressBookId(String bcId,String addressType)
	{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		int parentId = 0;


		try
		{
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);
			String sql = "SELECT BC_ADDRESS_BOOK_ID FROM BC_ADDRESS_BOOK WHERE ENTITY_ID=? AND ADDRESS_TYPE=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bcId);
			pstmt.setString(2, addressType);
			rs = pstmt.executeQuery();
			if(rs.next())
				parentId = rs.getInt("BC_ADDRESS_BOOK_ID");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);	
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
		return parentId;
	}

	public static ArrayList<UsersModel> getEntityAddressById(int parentId)
	{

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		ArrayList<UsersModel> addressList = null;

		try
		{
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);
			addressList = new ArrayList<UsersModel>();
			String sql = PropertyAction.SqlContainer.get("getAddressByParentId");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, parentId);
			pstmt.setInt(2, parentId);
			rs = pstmt.executeQuery();
			//ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,EMAIL,ENTITY_ID;
			while(rs.next())
			{
				UsersModel address = new UsersModel();
				address.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
				address.setAddress1(rs.getString("ADDRESS1"));
				address.setAddress2(rs.getString("ADDRESS2"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZipCode(rs.getString("ZIP"));
				address.setCountry(rs.getString("COUNTRY"));
				address.setEmailAddress(rs.getString("EMAIL"));
				address.setEntityId(rs.getString("ENTITY_ID"));
				addressList.add(address);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);	
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
		return addressList;

	}

	public static UsersModel getEntityAddressByCompanyId(int parentId)
	{

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		UsersModel address = null;

		try
		{
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);
			String sql = PropertyAction.SqlContainer.get("getAdressByCompanyId");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, parentId);
			rs = pstmt.executeQuery();
			//ADDRESS1,ADDRESS2,CITY,STATE,ZIP,COUNTRY,EMAIL,ENTITY_ID;
			while(rs.next())
			{
				address = new UsersModel();
				address.setCustomerName(rs.getString("CUSTOMER_NAME"));
				address.setBuyingCompanyId(rs.getInt("BUYING_COMPANY_ID"));
				address.setAddress1(rs.getString("ADDRESS1"));
				address.setAddress2(rs.getString("ADDRESS2"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZipCode(rs.getString("ZIP"));
				address.setCountry(rs.getString("COUNTRY"));
				address.setEmailAddress(rs.getString("EMAIL"));
				address.setEntityId(rs.getString("ENTITY_ID"));

			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);	
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
		return address;

	}

	public static boolean isRegisteredUser(Connection conn,String email)
	{

		boolean isUser= false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			String sql = "select user_id from cimm_users where upper(email)=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email.toUpperCase());

			rs = pstmt.executeQuery();
			if(rs.next())
			{
				isUser = true;
			}
		}
		catch(SQLException e)
		{
			isUser =true;
			e.printStackTrace();
		}
		catch(Exception e)
		{
			isUser =true;
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}

		return isUser;

	}

	public static int insertNewAddressintoBCAddressBook(Connection conn,UsersModel userDetailList,int userId,String type)
	{
		int addressBookId=0;
		int count = 0;

		PreparedStatement pstmt = null;

		try
		{

			String sql =PropertyAction.SqlContainer.get("insertBCAddressBook");
			addressBookId = CommonDBQuery.getSequenceId("BC_ADDRESS_BOOK_ID_SEQ");
			//ADDRESS_BOOK_ID,USER_ID,ADDRESS1,ADDRESS2,CITY,STATE,ZIPCODE,COUNTRY,PHONE,ADDRESS_TYPE
			String country = userDetailList.getCountry();
			if(country !=null){
				String ecCountry = UsersDAO.getCountryCode(country);
				if(ecCountry!=null && !ecCountry.equalsIgnoreCase(""))
					country = ecCountry;
				else
					country = "US";
			}
			else
				country = "US";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, addressBookId);
			pstmt.setInt(2, userId);
			pstmt.setString(3, userDetailList.getAddress1());
			pstmt.setString(4, userDetailList.getAddress2());
			pstmt.setString(5, userDetailList.getCity());
			pstmt.setString(6, userDetailList.getState());
			pstmt.setString(7, userDetailList.getZipCode());
			pstmt.setString(8, country);
			pstmt.setString(9, userDetailList.getPhoneNo());
			pstmt.setString(10, type);
			pstmt.setString(11, userDetailList.getEntityId());
			pstmt.setInt(12, userDetailList.getBuyingCompanyId());
			count = pstmt.executeUpdate();

			if(count<1)
				addressBookId = 0;

		}
		catch(SQLException e)
		{
			addressBookId = 0;
			e.printStackTrace();
		}
		catch(Exception e)
		{
			addressBookId = 0;
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}

		return addressBookId;
	}

	public static int updateBCAddressBook(Connection conn,int shipAddressId,int billAddressId,int userId)
	{
		int count = 0;
		PreparedStatement pstmt = null;

		try
		{
			String sql = "UPDATE CIMM_USERS SET DEFAULT_BILLING_ADDRESS_ID=?,DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE USER_ID=?";
			//DEFAULT_BILLING_ADDRESS_ID=? AND DEFAULT_SHIPPING_ADDRESS_ID = ? WHERE USER_ID=?
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, billAddressId);
			pstmt.setInt(2, shipAddressId);
			pstmt.setInt(3, userId);
			count = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			count =0;
			e.printStackTrace();
		}
		catch(Exception e)
		{
			count =0;
			e.printStackTrace();
		}
		finally
		{

			ConnectionManager.closeDBPreparedStatement(pstmt);	

		}

		return count;
	}

	public static int registerInforSXUsertoDB(AddressModel userInfo)
	{
		Connection conn = null;

		int userId = 0;
		int count = 0;
		String entityid = userInfo.getEntityId();
		String buyingid = userInfo.getBuyingComanyIdStr();
		int buyingCompanyId = CommonUtility.validateNumber(buyingid);
		String parentid = userInfo.getParentUserId();
		int parentUserId = CommonUtility.validateNumber(parentid);
		ArrayList<String> contactIdList = new ArrayList<String>();

		try{

			HttpSession session = userInfo.getSession();



			conn = ConnectionManager.getDBConnection();
			contactIdList.add(CommonUtility.validateString(entityid));

			UsersModel userDetails = new UsersModel();
			userDetails.setUserName(userInfo.getUserName());
			userDetails.setPassword(userInfo.getUserPassword());
			userDetails.setFirstName(userInfo.getFirstName());
			userDetails.setLastName(userInfo.getLastName());
			userDetails.setPhoneNo(userInfo.getPhoneNo());
			userDetails.setOfficePhone(userInfo.getPhoneCode()!=null?userInfo.getPhoneCode():"");
			userDetails.setAddress1(userInfo.getAddress1());
			userDetails.setAddress2(userInfo.getAddress2());
			userDetails.setCity(userInfo.getCity());
			userDetails.setState(userInfo.getState());
			userDetails.setZipCode(userInfo.getZipCode());
			userDetails.setCountry(userInfo.getCountry());
			userDetails.setEmailAddress(userInfo.getEmailAddress());
			userDetails.setEntityId(entityid);
			userDetails.setBuyingCompanyId(buyingCompanyId);
			userDetails.setContactIdList(contactIdList);

			userDetails.setParentId(parentUserId);
			userDetails.setExistingCustomer("N");
			userDetails.setUserStatus(userInfo.getUserStatus());
			userDetails.setTermsType((session!=null && session.getAttribute("termsType")!=null)?(String)session.getAttribute("termsType"):null);
			userDetails.setTermsTypeDesc((session!=null && session.getAttribute("termsTypeDesc")!=null)?(String)session.getAttribute("termsTypeDesc"):null);
			userDetails.setShipViaID((session!=null && session.getAttribute("customerShipVia")!=null)?(String)session.getAttribute("customerShipVia"):null);
			userDetails.setShipViaMethod((session!=null && session.getAttribute("customerShipViaDesc")!=null)?(String)session.getAttribute("customerShipViaDesc"):null);

			if(buyingCompanyId >0)
			{
				//userId = insertNewUser(conn,userDetails,userInfo.getEmailAddress(), userInfo.getUserPassword(),buyingCompanyId,"N",parentUserId,userInfo.getUserStatus());
				userId = insertNewUser(conn,userDetails);
			}
			System.out.println("User Id : " + userId);

			HashMap<String,Integer> defaultAddresssId = UsersDAO.getDefaultAddressId(parentUserId);
			int shipAddressId = defaultAddresssId.get("Ship");

			int billAddressId = defaultAddresssId.get("Bill");

			if(userId > 0){
				if(billAddressId>0 && shipAddressId > 0)
				{
					count = updateUserAddressBook(conn, billAddressId, shipAddressId, userId);
				}

				if(CommonDBQuery.getSystemParamtersList().get("CALL_FOR_KEYWORD_BUILDER")!=null && CommonDBQuery.getSystemParamtersList().get("CALL_FOR_KEYWORD_BUILDER").trim().equalsIgnoreCase("Y")){
					if(CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD")!=null && CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD").trim().equalsIgnoreCase("Y")){
						UsersDAO.buildKeyWord("USER", userId);
					}else{
						UsersDAO.buildUserKeyWord("USER", userId);
					}
				}

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBConnection(conn);
		}
		return userId;
	}
}
