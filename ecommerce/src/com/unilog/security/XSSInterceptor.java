package com.unilog.security;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

public class XSSInterceptor implements Interceptor{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String result = "";
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
              if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SECURITY_POLICY")).equalsIgnoreCase("Y")){
            	  entry.setValue(CommonUtility.escapeHtml(value)); //Transcoding the submitted string
              }
          }
        }
        result = invocation.invoke();
		return result;
	}
}
