package com.paymentgateway.elementexpress.services;

public class ExpressSoapProxy implements com.paymentgateway.elementexpress.services.ExpressSoap {
  private String _endpoint = null;
  private com.paymentgateway.elementexpress.services.ExpressSoap expressSoap = null;
  
  public ExpressSoapProxy() {
    _initExpressSoapProxy();
  }
  
  public ExpressSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initExpressSoapProxy();
  }
  
  private void _initExpressSoapProxy() {
    try {
      expressSoap = (new com.paymentgateway.elementexpress.services.ExpressLocator()).getExpressSoap();
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
  
  public com.paymentgateway.elementexpress.services.ExpressSoap getExpressSoap() {
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap;
  }
  
  public com.paymentgateway.elementexpress.services.Response paymentAccountCreateWithTransID(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.Transaction transaction, com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.services.Address address, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.paymentAccountCreateWithTransID(credentials, application, transaction, paymentAccount, address, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response paymentAccountCreate(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.services.Card card, com.paymentgateway.elementexpress.services.DemandDepositAccount demandDepositAccount, com.paymentgateway.elementexpress.services.Address address, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.paymentAccountCreate(credentials, application, paymentAccount, card, demandDepositAccount, address, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.TokenCreateResponse tokenCreate(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.Terminal terminal, com.paymentgateway.elementexpress.services.Card card, com.paymentgateway.elementexpress.services.Token token, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.tokenCreate(credentials, application, terminal, card, token, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.TokenCreateResponse tokenCreateWithTransID(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.Terminal terminal, com.paymentgateway.elementexpress.services.Transaction transaction, com.paymentgateway.elementexpress.services.Token token, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.tokenCreateWithTransID(credentials, application, terminal, transaction, token, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response paymentAccountUpdate(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.services.Card card, com.paymentgateway.elementexpress.services.DemandDepositAccount demandDepositAccount, com.paymentgateway.elementexpress.services.Address address, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.paymentAccountUpdate(credentials, application, paymentAccount, card, demandDepositAccount, address, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response paymentAccountDelete(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.paymentAccountDelete(credentials, application, paymentAccount, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response paymentAccountQuery(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.PaymentAccountParameters paymentAccountParameters, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.paymentAccountQuery(credentials, application, paymentAccountParameters, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response paymentAccountQueryRecordCount(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.paymentAccountQueryRecordCount(credentials, application, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response paymentAccountQueryTokenReport(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.Paging paging, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.paymentAccountQueryTokenReport(credentials, application, paging, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response paymentAccountAutoUpdate(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.paymentAccountAutoUpdate(credentials, application, paymentAccount, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response scheduledTaskDelete(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.ScheduledTask scheduledTask, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.scheduledTaskDelete(credentials, application, scheduledTask, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response scheduledTaskQuery(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.ScheduledTaskParameters scheduledTaskParameters, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.scheduledTaskQuery(credentials, application, scheduledTaskParameters, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response scheduledTaskUpdate(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.ScheduledTask scheduledTask, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.scheduledTaskUpdate(credentials, application, scheduledTask, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.services.Response scheduledTaskRetry(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.ScheduledTask scheduledTask, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.scheduledTaskRetry(credentials, application, scheduledTask, extendedParameters);
  }
  
  
}