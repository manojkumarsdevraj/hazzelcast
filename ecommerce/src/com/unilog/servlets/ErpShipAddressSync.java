package com.unilog.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.commons.lang.StringUtils;
import com.erp.service.UserManagement;
import com.erp.service.impl.UserManagementImpl;
import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.products.ImagesModel;
import com.unilog.security.SecureData;
import com.unilog.servlets.model.CustomerListModel;
import com.unilog.servlets.model.CustomerModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

/**
 * Servlet implementation class ErpShipAddressSync
 */
public class ErpShipAddressSync extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String refreshStatus = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ErpShipAddressSync() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 HttpSession session = request.getSession();
		 
		 Connection  conn = null;
		 OutputStream os = response.getOutputStream();
		 //response.addHeader("Access-Control-Allow-Origin", "*");
		try
		{
			if(refreshStatus==null)
				refreshStatus = "Completed";
			String userName = request.getParameter("username");
			String password = request.getParameter("password");
			String status = request.getParameter("status");
			String customerIdList = request.getParameter("customerIdList");
			boolean listFromParam = false;
			String outputString = "";
			int i = 1;
			
			if(CommonUtility.validateString(status).trim().equalsIgnoreCase(""))
				status = "N";
			 session.setAttribute(Global.USERNAME_KEY, userName);
			conn = ConnectionManager.getDBConnection();
			
			if(customerIdList!=null && !customerIdList.trim().equalsIgnoreCase("")){
				String custList[] = customerIdList.split(",");
				customerIdList = StringUtils.join(custList,",");
				listFromParam = true;
			}
			boolean isValidUser = validateUser(conn, userName, password);
			//StringUtils.join(slist, ',');
        	if(isValidUser && !refreshStatus.trim().contains("Processing") && status.trim().equalsIgnoreCase("N")){
        		outputString = "Getting customer information";
        		os.write(outputString.getBytes());
        		System.out.println("Sync Log : " + outputString);
        		//response.getWriter().println(outputString);
        		List<CustomerModel> customerList = getErpTokenInfo(conn,customerIdList,listFromParam);
        		if(customerList!=null && customerList.size()>0)
        		{
        			UsersModel customerInfoInput = null;
        			refreshStatus = "Processing Request";
        			outputString = refreshStatus;
            		os.write(outputString.getBytes());
            		System.out.println("Sync Log : " + outputString);
            		
            		//response.getWriter().println(outputString);
        			for(CustomerModel customer:customerList){
        				refreshStatus = "Processing "+i+" of "+customerList.size()+" Request";
        				outputString = refreshStatus;
                		os.write(outputString.getBytes());
                		System.out.println("Sync Log : " + outputString);
                		//response.getWriter().println(outputString);
        				customerInfoInput = new UsersModel();
            			session.setAttribute("userToken", customer.getEntityId());
           			 	session.setAttribute("customerId", customer.getEntityId());
           			 	session.setAttribute("buyingCompanyId", customer.getBuyingCompanyId());
           			 	
            			customerInfoInput.setSession(session);
            			customerInfoInput.setUserToken(customer.getEntityId());
            			customerInfoInput.setCustomerId(CommonUtility.validateString((customer.getEntityId())));
            			scynEntityAddress(customerInfoInput);
            			 i++;
        			}
        			refreshStatus = "Process Completed";
        			outputString = "Process Completed";
            		os.write(outputString.getBytes());
            		System.out.println("Sync Log : " + outputString);
            		//response.getWriter().println(outputString);
        		}else{
        			outputString = "No Customer records";
        			refreshStatus = outputString;
            		os.write(outputString.getBytes());
            		System.out.println("Sync Log : " + outputString);
            		//response.getWriter().println(outputString);
        		}
        		
    			
        	}
        	else
        	{
        		if(!isValidUser){
        			outputString = "Invalid user credentials";
        			refreshStatus = outputString;
        		os.write(outputString.getBytes());
        		System.out.println("Sync Log : " + outputString);
        		//response.getWriter().println(outputString);
        		}else{
        			outputString = refreshStatus;
            		os.write(outputString.getBytes());
            		System.out.println("Sync Log : " + outputString);
            		//response.getWriter().println(outputString);
        		}
        	}
        	/*if(rs.next())
        	{
        		
        	}*/
        	
		}catch (Exception e) {
			refreshStatus = "Error While importing Shipping address.";
			e.printStackTrace();
		}finally{
			
			os.write(refreshStatus.getBytes());
			ConnectionManager.closeDBConnection(conn);
			os.flush();
    		os.close();
		}
	}
	
	private boolean validateUser(Connection conn,String userName,String password){
		boolean isValidUser = false;
		 PreparedStatement preStat=null;
		 ResultSet rs = null;
		try{
			String sql = "SELECT USER_ID FROM CIMM_USERS WHERE USER_NAME=? AND PASSWORD=?";
        	preStat = conn.prepareStatement(sql);
        	preStat.setString(1, userName);
        	preStat.setString(2, password);
        	rs = preStat.executeQuery();
        	if(rs.next()){
        		isValidUser = true;
        	}
		}catch(Exception e){
			
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(preStat);
		}
		return isValidUser;
	}
	
	private List<CustomerModel> getErpTokenInfo(Connection conn,String customerIds, boolean listFromParam){
		 PreparedStatement preStat=null;
		 ResultSet rs = null;
		 String jsonString = "";
		 List<CustomerModel> customerList = null;
		 CustomerListModel output = null;
		 Gson gson = new Gson();
		try
		{
			String sql = "select '{\"customerList\":['||result||']}' CUSTOMER_LIST from (SELECT DBMS_XMLGEN.CONVERT(RTRIM(XMLAGG(XMLELEMENT(BC,'{\"buyingCompanyId\" : \"'||BUYING_COMPANY_ID||'\",\"entityId\" : \"'||ENTITY_ID||'\"}'|| ', ')).EXTRACT( '//text()' ).getClobVal(), ', '), 1) result FROM BUYING_COMPANY WHERE status!='D')";
			if(listFromParam){
			sql = "select '{\"customerList\":['||result||']}' CUSTOMER_LIST from (SELECT DBMS_XMLGEN.CONVERT(RTRIM(XMLAGG(XMLELEMENT(BC,'{\"buyingCompanyId\" : \"'||BUYING_COMPANY_ID||'\",\"entityId\" : \"'||ENTITY_ID||'\"}'|| ', ')).EXTRACT( '//text()' ).getClobVal(), ', '), 1) result FROM BUYING_COMPANY WHERE status!='D' and ENTITY_ID in(?))";
		}
			preStat = conn.prepareStatement(sql);
			if(listFromParam)
				preStat.setString(1, customerIds);
			rs = preStat.executeQuery();
        	if(rs.next()){
        		jsonString = rs.getString("CUSTOMER_LIST");
    			output = gson.fromJson(jsonString, CustomerListModel.class);
    			customerList = output.getCustomerList();
        	}
        	
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(preStat);
		}
		return customerList;
	}
	
	public void scynEntityAddress(UsersModel customerInfoInput) {
		
		String erp = "defaults";
		if(CommonDBQuery.getSystemParamtersList().get("ERP")!=null && CommonDBQuery.getSystemParamtersList().get("ERP").trim().length()>0){
			erp = CommonDBQuery.getSystemParamtersList().get("ERP").trim();
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

	public static void setRefreshStatus(String refreshStatus) {
		ErpShipAddressSync.refreshStatus = refreshStatus;
	}

	public static String getRefreshStatus() {
		return refreshStatus;
	}

}
