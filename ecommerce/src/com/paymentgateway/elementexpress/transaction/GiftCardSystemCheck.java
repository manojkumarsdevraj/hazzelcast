/**
 * GiftCardSystemCheck.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class GiftCardSystemCheck  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.transaction.Credentials credentials;

    private com.paymentgateway.elementexpress.transaction.Application application;

    public GiftCardSystemCheck() {
    }

    public GiftCardSystemCheck(
           com.paymentgateway.elementexpress.transaction.Credentials credentials,
           com.paymentgateway.elementexpress.transaction.Application application) {
           this.credentials = credentials;
           this.application = application;
    }


    /**
     * Gets the credentials value for this GiftCardSystemCheck.
     * 
     * @return credentials
     */
    public com.paymentgateway.elementexpress.transaction.Credentials getCredentials() {
        return credentials;
    }


    /**
     * Sets the credentials value for this GiftCardSystemCheck.
     * 
     * @param credentials
     */
    public void setCredentials(com.paymentgateway.elementexpress.transaction.Credentials credentials) {
        this.credentials = credentials;
    }


    /**
     * Gets the application value for this GiftCardSystemCheck.
     * 
     * @return application
     */
    public com.paymentgateway.elementexpress.transaction.Application getApplication() {
        return application;
    }


    /**
     * Sets the application value for this GiftCardSystemCheck.
     * 
     * @param application
     */
    public void setApplication(com.paymentgateway.elementexpress.transaction.Application application) {
        this.application = application;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GiftCardSystemCheck)) return false;
        GiftCardSystemCheck other = (GiftCardSystemCheck) obj;
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
              this.application.equals(other.getApplication())));
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GiftCardSystemCheck.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardSystemCheck"));
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
