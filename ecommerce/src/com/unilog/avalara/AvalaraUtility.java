package com.unilog.avalara;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.ws.rs.HttpMethod;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.unilog.avalara.model.Addresses;
import com.unilog.avalara.model.AvalaraAddress;
import com.unilog.avalara.model.AvalaraAddressResponse;
import com.unilog.avalara.model.AvalaraModel;
import com.unilog.avalara.model.Lines;
import com.unilog.avalara.model.TaxResponse;
import com.unilog.cimmesb.client.ecomm.response.CimmV2LineItem;
import com.unilog.cimmesb.client.ecomm.response.CimmV2TaxAddress;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

public class AvalaraUtility {

private static AvalaraUtility avalaraUtility = null;
	
	private AvalaraUtility(){
		
	} 
	
	public static AvalaraUtility getInstance(){
		synchronized (AvalaraUtility.class) {
			if(avalaraUtility==null){
				avalaraUtility = new AvalaraUtility();
			}
		}
		return avalaraUtility;
	}
	
	
	public Cimm2BCentralResponseEntity getTax(ArrayList<ProductsModel> cartListData, AddressModel overrideShipAddress, String wareHouse, String poNumber){
		Cimm2BCentralResponseEntity response = null;
		try{
			int i = 1;
			ProductsModel fromShipAddress = CommonDBQuery.branchDetailData.get(wareHouse);
			 Date currentDate = new Date();
		     String date= new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
			AvalaraModel avalaraTax = new AvalaraModel();
			avalaraTax.setCustomerCode(CommonDBQuery.getSystemParamtersList().get("AVALARA_CUSTOMER_CODE"));
			avalaraTax.setDocCode(poNumber);
			avalaraTax.setDocDate(date);
			
			ArrayList<Addresses> addresses = new ArrayList<Addresses>();
			Addresses address = new Addresses();
			address.setAddressCode(CommonUtility.validateString(overrideShipAddress.getShipToId()).trim().equals("")?"01":CommonUtility.validateString(overrideShipAddress.getShipToId()));
			address.setCity(overrideShipAddress.getCity());
			address.setCountry(overrideShipAddress.getCountry());
			address.setLine1(overrideShipAddress.getAddress1());
			address.setLine2(overrideShipAddress.getAddress2());
			address.setLine3("");
			address.setPostalCode(overrideShipAddress.getZipCode());
			address.setRegion(overrideShipAddress.getState());
			//CustomServiceProvider
			if(CommonUtility.customServiceUtility()!=null) {
				String address1 = CommonUtility.customServiceUtility().addressLine1Combined(overrideShipAddress);
				if(CommonUtility.validateString(address1).length()>0) {
					address.setLine1(address1);
				}
			}
			//CustomServiceProvider
			addresses.add(address);
			avalaraTax.setAddresses(addresses);
			if(fromShipAddress!=null){
				address = new Addresses();
				address.setAddressCode(fromShipAddress.getBranchID().trim().equals("")?"00":fromShipAddress.getBranchID().trim());
				address.setCity(fromShipAddress.getBranchCity());
				address.setCountry(fromShipAddress.getBranchCountry());
				address.setLine1(fromShipAddress.getBranchAddress1());
				address.setLine2(fromShipAddress.getBranchAddress2());
				address.setLine3("");
				address.setPostalCode(fromShipAddress.getBranchPostalCode());
				address.setRegion(fromShipAddress.getBranchState());
				addresses.add(address);
				avalaraTax.setAddresses(addresses);
			}
			ArrayList<Lines> lines = new ArrayList<Lines>();
			for(ProductsModel products:cartListData){
				Lines lineItem = new Lines();
				if(products.getQty()>0) {
					lineItem.setAmount(products.getPrice() * products.getQty());
				}else {
				lineItem.setAmount(products.getPrice());
				}
				lineItem.setDestinationCode(CommonUtility.validateString(overrideShipAddress.getShipToId()).equals("")?"01":CommonUtility.validateString(overrideShipAddress.getShipToId()).trim());
				lineItem.setItemCode(products.getPartNumber());
				lineItem.setLineNo(i+"");
				lineItem.setOriginCode(fromShipAddress == null || fromShipAddress.getBranchID().trim().equals("")?"00":fromShipAddress.getBranchID().trim());
				lineItem.setQty(products.getQty());
				lines.add(lineItem);
				i++;
			}
			avalaraTax.setLines(lines);	
			response = Cimm2BCentralClient.getInstance().getDataObject(CommonDBQuery.getSystemParamtersList().get("AVALARA_TAX_API"), HttpMethod.POST, avalaraTax, TaxResponse.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public Cimm2BCentralResponseEntity getAddress(UsersModel validateAddress){
		Cimm2BCentralResponseEntity response = null;
		try{
			AvalaraAddress address = new AvalaraAddress();
			if(validateAddress!=null){
				address.setCity(validateAddress.getCity());
				address.setRegion(validateAddress.getState());
				address.setCountry(validateAddress.getCountry());
				address.setPostalCode(validateAddress.getZipCode());
				address.setLine1(validateAddress.getAddress1());
				address.setLine2(validateAddress.getAddress2());
				address.setLine3("");
				
			}	
			response = Cimm2BCentralClient.getInstance().getDataObject(CommonDBQuery.getSystemParamtersList().get("AVALARA_ADDRESS_API"), HttpMethod.POST, address, AvalaraAddressResponse.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static com.unilog.cimmesb.client.ecomm.request.CimmV2TaxRequest getTaxModelEsb(LinkedHashMap<String, Object> taxParameters){
		
		com.unilog.cimmesb.client.ecomm.request.CimmV2TaxRequest taxRequest=null;
		WarehouseModel wareHouseDetails=null;
		
		try {
			taxRequest=new com.unilog.cimmesb.client.ecomm.request.CimmV2TaxRequest();
			UsersModel userShipToAddress = (UsersModel) taxParameters.get("shipAddress");

			if(taxParameters.get("wareHousecode")!=null) {
				wareHouseDetails = new WarehouseModel();
				wareHouseDetails = UsersDAO.getWareHouseDetailsByCode((String)taxParameters.get("wareHousecode"));
			}
			
			taxRequest.setCartId((String)taxParameters.get("ExternalCartId"));
			taxRequest.setDeliveredBySeller(false);
			
			com.unilog.cimmesb.client.ecomm.response.CimmV2TaxAddress destinationAddress=new CimmV2TaxAddress();
			destinationAddress.setAddressLine1(userShipToAddress.getAddress1());
			destinationAddress.setCity(userShipToAddress.getCity());
			destinationAddress.setState(userShipToAddress.getState());
			destinationAddress.setPostalCode(userShipToAddress.getZipCode());
			taxRequest.setDestinationAddress(destinationAddress);
			
			com.unilog.cimmesb.client.ecomm.response.CimmV2TaxAddress originAddress =new CimmV2TaxAddress();
			originAddress.setAddressLine1(wareHouseDetails.getAddress1()!=null?wareHouseDetails.getAddress1():"");
			originAddress.setCity(wareHouseDetails.getCity()!=null?wareHouseDetails.getCity():"");
			originAddress.setState(wareHouseDetails.getState()!=null?wareHouseDetails.getState():"");
			originAddress.setPostalCode(wareHouseDetails.getZip()!=null?wareHouseDetails.getZip():"");
			
			taxRequest.setOriginAddress(originAddress);
			
			ArrayList<CimmV2LineItem> lineItemList=new ArrayList<>();
			CimmV2LineItem lineItems=null;
			for (ProductsModel products:(ArrayList<ProductsModel>) taxParameters.get("itemList"))
			{
				lineItems=new CimmV2LineItem();
				lineItems.setItemId(products.getPartNumber());
				lineItems.setQty(products.getQty());
				lineItems.setPrice(products.getPrice()*products.getQty());
				lineItemList.add(lineItems);
			}
			taxRequest.setLineItems(lineItemList);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taxRequest;
	}
}
