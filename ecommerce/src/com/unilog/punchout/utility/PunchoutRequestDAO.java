package com.unilog.punchout.utility;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.simple.JSONObject;

import oracle.jdbc.OracleTypes;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.punchout.model.PunchoutRequestModel;
import com.unilog.security.SecureData;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class PunchoutRequestDAO {

	public static HashMap<String, String> getCXMLUserDetail(Connection conn, String cxmlNetworkId)
	   {
	   	 CallableStatement stmt = null;
	        ResultSet cxmlUserData = null;
	       
	     
	        int resultCount = 0;
	        HashMap<String, String> cxmlResultData = new HashMap<String, String>();
	        
	       
	        try{
	        	
	       	 stmt = conn.prepareCall("{call GET_CXML_PUNCHOUT_USER_INFO(?,?)}");
	       	 stmt.setString(1, cxmlNetworkId);
	       	 stmt.registerOutParameter(2,OracleTypes.CURSOR);
	       	
	       	 
	       	 stmt.execute();
	       	 
	       	 if(stmt.getObject(2)!=null)
	       		 cxmlUserData = (ResultSet) stmt.getObject(2);
	       	 
	       	 
	       	SecureData getPassword = new SecureData();
	       	 while(cxmlUserData!=null && cxmlUserData.next())
	       	 {
	       		 
	       		
	       		 cxmlResultData.put("userName", cxmlUserData.getString("USER_NAME"));
	       		 cxmlResultData.put("passWord", getPassword.validatePassword(cxmlUserData.getString("PASSWORD")));
	       		 resultCount++;
	       	 }
	       	 if(resultCount>1)
	       		 cxmlResultData.put("errMsg", "Multiple Record found for Netword id : "+cxmlNetworkId);
	        }
	        catch(Exception e)
	        {
	       	 e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBResultSet(cxmlUserData);
	        	ConnectionManager.closeDBStatement(stmt);
	        }
	        return cxmlResultData;
	   	
	   }
	
	public static UsersModel getValidBuyer(Connection conn, String sapUserName,String sapUserPassword){
		
		    PreparedStatement pstmt=null;
		    ResultSet rs = null;
		    String sql = "";
		    
		    UsersModel responseValidBuyer = null;
		    try {
		    	String securePassword = SecureData.getPunchoutSecurePassword(sapUserPassword);
	            
	            sql = "SELECT CU.customer_id BUYING_COMPANY_ID,CU.USER_ID, BC.SUBSET_ID, BC.GENERAL_CATALOG_ACCESS FROM punchout_user CU, BUYING_COMPANY BC WHERE USER_NAME =? AND PASSWORD =? AND BC.BUYING_COMPANY_ID = CU.customer_id";//PropertyAction.SqlContainer.get("getSapUser");
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, sapUserName);
	            pstmt.setString(2, securePassword);
	            rs = pstmt.executeQuery();
	            if(rs.next()){
	            	responseValidBuyer = new UsersModel();
	            	responseValidBuyer.setValidBuyer(true);
	            	//responseValidBuyer.setExtrensicType(rs.getString("EXTRENSIC_TYPE"));
	            	responseValidBuyer.setUserId(rs.getInt("USER_ID"));
	            	responseValidBuyer.setEntityId(rs.getString("USER_ID"));
	            }
	           
	        } catch (SQLException e) { 
	            e.printStackTrace();
	        }finally {	    
	        	ConnectionManager.closeDBResultSet(rs);
	        	ConnectionManager.closeDBPreparedStatement(pstmt);
		    	 
		      }
		return responseValidBuyer;
	}
	
	//public static int getPunchoutUser(Connection conn,String cxmlUserName,String cxmlUserPassword,String hookUrl, String sl_UserName,String loginId,String buyCook,String operType,String payLodId,String timeStamp,String custLocation, String fromId, String toId, String userAgent)
	
	public static int getPunchoutUser(Connection conn, PunchoutRequestModel punchoutModel) throws ParseException{

		    PreparedStatement pstmt=null;
		    ResultSet rs = null;
		   String sql = "";
		    int custId=0;
		    int userId = 0;
		    int billingAddress=0;
		    int shippingAddress=0;
		    String addtionalInfo="";
		    try {
	            
	            
	           sql = "SELECT CU.customer_id BUYING_COMPANY_ID,CU.USER_ID, BC.SUBSET_ID, BC.GENERAL_CATALOG_ACCESS FROM punchout_user CU, BUYING_COMPANY BC WHERE USER_NAME =? AND PASSWORD =? AND BC.BUYING_COMPANY_ID = CU.customer_id";//PropertyAction.SqlContainer.get("getSapUser");
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, punchoutModel.getCimmUserName());
	            pstmt.setString(2, SecureData.getPunchoutSecurePassword(punchoutModel.getCimmPassword()));
	            rs = pstmt.executeQuery();
	            if(rs.next())
	            {
	            	custId = rs.getInt("BUYING_COMPANY_ID");
	            	billingAddress=0;//rs.getInt("DEFAULT_BILLING_ADDRESS_ID");
	            	shippingAddress=0;//rs.getInt("DEFAULT_SHIPPING_ADDRESS_ID");
	            	ConnectionManager.closeDBPreparedStatement(pstmt);
	            	ConnectionManager.closeDBResultSet(rs);
	            	sql = PropertyAction.SqlContainer.get("getValidSapUser");
	            	pstmt = conn.prepareStatement(sql);
	            	pstmt.setString(1, punchoutModel.getTempUserName());
	            	pstmt.setInt(2, custId);
	            	rs = pstmt.executeQuery();
	            	if(rs.next()){
	            		userId = rs.getInt("USER_ID");
	            	}else{
	            		ConnectionManager.closeDBResultSet(rs);
	            		ConnectionManager.closeDBPreparedStatement(pstmt);
		            	sql = PropertyAction.SqlContainer.get("insertSapUser");
		            	userId = CommonDBQuery.getSequenceId("USER_ID_SEQUENCE");
		            	//USER_ID,USER_NAME,PASSWORD,BUYING_COMPANY_ID
		            	pstmt = conn.prepareStatement(sql);
		            	pstmt.setInt(1, userId);
		            	pstmt.setString(2, CommonUtility.validateString(punchoutModel.getTempUserName()));
		            	pstmt.setString(3, "cxml"+punchoutModel.getTempUserName().substring(0,3)+"123##");
		            	pstmt.setInt(4, custId);
		            	pstmt.setInt(5, shippingAddress);
		            	pstmt.setInt(6, billingAddress);
		            	pstmt.setString(7, punchoutModel.getExtrinisicFirstName());
		            	pstmt.setString(8, punchoutModel.getExtrinisicLastName());
		            	int count = pstmt.executeUpdate();
	            	}
	            	
	            	if(userId>0){
	            	ConnectionManager.closeDBResultSet(rs);
	            	ConnectionManager.closeDBPreparedStatement(pstmt);
	            	sql =PropertyAction.SqlContainer.get("insertPunchoutLoginDetail");
	            	//USERNAME_CXML,PASSWORD_CXML,SL_USER_ID,SL_USERNAME,SL_USER_PWD,LOGIN_ID,BUYER_COOKIE,OPERATION_TYPE,PAYLOAD_ID,LOGIN_TIME,PUNCHOUT_URL
	            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	            	java.sql.Timestamp tsmp = new java.sql.Timestamp(sdf.parse(punchoutModel.getTimeStamp()).getTime()); 
	            	pstmt = conn.prepareStatement(sql);
	            	pstmt.setString(1, punchoutModel.getCimmUserName());
	            	pstmt.setString(2, SecureData.getPunchoutSecurePassword(CommonUtility.validateString(punchoutModel.getCimmPassword())));
	            	pstmt.setInt(3, userId);
	            	pstmt.setString(4, punchoutModel.getTempUserName());
	            	pstmt.setString(5,"cxml"+punchoutModel.getTempUserName()+"123##");
	            	pstmt.setString(6, punchoutModel.getSessionId());
	            	pstmt.setString(7, punchoutModel.getBuyerCookie());
	            	pstmt.setString(8, punchoutModel.getOperationType());
	            	pstmt.setString(9, punchoutModel.getPayloadId());
	            	pstmt.setTimestamp(10, tsmp);
	            	pstmt.setString(11, punchoutModel.getResponseUrl());
	            	pstmt.setInt(12, custId);
	            	pstmt.setString(13, "");
	            	
	            	pstmt.setString(14,punchoutModel.getFromDomain());
	            	pstmt.setString(15,punchoutModel.getFromId());
	            	pstmt.setString(16,punchoutModel.getToDomain());
	            	pstmt.setString(17,punchoutModel.getToId());
	            	pstmt.setString(18,punchoutModel.getSenderDomain());
	            	pstmt.setString(19,punchoutModel.getSenderId());
	            	pstmt.setString(20, punchoutModel.getUserAgent());	
	            	if(CommonUtility.customServiceUtility() != null )
	            	addtionalInfo= CommonUtility.customServiceUtility().formAdditionalInfo(punchoutModel);	            			
	            	pstmt.setString(21, addtionalInfo);
	            	
	            	int cnt = pstmt.executeUpdate();
	            	}
	            }
	           
	        } catch (SQLException e) { 
	            e.printStackTrace();
	        }
	        finally {	    
	        	ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	
		      } // finally
		return userId;
	}
	
	public static String getTimestamp () {
    	Date now = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        sdf.format(now);
  return sdf.format(now);
}
	
	public static String getPayloadId (){
		int randNum = new Double( Math.random() * 100000 ).intValue();
		return getTimestamp()+"."+randNum;
	}
}
