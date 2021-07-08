/**
 * Transaction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class Transaction  implements java.io.Serializable {
    private java.lang.String transactionID;

    private java.lang.String clerkNumber;

    private java.lang.String shiftID;

    private java.lang.String transactionAmount;

    private java.lang.String originalAuthorizedAmount;

    private java.lang.String totalAuthorizedAmount;

    private java.lang.String salesTaxAmount;

    private java.lang.String tipAmount;

    private java.lang.String approvalNumber;

    private java.lang.String referenceNumber;

    private java.lang.String ticketNumber;

    private com.paymentgateway.elementexpress.transaction.ReversalType reversalType;

    private com.paymentgateway.elementexpress.transaction.MarketCode marketCode;

    private java.lang.String acquirerData;

    private java.lang.String cashBackAmount;

    private com.paymentgateway.elementexpress.transaction.BooleanType billPaymentFlag;

    private com.paymentgateway.elementexpress.transaction.BooleanType duplicateCheckDisableFlag;

    private com.paymentgateway.elementexpress.transaction.BooleanType duplicateOverrideFlag;

    private com.paymentgateway.elementexpress.transaction.BooleanType recurringFlag;

    private java.lang.String commercialCardCustomerCode;

    private java.lang.String processorName;

    private java.lang.String transactionStatus;

    private java.lang.String transactionStatusCode;

    private java.lang.String hostTransactionID;

    private java.lang.String transactionSetupID;

    private java.lang.String merchantVerificationValue;

    private com.paymentgateway.elementexpress.transaction.BooleanType partialApprovedFlag;

    private java.lang.String approvedAmount;

    private java.lang.String commercialCardResponseCode;

    private java.lang.String balanceAmount;

    private java.lang.String balanceCurrencyCode;

    private java.lang.String convenienceFeeAmount;

    private java.lang.String giftCardStatusCode;

    private java.lang.String billPayerAccountNumber;

    private java.lang.String giftCardBalanceTransferCode;

    private com.paymentgateway.elementexpress.transaction.EMVEncryptionFormat EMVEncryptionFormat;

    private com.paymentgateway.elementexpress.transaction.ReversalReason reversalReason;

    public Transaction() {
    }

    public Transaction(
           java.lang.String transactionID,
           java.lang.String clerkNumber,
           java.lang.String shiftID,
           java.lang.String transactionAmount,
           java.lang.String originalAuthorizedAmount,
           java.lang.String totalAuthorizedAmount,
           java.lang.String salesTaxAmount,
           java.lang.String tipAmount,
           java.lang.String approvalNumber,
           java.lang.String referenceNumber,
           java.lang.String ticketNumber,
           com.paymentgateway.elementexpress.transaction.ReversalType reversalType,
           com.paymentgateway.elementexpress.transaction.MarketCode marketCode,
           java.lang.String acquirerData,
           java.lang.String cashBackAmount,
           com.paymentgateway.elementexpress.transaction.BooleanType billPaymentFlag,
           com.paymentgateway.elementexpress.transaction.BooleanType duplicateCheckDisableFlag,
           com.paymentgateway.elementexpress.transaction.BooleanType duplicateOverrideFlag,
           com.paymentgateway.elementexpress.transaction.BooleanType recurringFlag,
           java.lang.String commercialCardCustomerCode,
           java.lang.String processorName,
           java.lang.String transactionStatus,
           java.lang.String transactionStatusCode,
           java.lang.String hostTransactionID,
           java.lang.String transactionSetupID,
           java.lang.String merchantVerificationValue,
           com.paymentgateway.elementexpress.transaction.BooleanType partialApprovedFlag,
           java.lang.String approvedAmount,
           java.lang.String commercialCardResponseCode,
           java.lang.String balanceAmount,
           java.lang.String balanceCurrencyCode,
           java.lang.String convenienceFeeAmount,
           java.lang.String giftCardStatusCode,
           java.lang.String billPayerAccountNumber,
           java.lang.String giftCardBalanceTransferCode,
           com.paymentgateway.elementexpress.transaction.EMVEncryptionFormat EMVEncryptionFormat,
           com.paymentgateway.elementexpress.transaction.ReversalReason reversalReason) {
           this.transactionID = transactionID;
           this.clerkNumber = clerkNumber;
           this.shiftID = shiftID;
           this.transactionAmount = transactionAmount;
           this.originalAuthorizedAmount = originalAuthorizedAmount;
           this.totalAuthorizedAmount = totalAuthorizedAmount;
           this.salesTaxAmount = salesTaxAmount;
           this.tipAmount = tipAmount;
           this.approvalNumber = approvalNumber;
           this.referenceNumber = referenceNumber;
           this.ticketNumber = ticketNumber;
           this.reversalType = reversalType;
           this.marketCode = marketCode;
           this.acquirerData = acquirerData;
           this.cashBackAmount = cashBackAmount;
           this.billPaymentFlag = billPaymentFlag;
           this.duplicateCheckDisableFlag = duplicateCheckDisableFlag;
           this.duplicateOverrideFlag = duplicateOverrideFlag;
           this.recurringFlag = recurringFlag;
           this.commercialCardCustomerCode = commercialCardCustomerCode;
           this.processorName = processorName;
           this.transactionStatus = transactionStatus;
           this.transactionStatusCode = transactionStatusCode;
           this.hostTransactionID = hostTransactionID;
           this.transactionSetupID = transactionSetupID;
           this.merchantVerificationValue = merchantVerificationValue;
           this.partialApprovedFlag = partialApprovedFlag;
           this.approvedAmount = approvedAmount;
           this.commercialCardResponseCode = commercialCardResponseCode;
           this.balanceAmount = balanceAmount;
           this.balanceCurrencyCode = balanceCurrencyCode;
           this.convenienceFeeAmount = convenienceFeeAmount;
           this.giftCardStatusCode = giftCardStatusCode;
           this.billPayerAccountNumber = billPayerAccountNumber;
           this.giftCardBalanceTransferCode = giftCardBalanceTransferCode;
           this.EMVEncryptionFormat = EMVEncryptionFormat;
           this.reversalReason = reversalReason;
    }


    /**
     * Gets the transactionID value for this Transaction.
     * 
     * @return transactionID
     */
    public java.lang.String getTransactionID() {
        return transactionID;
    }


    /**
     * Sets the transactionID value for this Transaction.
     * 
     * @param transactionID
     */
    public void setTransactionID(java.lang.String transactionID) {
        this.transactionID = transactionID;
    }


    /**
     * Gets the clerkNumber value for this Transaction.
     * 
     * @return clerkNumber
     */
    public java.lang.String getClerkNumber() {
        return clerkNumber;
    }


    /**
     * Sets the clerkNumber value for this Transaction.
     * 
     * @param clerkNumber
     */
    public void setClerkNumber(java.lang.String clerkNumber) {
        this.clerkNumber = clerkNumber;
    }


    /**
     * Gets the shiftID value for this Transaction.
     * 
     * @return shiftID
     */
    public java.lang.String getShiftID() {
        return shiftID;
    }


    /**
     * Sets the shiftID value for this Transaction.
     * 
     * @param shiftID
     */
    public void setShiftID(java.lang.String shiftID) {
        this.shiftID = shiftID;
    }


    /**
     * Gets the transactionAmount value for this Transaction.
     * 
     * @return transactionAmount
     */
    public java.lang.String getTransactionAmount() {
        return transactionAmount;
    }


    /**
     * Sets the transactionAmount value for this Transaction.
     * 
     * @param transactionAmount
     */
    public void setTransactionAmount(java.lang.String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    /**
     * Gets the originalAuthorizedAmount value for this Transaction.
     * 
     * @return originalAuthorizedAmount
     */
    public java.lang.String getOriginalAuthorizedAmount() {
        return originalAuthorizedAmount;
    }


    /**
     * Sets the originalAuthorizedAmount value for this Transaction.
     * 
     * @param originalAuthorizedAmount
     */
    public void setOriginalAuthorizedAmount(java.lang.String originalAuthorizedAmount) {
        this.originalAuthorizedAmount = originalAuthorizedAmount;
    }


    /**
     * Gets the totalAuthorizedAmount value for this Transaction.
     * 
     * @return totalAuthorizedAmount
     */
    public java.lang.String getTotalAuthorizedAmount() {
        return totalAuthorizedAmount;
    }


    /**
     * Sets the totalAuthorizedAmount value for this Transaction.
     * 
     * @param totalAuthorizedAmount
     */
    public void setTotalAuthorizedAmount(java.lang.String totalAuthorizedAmount) {
        this.totalAuthorizedAmount = totalAuthorizedAmount;
    }


    /**
     * Gets the salesTaxAmount value for this Transaction.
     * 
     * @return salesTaxAmount
     */
    public java.lang.String getSalesTaxAmount() {
        return salesTaxAmount;
    }


    /**
     * Sets the salesTaxAmount value for this Transaction.
     * 
     * @param salesTaxAmount
     */
    public void setSalesTaxAmount(java.lang.String salesTaxAmount) {
        this.salesTaxAmount = salesTaxAmount;
    }


    /**
     * Gets the tipAmount value for this Transaction.
     * 
     * @return tipAmount
     */
    public java.lang.String getTipAmount() {
        return tipAmount;
    }


    /**
     * Sets the tipAmount value for this Transaction.
     * 
     * @param tipAmount
     */
    public void setTipAmount(java.lang.String tipAmount) {
        this.tipAmount = tipAmount;
    }


    /**
     * Gets the approvalNumber value for this Transaction.
     * 
     * @return approvalNumber
     */
    public java.lang.String getApprovalNumber() {
        return approvalNumber;
    }


    /**
     * Sets the approvalNumber value for this Transaction.
     * 
     * @param approvalNumber
     */
    public void setApprovalNumber(java.lang.String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }


    /**
     * Gets the referenceNumber value for this Transaction.
     * 
     * @return referenceNumber
     */
    public java.lang.String getReferenceNumber() {
        return referenceNumber;
    }


    /**
     * Sets the referenceNumber value for this Transaction.
     * 
     * @param referenceNumber
     */
    public void setReferenceNumber(java.lang.String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }


    /**
     * Gets the ticketNumber value for this Transaction.
     * 
     * @return ticketNumber
     */
    public java.lang.String getTicketNumber() {
        return ticketNumber;
    }


    /**
     * Sets the ticketNumber value for this Transaction.
     * 
     * @param ticketNumber
     */
    public void setTicketNumber(java.lang.String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }


    /**
     * Gets the reversalType value for this Transaction.
     * 
     * @return reversalType
     */
    public com.paymentgateway.elementexpress.transaction.ReversalType getReversalType() {
        return reversalType;
    }


    /**
     * Sets the reversalType value for this Transaction.
     * 
     * @param reversalType
     */
    public void setReversalType(com.paymentgateway.elementexpress.transaction.ReversalType reversalType) {
        this.reversalType = reversalType;
    }


    /**
     * Gets the marketCode value for this Transaction.
     * 
     * @return marketCode
     */
    public com.paymentgateway.elementexpress.transaction.MarketCode getMarketCode() {
        return marketCode;
    }


    /**
     * Sets the marketCode value for this Transaction.
     * 
     * @param marketCode
     */
    public void setMarketCode(com.paymentgateway.elementexpress.transaction.MarketCode marketCode) {
        this.marketCode = marketCode;
    }


    /**
     * Gets the acquirerData value for this Transaction.
     * 
     * @return acquirerData
     */
    public java.lang.String getAcquirerData() {
        return acquirerData;
    }


    /**
     * Sets the acquirerData value for this Transaction.
     * 
     * @param acquirerData
     */
    public void setAcquirerData(java.lang.String acquirerData) {
        this.acquirerData = acquirerData;
    }


    /**
     * Gets the cashBackAmount value for this Transaction.
     * 
     * @return cashBackAmount
     */
    public java.lang.String getCashBackAmount() {
        return cashBackAmount;
    }


    /**
     * Sets the cashBackAmount value for this Transaction.
     * 
     * @param cashBackAmount
     */
    public void setCashBackAmount(java.lang.String cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
    }


    /**
     * Gets the billPaymentFlag value for this Transaction.
     * 
     * @return billPaymentFlag
     */
    public com.paymentgateway.elementexpress.transaction.BooleanType getBillPaymentFlag() {
        return billPaymentFlag;
    }


    /**
     * Sets the billPaymentFlag value for this Transaction.
     * 
     * @param billPaymentFlag
     */
    public void setBillPaymentFlag(com.paymentgateway.elementexpress.transaction.BooleanType billPaymentFlag) {
        this.billPaymentFlag = billPaymentFlag;
    }


    /**
     * Gets the duplicateCheckDisableFlag value for this Transaction.
     * 
     * @return duplicateCheckDisableFlag
     */
    public com.paymentgateway.elementexpress.transaction.BooleanType getDuplicateCheckDisableFlag() {
        return duplicateCheckDisableFlag;
    }


    /**
     * Sets the duplicateCheckDisableFlag value for this Transaction.
     * 
     * @param duplicateCheckDisableFlag
     */
    public void setDuplicateCheckDisableFlag(com.paymentgateway.elementexpress.transaction.BooleanType duplicateCheckDisableFlag) {
        this.duplicateCheckDisableFlag = duplicateCheckDisableFlag;
    }


    /**
     * Gets the duplicateOverrideFlag value for this Transaction.
     * 
     * @return duplicateOverrideFlag
     */
    public com.paymentgateway.elementexpress.transaction.BooleanType getDuplicateOverrideFlag() {
        return duplicateOverrideFlag;
    }


    /**
     * Sets the duplicateOverrideFlag value for this Transaction.
     * 
     * @param duplicateOverrideFlag
     */
    public void setDuplicateOverrideFlag(com.paymentgateway.elementexpress.transaction.BooleanType duplicateOverrideFlag) {
        this.duplicateOverrideFlag = duplicateOverrideFlag;
    }


    /**
     * Gets the recurringFlag value for this Transaction.
     * 
     * @return recurringFlag
     */
    public com.paymentgateway.elementexpress.transaction.BooleanType getRecurringFlag() {
        return recurringFlag;
    }


    /**
     * Sets the recurringFlag value for this Transaction.
     * 
     * @param recurringFlag
     */
    public void setRecurringFlag(com.paymentgateway.elementexpress.transaction.BooleanType recurringFlag) {
        this.recurringFlag = recurringFlag;
    }


    /**
     * Gets the commercialCardCustomerCode value for this Transaction.
     * 
     * @return commercialCardCustomerCode
     */
    public java.lang.String getCommercialCardCustomerCode() {
        return commercialCardCustomerCode;
    }


    /**
     * Sets the commercialCardCustomerCode value for this Transaction.
     * 
     * @param commercialCardCustomerCode
     */
    public void setCommercialCardCustomerCode(java.lang.String commercialCardCustomerCode) {
        this.commercialCardCustomerCode = commercialCardCustomerCode;
    }


    /**
     * Gets the processorName value for this Transaction.
     * 
     * @return processorName
     */
    public java.lang.String getProcessorName() {
        return processorName;
    }


    /**
     * Sets the processorName value for this Transaction.
     * 
     * @param processorName
     */
    public void setProcessorName(java.lang.String processorName) {
        this.processorName = processorName;
    }


    /**
     * Gets the transactionStatus value for this Transaction.
     * 
     * @return transactionStatus
     */
    public java.lang.String getTransactionStatus() {
        return transactionStatus;
    }


    /**
     * Sets the transactionStatus value for this Transaction.
     * 
     * @param transactionStatus
     */
    public void setTransactionStatus(java.lang.String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }


    /**
     * Gets the transactionStatusCode value for this Transaction.
     * 
     * @return transactionStatusCode
     */
    public java.lang.String getTransactionStatusCode() {
        return transactionStatusCode;
    }


    /**
     * Sets the transactionStatusCode value for this Transaction.
     * 
     * @param transactionStatusCode
     */
    public void setTransactionStatusCode(java.lang.String transactionStatusCode) {
        this.transactionStatusCode = transactionStatusCode;
    }


    /**
     * Gets the hostTransactionID value for this Transaction.
     * 
     * @return hostTransactionID
     */
    public java.lang.String getHostTransactionID() {
        return hostTransactionID;
    }


    /**
     * Sets the hostTransactionID value for this Transaction.
     * 
     * @param hostTransactionID
     */
    public void setHostTransactionID(java.lang.String hostTransactionID) {
        this.hostTransactionID = hostTransactionID;
    }


    /**
     * Gets the transactionSetupID value for this Transaction.
     * 
     * @return transactionSetupID
     */
    public java.lang.String getTransactionSetupID() {
        return transactionSetupID;
    }


    /**
     * Sets the transactionSetupID value for this Transaction.
     * 
     * @param transactionSetupID
     */
    public void setTransactionSetupID(java.lang.String transactionSetupID) {
        this.transactionSetupID = transactionSetupID;
    }


    /**
     * Gets the merchantVerificationValue value for this Transaction.
     * 
     * @return merchantVerificationValue
     */
    public java.lang.String getMerchantVerificationValue() {
        return merchantVerificationValue;
    }


    /**
     * Sets the merchantVerificationValue value for this Transaction.
     * 
     * @param merchantVerificationValue
     */
    public void setMerchantVerificationValue(java.lang.String merchantVerificationValue) {
        this.merchantVerificationValue = merchantVerificationValue;
    }


    /**
     * Gets the partialApprovedFlag value for this Transaction.
     * 
     * @return partialApprovedFlag
     */
    public com.paymentgateway.elementexpress.transaction.BooleanType getPartialApprovedFlag() {
        return partialApprovedFlag;
    }


    /**
     * Sets the partialApprovedFlag value for this Transaction.
     * 
     * @param partialApprovedFlag
     */
    public void setPartialApprovedFlag(com.paymentgateway.elementexpress.transaction.BooleanType partialApprovedFlag) {
        this.partialApprovedFlag = partialApprovedFlag;
    }


    /**
     * Gets the approvedAmount value for this Transaction.
     * 
     * @return approvedAmount
     */
    public java.lang.String getApprovedAmount() {
        return approvedAmount;
    }


    /**
     * Sets the approvedAmount value for this Transaction.
     * 
     * @param approvedAmount
     */
    public void setApprovedAmount(java.lang.String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }


    /**
     * Gets the commercialCardResponseCode value for this Transaction.
     * 
     * @return commercialCardResponseCode
     */
    public java.lang.String getCommercialCardResponseCode() {
        return commercialCardResponseCode;
    }


    /**
     * Sets the commercialCardResponseCode value for this Transaction.
     * 
     * @param commercialCardResponseCode
     */
    public void setCommercialCardResponseCode(java.lang.String commercialCardResponseCode) {
        this.commercialCardResponseCode = commercialCardResponseCode;
    }


    /**
     * Gets the balanceAmount value for this Transaction.
     * 
     * @return balanceAmount
     */
    public java.lang.String getBalanceAmount() {
        return balanceAmount;
    }


    /**
     * Sets the balanceAmount value for this Transaction.
     * 
     * @param balanceAmount
     */
    public void setBalanceAmount(java.lang.String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }


    /**
     * Gets the balanceCurrencyCode value for this Transaction.
     * 
     * @return balanceCurrencyCode
     */
    public java.lang.String getBalanceCurrencyCode() {
        return balanceCurrencyCode;
    }


    /**
     * Sets the balanceCurrencyCode value for this Transaction.
     * 
     * @param balanceCurrencyCode
     */
    public void setBalanceCurrencyCode(java.lang.String balanceCurrencyCode) {
        this.balanceCurrencyCode = balanceCurrencyCode;
    }


    /**
     * Gets the convenienceFeeAmount value for this Transaction.
     * 
     * @return convenienceFeeAmount
     */
    public java.lang.String getConvenienceFeeAmount() {
        return convenienceFeeAmount;
    }


    /**
     * Sets the convenienceFeeAmount value for this Transaction.
     * 
     * @param convenienceFeeAmount
     */
    public void setConvenienceFeeAmount(java.lang.String convenienceFeeAmount) {
        this.convenienceFeeAmount = convenienceFeeAmount;
    }


    /**
     * Gets the giftCardStatusCode value for this Transaction.
     * 
     * @return giftCardStatusCode
     */
    public java.lang.String getGiftCardStatusCode() {
        return giftCardStatusCode;
    }


    /**
     * Sets the giftCardStatusCode value for this Transaction.
     * 
     * @param giftCardStatusCode
     */
    public void setGiftCardStatusCode(java.lang.String giftCardStatusCode) {
        this.giftCardStatusCode = giftCardStatusCode;
    }


    /**
     * Gets the billPayerAccountNumber value for this Transaction.
     * 
     * @return billPayerAccountNumber
     */
    public java.lang.String getBillPayerAccountNumber() {
        return billPayerAccountNumber;
    }


    /**
     * Sets the billPayerAccountNumber value for this Transaction.
     * 
     * @param billPayerAccountNumber
     */
    public void setBillPayerAccountNumber(java.lang.String billPayerAccountNumber) {
        this.billPayerAccountNumber = billPayerAccountNumber;
    }


    /**
     * Gets the giftCardBalanceTransferCode value for this Transaction.
     * 
     * @return giftCardBalanceTransferCode
     */
    public java.lang.String getGiftCardBalanceTransferCode() {
        return giftCardBalanceTransferCode;
    }


    /**
     * Sets the giftCardBalanceTransferCode value for this Transaction.
     * 
     * @param giftCardBalanceTransferCode
     */
    public void setGiftCardBalanceTransferCode(java.lang.String giftCardBalanceTransferCode) {
        this.giftCardBalanceTransferCode = giftCardBalanceTransferCode;
    }


    /**
     * Gets the EMVEncryptionFormat value for this Transaction.
     * 
     * @return EMVEncryptionFormat
     */
    public com.paymentgateway.elementexpress.transaction.EMVEncryptionFormat getEMVEncryptionFormat() {
        return EMVEncryptionFormat;
    }


    /**
     * Sets the EMVEncryptionFormat value for this Transaction.
     * 
     * @param EMVEncryptionFormat
     */
    public void setEMVEncryptionFormat(com.paymentgateway.elementexpress.transaction.EMVEncryptionFormat EMVEncryptionFormat) {
        this.EMVEncryptionFormat = EMVEncryptionFormat;
    }


    /**
     * Gets the reversalReason value for this Transaction.
     * 
     * @return reversalReason
     */
    public com.paymentgateway.elementexpress.transaction.ReversalReason getReversalReason() {
        return reversalReason;
    }


    /**
     * Sets the reversalReason value for this Transaction.
     * 
     * @param reversalReason
     */
    public void setReversalReason(com.paymentgateway.elementexpress.transaction.ReversalReason reversalReason) {
        this.reversalReason = reversalReason;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Transaction)) return false;
        Transaction other = (Transaction) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.transactionID==null && other.getTransactionID()==null) || 
             (this.transactionID!=null &&
              this.transactionID.equals(other.getTransactionID()))) &&
            ((this.clerkNumber==null && other.getClerkNumber()==null) || 
             (this.clerkNumber!=null &&
              this.clerkNumber.equals(other.getClerkNumber()))) &&
            ((this.shiftID==null && other.getShiftID()==null) || 
             (this.shiftID!=null &&
              this.shiftID.equals(other.getShiftID()))) &&
            ((this.transactionAmount==null && other.getTransactionAmount()==null) || 
             (this.transactionAmount!=null &&
              this.transactionAmount.equals(other.getTransactionAmount()))) &&
            ((this.originalAuthorizedAmount==null && other.getOriginalAuthorizedAmount()==null) || 
             (this.originalAuthorizedAmount!=null &&
              this.originalAuthorizedAmount.equals(other.getOriginalAuthorizedAmount()))) &&
            ((this.totalAuthorizedAmount==null && other.getTotalAuthorizedAmount()==null) || 
             (this.totalAuthorizedAmount!=null &&
              this.totalAuthorizedAmount.equals(other.getTotalAuthorizedAmount()))) &&
            ((this.salesTaxAmount==null && other.getSalesTaxAmount()==null) || 
             (this.salesTaxAmount!=null &&
              this.salesTaxAmount.equals(other.getSalesTaxAmount()))) &&
            ((this.tipAmount==null && other.getTipAmount()==null) || 
             (this.tipAmount!=null &&
              this.tipAmount.equals(other.getTipAmount()))) &&
            ((this.approvalNumber==null && other.getApprovalNumber()==null) || 
             (this.approvalNumber!=null &&
              this.approvalNumber.equals(other.getApprovalNumber()))) &&
            ((this.referenceNumber==null && other.getReferenceNumber()==null) || 
             (this.referenceNumber!=null &&
              this.referenceNumber.equals(other.getReferenceNumber()))) &&
            ((this.ticketNumber==null && other.getTicketNumber()==null) || 
             (this.ticketNumber!=null &&
              this.ticketNumber.equals(other.getTicketNumber()))) &&
            ((this.reversalType==null && other.getReversalType()==null) || 
             (this.reversalType!=null &&
              this.reversalType.equals(other.getReversalType()))) &&
            ((this.marketCode==null && other.getMarketCode()==null) || 
             (this.marketCode!=null &&
              this.marketCode.equals(other.getMarketCode()))) &&
            ((this.acquirerData==null && other.getAcquirerData()==null) || 
             (this.acquirerData!=null &&
              this.acquirerData.equals(other.getAcquirerData()))) &&
            ((this.cashBackAmount==null && other.getCashBackAmount()==null) || 
             (this.cashBackAmount!=null &&
              this.cashBackAmount.equals(other.getCashBackAmount()))) &&
            ((this.billPaymentFlag==null && other.getBillPaymentFlag()==null) || 
             (this.billPaymentFlag!=null &&
              this.billPaymentFlag.equals(other.getBillPaymentFlag()))) &&
            ((this.duplicateCheckDisableFlag==null && other.getDuplicateCheckDisableFlag()==null) || 
             (this.duplicateCheckDisableFlag!=null &&
              this.duplicateCheckDisableFlag.equals(other.getDuplicateCheckDisableFlag()))) &&
            ((this.duplicateOverrideFlag==null && other.getDuplicateOverrideFlag()==null) || 
             (this.duplicateOverrideFlag!=null &&
              this.duplicateOverrideFlag.equals(other.getDuplicateOverrideFlag()))) &&
            ((this.recurringFlag==null && other.getRecurringFlag()==null) || 
             (this.recurringFlag!=null &&
              this.recurringFlag.equals(other.getRecurringFlag()))) &&
            ((this.commercialCardCustomerCode==null && other.getCommercialCardCustomerCode()==null) || 
             (this.commercialCardCustomerCode!=null &&
              this.commercialCardCustomerCode.equals(other.getCommercialCardCustomerCode()))) &&
            ((this.processorName==null && other.getProcessorName()==null) || 
             (this.processorName!=null &&
              this.processorName.equals(other.getProcessorName()))) &&
            ((this.transactionStatus==null && other.getTransactionStatus()==null) || 
             (this.transactionStatus!=null &&
              this.transactionStatus.equals(other.getTransactionStatus()))) &&
            ((this.transactionStatusCode==null && other.getTransactionStatusCode()==null) || 
             (this.transactionStatusCode!=null &&
              this.transactionStatusCode.equals(other.getTransactionStatusCode()))) &&
            ((this.hostTransactionID==null && other.getHostTransactionID()==null) || 
             (this.hostTransactionID!=null &&
              this.hostTransactionID.equals(other.getHostTransactionID()))) &&
            ((this.transactionSetupID==null && other.getTransactionSetupID()==null) || 
             (this.transactionSetupID!=null &&
              this.transactionSetupID.equals(other.getTransactionSetupID()))) &&
            ((this.merchantVerificationValue==null && other.getMerchantVerificationValue()==null) || 
             (this.merchantVerificationValue!=null &&
              this.merchantVerificationValue.equals(other.getMerchantVerificationValue()))) &&
            ((this.partialApprovedFlag==null && other.getPartialApprovedFlag()==null) || 
             (this.partialApprovedFlag!=null &&
              this.partialApprovedFlag.equals(other.getPartialApprovedFlag()))) &&
            ((this.approvedAmount==null && other.getApprovedAmount()==null) || 
             (this.approvedAmount!=null &&
              this.approvedAmount.equals(other.getApprovedAmount()))) &&
            ((this.commercialCardResponseCode==null && other.getCommercialCardResponseCode()==null) || 
             (this.commercialCardResponseCode!=null &&
              this.commercialCardResponseCode.equals(other.getCommercialCardResponseCode()))) &&
            ((this.balanceAmount==null && other.getBalanceAmount()==null) || 
             (this.balanceAmount!=null &&
              this.balanceAmount.equals(other.getBalanceAmount()))) &&
            ((this.balanceCurrencyCode==null && other.getBalanceCurrencyCode()==null) || 
             (this.balanceCurrencyCode!=null &&
              this.balanceCurrencyCode.equals(other.getBalanceCurrencyCode()))) &&
            ((this.convenienceFeeAmount==null && other.getConvenienceFeeAmount()==null) || 
             (this.convenienceFeeAmount!=null &&
              this.convenienceFeeAmount.equals(other.getConvenienceFeeAmount()))) &&
            ((this.giftCardStatusCode==null && other.getGiftCardStatusCode()==null) || 
             (this.giftCardStatusCode!=null &&
              this.giftCardStatusCode.equals(other.getGiftCardStatusCode()))) &&
            ((this.billPayerAccountNumber==null && other.getBillPayerAccountNumber()==null) || 
             (this.billPayerAccountNumber!=null &&
              this.billPayerAccountNumber.equals(other.getBillPayerAccountNumber()))) &&
            ((this.giftCardBalanceTransferCode==null && other.getGiftCardBalanceTransferCode()==null) || 
             (this.giftCardBalanceTransferCode!=null &&
              this.giftCardBalanceTransferCode.equals(other.getGiftCardBalanceTransferCode()))) &&
            ((this.EMVEncryptionFormat==null && other.getEMVEncryptionFormat()==null) || 
             (this.EMVEncryptionFormat!=null &&
              this.EMVEncryptionFormat.equals(other.getEMVEncryptionFormat()))) &&
            ((this.reversalReason==null && other.getReversalReason()==null) || 
             (this.reversalReason!=null &&
              this.reversalReason.equals(other.getReversalReason())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getTransactionID() != null) {
            _hashCode += getTransactionID().hashCode();
        }
        if (getClerkNumber() != null) {
            _hashCode += getClerkNumber().hashCode();
        }
        if (getShiftID() != null) {
            _hashCode += getShiftID().hashCode();
        }
        if (getTransactionAmount() != null) {
            _hashCode += getTransactionAmount().hashCode();
        }
        if (getOriginalAuthorizedAmount() != null) {
            _hashCode += getOriginalAuthorizedAmount().hashCode();
        }
        if (getTotalAuthorizedAmount() != null) {
            _hashCode += getTotalAuthorizedAmount().hashCode();
        }
        if (getSalesTaxAmount() != null) {
            _hashCode += getSalesTaxAmount().hashCode();
        }
        if (getTipAmount() != null) {
            _hashCode += getTipAmount().hashCode();
        }
        if (getApprovalNumber() != null) {
            _hashCode += getApprovalNumber().hashCode();
        }
        if (getReferenceNumber() != null) {
            _hashCode += getReferenceNumber().hashCode();
        }
        if (getTicketNumber() != null) {
            _hashCode += getTicketNumber().hashCode();
        }
        if (getReversalType() != null) {
            _hashCode += getReversalType().hashCode();
        }
        if (getMarketCode() != null) {
            _hashCode += getMarketCode().hashCode();
        }
        if (getAcquirerData() != null) {
            _hashCode += getAcquirerData().hashCode();
        }
        if (getCashBackAmount() != null) {
            _hashCode += getCashBackAmount().hashCode();
        }
        if (getBillPaymentFlag() != null) {
            _hashCode += getBillPaymentFlag().hashCode();
        }
        if (getDuplicateCheckDisableFlag() != null) {
            _hashCode += getDuplicateCheckDisableFlag().hashCode();
        }
        if (getDuplicateOverrideFlag() != null) {
            _hashCode += getDuplicateOverrideFlag().hashCode();
        }
        if (getRecurringFlag() != null) {
            _hashCode += getRecurringFlag().hashCode();
        }
        if (getCommercialCardCustomerCode() != null) {
            _hashCode += getCommercialCardCustomerCode().hashCode();
        }
        if (getProcessorName() != null) {
            _hashCode += getProcessorName().hashCode();
        }
        if (getTransactionStatus() != null) {
            _hashCode += getTransactionStatus().hashCode();
        }
        if (getTransactionStatusCode() != null) {
            _hashCode += getTransactionStatusCode().hashCode();
        }
        if (getHostTransactionID() != null) {
            _hashCode += getHostTransactionID().hashCode();
        }
        if (getTransactionSetupID() != null) {
            _hashCode += getTransactionSetupID().hashCode();
        }
        if (getMerchantVerificationValue() != null) {
            _hashCode += getMerchantVerificationValue().hashCode();
        }
        if (getPartialApprovedFlag() != null) {
            _hashCode += getPartialApprovedFlag().hashCode();
        }
        if (getApprovedAmount() != null) {
            _hashCode += getApprovedAmount().hashCode();
        }
        if (getCommercialCardResponseCode() != null) {
            _hashCode += getCommercialCardResponseCode().hashCode();
        }
        if (getBalanceAmount() != null) {
            _hashCode += getBalanceAmount().hashCode();
        }
        if (getBalanceCurrencyCode() != null) {
            _hashCode += getBalanceCurrencyCode().hashCode();
        }
        if (getConvenienceFeeAmount() != null) {
            _hashCode += getConvenienceFeeAmount().hashCode();
        }
        if (getGiftCardStatusCode() != null) {
            _hashCode += getGiftCardStatusCode().hashCode();
        }
        if (getBillPayerAccountNumber() != null) {
            _hashCode += getBillPayerAccountNumber().hashCode();
        }
        if (getGiftCardBalanceTransferCode() != null) {
            _hashCode += getGiftCardBalanceTransferCode().hashCode();
        }
        if (getEMVEncryptionFormat() != null) {
            _hashCode += getEMVEncryptionFormat().hashCode();
        }
        if (getReversalReason() != null) {
            _hashCode += getReversalReason().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Transaction.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clerkNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ClerkNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shiftID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ShiftID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originalAuthorizedAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "OriginalAuthorizedAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalAuthorizedAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TotalAuthorizedAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("salesTaxAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "SalesTaxAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TipAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("approvalNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ApprovalNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ReferenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ticketNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TicketNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reversalType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ReversalType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ReversalType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("marketCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "MarketCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "MarketCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acquirerData");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AcquirerData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cashBackAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CashBackAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("billPaymentFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BillPaymentFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("duplicateCheckDisableFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DuplicateCheckDisableFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("duplicateOverrideFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DuplicateOverrideFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recurringFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "RecurringFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("commercialCardCustomerCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CommercialCardCustomerCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processorName");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ProcessorName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionStatusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionStatusCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostTransactionID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostTransactionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetupID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("merchantVerificationValue");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "MerchantVerificationValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partialApprovedFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PartialApprovedFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("approvedAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ApprovedAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("commercialCardResponseCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CommercialCardResponseCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balanceAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BalanceAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balanceCurrencyCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BalanceCurrencyCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("convenienceFeeAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ConvenienceFeeAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("giftCardStatusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardStatusCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("billPayerAccountNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BillPayerAccountNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("giftCardBalanceTransferCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardBalanceTransferCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EMVEncryptionFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EMVEncryptionFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EMVEncryptionFormat"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reversalReason");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ReversalReason"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ReversalReason"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
