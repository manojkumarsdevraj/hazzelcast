package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.erp.service.LoginSubmitManagement;
import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralContact;
import com.erp.service.impl.LoginSubmitManagementImpl;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.LoginSubmitManagementModel;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.products.ProductsDAO;
import com.unilog.security.LoginAuthentication;
import com.unilog.security.SecureData;
import com.unilog.users.AddressModel;
import com.unilog.users.UserRegisterUtility;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WebLogin;
import com.unilog.utility.CommonUtility;


/**
 * Sample Java program to read and write Excel file in Java using Apache POI
 *
 */
public class XLSXReaderWriterP21 extends SecureData{
	
	
    public static void main(String[] args) {
    	String filePath = "D:\\Projects\\PssUserList2KPlus.xlsx";
    	String errorFilePath = "D:\\Projects\\";
    	
    	if(CommonUtility.validateString(filePath).length()>0){
    		readRecordsFromFile(filePath, errorFilePath);
    		
    	}
    	
    }
    
    

public static void readRecordsFromFile(String filePath, String errorFilePath){
    	ArrayList<RegistrationStandAlonModel> userDetails1 = null;
    	String continueSession="";
    	boolean loginSuccess=false;
    	
    	
        try {
        	StringBuilder valueFromFILESB = new StringBuilder("");
        	File excel = new File(filePath);
        	//File excel = new File("D:\\Projects\\testuserlogins.xlsx");
            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);
            

            Iterator<Row> itr = sheet.iterator();

            
            userDetails1 = new ArrayList<RegistrationStandAlonModel>();
            System.out.print("inside");
            // Iterating over Excel file in Java
            while (itr.hasNext()) {
                Row row = itr.next();
                RegistrationStandAlonModel registrationStandAlonModel = new RegistrationStandAlonModel();
                // Iterating over each column of Excel file
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    String cellValue = "";
                    
                    	
                        System.out.print(cell.getColumnIndex()+" - "+cell.getStringCellValue() + "\t");
                        
                        cellValue = CommonUtility.validateString(cell.getStringCellValue());
                        if(cell.getColumnIndex()==0){
                        	registrationStandAlonModel.setEmail(cellValue);
                        	
                        }else if(cell.getColumnIndex()==1){
                        	registrationStandAlonModel.setUserpassword(cellValue);
                        	
                        }
                        valueFromFILESB.append(CommonUtility.validateString(registrationStandAlonModel.getEmail())).append("\t").append(CommonUtility.validateString(registrationStandAlonModel.getUserpassword())).append("\n");
                    
                   /* switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                    	
                        System.out.print(cell.getColumnIndex()+" - "+cell.getStringCellValue() + "\t");
                        cellValue = CommonUtility.validateString(cell.getStringCellValue());
                        if(cell.getColumnIndex()==0){
                        	registrationStandAlonModel.setEmail(cellValue);
                        	
                        }else if(cell.getColumnIndex()==1){
                        	registrationStandAlonModel.setUserpassword(cellValue);
                        	
                        }
                        break;
                     case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t");
                        cellValue = CommonUtility.validateString(""+cell.getNumericCellValue());
                        if(cell.getColumnIndex()==0){
                        	registrationStandAlonModel.setEmail(cellValue);
                        	
                        }else if(cell.getColumnIndex()==1){
                        	registrationStandAlonModel.setUserpassword(cellValue);
                        	
                        }
                        break;
                    default:

                    }*/
                }
                System.out.println("-------------------------------------------------------");
                userDetails1.add(registrationStandAlonModel);
                System.out.println("");
            }
            
