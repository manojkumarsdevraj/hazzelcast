/**
 * Card.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class Card  implements java.io.Serializable {
    private java.lang.String track1Data;

    private java.lang.String track2Data;

    private java.lang.String track3Data;

    private java.lang.String magneprintData;

    private java.lang.String cardNumber;

    private java.lang.String truncatedCardNumber;

    private java.lang.String expirationMonth;

    private java.lang.String expirationYear;

    private java.lang.String cardholderName;

    private java.lang.String CVV;

    private java.lang.String CAVV;

    private java.lang.String XID;

    private java.lang.String PINBlock;

    private java.lang.String keySerialNumber;

    private com.paymentgateway.elementexpress.transaction.EncryptionFormat encryptedFormat;

    private java.lang.String encryptedTrack1Data;

    private java.lang.String encryptedTrack2Data;

    private java.lang.String encryptedCardData;

    private java.lang.String cardDataKeySerialNumber;

    private java.lang.String AVSResponseCode;

    private java.lang.String CVVResponseCode;

    private java.lang.String CAVVResponseCode;

    private java.lang.String cardLogo;

    private java.lang.String giftCardSecurityCode;

    private java.lang.String alternateCardNumber1;

    private java.lang.String alternateCardNumber2;

    private java.lang.String alternateCardNumber3;

    private java.lang.String secondaryCardNumber;

    public Card() {
    }

    public Card(
           java.lang.String track1Data,
           java.lang.String track2Data,
           java.lang.String track3Data,
           java.lang.String magneprintData,
           java.lang.String cardNumber,
           java.lang.String truncatedCardNumber,
           java.lang.String expirationMonth,
           java.lang.String expirationYear,
           java.lang.String cardholderName,
           java.lang.String CVV,
           java.lang.String CAVV,
           java.lang.String XID,
           java.lang.String PINBlock,
           java.lang.String keySerialNumber,
           com.paymentgateway.elementexpress.transaction.EncryptionFormat encryptedFormat,
           java.lang.String encryptedTrack1Data,
           java.lang.String encryptedTrack2Data,
           java.lang.String encryptedCardData,
           java.lang.String cardDataKeySerialNumber,
           java.lang.String AVSResponseCode,
           java.lang.String CVVResponseCode,
           java.lang.String CAVVResponseCode,
           java.lang.String cardLogo,
           java.lang.String giftCardSecurityCode,
           java.lang.String alternateCardNumber1,
           java.lang.String alternateCardNumber2,
           java.lang.String alternateCardNumber3,
           java.lang.String secondaryCardNumber) {
           this.track1Data = track1Data;
           this.track2Data = track2Data;
           this.track3Data = track3Data;
           this.magneprintData = magneprintData;
           this.cardNumber = cardNumber;
           this.truncatedCardNumber = truncatedCardNumber;
           this.expirationMonth = expirationMonth;
           this.expirationYear = expirationYear;
           this.cardholderName = cardholderName;
           this.CVV = CVV;
           this.CAVV = CAVV;
           this.XID = XID;
           this.PINBlock = PINBlock;
           this.keySerialNumber = keySerialNumber;
           this.encryptedFormat = encryptedFormat;
           this.encryptedTrack1Data = encryptedTrack1Data;
           this.encryptedTrack2Data = encryptedTrack2Data;
           this.encryptedCardData = encryptedCardData;
           this.cardDataKeySerialNumber = cardDataKeySerialNumber;
           this.AVSResponseCode = AVSResponseCode;
           this.CVVResponseCode = CVVResponseCode;
           this.CAVVResponseCode = CAVVResponseCode;
           this.cardLogo = cardLogo;
           this.giftCardSecurityCode = giftCardSecurityCode;
           this.alternateCardNumber1 = alternateCardNumber1;
           this.alternateCardNumber2 = alternateCardNumber2;
           this.alternateCardNumber3 = alternateCardNumber3;
           this.secondaryCardNumber = secondaryCardNumber;
    }


    /**
     * Gets the track1Data value for this Card.
     * 
     * @return track1Data
     */
    public java.lang.String getTrack1Data() {
        return track1Data;
    }


    /**
     * Sets the track1Data value for this Card.
     * 
     * @param track1Data
     */
    public void setTrack1Data(java.lang.String track1Data) {
        this.track1Data = track1Data;
    }


    /**
     * Gets the track2Data value for this Card.
     * 
     * @return track2Data
     */
    public java.lang.String getTrack2Data() {
        return track2Data;
    }


    /**
     * Sets the track2Data value for this Card.
     * 
     * @param track2Data
     */
    public void setTrack2Data(java.lang.String track2Data) {
        this.track2Data = track2Data;
    }


    /**
     * Gets the track3Data value for this Card.
     * 
     * @return track3Data
     */
    public java.lang.String getTrack3Data() {
        return track3Data;
    }


    /**
     * Sets the track3Data value for this Card.
     * 
     * @param track3Data
     */
    public void setTrack3Data(java.lang.String track3Data) {
        this.track3Data = track3Data;
    }


    /**
     * Gets the magneprintData value for this Card.
     * 
     * @return magneprintData
     */
    public java.lang.String getMagneprintData() {
        return magneprintData;
    }


    /**
     * Sets the magneprintData value for this Card.
     * 
     * @param magneprintData
     */
    public void setMagneprintData(java.lang.String magneprintData) {
        this.magneprintData = magneprintData;
    }


    /**
     * Gets the cardNumber value for this Card.
     * 
     * @return cardNumber
     */
    public java.lang.String getCardNumber() {
        return cardNumber;
    }


    /**
     * Sets the cardNumber value for this Card.
     * 
     * @param cardNumber
     */
    public void setCardNumber(java.lang.String cardNumber) {
        this.cardNumber = cardNumber;
    }


    /**
     * Gets the truncatedCardNumber value for this Card.
     * 
     * @return truncatedCardNumber
     */
    public java.lang.String getTruncatedCardNumber() {
        return truncatedCardNumber;
    }


    /**
     * Sets the truncatedCardNumber value for this Card.
     * 
     * @param truncatedCardNumber
     */
    public void setTruncatedCardNumber(java.lang.String truncatedCardNumber) {
        this.truncatedCardNumber = truncatedCardNumber;
    }


    /**
     * Gets the expirationMonth value for this Card.
     * 
     * @return expirationMonth
     */
    public java.lang.String getExpirationMonth() {
        return expirationMonth;
    }


    /**
     * Sets the expirationMonth value for this Card.
     * 
     * @param expirationMonth
     */
    public void setExpirationMonth(java.lang.String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }


    /**
     * Gets the expirationYear value for this Card.
     * 
     * @return expirationYear
     */
    public java.lang.String getExpirationYear() {
        return expirationYear;
    }


    /**
     * Sets the expirationYear value for this Card.
     * 
     * @param expirationYear
     */
    public void setExpirationYear(java.lang.String expirationYear) {
        this.expirationYear = expirationYear;
    }


    /**
     * Gets the cardholderName value for this Card.
     * 
     * @return cardholderName
     */
    public java.lang.String getCardholderName() {
        return cardholderName;
    }


    /**
     * Sets the cardholderName value for this Card.
     * 
     * @param cardholderName
     */
    public void setCardholderName(java.lang.String cardholderName) {
        this.cardholderName = cardholderName;
    }


    /**
     * Gets the CVV value for this Card.
     * 
     * @return CVV
     */
    public java.lang.String getCVV() {
        return CVV;
    }


    /**
     * Sets the CVV value for this Card.
     * 
     * @param CVV
     */
    public void setCVV(java.lang.String CVV) {
        this.CVV = CVV;
    }


    /**
     * Gets the CAVV value for this Card.
     * 
     * @return CAVV
     */
    public java.lang.String getCAVV() {
        return CAVV;
    }


    /**
     * Sets the CAVV value for this Card.
     * 
     * @param CAVV
     */
    public void setCAVV(java.lang.String CAVV) {
        this.CAVV = CAVV;
    }


    /**
     * Gets the XID value for this Card.
     * 
     * @return XID
     */
    public java.lang.String getXID() {
        return XID;
    }


    /**
     * Sets the XID value for this Card.
     * 
     * @param XID
     */
    public void setXID(java.lang.String XID) {
        this.XID = XID;
    }


    /**
     * Gets the PINBlock value for this Card.
     * 
     * @return PINBlock
     */
    public java.lang.String getPINBlock() {
        return PINBlock;
    }


    /**
     * Sets the PINBlock value for this Card.
     * 
     * @param PINBlock
     */
    public void setPINBlock(java.lang.String PINBlock) {
        this.PINBlock = PINBlock;
    }


    /**
     * Gets the keySerialNumber value for this Card.
     * 
     * @return keySerialNumber
     */
    public java.lang.String getKeySerialNumber() {
        return keySerialNumber;
    }


    /**
     * Sets the keySerialNumber value for this Card.
     * 
     * @param keySerialNumber
     */
    public void setKeySerialNumber(java.lang.String keySerialNumber) {
        this.keySerialNumber = keySerialNumber;
    }


    /**
     * Gets the encryptedFormat value for this Card.
     * 
     * @return encryptedFormat
     */
    public com.paymentgateway.elementexpress.transaction.EncryptionFormat getEncryptedFormat() {
        return encryptedFormat;
    }


    /**
     * Sets the encryptedFormat value for this Card.
     * 
     * @param encryptedFormat
     */
    public void setEncryptedFormat(com.paymentgateway.elementexpress.transaction.EncryptionFormat encryptedFormat) {
        this.encryptedFormat = encryptedFormat;
    }


    /**
     * Gets the encryptedTrack1Data value for this Card.
     * 
     * @return encryptedTrack1Data
     */
    public java.lang.String getEncryptedTrack1Data() {
        return encryptedTrack1Data;
    }


    /**
     * Sets the encryptedTrack1Data value for this Card.
     * 
     * @param encryptedTrack1Data
     */
    public void setEncryptedTrack1Data(java.lang.String encryptedTrack1Data) {
        this.encryptedTrack1Data = encryptedTrack1Data;
    }


    /**
     * Gets the encryptedTrack2Data value for this Card.
     * 
     * @return encryptedTrack2Data
     */
    public java.lang.String getEncryptedTrack2Data() {
        return encryptedTrack2Data;
    }


    /**
     * Sets the encryptedTrack2Data value for this Card.
     * 
     * @param encryptedTrack2Data
     */
    public void setEncryptedTrack2Data(java.lang.String encryptedTrack2Data) {
        this.encryptedTrack2Data = encryptedTrack2Data;
    }


    /**
     * Gets the encryptedCardData value for this Card.
     * 
     * @return encryptedCardData
     */
    public java.lang.String getEncryptedCardData() {
        return encryptedCardData;
    }


    /**
     * Sets the encryptedCardData value for this Card.
     * 
     * @param encryptedCardData
     */
    public void setEncryptedCardData(java.lang.String encryptedCardData) {
        this.encryptedCardData = encryptedCardData;
    }


    /**
     * Gets the cardDataKeySerialNumber value for this Card.
     * 
     * @return cardDataKeySerialNumber
     */
    public java.lang.String getCardDataKeySerialNumber() {
        return cardDataKeySerialNumber;
    }


    /**
     * Sets the cardDataKeySerialNumber value for this Card.
     * 
     * @param cardDataKeySerialNumber
     */
    public void setCardDataKeySerialNumber(java.lang.String cardDataKeySerialNumber) {
        this.cardDataKeySerialNumber = cardDataKeySerialNumber;
    }


    /**
     * Gets the AVSResponseCode value for this Card.
     * 
     * @return AVSResponseCode
     */
    public java.lang.String getAVSResponseCode() {
        return AVSResponseCode;
    }


    /**
     * Sets the AVSResponseCode value for this Card.
     * 
     * @param AVSResponseCode
     */
    public void setAVSResponseCode(java.lang.String AVSResponseCode) {
        this.AVSResponseCode = AVSResponseCode;
    }


    /**
     * Gets the CVVResponseCode value for this Card.
     * 
     * @return CVVResponseCode
     */
    public java.lang.String getCVVResponseCode() {
        return CVVResponseCode;
    }


    /**
     * Sets the CVVResponseCode value for this Card.
     * 
     * @param CVVResponseCode
     */
    public void setCVVResponseCode(java.lang.String CVVResponseCode) {
        this.CVVResponseCode = CVVResponseCode;
    }


    /**
     * Gets the CAVVResponseCode value for this Card.
     * 
     * @return CAVVResponseCode
     */
    public java.lang.String getCAVVResponseCode() {
        return CAVVResponseCode;
    }


    /**
     * Sets the CAVVResponseCode value for this Card.
     * 
     * @param CAVVResponseCode
     */
    public void setCAVVResponseCode(java.lang.String CAVVResponseCode) {
        this.CAVVResponseCode = CAVVResponseCode;
    }


    /**
     * Gets the cardLogo value for this Card.
     * 
     * @return cardLogo
     */
    public java.lang.String getCardLogo() {
        return cardLogo;
    }


    /**
     * Sets the cardLogo value for this Card.
     * 
     * @param cardLogo
     */
    public void setCardLogo(java.lang.String cardLogo) {
        this.cardLogo = cardLogo;
    }


    /**
     * Gets the giftCardSecurityCode value for this Card.
     * 
     * @return giftCardSecurityCode
     */
    public java.lang.String getGiftCardSecurityCode() {
        return giftCardSecurityCode;
    }


    /**
     * Sets the giftCardSecurityCode value for this Card.
     * 
     * @param giftCardSecurityCode
     */
    public void setGiftCardSecurityCode(java.lang.String giftCardSecurityCode) {
        this.giftCardSecurityCode = giftCardSecurityCode;
    }


    /**
     * Gets the alternateCardNumber1 value for this Card.
     * 
     * @return alternateCardNumber1
     */
    public java.lang.String getAlternateCardNumber1() {
        return alternateCardNumber1;
    }


    /**
     * Sets the alternateCardNumber1 value for this Card.
     * 
     * @param alternateCardNumber1
     */
    public void setAlternateCardNumber1(java.lang.String alternateCardNumber1) {
        this.alternateCardNumber1 = alternateCardNumber1;
    }


    /**
     * Gets the alternateCardNumber2 value for this Card.
     * 
     * @return alternateCardNumber2
     */
    public java.lang.String getAlternateCardNumber2() {
        return alternateCardNumber2;
    }


    /**
     * Sets the alternateCardNumber2 value for this Card.
     * 
     * @param alternateCardNumber2
     */
    public void setAlternateCardNumber2(java.lang.String alternateCardNumber2) {
        this.alternateCardNumber2 = alternateCardNumber2;
    }


    /**
     * Gets the alternateCardNumber3 value for this Card.
     * 
     * @return alternateCardNumber3
     */
    public java.lang.String getAlternateCardNumber3() {
        return alternateCardNumber3;
    }


    /**
     * Sets the alternateCardNumber3 value for this Card.
     * 
     * @param alternateCardNumber3
     */
    public void setAlternateCardNumber3(java.lang.String alternateCardNumber3) {
        this.alternateCardNumber3 = alternateCardNumber3;
    }


    /**
     * Gets the secondaryCardNumber value for this Card.
     * 
     * @return secondaryCardNumber
     */
    public java.lang.String getSecondaryCardNumber() {
        return secondaryCardNumber;
    }


    /**
     * Sets the secondaryCardNumber value for this Card.
     * 
     * @param secondaryCardNumber
     */
    public void setSecondaryCardNumber(java.lang.String secondaryCardNumber) {
        this.secondaryCardNumber = secondaryCardNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Card)) return false;
        Card other = (Card) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.track1Data==null && other.getTrack1Data()==null) || 
             (this.track1Data!=null &&
              this.track1Data.equals(other.getTrack1Data()))) &&
            ((this.track2Data==null && other.getTrack2Data()==null) || 
             (this.track2Data!=null &&
              this.track2Data.equals(other.getTrack2Data()))) &&
            ((this.track3Data==null && other.getTrack3Data()==null) || 
             (this.track3Data!=null &&
              this.track3Data.equals(other.getTrack3Data()))) &&
            ((this.magneprintData==null && other.getMagneprintData()==null) || 
             (this.magneprintData!=null &&
              this.magneprintData.equals(other.getMagneprintData()))) &&
            ((this.cardNumber==null && other.getCardNumber()==null) || 
             (this.cardNumber!=null &&
              this.cardNumber.equals(other.getCardNumber()))) &&
            ((this.truncatedCardNumber==null && other.getTruncatedCardNumber()==null) || 
             (this.truncatedCardNumber!=null &&
              this.truncatedCardNumber.equals(other.getTruncatedCardNumber()))) &&
            ((this.expirationMonth==null && other.getExpirationMonth()==null) || 
             (this.expirationMonth!=null &&
              this.expirationMonth.equals(other.getExpirationMonth()))) &&
            ((this.expirationYear==null && other.getExpirationYear()==null) || 
             (this.expirationYear!=null &&
              this.expirationYear.equals(other.getExpirationYear()))) &&
            ((this.cardholderName==null && other.getCardholderName()==null) || 
             (this.cardholderName!=null &&
              this.cardholderName.equals(other.getCardholderName()))) &&
            ((this.CVV==null && other.getCVV()==null) || 
             (this.CVV!=null &&
              this.CVV.equals(other.getCVV()))) &&
            ((this.CAVV==null && other.getCAVV()==null) || 
             (this.CAVV!=null &&
              this.CAVV.equals(other.getCAVV()))) &&
            ((this.XID==null && other.getXID()==null) || 
             (this.XID!=null &&
              this.XID.equals(other.getXID()))) &&
            ((this.PINBlock==null && other.getPINBlock()==null) || 
             (this.PINBlock!=null &&
              this.PINBlock.equals(other.getPINBlock()))) &&
            ((this.keySerialNumber==null && other.getKeySerialNumber()==null) || 
             (this.keySerialNumber!=null &&
              this.keySerialNumber.equals(other.getKeySerialNumber()))) &&
            ((this.encryptedFormat==null && other.getEncryptedFormat()==null) || 
             (this.encryptedFormat!=null &&
              this.encryptedFormat.equals(other.getEncryptedFormat()))) &&
            ((this.encryptedTrack1Data==null && other.getEncryptedTrack1Data()==null) || 
             (this.encryptedTrack1Data!=null &&
              this.encryptedTrack1Data.equals(other.getEncryptedTrack1Data()))) &&
            ((this.encryptedTrack2Data==null && other.getEncryptedTrack2Data()==null) || 
             (this.encryptedTrack2Data!=null &&
              this.encryptedTrack2Data.equals(other.getEncryptedTrack2Data()))) &&
            ((this.encryptedCardData==null && other.getEncryptedCardData()==null) || 
             (this.encryptedCardData!=null &&
              this.encryptedCardData.equals(other.getEncryptedCardData()))) &&
            ((this.cardDataKeySerialNumber==null && other.getCardDataKeySerialNumber()==null) || 
             (this.cardDataKeySerialNumber!=null &&
              this.cardDataKeySerialNumber.equals(other.getCardDataKeySerialNumber()))) &&
            ((this.AVSResponseCode==null && other.getAVSResponseCode()==null) || 
             (this.AVSResponseCode!=null &&
              this.AVSResponseCode.equals(other.getAVSResponseCode()))) &&
            ((this.CVVResponseCode==null && other.getCVVResponseCode()==null) || 
             (this.CVVResponseCode!=null &&
              this.CVVResponseCode.equals(other.getCVVResponseCode()))) &&
            ((this.CAVVResponseCode==null && other.getCAVVResponseCode()==null) || 
             (this.CAVVResponseCode!=null &&
              this.CAVVResponseCode.equals(other.getCAVVResponseCode()))) &&
            ((this.cardLogo==null && other.getCardLogo()==null) || 
             (this.cardLogo!=null &&
              this.cardLogo.equals(other.getCardLogo()))) &&
            ((this.giftCardSecurityCode==null && other.getGiftCardSecurityCode()==null) || 
             (this.giftCardSecurityCode!=null &&
              this.giftCardSecurityCode.equals(other.getGiftCardSecurityCode()))) &&
            ((this.alternateCardNumber1==null && other.getAlternateCardNumber1()==null) || 
             (this.alternateCardNumber1!=null &&
              this.alternateCardNumber1.equals(other.getAlternateCardNumber1()))) &&
            ((this.alternateCardNumber2==null && other.getAlternateCardNumber2()==null) || 
             (this.alternateCardNumber2!=null &&
              this.alternateCardNumber2.equals(other.getAlternateCardNumber2()))) &&
            ((this.alternateCardNumber3==null && other.getAlternateCardNumber3()==null) || 
             (this.alternateCardNumber3!=null &&
              this.alternateCardNumber3.equals(other.getAlternateCardNumber3()))) &&
            ((this.secondaryCardNumber==null && other.getSecondaryCardNumber()==null) || 
             (this.secondaryCardNumber!=null &&
              this.secondaryCardNumber.equals(other.getSecondaryCardNumber())));
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
        if (getTrack1Data() != null) {
            _hashCode += getTrack1Data().hashCode();
        }
        if (getTrack2Data() != null) {
            _hashCode += getTrack2Data().hashCode();
        }
        if (getTrack3Data() != null) {
            _hashCode += getTrack3Data().hashCode();
        }
        if (getMagneprintData() != null) {
            _hashCode += getMagneprintData().hashCode();
        }
        if (getCardNumber() != null) {
            _hashCode += getCardNumber().hashCode();
        }
        if (getTruncatedCardNumber() != null) {
            _hashCode += getTruncatedCardNumber().hashCode();
        }
        if (getExpirationMonth() != null) {
            _hashCode += getExpirationMonth().hashCode();
        }
        if (getExpirationYear() != null) {
            _hashCode += getExpirationYear().hashCode();
        }
        if (getCardholderName() != null) {
            _hashCode += getCardholderName().hashCode();
        }
        if (getCVV() != null) {
            _hashCode += getCVV().hashCode();
        }
        if (getCAVV() != null) {
            _hashCode += getCAVV().hashCode();
        }
        if (getXID() != null) {
            _hashCode += getXID().hashCode();
        }
        if (getPINBlock() != null) {
            _hashCode += getPINBlock().hashCode();
        }
        if (getKeySerialNumber() != null) {
            _hashCode += getKeySerialNumber().hashCode();
        }
        if (getEncryptedFormat() != null) {
            _hashCode += getEncryptedFormat().hashCode();
        }
        if (getEncryptedTrack1Data() != null) {
            _hashCode += getEncryptedTrack1Data().hashCode();
        }
        if (getEncryptedTrack2Data() != null) {
            _hashCode += getEncryptedTrack2Data().hashCode();
        }
        if (getEncryptedCardData() != null) {
            _hashCode += getEncryptedCardData().hashCode();
        }
        if (getCardDataKeySerialNumber() != null) {
            _hashCode += getCardDataKeySerialNumber().hashCode();
        }
        if (getAVSResponseCode() != null) {
            _hashCode += getAVSResponseCode().hashCode();
        }
        if (getCVVResponseCode() != null) {
            _hashCode += getCVVResponseCode().hashCode();
        }
        if (getCAVVResponseCode() != null) {
            _hashCode += getCAVVResponseCode().hashCode();
        }
        if (getCardLogo() != null) {
            _hashCode += getCardLogo().hashCode();
        }
        if (getGiftCardSecurityCode() != null) {
            _hashCode += getGiftCardSecurityCode().hashCode();
        }
        if (getAlternateCardNumber1() != null) {
            _hashCode += getAlternateCardNumber1().hashCode();
        }
        if (getAlternateCardNumber2() != null) {
            _hashCode += getAlternateCardNumber2().hashCode();
        }
        if (getAlternateCardNumber3() != null) {
            _hashCode += getAlternateCardNumber3().hashCode();
        }
        if (getSecondaryCardNumber() != null) {
            _hashCode += getSecondaryCardNumber().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Card.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("track1Data");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Track1Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("track2Data");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Track2Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("track3Data");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Track3Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("magneprintData");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "MagneprintData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cardNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CardNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("truncatedCardNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TruncatedCardNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expirationMonth");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExpirationMonth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expirationYear");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExpirationYear"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cardholderName");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CardholderName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CVV");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CVV"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CAVV");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CAVV"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "XID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PINBlock");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PINBlock"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keySerialNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "KeySerialNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encryptedFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EncryptedFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EncryptionFormat"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encryptedTrack1Data");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EncryptedTrack1Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encryptedTrack2Data");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EncryptedTrack2Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encryptedCardData");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EncryptedCardData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cardDataKeySerialNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CardDataKeySerialNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AVSResponseCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AVSResponseCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CVVResponseCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CVVResponseCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CAVVResponseCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CAVVResponseCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cardLogo");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CardLogo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("giftCardSecurityCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardSecurityCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alternateCardNumber1");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AlternateCardNumber1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alternateCardNumber2");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AlternateCardNumber2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alternateCardNumber3");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AlternateCardNumber3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("secondaryCardNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "SecondaryCardNumber"));
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
