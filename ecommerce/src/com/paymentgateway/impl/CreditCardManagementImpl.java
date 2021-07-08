package com.paymentgateway.impl;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.model.SalesOrderManagementModel;
import com.paymentgateway.model.CreditCardManagementModel;
import com.paymentgateway.service.CreditCardManagement;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.SalesModel;

public class CreditCardManagementImpl implements CreditCardManagement {
	
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

	public CreditCardModel creditCardTransactionSetup(CreditCardManagementModel creditCartInput) {

		CreditCardModel creditCardOutput = new CreditCardModel();
		
		String paymentGateway = null;
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("PAYMENT_GATEWAY")!=null && session.getAttribute("PAYMENT_GATEWAY").toString().trim().length()>0){
			paymentGateway = session.getAttribute("PAYMENT_GATEWAY").toString().trim();
		}
		if(paymentGateway!=null && paymentGateway.trim().length()>0){
			Class<?>[] paramObject = new Class[1];	
			paramObject[0] = CreditCardManagementModel.class;
			
			Object[] arguments = new Object[1];
			arguments[0] = creditCartInput;
	
			try
			{
				Class<?> cls = Class.forName("com.paymentgateway."+paymentGateway+".action.CreditCardManagementAction");
				Object obj = cls.newInstance();
				Method method = cls.getDeclaredMethod("creditCardTransactionSetup", paramObject);
				creditCardOutput = (CreditCardModel) method.invoke(obj, arguments);
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		return creditCardOutput;
	}

}
