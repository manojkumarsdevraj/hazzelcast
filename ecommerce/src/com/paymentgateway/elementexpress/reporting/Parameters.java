/**
 * Parameters.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.reporting;

public class Parameters  implements java.io.Serializable {
    private java.lang.String transactionID;

    private java.lang.String terminalID;

    private java.lang.String applicationID;

    private java.lang.String approvalNumber;

    private java.lang.String approvedAmount;

    private java.lang.String expressTransactionDate;

    private java.lang.String expressTransactionTime;

    private java.lang.String hostBatchID;

    private java.lang.String hostItemID;

    private java.lang.String hostReversalQueueID;

    private java.lang.String originalAuthorizedAmount;

    private java.lang.String referenceNumber;

    private java.lang.String shiftID;

    private java.lang.String sourceTransactionID;

    private com.paymentgateway.elementexpress.reporting.TerminalType terminalType;

    private java.lang.String trackingID;

    private java.lang.String transactionAmount;

    private java.lang.String transactionStatus;

    private java.lang.String transactionStatusCode;

    private java.lang.String transactionType;

    private java.lang.String XID;

    private java.lang.String sourceIPAddress;

    private java.lang.String externalInterface;

    private java.lang.String[] logTraceLevel;

    private java.lang.String logTraceLevelName;

    private java.lang.String machineName;

    private java.lang.String sourceObject;

    private java.lang.String processID;

    private java.lang.String threadID;

    private com.paymentgateway.elementexpress.reporting.BooleanType reverseOrder;

    private java.lang.String transactionDateTimeBegin;

    private java.lang.String transactionDateTimeEnd;

    private java.lang.String transactionSetupID;

    public Parameters() {
    }

    public Parameters(
           java.lang.String transactionID,
           java.lang.String terminalID,
           java.lang.String applicationID,
           java.lang.String approvalNumber,
           java.lang.String approvedAmount,
           java.lang.String expressTransactionDate,
           java.lang.String expressTransactionTime,
           java.lang.String hostBatchID,
           java.lang.String hostItemID,
           java.lang.String hostReversalQueueID,
           java.lang.String originalAuthorizedAmount,
           java.lang.String referenceNumber,
           java.lang.String shiftID,
           java.lang.String sourceTransactionID,
           com.paymentgateway.elementexpress.reporting.TerminalType terminalType,
           java.lang.String trackingID,
           java.lang.String transactionAmount,
           java.lang.String transactionStatus,
           java.lang.String transactionStatusCode,
           java.lang.String transactionType,
           java.lang.String XID,
           java.lang.String sourceIPAddress,
           java.lang.String externalInterface,
           java.lang.String[] logTraceLevel,
           java.lang.String logTraceLevelName,
           java.lang.String machineName,
           java.lang.String sourceObject,
           java.lang.String processID,
           java.lang.String threadID,
           com.paymentgateway.elementexpress.reporting.BooleanType reverseOrder,
           java.lang.String transactionDateTimeBegin,
           java.lang.String transactionDateTimeEnd,
           java.lang.String transactionSetupID) {
           this.transactionID = transactionID;
           this.terminalID = terminalID;
           this.applicationID = applicationID;
           this.approvalNumber = approvalNumber;
           this.approvedAmount = approvedAmount;
           this.expressTransactionDate = expressTransactionDate;
           this.expressTransactionTime = expressTransactionTime;
           this.hostBatchID = hostBatchID;
           this.hostItemID = hostItemID;
           this.hostReversalQueueID = hostReversalQueueID;
           this.originalAuthorizedAmount = originalAuthorizedAmount;
           this.referenceNumber = referenceNumber;
           this.shiftID = shiftID;
           this.sourceTransactionID = sourceTransactionID;
           this.terminalType = terminalType;
           this.trackingID = trackingID;
           this.transactionAmount = transactionAmount;
           this.transactionStatus = transactionStatus;
           this.transactionStatusCode = transactionStatusCode;
           this.transactionType = transactionType;
           this.XID = XID;
           this.sourceIPAddress = sourceIPAddress;
           this.externalInterface = externalInterface;
           this.logTraceLevel = logTraceLevel;
           this.logTraceLevelName = logTraceLevelName;
           this.machineName = machineName;
           this.sourceObject = sourceObject;
           this.processID = processID;
           this.threadID = threadID;
           this.reverseOrder = reverseOrder;
           this.transactionDateTimeBegin = transactionDateTimeBegin;
           this.transactionDateTimeEnd = transactionDateTimeEnd;
           this.transactionSetupID = transactionSetupID;
    }


    /**
     * Gets the transactionID value for this Parameters.
     * 
     * @return transactionID
     */
    public java.lang.String getTransactionID() {
        return transactionID;
    }


    /**
     * Sets the transactionID value for this Parameters.
     * 
     * @param transactionID
     */
    public void setTransactionID(java.lang.String transactionID) {
        this.transactionID = transactionID;
    }


    /**
     * Gets the terminalID value for this Parameters.
     * 
     * @return terminalID
     */
    public java.lang.String getTerminalID() {
        return terminalID;
    }


    /**
     * Sets the terminalID value for this Parameters.
     * 
     * @param terminalID
     */
    public void setTerminalID(java.lang.String terminalID) {
        this.terminalID = terminalID;
    }


    /**
     * Gets the applicationID value for this Parameters.
     * 
     * @return applicationID
     */
    public java.lang.String getApplicationID() {
        return applicationID;
    }


    /**
     * Sets the applicationID value for this Parameters.
     * 
     * @param applicationID
     */
    public void setApplicationID(java.lang.String applicationID) {
        this.applicationID = applicationID;
    }


    /**
     * Gets the approvalNumber value for this Parameters.
     * 
     * @return approvalNumber
     */
    public java.lang.String getApprovalNumber() {
        return approvalNumber;
    }


    /**
     * Sets the approvalNumber value for this Parameters.
     * 
     * @param approvalNumber
     */
    public void setApprovalNumber(java.lang.String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }


    /**
     * Gets the approvedAmount value for this Parameters.
     * 
     * @return approvedAmount
     */
    public java.lang.String getApprovedAmount() {
        return approvedAmount;
    }


    /**
     * Sets the approvedAmount value for this Parameters.
     * 
     * @param approvedAmount
     */
    public void setApprovedAmount(java.lang.String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }


    /**
     * Gets the expressTransactionDate value for this Parameters.
     * 
     * @return expressTransactionDate
     */
    public java.lang.String getExpressTransactionDate() {
        return expressTransactionDate;
    }


    /**
     * Sets the expressTransactionDate value for this Parameters.
     * 
     * @param expressTransactionDate
     */
    public void setExpressTransactionDate(java.lang.String expressTransactionDate) {
        this.expressTransactionDate = expressTransactionDate;
    }


    /**
     * Gets the expressTransactionTime value for this Parameters.
     * 
     * @return expressTransactionTime
     */
    public java.lang.String getExpressTransactionTime() {
        return expressTransactionTime;
    }


    /**
     * Sets the expressTransactionTime value for this Parameters.
     * 
     * @param expressTransactionTime
     */
    public void setExpressTransactionTime(java.lang.String expressTransactionTime) {
        this.expressTransactionTime = expressTransactionTime;
    }


    /**
     * Gets the hostBatchID value for this Parameters.
     * 
     * @return hostBatchID
     */
    public java.lang.String getHostBatchID() {
        return hostBatchID;
    }


    /**
     * Sets the hostBatchID value for this Parameters.
     * 
     * @param hostBatchID
     */
    public void setHostBatchID(java.lang.String hostBatchID) {
        this.hostBatchID = hostBatchID;
    }


    /**
     * Gets the hostItemID value for this Parameters.
     * 
     * @return hostItemID
     */
    public java.lang.String getHostItemID() {
        return hostItemID;
    }


    /**
     * Sets the hostItemID value for this Parameters.
     * 
     * @param hostItemID
     */
    public void setHostItemID(java.lang.String hostItemID) {
        this.hostItemID = hostItemID;
    }


    /**
     * Gets the hostReversalQueueID value for this Parameters.
     * 
     * @return hostReversalQueueID
     */
    public java.lang.String getHostReversalQueueID() {
        return hostReversalQueueID;
    }


    /**
     * Sets the hostReversalQueueID value for this Parameters.
     * 
     * @param hostReversalQueueID
     */
    public void setHostReversalQueueID(java.lang.String hostReversalQueueID) {
        this.hostReversalQueueID = hostReversalQueueID;
    }


    /**
     * Gets the originalAuthorizedAmount value for this Parameters.
     * 
     * @return originalAuthorizedAmount
     */
    public java.lang.String getOriginalAuthorizedAmount() {
        return originalAuthorizedAmount;
    }


    /**
     * Sets the originalAuthorizedAmount value for this Parameters.
     * 
     * @param originalAuthorizedAmount
     */
    public void setOriginalAuthorizedAmount(java.lang.String originalAuthorizedAmount) {
        this.originalAuthorizedAmount = originalAuthorizedAmount;
    }


    /**
     * Gets the referenceNumber value for this Parameters.
     * 
     * @return referenceNumber
     */
    public java.lang.String getReferenceNumber() {
        return referenceNumber;
    }


    /**
     * Sets the referenceNumber value for this Parameters.
     * 
     * @param referenceNumber
     */
    public void setReferenceNumber(java.lang.String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }


    /**
     * Gets the shiftID value for this Parameters.
     * 
     * @return shiftID
     */
    public java.lang.String getShiftID() {
        return shiftID;
    }


    /**
     * Sets the shiftID value for this Parameters.
     * 
     * @param shiftID
     */
    public void setShiftID(java.lang.String shiftID) {
        this.shiftID = shiftID;
    }


    /**
     * Gets the sourceTransactionID value for this Parameters.
     * 
     * @return sourceTransactionID
     */
    public java.lang.String getSourceTransactionID() {
        return sourceTransactionID;
    }


    /**
     * Sets the sourceTransactionID value for this Parameters.
     * 
     * @param sourceTransactionID
     */
    public void setSourceTransactionID(java.lang.String sourceTransactionID) {
        this.sourceTransactionID = sourceTransactionID;
    }


    /**
     * Gets the terminalType value for this Parameters.
     * 
     * @return terminalType
     */
    public com.paymentgateway.elementexpress.reporting.TerminalType getTerminalType() {
        return terminalType;
    }


    /**
     * Sets the terminalType value for this Parameters.
     * 
     * @param terminalType
     */
    public void setTerminalType(com.paymentgateway.elementexpress.reporting.TerminalType terminalType) {
        this.terminalType = terminalType;
    }


    /**
     * Gets the trackingID value for this Parameters.
     * 
     * @return trackingID
     */
    public java.lang.String getTrackingID() {
        return trackingID;
    }


    /**
     * Sets the trackingID value for this Parameters.
     * 
     * @param trackingID
     */
    public void setTrackingID(java.lang.String trackingID) {
        this.trackingID = trackingID;
    }


    /**
     * Gets the transactionAmount value for this Parameters.
     * 
     * @return transactionAmount
     */
    public java.lang.String getTransactionAmount() {
        return transactionAmount;
    }


    /**
     * Sets the transactionAmount value for this Parameters.
     * 
     * @param transactionAmount
     */
    public void setTransactionAmount(java.lang.String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    /**
     * Gets the transactionStatus value for this Parameters.
     * 
     * @return transactionStatus
     */
    public java.lang.String getTransactionStatus() {
        return transactionStatus;
    }


    /**
     * Sets the transactionStatus value for this Parameters.
     * 
     * @param transactionStatus
     */
    public void setTransactionStatus(java.lang.String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }


    /**
     * Gets the transactionStatusCode value for this Parameters.
     * 
     * @return transactionStatusCode
     */
    public java.lang.String getTransactionStatusCode() {
        return transactionStatusCode;
    }


    /**
     * Sets the transactionStatusCode value for this Parameters.
     * 
     * @param transactionStatusCode
     */
    public void setTransactionStatusCode(java.lang.String transactionStatusCode) {
        this.transactionStatusCode = transactionStatusCode;
    }


    /**
     * Gets the transactionType value for this Parameters.
     * 
     * @return transactionType
     */
    public java.lang.String getTransactionType() {
        return transactionType;
    }


    /**
     * Sets the transactionType value for this Parameters.
     * 
     * @param transactionType
     */
    public void setTransactionType(java.lang.String transactionType) {
        this.transactionType = transactionType;
    }


    /**
     * Gets the XID value for this Parameters.
     * 
     * @return XID
     */
    public java.lang.String getXID() {
        return XID;
    }


    /**
     * Sets the XID value for this Parameters.
     * 
     * @param XID
     */
    public void setXID(java.lang.String XID) {
        this.XID = XID;
    }


    /**
     * Gets the sourceIPAddress value for this Parameters.
     * 
     * @return sourceIPAddress
     */
    public java.lang.String getSourceIPAddress() {
        return sourceIPAddress;
    }


    /**
     * Sets the sourceIPAddress value for this Parameters.
     * 
     * @param sourceIPAddress
     */
    public void setSourceIPAddress(java.lang.String sourceIPAddress) {
        this.sourceIPAddress = sourceIPAddress;
    }


    /**
     * Gets the externalInterface value for this Parameters.
     * 
     * @return externalInterface
     */
    public java.lang.String getExternalInterface() {
        return externalInterface;
    }


    /**
     * Sets the externalInterface value for this Parameters.
     * 
     * @param externalInterface
     */
    public void setExternalInterface(java.lang.String externalInterface) {
        this.externalInterface = externalInterface;
    }


    /**
     * Gets the logTraceLevel value for this Parameters.
     * 
     * @return logTraceLevel
     */
    public java.lang.String[] getLogTraceLevel() {
        return logTraceLevel;
    }


    /**
     * Sets the logTraceLevel value for this Parameters.
     * 
     * @param logTraceLevel
     */
    public void setLogTraceLevel(java.lang.String[] logTraceLevel) {
        this.logTraceLevel = logTraceLevel;
    }


    /**
     * Gets the logTraceLevelName value for this Parameters.
     * 
     * @return logTraceLevelName
     */
    public java.lang.String getLogTraceLevelName() {
        return logTraceLevelName;
    }


    /**
     * Sets the logTraceLevelName value for this Parameters.
     * 
     * @param logTraceLevelName
     */
    public void setLogTraceLevelName(java.lang.String logTraceLevelName) {
        this.logTraceLevelName = logTraceLevelName;
    }


    /**
     * Gets the machineName value for this Parameters.
     * 
     * @return machineName
     */
    public java.lang.String getMachineName() {
        return machineName;
    }


    /**
     * Sets the machineName value for this Parameters.
     * 
     * @param machineName
     */
    public void setMachineName(java.lang.String machineName) {
        this.machineName = machineName;
    }


    /**
     * Gets the sourceObject value for this Parameters.
     * 
     * @return sourceObject
     */
    public java.lang.String getSourceObject() {
        return sourceObject;
    }


    /**
     * Sets the sourceObject value for this Parameters.
     * 
     * @param sourceObject
     */
    public void setSourceObject(java.lang.String sourceObject) {
        this.sourceObject = sourceObject;
    }


    /**
     * Gets the processID value for this Parameters.
     * 
     * @return processID
     */
    public java.lang.String getProcessID() {
        return processID;
    }


    /**
     * Sets the processID value for this Parameters.
     * 
     * @param processID
     */
    public void setProcessID(java.lang.String processID) {
        this.processID = processID;
    }


    /**
     * Gets the threadID value for this Parameters.
     * 
     * @return threadID
     */
    public java.lang.String getThreadID() {
        return threadID;
    }


    /**
     * Sets the threadID value for this Parameters.
     * 
     * @param threadID
     */
    public void setThreadID(java.lang.String threadID) {
        this.threadID = threadID;
    }


    /**
     * Gets the reverseOrder value for this Parameters.
     * 
     * @return reverseOrder
     */
    public com.paymentgateway.elementexpress.reporting.BooleanType getReverseOrder() {
        return reverseOrder;
    }


    /**
     * Sets the reverseOrder value for this Parameters.
     * 
     * @param reverseOrder
     */
    public void setReverseOrder(com.paymentgateway.elementexpress.reporting.BooleanType reverseOrder) {
        this.reverseOrder = reverseOrder;
    }


    /**
     * Gets the transactionDateTimeBegin value for this Parameters.
     * 
     * @return transactionDateTimeBegin
     */
    public java.lang.String getTransactionDateTimeBegin() {
        return transactionDateTimeBegin;
    }


    /**
     * Sets the transactionDateTimeBegin value for this Parameters.
     * 
     * @param transactionDateTimeBegin
     */
    public void setTransactionDateTimeBegin(java.lang.String transactionDateTimeBegin) {
        this.transactionDateTimeBegin = transactionDateTimeBegin;
    }


    /**
     * Gets the transactionDateTimeEnd value for this Parameters.
     * 
     * @return transactionDateTimeEnd
     */
    public java.lang.String getTransactionDateTimeEnd() {
        return transactionDateTimeEnd;
    }


    /**
     * Sets the transactionDateTimeEnd value for this Parameters.
     * 
     * @param transactionDateTimeEnd
     */
    public void setTransactionDateTimeEnd(java.lang.String transactionDateTimeEnd) {
        this.transactionDateTimeEnd = transactionDateTimeEnd;
    }


    /**
     * Gets the transactionSetupID value for this Parameters.
     * 
     * @return transactionSetupID
     */
    public java.lang.String getTransactionSetupID() {
        return transactionSetupID;
    }


    /**
     * Sets the transactionSetupID value for this Parameters.
     * 
     * @param transactionSetupID
     */
    public void setTransactionSetupID(java.lang.String transactionSetupID) {
        this.transactionSetupID = transactionSetupID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Parameters)) return false;
        Parameters other = (Parameters) obj;
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
            ((this.terminalID==null && other.getTerminalID()==null) || 
             (this.terminalID!=null &&
              this.terminalID.equals(other.getTerminalID()))) &&
            ((this.applicationID==null && other.getApplicationID()==null) || 
             (this.applicationID!=null &&
              this.applicationID.equals(other.getApplicationID()))) &&
            ((this.approvalNumber==null && other.getApprovalNumber()==null) || 
             (this.approvalNumber!=null &&
              this.approvalNumber.equals(other.getApprovalNumber()))) &&
            ((this.approvedAmount==null && other.getApprovedAmount()==null) || 
             (this.approvedAmount!=null &&
              this.approvedAmount.equals(other.getApprovedAmount()))) &&
            ((this.expressTransactionDate==null && other.getExpressTransactionDate()==null) || 
             (this.expressTransactionDate!=null &&
              this.expressTransactionDate.equals(other.getExpressTransactionDate()))) &&
            ((this.expressTransactionTime==null && other.getExpressTransactionTime()==null) || 
             (this.expressTransactionTime!=null &&
              this.expressTransactionTime.equals(other.getExpressTransactionTime()))) &&
            ((this.hostBatchID==null && other.getHostBatchID()==null) || 
             (this.hostBatchID!=null &&
              this.hostBatchID.equals(other.getHostBatchID()))) &&
            ((this.hostItemID==null && other.getHostItemID()==null) || 
             (this.hostItemID!=null &&
              this.hostItemID.equals(other.getHostItemID()))) &&
            ((this.hostReversalQueueID==null && other.getHostReversalQueueID()==null) || 
             (this.hostReversalQueueID!=null &&
              this.hostReversalQueueID.equals(other.getHostReversalQueueID()))) &&
            ((this.originalAuthorizedAmount==null && other.getOriginalAuthorizedAmount()==null) || 
             (this.originalAuthorizedAmount!=null &&
              this.originalAuthorizedAmount.equals(other.getOriginalAuthorizedAmount()))) &&
            ((this.referenceNumber==null && other.getReferenceNumber()==null) || 
             (this.referenceNumber!=null &&
              this.referenceNumber.equals(other.getReferenceNumber()))) &&
            ((this.shiftID==null && other.getShiftID()==null) || 
             (this.shiftID!=null &&
              this.shiftID.equals(other.getShiftID()))) &&
            ((this.sourceTransactionID==null && other.getSourceTransactionID()==null) || 
             (this.sourceTransactionID!=null &&
              this.sourceTransactionID.equals(other.getSourceTransactionID()))) &&
            ((this.terminalType==null && other.getTerminalType()==null) || 
             (this.terminalType!=null &&
              this.terminalType.equals(other.getTerminalType()))) &&
            ((this.trackingID==null && other.getTrackingID()==null) || 
             (this.trackingID!=null &&
              this.trackingID.equals(other.getTrackingID()))) &&
            ((this.transactionAmount==null && other.getTransactionAmount()==null) || 
             (this.transactionAmount!=null &&
              this.transactionAmount.equals(other.getTransactionAmount()))) &&
            ((this.transactionStatus==null && other.getTransactionStatus()==null) || 
             (this.transactionStatus!=null &&
              this.transactionStatus.equals(other.getTransactionStatus()))) &&
            ((this.transactionStatusCode==null && other.getTransactionStatusCode()==null) || 
             (this.transactionStatusCode!=null &&
              this.transactionStatusCode.equals(other.getTransactionStatusCode()))) &&
            ((this.transactionType==null && other.getTransactionType()==null) || 
             (this.transactionType!=null &&
              this.transactionType.equals(other.getTransactionType()))) &&
            ((this.XID==null && other.getXID()==null) || 
             (this.XID!=null &&
              this.XID.equals(other.getXID()))) &&
            ((this.sourceIPAddress==null && other.getSourceIPAddress()==null) || 
             (this.sourceIPAddress!=null &&
              this.sourceIPAddress.equals(other.getSourceIPAddress()))) &&
            ((this.externalInterface==null && other.getExternalInterface()==null) || 
             (this.externalInterface!=null &&
              this.externalInterface.equals(other.getExternalInterface()))) &&
            ((this.logTraceLevel==null && other.getLogTraceLevel()==null) || 
             (this.logTraceLevel!=null &&
              java.util.Arrays.equals(this.logTraceLevel, other.getLogTraceLevel()))) &&
            ((this.logTraceLevelName==null && other.getLogTraceLevelName()==null) || 
             (this.logTraceLevelName!=null &&
              this.logTraceLevelName.equals(other.getLogTraceLevelName()))) &&
            ((this.machineName==null && other.getMachineName()==null) || 
             (this.machineName!=null &&
              this.machineName.equals(other.getMachineName()))) &&
            ((this.sourceObject==null && other.getSourceObject()==null) || 
             (this.sourceObject!=null &&
              this.sourceObject.equals(other.getSourceObject()))) &&
            ((this.processID==null && other.getProcessID()==null) || 
             (this.processID!=null &&
              this.processID.equals(other.getProcessID()))) &&
            ((this.threadID==null && other.getThreadID()==null) || 
             (this.threadID!=null &&
              this.threadID.equals(other.getThreadID()))) &&
            ((this.reverseOrder==null && other.getReverseOrder()==null) || 
             (this.reverseOrder!=null &&
              this.reverseOrder.equals(other.getReverseOrder()))) &&
            ((this.transactionDateTimeBegin==null && other.getTransactionDateTimeBegin()==null) || 
             (this.transactionDateTimeBegin!=null &&
              this.transactionDateTimeBegin.equals(other.getTransactionDateTimeBegin()))) &&
            ((this.transactionDateTimeEnd==null && other.getTransactionDateTimeEnd()==null) || 
             (this.transactionDateTimeEnd!=null &&
              this.transactionDateTimeEnd.equals(other.getTransactionDateTimeEnd()))) &&
            ((this.transactionSetupID==null && other.getTransactionSetupID()==null) || 
             (this.transactionSetupID!=null &&
              this.transactionSetupID.equals(other.getTransactionSetupID())));
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
        if (getTerminalID() != null) {
            _hashCode += getTerminalID().hashCode();
        }
        if (getApplicationID() != null) {
            _hashCode += getApplicationID().hashCode();
        }
        if (getApprovalNumber() != null) {
            _hashCode += getApprovalNumber().hashCode();
        }
        if (getApprovedAmount() != null) {
            _hashCode += getApprovedAmount().hashCode();
        }
        if (getExpressTransactionDate() != null) {
            _hashCode += getExpressTransactionDate().hashCode();
        }
        if (getExpressTransactionTime() != null) {
            _hashCode += getExpressTransactionTime().hashCode();
        }
        if (getHostBatchID() != null) {
            _hashCode += getHostBatchID().hashCode();
        }
        if (getHostItemID() != null) {
            _hashCode += getHostItemID().hashCode();
        }
        if (getHostReversalQueueID() != null) {
            _hashCode += getHostReversalQueueID().hashCode();
        }
        if (getOriginalAuthorizedAmount() != null) {
            _hashCode += getOriginalAuthorizedAmount().hashCode();
        }
        if (getReferenceNumber() != null) {
            _hashCode += getReferenceNumber().hashCode();
        }
        if (getShiftID() != null) {
            _hashCode += getShiftID().hashCode();
        }
        if (getSourceTransactionID() != null) {
            _hashCode += getSourceTransactionID().hashCode();
        }
        if (getTerminalType() != null) {
            _hashCode += getTerminalType().hashCode();
        }
        if (getTrackingID() != null) {
            _hashCode += getTrackingID().hashCode();
        }
        if (getTransactionAmount() != null) {
            _hashCode += getTransactionAmount().hashCode();
        }
        if (getTransactionStatus() != null) {
            _hashCode += getTransactionStatus().hashCode();
        }
        if (getTransactionStatusCode() != null) {
            _hashCode += getTransactionStatusCode().hashCode();
        }
        if (getTransactionType() != null) {
            _hashCode += getTransactionType().hashCode();
        }
        if (getXID() != null) {
            _hashCode += getXID().hashCode();
        }
        if (getSourceIPAddress() != null) {
            _hashCode += getSourceIPAddress().hashCode();
        }
        if (getExternalInterface() != null) {
            _hashCode += getExternalInterface().hashCode();
        }
        if (getLogTraceLevel() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLogTraceLevel());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLogTraceLevel(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLogTraceLevelName() != null) {
            _hashCode += getLogTraceLevelName().hashCode();
        }
        if (getMachineName() != null) {
            _hashCode += getMachineName().hashCode();
        }
        if (getSourceObject() != null) {
            _hashCode += getSourceObject().hashCode();
        }
        if (getProcessID() != null) {
            _hashCode += getProcessID().hashCode();
        }
        if (getThreadID() != null) {
            _hashCode += getThreadID().hashCode();
        }
        if (getReverseOrder() != null) {
            _hashCode += getReverseOrder().hashCode();
        }
        if (getTransactionDateTimeBegin() != null) {
            _hashCode += getTransactionDateTimeBegin().hashCode();
        }
        if (getTransactionDateTimeEnd() != null) {
            _hashCode += getTransactionDateTimeEnd().hashCode();
        }
        if (getTransactionSetupID() != null) {
            _hashCode += getTransactionSetupID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Parameters.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Parameters"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TransactionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminalID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TerminalID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("applicationID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ApplicationID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("approvalNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ApprovalNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("approvedAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ApprovedAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressTransactionDate");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExpressTransactionDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressTransactionTime");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExpressTransactionTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostBatchID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "HostBatchID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostItemID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "HostItemID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostReversalQueueID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "HostReversalQueueID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originalAuthorizedAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "OriginalAuthorizedAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ReferenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shiftID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ShiftID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceTransactionID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "SourceTransactionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminalType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TerminalType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TerminalType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("trackingID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TrackingID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TransactionAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TransactionStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionStatusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TransactionStatusCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TransactionType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "XID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceIPAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "SourceIPAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("externalInterface");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExternalInterface"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logTraceLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "LogTraceLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "LogTraceLevel"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logTraceLevelName");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "LogTraceLevelName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("machineName");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "MachineName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceObject");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "SourceObject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ProcessID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("threadID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ThreadID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reverseOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ReverseOrder"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "BooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionDateTimeBegin");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TransactionDateTimeBegin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionDateTimeEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TransactionDateTimeEnd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetupID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TransactionSetupID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
