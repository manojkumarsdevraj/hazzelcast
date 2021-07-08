/**
 * TransactionQuery.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.reporting;

public class TransactionQuery  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.reporting.Credentials credentials;

    private com.paymentgateway.elementexpress.reporting.Application application;

    private com.paymentgateway.elementexpress.reporting.Parameters parameters;

    private com.paymentgateway.elementexpress.reporting.ExtendedParameters[] extendedParameters;

    public TransactionQuery() {
    }

    public TransactionQuery(
           com.paymentgateway.elementexpress.reporting.Credentials credentials,
           com.paymentgateway.elementexpress.reporting.Application application,
           com.paymentgateway.elementexpress.reporting.Parameters parameters,
           com.paymentgateway.elementexpress.reporting.ExtendedParameters[] extendedParameters) {
           this.credentials = credentials;
           this.application = application;
           this.parameters = parameters;
           this.extendedParameters = extendedParameters;
    }


    /**
     * Gets the credentials value for this TransactionQuery.
     * 
     * @return credentials
     */
    public com.paymentgateway.elementexpress.reporting.Credentials getCredentials() {
        return credentials;
    }


    /**
     * Sets the credentials value for this TransactionQuery.
     * 
     * @param credentials
     */
    public void setCredentials(com.paymentgateway.elementexpress.reporting.Credentials credentials) {
        this.credentials = credentials;
    }


    /**
     * Gets the application value for this TransactionQuery.
     * 
     * @return application
     */
    public com.paymentgateway.elementexpress.reporting.Application getApplication() {
        return application;
    }


    /**
     * Sets the application value for this TransactionQuery.
     * 
     * @param application
     */
    public void setApplication(com.paymentgateway.elementexpress.reporting.Application application) {
        this.application = application;
    }


    /**
     * Gets the parameters value for this TransactionQuery.
     * 
     * @return parameters
     */
    public com.paymentgateway.elementexpress.reporting.Parameters getParameters() {
        return parameters;
    }


    /**
     * Sets the parameters value for this TransactionQuery.
     * 
     * @param parameters
     */
    public void setParameters(com.paymentgateway.elementexpress.reporting.Parameters parameters) {
        this.parameters = parameters;
    }


    /**
     * Gets the extendedParameters value for this TransactionQuery.
     * 
     * @return extendedParameters
     */
    public com.paymentgateway.elementexpress.reporting.ExtendedParameters[] getExtendedParameters() {
        return extendedParameters;
    }


    /**
     * Sets the extendedParameters value for this TransactionQuery.
     * 
     * @param extendedParameters
     */
    public void setExtendedParameters(com.paymentgateway.elementexpress.reporting.ExtendedParameters[] extendedParameters) {
        this.extendedParameters = extendedParameters;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransactionQuery)) return false;
        TransactionQuery other = (TransactionQuery) obj;
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
            ((this.parameters==null && other.getParameters()==null) || 
             (this.parameters!=null &&
              this.parameters.equals(other.getParameters()))) &&
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
        if (getParameters() != null) {
            _hashCode += getParameters().hashCode();
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
        new org.apache.axis.description.TypeDesc(TransactionQuery.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", ">TransactionQuery"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("credentials");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "credentials"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Credentials"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("application");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "application"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Application"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameters");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "parameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Parameters"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extendedParameters");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "extendedParameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExtendedParameters"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExtendedParameters"));
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
