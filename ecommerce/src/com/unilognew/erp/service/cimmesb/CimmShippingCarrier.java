package com.unilognew.erp.service.cimmesb;

import java.util.Date;
import java.util.List;

import com.unilog.cimmesb.client.ecomm.request.CimmAddress;
import com.unilog.cimmesb.client.ecomm.request.CimmContact;
import com.unilog.cimmesb.client.ecomm.request.CimmPackage;
import com.unilog.cimmesb.client.ecomm.request.CimmPackageDimension;
import com.unilog.cimmesb.client.ecomm.request.CimmShippingActivity;
import com.unilog.cimmesb.client.ecomm.request.CimmShippingCharges;
import com.unilog.cimmesb.client.response.CimmLineItem;

public class CimmShippingCarrier {

	public static final String SHIPPER_CONTACT_FIELD_NAME = "shipperContact";
	public static final String SHIPPER_ADDRESS_FIELD_NAME = "shipperAddress";
	public static final String SHIP_TO_CONTACT_FIELD_NAME = "shipToContact";
	public static final String SHIP_FROM_CONTACT_FIELD_NAME = "shipFromContact";
	public static final String SHIP_TO_ADDRESS_FIELD_NAME = "shipToAddress";
	public static final String SHIP_FROM_ADDRESS_FIELD_NAME = "shipFromAddress";
	public static final String SERVICE_CODE_FIELD_NAME = "serviceCode";
	public static final String SERVICE_NAME_FIELD_NAME = "serviceName";
	public static final String SHIPPING_WEIGHT_FIELD_NAME = "shippingWeight";
	public static final String PACKAGE_INFO_FIELD_NAME = "packageInfo";
	
	public static final String DOT_FIELD_NAME = ".";
	
	
	private CimmAddress shipperAddress;
	private CimmAddress shipToAddress;
	private CimmAddress shipFromAddress;
	private CimmContact shipperContact;
	private CimmContact shipToContact;
	private CimmContact shipFromContact;
	private CimmPackage packageInfo;
	private String serviceCode;
	private String serviceName;
	private Date shipmentDate;
	private String trackingNumber;

	private CimmPackage billingWeight;
	private CimmPackage shippingWeight;
	private String shipperName;
	private CimmShippingCharges declaredValue;
	private CimmShippingCharges transportationCharges;
	private CimmShippingCharges serviceCharges;
	private CimmShippingCharges totalCharges;
	private CimmShippingCharges negotiatedRateCharges;
	private String shipmentDescription;
	private Date estimatedArrivalDate;
	private String businessDaysInTransit;
	private String dayOfWeek;
	private List<CimmShippingCarrier> serviceSummary;
	private Double quality;
	private String zipCodeLowEnd;
	private String zipCodeHighEnd;
	private List<CimmShippingActivity> cimmShippingActivity;

	private List<CimmLineItem> lineItems;// fedex rate
	private String dropType; // fedex rate
	private String packageType; // fedex rate
	private String paymentTypes;// fedex rate
	private String rateTypes;// fedex rate

	private CimmPackageDimension packageDimension;
	private String siteName;
	private String siteAccountName;
	private String shipmentQuoteId;
	private Boolean guaranteedDelivery;
	private List<CimmPackage> listOfshippingWeight;
	private String classificationCode;
	private String classificationDescription;

	private Double pounds; // USPS
	private Double ounces; // USPS
	private String machinable;
	private Integer rank; // UPS
	private Date pickUpDate; // time in transit API
	private String handlingUnitType;
	
