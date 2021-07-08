package com.unilog.services.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.unilog.products.ProductsModel;
import com.unilog.sales.CIMMTaxModel;
import com.unilog.services.UnilogFactoryInterface;


public class SafewareCustomServices implements UnilogFactoryInterface{
	
	private static UnilogFactoryInterface serviceProvider;	
	
	private SafewareCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (SafewareCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new SafewareCustomServices();
				}
			}
		return serviceProvider;
	}
	@Override
	public void configCartListValues(ProductsModel cartListVal, ResultSet rs) {
		try {
			cartListVal.setOverRidePriceRule(rs.getString("OVERRIDE_PRICE_RULE"));
			cartListVal.setPrice(rs.getDouble("PRICE"));
			cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
			cartListVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void configOrderItemValues(ProductsModel orderItemVal, ResultSet rs) {
		try {
			orderItemVal.setOverRidePriceRule(rs.getString("OVERRIDE_PRICE_RULE"));
			orderItemVal.setPrice(rs.getDouble("PRICE"));
			orderItemVal.setPartNumber(rs.getString("ALT_PART_NUMBER1"));
			orderItemVal.setAltPartNumber1(rs.getString("PART_NUMBER"));
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}