/**
 * TerminalCapabilityCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class TerminalCapabilityCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TerminalCapabilityCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _UseDefault = "UseDefault";
    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _NoTerminal = "NoTerminal";
    public static final java.lang.String _MagstripeReader = "MagstripeReader";
    public static final java.lang.String _ContactlessMagstripeReader = "ContactlessMagstripeReader";
    public static final java.lang.String _KeyEntered = "KeyEntered";
    public static final java.lang.String _ChipReader = "ChipReader";
    public static final java.lang.String _ContactlessChipReader = "ContactlessChipReader";
    public static final TerminalCapabilityCode UseDefault = new TerminalCapabilityCode(_UseDefault);
    public static final TerminalCapabilityCode Unknown = new TerminalCapabilityCode(_Unknown);
    public static final TerminalCapabilityCode NoTerminal = new TerminalCapabilityCode(_NoTerminal);
    public static final TerminalCapabilityCode MagstripeReader = new TerminalCapabilityCode(_MagstripeReader);
    public static final TerminalCapabilityCode ContactlessMagstripeReader = new TerminalCapabilityCode(_ContactlessMagstripeReader);
    public static final TerminalCapabilityCode KeyEntered = new TerminalCapabilityCode(_KeyEntered);
    public static final TerminalCapabilityCode ChipReader = new TerminalCapabilityCode(_ChipReader);
    public static final TerminalCapabilityCode ContactlessChipReader = new TerminalCapabilityCode(_ContactlessChipReader);
    public java.lang.String getValue() { return _value_;}
    public static TerminalCapabilityCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TerminalCapabilityCode enumeration = (TerminalCapabilityCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TerminalCapabilityCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TerminalCapabilityCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TerminalCapabilityCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
