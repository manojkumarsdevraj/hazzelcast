package com.erp.service.cimm2bcentral.utilities;

public interface Cimm2BCentralRequestParams {

	public String orderNumber = "orderNumber";
	public String sequenceNumber = "sequenceNumber";
	public String customerERPId = "customerERPId";
	public String customerERPCard = "customerERPCard";
	public String guestFlag = "guestFlag";
	public String partNumber ="partNumber";
	public String userERPId ="userERPId";
	public String statementYear = "statementYear";
	public String orderGenerationNumber = "orderGenerationNumber";
	public String orderType = "orderType";
	public String startDate  = "startDate";
	public String endDate = "endDate";
	public String qouteOrder = "quoteOrderRequired";
	public String orderSuffix = "orderSuffix";
	//public String orderInvoiceNumber = "orderInvoiceNumber";
	public String orderInvoiceNumber = "invoiceNumber";
	public String elementSetupId = "elementSetupId";
	public String invoiceNumber = "invoiceNumber";
	public String jobId = "jobId";
	public String emailId = "emailId";
	public String shipToListRequired = "shipToListRequired";
	public String warehouseCode = "warehouseCode";
	public String contractsListRequired = "includeMyAccountContracts";
	public String recordLimit = "recordLimit";
	public String sortDirection = "sortDirection";
}
