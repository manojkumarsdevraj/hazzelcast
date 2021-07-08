package com.unilog.ecommerce.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.ecommerce.user.model.AccessPermissionFactory;
import com.unilog.ecommerce.user.model.User;
import com.unilog.ecommerce.user.service.UserService;
import com.unilog.ecommerce.user.service.UserServiceFacotry;
import com.unilog.ecommerce.user.validation.RegValidatorFactory;
import com.unilog.ecommerce.user.validation.RegistrationValidator;
import com.unilog.ecommerce.user.validation.util.RegistrationUtils;
import com.unilog.ecommerce.validataion.ValidationStatus;

public class Registration extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Registration.class);
	private String renderContent = "";
	private HttpServletRequest request;
	
	private User user;
	
	private RegistrationValidator validator;
	private ValidationStatus validationStatus;
	private Gson gson = new Gson();
	
	public String b2bOnAccount() {
		request = ServletActionContext.getRequest();
	    HttpSession session = request.getSession(); 
	    String locale = session.getAttribute("localeCode").toString().toUpperCase();
	    if(user == null) {
	    	user = gson.fromJson(new JsonParser().parse(request.getParameter("user")), User.class);
	    }
	    //Locale localeObj = ActionContext.getContext().getLocale(); //-- alternative for locale object can be used
	    String requestValidatorType = CommonDBQuery.getSystemParamtersList().get("REGISTERATION_VALIDATOR_TYPE");
	    if(requestValidatorType == null || requestValidatorType.trim().length() <= 0) {
	    	requestValidatorType = "GENERIC";
		}
	    validator = RegValidatorFactory.getValidator(requestValidatorType);
	    validationStatus = validator.validateB2bOnAccount(user, locale);
	    if(validationStatus.isValid()) {
	    	logger.info(user);
	    	validationStatus.setSourceFormId(request.getParameter("sourceFormId"));
	    	String userServiceType = CommonDBQuery.getSystemParamtersList().get("USER_REG_SERVICE_TYPE");
	    	user.setAccessPermission(AccessPermissionFactory.getUserAccessRight("B2BONACCOUNT"));
	    	String userStatus = CommonDBQuery.getSystemParamtersList().get("B2B_ON_ACCOUNT_USER_STATUS");
	    	userStatus = (userStatus != null)? userStatus : "Y";
	    	user.setStatus(userStatus);
	    	UserService userService = UserServiceFacotry.getUserService(userServiceType);
	    	boolean isRegistered = userService.b2bOnAccountService(user);
	    	if(isRegistered) {
	    		validationStatus.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("registeration.b2bOnAccount.success"));
	    		RegistrationUtils.notifyRegistration(validationStatus.getSourceFormId(), user, "ALL");
	    	}else {
	    		validationStatus.setValid(false);
	    		validationStatus.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("registeration.b2bOnAccount.failure"));
	    	}
	    }
	    validationStatus.setSourceObj(gson.toJson(user));
	    renderContent = gson.toJson(validationStatus);
		return SUCCESS;
	}
	
	public String b2bNewAccount() {
		request = ServletActionContext.getRequest();
	    HttpSession session = request.getSession(); 
	    String locale = session.getAttribute("localeCode").toString().toUpperCase();
	    if(user == null) {
	    	user = gson.fromJson(new JsonParser().parse(request.getParameter("user")), User.class);
	    }
	    String requestValidatorType = CommonDBQuery.getSystemParamtersList().get("REGISTERATION_VALIDATOR_TYPE");
	    if(requestValidatorType == null || requestValidatorType.trim().length() <= 0) {
	    	requestValidatorType = "GENERIC";
		}
	    validator = RegValidatorFactory.getValidator(requestValidatorType);
	    validationStatus = validator.validateB2bNewAccount(user, locale);
	    if(validationStatus.isValid()) {
	    	logger.info(user);
	    	validationStatus.setSourceFormId(request.getParameter("sourceFormId"));
	    	String userServiceType = CommonDBQuery.getSystemParamtersList().get("USER_REG_SERVICE_TYPE");
	    	user.setAccessPermission(AccessPermissionFactory.getUserAccessRight("B2BNEWACCOUNT"));
	    	String userStatus = CommonDBQuery.getSystemParamtersList().get("B2B_NEW_ACCOUNT_USER_STATUS");
	    	userStatus = (userStatus != null)? userStatus : "Y";
	    	user.setStatus(userStatus);
	    	UserService userService = UserServiceFacotry.getUserService(userServiceType);
	    	boolean isRegistered = userService.b2bNewAccountService(user);
	    	if(isRegistered) {
	    		validationStatus.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("registeration.b2bNewAccount.success"));
	    		RegistrationUtils.notifyRegistration(validationStatus.getSourceFormId(), user, "ALL");
	    	}else {
	    		validationStatus.setValid(false);
	    		validationStatus.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("registeration.b2bNewAccount.failure"));
	    	}
	    }
	    validationStatus.setSourceObj(gson.toJson(user));
	    renderContent = gson.toJson(validationStatus);
		return SUCCESS;
	}
	
	public String b2cRetail() {
		request = ServletActionContext.getRequest();
	    HttpSession session = request.getSession(); 
	    String locale = session.getAttribute("localeCode").toString().toUpperCase();
	    if(user == null) {
	    	user = gson.fromJson(new JsonParser().parse(request.getParameter("user")), User.class);
	    }
	    String requestValidatorType = CommonDBQuery.getSystemParamtersList().get("REGISTERATION_VALIDATOR_TYPE");
	    if(requestValidatorType == null || requestValidatorType.trim().length() <= 0) {
	    	requestValidatorType = "GENERIC";
		}
	    validator = RegValidatorFactory.getValidator(requestValidatorType);
	    validationStatus = validator.validateB2cRetail(user, locale);
	    if(validationStatus.isValid()) {
	    	logger.info(user);
	    	validationStatus.setSourceFormId(request.getParameter("sourceFormId"));
	    	String userServiceType = CommonDBQuery.getSystemParamtersList().get("USER_REG_SERVICE_TYPE");
	    	user.setAccessPermission(AccessPermissionFactory.getUserAccessRight("B2CRETAIL"));
	    	String userStatus = CommonDBQuery.getSystemParamtersList().get("B2C_USER_STATUS");
	    	userStatus = (userStatus != null)? userStatus : "Y";
	    	user.setStatus(userStatus);
	    	UserService userService = UserServiceFacotry.getUserService(userServiceType);
	    	boolean isRegistered = userService.b2cRetailService(user);
	    	if(isRegistered) {
	    		validationStatus.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("registeration.b2cRetail.success"));
	    		RegistrationUtils.notifyRegistration(validationStatus.getSourceFormId(), user, "ALL");
	    	}else {
	    		validationStatus.setValid(false);
	    		validationStatus.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("registeration.b2cRetail.failure"));
	    	}
	    }
	    validationStatus.setSourceObj(gson.toJson(user));
	    renderContent = gson.toJson(validationStatus);
		return SUCCESS;
	}
	
	public String b2bCreditRequest() {
		//request = ServletActionContext.getRequest();
	    //HttpSession session = request.getSession(); 
	   // String locale = session.getAttribute("localeCode").toString().toUpperCase();
		return SUCCESS;
	}
	
	public String customerImport() {
		request = ServletActionContext.getRequest();
	    String userServiceType = CommonDBQuery.getSystemParamtersList().get("USER_REG_SERVICE_TYPE");
	    UserService userService = UserServiceFacotry.getUserService(userServiceType);
	    userService.customerImport(request);
		return SUCCESS;
	}
	
	public String getRenderContent() {
		return renderContent;
	}
	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
