package com.unilog.cxmlinterfacemethods;
import java.util.HashMap;

import javax.ejb.Remote;

import com.unilog.cxml.models.reqresp.PurchaseOrderRequestDetails;

@Remote
public interface CxmlInterfaceMethods {

	public PurchaseOrderRequestDetails purchaseOrderRequestHandler(String purchaseOrderData);
	public String cxmlResponseData(HashMap<String,String> dataForResponse);
}