	public CimmAddress getShipperAddress() {
		return shipperAddress;
	}
	public void setShipperAddress(CimmAddress shipperAddress) {
		this.shipperAddress = shipperAddress;
	}
	public CimmAddress getShipToAddress() {
		return shipToAddress;
	}
	public void setShipToAddress(CimmAddress shipToAddress) {
		this.shipToAddress = shipToAddress;
	}
	public CimmAddress getShipFromAddress() {
		return shipFromAddress;
	}
	public void setShipFromAddress(CimmAddress shipFromAddress) {
		this.shipFromAddress = shipFromAddress;
	}
	public CimmContact getShipperContact() {
		return shipperContact;
	}
	public void setShipperContact(CimmContact shipperContact) {
		this.shipperContact = shipperContact;
	}
	public CimmContact getShipToContact() {
		return shipToContact;
	}
	public void setShipToContact(CimmContact shipToContact) {
		this.shipToContact = shipToContact;
	}
	public CimmContact getShipFromContact() {
		return shipFromContact;
	}
	public void setShipFromContact(CimmContact shipFromContact) {
		this.shipFromContact = shipFromContact;
	}
	public CimmPackage getPackageInfo() {
		return packageInfo;
	}
	public void setPackageInfo(CimmPackage packageInfo) {
		this.packageInfo = packageInfo;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Date getShipmentDate() {
		return shipmentDate;
	}
	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public CimmPackage getBillingWeight() {
		return billingWeight;
	}
	public void setBillingWeight(CimmPackage billingWeight) {
		this.billingWeight = billingWeight;
	}
	public CimmPackage getShippingWeight() {
		return shippingWeight;
	}
	public void setShippingWeight(CimmPackage shippingWeight) {
		this.shippingWeight = shippingWeight;
	}
	public String getShipperName() {
		return shipperName;
	}
	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}
	public CimmShippingCharges getDeclaredValue() {
		return declaredValue;
	}
	public void setDeclaredValue(CimmShippingCharges declaredValue) {
		this.declaredValue = declaredValue;
	}
	public CimmShippingCharges getTransportationCharges() {
		return transportationCharges;
	}
	public void setTransportationCharges(CimmShippingCharges transportationCharges) {
		this.transportationCharges = transportationCharges;
	}
	public CimmShippingCharges getServiceCharges() {
		return serviceCharges;
	}
	public void setServiceCharges(CimmShippingCharges serviceCharges) {
		this.serviceCharges = serviceCharges;
	}
	public CimmShippingCharges getTotalCharges() {
		return totalCharges;
	}
	public void setTotalCharges(CimmShippingCharges totalCharges) {
		this.totalCharges = totalCharges;
	}
	public CimmShippingCharges getNegotiatedRateCharges() {
		return negotiatedRateCharges;
	}
	public void setNegotiatedRateCharges(CimmShippingCharges negotiatedRateCharges) {
		this.negotiatedRateCharges = negotiatedRateCharges;
	}
	public String getShipmentDescription() {
		return shipmentDescription;
	}
	public void setShipmentDescription(String shipmentDescription) {
		this.shipmentDescription = shipmentDescription;
	}
	public Date getEstimatedArrivalDate() {
		return estimatedArrivalDate;
	}
	public void setEstimatedArrivalDate(Date estimatedArrivalDate) {
		this.estimatedArrivalDate = estimatedArrivalDate;
	}
	public String getBusinessDaysInTransit() {
		return businessDaysInTransit;
	}
	public void setBusinessDaysInTransit(String businessDaysInTransit) {
		this.businessDaysInTransit = businessDaysInTransit;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public List<CimmShippingCarrier> getServiceSummary() {
		return serviceSummary;
	}
	public void setServiceSummary(List<CimmShippingCarrier> serviceSummary) {
		this.serviceSummary = serviceSummary;
	}
	public Double getQuality() {
		return quality;
	}
	public void setQuality(Double quality) {
		this.quality = quality;
	}
	public String getZipCodeLowEnd() {
		return zipCodeLowEnd;
	}
	public void setZipCodeLowEnd(String zipCodeLowEnd) {
		this.zipCodeLowEnd = zipCodeLowEnd;
	}
	public String getZipCodeHighEnd() {
		return zipCodeHighEnd;
	}
	public void setZipCodeHighEnd(String zipCodeHighEnd) {
		this.zipCodeHighEnd = zipCodeHighEnd;
	}
	public List<CimmShippingActivity> getCimmShippingActivity() {
		return cimmShippingActivity;
	}
	public void setCimmShippingActivity(List<CimmShippingActivity> cimmShippingActivity) {
		this.cimmShippingActivity = cimmShippingActivity;
	}
	public List<CimmLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(List<CimmLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	public String getDropType() {
		return dropType;
	}
	public void setDropType(String dropType) {
		this.dropType = dropType;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public String getPaymentTypes() {
		return paymentTypes;
	}
	public void setPaymentTypes(String paymentTypes) {
		this.paymentTypes = paymentTypes;
	}
	public String getRateTypes() {
		return rateTypes;
	}
	public void setRateTypes(String rateTypes) {
		this.rateTypes = rateTypes;
	}
	public CimmPackageDimension getPackageDimension() {
		return packageDimension;
	}
	public void setPackageDimension(CimmPackageDimension packageDimension) {
		this.packageDimension = packageDimension;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteAccountName() {
		return siteAccountName;
	}
	public void setSiteAccountName(String siteAccountName) {
		this.siteAccountName = siteAccountName;
	}
	public String getShipmentQuoteId() {
		return shipmentQuoteId;
	}
	public void setShipmentQuoteId(String shipmentQuoteId) {
		this.shipmentQuoteId = shipmentQuoteId;
	}
	public Boolean getGuaranteedDelivery() {
		return guaranteedDelivery;
	}
	public void setGuaranteedDelivery(Boolean guaranteedDelivery) {
		this.guaranteedDelivery = guaranteedDelivery;
	}
	public List<CimmPackage> getListOfshippingWeight() {
		return listOfshippingWeight;
	}
	public void setListOfshippingWeight(List<CimmPackage> listOfshippingWeight) {
		this.listOfshippingWeight = listOfshippingWeight;
	}
	public String getClassificationCode() {
		return classificationCode;
	}
	public void setClassificationCode(String classificationCode) {
		this.classificationCode = classificationCode;
	}
	public String getClassificationDescription() {
		return classificationDescription;
	}
	public void setClassificationDescription(String classificationDescription) {
		this.classificationDescription = classificationDescription;
	}
	public Double getPounds() {
		return pounds;
	}
	public void setPounds(Double pounds) {
		this.pounds = pounds;
	}
	public Double getOunces() {
		return ounces;
	}
	public void setOunces(Double ounces) {
		this.ounces = ounces;
	}
	public String getMachinable() {
		return machinable;
	}
	public void setMachinable(String machinable) {
		this.machinable = machinable;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Date getPickUpDate() {
		return pickUpDate;
	}
	public void setPickUpDate(Date pickUpDate) {
		this.pickUpDate = pickUpDate;
	}
	public String getHandlingUnitType() {
		return handlingUnitType;
	}
	public void setHandlingUnitType(String handlingUnitType) {
		this.handlingUnitType = handlingUnitType;
	}

}

