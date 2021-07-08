package com.erp.eclipse.inquiry;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.erp.eclipse.parser.LoginLogoutParser;
import com.unilog.database.CommonDBQuery;
import com.unilog.users.UserRegisterUtility;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
public class LoginSubmit {

	public static String ERPLOGIN(String userName, String password, String activeCustomerId) {
		userName = userName.trim();
		password = password.trim();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String userSessionId = "";
		UsersModel UsersModel = new UsersModel();
	    UsersModel.setLoginID(userName);
	    UsersModel.setLoginPassword(password);
	    String eclipseURL = CommonDBQuery.getSystemParamtersList().get("ERPCONNECTIONURL");
		String ERPTEMPLATEPATH = CommonDBQuery.getSystemParamtersList().get("ERPTEMPLATEPATH");
		System.out.println("Eclipse Path : " + ERPTEMPLATEPATH);
		HttpURLConnection eclipseConn = null;
	   	try 
		{

	   		VelocityEngine velocityTemplateEngine = new VelocityEngine();
	   		velocityTemplateEngine.setProperty("file.resource.loader.path", ERPTEMPLATEPATH);
	   		velocityTemplateEngine.init();

	        Template t = velocityTemplateEngine.getTemplate("LoginSubmit.xml");
	        /*  create a context and add data */
	        VelocityContext context = new VelocityContext();
	       	       
	        context.put("loginID", UsersModel.getLoginID());	
	        context.put("loginPassword", UsersModel.getLoginPassword());
	        if(CommonUtility.validateNumber(CommonUtility.validateString(activeCustomerId))>0){
	        	context.put("activeCustomer", "Yes");
	        	context.put("activeCustomerId", CommonUtility.validateString(activeCustomerId));
	        }else{
	        	context.put("activeCustomerId", "");
	        	context.put("activeCustomer", "No");
	        }
	        
	        
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
								
			BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
			String line = null;
			
			StringBuffer responseData = new StringBuffer();
		
			while((line = in.readLine()) != null) {
			
				responseData.append(line);
			}
			long lStartTime = new Date().getTime(); //start time	
			@SuppressWarnings("deprecation")
			LoginLogoutParser xmlFileSAX	=new LoginLogoutParser(new StringBufferInputStream(responseData.toString()));
			xmlFileSAX.getUserDetail();
    		UsersModel userDetailList = xmlFileSAX.getUserDetail();
	        System.out.println("session id:"+userDetailList.getSessionID());
	        session.setAttribute("connectionError", "No");
	        long lEndTime = new Date().getTime(); //end time
   		 
  	      long difference = lEndTime - lStartTime; //check difference
  	 
  	      System.out.println("Elapsed milliseconds: " + difference);
  	      	userSessionId =  userDetailList.getSessionID();
  	        session.setAttribute("contactID", userDetailList.getContactId());
	        System.out.println("User Session Id at  Login : " + userSessionId);
	        System.out.println("custPartEntity id:"+userDetailList.getEntityName());
	        String custEntity = userDetailList.getEntityName();
	        if(CommonDBQuery.getSystemParamtersList().get("SEARCHBY_USER_ENTITY")!=null && CommonDBQuery.getSystemParamtersList().get("SEARCHBY_USER_ENTITY").trim().equalsIgnoreCase("Y"))
	    	{
	        	if(custEntity!=null && !custEntity.trim().equalsIgnoreCase(""))
		        {
		        	session.setAttribute("customerBuyingCompanyId", UserRegisterUtility.checkEntityId(CommonUtility.validateString(custEntity)));
		        }
		        else
		        {
		        	session.setAttribute("customerBuyingCompanyId",0);
		        }
	    	}
	        
	       
	        
	        session.setAttribute("customerEntity", custEntity);
	        os.close();
	        in.close();
	        
	    } catch (ResourceNotFoundException ex) {
	        ex.printStackTrace();
	        session.setAttribute("connectionError", "Yes");
	       
	    } catch (ParseErrorException ex) {
	          ex.printStackTrace();
	       
	    } catch (Exception ex) {
	          ex.printStackTrace();
	          session.setAttribute("connectionError", "Yes");
	    }
	    finally
	    {
	    	if(eclipseConn!=null)
		    	eclipseConn.disconnect();
	    }
	    return userSessionId;
	}
	
	
	
