/**
 * LogTraceLevelNull.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.reporting;

public class LogTraceLevelNull implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected LogTraceLevelNull(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _None = "None";
    public static final java.lang.String _Fatal = "Fatal";
    public static final java.lang.String _Error = "Error";
    public static final java.lang.String _Warning = "Warning";
    public static final java.lang.String _Information = "Information";
    public static final java.lang.String _Trace = "Trace";
    public static final java.lang.String _Debug = "Debug";
    public static final java.lang.String _All = "All";
    public static final LogTraceLevelNull None = new LogTraceLevelNull(_None);
    public static final LogTraceLevelNull Fatal = new LogTraceLevelNull(_Fatal);
    public static final LogTraceLevelNull Error = new LogTraceLevelNull(_Error);
    public static final LogTraceLevelNull Warning = new LogTraceLevelNull(_Warning);
    public static final LogTraceLevelNull Information = new LogTraceLevelNull(_Information);
    public static final LogTraceLevelNull Trace = new LogTraceLevelNull(_Trace);
    public static final LogTraceLevelNull Debug = new LogTraceLevelNull(_Debug);
    public static final LogTraceLevelNull All = new LogTraceLevelNull(_All);
    public java.lang.String getValue() { return _value_;}
    public static LogTraceLevelNull fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        LogTraceLevelNull enumeration = (LogTraceLevelNull)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static LogTraceLevelNull fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(LogTraceLevelNull.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "LogTraceLevel>null"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
