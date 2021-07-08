/**
 * EnhancedBIN.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class EnhancedBIN  implements java.io.Serializable {
    private java.lang.String status;

    private java.lang.String debitCard;

    private java.lang.String checkCard;

    private java.lang.String creditCard;

    private java.lang.String giftCard;

    private java.lang.String commercialCard;

    private java.lang.String fleetCard;

    private java.lang.String prepaidCard;

    private java.lang.String HSAFSACard;

    private java.lang.String PINLessBillPay;

    private java.lang.String EBT;

    private java.lang.String WIC;

    private java.lang.String internationalBIN;

    private java.lang.String durbinBINRegulation;

    public EnhancedBIN() {
    }

    public EnhancedBIN(
           java.lang.String status,
           java.lang.String debitCard,
           java.lang.String checkCard,
           java.lang.String creditCard,
           java.lang.String giftCard,
           java.lang.String commercialCard,
           java.lang.String fleetCard,
           java.lang.String prepaidCard,
           java.lang.String HSAFSACard,
           java.lang.String PINLessBillPay,
           java.lang.String EBT,
           java.lang.String WIC,
           java.lang.String internationalBIN,
           java.lang.String durbinBINRegulation) {
           this.status = status;
           this.debitCard = debitCard;
           this.checkCard = checkCard;
           this.creditCard = creditCard;
           this.giftCard = giftCard;
           this.commercialCard = commercialCard;
           this.fleetCard = fleetCard;
           this.prepaidCard = prepaidCard;
           this.HSAFSACard = HSAFSACard;
           this.PINLessBillPay = PINLessBillPay;
           this.EBT = EBT;
           this.WIC = WIC;
           this.internationalBIN = internationalBIN;
           this.durbinBINRegulation = durbinBINRegulation;
    }


    /**
     * Gets the status value for this EnhancedBIN.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this EnhancedBIN.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the debitCard value for this EnhancedBIN.
     * 
     * @return debitCard
     */
    public java.lang.String getDebitCard() {
        return debitCard;
    }


    /**
     * Sets the debitCard value for this EnhancedBIN.
     * 
     * @param debitCard
     */
    public void setDebitCard(java.lang.String debitCard) {
        this.debitCard = debitCard;
    }


    /**
     * Gets the checkCard value for this EnhancedBIN.
     * 
     * @return checkCard
     */
    public java.lang.String getCheckCard() {
        return checkCard;
    }


    /**
     * Sets the checkCard value for this EnhancedBIN.
     * 
     * @param checkCard
     */
    public void setCheckCard(java.lang.String checkCard) {
        this.checkCard = checkCard;
    }


    /**
     * Gets the creditCard value for this EnhancedBIN.
     * 
     * @return creditCard
     */
    public java.lang.String getCreditCard() {
        return creditCard;
    }


    /**
     * Sets the creditCard value for this EnhancedBIN.
     * 
     * @param creditCard
     */
    public void setCreditCard(java.lang.String creditCard) {
        this.creditCard = creditCard;
    }


    /**
     * Gets the giftCard value for this EnhancedBIN.
     * 
     * @return giftCard
     */
    public java.lang.String getGiftCard() {
        return giftCard;
    }


    /**
     * Sets the giftCard value for this EnhancedBIN.
     * 
     * @param giftCard
     */
    public void setGiftCard(java.lang.String giftCard) {
        this.giftCard = giftCard;
    }


    /**
     * Gets the commercialCard value for this EnhancedBIN.
     * 
     * @return commercialCard
     */
    public java.lang.String getCommercialCard() {
        return commercialCard;
    }


    /**
     * Sets the commercialCard value for this EnhancedBIN.
     * 
     * @param commercialCard
     */
    public void setCommercialCard(java.lang.String commercialCard) {
        this.commercialCard = commercialCard;
    }


    /**
     * Gets the fleetCard value for this EnhancedBIN.
     * 
     * @return fleetCard
     */
    public java.lang.String getFleetCard() {
        return fleetCard;
    }


    /**
     * Sets the fleetCard value for this EnhancedBIN.
     * 
     * @param fleetCard
     */
    public void setFleetCard(java.lang.String fleetCard) {
        this.fleetCard = fleetCard;
    }


    /**
     * Gets the prepaidCard value for this EnhancedBIN.
     * 
     * @return prepaidCard
     */
    public java.lang.String getPrepaidCard() {
        return prepaidCard;
    }


    /**
     * Sets the prepaidCard value for this EnhancedBIN.
     * 
     * @param prepaidCard
     */
    public void setPrepaidCard(java.lang.String prepaidCard) {
        this.prepaidCard = prepaidCard;
    }


    /**
     * Gets the HSAFSACard value for this EnhancedBIN.
     * 
     * @return HSAFSACard
     */
    public java.lang.String getHSAFSACard() {
        return HSAFSACard;
    }


    /**
     * Sets the HSAFSACard value for this EnhancedBIN.
     * 
     * @param HSAFSACard
     */
    public void setHSAFSACard(java.lang.String HSAFSACard) {
        this.HSAFSACard = HSAFSACard;
    }


    /**
     * Gets the PINLessBillPay value for this EnhancedBIN.
     * 
     * @return PINLessBillPay
     */
    public java.lang.String getPINLessBillPay() {
        return PINLessBillPay;
    }


    /**
     * Sets the PINLessBillPay value for this EnhancedBIN.
     * 
     * @param PINLessBillPay
     */
    public void setPINLessBillPay(java.lang.String PINLessBillPay) {
        this.PINLessBillPay = PINLessBillPay;
    }


    /**
     * Gets the EBT value for this EnhancedBIN.
     * 
     * @return EBT
     */
    public java.lang.String getEBT() {
        return EBT;
    }


    /**
     * Sets the EBT value for this EnhancedBIN.
     * 
     * @param EBT
     */
    public void setEBT(java.lang.String EBT) {
        this.EBT = EBT;
    }


    /**
     * Gets the WIC value for this EnhancedBIN.
     * 
     * @return WIC
     */
    public java.lang.String getWIC() {
        return WIC;
    }


    /**
     * Sets the WIC value for this EnhancedBIN.
     * 
     * @param WIC
     */
    public void setWIC(java.lang.String WIC) {
        this.WIC = WIC;
    }


    /**
     * Gets the internationalBIN value for this EnhancedBIN.
     * 
     * @return internationalBIN
     */
    public java.lang.String getInternationalBIN() {
        return internationalBIN;
    }


    /**
     * Sets the internationalBIN value for this EnhancedBIN.
     * 
     * @param internationalBIN
     */
    public void setInternationalBIN(java.lang.String internationalBIN) {
        this.internationalBIN = internationalBIN;
    }


    /**
     * Gets the durbinBINRegulation value for this EnhancedBIN.
     * 
     * @return durbinBINRegulation
     */
    public java.lang.String getDurbinBINRegulation() {
        return durbinBINRegulation;
    }


    /**
     * Sets the durbinBINRegulation value for this EnhancedBIN.
     * 
     * @param durbinBINRegulation
     */
    public void setDurbinBINRegulation(java.lang.String durbinBINRegulation) {
        this.durbinBINRegulation = durbinBINRegulation;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EnhancedBIN)) return false;
        EnhancedBIN other = (EnhancedBIN) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.debitCard==null && other.getDebitCard()==null) || 
             (this.debitCard!=null &&
              this.debitCard.equals(other.getDebitCard()))) &&
            ((this.checkCard==null && other.getCheckCard()==null) || 
             (this.checkCard!=null &&
              this.checkCard.equals(other.getCheckCard()))) &&
            ((this.creditCard==null && other.getCreditCard()==null) || 
             (this.creditCard!=null &&
              this.creditCard.equals(other.getCreditCard()))) &&
            ((this.giftCard==null && other.getGiftCard()==null) || 
             (this.giftCard!=null &&
              this.giftCard.equals(other.getGiftCard()))) &&
            ((this.commercialCard==null && other.getCommercialCard()==null) || 
             (this.commercialCard!=null &&
              this.commercialCard.equals(other.getCommercialCard()))) &&
            ((this.fleetCard==null && other.getFleetCard()==null) || 
             (this.fleetCard!=null &&
              this.fleetCard.equals(other.getFleetCard()))) &&
            ((this.prepaidCard==null && other.getPrepaidCard()==null) || 
             (this.prepaidCard!=null &&
              this.prepaidCard.equals(other.getPrepaidCard()))) &&
            ((this.HSAFSACard==null && other.getHSAFSACard()==null) || 
             (this.HSAFSACard!=null &&
              this.HSAFSACard.equals(other.getHSAFSACard()))) &&
            ((this.PINLessBillPay==null && other.getPINLessBillPay()==null) || 
             (this.PINLessBillPay!=null &&
              this.PINLessBillPay.equals(other.getPINLessBillPay()))) &&
            ((this.EBT==null && other.getEBT()==null) || 
             (this.EBT!=null &&
              this.EBT.equals(other.getEBT()))) &&
            ((this.WIC==null && other.getWIC()==null) || 
             (this.WIC!=null &&
              this.WIC.equals(other.getWIC()))) &&
            ((this.internationalBIN==null && other.getInternationalBIN()==null) || 
             (this.internationalBIN!=null &&
              this.internationalBIN.equals(other.getInternationalBIN()))) &&
            ((this.durbinBINRegulation==null && other.getDurbinBINRegulation()==null) || 
             (this.durbinBINRegulation!=null &&
              this.durbinBINRegulation.equals(other.getDurbinBINRegulation())));
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
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getDebitCard() != null) {
            _hashCode += getDebitCard().hashCode();
        }
        if (getCheckCard() != null) {
            _hashCode += getCheckCard().hashCode();
        }
        if (getCreditCard() != null) {
            _hashCode += getCreditCard().hashCode();
        }
        if (getGiftCard() != null) {
            _hashCode += getGiftCard().hashCode();
        }
        if (getCommercialCard() != null) {
            _hashCode += getCommercialCard().hashCode();
        }
        if (getFleetCard() != null) {
            _hashCode += getFleetCard().hashCode();
        }
        if (getPrepaidCard() != null) {
            _hashCode += getPrepaidCard().hashCode();
        }
        if (getHSAFSACard() != null) {
            _hashCode += getHSAFSACard().hashCode();
        }
        if (getPINLessBillPay() != null) {
            _hashCode += getPINLessBillPay().hashCode();
        }
        if (getEBT() != null) {
            _hashCode += getEBT().hashCode();
        }
        if (getWIC() != null) {
            _hashCode += getWIC().hashCode();
        }
        if (getInternationalBIN() != null) {
            _hashCode += getInternationalBIN().hashCode();
        }
        if (getDurbinBINRegulation() != null) {
            _hashCode += getDurbinBINRegulation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EnhancedBIN.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EnhancedBIN"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("debitCard");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DebitCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checkCard");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditCard");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("giftCard");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("commercialCard");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CommercialCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fleetCard");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "FleetCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prepaidCard");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PrepaidCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("HSAFSACard");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HSAFSACard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PINLessBillPay");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PINLessBillPay"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EBT");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("WIC");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "WIC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("internationalBIN");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "InternationalBIN"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("durbinBINRegulation");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DurbinBINRegulation"));
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
