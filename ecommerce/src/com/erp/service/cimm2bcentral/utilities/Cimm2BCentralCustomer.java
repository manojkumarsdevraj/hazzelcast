package com.erp.service.cimm2bcentral.utilities;

import java.util.ArrayList;
import java.util.List;

public class Cimm2BCentralCustomer extends Cimm2BCentralResponseEntity {
	
	private String customerERPId;
	private String customerId;
	private String customerName;
	private Cimm2BCentralAddress address;
	private String billToCustomerId;
	private String billToCustomerERPId;
	private ArrayList<Cimm2BCentralCustomer> customerLocations;
	private ArrayList<Cimm2BCentralContact> contacts;
	private Cimm2BCentralContact secondaryContact;
	private String termsType;
	private String termsTypeDescription;
	private ArrayList<Cimm2BCentralShipVia> shippingMethods;
	private String homeBranch;
	private String salesPersonCode;
	private Boolean codFlag;
	private Boolean poRequired;
	private Boolean gasPORequired;
	private String defaultShipLocationId;
	private String arCustomerNumber;
	private String customerPoNumber;
	private String defaultShipVia;
	private Boolean fuelSurcharge;
	private double minimumOrderAmount;
	private double prepaidFreightAmount;
	private String priceListPath;
	private String fullPriceListPath;
	private String primarySalesRepId;
	private String defaultBranchId;
	private String salesRepId;
	private Cimm2BCentralCustomerType customerType;
	private ArrayList<Cimm2BCentralGetInvoiceList> cimm2BInvoices;
	private String password;
	private String userERPId;
	private Boolean isSuperuser;
	private String loginId;
	private String paymentTerm;
	private List<String> orderStatus;
	private String department;
	private String classificationId;
	private String elementProcessorId;
	private ArrayList<Cimm2BCentralCreditCardDetails> creditCard;
	private ArrayList<Cimm2BCentralCustomerClassificationItems> classificationItems;
	private Boolean customerTemplateOverride = false;
	private String description;
	private Boolean customerTypeFlag = false;
	private String priceType;
	private String addOnNumber4;
	private String addOnNumber2;
	private String taxable;	
	private ArrayList<Cimm2BCentralContracts> contracts;
	private String subsetName;
	private String userName;
	private String creditDate;
	private String templateCustomerCode;
	private String contactTitle;
	private String webAddress;
	
	public String getTemplateCustomerCode() {
		return templateCustomerCode;
	}
	public void setTemplateCustomerCode(String templateCustomerCode) {
		this.templateCustomerCode = templateCustomerCode;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
	
	public String getPaymentTerm() {
		return paymentTerm;
	}
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}
	
