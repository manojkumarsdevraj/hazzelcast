package com.unilog.ecommerce.user.validation.util;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.ecommerce.user.model.User;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.security.SecureData;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

public class RegistrationUtils {
	private static final Logger logger = Logger.getLogger(RegistrationUtils.class);
	public static String getShortName(String name) {
		String shortNameArr[] = {};// = name.trim().split("\\s+");
    	String shortName = "";
    	if(name != null) {
    		shortNameArr = name.trim().split("\\s+");
    	}
    	if(shortNameArr!=null && shortNameArr.length>0)
    	{
    		for(String sName:shortNameArr)
    		{
    			shortName = shortName + sName.substring(0, 1);
    		}
    	}
    	return shortName;
	}
	
	public static String validateCustomerName(String customerName, String firstName, String lastName) {
		if(customerName == null) {
			if(firstName!=null && firstName.trim().length()>0){
    			customerName = firstName.trim();
    		}
    		if(lastName!=null && lastName.trim().length()>0){
    			customerName = customerName+" "+lastName;
    		}
		}
		return customerName;
	}
	
	public static String getCountryCode(String country) {
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
		String eclipseCountryCode = country.toUpperCase();
		if(eclipseCountryCode.equalsIgnoreCase("USA")){
			eclipseCountryCode = "US";
		}
		if(eclipseCountryCode.equalsIgnoreCase("CAN")){
			eclipseCountryCode = "CA";
		}
		if(eclipseCountryCode.equalsIgnoreCase("MEX")){
			eclipseCountryCode = "MX";
		}
		try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("getCoutryCodeQuery");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, eclipseCountryCode);
            rs = pstmt.executeQuery();
            if(rs.next())
            {
            	if(rs.getString("COUNTRY_CODE")!=null && !rs.getString("COUNTRY_CODE").trim().equalsIgnoreCase(""))
            		eclipseCountryCode = rs.getString("COUNTRY_CODE");
            }
            else
            {
            	eclipseCountryCode = null;
            }
           
        
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBResultSet(rs);
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		return eclipseCountryCode;
	}
	
	public static int buildKey(String type, int id) {
		int count=-1;
		PreparedStatement pstmt=null;
	    Connection  conn = null;
	    boolean flag=false;
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareCall("{call USER_KEYWORD_PROC(?,?)}");
			pstmt.setString(1,type);
			pstmt.setInt(2,id);
			flag = pstmt.execute();
            if(flag){
            	System.out.println(type+" Keywords updated");
            }else{
            	System.out.println(type+" Keywords Failed");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 finally {	    
			 ConnectionManager.closeDBPreparedStatement(pstmt);
			 ConnectionManager.closeDBConnection(conn);
	      }
			return count;
	}
	
	public static int buildUserKeyWord(String type, int id){
		
		int count=-1;
		PreparedStatement pstmt=null;
	    String sql = "";
	    Connection  conn = null;
		try {
			conn = ConnectionManager.getDBConnection();
			if(type!=null && type.trim().equalsIgnoreCase("BUYINGCOMPANY")){
				sql = "UPDATE BUYING_COMPANY BC SET KEYWORDS = CUSTOMER_NAME || ' ; ' ||  SHORT_NAME || ' ; ' ||  ADDRESS1  || ' ; ' || CITY  || ' ; ' || STATE || ' ; ' ||  ZIP || ' ; ' ||  COUNTRY || ' ; ' ||  EMAIL || ' ; ' ||  URL || ' ; ' ||  ADDRESS2 || ' ; ' ||  ENTITY_ID || ' ; ' || (SELECT S.SUBSET_NAME FROM SUBSETS S WHERE S.SUBSET_ID = BC.SUBSET_ID) WHERE BUYING_COMPANY_ID=?";
			}
			if(type!=null && type.trim().equalsIgnoreCase("USER")){
				sql = "UPDATE CIMM_USERS SET KEYWORDS = USER_NAME || ' ; ' || FIRST_NAME  || ' ; ' || MIDDLE_NAME  || ' ; ' ||  LAST_NAME  || ' ; ' || EMAIL || ' ; ' ||  ADDRESS1 || ' ; ' || ADDRESS2 || ' ; ' || CITY || ' ; ' || STATE || ' ; ' || COUNTRY  || ' ; ' || ZIP || ' ; ' ||  OFFICE_PHONE  || ' ; ' || CELL_PHONE WHERE USER_ID=?";
			}
			if(type!=null && type.trim().equalsIgnoreCase("BUYINGCOMPANYALL")){
				sql = "UPDATE BUYING_COMPANY BC SET KEYWORDS = CUSTOMER_NAME || ' ; ' ||  SHORT_NAME || ' ; ' ||  ADDRESS1  || ' ; ' || CITY  || ' ; ' || STATE || ' ; ' ||  ZIP || ' ; ' ||  COUNTRY || ' ; ' ||  EMAIL || ' ; ' ||  URL || ' ; ' ||  ADDRESS2 || ' ; ' ||  ENTITY_ID || ' ; ' || (SELECT S.SUBSET_NAME FROM SUBSETS S WHERE S.SUBSET_ID = BC.SUBSET_ID) WHERE KEYWORDS IS NULL";
			}
            pstmt = conn.prepareStatement(sql);
            if(!CommonUtility.validateString(type).equalsIgnoreCase("BUYINGCOMPANYALL")){
	           pstmt.setInt(1, id);
			}
            count = pstmt.executeUpdate();
            pstmt.close();
            if(count>0){
            	System.out.println(type+" Keywords updated");
            }else{
            	System.out.println(type+" Keywords Failed");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 finally {	    
			 ConnectionManager.closeDBPreparedStatement(pstmt);
			 ConnectionManager.closeDBConnection(conn);
	      }
			return count;
	}
	
	public static void buildRequiredKeywords(int buyingCompanyId, int userId) {
		if(CommonDBQuery.getSystemParamtersList().get("CALL_FOR_KEYWORD_BUILDER")!=null && CommonDBQuery.getSystemParamtersList().get("CALL_FOR_KEYWORD_BUILDER").trim().equalsIgnoreCase("Y")){
		   if(CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD")!=null && CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD").trim().equalsIgnoreCase("Y")){
		      	UsersDAO.buildKeyWord("BUYINGCOMPANY", buyingCompanyId);
		   }else{
		       	UsersDAO.buildUserKeyWord("BUYINGCOMPANY", buyingCompanyId);
		   }
		   if(CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD")!=null && CommonDBQuery.getSystemParamtersList().get("PROCEDURE_CALL_FOR_KEYWORD").trim().equalsIgnoreCase("Y")){
			   UsersDAO.buildKeyWord("USER", userId);
		    }else{
		       	UsersDAO.buildUserKeyWord("USER", userId);
		   }
		 }
	}
	
	public static void notifyRegistration(String sourceFormId, User user, String type) {
		try {

			String subject;
			LinkedHashMap<String, String> notificationDetails = new SaveCustomFormDetails().getNotificationDetails(sourceFormId);
			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			if(notificationDetails!=null && notificationDetails.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetails.get("FROM_EMAIL");
			}
			VelocityContext context = new VelocityContext();
			subject = SendMailUtility.propertyLoader("registeration.form.subject." + sourceFormId, "");
			context.put("WelcomeText",CommonDBQuery.getSystemParamtersList().get("MAIL_WELCOME_MSG"));
			context.put("webAddress",CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			context.put("user", user);
			context.put("locale",LayoutLoader.getMessageProperties().get("EN"));
			StringWriter writer = new StringWriter();
	        if(notificationDetails!=null && notificationDetails.get("DESCRIPTION")!=null) {
	        	try {
					Velocity.evaluate(context, writer, "", notificationDetails.get("DESCRIPTION"));
				} catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException | IOException e) {
					e.printStackTrace();
				}
	        }
	        StringBuilder finalMessage= new StringBuilder();
	        finalMessage.append(writer.toString());
			sendMail(notificationDetails, subject, fromEmail, finalMessage.toString(), getEmailRecipients(user, type, notificationDetails));
		}catch (Exception e) {
			logger.error("Exception while sending registration notification", e);
		}
	}
	
	public static void notifyResgistrationToSuperUsers(User user,List<String> superUserMailIds) {
		try {
			String subject;
			LinkedHashMap<String, String> notificationDetails = new SaveCustomFormDetails().getNotificationDetails("RegistrationInfoToSuperUsers");
			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			if(notificationDetails!=null && notificationDetails.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetails.get("FROM_EMAIL");
			}
			VelocityContext context = new VelocityContext();
			subject = SendMailUtility.propertyLoader("registeration.form.subject.notify.superuser", "");
			context.put("WelcomeText",CommonDBQuery.getSystemParamtersList().get("MAIL_WELCOME_MSG"));
			context.put("webAddress",CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			context.put("user", user);
			context.put("locale",LayoutLoader.getMessageProperties().get("EN"));
			StringWriter writer = new StringWriter();
	        if(notificationDetails!=null && notificationDetails.get("DESCRIPTION")!=null) {
	        	try {
					Velocity.evaluate(context, writer, "", notificationDetails.get("DESCRIPTION"));
				} catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException | IOException e) {
					e.printStackTrace();
				}
	        }
	        StringBuilder finalMessage= new StringBuilder();
	        finalMessage.append(writer.toString());
	        Map<String, List<String>> allRecipients = extractRecipientsFromNotification(notificationDetails);
	        if(allRecipients != null) {
	        	List<String> toMailIds = allRecipients.get("TO");
	        	if(toMailIds != null)
	        		toMailIds.addAll(superUserMailIds);
	        	else
	        		allRecipients.put("TO", superUserMailIds);
	        }
			sendMail(notificationDetails, subject, fromEmail, finalMessage.toString(), allRecipients);
		}catch (Exception e) {
			logger.error("Exception while sending registration notification", e);
		}
	}
	
	public static Map<String, List<String>> extractRecipientsFromNotification(Map<String, String> notificationDetails) {
		List<String> toEmailIds = new ArrayList<>();
		List<String> ccEmailIds = new ArrayList<>();
		List<String> bccEmailIds = new ArrayList<>();
		Map<String, List<String>> allRecipients = new LinkedHashMap<>();
		if(notificationDetails.get("TO_EMAIL") != null) {
			toEmailIds.addAll(Arrays.asList(notificationDetails.get("TO_EMAIL").split(";")));
			allRecipients.put("TO", toEmailIds);
		}
		if(notificationDetails.get("CC_EMAIL") != null) {
			ccEmailIds.addAll(Arrays.asList(notificationDetails.get("CC_EMAIL").split(";")));
			allRecipients.put("CC", ccEmailIds);
		}
		if(notificationDetails.get("BCC_EMAIL") != null) {
			bccEmailIds.addAll(Arrays.asList(notificationDetails.get("BCC_EMAIL").split(";")));
			allRecipients.put("BCC", bccEmailIds);
		}
		return allRecipients;
	}
	
	private static Map<String, List<String>> getEmailRecipients(User user, String type, Map<String, String> notificationDetails){
		
		Map<String, List<String>> allRecipients = null;
		switch(type.toUpperCase()) {
		case "USER" : 
			break;
		case "CUSTOMER" : 
			break;
		case "ALL" :
		default:
			
			allRecipients = extractRecipientsFromNotification(notificationDetails);
			if(allRecipients != null) {
				List<String> toEmailIds = allRecipients.get("TO");
				
				if(toEmailIds != null)
					toEmailIds.add(user.getEmailId());
				else
					allRecipients.put("TO",Arrays.asList(user.getEmailId()));
			}
			break;
		}
		return allRecipients;
	}
	
	
	
	public static boolean sendMail(Map<String, String> notification,  String subject, String from, String body, Map<String, List<String>> recipients) {
		System.out.println("sending mail process :----");
        boolean result = false;
    	HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session1 = request.getSession();//spam email change
		String sessionUserId = (String) session1.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
       try {
        	SecureData validUserPass = new SecureData();
 			String emailRelayEncrpt = CommonDBQuery.getSystemParamtersList().get("EMAILRELAY");
 			String clientHost = CommonDBQuery.getSystemParamtersList().get("CLIENTHOST");
 			/*spam email changes start*/
 			int maxAllowedRequest = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAXIMUM_REQUEST_MAIL"));
 			long timeInterval = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TIME_INTERVAL_MAIL"));
 			long current_time = System.currentTimeMillis();
 			long last_mail_time = 0;
 			if(session1.getAttribute("last_sent_time")!=null){
 			last_mail_time =  (Long) session1.getAttribute("last_sent_time");
 			}
 			String reqestCount = (String) session1.getAttribute("reqestCount");
 			if(CommonUtility.validateString(reqestCount).equalsIgnoreCase("")){
 				session1.setAttribute("reqestCount", "0");
 			}
 			int count = CommonUtility.validateNumber(reqestCount);
 			/*spam email changes end*/
 			int emailRelayPort = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAILSMTPPORT"));
 			String emailRelay = validUserPass.validatePassword(emailRelayEncrpt);
 			Properties props = new Properties();

	         props.put("mail.smtp.host", emailRelay);
	         
	         if(emailRelayPort==0){
	        	 emailRelayPort = 25;
	         }
	         
	         props.put("mail.smtp.port", emailRelayPort);
	         
	         //props.put("mail.smtp.user", USER);
	         props.put("mail.smtp.from", from);//***Needed for NCE relay***
	
	         props.put("mail.smtp.auth", "false");
	         props.put("mail.smtp.starttls.enable", "false");
	         props.put("mail.smtp.debug", "true");
	         if(clientHost!=null && clientHost.length()>0){
	         props.put("mail.smtp.ehlo", true);
	         props.put("mail.smtp.localhost", clientHost);
	         }
	         props.put("mail.smtp.socketFactory.port", emailRelayPort);
	         //props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
	         props.put("mail.smtp.socketFactory.fallback", "false");
         
          	 Session session = Session.getInstance(props, null);
             session.setDebug(true);
             MimeMessage message = new MimeMessage(session);
          
 			String toMailList = StringUtils.join(recipients.get("TO"), ";");
 			String ccMailList = StringUtils.join(recipients.get("CC"), ";");
 			String bccMailList = StringUtils.join(recipients.get("BCC"), ";");
 			
           String techSupp =   CommonDBQuery.getSystemParamtersList().get("REPLYTO_EMAIL");
           if(techSupp!=null && !techSupp.trim().equalsIgnoreCase(""))
           {
        	   String techSuppArr[] = techSupp.split(";");
        	   if(techSuppArr.length > 0) {
        		   message.setReplyTo(Arrays.asList(techSuppArr).toArray(new Address[0]));
        	   }
           }
             message.saveChanges();
             if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")).length()>0){
            	 message.setFrom(new InternetAddress(from,CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")));
             }else{
            	 message.setFrom(new InternetAddress(from));
             }
             if(recipients.get("TO") != null && recipients.get("TO").size() > 0){
            	 message.addRecipients(Message.RecipientType.TO,convetToInternetAddress(recipients.get("TO")).toArray(new Address[0]));
             }else{
            	 message.addRecipients(Message.RecipientType.TO,convetToInternetAddress(recipients.get("BCC")).toArray(new Address[0]));
             }
             
             if(recipients.get("CC") != null && recipients.get("CC").size() > 0) {
            	 message.addRecipients(Message.RecipientType.CC,convetToInternetAddress(recipients.get("CC")).toArray(new Address[0]));
             }
             
             if(recipients.get("BCC") != null && recipients.get("BCC").size() > 0) {
            	 message.addRecipients(Message.RecipientType.BCC,convetToInternetAddress(recipients.get("BCC")).toArray(new Address[0]));
             }
            message.setSubject(subject);
            message.setSubject(subject);
          	message.setContent(body,"text/html");
          	message.setSentDate(new Date());
          	/*spam email changes start*/
          	System.out.println("current_time: "+current_time);
          	System.out.println("last_mail_time: "+last_mail_time);
          	System.out.println("count: "+count);
	         if(userId>1||subject.contains("Order Confirmation")||(current_time-last_mail_time > timeInterval)||(count<maxAllowedRequest)){
	        	Transport.send(message);
	          	count++;
	          	session1.setAttribute("reqestCount",""+count);
	          	long last_sent_time = System.currentTimeMillis();
	          	session1.setAttribute("last_sent_time", last_sent_time);
	          	result = true;
	          	if(userId<2 && count==maxAllowedRequest){
	          	session1.setAttribute("captcha_required", "Y");
	          	}
	         }else{
	        	session1.setAttribute("captcha_required", "Y");
            	result = false;
            	UsersDAO.saveSpamEmailsInDB(toMailList, ccMailList, bccMailList, sessionUserId, subject);
             }
	         /*spam email changes end*/
         }catch (Exception e) {
        	 e.printStackTrace();
         }
         return result;
    
	}
	
	private static List<Address> convetToInternetAddress(List<String> addresses){
		List<Address> internetAddressess = new ArrayList<>();
		for(String address : addresses) {
			try {
				internetAddressess.add(new InternetAddress(address));
			} catch (AddressException e) {
				e.printStackTrace();
			}
		}
		return internetAddressess;
	}
	
	public static List<User> getActiveSuperUsers(int customerId, Connection connection) {
		List<User> superUsers = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			String query = "SELECT C.EMAIL,C.USER_ID FROM CIMM_USERS C, CIMM_USER_ROLES CUR, BC_ADDRESS_BOOK BC WHERE BC.BC_ADDRESS_BOOK_ID = C.DEFAULT_BILLING_ADDRESS_ID AND C.USER_ID = CUR.USER_ID AND C.BUYING_COMPANY_ID = ? AND C.STATUS = ? AND CUR.ROLE_ID in(SELECT CR.ROLE_ID from CIMM_ROLES CR where CR.ROLE_NAME = ?)";
			statement = connection.prepareStatement(query);
			statement.setInt(1, customerId);
			statement.setString(2, "Y");
			statement.setString(3, "Ecomm Customer Super User");
			result = statement.executeQuery();
			while(result.next()) {
				User user = new User();
				user.setEmailId(result.getString("EMAIL"));
				user.setUserId(result.getInt("USER_ID"));
				superUsers.add(user);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.closeDBResultSet(result);
			ConnectionManager.closeDBPreparedStatement(statement);
		}
		return superUsers;
	}
	
	public static List<String> extractEmailIds(List<User> users){
		List<String> emailIds = new ArrayList<>();
		for(User user : users) {
			emailIds.add(user.getEmailId());
		}
		return emailIds;
	}
}
