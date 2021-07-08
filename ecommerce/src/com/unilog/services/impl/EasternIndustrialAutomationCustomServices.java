package com.unilog.services.impl;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerCard;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.AddressModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class EasternIndustrialAutomationCustomServices implements UnilogFactoryInterface {
	private static UnilogFactoryInterface serviceProvider;
	private EasternIndustrialAutomationCustomServices() {}
	public static UnilogFactoryInterface getInstance() {
			synchronized (EasternIndustrialAutomationCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new EasternIndustrialAutomationCustomServices();
				}
		}
		return serviceProvider;
	}
	
	public double getdefaultCustomerPrice(ProductsModel eclipseitemPrice,ProductsModel itemPrice,double price) {
		double defaultCustprice = price + eclipseitemPrice.getPrice();
		return defaultCustprice;
	}
	
	public void getUnitPrice(ProductsModel eclipseitemPrice,ProductsModel itemPrice) {
			itemPrice.setUnitPrice(eclipseitemPrice.getPrice());
	}
	
	public String setFirstLoginTrueForInactiveUser() {
		return "Y";
	}
	
	public String getRetailUserOrdersFromCIMM(String userRole) {
		if(userRole=="Y") {
			return userRole;
		}else{
			userRole="N";
		}
		return userRole;
	}
	
	public UsersModel getUserContactAddress(int userId,  HttpSession session) {
		UsersModel addressList = null;
		String isRetailUser = (String) session.getAttribute("isRetailUser");
		if(isRetailUser=="Y"){
			addressList = new UsersModel();
			addressList= UsersDAO.getEntityDetailsByUserId(userId);
			session.setAttribute("userContactAddress", addressList);
		}
		return addressList;
	}
	
	public void getRetailCustomerUserERPId(UsersModel contactInformation,String custNumber) {
		if(custNumber!=null) {
			contactInformation.setUserERPId(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIL_CUSTOMERS_USER_CONTACT_ID")));
		}
	}
	
	public String setPricingDefaultWareHouseCode(String warehouseCode) {
		if(warehouseCode!=null) {
			warehouseCode = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_PRICING_WAREHOUSE_CODE"));
		}
		return warehouseCode;
	}
	
	public void setBillToShipToAddressFields(String firstName, UsersModel userBillAddress, AddressModel selectedShipAddress, HttpSession session){
		String isRetailUser = (String) session.getAttribute("isRetailUser");
		selectedShipAddress.setCompanyName(CommonUtility.validateString(firstName));
		if(isRetailUser=="Y"){
			session.setAttribute("shipTofirstName",CommonUtility.validateString(firstName));
			selectedShipAddress.setCompanyName(CommonUtility.validateString(firstName));
			AddressModel overrideBillAddress = (AddressModel) session.getAttribute("overrideBillAddress");
			if(overrideBillAddress!=null){
			if(CommonUtility.validateString(overrideBillAddress.getCompanyName()).length()>0) {
				String[] billToName = overrideBillAddress.getCompanyName().split(" ");
				if(billToName!=null && billToName.length>0){
					userBillAddress.setFirstName(CommonUtility.validateString(billToName[0]));
					userBillAddress.setLastName(CommonUtility.validateString(billToName[1]));
				}
				userBillAddress.setCustomerName(overrideBillAddress.getCompanyName());
				userBillAddress.setCompanyName(overrideBillAddress.getCompanyName());
			}
		  }
		}
	}
	
	public String getPartnumberUomQtySplitChar() {
		String partNumberUomQtySeparator = CommonDBQuery.getSystemParamtersList().get("PARTNUMBER_UOM_QTY_SPLIT_CHARACTER");
		return partNumberUomQtySeparator;
	}
}
