package com.unilog.express;

import java.rmi.RemoteException;

import com.paymentgateway.elementexpress.services.Address;
import com.paymentgateway.elementexpress.services.Application;
import com.paymentgateway.elementexpress.services.Card;
import com.paymentgateway.elementexpress.services.Credentials;
import com.paymentgateway.elementexpress.services.DemandDepositAccount;
import com.paymentgateway.elementexpress.services.ExpressSoap;
import com.paymentgateway.elementexpress.services.ExpressSoapProxy;
import com.paymentgateway.elementexpress.services.ExtendedParameters;
import com.paymentgateway.elementexpress.services.Paging;
import com.paymentgateway.elementexpress.services.PaymentAccount;
import com.paymentgateway.elementexpress.services.PaymentAccountParameters;
import com.paymentgateway.elementexpress.services.Response;
import com.paymentgateway.elementexpress.services.ScheduledTask;
import com.paymentgateway.elementexpress.services.ScheduledTaskParameters;
import com.paymentgateway.elementexpress.services.Terminal;
import com.paymentgateway.elementexpress.services.Token;
import com.paymentgateway.elementexpress.services.TokenCreateResponse;
import com.paymentgateway.elementexpress.services.Transaction;

public class ExpressServiceImpl implements ExpressSoap{

	@Override
	public Response paymentAccountCreateWithTransID(Credentials credentials,
			Application application, Transaction transaction,
			PaymentAccount paymentAccount, Address address,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		ExpressSoapProxy exp = new ExpressSoapProxy();
		Response resp = exp.paymentAccountCreateWithTransID(credentials, application, transaction, paymentAccount, address, extendedParameters);
		
		return resp;
	}

	@Override
	public Response paymentAccountCreate(Credentials credentials,
			Application application, PaymentAccount paymentAccount, Card card,
			DemandDepositAccount demandDepositAccount, Address address,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public TokenCreateResponse tokenCreate(Credentials credentials,
			Application application, Terminal terminal, Card card, Token token,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public TokenCreateResponse tokenCreateWithTransID(Credentials credentials,
			Application application, Terminal terminal,
			Transaction transaction, Token token,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response paymentAccountUpdate(Credentials credentials,
			Application application, PaymentAccount paymentAccount, Card card,
			DemandDepositAccount demandDepositAccount, Address address,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response paymentAccountDelete(Credentials credentials,
			Application application, PaymentAccount paymentAccount,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response paymentAccountQuery(Credentials credentials,
			Application application,
			PaymentAccountParameters paymentAccountParameters,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		ExpressSoapProxy exp = new ExpressSoapProxy();
		Response response = exp.paymentAccountQuery(credentials, application, paymentAccountParameters, extendedParameters);
		
		return response;
	}

	@Override
	public Response paymentAccountQueryRecordCount(Credentials credentials,
			Application application, ExtendedParameters[] extendedParameters)
			throws RemoteException {
		
		return null;
	}

	@Override
	public Response paymentAccountQueryTokenReport(Credentials credentials,
			Application application, Paging paging,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response paymentAccountAutoUpdate(Credentials credentials,
			Application application, PaymentAccount paymentAccount,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response scheduledTaskDelete(Credentials credentials,
			Application application, ScheduledTask scheduledTask,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response scheduledTaskQuery(Credentials credentials,
			Application application,
			ScheduledTaskParameters scheduledTaskParameters,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response scheduledTaskUpdate(Credentials credentials,
			Application application, ScheduledTask scheduledTask,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

	@Override
	public Response scheduledTaskRetry(Credentials credentials,
			Application application, ScheduledTask scheduledTask,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		
		return null;
	}

}
