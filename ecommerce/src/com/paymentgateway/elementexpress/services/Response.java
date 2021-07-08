/**
 * Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class Response  extends com.paymentgateway.elementexpress.services.ExpressResponse  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount;

    private com.paymentgateway.elementexpress.services.ScheduledTask scheduledTask;

    private java.lang.String queryData;

    private java.lang.String servicesID;

    public Response() {
    }

    public Response(
           java.lang.String expressResponseCode,
           java.lang.String expressResponseMessage,
           java.lang.String expressTransactionDate,
           java.lang.String expressTransactionTime,
           java.lang.String expressTransactionTimezone,
           com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters,
           com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount,
           com.paymentgateway.elementexpress.services.ScheduledTask scheduledTask,
           java.lang.String queryData,
           java.lang.String servicesID) {
        super(
            expressResponseCode,
            expressResponseMessage,
            expressTransactionDate,
            expressTransactionTime,
            expressTransactionTimezone,
            extendedParameters);
        this.paymentAccount = paymentAccount;
        this.scheduledTask = scheduledTask;
        this.queryData = queryData;
        this.servicesID = servicesID;
    }


    /**
     * Gets the paymentAccount value for this Response.
     * 
     * @return paymentAccount
     */
    public com.paymentgateway.elementexpress.services.PaymentAccount getPaymentAccount() {
        return paymentAccount;
    }


    /**
     * Sets the paymentAccount value for this Response.
     * 
     * @param paymentAccount
     */
    public void setPaymentAccount(com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount) {
        this.paymentAccount = paymentAccount;
    }


    /**
     * Gets the scheduledTask value for this Response.
     * 
     * @return scheduledTask
     */
    public com.paymentgateway.elementexpress.services.ScheduledTask getScheduledTask() {
        return scheduledTask;
    }


    /**
     * Sets the scheduledTask value for this Response.
     * 
     * @param scheduledTask
     */
    public void setScheduledTask(com.paymentgateway.elementexpress.services.ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }


    /**
     * Gets the queryData value for this Response.
     * 
     * @return queryData
     */
    public java.lang.String getQueryData() {
        return queryData;
    }


    /**
     * Sets the queryData value for this Response.
     * 
     * @param queryData
     */
    public void setQueryData(java.lang.String queryData) {
        this.queryData = queryData;
    }


    /**
     * Gets the servicesID value for this Response.
     * 
     * @return servicesID
     */
    public java.lang.String getServicesID() {
        return servicesID;
    }


    /**
     * Sets the servicesID value for this Response.
     * 
     * @param servicesID
     */
    public void setServicesID(java.lang.String servicesID) {
        this.servicesID = servicesID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Response)) return false;
        Response other = (Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.paymentAccount==null && other.getPaymentAccount()==null) || 
             (this.paymentAccount!=null &&
              this.paymentAccount.equals(other.getPaymentAccount()))) &&
            ((this.scheduledTask==null && other.getScheduledTask()==null) || 
             (this.scheduledTask!=null &&
              this.scheduledTask.equals(other.getScheduledTask()))) &&
            ((this.queryData==null && other.getQueryData()==null) || 
             (this.queryData!=null &&
              this.queryData.equals(other.getQueryData()))) &&
            ((this.servicesID==null && other.getServicesID()==null) || 
             (this.servicesID!=null &&
              this.servicesID.equals(other.getServicesID())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getPaymentAccount() != null) {
            _hashCode += getPaymentAccount().hashCode();
        }
        if (getScheduledTask() != null) {
            _hashCode += getScheduledTask().hashCode();
        }
        if (getQueryData() != null) {
            _hashCode += getQueryData().hashCode();
        }
        if (getServicesID() != null) {
            _hashCode += getServicesID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentAccount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccount"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTask");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTask"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTask"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryData");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "QueryData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("servicesID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ServicesID"));
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
