/**
 * MarketCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class MarketCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected MarketCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Default = "Default";
    public static final java.lang.String _AutoRental = "AutoRental";
    public static final java.lang.String _DirectMarketing = "DirectMarketing";
    public static final java.lang.String _ECommerce = "ECommerce";
    public static final java.lang.String _FoodRestaurant = "FoodRestaurant";
    public static final java.lang.String _HotelLodging = "HotelLodging";
    public static final java.lang.String _Petroleum = "Petroleum";
    public static final java.lang.String _Retail = "Retail";
    public static final java.lang.String _QSR = "QSR";
    public static final MarketCode Default = new MarketCode(_Default);
    public static final MarketCode AutoRental = new MarketCode(_AutoRental);
    public static final MarketCode DirectMarketing = new MarketCode(_DirectMarketing);
    public static final MarketCode ECommerce = new MarketCode(_ECommerce);
    public static final MarketCode FoodRestaurant = new MarketCode(_FoodRestaurant);
    public static final MarketCode HotelLodging = new MarketCode(_HotelLodging);
    public static final MarketCode Petroleum = new MarketCode(_Petroleum);
    public static final MarketCode Retail = new MarketCode(_Retail);
    public static final MarketCode QSR = new MarketCode(_QSR);
    public java.lang.String getValue() { return _value_;}
    public static MarketCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        MarketCode enumeration = (MarketCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static MarketCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(MarketCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "MarketCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
