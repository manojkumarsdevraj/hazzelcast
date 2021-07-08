/**
 * RealTimeService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package  com.interactiveadvisor.epiphany.www.RP;

public interface RealTimeService extends javax.xml.rpc.Service {

/**
 * Epiphany Real Time Service
 */
    public java.lang.String getRtPortAddress();

    public com.interactiveadvisor.epiphany.www.RP.RtPortType getRtPort() throws javax.xml.rpc.ServiceException;

    public com.interactiveadvisor.epiphany.www.RP.RtPortType getRtPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
