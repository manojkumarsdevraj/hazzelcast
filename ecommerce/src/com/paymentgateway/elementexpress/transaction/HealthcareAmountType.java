/**
 * HealthcareAmountType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class HealthcareAmountType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected HealthcareAmountType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _LedgerBalance = "LedgerBalance";
    public static final java.lang.String _AvailableBalance = "AvailableBalance";
    public static final java.lang.String _Healthcare = "Healthcare";
    public static final java.lang.String _Transit = "Transit";
    public static final java.lang.String _Copayment = "Copayment";
    public static final java.lang.String _OriginalAmount = "OriginalAmount";
    public static final java.lang.String _PartialAuthorizedAmount = "PartialAuthorizedAmount";
    public static final java.lang.String _Prescription = "Prescription";
    public static final java.lang.String _Vision = "Vision";
    public static final java.lang.String _Clinic = "Clinic";
    public static final java.lang.String _Dental = "Dental";
    public static final java.lang.String _CashOver = "CashOver";
    public static final java.lang.String _OriginalCashOver = "OriginalCashOver";
    public static final HealthcareAmountType LedgerBalance = new HealthcareAmountType(_LedgerBalance);
    public static final HealthcareAmountType AvailableBalance = new HealthcareAmountType(_AvailableBalance);
    public static final HealthcareAmountType Healthcare = new HealthcareAmountType(_Healthcare);
    public static final HealthcareAmountType Transit = new HealthcareAmountType(_Transit);
    public static final HealthcareAmountType Copayment = new HealthcareAmountType(_Copayment);
    public static final HealthcareAmountType OriginalAmount = new HealthcareAmountType(_OriginalAmount);
    public static final HealthcareAmountType PartialAuthorizedAmount = new HealthcareAmountType(_PartialAuthorizedAmount);
    public static final HealthcareAmountType Prescription = new HealthcareAmountType(_Prescription);
    public static final HealthcareAmountType Vision = new HealthcareAmountType(_Vision);
    public static final HealthcareAmountType Clinic = new HealthcareAmountType(_Clinic);
    public static final HealthcareAmountType Dental = new HealthcareAmountType(_Dental);
    public static final HealthcareAmountType CashOver = new HealthcareAmountType(_CashOver);
    public static final HealthcareAmountType OriginalCashOver = new HealthcareAmountType(_OriginalCashOver);
    public java.lang.String getValue() { return _value_;}
    public static HealthcareAmountType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        HealthcareAmountType enumeration = (HealthcareAmountType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static HealthcareAmountType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(HealthcareAmountType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HealthcareAmountType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
