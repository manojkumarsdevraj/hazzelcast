package com.erp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceSummary;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCylinderBalanceSummary;
import com.erp.service.model.UserManagementModel;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCustomer;
import com.unilog.users.AddressModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersModel;


public interface UserManagement {
	
	public String getUserToken(String userName, String password);

	public String insertUser(UsersModel billAddressList,UsersModel shipAddressList, String shipvarity);
	
	public String newRetailUserRegistration(String customerNumber, String country,String customerPass, String locUser, String userRole, String buyingCompanyIdStr, String parentUserId, String firstName, String lstName, String status,UsersModel contactInformation, boolean isFrmRegPage, boolean updateRole,UsersModel customerInfo);
	
	public String newOnAccountUserRegistration(UsersModel userDetailsInput);
	
	public String existingUserRegistration(UsersModel customerInfoInput);
	
	public HashMap<String, ArrayList<UsersModel>> getAddressListFromBCAddressBook(int buyingCompanyId, int userId);
	
	public HashMap<String, UsersModel> getUserAddressFromBCAddressBook(int billId,int shipId);
	
	public String createRetailUser(AddressModel addressModel);
	
	public String createCommertialCustomer(AddressModel addressModel);
	
	public UsersModel getAccountDetail(LinkedHashMap<String, String> accountInquiryInput);
	
	public String editBillingAddress(UsersModel userInfo);
	
	public String editShippingAddress(UsersModel userInfo);
	
	public String addNewShippingAddress(UsersModel userInfo);
	
	public String updatePrevilege(UsersModel userInfo);
	
	public String createNewAgent(AddressModel userInfo);
	
	public LinkedHashMap<String, String> getErpData(UsersModel userInfo,LinkedHashMap<String,String> erpData);
	
	public UsersModel getRoleFromErp(UsersModel userInfo);
	
	public void assignErpValues(UsersModel userInfo);
	
	public String contactUpdate(UsersModel userInfo,HttpSession session);
	
	public ArrayList<ShipVia> getShipViaList(UsersModel customerInfoInput);
	
	public ArrayList<ShipVia> getUserShipViaList(UsersModel customerInfoInput);

	public UsersModel contactEdit(UsersModel customerInfoInput,UsersModel addressDetail);
	
	public void checkERPConnection(UsersModel customerInfoInput);
	
	public HashMap<String, ArrayList<UsersModel>> getAgentAddressListFromBCAddressBook(UsersModel customerInfoInput);
	
	public UsersModel getCatalogfromERP(UsersModel customerInfoInput);
	
	public void scynEntityAddress(UsersModel customerInfoInput);
	
	public String createWLUser(UsersModel userDetails);
	
	public String UpdateWLUserAddress(UsersModel userDetails);
	
	public String getCustomerDataFromERP(UsersModel userDetails);
	
	public int getLastOrderNumber(UsersModel userDetails);

	public UsersModel getTravelPoints(UsersModel userInfo);
	
	public UserManagementModel getCustomerDataCredit(UsersModel customerInfoInput);
	
	public UserManagementModel getCustomerBalance(UsersModel customerInfoInput);
	
	public UserManagementModel getCustomerBalanceV2(UsersModel customerInfoInput);
	
	public UserManagementModel getCustomerDataGeneralV2(UsersModel customerInfoInput);
	
	public ArrayList<UserManagementModel> getARGetInvoiceListV2(UsersModel customerInfoInput);
	
	public Cimm2BCentralARBalanceSummary getARBalanceForCimm2BCentral(UsersModel customerInfoInput);
	
	public Cimm2BCentralCylinderBalanceSummary getCylinderBalanceForCimm2BCentral(UsersModel customerInfoInput);
	
	public ArrayList<UserManagementModel> getARBalanceDetailsForCimm2BCentral(UsersModel customerInfoInput);
	
	public UsersModel contactInquiry(String contactId);
	
	public String getShipToStoreWarehouseCode(String warehouse);
	
	public String requestAcceptHostedFormToken(LinkedHashMap<String, String> tokenRequest);

	public UsersModel getIframeAccessToken(UsersModel tokenInputParameter);
	
	public String checkforUserNameInERP(UsersModel inputData);
	
	public String validateEmailfromESB(String userName, String AuthCode, String erpOverrideFlag);

	public String updatePassword(UsersModel userInfo, HttpSession session);
	
	public String changeEmail(UsersModel userInfo, HttpSession session);
	
	public Cimm2BCustomer getUserInfoFromErp(UsersModel userDetails);
}
