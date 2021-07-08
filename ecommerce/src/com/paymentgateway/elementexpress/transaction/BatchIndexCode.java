/**
 * BatchIndexCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class BatchIndexCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected BatchIndexCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Current = "Current";
    public static final java.lang.String _FirstPrevious = "FirstPrevious";
    public static final java.lang.String _SecondPrevious = "SecondPrevious";
    public static final java.lang.String _ThirdPrevious = "ThirdPrevious";
    public static final java.lang.String _FourthPrevious = "FourthPrevious";
    public static final java.lang.String _FifthPrevious = "FifthPrevious";
    public static final java.lang.String _SixthPrevious = "SixthPrevious";
    public static final java.lang.String _SeventhPrevious = "SeventhPrevious";
    public static final BatchIndexCode Current = new BatchIndexCode(_Current);
    public static final BatchIndexCode FirstPrevious = new BatchIndexCode(_FirstPrevious);
    public static final BatchIndexCode SecondPrevious = new BatchIndexCode(_SecondPrevious);
    public static final BatchIndexCode ThirdPrevious = new BatchIndexCode(_ThirdPrevious);
    public static final BatchIndexCode FourthPrevious = new BatchIndexCode(_FourthPrevious);
    public static final BatchIndexCode FifthPrevious = new BatchIndexCode(_FifthPrevious);
    public static final BatchIndexCode SixthPrevious = new BatchIndexCode(_SixthPrevious);
    public static final BatchIndexCode SeventhPrevious = new BatchIndexCode(_SeventhPrevious);
    public java.lang.String getValue() { return _value_;}
    public static BatchIndexCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        BatchIndexCode enumeration = (BatchIndexCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static BatchIndexCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(BatchIndexCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchIndexCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
