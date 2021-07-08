package com.unilog.products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.ULLog;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.propertiesutility.PropertyAction;

public class ManufacturerDataDao {
	
	public static ArrayList<MenuAndBannersModal> getShopByManufacturerData(int subsetId)
	{	
		ArrayList<MenuAndBannersModal> brandMenu=null;
		
		Connection  conn = null;
		PreparedStatement preStat=null;
		ResultSet rs = null;
		String strQuery = new String();
		brandMenu = new ArrayList<MenuAndBannersModal>();

			 
			    try {
		            conn = ConnectionManager.getDBConnection();
		            strQuery=PropertyAction.SqlContainer.get("manufacturerListQuery");
		        	ULLog.sqlTrace(strQuery.toString());
		        	preStat = conn.prepareStatement(strQuery);
		        	preStat.setInt(1, subsetId);
					rs = preStat.executeQuery();
					
					while(rs.next())
					{			  
						  MenuAndBannersModal manufacturerListVal = new MenuAndBannersModal();
						  String itemUrl = rs.getString("BRAND_NAME").toString().trim();
						  itemUrl = itemUrl.replaceAll(" ","-");
			              manufacturerListVal.setItemUrl(itemUrl);
						  manufacturerListVal.setBrandId(rs.getInt("BRAND_ID"));
						  manufacturerListVal.setBrandName(rs.getString("BRAND_NAME"));
						  brandMenu.add(manufacturerListVal);
					}	
		        }catch(SQLException e){
		        	ULLog.error("Error in getShopByManufacturerData():ERROR:" +  e.fillInStackTrace());
			        e.printStackTrace();
			    }catch(Exception e){
			    	ULLog.errorTrace(e.fillInStackTrace());
			        e.printStackTrace();
			    }finally {	    	 
			    	ConnectionManager.closeDBPreparedStatement(preStat);
			    	ConnectionManager.closeDBConnection(conn);	
			    }
			    return brandMenu;
		}

		public static ArrayList<ProductsModel> getManufacturerBrandsList( int brandid) {
		
		ArrayList<ProductsModel> manufacturerBrandlist = null;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
	
		try
		{
			 conn = ConnectionManager.getDBConnection();
			System.out.println("manufacturerid in getmanufacturer method::::::"+brandid);
			manufacturerBrandlist = new ArrayList<ProductsModel>();
			String sql = CommonDBQuery.getSystemParamtersList().get("manufacturerBrandsList");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (brandid));
			ULLog.sqlTrace("Brand ID =" + brandid);
			ULLog.sqlTrace(sql);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				ProductsModel manufacturerBrandVal = new ProductsModel();		
				manufacturerBrandVal.setItemId(rs.getInt("ITEM_ID"));
				manufacturerBrandVal.setPartNumber(rs.getString("PART_NUMBER"));
				manufacturerBrandVal.setProductName(rs.getString("PRODUCT_NAME"));
				manufacturerBrandVal.setShortDesc(rs.getString("SHORT_DESC"));
				manufacturerBrandVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				manufacturerBrandVal.setPrice(rs.getDouble("LIST_PRICE"));
				manufacturerBrandlist.add(manufacturerBrandVal);
			}
		}
		catch(SQLException e)
		{
			 ULLog.error("Error in getManufacturerBrandsList():ERROR:" +  e.fillInStackTrace());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			 ULLog.errorTrace(e.fillInStackTrace());
			e.printStackTrace();
		}
		finally
		{
				ConnectionManager.closeDBPreparedStatement(pstmt);	
				ConnectionManager.closeDBConnection(conn);	

		}
		return manufacturerBrandlist;
	}
	
}
