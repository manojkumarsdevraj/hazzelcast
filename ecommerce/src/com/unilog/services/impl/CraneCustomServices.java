package com.unilog.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.model.SalesOrderManagementModel;
import com.hazelcast.util.Base64;
import com.unilog.custommodule.model.FreightCalculatorModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.ecomm.model.Cart;
import com.unilog.ecomm.model.Coupon;
import com.unilog.ecomm.model.Discount;
import com.unilog.ecomm.model.LineItem;
import com.unilog.ecomm.promotion.SalesPromotionService;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.AddressModel;
import com.unilog.users.CreditApplicationModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.ConvertHtmlToPdf;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class CraneCustomServices implements UnilogFactoryInterface  {
	private static UnilogFactoryInterface serviceProvider;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private CraneCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (CraneCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new CraneCustomServices();
				}
			}
		return serviceProvider;
	}

	public int CustomizatedSubsetId( UsersModel userDetailList, int subsetId) {
		if(!CommonUtility.validateString(userDetailList.getSubsetFlag()).equalsIgnoreCase("YES")) {
			 subsetId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("NOT_WISCONSIN_SUBSET"));
		}
		return subsetId;
	}
	
	
	public AddressModel CustomizedBuyingCompany(AddressModel userRegistrationDetail) {
		
		if(CommonUtility.validateString(userRegistrationDetail.getSubsetFlag()).equalsIgnoreCase("YES")) {
			userRegistrationDetail.setBuyingComanyIdStr(String.valueOf(UsersDAO.getBuyingCompanyIdByEntityId(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID"))));
			}else{
				userRegistrationDetail.setBuyingComanyIdStr(String.valueOf(UsersDAO.getBuyingCompanyIdByEntityId(CommonDBQuery.getSystemParamtersList().get("NOT_WISCONSIN_DEFAULT_CUSTOMER_ERP_ID"))));
			}
		return userRegistrationDetail;
	}
	
	
	public String customizedCustomerNumber(UsersModel contactInformation,String custNumber) {
		
		if(CommonUtility.validateString(contactInformation.getSubsetFlag()).equalsIgnoreCase("YES")) {
				custNumber = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID"));
			}else{
				custNumber = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NOT_WISCONSIN_DEFAULT_CUSTOMER_ERP_ID"));
			}
		return custNumber;
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
					details.put("totalSavingOnOrder", totalSavingOnOrder);
					session.setAttribute("availedDiscounts", cart);
					session.setAttribute("appliedDiscountCoupons", appliedlDiscounts);	
				}catch(Exception e){
					e.printStackTrace();
				}
			}		
			

		}
}

