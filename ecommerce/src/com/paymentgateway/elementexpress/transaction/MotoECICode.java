/**
 * MotoECICode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class MotoECICode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected MotoECICode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _UseDefault = "UseDefault";
    public static final java.lang.String _NotUsed = "NotUsed";
    public static final java.lang.String _Single = "Single";
    public static final java.lang.String _Recurring = "Recurring";
    public static final java.lang.String _Installment = "Installment";
    public static final java.lang.String _SecureECommerce = "SecureECommerce";
    public static final java.lang.String _NonAuthenticatedSecureTransaction = "NonAuthenticatedSecureTransaction";
    public static final java.lang.String _NonAuthenticatedSecureECommerceTransaction = "NonAuthenticatedSecureECommerceTransaction";
    public static final java.lang.String _NonSecureECommerceTransaction = "NonSecureECommerceTransaction";
    public static final MotoECICode UseDefault = new MotoECICode(_UseDefault);
    public static final MotoECICode NotUsed = new MotoECICode(_NotUsed);
    public static final MotoECICode Single = new MotoECICode(_Single);
    public static final MotoECICode Recurring = new MotoECICode(_Recurring);
    public static final MotoECICode Installment = new MotoECICode(_Installment);
    public static final MotoECICode SecureECommerce = new MotoECICode(_SecureECommerce);
    public static final MotoECICode NonAuthenticatedSecureTransaction = new MotoECICode(_NonAuthenticatedSecureTransaction);
    public static final MotoECICode NonAuthenticatedSecureECommerceTransaction = new MotoECICode(_NonAuthenticatedSecureECommerceTransaction);
    public static final MotoECICode NonSecureECommerceTransaction = new MotoECICode(_NonSecureECommerceTransaction);
    public java.lang.String getValue() { return _value_;}
    public static MotoECICode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        MotoECICode enumeration = (MotoECICode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static MotoECICode fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MotoECICode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "MotoECICode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
