/**
 * ExpressSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.services;

public class ExpressSoapStub extends org.apache.axis.client.Stub implements com.paymentgateway.elementexpress.services.ExpressSoap {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[14];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PaymentAccountCreateWithTransID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.services.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "paymentAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccount"), com.paymentgateway.elementexpress.services.PaymentAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "address"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Address"), com.paymentgateway.elementexpress.services.Address.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PaymentAccountCreate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "paymentAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccount"), com.paymentgateway.elementexpress.services.PaymentAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Card"), com.paymentgateway.elementexpress.services.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "demandDepositAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "DemandDepositAccount"), com.paymentgateway.elementexpress.services.DemandDepositAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "address"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Address"), com.paymentgateway.elementexpress.services.Address.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TokenCreate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.services.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Card"), com.paymentgateway.elementexpress.services.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "token"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Token"), com.paymentgateway.elementexpress.services.Token.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "TokenCreateResponse"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.TokenCreateResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TokenCreateWithTransID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.services.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.services.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "token"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Token"), com.paymentgateway.elementexpress.services.Token.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "TokenCreateResponse"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.TokenCreateResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PaymentAccountUpdate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "paymentAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccount"), com.paymentgateway.elementexpress.services.PaymentAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Card"), com.paymentgateway.elementexpress.services.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "demandDepositAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "DemandDepositAccount"), com.paymentgateway.elementexpress.services.DemandDepositAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "address"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Address"), com.paymentgateway.elementexpress.services.Address.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PaymentAccountDelete");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "paymentAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccount"), com.paymentgateway.elementexpress.services.PaymentAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PaymentAccountQuery");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "paymentAccountParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountParameters"), com.paymentgateway.elementexpress.services.PaymentAccountParameters.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PaymentAccountQueryRecordCount");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PaymentAccountQueryTokenReport");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "paging"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Paging"), com.paymentgateway.elementexpress.services.Paging.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PaymentAccountAutoUpdate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "paymentAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccount"), com.paymentgateway.elementexpress.services.PaymentAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ScheduledTaskDelete");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "scheduledTask"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTask"), com.paymentgateway.elementexpress.services.ScheduledTask.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ScheduledTaskQuery");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "scheduledTaskParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskParameters"), com.paymentgateway.elementexpress.services.ScheduledTaskParameters.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ScheduledTaskUpdate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "scheduledTask"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTask"), com.paymentgateway.elementexpress.services.ScheduledTask.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ScheduledTaskRetry");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.services.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "Application"), com.paymentgateway.elementexpress.services.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "scheduledTask"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTask"), com.paymentgateway.elementexpress.services.ScheduledTask.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://services.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.services.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://services.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.services.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://services.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

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
            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountAutoUpdate");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountAutoUpdate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountAutoUpdateResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountAutoUpdateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountCreate");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountCreate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountCreateResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountCreateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountCreateWithTransID");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountCreateWithTransID.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountCreateWithTransIDResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountCreateWithTransIDResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountDelete");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountDelete.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountDeleteResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountDeleteResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountQuery");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountQueryRecordCount");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountQueryRecordCount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountQueryRecordCountResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountQueryRecordCountResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountQueryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountQueryTokenReport");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountQueryTokenReport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountQueryTokenReportResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountQueryTokenReportResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountUpdate");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountUpdate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">PaymentAccountUpdateResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountUpdateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">ScheduledTaskDelete");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskDelete.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">ScheduledTaskDeleteResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskDeleteResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">ScheduledTaskQuery");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">ScheduledTaskQueryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">ScheduledTaskRetry");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskRetry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">ScheduledTaskRetryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskRetryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">ScheduledTaskUpdate");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskUpdate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">ScheduledTaskUpdateResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskUpdateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">TokenCreate");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.TokenCreate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">TokenCreateResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.TokenCreateResponseType0.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">TokenCreateWithTransID");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.TokenCreateWithTransID.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", ">TokenCreateWithTransIDResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.TokenCreateWithTransIDResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "Address");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.Address.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "Application");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.Application.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ArrayOfExtendedParameters");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ExtendedParameters[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters");
            qName2 = new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "BooleanType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.BooleanType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "Card");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.Card.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "CardholderPresentCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.CardholderPresentCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "CardInputCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.CardInputCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "CardPresentCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.CardPresentCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "CheckType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.CheckType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ConsentCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ConsentCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "Credentials");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.Credentials.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "CVVPresenceCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.CVVPresenceCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "CVVResponseType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.CVVResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "DDAAccountType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.DDAAccountType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "DemandDepositAccount");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.DemandDepositAccount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "EMVEncryptionFormat");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.EMVEncryptionFormat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "EncryptionFormat");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.EncryptionFormat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ExpressResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ExpressResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedBooleanType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ExtendedBooleanType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedParameters");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ExtendedParameters.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedRunFrequency");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ExtendedRunFrequency.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ExtendedStatusType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ExtendedStatusType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "MarketCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.MarketCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "MotoECICode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.MotoECICode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "Paging");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.Paging.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterBatchStatus");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PASSUpdaterBatchStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterOption");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PASSUpdaterOption.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "PASSUpdaterStatus");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PASSUpdaterStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccount");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountParameters");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountParameters.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.PaymentAccountType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "Response");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ReversalReason");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ReversalReason.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ReversalType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ReversalType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "RunFrequency");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.RunFrequency.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTask");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTask.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskParameters");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskParameters.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskQueryType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskQueryType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskRunStatus");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.ScheduledTaskRunStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "StatusType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.StatusType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "Terminal");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.Terminal.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalCapabilityCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.TerminalCapabilityCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalEnvironmentCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.TerminalEnvironmentCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "TerminalType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.TerminalType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "Token");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.Token.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "TokenCreateResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.TokenCreateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "TokenProvider");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.TokenProvider.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://services.elementexpress.com", "Transaction");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.services.Transaction.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

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

    public com.paymentgateway.elementexpress.services.Response paymentAccountCreateWithTransID(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.Transaction transaction, com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.services.Address address, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/PaymentAccountCreateWithTransID");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountCreateWithTransID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, transaction, paymentAccount, address, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response paymentAccountCreate(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.services.Card card, com.paymentgateway.elementexpress.services.DemandDepositAccount demandDepositAccount, com.paymentgateway.elementexpress.services.Address address, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/PaymentAccountCreate");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountCreate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, paymentAccount, card, demandDepositAccount, address, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.TokenCreateResponse tokenCreate(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.Terminal terminal, com.paymentgateway.elementexpress.services.Card card, com.paymentgateway.elementexpress.services.Token token, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/TokenCreate");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "TokenCreate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, token, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.TokenCreateResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.TokenCreateResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.TokenCreateResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.TokenCreateResponse tokenCreateWithTransID(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.Terminal terminal, com.paymentgateway.elementexpress.services.Transaction transaction, com.paymentgateway.elementexpress.services.Token token, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/TokenCreateWithTransID");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "TokenCreateWithTransID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, transaction, token, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.TokenCreateResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.TokenCreateResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.TokenCreateResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response paymentAccountUpdate(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.services.Card card, com.paymentgateway.elementexpress.services.DemandDepositAccount demandDepositAccount, com.paymentgateway.elementexpress.services.Address address, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/PaymentAccountUpdate");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountUpdate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, paymentAccount, card, demandDepositAccount, address, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response paymentAccountDelete(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/PaymentAccountDelete");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountDelete"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, paymentAccount, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response paymentAccountQuery(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.PaymentAccountParameters paymentAccountParameters, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/PaymentAccountQuery");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, paymentAccountParameters, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response paymentAccountQueryRecordCount(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/PaymentAccountQueryRecordCount");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountQueryRecordCount"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response paymentAccountQueryTokenReport(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.Paging paging, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/PaymentAccountQueryTokenReport");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountQueryTokenReport"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, paging, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response paymentAccountAutoUpdate(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/PaymentAccountAutoUpdate");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "PaymentAccountAutoUpdate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, paymentAccount, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response scheduledTaskDelete(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.ScheduledTask scheduledTask, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/ScheduledTaskDelete");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskDelete"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, scheduledTask, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response scheduledTaskQuery(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.ScheduledTaskParameters scheduledTaskParameters, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/ScheduledTaskQuery");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, scheduledTaskParameters, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response scheduledTaskUpdate(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.ScheduledTask scheduledTask, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/ScheduledTaskUpdate");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskUpdate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, scheduledTask, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.services.Response scheduledTaskRetry(com.paymentgateway.elementexpress.services.Credentials credentials, com.paymentgateway.elementexpress.services.Application application, com.paymentgateway.elementexpress.services.ScheduledTask scheduledTask, com.paymentgateway.elementexpress.services.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://services.elementexpress.com/ScheduledTaskRetry");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://services.elementexpress.com", "ScheduledTaskRetry"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, scheduledTask, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.services.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.services.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.services.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
