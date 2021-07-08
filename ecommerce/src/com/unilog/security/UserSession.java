package com.unilog.security;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.users.WebLogin;
import com.unilog.utility.CommonUtility;
import com.opensymphony.xwork2.ActionContext;
import java.util.Map.Entry;
import java.util.Map;
import com.unilog.products.XSSRequestWrappers;

public class UserSession  implements Interceptor{
	private static final long serialVersionUID = 1L;
	static final String[] MOBILE_SPECIFIC_SUBSTRING = {
		"iPhone","Android","MIDP","Opera Mobi","IPad",
		"Opera Mini","BlackBerry","HP iPAQ","IEMobile",
		"MSIEMobile","Windows Phone","HTC","LG",
		"MOT","Nokia","Symbian","Fennec",
		"Maemo","Tear","Midori","armv",
		"Windows CE","WindowsCE","Smartphone","240x320",
		"176x220","320x320","160x160","webOS",
		"Palm","Sagem","Samsung","SGH",
		"SonyEricsson","MMP","UCWEB"};
	private static String[] noProductsAction = {"welcome.action","staticpages.action","registerlink.action","staticpage.action","location.action","CustomLocationsMap.action","callUnit.action","listStaticPageCms.action","generateFromCms.action","generateTemplateWidget.action","cmsStaticPageCms.action","bannerApi.action","formListCms.action","Cms.action","Widget.action","staticSearchPage.action", "customUtil.action" };
	static final List<String> noProductsActionList = Arrays.asList(noProductsAction);  
	private static String[] loginOnly = {"loginlink.action","forgotLink.action","registerlink.action","forgotpasswordunit.action","advforgotpasswordlink.action","advforgotpassworddisableunit.action","callUnit.action"};
	static final List<String> loginOnlyList = Arrays.asList(loginOnly);  
	private String[] staticPageOnlyAction = {"propertyLoaderLink.action","staticpages.action","ForgotLink.action","RegisterLink.action","ForgotPasswordUnit.action","AdvForgotPasswordLink.action","AdvForgotPasswordDisableUnit.action","callUnit.action"};
	static final String[] APP_SPECIFIC_SUBSTRING = {"cfnetwork","darwin"};
	private String nextPage;

	public void destroy() {
		

	}

	public void init() {
		
		
		
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		//String className = invocation.getAction().getClass().getName();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String result = "";
		XSSRequestWrappers xssFilter = new XSSRequestWrappers(request);
		System.out.println("Welcome to UniLog Logs");
		System.out.println("Request URL:" +request.getRequestURI()+":"+request.getAttribute("javax.servlet.forward.request_uri"));
		String userAgent = request.getHeader("User-Agent");
		userAgent = CommonUtility.validateString(userAgent).toLowerCase();
		HttpServletResponse response = ServletActionContext.getResponse();
		//String checkApp = request.getParameter("checkApp");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
		String appAgentName = "UnilogMobileApps";
		session.setAttribute("browseType", "");
		System.out.println("user Agent : " + userAgent);
		ActionContext actionContext = invocation.getInvocationContext();
        Map<String, Object> map = actionContext.getParameters();
        for (Entry<String, Object> entry : map.entrySet()) {
          if(!CommonUtility.escapeFields(entry.getKey())){
              String value = ((String[])(entry.getValue()))[0];
              String valueArray[] = ((String[])(entry.getValue()));
              if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SECURITY_POLICY")).equalsIgnoreCase("Y")){
            	  if(valueArray!=null && valueArray.length>1) {
            		  entry.setValue(CommonUtility.setArrayValueFields(valueArray));
            	  }else {
            		  entry.setValue(xssFilter.stripXSS(value)); //Transcoding the submitted string 
            	  }
              }
          }
        }

		if(userAgent!=null && (userAgent.toUpperCase().contains(appAgentName.toUpperCase())||userAgent.toLowerCase().contains(appAgentName.toLowerCase()))){
			session.setAttribute("browseType", "Mobile");
		}else{
			for(String mobile: MOBILE_SPECIFIC_SUBSTRING){
				if (userAgent.contains(mobile)|| userAgent.contains(mobile.toUpperCase())|| userAgent.contains(mobile.toLowerCase())){
					System.out.println("Browse type : " + mobile);
					session.setAttribute("browseType", "phone");
					break;
				}
			}
		}

