/**
 * TokenCreateWithTransID.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class TokenCreateWithTransID  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.services.Credentials credentials;

    private com.paymentgateway.elementexpress.services.Application application;

    private com.paymentgateway.elementexpress.services.Terminal terminal;

    private com.paymentgateway.elementexpress.services.Transaction transaction;

    private com.paymentgateway.elementexpress.services.Token token;

    private com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters;

    public TokenCreateWithTransID() {
    }

    public TokenCreateWithTransID(
           com.paymentgateway.elementexpress.services.Credentials credentials,
           com.paymentgateway.elementexpress.services.Application application,
           com.paymentgateway.elementexpress.services.Terminal terminal,
           com.paymentgateway.elementexpress.services.Transaction transaction,
           com.paymentgateway.elementexpress.services.Token token,
           com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) {
           this.credentials = credentials;
           this.application = application;
           this.terminal = terminal;
           this.transaction = transaction;
           this.token = token;
           this.extendedParameters = extendedParameters;
    }


    /**
     * Gets the credentials value for this TokenCreateWithTransID.
     * 
     * @return credentials
     */
    public com.paymentgateway.elementexpress.services.Credentials getCredentials() {
        return credentials;
    }


    /**
     * Sets the credentials value for this TokenCreateWithTransID.
     * 
     * @param credentials
     */
    public void setCredentials(com.paymentgateway.elementexpress.services.Credentials credentials) {
        this.credentials = credentials;
    }


    /**
     * Gets the application value for this TokenCreateWithTransID.
     * 
     * @return application
     */
    public com.paymentgateway.elementexpress.services.Application getApplication() {
        return application;
    }


    /**
     * Sets the application value for this TokenCreateWithTransID.
     * 
     * @param application
     */
    public void setApplication(com.paymentgateway.elementexpress.services.Application application) {
        this.application = application;
    }


    /**
     * Gets the terminal value for this TokenCreateWithTransID.
     * 
     * @return terminal
     */
    public com.paymentgateway.elementexpress.services.Terminal getTerminal() {
        return terminal;
    }


    /**
     * Sets the terminal value for this TokenCreateWithTransID.
     * 
     * @param terminal
     */
    public void setTerminal(com.paymentgateway.elementexpress.services.Terminal terminal) {
        this.terminal = terminal;
    }


    /**
     * Gets the transaction value for this TokenCreateWithTransID.
     * 
     * @return transaction
     */
    public com.paymentgateway.elementexpress.services.Transaction getTransaction() {
        return transaction;
    }


    /**
     * Sets the transaction value for this TokenCreateWithTransID.
     * 
     * @param transaction
     */
    public void setTransaction(com.paymentgateway.elementexpress.services.Transaction transaction) {
        this.transaction = transaction;
    }


    /**
     * Gets the token value for this TokenCreateWithTransID.
     * 
     * @return token
     */
    public com.paymentgateway.elementexpress.services.Token getToken() {
        return token;
    }


    /**
     * Sets the token value for this TokenCreateWithTransID.
     * 
     * @param token
     */
    public void setToken(com.paymentgateway.elementexpress.services.Token token) {
        this.token = token;
    }


    /**
     * Gets the extendedParameters value for this TokenCreateWithTransID.
     * 
     * @return extendedParameters
     */
    public com.paymentgateway.elementexpress.services.ExtendedParameters[] getExtendedParameters() {
        return extendedParameters;
    }


    /**
     * Sets the extendedParameters value for this TokenCreateWithTransID.
     * 
     * @param extendedParameters
     */
    public void setExtendedParameters(com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) {
        this.extendedParameters = extendedParameters;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TokenCreateWithTransID)) return false;
        TokenCreateWithTransID other = (TokenCreateWithTransID) obj;
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
            ((this.terminal==null && other.getTerminal()==null) || 
             (this.terminal!=null &&
              this.terminal.equals(other.getTerminal()))) &&
            ((this.transaction==null && other.getTransaction()==null) || 
             (this.transaction!=null &&
              this.transaction.equals(other.getTransaction()))) &&
            ((this.token==null && other.getToken()==null) || 
             (this.token!=null &&
              this.token.equals(other.getToken()))) &&
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
        if (getTerminal() != null) {
            _hashCode += getTerminal().hashCode();
        }
        if (getTransaction() != null) {
            _hashCode += getTransaction().hashCode();
        }
        if (getToken() != null) {
            _hashCode += getToken().hashCode();
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
        new org.apache.axis.description.TypeDesc(TokenCreateWithTransID.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", ">TokenCreateWithTransID"));
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
        elemField.setFieldName("terminal");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "terminal"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Terminal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transaction");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "transaction"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Transaction"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("token");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "token"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Token"));
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
