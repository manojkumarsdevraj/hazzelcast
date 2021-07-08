package com.erp.service.model;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.unilog.users.AddressModel;

public class UserManagementModel {
	
	private String errorMessage;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private String countryCode;
    private String countryDesc;
    private String phoneNo;
    private String faxNo;
    private String statusType;
    private String comment;
    private int sicCd1;
    private int sicCd2;
    private int sicCd3;
    private String address3;
    private boolean customerProductsFlag;
    private String lastAgingDate;
    private String lastSalesDate;
    private String notesIndicator;
    private boolean syncCRMFlag;
    private String lastUpdate;
    private int bankNumber;
    private int classNumber;
    private String currencyType;
    private double statementCustomerNumber;
    private String customerType;
    private String cycleCode;
    private int divisionNumber;
    private boolean dunningFlag;
    private String emailAddress;
    private boolean floorPlanCustomerFlag;
    private int geoCode;
    private String languageCode;
    private String lookupName;
    private String purchasingAgent;
    private String purchasingAgentPhoneNo;
    private boolean serviceChargeFlag;
    private String shipLabel;
    private String statementType;
    private boolean unearnedDiscFlag;
    private String termsType;
    private String termsTypeDescription;
    private int orderNumber;
    private int orderSuffix;
    private String warehouse;
    private double customerNumber;
    private String shipTo;
    private int stageCode;
    private String stageCodeString;
    private String enterDate;
    private double totalInvoiceAmount;
    private String transactionType;
    private String customerPurchaseOrder;
    private String requestedShipDate;
    private String promiseDate;
    private String pickedDate;
    private String shipDate;
    private String invoiceDate;
    private String sortField;
    private String approvalType;
    private String pickedTime;
    private String pickedInitials;
    private String takenBy;
    private double totalLineOrdered;
    private double totalLineAmount;
    private String customerName;
    private String customerShortName;
    private String homeBranch;
    private boolean includeCreditCardOnly;
    private String billToId;
	private String billToAddressLine1;
	private String billToAddressLine2;
	private String billToCity;
	private String billToProvince;
	private String billToPostalCode;
	private String billToCountry;
    private String errorCode;
    private String companyName;
    private String creditCardHandle;
    private String invoiceNumber;
    private String[] CustomerNumbers;
    private boolean customerExist;
    private double creditLimit;
    private double lastPaymentAmount;
    private String lastPaymentDate;
    
    private double period1Balance;
    private String period1Text;
    private double period2Balance;
    private String period2Text;
    private double period3Balance;
    private String period3Text;
    private double period4Balance;
    private String period4Text;
    private double period5Balance;
    private String period5Text;
    
    private double futureBalance;
    private String futureBalanceText;
    
    private double total1Balance;
    private String total1Text;
    private double total2Balance;
    private String total2Text;
    private double total3Balance;
    private String total3Text;
    private double total4Balance;
    private String total4Text;
    private double total5Balance;
    private String total5Text;
    private String returnData;
    private ArrayList<AddressModel> shipToList;
    
    //Invoice List
    private String amount;
    private String amountDue;
    private java.math.BigDecimal discountAmount;
    private String discountDate;
    private Boolean disputed;
     private String invoiceNumberString;
    private Integer invoiceSuffix;
    private Integer journalNumber;
    private Integer period;
    private String reference;
    private Integer sequenceNumber;
    private Integer setNumber;
    private Integer transactionCode;
    private String transactionCodeString;
    private String customerERPId;
	private String documentNumber;
	private String applyToNumber;
	private Integer lineNumber;
	private String branch;
	private Integer documentType;
	private Integer detailedDocumentType;
	private Double balance;
	private Double debit;
	private Double credit;
	private Integer documentAge;
	private Integer pastDue;
	private Integer cashBatch;
	private Integer uniqueId;
	private String dueDate;
	private String documentDate;
    //Invoice List
    
