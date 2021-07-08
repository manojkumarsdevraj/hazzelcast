/**
 * Identification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class Identification  implements java.io.Serializable {
    private java.lang.String taxIDNumber;

    private java.lang.String driversLicenseNumber;

    private java.lang.String driversLicenseState;

    private java.lang.String birthDate;

    public Identification() {
    }

    public Identification(
           java.lang.String taxIDNumber,
           java.lang.String driversLicenseNumber,
           java.lang.String driversLicenseState,
           java.lang.String birthDate) {
           this.taxIDNumber = taxIDNumber;
           this.driversLicenseNumber = driversLicenseNumber;
           this.driversLicenseState = driversLicenseState;
           this.birthDate = birthDate;
    }


    /**
     * Gets the taxIDNumber value for this Identification.
     * 
     * @return taxIDNumber
     */
    public java.lang.String getTaxIDNumber() {
        return taxIDNumber;
    }


    /**
     * Sets the taxIDNumber value for this Identification.
     * 
     * @param taxIDNumber
     */
    public void setTaxIDNumber(java.lang.String taxIDNumber) {
        this.taxIDNumber = taxIDNumber;
    }


    /**
     * Gets the driversLicenseNumber value for this Identification.
     * 
     * @return driversLicenseNumber
     */
    public java.lang.String getDriversLicenseNumber() {
        return driversLicenseNumber;
    }


    /**
     * Sets the driversLicenseNumber value for this Identification.
     * 
     * @param driversLicenseNumber
     */
    public void setDriversLicenseNumber(java.lang.String driversLicenseNumber) {
        this.driversLicenseNumber = driversLicenseNumber;
    }


    /**
     * Gets the driversLicenseState value for this Identification.
     * 
     * @return driversLicenseState
     */
    public java.lang.String getDriversLicenseState() {
        return driversLicenseState;
    }


    /**
     * Sets the driversLicenseState value for this Identification.
     * 
     * @param driversLicenseState
     */
    public void setDriversLicenseState(java.lang.String driversLicenseState) {
        this.driversLicenseState = driversLicenseState;
    }


    /**
     * Gets the birthDate value for this Identification.
     * 
     * @return birthDate
     */
    public java.lang.String getBirthDate() {
        return birthDate;
    }


    /**
     * Sets the birthDate value for this Identification.
     * 
     * @param birthDate
     */
    public void setBirthDate(java.lang.String birthDate) {
        this.birthDate = birthDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Identification)) return false;
        Identification other = (Identification) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.taxIDNumber==null && other.getTaxIDNumber()==null) || 
             (this.taxIDNumber!=null &&
              this.taxIDNumber.equals(other.getTaxIDNumber()))) &&
            ((this.driversLicenseNumber==null && other.getDriversLicenseNumber()==null) || 
             (this.driversLicenseNumber!=null &&
              this.driversLicenseNumber.equals(other.getDriversLicenseNumber()))) &&
            ((this.driversLicenseState==null && other.getDriversLicenseState()==null) || 
             (this.driversLicenseState!=null &&
              this.driversLicenseState.equals(other.getDriversLicenseState()))) &&
            ((this.birthDate==null && other.getBirthDate()==null) || 
             (this.birthDate!=null &&
              this.birthDate.equals(other.getBirthDate())));
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
        if (getTaxIDNumber() != null) {
            _hashCode += getTaxIDNumber().hashCode();
        }
        if (getDriversLicenseNumber() != null) {
            _hashCode += getDriversLicenseNumber().hashCode();
        }
        if (getDriversLicenseState() != null) {
            _hashCode += getDriversLicenseState().hashCode();
        }
        if (getBirthDate() != null) {
            _hashCode += getBirthDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Identification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Identification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxIDNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TaxIDNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driversLicenseNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DriversLicenseNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driversLicenseState");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DriversLicenseState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("birthDate");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BirthDate"));
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
