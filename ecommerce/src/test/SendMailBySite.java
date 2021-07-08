package test;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class SendMailBySite {
 public static void main(String[] args) {

  String host="nglantz18.nglantz.com";
  final String user="customerservice";//change accordingly
  final String password="!EcommUlog";//change accordingly
  
  String to="bharath.ks@unilogcorp.com";//change accordingly
  String from =  "customerservice@nglantz.com";
   //Get the session object
   Properties props = new Properties();
   props.put("mail.smtp.host",host);
   props.put("mail.smtp.auth", "false");
   
  /* Session session = Session.getDefaultInstance(props,
    new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
	return new PasswordAuthentication(user,password);
      }
    });*/

   //Compose the message
    try {
    // MimeMessage message = new MimeMessage(session);
     //message.setFrom(new InternetAddress(user));
     
     Session session = Session.getInstance(props);
	 MimeMessage message = new MimeMessage(session);
     
     message.setFrom(new InternetAddress(from));
     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
     message.setSubject("javatpoint");
     message.setContent("Testing Email Relay","text/html");
     
    //send the message
     Transport.send(message);

     System.out.println("message sent successfully...");
 
     } catch (MessagingException e) {e.printStackTrace();}
 }
}