           System.out.println("******************************************** File Read Completed  *********************************************************");
            StringBuilder successSB = new StringBuilder("");
	        StringBuilder errorSB = new StringBuilder("");
	        StringBuilder userExistInDBSB = new StringBuilder("");
	        StringBuilder userAvaialbleInERPSB = new StringBuilder("");
	        StringBuilder userNotAvaialbleInERPSB = new StringBuilder("");
	        StringBuilder userLoginSuccessSB = new StringBuilder("");
	        StringBuilder userLoginFailSB = new StringBuilder("");
            if(userDetails1!=null && userDetails1.size()>0){
            	for(RegistrationStandAlonModel registrationStandAlonModel : userDetails1){
            		if(registrationStandAlonModel!=null){
            			HttpServletRequest request = ServletActionContext.getRequest();
                	    HttpSession session = request.getSession();
                	    String ipaddress = request.getHeader("X-Forwarded-For");
                	    if (ipaddress  == null)
                			ipaddress = request.getRemoteAddr();
                		System.out.println("At Login Page Ip : " + ipaddress);
                		boolean popUpRequest = false;
                		if(request.getParameter("loginType")!=null && request.getParameter("loginType").trim().equalsIgnoreCase("popup")){
                			 popUpRequest = true;
                		}
                	    
                		String browseType = (String) session.getAttribute("browseType");
                		
                		String result = "";
                	    String cartClear ="";
                	    
                	    boolean allowDBLogin =true;
                	    String isEclipseDown = "";
                	    Boolean loginAvailable = true;
                	    isEclipseDown = CommonDBQuery.getSystemParamtersList().get("ERPAVAILABLE");
            			String userToken = null;
            			String erpLogin = CommonDBQuery.getSystemParamtersList().get("ERPLOGIN");
            	          
            			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
            			LoginAuthentication loginAuthentication = new LoginAuthentication();
            			
            			LoginSubmitManagement loginSubmit = new LoginSubmitManagementImpl();
            			UsersDAO userUtilityDAO = new UsersDAO();
            			UsersModel enteredUserNameInfo = new UsersModel(); 
            			enteredUserNameInfo = userUtilityDAO.getUserStatus((CommonUtility.validateString(registrationStandAlonModel.getEmail())),true);
            			System.out.println("-------------->"+enteredUserNameInfo.getStatusDescription());
            			LoginSubmitManagementModel loginSubmitManagementModel = new LoginSubmitManagementModel();
            		
            			
            	    	loginSubmitManagementModel.setUserName(CommonUtility.validateString(registrationStandAlonModel.getEmail()));
            	    	loginSubmitManagementModel.setPassword(CommonUtility.validateString(registrationStandAlonModel.getUserpassword()));
            	    	
            	    	if((CommonUtility.validateString(registrationStandAlonModel.getEmail()).length()>0 && CommonUtility.validateString(registrationStandAlonModel.getUserpassword()).length()>0) && (enteredUserNameInfo.getStatusDescription()==null || enteredUserNameInfo.getStatusDescription().equalsIgnoreCase("Y")||enteredUserNameInfo.getStatusDescription().equalsIgnoreCase(""))){
            	    		loginAuthentication = new LoginAuthentication();
            	    		
            	        	if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
            	        		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y")){
            	        			loginSubmitManagementModel.setUserName(CommonUtility.validateString(registrationStandAlonModel.getEmail()).toUpperCase());
            	    				userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel); // User name UpperCase
            	    				
            	    				if(CommonUtility.validateString(userToken).length()==0){
            	    					UsersModel upperCheckUserDetail =  userUtilityDAO.getUserStatus((CommonUtility.validateString(registrationStandAlonModel.getEmail())),false);
            	    					
            	    					if(CommonUtility.validateString(upperCheckUserDetail.getUserName()).length()>0){
            	    						loginSubmitManagementModel.setUserName(CommonUtility.validateString(upperCheckUserDetail.getUserName()));
            	    						userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
            	    						
            	    						if(CommonUtility.validateString(userToken).length()==0 && !(CommonUtility.validateString(registrationStandAlonModel.getEmail())).equals(upperCheckUserDetail.getUserName())){
            	    							loginSubmitManagementModel.setUserName(CommonUtility.validateString((CommonUtility.validateString(registrationStandAlonModel.getEmail()))));
            	    							userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
            	    						}
            	    					}else{
            	    						loginSubmitManagementModel.setUserName(CommonUtility.validateString((CommonUtility.validateString(registrationStandAlonModel.getEmail()))));
            	    						userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
            	    					}
            	    					
            	    				}
            	    				
            	    			}
            	        		else{
            	    				
            	    				userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
            		    		}
            	        	}else{
            					userToken = "x";
            					session.setAttribute("connectionError", "No");
            				}
            	    	
            			//------------------------ Registration call
            	        	String connectionError = (String) session.getAttribute("connectionError");
            	        	if(CommonUtility.validateString(connectionError).equalsIgnoreCase("No") && CommonUtility.validateString(isEclipseDown).equalsIgnoreCase("Y")){
            		        	
            	        		if(CommonUtility.validateString(userToken).length()>0){
            		        		allowDBLogin = true;
            		        		userAvaialbleInERPSB.append(CommonUtility.validateString(registrationStandAlonModel.getEmail())).append("\t").append(CommonUtility.validateString(registrationStandAlonModel.getUserpassword())).append("\n");
            		        	}else{
            		        		allowDBLogin = false;
            		        		userNotAvaialbleInERPSB.append(CommonUtility.validateString(registrationStandAlonModel.getEmail())).append("\t").append(CommonUtility.validateString(registrationStandAlonModel.getUserpassword())).append("\n");
            		        	}
            		        	
            		        	loginAvailable = true;
            		        	
            		        }else{
            		        	loginAvailable = false;
            		        }
            	        	if(allowDBLogin && loginAvailable){
            		        	
            		        	if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")  && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPDATE_ERP_PASSWORD_TO_DB")).equalsIgnoreCase("N")){
            		        UsersDAO.updatePasswordWithUserName(CommonUtility.validateString(registrationStandAlonModel.getEmail()),CommonUtility.validateString(registrationStandAlonModel.getUserpassword()));
            		        	}
            		        	
            		        }
            	        	session.setAttribute("userToken", userToken);
            	            session.setAttribute("eclipseSessionTime", new java.util.Date());
            	            session.removeAttribute("isOciUser");
            	            session.removeAttribute("cartCountSession"); 
            	            System.out.println("---------- : " + session.getAttribute("userToken"));
            	            
            	           if(allowDBLogin){
            	            loginSuccess=loginAuthentication.authenticate(CommonUtility.validateString(registrationStandAlonModel.getEmail()), CommonUtility.validateString(registrationStandAlonModel.getUserpassword()), session);
            	            //userExistSB
            	           }
            	           
            	           
            	           if(loginAvailable){

                   	       	if(loginSuccess)
                   			{	
                   	       		userExistInDBSB.append(CommonUtility.validateString(registrationStandAlonModel.getEmail())).append("\t").append(CommonUtility.validateString(registrationStandAlonModel.getUserpassword())).append("\n");
                   				userToken = (String) session.getAttribute("userToken");
                   				if(userToken!=null && !userToken.trim().equalsIgnoreCase(""))
                   				{
                   					String tempSubset = (String) session.getAttribute("userSubsetId");
                   					String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
                   					if(CommonUtility.validateNumber(tempSubset)==0 && CommonUtility.validateNumber(tempGeneralSubset) >0)
                   					{
                   						session.setAttribute("userSubsetId",tempGeneralSubset);
                   						session.setAttribute("generalCatalog","0");
                   						tempSubset = (String) session.getAttribute("userSubsetId");
                   						tempGeneralSubset = (String) session.getAttribute("generalCatalog");
                   					}
                   					
                   					
                   					if(tempSubset!=null && !tempSubset.trim().equalsIgnoreCase(""))
                   					{
                       				    
                       					int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
                       					int subsetId = CommonUtility.validateNumber(tempSubset);
                   						/*ArrayList<Integer> userTab = ProductsDAO.getTopTab(subsetId, generalSubset);
                       					session.setAttribute("userTabList", userTab);*/
                   						if(session.getAttribute("isPunchoutUser")!=null && session.getAttribute("isPunchoutUser").toString().trim().equalsIgnoreCase("N") ){
                   							UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Login", session.getId(), ipaddress, "Login", "NA");
                   							
                   							cartClear = CommonDBQuery.getSystemParamtersList().get("CART_CLEAR");
                   							if(cartClear!=null && cartClear.equalsIgnoreCase("Y")){
                   								 ProductsDAO.clearCart(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)));
                   							}
                   								   int updated = ProductsDAO.updateCart(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), session.getId());
                   									       System.out.println("cart updated :: "+updated);
                   							/*if(session.getAttribute("firstLogin")!=null && session.getAttribute("firstLogin").toString().trim().equalsIgnoreCase("Y")){
                   								SendMailUtility.welcomeMail((String)session.getAttribute(Global.USERID_KEY));
                   							}*/
                   							
                   						}else{
                   							result="Invalid User Name/Password.";
                   							if(!CommonUtility.validateString(continueSession).equalsIgnoreCase("Y")){
                   								boolean logoutSuccess=loginAuthentication.logout(session);
               									System.out.println("logoutSuccess:---"+logoutSuccess);
               									if(logoutSuccess){
               										System.out.println("logoutSuccess:--- Assigning Web Session Values");
               										HttpSession sessionForMobile = request.getSession();
               										sessionForMobile.setAttribute("browseType", browseType);
               										WebLogin.loadWebUser();
               										WebLogin.webUserSession(session.getId());
               										
               									}
                   							}	
                   						}
                   					}else{
                   						result="No Catalog assigned to this user. Please contact our customer service.";
                   					
                   					}
                   				
                   				}
                   				else
                   				{
                   					result="Invalid User Name/Password";
                   				}
                   			
                   			}else{
                   				
                   				String eclipseResponse = "";
                   				if(CommonUtility.validateString(userToken).length()>0 && CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
                   					AddressModel addressModel = new AddressModel();
                   					addressModel.setEmailAddress(CommonUtility.validateString(registrationStandAlonModel.getEmail()));
               		        	 	addressModel.setUserPassword(CommonUtility.validateString(registrationStandAlonModel.getUserpassword()));
               		        	 	addressModel.setUserToken(userToken);
               		        	 	addressModel.setExistingUser("Y");
               		        	 	addressModel.setParentUserId(CommonUtility.validateParseIntegerToString(0));
               		        	 	addressModel.setUserStatus("Y");
                   					
               		        	 	
               		        	 	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("cimm2bcentral")){
               		        	 		
               		        	 		UsersModel userDetails = new UsersModel();
               		        	 		
               		        	 		
               		        	 		if(session!=null && session.getAttribute("erpLoginContactDetails")!=null){
               		        	 			
               		        	 			ArrayList<Cimm2BCentralContact> responseObj = (ArrayList<Cimm2BCentralContact>) session.getAttribute("erpLoginContactDetails");
               		        	 			
               		        	 			for(Cimm2BCentralContact contactDetail : responseObj){
               		        	 				
               		        	 				if(CommonUtility.validateString(CommonUtility.validateString(registrationStandAlonModel.getEmail())).equalsIgnoreCase(CommonUtility.validateString(contactDetail.getPrimaryEmailAddress()))){
               		        	 					addressModel.setFirstName(CommonUtility.validateString(contactDetail.getFirstName()));
               		        	 					addressModel.setLastName(CommonUtility.validateString(contactDetail.getLastName()));
               		        	 					addressModel.setPhoneNo(CommonUtility.validateString(contactDetail.getPrimaryPhoneNumber()));
               		        	 					addressModel.setFaxNumber(CommonUtility.validateString(contactDetail.getFaxNumber()));
               		        	 					
               		        	 					userDetails.setFirstName(CommonUtility.validateString(contactDetail.getFirstName()));
               		    							userDetails.setLastName(CommonUtility.validateString(contactDetail.getLastName()));
               		    							userDetails.setEmailAddress(CommonUtility.validateString(contactDetail.getPrimaryEmailAddress()));
               		    							userDetails.setPhoneNo(CommonUtility.validateString(contactDetail.getPrimaryPhoneNumber()).replaceAll("[^a-zA-Z0-9]", ""));
               		    							userDetails.setJobTitle(CommonUtility.validateString(contactDetail.getTitle()));
               		    							break;
               		        	 				}
               		        	 				
               		        	 			}
               		        	 		}
               		        	 		 
               		        	 		 
               		        	 		 userDetails.setSession(session);
               							 userDetails.setEntityId(CommonUtility.validateString(userToken));
               							 userDetails.setAccountName(CommonUtility.validateString(userToken));
               							 userDetails.setPassword(CommonUtility.validateString(registrationStandAlonModel.getUserpassword()));
               							 //userDetails.setAddress1(companyBillingAddress1B);
               							 //userDetails.setAddress2(suiteNo1B);
               							 //userDetails.setCity(cityName1B);
               							 //userDetails.setState(stateName2AB);
               							 //userDetails.setCountry(countryName1B);
               							 //userDetails.setZipCode(zipCode1B);
               							 if(CommonDBQuery.getSystemParamtersList().get("OnAccountExistingCustomerRegistrationStatus")!=null && CommonDBQuery.getSystemParamtersList().get("OnAccountExistingCustomerRegistrationStatus").trim().length()>0){
               								userDetails.setUserStatus(CommonDBQuery.getSystemParamtersList().get("OnAccountExistingCustomerRegistrationStatus"));
               							 }else{
               								userDetails.setUserStatus("Y");
               							 }
               							 	 		
               		        	 		UserManagement usersObj = new UserManagementImpl();
               		        	 		eclipseResponse = usersObj.newOnAccountUserRegistration(userDetails);
               		        	 		
               		        	 	}else{
               		        	 		//eclipseResponse = UserRegisterUtility.registerUserInDB(getLoginId(), getLoginPassword(),userToken,"Y",0,"Y");
                   		        	 	eclipseResponse = UserRegisterUtility.registerUserInDB(addressModel);
               		        	 	}

               		        	 if(eclipseResponse!=null && eclipseResponse.trim().contains("successfully"))
                   					loginSuccess = true;
               		        	 	successSB.append(CommonUtility.validateString(registrationStandAlonModel.getEmail())).append("\t").append(CommonUtility.validateString(registrationStandAlonModel.getUserpassword())).append("\n");
                   				 }else{
                   					errorSB.append(CommonUtility.validateString(registrationStandAlonModel.getEmail())).append("\t").append(CommonUtility.validateString(registrationStandAlonModel.getUserpassword())).append("\n");
                   				}
                   				
                   				
                   				if(loginSuccess){
                   					loginSuccess=loginAuthentication.authenticate(CommonUtility.validateString(registrationStandAlonModel.getEmail()), CommonUtility.validateString(registrationStandAlonModel.getUserpassword()), session);
                   				}
                   				
                   				if(!loginSuccess)
                   				{
                   					userLoginFailSB.append(CommonUtility.validateString(registrationStandAlonModel.getEmail())).append("\t").append(CommonUtility.validateString(registrationStandAlonModel.getUserpassword())).append("\n");
                   					if(session.getAttribute("validUser")!=null)
                   						result="Invalid User Name/Password.";
                   					else if(userToken==null)
                   						result="User not registered.";
                   					else
                   					{
                   						if(eclipseResponse!=null && eclipseResponse.trim().contains("Member"))
                   							result = eclipseResponse;
                   						else
                   						result = "User registration failed. Please contact customer service.";
                   					}	
                   				}else{
                   					userLoginSuccessSB.append(CommonUtility.validateString(registrationStandAlonModel.getEmail())).append("\t").append(CommonUtility.validateString(registrationStandAlonModel.getUserpassword())).append("\n");
                   					String tempSubset = (String) session.getAttribute("userSubsetId");
                   				    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
                   					int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
                   					int subsetId = CommonUtility.validateNumber(tempSubset);
                   					ArrayList<Integer> userTab = ProductsDAO.getTopTab(subsetId, generalSubset);
                   					session.setAttribute("userTabList", userTab);
                   					if(session.getAttribute("isPunchoutUser")!=null && session.getAttribute("isPunchoutUser").toString().trim().equalsIgnoreCase("N") ){
                   						UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Login", session.getId(), ipaddress, "Login", "NA");
                   						if(cartClear.equalsIgnoreCase("Y")){
                   							ProductsDAO.clearCart(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)));
                   						}
                   						if(session.getAttribute("firstLogin")!=null && session.getAttribute("firstLogin").toString().trim().equalsIgnoreCase("Y")){
                   							//SendMailUtility.welcomeMail((String)session.getAttribute(Global.USERID_KEY));
                   						}
                   					}else{
                   						result="Invalid User Name/Password.";
                   					}
                   				}
                   			}
                   	        
                   		//********************//
                   	
            	        	   
            	        	   
            	           }
            			
            		}
            	    	
            	}
            }
            }
            
            if(valueFromFILESB!=null && valueFromFILESB.length()>0){
            	System.out.println("******************* valueFromFILESB FILE START ******************");
            	File file = new File(errorFilePath+"valueFromFILESB.txt");
            	if (!file.exists()) {
    				file.createNewFile();
    			}
            	FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			bw.write(valueFromFILESB.toString());
    			bw.close();
    			System.out.println("******************* valueFromFILESB FILE END ******************");
            }
            if(errorSB!=null && errorSB.length()>0){
            	System.out.println("******************* Error FILE START ******************");
            	File file = new File(errorFilePath+"userInserEcommerceError.txt");
            	if (!file.exists()) {
    				file.createNewFile();
    			}
            	FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			bw.write(errorSB.toString());
    			bw.close();
    			
	           /* PrintWriter writer = new PrintWriter(errorFilePath+"userInserEcommerceError.txt", "UTF-8");
				writer.println(errorSB.toString());
				writer.close();*/
    			System.out.println("******************* Error FILE END ******************");
            }
            if(successSB!=null && successSB.length()>0){
            	System.out.println("******************* Success FILE START ******************");
            	File file = new File(errorFilePath+"userInserEcommerceSuccess.txt");
            	if (!file.exists()) {
    				file.createNewFile();
    			}
            	FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			bw.write(errorSB.toString());
    			bw.close();
    			
            	/*PrintWriter writer = new PrintWriter(errorFilePath+"userInserEcommerceSuccess.txt", "UTF-8");
				writer.println(errorSB.toString());
				writer.close();*/
    			System.out.println("******************* Success FILE End ******************");
            }
            if(userExistInDBSB!=null && userExistInDBSB.length()>0){
            	System.out.println("******************* userExistInDBSB FILE START ******************");
            	File file = new File(errorFilePath+"userExistInDBSB.txt");
            	if (!file.exists()) {
    				file.createNewFile();
    			}
            	FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			bw.write(userExistInDBSB.toString());
    			bw.close();
    			System.out.println("******************* userExistInDBSB FILE End ******************");
            }
            if(userAvaialbleInERPSB!=null && userAvaialbleInERPSB.length()>0){
            	System.out.println("******************* userAvaialbleInERPSB FILE START ******************");
            	File file = new File(errorFilePath+"userAvaialbleInERPSB.txt");
            	if (!file.exists()) {
    				file.createNewFile();
    			}
            	FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			bw.write(userAvaialbleInERPSB.toString());
    			bw.close();
    			System.out.println("******************* userAvaialbleInERPSB FILE End ******************");
            }
            if(userNotAvaialbleInERPSB!=null && userNotAvaialbleInERPSB.length()>0){
            	System.out.println("******************* userNotAvaialbleInERPSB FILE START ******************");
            	File file = new File(errorFilePath+"userNotAvaialbleInERPSB.txt");
            	if (!file.exists()) {
    				file.createNewFile();
    			}
            	FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			bw.write(userNotAvaialbleInERPSB.toString());
    			bw.close();
    			System.out.println("******************* userNotAvaialbleInERPSB FILE End ******************");
            }
            if(userLoginSuccessSB!=null && userLoginSuccessSB.length()>0){
            	System.out.println("******************* userLoginSuccessSB FILE START ******************");
            	File file = new File(errorFilePath+"userLoginSuccessSB.txt");
            	if (!file.exists()) {
    				file.createNewFile();
    			}
            	FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			bw.write(userLoginSuccessSB.toString());
    			bw.close();
    			System.out.println("******************* userLoginSuccessSB FILE End ******************");
            }
            if(userLoginFailSB!=null && userLoginFailSB.length()>0){
            	System.out.println("******************* userLoginFailSB FILE START ******************");
            	File file = new File(errorFilePath+"userLoginFailSB.txt");
            	if (!file.exists()) {
    				file.createNewFile();
    			}
            	FileWriter fw = new FileWriter(file.getAbsoluteFile());
    			BufferedWriter bw = new BufferedWriter(fw);
    			bw.write(userLoginFailSB.toString());
    			bw.close();
    			System.out.println("******************* userLoginFailSB FILE End ******************");
            }
	        System.out.println("******************************************** All User Registration Completed  *********************************************************");

        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
}



	/* public static void readRecordsFromFile(String filePath){

    	ArrayList<RegistrationStandAlonModel> userDetails = null;
        try {
        	
        	File excel = new File(filePath);
        	//File excel = new File("D:\\Projects\\McMc\\UserList\\Book1.xlsx");
            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);

            Iterator<Row> itr = sheet.iterator();

            
            userDetails = new ArrayList<RegistrationStandAlonModel>();
            // Iterating over Excel file in Java
            while (itr.hasNext()) {
                Row row = itr.next();
                RegistrationStandAlonModel registrationStandAlonModel = new RegistrationStandAlonModel();
                // Iterating over each column of Excel file
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    String cellValue = "";
                    
                    switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                    	
                        System.out.print(cell.getColumnIndex()+" - "+cell.getStringCellValue() + "\t");
                        cellValue = CommonUtility.validateString(cell.getStringCellValue());
                        if(cell.getColumnIndex()==0){
                        	registrationStandAlonModel.setCompanyName1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==1){
                        	registrationStandAlonModel.setAccountManager1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==2){
                        	registrationStandAlonModel.setAccountNo1B(cellValue);                    	
                        	
                        }else if(cell.getColumnIndex()==3){
                        	registrationStandAlonModel.setClosestBranch1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==4){
                        	registrationStandAlonModel.setCompanyBillingAddress1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==5){
                        	registrationStandAlonModel.setFirstName1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==6){
                        	registrationStandAlonModel.setLastName1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==7){
                        	registrationStandAlonModel.setEmailAddress1B(CommonUtility.validateString(cellValue).toLowerCase());
                        	
                        }else if(cell.getColumnIndex()==8){
                        	registrationStandAlonModel.setSuiteNo1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==9){
                        	registrationStandAlonModel.setCityName1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==10){
                        	registrationStandAlonModel.setStateName2AB(cellValue);
                        	
                        }else if(cell.getColumnIndex()==11){
                        	registrationStandAlonModel.setZipCode1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==12){
                        	registrationStandAlonModel.setCountryName1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==13){
                        	registrationStandAlonModel.setPhoneNo1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==14){
                        	registrationStandAlonModel.setNewPassword1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==15){
                        	registrationStandAlonModel.setNewPasswordConfirm1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==16){
                        	registrationStandAlonModel.setSalesRepContact1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==19){
                        	registrationStandAlonModel.setUserRole(cellValue);
                        }
                        break;
                     case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t");
                        cellValue = CommonUtility.validateString(""+cell.getNumericCellValue());
                        if(cell.getColumnIndex()==0){
                        	registrationStandAlonModel.setCompanyName1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==1){
                        	registrationStandAlonModel.setAccountManager1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==2){
                        	registrationStandAlonModel.setAccountNo1B(cellValue);                    	
                        	
                        }else if(cell.getColumnIndex()==3){
                        	registrationStandAlonModel.setClosestBranch1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==4){
                        	registrationStandAlonModel.setCompanyBillingAddress1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==5){
                        	registrationStandAlonModel.setFirstName1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==6){
                        	registrationStandAlonModel.setLastName1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==7){
                        	registrationStandAlonModel.setEmailAddress1B(cellValue.toLowerCase());
                        	
                        }else if(cell.getColumnIndex()==8){
                        	registrationStandAlonModel.setSuiteNo1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==9){
                        	registrationStandAlonModel.setCityName1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==10){
                        	registrationStandAlonModel.setStateName2AB(cellValue);
                        	
                        }else if(cell.getColumnIndex()==11){
                        	registrationStandAlonModel.setZipCode1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==12){
                        	registrationStandAlonModel.setCountryName1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==13){
                        	registrationStandAlonModel.setPhoneNo1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==14){
                        	registrationStandAlonModel.setNewPassword1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==15){
                        	registrationStandAlonModel.setNewPasswordConfirm1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==16){
                        	registrationStandAlonModel.setSalesRepContact1B(cellValue);
                        	
                        }else if(cell.getColumnIndex()==17){
                        	registrationStandAlonModel.setSalesRepContact1B(cellValue);
                         }
                        break;
                    default:
                    	
 
                    }
                }
                System.out.println("-------------------------------------------------------");
                userDetails.add(registrationStandAlonModel);
                System.out.println("");
            }
            System.out.println("******************************************** File Read Completed  *********************************************************");
            if(userDetails!=null && userDetails.size()>0){
            	for(RegistrationStandAlonModel registrationStandAlonModel : userDetails){
            		if(registrationStandAlonModel!=null){
            			//existingUserRegisteration(registrationStandAlonModel);
            			System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            			System.out.println(CommonUtility.validateString(registrationStandAlonModel.getAccountNo1B())+" : "+CommonUtility.validateString(registrationStandAlonModel.getEmailAddress1B())+" : "+CommonUtility.validateString(registrationStandAlonModel.getUserRole())+" : "+registrationMethod(registrationStandAlonModel));
            			System.out.println("---------------------------------------------------------------------- Password Updates -------------------------------------------------------------------------");
            			if(CommonUtility.validateString(registrationStandAlonModel.getNewPassword1B()).equalsIgnoreCase("")){
            				registrationStandAlonModel.setNewPassword1B("test123##");
            			}
            			if(CommonUtility.validateString(registrationStandAlonModel.getEmailAddress1B()).length()>0 && CommonUtility.validateString(registrationStandAlonModel.getNewPassword1B()).length()>0){
            				updatePassword(registrationStandAlonModel.getEmailAddress1B(), registrationStandAlonModel.getNewPassword1B());
            			}
                  		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            		}
            	}
            }
            System.out.println("******************************************** All User Registration Completed  *********************************************************");

        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    
    }
    */
    public static void updatePassword(String userName, String password){
    	
    	PreparedStatement pstmt = null;
 	    int count = 0;
 	    Connection conn = null;
    	try {
			
    		String securePassword = getsecurePassword(password);;
    		String sql = "UPDATE CIMM_USERS SET PASSWORD=? WHERE UPPER(USER_NAME) = ?";
    		
    		conn = ConnectionManager.getDBConnection();
    		pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(securePassword));
			pstmt.setString(2, CommonUtility.validateString(userName).toUpperCase());//userName
			count = pstmt.executeUpdate();
			if(count>0){
				System.out.println("Password Update Successful for "+userName);
			}else{
				System.out.println("Password Update failed for "+userName);
			}

    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	finally{
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
	    }
    	
    }
    
    public static String registrationMethod(RegistrationStandAlonModel registrationStandAlonModel){
    	
    	String result = "";
    	try {
    		
    		HttpServletRequest request = ServletActionContext.getRequest();
    	    HttpSession session = request.getSession(); 
    	    ArrayList<UsersModel> superUserStatus = null;
    		boolean isValidUser = true; 
    		boolean isAccountExist = false;
    		boolean userExist = false;
    		
    		UsersModel userDetails = new UsersModel();
    		 if(isValidUser){
    			 
    			 userExist = UsersDAO.checkForUserName(registrationStandAlonModel.getEmailAddress1B());
    			 isAccountExist = UsersDAO.checkForUsersCustomerId(registrationStandAlonModel.getAccountNo1B());
    			 if(userExist){
    				 result = result + "Email Address already exists.|";
    			 }else{
    				 
    				 	 userDetails.setEntityName(CommonUtility.validateString(registrationStandAlonModel.getCompanyName1B()));
    					 userDetails.setEntityId(CommonUtility.validateString(registrationStandAlonModel.getAccountNo1B()));
    					 userDetails.setAccountName(registrationStandAlonModel.getAccountNo1B());
    					 userDetails.setFirstName(CommonUtility.validateString(registrationStandAlonModel.getFirstName1B()));
    					 userDetails.setLastName(CommonUtility.validateString(registrationStandAlonModel.getLastName1B()));
    					 userDetails.setEmailAddress(CommonUtility.validateString(registrationStandAlonModel.getEmailAddress1B()));
    					 userDetails.setPassword(CommonUtility.validateString(registrationStandAlonModel.getFirstName1B()+"321##").toLowerCase());
    					 userDetails.setRequestAuthorizationLevel("");
    					 userDetails.setSalesContact(CommonUtility.validateString(registrationStandAlonModel.getSalesRepContact1B()));
    					 userDetails.setAddress1(CommonUtility.validateString(registrationStandAlonModel.getCompanyBillingAddress1B()));
    					 userDetails.setAddress2(CommonUtility.validateString(registrationStandAlonModel.getSuiteNo1B()));
    					 userDetails.setCity(CommonUtility.validateString(registrationStandAlonModel.getCityName1B()));
    					 userDetails.setState(CommonUtility.validateString(registrationStandAlonModel.getStateName2AB()));
    					 userDetails.setCountry(CommonUtility.validateString(registrationStandAlonModel.getCountryName1B()));
    					 userDetails.setZipCode(CommonUtility.validateString(registrationStandAlonModel.getZipCode1B()));
    					 userDetails.setPhoneNo(CommonUtility.validateString(registrationStandAlonModel.getPhoneNo1B()).replaceAll("[^a-zA-Z0-9]", ""));
    					 userDetails.setNewsLetterSub("Y");
    					 
    					 if(CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION")!=null && CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION").trim().equalsIgnoreCase("Y")){
    						 userDetails.setWareHouseCode(CommonUtility.validateNumber(""));
    					 }
    					 userDetails.setCustomerAccountExist(isAccountExist);
    					 
    					 if(CommonDBQuery.getSystemParamtersList().get("OnAccountExistingCustomerRegistrationStatus")!=null && CommonDBQuery.getSystemParamtersList().get("OnAccountExistingCustomerRegistrationStatus").trim().length()>0){
    						 userDetails.setUserStatus(CommonDBQuery.getSystemParamtersList().get("OnAccountExistingCustomerRegistrationStatus"));
    					 }else{
    							userDetails.setUserStatus("Y");
    					 }
    					 if(CommonUtility.validateString(registrationStandAlonModel.getJobTitle1B()).length()>0){
    						 userDetails.setJobTitle(CommonUtility.validateString(registrationStandAlonModel.getJobTitle1B()));
    					 }
    					 if(CommonUtility.validateString(registrationStandAlonModel.getSalesRepContact1B()).length()>0){
    						 userDetails.setSalesRepContact(CommonUtility.validateString( registrationStandAlonModel.getSalesRepContact1B()));
    					 }
    					 if(CommonUtility.validateString(registrationStandAlonModel.getAccountManager1B()).length()>0){
    						 userDetails.setAccountManager(CommonUtility.validateString( registrationStandAlonModel.getAccountManager1B()));
    					 }
    					 if(CommonUtility.validateString(registrationStandAlonModel.getClosestBranch1B()).length()>0){
    						 userDetails.setClosestBranch(CommonUtility.validateString(registrationStandAlonModel.getClosestBranch1B()));
    					 }
    					 if(CommonUtility.validateString(registrationStandAlonModel.getUserRole()).length()>0){
    						 userDetails.setUserRole(CommonUtility.validateString(registrationStandAlonModel.getUserRole()));
    					 }
    					 
    			
    					 userDetails.setSession(session);
    					 
    					 LinkedHashMap<String,String> userRegisteration=new  LinkedHashMap<String,String>();
    					 userRegisteration.put("companyName1B",CommonUtility.validateString(registrationStandAlonModel.getCompanyName1B()));
    					 userRegisteration.put("accountNo1B",registrationStandAlonModel.getAccountNo1B());
    					 userRegisteration.put("firstName1B",CommonUtility.validateString(registrationStandAlonModel.getFirstName1B()));
    					 userRegisteration.put("lastName1B",CommonUtility.validateString(registrationStandAlonModel.getLastName1B()));
    					 userRegisteration.put("emailAddress1B",CommonUtility.validateString(registrationStandAlonModel.getEmailAddress1B()));
    					 //userRegisteration.put("emailAddress1B",emailAddress1B);
    					 userRegisteration.put("newPassword1B",CommonUtility.validateString(registrationStandAlonModel.getFirstName1B()+"321##").toLowerCase());
    					 userRegisteration.put("requestAuthorization1B",userDetails.getRequestAuthorizationLevel());
    					 userRegisteration.put("salesContact1B",userDetails.getSalesContact());
    					 userRegisteration.put("companyBillingAddress1B",CommonUtility.validateString(registrationStandAlonModel.getCompanyBillingAddress1B()));
    					 userRegisteration.put("suiteNo1B",CommonUtility.validateString(registrationStandAlonModel.getSuiteNo1B()));
    					 userRegisteration.put("cityName1B",CommonUtility.validateString(registrationStandAlonModel.getCityName1B()));
    					 userRegisteration.put("stateName1B", CommonUtility.validateString(registrationStandAlonModel.getStateName2AB()) );
    					 userRegisteration.put("countryName1B",CommonUtility.validateString(registrationStandAlonModel.getCountryName1B()));
    					 userRegisteration.put("zipCode1B", CommonUtility.validateString(registrationStandAlonModel.getZipCode1B()));
    					 userRegisteration.put("phoneNo1B",	userDetails.getPhoneNo());
    					 userRegisteration.put("newsLetterSub1B",userDetails.getNewsLetterSub());
    					 userRegisteration.put("Status", userDetails.getUserStatus());
    					 
    					  
    					 if(CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION")!=null && CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION").trim().equalsIgnoreCase("Y")){
    						 userRegisteration.put("warehousecode", "");
    					 }
    					 
    					 if(CommonUtility.validateString(registrationStandAlonModel.getJobTitle1B()).length()>0){
    						 userRegisteration.put("jobTitle1B",CommonUtility.validateString(registrationStandAlonModel.getJobTitle1B()));
    					 }
    					
    					 if(CommonUtility.validateString(registrationStandAlonModel.getSalesRepContact1B()).length()>0){
    						 userRegisteration.put("salesRepContact1B",CommonUtility.validateString(registrationStandAlonModel.getSalesRepContact1B()));
    					 }
    					 
    					 if(CommonUtility.validateString(registrationStandAlonModel.getClosestBranch1B()).length()>0){
    						 userRegisteration.put("closestBranch1B",CommonUtility.validateString(registrationStandAlonModel.getClosestBranch1B()));
    					 }
    					 
    					 if(CommonUtility.validateString(registrationStandAlonModel.getAccountManager1B()).length()>0){
    						 userRegisteration.put("accountManager1B",CommonUtility.validateString(registrationStandAlonModel.getAccountManager1B()));
    					 }
    					 
    					 SaveCustomFormDetails saveForm = new SaveCustomFormDetails();
    					 saveForm.saveToDataBase(userRegisteration,"RegisterationForm1B");
    					 
    					 
    					 superUserStatus = UsersDAO.getSuperUserForCompany(CommonUtility.validateString(registrationStandAlonModel.getCompanyName1B()));
    					 UserManagement usersObj = new UserManagementImpl();
    					 
    					 System.out.println("Before Login request to ERP @OnAccountExistingCustomerRegistration");
    					 result = usersObj.newOnAccountUserRegistration( userDetails);
    					 System.out.println("After Login request to ERP @OnAccountExistingCustomerRegistration");
    					 userDetails.setCompanyName(CommonUtility.validateString(registrationStandAlonModel.getCompanyName1B()));
    					 if(result!=null && result.toLowerCase().contains("successfully")){
                             String constantContact = CommonDBQuery.getSystemParamtersList().get("CONSTANT_CONTACT_ENABLE");
                             if(CommonUtility.validateString(constantContact).length()>0 && constantContact.trim().equalsIgnoreCase("Y")){
                                    String response = UsersDAO.constantContactOne(userDetails);
                                    System.out.println("Constant Contact Response:OnAccountExistingCustomerRegistration: "+response);
                                    
                             }
    					 }
    			 }
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    public static void existingUserRegisteration(RegistrationStandAlonModel registrationStandAlonModel){
    	//String url = "https://mcmcstage.cimm2.com/OnAccountReg.action";
    	String url = "http://122.166.57.30/OnAccountReg.action";
        InputStream in = null;

        try {
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(url);

            //Add any parameter if u want to send it with Post req.
            method.addParameter("companyName1B", CommonUtility.validateString(registrationStandAlonModel.getCompanyName1B()));
            method.addParameter("emailAddress1B", CommonUtility.validateString(registrationStandAlonModel.getEmailAddress1B()));
            method.addParameter("newPassword1B", CommonUtility.validateString(registrationStandAlonModel.getNewPassword1B()));
            method.addParameter("newPasswordConfirm1B", CommonUtility.validateString(registrationStandAlonModel.getNewPasswordConfirm1B()));
            method.addParameter("accountManager1B",CommonUtility.validateString( registrationStandAlonModel.getAccountManager1B()));
            method.addParameter("phoneNo1B", CommonUtility.validateString(registrationStandAlonModel.getPhoneNo1B()));
            method.addParameter("closestBranch1B", CommonUtility.validateString(registrationStandAlonModel.getClosestBranch1B()));
            method.addParameter("accountNo1B", CommonUtility.validateString(registrationStandAlonModel.getAccountNo1B()));
            method.addParameter("companyBillingAddress1B", CommonUtility.validateString(registrationStandAlonModel.getCompanyBillingAddress1B()));
            method.addParameter("suiteNo1B", CommonUtility.validateString(registrationStandAlonModel.getSuiteNo1B()));
            method.addParameter("firstName1B", CommonUtility.validateString(registrationStandAlonModel.getFirstName1B()));
            method.addParameter("lastName1B", CommonUtility.validateString(registrationStandAlonModel.getLastName1B()));
            method.addParameter("cityName1B", CommonUtility.validateString(registrationStandAlonModel.getCityName1B()));
            method.addParameter("jobTitle1B", CommonUtility.validateString(registrationStandAlonModel.getJobTitle1B()));
            method.addParameter("stateName2AB", CommonUtility.validateString(registrationStandAlonModel.getStateName2AB()));
            method.addParameter("zipCode1B", CommonUtility.validateString(registrationStandAlonModel.getZipCode1B()));
            method.addParameter("countryName1B", CommonUtility.validateString(registrationStandAlonModel.getCountryName1B()));
            method.addParameter("newsLetterSub1B", CommonUtility.validateString(registrationStandAlonModel.getNewsLetterSub1B()));
            
            int statusCode = client.executeMethod(method);
            if (statusCode != -1) {
                in = method.getResponseBodyAsStream();
            }

            System.out.println(registrationStandAlonModel.getEmailAddress1B()+" : "+in);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
	    
}