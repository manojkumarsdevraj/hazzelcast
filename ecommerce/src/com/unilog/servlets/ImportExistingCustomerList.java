package com.unilog.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpStatus;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthenticationRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAuthenticationResponse;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralGetInvoiceList;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralRequestParams;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.impl.UserManagementImpl;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.CustomFieldModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.security.LoginAuthentication;
import com.unilog.security.SecureData;
import com.unilog.users.AddressModel;
import com.unilog.users.NewUserRegisterUtility;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

/**
 * Servlet implementation class ImportExistingCustomerList
 */
public class ImportExistingCustomerList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImportExistingCustomerList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		
		 HttpSession session = request.getSession();
		 String s = (String)session.getAttribute("erpType");
		 System.out.println("Session :" +s);
		  String filePath = CommonDBQuery.getSystemParamtersList().get("IMPORT_CUSTOMER_LIST");
		 	//String filePath = "D://RegisterUse.xlsx";
		    	if(CommonUtility.validateString(filePath).length()>0){
		    		readRecordsFromFile(filePath,session);
		    	}
	}
	
		public static void readRecordsFromFile(String filePath,HttpSession session){
	    	
	    	ArrayList<UsersModel> userDetails = new ArrayList<UsersModel>();
	        try {
	        	File excel = new File(filePath);
	        	//File excel = new File("E:\\RegisterUser.xlsx");
	            FileInputStream fis = new FileInputStream(excel);
	            XSSFWorkbook book = new XSSFWorkbook(fis);
	            XSSFSheet sheet = book.getSheetAt(0);

	            Iterator<Row> itr = sheet.iterator();
	         
	            // Iterating over Excel file in Java
	            while (itr.hasNext()) {
	            	
	                Row row = itr.next();	               
	            
	                // Iterating over each column of Excel file
	                Iterator<Cell> cellIterator = row.cellIterator();
	                System.out.println("row number : " + row.getRowNum());
	               if(row.getRowNum() >= 0){
	            	   UsersModel registerDetails=new UsersModel();
	            	   while (cellIterator.hasNext()) {
		                	
		                    Cell cell = cellIterator.next();
		                    String cellValue = "";
		                    
		                    switch (cell.getCellType()) {
		                    case Cell.CELL_TYPE_STRING:
		                    	
		                        System.out.print(cell.getColumnIndex()+" - "+cell.getStringCellValue() + "\t");
		                        cellValue = CommonUtility.validateString(cell.getStringCellValue());
		                        if(cell.getColumnIndex()==0){
		                        	registerDetails.setEmailAddress(cellValue);
		                        	
		                        }
		                        else if(cell.getColumnIndex()==1){
		                        	registerDetails.setPassword(cellValue);
		                      	
		                      }
		                        break;
		                }
		                   
		                }
		                registerDetails.setUserStatus("Y");
		                registerDetails.setRequestAuthorizationLevel("Ecomm Customer Auth Purchase Agent");
		                //registerDetails.setCustomFieldName("NEWSLETTER");
		                registerDetails.setSession(session);
		                userDetails.add(registerDetails);
	               }
	                
	            }
	            
	            
	            
	                if(userDetails!=null && userDetails.size()>0){
	               for(UsersModel userDetailsInput : userDetails){
	            	   if(userDetailsInput!=null){
	            			String result = "";
	            			Connection conn=null;
	            			String userNumber=null;
	            			try {
	            				Cimm2BCentralAuthenticationRequest requestObj = new Cimm2BCentralAuthenticationRequest();
	            				requestObj.setUsername(userDetailsInput.getEmailAddress());
	            				requestObj.setPassword(userDetailsInput.getPassword());
	            				String AUTHENTICATION_REQUEST_API = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("AUTHENTICATION_REQUEST_API"));
	            				Cimm2BCentralResponseEntity authenticationResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(AUTHENTICATION_REQUEST_API, HttpMethod.POST, requestObj, Cimm2BCentralAuthenticationResponse.class);
	            				
	            				Cimm2BCentralAuthenticationResponse authResponseObj = null;
	            				if(authenticationResponseEntity!=null && authenticationResponseEntity.getData() != null && authenticationResponseEntity.getStatus() != null && authenticationResponseEntity.getStatus().getCode() == HttpStatus.SC_OK){
	            					authResponseObj = (Cimm2BCentralAuthenticationResponse) authenticationResponseEntity.getData();
	            					userDetailsInput.setUserERPId(authResponseObj.getUserERPId());
	            					userDetailsInput.setAccountName(authResponseObj.getCustomerERPId());
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
		            				String CompanyName=null;
		            				boolean userExist = false;
		            				boolean validInvoice = false;
		            				String invoiceNo = userDetailsInput.getInvoiceNumber();
		            				String poNumber = userDetailsInput.getPoNumber()!=null?userDetailsInput.getPoNumber():"";
		            				String zipCode = userDetailsInput.getZipCode();
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

		            				if(customerDetailsResponseEntity!=null && customerDetailsResponseEntity.getData() != null &&  customerDetailsResponseEntity.getStatus().getCode() == 200 ){  

		            					customerDetails = (Cimm2BCentralCustomer) customerDetailsResponseEntity.getData();

		            					if(customerDetails != null){
		            						String entityid="0";
		            						String billEntityId = "0";
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

		            						customerinfo.setEmailAddress(userDetailsInput.getEmailAddress());
		            						customerinfo.setTermsType(customerDetails.getTermsType());
		            						customerinfo.setTermsTypeDesc(customerDetails.getTermsTypeDescription());
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
		            		
		            									if(shipTo.getContacts() != null && customerDetails.getContacts().size() > 0 && CommonUtility.validateString(customerDetails.getContacts().get(0).getPrimaryPhoneNumber()).length() > 0){
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
		            								userNumber=userDetailsInput.getUserERPId();
		            							
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
		            							updateuserdata(userDetailList);
		            							
		            							}
		            							/*if(buyingCompanyid > 0){						           
		            								customerinfo.setBuyingCompanyId(buyingCompanyid);
		            								customerinfo.setContactId(CommonUtility.validateString(account));
		            								customerinfo.setUserStatus(userDetailsInput.getUserStatus());
		            								userDetailsInput.setBuyingCompanyId(buyingCompanyid);
		            								userDetailsInput.setContactId(CommonUtility.validateString(account));
		            								userDetailsInput.setUserStatus(userDetailsInput.getUserStatus());
		            								userDetailsInput.setTermsType(CommonUtility.validateString(customerDetails.getTermsType()));
		            								userDetailsInput.setTermsTypeDesc(CommonUtility.validateString(customerDetails.getTermsTypeDescription()));
		            								userDetailsInput.setFirstName(userDetailList.getFirstName()!=null?userDetailList.getFirstName():"");
		            								userDetailsInput.setLastName(userDetailList.getLastName()!=null?userDetailList.getLastName():"");
		            								userDetailsInput.setPhoneNo(userDetailList.getPhoneNo()!=null?userDetailList.getPhoneNo():"");
		            								userDetailsInput.setAddress1(userDetailList.getAddress1()!=null?userDetailList.getAddress1():"");
		            								userDetailsInput.setAddress2(userDetailList.getAddress2()!=null?userDetailList.getAddress2():"");
		            								userDetailsInput.setCity(userDetailList.getCity()!=null?userDetailList.getCity():"");
		            								userDetailsInput.setState(userDetailList.getState()!=null?userDetailList.getState():"");
		            								userDetailsInput.setZipCode(userDetailList.getZipCode()!=null?userDetailList.getZipCode():"");
		            								userDetailsInput.setCountry(userDetailList.getCountry()!=null?userDetailList.getCountry():"US");
		            								if(userNumber!=null && CommonUtility.validateString(userNumber).length()>0){
		            								userDetailsInput.setUserERPId(CommonUtility.validateString(userNumber));
		            								}
		            								userId = UsersDAO.insertNewUser(conn,userDetailsInput, account, password, buyingCompanyid, "0",false);
		            							}else{
		            								result = "Warehouse not found. Error While Registering. Contact our Customer Service for Further Assistance.|";
		            							}*/
		            						/*	if(userId>0){    
		            								int shipId= 0;  

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
		            										//shipId = UsersDAO.insertNewAddress(conn, customerinfo, userId, "Ship");
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
		            								
		            								UsersDAO.insertCustomField(CommonUtility.validateString(codCustomFiledValue), "COD", userId, buyingCompanyid, "BUYING_COMPANY");
		            								UsersDAO.insertCustomField(CommonUtility.validateString(poRequiredCustomFiledValue), "PO_REQUIRED", userId, buyingCompanyid, "BUYING_COMPANY");
		            								UsersDAO.insertCustomField(CommonUtility.validateString(gasPoRequiredCustomFiledValue), "GAS_PO_REQUIRED", userId, buyingCompanyid, "BUYING_COMPANY");
		            								UsersDAO.insertCustomField(CommonUtility.validateString(defaultShipVia), "SHIPPING_METHOD", userId, buyingCompanyid, "BUYING_COMPANY");
		            								UsersDAO.insertCustomField(CommonUtility.validateString(fuelSurcharge), "FUEL_SURCHARGE", userId, buyingCompanyid, "BUYING_COMPANY");
		            								UsersDAO.insertCustomField(CommonUtility.validateParseDoubleToString(minimumOrderAmount), "MINIMUM_ORDER_AMOUNT", userId, buyingCompanyid, "BUYING_COMPANY");
		            								UsersDAO.insertCustomField(CommonUtility.validateParseDoubleToString(prepaidFreightAmount), "PREPAID_FREIGHT_TARGET_AMOUNT", userId, buyingCompanyid, "BUYING_COMPANY");
		            								UsersDAO.insertCustomField(CommonUtility.validateString(priceListPath), "MY_PRICING_LINK_ONE", userId, buyingCompanyid, "BUYING_COMPANY");
		            								UsersDAO.insertCustomField(CommonUtility.validateString(fullPriceListPath), "MY_PRICING_LINK_TWO",userId, buyingCompanyid, "BUYING_COMPANY");
		            								if(CommonUtility.customServiceUtility()!=null) { 
		            									CommonUtility.customServiceUtility().insertCustomertarget(warehouse,userId,buyingCompanyid); 
		            								} //CustomServiceProvider
		            								
		            							}else{
		            								result = "Error While Registering. Contact our Customer Service for Further Assistance.|";
		            							}*/
		            						}
		            					}
		            					}else{
		            						result="Invalid Invoice, Zip code and PO Number. Please add valid invoice number or zip code or PO Number.";
		            					}
		            					}else{
		            						result="Invalid Invoice or invoice does not match in the ERP. Please add the recent invoice.";
		            					}
		            				}else{
		            					result = customerDetailsResponseEntity.getStatus().getMessage();
		            					
		            				}
	            				}
	            				
	            				
	            			}catch (Exception e) {
	            				result = "Error While Registering. Contact our Customer Service for Further Assistance.|";
	            				
	            				e.printStackTrace();
	            			}
	            			finally{
	            				ConnectionManager.closeDBConnection(conn);
	            			}
	            		
	            	   }
	            
	                	}
	               System.out.println("******************************************** File Read Completed  ********************************************************* \n" );
	                }
	            
	        } catch (FileNotFoundException fe) {
	            fe.printStackTrace();
	        } catch (IOException ie) {
	            ie.printStackTrace();
	        }
	    
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
					
				}
			}
			catch (Exception e) {

				e.printStackTrace();
			}
			
			return userDetailList;

		}
		
		
		public static void updateuserdata(UsersModel userDetailList) throws SQLException {
			int count=0;
			
			Connection  conn = null;
		    PreparedStatement pstmt=null;
		    String sql = "";
		    String country = "";
		    try{
			conn = ConnectionManager.getDBConnection();
            sql = "UPDATE CIMM_USERS SET FIRST_NAME=?,LAST_NAME=?,ADDRESS1=?, ADDRESS2=?, CITY=?, STATE=?,ZIP=?,COUNTRY=?,EMAIL=?,USER_NAME=?, OFFICE_PHONE=?, STATUS=?, JOB_TITLE=? WHERE USER_NAME=?";
            pstmt = conn.prepareStatement(sql);
            if(userDetailList.getCountry()!=null && !userDetailList.getCountry().trim().equals("")){
				country = CommonUtility.getCountryCode(userDetailList.getCountry(), "UpdateUserData");
			}
            //String securePassword = SecureData.getPunchoutSecurePassword(userDetailList.getChangedPassword());
            pstmt.setString(1, userDetailList.getFirstName());
            pstmt.setString(2, userDetailList.getLastName());
            pstmt.setString(3, userDetailList.getAddress1());
            pstmt.setString(4, userDetailList.getAddress2());
            pstmt.setString(5, userDetailList.getCity());
            pstmt.setString(6, userDetailList.getState());
            pstmt.setString(7, userDetailList.getZipCode());
            pstmt.setString(8, country);
            if(userDetailList.getEmailAddress() !=null) {
            	pstmt.setString(9, userDetailList.getEmailAddress());
                pstmt.setString(10, userDetailList.getEmailAddress());
            }else {
            	pstmt.setString(9, userDetailList.getLoginID());
                pstmt.setString(10, userDetailList.getLoginID());
            }
            pstmt.setString(9, userDetailList.getEmailAddress());
            if(userDetailList.getEmailAddress() !=null)
            pstmt.setString(10, userDetailList.getEmailAddress());
           /* if(securePassword!=null){
            	  pstmt.setString(11, securePassword);
            }*/
          
            pstmt.setString(11, userDetailList.getPhoneNo());
          
            pstmt.setString(12, "Y");
            pstmt.setString(13, userDetailList.getJobTitle());
           /* if(userDetailList.getEmailAddress() !=null) {
            pstmt.setString(14, userDetailList.getEmailAddress());
            }else
            {*/
            	pstmt.setString(14, userDetailList.getEmailAddress());
           // }
            count = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally {	    	 
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
	      
			
	}			

}
