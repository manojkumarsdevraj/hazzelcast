package com.unilog.services.impl;

import java.sql.Connection;

import javax.servlet.http.HttpSession;

import com.erp.service.model.ProductManagementModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class FrommElectricCustomServices implements UnilogFactoryInterface {
	private static UnilogFactoryInterface serviceProvider;
	private FrommElectricCustomServices() {}
	public static UnilogFactoryInterface getInstance() {
			synchronized (FrommElectricCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new FrommElectricCustomServices();
				}
			}
		return serviceProvider;
	}
	@Override
	public String getRetailUserOrdersFromCIMM(String userRole) {
		if(userRole.equalsIgnoreCase("Y")) {
			return userRole;
		}else{
			userRole="N";
		}
		return userRole;
	}
	@Override
	public void setAllBranchAvailValueInGroupAndCart(ProductManagementModel priceInquiryInput) {
		 priceInquiryInput.setAllBranchavailabilityRequired("Y");
	}
	@Override
	public String setFirstLoginTrueForInactiveUser() {
		return "Y";
	}
	@Override
	public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
	{	
		String newsLetterCustomFiledValue="";
		if(userDetailsInput.getNewsLetterSub()!=null && userDetailsInput.getNewsLetterSub().equals("Y")){
			newsLetterCustomFiledValue="Y";
		}else{
			newsLetterCustomFiledValue="N";
		}
		UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
	}
	@Override
	public UsersModel getUserContactAddress(int userId,  HttpSession session) {
		UsersModel addressList = null;
		String isRetailUser = (String) session.getAttribute("isRetailUser");
		if(CommonUtility.validateString(isRetailUser).equalsIgnoreCase("Y")){
			addressList = new UsersModel();
			addressList= UsersDAO.getEntityDetailsByUserId(userId);
			if(addressList!=null){
				session.setAttribute("userContactAddress1",addressList.getAddress1());
				session.setAttribute("userContactAddress2",addressList.getAddress2() );
				session.setAttribute("userContactCity",addressList.getCity());
				session.setAttribute("userContactState",addressList.getState());
				session.setAttribute("userContactZipcode",addressList.getZipCode());
				session.setAttribute("userContactEmailAddress",addressList.getEmailAddress());
				session.setAttribute("userContactPhoneNo",addressList.getPhoneNo());
				session.setAttribute("userContactAddress", addressList);
			}
		}
		return addressList;
	}
	
	@Override
	public void addBankAndTradeReferenceAttachments(String [] attachments, String [] attachmentsFileName, String salesTaxExemptionCertificateFileName, String bankReferenceFileName, String tradeReferenceFileName, String filePath, String sessionId) {
		if (CommonUtility.validateString(attachments[1]).contains(filePath)) {
			attachments[1] = null;
			attachmentsFileName[1] = null;
		}
		int count = 1;
		if (CommonUtility.validateString(salesTaxExemptionCertificateFileName).length() > 0) {
			attachments[count] = filePath+"/"+salesTaxExemptionCertificateFileName;
    		attachmentsFileName[count] = salesTaxExemptionCertificateFileName.replaceAll(sessionId+"_", "");
    		count+=1;
		}
		if (CommonUtility.validateString(bankReferenceFileName).length() > 0) {
			attachments[count] = filePath+"/"+bankReferenceFileName;
    		attachmentsFileName[count] = bankReferenceFileName.replaceAll(sessionId+"_", "");
    		count+=1;
		}
		if (CommonUtility.validateString(tradeReferenceFileName).length() > 0) {
			attachments[count] = filePath+"/"+tradeReferenceFileName;
    		attachmentsFileName[count] = tradeReferenceFileName.replaceAll(sessionId+"_", "");
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
}
