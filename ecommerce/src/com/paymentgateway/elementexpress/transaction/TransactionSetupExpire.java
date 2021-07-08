/**
 * TransactionSetupExpire.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class TransactionSetupExpire  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.transaction.Credentials credentials;

    private com.paymentgateway.elementexpress.transaction.Application application;

    private com.paymentgateway.elementexpress.transaction.TransactionSetup transactionSetup;

    private com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters;

    public TransactionSetupExpire() {
    }

    public TransactionSetupExpire(
           com.paymentgateway.elementexpress.transaction.Credentials credentials,
           com.paymentgateway.elementexpress.transaction.Application application,
           com.paymentgateway.elementexpress.transaction.TransactionSetup transactionSetup,
           com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) {
           this.credentials = credentials;
           this.application = application;
           this.transactionSetup = transactionSetup;
           this.extendedParameters = extendedParameters;
    }


    /**
     * Gets the credentials value for this TransactionSetupExpire.
     * 
     * @return credentials
     */
    public com.paymentgateway.elementexpress.transaction.Credentials getCredentials() {
        return credentials;
    }


    /**
     * Sets the credentials value for this TransactionSetupExpire.
     * 
     * @param credentials
     */
    public void setCredentials(com.paymentgateway.elementexpress.transaction.Credentials credentials) {
        this.credentials = credentials;
    }


    /**
     * Gets the application value for this TransactionSetupExpire.
     * 
     * @return application
     */
    public com.paymentgateway.elementexpress.transaction.Application getApplication() {
        return application;
    }


    /**
     * Sets the application value for this TransactionSetupExpire.
     * 
     * @param application
     */
    public void setApplication(com.paymentgateway.elementexpress.transaction.Application application) {
        this.application = application;
    }


    /**
     * Gets the transactionSetup value for this TransactionSetupExpire.
     * 
     * @return transactionSetup
     */
    public com.paymentgateway.elementexpress.transaction.TransactionSetup getTransactionSetup() {
        return transactionSetup;
    }


    /**
     * Sets the transactionSetup value for this TransactionSetupExpire.
     * 
     * @param transactionSetup
     */
    public void setTransactionSetup(com.paymentgateway.elementexpress.transaction.TransactionSetup transactionSetup) {
        this.transactionSetup = transactionSetup;
    }


    /**
     * Gets the extendedParameters value for this TransactionSetupExpire.
     * 
     * @return extendedParameters
     */
    public com.paymentgateway.elementexpress.transaction.ExtendedParameters[] getExtendedParameters() {
        return extendedParameters;
    }


    /**
     * Sets the extendedParameters value for this TransactionSetupExpire.
     * 
     * @param extendedParameters
     */
    public void setExtendedParameters(com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) {
        this.extendedParameters = extendedParameters;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransactionSetupExpire)) return false;
        TransactionSetupExpire other = (TransactionSetupExpire) obj;
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
            ((this.transactionSetup==null && other.getTransactionSetup()==null) || 
             (this.transactionSetup!=null &&
              this.transactionSetup.equals(other.getTransactionSetup()))) &&
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
        if (getTransactionSetup() != null) {
            _hashCode += getTransactionSetup().hashCode();
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
        new org.apache.axis.description.TypeDesc(TransactionSetupExpire.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">TransactionSetupExpire"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("credentials");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("application");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetup");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transactionSetup"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetup"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extendedParameters");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
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
