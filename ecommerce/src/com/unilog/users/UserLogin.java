package com.unilog.users;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.LoginSubmitManagement;
import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralContact;
import com.erp.service.impl.LoginSubmitManagementImpl;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.LoginSubmitManagementModel;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.products.ProductsDAO;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.security.LoginAuthentication;
import com.unilog.security.SecureData;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.utility.CommonUtility;
import com.unilog.velocitytool.CIMM2VelocityTool;
public class UserLogin extends ActionSupport 
{
	private static final long serialVersionUID = -3459347464645014823L;
	private long startTimer = 0;
	protected String target = ERROR;
	private String externalSystemId;
	private String loginId;
	private String loginPassword;
	private ArrayList<MenuAndBannersModal> topMenu=null;
	private String result;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String loginType;
	private String renderContent;
	private String continueSession;
	private String pageUrl;
	private String fromPageValue;
	public String woeUserName;
	public String woePassword;
	public String woeConfirmPassword;
	public String woeLogin;
	
	public String getExternalSystemId() {
		return externalSystemId;
	}
	public void setExternalSystemId(String externalSystemId) {
		this.externalSystemId = externalSystemId;
	}
	public String getRenderContent() {
		return renderContent;
	}
	public void setFromPageValue(String fromPageValue) {
		this.fromPageValue = fromPageValue;
	}
	public String getFromPageValue() {
		return fromPageValue;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setContinueSession(String continueSession) {
		this.continueSession = continueSession;
	}
	public String getContinueSession() {
		return continueSession;
	}
	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getResult() {
		return result;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(final String loginId) {
		this.loginId = loginId;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
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
	public ArrayList<MenuAndBannersModal> getTopMenu() {
		return topMenu;
	}
	public void setTopMenu(ArrayList<MenuAndBannersModal> topMenu) {
		this.topMenu = topMenu;
	}
	public void setLoginPassword(final String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getWoeUserName() {
		return woeUserName;
	}
	public void setWoeUserName(String woeUserName) {
		this.woeUserName = woeUserName;
	}
	public String getWoePassword() {
		return woePassword;
	}
	public void setWoePassword(String woePassword) {
		this.woePassword = woePassword;
	}
	public String getWoeConfirmPassword() {
		return woeConfirmPassword;
	}
	public void setWoeConfirmPassword(String woeConfirmPassword) {
		this.woeConfirmPassword = woeConfirmPassword;
	}
	public String getWoeLogin() {
		return woeLogin;
	}
	public void setWoeLogin(String woeLogin) {
		this.woeLogin = woeLogin;
	}
	public String execute() throws Exception {
			startTimer = CommonUtility.startTimeDispaly();
			boolean loginSuccess=false;
			result= "";
			String cartClear ="";
			String isEclipseDown = "";
			String userToken = null;
			String status = null;
			System.out.println("Validating login : New method Implemented");
	        boolean allowDBLogin =true;
	        request =ServletActionContext.getRequest();
	        boolean isPost = "POST".equals(request.getMethod());
	        response = ServletActionContext.getResponse();
	        HttpSession session = request.getSession();
	        String currentSessionId = session.getId();
	        Boolean loginAvailable = true;
	        String browseType = (String) session.getAttribute("browseType");
	        String ipaddress = request.getHeader("X-Forwarded-For");
	        isEclipseDown = CommonDBQuery.getSystemParamtersList().get("ERPAVAILABLE");
			String erpLogin =  CommonDBQuery.getSystemParamtersList().get("ERPLOGIN");
	        session.setAttribute("isEclipseDown", isEclipseDown);
	        String URLString=request.getQueryString();
	        HttpServletResponse response1 = ServletActionContext.getResponse();
			
	        if (ipaddress  == null){
				ipaddress = request.getRemoteAddr();
				System.out.println("At Login Page Ip : " + ipaddress);
			}
	        if(CommonDBQuery.getSystemParamtersList().get("ENABLE_CSRF_TOKEN")!=null && CommonDBQuery.getSystemParamtersList().get("ENABLE_CSRF_TOKEN").equalsIgnoreCase("Y")) {
				
	        	if(!isPost || (isPost && URLString!=null)) {
    				response.setStatus(response.SC_METHOD_NOT_ALLOWED);
    				return null;
    			}
	        	String localeCode=CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_LOCALE_CODE"));
	    		String getPostMethodList=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(localeCode).getProperty("httppostmethodlist.names"));
	    		if(getPostMethodList!=null && getPostMethodList!="") {
	    		String [] methodList=getPostMethodList.split(",");
	    		for(String methodName: methodList) {
	    			if(!isPost && methodName.equalsIgnoreCase("doLogin")) {
	    				response.setStatus(response.SC_METHOD_NOT_ALLOWED);
	    				return null;
	    			}
	    		}
	    	}
	        	System.out.println("csrftoken: "+request.getParameter("csrftoken"));
				
				String csrfPreventionSalt =   CommonUtility.validateString((String) session.getAttribute("csrfPreventionSalt"));
				System.out.println("csrfPreventionSalt: "+CommonUtility.validateString(csrfPreventionSalt));
				if(!csrfPreventionSalt.equalsIgnoreCase(CommonUtility.validateString(request.getParameter("csrftoken")))) {
					response1.setStatus(response1.SC_BAD_REQUEST);
					return null;
				}
				CIMM2VelocityTool.getInstance().generateSalt();
			}
			
			boolean popUpRequest = false;
			if(request.getParameter("loginType")!=null && request.getParameter("loginType").trim().equalsIgnoreCase("popup")){
				 popUpRequest = true;
			}
			
			//-----(SSO) Single Sign On Login
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SSO_ENABLED")).equalsIgnoreCase("Y")) {
				System.out.println("SSO LOGIN");
				if(request.getAttribute("externalSystemId")!=null && CommonUtility.validateString(request.getAttribute("externalSystemId").toString()).length()>0) {
					externalSystemId = CommonUtility.validateString(request.getAttribute("externalSystemId").toString());
					HashMap<String,String> userDetailsForSystemID = UsersDAO.getUserNameAndPasswordForExternalSystemUserId(CommonUtility.validateString(externalSystemId), "Y");
					if(userDetailsForSystemID.isEmpty() && !(userDetailsForSystemID.size()>0) && CommonUtility.customServiceUtility()!=null) {
						Map<String,String> ssoUserDetail = (Map<String, String>) request.getAttribute("ssoUserDetail");
						session.setAttribute(Global.USERNAME_KEY,ssoUserDetail.get("username"));
						SecureData userPassword=new SecureData();
						session.setAttribute("securedPassword",SecureData.getsecurePassword(ssoUserDetail.get("guid")));
						userDetailsForSystemID =CommonUtility.customServiceUtility().registerSsoUserInDb(ssoUserDetail);
					}
					setLoginId(userDetailsForSystemID.get("userName"));
					setLoginPassword(userDetailsForSystemID.get("password"));
				}
				session.setAttribute("frPage","loginPage");
			}
			//-----(SSO) Single Sign On Login
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			LoginAuthentication loginAuthentication = new LoginAuthentication();
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD")).length()>0){
				SecureData checkPass = new SecureData();
				String DecrPassw = checkPass.validatePassword(CommonDBQuery.getSystemParamtersList().get("WL_CHECHOUT_USER_DEFAULT_PASSWORD"));
				if(CommonUtility.validateString(DecrPassw).equals(CommonUtility.validateString(getLoginPassword()))){
					setLoginPassword("S7Hk2o4-OHAid16RW7D4F_k4dTbAJmxDlWjvulF1qjI");
				}
			}
			
			if(CommonUtility.validateString(getLoginId()).length()>0 && CommonUtility.validateString(getLoginPassword()).length()>0){
				
				LoginSubmitManagement loginSubmit = new LoginSubmitManagementImpl();
				LoginSubmitManagementModel loginSubmitManagementModel = new LoginSubmitManagementModel();
				UsersDAO userUtilityDAO = new UsersDAO();
				UsersModel enteredUserNameInfo = new UsersModel(); 
			    enteredUserNameInfo = userUtilityDAO.getUserStatus(CommonUtility.validateString(getLoginId()),true);
		    	System.out.println("-------------->"+enteredUserNameInfo.getStatusDescription());
		        
		    	loginSubmitManagementModel.setUserName(CommonUtility.validateString(getLoginId()));
		    	loginSubmitManagementModel.setPassword(CommonUtility.validateString(getLoginPassword()));
		    	
		    	if(enteredUserNameInfo.getStatusDescription()==null ||enteredUserNameInfo.getStatusDescription().equalsIgnoreCase("P") ||enteredUserNameInfo.getStatusDescription().equalsIgnoreCase("Y")||enteredUserNameInfo.getStatusDescription().equalsIgnoreCase("")){
		        	loginAuthentication = new LoginAuthentication();
		        	
		        	if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
		        		
		        		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIN_USER_NAME_CASE")).equalsIgnoreCase("Y")){
		        			loginSubmitManagementModel.setUserName(CommonUtility.validateString(getLoginId()).toUpperCase());
		    				userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel); // User name UpperCase
		    				
		    				if(CommonUtility.validateString(userToken).length()==0){
		    					UsersModel upperCheckUserDetail =  userUtilityDAO.getUserStatus(CommonUtility.validateString(getLoginId()),false);
		    					
		    					if(CommonUtility.validateString(upperCheckUserDetail.getUserName()).length()>0){
		    						loginSubmitManagementModel.setUserName(CommonUtility.validateString(upperCheckUserDetail.getUserName()));
		    						userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
		    						
		    						if(CommonUtility.validateString(userToken).length()==0 && !getLoginId().equals(upperCheckUserDetail.getUserName())){
		    							loginSubmitManagementModel.setUserName(CommonUtility.validateString(getLoginId()));
		    							userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
		    						}
		    					}else{
		    						loginSubmitManagementModel.setUserName(CommonUtility.validateString(getLoginId()));
		    						userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
		    					}
		    					
		    				}
		    				
		    			}else{
		    				
		    				userToken = loginSubmit.ERPLOGIN(loginSubmitManagementModel);
			    		}
		        		
		    		}else{
						userToken = "x";
						session.setAttribute("connectionError", "No");
					}
					
		        	String connectionError = (String) session.getAttribute("connectionError");
			        
		        	if(CommonUtility.validateString(connectionError).equalsIgnoreCase("No") && CommonUtility.validateString(isEclipseDown).equalsIgnoreCase("Y")){
			        	
		        		if(CommonUtility.validateString(userToken).length()>0){
			        		allowDBLogin = true;
			        	}else{
			        		allowDBLogin = false;
			        	}
			        	
			        	loginAvailable = true;
			        	
			        }else{
			        	loginAvailable = false;
			        }
			        
			        if(allowDBLogin && loginAvailable){
			        	
			        	if(CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")  && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPDATE_ERP_PASSWORD_TO_DB")).equalsIgnoreCase("N")){
			        		UsersDAO.updatePasswordWithUserName(CommonUtility.validateString(getLoginId()),getLoginPassword());
			        	}
			        	
			        }
	    		
	
	    	session.setAttribute("userToken", userToken);
	        session.setAttribute("eclipseSessionTime", new java.util.Date());
	        session.removeAttribute("isOciUser");
	        session.removeAttribute("cartCountSession"); 
	        System.out.println("---------- : " + session.getAttribute("userToken"));
	        
	       if(allowDBLogin) {
	    	   loginSuccess=loginAuthentication.authenticate(CommonUtility.validateString(getLoginId()), getLoginPassword(), session);
	       }
	       
	       if(CommonUtility.customServiceUtility() != null && loginSuccess) {
				CommonUtility.customServiceUtility().getUserInfoFromErp(session);
			}
	       
	        if(session.getAttribute("userToken")==null){
	        	System.out.println("Eclipse session is null");
	        }else{
	        	System.out.println("Eclipse session : " + (String)session.getAttribute("userToken"));
	        }
	       
	        		if(loginAvailable){
	        	       	if(loginSuccess)
	        			{	
	        				
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
	            					//if(session.getAttribute("isPunchoutUser")!=null && session.getAttribute("isPunchoutUser").toString().trim().equalsIgnoreCase("N") ){ //- Removed because of introducing Self service punchout 
	    	        				if(!CommonUtility.validateString((String)session.getAttribute("isPunchoutUser")).equalsIgnoreCase("Y") ){
	        							UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Login", session.getId(), ipaddress, "Login", "NA");
	        							
	        							cartClear = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CART_CLEAR"));
	        							if(cartClear!=null && CommonUtility.validateString(cartClear).equalsIgnoreCase("Y")){
	        								 ProductsDAO.clearCart(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)));
	        							}
	        							
	        							int updated = ProductsDAO.updateCart(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), session.getId());
	        							System.out.println("cart updated :: "+updated);
	        							
	        							if(session.getAttribute("firstLogin")!=null && session.getAttribute("firstLogin").toString().trim().equalsIgnoreCase("Y")){
	        								SendMailUtility.welcomeMail((String)session.getAttribute(Global.USERID_KEY));
	        							}
	        							
	        							if(popUpRequest){
	        								renderContent = "";
	        								target="ResultLoader";	
	        							}else{
	        								if(CommonUtility.convertToBoolean(CommonDBQuery.getSystemParamtersList().get("ENABLE_ADDRESS_AUTO_SYNC"))){
	        									
	        									//UPDATE CONTACT FROM CIMM2BC
	        									if(session.getAttribute("erpLoginContactDetails")!=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("cimm2bcentral")){
		        									ArrayList<Cimm2BCentralContact> responseObj = (ArrayList<Cimm2BCentralContact>) session.getAttribute("erpLoginContactDetails");
		        									if(responseObj!=null && !responseObj.isEmpty()){
		        										UsersModel contactDetails = null;
		        										for(Cimm2BCentralContact contactDetail : responseObj){
		        		    		        				if(CommonUtility.validateString(getLoginId()).equalsIgnoreCase(CommonUtility.validateString(contactDetail.getPrimaryEmailAddress()))){
		        		    		        	 					contactDetails = new UsersModel();
		        		    		        	 					contactDetails.setFirstName(CommonUtility.validateString(contactDetail.getFirstName()));
		        		    		        	 					contactDetails.setLastName(CommonUtility.validateString(contactDetail.getLastName()));
		        		    		        	 					contactDetails.setEmailAddress(CommonUtility.validateString(contactDetail.getPrimaryEmailAddress()));
		        		    		        	 					contactDetails.setPhoneNo(CommonUtility.validateString(contactDetail.getPrimaryPhoneNumber()).replaceAll("[^a-zA-Z0-9]", ""));
		        		    		        	 					contactDetails.setFaxNo(CommonUtility.validateString(contactDetail.getFaxNumber()).replaceAll("[^a-zA-Z0-9]", ""));
		        		    		        	 					if(session.getAttribute(Global.USERID_KEY)!=null){
		        		    		        	 						contactDetails.setUserId(CommonUtility.validateNumber(session.getAttribute(Global.USERID_KEY).toString()));
		        		    		        						}
		        		    		        					break;
		        		    		        				}
		        		    		        			}
		        										if(contactDetails!=null){
		        											UsersDAO.updateUserContact(contactDetails);
		        										}
		        									}
		        								}
	        									//UPDATE CONTACT FROM CIMM2BC
	        									
	        									target="addressSync";
	        									if(CommonUtility.convertToBoolean(CommonDBQuery.getSystemParamtersList().get("ENABLE_JOB_ACCOUNTS"))){
	            									if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("cimm2bcentral")){
	            										target="jobAccounts";
	    	        								}
	        									}
	        								}else{
	        									target=SUCCESS;	
	        								}
	        								
	        								if(CommonUtility.validateString(fromPageValue).length()>0){
	        									pageUrl = fromPageValue; 
	        								}else{
	        									pageUrl = "loginPage"; 
	        								}
	        							}
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
	    										WebLogin.webUserSession(currentSessionId);
	    										
	    									}
	        							}
	        							contentObject.put("message", "Unauthorized : Access is denied due to invalid credentials");
										renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject, null, null, null);
	        							if(popUpRequest){
	    									renderContent = result;
	    									target="ResultLoader";	
	    								}else{
	    									target=ERROR;
	    								}
	        						}
	        					}else{
	        						result="No Catalog assigned to this user. Please contact our customer service.";
	        						if(popUpRequest){
										renderContent = result;
										target="ResultLoader";	
									}else{
										try{
											
											boolean logoutSuccess=loginAuthentication.logout(session);
											System.out.println("logoutSuccess:---"+logoutSuccess);
											if(logoutSuccess){
												System.out.println("logoutSuccess:--- Assigning Web Session Values");
												HttpSession sessionForMobile = request.getSession();
												sessionForMobile.setAttribute("browseType", browseType);
												WebLogin.loadWebUser();
												WebLogin.webUserSession(currentSessionId);
											}
											
											
											if(result!=null && result.trim().length()>0){
												contentObject.put("message", result);
												renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject, null, null, null);
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										target=ERROR;
									}
	        					}
	        				
	        				}
	        				else
	        				{
	        					result="Invalid User Name/Password";
	        					if(popUpRequest){
									renderContent = result;
									target="ResultLoader";	
								}else{
									target=ERROR;
								}
	        				}
	        			
	        			}else{
	        				
	        				String eclipseResponse = "";
	        				if(CommonUtility.validateString(userToken).length()>0 && CommonUtility.validateString(erpLogin).equalsIgnoreCase("Y")){
	        					AddressModel addressModel = new AddressModel();
	        					addressModel.setEmailAddress(CommonUtility.validateString(getLoginId()));
	    		        	 	addressModel.setUserPassword(getLoginPassword());
	    		        	 	addressModel.setUserToken(CommonUtility.validateString(userToken));
	    		        	 	addressModel.setExistingUser("Y");
	    		        	 	addressModel.setParentUserId(CommonUtility.validateParseIntegerToString(0));
	    		        	 	addressModel.setUserStatus("Y");
	        					
	    		        	 	
	    		        	 	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("cimm2bcentral")){
	    		        	 		
	    		        	 		UsersModel userDetails = new UsersModel();
	    		        	 		
	    		        	 		if(session!=null && session.getAttribute("erpLoginContactDetails")!=null){
	    		        	 			
	    		        	 			ArrayList<Cimm2BCentralContact> responseObj = (ArrayList<Cimm2BCentralContact>) session.getAttribute("erpLoginContactDetails");
	    		        	 			
	    		        	 			for(Cimm2BCentralContact contactDetail : responseObj){
	    		        	 				
	    		        	 				if(CommonUtility.validateString(getLoginId()).equalsIgnoreCase(CommonUtility.validateString(contactDetail.getPrimaryEmailAddress()))){
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
	    		        	 		 
	    		        	 		 userDetails.setEmailAddress(getLoginId());
	    		        	 		 userDetails.setSession(session);
	    		        	 		 userDetails.setEntityId(CommonUtility.validateString(session.getAttribute("loginCustomerERPId").toString()));
	    		                     userDetails.setAccountName(CommonUtility.validateString(session.getAttribute("loginCustomerERPId").toString()));
	    							 //userDetails.setEntityId(CommonUtility.validateString(userToken));
	    							 //userDetails.setAccountName(CommonUtility.validateString(userToken));
	    							 userDetails.setPassword(getLoginPassword());
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
	    							 if(CommonDBQuery.getSystemParamtersList().get("CommercialCustomerRegistrationStatus")!=null && CommonDBQuery.getSystemParamtersList().get("CommercialCustomerRegistrationStatus").trim().length()>0){
	    					              userDetails.setUserStatus(CommonDBQuery.getSystemParamtersList().get("CommercialCustomerRegistrationStatus"));
	    					         }
	    							 if(CommonUtility.validateString(getWoeLogin()).equalsIgnoreCase("Y")){//WOE Login For Eclipse Customers
		    							 userDetails.setWoeLogin("Y");
		    							 userDetails.setWoeUserName(getWoeUserName());
		    							 userDetails.setWoePassword(getWoePassword());
		    							 userDetails.setWoeConfirmPassword(getWoeConfirmPassword());
		    							 userDetails.setUserStatus("Y");//For WOE Login
		    							 setLoginId(getWoeUserName());
		    							 setLoginPassword(getWoePassword());//Setting updated userName and password for authentication
		    						}
	    							 
	    		        	 		UserManagement usersObj = new UserManagementImpl();
	    		        	 		eclipseResponse = usersObj.newOnAccountUserRegistration(userDetails);
	    		        	 		
	    		        	 	}else{
	    		        	 		//eclipseResponse = UserRegisterUtility.registerUserInDB(getLoginId(), getLoginPassword(),userToken,"Y",0,"Y");
	        		        	 	eclipseResponse = UserRegisterUtility.registerUserInDB(addressModel);
	    		        	 	}
	    		        	 	
	    		        	 	
	    		        	 if(eclipseResponse!=null && eclipseResponse.trim().contains("successfully"))
	        					loginSuccess = true;
	        				}
	        				
	        				
	        				if(loginSuccess){
	        					loginSuccess=loginAuthentication.authenticate(CommonUtility.validateString(getLoginId()), getLoginPassword(), session);
	        				}
	        				
	        				if(!loginSuccess)
	        				{
	        					
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
	        					if(popUpRequest){
									renderContent = result;
									target="ResultLoader";	
								}else{
									try{
										if(!CommonUtility.validateString(continueSession).equalsIgnoreCase("Y")){
											boolean logoutSuccess=loginAuthentication.logout(session);
											System.out.println("logoutSuccess:---"+logoutSuccess);
											if(logoutSuccess){
												System.out.println("logoutSuccess:--- Assigning Web Session Values");
												HttpSession sessionForMobile = request.getSession();
												sessionForMobile.setAttribute("browseType", browseType);
												WebLogin.loadWebUser();
												WebLogin.webUserSession(currentSessionId);
											}
										}
										if(result!=null && result.trim().length()>0){
											contentObject.put("message", result);
											contentObject.put("continueSession", CommonUtility.validateString(continueSession));
											contentObject.put("fromPageValue", fromPageValue);
											if(CommonUtility.validateString(fromPageValue).equalsIgnoreCase("wlCheckout")){
												renderContent = LayoutGenerator.templateLoader("WLCheckout", contentObject, null, null, null);
											}else{
												renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject, null, null, null);
											}
										}
									}catch (Exception e) {
										e.printStackTrace();
									}
									target=ERROR;
								}
	        				}else{
	        					
	        					String tempSubset = (String) session.getAttribute("userSubsetId");
	        				    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	        					int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
	        					int subsetId = CommonUtility.validateNumber(tempSubset);
	        					ArrayList<Integer> userTab = ProductsDAO.getTopTab(subsetId, generalSubset);
	        					session.setAttribute("userTabList", userTab);
	        					//if(session.getAttribute("isPunchoutUser")!=null && session.getAttribute("isPunchoutUser").toString().trim().equalsIgnoreCase("N") ){
	        					if(!CommonUtility.validateString((String)session.getAttribute("isPunchoutUser")).equalsIgnoreCase("Y") ){
	        						UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Login", session.getId(), ipaddress, "Login", "NA");
	        						if(cartClear.equalsIgnoreCase("Y")){
	        							ProductsDAO.clearCart(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)));
	        						}
	        						if(session.getAttribute("firstLogin")!=null && session.getAttribute("firstLogin").toString().trim().equalsIgnoreCase("Y")){
	        							SendMailUtility.welcomeMail((String)session.getAttribute(Global.USERID_KEY));
	        						}
	        						if(popUpRequest && UsersDAO.isSalesUser(session)) {
	        							renderContent = UsersDAO.getSalesUserDetails(session);
	        							target="ResultLoader";	
	        						}else if(popUpRequest){
	        							renderContent = "";
	        							target="ResultLoader";	
	        						}else{
	        							if(CommonUtility.validateString(fromPageValue).length()>0){
	    									pageUrl = fromPageValue; 
	    								}else{
	    									pageUrl = "loginPage"; 
	    								}
	        							if(CommonUtility.convertToBoolean(CommonDBQuery.getSystemParamtersList().get("ENABLE_ADDRESS_AUTO_SYNC"))){
	    									target="addressSync";
	    								}else{
	    									target=SUCCESS;	
	    								}
	        						}
	
	        					}else{
	        						result="Invalid User Name/Password.";
	        						if(popUpRequest){
	        							renderContent = result;
	        							target="ResultLoader";	
	        						}else{
	        							try{
	
	        								boolean logoutSuccess=loginAuthentication.logout(session);
	        								System.out.println("logoutSuccess:---"+logoutSuccess);
	        								if(logoutSuccess){
	        									System.out.println("logoutSuccess:--- Assigning Web Session Values");
	        									HttpSession sessionForMobile = request.getSession();
	        									sessionForMobile.setAttribute("browseType", browseType);
	        									WebLogin.loadWebUser();
	        									WebLogin.webUserSession(currentSessionId);
	        								}
	
	
	        								if(result!=null && result.trim().length()>0){
	        									contentObject.put("message", result);
	        									renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject, null, null, null);
	        								}
	        							}catch (Exception e) {
	        								e.printStackTrace();
	        							}
	
	        							target=ERROR;
	        						}
	        					}
	        				}
	        			}
	        	        
	        		//********************//
	        	}else{
	            	if(loginSuccess){
	        			String tempSubset = (String) session.getAttribute("userSubsetId");
	        		    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	        			
	        			if(tempSubset!=null && !tempSubset.trim().equalsIgnoreCase(""))
	        			{
	        				int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
	        				int subsetId = CommonUtility.validateNumber(tempSubset);
	        				//ArrayList<Integer> userTab = ProductsDAO.getTopTab(subsetId, generalSubset);
	        				//session.setAttribute("userTabList", userTab);
	        				
	        				//if(session.getAttribute("isPunchoutUser")!=null && session.getAttribute("isPunchoutUser").toString().trim().equalsIgnoreCase("N") ){ //- Removed because of introducing Self service punchout 
	        				if(!CommonUtility.validateString((String)session.getAttribute("isPunchoutUser")).equalsIgnoreCase("Y") ){
	        					UsersDAO.updateUserLog(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), "Login", session.getId(), ipaddress, "Login", "NA");
	
	        					cartClear = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CART_CLEAR"));
	        					if(CommonUtility.validateString(cartClear).equalsIgnoreCase("Y")){
	        						ProductsDAO.clearCart(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)));
	        					}
	        					int updated = 0;
	        					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("UPDATE_CART_DURING_LOGIN")).equalsIgnoreCase("N")) {
	        						//CustomServiceProvider
	        						if(CommonUtility.customServiceUtility()!=null) {
	        							int count = CommonUtility.customServiceUtility().checkItemsFordifferentLocation(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), session.getId(),session, "");
	        							if(count > 1) {
	        								session.setAttribute("itemsWithDifferentLocation", 'Y');
	        							}
	        						}
	        						//CustomServiceProvider
	        					}else {
	        						System.out.println("cart updated :else block: "+updated);
	        						updated = ProductsDAO.updateCart(CommonUtility.validateNumber((String)session.getAttribute(Global.USERID_KEY)), session.getId());
	        					}
	        					System.out.println("cart updated :: "+updated);
	        					if(session.getAttribute("firstLogin")!=null && session.getAttribute("firstLogin").toString().trim().equalsIgnoreCase("Y")){
	        						SendMailUtility.welcomeMail((String)session.getAttribute(Global.USERID_KEY));
	        					}
	        					if(popUpRequest && UsersDAO.isSalesUser(session)) {
        							renderContent = UsersDAO.getSalesUserDetails(session);
        							target="ResultLoader";	
        						}else if(popUpRequest) {
	        						if(CommonUtility.validateString(enteredUserNameInfo.getStatusDescription()).equalsIgnoreCase("P")) 
	        				        {
	        				        	result = "partial";
	        				        	renderContent ="partial";
	        				        }else{
	        				        	renderContent = "";
		        						target="ResultLoader";
	        				        }
	
	        					}else{
	        						if(CommonUtility.convertToBoolean(CommonDBQuery.getSystemParamtersList().get("ENABLE_ADDRESS_AUTO_SYNC"))){
										target="addressSync";
									}else{
										target=SUCCESS;	
									}
	        						if(CommonUtility.validateString(fromPageValue).length()>0){
										pageUrl = fromPageValue; 
									}
	        					}
	        				}
	        				else
	        				{
	        					result="Invalid User Name/Password.";
	        					if(popUpRequest){
									renderContent = result;
									target="ResultLoader";	
								}else{
									try{
										
										boolean logoutSuccess=loginAuthentication.logout(session);
										System.out.println("logoutSuccess:---"+logoutSuccess);
										if(logoutSuccess){
											System.out.println("logoutSuccess:--- Assigning Web Session Values");
											HttpSession sessionForMobile = request.getSession();
											sessionForMobile.setAttribute("browseType", browseType);
											WebLogin.loadWebUser();
											WebLogin.webUserSession(currentSessionId);
										}
										
										
										if(result!=null && result.trim().length()>0){
											contentObject.put("message", result);
											renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject, null, null, null);
										}
									}catch (Exception e) {
										e.printStackTrace();
									}
									target=ERROR;
								}
	        				}
	        			}
	        			else
	        			{
	        				result="No subset assigned to this user. Please contact our customer service.";
	        				if(popUpRequest){
								renderContent = result;
								target="ResultLoader";	
							}else{
								try{
									
									boolean logoutSuccess=loginAuthentication.logout(session);
									System.out.println("logoutSuccess:---"+logoutSuccess);
									if(logoutSuccess){
										System.out.println("logoutSuccess:--- Assigning Web Session Values");
										HttpSession sessionForMobile = request.getSession();
										sessionForMobile.setAttribute("browseType", browseType);
										WebLogin.loadWebUser();
										WebLogin.webUserSession(currentSessionId);
									}
									
									
									if(result!=null && result.trim().length()>0){
										contentObject.put("message", result);
										renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject, null, null, null);
									}
								}catch (Exception e) {
									e.printStackTrace();
								}
								target=ERROR;
							}
	        			}
	        		
	                	}else{
	                		result="Invalid User Name/Password.";
	                		if(popUpRequest){
	                			if(CommonUtility.validateString(enteredUserNameInfo.getStatusDescription()).equalsIgnoreCase("P")) 
        				        {
        				        	result = "partial";
        				        	renderContent ="partial";
        				        }else{
								renderContent = result;
								target="ResultLoader";
        				        }
							}else{
								try{
									
									boolean logoutSuccess=loginAuthentication.logout(session);
									System.out.println("logoutSuccess:---"+logoutSuccess);
									if(logoutSuccess){
										System.out.println("logoutSuccess:--- Assigning Web Session Values");
										HttpSession sessionForMobile = request.getSession();
										sessionForMobile.setAttribute("browseType", browseType);
										WebLogin.loadWebUser();
										WebLogin.webUserSession(currentSessionId);
									}
									
									
									if(result!=null && result.trim().length()>0){
										contentObject.put("message", result);
										renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject, null, null, null);
									}
								}catch (Exception e) {
									e.printStackTrace();
								}
								target=ERROR;
							}
	                	}
	                }
	        } else {
	        	result="Invalid User Name/Password.";
	        	if(popUpRequest){
					renderContent = result;
					target="ResultLoader";
					UnilogFactoryInterface serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
					if(serviceClass!=null) {
						String loginContent = serviceClass.partialUserLogin(enteredUserNameInfo);
						if(loginContent!=null && loginContent.length()>0){
							renderContent = loginContent;
							target="ResultLoader";
						}
					}

				}else{
					
					try{
						
						boolean logoutSuccess=loginAuthentication.logout(session);
						System.out.println("logoutSuccess:---"+logoutSuccess);
						if(logoutSuccess){
							System.out.println("logoutSuccess:--- Assigning Web Session Values");
							HttpSession sessionForMobile = request.getSession();
							sessionForMobile.setAttribute("browseType", browseType);
							WebLogin.loadWebUser();
							WebLogin.webUserSession(currentSessionId);
						}
						
						
						if(result!=null && result.trim().length()>0){
							contentObject.put("message", result);
							renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject, null, null, null);
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
					target=ERROR;
				}
		   }
		}else{
				
				String userName = "";
				String userPassword = "";
				if(getLoginId()!=null){
					userName = CommonUtility.validateString(getLoginId());
				}
				if(getLoginPassword()!=null){
					userPassword = getLoginPassword().trim();
				}
				if(userName.trim().length()<1 && userPassword.trim().length()<1){
					result="Enter User Name & Password";
				}else if(userPassword.equalsIgnoreCase("S7Hk2o4-OHAid16RW7D4F_k4dTbAJmxDlWjvulF1qjI")){
					result="Invalid Username/Password";
				}else{
					if(userName.trim().length()<1){
						result="Enter User Name";
					}
					if(userPassword.trim().length()<1){
						result="Enter Password";//+userName.trim();
					}
				}
				
				if(popUpRequest){
					renderContent = result;
					target="ResultLoader";	
				}else{
					target=ERROR;
					try{
						if(!CommonUtility.validateString(fromPageValue).equalsIgnoreCase("wlCheckout")){
							loginAuthentication = new LoginAuthentication();
							boolean logoutSuccess=loginAuthentication.logout(session);
							System.out.println("logoutSuccess:---"+logoutSuccess);
							if(!CommonUtility.validateString(fromPageValue).equalsIgnoreCase("wlCheckout") && logoutSuccess){
								System.out.println("logoutSuccess:--- Assigning Web Session Values");
								HttpSession sessionForMobile = request.getSession();
								sessionForMobile.setAttribute("browseType", browseType);
								WebLogin.loadWebUser();
								WebLogin.webUserSession(currentSessionId);
							}
						}
	
						if(result!=null && result.trim().length()>0){
							contentObject.put("message", result);
							contentObject.put("userNameTmp", CommonUtility.validateString(getLoginId()));
							if(CommonUtility.validateString(fromPageValue).equalsIgnoreCase("wlCheckout")){
								renderContent = LayoutGenerator.templateLoader("WLCheckout", contentObject, null, null, null);
							}else{
								renderContent = LayoutGenerator.templateLoader("LoginPage", contentObject, null, null, null);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				
		}
		try {
			if(CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY))>1 && CommonUtility.validateString((String) session.getAttribute("changePasswordOnLogin")).equalsIgnoreCase("1")) {
				System.out.println("changePasswordOnLogin Called \t"+CommonUtility.validateString((String) session.getAttribute("changePasswordOnLogin")));
				String username = CommonUtility.validateString((String) session.getAttribute(Global.USERNAME_KEY));
				String emailAddress = CommonUtility.validateString((String) session.getAttribute("userEmailAddress"));
				if(CommonUtility.validateEmail(emailAddress)){
					UsersModel userInfo = UsersDAO.forgotPassword(username, emailAddress);
					Date timeNow = new Date();
					userInfo.setaRPassword(SecureData.getPunchoutSecurePassword(timeNow.toString()));
					userInfo.setEmailAddress(emailAddress);
					int results = UsersDAO.advancedForgotPasswordInsert(userInfo);
					if(results>0){
						renderContent = "changePasswordOnLogin";
						target = "changePasswordOnLogin";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(CommonUtility.customServiceUtility() != null) {
			CommonUtility.customServiceUtility().checkBCAddressCustomFieldsAfterAutoSync(session);//Electrozad Custom Service
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	    return target;
	}
}