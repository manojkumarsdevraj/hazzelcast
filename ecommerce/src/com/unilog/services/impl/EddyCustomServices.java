package com.unilog.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerCard;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.misc.EventModel;
import com.unilog.products.ProductsModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class EddyCustomServices implements UnilogFactoryInterface {

	private static UnilogFactoryInterface serviceProvider;
	private EddyCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (EddyCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new EddyCustomServices();
				}
			}
		return serviceProvider;
	}    	
	public void getBranchCode(Cimm2BCentralOrder order, String warehouseCode) {
		order.setBranchCode(warehouseCode);
	}

	public void getListPrice(ProductsModel itemModel, Cimm2BCentralLineItem lineItem) {
		if(itemModel.getUnitPrice()>0) {
			lineItem.setListPrice(itemModel.getUnitPrice());
		}else if(itemModel.getPrice()>0) {
			lineItem.setListPrice(itemModel.getPrice());
		}else {
			lineItem.setListPrice(0);
		}
	}
	@Override
	public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
    {   
        String newsLetterCustomFiledValue = "N";
        if(CommonUtility.validateString(userDetailsInput.getNewsLetterSub()).length() > 0 && userDetailsInput.getNewsLetterSub().equalsIgnoreCase("ON")){
            newsLetterCustomFiledValue="Y";
        }
        UsersDAO.insertCustomField(newsLetterCustomFiledValue,"NEWSLETTER",userid, buyingCompanyid,"USER");
    }
	public String setFirstLoginTrueForInactiveUser() {
		return "Y";
	}
}
	