package com.paymentgateway.elementexpress.reporting;

public class ExpressSoapProxy implements com.paymentgateway.elementexpress.reporting.ExpressSoap {
  private String _endpoint = null;
  private com.paymentgateway.elementexpress.reporting.ExpressSoap expressSoap = null;
  
  public ExpressSoapProxy() {
    _initExpressSoapProxy();
  }
  
  public ExpressSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initExpressSoapProxy();
  }
  
  private void _initExpressSoapProxy() {
    try {
      expressSoap = (new com.paymentgateway.elementexpress.reporting.ExpressLocator()).getExpressSoap();
      if (expressSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)expressSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)expressSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (expressSoap != null)
      ((javax.xml.rpc.Stub)expressSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.paymentgateway.elementexpress.reporting.ExpressSoap getExpressSoap() {
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap;
  }
  
  public com.paymentgateway.elementexpress.reporting.Response transactionQuery(com.paymentgateway.elementexpress.reporting.Credentials credentials, com.paymentgateway.elementexpress.reporting.Application application, com.paymentgateway.elementexpress.reporting.Parameters parameters, com.paymentgateway.elementexpress.reporting.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.transactionQuery(credentials, application, parameters, extendedParameters);
  }
  
  
}