	public String getPriceListPath() {
		return priceListPath;
	}
	public void setPriceListPath(String priceListPath) {
		this.priceListPath = priceListPath;
	}
	public String getFullPriceListPath() {
		return fullPriceListPath;
	}
	public void setFullPriceListPath(String fullPriceListPath) {
		this.fullPriceListPath = fullPriceListPath;
	}
	public double getMinimumOrderAmount() {
		return minimumOrderAmount;
	}
	public void setMinimumOrderAmount(double minimumOrderAmount) {
		this.minimumOrderAmount = minimumOrderAmount;
	}
	public double getPrepaidFreightAmount() {
		return prepaidFreightAmount;
	}
	public void setPrepaidFreightAmount(double prepaidFreightAmount) {
		this.prepaidFreightAmount = prepaidFreightAmount;
	}
	public Boolean getGasPORequired() {
		return gasPORequired;
	}
	public void setGasPORequired(Boolean gasPORequired) {
		this.gasPORequired = gasPORequired;
	}
	public String getDefaultShipLocationId() {
		return defaultShipLocationId;
	}
	public void setDefaultShipLocationId(String defaultShipLocationId) {
		this.defaultShipLocationId = defaultShipLocationId;
	}
	public String getArCustomerNumber() {
		return arCustomerNumber;
	}
	public void setArCustomerNumber(String arCustomerNumber) {
		this.arCustomerNumber = arCustomerNumber;
	}
	public String getCustomerPoNumber() {
		return customerPoNumber;
	}
	public void setCustomerPoNumber(String customerPoNumber) {
		this.customerPoNumber = customerPoNumber;
	}
	public String getDefaultShipVia() {
		return defaultShipVia;
	}
	public void setDefaultShipVia(String defaultShipVia) {
		this.defaultShipVia = defaultShipVia;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName!= null ? customerName.toUpperCase() : customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName.toUpperCase();
	}
	public Cimm2BCentralAddress getAddress() {
		return address;
	}
	public void setAddress(Cimm2BCentralAddress address) {
		this.address = address;
	}
	public String getBillToCustomerId() {
		return billToCustomerId;
	}
	public void setBillToCustomerId(String billToCustomerId) {
		this.billToCustomerId = billToCustomerId;
	}
	public String getBillToCustomerERPId() {
		return billToCustomerERPId;
	}
	public void setBillToCustomerERPId(String billToCustomerERPId) {
		this.billToCustomerERPId = billToCustomerERPId;
	}
	public ArrayList<Cimm2BCentralCustomer> getCustomerLocations() {
		return customerLocations;
	}
	public void setCustomerLocations(
			ArrayList<Cimm2BCentralCustomer> customerLocations) {
		this.customerLocations = customerLocations;
	}
	public Cimm2BCentralContact getSecondaryContact() {
		return secondaryContact;
	}
	public void setSecondaryContact(Cimm2BCentralContact secondaryContact) {
		this.secondaryContact = secondaryContact;
	}
	public String getTermsType() {
		return termsType;
	}
	public void setTermsType(String termsType) {
		this.termsType = termsType;
	}
	public String getTermsTypeDescription() {
		return termsTypeDescription;
	}
	public void setTermsTypeDescription(String termsTypeDescription) {
		this.termsTypeDescription = termsTypeDescription;
	}
	public ArrayList<Cimm2BCentralShipVia> getShippingMethods() {
		return shippingMethods;
	}
	public void setShippingMethods(ArrayList<Cimm2BCentralShipVia> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}
	public String getHomeBranch() {
		return homeBranch;
	}
	public void setHomeBranch(String homeBranch) {
		this.homeBranch = homeBranch;
	}
	public String getSalesPersonCode() {
		return salesPersonCode;
	}
	public void setSalesPersonCode(String salesPersonCode) {
		this.salesPersonCode = salesPersonCode;
	}
	public Boolean getCodFlag() {
		return codFlag;
	}
	public void setCodFlag(Boolean codFlag) {
		this.codFlag = codFlag;
	}
	public Boolean getPoRequired() {
		return poRequired;
	}
	public void setPoRequired(Boolean poRequired) {
		this.poRequired = poRequired;
	}
	public Boolean getGasPoRequired() {
		return gasPORequired;
	}
	public void setGasPoRequired(Boolean gasPoRequired) {
		this.gasPORequired = gasPoRequired;
	}
	public ArrayList<Cimm2BCentralContact> getContacts() {
		return contacts;
	}
	public void setContacts(ArrayList<Cimm2BCentralContact> contacts) {
		this.contacts = contacts;
	}
	/**
	 * @return the fuelSurcharge
	 */
	public Boolean getFuelSurcharge() {
		return fuelSurcharge;
	}
	/**
	 * @param fuelSurcharge the fuelSurcharge to set
	 */
	public void setFuelSurcharge(Boolean fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}
	public String getPrimarySalesRepId() {
		return primarySalesRepId;
	}
	public void setPrimarySalesRepId(String primarySalesRepId) {
		this.primarySalesRepId = primarySalesRepId;
	}
	public String getDefaultBranchId() {
		return defaultBranchId;
	}
	public void setDefaultBranchId(String defaultBranchId) {
		this.defaultBranchId = defaultBranchId;
	}
	public String getSalesRepId() {
		return salesRepId;
	}
	public void setSalesRepId(String salesRepId) {
		this.salesRepId = salesRepId;
	}
	public Cimm2BCentralCustomerType getCustomerType() {
		return customerType;
	}
	public void setCustomerType(Cimm2BCentralCustomerType customerType) {
		this.customerType = customerType;
	}
	public ArrayList<Cimm2BCentralGetInvoiceList> getCimm2BInvoices() {
		return cimm2BInvoices;
	}
	public void setCimm2BInvoices(ArrayList<Cimm2BCentralGetInvoiceList> cimm2bInvoices) {
		cimm2BInvoices = cimm2bInvoices;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserERPId() {
		return userERPId;
	}
	public void setUserERPId(String userERPId) {
		this.userERPId = userERPId;
	}
	public Boolean getIsSuperuser() {
		return isSuperuser;
	}
	public void setIsSuperuser(Boolean isSuperuser) {
		this.isSuperuser = isSuperuser;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public List<String> getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(List<String> orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getClassificationId() {
		return classificationId;
	}
	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}
	public String getElementProcessorId() {
		return elementProcessorId;
	}
	public void setElementProcessorId(String elementProcessorId) {
		this.elementProcessorId = elementProcessorId;
	}
	public ArrayList<Cimm2BCentralCreditCardDetails> getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(ArrayList<Cimm2BCentralCreditCardDetails> creditCard) {
		this.creditCard = creditCard;
	}
	public ArrayList<Cimm2BCentralCustomerClassificationItems> getClassificationItems() {
		return classificationItems;
	}
	public void setClassificationItems(ArrayList<Cimm2BCentralCustomerClassificationItems> classificationItems) {
		this.classificationItems = classificationItems;
	}
	public Boolean getCustomerTemplateOverride() {
		return customerTemplateOverride;
	}
	public void setCustomerTemplateOverride(Boolean customerTemplateOverride) {
		this.customerTemplateOverride = customerTemplateOverride;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getCustomerTypeFlag() {
		return customerTypeFlag;
	}
	public void setCustomerTypeFlag(Boolean customerTypeFlag) {
		this.customerTypeFlag = customerTypeFlag;
	}
	public String getPriceType() {
		return priceType;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	public String getAddOnNumber4() {
		return addOnNumber4;
	}
	public void setAddOnNumber4(String addOnNumber4) {
		this.addOnNumber4 = addOnNumber4;
	}
	public String getAddOnNumber2() {
		return addOnNumber2;
	}
	public void setAddOnNumber2(String addOnNumber2) {
		this.addOnNumber2 = addOnNumber2;
	}
	public String getTaxable() {
		return taxable;
	}
	public void setTaxable(String taxable) {
		this.taxable = taxable;
	}
	public ArrayList<Cimm2BCentralContracts> getContracts() {
		return contracts;
	}
	public void setContracts(ArrayList<Cimm2BCentralContracts> contracts) {
		this.contracts = contracts;
	}
	public String getSubsetName() {
		return subsetName;
	}
	public void setSubsetName(String subsetName) {
		this.subsetName = subsetName;
	}	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreditDate() {
		return creditDate;
	}
	public void setCreditDate(String creditDate) {
		this.creditDate = creditDate;
	}
	public String getContactTitle() {
		return contactTitle;
	}
	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}
	public String getWebAddress() {
		return webAddress;
	}
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	
}
