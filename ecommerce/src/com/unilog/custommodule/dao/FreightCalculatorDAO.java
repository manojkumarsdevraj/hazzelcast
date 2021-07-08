package com.unilog.custommodule.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.unilog.custommodule.model.FreightCalculatorModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

public class FreightCalculatorDAO {

	public static FreightCalculatorModel getFreightValueByTotal(FreightCalculatorModel freightInput)
	{
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    String calculateDefaultFreight =  CommonDBQuery.getSystemParamtersList().get("GET_DEFAULT_FREIGHT");
	    FreightCalculatorModel freightValue = null;
		try
		{
			//WH.WAREHOUSE_CODE=? AND FR.WAREHOUSE_ID = WH.WAREHOUSE_ID AND UPPER(SHIPVIA_CODE)=? AND MINORDER_VALUE <= ? AND MAXORDER_VALUE >= ?
			String sql = PropertyAction.SqlContainer.get("getFreightValue");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, freightInput.getWareHouseCode());
			pstmt.setString(2, CommonUtility.validateString(freightInput.getShipVia()).toUpperCase());
			pstmt.setDouble(3, freightInput.getCartTotal());
			pstmt.setDouble(4, freightInput.getCartTotal());
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				freightValue = new FreightCalculatorModel();
				freightValue.setFreightValue(rs.getDouble("FREIGHT"));
				freightValue.setOverSize(rs.getString("OVERSIZE_INCLUDED"));
				calculateDefaultFreight = "N";
			}
			
			if(CommonUtility.validateString(calculateDefaultFreight).trim().equalsIgnoreCase("Y"))
			{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, CommonDBQuery.getSystemParamtersList().get("DEFAULT_WAREHOUSE_CODE"));
				pstmt.setString(2, CommonUtility.validateString(freightInput.getShipVia()).toUpperCase());
				pstmt.setDouble(3, freightInput.getCartTotal());
				pstmt.setDouble(4, freightInput.getCartTotal());
				
				rs = pstmt.executeQuery();
				if(rs.next())
				{
					freightValue = new FreightCalculatorModel();
					freightValue.setFreightValue(rs.getDouble("FREIGHT"));
					freightValue.setOverSize(rs.getString("OVERSIZE_INCLUDED"));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		finally
		{
			
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return freightValue;
	}
	
}
