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
import com.unilog.utility.CommonUtility;


public class LoginLogoutParser {
public ArrayList<UsersModel> userDetailList = null;
	
	private String response = "Success";
	private UsersModel userDetail = null;
	String value;
	
	private InputStream is;
	public LoginLogoutParser(InputStream stringBufferInputStream){
		userDetailList = new ArrayList<UsersModel>(); 
		userDetail = new UsersModel();
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
    	 value = "";
    	    if(qName.equalsIgnoreCase("StatusResult")){     	
	        	
	        	if(attributes.getValue("Success").equalsIgnoreCase("No")){
	        		response = "Failure";
	        		userDetailList.add(userDetail);
	        	}else{
	        		response = "Success";
	        	}
	        }
	       }
     public void endElement(String uri, String localName,
		        String qName) throws SAXException {
		        if(response.equalsIgnoreCase("Success")){
		        	 if("SessionID".equalsIgnoreCase(currentElement())){
		        		userDetail.setSessionID(value);
		        	 }else  if("Description".equalsIgnoreCase(currentElement())){
		        		 userDetail.setDescription(value);
		        	 }else  if("EntityID".equalsIgnoreCase(currentElement())){
		        		 userDetail.setEntityName(value);
		        	 }else  if("ContactID".equalsIgnoreCase(currentElement())){
		        		 userDetail.setContactId(CommonUtility.validateString(value));
		        	 }
		        	 
		             }
		        this.elementStack.pop();
	}
    public void characters(char ch[], int start, int length)
         throws SAXException {
    	value = value + new String(ch, start, length).trim();
    	
         if(value.length() == 0) return; // ignore white space

       
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
