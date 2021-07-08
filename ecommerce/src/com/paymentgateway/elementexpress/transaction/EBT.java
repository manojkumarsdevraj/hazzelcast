/**
 * EBT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class EBT  implements java.io.Serializable {
    private java.lang.String EBTTypeIndex;

    private java.lang.String voucherNumber;

    public EBT() {
    }

    public EBT(
           java.lang.String EBTTypeIndex,
           java.lang.String voucherNumber) {
           this.EBTTypeIndex = EBTTypeIndex;
           this.voucherNumber = voucherNumber;
    }


    /**
     * Gets the EBTTypeIndex value for this EBT.
     * 
     * @return EBTTypeIndex
     */
    public java.lang.String getEBTTypeIndex() {
        return EBTTypeIndex;
    }


    /**
     * Sets the EBTTypeIndex value for this EBT.
     * 
     * @param EBTTypeIndex
     */
    public void setEBTTypeIndex(java.lang.String EBTTypeIndex) {
        this.EBTTypeIndex = EBTTypeIndex;
    }


    /**
     * Gets the voucherNumber value for this EBT.
     * 
     * @return voucherNumber
     */
    public java.lang.String getVoucherNumber() {
        return voucherNumber;
    }


    /**
     * Sets the voucherNumber value for this EBT.
     * 
     * @param voucherNumber
     */
    public void setVoucherNumber(java.lang.String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EBT)) return false;
        EBT other = (EBT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.EBTTypeIndex==null && other.getEBTTypeIndex()==null) || 
             (this.EBTTypeIndex!=null &&
              this.EBTTypeIndex.equals(other.getEBTTypeIndex()))) &&
            ((this.voucherNumber==null && other.getVoucherNumber()==null) || 
             (this.voucherNumber!=null &&
              this.voucherNumber.equals(other.getVoucherNumber())));
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
        if (getEBTTypeIndex() != null) {
            _hashCode += getEBTTypeIndex().hashCode();
        }
        if (getVoucherNumber() != null) {
            _hashCode += getVoucherNumber().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EBT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EBTTypeIndex");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBTTypeIndex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("voucherNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "VoucherNumber"));
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
