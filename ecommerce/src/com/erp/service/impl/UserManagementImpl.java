package com.erp.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralARBalanceSummary;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerData;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCylinderBalanceSummary;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralNearestWareHouseResponse;
import com.erp.service.model.UserManagementModel;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCustomer;
import com.unilog.database.CommonDBQuery;
import com.unilog.users.AddressModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class UserManagementImpl implements UserManagement{
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getUserToken(String userName, String password) {
		return null;
	}

	public String createRetailUser(AddressModel addressModel)
	{
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//Override ERP to defaults. To override add "erpOverrideFlag" hidden field in the register.html
		if(CommonUtility.validateString(addressModel.getErpOverrideFlag()).equalsIgnoreCase("Y")){
			erp = "defaults";
		}
		else if (CommonUtility.validateString(addressModel.getErpOverrideFlag()).length()>0) {
			erp = CommonUtility.validateString(addressModel.getErpOverrideFlag());
		} 
		String resultVal = "";
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = AddressModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("createRetailUser", paramObject);
			resultVal = (String) method.invoke(obj, addressModel);
			if(CommonUtility.validateString(resultVal).trim().equalsIgnoreCase(""))
				resultVal = "Registration Failed.";
			
		}
		catch (Exception e) {
			
			resultVal = "Registration Failed.";
			e.printStackTrace();
		}
		return resultVal;
	}
	
	public String createCommertialCustomer(AddressModel addressModel) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		  if(!CommonUtility.validateString(addressModel.getErpOverrideFlag()).equalsIgnoreCase("N") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y")){
			  erp = "defaults";
		  }
		String resultVal = "";
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = AddressModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("createCommertialCustomer", paramObject);
			resultVal = (String) method.invoke(obj, addressModel);
			if(CommonUtility.validateString(resultVal).trim().equalsIgnoreCase(""))
				resultVal = "Registration Failed.";
			
		}
		catch (Exception e) {
			
			resultVal = "Registration Failed.";
			e.printStackTrace();
		}
		return resultVal;
	}
	
	public String insertUser(UsersModel billAddressList, UsersModel shipAddressList, String shipvarity){
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		//String parameter
		Class<?>[] paramObject = new Class[3];	
		paramObject[0] = UsersModel.class;
		paramObject[1] = UsersModel.class;
		paramObject[2] = String.class;
		
		Object[] arguments = new Object[3];
		arguments[0] = billAddressList;
		arguments[1] = shipAddressList;
		arguments[2] = shipvarity;
		
		String custNumber = "0";
		
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("insertUser", paramObject);
			custNumber = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return custNumber;
	}

	public String newRetailUserRegistration(String customerNumber, String country, String customerPass, String locUser, String userRole, String buyingCompanyIdStr, String parentUserId, String firstName, String lstName, String status, UsersModel contactInformation, boolean isFrmRegPage, boolean updateRole, UsersModel customerInfo) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		//String parameter
		Class<?>[] paramObject = new Class[14];	
		paramObject[0] = String.class;
		paramObject[1] = String.class;
		paramObject[2] = String.class;
		paramObject[3] = String.class;
		paramObject[4] = String.class;
		paramObject[5] = String.class;
		paramObject[6] = String.class;
		paramObject[7] = String.class;
		paramObject[8] = String.class;
		paramObject[9] = String.class;
		paramObject[10] = UsersModel.class;
		paramObject[11] = boolean.class;
		paramObject[12] = boolean.class;
		paramObject[13] = UsersModel.class;
		
		Object[] arguments = new Object[14];
		arguments[0] = customerNumber;
		arguments[1] = country;
		arguments[2] = customerPass;
		arguments[3] = locUser;
		arguments[4] = userRole;
		arguments[5] = buyingCompanyIdStr;
		arguments[6] = parentUserId;
		arguments[7] = firstName;
		arguments[8] = lstName;
		arguments[9] = status;
		arguments[10] = contactInformation;
		arguments[11] = isFrmRegPage;
		arguments[12] = updateRole;
		arguments[13] = customerInfo;
		
		String resultVal = "";
		
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("newRetailUserRegistration", paramObject);
			resultVal = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return resultVal;
	}

