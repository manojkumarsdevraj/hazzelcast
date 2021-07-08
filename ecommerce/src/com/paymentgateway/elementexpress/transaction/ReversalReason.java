/**
 * ReversalReason.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class ReversalReason implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ReversalReason(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _RejectedPartialApproval = "RejectedPartialApproval";
    public static final java.lang.String _Timeout = "Timeout";
    public static final java.lang.String _EditError = "EditError";
    public static final java.lang.String _MACVerifyError = "MACVerifyError";
    public static final java.lang.String _MACSyncError = "MACSyncError";
    public static final java.lang.String _EncryptionError = "EncryptionError";
    public static final java.lang.String _SystemError = "SystemError";
    public static final java.lang.String _PossibleFraud = "PossibleFraud";
    public static final java.lang.String _CardRemoval = "CardRemoval";
    public static final java.lang.String _ChipDecline = "ChipDecline";
    public static final java.lang.String _TerminalError = "TerminalError";
    public static final ReversalReason Unknown = new ReversalReason(_Unknown);
    public static final ReversalReason RejectedPartialApproval = new ReversalReason(_RejectedPartialApproval);
    public static final ReversalReason Timeout = new ReversalReason(_Timeout);
    public static final ReversalReason EditError = new ReversalReason(_EditError);
    public static final ReversalReason MACVerifyError = new ReversalReason(_MACVerifyError);
    public static final ReversalReason MACSyncError = new ReversalReason(_MACSyncError);
    public static final ReversalReason EncryptionError = new ReversalReason(_EncryptionError);
    public static final ReversalReason SystemError = new ReversalReason(_SystemError);
    public static final ReversalReason PossibleFraud = new ReversalReason(_PossibleFraud);
    public static final ReversalReason CardRemoval = new ReversalReason(_CardRemoval);
    public static final ReversalReason ChipDecline = new ReversalReason(_ChipDecline);
    public static final ReversalReason TerminalError = new ReversalReason(_TerminalError);
    public java.lang.String getValue() { return _value_;}
    public static ReversalReason fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ReversalReason enumeration = (ReversalReason)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ReversalReason fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(ReversalReason.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ReversalReason"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
