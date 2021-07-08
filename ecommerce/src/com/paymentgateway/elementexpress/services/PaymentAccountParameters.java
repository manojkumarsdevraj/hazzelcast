/**
 * PaymentAccountParameters.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class PaymentAccountParameters  implements java.io.Serializable {
    private java.lang.String paymentAccountID;

    private com.paymentgateway.elementexpress.services.PaymentAccountType paymentAccountType;

    private java.lang.String paymentAccountReferenceNumber;

    private java.lang.String expirationMonthBegin;

    private java.lang.String expirationMonthEnd;

    private java.lang.String expirationYearBegin;

    private java.lang.String expirationYearEnd;

    private java.lang.String transactionSetupID;

    private java.lang.String paymentBrand;

    private com.paymentgateway.elementexpress.services.PASSUpdaterStatus PASSUpdaterStatus;

    private com.paymentgateway.elementexpress.services.PASSUpdaterBatchStatus PASSUpdaterBatchStatus;

    private java.lang.String PASSUpdaterDateTimeBegin;

    private java.lang.String PASSUpdaterDateTimeEnd;

    public PaymentAccountParameters() {
    }

    public PaymentAccountParameters(
           java.lang.String paymentAccountID,
           com.paymentgateway.elementexpress.services.PaymentAccountType paymentAccountType,
           java.lang.String paymentAccountReferenceNumber,
           java.lang.String expirationMonthBegin,
           java.lang.String expirationMonthEnd,
           java.lang.String expirationYearBegin,
           java.lang.String expirationYearEnd,
           java.lang.String transactionSetupID,
           java.lang.String paymentBrand,
           com.paymentgateway.elementexpress.services.PASSUpdaterStatus PASSUpdaterStatus,
           com.paymentgateway.elementexpress.services.PASSUpdaterBatchStatus PASSUpdaterBatchStatus,
           java.lang.String PASSUpdaterDateTimeBegin,
           java.lang.String PASSUpdaterDateTimeEnd) {
           this.paymentAccountID = paymentAccountID;
           this.paymentAccountType = paymentAccountType;
           this.paymentAccountReferenceNumber = paymentAccountReferenceNumber;
           this.expirationMonthBegin = expirationMonthBegin;
           this.expirationMonthEnd = expirationMonthEnd;
           this.expirationYearBegin = expirationYearBegin;
           this.expirationYearEnd = expirationYearEnd;
           this.transactionSetupID = transactionSetupID;
           this.paymentBrand = paymentBrand;
           this.PASSUpdaterStatus = PASSUpdaterStatus;
           this.PASSUpdaterBatchStatus = PASSUpdaterBatchStatus;
           this.PASSUpdaterDateTimeBegin = PASSUpdaterDateTimeBegin;
           this.PASSUpdaterDateTimeEnd = PASSUpdaterDateTimeEnd;
    }


    /**
     * Gets the paymentAccountID value for this PaymentAccountParameters.
     * 
     * @return paymentAccountID
     */
    public java.lang.String getPaymentAccountID() {
        return paymentAccountID;
    }


    /**
     * Sets the paymentAccountID value for this PaymentAccountParameters.
     * 
     * @param paymentAccountID
     */
    public void setPaymentAccountID(java.lang.String paymentAccountID) {
        this.paymentAccountID = paymentAccountID;
    }


    /**
     * Gets the paymentAccountType value for this PaymentAccountParameters.
     * 
     * @return paymentAccountType
     */
    public com.paymentgateway.elementexpress.services.PaymentAccountType getPaymentAccountType() {
        return paymentAccountType;
    }


    /**
     * Sets the paymentAccountType value for this PaymentAccountParameters.
     * 
     * @param paymentAccountType
     */
    public void setPaymentAccountType(com.paymentgateway.elementexpress.services.PaymentAccountType paymentAccountType) {
        this.paymentAccountType = paymentAccountType;
    }


    /**
     * Gets the paymentAccountReferenceNumber value for this PaymentAccountParameters.
     * 
     * @return paymentAccountReferenceNumber
     */
    public java.lang.String getPaymentAccountReferenceNumber() {
        return paymentAccountReferenceNumber;
    }


    /**
     * Sets the paymentAccountReferenceNumber value for this PaymentAccountParameters.
     * 
     * @param paymentAccountReferenceNumber
     */
    public void setPaymentAccountReferenceNumber(java.lang.String paymentAccountReferenceNumber) {
        this.paymentAccountReferenceNumber = paymentAccountReferenceNumber;
    }


    /**
     * Gets the expirationMonthBegin value for this PaymentAccountParameters.
     * 
     * @return expirationMonthBegin
     */
    public java.lang.String getExpirationMonthBegin() {
        return expirationMonthBegin;
    }


    /**
     * Sets the expirationMonthBegin value for this PaymentAccountParameters.
     * 
     * @param expirationMonthBegin
     */
    public void setExpirationMonthBegin(java.lang.String expirationMonthBegin) {
        this.expirationMonthBegin = expirationMonthBegin;
    }


    /**
     * Gets the expirationMonthEnd value for this PaymentAccountParameters.
     * 
     * @return expirationMonthEnd
     */
    public java.lang.String getExpirationMonthEnd() {
        return expirationMonthEnd;
    }


    /**
     * Sets the expirationMonthEnd value for this PaymentAccountParameters.
     * 
     * @param expirationMonthEnd
     */
    public void setExpirationMonthEnd(java.lang.String expirationMonthEnd) {
        this.expirationMonthEnd = expirationMonthEnd;
    }


    /**
     * Gets the expirationYearBegin value for this PaymentAccountParameters.
     * 
     * @return expirationYearBegin
     */
    public java.lang.String getExpirationYearBegin() {
        return expirationYearBegin;
    }


    /**
     * Sets the expirationYearBegin value for this PaymentAccountParameters.
     * 
     * @param expirationYearBegin
     */
    public void setExpirationYearBegin(java.lang.String expirationYearBegin) {
        this.expirationYearBegin = expirationYearBegin;
    }


    /**
     * Gets the expirationYearEnd value for this PaymentAccountParameters.
     * 
     * @return expirationYearEnd
     */
    public java.lang.String getExpirationYearEnd() {
        return expirationYearEnd;
    }


    /**
     * Sets the expirationYearEnd value for this PaymentAccountParameters.
     * 
     * @param expirationYearEnd
     */
    public void setExpirationYearEnd(java.lang.String expirationYearEnd) {
        this.expirationYearEnd = expirationYearEnd;
    }


    /**
     * Gets the transactionSetupID value for this PaymentAccountParameters.
     * 
     * @return transactionSetupID
     */
    public java.lang.String getTransactionSetupID() {
        return transactionSetupID;
    }


    /**
     * Sets the transactionSetupID value for this PaymentAccountParameters.
     * 
     * @param transactionSetupID
     */
    public void setTransactionSetupID(java.lang.String transactionSetupID) {
        this.transactionSetupID = transactionSetupID;
    }


    /**
     * Gets the paymentBrand value for this PaymentAccountParameters.
     * 
     * @return paymentBrand
     */
    public java.lang.String getPaymentBrand() {
        return paymentBrand;
    }


    /**
     * Sets the paymentBrand value for this PaymentAccountParameters.
     * 
     * @param paymentBrand
     */
    public void setPaymentBrand(java.lang.String paymentBrand) {
        this.paymentBrand = paymentBrand;
    }


    /**
     * Gets the PASSUpdaterStatus value for this PaymentAccountParameters.
     * 
     * @return PASSUpdaterStatus
     */
    public com.paymentgateway.elementexpress.services.PASSUpdaterStatus getPASSUpdaterStatus() {
        return PASSUpdaterStatus;
    }


    /**
     * Sets the PASSUpdaterStatus value for this PaymentAccountParameters.
     * 
     * @param PASSUpdaterStatus
     */
    public void setPASSUpdaterStatus(com.paymentgateway.elementexpress.services.PASSUpdaterStatus PASSUpdaterStatus) {
        this.PASSUpdaterStatus = PASSUpdaterStatus;
    }


    /**
     * Gets the PASSUpdaterBatchStatus value for this PaymentAccountParameters.
     * 
     * @return PASSUpdaterBatchStatus
     */
    public com.paymentgateway.elementexpress.services.PASSUpdaterBatchStatus getPASSUpdaterBatchStatus() {
        return PASSUpdaterBatchStatus;
    }


    /**
     * Sets the PASSUpdaterBatchStatus value for this PaymentAccountParameters.
     * 
     * @param PASSUpdaterBatchStatus
     */
    public void setPASSUpdaterBatchStatus(com.paymentgateway.elementexpress.services.PASSUpdaterBatchStatus PASSUpdaterBatchStatus) {
        this.PASSUpdaterBatchStatus = PASSUpdaterBatchStatus;
    }


    /**
     * Gets the PASSUpdaterDateTimeBegin value for this PaymentAccountParameters.
     * 
     * @return PASSUpdaterDateTimeBegin
     */
    public java.lang.String getPASSUpdaterDateTimeBegin() {
        return PASSUpdaterDateTimeBegin;
    }


    /**
     * Sets the PASSUpdaterDateTimeBegin value for this PaymentAccountParameters.
     * 
     * @param PASSUpdaterDateTimeBegin
     */
    public void setPASSUpdaterDateTimeBegin(java.lang.String PASSUpdaterDateTimeBegin) {
        this.PASSUpdaterDateTimeBegin = PASSUpdaterDateTimeBegin;
    }


    /**
     * Gets the PASSUpdaterDateTimeEnd value for this PaymentAccountParameters.
     * 
     * @return PASSUpdaterDateTimeEnd
     */
    public java.lang.String getPASSUpdaterDateTimeEnd() {
        return PASSUpdaterDateTimeEnd;
    }


    /**
     * Sets the PASSUpdaterDateTimeEnd value for this PaymentAccountParameters.
     * 
     * @param PASSUpdaterDateTimeEnd
     */
    public void setPASSUpdaterDateTimeEnd(java.lang.String PASSUpdaterDateTimeEnd) {
        this.PASSUpdaterDateTimeEnd = PASSUpdaterDateTimeEnd;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PaymentAccountParameters)) return false;
        PaymentAccountParameters other = (PaymentAccountParameters) obj;
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
            ((this.paymentAccountReferenceNumber==null && other.getPaymentAccountReferenceNumber()==null) || 
             (this.paymentAccountReferenceNumber!=null &&
              this.paymentAccountReferenceNumber.equals(other.getPaymentAccountReferenceNumber()))) &&
            ((this.expirationMonthBegin==null && other.getExpirationMonthBegin()==null) || 
             (this.expirationMonthBegin!=null &&
              this.expirationMonthBegin.equals(other.getExpirationMonthBegin()))) &&
            ((this.expirationMonthEnd==null && other.getExpirationMonthEnd()==null) || 
             (this.expirationMonthEnd!=null &&
              this.expirationMonthEnd.equals(other.getExpirationMonthEnd()))) &&
            ((this.expirationYearBegin==null && other.getExpirationYearBegin()==null) || 
             (this.expirationYearBegin!=null &&
              this.expirationYearBegin.equals(other.getExpirationYearBegin()))) &&
            ((this.expirationYearEnd==null && other.getExpirationYearEnd()==null) || 
             (this.expirationYearEnd!=null &&
              this.expirationYearEnd.equals(other.getExpirationYearEnd()))) &&
            ((this.transactionSetupID==null && other.getTransactionSetupID()==null) || 
             (this.transactionSetupID!=null &&
              this.transactionSetupID.equals(other.getTransactionSetupID()))) &&
            ((this.paymentBrand==null && other.getPaymentBrand()==null) || 
             (this.paymentBrand!=null &&
              this.paymentBrand.equals(other.getPaymentBrand()))) &&
            ((this.PASSUpdaterStatus==null && other.getPASSUpdaterStatus()==null) || 
             (this.PASSUpdaterStatus!=null &&
              this.PASSUpdaterStatus.equals(other.getPASSUpdaterStatus()))) &&
            ((this.PASSUpdaterBatchStatus==null && other.getPASSUpdaterBatchStatus()==null) || 
             (this.PASSUpdaterBatchStatus!=null &&
              this.PASSUpdaterBatchStatus.equals(other.getPASSUpdaterBatchStatus()))) &&
            ((this.PASSUpdaterDateTimeBegin==null && other.getPASSUpdaterDateTimeBegin()==null) || 
             (this.PASSUpdaterDateTimeBegin!=null &&
              this.PASSUpdaterDateTimeBegin.equals(other.getPASSUpdaterDateTimeBegin()))) &&
            ((this.PASSUpdaterDateTimeEnd==null && other.getPASSUpdaterDateTimeEnd()==null) || 
             (this.PASSUpdaterDateTimeEnd!=null &&
              this.PASSUpdaterDateTimeEnd.equals(other.getPASSUpdaterDateTimeEnd())));
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
        if (getPaymentAccountReferenceNumber() != null) {
            _hashCode += getPaymentAccountReferenceNumber().hashCode();
        }
        if (getExpirationMonthBegin() != null) {
            _hashCode += getExpirationMonthBegin().hashCode();
        }
        if (getExpirationMonthEnd() != null) {
            _hashCode += getExpirationMonthEnd().hashCode();
        }
        if (getExpirationYearBegin() != null) {
            _hashCode += getExpirationYearBegin().hashCode();
        }
        if (getExpirationYearEnd() != null) {
            _hashCode += getExpirationYearEnd().hashCode();
        }
        if (getTransactionSetupID() != null) {
            _hashCode += getTransactionSetupID().hashCode();
        }
        if (getPaymentBrand() != null) {
            _hashCode += getPaymentBrand().hashCode();
        }
        if (getPASSUpdaterStatus() != null) {
            _hashCode += getPASSUpdaterStatus().hashCode();
        }
        if (getPASSUpdaterBatchStatus() != null) {
            _hashCode += getPASSUpdaterBatchStatus().hashCode();
        }
        if (getPASSUpdaterDateTimeBegin() != null) {
            _hashCode += getPASSUpdaterDateTimeBegin().hashCode();
        }
        if (getPASSUpdaterDateTimeEnd() != null) {
            _hashCode += getPASSUpdaterDateTimeEnd().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PaymentAccountParameters.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountParameters"));
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
        elemField.setFieldName("paymentAccountReferenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountReferenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expirationMonthBegin");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExpirationMonthBegin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expirationMonthEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExpirationMonthEnd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expirationYearBegin");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExpirationYearBegin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expirationYearEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExpirationYearEnd"));
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
        elemField.setFieldName("paymentBrand");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentBrand"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PASSUpdaterStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PASSUpdaterBatchStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterBatchStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterBatchStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PASSUpdaterDateTimeBegin");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterDateTimeBegin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PASSUpdaterDateTimeEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterDateTimeEnd"));
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
