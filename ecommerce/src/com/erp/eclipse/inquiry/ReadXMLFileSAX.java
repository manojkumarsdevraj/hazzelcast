package com.erp.eclipse.inquiry;
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
import com.unilog.sales.CreditCardModel;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;



public class ReadXMLFileSAX {
	public ArrayList<UsersModel> userDetailList = null;
	public ArrayList<CreditCardModel> cerditCardDetailList = null;
	private Stack<UsersModel> objectStack = new Stack<UsersModel>();
	private String response = "Success";
	private UsersModel userDetail = null;
	private CreditCardModel cerditCardDetail = null;
	private boolean flag = false;
	private ArrayList<String> shipList;
	private ArrayList<String> contactList;
	private ArrayList<AddressModel> contactShortList;
	private ArrayList<Double> listPriceList;
	private ArrayList<Double> customerPriceList;
	private ArrayList<Double> extendedPriceList;


	private InputStream is;
	public ReadXMLFileSAX(InputStream stringBufferInputStream){
		userDetailList = new ArrayList<UsersModel>(); 
		cerditCardDetailList = new ArrayList<CreditCardModel>();
		contactList = new ArrayList<String>();
		contactShortList = new ArrayList<AddressModel>();
		shipList = new ArrayList<String>();
		listPriceList = new ArrayList<Double>();
		customerPriceList = new ArrayList<Double>();
		extendedPriceList = new ArrayList<Double>();
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
				boolean pricing = false;
				boolean billto = false;
				boolean cerditCard = false;
				boolean cerditCardList = false;
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
					if(qName.equalsIgnoreCase("ShipToList"))
					{
						flag = true;
					}

					if(qName.equalsIgnoreCase("ContactShort"))
					{
						contactid = true;
					}
					if(qName.equalsIgnoreCase("Address"))
					{
						address = true;
					}
					if(qName.equalsIgnoreCase("Pricing"))
					{
						pricing = true;
					}
					if(qName.equalsIgnoreCase("billto"))
					{
						billto = true;
					}
					if(qName.equalsIgnoreCase("CreditCardList"))
					{
						cerditCardList = true;
					}
					if(qName.equalsIgnoreCase("CreditCard"))
					{
						cerditCardDetail = new CreditCardModel();
						cerditCard = true;
					}

				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {

					this.elementStack.pop();

					if(qName.equalsIgnoreCase("ShipToList")){
						userDetailList.add(objectStack.pop());
					}
					if(qName.equalsIgnoreCase("ContactShortList")){
						contactid = false;
					}
					if(qName.equalsIgnoreCase("Address"))
					{
						address = false;
					}
					if(qName.equalsIgnoreCase("Pricing"))
					{
						pricing = false;
					}
					if(qName.equalsIgnoreCase("billto"))
					{
						billto = false;
					}
					if(qName.equalsIgnoreCase("CreditCardList"))
					{
						userDetail.setCreditCardDetailList(cerditCardDetailList);
						cerditCardList = false;
					}
					if(qName.equalsIgnoreCase("CreditCard"))
					{
						cerditCardDetailList.add(cerditCardDetail);
						cerditCard = false;
					}

				}
				public void characters(char ch[], int start, int length)
				throws SAXException {
					String value = new String(ch, start, length).trim();

					if(value.length() == 0) return; // ignore white space

					if(response.equalsIgnoreCase("Success")){
						if("entityid".equalsIgnoreCase(currentElement())){
							if(flag){
								shipList.add(value);
								userDetail.setShipIdList(shipList);
							}else if (billto){
								userDetail.setBillEntityId(CommonUtility.validateString(value));
							}else
								userDetail.setEntityId(CommonUtility.validateString(value));

						}else if("EntityName".equalsIgnoreCase(currentElement())){
							userDetail.setEntityName(value);
						}else if("LoginID".equalsIgnoreCase(currentElement())){
							userDetail.setErpLoginId(value);
						}else if("FirstName".equalsIgnoreCase(currentElement())){
							userDetail.setFirstName(value);
						}else if("LastName".equalsIgnoreCase(currentElement())){
							userDetail.setLastName(value);
						}else if("StreetLineOne".equalsIgnoreCase(currentElement())&& address){
							userDetail.setAddress1(value);
						}else if("StreetLineTwo".equalsIgnoreCase(currentElement()) && address){
							userDetail.setAddress2(value);
						}else if("City".equalsIgnoreCase(currentElement())&& address){
							userDetail.setCity(value);
						}else if("Country".equalsIgnoreCase(currentElement())&& address){
							userDetail.setCountry(value);
						}else if("PostalCode".equalsIgnoreCase(currentElement()) && address){
							userDetail.setZipCode(value);
						}else if("EmailAddress".equalsIgnoreCase(currentElement())){
							userDetail.setEmailAddress(value);
						}else if("WebAddress".equalsIgnoreCase(currentElement())){
							userDetail.setWebAddress(value);
						}else if("state".equalsIgnoreCase(currentElement()) && address){
							userDetail.setState(value);
						}else if("SessionID".equalsIgnoreCase(currentElement())){
							userDetail.setSessionID(value);
						}else if("ContactID".equalsIgnoreCase(currentElement())){
							contactList.add(CommonUtility.validateString(value));
							userDetail.setContactIdList(contactList);
						}else if(contactid){
							if("Description".equalsIgnoreCase(currentElement())||"Number".equalsIgnoreCase(currentElement())){
								AddressModel phone = new AddressModel();
			            		phone.setPhoneNo(value);
			            		contactShortList.add(phone);
								userDetail.setContactShortList(contactShortList);
							}
						}else if("IsSuperuser".equalsIgnoreCase(currentElement())){
							userDetail.setIsSuperUser(value);
						}
						else if("CreditCardType".equalsIgnoreCase(currentElement())&& cerditCardList && cerditCard){
							cerditCardDetail.setCreditCardType(value);
						}else if("CreditCardNumber".equalsIgnoreCase(currentElement()) && cerditCardList && cerditCard){
							cerditCardDetail.setCreditCardNumber(value);
						}else if("Date".equalsIgnoreCase(currentElement())&& cerditCardList && cerditCard){
							cerditCardDetail.setDate(value);
						}else if("CardHolder".equalsIgnoreCase(currentElement())&& cerditCardList && cerditCard){
							cerditCardDetail.setCardHolder(value);
						}else if("StreetAddress".equalsIgnoreCase(currentElement()) && cerditCardList && cerditCard){
							cerditCardDetail.setAddress1(value);
						}else if("PostalCode".equalsIgnoreCase(currentElement())&& cerditCardList && cerditCard){
							cerditCardDetail.setZipCode(value);
						}else if("ElementPaymentAccountId".equalsIgnoreCase(currentElement())&& cerditCardList && cerditCard){
							cerditCardDetail.setElementPaymentAccountId(value);
						}else if("Number".equalsIgnoreCase(currentElement())){
							userDetail.setPhoneNo(value);
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

	/**
	 * @return the listPriceList
	 */
	public ArrayList<Double> getListPriceList() {
		return listPriceList;
	}

	/**
	 * @param listPriceList the listPriceList to set
	 */
	public void setListPriceList(ArrayList<Double> listPriceList) {
		this.listPriceList = listPriceList;
	}

	/**
	 * @return the customerPriceList
	 */
	public ArrayList<Double> getCustomerPriceList() {
		return customerPriceList;
	}

	/**
	 * @param customerPriceList the customerPriceList to set
	 */
	public void setCustomerPriceList(ArrayList<Double> customerPriceList) {
		this.customerPriceList = customerPriceList;
	}

	/**
	 * @return the extendedPriceList
	 */
	public ArrayList<Double> getExtendedPriceList() {
		return extendedPriceList;
	}

	/**
	 * @param extendedPriceList the extendedPriceList to set
	 */
	public void setExtendedPriceList(ArrayList<Double> extendedPriceList) {
		this.extendedPriceList = extendedPriceList;
	}

}