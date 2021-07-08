package com.unilog.punchout.servlets;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsModel;
import com.unilog.punchout.jaxb.BuyerCookie;
import com.unilog.punchout.jaxb.CXML;
import com.unilog.punchout.jaxb.Classification;
import com.unilog.punchout.jaxb.Credential;
import com.unilog.punchout.jaxb.Description;
import com.unilog.punchout.jaxb.From;
import com.unilog.punchout.jaxb.Header;
import com.unilog.punchout.jaxb.Identity;
import com.unilog.punchout.jaxb.ItemDetail;
import com.unilog.punchout.jaxb.ItemID;
import com.unilog.punchout.jaxb.ItemIn;
import com.unilog.punchout.jaxb.ManufacturerName;
import com.unilog.punchout.jaxb.ManufacturerPartID;
import com.unilog.punchout.jaxb.Message;
import com.unilog.punchout.jaxb.Money;
import com.unilog.punchout.jaxb.PunchOutOrderMessage;
import com.unilog.punchout.jaxb.PunchOutOrderMessageHeader;
import com.unilog.punchout.jaxb.Sender;
import com.unilog.punchout.jaxb.SupplierPartAuxiliaryID;
import com.unilog.punchout.jaxb.SupplierPartID;
import com.unilog.punchout.jaxb.To;
import com.unilog.punchout.jaxb.Total;
import com.unilog.punchout.jaxb.UnitOfMeasure;
import com.unilog.punchout.jaxb.UnitPrice;
import com.unilog.punchout.jaxb.UserAgent;
import com.unilog.punchout.model.PunchoutRequestModel;
import com.unilog.punchout.model.PunchoutResponseModel;
import com.unilog.punchout.utility.PunchoutRequestDAO;
import com.unilog.punchout.utility.XmlToObjectConverter;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

public class ProcessPunchoutResponse {

	private static ProcessPunchoutResponse processPunchoutResponse = null;
	

	private ProcessPunchoutResponse() {
		//
	}

	public static ProcessPunchoutResponse getInstance() {
		synchronized (ProcessPunchoutResponse.class) {
			if (processPunchoutResponse == null) {
				processPunchoutResponse = new ProcessPunchoutResponse();
			}
		}
		return processPunchoutResponse;
	}
	
