package com.unilog.customfields;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.resource.adapter.jdbc.WrappedConnection;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import com.google.gson.Gson;
import com.unilog.database.ConnectionManager;
import com.unilog.products.ProductsModel;
import com.unilog.users.ShipVia;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.utility.CustomTableModel;

public class CustomFieldDAO {
	
	
	public String getCustomFieldTableDetailsInJson(CustomModel customFieldModel){
		   
		   CallableStatement stmt = null;
	       ResultSet itemData = null;
	       ResultSet resultCount = null;
	       Connection conn = null;
	       String jasonResult = "";
	       int recordCount = -1;
	       
	       try {
	         conn = ConnectionManager.getDBConnection();
	      	 
	         //stmt = conn.prepareCall("{call READ_TABLE_CUSTOM_FIELD_VALUES(?,?,?,?,?,?,?,?,?)}");
	         stmt = conn.prepareCall("{call READ_CUSTOM_FIELD_VALUES(?,?,?,?,?,?,?,?,?)}");
	      	 
	         stmt.setString(1, CommonUtility.validateString(customFieldModel.getCustomFieldEntityType()));
	      	 stmt.setString(2, CommonUtility.validateString(customFieldModel.getCustomFieldName()));
	      	 stmt.setInt(3, customFieldModel.getCustomFieldEntityId());
	      	 stmt.setInt(4, customFieldModel.getCustomFieldPageNumber());
	      	 stmt.setInt(5, customFieldModel.getCustomFieldItemPerPage());
	      	 stmt.setString(6, customFieldModel.getCustomFieldResultType());
	      	 stmt.registerOutParameter(7,OracleTypes.CURSOR);
	      	 stmt.registerOutParameter(8,OracleTypes.CURSOR);
	   	 	 stmt.registerOutParameter(9,OracleTypes.VARCHAR);
	      	 stmt.execute();
	      	 
	      	 System.out.println("READ_TABLE_CUSTOM_FIELD_VALUES @ getCustomFieldTableDetails");
	      	 
		        	 if(stmt.getObject(7)!=null){
		        		 itemData = (ResultSet) stmt.getObject(7);
		        		 
		        		 if(itemData!=null && itemData.next()){
		        			 jasonResult = itemData.getString("DATA");
		        			 System.out.println("jasonResult @ getCustomFieldTableDetailsInJson : "+jasonResult);
		        		 }
			        	
		        	 }
		        	if(stmt.getObject(8)!=null){
		        		 resultCount = (ResultSet) stmt.getObject(8);
		        		 while(resultCount!=null && resultCount.next())
			        	 {
			        		recordCount =  resultCount.getInt("TOTAL_ROWS");
			        		System.out.println("recordCount : "+recordCount);
			        	 }
		        	 }
		        	 /*
		        	 if(stmt.getObject(9)!=null){
		        		 resultFlag =  (String) stmt.getObject(9);
		        		 System.out.println("resultFlag : "+resultFlag);
		        	 }*/
	     }catch (Exception e) {
	          e.printStackTrace();
	     }finally{
	    	 ConnectionManager.closeDBResultSet(itemData);
	    	 ConnectionManager.closeDBResultSet(resultCount);
		     ConnectionManager.closeDBStatement(stmt);
		     ConnectionManager.closeDBConnection(conn);
	     }
			return jasonResult;
		}
	
