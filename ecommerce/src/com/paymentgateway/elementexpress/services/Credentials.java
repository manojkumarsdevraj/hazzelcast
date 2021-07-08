/**
 * Credentials.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class Credentials  implements java.io.Serializable {
    private java.lang.String accountID;

    private java.lang.String accountToken;

    private java.lang.String acceptorID;

    private java.lang.String newAccountToken;

    public Credentials() {
    }

    public Credentials(
           java.lang.String accountID,
           java.lang.String accountToken,
           java.lang.String acceptorID,
           java.lang.String newAccountToken) {
           this.accountID = accountID;
           this.accountToken = accountToken;
           this.acceptorID = acceptorID;
           this.newAccountToken = newAccountToken;
    }


    /**
     * Gets the accountID value for this Credentials.
     * 
     * @return accountID
     */
    public java.lang.String getAccountID() {
        return accountID;
    }


    /**
     * Sets the accountID value for this Credentials.
     * 
     * @param accountID
     */
    public void setAccountID(java.lang.String accountID) {
        this.accountID = accountID;
    }


    /**
     * Gets the accountToken value for this Credentials.
     * 
     * @return accountToken
     */
    public java.lang.String getAccountToken() {
        return accountToken;
    }


    /**
     * Sets the accountToken value for this Credentials.
     * 
     * @param accountToken
     */
    public void setAccountToken(java.lang.String accountToken) {
        this.accountToken = accountToken;
    }


    /**
     * Gets the acceptorID value for this Credentials.
     * 
     * @return acceptorID
     */
    public java.lang.String getAcceptorID() {
        return acceptorID;
    }


    /**
     * Sets the acceptorID value for this Credentials.
     * 
     * @param acceptorID
     */
    public void setAcceptorID(java.lang.String acceptorID) {
        this.acceptorID = acceptorID;
    }


    /**
     * Gets the newAccountToken value for this Credentials.
     * 
     * @return newAccountToken
     */
    public java.lang.String getNewAccountToken() {
        return newAccountToken;
    }


    /**
     * Sets the newAccountToken value for this Credentials.
     * 
     * @param newAccountToken
     */
    public void setNewAccountToken(java.lang.String newAccountToken) {
        this.newAccountToken = newAccountToken;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Credentials)) return false;
        Credentials other = (Credentials) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.accountID==null && other.getAccountID()==null) || 
             (this.accountID!=null &&
              this.accountID.equals(other.getAccountID()))) &&
            ((this.accountToken==null && other.getAccountToken()==null) || 
             (this.accountToken!=null &&
              this.accountToken.equals(other.getAccountToken()))) &&
            ((this.acceptorID==null && other.getAcceptorID()==null) || 
             (this.acceptorID!=null &&
              this.acceptorID.equals(other.getAcceptorID()))) &&
            ((this.newAccountToken==null && other.getNewAccountToken()==null) || 
             (this.newAccountToken!=null &&
              this.newAccountToken.equals(other.getNewAccountToken())));
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
        if (getAccountID() != null) {
            _hashCode += getAccountID().hashCode();
        }
        if (getAccountToken() != null) {
            _hashCode += getAccountToken().hashCode();
        }
        if (getAcceptorID() != null) {
            _hashCode += getAcceptorID().hashCode();
        }
        if (getNewAccountToken() != null) {
            _hashCode += getNewAccountToken().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Credentials.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "AccountID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountToken");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "AccountToken"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acceptorID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "AcceptorID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newAccountToken");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "NewAccountToken"));
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
