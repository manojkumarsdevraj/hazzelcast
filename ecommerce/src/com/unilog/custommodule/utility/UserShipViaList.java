package com.unilog.custommodule.utility;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unilog.database.CommonDBQuery;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class UserShipViaList {

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

	public static ArrayList<ShipVia> getUserShipViaList(UsersModel customerInfoInput) {
		ArrayList<ShipVia> shipVia = new ArrayList<ShipVia>();
		// String parameter
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = UsersModel.class;

		Object[] arguments = new Object[1];
		arguments[0] = customerInfoInput;

		try {
			String version = "V1";
			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GET_USER_SHIPVIA_LIST")).length()>0) {
				version = CommonDBQuery.getSystemParamtersList().get("GET_USER_SHIPVIA_LIST_VERSION");
			}
			Class<?> cls = Class.forName("com.unilog.custommodule.utility.UserShipViaList");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getUserShipViaList"+version, paramObject);
			shipVia = (ArrayList<ShipVia>) method.invoke(obj, arguments);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shipVia;
	}
}
