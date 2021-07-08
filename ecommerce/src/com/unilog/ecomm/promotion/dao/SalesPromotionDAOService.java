package com.unilog.ecomm.promotion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.log4j.Logger;

import com.unilog.database.ConnectionManager;
import com.unilog.ecomm.model.Discount;
import com.unilog.ecomm.model.DiscountType;
import com.unilog.ecomm.promotion.exception.SalesPromotionException;
import com.unilog.utility.CommonUtility;

public class SalesPromotionDAOService {
	
	private static Logger logger = Logger.getLogger(SalesPromotionDAOService.class.getName());
	
	private static SalesPromotionDAOService serviceInstance;
	
	private SalesPromotionDAOService() {
		// avoid direct instance
	}
	
	public static SalesPromotionDAOService getInstance() {
		synchronized(SalesPromotionDAOService.class) {
			if(serviceInstance==null) {
				serviceInstance = new SalesPromotionDAOService();
			}
		}
		return serviceInstance;
	}
	
	

	public boolean storeCouponUsed(Discount discount, String orderNo, long userId, long buyingCompanyId,
			String website)throws SalesPromotionException{
		
		
		Connection conn = null;
		String result  = "";
		boolean returnFlag = false;
		String[] rowData = new String[3];
		String[][] data = new String[5][3];
		

		CallableStatement updateCall = null;
		DiscountType  discountType = discount.getDiscountType();
	
		try{
			
			rowData[0] = "0";
			rowData[1] = "COUPON_CODE";
			rowData[2] = discount.getDiscountCoupon().getCopounCode();
			
			data[0] = rowData;
			rowData = new String[3];
			
			rowData[0] = "0";
			rowData[1] = "ORDER_NUMBER";
			rowData[2] = orderNo; 
			
			data[1] = rowData;
			rowData = new String[3];
			
			rowData[0] = "0";
			rowData[1] = "USED_DATE";
			rowData[2] = new Date().toString();
			
			data[2] = rowData;
			rowData = new String[3];
			
			rowData[0] = "0";
			rowData[1] = "TOTAL_DISCOUNT_AVAILED";
			rowData[2] = String.format("%4.3f",discount.getDiscountValue()); 
			
			data[3] = rowData;
			rowData = new String[3];
			
			rowData[0] = "0";
			rowData[1] = "WEBSITE_NAME";
			rowData[2] = website; 
			
			data[4] = rowData;
			
			conn = ConnectionManager.getDBConnection();
			
			
			if (conn.isWrapperFor(OracleConnection.class)){
				conn = conn.unwrap(OracleConnection.class);  
			}
			
			ArrayDescriptor arDesc = new ArrayDescriptor("STRING_ARRAY2D",conn);
			ARRAY array = new ARRAY(arDesc,conn, data);
			
			updateCall = conn.prepareCall("{call SAVE_CUSTOM_FIELD_VALUES(?,?,?,?,?,?)}");
			
			if(discountType == DiscountType.USER_SPECIFIC || discountType == DiscountType.ALL_USER){
				updateCall.setString(1, "USER");
				updateCall.setString(2, "USER_COUPON_USE"); // Custom Field name
				updateCall.setLong(3,userId ); // entity id
			}else {
				updateCall.setString(1, "BUYING_COMPANY");
				updateCall.setString(2, "CUSTOMER_COUPON_USE"); // Custom Field name
				updateCall.setLong(3,buyingCompanyId ); // entity id
			}
			
			updateCall.setLong(4, userId); // user Id
			updateCall.setArray(5, array);
			updateCall.registerOutParameter(6, OracleTypes.VARCHAR);
			
			updateCall.execute();
			
			result = updateCall.getString(6);
			
			if(result.equalsIgnoreCase("SUCCESS")) {
				returnFlag =  true;
			}
		}catch(Exception e ){
			e.printStackTrace();
			//throw new SalesPromotionException("Error occured while saving the coupon " + discount.getDiscountCoupon().getCopounCode(),e);
		}finally{
				closeCallableStatement(updateCall);
				ConnectionManager.closeDBConnection(conn);	
		} 
		
		return returnFlag;
		
	}


