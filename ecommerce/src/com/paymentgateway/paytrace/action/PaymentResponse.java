package com.paymentgateway.paytrace.action;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.utility.CommonUtility;

public class PaymentResponse extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 764157980620128919L;
	private HttpServletRequest request;
	private String renderContent;
	public String transactionResponse() {
		request = ServletActionContext.getRequest();
		try {
			 String parmList = request.getParameter(LayoutLoader.getMessageProperties().get("EN").getProperty("creditcard.paramlist").trim());
				//String decodedParmList = URLDecoder.decode(parmList,"UTF-8");
				if(CommonUtility.validateString(parmList).length()>0)
				{
					String[] silentPostResponse = parmList.split("\\|");
					for(int i =0;i<silentPostResponse.length;i++) {
						String[] keyValuePair = silentPostResponse[i].split("~");
						String respKey = keyValuePair[0].trim();
						String respValue = keyValuePair[1].trim();
						if(respKey.equalsIgnoreCase("ORDERID")){
			        		if(CreditCardManagementAction.getPaymentResponse()==null) {
			        			CreditCardManagementAction.setPaymentResponse(new LinkedHashMap<String,String>());
			        		}
			        			LinkedHashMap<String, String> paymentRes = CreditCardManagementAction.getPaymentResponse();
			        			paymentRes.put(respValue, parmList);
			        			CreditCardManagementAction.setPaymentResponse(paymentRes);
			        		    break;
			        	}
					}
					
				}
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return null;
	}
	
	public String transactionApproved() {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		try {
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			
			int orderNum = 0;
			if(session.getAttribute("orderNumber")!=null) {
				orderNum =(int) session.getAttribute("orderNumber");
			}
			String orderNumber = CommonUtility.validateParseIntegerToString(orderNum);
			System.out.println("Order Number at approval: " + orderNumber);
			String paramList = null;
			if(CommonUtility.validateString(orderNumber).length() > 0) {
				paramList = CreditCardManagementAction.getPaymentResponse().get(orderNumber);
				LinkedHashMap<String, String> paymentObj = CreditCardManagementAction.getPaymentResponse();
				if(paymentObj!=null) {
					paymentObj.remove(orderNumber);
				}
				CreditCardManagementAction.setPaymentResponse(paymentObj);
			}
			
			System.out.println("paramList at redirect : " + paramList);
			
			contentObject.put("responseType", "PayTrace");
			contentObject.put("paramList",paramList);
 			renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject , null, null, null);
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return SUCCESS;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getRenderContent() {
		return renderContent;
	}

	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
}
