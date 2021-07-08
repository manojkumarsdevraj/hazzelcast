package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import com.erp.service.cimm2bcentral.models.ContactDetails;
import com.erp.service.cimm2bcentral.models.FedExFreight;
import com.erp.service.cimm2bcentral.models.PackageDimension;
import com.erp.service.cimm2bcentral.models.PackageInfo;
import com.erp.service.cimm2bcentral.models.TimeInTransit;
import com.erp.service.cimm2bcentral.models.UpsFreight;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.cimmesb.client.ecomm.request.CimmAddress;
import com.unilog.cimmesb.client.ecomm.request.CimmContact;
import com.unilog.cimmesb.client.ecomm.request.CimmPackage;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BAddress;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BContact;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BLineItem;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

public class FreightServiceUtils {
	
	public static UpsFreight getUpsFrieghtModelData(SalesModel freightParameters) {
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		UpsFreight upsFreightdetials = new UpsFreight();
		try {
		WarehouseModel wareHouseDetails = new WarehouseModel();
		if(freightParameters.getWareHouseDetails()!=null) {
			wareHouseDetails = freightParameters.getWareHouseDetails();
		}
		UsersModel userShipTo = freightParameters.getShippingAddress();
		double totalCartWeight= freightParameters.getTotalCartFrieght();
		String serviceCode = freightParameters.getShipViaServiceCode();
		String defaultUom = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CART_WEIGHT_UOM"));
		String packageCode = "02";
		String packageInstruction = "Package";
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels")).length()>0){
			 packageCode=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"));
		}
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels")).length()>0){
			packageInstruction=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"));
		}
		ContactDetails shipperContact = new ContactDetails();
		shipperContact.setFirstName(wareHouseDetails.getWareHouseName()!=null?wareHouseDetails.getWareHouseName():"");
		shipperContact.setPrimaryPhoneNumber(wareHouseDetails.getPhone()!=null?wareHouseDetails.getPhone():"");
		upsFreightdetials.setShipperContact(shipperContact);
		
		ContactDetails shipToContact = new ContactDetails();
		shipToContact.setFirstName(userShipTo.getFirstName()!=null?userShipTo.getFirstName():".");
		shipToContact.setPrimaryPhoneNumber(userShipTo.getPhoneNo());
		upsFreightdetials.setShipToContact(shipToContact);
		
		ContactDetails shipFromContact = new ContactDetails();
		shipFromContact.setFirstName(wareHouseDetails.getWareHouseName()!=null?wareHouseDetails.getWareHouseName():"");
		shipFromContact.setPrimaryPhoneNumber(wareHouseDetails.getPhone()!=null?wareHouseDetails.getPhone():"");
		upsFreightdetials.setShipFromContact(shipFromContact);

		ArrayList<PackageInfo> listShippingWeights = new ArrayList<PackageInfo>();
		PackageInfo packageInfoDetails = new PackageInfo();
		
		packageInfoDetails.setWeight(CommonUtility.validateParseDoubleToString(totalCartWeight));
		packageInfoDetails.setUom(defaultUom);
		packageInfoDetails.setPackageCode(packageCode);
		packageInfoDetails.setPackageInstruction(packageInstruction);
		
		listShippingWeights.add(packageInfoDetails);
		Cimm2BCentralAddress shipperAddress = new Cimm2BCentralAddress();
		shipperAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipperAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipperAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipperAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipperAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipperAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		upsFreightdetials.setShipperAddress(shipperAddress);
		
		Cimm2BCentralAddress shipToAddress = new Cimm2BCentralAddress();
		shipToAddress.setAddressLine1(userShipTo.getAddress1());
		//shipToAddress.setAddressLine2(userShipTo.getAddress2());
		shipToAddress.setCity(userShipTo.getCity());
		shipToAddress.setState(userShipTo.getState());
		shipToAddress.setCountryCode(userShipTo.getCountry());
		shipToAddress.setZipCode(userShipTo.getZipCode());
		upsFreightdetials.setShipToAddress(shipToAddress);
		
		Cimm2BCentralAddress shipFromAddress = new Cimm2BCentralAddress();
		shipFromAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipFromAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipFromAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipFromAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipFromAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipFromAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		upsFreightdetials.setShipFromAddress(shipFromAddress);
		
