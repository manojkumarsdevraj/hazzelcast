/**
 * ScheduledTask.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class ScheduledTask  implements java.io.Serializable {
    private java.lang.String scheduledTaskID;

    private java.lang.String scheduledTaskRunLogID;

    private java.lang.String scheduledTaskGroupID;

    private java.lang.String scheduledTaskName;

    private java.lang.String scheduledTaskReferenceNumber;

    private com.paymentgateway.elementexpress.transaction.RunFrequency runFrequency;

    private java.lang.String runStartDate;

    private com.paymentgateway.elementexpress.transaction.BooleanType runUntilCancelFlag;

    private java.lang.String runCycles;

    private com.paymentgateway.elementexpress.transaction.StatusType scheduledTaskStatus;

    public ScheduledTask() {
    }

    public ScheduledTask(
           java.lang.String scheduledTaskID,
           java.lang.String scheduledTaskRunLogID,
           java.lang.String scheduledTaskGroupID,
           java.lang.String scheduledTaskName,
           java.lang.String scheduledTaskReferenceNumber,
           com.paymentgateway.elementexpress.transaction.RunFrequency runFrequency,
           java.lang.String runStartDate,
           com.paymentgateway.elementexpress.transaction.BooleanType runUntilCancelFlag,
           java.lang.String runCycles,
           com.paymentgateway.elementexpress.transaction.StatusType scheduledTaskStatus) {
           this.scheduledTaskID = scheduledTaskID;
           this.scheduledTaskRunLogID = scheduledTaskRunLogID;
           this.scheduledTaskGroupID = scheduledTaskGroupID;
           this.scheduledTaskName = scheduledTaskName;
           this.scheduledTaskReferenceNumber = scheduledTaskReferenceNumber;
           this.runFrequency = runFrequency;
           this.runStartDate = runStartDate;
           this.runUntilCancelFlag = runUntilCancelFlag;
           this.runCycles = runCycles;
           this.scheduledTaskStatus = scheduledTaskStatus;
    }


    /**
     * Gets the scheduledTaskID value for this ScheduledTask.
     * 
     * @return scheduledTaskID
     */
    public java.lang.String getScheduledTaskID() {
        return scheduledTaskID;
    }


    /**
     * Sets the scheduledTaskID value for this ScheduledTask.
     * 
     * @param scheduledTaskID
     */
    public void setScheduledTaskID(java.lang.String scheduledTaskID) {
        this.scheduledTaskID = scheduledTaskID;
    }


    /**
     * Gets the scheduledTaskRunLogID value for this ScheduledTask.
     * 
     * @return scheduledTaskRunLogID
     */
    public java.lang.String getScheduledTaskRunLogID() {
        return scheduledTaskRunLogID;
    }


    /**
     * Sets the scheduledTaskRunLogID value for this ScheduledTask.
     * 
     * @param scheduledTaskRunLogID
     */
    public void setScheduledTaskRunLogID(java.lang.String scheduledTaskRunLogID) {
        this.scheduledTaskRunLogID = scheduledTaskRunLogID;
    }


    /**
     * Gets the scheduledTaskGroupID value for this ScheduledTask.
     * 
     * @return scheduledTaskGroupID
     */
    public java.lang.String getScheduledTaskGroupID() {
        return scheduledTaskGroupID;
    }


    /**
     * Sets the scheduledTaskGroupID value for this ScheduledTask.
     * 
     * @param scheduledTaskGroupID
     */
    public void setScheduledTaskGroupID(java.lang.String scheduledTaskGroupID) {
        this.scheduledTaskGroupID = scheduledTaskGroupID;
    }


    /**
     * Gets the scheduledTaskName value for this ScheduledTask.
     * 
     * @return scheduledTaskName
     */
    public java.lang.String getScheduledTaskName() {
        return scheduledTaskName;
    }


    /**
     * Sets the scheduledTaskName value for this ScheduledTask.
     * 
     * @param scheduledTaskName
     */
    public void setScheduledTaskName(java.lang.String scheduledTaskName) {
        this.scheduledTaskName = scheduledTaskName;
    }


    /**
     * Gets the scheduledTaskReferenceNumber value for this ScheduledTask.
     * 
     * @return scheduledTaskReferenceNumber
     */
    public java.lang.String getScheduledTaskReferenceNumber() {
        return scheduledTaskReferenceNumber;
    }


    /**
     * Sets the scheduledTaskReferenceNumber value for this ScheduledTask.
     * 
     * @param scheduledTaskReferenceNumber
     */
    public void setScheduledTaskReferenceNumber(java.lang.String scheduledTaskReferenceNumber) {
        this.scheduledTaskReferenceNumber = scheduledTaskReferenceNumber;
    }


    /**
     * Gets the runFrequency value for this ScheduledTask.
     * 
     * @return runFrequency
     */
    public com.paymentgateway.elementexpress.transaction.RunFrequency getRunFrequency() {
        return runFrequency;
    }


    /**
     * Sets the runFrequency value for this ScheduledTask.
     * 
     * @param runFrequency
     */
    public void setRunFrequency(com.paymentgateway.elementexpress.transaction.RunFrequency runFrequency) {
        this.runFrequency = runFrequency;
    }


    /**
     * Gets the runStartDate value for this ScheduledTask.
     * 
     * @return runStartDate
     */
    public java.lang.String getRunStartDate() {
        return runStartDate;
    }


    /**
     * Sets the runStartDate value for this ScheduledTask.
     * 
     * @param runStartDate
     */
    public void setRunStartDate(java.lang.String runStartDate) {
        this.runStartDate = runStartDate;
    }


    /**
     * Gets the runUntilCancelFlag value for this ScheduledTask.
     * 
     * @return runUntilCancelFlag
     */
    public com.paymentgateway.elementexpress.transaction.BooleanType getRunUntilCancelFlag() {
        return runUntilCancelFlag;
    }


    /**
     * Sets the runUntilCancelFlag value for this ScheduledTask.
     * 
     * @param runUntilCancelFlag
     */
    public void setRunUntilCancelFlag(com.paymentgateway.elementexpress.transaction.BooleanType runUntilCancelFlag) {
        this.runUntilCancelFlag = runUntilCancelFlag;
    }


    /**
     * Gets the runCycles value for this ScheduledTask.
     * 
     * @return runCycles
     */
    public java.lang.String getRunCycles() {
        return runCycles;
    }


    /**
     * Sets the runCycles value for this ScheduledTask.
     * 
     * @param runCycles
     */
    public void setRunCycles(java.lang.String runCycles) {
        this.runCycles = runCycles;
    }


    /**
     * Gets the scheduledTaskStatus value for this ScheduledTask.
     * 
     * @return scheduledTaskStatus
     */
    public com.paymentgateway.elementexpress.transaction.StatusType getScheduledTaskStatus() {
        return scheduledTaskStatus;
    }


    /**
     * Sets the scheduledTaskStatus value for this ScheduledTask.
     * 
     * @param scheduledTaskStatus
     */
    public void setScheduledTaskStatus(com.paymentgateway.elementexpress.transaction.StatusType scheduledTaskStatus) {
        this.scheduledTaskStatus = scheduledTaskStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ScheduledTask)) return false;
        ScheduledTask other = (ScheduledTask) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.scheduledTaskID==null && other.getScheduledTaskID()==null) || 
             (this.scheduledTaskID!=null &&
              this.scheduledTaskID.equals(other.getScheduledTaskID()))) &&
            ((this.scheduledTaskRunLogID==null && other.getScheduledTaskRunLogID()==null) || 
             (this.scheduledTaskRunLogID!=null &&
              this.scheduledTaskRunLogID.equals(other.getScheduledTaskRunLogID()))) &&
            ((this.scheduledTaskGroupID==null && other.getScheduledTaskGroupID()==null) || 
             (this.scheduledTaskGroupID!=null &&
              this.scheduledTaskGroupID.equals(other.getScheduledTaskGroupID()))) &&
            ((this.scheduledTaskName==null && other.getScheduledTaskName()==null) || 
             (this.scheduledTaskName!=null &&
              this.scheduledTaskName.equals(other.getScheduledTaskName()))) &&
            ((this.scheduledTaskReferenceNumber==null && other.getScheduledTaskReferenceNumber()==null) || 
             (this.scheduledTaskReferenceNumber!=null &&
              this.scheduledTaskReferenceNumber.equals(other.getScheduledTaskReferenceNumber()))) &&
            ((this.runFrequency==null && other.getRunFrequency()==null) || 
             (this.runFrequency!=null &&
              this.runFrequency.equals(other.getRunFrequency()))) &&
            ((this.runStartDate==null && other.getRunStartDate()==null) || 
             (this.runStartDate!=null &&
              this.runStartDate.equals(other.getRunStartDate()))) &&
            ((this.runUntilCancelFlag==null && other.getRunUntilCancelFlag()==null) || 
             (this.runUntilCancelFlag!=null &&
              this.runUntilCancelFlag.equals(other.getRunUntilCancelFlag()))) &&
            ((this.runCycles==null && other.getRunCycles()==null) || 
             (this.runCycles!=null &&
              this.runCycles.equals(other.getRunCycles()))) &&
            ((this.scheduledTaskStatus==null && other.getScheduledTaskStatus()==null) || 
             (this.scheduledTaskStatus!=null &&
              this.scheduledTaskStatus.equals(other.getScheduledTaskStatus())));
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
        if (getScheduledTaskID() != null) {
            _hashCode += getScheduledTaskID().hashCode();
        }
        if (getScheduledTaskRunLogID() != null) {
            _hashCode += getScheduledTaskRunLogID().hashCode();
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
        if (getRunFrequency() != null) {
            _hashCode += getRunFrequency().hashCode();
        }
        if (getRunStartDate() != null) {
            _hashCode += getRunStartDate().hashCode();
        }
        if (getRunUntilCancelFlag() != null) {
            _hashCode += getRunUntilCancelFlag().hashCode();
        }
        if (getRunCycles() != null) {
            _hashCode += getRunCycles().hashCode();
        }
        if (getScheduledTaskStatus() != null) {
            _hashCode += getScheduledTaskStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ScheduledTask.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ScheduledTask"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ScheduledTaskID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskRunLogID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ScheduledTaskRunLogID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskGroupID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ScheduledTaskGroupID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskName");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ScheduledTaskName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskReferenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ScheduledTaskReferenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("runFrequency");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "RunFrequency"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "RunFrequency"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("runStartDate");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "RunStartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("runUntilCancelFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "RunUntilCancelFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BooleanType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("runCycles");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "RunCycles"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTaskStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ScheduledTaskStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "StatusType"));
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
