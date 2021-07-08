/**
 * ExpressResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public abstract class ExpressResponse  implements java.io.Serializable {
    private java.lang.String expressResponseCode;

    private java.lang.String expressResponseMessage;

    private java.lang.String expressTransactionDate;

    private java.lang.String expressTransactionTime;

    private java.lang.String expressTransactionTimezone;

    private com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters;

    public ExpressResponse() {
    }

    public ExpressResponse(
           java.lang.String expressResponseCode,
           java.lang.String expressResponseMessage,
           java.lang.String expressTransactionDate,
           java.lang.String expressTransactionTime,
           java.lang.String expressTransactionTimezone,
           com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) {
           this.expressResponseCode = expressResponseCode;
           this.expressResponseMessage = expressResponseMessage;
           this.expressTransactionDate = expressTransactionDate;
           this.expressTransactionTime = expressTransactionTime;
           this.expressTransactionTimezone = expressTransactionTimezone;
           this.extendedParameters = extendedParameters;
    }


    /**
     * Gets the expressResponseCode value for this ExpressResponse.
     * 
     * @return expressResponseCode
     */
    public java.lang.String getExpressResponseCode() {
        return expressResponseCode;
    }


    /**
     * Sets the expressResponseCode value for this ExpressResponse.
     * 
     * @param expressResponseCode
     */
    public void setExpressResponseCode(java.lang.String expressResponseCode) {
        this.expressResponseCode = expressResponseCode;
    }


    /**
     * Gets the expressResponseMessage value for this ExpressResponse.
     * 
     * @return expressResponseMessage
     */
    public java.lang.String getExpressResponseMessage() {
        return expressResponseMessage;
    }


    /**
     * Sets the expressResponseMessage value for this ExpressResponse.
     * 
     * @param expressResponseMessage
     */
    public void setExpressResponseMessage(java.lang.String expressResponseMessage) {
        this.expressResponseMessage = expressResponseMessage;
    }


    /**
     * Gets the expressTransactionDate value for this ExpressResponse.
     * 
     * @return expressTransactionDate
     */
    public java.lang.String getExpressTransactionDate() {
        return expressTransactionDate;
    }


    /**
     * Sets the expressTransactionDate value for this ExpressResponse.
     * 
     * @param expressTransactionDate
     */
    public void setExpressTransactionDate(java.lang.String expressTransactionDate) {
        this.expressTransactionDate = expressTransactionDate;
    }


    /**
     * Gets the expressTransactionTime value for this ExpressResponse.
     * 
     * @return expressTransactionTime
     */
    public java.lang.String getExpressTransactionTime() {
        return expressTransactionTime;
    }


    /**
     * Sets the expressTransactionTime value for this ExpressResponse.
     * 
     * @param expressTransactionTime
     */
    public void setExpressTransactionTime(java.lang.String expressTransactionTime) {
        this.expressTransactionTime = expressTransactionTime;
    }


    /**
     * Gets the expressTransactionTimezone value for this ExpressResponse.
     * 
     * @return expressTransactionTimezone
     */
    public java.lang.String getExpressTransactionTimezone() {
        return expressTransactionTimezone;
    }


    /**
     * Sets the expressTransactionTimezone value for this ExpressResponse.
     * 
     * @param expressTransactionTimezone
     */
    public void setExpressTransactionTimezone(java.lang.String expressTransactionTimezone) {
        this.expressTransactionTimezone = expressTransactionTimezone;
    }


    /**
     * Gets the extendedParameters value for this ExpressResponse.
     * 
     * @return extendedParameters
     */
    public com.paymentgateway.elementexpress.transaction.ExtendedParameters[] getExtendedParameters() {
        return extendedParameters;
    }


    /**
     * Sets the extendedParameters value for this ExpressResponse.
     * 
     * @param extendedParameters
     */
    public void setExtendedParameters(com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) {
        this.extendedParameters = extendedParameters;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExpressResponse)) return false;
        ExpressResponse other = (ExpressResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.expressResponseCode==null && other.getExpressResponseCode()==null) || 
             (this.expressResponseCode!=null &&
              this.expressResponseCode.equals(other.getExpressResponseCode()))) &&
            ((this.expressResponseMessage==null && other.getExpressResponseMessage()==null) || 
             (this.expressResponseMessage!=null &&
              this.expressResponseMessage.equals(other.getExpressResponseMessage()))) &&
            ((this.expressTransactionDate==null && other.getExpressTransactionDate()==null) || 
             (this.expressTransactionDate!=null &&
              this.expressTransactionDate.equals(other.getExpressTransactionDate()))) &&
            ((this.expressTransactionTime==null && other.getExpressTransactionTime()==null) || 
             (this.expressTransactionTime!=null &&
              this.expressTransactionTime.equals(other.getExpressTransactionTime()))) &&
            ((this.expressTransactionTimezone==null && other.getExpressTransactionTimezone()==null) || 
             (this.expressTransactionTimezone!=null &&
              this.expressTransactionTimezone.equals(other.getExpressTransactionTimezone()))) &&
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
        if (getExpressResponseCode() != null) {
            _hashCode += getExpressResponseCode().hashCode();
        }
        if (getExpressResponseMessage() != null) {
            _hashCode += getExpressResponseMessage().hashCode();
        }
        if (getExpressTransactionDate() != null) {
            _hashCode += getExpressTransactionDate().hashCode();
        }
        if (getExpressTransactionTime() != null) {
            _hashCode += getExpressTransactionTime().hashCode();
        }
        if (getExpressTransactionTimezone() != null) {
            _hashCode += getExpressTransactionTimezone().hashCode();
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
        new org.apache.axis.description.TypeDesc(ExpressResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExpressResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressResponseCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExpressResponseCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressResponseMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExpressResponseMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressTransactionDate");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExpressTransactionDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressTransactionTime");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExpressTransactionTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressTransactionTimezone");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExpressTransactionTimezone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extendedParameters");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
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
