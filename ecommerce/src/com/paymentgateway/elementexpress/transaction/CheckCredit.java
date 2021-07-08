/**
 * CheckCredit.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class CheckCredit  implements java.io.Serializable {
    private com.paymentgateway.elementexpress.transaction.Credentials credentials;

    private com.paymentgateway.elementexpress.transaction.Application application;

    private com.paymentgateway.elementexpress.transaction.Terminal terminal;

    private com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount;

    private com.paymentgateway.elementexpress.transaction.Transaction transaction;

    private com.paymentgateway.elementexpress.transaction.Identification identification;

    private com.paymentgateway.elementexpress.transaction.Address address;

    private com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters;

    public CheckCredit() {
    }

    public CheckCredit(
           com.paymentgateway.elementexpress.transaction.Credentials credentials,
           com.paymentgateway.elementexpress.transaction.Application application,
           com.paymentgateway.elementexpress.transaction.Terminal terminal,
           com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount,
           com.paymentgateway.elementexpress.transaction.Transaction transaction,
           com.paymentgateway.elementexpress.transaction.Identification identification,
           com.paymentgateway.elementexpress.transaction.Address address,
           com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) {
           this.credentials = credentials;
           this.application = application;
           this.terminal = terminal;
           this.demandDepositAccount = demandDepositAccount;
           this.transaction = transaction;
           this.identification = identification;
           this.address = address;
           this.extendedParameters = extendedParameters;
    }


    /**
     * Gets the credentials value for this CheckCredit.
     * 
     * @return credentials
     */
    public com.paymentgateway.elementexpress.transaction.Credentials getCredentials() {
        return credentials;
    }


    /**
     * Sets the credentials value for this CheckCredit.
     * 
     * @param credentials
     */
    public void setCredentials(com.paymentgateway.elementexpress.transaction.Credentials credentials) {
        this.credentials = credentials;
    }


    /**
     * Gets the application value for this CheckCredit.
     * 
     * @return application
     */
    public com.paymentgateway.elementexpress.transaction.Application getApplication() {
        return application;
    }


    /**
     * Sets the application value for this CheckCredit.
     * 
     * @param application
     */
    public void setApplication(com.paymentgateway.elementexpress.transaction.Application application) {
        this.application = application;
    }


    /**
     * Gets the terminal value for this CheckCredit.
     * 
     * @return terminal
     */
    public com.paymentgateway.elementexpress.transaction.Terminal getTerminal() {
        return terminal;
    }


    /**
     * Sets the terminal value for this CheckCredit.
     * 
     * @param terminal
     */
    public void setTerminal(com.paymentgateway.elementexpress.transaction.Terminal terminal) {
        this.terminal = terminal;
    }


    /**
     * Gets the demandDepositAccount value for this CheckCredit.
     * 
     * @return demandDepositAccount
     */
    public com.paymentgateway.elementexpress.transaction.DemandDepositAccount getDemandDepositAccount() {
        return demandDepositAccount;
    }


    /**
     * Sets the demandDepositAccount value for this CheckCredit.
     * 
     * @param demandDepositAccount
     */
    public void setDemandDepositAccount(com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount) {
        this.demandDepositAccount = demandDepositAccount;
    }


    /**
     * Gets the transaction value for this CheckCredit.
     * 
     * @return transaction
     */
    public com.paymentgateway.elementexpress.transaction.Transaction getTransaction() {
        return transaction;
    }


    /**
     * Sets the transaction value for this CheckCredit.
     * 
     * @param transaction
     */
    public void setTransaction(com.paymentgateway.elementexpress.transaction.Transaction transaction) {
        this.transaction = transaction;
    }


    /**
     * Gets the identification value for this CheckCredit.
     * 
     * @return identification
     */
    public com.paymentgateway.elementexpress.transaction.Identification getIdentification() {
        return identification;
    }


    /**
     * Sets the identification value for this CheckCredit.
     * 
     * @param identification
     */
    public void setIdentification(com.paymentgateway.elementexpress.transaction.Identification identification) {
        this.identification = identification;
    }


    /**
     * Gets the address value for this CheckCredit.
     * 
     * @return address
     */
    public com.paymentgateway.elementexpress.transaction.Address getAddress() {
        return address;
    }


    /**
     * Sets the address value for this CheckCredit.
     * 
     * @param address
     */
    public void setAddress(com.paymentgateway.elementexpress.transaction.Address address) {
        this.address = address;
    }


    /**
     * Gets the extendedParameters value for this CheckCredit.
     * 
     * @return extendedParameters
     */
    public com.paymentgateway.elementexpress.transaction.ExtendedParameters[] getExtendedParameters() {
        return extendedParameters;
    }


    /**
     * Sets the extendedParameters value for this CheckCredit.
     * 
     * @param extendedParameters
     */
    public void setExtendedParameters(com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) {
        this.extendedParameters = extendedParameters;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CheckCredit)) return false;
        CheckCredit other = (CheckCredit) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.credentials==null && other.getCredentials()==null) || 
             (this.credentials!=null &&
              this.credentials.equals(other.getCredentials()))) &&
            ((this.application==null && other.getApplication()==null) || 
             (this.application!=null &&
              this.application.equals(other.getApplication()))) &&
            ((this.terminal==null && other.getTerminal()==null) || 
             (this.terminal!=null &&
              this.terminal.equals(other.getTerminal()))) &&
            ((this.demandDepositAccount==null && other.getDemandDepositAccount()==null) || 
             (this.demandDepositAccount!=null &&
              this.demandDepositAccount.equals(other.getDemandDepositAccount()))) &&
            ((this.transaction==null && other.getTransaction()==null) || 
             (this.transaction!=null &&
              this.transaction.equals(other.getTransaction()))) &&
            ((this.identification==null && other.getIdentification()==null) || 
             (this.identification!=null &&
              this.identification.equals(other.getIdentification()))) &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            ((this.extendedParameters==null && other.getExtendedParameters()==null) || 
             (this.extendedParameters!=null &&
              java.util.Arrays.equals(this.extendedParameters, other.getExtendedParameters())));
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
        if (getCredentials() != null) {
            _hashCode += getCredentials().hashCode();
        }
        if (getApplication() != null) {
            _hashCode += getApplication().hashCode();
        }
        if (getTerminal() != null) {
            _hashCode += getTerminal().hashCode();
        }
        if (getDemandDepositAccount() != null) {
            _hashCode += getDemandDepositAccount().hashCode();
        }
        if (getTransaction() != null) {
            _hashCode += getTransaction().hashCode();
        }
        if (getIdentification() != null) {
            _hashCode += getIdentification().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getExtendedParameters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExtendedParameters());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExtendedParameters(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CheckCredit.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckCredit"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("credentials");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("application");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminal");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("demandDepositAccount");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "demandDepositAccount"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DemandDepositAccount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transaction");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identification");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "identification"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Identification"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "address"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extendedParameters");
        elemField.setXmlName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
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
