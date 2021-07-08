/**
 * EncryptionFormat.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class EncryptionFormat implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected EncryptionFormat(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Default = "Default";
    public static final java.lang.String _Format1 = "Format1";
    public static final java.lang.String _Format2 = "Format2";
    public static final java.lang.String _Format3 = "Format3";
    public static final java.lang.String _Format4 = "Format4";
    public static final java.lang.String _Format5 = "Format5";
    public static final java.lang.String _Format6 = "Format6";
    public static final java.lang.String _Format7 = "Format7";
    public static final EncryptionFormat Default = new EncryptionFormat(_Default);
    public static final EncryptionFormat Format1 = new EncryptionFormat(_Format1);
    public static final EncryptionFormat Format2 = new EncryptionFormat(_Format2);
    public static final EncryptionFormat Format3 = new EncryptionFormat(_Format3);
    public static final EncryptionFormat Format4 = new EncryptionFormat(_Format4);
    public static final EncryptionFormat Format5 = new EncryptionFormat(_Format5);
    public static final EncryptionFormat Format6 = new EncryptionFormat(_Format6);
    public static final EncryptionFormat Format7 = new EncryptionFormat(_Format7);
    public java.lang.String getValue() { return _value_;}
    public static EncryptionFormat fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        EncryptionFormat enumeration = (EncryptionFormat)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static EncryptionFormat fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(EncryptionFormat.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "EncryptionFormat"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
