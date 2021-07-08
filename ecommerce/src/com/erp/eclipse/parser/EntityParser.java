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
import com.unilog.users.AddressModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
public class EntityParser {
	
	public ArrayList<UsersModel> userDetailList = null;
	private Stack<UsersModel> objectStack = new Stack<UsersModel>();
	private String response = "Success";
	private UsersModel userDetail = null;
	public String value = "";
	private boolean flag = false;
	private ArrayList<String> shipList;
	private ArrayList<String> contactList;
	private ArrayList<AddressModel> contactShortList;
	private ShipVia shipVia = null;
	private ArrayList<ShipVia> shipViaLists;
	private UsersModel orderStatus = null;
	private ArrayList<UsersModel> orderStatusLists;
	private AddressModel contactShort;
	private ArrayList<String> customerType =null;
	
	private InputStream is;
	public EntityParser(InputStream stringBufferInputStream){
		userDetailList = new ArrayList<UsersModel>(); 
		contactList = new ArrayList<String>();
		contactShortList = new ArrayList<AddressModel>();
		shipList = new ArrayList<String>();
		shipViaLists = new ArrayList<ShipVia>();
		orderStatusLists = new ArrayList<UsersModel>();
		customerType = new ArrayList<String>();
		this.is=stringBufferInputStream;
		excute();
	} 
	
