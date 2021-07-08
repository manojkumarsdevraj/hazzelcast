package com.unilog.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
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

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.security.SecureData;
import com.unilog.utility.CommonUtility;

public class SendDetailMailUtility {
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
			System.out.println("----------Authenticating----------");
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
            
            if(CommonUtility.validateNumber(CommonUtility.validateString(notificationDetail.get("CMS_STATIC_PAGE_ID")))>0) {
                finalMessage.append(new SendMailUtility().getStaticPageRenderedContent(context,CommonUtility.validateNumber(CommonUtility.validateString(notificationDetail.get("CMS_STATIC_PAGE_ID")))));
            }

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
	
	public boolean sendNotification(LinkedHashMap<String, String> notification, String subject,String from, String body){
		System.out.println("sending mail process :----");
        boolean result = false;
        String subject_data = subject;
        HttpServletRequest request = ServletActionContext.getRequest();
 		HttpSession session1 = request.getSession();//spam email change
 		String sessionUserId = (String) session1.getAttribute(Global.USERID_KEY);
 		int userId = CommonUtility.validateNumber(sessionUserId);
         
        if(CommonDBQuery.getSystemParamtersList().get("MAILSELECT")!=null && CommonDBQuery.getSystemParamtersList().get("MAILSELECT").trim().equalsIgnoreCase("MODIFIED"))
        {
        	System.out.println("Inside New Mail Code....");
        	result = sendSalesOrderMessageNew(notification, subject_data, from, body);
        }
        else
        {
        	try {
        		System.out.println("Inside Old Mail Code....");
           	 	
        		//SecureData validUserPass = new SecureData();
      			//String emailRelayEncrpt = CommonDBQuery.getSystemParamtersList().get("EMAILRELAY");
      			
      			 String mailSmtp = CommonDBQuery.getSystemParamtersList().get("MAILSMTP");
      			int emailRelayPort = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAILSMTPPORT"));
    	        
      			/*spam email changes start*/
      			int maxAllowedRequest = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAXIMUM_REQUEST_MAIL"));
      			long timeInterval = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TIME_INTERVAL_MAIL"));
      			long current_time = System.currentTimeMillis();
      			long last_mail_time = 0;
      			if(session1.getAttribute("last_sent_time")!=null){
      			last_mail_time = (Long) session1.getAttribute("last_sent_time");
      			}
      			String reqestCount = (String) session1.getAttribute("reqestCount");
      			if(CommonUtility.validateString(reqestCount).equalsIgnoreCase("")){
      				session1.setAttribute("reqestCount", "0");
      			}
      			int count = CommonUtility.validateNumber(reqestCount);
      			/*spam email changes end*/
      			if(emailRelayPort==0){
   	        	 emailRelayPort = 25;
      			}
                 Properties props = new Properties();
                props.put("mail.smtp.host",mailSmtp);
              	props.put("mail.smtp.port",emailRelayPort);
              	props.put("mail.smtp.auth", "true");
            	props.put("mail.smtp.debug", "true");
            	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")).length()>0) {
                    props.put("mail.smtp.ssl.trust", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")));
            	}
              	/**
    			 *Below code Written is for Tyndale *Reference- Chetan Sandesh
    			 */
              	/**
    			 *Below code Written is for Tyndale *Reference- Chetan Sandesh
    			 */
              	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_FROMUSERNAME")).length()>0) {
              	props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.ehlo", true);
              	}
                 Authenticator auth = new SMTPAuthenticator();
                 Session session = Session.getInstance(props, auth);

                MimeMessage message = new MimeMessage(session);
                Address[] toAddress = null;
    			Address[] ccAddress = null;
    			Address[] bccAddress = null;
    			String toMailList ="";
     			String ccMailList ="";
     			String bccMailList ="";
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
    				toMailList = notification.get("TO_EMAIL");
    				
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
    				ccMailList = notification.get("CC_EMAIL");
    				
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
    				bccMailList = notification.get("BCC_EMAIL");
    				
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
               
                Multipart mp = new MimeMultipart();

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(new HTMLDataSource(body)));
                mp.addBodyPart(messageBodyPart);

                // Put parts in message
                message.setContent(mp);

                // Send the message
                /*Transport.send(message);
                result = true;*/
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
                	UsersSendMailDAO.saveSpamEmailsInDB(toMailList, ccMailList, bccMailList, sessionUserId, subject);
                 }
    	         /*spam email changes end*/
            } catch (Exception e) {
           	 e.printStackTrace();
              
            }
        }
         
        System.out.println("sending mail process  completed:----");
        return result;
    }
	
	public boolean sendSalesOrderMessageNew(LinkedHashMap<String, String> notification,  String subject, String from, String body){
		   
		System.out.println("sending mail process :----");
        boolean result = false;
        String subject_data = subject;
    	HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session1 = request.getSession();//spam email change
		String sessionUserId = (String) session1.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
        

       try {
        	SecureData validUserPass = new SecureData();
 			String emailRelayEncrpt = CommonDBQuery.getSystemParamtersList().get("EMAILRELAY");
 			/*spam email changes start*/
 			int maxAllowedRequest = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAXIMUM_REQUEST_MAIL"));
 			long timeInterval = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TIME_INTERVAL_MAIL"));
 			long current_time = System.currentTimeMillis();
 			long last_mail_time = 0;
 			if(session1.getAttribute("last_sent_time")!=null){
 			last_mail_time = (Long) session1.getAttribute("last_sent_time");
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
 			String toMailList ="";
 			String ccMailList ="";
 			String bccMailList ="";
 			int loop = 0;
 			if(notification.get("TO_EMAIL")!=null && !notification.get("TO_EMAIL").trim().equalsIgnoreCase("")){
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
 				toMailList = notification.get("TO_EMAIL");
 				
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
 						bccAddress[loop] = new InternetAddress(toAddr.trim());
 						loop++;
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
            	UsersSendMailDAO.saveSpamEmailsInDB(toMailList, ccMailList, bccMailList, sessionUserId, subject);
             }
	         /*spam email changes end*/
         }catch (Exception e) {
        	 e.printStackTrace();
         }
         return result;
    }

}
