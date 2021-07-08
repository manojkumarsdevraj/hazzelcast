/**
 * Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class Response  extends com.paymentgateway.elementexpress.transaction.ExpressResponse  implements java.io.Serializable {
    private java.lang.String hostResponseCode;

    private java.lang.String hostResponseMessage;

    private java.lang.String hostTransactionDateTime;

    private com.paymentgateway.elementexpress.transaction.Credentials credentials;

    private com.paymentgateway.elementexpress.transaction.Batch batch;

    private com.paymentgateway.elementexpress.transaction.Card card;

    private com.paymentgateway.elementexpress.transaction.Transaction transaction;

    private com.paymentgateway.elementexpress.transaction.PaymentAccount paymentAccount;

    private com.paymentgateway.elementexpress.transaction.Address address;

    private com.paymentgateway.elementexpress.transaction.ScheduledTask scheduledTask;

    private com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount;

    private com.paymentgateway.elementexpress.transaction.TransactionSetup transactionSetup;

    private com.paymentgateway.elementexpress.transaction.Terminal terminal;

    private com.paymentgateway.elementexpress.transaction.AutoRental autoRental;

    private com.paymentgateway.elementexpress.transaction.Healthcare healthcare;

    private com.paymentgateway.elementexpress.transaction.Lodging lodging;

    private com.paymentgateway.elementexpress.transaction.BIN BIN;

    private com.paymentgateway.elementexpress.transaction.EnhancedBIN enhancedBIN;

    private com.paymentgateway.elementexpress.transaction.Token token;

    public Response() {
    }

    public Response(
           java.lang.String expressResponseCode,
           java.lang.String expressResponseMessage,
           java.lang.String expressTransactionDate,
           java.lang.String expressTransactionTime,
           java.lang.String expressTransactionTimezone,
           com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters,
           java.lang.String hostResponseCode,
           java.lang.String hostResponseMessage,
           java.lang.String hostTransactionDateTime,
           com.paymentgateway.elementexpress.transaction.Credentials credentials,
           com.paymentgateway.elementexpress.transaction.Batch batch,
           com.paymentgateway.elementexpress.transaction.Card card,
           com.paymentgateway.elementexpress.transaction.Transaction transaction,
           com.paymentgateway.elementexpress.transaction.PaymentAccount paymentAccount,
           com.paymentgateway.elementexpress.transaction.Address address,
           com.paymentgateway.elementexpress.transaction.ScheduledTask scheduledTask,
           com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount,
           com.paymentgateway.elementexpress.transaction.TransactionSetup transactionSetup,
           com.paymentgateway.elementexpress.transaction.Terminal terminal,
           com.paymentgateway.elementexpress.transaction.AutoRental autoRental,
           com.paymentgateway.elementexpress.transaction.Healthcare healthcare,
           com.paymentgateway.elementexpress.transaction.Lodging lodging,
           com.paymentgateway.elementexpress.transaction.BIN BIN,
           com.paymentgateway.elementexpress.transaction.EnhancedBIN enhancedBIN,
           com.paymentgateway.elementexpress.transaction.Token token) {
        super(
            expressResponseCode,
            expressResponseMessage,
            expressTransactionDate,
            expressTransactionTime,
            expressTransactionTimezone,
            extendedParameters);
        this.hostResponseCode = hostResponseCode;
        this.hostResponseMessage = hostResponseMessage;
        this.hostTransactionDateTime = hostTransactionDateTime;
        this.credentials = credentials;
        this.batch = batch;
        this.card = card;
        this.transaction = transaction;
        this.paymentAccount = paymentAccount;
        this.address = address;
        this.scheduledTask = scheduledTask;
        this.demandDepositAccount = demandDepositAccount;
        this.transactionSetup = transactionSetup;
        this.terminal = terminal;
        this.autoRental = autoRental;
        this.healthcare = healthcare;
        this.lodging = lodging;
        this.BIN = BIN;
        this.enhancedBIN = enhancedBIN;
        this.token = token;
    }


    /**
     * Gets the hostResponseCode value for this Response.
     * 
     * @return hostResponseCode
     */
    public java.lang.String getHostResponseCode() {
        return hostResponseCode;
    }


    /**
     * Sets the hostResponseCode value for this Response.
     * 
     * @param hostResponseCode
     */
    public void setHostResponseCode(java.lang.String hostResponseCode) {
        this.hostResponseCode = hostResponseCode;
    }


    /**
     * Gets the hostResponseMessage value for this Response.
     * 
     * @return hostResponseMessage
     */
    public java.lang.String getHostResponseMessage() {
        return hostResponseMessage;
    }


    /**
     * Sets the hostResponseMessage value for this Response.
     * 
     * @param hostResponseMessage
     */
    public void setHostResponseMessage(java.lang.String hostResponseMessage) {
        this.hostResponseMessage = hostResponseMessage;
    }


    /**
     * Gets the hostTransactionDateTime value for this Response.
     * 
     * @return hostTransactionDateTime
     */
    public java.lang.String getHostTransactionDateTime() {
        return hostTransactionDateTime;
    }


    /**
     * Sets the hostTransactionDateTime value for this Response.
     * 
     * @param hostTransactionDateTime
     */
    public void setHostTransactionDateTime(java.lang.String hostTransactionDateTime) {
        this.hostTransactionDateTime = hostTransactionDateTime;
    }


    /**
     * Gets the credentials value for this Response.
     * 
     * @return credentials
     */
    public com.paymentgateway.elementexpress.transaction.Credentials getCredentials() {
        return credentials;
    }


    /**
     * Sets the credentials value for this Response.
     * 
     * @param credentials
     */
    public void setCredentials(com.paymentgateway.elementexpress.transaction.Credentials credentials) {
        this.credentials = credentials;
    }


    /**
     * Gets the batch value for this Response.
     * 
     * @return batch
     */
    public com.paymentgateway.elementexpress.transaction.Batch getBatch() {
        return batch;
    }


    /**
     * Sets the batch value for this Response.
     * 
     * @param batch
     */
    public void setBatch(com.paymentgateway.elementexpress.transaction.Batch batch) {
        this.batch = batch;
    }


    /**
     * Gets the card value for this Response.
     * 
     * @return card
     */
    public com.paymentgateway.elementexpress.transaction.Card getCard() {
        return card;
    }


    /**
     * Sets the card value for this Response.
     * 
     * @param card
     */
    public void setCard(com.paymentgateway.elementexpress.transaction.Card card) {
        this.card = card;
    }


    /**
     * Gets the transaction value for this Response.
     * 
     * @return transaction
     */
    public com.paymentgateway.elementexpress.transaction.Transaction getTransaction() {
        return transaction;
    }


    /**
     * Sets the transaction value for this Response.
     * 
     * @param transaction
     */
    public void setTransaction(com.paymentgateway.elementexpress.transaction.Transaction transaction) {
        this.transaction = transaction;
    }


    /**
     * Gets the paymentAccount value for this Response.
     * 
     * @return paymentAccount
     */
    public com.paymentgateway.elementexpress.transaction.PaymentAccount getPaymentAccount() {
        return paymentAccount;
    }


    /**
     * Sets the paymentAccount value for this Response.
     * 
     * @param paymentAccount
     */
    public void setPaymentAccount(com.paymentgateway.elementexpress.transaction.PaymentAccount paymentAccount) {
        this.paymentAccount = paymentAccount;
    }


    /**
     * Gets the address value for this Response.
     * 
     * @return address
     */
    public com.paymentgateway.elementexpress.transaction.Address getAddress() {
        return address;
    }


    /**
     * Sets the address value for this Response.
     * 
     * @param address
     */
    public void setAddress(com.paymentgateway.elementexpress.transaction.Address address) {
        this.address = address;
    }


    /**
     * Gets the scheduledTask value for this Response.
     * 
     * @return scheduledTask
     */
    public com.paymentgateway.elementexpress.transaction.ScheduledTask getScheduledTask() {
        return scheduledTask;
    }


    /**
     * Sets the scheduledTask value for this Response.
     * 
     * @param scheduledTask
     */
    public void setScheduledTask(com.paymentgateway.elementexpress.transaction.ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }


    /**
     * Gets the demandDepositAccount value for this Response.
     * 
     * @return demandDepositAccount
     */
    public com.paymentgateway.elementexpress.transaction.DemandDepositAccount getDemandDepositAccount() {
        return demandDepositAccount;
    }


    /**
     * Sets the demandDepositAccount value for this Response.
     * 
     * @param demandDepositAccount
     */
    public void setDemandDepositAccount(com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount) {
        this.demandDepositAccount = demandDepositAccount;
    }


    /**
     * Gets the transactionSetup value for this Response.
     * 
     * @return transactionSetup
     */
    public com.paymentgateway.elementexpress.transaction.TransactionSetup getTransactionSetup() {
        return transactionSetup;
    }


    /**
     * Sets the transactionSetup value for this Response.
     * 
     * @param transactionSetup
     */
    public void setTransactionSetup(com.paymentgateway.elementexpress.transaction.TransactionSetup transactionSetup) {
        this.transactionSetup = transactionSetup;
    }


    /**
     * Gets the terminal value for this Response.
     * 
     * @return terminal
     */
    public com.paymentgateway.elementexpress.transaction.Terminal getTerminal() {
        return terminal;
    }


    /**
     * Sets the terminal value for this Response.
     * 
     * @param terminal
     */
    public void setTerminal(com.paymentgateway.elementexpress.transaction.Terminal terminal) {
        this.terminal = terminal;
    }


    /**
     * Gets the autoRental value for this Response.
     * 
     * @return autoRental
     */
    public com.paymentgateway.elementexpress.transaction.AutoRental getAutoRental() {
        return autoRental;
    }


    /**
     * Sets the autoRental value for this Response.
     * 
     * @param autoRental
     */
    public void setAutoRental(com.paymentgateway.elementexpress.transaction.AutoRental autoRental) {
        this.autoRental = autoRental;
    }


    /**
     * Gets the healthcare value for this Response.
     * 
     * @return healthcare
     */
    public com.paymentgateway.elementexpress.transaction.Healthcare getHealthcare() {
        return healthcare;
    }


    /**
     * Sets the healthcare value for this Response.
     * 
     * @param healthcare
     */
    public void setHealthcare(com.paymentgateway.elementexpress.transaction.Healthcare healthcare) {
        this.healthcare = healthcare;
    }


    /**
     * Gets the lodging value for this Response.
     * 
     * @return lodging
     */
    public com.paymentgateway.elementexpress.transaction.Lodging getLodging() {
        return lodging;
    }


    /**
     * Sets the lodging value for this Response.
     * 
     * @param lodging
     */
    public void setLodging(com.paymentgateway.elementexpress.transaction.Lodging lodging) {
        this.lodging = lodging;
    }


    /**
     * Gets the BIN value for this Response.
     * 
     * @return BIN
     */
    public com.paymentgateway.elementexpress.transaction.BIN getBIN() {
        return BIN;
    }


    /**
     * Sets the BIN value for this Response.
     * 
     * @param BIN
     */
    public void setBIN(com.paymentgateway.elementexpress.transaction.BIN BIN) {
        this.BIN = BIN;
    }


    /**
     * Gets the enhancedBIN value for this Response.
     * 
     * @return enhancedBIN
     */
    public com.paymentgateway.elementexpress.transaction.EnhancedBIN getEnhancedBIN() {
        return enhancedBIN;
    }


    /**
     * Sets the enhancedBIN value for this Response.
     * 
     * @param enhancedBIN
     */
    public void setEnhancedBIN(com.paymentgateway.elementexpress.transaction.EnhancedBIN enhancedBIN) {
        this.enhancedBIN = enhancedBIN;
    }


    /**
     * Gets the token value for this Response.
     * 
     * @return token
     */
    public com.paymentgateway.elementexpress.transaction.Token getToken() {
        return token;
    }


    /**
     * Sets the token value for this Response.
     * 
     * @param token
     */
    public void setToken(com.paymentgateway.elementexpress.transaction.Token token) {
        this.token = token;
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
            ((this.hostResponseCode==null && other.getHostResponseCode()==null) || 
             (this.hostResponseCode!=null &&
              this.hostResponseCode.equals(other.getHostResponseCode()))) &&
            ((this.hostResponseMessage==null && other.getHostResponseMessage()==null) || 
             (this.hostResponseMessage!=null &&
              this.hostResponseMessage.equals(other.getHostResponseMessage()))) &&
            ((this.hostTransactionDateTime==null && other.getHostTransactionDateTime()==null) || 
             (this.hostTransactionDateTime!=null &&
              this.hostTransactionDateTime.equals(other.getHostTransactionDateTime()))) &&
            ((this.credentials==null && other.getCredentials()==null) || 
             (this.credentials!=null &&
              this.credentials.equals(other.getCredentials()))) &&
            ((this.batch==null && other.getBatch()==null) || 
             (this.batch!=null &&
              this.batch.equals(other.getBatch()))) &&
            ((this.card==null && other.getCard()==null) || 
             (this.card!=null &&
              this.card.equals(other.getCard()))) &&
            ((this.transaction==null && other.getTransaction()==null) || 
             (this.transaction!=null &&
              this.transaction.equals(other.getTransaction()))) &&
            ((this.paymentAccount==null && other.getPaymentAccount()==null) || 
             (this.paymentAccount!=null &&
              this.paymentAccount.equals(other.getPaymentAccount()))) &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            ((this.scheduledTask==null && other.getScheduledTask()==null) || 
             (this.scheduledTask!=null &&
              this.scheduledTask.equals(other.getScheduledTask()))) &&
            ((this.demandDepositAccount==null && other.getDemandDepositAccount()==null) || 
             (this.demandDepositAccount!=null &&
              this.demandDepositAccount.equals(other.getDemandDepositAccount()))) &&
            ((this.transactionSetup==null && other.getTransactionSetup()==null) || 
             (this.transactionSetup!=null &&
              this.transactionSetup.equals(other.getTransactionSetup()))) &&
            ((this.terminal==null && other.getTerminal()==null) || 
             (this.terminal!=null &&
              this.terminal.equals(other.getTerminal()))) &&
            ((this.autoRental==null && other.getAutoRental()==null) || 
             (this.autoRental!=null &&
              this.autoRental.equals(other.getAutoRental()))) &&
            ((this.healthcare==null && other.getHealthcare()==null) || 
             (this.healthcare!=null &&
              this.healthcare.equals(other.getHealthcare()))) &&
            ((this.lodging==null && other.getLodging()==null) || 
             (this.lodging!=null &&
              this.lodging.equals(other.getLodging()))) &&
            ((this.BIN==null && other.getBIN()==null) || 
             (this.BIN!=null &&
              this.BIN.equals(other.getBIN()))) &&
            ((this.enhancedBIN==null && other.getEnhancedBIN()==null) || 
             (this.enhancedBIN!=null &&
              this.enhancedBIN.equals(other.getEnhancedBIN()))) &&
            ((this.token==null && other.getToken()==null) || 
             (this.token!=null &&
              this.token.equals(other.getToken())));
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
        if (getHostResponseCode() != null) {
            _hashCode += getHostResponseCode().hashCode();
        }
        if (getHostResponseMessage() != null) {
            _hashCode += getHostResponseMessage().hashCode();
        }
        if (getHostTransactionDateTime() != null) {
            _hashCode += getHostTransactionDateTime().hashCode();
        }
        if (getCredentials() != null) {
            _hashCode += getCredentials().hashCode();
        }
        if (getBatch() != null) {
            _hashCode += getBatch().hashCode();
        }
        if (getCard() != null) {
            _hashCode += getCard().hashCode();
        }
        if (getTransaction() != null) {
            _hashCode += getTransaction().hashCode();
        }
        if (getPaymentAccount() != null) {
            _hashCode += getPaymentAccount().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getScheduledTask() != null) {
            _hashCode += getScheduledTask().hashCode();
        }
        if (getDemandDepositAccount() != null) {
            _hashCode += getDemandDepositAccount().hashCode();
        }
        if (getTransactionSetup() != null) {
            _hashCode += getTransactionSetup().hashCode();
        }
        if (getTerminal() != null) {
            _hashCode += getTerminal().hashCode();
        }
        if (getAutoRental() != null) {
            _hashCode += getAutoRental().hashCode();
        }
        if (getHealthcare() != null) {
            _hashCode += getHealthcare().hashCode();
        }
        if (getLodging() != null) {
            _hashCode += getLodging().hashCode();
        }
        if (getBIN() != null) {
            _hashCode += getBIN().hashCode();
        }
        if (getEnhancedBIN() != null) {
            _hashCode += getEnhancedBIN().hashCode();
        }
        if (getToken() != null) {
            _hashCode += getToken().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostResponseCode");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostResponseCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostResponseMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostResponseMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostTransactionDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HostTransactionDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("credentials");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batch");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Batch"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Batch"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("card");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transaction");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentAccount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PaymentAccount"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PaymentAccount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledTask");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ScheduledTask"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ScheduledTask"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("demandDepositAccount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DemandDepositAccount"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DemandDepositAccount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionSetup");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetup"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetup"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminal");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("autoRental");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AutoRental"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AutoRental"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("healthcare");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Healthcare"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Healthcare"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lodging");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Lodging"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Lodging"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BIN");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BIN"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BIN"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enhancedBIN");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EnhancedBIN"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EnhancedBIN"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("token");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Token"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Token"));
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
