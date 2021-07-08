package com.unilog.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import com.unilog.utility.CommonUtility;

public class GlobalDBConfig {
	
	public static boolean getBoolValueForKey(String strKey)
	{
		boolean bVal = false;
		
		String strVal = getValueForKey(strKey);

		if(strVal != null && (strVal.equalsIgnoreCase("1") || strVal.equalsIgnoreCase("Y")))
			bVal = true;

		return bVal;
		
	}
	
	public static int getIntValueForKey( String strKey)
	{
		int iVal = 0;
		
		String strVal = getValueForKey(strKey);
		try
		{
			if(strVal != null)
				iVal = CommonUtility.validateNumber(strVal);
		}
		catch(NumberFormatException e)
		{
			
		}
		
		return iVal;
		
	}
	public static String getValueForKey(String strKey)
	{
		String strVal = null;
		String strSQL = getValueForKeySQL(strKey);
		Connection oraConn = null;
		
		try {
			oraConn = (Connection) ConnectionManager.getDBConnection();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ResultSet oTable = getAdminData(oraConn,strSQL);

		try
		{
			if(oTable != null && oTable.next())
			{
				strVal = oTable.getString(1);
				oTable.close();
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBConnection(oraConn);
		}

		return strVal;
	}
	
	public static ResultSet getAllDBConfigValues(Connection oraConn)
	{
		String strSQL = getAllConfigValuesSQL();

		ResultSet oTable = getAdminData(oraConn,strSQL);
		

		return oTable;
	}
	
	public static ResultSet getDBConfigValuesForKey(Connection oraConn, String key)
	{
		String strSQL = getConfigValuesForKeySQL(key);
		ResultSet oTable = getAdminData(oraConn,strSQL);

		return oTable;
	}
	
	public static ResultSet getDBConfigValuesForCategory(String category)
	{
		String strSQL = getConfigValuesForCategorySQL(category);
		
Connection oraConn = null;
		
		try {
			oraConn = (Connection) ConnectionManager.getDBConnection();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ResultSet oTable = getAdminData(oraConn,strSQL);

		return oTable;
	}
	
	public static ResultSet getDBConfigValuesForCategory(String category, String paramType)
	{
		String strSQL = getConfigValuesForCategorySQL(category, paramType);
		
Connection oraConn = null;
		
		try {
			oraConn = (Connection) ConnectionManager.getDBConnection();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ResultSet oTable = getAdminData(oraConn,strSQL);

		return oTable;
	}
	
	public static ResultSet getSysParamCategoryList()
	{
		String strSQL = getSysParamCategoryListSQL();
		
Connection oraConn = null;
		
		try {
			oraConn = (Connection) ConnectionManager.getDBConnection();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ResultSet oTable = getAdminData(oraConn,strSQL);

		return oTable;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return Returns -999999 if there was an exception or the actual
	 * number of rows affected
	 */
	public static int saveSysParam(String key, String value)
	{
    	// Need to escape the value here
		String escValue = escape(value);
		String strSQL = getSaveSysParamSQL(key,escValue);
		
		int count = runInsertUpdateDeleteSQLReturnCount(strSQL);
	
		return count;
	}
	
	/**
	 * 
	 * @param paramList
	 * @return Returns the number of rows affected or -999999 if there was any exception 
	 */
	public static int saveSysParams(ArrayList<UNKeyValue> paramList)
	{
		int ret = 0;
		Connection oraConn = null;
		Statement stmt=null;
	

		try
		{	
			oraConn = (Connection) ConnectionManager.getDBConnection();
			 stmt = oraConn.createStatement();
			
			for(int i = 0 ; i < paramList.size() ; i++)
			{
				UNKeyValue kv = paramList.get(i);
				String escValue = escape(kv.getValue());
		    	
				// Need to escape the values here
				String strSQL = getSaveSysParamSQL(kv.getKey(),escValue);
				
				int ret1 = stmt.executeUpdate(strSQL);
					
				ret += ret1;    
					
			}	
			
			oraConn.commit();

			
		} catch(SQLException e)
		{
			try
			{
				if(oraConn!=null)
				oraConn.rollback();
			} catch(SQLException e1)
			{
				
			}
			
			
			e.printStackTrace();
			return -999999;
		}
		finally
		{
			
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(oraConn);
			
		}
	
		return ret;
	}
	
	public static ResultSet getValuesForKeys(ArrayList<String> arrKeys)
	{
		String strKeys = getStringListFromArray(arrKeys);
		String strSQL = getValuesForKeysSQL(strKeys);
		
Connection oraConn = null;
		
		try {
			oraConn = (Connection) ConnectionManager.getDBConnection();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ResultSet oTable = getAdminData(oraConn,strSQL);

		return oTable;
	}
	
	public static Hashtable<String,String> getValuesForKeyList(ArrayList<String> arrKeys)
	{
		String strKeys = getStringListFromArray(arrKeys);
		String strSQL = getValuesForKeysSQL(strKeys);
Connection oraConn = null;
		
		try {
			oraConn = (Connection) ConnectionManager.getDBConnection();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}		
		ResultSet rs = getAdminData(oraConn,strSQL);
		Hashtable<String,String> keyValTable = new Hashtable<String,String>();
		
    	try
    	{
    		if(rs != null)
    		{
		    	while(rs.next())
		    	{
		    		String val = rs.getString(2);
		    		if(val == null)
		    			val = "";
		    		keyValTable.put(rs.getString(1),val);	
		    	}
		    	
		    	rs.close();
    		}
	    	
    	}catch(Exception e)
    	{
    		
    	}
    	finally
    	{
    		ConnectionManager.closeDBConnection(oraConn);
    	
    	}
		

		return keyValTable;
	}
	
	public static Properties getPropertiesForKeyList(ArrayList<String> arrKeys)
	{
		String strKeys = getStringListFromArray(arrKeys);
		String strSQL = getValuesForKeysSQL(strKeys);
		Connection oraConn=null;
		try {
			 oraConn = (Connection) ConnectionManager.getDBConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ResultSet rs = getAdminData(oraConn,strSQL);
		Properties properties = new Properties();
		
    	try
    	{
    		if(rs != null)
    		{
		    	while(rs.next())
		    	{
		    		String key = rs.getString(1);
		    		String val = rs.getString(2);
		    		if(val == null)
		    			val = "";
		    		properties.put(key,val);
		    		//
		    	}
		    	
		    	rs.close();
    		}
	    	
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally
		{
    		ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBConnection(oraConn);
		}
		

		return properties;
	}
	
	public static ResultSet getBusinessData(Connection oraConn,String strSQL)
	{
		ResultSet oTable = null;
		try
		{
			// Call the DB method to get the results
			oTable = runSQLReturnResultSet(oraConn,strSQL);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return oTable;
	}

	public static ResultSet getAdminData(Connection oraConn, String strSQL)
	{
		ResultSet oTable = null;
		try
		{
			// Call the DB method to get the results
			oTable = runSQLReturnResultSet(oraConn,strSQL);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return oTable;
	}
	
	/**
	 * 
	 * @param db = OracleConnection.adminDB or OracleConnection.businessDB
	 * @param sql
	 * @return
	 */
	public static int runSQLReturnInt(String db,String sql)
	{
		int ret = 0;
		Connection oraConn=null;
		Statement stmt=null;
		ResultSet resultSet=null;
		try
		{
			oraConn = (Connection) ConnectionManager.getDBConnection();
			
			
			stmt = oraConn.createStatement();
			resultSet = stmt.executeQuery(sql);
			
		    if (resultSet.next()) {
		         ret = resultSet.getInt(1);
		    }
		    stmt.close();
			
		}catch(SQLException e)
		{
			
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(resultSet);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(oraConn);
		}
		
		
		return ret;
	}
	
	/**
	 * 
	 * @param db = OracleConnection.adminDB or OracleConnection.businessDB
	 * @param sql
	 * @return
	 */
	public static String runSQLReturnString(String db,String sql)
	{
		String ret = "";
		Connection oraConn=null;
		Statement stmt=null;
		ResultSet resultSet=null;
		try
		{
			oraConn = (Connection) ConnectionManager.getDBConnection();
			
			
			stmt = oraConn.createStatement();
			resultSet = stmt.executeQuery(sql);
			
		    if (resultSet.next()) {
		         ret = resultSet.getString(1);
		    }
		    stmt.close();
			
		}catch(SQLException e)
		{
			
			e.printStackTrace();
		}finally
		{
			ConnectionManager.closeDBResultSet(resultSet);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(oraConn);
		}
		
		
		return ret;
	}
	
	/**
	 * 
	 * @param oraConn = OracleConnection.adminDB or OracleConnection.businessDB
	 * @param sql
	 * @return
	 */
	public static ResultSet runSQLReturnResultSet(Connection oraConn,String sql)
	{
		ResultSet rs = null;
		Statement stmt = null;
		try
		{	
			stmt = oraConn.createStatement();
			rs = stmt.executeQuery(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(stmt);
		}
		return rs;
	}
	
	/**
	 * Returns -999999 if there is an exception
	 * Else returns an integer count
	 * @param sql
	 * @return
	 */
	public static int runInsertUpdateDeleteSQLReturnCount(String sql)
	{
		int ret = -999999;
		Connection oraConn = null;
		Statement stmt = null;
		try
		{
			oraConn = (Connection) ConnectionManager.getDBConnection();
			
			stmt = oraConn.createStatement();
			ret = stmt.executeUpdate(sql);
			
		    stmt.close();
			
		}catch(SQLException e)
		{
			
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(oraConn);
		}
		
		return ret;
	}
	
	
	
	public static String getValuesForKeysSQL(String strKeys)
	{
		StringBuilder oSQL = new StringBuilder();
		oSQL.append("Select CONFIG_KEY, CONFIG_VALUE from UN_SYS_CONFIG ");
		oSQL.append(" Where CONFIG_KEY IN (").append(strKeys).append(") ");

		return oSQL.toString();
	}

	public static String getValueForKeySQL(String strKey)
	{
		StringBuilder oSQL = new StringBuilder();
		oSQL.append("Select CONFIG_VALUE from SYSTEM_PARAMETERS ");
		oSQL.append(" Where CONFIG_KEY = '").append(strKey).append("'");

		return oSQL.toString();
	}
	
	public static String getAllConfigValuesSQL()
	{
		StringBuilder oSQL = new StringBuilder();
		oSQL.append("Select CONFIG_KEY, CONFIG_VALUE,CATEGORY,DISPLAY_NAME,FIELD_TYPE,");
		oSQL.append(" OPTION_VALUES,DESCRIPTION,VALUE_TYPE,FIELD_WIDTH from UN_SYS_CONFIG order by CATEGORY ");

		return oSQL.toString();
	}
	
	public static String getConfigValuesForCategorySQL(String category, String paramType)
	{
		StringBuilder oSQL = new StringBuilder();
		oSQL.append("Select CONFIG_KEY, CONFIG_VALUE,CATEGORY,DISPLAY_NAME,FIELD_TYPE,");
		oSQL.append(" OPTION_VALUES,DESCRIPTION,VALUE_TYPE,FIELD_WIDTH from UN_SYS_CONFIG ");
		oSQL.append(" where CATEGORY = '").append(category).append("' AND FIELD_TYPE = '").append(paramType).append("'");
		oSQL.append(" order by CATEGORY ");

		return oSQL.toString();
	}
	
	public static String getConfigValuesForCategorySQL(String category)
	{
		StringBuilder oSQL = new StringBuilder();
		oSQL.append("Select CONFIG_KEY, CONFIG_VALUE,CATEGORY,DISPLAY_NAME,FIELD_TYPE,");
		oSQL.append(" OPTION_VALUES,DESCRIPTION,VALUE_TYPE,FIELD_WIDTH from UN_SYS_CONFIG ");
		oSQL.append(" where CATEGORY = '").append(category).append("' order by CATEGORY ");

		return oSQL.toString();
	}
	
	public static String getConfigValuesForKeySQL(String key)
	{
		StringBuilder oSQL = new StringBuilder();
		oSQL.append("Select CONFIG_KEY, CONFIG_VALUE,CATEGORY,DISPLAY_NAME,FIELD_TYPE,");
		oSQL.append(" OPTION_VALUES,DESCRIPTION,VALUE_TYPE,FIELD_WIDTH from UN_SYS_CONFIG ");
		oSQL.append(" where CONFIG_KEY = '").append(key).append("' ");

		return oSQL.toString();
	}
	
	public static String getSysParamCategoryListSQL()
	{
		StringBuilder oSQL = new StringBuilder();
		oSQL.append("Select distinct CATEGORY from UN_SYS_CONFIG order by CATEGORY ");

		return oSQL.toString();
	}
	
	public static String getSaveSysParamSQL(String key, String value)
	{
		StringBuilder oSQL = new StringBuilder();
		oSQL.append("update UN_SYS_CONFIG  set CONFIG_VALUE='").append(value).append("' ");
		oSQL.append(" where CONFIG_KEY = '").append(key).append("' ");

		return oSQL.toString();
	}
	
	public static String escape(String value)
	{
		return (value.replace("'","''"));
	}
	public static String getStringListFromArray(ArrayList<String> strList)
	{
		int count = strList.size();
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < count; i++)
		{
			if(i != 0)
				sb.append(",");
			
			sb.append("'").append(strList.get(i)).append("'");			
		}
		
		return sb.toString();
	}

}
