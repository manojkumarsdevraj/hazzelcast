/**
 *
 */
package com.unilog.cxml.models.reqresp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author satish
 *
 */
public class PurchaseOrderRequestDetails implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 6849315943762732445L;

	private CxmlHeaderReqResp cxmlHeaderReqResp;

	private String errorMessageString;
	private String sucessMessageString;
	private String payloadId;
	private String timeStamp;

	private String orderDate;
	private String orderId;
	private String orderType;
	private String requisitionID;
	private String type;
	private String orderVersion;

	private String shipComplete;

	private String totalAlternateAmount;
	private String totalAlternateCurrenty;
	private String totalCurrency;
	private String totalValue;

	private String shipToAddressId;
	private String shipToIsoCountryCode;
	private String shipToName;
	private String shipToEmail;
	private String shipToPhoneNo;
	private PostalAddressForAll shipToPostalAddress;

	private String billToAddressId;
	private String billToIsoCountryCode;
	private String billToName;
	private String billToEmail;
	private String billToPhoneNo;
	private PostalAddressForAll billToPostalAddress;

	private String shippingAlternateAmount;
	private String shippingAlternateCurrenty;
	private String shippingCurrency;
	private String shippingValue;

	private String taxInfoAlternateAmount;
	private String taxInfoAlternateCurrenty;
	private String taxInfoCurrency;
	private String taxInfoValue;
	private String taxInfoDescription;
	private LinkedHashMap<String,String> taxInfoExtrinsicNameValueList = new LinkedHashMap<String, String>();


	private String pCardExpiration;
	private String pCardHolderName;
	private String pCardNumber;


	private String paymentTermsAlternateAmount;
	private String paymentTermsAlternateCurrenty;
	private String paymentTermsCurrency;
	private String paymentTermsValue;
	private PostalAddressForAll paymentPostalAddress;
	private String comments;

	private LinkedHashMap<String,String> orderLevelExtrinsicNameValueList = new LinkedHashMap<String, String>();

	private ArrayList<PurchasedOrderItemDetails> purchasedOrderItemDetailsList;

	/**
	 * @return the cxmlHeaderReqResp
	 */
	public CxmlHeaderReqResp getCxmlHeaderReqResp() {
		return cxmlHeaderReqResp;
	}


	/**
	 * @param errorMessageString the errorMessageString to set
	 */
	public void setErrorMessageString(String errorMessageString) {
		this.errorMessageString = errorMessageString;
	}

	/**
	 * @return the errorMessageString
	 */
	public String getErrorMessageString() {
		return errorMessageString;
	}


	/**
	 * @param sucessMessageString the sucessMessageString to set
	 */
	public void setSucessMessageString(String sucessMessageString) {
		this.sucessMessageString = sucessMessageString;
	}


	/**
	 * @return the sucessMessageString
	 */
	public String getSucessMessageString() {
		return sucessMessageString;
	}


	/**
	 * @return the payloadId
	 */
	public String getPayloadId() {
		return payloadId;
	}

	/**
	 * @param payloadId the payloadId to set
	 */
	public void setPayloadId(String payloadId) {
		this.payloadId = payloadId;
	}

	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @return the requisitionID
	 */
	public String getRequisitionID() {
		return requisitionID;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the orderVersion
	 */
	public String getOrderVersion() {
		return orderVersion;
	}

	/**
	 * @return the shipComplete
	 */
	public String getShipComplete() {
		return shipComplete;
	}

	/**
	 * @return the totalAlternateAmount
	 */
	public String getTotalAlternateAmount() {
		return totalAlternateAmount;
	}

	/**
	 * @return the totalAlternateCurrenty
	 */
	public String getTotalAlternateCurrenty() {
		return totalAlternateCurrenty;
	}

	/**
	 * @return the totalCurrency
	 */
	public String getTotalCurrency() {
		return totalCurrency;
	}

	/**
	 * @return the totalValue
	 */
	public String getTotalValue() {
		return totalValue;
	}

	/**
	 * @return the shipToAddressId
	 */
	public String getShipToAddressId() {
		return shipToAddressId;
	}

	/**
	 * @return the shipToIsoCountryCode
	 */
	public String getShipToIsoCountryCode() {
		return shipToIsoCountryCode;
	}

	/**
	 * @return the shipToName
	 */
	public String getShipToName() {
		return shipToName;
	}

	/**
	 * @return the shipToEmail
	 */
	public String getShipToEmail() {
		return shipToEmail;
	}

	/**
	 * @return the shipToPhoneNo
	 */
	public String getShipToPhoneNo() {
		return shipToPhoneNo;
	}

	/**
	 * @return the shipToPostalAddress
	 */
	public PostalAddressForAll getShipToPostalAddress() {
		return shipToPostalAddress;
	}

	/**
	 * @return the billToAddressId
	 */
	public String getBillToAddressId() {
		return billToAddressId;
	}

	/**
	 * @return the billToIsoCountryCode
	 */
	public String getBillToIsoCountryCode() {
		return billToIsoCountryCode;
	}

	/**
	 * @return the billToName
	 */
	public String getBillToName() {
		return billToName;
	}

	/**
	 * @return the billToEmail
	 */
	public String getBillToEmail() {
		return billToEmail;
	}

	/**
	 * @return the billToPhoneNo
	 */
	public String getBillToPhoneNo() {
		return billToPhoneNo;
	}

	/**
	 * @return the billToPostalAddress
	 */
	public PostalAddressForAll getBillToPostalAddress() {
		return billToPostalAddress;
	}

	/**
	 * @return the shippingAlternateAmount
	 */
	public String getShippingAlternateAmount() {
		return shippingAlternateAmount;
	}

	/**
	 * @return the shippingAlternateCurrenty
	 */
	public String getShippingAlternateCurrenty() {
		return shippingAlternateCurrenty;
	}

	/**
	 * @return the shippingCurrency
	 */
	public String getShippingCurrency() {
		return shippingCurrency;
	}

	/**
	 * @return the shippingValue
	 */
	public String getShippingValue() {
		return shippingValue;
	}

	/**
	 * @return the taxInfoAlternateAmount
	 */
	public String getTaxInfoAlternateAmount() {
		return taxInfoAlternateAmount;
	}

	/**
	 * @return the taxInfoAlternateCurrenty
	 */
	public String getTaxInfoAlternateCurrenty() {
		return taxInfoAlternateCurrenty;
	}

	/**
	 * @return the taxInfoCurrency
	 */
	public String getTaxInfoCurrency() {
		return taxInfoCurrency;
	}

	/**
	 * @return the taxInfoValue
	 */
	public String getTaxInfoValue() {
		return taxInfoValue;
	}

	/**
	 * @return the taxInfoDescription
	 */
	public String getTaxInfoDescription() {
		return taxInfoDescription;
	}

	/**
	 * @return the taxInfoExtrinsicNameValueList
	 */
	public LinkedHashMap<String, String> getTaxInfoExtrinsicNameValueList() {
		return taxInfoExtrinsicNameValueList;
	}

	/**
	 * @return the pCardExpiration
	 */
	public String getpCardExpiration() {
		return pCardExpiration;
	}

	/**
	 * @return the pCardHolderName
	 */
	public String getpCardHolderName() {
		return pCardHolderName;
	}

	/**
	 * @return the pCardNumber
	 */
	public String getpCardNumber() {
		return pCardNumber;
	}

	/**
	 * @return the paymentTermsAlternateAmount
	 */
	public String getPaymentTermsAlternateAmount() {
		return paymentTermsAlternateAmount;
	}

	/**
	 * @return the paymentTermsAlternateCurrenty
	 */
	public String getPaymentTermsAlternateCurrenty() {
		return paymentTermsAlternateCurrenty;
	}

	/**
	 * @return the paymentTermsCurrency
	 */
	public String getPaymentTermsCurrency() {
		return paymentTermsCurrency;
	}

	/**
	 * @return the paymentTermsValue
	 */
	public String getPaymentTermsValue() {
		return paymentTermsValue;
	}

	/**
	 * @return the paymentPostalAddress
	 */
	public PostalAddressForAll getPaymentPostalAddress() {
		return paymentPostalAddress;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @return the orderLevelExtrinsicNameValueList
	 */
	public LinkedHashMap<String, String> getOrderLevelExtrinsicNameValueList() {
		return orderLevelExtrinsicNameValueList;
	}

	/**
	 * @return the purchasedOrderItemDetailsList
	 */
	public ArrayList<PurchasedOrderItemDetails> getPurchasedOrderItemDetailsList() {
		return purchasedOrderItemDetailsList;
	}

	/**
	 * @param cxmlHeaderReqResp the cxmlHeaderReqResp to set
	 */
	public void setCxmlHeaderReqResp(CxmlHeaderReqResp cxmlHeaderReqResp) {
		this.cxmlHeaderReqResp = cxmlHeaderReqResp;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @param requisitionID the requisitionID to set
	 */
	public void setRequisitionID(String requisitionID) {
		this.requisitionID = requisitionID;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param orderVersion the orderVersion to set
	 */
	public void setOrderVersion(String orderVersion) {
		this.orderVersion = orderVersion;
	}

	/**
	 * @param shipComplete the shipComplete to set
	 */
	public void setShipComplete(String shipComplete) {
		this.shipComplete = shipComplete;
	}

	/**
	 * @param totalAlternateAmount the totalAlternateAmount to set
	 */
	public void setTotalAlternateAmount(String totalAlternateAmount) {
		this.totalAlternateAmount = totalAlternateAmount;
	}

	/**
	 * @param totalAlternateCurrenty the totalAlternateCurrenty to set
	 */
	public void setTotalAlternateCurrenty(String totalAlternateCurrenty) {
		this.totalAlternateCurrenty = totalAlternateCurrenty;
	}

	/**
	 * @param totalCurrency the totalCurrency to set
	 */
	public void setTotalCurrency(String totalCurrency) {
		this.totalCurrency = totalCurrency;
	}

	/**
	 * @param totalValue the totalValue to set
	 */
	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	/**
	 * @param shipToAddressId the shipToAddressId to set
	 */
	public void setShipToAddressId(String shipToAddressId) {
		this.shipToAddressId = shipToAddressId;
	}

	/**
	 * @param shipToIsoCountryCode the shipToIsoCountryCode to set
	 */
	public void setShipToIsoCountryCode(String shipToIsoCountryCode) {
		this.shipToIsoCountryCode = shipToIsoCountryCode;
	}

	/**
	 * @param shipToName the shipToName to set
	 */
	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}

	/**
	 * @param shipToEmail the shipToEmail to set
	 */
	public void setShipToEmail(String shipToEmail) {
		this.shipToEmail = shipToEmail;
	}

	/**
	 * @param shipToPhoneNo the shipToPhoneNo to set
	 */
	public void setShipToPhoneNo(String shipToPhoneNo) {
		this.shipToPhoneNo = shipToPhoneNo;
	}

	/**
	 * @param shipToPostalAddress the shipToPostalAddress to set
	 */
	public void setShipToPostalAddress(PostalAddressForAll shipToPostalAddress) {
		this.shipToPostalAddress = shipToPostalAddress;
	}

	/**
	 * @param billToAddressId the billToAddressId to set
	 */
	public void setBillToAddressId(String billToAddressId) {
		this.billToAddressId = billToAddressId;
	}

	/**
	 * @param billToIsoCountryCode the billToIsoCountryCode to set
	 */
	public void setBillToIsoCountryCode(String billToIsoCountryCode) {
		this.billToIsoCountryCode = billToIsoCountryCode;
	}

	/**
	 * @param billToName the billToName to set
	 */
	public void setBillToName(String billToName) {
		this.billToName = billToName;
	}

	/**
	 * @param billToEmail the billToEmail to set
	 */
	public void setBillToEmail(String billToEmail) {
		this.billToEmail = billToEmail;
	}

	/**
	 * @param billToPhoneNo the billToPhoneNo to set
	 */
	public void setBillToPhoneNo(String billToPhoneNo) {
		this.billToPhoneNo = billToPhoneNo;
	}

	/**
	 * @param billToPostalAddress the billToPostalAddress to set
	 */
	public void setBillToPostalAddress(PostalAddressForAll billToPostalAddress) {
		this.billToPostalAddress = billToPostalAddress;
	}

	/**
	 * @param shippingAlternateAmount the shippingAlternateAmount to set
	 */
	public void setShippingAlternateAmount(String shippingAlternateAmount) {
		this.shippingAlternateAmount = shippingAlternateAmount;
	}

	/**
	 * @param shippingAlternateCurrenty the shippingAlternateCurrenty to set
	 */
	public void setShippingAlternateCurrenty(String shippingAlternateCurrenty) {
		this.shippingAlternateCurrenty = shippingAlternateCurrenty;
	}

	/**
	 * @param shippingCurrency the shippingCurrency to set
	 */
	public void setShippingCurrency(String shippingCurrency) {
		this.shippingCurrency = shippingCurrency;
	}

	/**
	 * @param shippingValue the shippingValue to set
	 */
	public void setShippingValue(String shippingValue) {
		this.shippingValue = shippingValue;
	}

	/**
	 * @param taxInfoAlternateAmount the taxInfoAlternateAmount to set
	 */
	public void setTaxInfoAlternateAmount(String taxInfoAlternateAmount) {
		this.taxInfoAlternateAmount = taxInfoAlternateAmount;
	}

	/**
	 * @param taxInfoAlternateCurrenty the taxInfoAlternateCurrenty to set
	 */
	public void setTaxInfoAlternateCurrenty(String taxInfoAlternateCurrenty) {
		this.taxInfoAlternateCurrenty = taxInfoAlternateCurrenty;
	}

	/**
	 * @param taxInfoCurrency the taxInfoCurrency to set
	 */
	public void setTaxInfoCurrency(String taxInfoCurrency) {
		this.taxInfoCurrency = taxInfoCurrency;
	}

	/**
	 * @param taxInfoValue the taxInfoValue to set
	 */
	public void setTaxInfoValue(String taxInfoValue) {
		this.taxInfoValue = taxInfoValue;
	}

	/**
	 * @param taxInfoDescription the taxInfoDescription to set
	 */
	public void setTaxInfoDescription(String taxInfoDescription) {
		this.taxInfoDescription = taxInfoDescription;
	}

	/**
	 * @param taxInfoExtrinsicNameValueList the taxInfoExtrinsicNameValueList to set
	 */
	public void setTaxInfoExtrinsicNameValueList(
			LinkedHashMap<String, String> taxInfoExtrinsicNameValueList) {
		this.taxInfoExtrinsicNameValueList = taxInfoExtrinsicNameValueList;
	}

	/**
	 * @param pCardExpiration the pCardExpiration to set
	 */
	public void setpCardExpiration(String pCardExpiration) {
		this.pCardExpiration = pCardExpiration;
	}

	/**
	 * @param pCardHolderName the pCardHolderName to set
	 */
	public void setpCardHolderName(String pCardHolderName) {
		this.pCardHolderName = pCardHolderName;
	}

	/**
	 * @param pCardNumber the pCardNumber to set
	 */
	public void setpCardNumber(String pCardNumber) {
		this.pCardNumber = pCardNumber;
	}

	/**
	 * @param paymentTermsAlternateAmount the paymentTermsAlternateAmount to set
	 */
	public void setPaymentTermsAlternateAmount(String paymentTermsAlternateAmount) {
		this.paymentTermsAlternateAmount = paymentTermsAlternateAmount;
	}

	/**
	 * @param paymentTermsAlternateCurrenty the paymentTermsAlternateCurrenty to set
	 */
	public void setPaymentTermsAlternateCurrenty(
			String paymentTermsAlternateCurrenty) {
		this.paymentTermsAlternateCurrenty = paymentTermsAlternateCurrenty;
	}

	/**
	 * @param paymentTermsCurrency the paymentTermsCurrency to set
	 */
	public void setPaymentTermsCurrency(String paymentTermsCurrency) {
		this.paymentTermsCurrency = paymentTermsCurrency;
	}

	/**
	 * @param paymentTermsValue the paymentTermsValue to set
	 */
	public void setPaymentTermsValue(String paymentTermsValue) {
		this.paymentTermsValue = paymentTermsValue;
	}

	/**
	 * @param paymentPostalAddress the paymentPostalAddress to set
	 */
	public void setPaymentPostalAddress(PostalAddressForAll paymentPostalAddress) {
		this.paymentPostalAddress = paymentPostalAddress;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @param orderLevelExtrinsicNameValueList the orderLevelExtrinsicNameValueList to set
	 */
	public void setOrderLevelExtrinsicNameValueList(
			LinkedHashMap<String, String> orderLevelExtrinsicNameValueList) {
		this.orderLevelExtrinsicNameValueList = orderLevelExtrinsicNameValueList;
	}

	/**
	 * @param purchasedOrderItemDetailsList the purchasedOrderItemDetailsList to set
	 */
	public void setPurchasedOrderItemDetailsList(
			ArrayList<PurchasedOrderItemDetails> purchasedOrderItemDetailsList) {
		this.purchasedOrderItemDetailsList = purchasedOrderItemDetailsList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PurchaseOrderRequestDetails [payloadId=");
		builder.append(payloadId);
		builder.append(", timeStamp=");
		builder.append(timeStamp);
		builder.append(", cxmlHeaderReqResp=");
		builder.append(cxmlHeaderReqResp.toString());
		builder.append(", orderDate=");
		builder.append(orderDate);
		builder.append(", orderId=");
		builder.append(orderId);
		builder.append(", orderType=");
		builder.append(orderType);
		builder.append(", requisitionID=");
		builder.append(requisitionID);
		builder.append(", type=");
		builder.append(type);
		builder.append(", orderVersion=");
		builder.append(orderVersion);
		builder.append(", shipComplete=");
		builder.append(shipComplete);
		builder.append(", totalAlternateAmount=");
		builder.append(totalAlternateAmount);
		builder.append(", totalAlternateCurrenty=");
		builder.append(totalAlternateCurrenty);
		builder.append(", totalCurrency=");
		builder.append(totalCurrency);
		builder.append(", totalValue=");
		builder.append(totalValue);
		builder.append(", shipToAddressId=");
		builder.append(shipToAddressId);
		builder.append(", shipToIsoCountryCode=");
		builder.append(shipToIsoCountryCode);
		builder.append(", shipToName=");
		builder.append(shipToName);
		builder.append(", shipToEmail=");
		builder.append(shipToEmail);
		builder.append(", shipToPhoneNo=");
		builder.append(shipToPhoneNo);
		builder.append(", shipToPostalAddress=");
		builder.append(shipToPostalAddress.toString());
		builder.append(", billToAddressId=");
		builder.append(billToAddressId);
		builder.append(", billToIsoCountryCode=");
		builder.append(billToIsoCountryCode);
		builder.append(", billToName=");
		builder.append(billToName);
		builder.append(", billToEmail=");
		builder.append(billToEmail);
		builder.append(", billToPhoneNo=");
		builder.append(billToPhoneNo);
		builder.append(", billToPostalAddress=");
		builder.append(billToPostalAddress.toString());
		builder.append(", shippingAlternateAmount=");
		builder.append(shippingAlternateAmount);
		builder.append(", shippingAlternateCurrenty=");
		builder.append(shippingAlternateCurrenty);
		builder.append(", shippingCurrency=");
		builder.append(shippingCurrency);
		builder.append(", shippingValue=");
		builder.append(shippingValue);
		builder.append(", taxInfoAlternateAmount=");
		builder.append(taxInfoAlternateAmount);
		builder.append(", taxInfoAlternateCurrenty=");
		builder.append(taxInfoAlternateCurrenty);
		builder.append(", taxInfoCurrency=");
		builder.append(taxInfoCurrency);
		builder.append(", taxInfoValue=");
		builder.append(taxInfoValue);
		builder.append(", taxInfoDescription=");
		builder.append(taxInfoDescription);
		builder.append(", taxInfoExtrinsicNameValueList=");
		builder.append(taxInfoExtrinsicNameValueList);
		builder.append(", pCardExpiration=");
		builder.append(pCardExpiration);
		builder.append(", pCardHolderName=");
		builder.append(pCardHolderName);
		builder.append(", pCardNumber=");
		builder.append(pCardNumber);
		builder.append(", paymentTermsAlternateAmount=");
		builder.append(paymentTermsAlternateAmount);
		builder.append(", paymentTermsAlternateCurrenty=");
		builder.append(paymentTermsAlternateCurrenty);
		builder.append(", paymentTermsCurrency=");
		builder.append(paymentTermsCurrency);
		builder.append(", paymentTermsValue=");
		builder.append(paymentTermsValue);
		builder.append(", paymentPostalAddress=");
		builder.append(paymentPostalAddress.toString());
		builder.append(", comments=");
		builder.append(comments);
		builder.append(", orderLevelExtrinsicNameValueList=");
		builder.append(orderLevelExtrinsicNameValueList);
		builder.append(", purchasedOrderItemDetailsList=");
		builder.append(purchasedOrderItemDetailsList);
		builder.append("]");
		return builder.toString();
	}





}
