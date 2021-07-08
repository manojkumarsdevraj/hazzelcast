/**
 * PaymentAccountCreateWithTransID.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class PaymentAccountCreateWithTransID  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.services.Credentials credentials;

    private com.paymentgateway.elementexpress.services.Application application;

    private com.paymentgateway.elementexpress.services.Transaction transaction;

    private com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount;

    private com.paymentgateway.elementexpress.services.Address address;

    private com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters;

    public PaymentAccountCreateWithTransID() {
    }

    public PaymentAccountCreateWithTransID(
           com.paymentgateway.elementexpress.services.Credentials credentials,
           com.paymentgateway.elementexpress.services.Application application,
           com.paymentgateway.elementexpress.services.Transaction transaction,
           com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount,
           com.paymentgateway.elementexpress.services.Address address,
           com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) {
           this.credentials = credentials;
           this.application = application;
           this.transaction = transaction;
           this.paymentAccount = paymentAccount;
           this.address = address;
           this.extendedParameters = extendedParameters;
    }


    /**
     * Gets the credentials value for this PaymentAccountCreateWithTransID.
     * 
     * @return credentials
     */
    public com.paymentgateway.elementexpress.services.Credentials getCredentials() {
        return credentials;
    }


    /**
     * Sets the credentials value for this PaymentAccountCreateWithTransID.
     * 
     * @param credentials
     */
    public void setCredentials(com.paymentgateway.elementexpress.services.Credentials credentials) {
        this.credentials = credentials;
    }


    /**
     * Gets the application value for this PaymentAccountCreateWithTransID.
     * 
     * @return application
     */
    public com.paymentgateway.elementexpress.services.Application getApplication() {
        return application;
    }


    /**
     * Sets the application value for this PaymentAccountCreateWithTransID.
     * 
     * @param application
     */
    public void setApplication(com.paymentgateway.elementexpress.services.Application application) {
        this.application = application;
    }


    /**
     * Gets the transaction value for this PaymentAccountCreateWithTransID.
     * 
     * @return transaction
     */
    public com.paymentgateway.elementexpress.services.Transaction getTransaction() {
        return transaction;
    }


    /**
     * Sets the transaction value for this PaymentAccountCreateWithTransID.
     * 
     * @param transaction
     */
    public void setTransaction(com.paymentgateway.elementexpress.services.Transaction transaction) {
        this.transaction = transaction;
    }


    /**
     * Gets the paymentAccount value for this PaymentAccountCreateWithTransID.
     * 
     * @return paymentAccount
     */
    public com.paymentgateway.elementexpress.services.PaymentAccount getPaymentAccount() {
        return paymentAccount;
    }


    /**
     * Sets the paymentAccount value for this PaymentAccountCreateWithTransID.
     * 
     * @param paymentAccount
     */
    public void setPaymentAccount(com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount) {
        this.paymentAccount = paymentAccount;
    }


    /**
     * Gets the address value for this PaymentAccountCreateWithTransID.
     * 
     * @return address
     */
    public com.paymentgateway.elementexpress.services.Address getAddress() {
        return address;
    }


    /**
     * Sets the address value for this PaymentAccountCreateWithTransID.
     * 
     * @param address
     */
    public void setAddress(com.paymentgateway.elementexpress.services.Address address) {
        this.address = address;
    }


    /**
     * Gets the extendedParameters value for this PaymentAccountCreateWithTransID.
     * 
     * @return extendedParameters
     */
    public com.paymentgateway.elementexpress.services.ExtendedParameters[] getExtendedParameters() {
        return extendedParameters;
    }


    /**
     * Sets the extendedParameters value for this PaymentAccountCreateWithTransID.
     * 
     * @param extendedParameters
     */
    public void setExtendedParameters(com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) {
        this.extendedParameters = extendedParameters;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PaymentAccountCreateWithTransID)) return false;
        PaymentAccountCreateWithTransID other = (PaymentAccountCreateWithTransID) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.credentials==null && other.getCredentials()==null) || 
             (this.credentials!=null &&
              this.credentials.equals(other.getCredentials()))) &&
            ((this.application==null && other.getApplication()==null) || 
             (this.application!=null &&
              this.application.equals(other.getApplication()))) &&
            ((this.transaction==null && other.getTransaction()==null) || 
             (this.transaction!=null &&
              this.transaction.equals(other.getTransaction()))) &&
            ((this.paymentAccount==null && other.getPaymentAccount()==null) || 
             (this.paymentAccount!=null &&
              this.paymentAccount.equals(other.getPaymentAccount()))) &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            ((this.extendedParameters==null && other.getExtendedParameters()==null) || 
             (this.extendedParameters!=null &&
              java.util.Arrays.equals(this.extendedParameters, other.getExtendedParameters())));
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
        if (getCredentials() != null) {
            _hashCode += getCredentials().hashCode();
        }
        if (getApplication() != null) {
            _hashCode += getApplication().hashCode();
        }
        if (getTransaction() != null) {
            _hashCode += getTransaction().hashCode();
        }
        if (getPaymentAccount() != null) {
            _hashCode += getPaymentAccount().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getExtendedParameters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExtendedParameters());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExtendedParameters(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PaymentAccountCreateWithTransID.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountCreateWithTransID"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("credentials");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("application");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transaction");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "transaction"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Transaction"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentAccount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "paymentAccount"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "address"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Address"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extendedParameters");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
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
