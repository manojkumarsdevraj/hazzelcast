/**
 * RealTimeServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package  com.interactiveadvisor.epiphany.www.RP;

public class RealTimeServiceLocator extends org.apache.axis.client.Service implements com.interactiveadvisor.epiphany.www.RP.RealTimeService {

/**
 * Epiphany Real Time Service
 */

    public RealTimeServiceLocator() {
    }


    public RealTimeServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RealTimeServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RtPort
    private java.lang.String RtPort_address = "http://localhost:7201/SOAP";

    public java.lang.String getRtPortAddress() {
        return RtPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RtPortWSDDServiceName = "RtPort";

    public java.lang.String getRtPortWSDDServiceName() {
        return RtPortWSDDServiceName;
    }

    public void setRtPortWSDDServiceName(java.lang.String name) {
        RtPortWSDDServiceName = name;
    }

    public com.interactiveadvisor.epiphany.www.RP.RtPortType getRtPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RtPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRtPort(endpoint);
    }

    public com.interactiveadvisor.epiphany.www.RP.RtPortType getRtPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.interactiveadvisor.epiphany.www.RP.RtSoapBindingStub _stub = new com.interactiveadvisor.epiphany.www.RP.RtSoapBindingStub(portAddress, this);
            _stub.setPortName(getRtPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRtPortEndpointAddress(java.lang.String address) {
        RtPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.interactiveadvisor.epiphany.www.RP.RtPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.interactiveadvisor.epiphany.www.RP.RtSoapBindingStub _stub = new com.interactiveadvisor.epiphany.www.RP.RtSoapBindingStub(new java.net.URL(RtPort_address), this);
                _stub.setPortName(getRtPortWSDDServiceName());
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
        if ("RtPort".equals(inputPortName)) {
            return getRtPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.epiphany.com/RP", "RealTimeService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "RtPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RtPort".equals(portName)) {
            setRtPortEndpointAddress(address);
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
