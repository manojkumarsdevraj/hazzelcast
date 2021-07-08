package com.unilog.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.unilog.defaults.Global;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class UserStatusCheck  implements Interceptor{

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
			if(session.getAttribute(Global.USERID_KEY)!=null){
				int userId = CommonUtility.validateNumber(session.getAttribute(Global.USERID_KEY).toString());
				if(userId>2 && session.getAttribute(Global.USERNAME_KEY)!=null){
					UsersDAO usersDAO = new UsersDAO();
					 UsersModel userDetails = usersDAO.getUserStatus(CommonUtility.validateString(session.getAttribute(Global.USERNAME_KEY).toString()), false);
					 if(userDetails!=null && userDetails.getUserId()>0){
						 result = invocation.invoke();
					 }else{
						 result = "";
					 }
				}else{
					result = invocation.invoke();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

}
