package com.unilog.users;

import java.util.LinkedHashMap;

public class CreditApplicationModel {
	
	private String businessName;
	private String date;
	private String businessPhoneNumber;
	private String businessFaxNumber;
	private String billingAddress2C;
	private String billingAddressTwo2C;
	private String shippingAddress2C;
	private String shippingAddressTwo2C;
	private String billingCity2C;
	private String shippingCity2C;
	private String billingState2C;
	private String shippingState2C;
	private String billingZipCode2C;
	private String shippingZipCode2C;

	private String legalStructureRadio;
	private String federalIdNumber;
	private String salesTaxStatusRadio;
	private String salesTaxExemptionCertificateFileName;
	private String divisionOf;
	private String subsidiaryOf;
	private String businessTyp;

	private String invoiceRadio;
	private String invoiceByEmailAddress;
	private String invoiceByFaxNumber;
	private String invoiceByEDIContactName;
	private String invoiceByEDIEmailAddress;

	//-- ParamNames
	private String[] principalOfficeName;
	private String[] principalOfficeTitle;
	private String[] principalOfficeAddress;
	private String[] principalOfficeCity;
	private String[] principalOfficeZipCode;
	private String[] principalOfficeHomePhone;
	private String[] principalOfficeSocSecNo;
	private String[] principalOfficeSpouseName;
	

	private String dateBusinessCommenced;
	private int numberOfEmployees;

	private String creditLimitRequest;
	private String apContactPersonEmail;

	private String finacialStatmentAvailableRadio;
	private String finacialStatmentFileName;

	//-- ParamNames
	private int tradreferenceCount;
	private String[] tradeReferenceName;
	private String[] tradeReferencePhoneNumber;
	private String[] tradeReferenceFaxNumber;
	private String[] tradeReferenceEmailAddress;
	private String[] tradeReferenceAddress;
	private String[] tradeReferenceCity;
	private String[] tradeReferenceZipCode;
	private String[] tradeReferenceAccountNumber;
	

	//-- ParamNames
	private String[] bankReferenceName;
	private String[] bankReferencePhoneNumber;
	private String[] bankReferenceFaxNumber;
	private String[] bankReferenceContactName;
	private String[] bankReferenceAccountOrLoanNumber;
	private String[] bankReferenceAddress;
	private String[] bankReferenceCity;
	private String[] bankReferenceZipCode;
	
	
	private String declarationName;
	private String declaratioEmailAddress;
	private String declarationTitle;
	private String declarationDate;
	
