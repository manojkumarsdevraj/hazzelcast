package com.erp.eclipse.inquiry;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.unilog.database.CommonDBQuery;
import com.unilog.sales.CreditCardModel;
import com.unilog.security.SecureData;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;


public class ContactInquiry {
	public static UsersModel ERPContactEnqiry(String sessionId, String contactId,String userName) {
		
		   
		UsersModel UsersModel = new UsersModel();
		String byUser = "No";
		String bySession = "No";
	   
		String ERPLOGIN = CommonDBQuery.getSystemParamtersList().get("ECLIPSELOGIN");
		String eclipseURL = CommonDBQuery.getSystemParamtersList().get("ERPCONNECTIONURL");
		String ERPTEMPLATEPATH = CommonDBQuery.getSystemParamtersList().get("ERPTEMPLATEPATH");
		
		if(ERPLOGIN!=null && ERPLOGIN.trim().equalsIgnoreCase("ByUser"))
		{
		    SecureData validUserPass = new SecureData();
		    HashMap<String,String> userDetails=UsersDAO.getUserPasswordAndUserId(userName,"Y");
		    String password = userDetails.get("password");
		    UsersModel.setLoginID(userName);
		    UsersModel.setLoginPassword(validUserPass.validatePassword(password));
		    byUser = "Yes";
		}
		else
		{
		    UsersModel.setSessionId(sessionId);
		    bySession = "Yes";
		}
		    
		   UsersModel wsContactInquiry = new UsersModel();
		   wsContactInquiry.setContactId(contactId);
		   UsersModel userDetailList = new UsersModel();
		   ArrayList<CreditCardModel> creditDetailList = new ArrayList<CreditCardModel>();
		   HttpURLConnection eclipseConn = null;
	 
	   	try 
		{

	   		VelocityEngine velocityTemplateEngine = new VelocityEngine();
	   		velocityTemplateEngine.setProperty("file.resource.loader.path", ERPTEMPLATEPATH);
	   		velocityTemplateEngine.init();

	        Template t = velocityTemplateEngine.getTemplate("ContactInquiry.xml");
	        /*  create a context and add data */
	        VelocityContext context = new VelocityContext();
	       
	        context.put("loginId", UsersModel.getLoginID());
            context.put("loginPassword", UsersModel.getLoginPassword());
            context.put("sessionId", UsersModel.getSessionId());
            context.put("byUser", byUser);
            context.put("bySession", bySession);
	        context.put("contactID", wsContactInquiry.getContactId());
	        /*context.put("loginID", UsersModal.getLoginID());
	        context.put("loginPassword", UsersModal.getLoginPassword());*/
	    
	        /* now render the template into a StringWriter */
	        StringWriter writer = new StringWriter();
	        t.merge(context, writer);
	        /* show the World */
	        StringBuffer finalMessage= new StringBuffer();
	        finalMessage.append(writer.toString());
	        System.out.println(finalMessage.toString());
	        eclipseConn = (HttpURLConnection) new URL(eclipseURL).openConnection();
			
            eclipseConn.setDoOutput(true);
			OutputStream os = eclipseConn.getOutputStream();
			os.write(finalMessage.toString().getBytes());
			//System.out.println(xmlRequest.toString());
			//InputStream  xmlResponse  = eclipseConn.getInputStream();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
			String line = null;
						
			StringBuffer responseData = new StringBuffer();
			
			while((line = in.readLine()) != null) {
				responseData.append(line);
				//System.out.println("xmlResponse:---"+xmlResponse.toString());
			}
			System.out.println("responseData.toString() : " + responseData.toString());
			
			ReadXMLFileSAX xmlFileSAX	=new ReadXMLFileSAX(new StringBufferInputStream(responseData.toString()));
			
			userDetailList = xmlFileSAX.getUserDetail();
			creditDetailList = userDetailList.getCreditCardDetailList();
			for(CreditCardModel temp:creditDetailList)
			{
				System.out.println(temp.getCreditCardType());
				System.out.println(temp.getCreditCardNumber());
			}
			if(userDetailList!=null)
			{
			System.out.println("sessionid: "+ userDetailList.getSessionID());
			System.out.println("Entity Id : " + userDetailList.getEntityId());
			System.out.println("First Name : " + userDetailList.getFirstName());
			System.out.println("Last Name : " + userDetailList.getLastName());
			System.out.println("Address : " + userDetailList.getAddress1());
			System.out.println("Address2 : " + userDetailList.getAddress2());
			System.out.println("city : " + userDetailList.getCity());
			System.out.println("State : " + userDetailList.getState());
			System.out.println("country : " + userDetailList.getCountry());
			System.out.println("Compare : " + userDetailList.getErpLoginId());
			}
			os.close();
			in.close();
	    } catch (ResourceNotFoundException ex) {
	        ex.printStackTrace();
	       
	    } catch (ParseErrorException ex) {
	          ex.printStackTrace();
	       
	    } catch (Exception ex) {
	          ex.printStackTrace();
	        
	    }
	    finally
	    {
	    	if(eclipseConn!=null)
	    	eclipseConn.disconnect();
	    }
	    return userDetailList;
	}




}
