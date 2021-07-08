/**
 * HealthcareAccountType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class HealthcareAccountType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected HealthcareAccountType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _NotSpecified = "NotSpecified";
    public static final java.lang.String _Savings = "Savings";
    public static final java.lang.String _Checking = "Checking";
    public static final java.lang.String _CreditCard = "CreditCard";
    public static final java.lang.String _Universal = "Universal";
    public static final java.lang.String _StoredValueAccount = "StoredValueAccount";
    public static final java.lang.String _CashBenefitsAccount = "CashBenefitsAccount";
    public static final java.lang.String _FoodStampsAccount = "FoodStampsAccount";
    public static final HealthcareAccountType NotSpecified = new HealthcareAccountType(_NotSpecified);
    public static final HealthcareAccountType Savings = new HealthcareAccountType(_Savings);
    public static final HealthcareAccountType Checking = new HealthcareAccountType(_Checking);
    public static final HealthcareAccountType CreditCard = new HealthcareAccountType(_CreditCard);
    public static final HealthcareAccountType Universal = new HealthcareAccountType(_Universal);
    public static final HealthcareAccountType StoredValueAccount = new HealthcareAccountType(_StoredValueAccount);
    public static final HealthcareAccountType CashBenefitsAccount = new HealthcareAccountType(_CashBenefitsAccount);
    public static final HealthcareAccountType FoodStampsAccount = new HealthcareAccountType(_FoodStampsAccount);
    public java.lang.String getValue() { return _value_;}
    public static HealthcareAccountType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        HealthcareAccountType enumeration = (HealthcareAccountType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static HealthcareAccountType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(HealthcareAccountType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HealthcareAccountType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
