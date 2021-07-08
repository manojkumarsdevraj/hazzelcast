/**
 * RtSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package  com.interactiveadvisor.epiphany.www.RP;

public class RtSoapBindingSkeleton implements com.interactiveadvisor.epiphany.www.RP.RtPortType, org.apache.axis.wsdl.Skeleton {
    private com.interactiveadvisor.epiphany.www.RP.RtPortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "input"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "retCode"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "testOutput"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("test", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "Test"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("test") == null) {
            _myOperations.put("test", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("test")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "fromServiceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "toServiceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deployCopyService", _params, new javax.xml.namespace.QName("", "retCode"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "DeployCopyService"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("deployCopyService") == null) {
            _myOperations.put("deployCopyService", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deployCopyService")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "target"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.epiphany.com/RP", "TestOrProduction"), com.interactiveadvisor.epiphany.www.RP.TestOrProduction.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deployDeployService", _params, new javax.xml.namespace.QName("", "retCode"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "DeployDeployService"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("deployDeployService") == null) {
            _myOperations.put("deployDeployService", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deployDeployService")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deployDiscardChanges", _params, new javax.xml.namespace.QName("", "retCode"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "DeployDiscardChanges"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("deployDiscardChanges") == null) {
            _myOperations.put("deployDiscardChanges", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deployDiscardChanges")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "retCode"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceData"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.epiphany.com/RP", "ServiceData"), com.interactiveadvisor.epiphany.www.RP.ServiceData.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deployGetService", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "DeployGetService"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("deployGetService") == null) {
            _myOperations.put("deployGetService", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deployGetService")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceTypeName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.epiphany.com/RP", "ServiceType"), com.interactiveadvisor.epiphany.www.RP.ServiceType.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "retCode"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "services"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.epiphany.com/RP", "ArrayOfString"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deployGetServiceNames", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "DeployGetServiceNames"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("deployGetServiceNames") == null) {
            _myOperations.put("deployGetServiceNames", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deployGetServiceNames")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deployRefineService", _params, new javax.xml.namespace.QName("", "retCode"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "DeployRefineService"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("deployRefineService") == null) {
            _myOperations.put("deployRefineService", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deployRefineService")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "serviceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deployRemoveService", _params, new javax.xml.namespace.QName("", "retCode"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "DeployRemoveService"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("deployRemoveService") == null) {
            _myOperations.put("deployRemoveService", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deployRemoveService")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "package"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "js"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "retCode"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "result"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("executeJS", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "ExecuteJS"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("executeJS") == null) {
            _myOperations.put("executeJS", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("executeJS")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "package"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "event"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "fields"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.epiphany.com/RP", "ArrayOfKeyValuePair"), com.interactiveadvisor.epiphany.www.RP.KeyValuePair[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "retCode"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "offers"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.epiphany.com/RP", "ArrayOfReturnedOffer"), com.interactiveadvisor.epiphany.www.RP.ReturnedOffer[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("processEvent", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "ProcessEvent"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("processEvent") == null) {
            _myOperations.put("processEvent", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("processEvent")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "retCode"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "majorVersion"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "minorVersion"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "majorRelease"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "minorRelease"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("serverGetVersion", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.epiphany.com/RP", "ServerGetVersion"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("serverGetVersion") == null) {
            _myOperations.put("serverGetVersion", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("serverGetVersion")).add(_oper);
    }

    public RtSoapBindingSkeleton() {
        this.impl = new com.interactiveadvisor.epiphany.www.RP.RtSoapBindingImpl();
    }

    public RtSoapBindingSkeleton(com.interactiveadvisor.epiphany.www.RP.RtPortType impl) {
        this.impl = impl;
    }
    public void test(java.lang.String input, javax.xml.rpc.holders.IntHolder retCode, javax.xml.rpc.holders.StringHolder testOutput) throws java.rmi.RemoteException
    {
        impl.test(input, retCode, testOutput);
    }

    public int deployCopyService(java.lang.String fromServiceName, java.lang.String toServiceName) throws java.rmi.RemoteException
    {
        int ret = impl.deployCopyService(fromServiceName, toServiceName);
        return ret;
    }

    public int deployDeployService(java.lang.String serviceName, com.interactiveadvisor.epiphany.www.RP.TestOrProduction target) throws java.rmi.RemoteException
    {
        int ret = impl.deployDeployService(serviceName, target);
        return ret;
    }

    public int deployDiscardChanges(java.lang.String serviceName) throws java.rmi.RemoteException
    {
        int ret = impl.deployDiscardChanges(serviceName);
        return ret;
    }

    public void deployGetService(java.lang.String serviceName, javax.xml.rpc.holders.IntHolder retCode, com.interactiveadvisor.epiphany.www.RP.holders.ServiceDataHolder serviceData) throws java.rmi.RemoteException
    {
        impl.deployGetService(serviceName, retCode, serviceData);
    }

    public void deployGetServiceNames(com.interactiveadvisor.epiphany.www.RP.ServiceType serviceTypeName, javax.xml.rpc.holders.IntHolder retCode, com.interactiveadvisor.epiphany.www.RP.holders.ArrayOfStringHolder services) throws java.rmi.RemoteException
    {
        impl.deployGetServiceNames(serviceTypeName, retCode, services);
    }

    public int deployRefineService(java.lang.String serviceName) throws java.rmi.RemoteException
    {
        int ret = impl.deployRefineService(serviceName);
        return ret;
    }

    public int deployRemoveService(java.lang.String serviceName) throws java.rmi.RemoteException
    {
        int ret = impl.deployRemoveService(serviceName);
        return ret;
    }

    public void executeJS(java.lang.String _package, java.lang.String js, java.lang.String sessionId, javax.xml.rpc.holders.IntHolder retCode, javax.xml.rpc.holders.StringHolder result) throws java.rmi.RemoteException
    {
        impl.executeJS(_package, js, sessionId, retCode, result);
    }

    public void processEvent(java.lang.String _package, java.lang.String event, com.interactiveadvisor.epiphany.www.RP.KeyValuePair[] fields, javax.xml.rpc.holders.IntHolder retCode, com.interactiveadvisor.epiphany.www.RP.holders.ArrayOfReturnedOfferHolder offers) throws java.rmi.RemoteException
    {
        impl.processEvent(_package, event, fields, retCode, offers);
    }

    public void serverGetVersion(javax.xml.rpc.holders.IntHolder retCode, javax.xml.rpc.holders.IntHolder majorVersion, javax.xml.rpc.holders.IntHolder minorVersion, javax.xml.rpc.holders.IntHolder majorRelease, javax.xml.rpc.holders.IntHolder minorRelease) throws java.rmi.RemoteException
    {
        impl.serverGetVersion(retCode, majorVersion, minorVersion, majorRelease, minorRelease);
    }

}
