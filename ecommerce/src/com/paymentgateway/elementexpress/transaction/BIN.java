/**
 * BIN.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class BIN  implements java.io.Serializable {
    private java.lang.String BINTypeCode;

    private java.lang.String BINTypeValue;

    private java.lang.String BINDecorator;

    public BIN() {
    }

    public BIN(
           java.lang.String BINTypeCode,
           java.lang.String BINTypeValue,
           java.lang.String BINDecorator) {
           this.BINTypeCode = BINTypeCode;
           this.BINTypeValue = BINTypeValue;
           this.BINDecorator = BINDecorator;
    }


    /**
     * Gets the BINTypeCode value for this BIN.
     * 
     * @return BINTypeCode
     */
    public java.lang.String getBINTypeCode() {
        return BINTypeCode;
    }


    /**
     * Sets the BINTypeCode value for this BIN.
     * 
     * @param BINTypeCode
     */
    public void setBINTypeCode(java.lang.String BINTypeCode) {
        this.BINTypeCode = BINTypeCode;
    }


    /**
     * Gets the BINTypeValue value for this BIN.
     * 
     * @return BINTypeValue
     */
    public java.lang.String getBINTypeValue() {
        return BINTypeValue;
    }


    /**
     * Sets the BINTypeValue value for this BIN.
     * 
     * @param BINTypeValue
     */
    public void setBINTypeValue(java.lang.String BINTypeValue) {
        this.BINTypeValue = BINTypeValue;
    }


    /**
     * Gets the BINDecorator value for this BIN.
     * 
     * @return BINDecorator
     */
    public java.lang.String getBINDecorator() {
        return BINDecorator;
    }


    /**
     * Sets the BINDecorator value for this BIN.
     * 
     * @param BINDecorator
     */
    public void setBINDecorator(java.lang.String BINDecorator) {
        this.BINDecorator = BINDecorator;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BIN)) return false;
        BIN other = (BIN) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.BINTypeCode==null && other.getBINTypeCode()==null) || 
             (this.BINTypeCode!=null &&
              this.BINTypeCode.equals(other.getBINTypeCode()))) &&
            ((this.BINTypeValue==null && other.getBINTypeValue()==null) || 
             (this.BINTypeValue!=null &&
              this.BINTypeValue.equals(other.getBINTypeValue()))) &&
            ((this.BINDecorator==null && other.getBINDecorator()==null) || 
             (this.BINDecorator!=null &&
              this.BINDecorator.equals(other.getBINDecorator())));
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
        if (getBINTypeCode() != null) {
            _hashCode += getBINTypeCode().hashCode();
        }
        if (getBINTypeValue() != null) {
            _hashCode += getBINTypeValue().hashCode();
        }
        if (getBINDecorator() != null) {
            _hashCode += getBINDecorator().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BIN.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BIN"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BINTypeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BINTypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BINTypeValue");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BINTypeValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BINDecorator");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BINDecorator"));
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
