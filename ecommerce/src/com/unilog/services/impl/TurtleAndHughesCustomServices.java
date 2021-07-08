package com.unilog.services.impl;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.erp.service.SalesOrderManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomer;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralCustomerCard;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralGetInvoiceList;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralSalesOrderHistoryRequest;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralShipVia;
import com.erp.service.cimmesb.utils.CimmESBServiceUtils;
import com.erp.service.impl.SalesOrderManagementImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.avalara.AvalaraUtility;
import com.unilog.avalara.model.AvalaraAddress;
import com.unilog.avalara.model.AvalaraAddressResponse;
import com.unilog.avalara.model.AvalaraMessages;
import com.unilog.avalara.model.AvalaraValidatedAddress;
import com.unilog.cimmesb.client.ecomm.response.ECommBlanketPOResponse;
import com.unilog.cimmesb.client.ecomm.response.ExternalTokenResponse;
import com.unilog.cimmesb.client.request.RestRequest;
import com.unilog.cimmesb.ecomm.cimm2bc.model.Cimm2BCResponse;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.products.ProductsModel;
import com.unilog.punchout.model.PunchoutRequestModel;
import com.unilog.sales.SalesModel;
import com.unilog.security.SecureData;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.ConvertHtmlToPdf;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;


