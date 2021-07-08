/**
 * CardholderPresentCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class CardholderPresentCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected CardholderPresentCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _UseDefault = "UseDefault";
    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _Present = "Present";
    public static final java.lang.String _NotPresent = "NotPresent";
    public static final java.lang.String _MailOrder = "MailOrder";
    public static final java.lang.String _PhoneOrder = "PhoneOrder";
    public static final java.lang.String _StandingAuth = "StandingAuth";
    public static final java.lang.String _ECommerce = "ECommerce";
    public static final CardholderPresentCode UseDefault = new CardholderPresentCode(_UseDefault);
    public static final CardholderPresentCode Unknown = new CardholderPresentCode(_Unknown);
    public static final CardholderPresentCode Present = new CardholderPresentCode(_Present);
    public static final CardholderPresentCode NotPresent = new CardholderPresentCode(_NotPresent);
    public static final CardholderPresentCode MailOrder = new CardholderPresentCode(_MailOrder);
    public static final CardholderPresentCode PhoneOrder = new CardholderPresentCode(_PhoneOrder);
    public static final CardholderPresentCode StandingAuth = new CardholderPresentCode(_StandingAuth);
    public static final CardholderPresentCode ECommerce = new CardholderPresentCode(_ECommerce);
    public java.lang.String getValue() { return _value_;}
    public static CardholderPresentCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        CardholderPresentCode enumeration = (CardholderPresentCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static CardholderPresentCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(CardholderPresentCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "CardholderPresentCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
