package com.unilog.services.impl;

import java.io.File;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.DisplayTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;

import com.erp.service.UserManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralAddress;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.impl.UserManagementImpl;
import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.customfields.CustomFieldUtility;
import com.unilog.customform.SaveCustomFormDetails;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailModel;
import com.unilog.defaults.SendMailUtility;
import com.unilog.ecomm.model.Discount;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.SalesDAO;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.AddressModel;
import com.unilog.users.NewUserRegisterUtility;
import com.unilog.users.UsersAction;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class AdaptPharmaCustomServies implements UnilogFactoryInterface {
	private HttpServletRequest request;
	private String[] eventsLocationFilter;
	public static SaveCustomFormDetails getNotificationDetail = new SaveCustomFormDetails();
	private static UnilogFactoryInterface serviceProvider;
	private AdaptPharmaCustomServies() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (AdaptPharmaCustomServies.class) {
				if(serviceProvider == null) {
					serviceProvider = new AdaptPharmaCustomServies();
				}
			}
		return serviceProvider;
	}    
	public Cimm2BCentralLineItem submitSalesOrderItemList(Cimm2BCentralLineItem cimm2bCentralLineItem) {
		Cimm2BCentralLineItem cimm2bCentralLineItemServiceObj = new Cimm2BCentralLineItem();
		try {
			if(cimm2bCentralLineItem!=null){
				String itemUOM = CommonUtility.validateString(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.itemUOM"));
				int eachCaseQty = CommonUtility.validateNumber(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.eachCaseQty"));
				if(eachCaseQty <= 0) {
					eachCaseQty = 12;
				}
				if(itemUOM.isEmpty()) {
					itemUOM = "EA";
				}
				cimm2bCentralLineItemServiceObj = cimm2bCentralLineItem;
				cimm2bCentralLineItemServiceObj.setShortDescription(cimm2bCentralLineItem.getPage_title()!=null?cimm2bCentralLineItem.getPage_title().replaceAll("\u00AE",""):"");
				cimm2bCentralLineItemServiceObj.setExtendedPrice(cimm2bCentralLineItem.getUnitPrice()*cimm2bCentralLineItem.getQty()*eachCaseQty);
				cimm2bCentralLineItemServiceObj.setUnitPrice(cimm2bCentralLineItem.getUnitPrice());
				//cimm2bCentralLineItemServiceObj.setUnitPrice(cimm2bCentralLineItem.getUnitPrice()*CommonUtility.validateDoubleNumber(cimm2bCentralLineItem.getUomQty()));
				cimm2bCentralLineItemServiceObj.setListPrice(cimm2bCentralLineItem.getListPrice());
				cimm2bCentralLineItemServiceObj.setUom(itemUOM);
				cimm2bCentralLineItemServiceObj.setUomQty(String.valueOf(eachCaseQty));
				cimm2bCentralLineItemServiceObj.setQty(cimm2bCentralLineItem.getQty()*eachCaseQty);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cimm2bCentralLineItemServiceObj;
	}
	public String sendShipAddressFile(UsersModel shipAddressInfo, String result, String fileNameToUpload, String addFirstName){

		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String message="";
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String [] defaultshipData = result.split("~");
		shipAddressInfo.setFormtype("addShippingAddress");
	    File fileList[] = null;
		Enumeration files = null;
		String name = "";
		String[] fileToUpload = null;
		String savedFileName = "";
		String folder = "";
		String saveFile = "";
		String path = "";
	    String [] filesToBeUploaded = fileNameToUpload.split("/");
		ArrayList sendMailFileName = new ArrayList();
		ArrayList sendMailFolderPath = new ArrayList();
		for(String fileSave : filesToBeUploaded){
    	MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper)ServletActionContext.getRequest();
	      fileList = multiWrapper.getFiles(fileSave);
	      if(fileList!= null &&  fileList.length >0){
	       files = multiWrapper.getFileParameterNames();
	       name = (String) files.nextElement();
	       fileToUpload = multiWrapper.getFileNames(name);
	       savedFileName = CommonUtility.validateString(fileToUpload[0]+"_"+userId);
	       folder = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH"));
	       saveFile = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH"))+"/"+savedFileName;
	       path = CommonUtility.validateString(folder);
	       sendMailFileName.add(savedFileName);
	       sendMailFolderPath.add(saveFile);
	       
	      File destination = new File(path);
	      if(!destination.exists()){
	       System.out.println(path+" : destination dir doesnt exists");
	       destination.mkdir();
	      }
	      UsersDAO.copyFile(fileList[0], new File(saveFile), session);
	      System.out.println("File "+savedFileName+" is Uploaded");
	      }
	     }
		shipAddressInfo.setContactIdList(sendMailFolderPath);
		shipAddressInfo.setContactDescriptionList(sendMailFileName);
		if(addFirstName != null && addFirstName.length() > 0){
		 eventsLocationFilter = addFirstName.split("/");
			for (int i = 0; i < eventsLocationFilter.length; i++) {
				String[] fieldNameAndValue = eventsLocationFilter[i].split("-");
				if(fieldNameAndValue!=null && fieldNameAndValue.length > 1){
					String customFiledValue = fieldNameAndValue[1];
					String fieldName =fieldNameAndValue[0];
					if(CommonUtility.validateString(fieldName).length()>0 && fieldName.contains("SHIPTO")){
						UsersDAO.insertCustomField(customFiledValue, fieldName, CommonUtility.validateNumber(sessionUserId), CommonUtility.validateNumber(defaultshipData[1]), CustomFieldUtility.bcAddressBookCustomFieldType);
						}
					}
				}
			}
		 SendMailUtility sendMailUtility = new SendMailUtility();
		boolean sentFlag = false;
		sentFlag = sendMailUtility.sendRegistrationMail(shipAddressInfo,"customer","","addNewShippingAddress"); //"2B"
		if(sentFlag){
			System.out.println("Add new shipping address Email sent successfully");	
		}else{
			System.out.println("Add new shipping address Email sending failed");
		}
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("REGISTRATION_SUCCESS_PAGE")).equalsIgnoreCase("Y")){
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();	
		contentObject.put("Result", "Add new shipping address Email sent successfully");
		message = LayoutGenerator.templateLoader("RegistrationSuccess", contentObject , null, null, null);
		}
		return message;
	}
	
	 public ProductsModel orderItem(ProductsModel itmVal, String pageTitle, String listPrice, String carton){
		 int cartonQty = CommonUtility.validateNumber(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.eachCaseQty")));
         if(cartonQty <= 0) {
        	 cartonQty = 12;
         }
		 itmVal.setPrice(itmVal.getPrice());
		 itmVal.setQty(itmVal.getQty());
		 itmVal.setListPrice((itmVal.getPrice() * itmVal.getQty() * cartonQty));
		 //itmVal.setPrice(CommonUtility.validateDoubleNumber(listPrice));
		 //itmVal.setListPrice(itmVal.getPrice() * CommonUtility.validateDoubleNumber(carton));
		 itmVal.setQtyUOM(String.valueOf(itmVal.getQty() * cartonQty));
		 //itmVal.setQtyUOM(carton);
		 itmVal.setPageTitle(pageTitle);
		 return itmVal;
	 }
	 
	 public LinkedHashMap<String, String> getFirstOrderNotification(){
		 LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("FIRST_ORDER_MAIL");
		 return notificationDetail;
	 }
	 
	 public String addContextObject(SalesModel orderDetail, VelocityContext context){
		 DecimalFormat df3 = new DecimalFormat("#,###.00");
         context.put("unitPrice", df3.format(orderDetail.getOrderList().get(0).getUnitPrice()));
         context.put("qtyUom", NumberFormat.getIntegerInstance().format(CommonUtility.validateNumber(orderDetail.getOrderList().get(0).getQtyUom())));
         context.put("totQty", NumberFormat.getIntegerInstance().format(orderDetail.getOrderList().get(0).getQtyordered()));
         context.put("orderTotal", df3.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTotal()))));
		return "Success" ;
	 }
	 
	 
	 public boolean sendMailRegistration(UsersModel userDetailsAddresss, String formtype, String whomTo,String SuperUserEmail){
			boolean flag = false;
			ResultSet rs = null;
		    Connection  conn = null;
		    HttpServletRequest request =ServletActionContext.getRequest();
		    HttpSession session = request.getSession();

		    String subject = "";
		    String to = "";
		    try{
	        	StringBuilder userDetails = new StringBuilder();
	        	if(userDetailsAddresss!=null)
	            {
	        	userDetails.append(userDetailsAddresss.getFirstName());
	        	if(userDetailsAddresss.getLastName()!=null)
	            	userDetails.append(" "+userDetailsAddresss.getLastName());
	            userDetails.append("<br/>"+userDetailsAddresss.getAddress1()+",<br/>");
	            if(userDetailsAddresss.getAddress2()!=null && userDetailsAddresss.getAddress2().trim().length()>0){
	            	userDetails.append(userDetailsAddresss.getAddress2()+",<br/>");
	            }
	            userDetails.append(userDetailsAddresss.getCity()+",<br/>");
	            userDetails.append(userDetailsAddresss.getState()+" "+userDetailsAddresss.getZipCode()+"<br/>");
	            userDetails.append(userDetailsAddresss.getCountry()+"<br/>");
	            if(userDetailsAddresss.getPhoneNo()!=null)
	            	userDetails.append("PH: "+userDetailsAddresss.getPhoneNo());
	            }
	        	String [] fileName = userDetailsAddresss.getContactDescriptionList().toArray(new String[userDetailsAddresss.getContactDescriptionList().size()]);
	        	String [] filePath = userDetailsAddresss.getContactIdList().toArray(new String[userDetailsAddresss.getContactIdList().size()]);

	        	LinkedHashMap<String, String> notificationDetail = null;
	        	if (formtype != null && formtype.trim().equalsIgnoreCase("1A")){
	        		notificationDetail = getNotificationDetail.getNotificationDetails("ExistingUserRegistrationOne");
	        	}else if (formtype != null && formtype.trim().equalsIgnoreCase("1B")){
	        		notificationDetail = getNotificationDetail.getNotificationDetails("ExistingUserRegistrationTwo");
	        	}else if(formtype != null && formtype.trim().equalsIgnoreCase("2A")){
	        		if(userDetailsAddresss.getUserStatus()!=null && userDetailsAddresss.getUserStatus().equalsIgnoreCase("P")){
	        			notificationDetail = getNotificationDetail.getNotificationDetails("Partial_Registration");
	        		}else{
	        			notificationDetail = getNotificationDetail.getNotificationDetails("NewUserRegistrationOne");
	        		}
	        	}else if(formtype != null && formtype.trim().equalsIgnoreCase("2B")){
	        		notificationDetail = getNotificationDetail.getNotificationDetails("NewUserRegistrationTwo");
	        	}else if(formtype != null && formtype.trim().equalsIgnoreCase("CommertialCustomerCreditApplicationRequest")){
	        		notificationDetail = getNotificationDetail.getNotificationDetails("CommertialCustomerCreditApplicationRequest");
	        	}else if(formtype != null && formtype.trim().equalsIgnoreCase("addNewShippingAddress")){
	        		notificationDetail = getNotificationDetail.getNotificationDetails("addNewShippingAddress");
	        	}
	        	
	        	if(notificationDetail!=null && notificationDetail.size()>0){
	        	
	        	String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
				if(notificationDetail!=null && notificationDetail.get("FROM_EMAIL")!=null){
					fromEmail = notificationDetail.get("FROM_EMAIL");
				}
				if(notificationDetail!=null)
				notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL"));
				
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
		            context.put("address",userDetails);
		            context.put("address1",userDetailsAddresss.getAddress1());
		            context.put("OrgName",userDetailsAddresss.getCompanyName());
		            context.put("OrgType",userDetailsAddresss.getContactClassification());
		            context.put("address2",userDetailsAddresss.getAddress2());
		            context.put("city",userDetailsAddresss.getCity());
		            context.put("state",userDetailsAddresss.getState());
		            context.put("zip",userDetailsAddresss.getZipCode());
		            context.put("phone",userDetailsAddresss.getPhoneNo());
		            context.put("creditApplicationRequest",userDetailsAddresss.getCreditApplicationRequest());
				}
				
				
				if (formtype != null && formtype.trim().equalsIgnoreCase("1A")){
					
					if(whomTo!=null && whomTo.trim().equalsIgnoreCase("customer")){
						to = userDetailsAddresss.getEmailAddress();
						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.customer.1A.subject",CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
					}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("webUser")){
						to = notificationDetail.get("TO_EMAIL");

						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.webUser.1A.subject","");
					}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("superUser")){
						to = SuperUserEmail;

						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.superUser.1A.subject","");
					}
					
				}else if (formtype != null && formtype.trim().equalsIgnoreCase("1B")){
					
					context.put("companyName", userDetailsAddresss.getEntityName());
					context.put("regCompanyName", userDetailsAddresss.getCompanyName());
			        context.put("ReqAuthLevel", userDetailsAddresss.getRequestAuthorizationLevel());
		            context.put("SalesContact", userDetailsAddresss.getSalesContact());
		            context.put("accountNumber", userDetailsAddresss.getAccountName());
		            
					if(whomTo!=null && whomTo.trim().equalsIgnoreCase("customer")){
						to = userDetailsAddresss.getEmailAddress();
						
						if(CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION")!=null && CommonDBQuery.getSystemParamtersList().get("SHOW_BRANCHLIST_IN_REGISTRATION").trim().equalsIgnoreCase("Y")){
							String warehouseEmail = UsersDAO.getWareHouseDetail(userDetailsAddresss.getWareHouseCode());
							if(warehouseEmail!=null && !warehouseEmail.trim().equalsIgnoreCase("")){
								to = to + ";" + warehouseEmail;
							}
						}

						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.customer.1B.subject","");
					}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("webUser")){
						to = notificationDetail.get("TO_EMAIL");
						if(to==null){
							to = notificationDetail.get("BCC_EMAIL");
						}

						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.webUser.1B.subject","");
					}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("superUser")){
						to = SuperUserEmail;

						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.superUser.1B.subject","");
					}
					
				}else if(formtype != null && formtype.trim().equalsIgnoreCase("2A")){
					
					context.put("companyName", userDetailsAddresss.getEntityName());
					
					if(whomTo!=null && whomTo.trim().equalsIgnoreCase("customer")){
						to = userDetailsAddresss.getEmailAddress();

						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.customer.2A.subject","");
					}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("webUser")){
						to = notificationDetail.get("TO_EMAIL");
						if(to==null){
							to = notificationDetail.get("BCC_EMAIL");
						}

						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.webUser.2A.subject","");
					}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("superUser")){
						to = SuperUserEmail;

						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.superUser.2A.subject","");
					}
					
				}else if(formtype != null && formtype.trim().equalsIgnoreCase("2B")){
					
					context.put("companyName", userDetailsAddresss.getEntityName());
					
					if(whomTo!=null && whomTo.trim().equalsIgnoreCase("customer")){
						to = userDetailsAddresss.getEmailAddress();

						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.customer.2B.subject","");
					}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("webUser")){
						to = userDetailsAddresss.getEmailAddress();
						if(CommonUtility.validateString(to).length()>0 && CommonUtility.validateString(notificationDetail.get("TO_EMAIL")).length()>0){
							to = to+";"+notificationDetail.get("TO_EMAIL");
						}
						//to = (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";"+userDetailsAddresss.getEmailAddress():notificationDetail.get("BCC_EMAIL"));
						//to = notificationDetail.get("TO_EMAIL");
						if(to==null){
							to = notificationDetail.get("BCC_EMAIL");
						}
						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.webUser.2B.subject","");
					}else if(whomTo!=null && whomTo.trim().equalsIgnoreCase("superUser")){
						to = SuperUserEmail;

						subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.superUser.2B.subject","");
					}
					
				}else if(formtype != null && formtype.trim().equalsIgnoreCase("CommertialCustomerCreditApplicationRequest")){
					if(CommonUtility.validateString(notificationDetail.get("TO_EMAIL")).length()>0){
						to = notificationDetail.get("TO_EMAIL");
					}
					if(to==null){
						to = notificationDetail.get("BCC_EMAIL");
					}
					subject = SendMailUtility.propertyLoader("sentmailconfig.sendRegistrationMail.customer.credit.application.request","");
	        	}else if(formtype != null && formtype.trim().equalsIgnoreCase("addNewShippingAddress")){
	        		to = userDetailsAddresss.getEmailAddress();
	        		if(!CommonUtility.validateString(to).isEmpty()){
						if(!CommonUtility.validateString(notificationDetail.get("TO_EMAIL")).isEmpty()) {
							to = to + ";" + notificationDetail.get("TO_EMAIL");
						} else {
							notificationDetail.put("TO_EMAIL", to);
						}
					}else {
						to = notificationDetail.get("BCC_EMAIL");
					}
					subject = SendMailUtility.propertyLoader("sentmailconfig.addNewShippingAddress","");
	        	}
				
				context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
	            context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
	            context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
	            context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
	            if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
	            	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
	            }
	            StringWriter writer = new StringWriter();
	            if(notificationDetail!=null && notificationDetail.get("DESCRIPTION")!=null)
	            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
	            
	            StringBuilder finalMessage= new StringBuilder();
	            finalMessage.append(writer.toString());
	            if(notificationDetail!=null)
	            {
	            	if(CommonUtility.validateString(to).contains(CommonUtility.validateString(notificationDetail.get("TO_EMAIL")))){
	            		notificationDetail.put("TO_EMAIL",to);
	            	}else{
	            		notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";":"")+to);
	            	}
	            

	            if(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("purchaseagent.notificaion.email")).length()>0){
	            	flag = SendMailUtility.sendDocumentNotification(notificationDetail, subject, fromEmail, finalMessage.toString(),fileName,filePath );
	            }
	           }
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
	
	 public String getPoSequenceId(String seqName){
			Statement stmt = null;
			ResultSet rs = null;
			Connection conn = null;
			String sequencePId = null;
			String strQuery = null;
			try
			{
			conn =  ConnectionManager.getDBConnection();
			stmt = conn.createStatement();
			if(seqName!=null && seqName.contains("BILL")){
			strQuery=PropertyAction.SqlContainer.get("getNextSevenDigitBillToId");	
			}else if(seqName!=null && seqName.contains("SHIP")){
			strQuery=PropertyAction.SqlContainer.get("getNextSevenDigitShipToId");
			}else{
			strQuery=PropertyAction.SqlContainer.get("getNextSevenDigitSequenceId");
			}
				strQuery=strQuery.replace("^SEQUENCENAME^", seqName);				
				rs = stmt.executeQuery(strQuery);
				if (rs.next())	{			
				 sequencePId=rs.getString("SEQVAL");
			}
			} 
			catch (Exception e){
				e.printStackTrace();
			} 
			finally{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBStatement(stmt);
				ConnectionManager.closeDBConnection(conn);
			}	
		 return sequencePId;
		 
	 }
	public SalesOrderManagementModel salesOrderdetail(SalesOrderManagementModel salesOrderInput, String shipTofirstName, String shipToLastName, String pageTitle, String sequencePoNum, UsersModel userShipAddress){
		
		AddressModel selectedShipAddress = salesOrderInput.getShipAddress();
		selectedShipAddress.setFirstName(shipTofirstName);
		selectedShipAddress.setLastName(shipToLastName);
		selectedShipAddress.setAddressBookId(userShipAddress.getAddressBookId());
		selectedShipAddress.setAddressType(userShipAddress.getAddressType());
		selectedShipAddress.setShipToName(userShipAddress.getShipToName());
		salesOrderInput.setComments(pageTitle);
		salesOrderInput.setPurchaseOrderNumber(sequencePoNum);
		salesOrderInput.setCustomerReleaseNumber(userShipAddress.getCustomerName());
		salesOrderInput.setShipAddress(selectedShipAddress);
		
		return salesOrderInput;

	}
	
	public UsersModel billShipAddressInsert(UsersModel billShipInfo, String type){
		UsersModel addressInfo = new UsersModel();
		if(type!=null && type.equalsIgnoreCase("bill")){
			String billToId = getPoSequenceId("BILL_TO_SEQ");
			addressInfo.setAddress1(billShipInfo.getBillAddress().getAddress1()!=null?billShipInfo.getBillAddress().getAddress1():(billShipInfo.getAddress1()!=null?billShipInfo.getAddress1():"No Address")); 
			addressInfo.setAddress2(billShipInfo.getBillAddress().getAddress2()!=null?billShipInfo.getBillAddress().getAddress2():billShipInfo.getAddress2()); 
			addressInfo.setCity(billShipInfo.getBillAddress().getCity()!=null?billShipInfo.getBillAddress().getCity():billShipInfo.getCity());
			addressInfo.setState( billShipInfo.getBillAddress().getState()!=null?billShipInfo.getBillAddress().getState():billShipInfo.getState());
			addressInfo.setZipCode(billShipInfo.getBillAddress().getZipCode()!=null?billShipInfo.getBillAddress().getZipCode():billShipInfo.getZipCode());
			addressInfo.setCountry(billShipInfo.getBillAddress().getCountry()!=null?billShipInfo.getBillAddress().getCountry():billShipInfo.getCountry());
			addressInfo.setPhoneNo(billShipInfo.getBillAddress().getPhoneNo()!=null?billShipInfo.getBillAddress().getPhoneNo():billShipInfo.getPhoneNo());
			addressInfo.setEntityId(billShipInfo.getBillAddress().getEntityId()!=null?billShipInfo.getBillAddress().getEntityId():billShipInfo.getEntityId()); 
			addressInfo.setFirstName(billShipInfo.getBillAddress().getFirstName()!=null?billShipInfo.getBillAddress().getFirstName():billShipInfo.getFirstName());
			addressInfo.setLastName(billShipInfo.getBillAddress().getLastName()!=null?billShipInfo.getBillAddress().getLastName():billShipInfo.getLastName()); 
			addressInfo.setEmailAddress(billShipInfo.getBillAddress().getEmailAddress()!=null?billShipInfo.getBillAddress().getEmailAddress():billShipInfo.getEmailAddress()); 
			addressInfo.setCompanyName(billShipInfo.getBillAddress().getCompanyName()!=null?billShipInfo.getBillAddress().getCompanyName():billShipInfo.getCompanyName());
			addressInfo.setBuyingCompanyId(billShipInfo.getBuyingCompanyId());
			addressInfo.setShipToName(billShipInfo.getCustomerName());
			addressInfo.setShipToId(billToId);
		}else{
			String shipToId = getPoSequenceId("SHIP_TO_SEQ");
			addressInfo.setAddress1(billShipInfo.getShipAddress().getAddress1()!=null?billShipInfo.getShipAddress().getAddress1():(billShipInfo.getAddress1()!=null?billShipInfo.getAddress1():"No Address")); 
			addressInfo.setAddress2(billShipInfo.getShipAddress().getAddress2()!=null?billShipInfo.getShipAddress().getAddress2():billShipInfo.getAddress2()); 
			addressInfo.setCity(billShipInfo.getShipAddress().getCity()!=null?billShipInfo.getShipAddress().getCity():billShipInfo.getCity());
			addressInfo.setState( billShipInfo.getShipAddress().getState()!=null?billShipInfo.getShipAddress().getState():billShipInfo.getState());
			addressInfo.setZipCode(billShipInfo.getShipAddress().getZipCode()!=null?billShipInfo.getShipAddress().getZipCode():billShipInfo.getZipCode());
			addressInfo.setCountry(billShipInfo.getShipAddress().getCountry()!=null?billShipInfo.getShipAddress().getCountry():billShipInfo.getCountry());
			addressInfo.setPhoneNo(billShipInfo.getShipAddress().getPhoneNo()!=null?billShipInfo.getShipAddress().getPhoneNo():billShipInfo.getPhoneNo());
			addressInfo.setEntityId(billShipInfo.getShipAddress().getEntityId()!=null?billShipInfo.getShipAddress().getEntityId():billShipInfo.getEntityId()); 
			addressInfo.setFirstName(billShipInfo.getShipAddress().getFirstName()!=null?billShipInfo.getShipAddress().getFirstName():billShipInfo.getFirstName());
			addressInfo.setLastName(billShipInfo.getShipAddress().getLastName()!=null?billShipInfo.getShipAddress().getLastName():billShipInfo.getLastName()); 
			addressInfo.setEmailAddress(billShipInfo.getShipAddress().getEmailAddress()!=null?billShipInfo.getShipAddress().getEmailAddress():billShipInfo.getEmailAddress()); 
			addressInfo.setCompanyName(billShipInfo.getShipAddress().getCompanyName()!=null?billShipInfo.getShipAddress().getCompanyName():billShipInfo.getCompanyName());
			addressInfo.setBuyingCompanyId(billShipInfo.getBuyingCompanyId());
			addressInfo.setShipToName(billShipInfo.getCustomerName());
			addressInfo.setShipToId(shipToId);
		}
		return addressInfo;
	}
	
	
	public UsersModel contactInfo(AddressModel addressModel, UsersModel billingAddress, UsersModel contactInformation, UsersModel shippingAddress) {
		 UsersModel address = new UsersModel();
		 billingAddress.setFirstName(addressModel.getBillingAddress().getFirstName()!=null?addressModel.getBillingAddress().getFirstName():addressModel.getFirstName());
		 billingAddress.setLastName(addressModel.getBillingAddress().getLastName()!=null?addressModel.getBillingAddress().getLastName():addressModel.getLastName());
		 billingAddress.setAddress1(addressModel.getBillingAddress().getAddress1()!=null?addressModel.getBillingAddress().getAddress1():addressModel.getAddress1());
		 billingAddress.setAddress2(addressModel.getBillingAddress().getAddress2()!=null?addressModel.getBillingAddress().getAddress2():addressModel.getAddress2());
		 billingAddress.setCity(addressModel.getBillingAddress().getCity()!=null?addressModel.getBillingAddress().getCity():addressModel.getCity());
		 billingAddress.setState(addressModel.getBillingAddress().getState()!=null?addressModel.getBillingAddress().getState():addressModel.getState());
		 billingAddress.setCountry(addressModel.getBillingAddress().getCountry()!=null?addressModel.getBillingAddress().getCountry():addressModel.getCountry());
		 billingAddress.setPhoneNo(addressModel.getBillingAddress().getPhoneNo()!=null?addressModel.getBillingAddress().getPhoneNo():addressModel.getPhoneNo());
		 billingAddress.setEmailAddress(addressModel.getBillingAddress().getEmailAddress()!=null?addressModel.getBillingAddress().getEmailAddress():addressModel.getEmailAddress());
		 billingAddress.setShipToName(addressModel.getBillingAddress().getCompanyName());
		 billingAddress.setZipCode(addressModel.getBillingAddress().getZipCode()!=null?addressModel.getBillingAddress().getZipCode():addressModel.getZipCode());
		 contactInformation.setAddress1(addressModel.getAddress1()!=null?addressModel.getAddress1():addressModel.getBillingAddress().getAddress1());
	     contactInformation.setCity(addressModel.getBillingAddress().getCity()!=null?addressModel.getBillingAddress().getCity():addressModel.getCity());
	     contactInformation.setState(addressModel.getBillingAddress().getState()!=null?addressModel.getBillingAddress().getState():addressModel.getState());
	     contactInformation.setCountry(addressModel.getBillingAddress().getCountry()!=null?addressModel.getBillingAddress().getCountry():addressModel.getCountry());
	     contactInformation.setZipCode(addressModel.getBillingAddress().getZipCode()!=null?addressModel.getBillingAddress().getZipCode():addressModel.getZipCode());
		 
		 
		return address;
	}
	
	public Cimm2BCentralAddress addcustomFieldData(String userName, AddressModel shipAddress, AddressModel billAddress) {
		
		HashMap<String,String> userDetails = new HashMap<String, String>();
		userDetails=UsersDAO.getUserPasswordAndUserId(userName,"Y");
		HashMap<String,String>	customerCustomFieldData = UsersDAO.getAllCustomerCustomFieldValue(CommonUtility.validateNumber(userDetails.get("buyingCompanyId")));
		HashMap<String,String>	shippingCustomFieldData = UsersDAO.getBCAddressBookCustomFields(shipAddress.getAddressBookId());
		Cimm2BCentralAddress customFieldData = new Cimm2BCentralAddress();
		if(customerCustomFieldData.get("BILLTO_ORGANIZATION_NAME")!=null && customerCustomFieldData.get("BILLTO_ORGANIZATION_NAME").length()>0){
			customFieldData.setCompanyName(customerCustomFieldData.get("BILLTO_ORGANIZATION_NAME"));
		}else{
			customFieldData.setCompanyName(billAddress.getFirstName()!=null?billAddress.getFirstName()+" "+billAddress.getLastName():"");
		}
		if(shippingCustomFieldData.get("SHIPTO_ORGANIZATION_NAME")!=null && shippingCustomFieldData.get("SHIPTO_ORGANIZATION_NAME").length()>0){
			customFieldData.setCustomerName(shippingCustomFieldData.get("SHIPTO_ORGANIZATION_NAME"));
		}else{
			customFieldData.setCustomerName(shipAddress.getFirstName()!=null?shipAddress.getFirstName()+" "+shipAddress.getLastName():"");
		}
		
		return customFieldData;
	}
	
	public ArrayList<UsersModel> addShipAddressList(ArrayList<UsersModel> shipAddressList) {
		ArrayList<UsersModel> shipAddress = new ArrayList<UsersModel>();
		for(UsersModel usersModelObject : shipAddressList){
			HashMap<String, String> userAddressId = UsersDAO.getBCAddressBookCustomFields(usersModelObject.getAddressBookId());
			if(userAddressId.get("APPROVAL_STATUS")!=null && !userAddressId.get("APPROVAL_STATUS").equalsIgnoreCase("N")){
				usersModelObject.setApproverAgentAssignStatus(userAddressId.get("APPROVAL_STATUS"));
				usersModelObject.setCompanyName(userAddressId.get("SHIPTO_ORGANIZATION_NAME"));
				usersModelObject.setShipToName(userAddressId.get("SHIPTO_ATTENTION"));
				usersModelObject.setShippingOrgType(userAddressId.get("SHIPTO_ORG_TYPE"));
				shipAddress.add(usersModelObject);
			}
		}
		Collections.sort(shipAddress, (UsersModel u1, UsersModel u2) -> {
			String firstAddress = CommonUtility.validateString(u1.getCompanyName())
								+ CommonUtility.validateString(u1.getFirstName())
								+ CommonUtility.validateString(u1.getLastName());
			String secondAddress = CommonUtility.validateString(u2.getCompanyName())
								+ CommonUtility.validateString(u2.getFirstName())
								+ CommonUtility.validateString(u2.getLastName());
			return firstAddress.toLowerCase().compareTo(secondAddress.toLowerCase());
		});
		return shipAddress;
	}
	
	 public String fetchItemId(int subset){
		 
		 	Connection  conn = null;
		    PreparedStatement pstmt=null;
		    ResultSet rs = null;
		    String sql = "";
		    String itemId="";
		
			try {
	            conn = ConnectionManager.getDBConnection();
	            sql = "SELECT * FROM ITEM_PRICES WHERE SUBSET_ID=?";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, subset);
	            rs = pstmt.executeQuery();
	           if(rs.next())
	           {
	        	   itemId= rs.getString("ITEM_PRICE_ID");
	           }
	    } catch (SQLException e) { 
	        e.printStackTrace();
	    }
	    finally {	    
	    		ConnectionManager.closeDBResultSet(rs);
	    		ConnectionManager.closeDBPreparedStatement(pstmt);
	    		ConnectionManager.closeDBConnection(conn);	
	      }
		 
		return itemId;
	 }
	 
	 public SalesModel orderdetail(ArrayList<Cimm2BCentralLineItem> ordersList){
		 SalesModel orderResponse = new SalesModel();
		 for(Cimm2BCentralLineItem orders : ordersList ){
			 orderResponse.setQtyordered(orders.getQty());
			 orderResponse.setCustomerPrice(orders.getUnitPrice());
			 orderResponse.setQtyShipped(CommonUtility.validateNumber(orders.getUomQty()));
		 }	
		 return orderResponse;
	 }

	public int getFirstOrders(SalesModel salesInputParameter)	{

	ResultSet rs = null;
	Connection  conn = null;
	PreparedStatement pstmt=null;
	ArrayList<SalesModel> orderStatus = null;
	int userId = 0;
	String orderNumber = "";
	String customerPoNumber = "";
	String startDate = "";
	String endDate = "";
	int count=0;
	String shipToId = "";
	DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

	try {
		conn = ConnectionManager.getDBConnection();
		if(salesInputParameter!=null){
			userId = salesInputParameter.getUserId();
			orderNumber = salesInputParameter.getOrderNum();
			customerPoNumber = salesInputParameter.getPoNumber();
			startDate = salesInputParameter.getStartDate();
			endDate = salesInputParameter.getEndDate();
			shipToId = salesInputParameter.getShipToId();
		}

		pstmt=conn.prepareStatement(PropertyAction.SqlContainer.get("firstOrderMail"));
		pstmt.setInt(1, userId);
		pstmt.setString(2,shipToId);
		rs=pstmt.executeQuery();
		orderStatus = new ArrayList<SalesModel>();
		while(rs.next())
		{
			count=rs.getInt("ORDERCOUNT");
		}

	} catch (Exception e) {         
		e.printStackTrace();

	} finally {
		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);
		ConnectionManager.closeDBConnection(conn);	
	} 
	return count;

}
	
	public boolean sendFirstOrderMail(SalesModel erpOrderDetail, SendMailModel sendMailModel, int firstOrder) {
		SendMailUtility sendMailUtility = new SendMailUtility();
		boolean mailsent = false;
		if (firstOrder >= 1) {
			sendMailModel.setFirstOrderEmail(false);
		} else {
			sendMailModel.setMailSubject("First Order e-Mail");
			sendMailModel.setFirstOrderEmail(true);
			mailsent = sendMailUtility.sendOrderMail(erpOrderDetail, sendMailModel);
			sendMailModel.setFirstOrderEmail(false);
		}

		return mailsent;
	}
	
	public String setDefaultAddress(String shipToName, String addressType, UsersModel userDefaultAddress ){
		userDefaultAddress.setShipToName(shipToName);
		userDefaultAddress.setAddressType(addressType);
		return shipToName;
	}
	
	public String partialUserLogin(UsersModel enteredUserNameInfo){
		String	renderContent="";
    	if(enteredUserNameInfo.getStatusDescription().equalsIgnoreCase("I")){
    		renderContent = "approval";
		}
		return renderContent;
	}
	
	public String getExpectedDeliveryDate(String date ){
		
		String expectedDelivery="";
		Calendar c = Calendar.getInstance();
		 try {
		Date  date1=new SimpleDateFormat("MM/dd/yyyy").parse(date);
		 int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		 int time=c.get(Calendar.HOUR_OF_DAY);
			if(time<11 && (dayOfWeek==2||dayOfWeek==3||dayOfWeek==4)){
				c.add(Calendar.DATE, 2);	
			}else if(time>11 && (dayOfWeek==2||dayOfWeek==3)){
				c.add(Calendar.DATE, 3);
			}else if((time>11 && (dayOfWeek==4))||dayOfWeek==5||dayOfWeek==6){
				if(dayOfWeek==4){
					c.add(Calendar.DATE, 7);
			
				}else if(dayOfWeek==5){
					c.add(Calendar.DATE, 6);
				}else{
					c.add(Calendar.DATE, 5);
				}
			}else if(dayOfWeek==7||dayOfWeek==1){
				if(dayOfWeek==4){
					c.add(Calendar.DATE, 4);
				}else{
					c.add(Calendar.DATE, 3);
				}
			}
			Date currentDatePlusOne = c.getTime();
			expectedDelivery =new SimpleDateFormat("MM-dd-yyyy").format(currentDatePlusOne);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return expectedDelivery;
	}
	
	public LinkedHashMap<String, Object> getAllShippingAddress(LinkedHashMap<String, Object> contentObject, int buyingCompanyId) {
		Map<String, ArrayList<UsersModel>> allShipAddressList = UsersDAO.getAllAddressListFromBCAddressBook(buyingCompanyId);
		if(allShipAddressList != null) {
			contentObject.put("shipEntity", addShipAddressList(allShipAddressList.get("Ship")));
		}
		return contentObject;
	}
	
	public boolean sendFirstMultiOrderMail(ArrayList<SalesModel> erpOrderDetailList, SendMailModel sendMailModel) {
		boolean mailsent = false;
		boolean firstOrderMail = false;
		for (SalesModel firstOrder : erpOrderDetailList) {
			if (firstOrder.getFirstOrder() <= 1) {
				firstOrderMail = true;
			}
		}
		if(firstOrderMail) {
			sendMailModel.setMailSubject("First Order e-Mail");
			sendMailModel.setFirstOrderEmail(true);
			mailsent = sendMultiOrderMail(erpOrderDetailList, sendMailModel, false);
			sendMailModel.setFirstOrderEmail(false);
		}
		return mailsent;
	}
	
	public boolean sendMultiOrderMail(ArrayList<SalesModel> erpOrderDetailList, SendMailModel sendMailModel, boolean massOrderEmail) {
		HttpServletRequest request =ServletActionContext.getRequest();
	    HttpSession session = request.getSession();
	    boolean flag = false;
	    String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
	    String erpDisplayName = null;
	    DecimalFormat df2 = CommonUtility.getPricePrecision(session);
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy"); //  HH:mm
		Date date = new Date();
		int orderListCount = 0;
		
	    try {
        	String orderMailAddressCustomerWise = UsersDAO.getCustomerCustomTextFieldValue(CommonUtility.validateNumber(buyingCompanyId),"ORDER_SUBMIT_EMAIL_ID");
        	sendMailModel.setUserId(CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY)));
        	int orderListSize = erpOrderDetailList.size();
        	
            for(SalesModel orderDetail : erpOrderDetailList) {
            	LinkedHashMap<String, String> notificationDetail = getNotificationDetail.getNotificationDetails("OrderMail");
            	VelocityContext context = new VelocityContext();
                context.put("orderDetailObject", orderDetail);
            	
            	if (session != null) {
    				if (session.getAttribute("loginCustomerName") != null) {
    					context.put("loginCustomerName", CommonUtility.validateString((String) session.getAttribute("loginCustomerName")));
    				}

    				String csrAdminName = "";
    				Map<String, String> salesUserDetails = (Map<String, String>) session.getAttribute("salesUserDetails");
    				if (session.getAttribute("isSalesUser") != null && CommonUtility.validateString((String) session.getAttribute("isSalesUser")).equalsIgnoreCase("Y")) {
    					context.put("isSalesUser",CommonUtility.validateString((String) session.getAttribute("isSalesUser")));
    				}
    				if (session.getAttribute("isSalesAdmin") != null && CommonUtility.validateString((String) session.getAttribute("isSalesAdmin")).equalsIgnoreCase("Y")) {
    					context.put("isSalesAdmin", CommonUtility.validateString((String) session.getAttribute("isSalesAdmin")));
    					context.put("csrRole", "Administrator");
    				} else {
    					context.put("csrRole", "Sales Rep");
    				}
    				if (salesUserDetails != null) {
    					if (CommonUtility.validateString(salesUserDetails.get("firstName")).length() > 0) {
    						csrAdminName = CommonUtility.validateString(salesUserDetails.get("firstName")) + " ";
    					}
    					if (CommonUtility.validateString(salesUserDetails.get("lastName")).length() > 0) {
    						csrAdminName = csrAdminName + CommonUtility.validateString(salesUserDetails.get("lastName"));
    					}
    					context.put("csrAdminName", CommonUtility.validateString(csrAdminName));
    				}
    			}
                
                String toEmailId = "";
                
                if(massOrderEmail && !CommonUtility.validateString(sendMailModel.getToEmailId()).isEmpty()) {
                	toEmailId = sendMailModel.getToEmailId();
                    context.put("erpOrderDetailList", erpOrderDetailList);
                }
                
                if(!massOrderEmail && orderDetail.getShipAddress() != null && !CommonUtility.validateString(orderDetail.getShipAddress().getEmailAddress()).isEmpty()) {
                   	toEmailId = orderDetail.getShipAddress().getEmailAddress();
                }
                
    			if(sendMailModel.isFirstOrderEmail()) {
    				notificationDetail = getFirstOrderNotification();
    				if(notificationDetail!=null){
    					toEmailId = "";
    				}
    			}
                String lastName = "";
                if(sendMailModel.getLastName()!=null && sendMailModel.getLastName().trim().length() > 0){
                	lastName = sendMailModel.getLastName().trim();
                }
                
                StringBuilder shippAddress = new StringBuilder();
                StringBuilder billAddress = new StringBuilder();
                String errDesc = orderDetail.getOrderStatusDesc();
                
                if(errDesc==null){errDesc = "Order Failed.";}
                	if(orderDetail.getBillAddress().getCompanyName()!=null && orderDetail.getBillAddress().getCompanyName().trim().length() > 0) {
                		billAddress.append(orderDetail.getBillAddress().getCompanyName() +"<br />");
                	}
                	billAddress.append(orderDetail.getBillAddress().getAddress1()  +"<br />");
                    if(orderDetail.getBillAddress().getAddress2()!=null){
                    	billAddress.append(orderDetail.getBillAddress().getAddress2() +"<br />");
                    }
                    billAddress.append("<br/>"+orderDetail.getBillAddress().getCity()+", "+orderDetail.getBillAddress().getState()+" "+orderDetail.getBillAddress().getZipCode()+"<br />");
                    billAddress.append(orderDetail.getBillAddress().getCountry()+"<br />");
                    if(orderDetail.getBillAddress().getPhoneNo()!=null){
                    	billAddress.append("Ph: " + orderDetail.getBillAddress().getPhoneNo());
                    }
                    if(orderDetail.getBillAddress().getCompanyName()!=null && orderDetail.getBillAddress().getCompanyName().trim().length() > 0) {
                    	shippAddress.append(orderDetail.getBillAddress().getCompanyName() +"<br />");
                    }
                    if(orderDetail.getShipAddress().getShipToId()!=null && orderDetail.getShipAddress().getShipToId().trim().length() > 0){
                    	shippAddress.append(orderDetail.getShipAddress().getShipToId() + "-");
                    }
                    if(orderDetail.getShipAddress().getShipToName()!=null && orderDetail.getShipAddress().getShipToName().trim().length() > 0){
                    	shippAddress.append(orderDetail.getShipAddress().getShipToName() + "<br />");
                    }else{
                    	shippAddress.append("<br />");
                    }
                    shippAddress.append(orderDetail.getShipAddress().getAddress1()  +"<br />");
                    if(orderDetail.getShipAddress().getAddress2()!=null && orderDetail.getShipAddress().getAddress2().trim().length() > 0){
                    	shippAddress.append(orderDetail.getShipAddress().getAddress2() +"<br />");
                    }
                    shippAddress.append(orderDetail.getShipAddress().getCity()+", "+orderDetail.getShipAddress().getState()+", "+orderDetail.getBillAddress().getCountry()+" "+orderDetail.getShipAddress().getZipCode()+"<br />");
                    if(orderDetail.getShipAddress().getPhoneNo()!=null){
                    	shippAddress.append("Ph: " + orderDetail.getShipAddress().getPhoneNo());
                    }
                
                if(CommonUtility.validateString(sendMailModel.getMailSubject()).toUpperCase().contains("QUOTE")){
                	context.put("orderType", "QUOTE");
                }
                context.put("subject", sendMailModel.getMailSubject());
                context.put("salesOrderNumber", orderDetail.getOrderNum());
                context.put("externalSystemId", CommonUtility.validateString(orderDetail.getOrderNum()));
                context.put("erpOrderNumber", CommonUtility.validateString(orderDetail.getErpOrderNumber()));
                if(orderDetail.getPaymentMethod()!=null && orderDetail.getPaymentMethod().trim().length()>0){
                	 context.put("paymentMethod",orderDetail.getPaymentMethod().trim());
                	 if(orderDetail.getPaymentMethod().trim().equalsIgnoreCase("Credit Card")){
                		 if(orderDetail.getTransactionId()!=null && orderDetail.getTransactionId().trim().length()>0){
                			 context.put("transactionId",orderDetail.getTransactionId());
                		 }else{
                			 context.put("transactionId","N/A");
                		 }
                	 }
                }
                UsersModel salesRepIn = (UsersModel) session.getAttribute("salesRepIn");
    			if(salesRepIn!=null){
    				context.put("insideSalesRepEmail", salesRepIn.getEmailAddress());
    			}
    			UsersModel salesRepOut = (UsersModel)session.getAttribute("salesRepOut");
    			if(salesRepOut!=null){
    				context.put("outsideSalesRepEmail", salesRepOut.getEmailAddress());
    			}
    			if(session.getAttribute("selectedshipToIdSx")!=null){
    	            context.put("selectedshipToIdSx",session.getAttribute("selectedshipToIdSx").toString());
    	        }
    			if(CommonUtility.validateString(sendMailModel.getAdditionalComments()).length()>0) {
    				context.put("AdditionalComments", sendMailModel.getAdditionalComments());
    			}
    			if(CommonUtility.validateString(sendMailModel.getAdditionalName()).length()>0) {
    				context.put("additionalPickupName", sendMailModel.getAdditionalName());		
    						}
    			if(CommonUtility.validateString(sendMailModel.getAdditionalPickupPerson()).length()>0) {
    				context.put("additionalShipToStoreName", sendMailModel.getAdditionalPickupPerson());
    			}
    			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SAVE_LEADTIME_ON_ORDER_CONFIRMATION")).equalsIgnoreCase("Y")){
    				for(SalesModel eachItem:orderDetail.getOrderList()){
    					if(!CommonUtility.validateString(eachItem.getLeadTime()).equalsIgnoreCase("-1")){
    						DateFormat df = new SimpleDateFormat("EEE MM/dd/yyyy");
    						int leadTime = CommonUtility.validateNumber(eachItem.getLeadTime());
    						Calendar c = Calendar.getInstance();
    						if(leadTime>0){
    							c.add(Calendar.DATE, leadTime);	
    						}
    						int hourofDay = c.get(Calendar.HOUR_OF_DAY);
    						if(hourofDay>15){
    							c.add(Calendar.DATE, 1);
    						}
    						int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    						if(dayOfWeek==Calendar.FRIDAY){
    							c.add(Calendar.DATE, 3);
    						}else if(dayOfWeek==Calendar.SATURDAY){
    							c.add(Calendar.DATE,2);
    						}else if(dayOfWeek==Calendar.SUNDAY){
    							c.add(Calendar.DATE,1);
    						}
    						eachItem.setLeadTime(df.format(c.getTime()));
    					}
    				}
    			}
    			
    			LinkedHashMap<String,ArrayList<SalesModel> > orderItemGroupList = null;
    			//CustomServiceProvider
    			orderItemGroupList = getGroupedItemsInSalesData(orderDetail.getOrderList());
    			context.put("orderItemGroupList", orderItemGroupList);
    			//CustomServiceProvider
    				
    			context.put("orderType", "Order");
    			context.put("customFieldValueObject", orderDetail.getCustomFieldVal());
    			context.put("billingAddressObject", orderDetail.getBillAddress());
                context.put("shippingAddressObject", orderDetail.getShipAddress());
                context.put("erpDisplayName", erpDisplayName);
                context.put("orderedBy", orderDetail.getOrderedBy());
                context.put("poNumber", orderDetail.getPoNumber());
                context.put("reqDate", orderDetail.getReqDate());
                context.put("shipMethod", orderDetail.getShipMethod());
                context.put("shipViaDisplayName", orderDetail.getShipViaMethod());
                context.put("shippingInstruction", orderDetail.getShippingInstruction());
                context.put("orderNotes", orderDetail.getOrderNotes());
                context.put("orderStatus", "Bid");
                context.put("shipViaDesc",  orderDetail.getShipViaDescription());
                context.put("billAddress", billAddress.toString());
                context.put("shipAddress", shippAddress.toString());
                context.put("lineItemList", orderDetail.getOrderList());
                context.put("orderTotal", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTotal()))));
                context.put("tax", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTax()))));
                context.put("subTotal", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getSubtotal()))));
                context.put("freight", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getFreight()))));
                context.put("handling", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getHandling()))));
                context.put("deliveryFee", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getDeliveryCharge()))));
                context.put("discount", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getDiscount()))));
                context.put("orderItemsDiscount", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getDiscount()))));
                context.put("totalSavings", df2.format(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(orderDetail.getTotalSavingsOnOrder()))));
                context.put("appliedDiscountCoupons", orderDetail.getDiscountCouponCode());
                context.put("date",dateFormat.format(date));
                context.put("customerReleaseNumber", orderDetail.getCustomerReleaseNumber());
                context.put("homeBranchName", orderDetail.getHomeBranchName());
                context.put("companyName", orderDetail.getBillAddress().getCompanyName()); 
                NumberTool numbrTool =  new NumberTool();
                context.put("numberTool", numbrTool); 
                context.put("escapeTool", new EscapeTool());
    	    	context.put("math", new MathTool());
    	    	context.put("dispalyTool", new DisplayTool());
    	    	context.put("dateTool", new ComparisonDateTool());
    	    	context.put(Integer.class.getSimpleName(), Integer.class);
    	    	context.put("session", session);
                if(errDesc!=null && errDesc.trim().contains("successfully")){
                	context.put("isErrDesc", "No");
                	context.put("status", errDesc);
                }else if(CommonUtility.validateString(errDesc).toUpperCase().contains("DUPLICATE")){
                	context.put("isErrDesc", "Yes");
                	context.put("status", errDesc);
                	context.put("duplicatePOStatus",SendMailUtility.propertyLoader("duplicate.status.po",""));
                	if(CommonUtility.validateString(SendMailUtility.propertyLoader("duplicate.status.po.subject","")).length()>0){
                		sendMailModel.setMailSubject(SendMailUtility.propertyLoader("duplicate.status.po.subject",""));
                	}
                }else if(errDesc!=null && !errDesc.trim().toUpperCase().contains("ERROR")){
                	context.put("isErrDesc", "No");
                	context.put("status", errDesc);
                }else{
                	context.put("isErrDesc", "Yes");
                	context.put("status", "Order Failed.");
                }
                
                context.put("errDesc", errDesc);
                context.put("firstName", sendMailModel.getFirstName());
                context.put("lastName", lastName);
                context.put("userName", sendMailModel.getUserName());
                context.put("customerServiceEmailId", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_EMAIL"));
                context.put("siteDisplayName", CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME"));
                context.put("webAddress", CommonDBQuery.getSystemParamtersList().get("WEB_ADDRESS"));
                context.put("siteName", CommonDBQuery.getSystemParamtersList().get("SITE_NAME"));
                if(CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER")!=null && CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER").trim().length()>0){
                	context.put("customerServiceNumber", CommonDBQuery.getSystemParamtersList().get("CUSTOMER_SERVICE_NUMBER"));
                }
                String orderSuffux = "";
    			if(CommonUtility.validateParseIntegerToString(orderDetail.getOrderSuffix()).length()>0){
    				orderSuffux = "- 0"+CommonUtility.validateParseIntegerToString(orderDetail.getOrderSuffix());
    				context.put("orderSuffux", orderSuffux);
    			}
    			
    			String contextObject = addContextObject(orderDetail, context);

    			if(notificationDetail!=null && CommonUtility.validateString(notificationDetail.get("DESCRIPTION")).length()>0){
    			
    	            StringWriter writer = new StringWriter();
    	            Velocity.evaluate(context, writer, "", notificationDetail.get("DESCRIPTION"));
    	            
    	            StringBuilder finalMessage= new StringBuilder();
    	            finalMessage.append(writer.toString());
    	
    				String fromEmail = CommonDBQuery.getSystemParamtersList().get("FROMMAILID");
    				//String customerEmail = CommonDBQuery.getSystemParamtersList().get("CUSTOMEREMAIL");
    				
    				if(notificationDetail.get("FROM_EMAIL")!=null){
    					fromEmail = notificationDetail.get("FROM_EMAIL");
    				}
    				
    				if(session!=null && session.getAttribute("wareHouseEmailID")!=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_WAREHOUSE_SPECIFIC_ORDER_MAIL")).equalsIgnoreCase("Y")){
    					String toFromNotification = notificationDetail.get("TO_EMAIL");
    					 String toEmailIdWarehouse = (String)session.getAttribute("wareHouseEmailID");
    					if(toFromNotification!=null && toFromNotification.trim().length()>0 && !toFromNotification.trim().equalsIgnoreCase("null")){
    						notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+toEmailIdWarehouse);
    					}else{
    						//notificationDetail.put("TO_EMAIL", toEmailIdWarehouse);
    						notificationDetail.put("TO_EMAIL", toEmailIdWarehouse+";"+toEmailId);
    					}
    				}else if(CommonUtility.validateString(toEmailId).length()>0 && !CommonUtility.validateString(toEmailId).equalsIgnoreCase("null")){
    					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ORDER_MAIL_TO_USER_ONLY")).equalsIgnoreCase("Y")){
    						toEmailId = CommonUtility.validateString(toEmailId);// Only user who is placing order
    					}/*else{
    						if(orderDetail!= null && orderDetail.getShipAddress().getEmailAddress()!=null && orderDetail.getShipAddress().getEmailAddress().trim().length()>0 && !toEmailId.trim().equalsIgnoreCase(orderDetail.getShipAddress().getEmailAddress())){
    							toEmailId = toEmailId+";"+orderDetail.getShipAddress().getEmailAddress().trim();
    			            }else if(orderDetail!= null && orderDetail.getShipEmailAddress()!=null && orderDetail.getShipEmailAddress().trim().length() > 0 && !toEmailId.trim().equalsIgnoreCase(orderDetail.getShipEmailAddress())){
    			            	toEmailId = toEmailId+";"+orderDetail.getShipEmailAddress().trim();
    			            }
    					}*/
    					String toFromNotification = notificationDetail.get("TO_EMAIL");
    					if(toFromNotification!=null && toFromNotification.trim().length()>0 && !toFromNotification.trim().equalsIgnoreCase("null")){
    						notificationDetail.put("TO_EMAIL", notificationDetail.get("TO_EMAIL")+";"+toEmailId);
    					}else{
    						notificationDetail.put("TO_EMAIL", toEmailId);
    					}
    				}
    				
    				if(orderMailAddressCustomerWise!=null && orderMailAddressCustomerWise.trim().length()>0 && !orderMailAddressCustomerWise.trim().equalsIgnoreCase("null")){
    					String bccFromNotification = notificationDetail.get("BCC_EMAIL");
    					if(bccFromNotification!=null && bccFromNotification.trim().length()>0 && !bccFromNotification.trim().equalsIgnoreCase("null")){
    						notificationDetail.put("BCC_EMAIL", notificationDetail.get("BCC_EMAIL")+";"+orderMailAddressCustomerWise);
    					}else{
    						notificationDetail.put("BCC_EMAIL", orderMailAddressCustomerWise);
    					}
    					
    				}
    				
    				if(CommonUtility.validateString(orderDetail.getSendmailToSalesRepOnly()).equalsIgnoreCase("Y")){
    					if(CommonUtility.validateString(UsersDAO.getUserCustomField(sendMailModel.getUserId(), "IN_SIDE_SALES_REP")).length()>0){
    						notificationDetail.put("TO_EMAIL",toEmailId);
    						notificationDetail.put("CC_EMAIL",UsersDAO.getUserCustomField(sendMailModel.getUserId(), "IN_SIDE_SALES_REP"));
    					}
    				}
    				
    				//notificationDetail.put("TO_EMAIL", (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL"):""));
    				//notificationDetail.put("BCC_EMAIL", (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";":"")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise:""));
    				
    				
    				String orderMailSubject = sendMailModel.getMailSubject();
    				/*if(CommonUtility.validateParseIntegerToString(orderDetail.getOrderSuffix()).length()>0){
    					orderSuffux = "-0"+CommonUtility.validateParseIntegerToString(orderDetail.getOrderSuffix());
    				}*/
    				if(massOrderEmail && erpOrderDetailList != null && erpOrderDetailList.size() > 1) {
    					orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject;
    				} else if(orderDetail!= null && orderDetail.getErpOrderNumber()!=null && CommonUtility.validateString(orderDetail.getErpOrderNumber()).length()>0){
    					orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject+" - #"+CommonUtility.validateString(orderDetail.getErpOrderNumber());
    				}else if(orderDetail!= null && orderDetail.getOrderNum()!=null && CommonUtility.validateString(orderDetail.getOrderNum()).length()>0){
    					orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject+" - #"+orderDetail.getOrderNum();
    				}else{
    					if(CommonUtility.validateString((String) session.getAttribute("erpType")).equalsIgnoreCase("DEFAULTS")){
    						orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+orderMailSubject;
    					}else {
    						orderMailSubject = CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")+" "+" - Order Failure";
    					}
    				}
    				
    				flag = new SendMailUtility().sendNotification(notificationDetail, orderMailSubject, fromEmail, finalMessage.toString());
    				
    				String toMailList = (notificationDetail.get("TO_EMAIL")!=null?notificationDetail.get("TO_EMAIL")+";" : toEmailId);
    				String bccMailList = (notificationDetail.get("BCC_EMAIL")!=null?notificationDetail.get("BCC_EMAIL")+";" : "")+(orderMailAddressCustomerWise!=null?orderMailAddressCustomerWise : "");
    				
    				String mailStatus = "N";
    				if(flag){
    					mailStatus = "Y";
    				}
    				boolean saveMailStatus = UsersDAO.saveEmailsInDB(toMailList, orderMailSubject, fromEmail, finalMessage, mailStatus, sendMailModel.getMailSubject(),bccMailList);
    				System.out.println("order Email in DB Status @ Order Confirmation Mail : "+saveMailStatus);
    				
    				orderListCount++;
    				
    				// send Mass Order Email to Order placer
    				if(orderListSize == orderListCount) {
        	            sendMultiOrderMail(erpOrderDetailList, sendMailModel, true);
    				}
    				
    				if(massOrderEmail) {
    					break;
    				}
    			}
            }
        }catch(Exception e){
        	e.printStackTrace();
        }
	    
       	return flag;
	}
	
	public String orderConfirmationV2AP(ArrayList<SalesModel> erpOrderDetailList) {
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("cartCountSession");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String buyingCompanyId = (String) session.getAttribute("buyingCompanyId");
		String discountValue = (String) session.getAttribute("discountValue");
		String promotionCode = (String) session.getAttribute("promotionCode");
		String requestType = CommonUtility.validateString((String)session.getAttribute("requestType"));
		String shipViaDisplay = "";
		String orderError = "";
		String renderContent = "";
		String billEntityName = "";
		String shipEntityName = "";
		String auEmail;
		int defaultBillToId;
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		HashMap<String, Integer> userAddressId;
		
		UsersModel userDetail = (UsersModel) session.getAttribute("auUserDetails");
		String auUser = (String)session.getAttribute("auUserLogin");
		SalesModel orderDetails = null;
		
		if(erpOrderDetailList != null && erpOrderDetailList.size() > 0) {
			orderDetails = erpOrderDetailList.get(0);
		} else {
			orderDetails = new SalesModel();
		}
		
		shipViaDisplay = CommonUtility.validateString(orderDetails.getShipViaDescription());
		
		int userId = 1;
		if(CommonUtility.validateString(auUser).equalsIgnoreCase("Y")){
			userId = userDetail.getUserId();
		}else if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GUSTE_CHECKOUT_USER_NAME")).length()>0 && auUser!=null){
			auEmail = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("GUSTE_CHECKOUT_USER_NAME"));
			HashMap<String, String> userDetailsFromDB = UsersDAO.getUserPasswordAndUserId(auEmail, "Y");
			userId= CommonUtility.validateNumber(userDetailsFromDB.get("userId"));
			userDetail.setUserId(userId);
		}else{
			userId = CommonUtility.validateNumber(sessionUserId);
		}

		List<Discount> appliedlDiscounts = new ArrayList<Discount>();
		if(userId>1){
			try {
				ArrayList<Integer> itemList = new ArrayList<Integer>();
				int subsetId = 0;
				int generalSubset = 0;
				if(CommonUtility.validateString(auUser).equalsIgnoreCase("Y")){
					subsetId = userDetail.getSubsetId();
					String tempGeneralSubset = CommonDBQuery.getSystemParamtersList().get("GENERALCATALOGID");
					generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
				}else{
					String tempSubset = (String) session.getAttribute("userSubsetId");
					subsetId = CommonUtility.validateNumber(tempSubset);
					String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
					generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
	
	
					if(requestType != null && requestType.equalsIgnoreCase("mobile")) {
						userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook((String)session.getAttribute("mobileUserName"));
						userId = CommonUtility.validateNumber((String)session.getAttribute("mobileUserId"));
					}else{
						userAddressId = UsersDAO.getDefaultAddressIdForBCAddressBook((String)session.getAttribute(Global.USERNAME_KEY));
					}
	
					defaultBillToId = userAddressId.get("Bill");
					String defaultShiptoId = (String) session.getAttribute("defaultShipToId");
	
					//HashMap<String, UsersModel> userAddress = UsersDAO.getUserAddressFromBCAddressBook(defaultBillToId,CommonUtility.validateNumber(defaultShiptoId));
					UserManagement userObj = new UserManagementImpl();
					HashMap<String, UsersModel> userAddress = userObj.getUserAddressFromBCAddressBook(defaultBillToId, CommonUtility.validateNumber(defaultShiptoId));
	
					UsersModel userBillAddress = userAddress.get("Bill");
					UsersModel userShipAddress = userAddress.get("Ship");
	
					if(userBillAddress.getCustomerName()!=null){
						billEntityName = userBillAddress.getCustomerName();
					}else{
						billEntityName = userBillAddress.getFirstName()+" "+userBillAddress.getLastName();
					}
	
					if(userShipAddress.getCustomerName()!=null){
						shipEntityName = userShipAddress.getCustomerName();
					}else{
						shipEntityName = userShipAddress.getFirstName()+" "+userShipAddress.getLastName();
					}
	
				}
				if (CommonUtility.validateString(promotionCode)!="" && CommonUtility.validateString(promotionCode).length()>0) {
					int counter = SalesDAO.updateCouponUse(buyingCompanyId, promotionCode, discountValue);
					if (counter > 0)
					{
						System.out.println("COUPONS UPDATED WITH ORDER DISCOUNT");
						session.removeAttribute("discountType");
						session.removeAttribute("discountValue");
						session.removeAttribute("discountValueToERP");
					}
				}
				String cartSortByValue = "";
				if(session.getAttribute("cartSortByValue")!=null){
					cartSortByValue = CommonUtility.validateString(session.getAttribute("cartSortByValue").toString());
					String[] sortCoumnArray = cartSortByValue.split(" ");
					if(sortCoumnArray!=null && sortCoumnArray.length>0){
					}
				}
				LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = null;
				if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
					customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "),"itemid");
				}
				
				/**
				 *Below code Written is for Tyndale to group items based on shipMethods. *Reference- Chetan Sandesh
				 */
				LinkedHashMap<String,ArrayList<SalesModel> > orderItemGroupList = new LinkedHashMap<String, ArrayList<SalesModel>>();
				//CustomServiceProvider
				if(orderDetails != null && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MultipleShipToAddressCartAP")).equalsIgnoreCase("Y")) {
					orderItemGroupList = getGroupedItemsInSalesData(orderDetails.getOrderList());
					contentObject.put("orderItemGroupList", orderItemGroupList);
				}
				//CustomServiceProvider
				
				contentObject.put("orderTotal", orderDetails.getTotal());
				contentObject.put("orderItemList", orderDetails.getOrderList());
				contentObject.put("orderDetail", orderDetails);
				contentObject.put("billAddress", orderDetails.getBillAddress());
				contentObject.put("orderId",orderDetails.getOrderId());
				contentObject.put("userId",orderDetails.getUserId());
				contentObject.put("fullItemDetails",orderDetails.getOrderList());
				contentObject.put("poNumber", orderDetails.getPoNumber());
				contentObject.put("orderShipDate", orderDetails.getShipDate());
				//contentObject.put("handling", orderDetail.getHandling());

				contentObject.put("billEntityName", billEntityName);
				contentObject.put("shipEntityName", shipEntityName);
				contentObject.put("discountValue", discountValue);
				contentObject.put("shipViaDisplay", shipViaDisplay);
				contentObject.put("customFieldVal",customFieldVal);
				contentObject.put("appliedDiscountCoupons",appliedlDiscounts);
				contentObject.put("erpOrderDetailList", erpOrderDetailList);
				contentObject.put("orderError",orderError);
				renderContent = LayoutGenerator.templateLoader("OrderConfirmation", contentObject , null, null, null);
				if(CommonUtility.validateString(auUser).equalsIgnoreCase("Y") && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_BRONTO_ORDER_MODULE")).equalsIgnoreCase("Y")){
					session.removeAttribute("auUserDetails");
					session.removeAttribute("auUserLogin");
					session.removeAttribute("fromPage");
				}
				session.removeAttribute("cartSortByValue");
			} catch(Exception e){
				e.printStackTrace();
			} finally{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);	
				ConnectionManager.closeDBConnection(conn);
			}

			if (requestType.equalsIgnoreCase("")) {
				return renderContent;
			}else{
				return "orderSubApp";
			}
		}else{
			return "SESSIONEXPIRED";
		}
	}
	public String validateNewPurchaseAgent(UsersAction usersAction) {
		StringBuilder result = new StringBuilder("");
		
		if(usersAction.getContactemailAddress().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.emailAddress")).append("|");
		}else{
			if(!CommonUtility.validateEmail(usersAction.getContactemailAddress())){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.validEmailAddress")).append("|");
			}else if(UsersDAO.isRegisteredUser(usersAction.getContactemailAddress())){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.emailIdExists")).append("|");
			}
		}
		if(usersAction.getContactFirstName().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.firstName")).append("|");
		}
		if(usersAction.getContactLastName().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.lastName")).append("|");
		}
		
		if(usersAction.getPassword().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.password")).append("|");
		}else{ 
			if(usersAction.getPassword().trim().length()<8){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.passwordMinimumCharacters")).append("|");
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
				if(!NewUserRegisterUtility.isAlfaNumericOnly(usersAction.getPassword())){
					result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.passwordCharacters")).append("|");
				}
			}
		
		}
		if(usersAction.getConfirmPassword().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.confirmPassword")).append("|");
		}
		if(!usersAction.getPassword().trim().equalsIgnoreCase("")){
		
			if(!usersAction.getConfirmPassword().trim().equalsIgnoreCase("")){
				
				if(usersAction.getPassword().trim().compareTo(usersAction.getConfirmPassword())!=0){
					result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.passwordMismatch")).append("|");
				}
			}
		}
		if(usersAction.getContactPhone().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.officePhone")).append("|");
		}else{
			if(!CommonUtility.validatePhoneNumber(usersAction.getContactPhone())){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.validOfficePhone")).append("|");
			}else{
				usersAction.setContactPhone(usersAction.getContactPhone().replaceAll("[^0-9]", ""));
			}
		}
		if(usersAction.getContactFax().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.cellPhone")).append("|");
		}else{
			if(!CommonUtility.validatePhoneNumber(usersAction.getContactFax())){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.validCellPhone")).append("|");
			}else{
				usersAction.setContactFax(usersAction.getContactFax().replaceAll("[^0-9]", ""));
			}
		}
		if(result.length()>0) {
			result.insert(0, "0|");
		}
		return result.toString();
	}
	
	public String validateUserRegistrationDetail(NewUserRegisterUtility user) {
		StringBuffer result = new StringBuffer("");
		//step-1
		if(user.getContactFirstName().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.firstName")).append("|");
		}
		if(user.getContactLastName().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.lastName")).append("|");
		}
		
		if(user.getContactemailAddress().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.emailAddress")).append("|");
		}else{
			if(!CommonUtility.validateEmail(user.getContactemailAddress())){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.validEmailAddress")).append("|");
			}else if(UsersDAO.isRegisteredUser(user.getContactemailAddress())){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.emailIdExists")).append("|");
			}
		}
		
		if(user.getContactPassword().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.password")).append("|");
		}else{ 
			if(user.getContactPassword().trim().length()<8){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.passwordMinimumCharacters")).append("|");
			}
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ALLOW_PASSWORD_SPECIAL_CHAR")).equalsIgnoreCase("N")){
				if(!NewUserRegisterUtility.isAlfaNumericOnly(user.getContactPassword())){
					result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.passwordCharacters")).append("|");
				}
			}
		
		}

		if(user.getContactCompanyName().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.organizationName")).append("|");
		}
		if(user.getContactPhone().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.contactNumber")).append("|");
		}else{
			if(!CommonUtility.validatePhoneNumber(user.getContactPhone())){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.validContactNumber")).append("|");
			}else{
				user.setContactPhone(user.getContactPhone().replaceAll("[^0-9]", ""));
			}
		}
		if(user.getContactClassification1B().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1A.error.organizationType")).append("|");
		}
		
		//step-2
		if(user.getCompanyName1B().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingOrganizationName")).append("|");
		}
		if(user.getFirstName1B().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingAttention")).append("|");
		}
		if(user.getContactAddress1().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingAddress1")).append("|");
		}
		if(user.getCityName1B().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingCity")).append("|");
		}
		if(user.getStateName1B().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingState")).append("|");
		}
		
		if(user.getZipCode1B().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingZipCode")).append("|");
		}
		
		if(user.getCountryName1B().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingCountry")).append("|");
		}
		if(user.getPhoneNo1B().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingPhone")).append("|");
		}else{
			if(!CommonUtility.validatePhoneNumber(user.getPhoneNo1B())){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingValidPhone")).append("|");
			}else{
				user.setPhoneNo1B(user.getPhoneNo1B().replaceAll("[^0-9]", ""));
			}
		}
		if(user.getEmailAddress1B().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingEmail")).append("|");
		}else if(!CommonUtility.validateEmail(user.getEmailAddress1B())){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.billingValidEmail")).append("|");
		}
		
		if(user.getAuShipCompanyName().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingOrganizationName")).append("|");
		}
		if(user.getShippingOrgType().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingOrganizationType")).append("|");
		}
		if(user.getAuShipFirstName().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingAttention")).append("|");
		}
		if(user.getAuShipAddress1().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingAddress1")).append("|");
		}
		if(user.getAuShipCity().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingCity")).append("|");
		}
		if(user.getAuShipState().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingState")).append("|");
		}
		if(user.getAuShipZipCode().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingZipCode")).append("|");
		}
		if(user.getAuShipCountry().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingCountry")).append("|");
		}
		if(user.getAuShipPhoneNo().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingPhone")).append("|");
		}else{
			if(!CommonUtility.validatePhoneNumber(user.getAuShipPhoneNo())){
				result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingValidPhone")).append("|");
			}else{
				user.setAuShipPhoneNo(user.getAuShipPhoneNo().replaceAll("[^0-9]", ""));
			}
		}
		if(user.getEmailAddress2AB().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingEmail")).append("|");
		}else if(!CommonUtility.validateEmail(user.getEmailAddress2AB())){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingValidEmail")).append("|");
		}
		
		if(user.getGrandtype()== null || user.getGrandtype().trim().equalsIgnoreCase("")){
			result.append(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("register1B.error.shippingGrantType")).append("|");
		}
		
		return result.toString();
	}
	
	@Override
	public double getExtendedPrice(double price, int orderqty, int salesQty, double salesQuntityUom) {
		int cartonQty = CommonUtility.validateNumber(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(ServletActionContext.getRequest().getSession().getAttribute("localeCode").toString().toUpperCase()).getProperty("form.label.eachCaseQty")));
		if (cartonQty <= 0) {
			cartonQty = 12;
		}
		double extendedPrice = (price * orderqty * cartonQty);
		return extendedPrice;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<SalesModel> getCustomerOrderHistory(Map<String, String> inputParam) {
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt=null;
		ArrayList<SalesModel> orderStatus = new ArrayList<SalesModel>();
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		LinkedHashMap<String, Object> userAllShipAddress = null;
		ArrayList<UsersModel> userAddressList = null;
		LinkedHashMap<String, UsersModel> userAddress = null;
		HttpSession session = ServletActionContext.getRequest().getSession();
		int buyingCompanyId = 0;
		String shipToId = null;
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = df1.parse(inputParam.get("startDate"));
			toDate = df1.parse(inputParam.get("endDate"));
			
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getOrdersHistoryForCustomer"));
			pstmt.setInt(1, CommonUtility.validateNumber(inputParam.get("userId")));
			pstmt.setString(2, CommonUtility.validateString(inputParam.get("customerErpId")));
			pstmt.setString(3, CommonUtility.validateString((String) session.getAttribute("buyingCompanyId")) );
			pstmt.setString(4, df.format(fromDate));
			pstmt.setString(5, df.format(toDate));
			
			rs = pstmt.executeQuery();
			
			if(rs.getFetchSize() >= 1) {
				userAllShipAddress = new LinkedHashMap<String, Object>();
				buyingCompanyId = CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId"));

				userAllShipAddress = getAllShippingAddress(userAllShipAddress, buyingCompanyId);
				if(userAllShipAddress != null && !userAllShipAddress.isEmpty()) {
					userAddressList = (ArrayList<UsersModel>) userAllShipAddress.get("shipEntity");
					userAddress = new LinkedHashMap<>();
					if(userAddressList != null && !userAddressList.isEmpty()) {
						for(UsersModel userInfo : userAddressList) {
							userAddress.put(userInfo.getShipToId(), userInfo);
						}
					}
				}
			}
			
			while (rs.next()) {
				SalesModel orderStatusVal = new SalesModel();
				orderStatusVal.setOrderId(rs.getInt("ORDER_ID"));
				orderStatusVal.setPoNumber(rs.getString("PURCHASE_ORDER_NUMBER"));
				orderStatusVal.setOrderNum(rs.getString("ORDER_NUMBER"));
				orderStatusVal.setExternalSystemId(rs.getString("EXTERNAL_SYSTEM_ID"));
				Date date = rs.getDate("ORDER_DATE");
				String orderDate = "";
				if(date!=null) {
					orderDate = df.format(date);
				}
				orderStatusVal.setOrderDate(orderDate);
				orderStatusVal.setSubtotal(rs.getDouble("SUBTOTAL_AMOUNT"));
				orderStatusVal.setTotal(rs.getDouble("TOTAL_AMOUNT"));
				orderStatusVal.setShipMethod(rs.getString("SHIP_METHOD"));
				orderStatusVal.setOrderStatus(rs.getString("ORDER_STATUS"));
				orderStatusVal.setReqDate(rs.getString("REQUIRED_BY_DATE"));
				orderStatusVal.setShipAddress1(rs.getString("SHIP_ADDRESS1"));
				orderStatusVal.setShipAddress2(rs.getString("SHIP_ADDRESS2"));
				orderStatusVal.setShipCity(rs.getString("SHIP_CITY"));
				orderStatusVal.setShipState(rs.getString("SHIP_STATE"));
				orderStatusVal.setShipZipCode(rs.getString("SHIP_ZIP_CODE"));
				orderStatusVal.setShipCountry(rs.getString("SHIP_COUNTRY_CODE"));
				orderStatusVal.setPaymentMethod(rs.getString("PAYMENT_METHOD"));
				orderStatusVal.setCarrierTrackingNumber(rs.getString("ORDER_TRACKING_NUMBER"));
				orderStatusVal.setQtyordered(rs.getInt("QTY"));
				shipToId = CommonUtility.validateString(rs.getString("SHIPPING_SHIP_TO_ID"));
				
				if(!shipToId.isEmpty() && userAddress != null && userAddress.containsKey(shipToId)) {
					//orderStatusVal.setStatus(userAddress.get(shipToId).getApproverAgentAssignStatus());
					orderStatusVal.setCompanyName(userAddress.get(shipToId).getCompanyName());
					orderStatusVal.setShipToName(userAddress.get(shipToId).getShipToName());
					orderStatusVal.setShippingOrgType(userAddress.get(shipToId).getShippingOrgType());
				}
				orderStatus.add(orderStatusVal);
			}
		} catch (Exception e) {         
			e.printStackTrace();

		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);	
		} 
		return orderStatus;
	}
	
}