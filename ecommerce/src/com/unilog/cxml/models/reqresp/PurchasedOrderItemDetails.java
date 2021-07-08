/**
 *
 */
package com.unilog.cxml.models.reqresp;

import java.io.Serializable;
import java.util.LinkedHashMap;


/**
 * @author satish
 *
 */
public class PurchasedOrderItemDetails implements Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 8803340351460334434L;
	private int lineNumber;
	private int quantity;
	private String requestedDeliveryDate;
	private String agreementItemNumber;
	private String isAdHoc;

	private String itemIdSupplierPartAuxiliaryId;
	private String itemIdSupplierPartId;
	
	private String unitPriceAlternateAmount;
	private String unitPriceAlternateCurrenty;
	private String unitPriceCurrency;
	private String unitPriceValue;

	private String itemDescriptionValue;
	private String unitOfMeasure;
	private LinkedHashMap<String,String> classificationDomainNameValueDetails = new LinkedHashMap<String, String>();

	private String manufacturerPartId;

	private String manufacturerName;
	private LinkedHashMap<String,String> urlNameValueList= new LinkedHashMap<String, String>();
	private LinkedHashMap<String,String> itemLevelExtrinsicNameValueList = new LinkedHashMap<String, String>();


	/**
	 *
	 */
	public PurchasedOrderItemDetails() {
		
	}


	/**
	 * @param lineNumber
	 * @param quantity
	 * @param requestedDeliveryDate
	 * @param agreementItemNumber
	 * @param isAdHoc
	 * @param itemIdSupplierPartAuxiliaryId
	 * @param itemIdSupplierPartId
	 * @param unitPriceAlternateAmount
	 * @param unitPriceAlternateCurrenty
	 * @param unitPriceCurrency
	 * @param unitPriceValue
	 * @param itemDescriptionValue
	 * @param unitOfMeasure
	 * @param classificationDomainNameValueDetails
	 * @param manufacturerPartId
	 * @param manufacturerName
	 * @param urlNameValueList
	 * @param itemLevelExtrinsicNameValueList
	 */
	public PurchasedOrderItemDetails(int lineNumber, int quantity,
			String requestedDeliveryDate, String agreementItemNumber,
			String isAdHoc, String itemIdSupplierPartAuxiliaryId,
			String itemIdSupplierPartId, 
			String unitPriceAlternateAmount,
			String unitPriceAlternateCurrenty, String unitPriceCurrency,
			String unitPriceValue,
			String itemDescriptionValue,
			String unitOfMeasure,
			LinkedHashMap<String, String> classificationDomainNameValueDetails,
			String manufacturerPartId,
			String manufacturerName,
			LinkedHashMap<String, String> urlNameValueList,
			LinkedHashMap<String, String> itemLevelExtrinsicNameValueList) {
		this.lineNumber = lineNumber;
		this.quantity = quantity;
		this.requestedDeliveryDate = requestedDeliveryDate;
		this.agreementItemNumber = agreementItemNumber;
		this.isAdHoc = isAdHoc;
		this.itemIdSupplierPartAuxiliaryId = itemIdSupplierPartAuxiliaryId;
		this.itemIdSupplierPartId = itemIdSupplierPartId;
		this.unitPriceAlternateAmount = unitPriceAlternateAmount;
		this.unitPriceAlternateCurrenty = unitPriceAlternateCurrenty;
		this.unitPriceCurrency = unitPriceCurrency;
		this.unitPriceValue = unitPriceValue;
		this.itemDescriptionValue = itemDescriptionValue;
		this.unitOfMeasure = unitOfMeasure;
		this.classificationDomainNameValueDetails = classificationDomainNameValueDetails;
		this.manufacturerPartId = manufacturerPartId;
		this.manufacturerName = manufacturerName;
		this.urlNameValueList = urlNameValueList;
		this.itemLevelExtrinsicNameValueList = itemLevelExtrinsicNameValueList;
	}


	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}


	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}


	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}


	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	/**
	 * @return the requestedDeliveryDate
	 */
	public String getRequestedDeliveryDate() {
		return requestedDeliveryDate;
	}


	/**
	 * @param requestedDeliveryDate the requestedDeliveryDate to set
	 */
	public void setRequestedDeliveryDate(String requestedDeliveryDate) {
		this.requestedDeliveryDate = requestedDeliveryDate;
	}


	/**
	 * @return the agreementItemNumber
	 */
	public String getAgreementItemNumber() {
		return agreementItemNumber;
	}


	/**
	 * @param agreementItemNumber the agreementItemNumber to set
	 */
	public void setAgreementItemNumber(String agreementItemNumber) {
		this.agreementItemNumber = agreementItemNumber;
	}


	/**
	 * @return the isAdHoc
	 */
	public String getIsAdHoc() {
		return isAdHoc;
	}


	/**
	 * @param isAdHoc the isAdHoc to set
	 */
	public void setIsAdHoc(String isAdHoc) {
		this.isAdHoc = isAdHoc;
	}


	/**
	 * @return the itemIdSupplierPartAuxiliaryId
	 */
	public String getItemIdSupplierPartAuxiliaryId() {
		return itemIdSupplierPartAuxiliaryId;
	}


	/**
	 * @param itemIdSupplierPartAuxiliaryId the itemIdSupplierPartAuxiliaryId to set
	 */
	public void setItemIdSupplierPartAuxiliaryId(
			String itemIdSupplierPartAuxiliaryId) {
		this.itemIdSupplierPartAuxiliaryId = itemIdSupplierPartAuxiliaryId;
	}


	/**
	 * @return the itemIdSupplierPartId
	 */
	public String getItemIdSupplierPartId() {
		return itemIdSupplierPartId;
	}


	/**
	 * @param itemIdSupplierPartId the itemIdSupplierPartId to set
	 */
	public void setItemIdSupplierPartId(String itemIdSupplierPartId) {
		this.itemIdSupplierPartId = itemIdSupplierPartId;
	}


	/**
	 * @return the unitPriceAlternateAmount
	 */
	public String getUnitPriceAlternateAmount() {
		return unitPriceAlternateAmount;
	}


	/**
	 * @param unitPriceAlternateAmount the unitPriceAlternateAmount to set
	 */
	public void setUnitPriceAlternateAmount(String unitPriceAlternateAmount) {
		this.unitPriceAlternateAmount = unitPriceAlternateAmount;
	}


	/**
	 * @return the unitPriceAlternateCurrenty
	 */
	public String getUnitPriceAlternateCurrenty() {
		return unitPriceAlternateCurrenty;
	}


	/**
	 * @param unitPriceAlternateCurrenty the unitPriceAlternateCurrenty to set
	 */
	public void setUnitPriceAlternateCurrenty(String unitPriceAlternateCurrenty) {
		this.unitPriceAlternateCurrenty = unitPriceAlternateCurrenty;
	}


	/**
	 * @return the unitPriceCurrency
	 */
	public String getUnitPriceCurrency() {
		return unitPriceCurrency;
	}


	/**
	 * @param unitPriceCurrency the unitPriceCurrency to set
	 */
	public void setUnitPriceCurrency(String unitPriceCurrency) {
		this.unitPriceCurrency = unitPriceCurrency;
	}


	/**
	 * @return the unitPriceValue
	 */
	public String getUnitPriceValue() {
		return unitPriceValue;
	}


	/**
	 * @param unitPriceValue the unitPriceValue to set
	 */
	public void setUnitPriceValue(String unitPriceValue) {
		this.unitPriceValue = unitPriceValue;
	}


	/**
	 * @return the itemDescriptionValue
	 */
	public String getItemDescriptionValue() {
		return itemDescriptionValue;
	}


	/**
	 * @param itemDescriptionValueLang the itemDescriptionValueLang to set
	 */
	public void setItemDescriptionValue(String itemDescriptionValue) {
		this.itemDescriptionValue = itemDescriptionValue;
	}


	/**
	 * @return the unitOfMeasure
	 */
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}


	/**
	 * @param unitOfMeasure the unitOfMeasure to set
	 */
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}


	/**
	 * @return the classificationDomainNameValueDetails
	 */
	public LinkedHashMap<String, String> getClassificationDomainNameValueDetails() {
		return classificationDomainNameValueDetails;
	}


	/**
	 * @param classificationDomainNameValueDetails the classificationDomainNameValueDetails to set
	 */
	public void setClassificationDomainNameValueDetails(
			LinkedHashMap<String, String> classificationDomainNameValueDetails) {
		this.classificationDomainNameValueDetails = classificationDomainNameValueDetails;
	}


	/**
	 * @return the manufacturerPartId
	 */
	public String getManufacturerPartId() {
		return manufacturerPartId;
	}


	/**
	 * @param manufacturerPartId the manufacturerPartId to set
	 */
	public void setManufacturerPartId(String manufacturerPartId) {
		this.manufacturerPartId = manufacturerPartId;
	}


	/**
	 * @return the manufacturerName
	 */
	public String getManufacturerName() {
		return manufacturerName;
	}


	/**
	 * @param manufacturerName the manufacturerName to set
	 */
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}


	/**
	 * @return the urlNameValueList
	 */
	public LinkedHashMap<String, String> getUrlNameValueList() {
		return urlNameValueList;
	}


	/**
	 * @param urlNameValueList the urlNameValueList to set
	 */
	public void setUrlNameValueList(LinkedHashMap<String, String> urlNameValueList) {
		this.urlNameValueList = urlNameValueList;
	}


	/**
	 * @return the itemLevelExtrinsicNameValueList
	 */
	public LinkedHashMap<String, String> getItemLevelExtrinsicNameValueList() {
		return itemLevelExtrinsicNameValueList;
	}


	/**
	 * @param itemLevelExtrinsicNameValueList the itemLevelExtrinsicNameValueList to set
	 */
	public void setItemLevelExtrinsicNameValueList(
			LinkedHashMap<String, String> itemLevelExtrinsicNameValueList) {
		this.itemLevelExtrinsicNameValueList = itemLevelExtrinsicNameValueList;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PurchasedOrderItemDetails [lineNumber=");
		builder.append(lineNumber);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", requestedDeliveryDate=");
		builder.append(requestedDeliveryDate);
		builder.append(", agreementItemNumber=");
		builder.append(agreementItemNumber);
		builder.append(", isAdHoc=");
		builder.append(isAdHoc);
		builder.append(", itemIdSupplierPartAuxiliaryId=");
		builder.append(itemIdSupplierPartAuxiliaryId);
		builder.append(", itemIdSupplierPartId=");
		builder.append(itemIdSupplierPartId);
		builder.append(", unitPriceAlternateAmount=");
		builder.append(unitPriceAlternateAmount);
		builder.append(", unitPriceAlternateCurrenty=");
		builder.append(unitPriceAlternateCurrenty);
		builder.append(", unitPriceCurrency=");
		builder.append(unitPriceCurrency);
		builder.append(", unitPriceValue=");
		builder.append(unitPriceValue);
		builder.append(", itemDescriptionValue=");
		builder.append(itemDescriptionValue);
		builder.append(", unitOfMeasure=");
		builder.append(unitOfMeasure);
		builder.append(", classificationDomainNameValueDetails=");
		builder.append(classificationDomainNameValueDetails);
		builder.append(", manufacturerPartId=");
		builder.append(manufacturerPartId);
		builder.append(", manufacturerName=");
		builder.append(manufacturerName);
		builder.append(", urlNameValueList=");
		builder.append(urlNameValueList);
		builder.append(", itemLevelExtrinsicNameValueList=");
		builder.append(itemLevelExtrinsicNameValueList);
		builder.append("]");
		return builder.toString();
	}


	
}




