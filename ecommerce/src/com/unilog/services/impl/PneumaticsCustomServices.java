package com.unilog.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.hazelcast.util.Base64;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.SendMailUtility;
import com.unilog.products.ProductsModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.AddressModel;
import com.unilog.users.CreditApplicationModel;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.ConvertHtmlToPdf;

public class PneumaticsCustomServices implements UnilogFactoryInterface {

	private static UnilogFactoryInterface serviceProvider;
	private PneumaticsCustomServices() {}
	public static UnilogFactoryInterface getInstance() {
			synchronized (PneumaticsCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new PneumaticsCustomServices();
				}
		}
		return serviceProvider;
	}
	@Override
	public double getdefaultCustomerPrice(ProductsModel eclipseitemPrice,ProductsModel itemPrice,double price) {
		double defaultCustprice = price + eclipseitemPrice.getPrice();
		return defaultCustprice;
	}
	@Override
	public void getUnitPrice(ProductsModel eclipseitemPrice,ProductsModel itemPrice) {
			itemPrice.setUnitPrice(eclipseitemPrice.getPrice());
	}
	@Override
	public String setFirstLoginTrueForInactiveUser() {
		return "Y";
	}
	@Override
	public String getRetailUserOrdersFromCIMM(String userRole) {
		if(userRole.equalsIgnoreCase("Y")){
			return userRole;
		}else{
			userRole="N";
		}
		return userRole;
	}
	@Override
	public UsersModel getUserContactAddress(int userId,  HttpSession session) {
		UsersModel addressList = null;
		String isRetailUser = (String) session.getAttribute("isRetailUser");
		if(isRetailUser=="Y"){
			addressList = new UsersModel();
			addressList= UsersDAO.getEntityDetailsByUserId(userId);
			session.setAttribute("userContactAddress", addressList);
		}
		return addressList;
	}
	@Override
	public void getRetailCustomerUserERPId(UsersModel contactInformation,String custNumber) {
		if(custNumber!=null) {
			contactInformation.setUserERPId(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("RETAIL_CUSTOMERS_USER_CONTACT_ID")));
		}
	}
	@Override
	public String setPricingDefaultWareHouseCode(String warehouseCode) {
		if(warehouseCode!=null) {
			warehouseCode = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_PRICING_WAREHOUSE_CODE"));
		}
		return warehouseCode;
	}
	@Override
	public void setBillToShipToAddressFields(String firstName, UsersModel userBillAddress, AddressModel selectedShipAddress, HttpSession session){
		String isRetailUser = (String) session.getAttribute("isRetailUser");
		selectedShipAddress.setCompanyName(CommonUtility.validateString(firstName));
		if(isRetailUser=="Y"){
			session.setAttribute("shipTofirstName",CommonUtility.validateString(firstName));
			selectedShipAddress.setCompanyName(CommonUtility.validateString(firstName));
			AddressModel overrideBillAddress = (AddressModel) session.getAttribute("overrideBillAddress");
			if(overrideBillAddress!=null){
			if(CommonUtility.validateString(overrideBillAddress.getCompanyName()).length()>0) {
				String[] billToName = overrideBillAddress.getCompanyName().split(" ");
				if(billToName!=null && billToName.length>0){
					userBillAddress.setFirstName(CommonUtility.validateString(billToName[0]));
					userBillAddress.setLastName(CommonUtility.validateString(billToName[1]));
				}
				userBillAddress.setCustomerName(overrideBillAddress.getCompanyName());
				userBillAddress.setCompanyName(overrideBillAddress.getCompanyName());
			}
		  }
		}
	}
	@Override
	public String getPartnumberUomQtySplitChar() {
		String partNumberUomQtySeparator = CommonDBQuery.getSystemParamtersList().get("PARTNUMBER_UOM_QTY_SPLIT_CHARACTER");
		return partNumberUomQtySeparator;
	}
	@Override
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
	  		if(CommonUtility.validateString(creditAplication.getSignature()).length()>0) {
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
	  	/*if(creditAplication.getSalesTaxStatusRadio().equalsIgnoreCase("No")){
	  		attachments[2]=creditAplication.getSalesTaxExemptionCertificateFileName();
	  		attachmentsFileName[2]="Sales_Attachment";
	  				
	  	}*/
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
}
