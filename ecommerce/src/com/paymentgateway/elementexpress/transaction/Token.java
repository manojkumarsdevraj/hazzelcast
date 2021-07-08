/**
 * Token.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class Token  implements java.io.Serializable {
    private java.lang.String tokenID;

    private com.paymentgateway.elementexpress.transaction.TokenProvider tokenProvider;

    private java.lang.String vaultID;

    private java.lang.String tokenOptions;

    private java.lang.String TAProviderID;

    public Token() {
    }

    public Token(
           java.lang.String tokenID,
           com.paymentgateway.elementexpress.transaction.TokenProvider tokenProvider,
           java.lang.String vaultID,
           java.lang.String tokenOptions,
           java.lang.String TAProviderID) {
           this.tokenID = tokenID;
           this.tokenProvider = tokenProvider;
           this.vaultID = vaultID;
           this.tokenOptions = tokenOptions;
           this.TAProviderID = TAProviderID;
    }


    /**
     * Gets the tokenID value for this Token.
     * 
     * @return tokenID
     */
    public java.lang.String getTokenID() {
        return tokenID;
    }


    /**
     * Sets the tokenID value for this Token.
     * 
     * @param tokenID
     */
    public void setTokenID(java.lang.String tokenID) {
        this.tokenID = tokenID;
    }


    /**
     * Gets the tokenProvider value for this Token.
     * 
     * @return tokenProvider
     */
    public com.paymentgateway.elementexpress.transaction.TokenProvider getTokenProvider() {
        return tokenProvider;
    }


    /**
     * Sets the tokenProvider value for this Token.
     * 
     * @param tokenProvider
     */
    public void setTokenProvider(com.paymentgateway.elementexpress.transaction.TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    /**
     * Gets the vaultID value for this Token.
     * 
     * @return vaultID
     */
    public java.lang.String getVaultID() {
        return vaultID;
    }


    /**
     * Sets the vaultID value for this Token.
     * 
     * @param vaultID
     */
    public void setVaultID(java.lang.String vaultID) {
        this.vaultID = vaultID;
    }


    /**
     * Gets the tokenOptions value for this Token.
     * 
     * @return tokenOptions
     */
    public java.lang.String getTokenOptions() {
        return tokenOptions;
    }


    /**
     * Sets the tokenOptions value for this Token.
     * 
     * @param tokenOptions
     */
    public void setTokenOptions(java.lang.String tokenOptions) {
        this.tokenOptions = tokenOptions;
    }


    /**
     * Gets the TAProviderID value for this Token.
     * 
     * @return TAProviderID
     */
    public java.lang.String getTAProviderID() {
        return TAProviderID;
    }


    /**
     * Sets the TAProviderID value for this Token.
     * 
     * @param TAProviderID
     */
    public void setTAProviderID(java.lang.String TAProviderID) {
        this.TAProviderID = TAProviderID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Token)) return false;
        Token other = (Token) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tokenID==null && other.getTokenID()==null) || 
             (this.tokenID!=null &&
              this.tokenID.equals(other.getTokenID()))) &&
            ((this.tokenProvider==null && other.getTokenProvider()==null) || 
             (this.tokenProvider!=null &&
              this.tokenProvider.equals(other.getTokenProvider()))) &&
            ((this.vaultID==null && other.getVaultID()==null) || 
             (this.vaultID!=null &&
              this.vaultID.equals(other.getVaultID()))) &&
            ((this.tokenOptions==null && other.getTokenOptions()==null) || 
             (this.tokenOptions!=null &&
              this.tokenOptions.equals(other.getTokenOptions()))) &&
            ((this.TAProviderID==null && other.getTAProviderID()==null) || 
             (this.TAProviderID!=null &&
              this.TAProviderID.equals(other.getTAProviderID())));
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
        if (getTokenID() != null) {
            _hashCode += getTokenID().hashCode();
        }
        if (getTokenProvider() != null) {
            _hashCode += getTokenProvider().hashCode();
        }
        if (getVaultID() != null) {
            _hashCode += getVaultID().hashCode();
        }
        if (getTokenOptions() != null) {
            _hashCode += getTokenOptions().hashCode();
        }
        if (getTAProviderID() != null) {
            _hashCode += getTAProviderID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Token.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Token"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TokenID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenProvider");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TokenProvider"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TokenProvider"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vaultID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "VaultID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenOptions");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TokenOptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TAProviderID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TAProviderID"));
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
