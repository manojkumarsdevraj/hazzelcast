package com.shippingcarrier.ups.parser;

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

import com.shippingcarrier.ups.model.UpsCarrierModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;

import com.unilog.users.AddressModel;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class UpsCarrierParser {
	
	private Stack<UsersModel> objectStack = new Stack<UsersModel>();
	private String response = "Success";
	private UpsCarrierModel upsDetail = null;
	public String value = "";
	private boolean flag = false;
	private ArrayList<UpsCarrierModel> upsDetailList = null;
	
	private InputStream is;
	public UpsCarrierParser(InputStream stringBufferInputStream){
		setUpsDetailList(new ArrayList<UpsCarrierModel>());
		this.is=stringBufferInputStream;
		excute();
	} 
	
	void excute() {
	try {
	
	 SAXParserFactory factory = SAXParserFactory.newInstance();
	 SAXParser saxParser = factory.newSAXParser();
	
	 DefaultHandler handler = new DefaultHandler() {
	 private Stack<String> elementStack = new Stack<String>();
	   boolean ratingServiceSelectionResponse = false;
	   boolean responseValue = false;
	   boolean transactionReference = false;
	   boolean responseStatusCode = false;
	   boolean responseStatusDescription = false;
	   boolean ratedShipment = false;
	   boolean service = false;
	   boolean ratedShipmentWarning = false;
	   boolean billingWeight = false;
	   boolean unitOfMeasurement = false;
	   boolean transportationCharges = false;
	   boolean serviceOptionsCharges = false;
	   boolean ratedPackage = false;
	   boolean bllingWeight = false;
	   boolean totalCharges = false;
	 public void startElement(String uri, String localName,
	    String qName, Attributes attributes)
	    throws SAXException {
		 this.elementStack.push(qName);
		 value = "";
		 
	        if(qName.equalsIgnoreCase("RatingServiceSelectionResponse"))
	        {
	        	ratingServiceSelectionResponse = true;
	        }
	        
	        if(qName.equalsIgnoreCase("Response"))
	        {
	        	responseValue = true;
	        }
	        if(qName.equalsIgnoreCase("TransactionReference"))
	        {
	        	transactionReference = true;
	        }
	        if(qName.equalsIgnoreCase("ResponseStatusCode"))
	        {
	        	responseStatusCode = true;
	        }
	        if(qName.equalsIgnoreCase("ResponseStatusDescription"))
	        {
	        	responseStatusDescription = true;
	        }
	        if(qName.equalsIgnoreCase("RatedShipment"))
	        {
	        	ratedShipment = true;
	        	upsDetail = new UpsCarrierModel();
	        }
	        if(qName.equalsIgnoreCase("Service"))
	        {
	        	service = true;
	        }
	        if(qName.equalsIgnoreCase("RatedShipmentWarning"))
	        {
	        	ratedShipmentWarning = true;
	        }
	        if(qName.equalsIgnoreCase("BillingWeight"))
	        {
	        	billingWeight = true;
	        }
	        if(qName.equalsIgnoreCase("UnitOfMeasurement"))
	        {
	        	unitOfMeasurement = true;
	        }
	        
	        if(qName.equalsIgnoreCase("TransportationCharges"))
	        {
	        	transportationCharges = true;
	        }
	        if(qName.equalsIgnoreCase("ServiceOptionsCharges"))
	        {
	        	serviceOptionsCharges = true;
	        }
	        if(qName.equalsIgnoreCase("RatedPackage"))
	        {
	        	ratedPackage = true;
	        }
	        if(qName.equalsIgnoreCase("TotalCharges"))
	        {
	        	totalCharges = true;
	        }
	      }
	
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if(response.equalsIgnoreCase("Success")){
			 if("Code".equalsIgnoreCase(currentElement()) && service && ratedShipment){
				upsDetail.setServiceCode(Integer.parseInt(value));
			}else if("CurrencyCode".equalsIgnoreCase(currentElement())&& transportationCharges && ratedPackage){
				upsDetail.setTransportationCurrencyCode(value);
			}else if("CurrencyCode".equalsIgnoreCase(currentElement())&& serviceOptionsCharges && ratedPackage){
				upsDetail.setServiceOptionsCurrencyCode(value);
			}else if("MonetaryValue".equalsIgnoreCase(currentElement())&& transportationCharges && ratedPackage){
				upsDetail.setTransportationCurrencyCode(value);
			}else if("MonetaryValue".equalsIgnoreCase(currentElement())&& serviceOptionsCharges && ratedPackage){
				upsDetail.setServiceOptionsMonetaryValue(Double.parseDouble(value));
			}else if("CurrencyCode".equalsIgnoreCase(currentElement())&& totalCharges && ratedPackage){
				upsDetail.setTotalCurrencyCode(value);
			}else if("MonetaryValue".equalsIgnoreCase(currentElement())&& totalCharges && ratedPackage){
				upsDetail.setTotalMonetaryValue(Double.parseDouble(value));
			}else if("Weight".equalsIgnoreCase(currentElement())&& ratedPackage){
				upsDetail.setWeight(Double.parseDouble(value));
			}else if("Code".equalsIgnoreCase(currentElement())&& unitOfMeasurement && billingWeight){
				upsDetail.setMeasurementCode(value);
			}else if("Weight".equalsIgnoreCase(currentElement())&& unitOfMeasurement && billingWeight){
				upsDetail.setBillingWeight(Double.parseDouble(value));
			}

		}

		this.elementStack.pop();

		 if(qName.equalsIgnoreCase("RatingServiceSelectionResponse"))
	        {
	        	ratingServiceSelectionResponse = false;
	        }
	        
	        if(qName.equalsIgnoreCase("Response"))
	        {
	        	responseValue = false;
	        }
	        if(qName.equalsIgnoreCase("TransactionReference"))
	        {
	        	transactionReference = false;
	        }
	        if(qName.equalsIgnoreCase("ResponseStatusCode"))
	        {
	        	responseStatusCode = false;
	        }
	        if(qName.equalsIgnoreCase("ResponseStatusDescription"))
	        {
	        	responseStatusDescription = false;
	        }
	        if(qName.equalsIgnoreCase("RatedShipment"))
	        {
	        	upsDetailList.add(upsDetail);
	        	ratedShipment = false;
	        	upsDetail = new UpsCarrierModel();
	        }
	        if(qName.equalsIgnoreCase("Service"))
	        {
	        	service = false;
	        }
	        if(qName.equalsIgnoreCase("RatedShipmentWarning"))
	        {
	        	ratedShipmentWarning = false;
	        }
	        if(qName.equalsIgnoreCase("BillingWeight"))
	        {
	        	billingWeight = false;
	        }
	        if(qName.equalsIgnoreCase("UnitOfMeasurement"))
	        {
	        	unitOfMeasurement = false;
	        }
	        if(qName.equalsIgnoreCase("TransportationCharges"))
	        {
	        	transportationCharges = false;
	        }
	        if(qName.equalsIgnoreCase("ServiceOptionsCharges"))
	        {
	        	serviceOptionsCharges = false;
	        }
	        if(qName.equalsIgnoreCase("RatedPackage"))
	        {
	        	ratedPackage = false;
	        }
	        if(qName.equalsIgnoreCase("TotalCharges"))
	        {
	        	totalCharges = false;
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
	  this.setupsDetail(upsDetail);
	} catch (Exception e) {
	  e.printStackTrace();
	}
	}
	

	/**
	* @return the upsDetail
	*/
	public UpsCarrierModel getupsDetail() {
	return upsDetail;
	}
	
	/**
	* @param upsDetail the upsDetail to set
	*/
	public void setupsDetail( UpsCarrierModel upsDetail) {
	this.upsDetail = upsDetail;
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

	public Stack<UsersModel> getObjectStack() {
		return objectStack;
	}

	public void setObjectStack(Stack<UsersModel> objectStack) {
		this.objectStack = objectStack;
	}

	public ArrayList<UpsCarrierModel> getUpsDetailList() {
		return upsDetailList;
	}

	public void setUpsDetailList(ArrayList<UpsCarrierModel> upsDetailList) {
		this.upsDetailList = upsDetailList;
	}
	
	/**
	* @param shipList the shipList to set
	*/
	

	
}
