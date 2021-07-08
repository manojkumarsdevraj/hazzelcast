/**
 * ExpressSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.reporting;

public class ExpressSoapStub extends org.apache.axis.client.Stub implements com.paymentgateway.elementexpress.reporting.ExpressSoap {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TransactionQuery");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.reporting.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Application"), com.paymentgateway.elementexpress.reporting.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "parameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Parameters"), com.paymentgateway.elementexpress.reporting.Parameters.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.reporting.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.reporting.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

    }

    public ExpressSoapStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ExpressSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ExpressSoapStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", ">TransactionQuery");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.TransactionQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", ">TransactionQueryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.TransactionQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Application");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.Application.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ArrayOfExtendedParameters");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.ExtendedParameters[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExtendedParameters");
            qName2 = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExtendedParameters");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "BooleanType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.BooleanType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Credentials");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.Credentials.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExpressResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.ExpressResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "ExtendedParameters");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.ExtendedParameters.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "LogTraceLevel");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplelistsf);
            cachedDeserFactories.add(simplelistdf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "LogTraceLevel>null");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.LogTraceLevelNull.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Parameters");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.Parameters.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "Response");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TerminalType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.reporting.TerminalType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public com.paymentgateway.elementexpress.reporting.Response transactionQuery(com.paymentgateway.elementexpress.reporting.Credentials credentials, com.paymentgateway.elementexpress.reporting.Application application, com.paymentgateway.elementexpress.reporting.Parameters parameters, com.paymentgateway.elementexpress.reporting.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://reporting.elementexpress.com/TransactionQuery");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://reporting.elementexpress.com", "TransactionQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, parameters, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.reporting.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.reporting.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.reporting.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
