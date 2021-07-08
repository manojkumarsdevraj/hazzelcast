package com.unilog.punchout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.unilog.database.ConnectionManager;
import com.unilog.punchout.model.PunchoutModel;
import com.unilog.security.SecureData;

public class PunchoutDAO {
	
	public PunchoutModel getPunchoutConfiguration(int customerId){
		PunchoutModel punchoutModel=null;
		Connection conn = null;
		 PreparedStatement pstmt= null;
		 ResultSet rs=null;
		 SecureData getPassword = new SecureData();
		 try{
			 conn=ConnectionManager.getDBConnection();
			 String str="select * from PUNCHOUT_USER where CUSTOMER_ID=?";
			 pstmt=conn.prepareStatement(str);
			 pstmt.setInt(1,customerId);
			 
			 rs=pstmt.executeQuery();
			 if(rs.next()){
				 punchoutModel=new PunchoutModel();
				 punchoutModel.setId(rs.getInt("ID"));
				 punchoutModel.setUserId(rs.getInt("USER_ID"));
				 punchoutModel.setUsername(rs.getString("USER_NAME"));
				 punchoutModel.setPassword(getPassword.validatePassword(rs.getString("PASSWORD")));
				 punchoutModel.setPunchoutType(rs.getString("PUNCHOUT_TYPE"));
				 punchoutModel.setProcurementSystem(rs.getString("PROCUREMENT_SYSTEM"));
				 punchoutModel.setNetworkId(rs.getString("NETWORK_ID"));
				
			 }
			 
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }finally {
			 ConnectionManager.closeDBResultSet(rs);
			 ConnectionManager.closeDBPreparedStatement(pstmt);
			 ConnectionManager.closeDBConnection(conn);
		 }
		 
		return punchoutModel;
	}
	
	public int insertUpdate(String username,String password,int siteId,int customerId,String punchoutType,String procurementSystem,String networkId,int user_id,int configId){
	 Connection conn = null;
	 PreparedStatement pstmt = null;
	 int i=1;
	 
	 try{
		 if(configId>0){
		 i = insertATRec(configId);
		 }
		 if(i > 0){
			 conn = ConnectionManager.getDBConnection();
			 String sql="Insert into PUNCHOUT_USER (ID,USER_NAME,PASSWORD,SITE_ID,CUSTOMER_ID,PUNCHOUT_TYPE,PROCUREMENT_SYSTEM,NETWORK_ID,USER_ID,UPDATED_DATETIME,CREATED_DATETIME) VALUES (PUNCHOUT_USER_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE)";
			 if(configId > 0){
			 sql = "UPDATE PUNCHOUT_USER SET USER_NAME = ? ,PASSWORD = ?,SITE_ID = ?,CUSTOMER_ID = ?,PUNCHOUT_TYPE = ?,PROCUREMENT_SYSTEM = ?,NETWORK_ID = ?,USER_ID = ?,UPDATED_DATETIME = SYSDATE WHERE ID = ?";
			 }
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, username);
			 pstmt.setString(2, SecureData.getsecurePassword(password));
			 pstmt.setInt(3, siteId);
			 pstmt.setInt(4, customerId);
			 pstmt.setString(5, punchoutType);		
			 pstmt.setString(6, procurementSystem);
			 pstmt.setString(7, networkId);
			 pstmt.setInt(8,user_id);
			 if(configId > 0 ){
				 pstmt.setInt(9,configId);
			 }
			 i=pstmt.executeUpdate();
		 }
		
	 }catch(Exception e){
		 e.printStackTrace();
		 if(e.getMessage().contains("unique constraint")){
				
				return -1; 
			}

			return -2;
	 }finally {
	
		 ConnectionManager.closeDBPreparedStatement(pstmt);
		 ConnectionManager.closeDBConnection(conn);
	 }
	 return 1;
	}
	
	public int insertATRec(int configId){

		 Connection conn = null;
		 PreparedStatement pstmt = null;
		 int i=-1;
		 
		 try{
			 conn = ConnectionManager.getDBConnection();
			 String sql="Insert into AT_PUNCHOUT_USER (ID,USER_NAME,PASSWORD,SITE_ID,CUSTOMER_ID,PUNCHOUT_TYPE,PROCUREMENT_SYSTEM,NETWORK_ID,USER_ID,UPDATED_DATETIME,CREATED_DATETIME) SELECT ID,USER_NAME,PASSWORD,SITE_ID,CUSTOMER_ID,PUNCHOUT_TYPE,PROCUREMENT_SYSTEM,NETWORK_ID,USER_ID,UPDATED_DATETIME,CREATED_DATETIME FROM PUNCHOUT_USER WHERE ID=?";
			 
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setInt(1,configId);
			 i=pstmt.executeUpdate();
		 }catch(Exception e){
			 e.printStackTrace();
			 if(e.getMessage().contains("unique constraint")){
					
					return -1; 
				}

				return -2;
		 }finally {
		
			 ConnectionManager.closeDBPreparedStatement(pstmt);
			 ConnectionManager.closeDBConnection(conn);
		 }
		 return i;
	}
	
}
