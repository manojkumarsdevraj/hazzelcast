/**
 * TerminalEnvironmentCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class TerminalEnvironmentCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TerminalEnvironmentCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _UseDefault = "UseDefault";
    public static final java.lang.String _NoTerminal = "NoTerminal";
    public static final java.lang.String _LocalAttended = "LocalAttended";
    public static final java.lang.String _LocalUnattended = "LocalUnattended";
    public static final java.lang.String _RemoteAttended = "RemoteAttended";
    public static final java.lang.String _RemoteUnattended = "RemoteUnattended";
    public static final java.lang.String _ECommerce = "ECommerce";
    public static final TerminalEnvironmentCode UseDefault = new TerminalEnvironmentCode(_UseDefault);
    public static final TerminalEnvironmentCode NoTerminal = new TerminalEnvironmentCode(_NoTerminal);
    public static final TerminalEnvironmentCode LocalAttended = new TerminalEnvironmentCode(_LocalAttended);
    public static final TerminalEnvironmentCode LocalUnattended = new TerminalEnvironmentCode(_LocalUnattended);
    public static final TerminalEnvironmentCode RemoteAttended = new TerminalEnvironmentCode(_RemoteAttended);
    public static final TerminalEnvironmentCode RemoteUnattended = new TerminalEnvironmentCode(_RemoteUnattended);
    public static final TerminalEnvironmentCode ECommerce = new TerminalEnvironmentCode(_ECommerce);
    public java.lang.String getValue() { return _value_;}
    public static TerminalEnvironmentCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TerminalEnvironmentCode enumeration = (TerminalEnvironmentCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TerminalEnvironmentCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TerminalEnvironmentCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalEnvironmentCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
