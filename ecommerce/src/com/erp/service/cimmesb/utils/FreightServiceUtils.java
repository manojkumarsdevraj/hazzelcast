package com.erp.service.cimmesb.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.cimmesb.client.ecomm.request.CimmAddress;
import com.unilog.cimmesb.client.ecomm.request.CimmContact;
import com.unilog.cimmesb.client.ecomm.request.CimmPackage;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BLineItem;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

public class FreightServiceUtils {
	
	public static com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier getUpsFrieghtModelEsb(SalesModel freightParameters){	
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		WarehouseModel wareHouseDetails = new WarehouseModel();
		if(freightParameters.getWareHouseDetails()!=null) {
			wareHouseDetails = freightParameters.getWareHouseDetails();
		}
		UsersModel userShipTo = freightParameters.getShippingAddress();
		double totalCartWeight= freightParameters.getTotalCartFrieght();
		String serviceCode = freightParameters.getShipViaServiceCode();
		String packageCode = "02";
		String packageInstruction = "Package";
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels")).length()>0){
			 packageCode=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"));
		}
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels")).length()>0){
			packageInstruction=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"));
		}
		String defaultUom = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CART_WEIGHT_UOM"));
		com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier upsFreightdetials = new com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier();
		CimmContact shipperContact = new CimmContact();
		shipperContact.setFirstName(wareHouseDetails.getWareHouseName()!=null?wareHouseDetails.getWareHouseName():"");
		shipperContact.setPrimaryPhoneNumber(wareHouseDetails.getPhone()!=null?wareHouseDetails.getPhone():"");
		upsFreightdetials.setShipperContact(shipperContact);
		
		CimmContact shipToContact = new CimmContact();
		shipToContact.setFirstName(userShipTo.getFirstName()!=null?userShipTo.getFirstName():".");
		shipToContact.setPrimaryPhoneNumber(userShipTo.getPhoneNo());
		upsFreightdetials.setShipToContact(shipToContact);
		
		CimmContact shipFromContact = new CimmContact();
		shipFromContact.setFirstName(wareHouseDetails.getWareHouseName()!=null?wareHouseDetails.getWareHouseName():"");
		shipFromContact.setPrimaryPhoneNumber(wareHouseDetails.getPhone()!=null?wareHouseDetails.getPhone():"");
		upsFreightdetials.setShipFromContact(shipFromContact);

		List<CimmPackage>  listShippingWeights=new ArrayList<>();
		CimmPackage packageInfoDetails = new CimmPackage();
		
		packageInfoDetails.setWeight(totalCartWeight);
		packageInfoDetails.setUom(defaultUom);
		packageInfoDetails.setPackageCode(packageCode);
		packageInfoDetails.setPackageInstruction(packageInstruction);
		
		listShippingWeights.add(packageInfoDetails);
		CimmAddress shipperAddress = new CimmAddress();
		shipperAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipperAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipperAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipperAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipperAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipperAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		upsFreightdetials.setShipperAddress(shipperAddress);
		
		CimmAddress shipToAddress = new CimmAddress();
		shipToAddress.setAddressLine1(userShipTo.getAddress1());
		//shipToAddress.setAddressLine2(userShipTo.getAddress2());
		shipToAddress.setCity(userShipTo.getCity());
		shipToAddress.setState(userShipTo.getState());
		shipToAddress.setCountryCode(userShipTo.getCountry());
		shipToAddress.setZipCode(userShipTo.getZipCode());
		upsFreightdetials.setShipToAddress(shipToAddress);
		
		CimmAddress shipFromAddress = new CimmAddress();
		shipFromAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipFromAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipFromAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipFromAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipFromAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipFromAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		upsFreightdetials.setShipFromAddress(shipFromAddress);
		
		upsFreightdetials.setPackageInfo(packageInfoDetails);
		//upsFreightdetials.setShippingWeight(new PackageInfo("02", "Package", defaultUom, CommonUtility.validateParseDoubleToString(totalCartWeight)));
		upsFreightdetials.setListOfshippingWeight(listShippingWeights);
		upsFreightdetials.setServiceCode("0"+serviceCode);
		if(freightParameters.getShipViaMethod()!=null){
			upsFreightdetials.setServiceName(freightParameters.getShipViaMethod()!=null?freightParameters.getShipViaMethod():freightParameters.getShipViaDescription());
		}
				
		return upsFreightdetials;
	}
	
	
	public static com.unilog.cimmesb.client.ecomm.request.FedExFreight getFedexFreightModelEsb(SalesModel freightParameters){	
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		WarehouseModel wareHouseDetails = new WarehouseModel();
		if(freightParameters.getWareHouseDetails()!=null) {
			wareHouseDetails = freightParameters.getWareHouseDetails();
		}
		UsersModel userShipTo = freightParameters.getShippingAddress();
		double totalCartWeight= freightParameters.getTotalCartFrieght();
		String packageCode = "02";
		String packageInstruction = "Package";
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels")).length()>0){
			 packageCode=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"));
		}
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels")).length()>0){
			packageInstruction=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"));
		}
		
		com.unilog.cimmesb.client.ecomm.request.FedExFreight fedexFreightdetials = new com.unilog.cimmesb.client.ecomm.request.FedExFreight();

		
		com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BAddress shipFromAddress = new   com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BAddress();
		shipFromAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipFromAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipFromAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipFromAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipFromAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipFromAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		fedexFreightdetials.setShipFromAddress(shipFromAddress);

		com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BAddress shipToAddress = new com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BAddress();
		shipToAddress.setAddressLine1(userShipTo.getAddress1());
		//shipToAddress.setAddressLine2(userShipTo.getAddress2());
		shipToAddress.setCity(userShipTo.getCity());
		shipToAddress.setState(userShipTo.getState());
		shipToAddress.setCountryCode(userShipTo.getCountry());
		shipToAddress.setZipCode(userShipTo.getZipCode());
		fedexFreightdetials.setShipToAddress(shipToAddress);

		
		com.unilog.cimmesb.client.ecomm.request.PackageInfo packageInfoDetails = new com.unilog.cimmesb.client.ecomm.request.PackageInfo();
		packageInfoDetails.setWeight(CommonUtility.validateParseDoubleToString(totalCartWeight));
		packageInfoDetails.setUom("LB");
		packageInfoDetails.setPackageCode(packageCode);
		packageInfoDetails.setPackageInstruction(packageInstruction);
		fedexFreightdetials.setPackageInfo(packageInfoDetails);
		
		ArrayList<Cimm2BLineItem> lineItemList=new ArrayList<>();
		Cimm2BLineItem lineItems=null;
		for (ProductsModel products:freightParameters.getCartData())
		{
			lineItems=new Cimm2BLineItem();
			lineItems.setPartNumber(products.getPartNumber());
			lineItems.setLineItemComment(products.getLineItemComment());
			lineItemList.add(lineItems);
		}
		fedexFreightdetials.setLineItems(lineItemList);
		
		if(freightParameters.getShipViaMethod()!=null){
			fedexFreightdetials.setServiceName(freightParameters.getShipViaMethod()!=null?freightParameters.getShipViaMethod():freightParameters.getShipViaDescription());
		}
				
		return fedexFreightdetials;
	}

}
