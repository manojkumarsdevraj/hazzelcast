package com.erp.service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;

import com.unilog.products.ProductsModel;

public class ProductManagementModel {

	private String userToken;
	private String customerId;
	private ArrayList<ProductsModel> partIdentifier;
	private String partIdentifierString;
	private String entityId;
	private String userName;
	private String requiredAvailabilty;
	private String homeTerritory;
	private String productNumber;
	private String ErrorMessage;
	private String ErrorCode;
	private String[] customerPartNumbers;
	private String wareHouse;
	private String customerCountry;
	private String buyingCompany;
	private HttpSession session;
	private boolean fromDetailPage;
	private ArrayList<Integer> partIdentifierQuantity;
	private ArrayList<String> customerPartNumberList;
	private HashMap<String, ArrayList<ProductsModel>> itemsCpnList;
	private String allBranchavailabilityRequired;
	private String oldCustomerPartNumber;
	private String newCustomerPartNumber; 
	private String shipToId;
	private String recordType;
	private String recordDescription;
	private boolean flag; 
	private LinkedHashMap<String, String> defaultHashMap;
	private String requestFrom;
	private String zipCode;
	private int qty;
	private String associatedItems;
	private String substitutedItems;
	
	
	public String getAssociatedItems() {
		return associatedItems;
	}
	public void setAssociatedItems(String associatedItems) {
		this.associatedItems = associatedItems;
	}
	public String getSubstitutedItems() {
		return substitutedItems;
	}
	public void setSubstitutedItems(String substitutedItems) {
		this.substitutedItems = substitutedItems;
	}
	
	
    public String getRequestFrom() {
                return requestFrom;
    }
    public void setRequestFrom(String requestFrom) {
                this.requestFrom = requestFrom;
    }

	public LinkedHashMap<String, String> getDefaultHashMap() {
		return defaultHashMap;
	}
	public void setDefaultHashMap(LinkedHashMap<String, String> defaultHashMap) {
		this.defaultHashMap = defaultHashMap;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getRecordDescription() {
		return recordDescription;
	}
	public void setRecordDescription(String recordDescription) {
		this.recordDescription = recordDescription;
	}
	public String getShipToId() {
		return shipToId;
	}
	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getAllBranchavailabilityRequired() {
		return allBranchavailabilityRequired;
	}
	public void setAllBranchavailabilityRequired(
			String allBranchavailabilityRequired) {
		this.allBranchavailabilityRequired = allBranchavailabilityRequired;
	}
	public HashMap<String, ArrayList<ProductsModel>> getItemsCpnList() {
		return itemsCpnList;
	}
	public void setItemsCpnList(HashMap<String, ArrayList<ProductsModel>> itemsCpnList) {
		this.itemsCpnList = itemsCpnList;
	}
	public ArrayList<String> getCustomerPartNumberList() {
		return customerPartNumberList;
	}
	public void setCustomerPartNumberList(ArrayList<String> customerPartNumberList) {
		this.customerPartNumberList = customerPartNumberList;
	}
	public ArrayList<Integer> getPartIdentifierQuantity() {
		return partIdentifierQuantity;
	}
	public void setPartIdentifierQuantity(ArrayList<Integer> partIdentifierQuantity) {
		this.partIdentifierQuantity = partIdentifierQuantity;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public boolean isFromDetailPage() {
		return fromDetailPage;
	}
	public void setFromDetailPage(boolean fromDetailPage) {
		this.fromDetailPage = fromDetailPage;
	}
	public String[] getCustomerPartNumbers() {
		return customerPartNumbers;
	}
	public void setCustomerPartNumbers(String[] customerPartNumbers) {
		this.customerPartNumbers = customerPartNumbers;
	}
	public String getErrorMessage() {
		return ErrorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}
	public String getErrorCode() {
		return ErrorCode;
	}
	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setRequiredAvailabilty(String requiredAvailabilty) {
		this.requiredAvailabilty = requiredAvailabilty;
	}
	public String getRequiredAvailabilty() {
		return requiredAvailabilty;
	}
	public void setHomeTerritory(String homeTerritory) {
		this.homeTerritory = homeTerritory;
	}
	public String getHomeTerritory() {
		return homeTerritory;
	}
	public void setPartIdentifierString(String partIdentifierString) {
		this.partIdentifierString = partIdentifierString;
	}
	public String getPartIdentifierString() {
		return partIdentifierString;
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}
	public String getWareHouse() {
		return wareHouse;
	}
	public void setCustomerCountry(String customerCountry) {
		this.customerCountry = customerCountry;
	}
	public String getCustomerCountry() {
		return customerCountry;
	}
	public void setBuyingCompany(String buyingCompany) {
		this.buyingCompany = buyingCompany;
	}
	public String getBuyingCompany() {
		return buyingCompany;
	}
	public String getOldCustomerPartNumber() {
		return oldCustomerPartNumber;
	}
	public void setOldCustomerPartNumber(String oldCustomerPartNumber) {
		this.oldCustomerPartNumber = oldCustomerPartNumber;
	}
	public String getNewCustomerPartNumber() {
		return newCustomerPartNumber;
	}
	public void setNewCustomerPartNumber(String newCustomerPartNumber) {
		this.newCustomerPartNumber = newCustomerPartNumber;
	}
	public ArrayList<ProductsModel> getPartIdentifier() {
		return partIdentifier;
	}
	public void setPartIdentifier(ArrayList<ProductsModel> partIdentifier) {
		this.partIdentifier = partIdentifier;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}

	
}
