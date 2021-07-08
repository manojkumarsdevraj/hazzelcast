/**
 * ExpressLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.reporting;

import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

public class ExpressLocator extends org.apache.axis.client.Service implements com.paymentgateway.elementexpress.reporting.Express {

    public ExpressLocator() {
    }


    public ExpressLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ExpressLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ExpressSoap
    //production value: "https://certreporting.elementexpress.com/express.asmx";
    //staging value: "https://reporting.elementexpress.com/express.asmx"
    //private java.lang.String ExpressSoap_address = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REPORTING_EXPRESS_SOAP_ADDRESS"));
    
    String expressurl = "https://reporting.elementexpress.com/express.asmx";
    {
	    if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAYMENT_ACCOUNT_TRANSACTION_URL")).contains("cert")) {
	    	expressurl = "https://certreporting.elementexpress.com/express.asmx";
	    }
    }
    
    private java.lang.String ExpressSoap_address = expressurl;
    public java.lang.String getExpressSoapAddress() {
        return ExpressSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ExpressSoapWSDDServiceName = "ExpressSoap";

    public java.lang.String getExpressSoapWSDDServiceName() {
        return ExpressSoapWSDDServiceName;
    }

    public void setExpressSoapWSDDServiceName(java.lang.String name) {
        ExpressSoapWSDDServiceName = name;
    }

    public com.paymentgateway.elementexpress.reporting.ExpressSoap getExpressSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ExpressSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getExpressSoap(endpoint);
    }

    public com.paymentgateway.elementexpress.reporting.ExpressSoap getExpressSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.paymentgateway.elementexpress.reporting.ExpressSoapStub _stub = new com.paymentgateway.elementexpress.reporting.ExpressSoapStub(portAddress, this);
            _stub.setPortName(getExpressSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setExpressSoapEndpointAddress(java.lang.String address) {
        ExpressSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.paymentgateway.elementexpress.reporting.ExpressSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.paymentgateway.elementexpress.reporting.ExpressSoapStub _stub = new com.paymentgateway.elementexpress.reporting.ExpressSoapStub(new java.net.URL(ExpressSoap_address), this);
                _stub.setPortName(getExpressSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ExpressSoap".equals(inputPortName)) {
            return getExpressSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Express");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExpressSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ExpressSoap".equals(portName)) {
            setExpressSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
