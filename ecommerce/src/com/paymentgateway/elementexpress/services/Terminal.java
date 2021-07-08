/**
 * Terminal.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class Terminal  implements java.io.Serializable {
    private java.lang.String terminalID;

    private com.paymentgateway.elementexpress.services.TerminalType terminalType;

    private com.paymentgateway.elementexpress.services.CardPresentCode cardPresentCode;

    private com.paymentgateway.elementexpress.services.CardholderPresentCode cardholderPresentCode;

    private com.paymentgateway.elementexpress.services.CardInputCode cardInputCode;

    private com.paymentgateway.elementexpress.services.CVVPresenceCode CVVPresenceCode;

    private com.paymentgateway.elementexpress.services.TerminalCapabilityCode terminalCapabilityCode;

    private com.paymentgateway.elementexpress.services.TerminalEnvironmentCode terminalEnvironmentCode;

    private com.paymentgateway.elementexpress.services.MotoECICode motoECICode;

    private com.paymentgateway.elementexpress.services.CVVResponseType CVVResponseType;

    private com.paymentgateway.elementexpress.services.ConsentCode consentCode;

    private java.lang.String terminalSerialNumber;

    private com.paymentgateway.elementexpress.services.EncryptionFormat terminalEncryptionFormat;

    private java.lang.String laneNumber;

    private java.lang.String model;

    private java.lang.String EMVKernelVersion;

    public Terminal() {
    }

    public Terminal(
           java.lang.String terminalID,
           com.paymentgateway.elementexpress.services.TerminalType terminalType,
           com.paymentgateway.elementexpress.services.CardPresentCode cardPresentCode,
           com.paymentgateway.elementexpress.services.CardholderPresentCode cardholderPresentCode,
           com.paymentgateway.elementexpress.services.CardInputCode cardInputCode,
           com.paymentgateway.elementexpress.services.CVVPresenceCode CVVPresenceCode,
           com.paymentgateway.elementexpress.services.TerminalCapabilityCode terminalCapabilityCode,
           com.paymentgateway.elementexpress.services.TerminalEnvironmentCode terminalEnvironmentCode,
           com.paymentgateway.elementexpress.services.MotoECICode motoECICode,
           com.paymentgateway.elementexpress.services.CVVResponseType CVVResponseType,
           com.paymentgateway.elementexpress.services.ConsentCode consentCode,
           java.lang.String terminalSerialNumber,
           com.paymentgateway.elementexpress.services.EncryptionFormat terminalEncryptionFormat,
           java.lang.String laneNumber,
           java.lang.String model,
           java.lang.String EMVKernelVersion) {
           this.terminalID = terminalID;
           this.terminalType = terminalType;
           this.cardPresentCode = cardPresentCode;
           this.cardholderPresentCode = cardholderPresentCode;
           this.cardInputCode = cardInputCode;
           this.CVVPresenceCode = CVVPresenceCode;
           this.terminalCapabilityCode = terminalCapabilityCode;
           this.terminalEnvironmentCode = terminalEnvironmentCode;
           this.motoECICode = motoECICode;
           this.CVVResponseType = CVVResponseType;
           this.consentCode = consentCode;
           this.terminalSerialNumber = terminalSerialNumber;
           this.terminalEncryptionFormat = terminalEncryptionFormat;
           this.laneNumber = laneNumber;
           this.model = model;
           this.EMVKernelVersion = EMVKernelVersion;
    }


    /**
     * Gets the terminalID value for this Terminal.
     * 
     * @return terminalID
     */
    public java.lang.String getTerminalID() {
        return terminalID;
    }


    /**
     * Sets the terminalID value for this Terminal.
     * 
     * @param terminalID
     */
    public void setTerminalID(java.lang.String terminalID) {
        this.terminalID = terminalID;
    }


    /**
     * Gets the terminalType value for this Terminal.
     * 
     * @return terminalType
     */
    public com.paymentgateway.elementexpress.services.TerminalType getTerminalType() {
        return terminalType;
    }


    /**
     * Sets the terminalType value for this Terminal.
     * 
     * @param terminalType
     */
    public void setTerminalType(com.paymentgateway.elementexpress.services.TerminalType terminalType) {
        this.terminalType = terminalType;
    }


    /**
     * Gets the cardPresentCode value for this Terminal.
     * 
     * @return cardPresentCode
     */
    public com.paymentgateway.elementexpress.services.CardPresentCode getCardPresentCode() {
        return cardPresentCode;
    }


    /**
     * Sets the cardPresentCode value for this Terminal.
     * 
     * @param cardPresentCode
     */
    public void setCardPresentCode(com.paymentgateway.elementexpress.services.CardPresentCode cardPresentCode) {
        this.cardPresentCode = cardPresentCode;
    }


    /**
     * Gets the cardholderPresentCode value for this Terminal.
     * 
     * @return cardholderPresentCode
     */
    public com.paymentgateway.elementexpress.services.CardholderPresentCode getCardholderPresentCode() {
        return cardholderPresentCode;
    }


    /**
     * Sets the cardholderPresentCode value for this Terminal.
     * 
     * @param cardholderPresentCode
     */
    public void setCardholderPresentCode(com.paymentgateway.elementexpress.services.CardholderPresentCode cardholderPresentCode) {
        this.cardholderPresentCode = cardholderPresentCode;
    }


    /**
     * Gets the cardInputCode value for this Terminal.
     * 
     * @return cardInputCode
     */
    public com.paymentgateway.elementexpress.services.CardInputCode getCardInputCode() {
        return cardInputCode;
    }


    /**
     * Sets the cardInputCode value for this Terminal.
     * 
     * @param cardInputCode
     */
    public void setCardInputCode(com.paymentgateway.elementexpress.services.CardInputCode cardInputCode) {
        this.cardInputCode = cardInputCode;
    }


    /**
     * Gets the CVVPresenceCode value for this Terminal.
     * 
     * @return CVVPresenceCode
     */
    public com.paymentgateway.elementexpress.services.CVVPresenceCode getCVVPresenceCode() {
        return CVVPresenceCode;
    }


    /**
     * Sets the CVVPresenceCode value for this Terminal.
     * 
     * @param CVVPresenceCode
     */
    public void setCVVPresenceCode(com.paymentgateway.elementexpress.services.CVVPresenceCode CVVPresenceCode) {
        this.CVVPresenceCode = CVVPresenceCode;
    }


    /**
     * Gets the terminalCapabilityCode value for this Terminal.
     * 
     * @return terminalCapabilityCode
     */
    public com.paymentgateway.elementexpress.services.TerminalCapabilityCode getTerminalCapabilityCode() {
        return terminalCapabilityCode;
    }


    /**
     * Sets the terminalCapabilityCode value for this Terminal.
     * 
     * @param terminalCapabilityCode
     */
    public void setTerminalCapabilityCode(com.paymentgateway.elementexpress.services.TerminalCapabilityCode terminalCapabilityCode) {
        this.terminalCapabilityCode = terminalCapabilityCode;
    }


    /**
     * Gets the terminalEnvironmentCode value for this Terminal.
     * 
     * @return terminalEnvironmentCode
     */
    public com.paymentgateway.elementexpress.services.TerminalEnvironmentCode getTerminalEnvironmentCode() {
        return terminalEnvironmentCode;
    }


    /**
     * Sets the terminalEnvironmentCode value for this Terminal.
     * 
     * @param terminalEnvironmentCode
     */
    public void setTerminalEnvironmentCode(com.paymentgateway.elementexpress.services.TerminalEnvironmentCode terminalEnvironmentCode) {
        this.terminalEnvironmentCode = terminalEnvironmentCode;
    }


    /**
     * Gets the motoECICode value for this Terminal.
     * 
     * @return motoECICode
     */
    public com.paymentgateway.elementexpress.services.MotoECICode getMotoECICode() {
        return motoECICode;
    }


    /**
     * Sets the motoECICode value for this Terminal.
     * 
     * @param motoECICode
     */
    public void setMotoECICode(com.paymentgateway.elementexpress.services.MotoECICode motoECICode) {
        this.motoECICode = motoECICode;
    }


    /**
     * Gets the CVVResponseType value for this Terminal.
     * 
     * @return CVVResponseType
     */
    public com.paymentgateway.elementexpress.services.CVVResponseType getCVVResponseType() {
        return CVVResponseType;
    }


    /**
     * Sets the CVVResponseType value for this Terminal.
     * 
     * @param CVVResponseType
     */
    public void setCVVResponseType(com.paymentgateway.elementexpress.services.CVVResponseType CVVResponseType) {
        this.CVVResponseType = CVVResponseType;
    }


    /**
     * Gets the consentCode value for this Terminal.
     * 
     * @return consentCode
     */
    public com.paymentgateway.elementexpress.services.ConsentCode getConsentCode() {
        return consentCode;
    }


    /**
     * Sets the consentCode value for this Terminal.
     * 
     * @param consentCode
     */
    public void setConsentCode(com.paymentgateway.elementexpress.services.ConsentCode consentCode) {
        this.consentCode = consentCode;
    }


    /**
     * Gets the terminalSerialNumber value for this Terminal.
     * 
     * @return terminalSerialNumber
     */
    public java.lang.String getTerminalSerialNumber() {
        return terminalSerialNumber;
    }


    /**
     * Sets the terminalSerialNumber value for this Terminal.
     * 
     * @param terminalSerialNumber
     */
    public void setTerminalSerialNumber(java.lang.String terminalSerialNumber) {
        this.terminalSerialNumber = terminalSerialNumber;
    }


    /**
     * Gets the terminalEncryptionFormat value for this Terminal.
     * 
     * @return terminalEncryptionFormat
     */
    public com.paymentgateway.elementexpress.services.EncryptionFormat getTerminalEncryptionFormat() {
        return terminalEncryptionFormat;
    }


    /**
     * Sets the terminalEncryptionFormat value for this Terminal.
     * 
     * @param terminalEncryptionFormat
     */
    public void setTerminalEncryptionFormat(com.paymentgateway.elementexpress.services.EncryptionFormat terminalEncryptionFormat) {
        this.terminalEncryptionFormat = terminalEncryptionFormat;
    }


    /**
     * Gets the laneNumber value for this Terminal.
     * 
     * @return laneNumber
     */
    public java.lang.String getLaneNumber() {
        return laneNumber;
    }


    /**
     * Sets the laneNumber value for this Terminal.
     * 
     * @param laneNumber
     */
    public void setLaneNumber(java.lang.String laneNumber) {
        this.laneNumber = laneNumber;
    }


    /**
     * Gets the model value for this Terminal.
     * 
     * @return model
     */
    public java.lang.String getModel() {
        return model;
    }


    /**
     * Sets the model value for this Terminal.
     * 
     * @param model
     */
    public void setModel(java.lang.String model) {
        this.model = model;
    }


    /**
     * Gets the EMVKernelVersion value for this Terminal.
     * 
     * @return EMVKernelVersion
     */
    public java.lang.String getEMVKernelVersion() {
        return EMVKernelVersion;
    }


    /**
     * Sets the EMVKernelVersion value for this Terminal.
     * 
     * @param EMVKernelVersion
     */
    public void setEMVKernelVersion(java.lang.String EMVKernelVersion) {
        this.EMVKernelVersion = EMVKernelVersion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Terminal)) return false;
        Terminal other = (Terminal) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.terminalID==null && other.getTerminalID()==null) || 
             (this.terminalID!=null &&
              this.terminalID.equals(other.getTerminalID()))) &&
            ((this.terminalType==null && other.getTerminalType()==null) || 
             (this.terminalType!=null &&
              this.terminalType.equals(other.getTerminalType()))) &&
            ((this.cardPresentCode==null && other.getCardPresentCode()==null) || 
             (this.cardPresentCode!=null &&
              this.cardPresentCode.equals(other.getCardPresentCode()))) &&
            ((this.cardholderPresentCode==null && other.getCardholderPresentCode()==null) || 
             (this.cardholderPresentCode!=null &&
              this.cardholderPresentCode.equals(other.getCardholderPresentCode()))) &&
            ((this.cardInputCode==null && other.getCardInputCode()==null) || 
             (this.cardInputCode!=null &&
              this.cardInputCode.equals(other.getCardInputCode()))) &&
            ((this.CVVPresenceCode==null && other.getCVVPresenceCode()==null) || 
             (this.CVVPresenceCode!=null &&
              this.CVVPresenceCode.equals(other.getCVVPresenceCode()))) &&
            ((this.terminalCapabilityCode==null && other.getTerminalCapabilityCode()==null) || 
             (this.terminalCapabilityCode!=null &&
              this.terminalCapabilityCode.equals(other.getTerminalCapabilityCode()))) &&
            ((this.terminalEnvironmentCode==null && other.getTerminalEnvironmentCode()==null) || 
             (this.terminalEnvironmentCode!=null &&
              this.terminalEnvironmentCode.equals(other.getTerminalEnvironmentCode()))) &&
            ((this.motoECICode==null && other.getMotoECICode()==null) || 
             (this.motoECICode!=null &&
              this.motoECICode.equals(other.getMotoECICode()))) &&
            ((this.CVVResponseType==null && other.getCVVResponseType()==null) || 
             (this.CVVResponseType!=null &&
              this.CVVResponseType.equals(other.getCVVResponseType()))) &&
            ((this.consentCode==null && other.getConsentCode()==null) || 
             (this.consentCode!=null &&
              this.consentCode.equals(other.getConsentCode()))) &&
            ((this.terminalSerialNumber==null && other.getTerminalSerialNumber()==null) || 
             (this.terminalSerialNumber!=null &&
              this.terminalSerialNumber.equals(other.getTerminalSerialNumber()))) &&
            ((this.terminalEncryptionFormat==null && other.getTerminalEncryptionFormat()==null) || 
             (this.terminalEncryptionFormat!=null &&
              this.terminalEncryptionFormat.equals(other.getTerminalEncryptionFormat()))) &&
            ((this.laneNumber==null && other.getLaneNumber()==null) || 
             (this.laneNumber!=null &&
              this.laneNumber.equals(other.getLaneNumber()))) &&
            ((this.model==null && other.getModel()==null) || 
             (this.model!=null &&
              this.model.equals(other.getModel()))) &&
            ((this.EMVKernelVersion==null && other.getEMVKernelVersion()==null) || 
             (this.EMVKernelVersion!=null &&
              this.EMVKernelVersion.equals(other.getEMVKernelVersion())));
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
        if (getTerminalID() != null) {
            _hashCode += getTerminalID().hashCode();
        }
        if (getTerminalType() != null) {
            _hashCode += getTerminalType().hashCode();
        }
        if (getCardPresentCode() != null) {
            _hashCode += getCardPresentCode().hashCode();
        }
        if (getCardholderPresentCode() != null) {
            _hashCode += getCardholderPresentCode().hashCode();
        }
        if (getCardInputCode() != null) {
            _hashCode += getCardInputCode().hashCode();
        }
        if (getCVVPresenceCode() != null) {
            _hashCode += getCVVPresenceCode().hashCode();
        }
        if (getTerminalCapabilityCode() != null) {
            _hashCode += getTerminalCapabilityCode().hashCode();
        }
        if (getTerminalEnvironmentCode() != null) {
            _hashCode += getTerminalEnvironmentCode().hashCode();
        }
        if (getMotoECICode() != null) {
            _hashCode += getMotoECICode().hashCode();
        }
        if (getCVVResponseType() != null) {
            _hashCode += getCVVResponseType().hashCode();
        }
        if (getConsentCode() != null) {
            _hashCode += getConsentCode().hashCode();
        }
        if (getTerminalSerialNumber() != null) {
            _hashCode += getTerminalSerialNumber().hashCode();
        }
        if (getTerminalEncryptionFormat() != null) {
            _hashCode += getTerminalEncryptionFormat().hashCode();
        }
        if (getLaneNumber() != null) {
            _hashCode += getLaneNumber().hashCode();
        }
        if (getModel() != null) {
            _hashCode += getModel().hashCode();
        }
        if (getEMVKernelVersion() != null) {
            _hashCode += getEMVKernelVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Terminal.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Terminal"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminalID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminalType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cardPresentCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "CardPresentCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "CardPresentCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cardholderPresentCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "CardholderPresentCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "CardholderPresentCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cardInputCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "CardInputCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "CardInputCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CVVPresenceCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "CVVPresenceCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "CVVPresenceCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminalCapabilityCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalCapabilityCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalCapabilityCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminalEnvironmentCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalEnvironmentCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalEnvironmentCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motoECICode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "MotoECICode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "MotoECICode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CVVResponseType");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "CVVResponseType"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "CVVResponseType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consentCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ConsentCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "ConsentCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminalSerialNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalSerialNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminalEncryptionFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalEncryptionFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://services.elementexpress.com", "EncryptionFormat"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("laneNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "LaneNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("model");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "Model"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EMVKernelVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("https://services.elementexpress.com", "EMVKernelVersion"));
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
