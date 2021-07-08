package com.unilog.defaults;

import static com.unilog.defaults.Global.context;
import static com.unilog.defaults.Global.find;
import static com.unilog.defaults.Global.staticPage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.itextpdf.text.pdf.codec.Base64;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.DisplayTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;

import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.api.bronto.BrontoModel;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.ecomm.model.Discount;
import com.unilog.misc.EventDetails;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.products.StaticPageData;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.CreditCardModel;
import com.unilog.sales.SalesDAO;
import com.unilog.sales.SalesModel;
import com.unilog.security.SecureData;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.users.AddressModel;
import com.unilog.users.CreditApplicationModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

public class SendMailUtility {
	
	public static SaveCustomFormDetails getNotificationDetail = new SaveCustomFormDetails();
	
	public static String sendErrorMail(String ErrorCode,Exception e){
		return "ErrorMail";
	}

	static class HTMLDataSource implements DataSource {
        private String html;

        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        // Return html string in an InputStream.
        // A new stream must be returned each time.
        public InputStream getInputStream() throws IOException {
            if (html == null) throw new IOException("Null HTML");
            return new ByteArrayInputStream(html.getBytes());
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        public String getContentType() {
            return "text/html";
        }

        public String getName() {
            return "JAF text/html dataSource to send e-mail only";
        }
    }

	private class SMTPAuthenticator extends javax.mail.Authenticator {

		public PasswordAuthentication getPasswordAuthentication() {
			System.out.println("-----Authenticating-----");
			SecureData validUserPass = new SecureData();
			String fromPassword = CommonDBQuery.getSystemParamtersList().get("FROMMAILPASSWORD");
			String username = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			String password = validUserPass.validatePassword(fromPassword);
			/**
			 *Below code Written is for Tyndale *Reference- Chetan Sandesh
			 */
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_FROMUSERNAME")).length()>0) {
				username = CommonDBQuery.getSystemParamtersList().get("SMTP_FROMUSERNAME");
			}
			return new PasswordAuthentication(username, password);
		}
	}
	
