package com.unilog.services.impl;

import javax.servlet.http.HttpServletRequest;

import com.unilog.cimmesb.client.response.CimmLineItem;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.SendMailUtility;
import com.unilog.products.ProductsModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HubbardSupplyCustomServices implements UnilogFactoryInterface{
	private static UnilogFactoryInterface serviceProvider;
	private HubbardSupplyCustomServices() {}
	public static UnilogFactoryInterface getInstance() {
			synchronized (FrommElectricCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new HubbardSupplyCustomServices();
				}
			}
		return serviceProvider;
	}
	
	public String sendShipAddressFile(UsersModel shipAddressInfo, String result, String fileNameToUpload, String addFirstName){
		
		String message="";
		SendMailUtility sendMailUtility = new SendMailUtility();
		boolean sentFlag = false;
		sentFlag = sendMailUtility.sendRegistrationMail(shipAddressInfo,"customer","","addNewShippingAddress"); //"2B"
		if(sentFlag){
			message="1|Add new shipping address Email sent successfully";	
		}else{
			message="0|Add new shipping address Email sending failed";
		}

		return message;
	}
	@Override
	public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
	{	
		String newsLetterCustomFiledValue="";
		if(userDetailsInput.getNewsLetterSub()!=null && userDetailsInput.getNewsLetterSub().equals("on")){
			newsLetterCustomFiledValue="Y";
		}else{
			newsLetterCustomFiledValue="N";
		}
		UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
	}
	
	@Override
	public String setRushFlagToLineItems(CimmLineItem lineItem, ProductsModel item) {
		String rushFlag = "N";
		if(CommonUtility.validateString(item.getAdditionalProperties()).equalsIgnoreCase("Y")) {
			rushFlag = "Y";
		}
		return rushFlag;
	}
	
	@Override
	public String setFirstLoginTrueForInactiveUser() {
		return "Y";
	}
	
	@Override
	public String setShipViaCodeToShipVia(String shipVia, HttpServletRequest request) {
		if(CommonUtility.validateString(request.getParameter("selectedShipViaWL")).length()>0) {
			shipVia = CommonUtility.validateString(request.getParameter("selectedShipViaWL"));
		}
		return shipVia;
	}
	
	@Override
	public void getUnitPrice(ProductsModel eclipseitemPrice,ProductsModel itemPrice) {
		itemPrice.setUnitPrice(eclipseitemPrice.getPrice());
	}
	
	@Override
	public double getdefaultCustomerPrice(ProductsModel eclipseitemPrice,ProductsModel itemPrice,double price) {
		double defaultCustprice = eclipseitemPrice.getPrice();
		return defaultCustprice;
	}
	
	public int addNonCatalogToRfqCart(String sessionId, int userId, int qty, String desc, String part,ProductsModel itemDetails)
	{
	Connection conn = null;
	PreparedStatement pstmt = null;	
	int count = -1;
	String sql = "INSERT INTO RFQ_CART (RFQ_CART_ID,USER_ID,ITEM_ID,ITEM_PRICE_ID,MANUFACTURER_PART_NO,MANUFACTURER_NAME,QTY,SHORT_DESC,UPDATED_DATETIME,SESSIONID) VALUES (RFQ_CART_ID_SEQ.NEXTVAL,?,?,?,?,?,?,?,SYSDATE,?)";
	
	try
	{
		conn = ConnectionManager.getDBConnection();
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		pstmt.setInt(2, itemDetails.getItemId());
		pstmt.setInt(3, itemDetails.getItemPriceId());
		pstmt.setString(4, part);
		pstmt.setString(5, part);
		pstmt.setInt(6, qty);
		pstmt.setString(7, desc);
		pstmt.setString(8, sessionId);
		count = pstmt.executeUpdate();
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	finally
	{	
		ConnectionManager.closeDBConnection(conn);
		ConnectionManager.closeDBPreparedStatement(pstmt);		
	}
	return count;
	}
	
	public void setMinOrdQtyandQtyIntForMultpleUOM(ProductsModel eclipseitemPrice,ProductsModel itemPrice) {
		double defaultQty=1.0;
		if(eclipseitemPrice.getUomList()!=null && eclipseitemPrice.getUomList().size()>1)	{
			itemPrice.setMinimumOrderQuantity(defaultQty);
			itemPrice.setOrderQuantityInterval(defaultQty);

		}
	}
	
}
