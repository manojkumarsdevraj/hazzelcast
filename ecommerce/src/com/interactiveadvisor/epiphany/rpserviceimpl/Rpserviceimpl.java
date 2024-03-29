package com.interactiveadvisor.epiphany.rpserviceimpl;
import com.interactiveadvisor.epiphany.www.RP.*; // Generated Java Class from rtws.wsdl
import com.interactiveadvisor.epiphany.www.RP.holders.*; // Generated Java Class for the Holders
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

import javax.xml.rpc.holders.*; // Contains the java primitive type holders.

import java.net.*; // URL class.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
// This is a Tester class that uses the Stub files generated by the AXIS with rtws.wsdl.
public class Rpserviceimpl {

public static ArrayList<ProductsModel> processEvent(RtPortType inPort, String partNumber){
	
	ArrayList<ProductsModel> interActiveAdvisorItemList = null;	
		try{
			String _package = null;
			String event = null;
			KeyValuePair keyVal = new KeyValuePair();
			KeyValuePair[] fields = new KeyValuePair[3];
			keyVal.setKey("SessionId");
			keyVal.setValue(CommonDBQuery.getSystemParamtersList().get("INTERACTIVE_ADVISOR_SESSION_ID")); //INTERACTIVE_ADVISOR_SESSION_ID //44034
			fields[0] = keyVal;
			keyVal = new KeyValuePair();
			keyVal.setKey("ItemCollection");
			keyVal.setValue(partNumber);//"CRHITP218"
			fields[1] = keyVal;
			keyVal = new KeyValuePair();
			keyVal.setKey("MaxItems");
			keyVal.setValue(CommonDBQuery.getSystemParamtersList().get("INTERACTIVE_ADVISOR_MAX_ITEM_REQUEST"));//
			fields[2] = keyVal;
			
			
			IntHolder retCode = new IntHolder();
			ArrayOfReturnedOfferHolder offers = new ArrayOfReturnedOfferHolder();
			inPort.processEvent(CommonDBQuery.getSystemParamtersList().get("INTERACTIVE_ADVISOR_PACKAGE"), CommonDBQuery.getSystemParamtersList().get("INTERACTIVE_ADVISOR_EVENT") , fields, retCode, offers); //INTERACTIVE_ADVISOR_PACKAGE  INTERACTIVE_ADVISOR_EVENT
			/*inPort.executeJS("RegTestDefault", "return 54", "1", lRetVal,result);*/
			/*
			System.out.println("Execute JS " + lRetVal.value + " " +result.value);*/
			
			
			Map<Integer, ProductsModel> unsortMap = new HashMap<Integer, ProductsModel>();
			ReturnedOffer[] op = offers.value;
			if(op!=null)
			{
				interActiveAdvisorItemList = new ArrayList<ProductsModel>();
				int mapKeyValue = 0;
				for(ReturnedOffer o:op)
				{
					ProductsModel productModel = new ProductsModel();
					productModel.setInterActiveOfferId(o.getOfferId());
					productModel.setInterActiveOfferName(o.getName());
					System.out.println(o.getOfferId());
					System.out.println(o.getName());
					KeyValuePair[] attr = o.getAttributes();
					if(attr!=null && attr.length >0)
					{
						
						for(KeyValuePair k:attr)
						{
							System.out.println(k.getKey() + " - " + k.getValue());
							
							if(k.getKey()!=null && k.getKey().trim().equalsIgnoreCase("OfferPosition")){
								mapKeyValue = CommonUtility.validateNumber(k.getValue());
								productModel.setInterActiveOfferPosition(mapKeyValue);
							}
							
							if(k.getKey()!=null && k.getKey().trim().equalsIgnoreCase("RecommendedProduct")){
								productModel.setPartNumber(k.getValue().trim());
							}
						}
						
					}
					unsortMap.put(mapKeyValue, productModel);
					System.out.println("-----------------------------------------------------------------------------------------------------------------");
				}
				if(unsortMap!=null && unsortMap.size()>0){
					Map<Integer, ProductsModel> treeMap = new TreeMap<Integer, ProductsModel>(unsortMap);
					
					for (Map.Entry<Integer, ProductsModel> entry : treeMap.entrySet()) {
						System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
						interActiveAdvisorItemList.add(entry.getValue());
					}
				}
			}
		
		}
		catch(Exception exp)
		{
		exp.printStackTrace();
		}
		return interActiveAdvisorItemList;
}


public static ArrayList<ProductsModel> callInterActiveAdvisor(String partNumber){
	
	ArrayList<ProductsModel>interActiveAdvisorItemList = null;
	
	try {
			// Url specifies where is the Real-Time Server.
			URL lUrl = new URL(CommonDBQuery.getSystemParamtersList().get("INTERACTIVE_ADVISOR_CONNECTION_URL")); //"http://ia.reynco.com:7201/SOAP"
			// Create a new service.
			RealTimeService lService = new RealTimeServiceLocator();
			// The object is the Stub that's generated by the AXIS WSDL2Java utilty.
			// is object contains all the functions call in the rtws.wsdlfile.
			RtPortType lRtPort = lService.getRtPort(lUrl);
			// Setting the Username and Password.
			RtSoapBindingStub s = (RtSoapBindingStub) lRtPort;
			s.setUsername(CommonDBQuery.getSystemParamtersList().get("INTERACTIVE_ADVISOR_USER_NAME")); //rt 
			s.setPassword(CommonDBQuery.getSystemParamtersList().get("INTERACTIVE_ADVISOR_PASSWORD"));
	
			// ---------------------------------WARNING---------------------------------------
			// Turning off the multiRef. This creates some problems with the parsing in Interaction Advisor in 6.5.2.
			// However, this is fixed in version Interaction Advisor 6.5.3
			org.apache.axis.client.Call lCall = (org.apache.axis.client.Call) lService.createCall();
			lCall.getMessageContext().getAxisEngine().setOption(
			org.apache.axis.AxisEngine.PROP_DOMULTIREFS, new Boolean("false"));
			// --- Testing ----------------------------
	
			interActiveAdvisorItemList = Rpserviceimpl.processEvent(lRtPort,partNumber);

		}catch (Exception exp) {
			exp.printStackTrace();
		}
	return interActiveAdvisorItemList;
}


/*public static void main(String[] args) {
	callInterActiveAdvisor("CRHITP218");
}*/
}