		boolean containsPath = false;
		System.out.println("session.getId() @ intercept : "+session.getId());
		String contPathcheck = request.getRequestURI().replace("/", "");
		String beforeLoginNavigation = CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_NAVIGATION");
		if(session.getAttribute(Global.USERID_KEY)==null)
		{
			session.removeAttribute("cartCountSession");
			String contPath = request.getContextPath().replace("/", "");
			session.setAttribute("contPath", contPath+"_");
			//WebLogin.loadWebUser(); 
			WebLogin.webUserSession(session.getId());
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			if(beforeLoginNavigation!=null && beforeLoginNavigation.trim().equalsIgnoreCase("N"))
			{
				for (String path : loginOnlyList) {
					if(contPathcheck.toLowerCase().contains(CommonUtility.validateString(path).toLowerCase())){
						containsPath = true;
						break;
					}
				}
				if(CommonUtility.validateNumber(sessionUserId) > 2 ){
					result = invocation.invoke();
				}else if(containsPath){
					result = invocation.invoke();
				}else{
					result = "userLogin";
				}
			}else if(beforeLoginNavigation!=null && beforeLoginNavigation.trim().equalsIgnoreCase("STATIC_PAGES_ONLY")){
				for (String path : staticPageOnlyAction) {
					if(contPathcheck.toLowerCase().contains(CommonUtility.validateString(path).toLowerCase())){
						containsPath = true;
						break;
					}
				}
				for (String path : loginOnlyList) {
					if(contPathcheck.toLowerCase().contains(CommonUtility.validateString(path).toLowerCase())){
						containsPath = true;
						break;
					}
				}
				if(CommonUtility.validateNumber(sessionUserId) ==1){
					if(containsPath){
						result = invocation.invoke();
					}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_NAVIGATION_PAGE")).length()>0){
						result = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_NAVIGATION_PAGE"));
					}else{
						result = "userLogin";
					}
				}else if(CommonUtility.validateNumber(sessionUserId) >2 ){
					result = invocation.invoke();
				}else{
					result = "userLogin";
				}

			}else{
				/*LoginAuthentication getWebUser = new LoginAuthentication();
				getWebUser.authenticate("web", "web", session);*/
				result = invocation.invoke();

			}
		}else{
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			if(beforeLoginNavigation!=null && beforeLoginNavigation.trim().equalsIgnoreCase("N"))
			{
				for(String path:loginOnlyList){
					if(contPathcheck.toLowerCase().contains(CommonUtility.validateString(path).toLowerCase())){
						containsPath = true;
						break;
					}
				}
				if(CommonUtility.validateNumber(sessionUserId) > 2){
					result = invocation.invoke();
				}else if(containsPath){
					result = invocation.invoke();
				}else{
					result = "userLogin";
				}
			}else if(beforeLoginNavigation!=null && beforeLoginNavigation.trim().equalsIgnoreCase("NO_PRODUCTS")){
				for (String path : noProductsActionList) {
					if(contPathcheck.toLowerCase().contains(CommonUtility.validateString(path).toLowerCase())){
						containsPath = true;
						break;
					}
				}
				for(String path:loginOnlyList){
					if(contPathcheck.toLowerCase().contains(CommonUtility.validateString(path).toLowerCase())){
						containsPath = true;
						break;
					}
				}
				if(CommonUtility.validateNumber(sessionUserId) ==1 && containsPath){
					result = invocation.invoke();
				}else if(CommonUtility.validateNumber(sessionUserId) >2 ){
					result = invocation.invoke();
				}else{
					result = "userLogin";
				}

			}else if(beforeLoginNavigation!=null && beforeLoginNavigation.trim().equalsIgnoreCase("STATIC_PAGES_ONLY")){
				for (String path : staticPageOnlyAction) {
					if(contPathcheck.toLowerCase().contains(CommonUtility.validateString(path).toLowerCase())){
						containsPath = true;
						break;
					}
				}
				for(String path:loginOnlyList){
					if(contPathcheck.toLowerCase().contains(CommonUtility.validateString(path).toLowerCase())){
						containsPath = true;
						break;
					}
				}
				if(CommonUtility.validateNumber(sessionUserId) ==1){
					if(containsPath){
						result = invocation.invoke();
					}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_NAVIGATION_PAGE")).length()>0){
						result = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_NAVIGATION_PAGE"));
					}else{
						result = "userLogin";
					}
				}else if(CommonUtility.validateNumber(sessionUserId) >2 ){
					result = invocation.invoke();
				}else{
					result = "userLogin";
				}
			}else{
				result = invocation.invoke();
			}
		}
		if(result!=null && result.equalsIgnoreCase("userLogin") && !contPathcheck.toLowerCase().contains("login") && request.getAttribute("javax.servlet.forward.request_uri")!=null){
			session.setAttribute("redirectURL", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")+CommonUtility.validateString((String) request.getAttribute("javax.servlet.forward.request_uri")));
		}
		return result;
	}

	/**
	 * @return the nextPage
	 */
	public String getNextPage() {
		return nextPage;
	}

	/**
	 * @param nextPage the nextPage to set
	 */
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
}