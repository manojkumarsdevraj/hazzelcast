/**
 * TokenCreateResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class TokenCreateResponse  extends com.paymentgateway.elementexpress.services.ExpressResponse  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.services.Card card;

    private com.paymentgateway.elementexpress.services.Token token;

    private java.lang.String servicesID;

    public TokenCreateResponse() {
    }

    public TokenCreateResponse(
           java.lang.String expressResponseCode,
           java.lang.String expressResponseMessage,
           java.lang.String expressTransactionDate,
           java.lang.String expressTransactionTime,
           java.lang.String expressTransactionTimezone,
           com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters,
           com.paymentgateway.elementexpress.services.Card card,
           com.paymentgateway.elementexpress.services.Token token,
           java.lang.String servicesID) {
        super(
            expressResponseCode,
            expressResponseMessage,
            expressTransactionDate,
            expressTransactionTime,
            expressTransactionTimezone,
            extendedParameters);
        this.card = card;
        this.token = token;
        this.servicesID = servicesID;
    }


    /**
     * Gets the card value for this TokenCreateResponse.
     * 
     * @return card
     */
    public com.paymentgateway.elementexpress.services.Card getCard() {
        return card;
    }


    /**
     * Sets the card value for this TokenCreateResponse.
     * 
     * @param card
     */
    public void setCard(com.paymentgateway.elementexpress.services.Card card) {
        this.card = card;
    }


    /**
     * Gets the token value for this TokenCreateResponse.
     * 
     * @return token
     */
    public com.paymentgateway.elementexpress.services.Token getToken() {
        return token;
    }


    /**
     * Sets the token value for this TokenCreateResponse.
     * 
     * @param token
     */
    public void setToken(com.paymentgateway.elementexpress.services.Token token) {
        this.token = token;
    }


    /**
     * Gets the servicesID value for this TokenCreateResponse.
     * 
     * @return servicesID
     */
    public java.lang.String getServicesID() {
        return servicesID;
    }


    /**
     * Sets the servicesID value for this TokenCreateResponse.
     * 
     * @param servicesID
     */
    public void setServicesID(java.lang.String servicesID) {
        this.servicesID = servicesID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TokenCreateResponse)) return false;
        TokenCreateResponse other = (TokenCreateResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.card==null && other.getCard()==null) || 
             (this.card!=null &&
              this.card.equals(other.getCard()))) &&
            ((this.token==null && other.getToken()==null) || 
             (this.token!=null &&
              this.token.equals(other.getToken()))) &&
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
        if (getCard() != null) {
            _hashCode += getCard().hashCode();
        }
        if (getToken() != null) {
            _hashCode += getToken().hashCode();
        }
        if (getServicesID() != null) {
            _hashCode += getServicesID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TokenCreateResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "TokenCreateResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("card");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "Card"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Card"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("token");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "Token"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Token"));
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
