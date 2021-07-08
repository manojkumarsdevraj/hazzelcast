package com.unilog.utility.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.unilog.database.ConnectionManager;
import com.unilog.utility.model.Themes;

public class ThemesDao {

	public static Themes getScheduledTheme(String currentThemeDate){
		System.out.println("Getting Scheduled Theme");
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		Connection conn = null;
		Themes themes = null;
		try{
			String sql = "SELECT * FROM THEMES WHERE STATUS = 'A' AND SCHEDULED = 'Y' AND START_DATE <= ? AND END_DATE >= ?";
			conn =  ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, currentThemeDate);
			pstmt.setString(2, currentThemeDate);
			rs = pstmt.executeQuery();
			if (rs.next()){
				themes = new Themes();
				themes.setId(rs.getInt("ID"));
				themes.setCssName(rs.getString("CSS_NAME"));
				themes.setThemeName(rs.getString("THEME_NAME"));
				themes.setStartDate(rs.getString("START_DATE"));
				themes.setEndDate(rs.getString("END_DATE"));
				themes.setStatus(rs.getString("STATUS"));
				themes.setScheduled(rs.getString("SCHEDULED"));
			}	
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);;
			ConnectionManager.closeDBConnection(conn);
		}	
		return themes;
	}
	
	public static int updateAppliedThemeToSystemParameter(String themeName){
		System.out.println("Updating Theme to System Parameters");
		PreparedStatement pstmt = null;
		Connection conn = null;
		int count = -1;
		try{
			String sql = "MERGE INTO system_parameters a USING (SELECT 'ThemeFileName' AS CONFIG_KEY,'THEME GENERATOR' AS CATEGORY,? AS CONFIG_VALUE,'To save applied theme' AS DESCRIPTION,null AS DISPLAY_NAME,'ITA' AS FIELD_TYPE,null AS OPTION_VALUES,null AS VALUE_TYPE,0 AS FIELD_WIDTH,SYSDATE AS UPDATED_DATETIME,1 AS USER_EDITED,'Y' AS DISPLAY,null AS VALIDATION_VALUE_LIST_ID,21 AS SITE_ID,1000 AS SYSTEM_PARAMETER_ID,null AS WFL_PHASE_ID FROM DUAL) b ON (a.CONFIG_KEY = b.CONFIG_KEY) WHEN MATCHED THEN UPDATE SET a.CONFIG_VALUE = b.CONFIG_VALUE, a.UPDATED_DATETIME = b.UPDATED_DATETIME WHEN NOT MATCHED THEN INSERT (a.CONFIG_KEY, a.CATEGORY, a.CONFIG_VALUE, a.DESCRIPTION, a.DISPLAY_NAME, a.FIELD_TYPE, a.OPTION_VALUES, a.VALUE_TYPE, a.FIELD_WIDTH, a.UPDATED_DATETIME, a.USER_EDITED, a.DISPLAY, a.VALIDATION_VALUE_LIST_ID, a.SITE_ID, a.SYSTEM_PARAMETER_ID, a.WFL_PHASE_ID) VALUES (b.CONFIG_KEY, b.CATEGORY, b.CONFIG_VALUE, b.DESCRIPTION, b.DISPLAY_NAME, b.FIELD_TYPE, b.OPTION_VALUES, b.VALUE_TYPE, b.FIELD_WIDTH, b.UPDATED_DATETIME, b.USER_EDITED, b.DISPLAY, b.VALIDATION_VALUE_LIST_ID, b.SITE_ID, b.SYSTEM_PARAMETER_ID, b.WFL_PHASE_ID)";
			conn =  ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, themeName);
			count = pstmt.executeUpdate();
			System.out.println("Theme update Status to System Parameters: "+count);
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}	
		return count;
	}
}
