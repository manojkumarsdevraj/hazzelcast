package com.erp.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.erp.service.model.ProductManagementModel;
import com.unilog.products.ProductsModel;
import com.unilog.products.model.AssociateItemsModel;
import com.unilog.users.UsersModel;

public interface ProductManagement {

	public ArrayList<ProductsModel> priceInquiry(ProductManagementModel priceInquiryInput,ArrayList<ProductsModel> productList);
	public String ajaxPriceInquiry(ProductManagementModel priceInquiryInput);
	public ArrayList<ProductsModel>customerPartNumberQuery(ProductManagementModel customerPartNumberInquiryInput);
	public ArrayList<ProductsModel>getAlternateItems(ProductManagementModel alternateItems);
	public ProductManagementModel customerPartNumberCreate(ProductManagementModel customerPartNumberInquiryInput);
	public ProductManagementModel customerPartNumberDelete(ProductManagementModel customerPartNumberInquiryInput);
	public ProductManagementModel customerPartNumberInquiry(ProductManagementModel customerPartNumberInquiryInput);
	public String branchAvailabile(String pns,String branch,String entityId,String userToken);
	public UsersModel getCustomerDetail(UsersModel priceInquiryInput);
	public String ajaxBasePriceAndLeadTime(ProductManagementModel priceInquiryInput);
	public ArrayList<ProductsModel>  itemsMultipleUOMList(String partNumber);
	public String getAllBranchAvailabilityForProductCode(ProductManagementModel priceInquiryInput);
	public LinkedHashMap<String, Object> getItemHistoryDetails(ProductsModel productParameters);
	public AssociateItemsModel associateItems(ProductManagementModel associateInputItems);
}
