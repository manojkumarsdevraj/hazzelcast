/**
 * Batch.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class Batch  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.transaction.BatchCloseType batchCloseType;

    private com.paymentgateway.elementexpress.transaction.BatchQueryType batchQueryType;

    private java.lang.String hostBatchID;

    private java.lang.String hostItemID;

    private java.lang.String hostBatchCount;

    private java.lang.String hostBatchAmount;

    private com.paymentgateway.elementexpress.transaction.BatchGroupingCode batchGroupingCode;

    private java.lang.String hostReversalQueueID;

    private java.lang.String hostCreditSaleCount;

    private java.lang.String hostCreditSaleAmount;

    private java.lang.String hostCreditReturnCount;

    private java.lang.String hostCreditReturnAmount;

    private java.lang.String hostDebitSaleCount;

    private java.lang.String hostDebitSaleAmount;

    private java.lang.String hostDebitReturnCount;

    private java.lang.String hostDebitReturnAmount;

    private java.lang.String hostBatchItems;

    private com.paymentgateway.elementexpress.transaction.BatchIndexCode batchIndexCode;

    public Batch() {
    }

    public Batch(
           com.paymentgateway.elementexpress.transaction.BatchCloseType batchCloseType,
           com.paymentgateway.elementexpress.transaction.BatchQueryType batchQueryType,
           java.lang.String hostBatchID,
           java.lang.String hostItemID,
           java.lang.String hostBatchCount,
           java.lang.String hostBatchAmount,
           com.paymentgateway.elementexpress.transaction.BatchGroupingCode batchGroupingCode,
           java.lang.String hostReversalQueueID,
           java.lang.String hostCreditSaleCount,
           java.lang.String hostCreditSaleAmount,
           java.lang.String hostCreditReturnCount,
           java.lang.String hostCreditReturnAmount,
           java.lang.String hostDebitSaleCount,
           java.lang.String hostDebitSaleAmount,
           java.lang.String hostDebitReturnCount,
           java.lang.String hostDebitReturnAmount,
           java.lang.String hostBatchItems,
           com.paymentgateway.elementexpress.transaction.BatchIndexCode batchIndexCode) {
           this.batchCloseType = batchCloseType;
           this.batchQueryType = batchQueryType;
           this.hostBatchID = hostBatchID;
           this.hostItemID = hostItemID;
           this.hostBatchCount = hostBatchCount;
           this.hostBatchAmount = hostBatchAmount;
           this.batchGroupingCode = batchGroupingCode;
           this.hostReversalQueueID = hostReversalQueueID;
           this.hostCreditSaleCount = hostCreditSaleCount;
           this.hostCreditSaleAmount = hostCreditSaleAmount;
           this.hostCreditReturnCount = hostCreditReturnCount;
           this.hostCreditReturnAmount = hostCreditReturnAmount;
           this.hostDebitSaleCount = hostDebitSaleCount;
           this.hostDebitSaleAmount = hostDebitSaleAmount;
           this.hostDebitReturnCount = hostDebitReturnCount;
           this.hostDebitReturnAmount = hostDebitReturnAmount;
           this.hostBatchItems = hostBatchItems;
           this.batchIndexCode = batchIndexCode;
    }


    /**
     * Gets the batchCloseType value for this Batch.
     * 
     * @return batchCloseType
     */
    public com.paymentgateway.elementexpress.transaction.BatchCloseType getBatchCloseType() {
        return batchCloseType;
    }


    /**
     * Sets the batchCloseType value for this Batch.
     * 
     * @param batchCloseType
     */
    public void setBatchCloseType(com.paymentgateway.elementexpress.transaction.BatchCloseType batchCloseType) {
        this.batchCloseType = batchCloseType;
    }


    /**
     * Gets the batchQueryType value for this Batch.
     * 
     * @return batchQueryType
     */
    public com.paymentgateway.elementexpress.transaction.BatchQueryType getBatchQueryType() {
        return batchQueryType;
    }


    /**
     * Sets the batchQueryType value for this Batch.
     * 
     * @param batchQueryType
     */
    public void setBatchQueryType(com.paymentgateway.elementexpress.transaction.BatchQueryType batchQueryType) {
        this.batchQueryType = batchQueryType;
    }


    /**
     * Gets the hostBatchID value for this Batch.
     * 
     * @return hostBatchID
     */
    public java.lang.String getHostBatchID() {
        return hostBatchID;
    }


    /**
     * Sets the hostBatchID value for this Batch.
     * 
     * @param hostBatchID
     */
    public void setHostBatchID(java.lang.String hostBatchID) {
        this.hostBatchID = hostBatchID;
    }


    /**
     * Gets the hostItemID value for this Batch.
     * 
     * @return hostItemID
     */
    public java.lang.String getHostItemID() {
        return hostItemID;
    }


    /**
     * Sets the hostItemID value for this Batch.
     * 
     * @param hostItemID
     */
    public void setHostItemID(java.lang.String hostItemID) {
        this.hostItemID = hostItemID;
    }


    /**
     * Gets the hostBatchCount value for this Batch.
     * 
     * @return hostBatchCount
     */
    public java.lang.String getHostBatchCount() {
        return hostBatchCount;
    }


    /**
     * Sets the hostBatchCount value for this Batch.
     * 
     * @param hostBatchCount
     */
    public void setHostBatchCount(java.lang.String hostBatchCount) {
        this.hostBatchCount = hostBatchCount;
    }


    /**
     * Gets the hostBatchAmount value for this Batch.
     * 
     * @return hostBatchAmount
     */
    public java.lang.String getHostBatchAmount() {
        return hostBatchAmount;
    }


    /**
     * Sets the hostBatchAmount value for this Batch.
     * 
     * @param hostBatchAmount
     */
    public void setHostBatchAmount(java.lang.String hostBatchAmount) {
        this.hostBatchAmount = hostBatchAmount;
    }


    /**
     * Gets the batchGroupingCode value for this Batch.
     * 
     * @return batchGroupingCode
     */
    public com.paymentgateway.elementexpress.transaction.BatchGroupingCode getBatchGroupingCode() {
        return batchGroupingCode;
    }


    /**
     * Sets the batchGroupingCode value for this Batch.
     * 
     * @param batchGroupingCode
     */
    public void setBatchGroupingCode(com.paymentgateway.elementexpress.transaction.BatchGroupingCode batchGroupingCode) {
        this.batchGroupingCode = batchGroupingCode;
    }


    /**
     * Gets the hostReversalQueueID value for this Batch.
     * 
     * @return hostReversalQueueID
     */
    public java.lang.String getHostReversalQueueID() {
        return hostReversalQueueID;
    }


    /**
     * Sets the hostReversalQueueID value for this Batch.
     * 
     * @param hostReversalQueueID
     */
    public void setHostReversalQueueID(java.lang.String hostReversalQueueID) {
        this.hostReversalQueueID = hostReversalQueueID;
    }


    /**
     * Gets the hostCreditSaleCount value for this Batch.
     * 
     * @return hostCreditSaleCount
     */
    public java.lang.String getHostCreditSaleCount() {
        return hostCreditSaleCount;
    }


    /**
     * Sets the hostCreditSaleCount value for this Batch.
     * 
     * @param hostCreditSaleCount
     */
    public void setHostCreditSaleCount(java.lang.String hostCreditSaleCount) {
        this.hostCreditSaleCount = hostCreditSaleCount;
    }


    /**
     * Gets the hostCreditSaleAmount value for this Batch.
     * 
     * @return hostCreditSaleAmount
     */
    public java.lang.String getHostCreditSaleAmount() {
        return hostCreditSaleAmount;
    }


    /**
     * Sets the hostCreditSaleAmount value for this Batch.
     * 
     * @param hostCreditSaleAmount
     */
    public void setHostCreditSaleAmount(java.lang.String hostCreditSaleAmount) {
        this.hostCreditSaleAmount = hostCreditSaleAmount;
    }


    /**
     * Gets the hostCreditReturnCount value for this Batch.
     * 
     * @return hostCreditReturnCount
     */
    public java.lang.String getHostCreditReturnCount() {
        return hostCreditReturnCount;
    }


    /**
     * Sets the hostCreditReturnCount value for this Batch.
     * 
     * @param hostCreditReturnCount
     */
    public void setHostCreditReturnCount(java.lang.String hostCreditReturnCount) {
        this.hostCreditReturnCount = hostCreditReturnCount;
    }


    /**
     * Gets the hostCreditReturnAmount value for this Batch.
     * 
     * @return hostCreditReturnAmount
     */
    public java.lang.String getHostCreditReturnAmount() {
        return hostCreditReturnAmount;
    }


    /**
     * Sets the hostCreditReturnAmount value for this Batch.
     * 
     * @param hostCreditReturnAmount
     */
    public void setHostCreditReturnAmount(java.lang.String hostCreditReturnAmount) {
        this.hostCreditReturnAmount = hostCreditReturnAmount;
    }


    /**
     * Gets the hostDebitSaleCount value for this Batch.
     * 
     * @return hostDebitSaleCount
     */
    public java.lang.String getHostDebitSaleCount() {
        return hostDebitSaleCount;
    }


    /**
     * Sets the hostDebitSaleCount value for this Batch.
     * 
     * @param hostDebitSaleCount
     */
    public void setHostDebitSaleCount(java.lang.String hostDebitSaleCount) {
        this.hostDebitSaleCount = hostDebitSaleCount;
    }


    /**
     * Gets the hostDebitSaleAmount value for this Batch.
     * 
     * @return hostDebitSaleAmount
     */
    public java.lang.String getHostDebitSaleAmount() {
        return hostDebitSaleAmount;
    }


    /**
     * Sets the hostDebitSaleAmount value for this Batch.
     * 
     * @param hostDebitSaleAmount
     */
    public void setHostDebitSaleAmount(java.lang.String hostDebitSaleAmount) {
        this.hostDebitSaleAmount = hostDebitSaleAmount;
    }


    /**
     * Gets the hostDebitReturnCount value for this Batch.
     * 
     * @return hostDebitReturnCount
     */
    public java.lang.String getHostDebitReturnCount() {
        return hostDebitReturnCount;
    }


    /**
     * Sets the hostDebitReturnCount value for this Batch.
     * 
     * @param hostDebitReturnCount
     */
    public void setHostDebitReturnCount(java.lang.String hostDebitReturnCount) {
        this.hostDebitReturnCount = hostDebitReturnCount;
    }


    /**
     * Gets the hostDebitReturnAmount value for this Batch.
     * 
     * @return hostDebitReturnAmount
     */
    public java.lang.String getHostDebitReturnAmount() {
        return hostDebitReturnAmount;
    }


    /**
     * Sets the hostDebitReturnAmount value for this Batch.
     * 
     * @param hostDebitReturnAmount
     */
    public void setHostDebitReturnAmount(java.lang.String hostDebitReturnAmount) {
        this.hostDebitReturnAmount = hostDebitReturnAmount;
    }


    /**
     * Gets the hostBatchItems value for this Batch.
     * 
     * @return hostBatchItems
     */
    public java.lang.String getHostBatchItems() {
        return hostBatchItems;
    }


    /**
     * Sets the hostBatchItems value for this Batch.
     * 
     * @param hostBatchItems
     */
    public void setHostBatchItems(java.lang.String hostBatchItems) {
        this.hostBatchItems = hostBatchItems;
    }


    /**
     * Gets the batchIndexCode value for this Batch.
     * 
     * @return batchIndexCode
     */
    public com.paymentgateway.elementexpress.transaction.BatchIndexCode getBatchIndexCode() {
        return batchIndexCode;
    }


    /**
     * Sets the batchIndexCode value for this Batch.
     * 
     * @param batchIndexCode
     */
    public void setBatchIndexCode(com.paymentgateway.elementexpress.transaction.BatchIndexCode batchIndexCode) {
        this.batchIndexCode = batchIndexCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Batch)) return false;
        Batch other = (Batch) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.batchCloseType==null && other.getBatchCloseType()==null) || 
             (this.batchCloseType!=null &&
              this.batchCloseType.equals(other.getBatchCloseType()))) &&
            ((this.batchQueryType==null && other.getBatchQueryType()==null) || 
             (this.batchQueryType!=null &&
              this.batchQueryType.equals(other.getBatchQueryType()))) &&
            ((this.hostBatchID==null && other.getHostBatchID()==null) || 
             (this.hostBatchID!=null &&
              this.hostBatchID.equals(other.getHostBatchID()))) &&
            ((this.hostItemID==null && other.getHostItemID()==null) || 
             (this.hostItemID!=null &&
              this.hostItemID.equals(other.getHostItemID()))) &&
            ((this.hostBatchCount==null && other.getHostBatchCount()==null) || 
             (this.hostBatchCount!=null &&
              this.hostBatchCount.equals(other.getHostBatchCount()))) &&
            ((this.hostBatchAmount==null && other.getHostBatchAmount()==null) || 
             (this.hostBatchAmount!=null &&
              this.hostBatchAmount.equals(other.getHostBatchAmount()))) &&
            ((this.batchGroupingCode==null && other.getBatchGroupingCode()==null) || 
             (this.batchGroupingCode!=null &&
              this.batchGroupingCode.equals(other.getBatchGroupingCode()))) &&
            ((this.hostReversalQueueID==null && other.getHostReversalQueueID()==null) || 
             (this.hostReversalQueueID!=null &&
              this.hostReversalQueueID.equals(other.getHostReversalQueueID()))) &&
            ((this.hostCreditSaleCount==null && other.getHostCreditSaleCount()==null) || 
             (this.hostCreditSaleCount!=null &&
              this.hostCreditSaleCount.equals(other.getHostCreditSaleCount()))) &&
            ((this.hostCreditSaleAmount==null && other.getHostCreditSaleAmount()==null) || 
             (this.hostCreditSaleAmount!=null &&
              this.hostCreditSaleAmount.equals(other.getHostCreditSaleAmount()))) &&
            ((this.hostCreditReturnCount==null && other.getHostCreditReturnCount()==null) || 
             (this.hostCreditReturnCount!=null &&
              this.hostCreditReturnCount.equals(other.getHostCreditReturnCount()))) &&
            ((this.hostCreditReturnAmount==null && other.getHostCreditReturnAmount()==null) || 
             (this.hostCreditReturnAmount!=null &&
              this.hostCreditReturnAmount.equals(other.getHostCreditReturnAmount()))) &&
            ((this.hostDebitSaleCount==null && other.getHostDebitSaleCount()==null) || 
             (this.hostDebitSaleCount!=null &&
              this.hostDebitSaleCount.equals(other.getHostDebitSaleCount()))) &&
            ((this.hostDebitSaleAmount==null && other.getHostDebitSaleAmount()==null) || 
             (this.hostDebitSaleAmount!=null &&
              this.hostDebitSaleAmount.equals(other.getHostDebitSaleAmount()))) &&
            ((this.hostDebitReturnCount==null && other.getHostDebitReturnCount()==null) || 
             (this.hostDebitReturnCount!=null &&
              this.hostDebitReturnCount.equals(other.getHostDebitReturnCount()))) &&
            ((this.hostDebitReturnAmount==null && other.getHostDebitReturnAmount()==null) || 
             (this.hostDebitReturnAmount!=null &&
              this.hostDebitReturnAmount.equals(other.getHostDebitReturnAmount()))) &&
            ((this.hostBatchItems==null && other.getHostBatchItems()==null) || 
             (this.hostBatchItems!=null &&
              this.hostBatchItems.equals(other.getHostBatchItems()))) &&
            ((this.batchIndexCode==null && other.getBatchIndexCode()==null) || 
             (this.batchIndexCode!=null &&
              this.batchIndexCode.equals(other.getBatchIndexCode())));
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
        if (getBatchCloseType() != null) {
            _hashCode += getBatchCloseType().hashCode();
        }
        if (getBatchQueryType() != null) {
            _hashCode += getBatchQueryType().hashCode();
        }
        if (getHostBatchID() != null) {
            _hashCode += getHostBatchID().hashCode();
        }
        if (getHostItemID() != null) {
            _hashCode += getHostItemID().hashCode();
        }
        if (getHostBatchCount() != null) {
            _hashCode += getHostBatchCount().hashCode();
        }
        if (getHostBatchAmount() != null) {
            _hashCode += getHostBatchAmount().hashCode();
        }
        if (getBatchGroupingCode() != null) {
            _hashCode += getBatchGroupingCode().hashCode();
        }
        if (getHostReversalQueueID() != null) {
            _hashCode += getHostReversalQueueID().hashCode();
        }
        if (getHostCreditSaleCount() != null) {
            _hashCode += getHostCreditSaleCount().hashCode();
        }
        if (getHostCreditSaleAmount() != null) {
            _hashCode += getHostCreditSaleAmount().hashCode();
        }
        if (getHostCreditReturnCount() != null) {
            _hashCode += getHostCreditReturnCount().hashCode();
        }
        if (getHostCreditReturnAmount() != null) {
            _hashCode += getHostCreditReturnAmount().hashCode();
        }
        if (getHostDebitSaleCount() != null) {
            _hashCode += getHostDebitSaleCount().hashCode();
        }
        if (getHostDebitSaleAmount() != null) {
            _hashCode += getHostDebitSaleAmount().hashCode();
        }
        if (getHostDebitReturnCount() != null) {
            _hashCode += getHostDebitReturnCount().hashCode();
        }
        if (getHostDebitReturnAmount() != null) {
            _hashCode += getHostDebitReturnAmount().hashCode();
        }
        if (getHostBatchItems() != null) {
            _hashCode += getHostBatchItems().hashCode();
        }
        if (getBatchIndexCode() != null) {
            _hashCode += getBatchIndexCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Batch.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Batch"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchCloseType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchCloseType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchCloseType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchQueryType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchQueryType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchQueryType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostBatchID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostBatchID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostItemID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostItemID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostBatchCount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostBatchCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostBatchAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostBatchAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchGroupingCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchGroupingCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchGroupingCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostReversalQueueID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostReversalQueueID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostCreditSaleCount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostCreditSaleCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostCreditSaleAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostCreditSaleAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostCreditReturnCount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostCreditReturnCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostCreditReturnAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostCreditReturnAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostDebitSaleCount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostDebitSaleCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostDebitSaleAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostDebitSaleAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostDebitReturnCount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostDebitReturnCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostDebitReturnAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostDebitReturnAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostBatchItems");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostBatchItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchIndexCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchIndexCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchIndexCode"));
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
