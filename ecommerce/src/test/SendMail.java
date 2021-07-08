package test;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendMail {
	public static void main(String args[]){
		try {
    		
			
			//String mailSmtp = "nglantz5.nglantz.com";
			//String mailSmtp = "smtp.net4india.com";
			//String mailSmtp = "207.86.30.99";//"mail.bk-eklectric.com";
			String mailSmtp = "mail.bk-eklectric.com";

             Properties props = new Properties();
             props.put("mail.smtp.host", mailSmtp);
             props.put("mail.smtp.auth", "false");
             props.put("mail.smtp.port", "25");


            Session session = Session.getInstance(props);
			MimeMessage message = new MimeMessage(session);
            	
			
			//String from =  "customerservice@nglantz.com";
			//String from =  "webmail@cimm2.com";
			String from =  "JDavis@bk-electric.com";
			
		
            message.setFrom(new InternetAddress(from));
            
            message.addRecipients(Message.RecipientType.TO,"bharath.ks@unilogcorp.com" );
            
            
            message.setSubject("Test email");
            message.setContent("Testing Email Relay","text/html");

          	message.setSentDate(new Date());

          	Transport.send(message);
          	 System.out.println("message sent successfully...");
           
           
        } catch (Exception e) {
       	 e.printStackTrace();
          
        }
	}

	/*
	
	com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.1 Client was not authenticated

	at com.sun.mail.smtp.SMTPTransport.issueSendCommand(SMTPTransport.java:1388)
	at com.sun.mail.smtp.SMTPTransport.mailFrom(SMTPTransport.java:959)
	at com.sun.mail.smtp.SMTPTransport.sendMessage(SMTPTransport.java:583)
	at javax.mail.Transport.send0(Transport.java:169)
	at javax.mail.Transport.send(Transport.java:98)
	at test.SendMail.main(SendMail.java:49)

	
	
	*/
	
	
}
