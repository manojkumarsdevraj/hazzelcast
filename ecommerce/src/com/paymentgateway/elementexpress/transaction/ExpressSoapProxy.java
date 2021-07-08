package com.paymentgateway.elementexpress.transaction;

public class ExpressSoapProxy implements com.paymentgateway.elementexpress.transaction.ExpressSoap {
  private String _endpoint = null;
  private com.paymentgateway.elementexpress.transaction.ExpressSoap expressSoap = null;
  
  public ExpressSoapProxy() {
    _initExpressSoapProxy();
  }
  
  public ExpressSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initExpressSoapProxy();
  }
  
  private void _initExpressSoapProxy() {
    try {
      expressSoap = (new com.paymentgateway.elementexpress.transaction.ExpressLocator()).getExpressSoap();
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
  
  public com.paymentgateway.elementexpress.transaction.ExpressSoap getExpressSoap() {
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap;
  }
  
  public com.paymentgateway.elementexpress.transaction.Response healthCheck(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.healthCheck(credentials, application);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response timeCheck(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.timeCheck(credentials, application);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response accountTokenCreate(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.accountTokenCreate(credentials, application, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response accountTokenActivate(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.accountTokenActivate(credentials, application, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response magneprintDataDecrypt(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.magneprintDataDecrypt(credentials, application, terminal, card, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response transactionSetup(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.TransactionSetup transactionSetup, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.transactionSetup(credentials, application, terminal, transaction, transactionSetup, address, paymentAccount, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response BINQuery(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.BINQuery(credentials, application, terminal, card, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response enhancedBINQuery(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.enhancedBINQuery(credentials, application, terminal, card, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response transactionSetupExpire(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.TransactionSetup transactionSetup, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.transactionSetupExpire(credentials, application, transactionSetup, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardSale(credentials, application, terminal, card, transaction, address, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardAuthorization(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardAuthorization(credentials, application, terminal, card, transaction, address, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardAuthorizationCompletion(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardAuthorizationCompletion(credentials, application, terminal, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardForce(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardForce(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardCredit(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardCredit(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardAdjustment(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardAdjustment(credentials, application, terminal, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardReversal(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardReversal(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardIncrementalAuthorization(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardIncrementalAuthorization(credentials, application, terminal, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardBalanceInquiry(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardBalanceInquiry(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardVoid(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardVoid(credentials, application, terminal, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardAVSOnly(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardAVSOnly(credentials, application, terminal, card, transaction, address, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response creditCardReturn(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.creditCardReturn(credentials, application, terminal, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response debitCardSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.debitCardSale(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response debitCardReturn(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.debitCardReturn(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response debitCardReversal(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.debitCardReversal(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response debitCardPinlessSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.debitCardPinlessSale(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response debitCardPinlessReturn(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.debitCardPinlessReturn(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response batchClose(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Batch batch, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.batchClose(credentials, application, terminal, batch, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response batchItemQuery(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Batch batch, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.batchItemQuery(credentials, application, terminal, batch, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response batchTotalsQuery(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Batch batch, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.batchTotalsQuery(credentials, application, terminal, batch, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response checkSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Identification identification, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.checkSale(credentials, application, terminal, demandDepositAccount, transaction, identification, address, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response checkCredit(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Identification identification, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.checkCredit(credentials, application, terminal, demandDepositAccount, transaction, identification, address, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response checkVerification(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Identification identification, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.checkVerification(credentials, application, terminal, demandDepositAccount, transaction, identification, address, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response checkReturn(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.checkReturn(credentials, application, terminal, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response checkVoid(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.checkVoid(credentials, application, terminal, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response checkReversal(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.checkReversal(credentials, application, terminal, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response EBTSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.EBT ebt, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.EBTSale(credentials, application, terminal, card, transaction, ebt, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response EBTCredit(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.EBT ebt, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.EBTCredit(credentials, application, terminal, card, transaction, ebt, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response EBTVoucher(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.EBT ebt, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.EBTVoucher(credentials, application, terminal, card, transaction, ebt, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response EBTBalanceInquiry(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.EBT ebt, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.EBTBalanceInquiry(credentials, application, terminal, card, transaction, ebt, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response EBTReversal(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.EBT ebt, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.EBTReversal(credentials, application, terminal, card, transaction, ebt, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardSystemCheck(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardSystemCheck(credentials, application);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardActivate(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardActivate(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardSale(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardReload(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardReload(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardCredit(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardCredit(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardReturn(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardReturn(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardBalanceInquiry(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardBalanceInquiry(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardUnload(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardUnload(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardClose(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardClose(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardAuthorization(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardAuthorization(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardAuthorizationCompletion(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardAuthorizationCompletion(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardReversal(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardReversal(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardReport(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardReport(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  public com.paymentgateway.elementexpress.transaction.Response giftCardBalanceTransfer(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException{
    if (expressSoap == null)
      _initExpressSoapProxy();
    return expressSoap.giftCardBalanceTransfer(credentials, application, terminal, card, transaction, extendedParameters);
  }
  
  
}