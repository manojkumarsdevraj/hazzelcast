package com.shippingcarrier.dao;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;

import com.shippingcarrier.ups.model.UpsCarrierModel;
import com.shippingcarrier.ups.parser.UpsCarrierParser;
import com.unilog.database.CommonDBQuery;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;


public class UpsCarrier {
		
	public static double getUPSFreightCharges(double totalWeight, WarehouseModel wareHouseAddress, UsersModel shipAddress, int serviceCode ) {
	
	 HttpURLConnection eclipseConn = null;
	 ArrayList<UpsCarrierModel> upslist = new ArrayList<UpsCarrierModel>();
	 double totalCharges= 0;
	try 
	{
		String ERPTEMPLATEPATH = CommonDBQuery.getSystemParamtersList().get("ERPTEMPLATEPATH");
		String upsApiUrl = CommonDBQuery.getSystemParamtersList().get("UPSSHIPURL");
   		VelocityEngine velocityTemplateEngine = new VelocityEngine();
   		velocityTemplateEngine.setProperty("file.resource.loader.path", ERPTEMPLATEPATH);
   		velocityTemplateEngine.init();

        Template t = velocityTemplateEngine.getTemplate("Rate_Tool_SampleRequest.xml");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
      
        context.put("accessLicenseNumber", CommonDBQuery.getSystemParamtersList().get("ACCESSLICENSENUMBER"));
        context.put("userId",CommonDBQuery.getSystemParamtersList().get("USERID"));
        context.put("password", CommonDBQuery.getSystemParamtersList().get("PASSWORD"));
        context.put("pickupType","DAILYPICKUP" );
        context.put("shipperName",wareHouseAddress.getWareHouseName());
        context.put("shipperPhoneNumber",wareHouseAddress.getPhone());
        context.put("shipperAddressLine1",wareHouseAddress.getAddress1());
        context.put("shipperCity",wareHouseAddress.getCity());
        context.put("shipperStateProvinceCode",wareHouseAddress.getState());
        context.put("shipperPostalCode",wareHouseAddress.getZip());
        context.put("shipperCountryCode",wareHouseAddress.getCountry());
        context.put("shipttoName",shipAddress.getShipToName());
        context.put("shipttoPhoneNumber",shipAddress.getPhoneNo());
        context.put("shipttoAddressLine1",shipAddress.getAddress1());
        context.put("shipttoCity",shipAddress.getCity());
        context.put("shipttoStateProvinceCode",shipAddress.getState());
        context.put("shipttoPostalCode",shipAddress.getZipCodeStringFormat());
        if(CommonUtility.validateString(shipAddress.getCountry()).equalsIgnoreCase("USA")){
        	shipAddress.setCountry("US");
        }
        context.put("shipttoCountryCode",shipAddress.getCountry());
        context.put("totalWeight",totalWeight );
        
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        /* show the World */
        StringBuffer finalMessage= new StringBuffer();
        finalMessage.append(writer.toString());
        System.out.println(finalMessage.toString());
        
        
		eclipseConn = (HttpURLConnection) new URL(upsApiUrl).openConnection();
        eclipseConn.setDoOutput(true);
		OutputStream os = eclipseConn.getOutputStream();
		os.write(finalMessage.toString().getBytes());
		
		BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
		System.out.println(in.toString());
		String line = null;
		
	
		
		StringBuilder responseData = new StringBuilder();
		long lStartTime = new Date().getTime(); //start time
		while((line = in.readLine()) != null) {
			responseData.append(line);
		}
		 
		@SuppressWarnings("deprecation")
		UpsCarrierParser xmlFileSAX = new UpsCarrierParser(new StringBufferInputStream(responseData.toString()));
		System.out.println(responseData.toString());
		upslist = xmlFileSAX.getUpsDetailList();
		
		for(UpsCarrierModel item : upslist){
			if(item.getServiceCode()==serviceCode){
				totalCharges = item.getTotalMonetaryValue();
				System.out.println("UPS Freight --- " + item.getTotalMonetaryValue());
				break;
			}
		}
		
		
		  long lEndTime = new Date().getTime(); //end time
		 
	      long difference = lEndTime - lStartTime; //check difference
	 
	      System.out.println("Elapsed milliseconds: " + difference);
	      os.close();
		  in.close();
		
    } catch (ParseErrorException ex) {
          ex.printStackTrace();
          //session.setAttribute("connectionError", "Y");
    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 		
		return totalCharges;
	}
public static LinkedHashMap<Integer,Double> getUPSCalculatorForDetail(double totalWeight, WarehouseModel wareHouseAddress, UsersModel shipAddress) {
	
	 HttpURLConnection eclipseConn = null;
	 ArrayList<UpsCarrierModel> upslist = new ArrayList<UpsCarrierModel>();
	 LinkedHashMap<Integer,Double> upsDetails=new LinkedHashMap<Integer, Double>();
	 //double totalCharges= 0;
	try 
	{
		String ERPTEMPLATEPATH = CommonDBQuery.getSystemParamtersList().get("ERPTEMPLATEPATH");
		String upsApiUrl = CommonDBQuery.getSystemParamtersList().get("UPSSHIPURL");
  		VelocityEngine velocityTemplateEngine = new VelocityEngine();
  		velocityTemplateEngine.setProperty("file.resource.loader.path", ERPTEMPLATEPATH);
  		velocityTemplateEngine.init();

       Template t = velocityTemplateEngine.getTemplate("Rate_Tool_SampleRequest.xml");
       /*  create a context and add data */
       VelocityContext context = new VelocityContext();
     
       context.put("accessLicenseNumber", CommonDBQuery.getSystemParamtersList().get("ACCESSLICENSENUMBER"));
       context.put("userId",CommonDBQuery.getSystemParamtersList().get("USERID"));
       context.put("password", CommonDBQuery.getSystemParamtersList().get("PASSWORD"));
       context.put("pickupType","DAILYPICKUP" );
       context.put("shipperName",wareHouseAddress.getWareHouseName());
       context.put("shipperPhoneNumber",wareHouseAddress.getPhone());
       context.put("shipperAddressLine1",wareHouseAddress.getAddress1());
       context.put("shipperCity",wareHouseAddress.getCity());
       context.put("shipperStateProvinceCode",wareHouseAddress.getState());
       context.put("shipperPostalCode",wareHouseAddress.getZip());
       context.put("shipperCountryCode",wareHouseAddress.getCountry());
       context.put("shipttoName",shipAddress.getShipToName());
       context.put("shipttoPhoneNumber",shipAddress.getPhoneNo());
       context.put("shipttoAddressLine1",shipAddress.getAddress1());
       context.put("shipttoCity",shipAddress.getCity());
       context.put("shipttoStateProvinceCode",shipAddress.getState());
       context.put("shipttoPostalCode",shipAddress.getZipCodeStringFormat());
       context.put("shipttoCountryCode",shipAddress.getCountry());
       context.put("totalWeight",totalWeight );
       
       /* now render the template into a StringWriter */
       StringWriter writer = new StringWriter();
       t.merge(context, writer);
       /* show the World */
       StringBuffer finalMessage= new StringBuffer();
       finalMessage.append(writer.toString());
       System.out.println(finalMessage.toString());
       
       
		eclipseConn = (HttpURLConnection) new URL(upsApiUrl).openConnection();
       eclipseConn.setDoOutput(true);
		OutputStream os = eclipseConn.getOutputStream();
		os.write(finalMessage.toString().getBytes());
		
		BufferedReader in = new BufferedReader(new InputStreamReader(eclipseConn.getInputStream()));
		System.out.println(in.toString());
		String line = null;
		
	
		
		StringBuilder responseData = new StringBuilder();
		long lStartTime = new Date().getTime(); //start time
		while((line = in.readLine()) != null) {
			responseData.append(line);
		}
		 
		@SuppressWarnings("deprecation")
		UpsCarrierParser xmlFileSAX = new UpsCarrierParser(new StringBufferInputStream(responseData.toString()));
		System.out.println(responseData.toString());
		upslist = xmlFileSAX.getUpsDetailList();
		
		for(UpsCarrierModel item : upslist){
			
				//totalCharges = item.getTotalMonetaryValue();
				upsDetails.put(item.getServiceCode(),item.getTotalMonetaryValue());
				System.out.println("UPS Freight --- " + item.getTotalMonetaryValue());			
			
		}
		
		
		  long lEndTime = new Date().getTime(); //end time
		 
	      long difference = lEndTime - lStartTime; //check difference
	 
	      System.out.println("Elapsed milliseconds: " + difference);
	      os.close();
		  in.close();
		
   } catch (ParseErrorException ex) {
         ex.printStackTrace();
         //session.setAttribute("connectionError", "Y");
   } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 		
		return upsDetails;
	}
	
	
}
