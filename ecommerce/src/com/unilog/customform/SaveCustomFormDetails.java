package com.unilog.customform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.utility.CommonUtility;

public class SaveCustomFormDetails {
	public int saveToDataBase(LinkedHashMap<String, String> parameters,String formName) {
		Connection conn = null;
		int count = 0;
		try {
			conn = ConnectionManager.getDBConnection();
			int formId = saveFormDetails(conn, formName);
			for (int i=0; i < parameters.size(); i++) {
				String fieldName = (String)parameters.keySet().toArray()[i];
				String fieldValue = (String)parameters.values().toArray()[i];
				if(CommonUtility.validateString(fieldName).toLowerCase().contains("password") || CommonUtility.validateString(fieldName).toLowerCase().contains("pwd")) {
					fieldValue = "********";
				}
				count = count + saveFormFieldDetails(conn, formId, fieldName, fieldValue);
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBConnection(conn);
		}
		return count;
	}
	
	public int saveFormDetails(Connection conn,String formName)
	{
		String sql = "INSERT INTO CIMM_FORMS (CIMM_FORM_ID,CIMM_FORM_NAME) VALUES(?,?)";
		int formId = CommonDBQuery.getSequenceId("CIMM_FORM_SEQ");
		PreparedStatement pstmt = null;
		//ResultSet rs = null;
		int count = 0;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, formId);
			pstmt.setString(2, formName);
			count = pstmt.executeUpdate();
			System.out.println("insert Count = "+count);
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
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
		}
		return formId;
		
	}
	
	public int saveFormFieldDetails(Connection conn,int formId,String fieldName,String fieldValue)
	{
		String sql = "INSERT INTO CIMM_FORM_CONTENTS(CIMM_FORM_ROW_ID,CIMM_FORM_ID,FIELD_NAME,FIELD_VALUE,UPDATED_DATETIME) VALUES(CIMM_FORM_CONTENT_SEQ.NEXTVAL,?,?,?,SYSDATE)";
		PreparedStatement pstmt = null;
		//ResultSet rs = null;
		int count = 0;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, formId);
			pstmt.setString(2, fieldName);
			pstmt.setString(3, fieldValue);
			count = pstmt.executeUpdate();
			System.out.println("insert form field Count = "+count);
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
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
		}
		return formId;
		
	}
	
	
	public  LinkedHashMap<String,String> getNotificationDetails(String notificationName){
		LinkedHashMap<String,String> notification  = new LinkedHashMap<String, String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//int count = 0;
		try {
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT * FROM CIMM_NOTIFICATIONS where NOTIFICATION_NAME=?";

			try
			{
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, notificationName);
				rs = pstmt.executeQuery();
				while(rs.next()){
					
					if(rs.getString("DESCRIPTION") != null) {
						notification.put("DESCRIPTION", rs.getString("DESCRIPTION"));
					}
					if(CommonUtility.validateString(rs.getString("TO_EMAIL")).length()>0 && !CommonUtility.validateString(rs.getString("TO_EMAIL")).equalsIgnoreCase("null")){
						notification.put("TO_EMAIL", rs.getString("TO_EMAIL"));
					}
					if(CommonUtility.validateString(rs.getString("CC_EMAIL")).length()>0 && !CommonUtility.validateString(rs.getString("CC_EMAIL")).equalsIgnoreCase("null")){
						notification.put("CC_EMAIL", rs.getString("CC_EMAIL"));
					}
					if(CommonUtility.validateString(rs.getString("BCC_EMAIL")).length()>0 && !CommonUtility.validateString(rs.getString("BCC_EMAIL")).equalsIgnoreCase("null")){
						notification.put("BCC_EMAIL", rs.getString("BCC_EMAIL"));
					}
					if(CommonUtility.validateString(rs.getString("FROM_EMAIL")).length()>0 && !CommonUtility.validateString(rs.getString("FROM_EMAIL")).equalsIgnoreCase("null")) {
						notification.put("FROM_EMAIL", rs.getString("FROM_EMAIL"));
					}
					if(CommonUtility.validateString(rs.getString("CMS_STATIC_PAGE_ID")).length()>0 && !CommonUtility.validateString(rs.getString("CMS_STATIC_PAGE_ID")).equalsIgnoreCase("null")) {
                        notification.put("CMS_STATIC_PAGE_ID", rs.getString("CMS_STATIC_PAGE_ID"));
					}
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);
			
		}
		return notification;
	}
}