public class TurtleAndHughesCustomServices implements UnilogFactoryInterface{
	private static UnilogFactoryInterface serviceProvider;
	static final Logger logger = Logger.getLogger(TurtleAndHughesCustomServices.class);
	private TurtleAndHughesCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (TurtleAndHughesCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new TurtleAndHughesCustomServices();
				}
			}
		return serviceProvider;
	}    
	@Override
	public LinkedHashMap<String,String> redefinedWarehouseList() {
		
		LinkedHashMap<String,String> wareHouseList  = CommonDBQuery.getActivewareHouseList();
		LinkedHashMap<String,String> activeWarehouseCodes = new LinkedHashMap<String,String>();
		try {
			if(wareHouseList!=null && wareHouseList.size()>0){
				for(Map.Entry<String,String> entry : wareHouseList.entrySet()){
					activeWarehouseCodes.put(entry.getKey(), entry.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activeWarehouseCodes;
	}

	@Override
	public LinkedHashMap<String, ProductsModel> activeWarehouseList() {
		// This code is written for T&H
		LinkedHashMap<String,String> wareHouseList  = CommonDBQuery.getActivewareHouseList();
		LinkedHashMap<String,ProductsModel> activeWarehouseCodes = new LinkedHashMap<String,ProductsModel>();
		try {
			if(wareHouseList!=null && wareHouseList.size()>0){
					for(Map.Entry<String,String> entry : wareHouseList.entrySet()){
						if(CommonDBQuery.branchDetailData.get(entry.getKey()) != null){
							activeWarehouseCodes.put(entry.getKey(),CommonDBQuery.branchDetailData.get(entry.getKey()));
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return activeWarehouseCodes;
	}

	@Override
	public String getNetworkWarehouseCode(String warehouseCode) {
		String netWrokWarehouseCode = null;
		LinkedHashMap<String,String> wareHouseList  = CommonDBQuery.getNetWorkwareHouseList();
		try {
			if(wareHouseList!=null && wareHouseList.size()>0){
				for(Map.Entry<String,String> entry : wareHouseList.entrySet()){
						if(CommonUtility.validateString(entry.getKey()).equalsIgnoreCase(warehouseCode)){
							netWrokWarehouseCode = CommonUtility.validateString(entry.getValue());
							break;
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return netWrokWarehouseCode;
	}


	@Override
	public String getUom(double salesQty) {
		String Uom = "";
		try {
			if(salesQty == 0.01){
				Uom = "HU";
			}else if(salesQty == 0.001){
				Uom = "TH";
			}else{
				Uom = "EA";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Uom;
	}

	@Override
	public double getExtendedPrice(double price, int orderqty, int salesQty, double salesQuntityUom) {
		double extendedPrice = 0.0;
		try {
			if(!(salesQty>0)){
				salesQty = 1;
			}
			extendedPrice = (price * orderqty)/(salesQty/salesQuntityUom);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return extendedPrice;
	}

	@Override
	public boolean doTwoStepValidation(Cimm2BCentralCustomer customerDetails, String invoiceNo, String poNumber,
			String zipCode) {
		
		boolean validInvoice = false;
		ArrayList<Cimm2BCentralGetInvoiceList> cimm2BInvoices = customerDetails.getCimm2BInvoices();
		try {
			if(cimm2BInvoices!=null && cimm2BInvoices.size()>0){
				for(Cimm2BCentralGetInvoiceList invoiceList : cimm2BInvoices){
					UsersModel invoices = new UsersModel();
					invoices.setInvoiceNumber(invoiceList.getInvoiceNumber());
					invoices.setPoNumber(invoiceList.getPoNumber());
					if((CommonUtility.validateString(invoiceNo).trim().length() > 0 && invoiceNo.equalsIgnoreCase(invoices.getInvoiceNumber())) || (CommonUtility.validateString(poNumber).trim().length() > 0 && poNumber.equalsIgnoreCase(invoices.getPoNumber()))){
						validInvoice = true;
						break;
					}
				}
			}
			if(!validInvoice){
				Cimm2BCentralAddress address = customerDetails.getAddress();
				if((address != null && CommonUtility.validateString(zipCode).trim().length()>0 && CommonUtility.validateString(address.getZipCode()).trim().length()>0 && ((CommonUtility.validateString(address.getZipCode()).trim().length()> 5 && CommonUtility.validateString(address.getZipCode()).trim().substring(0,5).equalsIgnoreCase(zipCode)) || CommonUtility.validateString(address.getZipCode()).trim().equalsIgnoreCase(zipCode)))) {
					validInvoice = true;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validInvoice;
	}

	@Override
	public LinkedHashMap<String, String> LoadNetworkWareHouseList() {
		
		LinkedHashMap<String, String> netWorkwareHouseList= new LinkedHashMap<String, String>();
		List<CustomTable> wareHouseList  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","NETWORK_WAREHOUSE");
		try {
			if(wareHouseList!=null && wareHouseList.size()>0){
			for(CustomTable warehouse:wareHouseList){
					for(Map<String, String> each : warehouse.getTableDetails()){
							netWorkwareHouseList.put( CommonUtility.validateString(each.get("WAREHOUSE_CODE")), CommonUtility.validateString(each.get("NETWORK_WAREHOUSE")));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return netWorkwareHouseList;
	}
	@Override
	public LinkedHashMap<String, String> LoadShipViaMapDetails() {
		
		LinkedHashMap<String, String> shipViaMapDetails= new LinkedHashMap<String, String>();
		List<CustomTable> shipViaDetails  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","SHIP_VIA_MAP");
		try {
			if(shipViaDetails!=null && shipViaDetails.size()>0){
			for(CustomTable shipviadesc:shipViaDetails){
					for(Map<String, String> each : shipviadesc.getTableDetails()){
						shipViaMapDetails.put( CommonUtility.validateString(each.get("SHIP_VIA")), CommonUtility.validateString(each.get("SHIP_VIA_DESC")));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shipViaMapDetails;
	}
	@Override
	public LinkedHashMap<String, String> LoadactiveWareHouseList() {
		LinkedHashMap<String, String> activewareHouseList= new LinkedHashMap<String, String>();
		TreeMap<Integer, String> dummyList= new TreeMap<Integer, String>();
		List<CustomTable> wareHouseList  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","ACTIVE_WAREHOUSE");
		try {
			if(wareHouseList!=null && wareHouseList.size()>0){
			for(CustomTable warehouse:wareHouseList){
					for(Map<String, String> each : warehouse.getTableDetails()){
						dummyList.put(CommonUtility.validateNumber(each.get("DISPLAY_SEQ")), CommonUtility.validateString(each.get("WAREHOUSE_CODE"))+"_"+CommonUtility.validateString(each.get("WAREHOUSE_NAME")));
					}
				}
			}
			if(dummyList!=null && dummyList.size()>0){
				for(Map.Entry<Integer,String> entry : dummyList.entrySet()){
						activewareHouseList.put( CommonUtility.validateString(entry.getValue().split("_")[0]), CommonUtility.validateString(entry.getValue().split("_")[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activewareHouseList;
	} 
	
	public String creditApplication(HttpSession session,Map<String, String> formParameters, SendMailUtility sendMailUtility, VelocityContext context, SaveCustomFormDetails getNotificationDetail, String uploadedFileNames){
		String sessionId =session.getId();
		String formName=formParameters.get("formName");
		String creditAppFileName = sessionId+"_"+"BusinessApplication.pdf";
		String filePath = "";
			try {
				if(formName!=null) {
				StringBuilder credAppformData=TurtleAndHughesCustomServices.buildCreditApplictionPdf(context,formName,getNotificationDetail);
				
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
			} catch (Exception e) {
				e.printStackTrace();
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
	
	public String convertDigitalSignature(HttpSession session, String[] signatures, String uploadedFileNames) {
		String sessionId = session.getId();
		try {
			String sigFilePath = CommonUtility
					.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH"));
			if (signatures != null) {
				for (int j = 0; j < signatures.length; j++) {
					String[] sigData = signatures[j].split("_");
					if (sigData.length == 2) {
						sigData[0] = sessionId + "_" + sigData[0];
						String sigImg = SendMailUtility.uploadImage(sigData[0], sigData[1], sigFilePath);
						if (!sigImg.isEmpty()) {
							if (uploadedFileNames != null
									&& CommonUtility.validateString(uploadedFileNames).length() > 0) {
								uploadedFileNames += "," + sigFilePath + "/" + sigImg;
							} else {
								uploadedFileNames = sigFilePath + "/" + sigImg;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadedFileNames;
	}

	public String getShipDetails(String renderContent, HttpSession session) {
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		ArrayList<UsersModel> shipAddressList = new ArrayList<UsersModel>();
		List<Cimm2BCentralCustomer> shipToList = null;
		Cimm2BCentralCustomer data = null;
		Cimm2BCentralAddress address = null;
		try {
			if (session != null && session.getAttribute("customerDetails") != null) {
				data = (Cimm2BCentralCustomer) session.getAttribute("customerDetails");
				shipToList = data.getCustomerLocations();
				UsersModel userModel = null;
				if (shipToList.size() > 0) {
					for (Cimm2BCentralCustomer shipTo : shipToList) {
						Cimm2BCentralAddress shipAddress = shipTo.getAddress();
						userModel = new UsersModel();
						userModel.setCustomerId(data.getCustomerERPId());
						userModel.setBillEntityId(data.getCustomerERPId());
						if (shipAddress != null) {
							userModel.setAddress1(shipAddress.getAddressLine1());
							userModel.setAddress2(shipAddress.getAddressLine2());
							userModel.setCity(shipAddress.getCity());
							userModel.setState(shipAddress.getState());
							userModel.setCountry(shipAddress.getCountry());
							userModel.setZipCode(shipAddress.getZipCode());
							userModel.setZipCodeStringFormat(shipAddress.getZipCode());
							userModel.setPhoneNo(shipAddress.getCustomerName());
							userModel.setEntityId(shipAddress.getPrimaryEmailAddress());
							userModel.setCustomerName(shipAddress.getCustomerName());
							userModel.setShipToId(shipAddress.getAddressERPId());
							userModel.setShipEntityId(shipTo.getCustomerERPId());
						}
						shipAddressList.add(userModel);
					}
				}
				contentObject.put("shipEntity", shipAddressList);
				contentObject.put("responseType", "salesRepCust");
				renderContent = LayoutGenerator.templateLoader("ResultLoaderPage", contentObject, null, null, null);
			}
		} catch (Exception e) {
		}
		return renderContent;
	}
	
	@Override
	public String validateAvaAddress(UsersModel validateAddress) {
		Cimm2BCentralResponseEntity response = null;
		AvalaraAddressResponse avalaraAddressResponse = null;
		List<AvalaraMessages> listOfAvalaraMessages = null;
		List<AvalaraValidatedAddress> listOfAvalaraValidatedAddress = null;
		String validAddressResponse = null;
		boolean returnValidatedAddress = false;
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		try {
			response = AvalaraUtility.getInstance().getAddress(validateAddress);
			if (response != null && response.getData() != null) {
				avalaraAddressResponse = (AvalaraAddressResponse) response.getData();
				listOfAvalaraValidatedAddress = avalaraAddressResponse.getValidatedAddresses();
				listOfAvalaraMessages = avalaraAddressResponse.getMessages();

				if (listOfAvalaraMessages != null && !listOfAvalaraMessages.isEmpty()) {
					// Address message not empty
					validAddressResponse = sb.append(gson.toJson(listOfAvalaraMessages)).toString();
				} else {
					// Compare address and validated address return the validated address when mismatch
					if (avalaraAddressResponse.getAddress() != null
							&& (avalaraAddressResponse.getValidatedAddresses() != null
									&& !avalaraAddressResponse.getValidatedAddresses().isEmpty())) {

						AvalaraAddress avalaraAddress = avalaraAddressResponse.getAddress();
						AvalaraValidatedAddress avalaraValidatedAddress = avalaraAddressResponse.getValidatedAddresses()
								.get(0); 

						// Avalara API is returning validated address with swap of address lines when both line exist in request
						if (avalaraValidatedAddress.getLine2() != null
								&& !avalaraValidatedAddress.getLine2().trim().isEmpty()) {
							if (avalaraAddress.getLine1().equalsIgnoreCase(avalaraValidatedAddress.getLine2())) {
								returnValidatedAddress = avalaraAddress.getLine2()
										.equalsIgnoreCase(avalaraValidatedAddress.getLine1());
							} else {
								returnValidatedAddress = false;
							}

						} else {
							if ((avalaraAddress.getLine1() != null && !avalaraAddress.getLine1().trim().isEmpty())
									&& (avalaraValidatedAddress.getLine1() != null
											&& !avalaraValidatedAddress.getLine1().trim().isEmpty())) {
								returnValidatedAddress = avalaraAddress.getLine1()
										.equalsIgnoreCase(avalaraValidatedAddress.getLine1());
							}
						}
						if ((avalaraAddress.getCity() != null && !avalaraAddress.getCity().trim().isEmpty())
								&& (avalaraValidatedAddress.getCity() != null
										&& !avalaraValidatedAddress.getCity().trim().isEmpty())
								&& returnValidatedAddress) {
							returnValidatedAddress = avalaraAddress.getCity()
									.equalsIgnoreCase(avalaraValidatedAddress.getCity());
						}
						if ((avalaraAddress.getRegion() != null && !avalaraAddress.getRegion().trim().isEmpty())
								&& (avalaraValidatedAddress.getRegion() != null
										&& !avalaraValidatedAddress.getRegion().trim().isEmpty())
								&& returnValidatedAddress) {
							returnValidatedAddress = avalaraAddress.getRegion()
									.equalsIgnoreCase(avalaraValidatedAddress.getRegion());
						}
						if ((avalaraAddress.getPostalCode() != null && !avalaraAddress.getPostalCode().trim().isEmpty())
								&& (avalaraValidatedAddress.getPostalCode() != null
										&& !avalaraValidatedAddress.getPostalCode().trim().isEmpty())
								&& returnValidatedAddress) {
							returnValidatedAddress = avalaraAddress.getPostalCode()
									.equalsIgnoreCase(avalaraValidatedAddress.getPostalCode());
						}
						if (!returnValidatedAddress) {
							validAddressResponse = sb.append(gson.toJson(listOfAvalaraValidatedAddress)).toString();
						}
					}
				}
			}else{
				validAddressResponse= sb.append(gson.toJson(response.getStatus())).toString();				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validAddressResponse;
	}
	
	@Override
	public void setOrderComment(Cimm2BCentralCustomerCard customerCard, Cimm2BCentralOrder salesOrderInput) {
		try {
			/* To set the credit card authorization number in Order source instead of Order comment*/
			if(CommonUtility.validateString(customerCard.getAuthorizationNumber()).length()>0) {
				salesOrderInput.setOrderSource(customerCard.getAuthorizationNumber());
			}else {
				salesOrderInput.setOrderSource("");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getBlanketPoList(HttpSession session) {
		String blanketPo= null;
		HttpServletRequest request =ServletActionContext.getRequest();
		session = request.getSession();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);
		String buyerCookie  = (String)session.getAttribute("buyCookie");
		String sql="SELECT ADDITIONAL_INFO FROM PUNCHOUT_USER_LOGIN WHERE ADDITIONAL_INFO!='null' AND BUYER_COOKIE='"+buyerCookie+"'";
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		while (rs.next()) {
		String jsonData = CommonUtility.validateString(rs.getString("ADDITIONAL_INFO"));		
		JSONObject jsonObj;
		String addressId="";
		if(CommonUtility.validateString(jsonData).length()>0) {
			Object obj = new JSONParser().parse(jsonData);
			jsonObj = (JSONObject) obj;
			addressId= (String) jsonObj.get("addressID");
			session.setAttribute("punchoutAddressID", addressId);
		}
		SecureData password=new SecureData();
		RestRequest<Void> restRequest = RestRequest.<Void>builder()
				.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
				.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
				.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
				.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString())) 
				.build();
				
		@SuppressWarnings("rawtypes")
		Cimm2BCResponse<List<ECommBlanketPOResponse>> response = CimmESBServiceUtils.getCustomerService().getCustomerBlanketPOs(restRequest, addressId);	            
		ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(response));			            
        blanketPo = CommonUtility.validateString(mapper.writeValueAsString(response));
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
	
		return blanketPo;
	}
	
	@SuppressWarnings("unchecked")
	public String formAdditionalInfo(PunchoutRequestModel punchoutModel){
		String result="";
	try {
	String key =  "addressID";			
	String value = CommonUtility.validateString(punchoutModel.getAddressId());
	JSONObject jsonObj = new JSONObject();			
	jsonObj.put(key, value);
	result=jsonObj.toString();
	}catch(Exception e) {
		e.printStackTrace();
	}
	
	return result;
	}
	
	public void getFirstApproverThresholdrange(LinkedHashMap<String, Object> contentObject,HttpSession session) {

		String cartTotalApproverFirst= "N";
		double FirstlevelapprovalthresholdrangeFrom=0.0;
		double FirstlevelapprovalthresholdrangeTo=0.0;
		double cartTotal=0.0;

		try {
			LinkedHashMap<String, String> userCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("userCustomFieldValue");
			
			if(userCustomFieldValue!=null){
				FirstlevelapprovalthresholdrangeFrom=Double.parseDouble(userCustomFieldValue.get("First_level_approval_threshold_range_From"));
				FirstlevelapprovalthresholdrangeTo=Double.parseDouble(userCustomFieldValue.get("First_level_approval_threshold_range_To"));
			}
			if(contentObject!=null && contentObject.get("cartTotal")!=null)
			{
			cartTotal = (Double) contentObject.get("cartTotal");
			}
			if(cartTotal>0 && cartTotal >= FirstlevelapprovalthresholdrangeFrom)
			{	
			contentObject.put("cartTotalApproverFirst", "Y");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getSecondApproverThresholdrange(LinkedHashMap<String, Object> contentObject) {
		String cartTotalSecondApprover= "N";
		double SecondlevelapprovalthresholdrangeFrom=0.0;
		double SecondlevelapprovalthresholdrangeTo=0.0;
		double cartTotal=0.0;
		ArrayList<ProductsModel> productListData=null;
		LinkedHashMap<String, String> userCustomFieldValue = new LinkedHashMap<String, String>();
		try {
			productListData = (ArrayList<ProductsModel>) contentObject.get("productListData");
			cartTotal=productListData.get(0).getCartTotal();
			String approveSenderid=(String) contentObject.get("approveSenderid");
			int userId = CommonUtility.validateNumber(approveSenderid);

			if(userId > 0 ){
				userCustomFieldValue = UsersDAO.getAllUserCustomFieldValue(userId);
				if(userCustomFieldValue!=null && userCustomFieldValue.get("Second_level_approval_threshold_range_From") != null){
					SecondlevelapprovalthresholdrangeFrom= Double.parseDouble(userCustomFieldValue.get("Second_level_approval_threshold_range_From"));
					SecondlevelapprovalthresholdrangeTo=Double.parseDouble(userCustomFieldValue.get("Second_level_approval_threshold_range_To"));	
					if(cartTotal>0 && cartTotal >= SecondlevelapprovalthresholdrangeFrom)
					{
					contentObject.put("cartTotalSecondApproverFirst", "Y");
						
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getChatbotExternalToken(HttpSession session){
		SecureData password = new SecureData();
		String chatToken=null ;
		try {
			RestRequest<Void> request = RestRequest.<Void>builder()
					.withClientId(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_APP_KEY"))
					.withClientSecret(CommonDBQuery.getSystemParamtersList().get("CIMMESB_CLIENT_SECRET"))
					.withUserName(session.getAttribute(Global.USERNAME_KEY).toString())
					.withUserSecret(password.validatePassword(session.getAttribute("securedPassword").toString())).build();
			Cimm2BCResponse<ExternalTokenResponse> response = CimmESBServiceUtils
					.chatbotExternalToken().chatbotExternalToken(request);
			ObjectMapper mapper = new ObjectMapper();
			logger.info("Mapper as String" + mapper.writeValueAsString(response) + "Response: {} " + response);
			chatToken=CommonUtility.validateString(mapper.writeValueAsString(response));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chatToken;
	}
	
	public LinkedHashMap<String, ArrayList<SalesModel>> getTrackOrderInfo(SalesModel salesInputParameter, HashMap<String,String> userDetails, HttpSession session, String orderId, LinkedHashMap<String, Object> contentObject) {
		SalesOrderManagement salesObj = new SalesOrderManagementImpl();
		LinkedHashMap<String, ArrayList<SalesModel>> orderInfo = new LinkedHashMap<String, ArrayList<SalesModel>>();
		ArrayList<SalesModel> orderList=null;
		try {
			salesInputParameter.setUserToken(CommonUtility.validateString(userDetails.get("billingEntityId")));
			salesInputParameter.setOrderStatus("TrackOrder");
			salesInputParameter.setSession(session);
			salesInputParameter.setCustomerNumber(CommonUtility.validateString(userDetails.get("billingEntityId")));
			
			if(CommonUtility.validateString(orderId).contains("-")){
				contentObject.put("IstrackOrderList","N");
				orderInfo = salesObj.OrderDetail(salesInputParameter);
			}else{
				ArrayList<SalesModel> trackOrderList = new ArrayList<SalesModel> ();
				session.setAttribute("localeCode","EN");
				orderList = new ArrayList<SalesModel>();
				orderList = salesObj.OrderHistory(salesInputParameter);
			
				if(orderList !=null && orderList.size() > 0 ) {
					for(SalesModel orderDetailsList : orderList){
						salesInputParameter.setOrderSuffix(orderDetailsList.getOrderSuffix());
						orderInfo = salesObj.OrderDetail(salesInputParameter);
						trackOrderList.addAll(orderInfo.get("OrderDetail"));
					}
					contentObject.put("IstrackOrderList","Y");
					contentObject.put("trackOrderList",trackOrderList);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return orderInfo;
	}
	
	@Override
	public void getTrackOrderRequestParams(SalesModel salesInputParameter, Cimm2BCentralSalesOrderHistoryRequest salesOrderHistoryRequest) {
		if(salesInputParameter.getOrderStatus() == "TrackOrder") {
			salesOrderHistoryRequest.setOrderNumber(CommonUtility.validateString(salesInputParameter.getOrderID()));
		}
	}
	@Override
	public void getTotalFrieghtAddonCharges(Cimm2BCentralOrder order, SalesModel orderDetailModel) {
		double totalAddOns = 0.0;
		
		try {
			if(CommonUtility.validateParseDoubleToString(order.getFreightOut()).length()>0) {
				orderDetailModel.setFreightOut(order.getFreightOut());
				totalAddOns=totalAddOns+order.getFreightOut();
			}
			if(CommonUtility.validateParseDoubleToString(order.getAddOnNumber3()).length()>0) {
				orderDetailModel.setAddOnNumber3(order.getAddOnNumber3());
				totalAddOns=totalAddOns+order.getAddOnNumber3();
			}
			if(CommonUtility.validateParseDoubleToString(order.getAddOnNumber4()).length()>0) {
				orderDetailModel.setAddOnNumber4(order.getAddOnNumber4());
				totalAddOns=totalAddOns+order.getAddOnNumber4();
			}
			if(CommonUtility.validateParseDoubleToString(order.getAddOnNumber6()).length()>0) {
				orderDetailModel.setAddOnNumber6(order.getAddOnNumber6());
				totalAddOns=totalAddOns+order.getAddOnNumber6();
			}
			if(CommonUtility.validateParseDoubleToString(order.getAddOnNumber7()).length()>0) {
				orderDetailModel.setAddOnNumber7(order.getAddOnNumber7());
				totalAddOns=totalAddOns+order.getAddOnNumber7();
			}
			if(CommonUtility.validateParseDoubleToString(order.getAddOnNumber8()).length()>0) {
				orderDetailModel.setAddOnNumber8(order.getAddOnNumber8());
				totalAddOns=totalAddOns+order.getAddOnNumber8();
			}
			if(CommonUtility.validateParseDoubleToString(order.getAddOnNumber11()).length()>0) {
				orderDetailModel.setAddOnNumber11(order.getAddOnNumber11());
				totalAddOns=totalAddOns+order.getAddOnNumber11();
			}
			if(CommonUtility.validateParseDoubleToString(order.getAddOnNumber12()).length()>0) {
				orderDetailModel.setAddOnNumber12(order.getAddOnNumber12());
				totalAddOns=totalAddOns+order.getAddOnNumber12();
			}
			if(CommonUtility.validateParseDoubleToString(order.getAddOnNumber44()).length()>0) {
				orderDetailModel.setAddOnNumber44(order.getAddOnNumber44());
				totalAddOns=totalAddOns+order.getAddOnNumber44();
			}		
			
			if(totalAddOns>0.0) {
				orderDetailModel.setTotalFreightAddOns(totalAddOns);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void getMultipleCarrierTrackingNumber(ArrayList<Cimm2BCentralShipVia> shipMethod, SalesModel orderDetailModel) {
		ArrayList<String> carrierTrackingIds= new ArrayList<String> ();
		
		try {
			for (Cimm2BCentralShipVia orderResponseGetShipMethod : shipMethod) {
				if(CommonUtility.validateString(orderResponseGetShipMethod.getCarrierTrackingNumber()).length()>0) {
					carrierTrackingIds.add(orderResponseGetShipMethod.getCarrierTrackingNumber());
				}
			}
			orderDetailModel.setMultipleTrackingIds(carrierTrackingIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
