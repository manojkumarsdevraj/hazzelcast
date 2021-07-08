package com.paymentgateway.paytrace.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;

import com.paymentgateway.model.CreditCardManagementModel;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.sales.CreditCardModel;
import com.unilog.utility.CommonUtility;

public class CreditCardManagementAction {
	private static LinkedHashMap<String, String> paymentResponse= null;
	
	public static LinkedHashMap<String, String> getPaymentResponse() {
		return paymentResponse;
	}

	public static void setPaymentResponse(LinkedHashMap<String, String> paymentResponse) {
		CreditCardManagementAction.paymentResponse = paymentResponse;
	}

	public CreditCardModel creditCardTransactionSetup(CreditCardManagementModel creditCardInput){
	CreditCardModel creditCardOutput  = null;
	try {
			int orderId = 0;
			String userName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYTRACE_USERNAME"));
			String password = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYTRACE_PASSWORD"));
			String tranxtype = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYTRACE_TRANXTYPE"));
			String terms = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYTRACE_TERMS"));
			String validateUrl = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYTRACE_VALIDATE_URL"));
			
			orderId = CommonDBQuery.getSequenceId("ORDER_ID_SEQ");
			
			String uri = "UN~"+userName+"|PSWD~"+password+"|TRANXTYPE~"+tranxtype+"|TERMS~"+terms+"|AMOUNT~"+creditCardInput.getTotal()+"|ORDERID~"+orderId+"|";
			System.out.println("Paytrace URI needs to encode: "+uri);
			String paytraceSetupId = URLEncoder.encode(uri,"UTF-8");
			System.out.println("Paytrace encoded URI: "+paytraceSetupId);
			if(CommonUtility.validateString(validateUrl).length()>0){
				validateUrl+=paytraceSetupId;
			}
			HttpURLConnection paytraceConn;
			
			paytraceConn = (HttpURLConnection) new URL(validateUrl).openConnection();
			paytraceConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			paytraceConn.setDoOutput(true);
			paytraceConn.setRequestMethod("POST");
			
			OutputStream os = paytraceConn.getOutputStream();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(paytraceConn.getInputStream()));
			String line = null;
			StringBuffer responseData = new StringBuffer();
			while((line = in.readLine()) != null) {
				responseData.append(line);
			}
			System.out.println("Paytrace validate call response: " +responseData.toString());
			
			if(CommonUtility.validateString(responseData.toString()).length()>0)
			{
				String[] validatePayResponse = responseData.toString().split("\\|");
				String respMsg = null, respOrderId = null, respAuthKey = null;
				for(int i =0;i<validatePayResponse.length;i++) {
					String[] keyValuePair = validatePayResponse[i].split("~");
					String respKey = keyValuePair[0].trim();
					String respValue = keyValuePair[1].trim();
					if(respKey=="ERROR"){
		        		respMsg = respValue;
		        	}else if(respKey.equalsIgnoreCase("ORDERID")){
		        		respOrderId = respValue;
		        	}else if(respKey.equalsIgnoreCase("AUTHKEY")){
		        		respAuthKey = respValue;
		        	}
				}
				creditCardOutput = new CreditCardModel();
				creditCardOutput.setPcardId(CommonUtility.validateNumber(respOrderId));
				creditCardOutput.setCreditCardToken(CommonUtility.validateString(respAuthKey));
				creditCardOutput.setCreditCardStatus(CommonUtility.validateString(respMsg));
			}
	  }catch (Exception e) {
		  e.printStackTrace();
	  }
	return creditCardOutput;
  }
}