		upsFreightdetials.setPackageInfo(new PackageInfo(packageCode, packageInstruction,defaultUom, CommonUtility.validateParseDoubleToString(totalCartWeight)));
		//upsFreightdetials.setShippingWeight(new PackageInfo("02", "Package", defaultUom, CommonUtility.validateParseDoubleToString(totalCartWeight)));
		upsFreightdetials.setListOfshippingWeight(listShippingWeights);
		upsFreightdetials.setServiceCode("0"+serviceCode);
		if(freightParameters.getShipViaMethod()!=null){
			upsFreightdetials.setServiceName(freightParameters.getShipViaMethod()!=null?freightParameters.getShipViaMethod():freightParameters.getShipViaDescription());
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return upsFreightdetials;
	}
	
	public static UpsFreight getUpsFrieghtModel(UsersModel userShipTo, WarehouseModel wareHouseDetails, double totalCartWeight, String serviceCode, String serviceName){
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		String defaultUom =  CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CART_WEIGHT_UOM"));
		String packageCode = "02";
		String packageInstruction = "Package";
		UpsFreight upsFreightdetials = new UpsFreight();
		try {
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels")).length()>0){
			 packageCode=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"));
		}
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels")).length()>0){
			packageInstruction=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"));
		}
		ContactDetails shipperContact = new ContactDetails();
		shipperContact.setFirstName(wareHouseDetails.getWareHouseName()!=null?wareHouseDetails.getWareHouseName():"");
		shipperContact.setPrimaryPhoneNumber(wareHouseDetails.getPhone()!=null?wareHouseDetails.getPhone():"");
		upsFreightdetials.setShipperContact(shipperContact);
		
		ContactDetails shipToContact = new ContactDetails();
		shipToContact.setFirstName(userShipTo.getFirstName()!=null?userShipTo.getFirstName():".");
		shipToContact.setPrimaryPhoneNumber(userShipTo.getPhoneNo());
		upsFreightdetials.setShipToContact(shipToContact);
		
		ContactDetails shipFromContact = new ContactDetails();
		shipFromContact.setFirstName(wareHouseDetails.getWareHouseName()!=null?wareHouseDetails.getWareHouseName():"");
		shipFromContact.setPrimaryPhoneNumber(wareHouseDetails.getPhone()!=null?wareHouseDetails.getPhone():"");
		upsFreightdetials.setShipFromContact(shipFromContact);

		ArrayList<PackageInfo> listShippingWeights = new ArrayList<PackageInfo>();
		PackageInfo packageInfoDetails = new PackageInfo();
		
		packageInfoDetails.setWeight(CommonUtility.validateParseDoubleToString(totalCartWeight));
		packageInfoDetails.setUom(defaultUom);
		packageInfoDetails.setPackageCode(packageCode);
		packageInfoDetails.setPackageInstruction(packageInstruction);
		
		listShippingWeights.add(packageInfoDetails);
		Cimm2BCentralAddress shipperAddress = new Cimm2BCentralAddress();
		shipperAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipperAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipperAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipperAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipperAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipperAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		upsFreightdetials.setShipperAddress(shipperAddress);
		
		Cimm2BCentralAddress shipToAddress = new Cimm2BCentralAddress();
		shipToAddress.setAddressLine1(userShipTo.getAddress1());
		//shipToAddress.setAddressLine2(userShipTo.getAddress2());
		shipToAddress.setCity(userShipTo.getCity());
		shipToAddress.setState(userShipTo.getState());
		shipToAddress.setCountryCode(userShipTo.getCountry());
		shipToAddress.setZipCode(userShipTo.getZipCode());
		upsFreightdetials.setShipToAddress(shipToAddress);
		
		Cimm2BCentralAddress shipFromAddress = new Cimm2BCentralAddress();
		shipFromAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipFromAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipFromAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipFromAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipFromAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipFromAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		upsFreightdetials.setShipFromAddress(shipFromAddress);
		
