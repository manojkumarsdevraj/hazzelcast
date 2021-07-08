package com.unilog.rockwell;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.rockwell.model.BomModel;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;

public class RockwellDao {

	public static ProductsModel checkItemExistInCIMM(Connection conn, String partNumber, String subsetId) {
	    ProductsModel itemDetails = null;
	   
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = PropertyAction.SqlContainer.get("checkItemExistInCIMM");
	    try
	    {
	      
	      pstmt = conn.prepareStatement(sql);
	      pstmt.setString(1, CommonUtility.validateString(partNumber));
	      pstmt.setString(2, CommonUtility.validateString(partNumber));
	      pstmt.setInt(3, CommonUtility.validateNumber(subsetId));
	      rs = pstmt.executeQuery();
	      while (rs.next())
	      {
	        itemDetails = new ProductsModel();
	        itemDetails.setPartNumber(rs.getString("PART_NUMBER"));
	        itemDetails.setItemId(rs.getInt("ITEM_ID"));
	        itemDetails.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      ConnectionManager.closeDBPreparedStatement(pstmt);
	      ConnectionManager.closeDBResultSet(rs);
	    }

	    return itemDetails;
	  }
	
	
	  public static int insertNonCatalogItemToCart(Connection conn,BomModel itemData, String sessionId,int userId) {
		    int count = -1;
		    int siteId = 0;
		    if (CommonDBQuery.getGlobalSiteId() > 0) {
		      siteId = CommonDBQuery.getGlobalSiteId();
		    }
		    String NonCatalogItemId = CommonUtility.validateString((String)CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID"));
		    String NonCatalogItemIdentifier = CommonUtility.validateString((String)CommonDBQuery.getSystemParamtersList().get("CART_NCI_IDENTIFIER"));
		    String sql = PropertyAction.SqlContainer.get("insertNonCatalogItemToCart");
		    
		    PreparedStatement pstmt = null;
		    try
		    {
		      
		      pstmt = conn.prepareStatement(sql);
		      pstmt.setString(1, itemData.getProduct());
		      pstmt.setString(2, null);
		      pstmt.setInt(3, itemData.getQty());
		      pstmt.setDouble(4, itemData.getListPrice());//------- remove if not required and pe
		      pstmt.setString(5, null);
		      pstmt.setString(6, itemData.getDescription());
		      pstmt.setString(7, "Cart");
		      pstmt.setString(8, sessionId);
		      pstmt.setInt(9, userId);
		      pstmt.setInt(10, siteId);
		      pstmt.setString(11, NonCatalogItemId);
		      pstmt.setString(12, NonCatalogItemIdentifier);
		      count = pstmt.executeUpdate();
		    }
		    catch (SQLException e)
		    {
		      e.printStackTrace();
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
		    finally
		    {
		      ConnectionManager.closeDBPreparedStatement(pstmt);
		      
		    }
		    return count;
		  }
	  
	  
		public static ArrayList<Integer> checkItemExistInCart(Connection conn, String partNumber, int userId,String sessionId){
			ArrayList<Integer> cartId = new ArrayList<>();
			String sql = PropertyAction.SqlContainer.get("checkItemExistInCart");
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = conn.prepareStatement(sql);
				 pstmt.setString(1, CommonUtility.validateString(partNumber));
			      pstmt.setInt(2, userId);
			      pstmt.setString(3, CommonUtility.validateString(sessionId));
				
				
				rs = pstmt.executeQuery();
				if(rs.next())
				{
					cartId.add(rs.getInt("CART_ID"));
					cartId.add(rs.getInt("QTY"));
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
			finally
			{
				ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
			}
			return cartId;
		}

}
