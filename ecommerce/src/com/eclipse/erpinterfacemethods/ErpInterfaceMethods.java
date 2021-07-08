package com.eclipse.erpinterfacemethods;

import java.util.HashMap;

import javax.ejb.Remote;

import com.unilog.cxml.models.reqresp.PurchaseOrderRequestDetails;

@Remote
public interface ErpInterfaceMethods {

	public HashMap<String,String> cxmlPurchaseOrderRegister(PurchaseOrderRequestDetails purchaseOrderRequestDetails);

	public HashMap<String,String> resendCxmlPurchaseOrderRegister(String orderNumber,PurchaseOrderRequestDetails purchaseOrderRequestDetailsCxml);
}