	public boolean sendNotification(LinkedHashMap<String, String> notification, String subject,String from, String body){
		System.out.println("sending mail process :----");
         boolean result = false;
         String subject_data = subject;
        if(CommonDBQuery.getSystemParamtersList().get("MAILSELECT")!=null && CommonDBQuery.getSystemParamtersList().get("MAILSELECT").trim().equalsIgnoreCase("MODIFIED"))
        {
        	System.out.println("Inside New Mail Code....");
        	result = sendSalesOrderMessageNew(notification, subject_data, from, body);
        }
        else
        {
        	try {
        		System.out.println("Inside Old Mail Code....");
        		HttpServletRequest request = ServletActionContext.getRequest();
        		HttpSession session1 = request.getSession();
        		int buyingCompanyId = CommonUtility.validateNumber((String)session1.getAttribute("buyingCompanyId"));
        		LinkedHashMap<String, String> customerCustomFieldValue  = UsersDAO.getAllCustomerCustomFieldValue(buyingCompanyId);
        		if(customerCustomFieldValue!=null && customerCustomFieldValue.size()>0 && customerCustomFieldValue.containsKey("CC_ON_ALL_MAILS")){
        			String ccOnAllMails = customerCustomFieldValue.get("CC_ON_ALL_MAILS");
        			if(CommonUtility.validateString(ccOnAllMails).length()>0){
        				if(notification.get("CC_EMAIL")!=null &&notification.get("CC_EMAIL").length()>0) {
        				notification.put("CC_EMAIL",notification.get("CC_EMAIL")+";"+ccOnAllMails);
        				}
        				else {
        					notification.put("CC_EMAIL",ccOnAllMails);
        				}
        			}	
        		}
           	 	
        		//SecureData validUserPass = new SecureData();
      			//String emailRelayEncrpt = CommonDBQuery.getSystemParamtersList().get("EMAILRELAY");
      			
      			 String mailSmtp = CommonDBQuery.getSystemParamtersList().get("MAILSMTP");
      			int emailRelayPort = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAILSMTPPORT"));
                 Properties props = new Properties();
                 props.put("mail.smtp.host", mailSmtp);
              	props.put("mail.smtp.port",emailRelayPort);
              	props.put("mail.smtp.ehlo", true);
            	props.put("mail.smtp.debug", "true");
            	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")).length()>0) {
                    props.put("mail.smtp.ssl.trust", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")));
            	}
              	/**
    			 *Below code Written is for Tyndale *Reference- Chetan Sandesh
    			 */
              	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_FROMUSERNAME")).length()>0) {
              	props.put("mail.smtp.auth", "true");
              	props.put("mail.transport.protocol", "smtp");
              	props.put("mail.smtp.starttls.enable", "true");
              	}
                 Authenticator auth = new SMTPAuthenticator();
                 Session session = Session.getInstance(props, auth);
                 session.setDebug(true);
                MimeMessage message = new MimeMessage(session);
                Address[] toAddress = null;
    			Address[] ccAddress = null;
    			Address[] bccAddress = null;
    			List<Address> toAddressList = new ArrayList<Address>();
    			int loop = 0;
    			if(notification.get("TO_EMAIL")!=null && !notification.get("TO_EMAIL").trim().equalsIgnoreCase(""))
    			{
     				String splitToAddress[] = notification.get("TO_EMAIL").split(";");
     				if(splitToAddress!=null && splitToAddress.length>0)
     				{
     					toAddress = new Address[splitToAddress.length];
     					for(String toAddr:splitToAddress)
     					{
     						toAddressList.add(new InternetAddress(toAddr.trim()));
     					}
     				}
     				
     			}
    			 if(notification.get("toEmailAddressFromPage") != null && notification.get("toEmailAddressFromPage").toString().length() > 0){
  	                String toEmailAddressFromPage = notification.get("toEmailAddressFromPage").toString();
  	                String []toEmailAddressFromPageArr = toEmailAddressFromPage.split(";");
  	                toAddress = new Address[toEmailAddressFromPageArr.length];
  	                for(String toAddr:toEmailAddressFromPageArr)
  	                {
  	                	toAddressList.add(new InternetAddress(toAddr.trim()));
  	                }
    			 }
    			 
    			 if(toAddressList != null && toAddressList.size() > 0){
     	             toAddress = toAddressList.toArray(new Address[0]);
     	         }
     			
    			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SEND_EMAIL_TO_TEST_USERS")).length()>0){
     				String sendtoTestUsers[]=CommonDBQuery.getSystemParamtersList().get("SEND_EMAIL_TO_TEST_USERS").split(";");
     				toAddress=null;
     				if(sendtoTestUsers!=null && sendtoTestUsers.length>0)
     				{  
     					toAddress = new Address[sendtoTestUsers.length];
     					for(String toAddr:sendtoTestUsers)
    					{
    						toAddress[loop] = new InternetAddress(toAddr.trim());
    						loop++;
    					}
    				}
     				}
     			
    			
    			if(notification.get("CC_EMAIL")!=null && !notification.get("CC_EMAIL").trim().equalsIgnoreCase(""))
    			{
    				loop = 0;
    				String splitToAddress[] = notification.get("CC_EMAIL").split(";");
    				if(splitToAddress!=null && splitToAddress.length>0)
    				{
    					ccAddress = new Address[splitToAddress.length];
    					for(String toAddr:splitToAddress)
    					{
    						ccAddress[loop] = new InternetAddress(toAddr.trim());
    						loop++;
    					}
    				}
    				
    			}
    			if(notification.get("BCC_EMAIL")!=null && !notification.get("BCC_EMAIL").trim().equalsIgnoreCase(""))
    			{
    				loop = 0;
    				String splitToAddress[] = notification.get("BCC_EMAIL").split(";");
    				if(splitToAddress!=null && splitToAddress.length>0)
    				{
    					bccAddress = new Address[splitToAddress.length];
    					for(String toAddr:splitToAddress)
    					{
    						bccAddress[loop] = new InternetAddress(toAddr.trim());
    						loop++;
    					}
    				}
    				
    			}
    			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")).length()>0){
    				message.setFrom(new InternetAddress(from,CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")));
				}else{
					message.setFrom(new InternetAddress(from));
				}
                
                System.out.println("TO EMAIL address:----"+toAddress);
                message.addRecipients(Message.RecipientType.TO,toAddress );
                if(ccAddress!=null && ccAddress.length>0)
               	 message.addRecipients(Message.RecipientType.CC,ccAddress );
                System.out.println("TO ccAddress address:----"+ccAddress);

                if(bccAddress!=null && bccAddress.length>0)
               	 message.addRecipients(Message.RecipientType.BCC,bccAddress );
                
                message.setSubject(subject_data);
               
                Multipart mp = new MimeMultipart();

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(new HTMLDataSource(body)));
                mp.addBodyPart(messageBodyPart);

                // Put parts in message
                message.setContent(mp);

                // Send the message
                Transport.send(message);
                result = true;
            } catch (Exception e) {
           	 e.printStackTrace();
              
            }
        }
         
        System.out.println("sending mail process  completed:----");
        return result;
    }
	
	public static String propertyLoader(String propertyKey,String args)
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Properties locale = new Properties();
		Object[] arguments = null;
		try{
		if(session.getAttribute("localeCode")!=null ){
			locale = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase());
	    	
	    }else{
	    	locale = LayoutLoader.getMessageProperties().get("EN");
	    }
		String argList[] = CommonUtility.validateString(args).trim().split(",");
		arguments = argList;
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return MessageFormat.format(CommonUtility.validateString(locale.getProperty(propertyKey)), arguments);
	}
	
	public static boolean welcomeMail(String userId){
		boolean flag = false;
		try{
		    	UsersDAO.updateFirstLogin(CommonUtility.validateNumber(userId));
		    	UsersModel approverMail = UsersDAO.getUserEmail(CommonUtility.validateNumber(userId));
		    	
		    	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("WelcomeMail");
		    	
		    	if(notificationDetail!=null && CommonUtility.validateString(notificationDetail.get("DESCRIPTION")).length()>0){
			    	
		    		String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			    	
			    	SendMailUtility sendMailUtility = new SendMailUtility();
			    	if(CommonDBQuery.getSystemParamtersList().get("WELCOME_MAIL_FOR_CLIENT")!=null && CommonDBQuery.getSystemParamtersList().get("WELCOME_MAIL_FOR_CLIENT").trim().equalsIgnoreCase("Y") )
			    	{
				    		LinkedHashMap<String, String> notificationDetailUser = getNotificationDetail.getNotificationDetails("WelcomeMailToClient");
				    		
				    		VelocityContext context = new VelocityContext();
							context.put("firstName", approverMail.getFirstName());
							context.put("lastName", approverMail.getLastName());
							context.put("companyName",approverMail.getCustomerName());
							context.put("userName", approverMail.getUserName());
							context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
				            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
				            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
				            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
				            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
				            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
				            }
		
				            StringWriter writer = new StringWriter();
					        Velocity.evaluate(context, writer, "", notificationDetailUser.get("DESCRIPTION"));
				            StringBuilder finalMessage= new StringBuilder();
					        finalMessage.append(writer.toString());
				    		
				    		
				    		flag = sendMailUtility.sendNotification(notificationDetailUser, propertyLoader("sentmailconfig.welcomemail.client.subject",""), fromEmail, finalMessage.toString());
				    		
				    		
				    	}
				    	
						if(notificationDetail.get("FROM_EMAIL")!=null){
							fromEmail = notificationDetail.get("FROM_EMAIL");
						}
						
						String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL"); 
						if(notificationDetail.get("BCC_EMAIL")!=null){
							customerEmail = notificationDetail.get("BCC_EMAIL");
							notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
						}
						
				    	VelocityContext context = new VelocityContext();
						context.put("firstName", approverMail.getFirstName());
						context.put("lastName", approverMail.getLastName());
						context.put("companyName",approverMail.getCustomerName());
						context.put("userName", approverMail.getUserName());
						context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
			            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
			            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
			            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
			            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
			            }
		
			            StringWriter writer = new StringWriter();
				        Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
			            StringBuilder finalMessage= new StringBuilder();
				        finalMessage.append(writer.toString());
				        flag = sendMailUtility.sendSalesOrderMessage(approverMail.getEmailAddress(),propertyLoader("sentmailconfig.welcomemail.user.subject", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")),fromEmail,finalMessage.toString(),customerEmail,notificationDetail.get("CC_EMAIL"));
		    	}
		
			}catch(Exception e){
		    	e.printStackTrace();
		    }
		    return flag;
	}
	
	public boolean sendRegistrationMail(UsersModel userDetailsAddresss, String whomTo,String SuperUserEmail,String formtype){
		boolean flag = false;
		ResultSet rs = null;
	    Connection  conn = null;
	    HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();

	    String emailType = "ForgotPassword";
	    String subject = "";
	    String to = "";
	    try{
        	StringBuilder userDetails = new StringBuilder();
        	if(userDetailsAddresss!=null)
            {
        	userDetails.append(userDetailsAddresss.getFirstName());
        	if(userDetailsAddresss.getLastName()!=null)
            	userDetails.append(" "+userDetailsAddresss.getLastName());
            userDetails.append("<br/>"+userDetailsAddresss.getAddress1()+",<br/>");
            if(userDetailsAddresss.getAddress2()!=null && userDetailsAddresss.getAddress2().trim().length()>0){
            	userDetails.append(userDetailsAddresss.getAddress2()+",<br/>");
            }
            userDetails.append(userDetailsAddresss.getCity()+",<br/>");
            userDetails.append(userDetailsAddresss.getState()+" "+userDetailsAddresss.getZipCode()+"<br/>");
            userDetails.append(userDetailsAddresss.getCountry()+"<br/>");
            if(userDetailsAddresss.getPhoneNo()!=null)
            	userDetails.append("PH: "+userDetailsAddresss.getPhoneNo());
            }

        	LinkedHashMap<String, String> notificationDetail = null;
        	if (formtype != null && formtype.trim().equalsIgnoreCase("1A")){
        		notificationDetail = getNotificationDetail.getNotificationDetails("ExistingUserRegistrationOne");
        	}else if (formtype != null && formtype.trim().equalsIgnoreCase("1B")){
        		notificationDetail = getNotificationDetail.getNotificationDetails("ExistingUserRegistrationTwo");
        	}else if(formtype != null && formtype.trim().equalsIgnoreCase("2A")){
        		notificationDetail = getNotificationDetail.getNotificationDetails("NewUserRegistrationOne");
        	}else if(formtype != null && formtype.trim().equalsIgnoreCase("2B")){
        		notificationDetail = getNotificationDetail.getNotificationDetails("NewUserRegistrationTwo");
        	}else if(formtype != null && formtype.trim().equalsIgnoreCase("CommertialCustomerCreditApplicationRequest")){
        		notificationDetail = getNotificationDetail.getNotificationDetails("CommertialCustomerCreditApplicationRequest");
        	}else if(formtype != null && formtype.trim().equalsIgnoreCase("addNewShippingAddress")){
        		notificationDetail = getNotificationDetail.getNotificationDetails("addNewShippingAddress");
        		emailType = "Add New Shipping Address";
        	}
        	
        	if(notificationDetail!=null && notificationDetail.size()>0){
        	
        	String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			if(notificationDetail!=null && notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			if(notificationDetail!=null)
			notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
			
			VelocityContext context = new VelocityContext();

        	context.put("WelcomeText", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MAIL_WELCOME_MSG")));
			context.put("webAddress", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS")));
			if(userDetailsAddresss!=null){
				context.put("firstName", CommonUtility.validateString(userDetailsAddresss.getFirstName()));
	            context.put("lastName", CommonUtility.validateString(userDetailsAddresss.getLastName()));
	            context.put("email", CommonUtility.validateString(userDetailsAddresss.getEmailAddress()));
	            context.put("password", CommonUtility.validateString(userDetailsAddresss.getPassword()));
	            context.put("accountType", CommonUtility.validateString(userDetailsAddresss.getAccountName()));
	            context.put("newsLetter", CommonUtility.validateString(userDetailsAddresss.getNewsLetterSub()));
	            context.put("address1", CommonUtility.validateString(userDetailsAddresss.getAddress1()));
	            context.put("address2",CommonUtility.validateString(userDetailsAddresss.getAddress2()));
	            context.put("city", CommonUtility.validateString(userDetailsAddresss.getCity()));
	            context.put("state",CommonUtility.validateString(userDetailsAddresss.getState()));
	            context.put("zip", CommonUtility.validateString(userDetailsAddresss.getZipCode()));
	            context.put("phone", CommonUtility.validateString(userDetailsAddresss.getPhoneNo()));
	            context.put("creditApplicationRequest", CommonUtility.validateString(userDetailsAddresss.getCreditApplicationRequest()));
	            context.put("position",userDetailsAddresss.getJobTitle());
	            context.put("websiteContact",userDetailsAddresss.getHowWebsiteContact());
	          }
			
			if(CommonUtility.validateString(userDetailsAddresss.getUserERPId()).length()>0) {
				  context.put("accountNumber",CommonUtility.validateString(userDetailsAddresss.getUserERPId()));
			}
			
			if(userDetails!=null) {
				context.put("address",userDetails);
			}
			
			
			if (formtype != null && formtype.trim().equalsIgnoreCase("1A")){
				
				if(whomTo!=null && whomTo.trim().equalsIgnoreCase("customer")){
					to = userDetailsAddresss.getEmailAddress();
					subject = propertyLoader("sentmailconfig.sendRegistrationMail.customer.1A.subject",CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
				}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("webUser")){
					to = notificationDetail.get("TO_EMAIL");

					subject = propertyLoader("sentmailconfig.sendRegistrationMail.webUser.1A.subject","");
				}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("superUser")){
					to = SuperUserEmail;

					subject = propertyLoader("sentmailconfig.sendRegistrationMail.superUser.1A.subject","");
				}
				
			}else if (formtype != null && formtype.trim().equalsIgnoreCase("1B")){
				
				context.put("companyName", userDetailsAddresss.getEntityName());
				context.put("regCompanyName", userDetailsAddresss.getCompanyName());
		        context.put("ReqAuthLevel", userDetailsAddresss.getRequestAuthorizationLevel());
	            context.put("SalesContact", userDetailsAddresss.getSalesContact());
	            context.put("accountNumber", userDetailsAddresss.getAccountName());
	            context.put("contactTitle", userDetailsAddresss.getContactTitle());
	            
				if(whomTo!=null && whomTo.trim().equalsIgnoreCase("customer")){
					to = userDetailsAddresss.getEmailAddress();
					
					if(CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION")!=null && CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION").trim().equalsIgnoreCase("Y")){
						String warehouseEmail = UsersDAO.getWareHouseDetail(userDetailsAddresss.getWareHouseCode());
						if(warehouseEmail!=null && !warehouseEmail.trim().equalsIgnoreCase("")){
							to = to + ";" + warehouseEmail;
						}
					}

					subject = propertyLoader("sentmailconfig.sendRegistrationMail.customer.1B.subject","");
				}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("webUser")){
					to = notificationDetail.get("TO_EMAIL");
					if(to==null){
						to = notificationDetail.get("BCC_EMAIL");
					}

					subject = propertyLoader("sentmailconfig.sendRegistrationMail.webUser.1B.subject","");
				}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("superUser")){
					to = SuperUserEmail;

					subject = propertyLoader("sentmailconfig.sendRegistrationMail.superUser.1B.subject","");
				}
				
			}else if(formtype != null && formtype.trim().equalsIgnoreCase("2A")){
				
				context.put("companyName", userDetailsAddresss.getEntityName());
				
				if(whomTo!=null && whomTo.trim().equalsIgnoreCase("customer")){
					to = userDetailsAddresss.getEmailAddress();

					subject = propertyLoader("sentmailconfig.sendRegistrationMail.customer.2A.subject","");
				}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("webUser")){
					to = notificationDetail.get("TO_EMAIL");
					if(to==null){
						to = notificationDetail.get("BCC_EMAIL");
					}

					subject = propertyLoader("sentmailconfig.sendRegistrationMail.webUser.2A.subject","");
				}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("superUser")){
					to = SuperUserEmail;

					subject = propertyLoader("sentmailconfig.sendRegistrationMail.superUser.2A.subject","");
				}
				
			}else if(formtype != null && formtype.trim().equalsIgnoreCase("2B")){
				
				context.put("companyName", userDetailsAddresss.getEntityName());
				
				if(whomTo!=null && whomTo.trim().equalsIgnoreCase("customer")){
					to = userDetailsAddresss.getEmailAddress();

					subject = propertyLoader("sentmailconfig.sendRegistrationMail.customer.2B.subject","");
				}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("webUser")){
					to = userDetailsAddresss.getEmailAddress();
					if(CommonUtility.validateString(to).length()>0 && CommonUtility.validateString(notificationDetail.get("TO_EMAIL")).length()>0){
						to = to+";"+notificationDetail.get("TO_EMAIL");
					}
					//to = (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";"+userDetailsAddresss.getEmailAddress():notificationDetail.get("BCC_EMAIL"));
					//to = notificationDetail.get("TO_EMAIL");
					if(to==null){
						to = notificationDetail.get("BCC_EMAIL");
					}
					subject = propertyLoader("sentmailconfig.sendRegistrationMail.webUser.2B.subject","");
				}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("superUser")){
					to = SuperUserEmail;

					subject = propertyLoader("sentmailconfig.sendRegistrationMail.superUser.2B.subject","");
				}
				
			}else if(formtype != null && formtype.trim().equalsIgnoreCase("CommertialCustomerCreditApplicationRequest")){
				if(CommonUtility.validateString(notificationDetail.get("TO_EMAIL")).length()>0){
					to = notificationDetail.get("TO_EMAIL");
				}
				if(to==null){
					to = notificationDetail.get("BCC_EMAIL");
				}
				subject = propertyLoader("sentmailconfig.sendRegistrationMail.customer.credit.application.request","");
        	}
			else if(formtype != null && formtype.trim().equalsIgnoreCase("addNewShippingAddress")){
				context.put("accountNumber", userDetailsAddresss.getEntityId());
				context.put("shipToName", userDetailsAddresss.getShipToName());
				if(whomTo!=null && whomTo.trim().equalsIgnoreCase("customer")) {
					to = userDetailsAddresss.getEmailAddress();
					if(!CommonUtility.validateString(to).isEmpty()){
						if(!CommonUtility.validateString(notificationDetail.get("TO_EMAIL")).isEmpty()) {
							to = to + ";" + notificationDetail.get("TO_EMAIL");
						} else {
							notificationDetail.put("TO_EMAIL", to);
						}
					} else {
						  to = notificationDetail.get("BCC_EMAIL");
					}
				}
				subject = SendMailUtility.propertyLoader("sentmailconfig.addNewShippingAddress","");
        	}
			
			context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            StringWriter writer = new StringWriter();
            if(notificationDetail!=null && CommonUtility.validateString(notificationDetail.get("DESCRIPTION")).length()>0){
            	Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            }
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());
            
            if(CommonUtility.validateNumber(CommonUtility.validateString(notificationDetail.get("CMS_STATIC_PAGE_ID")))>0) {
                finalMessage.append(getStaticPageRenderedContent(context,CommonUtility.validateNumber(CommonUtility.validateString(notificationDetail.get("CMS_STATIC_PAGE_ID")))));
            }

            if(notificationDetail!=null)
            {
            	if(CommonUtility.validateString(to).contains(CommonUtility.validateString(notificationDetail.get("TO_EMAIL")))){
            		notificationDetail.put("TO_EMAIL",to);
            	}else{
            		notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+to);
            	}
            

	            if((LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("purchaseagent.notificaion.email")!=null)&&(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("purchaseagent.notificaion.email")).length()>0)){
	    			UnilogFactoryInterface serviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
	    			if(serviceClass!=null) {
	    				flag = serviceClass.sendMailRegistration(userDetailsAddresss, formtype, whomTo, SuperUserEmail);
	    			}
	            }else{
	            	flag = sendNotification(notificationDetail, subject, fromEmail, finalMessage.toString());
	            }
	            String toMailList = notificationDetail.get("TO_EMAIL")+";"+to;
 			
	            String mailStatus = "N";
				if(flag){
					mailStatus = "Y";
				}
					boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, subject, fromEmail, finalMessage, mailStatus, emailType,null);
				System.out.println("Your Save Email in DB Status @ sendRegistrationMail : "+saveMailStatus);
            }
            
        	}else{
	    		System.out.println("-------------- Note : sendRegistrationMail mail sending : template Not Available in CIMM2 Notification");
	    	}
            
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBConnection(conn);
        }
        return flag;
	}
	
	public boolean sendSalesOrderMessageNew(LinkedHashMap<String, String> notification,  String subject, String from, String body){
   
		System.out.println("sending mail process :----");
        boolean result = false;
        String subject_data = subject;
    	HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session1 = request.getSession();//spam email change
		String sessionUserId = (String) session1.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		int buyingCompanyId = CommonUtility.validateNumber((String)session1.getAttribute("buyingCompanyId"));
		LinkedHashMap<String, String> customerCustomFieldValue  = UsersDAO.getAllCustomerCustomFieldValue(buyingCompanyId);
		if(customerCustomFieldValue!=null && customerCustomFieldValue.size()>0 && customerCustomFieldValue.containsKey("IN_SIDE_SALES_REP_CUST")){
			String repEmailId = customerCustomFieldValue.get("IN_SIDE_SALES_REP_CUST");
			if(CommonUtility.validateString(repEmailId).length()>0){
				String SalesRepNotificationList  = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SALESREP_NOTIFICATION_LIST"));
				if(SalesRepNotificationList.contains(CommonUtility.validateString(notification.get("NOTIFICATION_NAME_FROM_DB")))){
					notification.put("BCC_EMAIL",notification.get("BCC_EMAIL")+";"+repEmailId);	
				}
			}
		}
		if(customerCustomFieldValue!=null && customerCustomFieldValue.size()>0 && customerCustomFieldValue.containsKey("CC_ON_ALL_MAILS")){
			String ccOnAllMails = customerCustomFieldValue.get("CC_ON_ALL_MAILS");
			if(CommonUtility.validateString(ccOnAllMails).length()>0){
				if(notification.get("CC_EMAIL")!=null &&notification.get("CC_EMAIL").length()>0) {
				notification.put("CC_EMAIL",notification.get("CC_EMAIL")+";"+ccOnAllMails);
				}
				else {
					notification.put("CC_EMAIL",ccOnAllMails);
				}
			}	
		}

        

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
         
			 Authenticator authenticator = null;
          	 

          	 Session session = Session.getInstance(props, null);
             session.setDebug(true);

             //Construct the mail message
             MimeMessage message = new MimeMessage(session);
          
             
            Address[] toAddress = null;
 			Address[] ccAddress = null;
 			Address[] bccAddress = null;
 			List<Address> toAddressList = new ArrayList<Address>();
 			String toMailList ="";
 			String ccMailList ="";
 			String bccMailList ="";
 			int loop = 0;
 			if(notification.get("TO_EMAIL")!=null && !notification.get("TO_EMAIL").trim().equalsIgnoreCase("")){
 				String splitToAddress[] = notification.get("TO_EMAIL").split(";");
 				if(splitToAddress!=null && splitToAddress.length>0)
 				{
 					toAddress = new Address[splitToAddress.length];
 					for(String toAddr:splitToAddress)
 					{
 						toAddressList.add(new InternetAddress(toAddr.trim()));
 					}
 				}
 				toMailList = notification.get("TO_EMAIL");
 				
 			}
 			if(notification.get("toEmailAddressFromPage") != null && notification.get("toEmailAddressFromPage").toString().length() > 0){
 	                String toEmailAddressFromPage = notification.get("toEmailAddressFromPage").toString();
 	                String []toEmailAddressFromPageArr = toEmailAddressFromPage.split(";");
 	                toAddress = new Address[toEmailAddressFromPageArr.length];
 	                for(String toAddr:toEmailAddressFromPageArr)
 	                {
 	                	toAddressList.add(new InternetAddress(toAddr.trim()));
 	                }
 	        }
 			
 			if(toAddressList != null && toAddressList.size() > 0){
 	             toAddress = toAddressList.toArray(new Address[0]);
 	         }
 			
 			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SEND_EMAIL_TO_TEST_USERS")).length()>0){
 				loop = 0;
 				String sendtoTestUsers[]=CommonDBQuery.getSystemParamtersList().get("SEND_EMAIL_TO_TEST_USERS").split(";");
 				toAddress=null;
 				if(sendtoTestUsers!=null && sendtoTestUsers.length>0)
 				{  
 					toAddress = new Address[sendtoTestUsers.length];
 					for(String toAddr:sendtoTestUsers)
					{
						toAddress[loop] = new InternetAddress(toAddr.trim());
						loop++;
					}
				}
 				}
 			
 			
 			if(notification.get("CC_EMAIL")!=null && !notification.get("CC_EMAIL").trim().equalsIgnoreCase("")){
 				loop = 0;
 				String splitToAddress[] = notification.get("CC_EMAIL").split(";");
 				if(splitToAddress!=null && splitToAddress.length>0)
 				{
 					ccAddress = new Address[splitToAddress.length];
 					for(String toAddr:splitToAddress)
 					{
 						ccAddress[loop] = new InternetAddress(toAddr.trim());
 						loop++;
 					}
 				}
 				ccMailList = notification.get("CC_EMAIL");
 				
 			}
 			if(notification.get("BCC_EMAIL")!=null && !notification.get("BCC_EMAIL").trim().equalsIgnoreCase("")){
 				loop = 0;
 				String splitToAddress[] = notification.get("BCC_EMAIL").split(";");
 				if(splitToAddress!=null && splitToAddress.length>0)
 				{
 					bccAddress = new Address[splitToAddress.length];
 					for(String toAddr:splitToAddress)
 					{
 						if(splitToAddress[loop].length()>0) {
 						bccAddress[loop] = new InternetAddress(toAddr.trim());
 						loop++;
 						}
 					}
 				}
 				bccMailList = notification.get("BCC_EMAIL");
 				
 			}
             
             
           String techSupp =   CommonDBQuery.getSystemParamtersList().get("REPLYTO_EMAIL");
           Address replytoList[] = null;
           if(techSupp!=null && !techSupp.trim().equalsIgnoreCase(""))
           {
        	   String techSuppArr[] = techSupp.split(";");
        	   replytoList = new Address[techSuppArr.length];
        	   int i = 0;
        	   for(String techEmail:techSuppArr)
        	   {
        		   replytoList[i] = new InternetAddress(techEmail.trim());
        		   i++;
        	   }
           }
           if(replytoList!=null && replytoList.length>0){
             message.setReplyTo(replytoList);//ADDED
       	   }
             message.saveChanges();
             if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")).length()>0){
            	 message.setFrom(new InternetAddress(from,CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")));
             }else{
            	 message.setFrom(new InternetAddress(from));
             }
             //message.setFrom(new InternetAddress(from));
             
             
             if(toAddress!=null && toAddress.length>0){
            	 message.addRecipients(Message.RecipientType.TO,toAddress );
             }else{
            	 message.addRecipients(Message.RecipientType.TO,bccAddress );
             }
             
             if(ccAddress!=null && ccAddress.length>0)
            	 message.addRecipients(Message.RecipientType.CC,ccAddress );
             
             if(bccAddress!=null && bccAddress.length>0)
            	 message.addRecipients(Message.RecipientType.BCC,bccAddress );
            
             message.setSubject(subject_data);
            
             /*Multipart mp = new MimeMultipart();
             MimeBodyPart messageBodyPart = new MimeBodyPart();
             messageBodyPart.setDataHandler(new DataHandler(new HTMLDataSource(body)));
             mp.addBodyPart(messageBodyPart);

             // Put parts in message
             message.setContent(mp);
             message.saveChanges();
             //Use Transport to deliver the message
             Transport transport = session.getTransport("smtp");
             transport.connect("relay.ncelec.com", "ncelec@ncelec.com", "");
             transport.sendMessage(message, message.getAllRecipients());
             transport.close();*/
             
             
            message.setSubject(subject);
          	message.setContent(body,"text/html");
          	message.setSentDate(new Date());
          	/*spam email changes start*/
          	System.out.println("current_time: "+current_time);
          	System.out.println("last_mail_time: "+last_mail_time);
          	System.out.println("count: "+count);
          	if(CommonDBQuery.isLocalDevelopmentEnvironment()) {
          		System.out.println("Mail Body:\n"+body);
          	}
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
	
	public static boolean sendMailWithAttachment(LinkedHashMap<String, String> notification, String[] attachments, String[] attachmentsFileName){System.out.println("sending mail with Attachment in process :----");
		boolean result = false;
    
    try {
       
       String subject_data = CommonUtility.validateString(notification.get("SUBJECT"));
       String from = CommonUtility.validateString(notification.get("FROM_MAIL_ADDRESS"));
       String body = CommonUtility.validateString(notification.get("EMAIL_CONTENT_TO_SEND"));
       String clientHost = CommonDBQuery.getSystemParamtersList().get("CLIENTHOST");
       SecureData validUserPass = new SecureData();
        String emailRelayEncrpt = CommonDBQuery.getSystemParamtersList().get("EMAILRELAY");
        int emailRelayPort = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAILSMTPPORT"));
       String mailSmtp = CommonDBQuery.getSystemParamtersList().get("MAILSMTP");
         
         String emailRelay = validUserPass.validatePassword(emailRelayEncrpt);
         Properties props = new Properties();

         Authenticator authenticator = null;
         if(CommonDBQuery.getSystemParamtersList().get("MAILSELECT")!=null && CommonDBQuery.getSystemParamtersList().get("MAILSELECT").trim().equalsIgnoreCase("MODIFIED"))
         {
             System.out.println("Inside New Mail Code....");
             props.put("mail.smtp.host", emailRelay);
             
             if(emailRelayPort==0){
                 emailRelayPort = 25;
             }
             
             props.put("mail.smtp.port", emailRelayPort);
             //props.put("mail.smtp.user", USER);
             props.put("mail.smtp.from", from);
             props.put("mail.smtp.auth", "false");
             props.put("mail.smtp.starttls.enable", "false");
             props.put("mail.smtp.debug", "true");
             props.put("mail.smtp.socketFactory.port", emailRelayPort);
             //props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
             props.put("mail.smtp.socketFactory.fallback", "false");
             if(clientHost!=null && clientHost.length()>0){
                 props.put("mail.smtp.ehlo", true);
                 props.put("mail.smtp.localhost", clientHost);
                 }
         }
         else
         {
             System.out.println("Inside old Mail Code...."); 
             authenticator = new SendMailUtility().new SMTPAuthenticator();
             props.put("mail.smtp.port", emailRelayPort);
             props.put("mail.smtp.host", mailSmtp);
             props.put("mail.smtp.auth", "true");
         }
         
           Session session = Session.getInstance(props, authenticator);
         session.setDebug(true);

         //Construct the mail message
         MimeMessage message = new MimeMessage(session);
      
         
        Address[] toAddress = null;
         Address[] ccAddress = null;
         Address[] bccAddress = null;
         List<Address> toAddressList = new ArrayList<Address>();
         int loop = 0;
         if(notification.get("TO_EMAIL")!=null && !notification.get("TO_EMAIL").trim().equalsIgnoreCase("")){
             //loop = 0;
             String splitToAddress[] = notification.get("TO_EMAIL").split(";");
             if(splitToAddress!=null && splitToAddress.length>0)
             {
                 //toAddress = new Address[splitToAddress.length];
                 for(String toAddr:splitToAddress)
                 {
                     toAddressList.add(new InternetAddress(toAddr.trim()));
                     //toAddress[loop] = new InternetAddress(toAddr.trim());
                     //loop++;
                 }
             }
             
         }
         
         if(notification.get("toEmailAddressFromPage") != null && notification.get("toEmailAddressFromPage").toString().length() > 0){
             //loop = toAddress.length;
                String toEmailAddressFromPage = notification.get("toEmailAddressFromPage").toString();
                String []toEmailAddressFromPageArr = toEmailAddressFromPage.split(";");
                
                for(String toAddr:toEmailAddressFromPageArr)
                {
                    toAddressList.add(new InternetAddress(toAddr.trim()));
                    /*toAddress[loop] = new InternetAddress(toAddr.trim());
                    loop++;*/
                }
        }
         
         if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SEND_EMAIL_TO_TEST_USERS")).length()>0){
             String sendtoTestUsers[]=CommonDBQuery.getSystemParamtersList().get("SEND_EMAIL_TO_TEST_USERS").split(",");
             toAddressList.clear();
             if(sendtoTestUsers!=null && sendtoTestUsers.length>0)
             {
                 //toAddress = new Address[splitToAddress.length];
                 for(String toAddr:sendtoTestUsers)
                 {
                     toAddressList.add(new InternetAddress(toAddr.trim()));
                     //toAddress[loop] = new InternetAddress(toAddr.trim());
                     //loop++;
                 }
             }
         }
         
         if(toAddressList != null && toAddressList.size() > 0){
             toAddress = toAddressList.toArray(new Address[0]);
         }
         
         //toAddress = new Address[toAddressList.size()];
         if(notification.get("CC_EMAIL")!=null && !notification.get("CC_EMAIL").trim().equalsIgnoreCase("")){
             loop = 0;
             String splitToAddress[] = notification.get("CC_EMAIL").split(";");
             if(splitToAddress!=null && splitToAddress.length>0)
             {
                 ccAddress = new Address[splitToAddress.length];
                 for(String toAddr:splitToAddress)
                 {
                     ccAddress[loop] = new InternetAddress(toAddr.trim());
                     loop++;
                 }
             }
             
         }
         if(notification.get("BCC_EMAIL")!=null && !notification.get("BCC_EMAIL").trim().equalsIgnoreCase("")){
             loop = 0;
             String splitToAddress[] = notification.get("BCC_EMAIL").split(";");
             if(splitToAddress!=null && splitToAddress.length>0)
             {
                 bccAddress = new Address[splitToAddress.length];
                 for(String toAddr:splitToAddress)
                 {
                     bccAddress[loop] = new InternetAddress(toAddr.trim());
                     loop++;
                 }
             }
             
         }
         
         
       String techSupp =   CommonDBQuery.getSystemParamtersList().get("REPLYTO_EMAIL");
       Address replytoList[] = null;
       if(techSupp!=null && !techSupp.trim().equalsIgnoreCase(""))
       {
           String techSuppArr[] = techSupp.split(";");
           replytoList = new Address[techSuppArr.length];
           int i = 0;
           for(String techEmail:techSuppArr)
           {
               replytoList[i] = new InternetAddress(techEmail.trim());
               i++;
           }
       }
       if(replytoList!=null && replytoList.length>0){
         message.setReplyTo(replytoList);//ADDED
          }
         message.saveChanges();
         if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")).length()>0){
             message.setFrom(new InternetAddress(from,CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")));
         }else{
             message.setFrom(new InternetAddress(from));
         }
         //message.setFrom(new InternetAddress(from));
         
         
         if(toAddress!=null && toAddress.length>0){
             message.addRecipients(Message.RecipientType.TO,toAddress );
         }else{
             message.addRecipients(Message.RecipientType.TO,bccAddress );
         }
         
         if(ccAddress!=null && ccAddress.length>0){
             message.addRecipients(Message.RecipientType.CC,ccAddress );
         }
            
         if(bccAddress!=null && bccAddress.length>0){
             message.addRecipients(Message.RecipientType.BCC,bccAddress );
         }
             
        
         message.setSubject(subject_data);
        
        
         
         BodyPart messageBodyPart = null;
         Multipart multipart = new MimeMultipart();
         
         messageBodyPart = new MimeBodyPart();
         messageBodyPart.setContent(body,"text/html");
         multipart.addBodyPart(messageBodyPart);
         
		 if(attachments!=null && attachments.length>0){
		     System.out.println("attachments.length : "+attachments.length);
		     int attachmentIdx = 0; 
		     for (String attachmentPath : attachments) {
		         if(CommonUtility.validateString(attachmentPath).length()>0){
		             System.out.println("attachmentPath : "+attachmentPath);
		             String attachedFileName = attachmentPath;
		             if(attachmentsFileName!=null && attachmentsFileName.length>0 && CommonUtility.validateString(attachmentsFileName[attachmentIdx]).length()>0){
		                 attachedFileName = CommonUtility.validateString(attachmentsFileName[attachmentIdx]);
		             }
		             messageBodyPart = new MimeBodyPart();
		             DataSource source = new FileDataSource(attachmentPath);
		             messageBodyPart.setDataHandler(new DataHandler(source));
		             messageBodyPart.setFileName(attachedFileName);
		             multipart.addBodyPart(messageBodyPart);
		         }
		         attachmentIdx++;
		    }
		 }
            
         message.setContent(multipart);
         message.setSentDate(new Date());
         
         Transport.send(message);

         result = true;
     }catch (Exception e) {
         e.printStackTrace();
     }
     return result;
     }
	
	private static void addAttachment(Multipart multipart, String filename)
	{
		try {
		
		    DataSource source = new FileDataSource(filename);
		    BodyPart messageBodyPart = new MimeBodyPart();        
		    messageBodyPart.setDataHandler(new DataHandler(source));
		    messageBodyPart.setFileName(filename);
		    multipart.addBodyPart(messageBodyPart);
		} catch (Exception e) {
			
		}
	}
	
	public boolean sendSalesOrderMessage(String to,  String subject, String from, String body,String bcc,String cc)
    {
		System.out.println("sending mail process :----");
        boolean result = false;
        String subject_data = subject;
        if(CommonDBQuery.getSystemParamtersList().get("MAILSELECT")!=null && CommonDBQuery.getSystemParamtersList().get("MAILSELECT").trim().equalsIgnoreCase("MODIFIED"))
        {
        	System.out.println("Inside New Mail Code....");
        	LinkedHashMap<String, String> notificationDetail = new LinkedHashMap<String, String>();
        	notificationDetail.put("TO_EMAIL", to);
			notificationDetail.put("BCC_EMAIL", bcc);
			notificationDetail.put("CC_EMAIL", cc);
        	result = sendSalesOrderMessageNew(notificationDetail, subject_data, from, body);
        }
        else if(CommonDBQuery.getSystemParamtersList().get("MAILSELECT")!=null && CommonDBQuery.getSystemParamtersList().get("MAILSELECT").trim().equalsIgnoreCase("DEFAULT"))
        {

        	try {
        		System.out.println("Inside Old Mail Code....");
        		String mailSmtp = CommonDBQuery.getSystemParamtersList().get("MAILSMTP");
        		int emailRelayPort = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAILSMTPPORT"));
        		if(emailRelayPort==0){
   	        	 	emailRelayPort = 25;
   	         	}
             	Properties props = System.getProperties();
             	props.put("mail.smtp.host",mailSmtp);
             	props.put("mail.smtp.port",emailRelayPort);
             	props.put("mail.smtp.debug", "true");
             	props.put("mail.smtp.ehlo", true);
             	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")).length()>0) {
                    props.put("mail.smtp.ssl.trust", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")));
            	}
             	/**
    			 *Below code Written is for Tyndale to enable SSL and Auth from code *Reference- Chetan Sandesh
    			 */
              	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_FROMUSERNAME")).length()>0) {
	             	props.put("mail.smtp.starttls.enable", "true");
	             	props.put("mail.smtp.auth", "true");
	             	props.put("mail.transport.protocol", "smtp");
              	}
             	Authenticator authenticator = new SMTPAuthenticator();
             	Session session = Session.getInstance(props, authenticator);
                MimeMessage message = new MimeMessage(session);
                if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")).length()>0){
    				message.setFrom(new InternetAddress(from,CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")));
				}else{
					message.setFrom(new InternetAddress(from));
				}
                //message.setFrom(new InternetAddress(from));
                if(to == null)
                {
               	 message.addRecipient(Message.RecipientType.TO, new InternetAddress(bcc));
                    
                }
                else
                {
                Address[] mailAddress_bcc = null;
                String[] bccList=null;
                if (bcc!=null){
                	bccList  = bcc.split(";");
                }
                if(bccList!=null && bccList.length>0)
                mailAddress_bcc = new Address[bccList.length];
                
                int i=0;
                if(bccList!=null && bccList.length>0){
                	for(String a:bccList){
                		if(mailAddress_bcc!=null) {
                			mailAddress_bcc[i] = new InternetAddress(a);
                		}
                    	i++;
                    }
                }
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.addRecipients(Message.RecipientType.BCC, mailAddress_bcc);
                }
                message.setSubject(subject_data);
               
                Multipart mp = new MimeMultipart();

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(new HTMLDataSource(body)));
                mp.addBodyPart(messageBodyPart);

                // Put parts in message
                message.setContent(mp);

                // Send the message
                Transport.send(message);
                result = true;
            } catch (Exception e) {
           	 e.printStackTrace();
            }
        
        }
        else
        {
        	try {
        		System.out.println("Inside Old Mail Code....");
           	 	SecureData validUserPass = new SecureData();
      			String emailRelayEncrpt = CommonDBQuery.getSystemParamtersList().get("EMAILRELAY");
      			String emailRelay = validUserPass.validatePassword(emailRelayEncrpt);
      			int emailRelayPort = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAILSMTPPORT"));
        		if(emailRelayPort==0){
   	        	 	emailRelayPort = 25;
   	         	}
             	Properties props = System.getProperties();
             	props.put("mail.smtp.host",emailRelay);
             	props.put("mail.smtp.port",emailRelayPort);
             	Authenticator authenticator = null;
             	Session session = Session.getInstance(props, authenticator);
                MimeMessage message = new MimeMessage(session);
                
                message.setFrom(new InternetAddress(from));
                if(to == null){
                	message.addRecipient(Message.RecipientType.TO, new InternetAddress(bcc));
                }else{
	                Address[] mailAddress_bcc = null;
	                String[] bccList  = bcc.split(";");
	                if(bccList!=null && bccList.length>0){
	                	mailAddress_bcc = new Address[bccList.length];	
	                	for(int i=0;i<bccList.length;i++){
		                	if(mailAddress_bcc!=null) {
		                		mailAddress_bcc[i] = new InternetAddress(bccList[i]);
		                	}
		                }
	                }
	                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	                message.addRecipients(Message.RecipientType.BCC, mailAddress_bcc);
                }
                message.setSubject(subject_data);
                Multipart mp = new MimeMultipart();
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(new HTMLDataSource(body)));
                mp.addBodyPart(messageBodyPart);// Put parts in message
                message.setContent(mp); //Send the message
                Transport.send(message);
                result = true;
            }catch (Exception e){
            	e.printStackTrace();
            }
        }
        System.out.println("sending mail process  completed:----");
        return result;
    }

    //Mail Builder
	public boolean sendApprovalMail(int GroupId, String savedGroupName,String email,String firstName,String lastName,String updatedDate,String orderStatus,int orderId,String rejectReason,String ApproverEmail){		
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    String tempSubset = (String) session.getAttribute("userSubsetId");
		String SiteImgPath = (String) session.getAttribute("siteImagePath");
		int subsetId = CommonUtility.validateNumber(tempSubset);
		String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);	    
	    boolean flag = false;
	    String cartApprovalMailSubject="sentmailconfig.sendApprovalMail.subject";
	    
	    //String userName, String userPassword,String emailId,String firstName,String lastName
	    ArrayList<SalesModel> salesOrderItem = new ArrayList<SalesModel>();
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        try
        {
    		salesOrderItem = SalesDAO.sendApprovalMailDao(salesOrderItem, orderStatus, orderId, GroupId, userId, subsetId, generalSubset, buyingCompanyId);
	    
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("ApproveCartMail");
            VelocityContext context = new VelocityContext();
          
            String toEmailId = email;
            if(CommonUtility.customServiceUtility() != null) {
            	context.put("lineItemAsMap", CommonUtility.customServiceUtility().loadPrice(salesOrderItem, session));
            }
            System.out.println("near context object"+salesOrderItem);
            context.put("orderId", orderId);
            context.put("lineItemList", salesOrderItem);
            context.put("orderStatus", orderStatus);
            context.put("firstName", firstName);
            if(lastName!=null && lastName.trim().length() > 0){
            	lastName = lastName.trim();
            }else{
            	lastName = "";
            }
            context.put("lastName", lastName);
            context.put("Cart ", savedGroupName);
            if(!rejectReason.equalsIgnoreCase("")){
            context.put("rejectReason", "Reason for Rejection: "+rejectReason);
            }
           
            
            context.put("orderDate", updatedDate);
            SalesModel salesInputParameter = new SalesModel();
		    salesInputParameter.setOrderID(CommonUtility.validateParseIntegerToString(orderId));
			
		    if(CommonUtility.validateString(salesInputParameter.getOrderID()).length()>0) {
		    	SalesModel orderDetailModel = SalesDAO.getOrderDetailByID(salesInputParameter);
		    	context.put("orderDetailModel", orderDetailModel);
		    }
            
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            context.put("session", session);
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            if(CommonUtility.customServiceUtility()!=null) {
            	CommonUtility.customServiceUtility().setOrderNumber(context,orderId);
			}
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId);
			notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
			if(CommonUtility.customServiceUtility() != null) {
	        	 cartApprovalMailSubject=CommonUtility.customServiceUtility().setRejectOrderMailSubject(orderStatus,cartApprovalMailSubject);
	        }
			flag = sendNotification(notificationDetail,propertyLoader(cartApprovalMailSubject,""), fromEmail, finalMessage.toString());
			String toMailList = notificationDetail.get("TO_EMAIL")+";"+toEmailId;
			
			String mailStatus = "N";
			if(flag){
				mailStatus = "Y";
			}
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.sendApprovalMail.subject",""), fromEmail, finalMessage, mailStatus, "Approve Cart Status",null);
			System.out.println("Your Save Email in DB Status @ sendApprovalMail : "+saveMailStatus);
        }catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
			return flag;	
	}
	
	public static boolean buildNewUser(String customerId,String userName, String userPassword,String emailId,String firstName,String lastName){		
		
		boolean flag = false;
	    HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    String SiteImgPath = (String) session.getAttribute("siteImagePath");
	    
	    try{
        	
        	String templatePath = CommonDBQuery.getSystemParamtersList().get("ERPTEMPLATEPATH");
        	System.out.println("templatePath : " + templatePath);
        	VelocityEngine velocityTemplateEngine = new VelocityEngine();
          	velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
          	velocityTemplateEngine.init();
            Template t = velocityTemplateEngine.getTemplate("NewUser.xml");
            /*  create a context and add data */
            VelocityContext context = new VelocityContext();
          
            context.put("firstName", firstName);
            if(lastName!=null && lastName.trim().length() > 0){
            	lastName = lastName.trim();
            }else{
            	lastName = "";
            }
            context.put("lastName", lastName);
            context.put("userName", userName);
            context.put("customerId", customerId);
            context.put("password", userPassword);
            if(SiteImgPath=="MIN_"){
            	context.put("siteLogoPath","MIN_images");
            }else{
            	context.put("siteLogoPath","images");
            }
            StringWriter writer = new StringWriter();
            t.merge(context, writer);
            
            /* show the World */
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			String newUserRegEmail = CommonDBQuery.getSystemParamtersList().get("NEW_USER_REGISTRATION_MAIL");
			SendMailUtility sendMail = new SendMailUtility();
			flag=sendMail.sendSalesOrderMessage(emailId,propertyLoader("sentmailconfig.buildNewUser.subject",""),fromEmail,finalMessage.toString(),newUserRegEmail,null);
			String mailStatus = "N";
			if(flag)
			{
				mailStatus = "Y";
			}
			UsersDAO.saveEmailsInDB(emailId, propertyLoader("sentmailconfig.buildNewUser.subject",""), fromEmail, finalMessage, mailStatus, "NewUser",null);
			
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
       	return flag;	
	}
	
	//Mail Builder
	public boolean buildChangePasswordMail(SendMailModel sendMailModel){		
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    String SiteImgPath = (String) session.getAttribute("siteImagePath");
	    boolean flag = false;
	    
	    //String userName, String userPassword,String emailId,String firstName,String lastName
	    
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        
        try{
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("ChangePassword");
            VelocityContext context = new VelocityContext();
          
            String toEmailId = sendMailModel.getToEmailId();
            String lastName = "";
            if(sendMailModel.getLastName()!=null && sendMailModel.getLastName().trim().length() > 0){
            	lastName = sendMailModel.getLastName().trim();
            }else{
            	lastName = "";
            }
            
            context.put("firstName", sendMailModel.getFirstName());
            context.put("lastName", lastName);
            context.put("userName", sendMailModel.getUserName());
            context.put("password", sendMailModel.getPassword());
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId);
			notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
			
			flag = sendNotification(notificationDetail,propertyLoader("sentmailconfig.buildChangePasswordMail.subject",""), fromEmail, finalMessage.toString());
			String toMailList = notificationDetail.get("TO_EMAIL")+";"+toEmailId;
			
			String mailStatus = "N";
			if(flag){
				mailStatus = "Y";
			}
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.buildChangePasswordMail.subject",""), fromEmail, finalMessage, mailStatus, "ChangePassword",null);
			System.out.println("Your Save Email in DB Status @ buildChangePasswordMail : "+saveMailStatus);
        }catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
			return flag;	
	}
	
	//Mail Builder
	public boolean buildForgotPasswordMail(SendMailModel sendMailModel){		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    String SiteImgPath = (String) session.getAttribute("siteImagePath");
	    boolean flag = false;
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        
        try{
        	LinkedHashMap<String, String> notificationDetail = null;
        	if(CommonUtility.validateString(sendMailModel.getTemplateName()).length()>0){
        		notificationDetail = getNotificationDetail.getNotificationDetails(sendMailModel.getTemplateName());
        	}else{
        		notificationDetail = getNotificationDetail.getNotificationDetails("ForgotPassword");
        	}
        	
            VelocityContext context = new VelocityContext();
          
            String toEmailId = sendMailModel.getToEmailId();
            String lastName = "";
            if(sendMailModel.getLastName()!=null && sendMailModel.getLastName().trim().length() > 0){
            	lastName = sendMailModel.getLastName().trim();
            }else{
            	lastName = "";
            }
            context.put("emailAddress", sendMailModel.getToEmailId());
            context.put("firstName", sendMailModel.getFirstName());
            context.put("lastName", lastName);
            context.put("userName", sendMailModel.getUserName());
            context.put("password", sendMailModel.getPassword());
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
             finalMessage.append(writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId);
			notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
			
			flag = sendNotification(notificationDetail,propertyLoader("sentmailconfig.buildForgotPasswordMail.subject",""), fromEmail, finalMessage.toString());
			String toMailList = notificationDetail.get("TO_EMAIL")+";"+toEmailId;
			
			String mailStatus = "N";
			if(flag){
				mailStatus = "Y";
			}
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.buildForgotPasswordMail.subject",""), fromEmail, finalMessage, mailStatus, "ForgotPassword",null);
			System.out.println("Your Save Email in DB Status @ buildForgotPasswordMail : "+saveMailStatus);
        }catch(Exception e){
        	flag = false;
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
			return flag;	
	}
	
	//Mail Builder
	public boolean sendChangedPrivilegsMail(String emailId,String firstName,String lastName,ArrayList<String> status,ArrayList<UsersModel> assigned , ArrayList<UsersModel> removed ){		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    boolean flag = false;
	    HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    String SiteImgPath = (String) session.getAttribute("siteImagePath");
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        
        try{
        	Date date = new Date();
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("AssignPreviMail");

        	VelocityContext context = new VelocityContext();
        	context.put("session", session);
        	if(CommonUtility.customServiceUtility() != null) {
        		context.put("currentStatus", CommonUtility.customServiceUtility().extractAssignedRole(status));
        	}
        	
        	context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            context.put("firstName", firstName);
            if(lastName!=null && lastName.trim().length() > 0){
            	lastName = lastName.trim();
            }else{
            	lastName = "";
            }
            context.put("lastName", lastName);
            context.put("status",status);
            if(assigned.size() == 0){
            	context.put("aList", "empty");
            }else{
            	context.put("assinedList", assigned);
            }
            
           if(removed.size() == 0){
            	context.put("rList", "empty");
            }else{
            	context.put("removedList", removed);
            }
            
            context.put("$orderDate", date);
            if(SiteImgPath=="MIN_"){
            	context.put("siteLogoPath","MIN_images");
            }else{
            	context.put("siteLogoPath","images");
            }
           
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());


            String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			String toMailList =  "";
			if(notificationDetail.get("TO_EMAIL")!=null && notificationDetail.get("TO_EMAIL").trim().length()>0){
				toMailList = notificationDetail.get("TO_EMAIL");
			}
			if(emailId!=null && emailId.trim().length()>0){
				toMailList = emailId+";"+toMailList;
			}
			notificationDetail.put("TO_EMAIL", toMailList);
			notificationDetail.put("BCC_EMAIL", customerEmail!=null?customerEmail+";" : notificationDetail.get("BCC_EMAIL"));
			//Adding system parameter to disable the notification triggering and only saving the mails in DB for reference
			if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DISABLE_MAIL_TRIGGERING")).equalsIgnoreCase("Y")) {
				flag = sendNotification(notificationDetail,propertyLoader("sentmailconfig.sendChangedPrivilegsMail.subject",""), fromEmail, finalMessage.toString());
			}
			
			
			String mailStatus = "N";
			if(flag){
				mailStatus = "Y";
			}
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.buildChangePasswordMail.subject",""), fromEmail, finalMessage, mailStatus, "ForgotPassword",null);
			System.out.println("Your Save Email in DB Status : "+saveMailStatus);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
		return flag;
	}
	
	//Mail Builder
	public boolean sendSharedCartInfoMail(String savedGroupName,String firstName,String lastName,String emailId,String SharedBy,String SharedByEmail,String msg){
	    boolean flag = false;
        try{
        	
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("SharedCartMail");
            VelocityContext context = new VelocityContext();
        	context.put("firstName", firstName);
            context.put("lastName", lastName);
            context.put("shareCartName",savedGroupName);
            context.put("sharedBy", SharedBy);
            context.put("comment",msg);
            context.put("sharedByEmailId",SharedByEmail); 
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonUtility.customServiceUtility() != null) {
				context.put("savedGroupId",CommonUtility.customServiceUtility().sendSharedCart(savedGroupName));
			}

            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
             finalMessage.append(writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+emailId);
			notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
			
			if(CommonUtility.validateString(msg).length()>0 && msg.equalsIgnoreCase("fromShoppingListPage")) {
				flag = sendNotification(notificationDetail,propertyLoader("sentmailconfig.sendShoppingListInfoMail.subject",""), fromEmail, finalMessage.toString());	
			}
			else {
				flag = sendNotification(notificationDetail,propertyLoader("sentmailconfig.sendSharedCartInfoMail.subject",""), fromEmail, finalMessage.toString());
			}
			String toMailList = notificationDetail.get("TO_EMAIL")+";"+emailId;
			
			String mailStatus = "N";
			if(flag){
				mailStatus = "Y";
			}
			if(CommonUtility.validateString(msg).length()>0 && msg.equalsIgnoreCase("fromShoppingListPage")) {
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.sendShoppingListInfoMail.subject",""), fromEmail, finalMessage, mailStatus, "ForgotPassword",null);
			System.out.println("Your Save Email in DB Status @ sendSharedCartInfoMail : "+saveMailStatus);
			}
			else {
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.sendSharedCartInfoMail.subject",""), fromEmail, finalMessage, mailStatus, "ForgotPassword",null);
			System.out.println("Your Save Email in DB Status @ sendSharedCartInfoMail : "+saveMailStatus);
			}
                   
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
 		return flag;	
	}
	
	//Mail Builder
	//public boolean sendOrderConfirmationMail(SalesModel orderDetail, ArrayList<SalesModel> orderItemList,SendMailModel sendMailModel,CreditCardModel creditCardValue){
	public boolean sendOrderConfirmationMail(SalesModel orderDetail, ArrayList<SalesModel> orderItemList,SendMailModel sendMailModel,CreditCardModel creditCardValue,List<Discount> appliedlDiscounts){
		HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    boolean flag = false;
	    String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
	    String erpDisplayName = null;
	    DecimalFormat df2 = CommonUtility.getPricePrecision(session);
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy"); //  HH:mm
		Date date = new Date();
	    
	    try{
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("OrderMail");
        	String orderMailAddressCustomerWise = UsersDAO.getCustomerCustomTextFieldValue(CommonUtility.validateNumber(buyingCompanyId),"ORDER_SUBMIT_EMAIL_ID");
        	sendMailModel.setUserId(CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY)));
        	
            VelocityContext context = new VelocityContext();
            context.put("orderDetailObject", orderDetail);
            context.put("pickUpLocation", CommonUtility.getWareHouseByCode(orderDetail.getShippingBranchId()));
            if(session!=null){
            	 if(session.getAttribute("loginCustomerName")!=null) {
                 	context.put("loginCustomerName", CommonUtility.validateString((String)session.getAttribute("loginCustomerName")));
                 }
            	 
            	 String csrAdminName = "";
            	 Map<String, String> salesUserDetails = (Map<String, String>) session.getAttribute("salesUserDetails");
            	 if(session.getAttribute("isSalesUser")!=null && CommonUtility.validateString((String) session.getAttribute("isSalesUser")).equalsIgnoreCase("Y")) {
            		 context.put("isSalesUser", CommonUtility.validateString((String) session.getAttribute("isSalesUser")));
                 }
            	 if(session.getAttribute("isSalesAdmin")!=null && CommonUtility.validateString((String) session.getAttribute("isSalesAdmin")).equalsIgnoreCase("Y")) {
            		 context.put("isSalesAdmin", CommonUtility.validateString((String) session.getAttribute("isSalesAdmin")));
            		 context.put("csrRole", "Administrator");
                 }else {
                	 context.put("csrRole", "Sales Rep");
                 }
            	 if(salesUserDetails!=null) {
            		 if(CommonUtility.validateString(salesUserDetails.get("firstName")).length()>0) {
            			 csrAdminName = CommonUtility.validateString(salesUserDetails.get("firstName"))+" "; 
            		 }
            		 if(CommonUtility.validateString(salesUserDetails.get("lastName")).length()>0) {
            			 csrAdminName =  csrAdminName + CommonUtility.validateString(salesUserDetails.get("lastName"));
            		 }
                 	context.put("csrAdminName", CommonUtility.validateString(csrAdminName));
            	 }
            }
          
            String toEmailId = sendMailModel.getToEmailId();
            String lastName = "";
            if(sendMailModel.getLastName()!=null && sendMailModel.getLastName().trim().length() > 0){
            	lastName = sendMailModel.getLastName().trim();
            }else{
            	lastName = "";
            }
            
            
            StringBuilder shippAddress = new StringBuilder();
            StringBuilder billAddress = new StringBuilder();
            String errDesc = orderDetail.getOrderStatusDesc();
            StringBuilder shippAddressFromModel = new StringBuilder();
            StringBuilder billAddressFromModel = new StringBuilder();
            
            if(errDesc==null){errDesc = "Order Failed.";}
            
           	if(orderDetail!=null) {
	        		if(CommonUtility.validateString(orderDetail.getBillAddress().getCompanyName()).length() > 0) {
	        			billAddressFromModel.append(orderDetail.getBillAddress().getCompanyName() +"<br />");
	            	}
	        		billAddressFromModel.append(orderDetail.getBillAddress().getAddress1()  +"<br />");
	        		if(orderDetail.getBillAddress().getAddress2()!=null){
	                	billAddressFromModel.append(orderDetail.getBillAddress().getAddress2() +"<br />");
	                }
	        		billAddressFromModel.append("<br/>"+orderDetail.getBillAddress().getCity()+", "+orderDetail.getBillAddress().getState()+" "+orderDetail.getBillAddress().getZipCode()+"<br />");
	                billAddress.append(orderDetail.getBillCountry()+"<br />");
	                if(orderDetail.getBillAddress().getPhoneNo()!=null){
	                	billAddressFromModel.append("Ph: " + orderDetail.getBillAddress().getPhoneNo());
	                }
	                
	                shippAddressFromModel.append(orderDetail.getBillAddress().getCompanyName() +"<br />");
	                if(orderDetail.getShipAddress().getShipToId()!=null && orderDetail.getShipAddress().getShipToId().trim().length() > 0){
	                	shippAddressFromModel.append(orderDetail.getShipAddress().getShipToId() + "-");
	                }
	                if(orderDetail.getShipAddress().getShipToName()!=null && orderDetail.getShipAddress().getShipToName().trim().length() > 0){
	                	shippAddressFromModel.append(orderDetail.getShipAddress().getShipToName() + "<br />");
	                }else{
	                	shippAddressFromModel.append("<br />");
	                }
	                shippAddressFromModel.append(orderDetail.getShipAddress().getAddress1()  +"<br />");
	                if(orderDetail.getShipAddress().getAddress2()!=null && orderDetail.getShipAddress().getAddress2().trim().length() > 0){
	                	shippAddressFromModel.append(orderDetail.getShipAddress().getAddress2() +"<br />");
	                }
	                shippAddressFromModel.append(orderDetail.getShipAddress().getCity()+", "+orderDetail.getShipAddress().getState()+" "+orderDetail.getShipAddress().getZipCode()+"<br />");
	                /*if((String) session.getAttribute("userEmailAddress")!=null && ((String) session.getAttribute("userEmailAddress")).trim().length() > 0){
	                	shippAddressFromModel.append("Email: " +(String) session.getAttribute("userEmailAddress") + "<br />");
	                }*/
	                //shippAddress.append(orderDetail.getShipCountry()+"<br />");
	                if(orderDetail.getShipAddress().getPhoneNo()!=null){
	                	shippAddressFromModel.append("Ph: " + orderDetail.getShipAddress().getPhoneNo());
	                }
	                
	                billAddress.append(orderDetail.getBillAddress1());
	                if(orderDetail.getBillAddress2()!=null){billAddress.append(", "+orderDetail.getBillAddress2());}
	                billAddress.append("<br/>"+orderDetail.getBillCity()+", "+orderDetail.getBillState()+" "+orderDetail.getBillZipCode()+"<br />");
	                //billAddress.append(orderDetail.getBillCountry()+"<br />");
	                if(orderDetail.getShipPhone()!=null){ billAddress.append("Ph: " + orderDetail.getBillPhone());}
	                
	                shippAddress.append(orderDetail.getShipAddress1());
	                if(orderDetail.getShipAddress2()!=null){shippAddress.append("<br/>"+orderDetail.getShipAddress2());}
	                shippAddress.append("<br />"+orderDetail.getShipCity()+", "+orderDetail.getShipState()+" "+orderDetail.getShipZipCode()+"<br />");
	                if(orderDetail.getShipCountry()!=null){shippAddress.append(orderDetail.getShipCountry()+"<br />");}
	                if(orderDetail.getShipPhone()!=null){shippAddress.append("Ph: " + orderDetail.getShipPhone());}
            	}
            
            context.put("orderType", "Order");
            if(CommonUtility.validateString(sendMailModel.getMailSubject()).toUpperCase().contains("QUOTE")){
            	context.put("orderType", "QUOTE");
            }
            context.put("subject", sendMailModel.getMailSubject());
            context.put("salesOrderNumber", orderDetail.getOrderNum());

            if(CommonDBQuery.getSystemParamtersList().get("ERP_DISPLAY_NAME")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP_DISPLAY_NAME").trim().equalsIgnoreCase("Default")){
            	erpDisplayName = CommonDBQuery.getSystemParamtersList().get("ERP_DISPLAY_NAME");
 	            if(CommonUtility.validateString(orderDetail.getExternalSystemId()).length()>0){
	            	context.put("externalSystemId", CommonUtility.validateString(orderDetail.getExternalSystemId()));
	            }else{
	            	context.put("externalSystemId", "");
	            }
            }
            context.put("externalSystemId", CommonUtility.validateString(orderDetail.getExternalSystemId()));
            
            if(orderDetail.getPaymentMethod()!=null && orderDetail.getPaymentMethod().trim().length()>0){
            	 context.put("paymentMethod",orderDetail.getPaymentMethod().trim());
            	 if(orderDetail.getPaymentMethod().trim().equalsIgnoreCase("Credit Card")){
            		 if(orderDetail.getTransactionId()!=null && orderDetail.getTransactionId().trim().length()>0){
            			 context.put("transactionId",orderDetail.getTransactionId());
            		 }else{
            			 context.put("transactionId","N/A");
            		 }
            	 }
            }
            UsersModel salesRepIn = (UsersModel) session.getAttribute("salesRepIn");
			if(salesRepIn!=null){
				context.put("insideSalesRepEmail", salesRepIn.getEmailAddress());
			}
			UsersModel salesRepOut = (UsersModel)session.getAttribute("salesRepOut");
			if(salesRepOut!=null){
				context.put("outsideSalesRepEmail", salesRepOut.getEmailAddress());
			}
			if(session.getAttribute("selectedshipToIdSx")!=null){
	            context.put("selectedshipToIdSx",session.getAttribute("selectedshipToIdSx").toString());
	        }
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SAVE_LEADTIME_ON_ORDER_CONFIRMATION")).equalsIgnoreCase("Y")){
				for(SalesModel eachItem:orderItemList){
					if(!CommonUtility.validateString(eachItem.getLeadTime()).equalsIgnoreCase("-1")){
						DateFormat df = new SimpleDateFormat("EEE MM/dd/yyyy");
						int leadTime = CommonUtility.validateNumber(eachItem.getLeadTime());
						Calendar c = Calendar.getInstance();
						if(leadTime>0){
							c.add(Calendar.DATE, leadTime);	
						}
						int hourofDay = c.get(Calendar.HOUR_OF_DAY);
						if(hourofDay>15){
							c.add(Calendar.DATE, 1);	
						}
						int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
						if(dayOfWeek==Calendar.FRIDAY){
							c.add(Calendar.DATE, 3);
						}else if(dayOfWeek==Calendar.SATURDAY){
							c.add(Calendar.DATE,2);
						}else if(dayOfWeek==Calendar.SUNDAY){
							c.add(Calendar.DATE,1);
						}
						eachItem.setLeadTime(df.format(c.getTime()));
					}
				}
			}
			context.put("orderDetail",orderDetail);
			context.put("customFieldValueObject", orderDetail.getCustomFieldVal());
			context.put("billingAddressObject", orderDetail.getBillAddress());
            context.put("shippingAddressObject", orderDetail.getShipAddress());
            context.put("erpDisplayName", erpDisplayName);
            context.put("orderedBy", orderDetail.getOrderedBy());
            context.put("poNumber", orderDetail.getPoNumber());
            context.put("reqDate", orderDetail.getReqDate());
            context.put("shipMethod", orderDetail.getShipMethod());
            context.put("shipViaDisplayName", orderDetail.getShipViaMethod());
            context.put("billAddressFromModel", billAddressFromModel.toString());
            context.put("shippAddressFromModel", shippAddressFromModel.toString());
            context.put("shippingInstruction", orderDetail.getShippingInstruction());
            context.put("orderNotes", orderDetail.getOrderNotes());
            context.put("orderStatus", "Bid");
            context.put("billAddress", billAddress.toString());
            context.put("shipAddress", shippAddress.toString());
            context.put("lineItemList", orderItemList);
            context.put("orderTotal", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTotal()))));
            context.put("tax", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTax()))));
            context.put("subTotal", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getSubtotal()))));
            context.put("freight", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getFreight()))));
            context.put("handling", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getHandling()))));
            context.put("deliveryFee", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getDeliveryCharge()))));
            context.put("discount", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getDiscount()))));
            context.put("orderItemsDiscount", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getDiscount()))));
            context.put("totalSavings", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTotalSavingsOnOrder()))));
            context.put("appliedDiscountCoupons", appliedlDiscounts);
            context.put("date",dateFormat.format(date));
            context.put("customerReleaseNumber", orderDetail.getCustomerReleaseNumber());
            context.put("homeBranchName", orderDetail.getHomeBranchName());
            context.put("companyName", orderDetail.getBillAddress().getCompanyName()); 
            NumberTool numbrTool =  new NumberTool();
            context.put("numberTool", numbrTool); 
            context.put("escapeTool", new EscapeTool());
	    	context.put("math", new MathTool());
	    	context.put("dispalyTool", new DisplayTool());
	    	context.put("dateTool", new ComparisonDateTool());
	    	context.put(Integer.class.getSimpleName(), Integer.class);
	    	context.put("session", session);
            if(errDesc!=null && errDesc.trim().contains("successfully")){
            	context.put("isErrDesc", "No");
            	context.put("status", errDesc);
            }else if(CommonUtility.validateString(errDesc).toUpperCase().contains("DUPLICATE")){
            	context.put("isErrDesc", "Yes");
            	context.put("status", errDesc);
            	context.put("duplicatePOStatus",propertyLoader("duplicate.status.po",""));
            	if(CommonUtility.validateString(propertyLoader("duplicate.status.po.subject","")).length()>0){
            		sendMailModel.setMailSubject(propertyLoader("duplicate.status.po.subject",""));
            	}
            }else if(errDesc!=null && !errDesc.trim().toUpperCase().contains("ERROR")){
            	context.put("isErrDesc", "No");
            	context.put("status", errDesc);
            }else{
            	context.put("isErrDesc", "Yes");
            	context.put("status", "Order Failed.");
            }
            
            context.put("containSubAcc",CommonUtility.validateString((String) session.getAttribute("containSubAcc"))!=null?session.getAttribute("containSubAcc"):"");
            context.put("errDesc", errDesc);
            context.put("firstName", sendMailModel.getFirstName());
            context.put("lastName", lastName);
            context.put("userName", sendMailModel.getUserName());
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            String orderSuffux = "";
			if(CommonUtility.validateParseIntegerToString(orderDetail.getOrderSuffix()).length()>0){
				orderSuffux = "- 0"+CommonUtility.validateParseIntegerToString(orderDetail.getOrderSuffix());
				context.put("orderSuffux", orderSuffux);
			}
			
			if(notificationDetail!=null && CommonUtility.validateString(notificationDetail.get("DESCRIPTION")).length()>0){
			
		            StringWriter writer = new StringWriter();
		            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
		            
		            StringBuilder finalMessage= new StringBuilder();
		            finalMessage.append(writer.toString());
		
					String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
					
					if(notificationDetail.get("FROM_EMAIL")!=null){
						fromEmail = notificationDetail.get("FROM_EMAIL");
					}
					
					
					if(session!=null && session.getAttribute("wareHouseEmailID")!=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_WAREHOUSE_SPECIFIC_ORDER_MAIL")).equalsIgnoreCase("Y")){
						String toFromNotification = notificationDetail.get("TO_EMAIL");
						 String toEmailIdWarehouse = (String)session.getAttribute("wareHouseEmailID");
						if(toFromNotification!=null && toFromNotification.trim().length()>0 && !toFromNotification.trim().equalsIgnoreCase("null")){
							notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+toEmailIdWarehouse);
						}else{
							//notificationDetail.put("TO_EMAIL", toEmailIdWarehouse);
							notificationDetail.put("TO_EMAIL", toEmailIdWarehouse+";"+toEmailId);
						}
					}else if(CommonUtility.validateString(toEmailId).length()>0 && !CommonUtility.validateString(toEmailId).equalsIgnoreCase("null")){
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ORDER_MAIL_TO_USER_ONLY")).equalsIgnoreCase("Y")){
							toEmailId = CommonUtility.validateString(toEmailId);// Only user who is placing order
						}else{
							if(orderDetail!= null && orderDetail.getShipAddress().getEmailAddress()!=null && orderDetail.getShipAddress().getEmailAddress().trim().length()>0 && !toEmailId.trim().equalsIgnoreCase(orderDetail.getShipAddress().getEmailAddress())){
								toEmailId = toEmailId+";"+orderDetail.getShipAddress().getEmailAddress().trim();
				            }else if(orderDetail!= null && orderDetail.getShipEmailAddress()!=null && orderDetail.getShipEmailAddress().trim().length() > 0 && !toEmailId.trim().equalsIgnoreCase(orderDetail.getShipEmailAddress())){
				            	toEmailId = toEmailId+";"+orderDetail.getShipEmailAddress().trim();
				            }
						}
						String toFromNotification = notificationDetail.get("TO_EMAIL");
						if(toFromNotification!=null && toFromNotification.trim().length()>0 && !toFromNotification.trim().equalsIgnoreCase("null")){
							notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+toEmailId);
						}else{
							notificationDetail.put("TO_EMAIL", toEmailId);
						}
					}
					
					if(orderMailAddressCustomerWise!=null && orderMailAddressCustomerWise.trim().length()>0 && !orderMailAddressCustomerWise.trim().equalsIgnoreCase("null")){
						String bccFromNotification = notificationDetail.get("BCC_EMAIL");
						if(bccFromNotification!=null && bccFromNotification.trim().length()>0 && !bccFromNotification.trim().equalsIgnoreCase("null")){
							notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL")+";"+orderMailAddressCustomerWise);
						}else{
							notificationDetail.put("BCC_EMAIL", orderMailAddressCustomerWise);
						}
						
					}
					
					if(CommonUtility.validateString(orderDetail.getSendmailToSalesRepOnly()).equalsIgnoreCase("Y")){
						if(CommonUtility.validateString(UsersDAO.getUserCustomField(sendMailModel.getUserId(), "IN_SIDE_SALES_REP")).length()>0){
							notificationDetail.put("TO_EMAIL",toEmailId);
							notificationDetail.put("CC_EMAIL",UsersDAO.getUserCustomField(sendMailModel.getUserId(), "IN_SIDE_SALES_REP"));
						}
					}
					//notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL"):""));
					//notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:""));
					String orderMailSubject = sendMailModel.getMailSubject();
					if(orderDetail!= null && orderDetail.getExternalSystemId()!=null && CommonUtility.validateString(orderDetail.getExternalSystemId()).length()>0){
						orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject+" - #"+orderDetail.getExternalSystemId();
					}else{
						if(CommonUtility.validateString((String) session.getAttribute("erpType")).equalsIgnoreCase("DEFAULTS")){
							orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject +" - #"+ orderDetail.getOrderNum();
						}else {
							orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+" - "+(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("sentmailconfig.orderFailMail.subject")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("sentmailconfig.orderFailMail.subject") :"Order Failure");
						}
					}
					
					if(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("order.confirmation.subject.override")!=null) {
						orderMailSubject = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("order.confirmation.subject");
					}
					
					flag = sendNotification(notificationDetail, orderMailSubject, fromEmail, finalMessage.toString());
					
					String toMailList = (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId;
					String bccMailList = (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:"");
					
					String mailStatus = "N";
					if(flag){
						mailStatus = "Y";
					}
					
					
					//String toAddress, String emailSubject,String emailFrom, StringBuilder emailMessage,String sentMailStatus,String emailType,String bcc
					boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, orderMailSubject, fromEmail, finalMessage, mailStatus, sendMailModel.getMailSubject(),bccMailList);
					
					System.out.println("Your Save Email in DB Status @ Order Confirmation Mail : "+saveMailStatus);
	    }
			
			
        }catch(Exception e){
        	e.printStackTrace();
        }
	    
       	return flag;	
	}
	
	public boolean sendProductMail(String toName,String toEmailId,String subject,String fromName,String fromEmailId,String mailBody,String descPart,String pricePart,String contentPart,String imgPart,String mailLink){
		boolean flag = false;
	    HttpServletRequest request =ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String SiteImgPath = (String) session.getAttribute("siteImagePath");
	    
        System.out.println("mailBody \n"+mailBody);
        System.out.println("descPart \n"+descPart);
        System.out.println("contentPart \n"+contentPart);
        System.out.println("imgPart \n"+imgPart);
        System.out.println("pricePart \n"+pricePart);
        
        
	    try{
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("ProductPageMail");
        	
        	VelocityContext context = new VelocityContext();
        	
            context.put("toName", toName);
            context.put("fromName", fromName);
            context.put("fromEmail", fromEmailId);
            context.put("mailBody", mailBody);
            context.put("descPart", descPart);
            context.put("pricePart", pricePart);
            context.put("contentPart", contentPart);
            context.put("mailLink", mailLink);
            context.put("imgPart", imgPart);
            if(SiteImgPath=="MIN_"){
      		  	context.put("siteLogoPath","MIN_images");
	      	}else{
	      		context.put("siteLogoPath","images");
	      	}
            
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());
            
            System.out.println("sendProductMail \n"+writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
			
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId);
			notificationDetail.put("CC_EMAIL", (notificationDetail.get("CC_EMAIL")!=null?notificationDetail.get("CC_EMAIL")+";":"")+fromEmailId);
			notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":""));
			
			flag = sendNotification(notificationDetail, subject , fromEmail, finalMessage.toString());
			
            
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
       return flag;
	}
	
	public boolean sendMailForAll(SendMailModel sendMailObj){
		boolean flag = false;
	    HttpServletRequest request =ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String SiteImgPath = (String) session.getAttribute("siteImagePath");
        String toName = "";
        String toEmailId = "";
        String subject = "";
        String fromName = "";
        String fromEmailId = "";
        String mailBody = "";
        String descPart = "";
        String pricePart = "";
        String contentPart = "";
        String imgPart = "";
        String mailTemplateName = "";
        String mailMessage = "";
	    try{
	    	
	    	if(sendMailObj!=null){
		    	toName = sendMailObj.getToName();
		    	toEmailId = sendMailObj.getToEmailId();
		    	subject = sendMailObj.getMailSubject();
		    	fromName = sendMailObj.getFromName();
		    	fromEmailId = sendMailObj.getFromEmailId();
		    	mailBody = sendMailObj.getMessageBody();
		    	descPart = sendMailObj.getDescPart();
		    	pricePart = sendMailObj.getPricePart();
		    	contentPart = sendMailObj.getContentPart();
		    	imgPart = sendMailObj.getImgPart();
		    	mailTemplateName = sendMailObj.getMailTemplateName();
		    	mailMessage = sendMailObj.getMailMessage();
	    	}
	    	
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails(mailTemplateName);
        	if(notificationDetail==null){
        		System.out.println("Yesss");
        	}
        	
        	VelocityContext context = new VelocityContext();
        	context.put("toName", toName);
            context.put("fromName", fromName);
            context.put("fromEmail", fromEmailId);
            context.put("mailBody", mailBody);
            context.put("descPart", descPart);
            context.put("pricePart", pricePart);
            context.put("contentPart", contentPart);
            context.put("imgPart", imgPart);
            context.put("mailMessage",mailMessage);
            if(SiteImgPath=="MIN_"){
      		  	context.put("siteLogoPath","MIN_images");
	      	}else{
	      		context.put("siteLogoPath","images");
	      	}
            
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            StringWriter writer = new StringWriter();
            
            if(notificationDetail!=null && notificationDetail.get("DESCRIPTION")==null){
            	notificationDetail.put("DESCRIPTION",contentPart);
            }
            if(notificationDetail!=null) {
            	Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            }
            
            StringBuilder finalMessage = new StringBuilder();
            finalMessage.append(writer.toString());
			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
			if(notificationDetail!=null && notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			if(notificationDetail!=null)
			{
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId);
			notificationDetail.put("CC_EMAIL", (notificationDetail.get("CC_EMAIL")!=null?notificationDetail.get("CC_EMAIL")+";":"")+fromEmailId);
			notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":""));
			flag = sendNotification(notificationDetail, subject , fromEmail, finalMessage.toString());
			}
        }
        catch(Exception e){
        	e.printStackTrace();
        }
       return flag;
	}
	
	//Mail Builder
	public boolean sendRFQConfirmationMail(SalesModel orderDetail, ArrayList<SalesModel> orderItemList,SendMailModel sendMailModel){
		
		HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    boolean flag = false;
	    String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
	    String erpDisplayName = null;
	    DecimalFormat df2 = new DecimalFormat("#,##0.00");
	    try{
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("RequestForQuote");
        	String orderMailAddressCustomerWise = UsersDAO.getCustomerCustomTextFieldValue(CommonUtility.validateNumber(buyingCompanyId),"ORDER_SUBMIT_EMAIL_ID");
        	
        	
            VelocityContext context = new VelocityContext();
          
            String toEmailId = sendMailModel.getToEmailId();
            String lastName = "";
            if(sendMailModel.getLastName()!=null && sendMailModel.getLastName().trim().length() > 0){
            	lastName = sendMailModel.getLastName().trim();
            }else{
            	lastName = "";
            }
            
            
            StringBuilder shippAddress = new StringBuilder();
            StringBuilder billAddress = new StringBuilder();
            String errDesc = orderDetail.getOrderStatusDesc();
            StringBuilder shippAddressFromModel = new StringBuilder();
            StringBuilder billAddressFromModel = new StringBuilder();
            
            if(errDesc==null)
            	errDesc = "RFQ Failed.";
            if(orderDetail.getBillAddress().getShipToId()!=null && orderDetail.getBillAddress().getShipToId().trim().length() > 0){
            	billAddressFromModel.append(orderDetail.getBillAddress().getShipToId() + "-");
            }
            if(orderDetail.getBillAddress().getFirstName()!=null && orderDetail.getBillAddress().getFirstName().trim().length() > 0){
            	billAddressFromModel.append(orderDetail.getBillAddress().getFirstName());
            }
            if(orderDetail.getBillAddress().getLastName()!=null && orderDetail.getBillAddress().getLastName().trim().length() > 0){
            	billAddressFromModel.append(" "+orderDetail.getBillAddress().getLastName()+"<br />");
            }else{
            	billAddressFromModel.append("<br />");
            }
            	
            billAddressFromModel.append(orderDetail.getBillAddress().getAddress1());
            if(orderDetail.getBillAddress().getAddress2()!=null){
            	billAddressFromModel.append(", "+orderDetail.getBillAddress().getAddress2());
            }
            billAddressFromModel.append("<br/>"+orderDetail.getBillAddress().getCity()+", "+orderDetail.getBillAddress().getState()+" "+orderDetail.getBillAddress().getZipCode()+"<br />");
            //billAddress.append(orderDetail.getBillCountry()+"<br />");
            if(orderDetail.getBillAddress().getPhoneNo()!=null){ billAddressFromModel.append("Ph: " + orderDetail.getBillAddress().getPhoneNo());}
            
            if(orderDetail.getShipAddress().getShipToId()!=null && orderDetail.getShipAddress().getShipToId().trim().length() > 0){
            	shippAddressFromModel.append(orderDetail.getShipAddress().getShipToId() + "-");
            }
            if(orderDetail.getShipAddress().getFirstName()!=null && orderDetail.getShipAddress().getFirstName().trim().length() > 0){
            	shippAddressFromModel.append(orderDetail.getShipAddress().getFirstName());
            }
            if(orderDetail.getShipAddress().getLastName()!=null && orderDetail.getShipAddress().getLastName().trim().length() > 0){
            	shippAddressFromModel.append(" "+orderDetail.getShipAddress().getLastName()+"<br />");
            }else{
            	shippAddressFromModel.append("<br />");
            }
            shippAddressFromModel.append(orderDetail.getShipAddress().getAddress1());
            if(orderDetail.getShipAddress().getAddress2()!=null){shippAddressFromModel.append(", "+orderDetail.getShipAddress().getAddress2());}
            shippAddressFromModel.append("<br />"+orderDetail.getShipAddress().getCity()+", "+orderDetail.getShipAddress().getState()+" "+orderDetail.getShipAddress().getZipCode()+"<br />");
            //shippAddress.append(orderDetail.getShipCountry()+"<br />");
            if(orderDetail.getShipAddress().getPhoneNo()!=null){shippAddressFromModel.append("Ph: " + orderDetail.getShipAddress().getPhoneNo());}
            
            
            billAddress.append(orderDetail.getBillAddress1());
            if(orderDetail.getBillAddress2()!=null){
            	billAddress.append(", "+orderDetail.getBillAddress2());
            }
            
            billAddress.append("<br/>"+orderDetail.getBillCity()+"<br />");
            billAddress.append(orderDetail.getBillState()+", "+orderDetail.getBillZipCode()+"<br />");
            billAddress.append(orderDetail.getBillCountry()+"<br />");
            if(orderDetail.getShipPhone()!=null)
            	 billAddress.append("Ph: " + orderDetail.getBillPhone());
            
            shippAddress.append(orderDetail.getShipAddress1());
            if(orderDetail.getShipAddress2()!=null){
            	shippAddress.append(", "+orderDetail.getShipAddress2());
            }
            shippAddress.append("<br />"+orderDetail.getShipCity()+"<br />");
            shippAddress.append(orderDetail.getShipState()+", "+orderDetail.getShipZipCode()+"<br />");
            shippAddress.append(orderDetail.getShipCountry()+"<br />");
            if(orderDetail.getShipPhone()!=null)
            	shippAddress.append("Ph: " + orderDetail.getShipPhone());
            
            context.put("subject", sendMailModel.getMailSubject());
            context.put("salesOrderNumber", orderDetail.getOrderNum());
           
            if(CommonDBQuery.getSystemParamtersList().get("ERP_DISPLAY_NAME")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP_DISPLAY_NAME").trim().equalsIgnoreCase("Default")){
            	erpDisplayName = CommonDBQuery.getSystemParamtersList().get("ERP_DISPLAY_NAME");
                
	            if(orderDetail.getExternalSystemId()!=null){
	            	context.put("externalSystemId", orderDetail.getExternalSystemId());
	            }else{
	            	context.put("externalSystemId", "");
	            }
            }
            context.put("erpDisplayName", erpDisplayName);
            context.put("email",toEmailId);
            context.put("orderDetail",orderDetail);
            context.put("phone",orderDetail.getShipPhone());
            context.put("orderStatus", "Bid");
            context.put("billAddress", billAddress.toString());
            context.put("shipAddress", shippAddress.toString());
            context.put("lineItemList", orderItemList);
            context.put("itemList", orderItemList);
            context.put("orderTotal", df2.format(orderDetail.getTotal()));
            context.put("tax", df2.format(orderDetail.getTax()));
            context.put("subTotal", df2.format(orderDetail.getSubtotal()));
            context.put("freight", df2.format(orderDetail.getFreight()));
            context.put("handling", df2.format(orderDetail.getHandling()));
            context.put("discount", df2.format(orderDetail.getDiscount()));
            context.put("comment", orderDetail.getUserNote());
            context.put("reqDate", orderDetail.getReqDate());
            context.put("billAddressFromModel", billAddressFromModel.toString());
            context.put("shippAddressFromModel", shippAddressFromModel.toString());

            if(errDesc!=null && errDesc.trim().contains("successfully")){
            	context.put("isErrDesc", "No");
            	context.put("status", errDesc);
            }else if(errDesc!=null && !errDesc.trim().toUpperCase().contains("ERROR")){
            	context.put("isErrDesc", "No");
            	context.put("status", errDesc);
            }else{
            	context.put("isErrDesc", "Yes");
            	context.put("status", "Order Failed.");
            }
            context.put("errDesc", errDesc);
            
            context.put("firstName", sendMailModel.getFirstName());
            context.put("lastName", lastName);
            context.put("userName", sendMailModel.getUserName());
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
			
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId);
			notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:""));
			
			String orderMailSubject = sendMailModel.getMailSubject();
			if(orderDetail!= null && orderDetail.getExternalSystemId()!=null && CommonUtility.validateString(orderDetail.getExternalSystemId()).length()>0){
				orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject+" - #"+orderDetail.getExternalSystemId();
			}else{
				if(CommonUtility.validateString((String) session.getAttribute("erpType")).equalsIgnoreCase("DEFAULTS")){
					orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject +" - #"+ orderDetail.getOrderNum();
				}else {
					orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+" - "+(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("sentmailconfig.orderFailMail.subject")!=null?LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("sentmailconfig.orderFailMail.subject") :"Order Failure");
				}
			}
			
			flag = sendNotification(notificationDetail, orderMailSubject, fromEmail, finalMessage.toString());
			
			String toMailList = (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId;
			String bccMailList = (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:"");
			
			String mailStatus = "N";
			if(flag){
				mailStatus = "Y";
			}
			//String toAddress, String emailSubject,String emailFrom, StringBuilder emailMessage,String sentMailStatus,String emailType,String bcc
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, sendMailModel.getMailSubject()+" - #"+orderDetail.getOrderNum(), fromEmail, finalMessage, mailStatus, sendMailModel.getMailSubject(),bccMailList);
			System.out.println("Your Save Email in DB Status @ buildChangePasswordMail : "+saveMailStatus);
        }catch(Exception e){
        	e.printStackTrace();
        }
       	return flag;	
	}
	
	public boolean sendMailToApprover(int approveUserId,int userId,String gUser){
		boolean flag = false;
		Connection  conn = null;
		 try {
		        conn = ConnectionManager.getDBConnection();
		    } catch (SQLException e) { 
		        e.printStackTrace();
		    }	
		try{
		UsersModel approverMail = UsersDAO.getUserEmail(approveUserId);

		
		HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
		String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("Approvemailid");
    	String orderMailAddressCustomerWise = UsersDAO.getCustomerCustomTextFieldValue(CommonUtility.validateNumber(buyingCompanyId),"ORDER_SUBMIT_EMAIL_ID");

        VelocityContext context = new VelocityContext();
		ProductsDAO.deleteFromCart(conn, userId);
		if(approverMail!=null){
			context.put("firstName", CommonUtility.validateString(approverMail.getFirstName()));
			context.put("lastName", CommonUtility.validateString(approverMail.getLastName()));
		}
		context.put("gUser", gUser);
		context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
        context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
        context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
        context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
        if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
        	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
        }
        
        
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
        
        StringBuilder finalMessage= new StringBuilder();
        finalMessage.append(writer.toString());

		String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
		//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
		
		if(notificationDetail.get("FROM_EMAIL")!=null){
			fromEmail = notificationDetail.get("FROM_EMAIL");
		}
		
		notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+approverMail.getEmailAddress());
		notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:""));
		
		flag = sendNotification(notificationDetail,propertyLoader("sentmailconfig.sendMailToApprover.subject",""), fromEmail, finalMessage.toString());
		
		String toMailList = (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+approverMail.getEmailAddress();
		String bccMailList = (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:"");
		
		String mailStatus = "N";
		if(flag){
			mailStatus = "Y";
		}
		//String toAddress, String emailSubject,String emailFrom, StringBuilder emailMessage,String sentMailStatus,String emailType,String bcc
		boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.sendMailToApprover.subject",""), fromEmail, finalMessage, mailStatus, "Approval Mail",bccMailList);
		System.out.println("Your Save Email in DB Status @ sendMailToApprover : "+saveMailStatus);
  
		}catch(Exception e){
			e.printStackTrace();
		}
		 finally{
	    	ConnectionManager.closeDBConnection(conn);
        }
		return flag;
	}
	
	public static int sendRFQMail(int orderId,int userId,int type, String emailAddress, String comments)
	{

		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;

		boolean flag = false;
		int sent =0;
		String sql = "";
		double salesOrderTotal = 0d;
		try {
			SalesModel salesOrderDetailList = new SalesModel();
			int punchoutUser = 0;
			DecimalFormat df2;
			String pricePrecision = CommonUtility.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision"));
			if(pricePrecision.equalsIgnoreCase("2")){
			df2 = new DecimalFormat( "####0.00" );
			}else{
			df2 = new DecimalFormat( "####0.0000" );
			}
			ArrayList<SalesModel> salesOrderItem = new ArrayList<SalesModel>();
		
			 conn = ConnectionManager.getDBConnection();
			 sql = PropertyAction.SqlContainer.get("getOrderDetail");
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, orderId);
				pstmt.setInt(2, userId);
				rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					SalesModel salesOrderDetail = new SalesModel();
					salesOrderDetail.setOrderNum(rs.getString("ORDER_NUMBER"));
					salesOrderDetail.setErpOrderNumber(rs.getString("EXTERNAL_SYSTEM_ID"));
					salesOrderDetail.setBillAddress1(rs.getString("BILL_ADDRESS1"));
					salesOrderDetail.setBillAddress2(rs.getString("BILL_ADDRESS2"));
					salesOrderDetail.setBillCity(rs.getString("BILL_CITY"));
					salesOrderDetail.setBillCountry(rs.getString("BILL_COUNTRY_CODE"));
					salesOrderDetail.setBillPhone(rs.getString("BILL_PHONE"));
					salesOrderDetail.setBillState(rs.getString("BILL_STATE"));
					salesOrderDetail.setBillZipCode(rs.getString("BILL_ZIP_CODE"));
					salesOrderDetail.setShipAddress1(rs.getString("SHIP_ADDRESS1"));
					salesOrderDetail.setShipAddress2(rs.getString("SHIP_ADDRESS2"));
					salesOrderDetail.setShipCity(rs.getString("SHIP_CITY"));
					salesOrderDetail.setShipCountry(rs.getString("SHIP_COUNTRY_CODE"));
					salesOrderDetail.setShipPhone(rs.getString("SHIP_PHONE"));
					salesOrderDetail.setShipState(rs.getString("SHIP_STATE"));
					salesOrderDetail.setShipZipCode(rs.getString("SHIP_ZIP_CODE"));
					salesOrderDetail.setTax(rs.getDouble("TAX_AMOUNT"));
					salesOrderDetail.setSubtotal(rs.getDouble("SUBTOTAL_AMOUNT"));
					salesOrderDetail.setFreight(rs.getDouble("FREIGHT"));
					salesOrderDetail.setHandling(rs.getDouble("HANDLING_FEE"));
					salesOrderDetail.setDiscount(rs.getDouble("CASHDISCOUNT_AMOUNT"));
					salesOrderDetail.setTotal(rs.getDouble("TOTAL_AMOUNT"));
					salesOrderDetail.setOrderStatusDesc(rs.getString("EXTERNAL_SYSTEM_ERROR"));
					salesOrderDetail.setPoNumber(rs.getString("PURCHASE_ORDER_NUMBER"));

					AddressModel addressModel = new AddressModel();
					addressModel.setFirstName(rs.getString("BILL_FIRST_NAME"));
					addressModel.setLastName(rs.getString("BILL_LAST_NAME"));
					addressModel.setAddress1(rs.getString("BILL_ADDRESS1"));
					addressModel.setAddress2(rs.getString("BILL_ADDRESS2"));
					addressModel.setCity(rs.getString("BILL_CITY"));
					addressModel.setCountry(rs.getString("BILL_COUNTRY_CODE"));
					addressModel.setPhoneNo(rs.getString("BILL_PHONE"));
					addressModel.setState(rs.getString("BILL_STATE"));
					addressModel.setZipCode(rs.getString("BILL_ZIP_CODE"));
					addressModel.setCompanyName(rs.getString("CUSTOMER_NAME"));
					addressModel.setShipToId(rs.getString("BILL_SHIP_TO_ID"));
					salesOrderDetail.setBillAddress(addressModel);
					
					addressModel = new AddressModel();
					addressModel.setFirstName(rs.getString("SHIP_FIRST_NAME"));
					addressModel.setLastName(rs.getString("SHIP_LAST_NAME"));
					addressModel.setAddress1(rs.getString("SHIP_ADDRESS1"));
					addressModel.setAddress2(rs.getString("SHIP_ADDRESS2"));
					addressModel.setCity(rs.getString("SHIP_CITY"));
					addressModel.setCountry(rs.getString("SHIP_COUNTRY_CODE"));
					addressModel.setPhoneNo(rs.getString("SHIP_PHONE"));
					addressModel.setState(rs.getString("SHIP_STATE"));
					addressModel.setZipCode(rs.getString("SHIP_ZIP_CODE"));
					addressModel.setShipToId(rs.getString("SHIPPING_SHIP_TO_ID"));
					if(rs.findColumn("SHIP_COMPANY_NAME")>0){
						addressModel.setCompanyName(rs.getString("SHIP_COMPANY_NAME"));	
					}
					salesOrderDetail.setShipAddress(addressModel);				
					
					salesOrderDetailList = salesOrderDetail;
					flag = true;
				}
				//ITEM_ID,PART_NUMBER,SHORT_DESC,QTY,PRICE,UOM, EXTPRICE
				if(flag)
				{
					rs.close();
					pstmt.close();
					sql = PropertyAction.SqlContainer.get("getOrderItemDetail");
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom")).length()>0){
						pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("getOrderItemDetailCustom"))));
						pstmt.setInt(1,CommonDBQuery.getGlobalSiteId());
						pstmt.setInt(2, orderId);
					}else{
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, orderId);
					}
					rs = pstmt.executeQuery();
					
					while(rs.next())
					{
						SalesModel salesOrderDetail = new SalesModel();
						salesOrderDetail.setItemId(rs.getInt("ITEM_ID"));
						salesOrderDetail.setPartNumber(rs.getString("PART_NUMBER"));
						salesOrderDetail.setShortDesc(rs.getString("SHORT_DESC"));
						salesOrderDetail.setOrderQty(rs.getInt("QTY"));
						salesOrderDetail.setOrderPrice(Double.parseDouble(df2.format(rs.getDouble("EXTPRICE"))));
						String UOM=salesOrderDetail.getOrderUom();
						if(UOM!=null && !UOM.trim().equalsIgnoreCase("")){
							salesOrderDetail.setOrderUom(rs.getString("UOM"));
						}else{
							salesOrderDetail.setOrderUom("|");	
						}
						salesOrderDetail.setQtyUom(rs.getString("PER_UOM"));
						salesOrderDetail.setErpQty(rs.getInt("PER_QTY"));
						salesOrderDetail.setTotal(Double.parseDouble(df2.format(rs.getDouble("EXTPRICE"))));
						salesOrderDetail.setImageName((rs.getString("IMAGE_NAME")==null)?"NoImage.png":rs.getString("IMAGE_NAME").toString().trim());
						salesOrderTotal = salesOrderTotal + rs.getDouble("EXTPRICE");
						salesOrderItem.add(salesOrderDetail);
						salesOrderDetailList.setOrderNotes(comments);
					}
					UsersModel userDetail = UsersDAO.getUserEmail(userId);
					if(type==1)
					{
						String userEmail = userDetail.getEmailAddress();
						if(emailAddress!=null)
						{
							userEmail = emailAddress;
							punchoutUser = 1;
						}
						
						sent= SalesDAO.sendConfirmationMail(orderId, userId, type, emailAddress, salesOrderDetailList);
					}
					else
						sent= SalesDAO.sendConfirmationMail(orderId, userId, type, emailAddress, salesOrderDetailList);
						
				}
				
				
		 } catch (SQLException e) { 
	            e.printStackTrace();
	        }	
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);
	        }
	        return sent;
	        
	}
	
	public boolean newUserMail(SendMailModel sendMailModel){		
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    String SiteImgPath = (String) session.getAttribute("siteImagePath");
	    boolean flag = false;
	    
	    //String userName, String userPassword,String emailId,String firstName,String lastName
	    
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        
        try{
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("RegisterNewPurchasingAgent");
        	//LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("NewUserRegistrationOne");//
        	//override
        	if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("purchaseagent.notificaion.name")).length()>0){
        	 notificationDetail = getNotificationDetail.getNotificationDetails(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("purchaseagent.notificaion.name"));
        	}
        	
            VelocityContext context = new VelocityContext();
          
            String toEmailId = sendMailModel.getToEmailId();
            String lastName = "";
            if(sendMailModel.getLastName()!=null && sendMailModel.getLastName().trim().length() > 0){
            	lastName = sendMailModel.getLastName().trim();
            }else{
            	lastName = "";
            }
            
            context.put("firstName", sendMailModel.getFirstName());
            context.put("lastName", lastName);
            context.put("userName", sendMailModel.getUserName());
            context.put("password", sendMailModel.getPassword());
            context.put("WelcomeText", "Good News! Your "+CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" account is setup and ready for use. Your new account may have been added by your company administrator or because of a request to "+CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(session.getAttribute("userFirstName")!=null){
            context.put("superUserName",session.getAttribute("userFirstName") +" "+ session.getAttribute("userLastName"));
            }
            if(session.getAttribute("customerId")!=null){
            	 context.put("superUserAccNo",session.getAttribute("customerId"));
            }
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId);
			notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
			
			flag = sendNotification(notificationDetail,propertyLoader("sentmailconfig.newUserMail.subject",""), fromEmail, finalMessage.toString());
			String toMailList = notificationDetail.get("TO_EMAIL")+";"+toEmailId;
			
			String mailStatus = "N";
			if(flag){
				mailStatus = "Y";
			}
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.newUserMail.subject",""), fromEmail, finalMessage, mailStatus, "Your New Account",null);
			System.out.println("Your Save Email in DB Status @ buildChangePasswordMail : "+saveMailStatus);
        }catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
			return flag;	
	}
	
	public boolean sendMultipleMailRecepient(Address[] toAddress,  String subject, String from, String body,Address[] ccAddress,Address[] bccAddress)
    {
		System.out.println("sending mail process :----");
         boolean result = false;
         String subject_data = subject;
         
         try {
        	 
        	 //String mailSmtp = CommonDBQuery.getSystemParamtersList().get("MAILSMTP");
        	 
        	 SecureData validUserPass = new SecureData();
        	 String emailRelayEncrpt = CommonDBQuery.getSystemParamtersList().get("EMAILRELAY");
  			 String emailRelay = validUserPass.validatePassword(emailRelayEncrpt);
  			 int emailRelayPort = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAILSMTPPORT"));
  			String clientHost = CommonDBQuery.getSystemParamtersList().get("CLIENTHOST");
  			if(emailRelayPort==0){
	        	 emailRelayPort = 25;
	         }
  			 
             Properties props = new Properties();
             props.put("mail.smtp.host", emailRelay);
             props.put("mail.smtp.auth", "false");
 			 props.put("mail.smtp.debug", "true");
 			 props.put("mail.smtp.port", emailRelayPort);
 			 if(clientHost!=null && clientHost.length()>0){
 		         props.put("mail.smtp.ehlo", true);
 		         props.put("mail.smtp.localhost", clientHost);
 		         }
 			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")).length()>0) {
                props.put("mail.smtp.ssl.trust", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")));
        	}
           	/**
 			 *Below code Written is for Tyndale *Reference- Chetan Sandesh
 			 */
           	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_FROMUSERNAME")).length()>0) {
           	props.put("mail.smtp.auth", "true");
           	props.put("mail.transport.protocol", "smtp");
           	props.put("mail.smtp.starttls.enable", "true");
           	}
             //Session session = Session.getDefaultInstance(props, auth);
             Authenticator auth = new SMTPAuthenticator();
             Session session = Session.getInstance(props, auth);
             MimeMessage message = new MimeMessage(session);
             message.setFrom(new InternetAddress(from));

             /*             String toAddress[] = new String[2];
             toAddress[0] = "shashikumar@unilogcorp.com";
             toAddress[1] = "vspkmaheshm@gmail.com";
             //Address[] toAddress= new Address[2];
            
             toAddress[0] = new InternetAddress("shashikumar@unilogcorp.com");
             toAddress[1] = new InternetAddress("vspkmaheshm@gmail.com");
             
             //toAddress[0] = ;
*/             
             message.addRecipients(Message.RecipientType.TO,toAddress );
             if(ccAddress!=null && ccAddress.length>0)
            	 message.addRecipients(Message.RecipientType.CC,ccAddress );
             
             if(bccAddress!=null && bccAddress.length>0)
            	 message.addRecipients(Message.RecipientType.BCC,bccAddress );
             
             message.setSubject(subject_data);
            
             Multipart mp = new MimeMultipart();

             MimeBodyPart messageBodyPart = new MimeBodyPart();
             messageBodyPart.setDataHandler(new DataHandler(new HTMLDataSource(body)));
             mp.addBodyPart(messageBodyPart);

             // Put parts in message
             message.setContent(mp);

             // Send the message
             Transport.send(message);
             
             result = true;
         } catch (Exception e) {
        	 e.printStackTrace();
           
         }
         return result;
    }

	public boolean sendRFQMailStandard(SendMailModel sendMailModel,ArrayList<ProductsModel> itemList, String reqDate, String comments){
		
		
	    boolean flag = false;
	    try{
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("RequestForQuoteMail");
        	
            VelocityContext context = new VelocityContext();
          
            String toEmailId = sendMailModel.getToEmailId();
            /*String lastName = "";
            if(sendMailModel.getLastName()!=null && sendMailModel.getLastName().trim().length() > 0){
            	lastName = sendMailModel.getLastName().trim();
            }else{
            	lastName = "";
            }*/
            Map<String, ProductsModel> requestForQuoteDetails = null;
             //CustomServiceProvider
            if(CommonUtility.customServiceUtility()!= null){
            	requestForQuoteDetails = CommonUtility.customServiceUtility().getRequestForQuoteDetails(itemList, sendMailModel.getSubsetId(), sendMailModel.getGeneralSubsetId());
            	context.put("requestForQuoteDetails", requestForQuoteDetails);
            }
            //CustomServiceProvider
            context.put("loginCustomerName", sendMailModel.getCustomerName());
            context.put("subject", sendMailModel.getMailSubject());
            context.put("reqDate", reqDate);
            context.put("comments", comments);
            context.put("userName", sendMailModel.getUserName());
            context.put("email", sendMailModel.getToEmailId());
            context.put("address1", sendMailModel.getAddress1());
            context.put("address2", sendMailModel.getAddress2());
            context.put("jobTitle", sendMailModel.getJobTitle());
            context.put("phone", sendMailModel.getPhone());
            context.put("itemList", itemList);
            context.put("rfqDetails", sendMailModel);
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
           
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());

            String fromEmail = null;
            //CustomServiceProvider
            if(CommonUtility.customServiceUtility()!= null){
            	fromEmail = CommonUtility.customServiceUtility().changeFromEmailaddress(sendMailModel);
            } 
            if(CommonUtility.validateString(fromEmail).isEmpty()){
            	 fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
    			//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
            }
            //CustomServiceProvider
			
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			
			
			if(toEmailId!=null && toEmailId.trim().length()>0 && !toEmailId.trim().equalsIgnoreCase("null")){
				String toFromNotification = notificationDetail.get("TO_EMAIL");
				if(toFromNotification!=null && toFromNotification.trim().length()>0 && !toFromNotification.trim().equalsIgnoreCase("null")){
					notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+toEmailId);
				}else{
					notificationDetail.put("TO_EMAIL", toEmailId);
				}
			}
			if(CommonUtility.validateString(sendMailModel.getBccEmailId()).length()>0){
				notificationDetail.put("BCC_EMAIL",sendMailModel.getBccEmailId()+notificationDetail.get("BCC_EMAIL"));
			}

			String toMailList = (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId;
			String bccMailList = (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"");
			
			//Send Email to Assigned Sales Rep only
			if(sendMailModel.isDefaultFalg()){
				if(CommonUtility.validateString(UsersDAO.getUserCustomField(sendMailModel.getUserId(), "IN_SIDE_SALES_REP")).length()>0){
					notificationDetail.put("TO_EMAIL",toEmailId);
					notificationDetail.put("CC_EMAIL",UsersDAO.getUserCustomField(sendMailModel.getUserId(), "IN_SIDE_SALES_REP"));
					toMailList= notificationDetail.get("TO_EMAIL");
				}else if(CommonDBQuery.getSystemParamtersList().get("sendmailToSalesRepOnly").equalsIgnoreCase("Y")) {
					notificationDetail.put("TO_EMAIL",sendMailModel.getAdditionalName());
				}
			}
			//Send Email to Assigned Sales Rep only
			Map<String, String> attachmentDetails = sendMailModel.getAttachmentDetails();
			if(attachmentDetails != null && attachmentDetails.size() > 0) {
				notificationDetail.put("SUBJECT", sendMailModel.getMailSubject());
				notificationDetail.put("FROM_MAIL_ADDRESS", fromEmail);
            	notificationDetail.put("EMAIL_CONTENT_TO_SEND", finalMessage.toString());
				flag = sendMailWithAttachment(notificationDetail, attachmentDetails.values().toArray(new String[0]), attachmentDetails.keySet().toArray(new String[0]));
			}else {
				flag = sendNotification(notificationDetail, sendMailModel.getMailSubject(), fromEmail, finalMessage.toString());
			}
			
			String mailStatus = "N";
			if(flag){
				mailStatus = "Y";
			}
			//String toAddress, String emailSubject,String emailFrom, StringBuilder emailMessage,String sentMailStatus,String emailType,String bcc
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, sendMailModel.getMailSubject(), fromEmail, finalMessage, mailStatus, sendMailModel.getMailSubject(),bccMailList);
			System.out.println("Your Save Email in DB Status @ buildChangePasswordMail : "+saveMailStatus);
        }catch(Exception e){
        	e.printStackTrace();
        }
       	return flag;	
	}

	public boolean sendEventMailToCoOrdinator(int id, EventDetails eventsDetails){
	boolean flag = false;
	ResultSet rs = null;
	Connection  conn = null;
	PreparedStatement pstmt = null;
	String toEmailId = "";
	String eventAddress = "";
	String coOrdinatorName = "";
	String date="";
	String time="";
	String startDate = "";
	String endDate = "";
	String startTime = "";
	String endTime = "";
	try{
		LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("EventsMail");

		VelocityContext context = new VelocityContext();

		conn = ConnectionManager.getDBConnection();
		String contactSQL = "SELECT START_DATE,END_DATE,ADDRESS,LOCATION,CONTACT,NOTIFICATION_EMAIL FROM EVENT_MANAGER WHERE EVENT_ID=?";
		pstmt = conn.prepareStatement(contactSQL);
		pstmt.setInt(1, id);
		rs = pstmt.executeQuery();
		while(rs.next()){
			coOrdinatorName = rs.getString("CONTACT");
			toEmailId =  rs.getString("NOTIFICATION_EMAIL");
			//eventAddress = rs.getString("ADDRESS");
			eventAddress = rs.getString("LOCATION");
			SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat sourceFormat = new SimpleDateFormat("MMMM dd, yyyy");
			SimpleDateFormat sourceFormat1 = new SimpleDateFormat("hh:mm aaa");
			startDate=sourceFormat.format(fromDB.parse(rs.getString("START_DATE")));
			endDate=sourceFormat.format(fromDB.parse(rs.getString("END_DATE")));
			startTime=sourceFormat1.format(fromDB.parse(rs.getString("START_DATE")));
			endTime=sourceFormat1.format(fromDB.parse(rs.getString("END_DATE")));
		}

		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);
		ConnectionManager.closeDBConnection(conn);	

		if(startDate!=null && endDate!=null && !endDate.equalsIgnoreCase(startDate)){
			date=startDate+" - "+endDate;
		}else{
			date=startDate;
		}

		time=startTime+" - "+endTime;
		Properties properties = new Properties();
		Velocity.init(properties);
		context.put("studFN", eventsDetails.getFirstName());
		context.put("studLN", eventsDetails.getLastName());
		context.put("company", eventsDetails.getCompanyName());
		context.put("paymentMode", eventsDetails.getPaymentMode());
		context.put("title", eventsDetails.getTitle());
		context.put("eveDate", date);
		context.put("eveTime", time);
		context.put("courseLocation", eventsDetails.getCourseLocation());
		context.put("eventAddress", eventAddress);
		context.put("CoordinatorName", coOrdinatorName);
		context.put("cost", eventsDetails.getCost());
		context.put("addStudFN",eventsDetails.getStudentFirstName());
		context.put("addStudLN",eventsDetails.getStudentLastName());
		context.put("addemail",eventsDetails.getStudentEmail());
		context.put("emailaddress",eventsDetails.getContactEmail());
		context.put("addcompany",eventsDetails.getStudentCompany());
		context.put("addphone",eventsDetails.getStudentPhone());
		context.put("phone",eventsDetails.getContactPhone());
		context.put("mobilephone",eventsDetails.getContactMobilePhone());
		context.put("customerAddress",eventsDetails.getAddress());
		context.put("eventsDetails",eventsDetails);
		context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
		context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
		context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
		context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
		NumberTool numbrTool =  new NumberTool();
        context.put("numberTool", numbrTool);
		if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
        	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
        }


		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));

		StringBuilder finalMessage= new StringBuilder();
		finalMessage.append(writer.toString());

		String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
		//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");

		if(notificationDetail.get("FROM_EMAIL")!=null){
			fromEmail = notificationDetail.get("FROM_EMAIL");
		}


		if(eventsDetails.getContactEmail()!=null && eventsDetails.getContactEmail().trim().length()>0 && !eventsDetails.getContactEmail().trim().equalsIgnoreCase("null")){
			String toFromNotification = notificationDetail.get("TO_EMAIL");
			if(toFromNotification!=null && toFromNotification.trim().length()>0 && !toFromNotification.trim().equalsIgnoreCase("null")){
				//notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+eventsDetails.getContactEmail()+";"+toEmailId);
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EVENTS_MAIL_TO_REGISTERER")).length()>0 && CommonDBQuery.getSystemParamtersList().get("EVENTS_MAIL_TO_REGISTERER").equalsIgnoreCase("Y")){
					notificationDetail.put("TO_EMAIL",notificationDetail.get("TO_EMAIL")+";"+toEmailId);
				}else{
					notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+eventsDetails.getContactEmail()+";"+toEmailId);
				}
			}else{
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EVENTS_MAIL_TO_REGISTERER")).length()>0 && CommonDBQuery.getSystemParamtersList().get("EVENTS_MAIL_TO_REGISTERER").equalsIgnoreCase("Y")){
					notificationDetail.put("TO_EMAIL",toEmailId);
				}else{
					notificationDetail.put("TO_EMAIL", eventsDetails.getContactEmail()+";"+toEmailId);
				}
			}
		}

		String subject = "Registration Submitted: "+eventsDetails.getTitle()+" | "+eventsDetails.getEventDate()+" | "+eventsDetails.getCourseLocation();
		if(CommonUtility.validateString(propertyLoader("event.registration.subject","")).length()>0){
			subject = propertyLoader("event.registration.subject","")+" "+eventsDetails.getTitle();
		}
		flag = sendNotification(notificationDetail, subject, fromEmail, finalMessage.toString());
		String toMailList = notificationDetail.get("TO_EMAIL");
		String bccMailList = (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"");

		String mailStatus = "N";
		if(flag){
			mailStatus = "Y";
		}
		//String toAddress, String emailSubject,String emailFrom, StringBuilder emailMessage,String sentMailStatus,String emailType,String bcc
		boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, "Registration Submitted: "+eventsDetails.getTitle()+" | "+eventsDetails.getEventDate()+" | "+eventsDetails.getCourseLocation(), fromEmail, finalMessage, mailStatus, "Event Registration",bccMailList);
		System.out.println("Your Save Email in DB Status @ sendEventMailToCoOrdinator : "+saveMailStatus);
	}catch(Exception e){
		e.printStackTrace();
	}finally {
		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);
		ConnectionManager.closeDBConnection(conn);	
	}
	return flag;
	}

	public boolean sendEventMailToRegisterer(int id, EventDetails eventsDetails){
	boolean flag = false;
	ResultSet rs = null;
	Connection  conn = null;
	PreparedStatement pstmt = null;
	//String toEmailId = "";
	String eventAddress = "";
	String coOrdinatorName = "";
	String date="";
	String time="";
	String startDate = "";
	String endDate = "";
	String startTime = "";
	String endTime = "";
	try{
		LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("EventMailParticipant");

		VelocityContext context = new VelocityContext();

		conn = ConnectionManager.getDBConnection();
		String contactSQL = "SELECT START_DATE,END_DATE,ADDRESS,CONTACT,NOTIFICATION_EMAIL FROM EVENT_MANAGER WHERE EVENT_ID=?";
		pstmt = conn.prepareStatement(contactSQL);
		pstmt.setInt(1, id);
		rs = pstmt.executeQuery();
		while(rs.next()){
			coOrdinatorName = rs.getString("CONTACT");
			//toEmailId =  rs.getString("NOTIFICATION_EMAIL");
			eventAddress = rs.getString("ADDRESS");

			SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat sourceFormat = new SimpleDateFormat("MMMM dd, yyyy");
			SimpleDateFormat sourceFormat1 = new SimpleDateFormat("hh:mm aaa");
			startDate=sourceFormat.format(fromDB.parse(rs.getString("START_DATE")));
			endDate=sourceFormat.format(fromDB.parse(rs.getString("END_DATE")));
			startTime=sourceFormat1.format(fromDB.parse(rs.getString("START_DATE")));
			endTime=sourceFormat1.format(fromDB.parse(rs.getString("END_DATE")));
		}

		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);
		ConnectionManager.closeDBConnection(conn);	

		if(startDate!=null && endDate!=null && !endDate.equalsIgnoreCase(startDate)){
			date=startDate+" - "+endDate;
		}else{
			date=startDate;
		}

		time=startTime+" - "+endTime;
		Properties properties = new Properties();
		Velocity.init(properties);
		context.put("studFN", eventsDetails.getFirstName());
		context.put("studLN", eventsDetails.getLastName());
		context.put("company", eventsDetails.getCompanyName());
		if(eventsDetails.getPaymentMode()!=null && eventsDetails.getPaymentMode().trim().equalsIgnoreCase("BillMe")){
			context.put("paymentMode", "Bill Me Later");
		}else if(eventsDetails.getPaymentMode()!=null && eventsDetails.getPaymentMode().trim().equalsIgnoreCase("PurchaseOrder")){
			context.put("paymentMode", "Purchase Order");
		}

		context.put("title", eventsDetails.getTitle());
		context.put("eveDate", date);
		context.put("eveTime", time);
		context.put("courseLocation", eventsDetails.getCourseLocation());
		context.put("eventAddress", eventAddress);
		context.put("CoordinatorName", coOrdinatorName);
		context.put("cost", eventsDetails.getCost());
		context.put("ContactPhone", eventsDetails.getContactMobilePhone());
		context.put("CompanyName", eventsDetails.getStudentCompany());
		context.put("CompanyAddress", eventsDetails.getAddress());
		context.put("CustomerEmail", eventsDetails.getStudentEmail());
		context.put("CustomerPhone", eventsDetails.getStudentPhone());
		context.put("eventsDetails", eventsDetails);
		NumberTool numbrTool =  new NumberTool();
        context.put("numberTool", numbrTool);

		context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
		context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
		context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
		context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
		if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
        	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
        }


		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));

		StringBuilder finalMessage= new StringBuilder();
		finalMessage.append(writer.toString());

		String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
		//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");

		if(notificationDetail.get("FROM_EMAIL")!=null){
			fromEmail = notificationDetail.get("FROM_EMAIL");
		}


		if(eventsDetails.getContactEmail()!=null && eventsDetails.getContactEmail().trim().length()>0 && !eventsDetails.getContactEmail().trim().equalsIgnoreCase("null")){
			String toFromNotification = notificationDetail.get("TO_EMAIL");
			if(toFromNotification!=null && toFromNotification.trim().length()>0 && !toFromNotification.trim().equalsIgnoreCase("null")){
				notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+eventsDetails.getContactEmail());
			}else{
				notificationDetail.put("TO_EMAIL", eventsDetails.getContactEmail());
			}
		}


		flag = sendNotification(notificationDetail, "Registration Submitted: "+eventsDetails.getTitle()+" | "+eventsDetails.getEventDate()+" | "+eventsDetails.getCourseLocation(), fromEmail, finalMessage.toString());

		String toMailList = notificationDetail.get("TO_EMAIL");
		String bccMailList = (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"");

		String mailStatus = "N";
		if(flag){
			mailStatus = "Y";
		}
		//String toAddress, String emailSubject,String emailFrom, StringBuilder emailMessage,String sentMailStatus,String emailType,String bcc
		boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, "Registration Submitted: "+eventsDetails.getTitle()+" | "+eventsDetails.getEventDate()+" | "+eventsDetails.getCourseLocation(), fromEmail, finalMessage, mailStatus, "Event Registration",bccMailList);
		System.out.println("Your Save Email in DB Status @ sendEventMailToCoOrdinator : "+saveMailStatus);
	}catch(Exception e){
		e.printStackTrace();
	}finally {
		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);
		ConnectionManager.closeDBConnection(conn);	
	}
	return flag;
	}
	
	public static boolean sendRegistrationNotification(LinkedHashMap<String, String> parameters) {
			LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("OnAccountRegistrationRequest");
			//HttpServletRequest request = ServletActionContext.getRequest();
		    //HttpSession session = request.getSession();
			SendMailUtility send = new SendMailUtility();
			boolean flag = false;
		    try {
		    	if(notificationDetail!=null && notificationDetail.size()>0){
			    	VelocityContext context = new VelocityContext();
			    	context.put("userDetails", parameters);
					context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
			        context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
			        context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			        context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
			        if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
		            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
		            }
			        
			        StringWriter writer = new StringWriter();
			        Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
			        
			        StringBuilder finalMessage= new StringBuilder();
			        finalMessage.append(writer.toString());
			        System.out.println(writer.toString());
					String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
					
					if(notificationDetail.get("FROM_EMAIL")!=null){
						fromEmail = notificationDetail.get("FROM_EMAIL");
					}
					
					if(parameters!=null && parameters.get("Email Address")!=null && parameters.get("Email Address").trim().length()>0 && !parameters.get("Email Address").trim().equalsIgnoreCase("null")){
						String toFromNotification = notificationDetail.get("TO_EMAIL");
						if(toFromNotification!=null && toFromNotification.trim().length()>0 && !toFromNotification.trim().equalsIgnoreCase("null")){
							notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+parameters.get("Email Address"));
						}else{
							notificationDetail.put("TO_EMAIL", parameters.get("Email Address"));
						}
					}
					
				    flag = send.sendNotification(notificationDetail, propertyLoader("register.label.onaccountregistration",""), fromEmail, finalMessage.toString());
				    
		    	}else{
		    		System.out.println("-------------- Note : OnAccountRegistrationRequest mail template Not Available in CIMM2 Notification");
		    	}
	    
	    } catch (ParseErrorException e) {
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
	
		return flag;
	}
	
	public static boolean sendOutstandingOrderPaymentMail(CreditCardModel creditCardValue,String userId){
		boolean flag = false;
	    HttpServletRequest request =ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        SendMailUtility send = new SendMailUtility();
           
        UsersModel userEmail = UsersDAO.getUserEmail(CommonUtility.validateNumber(userId));
        
	    try{
	    	//CPE
	    	UsersModel billAddress = new UsersModel();
	    	if(session.getAttribute("OutStandingOrderPaymentBillAddress")!=null){
	    		billAddress = (UsersModel)session.getAttribute("OutStandingOrderPaymentBillAddress");
	    	}
	    	UsersModel shipAddress = new UsersModel();
	    	if(session.getAttribute("OutStandingOrderPaymentShipAddress")!=null){
	    		shipAddress = (UsersModel)session.getAttribute("OutStandingOrderPaymentShipAddress");
	    	}
	    	ArrayList<SalesModel>orderDetailList = new ArrayList<SalesModel>();
	    	if(session.getAttribute("OutStandingOrderPaymentList")!=null){
	    		orderDetailList = (ArrayList<SalesModel>) session.getAttribute("OutStandingOrderPaymentList");
	    	}
	    	//CPE
	    	
	    	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("OutstandingOrderPayment");
        	
        	VelocityContext context = new VelocityContext();
        	context.put("creditCardValue", creditCardValue);
        	context.put("orderDetailList", orderDetailList);
        	context.put("billAddress", billAddress);
        	context.put("shipAddress", shipAddress);
        	context.put("firstName", userEmail.getFirstName());
			context.put("lastName", userEmail.getLastName());
			context.put("userEmailDetails", userEmail);
		 	context.put("contactId", session.getAttribute("contactId").toString());
			context.put("ccRefrenceCode", creditCardValue.getCreditCardRefNumber());
            //context.put("ccRefrenceCode", creditCardValue.getCreditCardTransactionID());
            context.put("ccApprovedAmount", creditCardValue.getCreditCardApprovedAmount());
            context.put("outStandingPaymentTotal", CommonUtility.validateString((String) session.getAttribute("outStandingPaymentTotal")));
            if(session.getAttribute("OutStandingOrderPaymentList")!=null)
            	context.put("outStandingOrderPaymentList",(ArrayList<SalesModel>) session.getAttribute("OutStandingOrderPaymentList"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
	        context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
	        context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
	        context.put("numberTool", new NumberTool());
	        
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());
            
            System.out.println("sendProductMail \n"+writer.toString()); 
            String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			String toEmail = userEmail.getEmailAddress();
			//notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":toEmail));
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmail);
			notificationDetail.put("CC_EMAIL", (notificationDetail.get("CC_EMAIL")!=null?notificationDetail.get("CC_EMAIL")+";":""));
			notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":""));
			
			flag = send.sendNotification(notificationDetail, propertyLoader("sentmailconfig.sendOutstandingOrderPaymentMail.subject","")  , fromEmail , finalMessage.toString());		
			//flag = send.sendNotification(notificationDetail, subject  , fromEmail , finalMessage.toString());
			
            
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
       return flag;
	}
	
	public boolean sendCreditApplicationRequestMail(UsersModel userDetailsAddresss, LinkedHashMap<String, Object>contentData){
		boolean flag = false;
		ResultSet rs = null;
	    Connection  conn = null;
	    String subject = "";
	    String to = "";
	    
	    try{
	    	HttpSession session =  (HttpSession) contentData.get("session");
	    	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails(CommonUtility.validateString(contentData.get("NotificationTemplateName").toString()));
        	if(notificationDetail!=null && notificationDetail.size()>0){
        	
		        	String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					if(notificationDetail!=null && notificationDetail.get("FROM_EMAIL")!=null){
						fromEmail = notificationDetail.get("FROM_EMAIL");
					}
					if(notificationDetail!=null) {
						notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
					}
					VelocityContext context = new VelocityContext();
					context.put("WelcomeText",CommonDBQuery.getSystemParamtersList().get("MAIL_WELCOME_MSG"));
					context.put("webAddress",CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
					if(userDetailsAddresss!=null)
					{
						context.put("firstName", userDetailsAddresss.getFirstName());
			            context.put("lastName", userDetailsAddresss.getLastName());
			            context.put("email", userDetailsAddresss.getEmailAddress());
			            context.put("password", userDetailsAddresss.getPassword());
			            context.put("newsLetter", userDetailsAddresss.getNewsLetterSub());
			            context.put("address1",userDetailsAddresss.getAddress1());
			            context.put("address2",userDetailsAddresss.getAddress2());
			            context.put("city",userDetailsAddresss.getCity());
			            context.put("state",userDetailsAddresss.getState());
			            context.put("zip",userDetailsAddresss.getZipCode());
			            context.put("phone",userDetailsAddresss.getPhoneNo());
			            if(CommonUtility.validateString(userDetailsAddresss.getSubsetFlag())!=null) {
			            	  context.put("subsetFlag",userDetailsAddresss.getSubsetFlag());
			            }
					}
					
					to = notificationDetail.get("TO_EMAIL");
					if(to==null){
						to = notificationDetail.get("BCC_EMAIL");
					}
					if(CommonUtility.validateString(contentData.get("EMAIL_TO").toString()).length()>0){
						if(CommonUtility.validateString(to).length()>0){
							to = to+";"+CommonUtility.validateString(contentData.get("EMAIL_TO").toString());
						}else{
							to = CommonUtility.validateString(contentData.get("EMAIL_TO").toString());
						}
					}
					
					subject = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("sentmailconfig.sendRegistrationMail.webUser.2C.subject");
					
					 if(CommonUtility.customServiceUtility()!=null) {
						 subject=CommonUtility.customServiceUtility().customizedSubjectMailToCustomerAndCreditManager(contentData,subject);
					 }
					
					context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
		            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
		            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
		            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
		            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
		            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
		            }
		            
		            
		            StringWriter writer = new StringWriter();
		            if(notificationDetail!=null && notificationDetail.get("DESCRIPTION")!=null) {
		            	Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
		            }
		            
		            StringBuilder finalMessage= new StringBuilder();
		            finalMessage.append(writer.toString());
		            if(notificationDetail!=null){
		            	if(CommonUtility.validateString(to).contains(CommonUtility.validateString(notificationDetail.get("TO_EMAIL")))){
		            		notificationDetail.put("TO_EMAIL",to);
		            	}else{
		            		notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+to);
		            	}
		            	notificationDetail.put("SUBJECT", subject);
		            	notificationDetail.put("FROM_MAIL_ADDRESS", fromEmail);
		            	notificationDetail.put("EMAIL_CONTENT_TO_SEND", finalMessage.toString());
		            	
		            	String[] attachments = new String[1];
		            	String[] attachmentsFileName = new String[1];
		            	if(contentData.get("attachments")!=null && ((String[]) contentData.get("attachments")).length>0){
		            		attachments = new String[((String[]) contentData.get("attachments")).length];
		            		attachments = (String[]) contentData.get("attachments");
		            		
		            		attachmentsFileName = new String[((String[]) contentData.get("attachmentsFileName")).length];
		            		attachmentsFileName = (String[]) contentData.get("attachmentsFileName");
		            	}
		            	/*
		            	attachments[0] = "E:\\Test1.pdf";
		            	attachments[1] = "E:\\OrderWritingService.log";
		            	attachments[2] = "E:\\CreditCard_with_CC_Response1.txt";*/
		            	
			 			flag =sendMailWithAttachment(notificationDetail, attachments, attachmentsFileName);
			            System.out.println("flag : "+flag);
						
			           /* String toMailList = notificationDetail.get("TO_EMAIL")+";"+to;
			            String mailStatus = "N";
						if(flag){
							mailStatus = "Y";
						}
						//boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, "User Registration", fromEmail, finalMessage, mailStatus, "ForgotPassword",null);
						//System.out.println("Your Save Email in DB Status @ sendRegistrationMail : "+saveMailStatus);*/	
		            }
	        	}else{
		    		System.out.println("-------------- Note : sendRegistrationMail mail sending : template Not Available in CIMM2 Notification");
		    	}
            
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBConnection(conn);
        }
        return flag;
	}

	public StringBuilder buildCreditApplictionForm(CreditApplicationModel creditApplicationModel){
		StringBuilder stringBuilder = new StringBuilder();
		try {
			LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("CreditApplicationTemplate");
			VelocityContext context = new VelocityContext();
			context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			context.put("creditAplication", creditApplicationModel);
			StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            System.out.println("writer.toString() Credit Application : "+writer.toString());
            stringBuilder.append(writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuilder;
	}
	
	public boolean sendMailForContactUsForm(String firstName,String lastName,String email,String companyName,String phoneNo,String comments, String loacationName, String toEmail, String pageUrl){
		SendMailUtility sendMailObj = new SendMailUtility();
		boolean flag = false;
		try {
			
			LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("ContactUsForm");
			
			VelocityContext context = new VelocityContext();
			context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			context.put("firstName", firstName);
			context.put("lastName", lastName);
			context.put("email", email);
			context.put("comments", comments);
			context.put("companyName", companyName);
			context.put("phoneNo", phoneNo);
			context.put("pageUrl", pageUrl);
			context.put("loacationName", loacationName);
			context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
	        context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
	        context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
	        
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());
            
            System.out.println("sendProductMail \n"+writer.toString()); 
            String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+email);
			//notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmail);
			notificationDetail.put("CC_EMAIL", (notificationDetail.get("CC_EMAIL")!=null?notificationDetail.get("CC_EMAIL")+";":""));
			notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+toEmail);
			
			flag = sendMailObj.sendNotification(notificationDetail, propertyLoader("sentmailconfig.contactusform.subject","")  , fromEmail , finalMessage.toString());	
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean sendMailForSubscriptionForm(String firstName,String lastName,String email,String companyName,String nearestBranch, String toEmail, String comments, String jobTitle){
		SendMailUtility sendMailObj = new SendMailUtility();
		boolean flag = false;
		try {
			
			LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("NewsLetterSubscriptionForm");
			
			VelocityContext context = new VelocityContext();
			context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			context.put("firstName", firstName);
			context.put("lastName", lastName);
			context.put("email", email);
			context.put("nearestBranch", nearestBranch);
			context.put("companyName", companyName);
			context.put("comments", comments);
			context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
	        context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
	        context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
	        context.put("jobTitle", jobTitle);
                      
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());
            
            System.out.println("NewsLetterSubscriptionForm \n"+writer.toString()); 
            String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+email);
			//notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmail);
			notificationDetail.put("CC_EMAIL", (notificationDetail.get("CC_EMAIL")!=null?notificationDetail.get("CC_EMAIL")+";":""));
			notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+toEmail);
			
			flag = sendMailObj.sendNotification(notificationDetail, propertyLoader("sentmailconfig.newslettersubscriptionform.subject","")  , fromEmail , finalMessage.toString());	
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean CommercialDiscountSubscription(BrontoModel userDetails){
		SendMailUtility sendMailObj = new SendMailUtility();
		boolean flag = false;
		try {
			
			LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("CommercialDiscountSubscription");
			
			VelocityContext context = new VelocityContext();
			context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			context.put("userDetails", userDetails);
			context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
	        context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
	        context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
                      
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());
            
            String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+userDetails.getEmail());
			//notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmail);
			notificationDetail.put("CC_EMAIL", (notificationDetail.get("CC_EMAIL")!=null?notificationDetail.get("CC_EMAIL")+";":""));
			notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":""));
			
			flag = sendMailObj.sendNotification(notificationDetail, propertyLoader("sentmailconfig.customerDiscountSubscripton.subject","")  , fromEmail , finalMessage.toString());	
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean requestForNewShippingAddressMail(UsersModel userDetails){
		SendMailUtility sendMailObj = new SendMailUtility();
		boolean flag = false;
		try {
			
			LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("NewShippingAddressRequest");
			
			VelocityContext context = new VelocityContext();
			context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			context.put("userDetails", userDetails);
			context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
	        context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
	        context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
                      
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());
            
            String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+userDetails.getEmailAddress());
			notificationDetail.put("TO_APPROVER", (notificationDetail.get("TO_APPROVER")!=null?notificationDetail.get("TO_APPROVER")+";":"")+userDetails.getApproverEmail());
			notificationDetail.put("CC_EMAIL", (notificationDetail.get("CC_EMAIL")!=null?notificationDetail.get("CC_EMAIL")+";":""));
			notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":""));
			
			flag = sendMailObj.sendNotification(notificationDetail, propertyLoader("sentmailconfig.newShippingAddressRequest.subject",""), fromEmail , finalMessage.toString());	
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	
	public boolean requestForNewShippingAddressMailtoApprover(UsersModel userDetails){
		SendMailUtility sendMailObj = new SendMailUtility();
		boolean flag = false;
		try {
			
			LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("NewShippingAddressRequestApprover");
			
			VelocityContext context = new VelocityContext();
			context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			context.put("userDetails", userDetails);
			context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
	        context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
	        context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
                      
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());
            
            String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+userDetails.getApproverEmail());
			notificationDetail.put("CC_EMAIL", (notificationDetail.get("CC_EMAIL")!=null?notificationDetail.get("CC_EMAIL")+";":""));
			notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":""));
			
			flag = sendMailObj.sendNotification(notificationDetail, propertyLoader("sentmailconfig.newShippingAddressRequest.subject",""), fromEmail , finalMessage.toString());	
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	
	public boolean sendRegistrationFailAttemptMail(AddressModel userDetailsAddresss){
		boolean flag = false;
		ResultSet rs = null;
	    Connection  conn = null;

	    String subject = propertyLoader("sentmailconfig.sendRegistrationFailAttemptMail.subject","");
	    String to = "";
	    try{
        	StringBuilder userDetails = new StringBuilder();
        	
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("ExistingAccountRegistrationFailMessage");
        	
        	
        	if(notificationDetail!=null && notificationDetail.size()>0){
        	
        	String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			if(notificationDetail!=null && notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			if(notificationDetail!=null)
			notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
			
			VelocityContext context = new VelocityContext();
        	
			context.put("WelcomeText",CommonDBQuery.getSystemParamtersList().get("MAIL_WELCOME_MSG"));
			context.put("webAddress",CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			
			if(userDetailsAddresss!=null){
				context.put("firstName", userDetailsAddresss.getFirstName());
	            context.put("lastName", userDetailsAddresss.getLastName());
	            context.put("companyName", userDetailsAddresss.getCompanyName());
	            context.put("phoneNumber", userDetailsAddresss.getPhoneNo());
	            context.put("accountNumber", userDetailsAddresss.getEntityId());
	            context.put("emailAddress",userDetailsAddresss.getEmailAddress());
	        }
			
			
			
			context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            StringWriter writer = new StringWriter();
            
	            if(notificationDetail!=null && notificationDetail.get("DESCRIPTION")!=null){
	            	
	            	Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
	            	
	            	StringBuilder finalMessage= new StringBuilder();
	                finalMessage.append(writer.toString());
	                
	                if(notificationDetail!=null){
	                	if(CommonUtility.validateString(to).contains(CommonUtility.validateString(notificationDetail.get("TO_EMAIL")))){
	                		notificationDetail.put("TO_EMAIL",to);
	                	}else{
	                		notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+to);
	                	}
	                
	
	    			
	                flag = sendNotification(notificationDetail, subject, fromEmail, finalMessage.toString());
	     			String toMailList = notificationDetail.get("TO_EMAIL")+";"+to;
	     			
	     			String mailStatus = "N";
	    			if(flag){
	    				mailStatus = "Y";
	    			}
	    			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, "User Registration Attempt", fromEmail, finalMessage, mailStatus, "ForgotPassword",null);
	    			System.out.println("Your Save Email in DB Status @ sendRegistrationMail : "+saveMailStatus);
	                }
	            }
         	}else{
	    		System.out.println("-------------- Note : sendRegistrationMail mail sending : template Not Available in CIMM2 Notification");
	    	}
            
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBConnection(conn);
        }
        return flag;
	}
	
	public static boolean sendNotificationOnCallForPrice(UsersModel userDetails,ArrayList<ProductsModel> itemList){
		boolean flag = false;
		try{
		    	
		    	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("NotifyOnCallForPrice");
		    	
		    	if(notificationDetail!=null && CommonUtility.validateString(notificationDetail.get("DESCRIPTION")).length()>0){
			    	
		    		String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			    	
			    	SendMailUtility sendMailUtility = new SendMailUtility();
				    		
				    VelocityContext context = new VelocityContext();
					context.put("itemList", itemList);
					context.put("customerName", userDetails.getUserName());
					context.put("userDetails", userDetails);
					context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
				    context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
				    context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
				    context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
				    if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
				       	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
				    }
		            StringWriter writer = new StringWriter();
		            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
				    StringBuilder finalMessage= new StringBuilder();
					finalMessage.append(writer.toString());
				    
				    flag = sendMailUtility.sendNotification(notificationDetail, propertyLoader("sentmailconfig.Item.with.no.price.on.website.subject",""), fromEmail, finalMessage.toString());
				    
		    	}
		    	System.out.println("No Price Notification Sent: "+flag);
		
			}catch(Exception e){
		    	e.printStackTrace();
		    }
		    return flag;
	}
	
	@SuppressWarnings("unchecked")
	public boolean sendOrderMail(SalesModel orderDetail, SendMailModel sendMailModel){
		
		HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    boolean flag = false;
	    String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
	    String erpDisplayName = null;
	    DecimalFormat df2 = CommonUtility.getPricePrecision(session);
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy"); //  HH:mm
	    if(session.getAttribute("timezone")!=null) {
	    	dateFormat.setTimeZone(TimeZone.getTimeZone((String)session.getAttribute("timezone")));
	    }
		Date date = new Date();
	    
	    try{
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("OrderMail");
        	String orderMailAddressCustomerWise = UsersDAO.getCustomerCustomTextFieldValue(CommonUtility.validateNumber(buyingCompanyId),"ORDER_SUBMIT_EMAIL_ID");
        	sendMailModel.setUserId(CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY)));
        	
            VelocityContext context = new VelocityContext();
            context.put("orderDetailObject", orderDetail);
            context.put("pickUpLocation", CommonUtility.getWareHouseByCode(orderDetail.getShippingBranchId()));
			if (session != null) {
				if (session.getAttribute("loginCustomerName") != null) {
					context.put("loginCustomerName", CommonUtility.validateString((String) session.getAttribute("loginCustomerName")));
				}

				String csrAdminName = "";
				Map<String, String> salesUserDetails = (Map<String, String>) session.getAttribute("salesUserDetails");
				if (session.getAttribute("isSalesUser") != null && CommonUtility.validateString((String) session.getAttribute("isSalesUser")).equalsIgnoreCase("Y")) {
					context.put("isSalesUser",CommonUtility.validateString((String) session.getAttribute("isSalesUser")));
				}
				if (session.getAttribute("isSalesAdmin") != null && CommonUtility.validateString((String) session.getAttribute("isSalesAdmin")).equalsIgnoreCase("Y")) {
					context.put("isSalesAdmin", CommonUtility.validateString((String) session.getAttribute("isSalesAdmin")));
					context.put("csrRole", "Administrator");
				} else {
					context.put("csrRole", "Sales Rep");
				}
				if (salesUserDetails != null) {
					if (CommonUtility.validateString(salesUserDetails.get("firstName")).length() > 0) {
						csrAdminName = CommonUtility.validateString(salesUserDetails.get("firstName")) + " ";
					}
					if (CommonUtility.validateString(salesUserDetails.get("lastName")).length() > 0) {
						csrAdminName = csrAdminName + CommonUtility.validateString(salesUserDetails.get("lastName"));
					}
					context.put("csrAdminName", CommonUtility.validateString(csrAdminName));
				}
			}
            
            String toEmailId = sendMailModel.getToEmailId();
			UnilogFactoryInterface firstOrderserviceClass = UnilogEcommFactory.getInstance().getData(PropertyAction.getVersionControl().get("CustomServiceProvider"));
			if(firstOrderserviceClass!=null && sendMailModel.isFirstOrderEmail()) {
				 notificationDetail = firstOrderserviceClass.getFirstOrderNotification();
				if(notificationDetail!=null){
					toEmailId = "";
				}
			}
			if(sendMailModel.isReviewOrderMail()) {
				notificationDetail = getNotificationDetail.getNotificationDetails("ReviewBeforeOrderMail");				
			}
			
            String lastName = "";
            if(sendMailModel.getLastName()!=null && sendMailModel.getLastName().trim().length() > 0){
            	lastName = sendMailModel.getLastName().trim();
            }else{
            	lastName = "";
            }
            
            StringBuilder shippAddress = new StringBuilder();
            StringBuilder billAddress = new StringBuilder();
            String errDesc = orderDetail.getOrderStatusDesc();
            
            if(errDesc==null){errDesc = "Order Failed.";}
            if(orderDetail.getBillAddress() != null && orderDetail.getShipAddress() != null) {
            	if(orderDetail.getBillAddress().getCompanyName()!=null && orderDetail.getBillAddress().getCompanyName().trim().length() > 0) {
            		billAddress.append(orderDetail.getBillAddress().getCompanyName() +"<br />");
            	}
            	billAddress.append(orderDetail.getBillAddress().getAddress1()  +"<br />");
                if(orderDetail.getBillAddress().getAddress2()!=null){
                	billAddress.append(orderDetail.getBillAddress().getAddress2() +"<br />");
                }
                billAddress.append("<br/>"+orderDetail.getBillAddress().getCity()+", "+orderDetail.getBillAddress().getState()+" "+orderDetail.getBillAddress().getZipCode()+"<br />");
                billAddress.append(orderDetail.getBillAddress().getCountry()+"<br />");
                if(orderDetail.getBillAddress().getPhoneNo()!=null){
                	billAddress.append("Ph: " + orderDetail.getBillAddress().getPhoneNo());
                }
                if(orderDetail.getBillAddress().getCompanyName()!=null && orderDetail.getBillAddress().getCompanyName().trim().length() > 0) {
                shippAddress.append(orderDetail.getBillAddress().getCompanyName() +"<br />");
                }
                if(orderDetail.getShipAddress().getShipToId()!=null && orderDetail.getShipAddress().getShipToId().trim().length() > 0){
                	shippAddress.append(orderDetail.getShipAddress().getShipToId() + "-");
                }
                if(orderDetail.getShipAddress().getShipToName()!=null && orderDetail.getShipAddress().getShipToName().trim().length() > 0){
                	shippAddress.append(orderDetail.getShipAddress().getShipToName() + "<br />");
                }else{
                	shippAddress.append("<br />");
                }
                shippAddress.append(orderDetail.getShipAddress().getAddress1()  +"<br />");
                if(orderDetail.getShipAddress().getAddress2()!=null && orderDetail.getShipAddress().getAddress2().trim().length() > 0){
                	shippAddress.append(orderDetail.getShipAddress().getAddress2() +"<br />");
                }
                String country = orderDetail.getBillAddress().getCountry()!=null?orderDetail.getBillAddress().getCountry():orderDetail.getShipAddress().getCountry();                
                shippAddress.append(orderDetail.getShipAddress().getCity()+", "+orderDetail.getShipAddress().getState()+", "+country+" "+orderDetail.getShipAddress().getZipCode()+"<br />");
                if(orderDetail.getShipAddress().getPhoneNo()!=null){
                	shippAddress.append("Ph: " + orderDetail.getShipAddress().getPhoneNo());
                }
            }
            if(CommonUtility.validateString(sendMailModel.getMailSubject()).toUpperCase().contains("QUOTE")){
            	context.put("orderType", "QUOTE");
            }
            context.put("subject", sendMailModel.getMailSubject());
            context.put("salesOrderNumber", orderDetail.getOrderNum());
            context.put("externalSystemId", CommonUtility.validateString(orderDetail.getOrderNum()));
            context.put("erpOrderNumber", CommonUtility.validateString(orderDetail.getErpOrderNumber()));
            if(orderDetail.getPaymentMethod()!=null && orderDetail.getPaymentMethod().trim().length()>0){
            	 context.put("paymentMethod",orderDetail.getPaymentMethod().trim());
            	 if(orderDetail.getPaymentMethod().trim().equalsIgnoreCase("Credit Card")){
            		 if(orderDetail.getTransactionId()!=null && orderDetail.getTransactionId().trim().length()>0){
            			 context.put("transactionId",orderDetail.getTransactionId());
            		 }else{
            			 context.put("transactionId","N/A");
            		 }
            	 }
            }
            UsersModel salesRepIn = (UsersModel) session.getAttribute("salesRepIn");
			if(salesRepIn!=null){
				context.put("insideSalesRepEmail", salesRepIn.getEmailAddress());
			}
			UsersModel salesRepOut = (UsersModel)session.getAttribute("salesRepOut");
			if(salesRepOut!=null){
				context.put("outsideSalesRepEmail", salesRepOut.getEmailAddress());
			}
			if(session.getAttribute("selectedshipToIdSx")!=null){
	            context.put("selectedshipToIdSx",session.getAttribute("selectedshipToIdSx").toString());
	        }
			if(CommonUtility.validateString(sendMailModel.getAdditionalComments()).length()>0) {
				context.put("AdditionalComments", sendMailModel.getAdditionalComments());
			}
			if(CommonUtility.validateString(sendMailModel.getAdditionalName()).length()>0) {
				context.put("additionalPickupName", sendMailModel.getAdditionalName());		
						}
			if(CommonUtility.validateString(sendMailModel.getAdditionalPickupPerson()).length()>0) {
				context.put("additionalShipToStoreName", sendMailModel.getAdditionalPickupPerson());
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SAVE_LEADTIME_ON_ORDER_CONFIRMATION")).equalsIgnoreCase("Y")){
				for(SalesModel eachItem:orderDetail.getOrderList()){
					if(!CommonUtility.validateString(eachItem.getLeadTime()).equalsIgnoreCase("-1")){
						DateFormat df = new SimpleDateFormat("EEE MM/dd/yyyy");
						int leadTime = CommonUtility.validateNumber(eachItem.getLeadTime());
						Calendar c = Calendar.getInstance();
						if(leadTime>0){
							c.add(Calendar.DATE, leadTime);	
						}
						int hourofDay = c.get(Calendar.HOUR_OF_DAY);
						if(hourofDay>15){
							c.add(Calendar.DATE, 1);	
						}
						int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
						if(dayOfWeek==Calendar.FRIDAY){
							c.add(Calendar.DATE, 3);
						}else if(dayOfWeek==Calendar.SATURDAY){
							c.add(Calendar.DATE,2);
						}else if(dayOfWeek==Calendar.SUNDAY){
							c.add(Calendar.DATE,1);
						}
						eachItem.setLeadTime(df.format(c.getTime()));
					}
				}
			}
			
			LinkedHashMap<String,ArrayList<SalesModel> > orderItemGroupList = null;
			//CustomServiceProvider
			if(CommonUtility.customServiceUtility()!=null) {
				orderItemGroupList = CommonUtility.customServiceUtility().getGroupedItemsInSalesData(orderDetail.getOrderList());
				context.put("orderItemGroupList", orderItemGroupList);
			}
			//CustomServiceProvider	
			context.put("orderType", "Order");
			context.put("customFieldValueObject", orderDetail.getCustomFieldVal());
			context.put("billingAddressObject", orderDetail.getBillAddress());
            context.put("shippingAddressObject", orderDetail.getShipAddress());
            context.put("erpDisplayName", erpDisplayName);
            context.put("orderedBy", orderDetail.getOrderedBy());
            context.put("poNumber", orderDetail.getPoNumber());
            context.put("reqDate", orderDetail.getReqDate());
            context.put("shipMethod", orderDetail.getShipMethod());
            context.put("shipViaDisplayName", orderDetail.getShipViaMethod());
            context.put("shippingInstruction", orderDetail.getShippingInstruction());
            context.put("orderNotes", orderDetail.getOrderNotes());
            context.put("orderStatus", "Bid");
            context.put("shipViaDesc",  orderDetail.getShipViaDescription());
            context.put("billAddress", billAddress.toString());
            context.put("shipAddress", shippAddress.toString());
            context.put("lineItemList", orderDetail.getOrderList());
            context.put("orderTotal", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTotal()))));
            context.put("tax", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTax()))));
            context.put("subTotal", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getSubtotal()))));
            context.put("freight", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getFreight()))));
            context.put("handling", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getHandling()))));
            context.put("deliveryFee", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getDeliveryCharge()))));
            if(CommonUtility.customServiceUtility() != null) {
            	CommonUtility.customServiceUtility().getDiscountPrice(orderDetail, context);
            }
            context.put("discount", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getDiscount()))));
            context.put("orderItemsDiscount", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getDiscount()))));
            context.put("totalSavings", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTotalSavingsOnOrder()))));
            context.put("appliedDiscountCoupons", orderDetail.getDiscountCouponCode());
            context.put("date",dateFormat.format(date));
            context.put("customerReleaseNumber", orderDetail.getCustomerReleaseNumber());
            context.put("homeBranchName", orderDetail.getHomeBranchName());
            context.put("companyName", orderDetail.getBillAddress()!= null?orderDetail.getBillAddress().getCompanyName():""); 
            NumberTool numbrTool =  new NumberTool();
            context.put("numberTool", numbrTool); 
            context.put("escapeTool", new EscapeTool());
	    	context.put("math", new MathTool());
	    	context.put("dispalyTool", new DisplayTool());
	    	context.put("dateTool", new ComparisonDateTool());
	    	context.put(Integer.class.getSimpleName(), Integer.class);
	    	context.put("session", session);
            if(errDesc!=null && errDesc.trim().contains("successfully")){
            	context.put("isErrDesc", "No");
            	context.put("status", errDesc);
            }else if(CommonUtility.validateString(errDesc).toUpperCase().contains("DUPLICATE")){
            	context.put("isErrDesc", "Yes");
            	context.put("status", errDesc);
            	context.put("duplicatePOStatus",propertyLoader("duplicate.status.po",""));
            	if(CommonUtility.validateString(propertyLoader("duplicate.status.po.subject","")).length()>0){
            		sendMailModel.setMailSubject(propertyLoader("duplicate.status.po.subject",""));
            	}
            }else if(errDesc!=null && !errDesc.trim().toUpperCase().contains("ERROR")){
            	context.put("isErrDesc", "No");
            	context.put("status", errDesc);
            }else{
            	context.put("isErrDesc", "Yes");
            	context.put("status", "Order Failed.");
            }
            
            context.put("errDesc", errDesc);
            context.put("firstName", sendMailModel.getFirstName());
            context.put("lastName", lastName);
            context.put("userName", sendMailModel.getUserName());
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            context.put("uniqueWebReferenceNumber", orderDetail.getUniqueWebReferenceNumber());
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            String orderSuffux = "";
			if(CommonUtility.validateParseIntegerToString(orderDetail.getOrderSuffix()).length()>0){
				orderSuffux = "- 0"+CommonUtility.validateParseIntegerToString(orderDetail.getOrderSuffix());
				context.put("orderSuffux", orderSuffux);
			}
			
			if(CommonUtility.customServiceUtility()!=null) {
				//@SuppressWarnings("unused")
				//String contextObject = CommonUtility.customServiceUtility().addContextObject(orderDetail, context);
				CommonUtility.customServiceUtility().addContextObject(orderDetail, context);
			}

			if(notificationDetail!=null && CommonUtility.validateString(notificationDetail.get("DESCRIPTION")).length()>0){
			
		            StringWriter writer = new StringWriter();
		            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
		            
		            StringBuilder finalMessage= new StringBuilder();
		            finalMessage.append(writer.toString());
		            
		            if(CommonUtility.validateNumber(CommonUtility.validateString(notificationDetail.get("CMS_STATIC_PAGE_ID")))>0) {
                        finalMessage.append(getStaticPageRenderedContent(context,CommonUtility.validateNumber(CommonUtility.validateString(notificationDetail.get("CMS_STATIC_PAGE_ID")))));
		            }

					String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
					
					if(notificationDetail.get("FROM_EMAIL")!=null){
						fromEmail = notificationDetail.get("FROM_EMAIL");
					}
					
					if(session!=null && session.getAttribute("wareHouseEmailID")!=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_WAREHOUSE_SPECIFIC_ORDER_MAIL")).equalsIgnoreCase("Y")){
						String toFromNotification = notificationDetail.get("TO_EMAIL");
						 String toEmailIdWarehouse = (String)session.getAttribute("wareHouseEmailID");
						if(toFromNotification!=null && toFromNotification.trim().length()>0 && !toFromNotification.trim().equalsIgnoreCase("null")){
							notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+toEmailIdWarehouse);
						}else{
							//notificationDetail.put("TO_EMAIL", toEmailIdWarehouse);
							notificationDetail.put("TO_EMAIL", toEmailIdWarehouse+";"+toEmailId);
						}
					}else if(CommonUtility.validateString(toEmailId).length()>0 && !CommonUtility.validateString(toEmailId).equalsIgnoreCase("null")){
						if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ORDER_MAIL_TO_USER_ONLY")).equalsIgnoreCase("Y")){
							toEmailId = CommonUtility.validateString(toEmailId);// Only user who is placing order
						}else{
							if(orderDetail!= null && orderDetail.getShipAddress()!=null && orderDetail.getShipAddress().getEmailAddress()!=null && orderDetail.getShipAddress().getEmailAddress().trim().length()>0 && !toEmailId.trim().equalsIgnoreCase(orderDetail.getShipAddress().getEmailAddress())){
								toEmailId = toEmailId+";"+orderDetail.getShipAddress().getEmailAddress().trim(); 
				            }else if(orderDetail!= null && orderDetail.getShipEmailAddress()!=null && orderDetail.getShipEmailAddress().trim().length() > 0 && !toEmailId.trim().equalsIgnoreCase(orderDetail.getShipEmailAddress())){
				            	toEmailId = toEmailId+";"+orderDetail.getShipEmailAddress().trim();
				            }
						}
						if(sendMailModel!=null && CommonUtility.validateString(sendMailModel.getAdditionalEmailNotification()).length()>0) {
							toEmailId = toEmailId+";"+sendMailModel.getAdditionalEmailNotification().trim();
						}
						String toFromNotification = notificationDetail.get("TO_EMAIL");
						if(toFromNotification!=null && toFromNotification.trim().length()>0 && !toFromNotification.trim().equalsIgnoreCase("null")){
							notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+toEmailId);
						}else{
							notificationDetail.put("TO_EMAIL", toEmailId);
						}
					}
					
					if(orderMailAddressCustomerWise!=null && orderMailAddressCustomerWise.trim().length()>0 && !orderMailAddressCustomerWise.trim().equalsIgnoreCase("null")){
						String bccFromNotification = notificationDetail.get("BCC_EMAIL");
						if(bccFromNotification!=null && bccFromNotification.trim().length()>0 && !bccFromNotification.trim().equalsIgnoreCase("null")){
							notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL")+";"+orderMailAddressCustomerWise);
						}else{
							notificationDetail.put("BCC_EMAIL", orderMailAddressCustomerWise);
						}
						
					}
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SEND_ORDER_MAIL_TO_WAREHOUSE")).equalsIgnoreCase("Y")){
						String wareHousecode = (String) session.getAttribute("wareHouseCode");
						WarehouseModel wareHouseDetail = new WarehouseModel();
						wareHouseDetail = UsersDAO.getWareHouseDetailsByCode(wareHousecode);
						if(wareHouseDetail!=null && CommonUtility.validateString(wareHouseDetail.getEmailAddress()).length()>0) {
							String bccFromNotification = notificationDetail.get("BCC_EMAIL");
							if(bccFromNotification!=null && bccFromNotification.trim().length()>0 && !bccFromNotification.trim().equalsIgnoreCase("null")){
								notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL")+";"+wareHouseDetail.getEmailAddress());
							}else{
								notificationDetail.put("BCC_EMAIL", wareHouseDetail.getEmailAddress());
							}
						}
					}
					if(CommonUtility.validateString(orderDetail.getSendmailToSalesRepOnly()).equalsIgnoreCase("Y")){
						if(CommonUtility.validateString(UsersDAO.getUserCustomField(sendMailModel.getUserId(), "IN_SIDE_SALES_REP")).length()>0){
							notificationDetail.put("TO_EMAIL",toEmailId);
							notificationDetail.put("CC_EMAIL",UsersDAO.getUserCustomField(sendMailModel.getUserId(), "IN_SIDE_SALES_REP"));
						}
					}
					LinkedHashMap<String, String> salesRepEmailList = UsersDAO.getAllCustomerCustomFieldValue(CommonUtility.validateNumber(buyingCompanyId));
					if(CommonUtility.validateString(orderDetail.getSendmailToSalesRepOnly()).equalsIgnoreCase("Y")){
						String userSalesRep = UsersDAO.getUserCustomField(sendMailModel.getUserId(), "IN_SIDE_SALES_REP");
						if(salesRepEmailList != null && salesRepEmailList.size() > 0 && salesRepEmailList.get("IN_SIDE_SALES_REP_CUST") != null) {
							String custSalesRep = CommonUtility.validateString(salesRepEmailList.get("IN_SIDE_SALES_REP_CUST"));
							String bccFromNotification = notificationDetail.get("BCC_EMAIL");
							if(bccFromNotification!=null && bccFromNotification.trim().length()>0 && !bccFromNotification.trim().equalsIgnoreCase("null")) {
								//notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL")+";"+orderMailAddressCustomerWise);
								notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL") + ";" + custSalesRep +";");
							} else {
								notificationDetail.put("BCC_EMAIL", custSalesRep + ";");
							}
						} else if(CommonUtility.validateString(userSalesRep).length()>0){
							notificationDetail.put("TO_EMAIL", toEmailId);
							notificationDetail.put("CC_EMAIL", userSalesRep);
						}
					} 
					if(salesRepEmailList!=null && salesRepEmailList.size()>0 && salesRepEmailList.containsKey("IN_SIDE_SALES_REP_CUST")){
	        			String repEmailId = salesRepEmailList.get("IN_SIDE_SALES_REP_CUST");
	        			if(CommonUtility.validateString(repEmailId).length()>0){
	        				String SalesRepNotificationList  = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SALESREP_NOTIFICATION_LIST"));
	        				if(SalesRepNotificationList.contains(CommonUtility.validateString(notificationDetail.get("NOTIFICATION_NAME_FROM_DB")))){
	        					notificationDetail.put("BCC_EMAIL",notificationDetail.get("BCC_EMAIL")+";"+repEmailId);	
	        				}
	        			}
	        		}
					//notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL"):""));
					//notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:""));
					
					
					String orderMailSubject = sendMailModel.getMailSubject();
					/*if(CommonUtility.validateParseIntegerToString(orderDetail.getOrderSuffix()).length()>0){
						orderSuffux = "-0"+CommonUtility.validateParseIntegerToString(orderDetail.getOrderSuffix());
					}*/
					if(orderDetail!= null && orderDetail.getErpOrderNumber()!=null && CommonUtility.validateString(orderDetail.getErpOrderNumber()).length()>0){
						orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject+" - #"+CommonUtility.validateString(orderDetail.getErpOrderNumber());
					}else if(orderDetail!= null && orderDetail.getOrderNum()!=null && CommonUtility.validateString(orderDetail.getOrderNum()).length()>0){
						orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject+" - #"+orderDetail.getOrderNum();
					}else{
						if(CommonUtility.validateString((String) session.getAttribute("erpType")).equalsIgnoreCase("DEFAULTS")){
							orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject;
						}else {
							orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+" - Order Failure";
						}
					}
					
					if(CommonUtility.customServiceUtility() != null) {
						orderMailSubject = CommonUtility.customServiceUtility().getModifiedOrderMailSubject(orderMailSubject,orderDetail, sendMailModel.getMailSubject());
					}
					
					flag = sendNotification(notificationDetail, orderMailSubject, fromEmail, finalMessage.toString());
					
					String toMailList = (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId;
					String bccMailList = (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:"");
					
					String mailStatus = "N";
					if(flag){
						mailStatus = "Y";
					}
					boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, orderMailSubject, fromEmail, finalMessage, mailStatus, sendMailModel.getMailSubject(),bccMailList);
					System.out.println("order Email in DB Status @ Order Confirmation Mail : "+saveMailStatus);
	    }
			
			
        }catch(Exception e){
        	e.printStackTrace();
        }
	    
       	return flag;	
	}
	public static boolean staticPageNotification(Map<String, String> formParameters, HttpSession session){
		LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails(formParameters.get("mailTemplateName").toString());
		boolean flag = false;
		if(notificationDetail!=null && CommonUtility.validateString(notificationDetail.get("DESCRIPTION")).length()>0){ 
			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			String subject = formParameters.get("mailSubject");
			SendMailUtility sendMailUtility = new SendMailUtility();
			String uploadedFileNames = formParameters.get("uploadedFileNames");
			LinkedHashMap<String, String> finalValueList =  new LinkedHashMap<String, String>();
			String toEmailAddressFromPage = CommonUtility.validateString(formParameters.get("toEmail"));

			if(formParameters!=null && formParameters.size()>0){
				for (Map.Entry<String, String> entry : formParameters.entrySet()){ 
					finalValueList.put(entry.getKey(), CommonUtility.validateString(entry.getValue()));
				}
			}


			VelocityContext context = new VelocityContext();
			context.put("formParameters",formParameters);
			context.put("formParameters", finalValueList);
			context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
			context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
			context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));

			if(CommonUtility.customServiceUtility() != null) {
				uploadedFileNames = CommonUtility.customServiceUtility().creditApplication(session,formParameters,sendMailUtility,context,getNotificationDetail,uploadedFileNames);
			}
			
			if(session.getAttribute("localeCode")!=null ){
				context.put("locale",LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()));
				context.put("localeLang",(String)session.getAttribute("sessionLocale"));
			}else{
				context.put("locale",LayoutLoader.getMessageProperties().get("EN"));
				context.put("localeLang","1_en");
				session.setAttribute("localeId","1");
				session.setAttribute("localeCode","EN");
				session.setAttribute("sessionLocale","1_en");
			}

			if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
				context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
			}
			StringWriter writer = new StringWriter();
			try {
				Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
			} catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException | IOException e) {
				e.printStackTrace();
			}
			StringBuilder finalMessage= new StringBuilder();
			finalMessage.append(writer.toString());
			System.out.println("Mail Template before sending email \n"+writer.toString());
			notificationDetail.put("toEmailAddressFromPage", toEmailAddressFromPage);

			if(uploadedFileNames !=null && uploadedFileNames.length() > 0){
				notificationDetail.put("SUBJECT", subject);
				notificationDetail.put("FROM_MAIL_ADDRESS", fromEmail);
				notificationDetail.put("EMAIL_CONTENT_TO_SEND", finalMessage.toString());
				
				if(CommonUtility.customServiceUtility() != null) {
					CommonUtility.customServiceUtility().setFromMailAddress(CommonUtility.validateString(formParameters.get("fromEmail")),notificationDetail);
				}

				String attachments[] = uploadedFileNames.split(",");
				String filePath = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH"));
				String attachmentsFileNames[] = new String[attachments.length];
				for(int i = 0; i < attachments.length; i++){
					String attachment = attachments[i];
					attachmentsFileNames[i] = attachment.replaceAll(filePath +"/"+ session.getId() + "_", "");
				}
				flag = sendMailWithAttachment(notificationDetail, attachments, attachmentsFileNames);
			}
			else{
				flag = sendMailUtility.sendNotification(notificationDetail, subject, fromEmail, finalMessage.toString());
			}
		}
		return flag;
	}
	public static boolean sendDocumentNotification(LinkedHashMap<String, String> notification, String subject,String from, String body,String [] attachedFileName,String [] attachmentFilePath){
		System.out.println("sending mail process :----");
         boolean result = false;
         String subject_data = subject;
       
        	try {
      			
      			
        		SecureData validUserPass = new SecureData();
        		String emailRelayEncrpt = CommonDBQuery.getSystemParamtersList().get("EMAILRELAY");
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
    	
    	         props.put("mail.smtp.socketFactory.port", emailRelayPort);
    	         //props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
    	         props.put("mail.smtp.socketFactory.fallback", "false");
             
    			 Authenticator authenticator = null;
              	 

              	 Session session = Session.getInstance(props, null);
                 session.setDebug(true);

                MimeMessage message = new MimeMessage(session);
                Address[] toAddress = null;
    			Address[] ccAddress = null;
    			Address[] bccAddress = null;
    			int loop = 0;
    			if(notification.get("TO_EMAIL")!=null && !notification.get("TO_EMAIL").trim().equalsIgnoreCase(""))
    			{
    				loop = 0;
    				String splitToAddress[] = notification.get("TO_EMAIL").split(";");
    				if(splitToAddress!=null && splitToAddress.length>0)
    				{
    					toAddress = new Address[splitToAddress.length];
    					for(String toAddr:splitToAddress)
    					{
    						toAddress[loop] = new InternetAddress(toAddr.trim());
    						loop++;
    					}
    				}
    				
    			}
    			
    			if(notification.get("CC_EMAIL")!=null && !notification.get("CC_EMAIL").trim().equalsIgnoreCase(""))
    			{
    				loop = 0;
    				String splitToAddress[] = notification.get("CC_EMAIL").split(";");
    				if(splitToAddress!=null && splitToAddress.length>0)
    				{
    					ccAddress = new Address[splitToAddress.length];
    					for(String toAddr:splitToAddress)
    					{
    						ccAddress[loop] = new InternetAddress(toAddr.trim());
    						loop++;
    					}
    				}
    				
    			}
    			if(notification.get("BCC_EMAIL")!=null && !notification.get("BCC_EMAIL").trim().equalsIgnoreCase(""))
    			{
    				loop = 0;
    				String splitToAddress[] = notification.get("BCC_EMAIL").split(";");
    				if(splitToAddress!=null && splitToAddress.length>0)
    				{
    					bccAddress = new Address[splitToAddress.length];
    					for(String toAddr:splitToAddress)
    					{
    						bccAddress[loop] = new InternetAddress(toAddr.trim());
    						loop++;
    					}
    				}
    				
    			}
    			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")).length()>0){
    				message.setFrom(new InternetAddress(from,CommonDBQuery.getSystemParamtersList().get("FROM_EMAIL_ADDRESS_NAME")));
				}else{
					message.setFrom(new InternetAddress(from));
				}
                
                
                message.addRecipients(Message.RecipientType.TO,toAddress );
                if(ccAddress!=null && ccAddress.length>0)
               	 message.addRecipients(Message.RecipientType.CC,ccAddress );
                
                if(bccAddress!=null && bccAddress.length>0)
               	 message.addRecipients(Message.RecipientType.BCC,bccAddress );
                
                message.setSubject(subject_data);
                BodyPart messageBodyPart = null;
                Multipart multipart = new MimeMultipart();
                // attched array of file path
                for(int i=0;i<attachmentFilePath.length;i++)
                {
                    System.out.println(attachmentFilePath[i]);

                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource((String)attachmentFilePath[i]);

                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName((String)attachedFileName[i]);
                    multipart.addBodyPart(messageBodyPart);
                    message.setContent(multipart);
                }

               		messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(new DataHandler(new HTMLDataSource(body)));
                    multipart.addBodyPart(messageBodyPart);
               	
	                message.setContent(multipart);
	                message.setSentDate(new Date());
                
               
                Transport.send(message);
                result = true;
            } catch (Exception e) {
           	 e.printStackTrace();
              
            }
         
        System.out.println("sending mail process  completed:----");
        return result;
    }
	
	public static String uploadImage(String imageName,String imageData,String path){
		 path= path+"/"+imageName+".jpg";
		  File file = new File(path);
		   try {
		   if(file.exists()){
		    System.out.println("File exists");
           }else{
           System.out.println("File does not exists");
           }
		     System.out.println(path);
		         if (imageData == null) {
		          System.out.println("Buffered Image is null");
		         }
		         else{
		         
		          byte[] byteArray = Base64.decode(imageData);
		          FileOutputStream fos1;
				
					fos1 = new FileOutputStream(file);
					  FileOutputStream fos = new FileOutputStream(path);
					   fos.write(byteArray);
					     if(fos1!=null){
					      fos1.close();
					     }
					     if(fos!=null)
					     {
					      fos.close();
					     }
		         }
		     	return imageName+".jpg";
				} catch (IOException e) {
					e.printStackTrace();
				 return "";
				}
		          
	}
	public static boolean sendCustomNotification(LinkedHashMap<String, Object> contentPart){
		boolean flag = false;
		try{
		    	
		    	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("NotifyOnAddingCPN");
		    	
		    	if(notificationDetail!=null && CommonUtility.validateString(notificationDetail.get("DESCRIPTION")).length()>0){
			    	
		    		String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			    	
			    	SendMailUtility sendMailUtility = new SendMailUtility();
				    		
				    VelocityContext context = new VelocityContext();
					context.put("contentPart", contentPart);
					context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
				    context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
				    context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
				    context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
				    if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
				       	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
				    }
		            StringWriter writer = new StringWriter();
		            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
				    StringBuilder finalMessage= new StringBuilder();
					finalMessage.append(writer.toString());
				    
				    flag = sendMailUtility.sendNotification(notificationDetail, propertyLoader("sentmailconfig.AddCPN.subject",""), fromEmail, finalMessage.toString());
				    
		    	}
		    	System.out.println("No Price Notification Sent: "+flag);
		
			}catch(Exception e){
		    	e.printStackTrace();
		    }
		    return flag;
	}
	
	public boolean sendvendorReturnMail(SendMailModel sendMailModel, ArrayList<SalesModel> itemList){
	    boolean flag = false;
	    try{
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("vendorReturnMail");        	
            VelocityContext context = new VelocityContext();          
            String toEmailId = sendMailModel.getToEmailId();
            String fromEmailId = sendMailModel.getFromEmailId();
            String ccEmailId = sendMailModel.getCcEmailId();
            String bccEmailId = sendMailModel.getBccEmailId();
            context.put("itemList", itemList);
            context.put("VRDeatails", sendMailModel);
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());
            String fromEmail = null;
            if(CommonUtility.validateString(fromEmail).isEmpty()){
            	fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
            }
			if(CommonUtility.validateString(notificationDetail.get("FROM_EMAIL")).length() > 0){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			if(CommonUtility.validateString(toEmailId).length()>0){
				String toFromNotification = notificationDetail.get("TO_EMAIL");
				if(CommonUtility.validateString(toFromNotification).length()>0){
					notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+toEmailId);
				}else{
					notificationDetail.put("TO_EMAIL", toEmailId);
				}
			}
			if(CommonUtility.validateString(ccEmailId).length() > 0){
				String toFromNotification = notificationDetail.get("CC_EMAIL");
				if(CommonUtility.validateString(toFromNotification).length() > 0){
					notificationDetail.put("CC_EMAIL", notificationDetail.get("CC_EMAIL")+";"+ccEmailId);
				}else{
					notificationDetail.put("CC_EMAIL", ccEmailId);
				}
			}
			if(CommonUtility.validateString(bccEmailId).length() > 0){
				String toFromNotification = notificationDetail.get("BCC_EMAIL");
				if(CommonUtility.validateString(toFromNotification).length() > 0){
					notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL")+";"+bccEmailId);
				}else{
					notificationDetail.put("BCC_EMAIL", bccEmailId);
				}
			}
			if(CommonUtility.validateString(fromEmailId).length() > 0){
				String toFromNotification = notificationDetail.get("CC_EMAIL");
				if(CommonUtility.validateString(toFromNotification).length() > 0){
					notificationDetail.put("CC_EMAIL", notificationDetail.get("CC_EMAIL")+";"+fromEmailId);
				}else{
					notificationDetail.put("CC_EMAIL", fromEmailId);
				}
			}
			String toMailList = (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"");
			String ccMailList = (notificationDetail.get("CC_EMAIL")!=null?notificationDetail.get("CC_EMAIL")+";":"");
			String bccMailList = (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"");
			String emilsList = ccMailList+""+bccMailList;
			Map<String, String> attachmentDetails = sendMailModel.getAttachmentDetails();
			if(attachmentDetails != null && attachmentDetails.size() > 0) {
				notificationDetail.put("SUBJECT", sendMailModel.getMailSubject());
				notificationDetail.put("FROM_MAIL_ADDRESS", fromEmail);
            	notificationDetail.put("EMAIL_CONTENT_TO_SEND", finalMessage.toString());
				flag = sendMailWithAttachment(notificationDetail, attachmentDetails.values().toArray(new String[0]), attachmentDetails.keySet().toArray(new String[0]));
			}else {
				flag = sendNotification(notificationDetail, sendMailModel.getMailSubject(), fromEmail, finalMessage.toString());
			}			
			String mailStatus = "N";
			if(flag){
				mailStatus = "Y";
			}
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, sendMailModel.getMailSubject(), fromEmail, finalMessage, mailStatus, sendMailModel.getMailSubject(),emilsList);
			System.out.println("Your Save Email in DB Status @ buildVendorRetutnMail : "+saveMailStatus);
        }catch(Exception e){
        	e.printStackTrace();
        }
       	return flag;
	}
	
	public boolean sendAbandonCartMail(ArrayList<ProductsModel> itemDetailList,String firstName,String lastName,String emailId,String subject){
		boolean flag = false;
	    
	    try{
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("AbandonedCartMail");
        	
        	VelocityContext context = new VelocityContext();
        	
            context.put("firstName", firstName);
            context.put("lastName", lastName);
            context.put("lineItemList", itemDetailList);
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());
            
            System.out.println("sendProductMail \n"+writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL") != null ? (String)notificationDetail.get("TO_EMAIL") + ";" : "") + emailId);
		    notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL") != null ? (String)notificationDetail.get("BCC_EMAIL") + ";" : "");
			
			flag = sendNotification(notificationDetail, subject , fromEmail, finalMessage.toString());
			
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
       return flag;
	}
	public boolean buildChangeEmailMail(SendMailModel sendMailModel){		
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    boolean flag = false;
	    
	    
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        
        try{
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("changeEmail");
            VelocityContext context = new VelocityContext();
          
            String toEmailId = sendMailModel.getToEmailId();
            String lastName = "";
            if(sendMailModel.getLastName()!=null && sendMailModel.getLastName().trim().length() > 0){
            	lastName = sendMailModel.getLastName().trim();
            }else{
            	lastName = "";
            }
            
            context.put("firstName", sendMailModel.getFirstName());
            context.put("lastName", lastName);
            context.put("userName", sendMailModel.getUserName());
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
            finalMessage.append(writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId);
			notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
			
			flag = sendNotification(notificationDetail,propertyLoader("sentmailconfig.buildChangeEmailMail.subject",""), fromEmail, finalMessage.toString());
			String toMailList = notificationDetail.get("TO_EMAIL");
			
			String mailStatus = "N";
			if(flag){
				mailStatus = "Y";
			}
			boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.buildChangeEmailMail.subject",""), fromEmail, finalMessage, mailStatus, "ChangePassword",null);
        }catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
			return flag;	
	}
	public String getStaticPageRenderedContent(VelocityContext velocityContext, int staticPageId) {
        String pageContent = "";
        try {
               String staticPageByIdUrl = context+staticPage+find+staticPageId;
               Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(staticPageByIdUrl, "GET", null, StaticPageData.class);
               if(response!=null && response.getStatus().getCode()==200) {
                      StaticPageData staticPageDataModel = (StaticPageData) response.getData();
                      if(staticPageDataModel!=null && (staticPageDataModel.getStatus().equalsIgnoreCase("Y") || staticPageDataModel.getStatus().equalsIgnoreCase("A"))) {
                            pageContent = staticPageDataModel.getPageContent();
                            StringWriter writer = new StringWriter();
                            Velocity.evaluate(velocityContext, writer, "", pageContent);
                            pageContent = writer.toString();
                      }
               }
        }catch (Exception e) {
               e.printStackTrace();
        }
        return pageContent;
  }
	public boolean sendMailToApprover(ArrayList<UsersModel> approveUserId,int userId,String gUser){
		boolean flag = false;
		Connection  conn = null;
		try {
			conn = ConnectionManager.getDBConnection();
		} catch (SQLException e) { 
			e.printStackTrace();
		}	
		try{
			if(approveUserId!=null && approveUserId.size()>0){
				for(UsersModel approveUserIdValue : approveUserId){
					UsersModel approverMail = UsersDAO.getUserEmail(approveUserIdValue.getApproverId());
					/*UsersModel approverMail = UsersDAO.getUserEmail(approveUserId);*/
					HttpServletRequest request =ServletActionContext.getRequest();
					HttpSession session = request.getSession();
					String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
					LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("Approvemailid");
					String orderMailAddressCustomerWise = UsersDAO.getCustomerCustomTextFieldValue(CommonUtility.validateNumber(buyingCompanyId),"ORDER_SUBMIT_EMAIL_ID");
					VelocityContext context = new VelocityContext();
					ProductsDAO.deleteFromCart(conn, userId);
					if(approverMail!=null){
						context.put("firstName", CommonUtility.validateString(approverMail.getFirstName()));
						context.put("lastName", CommonUtility.validateString(approverMail.getLastName()));
					}
					context.put("gUser", gUser);
					context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
					context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
					context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
					context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
					if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
						context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
					}
					StringWriter writer = new StringWriter();
					Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
					StringBuilder finalMessage= new StringBuilder();
					finalMessage.append(writer.toString());
					String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					if(notificationDetail.get("FROM_EMAIL")!=null){
						fromEmail = notificationDetail.get("FROM_EMAIL");
					}
					if(session.getAttribute("customerCustomFieldValue") != null){
						@SuppressWarnings("unchecked")
						LinkedHashMap<String, String> salesRepEmailList = (LinkedHashMap<String, String>)session.getAttribute("customerCustomFieldValue");
						String repEmailId = salesRepEmailList.get("IN_SIDE_SALES_REP_CUST");
	        			if(CommonUtility.validateString(repEmailId).length()>0){
	        				String SalesRepNotificationList  = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SALESREP_NOTIFICATION_LIST"));
	        				if(SalesRepNotificationList.contains(CommonUtility.validateString(notificationDetail.get("NOTIFICATION_NAME_FROM_DB")))){
	        					notificationDetail.put("BCC_EMAIL",notificationDetail.get("BCC_EMAIL")+";"+repEmailId);	
	        				}
	        			}
					}
					notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+approverMail.getEmailAddress());
					notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:""));
					flag = sendNotification(notificationDetail,propertyLoader("sentmailconfig.sendMailToApprover.subject",""), fromEmail, finalMessage.toString());
					System.out.println("APA email:" + approverMail.getEmailAddress());
					String toMailList = (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+approverMail.getEmailAddress();
					String bccMailList = (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:"");

					String mailStatus = "N";
					if(flag){
						mailStatus = "Y";
					}
					//String toAddress, String emailSubject,String emailFrom, StringBuilder emailMessage,String sentMailStatus,String emailType,String bcc
					boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.sendMailToApprover.subject",""), fromEmail, finalMessage, mailStatus, "Approval Mail",bccMailList);
					System.out.println("Your Save Email in DB Status @ sendMailToApprover : "+saveMailStatus);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBConnection(conn);
		}
		return flag;
	}
