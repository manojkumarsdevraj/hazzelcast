package com.erp.service.defaults.action;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.erp.service.model.ProductManagementModel;
import com.google.gson.Gson;
import com.unilog.products.ProductsModel;
import com.unilog.utility.CommonUtility;

public class ProductManagementAction {

	public static ArrayList<ProductsModel> getPriceFromERP(ProductManagementModel priceInquiryInput,ArrayList<ProductsModel> productList){

		DecimalFormat df = CommonUtility.getPricePrecision(priceInquiryInput.getSession());
		//DecimalFormat df1 = new DecimalFormat("####0.#####");
		try{
			double total;
			total = 0;
			if(productList!=null && productList.size()>0)
				for(ProductsModel itemPrice : productList){
					double extPrice = itemPrice.getPrice();
					String twoDecimalPrice = df.format(extPrice);
					itemPrice.setTotal( Double.parseDouble(twoDecimalPrice) * itemPrice.getQty());
					String twoDecimalTotal = df.format(itemPrice.getTotal());
					System.out.println("Two Decimal Price : " + twoDecimalPrice);
					total = total + Double.parseDouble(twoDecimalTotal);
					System.out.println("total : "+total);
					itemPrice.setCartTotal(total);
				}
			if(productList!=null && productList.size()>0)
				productList.get(0).setCartTotal(total);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return productList;
	}
	public static ArrayList<ProductsModel> customerPartNumberQuery(ProductManagementModel priceInquiryInput) {

		ArrayList<ProductsModel> customerPartNumberlist = new ArrayList<ProductsModel>();

		try{
			customerPartNumberlist = null;


		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return customerPartNumberlist;
	}
	public static ArrayList<ProductsModel> getAlternateItems(ProductManagementModel priceInquiryInput) {

		ArrayList<ProductsModel> alternateItemsList = new ArrayList<ProductsModel>();

		try{
			alternateItemsList = null;


		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return alternateItemsList;
	}

	public static ProductManagementModel customerPartNumberCreate(ProductManagementModel priceInquiryInput) {

		ProductManagementModel customerPartNumberResponse = new ProductManagementModel();

		try{

			customerPartNumberResponse.setErrorCode("0");
			customerPartNumberResponse.setErrorMessage(null);



		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return customerPartNumberResponse;
	}

	public static ProductManagementModel customerPartNumberDelete(ProductManagementModel priceInquiryInput) {

		ProductManagementModel customerPartNumberResponse = new ProductManagementModel();

		try{
			customerPartNumberResponse.setErrorCode("0");
			customerPartNumberResponse.setErrorMessage(null);

		}catch (Exception e) {
			
			e.printStackTrace();
		}
		return customerPartNumberResponse;
	}

	public static String ajaxBasePriceAndLeadTimeFromErp(ProductManagementModel priceInquiryInput)
	{

		String jsonResponse = "";
		Gson gson = new Gson();
		try
		{
			ArrayList<ProductsModel> priceResponse = new ArrayList<ProductsModel>();
			jsonResponse = gson.toJson(priceResponse);
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}

		return jsonResponse;
	}
	public static ArrayList<ProductsModel> getItemMultipleUOMData(String partNumber){
		return null;
	}

}
