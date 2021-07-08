package com.unilog.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.unilog.defaults.Global;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

public class UserAuthority implements Interceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 857830881812484755L;

	@Override
	public void destroy() {
		
		
	}

	@Override
	public void init() {
		
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String result = "";
		try{
			if(session.getAttribute(Global.USERID_KEY)!=null)
			{
				  String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
					int userId = CommonUtility.validateNumber(sessionUserId);
					if(userId>2){
						result = invocation.invoke();
					}else{
						result = "unAuthorized";
					}
						
			}else{
				result = "unAuthorized";
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
		
		return result;
	}

}
