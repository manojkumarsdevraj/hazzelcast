/**
 * PASSUpdaterStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class PASSUpdaterStatus implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected PASSUpdaterStatus(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Null = "Null";
    public static final java.lang.String _UpdateInProgress = "UpdateInProgress";
    public static final java.lang.String _MatchNoChanges = "MatchNoChanges";
    public static final java.lang.String _MatchAccountChange = "MatchAccountChange";
    public static final java.lang.String _MatchExpirationChange = "MatchExpirationChange";
    public static final java.lang.String _MatchAccountClosed = "MatchAccountClosed";
    public static final java.lang.String _MatchContactCardholder = "MatchContactCardholder";
    public static final java.lang.String _NoMatchParticipating = "NoMatchParticipating";
    public static final java.lang.String _NoMatchNonParticipating = "NoMatchNonParticipating";
    public static final java.lang.String _InvalidInfo = "InvalidInfo";
    public static final java.lang.String _NoResponse = "NoResponse";
    public static final java.lang.String _NotAllowed = "NotAllowed";
    public static final java.lang.String _Error = "Error";
    public static final java.lang.String _PASSUpdaterDisabled = "PASSUpdaterDisabled";
    public static final java.lang.String _NotUpdated = "NotUpdated";
    public static final PASSUpdaterStatus Null = new PASSUpdaterStatus(_Null);
    public static final PASSUpdaterStatus UpdateInProgress = new PASSUpdaterStatus(_UpdateInProgress);
    public static final PASSUpdaterStatus MatchNoChanges = new PASSUpdaterStatus(_MatchNoChanges);
    public static final PASSUpdaterStatus MatchAccountChange = new PASSUpdaterStatus(_MatchAccountChange);
    public static final PASSUpdaterStatus MatchExpirationChange = new PASSUpdaterStatus(_MatchExpirationChange);
    public static final PASSUpdaterStatus MatchAccountClosed = new PASSUpdaterStatus(_MatchAccountClosed);
    public static final PASSUpdaterStatus MatchContactCardholder = new PASSUpdaterStatus(_MatchContactCardholder);
    public static final PASSUpdaterStatus NoMatchParticipating = new PASSUpdaterStatus(_NoMatchParticipating);
    public static final PASSUpdaterStatus NoMatchNonParticipating = new PASSUpdaterStatus(_NoMatchNonParticipating);
    public static final PASSUpdaterStatus InvalidInfo = new PASSUpdaterStatus(_InvalidInfo);
    public static final PASSUpdaterStatus NoResponse = new PASSUpdaterStatus(_NoResponse);
    public static final PASSUpdaterStatus NotAllowed = new PASSUpdaterStatus(_NotAllowed);
    public static final PASSUpdaterStatus Error = new PASSUpdaterStatus(_Error);
    public static final PASSUpdaterStatus PASSUpdaterDisabled = new PASSUpdaterStatus(_PASSUpdaterDisabled);
    public static final PASSUpdaterStatus NotUpdated = new PASSUpdaterStatus(_NotUpdated);
    public java.lang.String getValue() { return _value_;}
    public static PASSUpdaterStatus fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        PASSUpdaterStatus enumeration = (PASSUpdaterStatus)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static PASSUpdaterStatus fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(PASSUpdaterStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterStatus"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
