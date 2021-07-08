/**
 * Express.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public interface Express extends javax.xml.rpc.Service {
    public java.lang.String getExpressSoapAddress();

    public com.paymentgateway.elementexpress.services.ExpressSoap getExpressSoap() throws javax.xml.rpc.ServiceException;

    public com.paymentgateway.elementexpress.services.ExpressSoap getExpressSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
