package com.unilog.misc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.unilog.users.UsersModel;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class StHTTPRequest {
	
	private static final Logger log = Logger.getLogger(StHTTPRequest.class);

    private String responseBody = "";
   
    private OAuthConsumer consumer = null;

    /** Default Constructor */
    public StHTTPRequest() { }
   
    public StHTTPRequest(OAuthConsumer consumer) {
        this.consumer = consumer;
    }

    public HttpURLConnection getConnection(String url) 
    throws IOException,
        OAuthMessageSignerException,
        OAuthExpectationFailedException, 
        OAuthCommunicationException
    {
     try {
             URL u = new URL(url);

             HttpURLConnection uc = (HttpURLConnection) u.openConnection();
             
             if (consumer != null) {
                 try {
                     log.info("Signing the oAuth consumer");
                     consumer.sign(uc);
                     
                 } catch (OAuthMessageSignerException e) {
                     log.error("Error signing the consumer", e);
                     throw e;

                 } catch (OAuthExpectationFailedException e) {
                 log.error("Error signing the consumer", e);
                 throw e;
                 
                 } catch (OAuthCommunicationException e) {
                 log.error("Error signing the consumer", e);
                 throw e;
                 }
                 uc.connect();
             }
             return uc;
     } catch (IOException e) {
     log.error("Error signing the consumer", e);
     throw e;
     }
    }
    
    /**
     * Sends an HTTP GET request to a url
     *
     * @param url the url
     * @return - HTTP response code
     */
    public UsersModel sendGetRequest(UsersModel locaDetail) 
    throws IOException,
    OAuthMessageSignerException,
    OAuthExpectationFailedException, 
    OAuthCommunicationException {
    
    	//UsersModel locaDetail = new UsersModel();
        int responseCode = 500;
        try {
            HttpURLConnection uc = getConnection(locaDetail.getElementSetupUrl());
            responseCode = uc.getResponseCode();
            locaDetail.setResultCount(responseCode);
            if(200 == responseCode || 401 == responseCode || 404 == responseCode){
                BufferedReader rd = new BufferedReader(new InputStreamReader(responseCode==200?uc.getInputStream():uc.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                setResponseBody(sb.toString());
                try {
                	 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                     DocumentBuilder builder = factory.newDocumentBuilder();
                     InputSource is = new InputSource(new StringReader(sb.toString()));
                     Document doc = builder.parse(is);
                     NodeList nodeList = doc.getElementsByTagName("latitude");
                     Node node = nodeList.item(0);
                     NodeList fstNm = node.getChildNodes();
                     YahooBossSupport.setLatitude((fstNm.item(0)).getNodeValue());
                     locaDetail.setLatitude((fstNm.item(0)).getNodeValue());
                     nodeList = doc.getElementsByTagName("longitude");
                     node = nodeList.item(0);
                     fstNm = node.getChildNodes();
                     YahooBossSupport.setLongitude((fstNm.item(0)).getNodeValue());
                     locaDetail.setLongitude((fstNm.item(0)).getNodeValue());
                     nodeList = doc.getElementsByTagName("city");
                     node = nodeList.item(0);
                     fstNm = node.getChildNodes();
                     locaDetail.setCity((fstNm.item(0)).getNodeValue());
                     //YahooBossSupport.setCity((fstNm.item(0)).getNodeValue());
                     nodeList = doc.getElementsByTagName("country");
                     node = nodeList.item(0);
                     fstNm = node.getChildNodes();
                     locaDetail.setCountry((fstNm.item(0)).getNodeValue());
                     //YahooBossSupport.setCounty((fstNm.item(0)).getNodeValue());
                     nodeList = doc.getElementsByTagName("state");
                     node = nodeList.item(0);
                     fstNm = node.getChildNodes();
                     locaDetail.setState((fstNm.item(0)).getNodeValue());
                     //YahooBossSupport.setState((fstNm.item(0)).getNodeValue());
                }catch(Exception e)
                {
                    	e.printStackTrace();
                }
                rd.close();
            }
         } catch (MalformedURLException ex) {
            throw new IOException( locaDetail.getElementSetupUrl() + " is not valid");
        } catch (IOException ie) {
            throw new IOException("IO Exception " + ie.getMessage());
        }
        return locaDetail;
    }


    /**
     * Return the Response body
     * @return String
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * Setter
     * @param responseBody
     */
    public void setResponseBody(String responseBody) {
        if (null != responseBody) {
            this.responseBody = responseBody;
        }
    }
   
    /**
     * Set the oAuth consumer
     * @param consumer
     */
    public void setOAuthConsumer(OAuthConsumer consumer) {
        this.consumer = consumer;
    }

}
