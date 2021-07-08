package com.unilog.searchconfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.velocity.app.VelocityEngine;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.ULLog;

public class SearchConfigDAO {
	
	
	public static ArrayList<SearchConfigModel> getDefaultConfigData(){

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = null;
		Connection conn = null;
		ArrayList<SearchConfigModel> defaultConfigList = new ArrayList<>();
		SearchConfigModel defaultConfig = null;

		try {

			strQuery = "select * from SEARCH_FIELDS_VIEW";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				defaultConfig = new SearchConfigModel();
				defaultConfig.setFieldName(rs.getString("FIELD_NAME"));
				defaultConfig.setFilterable(rs.getString("FILTER_ENABLED"));
				defaultConfig.setId(rs.getInt("ID"));
				defaultConfig.setQueryBoost(rs.getDouble("QUERY_BOOST"));
				defaultConfig.setSearchable(rs.getString("SEARCH_ENABLED"));
				defaultConfig.setFieldType(rs.getString("FIELD_TYPE"));
				
				defaultConfigList.add(defaultConfig);
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return defaultConfigList;
	}
	
	public static void deleteConfig(int configId){


		PreparedStatement pstmt = null;
		String strQuery = null;
		Connection conn = null;
		ArrayList<SearchConfigModel> defaultConfigList = new ArrayList<>();
		SearchConfigModel defaultConfig = null;

		try {
			strQuery = "DELETE from SOLR_SEARCH_SETTINGS WHERE CONFIG_ID = ?";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			pstmt.setInt(1, configId);
			int i = pstmt.executeUpdate();
			ConnectionManager.closeDBPreparedStatement(pstmt);
			strQuery = "DELETE from SEARCH_CONFIG WHERE ID = ?";
			pstmt = conn.prepareStatement(strQuery);
			pstmt.setInt(1, configId);
			i = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		
	
	}
	
	public static ArrayList<SearchConfigModel> getConfigData(int configId){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = null;
		Connection conn = null;
		ArrayList<SearchConfigModel> defaultConfigList = new ArrayList<>();
		SearchConfigModel defaultConfig = null;

		try {

			strQuery = "select SFV.FIELD_NAME,SSS.FILTER_ENABLED,SSS.QUERY_BOOST,SSS.SEARCH_ENABLED,SFV.FIELD_TYPE from SEARCH_FIELDS_VIEW SFV, SOLR_SEARCH_SETTINGS SSS WHERE  SSS.FIELD_NAME(+) = SFV.FIELD_NAME AND SSS.CONFIG_ID(+) = ?";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			pstmt.setInt(1, configId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				defaultConfig = new SearchConfigModel();
				defaultConfig.setFieldName(rs.getString("FIELD_NAME"));
				defaultConfig.setFilterable(rs.getString("FILTER_ENABLED"));
				//defaultConfig.setId(rs.getInt("ID"));
				defaultConfig.setQueryBoost(rs.getDouble("QUERY_BOOST"));
				defaultConfig.setSearchable(rs.getString("SEARCH_ENABLED"));
				defaultConfig.setFieldType(rs.getString("FIELD_TYPE"));
				
				defaultConfigList.add(defaultConfig);
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return defaultConfigList;
	}
	
	
	public static ArrayList<SearchConfigListModel> getConfigList(int siteId){

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = null;
		Connection conn = null;
		ArrayList<SearchConfigListModel> configList = new ArrayList<>();
		SearchConfigListModel defaultConfig = null;

		try {

			strQuery = "select * from SEARCH_CONFIG WHERE SITE_ID = ?";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			pstmt.setInt(1, siteId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				defaultConfig = new SearchConfigListModel();
				defaultConfig.setConfigName(rs.getString("CONFIG_NAME"));
				defaultConfig.setId(rs.getInt("ID"));
				defaultConfig.setSiteId(rs.getInt("SITE_ID"));
				defaultConfig.setActive(rs.getString("ACTIVE"));
				configList.add(defaultConfig);
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return configList;
	}
	
	public static LinkedHashMap<String, Object> insertSearchConfig(String configName,int siteId,int userId,List<SearchConfigModel> configList){

		int configId = 0;
		LinkedHashMap<String, Object> responseObj = new LinkedHashMap<>();
		PreparedStatement pstmt = null;
		String strQuery = null;
		Connection conn = null;
		try {
			configId= CommonDBQuery.getSequenceId("SEARCH_CONFIG_ID_SEQ");	
			strQuery = "INSERT INTO SEARCH_CONFIG(ID,CONFIG_NAME,SITE_ID,USER_EDITED,UPDATED_DATETIME) VALUES(?,?,?,?,SYSDATE)";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			pstmt.setInt(1, configId);
			pstmt.setString(2, configName);
			pstmt.setInt(3, siteId);
			pstmt.setInt(4, userId);
			int i = pstmt.executeUpdate();
			if(i>0){
				int res = insertConfigValues(configId, userId);
				if(res > 0){
					updateConfig(configList, configId);
					responseObj.put("configId", configId);
					responseObj.put("error", "Saved Successfully");
				}else{
					responseObj.put("configId", 0);
					responseObj.put("error", "Something went wrong.");
				}
				
				
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			responseObj.put("configId", 0);
			responseObj.put("error", e.getMessage());
			e.printStackTrace();
			
		} finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return responseObj;
	
	}
	
	public static int insertConfigValues(int configId,int userId){

		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		String strQuery = null;
		String strQuery1 = null;
		Connection conn = null;
		int i =0;
		try {
			strQuery = "MERGE INTO SOLR_SEARCH_SETTINGS a USING ( select FIELD_NAME,'default' FIELD_TYPE,FILTER_ENABLED,SEARCH_ENABLED, QUERY_BOOST FROM SEARCH_FIELDS) b ON (a.CONFIG_ID=? AND a.FIELD_NAME = b.FIELD_NAME) WHEN MATCHED THEN UPDATE SET a.FIELD_TYPE = b.FIELD_TYPE WHEN NOT MATCHED THEN INSERT (a.ID, a.CONFIG_ID, a.FIELD_NAME, a.FIELD_TYPE, a.FILTER_ENABLED, a.SEARCH_ENABLED, a.QUERY_BOOST, a.USER_EDITED, a.UPDATED_DATETIME) VALUES (SOLR_SEARCH_SETTINGS_ID_SEQ.NEXTVAL, ?, b.FIELD_NAME, b.FIELD_TYPE, b.FILTER_ENABLED, b.SEARCH_ENABLED, b.QUERY_BOOST, ?, SYSDATE)";
			
			strQuery1 = "MERGE INTO SOLR_SEARCH_SETTINGS a USING ( select FIELD_NAME,'custom' FIELD_TYPE FROM CUSTOM_FIELDS WHERE DATA_ENTITY='ITEM' AND FIELD_DATA_TYPE!='TBL' AND PARENT_FIELD_ID=0) b ON (a.CONFIG_ID=? AND a.FIELD_NAME = b.FIELD_NAME) WHEN MATCHED THEN UPDATE SET a.FIELD_TYPE = b.FIELD_TYPE WHEN NOT MATCHED THEN INSERT (a.ID, a.CONFIG_ID, a.FIELD_NAME, a.FIELD_TYPE, a.FILTER_ENABLED, a.SEARCH_ENABLED, a.QUERY_BOOST, a.USER_EDITED, a.UPDATED_DATETIME) VALUES (SOLR_SEARCH_SETTINGS_ID_SEQ.NEXTVAL, ?, b.FIELD_NAME, 'custom', 'N', 'N', 0, ?, SYSDATE)";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			pstmt.setInt(1, configId);
			pstmt.setInt(2, configId);
			pstmt.setInt(3, userId);
			i = pstmt.executeUpdate();
			
			
			pstmt1 = conn.prepareStatement(strQuery1);
			pstmt1.setInt(1, configId);
			pstmt1.setInt(2, configId);
			pstmt1.setInt(3, userId);
			i = pstmt1.executeUpdate();
			
			

		} catch (Exception e) {
			e.printStackTrace();
			i = 0;
		} finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBPreparedStatement(pstmt1);
			ConnectionManager.closeDBConnection(conn);
		}	
		return i;
	}
	
	public static ArrayList<SearchConfigModel> getCustomFields(){

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = null;
		Connection conn = null;
		ArrayList<SearchConfigModel> defaultConfigList = new ArrayList<>();
		SearchConfigModel defaultConfig = null;

		try {

			strQuery = "SELECT * FROM CUSTOM_FIELDS WHERE DATA_ENTITY='ITEM' AND FIELD_DATA_TYPE!='TBL' AND PARENT_FIELD_ID=0";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				defaultConfig = new SearchConfigModel();
				defaultConfig.setFieldName(rs.getString("FIELD_NAME"));
				defaultConfig.setFieldType("custom");
				defaultConfigList.add(defaultConfig);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return defaultConfigList;
	}
	
	public static LinkedHashMap<String, Object> updateConfig(List<SearchConfigModel> configList,int configId){

		Connection  conn = null;
		PreparedStatement pstmt = null;

		int count = -1;
		LinkedHashMap<String, Object> responseObj = new LinkedHashMap<>();
		try{
			conn = ConnectionManager.getDBConnection();
			String query = "UPDATE SOLR_SEARCH_SETTINGS SET SEARCH_ENABLED = ?, QUERY_BOOST=? WHERE FIELD_NAME=? AND CONFIG_ID=?";
			pstmt = conn.prepareStatement(query);
			
			for(SearchConfigModel configData : configList) {
				pstmt.setString(1, configData.getSearchable());
				pstmt.setDouble(2, configData.getQueryBoost());
				pstmt.setString(3, configData.getFieldName());
				pstmt.setInt(4, configId);
				pstmt.addBatch();
			}
			int[] res = pstmt.executeBatch();
			count = 1;
			System.out.println(res);
			for(int i:res){
				System.out.println(i);
			}
			responseObj.put("configId", configId);
			responseObj.put("error", "Saved Successfully");
		}
		catch(SQLException e)
		{
			responseObj.put("configId", 0);
			responseObj.put("error", e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return responseObj;
	
	}
	
	public static LinkedHashMap<String, Object> mergeConfig(List<SearchConfigModel> configList,int configId,int userId){

		Connection  conn = null;
		PreparedStatement pstmt = null;

		int count = -1;
		LinkedHashMap<String, Object> responseObj = new LinkedHashMap<>();
		try{
			conn = ConnectionManager.getDBConnection();
			String strQuery = "MERGE INTO SOLR_SEARCH_SETTINGS a USING ( select ? FIELD_NAME, ? FIELD_TYPE,? FILTER_ENABLED,? SEARCH_ENABLED, ? QUERY_BOOST FROM DUAL) b ON (a.CONFIG_ID=? AND a.FIELD_NAME = b.FIELD_NAME) WHEN MATCHED THEN UPDATE SET a.SEARCH_ENABLED = b.SEARCH_ENABLED,a.QUERY_BOOST = b.QUERY_BOOST WHEN NOT MATCHED THEN INSERT (a.ID, a.CONFIG_ID, a.FIELD_NAME, a.FIELD_TYPE, a.FILTER_ENABLED, a.SEARCH_ENABLED, a.QUERY_BOOST, a.USER_EDITED, a.UPDATED_DATETIME) VALUES (SOLR_SEARCH_SETTINGS_ID_SEQ.NEXTVAL, ?, b.FIELD_NAME, b.FIELD_TYPE, b.FILTER_ENABLED, b.SEARCH_ENABLED, b.QUERY_BOOST, ?, SYSDATE)";
			
			pstmt = conn.prepareStatement(strQuery);
			
			for(SearchConfigModel configData : configList) {
				pstmt.setString(1, configData.getFieldName());
				pstmt.setString(2, configData.getFieldType());
				pstmt.setString(3, configData.getFilterable());
				pstmt.setString(4, configData.getSearchable());
				pstmt.setDouble(5, configData.getQueryBoost());
				pstmt.setInt(6, configId);
				pstmt.setInt(7, configId);
				pstmt.setInt(8, userId);
				pstmt.addBatch();
			}
			int[] res = pstmt.executeBatch();
			count = 1;
			System.out.println(res);
			for(int i:res){
				System.out.println(i);
			}
			responseObj.put("configId", configId);
			responseObj.put("error", "Saved Successfully");
		}
		catch(SQLException e)
		{
			responseObj.put("configId", 0);
			responseObj.put("error", e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return responseObj;
	
	}
	
	public static LinkedHashMap<String, Object> mergeFilter(List<FilterAttributeModel> filterList){

		Connection  conn = null;
		PreparedStatement pstmt = null;

		int count = -1;
		LinkedHashMap<String, Object> responseObj = new LinkedHashMap<>();
		LinkedHashMap<String, String> globalFilterObj = new LinkedHashMap<>();
		globalFilterObj.put("category", "Y");
		globalFilterObj.put("brand", "Y");
		globalFilterObj.put("manufacturerName", "Y");
		
		try{
			conn = ConnectionManager.getDBConnection();
			String strQuery = "MERGE INTO DEFAULT_FILTER_ATTRIBUTE a USING ( select ? ATTR_NAME, ? STATUS,? DISPLAY_SEQ FROM DUAL) b ON (a.ATTR_NAME = b.ATTR_NAME) WHEN MATCHED THEN UPDATE SET a.STATUS = b.STATUS, a.DISPLAY_SEQ = b.DISPLAY_SEQ WHEN NOT MATCHED THEN INSERT (a.ATTR_NAME, a.STATUS, a.DISPLAY_SEQ) VALUES (b.ATTR_NAME, b.STATUS, b.DISPLAY_SEQ)";
			
			pstmt = conn.prepareStatement(strQuery);
			
			for(FilterAttributeModel data : filterList) {
				String attrName = data.getAttrName();
				if(globalFilterObj.get(attrName)==null){
					attrName = "attr_"+  data.getAttrName();
				}
				pstmt.setString(1, attrName);
				pstmt.setString(2, data.getStatus());
				pstmt.setInt(3, data.getDisplaySeq());
				
				pstmt.addBatch();
			}
			int[] res = pstmt.executeBatch();
			count = 1;
			System.out.println(res);
			for(int i:res){
				System.out.println(i);
			}
			responseObj.put("id", 1);
			responseObj.put("error", "Saved Successfully");
		}
		catch(SQLException e)
		{
			responseObj.put("configId", 0);
			responseObj.put("error", e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return responseObj;
	
	}
	
	public static LinkedHashMap<String, Object> inActiveFilter(){
		Connection  conn = null;
		PreparedStatement pstmt = null;

		int count = -1;
		LinkedHashMap<String, Object> responseObj = new LinkedHashMap<>();
		try{
			conn = ConnectionManager.getDBConnection();
			String query = "UPDATE DEFAULT_FILTER_ATTRIBUTE SET STATUS = ?";  
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "D");
			int res = pstmt.executeUpdate();
			System.out.println("Updated active");
			count = 1;
			responseObj.put("error", "Saved Successfully");
		}
		catch(SQLException e)
		{
			responseObj.put("configId", 0);
			responseObj.put("error", e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return responseObj;
	
	}
	
	public static LinkedHashMap<String, Object> deleteFilter(){
		Connection  conn = null;
		PreparedStatement pstmt = null;

		int count = -1;
		LinkedHashMap<String, Object> responseObj = new LinkedHashMap<>();
		try{
			conn = ConnectionManager.getDBConnection();
			String query = "DELETE FROM DEFAULT_FILTER_ATTRIBUTE WHERE STATUS = ?";  
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "D");
			int res = pstmt.executeUpdate();
			System.out.println("Updated active");
			count = 1;
			responseObj.put("error", "Saved Successfully");
		}
		catch(SQLException e)
		{
			responseObj.put("configId", 0);
			responseObj.put("error", e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return responseObj;
	
	}

	
	public static LinkedHashMap<String, Object> activeConfig(int configId,int siteId){


		Connection  conn = null;
		PreparedStatement pstmt = null;

		int count = -1;
		LinkedHashMap<String, Object> responseObj = new LinkedHashMap<>();
		try{
			conn = ConnectionManager.getDBConnection();
			String query = "UPDATE SEARCH_CONFIG SET ACTIVE = CASE ID WHEN ? THEN CASE ACTIVE WHEN 'Y' THEN 'N' ELSE 'Y' END ELSE 'N' END WHERE SITE_ID=?";  
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, configId);
			pstmt.setInt(2, siteId);
			int res = pstmt.executeUpdate();
			System.out.println("Updated active");
			count = 1;
			responseObj.put("configId", configId);
			responseObj.put("error", "Saved Successfully");
		}
		catch(SQLException e)
		{
			responseObj.put("configId", 0);
			responseObj.put("error", e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return responseObj;
	
	}


	
	public static String getQueryFields(int configId){

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String strQuery = null;
		Connection conn = null;
		String queryFields = null;

		try {
			strQuery = "SELECT LISTAGG(CASE FIELD_TYPE WHEN 'custom' THEN 'customSearchable_'||FIELD_NAME ELSE FIELD_NAME END||'^'||to_char(QUERY_BOOST,'FM0D90'), ' ') WITHIN GROUP (ORDER BY FIELD_NAME) \"QUERY\" FROM SOLR_SEARCH_SETTINGS SSS,SEARCH_CONFIG SC WHERE SC.ACTIVE='Y' AND SSS.CONFIG_ID = SC.ID AND SEARCH_ENABLED = 'Y' AND SITE_ID = ?";
			if(configId>0){
				strQuery = "SELECT LISTAGG(CASE FIELD_TYPE WHEN 'custom' THEN 'customSearchable_'||FIELD_NAME ELSE FIELD_NAME END||'^'||to_char(QUERY_BOOST,'FM0D90'), ' ') WITHIN GROUP (ORDER BY FIELD_NAME) \"QUERY\" FROM SOLR_SEARCH_SETTINGS SSS,SEARCH_CONFIG SC WHERE SC.ID = ? AND SSS.CONFIG_ID = SC.ID AND SEARCH_ENABLED = 'Y' AND SITE_ID = ?";
			}
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(strQuery);
			if(configId > 0){
				pstmt.setInt(1, configId);
				pstmt.setInt(2, CommonDBQuery.getGlobalSiteId());
			}else{
				pstmt.setInt(1, CommonDBQuery.getGlobalSiteId());
			}
			 
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				queryFields = rs.getString("QUERY");
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return queryFields;
	}
	
	public static LinkedHashMap<String, Integer> getSelectedFilterAttributes(){

		System.out.println("Getting Default Facet");
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		LinkedHashMap<String, Integer> selectedAttributes = new LinkedHashMap<String, Integer>();
		try {

			conn = ConnectionManager.getDBConnection();
			stmt = conn.createStatement();
			String sql = "SELECT ATTR_NAME,DISPLAY_SEQ FROM DEFAULT_FILTER_ATTRIBUTE WHERE STATUS!='D'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				selectedAttributes.put(rs.getString("ATTR_NAME"), rs.getInt("DISPLAY_SEQ"));
				}
		} catch (SQLException e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} catch (Exception e) {
			ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
	return selectedAttributes;
	}

}
