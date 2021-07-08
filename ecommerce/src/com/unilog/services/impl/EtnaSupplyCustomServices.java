package com.unilog.services.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.SendMailUtility;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class EtnaSupplyCustomServices implements UnilogFactoryInterface {

private static UnilogFactoryInterface serviceProvider;
public static SaveCustomFormDetails getNotificationDetail = new SaveCustomFormDetails();
	
	private EtnaSupplyCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (EtnaSupplyCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new EtnaSupplyCustomServices();
				}
			}
		return serviceProvider;
	}
	
	public Object getCustomerAlsoBoughtCustom(int userId,int subsetId, int generalSubset, ArrayList<SalesModel> salesOrderItem) {
		ArrayList<ProductsModel> customerAlsoBoughtdata = new ArrayList<ProductsModel>();
		
		for(SalesModel item : salesOrderItem) {
			ArrayList<ProductsModel> customerAlsoBoughtTemp = new ArrayList<ProductsModel>();
			customerAlsoBoughtTemp = ProductsDAO.getCustomerAlsoBought(userId, subsetId, generalSubset, item.getItemId(), userId, 1, 2);
			customerAlsoBoughtdata.addAll(customerAlsoBoughtTemp);
		}
		for(int i=0;i<customerAlsoBoughtdata.size();i++){
			 
			 for(int j=i+1;j<customerAlsoBoughtdata.size();j++){
			            if(customerAlsoBoughtdata.get(i).getPartNumber().equals(customerAlsoBoughtdata.get(j).getPartNumber())){
			            	customerAlsoBoughtdata.remove(j);
			                j--;
			            }
			    }
			 
			 }
		
		return customerAlsoBoughtdata;		
	}
	public String sendmailWithAccountNumber(UsersModel userDetailsInput) {		 
	    String result = "";
		LinkedHashMap<String,String> onAccountUserRegisteration=new  LinkedHashMap<String,String>();
		 onAccountUserRegisteration.put("Company Name",userDetailsInput.getEntityName());
		 onAccountUserRegisteration.put("First Name",userDetailsInput.getFirstName());
		 onAccountUserRegisteration.put("Last Name",userDetailsInput.getLastName());
		 onAccountUserRegisteration.put("Email Address",userDetailsInput.getEmailAddress());
		 onAccountUserRegisteration.put("New Password",userDetailsInput.getPassword());		
		 onAccountUserRegisteration.put("Company Billing Address 1",userDetailsInput.getAddress1());
		 onAccountUserRegisteration.put("Company Billing Address 2",userDetailsInput.getAddress2());
		 onAccountUserRegisteration.put("City Name",userDetailsInput.getCity());
		 onAccountUserRegisteration.put("State Name", userDetailsInput.getState() );
		 onAccountUserRegisteration.put("Country Name",userDetailsInput.getCountry());
		 onAccountUserRegisteration.put("Zip Code", userDetailsInput.getZipCode());
		 onAccountUserRegisteration.put("Phone No",	userDetailsInput.getPhoneNo() );
		 onAccountUserRegisteration.put("Status", userDetailsInput.getUserStatus());
		 String AccountNumber = "";
		 if(userDetailsInput.getAccountName().length()>0 && !userDetailsInput.getAccountName().equalsIgnoreCase("Unknown")) {
			 AccountNumber =  userDetailsInput.getAccountName() + "  <-- UNKNOWN ACCOUNT NUMBER";		 
			 onAccountUserRegisteration.put("Account Number", AccountNumber );
		 }
		 else {
			 AccountNumber = "UNKNOWN";		
			 onAccountUserRegisteration.put("Account Number", "UNKNOWN" );				
		 }
		 	onAccountUserRegisteration.put("Existing Account", userDetailsInput.getUserStatus());
		 	boolean notificationStatus = false;		
			notificationStatus = SendMailUtility.sendRegistrationNotification(onAccountUserRegisteration);
			 if(notificationStatus){
				 if(userDetailsInput.getAccountName().length()>0) {
					 result = "accountNumber";
				 }
				 else{
					 result = "noaccountNumber";
				 }
				 
			 }else{
				 result = "Error While Registering. Contact our Customer Service for Further Assistance.|";
			 }
		return result;
		
	}
	
	public void addDiscountToLineItems(ArrayList<Cimm2BCentralLineItem> lineItems,double discountAmount,HttpSession htSession) {
		
		HttpSession session = htSession;
		if(discountAmount > 0){
		double discountAmountRoundOff = Math.round(discountAmount * 100.0) / 100.0;
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
		  double extPrice=0.0;
		  if(item!=null && item.getPricingWarehouse()!=null) {
		   price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(item.getPricingWarehouse().getCustomerPrice()));
		   extPrice=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(item.getPricingWarehouse().getExtendedPrice()));
		   item.getPricingWarehouse().setCustomerPrice(price);
		   item.getPricingWarehouse().setExtendedPrice(extPrice);
		   product.setPrice(price);
		  }
		 }

	public SolrQuery getquery(String queryString , SolrQuery query) {
		if((queryString.toLowerCase().contains("pipe") ||  queryString.toLowerCase().contains("pipes")) && (!queryString.toLowerCase().contains("fitting") || !queryString.toLowerCase().contains("fittings") || !queryString.toLowerCase().contains("fit")))
        {
               String pipeCategory = ClientUtils.escapeQueryChars("Pipe Tubing & Hoses");
               query.add("bq","level1Category:\"" +pipeCategory+"\"^10");
        }
			
        if((queryString.toLowerCase().contains("pipe") ||  queryString.toLowerCase().contains("pipes")) && (queryString.toLowerCase().contains("fitting") || queryString.toLowerCase().contains("fittings") || queryString.toLowerCase().contains("fit")))
        {
               String pipeFittingCategory = ClientUtils.escapeQueryChars("Pipe Tube & Hose Fittings");
               query.add("bq","level1Category:\""+pipeFittingCategory+"\"^10");
        }
		return query;
	}
	public Cimm2BCentralLineItem disablePriceToErp(Cimm2BCentralLineItem cimm2bCentralLineItem, ProductsModel item){
		 cimm2bCentralLineItem.setUnitPrice(null);
		 cimm2bCentralLineItem.setListPrice(0);
		 return cimm2bCentralLineItem;
	}
	public void setWrittenBy(Cimm2BCentralOrder orderRequest) {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();			
		orderRequest.setWrittenBy(CommonUtility.validateString(request.getHeader("User-Agent")).equalsIgnoreCase("WEBVIEW")?LayoutLoader.getMessageProperties().get(CommonUtility.validateString((String)session.getAttribute("localeCode")).toUpperCase()).getProperty("orderRequest.writtenBy.mobileUser"):LayoutLoader.getMessageProperties().get(CommonUtility.validateString((String)session.getAttribute("localeCode")).toUpperCase()).getProperty("orderRequest.writtenBy.webUser"));
		}
	
	public boolean sendItemNotInCimmMail(List<ProductsModel> itemsNotInCimm,String orderNumber) {		 

	    HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		boolean flag = false;
        try{
        	
        	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("productNotInCimmMail");
            VelocityContext context = new VelocityContext();
        	context.put("firstName", session.getAttribute("userFirstName"));
            context.put("lastName", session.getAttribute("userLastName"));
            context.put("companyName",session.getAttribute("userJobTitle"));
            context.put("orderNumber",orderNumber);
            context.put("itemList",itemsNotInCimm);
            context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
            }
            
            
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
            
            StringBuilder finalMessage= new StringBuilder();
             finalMessage.append(writer.toString());

			String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
			if(notificationDetail.get("FROM_EMAIL")!=null){
				fromEmail = notificationDetail.get("FROM_EMAIL");
			}
			String toMailList="";
			if(notificationDetail.get("TO_EMAIL")!=null && notificationDetail.get("TO_EMAIL").trim().length()>0){
				toMailList= notificationDetail.get("TO_EMAIL");
			}
			notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+toMailList);
			notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
			
			flag = new SendMailUtility().sendNotification(notificationDetail,"Item not in PIM", fromEmail, finalMessage.toString());
			
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
 		return flag;	
	}
	
	public void setFromMailAddress(String fromEmail,LinkedHashMap<String, String> notificationDetail) {
		notificationDetail.put("FROM_MAIL_ADDRESS",CommonUtility.validateString(fromEmail).length()>0?fromEmail:CommonDBQuery.getSystemParamtersList().get("FROMMAILID"));
		}
}
