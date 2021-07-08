/**
 * TransactionSetup.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class TransactionSetup  implements java.io.Serializable {
    private java.lang.String transactionSetupID;

    private java.lang.String transactionSetupAccountID;

    private java.lang.String transactionSetupAcceptorID;

    private java.lang.String transactionSetupApplicationID;

    private java.lang.String transactionSetupApplicationName;

    private java.lang.String transactionSetupApplicationVersion;

    private com.paymentgateway.elementexpress.transaction.TransactionSetupMethod transactionSetupMethod;

    private com.paymentgateway.elementexpress.transaction.Device device;

    private com.paymentgateway.elementexpress.transaction.BooleanType embedded;

    private com.paymentgateway.elementexpress.transaction.BooleanType CVVRequired;

    private com.paymentgateway.elementexpress.transaction.BooleanType autoReturn;

    private java.lang.String companyName;

    private java.lang.String logoURL;

    private java.lang.String tagline;

    private java.lang.String welcomeMessage;

    private java.lang.String returnURL;

    private java.lang.String returnURLTitle;

    private java.lang.String orderDetails;

    private java.lang.String processTransactionTitle;

    private java.lang.String validationCode;

    private com.paymentgateway.elementexpress.transaction.DeviceInputCode deviceInputCode;

    public TransactionSetup() {
    }

    public TransactionSetup(
           java.lang.String transactionSetupID,
           java.lang.String transactionSetupAccountID,
           java.lang.String transactionSetupAcceptorID,
           java.lang.String transactionSetupApplicationID,
           java.lang.String transactionSetupApplicationName,
           java.lang.String transactionSetupApplicationVersion,
           com.paymentgateway.elementexpress.transaction.TransactionSetupMethod transactionSetupMethod,
           com.paymentgateway.elementexpress.transaction.Device device,
           com.paymentgateway.elementexpress.transaction.BooleanType embedded,
           com.paymentgateway.elementexpress.transaction.BooleanType CVVRequired,
           com.paymentgateway.elementexpress.transaction.BooleanType autoReturn,
           java.lang.String companyName,
           java.lang.String logoURL,
           java.lang.String tagline,
           java.lang.String welcomeMessage,
           java.lang.String returnURL,
           java.lang.String returnURLTitle,
           java.lang.String orderDetails,
           java.lang.String processTransactionTitle,
           java.lang.String validationCode,
           com.paymentgateway.elementexpress.transaction.DeviceInputCode deviceInputCode) {
           this.transactionSetupID = transactionSetupID;
           this.transactionSetupAccountID = transactionSetupAccountID;
           this.transactionSetupAcceptorID = transactionSetupAcceptorID;
           this.transactionSetupApplicationID = transactionSetupApplicationID;
           this.transactionSetupApplicationName = transactionSetupApplicationName;
           this.transactionSetupApplicationVersion = transactionSetupApplicationVersion;
           this.transactionSetupMethod = transactionSetupMethod;
           this.device = device;
           this.embedded = embedded;
           this.CVVRequired = CVVRequired;
           this.autoReturn = autoReturn;
           this.companyName = companyName;
           this.logoURL = logoURL;
           this.tagline = tagline;
           this.welcomeMessage = welcomeMessage;
           this.returnURL = returnURL;
           this.returnURLTitle = returnURLTitle;
           this.orderDetails = orderDetails;
           this.processTransactionTitle = processTransactionTitle;
           this.validationCode = validationCode;
           this.deviceInputCode = deviceInputCode;
    }


    /**
     * Gets the transactionSetupID value for this TransactionSetup.
     * 
     * @return transactionSetupID
     */
    public java.lang.String getTransactionSetupID() {
        return transactionSetupID;
    }


    /**
     * Sets the transactionSetupID value for this TransactionSetup.
     * 
     * @param transactionSetupID
     */
    public void setTransactionSetupID(java.lang.String transactionSetupID) {
        this.transactionSetupID = transactionSetupID;
    }


    /**
     * Gets the transactionSetupAccountID value for this TransactionSetup.
     * 
     * @return transactionSetupAccountID
     */
    public java.lang.String getTransactionSetupAccountID() {
        return transactionSetupAccountID;
    }


    /**
     * Sets the transactionSetupAccountID value for this TransactionSetup.
     * 
     * @param transactionSetupAccountID
     */
    public void setTransactionSetupAccountID(java.lang.String transactionSetupAccountID) {
        this.transactionSetupAccountID = transactionSetupAccountID;
    }


    /**
     * Gets the transactionSetupAcceptorID value for this TransactionSetup.
     * 
     * @return transactionSetupAcceptorID
     */
    public java.lang.String getTransactionSetupAcceptorID() {
        return transactionSetupAcceptorID;
    }


    /**
     * Sets the transactionSetupAcceptorID value for this TransactionSetup.
     * 
     * @param transactionSetupAcceptorID
     */
    public void setTransactionSetupAcceptorID(java.lang.String transactionSetupAcceptorID) {
        this.transactionSetupAcceptorID = transactionSetupAcceptorID;
    }


    /**
     * Gets the transactionSetupApplicationID value for this TransactionSetup.
     * 
     * @return transactionSetupApplicationID
     */
    public java.lang.String getTransactionSetupApplicationID() {
        return transactionSetupApplicationID;
    }


    /**
     * Sets the transactionSetupApplicationID value for this TransactionSetup.
     * 
     * @param transactionSetupApplicationID
     */
    public void setTransactionSetupApplicationID(java.lang.String transactionSetupApplicationID) {
        this.transactionSetupApplicationID = transactionSetupApplicationID;
    }


    /**
     * Gets the transactionSetupApplicationName value for this TransactionSetup.
     * 
     * @return transactionSetupApplicationName
     */
    public java.lang.String getTransactionSetupApplicationName() {
        return transactionSetupApplicationName;
    }


    /**
     * Sets the transactionSetupApplicationName value for this TransactionSetup.
     * 
     * @param transactionSetupApplicationName
     */
    public void setTransactionSetupApplicationName(java.lang.String transactionSetupApplicationName) {
        this.transactionSetupApplicationName = transactionSetupApplicationName;
    }


    /**
     * Gets the transactionSetupApplicationVersion value for this TransactionSetup.
     * 
     * @return transactionSetupApplicationVersion
     */
    public java.lang.String getTransactionSetupApplicationVersion() {
        return transactionSetupApplicationVersion;
    }


    /**
     * Sets the transactionSetupApplicationVersion value for this TransactionSetup.
     * 
     * @param transactionSetupApplicationVersion
     */
    public void setTransactionSetupApplicationVersion(java.lang.String transactionSetupApplicationVersion) {
        this.transactionSetupApplicationVersion = transactionSetupApplicationVersion;
    }


    /**
     * Gets the transactionSetupMethod value for this TransactionSetup.
     * 
     * @return transactionSetupMethod
     */
    public com.paymentgateway.elementexpress.transaction.TransactionSetupMethod getTransactionSetupMethod() {
        return transactionSetupMethod;
    }


    /**
     * Sets the transactionSetupMethod value for this TransactionSetup.
     * 
     * @param transactionSetupMethod
     */
    public void setTransactionSetupMethod(com.paymentgateway.elementexpress.transaction.TransactionSetupMethod transactionSetupMethod) {
        this.transactionSetupMethod = transactionSetupMethod;
    }


    /**
     * Gets the device value for this TransactionSetup.
     * 
     * @return device
     */
    public com.paymentgateway.elementexpress.transaction.Device getDevice() {
        return device;
    }


    /**
     * Sets the device value for this TransactionSetup.
     * 
     * @param device
     */
    public void setDevice(com.paymentgateway.elementexpress.transaction.Device device) {
        this.device = device;
    }


    /**
     * Gets the embedded value for this TransactionSetup.
     * 
     * @return embedded
     */
    public com.paymentgateway.elementexpress.transaction.BooleanType getEmbedded() {
        return embedded;
    }


    /**
     * Sets the embedded value for this TransactionSetup.
     * 
     * @param embedded
     */
    public void setEmbedded(com.paymentgateway.elementexpress.transaction.BooleanType embedded) {
        this.embedded = embedded;
    }


    /**
     * Gets the CVVRequired value for this TransactionSetup.
     * 
     * @return CVVRequired
     */
    public com.paymentgateway.elementexpress.transaction.BooleanType getCVVRequired() {
        return CVVRequired;
    }


    /**
     * Sets the CVVRequired value for this TransactionSetup.
     * 
     * @param CVVRequired
     */
    public void setCVVRequired(com.paymentgateway.elementexpress.transaction.BooleanType CVVRequired) {
        this.CVVRequired = CVVRequired;
    }


    /**
     * Gets the autoReturn value for this TransactionSetup.
     * 
     * @return autoReturn
     */
    public com.paymentgateway.elementexpress.transaction.BooleanType getAutoReturn() {
        return autoReturn;
    }


    /**
     * Sets the autoReturn value for this TransactionSetup.
     * 
     * @param autoReturn
     */
    public void setAutoReturn(com.paymentgateway.elementexpress.transaction.BooleanType autoReturn) {
        this.autoReturn = autoReturn;
    }


    /**
     * Gets the companyName value for this TransactionSetup.
     * 
     * @return companyName
     */
    public java.lang.String getCompanyName() {
        return companyName;
    }


    /**
     * Sets the companyName value for this TransactionSetup.
     * 
     * @param companyName
     */
    public void setCompanyName(java.lang.String companyName) {
        this.companyName = companyName;
    }


    /**
     * Gets the logoURL value for this TransactionSetup.
     * 
     * @return logoURL
     */
    public java.lang.String getLogoURL() {
        return logoURL;
    }


    /**
     * Sets the logoURL value for this TransactionSetup.
     * 
     * @param logoURL
     */
    public void setLogoURL(java.lang.String logoURL) {
        this.logoURL = logoURL;
    }


    /**
     * Gets the tagline value for this TransactionSetup.
     * 
     * @return tagline
     */
    public java.lang.String getTagline() {
        return tagline;
    }


    /**
     * Sets the tagline value for this TransactionSetup.
     * 
     * @param tagline
     */
    public void setTagline(java.lang.String tagline) {
        this.tagline = tagline;
    }


    /**
     * Gets the welcomeMessage value for this TransactionSetup.
     * 
     * @return welcomeMessage
     */
    public java.lang.String getWelcomeMessage() {
        return welcomeMessage;
    }


    /**
     * Sets the welcomeMessage value for this TransactionSetup.
     * 
     * @param welcomeMessage
     */
    public void setWelcomeMessage(java.lang.String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }


    /**
     * Gets the returnURL value for this TransactionSetup.
     * 
     * @return returnURL
     */
    public java.lang.String getReturnURL() {
        return returnURL;
    }


    /**
     * Sets the returnURL value for this TransactionSetup.
     * 
     * @param returnURL
     */
    public void setReturnURL(java.lang.String returnURL) {
        this.returnURL = returnURL;
    }


    /**
     * Gets the returnURLTitle value for this TransactionSetup.
     * 
     * @return returnURLTitle
     */
    public java.lang.String getReturnURLTitle() {
        return returnURLTitle;
    }


    /**
     * Sets the returnURLTitle value for this TransactionSetup.
     * 
     * @param returnURLTitle
     */
    public void setReturnURLTitle(java.lang.String returnURLTitle) {
        this.returnURLTitle = returnURLTitle;
    }


    /**
     * Gets the orderDetails value for this TransactionSetup.
     * 
     * @return orderDetails
     */
    public java.lang.String getOrderDetails() {
        return orderDetails;
    }


    /**
     * Sets the orderDetails value for this TransactionSetup.
     * 
     * @param orderDetails
     */
    public void setOrderDetails(java.lang.String orderDetails) {
        this.orderDetails = orderDetails;
    }


    /**
     * Gets the processTransactionTitle value for this TransactionSetup.
     * 
     * @return processTransactionTitle
     */
    public java.lang.String getProcessTransactionTitle() {
        return processTransactionTitle;
    }


    /**
     * Sets the processTransactionTitle value for this TransactionSetup.
     * 
     * @param processTransactionTitle
     */
    public void setProcessTransactionTitle(java.lang.String processTransactionTitle) {
        this.processTransactionTitle = processTransactionTitle;
    }


    /**
     * Gets the validationCode value for this TransactionSetup.
     * 
     * @return validationCode
     */
    public java.lang.String getValidationCode() {
        return validationCode;
    }


    /**
     * Sets the validationCode value for this TransactionSetup.
     * 
     * @param validationCode
     */
    public void setValidationCode(java.lang.String validationCode) {
        this.validationCode = validationCode;
    }


    /**
     * Gets the deviceInputCode value for this TransactionSetup.
     * 
     * @return deviceInputCode
     */
    public com.paymentgateway.elementexpress.transaction.DeviceInputCode getDeviceInputCode() {
        return deviceInputCode;
    }


    /**
     * Sets the deviceInputCode value for this TransactionSetup.
     * 
     * @param deviceInputCode
     */
    public void setDeviceInputCode(com.paymentgateway.elementexpress.transaction.DeviceInputCode deviceInputCode) {
        this.deviceInputCode = deviceInputCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransactionSetup)) return false;
        TransactionSetup other = (TransactionSetup) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.transactionSetupID==null && other.getTransactionSetupID()==null) || 
             (this.transactionSetupID!=null &&
              this.transactionSetupID.equals(other.getTransactionSetupID()))) &&
            ((this.transactionSetupAccountID==null && other.getTransactionSetupAccountID()==null) || 
             (this.transactionSetupAccountID!=null &&
              this.transactionSetupAccountID.equals(other.getTransactionSetupAccountID()))) &&
            ((this.transactionSetupAcceptorID==null && other.getTransactionSetupAcceptorID()==null) || 
             (this.transactionSetupAcceptorID!=null &&
              this.transactionSetupAcceptorID.equals(other.getTransactionSetupAcceptorID()))) &&
            ((this.transactionSetupApplicationID==null && other.getTransactionSetupApplicationID()==null) || 
             (this.transactionSetupApplicationID!=null &&
              this.transactionSetupApplicationID.equals(other.getTransactionSetupApplicationID()))) &&
            ((this.transactionSetupApplicationName==null && other.getTransactionSetupApplicationName()==null) || 
             (this.transactionSetupApplicationName!=null &&
              this.transactionSetupApplicationName.equals(other.getTransactionSetupApplicationName()))) &&
            ((this.transactionSetupApplicationVersion==null && other.getTransactionSetupApplicationVersion()==null) || 
             (this.transactionSetupApplicationVersion!=null &&
              this.transactionSetupApplicationVersion.equals(other.getTransactionSetupApplicationVersion()))) &&
            ((this.transactionSetupMethod==null && other.getTransactionSetupMethod()==null) || 
             (this.transactionSetupMethod!=null &&
              this.transactionSetupMethod.equals(other.getTransactionSetupMethod()))) &&
            ((this.device==null && other.getDevice()==null) || 
             (this.device!=null &&
              this.device.equals(other.getDevice()))) &&
            ((this.embedded==null && other.getEmbedded()==null) || 
             (this.embedded!=null &&
              this.embedded.equals(other.getEmbedded()))) &&
            ((this.CVVRequired==null && other.getCVVRequired()==null) || 
             (this.CVVRequired!=null &&
              this.CVVRequired.equals(other.getCVVRequired()))) &&
            ((this.autoReturn==null && other.getAutoReturn()==null) || 
             (this.autoReturn!=null &&
              this.autoReturn.equals(other.getAutoReturn()))) &&
            ((this.companyName==null && other.getCompanyName()==null) || 
             (this.companyName!=null &&
              this.companyName.equals(other.getCompanyName()))) &&
            ((this.logoURL==null && other.getLogoURL()==null) || 
             (this.logoURL!=null &&
              this.logoURL.equals(other.getLogoURL()))) &&
            ((this.tagline==null && other.getTagline()==null) || 
             (this.tagline!=null &&
              this.tagline.equals(other.getTagline()))) &&
            ((this.welcomeMessage==null && other.getWelcomeMessage()==null) || 
             (this.welcomeMessage!=null &&
              this.welcomeMessage.equals(other.getWelcomeMessage()))) &&
            ((this.returnURL==null && other.getReturnURL()==null) || 
             (this.returnURL!=null &&
              this.returnURL.equals(other.getReturnURL()))) &&
            ((this.returnURLTitle==null && other.getReturnURLTitle()==null) || 
             (this.returnURLTitle!=null &&
              this.returnURLTitle.equals(other.getReturnURLTitle()))) &&
            ((this.orderDetails==null && other.getOrderDetails()==null) || 
             (this.orderDetails!=null &&
              this.orderDetails.equals(other.getOrderDetails()))) &&
            ((this.processTransactionTitle==null && other.getProcessTransactionTitle()==null) || 
             (this.processTransactionTitle!=null &&
              this.processTransactionTitle.equals(other.getProcessTransactionTitle()))) &&
            ((this.validationCode==null && other.getValidationCode()==null) || 
             (this.validationCode!=null &&
              this.validationCode.equals(other.getValidationCode()))) &&
            ((this.deviceInputCode==null && other.getDeviceInputCode()==null) || 
             (this.deviceInputCode!=null &&
              this.deviceInputCode.equals(other.getDeviceInputCode())));
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
        if (getTransactionSetupID() != null) {
            _hashCode += getTransactionSetupID().hashCode();
        }
        if (getTransactionSetupAccountID() != null) {
            _hashCode += getTransactionSetupAccountID().hashCode();
        }
        if (getTransactionSetupAcceptorID() != null) {
            _hashCode += getTransactionSetupAcceptorID().hashCode();
        }
        if (getTransactionSetupApplicationID() != null) {
            _hashCode += getTransactionSetupApplicationID().hashCode();
        }
        if (getTransactionSetupApplicationName() != null) {
            _hashCode += getTransactionSetupApplicationName().hashCode();
        }
        if (getTransactionSetupApplicationVersion() != null) {
            _hashCode += getTransactionSetupApplicationVersion().hashCode();
        }
        if (getTransactionSetupMethod() != null) {
            _hashCode += getTransactionSetupMethod().hashCode();
        }
        if (getDevice() != null) {
            _hashCode += getDevice().hashCode();
        }
        if (getEmbedded() != null) {
            _hashCode += getEmbedded().hashCode();
        }
        if (getCVVRequired() != null) {
            _hashCode += getCVVRequired().hashCode();
        }
        if (getAutoReturn() != null) {
            _hashCode += getAutoReturn().hashCode();
        }
        if (getCompanyName() != null) {
            _hashCode += getCompanyName().hashCode();
        }
        if (getLogoURL() != null) {
            _hashCode += getLogoURL().hashCode();
        }
        if (getTagline() != null) {
            _hashCode += getTagline().hashCode();
        }
        if (getWelcomeMessage() != null) {
            _hashCode += getWelcomeMessage().hashCode();
        }
        if (getReturnURL() != null) {
            _hashCode += getReturnURL().hashCode();
        }
        if (getReturnURLTitle() != null) {
            _hashCode += getReturnURLTitle().hashCode();
        }
        if (getOrderDetails() != null) {
            _hashCode += getOrderDetails().hashCode();
        }
        if (getProcessTransactionTitle() != null) {
            _hashCode += getProcessTransactionTitle().hashCode();
        }
        if (getValidationCode() != null) {
            _hashCode += getValidationCode().hashCode();
        }
        if (getDeviceInputCode() != null) {
            _hashCode += getDeviceInputCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransactionSetup.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetup"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetupID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetupAccountID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupAccountID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetupAcceptorID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupAcceptorID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetupApplicationID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupApplicationID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetupApplicationName");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupApplicationName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetupApplicationVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupApplicationVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetupMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupMethod"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("device");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Device"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Device"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("embedded");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Embedded"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CVVRequired");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CVVRequired"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("autoReturn");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AutoReturn"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("companyName");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CompanyName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logoURL");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "LogoURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tagline");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Tagline"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("welcomeMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "WelcomeMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnURL");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ReturnURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnURLTitle");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ReturnURLTitle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "OrderDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processTransactionTitle");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ProcessTransactionTitle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("validationCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ValidationCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deviceInputCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DeviceInputCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DeviceInputCode"));
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