	public static ArrayList<ShipVia> getShipViaDetailsFromCustomFieldTable(CustomModel customFieldModel){
		long startTimer = CommonUtility.startTimeDispaly();
		   CallableStatement stmt = null;
	       ResultSet itemData = null;
	       ResultSet itemDataTwo = null;
	       Connection conn = null;
	       ArrayList<ShipVia> shipViaList = new ArrayList<ShipVia>();
	       
	       try{
	    	 //conn =  getDBConnection();  
	         conn = ConnectionManager.getDBConnection();
	      	 stmt = conn.prepareCall("{call READ_CUSTOM_FIELD_VALUES(?,?,?,?,?,?,?,?,?)}");
	      	 stmt.setString(1, CommonUtility.validateString(customFieldModel.getCustomFieldEntityType()));
	      	 stmt.setString(2, CommonUtility.validateString(customFieldModel.getCustomFieldName()));
	      	 stmt.setInt(3, customFieldModel.getCustomFieldEntityId());
	      	 stmt.setInt(4, customFieldModel.getCustomFieldPageNumber());
	      	 stmt.setInt(5, customFieldModel.getCustomFieldItemPerPage());
	      	 stmt.setString(6, CommonUtility.validateString(customFieldModel.getCustomFieldResultType()));
	      	 stmt.registerOutParameter(7,OracleTypes.CURSOR);
	      	 stmt.registerOutParameter(8,OracleTypes.CURSOR);
	   	 	 stmt.registerOutParameter(9,OracleTypes.VARCHAR);
	      	 stmt.execute();
	      	 
	      	 System.out.println("READ_CUSTOM_FIELD_VALUES @ getCustomFieldTableDetails");
	      	 
		        	 if(stmt.getObject(7)!=null){
		        		 itemData = (ResultSet) stmt.getObject(7);
		        		 
		        		 if(itemData!=null){
		        			 LinkedHashMap<String, String> columnNameList = new LinkedHashMap<String, String>();
		        			 ResultSetMetaData rsMetaData = itemData.getMetaData();
		        			 int numberOfColumns = rsMetaData.getColumnCount();
		        			 
		        			 for (int i = 1; i < numberOfColumns + 1; i++) {
		        			     columnNameList.put(CommonUtility.validateString(rsMetaData.getColumnName(i)), "Y");
		        			 }
		        			 
		        			 while(itemData.next()){
		        				 ShipVia shipViaModel = new ShipVia();
		        				 if(columnNameList.get("SHIP_VIA_CODE") !=null){
		        					 shipViaModel.setShipViaID(itemData.getString("SHIP_VIA_CODE"));
		        				 }
								 if(columnNameList.get("SHIP_VIA_NAME") !=null){
									 shipViaModel.setDescription(itemData.getString("SHIP_VIA_NAME"));	        					 
								 }
								 if(columnNameList.get("SHIP_CODE") !=null){
									 shipViaModel.setShipCode(itemData.getString("SHIP_CODE"));
									 shipViaModel.setServiceCode(CommonUtility.validateNumber(itemData.getString("SHIP_CODE")));
								 }
								 if(columnNameList.get("SHIPPING_COST") !=null){
									 shipViaModel.setShipCost(itemData.getDouble("SHIPPING_COST"));
								 }
								 if(columnNameList.get("SITE_NAME") !=null){
									 shipViaModel.setShipViaWebSiteName(itemData.getString("SITE_NAME"));
								 }
								 if(columnNameList.get("DISPLAY_SEQ") !=null){
									 shipViaModel.setDisplaySeq(CommonUtility.validateNumber(itemData.getString("DISPLAY_SEQ")));
								 }
								 if(columnNameList.get("ERP_CODE") !=null){
									 shipViaModel.setExternalSystemCode(itemData.getString("ERP_CODE"));
								 }
								 if(columnNameList.get("SERVICE_PROVIDER") !=null){
									 shipViaModel.setServiceProvider(itemData.getString("SERVICE_PROVIDER"));
								 }
								 if(columnNameList.get("ACCOUNT_NUMBER") !=null){
									 shipViaModel.setAccountNumber(itemData.getString("ACCOUNT_NUMBER"));
								 }
								 shipViaModel.setShipViaWebSiteId(customFieldModel.getCustomFieldEntityId());
								 shipViaList.add(shipViaModel);
		        			 }
		        		 }
		        	 }
		        	 
		        	 if(stmt.getObject(8)!=null){
		        		 itemDataTwo = (ResultSet) stmt.getObject(8);
		        	 }
	     }catch (Exception e) {
	           e.printStackTrace();
	     }finally{
	    	ConnectionManager.closeDBResultSet(itemData);
	    	ConnectionManager.closeDBResultSet(itemDataTwo);
		    ConnectionManager.closeDBStatement(stmt);
		    ConnectionManager.closeDBConnection(conn);
	     }
	     CommonUtility.endTimeAndDiffrenceDisplay(startTimer);  
		 return shipViaList;
		}
	