public String newOnAccountUserRegistration(UsersModel userDetailsInput) {
		
		String erp = "defaults";
		HttpSession session = userDetailsInput.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		//String parameter
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = UsersModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = userDetailsInput;
		
		
		String resultVal = "";
		
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("newOnAccountUserRegistration", paramObject);
			resultVal = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultVal;
	}

	public String existingUserRegistration(UsersModel customerInfoInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = UsersModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = customerInfoInput;
		
		
		String resultVal = "";
		
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("existingUserRegistration", paramObject);
			resultVal = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return resultVal;
		
	}

	public HashMap<String, ArrayList<UsersModel>> getAddressListFromBCAddressBook(
			int buyingCompanyId, int userId) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y")){
			  erp = "defaults";
		}
		//String parameter
		Class<?>[] paramObject = new Class[2];	
		paramObject[0] = int.class;
		paramObject[1] = int.class;
		
		Object[] arguments = new Object[2];
		arguments[0] = buyingCompanyId;
		arguments[1] = userId;
		
		HashMap<String, ArrayList<UsersModel>> shipAddressList = new HashMap<String, ArrayList<UsersModel>>();
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getAddressListFromBCAddressBook", paramObject);
			shipAddressList = (HashMap<String, ArrayList<UsersModel>>) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return shipAddressList;
	}
	
	public HashMap<String, UsersModel> getUserAddressFromBCAddressBook(int billId,int shipId){
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		//String parameter
		Class<?>[] paramObject = new Class[2];	
		paramObject[0] = int.class;
		paramObject[1] = int.class;
		
		Object[] arguments = new Object[2];
		arguments[0] = billId;
		arguments[1] = shipId;
		
		HashMap<String, UsersModel> userAddress = new HashMap<String, UsersModel>();
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getUserAddressFromBCAddressBook", paramObject);
			userAddress = (HashMap<String, UsersModel>) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return userAddress;
	}

	@Override
	public UsersModel getAccountDetail(LinkedHashMap<String, String> accountInquiryInput) {
		
		UsersModel accountDetail = new UsersModel();
		String erp = "eclipse";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = LinkedHashMap.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("accountDetail", paramObject);
			accountDetail = (UsersModel) method.invoke(obj, accountInquiryInput);
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return accountDetail;
	}
	
	public String editBillingAddress(UsersModel userInfo) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if(!CommonUtility.validateString(userInfo.getErpOverrideFlag()).equalsIgnoreCase("N") && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y") || CommonUtility.validateString(userInfo.getExtraParam()).equalsIgnoreCase("Y")){
			  erp = "defaults";
		  }
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
				
		Object[] arguments = new Object[1];
		arguments[0] = userInfo;
		
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("editBillingAddress", paramObject);
			result = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String editShippingAddress(UsersModel shipInfo) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
				
		Object[] arguments = new Object[1];
		arguments[0] = shipInfo;

		
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("editShippingAddress", paramObject);
			result = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String addNewShippingAddress(UsersModel shipInfo) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		  if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y")){
			  erp = "defaults";
		  }
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
				
		Object[] arguments = new Object[1];
		arguments[0] = shipInfo;
		
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("addNewShippingAddress", paramObject);
			result = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public String updatePrevilege(UsersModel userInfo) {
		

		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
				
		Object[] arguments = new Object[1];
		arguments[0] = userInfo;
		
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("updatePrevilege", paramObject);
			result = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	
	}
	public String contactUpdate(UsersModel userInfo,HttpSession session) {
		

		String erp = "defaults";
		request =ServletActionContext.getRequest();
		session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		if (CommonUtility.validateString(userInfo.getErpOverrideFlag()).length()>0) {
			erp = CommonUtility.validateString(userInfo.getErpOverrideFlag());
		} 
		Class<?>[] paramObject = new Class[2];
		paramObject[0] = UsersModel.class;
		paramObject[1] = HttpSession.class;
				
		Object[] arguments = new Object[2];
		arguments[0] = userInfo;
		arguments[1] = session;
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("contactUpdate", paramObject);
			result = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public UsersModel contactInquiry(UsersModel userInfo,HttpSession session) {
		

		String erp = "defaults";
		request =ServletActionContext.getRequest();
		session = request.getSession();
		UsersModel userDetails=new UsersModel();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[2];
		paramObject[0] = UsersModel.class;
		paramObject[1] = HttpSession.class;
				
		Object[] arguments = new Object[2];
		arguments[0] = userInfo;
		arguments[1] = session;
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("contactInquiry", paramObject);
			userDetails = (UsersModel) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return userDetails;
	}

	
	public String createNewAgent(AddressModel userInfo) {
		

		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = AddressModel.class;
				
		Object[] arguments = new Object[1];
		arguments[0] = userInfo;
		
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("createNewAgent", paramObject);
			result = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	
	}

	
	public LinkedHashMap<String, String> getErpData(UsersModel userInfo,
			LinkedHashMap<String, String> erpData) {
		
		
		String erp = "defaults";
		if(CommonDBQuery.getSystemParamtersList().get("ERP")!=null && CommonDBQuery.getSystemParamtersList().get("ERP").trim().length()>0){
			erp = CommonDBQuery.getSystemParamtersList().get("ERP").trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[2];
		paramObject[0] = UsersModel.class;
		paramObject[1] = LinkedHashMap.class;
				
		Object[] arguments = new Object[2];
		arguments[0] = userInfo;
		arguments[1] = erpData;
		
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getErpData", paramObject);
			erpData = (LinkedHashMap<String, String>) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return erpData;
		
	}

	@Override
	public UsersModel getRoleFromErp(UsersModel userInfo) {
		
		
		String erp = "defaults";
		UsersModel userInfoRes = new UsersModel();
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
		
		Object[] arguments = new Object[1];
		arguments[0] = userInfo;
		

		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getRoleFromErp", paramObject);
			userInfoRes = (UsersModel) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return userInfoRes;
		
	}

	@Override
	public void assignErpValues(UsersModel userInfo) {

		
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
		
		Object[] arguments = new Object[1];
		arguments[0] = userInfo;
		

		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("assignErpValues", paramObject);
			method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	
		
	}

	public ArrayList<ShipVia> getShipViaList(UsersModel customerInfoInput) {
		String erp = "defaults";
		ArrayList<ShipVia> shipVia = new ArrayList<ShipVia>();
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
		
		Object[] arguments = new Object[1];
		arguments[0] = customerInfoInput;

		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getShipViaList", paramObject);
			shipVia = (ArrayList<ShipVia>) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return shipVia;
	}
	public ArrayList<ShipVia> getUserShipViaList(UsersModel customerInfoInput) {
		String erp = "defaults";
		ArrayList<ShipVia> shipVia = new ArrayList<ShipVia>();
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
		
		Object[] arguments = new Object[1];
		arguments[0] = customerInfoInput;

		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getUserShipViaList", paramObject);
			shipVia = (ArrayList<ShipVia>) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return shipVia;
	}
	
	public UsersModel contactEdit(UsersModel customerInfoInput,UsersModel addressDetail) {
		String erp = "defaults";
		HttpSession session =customerInfoInput.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if(!CommonUtility.validateString(addressDetail.getErpOverrideFlag()).equalsIgnoreCase("N") && (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USE_DEFAULTS_ERP")).equalsIgnoreCase("Y") || CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RESTRICT_USER_UPDATE_ERP")).equalsIgnoreCase("Y"))){
			  erp = "defaults";
		 }
		if(CommonUtility.validateString(addressDetail.getErpOverrideFlag()).equalsIgnoreCase("cimmesb")){
			erp = "cimmesb";
		}
		UsersModel customerInfoOutput = null;
		//String parameter
		Class<?>[] paramObject = new Class[2];
		paramObject[0] = UsersModel.class;
		paramObject[1] = UsersModel.class;
		
		Object[] arguments = new Object[2];
		arguments[0] = customerInfoInput;
		arguments[1] = addressDetail;

		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("contactEdit", paramObject);
			customerInfoOutput = (UsersModel) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return customerInfoOutput;
		
	}

	public void checkERPConnection(UsersModel customerInfoInput) {
		String erp = "defaults";
		HttpSession session =customerInfoInput.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		Object[] arguments = new Object[0];
		
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("checkERPConnection");
			method.invoke(obj, arguments);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, ArrayList<UsersModel>> getAgentAddressListFromBCAddressBook(
			UsersModel customerInfoInput) {
		String erp = "defaults";
		HttpSession session =customerInfoInput.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		HashMap<String, ArrayList<UsersModel>> shipList = null;
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = customerInfoInput;
		

		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getAgentAddressListFromBCAddressBook", paramObject);
			shipList = (HashMap<String, ArrayList<UsersModel>>) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return shipList;
	}

	
	//<------------------------->
	public UsersModel getCatalogfromERP(UsersModel customerInfoInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = customerInfoInput;
		

		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getcatalogfromerp", paramObject);
			method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return customerInfoInput;
	}
	//<------------------------->

	
	public void scynEntityAddress(UsersModel customerInfoInput) {
		String erp = "defaults";
		
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
		
		Object[] arguments = new Object[1];
		arguments[0] = customerInfoInput;

		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("scynEntityAddress", paramObject);
			method.invoke(obj, arguments);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String createWLUser(UsersModel userDetails)
	{
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//Override ERP to defaults. To override add "erpOverrideFlag" hidden field in the guestCheckoutPage.html
		if(CommonUtility.validateString(userDetails.getErpOverrideFlag()).equalsIgnoreCase("Y")){
			erp = "defaults";
		}
		String resultVal = "";
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("createWLUser", paramObject);
			resultVal = (String) method.invoke(obj, userDetails);
			if(CommonUtility.validateString(resultVal).trim().length()==0)
				resultVal = "Registration Failed.";
			
		}
		catch (Exception e) {
			
			resultVal = "Registration Failed.";
			e.printStackTrace();
		}
		return resultVal;
	}
	public String UpdateWLUserAddress(UsersModel userDetails)
	{
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		String resultVal = "";
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("UpdateWLUserAddress", paramObject);
			resultVal = (String) method.invoke(obj, userDetails);
			if(CommonUtility.validateString(resultVal).trim().length()==0)
				resultVal = "Registration Failed.";
			
		}
		catch (Exception e) {
			
			resultVal = "Registration Failed.";
			e.printStackTrace();
		}
		return resultVal;
	}
	
	public String getCustomerDataFromERP(UsersModel userDetails)
	{
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		String resultVal = "";
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getCustomerDataFromERP", paramObject);
			resultVal = (String) method.invoke(obj, userDetails);
			
		}
		catch (Exception e) {
			
			resultVal = "Update Failed.";
			e.printStackTrace();
		}
		return resultVal;
	}
	public int getLastOrderNumber(UsersModel userDetails)
	{
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		int resultVal = 0;
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getLastOrderDetails", paramObject);
			resultVal = (Integer) method.invoke(obj, userDetails);
			
		}
		catch (Exception e) {
			
			resultVal = 0;
			e.printStackTrace();
		}
		return resultVal;
	}
	
	@Override
	public UsersModel getTravelPoints(UsersModel userInfo) {
		
		
		String erp = "defaults";
		UsersModel userInfoRes = new UsersModel();
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;
		
		Object[] arguments = new Object[1];
		arguments[0] = userInfo;
		

		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getTravelPoint", paramObject);
			userInfoRes = (UsersModel) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return userInfoRes;
		
	}
	
	public UserManagementModel getCustomerDataCredit(UsersModel userDetails)
	{
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		UserManagementModel userManagementModel = new UserManagementModel();
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("arGetCustomerDataCreditResponse", paramObject);
			userManagementModel = (UserManagementModel) method.invoke(obj, userDetails);
			
		}
		catch (Exception e) {
			
			userManagementModel = null;
			e.printStackTrace();
		}
		return userManagementModel;
	}

	
	public UserManagementModel getCustomerBalance(UsersModel customerInfoInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		UserManagementModel userManagementModel = new UserManagementModel();
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("arGetCustomerBalanceResponse", paramObject);
			userManagementModel = (UserManagementModel) method.invoke(obj, customerInfoInput);
			
		}
		catch (Exception e) {
			
			userManagementModel = null;
			e.printStackTrace();
		}
		return userManagementModel;
	}

	
	public UserManagementModel getCustomerBalanceV2(UsersModel customerInfoInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		UserManagementModel userManagementModel = new UserManagementModel();
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("arGetCustomerBalanceV2Response", paramObject);
			userManagementModel = (UserManagementModel) method.invoke(obj, customerInfoInput);
			
		}
		catch (Exception e) {
			
			userManagementModel = null;
			e.printStackTrace();
		}
		return userManagementModel;
	}


	public UserManagementModel getCustomerDataGeneralV2(UsersModel customerInfoInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		UserManagementModel userManagementModel = new UserManagementModel();
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("arGetCustomerDataGeneralV2Response", paramObject);
			userManagementModel = (UserManagementModel) method.invoke(obj, customerInfoInput);
			
		}
		catch (Exception e) {
			
			userManagementModel = null;
			e.printStackTrace();
		}
		return userManagementModel;
	}
	
	public ArrayList<UserManagementModel> getARGetInvoiceListV2(UsersModel customerInfoInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		ArrayList<UserManagementModel> userManagementModel = new ArrayList<UserManagementModel>();
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("ARGetInvoiceListV2", paramObject);
			userManagementModel = (ArrayList<UserManagementModel>) method.invoke(obj, customerInfoInput);
			
		}
		catch (Exception e) {
			
			userManagementModel = null;
			e.printStackTrace();
		}
		return userManagementModel;
	}
	public Cimm2BCentralARBalanceSummary getARBalanceForCimm2BCentral(UsersModel customerInfoInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		Cimm2BCentralARBalanceSummary arBalanceSummary = new Cimm2BCentralARBalanceSummary();
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getARBalanceForCimm2BCentral", paramObject);
			arBalanceSummary = (Cimm2BCentralARBalanceSummary) method.invoke(obj, customerInfoInput);
			
		}catch (Exception e) {
			
			arBalanceSummary = null;
			e.printStackTrace();
		}
		return arBalanceSummary;
	}
	@Override
	public Cimm2BCentralCylinderBalanceSummary getCylinderBalanceForCimm2BCentral(UsersModel customerInfoInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		Cimm2BCentralCylinderBalanceSummary cylinderBalanceSummaryList = new Cimm2BCentralCylinderBalanceSummary();
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;

			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getCylinderBalanceForCimm2BCentral", paramObject);
			cylinderBalanceSummaryList = (Cimm2BCentralCylinderBalanceSummary) method.invoke(obj, customerInfoInput);

		}catch (Exception e) {
			
			cylinderBalanceSummaryList = null;
			e.printStackTrace();
		}
		return cylinderBalanceSummaryList;
	}
	public ArrayList<UserManagementModel> getARBalanceDetailsForCimm2BCentral(UsersModel customerInfoInput) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		ArrayList<UserManagementModel> arBalanceDetails = new ArrayList<UserManagementModel>();
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getARBalanceDetailForCimm2BCentral", paramObject);
			arBalanceDetails = (ArrayList<UserManagementModel>) method.invoke(obj, customerInfoInput);
			
		}
		catch (Exception e) {
			
			arBalanceDetails = null;
			e.printStackTrace();
		}
		return arBalanceDetails;
	}
	public Cimm2BCentralNearestWareHouseResponse getNearestWareHouseForCimm2BCentral(String zipCode) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		Cimm2BCentralNearestWareHouseResponse warehouseDetails = null;
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = String.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getWareHouseBasedOnZipCode", paramObject);
			warehouseDetails = (Cimm2BCentralNearestWareHouseResponse) method.invoke(obj, zipCode);
			
		}
		catch (Exception e) {
			
			warehouseDetails = null;
			e.printStackTrace();
		}
		return warehouseDetails;
	}
	
	@Override
	public UsersModel contactInquiry(String contactId) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		UsersModel userDetailList = new UsersModel();
		try
		{
			Class<?>[] paramObject = new Class[1];
			paramObject[0] = String.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("contactInquiry", paramObject);
			userDetailList = (UsersModel) method.invoke(obj, contactId);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return userDetailList;

	}
	
	public String getShipToStoreWarehouseCode(String warehouse) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		//String parameter
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = String.class;
		Object[] arguments = new Object[1];
		arguments[0] = warehouse;
		
		String dcNumber = "";
		
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getShipToStoreWarehouseCode", paramObject);
			dcNumber = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return dcNumber;
	}
	
	public String requestAcceptHostedFormToken(LinkedHashMap<String, String> tokenRequest) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		
		//String parameter
		Class<?>[] paramObject = new Class[1];	
		paramObject[0] = LinkedHashMap.class;
		Object[] arguments = new Object[1];
		arguments[0] = tokenRequest;
		
		String dcNumber = "";
		
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("requestAcceptHostedFormToken", paramObject);
			dcNumber = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return dcNumber;
	}
	
	public Cimm2BCentralCustomerData searchCustomerInERP(UsersModel userDetails)
	{
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		Cimm2BCentralCustomerData customerData = null;
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("searchCustomersInERP", paramObject);
			customerData = (Cimm2BCentralCustomerData) method.invoke(obj, userDetails);
			
		}
		catch (Exception e) {
			customerData = null;
			e.printStackTrace();
		}
		return customerData;
	}
	@Override
	public UsersModel getIframeAccessToken(UsersModel tokenInputParameter) {
		String erp = "defaults";
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && CommonUtility.validateString((String) session.getAttribute("erpType")).length()>0){
			erp =  CommonUtility.validateString((String) session.getAttribute("erpType"));
		}
		if (CommonUtility.validateString(tokenInputParameter.getErpOverrideFlag()).length()>0) {
			erp = CommonUtility.validateString(tokenInputParameter.getErpOverrideFlag());
		} 
		UsersModel tokenOutputResponse = null;
		try {
			Class<?>[] paramObject = new Class[1];
			paramObject[0] = UsersModel.class;

			Class<?> cls = Class.forName("com.erp.service." + erp + ".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getIframeAccessToken", paramObject);
			tokenOutputResponse = (UsersModel) method.invoke(obj, tokenInputParameter);
		} catch (Exception e) {
			tokenOutputResponse = null;
			e.printStackTrace();
		}
		return tokenOutputResponse;
	}

	@Override
	public String checkforUserNameInERP(UsersModel inputData) {
		String erp = "defaults";
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && CommonUtility.validateString((String) session.getAttribute("erpType")).length()>0){
			erp = CommonUtility.validateString((String) session.getAttribute("erpType"));
		}
		if (CommonUtility.validateString(inputData.getErpOverrideFlag()).length()>0) {
			erp = inputData.getErpOverrideFlag();
		}
		String outputResponse = null;
		try
		{
			Class<?>[] paramObject = new Class[1];
			paramObject[0] = UsersModel.class;
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("checkForEmailInERP", paramObject);
			outputResponse =    (String) method.invoke(obj, inputData);
			
		}
		catch (Exception e) {
			outputResponse = null;
			e.printStackTrace();
		}
		return outputResponse;
	}

	@Override
	public String validateEmailfromESB(String userName, String AuthCode, String erpOverrideFlag) {
		String erp = "defaults";
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("erpType")!=null && CommonUtility.validateString((String) session.getAttribute("erpType")).length()>0){
			erp =  CommonUtility.validateString((String) session.getAttribute("erpType"));
		}
		if (erpOverrideFlag.length()>0) {
			erp = erpOverrideFlag;
		}
		String outputResponse = null;
		try
		{
			Class<?>[] paramObject = new Class[2];	
			paramObject[0] = String.class;
			paramObject[1] = String.class;
			
			Object[] arguments = new Object[2];
			arguments[0] = userName;
			arguments[1] = AuthCode;
			
				Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
				Object obj = cls.newInstance();
				Method method = cls.getDeclaredMethod("validateEmailAuthcode", paramObject);
				outputResponse =  (String) method.invoke(obj, arguments);
			}catch (Exception e) {
				outputResponse = null;
				e.printStackTrace();
			}
		return outputResponse;
	}

	@Override
	public String updatePassword(UsersModel userInfo, HttpSession session) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		session = request.getSession();
		if(CommonUtility.validateString((String) session.getAttribute("erpType")).length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		if (CommonUtility.validateString(userInfo.getErpOverrideFlag()).length()>0) {
			erp = CommonUtility.validateString(userInfo.getErpOverrideFlag());
		} 
		Class<?>[] paramObject = new Class[2];
		paramObject[0] = UsersModel.class;
		paramObject[1] = HttpSession.class;
				
		Object[] arguments = new Object[2];
		arguments[0] = userInfo;
		arguments[1] = session;
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("updatePassword", paramObject);
			result = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String woeContactUpdate(UsersModel userInfo,HttpSession session) {
		
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		session = request.getSession();
		if(session.getAttribute("erpType")!=null && session.getAttribute("erpType").toString().trim().length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		//String parameter
		if (CommonUtility.validateString(userInfo.getErpOverrideFlag()).length()>0) {
			erp = CommonUtility.validateString(userInfo.getErpOverrideFlag());
		} 
		Class<?>[] paramObject = new Class[2];
		paramObject[0] = UsersModel.class;
		paramObject[1] = HttpSession.class;
				
		Object[] arguments = new Object[2];
		arguments[0] = userInfo;
		arguments[1] = session;
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("woeContactUpdate", paramObject);
			result = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String changeEmail(UsersModel userInfo, HttpSession session) {
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		session = request.getSession();
		if(CommonUtility.validateString((String) session.getAttribute("erpType")).length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if (CommonUtility.validateString(userInfo.getErpOverrideFlag()).length()>0) {
			erp = CommonUtility.validateString(userInfo.getErpOverrideFlag());
		} 
		Class<?>[] paramObject = new Class[2];
		paramObject[0] = UsersModel.class;
		paramObject[1] = HttpSession.class;
				
		Object[] arguments = new Object[2];
		arguments[0] = userInfo;
		arguments[1] = session;
		String result = "";
		try{
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("changeEmail", paramObject);
			result = (String) method.invoke(obj, arguments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Cimm2BCustomer getUserInfoFromErp(UsersModel userDetails)
	{
		String erp = "defaults";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(CommonUtility.validateString((String) session.getAttribute("erpType")).length()>0){
			erp = session.getAttribute("erpType").toString().trim();
		}
		if (CommonUtility.validateString(userDetails.getErpOverrideFlag()).length()>0) {
			erp = CommonUtility.validateString(userDetails.getErpOverrideFlag());
		} 
		Cimm2BCustomer resultVal = new Cimm2BCustomer();
		try
		{
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = UsersModel.class;
			
			Class<?> cls = Class.forName("com.erp.service."+erp+".action.UserManagementAction");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getUserInfoFromErp", paramObject);
			resultVal = (Cimm2BCustomer) method.invoke(obj, userDetails);
			
		}
		catch (Exception e) {
			
			resultVal = null;
			e.printStackTrace();
		}
		return resultVal;
	}

}
