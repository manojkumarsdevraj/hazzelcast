package com.unilog.express;

import java.rmi.RemoteException;

import com.paymentgateway.elementexpress.transaction.Address;
import com.paymentgateway.elementexpress.transaction.Application;
import com.paymentgateway.elementexpress.transaction.Batch;
import com.paymentgateway.elementexpress.transaction.Card;
import com.paymentgateway.elementexpress.transaction.Credentials;
import com.paymentgateway.elementexpress.transaction.DemandDepositAccount;
import com.paymentgateway.elementexpress.transaction.EBT;
import com.paymentgateway.elementexpress.transaction.ExpressSoap;
import com.paymentgateway.elementexpress.transaction.ExpressSoapProxy;
import com.paymentgateway.elementexpress.transaction.ExtendedParameters;
import com.paymentgateway.elementexpress.transaction.Identification;
import com.paymentgateway.elementexpress.transaction.PaymentAccount;
import com.paymentgateway.elementexpress.transaction.Response;
import com.paymentgateway.elementexpress.transaction.Terminal;
import com.paymentgateway.elementexpress.transaction.Transaction;
import com.paymentgateway.elementexpress.transaction.TransactionSetup;

public class ExpressImpl implements ExpressSoap{

	public Response healthCheck(Credentials credentials, Application application)
			throws RemoteException {
		
		return null;
	}

	public Response timeCheck(Credentials credentials, Application application)
			throws RemoteException {
		
		return null;
	}

	public Response accountTokenCreate(Credentials credentials, Application application, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response accountTokenActivate(Credentials credentials, Application application, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response magneprintDataDecrypt(Credentials credentials, Application application, Terminal terminal, Card card, ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	public Response transactionSetup(Credentials credentials, Application application, Terminal terminal, Transaction transaction, TransactionSetup transactionSetup, Address address, PaymentAccount paymentAccount, ExtendedParameters[] extendedParameters) throws RemoteException {
		ExpressSoapProxy expressSoapProxy = new ExpressSoapProxy(); 
		Response response = expressSoapProxy.transactionSetup(credentials, application, terminal, transaction, transactionSetup, address, paymentAccount, extendedParameters);
		return response;
	}

	public Response BINQuery(Credentials credentials, Application application, Terminal terminal, Card card, ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}
	
	public Response EnhancedBINQuery(Credentials credentials, Application application, Terminal terminal, Card card, ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	public Response transactionSetupExpire(Credentials credentials, Application application, TransactionSetup transactionSetup, ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	public Response creditCardSale(Credentials credentials, Application application, Terminal terminal, Card card, Transaction transaction, Address address, ExtendedParameters[] extendedParameters) throws RemoteException {
		ExpressSoapProxy expressSoapProxy = new ExpressSoapProxy(); //creditCardSale
		Response response = expressSoapProxy.creditCardSale(credentials, application, terminal, card, transaction, address, extendedParameters);
		return response;
	}

	public Response creditCardAuthorization(Credentials credentials, Application application, Terminal terminal, Card card, Transaction transaction, Address address, ExtendedParameters[] extendedParameters) throws RemoteException {
		ExpressSoapProxy expressSoapProxy = new ExpressSoapProxy(); //creditCardSale
		Response response = expressSoapProxy.creditCardAuthorization(credentials, application, terminal, card, transaction, address, extendedParameters);
		return response;
	}

	public Response creditCardAuthorizationCompletion(Credentials credentials, Application application, Terminal terminal, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response creditCardForce(Credentials credentials, Application application, Terminal terminal, Card card, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response creditCardCredit(Credentials credentials, Application application, Terminal terminal, Card card, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response creditCardAdjustment(Credentials credentials, Application application, Terminal terminal, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response creditCardReversal(Credentials credentials, Application application, Terminal terminal, Card card, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response creditCardIncrementalAuthorization(Credentials credentials, Application application, Terminal terminal, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response creditCardBalanceInquiry(Credentials credentials, Application application, Terminal terminal, Card card, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response creditCardVoid(Credentials credentials, Application application, Terminal terminal, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response creditCardAVSOnly(Credentials credentials, Application application, Terminal terminal, Card card, Transaction transaction, Address address, ExtendedParameters[] extendedParameters) throws RemoteException {
		ExpressSoapProxy expressSoapProxy = new ExpressSoapProxy(); //creditCardSale
		Response response = expressSoapProxy.creditCardAVSOnly(credentials, application, terminal, card, transaction, address, extendedParameters);
		return response;
	}

	public Response creditCardReturn(Credentials credentials, Application application, Terminal terminal, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response debitCardSale(Credentials credentials, Application application, Terminal terminal, Card card, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response debitCardReturn(Credentials credentials, Application application, Terminal terminal, Card card, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response debitCardReversal(Credentials credentials, Application application, Terminal terminal, Card card, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response batchClose(Credentials credentials, Application application, Terminal terminal, Batch batch, ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	public Response batchItemQuery(Credentials credentials, Application application, Terminal terminal, Batch batch, ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	public Response batchTotalsQuery(Credentials credentials, Application application, Terminal terminal, Batch batch, ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	public Response checkSale(Credentials credentials, Application application, Terminal terminal, DemandDepositAccount demandDepositAccount, Transaction transaction, Identification identification, Address address, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response checkCredit(Credentials credentials, Application application, Terminal terminal, DemandDepositAccount demandDepositAccount, Transaction transaction, Identification identification, Address address, ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	public Response checkVerification(Credentials credentials, Application application, Terminal terminal, DemandDepositAccount demandDepositAccount, Transaction transaction, Identification identification, Address address, ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	public Response checkReturn(Credentials credentials, Application application, Terminal terminal, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	public Response checkVoid(Credentials credentials, Application application, Terminal terminal, Transaction transaction, ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	public Response checkReversal(Credentials credentials, Application application, Terminal terminal, Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response enhancedBINQuery(Credentials credentials,
			Application application, Terminal terminal, Card card,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response debitCardPinlessSale(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response debitCardPinlessReturn(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response EBTSale(Credentials credentials, Application application,
			Terminal terminal, Card card, Transaction transaction, EBT ebt,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response EBTCredit(Credentials credentials, Application application,
			Terminal terminal, Card card, Transaction transaction, EBT ebt,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response EBTVoucher(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, EBT ebt,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response EBTBalanceInquiry(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, EBT ebt,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response EBTReversal(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, EBT ebt,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardSystemCheck(Credentials credentials,
			Application application) throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardActivate(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardSale(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardReload(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardCredit(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardReturn(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardBalanceInquiry(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardUnload(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardClose(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardAuthorization(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardAuthorizationCompletion(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardReversal(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardReport(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response giftCardBalanceTransfer(Credentials credentials,
			Application application, Terminal terminal, Card card,
			Transaction transaction, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

}