@Override
public double getExtendedPrice(double price, int orderqty, int salesQty, double salesQuntityUom) {
	double extendedPrice = 0.0;
	try {
		if(!(salesQty>0)){
			salesQty = 1;
		}
		extendedPrice = (price * orderqty);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return extendedPrice;
}

@Override
public void updateUom(Cimm2BCentralLineItem cimm2bCentralLineItem ,ProductsModel item) {
	
	try {
		cimm2bCentralLineItem.setUom(item.getUom());
		
	} catch (Exception e) {
		e.printStackTrace();
	}	
}

public void orderFreightcalculation(Map<String, Object> details,List<ProductsModel> cartDetails) {
		try{

			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			double freightBaseAmount=0;
			double freightPercentage=0;
			double handlingBaseAmount=0;
			double handlingPercentage=0;
			double freight_Min_Threshold=0;
			double freight_Max_Threshold=0;
			double weight = 0;
			Map<String, String> customerCustomFieldValue = (Map<String, String>) session.getAttribute("customerCustomFieldValue");
			if(customerCustomFieldValue==null || (customerCustomFieldValue!=null && !(CommonUtility.validateString(customerCustomFieldValue.get("NO_FREIGHT")).equalsIgnoreCase("Y")))){
				double additional = Double.valueOf(CommonDBQuery.getSystemParamtersList().get("ADDITIONAL_CHARGES_OVERWEIGHT").toString());
				for(ProductsModel item : cartDetails){
					if(item.getWeight()>0.0){
						weight = item.getWeight()*item.getQty();
					}
				}
				String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
				 int userId = CommonUtility.validateNumber(sessionUserId);
					FreightCalculatorModel freightInput = new FreightCalculatorModel();  
					String shipVia = (String) session.getAttribute("selectedShipVia");
					String ShipViaDisplayName = null;
					List<Map<String, String>> customShipVias = null;
						  List<CustomTable> customValues = CIMM2VelocityTool.getInstance().getCusomTableData("Website","FRIGHT_TABLE");
						  if(customValues !=null && customValues.size() > 0){
						   customShipVias = customValues.get(0).getTableDetails();    
						  }
						  
						  List<Map<String, String>> ShipViaCostTable = null;
						  List<CustomTable> shipValues = CIMM2VelocityTool.getInstance().getCusomTableData("Website","SHIPVIA_COST_TABLE");
						  if(shipValues !=null && shipValues.size() > 0){
							  ShipViaCostTable = shipValues.get(0).getTableDetails();    
						  }
						  for(Map<String, String> tableValues :ShipViaCostTable){
							  if(tableValues.get("SHIP_VIA_CODE") != null) {
								  if(tableValues.get("SHIP_VIA_CODE").equals(shipVia)){
									  ShipViaDisplayName = tableValues.get("SHIP_VIA_NAME");
									  break;
								  }
							  }
						}
						  if(ShipViaDisplayName!=null && (!ShipViaDisplayName.contains("Collect"))) {
						 for(Map<String, String> tableValues :customShipVias){
							 freightBaseAmount=Double.parseDouble(tableValues.get("Frieght_Base_Amount"));
					         handlingBaseAmount=Double.parseDouble(tableValues.get("Handling_Base_Amount"));
					 		 handlingPercentage=Double.parseDouble(tableValues.get("Frieght_Percentage"));
					 		freight_Min_Threshold=Double.parseDouble(tableValues.get("Freight_Min_Threshold")); 
					 		freight_Max_Threshold=Double.parseDouble(tableValues.get("Freight_Max_Threshold")); 
							    		 if((cartDetails.get(0).getCartTotal() >= freight_Min_Threshold) && (cartDetails.get(0).getCartTotal()  <= freight_Max_Threshold)) {
							    			 if(freightBaseAmount==0) {
							    				 double freight = ((cartDetails.get(0).getCartTotal()*(handlingPercentage/100)));
							    				 if(weight >=150){
							    					 freight = freight + additional; 
							    					 details.put("otherCharges", freight);
							    				 }else {
							    					 details.put("otherCharges", freight); 
							    				 }
							    				 break;
							    			 }else {
							    				 if(weight >=150){
							    					 freightBaseAmount = freightBaseAmount + additional;
							    					 details.put("otherCharges", freightBaseAmount);
							    				}else {
							    					details.put("otherCharges", freightBaseAmount);
							    				}
							    				 break;
							    			 }
							    		 }	 
					}	
						  
			 }
			}
			
			}catch(Exception e){
				e.printStackTrace();
			}
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
	  userAddress.setSubsetFlag(userRegistrationDetail.getSubsetFlag()!=null?userRegistrationDetail.getSubsetFlag():"");
	  
	  
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
  	try {
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
  	} catch (Exception e) {
		e.printStackTrace();
	}
  	if(creditAplication.getSalesTaxStatusRadio().equalsIgnoreCase("No")){
  		attachments[2]=creditAplication.getSalesTaxExemptionCertificateFileName();
  		attachmentsFileName[2]="Sales_Attachment";
  				
  	}
  	LinkedHashMap<String, Object>contentData = new LinkedHashMap<String, Object>();
  	contentData.put("creditAplication", creditAplication);
  	contentData.put("NotificationTemplateName", "CreditApplicationRequestMailToCustomer");//CommonDBQuery.getSystemParamtersList().get("CreditApplicationRequestMailToCustomer"));			        	    	
  	contentData.put("EMAIL_TO", creditAplication.getDeclaratioEmailAddress());
  	contentData.put("session", session);
  	contentData.put("attachments", attachments);
  	contentData.put("attachmentsFileName", attachmentsFileName);
  	sentFlag = sendMailUtility.sendCreditApplicationRequestMail(userAddress, contentData);
  	if(sentFlag==true) {
  		System.out.println("CreditApplicationRequestMailToCustomer : "+sentFlag);
  		contentData.put("session", session);
  		contentData.put("creditAplication", creditAplication);
	    	contentData.put("attachments", attachments);
	    	contentData.put("attachmentsFileName", attachmentsFileName);
  		contentData.put("EMAIL_TO", creditAplication.getDeclaratioEmailAddress());
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


public SalesModel substractingDiscountToCartItem(SalesModel salesInputParameter) {
	salesInputParameter.setCartData(null);
	salesInputParameter.setFreight(0);
	salesInputParameter.setDiscount(0);
	return salesInputParameter;
}

@Override
public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
{	
	String newsLetterCustomFiledValue="";
	if(CommonUtility.validateString(userDetailsInput.getNewsLetterSub()).equalsIgnoreCase("Y")){
		newsLetterCustomFiledValue="Y";
	}else{
		newsLetterCustomFiledValue="N";
	}
	UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
	
	if(CommonUtility.validateString(userDetailsInput.getCustomerName()).length()>0){
		UsersDAO.insertCustomField(CommonUtility.validateString(userDetailsInput.getCustomerName()),"USER_COMPANY_NAME",userid, buyingCompanyid,"USER");	
	}else{
		UsersDAO.insertCustomField(CommonUtility.validateString(userDetailsInput.getFirstName() +" "+userDetailsInput.getLastName()),"USER_COMPANY_NAME",userid, buyingCompanyid,"USER");	
	}
	
}
@Override
public int setBillAddressToShippAddress(Connection conn, UsersModel customerinfo, int shipId) {
	String entityid="0";
	entityid = customerinfo.getEntityId();
	customerinfo.setEntityId(null);
	shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn,customerinfo,"Ship",false);
	customerinfo.setEntityId(entityid);
	return shipId;
}


@Override
public void assignEntityIdForShip(UsersModel usersModel, HttpSession session) {
	String userToken = (String) session.getAttribute("userToken");
	if(CommonUtility.validateString(usersModel.getEntityId()).length()==0) {
		usersModel.setEntityId(userToken);
	}		
}

public UsersModel getUserContactAddress(int userId,  HttpSession session) {
	UsersModel addressList = null;
	String isRetailUser = (String) session.getAttribute("isRetailUser");
	if(CommonUtility.validateString(isRetailUser).equalsIgnoreCase("Y")){
		addressList = new UsersModel();
		addressList= UsersDAO.getEntityDetailsByUserId(userId);
		addressList.setAddressType("Ship");
		session.setAttribute("userContactAddress", addressList);
	}
	return addressList;
}

public int updateCartCountWithSumOfQty(int totalqty, HttpSession session) {
	totalqty = Integer.parseInt(session.getAttribute("cartItemQuantityCountSession").toString());
	return totalqty;
}


public void changeCustomerName(Cimm2BCentralOrder orderRequest,SalesOrderManagementModel salesOrderInput) {
	orderRequest.setCustomerName(salesOrderInput.getShipAddress().getFirstName()+""+salesOrderInput.getShipAddress().getLastName());
	orderRequest.setUser1("S");
	orderRequest.setUser4(salesOrderInput.getShipAddress().getEmailAddress());	
	orderRequest.setOrderNotes(salesOrderInput.getNotesIndicator());
}
public void addfirstNameAndlastNameToshipAddress(AddressModel selectedShipAddress, UsersModel userShipAddress,HttpSession session) {
	selectedShipAddress.setFirstName(userShipAddress.getFirstName());
	selectedShipAddress.setLastName(userShipAddress.getLastName());
	String isRetailUser = (String) session.getAttribute("isRetailUser");
	if(CommonUtility.validateString(isRetailUser).equalsIgnoreCase("Y")){
		selectedShipAddress.setShipToId("");
		selectedShipAddress.setEntityId("");
		selectedShipAddress.setShipToName(userShipAddress.getCustomerName());
	}
	
}

@Override
public String getRetailUserOrdersFromCIMM(String userRole) {
	try{
		request =ServletActionContext.getRequest();
		String orderStatus = request.getParameter("orderStatus");
		if(userRole.equalsIgnoreCase("Y") && CommonUtility.validateString(orderStatus).equalsIgnoreCase("TrackOrder")) {
			userRole="N";
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
		return userRole;
}
	
@Override
public String customizedSubjectMailToCustomerAndCreditManager(LinkedHashMap<String, Object> contentData, String subject){
		if(contentData.get("NotificationTemplateName").equals("CreditApplicationRequestMailToCustomer")) {
			return subject = "New Credit Application";
		}else {
			return subject = "New Credit Request";
		}		
}

@Override
public String setFirstLoginTrueForInactiveUser() {
	return "Y";
}

}