	public static String ERPLOGINWithoutSession(String userName, String password)
	{

		
		
		
		String userSessionId = "";
	    UsersModel UsersModel = new UsersModel();
	    UsersModel.setLoginID(userName);
	    UsersModel.setLoginPassword(password);
	    String eclipseURL = CommonDBQuery.getSystemParamtersList().get("ERPCONNECTIONURL");
		String ERPTEMPLATEPATH = CommonDBQuery.getSystemParamtersList().get("ERPTEMPLATEPATH");
		System.out.println("Eclipse Path : " + ERPTEMPLATEPATH);
		HttpURLConnection eclipseConn = null;
	   	try 
		{

	   		VelocityEngine velocityTemplateEngine = new VelocityEngine();
	   		velocityTemplateEngine.setProperty("file.resource.loader.path", ERPTEMPLATEPATH);
	   		velocityTemplateEngine.init();

	        Template t = velocityTemplateEngine.getTemplate("LoginSubmit.xml");
	        /*  create a context and add data */
	        VelocityContext context = new VelocityContext();
	       	       
	        context.put("loginID", UsersModel.getLoginID());
	        context.put("loginPassword", UsersModel.getLoginPassword());
	        
	        /* now render the template into a StringWriter */
	        StringWriter writer = new StringWriter();
	        t.merge(context, writer);
	        /* show the World */
	        StringBuffer finalMessage= new StringBuffer();
	        finalMessage.append(writer.toString());
	       
	        
	       eclipseConn = (HttpURLConnection) new URL(eclipseURL).openConnection();
			
            eclipseConn.setDoOutput(true);
			OutputStream os = eclipseConn.getOutputStream();
			os.write(finalMessage.toString().getBytes());
								
			BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
			String line = null;
			
			StringBuffer responseData = new StringBuffer();
		
			while((line = in.readLine()) != null) {
			
				responseData.append(line);
			}
			long lStartTime = new Date().getTime(); //start time	
			@SuppressWarnings("deprecation")
			LoginLogoutParser xmlFileSAX	=new LoginLogoutParser(new StringBufferInputStream(responseData.toString()));
			xmlFileSAX.getUserDetail();
    		UsersModel userDetailList = xmlFileSAX.getUserDetail();
	        System.out.println("session id:"+userDetailList.getSessionID());
	       
	        long lEndTime = new Date().getTime(); //end time
   		 
  	      long difference = lEndTime - lStartTime; //check difference
  	 
  	      System.out.println("Elapsed milliseconds: " + difference);
  	    userSessionId =  userDetailList.getSessionID();
	        System.out.println("User Session Id at  Login : " + userSessionId);
	        os.close();
	        in.close();
	    } catch (ResourceNotFoundException ex) {
	        ex.printStackTrace();
	        
	       
	    } catch (ParseErrorException ex) {
	          ex.printStackTrace();
	       
	    } catch (Exception ex) {
	          ex.printStackTrace();
	          
	    } finally
	    {
	    	if(eclipseConn!=null)
		    	eclipseConn.disconnect();
	    }
return userSessionId;
	
	}
	
	public static UsersModel ERPLOGINWithoutSessionPhone(String userName, String password)
	{

		
		UsersModel userDetailList =null;
		
		String userSessionId = "";
	    UsersModel UsersModel = new UsersModel();
	    UsersModel.setLoginID(userName);
	    UsersModel.setLoginPassword(password);
	    String eclipseURL = CommonDBQuery.getSystemParamtersList().get("ERPCONNECTIONURL");
		String ERPTEMPLATEPATH = CommonDBQuery.getSystemParamtersList().get("ERPTEMPLATEPATH");
		System.out.println("Eclipse Path : " + ERPTEMPLATEPATH);
		HttpURLConnection eclipseConn = null;
	   	try 
		{

	   		VelocityEngine velocityTemplateEngine = new VelocityEngine();
	   		velocityTemplateEngine.setProperty("file.resource.loader.path", ERPTEMPLATEPATH);
	   		velocityTemplateEngine.init();
	        Template t = velocityTemplateEngine.getTemplate("LoginSubmit.xml");
	        /*  create a context and add data */
	        VelocityContext context = new VelocityContext();
	       	       
	        context.put("loginID", UsersModel.getLoginID());
	        context.put("loginPassword", UsersModel.getLoginPassword());
	        
	        /* now render the template into a StringWriter */
	        StringWriter writer = new StringWriter();
	        t.merge(context, writer);
	        /* show the World */
	        StringBuffer finalMessage= new StringBuffer();
	        finalMessage.append(writer.toString());
	       
	        
	       eclipseConn = (HttpURLConnection) new URL(eclipseURL).openConnection();
			
            eclipseConn.setDoOutput(true);
			OutputStream os = eclipseConn.getOutputStream();
			os.write(finalMessage.toString().getBytes());
								
			BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
			String line = null;
			
			StringBuffer responseData = new StringBuffer();
			
			while((line = in.readLine()) != null) {
			
				responseData.append(line);
			}
			long lStartTime = new Date().getTime(); //start time	
			@SuppressWarnings("deprecation")
			LoginLogoutParser xmlFileSAX	=new LoginLogoutParser(new StringBufferInputStream(responseData.toString()));
			xmlFileSAX.getUserDetail();
    		userDetailList = xmlFileSAX.getUserDetail();
	        System.out.println("session id:"+userDetailList.getSessionID());
	        userDetailList.setConnectionError("no");
	        long lEndTime = new Date().getTime(); //end time
   		 
  	      long difference = lEndTime - lStartTime; //check difference
  	 
  	      System.out.println("Elapsed milliseconds: " + difference);
  	    userSessionId =  userDetailList.getSessionID();
	        System.out.println("User Session Id at  Login : " + userSessionId);
	        os.close();
	        in.close();
	    } catch (ResourceNotFoundException ex) {
	        ex.printStackTrace();
	        if(userDetailList!=null)
	        userDetailList.setConnectionError("no");
	       
	    } catch (ParseErrorException ex) {
	          ex.printStackTrace();
	          if(userDetailList!=null)
	          userDetailList.setConnectionError("no");
	    } catch (Exception ex) {
	          ex.printStackTrace();
	          if(userDetailList!=null)
	          userDetailList.setConnectionError("no");
	    } finally
	    {
	    	if(eclipseConn!=null)
		    	eclipseConn.disconnect();
	    }
return userDetailList;
	
	}
}
