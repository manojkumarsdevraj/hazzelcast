/**
 * ExpressSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.reporting;

public interface ExpressSoap extends java.rmi.Remote {
    public com.paymentgateway.elementexpress.reporting.Response transactionQuery(com.paymentgateway.elementexpress.reporting.Credentials credentials, com.paymentgateway.elementexpress.reporting.Application application, com.paymentgateway.elementexpress.reporting.Parameters parameters, com.paymentgateway.elementexpress.reporting.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException;
}
