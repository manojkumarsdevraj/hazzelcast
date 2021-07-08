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

import com.erp.eclipse.parser.EntityParser;
import com.unilog.database.CommonDBQuery;
import com.unilog.security.SecureData;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
@SuppressWarnings("deprecation")
public class EntityInquiry {
	
	public static UsersModel eclipseEntityEnquiry(String sessionId,String entityId,String userName) {
		 UsersModel UsersModel = new UsersModel();
		 String byUser = "No";
			String bySession = "No";
		   
			String ERPLOGIN = CommonDBQuery.getSystemParamtersList().get("ECLIPSELOGIN");
			String eclipseURL = CommonDBQuery.getSystemParamtersList().get("ERPCONNECTIONURL");
			String ERPTEMPLATEPATH = CommonDBQuery.getSystemParamtersList().get("ERPTEMPLATEPATH");
			
			if(ERPLOGIN!=null && ERPLOGIN.trim().equalsIgnoreCase("ByUser"))
			{
		    SecureData validUserPass = new SecureData();
		    HashMap<String,String> userDetails = UsersDAO.getUserPasswordAndUserId(userName,"Y");
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
		 UsersModel userDetailList = null;
		 HttpURLConnection eclipseConn = null;
	   	try 
		{
	   		VelocityEngine velocityTemplateEngine = new VelocityEngine();
	   		velocityTemplateEngine.setProperty("file.resource.loader.path", ERPTEMPLATEPATH);
	   		velocityTemplateEngine.init();

            Template t = velocityTemplateEngine.getTemplate("EntityInquiry.xml");
            /*  create a context and add data */
            VelocityContext context = new VelocityContext();
           
            context.put("loginId", UsersModel.getLoginID());
            context.put("loginPassword", UsersModel.getLoginPassword());
            context.put("sessionId", sessionId);
            context.put("byUser", byUser);
            context.put("bySession", bySession);
            context.put("entityId", entityId);
     
            
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
			System.out.println("responseData.toString() : " + responseData.toString());
			EntityParser xmlFileSAX	=new EntityParser(new StringBufferInputStream(responseData.toString()));
			String statusResult = xmlFileSAX.value;
			ArrayList<ShipVia> ship = xmlFileSAX.getShipViaLists();
			if(ship!=null){
				for(ShipVia t4:ship){ 
					System.out.println(t4.getShipViaID());
				}
			}
			ArrayList<UsersModel> orderStatus = xmlFileSAX.getOrderStatusLists();
			if(orderStatus!=null){
				for(UsersModel t4:orderStatus){ 
					System.out.println("code --> "+t4.getOrderStatusCode()+" order Status --- >"+t4.getOrderStatus());
				}
			}
			if(statusResult!=null && statusResult.trim().contains("successfully"))
			{
				userDetailList = new UsersModel();
				userDetailList = xmlFileSAX.getUserDetail();
				userDetailList.setShipViaList(ship);
				userDetailList.setOrderStatusList(orderStatus);
			}
			ArrayList<String> customerType = xmlFileSAX.getCustomerType();
			if(customerType!=null && customerType.size() > 2){
				if(CommonUtility.validateString(customerType.get(2)).length()>0 && userDetailList!=null){
					userDetailList.setCustomerType(customerType.get(2));
				}
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
        	System.out.println("Clossing url connection");
        	if(eclipseConn!=null)
    	    	eclipseConn.disconnect();
        }
return userDetailList;
	}
    

}