	private int creditApplicationId;
	private StringBuilder electronicallySignedCreditApplication;
	private String signature;
	private String amountOfCredit;
	private String phoneNumber;
	private String yearsInBusiness;
	private String[] realEstateName;
	private String[] realEstateAddress;
	private String[] realEstateValue;
	private String[] realEstateTitle;
	private String[] realEstateBalance;
	private String[] realEstateMortgage;
	private String[] realEstateBusiness;
	private String[] realEstateBusinessAddress;
	private String[] realEstateBusinessValue;
	private String[] realEstateBusinessTitle;
	private String[] realEstateBusinessBalance;
	private String[] realEstateBusinessMortgage;
	private String[] otherStates;
	private String[] otherStatesValues;
	private String[] chargeCardName;
	private String[] chargeCardExpDate;
	private String accountNumber;
	private String addtionalInformation;
	private String billShipInstruction;
	private String billingMode;
	private String subsetFlag;
	private LinkedHashMap<String, String> formData = new LinkedHashMap<>();
	
	
	
	
	
	
	public int getTradreferenceCount() {
		return tradreferenceCount;
	}
	public void setTradreferenceCount(int tradreferenceCount) {
		this.tradreferenceCount = tradreferenceCount;
	}
	public StringBuilder getElectronicallySignedCreditApplication() {
		return electronicallySignedCreditApplication;
	}
	public void setElectronicallySignedCreditApplication(
			StringBuilder electronicallySignedCreditApplication) {
		this.electronicallySignedCreditApplication = electronicallySignedCreditApplication;
	}
	public String getFinacialStatmentFileName() {
		return finacialStatmentFileName;
	}
	public void setFinacialStatmentFileName(String finacialStatmentFileName) {
		this.finacialStatmentFileName = finacialStatmentFileName;
	}
	public int getCreditApplicationId() {
		return creditApplicationId;
	}
	public void setCreditApplicationId(int creditApplicationId) {
		this.creditApplicationId = creditApplicationId;
	}
	public String getBillingAddressTwo2C() {
		return billingAddressTwo2C;
	}
	public void setBillingAddressTwo2C(String billingAddressTwo2C) {
		this.billingAddressTwo2C = billingAddressTwo2C;
	}
	public String getShippingAddressTwo2C() {
		return shippingAddressTwo2C;
	}
	public void setShippingAddressTwo2C(String shippingAddressTwo2C) {
		this.shippingAddressTwo2C = shippingAddressTwo2C;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBusinessPhoneNumber() {
		return businessPhoneNumber;
	}
	public void setBusinessPhoneNumber(String businessPhoneNumber) {
		this.businessPhoneNumber = businessPhoneNumber;
	}
	public String getBusinessFaxNumber() {
		return businessFaxNumber;
	}
	public void setBusinessFaxNumber(String businessFaxNumber) {
		this.businessFaxNumber = businessFaxNumber;
	}
	public String getBillingAddress2C() {
		return billingAddress2C;
	}
	public void setBillingAddress2C(String billingAddress2C) {
		this.billingAddress2C = billingAddress2C;
	}
	public String getShippingAddress2C() {
		return shippingAddress2C;
	}
	public void setShippingAddress2C(String shippingAddress2C) {
		this.shippingAddress2C = shippingAddress2C;
	}
	public String getBillingCity2C() {
		return billingCity2C;
	}
	public void setBillingCity2C(String billingCity2C) {
		this.billingCity2C = billingCity2C;
	}
	public String getShippingCity2C() {
		return shippingCity2C;
	}
	public void setShippingCity2C(String shippingCity2C) {
		this.shippingCity2C = shippingCity2C;
	}
	public String getBillingState2C() {
		return billingState2C;
	}
	public void setBillingState2C(String billingState2C) {
		this.billingState2C = billingState2C;
	}
	public String getShippingState2C() {
		return shippingState2C;
	}
	public void setShippingState2C(String shippingState2C) {
		this.shippingState2C = shippingState2C;
	}
	public String getBillingZipCode2C() {
		return billingZipCode2C;
	}
	public void setBillingZipCode2C(String billingZipCode2C) {
		this.billingZipCode2C = billingZipCode2C;
	}
	public String getShippingZipCode2C() {
		return shippingZipCode2C;
	}
	public void setShippingZipCode2C(String shippingZipCode2C) {
		this.shippingZipCode2C = shippingZipCode2C;
	}
	public String getLegalStructureRadio() {
		return legalStructureRadio;
	}
	public void setLegalStructureRadio(String legalStructureRadio) {
		this.legalStructureRadio = legalStructureRadio;
	}
	public String getFederalIdNumber() {
		return federalIdNumber;
	}
	public void setFederalIdNumber(String federalIdNumber) {
		this.federalIdNumber = federalIdNumber;
	}
	public String getSalesTaxStatusRadio() {
		return salesTaxStatusRadio;
	}
	public void setSalesTaxStatusRadio(String salesTaxStatusRadio) {
		this.salesTaxStatusRadio = salesTaxStatusRadio;
	}
	public String getSalesTaxExemptionCertificateFileName() {
		return salesTaxExemptionCertificateFileName;
	}
	public void setSalesTaxExemptionCertificateFileName(
			String salesTaxExemptionCertificateFileName) {
		this.salesTaxExemptionCertificateFileName = salesTaxExemptionCertificateFileName;
	}
	public String getDivisionOf() {
		return divisionOf;
	}
	public void setDivisionOf(String divisionOf) {
		this.divisionOf = divisionOf;
	}
	public String getSubsidiaryOf() {
		return subsidiaryOf;
	}
	public void setSubsidiaryOf(String subsidiaryOf) {
		this.subsidiaryOf = subsidiaryOf;
	}
	public String getBusinessTyp() {
		return businessTyp;
	}
	public void setBusinessTyp(String businessTyp) {
		this.businessTyp = businessTyp;
	}
	public String getInvoiceRadio() {
		return invoiceRadio;
	}
	public void setInvoiceRadio(String invoiceRadio) {
		this.invoiceRadio = invoiceRadio;
	}
	public String getInvoiceByEmailAddress() {
		return invoiceByEmailAddress;
	}
	public void setInvoiceByEmailAddress(String invoiceByEmailAddress) {
		this.invoiceByEmailAddress = invoiceByEmailAddress;
	}
	public String getInvoiceByFaxNumber() {
		return invoiceByFaxNumber;
	}
	public void setInvoiceByFaxNumber(String invoiceByFaxNumber) {
		this.invoiceByFaxNumber = invoiceByFaxNumber;
	}
	public String getInvoiceByEDIContactName() {
		return invoiceByEDIContactName;
	}
	public void setInvoiceByEDIContactName(String invoiceByEDIContactName) {
		this.invoiceByEDIContactName = invoiceByEDIContactName;
	}
	public String getInvoiceByEDIEmailAddress() {
		return invoiceByEDIEmailAddress;
	}
	public void setInvoiceByEDIEmailAddress(String invoiceByEDIEmailAddress) {
		this.invoiceByEDIEmailAddress = invoiceByEDIEmailAddress;
	}
	public String[] getPrincipalOfficeName() {
		return principalOfficeName;
	}
	public void setPrincipalOfficeName(String[] principalOfficeName) {
		this.principalOfficeName = principalOfficeName;
	}
	public String[] getPrincipalOfficeTitle() {
		return principalOfficeTitle;
	}
	public void setPrincipalOfficeTitle(String[] principalOfficeTitle) {
		this.principalOfficeTitle = principalOfficeTitle;
	}
	public String getDateBusinessCommenced() {
		return dateBusinessCommenced;
	}
	public void setDateBusinessCommenced(String dateBusinessCommenced) {
		this.dateBusinessCommenced = dateBusinessCommenced;
	}
	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}
	public void setNumberOfEmployees(int numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}
	