	public String processResponse(HttpServletRequest request)
	{
		String fromId =request.getParameter("fromId");
		String fromDomain = request.getParameter("fromDomain");
		String toId = request.getParameter("toId");
		String toDomain = request.getParameter("toDomain");
		String outputDoc = "";
		
		
		
		try
		{
			String count = request.getParameter("cxmlParamitemCount");
			System.out.println("Canceled, Item Count Is : "+count);
			int itemCount = Integer.parseInt(count);
			String punchoutUrl = request.getParameter("cxmlParamHOOK_URL");
			String cartTotal = request.getParameter("cxmlParamcartTotalAmt");
			String buyerCookie = request.getParameter("cxmlParambCookie");
			String userAgent = request.getParameter("cxmlAribaUserAgent");
			
			String partNumber = "";
			String shortDesc = "";
			String itemQty = "";
			String itemUom = "";
			String itemMatGroup = "";
			String itemPrice = "";
			String itemPriceUnit = "";
			String itemCurr = "";
			String itemLongDesc = "";
			String lineItemId = "";
			String itemManufacturerName = "";
			String itemManufacturerPartNo = "";
			String itemUnspc = "";
			String itemCustPartNum = "";
			String blanketPoID = "";
			
			String partNumTmp = null;
			String shortDescTmp = null;
			String itemQtyTmp = null;
			String itemUomAbTmp = null;
			String itemLeadTimeTmp = null;
			String itemMatGroupTmp = null;
			String itemPriceTmp = null;
			String itemPriceUnitTmp = null;
			String itemCurrTmp = null;
			String itemLongDescTmp = null;
			String lineItemIdTmp = null;
			String itemManufNameTmp = null;
			String itemManPrtNoTmp = null;
			String itemUnspcTmp = null;
			String itemCustPartNumTmp = null;
			String blanketPoIDTmp = null;
			
			String logId = request.getParameter("logID");
		      
			PunchoutResponseModel responseModel= new PunchoutResponseModel();
			responseModel.setFromId(toId);
			responseModel.setToId(fromId);
			responseModel.setSenderId(toId);
			responseModel.setFromDomain(toDomain);
			responseModel.setToDomain(fromDomain);
			responseModel.setSenderDomain(toDomain);
			responseModel.setBuyerCookie(buyerCookie);
			responseModel.setCartTotal(cartTotal);
						
			ArrayList<ProductsModel> itemList = new ArrayList<ProductsModel>();
			ProductsModel itemValue = null;
			for(int i = 0; i < itemCount; i++ ){
				
				itemValue = new ProductsModel();
				partNumTmp = "cxmlParamsfPartNum["+i+"]";
				shortDescTmp = "cxmlParamshortDesc["+i+"]";
				itemQtyTmp = "cxmlParamitemQty["+i+"]";
				itemUomAbTmp = "cxmlParamitemUnit["+i+"]";
				itemLeadTimeTmp = "cxmlParamitemLeadTime["+i+"]";
				itemMatGroupTmp = "cxmlParamitemMatGroup["+i+"]";
				itemPriceTmp = "cxmlParamitemPrice["+i+"]";
				itemPriceUnitTmp = "cxmlParamitemPriceUnit["+i+"]";
				itemCurrTmp = "cxmlParamitemCurr["+i+"]";
				itemLongDescTmp = "cxmlParamitemLongDesc["+i+"]";
				lineItemIdTmp = "cxmlParamlineItemId["+i+"]";
				itemManufNameTmp = "cxmlParamitemManufName["+i+"]";
				itemManPrtNoTmp = "cxmlParamitemManPrtNo["+i+"]";
				itemUnspcTmp = "cxmlParamUnspc["+i+"]";
				itemCustPartNumTmp = "cxmlParamCustPartNum["+i+"]";
				blanketPoIDTmp = "cxmlParamBlanketPOId["+i+"]";
				
				partNumber  = request.getParameter(partNumTmp);
				shortDesc  = request.getParameter(shortDescTmp);
				itemQty  = request.getParameter(itemQtyTmp);
				itemUom  = request.getParameter(itemUomAbTmp);
				itemMatGroup  = request.getParameter(itemMatGroupTmp);
				itemPrice  = request.getParameter(itemPriceTmp);
				itemPriceUnit  = request.getParameter(itemPriceUnitTmp);
				itemLongDesc  = request.getParameter(itemLongDescTmp);
				lineItemId  = request.getParameter(lineItemIdTmp);
				itemManufacturerName  = request.getParameter(itemManufNameTmp);
				itemManufacturerPartNo  = request.getParameter(itemManPrtNoTmp);
				itemUnspc = request.getParameter(itemUnspcTmp);
				itemCustPartNum = request.getParameter(itemCustPartNumTmp);
				blanketPoID = request.getParameter(blanketPoIDTmp);
						
				itemValue.setPartNumber(partNumber);
				itemValue.setShortDesc(shortDesc);
				itemValue.setQty(CommonUtility.validateNumber(itemQty));
				itemValue.setUom(itemPriceUnit);
				itemValue.setUnspc(itemUnspc);
				itemValue.setMaterialGroup(itemMatGroup);
				itemValue.setPrice(Double.parseDouble(itemPrice));
				itemValue.setLongDesc(itemLongDesc);
				itemValue.setItemId(CommonUtility.validateNumber(lineItemId));
				itemValue.setManufacturerPartNumber(itemManufacturerPartNo);
				itemValue.setManufacturerName(itemManufacturerName);
				itemValue.setBlanketPoID(blanketPoID);
				itemList.add(itemValue);
			}
			responseModel.setItemList(itemList);
			outputDoc = generateCxml(responseModel);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return outputDoc;
	}
	
	
	
	public static Header generateCxmlHeader(PunchoutResponseModel responseModel){
		Header cxmlHeader = new Header();
		try
		{
			Identity identity = new Identity();
			Credential cxmlCredential = new Credential();
			From cxmlFrom = new From();
			identity.getContent().add(responseModel.getFromId());
			cxmlCredential.setDomain(responseModel.getFromDomain());
			cxmlCredential.setIdentity(identity);
			cxmlFrom.getCredential().add(cxmlCredential );
			cxmlHeader.setFrom(cxmlFrom );
			
			To cxmlTo = new To();
			cxmlCredential = new Credential();
			identity = new Identity();
			identity.getContent().add(responseModel.getToId());
			//cxmlCredential.setDomain("NetworkId");
			cxmlCredential.setDomain(responseModel.getToDomain());
			cxmlCredential.setIdentity(identity);
			cxmlTo.getCredential().add(cxmlCredential);
			cxmlHeader.setTo(cxmlTo );
			
			Sender cxmlSender = new Sender();
			UserAgent userAgent = new UserAgent();
			userAgent.setContent("Network");
			
			cxmlCredential = new Credential();
			cxmlCredential.setDomain(responseModel.getSenderDomain());
			identity = new Identity();
			identity.getContent().add(responseModel.getSenderId());
			cxmlCredential.setIdentity(identity );
			cxmlSender.getCredential().add(cxmlCredential);
			cxmlSender.setUserAgent(userAgent );
			cxmlHeader.setSender(cxmlSender);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return cxmlHeader;
	}
	
	public static List<ItemIn> generateItemIn(ArrayList<ProductsModel> itemList)
	{
		List<ItemIn> itemInList = new ArrayList<ItemIn>();
		try{
			PunchOutOrderMessage punchoutOrderMessage = new PunchOutOrderMessage();
			ItemIn itemIn =  null;
			SupplierPartAuxiliaryID supAuxId = null;
			SupplierPartID supId=null;
			ItemDetail itemDetail = null;
			ManufacturerName manufacturerName = null;
			ManufacturerPartID manufacturerPartID = null;
			UnitOfMeasure unitOfMeasure=null;
			Classification classification = null;
			Description description = null;
			UnitPrice unitPrice = null;
			ItemID itemId = null;
			int i = 0;
			Money money = new Money();
			for(ProductsModel itemValue:itemList)
			{
				itemIn = new ItemIn();
				itemId = new ItemID();
				
				supId=new SupplierPartID();
				itemDetail = new ItemDetail();
				manufacturerName = new ManufacturerName();
				manufacturerPartID = new ManufacturerPartID();
				unitOfMeasure=new UnitOfMeasure();
				classification = new Classification();
				description = new Description();
				unitPrice = new UnitPrice();
				supId.setContent(appendCdata(itemValue.getPartNumber()));
				
				
				itemId.setSupplierPartAuxiliaryID(supAuxId);
				itemId.setSupplierPartID(supId);
			
				
				
				
				
				manufacturerName.setContent(appendCdata(itemValue.getManufacturerName()));
				manufacturerPartID.setContent(appendCdata(itemValue.getManufacturerPartNumber()));
				if(CommonUtility.validateString(itemValue.getUom()).trim().length()>0)
				unitOfMeasure.setContent(itemValue.getUom());
				else
					unitOfMeasure.setContent("EA");
				if(CommonUtility.validateString(itemValue.getUnspc()).trim().length()>0)
				classification.setContent(itemValue.getUnspc());
				classification.setDomain("UNSPSC");
				
				description.setLang("en");
				description.getContent().add(appendCdata(itemValue.getShortDesc()));
				
				money = new Money();
				money.setCurrency("USD");
				money.setContent(Double.toString(itemValue.getPrice()));
				unitPrice.setMoney(money);
				
				itemDetail.setManufacturerName(manufacturerName);
				itemDetail.setManufacturerPartID(manufacturerPartID );
				itemDetail.setUnitOfMeasure(unitOfMeasure);
				itemDetail.getClassification().add(classification);
				itemDetail.getDescription().add(description);
				itemDetail.setUnitPrice(unitPrice);
				
				itemIn.setQuantity(Integer.toString(itemValue.getQty()));
				itemIn.setItemID(itemId);
				itemIn.setItemDetail(itemDetail);
				punchoutOrderMessage.getItemIn().add(itemIn);
			}
			itemInList = punchoutOrderMessage.getItemIn();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return itemInList;
		
	}
	
	public static String appendCdata(String input)
	{
		String cdataVal = "";
		try
		{
			cdataVal = "<![CDATA["+CommonUtility.validateString(input)+ "]]>";
		}
		catch (Exception e) {
			
		}
		return cdataVal;
	}
	public static PunchOutOrderMessageHeader generateMessageHeader(String cartTotal){
		
		PunchOutOrderMessageHeader punchOutOrderMessageHeader = new PunchOutOrderMessageHeader();
		try{
			
			punchOutOrderMessageHeader.setOperationAllowed("create");
			Total total =  new Total();
			Money money = new Money();
			money.setCurrency("USD");
			money.setContent(cartTotal);
			total.setMoney(money);
			punchOutOrderMessageHeader.setTotal(total);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return punchOutOrderMessageHeader;
	}
	
	public static String generateCxml(PunchoutResponseModel responseModel){
		
		String cxmlResponse = null;
		try
			{
			CXML cxml = new CXML();		
			Header cxmlHeader = null;
			Message message = new Message();
			PunchOutOrderMessage punchoutOrderMessage = new PunchOutOrderMessage();
			
			cxmlHeader = generateCxmlHeader(responseModel);
			cxml.setLang("en-US");
			cxml.setPayloadID(PunchoutRequestDAO.getPayloadId());
			cxml.setTimestamp(PunchoutRequestDAO.getTimestamp());
			cxml.setVersion("1.2.021");
			BuyerCookie buyerCookie = new BuyerCookie();
			buyerCookie.getContent().add(responseModel.getBuyerCookie());
			
			PunchOutOrderMessageHeader punchOutOrderMessageHeader = generateMessageHeader(responseModel.getCartTotal());
			punchoutOrderMessage.setPunchOutOrderMessageHeader(punchOutOrderMessageHeader );
			
			punchoutOrderMessage.setBuyerCookie(buyerCookie);
			punchoutOrderMessage.getItemIn().addAll(generateItemIn(responseModel.getItemList()));
			message.setPunchOutOrderMessage(punchoutOrderMessage );
			cxml.setHeader(cxmlHeader);
			cxml.setMessage(message);
			cxmlResponse = XmlToObjectConverter.convertJavaObjectToXmlString(cxml);
			System.out.println(cxmlResponse);
			}catch (Exception e) {
			e.printStackTrace();
		}
		return cxmlResponse;
		
	}
	
	public static void main(String args[])
	{
		String cxmlResponse = null;
		CXML cxml = new CXML();	
		cxml.setLang("en-US");
		cxml.setPayloadID(PunchoutRequestDAO.getPayloadId());
		cxml.setTimestamp(PunchoutRequestDAO.getTimestamp());
		cxml.setVersion("1.2.021");
		cxmlResponse = XmlToObjectConverter.convertJavaObjectToXmlString(cxml);
		System.out.println(cxmlResponse);
	///	generateCxml();
	}
}
