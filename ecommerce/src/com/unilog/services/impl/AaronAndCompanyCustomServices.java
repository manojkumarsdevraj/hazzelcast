package com.unilog.services.impl;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.google.gson.JsonObject;
import com.hazelcast.util.Base64;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.SendMailUtility;
import com.unilog.users.AddressModel;
import com.unilog.users.CreditApplicationModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.ConvertHtmlToPdf;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;
import com.unilog.services.UnilogFactoryInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.model.ProductManagementModel;
import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.ecomm.model.Cart;
import com.unilog.ecomm.model.Coupon;
import com.unilog.ecomm.model.Discount;
import com.unilog.ecomm.model.LineItem;
import com.unilog.ecomm.promotion.SalesPromotionService;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.SalesDAO;
import com.unilog.sales.SalesModel;
import com.unilog.services.factory.UnilogEcommFactory;
import com.unilog.utility.CommonUtility;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import com.unilog.database.ConnectionManager;
import java.sql.Connection;

public class AaronAndCompanyCustomServices implements UnilogFactoryInterface{
		private static UnilogFactoryInterface serviceProvider;
		private HttpServletRequest request;
		private AaronAndCompanyCustomServices() {}
		
		public static UnilogFactoryInterface getInstance() {
				synchronized (AaronAndCompanyCustomServices.class) {
					if(serviceProvider == null) {
						serviceProvider = new AaronAndCompanyCustomServices();
					}
				}
			return serviceProvider;
		}
		public String CreditAppRegistrationAndPdf(AddressModel userRegistrationDetail, String result,CreditApplicationModel creditAplication,HttpSession session)
		{
			SendMailUtility sendMailUtility = new SendMailUtility();
			boolean sentFlag = false;
			UsersModel userAddress = new UsersModel();
			userAddress.setFirstName(userRegistrationDetail.getFirstName());
			userAddress.setLastName(userRegistrationDetail.getLastName());
			if(userRegistrationDetail.getCompanyName()!=null && !userRegistrationDetail.getCompanyName().trim().equalsIgnoreCase("")){
				userAddress.setEntityName(userRegistrationDetail.getCompanyName());
			}else{
				userAddress.setEntityName(userRegistrationDetail.getFirstName() +" "+userRegistrationDetail.getLastName());
			}
			userAddress.setAddress1(userRegistrationDetail.getAddress1());
			userAddress.setAddress2(userRegistrationDetail.getAddress2());
			userAddress.setCity(userRegistrationDetail.getCity());
			userAddress.setState(userRegistrationDetail.getState());
			userAddress.setZipCode(userRegistrationDetail.getZipCode());
			  if(userRegistrationDetail.getCountry().equalsIgnoreCase("USA")){
				  userAddress.setCountry(userRegistrationDetail.getCountry());
			  }else{
				  if(userRegistrationDetail.getCountry()!=null && userRegistrationDetail.getCountry().trim().length()>0){
					  userAddress.setCountry(userRegistrationDetail.getCountry());
				  }else{
					  userAddress.setCountry("USA");  
				  }
			  }
			  userAddress.setPhoneNo(userRegistrationDetail.getPhoneNo());//.replaceAll("[^a-zA-Z0-9]", "")
			  userAddress.setEmailAddress(userRegistrationDetail.getEmailAddress());
			  userAddress.setNewsLetterSub("");//--------------------------
			  userAddress.setPassword(userRegistrationDetail.getUserPassword());
			  //start
			  UsersDAO usersDAO = new UsersDAO();
  	    	int userId = 1;
  	    	//int creditApplicationId = usersDAO.insertCustomerCreditApplicationDetails(userId, creditAplication);
  	    	
  	    	//CreditApplicationTemplate
  	    	userRegistrationDetail.setFormHtmlDetails(sendMailUtility.buildCreditApplictionForm(creditAplication));
  	    	//------- Generate PDF
  	    	String creditAppFileName = "CreditApplication_"+session.getId()+".pdf";
  	    	if(userRegistrationDetail!=null && userRegistrationDetail.getFormHtmlDetails()!=null && userRegistrationDetail.getFormHtmlDetails().length()>0){
  	    		ConvertHtmlToPdf convertHtmlToPdf = new ConvertHtmlToPdf();
  	    		convertHtmlToPdf.gerratePdfFromHtml(userRegistrationDetail.getFormHtmlDetails(), creditAppFileName);
  	    	}
  	    	//-------Generate PDF
  	    	
  	    	String filePath = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH"));
  	    	String[] attachments = new String[3];
  	    	String[] attachmentsFileName = new String[3];
  	    	attachments[0] = filePath+"/"+creditAppFileName;
  	    	attachmentsFileName[0] = creditAppFileName;		        	    	
  	    	if(creditAplication.getSignature()!=null) {
  	    		String encoded=creditAplication.getSignature();
  	    		byte[] image=encoded.getBytes();
  	    		String sign=session.getId();
  	    		String imageFileName=CommonDBQuery.getSystemParamtersList().get("SIGNATURE_UPLOAD_DIRECTORYPATH")+"/"+sign +".jpg";
  	    		
  	    		File file = new File(imageFileName);
  	    		
  	    		String imgpath= sign +".jpg";
  	    		/*if(!file.exists())
  		         {
  					System.out.println(file+" : destination dir doesnt exists");
  		        	 	
  		         }
  				else{
  					System.out.println(file+" : destination dir exists");
  				}*/
  	    		if (image == null) {
  		        	System.out.println("Buffered Image is null");
  		        }
  	    		else
  	    		{
  	    			FileOutputStream fo;
					try {
						fo = new FileOutputStream(imageFileName);
						byte[] byteArray = Base64.decode(image);
	  	    			fo.write(byteArray);
	  	    			attachments[1] =imageFileName;
	  	    			attachmentsFileName[1] = userRegistrationDetail.getFirstName();
	  	    			if(fo!=null){
				        	 fo.close();
				 		 			}
	  	    			
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
  	    		}
  	    	}
  	    	if(creditAplication.getSalesTaxStatusRadio().equalsIgnoreCase("No")){
  	    		attachments[2]=creditAplication.getSalesTaxExemptionCertificateFileName();
  	    		attachmentsFileName[2]="Sales_Attachment";
  	    				
  	    	}
  	    	LinkedHashMap<String, Object>contentData = new LinkedHashMap<String, Object>();
  	    	//contentData.put("creditAplication", creditAplication);
  	    	contentData.put("NotificationTemplateName", "CreditApplicationRequestMailToCustomer");//CommonDBQuery.getSystemParamtersList().get("CreditApplicationRequestMailToCustomer"));			        	    	
  	    	contentData.put("EMAIL_TO", creditAplication.getDeclaratioEmailAddress());
  	    	contentData.put("session", session);
  	    	//contentData.put("attachments", attachments);
  	    	//contentData.put("attachmentsFileName", attachmentsFileName);
  	    	sentFlag = CreditApplicationRequestMailToUser(userAddress, contentData);
  	    	if(sentFlag==true) {
  	    		userAddress.setEmailAddress("");
  	    		System.out.println("CreditApplicationRequestMailToCustomer : "+sentFlag);
  	    		contentData.put("session", session);
  	    		contentData.put("creditAplication", creditAplication);
  	  	    	contentData.put("attachments", attachments);
  	  	    	contentData.put("attachmentsFileName", attachmentsFileName);
  	    		contentData.put("EMAIL_TO", "");
  	    		contentData.put("NotificationTemplateName", "CreditApplicationRequestMailToCreditManager");
  	    		sentFlag = sendMailUtility.sendCreditApplicationRequestMail(userAddress, contentData);
  	    		System.out.println("CreditApplicationRequestMailToCreditManager : "+sentFlag);
  	    		if(sentFlag==true){
  	  	    		result = "1|Registration request sent successfully";
  	  	    	}else{
  	  	    		result = "0|Error while sending email. kindly contact customer Care ";
  	  	    	}
  	    	}else{
  	    		result = "0|Error while sending email. kindly contact customer Care ";
  	    	}
  	    	System.out.println("creditApp result:" + result);
			return result;
		}
		public void addDiscountToLineItems(ArrayList<Cimm2BCentralLineItem> lineItems,double discountAmount,HttpSession htSession) {
			
			HttpSession session = htSession;
			if(discountAmount > 0){
				DecimalFormat df = CommonUtility.getPricePrecision(session);
			double discountAmountRoundOff = CommonUtility.validateDoubleNumber(df.format((discountAmount * 100.0) / 100.0));
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
		public void enableDiscountCoupon(HttpSession session,LinkedHashMap<String, Object> contentObject,ArrayList<String> shipVia,String discountCoupons,Map<String, Object> details,List<ProductsModel> cartDetails) {
		
		ArrayList<String> invalidCoupons = new ArrayList<String>();
		List<Discount> appliedlDiscounts = new ArrayList<Discount>();
		String discountAvailed = "no";
		double orderItemsDiscountVal = 0.0D;
		double orderDiscountVal = 0.0D;
		double totalSavingOnOrder = 0.0D;
		double total = 0;
		String  freeShipping = "no";
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_DISCOUNT_COUPONS_ENABLED")).equalsIgnoreCase("Y")){
			Cart cart = null;
			if(cartDetails!= null && cartDetails.size() > 0){
				// removing old discount calculation if any from session 
				session.removeAttribute("availedDiscounts");
				session.removeAttribute("appliedlDiscounts");
				cart = new Cart();
				cart.setCustomerName((String) session.getAttribute("loginCustomerName"));
				cart.setUserId(userId);
				cart.setBuyingCompanyId(Long.parseLong(session.getAttribute("buyingCompanyId").toString()));
				cart.setUserName((String) session.getAttribute(Global.USERNAME_KEY));
				cart.setWebsite((String)session.getAttribute("websiteName"));
				LinkedHashMap<String, String> userCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("userCustomFieldValue");
				if(userCustomFieldValue!=null && CommonUtility.validateString(userCustomFieldValue.get("USER_LEVEL_COUPON_GROUPS")).length()>0){
					cart.setUserGroupName(userCustomFieldValue.get("USER_LEVEL_COUPON_GROUPS"));	
				}
				LinkedHashMap<String, String> customerCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("customerCustomFieldValue");
				if(customerCustomFieldValue!=null && CommonUtility.validateString(customerCustomFieldValue.get("CUSTOMER_COUPON_GROUP")).length()>0){
					cart.setCustomerGroupName(customerCustomFieldValue.get("CUSTOMER_COUPON_GROUP"));
				}
				if(session.getAttribute("wareHouseName") != null){
					cart.setWareHouseName( (String)session.getAttribute("wareHouseName"));
				}
				if(shipVia==null){
					if(session.getAttribute("selectedShipVia")!=null){
						cart.setSelectedShipMethod((String) session.getAttribute("selectedShipVia"));
					}
				}else{
					cart.setSelectedShipMethod(shipVia);
				}
				//TODO set Customer Group Name and User Group Name 
				for(ProductsModel cartItem : cartDetails){
					LineItem lineItem =  new LineItem();
					lineItem.setLineItemId(cartItem.getProductListId());
					lineItem.setPartNumber(cartItem.getPartNumber());
					lineItem.setOrderQuantity(cartItem.getQty());
					lineItem.setPrice((float) cartItem.getPrice());
					lineItem.setExtendedPrice((float) cartItem.getTotal());
					if(cartItem.getCustomFieldVal() != null && cartItem.getCustomFieldVal().size() > 0 && cartItem.getCustomFieldVal().get("custom_ITEM_LEVEL_COUPON_GROUPS") != null && CommonUtility.validateString(cartItem.getCustomFieldVal().get("custom_ITEM_LEVEL_COUPON_GROUPS").toString()).length() > 0){
						if(CommonUtility.validateString((String)cartItem.getCustomFieldVal().get("custom_ITEM_LEVEL_COUPON_GROUPS")).length()>0){
							lineItem.setItemGroupName((String)cartItem.getCustomFieldVal().get("custom_ITEM_LEVEL_COUPON_GROUPS"));
						}
					}
					if(session.getAttribute("wareHouseName") != null){
						lineItem.setWareHouseName( (String)session.getAttribute("wareHouseName"));
					}
					cart.getLineItems().add(lineItem);
				}

				try{
					SalesPromotionService promotionService = SalesPromotionService.getInstance();

					List<Coupon> couponsList = new ArrayList<Coupon>();

					if(discountCoupons != null && !discountCoupons.trim().isEmpty()){
						for(String couponCode: discountCoupons.split(",")){
							Coupon coupon = new Coupon();
							coupon.setCopounCode(couponCode);
							couponsList.add(coupon);
						}
					}
					promotionService.checkDiscount(cart,couponsList);
					for(LineItem discountedLineItem: cart.getLineItems()){
						for(ProductsModel cartItem : cartDetails){
							if(cartItem.getProductListId() == discountedLineItem.getLineItemId() ){
								cartItem.setdDiscountValue(discountedLineItem.getDiscount());
								cartItem.setTotal(discountedLineItem.getExtendedPrice());
								cartItem.setNetPrice(discountedLineItem.getNetPrice());
								if(discountedLineItem.getAvailedDiscount() != null ){
									cartItem.setPromoCode(discountedLineItem.getAvailedDiscount().getDiscountCoupon().getCopounCode());
									appliedlDiscounts.add(discountedLineItem.getAvailedDiscount());
								}
								orderItemsDiscountVal += discountedLineItem.getDiscount();	
							}
						}
						orderItemsDiscountVal += discountedLineItem.getDiscount();	
					}
						
					if(cart.isItemDiscountAvailed()){	
						discountAvailed = "yes";
					}
					if(cart.getDiscount() > 0.0 ){
						orderDiscountVal = cart.getDiscount();
						appliedlDiscounts.addAll(cart.getAvailedDiscounts());
						discountAvailed = "yes";
					}
					if(cart.isFreeShipping()){
						freeShipping = "yes";
						appliedlDiscounts.add(cart.getAvailedShippingDiscount());
					}
					total = cart.getTotal();
					totalSavingOnOrder = orderItemsDiscountVal + orderDiscountVal;
					// setting in session to use in save sales order
					Collection<String> appliedCouponsArray = new ArrayList<String>();
					for(Discount test:appliedlDiscounts){
						appliedCouponsArray.add(test.getDiscountCoupon().getCopounCode());
					}
					
					for(Coupon invalidCoupon : cart.getRejectedCoupons()){
						invalidCoupons.add(invalidCoupon.getCopounCode());
					}
					session.setAttribute("availedDiscounts", cart);
					session.setAttribute("appliedDiscountCoupons", appliedlDiscounts);	
				}catch(Exception e){
					e.printStackTrace();
				}
			}		
			

		}
		DecimalFormat df = CommonUtility.getPricePrecision(session);
		double totalSavingOnOrderOfCoupons = CommonUtility.validateDoubleNumber(df.format((totalSavingOnOrder * 100.0) / 100.0));
		details.put("totalSavingOnOrder", totalSavingOnOrderOfCoupons);
		
		contentObject.put("discountAvailed",discountAvailed);
		contentObject.put("appliedDiscountCoupons",appliedlDiscounts);
		contentObject.put("orderItemsDiscount", orderItemsDiscountVal);
		contentObject.put("orderDiscount", orderDiscountVal);
		contentObject.put("totalSavings", totalSavingOnOrderOfCoupons);
		contentObject.put("appliedDiscountCoupons",appliedlDiscounts);	
	}
	public void erpQuanitityBreakUpdate(ProductManagementModel priceInquiryInput,ProductsModel productsModel,ArrayList<ProductsModel> quantityBreak) {
			if(priceInquiryInput.getPartIdentifierQuantity()!=null){
				for(int j=priceInquiryInput.getPartIdentifierQuantity().size()-1;j>=0;j--) {
					for(int i=quantityBreak.size()-1;i>=0;i--)
					 {
						if(priceInquiryInput.getPartIdentifier().get(j).getPartNumber().equals(quantityBreak.get(i).getPartNumber()) && priceInquiryInput.getPartIdentifierQuantity().get(j) >0 && priceInquiryInput.getPartIdentifierQuantity().get(j)>= quantityBreak.get(i).getMinimumQuantityBreak())
						{
							System.out.println(quantityBreak.get(i).getCustomerPriceBreak() + " : " + quantityBreak.get(i).getMinimumQuantityBreak()+ " : " + quantityBreak.get(i).getMaximumQuantityBreak());
							double price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(quantityBreak.get(i).getCustomerPriceBreak()));
							productsModel.setPrice(price); 
							break;
						}
					 }
				}
			}
	}
	public void getDiscountPrice(SalesModel orderDetail,VelocityContext context) {
		HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
		DecimalFormat df2 = CommonUtility.getPricePrecision(session);
		for(SalesModel erpOrderItem : orderDetail.getOrderList()){
            if(CommonDBQuery.getSystemParamtersList().get("DISCOUNT_PART_NUMBER")!=null && erpOrderItem.getPartNumber()!=null && CommonDBQuery.getSystemParamtersList().get("DISCOUNT_PART_NUMBER").trim().equalsIgnoreCase(erpOrderItem.getPartNumber().trim())){
            	erpOrderItem.setCashDiscountAmount(erpOrderItem.getUnitPrice()>0?erpOrderItem.getUnitPrice():0.0);
            	context.put("discountCalculated", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(erpOrderItem.getCashDiscountAmount()))));
            	context.put("subTotalAmount", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getSubtotal()))));
            	context.put("discountOrderTotal", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTotal()-(erpOrderItem.getCashDiscountAmount())))));
            }
        }
	}
	public String setFirstLoginTrueForInactiveUser() {
		return "Y";
	}
	 public Cimm2BCentralLineItem  disableUmqtyToErp(Cimm2BCentralLineItem cimm2bCentralLineItem){
		 cimm2bCentralLineItem.setUomQty("");
		 return cimm2bCentralLineItem;
	 }
	 public void pricingBranchCode(Cimm2BCentralOrder order,Map<String, Object> otherDetails) {
			String wareHouseCode = CommonUtility.validateString((String) otherDetails.get("wareHouseCode"));
			order.setPricingBranchCode(wareHouseCode);
			order.setShipBranchId(CommonUtility.validateString((String) otherDetails.get("pickUpWareHouseCode")));
	 }
	 public void pricingBranchId(Cimm2BCentralOrder orderRequest) {
		 	HttpServletRequest request =ServletActionContext.getRequest();
		 	HttpSession session = request.getSession();
		 	String salesLocationId = "";
		 	String wareHousecode = "";
		 	if(session!=null && (String) session.getAttribute("salesLocationId")!=null){
		 	salesLocationId = CommonUtility.validateString((String) session.getAttribute("salesLocationId"));}
		 	if(session!=null && (String) session.getAttribute("wareHouseCode")!=null){
		 	wareHousecode = CommonUtility.validateString((String) session.getAttribute("wareHouseCode"));}
			orderRequest.setShippingBranch(salesLocationId);
			orderRequest.setPricingBranchCode(wareHousecode);
	 }
	 public void pickUpSelectedBranch(String pickUpWareHouseCode) {
		 if(CommonUtility.validateString(pickUpWareHouseCode).trim().length() > 0){
			 	HttpServletRequest request =ServletActionContext.getRequest();
			 	HttpSession session = request.getSession();
				session.setAttribute("salesLocationId",pickUpWareHouseCode);
			}
	 }
	 public void getPickUpBranchCode(Cimm2BCentralOrder orderRequest,String salesLocationId) {
		 orderRequest.setBranchCode(salesLocationId);
		}
	 public boolean CreditApplicationRequestMailToUser(UsersModel userDetailsAddresss, LinkedHashMap<String, Object>contentData) {

			boolean flag = false;
			ResultSet rs = null;
		    Connection  conn = null;
		    String subject = "";
		    String to = "";
		    
		    try{
		    	HttpSession session =  (HttpSession) contentData.get("session");
		    	SaveCustomFormDetails getNotificationDetail = new SaveCustomFormDetails();
		    	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails(CommonUtility.validateString(contentData.get("NotificationTemplateName").toString()));
		    	if(notificationDetail!=null && notificationDetail.size()>0){
		    	
			        	String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
						if(notificationDetail!=null && notificationDetail.get("FROM_EMAIL")!=null){
							fromEmail = notificationDetail.get("FROM_EMAIL");
						}
						if(notificationDetail!=null) {
							notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
						}
						VelocityContext context = new VelocityContext();
						context.put("WelcomeText",CommonDBQuery.getSystemParamtersList().get("MAIL_WELCOME_MSG"));
						context.put("webAddress",CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
						if(userDetailsAddresss!=null)
						{
							context.put("firstName", userDetailsAddresss.getFirstName());
				            context.put("lastName", userDetailsAddresss.getLastName());
				            context.put("email", userDetailsAddresss.getEmailAddress());
				            context.put("password", userDetailsAddresss.getPassword());
				            context.put("newsLetter", userDetailsAddresss.getNewsLetterSub());
				            context.put("address1",userDetailsAddresss.getAddress1());
				            context.put("address2",userDetailsAddresss.getAddress2());
				            context.put("city",userDetailsAddresss.getCity());
				            context.put("state",userDetailsAddresss.getState());
				            context.put("zip",userDetailsAddresss.getZipCode());
				            context.put("phone",userDetailsAddresss.getPhoneNo());
						}
						
						to = notificationDetail.get("TO_EMAIL");
						if(to==null){
							to = notificationDetail.get("BCC_EMAIL");
						}
						if(CommonUtility.validateString(contentData.get("EMAIL_TO").toString()).length()>0){
							if(CommonUtility.validateString(to).length()>0){
								to = to+";"+CommonUtility.validateString(contentData.get("EMAIL_TO").toString());
							}else{
								to = CommonUtility.validateString(contentData.get("EMAIL_TO").toString());
							}
						}
						
						subject = LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("sentmailconfig.sendRegistrationMail.webUser.2C.subject");
						
						context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
			            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
			            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
			            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
			            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
			            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
			            }
			            
			            
			            StringWriter writer = new StringWriter();
			            if(notificationDetail!=null && notificationDetail.get("DESCRIPTION")!=null) {
			            	Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
			            }
			            
			            StringBuilder finalMessage= new StringBuilder();
			            finalMessage.append(writer.toString());
			            if(notificationDetail!=null){
			            	if(CommonUtility.validateString(to).contains(CommonUtility.validateString(notificationDetail.get("TO_EMAIL")))){
			            		notificationDetail.put("TO_EMAIL",to);
			            	}else{
			            		notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+to);
			            	}
			            	notificationDetail.put("SUBJECT", subject);
			            	notificationDetail.put("FROM_MAIL_ADDRESS", fromEmail);
			            	notificationDetail.put("EMAIL_CONTENT_TO_SEND", finalMessage.toString());
			            	
			            	 SendMailUtility sendMailUtility = new SendMailUtility();
			           
				 			flag = sendMailUtility.sendNotification(notificationDetail, subject, fromEmail, finalMessage.toString());
				            System.out.println("flag : "+flag);
							
				           
			            }
		        	}else{
			    		System.out.println("-------------- Note : sendRegistrationMail mail sending : template Not Available in CIMM2 Notification");
			    	}
		        
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    }
		    finally
		    {
		    	ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBConnection(conn);
		    }
		    return flag;
		}
}
