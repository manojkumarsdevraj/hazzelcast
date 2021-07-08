package com.unilog.utility.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import com.unilog.database.ConnectionManager;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.utility.model.ValueListModel;

public class ValueListDao {

	public static ArrayList<ValueListModel> getValueList(String valueListName){
		ArrayList<ValueListModel> valueList = new ArrayList<ValueListModel>();
		Connection  conn = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		try{
			ValueListModel valueListData = null;
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getValueList");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, valueListName);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				valueListData = new ValueListModel();
				valueListData.setValueListId(rs.getInt("VALUE_DATA_ID"));
				valueListData.setListValue(rs.getString("LIST_VALUE"));
				valueList.add(valueListData);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return valueList;
	}
}
