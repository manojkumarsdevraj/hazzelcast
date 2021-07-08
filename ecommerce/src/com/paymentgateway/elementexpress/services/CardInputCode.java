/**
 * CardInputCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class CardInputCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected CardInputCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _UseDefault = "UseDefault";
    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _MagstripeRead = "MagstripeRead";
    public static final java.lang.String _ContactlessMagstripeRead = "ContactlessMagstripeRead";
    public static final java.lang.String _ManualKeyed = "ManualKeyed";
    public static final java.lang.String _ManualKeyedMagstripeFailure = "ManualKeyedMagstripeFailure";
    public static final java.lang.String _ChipRead = "ChipRead";
    public static final java.lang.String _ContactlessChipRead = "ContactlessChipRead";
    public static final java.lang.String _ManualKeyedChipReadFailure = "ManualKeyedChipReadFailure";
    public static final java.lang.String _MagstripeReadChipReadFailure = "MagstripeReadChipReadFailure";
    public static final CardInputCode UseDefault = new CardInputCode(_UseDefault);
    public static final CardInputCode Unknown = new CardInputCode(_Unknown);
    public static final CardInputCode MagstripeRead = new CardInputCode(_MagstripeRead);
    public static final CardInputCode ContactlessMagstripeRead = new CardInputCode(_ContactlessMagstripeRead);
    public static final CardInputCode ManualKeyed = new CardInputCode(_ManualKeyed);
    public static final CardInputCode ManualKeyedMagstripeFailure = new CardInputCode(_ManualKeyedMagstripeFailure);
    public static final CardInputCode ChipRead = new CardInputCode(_ChipRead);
    public static final CardInputCode ContactlessChipRead = new CardInputCode(_ContactlessChipRead);
    public static final CardInputCode ManualKeyedChipReadFailure = new CardInputCode(_ManualKeyedChipReadFailure);
    public static final CardInputCode MagstripeReadChipReadFailure = new CardInputCode(_MagstripeReadChipReadFailure);
    public java.lang.String getValue() { return _value_;}
    public static CardInputCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        CardInputCode enumeration = (CardInputCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static CardInputCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(CardInputCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "CardInputCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