	public String getCreditLimitRequest() {
		return creditLimitRequest;
	}
	public void setCreditLimitRequest(String creditLimitRequest) {
		this.creditLimitRequest = creditLimitRequest;
	}
	public String getApContactPersonEmail() {
		return apContactPersonEmail;
	}
	public void setApContactPersonEmail(String apContactPersonEmail) {
		this.apContactPersonEmail = apContactPersonEmail;
	}
	public String getFinacialStatmentAvailableRadio() {
		return finacialStatmentAvailableRadio;
	}
	public void setFinacialStatmentAvailableRadio(
			String finacialStatmentAvailableRadio) {
		this.finacialStatmentAvailableRadio = finacialStatmentAvailableRadio;
	}
	public String[] getTradeReferenceName() {
		return tradeReferenceName;
	}
	public void setTradeReferenceName(String[] tradeReferenceName) {
		this.tradeReferenceName = tradeReferenceName;
	}
	public String[] getTradeReferencePhoneNumber() {
		return tradeReferencePhoneNumber;
	}
	public void setTradeReferencePhoneNumber(String[] tradeReferencePhoneNumber) {
		this.tradeReferencePhoneNumber = tradeReferencePhoneNumber;
	}
	public String[] getTradeReferenceFaxNumber() {
		return tradeReferenceFaxNumber;
	}
	public void setTradeReferenceFaxNumber(String[] tradeReferenceFaxNumber) {
		this.tradeReferenceFaxNumber = tradeReferenceFaxNumber;
	}
	public String[] getTradeReferenceEmailAddress() {
		return tradeReferenceEmailAddress;
	}
	public void setTradeReferenceEmailAddress(String[] tradeReferenceEmailAddress) {
		this.tradeReferenceEmailAddress = tradeReferenceEmailAddress;
	}
	public String[] getBankReferenceName() {
		return bankReferenceName;
	}
	public void setBankReferenceName(String[] bankReferenceName) {
		this.bankReferenceName = bankReferenceName;
	}
	public String[] getBankReferencePhoneNumber() {
		return bankReferencePhoneNumber;
	}
	public void setBankReferencePhoneNumber(String[] bankReferencePhoneNumber) {
		this.bankReferencePhoneNumber = bankReferencePhoneNumber;
	}
	public String[] getBankReferenceFaxNumber() {
		return bankReferenceFaxNumber;
	}
	public void setBankReferenceFaxNumber(String[] bankReferenceFaxNumber) {
		this.bankReferenceFaxNumber = bankReferenceFaxNumber;
	}
	public String[] getBankReferenceContactName() {
		return bankReferenceContactName;
	}
	public void setBankReferenceContactName(String[] bankReferenceContactName) {
		this.bankReferenceContactName = bankReferenceContactName;
	}
	public String[] getBankReferenceAccountOrLoanNumber() {
		return bankReferenceAccountOrLoanNumber;
	}
	public void setBankReferenceAccountOrLoanNumber(
			String[] bankReferenceAccountOrLoanNumber) {
		this.bankReferenceAccountOrLoanNumber = bankReferenceAccountOrLoanNumber;
	}
	public String getDeclarationName() {
		return declarationName;
	}
	public void setDeclarationName(String declarationName) {
		this.declarationName = declarationName;
	}
	public String getDeclaratioEmailAddress() {
		return declaratioEmailAddress;
	}
	public void setDeclaratioEmailAddress(String declaratioEmailAddress) {
		this.declaratioEmailAddress = declaratioEmailAddress;
	}
	public String getDeclarationTitle() {
		return declarationTitle;
	}
	public void setDeclarationTitle(String declarationTitle) {
		this.declarationTitle = declarationTitle;
	}
	public String getDeclarationDate() {
		return declarationDate;
	}
	public void setDeclarationDate(String declarationDate) {
		this.declarationDate = declarationDate;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getAmountOfCredit() {
		return amountOfCredit;
	}
	public void setAmountOfCredit(String amountOfCredit) {
		this.amountOfCredit = amountOfCredit;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getYearsInBusiness() {
		return yearsInBusiness;
	}
	public void setYearsInBusiness(String yearsInBusiness) {
		this.yearsInBusiness = yearsInBusiness;
	}
	public String[] getBankReferenceAddress() {
		return bankReferenceAddress;
	}
	public void setBankReferenceAddress(String[] bankReferenceAddress) {
		this.bankReferenceAddress = bankReferenceAddress;
	}
	public String[] getBankReferenceCity() {
		return bankReferenceCity;
	}
	public void setBankReferenceCity(String[] bankReferenceCity) {
		this.bankReferenceCity = bankReferenceCity;
	}
	public String[] getBankReferenceZipCode() {
		return bankReferenceZipCode;
	}
	public void setBankReferenceZipCode(String[] bankReferenceZipCode) {
		this.bankReferenceZipCode = bankReferenceZipCode;
	}
	public String[] getPrincipalOfficeAddress() {
		return principalOfficeAddress;
	}
	public void setPrincipalOfficeAddress(String[] principalOfficeAddress) {
		this.principalOfficeAddress = principalOfficeAddress;
	}
	public String[] getPrincipalOfficeCity() {
		return principalOfficeCity;
	}
	public void setPrincipalOfficeCity(String[] principalOfficeCity) {
		this.principalOfficeCity = principalOfficeCity;
	}
	public String[] getPrincipalOfficeZipCode() {
		return principalOfficeZipCode;
	}
	public void setPrincipalOfficeZipCode(String[] principalOfficeZipCode) {
		this.principalOfficeZipCode = principalOfficeZipCode;
	}
	public String[] getPrincipalOfficeHomePhone() {
		return principalOfficeHomePhone;
	}
	public void setPrincipalOfficeHomePhone(String[] principalOfficeHomePhone) {
		this.principalOfficeHomePhone = principalOfficeHomePhone;
	}
	public String[] getPrincipalOfficeSocSecNo() {
		return principalOfficeSocSecNo;
	}
	public void setPrincipalOfficeSocSecNo(String[] principalOfficeSocSecNo) {
		this.principalOfficeSocSecNo = principalOfficeSocSecNo;
	}
	public String[] getPrincipalOfficeSpouseName() {
		return principalOfficeSpouseName;
	}
	public void setPrincipalOfficeSpouseName(String[] principalOfficeSpouseName) {
		this.principalOfficeSpouseName = principalOfficeSpouseName;
	}
	public String[] getRealEstateBusiness() {
		return realEstateBusiness;
	}
	public void setRealEstateBusiness(String[] realEstateBusiness) {
		this.realEstateBusiness = realEstateBusiness;
	}
	public String[] getRealEstateBusinessTitle() {
		return realEstateBusinessTitle;
	}
	public void setRealEstateBusinessTitle(String[] realEstateBusinessTitle) {
		this.realEstateBusinessTitle = realEstateBusinessTitle;
	}
	public String[] getRealEstateBusinessValue() {
		return realEstateBusinessValue;
	}
	public void setRealEstateBusinessValue(String[] realEstateBusinessValue) {
		this.realEstateBusinessValue = realEstateBusinessValue;
	}
	public String[] getRealEstateBusinessBalance() {
		return realEstateBusinessBalance;
	}
	public void setRealEstateBusinessBalance(String[] realEstateBusinessBalance) {
		this.realEstateBusinessBalance = realEstateBusinessBalance;
	}
	public String[] getRealEstateBusinessMortgage() {
		return realEstateBusinessMortgage;
	}
	public void setRealEstateBusinessMortgage(String[] realEstateBusinessMortgage) {
		this.realEstateBusinessMortgage = realEstateBusinessMortgage;
	}
	public String[] getRealEstateBusinessAddress() {
		return realEstateBusinessAddress;
	}
	public void setRealEstateBusinessAddress(String[] realEstateBusinessAddress) {
		this.realEstateBusinessAddress = realEstateBusinessAddress;
	}
	public String[] getOtherStates() {
		return otherStates;
	}
	public void setOtherStates(String[] otherStates) {
		this.otherStates = otherStates;
	}
	public String[] getOtherStatesValues() {
		return otherStatesValues;
	}
	public void setOtherStatesValues(String[] otherStatesValues) {
		this.otherStatesValues = otherStatesValues;
	}
	public String getAddtionalInformation() {
		return addtionalInformation;
	}
	public void setAddtionalInformation(String addtionalInformation) {
		this.addtionalInformation = addtionalInformation;
	}
	public String getBillShipInstruction() {
		return billShipInstruction;
	}
	public void setBillShipInstruction(String billShipInstruction) {
		this.billShipInstruction = billShipInstruction;
	}
	public String getBillingMode() {
		return billingMode;
	}
	public void setBillingMode(String billingMode) {
		this.billingMode = billingMode;
	}
	public String[] getTradeReferenceAddress() {
		return tradeReferenceAddress;
	}
	public void setTradeReferenceAddress(String[] tradeReferenceAddress) {
		this.tradeReferenceAddress = tradeReferenceAddress;
	}
	public String[] getTradeReferenceCity() {
		return tradeReferenceCity;
	}
	public void setTradeReferenceCity(String[] tradeReferenceCity) {
		this.tradeReferenceCity = tradeReferenceCity;
	}
	public String[] getTradeReferenceZipCode() {
		return tradeReferenceZipCode;
	}
	public void setTradeReferenceZipCode(String[] tradeReferenceZipCode) {
		this.tradeReferenceZipCode = tradeReferenceZipCode;
	}
	public String[] getTradeReferenceAccountNumber() {
		return tradeReferenceAccountNumber;
	}
	public void setTradeReferenceAccountNumber(String[] tradeReferenceAccountNumber) {
		this.tradeReferenceAccountNumber = tradeReferenceAccountNumber;
	}
	public String[] getChargeCardName() {
		return chargeCardName;
	}
	public void setChargeCardName(String[] chargeCardName) {
		this.chargeCardName = chargeCardName;
	}
	public String[] getChargeCardExpDate() {
		return chargeCardExpDate;
	}
	public void setChargeCardExpDate(String[] chargeCardExpDate) {
		this.chargeCardExpDate = chargeCardExpDate;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String[] getRealEstateName() {
		return realEstateName;
	}
	public void setRealEstateName(String[] realEstateName) {
		this.realEstateName = realEstateName;
	}
	public String[] getRealEstateAddress() {
		return realEstateAddress;
	}
	public void setRealEstateAddress(String[] realEstateAddress) {
		this.realEstateAddress = realEstateAddress;
	}
	public String[] getRealEstateValue() {
		return realEstateValue;
	}
	public void setRealEstateValue(String[] realEstateValue) {
		this.realEstateValue = realEstateValue;
	}
	public String[] getRealEstateTitle() {
		return realEstateTitle;
	}
	public void setRealEstateTitle(String[] realEstateTitle) {
		this.realEstateTitle = realEstateTitle;
	}
	public String[] getRealEstateBalance() {
		return realEstateBalance;
	}
	public void setRealEstateBalance(String[] realEstateBalance) {
		this.realEstateBalance = realEstateBalance;
	}
	public String[] getRealEstateMortgage() {
		return realEstateMortgage;
	}
	public void setRealEstateMortgage(String[] realEstateMortgage) {
		this.realEstateMortgage = realEstateMortgage;
	}
	public LinkedHashMap<String, String> getFormData() {
		return formData;
	}
	public void setFormData(LinkedHashMap<String, String> formData) {
		this.formData = formData;
	}
	public String getSubsetFlag() {
		return subsetFlag;
	}
	public void setSubsetFlag(String subsetFlag) {
		this.subsetFlag = subsetFlag;
	}
	
}
