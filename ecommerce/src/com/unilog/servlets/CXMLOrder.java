package com.unilog.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.eclipse.erpinterfacemethods.ErpInterfaceMethods;
import com.unilog.cxml.models.reqresp.PurchaseOrderRequestDetails;
import com.unilog.cxmlinterfacemethods.CxmlInterfaceMethods;
import com.unilog.database.CommonDBQuery;
import com.unilog.exception.RestServiceException;
import com.unilog.security.SecureData;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;
import com.erp.service.rest.util.HttpConnectionUtility;
import com.erp.service.rest.util.HttpDeleteWithBody;

/**
 * Servlet implementation class CXMLOrder
 */
public class CXMLOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	   //@EJB (mappedName = "cXMLAsEjbService")
	   // private static CxmlInterfaceMethods  cxmlInterfaceMethods;

	    //@EJB (mappedName = "EclipseErpAsEjbService")
	   // private static ErpInterfaceMethods  erpInterfaceMethods;
	    

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CXMLOrder() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        System.out.println("============================DoGet--------------------------");
        handleRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		  System.out.println("============================DoPost--------------------------");
	        System.out.println("request from : --"+request.getRequestURI().toString());
	        System.out.println("path from : --"+request.getPathInfo());
	        System.out.println("path from : --"+request.getQueryString());
	        doGet(request, response);
	}
	
	 private static void handleRequest(HttpServletRequest request, HttpServletResponse response)
	    {   
		 if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).length()>0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("cimmesb")){
			 	processPunchoutOrderInESB(request, response);
		 }else{
			 	//processPunchoutOrder(request, response);
		 }
	    }
	 
	 private static void processPunchoutOrderInESB(HttpServletRequest request, HttpServletResponse response){
		 
		 String esbBaseUrl = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CIMMESB_ECOMM_BASE_URL"));
		 String cxmlOrderUrl= esbBaseUrl + CommonUtility.validateString(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CXMLESB_ORDER_API")));
		 System.out.println("Request url : " + cxmlOrderUrl);

			try {

				URL urlString = new URL(cxmlOrderUrl);
				HttpURLConnection connection = (HttpURLConnection) urlString.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
				String data = "";
				 InputStream stream = request.getInputStream();
		            data = readPurchaseOrderDataFromStream(stream);
				osw.write(String.format(data));
				osw.flush();
			    osw.close();
				CloseableHttpClient client=null;
				int timeout =180000;
				RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).build();
				 client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				HttpEntity entity=new StringEntity(data);
						HttpPost httpRequest = new HttpPost(cxmlOrderUrl);
						httpRequest.setEntity(entity);
					
						httpRequest.setHeader("content-type","application/xml");
					CloseableHttpResponse closeResponse = client.execute(httpRequest);
					String result = EntityUtils.toString(closeResponse.getEntity()).trim();
					System.out.println(result);
					response.setHeader("content-type","application/xml");
					response.getWriter().write(result);
					response.getWriter().flush();
					response.getWriter().close();
		       
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 
	/* private static void processPunchoutOrder(HttpServletRequest request, HttpServletResponse response){
		 String data = null;
	        try {
	            InputStream stream = request.getInputStream();
	            data = readPurchaseOrderDataFromStream(stream);
	            System.out.println(data);
	            if(data!=null && data.trim().length()>0)
	            {
	                
	                sendSalesOrderMessage("New CXML Order Received. In process...", data);
	                String ipaddress = request.getHeader("X-Forwarded-For");
	                
	                if (ipaddress  == null)
	                    ipaddress = request.getRemoteAddr();
	                
	                String reqUri = request.getRequestURI().toString();
	                String queryString = request.getQueryString();
	                System.out.println("Request Url : " + reqUri + "?"+ queryString);
	                
	                StringBuilder dataVal1 = new StringBuilder(data);
	                UsersDAO.saveFlatFileInDB(reqUri + "?" + (queryString == null ? "" : queryString), ipaddress, dataVal1, request.getContentType());    
	                System.out.println("Sending data to cxml EJB--------------"+data);
	                PurchaseOrderRequestDetails purchaseOrderRequestParsedData = cxmlInterfaceMethods.purchaseOrderRequestHandler(data);
	                
	                if(purchaseOrderRequestParsedData!=null)
	                {
	                    if(purchaseOrderRequestParsedData.getErrorMessageString()!=null)
	                    {
	                        SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	                        Date date = new Date();
	                        String timeStampDate = timeStamp.format(date);

	                        timeStampDate= timeStampDate.substring(0, timeStampDate.length()-2)+":"+timeStampDate.substring(timeStampDate.length()-2, timeStampDate.length());
	                        String payLoadId = purchaseOrderRequestParsedData.getPayloadId();
	                        if(payLoadId==null)
	                            payLoadId = "e3b3cc7e-7d84-47e1-a380-6ea4eaed1481";
	                        String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE cXML SYSTEM \"http://xml.cxml.org/schemas/cXML/1.2.018/cXML.dtd\"><cXML xml:lang=\"en-US\" timestamp=\""+timeStampDate+"\" payloadID=\""+payLoadId+"\" version=\"1.2.018\"><Response><Status text=\"OK\" code=\"200\"/></Response></cXML>";
	                        sendSalesOrderMessage("Parsing Error : " +purchaseOrderRequestParsedData.getErrorMessageString() , purchaseOrderRequestParsedData.getErrorMessageString()+" Please contact customer support. <br />" + data);
	                        response.setContentType("text/xml");
	                        response.getWriter().write(res);
	                        response.flushBuffer();
	                    }

	                    if(purchaseOrderRequestParsedData.getErrorMessageString()==null)
	                    {
	                        
	                        purchaseOrderRequestParsedData.getpCardHolderName();
	                        if(purchaseOrderRequestParsedData.getpCardHolderName()!=null)
	                        {
	                        	SecureData secureData = new SecureData();
	                            String encryptedCardNumber = purchaseOrderRequestParsedData.getpCardNumber().trim();
	                            encryptedCardNumber=secureData.validatePassword(encryptedCardNumber.substring(0, encryptedCardNumber.length()-4))
	                            +encryptedCardNumber.substring(encryptedCardNumber.length()-4,encryptedCardNumber.length());
	                            System.out.println("purchaseOrderRequestParsedData.getpCardNumber() : " + purchaseOrderRequestParsedData.getpCardNumber());
	                            data = data.replace(encryptedCardNumber,purchaseOrderRequestParsedData.getpCardNumber());
	                        }
	                        StringBuilder dataVal = new StringBuilder(data);
	                        UsersDAO.saveCXMLOrderInDB(purchaseOrderRequestParsedData.getOrderId(), purchaseOrderRequestParsedData.getCxmlHeaderReqResp().getFromCredentialsDetails().getFromDetailsList().get(0).getCredentialIdentity(), dataVal);
	                        System.out.println("Order Number : " + purchaseOrderRequestParsedData.getOrderId());
	                        System.out.println("Replaced Data : " + data);

	                        response.setContentType("text/xml");
	                        response.getWriter().write(purchaseOrderRequestParsedData.getSucessMessageString());
	                        response.flushBuffer();

	                        System.out.println("Sending data to ECLIPSE EJB--------------");
	                        HashMap<String,String> cxmlPurchaseOrderResponse=erpInterfaceMethods.cxmlPurchaseOrderRegister(purchaseOrderRequestParsedData);
	                        if(cxmlPurchaseOrderResponse.size()>0)
	                        {

	                            for(Entry<String, String> dataResponse : cxmlPurchaseOrderResponse.entrySet())
	                            {
	                                System.out.println("Key:---"+dataResponse.getKey()+"\tValue  "+dataResponse.getValue());
	                            }
	                            
	                        }
	                }
	                }

	                
	            }
	            else
	            {
	                //Commented to skip sending the mail wrt Error reading data from steam when cXML is null.
	                //sendSalesOrderMessage("Error reading data from stream.", "Error reading data from stream. Please contact customer support.");  
	                
	            }
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            sendSalesOrderMessage("Parsing Error","Parsing Error :  Please contact customer support. <br />" + data);
	            e.printStackTrace();
	        }
	 }*/
	 public static void sendSalesOrderMessage(String cxmlSubject,String cxmlMessage)
	    {
	        LinkedHashMap<String, String> systemParamtersList = CommonDBQuery.getSystemParamtersList();

	        //SecureSpecificEclipseData.unSecureData(cxmlUserData.getString("PASSWORD"))
	        
	        SecureData getPassword = new SecureData();
	        String userPassword=getPassword.validatePassword(systemParamtersList.get("EMAILRELAY"));
	        HtmlEmail email = new HtmlEmail();
	        email.setSentDate(new Date());
	        email.setTLS(true);
	        email.setHostName(userPassword);
	        email.setSmtpPort(Integer.parseInt(systemParamtersList.get("INT_SYS_NOTIFY_EMAIL_SMTP_PORT")));
	        
	        try
	        {
	            ArrayList<InternetAddress> toListCollection = new ArrayList<InternetAddress>();
	            if (systemParamtersList.get("SENDCXMLERRORVIAEMAIL").trim().length() > 0)
	            {
	                String[] toList = systemParamtersList.get("SENDCXMLERRORVIAEMAIL").trim().split(";", -1);

	                for (int i = 0; i < toList.length; i++)
	                {
	                    toListCollection.add(new InternetAddress(toList[i]));
	                }
	            }
	            email.setTo(toListCollection);

	            if (systemParamtersList.get("INT_SYS_NOTIFY_EMAIL_ID_BCC").trim().length() > 0)
	            {
	                String[] bccList = systemParamtersList.get("INT_SYS_NOTIFY_EMAIL_ID_BCC").trim().split(";", -1);
	                ArrayList<InternetAddress> bccListCollection = new ArrayList<InternetAddress>();

	                for (int i = 0; i < bccList.length; i++)
	                {
	                    bccListCollection.add(new InternetAddress(bccList[i]));
	                }
	                email.setBcc(bccListCollection);
	            }
	            email.setFrom(systemParamtersList.get("FROMMAILID"));
	            email.setSubject(cxmlSubject);
	            email.setMsg(cxmlMessage);

	            // send the email
	            email.send();
	        } catch (AddressException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (EmailException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	    }
	 
	 private static String readPurchaseOrderDataFromStream(InputStream inputData)
	    {
	        String message="";
	        String requestData ="";
	         try {
	            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputData));

	             String data;
	             //requestData.append(data);

	             while ((data=bufferedReader.readLine()) != null)
	             {
	                 requestData+=data;
	             }
	             message="CXML Response - Supplyforce";

	        } catch (IOException e) {
	            // TODO Auto-generated catch block

	            message="CXML Response - Supplyforce - ERROR";
	            requestData="";
	             for(StackTraceElement stackTraceElement: e.getStackTrace())
	                {
	                 requestData+=stackTraceElement+"<br/>";
	                }

	            e.printStackTrace();
	        }
	        catch(Exception e)
	        {
	            requestData="";
	            message="CXML Response - Supplyforce - ERROR";
	            for(StackTraceElement stackTraceElement: e.getStackTrace())
	            {
	                requestData+=stackTraceElement+"<br/>";
	            }
	          e.printStackTrace();
	        }
	        /*finally{
	             SendCxmlRelatedEmails.sendSalesOrderMessage(message, requestData.toString());
	        }*/

	         return requestData.toString();
	    }

	    private static void sendCxmlResponse(String cxmlPurchaseOrderResponse)
	    {
	         URLConnection cxmlConn = null;
	            OutputStream os = null;
	            BufferedReader responseData = null;
	            try {

	                cxmlConn = new URL("https://service.ariba.com/service/transaction/cxml.asp").openConnection();

	                cxmlConn.setDoOutput(true);

	                os = cxmlConn.getOutputStream();
	                os.write(cxmlPurchaseOrderResponse.getBytes());
	                os.close();
	            } catch (MalformedURLException ex) {
	                ex.printStackTrace();
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            } catch (Exception e)
	            {
	                 e.printStackTrace();
	            }
	            finally
	            {
	                try {
	                    os.close();
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }

	           


	    }
	    

}