    private JsonArray arSummaryDetails;
    
    
	public String getReturnData() {
		return returnData;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	public String getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	public String getApplyToNumber() {
		return applyToNumber;
	}
	public void setApplyToNumber(String applyToNumber) {
		this.applyToNumber = applyToNumber;
	}
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public Integer getDocumentType() {
		return documentType;
	}
	public void setDocumentType(Integer documentType) {
		this.documentType = documentType;
	}
	public Integer getDetailedDocumentType() {
		return detailedDocumentType;
	}
	public void setDetailedDocumentType(Integer detailedDocumentType) {
		this.detailedDocumentType = detailedDocumentType;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getDebit() {
		return debit;
	}
	public void setDebit(Double debit) {
		this.debit = debit;
	}
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	public Integer getDocumentAge() {
		return documentAge;
	}
	public void setDocumentAge(Integer documentAge) {
		this.documentAge = documentAge;
	}
	public Integer getCashBatch() {
		return cashBatch;
	}
	public void setCashBatch(Integer cashBatch) {
		this.cashBatch = cashBatch;
	}
	public Integer getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(String documentDate) {
		this.documentDate = documentDate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAmountDue() {
		return amountDue;
	}
	public String getBillToId() {
		return billToId;
	}
	public void setBillToId(String billToId) {
		this.billToId = billToId;
	}
	public Integer getPastDue() {
		return pastDue;
	}
	public void setPastDue(Integer pastDue) {
		this.pastDue = pastDue;
	}
	public void setAmountDue(String amountDue) {
		this.amountDue = amountDue;
	}
	public java.math.BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(java.math.BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getDiscountDate() {
		return discountDate;
	}
	public void setDiscountDate(String discountDate) {
		this.discountDate = discountDate;
	}
	public Boolean getDisputed() {
		return disputed;
	}
	public void setDisputed(Boolean disputed) {
		this.disputed = disputed;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getInvoiceNumberString() {
		return invoiceNumberString;
	}
	public void setInvoiceNumberString(String invoiceNumberString) {
		this.invoiceNumberString = invoiceNumberString;
	}
	public Integer getInvoiceSuffix() {
		return invoiceSuffix;
	}
	public void setInvoiceSuffix(Integer invoiceSuffix) {
		this.invoiceSuffix = invoiceSuffix;
	}
	public Integer getJournalNumber() {
		return journalNumber;
	}
	public void setJournalNumber(Integer journalNumber) {
		this.journalNumber = journalNumber;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public Integer getSetNumber() {
		return setNumber;
	}
	public void setSetNumber(Integer setNumber) {
		this.setNumber = setNumber;
	}
	public Integer getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(Integer transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getTransactionCodeString() {
		return transactionCodeString;
	}
	public void setTransactionCodeString(String transactionCodeString) {
		this.transactionCodeString = transactionCodeString;
	}
	public void setReturnData(String returnData) {
		this.returnData = returnData;
	}
	public double getPeriod1Balance() {
		return period1Balance;
	}
	public void setPeriod1Balance(double period1Balance) {
		this.period1Balance = period1Balance;
	}
	public String getPeriod1Text() {
		return period1Text;
	}
	public void setPeriod1Text(String period1Text) {
		this.period1Text = period1Text;
	}
	public double getPeriod2Balance() {
		return period2Balance;
	}
	public void setPeriod2Balance(double period2Balance) {
		this.period2Balance = period2Balance;
	}
	public String getPeriod2Text() {
		return period2Text;
	}
	public void setPeriod2Text(String period2Text) {
		this.period2Text = period2Text;
	}
	public double getPeriod3Balance() {
		return period3Balance;
	}
	public void setPeriod3Balance(double period3Balance) {
		this.period3Balance = period3Balance;
	}
	public String getPeriod3Text() {
		return period3Text;
	}
	public void setPeriod3Text(String period3Text) {
		this.period3Text = period3Text;
	}
	public double getPeriod4Balance() {
		return period4Balance;
	}
	public void setPeriod4Balance(double period4Balance) {
		this.period4Balance = period4Balance;
	}
	public String getPeriod4Text() {
		return period4Text;
	}
	public void setPeriod4Text(String period4Text) {
		this.period4Text = period4Text;
	}
	public double getPeriod5Balance() {
		return period5Balance;
	}
	public void setPeriod5Balance(double period5Balance) {
		this.period5Balance = period5Balance;
	}
	public String getPeriod5Text() {
		return period5Text;
	}
	public void setPeriod5Text(String period5Text) {
		this.period5Text = period5Text;
	}
	public double getFutureBalance() {
		return futureBalance;
	}
	public void setFutureBalance(double futureBalance) {
		this.futureBalance = futureBalance;
	}
	public String getFutureBalanceText() {
		return futureBalanceText;
	}
	public void setFutureBalanceText(String futureBalanceText) {
		this.futureBalanceText = futureBalanceText;
	}
	public double getTotal1Balance() {
		return total1Balance;
	}
	public void setTotal1Balance(double total1Balance) {
		this.total1Balance = total1Balance;
	}
	public String getTotal1Text() {
		return total1Text;
	}
	public void setTotal1Text(String total1Text) {
		this.total1Text = total1Text;
	}
	public double getTotal2Balance() {
		return total2Balance;
	}
	public void setTotal2Balance(double total2Balance) {
		this.total2Balance = total2Balance;
	}
	public String getTotal2Text() {
		return total2Text;
	}
	public void setTotal2Text(String total2Text) {
		this.total2Text = total2Text;
	}
	public double getTotal3Balance() {
		return total3Balance;
	}
	public void setTotal3Balance(double total3Balance) {
		this.total3Balance = total3Balance;
	}
	public String getTotal3Text() {
		return total3Text;
	}
	public void setTotal3Text(String total3Text) {
		this.total3Text = total3Text;
	}
	public double getTotal4Balance() {
		return total4Balance;
	}
	public void setTotal4Balance(double total4Balance) {
		this.total4Balance = total4Balance;
	}
	public String getTotal4Text() {
		return total4Text;
	}
	public void setTotal4Text(String total4Text) {
		this.total4Text = total4Text;
	}
	public double getTotal5Balance() {
		return total5Balance;
	}
	public void setTotal5Balance(double total5Balance) {
		this.total5Balance = total5Balance;
	}
	public String getTotal5Text() {
		return total5Text;
	}
	public void setTotal5Text(String total5Text) {
		this.total5Text = total5Text;
	}
	public double getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}
	public double getLastPaymentAmount() {
		return lastPaymentAmount;
	}
	public void setLastPaymentAmount(double lastPaymentAmount) {
		this.lastPaymentAmount = lastPaymentAmount;
	}
	public String getLastPaymentDate() {
		return lastPaymentDate;
	}
	public void setLastPaymentDate(String lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}
	public boolean isCustomerExist() {
		return customerExist;
	}
	public void setCustomerExist(boolean customerExist) {
		this.customerExist = customerExist;
	}
	public String[] getCustomerNumbers() {
		return CustomerNumbers;
	}
	public void setCustomerNumbers(String[] customerNumbers) {
		CustomerNumbers = customerNumbers;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getCreditCardHandle() {
		return creditCardHandle;
	}
	public void setCreditCardHandle(String creditCardHandle) {
		this.creditCardHandle = creditCardHandle;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerShortName() {
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}
	public String getHomeBranch() {
		return homeBranch;
	}
	public void setHomeBranch(String homeBranch) {
		this.homeBranch = homeBranch;
	}
	
	public boolean isIncludeCreditCardOnly() {
		return includeCreditCardOnly;
	}
	public void setIncludeCreditCardOnly(boolean includeCreditCardOnly) {
		this.includeCreditCardOnly = includeCreditCardOnly;
	}
	public String getBillToAddressLine1() {
		return billToAddressLine1;
	}
	public void setBillToAddressLine1(String billToAddressLine1) {
		this.billToAddressLine1 = billToAddressLine1;
	}
	public String getBillToAddressLine2() {
		return billToAddressLine2;
	}
	public void setBillToAddressLine2(String billToAddressLine2) {
		this.billToAddressLine2 = billToAddressLine2;
	}
	public String getBillToCity() {
		return billToCity;
	}
	public void setBillToCity(String billToCity) {
		this.billToCity = billToCity;
	}
	public String getBillToProvince() {
		return billToProvince;
	}
	public void setBillToProvince(String billToProvince) {
		this.billToProvince = billToProvince;
	}
	public String getBillToPostalCode() {
		return billToPostalCode;
	}
	public void setBillToPostalCode(String billToPostalCode) {
		this.billToPostalCode = billToPostalCode;
	}
	public String getBillToCountry() {
		return billToCountry;
	}
	public void setBillToCountry(String billToCountry) {
		this.billToCountry = billToCountry;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public int getOrderSuffix() {
		return orderSuffix;
	}
	public void setOrderSuffix(int orderSuffix) {
		this.orderSuffix = orderSuffix;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public double getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(double customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getShipTo() {
		return shipTo;
	}
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	public String getStageCodeString() {
		return stageCodeString;
	}
	public void setStageCodeString(String stageCodeString) {
		this.stageCodeString = stageCodeString;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getCustomerPurchaseOrder() {
		return customerPurchaseOrder;
	}
	public void setCustomerPurchaseOrder(String customerPurchaseOrder) {
		this.customerPurchaseOrder = customerPurchaseOrder;
	}
	public String getRequestedShipDate() {
		return requestedShipDate;
	}
	public void setRequestedShipDate(String requestedShipDate) {
		this.requestedShipDate = requestedShipDate;
	}
	public String getPromiseDate() {
		return promiseDate;
	}
	public void setPromiseDate(String promiseDate) {
		this.promiseDate = promiseDate;
	}
	public String getPickedDate() {
		return pickedDate;
	}
	public void setPickedDate(String pickedDate) {
		this.pickedDate = pickedDate;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getApprovalType() {
		return approvalType;
	}
	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}
	public String getPickedTime() {
		return pickedTime;
	}
	public void setPickedTime(String pickedTime) {
		this.pickedTime = pickedTime;
	}
	public String getPickedInitials() {
		return pickedInitials;
	}
	public void setPickedInitials(String pickedInitials) {
		this.pickedInitials = pickedInitials;
	}
	public String getTakenBy() {
		return takenBy;
	}
	public void setTakenBy(String takenBy) {
		this.takenBy = takenBy;
	}
	public double getTotalLineOrdered() {
		return totalLineOrdered;
	}
	public void setTotalLineOrdered(double totalLineOrdered) {
		this.totalLineOrdered = totalLineOrdered;
	}
	public String getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(String enterDate) {
		this.enterDate = enterDate;
	}
	public double getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}
	public void setTotalInvoiceAmount(double totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}
	public int getStageCode() {
		return stageCode;
	}
	public void setStageCode(int stageCode) {
		this.stageCode = stageCode;
	}
	public double getTotalLineAmount() {
		return totalLineAmount;
	}
	public void setTotalLineAmount(double totalLineAmount) {
		this.totalLineAmount = totalLineAmount;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getBankNumber() {
		return bankNumber;
	}
	public void setBankNumber(int bankNumber) {
		this.bankNumber = bankNumber;
	}
	public int getClassNumber() {
		return classNumber;
	}
	public void setClassNumber(int classNumber) {
		this.classNumber = classNumber;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public double getStatementCustomerNumber() {
		return statementCustomerNumber;
	}
	public void setStatementCustomerNumber(double statementCustomerNumber) {
		this.statementCustomerNumber = statementCustomerNumber;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getCycleCode() {
		return cycleCode;
	}
	public void setCycleCode(String cycleCode) {
		this.cycleCode = cycleCode;
	}
	public int getDivisionNumber() {
		return divisionNumber;
	}
	public void setDivisionNumber(int divisionNumber) {
		this.divisionNumber = divisionNumber;
	}
	public boolean isDunningFlag() {
		return dunningFlag;
	}
	public void setDunningFlag(boolean dunningFlag) {
		this.dunningFlag = dunningFlag;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmail(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public boolean isFloorPlanCustomerFlag() {
		return floorPlanCustomerFlag;
	}
	public void setFloorPlanCustomerFlag(boolean floorPlanCustomerFlag) {
		this.floorPlanCustomerFlag = floorPlanCustomerFlag;
	}
	public int getGeoCode() {
		return geoCode;
	}
	public void setGeoCode(int geoCode) {
		this.geoCode = geoCode;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public String getLookupName() {
		return lookupName;
	}
	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}
	public String getPurchasingAgent() {
		return purchasingAgent;
	}
	public void setPurchasingAgent(String purchasingAgent) {
		this.purchasingAgent = purchasingAgent;
	}
	public String getPurchasingAgentPhoneNo() {
		return purchasingAgentPhoneNo;
	}
	public void setPurchasingAgentPhoneNo(String purchasingAgentPhoneNo) {
		this.purchasingAgentPhoneNo = purchasingAgentPhoneNo;
	}
	public boolean isServiceChargeFlag() {
		return serviceChargeFlag;
	}
	public void setServiceChargeFlag(boolean serviceChargeFlag) {
		this.serviceChargeFlag = serviceChargeFlag;
	}
	public String getShipLabel() {
		return shipLabel;
	}
	public void setShipLabel(String shipLabel) {
		this.shipLabel = shipLabel;
	}
	public String getStatementType() {
		return statementType;
	}
	public void setStatementType(String statementType) {
		this.statementType = statementType;
	}
	public boolean isUnearnedDiscFlag() {
		return unearnedDiscFlag;
	}
	public void setUnearnedDiscFlag(boolean unearnedDiscFlag) {
		this.unearnedDiscFlag = unearnedDiscFlag;
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
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryDesc() {
		return countryDesc;
	}
	public void setCountryDesc(String countryDesc) {
		this.countryDesc = countryDesc;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getSicCd1() {
		return sicCd1;
	}
	public void setSicCd1(int sicCd1) {
		this.sicCd1 = sicCd1;
	}
	public int getSicCd2() {
		return sicCd2;
	}
	public void setSicCd2(int sicCd2) {
		this.sicCd2 = sicCd2;
	}
	public int getSicCd3() {
		return sicCd3;
	}
	public void setSicCd3(int sicCd3) {
		this.sicCd3 = sicCd3;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public boolean isCustomerProductsFlag() {
		return customerProductsFlag;
	}
	public void setCustomerProductsFlag(boolean customerProductsFlag) {
		this.customerProductsFlag = customerProductsFlag;
	}
	public String getLastAgingDate() {
		return lastAgingDate;
	}
	public void setLastAgingDate(String lastAgingDate) {
		this.lastAgingDate = lastAgingDate;
	}
	public String getLastSalesDate() {
		return lastSalesDate;
	}
	public void setLastSalesDate(String lastSalesDate) {
		this.lastSalesDate = lastSalesDate;
	}
	public String getNotesIndicator() {
		return notesIndicator;
	}
	public void setNotesIndicator(String notesIndicator) {
		this.notesIndicator = notesIndicator;
	}
	public boolean isSyncCRMFlag() {
		return syncCRMFlag;
	}
	public void setSyncCRMFlag(boolean syncCRMFlag) {
		this.syncCRMFlag = syncCRMFlag;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public ArrayList<AddressModel> getShipToList() {
		return shipToList;
	}
	public void setShipToList(ArrayList<AddressModel> shipToList) {
		this.shipToList = shipToList;
	}
	public JsonArray getArSummaryDetails() {
		return arSummaryDetails;
	}
	public void setArSummaryDetails(JsonArray arSummaryDetails) {
		this.arSummaryDetails = arSummaryDetails;
	}

}
