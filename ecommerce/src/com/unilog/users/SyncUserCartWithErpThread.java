package com.unilog.users;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;

import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsDAO;
import com.unilog.utility.CommonUtility;

	public class SyncUserCartWithErpThread implements Runnable {

	private HttpSession session;
	
	public SyncUserCartWithErpThread(HttpSession _session) {
		this.session = _session;
	}
	@Override
	public void run() {
		long startTimer = CommonUtility.startTimeDispaly();
		try {
			int userId = CommonUtility.validateNumber((String) session.getAttribute(Global.USERID_KEY));
			int siteId = 0;
	    	if(CommonDBQuery.getGlobalSiteId()>0){
	        	siteId = CommonDBQuery.getGlobalSiteId();
	    	}
			int cartPriceSyncCutOffHours = CommonUtility.validateNumber(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInCartSyncCutOffHours")));
			if(cartPriceSyncCutOffHours<=0) {
				cartPriceSyncCutOffHours = 8;
			}
			LinkedHashMap<String, Object> cartDetails = ProductsDAO.getLastUpdateTimeDifferenceFromCart(userId, siteId);
			boolean callErpPiceflag = false;
			if(cartDetails!=null) {
				if(cartDetails.get("daysMaxValue")!=null && (Integer) cartDetails.get("daysMaxValue") > 0) {
					callErpPiceflag = true;
				}else if(cartDetails.get("hoursMaxValue")!=null && (Integer) cartDetails.get("hoursMaxValue") >= cartPriceSyncCutOffHours){
					callErpPiceflag = true;
				}
			}
			if(callErpPiceflag) {
				System.out.println("----------------- Start : Call Cart Price Sync -------------------");
				LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
				contentObject.put("session", session);
				contentObject = ProductsDAO.getShoppingCartDao(session, contentObject);//-- Due to custom services we have to use getShoppingcartDao
				if(contentObject != null && contentObject.get("productListData") != null && contentObject.get("partIdentifierQuantity") != null) {
					ProductsDAO.updateCartItemPrice(contentObject);
				}
				System.out.println("----------------- End of Call Cart Price Sync --------------------");
			}else {
				System.out.println("----------------- Do not call Cart Price Sync --------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	}
	
	
	
	
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}

}