		upsFreightdetials.setPackageInfo(new PackageInfo(packageCode, packageInstruction,defaultUom, CommonUtility.validateParseDoubleToString(totalCartWeight)));
		//upsFreightdetials.setShippingWeight(new PackageInfo("02", "Package", defaultUom, CommonUtility.validateParseDoubleToString(totalCartWeight)));
		upsFreightdetials.setListOfshippingWeight(listShippingWeights);
		upsFreightdetials.setServiceCode("0"+serviceCode);
		upsFreightdetials.setServiceName(serviceName);
	}
	catch (Exception e) {
		e.printStackTrace();
	}
		
		return upsFreightdetials;
	}
	public static com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier getUpsFrieghtModelEsb(UsersModel userShipTo, WarehouseModel wareHouseDetails, double totalCartWeight, String serviceCode, String serviceName){
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		String defaultUom = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CART_WEIGHT_UOM"));
		String packageCode = "02";
		String packageInstruction = "Package";
		com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier upsFreightdetials = new com.unilog.cimmesb.client.ecomm.request.CimmShippingCarrier();
		try {
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels")).length()>0){
			 packageCode=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"));
		}
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels")).length()>0){
			packageInstruction=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"));
		}
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
		upsFreightdetials.setServiceName(serviceName);
	}
	catch (Exception e) {
		e.printStackTrace();
	}
		
		return upsFreightdetials;
	}
	
	public static UpsFreight getUpsFrieghtModelByItems(UsersModel userShipTo, WarehouseModel wareHouseDetails, List<ProductsModel> products, String serviceCode, String serviceName){
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		String packageCode = "02";
		String packageInstruction = "Package";
		UpsFreight upsFreightdetials = new UpsFreight();
		try {
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels")).length()>0){
			 packageCode=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"));
		}
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels")).length()>0){
			packageInstruction=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"));
		}
		ContactDetails shipperContact = new ContactDetails();
		shipperContact.setFirstName(wareHouseDetails.getWareHouseName()!=null?wareHouseDetails.getWareHouseName():"");
		shipperContact.setPrimaryPhoneNumber(wareHouseDetails.getPhone()!=null?wareHouseDetails.getPhone():"");
		upsFreightdetials.setShipperContact(shipperContact);
		
		ContactDetails shipToContact = new ContactDetails();
		shipToContact.setFirstName(userShipTo.getFirstName()!=null?userShipTo.getFirstName():".");
		shipToContact.setPrimaryPhoneNumber(userShipTo.getPhoneNo());
		upsFreightdetials.setShipToContact(shipToContact);
		
		ContactDetails shipFromContact = new ContactDetails();
		shipFromContact.setFirstName(wareHouseDetails.getWareHouseName()!=null?wareHouseDetails.getWareHouseName():"");
		shipFromContact.setPrimaryPhoneNumber(wareHouseDetails.getPhone()!=null?wareHouseDetails.getPhone():"");
		upsFreightdetials.setShipFromContact(shipFromContact);

		Cimm2BCentralAddress shipperAddress = new Cimm2BCentralAddress();
		shipperAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipperAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipperAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipperAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipperAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipperAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		upsFreightdetials.setShipperAddress(shipperAddress);
		
		Cimm2BCentralAddress shipToAddress = new Cimm2BCentralAddress();
		shipToAddress.setAddressLine1(userShipTo.getAddress1());
		//shipToAddress.setAddressLine2(userShipTo.getAddress2());
		shipToAddress.setCity(userShipTo.getCity());
		shipToAddress.setState(userShipTo.getState());
		shipToAddress.setCountryCode(userShipTo.getCountry());
		shipToAddress.setZipCode(userShipTo.getZipCode());
		upsFreightdetials.setShipToAddress(shipToAddress);
		
		Cimm2BCentralAddress shipFromAddress = new Cimm2BCentralAddress();
		shipFromAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipFromAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipFromAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipFromAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipFromAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipFromAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		upsFreightdetials.setShipFromAddress(shipFromAddress);
		upsFreightdetials.setPackageInfo(new PackageInfo(packageCode,packageInstruction,"", ""));
		
		if(CommonUtility.customServiceUtility() != null) {
			upsFreightdetials.setListOfshippingWeight(CommonUtility.customServiceUtility().getItemWeights(products));
		}
		
		upsFreightdetials.setServiceCode("0"+serviceCode);
		upsFreightdetials.setServiceName(serviceName);
	}
	catch (Exception e) {
		e.printStackTrace();
	}
		return upsFreightdetials;
	}
	
	
	public static TimeInTransit getTimeInTransitModel(UsersModel userShipTo, WarehouseModel wareHouseDetails,double weight, String pickUpDate){
		HttpServletRequest httpRequest=ServletActionContext.getRequest();
		HttpSession session = httpRequest.getSession();
		String defaultUom =  CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CART_WEIGHT_UOM"));
		String packageCode = "02";
		String packageInstruction = "Package";
		TimeInTransit timeInTransit = new TimeInTransit();
		try {
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels")).length()>0){
			 packageCode=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageCode.labels"));
		}
		if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"))!=null &&  CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels")).length()>0){
			packageInstruction=CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("packageInstruction.labels"));
		}
		Cimm2BCentralAddress shipToAddress = new Cimm2BCentralAddress();
		shipToAddress.setAddressLine1(userShipTo.getAddress1());
		//shipToAddress.setAddressLine2(userShipTo.getAddress2());
		shipToAddress.setCity(userShipTo.getCity());
		shipToAddress.setState(userShipTo.getState());
		shipToAddress.setCountryCode(userShipTo.getCountry());
		shipToAddress.setZipCode(userShipTo.getZipCodeStringFormat());
		timeInTransit.setShipToAddress(shipToAddress);
		
		Cimm2BCentralAddress shipFromAddress = new Cimm2BCentralAddress();
		shipFromAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipFromAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipFromAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipFromAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipFromAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipFromAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		timeInTransit.setShipFromAddress(shipFromAddress);
		
		timeInTransit.setShippingWeight(new PackageInfo(packageCode, packageInstruction, defaultUom, CommonUtility.validateParseDoubleToString(weight)));

		timeInTransit.setPickUpDate(pickUpDate);
	}
	catch (Exception e) {
		e.printStackTrace();
	}
		
		return timeInTransit;
	}
	
	public static Cimm2BCentralAddress getCIMM2BCAddressModel(AddressModel address){
		Cimm2BCentralAddress cimm2BCAddress = new Cimm2BCentralAddress();
		try {
		cimm2BCAddress.setAddressLine1(address.getAddress1());
		//cimm2BCAddress.setAddressLine2(address.getAddress2());
		cimm2BCAddress.setCity(address.getCity());
		cimm2BCAddress.setState(address.getState());
		cimm2BCAddress.setCountry(address.getCountry());
		cimm2BCAddress.setCountryCode(address.getCountry());
	}
	catch (Exception e) {
		e.printStackTrace();
	}
		return cimm2BCAddress;
	}
	
	public static FedExFreight getFedExFreightModel(UsersModel userShipTo, WarehouseModel wareHouseDetails, PackageInfo packageInfo, PackageDimension packageDimension, String dropType, String serviceName, String packageType, List<ProductsModel> cartListdata){
		FedExFreight fedExFreight = new FedExFreight();
		try {
		Cimm2BCentralContact shipFromContact = new Cimm2BCentralContact();
		shipFromContact.setTitle(wareHouseDetails.getWareHouseCode() + wareHouseDetails.getWareHouseName());
		shipFromContact.setFirstName(wareHouseDetails.getWareHouseName());
		shipFromContact.setCompanyName(userShipTo.getCompanyName());
		shipFromContact.setPrimaryPhoneNumber(wareHouseDetails.getPhone());
		shipFromContact.setPrimaryEmailAddress(wareHouseDetails.getEmailAddress());
		fedExFreight.setShipFromContact(shipFromContact);
		
		Cimm2BCentralAddress shipFromAddress = new Cimm2BCentralAddress();
		shipFromAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipFromAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipFromAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipFromAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipFromAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipFromAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		fedExFreight.setShipFromAddress(shipFromAddress);
		
		Cimm2BCentralContact shipToContact = new Cimm2BCentralContact();
		shipToContact.setTitle(userShipTo.getFirstName()!=null?userShipTo.getFirstName():"." + userShipTo.getLastName());
		shipToContact.setFirstName(userShipTo.getFirstName()!=null?userShipTo.getFirstName():".");
		shipToContact.setCompanyName(userShipTo.getCompanyName());
		shipToContact.setPrimaryPhoneNumber(userShipTo.getPhoneNo());
		shipToContact.setPrimaryEmailAddress(userShipTo.getEmailAddress());
		fedExFreight.setShipToContact(shipToContact);
		
		Cimm2BCentralAddress shipToAddress = new Cimm2BCentralAddress();
		shipToAddress.setAddressLine1(userShipTo.getAddress1());
		shipToAddress.setCity(userShipTo.getCity());
		shipToAddress.setState(userShipTo.getState());
		shipToAddress.setCountryCode(userShipTo.getCountry());
		shipToAddress.setZipCode(userShipTo.getZipCode());
		fedExFreight.setShipToAddress(shipToAddress);
		
		fedExFreight.setPackageDimension(packageDimension);
		fedExFreight.setPackageInfo(packageInfo);
		fedExFreight.setDropType(dropType);
		fedExFreight.setPackageType(packageType);
		fedExFreight.setServiceName(serviceName);
		
		for(ProductsModel eachProduct : cartListdata){
			Cimm2BCentralLineItem lineItem = new Cimm2BCentralLineItem();
			lineItem.setPartNumber(eachProduct.getPartNumber());
			lineItem.setItemId(String.valueOf(eachProduct.getItemId()));
			lineItem.setLineItemComment((eachProduct.getLineItemComment()==null)?eachProduct.getLineItemComment():"Content Description");
			fedExFreight.getLineItems().add(lineItem);
		}
	}
	catch (Exception e) {
		e.printStackTrace();
	}
		return fedExFreight;
	}
	public static com.unilog.cimmesb.client.ecomm.request.FedExFreight getFedExFreightModelEsb(UsersModel userShipTo, WarehouseModel wareHouseDetails, com.unilog.cimmesb.client.ecomm.request.PackageInfo packageInfo, com.unilog.cimmesb.client.ecomm.request.PackageDimension packageDimension, String dropType, String serviceName, String packageType, List<ProductsModel> cartListdata){
		com.unilog.cimmesb.client.ecomm.request.FedExFreight fedExFreight = new com.unilog.cimmesb.client.ecomm.request.FedExFreight();
		try {
		Cimm2BContact shipFromContact = new Cimm2BContact();
		shipFromContact.setTitle(wareHouseDetails.getWareHouseCode() + wareHouseDetails.getWareHouseName());
		shipFromContact.setFirstName(wareHouseDetails.getWareHouseName());
		shipFromContact.setCompanyName(userShipTo.getCompanyName());
		shipFromContact.setPrimaryPhoneNumber(wareHouseDetails.getPhone());
		shipFromContact.setPrimaryEmailAddress(wareHouseDetails.getEmailAddress());
		fedExFreight.setShipFromContact(shipFromContact);
		
		Cimm2BAddress shipFromAddress = new Cimm2BAddress();
		shipFromAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
		//shipFromAddress.setAddressLine2(wareHouseDetails.getAddress2());
		shipFromAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
		shipFromAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
		shipFromAddress.setCountryCode(wareHouseDetails.getCountry()!=null?wareHouseDetails.getCountry():"");
		shipFromAddress.setZipCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
		fedExFreight.setShipToAddress(shipFromAddress);
		
		Cimm2BContact shipToContact = new Cimm2BContact();
		shipToContact.setTitle(userShipTo.getFirstName()!=null?userShipTo.getFirstName():"." + userShipTo.getLastName());
		shipToContact.setFirstName(userShipTo.getFirstName()!=null?userShipTo.getFirstName():".");
		shipToContact.setCompanyName(userShipTo.getCompanyName());
		shipToContact.setPrimaryPhoneNumber(userShipTo.getPhoneNo());
		shipToContact.setPrimaryEmailAddress(userShipTo.getEmailAddress());
		fedExFreight.setShipToContact(shipToContact);
		
		Cimm2BAddress shipToAddress = new Cimm2BAddress();
		shipToAddress.setAddressLine1(userShipTo.getAddress1());
		shipToAddress.setCity(userShipTo.getCity());
		shipToAddress.setState(userShipTo.getState());
		shipToAddress.setCountryCode(userShipTo.getCountry());
		shipToAddress.setZipCode(userShipTo.getZipCode());
		fedExFreight.setShipFromAddress(shipToAddress);
		
		fedExFreight.setPackageDimension(packageDimension);
		fedExFreight.setPackageInfo(packageInfo);
		fedExFreight.setDropType(dropType);
		fedExFreight.setPackageType(packageType);
		fedExFreight.setServiceName(serviceName);
		
		for(ProductsModel eachProduct : cartListdata){
			Cimm2BLineItem lineItem = new Cimm2BLineItem();
			lineItem.setPartNumber(eachProduct.getPartNumber());
			lineItem.setItemId(String.valueOf(eachProduct.getItemId()));
			lineItem.setLineItemComment((eachProduct.getLineItemComment()==null)?eachProduct.getLineItemComment():"Content Description");
			fedExFreight.getLineItems().add(lineItem);
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return fedExFreight;
	}
}
