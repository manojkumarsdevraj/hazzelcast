/**
 * TransactionSetupMethod.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class TransactionSetupMethod implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TransactionSetupMethod(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Null = "Null";
    public static final java.lang.String _CreditCardSale = "CreditCardSale";
    public static final java.lang.String _CreditCardAuthorization = "CreditCardAuthorization";
    public static final java.lang.String _CreditCardAVSOnly = "CreditCardAVSOnly";
    public static final java.lang.String _CreditCardForce = "CreditCardForce";
    public static final java.lang.String _DebitCardSale = "DebitCardSale";
    public static final java.lang.String _CheckSale = "CheckSale";
    public static final java.lang.String _PaymentAccountCreate = "PaymentAccountCreate";
    public static final java.lang.String _PaymentAccountUpdate = "PaymentAccountUpdate";
    public static final java.lang.String _Sale = "Sale";
    public static final TransactionSetupMethod Null = new TransactionSetupMethod(_Null);
    public static final TransactionSetupMethod CreditCardSale = new TransactionSetupMethod(_CreditCardSale);
    public static final TransactionSetupMethod CreditCardAuthorization = new TransactionSetupMethod(_CreditCardAuthorization);
    public static final TransactionSetupMethod CreditCardAVSOnly = new TransactionSetupMethod(_CreditCardAVSOnly);
    public static final TransactionSetupMethod CreditCardForce = new TransactionSetupMethod(_CreditCardForce);
    public static final TransactionSetupMethod DebitCardSale = new TransactionSetupMethod(_DebitCardSale);
    public static final TransactionSetupMethod CheckSale = new TransactionSetupMethod(_CheckSale);
    public static final TransactionSetupMethod PaymentAccountCreate = new TransactionSetupMethod(_PaymentAccountCreate);
    public static final TransactionSetupMethod PaymentAccountUpdate = new TransactionSetupMethod(_PaymentAccountUpdate);
    public static final TransactionSetupMethod Sale = new TransactionSetupMethod(_Sale);
    public java.lang.String getValue() { return _value_;}
    public static TransactionSetupMethod fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TransactionSetupMethod enumeration = (TransactionSetupMethod)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TransactionSetupMethod fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TransactionSetupMethod.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupMethod"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
