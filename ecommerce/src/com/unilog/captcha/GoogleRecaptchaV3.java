package com.unilog.captcha;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GoogleRecaptchaV3 {

	private static final Logger LOGGER = Logger.getLogger(GoogleRecaptchaV3.class);

	private static final String RECAPTCHA_SERVICE_URL = CommonDBQuery.getSystemParamtersList().get("RECAPTCHA_URL");
	private static final String RECAPTCHA_SECRET_KEY = CommonDBQuery.getSystemParamtersList().get("RECAPTCHA_SECRET_KEY");
	 private  String renderContent;
	    public String getRenderContent() {
	        return renderContent;
	    }
	    public void setRenderContent(String renderContent) {
	        this.renderContent = renderContent;
	    }
	    

	public  String isValid(String token)  {
		Boolean success = false;
		Double captchaScore = 0.0;
		Double captchaSuccessScore = 0.5;
		int responseCode;
		String result=null;
		String content = null;
		HttpsURLConnection connection = null;
		BufferedReader br;
		
		String inputLine;
		StringBuffer response = new StringBuffer();

		  String recaptchaResponse =ServletActionContext.getRequest().getParameter("recaptchaResponse");
		// checking Recaptch response getting null or not
		if (CommonUtility.validateString(recaptchaResponse).length()<=0) {
			if(CommonUtility.validateString(token) != null){
				recaptchaResponse=token;
			}
			
			else {
		
			LOGGER.info("Responce getting NULL");
			return String.valueOf(success);
			}
		}
		
		

		try {
			URL serviceUrl = new URL(RECAPTCHA_SERVICE_URL);
			connection = (HttpsURLConnection) serviceUrl.openConnection();
			// client result added to post method
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// send post request to google recaptcha server
			content = "secret=" + RECAPTCHA_SECRET_KEY + "&response=" + recaptchaResponse;
			connection.setDoOutput(true);
			DataOutputStream data = new DataOutputStream(connection.getOutputStream());
			data.writeBytes(content);
			data.flush();
			data.close();

			responseCode = connection.getResponseCode();
			LOGGER.info("Post content: " + content);
			LOGGER.info("Response Code: " + responseCode);


			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			System.out.println("Response from -"+RECAPTCHA_SERVICE_URL+" -is: "+response!= null?response.toString():"");
			br.close();
		} catch (IOException exception) {
			LOGGER.error("Exception occured in isValid "+exception);
		}

		try {
			// getting Parse JSON-response
			JSONParser parser = new JSONParser();
			JSONObject json;
			json = (JSONObject) parser.parse(response.toString());
			success = (Boolean) json.get("success");
			captchaScore = (Double) json.get("score");

			LOGGER.info("success : " + success);
			LOGGER.info("score : " + captchaScore);
			success=(success && captchaScore>=captchaSuccessScore)?true:false;
			renderContent=(String.valueOf(success));
			 if(CommonUtility.validateString(token).length()>0)
	         {	             
	             renderContent=(String.valueOf(success));
	             result=renderContent;
	             // Allowing only customer ip address with captcha score till 0.1, HMI-2181 case reference
	    		 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_FOR_CUSTOMER_IP")).equalsIgnoreCase("Y")){
	    			 HttpServletRequest request =ServletActionContext.getRequest();
	    			 String ipaddress = request.getHeader("X-Forwarded-For");
	 				if (ipaddress == null) {
	 					ipaddress = request.getRemoteAddr();
	 				}
	 				System.out.println("User system IP address is := "+ ipaddress);
	    			 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_IP_ADDRESS")).length()>0 && ipaddress.equals(CommonDBQuery.getSystemParamtersList().get("ALLOW_IP_ADDRESS"))){
	    				 success=((Boolean) json.get("success") && captchaScore>=0.1)?true:false;
	    				  	 System.out.println("success:"+ success);
	    		             renderContent=(String.valueOf(success));
	    		             result=renderContent;
	    			 }
	    		 }
	         }else
	         {
	            	result="success";
	          }
		} catch (ParseException exception) {
			LOGGER.error("Exception occured while parsing reCaptcha JSON response "+exception);
		}

		   return result;
	}
}
