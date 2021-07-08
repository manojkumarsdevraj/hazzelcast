/**
 * ExpressSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.paymentgateway.elementexpress.transaction;

public class ExpressSoapStub extends org.apache.axis.client.Stub implements com.paymentgateway.elementexpress.transaction.ExpressSoap {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[54];
        _initOperationDesc1();
        _initOperationDesc2();
        _initOperationDesc3();
        _initOperationDesc4();
        _initOperationDesc5();
        _initOperationDesc6();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("HealthCheck");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TimeCheck");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AccountTokenCreate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AccountTokenActivate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("MagneprintDataDecrypt");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TransactionSetup");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transactionSetup"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetup"), com.paymentgateway.elementexpress.transaction.TransactionSetup.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "address"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address"), com.paymentgateway.elementexpress.transaction.Address.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "paymentAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PaymentAccount"), com.paymentgateway.elementexpress.transaction.PaymentAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("BINQuery");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("EnhancedBINQuery");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TransactionSetupExpire");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transactionSetup"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetup"), com.paymentgateway.elementexpress.transaction.TransactionSetup.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardSale");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "address"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address"), com.paymentgateway.elementexpress.transaction.Address.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardAuthorization");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "address"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address"), com.paymentgateway.elementexpress.transaction.Address.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardAuthorizationCompletion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardForce");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardCredit");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardAdjustment");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardReversal");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardIncrementalAuthorization");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardBalanceInquiry");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardVoid");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[18] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardAVSOnly");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "address"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address"), com.paymentgateway.elementexpress.transaction.Address.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[19] = oper;

    }

    private static void _initOperationDesc3(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreditCardReturn");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[20] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DebitCardSale");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[21] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DebitCardReturn");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[22] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DebitCardReversal");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[23] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DebitCardPinlessSale");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[24] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DebitCardPinlessReturn");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[25] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("BatchClose");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "batch"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Batch"), com.paymentgateway.elementexpress.transaction.Batch.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[26] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("BatchItemQuery");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "batch"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Batch"), com.paymentgateway.elementexpress.transaction.Batch.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[27] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("BatchTotalsQuery");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "batch"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Batch"), com.paymentgateway.elementexpress.transaction.Batch.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[28] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CheckSale");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "demandDepositAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DemandDepositAccount"), com.paymentgateway.elementexpress.transaction.DemandDepositAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "identification"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Identification"), com.paymentgateway.elementexpress.transaction.Identification.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "address"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address"), com.paymentgateway.elementexpress.transaction.Address.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[29] = oper;

    }

    private static void _initOperationDesc4(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CheckCredit");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "demandDepositAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DemandDepositAccount"), com.paymentgateway.elementexpress.transaction.DemandDepositAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "identification"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Identification"), com.paymentgateway.elementexpress.transaction.Identification.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "address"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address"), com.paymentgateway.elementexpress.transaction.Address.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[30] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CheckVerification");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "demandDepositAccount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DemandDepositAccount"), com.paymentgateway.elementexpress.transaction.DemandDepositAccount.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "identification"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Identification"), com.paymentgateway.elementexpress.transaction.Identification.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "address"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address"), com.paymentgateway.elementexpress.transaction.Address.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[31] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CheckReturn");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[32] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CheckVoid");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[33] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CheckReversal");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[34] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("EBTSale");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ebt"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBT"), com.paymentgateway.elementexpress.transaction.EBT.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[35] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("EBTCredit");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ebt"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBT"), com.paymentgateway.elementexpress.transaction.EBT.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[36] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("EBTVoucher");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ebt"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBT"), com.paymentgateway.elementexpress.transaction.EBT.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[37] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("EBTBalanceInquiry");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ebt"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBT"), com.paymentgateway.elementexpress.transaction.EBT.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[38] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("EBTReversal");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ebt"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBT"), com.paymentgateway.elementexpress.transaction.EBT.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[39] = oper;

    }

    private static void _initOperationDesc5(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardSystemCheck");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[40] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardActivate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[41] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardSale");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[42] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardReload");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[43] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardCredit");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[44] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardReturn");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[45] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardBalanceInquiry");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[46] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardUnload");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[47] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardClose");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[48] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardAuthorization");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[49] = oper;

    }

    private static void _initOperationDesc6(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardAuthorizationCompletion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[50] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardReversal");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[51] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardReport");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[52] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GiftCardBalanceTransfer");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "credentials"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials"), com.paymentgateway.elementexpress.transaction.Credentials.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "application"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application"), com.paymentgateway.elementexpress.transaction.Application.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "terminal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal"), com.paymentgateway.elementexpress.transaction.Terminal.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "card"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card"), com.paymentgateway.elementexpress.transaction.Card.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "transaction"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction"), com.paymentgateway.elementexpress.transaction.Transaction.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "extendedParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters"), com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response"));
        oper.setReturnClass(com.paymentgateway.elementexpress.transaction.Response.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "response"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[53] = oper;

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
        addBindings0();
        addBindings1();
    }

    private void addBindings0() {
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
            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">AccountTokenActivate");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.AccountTokenActivate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">AccountTokenActivateResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.AccountTokenActivateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">AccountTokenCreate");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.AccountTokenCreate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">AccountTokenCreateResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.AccountTokenCreateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">BatchClose");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BatchClose.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">BatchCloseResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BatchCloseResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">BatchItemQuery");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BatchItemQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">BatchItemQueryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BatchItemQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">BatchTotalsQuery");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BatchTotalsQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">BatchTotalsQueryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BatchTotalsQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">BINQuery");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BINQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">BINQueryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BINQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckCredit");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckCredit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckCreditResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckCreditResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckReturn");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckReturn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckReturnResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckReturnResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckReversal");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckReversal.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckReversalResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckReversalResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckSale");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckSale.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckSaleResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckSaleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckVerification");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckVerification.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckVerificationResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckVerificationResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckVoid");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckVoid.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CheckVoidResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckVoidResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardAdjustment");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardAdjustment.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardAdjustmentResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardAdjustmentResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardAuthorization");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardAuthorization.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardAuthorizationCompletion");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardAuthorizationCompletion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardAuthorizationCompletionResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardAuthorizationCompletionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardAuthorizationResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardAuthorizationResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardAVSOnly");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardAVSOnly.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardAVSOnlyResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardAVSOnlyResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardBalanceInquiry");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardBalanceInquiry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardBalanceInquiryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardBalanceInquiryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardCredit");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardCredit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardCreditResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardCreditResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardForce");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardForce.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardForceResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardForceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardIncrementalAuthorization");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardIncrementalAuthorization.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardIncrementalAuthorizationResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardIncrementalAuthorizationResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardReturn");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardReturn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardReturnResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardReturnResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardReversal");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardReversal.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardReversalResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardReversalResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardSale");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardSale.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardSaleResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardSaleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardVoid");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardVoid.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">CreditCardVoidResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CreditCardVoidResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">DebitCardPinlessReturn");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DebitCardPinlessReturn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">DebitCardPinlessReturnResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DebitCardPinlessReturnResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">DebitCardPinlessSale");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DebitCardPinlessSale.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">DebitCardPinlessSaleResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DebitCardPinlessSaleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">DebitCardReturn");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DebitCardReturn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">DebitCardReturnResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DebitCardReturnResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">DebitCardReversal");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DebitCardReversal.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">DebitCardReversalResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DebitCardReversalResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">DebitCardSale");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DebitCardSale.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">DebitCardSaleResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DebitCardSaleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EBTBalanceInquiry");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBTBalanceInquiry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EBTBalanceInquiryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBTBalanceInquiryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EBTCredit");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBTCredit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EBTCreditResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBTCreditResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EBTReversal");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBTReversal.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EBTReversalResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBTReversalResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EBTSale");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBTSale.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EBTSaleResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBTSaleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EBTVoucher");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBTVoucher.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EBTVoucherResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBTVoucherResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EnhancedBINQuery");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EnhancedBINQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">EnhancedBINQueryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EnhancedBINQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardActivate");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardActivate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardActivateResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardActivateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardAuthorization");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardAuthorization.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardAuthorizationCompletion");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardAuthorizationCompletion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardAuthorizationCompletionResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardAuthorizationCompletionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardAuthorizationResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardAuthorizationResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardBalanceInquiry");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardBalanceInquiry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardBalanceInquiryResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardBalanceInquiryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardBalanceTransfer");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardBalanceTransfer.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardBalanceTransferResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardBalanceTransferResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardClose");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardClose.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardCloseResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardCloseResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardCredit");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardCredit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardCreditResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardCreditResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardReload");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardReload.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardReloadResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardReloadResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardReport");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardReport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardReportResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardReportResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardReturn");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardReturn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardReturnResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardReturnResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardReversal");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardReversal.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardReversalResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardReversalResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardSale");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardSale.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardSaleResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardSaleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardSystemCheck");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardSystemCheck.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardSystemCheckResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardSystemCheckResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardUnload");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardUnload.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">GiftCardUnloadResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.GiftCardUnloadResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">HealthCheck");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.HealthCheck.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">HealthCheckResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.HealthCheckResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }
    private void addBindings1() {
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
            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">MagneprintDataDecrypt");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.MagneprintDataDecrypt.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">MagneprintDataDecryptResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.MagneprintDataDecryptResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">TimeCheck");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TimeCheck.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">TimeCheckResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TimeCheckResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">TransactionSetup");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TransactionSetupType0.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">TransactionSetupExpire");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TransactionSetupExpire.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">TransactionSetupExpireResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TransactionSetupExpireResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", ">TransactionSetupResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TransactionSetupResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Address");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Address.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AmountSign");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.AmountSign.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Application");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Application.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ArrayOfExtendedParameters");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.ExtendedParameters[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters");
            qName2 = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AutoRental");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.AutoRental.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AutoRentalAuditAdjustmentCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.AutoRentalAuditAdjustmentCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AutoRentalDistanceUnit");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.AutoRentalDistanceUnit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AutoRentalVehicleClassCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.AutoRentalVehicleClassCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Batch");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Batch.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchCloseType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BatchCloseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchGroupingCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BatchGroupingCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchIndexCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BatchIndexCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchQueryType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BatchQueryType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BIN");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BIN.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BooleanType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.BooleanType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Card");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Card.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CardholderPresentCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CardholderPresentCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CardInputCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CardInputCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CardPresentCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CardPresentCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CheckType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ConsentCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.ConsentCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Credentials");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Credentials.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CVVPresenceCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CVVPresenceCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CVVResponseType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.CVVResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DDAAccountType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DDAAccountType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DemandDepositAccount");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DemandDepositAccount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Device");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Device.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DeviceInputCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.DeviceInputCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBT");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EBT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EMVEncryptionFormat");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EMVEncryptionFormat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EncryptionFormat");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EncryptionFormat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EnhancedBIN");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.EnhancedBIN.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExpressResponse");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.ExpressResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ExtendedParameters");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.ExtendedParameters.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Healthcare");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Healthcare.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HealthcareAccountType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.HealthcareAccountType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HealthcareAmountType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.HealthcareAmountType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Identification");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Identification.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Lodging");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Lodging.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "LodgingChargeType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.LodgingChargeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "LodgingPrestigiousPropertyCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.LodgingPrestigiousPropertyCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "LodgingSpecialProgramCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.LodgingSpecialProgramCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "MarketCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.MarketCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "MotoECICode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.MotoECICode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PASSUpdaterBatchStatus");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.PASSUpdaterBatchStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PASSUpdaterOption");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.PASSUpdaterOption.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PaymentAccount");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.PaymentAccount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "PaymentAccountType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.PaymentAccountType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Response");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ReversalReason");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.ReversalReason.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ReversalType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.ReversalType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "RunFrequency");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.RunFrequency.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "ScheduledTask");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.ScheduledTask.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "StatusType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.StatusType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Terminal");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Terminal.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TerminalCapabilityCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TerminalCapabilityCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TerminalEnvironmentCode");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TerminalEnvironmentCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TerminalType");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TerminalType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Token");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Token.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TokenProvider");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TokenProvider.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "Transaction");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.Transaction.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetup");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TransactionSetup.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupMethod");
            cachedSerQNames.add(qName);
            cls = com.paymentgateway.elementexpress.transaction.TransactionSetupMethod.class;
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

    public com.paymentgateway.elementexpress.transaction.Response healthCheck(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/HealthCheck");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "HealthCheck"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response timeCheck(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/TimeCheck");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TimeCheck"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response accountTokenCreate(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/AccountTokenCreate");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AccountTokenCreate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response accountTokenActivate(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/AccountTokenActivate");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "AccountTokenActivate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response magneprintDataDecrypt(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/MagneprintDataDecrypt");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "MagneprintDataDecrypt"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response transactionSetup(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.TransactionSetup transactionSetup, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.PaymentAccount paymentAccount, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/TransactionSetup");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetup"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, transaction, transactionSetup, address, paymentAccount, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response BINQuery(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/BINQuery");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BINQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response enhancedBINQuery(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/EnhancedBINQuery");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EnhancedBINQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response transactionSetupExpire(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.TransactionSetup transactionSetup, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/TransactionSetupExpire");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "TransactionSetupExpire"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, transactionSetup, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardSale");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardSale"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, address, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardAuthorization(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardAuthorization");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardAuthorization"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, address, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardAuthorizationCompletion(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardAuthorizationCompletion");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardAuthorizationCompletion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardForce(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardForce");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardForce"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardCredit(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardCredit");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardCredit"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardAdjustment(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardAdjustment");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardAdjustment"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardReversal(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardReversal");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardReversal"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardIncrementalAuthorization(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardIncrementalAuthorization");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardIncrementalAuthorization"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardBalanceInquiry(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardBalanceInquiry");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardBalanceInquiry"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardVoid(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardVoid");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardVoid"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardAVSOnly(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[19]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardAVSOnly");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardAVSOnly"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, address, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response creditCardReturn(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[20]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CreditCardReturn");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CreditCardReturn"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response debitCardSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[21]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/DebitCardSale");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DebitCardSale"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response debitCardReturn(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[22]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/DebitCardReturn");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DebitCardReturn"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response debitCardReversal(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[23]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/DebitCardReversal");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DebitCardReversal"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response debitCardPinlessSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[24]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/DebitCardPinlessSale");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DebitCardPinlessSale"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response debitCardPinlessReturn(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[25]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/DebitCardPinlessReturn");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "DebitCardPinlessReturn"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response batchClose(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Batch batch, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[26]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/BatchClose");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchClose"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, batch, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response batchItemQuery(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Batch batch, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[27]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/BatchItemQuery");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchItemQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, batch, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response batchTotalsQuery(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Batch batch, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[28]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/BatchTotalsQuery");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "BatchTotalsQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, batch, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response checkSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Identification identification, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[29]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CheckSale");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckSale"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, demandDepositAccount, transaction, identification, address, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response checkCredit(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Identification identification, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[30]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CheckCredit");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckCredit"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, demandDepositAccount, transaction, identification, address, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response checkVerification(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.DemandDepositAccount demandDepositAccount, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.Identification identification, com.paymentgateway.elementexpress.transaction.Address address, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[31]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CheckVerification");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckVerification"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, demandDepositAccount, transaction, identification, address, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response checkReturn(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[32]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CheckReturn");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckReturn"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response checkVoid(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[33]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CheckVoid");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckVoid"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response checkReversal(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[34]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/CheckReversal");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "CheckReversal"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response EBTSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.EBT ebt, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[35]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/EBTSale");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBTSale"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, ebt, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response EBTCredit(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.EBT ebt, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[36]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/EBTCredit");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBTCredit"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, ebt, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response EBTVoucher(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.EBT ebt, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[37]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/EBTVoucher");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBTVoucher"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, ebt, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response EBTBalanceInquiry(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.EBT ebt, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[38]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/EBTBalanceInquiry");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBTBalanceInquiry"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, ebt, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response EBTReversal(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.EBT ebt, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[39]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/EBTReversal");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "EBTReversal"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, ebt, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardSystemCheck(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[40]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardSystemCheck");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardSystemCheck"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardActivate(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[41]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardActivate");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardActivate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardSale(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[42]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardSale");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardSale"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardReload(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[43]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardReload");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardReload"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardCredit(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[44]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardCredit");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardCredit"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardReturn(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[45]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardReturn");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardReturn"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardBalanceInquiry(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[46]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardBalanceInquiry");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardBalanceInquiry"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardUnload(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[47]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardUnload");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardUnload"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardClose(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[48]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardClose");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardClose"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardAuthorization(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[49]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardAuthorization");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardAuthorization"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardAuthorizationCompletion(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[50]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardAuthorizationCompletion");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardAuthorizationCompletion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardReversal(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[51]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardReversal");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardReversal"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardReport(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[52]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardReport");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardReport"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.paymentgateway.elementexpress.transaction.Response giftCardBalanceTransfer(com.paymentgateway.elementexpress.transaction.Credentials credentials, com.paymentgateway.elementexpress.transaction.Application application, com.paymentgateway.elementexpress.transaction.Terminal terminal, com.paymentgateway.elementexpress.transaction.Card card, com.paymentgateway.elementexpress.transaction.Transaction transaction, com.paymentgateway.elementexpress.transaction.ExtendedParameters[] extendedParameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[53]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://transaction.elementexpress.com/GiftCardBalanceTransfer");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://transaction.elementexpress.com", "GiftCardBalanceTransfer"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {credentials, application, terminal, card, transaction, extendedParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.paymentgateway.elementexpress.transaction.Response) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.paymentgateway.elementexpress.transaction.Response) org.apache.axis.utils.JavaUtils.convert(_resp, com.paymentgateway.elementexpress.transaction.Response.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