	public static String getCustomFieldTableByFieldValue(CustomModel customFieldModel){
		long startTimer = CommonUtility.startTimeDispaly();
		   CallableStatement stmt = null;
	       ResultSet itemData = null;
	       ResultSet itemDataTwo = null;
	       Connection conn = null;
	       String jsonResult = null;
	      
	       String columnNames[] = customFieldModel.getCustomFieldNameList();
	       String columnValues[] = customFieldModel.getCustomFieldValueList();
	       try{
	    	   //conn =  getDBConnection();  
	    	   conn = ConnectionManager.getDBConnection();
	    	  
	    	   Connection oracleConnection = null;
	     		if (conn instanceof org.jboss.resource.adapter.jdbc.WrappedConnection) {
	 				WrappedConnection wc = (WrappedConnection) conn;
	 				// with getUnderlying connection method , cast it to Oracle
	 				// Connection
	 				oracleConnection = wc.getUnderlyingConnection();
	 			}
				
	    	   ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("STRING_ARRAY", oracleConnection );
	     		ARRAY columnNameArray = new ARRAY( descriptor, oracleConnection, columnNames );
	     		ARRAY columnValuesArray  = new ARRAY( descriptor, oracleConnection, columnValues );
	    	
	        
	      	 stmt = conn.prepareCall("{call READ_ENTITY_ON_CF_VALUE(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
	      	 stmt.setString(1, CommonUtility.validateString(customFieldModel.getCustomFieldEntityType()));
	      	 stmt.setString(2, CommonUtility.validateString(customFieldModel.getCustomFieldName()));
	      	 stmt.setInt(3, customFieldModel.getCustomFieldEntityId());
	      	 stmt.setInt(4, customFieldModel.getCustomFieldPageNumber());
	      	 stmt.setInt(5, customFieldModel.getCustomFieldItemPerPage());
	      	 stmt.setString(6, CommonUtility.validateString(customFieldModel.getCustomFieldResultType()));
	      	 stmt.setArray(7, columnNameArray);
	      	 stmt.setArray(8, columnValuesArray);
	      	 stmt.setString(9, CommonUtility.validateString(customFieldModel.getSourceTableName()));
	      	 stmt.setString(10, CommonUtility.validateString(customFieldModel.getSourceColumnName()));
	      	 stmt.setString(11, CustomFieldUtility.getInstance().buildJsonField(CommonUtility.validateString(customFieldModel.getSourceColumnName())));
	      	 stmt.registerOutParameter(12,OracleTypes.CURSOR);
	      	 stmt.registerOutParameter(13,OracleTypes.CURSOR);
	   	 	 stmt.registerOutParameter(14,OracleTypes.VARCHAR);
	      	 stmt.execute();
	      	 
	      	 System.out.println("READ_CUSTOM_FIELD_VALUES @ getCustomFieldTableDetails");
	      	 
	      	 if(stmt.getObject(12)!=null){
        		 itemData = (ResultSet) stmt.getObject(12);
        		 
        		 if(itemData!=null && itemData.next()){
        			 jsonResult = itemData.getString("DATA");
        			 System.out.println("jasonResult @ getCustomFieldTableDetailsInJson : "+jsonResult);
        		 }
	        	
        	 }
	      	 
	      	if(stmt.getObject(13)!=null){
	      		itemDataTwo= (ResultSet) stmt.getObject(13);
	      	}
	     }catch (Exception e) {
	           e.printStackTrace();
	     }finally{
	    	/* try {
	    		 itemData.close();
	    		 stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	    	ConnectionManager.closeDBResultSet(itemData);
	    	ConnectionManager.closeDBResultSet(itemDataTwo);
		    ConnectionManager.closeDBStatement(stmt);
		    ConnectionManager.closeDBConnection(conn);
	     }
	     CommonUtility.endTimeAndDiffrenceDisplay(startTimer);  
		 return jsonResult;
		}
	
	
	public static String getCustomFieldTableFormBuilder(CustomModel customFieldModel){
		   
		   CallableStatement stmt = null;
	       ResultSet itemData = null;
	       ResultSet itemDataTwo = null;
	       Connection conn = null;
	       String jsonResult = null;
	      
	       
	       try{
	    	 //conn =  getDBConnection();  
	    	 conn = ConnectionManager.getDBConnection();
	      	 stmt = conn.prepareCall("{call READ_CUSTOM_FIELD_VALUES_SORT(?,?,?,?,?,?,?,?,?,?)}");
	      	 stmt.setString(1, CommonUtility.validateString(customFieldModel.getCustomFieldEntityType()));
	      	 stmt.setString(2, CommonUtility.validateString(customFieldModel.getCustomFieldName()));
	      	 stmt.setInt(3, customFieldModel.getCustomFieldEntityId());
	      	 stmt.setInt(4, customFieldModel.getCustomFieldPageNumber());
	      	 stmt.setInt(5, customFieldModel.getCustomFieldItemPerPage());
	      	 stmt.setString(6, CommonUtility.validateString(customFieldModel.getCustomFieldResultType()));
	      	 stmt.setString(7, CommonUtility.validateString(customFieldModel.getSortOn()));
	      	 stmt.registerOutParameter(8,OracleTypes.CURSOR);
	      	 stmt.registerOutParameter(9,OracleTypes.CURSOR);
	   	 	 stmt.registerOutParameter(10,OracleTypes.VARCHAR);
	      	 stmt.execute();
	      	 
	      	 System.out.println("READ_CUSTOM_FIELD_VALUES @ Frombuilder");
	      	 
	      	 if(stmt.getObject(8)!=null){
     		 itemData = (ResultSet) stmt.getObject(8);
	     		 if(itemData!=null && itemData.next()){
	     			 jsonResult = itemData.getString("DATA");
	     			 System.out.println("jasonResult @ getCustomFieldTableDetailsInJson : "+jsonResult);
	     		 }
	      	 }
	      	if(stmt.getObject(9)!=null){
	      		itemDataTwo = (ResultSet) stmt.getObject(9);
	      	}
	     }catch (Exception e) {
	           e.printStackTrace();
	     }finally{
	    	ConnectionManager.closeDBResultSet(itemData);
	    	ConnectionManager.closeDBResultSet(itemDataTwo);
		    ConnectionManager.closeDBStatement(stmt);
		    ConnectionManager.closeDBConnection(conn);
	     }
	       
		 return jsonResult;
		}
	
	
	
	/*public static ArrayList<ProductsModel> getCustomFieldTableDetails(CustomModel customFieldModel){
	   
	   CallableStatement stmt = null;
       ResultSet itemData = null;
       ResultSet resultCount = null;
       Connection conn = null;
       String resultFlag = "";
       int recordCount = -1;
       
       try {
    	   conn =  getDBConnection();
           //conn = ConnectionManager.getDBConnection();
      	 
         stmt = conn.prepareCall("{call READ_CUSTOM_FIELD_VALUES(?,?,?,?,?,?,?,?,?)}");
      	 
         stmt.setString(1, CommonUtility.validateString(customFieldModel.getCustomFieldEntityType()));
      	 stmt.setString(2, CommonUtility.validateString(customFieldModel.getCustomFieldName()));
      	 stmt.setInt(3, customFieldModel.getCustomFieldEntityId());
      	 stmt.setInt(4, customFieldModel.getCustomFieldPageNumber());
      	 stmt.setInt(5, customFieldModel.getCustomFieldItemPerPage());
      	 stmt.setString(6, customFieldModel.getCustomFieldResultType());
      	 stmt.registerOutParameter(7,OracleTypes.CURSOR);
      	 stmt.registerOutParameter(8,OracleTypes.CURSOR);
   	 	 stmt.registerOutParameter(9,OracleTypes.VARCHAR);
      	
      	 stmt.execute();
      	 
      	 System.out.println("READ_TABLE_CUSTOM_FIELD_VALUES @ getCustomFieldTableDetails");
      	 
	        	 if(stmt.getObject(7)!=null){
	        		 itemData = (ResultSet) stmt.getObject(7);
	        		 
	        		 if(itemData!=null){
		        		
	        			 while(itemData.next()){
	        				 ShipVia shipVia = new ShipVia(); 
	        				 
	        				 
	        				 
	        				 shipVia.setDescription(itemData.getString("SHIP_VIA_NAME"));
	        				 
	        				 
	        			 }
	        			 
	        			 
	        			  ResultSetMetaData metaData = itemData.getMetaData();
			        	 int columnCount = metaData.getColumnCount();
			        	 for(int i=0; i<columnCount; i++){
			        		 String columnName = metaData.getColumnName(i);
			        		 
			        		 metaData.getColumnType(i);
			        		 
			        		 itemData.getObject(columnName);
			        	 }
			        	 
			        	 
	        		 }
		        	
	        	 }
	        	 
	        	 
	        	 if(stmt.getObject(8)!=null){
	        		 resultCount = (ResultSet) stmt.getObject(8);
	        		 while(resultCount!=null && resultCount.next())
		        	 {
		        		recordCount =  resultCount.getInt("TOTAL_ROWS");
		        		System.out.println("recordCount : "+recordCount);
		        	 }
	        	 }
	        	 
	        	 if(stmt.getObject(9)!=null){
	        		 resultFlag =  (String) stmt.getObject(9);
	        		 System.out.println("resultFlag : "+resultFlag);
	        	 }
	        	 
	        	 
	       
       }
       catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
     } catch (Exception e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
     }finally{
    	 		try {
					conn.close();
					stmt.close();
					if(itemData!=null)
					itemData.close();
					resultCount.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 ConnectionManager.closeDBResultSet(itemData);
	        	 ConnectionManager.closeDBStatement(stmt);
	        	 ConnectionManager.closeDBConnection(conn);
     }
		      return null;
	}*/

	/*public static void main(String[] args) {
		CustomModel customFieldModel = new CustomModel();
		CustomFieldUtility.getInstance().buildJsonField("BRAND_NAME,BRAND_IMAGE");
		customFieldModel.setCustomFieldEntityType("WEBSITE");
		customFieldModel.setCustomFieldName("SHIPVIA_COST_TABLE");
		customFieldModel.setCustomFieldEntityId(21);
		// ITEM - BRAND - WEBSITE
		getCustomFieldTableDetails(customFieldModel);
		
		
		customFieldModel = new CustomModel();
		customFieldModel.setCustomFieldEntityType("ITEM");
		customFieldModel.setCustomFieldName("ITEM_SHIPVIA_CODE");
		customFieldModel.setCustomFieldEntityId(807378);
		customFieldModel.setCustomFieldResultType("JSON");
		getCustomFieldTableDetailsInJson(customFieldModel);
		
		String[] customFieldNameList = new String[1];
		String[] customFieldValueList = new String[1];
		customFieldNameList[0] = "BRAND_NAME_LOOKUP";
		customFieldValueList[0] = "Y";
		String sourceTableName = "BRANDS";
		String sourceColumnName = "BRAND_NAME,BRAND_IMAGE";
		
		customFieldModel.setCustomFieldResultType("JSON");
		customFieldModel.setCustomFieldEntityType("BRAND");
		customFieldModel.setCustomFieldName("BRAND_MODEL_LOOKUP");
		
		customFieldModel.setCustomFieldNameList(customFieldNameList);
		
		customFieldModel.setCustomFieldValueList(customFieldValueList);
		
		customFieldModel.setSourceTableName(sourceTableName);
		
		customFieldModel.setSourceColumnName(sourceColumnName);
		
		String jsonObject = getCustomFieldTableByFieldValue(customFieldModel);
		jsonObject = "{\"customTableObject\":"+jsonObject+"}";
		Gson gson = new Gson();
		CustomTableModel output = gson.fromJson(jsonObject, CustomTableModel.class);
		List<CustomTable> out = output.getCustomTableObject();
		System.out.println(out.size());
		for(CustomTable ll:out){
			System.out.println(ll.getEntityId());
			List<Map<String, String>> tableDetail = ll.getTableDetails();
			Map<String, String> entityFieldValue = ll.getEntityFieldValue();
			if(entityFieldValue!=null && entityFieldValue.size()>0){
				for (Map.Entry<String, String> entry : entityFieldValue.entrySet()) 
		        {            
		            
		            System.out.println(entry.getKey()+" --------  "+entry.getValue());
		        }
			}
			for(Map<String, String> ss:tableDetail){
				System.out.println("Map Out : "+ss.get("Description"));
				for (Map.Entry<String, String> entry : ss.entrySet()) 
		        {            
		            
		            System.out.println(entry.getKey()+" --------  "+entry.getValue());
		        }
			}
		}
	}

	 public static Connection getDBConnection() throws SQLException {
			Connection con = null;
			try {
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:gorcl", "staging","stage123##");
				return con;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}*/

}
