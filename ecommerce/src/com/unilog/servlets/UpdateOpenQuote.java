package com.unilog.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.security.SecureData;
import com.unilog.utility.CommonUtility;





/**
 * Servlet implementation class UpdateOpenQuote
 */
public class UpdateOpenQuote extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateOpenQuote() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String ipaddress = request.getHeader("X-Forwarded-For");
		if (ipaddress  == null)
			ipaddress = request.getRemoteAddr();
		System.out.println("At Validate open quote access : " + ipaddress);
		String orderId = request.getParameter("OrderId");
		String customerId = request.getParameter("CustomerId");
		String comments = request.getParameter("Comment");
		String email = request.getParameter("email");
		String status = request.getParameter("status");
		
		
		System.out.println("----------------------------------------------------------");
		System.out.println("Request Parameters Recieved from Eclipse");
		System.out.println("----------------------------------------------------------");
		System.out.println("Order Id: " + orderId);
		System.out.println("Customer Id: " + customerId);
		System.out.println("To Email Id: "  + email);
		System.out.println("Comment: " + comments);
		System.out.println("Status: " + status);
		System.out.println("------------------End of request Parameters---------------");
		
		boolean updateOpenQuote = true;
		String statusDesc = "";
		Connection conn = null;
		String from = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
		try
		{
			conn = ConnectionManager.getDBConnection();
			LinkedHashMap<String,String> notification = getNotificationDetails(conn,"OPENQUOTESNOTIFICATION");
			System.out.println(notification.entrySet());
			int loop = 0;
		
			Address[] toAddress = null;
			Address[] ccAddress = null;
			Address[] bccAddress = null;
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
			if(validateString(orderId).trim().equalsIgnoreCase(""))
			{
				statusDesc = statusDesc + "ERP Order Id is empty";
				updateOpenQuote = false;
			}
			if(validateString(customerId).trim().equalsIgnoreCase(""))
			{
				
				statusDesc = statusDesc + "Customer Id is empty";
				updateOpenQuote = false;
			}
			
			if(updateOpenQuote)
			{
				String subject =  "Open Quote Id - " + orderId;
				String queryType = "Insert";
				if(checkOpenQuote(conn,orderId))
				{
					queryType = "Update";
				}
				int queryResult = updateOpenQuote(conn,queryType,orderId,customerId,email,status);
				
				if(queryResult>0)
				{
					if(!validateString(email).trim().equalsIgnoreCase(""))
					{
						toAddress = null;
						ccAddress = null;
						bccAddress = null;
						toAddress = new Address[1];
						toAddress[0] = new InternetAddress(email);
						sendMultipleMailRecepient(toAddress,subject, from, "Dear Customer, <br /> Your Open Quote is ready for Review. <br />"+comments, ccAddress, bccAddress);
					}
					else
					{
						subject = subject + ". Email not found.";
						String body = "Open Quote is ready for Review. <br /> The notification has not sent to customer. Please resend the notification";
						sendMultipleMailRecepient(toAddress, subject, from, body, ccAddress, bccAddress);
					}
				}
				else
				{
					String body = "Open Quote creation failed. Contact technical support.";
					if(notification.get("DESCRIPTION")!=null && !notification.get("DESCRIPTION").trim().equalsIgnoreCase(""))
						body =   notification.get("DESCRIPTION") + body;
					sendMultipleMailRecepient(toAddress, subject, from, body, ccAddress, bccAddress);
				}
			}
			else
			{
				String body = "<br />" + statusDesc;
				if(notification.get("DESCRIPTION")!=null && !notification.get("DESCRIPTION").trim().equalsIgnoreCase(""))
					body =   notification.get("DESCRIPTION") + body;
				
				sendMultipleMailRecepient(toAddress, "Open quote creation failed", from, body, ccAddress, bccAddress);
			}
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBConnection(conn);
		}
		
		
		
		
	}
	
	
	public boolean sendMultipleMailRecepient(Address[] toAddress,  String subject, String from, String body,Address[] ccAddress,Address[] bccAddress)
    {
		System.out.println("sending mail process :----");
        boolean result = false;
        String subject_data = subject;
        
        try {
       	 
       	 String mailSmtp = CommonDBQuery.getSystemParamtersList().get("MAILSMTP");
       	 	int emailRelayPort = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAILSMTPPORT"));
            Properties props = new Properties();
            props.put("mail.smtp.host", mailSmtp);
            props.put("mail.smtp.debug", "true");
         	props.put("mail.smtp.auth", "true");
         	props.put("mail.smtp.port",emailRelayPort);
         	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")).length()>0) {
                props.put("mail.smtp.ssl.trust", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")));
        	}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_FROMUSERNAME")).length()>0) {
	         	props.put("mail.smtp.starttls.enable", "true");
	         	props.put("mail.transport.protocol", "smtp");
	         	props.put("mail.smtp.ehlo", true);
			}
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(true);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
          
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
	
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {

		public PasswordAuthentication getPasswordAuthentication() {
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
	 
	
	private static int updateOpenQuote(Connection conn, String queryType, String orderId, String customerId, String email, String status)
	{
		int updatedRow = 0;
		PreparedStatement pstmt = null;
		String sql = "Insert into open_quotes (OPEN_QUOTES_ID,BUYING_COMPANY_ID,EMAILS_TOBE_NOTIFIED,STATUS,ERP_QUOTE_ID,UPDATE_DATETIME) values (OPEN_QUOTES_ID_SEQ.NEXTVAL,?,?,?,?,sysdate)";
		try
		{
			System.out.println("Query Type : " + queryType);	
			if(queryType!=null && queryType.trim().equalsIgnoreCase("Update"))
			{
				sql = "UPDATE open_quotes SET BUYING_COMPANY_ID = ?,EMAILS_TOBE_NOTIFIED=?,STATUS = ?, UPDATE_DATETIME = sysdate WHERE ERP_QUOTE_ID = ?";
			}
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, getBuyingCompanyId(conn, customerId));
				pstmt.setString(2, email);
				pstmt.setString(3, status);
				pstmt.setString(4, validateString(orderId));
				updatedRow = pstmt.executeUpdate();
		
	
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		
		return updatedRow;
	}
	
	private static boolean checkOpenQuote(Connection conn, String orderId)
	{
		boolean openQuoteExist = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			String sql = "SELECT OPEN_QUOTES_ID FROM open_quotes WHERE ERP_QUOTE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, validateString(orderId));
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				openQuoteExist = true;
			}
			
			
		}
		catch (Exception e) {
			
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			
		}
		
		return openQuoteExist;
	}
	
	private static int getBuyingCompanyId(Connection conn, String customerId)
	{
		int buyingCompanyId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			String sql = "SELECT BUYING_COMPANY_ID FROM BUYING_COMPANY WHERE ENTITY_ID = ? AND STATUS = 'A'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, validateNumber(customerId));
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				buyingCompanyId = rs.getInt("BUYING_COMPANY_ID");
			}
			
			
		}
		catch (Exception e) {
			
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			
		}
		
		
		return buyingCompanyId;
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	public static String validateString(String val)
	{
		try
		{
		if(val == null)
			val = "";
		}
		catch (Exception e) {
			val = "";
			e.printStackTrace();
			
		}
		
		return val;
	}
	
	public static int validateNumber(String val)
	{
		int returnVal = 0;
		try
		{
			returnVal = Integer.parseInt(val);
		}
		catch (Exception e) {
			returnVal = 0;
			
			
		}
		
		return returnVal;
	}
	
	
	public  LinkedHashMap<String,String> getNotificationDetails(Connection conn,String notificationName){
		LinkedHashMap<String,String> notification  = new LinkedHashMap<String, String>();
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			
			String sql = "SELECT * FROM CIMM_NOTIFICATIONS where NOTIFICATION_NAME=?";

			try
			{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, notificationName);
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					
					if(rs.getString("DESCRIPTION") != null)
					notification.put("DESCRIPTION", rs.getString("DESCRIPTION"));
					
					if(rs.getString("TO_EMAIL") != null)
					notification.put("TO_EMAIL", rs.getString("TO_EMAIL"));
					
					if(rs.getString("CC_EMAIL") != null)
					notification.put("CC_EMAIL", rs.getString("CC_EMAIL"));

					
					if(rs.getString("BCC_EMAIL") != null)
					notification.put("BCC_EMAIL", rs.getString("BCC_EMAIL"));
				

				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	
		}
		return notification;
	}

}
