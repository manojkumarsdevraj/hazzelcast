/**
 * PaymentAccount.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class PaymentAccount  implements java.io.Serializable {
    private java.lang.String paymentAccountID;

    private com.paymentgateway.elementexpress.services.PaymentAccountType paymentAccountType;

    private java.lang.String paymentBrand;

    private java.lang.String paymentAccountReferenceNumber;

    private java.lang.String transactionSetupID;

    private com.paymentgateway.elementexpress.services.PASSUpdaterBatchStatus PASSUpdaterBatchStatus;

    private com.paymentgateway.elementexpress.services.PASSUpdaterOption PASSUpdaterOption;

    public PaymentAccount() {
    }

    public PaymentAccount(
           java.lang.String paymentAccountID,
           com.paymentgateway.elementexpress.services.PaymentAccountType paymentAccountType,
           java.lang.String paymentBrand,
           java.lang.String paymentAccountReferenceNumber,
           java.lang.String transactionSetupID,
           com.paymentgateway.elementexpress.services.PASSUpdaterBatchStatus PASSUpdaterBatchStatus,
           com.paymentgateway.elementexpress.services.PASSUpdaterOption PASSUpdaterOption) {
           this.paymentAccountID = paymentAccountID;
           this.paymentAccountType = paymentAccountType;
           this.paymentBrand = paymentBrand;
           this.paymentAccountReferenceNumber = paymentAccountReferenceNumber;
           this.transactionSetupID = transactionSetupID;
           this.PASSUpdaterBatchStatus = PASSUpdaterBatchStatus;
           this.PASSUpdaterOption = PASSUpdaterOption;
    }


    /**
     * Gets the paymentAccountID value for this PaymentAccount.
     * 
     * @return paymentAccountID
     */
    public java.lang.String getPaymentAccountID() {
        return paymentAccountID;
    }


    /**
     * Sets the paymentAccountID value for this PaymentAccount.
     * 
     * @param paymentAccountID
     */
    public void setPaymentAccountID(java.lang.String paymentAccountID) {
        this.paymentAccountID = paymentAccountID;
    }


    /**
     * Gets the paymentAccountType value for this PaymentAccount.
     * 
     * @return paymentAccountType
     */
    public com.paymentgateway.elementexpress.services.PaymentAccountType getPaymentAccountType() {
        return paymentAccountType;
    }


    /**
     * Sets the paymentAccountType value for this PaymentAccount.
     * 
     * @param paymentAccountType
     */
    public void setPaymentAccountType(com.paymentgateway.elementexpress.services.PaymentAccountType paymentAccountType) {
        this.paymentAccountType = paymentAccountType;
    }


    /**
     * Gets the paymentBrand value for this PaymentAccount.
     * 
     * @return paymentBrand
     */
    public java.lang.String getPaymentBrand() {
        return paymentBrand;
    }


    /**
     * Sets the paymentBrand value for this PaymentAccount.
     * 
     * @param paymentBrand
     */
    public void setPaymentBrand(java.lang.String paymentBrand) {
        this.paymentBrand = paymentBrand;
    }


    /**
     * Gets the paymentAccountReferenceNumber value for this PaymentAccount.
     * 
     * @return paymentAccountReferenceNumber
     */
    public java.lang.String getPaymentAccountReferenceNumber() {
        return paymentAccountReferenceNumber;
    }


    /**
     * Sets the paymentAccountReferenceNumber value for this PaymentAccount.
     * 
     * @param paymentAccountReferenceNumber
     */
    public void setPaymentAccountReferenceNumber(java.lang.String paymentAccountReferenceNumber) {
        this.paymentAccountReferenceNumber = paymentAccountReferenceNumber;
    }


    /**
     * Gets the transactionSetupID value for this PaymentAccount.
     * 
     * @return transactionSetupID
     */
    public java.lang.String getTransactionSetupID() {
        return transactionSetupID;
    }


    /**
     * Sets the transactionSetupID value for this PaymentAccount.
     * 
     * @param transactionSetupID
     */
    public void setTransactionSetupID(java.lang.String transactionSetupID) {
        this.transactionSetupID = transactionSetupID;
    }


    /**
     * Gets the PASSUpdaterBatchStatus value for this PaymentAccount.
     * 
     * @return PASSUpdaterBatchStatus
     */
    public com.paymentgateway.elementexpress.services.PASSUpdaterBatchStatus getPASSUpdaterBatchStatus() {
        return PASSUpdaterBatchStatus;
    }


    /**
     * Sets the PASSUpdaterBatchStatus value for this PaymentAccount.
     * 
     * @param PASSUpdaterBatchStatus
     */
    public void setPASSUpdaterBatchStatus(com.paymentgateway.elementexpress.services.PASSUpdaterBatchStatus PASSUpdaterBatchStatus) {
        this.PASSUpdaterBatchStatus = PASSUpdaterBatchStatus;
    }


    /**
     * Gets the PASSUpdaterOption value for this PaymentAccount.
     * 
     * @return PASSUpdaterOption
     */
    public com.paymentgateway.elementexpress.services.PASSUpdaterOption getPASSUpdaterOption() {
        return PASSUpdaterOption;
    }


    /**
     * Sets the PASSUpdaterOption value for this PaymentAccount.
     * 
     * @param PASSUpdaterOption
     */
    public void setPASSUpdaterOption(com.paymentgateway.elementexpress.services.PASSUpdaterOption PASSUpdaterOption) {
        this.PASSUpdaterOption = PASSUpdaterOption;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PaymentAccount)) return false;
        PaymentAccount other = (PaymentAccount) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.paymentAccountID==null && other.getPaymentAccountID()==null) || 
             (this.paymentAccountID!=null &&
              this.paymentAccountID.equals(other.getPaymentAccountID()))) &&
            ((this.paymentAccountType==null && other.getPaymentAccountType()==null) || 
             (this.paymentAccountType!=null &&
              this.paymentAccountType.equals(other.getPaymentAccountType()))) &&
            ((this.paymentBrand==null && other.getPaymentBrand()==null) || 
             (this.paymentBrand!=null &&
              this.paymentBrand.equals(other.getPaymentBrand()))) &&
            ((this.paymentAccountReferenceNumber==null && other.getPaymentAccountReferenceNumber()==null) || 
             (this.paymentAccountReferenceNumber!=null &&
              this.paymentAccountReferenceNumber.equals(other.getPaymentAccountReferenceNumber()))) &&
            ((this.transactionSetupID==null && other.getTransactionSetupID()==null) || 
             (this.transactionSetupID!=null &&
              this.transactionSetupID.equals(other.getTransactionSetupID()))) &&
            ((this.PASSUpdaterBatchStatus==null && other.getPASSUpdaterBatchStatus()==null) || 
             (this.PASSUpdaterBatchStatus!=null &&
              this.PASSUpdaterBatchStatus.equals(other.getPASSUpdaterBatchStatus()))) &&
            ((this.PASSUpdaterOption==null && other.getPASSUpdaterOption()==null) || 
             (this.PASSUpdaterOption!=null &&
              this.PASSUpdaterOption.equals(other.getPASSUpdaterOption())));
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
        if (getPaymentAccountID() != null) {
            _hashCode += getPaymentAccountID().hashCode();
        }
        if (getPaymentAccountType() != null) {
            _hashCode += getPaymentAccountType().hashCode();
        }
        if (getPaymentBrand() != null) {
            _hashCode += getPaymentBrand().hashCode();
        }
        if (getPaymentAccountReferenceNumber() != null) {
            _hashCode += getPaymentAccountReferenceNumber().hashCode();
        }
        if (getTransactionSetupID() != null) {
            _hashCode += getTransactionSetupID().hashCode();
        }
        if (getPASSUpdaterBatchStatus() != null) {
            _hashCode += getPASSUpdaterBatchStatus().hashCode();
        }
        if (getPASSUpdaterOption() != null) {
            _hashCode += getPASSUpdaterOption().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PaymentAccount.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccount"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentAccountID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentAccountType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentBrand");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentBrand"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentAccountReferenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountReferenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetupID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "TransactionSetupID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PASSUpdaterBatchStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterBatchStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterBatchStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PASSUpdaterOption");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterOption"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterOption"));
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
