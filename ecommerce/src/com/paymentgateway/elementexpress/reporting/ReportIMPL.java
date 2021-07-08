package com.paymentgateway.elementexpress.reporting;

import java.rmi.RemoteException;

public class ReportIMPL  implements ExpressSoap{

	@Override
	public Response transactionQuery(Credentials credentials,
			Application application, Parameters parameters,
			ExtendedParameters[] extendedParameters) throws RemoteException {
		ExpressSoapProxy expressSoapProxy = new ExpressSoapProxy(); 
		Response response = expressSoapProxy.transactionQuery(credentials, application, parameters, extendedParameters);
		return response;
	}

}
