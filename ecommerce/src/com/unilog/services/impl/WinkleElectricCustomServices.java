package com.unilog.services.impl;

import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;


public class WinkleElectricCustomServices implements UnilogFactoryInterface{
	private static UnilogFactoryInterface serviceProvider;
	private WinkleElectricCustomServices() {}
	public static UnilogFactoryInterface getInstance() {
			synchronized (WinkleElectricCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new WinkleElectricCustomServices();
				}
			}
		return serviceProvider;
	}
	
	@Override
	 public Cimm2BCentralLineItem  disableUmqtyToErp(Cimm2BCentralLineItem cimm2bCentralLineItem){
		 cimm2bCentralLineItem.setUomQty("");
		 return cimm2bCentralLineItem;
	 }
	
	@Override
	 public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
		{	
			String newsLetterCustomFiledValue="";
			if(userDetailsInput.getNewsLetterSub()!=null && userDetailsInput.getNewsLetterSub().equalsIgnoreCase("ON")){
				newsLetterCustomFiledValue="Y";
			}else{
				newsLetterCustomFiledValue="N";
			}
			UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
		}
	 
	@Override
	 public String getRetailUserOrdersFromCIMM(String userRole) {
			if(userRole.equals("Y")) {
				userRole= "Y";
			}
				return userRole;
		}
	 @Override
	 public UsersModel getUserContactAddress(int userId,  HttpSession session) {
			UsersModel addressList = null;
			String isRetailUser = (String) session.getAttribute("isRetailUser");
			if(CommonUtility.validateString(isRetailUser).equalsIgnoreCase("Y")){
				addressList= UsersDAO.getEntityDetailsByUserId(userId);
				addressList.setAddressType("Ship");
				if(addressList!=null){
					session.setAttribute("userContactAddress", addressList);
				}
			}
			return addressList;
		}
	 
		
	@Override
	public void addfirstNameAndlastNameToshipAddress(AddressModel selectedShipAddress, UsersModel userShipAddress,HttpSession session) {
		selectedShipAddress.setFirstName(userShipAddress.getFirstName());
		selectedShipAddress.setLastName(userShipAddress.getLastName());
		selectedShipAddress.setShipToName(userShipAddress.getCustomerName());
		
	}
	@Override
	public double getdefaultCustomerPrice(ProductsModel eclipseitemPrice,ProductsModel itemPrice,double price) {
		return eclipseitemPrice.getPrice();
	}
	
	@Override
	 public void dontaddfreightcharges(SalesModel quoteInfo,Cimm2BCentralOrder orderResponse) {
		 if(orderResponse.getFreight()!=null && orderResponse.getFreight() > 0) {
			 quoteInfo.setTotal(orderResponse.getOrderTotal());
		 }
	 }
}