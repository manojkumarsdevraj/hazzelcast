package com.unilog.custommodule.utility;

import java.lang.reflect.Method;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.custommodule.dao.FreightCalculatorDAO;
import com.unilog.custommodule.model.FreightCalculatorModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

public class FreightCalculator {

	
	public static FreightCalculatorModel getFreightByTotal(FreightCalculatorModel freightInput)
	{
		
		FreightCalculatorModel freightValue = new FreightCalculatorModel();
		Class<?>[] paramObject = new Class[1];
		paramObject[0] = FreightCalculatorModel.class;
		
		
		Object[] arguments = new Object[1];
		arguments[0] = freightInput;
		
		try
		{
			if(CommonDBQuery.getSystemParamtersList().get("CALCULATE_FREIGHT")!=null && CommonDBQuery.getSystemParamtersList().get("CALCULATE_FREIGHT").trim().equalsIgnoreCase("Y"))
			{
				String version = "V1";
				if(CommonDBQuery.getSystemParamtersList().get("FREIGHT_CALCULATOR_VERSION")!=null)
				{
					version = CommonDBQuery.getSystemParamtersList().get("FREIGHT_CALCULATOR_VERSION");
				}
				Class<?> cls = Class.forName("com.unilog.custommodule.utility.FreightCalculator");
				Object obj = cls.newInstance();
				Method method = cls.getDeclaredMethod("getFreightByTotal"+version, paramObject);
				freightValue = (FreightCalculatorModel) method.invoke(obj, arguments);
			}
		}
			
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return freightValue;
		
	}
	
	public static FreightCalculatorModel getFreightByTotalV1(FreightCalculatorModel freightInput)
	{
		FreightCalculatorModel freightValue = new FreightCalculatorModel();
		try
		{
			
			if(freightInput!=null){
				
				double cartTotal=2000.00;
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("FREIGHT_RESTRICTION_AMOUNT_ON_CART_TOTAL")).length()>0){
					cartTotal = CommonUtility.validateDoubleNumber(CommonDBQuery.getSystemParamtersList().get("FREIGHT_RESTRICTION_AMOUNT_ON_CART_TOTAL"));
				}
				
				freightValue = FreightCalculatorDAO.getFreightValueByTotal(freightInput);
				if(freightValue !=null)
				{
					if(CommonUtility.validateString(freightInput.getCountry()).trim().equalsIgnoreCase("USA")){
						freightInput.setCountry("US");
					}
					if(!CommonUtility.validateString(freightInput.getCountry()).trim().equalsIgnoreCase("US") || CommonUtility.validateString(freightInput.getState()).trim().equalsIgnoreCase("HI") || CommonUtility.validateString(freightInput.getState()).trim().equalsIgnoreCase("AK"))
					{
						//if(freightInput.getShipVia()!=null && (freightInput.getShipVia().equalsIgnoreCase("W/C") || freightInput.getShipVia().equalsIgnoreCase("DEL"))){
						if(freightInput.getShipVia()!=null){
							freightValue.setFreightValue(0.00);
							freightValue.setMessage(LayoutLoader.getMessageProperties().get(freightInput.getLocale()).getProperty("message.freight"));
						}
					}
					
					if(CommonUtility.validateString(freightInput.getCountry()).trim().equalsIgnoreCase("US") && freightInput.getCartTotal() > cartTotal && freightInput.getCartTotal() < 99999.99)
					{
						if(freightInput.getShipVia()!=null){
							freightValue = new FreightCalculatorModel();
							freightValue.setFreightValue(0.00);
							freightValue.setMessage(LayoutLoader.getMessageProperties().get(freightInput.getLocale()).getProperty("message.freight"));
						}
					}
					
					if(freightInput.getOverSize()!=null && freightInput.getOverSize().trim().equalsIgnoreCase("Y"))
					{
						if(freightInput.getShipVia()!=null){
							freightValue = new FreightCalculatorModel();
							freightValue.setFreightValue(0.00);
							freightValue.setMessage(LayoutLoader.getMessageProperties().get(freightInput.getLocale()).getProperty("message.freight"));
						}
					}
					
				}else{
					if(CommonUtility.validateString(freightInput.getCountry()).trim().equalsIgnoreCase("USA")){
						freightInput.setCountry("US");
					}
					if(!CommonUtility.validateString(freightInput.getCountry()).trim().equalsIgnoreCase("US") || CommonUtility.validateString(freightInput.getState()).trim().equalsIgnoreCase("HI") || CommonUtility.validateString(freightInput.getState()).trim().equalsIgnoreCase("AK"))
					{
						//if(freightInput.getShipVia()!=null && (freightInput.getShipVia().equalsIgnoreCase("W/C") || freightInput.getShipVia().equalsIgnoreCase("DEL"))){
						if(freightInput.getShipVia()!=null){
							freightValue = new FreightCalculatorModel();
							freightValue.setFreightValue(0.00);
							freightValue.setMessage(LayoutLoader.getMessageProperties().get(freightInput.getLocale()).getProperty("message.freight"));
						}
					}
					if(CommonUtility.validateString(freightInput.getCountry()).trim().equalsIgnoreCase("US") && freightInput.getCartTotal() > cartTotal && freightInput.getCartTotal() < 99999.99)
					{
						if(freightInput.getShipVia()!=null){
							freightValue = new FreightCalculatorModel();
							freightValue.setFreightValue(0.00);
							freightValue.setMessage(LayoutLoader.getMessageProperties().get(freightInput.getLocale()).getProperty("message.freight"));
						}
					}
					
					if(freightInput.getOverSize()!=null && freightInput.getOverSize().trim().equalsIgnoreCase("Y"))
					{
						if(freightInput.getShipVia()!=null){
							freightValue = new FreightCalculatorModel();
							freightValue.setFreightValue(0.00);
							freightValue.setMessage(LayoutLoader.getMessageProperties().get(freightInput.getLocale()).getProperty("message.freight"));
						}
					}
					
				}
			}else{
				freightValue = new FreightCalculatorModel();
				freightValue.setFreightValue(0.00);
				freightValue.setMessage(LayoutLoader.getMessageProperties().get(freightInput.getLocale()).getProperty("message.freight"));
			}
			//--
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return freightValue;
	}
	
}
