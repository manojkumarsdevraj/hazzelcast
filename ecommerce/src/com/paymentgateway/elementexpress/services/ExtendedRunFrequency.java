/**
 * ExtendedRunFrequency.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class ExtendedRunFrequency implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ExtendedRunFrequency(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Null = "Null";
    public static final java.lang.String _OneTimeFuture = "OneTimeFuture";
    public static final java.lang.String _Daily = "Daily";
    public static final java.lang.String _Weekly = "Weekly";
    public static final java.lang.String _BiWeekly = "BiWeekly";
    public static final java.lang.String _Monthly = "Monthly";
    public static final java.lang.String _BiMonthly = "BiMonthly";
    public static final java.lang.String _Quarterly = "Quarterly";
    public static final java.lang.String _SemiAnnually = "SemiAnnually";
    public static final java.lang.String _Yearly = "Yearly";
    public static final ExtendedRunFrequency Null = new ExtendedRunFrequency(_Null);
    public static final ExtendedRunFrequency OneTimeFuture = new ExtendedRunFrequency(_OneTimeFuture);
    public static final ExtendedRunFrequency Daily = new ExtendedRunFrequency(_Daily);
    public static final ExtendedRunFrequency Weekly = new ExtendedRunFrequency(_Weekly);
    public static final ExtendedRunFrequency BiWeekly = new ExtendedRunFrequency(_BiWeekly);
    public static final ExtendedRunFrequency Monthly = new ExtendedRunFrequency(_Monthly);
    public static final ExtendedRunFrequency BiMonthly = new ExtendedRunFrequency(_BiMonthly);
    public static final ExtendedRunFrequency Quarterly = new ExtendedRunFrequency(_Quarterly);
    public static final ExtendedRunFrequency SemiAnnually = new ExtendedRunFrequency(_SemiAnnually);
    public static final ExtendedRunFrequency Yearly = new ExtendedRunFrequency(_Yearly);
    public java.lang.String getValue() { return _value_;}
    public static ExtendedRunFrequency fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ExtendedRunFrequency enumeration = (ExtendedRunFrequency)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ExtendedRunFrequency fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(ExtendedRunFrequency.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedRunFrequency"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
