/**
 * PASSUpdaterBatchStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class PASSUpdaterBatchStatus implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected PASSUpdaterBatchStatus(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Null = "Null";
    public static final java.lang.String _IncludedInNextBatch = "IncludedInNextBatch";
    public static final java.lang.String _NotIncludedInNextBatch = "NotIncludedInNextBatch";
    public static final PASSUpdaterBatchStatus Null = new PASSUpdaterBatchStatus(_Null);
    public static final PASSUpdaterBatchStatus IncludedInNextBatch = new PASSUpdaterBatchStatus(_IncludedInNextBatch);
    public static final PASSUpdaterBatchStatus NotIncludedInNextBatch = new PASSUpdaterBatchStatus(_NotIncludedInNextBatch);
    public java.lang.String getValue() { return _value_;}
    public static PASSUpdaterBatchStatus fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        PASSUpdaterBatchStatus enumeration = (PASSUpdaterBatchStatus)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static PASSUpdaterBatchStatus fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(PASSUpdaterBatchStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PASSUpdaterBatchStatus"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
