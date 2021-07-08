/**
 * DemandDepositAccount.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class DemandDepositAccount  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.transaction.DDAAccountType DDAAccountType;

    private java.lang.String accountNumber;

    private java.lang.String routingNumber;

    private java.lang.String checkNumber;

    private com.paymentgateway.elementexpress.transaction.CheckType checkType;

    private java.lang.String truncatedAccountNumber;

    private java.lang.String truncatedRoutingNumber;

    public DemandDepositAccount() {
    }

    public DemandDepositAccount(
           com.paymentgateway.elementexpress.transaction.DDAAccountType DDAAccountType,
           java.lang.String accountNumber,
           java.lang.String routingNumber,
           java.lang.String checkNumber,
           com.paymentgateway.elementexpress.transaction.CheckType checkType,
           java.lang.String truncatedAccountNumber,
           java.lang.String truncatedRoutingNumber) {
           this.DDAAccountType = DDAAccountType;
           this.accountNumber = accountNumber;
           this.routingNumber = routingNumber;
           this.checkNumber = checkNumber;
           this.checkType = checkType;
           this.truncatedAccountNumber = truncatedAccountNumber;
           this.truncatedRoutingNumber = truncatedRoutingNumber;
    }


    /**
     * Gets the DDAAccountType value for this DemandDepositAccount.
     * 
     * @return DDAAccountType
     */
    public com.paymentgateway.elementexpress.transaction.DDAAccountType getDDAAccountType() {
        return DDAAccountType;
    }


    /**
     * Sets the DDAAccountType value for this DemandDepositAccount.
     * 
     * @param DDAAccountType
     */
    public void setDDAAccountType(com.paymentgateway.elementexpress.transaction.DDAAccountType DDAAccountType) {
        this.DDAAccountType = DDAAccountType;
    }


    /**
     * Gets the accountNumber value for this DemandDepositAccount.
     * 
     * @return accountNumber
     */
    public java.lang.String getAccountNumber() {
        return accountNumber;
    }


    /**
     * Sets the accountNumber value for this DemandDepositAccount.
     * 
     * @param accountNumber
     */
    public void setAccountNumber(java.lang.String accountNumber) {
        this.accountNumber = accountNumber;
    }


    /**
     * Gets the routingNumber value for this DemandDepositAccount.
     * 
     * @return routingNumber
     */
    public java.lang.String getRoutingNumber() {
        return routingNumber;
    }


    /**
     * Sets the routingNumber value for this DemandDepositAccount.
     * 
     * @param routingNumber
     */
    public void setRoutingNumber(java.lang.String routingNumber) {
        this.routingNumber = routingNumber;
    }


    /**
     * Gets the checkNumber value for this DemandDepositAccount.
     * 
     * @return checkNumber
     */
    public java.lang.String getCheckNumber() {
        return checkNumber;
    }


    /**
     * Sets the checkNumber value for this DemandDepositAccount.
     * 
     * @param checkNumber
     */
    public void setCheckNumber(java.lang.String checkNumber) {
        this.checkNumber = checkNumber;
    }


    /**
     * Gets the checkType value for this DemandDepositAccount.
     * 
     * @return checkType
     */
    public com.paymentgateway.elementexpress.transaction.CheckType getCheckType() {
        return checkType;
    }


    /**
     * Sets the checkType value for this DemandDepositAccount.
     * 
     * @param checkType
     */
    public void setCheckType(com.paymentgateway.elementexpress.transaction.CheckType checkType) {
        this.checkType = checkType;
    }


    /**
     * Gets the truncatedAccountNumber value for this DemandDepositAccount.
     * 
     * @return truncatedAccountNumber
     */
    public java.lang.String getTruncatedAccountNumber() {
        return truncatedAccountNumber;
    }


    /**
     * Sets the truncatedAccountNumber value for this DemandDepositAccount.
     * 
     * @param truncatedAccountNumber
     */
    public void setTruncatedAccountNumber(java.lang.String truncatedAccountNumber) {
        this.truncatedAccountNumber = truncatedAccountNumber;
    }


    /**
     * Gets the truncatedRoutingNumber value for this DemandDepositAccount.
     * 
     * @return truncatedRoutingNumber
     */
    public java.lang.String getTruncatedRoutingNumber() {
        return truncatedRoutingNumber;
    }


    /**
     * Sets the truncatedRoutingNumber value for this DemandDepositAccount.
     * 
     * @param truncatedRoutingNumber
     */
    public void setTruncatedRoutingNumber(java.lang.String truncatedRoutingNumber) {
        this.truncatedRoutingNumber = truncatedRoutingNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DemandDepositAccount)) return false;
        DemandDepositAccount other = (DemandDepositAccount) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.DDAAccountType==null && other.getDDAAccountType()==null) || 
             (this.DDAAccountType!=null &&
              this.DDAAccountType.equals(other.getDDAAccountType()))) &&
            ((this.accountNumber==null && other.getAccountNumber()==null) || 
             (this.accountNumber!=null &&
              this.accountNumber.equals(other.getAccountNumber()))) &&
            ((this.routingNumber==null && other.getRoutingNumber()==null) || 
             (this.routingNumber!=null &&
              this.routingNumber.equals(other.getRoutingNumber()))) &&
            ((this.checkNumber==null && other.getCheckNumber()==null) || 
             (this.checkNumber!=null &&
              this.checkNumber.equals(other.getCheckNumber()))) &&
            ((this.checkType==null && other.getCheckType()==null) || 
             (this.checkType!=null &&
              this.checkType.equals(other.getCheckType()))) &&
            ((this.truncatedAccountNumber==null && other.getTruncatedAccountNumber()==null) || 
             (this.truncatedAccountNumber!=null &&
              this.truncatedAccountNumber.equals(other.getTruncatedAccountNumber()))) &&
            ((this.truncatedRoutingNumber==null && other.getTruncatedRoutingNumber()==null) || 
             (this.truncatedRoutingNumber!=null &&
              this.truncatedRoutingNumber.equals(other.getTruncatedRoutingNumber())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDDAAccountType() != null) {
            _hashCode += getDDAAccountType().hashCode();
        }
        if (getAccountNumber() != null) {
            _hashCode += getAccountNumber().hashCode();
        }
        if (getRoutingNumber() != null) {
            _hashCode += getRoutingNumber().hashCode();
        }
        if (getCheckNumber() != null) {
            _hashCode += getCheckNumber().hashCode();
        }
        if (getCheckType() != null) {
            _hashCode += getCheckType().hashCode();
        }
        if (getTruncatedAccountNumber() != null) {
            _hashCode += getTruncatedAccountNumber().hashCode();
        }
        if (getTruncatedRoutingNumber() != null) {
            _hashCode += getTruncatedRoutingNumber().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DemandDepositAccount.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DemandDepositAccount"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DDAAccountType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DDAAccountType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DDAAccountType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AccountNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routingNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "RoutingNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checkNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checkType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("truncatedAccountNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TruncatedAccountNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("truncatedRoutingNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TruncatedRoutingNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
