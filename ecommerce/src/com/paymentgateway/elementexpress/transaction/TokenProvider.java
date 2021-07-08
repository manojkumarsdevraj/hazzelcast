/**
 * TokenProvider.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class TokenProvider implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TokenProvider(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Null = "Null";
    public static final java.lang.String _ExpressPASS = "ExpressPASS";
    public static final java.lang.String _OmniToken = "OmniToken";
    public static final java.lang.String _Paymetric = "Paymetric";
    public static final java.lang.String _TransArmor = "TransArmor";
    public static final TokenProvider Null = new TokenProvider(_Null);
    public static final TokenProvider ExpressPASS = new TokenProvider(_ExpressPASS);
    public static final TokenProvider OmniToken = new TokenProvider(_OmniToken);
    public static final TokenProvider Paymetric = new TokenProvider(_Paymetric);
    public static final TokenProvider TransArmor = new TokenProvider(_TransArmor);
    public java.lang.String getValue() { return _value_;}
    public static TokenProvider fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TokenProvider enumeration = (TokenProvider)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TokenProvider fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TokenProvider.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TokenProvider"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}