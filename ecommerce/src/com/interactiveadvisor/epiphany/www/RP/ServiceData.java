/**
 * ServiceData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package  com.interactiveadvisor.epiphany.www.RP;

public class ServiceData  implements java.io.Serializable {
    private com.interactiveadvisor.epiphany.www.RP.KeyValuePair[] configData;

    private java.lang.String lockOwner;

    private java.lang.Integer version;

    private java.lang.Boolean isEditableByMe;

    private java.lang.Boolean isLockedByMe;

    public ServiceData() {
    }

    public ServiceData(
           com.interactiveadvisor.epiphany.www.RP.KeyValuePair[] configData,
           java.lang.String lockOwner,
           java.lang.Integer version,
           java.lang.Boolean isEditableByMe,
           java.lang.Boolean isLockedByMe) {
           this.configData = configData;
           this.lockOwner = lockOwner;
           this.version = version;
           this.isEditableByMe = isEditableByMe;
           this.isLockedByMe = isLockedByMe;
    }


    /**
     * Gets the configData value for this ServiceData.
     * 
     * @return configData
     */
    public com.interactiveadvisor.epiphany.www.RP.KeyValuePair[] getConfigData() {
        return configData;
    }


    /**
     * Sets the configData value for this ServiceData.
     * 
     * @param configData
     */
    public void setConfigData(com.interactiveadvisor.epiphany.www.RP.KeyValuePair[] configData) {
        this.configData = configData;
    }


    /**
     * Gets the lockOwner value for this ServiceData.
     * 
     * @return lockOwner
     */
    public java.lang.String getLockOwner() {
        return lockOwner;
    }


    /**
     * Sets the lockOwner value for this ServiceData.
     * 
     * @param lockOwner
     */
    public void setLockOwner(java.lang.String lockOwner) {
        this.lockOwner = lockOwner;
    }


    /**
     * Gets the version value for this ServiceData.
     * 
     * @return version
     */
    public java.lang.Integer getVersion() {
        return version;
    }


    /**
     * Sets the version value for this ServiceData.
     * 
     * @param version
     */
    public void setVersion(java.lang.Integer version) {
        this.version = version;
    }


    /**
     * Gets the isEditableByMe value for this ServiceData.
     * 
     * @return isEditableByMe
     */
    public java.lang.Boolean getIsEditableByMe() {
        return isEditableByMe;
    }


    /**
     * Sets the isEditableByMe value for this ServiceData.
     * 
     * @param isEditableByMe
     */
    public void setIsEditableByMe(java.lang.Boolean isEditableByMe) {
        this.isEditableByMe = isEditableByMe;
    }


    /**
     * Gets the isLockedByMe value for this ServiceData.
     * 
     * @return isLockedByMe
     */
    public java.lang.Boolean getIsLockedByMe() {
        return isLockedByMe;
    }


    /**
     * Sets the isLockedByMe value for this ServiceData.
     * 
     * @param isLockedByMe
     */
    public void setIsLockedByMe(java.lang.Boolean isLockedByMe) {
        this.isLockedByMe = isLockedByMe;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiceData)) return false;
        ServiceData other = (ServiceData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.configData==null && other.getConfigData()==null) || 
             (this.configData!=null &&
              java.util.Arrays.equals(this.configData, other.getConfigData()))) &&
            ((this.lockOwner==null && other.getLockOwner()==null) || 
             (this.lockOwner!=null &&
              this.lockOwner.equals(other.getLockOwner()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.isEditableByMe==null && other.getIsEditableByMe()==null) || 
             (this.isEditableByMe!=null &&
              this.isEditableByMe.equals(other.getIsEditableByMe()))) &&
            ((this.isLockedByMe==null && other.getIsLockedByMe()==null) || 
             (this.isLockedByMe!=null &&
              this.isLockedByMe.equals(other.getIsLockedByMe())));
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
        if (getConfigData() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getConfigData());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getConfigData(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLockOwner() != null) {
            _hashCode += getLockOwner().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getIsEditableByMe() != null) {
            _hashCode += getIsEditableByMe().hashCode();
        }
        if (getIsLockedByMe() != null) {
            _hashCode += getIsLockedByMe().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiceData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "ServiceData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("configData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "configData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "KeyValuePair"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "attribute"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lockOwner");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lockOwner"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isEditableByMe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isEditableByMe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isLockedByMe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isLockedByMe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