//naveen
	//Mail Builder
			public boolean sendApprovalMailBySequenceLevel(String savedGroupName,String email,String firstName,String lastName,String updatedDate,String approverComment,String ApproverEmail,String reminderMail){		
				
				ResultSet rs = null;
			    Connection  conn = null;
			    PreparedStatement pstmt = null;
			    HttpServletRequest request =ServletActionContext.getRequest();
			    HttpSession session = request.getSession();
			    String tempSubset = (String) session.getAttribute("userSubsetId");
				String SiteImgPath = (String) session.getAttribute("siteImagePath");
				int subsetId = CommonUtility.validateNumber(tempSubset);
				String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
				int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
				String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
				int userId = CommonUtility.validateNumber(sessionUserId);
				String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
				int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);	    
			    boolean flag = false;
			    
			    //String userName, String userPassword,String emailId,String firstName,String lastName
			    ArrayList<SalesModel> salesOrderItem = new ArrayList<SalesModel>();
			    try {
		            conn = ConnectionManager.getDBConnection();
		        } catch (SQLException e) { 
		            e.printStackTrace();
		        }
		        try
		        {
		        	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		    		SimpleDateFormat actualDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		        	
		    		/*salesOrderItem = SalesDAO.sendApprovalMailDao(salesOrderItem, orderStatus, orderId, GroupId, userId, subsetId, generalSubset, buyingCompanyId);*/
			    
		        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("ApproveCartMailBySequenceLevel2");
		            VelocityContext context = new VelocityContext();
		          
		            String toEmailId = email;
		            
		            
		            context.put("lineItemList", salesOrderItem);
		            /*context.put("orderStatus", orderStatus);*/
		            context.put("firstName", firstName);
		            if(lastName!=null && lastName.trim().length() > 0){
		            	lastName = lastName.trim();
		            }else{
		            	lastName = "";
		            }
		            context.put("lastName", lastName);
		            context.put("Cart", savedGroupName);
		            if(!approverComment.equalsIgnoreCase("")){
		            context.put("ApprovedComment", "Approved Comment: "+approverComment);
		            }else{
		            	 context.put("ApprovedComment",approverComment);
		            }
		           
		            if(CommonUtility.validateString(updatedDate).length() > 0)
		            context.put("orderDate", actualDate.format(dateFormat.parse(updatedDate)));
		            
		            context.put("reminderMail",reminderMail);
		            
		            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
		            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
		            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
		            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
		            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
		            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
		            }
		            
		            StringWriter writer = new StringWriter();
		            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
		            
		            StringBuilder finalMessage= new StringBuilder();
		            finalMessage.append(writer.toString());

					String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
					//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
					if(notificationDetail.get("FROM_EMAIL")!=null){
						fromEmail = notificationDetail.get("FROM_EMAIL");
					}
					
					notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toEmailId);
					notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
					System.out.println("Mail:"+finalMessage.toString());
					flag = sendNotification(notificationDetail,propertyLoader("sentmailconfig.sendApprovalMail.subject",""), fromEmail, finalMessage.toString());
					String toMailList = notificationDetail.get("TO_EMAIL")+";"+toEmailId;
					
					String mailStatus = "N";
					if(flag){
						mailStatus = "Y";
					}
					boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList,propertyLoader("sentmailconfig.sendApprovalMail.subject",""), fromEmail, finalMessage, mailStatus, "Approve Cart By Sequence Level",null);
					System.out.println("Your Save Email in DB Status @ sendApprovalMail : "+saveMailStatus);
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
		        finally{
		        	ConnectionManager.closeDBResultSet(rs);
		        	ConnectionManager.closeDBPreparedStatement(pstmt);	
			    	ConnectionManager.closeDBConnection(conn);
		        }
					return flag;	
			}



}