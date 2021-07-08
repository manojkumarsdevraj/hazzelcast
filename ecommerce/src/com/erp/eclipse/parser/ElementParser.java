package com.erp.eclipse.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.unilog.database.CommonDBQuery;
import com.unilog.users.UsersModel;


/**
 * 
 *
 */
public class ElementParser {
	
public ArrayList<UsersModel> userDetailList = null;
	
	private Stack<UsersModel> objectStack = new Stack<UsersModel>();
	private String response = "Success";
	private UsersModel userDetail = null;
	
	private InputStream is;
	public ElementParser(InputStream stringBufferInputStream){
		userDetailList = new ArrayList<UsersModel>(); 
		this.is=stringBufferInputStream;
		excute();
	} 
	
void excute() {
  try {
 
     SAXParserFactory factory = SAXParserFactory.newInstance();
     SAXParser saxParser = factory.newSAXParser();
 
     DefaultHandler handler = new DefaultHandler() {
     private Stack<String> elementStack = new Stack<String>();
      
     public void startElement(String uri, String localName,
        String qName, Attributes attributes)
        throws SAXException {
    	 this.elementStack.push(qName);
    		
	        if(qName.equalsIgnoreCase("StatusResult")){     	
	        	
	        	if(attributes.getValue("Success").equalsIgnoreCase("No")){
	        		response = "Failure";
	        		userDetail = new UsersModel();
	        		userDetailList.add(userDetail);
	        	}else{
	        		response = "Success";
	        	}
	        }
	        if(qName.equalsIgnoreCase("SessionID")){
	        	userDetail = new UsersModel();
	        	objectStack.push(userDetail);
	        }
	        
	        }
 
    public void endElement(String uri, String localName,
		        String qName) throws SAXException {

		        this.elementStack.pop();

		        if(qName.equalsIgnoreCase("SessionID")){
		        	userDetailList.add(objectStack.pop());
		        }
		    	 }
    public void characters(char ch[], int start, int length)
         throws SAXException {
    	 String value = new String(ch, start, length).trim();
    	
         if(value.length() == 0) return; // ignore white space

         if(response.equalsIgnoreCase("Success")){
        	 if("ElementSetupUrl".equalsIgnoreCase(currentElement())){
        		userDetail.setElementSetupUrl(value);
        	 }else  if("ElementSetupId".equalsIgnoreCase(currentElement())){
        		userDetail.setElementSetupId(value);
        	 }else  if("ElementPaymentAccountId".equalsIgnoreCase(currentElement())){
        		userDetail.setElementPaymentAccountId(value);
        	 }else  if("PostalCode".equalsIgnoreCase(currentElement())){
        		userDetail.setZipCode(value);
        	 }else  if("StreetAddress".equalsIgnoreCase(currentElement())){
        		userDetail.setAddress1(value);
        	 }else  if("CardHolder".equalsIgnoreCase(currentElement())){
        		userDetail.setCardHolder(value);
        	 }else  if("Date".equalsIgnoreCase(currentElement())){
        		userDetail.setDate(value);
        	 }else  if("CreditCardNumber".equalsIgnoreCase(currentElement())){
        		userDetail.setCreditCardNumber(value);
        	 }else  if("CreditCardType".equalsIgnoreCase(currentElement())){
         		userDetail.setCreditCardType(value);
         	 }
        	 
             }
        }
     private String currentElement() {
	        return this.elementStack.peek();
	    }
    
     public InputSource resolveEntity(String publicId, String systemId)
		throws IOException, SAXException {
	
    	 InputSource isrc = new InputSource(new FileInputStream(new File(CommonDBQuery.getSystemParamtersList().get("DTD_FILE_PATH"))));
	
    	 	return isrc;

     	}
     
    };
 
      saxParser.parse(is, handler);
      this.setUserDetail(userDetail);
 
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


/**
 * @return the userDetail
 */
public UsersModel getUserDetail() {
	return userDetail;
}

/**
 * @param userDetail the userDetail to set
 */
public void setUserDetail(UsersModel userDetail) {
	this.userDetail = userDetail;
}


}
