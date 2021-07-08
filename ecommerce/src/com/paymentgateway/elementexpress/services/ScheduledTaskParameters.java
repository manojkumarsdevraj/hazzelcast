/**
 * ScheduledTaskParameters.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class ScheduledTaskParameters  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.services.ScheduledTaskQueryType scheduledTaskQueryType;

    private java.lang.String scheduledTaskID;

    private java.lang.String scheduledTaskGroupID;

    private java.lang.String scheduledTaskName;

    private java.lang.String scheduledTaskReferenceNumber;

    private com.paymentgateway.elementexpress.services.ScheduledTaskRunStatus scheduledTaskRunStatus;

    private java.lang.String runStartDate;

    private com.paymentgateway.elementexpress.services.ExtendedBooleanType runUntilCancelFlag;

    private com.paymentgateway.elementexpress.services.ExtendedStatusType scheduledTaskStatus;

    private com.paymentgateway.elementexpress.services.ExtendedRunFrequency runFrequency;

    public ScheduledTaskParameters() {
    }

    public ScheduledTaskParameters(
           com.paymentgateway.elementexpress.services.ScheduledTaskQueryType scheduledTaskQueryType,
           java.lang.String scheduledTaskID,
           java.lang.String scheduledTaskGroupID,
           java.lang.String scheduledTaskName,
           java.lang.String scheduledTaskReferenceNumber,
           com.paymentgateway.elementexpress.services.ScheduledTaskRunStatus scheduledTaskRunStatus,
           java.lang.String runStartDate,
           com.paymentgateway.elementexpress.services.ExtendedBooleanType runUntilCancelFlag,
           com.paymentgateway.elementexpress.services.ExtendedStatusType scheduledTaskStatus,
           com.paymentgateway.elementexpress.services.ExtendedRunFrequency runFrequency) {
           this.scheduledTaskQueryType = scheduledTaskQueryType;
           this.scheduledTaskID = scheduledTaskID;
           this.scheduledTaskGroupID = scheduledTaskGroupID;
           this.scheduledTaskName = scheduledTaskName;
           this.scheduledTaskReferenceNumber = scheduledTaskReferenceNumber;
           this.scheduledTaskRunStatus = scheduledTaskRunStatus;
           this.runStartDate = runStartDate;
           this.runUntilCancelFlag = runUntilCancelFlag;
           this.scheduledTaskStatus = scheduledTaskStatus;
           this.runFrequency = runFrequency;
    }


    /**
     * Gets the scheduledTaskQueryType value for this ScheduledTaskParameters.
     * 
     * @return scheduledTaskQueryType
     */
    public com.paymentgateway.elementexpress.services.ScheduledTaskQueryType getScheduledTaskQueryType() {
        return scheduledTaskQueryType;
    }


    /**
     * Sets the scheduledTaskQueryType value for this ScheduledTaskParameters.
     * 
     * @param scheduledTaskQueryType
     */
    public void setScheduledTaskQueryType(com.paymentgateway.elementexpress.services.ScheduledTaskQueryType scheduledTaskQueryType) {
        this.scheduledTaskQueryType = scheduledTaskQueryType;
    }


    /**
     * Gets the scheduledTaskID value for this ScheduledTaskParameters.
     * 
     * @return scheduledTaskID
     */
    public java.lang.String getScheduledTaskID() {
        return scheduledTaskID;
    }


    /**
     * Sets the scheduledTaskID value for this ScheduledTaskParameters.
     * 
     * @param scheduledTaskID
     */
    public void setScheduledTaskID(java.lang.String scheduledTaskID) {
        this.scheduledTaskID = scheduledTaskID;
    }


    /**
     * Gets the scheduledTaskGroupID value for this ScheduledTaskParameters.
     * 
     * @return scheduledTaskGroupID
     */
    public java.lang.String getScheduledTaskGroupID() {
        return scheduledTaskGroupID;
    }


    /**
     * Sets the scheduledTaskGroupID value for this ScheduledTaskParameters.
     * 
     * @param scheduledTaskGroupID
     */
    public void setScheduledTaskGroupID(java.lang.String scheduledTaskGroupID) {
        this.scheduledTaskGroupID = scheduledTaskGroupID;
    }


    /**
     * Gets the scheduledTaskName value for this ScheduledTaskParameters.
     * 
     * @return scheduledTaskName
     */
    public java.lang.String getScheduledTaskName() {
        return scheduledTaskName;
    }


    /**
     * Sets the scheduledTaskName value for this ScheduledTaskParameters.
     * 
     * @param scheduledTaskName
     */
    public void setScheduledTaskName(java.lang.String scheduledTaskName) {
        this.scheduledTaskName = scheduledTaskName;
    }


    /**
     * Gets the scheduledTaskReferenceNumber value for this ScheduledTaskParameters.
     * 
     * @return scheduledTaskReferenceNumber
     */
    public java.lang.String getScheduledTaskReferenceNumber() {
        return scheduledTaskReferenceNumber;
    }


    /**
     * Sets the scheduledTaskReferenceNumber value for this ScheduledTaskParameters.
     * 
     * @param scheduledTaskReferenceNumber
     */
    public void setScheduledTaskReferenceNumber(java.lang.String scheduledTaskReferenceNumber) {
        this.scheduledTaskReferenceNumber = scheduledTaskReferenceNumber;
    }


    /**
     * Gets the scheduledTaskRunStatus value for this ScheduledTaskParameters.
     * 
     * @return scheduledTaskRunStatus
     */
    public com.paymentgateway.elementexpress.services.ScheduledTaskRunStatus getScheduledTaskRunStatus() {
        return scheduledTaskRunStatus;
    }


    /**
     * Sets the scheduledTaskRunStatus value for this ScheduledTaskParameters.
     * 
     * @param scheduledTaskRunStatus
     */
    public void setScheduledTaskRunStatus(com.paymentgateway.elementexpress.services.ScheduledTaskRunStatus scheduledTaskRunStatus) {
        this.scheduledTaskRunStatus = scheduledTaskRunStatus;
    }


    /**
     * Gets the runStartDate value for this ScheduledTaskParameters.
     * 
     * @return runStartDate
     */
    public java.lang.String getRunStartDate() {
        return runStartDate;
    }


    /**
     * Sets the runStartDate value for this ScheduledTaskParameters.
     * 
     * @param runStartDate
     */
    public void setRunStartDate(java.lang.String runStartDate) {
        this.runStartDate = runStartDate;
    }


    /**
     * Gets the runUntilCancelFlag value for this ScheduledTaskParameters.
     * 
     * @return runUntilCancelFlag
     */
    public com.paymentgateway.elementexpress.services.ExtendedBooleanType getRunUntilCancelFlag() {
        return runUntilCancelFlag;
    }


    /**
     * Sets the runUntilCancelFlag value for this ScheduledTaskParameters.
     * 
     * @param runUntilCancelFlag
     */
    public void setRunUntilCancelFlag(com.paymentgateway.elementexpress.services.ExtendedBooleanType runUntilCancelFlag) {
        this.runUntilCancelFlag = runUntilCancelFlag;
    }


    /**
     * Gets the scheduledTaskStatus value for this ScheduledTaskParameters.
     * 
     * @return scheduledTaskStatus
     */
    public com.paymentgateway.elementexpress.services.ExtendedStatusType getScheduledTaskStatus() {
        return scheduledTaskStatus;
    }


    /**
     * Sets the scheduledTaskStatus value for this ScheduledTaskParameters.
     * 
     * @param scheduledTaskStatus
     */
    public void setScheduledTaskStatus(com.paymentgateway.elementexpress.services.ExtendedStatusType scheduledTaskStatus) {
        this.scheduledTaskStatus = scheduledTaskStatus;
    }


    /**
     * Gets the runFrequency value for this ScheduledTaskParameters.
     * 
     * @return runFrequency
     */
    public com.paymentgateway.elementexpress.services.ExtendedRunFrequency getRunFrequency() {
        return runFrequency;
    }


    /**
     * Sets the runFrequency value for this ScheduledTaskParameters.
     * 
     * @param runFrequency
     */
    public void setRunFrequency(com.paymentgateway.elementexpress.services.ExtendedRunFrequency runFrequency) {
        this.runFrequency = runFrequency;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ScheduledTaskParameters)) return false;
        ScheduledTaskParameters other = (ScheduledTaskParameters) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.scheduledTaskQueryType==null && other.getScheduledTaskQueryType()==null) || 
             (this.scheduledTaskQueryType!=null &&
              this.scheduledTaskQueryType.equals(other.getScheduledTaskQueryType()))) &&
            ((this.scheduledTaskID==null && other.getScheduledTaskID()==null) || 
             (this.scheduledTaskID!=null &&
              this.scheduledTaskID.equals(other.getScheduledTaskID()))) &&
            ((this.scheduledTaskGroupID==null && other.getScheduledTaskGroupID()==null) || 
             (this.scheduledTaskGroupID!=null &&
              this.scheduledTaskGroupID.equals(other.getScheduledTaskGroupID()))) &&
            ((this.scheduledTaskName==null && other.getScheduledTaskName()==null) || 
             (this.scheduledTaskName!=null &&
              this.scheduledTaskName.equals(other.getScheduledTaskName()))) &&
            ((this.scheduledTaskReferenceNumber==null && other.getScheduledTaskReferenceNumber()==null) || 
             (this.scheduledTaskReferenceNumber!=null &&
              this.scheduledTaskReferenceNumber.equals(other.getScheduledTaskReferenceNumber()))) &&
            ((this.scheduledTaskRunStatus==null && other.getScheduledTaskRunStatus()==null) || 
             (this.scheduledTaskRunStatus!=null &&
              this.scheduledTaskRunStatus.equals(other.getScheduledTaskRunStatus()))) &&
            ((this.runStartDate==null && other.getRunStartDate()==null) || 
             (this.runStartDate!=null &&
              this.runStartDate.equals(other.getRunStartDate()))) &&
            ((this.runUntilCancelFlag==null && other.getRunUntilCancelFlag()==null) || 
             (this.runUntilCancelFlag!=null &&
              this.runUntilCancelFlag.equals(other.getRunUntilCancelFlag()))) &&
            ((this.scheduledTaskStatus==null && other.getScheduledTaskStatus()==null) || 
             (this.scheduledTaskStatus!=null &&
              this.scheduledTaskStatus.equals(other.getScheduledTaskStatus()))) &&
            ((this.runFrequency==null && other.getRunFrequency()==null) || 
             (this.runFrequency!=null &&
              this.runFrequency.equals(other.getRunFrequency())));
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
        if (getScheduledTaskQueryType() != null) {
            _hashCode += getScheduledTaskQueryType().hashCode();
        }
        if (getScheduledTaskID() != null) {
            _hashCode += getScheduledTaskID().hashCode();
        }
        if (getScheduledTaskGroupID() != null) {
            _hashCode += getScheduledTaskGroupID().hashCode();
        }
        if (getScheduledTaskName() != null) {
            _hashCode += getScheduledTaskName().hashCode();
        }
        if (getScheduledTaskReferenceNumber() != null) {
            _hashCode += getScheduledTaskReferenceNumber().hashCode();
        }
        if (getScheduledTaskRunStatus() != null) {
            _hashCode += getScheduledTaskRunStatus().hashCode();
        }
        if (getRunStartDate() != null) {
            _hashCode += getRunStartDate().hashCode();
        }
        if (getRunUntilCancelFlag() != null) {
            _hashCode += getRunUntilCancelFlag().hashCode();
        }
        if (getScheduledTaskStatus() != null) {
            _hashCode += getScheduledTaskStatus().hashCode();
        }
        if (getRunFrequency() != null) {
            _hashCode += getRunFrequency().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ScheduledTaskParameters.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskParameters"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskQueryType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskQueryType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskQueryType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskGroupID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskGroupID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskName");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskReferenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskReferenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskRunStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskRunStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskRunStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("runStartDate");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "RunStartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("runUntilCancelFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "RunUntilCancelFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedBooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedStatusType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("runFrequency");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "RunFrequency"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedRunFrequency"));
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
