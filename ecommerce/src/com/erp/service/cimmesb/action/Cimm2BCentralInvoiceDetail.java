package com.erp.service.cimmesb.action;
//cpe
import java.util.ArrayList;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;

public class Cimm2BCentralInvoiceDetail extends Cimm2BCentralResponseEntity{
	{
}
	private String customerERPId;
	private Cimm2BCentralAddress billToAddress;
	private Cimm2BCentralAddress shipToAddress;
	private String invoiceNumber;
    private String orderNumber;
    private double totalAmount;
    private double tax;
    private String poNumber;
    private double freightAmount;
    private ArrayList<Cimm2BCentralLineItem> lineItems;
    private String invoicedDate;
    private String orderedDate;
    private String dueDate;
	private String billToCode;
	private String taxType;
	private Double taxAmount;
	private String grossAmount;
	private String discountPercentage;
	private String lrNo;
	private String lrDate;
	private String billToName;
	private String shipToCode;
	private String shipToName;
	private String netAmount;
	private String discountAmount;
	
	
	public String getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}
	public String getShipToName() {
		return shipToName;
	}
	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}
	public String getShipToCode() {
		return shipToCode;
	}
	public void setShipToCode(String shipToCode) {
		this.shipToCode = shipToCode;
	}
	public String getBillToName() {
		return billToName;
	}
	public void setBillToName(String billToName) {
		this.billToName = billToName;
	}
	public String getBillToCode() {
		return billToCode;
	}
	public void setBillToCode(String billToCode) {
		this.billToCode = billToCode;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public String getGrossAmount() {
		return grossAmount;
	}
	public void setGrossAmount(String grossAmount) {
		this.grossAmount = grossAmount;
	}
	public String getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public String getLrNo() {
		return lrNo;
	}
	public void setLrNo(String lrNo) {
		this.lrNo = lrNo;
	}
	public String getLrDate() {
		return lrDate;
	}
	public void setLrDate(String lrDate) {
		this.lrDate = lrDate;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public double getFreightAmount() {
		return freightAmount;
	}
	public void setFreightAmount(double freightAmount) {
		this.freightAmount = freightAmount;
	}
	
	public String getInvoicedDate() {
		return invoicedDate;
	}
	public void setInvoicedDate(String invoicedDate) {
		this.invoicedDate = invoicedDate;
	}
	public String getOrderedDate() {
		return orderedDate;
	}
	public void setOrderedDate(String orderedDate) {
		this.orderedDate = orderedDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public ArrayList<Cimm2BCentralLineItem> getLineItems() {
		return lineItems;
	}
	public void setLineItems(ArrayList<Cimm2BCentralLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	public String getCustomerERPId() {
		return customerERPId;
	}
	public void setCustomerERPId(String customerERPId) {
		this.customerERPId = customerERPId;
	}
	
	public Cimm2BCentralAddress getShipToAddress() {
		return shipToAddress;
	}

	public void setShipToAddress(Cimm2BCentralAddress shipToAddress) {
		this.shipToAddress = shipToAddress;
	}
	public Cimm2BCentralAddress getBillToAddress() {
		return billToAddress;
	}
	public void setBillToAddress(Cimm2BCentralAddress billToAddress) {
		this.billToAddress = billToAddress;
	}
}