	public Map<String, Long> getCouponsUsed(long userId, long buyingCompanyId)throws SalesPromotionException {
		
		Connection conn = null;
		ResultSet res = null;
		ResultSet res1  = null;
	    CallableStatement readCall = null;
	    
	    Map<String,Long> couponsUsed = new HashMap<String,Long>();
	    
		try{
				
			conn = ConnectionManager.getDBConnection();
			
			/*if (conn.isWrapperFor(OracleConnection.class)){
				conn = conn.unwrap(OracleConnection.class);  
			}*/
			
			// read customer coupon usage
			
			readCall = conn.prepareCall("{call READ_CUSTOM_FIELD_VALUES(?,?,?,?,?,?,?,?,?)}");
			readCall.setString(1, "BUYING_COMPANY");
			readCall.setString(2, "CUSTOMER_COUPON_USE"); // Custom Field name
			readCall.setLong(3, buyingCompanyId); // Replace actual user id
			readCall.setLong(4, 1);
			readCall.setLong(5, 999999);
			readCall.setString(6, "");
			
			readCall.registerOutParameter(7, OracleTypes.CURSOR);
			readCall.registerOutParameter(8, OracleTypes.CURSOR);
			readCall.registerOutParameter(9, OracleTypes.VARCHAR);
			
			readCall.execute();
			
			if( readCall.getObject(7) != null) res = (ResultSet) readCall.getObject(7); // Data 
			
			
			if(res != null){
				while(res.next()){
					String couponCode = res.getString("COUPON_CODE");
					if(CommonUtility.validateString(couponCode).length()>0){
						long usageCount = couponsUsed.get(couponCode.toUpperCase()) != null ? couponsUsed.get(couponCode.toUpperCase()):0;
						couponsUsed.put(couponCode.toUpperCase(),usageCount + 1);	
					}
				}
			}
			
			res1 = (ResultSet) readCall.getObject(8); // Count
			
			if(res1 != null){
				if(res1.next()){
					long rowCount = res1.getLong("TOTAL_ROWS");
					logger.debug("Total Rows from coupons used for Buying Company Id " + buyingCompanyId + " is - " + rowCount);
				}
			}
			
			String status = readCall.getString(9); // status
			logger.debug("Read coupons used call status for Buying Company Id " + buyingCompanyId + " is - " + status);
		
		
			ConnectionManager.closeDBResultSet(res);
			ConnectionManager.closeDBResultSet(res1);
			closeCallableStatement(readCall);
		
		
			// read user coupon usage
			readCall = conn.prepareCall("{call READ_CUSTOM_FIELD_VALUES(?,?,?,?,?,?,?,?,?)}");
			readCall.setString(1, "USER");
			readCall.setString(2, "USER_COUPON_USE"); // Custom Field name
			readCall.setLong(3, userId); // Replace actual user id
			readCall.setLong(4, 1);
			readCall.setLong(5, 999999);
			readCall.setString(6, "");
			
			readCall.registerOutParameter(7, OracleTypes.CURSOR);
			readCall.registerOutParameter(8, OracleTypes.CURSOR);
			readCall.registerOutParameter(9, OracleTypes.VARCHAR);
			
			readCall.execute();
			
			
			if( readCall.getObject(7) != null) res = (ResultSet) readCall.getObject(7); // Data 
			
			if(res != null){
				while(res.next()){
					String couponCode = res.getString("COUPON_CODE");
					if(CommonUtility.validateString(couponCode).length()>0){
						long usageCount = couponsUsed.get(couponCode.toUpperCase()) != null ? couponsUsed.get(couponCode.toUpperCase()):0;
						couponsUsed.put(couponCode.toUpperCase(),usageCount + 1);
					}
				}
			}
			
			res1 = (ResultSet) readCall.getObject(8); // Count
			
			if(res1 != null){
				if(res1.next()){
					long rowCount = res1.getLong("TOTAL_ROWS");
					logger.debug("Total Rows from coupons used for user Id " + userId + " is - " + rowCount);
				}
			}
				
			status = readCall.getString(9); 
			logger.debug("Read coupons used call status forUser Id " + userId + " is - " + status);	
			
		}catch(Exception e ){
			//throw new SalesPromotionException("Error occurred while fetching the coupons used from data base.",e);
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(res);
			ConnectionManager.closeDBResultSet(res1);
			closeCallableStatement(readCall);
			ConnectionManager.closeDBConnection(conn);
		} 
		
		return couponsUsed;
	}
		
	void closeCallableStatement(CallableStatement statement){
		if(statement != null){
			try{
				statement.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}	
	
	void closeOracleConnection(OracleConnection conn){
		if(conn != null){
			try{
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}	
}
