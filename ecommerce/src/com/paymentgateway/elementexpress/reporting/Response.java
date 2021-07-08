/**
 * Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.reporting;

public class Response  extends com.paymentgateway.elementexpress.reporting.ExpressResponse  implements java.io.Serializable {
    private java.lang.String reportingData;

    private java.lang.String reportingID;

    public Response() {
    }

    public Response(
           java.lang.String expressResponseCode,
           java.lang.String expressResponseMessage,
           java.lang.String expressTransactionDate,
           java.lang.String expressTransactionTime,
           java.lang.String expressTransactionTimezone,
           com.paymentgateway.elementexpress.reporting.ExtendedParameters[] extendedParameters,
           java.lang.String reportingData,
           java.lang.String reportingID) {
        super(
            expressResponseCode,
            expressResponseMessage,
            expressTransactionDate,
            expressTransactionTime,
            expressTransactionTimezone,
            extendedParameters);
        this.reportingData = reportingData;
        this.reportingID = reportingID;
    }


    /**
     * Gets the reportingData value for this Response.
     * 
     * @return reportingData
     */
    public java.lang.String getReportingData() {
        return reportingData;
    }


    /**
     * Sets the reportingData value for this Response.
     * 
     * @param reportingData
     */
    public void setReportingData(java.lang.String reportingData) {
        this.reportingData = reportingData;
    }


    /**
     * Gets the reportingID value for this Response.
     * 
     * @return reportingID
     */
    public java.lang.String getReportingID() {
        return reportingID;
    }


    /**
     * Sets the reportingID value for this Response.
     * 
     * @param reportingID
     */
    public void setReportingID(java.lang.String reportingID) {
        this.reportingID = reportingID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Response)) return false;
        Response other = (Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.reportingData==null && other.getReportingData()==null) || 
             (this.reportingData!=null &&
              this.reportingData.equals(other.getReportingData()))) &&
            ((this.reportingID==null && other.getReportingID()==null) || 
             (this.reportingID!=null &&
              this.reportingID.equals(other.getReportingID())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getReportingData() != null) {
            _hashCode += getReportingData().hashCode();
        }
        if (getReportingID() != null) {
            _hashCode += getReportingID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportingData");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ReportingData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportingID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ReportingID"));
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