	void excute() {
	try {
	
	 SAXParserFactory factory = SAXParserFactory.newInstance();
	 SAXParser saxParser = factory.newSAXParser();
	
	 DefaultHandler handler = new DefaultHandler() {
	 private Stack<String> elementStack = new Stack<String>();
	   boolean contactid = false;
	   boolean address = false;
	   boolean billto = false;
	   boolean statusResult = false;
	   boolean entityList = false;
	   boolean contact =false;
	   boolean contactName = false;
	   boolean ContactShort = false;
	   boolean shipViaList = false;
	   boolean orderStatusListTag = false;
	   boolean orderStatusTag = false;
	   boolean entityRemoteData = false;
	   boolean aRPassword = false;
	   boolean CustomerType=false;
	   boolean loginMessage = false;
	   boolean PricingDecimalPlaces =false;
	 public void startElement(String uri, String localName,
	    String qName, Attributes attributes)
	    throws SAXException {
		 this.elementStack.push(qName);
		 value = "";
	        if(qName.equalsIgnoreCase("StatusResult")){     	
	        	statusResult= true;
	        	if(attributes.getValue("Success").equalsIgnoreCase("No")){
	        		response = "Failure";
	        		userDetail = new UsersModel();
	        		shipVia = new ShipVia();
	        		orderStatus = new UsersModel();
	        		userDetailList.add(userDetail);
	        	}else{
	        		response = "Success";
	        	}
	        }
	        if(qName.equalsIgnoreCase("SessionID")){
	        	userDetail = new UsersModel();
	        	shipVia = new ShipVia();
	        	orderStatus = new UsersModel();
	        	objectStack.push(userDetail);
	        }
	        if(qName.equalsIgnoreCase("ShipToList"))
	        {
	        	flag = true;
	        }
	        if(qName.equalsIgnoreCase("PricingDecimalPlaces"))
	        {
	        	PricingDecimalPlaces=true;
	        }
	        
	        if(qName.equalsIgnoreCase("ContactShortList"))
	        {
	        	contactid = true;
	        }
	        
	        if(qName.equalsIgnoreCase("ContactShort"))
	        {
	        	ContactShort = true;
	        	contactShort = new AddressModel();
	        }
	        
	        if(qName.equalsIgnoreCase("Address"))
	        {
	        	address = true;
	        }
	        if(qName.equalsIgnoreCase("Contact"))
	        {
	        	contact = true;
	        }
	        
	        if(qName.equalsIgnoreCase("ContactName"))
	        {
	        	contactName = true;
	        }	        
	        if(qName.equalsIgnoreCase("ShipViaList"))
	        {
	        	shipViaList = true;
	        }
	        if(qName.equalsIgnoreCase("billto"))
	        {
	        	billto = true;
	        }
	        if(qName.equalsIgnoreCase("EntityNoteList"))
	        {
	        	entityList = true;
	        }
	        if(qName.equalsIgnoreCase("Credit"))
	        {
	        	
	        
	        int length = attributes.getLength();

	        // Process each attribute
	        for (int i=0; i<length; i++) {
	            // Get names and values for each attribute
	            String name = attributes.getQName(i);
	            String value = attributes.getValue(i);
						System.out.print(name+":");
						System.out.println(value);
					if("Currency".equalsIgnoreCase(name)){
						userDetail.setCurrency(value);
					}
					
					if("CreditLimit".equalsIgnoreCase(name)){
						userDetail.setCreditLimit(Double.parseDouble(value));
					}
					if("OrderEntryOK".equalsIgnoreCase(name)){
						userDetail.setOrderEntryOk(value);
					}
					
	        }
	        }
	        
	        if(qName.equalsIgnoreCase("OrderStatusList"))
	        {
	        	orderStatusListTag = true;
	        	
	        }
	        
	        if(qName.equalsIgnoreCase("OrderStatusList"))
	        {
	        	orderStatusListTag = true;
	        	
	        }
	        
	        if(qName.equalsIgnoreCase("OrderStatus"))
	        {
	        	orderStatusTag = true;
	        	orderStatus.setOrderStatusCode(attributes.getValue("Code"));
	        	System.out.println(attributes.getValue("Code"));
	        }
	        if(qName.equalsIgnoreCase("EntityRemoteData")){
	        	entityRemoteData = true;
	        }
	        if(qName.equalsIgnoreCase("ARPassword")){
	        	aRPassword = true;
	        }
	        if(qName.equalsIgnoreCase("CustomerType")){
	        	CustomerType = true;
	        }
	        if(qName.equalsIgnoreCase("LoginMessage")){
	        	loginMessage = true;
	        }
	        
	      }
	
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if(response.equalsIgnoreCase("Success")){
			if("entityid".equalsIgnoreCase(currentElement())){
				if(flag){
					shipList.add(CommonUtility.validateString(value));
					userDetail.setShipIdList(shipList);
				}else if (billto){
					userDetail.setBillEntityId(CommonUtility.validateString(value));
				}else{
					userDetail.setEntityId(CommonUtility.validateString(value));
				}
			}else if("PricingDecimalPlaces".equalsIgnoreCase(currentElement())&&PricingDecimalPlaces){
	        	 userDetail.setPricePrecision(value);
	        }else if("EntityName".equalsIgnoreCase(currentElement())){
				userDetail.setEntityName(value);
			}else if("FirstName".equalsIgnoreCase(currentElement())){
				userDetail.setFirstName(value);
			}else if("LastName".equalsIgnoreCase(currentElement())){
				userDetail.setLastName(value);
			}else if("StreetLineOne".equalsIgnoreCase(currentElement())&& address){
				if(value!=null && !value.trim().equalsIgnoreCase(""))
					userDetail.setAddress1(value);
				else
					userDetail.setAddress1("N/A");
			}else if("StreetLineTwo".equalsIgnoreCase(currentElement()) && address){
				userDetail.setAddress2(value);
			}else if("City".equalsIgnoreCase(currentElement())&& address){
				userDetail.setCity(value);
			}else if("Country".equalsIgnoreCase(currentElement())&& address){
				userDetail.setCountry(value);
			}else if("PostalCode".equalsIgnoreCase(currentElement()) && address){
				userDetail.setZipCode(value);
			}else if("state".equalsIgnoreCase(currentElement()) && address){
				userDetail.setState(value);
			}else if("SessionID".equalsIgnoreCase(currentElement())){
				userDetail.setSessionID(value);
			}else if("FirstName".equalsIgnoreCase(currentElement()) && contact &&  contactName){
				userDetail.setFirstName(value);
			}else if("ContactID".equalsIgnoreCase(currentElement())){
				contactList.add(CommonUtility.validateString(value));
				userDetail.setContactIdList(contactList);
			}else if("EmailAddress".equalsIgnoreCase(currentElement())){
				userDetail.setEmailAddress(value);
			}else  if("Description".equalsIgnoreCase(currentElement())&& contactid && ContactShort){
				//contactShortList.add(value);
				contactShort.setPhoneDescription(value);
				//userDetail.setContactShortList(contactShortList);
			}else  if("Number".equalsIgnoreCase(currentElement())&& contactid && ContactShort){
				/*contactShortList.add(value);
				userDetail.setContactShortList(contactShortList);*/
				contactShort.setPhoneNo(value);
			}else  if("Code".equalsIgnoreCase(currentElement())&& contactid && ContactShort){
				/*contactShortList.add(value);
				userDetail.setContactShortList(contactShortList);*/
				contactShort.setPhoneCode(value);
			}else if("Description".equalsIgnoreCase(currentElement())&&statusResult){
				if(userDetail.getStatusDescription()==null)
					userDetail.setStatusDescription(value.trim());
				else
					userDetail.setStatusDescription(userDetail.getStatusDescription()+value.trim());

			}

			else if("Type".equalsIgnoreCase(currentElement())&&entityList){
				if(value.contains("Vendor"))
					userDetail.setEntityType(value);
			}else if("ShipViaID".equalsIgnoreCase(currentElement())&&shipViaList){
				System.out.println("ship via val : " + value);
				shipVia = new ShipVia();
				shipVia.setShipViaID(value);
				shipViaLists.add(shipVia);

			}
			else if("BranchID".equalsIgnoreCase(currentElement())){
				userDetail.setBranchID(value);
			}else if("DefaultShipVia".equalsIgnoreCase(currentElement())){
				userDetail.setDefaultShipVia(value);
			}else if("OrderStatus".equalsIgnoreCase(currentElement())&&orderStatusTag && orderStatusListTag){
				orderStatus.setOrderStatus(value);
				System.out.println(value);
			} else if("BranchName".equalsIgnoreCase(currentElement())){
				userDetail.setBranchName(value);
			}else if("ARPassword".equalsIgnoreCase(currentElement()) && aRPassword && entityRemoteData){
				userDetail.setaRPassword(value);
			}else if("CustomerType".equalsIgnoreCase(currentElement()) && CustomerType){
				customerType.add(CommonUtility.validateString(value));
				userDetail.setCustomerTypeList(customerType);
			}else if("LoginMessage".equalsIgnoreCase(currentElement()) && loginMessage){
				userDetail.setLoginMessage(value);;
			}


		}
		else if("Description".equalsIgnoreCase(currentElement())&&statusResult){
			userDetail.setStatusDescription(value);
		}






		this.elementStack.pop();

		if(qName.equalsIgnoreCase("ShipToList")){
			userDetailList.add(objectStack.pop());
		}
		if(qName.equalsIgnoreCase("ContactShortList")){
			contactid = false;
			userDetail.setContactShortList(contactShortList);
		}
		if(qName.equalsIgnoreCase("ContactShort"))
		{
			ContactShort = false;
			contactShortList.add(contactShort);
			contactShort = new AddressModel();
		}
		if(qName.equalsIgnoreCase("Address"))
		{
			address = false;
		}
		if(qName.equalsIgnoreCase("Contact"))
		{
			contact = false;
		}
		if(qName.equalsIgnoreCase("ShipViaList"))
		{
			shipViaList = false;

		}
		if(qName.equalsIgnoreCase("ContactName"))
		{
			contactName = false;
		}	
		if(qName.equalsIgnoreCase("billto"))
		{
			billto = false;
		}
		if(qName.equalsIgnoreCase("StatusResult")){     	
			statusResult= false;
		}
		if(qName.equalsIgnoreCase("OrderStatusList"))
		{
			orderStatusListTag = false;

		}
		if(qName.equalsIgnoreCase("OrderStatus"))
		{
			orderStatusTag = false;
			orderStatusLists.add(orderStatus);
			orderStatus = new UsersModel();
		}
		if(qName.equalsIgnoreCase("EntityRemoteData")){
			entityRemoteData = false;
		}
		if(qName.equalsIgnoreCase("ARPassword")){
			aRPassword = false;
		}
		
		if(qName.equalsIgnoreCase("CustomerType")){
			CustomerType = false;
		}
		if(qName.equalsIgnoreCase("LoginMessage")){
        	loginMessage = false;
        }

		
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
	  this.setShipViaLists(shipViaLists);
	  this.setOrderStatusLists(orderStatusLists);
	
	} catch (Exception e) {
	  e.printStackTrace();
	}
	}
	
	
	public ArrayList<String> getCustomerType() {
		return customerType;
	}

	public void setCustomerType(ArrayList<String> customerType) {
		this.customerType = customerType;
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
	
	/**
	* @param flag the flag to set
	*/
	public void setFlag(boolean flag) {
	this.flag = flag;
	}
	
	/**
	* @return the flag
	*/
	public boolean isFlag() {
	return flag;
	}
	
	/**
	* @param shipList the shipList to set
	*/
	public void setShipList(ArrayList<String> shipList) {
	this.shipList = shipList;
	}
	/**
	* @return the shipList
	*/
	public ArrayList<String> getShipList() {
	return shipList;
	}
	
	/**
	* @return the contactIdList
	*/
	public ArrayList<String> getContactList() {
	return contactList;
	}
	
	/**
	* @param contactIdList the contactIdList to set
	*/
	public void setContactIdList(ArrayList<String> contactList) {
	this.contactList = contactList;
	}
	
	/**
	* @return the contactShortList
	*/
	public ArrayList<AddressModel> getContactShortList() {
	return contactShortList;
	}
	
	/**
	* @param contactShortList the contactShortList to set
	*/
	public void setContactShortList(ArrayList<AddressModel> contactShortList) {
	this.contactShortList = contactShortList;
	}

	public void setShipViaLists(ArrayList<ShipVia> shipViaLists) {
		this.shipViaLists = shipViaLists;
	}

	public ArrayList<ShipVia> getShipViaLists() {
		return shipViaLists;
	}

	public void setShipVia(ShipVia shipVia) {
		this.shipVia = shipVia;
	}

	public ShipVia getShipVia() {
		return shipVia;
	}
	
	public void setOrderStatus(UsersModel orderStatus) {
		this.orderStatus = orderStatus;
	}

	public UsersModel getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatusLists(ArrayList<UsersModel> orderStatusLists) {
		this.orderStatusLists = orderStatusLists;
	}

	public ArrayList<UsersModel> getOrderStatusLists() {
		return orderStatusLists;
	}
	
}
