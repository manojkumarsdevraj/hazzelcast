/**
 * RtPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package  com.interactiveadvisor.epiphany.www.RP;

public interface RtPortType extends java.rmi.Remote {
    public int deployCopyService(java.lang.String fromServiceName, java.lang.String toServiceName) throws java.rmi.RemoteException;
    public int deployDeployService(java.lang.String serviceName, com.interactiveadvisor.epiphany.www.RP.TestOrProduction target) throws java.rmi.RemoteException;
    public int deployDiscardChanges(java.lang.String serviceName) throws java.rmi.RemoteException;
    public void deployGetService(java.lang.String serviceName, javax.xml.rpc.holders.IntHolder retCode, com.interactiveadvisor.epiphany.www.RP.holders.ServiceDataHolder serviceData) throws java.rmi.RemoteException;
    public void deployGetServiceNames(com.interactiveadvisor.epiphany.www.RP.ServiceType serviceTypeName, javax.xml.rpc.holders.IntHolder retCode, com.interactiveadvisor.epiphany.www.RP.holders.ArrayOfStringHolder services) throws java.rmi.RemoteException;
    public int deployRefineService(java.lang.String serviceName) throws java.rmi.RemoteException;
    public int deployRemoveService(java.lang.String serviceName) throws java.rmi.RemoteException;
    public void executeJS(java.lang.String _package, java.lang.String js, java.lang.String sessionId, javax.xml.rpc.holders.IntHolder retCode, javax.xml.rpc.holders.StringHolder result) throws java.rmi.RemoteException;
    public void processEvent(java.lang.String _package, java.lang.String event, com.interactiveadvisor.epiphany.www.RP.KeyValuePair[] fields, javax.xml.rpc.holders.IntHolder retCode, com.interactiveadvisor.epiphany.www.RP.holders.ArrayOfReturnedOfferHolder offers) throws java.rmi.RemoteException;
    public void serverGetVersion(javax.xml.rpc.holders.IntHolder retCode, javax.xml.rpc.holders.IntHolder majorVersion, javax.xml.rpc.holders.IntHolder minorVersion, javax.xml.rpc.holders.IntHolder majorRelease, javax.xml.rpc.holders.IntHolder minorRelease) throws java.rmi.RemoteException;
    public void test(java.lang.String input, javax.xml.rpc.holders.IntHolder retCode, javax.xml.rpc.holders.StringHolder testOutput) throws java.rmi.RemoteException;
}
