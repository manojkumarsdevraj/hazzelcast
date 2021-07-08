package com.unilog.services.impl;

import static java.util.stream.Collectors.toCollection;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralWarehouse;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.SendMailUtility;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.ConvertHtmlToPdf;

public class DaveCarterCustomServices implements UnilogFactoryInterface  {
	private static UnilogFactoryInterface serviceProvider;
	
	private DaveCarterCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (DaveCarterCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new DaveCarterCustomServices();
				}
			}
		return serviceProvider;
	}
	
	public String creditApplication(HttpSession session,Map<String, String> formParameters, SendMailUtility sendMailUtility, VelocityContext context, SaveCustomFormDetails getNotificationDetail, String uploadedFileNames){
		String sessionId =session.getId();
		String formName=formParameters.get("formName");
		String creditAppFileName = sessionId+"_"+"BusinessApplication.pdf";
		String filePath = "";
		if(formName!=null) {
		StringBuilder credAppformData=DaveCarterCustomServices.buildCreditApplictionPdf(context,formName,getNotificationDetail);
		
		//------- Generate PDF
	    	if(credAppformData!=null && credAppformData.length()>0){
	    		filePath = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH"));
	    		ConvertHtmlToPdf convertHtmlToPdf = new ConvertHtmlToPdf();
	    		convertHtmlToPdf.gerratePdfFromHtml(credAppformData, creditAppFileName);
	    		filePath = filePath+"/"+creditAppFileName;
	    	}
		}
	    	if(uploadedFileNames!=null && CommonUtility.validateString(uploadedFileNames).length()>0) {
	    		uploadedFileNames+=","+filePath;
	    	}else {
	    		uploadedFileNames = filePath;
	    	}
	    	return 	uploadedFileNames;
	    	}
	
	public static StringBuilder buildCreditApplictionPdf(VelocityContext context,String notificationName, SaveCustomFormDetails getNotificationDetail){
		StringBuilder stringBuilder = new StringBuilder();
		try {
			LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails(notificationName);
			StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            System.out.println("writer.toString() Business Application : "+writer.toString());
            stringBuilder.append(writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuilder;
	}
	public void setQuoteTotal(SalesModel quoteResponse, double total) {
		double overTotal = 0.0;
		if(quoteResponse.getTotal()>0)
		{
			overTotal = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(total + quoteResponse.getTax()));
			quoteResponse.setTotal(overTotal);
		}
		}
	
	public void addDiscountToLineItems(ArrayList<Cimm2BCentralLineItem> lineItems,double discountAmount,HttpSession htSession) {
		HttpSession session = htSession;
		if(discountAmount > 0){
		double discountAmountRoundOff = discountAmount;
		Cimm2BCentralLineItem cimm2bCentralModifiedLineItem = new Cimm2BCentralLineItem();
		cimm2bCentralModifiedLineItem.setPartNumber(CommonDBQuery.getSystemParamtersList().get("DISCOUNT_PART_NUMBER"));
		cimm2bCentralModifiedLineItem.setUnitPrice(discountAmountRoundOff);
		cimm2bCentralModifiedLineItem.setExtendedPrice(discountAmountRoundOff);
		cimm2bCentralModifiedLineItem.setQty(-1);
		cimm2bCentralModifiedLineItem.setUom("ea");
		cimm2bCentralModifiedLineItem.setUomQty("1");
		cimm2bCentralModifiedLineItem.setLineItemComment(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("product.label.couponLineItemCommentV1")+" "+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("product.label.couponLineItemCommentV2")+CommonUtility.validateParseDoubleToString(discountAmountRoundOff)+" "+LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("product.label.couponLineItemCommentV3"));
		lineItems.add(cimm2bCentralModifiedLineItem);
		}
	}
	
	public void setRoundPrice(Cimm2BCentralItem item,ProductsModel product) {
		double price=0.0;
		double qty=0.0;
		//double extPrice=0.0;
		if(item!=null && item.getPricingWarehouse()!=null) {
			qty = item.getPricingWarehouse().getPriceUomQuantity()>0?item.getPricingWarehouse().getPriceUomQuantity().intValue():1;
			product.setUomQuantity(CommonUtility.validateInteger(qty));
			if(CommonUtility.validateInteger(product.getPrice())<item.getPricingWarehouse().getCustomerPrice()) {
				price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(product.getPrice()));
				if(price>0) {
				price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(product.getPrice() * qty));
				}
			}
			else {
				if(price>0) {
				price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(item.getPricingWarehouse().getCustomerPrice() * qty));
				}
			}
			//extPrice=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(item.getPricingWarehouse().getExtendedPrice() * qty));
			item.getPricingWarehouse().setListPrice(price);
			item.getPricingWarehouse().setCustomerPrice(price);
			item.getPricingWarehouse().setExtendedPrice(price);
			product.setPrice(price);
		}
	}
	
	public void setQtyOnERP(SalesModel sales,Cimm2BCentralLineItem item)
	{
			if(item.getQty()>0)
			{
				item.setQtyShipped(Double.valueOf(item.getQty()));
			}else{
				item.setQtyShipped(Double.valueOf(1));
			}
		}
	
	public double setroundOfDiscount(double discountAmount) {
		double discountPrice = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(discountAmount));
		if(discountPrice>0) {
			discountAmount = discountPrice;
		}
		return discountAmount;
	}
	
	public Cimm2BCentralLineItem customizeSalesOrderLineItem(Cimm2BCentralLineItem cimm2bCentralLineItem) {
		Cimm2BCentralLineItem cimm2bCentralLineItemServiceObj = new Cimm2BCentralLineItem();
		try {
				cimm2bCentralLineItemServiceObj.setItemId(cimm2bCentralLineItem.getPartNumber());
				cimm2bCentralLineItemServiceObj.setLineIdentifier(cimm2bCentralLineItem.getLineIdentifier());
				cimm2bCentralLineItemServiceObj.setPartNumber(cimm2bCentralLineItem.getPartNumber());
				cimm2bCentralLineItemServiceObj.setShortDescription(cimm2bCentralLineItem.getShortDescription());
				cimm2bCentralLineItemServiceObj.setQty(cimm2bCentralLineItem.getQty());
				cimm2bCentralLineItemServiceObj.setUom(cimm2bCentralLineItem.getUom());
				cimm2bCentralLineItemServiceObj.setShippingBranch(cimm2bCentralLineItem.getShippingBranch());
				cimm2bCentralLineItemServiceObj.setCalculateTax(cimm2bCentralLineItem.getCalculateTax());
				cimm2bCentralLineItemServiceObj.setLineItemComment(cimm2bCentralLineItem.getLineItemComment());
				cimm2bCentralLineItemServiceObj.setManufacturer(cimm2bCentralLineItem.getManufacturer());
				cimm2bCentralLineItemServiceObj.setPage_title(cimm2bCentralLineItem.getPage_title());
				cimm2bCentralLineItemServiceObj.setUomQty(cimm2bCentralLineItem.getUomQty());
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cimm2bCentralLineItemServiceObj;
	}

	public void getPackDesc(ProductsModel saveItems) {
		if(saveItems.getPackDesc()!=null && saveItems.getPackDesc().length()>0) {
		if(saveItems.getPackDesc()!=null && saveItems.getPackDesc().length()>20) {
			saveItems.setPackDesc(saveItems.getPackDesc().substring(0, 19));
		}
		else {
			saveItems.setPackDesc(saveItems.getPackDesc());
		}
		}
		else {
			saveItems.setPackDesc("");
		}
	}
	
	public void filterMyCatalogGroups(Map<String, Object> contentObject, HttpServletRequest request) {
		
		@SuppressWarnings("unchecked")
		ArrayList<ProductsModel> groupListData = (ArrayList<ProductsModel>) contentObject.get("groupListData");
		
		if(CommonUtility.validateString(request.getParameter("groupType")).equals("MC")) {
			contentObject.put("groupType", "MC");
			groupListData = groupListData.stream().filter(g -> g.getProductListName().toUpperCase().startsWith("MYITEMHISTORY_")).collect(toCollection(ArrayList::new));
		}else {
			groupListData = groupListData.stream().filter(g -> !g.getProductListName().toUpperCase().startsWith("MYITEMHISTORY_")).collect(toCollection(ArrayList::new));
			
		}
		contentObject.put("groupListData", groupListData);
	}
	
	private void setMaxQtyBreak(List<ProductsModel> qtyBreaks) {
		int noOfBreaks = qtyBreaks.size();
		for(int i = 0; i < qtyBreaks.size(); i++) {
			ProductsModel eachBreak = qtyBreaks.get(i);
			if((i + 1) < noOfBreaks) {
				eachBreak.setMaximumQuantityBreak(qtyBreaks.get(i+1).getMinimumQuantityBreak() - 1);
			}
		}
	}
	
	public void erpQuanitityBreakUpdate(ProductsModel productsModel, ProductsModel pricingResponse) {
		if(pricingResponse.getQuantityBreakList()!=null && pricingResponse.getQuantityBreakList().size()>0) {
		setMaxQtyBreak(pricingResponse.getQuantityBreakList());
		Optional<ProductsModel> quantityBreakPrice = pricingResponse.getQuantityBreakList().stream()
				.filter(qb -> quantityBreakRange(productsModel.getQty(), qb))
				.findFirst();
		if (quantityBreakPrice.isPresent()) {
			double price = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(quantityBreakPrice.get().getCustomerPriceBreak()));
			productsModel.setPrice(price);
			pricingResponse.setPrice(price);
		}else {
			double price = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(pricingResponse.getCimm2BCentralPricingWarehouse().getCustomerPrice()));
			productsModel.setPrice(price);
			pricingResponse.setPrice(price);
		}
		}
	}
	private boolean quantityBreakRange(int qty, ProductsModel quantityBreak) {
		boolean status = false;
		if (quantityBreak.getMaximumQuantityBreak() > 0) {
            if (qty >= quantityBreak.getMinimumQuantityBreak() && qty <= quantityBreak.getMaximumQuantityBreak()) {
            	status = true;
            }
        } else {
            if (qty >= quantityBreak.getMinimumQuantityBreak()) {
            	status = true;
            }
        }
		return status;
	}
	
	public void setWareHousePrice(Cimm2BCentralWarehouse item,ProductsModel product) {
		double price=0.0;
		double extPrice=0.0;
		double qty=0.0;
		if(item!=null && item.getCustomerPrice()!=null) {
			qty = item.getPriceUomQuantity()>0?item.getPriceUomQuantity().intValue():1;
			if(CommonUtility.validateInteger(product.getPrice())>0) {
				price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(product.getPrice()));
			}
			else {
				price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(product.getCimm2BCentralPricingWarehouse().getCustomerPrice()));
			}
			extPrice=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(item.getExtendedPrice()));
			item.setListPrice(price);
			item.setCustomerPrice(price);
			item.setExtendedPrice(price);
			product.setPrice(price);
		}
	}
	
	public boolean disableSendMailApprove(SalesModel erpOrderDetail) {
		if(erpOrderDetail!=null && erpOrderDetail.getErpOrderNumber()!=null && erpOrderDetail.getErpOrderNumber().length()>0) {
			return true;
		}
		return false;
	}
}
