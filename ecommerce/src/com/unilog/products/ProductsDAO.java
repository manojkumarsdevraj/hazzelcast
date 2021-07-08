package com.unilog.products;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.struts2.ServletActionContext;
import org.jboss.resource.adapter.jdbc.WrappedConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.service.ProductManagement;
import com.erp.service.impl.ProductManagementImpl;
import com.erp.service.model.ProductManagementModel;
import com.google.gson.Gson;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailUtility;
import com.unilog.defaults.ULLog;
import com.unilog.erpmanager.ERPProductsWrapper;
import com.unilog.logocustomization.LogoCustomization;
import com.unilog.logocustomization.LogoCustomizationModule;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.users.WarehouseModel;
import com.unilog.utility.CommonUtility;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;


public class ProductsDAO {
	private static final Logger logger = LoggerFactory.getLogger(ProductsDAO.class);
	public static HashMap<String, String> searKeywordBuilder(String keyWord)
  	{
		HashMap<String, String> keyWordBuilder = new HashMap<String, String>();
  		String orgKeyWord = keyWord.trim().toUpperCase();
  		String pnKeyWord = "";
  		keyWord = keyWord.trim().toUpperCase();
  		keyWord = keyWord.replaceAll("\\*", "%");
  		StringBuffer sb = new StringBuffer();
  		StringBuffer sb1 = new StringBuffer();
  		String appendAnd = "";
  		String appendAccum = "";
  		String appendString = "";
  		String appendStringAccum = "";
  		appendStringAccum = " ACCUM ";
  		appendString = " AND ";
  		String charStr = keyWord.trim();
  		try{
	  		charStr = charStr.replaceAll("�", "\"");
	  		charStr = charStr.replaceAll("�", "\"");
	  		charStr = charStr.replaceAll("�", "'");
	  		charStr = charStr.replaceAll("�", "'");
	  		String checkString[] = charStr.split("'");
	  		String[] reserveString = charStr.split("\\s+", -1);
	  		charStr = charStr.replaceAll("'", "''");
	  		charStr = charStr.toUpperCase();
	  		//charStr = charStr.replaceAll(" AND "," & ");
	  		String strArr[] ={",","&","=","\\?","\\(","\\)","\\[","\\]","-",";","~","\\|","\\$","!","\\*","''","\""};
	  		//String strArr[] = { "=", "\\|", "\\*", "\\^", "%", "-", ",", "~", ">", "<", "!", "_", ";","\\+","\\#","\\@","\\$","&" };
	  		String reserveWord[] = {"ABOUT","AND","BT","BTG","BTI","BTP","EQUIV","FUZZY","HASPATH","INPATH","MDATA","MINUS","NEAR","NOT","NT","NTG","NTI","NTP","OR","PT","RT","SQE","SYN","TR","TRSYN","TT","WITHIN"};
	  		for (int i = 0; i < strArr.length; i++) {
	  			charStr = charStr.replaceAll(strArr[i],"{"+strArr[i]+"}");
	  		}
	  		for (int j=0;j<reserveWord.length;j++)
	  		{
	  			for(int k=0;k<reserveString.length;k++)
	  			{
	  				if(reserveString[k].equalsIgnoreCase(reserveWord[j]))
	  				{
	  					charStr = charStr.replaceAll(reserveWord[j],"{"+reserveWord[j]+"}");
	  				}
	  			}
	  		}
	  		String searchKeyword = charStr;
	  		String searchString = "";
	  		String searchString1 = "";
	  		String newPnKeyWord = "";
	  		String pnsimKeyword = "";
	  		String pattern = "[^A-Za-z0-9]";
	  		String scrubbedKeyword = keyWord.replaceAll(pattern,"");
	
	  		boolean result1 = Character.isDigit(searchKeyword.charAt(0));
	  		boolean result2 = Character.isDigit(searchKeyword.charAt(searchKeyword.length()-1));
	  		System.out.println("At 0 : " + searchKeyword.charAt(0));
	  		System.out.println("At end : " + searchKeyword.charAt(searchKeyword.length()-1));
	  		if(searchKeyword.length()>2&&!scrubbedKeyword.equalsIgnoreCase("")&&scrubbedKeyword.length()>2)
	  		{
	  			if(searchKeyword.charAt(0)=='{' && searchKeyword.charAt(searchKeyword.length()-1)=='}'){
	  				searchString1 = searchKeyword+"|"+searchKeyword;
	  				pnsimKeyword = searchKeyword+"|%"+scrubbedKeyword+"%";
	  			}
	  			else if(searchKeyword.charAt(0)=='{'){
	  			searchString1 = searchKeyword+"|"+searchKeyword+"%";
	  			pnsimKeyword = searchKeyword+"%|%"+scrubbedKeyword+"%";
	  			}
	  			else if(searchKeyword.charAt(searchKeyword.length()-1)=='}'){
	  				searchString1 = searchKeyword+"|%"+searchKeyword;
	  				pnsimKeyword = "%"+searchKeyword+"|%"+scrubbedKeyword+"%";
	  			}
	  			else{
	  				if(result1){
	  				searchString1 = searchKeyword+"|"+searchKeyword+"%";
	  				if(searchKeyword.length()>2)
	  				{
	  					if(searchKeyword.charAt(searchKeyword.length()-2)=='}')
	  	  				{
	  	  					System.out.println("Present"+(searchKeyword.length()-1));
	  	  				pnsimKeyword = searchKeyword+"|%"+scrubbedKeyword+"%";
	  	  				}
	  					else
	  					{
	  						pnsimKeyword = searchKeyword+"%|%"+scrubbedKeyword+"%";
	  					}
	  				}
	  				else
	  				{
	  					pnsimKeyword = searchKeyword+"%|%"+scrubbedKeyword+"%";
	  				}
	  				
	  				}
	  				else if(checkString[checkString.length-1].length()==1){
	  					searchString1 = searchKeyword+"|%"+searchKeyword;
	  					pnsimKeyword = "%"+searchKeyword+"|%"+scrubbedKeyword+"%";
	  				}
	  				else{
	  					searchString1 = searchKeyword+"|%"+searchKeyword+"%";
	  					String s1 = "%"+searchKeyword+"%";
	  					String s2 = "%"+scrubbedKeyword+"%";
	  			if(s1.trim().equalsIgnoreCase(s2)){
	  				pnsimKeyword = "%"+searchKeyword+"%";
	  			}
	  			else
	  			{
	  				if(result2)
	  				pnsimKeyword = "%"+searchKeyword+"|%"+scrubbedKeyword+"%";
	  				else
	  					pnsimKeyword = "%"+searchKeyword+"%|%"+scrubbedKeyword+"%";
	  			}
	  				}	
	  			}
	  			String searchString2 = scrubbedKeyword+"|%"+scrubbedKeyword+"%";
	  			
	  			if(searchKeyword.trim().equals(scrubbedKeyword))
	  				newPnKeyWord = searchKeyword;
	  			else  			
	  			newPnKeyWord = searchKeyword+"|"+scrubbedKeyword;
	  			if(searchString1.equalsIgnoreCase(searchString2))
	  				searchString = searchString1;
	  			else
	  				searchString = searchString1 + "|" + searchString2;
	  		}
	  		else
	  		{
	  			searchString = searchKeyword;
	  			newPnKeyWord = searchKeyword;
	  			pnsimKeyword = searchKeyword;
	  		}
	  		pnKeyWord=searchString;
	  		
	  		String[] splitKeyword = searchKeyword.split("\\s+",-1);
	  		for(int i=0;i<splitKeyword.length;i++)
	  		{
	  			String alphaNumeric = splitKeyword[i];
	  			Pattern p = Pattern.compile("[^a-zA-Z]");//Pattern.compile("[^\\d]");
	  			Matcher m = p.matcher(alphaNumeric);
	  			boolean result = m.find();						        
	  			boolean deletedIllegalChars = false;
	  			while(result)
	  			{
	  				deletedIllegalChars = true;						          
	  				result = m.find();
	  			}
	  			if (!deletedIllegalChars) 
	  			{
	  				if(alphaNumeric.length()>2){
	  				sb.append("$").append(alphaNumeric).append(" ");
	  				sb1.append("$").append(alphaNumeric).append("% ");
	  				}
	  				else
	  				{
	  					sb.append("$").append(alphaNumeric).append(" ");
	  					sb1.append("$").append(alphaNumeric).append(" ");
	  				}
	  			}
	  			else
	  			{
	  				sb.append(alphaNumeric).append(" ");
	  				if(alphaNumeric.substring(0,1).equalsIgnoreCase("{"))
	  					sb1.append(alphaNumeric).append(" ");
	  				else if(alphaNumeric.charAt(alphaNumeric.length()-1)=='}')
	  					sb1.append(alphaNumeric).append(" ");
	  				else
	  				{
	  					System.out.println(checkString[checkString.length-1].length());
	  					if(alphaNumeric.length()>1 && checkString[checkString.length-1].length()>1){
	  						if(!result2)
	  						sb1.append(alphaNumeric.trim()).append("% ");
	  						else
	  							sb1.append(alphaNumeric.trim());
	  						
	  					}
	  					else
	  					{
	  						sb1.append(alphaNumeric.trim());
	  					}
	  				}
				}
			}
	  					
			String resultKeyword = sb.toString();
			String resultKeyword1 = sb1.toString();
			String resultAccum="";
			String resultAccum1="";
	  		appendAnd = resultKeyword.trim().replaceAll(" ", appendString);
	  		appendAccum = resultKeyword.trim().replaceAll(" ", appendStringAccum);
	  		resultAccum = appendAccum;
	  		resultKeyword = appendAnd;
	  		appendAnd = resultKeyword1.trim().replaceAll(" ", appendString);
	  		appendAccum = resultKeyword1.trim().replaceAll(" ", appendStringAccum);
	  		resultAccum1 = appendAccum;
	  		resultKeyword1 = appendAnd;
	  		System.out.println("New PnKeyword : " + newPnKeyWord);
	  		System.out.println("pnsimKeyword : " + pnsimKeyword);
	  		System.out.println("orgKeyWord : " + orgKeyWord);
	  		System.out.println("pnKeyWord : "+ pnKeyWord);
	  		System.out.println("fpKeyWord : "+ resultKeyword);
	  		System.out.println("bsKeyWord : " + resultKeyword1);
	  		System.out.println("accumKeyword1 : "+ resultAccum);
	  		System.out.println("accumKeyword2 : " + resultAccum1);
	  			
	  		keyWordBuilder.put("orgKeyWord", orgKeyWord);
	  		keyWordBuilder.put("pnKeyWord", pnKeyWord);
	  		keyWordBuilder.put("fpKeyWord", resultKeyword);
	  		keyWordBuilder.put("bsKeyWord", resultKeyword1);
	  		keyWordBuilder.put("accumKeyword1", resultAccum);
	  		keyWordBuilder.put("accumKeyword2", resultAccum1);
	  		keyWordBuilder.put("newPnKeyWord", newPnKeyWord);
	  		keyWordBuilder.put("pnsimKeyword", pnsimKeyword);
  		}
		 catch (Exception e) {
			
			 e.printStackTrace();
		}
  		return keyWordBuilder;
  	}
	public static HashMap<String, ArrayList<ProductsModel>> searchNavigation(String keyWord,int subsetId,int generalSubset,int fromRow,int toRow,String requestType,int psid,int npsid,int treeId,int levelNo,String attrFilterList,String brandId, String sessionId,String sortBy,String userName,String eclipseSessionId,String entityId,boolean isItem,int userId,String type,String viewFrequentlyPurcahsedOnly)
  	{
		/*String s = testArray();*/
		/*try {
			keyWord = URLEncoder.encode(keyWord, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		if(type==null)
			type="";
		boolean isSortBy = false;
		boolean massEnquiry = false;
		if(sortBy!=null && !sortBy.trim().equalsIgnoreCase(""))
		{
			isSortBy = true;
			if(sortBy.trim().equalsIgnoreCase("price_high"))
				sortBy = "NET_PRICE DESC";
			else if(sortBy.trim().equalsIgnoreCase("price_low"))
				sortBy = "NET_PRICE ASC";
			else if(sortBy.trim().equalsIgnoreCase("brand"))
				sortBy = "BRAND_NAME ASC";
			else if(sortBy.trim().equalsIgnoreCase("brand_asc"))
				sortBy = "BRAND_NAME ASC";
			else if(sortBy.trim().equalsIgnoreCase("brand_desc"))
				sortBy = "BRAND_NAME DESC";
			else if(sortBy.trim().equalsIgnoreCase("partnum_asc"))
				sortBy = "lpad(PART_NUMBER, 100) ASC";
			else if(sortBy.trim().equalsIgnoreCase("partnum_desc"))
				sortBy = "lpad(PART_NUMBER, 100) DESC";
			else if(sortBy.trim().equalsIgnoreCase("mfpartnum_asc"))
				sortBy = "NLSSORT(MANUFACTURER_PART_NUMBER, 'NLS_SORT = GENERIC_M') ASC";
			else if(sortBy.trim().equalsIgnoreCase("mfpartnum_desc"))
				sortBy = "NLSSORT(MANUFACTURER_PART_NUMBER, 'NLS_SORT = GENERIC_M') DESC";
			else if(sortBy.trim().equalsIgnoreCase("cust_partnum_asc"))
				sortBy = "COMPETITOR_PART_NUMBER ASC";
			else if(sortBy.trim().equalsIgnoreCase("cust_partnum_desc"))
				sortBy = "COMPETITOR_PART_NUMBER DESC";
			else if(sortBy.trim().equalsIgnoreCase("default"))
				isSortBy = false;
			
		}
		
		keyWord = keyWord.replaceAll("\\+", " ");
		
  		String orgKeyWord = "";
  		String pnKeyWord = "";
  		String fpKeyWord = "";
  		String bsKeyWord = "";
  		String accumKeyword1 = "";
  		String accumKeyword2 = "";
  		String pnsimKeyword = "";
  		String newPnKeyword = "";
  		
  		if(keyWord!=null && !keyWord.trim().equalsIgnoreCase(""))
  		{
	  		HashMap<String,String> keyWordBuilder = searKeywordBuilder(keyWord);
	  		orgKeyWord = keyWordBuilder.get("orgKeyWord");
	  		pnKeyWord = keyWordBuilder.get("pnKeyWord");
	  		fpKeyWord = keyWordBuilder.get("fpKeyWord");
	  		bsKeyWord = keyWordBuilder.get("bsKeyWord");
	  		accumKeyword1 = keyWordBuilder.get("accumKeyword1");
	  		accumKeyword2 = keyWordBuilder.get("accumKeyword2");
	  		pnsimKeyword = keyWordBuilder.get("pnsimKeyword");
	  		newPnKeyword = keyWordBuilder.get("newPnKeyWord");
  		}

  		 CallableStatement stmt = null;
  		 int popularSearchId = 0;
         Connection conn = null;
         ResultSet searchResult = null;
         ResultSet searchCategory = null;
         ResultSet searchFilters = null;
         HashMap<String, ArrayList<ProductsModel>> searchResultList = new HashMap<String, ArrayList<ProductsModel>>();
      
         try {
        	 conn = ConnectionManager.getDBConnection();
        	 System.out.println("Store Procedure Start : " + new Date());
	        	int FILTERATTRID[] = null;
	     		int FILTERATTRVALID[] = null;
	     		int FILTERTYPEID[] = null;
	     		int FILTERUOMID[] = null;
	     		
	     		if(attrFilterList!=null && !attrFilterList.trim().equalsIgnoreCase(""))
	     		{
	     			String attrArray[] = attrFilterList.split("~");
	     			FILTERATTRID = new int[attrArray.length];
	     			FILTERATTRVALID = new int[attrArray.length];
	     			FILTERTYPEID = new int[attrArray.length];
	     			FILTERUOMID = new int[attrArray.length];
	     			int attrArrayCount = 0;
	     			for(String aArray:attrArray)
	     			{
	     				
	     				String attrList[] = aArray.split(",");
	     				for(int i=0;i<attrList.length;i=i+4)
	     				{
	     					FILTERATTRID[attrArrayCount] = CommonUtility.validateNumber(attrList[i]);
	     					FILTERATTRVALID[attrArrayCount] =CommonUtility.validateNumber(attrList[i+1]);
	     					FILTERTYPEID[attrArrayCount] =CommonUtility.validateNumber(attrList[i+2]);
	     					FILTERUOMID[attrArrayCount] =CommonUtility.validateNumber(attrList[i+3]);
	     				}
	     				attrArrayCount++;
	     			}
	     		}
	     		 if(requestType.trim().equalsIgnoreCase("SHOP_BY_BRAND"))
 	        	 {
	     			
 					
 					if(attrFilterList!=null && !attrFilterList.trim().equalsIgnoreCase(""))
 		     		{
 		     			String attrArray[] = attrFilterList.split("~");
 		     			FILTERATTRID = new int[attrArray.length+1];
 		     			FILTERATTRVALID = new int[attrArray.length+1];
 		     			FILTERTYPEID = new int[attrArray.length+1];
 		     			FILTERUOMID = new int[attrArray.length+1];
 		     			int attrArrayCount = 1;
 		     			FILTERATTRID[0] = -2;
 	 					FILTERATTRVALID[0] =CommonUtility.validateNumber(brandId);
 	 					FILTERTYPEID[0] =2;
 	 					FILTERUOMID[0] =0;
 		     			for(String aArray:attrArray)
 		     			{
 		     				
 		     				String attrList[] = aArray.split(",");
 		     				for(int i=0;i<attrList.length;i=i+4)
 		     				{
 		     					FILTERATTRID[attrArrayCount] = CommonUtility.validateNumber(attrList[i]);
 		     					FILTERATTRVALID[attrArrayCount] =CommonUtility.validateNumber(attrList[i+1]);
 		     					FILTERTYPEID[attrArrayCount] =CommonUtility.validateNumber(attrList[i+2]);
 		     					FILTERUOMID[attrArrayCount] =CommonUtility.validateNumber(attrList[i+3]);
 		     				}
 		     				attrArrayCount++;
 		     			}
 		     		}
 					else
 					{
 						FILTERATTRID = new int[1];
 		     			FILTERATTRVALID = new int[1];
 		     			FILTERTYPEID = new int[1];
 		     			FILTERUOMID = new int[1];
 		     			FILTERATTRID[0] = -2;
 	 					FILTERATTRVALID[0] =CommonUtility.validateNumber(brandId);
 	 					FILTERTYPEID[0] =2;
 	 					FILTERUOMID[0] =0;
 					}
 	        	 }
	     		
	     		Connection oracleConnection = null;
	     		if (conn instanceof org.jboss.resource.adapter.jdbc.WrappedConnection) {
	 				WrappedConnection wc = (WrappedConnection) conn;
	 				// with getUnderlying connection method , cast it to Oracle
	 				// Connection
	 				oracleConnection = wc.getUnderlyingConnection();
	 			}
	     		
	     		ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor( "NUM_ARRAY", oracleConnection );
	     		ARRAY FILTER_ATTR_ID = new ARRAY( descriptor, oracleConnection, FILTERATTRID );
	     		ARRAY FILTER_ATTRVAL_ID = new ARRAY( descriptor, oracleConnection, FILTERATTRVALID );
	     		ARRAY FILTER_TYPE_ID = new ARRAY( descriptor, oracleConnection, FILTERTYPEID );
	     		ARRAY FILTER_ATTRUOM_ID = new ARRAY( descriptor, oracleConnection, FILTERUOMID );        	 
	     		
	     		ULLog.debug("requestType:--"+requestType);
     			ULLog.debug("orgKeyWord:--"+orgKeyWord);
     			ULLog.debug("pnKeyWord:--"+pnKeyWord);
     			ULLog.debug("fpKeyWord:--"+fpKeyWord);
     			ULLog.debug("bsKeyWord:--"+bsKeyWord);
     			ULLog.debug("accumKeyword1:--"+accumKeyword1);
     			ULLog.debug("accumKeyword2:--"+accumKeyword2);
     			ULLog.debug("psid:--"+psid);
     			ULLog.debug("npsid:--"+npsid);
     			ULLog.debug("subsetId:--"+subsetId);
     			ULLog.debug("generalSubset:--"+generalSubset);
     			ULLog.debug("treeId:--"+treeId);
     			ULLog.debug("levelNo:--"+levelNo);
     			ULLog.debug("fromRow:--"+fromRow);
     			ULLog.debug("toRow:--"+toRow);
     			
	     		 if(CommonDBQuery.getSystemParamtersList().get("ENABLEFASTSEARCH").equalsIgnoreCase("Y"))
	     		 {
	     			 ULLog.debug("Calling Product Hunter Ultimate : PRODHUN_ULTIMATE_MAIN");
	     			 System.out.println("Calling Product Hunter Ultimate : PRODHUN_ULTIMATE_MAIN");
	     			
	     			 stmt = conn.prepareCall("{call PRODHUN_ULTIMATE_MAIN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
	     		 }
	     		 else
	     		 {	     	
	     			ULLog.debug("calling PRODUCT_HUNTER_MAIN_IN_USE");
	     			
	     			System.out.println("calling PRODUCT_HUNTER_MAIN_IN_USE");
	     			/*if(CommonDBQuery.getSystemParamtersList().get("ENABLEFASTSEARCH").equalsIgnoreCase("Y"))
	     			{
	     				System.out.println("Calling fast search procedure");
	     				stmt = conn.prepareCall("{call PRODUCT_HUNTER_MAIN_FASTER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
	     			}
	     			else{*/
	     				System.out.println("Calling Normal search procedure");
	     			 stmt = conn.prepareCall("{call PRODUCT_HUNTER_MAIN_IN_USE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
	     			//}
	     			//stmt = conn.prepareCall("{call PRODUCT_HUNTER_MAIN_IN_USE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
	     		 }
	        	 
	        	 stmt.setString(1, requestType);
	        	 stmt.setString(2, orgKeyWord);
	             if(CommonDBQuery.getSystemParamtersList().get("ENABLEFASTSEARCH").equalsIgnoreCase("Y"))
	     		 {
	            	 stmt.setString(3, newPnKeyword);
	            	 stmt.setString(4, pnsimKeyword);
	            	 stmt.setString(5, fpKeyWord);
		             stmt.setString(6, bsKeyWord);
		             stmt.setString(7, accumKeyword1);
		             stmt.setString(8, accumKeyword2);  
		             stmt.setInt(9, psid);  
		             stmt.setInt(10, npsid);  
		             stmt.setInt(11, subsetId);
		             stmt.setInt(12, generalSubset); 
		             stmt.setInt(13, treeId);
		             stmt.setInt(14, levelNo);
		             if(isSortBy)
		             {
		            	 if(requestType.trim().equalsIgnoreCase("NAVIGATION"))
		    	             stmt.setString(15, sortBy+",DISPLAY_PRIORITY");
		    	            else
		    	            	 stmt.setString(15, sortBy);
		             }
		             else
		             {
		             if(requestType.trim().equalsIgnoreCase("NAVIGATION"))
		             stmt.setString(15, "IS_GOLDENITEM,DISPLAY_PRIORITY,BRAND_NAME,PART_NUMBER");
		             else if(requestType.trim().equalsIgnoreCase("SHOP_BY_BRAND"))
		            	 stmt.setString(15, "IS_GOLDENITEM,BRAND_NAME,PART_NUMBER");
		             else
		            	 stmt.setString(15, "IS_GOLDENITEM,RANK");
		             }
		             stmt.setInt(16, fromRow);  
		             stmt.setInt(17, toRow); 
		             stmt.setArray(18, FILTER_ATTR_ID);
		             stmt.setArray(19, FILTER_ATTRVAL_ID);
		             stmt.setArray(20, FILTER_ATTRUOM_ID);
		             stmt.setArray(21, FILTER_TYPE_ID);
		             stmt.registerOutParameter(22,java.sql.Types.INTEGER);//Result count
		             stmt.registerOutParameter(23,java.sql.Types.INTEGER); //Psid
		             stmt.registerOutParameter(24,java.sql.Types.INTEGER); //Npsid
		             stmt.registerOutParameter(25,OracleTypes.CURSOR);  //search_result rs
		             stmt.registerOutParameter(26,OracleTypes.CURSOR);//categorries rs
		             stmt.registerOutParameter(27,OracleTypes.CURSOR);//filter rs
		             stmt.registerOutParameter(28,java.sql.Types.VARCHAR);//query response
		             stmt.registerOutParameter(29,java.sql.Types.VARCHAR);
	            	 
	     		 }
	             else
	             {
	            	 stmt.setString(3, pnKeyWord);
	            	 stmt.setString(4, fpKeyWord);
		             stmt.setString(5, bsKeyWord);
		             stmt.setString(6, accumKeyword1);
		             stmt.setString(7, accumKeyword2);  
		             stmt.setInt(8, psid);  
		             stmt.setInt(9, npsid);  
		             stmt.setInt(10, subsetId);
		             stmt.setInt(11, generalSubset); 
		             stmt.setInt(12, treeId);
		             stmt.setInt(13, levelNo);
		             if(isSortBy)
		             {
		            	 if(requestType.trim().equalsIgnoreCase("NAVIGATION"))
		    	             stmt.setString(14, sortBy+",DISPLAY_PRIORITY");
		    	            else
		    	            	 stmt.setString(14, sortBy);
		             }
		             else
		             {
		             if(requestType.trim().equalsIgnoreCase("NAVIGATION"))
		             stmt.setString(14, "DISPLAY_PRIORITY,BRAND_NAME,PART_NUMBER");
		             else if(requestType.trim().equalsIgnoreCase("SHOP_BY_BRAND"))
		            	 stmt.setString(14, "BRAND_NAME,PART_NUMBER");
		             else
		            	 stmt.setString(14, "RANK");
		             }
		             stmt.setInt(15, fromRow);  
		             stmt.setInt(16, toRow); 
		             stmt.setArray(17, FILTER_ATTR_ID);
		             stmt.setArray(18, FILTER_ATTRVAL_ID);
		             stmt.setArray(19, FILTER_ATTRUOM_ID);
		             stmt.setArray(20, FILTER_TYPE_ID);
		             stmt.registerOutParameter(21,java.sql.Types.INTEGER);//Result count
		             stmt.registerOutParameter(22,java.sql.Types.INTEGER); //Psid
		             stmt.registerOutParameter(23,java.sql.Types.INTEGER); //Npsid
		             stmt.registerOutParameter(24,OracleTypes.CURSOR);  //search_result rs
		             stmt.registerOutParameter(25,OracleTypes.CURSOR);//categorries rs
		             stmt.registerOutParameter(26,OracleTypes.CURSOR);//filter rs
		             stmt.registerOutParameter(27,java.sql.Types.VARCHAR);//query response
		             stmt.registerOutParameter(28,java.sql.Types.VARCHAR);
	             }
	             
	             System.out.println("Start Time: " + new Date());
	             stmt.execute();
	             System.out.println("End Time: " + new Date());	       
	            
	             String queryResult=null;
	             if(CommonDBQuery.getSystemParamtersList().get("ENABLEFASTSEARCH").equalsIgnoreCase("Y"))
	     		 {
	            	
	            	 stmt.getInt(22);	   
	            	 popularSearchId = stmt.getInt(23);
	            	 queryResult = stmt.getString(28);	     		 
	     		 }
	             else
	             {
	            	 stmt.getInt(21);
	            	 popularSearchId = stmt.getInt(22);
	            	 queryResult = stmt.getString(27);
	             }
	             
	             System.out.println("queryResult : " +queryResult);
	             ArrayList<ProductsModel> itemLevelFilterData = new ArrayList<ProductsModel>();
	             ArrayList<ProductsModel> taxonomyLevelFilterData = new ArrayList<ProductsModel>();
	             ArrayList<ProductsModel> attrFilterData = new ArrayList<ProductsModel>();
	             ArrayList<ProductsModel> attrFilteredList = new ArrayList<ProductsModel>();
	             
	             if(CommonDBQuery.getSystemParamtersList().get("ENABLEFASTSEARCH").equalsIgnoreCase("Y"))
 	     		 {
	            	 
	            	 if(stmt.getString(29).equalsIgnoreCase("Y"))
            		 {
            		  searchCategory = (ResultSet) stmt.getObject(26);
            		 }
 	     		 }
            	  else
            	  {
            		  if(stmt.getString(28).equalsIgnoreCase("Y"))
	            		 {
            		  searchCategory = (ResultSet) stmt.getObject(25);
	            		 }
            	  }
	             
	             if(CommonDBQuery.getSystemParamtersList().get("ENABLEFASTSEARCH").equalsIgnoreCase("Y"))
	     		 {	              
	            	 if(requestType.equalsIgnoreCase("SEARCH"))
	            	 {
	            		 if(stmt.getString(29).equalsIgnoreCase("Y"))
	            		 {
	            			 if(stmt.getObject(25)!=null)
	        	             {
	            	             searchResult = (ResultSet) stmt.getObject(25);
	            	             if(stmt.getObject(27)!=null){
		            	             searchFilters = (ResultSet) stmt.getObject(27);
	            	             }
	        	             }
	            		 else{
	            			 searchResult=null;
	            			 searchFilters=null;
	            		 }
	            		 }
	            	 }
	            	 else
	            	 {
	            		 if(stmt.getObject(25)!=null)
	            		 searchResult = (ResultSet) stmt.getObject(25);
	            		 if(stmt.getObject(27)!=null)
           	              searchFilters = (ResultSet) stmt.getObject(27);
	            	 }
	     		 }
	             else
	             {
	            	 if(requestType.contains("SEARCH"))
	            	 {
	            		 if(stmt.getString(28).equalsIgnoreCase("Y"))
	            		 {
	            			 if(stmt.getObject(24)!=null)
	        	             {
	            	             searchResult = (ResultSet) stmt.getObject(24);
	            	             if(stmt.getObject(26)!=null)
		            	              searchFilters = (ResultSet) stmt.getObject(26);
	        	             }
	            		 else{
	            			 searchResult=null;
	            			 searchFilters=null;
	            		 }
	            		 }
	            	 }
	            	 else
	            	 {
	            		 if(stmt.getObject(24)!=null)
	            		 searchResult = (ResultSet) stmt.getObject(24);
	            		 if(stmt.getObject(26)!=null)
           	              searchFilters = (ResultSet) stmt.getObject(26);
	            	 }
	             }        
	             
	            
	             
	             if(searchResult==null)
	            	 System.out.println("Search returns Null");
	     
	              
	              // searchResult=null;
	             if(searchResult!=null)
	             {
	            	 
	            	  while(searchCategory.next())
	 	             {
	            		ProductsModel nameCode= new ProductsModel();
	 	            	String itemUrl = searchCategory.getString("CATEGORY_NAME");
		            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
		            	itemUrl = itemUrl.replaceAll(" ","-");
		            	nameCode.setItemUrl(itemUrl);
	 	 	        	nameCode.setCategoryCode(searchCategory.getString("TAXONOMY_TREE_ID").toString().trim());
	 	 	        	nameCode.setCategoryName(searchCategory.getString("CATEGORY_NAME").toString().trim());
	 	 	        	nameCode.setCategoryCount(searchCategory.getInt("CATEGORY_COUNT"));
	 	 	        	if(requestType.trim().equalsIgnoreCase("NAVIGATION")){
		 	 	        	nameCode.setLevelNumber(searchCategory.getInt("LEVEL_NUMBER"));
		 	 	        	nameCode.setImageName((searchCategory.getString("IMAGE_NAME")==null)?"NoImage.png":searchCategory.getString("IMAGE_NAME").toString().trim());
	 	 	        	}
	 	 	        	if(!requestType.trim().equalsIgnoreCase("NAVIGATION"))
	 	 	        	{
	 	 	        		nameCode.setParentCategory(searchCategory.getString("PARENT_CATEGORY_NAME"));
	 	 	        	 
	 	 	        	}
	 	 	        	taxonomyLevelFilterData.add(nameCode);
	 	               }
	 	               System.out.println("End cat RS Time: " + new Date());
	 	               boolean appendCategoryFilter = false;
	 	               LinkedHashMap<String, ArrayList<ProductsModel>>  catFilteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
	 	               ArrayList<ProductsModel> catAttrFilteredList = new ArrayList<ProductsModel>();
	 	               ArrayList<ProductsModel> catAttrFilterData = new ArrayList<ProductsModel>();
	 	              ProductsModel catAttrVal = new ProductsModel();
	 	               if(taxonomyLevelFilterData.size()==1)
	 	               {
	 	            	  if(attrFilterList!=null && !attrFilterList.trim().equalsIgnoreCase(""))
	            		   {   
	            		   for(int i=0;i<FILTERATTRID.length;i++)
	            		   {
	            			   int typeId = FILTERTYPEID[i];
	            			   if(typeId==1 && FILTERATTRID[i]==0)
	            			   {
	            				   taxonomyLevelFilterData.get(0).getCategoryName();
	            				   	            		   catAttrVal.setAttrId(0);
	            				   	            		   catAttrVal.setAttrType(1);
	            				   	            		   catAttrVal.setAttrValue(taxonomyLevelFilterData.get(0).getCategoryName());
	            				   	            		   catAttrVal.setAttrValueId(CommonUtility.validateNumber(taxonomyLevelFilterData.get(0).getCategoryCode()));
	            				   	            	       catAttrVal.setAttrName("Category");
	            				   						   catAttrFilteredList.add(catAttrVal);
	            				   						   catFilteredList.put("Category",catAttrFilteredList);
	            				   						   catAttrVal = new ProductsModel();
	            				   						   catAttrVal.setAttrFilterList(catFilteredList);
	            				   						   catAttrFilterData.add(catAttrVal);
	            				   						   appendCategoryFilter = true;
	            			   }
	            		   }
	            		   
	            		   }
	 	               }
	 	               if(appendCategoryFilter)
	 	               {
	 	            	  taxonomyLevelFilterData = new ArrayList<ProductsModel>();
	 	               }
	 	              searchResultList.put("categoryList", taxonomyLevelFilterData);
	 	               
	 	              if(isItem)
		            	 {
	            	 ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
	            	 System.out.println("Start RS Time: " + new Date());
		               while(searchResult.next())
		               {	     
		            	   ProductsModel itemVal =  new ProductsModel();
		            	String itemUrl = searchResult.getString("BRAND_NAME")+ searchResult.getString("MANUFACTURER_PART_NUMBER");
		            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
		            	itemUrl = itemUrl.replaceAll(" ","-");
		            	itemVal.setItemId(searchResult.getInt("ITEM_ID"));
		       			itemVal.setItemPriceId(searchResult.getInt("ITEM_PRICE_ID"));
		       			itemVal.setPartNumber(searchResult.getString("PART_NUMBER"));
		       			itemVal.setCustomerPartNumber(searchResult.getString("COMPETITOR_PART_NUMBER"));
		       			itemVal.setManufacturerPartNumber(searchResult.getString("MANUFACTURER_PART_NUMBER"));
		       			itemVal.setItemUrl(itemUrl);
		       			itemVal.setManufacturerName(searchResult.getString("BRAND_NAME"));
		       			itemVal.setShortDesc(searchResult.getString("SHORT_DESC"));
		       			itemVal.setPrice(searchResult.getDouble("NET_PRICE"));
		       			itemVal.setImapPrice(searchResult.getDouble("NET_PRICE"));
		       			itemVal.setIsImap(searchResult.getString("IMAP"));		       	        
		       			itemVal.setUpc(searchResult.getString("UPC"));	
		       			ProductsModel partIdentifiers = new ProductsModel();
		       	        partIdentifiers.setErpPartNumber(searchResult.getString("PART_NUMBER"));
		       	        partIdentifiersList.add(partIdentifiers);
		       			itemVal.setUom(searchResult.getString("SALES_UOM"));
		       			itemVal.setSaleQty(searchResult.getInt("SALES_QTY"));
		       			System.out.println("Sale Qty : " + searchResult.getString("PART_NUMBER")+ " - " +searchResult.getInt("SALES_QTY"));
		       			itemVal.setNotes(searchResult.getString("NOTES"));
		       			itemVal.setPackDesc(searchResult.getString("PACK_DESC"));
		       			itemVal.setResultCount(searchResult.getInt("RESULT_COUNT"));
		       			itemVal.setImageName((searchResult.getString("IMAGE_NAME")==null)?"NoImage.png":searchResult.getString("IMAGE_NAME").toString().trim());
		       			itemVal.setProductSeardId(popularSearchId);
		       			itemVal.setMaterialGroup(searchResult.getString("MATERIAL_GROUP"));
		       			if(searchResult.getInt("MIN_ORDER_QTY")>0)
		       				itemVal.setMinOrderQty(searchResult.getInt("MIN_ORDER_QTY"));
		       			else
		       				itemVal.setMinOrderQty(1);
		       			itemVal.setOrderInterval(searchResult.getInt("ORDER_QTY_INTERVAL"));
		       			
		       			itemLevelFilterData.add(itemVal);
		               }
		               String requiredAvailabilty = null;
			 			if(userId>1){
			 				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_AVAILABILITY_PRODUCT_LISTING");
			 			}else{
			 				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_AVAILABILITY_PRODUCT_LISTING");	
			 			}
		               String isErpDown = CommonDBQuery.getSystemParamtersList().get("ECLIPSEAVAILABLE");
		               String swithOnEclipse = CommonDBQuery.getSystemParamtersList().get("SWITCHONECLIPSEPRICE");
		               
		               if(swithOnEclipse.equalsIgnoreCase("Y") && type.equalsIgnoreCase("mobile")){
		            	   massEnquiry = true;
		               }else if(swithOnEclipse.equalsIgnoreCase("N") && type.equalsIgnoreCase("")){
		            	   massEnquiry = true;
		               }else {
		            	   massEnquiry = false;
		               }
		              
		               
		               if(massEnquiry){
		               
			        	 if(isErpDown.equalsIgnoreCase("Y")){
		               if(itemLevelFilterData!=null && itemLevelFilterData.size()>0 && eclipseSessionId != null && !eclipseSessionId.trim().equalsIgnoreCase(""))
		               {
		            	   ArrayList<ProductsModel> productPriceOutput = new ArrayList<ProductsModel>();
		            	   
		            	   if( type.equalsIgnoreCase(""))
		            		   productPriceOutput = ERPProductsWrapper.massProductInquiry(eclipseSessionId,partIdentifiersList, userName,entityId,requiredAvailabilty);
		            	   for(ProductsModel itemPrice : itemLevelFilterData)
		            	   {
		            	   
		            		   	for(ProductsModel eclipseitemPrice : productPriceOutput)
		            		   		{
		            		   			if(itemPrice!=null)
		            		   				{
		            		   					if(itemPrice.getPartNumber().equals(eclipseitemPrice.getPartNumber().trim()))
		            		   							{
		            		   						if(itemPrice.getIsImap()!=null && itemPrice.getIsImap().trim().equalsIgnoreCase("Y"))
		            		   							itemPrice.setImapPrice(eclipseitemPrice.getCustomerPrice());
		            		   						else
		            		   						           itemPrice.setPrice(eclipseitemPrice.getCustomerPrice());
		            		   						           itemPrice.setBranchAvail(eclipseitemPrice.getBranchAvail());
		            		   						        if(eclipseitemPrice.getBranchTotalQty()>0)
		            		   						           {
		            		   						        	   if(eclipseitemPrice.getBranchAvail().size()>0 && eclipseitemPrice.getBranchAvail().get(0).getAvailQty()==0)
		            		   						        	   {
		            		   						        		   itemPrice.setDisplayFrieghtAlert("Y");
		            		   						        	   }
		            		   						           }
		            		   						           itemPrice.setBranchTotalQty(eclipseitemPrice.getBranchTotalQty());
		            		   						        if( CommonDBQuery.getSystemParamtersList().get("GET_PRICE_FROM").equalsIgnoreCase("eclipse")){
		            		   						        	 itemPrice.setUom(eclipseitemPrice.getUom());
		            		   						        	itemPrice.setPrice(eclipseitemPrice.getCustomerPrice()*eclipseitemPrice.getQty());
		            		   						        	itemPrice.setQty(eclipseitemPrice.getQty());
		            		   						           }
		            		   							}
		            		   				}
		            	   }
		            	   
		               }
		               }
			        	 }
			        	 }
		               searchResultList.put("itemList", itemLevelFilterData);	             
		               System.out.println("End RS Time: " + new Date());
	            
	            	 System.out.println("Start cat RS Time: " + new Date());
	              
	               if(searchFilters!=null)
	               {
	            	 LinkedHashMap<String, ArrayList<ProductsModel>>  filterDataList =new LinkedHashMap<String, ArrayList<ProductsModel>>();
	            	 LinkedHashMap<String, ArrayList<ProductsModel>>  filteredList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
	          		 attrFilterData = new ArrayList<ProductsModel>();
	          		attrFilteredList = new ArrayList<ProductsModel>();
	          		System.out.println("Start Filter Time: " + new Date());
	          		int counter = 0;
	          		ProductsModel oldAttrVal =  null;
	          		int oldAttrId = 0;
	          		 if(appendCategoryFilter)
	          		 {
	          		filteredList = catFilteredList;
	          		 }
	            	   while(searchFilters.next())
	            	   {
	            		   boolean addToList = true;
	            		   attrFilterData=filterDataList.get(searchFilters.getString("ATTR_NAME"));
	            		   attrFilteredList = filteredList.get(searchFilters.getString("ATTR_NAME"));
	            		   ProductsModel attrVal =  new ProductsModel();
	            		   attrVal.setAttrId(searchFilters.getInt("ATTR_ID"));
	            		   attrVal.setAttrType(searchFilters.getInt("ATTR_FILTER_TYPE"));
	            		   attrVal.setAttrValue(searchFilters.getString("ATTR_VALUE"));
	            		   attrVal.setAttrValueId(searchFilters.getInt("ATTR_VALUE_ID"));
	            		   attrVal.setResultCount(searchFilters.getInt("VALUE_COUNT"));
	            		   attrVal.setAttrName(searchFilters.getString("ATTR_NAME"));
	            		
	            		   if(attrFilterList!=null && !attrFilterList.trim().equalsIgnoreCase(""))
	            		   {   
	            			   
	            			  
	            		   for(int i=0;i<FILTERATTRID.length;i++)
	            		   {
	            			   int typeId = FILTERTYPEID[i];
	            			   
	            			   if(typeId==5 && FILTERATTRID[i]==searchFilters.getInt("ATTR_ID"))
	            			   {
	            				   if(oldAttrId!=searchFilters.getInt("ATTR_ID"))
	            				   {
		            				   counter =0;
		            				   oldAttrId = searchFilters.getInt("ATTR_ID");
	            				   }
	            				   counter++;
	            				   if(counter>1){
	            				   addToList = true;
	            				   }
	            				   else{
	            					   oldAttrVal = new ProductsModel();
	            					   oldAttrVal = attrVal;
	            					   addToList = false;
	            				   }
	            				   
	            				   if(counter>2)
	            					   oldAttrVal=null;
	            				   
	            				   if(attrFilteredList==null)
		     						  {
		            					   attrFilteredList = new ArrayList<ProductsModel>();
		            					   attrFilteredList.add(attrVal);
		     								 
		     						  }
		     						  else
		     						  {
		     							 attrFilteredList.add(attrVal);
		     						  }
		            				   filteredList.put(searchFilters.getString("ATTR_NAME"),attrFilteredList);
	            			   }
	            			   else
	            			   {
	            				   if(FILTERATTRID[i]==searchFilters.getInt("ATTR_ID") && FILTERATTRVALID[i]==searchFilters.getInt("ATTR_VALUE_ID") && typeId==searchFilters.getInt("ATTR_FILTER_TYPE"))
		            			   {
		            				  
	            					   addToList = false;
		            				   if(attrFilteredList==null)
		     						  {
		            					   attrFilteredList = new ArrayList<ProductsModel>();
		            					   attrFilteredList.add(attrVal);
		     						  }
		     						  else
		     						  {
		     							 attrFilteredList.add(attrVal);
		     						  }
		            				   filteredList.put(searchFilters.getString("ATTR_NAME"),attrFilteredList);
		            	     			
		            			   }
	            			   }
	            			  
	            		   }
	            		   }
	            		   if(addToList){
	            		   if(attrFilterData==null)
							  {
	            			   attrFilterData = new ArrayList<ProductsModel>();
	            			  if(oldAttrVal!=null && counter>1)
	            				  attrFilterData.add(oldAttrVal);
								  attrFilterData.add(attrVal);
								  counter = 0;
									 
							  }
							  else
							  {
								  if(oldAttrVal!=null && counter>1)
		            				  attrFilterData.add(oldAttrVal);
								  attrFilterData.add(attrVal);
							  }
	            		   filterDataList.put(searchFilters.getString("ATTR_NAME"),attrFilterData);
	            		   }
	            	   }
	            	   attrFilterData = new ArrayList<ProductsModel>();
	            	   ProductsModel attrVal =  new ProductsModel();
	            	   if(filterDataList!=null && filterDataList.size()>0){
	            	   attrVal.setAttrFilterList(filterDataList);
	            	   attrFilterData.add(attrVal);}
	            	   searchResultList.put("attrList", attrFilterData);
	            	   attrVal = new ProductsModel();
	            	   attrFilterData = new ArrayList<ProductsModel>();
	            	   if(filteredList.size()>0){
	            		  
	            			  
	            			   attrVal.setAttrFilterList(filteredList);
	    	            	   attrFilterData.add(attrVal);
	            			            	   
	            	   }
	            	   searchResultList.put("filteredList", attrFilterData);
	            	   System.out.println("End Filter Time: " + new Date());
	               }
	               else
	               {
	            	   if(appendCategoryFilter)
		          		 {
	            		   	searchResultList.put("filteredList", catAttrFilterData);
		          		 }
	               }
		            	 }
	               if(searchResult!=null)
	            	   searchResult.close();
	               
	               if(searchCategory!=null)
	            	   searchCategory.close();
	               
	               }
         } catch (SQLException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
         } catch (Exception e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
         }finally{
	        	 ConnectionManager.closeDBResultSet(searchResult);
	        	 ConnectionManager.closeDBResultSet(searchCategory);
	        	 ConnectionManager.closeDBResultSet(searchFilters);
            	 ConnectionManager.closeDBStatement(stmt);
            	 ConnectionManager.closeDBConnection(conn);
         }
         System.out.println("Store Procedure End : " + new Date());
  		return searchResultList;
  	}
	
	
	public static void writePopularityHits(int userId,int itemId,int popularity,int hits)
	{
		PrintWriter pw = null;
		FileWriter fr = null;
		try{
    		
    		int recordCount = CommonDBQuery.getRecordCount();
    		String fileName = "";
    		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");  
    		SimpleDateFormat format1 = new SimpleDateFormat("yyMMddHHmmss");  
    		Date date = new Date();
    		System.out.println(format.format(date));
    		String dateStart = format.format(date);
           
            Date d1 = format.parse(dateStart);

              
            fileName = format1.format(d1);
            // Get msec from each, and subtract.
           
              
            boolean newFile = false;
            
            if(recordCount==0)
            {
            	CommonDBQuery.setPopularityHitsFileName(fileName);
            }
            else
            {
            	fileName = CommonDBQuery.getPopularityHitsFileName();
            }
    		String data = userId + "\t" + itemId+ "\t"+popularity+ "\t"+hits;
    		String filePath = CommonDBQuery.getSystemParamtersList().get("POPULARITY_HITS_PATH");
    		if(CommonUtility.validateString(filePath).trim().equalsIgnoreCase(""))
    			filePath = "/var/popularity/";
    		UsersDAO.folderExist(filePath);
			String fileNameGen = filePath+fileName+".txt";
			System.out.println("PopularityHits File Name : " + fileNameGen);
    		File file =new File(fileNameGen);
    		if(!file.exists()){
    			file.createNewFile();
    			newFile = true;
    		}
    		fr = new FileWriter(file, true);
    		pw = new PrintWriter(fr);
    		if(newFile) {
    			 pw.println("USER_ID \t ITEM_ID \t HITS \t POPULARITY");
    		}
    		pw.println(data);
    		pw.flush();
    		if(recordCount+1 > 1000)
    			recordCount = -1;
    		
    		recordCount = recordCount + 1;
    		CommonDBQuery.setRecordCount(recordCount);
    		
 	        System.out.println("Done" + CommonDBQuery.getRecordCount());
 
    	}catch(IOException e){
    		e.printStackTrace();
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closePrintWriter(pw);
			ConnectionManager.closeFileWriter(fr);
    	}
	}
	
	
	
	public static void writeUserLog(int userId,String action, String sessionId, String userIp, String actionType,String actionKey)
	{
		PrintWriter pw = null;
		FileWriter fr = null;
		try{
    		
    		int recordCount = CommonDBQuery.getUserLogRecordCount();
    		String fileName = "";
    		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");  
    		SimpleDateFormat format1 = new SimpleDateFormat("yyMMddHHmmss");  
    		Date date = new Date();
    		System.out.println(format.format(date));
    		String dateStart = format.format(date);
           
            Date d1 = format.parse(dateStart);

              
            fileName = format1.format(d1);
            // Get msec from each, and subtract.
           
              
            boolean newFile = false;
            
            if(recordCount==0)
            {
            	CommonDBQuery.setUserLogFileName(fileName);
            }
            else
            {
            	fileName = CommonDBQuery.getUserLogFileName();
            }
    		String data = userId + "\t" + action+ "\t"+sessionId+ "\t"+date+"\t"+userIp+"\t"+actionType+"\t"+actionKey;
    		String filePath =  CommonDBQuery.getSystemParamtersList().get("USER_LOG_PATH");
    		if(CommonUtility.validateString(filePath).trim().equalsIgnoreCase("")) {
    			filePath = "/var/userlog/";
    		}
    		UsersDAO.folderExist(filePath);
    		
			String fileNameGen = filePath+fileName+".txt";
			System.out.println("UserLog File Name : " + fileNameGen);
    		File file =new File(fileNameGen);
    		if(!file.exists()){
    			file.createNewFile();
    			newFile = true;
    		}
    		fr = new FileWriter(file, true);
    		pw = new PrintWriter(fr);
    		if(newFile) {
    			 pw.println("USER_ID \t ACTION \t SESSION_ID \t DATETIME \t USER_IP_ADDRESS \t ACTION_TYPE \t ACTION_KEY");
    		}
    		pw.println(data);
    		pw.flush();
    		if(recordCount+1 > 1000)
    			recordCount = -1;
    		
    		recordCount = recordCount+1;
    		CommonDBQuery.setUserLogRecordCount(recordCount);
 	        System.out.println("Done");
 
    	}catch(IOException e){
    		e.printStackTrace();
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closePrintWriter(pw);
			ConnectionManager.closeFileWriter(fr);
    	}
	}
	
	public static void writePopularSearchWord(String keyWord,int userId,int subsetId, int count,String searchType)
	{
		PrintWriter pw = null;
		FileWriter fr = null;
		try{
    		
    		String fileDateTime = CommonDBQuery.getPopularSearchFileName();
    		String fileName = "";
    		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");  
    		SimpleDateFormat format1 = new SimpleDateFormat("yyMMddHHmmss");  
    		Date date = new Date();
    		System.out.println(format.format(date));
    		String dateStart = format.format(date);
            String dateStop = format.format(date);
            if(fileDateTime!=null)
            	dateStart = fileDateTime;
            else
            	CommonDBQuery.setPopularSearchFileName(dateStart);
            Date d1 = null;
            Date d2 = null;
         
                d1 = format.parse(dateStart);
                d2 = format.parse(dateStop);
              
            fileName = format1.format(d1);
            // Get msec from each, and subtract.
            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000;         
            long diffMinutes = diff / (60 * 1000) % 60;   
            String timeDifference = CommonDBQuery.getSystemParamtersList().get("TIME_DIFFERENCE");
            if(timeDifference!=null && !timeDifference.trim().equalsIgnoreCase(""))
            {
            	
            }
            else
            {
            	timeDifference = "10";
            }
            if(diffMinutes>=CommonUtility.validateNumber(timeDifference))
            {
            	fileName = format1.format(d2);
            	CommonDBQuery.setPopularSearchFileName(dateStop);
            }
            System.out.println("Time in seconds: " + diffSeconds + " seconds.");         
            System.out.println("Time in minutes: " + diffMinutes + " minutes.");         
            boolean newFile = false;
    		String data = keyWord + "\t" + userId+ "\t"+subsetId+ "\t"+count + "\t" + searchType;
    		String filePath = CommonDBQuery.getSystemParamtersList().get("KEYWORD_FILE_PATH");
			String fileNameGen = filePath+fileName+".txt";
			System.out.println("SearchFile Name : " + fileNameGen);
    		File file =new File(fileNameGen);
    		if(!file.exists()){
    			file.createNewFile();
    			newFile = true;
    		}
    		fr = new FileWriter(file, true);
    		pw = new PrintWriter(fr);
    		if(newFile)
    			 pw.println("Keyword \t UserId \t SubsetId \t Count \t SearchType");
    		pw.println(data);
    		pw.flush();
 	        System.out.println("Done");
 
    	}catch(IOException e){
    		e.printStackTrace();
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectionManager.closePrintWriter(pw);
			ConnectionManager.closeFileWriter(fr);
    	}
	}
	public static ProductsModel getCategoryDescription(String taxonomyId)
	{
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    ProductsModel bannerDetail = null;
	    try {
        	conn = ConnectionManager.getDBConnection();
	        String sql = ""; 
	        sql = "SELECT TT.TAXONOMY_TREE_ID, TT.STATIC_PAGE_ID, TT.IMAGE_NAME, TT.IMAGE_TYPE as CATEGORY_IMAGE_TYPE, TT.CATEGORY_DESC,BR.BANNER_IMAGE_NAME, BR.IMAGE_TYPE, TT.PAGE_TITLE, TT.META_DESC FROM TAXONOMY_TREE TT, BANNERS BR WHERE TT.TAXONOMY_TREE_ID=? AND BR.BANNER_ID (+) = TT.BANNER_ID AND SYSDATE BETWEEN BR.EFFECTIVE_DATE(+) AND BR.END_DATE(+) ";
	        pstmt=conn.prepareStatement(sql);
	        pstmt.setString(1, taxonomyId);
	        rs=pstmt.executeQuery();
	        if(rs.next())
	        {
	        	  bannerDetail = new ProductsModel();
	        	  bannerDetail.setImageName(rs.getString("BANNER_IMAGE_NAME"));
	        	  bannerDetail.setShortDesc(rs.getString("CATEGORY_DESC"));
	        	  bannerDetail.setImageType(rs.getString("IMAGE_TYPE"));
	        	  bannerDetail.setProductCategoryImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
	        	  bannerDetail.setProductCategoryImageType(rs.getString("CATEGORY_IMAGE_TYPE"));
	        	  bannerDetail.setStaticPageId(rs.getString("STATIC_PAGE_ID"));
	        	  bannerDetail.setPageTitle(rs.getString("PAGE_TITLE"));
	        	  bannerDetail.setMetaDesc(rs.getString("META_DESC"));
	        }
	      } catch (SQLException e) {
	        	ULLog.sqlTrace(e.getSQLState());
	            e.printStackTrace();
	      } catch (Exception e) {
	    	  ULLog.errorTrace(e.fillInStackTrace());
	          e.printStackTrace();
	
	      } finally {
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	      }
	     return bannerDetail;
	}
	
	
	public static ArrayList<ProductsModel> getBreadCrumb(String taxonomyLevelNo,String taxonomyId)
	{
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
	    StringBuilder selectSql = new StringBuilder();
	    StringBuilder fromSql = new StringBuilder();
	    StringBuilder whereSql = new StringBuilder();
	    int levelNo = CommonUtility.validateNumber(taxonomyLevelNo);
	    int i = 1;
		ArrayList<ProductsModel> breadCrumbList = new ArrayList<ProductsModel>();
		try {
	    	conn = ConnectionManager.getDBConnection();
	    	selectSql.append("SELECT ");
	    	fromSql.append(" FROM ");
	    	whereSql.append(" WHERE TT.TAXONOMY_TREE_ID =? and ");
	    	for(i=1;i<=levelNo;i++)
	    	{
	    		selectSql.append("LTMV").append(i).append(".TAXONOMY_TREE_ID LEVEL").append(i).append(",LTMV").append(i).append(".CATEGORY_NAME LEVEL").append(i).append("_NAME");
	    		fromSql.append(" LOC_TAXONOMY_MV ltmv").append(i);
	    		whereSql.append("LTMV").append(i).append(".TAXONOMY_TREE_ID=TT.LEVEL").append(i);
	    		if(i!=levelNo){
	    			selectSql.append(", ");
	    			fromSql.append(", ");
	    			whereSql.append(" AND ");
	    		}
	    		
	    		
	    	}
	    	fromSql.append(",  TAXONOMY_TREE TT");
	    	selectSql.append(" %s %s");
	    	String sql = String.format(selectSql.toString(),fromSql.toString(),whereSql.toString());
	    	
	        pstmt=conn.prepareStatement(sql);
	        pstmt.setString(1, taxonomyId);
	        rs=pstmt.executeQuery();
	                   
	          if(rs.next())
	          {  
	        	  for(i=1;i<=levelNo;i++)
	  	    	{
	        	  ProductsModel nameCode= new ProductsModel();
	        	  String itemUrl = rs.getString("LEVEL"+i+"_NAME").toString().trim();
	              //itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	              itemUrl = itemUrl.replaceAll(" ","-");
	              nameCode.setItemUrl(itemUrl);
	        	  nameCode.setCategoryCode(rs.getString("LEVEL"+i));
	        	  nameCode.setCategoryName(rs.getString("LEVEL"+i+"_NAME").toString().trim());
	        	  nameCode.setLevelNumber(i);
	        	  breadCrumbList.add(nameCode);
	  	    	}
	        	  
	          }
	         
	    } catch (SQLException e) { 
	    	e.printStackTrace();
	    }catch (Exception e) {         
	          e.printStackTrace();
	    } finally {
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	  ConnectionManager.closeDBConnection(conn);	
	      }
		return breadCrumbList;
	}
	public static LinkedHashMap<String,ArrayList<MenuAndBannersModal>>getCategoryBanners(String codeId)
	{
		LinkedHashMap<String,ArrayList<MenuAndBannersModal>> categoryBanners = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
		ArrayList<MenuAndBannersModal> categoryBanner = new ArrayList<MenuAndBannersModal>();

		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{ 
			String sql =PropertyAction.SqlContainer.get("getCategoryBanners"); 
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, codeId);
			pstmt.setString(2, codeId);
			pstmt.setString(3, codeId);
			pstmt.setString(4, codeId);
			
			rs = pstmt.executeQuery();
			System.out.println("----------------------CATEGORY BANNERS --------------------");
			while(rs.next())
			{
				MenuAndBannersModal bannerInfo = new MenuAndBannersModal();
				bannerInfo.setImageName(rs.getString("BANNER_IMAGE_NAME"));
				bannerInfo.setImageType(rs.getString("IMAGE_TYPE"));
				bannerInfo.setImageURL(rs.getString("BANNER_LANDING_URL"));
				bannerInfo.setImagePosition(rs.getString("BANNER_POSITION"));
				bannerInfo.setBannerScroll(rs.getString("SCROLLABLE"));
				bannerInfo.setBannerNumberofItem(rs.getString("NUMOFIMGSTOSCROLL"));
				bannerInfo.setBannerDirection(rs.getString("DIRECTION"));
				bannerInfo.setBannerDelay(rs.getString("DELAY"));
				String bannerPosition = rs.getString("BANNER_POSITION").toLowerCase();
				
				categoryBanner = categoryBanners.get(bannerPosition);
				if(categoryBanner==null)
				{
					categoryBanner = new ArrayList<MenuAndBannersModal>();
					categoryBanner.add(bannerInfo);
					
				}
				else
				{
					categoryBanner.add(bannerInfo);
					
				}
				categoryBanners.put(bannerPosition, categoryBanner);
				
				
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
		
		return categoryBanners;
			 
		
	}
	
	public static ArrayList<MenuAndBannersModal> getHomePageBanners()
	{
		ArrayList<MenuAndBannersModal> homePageBanners = new ArrayList<MenuAndBannersModal>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//String sql ="SELECT BANNER_IMAGE_NAME,IMAGE_TYPE ,BANNER_LANDING_URL FROM BANNERS B,VALUE_LIST_DATA V WHERE SYSDATE BETWEEN B.EFFECTIVE_DATE AND B.END_DATE AND V.BANNER_ID = B.BANNER_ID AND V.VALUE_LIST_ID IN (SELECT VALUE_LIST_ID FROM VALUES_LIST WHERE VALUE_LIST_NAME='Home Page Banners')";
		//String sql ="SELECT BANNER_IMAGE_NAME,IMAGE_TYPE ,BANNER_LANDING_URL,URL_WILL_BE FROM BANNERS B,VALUE_LIST_DATA V WHERE V.BANNER_ID = B.BANNER_ID(+) AND V.VALUE_LIST_ID IN (SELECT VALUE_LIST_ID FROM VALUES_LIST WHERE VALUE_LIST_NAME='Home Page Banners')  ORDER BY V.DISP_SEQ";
		
		try
		{
			String sql = PropertyAction.SqlContainer.get("getHomePageBanners");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("----------------------HOME PAGE BANNERS --------------------");
			while(rs.next()){
				MenuAndBannersModal bannerInfo = new MenuAndBannersModal();
				bannerInfo.setImageName(rs.getString("BANNER_IMAGE_NAME"));
				bannerInfo.setImageType(rs.getString("IMAGE_TYPE"));
				bannerInfo.setImageURL(rs.getString("BANNER_LANDING_URL"));
				bannerInfo.setBannerType(rs.getString("URL_WILL_BE"));
				bannerInfo.setBannerScrollDelay(rs.getString("DELAY"));
				bannerInfo.setBannerIsScrollable(rs.getString("scrollable"));
				bannerInfo.setBannerName(rs.getString("LIST_VALUE"));
				System.out.println("BNIMG/typ/BANNER_LANDING_URL/URL_WILL_BE/scrollable ----------------------"+rs.getString("BANNER_IMAGE_NAME")+"/"+rs.getString("IMAGE_TYPE")+"/"+rs.getString("BANNER_LANDING_URL")+"/"+rs.getString("URL_WILL_BE")+"/"+rs.getString("scrollable"));
				homePageBanners.add(bannerInfo);
			}
			System.out.println("--------------------------------------------------------");
			
			
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
		
		return homePageBanners;
			 
		
	}
	
	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getAllBannersList(){
		LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getAllBannerList = null;
		ArrayList<MenuAndBannersModal> homePageBanners = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			String avaialbleBannerNames = CommonDBQuery.getSystemParamtersList().get("BANNER_NAMES");
			if(avaialbleBannerNames!=null && avaialbleBannerNames.trim().length()>0){
				conn = ConnectionManager.getDBConnection();
				String[] bannerNameArray = avaialbleBannerNames.split("\\|");
				if(bannerNameArray!=null && bannerNameArray.length>0){
					getAllBannerList = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
					for(String bannerName : bannerNameArray){
						if(bannerName!=null && bannerName.trim().length()>0){
							
							homePageBanners = new ArrayList<MenuAndBannersModal>();
							String sql = PropertyAction.SqlContainer.get("getAllBannersList");
								pstmt = conn.prepareStatement(sql);
								pstmt.setString(1, bannerName.trim().toUpperCase());
								rs = pstmt.executeQuery();
								System.out.println("---------------------- "+bannerName+" --------------------");
								while(rs.next()){
									MenuAndBannersModal bannerInfo = new MenuAndBannersModal();
									bannerInfo.setImageName(rs.getString("BANNER_IMAGE_NAME"));
									bannerInfo.setImageType(rs.getString("IMAGE_TYPE"));
									bannerInfo.setImageURL(rs.getString("BANNER_LANDING_URL"));
									bannerInfo.setBannerType(rs.getString("URL_WILL_BE"));
									bannerInfo.setBannerScrollDelay(rs.getString("DELAY"));
									System.out.println("BNIMG/typ/url ----------------------"+rs.getString("BANNER_IMAGE_NAME")+"/"+rs.getString("IMAGE_TYPE")+"/"+rs.getString("BANNER_LANDING_URL")+"/"+rs.getString("URL_WILL_BE"));
									homePageBanners.add(bannerInfo);
								}
								ConnectionManager.closeDBResultSet(rs);
								ConnectionManager.closeDBPreparedStatement(pstmt);
								getAllBannerList.put(bannerName, homePageBanners);
								System.out.println("----------------------- "+bannerName+" -----------------");
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return getAllBannerList;
	}
	
	public static ArrayList<ProductsModel> getFeatureProduct(int subsetId, int generalSubsetId){
		 CallableStatement stmt = null;
         ResultSet itemData = null;
         Connection conn = null;
         ArrayList<ProductsModel> itemDetailObject = null;
         try {
             conn = ConnectionManager.getDBConnection();
        	 stmt = conn.prepareCall("{call GET_FEATURED_PRODUCTS_IN_USE(?,?,?,?)}");
        	 stmt.setInt(1, subsetId);
        	 stmt.setInt(2, generalSubsetId);
        	 stmt.registerOutParameter(3,OracleTypes.CURSOR);
        	 stmt.registerOutParameter(4,java.sql.Types.INTEGER);
        	 stmt.execute();
        	 System.out.println("Test");
        	 int queryResponse = (Integer) stmt.getObject(4);
        	 System.out.println("queryResponse : " + queryResponse);
        	 if(queryResponse!=0)
        	 {
	        	 if(stmt.getObject(3)!=null)
	        		 itemData = (ResultSet) stmt.getObject(3);
	        	 
	        	 itemDetailObject = new ArrayList<ProductsModel>();
	        	 while(itemData!=null && itemData.next())
	        	 {
	        		 ProductsModel detailData = new ProductsModel();
	        		 detailData.setItemId(itemData.getInt("ITEM_ID"));
	        		 String itemUrl = itemData.getString("BRAND_NAME")+itemData.getString("MANUFACTURER_PART_NUMBER");
	             	 //itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	             	 itemUrl = itemUrl.replaceAll(" ","-");
	             	 detailData.setItemUrl(itemUrl);
	        		 detailData.setItemPriceId(itemData.getInt("ITEM_PRICE_ID"));
	        		 detailData.setBrandName(itemData.getString("BRAND_NAME"));
	        		 detailData.setPartNumber(itemData.getString("PART_NUMBER"));
	        		 detailData.setManufacturerPartNumber(itemData.getString("MANUFACTURER_PART_NUMBER"));
	        		 detailData.setShortDesc(itemData.getString("SHORT_DESC"));
	        		 detailData.setPrice(itemData.getDouble("NET_PRICE"));
	        		 detailData.setPackDesc(itemData.getString("PACK_DESC"));
	        		 detailData.setUom(itemData.getString("SALES_UOM"));
	        		 detailData.setImageName(itemData.getString("IMAGE_NAME")==null?"NoImage.png":itemData.getString("IMAGE_NAME"));
	        		 detailData.setImageType(itemData.getString("IMAGE_TYPE"));
	        		 if(CommonUtility.checkDBColumn(itemData, "MANUFACTURER_NAME")){
		             		detailData.setManufacturerName(itemData.getString("MANUFACTURER_NAME"));
		             }
	        		 if(CommonUtility.checkDBColumn(itemData, "PAGE_TITLE")){
	        			 detailData.setPageTitle(itemData.getString("PAGE_TITLE"));
		             }
	        		 detailData.setMinOrderQty(itemData.getInt("MIN_ORDER_QTY"));
	        		 detailData.setOrderInterval(itemData.getInt("ORDER_QTY_INTERVAL"));
	        		 
	        		 
	        		 itemDetailObject.add(detailData);
	        	 }
	        }
         }
         catch (SQLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
       } catch (Exception e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
       }finally{
	        	 ConnectionManager.closeDBResultSet(itemData);
	        	 ConnectionManager.closeDBStatement(stmt);
	        	 ConnectionManager.closeDBConnection(conn);
       }
		      return itemDetailObject;
	}
	public static ArrayList<ProductsModel> getCategory()
	{
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
		ArrayList<ProductsModel> taxonomyLevelFilterData = new ArrayList<ProductsModel>();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
			
	    
	    try {
	    	conn = ConnectionManager.getDBConnection();
System.out.println(PropertyAction.SqlContainer.get("taxonomyLevelFiler"));
System.out.println(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
	          pstmt=conn.prepareStatement(PropertyAction.SqlContainer.get("taxonomyLevelFiler"));
	          pstmt.setInt(1, CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID")));
	          pstmt.setInt(2, 1);
	          pstmt.setInt(3, 1);	          
	          rs=pstmt.executeQuery();
	          ArrayList<Integer> userTabList = new ArrayList<Integer>();
	          userTabList = (ArrayList<Integer>) session.getAttribute("userTabList");
	          while(rs.next())
	          {  
	        	  boolean addList = false;
	        	 for(Integer uTabList:userTabList)
	        	  {
	        		  if(rs.getInt("TAXONOMY_TREE_ID")== uTabList)
	        			  addList = true;
	        	  }
	        	  if(addList){
	        		ProductsModel nameCode= new ProductsModel();
	        		String itemUrl = rs.getString("CATEGORY_NAME");
	            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	            	itemUrl = itemUrl.replaceAll(" ","-");
	            	nameCode.setItemUrl(itemUrl);
	            	nameCode.setCategoryCode(rs.getString("TAXONOMY_TREE_ID").toString().trim());
	            	nameCode.setCategoryName(rs.getString("CATEGORY_NAME").toString().trim());
	        	  	nameCode.setImageName((rs.getString("IMAGE_NAME")==null)?"NoImage.png":rs.getString("IMAGE_NAME").toString().trim());
	        	  	nameCode.setLevelNumber(rs.getInt("LEVEL_NUMBER"));	        	  
	        	  	taxonomyLevelFilterData.add(nameCode);
	        	  }
	          }
	          
	          
	      } catch (Exception e) {         
	          e.printStackTrace();

	      } finally {
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } 
		
		return taxonomyLevelFilterData;
	}
	
	public static ArrayList<ProductsModel> getCategoryApp(String tempSubset, String tempGeneralSubset)
	{
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt=null;
		ArrayList<ProductsModel> taxonomyLevelFilterData = new ArrayList<ProductsModel>();
		//HttpServletRequest request = ServletActionContext.getRequest();
		//HttpSession session = request.getSession();
			
	    
	    try {
	    	conn = ConnectionManager.getDBConnection();
	          pstmt=conn.prepareStatement(PropertyAction.SqlContainer.get("taxonomyLevelFiler"));
	          pstmt.setInt(1, CommonUtility.validateNumber(PropertyAction.SqlContainer.get("TAXONOMY_ID")));
	          pstmt.setInt(2, 1);
	          pstmt.setInt(3, 1);	          
	          rs=pstmt.executeQuery();
	          ArrayList<Integer> userTabList = new ArrayList<Integer>();
	          userTabList = (ArrayList<Integer>)getTopTab(CommonUtility.validateNumber(tempSubset), CommonUtility.validateNumber(tempGeneralSubset));
	          while(rs.next())
	          {  
	        	  boolean addList = false;
	        	 for(Integer uTabList:userTabList)
	        	  {
	        		  if(rs.getInt("TAXONOMY_TREE_ID")== uTabList)
	        			  addList = true;
	        	  }
	        	  if(addList){
	        		  ProductsModel nameCode= new ProductsModel();
	        		  String itemUrl = rs.getString("CATEGORY_NAME");
	        		  //itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	        		  itemUrl = itemUrl.replaceAll(" ","-");
	        		  nameCode.setItemUrl(itemUrl);
	        		  nameCode.setCategoryCode(rs.getString("TAXONOMY_TREE_ID").toString().trim());
	        		  nameCode.setCategoryName(rs.getString("CATEGORY_NAME").toString().trim());
	        		  nameCode.setImageName((rs.getString("IMAGE_NAME")==null)?"NoImage.png":rs.getString("IMAGE_NAME").toString().trim());
	        		  nameCode.setLevelNumber(rs.getInt("LEVEL_NUMBER"));	        	  
	        		  taxonomyLevelFilterData.add(nameCode);
	        	  }
	          }
	          
	          
	      } catch (Exception e) {         
	          e.printStackTrace();

	      } finally {
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } 
		
		return taxonomyLevelFilterData;

	}
	public static int updateCart(int userId,String sessionId)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		Connection  conn = null;
		PreparedStatement pstmt = null;
		int count=0;


        try
        {
        	conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("updatecart");
        	pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,userId);
			pstmt.setString(2, sessionId);
			count = pstmt.executeUpdate();
			
        	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
        CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		return count;
		 
	}
	public static void clearCart(int userId)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		Connection conn = null;
		    PreparedStatement pstmt = null;
		   
		    try {
		    	conn = ConnectionManager.getDBConnection();
		    	String sql = PropertyAction.SqlContainer.get("deleteFromCart");
		    	pstmt = conn.prepareStatement(sql);
		    	pstmt.setInt(1, userId);
		    	int count = pstmt.executeUpdate();
		    	
		    } catch (SQLException e) { 
	            e.printStackTrace();
	        }	
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);
	        }
		    CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
	}
	public static ArrayList<Integer> getTopTab(int subsetId,int generalSubsetId)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		Connection conn = null;
		ResultSet rs = null;
		 CallableStatement stmt = null;
		ArrayList<Integer> tabList = new ArrayList<Integer>();
		


         try{
        	 conn = ConnectionManager.getDBConnection();
        	 stmt = conn.prepareCall("{call GET_TOP_TABS_IN_USE(?,?,?)}");
        	 stmt.setInt(1, subsetId);
        	 stmt.setInt(2, generalSubsetId);
        	 stmt.registerOutParameter(3,OracleTypes.CURSOR);
        	 stmt.execute();
        	 if(stmt.getObject(3)!=null)
        		 rs = (ResultSet) stmt.getObject(3);
        	 while(rs!=null && rs.next())
        	 {
        		 tabList.add(rs.getInt("LEVEL1"));
        	 }
         }
         catch (SQLException e) {
             e.printStackTrace();
       } catch (Exception e) {
             e.printStackTrace();
       }finally{
	        	 ConnectionManager.closeDBResultSet(rs);
	        	 ConnectionManager.closeDBStatement(stmt);
	        	 ConnectionManager.closeDBConnection(conn);
       }
       CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
       return tabList;
	}
	public static boolean getCartBrandExcludeStatus(int userId,String sessionId,int brandID,String CartType)
	{
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    boolean isAvailable = false;
	   
        try
        {
        	int siteId = 0;
    		if(CommonDBQuery.getGlobalSiteId()>0){
        		siteId = CommonDBQuery.getGlobalSiteId();
    		}
        	conn = ConnectionManager.getDBConnection();
        	String sql = "";
        	if(CommonUtility.validateString(CartType).length()>0){
        		if(CartType.trim().equalsIgnoreCase("cart")){
        			if(CommonUtility.validateString(sessionId).length()>0){
                		sql =  PropertyAction.SqlContainer.get("getCartBrandExcludeStatusSessionUser");
                		pstmt = conn.prepareStatement(sql);
            			pstmt.setInt(1, brandID);
            			pstmt.setInt(2, siteId);
            			pstmt.setString(3, sessionId);
            			pstmt.setInt(4, userId);
            			rs = pstmt.executeQuery();
                	}else{
                		sql =  PropertyAction.SqlContainer.get("getCartBrandExcludeStatusWebUser");
                		pstmt = conn.prepareStatement(sql);
            			pstmt.setInt(1, brandID);
            			pstmt.setInt(2, siteId);
            			pstmt.setInt(3, userId);
            			rs = pstmt.executeQuery();
                	}
        		}else if(CartType.trim().equalsIgnoreCase("QuoteCart")){
        			sql = PropertyAction.SqlContainer.get("getCartBrandExcludeStatusReorderCart");
            		pstmt = conn.prepareStatement(sql);
        			pstmt.setInt(1, brandID);
        			pstmt.setInt(2, siteId);
        			pstmt.setString(3, sessionId);
        			rs = pstmt.executeQuery();
        		}
        	}
        	if(rs!=null && rs.next())
			{
				isAvailable=true;
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
        	ConnectionManager.closeDBConnection(conn);
        }
		return isAvailable;
	}
	
	public static LinkedHashMap<String, Object> getCartTotal(HttpSession session, LinkedHashMap<String, Object> contentObject)
	{
		
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    int cartCount = 0;
	    try
        {
        	DecimalFormat df = CommonUtility.getPricePrecision(session);
        	String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
    		int userId = CommonUtility.validateNumber(sessionUserId);
    		String tempSubset = (String) session.getAttribute("userSubsetId");
    	    int subsetId = CommonUtility.validateNumber(tempSubset);
    	    String sortByValue = " CART_ID ";
    		String sortBy =(String) session.getAttribute("sortBy");
    	    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
    	    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
    	    String homeTerritory = (String) session.getAttribute("shipBranchId");
    	    String quickCartView = (String) contentObject.get("quickCartView");
    	    int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
    	    double total = 0.0;
    		double cartTotal = 0.0;
    		int checkOciUser = -1;
    	    int siteId = 0;
    		if(CommonDBQuery.getGlobalSiteId()>0){
        		siteId = CommonDBQuery.getGlobalSiteId();
    		}
    		
    		ArrayList<ProductsModel> productListData = null;
    		ArrayList<String> partIdentifier = new ArrayList<String>();
    		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
    		
        	conn = ConnectionManager.getDBConnection();
        	
    	    String sql = "";
    	    if(session.getAttribute("isOciUser")!=null){
    			checkOciUser = (Integer) session.getAttribute("isOciUser");
     		}
        	if(checkOciUser==1){
        		
        		sql = PropertyAction.SqlContainer.get("getCartItemDetailQueryBySession");
        		sql = "SELECT * FROM ("+sql+") ORDER BY "+sortByValue;
        		if(quickCartView!=null && quickCartView.trim().equalsIgnoreCase("Y")){
        			sql = "SELECT * FROM (SELECT * FROM ("+sql+")ORDER BY CART_ID DESC) WHERE ROWNUM<=4";
        		}
        		pstmt = conn.prepareStatement(sql);
        		pstmt.setInt(1, siteId);
        		pstmt.setInt(2, userId);
    			pstmt.setString(3, session.getId());
    			pstmt.setInt(4, subsetId);
    			pstmt.setInt(5, siteId); 
    			pstmt.setInt(6, userId);
    			pstmt.setString(7, session.getId());
    			pstmt.setInt(8, generalSubset);
    			pstmt.setInt(9, siteId); 
    			pstmt.setInt(10, userId);
    			pstmt.setString(11, session.getId());
    			pstmt.setInt(12, subsetId);
    			rs = pstmt.executeQuery();
        	
        	}else{

        		if(userId>2){

        			sql = PropertyAction.SqlContainer.get("getCartItemDetailQuery");
        			sql = "SELECT * FROM ("+sql+") ORDER BY "+sortByValue;
        			if(quickCartView!=null && quickCartView.trim().equalsIgnoreCase("Y")){
        				sql = "SELECT * FROM (SELECT * FROM ("+sql+")ORDER BY CART_ID DESC) WHERE ROWNUM<=4";
        			}
        			pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, siteId);
                    pstmt.setInt(2, userId);
                    pstmt.setInt(3, subsetId);
                    pstmt.setInt(4, siteId);
                    pstmt.setInt(5, userId);
                    pstmt.setInt(6, generalSubset);
                    pstmt.setInt(7, siteId);
                    pstmt.setInt(8, userId);
                    pstmt.setInt(9, subsetId);
                    rs = pstmt.executeQuery();

        		}else{

        			sql = PropertyAction.SqlContainer.get("getCartItemDetailQueryBySession");
        			sql = "SELECT * FROM ("+sql+") ORDER BY "+sortByValue;
        			if(quickCartView!=null && quickCartView.trim().equalsIgnoreCase("Y")){
        				sql = "SELECT * FROM (SELECT * FROM ("+sql+")ORDER BY CART_ID DESC) WHERE ROWNUM<=4";
        			}
        			pstmt = conn.prepareStatement(sql);
        			pstmt.setInt(1, siteId);
        			pstmt.setInt(2, userId);
        			pstmt.setString(3, session.getId());
        			pstmt.setInt(4, subsetId);
        			pstmt.setInt(5, siteId);
        			pstmt.setInt(6, userId);
        			pstmt.setString(7, session.getId());
        			pstmt.setInt(8, generalSubset);
        			pstmt.setInt(9, siteId);
        			pstmt.setInt(10, userId);
        			pstmt.setString(11, session.getId());
        			pstmt.setInt(12, subsetId);
        			rs = pstmt.executeQuery();

        		}

        	}
        	productListData = new ArrayList<ProductsModel>();
        	while(rs.next()){
        		ProductsModel cartListVal = new ProductsModel();
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
				int packageQty = 1;
            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
            	itemUrl = itemUrl.replaceAll(" ","-");
            	cartListVal.setItemUrl(itemUrl);
				cartListVal.setProductListId(rs.getInt("CART_ID"));
				cartListVal.setItemId(rs.getInt("ITEM_ID"));
				cartListVal.setQty(rs.getInt("QTY"));
				cartListVal.setPrice(rs.getDouble("NET_PRICE"));
				cartListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
				cartListVal.setTotal(Double.parseDouble(df.format(rs.getDouble("NET_PRICE") * rs.getDouble("QTY"))));
				cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
				cartListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
				cartListVal.setManufacturerName(rs.getString("BRAND_NAME"));
				cartListVal.setImageType(rs.getString("IMAGE_TYPE"));
				cartListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				cartListVal.setUnspc(rs.getString("UNSPSC"));
				cartListVal.setPackDesc(rs.getString("PACK_DESC"));
				cartListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				cartListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				cartListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
				cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				cartListVal.setUom(rs.getString("SALES_UOM"));
				cartListVal.setSaleQty(rs.getInt("SALES_QTY"));
				cartListVal.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
				cartListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
				cartListVal.setPageTitle(rs.getString("PAGE_TITLE"));
				
				if(rs.findColumn("UOM")>0){
					cartListVal.setUom(rs.getString("UOM"));
				}
				partIdentifier.add(cartListVal.getPartNumber());
				partIdentifierQuantity.add(rs.getInt("QTY"));
				
				if(rs.getInt("PACKAGE_QTY")>0){
					packageQty = rs.getInt("PACKAGE_QTY");
				}
				cartListVal.setPackageFlag(rs.getInt("PACKAGE_FLAG"));
				cartListVal.setPackageQty(packageQty);
				cartListVal.setExtendedPrice(rs.getDouble("EXTPRICE"));
				if(rs.findColumn("ADDITIONAL_PROPERTIES")>0) {
					cartListVal.setAdditionalProperties(rs.getString("ADDITIONAL_PROPERTIES"));
				}
				total = total + rs.getDouble("EXTPRICE");
				productListData.add(cartListVal);
        	}
        	
        	if(productListData!=null && productListData.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
			{
				
				ProductManagement priceInquiry = new ProductManagementImpl();
				ProductManagementModel priceInquiryInput = new ProductManagementModel();
				String entityId = (String) session.getAttribute("entityId");
				priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
				priceInquiryInput.setHomeTerritory(homeTerritory);
				priceInquiryInput.setPartIdentifier(productListData);
				priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
				priceInquiryInput.setRequiredAvailabilty("Y");
				priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
				priceInquiryInput.setSession(session);
				productListData = priceInquiry.priceInquiry(priceInquiryInput , productListData);
				total = productListData.get(0).getCartTotal();
					
			}
        	String twoDecimalTotal = df.format(total);
        	cartTotal = Double.parseDouble(twoDecimalTotal);
			contentObject.put("productListData", productListData);
			contentObject.put("productListDataForCart", productListData);
			contentObject.put("cartSubTotal", cartTotal);
			contentObject.put("cartTotal", cartTotal);
			if(productListData != null && productListData.size() > 0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCSPConfigurator")).equalsIgnoreCase("Y")) {
				LogoCustomization logoCustomization = new LogoCustomization();
				int buyingCompanyId = CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString());
				contentObject = logoCustomization.setDesignDetails(productListData, userId, buyingCompanyId, contentObject);
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
        	ConnectionManager.closeDBConnection(conn);
        }
		return contentObject;
	}
	
	public static int getCartCount(int userId, int subsetID, int generalSubsetId)
	{
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    int cartCount = 0;
	   
        try
        {
        	int siteId = 0;
    		if(CommonDBQuery.getGlobalSiteId()>0){
        		siteId = CommonDBQuery.getGlobalSiteId();
    		}
        	conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("selectCartCount");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, siteId);
			pstmt.setInt(3, subsetID);
			pstmt.setInt(4, userId);
			pstmt.setInt(5, siteId);
			pstmt.setInt(6, generalSubsetId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHOW_CART_COUNT_BY")).equalsIgnoreCase("QUANTITY")){
					cartCount = rs.getInt("QTYCOUNT");
				}else{
					cartCount = rs.getInt("CARTCOUNT");	
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
        finally
        {
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
        	ConnectionManager.closeDBConnection(conn);
        }
		return cartCount;
	}
	
	public static ProductsModel getCartCountAndQuantityCount(int userId, int subsetID, int generalSubsetId)
	{
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    ProductsModel pModel = null;
        try
        {
        	int siteId = 0;
    		if(CommonDBQuery.getGlobalSiteId()>0){
        		siteId = CommonDBQuery.getGlobalSiteId();
    		}
        	conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("selectCartCount");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, siteId);
			pstmt.setInt(3, subsetID);
			pstmt.setInt(4, userId);
			pstmt.setInt(5, siteId);
			pstmt.setInt(6, generalSubsetId); 
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				pModel = new ProductsModel();
				pModel.setCartItemCount(rs.getInt("CARTCOUNT"));
				pModel.setCartIndividualItemQuantitySum(rs.getInt("QTYCOUNT"));
				
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
        	ConnectionManager.closeDBConnection(conn);
        }
		return pModel;
	}
	
	public static ProductsModel getCartCaseCount(int userId, int subsetID, int generalSubsetId)
	{
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    ProductsModel pModel = null;
        try
        {
        	int siteId = 0;
    		if(CommonDBQuery.getGlobalSiteId()>0){
        		siteId = CommonDBQuery.getGlobalSiteId();
    		}
        	conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("selectCartCaseCount");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, siteId);
			pstmt.setInt(3, subsetID);
			pstmt.setInt(4, userId);
			pstmt.setInt(5, siteId);
			pstmt.setInt(6, generalSubsetId); 
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				pModel = new ProductsModel();
				pModel.setCartItemCount(rs.getInt("CARTCOUNT"));
				pModel.setCartIndividualItemQuantitySum(rs.getInt("QTYCOUNT"));
				pModel.setMinOrderQty(rs.getInt("MINORDERQTY"));
				pModel.setCartCaseCount(rs.getInt("SUM_CASES"));
				pModel.setWeight(rs.getDouble("ITEM_WEIGHT"));
				pModel.setUnits(rs.getDouble("ITEM_UNITS"));
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
        	ConnectionManager.closeDBConnection(conn);
        }
		return pModel;
	}
	
	public static int getCartCountBySession(int userId,String sessionId,int subsetId, int generalSubsetId)
	{
		System.out.println("Getting Cart Count on session : " + sessionId);
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    int cartCount = 0;
	   
        try
        {
        	int siteId = 0;
    		if(CommonDBQuery.getGlobalSiteId()>0){
        		siteId = CommonDBQuery.getGlobalSiteId();
    		}
        	conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("selectCartCountBySession");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setString(2, sessionId);
			pstmt.setInt(3, siteId);
			pstmt.setInt(4, subsetId);
			pstmt.setInt(5, generalSubsetId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SHOW_CART_COUNT_BY")).equalsIgnoreCase("QUANTITY")){
					cartCount = rs.getInt("QTYCOUNT");
				}else{
					cartCount = rs.getInt("CARTCOUNT");	
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
        finally
        {
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
        	ConnectionManager.closeDBConnection(conn);
        }
		return cartCount;
	}
	
	public static String getBannerText(int subsetId)
	{
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String bannerText = "NoMessage";
		
        try
        {
        	conn = ConnectionManager.getDBConnection();
        	String sql = "SELECT BANNER_TEXT FROM SUBSETS WHERE SUBSET_ID=?";
        	pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subsetId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				if(rs.getString("BANNER_TEXT")!=null)
					bannerText = rs.getString("BANNER_TEXT");
				
			}
        	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return bannerText;
		
	}
	
	public static ArrayList<ProductsModel> getManufacturerList(String mIndex,int subsetId,int generalSubsetId)
	{
		ArrayList<ProductsModel> manfMenu = new ArrayList<ProductsModel>();
		Connection  conn = null;
		PreparedStatement preStat=null;
		ResultSet rs = null;
		String strQuery = new String();
		
		        try {
		        	conn = ConnectionManager.getDBConnection();
		        	if(CommonDBQuery.getSystemParamtersList().get("ALL_MANUFACTURERS_AS_GROUP") !=null && CommonDBQuery.getSystemParamtersList().get("ALL_MANUFACTURERS_AS_GROUP") .equalsIgnoreCase("Y")) {
		        		strQuery= PropertyAction.SqlContainer.get("getManufacturerListAsGroup");
		        		
		        		preStat = conn.prepareStatement(strQuery);
		        		preStat.setInt(1, subsetId);
		        		preStat.setInt(2, generalSubsetId);
		        	}else{
		        		strQuery= PropertyAction.SqlContainer.get("getManufacturerList");
		        		preStat = conn.prepareStatement(strQuery);
		        		preStat.setString(1, mIndex.trim().toUpperCase()+"%");
		        		preStat.setInt(2, subsetId);
		        	}
		        	
	        		rs = preStat.executeQuery();
	        	
				 while(rs.next())
				  {			  
					  	ProductsModel manufacturerListVal = new ProductsModel();
					  	String itemUrl = rs.getString("MANUFACTURER_NAME").toString().trim();
					  	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					  	itemUrl = itemUrl.replaceAll(" ","-");
		            	manufacturerListVal.setItemUrl(itemUrl);
		            	manufacturerListVal.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
		            	manufacturerListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
		            	manufacturerListVal.setManufacturerLogo(rs.getString("MANUFACTURER_LOGO"));
		            	manfMenu.add(manufacturerListVal);
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
			       finally {	
			    	  ConnectionManager.closeDBResultSet(rs);
			    	  ConnectionManager.closeDBPreparedStatement(preStat);
			    	  ConnectionManager.closeDBConnection(conn);	
			      }
		return manfMenu;
	}
	
	public static ArrayList<ProductsModel> getDirectFromManufacturerListDAO(String mIndex,int subsetId,int generalSubsetId)
	{
		ArrayList<ProductsModel> manfMenu = new ArrayList<ProductsModel>();
		Connection  conn = null;
		PreparedStatement preStat=null;
		ResultSet rs = null;
		String strQuery = new String();
		

  
		        try {
		        	conn = ConnectionManager.getDBConnection();
		        	strQuery= PropertyAction.SqlContainer.get("getDirectFromManufacturerList");
	        		preStat = conn.prepareStatement(strQuery);
	        		preStat.setString(1, mIndex.trim().toUpperCase()+"%");
	        		preStat.setInt(2, subsetId);
	        		rs = preStat.executeQuery();
	        	
				 while(rs.next())
				  {			  
					  	ProductsModel manufacturerListVal = new ProductsModel();
					  	String itemUrl = rs.getString("MANUFACTURER_NAME").toString().trim();
					  	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					  	itemUrl = itemUrl.replaceAll(" ","-");
		            	manufacturerListVal.setItemUrl(itemUrl);
		            	manufacturerListVal.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
		            	manufacturerListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
		            	manfMenu.add(manufacturerListVal);
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
			       finally {	
			    	  ConnectionManager.closeDBResultSet(rs);
			    	  ConnectionManager.closeDBPreparedStatement(preStat);
			    	  ConnectionManager.closeDBConnection(conn);	
			      }
		return manfMenu;
	}
	
	public static ArrayList<String> getManufacturerIndex(int subsetId,int generalSubsetId)
	{
		ArrayList<String> indexList = new ArrayList<String>();
		Connection conn = null;
		String sql = PropertyAction.SqlContainer.get("getManufacturerIndex");
		//SELECT LIST_ITEM_ID, QTY FROM SAVED_LIST_ITEMS WHERE SAVED_LIST_ID = ? AND ITEM_ID=?
		PreparedStatement pstmt = null;
		ResultSet rs = null;


		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subsetId);
			pstmt.setInt(2, generalSubsetId);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				indexList.add(rs.getString("MANUFACTURER_NAME"));
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
	    	ConnectionManager.closeDBConnection(conn);
		}
		return indexList;
	}
	
	public static ArrayList<String> getDirectFromManufacturerIndex(int subsetId,int generalSubsetId)
	{
		ArrayList<String> indexList = new ArrayList<String>();
		Connection conn = null;
		String sql = PropertyAction.SqlContainer.get("getDirectFromManufacturerIndex");
		//SELECT LIST_ITEM_ID, QTY FROM SAVED_LIST_ITEMS WHERE SAVED_LIST_ID = ? AND ITEM_ID=?
		PreparedStatement pstmt = null;
		ResultSet rs = null;


		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subsetId);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				indexList.add(rs.getString("MANUFACTURER_NAME"));
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
	    	ConnectionManager.closeDBConnection(conn);
		}
		return indexList;
	}
	
	public static ArrayList<ProductsModel> brandList(String mIndex,int subsetId, int generalSubsetId){
		ArrayList<ProductsModel> brandMenu = new ArrayList<ProductsModel>();
		Connection  conn = null;
		PreparedStatement preStat=null;
		ResultSet rs = null;
		String strQuery = new String();
		
			  	  
		        try {
		        	conn = ConnectionManager.getDBConnection();
		        	
		        	if(generalSubsetId>0){
		        		strQuery= PropertyAction.SqlContainer.get("getBrandListIncludingGenralSubset");
		        		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOM_BRAND_LIST_QUERY_FOR_GENERAL_SUBSET")).length()>0){
		        			String query = PropertyAction.SqlContainer.get(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOM_BRAND_LIST_QUERY_FOR_GENERAL_SUBSET")));
		        			preStat = conn.prepareStatement(query);
		        			preStat.setInt(1, subsetId);
			        		preStat.setInt(2, generalSubsetId);
		        		}else{
			        		preStat = conn.prepareStatement(strQuery);
			        		preStat.setString(1, CommonUtility.validateString(mIndex).toUpperCase()+"%");
			        		preStat.setInt(2, subsetId);
			        		preStat.setInt(3, generalSubsetId);
		        		}
		        	}else{
		        		//
		        		strQuery= PropertyAction.SqlContainer.get("getBrandList");
		        		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOM_BRAND_LIST_QUERY")).length()>0){
		        			String query = PropertyAction.SqlContainer.get(CommonDBQuery.getSystemParamtersList().get("CUSTOM_BRAND_LIST_QUERY"));
		        			preStat = conn.prepareStatement(query);
		        			preStat.setInt(1, subsetId);
		        			
		        		}else{
		        			preStat = conn.prepareStatement(strQuery);
			        		preStat.setString(1, CommonUtility.validateString(mIndex).toUpperCase()+"%");
			        		preStat.setInt(2, subsetId);
		        		}
		        		
		        	}
	        		
	        		rs = preStat.executeQuery();
	        	
				 while(rs.next()){			  
					  	ProductsModel manufacturerListVal = new ProductsModel();
					  	String itemUrl = rs.getString("BRAND_NAME").toString().trim();
					  	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					  	itemUrl = itemUrl.replaceAll(" ","-");
		            	manufacturerListVal.setItemUrl(itemUrl);
		            	manufacturerListVal.setBrandId(rs.getInt("BRAND_ID"));
		            	manufacturerListVal.setBrandName(rs.getString("BRAND_NAME"));
		            	manufacturerListVal.setBrandImage(rs.getString("BRAND_IMAGE"));
		            	brandMenu.add(manufacturerListVal);
				  }
				}catch(SQLException e){
			       e.printStackTrace();
			    }
			    catch(Exception e){
			       e.printStackTrace();
			    }
			    finally {	
			      ConnectionManager.closeDBResultSet(rs);
			      ConnectionManager.closeDBPreparedStatement(preStat);
			      ConnectionManager.closeDBConnection(conn);	
			    }
		return brandMenu;
	}
	
	//========= NEW
	public static ArrayList<Integer> selectFromCartBeforeLogin(String sessionID,int itemId,String uom){
		ArrayList<Integer> cartId = new ArrayList<Integer>();
		
		Connection conn = null;


		
		String sql = PropertyAction.SqlContainer.get("selectFromCartQueryBeforeLogin");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sessionID);
			pstmt.setInt(2, itemId);
			if(CommonUtility.validateString(uom).length()==0){
				pstmt.setString(3," ");
			}else{
				pstmt.setString(3, uom);
			}
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
	    	ConnectionManager.closeDBConnection(conn);
		}
		return cartId;
	}
	
	public static ArrayList<Integer> selectFromCart(int userId,int itemId,String uom){
		ArrayList<Integer> cartId = new ArrayList<Integer>();
		
		Connection conn = null;
		  
		
		String sql = PropertyAction.SqlContainer.get("selectFromCart");
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCSPConfigurator")).equalsIgnoreCase("Y")){
			sql = PropertyAction.SqlContainer.get("selectFromCartDesign");
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, itemId);
			if(CommonUtility.validateString(uom).length()==0){
				pstmt.setString(3," ");
			}else{
				pstmt.setString(3, CommonUtility.validateString(uom));
			}
			
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				cartId.add(rs.getInt("CART_ID"));
				cartId.add(rs.getInt("QTY"));
				cartId.add(rs.getInt("ITEM_ID"));
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
	    	ConnectionManager.closeDBConnection(conn);
		}
		return cartId;
	}
	
	public static ProductsModel getMinOrderQty(ProductsModel pModel){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
        try
        {
        	conn = ConnectionManager.getDBConnection();
        	String sql = "With t1 as(SELECT ITEM_PRICE_ID,IM.ITEM_ID, CASE WHEN NVL(IP.MIN_ORDER_QTY,1) < 2 THEN NVL(IM.MIN_ORDER_QTY,1) WHEN NVL(IP.MIN_ORDER_QTY,1) > 1 THEN NVL(IP.MIN_ORDER_QTY,1) END MIN_ORDER_QTY, CASE WHEN NVL(IP.MIN_ORDER_QTY,1) < 2 THEN NVL(IM.ORDER_QTY_INTERVAL,1) WHEN NVL(IP.MIN_ORDER_QTY,1) > 1 THEN NVL(IP.ORDER_QTY_INTERVAL,1) END ORDER_QTY_INTERVAL FROM ITEM_PRICES IP, ITEM_MASTER IM WHERE ITEM_PRICE_ID= ?  AND IM.ITEM_ID     = IP.ITEM_ID),t2 as( select NVL(SIR.MIN_ORDER_QTY,1) SHIP_MIN_ORDER_QTY,NVL(SIR.ORDER_QTY_INTERVAL,1) SHIP_ORDER_QTY_INTERVAL,IP.ITEM_ID from  SHIP_VIA SV, ITEM_PRICES IP, shipvia_itemqty_restriction sir where UPPER(SV.SHIP_VIA_CODE)= ?  AND SIR.SHIP_VIA_ID = SV.SHIP_VIA_ID AND IP.ITEM_PRICE_ID =  ?  and sir.item_id(+) = IP.item_id ) select T1.ITEM_PRICE_ID, CASE WHEN NVL(T2.SHIP_MIN_ORDER_QTY,1) > 1 THEN NVL(T2.SHIP_MIN_ORDER_QTY,1) ELSE NVL(T1.MIN_ORDER_QTY,1) END MIN_ORDER_QTY, CASE WHEN NVL(T2.SHIP_MIN_ORDER_QTY,1) > 1 THEN NVL(T2.SHIP_ORDER_QTY_INTERVAL,1) ELSE NVL(T1.ORDER_QTY_INTERVAL,1) END ORDER_QTY_INTERVAL from t1,T2 WHERE T2.ITEM_ID(+) = T1.ITEM_ID";
        	pstmt = conn.prepareStatement(sql);
        	pstmt.setInt(1, pModel.getItemPriceId());
        	pstmt.setString(2, pModel.getShipViaCode().toUpperCase());
        	pstmt.setInt(3, pModel.getItemPriceId());
        	
        	rs = pstmt.executeQuery();
			if(rs.next())
			{
				pModel.setMinOrderQty(rs.getInt("MIN_ORDER_QTY")!=0?rs.getInt("MIN_ORDER_QTY"):1);
				pModel.setMinimumOrderQuantity(rs.getDouble("MIN_ORDER_QTY")!=0?rs.getDouble("MIN_ORDER_QTY"):1);
				pModel.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL")!=0?rs.getInt("ORDER_QTY_INTERVAL"):1);
			}
        	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
		return pModel;
		
	}
	
	/*public static ProductsModel getMinOrderQty(int itemPriceId){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductsModel minOrderQty = null;


        try
        {
        	conn = ConnectionManager.getDBConnection();
        	String sql = "SELECT ITEM_PRICE_ID,CASE WHEN NVL(IP.MIN_ORDER_QTY,1) < 2 THEN NVL(IM.MIN_ORDER_QTY,1) WHEN NVL(IP.MIN_ORDER_QTY,1) > 1 THEN NVL(IP.MIN_ORDER_QTY,1) END MIN_ORDER_QTY, CASE WHEN NVL(IP.ORDER_QTY_INTERVAL,1) < 2 THEN NVL(IM.ORDER_QTY_INTERVAL,1) WHEN NVL(IP.ORDER_QTY_INTERVAL,1) > 1 THEN NVL(IP.ORDER_QTY_INTERVAL,1) END ORDER_QTY_INTERVAL FROM ITEM_PRICES IP,ITEM_MASTER IM WHERE ITEM_PRICE_ID=? AND IM.ITEM_ID = IP.ITEM_ID";
        	pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, itemPriceId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				minOrderQty = new ProductsModel();
				minOrderQty.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				minOrderQty.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
			}
        	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
		return minOrderQty;
		
	}*/
	
	public static ArrayList<Integer> selectFromCartSession(int userId,int itemId, String sessionId,String uom){
		
		ArrayList<Integer> cartId = new ArrayList<Integer>();
		
		String sql = "SELECT CART_ID, QTY FROM CART WHERE USER_ID = ? AND ITEM_ID=? AND SESSIONID=? AND UOM=?";
		
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, itemId);
			pstmt.setString(3, sessionId);
			if(CommonUtility.validateString(uom).length()==0){
				pstmt.setString(4," ");
			}else{
				pstmt.setString(4, CommonUtility.validateString(uom));
			}

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
	    	ConnectionManager.closeDBConnection(conn);	
		}
		return cartId;
	}
	
	public static int insertItemToCart(int userId,int itemId,int qty, String sessionId, String lineItemComment,String catalogId,String uom, String price, int minOrderQty, double itemWeight, double itemUnit, LinkedHashMap<String, Object> utilityMap)
	{
		int count = -1;
		int siteId = 0;
		if(CommonDBQuery.getGlobalSiteId()>0){
    		siteId = CommonDBQuery.getGlobalSiteId();
		}
		String sql = PropertyAction.SqlContainer.get("insertItemToCart");
		Connection  conn = null;
		PreparedStatement pstmt = null;
		boolean considerLineItemComment = true;
		int designId = 0;
		int addressBookId = 0;
		int availabilityToCart = 0;
		String getPriceFrom = null;
		try{
			
			if(utilityMap!=null) {
				if(utilityMap.get("considerLineItemComment")!=null) {
					considerLineItemComment = (boolean) utilityMap.get("considerLineItemComment");
				}
				if(utilityMap.get("addToMultipleShippingAddress")!=null) {
					addressBookId = (int) utilityMap.get("addToMultipleShippingAddress");
				}
				if(utilityMap.get("designId")!=null) {
					designId = (int) utilityMap.get("designId");
				}
				if(utilityMap.get("availabilityToCart") != null) {
					availabilityToCart = CommonUtility.validateNumber(CommonUtility.validateString((String) utilityMap.get("availabilityToCart"))) ;
				}
				if(utilityMap.get("getPriceFrom") != null) {
					getPriceFrom = CommonUtility.validateString((String)utilityMap.get("getPriceFrom"));
				}
			}
			conn = ConnectionManager.getDBConnection();
			if(!considerLineItemComment) {
				if(designId > 0) {
					pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("insertItemToCartWithDesignId"));
					pstmt.setInt(1, itemId);
					pstmt.setInt(2, qty);
					pstmt.setString(3, sessionId);
					pstmt.setInt(4, userId);
					pstmt.setInt(5, siteId);
					pstmt.setString(6, catalogId);
					if(CommonUtility.validateString(uom).length()==0){
						pstmt.setString(7," ");	
					}else{
						pstmt.setString(7, CommonUtility.validateString(uom));	
					}
					pstmt.setString(8, price);
					pstmt.setInt(9, minOrderQty);
					pstmt.setDouble(10, itemWeight);
					pstmt.setDouble(11, itemUnit);
					pstmt.setInt(12, addressBookId);
					pstmt.setInt(13, designId);
					pstmt.setInt(14, availabilityToCart);
				}else {
					pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("insertItemToCartWitoutLineItemComment"));
					pstmt.setInt(1, itemId);
					pstmt.setInt(2, qty);
					pstmt.setString(3, sessionId);
					pstmt.setInt(4, userId);
					pstmt.setInt(5, siteId);
					pstmt.setString(6, catalogId);
					if(CommonUtility.validateString(uom).length()==0){
						pstmt.setString(7," ");	
					}else{
						pstmt.setString(7, CommonUtility.validateString(uom));	
					}
					pstmt.setString(8, price);
					pstmt.setInt(9, minOrderQty);
					pstmt.setDouble(10, itemWeight);
					pstmt.setDouble(11, itemUnit);
					pstmt.setInt(12, addressBookId);
					pstmt.setInt(13, availabilityToCart);
					pstmt.setString(14, getPriceFrom);
				}
			}else{
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, itemId);
				pstmt.setInt(2, qty);
				pstmt.setString(3, sessionId);
				pstmt.setInt(4, userId);
				pstmt.setInt(5, siteId);
				pstmt.setString(6, lineItemComment);
				pstmt.setString(7, catalogId);
				if(CommonUtility.validateString(uom).length()==0){
					pstmt.setString(8," ");	
				}else{
					pstmt.setString(8, CommonUtility.validateString(uom));	
				}
				pstmt.setString(9, price);
				pstmt.setInt(10, minOrderQty);
				pstmt.setDouble(11, itemWeight);
				pstmt.setDouble(12, itemUnit);
				pstmt.setString(13, utilityMap.get("orderOrQuoteNumber") != null ? utilityMap.get("orderOrQuoteNumber").toString() : "");
				pstmt.setInt(14, availabilityToCart);
				pstmt.setString(15, getPriceFrom);
				
			}
			count = pstmt.executeUpdate();
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
	    	ConnectionManager.closeDBConnection(conn);		
		}
		return count;
	}
	public static int insertNonCatalogItemToCart(ProductsModel itemData,UsersModel userData)
	{
		int count = -1;
		int siteId = 0;
		if(CommonDBQuery.getGlobalSiteId()>0){
			siteId = CommonDBQuery.getGlobalSiteId();
		}
		String NonCatalogItemId = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("NON_CATALOG_ITEM_ID_FOR_ADDTOCART"));//2520626 - For Weingartz
		String NonCatalogItemIdentifier = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CART_NCI_IDENTIFIER"));
		
		Connection  conn = null;
		PreparedStatement pstmt = null;
		try
		{
			String sql = PropertyAction.SqlContainer.get("insertNonCatalogItemToCart");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  itemData.getPartNumber());
			pstmt.setString(2,itemData.getBrandName());
			pstmt.setInt(3, itemData.getQty());
			pstmt.setDouble(4, itemData.getPrice());
			pstmt.setString(5, itemData.getManufacturerName());
			pstmt.setString(6, itemData.getDescription());
			pstmt.setString(7, "Cart");
			pstmt.setString(8, userData.getSession().getId());
			pstmt.setInt(9, userData.getUserId());
			pstmt.setInt(10,siteId);
			pstmt.setString(11, NonCatalogItemId);
			pstmt.setString(12, NonCatalogItemIdentifier);
			count = pstmt.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);		
		}
		return count;
	}
	public static void updatePopularity(int itemId,int rating){
		Connection  conn = null;
		PreparedStatement pstmt=null;
		String sql = "";
		try {
			conn = ConnectionManager.getDBConnection();
			sql = "UPDATE ITEM_MASTER SET REPLACECOLUMN = REPLACECOLUMNRATING WHERE ITEM_ID=?";
			sql = sql.replaceAll("REPLACECOLUMNRATING", "NVL(POPULARITY,0) + "+rating);
			sql = sql.replaceAll("REPLACECOLUMN", "POPULARITY");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, itemId);
			pstmt.executeUpdate();
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally {	    
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
	}
	
	public static void updateHits(int itemId){
		Connection  conn = null;
		PreparedStatement pstmt=null;
		String sql = "";
		try {
			conn = ConnectionManager.getDBConnection();
			sql = "UPDATE ITEM_MASTER SET HITS = NVL(HITS,0)+1 WHERE ITEM_ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, itemId);
			pstmt.executeUpdate();
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);	
		}
	}
	public static LinkedHashMap<String, ArrayList<ProductsModel>> getAttributeForCompare(String attrFilterList,String eclipseSessionId,String userName,String entityId,int userId){
		CallableStatement stmt = null;
		Connection conn = null;
		ResultSet itemData = null;
		ResultSet attrList = null;
		ResultSet uniqueAttrList = null;
		LinkedHashMap<String, ArrayList<ProductsModel>>  compareListGen = new LinkedHashMap<String, ArrayList<ProductsModel>>();
		LinkedHashMap<String, ArrayList<ProductsModel>> tempCompareList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
		ArrayList<Integer> itmIdSize = new ArrayList<Integer>();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String homeTerritory = (String) session.getAttribute("shipBranchId");
		try {
			conn = ConnectionManager.getDBConnection();
			if(attrFilterList!=null && !attrFilterList.equalsIgnoreCase("")){
				System.out.println("Store Procedure Start : " + new Date());
				int FILTERATTRID[] = null;
				String compareId[] = attrFilterList.split(",");
				FILTERATTRID = new int[compareId.length];
				for(int i=0;i<compareId.length;i++)
				{
					FILTERATTRID[i] = CommonUtility.validateNumber(compareId[i]);
				}
				Connection oracleConnection = null;
				if (conn instanceof org.jboss.resource.adapter.jdbc.WrappedConnection) {
					WrappedConnection wc = (WrappedConnection) conn;
					oracleConnection = wc.getUnderlyingConnection();
				}
				ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor( "NUM_ARRAY", oracleConnection );
				ARRAY FILTER_ATTR_ID = new ARRAY( descriptor, oracleConnection, FILTERATTRID );
				stmt = conn.prepareCall("{call ITEMS_COMPARE_IN_USE(?,?,?,?)}");
				stmt.setArray(1, FILTER_ATTR_ID);
				stmt.registerOutParameter(2,OracleTypes.CURSOR);
				stmt.registerOutParameter(3,OracleTypes.CURSOR);
				stmt.registerOutParameter(4,OracleTypes.CURSOR);
				stmt.execute();
				if(stmt.getObject(2)!=null){
					itemData = (ResultSet) stmt.getObject(2);
				}
				if(stmt.getObject(3)!=null){
					attrList = (ResultSet) stmt.getObject(3);
				}
				if(stmt.getObject(4)!=null){
					uniqueAttrList = (ResultSet) stmt.getObject(4);
				}
				LinkedHashMap<String, ArrayList<ProductsModel>>  compareList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				LinkedHashMap<String, ArrayList<ProductsModel>>  tmpCompareList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				ArrayList<ProductsModel> comapreFilterData = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> uniqueAttr = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> itemDataList = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> arrangeAttr = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> finalList = new ArrayList<ProductsModel>();
				ArrayList<ProductsModel> finalList1 = new ArrayList<ProductsModel>();
				ArrayList<String> partIdentifier = new ArrayList<String>();
				ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
				while(itemData!=null && itemData.next()){
					int packageQty = 1;
					finalList1 = tempCompareList.get("Detail");
					ProductsModel detailData =  new ProductsModel();
					detailData.setItemId(itemData.getInt("ITEM_ID"));
					itmIdSize.add(itemData.getInt("ITEM_ID"));
					String itemUrl = itemData.getString("BRAND_NAME")+itemData.getString("MANUFACTURER_PART_NUMBER");
					//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					itemUrl = itemUrl.replaceAll(" ","-");
					detailData.setItemUrl(itemUrl);
					detailData.setItemPriceId(itemData.getInt("ITEM_PRICE_ID"));
					detailData.setAltPartNumber1(itemData.getString("ALT_PART_NUMBER1"));
					detailData.setBrandName(itemData.getString("BRAND_NAME"));
					detailData.setBrandImage(itemData.getString("BRAND_IMAGE"));
					detailData.setPartNumber(itemData.getString("PART_NUMBER"));
					detailData.setManufacturerPartNumber(itemData.getString("MANUFACTURER_PART_NUMBER"));
					detailData.setUpc(itemData.getString("UPC"));
					detailData.setShortDesc(itemData.getString("SHORT_DESC"));
					detailData.setLongDesc(itemData.getString("LONG_DESC1")+" "+itemData.getString("LONG_DESC2"));
					detailData.setMetaDesc(itemData.getString("META_DESC"));
					detailData.setPageTitle(itemData.getString("PAGE_TITLE"));
					detailData.setPrice(itemData.getDouble("NET_PRICE"));
					detailData.setImapPrice(itemData.getDouble("NET_PRICE"));
					detailData.setIsImap(itemData.getString("IMAP"));
					detailData.setPackDesc(itemData.getString("PACK_DESC"));
					detailData.setUom(itemData.getString("SALES_UOM"));
					detailData.setSalesUom(itemData.getString("SALES_UOM"));
					detailData.setImageType(itemData.getString("IMAGE_TYPE"));
					detailData.setImageName(itemData.getString("IMAGE_NAME")==null?"NoImage.png":itemData.getString("IMAGE_NAME"));
					detailData.setNotes(itemData.getString("NOTES"));
					detailData.setImageType(itemData.getString("IMAGE_TYPE"));
					detailData.setSaleQty(itemData.getInt("SALES_QTY"));
					detailData.setQty(itemData.getInt("SALES_QTY"));
					detailData.setPackageFlag(itemData.getInt("PACKAGE_FLAG"));
					detailData.setMinOrderQty(itemData.getInt("MIN_ORDER_QTY")<1?1:itemData.getInt("MIN_ORDER_QTY"));
					detailData.setOrderInterval(itemData.getInt("ORDER_QTY_INTERVAL")<1?1:itemData.getInt("ORDER_QTY_INTERVAL"));
					detailData.setManufacturerName(itemData.getString("MANUFACTURER_NAME"));
					detailData.setOverRidePriceRule(itemData.getString("OVERRIDE_PRICE_RULE"));
					if(itemData.getInt("PACKAGE_QTY")>0){
						packageQty = itemData.getInt("PACKAGE_QTY");
					}
					detailData.setPackageQty(packageQty);
					partIdentifier.add(detailData.getPartNumber());
					partIdentifierQuantity.add(1);//Get Qty Dynamic If necessary
					if(finalList1==null){
						finalList1 = new ArrayList<ProductsModel>();
						finalList1.add(detailData);

					}else{
						finalList1.add(detailData);
					}
					tempCompareList.put("Detail", finalList1);
					itemDataList.add(detailData);
				}

				if(itemDataList!=null && itemDataList.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
				{
					ProductManagement priceInquiry = new ProductManagementImpl();
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					entityId = (String) session.getAttribute("entityId");
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					priceInquiryInput.setHomeTerritory(homeTerritory);
					priceInquiryInput.setPartIdentifier(itemDataList);
					priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
					priceInquiryInput.setRequiredAvailabilty("Y");
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					itemDataList = priceInquiry.priceInquiry(priceInquiryInput , itemDataList);
				}

				while(attrList!=null && attrList.next()){
					ProductsModel attrVal =  new ProductsModel();
					comapreFilterData = compareList.get(attrList.getString("ITEM_ID"));
					System.out.println(attrList.getInt("ITEM_ID")+" : "+attrList.getString("ATTRIBUTE_NAME")+" - "+attrList.getString("ATTRIBUTE_VALUE")+" - "+attrList.getString("ATTRIBUTE_UOM"));
					attrVal.setItemId(attrList.getInt("ITEM_ID"));
					attrVal.setAttrName(attrList.getString("ATTRIBUTE_NAME"));
					attrVal.setAttrValue(attrList.getString("ATTRIBUTE_VALUE"));
					attrVal.setAttrUom(attrList.getString("ATTRIBUTE_UOM"));

					if(comapreFilterData==null)
					{
						comapreFilterData = new ArrayList<ProductsModel>();
						comapreFilterData.add(attrVal);

					}
					else
					{
						comapreFilterData.add(attrVal);
					}
					compareList.put(attrList.getString("ITEM_ID"),comapreFilterData);
				}
				if(itmIdSize.size()>compareList.size()){
					for(Integer itmId : itmIdSize)
					{
						comapreFilterData = compareList.get(Integer.toString(itmId));
						if(comapreFilterData==null)
						{
							comapreFilterData = new ArrayList<ProductsModel>();

						}
						tmpCompareList.put(Integer.toString(itmId), comapreFilterData);

					}
					compareList = tmpCompareList;
				}
				while(uniqueAttrList!=null && uniqueAttrList.next()){
					ProductsModel attrVal =  new ProductsModel();
					attrVal.setAttrName(uniqueAttrList.getString("ATTRIBUTE_NAME"));
					uniqueAttr.add(attrVal);
				}
				for (Map.Entry<String, ArrayList<ProductsModel>> entry : compareList.entrySet()){

					arrangeAttr = entry.getValue();
					for(ProductsModel unAttr:uniqueAttr)
					{
						String attrName = unAttr.getAttrName();
						String attributeValue = " - ";
						String attributeUOM = " - ";
						int itemId = unAttr.getItemId();
						for(ProductsModel attrGen:arrangeAttr)
						{
							if(attrName.trim().equalsIgnoreCase(attrGen.getAttrName().trim()))
							{
								if(attrGen.getAttrValue()!=null)
									attributeValue = attrGen.getAttrValue();
								attributeUOM = attrGen.getAttrUom();
							}
						}
						ProductsModel attrVal =  new ProductsModel();
						attrVal.setItemId(itemId);
						attrVal.setAttrName(attrName);
						attrVal.setAttrValue(attributeValue);
						attrVal.setAttrUom(attributeUOM);
						attrVal.setItemId(CommonUtility.validateNumber(entry.getKey()));
						finalList.add(attrVal);
					}
					compareListGen.put(entry.getKey(), finalList);
				}
				for(ProductsModel temp:finalList){
					finalList1 = tempCompareList.get(temp.getAttrName());
					ProductsModel attrVal =  new ProductsModel();
					attrVal.setItemId(temp.getItemId());
					attrVal.setAttrName(temp.getAttrName());
					attrVal.setAttrValue(temp.getAttrValue());
					attrVal.setAttrUom(temp.getAttrUom());
					System.out.println("Check attr Val : " + temp.getAttrValue());
					if(finalList1==null){
						finalList1 = new ArrayList<ProductsModel>();
						finalList1.add(attrVal);

					}else{
						finalList1.add(attrVal);
					}
					tempCompareList.put(temp.getAttrName(), finalList1);
					System.out.println(temp.getAttrName() + " - " + temp.getAttrValue());
				}

			}
		}catch (SQLException e) { 
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(itemData);
			ConnectionManager.closeDBResultSet(attrList);
			ConnectionManager.closeDBResultSet(uniqueAttrList);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return tempCompareList;
	}
	
	public static LinkedHashMap<String, Object> getShoppingCartDao(HttpSession session, LinkedHashMap<String, Object> contentObject){
		long startTimer = CommonUtility.startTimeDispaly();
		String sessionUserId = (String)session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		
		String hookUrl = (String) session.getAttribute("hookUrl");
		String sortByValue = " CART_ID ";
		String sortBy =(String) session.getAttribute("sortBy");
		String tempSubset = (String) session.getAttribute("userSubsetId");
	    int subsetId = CommonUtility.validateNumber(tempSubset);
	    
	    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		String shipVia = (String) session.getAttribute("customerShipVia");
		String customMessage = CommonDBQuery.getSystemParamtersList().get("SOLDOUTMESSAGE");
		String homeTerritory = (String) session.getAttribute("shipBranchId");
		int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
		if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
   			session.removeAttribute("shipViaFlag");
		}

		WarehouseModel wareHouseDetail = new WarehouseModel();
		if(CommonUtility.validateString((String) session.getAttribute("wareHouseCode")).length()>0){
			wareHouseDetail = UsersDAO.getWareHouseDetailsByCode(session.getAttribute("wareHouseCode").toString());
		}
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
        try{
        	String quickCartView = (String) contentObject.get("quickCartView");
    		ArrayList<ProductsModel> productListData = null;
    		ArrayList<String> partIdentifier = new ArrayList<String>();
    		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
    		
    		ArrayList<Integer> itemList = new ArrayList<Integer>();
    		
    		double total = 0.0;
    		double cartTotal = 0.0;
    		int checkOciUser = -1;
    		String c = "";
    		String idList = "";
    		
    		DecimalFormat df = CommonUtility.getPricePrecision(session);
    		String sql = "";
        	int siteId = 0;
    		if(CommonDBQuery.getGlobalSiteId()>0){
        		siteId = CommonDBQuery.getGlobalSiteId();
    		}
    		if(ProductsDAO.getSubsetIdFromName(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME")))>0){
    			generalSubset = ProductsDAO.getSubsetIdFromName(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME")));
    	    }
        	conn = ConnectionManager.getDBConnection();
        	if(sortBy!=null && sortBy.trim().length()>0){
        		if(sortBy.trim().equalsIgnoreCase("MPNA")){
        			sortByValue = " MANUFACTURER_PART_NUMBER ASC ";
        		}else if(sortBy.trim().equalsIgnoreCase("MPND")){
        			sortByValue = " MANUFACTURER_PART_NUMBER DESC ";
        		}else if(sortBy.trim().equalsIgnoreCase("PartA")){
        			sortByValue = " PART_NUMBER ASC ";
        		}else if(sortBy.trim().equalsIgnoreCase("PartD")){
        			sortByValue = " PART_NUMBER DESC ";
        		}else if(sortBy.trim().equalsIgnoreCase("ProdNameA")){
        			sortByValue = " PRODUCT_NAME ASC ";
        		}else if(sortBy.trim().equalsIgnoreCase("ProdNameZ")){
        			sortByValue = " PRODUCT_NAME DESC ";
        		}else if(sortBy.trim().equalsIgnoreCase("ManfNameZ")){
        			sortByValue = " MANUFACTURER_NAME DESC ";
        		}else if(sortBy.trim().equalsIgnoreCase("ManfNameA")){
        			sortByValue = " MANUFACTURER_NAME ASC ";
        		}else if(sortBy.trim().equalsIgnoreCase("UPCA")){
        			sortByValue = " UPC ASC ";
        		}else if(sortBy.trim().equalsIgnoreCase("UPCZ")){
        			sortByValue = " UPC DESC ";
        		}
        		else if(sortBy.trim().equalsIgnoreCase("CPNA")){
        			sortByValue = " CUSTOMER_PART_NUMBER ASC ";
        		}
        		else if(sortBy.trim().equalsIgnoreCase("CPND")){
        			sortByValue = " CUSTOMER_PART_NUMBER DESC ";
        		}else{
        			sortByValue = " CART_ID";
        		}
        	}
        	session.setAttribute("cartSortByValue", sortByValue);
        	productListData = new ArrayList<ProductsModel>();
        	if(session.getAttribute("isOciUser")!=null){
    			checkOciUser = (Integer) session.getAttribute("isOciUser");
     		}
        	if(checkOciUser==1 || ( checkOciUser==2 && CommonUtility.validateString((String)session.getAttribute("SESSION_AUTH_REQUIRED")).equalsIgnoreCase("Y"))){
        		if(contentObject.get("partialCartDisplay")!=null && contentObject.get("partialCartDisplay")=="Y") {
    				sql = PropertyAction.SqlContainer.get("getPartialCartItemDetailQueryBySession");
        		}else {
        			sql = PropertyAction.SqlContainer.get("getCartItemDetailQueryBySession");
        		}
        		sql = "SELECT * FROM ("+sql+") ORDER BY "+sortByValue;
        		if(quickCartView!=null && quickCartView.trim().equalsIgnoreCase("Y")){
        			sql = "SELECT * FROM (SELECT * FROM ("+sql+")ORDER BY CART_ID DESC) WHERE ROWNUM<=4";
        		}
        		pstmt = conn.prepareStatement(sql);
        		pstmt.setInt(1, siteId);
        		pstmt.setInt(2, userId);
    			pstmt.setString(3, session.getId());
    			pstmt.setInt(4, subsetId);
    			pstmt.setInt(5, siteId);
    			pstmt.setInt(6, userId);
    			pstmt.setString(7, session.getId());
    			pstmt.setInt(8, generalSubset);
    			pstmt.setInt(9, siteId);
    			pstmt.setInt(10, userId);
    			pstmt.setString(11, session.getId());
    			pstmt.setInt(12, subsetId);
    			rs = pstmt.executeQuery();
        	}else{
	        	if(userId>2){
	        		if(contentObject.get("partialCartDisplay")!=null && contentObject.get("partialCartDisplay")=="Y") {
        				sql = PropertyAction.SqlContainer.get("getPartialCartItemDetailQuery");
	        		}else {
	        			sql = PropertyAction.SqlContainer.get("getCartItemDetailQuery");
	        		}
	        		
	        		sql = "SELECT * FROM ("+sql+") ORDER BY "+sortByValue;
	        		if(quickCartView!=null && quickCartView.trim().equalsIgnoreCase("Y")){
	        			sql = "SELECT * FROM (SELECT * FROM ("+sql+")ORDER BY CART_ID DESC) WHERE ROWNUM<=4";
	        		}
	        		pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, siteId);
                    pstmt.setInt(2, userId);
                    pstmt.setInt(3, subsetId);
                    pstmt.setInt(4, siteId);
                    pstmt.setInt(5, userId);
                    pstmt.setInt(6, generalSubset);
                    pstmt.setInt(7, siteId);
                    pstmt.setInt(8, userId);
                    pstmt.setInt(9, subsetId);
                    rs = pstmt.executeQuery();
	        	}else{
	        		sql = PropertyAction.SqlContainer.get("getCartItemDetailQueryBySession");
	        		sql = "SELECT * FROM ("+sql+") ORDER BY "+sortByValue;
	        		if(quickCartView!=null && quickCartView.trim().equalsIgnoreCase("Y")){
	        			sql = "SELECT * FROM (SELECT * FROM ("+sql+")ORDER BY CART_ID DESC) WHERE ROWNUM<=4";
	        		}
	    			pstmt = conn.prepareStatement(sql);
	    			pstmt.setInt(1, siteId);
	    			pstmt.setInt(2, userId);
	    			pstmt.setString(3, session.getId());
	    			pstmt.setInt(4, subsetId);
	    			pstmt.setInt(5, siteId);
	    			pstmt.setInt(6, userId);
	    			pstmt.setString(7, session.getId());
	    			pstmt.setInt(8, generalSubset);
	    			pstmt.setInt(9, siteId);
	    			pstmt.setInt(10, userId);
	    			pstmt.setString(11, session.getId());
	    			pstmt.setInt(12, subsetId);
	    			rs = pstmt.executeQuery();
	    			
	        	}
        	}
			while(rs.next()){
				ProductsModel cartListVal = new ProductsModel();
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
				int packageQty = 1;
            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
            	itemUrl = itemUrl.replaceAll(" ","-");
            	cartListVal.setItemUrl(itemUrl);
				cartListVal.setProductListId(rs.getInt("CART_ID"));
				cartListVal.setCartId(rs.getInt("CART_ID"));
				cartListVal.setOrderOrQuoteNumber(rs.getString("ORDER_QUOTE_NUMBER"));
				cartListVal.setItemId(rs.getInt("ITEM_ID"));
				cartListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				cartListVal.setImageType(rs.getString("IMAGE_TYPE"));
				cartListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				cartListVal.setUnspc(rs.getString("UNSPSC"));
				cartListVal.setUpc(rs.getString("UPC"));
				cartListVal.setPackDesc(rs.getString("PACK_DESC"));
				cartListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				cartListVal.setClearance(rs.getString("CLEARANCE"));
				cartListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				cartListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
				cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				cartListVal.setUom(CommonUtility.validateString(rs.getString("UOM")));
				cartListVal.setSalesUom(rs.getString("SALES_UOM"));
				cartListVal.setSaleQty(rs.getInt("SALES_QTY"));
				cartListVal.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
				cartListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
				cartListVal.setDisplayPrice(rs.getString("DISPLAY_PRICING"));
				cartListVal.setPageTitle(rs.getString("PAGE_TITLE"));
				cartListVal.setMultipleShipVia(rs.getString("ITEM_LEVEL_SHIPVIA"));
				cartListVal.setStandardApprovals(CommonUtility.validateString(rs.getString("STANDARD_APPROVALS")));
				cartListVal.setMultipleShipViaDesc(rs.getString("ITEM_LEVEL_SHIPVIA_DESC"));
				cartListVal.setWeight(rs.getDouble("WEIGHT"));
				cartListVal.setOverRidePriceRule(rs.getString("OVERRIDE_PRICE_RULE"));
				cartListVal.setWeight(rs.getDouble("WEIGHT"));
				cartListVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
				cartListVal.setAltPartNumber2(rs.getString("ALT_PART_NUMBER2"));
				if(rs.findColumn("AVAILABILITY")>0){
					cartListVal.setAvailQty(rs.getInt("AVAILABILITY"));
				}
				if(rs.findColumn("GET_PRICE_FROM")>0 && CommonUtility.validateString(rs.getString("GET_PRICE_FROM")).equalsIgnoreCase("CART")){
					cartListVal.setManufacturerName(rs.getString("MANUFACTURER"));
					cartListVal.setPartNumber(rs.getString("NC_PART_NUMBER"));
					cartListVal.setShortDesc(rs.getString("SHORT_DESCRIPTION"));
					cartListVal.setPrice(rs.getDouble("PRICE"));
					cartListVal.setUnitPrice(rs.getDouble("PRICE"));
					cartListVal.setQty(rs.getInt("QTY"));
					cartListVal.setOverRidePriceRule("Y");
					cartListVal.setTotal(Double.parseDouble(df.format(rs.getDouble("PRICE") * rs.getDouble("QTY"))));
					cartListVal.setBrandName(rs.getString("BRAND"));
					if(CommonUtility.validateString(rs.getString("GET_PRICE_FROM")).equalsIgnoreCase("CART")){
						if(CommonUtility.customServiceUtility()!=null){
							CommonUtility.customServiceUtility().configCartListValues(cartListVal, rs);
						}
					}
				} else{
					cartListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
					cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
					cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
					cartListVal.setQty(rs.getInt("QTY"));
					cartListVal.setBrandName(rs.getString("BRAND_NAME"));
					
					// "SavePriceInShoppingCart" below line to get price stored in cart and revoke Price call from erp for Shopping cart - note: Availability cannot be pulled on Cart if enabled 
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInShoppingCart")).equalsIgnoreCase("Y")) {
						cartListVal.setPrice(rs.getDouble("PRICE"));
						cartListVal.setUnitPrice(rs.getDouble("PRICE"));
						cartListVal.setTotal(Double.parseDouble(df.format(rs.getDouble("PRICE") * rs.getDouble("QTY"))));
					}else {
						cartListVal.setPrice(rs.getDouble("NET_PRICE"));
						cartListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
						cartListVal.setTotal(Double.parseDouble(df.format(rs.getDouble("NET_PRICE") * rs.getDouble("QTY"))));
					}
					
				}
				if(rs.findColumn("CATALOG_ID")>0){
					cartListVal.setCatalogId(rs.getString("CATALOG_ID"));	
				}
				if(CommonUtility.validateString(cartListVal.getCatalogId()).length() <= 0) {
					partIdentifier.add(cartListVal.getPartNumber());
				}
				if(CommonUtility.customServiceUtility() != null) {
					CommonUtility.customServiceUtility().mergeRequiredFields(cartListVal, rs);
				}
				
				partIdentifierQuantity.add(rs.getInt("QTY"));
				itemList.add(rs.getInt("ITEM_ID"));
				if(rs.getInt("PACKAGE_QTY")>0){
					packageQty = rs.getInt("PACKAGE_QTY");
				}
				cartListVal.setPackageFlag(rs.getInt("PACKAGE_FLAG"));
				cartListVal.setPackageQty(packageQty);
				cartListVal.setExtendedPrice(rs.getDouble("EXTPRICE"));
				
				if(rs.findColumn("GET_PRICE_FROM")>0 && CommonUtility.validateString(rs.getString("GET_PRICE_FROM")).equalsIgnoreCase("CART")){
					total = total + cartListVal.getTotal();
				}else{
					// "SavePriceInShoppingCart" below line to get price stored in cart and revoke Price call from erp for Shopping cart - note: Availability cannot be pulled if enabled 
					if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInShoppingCart")).equalsIgnoreCase("Y")) {
						total = total + cartListVal.getTotal();
					}else {
						idList = idList + c + rs.getInt("ITEM_ID");
						total = total + rs.getDouble("EXTPRICE");
						c = " OR ";
					}
				}
				
				if(rs.findColumn("UOM")>0 && rs.getString("SALES_UOM")==null){
					cartListVal.setUom(CommonUtility.validateString(rs.getString("UOM")));
				}
				
				if(rs.findColumn("ADDITIONAL_PROPERTIES")>0) {
					cartListVal.setAdditionalProperties(rs.getString("ADDITIONAL_PROPERTIES"));
				}
				/**
    			 *Below code Written is for Tyndale to get item level shipVia*Reference- Chetan Sandesh
    			 */
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")).equalsIgnoreCase("Y")){
					wareHouseDetail = new WarehouseModel();
					if(cartListVal.getMultipleShipVia().contains("STORE")) {
						session.setAttribute("shipViaFlag","Y");
					}
					if(cartListVal.getMultipleShipViaDesc()!=null) {
					String[] ShipViaWhrs = (cartListVal.getMultipleShipViaDesc()).split(",");
					if(ShipViaWhrs[1].length()>0) {wareHouseDetail = UsersDAO.getWareHouseDetailsByCode(ShipViaWhrs[1]);}
					}
				}
				if(wareHouseDetail!=null) {
				cartListVal.setWarehouseName(wareHouseDetail.getWareHouseName());
				cartListVal.setBranchAddress1(wareHouseDetail.getAddress1());
				cartListVal.setBranchAddress2(wareHouseDetail.getAddress2());
				cartListVal.setBranchCity(wareHouseDetail.getCity());
				cartListVal.setBranchState(wareHouseDetail.getState());
				cartListVal.setBranchPostalCode(wareHouseDetail.getZip());
				}
				
				/**
    			 *Below code is written to get item level manufacturer id
    			 */
				if(CommonUtility.checkDBColumn(rs,"MANUFACTURER_ID") && (rs.findColumn("MANUFACTURER_ID")>0 && rs.getInt("MANUFACTURER_ID") >0)){
					cartListVal.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				}
				
				cartListVal.setBcAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
				
				productListData.add(cartListVal);
				
				LinkedHashMap<String,ArrayList<ProductsModel> > cartItemDataList = new LinkedHashMap<String, ArrayList<ProductsModel>>();
				//CustomServiceProvider
				if(CommonUtility.customServiceUtility()!=null) {
					cartItemDataList = CommonUtility.customServiceUtility().getGroupedItemsInProductsData(productListData);
					contentObject.put("cartItemDataList", cartItemDataList);
					CommonUtility.customServiceUtility().cartRelatedAddOns(productListData, contentObject);
				}
				//CustomServiceProvider
			}
			
			if(!CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInShoppingCart")).equalsIgnoreCase("Y") && CommonUtility.validateString(idList).length()>0 && productListData!=null && productListData.size()>0 && session.getAttribute("userToken")!=null && CommonUtility.validateString(session.getAttribute("userToken").toString()).length()>0 && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("defaults")){
				ProductManagement priceInquiry = new ProductManagementImpl();
				ProductManagementModel priceInquiryInput = new ProductManagementModel();
				String entityId = (String) session.getAttribute("entityId");
				priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
				priceInquiryInput.setHomeTerritory(homeTerritory);
				priceInquiryInput.setPartIdentifier(productListData);
				priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
				priceInquiryInput.setRequiredAvailabilty("Y");
				if(CommonUtility.validateString(idList).length()>0 &&  CommonDBQuery.getSystemParamtersList().get("GET_ALL_BRANCH_AVAILABILITY")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ALL_BRANCH_AVAILABILITY").trim().equalsIgnoreCase("Y")){
				priceInquiryInput.setAllBranchavailabilityRequired("Y");
				}
				priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
				priceInquiryInput.setSession(session);
				if(CommonUtility.customServiceUtility()!=null) {
					 CommonUtility.customServiceUtility().setAllBranchAvailValue(priceInquiryInput);
					}
				productListData = priceInquiry.priceInquiry(priceInquiryInput , productListData);
				total = productListData.get(0).getCartTotal();
			}
			int buyingCompanyId = 0;
			if (CommonUtility.validateString(idList).length()>0 && productListData!=null && productListData.size()>0 && session.getAttribute("buyingCompanyId") != null) {
			buyingCompanyId = CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString());
			LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = ProductHunterSolr.getcustomerPartnumber(idList, buyingCompanyId, buyingCompanyId);
			
			if(customerPartNumber!=null && customerPartNumber.size()>0)
			{
				for(ProductsModel item : productListData) {
					item.setCustomerPartNumberList(customerPartNumber.get(item.getItemId()));
					}
				}
			}
			
			LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = null;
			if(CommonUtility.validateString(idList).length()>0 &&  CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
				customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "),"itemid");
			}
			
			if(productListData!=null && productListData.size()>0 && CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA")!=null && CommonDBQuery.getSystemParamtersList().get("ORDER_QTY_INTERVAL_SHIPVIA").trim().equalsIgnoreCase("Y")){
				if(itemList!=null && itemList.size()>0){
					LinkedHashMap<Integer, LinkedHashMap<String, ProductsModel>> shipOrderQtyAndIntervalByItem = ProductHunterSolr.getshipOrderQtyAndIntervalByItems(subsetId, generalSubset, StringUtils.join(itemList," OR "));
					contentObject.put("shipOrderQtyAndInterval", shipOrderQtyAndIntervalByItem);
					
					if(session!=null && session.getAttribute("selectedShipVia")!=null){
						ArrayList<ProductsModel> productListDataTemp = new ArrayList<ProductsModel>();
						for(ProductsModel productModel : productListData){
							
							LinkedHashMap<String, ProductsModel> getShipMinOrderAndInterval = shipOrderQtyAndIntervalByItem.get(productModel.getItemId());
							ProductsModel shipOrderQtyAndIntervalModel = new ProductsModel();
							
							if(getShipMinOrderAndInterval!=null && getShipMinOrderAndInterval.size()>0){
								shipOrderQtyAndIntervalModel = getShipMinOrderAndInterval.get(CommonUtility.validateString(session.getAttribute("selectedShipVia").toString()).trim().toUpperCase());
								if(shipOrderQtyAndIntervalModel!=null && shipOrderQtyAndIntervalModel.getMinOrderQty()>0){
									productModel.setMinOrderQty(shipOrderQtyAndIntervalModel.getMinOrderQty());
								}
								if(shipOrderQtyAndIntervalModel!=null && shipOrderQtyAndIntervalModel.getOrderInterval()>0){
									productModel.setOrderInterval(shipOrderQtyAndIntervalModel.getOrderInterval());
								}
							}
							productListDataTemp.add(productModel);
						}
						
						if(productListDataTemp!=null && productListDataTemp.size()>0){
							productListData = productListDataTemp;
						}
					}
				}
			}
			
			
			String approverID= (String) session.getAttribute("approvalUserId");
			if(approverID==null)
				approverID = "";
			
			
			 int approveUserId = 0;
			    ArrayList<Integer> approverList = UsersDAO.getApprovalUserList(userId);
			   if(approverList!=null && approverList.size()>0)
				   approveUserId = approverList.get(0);
			   
			String isGeneralUser = (String) session.getAttribute("isGeneralUser");
			String approveSenderid = "Y";
			if(isGeneralUser!=null && isGeneralUser.trim().equalsIgnoreCase("Y"))
			{
				if(approveUserId==0){
					
					approveSenderid="N";
					
				 
				}
				else if(approveUserId>0){
					approveSenderid="Y";
				}
			}
			if(CommonDBQuery.getSystemParamtersList().get("COUPON_APPLICABLE")!=null && CommonDBQuery.getSystemParamtersList().get("COUPON_APPLICABLE").trim().equalsIgnoreCase("Y")){
				session.setAttribute("cartSubTotalExt",total);
				String discountValue =  (String) session.getAttribute("discountValue");
				if(CommonUtility.validateString(discountValue) != ""){
				total = total - Double.parseDouble(discountValue);
				}
			}
			//String twoDecimalTotal = df.format(total);
			//cartTotal = Double.parseDouble(twoDecimalTotal);
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WITHOUT_ROUNDUP")).equalsIgnoreCase("Y")){
				cartTotal = total;
			}else{
				cartTotal = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(total));
			}
			
			contentObject.put("productListData", productListData);
			contentObject.put("partIdentifierQuantity", partIdentifierQuantity);
			contentObject.put("idList", idList);
			contentObject.put("cartSubTotal", cartTotal);
			contentObject.put("cartTotal", cartTotal);
			if(productListData != null && productListData.size() > 0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("EnableCSPConfigurator")).equalsIgnoreCase("Y")) {
				LogoCustomization logoCustomization = new LogoCustomization();
				buyingCompanyId = CommonUtility.validateNumber(session.getAttribute("buyingCompanyId").toString());
				contentObject = logoCustomization.setDesignDetails(productListData, userId, buyingCompanyId, contentObject);
			}
			System.out.println("CartTotal In GetShopping Cart : "+cartTotal);
			contentObject.put("approveSenderid", approveSenderid);
			contentObject.put("hookUrl", hookUrl);
			contentObject.put("shipVia", shipVia);
			contentObject.put("checkOciUser", checkOciUser);
			contentObject.put("customMessage", customMessage);
			contentObject.put("customFieldVal", customFieldVal);			
        }catch(SQLException e){
        	e.printStackTrace();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
        CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
        return contentObject;
	}
	//naveen
public static String updateAppCartGroupItemsDao(HttpSession session,ArrayList<Integer> shoppingCartId,String shoppingCartQty[], ArrayList<Integer> idList, int savedGroupId,int requestTokenId,String approveSenderid){
		
		String target = "UpdateListItem";
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }	
        
        try{
        	
        	
        	
        	ArrayList<UsersModel> approverGroupList = UsersDAO.getApproverGroupList(requestTokenId, CommonUtility.validateNumber(approveSenderid));
        	
        	 String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
        	 
        	 if(approverGroupList.size() > 0){
        		 
        		 int loginUserDeq = 0;
        		 ArrayList<Integer> listofPendingAppSequence = new ArrayList<Integer>();
        		 // Finding the login User Sequence Level 
        	 for(UsersModel obj : approverGroupList ){
        		 if(obj.getUserId() == CommonUtility.validateNumber(sessionUserId)){
        			 loginUserDeq =  CommonUtility.validateNumber(obj.getApproverSequence());
        		 }
        	 }
        	 
        	 //finding Next level of approvers
        	 for(UsersModel obj : approverGroupList ){
        		 int sequenceLevel = CommonUtility.validateNumber(obj.getApproverSequence());
        		 if(loginUserDeq != 0 && sequenceLevel >= loginUserDeq){
        			 listofPendingAppSequence.add(obj.getApproveCartGroupId());
        		 }
        		 
        	 }
        	 
        	 
        	 
				if(shoppingCartId!=null && shoppingCartId.size()>0 && idList!=null && listofPendingAppSequence.size() > 0)
				{
					 exitFromMainLoop:
					for(int k = 0; k<listofPendingAppSequence.size(); k++){
					
						 int deletedItems = 0;
					
						for(int i=0;i<shoppingCartId.size();i++)
						{
							for(int j=0;j<idList.size();j++)
							{
			 					if(idList.get(j).equals(shoppingCartId.get(i)))
								{
									int qty =1;
									if(!shoppingCartQty[i].trim().equalsIgnoreCase(""))
										qty = CommonUtility.validateNumber(shoppingCartQty[i]);
									if(qty==0){
										String sql = PropertyAction.SqlContainer.get("deleteAppGroupItemQuery");
										pstmt = conn.prepareStatement(sql);
										pstmt.setInt(1, idList.get(j));
										pstmt.setInt(2, listofPendingAppSequence.get(k));
										pstmt.setInt(3, requestTokenId);
										deletedItems++;
									}else{
										String sql = PropertyAction.SqlContainer.get("updateAppGroupItemQuery");
									pstmt = conn.prepareStatement(sql);
									pstmt.setInt(1, qty);
									pstmt.setInt(2, idList.get(j));
									pstmt.setInt(3, listofPendingAppSequence.get(k));
									pstmt.setInt(4, requestTokenId);
									}
									int count = pstmt.executeUpdate();
									ConnectionManager.closeDBResultSet(rs);
							    	ConnectionManager.closeDBPreparedStatement(pstmt);	
								}
							}
						}
					
						
						if(shoppingCartId!=null && shoppingCartId.size() == deletedItems){
							System.out.println("All the items deleted");
							String sql = PropertyAction.SqlContainer.get("deleteApprovalCartItemQuery");
							pstmt = conn.prepareStatement(sql);
							pstmt.setInt(1, requestTokenId);
							int count = pstmt.executeUpdate();
							if(count==0)
							{
								ConnectionManager.closeDBPreparedStatement(pstmt);	
								sql = PropertyAction.SqlContainer.get("deleteApprovalCartNameQuery");
								pstmt = conn.prepareStatement(sql);
								pstmt.setInt(1, requestTokenId);
								count = pstmt.executeUpdate();
							}
							
							 break exitFromMainLoop;
						}
						
					}
				
				}
				
				
		/*		if(shoppingCartId!=null && shoppingCartId.size() == deletedItems){
					System.out.println("All the items deleted");
					String sql = PropertyAction.SqlContainer.get("deleteSavedCartItemQuery");
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, savedGroupId);
					int count = pstmt.executeUpdate();
					if(count==0)
					{
						ConnectionManager.closeDBPreparedStatement(pstmt);	
						sql = PropertyAction.SqlContainer.get("deleteSavedCartNameQuery");
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, savedGroupId);
						count = pstmt.executeUpdate();
					}
				}*/
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
	    	ConnectionManager.closeDBConnection(conn);
	    }
		
		return target;
	}
public static String updateNextLevelAppCartGroupItems(HttpSession session,ArrayList<Integer> shoppingCartId,String shoppingCartQty[], ArrayList<Integer> idList, int savedGroupId,int requestTokenId,String approveSenderid){
	
	String target = "UpdateListItem";
	ResultSet rs = null;
    Connection  conn = null;
    PreparedStatement pstmt = null;
    
    try {
        conn = ConnectionManager.getDBConnection();
    } catch (SQLException e) { 
        e.printStackTrace();
    }	
    
    try{
    	
    	ArrayList<UsersModel> approverGroupList = UsersDAO.getApproverGroupList(requestTokenId, CommonUtility.validateNumber(approveSenderid));
    	
    	 String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
    	 
    	 if(approverGroupList.size() > 0){
    		 
    		 int loginUserDeq = 0;
    		 ArrayList<Integer> listofPendingAppSequence = new ArrayList<Integer>();
    		 // Finding the login User Sequence Level 
    	 for(UsersModel obj : approverGroupList ){
    		 if(obj.getUserId() == CommonUtility.validateNumber(sessionUserId)){
    			 loginUserDeq =  CommonUtility.validateNumber(obj.getApproverSequence());
    		 }
    	 }
    	 
    	 //finding Next level of approvers
    	 for(UsersModel obj : approverGroupList ){
    		 int sequenceLevel = CommonUtility.validateNumber(obj.getApproverSequence());
    		 if(loginUserDeq != 0 && sequenceLevel >= loginUserDeq){
    			 listofPendingAppSequence.add(obj.getApproveCartGroupId());
    		 }
    		 
    	 }
    	 
			if(shoppingCartId!=null && shoppingCartId.size()>0 && idList!=null && listofPendingAppSequence.size() > 0)
			{
				for(int k = 0; k<listofPendingAppSequence.size(); k++){
				
					 
					for(int i=0;i<shoppingCartId.size();i++)
					{
						
						if(idList.contains(shoppingCartId.get(i))){
							
							for(int j=0;j<idList.size();j++)
							{
			 					if(idList.get(j).equals(shoppingCartId.get(i)))
								{
									int qty =1;
									if(!shoppingCartQty[i].trim().equalsIgnoreCase(""))
										qty = CommonUtility.validateNumber(shoppingCartQty[i]);
									
										String sql = PropertyAction.SqlContainer.get("updateAppGroupItemQuery");
									pstmt = conn.prepareStatement(sql);
									pstmt.setInt(1, qty);
									pstmt.setInt(2, idList.get(j));
									pstmt.setInt(3, listofPendingAppSequence.get(k));
									pstmt.setInt(4, requestTokenId);
									
									int count = pstmt.executeUpdate();
									if(count >0)
									System.out.println("Item:"+shoppingCartId.get(i) +" has been updated");
									ConnectionManager.closeDBResultSet(rs);
							    	ConnectionManager.closeDBPreparedStatement(pstmt);	
								}
							}
							
						}else{
							String sql = PropertyAction.SqlContainer.get("deleteAppGroupItemQuery");
							pstmt = conn.prepareStatement(sql);
							pstmt.setInt(1, shoppingCartId.get(i));
							pstmt.setInt(2, listofPendingAppSequence.get(k));
							pstmt.setInt(3, requestTokenId);
							
							int count = pstmt.executeUpdate();
							if(count >0)
							System.out.println("Item:"+shoppingCartId.get(i) +" has been deleted");
							ConnectionManager.closeDBResultSet(rs);
					    	ConnectionManager.closeDBPreparedStatement(pstmt);
						}
						
					}
				
				}
			
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
    finally
    {
    	ConnectionManager.closeDBResultSet(rs);
    	ConnectionManager.closeDBPreparedStatement(pstmt);	
    	ConnectionManager.closeDBConnection(conn);
    }
	
	return target;
}



	
	
	
	/*public static ProductsModel getMinOrderQty(String itemPriceId){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductsModel minOrderQty = null;
		


		
        try
        {
        	conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("getMinOrderQtySql");
        	pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, itemPriceId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				minOrderQty = new ProductsModel();
				minOrderQty.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				minOrderQty.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
			}
        	
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return minOrderQty;
	}*/
	public static int deleteFromCart(int userId,int cartId){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = -1;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("deleteCartQuery");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, cartId);
			count = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}
	
	public static int deleteQuoteCartById(int quoteCartId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try{
			conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("deleteQuoteCart");
        	pstmt = conn.prepareStatement(sql);
        	pstmt.setInt(1, quoteCartId);
        	count = pstmt.executeUpdate();
        }
        catch(Exception e){
         	e.printStackTrace();
        }
        finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return count;
	}
	
	public static void deleteQuoteCart(String sessionId)
	{
		PreparedStatement pstmt = null;
		Connection conn = null;
		 
	        try
	        {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("clearQuoteCart");
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setString(1, sessionId);
	        	int count = pstmt.executeUpdate();
	        	if(count>0){
	        		System.out.println("Quote Cart Cleared");
	        	}else{
	        		System.out.println("Clear Quote Cart Failed");
	        	}
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally{
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
	}
	
	public static int updateQuoteCartDao(int cartId,int qty,String lineItemComment,String itemLevelRequiredDate){
		int count = -1;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("updateQuoteCartDao");
			pstmt = conn.prepareStatement(sql);
        	pstmt.setInt(1, qty);
        	pstmt.setInt(2, qty);
        	pstmt.setString(3, lineItemComment);
        	pstmt.setString(4, itemLevelRequiredDate);
        	pstmt.setInt(5, cartId);
        	count = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}

	
	public static int deleteFromQuoteCartDao(int cartId){
		
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = -1;

		
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "DELETE FROM QUOTE_CART WHERE QUOTE_CART_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cartId);
			count = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}
	
	public static int updateCartQuantity(ProductsModel productModel){
		
		int count = -1;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("updateCartQuantityOnlyQuery");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, productModel.getMinOrderQty());
			pstmt.setInt(2, productModel.getUserId());
			pstmt.setInt(3, productModel.getProductListId());
			count = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}
	
	public static int updateCart(int userId,int cartId,int qty,int cartQty,String line_item_comment,String itemLevelRequiredDate,LinkedHashMap<String, Object> utilityMap)
	{
		int count = -1;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean considerLineItemComment = true;
		String additionalProperties = "";
		try{
			if(utilityMap!=null) {
				if(utilityMap.get("considerLineItemComment")!=null) {
					considerLineItemComment = (boolean) utilityMap.get("considerLineItemComment");
				}
				if(utilityMap.get("additionalProperties") != null) {
					additionalProperties = (String) utilityMap.get("additionalProperties");
				}
			}
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("updateCartQuery");
			if(!considerLineItemComment) {
				sql = PropertyAction.SqlContainer.get("updateCartQueryWitoutLineItemComment");
				logger.debug("updateCartQueryWitoutLineItemComment Query: {} ",sql);
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, (cartQty+qty));
				pstmt.setString(2, itemLevelRequiredDate);
				pstmt.setInt(3, userId);
				pstmt.setInt(4, cartId);
			}else if(CommonUtility.validateString(additionalProperties).length() > 0) {
				sql = PropertyAction.SqlContainer.get("updateCartQueryWithAddtionalProps");
				logger.debug("updateCartQueryWithAddtionalProps Query: {} ",sql);
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, (cartQty+qty));
				pstmt.setString(2, line_item_comment);
				pstmt.setString(3, itemLevelRequiredDate);
				pstmt.setString(4, utilityMap.get("orderOrQuoteNumber") != null ? utilityMap.get("orderOrQuoteNumber").toString() : "");
				pstmt.setString(5, additionalProperties);
				pstmt.setInt(6, userId);
				pstmt.setInt(7, cartId);
			}else {
				logger.debug("updateCartQuery Query: {} ",sql);
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, (cartQty+qty));
				pstmt.setString(2, line_item_comment);
				pstmt.setString(3, itemLevelRequiredDate);
				pstmt.setString(4, utilityMap.get("orderOrQuoteNumber") != null ? utilityMap.get("orderOrQuoteNumber").toString() : "");
				pstmt.setInt(5, userId);
				pstmt.setInt(6, cartId);
			}
			count = pstmt.executeUpdate();
		}
		catch(SQLException sqlEx){
			logger.error(sqlEx.getMessage());
		}catch(Exception e)	{
			logger.error(e.getMessage());
		}
		finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}
	
	public static int updateCartBySessionId(String sessionId,int cartId,int qty,int cartQty,String line_item_comment,String itemLevelRequiredDate)
	{
		int count = -1;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("updateCartBySessionIdQuery");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (cartQty+qty));
			pstmt.setString(2, line_item_comment);
			pstmt.setString(3, itemLevelRequiredDate);
			pstmt.setString(4, sessionId);
			pstmt.setInt(5, cartId);
			count = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}
	
	public static int clearCartBySessionId(int userId, String sessionId){
		int count = -1;
		
		Connection  conn = null;
		PreparedStatement pstmt = null;
		
		
		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql="";
			if (CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT")).length() > 0 && CommonDBQuery.getSystemParamtersList().get("PARTIAL_CART_CHECKOUT").trim().equalsIgnoreCase("Y")) {
				sql = PropertyAction.SqlContainer.get("partialDeleteFromCartBySession");
			}else {
				sql = PropertyAction.SqlContainer.get("deleteFromCartBySession");
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setString(2, sessionId);
			count = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
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
		return count;
	}
	public static LinkedHashMap<String, ArrayList<ProductsModel>> getReviews(int itemId) { 
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			float rateCount =0;
			LinkedHashMap<String, ArrayList<ProductsModel>>  reviewDataList =new LinkedHashMap<String, ArrayList<ProductsModel>>();
			ArrayList<ProductsModel> reviewList = new ArrayList<ProductsModel>();
			ArrayList<ProductsModel> reviewListCount = new ArrayList<ProductsModel>();
			
			try
			{
				String sql = PropertyAction.SqlContainer.get("getReviews");
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, itemId);
				rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					ProductsModel review = new ProductsModel();
					review.setReviewId(rs.getString("REVIEW_ID"));
					review.setItemId(rs.getInt("ITEM_ID"));
					review.setComments(rs.getString("COMMENTS"));
					review.setRating(rs.getFloat("RATING"));
					review.setUserAdded(rs.getString("USER_NAME"));
					rateCount = rateCount + rs.getFloat("RATING");
					review.setTitle(rs.getString("TITLE"));
					
					String res[] = rs.getString("UPDATED_DATETIME").split(" ");
					review.setDate(res[0]);
					review.setReviewTime(res[1]);
					reviewList.add(review);
				}
				
				reviewDataList.put("reviewList", reviewList);
				
				ProductsModel review = new ProductsModel();
				review.setReviewCount(rateCount);
				reviewListCount.add(review);
				reviewDataList.put("reviewListCount", reviewListCount);
				System.out.println(rateCount); 
				
			}catch (Exception e) {
				
				e.printStackTrace();
				
			}finally{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return reviewDataList;
		}
	public static ProductsModel getTreeIdLevelNo(String itemPriceId,String taxonomyId)
	{
		String sql = "";
		ProductsModel taxonomyData = null;
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    

  
	    
	    try {
	    	conn = ConnectionManager.getDBConnection();
	    	sql = PropertyAction.SqlContainer.get("getTreeIdLevelNo");
	    	
	          pstmt=conn.prepareStatement(sql);
	          pstmt.setString(1, itemPriceId);
	          pstmt.setString(2, taxonomyId);
	          rs=pstmt.executeQuery();
	          if(rs.next())
	          {
	        	  taxonomyData = new ProductsModel();
	        	  taxonomyData.setCategoryCode(rs.getString("TAXONOMY_TREE_ID"));
	        	  taxonomyData.setLevelNumber(rs.getInt("LEVEL_NUMBER"));
	        	  
	          }
	          
	      } catch (Exception e) {
	    	  ULLog.errorTrace(e.fillInStackTrace());
	          e.printStackTrace();

	      } finally {
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
	      
	      return taxonomyData;
	}
	
	public static HashMap<String, ArrayList<ProductsModel>> itemDetailData(String taxonomyId,String eclipseSessionId,String userName,String entityId,String homeTeritory,int userId,String type,String wareHousecode,String customerId,String customerCountry)
	{
		CallableStatement stmt = null;
		ResultSet itemData = null;
		ResultSet itemImages = null;
		ResultSet itemAttributes = null;
		ResultSet itemDocuments = null;
		ResultSet productData = null;
		ResultSet linkedItems = null;
		Connection conn = null;

		double price = 0.00;
		double discountAmount = 0.00;



		HashMap<String, ArrayList<ProductsModel>> itemDetailList = new HashMap<String, ArrayList<ProductsModel>>();
		ArrayList<ProductsModel> itemDetailObject = null;
		ArrayList<String> partIdentifier = new ArrayList<String>();
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String homeTerritory = (String) session.getAttribute("shipBranchId");

		try{
			conn = ConnectionManager.getDBConnection();
			stmt = conn.prepareCall("{call GET_ITEM_DETAILS_IN_USE(?,?,?,?,?,?,?,?)}");
			stmt.setInt(1, CommonUtility.validateNumber(taxonomyId));
			stmt.registerOutParameter(2,OracleTypes.CURSOR);
			stmt.registerOutParameter(3,OracleTypes.CURSOR);
			stmt.registerOutParameter(4,OracleTypes.CURSOR);
			stmt.registerOutParameter(5,OracleTypes.CURSOR);
			stmt.registerOutParameter(6,OracleTypes.CURSOR);
			stmt.registerOutParameter(7,OracleTypes.CURSOR);
			stmt.registerOutParameter(8,java.sql.Types.INTEGER);
			stmt.execute();
			int queryResponse = (Integer) stmt.getObject(8);
			if(queryResponse!=0){
				if(stmt.getObject(2)!=null)
					itemData = (ResultSet) stmt.getObject(2);

				if(stmt.getObject(3)!=null)
					itemImages = (ResultSet) stmt.getObject(3);

				if(stmt.getObject(4)!=null)
					itemAttributes = (ResultSet) stmt.getObject(4);

				if(stmt.getObject(5)!=null)
					itemDocuments = (ResultSet) stmt.getObject(5);

				if(stmt.getObject(6)!=null)
					productData = (ResultSet) stmt.getObject(6);

				if(stmt.getObject(7)!=null)
					linkedItems = (ResultSet) stmt.getObject(7);

				itemDetailObject = new ArrayList<ProductsModel>();
				String displayPricing = "Y";
				int itemId = 0;
				while(itemData!=null && itemData.next())
				{
					ProductsModel detailData = new ProductsModel();

					String longDesc = "";
					int packageQty = 1;
					if(itemData.getString("LONG_DESC1")!=null)
						longDesc = longDesc + itemData.getString("LONG_DESC1");
					if(itemData.getString("LONG_DESC2")!=null)
						longDesc = longDesc + itemData.getString("LONG_DESC2");
					String itemUrl = itemData.getString("BRAND_NAME")+ itemData.getString("MANUFACTURER_PART_NUMBER");
					//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					itemUrl = itemUrl.replaceAll(" ","-");
					detailData.setItemUrl(itemUrl);

					displayPricing = CommonUtility.validateString(itemData.getString("DISPLAY_PRICING"));
					detailData.setDisplayPricing(displayPricing);
					itemId = itemData.getInt("ITEM_ID");
					detailData.setItemId(itemId);
					detailData.setBrandId(itemData.getInt("BRAND_ID"));
					detailData.setItemPriceId(itemData.getInt("ITEM_PRICE_ID"));
					detailData.setBrandName(itemData.getString("BRAND_NAME"));
					detailData.setBrandImage(itemData.getString("BRAND_IMAGE"));
					detailData.setPartNumber(itemData.getString("PART_NUMBER"));
					detailData.setManufacturerName(itemData.getString("MANUFACTURER_NAME"));
					detailData.setAltPartNumber1(itemData.getString("ALT_PART_NUMBER1"));
					detailData.setManufacturerId(itemData.getInt("MANUFACTURER_ID"));
					detailData.setCustomerPartNumber(itemData.getString("CUSTOMER_PART_NUMBER"));
					detailData.setManufacturerPartNumber(itemData.getString("MANUFACTURER_PART_NUMBER"));
					detailData.setItemFeature(itemData.getString("ITEM_FEATURES"));
					detailData.setUpc(itemData.getString("UPC"));
					detailData.setShortDesc(itemData.getString("SHORT_DESC"));
					detailData.setOverRidePriceRule(itemData.getString("OVERRIDE_PRICE_RULE"));
					if(itemData.getString("ITEM_MARKETING_DESC")!=null){
						detailData.setMarketingText(itemData.getString("ITEM_MARKETING_DESC"));
					}
					if(longDesc!=null && !longDesc.trim().equalsIgnoreCase("")){
						detailData.setLongDesc(longDesc);
					}
					detailData.setMetaDesc(itemData.getString("META_DESC"));
					detailData.setMetaKeyword(itemData.getString("META_KEYWORD"));
					detailData.setPageTitle(itemData.getString("PAGE_TITLE"));
					detailData.setPrice(itemData.getDouble("NET_PRICE"));
					//detailData.setImapPrice(itemData.getDouble("NET_PRICE"));
					detailData.setImapPrice(itemData.getDouble("IMAP_PRICE"));
					detailData.setIsImap(itemData.getString("IMAP"));
					detailData.setPackDesc(itemData.getString("PACK_DESC"));
					detailData.setUom(itemData.getString("SALES_UOM"));
					detailData.setSalesUom(itemData.getString("SALES_UOM"));
					detailData.setProductId(itemData.getInt("PRODUCT_ID"));
					detailData.setPackageFlag(itemData.getInt("PACKAGE_FLAG"));
					if(itemData.getInt("PACKAGE_QTY")>0){
						packageQty = itemData.getInt("PACKAGE_QTY");
					}
					detailData.setPackageQty(packageQty);
					detailData.setImageName(itemData.getString("IMAGE_NAME")==null?"NoImage.png":itemData.getString("IMAGE_NAME"));
					detailData.setNotes(itemData.getString("NOTES"));
					if(itemData.getInt("MIN_ORDER_QTY")>0){
						detailData.setMinOrderQty(itemData.getInt("MIN_ORDER_QTY"));
					}else{
						detailData.setMinOrderQty(1);
					}
					detailData.setOrderInterval(itemData.getInt("ORDER_QTY_INTERVAL"));
					detailData.setMaterialGroup(itemData.getString("MATERIAL_GROUP"));
					System.out.println("Material Group : " + itemData.getString("MATERIAL_GROUP"));
					detailData.setImageType(itemData.getString("IMAGE_TYPE"));
					detailData.setCollectionAttr(itemData.getString("COLLECTION"));
					if(itemData.getInt("SALES_QTY")>0){
						detailData.setSaleQty(itemData.getInt("SALES_QTY"));
					}else{
						detailData.setSaleQty(1);
					}
					detailData.setAvailQty(itemData.getInt("QTY_AVAILABLE"));
					detailData.setWeight(itemData.getDouble("WEIGHT"));
					detailData.setWeightUom(itemData.getString("WEIGHT_UOM"));
					detailData.setInvoiceDesc(CommonUtility.validateString(itemData.getString("INVOICE_DESC")));
					detailData.setClearance(CommonUtility.validateString(itemData.getString("CLEARANCE")));
					itemDetailObject.add(detailData);
				}
				itemDetailList.put("itemData", itemDetailObject);
				itemDetailObject = new ArrayList<ProductsModel>();
				while(itemImages!=null && itemImages.next())
				{
					ProductsModel detailData = new ProductsModel();
					detailData.setImageName(itemImages.getString("IMAGE_NAME"));
					detailData.setImageType(itemImages.getString("IMAGE_TYPE"));
					detailData.setImageFrom(itemImages.getString("IMAGE_FROM"));
					detailData.setImageCaption(itemImages.getString("CAPTION"));
					itemDetailObject.add(detailData);
				}
				itemDetailList.put("itemImages", itemDetailObject);
				itemDetailObject = new ArrayList<ProductsModel>();
				while(itemAttributes!=null && itemAttributes.next())
				{
					ProductsModel detailData = new ProductsModel();
					if(itemAttributes.getString("ATTRIBUTE_VALUE")!=null && !itemAttributes.getString("ATTRIBUTE_VALUE").trim().equalsIgnoreCase(""))
					{
						detailData.setAttrName(itemAttributes.getString("ATTRIBUTE_NAME"));
						detailData.setAttrValue(itemAttributes.getString("ATTRIBUTE_VALUE")+(itemAttributes.getString("ATTRIBUTE_UOM")==null?"":" "+itemAttributes.getString("ATTRIBUTE_UOM")));
						itemDetailObject.add(detailData);
						System.out.println(itemAttributes.getString("ATTRIBUTE_NAME") + " : " + itemAttributes.getString("ATTRIBUTE_VALUE"));
					}
				}
				itemDetailList.put("itemAttributes", itemDetailObject);
				itemDetailObject = new ArrayList<ProductsModel>();
				while(itemDocuments!=null && itemDocuments.next())
				{
					ProductsModel detailData = new ProductsModel();
					detailData.setDocFrom(itemDocuments.getString("DOC_FROM"));
					detailData.setDocName(itemDocuments.getString("DOCUMENT_NAME"));
					detailData.setDocType(itemDocuments.getString("DOCUMENT_TYPE"));
					detailData.setDocDesc(itemDocuments.getString("DOCUMENT_DESC"));
					detailData.setDocCategoryType(itemDocuments.getString("ITEM_DOCUMENTS_CATEGORY_NAME"));
					itemDetailObject.add(detailData);
				}
				itemDetailList.put("itemDocuments", itemDetailObject);
				itemDetailObject = new ArrayList<ProductsModel>();
				while(productData!=null && productData.next()){
					ProductsModel detailData = new ProductsModel();
					detailData.setProductFeature(CommonUtility.validateString(productData.getString("PRODUCT_FEATURES")));
					detailData.setMarketingText(CommonUtility.validateString(productData.getString("PRODUCT_MARKETING_DESC")));
					detailData.setLongDesc(CommonUtility.validateString(CommonUtility.validateString(productData.getString("PRODUCT_DESC1"))+" "+CommonUtility.validateString(productData.getString("PRODUCT_DESC2"))));
					detailData.setImageName(CommonUtility.validateString(productData.getString("IMAGE_NAME")));
					detailData.setImageType(CommonUtility.validateString(productData.getString("IMAGE_TYPE")));
					detailData.setProductName(CommonUtility.validateString(productData.getString("PRODUCT_NAME")));
					if(CommonUtility.validateString(productData.getString("PRODUCT_NAME")).length()>0){
						itemDetailObject.add(detailData);
					}
				}
				itemDetailList.put("productDataList", itemDetailObject);
				itemDetailObject = new ArrayList<ProductsModel>();
				while(linkedItems!=null && linkedItems.next())
				{
					ProductsModel detailData = new ProductsModel();
					detailData.setLinkTypeName(linkedItems.getString("LINK_TYPE_NAME"));
					detailData.setItemId(linkedItems.getInt("ITEM_ID"));
					ProductsModel tempData = itemDetailFilter(linkedItems.getString("ITEM_PRICE_ID"), eclipseSessionId);
					detailData.setShortDesc(linkedItems.getString("SHORT_DESC"));
					detailData.setImageType(linkedItems.getString("IMAGE_TYPE"));
					detailData.setImageName(linkedItems.getString("IMAGE_NAME"));
					detailData.setPageTitle(linkedItems.getString("PAGE_TITLE"));
					if(linkedItems.getInt("SALES_QTY")>0){
						detailData.setSaleQty(linkedItems.getInt("SALES_QTY"));
					}else{
						detailData.setSaleQty(1);
					}
					detailData.setPrice(linkedItems.getDouble("NET_PRICE"));
					detailData.setBrandName(tempData.getBrandName());
					detailData.setPartNumber(tempData.getPartNumber());
					partIdentifier.add(detailData.getPartNumber());
					partIdentifierQuantity.add(1); // Get Dynamic QTy if Necessary
					String itemUrl = tempData.getBrandName()+tempData.getManufacturerPartNumber();
					//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
					itemUrl = itemUrl.replaceAll(" ","-");
					detailData.setItemUrl(itemUrl);
					detailData.setAltPartNumber1(tempData.getAltPartNumber1());
					detailData.setItemPriceId(tempData.getItemPriceId());
					detailData.setItemId(tempData.getItemId());
					detailData.setManufacturerPartNumber(tempData.getManufacturerPartNumber());
					detailData.setManufacturerName(tempData.getManufacturerName());
					itemDetailObject.add(detailData);
				}
				//Price for Linked items
				/* 
	        	 if(itemDetailObject!=null && itemDetailObject.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
	 			{
	 				ProductManagement priceInquiry = new ProductManagementImpl();
	 				ProductManagementModel priceInquiryInput = new ProductManagementModel();
	 				priceInquiryInput.setEntityId(CommonUtility.validateNumber(entityId));
	 				priceInquiryInput.setHomeTerritory(homeTerritory);
	 				priceInquiryInput.setPartIdentifier(partIdentifier);
	 				priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
	 				priceInquiryInput.setRequiredAvailabilty("Y");
	 				priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
	 				priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
	 				priceInquiryInput.setSession(session);
	 				itemDetailObject = priceInquiry.priceInquiry(priceInquiryInput , itemDetailObject);

	 			}*/
				//Price for Linked items
				itemDetailList.put("linkedItems", itemDetailObject);

				if(itemId>0){
					ArrayList<ProductsModel> videoLinks = getProducgtVideoLinks(itemId);
					if(videoLinks!=null && videoLinks.size()>0){
						itemDetailList.put("videoLinks", videoLinks);
					}
				}

			}
		}catch (SQLException e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(itemData);
			ConnectionManager.closeDBResultSet(linkedItems);
			ConnectionManager.closeDBResultSet(productData);
			ConnectionManager.closeDBResultSet(itemDocuments);
			ConnectionManager.closeDBResultSet(itemAttributes);
			ConnectionManager.closeDBResultSet(itemImages);
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return itemDetailList;
	}
	
	public static ArrayList<ProductsModel> getProducgtVideoLinks(int itemId)
	{
		
		 ResultSet rs = null;
	     Connection  conn = null;
	     PreparedStatement pstmt = null;
	     ArrayList<ProductsModel> videoLinks = new ArrayList<ProductsModel>();
	     try
	     {
	    	 conn = ConnectionManager.getDBConnection();
	    	 String sql = PropertyAction.SqlContainer.get("getProductVideoLinks");
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setInt(1, itemId);
			 rs = pstmt.executeQuery();
			 while (rs.next()) {
				 ProductsModel vedioModel = new ProductsModel();
				 vedioModel.setVideoCaption(rs.getString("VIDEO_CAPTION"));
				 vedioModel.setVideoName(rs.getString("VIDEO_NAME"));
				 vedioModel.setVideoType(rs.getString("VIDEO_TYPE"));
				 videoLinks.add(vedioModel);
			 }
	     }
	     catch(Exception e)
	     {
	    	 e.printStackTrace();
	     }
	     finally{
	    	 ConnectionManager.closeDBResultSet(rs);
        	 ConnectionManager.closeDBPreparedStatement(pstmt);
        	 ConnectionManager.closeDBConnection(conn);
	     }
	     return videoLinks;
	}
	public static ProductsModel itemDetailFilter(String itemId,String eclipseSessionId)
	{
		 ResultSet rs = null;
	     Connection  conn = null;
	     PreparedStatement pstmt = null;
	     	
	       
	      	ProductsModel itemVal = null;
	      	try {
	      		conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("brandQuery");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, itemId);
				rs = pstmt.executeQuery();
				  
				while (rs.next()) {
					itemVal = new ProductsModel();
					itemVal.setItemId(rs.getInt("ITEM_ID"));
					itemVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
					itemVal.setPartNumber(rs.getString("PART_NUMBER"));
					itemVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
					itemVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
					itemVal.setBrandName(rs.getString("BRAND_NAME"));
					itemVal.setShortDesc(rs.getString("SHORT_DESC"));
					itemVal.setPrice(rs.getDouble("NET_PRICE"));	
					itemVal.setUom(rs.getString("SALES_UOM"));
					itemVal.setNotes(rs.getString("NOTES"));
					itemVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
		    	  ConnectionManager.closeDBResultSet(rs);
		    	  ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	  ConnectionManager.closeDBConnection(conn);	
		      } // finally
		      return itemVal;
	}
	
	
	public static ArrayList<ProductsModel> productListIdName(Integer userId, String groupType, int buyingCompanyId)
	{
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    ArrayList<ProductsModel> productListData = new ArrayList<ProductsModel>();
	    int siteId = 21;
	    try{
	    	
	    	if(CommonDBQuery.getGlobalSiteId()>0){
	    		siteId = CommonDBQuery.getGlobalSiteId();
	    	}
	    	conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("productListIdNameQuery");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, buyingCompanyId);
			pstmt.setString(3, groupType);
			pstmt.setInt(4, siteId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ProductsModel productListVal = new ProductsModel();
				productListVal.setProductListId(rs.getInt("SAVED_LIST_ID"));
				productListVal.setProductListName(rs.getString("SAVED_LIST_NAME"));
				productListData.add(productListVal);
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
	    	ConnectionManager.closeDBConnection(conn);
        }
	    return productListData;
	}
	
	public static int insertProductListItem(int listId,String listName,int userId,String productIdList,int itemQty,String uom){
		Connection  conn = null;
	    int savedListId = 0;
	    int count = 0;
	    int savedGroupId = 0;
	    int validateResponse =0;
	    ArrayList<Integer> savedListArr = new ArrayList<Integer>();
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        try
        {
        	listName= URLDecoder.decode(listName, "UTF-8");
        	System.out.println("Group name - " + listName);
        	int isGroupId = listId;
        	if(listId==0)
        	{
        		isGroupId = validateGroupName(conn, listName, userId, "P");
        	}
        	if(isGroupId == 0){
        		savedListId = CommonDBQuery.getSequenceId("SAVED_ITEM_LIST_SEQ");
        		savedGroupId = savedListId;
        		count = createGroupName(conn, savedListId, userId, "P", listName);
        		if(count > 0){
        			//count = insertItemToGroup(conn, savedListId, CommonUtility.validateNumber(productIdList),itemQty,"");
        			count = insertItemToGroup(conn, savedListId, CommonUtility.validateNumber(productIdList),itemQty,"","","",uom,"","", 0, 0.0);    				
    				validateResponse = 0;
        		}
        		else
        		{
        			validateResponse = 1;
        		}
        	}else{
        		listId = isGroupId;
        		savedGroupId = listId;
        		savedListArr = selectFromProductGroup(conn, listId, CommonUtility.validateNumber(productIdList));
        		if(savedListArr!=null && savedListArr.size()>0){
        			count = updateItemGroup(conn, savedListArr.get(0), itemQty, savedListArr.get(1));
        		}else{
        			count = insertItemToGroup(conn, listId, CommonUtility.validateNumber(productIdList),itemQty,"","","",uom,"","", 0, 0.0);        		}
        		validateResponse = 0;
        		
        	}
         }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBConnection(conn);
        }
        
        return savedGroupId;
	}
	public static int validateGroupName(Connection conn,String groupName,int userId,String type){
		int isGroupId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			String sql = PropertyAction.SqlContainer.get("validateGroupName");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, groupName.toUpperCase());
			pstmt.setInt(2, userId);
			pstmt.setString(3, type);
			rs = pstmt.executeQuery();
			if(rs.next())
				isGroupId = rs.getInt("SAVED_LIST_ID");
		}catch(SQLException e){
        	e.printStackTrace();
        }catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    }
		return isGroupId;
	}
	public static int createGroupName(Connection conn,int savedListId,int userId,String type,String groupName)
	{
		int count=0;
		PreparedStatement pstmt = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		int buyingCompanyId = CommonUtility.validateNumber((String) session.getAttribute("buyingCompanyId"));
		boolean insertBuyingCompany = false;
		try
		{
			int siteId = 0;
			if(CommonDBQuery.getGlobalSiteId()>0){
	    		siteId = CommonDBQuery.getGlobalSiteId();
			}
			String sql = PropertyAction.SqlContainer.get("insertProductGroupName");
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_CUSTOMER_LEVEL_PRODUCT_GROUP")).length() > 0 && CommonDBQuery.getSystemParamtersList().get("ENABLE_CUSTOMER_LEVEL_PRODUCT_GROUP").equalsIgnoreCase("Y") && CommonUtility.validateString(type).length() > 0 && type.equalsIgnoreCase("P")){
				  sql = PropertyAction.SqlContainer.get("insertProductGroupNameForCustomer");
				  insertBuyingCompany = true;
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, savedListId);
    		pstmt.setString(2, groupName);
    		pstmt.setInt(3, userId);
    		pstmt.setString(4, type);
    		pstmt.setInt(5, siteId);
    		if(insertBuyingCompany){
    			pstmt.setInt(6, buyingCompanyId);
    		}
    		count = pstmt.executeUpdate();
    		
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
		return count;
	}
	
	public static int insertItemToGroup(Connection conn,int savedListId,int itemId,int itemQty,String itemLevelRequiredByDate,String catalogId,String lineItemComment, String uom, String ItemLevelShipVia,String ItemLevelShipViaDesc, int BCAddressBookId, double itemPrice)
	{
		int count=0;
		PreparedStatement pstmt = null;
		try
		{
			String sql = PropertyAction.SqlContainer.get("insertItemToGroupQuery");
			//INSERT INTO SAVED_LIST_ITEMS (LIST_ITEM_ID,SAVED_LIST_ID,ITEM_ID,QTY,UPDATED_DATETIME,BC_ADDRESS_BOOK_ID,PRICE) VALUES (SAVED_LIST_ITEMS.NEXTVAL,?,?,?,SYSDATE,?,?);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, savedListId);
    		pstmt.setInt(2, itemId);
    		pstmt.setInt(3, itemQty);
    		pstmt.setString(4, itemLevelRequiredByDate);
    		pstmt.setString(5,catalogId);
    		pstmt.setString(6,lineItemComment);
    		pstmt.setString(7,ItemLevelShipVia);
    		pstmt.setString(8,ItemLevelShipViaDesc);
    		pstmt.setString(9,CommonUtility.validateString(uom));
    		pstmt.setInt(10, BCAddressBookId);
    		pstmt.setDouble(11, itemPrice);
    		
    		count = pstmt.executeUpdate();
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
		return count;
	}

	public static ArrayList<Integer> selectFromProductGroup(Connection conn,int savedId,int itemId)
	{
		ArrayList<Integer> savedListId = new ArrayList<Integer>();
		String sql = PropertyAction.SqlContainer.get("selectFromProductGroup");
		//SELECT LIST_ITEM_ID, QTY FROM SAVED_LIST_ITEMS WHERE SAVED_LIST_ID = ? AND ITEM_ID=?
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, savedId);
			pstmt.setInt(2, itemId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				savedListId.add(rs.getInt("LIST_ITEM_ID"));
				savedListId.add(rs.getInt("QTY"));
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
		return savedListId;
	}
	public static int updateItemGroup(Connection conn,int listItemId,int qty,int oldQty)
	{
		int count=0;
		PreparedStatement pstmt = null;
		try
		{
			String sql = PropertyAction.SqlContainer.get("updateProductGroup");
			//UPDATE SAVED_LIST_ITEMS SET QTY = ? WHERE LIST_ITEM_ID = ?
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (oldQty+qty));
    		pstmt.setInt(2, listItemId);
    		count = pstmt.executeUpdate();
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
		return count;
	}
	
	public static LinkedHashMap<String, Object> getSharedCartDataDao(HttpSession session, LinkedHashMap<String, Object> contentObject,int savedGroupId, int assignedShipTo, String approveSenderid,String updatedDate, String savedGroupName) {
		String target = "";
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    String msg = null;
	    String tempSubset = (String) session.getAttribute("userSubsetId");
	    int subsetId = CommonUtility.validateNumber(tempSubset);
	    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		ArrayList<String> partIdentifier = new ArrayList<String>();
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		
		String homeTerritory = (String) session.getAttribute("shipBranchId");
		ArrayList<ProductsModel> productListData = new ArrayList<ProductsModel>();
		target = "ProductListItem";

	
        
        String userName = (String) session.getAttribute(Global.USERNAME_KEY);
        try{
        	conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("sharedCartQuery");
        	String type = "";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, savedGroupId);
			pstmt.setInt(2, subsetId);
			pstmt.setInt(3, savedGroupId);
			pstmt.setInt(4, subsetId);
			pstmt.setInt(5, savedGroupId);
			pstmt.setInt(6, subsetId);
			System.out.println(pstmt);
			rs = pstmt.executeQuery();
			String groupName = "";
			
			while(rs.next()){
				
				int packageQty = 1;
				ProductsModel productListVal = new ProductsModel();
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
	        	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
	        	itemUrl = itemUrl.replaceAll(" ","-");
	        	productListVal.setItemUrl(itemUrl);
				productListVal.setItemId(rs.getInt("ITEM_ID"));
				productListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				productListVal.setPartNumber(rs.getString("PART_NUMBER"));
				productListVal.setProductListId(rs.getInt("LIST_ITEM_ID"));
				productListVal.setQty(rs.getInt("QTY"));
				productListVal.setShortDesc(rs.getString("SHORT_DESC"));
				productListVal.setManufacturerName(rs.getString("BRAND_NAME"));
				productListVal.setPrice(rs.getDouble("NET_PRICE"));
				productListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
				productListVal.setTotal(rs.getDouble("EXTPRICE"));
				productListVal.setUom(rs.getString("SALES_UOM"));
				productListVal.setSaleQty(rs.getInt("SALES_QTY"));
				partIdentifier.add(rs.getString("PART_NUMBER"));
				partIdentifierQuantity.add(rs.getInt("QTY"));
				
				productListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				productListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				productListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				productListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
				productListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				if(rs.getInt("PACKAGE_QTY")>0)
					packageQty = rs.getInt("PACKAGE_QTY");
				
				productListVal.setPackageFlag(rs.getInt("PACKAGE_FLAG"));
				productListVal.setPackageQty(packageQty);
			    msg= rs.getString("NOTES");
				groupName = rs.getString("SAVED_LIST_NAME");
				approveSenderid=rs.getString("SENT_BY_APPR_USER_ID");
				
				System.out.println("the notes is"+msg);
				productListData.add(productListVal);
			}
			String requiredAvailabilty = null;
			if(userId>1){
				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_AVAILABILITY_PRODUCT_LISTING");
			}else{
				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("BEFORE_LOGIN_AVAILABILITY_PRODUCT_LISTING");	
			}
			if(productListData!=null && productListData.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
					{
						ProductManagement priceInquiry = new ProductManagementImpl();
						ProductManagementModel priceInquiryInput = new ProductManagementModel();
						String entityId = (String) session.getAttribute("entityId");
						priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
						priceInquiryInput.setHomeTerritory(homeTerritory);
						priceInquiryInput.setPartIdentifier(productListData);
						priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
						priceInquiryInput.setRequiredAvailabilty("Y");
						priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
						priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
						priceInquiryInput.setSession(session);
						productListData = priceInquiry.priceInquiry(priceInquiryInput , productListData);
					}
			savedGroupName = groupName;
			contentObject.put("responseType", target);
			contentObject.put("productListData", productListData);
			contentObject.put("updatedDate", updatedDate);
			contentObject.put("assignedShipTo", assignedShipTo);
			contentObject.put("savedGroupId", savedGroupId);
			contentObject.put("approveSenderid", approveSenderid);
			contentObject.put("requiredAvailabilty", requiredAvailabilty);
			contentObject.put("savedGroupName", savedGroupName);
			
        }catch(SQLException e){
        	e.printStackTrace();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
		return contentObject;
	}
public static LinkedHashMap<String, Object> getProductListDataDao(HttpSession session, LinkedHashMap<String, Object> contentObject, int savedGroupId, int assignedShipTo, String approveSenderid, String updatedDate, String savedGroupName,int fromRow,int toRow,String sortBy, String reqType){
		
		String target = "";
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    String tempSubset = (String) session.getAttribute("userSubsetId");
	    int subsetId = CommonUtility.validateNumber(tempSubset);
	    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String homeTerritory = (String) session.getAttribute("shipBranchId");
		String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
		ArrayList<String> partIdentifier = new ArrayList<String>();
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		double total = 0.0;
		double cartTotal = 0.0;
		String createdDate="";
		DecimalFormat df = CommonUtility.getPricePrecision(session);
		String isGeneralUser = (String)session.getAttribute("isGeneralUser");
		int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
		//reqType = "A";
		try{
			if(ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"))>0){
	    		generalSubset = ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"));
	    	}
			
			String sortByQry = "ORDER BY MANUFACTURER_PART_NUMBER ASC ";
			ArrayList<ProductsModel> productListData = new ArrayList<ProductsModel>();
			target = "ProductListItem";
			String idList = "";
			String c = "";
			if(sortBy!=null && !sortBy.trim().equalsIgnoreCase(""))
		    {
		    	sortBy = sortBy.trim();
		    	
		    	
		    	if(sortBy.equalsIgnoreCase("DescA"))
		    	{
		    		sortByQry = " ORDER BY SHORT_DESC ASC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("DescD"))
		    	{
		    		sortByQry = " ORDER BY SHORT_DESC DESC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("MPNA"))
		    	{
		    		sortByQry = " ORDER BY MANUFACTURER_PART_NUMBER ASC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("MPND"))
		    	{
		    		sortByQry = " ORDER BY MANUFACTURER_PART_NUMBER DESC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("PartA"))
		    	{
		    		sortByQry = " ORDER BY PART_NUMBER ASC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("PartD"))
		    	{
		    		sortByQry = " ORDER BY PART_NUMBER DESC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("ProdNameA"))
		    	{
		    		sortByQry = " ORDER BY BRAND_NAME ASC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("ProdNameB"))
		    	{
		    		sortByQry = " ORDER BY BRAND_NAME DESC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("UpcA"))
		    	{
		    		sortByQry = " ORDER BY UPC ASC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("UpcD"))
		    	{
		    		sortByQry = " ORDER BY UPC DESC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("ManfD"))
		    	{
		    		sortByQry = " ORDER BY MANUFACTURER_NAME DESC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("ManfA"))
		    	{
		    		sortByQry = " ORDER BY MANUFACTURER_NAME ASC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("CPNAsc"))
		    	{
		    		sortByQry = " ORDER BY MANUFACTURER_PART_NUMBER ASC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("CPNDesc"))
		    	{
		    		sortByQry = " ORDER BY MANUFACTURER_PART_NUMBER DESC";
		    	}
		    	else if(sortBy.equalsIgnoreCase("ItemAddedASC"))
		    	{
		    		sortByQry = " ORDER BY UPDATED_DATETIME ASC NULLS FIRST";
		    	}
		    	else if(sortBy.equalsIgnoreCase("ItemAddedDesc"))
		    	{
		    		sortByQry = " ORDER BY UPDATED_DATETIME ASC NULLS FIRST";
		    	}
		    }
	        String userName = (String) session.getAttribute(Global.USERNAME_KEY);
        
 
        	 conn = ConnectionManager.getDBConnection();
        	String type = "";
        	String sql = "";
        	if(contentObject != null && contentObject.get("isShared")!=null && contentObject.get("isShared").toString().trim().equalsIgnoreCase("Y")){
        		//String prepend = "SELECT * FROM ( SELECT * FROM (SELECT LIST_ITEM_ID  AS CART_ID,SAVED_LIST_NAME,TYPE,STATUS,ITEM_ID,ITEM_PRICE_ID,SUBSET_ID,PAGE_TITLE,PART_NUMBER,SHORT_DESC,LONG_DESC1,BRAND_NAME,BRAND_IMAGE,MANUFACTURER_NAME,MATERIAL_GROUP,INVOICE_DESC,PACK_DESC,SALES_UOM,SALES_QTY,IMAGE_NAME,IMAGE_TYPE,NET_PRICE,QTY,SENT_BY_APPR_USER_ID,MIN_ORDER_QTY,ORDER_QTY_INTERVAL,PACKAGE_QTY,PACKAGE_FLAG,CUSTOMER_PART_NUMBER,MANUFACTURER_PART_NUMBER,EXTPRICE,LINE_ITEM_COMMENT,ITEM_LEVEL_SHIPVIA,ITEM_LEVEL_SHIPVIA_DESC,UPC,ROW_NUMBER() OVER ("+sortByQry+") RN,COUNT(*) OVER() RESULT_COUNT FROM (";
        		//String prepend = "SELECT * FROM ( SELECT * FROM (SELECT LIST_ITEM_ID  AS CART_ID,SAVED_LIST_NAME,TYPE,STATUS,ITEM_ID,ITEM_PRICE_ID,SUBSET_ID,PAGE_TITLE,PART_NUMBER,SHORT_DESC,LONG_DESC1,BRAND_NAME,BRAND_IMAGE,MANUFACTURER_NAME,MATERIAL_GROUP,INVOICE_DESC,PACK_DESC,SALES_UOM,SALES_QTY,IMAGE_NAME,IMAGE_TYPE,NET_PRICE,QTY,SENT_BY_APPR_USER_ID,MIN_ORDER_QTY,ORDER_QTY_INTERVAL,PACKAGE_QTY,PACKAGE_FLAG,CUSTOMER_PART_NUMBER,MANUFACTURER_PART_NUMBER,EXTPRICE,LINE_ITEM_COMMENT,ITEM_LEVEL_SHIPVIA,ITEM_LEVEL_SHIPVIA_DESC,ITEMLEVEL_REQUIREDBYDATE,UPC,ROW_NUMBER() OVER ("+sortByQry+") RN,COUNT(*) OVER() RESULT_COUNT FROM (";

        		//String prepend = "SELECT * FROM ( SELECT * FROM (SELECT LIST_ITEM_ID  AS CART_ID,UPDATED_DATETIME,SAVED_LIST_NAME,TYPE,STATUS,ITEM_ID,ITEM_PRICE_ID,SUBSET_ID,PAGE_TITLE,PART_NUMBER,ALT_PART_NUMBER1,SHORT_DESC,LONG_DESC1,BRAND_NAME,BRAND_IMAGE,MANUFACTURER_NAME,MANUFACTURER_ID,MATERIAL_GROUP,INVOICE_DESC,PACK_DESC,SALES_UOM,SALES_QTY,IMAGE_NAME,IMAGE_TYPE,OVERRIDE_PRICE_RULE,NET_PRICE,QTY,SENT_BY_APPR_USER_ID,MIN_ORDER_QTY,ORDER_QTY_INTERVAL,PACKAGE_QTY,PACKAGE_FLAG,CUSTOMER_PART_NUMBER,MANUFACTURER_PART_NUMBER,EXTPRICE,LINE_ITEM_COMMENT,ITEM_LEVEL_SHIPVIA,ITEM_LEVEL_SHIPVIA_DESC,ITEMLEVEL_REQUIREDBYDATE,CATALOG_ID,UPC,ROW_NUMBER() OVER ("+sortByQry+") RN,COUNT(*) OVER() RESULT_COUNT FROM (";
        		String prepend = "SELECT * FROM ( SELECT * FROM (SELECT LIST_ITEM_ID  AS CART_ID,UPDATED_DATETIME,SAVED_LIST_NAME,TYPE,STATUS,ASSIGNED_SHIP_TO,ITEM_ID,ITEM_PRICE_ID,SUBSET_ID,PAGE_TITLE,PART_NUMBER,ALT_PART_NUMBER1,ALT_PART_NUMBER2,SHORT_DESC,LONG_DESC1,BRAND_NAME,BRAND_IMAGE,MANUFACTURER_NAME,MANUFACTURER_ID,MATERIAL_GROUP,INVOICE_DESC,PACK_DESC,SALES_UOM,SALES_QTY,UOM,IMAGE_NAME,IMAGE_TYPE,OVERRIDE_PRICE_RULE,NET_PRICE,QTY,QTY_AVAILABLE,SENT_BY_APPR_USER_ID,MIN_ORDER_QTY,ORDER_QTY_INTERVAL,PACKAGE_QTY,PACKAGE_FLAG,CUSTOMER_PART_NUMBER,MANUFACTURER_PART_NUMBER,EXTPRICE,LINE_ITEM_COMMENT,ITEM_LEVEL_SHIPVIA,ITEM_LEVEL_SHIPVIA_DESC,ITEMLEVEL_REQUIREDBYDATE,CATALOG_ID,UPC,WEIGHT,ROW_NUMBER() OVER ("+sortByQry+") RN,COUNT(*) OVER() RESULT_COUNT,BC_ADDRESS_BOOK_ID FROM (";

        		String append = ")) "+sortByQry+" ) WHERE RN BETWEEN ? AND ?";
        		sql = PropertyAction.SqlContainer.get("selectGroupItemForSharedCart");
	        	sql = prepend + sql + append;
	        	pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, savedGroupId);
				pstmt.setInt(2, subsetId);
				pstmt.setInt(3, savedGroupId);
				pstmt.setInt(4, generalSubset);
				pstmt.setInt(5, savedGroupId);
				pstmt.setInt(6, subsetId);
				pstmt.setInt(7, fromRow);
				pstmt.setInt(8, toRow);
        	}else{
        		if(reqType!=null && CommonUtility.validateString(reqType).equalsIgnoreCase("GP")) {
        			sortByQry = sortByQry!=null?sortByQry:"";
        			String prepend = "SELECT * FROM ( SELECT * FROM (SELECT LIST_ITEM_ID  AS CART_ID,UPDATED_DATETIME,SAVED_LIST_NAME,TYPE,STATUS,ASSIGNED_SHIP_TO,ITEM_ID,ITEM_PRICE_ID,SUBSET_ID,PAGE_TITLE,PART_NUMBER,ALT_PART_NUMBER1,ALT_PART_NUMBER2,SHORT_DESC,LONG_DESC1,BRAND_NAME,BRAND_IMAGE,MANUFACTURER_NAME,MANUFACTURER_ID,MATERIAL_GROUP,INVOICE_DESC,PACK_DESC,UOM,SALES_UOM,SALES_QTY,IMAGE_NAME,IMAGE_TYPE,OVERRIDE_PRICE_RULE,NET_PRICE,QTY,QTY_AVAILABLE,SENT_BY_APPR_USER_ID,MIN_ORDER_QTY,ORDER_QTY_INTERVAL,PACKAGE_QTY,PACKAGE_FLAG,CUSTOMER_PART_NUMBER,MANUFACTURER_PART_NUMBER,EXTPRICE,LINE_ITEM_COMMENT,ITEM_LEVEL_SHIPVIA,ITEM_LEVEL_SHIPVIA_DESC,ITEMLEVEL_REQUIREDBYDATE,CATALOG_ID,UPC,WEIGHT,ROW_NUMBER() OVER ("+sortByQry+") RN,COUNT(*) OVER() RESULT_COUNT, BC_ADDRESS_BOOK_ID FROM (";
        			sql = PropertyAction.SqlContainer.get("selectPromotedProductGroupItems");
        			String append = ")) "+sortByQry+" ) WHERE RN BETWEEN ? AND ?";;
        		    sql = prepend + sql + append;
	    			pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, savedGroupId);
					pstmt.setInt(2, userId);
					pstmt.setInt(3, buyingCompanyId);
					pstmt.setInt(4, subsetId);
					pstmt.setInt(5, savedGroupId);
					pstmt.setInt(6, userId);
					pstmt.setInt(7, buyingCompanyId);
					pstmt.setInt(8, generalSubset);
					pstmt.setInt(9, savedGroupId);
					pstmt.setInt(10, subsetId);
					pstmt.setInt(11, fromRow);
					pstmt.setInt(12, toRow);
	        	}else {
	        		//String prepend = "SELECT * FROM ( SELECT * FROM (SELECT LIST_ITEM_ID  AS CART_ID,SAVED_LIST_NAME,TYPE,STATUS,ITEM_ID,ITEM_PRICE_ID,SUBSET_ID,PART_NUMBER,SHORT_DESC,LONG_DESC1,BRAND_NAME,BRAND_IMAGE,MATERIAL_GROUP,INVOICE_DESC,PACK_DESC,SALES_UOM,SALES_QTY,IMAGE_NAME,IMAGE_TYPE,NET_PRICE,QTY,SENT_BY_APPR_USER_ID,MIN_ORDER_QTY,ORDER_QTY_INTERVAL,PACKAGE_QTY,PACKAGE_FLAG,CUSTOMER_PART_NUMBER,MANUFACTURER_PART_NUMBER,EXTPRICE,LINE_ITEM_COMMENT,UPC,ROW_NUMBER() OVER ("+sortByQry+") RN,COUNT(*) OVER() RESULT_COUNT FROM (";
	        		//String prepend = "SELECT * FROM ( SELECT * FROM (SELECT LIST_ITEM_ID  AS CART_ID,SAVED_LIST_NAME,TYPE,STATUS,ITEM_ID,ITEM_PRICE_ID,SUBSET_ID,PAGE_TITLE,PART_NUMBER,SHORT_DESC,LONG_DESC1,BRAND_NAME,BRAND_IMAGE,MANUFACTURER_NAME,MATERIAL_GROUP,INVOICE_DESC,PACK_DESC,SALES_UOM,SALES_QTY,IMAGE_NAME,IMAGE_TYPE,NET_PRICE,QTY,SENT_BY_APPR_USER_ID,MIN_ORDER_QTY,ORDER_QTY_INTERVAL,PACKAGE_QTY,PACKAGE_FLAG,CUSTOMER_PART_NUMBER,MANUFACTURER_PART_NUMBER,EXTPRICE,LINE_ITEM_COMMENT,ITEM_LEVEL_SHIPVIA,ITEM_LEVEL_SHIPVIA_DESC,ITEMLEVEL_REQUIREDBYDATE,UPC,ROW_NUMBER() OVER ("+sortByQry+") RN,COUNT(*) OVER() RESULT_COUNT FROM (";
	        		
	        		reqType = "A";
	        		String prepend = "SELECT * FROM ( SELECT * FROM (SELECT LIST_ITEM_ID  AS CART_ID,UPDATED_DATETIME,SAVED_LIST_NAME,TYPE,STATUS,ASSIGNED_SHIP_TO,ITEM_ID,ITEM_PRICE_ID,SUBSET_ID,PAGE_TITLE,PART_NUMBER,ALT_PART_NUMBER1,ALT_PART_NUMBER2,SHORT_DESC,LONG_DESC1,BRAND_NAME,BRAND_IMAGE,MANUFACTURER_NAME,MANUFACTURER_ID,MATERIAL_GROUP,INVOICE_DESC,PACK_DESC,UOM,SALES_UOM,SALES_QTY,IMAGE_NAME,IMAGE_TYPE,OVERRIDE_PRICE_RULE,NET_PRICE,QTY,QTY_AVAILABLE,SENT_BY_APPR_USER_ID,MIN_ORDER_QTY,ORDER_QTY_INTERVAL,PACKAGE_QTY,PACKAGE_FLAG,CUSTOMER_PART_NUMBER,MANUFACTURER_PART_NUMBER,EXTPRICE,LINE_ITEM_COMMENT,ITEM_LEVEL_SHIPVIA,ITEM_LEVEL_SHIPVIA_DESC,ITEMLEVEL_REQUIREDBYDATE,CATALOG_ID,UPC,WEIGHT,ROW_NUMBER() OVER ("+sortByQry+") RN,COUNT(*) OVER() RESULT_COUNT,BC_ADDRESS_BOOK_ID FROM (";
	        		
	        		String append = ")) "+sortByQry+" ) WHERE RN BETWEEN ? AND ?";
	        		
		        	sql = PropertyAction.SqlContainer.get("selectGroupItem");
		        	if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_APPROVE_CART")).length() > 0 && CommonDBQuery.getSystemParamtersList().get("ENABLE_APPROVE_CART").equalsIgnoreCase("Y") &&  CommonUtility.validateString(reqType).equalsIgnoreCase("A") && CommonUtility.validateString(isGeneralUser).equalsIgnoreCase("Y")){
	        			sql = PropertyAction.SqlContainer.get("selectGroupItemByUser");
	        		}
		        	sql = prepend + sql + append;
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, savedGroupId);
					pstmt.setInt(2, userId);
					pstmt.setInt(3, buyingCompanyId);
					pstmt.setInt(4, subsetId);
					pstmt.setInt(5, activeTaxonomyId);
					pstmt.setInt(6, savedGroupId);
					pstmt.setInt(7, userId);
					pstmt.setInt(8, buyingCompanyId);
					pstmt.setInt(9, generalSubset);
					pstmt.setInt(10, activeTaxonomyId);
					pstmt.setInt(11, savedGroupId);
					pstmt.setInt(12, subsetId);
					pstmt.setInt(13, fromRow);
					pstmt.setInt(14, toRow);
	        	}
        	}
			
			
			
			System.out.println(pstmt);
			rs = pstmt.executeQuery();
			String groupName = "";
			while(rs.next()){
				ProductsModel productListVal = new ProductsModel();
				type = rs.getString("TYPE");
				int packageQty = 1;
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
            	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
            	itemUrl = itemUrl.replaceAll(" ","-");
            	productListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
            	productListVal.setItemUrl(itemUrl);
				productListVal.setItemId(rs.getInt("ITEM_ID"));
				productListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				productListVal.setPartNumber(rs.getString("PART_NUMBER"));
				productListVal.setProductListId(rs.getInt("CART_ID"));
				productListVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
				productListVal.setAltPartNumber2(rs.getString("ALT_PART_NUMBER2"));
				productListVal.setQty(rs.getInt("QTY"));
				productListVal.setAvailQty(rs.getInt("QTY_AVAILABLE"));
				productListVal.setShortDesc(rs.getString("SHORT_DESC"));
				productListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
				productListVal.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				productListVal.setBrandName(rs.getString("BRAND_NAME"));
				productListVal.setPrice(rs.getDouble("NET_PRICE"));
				productListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
				productListVal.setTotal(rs.getDouble("EXTPRICE"));
				if(CommonUtility.validateString(rs.getString("UOM")).length()>0){
					productListVal.setUom(rs.getString("UOM"));
				}else{
					productListVal.setUom(rs.getString("SALES_UOM"));
				}
				productListVal.setSalesUom(rs.getString("SALES_UOM"));
				productListVal.setImageType(rs.getString("IMAGE_TYPE"));
				productListVal.setSaleQty(rs.getInt("SALES_QTY"));
				productListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				productListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				productListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				productListVal.setPageTitle(rs.getString("PAGE_TITLE"));
				productListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
				productListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				productListVal.setOverRidePriceRule(rs.getString("OVERRIDE_PRICE_RULE"));
				if(rs.findColumn("CATALOG_ID")>0){
					productListVal.setCatalogId(rs.getString("CATALOG_ID"));	
				}
				if(rs.findColumn("ASSIGNED_SHIP_TO")>0) {
					productListVal.setAssignedShipTo(rs.getInt("ASSIGNED_SHIP_TO"));
				}
				productListVal.setMultipleShipVia(rs.getString("ITEM_LEVEL_SHIPVIA"));
				productListVal.setMultipleShipViaDesc(rs.getString("ITEM_LEVEL_SHIPVIA_DESC"));
				productListVal.setItemLevelRequiredByDate(rs.getString("ITEMLEVEL_REQUIREDBYDATE"));
				productListVal.setWeight(rs.getDouble("WEIGHT"));
				partIdentifier.add(productListVal.getPartNumber());
				partIdentifierQuantity.add(rs.getInt("QTY"));
				total = total + rs.getDouble("EXTPRICE");
				
				productListVal.setResultCount(rs.getInt("RESULT_COUNT"));
				if(rs.getInt("PACKAGE_QTY")>0)
					packageQty = rs.getInt("PACKAGE_QTY");
				
				idList = idList + c + rs.getInt("ITEM_ID");
				c = " OR ";
				
				productListVal.setPackageQty(packageQty);
				productListVal.setUpc(rs.getString("UPC"));
				groupName = rs.getString("SAVED_LIST_NAME");
				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(rs.getString("UPDATED_DATETIME"));
	            createdDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
				approveSenderid = rs.getString("SENT_BY_APPR_USER_ID");
				
				// get Customer Part Number List
				productListVal.setBcAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
				
				productListData.add(productListVal);
			}
			
			
			//-------------- ERP pricing here
			if(productListData!=null && productListData.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
			{
				LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = ProductHunterSolr.getcustomerPartnumber(idList, buyingCompanyId, buyingCompanyId);
				if(customerPartNumber!=null && customerPartNumber.size()>0)
				{
					for(ProductsModel item : productListData) {
						ArrayList<ProductsModel> eachCPN = customerPartNumber.get(item.getItemId());
						if(eachCPN!=null && eachCPN.size()>1){
							Collections.sort(eachCPN, ProductsModel.partNumberAscendingComparator);
							if(sortBy.equalsIgnoreCase("CPNDesc")){
								Collections.sort(eachCPN, ProductsModel.partNumberDescendingComparator);
							}
							item.setCustomerPartNumber(eachCPN.get(0).getPartNumber());
						}else if(eachCPN!=null && eachCPN.size()>0){
							item.setCustomerPartNumber(eachCPN.get(0).getPartNumber());
						}
						item.setCustomerPartNumberList(eachCPN);
					}
				}
				ProductManagement priceInquiry = new ProductManagementImpl();
				ProductManagementModel priceInquiryInput = new ProductManagementModel();
				String entityId = (String) session.getAttribute("entityId");
				priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
				priceInquiryInput.setHomeTerritory(homeTerritory);
				priceInquiryInput.setPartIdentifier(productListData);
				priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
				priceInquiryInput.setRequiredAvailabilty("Y");
				priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
				priceInquiryInput.setSession(session);
				if(CommonUtility.customServiceUtility()!=null) {
					 CommonUtility.customServiceUtility().setAllBranchAvailValueInGroupAndCart(priceInquiryInput);
				}
				productListData = priceInquiry.priceInquiry(priceInquiryInput , productListData);
			}

			if(type!=null)
			{
				if(type.equalsIgnoreCase("C")){
					target="SavedCartGroup";
				}
				if(type.equalsIgnoreCase("A")){
					target="ApproveCart";
				}
				if(type.equalsIgnoreCase("GP")) {
					target="PromotedProductsList";
				}
				contentObject.put("reqType",type);
			}
			
			
			if(assignedShipTo>0){
				sql = PropertyAction.SqlContainer.get("getUserName");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, CommonUtility.validateNumber(approveSenderid));
				rs = pstmt.executeQuery();
				while(rs.next()){
					userName=rs.getString("USER_NAME");
				}
				session.setAttribute("groupName", groupName);
				session.setAttribute("listUserName", userName);
				session.setAttribute("updatedDate", updatedDate);
				session.setAttribute("fromApproveCart", "Y");
				session.setAttribute("senderId", approveSenderid);
				
			}
			
			String requiredAvailabilty = null;
			if(target.equalsIgnoreCase("SavedCartGroup")){
				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_AVAILABILITY_SAVED_CART");
			}else if(target.equalsIgnoreCase("ProductListItem")){
				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_AVAILABILITY_PRODUCT_GROUP");
			}else{
				requiredAvailabilty = "N";
			}
       	
			String twoDecimalTotal = df.format(total);
        	cartTotal = Double.parseDouble(twoDecimalTotal);
			savedGroupName = groupName;
			if(contentObject!=null)
			{
			contentObject.put("Total", cartTotal);	
			contentObject.put("responseType", target);
			contentObject.put("productListData", productListData);
			contentObject.put("updatedDate", updatedDate);
			contentObject.put("assignedShipTo", assignedShipTo);
			contentObject.put("savedGroupId", savedGroupId);
			contentObject.put("approveSenderid", approveSenderid);
			contentObject.put("requiredAvailabilty", requiredAvailabilty);
			contentObject.put("savedGroupName", savedGroupName);
			contentObject.put("groupCreatedDate",createdDate);
			session.setAttribute("groupCreatedDate_"+savedGroupId, createdDate);
			}
			
        }catch(SQLException e){
        	e.printStackTrace();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
		return contentObject;
	}
	
	public static ProductsModel getMyProductGroupDetails(int savedGroupId, int userId) {
		ProductsModel result = new ProductsModel();
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    try {
            conn = ConnectionManager.getDBConnection();
	        String sql = "SELECT * FROM SAVED_ITEM_LIST WHERE SAVED_LIST_ID=? AND USER_ID=?";
	    	pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, savedGroupId);
			pstmt.setInt(2, userId);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				result.setGroupName(rs.getString("SAVED_LIST_NAME"));
				result.setLinkTypeName(rs.getString("TYPE"));
			}
			
	    }catch (SQLException e) { 
            e.printStackTrace();
        }finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return result;
	}
	
	public static String updateGroupItemsDao(String shoppingCartId[],String shoppingCartQty[], ArrayList<Integer> idList, int savedGroupId){
		
		String target = "UpdateListItem";
		ResultSet rs = null;
	    Connection  conn = null;
	    int deletedItems = 0;
	    PreparedStatement pstmt = null;
	    
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }	
        
        try{

			if(shoppingCartId!=null && shoppingCartId.length>0 && idList!=null)
			{
				for(int i=0;i<shoppingCartId.length;i++)
				{
					for(int j=0;j<idList.size();j++)
					{
	 					if(idList.get(j)==CommonUtility.validateNumber(shoppingCartId[i]))
						{
							int qty =1;
							if(!shoppingCartQty[i].trim().equalsIgnoreCase(""))
								qty = CommonUtility.validateNumber(shoppingCartQty[i]);
							if(qty==0){
								String sql = PropertyAction.SqlContainer.get("deleteGroupItemQuery");
								pstmt = conn.prepareStatement(sql);
								pstmt.setInt(1, idList.get(j));
								deletedItems++;
							}else{
								String sql = PropertyAction.SqlContainer.get("updateGroupItemQuery");
							pstmt = conn.prepareStatement(sql);
							pstmt.setInt(1, qty);
							pstmt.setInt(2, idList.get(j));
							}
							int count = pstmt.executeUpdate();
							ConnectionManager.closeDBResultSet(rs);
					    	ConnectionManager.closeDBPreparedStatement(pstmt);	
						}
					}
				}
			}
			if(shoppingCartId!=null && shoppingCartId.length == deletedItems){
				System.out.println("All the items deleted");
				String sql = PropertyAction.SqlContainer.get("deleteSavedCartItemQuery");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, savedGroupId);
				int count = pstmt.executeUpdate();
				if(count==0)
				{
					ConnectionManager.closeDBPreparedStatement(pstmt);	
					sql = PropertyAction.SqlContainer.get("deleteSavedCartNameQuery");
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, savedGroupId);
					count = pstmt.executeUpdate();
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
	    finally
	    {
	    	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
	    }
		
		return target;
	}
	
	public static void deleteSavedCartDao(int savedGroupId)
	{
		
		Connection  conn = null;
	    PreparedStatement pstmt = null;
	    int count = 0;

        try
        {
        	conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("deleteSavedCartItemQuery");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, savedGroupId);
			count = pstmt.executeUpdate();
			if(count>0)
			{
				ConnectionManager.closeDBPreparedStatement(pstmt);	
				sql = PropertyAction.SqlContainer.get("deleteSavedCartNameQuery");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, savedGroupId);
				count = pstmt.executeUpdate();
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
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
        
		
	}
	
	public static boolean deleteGroupItemDao(String shoppingCartId[], ArrayList<Integer> idList, int savedGroupId){
		Connection  conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    boolean flag = false;
	   
        try
        {
        conn = ConnectionManager.getDBConnection();
		String sql = PropertyAction.SqlContainer.get("deleteGroupItemQuery");
		if(shoppingCartId!=null && shoppingCartId.length>0 && idList!=null)
		{
			for(int i=0;i<shoppingCartId.length;i++)
			{
				for(int j=0;j<idList.size();j++)
				{
 					if(idList.get(j)==CommonUtility.validateNumber(shoppingCartId[i]))
					{
											
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, idList.get(j));
						int count = pstmt.executeUpdate();
						ConnectionManager.closeDBPreparedStatement(pstmt);	
					}
				}
			}
		}
		ConnectionManager.closeDBPreparedStatement(pstmt);
    	String checkForItemsSQL = PropertyAction.SqlContainer.get("checkForItemsSQL");
    	pstmt = conn.prepareStatement(checkForItemsSQL);
    	pstmt.setInt(1, savedGroupId);
    	rs = pstmt.executeQuery();
    	if(rs.next()){
    		System.out.println("data exists");
    		flag = true;
    	}else{
    		System.out.println("No data");
    		String deleteSQL = PropertyAction.SqlContainer.get("deleteSQL");
    		pstmt = conn.prepareStatement(deleteSQL);
        	pstmt.setInt(1, savedGroupId);
        	int deleted = pstmt.executeUpdate();
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	flag = false;
    	}
		
	}catch(SQLException e){
       	e.printStackTrace();
    }catch(Exception e){
       	e.printStackTrace();
    }
    finally{
    	ConnectionManager.closeDBResultSet(rs);
    	ConnectionManager.closeDBPreparedStatement(pstmt);	
    	ConnectionManager.closeDBConnection(conn);
    }
	return flag;
	}
	public static UsersModel getUserEmail(int userId)
	{
		UsersModel userDetail = null;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    String sql = "";
	
		try {
            conn = ConnectionManager.getDBConnection();
            sql = PropertyAction.SqlContainer.get("getEmailId");
          
            pstmt = conn.prepareStatement(sql);
             pstmt.setInt(1, userId);
           rs = pstmt.executeQuery();
           if(rs.next())
           {
        	   userDetail = new UsersModel();
        	   userDetail.setFirstName(rs.getString("FIRST_NAME"));
        	   userDetail.setLastName(rs.getString("LAST_NAME"));
        	   userDetail.setEmailAddress(rs.getString("EMAIL"));
           }
           
    } catch (SQLException e) { 
        e.printStackTrace();
    }
    finally {	    
    	  ConnectionManager.closeDBResultSet(rs);
    	  ConnectionManager.closeDBPreparedStatement(pstmt);
    	  ConnectionManager.closeDBConnection(conn);	
      }
		return userDetail;
	}
	public static String rejectApprCartDao(HttpSession session, int savedGroupId, String rejectReason){
		String result = "";
		ResultSet rs = null;
	    Connection  conn = null;
	    String sql ="";
	    PreparedStatement pstmt = null;
	    String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String updatedDate = (String) session.getAttribute("updatedDate");
		String senderID = (String) session.getAttribute("senderId");
		String savedGroupName = (String) session.getAttribute("groupName");
		int userId = CommonUtility.validateNumber(sessionUserId);
		UsersModel ApproverEmail = getUserEmail(CommonUtility.validateNumber(sessionUserId));
		int count = 0;
		try {
	        conn = ConnectionManager.getDBConnection();
	    } catch (SQLException e) { 
	        e.printStackTrace();
	    }	
	    try
	    {
	    	if(!rejectReason.equalsIgnoreCase("")){
		    	sql= PropertyAction.SqlContainer.get("updateRejectipnReason");
		    	pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, savedGroupId);
				pstmt.setInt(2, userId);
				count = pstmt.executeUpdate();
				if(count>0){
					ConnectionManager.closeDBPreparedStatement(pstmt);
			    	String sql1 = PropertyAction.SqlContainer.get("getEmailId");
					pstmt = conn.prepareStatement(sql1);
					pstmt.setString(1, senderID);
					rs = pstmt.executeQuery();
					while(rs.next())
					{
						SendMailUtility mailObj = new SendMailUtility();
						mailObj.sendApprovalMail(savedGroupId,savedGroupName,rs.getString("EMAIL"),rs.getString("FIRST_NAME"),rs.getString("LAST_NAME"),updatedDate,"Rejected",0,rejectReason,ApproverEmail.getEmailAddress());
						result = "1|Cart Rejected";
					}
			    	
				}
				else{
					result="0|Cart Rejection Failed. Please try after sometime";
				}
			}else{
				result = "0|Please Enter Reason for Rejection";
			}
	    }
	   catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
	    }
	    return result;
	}
	public static String addGroupToCartDao(String shoppingCartId[], String shoppingCartQty[], ArrayList<Integer> idList, ArrayList<Integer> lineItemIdList, int userId, String sessionId, String[] price){
		String result = "";
		ResultSet rs = null;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try
		{
			LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
			utilityMap.put("considerLineItemComment", true);
			conn = ConnectionManager.getDBConnection();
			if(shoppingCartId!=null && shoppingCartId.length>0 && idList!=null)
			{
				for(int i=0;i<shoppingCartId.length;i++)
				{
					for(int j=0;j<idList.size();j++)
					{
						if(idList.get(j)==CommonUtility.validateNumber(shoppingCartId[i]))
						{
							int qty =1;
							if(CommonUtility.validateNumber(shoppingCartQty[i]) > 0){
								qty = CommonUtility.validateNumber(shoppingCartQty[i]);
							}

							if(CommonUtility.validateNumber(shoppingCartQty[i]) > 0){
								ArrayList<Integer> cartId = new ArrayList<Integer>();
								cartId = selectFromCart(userId, lineItemIdList.get(i)," ");
								if(cartId!=null && cartId.size()>0){
									count = updateCart( userId,  cartId.get(0), qty, cartId.get(1),"","",utilityMap);
								}else{
									count = insertItemToCart( userId, lineItemIdList.get(i), qty, sessionId,null,"","",CommonUtility.validateString(price[i]),0, 0.0, 0.0,utilityMap);
								}	
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);	
			ConnectionManager.closeDBConnection(conn);
		}
		return result;
	}
	public static int deleteSavedGroup(int savedGroupId,String reqType){
		Connection  conn = null;
	    PreparedStatement pstmt=null;

	    int count = 0;
	    try{
	    	String sql = "";
	    	if(reqType!=null && reqType.trim().equalsIgnoreCase("AP")){
	    		sql ="DELETE PRODUCT_GROUPS_V2 WHERE PRODUCT_GROUP_ID = ?";
	    	}else{
	    		sql = PropertyAction.SqlContainer.get("deleteSavedCartItemQuery");	
	    	}
	    	
	    	conn = ConnectionManager.getDBConnection();
	    	pstmt = conn.prepareStatement(sql);
	    	pstmt.setInt(1, savedGroupId);
	    	count = pstmt.executeUpdate();
    		ConnectionManager.closeDBPreparedStatement(pstmt);	
    		if(reqType!=null && reqType.trim().equalsIgnoreCase("AP")){
	    		sql ="DELETE PRODUCT_GROUP_ITEMS_V2 WHERE PRODUCT_GROUP_ID = ?";
	    	}else{
	    		sql = PropertyAction.SqlContainer.get("deleteSavedCartNameQuery");	
	    	}
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setInt(1, savedGroupId);
    		count = pstmt.executeUpdate();
	    }catch(SQLException e){
	    	e.printStackTrace();
	    }
	    finally {
		    ConnectionManager.closeDBPreparedStatement(pstmt);
		    ConnectionManager.closeDBConnection(conn);	
	    }
		return 0;
	}
	public static int updateGroupName(String groupId,String newName,String userID,String oldGroupName,String GroupType){
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
	    int count = 0;

	    try {
	    		String sql = "";
	    		if(GroupType!=null && GroupType.trim().equalsIgnoreCase("AP")){
	    			sql= "SELECT * FROM PRODUCT_GROUPS_V2 WHERE CIMM_USER_ID=? AND PRODUCT_GROUP_NAME=?";
	    			conn = ConnectionManager.getDBConnection();
			    	pstmt = conn.prepareStatement(sql);
		            pstmt.setString(1, userID);
		    		pstmt.setString(2, newName);
		    	}else{
	    			sql = PropertyAction.SqlContainer.get("selectGroupName");
	    			conn = ConnectionManager.getDBConnection();
			    	pstmt = conn.prepareStatement(sql);
		            pstmt.setString(1, userID);
		    		pstmt.setString(2, newName);
		    		pstmt.setString(3, GroupType);
	    		}
		    	rs = pstmt.executeQuery();
    		if(rs.next())
            {
            	count = 3;
            }else{
            	String sql1 = "";
            	ConnectionManager.closeDBPreparedStatement(pstmt);
            	if(GroupType!=null && GroupType.trim().equalsIgnoreCase("AP")){
            		sql1 = "UPDATE PRODUCT_GROUPS_V2 SET PRODUCT_GROUP_NAME=? WHERE PRODUCT_GROUP_ID=?";
            	}else{
            		sql1 = PropertyAction.SqlContainer.get("updateGroupName");
            	}
            	pstmt = conn.prepareStatement(sql1);
	            pstmt.setString(1, newName);
	    		pstmt.setString(2, groupId);
	    		count = pstmt.executeUpdate();
            }
           
    } catch (SQLException e) { 
        e.printStackTrace();
    }finally {	    
    	ConnectionManager.closeDBResultSet(rs);
    	ConnectionManager.closeDBPreparedStatement(pstmt);
    	ConnectionManager.closeDBConnection(conn);	
      }
		return count;
		
	}
	public static boolean addItemReview(ProductsModel reviewDetails)
	{
		Connection  conn = null;
		PreparedStatement pstmt = null;
	    int reviewId = 0;
	    boolean flag =false;
        try
        {
        	conn = ConnectionManager.getDBConnection();
        	reviewId = CommonDBQuery.getSequenceId("REVIEW_ID_SEQ");
        	String sql = "INSERT INTO REVIEWS (REVIEW_ID,ITEM_ID,COMMENTS,RATING,STATUS,USER_EDITED,UPDATED_DATETIME,USER_ID,TITLE) VALUES ("+reviewId+",?,?,?,?,?,SYSDATE,?,?)";
        	//REVIEW_ID,ITEM_ID,COMMENTS,RATING,STATUS,USER_EDITED,UPDATED_DATETIME,USER_ID,TITLE
        	pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reviewDetails.getItemId());
    		pstmt.setString(2, reviewDetails.getComments());
    		pstmt.setFloat(3, reviewDetails.getRating());
    		pstmt.setString(4, null);
    		pstmt.setInt(5, CommonUtility.validateNumber(reviewDetails.getUserAdded()));
    		pstmt.setInt(6, CommonUtility.validateNumber(reviewDetails.getUserAdded()));
    		pstmt.setString(7, reviewDetails.getTitle());
    		
    		int count = pstmt.executeUpdate();
        		if(count==1){
        			flag = true;
        		}
        		
         }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
        
        return flag;
	}
	public static int  saveCartDao(int userId,String sessionId, String isReOrder, int listId, String listName, String groupType){
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
		
		int count = 0;
		int savedListId = 0;
	    int savedGroupId=0;
	    int validateResponse =0;
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }	
        
        try
        {
        	String sql = PropertyAction.SqlContainer.get("getFromCartQuery");
        	if(isReOrder!=null && isReOrder.trim().equalsIgnoreCase("Y")){
        		 sql = PropertyAction.SqlContainer.get("getFromQuoteCartQuery");
        	}
        	
        		  
        	
        	int isGroupId = listId;
        	if(listId==0){
        		isGroupId = validateGroupName(conn, listName, userId, groupType);
        	}
        	if(isGroupId == 0){
        		savedListId = CommonDBQuery.getSequenceId("SAVED_ITEM_LIST_SEQ");
        		savedGroupId = savedListId;
        		count = createGroupName(conn, savedListId, userId, groupType, listName);
        		if(count > 0){
        			pstmt = conn.prepareStatement(sql);
        			if(isReOrder!=null && isReOrder.trim().equalsIgnoreCase("Y")){
        				pstmt.setString(1, sessionId);
        			}else{
        				pstmt.setInt(1, userId);
        			}
        			
        			rs = pstmt.executeQuery();
        			while(rs.next())
        			{
        				count = insertItemToGroup(conn, savedListId, rs.getInt("ITEM_ID"),rs.getInt("QTY"),rs.getString("ITEMLEVEL_REQUIREDBYDATE"),rs.getString("CATALOG_ID"),rs.getString("LINE_ITEM_COMMENT"),rs.getString("UOM"),rs.getString("ITEM_LEVEL_SHIPVIA"),rs.getString("ITEM_LEVEL_SHIPVIA_DESC"), 0, 0);

        			}
        			validateResponse = 0;
        		}else{
        			validateResponse = 1;
        		}
        	}
        	else
        	{
        		listId = isGroupId;
        		savedGroupId = listId;
        		 ArrayList<Integer> savedListArr = new ArrayList<Integer>();
        		 pstmt = conn.prepareStatement(sql);
        		 if(isReOrder!=null && isReOrder.trim().equalsIgnoreCase("Y")){
     				pstmt.setString(1, sessionId);
	     		  }else{
	     				pstmt.setInt(1, userId);
	     		  }
     			rs = pstmt.executeQuery();
     			while(rs.next())
     			{
	         		
	         		savedListArr = selectFromProductGroup(conn, listId, rs.getInt("ITEM_ID"));
					if(savedListArr!=null && savedListArr.size()>0){
						count = updateItemGroup(conn, savedListArr.get(0), rs.getInt("QTY"), savedListArr.get(1));
					}else{
						count = insertItemToGroup(conn, listId, rs.getInt("ITEM_ID"),rs.getInt("QTY"),rs.getString("ITEMLEVEL_REQUIREDBYDATE"),rs.getString("CATALOG_ID"),rs.getString("LINE_ITEM_COMMENT"),rs.getString("UOM"),rs.getString("ITEM_LEVEL_SHIPVIA"), rs.getString("ITEM_LEVEL_SHIPVIA_DESC"), 0, 0.0);
 					
					}
     			}
        		validateResponse = 0;
        	}
	}
       catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
        }
		return savedGroupId;
	}
	public static int getSavedGroupIDByName(String groupName){
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    int listId = 0;
        try {
        	conn = ConnectionManager.getDBConnection();
        	String sql = ""; 
	        sql = PropertyAction.SqlContainer.get("getSavedGroupNameByID");
	        pstmt=conn.prepareStatement(sql);
	        pstmt.setString(1, groupName);
	        rs=pstmt.executeQuery();
	        if(rs.next())
	        {
	        	listId = rs.getInt("SAVED_LIST_ID");
	        }
	      } catch (Exception e) {
	    	  ULLog.errorTrace(e.fillInStackTrace());
	          e.printStackTrace();
	      } finally {
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
	     return listId;
	}
	public static int getSubsetIdFromName(String subsetName){
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    int SubsetID = 0;
        try {
        	conn = ConnectionManager.getDBConnection();
        	String sql = ""; 
	        sql = "SELECT SUBSET_ID FROM SUBSETS WHERE subset_name=?";
	        pstmt=conn.prepareStatement(sql);
	        pstmt.setString(1, subsetName);
	        rs=pstmt.executeQuery();
	        if(rs.next())
	        {
	        	SubsetID = rs.getInt("SUBSET_ID");
	        }
	      } catch (Exception e) {
	    	  ULLog.errorTrace(e.fillInStackTrace());
	          e.printStackTrace();
	      } finally {
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBPreparedStatement(pstmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
	     return SubsetID;
	}
	
	public static LinkedHashMap<String, Object> getSavedGroupNameByUserIdDao(int userId, LinkedHashMap<String, Object> contentObject, int buyingCompanyId){
		if(contentObject==null)
			contentObject = new LinkedHashMap<String, Object>();
		System.out.println("group name called");
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    LinkedHashMap<Integer,ArrayList<ProductsModel>> approverDetails = new LinkedHashMap<Integer,ArrayList<ProductsModel>> (); 
	    
	 	ArrayList<ProductsModel> groupListData = new ArrayList<ProductsModel>();
		ArrayList<ProductsModel> savedCartData = new ArrayList<ProductsModel>();
		ArrayList<ProductsModel> approveCartData = new ArrayList<ProductsModel>();
		String sortoprtion = null;
		int siteId = 21;
		HttpSession session = (HttpSession) contentObject.get("session");
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		String entityId = (String)session.getAttribute("entityId")!=null?(String) session.getAttribute("entityId"):"";
		if(CommonUtility.customServiceUtility() != null) {
			entityId = CommonUtility.customServiceUtility().getCustomerDefaultEntityId(buyingCompanyId, entityId, session);
		}
	    try{
	    	
	    	if(CommonDBQuery.getGlobalSiteId()>0){
	    		siteId = CommonDBQuery.getGlobalSiteId();
	    	}
	    	  
	    	 if(CommonDBQuery.getSystemParamtersList().get("SAVED_GROUP_SORT") !=null) {
	    		  sortoprtion = CommonDBQuery.getSystemParamtersList().get("SAVED_GROUP_SORT");
	    	 }else {
	    		  sortoprtion = "UPDATED_DATETIME DESC";
	    	 }
        	conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("getSavedAndSharedListNameQuery");
        	if(contentObject.get("listWithZeroItems") != null && CommonUtility.validateString((String)contentObject.get("listWithZeroItems")).equalsIgnoreCase("Y")) {
        		sql = PropertyAction.SqlContainer.get("getSavedAndSharedListNameCustomQuery");
    		}
//			/* sql = sql + " ORDER BY " + sortoprtion; */
        	System.out.println(sql);
        	pstmt = conn.prepareStatement(sql);
        	pstmt.setInt(1, siteId);
        	pstmt.setInt(2, userId);
        	pstmt.setInt(3, buyingCompanyId);
//			/* pstmt.setString(4, entityId); */
        	pstmt.setInt(4, siteId);
        	pstmt.setInt(5, userId);
			/*
			 * //pstmt.setString(7,sortoprtion);
			 */        
        	rs = pstmt.executeQuery();
        	
        	while(rs.next())
        	{
        		ProductsModel productVal = new ProductsModel();
        		productVal.setProductListId(rs.getInt("SAVED_LIST_ID"));
        		productVal.setProductListName(rs.getString("SAVED_LIST_NAME"));
        		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(rs.getString("UPDATED_DATETIME"));
                String updatedDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
                productVal.setDate(updatedDate);
        		productVal.setAssignedShipTo(rs.getInt("ASSIGNED_SHIP_TO"));
        		productVal.setStatus(rs.getString("STATUS"));
        		productVal.setIsSharedCart(rs.getString("IS_SHARED"));
        		if(rs.findColumn("ITEM_COUNTS")>0){
        			productVal.setGroupItemCount(rs.getString("ITEM_COUNTS"));	
        		}
				/*
				 * if(rs.getString("IS_PUBLIC")!=null) {
				 * productVal.setTitle(rs.getString("IS_PUBLIC")); }
				 */
        		if(rs.findColumn("REQ_TOKEN_ID")>0){
        			productVal.setRequestTokenId(rs.getInt("REQ_TOKEN_ID"));
        		}

				/*
				 * if(rs.getInt("USER_ID")>0) {
				 * productVal.setUserAdded(CommonUtility.validateParseIntegerToString(rs.getInt(
				 * "USER_ID")));
				 * productVal.setSharedBy(UsersDAO.getUsersName(rs.getInt("USER_ID"))); }
				 */

        		String senderUserID = rs.getString("SENT_BY_APPR_USER_ID");
        		if(CommonUtility.validateString(rs.getString("TYPE")).equalsIgnoreCase("P") || CommonUtility.validateString(rs.getString("TYPE")).equalsIgnoreCase("B")){
        			productVal.setGroupType(rs.getString("TYPE"));
        			groupListData.add(productVal);
        		}else if(rs.getString("TYPE").trim().equalsIgnoreCase("A") && rs.getString("STATUS").trim().equalsIgnoreCase("N")){
        			//if(sessionUserId.equalsIgnoreCase(approveUserId[0])){
    				productVal.setAppvalsenderUserID(senderUserID);
    				if(rs.getString("ALWAYS_APPROVE") != null){
    					productVal.setAlwaysApprover(rs.getString("ALWAYS_APPROVE"));
    				}
    				
    				if(rs.getString("APPROVER_SEQ") != null){
    					productVal.setApproverSequence(rs.getString("APPROVER_SEQ"));
    				}
    				
    				if(rs.getString("APPROVAL_STATUS") != null){
    					productVal.setApproverSequence(rs.getString("APPROVAL_STATUS"));
    				}
    				
    				////////////
    				
    				
    				int leastApprover = Integer.MAX_VALUE;
    				int pendingApprover = 0;
    				ArrayList<UsersModel> approverGroupList = new ArrayList<UsersModel>();
    				approverGroupList = UsersDAO.getApproverGroupList(rs.getInt("REQ_TOKEN_ID"), CommonUtility.validateNumber(rs.getString("SENT_BY_APPR_USER_ID")));// getting all APA sequence list List  
    				String loginUserSeq = null;
    				
    				ArrayList<UsersModel> pendingApproverList =  new ArrayList<UsersModel> ();
    				
    			
    				for(UsersModel userModel : approverGroupList){
    					
    					ArrayList<ProductsModel> userProductsList = new ArrayList<ProductsModel>();

    					// finding logged in user sequence
    					if(userModel.getUserId() == CommonUtility.validateNumber(sessionUserId)){
    						loginUserSeq = userModel.getApproverSequence();
    					}
    					
    					
    					//Keeping all approved and not approved details in linked hashmap 
    					if(userProductsList.size() > 0){
    						approverDetails.put(userModel.getUserId(), userProductsList);
    					}
    				}
    				
    				if(approverGroupList.size() > 0){
    					
    					ArrayList<UsersModel> filteredGroup = new ArrayList<UsersModel>();

    					// keeping all approver details except current login approver, to display status of all approver details.
    					for(UsersModel obj : approverGroupList){
    						if(obj.getUserId() != CommonUtility.validateNumber(sessionUserId) && loginUserSeq != null && !loginUserSeq.equals(obj.getApproverSequence())){
    							filteredGroup.add(obj);
    						}
    					}
    					
    					approverGroupList.clear();
    					approverGroupList = filteredGroup;
    					
    					for(UsersModel userModel : approverGroupList){
    						
    						if(userModel.getApprovalStatus().equalsIgnoreCase("N")){
    							pendingApproverList.add(userModel);
    							if(userModel.getUserId() != CommonUtility.validateNumber(sessionUserId) && loginUserSeq != null && !loginUserSeq.equals(userModel.getApproverSequence())){
    								pendingApprover = CommonUtility.validateNumber(userModel.getApproverSequence());
    							}
    							System.out.println("pendingApproverList:" + userModel.getUserName() +" " + userModel.getApproverSequence());
    							
    						}
    					}
    					//Finding the least approver behind the current log in user. For Ex: 1,2,3,4 login user sequence is  3 and nearest approver is 2. 
    					// if sequence 2 approver is not yet approved, then nearest approver is 1 for log in user sequence 3
    					for(UsersModel obj : pendingApproverList) {
    			             if(leastApprover > CommonUtility.validateNumber(obj.getApproverSequence())) {
    			            	 leastApprover = CommonUtility.validateNumber(obj.getApproverSequence());
    			            	 
    			             }
    			         }
    				}
    		
    				if(leastApprover != Integer.MAX_VALUE){
    					if(CommonUtility.validateString(loginUserSeq).length()>0){
    						if(leastApprover != Integer.MAX_VALUE && leastApprover > CommonUtility.validateNumber(loginUserSeq)){
    							approveCartData.add(productVal);
    						}
    					}
    				}else{
    					approveCartData.add(productVal);
    				}
        		//}
		}
else if(rs.getString("TYPE").trim().equalsIgnoreCase("C")){
        			savedCartData.add(productVal);
        		}
        		
        	}
        	if(CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP")!=null && CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP").trim().equalsIgnoreCase("Y")){
        		sql = "SELECT * FROM PRODUCT_GROUPS_V2 WHERE CIMM_USER_ID=?";
            	System.out.println(sql);
            	ConnectionManager.closeDBResultSet(rs);
            	ConnectionManager.closeDBPreparedStatement(pstmt);
            	pstmt = conn.prepareStatement(sql);
            	pstmt.setInt(1, userId);
            	rs = pstmt.executeQuery();
            	while(rs.next())
            	{
            		ProductsModel productVal = new ProductsModel();
            		productVal.setProductListId(rs.getInt("PRODUCT_GROUP_ID"));
            		productVal.setProductListName(rs.getString("PRODUCT_GROUP_NAME"));
            		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(rs.getString("UPDATED_DATETIME"));
                    String updatedDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
                    productVal.setDate(updatedDate);
            		productVal.setLevelNumber(rs.getInt("LEVEL_NUMBER"));
            		productVal.setLevel1Menu(rs.getString("PARENT_GROUP_ID"));
            		productVal.setImageName(rs.getString("IMAGE_NAME"));
            		productVal.setStatus(rs.getString("PUBLIC_PRODUCT_GROUP"));
            		groupListData.add(productVal);
            	}
        	}
        	
        	contentObject.put("groupListData", groupListData);
        	contentObject.put("savedCartData", savedCartData);
        	contentObject.put("approveCartData", approveCartData);
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
	    	ConnectionManager.closeDBConnection(conn);
        }
        System.out.println("group end called");
		return contentObject;
	}
	
	public static int rolbackCheckOutCart(int userId,String sessionId, int actualuserId)
	{
		Connection  conn = null;
		PreparedStatement pstmt = null;
		int count=-1;

        try
        {
        	conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("rolbackCheckOutCart");
        	pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,actualuserId);
			pstmt.setInt(2,userId);
			pstmt.setString(3, sessionId);
			count = pstmt.executeUpdate();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
		 
	}
public static LinkedHashMap<String, Object>  getQuoteCartDao(HttpSession session, LinkedHashMap<String, Object> contentObject){
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		double cartSubTotal = 0.0;
		DecimalFormat df = CommonUtility.getPricePrecision(session);
	     try{
	    	 conn = ConnectionManager.getDBConnection();
			//String sql = "SELECT QUOTE_CART_ID, PART_NUMBER, MANUFACTURER_PART_NUMBER, UPC, SHORT_DESC, LONG_DESC, PRICE, EXT_PRICE, SESSION_ID, ITEM_ID, UNSPSC, MATERIAL_GROUP, QTY, UOM FROM QUOTE_CART WHERE SESSION_ID = ?";
	    	// String sql = "SELECT QC.QUOTE_CART_ID, QC.PART_NUMBER, QC.MANUFACTURER_PART_NUMBER, QC.UPC, QC.SHORT_DESC, QC.LONG_DESC, QC.PRICE, QC.EXT_PRICE, QC.SESSION_ID, QC.ITEM_ID, QC.UNSPSC, QC.MATERIAL_GROUP, QC.QTY, QC.UOM,IDMV.BRAND_NAME, IDMV.MATERIAL_GROUP,  IDMV.INVOICE_DESC,  IDMV.PACK_DESC,IP.SALES_UOM,  IP.SALES_QTY,  IDMV.IMAGE_NAME,  IDMV.IMAGE_TYPE,IP.ITEM_PRICE_ID,IDMV.PAGE_TITLE,IDMV.MIN_ORDER_QTY,IDMV.ORDER_QTY_INTERVAL FROM QUOTE_CART QC, ITEM_DETAILS_MV IDMV, ITEM_PRICES IP  where QC.SESSION_ID = ?  AND IP.ITEM_PRICE_ID = IDMV.ITEM_PRICE_ID  AND QC.ITEM_ID = IP.ITEM_ID";
	    	String sql = PropertyAction.SqlContainer.get("getQuoteCartDao");
	    	
			pstmt = conn.prepareStatement(sql);
			//pstmt.setString(1, sessionId);
			pstmt.setString(1, session.getId());
			pstmt.setInt(2, CommonUtility.validateNumber(contentObject.get("subsetId").toString()));
			pstmt.setString(3, session.getId());
			pstmt.setInt(4, CommonUtility.validateNumber(contentObject.get("generalsubsetId").toString()));
			pstmt.setString(5, session.getId());
			pstmt.setInt(6, CommonUtility.validateNumber(contentObject.get("subsetId").toString()));
			double orderTotal = 0.0;
			double total = 0.0;
			rs = pstmt.executeQuery();	
			ArrayList<ProductsModel> productListData = new ArrayList<ProductsModel>();
			String homeTerritory = (String) session.getAttribute("shipBranchId");
			//ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
			ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
			//ArrayList<String> partIdentifier = new ArrayList<String>();
			ArrayList<Integer> itemList = new ArrayList<Integer>();

			int selectQuoteCartCount = 0;
			while(rs.next())
			{
				ProductsModel cartListVal = new ProductsModel();
				cartListVal.setProductListId(rs.getInt("QUOTE_CART_ID"));
				cartListVal.setQuoteCartId(rs.getInt("QUOTE_CART_ID"));
				cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
				cartListVal.setBrandName(rs.getString("BRAND_NAME"));
				cartListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
				cartListVal.setPartNumber(rs.getString("PART_NUMBER"));
				cartListVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
				cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
				cartListVal.setPrice(rs.getDouble("PRICE"));
				cartListVal.setTotal(rs.getDouble("EXT_PRICE"));
				cartListVal.setItemId(rs.getInt("ITEM_ID"));
				cartListVal.setUnspc(rs.getString("UNSPSC"));
				cartListVal.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
				cartListVal.setQty(rs.getInt("QTY"));
				if(CommonUtility.checkDBColumn(rs, "GET_PRICE_FROM")){
					cartListVal.setGetPriceFrom(rs.getString("GET_PRICE_FROM"));
				}
				partIdentifierQuantity.add(rs.getInt("QTY"));
				if(rs.getInt("QTY")==0){
					cartListVal.setQty(rs.getInt("SALES_QTY"));
					partIdentifierQuantity.add(rs.getInt("SALES_QTY"));
				}
				if(rs.findColumn("UOM")>0 && rs.getString("UOM")!=null){
					cartListVal.setUom(rs.getString("UOM"));
				}
				cartListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
				cartListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				cartListVal.setManufacturerName(rs.getString("BRAND_NAME"));
				cartListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				cartListVal.setImageType(rs.getString("IMAGE_TYPE"));
				cartListVal.setPageTitle(rs.getString("PAGE_TITLE"));
				cartListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				cartListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				cartListVal.setMultipleShipVia(rs.getString("ITEM_LEVEL_SHIPVIA"));
				cartListVal.setMultipleShipViaDesc(rs.getString("ITEM_LEVEL_SHIPVIA_DESC"));
				cartListVal.setOverRidePriceRule(rs.getString("OVERRIDE_PRICE_RULE"));
				cartListVal.setItemLevelRequiredByDate(rs.getString("ITEMLEVEL_REQUIREDBYDATE"));
				cartListVal.setDisplayPrice(rs.getString("DISPLAY_PRICING"));

				itemList.add(rs.getInt("ITEM_ID"));
				
				total = total + rs.getDouble("EXT_PRICE");
				
				productListData.add(cartListVal);
				selectQuoteCartCount++;
			}
			
			
			orderTotal = total;
			if(productListData!=null && productListData.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
			{
				
				ProductManagement priceInquiry = new ProductManagementImpl();
				ProductManagementModel priceInquiryInput = new ProductManagementModel();
				String entityId = (String) session.getAttribute("entityId");
				priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
				priceInquiryInput.setHomeTerritory(homeTerritory);
				priceInquiryInput.setPartIdentifier(productListData);
				priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
				priceInquiryInput.setRequiredAvailabilty("Y");
				priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
				priceInquiryInput.setSession(session);
				productListData = priceInquiry.priceInquiry(priceInquiryInput , productListData);
				total = productListData.get(0).getCartTotal();
			}
			
			
			
			String twoDecimalTotal = df.format(total);
			cartSubTotal = Double.parseDouble(twoDecimalTotal);
			
			LinkedHashMap<Integer, LinkedHashMap<String, Object>> customFieldVal = null;
			if(CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ITEM_CUSTOM_FIELDS").trim().equalsIgnoreCase("Y")){
				customFieldVal = ProductHunterSolr.getCustomFieldValuesByItems(CommonUtility.validateNumber(contentObject.get("subsetId").toString()), CommonUtility.validateNumber(contentObject.get("generalsubsetId").toString()), StringUtils.join(itemList," OR "),"itemid");
			}

			contentObject.put("customFieldVal", customFieldVal);
			contentObject.put("selectQuoteCartCount", selectQuoteCartCount);
			contentObject.put("cartSubTotal", cartSubTotal);
			contentObject.put("cartTotal", cartSubTotal);
			contentObject.put("productListData", productListData);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			 	ConnectionManager.closeDBResultSet(rs);
	        	ConnectionManager.closeDBPreparedStatement(pstmt);
	        	ConnectionManager.closeDBConnection(conn);	
		 }
		 return contentObject;
	}
	public static String performShareCart(String sharedUserIds[],int userId,int savedGroupId,String savedGroupName,String msg) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SharedBy="";
		int count =0;
		String result = "";
		UsersModel SharedByEmail= null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			if(sharedUserIds!=null && sharedUserIds.length>0 && savedGroupId > 0)
			{
				SharedBy=UsersDAO.getUsersName(userId);
				SharedByEmail = UsersDAO.getUserEmail(userId);
			for(String tempSharedUserId :  sharedUserIds)
			{
				count = UsersDAO.shareUserSavedCart(savedGroupId, CommonUtility.validateNumber(tempSharedUserId), userId,msg);
				String sql1 = "SELECT EMAIL,FIRST_NAME,LAST_NAME FROM CIMM_USERS WHERE USER_ID=?";
				pstmt = conn.prepareStatement(sql1);
				pstmt.setString(1, tempSharedUserId);
				rs = pstmt.executeQuery();
				SendMailUtility sendMailUtility = new SendMailUtility();
				while(rs.next()){
					sendMailUtility.sendSharedCartInfoMail(savedGroupName,rs.getString("FIRST_NAME"),rs.getString("LAST_NAME"),rs.getString("EMAIL"),SharedBy,SharedByEmail.getEmailAddress(),msg);
				}
				ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
			}
			
			if(count > 0 && (CommonUtility.validateString(msg).length()>0 && msg.equalsIgnoreCase("fromShoppingListPage")))
			{
				System.out.println("successfully Shared");
				result = "Shopping List Shared Successfully.|";
			}
			else if(count > 0)
			{
				System.out.println("successfully Shared");
				result = "Saved Cart Shared Successfully.|";
			}
			else{
				result = "Unable To Share Cart This Time Please Try After Some Time. |";
			}
			}else{
				result = "Unable To Share Cart This Time Please Try After Some Time. |";
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
		
		return result;
	}
	public static int createApproveCartName(Connection conn,int savedListId,int userId,String type,String groupName, int shipToAssigned, int userID) {
		int count=0;
		PreparedStatement pstmt = null;
		try
		{
			int siteId = 0;
			if(CommonDBQuery.getGlobalSiteId()>0){
	    		siteId = CommonDBQuery.getGlobalSiteId();
			}
			String sql = PropertyAction.SqlContainer.get("insertApproveCartName");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, savedListId);
    		pstmt.setString(2, groupName);
    		pstmt.setInt(3, userId);
    		pstmt.setString(4, type);
    		pstmt.setInt(5, shipToAssigned);
    		pstmt.setInt(6, userID);
    		pstmt.setInt(7, siteId);
    		count = pstmt.executeUpdate();
    		
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
		return count;
	}
	public static void deleteFromCart(Connection conn, int userId)
	{
		    PreparedStatement pstmt = null;
		    try {
		    	String sql = PropertyAction.SqlContainer.get("deleteFromCart");
		    	pstmt = conn.prepareStatement(sql);
		    	pstmt.setInt(1, userId);
		    	int count = pstmt.executeUpdate();
		    	
		    } catch (SQLException e) { 
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
	}
	
	public static ArrayList<ProductsModel> getRfqRefKey(int userId, int fromRow, int toRow)
	{
		
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    ArrayList<ProductsModel> referenceList = null;
	  


        try
        {
        	conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getRfqRefKey");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, fromRow);
			pstmt.setInt(3, toRow);
			rs = pstmt.executeQuery();
			referenceList = new ArrayList<ProductsModel>();
			while(rs.next())
			{
				ProductsModel refVal = new ProductsModel();
				refVal.setReferenceKey(rs.getString("REFERENCE_KEY"));
				refVal.setStatus(rs.getString("ORDER_STATUS"));
				refVal.setResultCount(rs.getInt("RESULT_COUNT"));
				referenceList.add(refVal);
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
	    	ConnectionManager.closeDBConnection(conn);
        }
		return referenceList;
	}
	
	public static ArrayList<String> getbrandListIndex(int subsetId, int gernralCatalogId)
	{
		ArrayList<String> indexList = new ArrayList<String>();
		Connection conn = null;
		String sql = "";
		
		//SELECT LIST_ITEM_ID, QTY FROM SAVED_LIST_ITEMS WHERE SAVED_LIST_ID = ? AND ITEM_ID=?
		PreparedStatement pstmt = null;
		ResultSet rs = null;


		try
		{
			conn = ConnectionManager.getDBConnection();
			
			if(gernralCatalogId>0){
				sql = PropertyAction.SqlContainer.get("brandAlphaIndexQueryWithGenralSubset");
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOM_BRAND_INDEX_QUERY_FOR_GENERAL_SUBSET")).length()>0){
					sql = PropertyAction.SqlContainer.get(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOM_BRAND_INDEX_QUERY_FOR_GENERAL_SUBSET")));
					pstmt = conn.prepareStatement(sql);
				}else{
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, subsetId);
					pstmt.setInt(2, gernralCatalogId);
				}
			}else{
				sql = PropertyAction.SqlContainer.get("brandAlphaIndexQuery");
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOM_BRAND_INDEX_QUERY")).length()>0){
					sql = PropertyAction.SqlContainer.get(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CUSTOM_BRAND_INDEX_QUERY")));
					pstmt = conn.prepareStatement(sql);
				}else{
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, subsetId);
				}
				
			}
			
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				indexList.add(rs.getString("BRAND_NAME"));
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
	    	ConnectionManager.closeDBConnection(conn);
		}
		return indexList;
	}
	public static LinkedHashMap<String, Object> getAdvancedProductListDataDao(HttpSession session, LinkedHashMap<String, Object> contentObject,int savedGroupId, int assignedShipTo, String approveSenderid,String updatedDate, String savedGroupName) {
		
		
		return contentObject;
	}
	public static int insertAdvancedProductListItem(int listId,String listName, int userId, String productIdList, int itemQty,String levelNo,int parentId,int itemId) {
		Connection  conn = null;
	    int savedListId = 0;
	    int count = 0;
	    int savedGroupId = 0;
	    int validateResponse =0;
	    ArrayList<Integer> savedListArr = new ArrayList<Integer>();
	    try {
            conn = ConnectionManager.getDBConnection();
            int isGroupId = listId;
            if(listId==0)
        	{
        		isGroupId = validateAdvancedProductGroupName(conn, listName, userId, "P",CommonUtility.validateNumber(levelNo));
        	}
            if(isGroupId == 0){
            	savedListId = CommonDBQuery.getSequenceId("PRODUCT_GROUPS_V2_SEQ");
            	savedGroupId = savedListId;
            	count = createAdvancedProductGroupName(conn, savedListId, userId, levelNo, listName,parentId);
            	validateResponse = 1;
            }else{
            	listId = isGroupId;
        		savedGroupId = listId;
        		savedListArr = selectFromAdvancedProductGroup(conn, listId, CommonUtility.validateNumber(productIdList));
        		if(savedListArr!=null && savedListArr.size()>0){
        			count = updateItemAdvancedGroup(conn, savedListArr.get(0), itemQty, savedListArr.get(1),listId,itemId,CommonUtility.validateNumber(productIdList));
        		}else{
        				count = insertItemToAdvancedGroup(conn, listId, CommonUtility.validateNumber(productIdList),itemQty,itemId);
            			validateResponse = 0;
        		}
            }
        }catch (SQLException e) { 
            e.printStackTrace();
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	ConnectionManager.closeDBConnection(conn);
        }
		return savedGroupId;
	}
	
	
	private static int insertItemToAdvancedGroup(Connection conn,int savedListId,int itemPriceId,int itemQty,int itemId) {
		int count=0;
		PreparedStatement pstmt = null;
		HttpSolrServer server = null;
		try
		{
			int savedListItemId = CommonDBQuery.getSequenceId("PRODUCT_GROUPS_ITEMS_V2_SEQ");
			String sql = "INSERT INTO PRODUCT_GROUP_ITEMS_V2(PRODUCT_GROUP_ITEM_ID,PRODUCT_GROUP_ID,ITEM_PRICE_ID,QUANTITY,UPDATED_DATETIME)VALUES(?,?,?,?,SYSDATE)";
			//INSERT INTO SAVED_LIST_ITEMS (LIST_ITEM_ID,SAVED_LIST_ID,ITEM_ID,QTY,UPDATED_DATETIME) VALUES (SAVED_LIST_ITEMS.NEXTVAL,?,?,?,SYSDATE);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, savedListItemId);
			pstmt.setInt(2, savedListId);
    		pstmt.setInt(3, itemPriceId);
    		pstmt.setInt(4, itemQty);
    		count = pstmt.executeUpdate();
    		if(count>0){
    			
    			String solrURL = CommonDBQuery.getSystemParamtersList().get("SOLR_URL");
    			server = ConnectionManager.getSolrClientConnection(solrURL+"/productgroupitems");
    			SolrInputDocument doc = new SolrInputDocument();
    			doc.addField("id", savedListItemId);
    			doc.addField("categoryID", savedListId);
    			getSolrItemDetail(itemId, doc);
    			
    			doc.addField("itemPriceId", itemPriceId);
    			doc.addField("qty", itemQty);
    			server.add(doc);
    			server.commit(); 
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
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
        	ConnectionManager.closeSolrClientConnection(server);
        }
		return count;
	}
	
	
	public static ProductsModel getSolrItemDetail(int itemId,SolrInputDocument doc2)
	{

		ProductsModel itemModel = new ProductsModel();
		HttpSolrServer server = null;
		try
		{
			server = ConnectionManager.getSolrClientConnection(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/mainitemdata");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			String fq= "itemid:"+itemId;
			int resultCount = 0;
			query.setQuery("*:*");
			//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
			String attrFtr[] = fq.split("~");
			query.setStart(0);
			query.setRows(1);
			
			query.addSortField("displaySeq", SolrQuery.ORDER.asc );
			query.setFilterQueries(attrFtr);
			
			QueryResponse response = server.query(query);
			System.out.println("Category Query : " + query);
			
			SolrDocumentList documents = response.getResults();
			resultCount = (int) response.getResults().getNumFound();
			Iterator<SolrDocument> itr = documents.iterator();
			
			while (itr.hasNext()) {
				SolrDocument doc = itr.next();
					doc2.addField("partnumber", doc.getFieldValue("partnumber").toString());
				if(doc.getFieldValue("imageName")!=null)
					doc2.addField("imageName", doc.getFieldValue("imageName").toString());
				if(doc.getFieldValue("upc")!=null)
					doc2.addField("upc", doc.getFieldValue("upc").toString());
    				doc2.addField("manfpartnumber", (doc.getFieldValue("manfpartnumber")!=null?doc.getFieldValue("manfpartnumber").toString():""));
    				doc2.addField("keywords", doc.getFieldValue("keywords").toString());
    			if(doc.getFieldValue("imageType")!=null)
    				doc2.addField("imageType", doc.getFieldValue("imageType").toString());
    			if(doc.getFieldValue("description")!=null)
    				doc2.addField("description", (doc.getFieldValue("description")!=null?doc.getFieldValue("description").toString():""));
    			if(doc.getFieldValue("longdescriptionone")!=null)
    				doc2.addField("longdescriptionone", (doc.getFieldValue("longdescriptionone")!=null?doc.getFieldValue("longdescriptionone").toString():""));
    			if(doc.getFieldValue("salesUom")!=null)
    				doc2.addField("salesUom", (doc.getFieldValue("salesUom")!=null?doc.getFieldValue("salesUom").toString():""));
    			if(doc.getFieldValue("qtyAvailable")!=null)
    				doc2.addField("qtyAvailable", (doc.getFieldValue("qtyAvailable")!=null?CommonUtility.validateNumber(doc.getFieldValue("qtyAvailable").toString()):0));
    				
    				doc2.addField("partkeywords", doc.getFieldValue("partkeywords").toString());
    			if(doc.getFieldValue("manufacturerName")!=null)
    				doc2.addField("manufacturerName", doc.getFieldValue("manufacturerName").toString());
    			if(doc.getFieldValue("brand")!=null)
    				doc2.addField("brand", (doc.getFieldValue("brand")!=null?doc.getFieldValue("brand").toString():""));
    				doc2.addField("itemid", doc.getFieldValue("itemid").toString());
 				}
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return itemModel;
	}
	
	private static int updateItemAdvancedGroup(Connection conn,int listItemId,int qty,int oldQty,int savedListId,int itemId,int itemPriceId) {
		int count=0;
		PreparedStatement pstmt = null;
		HttpSolrServer server = null;
		try
		{
			String sql = "UPDATE PRODUCT_GROUP_ITEMS_V2 SET QUANTITY = ? WHERE PRODUCT_GROUP_ITEM_ID = ?";
			//UPDATE SAVED_LIST_ITEMS SET QTY = ? WHERE LIST_ITEM_ID = ?
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (oldQty+qty));
    		pstmt.setInt(2, listItemId);
    		count = pstmt.executeUpdate();
    		if(count>0){
    			String solrURL = CommonDBQuery.getSystemParamtersList().get("SOLR_URL");
    			server = ConnectionManager.getSolrClientConnection(solrURL+"/productgroupitems");
    			SolrInputDocument doc = new SolrInputDocument();
    			doc.addField("id", listItemId);
    			doc.addField("categoryID", savedListId);
    			getSolrItemDetail(itemId, doc);
    			
    			doc.addField("itemPriceId", itemPriceId);
    			doc.addField("qty", oldQty+qty);
    			server.add(doc);
    			server.commit(); 
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
        finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeSolrClientConnection(server);
        }
		return count;
	}
	private static ArrayList<Integer> selectFromAdvancedProductGroup(Connection conn, int listId, int itemPriceId) {
		ArrayList<Integer> savedListId = new ArrayList<Integer>();
		String sql = "SELECT PRODUCT_GROUP_ITEM_ID,QUANTITY FROM PRODUCT_GROUP_ITEMS_V2 WHERE PRODUCT_GROUP_ID=? AND ITEM_PRICE_ID=?";
		//SELECT LIST_ITEM_ID, QTY FROM SAVED_LIST_ITEMS WHERE SAVED_LIST_ID = ? AND ITEM_ID=?
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, listId);
			pstmt.setInt(2, itemPriceId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				savedListId.add(rs.getInt("PRODUCT_GROUP_ITEM_ID"));
				savedListId.add(rs.getInt("QUANTITY"));
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
		return savedListId;
	}
	public static int createAdvancedProductGroupName(Connection conn,int savedListId, int userId, String levelNo, String listName,int parentId) {
		int count=0;
		PreparedStatement pstmt = null;
		HttpSolrServer server = null;
		try
		{
			int siteId = 0;
			if(CommonDBQuery.getGlobalSiteId()>0){
	    		siteId = CommonDBQuery.getGlobalSiteId();
			}
			String sql = "INSERT INTO PRODUCT_GROUPS_V2 (PRODUCT_GROUP_ID,PRODUCT_GROUP_NAME,CIMM_USER_ID,LEVEL_NUMBER,PARENT_GROUP_ID,UPDATED_DATETIME,SITE_ID) VALUES(?,?,?,?,?,SYSDATE,?)";
			//PRODUCT_GROUP_ID,PRODUCT_GROUP_NAME,CIMM_USER_ID,LEVEL_NUMBER,PARENT_GROUP_ID
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, savedListId);
    		pstmt.setString(2, listName);
    		pstmt.setInt(3, userId);
    		pstmt.setString(4, levelNo);
    		pstmt.setInt(5, parentId);
    		pstmt.setInt(6, siteId);
    		count = pstmt.executeUpdate();
    		if(count>0){
    			String solrURL = CommonDBQuery.getSystemParamtersList().get("SOLR_URL");
    			server = ConnectionManager.getSolrClientConnection(solrURL+"/productgroup");
    			SolrInputDocument doc = new SolrInputDocument();
    			doc.addField("id", savedListId);
    			doc.addField("category", listName);
    			doc.addField("indexType", userId);
    			doc.addField("type", "Level"+levelNo);
    			doc.addField("levelNumber", levelNo);
    			doc.addField("parentCategoryId", parentId);
    			server.add(doc);
    			server.commit(); 
    		}
    	}catch(Exception e){
        	e.printStackTrace();
        }finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeSolrClientConnection(server);
	    }
		return count;
	}
	
	public static String getParentName(int parentId)
	{
		String parentName = "";
		HttpSolrServer server = null;
		try
		{
			String c = "";
			while(parentId>0)
			{
			server = ConnectionManager.getSolrClientConnection(CommonDBQuery.getSystemParamtersList().get("SOLR_URL")+"/productgroup");
			server.setParser(new XMLResponseParser());
			QueryRequest req = new QueryRequest();
			req.setMethod(METHOD.POST);
			SolrQuery query = new SolrQuery();
			String fq= "id:"+parentId;
			int resultCount = 0;
			query.setQuery("*:*");
			//String fq = "{!join from=itemid to=id fromIndex=itempricedata}type:"+indexType;
			String attrFtr[] = fq.split("~");
			query.setStart(0);
			query.setRows(5);
			
			query.addSortField("displaySeq", SolrQuery.ORDER.asc );
			query.setFilterQueries(attrFtr);
			
			QueryResponse response = server.query(query);
			System.out.println("Category Query : " + query);
			
			SolrDocumentList documents = response.getResults();
			resultCount = (int) response.getResults().getNumFound();
			List<FacetField> facetFeild = response.getFacetFields();
			Iterator<SolrDocument> itr = documents.iterator();
			while (itr.hasNext()) {
				SolrDocument doc = itr.next();
				parentName = doc.getFieldValue("category").toString() + c +parentName;
				parentId = CommonUtility.validateNumber(doc.getFieldValue("id").toString());
				c = " > ";
			}
			if(parentId==0)
				parentId = -1;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.closeSolrClientConnection(server);
		}
		return parentName;
	}
	public static int validateAdvancedProductGroupName(Connection conn,String listName, int userId, String type,int levelNumber) {
		int isGroupId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String sql = "SELECT PRODUCT_GROUP_ID FROM PRODUCT_GROUPS_V2 WHERE UPPER(PRODUCT_GROUP_NAME)=? AND CIMM_USER_ID=? AND LEVEL_NUMBER = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, listName.toUpperCase());
			pstmt.setInt(2, userId);
			pstmt.setInt(3, levelNumber);
			rs = pstmt.executeQuery();
			if(rs.next())
				isGroupId = rs.getInt("PRODUCT_GROUP_ID");
		}catch(SQLException e){
        	e.printStackTrace();
        }catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    }
		return isGroupId;
	}
	public static String updateAdvancedGroupItemsDao(String[] shoppingCartId,String[] shoppingCartQty, ArrayList<Integer> idPriceList,int savedGroupId) {
		String target = "UpdateListItem";
		ResultSet rs = null;
	    Connection  conn = null;
	    int deletedItems = 0;
	    PreparedStatement pstmt = null;
	    
	    try {
            conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }	
        
        try{

			if(shoppingCartId!=null && shoppingCartId.length>0 && idPriceList!=null)
			{
				for(int i=0;i<shoppingCartId.length;i++)
				{
					for(int j=0;j<idPriceList.size();j++)
					{
	 					if(idPriceList.get(j)==CommonUtility.validateNumber(shoppingCartId[i]))
						{
							int qty =1;
							if(!shoppingCartQty[i].trim().equalsIgnoreCase(""))
								qty = CommonUtility.validateNumber(shoppingCartQty[i]);
							if(qty==0){
								String sql = "DELETE PRODUCT_GROUP_ITEMS_V2 WHERE PRODUCT_GROUP_ITEM_ID = ?";
								pstmt = conn.prepareStatement(sql);
								pstmt.setInt(1, idPriceList.get(j));
								deletedItems++;
							}else{
								String sql = "UPDATE PRODUCT_GROUP_ITEMS_V2 SET QUANTITY = ? WHERE ITEM_PRICE_ID = ?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setInt(1, qty);
							pstmt.setInt(2, idPriceList.get(j));
							}
							int count = pstmt.executeUpdate();
							ConnectionManager.closeDBResultSet(rs);
					    	ConnectionManager.closeDBPreparedStatement(pstmt);	
						}
					}
				}
			}
			if(shoppingCartId!=null && shoppingCartId.length == deletedItems){
				System.out.println("All the items deleted");
				String sql = "DELETE PRODUCT_GROUP_ITEMS_V2 WHERE PRODUCT_GROUP_ITEM_ID = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, savedGroupId);
				int count = pstmt.executeUpdate();
				if(count==0)
				{
					ConnectionManager.closeDBPreparedStatement(pstmt);	
					sql = "DELETE PRODUCT_GROUP_V2 WHERE PRODUCT_GROUP_ID = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, savedGroupId);
					count = pstmt.executeUpdate();
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
	    finally
	    {
	    	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
	    }
		
		return target;
		
	}
	
	public static ArrayList<ProductsModel> getManufacturersListForQuickOrder(int subsetId, int generalSubsetId){
		ArrayList<ProductsModel> manufacturersList = new ArrayList<ProductsModel>();
	ResultSet rs = null;
	Connection  conn = null;
	String sql ="";
	PreparedStatement pstmt = null;
	try {
		conn = ConnectionManager.getDBConnection();
	
		
		if(generalSubsetId>0){
			sql=PropertyAction.SqlContainer.get("getManufListForQOrderGeneralSubset");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subsetId);
			pstmt.setInt(2, generalSubsetId);
		}else{
			sql=PropertyAction.SqlContainer.get("getManufListForQOrder");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subsetId);
			
		}
		
		
		rs = pstmt.executeQuery();
		while(rs.next())
		{

			ProductsModel manufacturer = new ProductsModel();
			manufacturer.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
			manufacturer.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
			manufacturersList.add(manufacturer);

		}

	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	finally
	{
		ConnectionManager.closeDBResultSet(rs);
		ConnectionManager.closeDBPreparedStatement(pstmt);	
		ConnectionManager.closeDBConnection(conn);
	}
	return manufacturersList;
}
	
	public static String getDistributionCenterFromWarehouseCustomField(int buyingCompanyId){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		String distributionCenter = "";
		
	
		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getDistributionCenterFromWarehouseCustomFields");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,buyingCompanyId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				distributionCenter = rs.getString("TEXT_FIELD_VALUE");
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return distributionCenter;
	}
	
public static String getWarehouseCustomField(int buyingCompanyId, String customFieldName){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		String custFieldValue = "";
		
	
		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getWarehouseCustomFields");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,customFieldName);
			pstmt.setInt(2,buyingCompanyId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				custFieldValue = rs.getString("TEXT_FIELD_VALUE");
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return custFieldValue;
	}
	
	public static String getDistributionCenterFromWarehouseCustomFieldByWarehouse(String wareHouseCode){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet  rs =null;
		String distributionCenter = "";
		
	
		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getDistributionCenterFromWarehouseCustomFieldsByWarehouse");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(wareHouseCode).toUpperCase());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				distributionCenter = rs.getString("TEXT_FIELD_VALUE");
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		finally{
	    	ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return distributionCenter;
	}
	
	public static ArrayList<ProductsModel> getCustomerAlsoBought(int userId,int subsetId,int generalSubset,int itemId,int itemPriceId,int fromRow,int toRow)
	{
		PreparedStatement pstmt = null;
        ResultSet itemData = null;
        HttpServletRequest request = ServletActionContext.getRequest();
 		Connection conn = null;
        int rowSize = 0;
        ArrayList<ProductsModel> itemDetailObject = null;
        try {
	        if(fromRow==0){
	        	fromRow = 1;
	        }
	        if(toRow==0){
	        	toRow = 3;
	        }
	        String idList = "";
	        String c = "";
	        String noOfItemToView = "12";
	        boolean getItemDetail = false;
	        noOfItemToView = CommonDBQuery.getSystemParamtersList().get("CAB_NUMBER_OF_ITEM_VIEW");
	        if(noOfItemToView!=null && !noOfItemToView.trim().equalsIgnoreCase("")){
	        	toRow=Integer.parseInt(noOfItemToView);
	        }
        
             conn = ConnectionManager.getDBConnection();
         	 int resultCount = 0;
        	 String sql = "SELECT * FROM (SELECT ITEM_ID,ROWNUM RN,COUNT(*) OVER() RESULT_COUNT FROM(WITH T1 AS( SELECT DISTINCT OI.ORDER_ID, ROW_NUMBER() OVER (ORDER BY OI.UPDATED_DATETIME DESC) RN FROM ORDER_ITEMS OI, ORDERS O, ITEM_PRICES IP WHERE IP.SUBSET_ID=? AND IP.ITEM_ID = ? AND OI.ITEM_ID = IP.ITEM_ID AND O.ORDER_ID = OI.ORDER_ID) SELECT DISTINCT(OI.ITEM_ID) FROM ORDER_ITEMS OI,ITEM_PRICES IP,T1 WHERE OI.ORDER_ID = T1.ORDER_ID AND IP.STATUS = 'A' AND IP.SUBSET_ID=? AND OI.ITEM_ID = IP.ITEM_ID)) WHERE RN BETWEEN "+fromRow+" AND "+toRow;
        	 pstmt = conn.prepareStatement(sql);
        	 pstmt.setInt(1, subsetId);
        	 pstmt.setInt(2, itemId);
        	 pstmt.setInt(3, subsetId); 
        	 itemData = pstmt.executeQuery();
        	 System.out.println("Getting Customer Also Bought");
        	 itemDetailObject = new ArrayList<ProductsModel>();
        	 while(itemData.next()){
        		 idList = idList+ c + itemData.getInt("ITEM_ID");
	        	 c = " OR ";
	        	 rowSize++;
	        	 if(!getItemDetail){
	        		 getItemDetail = true;
	        	 }
	        	 resultCount = itemData.getInt("RESULT_COUNT");
	        }
        	if(getItemDetail){
        		itemDetailObject = ProductHunterSolr.getItemListByItemId(idList, subsetId, generalSubset, rowSize,0,"");
        	}
        	if(itemDetailObject.size()>0){
        		itemDetailObject.get(0).setResultCount(resultCount);
        	}
         }catch (SQLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }catch (Exception e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }finally{
    	   ConnectionManager.closeDBResultSet(itemData);
	       ConnectionManager.closeDBPreparedStatement(pstmt);
    	   ConnectionManager.closeDBConnection(conn);
         }
         return itemDetailObject;
	}
	
	public static LinkedHashMap<String, Object> getApproveCartList(HttpSession session, LinkedHashMap<String, Object> contentObject, int savedGroupId, int assignedShipTo, String approveSenderid, String updatedDate, String savedGroupName,int fromRow,int toRow,String sortBy,int requesterTokenId){
		
		String target = "";
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	    String tempSubset = (String) session.getAttribute("userSubsetId");
	    int subsetId = CommonUtility.validateNumber(tempSubset);
	    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		//int userId = CommonUtility.validateNumber(sessionUserId);
		String homeTerritory = (String) session.getAttribute("shipBranchId");
		String tempBuyingCompany = (String) session.getAttribute("buyingCompanyId");
		int buyingCompanyId = CommonUtility.validateNumber(tempBuyingCompany);
		ArrayList<String> partIdentifier = new ArrayList<String>();
		ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
		
		LinkedHashMap<Integer,ArrayList<ProductsModel>> approverDetails = new LinkedHashMap<Integer,ArrayList<ProductsModel>> (); 
		
		
		if(ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"))>0){
			generalSubset = ProductsDAO.getSubsetIdFromName(CommonDBQuery.getSystemParamtersList().get("EXT_SEARCH_SUBSET_NAME"));
		}
		
		String sortByQry = "ORDER BY MANUFACTURER_PART_NUMBER ASC ";
		ArrayList<ProductsModel> productListData = new ArrayList<ProductsModel>();
		target = "ProductListItem";
		String idList = "";
		String c = "";
		if(sortBy!=null && !sortBy.trim().equalsIgnoreCase(""))
	    {
	    	sortBy = sortBy.trim();
	    	
	    	
	    	if(sortBy.equalsIgnoreCase("DescA"))
	    	{
	    		sortByQry = " ORDER BY SHORT_DESC ASC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("DescD"))
	    	{
	    		sortByQry = " ORDER BY SHORT_DESC DESC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("MPNA"))
	    	{
	    		sortByQry = " ORDER BY MANUFACTURER_PART_NUMBER ASC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("MPND"))
	    	{
	    		sortByQry = " ORDER BY MANUFACTURER_PART_NUMBER DESC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("PartA"))
	    	{
	    		sortByQry = " ORDER BY PART_NUMBER ASC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("PartD"))
	    	{
	    		sortByQry = " ORDER BY PART_NUMBER DESC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("ProdNameA"))
	    	{
	    		sortByQry = " ORDER BY BRAND_NAME ASC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("ProdNameB"))
	    	{
	    		sortByQry = " ORDER BY BRAND_NAME DESC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("UpcA"))
	    	{
	    		sortByQry = " ORDER BY UPC ASC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("UpcD"))
	    	{
	    		sortByQry = " ORDER BY UPC DESC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("ManfD"))
	    	{
	    		sortByQry = " ORDER BY MANUFACTURER_NAME DESC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("ManfA"))
	    	{
	    		sortByQry = " ORDER BY MANUFACTURER_NAME ASC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("CPNAsc"))
	    	{
	    		sortByQry = " ORDER BY MANUFACTURER_PART_NUMBER ASC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("CPNDesc"))
	    	{
	    		sortByQry = " ORDER BY MANUFACTURER_PART_NUMBER DESC";
	    	}
	    	else if(sortBy.equalsIgnoreCase("ItemAddedASC"))
	    	{
	    		sortByQry = " ORDER BY UPDATED_DATETIME ASC NULLS FIRST";
	    	}
	    	else if(sortBy.equalsIgnoreCase("ItemAddedDesc"))
	    	{
	    		sortByQry = " ORDER BY UPDATED_DATETIME ASC NULLS FIRST";
	    	}
	    }
	    String userName = (String) session.getAttribute(Global.USERNAME_KEY);
	    try{

	    	 conn = ConnectionManager.getDBConnection();
	    	String type = "";
	    	String sql = "";
	    
	    		String prepend = "SELECT * FROM ( SELECT * FROM (SELECT LIST_ITEM_ID  AS CART_ID,UPDATED_DATETIME,SAVED_LIST_NAME,REQ_TOKEN_ID,  APPROVAL_STATUS,APPROVER_SEQ,APPROVER_COMMENT,USER_NAME,USER_ID,TYPE,STATUS,ITEM_ID,ITEM_PRICE_ID,SUBSET_ID,PAGE_TITLE,PART_NUMBER,ALT_PART_NUMBER1,SHORT_DESC,LONG_DESC1,BRAND_NAME,BRAND_IMAGE,MANUFACTURER_NAME,MANUFACTURER_ID,MATERIAL_GROUP,INVOICE_DESC,PACK_DESC,SALES_UOM,SALES_QTY,IMAGE_NAME,IMAGE_TYPE,OVERRIDE_PRICE_RULE,NET_PRICE,QTY,SENT_BY_APPR_USER_ID,MIN_ORDER_QTY,ORDER_QTY_INTERVAL,PACKAGE_QTY,PACKAGE_FLAG,CUSTOMER_PART_NUMBER,MANUFACTURER_PART_NUMBER,EXTPRICE,LINE_ITEM_COMMENT,ITEM_LEVEL_SHIPVIA,ITEM_LEVEL_SHIPVIA_DESC,ITEMLEVEL_REQUIREDBYDATE,CATALOG_ID,UPC,UOM,ROW_NUMBER() OVER ("+sortByQry+") RN,COUNT(*) OVER() RESULT_COUNT FROM (";

	    		String append = ")) "+sortByQry+" )";
	    		
	    		/* we are removed restricted rows as we are pulling all items assigned at approver sequence level   
	    		 * WHERE RN BETWEEN ? AND ?";*/
	    		
	    		
	        	sql = PropertyAction.SqlContainer.get("selectGroupItemForApproveCart");
	        	sql = prepend + sql + append;
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, requesterTokenId);
				pstmt.setInt(2, savedGroupId);
				pstmt.setInt(3, buyingCompanyId);
				pstmt.setInt(4, subsetId);
				pstmt.setInt(5, requesterTokenId);
				pstmt.setInt(6, savedGroupId);
				pstmt.setInt(7, buyingCompanyId);
				pstmt.setInt(8, generalSubset);
				pstmt.setInt(9, requesterTokenId);
				pstmt.setInt(10, subsetId);
				/*pstmt.setInt(9, fromRow);
				pstmt.setInt(10, toRow);*/
			
			
			
			System.out.println(pstmt);
			rs = pstmt.executeQuery();
			String groupName = "";
			int requestTokenId = 0;
			while(rs.next()){
				ProductsModel productListVal = new ProductsModel();
				UsersModel userModel = new UsersModel();
				type = rs.getString("TYPE");
				int packageQty = 1;
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
	        	itemUrl = itemUrl.replaceAll(" ","-");
	        	productListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
	        	productListVal.setItemUrl(itemUrl);
				productListVal.setItemId(rs.getInt("ITEM_ID"));
				productListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				productListVal.setPartNumber(rs.getString("PART_NUMBER"));
				productListVal.setProductListId(rs.getInt("CART_ID"));
				productListVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
				productListVal.setQty(rs.getInt("QTY"));
				productListVal.setShortDesc(rs.getString("SHORT_DESC"));
				productListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
				productListVal.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				productListVal.setBrandName(rs.getString("BRAND_NAME"));
				productListVal.setPrice(rs.getDouble("NET_PRICE"));
				productListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
				productListVal.setTotal(rs.getDouble("EXTPRICE"));
				productListVal.setUom(CommonUtility.validateString(rs.getString("UOM")).length() > 0 ? rs.getString("UOM") : CommonUtility.validateString(rs.getString("SALES_UOM")));
				productListVal.setSalesUom(rs.getString("SALES_UOM"));
				productListVal.setImageType(rs.getString("IMAGE_TYPE"));
				productListVal.setSaleQty(rs.getInt("SALES_QTY"));
				productListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				productListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				productListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				productListVal.setPageTitle(rs.getString("PAGE_TITLE"));
				productListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
				productListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				productListVal.setOverRidePriceRule(rs.getString("OVERRIDE_PRICE_RULE"));
				if(rs.findColumn("CATALOG_ID")>0){
					productListVal.setCatalogId(rs.getString("CATALOG_ID"));	
				}
				
				
				userModel.setUserId(rs.getInt("USER_ID"));
				userModel.setUserName(rs.getString("USER_NAME"));
				userModel.setApproverSequence(rs.getString("APPROVER_SEQ"));
				userModel.setApproverComments(rs.getString("APPROVER_COMMENT"));
				userModel.setApprovalStatus(rs.getString("APPROVAL_STATUS"));
				productListVal.setUserModelObject(userModel);
				
				productListVal.setApprovalStatus(rs.getString("APPROVAL_STATUS"));
				productListVal.setApproverSequence(rs.getString("APPROVER_SEQ"));
				
				productListVal.setMultipleShipVia(rs.getString("ITEM_LEVEL_SHIPVIA"));
				productListVal.setMultipleShipViaDesc(rs.getString("ITEM_LEVEL_SHIPVIA_DESC"));
				productListVal.setItemLevelRequiredByDate(rs.getString("ITEMLEVEL_REQUIREDBYDATE"));
				partIdentifier.add(productListVal.getPartNumber());
				partIdentifierQuantity.add(rs.getInt("QTY"));
				
				
				productListVal.setResultCount(rs.getInt("RESULT_COUNT"));
				if(rs.getInt("PACKAGE_QTY")>0)
					packageQty = rs.getInt("PACKAGE_QTY");
				
				idList = idList + c + rs.getInt("ITEM_ID");
				c = " OR ";
				
				productListVal.setPackageQty(packageQty);
				productListVal.setUpc(rs.getString("UPC"));
				groupName = rs.getString("SAVED_LIST_NAME");
				approveSenderid = rs.getString("SENT_BY_APPR_USER_ID");
				if(rs.findColumn("REQ_TOKEN_ID")>0){
					requestTokenId = rs.getInt("REQ_TOKEN_ID");
				}
				 
				
				// get Customer Part Number List
				
				
				productListData.add(productListVal);
			}
			
			
			//-------------- ERP pricing here
			if(productListData!=null && productListData.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
			{
				LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = ProductHunterSolr.getcustomerPartnumber(idList, buyingCompanyId, buyingCompanyId);
				if(customerPartNumber!=null && customerPartNumber.size()>0)
				{
					for(ProductsModel item : productListData) {
						ArrayList<ProductsModel> eachCPN = customerPartNumber.get(item.getItemId());
						if(eachCPN!=null && eachCPN.size()>1){
							Collections.sort(eachCPN, ProductsModel.partNumberAscendingComparator);
							if(sortBy.equalsIgnoreCase("CPNDesc")){
								Collections.sort(eachCPN, ProductsModel.partNumberDescendingComparator);
							}
							item.setCustomerPartNumber(eachCPN.get(0).getPartNumber());
						}else if(eachCPN!=null && eachCPN.size()>0){
							item.setCustomerPartNumber(eachCPN.get(0).getPartNumber());
						}
						item.setCustomerPartNumberList(eachCPN);
					}
				}
				ProductManagement priceInquiry = new ProductManagementImpl();
				ProductManagementModel priceInquiryInput = new ProductManagementModel();
				String entityId = (String) session.getAttribute("entityId");
				priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
				priceInquiryInput.setHomeTerritory(homeTerritory);
				priceInquiryInput.setPartIdentifier(productListData);
				priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
				priceInquiryInput.setRequiredAvailabilty("Y");
				priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
				priceInquiryInput.setSession(session);
				productListData = priceInquiry.priceInquiry(priceInquiryInput , productListData);
			}

			if(type!=null)
			{
				if(type.equalsIgnoreCase("C")){
					target="SavedCartGroup";
				}
				if(type.equalsIgnoreCase("A")){
					target="ApproveCart";
				}
				contentObject.put("reqType",type);
			}
			
			
			if(assignedShipTo>0){
				sql = PropertyAction.SqlContainer.get("getUserName");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, CommonUtility.validateNumber(approveSenderid));
				rs = pstmt.executeQuery();
				while(rs.next()){
					userName=rs.getString("USER_NAME");
				}
				session.setAttribute("groupName", groupName);
				session.setAttribute("listUserName", userName);
				session.setAttribute("updatedDate", updatedDate);
				session.setAttribute("fromApproveCart", "Y");
				session.setAttribute("senderId", approveSenderid);
				if(requestTokenId > 0){
					session.setAttribute("requestTokenId", requestTokenId);	
				}
			}
			
			String requiredAvailabilty = null;
			if(target.equalsIgnoreCase("SavedCartGroup")){
				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_AVAILABILITY_SAVED_CART");
			}else if(target.equalsIgnoreCase("ProductListItem")){
				requiredAvailabilty = CommonDBQuery.getSystemParamtersList().get("AFTER_LOGIN_AVAILABILITY_PRODUCT_GROUP");
			}else{
				requiredAvailabilty = "N";
			}
	   	
			
			savedGroupName = groupName;
			if(contentObject!=null)
			{
				int leastApprover = Integer.MAX_VALUE;
				int pendingApprover = 0;
				ArrayList<UsersModel> approverGroupList = new ArrayList<UsersModel>();
				approverGroupList = UsersDAO.getApproverGroupList(requestTokenId, CommonUtility.validateNumber(approveSenderid));// getting all APA sequence list List  
				String loginUserSeq = null;
				
				ArrayList<UsersModel> pendingApproverList =  new ArrayList<UsersModel> ();
				
			
				for(UsersModel userModel : approverGroupList){
					
					ArrayList<ProductsModel> userProductsList = new ArrayList<ProductsModel>();

					// finding logged in user sequence
					if(userModel.getUserId() == CommonUtility.validateNumber(sessionUserId)){
						loginUserSeq = userModel.getApproverSequence();
					}
					
					for(ProductsModel productsModel : productListData){
						
						if(productsModel.getUserModelObject().getUserId() > 0){
							if(userModel.getUserId() == productsModel.getUserModelObject().getUserId()){
								userProductsList.add(productsModel);
							}
						}
					}
					
					//Keeping all approved and not approved details in linked hashmap 
					if(userProductsList.size() > 0){
						approverDetails.put(userModel.getUserId(), userProductsList);
					}
				}
				
				
				
				if(approverGroupList.size() > 0){
					
					ArrayList<UsersModel> filteredGroup = new ArrayList<UsersModel>();

					// keeping all approver details except current login approver, to display status of all approver details.
					for(UsersModel obj : approverGroupList){
						if(obj.getUserId() != CommonUtility.validateNumber(sessionUserId) && loginUserSeq != null && !loginUserSeq.equals(obj.getApproverSequence())){
							filteredGroup.add(obj);
						}
					}
					
					approverGroupList.clear();
					approverGroupList = filteredGroup;
					
					
					for(UsersModel userModel : approverGroupList){
						
						if(userModel.getApprovalStatus().equalsIgnoreCase("N")){
							pendingApproverList.add(userModel);
							if(userModel.getUserId() != CommonUtility.validateNumber(sessionUserId) && loginUserSeq != null && !loginUserSeq.equals(userModel.getApproverSequence())){
								pendingApprover = CommonUtility.validateNumber(userModel.getApproverSequence());
							}
							System.out.println("pendingApproverList:" + userModel.getUserName() +" " + userModel.getApproverSequence());
							
						}
					}
					
					
					//Finding the least approver behind the current log in user. For Ex: 1,2,3,4 login user sequence is  3 and nearest approver is 2. 
					// if sequence 2 approver is not yet approved, then nearest approver is 1 for log in user sequence 3
					for(UsersModel obj : pendingApproverList) {
			             if(leastApprover > CommonUtility.validateNumber(obj.getApproverSequence())) {
			            	 leastApprover = CommonUtility.validateNumber(obj.getApproverSequence());
			            	 
			             }
			         }
				}
		
				if(leastApprover != Integer.MAX_VALUE){
					
					if(CommonUtility.validateString(loginUserSeq).length()>0){
						if(leastApprover != Integer.MAX_VALUE && leastApprover > CommonUtility.validateNumber(loginUserSeq)){
							leastApprover = 0;
						}
					}
					contentObject.put("leastApprover", leastApprover);
					System.out.println("Least Approver :" + leastApprover);
					
				}else{
					contentObject.put("leastApprover", 0);
					System.out.println("Least Approver :" + 0);
				}
			contentObject.put("pendingApprover", pendingApprover);	
			contentObject.put("requesterTokenId", requesterTokenId);
			contentObject.put("approverDetails", approverDetails);
			contentObject.put("pendingApproverList", pendingApproverList);
			contentObject.put("approverGroupList", approverGroupList);
			contentObject.put("responseType", target);
			contentObject.put("productListData", productListData);
			contentObject.put("updatedDate", updatedDate);
			contentObject.put("assignedShipTo", assignedShipTo);
			contentObject.put("savedGroupId", savedGroupId);
			contentObject.put("approveSenderid", approveSenderid);
			contentObject.put("requiredAvailabilty", requiredAvailabilty);
			contentObject.put("savedGroupName", savedGroupName);
			}
			
	    }catch(SQLException e){
	    	e.printStackTrace();
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    finally{
	    	ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
	    }
		return contentObject;
	}

	public static LinkedHashMap<String, Object>  getApproveCartDao(HttpSession session, LinkedHashMap<String, Object> contentObject){		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		double cartSubTotal = 0.0;
		DecimalFormat df = CommonUtility.getPricePrecision(session);
		int activeTaxonomyId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"));
		try{
			conn = ConnectionManager.getDBConnection();
			

			String sql = PropertyAction.SqlContainer.get("selectGroupItem");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, CommonUtility.validateNumber(contentObject.get("savedGroupId").toString()));
			pstmt.setInt(2, CommonUtility.validateNumber(contentObject.get("userId").toString()));
			pstmt.setInt(3, CommonUtility.validateNumber(contentObject.get("buyingCompanyId").toString()));
			pstmt.setInt(4, CommonUtility.validateNumber(contentObject.get("subsetId").toString()));
			pstmt.setInt(5, activeTaxonomyId);
			pstmt.setInt(6, CommonUtility.validateNumber(contentObject.get("savedGroupId").toString()));
			pstmt.setInt(7, CommonUtility.validateNumber(contentObject.get("userId").toString()));
			pstmt.setInt(8, CommonUtility.validateNumber(contentObject.get("buyingCompanyId").toString()));
			pstmt.setInt(9, CommonUtility.validateNumber(contentObject.get("generalsubsetId").toString()));
			pstmt.setInt(10, activeTaxonomyId);
			pstmt.setInt(11, CommonUtility.validateNumber(contentObject.get("savedGroupId").toString()));
			pstmt.setInt(12, CommonUtility.validateNumber(contentObject.get("subsetId").toString()));

					
			double orderTotal = 0.0;
			double total = 0.0;
			rs = pstmt.executeQuery();	
			ArrayList<ProductsModel> productListData = new ArrayList<ProductsModel>();
			String homeTerritory = (String) session.getAttribute("shipBranchId");
			ArrayList<ProductsModel> partIdentifiersList = new ArrayList<ProductsModel>();
			ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
			ArrayList<String> partIdentifier = new ArrayList<String>();
			int selectQuoteCartCount = 0;
			String itemShip="";
			String delimeter="";
			String itemShipDesc="";
			String delimeterDesc="";
			while(rs.next())
			{


				ProductsModel productListVal = new ProductsModel();
				int packageQty = 1;
				String itemUrl = rs.getString("BRAND_NAME")+rs.getString("MANUFACTURER_PART_NUMBER");
				//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
				itemUrl = itemUrl.replaceAll(" ","-");
				productListVal.setItemUrl(itemUrl);
				productListVal.setItemId(rs.getInt("ITEM_ID"));
				productListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				productListVal.setPartNumber(rs.getString("PART_NUMBER"));
				productListVal.setProductListId(rs.getInt("CART_ID"));
				productListVal.setQty(rs.getInt("QTY"));
				productListVal.setShortDesc(rs.getString("SHORT_DESC"));
				productListVal.setAltPartNumber1(rs.getString("ALT_PART_NUMBER1"));
				productListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
				productListVal.setBrandName(rs.getString("BRAND_NAME"));
				productListVal.setPrice(rs.getDouble("NET_PRICE"));
				productListVal.setUnitPrice(rs.getDouble("NET_PRICE"));
				productListVal.setTotal(rs.getDouble("EXTPRICE"));
				productListVal.setUom(rs.getString("SALES_UOM"));
				productListVal.setSalesUom(rs.getString("SALES_UOM"));
				productListVal.setImageType(rs.getString("IMAGE_TYPE"));
				productListVal.setSaleQty(rs.getInt("SALES_QTY"));
				productListVal.setImageName(rs.getString("IMAGE_NAME")==null?"NoImage.png":rs.getString("IMAGE_NAME"));
				productListVal.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
				productListVal.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
				productListVal.setPageTitle(rs.getString("PAGE_TITLE"));
				productListVal.setCustomerPartNumber(rs.getString("CUSTOMER_PART_NUMBER"));
				productListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
				productListVal.setMultipleShipVia(rs.getString("ITEM_LEVEL_SHIPVIA"));
				productListVal.setMultipleShipViaDesc(rs.getString("ITEM_LEVEL_SHIPVIA_DESC"));
				productListVal.setLineItemComment(rs.getString("LINE_ITEM_COMMENT"));
				if(CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA")!=null &&CommonDBQuery.getSystemParamtersList().get("ITEMLEVELSHIPVIA").equalsIgnoreCase("Y")){
					if(rs.getString("ITEM_LEVEL_SHIPVIA") != null && rs.getString("ITEM_LEVEL_SHIPVIA").equalsIgnoreCase("SHIPTOME")){
						session.setAttribute("shipViaMethodDisplay","Y");
					}
				itemShip =itemShip+delimeter+rs.getString("ITEM_LEVEL_SHIPVIA");
    			delimeter=",";
    			System.out.println("itemLevelShip:"+itemShip);
    			session.setAttribute("itemleveShip",itemShip);
    			itemShipDesc =itemShipDesc+delimeterDesc+rs.getString("ITEM_LEVEL_SHIPVIA_DESC");
				delimeterDesc=",";
    			System.out.println("itemShipDesc:"+itemShipDesc);
    			session.setAttribute("itemShipDesc",itemShipDesc);
				}
				
				partIdentifier.add(productListVal.getPartNumber());
				
				partIdentifierQuantity.add(rs.getInt("QTY"));

				//productListVal.setResultCount(rs.getInt("RESULT_COUNT"));
				if(rs.getInt("PACKAGE_QTY")>0)
					packageQty = rs.getInt("PACKAGE_QTY");


				productListVal.setPackageQty(packageQty);
				productListVal.setUpc(rs.getString("UPC"));

				// get Customer Part Number List
				productListVal.setBcAddressBookId(rs.getInt("BC_ADDRESS_BOOK_ID"));
				productListData.add(productListVal);
				
				//total = total + rs.getDouble("EXT_PRICE");

				selectQuoteCartCount++;
			}


			orderTotal = total;
			if(productListData!=null && productListData.size()>0 && session.getAttribute("userToken")!=null && !session.getAttribute("userToken").toString().trim().equalsIgnoreCase("") && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
			{

				ProductManagement priceInquiry = new ProductManagementImpl();
				ProductManagementModel priceInquiryInput = new ProductManagementModel();
				String entityId = (String) session.getAttribute("entityId");
				priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
				priceInquiryInput.setHomeTerritory(homeTerritory);
				priceInquiryInput.setPartIdentifier(productListData);
				priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
				priceInquiryInput.setRequiredAvailabilty("Y");
				priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
				priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
				priceInquiryInput.setSession(session);
				productListData = priceInquiry.priceInquiry(priceInquiryInput , productListData);
				total = productListData.get(0).getCartTotal();
			}



			String twoDecimalTotal = df.format(total);
			cartSubTotal = Double.parseDouble(twoDecimalTotal);
			contentObject.put("selectQuoteCartCount", selectQuoteCartCount);
			contentObject.put("cartSubTotal", cartSubTotal);
			contentObject.put("cartTotal", cartSubTotal);
			contentObject.put("productListData", productListData);

		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);	
		}
		return contentObject;
	}
	
	public static ArrayList<String> getWarehouseIndex()
	{
		ArrayList<String> warehouseIndexList = new ArrayList<String>();
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			String sql = PropertyAction.SqlContainer.get("getWarehouseIndex");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				warehouseIndexList.add(rs.getString("WAREHOUSE_NAME"));
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
	    	ConnectionManager.closeDBConnection(conn);
		}
		return warehouseIndexList;
	}
	
	public void saveItemLevelShipVia(int savedListId,int itemId,String itemLevelShipvia,String itemLevelShipviaDesc,int cartId,String pageName,int userId){
		
	   Connection conn = null;
		int count=0;
		String sql =""; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			if(pageName.equalsIgnoreCase("approveCart")){
				sql = PropertyAction.SqlContainer.get("getApproveCartMultipleShipVia");
				 }
				 else if(pageName.equalsIgnoreCase("Cart")) {
						sql = PropertyAction.SqlContainer.get("getCartMultipleShipVia");
				 }
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,itemLevelShipvia);
			pstmt.setString(2,itemLevelShipviaDesc);
			 if(pageName.equalsIgnoreCase("Cart")) {
				 pstmt.setInt(3,cartId);
			 } else if(pageName.equalsIgnoreCase("approveCart")){
			pstmt.setInt(3,savedListId);
			 }
			pstmt.setInt(4,itemId);
			if(pageName.equalsIgnoreCase("Cart")) {
				 pstmt.setInt(5,userId);
			}
			count = pstmt.executeUpdate();
			
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
	    	ConnectionManager.closeDBConnection(conn);
		}
		
	}

	public void reorderItemLevelShipVia(int quoteCartId,int partnumber,String itemLevelShipvia,String itemLevelShipviaDesc){
		
		   Connection conn = null;
			int count=0;
			String sql ="";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				sql = PropertyAction.SqlContainer.get("getReorderCartMultipleShipVia");
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,itemLevelShipvia);
				pstmt.setString(2,itemLevelShipviaDesc);
				pstmt.setInt(3,quoteCartId);
				pstmt.setInt(4,partnumber);
				count = pstmt.executeUpdate();
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
		    	ConnectionManager.closeDBConnection(conn);
			}
			
		}
	
	public static ProductsModel getPromotionalCode(ProductsModel promo) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		ProductsModel  promotion = new ProductsModel();
		String couponType = CommonDBQuery.getSystemParamtersList().get("COUPON_TYPE");
	    try
	    {
	      conn = ConnectionManager.getDBConnection();
	      
	      String sql = PropertyAction.SqlContainer.get("getPromotionalCodeByWarehouse");
	      sql = sql.replace("COUPON_RULE_TABLE", couponType);
	      pstmt = conn.prepareStatement(sql);
	      pstmt.setString(1, promo.getWareHouseCode());
	      pstmt.setString(2, promo.getPromoCode());
	      pstmt.setDouble(3, promo.getCartTotal());
	      pstmt.setDouble(4, promo.getCartTotal());
	      rs = pstmt.executeQuery();
	      while (rs.next())
	      {
         	promotion = new ProductsModel();
	        promotion.setDiscountType(rs.getString("DISCOUNT_TYPE"));
	        promotion.setDiscountValue(rs.getString("DISCOUNT_VALUE"));
	        promotion.setCouponCounter(rs.getInt("NUMBER_OF_USE_PER_CUSTOMER")); // REVIEW AGAIN AFTER DISCUSSING WITH BHARATH
	      }

	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    finally
	    {
	    	ConnectionManager.closeDBConnection(conn);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);
	    	ConnectionManager.closeDBResultSet(rs);
	    }

	    return promotion;
	}
	public void saveItemLevelShipViaBySession(int savedListId,int itemId,String itemLevelShipvia,String itemLevelShipviaDesc,int cartId,String pageName,int userId,HttpSession session,String lineitemComment){
		
		   Connection conn = null;
			int count=0;
			String sql ="";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				sql = PropertyAction.SqlContainer.get("getCartMultipleShipViaBySession");
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,itemLevelShipvia);
				pstmt.setString(2,itemLevelShipviaDesc);
				pstmt.setString(3,lineitemComment);
				pstmt.setString(4,session.getId());
				pstmt.setInt(5,cartId);
				pstmt.setInt(6,itemId);
				pstmt.setInt(7,userId);
				
				count = pstmt.executeUpdate();
				
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
		    	ConnectionManager.closeDBConnection(conn);
			}
			
		}
	public static ProductsModel getCouponCounter(String buyingCompany, String couponCode){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ProductsModel couponInfo = null;
		try
		{
			String sql = PropertyAction.SqlContainer.get("getCouponCounter");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, buyingCompany);
			pstmt.setString(2, couponCode);
			pstmt.setString(3, couponCode);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				couponInfo = new ProductsModel();
				couponInfo.setCouponCounter(rs.getInt("COUNTER"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		return couponInfo;
	}
	public static ConcurrentHashMap<String, String> getCustomFieldDetails(String customFieldName) {
		ConcurrentHashMap<String, String> customFieldValue = new ConcurrentHashMap<String, String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			String sql = PropertyAction.SqlContainer.get("getBrandCustomFieldValues");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(customFieldName));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				customFieldValue.put(rs.getString("BRAND_NAME").toUpperCase(), rs.getString("TEXT_FIELD_VALUE"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return customFieldValue;
	}
	
	public static LinkedHashMap<Integer, String> getManufacturerCustomFieldDetailsFromView(String customFieldName) {
		LinkedHashMap<Integer, String> customFieldValue = new LinkedHashMap<Integer, String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			String sql = PropertyAction.SqlContainer.get("getManufacturerCustomFieldValuesFromView");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(customFieldName));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				customFieldValue.put(rs.getInt("MANUFACTURER_ID"), rs.getString("MANUFACTURER_NAME"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return customFieldValue;
	}
	
	public static LinkedHashMap<Integer, ProductsModel> getManufacturerCustomFieldsFromView(String customFieldName) {
		LinkedHashMap<Integer, ProductsModel> customFieldValue = new LinkedHashMap<Integer, ProductsModel>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			String sql = PropertyAction.SqlContainer.get("getManufacturerCustomFieldValuesFromView");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(customFieldName));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				ProductsModel pModel = new ProductsModel();
				pModel.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				pModel.setManufacturerLogo(rs.getString("MANUFACTURER_LOGO"));
				pModel.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
				customFieldValue.put(rs.getInt("MANUFACTURER_ID"), pModel);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return customFieldValue;
	}
	public static LinkedHashMap<Integer, ProductsModel> getManufacturerMultipleCustomFields(String customFieldName) {
		LinkedHashMap<Integer, ProductsModel> customFieldValue = new LinkedHashMap<Integer, ProductsModel>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection connQuery = null;
		PreparedStatement pstmtQuery = null;
		ResultSet rsQuery = null;
		try
		{
			String sql = PropertyAction.SqlContainer.get("getManufacturerCustomFieldValuesFromView");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(customFieldName));
			rs = pstmt.executeQuery();
			String sqlQuery = PropertyAction.SqlContainer.get("getManufacturerMultipleCustomFieldValues");				
			connQuery = ConnectionManager.getDBConnection();
			pstmtQuery = connQuery.prepareStatement(sqlQuery);
			
			while (rs.next())
			{
				ProductsModel pModel = new ProductsModel();
				pModel.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				pModel.setManufacturerLogo(rs.getString("MANUFACTURER_LOGO"));
				pModel.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
				pstmtQuery.setLong(1, rs.getInt("MANUFACTURER_ID"));
				rsQuery = pstmtQuery.executeQuery();
				LinkedHashMap<String, String> customFieldList = new LinkedHashMap<String, String>();
				while (rsQuery.next()) {
					customFieldList.put(rsQuery.getString("FIELD_NAME"), rsQuery.getString("FIELD_VALUE"));
				}		
				pModel.setCustomFieldListValues(customFieldList);
				customFieldValue.put(rs.getInt("MANUFACTURER_ID"), pModel);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(connQuery);
			ConnectionManager.closeDBPreparedStatement(pstmtQuery);
			ConnectionManager.closeDBResultSet(rsQuery);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return customFieldValue;
	}
	
	public static ArrayList<ProductsModel> getFeaturedManufacturer(String customFieldName) {
		ArrayList<ProductsModel> customFieldValue = new ArrayList<ProductsModel>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		
		try
		{
			String sql = PropertyAction.SqlContainer.get("getManufacturerCustomFieldValuesFromView");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(customFieldName));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				ProductsModel pModel = new ProductsModel();
				pModel.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				pModel.setManufacturerLogo(rs.getString("MANUFACTURER_LOGO"));
				pModel.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
				customFieldValue.add(pModel);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return customFieldValue;
	}
	
	public static ArrayList<ProductsModel> getFeaturedManufacturerMultipleCustomFields(String customFieldName) {
		ArrayList<ProductsModel> customFieldValue = new ArrayList<ProductsModel>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection connQuery = null;
		PreparedStatement pstmtQuery = null;
		ResultSet rsQuery = null;
		try
		{
			String sql = PropertyAction.SqlContainer.get("getManufacturerCustomFieldValuesFromView");
			conn = ConnectionManager.getDBConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(customFieldName));
			rs = pstmt.executeQuery();
			String sqlQuery = PropertyAction.SqlContainer.get("getManufacturerMultipleCustomFieldValues");				
			connQuery = ConnectionManager.getDBConnection();
			pstmtQuery = connQuery.prepareStatement(sqlQuery);
			while (rs.next())
			{
				ProductsModel pModel = new ProductsModel();
				pModel.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				pModel.setManufacturerLogo(rs.getString("MANUFACTURER_LOGO"));
				pModel.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
				pstmtQuery.setLong(1, rs.getInt("MANUFACTURER_ID"));
				rsQuery = pstmtQuery.executeQuery();
				LinkedHashMap<String, String> customFieldList = new LinkedHashMap<String, String>();
				while (rsQuery.next()) {
					customFieldList.put(rsQuery.getString("FIELD_NAME"), rsQuery.getString("FIELD_VALUE"));
				}		
				pModel.setCustomFieldListValues(customFieldList);
				customFieldValue.add(pModel);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(connQuery);
			ConnectionManager.closeDBPreparedStatement(pstmtQuery);
			ConnectionManager.closeDBResultSet(rsQuery);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return customFieldValue;
	}
	
	public static ArrayList<ProductsModel> getFeaturedBrand(String customFieldName) {
		ArrayList<ProductsModel> customFieldValue = new ArrayList<ProductsModel>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			String sql = PropertyAction.SqlContainer.get("getBrandCustomFieldValuesFromView");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(customFieldName));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				ProductsModel pModel = new ProductsModel();
				pModel.setBrandId(rs.getInt("BRAND_ID"));
				pModel.setBrandName(rs.getString("BRAND_NAME"));
				pModel.setBrandUrl(rs.getString("BRAND_URL"));
				pModel.setBrandImage(rs.getString("BRAND_IMAGE"));
				pModel.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				customFieldValue.add(pModel);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return customFieldValue;
	}
	
	public static LinkedHashMap<Integer, ProductsModel> getBrandCustomFieldDetailsFromView(String customFieldName) {
		LinkedHashMap<Integer, ProductsModel> customFieldValue = new LinkedHashMap<Integer, ProductsModel>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			String sql = PropertyAction.SqlContainer.get("getBrandCustomFieldValuesFromView");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(customFieldName));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				ProductsModel pModel = new ProductsModel();
				pModel.setBrandId(rs.getInt("BRAND_ID"));
				pModel.setBrandName(rs.getString("BRAND_NAME"));
				pModel.setBrandUrl(rs.getString("BRAND_URL"));
				pModel.setBrandImage(rs.getString("BRAND_IMAGE"));
				pModel.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				customFieldValue.put(rs.getInt("BRAND_ID"), pModel);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return customFieldValue;
	}
	
	public static ProductsModel checkItemExistInCIMM(String partNumber , String subsetId) {
		ProductsModel itemDetails =null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			String sql = PropertyAction.SqlContainer.get("checkItemExistInCIMM");
			conn = ConnectionManager.getDBConnection();
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
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return itemDetails;
	}
	
	public static String getAlternatePartNumbers(String partNumber , String subsetId) {
		String alternatePartNumber = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		partNumber = partNumber.replaceAll("_", " ");
		String sql = "SELECT IM.ALT_PART_NUMBER1 FROM ITEM_MASTER IM,ITEM_PRICES IP WHERE IM.PART_NUMBER =? AND IM.ACTIVE != 'D' AND IM.DISPLAY_ONLINE='Y' AND IM.ITEM_ID = IP.ITEM_ID AND IP.SUBSET_ID=?";
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, CommonUtility.validateString(partNumber));
			pstmt.setInt(2, CommonUtility.validateNumber(subsetId));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				alternatePartNumber = rs.getString("ALT_PART_NUMBER1");
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		
		return alternatePartNumber;
	}
	public static String getARIModelNameFromModelID(String modelID,String brandID) {
		String modelName = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from ARI_BRAND_MODEL where ARI_MODEL_CODE=? AND BRAND_ID=?";
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, CommonUtility.validateNumber(modelID));
			pstmt.setInt(2, CommonUtility.validateNumber(brandID));
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				modelName = rs.getString("MODEL_NAME");
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
		}
		return modelName;
	}
	
	public static BrandAndManufacturerModel getBrandDetails(int brandId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		BrandAndManufacturerModel brandAndManufacturerModel = null;
		try {
			
			String sql = PropertyAction.SqlContainer.get("getBrandDetails");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, brandId);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				brandAndManufacturerModel = new BrandAndManufacturerModel();
				brandAndManufacturerModel.setBrandId(rs.getInt("BRAND_ID"));
				brandAndManufacturerModel.setBrandDescription(rs.getString("BRAND_DESC"));
				brandAndManufacturerModel.setBrandname(rs.getString("BRAND_NAME"));
				brandAndManufacturerModel.setBrandUrl(rs.getString("BRAND_URL"));
				brandAndManufacturerModel.setBrandImage(rs.getString("BRAND_IMAGE"));
				brandAndManufacturerModel.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				brandAndManufacturerModel.setStaticPageId(rs.getInt("STATIC_PAGE_ID"));
				if(brandAndManufacturerModel.getStaticPageId()>0) {
					sql = PropertyAction.SqlContainer.get("getActiveStaticPage");
					pstmt2 = conn.prepareStatement(sql);
					pstmt2.setInt(1, brandAndManufacturerModel.getStaticPageId());
					rs2 = pstmt2.executeQuery();
					if(rs2.next()) {}else {
						brandAndManufacturerModel.setStaticPageId(0);
					}
				}
				
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBPreparedStatement(pstmt2);
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBResultSet(rs2);
		}
		return brandAndManufacturerModel;
		
	}
	
	public static BrandAndManufacturerModel getManufacturerDetails(int manufacturerId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		BrandAndManufacturerModel brandAndManufacturerModel = null;
		try {
			
			String sql = PropertyAction.SqlContainer.get("getManufacturerDetails");
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, manufacturerId);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				brandAndManufacturerModel = new BrandAndManufacturerModel();
				brandAndManufacturerModel.setManufacturerId(rs.getInt("MANUFACTURER_ID"));
				brandAndManufacturerModel.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
				brandAndManufacturerModel.setManufacturerImage(rs.getString("MANUFACTURER_LOGO"));
				brandAndManufacturerModel.setStaticPageId(rs.getInt("STATIC_PAGE_ID"));
				if(brandAndManufacturerModel.getStaticPageId()>0) {
					sql = PropertyAction.SqlContainer.get("getActiveStaticPage");
					pstmt2 = conn.prepareStatement(sql);
					pstmt2.setInt(1, brandAndManufacturerModel.getStaticPageId());
					rs2 = pstmt2.executeQuery();
					if(rs2.next()) {}else {
						brandAndManufacturerModel.setStaticPageId(0);
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBPreparedStatement(pstmt2);
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBResultSet(rs2);
		}
		return brandAndManufacturerModel;
		
	}
	public static int updatecartSessionId(String oldSessionId,String newSessionId){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		int count=0;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("updatecartSession");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,newSessionId);
			pstmt.setString(2, oldSessionId);
			count = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return count;

	}
	public static LinkedHashMap<String, ArrayList<ProductsModel>> getLinkedItems(int itemID,int subsetID){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet rsEach = null;
		LinkedHashMap<String, ArrayList<ProductsModel>> fullData= new LinkedHashMap<String, ArrayList<ProductsModel>>();
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getLinkedItemsLists");
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			sql = PropertyAction.SqlContainer.get("getLinkedItems");
			while (rs.next())
			{
				rsEach = null;
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,itemID);
				pstmt.setInt(2, rs.getInt("ITEM_LINK_TYPE_ID"));
				pstmt.setInt(3, subsetID);
				rsEach = pstmt.executeQuery();
				ArrayList<ProductsModel> eachLinkedLists = new ArrayList<ProductsModel>();
				while(rsEach.next()){
					ProductsModel eachItems = new ProductsModel();
					eachItems.setItemId(rsEach.getInt("ITEM_ID"));
					eachItems.setPartNumber(rsEach.getString("PART_NUMBER"));	
					eachItems.setItemPriceId(rsEach.getInt("ITEM_PRICE_ID"));
					eachItems.setShortDesc(rsEach.getString("SHORT_DESC"));
					eachItems.setImageType(rsEach.getString("IMAGE_TYPE"));
					eachItems.setImageName(rsEach.getString("IMAGE_NAME"));
					eachItems.setBrandName(rsEach.getString("BRAND_NAME"));
					eachItems.setManufacturerPartNumber(rsEach.getString("MANUFACTURER_PART_NUMBER"));
					eachItems.setManufacturerName(rsEach.getString("MANUFACTURER_NAME"));
					eachItems.setItemTitleString(rsEach.getString("PAGE_TITLE"));
					eachLinkedLists.add(eachItems);
				}
				fullData.put(rs.getString("LINK_TYPE_NAME"), eachLinkedLists);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBResultSet(rsEach);
		}
		return fullData;

	}
	public static String redirectCatWebId(String webID){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String partNumber = "";
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("redirectsFromCatalog");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,webID);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				partNumber = rs.getString("PART_NUMBER");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
		}
		return partNumber;

	}


	public static int addToRfqCartSession(String sessionId, int userId, int qty, String productIdList,int itemPriceId)
	{
		int count = 0;
		Connection  conn = null;
		try {
        conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }	
        try
        {
        	ArrayList<Integer> cartId = new ArrayList<Integer>();
        	cartId = selectFromRfqCartSession(conn,userId, Integer.parseInt(productIdList),sessionId);
        	if(cartId!=null && cartId.size()>0)
        	{
        	count = updateRfqCart(conn, userId,  cartId.get(0), qty, cartId.get(1));
        	}
        	else
        	{
        		count = insertItemToRfqCart(conn, userId, Integer.parseInt(productIdList), qty, sessionId,itemPriceId);
        	}
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBConnection(conn);
        }
        return count;
	}
	
	public static int addToRfqCart(String sessionId, int userId, int qty, String productIdList,int itemPriceId)
	{
		int count = 0;
		Connection  conn = null;
		try {
        conn = ConnectionManager.getDBConnection();
        } catch (SQLException e) { 
            e.printStackTrace();
        }	
        try
        {
        	ArrayList<Integer> cartId = new ArrayList<Integer>();
        	cartId = selectFromRfqCart(conn,userId, Integer.parseInt(productIdList));
        	if(cartId!=null && cartId.size()>0)
        	{
        	count = updateRfqCart(conn, userId,  cartId.get(0), qty, cartId.get(1));
        	}
        	else
        	{
        		count = insertItemToRfqCart(conn, userId, Integer.parseInt(productIdList), qty, sessionId,itemPriceId);
        	}
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        	ConnectionManager.closeDBConnection(conn);
        }
        return count;
	}
	
	
	public static int insertItemToRfqCart(Connection conn,int userId,int itemId,int qty, String sessionId,int itemPriceId)
	{
		int count = -1;
		String sql = "INSERT INTO RFQ_CART (RFQ_CART_ID,USER_ID,ITEM_ID,ITEM_PRICE_ID,MANUFACTURER_PART_NO,MANUFACTURER_NAME,QTY,SHORT_DESC,UPDATED_DATETIME,SESSIONID) SELECT RFQ_CART_ID_SEQ.NEXTVAL,? USER_ID, ITEM_ID,ITEM_PRICE_ID, MANUFACTURER_PART_NUMBER,MANUFACTURER_NAME,? QTY,SHORT_DESC,SYSDATE,? SESSIONID FROM ITEM_DETAILS_MV WHERE ITEM_PRICE_ID = ?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, qty);
			pstmt.setString(3, sessionId);
			pstmt.setInt(4, itemPriceId);
			count = pstmt.executeUpdate();
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
		return count;
	}
	
	public static ArrayList<Integer> selectFromRfqCart(Connection conn,int userId,int itemId)
	{
		ArrayList<Integer> cartId = new ArrayList<Integer>();
		String sql = "SELECT RFQ_CART_ID, QTY FROM RFQ_CART WHERE USER_ID = ? AND ITEM_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, itemId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				cartId.add(rs.getInt("RFQ_CART_ID"));
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
	
	
	public static int deleteFromRfqCart(int userId,int cartId)
	{
		int count = -1;
		Connection conn = null;
		String sql = "DELETE FROM RFQ_CART WHERE USER_ID=? AND RFQ_CART_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, cartId);
			count = pstmt.executeUpdate();
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
			ConnectionManager.closeDBConnection(conn);	
		}
		return count;
	}
	
	
	public static ArrayList<ProductsModel> getRfqItemList(int checkOciUser,int userId,String sessionId){
		Connection  conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    ArrayList<ProductsModel> productListData = new ArrayList<ProductsModel>();
		 try {
	            conn = ConnectionManager.getDBConnection();
	       
	        	
	        	if(checkOciUser>0)
	        	{
	        		String sql = "SELECT RFQ_CART_ID,ITEM_ID,ITEM_PRICE_ID, QTY,MANUFACTURER_NAME,MANUFACTURER_PART_NO,QTY,SHORT_DESC FROM RFQ_CART WHERE USER_ID = ? AND SESSIONID=?";
	    			pstmt = conn.prepareStatement(sql);
	    			pstmt.setInt(1, userId);
	    			pstmt.setString(2, sessionId);
	    			rs = pstmt.executeQuery();
	        	}
	        	else
	        	{
	        	    String sql = "SELECT RFQ_CART_ID,ITEM_ID,ITEM_PRICE_ID, QTY,MANUFACTURER_NAME,MANUFACTURER_PART_NO,QTY,SHORT_DESC FROM RFQ_CART WHERE USER_ID = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				rs = pstmt.executeQuery();
	        	}
				
				while(rs.next())
				{
					ProductsModel cartListVal = new ProductsModel();
					cartListVal.setProductListId(rs.getInt("RFQ_CART_ID"));
					cartListVal.setItemId(rs.getInt("ITEM_ID"));
					cartListVal.setQty(rs.getInt("QTY"));
					cartListVal.setShortDesc(rs.getString("SHORT_DESC"));
					cartListVal.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
					cartListVal.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
					cartListVal.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NO"));
					
					productListData.add(cartListVal);
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			return productListData;
	}
	public static int updateRfqCart(Connection conn,int userId,int cartId,int qty,int cartQty)
	{
		int count = -1;
		String sql = "UPDATE RFQ_CART SET QTY = ? WHERE USER_ID=? AND RFQ_CART_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (cartQty+qty));
			pstmt.setInt(2, userId);
			pstmt.setInt(3, cartId);
			count = pstmt.executeUpdate();
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
		return count;
	}
	
	public static ArrayList<Integer> selectFromRfqCartSession(Connection conn,int userId,int itemId,String sessionId)
	{
		ArrayList<Integer> cartId = new ArrayList<Integer>();
		String sql = "SELECT RFQ_CART_ID, QTY FROM RFQ_CART WHERE USER_ID = ? AND ITEM_ID=? AND SESSIONID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, itemId);
			pstmt.setString(3, sessionId);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				cartId.add(rs.getInt("RFQ_CART_ID"));
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
	
	public static ArrayList<Integer> customerAlsoBoughtDAO(String itemIdString){
		
		ArrayList<Integer> customerAlsoBoughtList = new ArrayList<Integer>();
		String sql = "SELECT ORDER_ITEM_ID,ORDER_ID,ITEM_ID FROM ORDER_ITEMS WHERE ORDER_ID IN (SELECT DISTINCT ORDER_ID FROM ORDER_ITEMS WHERE ITEM_ID IN ("+itemIdString+")) AND ITEM_ID NOT IN ("+itemIdString+")";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				customerAlsoBoughtList.add(rs.getInt("ITEM_ID"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
		}
		return customerAlsoBoughtList;
	}
	
	public static ArrayList<ProductsModel> getRecentlyOrderedItems(int userId,String sSubsetId)
	{

		  Connection  conn = null;
		  PreparedStatement pstmt = null;   
		  ResultSet rs = null;
		  ArrayList<ProductsModel> recentOrders=new ArrayList<ProductsModel>();
		  ArrayList<ProductsModel> recentOrderswithoutDuplicate=new ArrayList<ProductsModel>();
		  Set<Integer> recentitemSet = new HashSet<Integer>();
		  try{
		   conn = ConnectionManager.getDBConnection();
		   String sql=PropertyAction.SqlContainer.get("getRecentOrderdItems");
		   pstmt = conn.prepareStatement(sql);
		   pstmt.setInt(1, userId);
		   pstmt.setString(2, sSubsetId);
		   rs=pstmt.executeQuery();
		   while(rs.next())
		   {
		   ProductsModel recentItems=new ProductsModel();
		   recentItems.setItemId(rs.getInt("ITEM_ID"));
		   recentItems.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
		   recentItems.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
		   recentItems.setBrandName(rs.getString("BRAND_NAME"));
		   recentItems.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
		   recentItems.setPartNumber(rs.getString("PART_NUMBER"));
		   recentItems.setShortDesc(rs.getString("SHORT_DESC"));
		   recentItems.setImageName(rs.getString("IMAGE_NAME"));
		   recentItems.setImageType(rs.getString("IMAGE_TYPE"));
		   recentItems.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
		   recentItems.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
		   recentItems.setUom("SALES_UOM");
		   recentItems.setUpc(rs.getString("UPC"));
		   recentOrders.add(recentItems);
		   }
		   for( ProductsModel itemModel : recentOrders ) {
		       if( recentitemSet.add( itemModel.getItemId() )) {
		        recentOrderswithoutDuplicate.add(itemModel );
		       }
		   }
		   
		  } catch (Exception e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }finally{
		   ConnectionManager.closeDBPreparedStatement(pstmt);
		   ConnectionManager.closeDBConnection(conn);
		   ConnectionManager.closeDBResultSet(rs);
		  }
		  return recentOrderswithoutDuplicate;
		}
	public static ArrayList<ProductsModel> getRecentlyOrderedItems(int userId, int subsetId, int generalSubsetId)
	{
		long startTimer = CommonUtility.startTimeDispaly();
		  Connection  conn = null;
		  PreparedStatement pstmt = null;   
		  ResultSet rs = null;
		  ArrayList<ProductsModel> recentOrders=new ArrayList<ProductsModel>();
		  ArrayList<ProductsModel> recentOrderswithoutDuplicate=new ArrayList<ProductsModel>();
		  Set<Integer> recentitemSet = new HashSet<Integer>();
		  try{
		   conn = ConnectionManager.getDBConnection();
		   String sql=PropertyAction.SqlContainer.get("getRecentOrderdItemsBySubset");
		   pstmt = conn.prepareStatement(sql);
		   pstmt.setInt(1, userId);
		   pstmt.setInt(2, subsetId);
		   pstmt.setInt(3, generalSubsetId);
		   rs=pstmt.executeQuery();
		   while(rs.next())
		   {
		   ProductsModel recentItems=new ProductsModel();
		   recentItems.setItemId(rs.getInt("ITEM_ID"));
		   recentItems.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
		   recentItems.setManufacturerPartNumber(rs.getString("MANUFACTURER_PART_NUMBER"));
		   recentItems.setBrandName(rs.getString("BRAND_NAME"));
		   recentItems.setManufacturerName(rs.getString("MANUFACTURER_NAME"));
		   recentItems.setPartNumber(rs.getString("PART_NUMBER"));
		   recentItems.setShortDesc(rs.getString("SHORT_DESC"));
		   recentItems.setImageName(rs.getString("IMAGE_NAME"));
		   recentItems.setImageType(rs.getString("IMAGE_TYPE"));
		   recentItems.setMinOrderQty(rs.getInt("MIN_ORDER_QTY"));
		   recentItems.setOrderInterval(rs.getInt("ORDER_QTY_INTERVAL"));
		   recentItems.setUom("SALES_UOM");
		   recentItems.setUpc(rs.getString("UPC"));
		   recentOrders.add(recentItems);
		   }
		   for( ProductsModel itemModel : recentOrders ) {
		       if( recentitemSet.add( itemModel.getItemId() )) {
		        recentOrderswithoutDuplicate.add(itemModel );
		       }
		   }
		   
		  } catch (Exception e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }finally{
		   ConnectionManager.closeDBPreparedStatement(pstmt);
		   ConnectionManager.closeDBConnection(conn);
		   ConnectionManager.closeDBResultSet(rs);
		  }
		  CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		  return recentOrderswithoutDuplicate;
		}
	public static ArrayList<Integer> getCustomerAlsoViewedItems(int itemId){

		  Connection  conn = null;
		  PreparedStatement pstmt = null;   
		  ResultSet rs = null;
		  ArrayList<Integer> customersAlsoViewed=new ArrayList<Integer>();
		 
		  try{
		   conn = ConnectionManager.getDBConnection();
		   String sql=PropertyAction.SqlContainer.get("getCustomersAlsoViewed");
		   pstmt = conn.prepareStatement(sql);
		   pstmt.setString(1, CommonUtility.validateParseIntegerToString(itemId));
		   rs=pstmt.executeQuery();
		   
		   LinkedHashMap<Integer, String> itemIdMap = new LinkedHashMap<Integer, String>();
		   while(rs.next()){
			   customersAlsoViewed.add(rs.getInt("ITEM_ID"));
		   }
		   
		   if(customersAlsoViewed!=null && !customersAlsoViewed.isEmpty()){
			   Set<Integer> hs = new HashSet<Integer>();
			   hs.addAll(customersAlsoViewed);
			   customersAlsoViewed.clear();
			   customersAlsoViewed.addAll(hs);
		   }
		   		
		  } catch (Exception e) {
		   e.printStackTrace();
		  }finally{
		   ConnectionManager.closeDBPreparedStatement(pstmt);
		   ConnectionManager.closeDBConnection(conn);
		   ConnectionManager.closeDBResultSet(rs);
		  }
		  return customersAlsoViewed;
	}
	
	
/*	public static ArrayList<ProductsModel> getSpecialItems(int subsetId)
	{
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String partNumber = "";
		ArrayList<ProductsModel> itemFilterData=new ArrayList<ProductsModel>();
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getSpecialItems");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,subsetId);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				ProductsModel productData=new ProductsModel();
				productData.setShortDesc(rs.getString("BRAND_NAME")+"_"+rs.getString("MANUFACTURER_PART_NUMBER")+"_"+rs.getString("SHORT_DESC"));
				productData.setItemId(rs.getInt("ITEM_ID"));
				productData.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				productData.setImageName(rs.getString("IMAGE_NAME"));
				productData.setImageType(rs.getString("IMAGE_TYPE"));
				productData.setPrice(rs.getDouble("PRICE"));
				itemFilterData.add(productData);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
		}
		return itemFilterData;
	}
	
	public static ArrayList<ProductsModel> getClearanceItems(int subsetId)
	{
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String partNumber = "";
		ArrayList<ProductsModel> itemFilterData=new ArrayList<ProductsModel>();
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getClearanceItems");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,subsetId);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				ProductsModel productData=new ProductsModel();
				productData.setShortDesc(rs.getString("BRAND_NAME")+"_"+rs.getString("MANUFACTURER_PART_NUMBER")+"_"+rs.getString("SHORT_DESC"));
				productData.setItemId(rs.getInt("ITEM_ID"));
				productData.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
				productData.setImageName(rs.getString("IMAGE_NAME"));
				productData.setImageType(rs.getString("IMAGE_TYPE"));
				productData.setPrice(rs.getDouble("PRICE"));
				itemFilterData.add(productData);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
		}
		return itemFilterData;
	}*/
	public static LinkedHashMap<String,ArrayList<MenuAndBannersModal>> getCategoryBannersV2(int categoryId)
	{
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LinkedHashMap<String,ArrayList<MenuAndBannersModal>> allBannerData = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
		MenuAndBannersModal bannerData=new MenuAndBannersModal();
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getTaxonomyInfo");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,categoryId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				bannerData.setTopBannerType(rs.getString("TOP_BANNER_LIST_TYPE"));
				bannerData.setTopBannerId(rs.getInt("TOP_BANNER_ID"));
				bannerData.setLeftBannerType(rs.getString("LEFT_BANNER_LIST_TYPE"));
				bannerData.setLeftBannerId(rs.getInt("LEFT_BANNER_ID"));
				bannerData.setRightBannerType(rs.getString("RIGHT_BANNER_LIST_TYPE"));
				bannerData.setRightBannerId(rs.getInt("RIGHT_BANNER_ID"));
				bannerData.setBottomBannerType(rs.getString("BOTTOM_BANNER_LIST_TYPE"));
				bannerData.setBottomBannerId(rs.getInt("BOTTOM_BANNER_ID"));
			}
			LinkedHashMap<String,ArrayList<MenuAndBannersModal>> defaultBanner = null;
			if(CommonDBQuery.defaultCategoryBanners!=null){
				defaultBanner = CommonDBQuery.defaultCategoryBanners;
			}else{
				CommonDBQuery.getDefaultCategoryBanners();
				defaultBanner = CommonDBQuery.defaultCategoryBanners;
			}
			LinkedHashMap<String,ArrayList<MenuAndBannersModal>> valueListBanners = getCategoryBanners(categoryId+"");
			String bannerType = "";
			bannerType = CommonUtility.validateString(bannerData.getTopBannerType());
			ArrayList<MenuAndBannersModal> eachBanners = new ArrayList<MenuAndBannersModal>();
			if(bannerType.equalsIgnoreCase("null")  && defaultBanner!=null && defaultBanner.get("TOP")!=null && defaultBanner.get("TOP").size()>0){
				eachBanners =defaultBanner.get("TOP");
			}else{
				if(bannerType.equalsIgnoreCase("BL")){
					eachBanners =  getBannerDetails(""+bannerData.getTopBannerId(),"BANNER_ID");
				}else if(bannerType.equalsIgnoreCase("B")){
					eachBanners = valueListBanners.get("top");
				}
			}
			allBannerData.put("top",eachBanners);
			eachBanners = new ArrayList<MenuAndBannersModal>();
			bannerType = CommonUtility.validateString(bannerData.getBottomBannerType());
			if(bannerType.equalsIgnoreCase("null")  && defaultBanner!=null && defaultBanner.get("BOTTOM")!=null && defaultBanner.get("BOTTOM").size()>0){
				eachBanners = defaultBanner.get("BOTTOM");
			}else{
				if(bannerType.equalsIgnoreCase("BL")){
					eachBanners =  getBannerDetails(""+bannerData.getBottomBannerId(),"BANNER_ID");
				}else if(bannerType.equalsIgnoreCase("B")){
					eachBanners = valueListBanners.get("bottom");
				}
			}
			allBannerData.put("bottom",eachBanners);
			eachBanners = new ArrayList<MenuAndBannersModal>();
			bannerType = CommonUtility.validateString(bannerData.getLeftBannerType());
			if(bannerType.equalsIgnoreCase("null") && defaultBanner!=null && defaultBanner.get("LEFT")!=null && defaultBanner.get("LEFT").size()>0){
				eachBanners = defaultBanner.get("LEFT");
			}else{
				if(bannerType.equalsIgnoreCase("BL")){
					eachBanners = getBannerDetails(""+bannerData.getLeftBannerId(),"BANNER_ID");
				}else if(bannerType.equalsIgnoreCase("B")){
					eachBanners = valueListBanners.get("left");
				}
			}
			allBannerData.put("left",eachBanners);
			eachBanners = new ArrayList<MenuAndBannersModal>();
			bannerType = CommonUtility.validateString(bannerData.getRightBannerType());
			if(bannerType.equalsIgnoreCase("null")  && defaultBanner!=null && defaultBanner.get("RIGHT")!=null && defaultBanner.get("RIGHT").size()>0){
				eachBanners = defaultBanner.get("RIGHT");
			}else{
				if(bannerType.equalsIgnoreCase("BL")){
					eachBanners = getBannerDetails(""+bannerData.getRightBannerId(),"BANNER_ID");
				}else if(bannerType.equalsIgnoreCase("B")){
					eachBanners = valueListBanners.get("right");
				}
			}
			allBannerData.put("right",eachBanners);
		}catch(Exception e){ 
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
		}
		return allBannerData;
	}
	public static ArrayList<MenuAndBannersModal> getBannerDetails(String requestKey,String requestType){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<MenuAndBannersModal> bannerDetails=new ArrayList<MenuAndBannersModal>();
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "";
			if(CommonUtility.validateString(requestType).equalsIgnoreCase("BANNER_NAME")){
				sql = PropertyAction.SqlContainer.get("getBannerDetailsName");
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,requestKey);
			}else if(CommonUtility.validateString(requestType).equalsIgnoreCase("BANNER_ID")){
				sql = PropertyAction.SqlContainer.get("getBannerDetailsID");
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,CommonUtility.validateNumber(requestKey));
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				MenuAndBannersModal bannerData=new MenuAndBannersModal();
				bannerData.setImageName(rs.getString("BANNER_IMAGE_NAME"));
				bannerData.setImageType(rs.getString("IMAGE_TYPE"));
				bannerData.setImageURL(rs.getString("BANNER_URL"));
//				bannerData.setImagePosition(rs.getString("BANNER_POSITION"));
//				bannerData.setBannerScroll(rs.getString("SCROLLABLE"));
//				bannerData.setBannerNumberofItem(rs.getString("NUMOFIMGSTOSCROLL"));
//				bannerData.setBannerDirection(rs.getString("DIRECTION"));
//				bannerData.setBannerDelay(rs.getString("DELAY"));
//				
//				
//				
//				bannerData.setImageName(rs.getString("BANNER_IMAGE_NAME"));
				bannerDetails.add(bannerData);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
			ConnectionManager.closeDBResultSet(rs);
		}
		return bannerDetails;
	}
	
	public static int updateCartSessionId(String previousSessionId, String currentSessionId) {
		  Connection conn = null;
		  PreparedStatement pstmt = null;
		  int count = 0;

		  try {
		   conn = ConnectionManager.getDBConnection();
		    if(!checkPreviousSessionUser(conn, previousSessionId)){
		    pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("updateCartSessionId"));
		    pstmt.setString(1, currentSessionId);
		    pstmt.setString(2, previousSessionId);
		    count = pstmt.executeUpdate();
		    }

		  } catch (SQLException e) {
		   e.printStackTrace();

		  } catch (Exception e) {
		   e.printStackTrace();
		  } finally {
		   ConnectionManager.closeDBPreparedStatement(pstmt);
		   ConnectionManager.closeDBConnection(conn);
		  }
		  return count;

		 }
	public static boolean checkPreviousSessionUser(Connection conn, String previousSessionId) {
		  boolean userLoggedIn = false;
		  PreparedStatement pstmt = null;
		  ResultSet rs = null;
		  try {
		   pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("getCartUserId"));
		   pstmt.setString(1, previousSessionId);
		   rs = pstmt.executeQuery();
		   while (rs.next()) {
		    if (rs.getInt("USER_ID") > 1) {
		     userLoggedIn = true;
		    }
		   }

		  } catch (SQLException e) {
		   e.printStackTrace();
		  } catch (Exception e) {
		   e.printStackTrace();
		  } finally {
		   ConnectionManager.closeDBResultSet(rs);
		   ConnectionManager.closeDBPreparedStatement(pstmt);
		  }
		  return userLoggedIn;
		 }
	
	public static int removeCartItems(List<Integer> cartItemIds,int userId, String sessionId) {
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = -1;
		try{
			conn = ConnectionManager.getDBConnection();
			String query = PropertyAction.SqlContainer.get("deleteCartQuery");
			pstmt = conn.prepareStatement(query);
			
			for(int cartItemId : cartItemIds) {
				pstmt.setInt(1, userId);
				pstmt.setInt(2, cartItemId);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			count = 1;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
        	ConnectionManager.closeDBResultSet(rs);
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}
	public static boolean verifyAddToCartRestrictions(ProductsModel product, int qty) {
		boolean status = true;
		if(qty <= 0) {
			status = false;
		}
		else if(qty < product.getMinOrderQty()){
			status = false;
		}
		else if(qty % product.getOrderInterval() != 0) {
			status = false;
		}
		return status;
	}
	public static int insertIntoCartWithShipVia(int userId,int itemId,int qty, String sessionId, String lineItemComment,String catalogId,String uom, String price, int minOrderQty, double itemWeight, double itemUnit, String itemLevelShipvia,String itemLevelShipviaDesc,LinkedHashMap<String, Object> utilityMap,String additionalProperties)
	{
		int count = -1;
		int siteId = 0;
		if(CommonDBQuery.getGlobalSiteId()>0){
    		siteId = CommonDBQuery.getGlobalSiteId();
		}
		String sql = PropertyAction.SqlContainer.get("insertItemsToCartWithShipVia");
		Connection  conn = null;
		PreparedStatement pstmt = null;
		boolean considerLineItemComment = true;
		try{
			conn = ConnectionManager.getDBConnection();
			if(utilityMap!=null && utilityMap.get("considerLineItemComment")!=null) {
				considerLineItemComment = (boolean) utilityMap.get("considerLineItemComment");
			}
			if(!considerLineItemComment) {
				pstmt = conn.prepareStatement(PropertyAction.SqlContainer.get("insertItemsToCartWithShipViaWitoutLineComment"));
				pstmt.setInt(1, itemId);
				pstmt.setInt(2, qty);
				pstmt.setString(3, sessionId);
				pstmt.setInt(4, userId);
				pstmt.setInt(5, siteId);
				pstmt.setString(6, catalogId);
				if(CommonUtility.validateString(uom).length()==0){
					pstmt.setString(7," ");	
				}else{
					pstmt.setString(7, uom);	
				}
				pstmt.setString(8, price);
				pstmt.setInt(9, minOrderQty);
				pstmt.setDouble(10, itemWeight);
				pstmt.setDouble(11, itemUnit);
				pstmt.setString(12, itemLevelShipvia);
				pstmt.setString(13, itemLevelShipviaDesc);
				pstmt.setString(14, additionalProperties);
			}else {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, itemId);
				pstmt.setInt(2, qty);
				pstmt.setString(3, sessionId);
				pstmt.setInt(4, userId);
				pstmt.setInt(5, siteId);
				pstmt.setString(6, lineItemComment);
				pstmt.setString(7, catalogId);
				if(CommonUtility.validateString(uom).length()==0){
					pstmt.setString(8," ");	
				}else{
					pstmt.setString(8, uom);	
				}
				pstmt.setString(9, price);
				pstmt.setInt(10, minOrderQty);
				pstmt.setDouble(11, itemWeight);
				pstmt.setDouble(12, itemUnit);
				pstmt.setString(13, itemLevelShipvia);
				pstmt.setString(14, itemLevelShipviaDesc);
				pstmt.setString(15, additionalProperties);
			}
			
			
			
			count = pstmt.executeUpdate();
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
	    	ConnectionManager.closeDBConnection(conn);		
		}
		return count;
	}
	public static void clearCartCustom(int userId, String keyword)
	{
		Connection conn = null;
		    PreparedStatement pstmt = null;
		   
		    try {
		    	conn = ConnectionManager.getDBConnection();
		    	String sql = PropertyAction.SqlContainer.get("deleteFromCartCustom");
		    	pstmt = conn.prepareStatement(sql);
		    	pstmt.setInt(1, userId);
		    	pstmt.setString(2, "%"+keyword+"%");
		    	int count = pstmt.executeUpdate();
		    	
		    } catch (SQLException e) { 
	            e.printStackTrace();
	        }	
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);
	        }
	}
	public static int clearCartBySessionIdCustom(int userId, String sessionId, String keyword){
		int count = -1;
		
		Connection  conn = null;
		PreparedStatement pstmt = null;
		
		
		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("deleteFromCartCustomBySession");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setString(2, sessionId);
			pstmt.setString(3, "%"+keyword+"%");
			count = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
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
		return count;
	}
	
	public static int updateCartWithShipVia(int userId, String shipVia, String cartId, String itemId){
		int count = -1;
		Connection  conn = null;
		PreparedStatement pstmt = null;
		try
		{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("updateCartItemLevelShipVia");
			for(int i =1; i<itemId.split(",").length; i++){
				ConnectionManager.closeDBPreparedStatement(pstmt);
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, shipVia);
				pstmt.setInt(2, userId);
				pstmt.setString(3, cartId.split(",")[i]);
				pstmt.setString(4, itemId.split(",")[i]);
				count = pstmt.executeUpdate();
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
		finally{
        	ConnectionManager.closeDBPreparedStatement(pstmt);
        	ConnectionManager.closeDBConnection(conn);
        }
		return count;
	}
	
	public static Map<Integer, String> checkManufacturerMinDollarDAO(LinkedHashMap<String, Object> contentObject) {
		Map<Integer, Map<String, Double>> manufacturerItemTotalMap= new HashMap<>();
		Map<Integer, Double> manufacturerSpecificDollarLimit=new HashMap<>();
		Map<Integer, String> manufacturerThresholdMessage=new HashMap<>();
		try {
			ArrayList<ProductsModel> productListData = (ArrayList<ProductsModel>) contentObject.get("productListData");
			Double total;
			for (ProductsModel prod : productListData) {

				//This block will store the item total specific to manufacturer with it's respective Id
				Map<String, Double> manufacturerItemTotal=new HashMap<>();
				if(manufacturerItemTotalMap.containsKey(prod.getManufacturerId())) {
					total=manufacturerItemTotalMap.get(prod.getManufacturerId()).get(prod.getManufacturerName());
					//total+=(prod.getQty()*prod.getPrice());
					total+=prod.getTotal();
					manufacturerItemTotal.put(prod.getManufacturerName(),total);
					manufacturerItemTotalMap.put(prod.getManufacturerId(),manufacturerItemTotal);
				}else{
					//manufacturerItemTotal.put(prod.getManufacturerName(), prod.getQty()*prod.getPrice());
					manufacturerItemTotal.put(prod.getManufacturerName(), prod.getTotal());
					manufacturerItemTotalMap.put(prod.getManufacturerId(),manufacturerItemTotal );
				}

				// This block will store threshold of the each manufacturer from the custom fields and respective Id

				if(CommonDBQuery.manfCustomField!=null && !manufacturerSpecificDollarLimit.containsKey(prod.getManufacturerId())){
					manufacturerSpecificDollarLimit.put(prod.getManufacturerId(), Double.valueOf(CommonDBQuery.manfCustomField.get(prod.getManufacturerId()+"_MIN_DOLLAR_LIMIT")!=null?CommonDBQuery.manfCustomField.get(prod.getManufacturerId()+"_MIN_DOLLAR_LIMIT"):"0.0"));
				} 
			}


			// This block will compare and update the display messages related to specific vendor if threshold not meet

			if (manufacturerItemTotalMap.size() == manufacturerSpecificDollarLimit.size()) {

				for (Map.Entry<Integer, Double> dollarLimit: manufacturerSpecificDollarLimit.entrySet()) {
					for (Map.Entry<Integer, Map<String, Double>> itemTotal: manufacturerItemTotalMap.entrySet()) {
						if(itemTotal.getKey().equals(dollarLimit.getKey())) {
							for(Map.Entry<String, Double> item: itemTotal.getValue().entrySet()){
								if(dollarLimit.getValue() > 0.0 && (dollarLimit.getValue()>item.getValue())) {
									manufacturerThresholdMessage.put(itemTotal.getKey(),item.getKey());
								} 
							} 	
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return manufacturerThresholdMessage;
	}
	
	public static int deleteSelectedItemFromCart(int userId,String cartIds){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = -1;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("deleteCartQuery");
			Integer[] splitCartId = Arrays.stream(cartIds.split(","))
					.map(String::trim)
					.map(Integer::valueOf)
					.toArray(Integer[]::new);
			pstmt = conn.prepareStatement(sql);
			for(Integer cartId : splitCartId){
				pstmt.setInt(1, userId);
				pstmt.setInt(2, cartId);
				count = pstmt.executeUpdate();
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
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return count;
	}
	
	public static ArrayList<Integer> selectFromCartAP(int userId,int itemId,String uom,int addressBookId){
		ArrayList<Integer> cartId = new ArrayList<Integer>();
		Connection conn = null;
		String sql = PropertyAction.SqlContainer.get("selectFromCartAP");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, itemId);
			if(CommonUtility.validateString(uom).length()==0){
				pstmt.setString(3," ");
			} else {
				pstmt.setString(3, CommonUtility.validateString(uom));
			}
			pstmt.setInt(4, addressBookId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cartId.add(rs.getInt("CART_ID"));
				cartId.add(rs.getInt("QTY"));
				cartId.add(rs.getInt("BC_ADDRESS_BOOK_ID"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
	    	ConnectionManager.closeDBPreparedStatement(pstmt);	
	    	ConnectionManager.closeDBConnection(conn);
		}
		return cartId;
	}
	
	public static int inactiveItemInCartCheckout(int userId,String cartIds){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = -1;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("logicalDeleteCartQuery");
			Integer[] splitCartId = Arrays.stream(cartIds.split(","))
					.map(String::trim)
					.map(Integer::valueOf)
					.toArray(Integer[]::new);
			pstmt = conn.prepareStatement(sql);
			for(Integer cartId : splitCartId){
				pstmt.setInt(1, userId);
				pstmt.setInt(2, cartId);
				count = pstmt.executeUpdate();
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
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return count;
	}
	
	public static int resetCartCheckoutType(int userId){
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = -1;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("resetCheckoutTypeInCart");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			count = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return count;
	}
	public static LinkedHashMap<String, Object> getPendingApproveCart(int userId,LinkedHashMap<String, Object> contentObject, int buyingCompanyId) {
		if(contentObject==null)
			contentObject = new LinkedHashMap<String, Object>();
		System.out.println("group name called");
		ResultSet rs = null;
	    Connection  conn = null;
	    PreparedStatement pstmt = null;
	 	ArrayList<ProductsModel> groupListData = new ArrayList<ProductsModel>();
		ArrayList<ProductsModel> savedCartData = new ArrayList<ProductsModel>();
		ArrayList<ProductsModel> approveCartData = new ArrayList<ProductsModel>();
		String sortoprtion = null;
		int siteId = 21;
	    try{
	    	
	    	if(CommonDBQuery.getGlobalSiteId()>0){
	    		siteId = CommonDBQuery.getGlobalSiteId();
	    	}
	    	  
	    	 if(CommonDBQuery.getSystemParamtersList().get("SAVED_GROUP_SORT") !=null) {
	    		  sortoprtion = CommonDBQuery.getSystemParamtersList().get("SAVED_GROUP_SORT");
	    	 }else {
	    		  sortoprtion = "UPDATED_DATETIME DESC";
	    	 }
        	conn = ConnectionManager.getDBConnection();
        	String sql = PropertyAction.SqlContainer.get("getPendingApproveCart");
        	System.out.println(sql);
        	pstmt = conn.prepareStatement(sql);
        	pstmt.setInt(1, siteId);
        	pstmt.setInt(2, userId);
        	pstmt.setInt(3, buyingCompanyId);
        	pstmt.setInt(4, siteId);
        	pstmt.setInt(5, userId);
        	pstmt.setString(6,sortoprtion);
        	rs = pstmt.executeQuery();
        	
        	while(rs.next())
        	{
        		ProductsModel productVal = new ProductsModel();
        		productVal.setProductListId(rs.getInt("SAVED_LIST_ID"));
        		productVal.setProductListName(rs.getString("SAVED_LIST_NAME"));
        		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(rs.getString("UPDATED_DATETIME"));
                String updatedDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
                productVal.setDate(updatedDate);
        		productVal.setAssignedShipTo(rs.getInt("ASSIGNED_SHIP_TO"));
        		productVal.setStatus(rs.getString("STATUS"));
        		productVal.setIsSharedCart(rs.getString("IS_SHARED"));
        		if(rs.findColumn("ITEM_COUNTS")>0){
        			productVal.setGroupItemCount(rs.getString("ITEM_COUNTS"));	
        		}
        		String senderUserID = rs.getString("SENT_BY_APPR_USER_ID");
        		if(CommonUtility.validateString(rs.getString("TYPE")).equalsIgnoreCase("P") || CommonUtility.validateString(rs.getString("TYPE")).equalsIgnoreCase("B")){
        			productVal.setGroupType(rs.getString("TYPE"));
        			groupListData.add(productVal);
        		}else if(rs.getString("TYPE").trim().equalsIgnoreCase("A") && rs.getString("STATUS").trim().equalsIgnoreCase("N")){
	        			//if(sessionUserId.equalsIgnoreCase(approveUserId[0])){
	        				productVal.setAppvalsenderUserID(senderUserID);
	    					approveCartData.add(productVal);
	            		//}
        			}else if(rs.getString("TYPE").trim().equalsIgnoreCase("C")){
        			savedCartData.add(productVal);
        		}
        	}
        	if(CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP")!=null && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ADVANCED_PRODUCTGROUP")).equalsIgnoreCase("Y")){
        		sql = "SELECT * FROM PRODUCT_GROUPS_V2 WHERE CIMM_USER_ID=?";
            	System.out.println(sql);
            	ConnectionManager.closeDBResultSet(rs);
            	ConnectionManager.closeDBPreparedStatement(pstmt);
            	pstmt = conn.prepareStatement(sql);
            	pstmt.setInt(1, userId);
            	rs = pstmt.executeQuery();
            	while(rs.next())
            	{
            		ProductsModel productVal = new ProductsModel();
            		productVal.setProductListId(rs.getInt("PRODUCT_GROUP_ID"));
            		productVal.setProductListName(rs.getString("PRODUCT_GROUP_NAME"));
            		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(rs.getString("UPDATED_DATETIME"));
                    String updatedDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
                    productVal.setDate(updatedDate);
            		productVal.setLevelNumber(rs.getInt("LEVEL_NUMBER"));
            		productVal.setLevel1Menu(rs.getString("PARENT_GROUP_ID"));
            		productVal.setImageName(rs.getString("IMAGE_NAME"));
            		productVal.setStatus(rs.getString("PUBLIC_PRODUCT_GROUP"));
            		groupListData.add(productVal);
            	}
        	}
        	
        	contentObject.put("groupListData", groupListData);
        	contentObject.put("savedCartData", savedCartData);
        	contentObject.put("approveCartData", approveCartData);
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
	    	ConnectionManager.closeDBConnection(conn);
        }
        System.out.println("group end called");
		return contentObject;
	}

	public static String getCategoryNameFromTaxonomyId(String codeId) {
		Connection  conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String categoryName = "";
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = PropertyAction.SqlContainer.get("getCategoryNameByTaxonomyID");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, codeId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				categoryName = rs.getString("CATEGORY_NAME");
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
		finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return categoryName;		
	}
	


	    public static int updateGroupToPublic(int buyingCompanyId,String groupType,int groupId,String key)
		{
			Connection  conn = null;
			PreparedStatement pstmt = null;
			int count=0;
	        try
	        {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("updateGroupToPublic");
	        	pstmt = conn.prepareStatement(sql);
	        	if(CommonUtility.validateString(key).equalsIgnoreCase("Y")) {
	        		pstmt.setInt(1,buyingCompanyId);
	        	}else {
				pstmt.setInt(1,0);
	        	}
				pstmt.setString(2, key);
				pstmt.setInt(3, groupId);
				pstmt.setString(4, groupType);
				count = pstmt.executeUpdate();
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);
	        	ConnectionManager.closeDBConnection(conn);
	        }
			return count;
			 
		}
	    
	    public static int insertDesignDetails(int buyingCompanyId,int userId,int status, int designId,String details)
		{
			Connection  conn = null;
			PreparedStatement pstmt = null;
			int count=0;
			int designListId = 0;
	        try
	        {
	        	designListId = CommonDBQuery.getSequenceId("DESIGN_DETAILS_SEQ");
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("insertDesignDetails");
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setInt(1,designListId);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, buyingCompanyId);
				pstmt.setInt(4, status);
				pstmt.setInt(5, designId);
				pstmt.setString(6, details);
				count = pstmt.executeUpdate();
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);
	        	ConnectionManager.closeDBConnection(conn);
	        }
			return count;
			 
		}
	    
	    public static int updateDesignStatus(int LogoCustomizationId,int status,String details)
		{
			Connection  conn = null;
			PreparedStatement pstmt = null;
			int count=0;
			int designListId = 0;
	        try
	        {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("updateDesignStatus");
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setInt(1,LogoCustomizationId);
				pstmt.setString(2, CommonUtility.validateParseIntegerToString(status));
				count = pstmt.executeUpdate();
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);
	        	ConnectionManager.closeDBConnection(conn);
	        }
			return count;
			 
		}
	    
	    public static ArrayList<LogoCustomizationModule> getDesignDetails(int buyingCompanyId,int userId)
		{
			Connection  conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			 Gson gson = new Gson();
			ArrayList<LogoCustomizationModule> designList = new ArrayList<LogoCustomizationModule>();
	        try
	        {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("getDesigneDetails");
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setInt(1,userId);
				pstmt.setInt(2, buyingCompanyId);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					if(!CommonUtility.validateString(rs.getString("DETAILS")).equals("")) {
						LogoCustomizationModule design =  gson.fromJson(rs.getString("DETAILS"), LogoCustomizationModule.class);
						design.setDesignId(rs.getInt("DESIGN_ID"));
						design.setLogoCustomizationId(rs.getInt("CUSTOMIZATION_LIST_ID"));
						design.setStatus(CommonUtility.validateNumber(rs.getString("DESIGN_STATUS")));
						designList.add(design);
					} 
				}
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);
	        	ConnectionManager.closeDBConnection(conn);
	        }
			return designList;
			 
		}
	    
	    public static int updateDesignDetails(Set<Integer> designIds,int jobId)
		{
	    	Connection  conn = null;
			PreparedStatement pstmt = null;
			int count=0;
	        try
	        {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("updateDesignDetails");
	        	for(int designId : designIds) {
		        	pstmt = conn.prepareStatement(sql);
		        	pstmt.setInt(1,jobId);
					pstmt.setInt(2, designId);
					count = count + pstmt.executeUpdate();
				}
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);
	        	ConnectionManager.closeDBConnection(conn);
	        }
			return count;
			 
		}
	    
	    public static boolean checkDesignExits(int designId)
		{
	    	Connection  conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			boolean flag = false;
	        try
	        {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("checkDesignExits");
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setInt(1,designId);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					flag = true;
				}
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);
	        	ConnectionManager.closeDBConnection(conn);
	        }
			return flag;
			 
		}
	    
	    public static boolean selectFromCartCsp(int userId,String designId)
		{
	    	Connection  conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			boolean flag = false;
	        try
	        {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("selectFromCartCSP");
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setInt(1, userId);
	        	pstmt.setString(2,designId);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					flag = true;
				}
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	        finally
	        {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);
	        	ConnectionManager.closeDBConnection(conn);
	        }
			return flag;
			 
		}
	    
	    public static LinkedHashMap<String, Object> getPromotedProductGroupNameDAO(int userId, LinkedHashMap<String, Object> contentObject, int buyingCompanyId){
			if(contentObject==null)
				contentObject = new LinkedHashMap<String, Object>();
			System.out.println("group name called");
			ResultSet rs = null;
			Connection  conn = null;
			PreparedStatement pstmt = null;
			ArrayList<ProductsModel> prmotedProductListData = new ArrayList<ProductsModel>();
			String sortoprtion = null;
			int siteId = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID"));
			try{	    	
				if(CommonDBQuery.getGlobalSiteId()>0){
					siteId = CommonDBQuery.getGlobalSiteId();
				}	    	  
				if(CommonDBQuery.getSystemParamtersList().get("SAVED_GROUP_SORT") !=null) {
				  sortoprtion = CommonDBQuery.getSystemParamtersList().get("SAVED_GROUP_SORT");
				}else {
				  sortoprtion = "UPDATED_DATETIME DESC";
				}
				conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("getPromotedProductGroupListNameQuery");
				System.out.println(sql);
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, siteId);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, buyingCompanyId);
				pstmt.setInt(4, siteId);
				pstmt.setString(5,sortoprtion);
				rs = pstmt.executeQuery();        	
				while(rs.next()){
					ProductsModel productVal = new ProductsModel();
					productVal.setProductListId(rs.getInt("SAVED_LIST_ID"));
					productVal.setProductListName(rs.getString("SAVED_LIST_NAME"));
					Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(rs.getString("UPDATED_DATETIME"));
					String updatedDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
					productVal.setDate(updatedDate);
					productVal.setAssignedShipTo(rs.getInt("ASSIGNED_SHIP_TO"));
					productVal.setStatus(rs.getString("STATUS"));
					if(rs.findColumn("ITEM_COUNTS")>0){
						productVal.setGroupItemCount(rs.getString("ITEM_COUNTS"));	
					}
					String senderUserID = rs.getString("SENT_BY_APPR_USER_ID");
					if(rs.getString("TYPE").trim().equalsIgnoreCase("GP")) {
						productVal.setGroupType(rs.getString("TYPE"));
						prmotedProductListData.add(productVal);
					}
				}
				contentObject.put("promotedProductGroupListData", prmotedProductListData);        	
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				ConnectionManager.closeDBResultSet(rs);
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);
			}
			System.out.println("group end called");
			return contentObject;
		}
	    
	    public static LinkedHashMap<String, Object> getLastUpdateTimeDifferenceFromCart(int userId, int siteId){
	    	long startTimer = CommonUtility.startTimeDispaly();
	    	LinkedHashMap<String, Object> cartDetails = new LinkedHashMap<String, Object>();
			
			Connection  conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try{
				String sql = PropertyAction.SqlContainer.get("getLastUpdateTimeDifferenceFromCart");
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userId);
				pstmt.setInt(2, siteId);
				rs = pstmt.executeQuery();
				ArrayList<ProductsModel> cartList = new  ArrayList<ProductsModel>();
				Integer daysMaxValue = 0;
				Integer hoursMaxValue = 0;
				
				while(rs.next()){
					ProductsModel cartModel = new ProductsModel();
					cartModel.setCartId(rs.getInt("CART_ID"));
					cartModel.setItemId(rs.getInt("ITEM_ID"));
					cartModel.setDays(rs.getInt("DAYS"));
					cartModel.setHours(rs.getInt("HOURS"));
					cartModel.setMinutes(rs.getInt("MINUTES"));
					cartModel.setSeconds(rs.getInt("SECONDS"));
					if(daysMaxValue < rs.getInt("DAYS")) {
						daysMaxValue = rs.getInt("DAYS");
					}
					if(hoursMaxValue < rs.getInt("HOURS")) {
						hoursMaxValue = rs.getInt("HOURS");
					}
					cartList.add(cartModel);
				}
				cartDetails.put("cartList", cartList);
				cartDetails.put("daysMaxValue", daysMaxValue);
				cartDetails.put("hoursMaxValue", hoursMaxValue);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);	
			}
			CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
			return cartDetails;
		}
	    
	    public static void updateCartItemPrice(LinkedHashMap<String, Object> contentObject)
		{
	    	long startTimer = CommonUtility.startTimeDispaly();
			PreparedStatement pstmt = null;
			Connection  conn = null;
			try
			{
				HttpSession session = (HttpSession) contentObject.get("session");
				ArrayList<ProductsModel> cartPriceList = (ArrayList<ProductsModel>) contentObject.get("productListData"); 
				ArrayList<Integer> partIdentifierQuantity = (ArrayList<Integer>) contentObject.get("partIdentifierQuantity");
				String idList =  CommonUtility.validateString((String) contentObject.get("idList"));
				String homeTerritory = (String) session.getAttribute("shipBranchId");
				
				if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SavePriceInShoppingCart")).equalsIgnoreCase("Y") && CommonUtility.validateString(idList).length()>0 && cartPriceList!=null && cartPriceList.size()>0 && CommonUtility.validateString((String) session.getAttribute("userToken")).length()>0 && !CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ERP")).equalsIgnoreCase("defaults")){
					ProductManagement priceInquiry = new ProductManagementImpl();
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					String entityId = (String) session.getAttribute("entityId");
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					priceInquiryInput.setHomeTerritory(homeTerritory);
					priceInquiryInput.setPartIdentifier(cartPriceList);
					priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
					priceInquiryInput.setRequiredAvailabilty("Y");
					if(CommonUtility.validateString(idList).length()>0 &&  CommonDBQuery.getSystemParamtersList().get("GET_ALL_BRANCH_AVAILABILITY")!=null && CommonDBQuery.getSystemParamtersList().get("GET_ALL_BRANCH_AVAILABILITY").trim().equalsIgnoreCase("Y")){
						priceInquiryInput.setAllBranchavailabilityRequired("Y");
					}
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					if(CommonUtility.customServiceUtility()!=null) {
						 CommonUtility.customServiceUtility().setAllBranchAvailValue(priceInquiryInput);
					}
					cartPriceList = priceInquiry.priceInquiry(priceInquiryInput , cartPriceList);
					//total = cartPriceList.get(0).getCartTotal();
					
					if(cartPriceList!=null && !cartPriceList.isEmpty()){
						String sql = PropertyAction.SqlContainer.get("updateCartItemPrieAndAvailabilityFromERP");
						conn = ConnectionManager.getDBConnection();
						for(ProductsModel cartPrice:cartPriceList){
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, CommonUtility.validateParseDoubleToString(cartPrice.getPrice()));//cartPrice.getUnitPrice()
							pstmt.setString(2, CommonUtility.validateParseIntegerToString(cartPrice.getAvailQty()));
							pstmt.setInt(3, cartPrice.getCartId());
							int updateCount = pstmt.executeUpdate();
							System.out.println("Price Update Status for:"+cartPrice.getPartNumber()+":is:"+updateCount+" :Price: "+cartPrice.getPrice()+" :Availability: "+cartPrice.getAvailQty());
							ConnectionManager.closeDBPreparedStatement(pstmt);
							
						}
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
			finally
			{	
				ConnectionManager.closeDBPreparedStatement(pstmt);
				ConnectionManager.closeDBConnection(conn);	
			}
			CommonUtility.endTimeAndDiffrenceDisplay(startTimer);
		}
	    
	    public static int updateABCartNotificaion(int userId) {
			Connection  conn = null;
			PreparedStatement pstmt = null;
			int count=0;
	        try {
	        	conn = ConnectionManager.getDBConnection();
	        	String sql = PropertyAction.SqlContainer.get("updateABCartNotificaion");
	        	pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,userId);
				count = pstmt.executeUpdate();
	        	
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	        finally {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);
	        	ConnectionManager.closeDBConnection(conn);
	        }
			return count;		 
		}
	    
	    public static ArrayList<ProductsModel> selectFromCartWithUserIdOrSessonId(HttpSession session, int userId, String sessionId) {
			ArrayList<ProductsModel> cartDetails = new ArrayList<ProductsModel>();
			Connection  conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			int siteId = 0;
			if (CommonDBQuery.getGlobalSiteId() > 0) {
				siteId = CommonDBQuery.getGlobalSiteId();
			}
			String tempSubset = (String) session.getAttribute("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
		    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			
			try{
				String sql = PropertyAction.SqlContainer.get("getCartWithUserOrSessionID");
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
    			pstmt.setInt(1, siteId);
    			pstmt.setInt(2, userId);
    			pstmt.setString(3, sessionId);
    			pstmt.setInt(4, subsetId);
    			pstmt.setInt(5, siteId);
    			pstmt.setInt(6, userId);
    			pstmt.setString(7, sessionId);
    			pstmt.setInt(8, generalSubset);
    			pstmt.setInt(9, siteId);
    			pstmt.setInt(10, userId);
    			pstmt.setString(11, sessionId);
    			pstmt.setInt(12, subsetId);
    			rs = pstmt.executeQuery();
				
				while(rs.next()){
					ProductsModel cartModel = new ProductsModel();
					cartModel.setCartId(rs.getInt("CART_ID"));
					cartModel.setItemId(rs.getInt("ITEM_ID"));
					cartModel.setUserId(rs.getInt("USER_ID"));
					cartDetails.add(cartModel);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);	
			}
			return cartDetails;
		}
	    
	    public static LinkedHashMap<String, Object> myItemListByBuyingCompanyDao(int subsetId,int generalSubset,int buyingCompany,String entityId){
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			ResultSet rs = null;
		    Connection  conn = null;
		    PreparedStatement pstmt = null;
		 	ArrayList<ProductsModel> myItemList = new ArrayList<ProductsModel>();
		 	ArrayList<Integer> itemList = new ArrayList<Integer>();
		 	Map<Integer, ProductsModel> itemListMap = new LinkedHashMap<>();
		 	//ArrayList<Integer> itemPriceList = new ArrayList<Integer>();
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			int buyingCompanyId = CommonUtility.validateNumber((String)session.getAttribute("buyingCompanyId"));
			String downloadMyItem = CommonUtility.validateString((String)request.getAttribute("downloadMyItems"));
			try {
				conn = ConnectionManager.getDBConnection();
				//String sql = "SELECT MI.YTD,MI.PART_NUMBER,MI.UNIT_STOCK,MI.ID_LIST,MI.LAST_PURCHASED_UNIT,IM.ITEM_ID,MI.LAST_PURCHASED_DATE,MI.LAST_PURCHASED_QUANTITY,CLC.CATEGORY_NAME FROM CNES_ENTITY_SPECIFIC_ITEMS MI,ITEM_MASTER IM,CUSTOMER_LEVEL_CATEGORIES CLC WHERE IM.PART_NUMBER = MI.PART_NUMBER AND IM.PART_NUMBER = CLC.PART_NUMBER(+) AND MI.CUSTOMER_NUMBER=CLC.ENTITY_ID(+) AND MI.CUSTOMER_NUMBER=? AND IM.ACTIVE='Y' AND IM.DISPLAY_ONLINE='Y'";
				String sql = PropertyAction.SqlContainer.get("getMyItemList");
				
				System.out.println(sql);
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, entityId);
				pstmt.setInt(2, subsetId);
				pstmt.setInt(3, generalSubset);
				/*pstmt.setInt(4, ProductsDAO.getSubsetIdFromName((String)session.getAttribute("Extended_Catalog_Search")));*/
				
				rs = pstmt.executeQuery();
				while(rs.next()) {
					ProductsModel itemDetails = new ProductsModel();
					itemDetails.setYearToDate(rs.getString("YTD"));
					itemDetails.setPartNumber(rs.getString("PART_NUMBER"));
					itemDetails.setUnitsPerStockingString(rs.getString("UNIT_STOCK"));
					itemDetails.setQty(1);
					itemDetails.setUom(rs.getString("LAST_PURCHASED_UNIT"));
					itemDetails.setItemId(rs.getInt("ITEM_ID"));
					itemDetails.setItemPriceId(rs.getInt("ITEM_PRICE_ID"));
					itemDetails.setIdList(rs.getString("ID_LIST"));
					itemDetails.setLastPurchasedDate(rs.getString("LAST_PURCHASED_DATE") != null ? rs.getString("LAST_PURCHASED_DATE") :"");
					itemDetails.setLastPurchasedQty(rs.getString("LAST_PURCHASED_QUANTITY"));
					itemDetails.setLastPurchasedUnit(rs.getString("LAST_PURCHASED_UNIT"));
					itemDetails.setLineItemComment(rs.getString("CATEGORY_NAME"));
					itemList.add(itemDetails.getItemId());
					//itemPriceList.add(itemDetails.getItemPriceId());
					myItemList.add(itemDetails);
					
					//added itemList in Map
					itemListMap.put(itemDetails.getItemId(), itemDetails);
				}
				
				ArrayList<ProductsModel> itemDetails = new ArrayList<ProductsModel>();
				LinkedHashMap<Integer, ArrayList<ProductsModel>> customerPartNumber = null;
				int chunkSize=500;
				int start=0;
				int end=chunkSize;
				int count=1;
				int chunkedSize = (int) Math.ceil((double) itemList.size() / chunkSize); 
				for (int index = 0; index < chunkedSize; index++) {
					if(chunkedSize==count) {
						end = itemList.size();
					}
					itemDetails.addAll(ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId,generalSubset,StringUtils.join(itemList.subList(start, end)," OR "),0,"N","itemid"));
					customerPartNumber = ProductHunterSolr.getcustomerPartnumber(StringUtils.join(itemList.subList(start, end)," OR "), buyingCompanyId, buyingCompanyId);
					start=start+chunkSize;
					end=end+chunkSize; 
					count++;
				}
				
				if(customerPartNumber!=null && customerPartNumber.size()>0)
				{
					for(ProductsModel item : itemDetails) {
						ArrayList<ProductsModel> eachCPN = customerPartNumber.get(item.getItemId());
						if(eachCPN!=null && eachCPN.size()>1){
							Collections.sort(eachCPN, ProductsModel.partNumberAscendingComparator);
							item.setCustomerPartNumber(eachCPN.get(0).getPartNumber());
						}else if(eachCPN!=null && eachCPN.size()>0){
							item.setCustomerPartNumber(eachCPN.get(0).getPartNumber());
						}
						item.setCustomerPartNumberList(eachCPN);
					}
				}
				
				LinkedHashMap<Integer, ProductsModel> allItemDetails = new LinkedHashMap<Integer, ProductsModel>();
				
				ArrayList<String> categoryList = new ArrayList<>();
				
				if(downloadMyItem.equalsIgnoreCase("Y")) {
					ProductManagementModel priceInquiryInput = new ProductManagementModel();
					ProductManagement priceInquiry = new ProductManagementImpl();
					priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
					String homeTerritory = (String) session.getAttribute("shipBranchId");
					priceInquiryInput.setHomeTerritory(homeTerritory);
					priceInquiryInput.setPartIdentifier(myItemList);
					priceInquiryInput.setRequiredAvailabilty("Y");
					priceInquiryInput.setUserName((String)session.getAttribute(Global.USERNAME_KEY));
					priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
					priceInquiryInput.setSession(session);
					myItemList = priceInquiry.priceInquiry(priceInquiryInput , myItemList);
				}

				for(ProductsModel eachItems:itemDetails){
					ProductsModel itemLists = itemListMap.get(eachItems.getItemId());
					if(itemLists != null) {
						eachItems.setPrice(itemLists.getPrice());
						eachItems.setYearToDate(itemLists.getYearToDate());
						eachItems.setUnitsPerStockingString(itemLists.getUnitsPerStockingString());
						//eachItems.setUom(eachItems.getUom());
						eachItems.setLastPurchasedDate(itemLists.getLastPurchasedDate());
						eachItems.setLastPurchasedQty(itemLists.getLastPurchasedQty());
						eachItems.setLastPurchasedUnit(itemLists.getLastPurchasedUnit());
						eachItems.setHomeBranchavailablity(itemLists.getHomeBranchavailablity());
						eachItems.setQtyUOM(itemLists.getQtyUOM());
						eachItems.setHomeBranchavailablity(itemLists.getHomeBranchavailablity());
						eachItems.setDistributionCenterAvailablity(itemLists.getDistributionCenterAvailablity());
						eachItems.setBranchAvail(itemLists.getBranchAvail());
						eachItems.setIdList(itemLists.getIdList());
						eachItems.setLineItemComment(itemLists.getLineItemComment());
						eachItems.setItemId(itemLists.getItemId());
						eachItems.setItemPriceId(itemLists.getItemPriceId());
						
						categoryList.add(eachItems.getCategoryName());
					}
					
					categoryList.add(eachItems.getCategoryName());
					allItemDetails.put(eachItems.getItemId(),eachItems);
				}
				Collections.sort(categoryList, String.CASE_INSENSITIVE_ORDER);
				LinkedHashMap<String,ArrayList<ProductsModel>> categoryLevelItems = new LinkedHashMap<String,ArrayList<ProductsModel>>();	
	        	for(String category:categoryList) {
	        		ArrayList<ProductsModel> categoryModel=new ArrayList<ProductsModel>();
	        		for(ProductsModel eachItems:itemDetails){ 
	        			if(category.equalsIgnoreCase(eachItems.getCategoryName()))
	        			categoryModel.add(eachItems);
	        		}
	        		categoryLevelItems.put(category, categoryModel);
	        	}
	        	
				contentObject.put("allItemDetails",allItemDetails);
	        	contentObject.put("myItemList", itemDetails);
	        	contentObject.put("categoryLevelItems",categoryLevelItems);
	        	if(CommonUtility.validateString((String)session.getAttribute("browseType")).equalsIgnoreCase("Mobile")) {
	        		Gson gsonObject = new Gson();
	        		String myitemDetailsGson = null;
	        		String myItemCategoryListGson = null;
	    			if(itemDetails != null && itemDetails.size() > 0) {
	    				myitemDetailsGson = gsonObject.toJson(itemDetails);
	    			}
	    			session.setAttribute("myItemsDetailGson", myitemDetailsGson);
	        		if(categoryLevelItems != null && categoryLevelItems.size() > 0) {
	        			myItemCategoryListGson = gsonObject.toJson(categoryLevelItems, LinkedHashMap.class);
	            	}
	    			session.setAttribute("myItemsCategoryJson", myItemCategoryListGson);
	        		System.out.println("My Item Details Gson for mobile: " + session.getAttribute("myItemsDetailGson"));
	        		System.out.println("My Item Category List Gson for mobile: " + session.getAttribute("myItemsCategoryJson"));
	        	}
	        }catch(Exception e){
	        	logger.error(e.getMessage());
			}finally{
				ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);
			}
			return contentObject;
			
		}
	    public static int updateMyItemsValue(int listId,String categoryName){
			Connection  conn = null;
		    PreparedStatement pstmt = null;
		 	int count = 0;
			try{
				conn = ConnectionManager.getDBConnection();
				String sql = "UPDATE SAVED_LIST_ITEMS SET LINE_ITEM_COMMENT=? WHERE LIST_ITEM_ID=?";
				System.out.println(sql);
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setString(1, categoryName);
	        	pstmt.setInt(2, listId);
	        	count = pstmt.executeUpdate();
	        }catch(Exception e){
				e.printStackTrace();
			}finally{
				ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);
			}
			return count;
		}
	    
	    public static LinkedHashMap<Integer, ProductsModel> myItemListForMatrixPricing(int subsetId,int generalSubset,int buyingCompany,String entityId) {
			LinkedHashMap<Integer, ProductsModel> myItemListData = new LinkedHashMap<Integer, ProductsModel>();
			ResultSet rs = null;
		    Connection  conn = null;
		    PreparedStatement pstmt = null;
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			try {
				conn = ConnectionManager.getDBConnection();
				String sql = PropertyAction.SqlContainer.get("getmyItemListForMatrixPricing");
				System.out.println(sql);
	        	pstmt = conn.prepareStatement(sql);
	        	pstmt.setString(1, entityId);
				pstmt.setString(2, "Y");
				pstmt.setString(3, "Y");
	        	rs = pstmt.executeQuery();
	        	while(rs.next()) {
	        		ProductsModel itemDetails = new ProductsModel();
	        		itemDetails.setYearToDate(rs.getString("YTD"));
	        		itemDetails.setPartNumber(rs.getString("PART_NUMBER"));
	        		itemDetails.setUnitsPerStockingString(rs.getString("UNIT_STOCK"));
	        		itemDetails.setQty(1);
	        		itemDetails.setUom(rs.getString("LAST_PURCHASED_UNIT"));
	        		itemDetails.setItemId(rs.getInt("ITEM_ID"));
	        		itemDetails.setIdList(rs.getString("ID_LIST"));
	        		itemDetails.setLastPurchasedDate(rs.getString("LAST_PURCHASED_DATE") != null ? rs.getString("LAST_PURCHASED_DATE") :"");
	        		itemDetails.setLastPurchasedQty(rs.getString("LAST_PURCHASED_QUANTITY"));
	        		itemDetails.setLastPurchasedUnit(rs.getString("LAST_PURCHASED_UNIT"));
	        		myItemListData.put(itemDetails.getItemId(), itemDetails);
	        	}
	        	
	    		if(session.getAttribute("myItemListData")!= null) {
	    			session.removeAttribute("myItemListData");
	            	session.setAttribute("myItemListData", myItemListData);
	    		}
	    		else
	    		{
	    			session.setAttribute("myItemListData", myItemListData);	
	    		}
	        } catch(Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.closeDBResultSet(rs);
		    	ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);
			}
			return myItemListData;
		}
		/*Matrix Pricing End*/

	    public static int insertCustomerCategory(String categoryName,String entityId,String partNumber){
			String sql = PropertyAction.SqlContainer.get("insertCustomerCategory");
			Connection  conn = null;
			PreparedStatement pstmt = null;
			int count = 0;
			try{
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, partNumber);
				pstmt.setString(2, entityId);
				pstmt.setString(3, categoryName);
				count = pstmt.executeUpdate();
			}catch(SQLException ex){
				logger.error(ex.getMessage());
			}catch(Exception e){
				logger.error(e.getMessage());
			}finally{
				ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);		
			}
			return count;
		}
	    public static int updateCustomerCategory(String categoryName,String entityId,String partNumber){
			String sql = PropertyAction.SqlContainer.get("updateCustomerCategory");
			Connection  conn = null;
			PreparedStatement pstmt = null;
			int count = 0;
			try{
				conn = ConnectionManager.getDBConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, categoryName);
				pstmt.setString(2, partNumber);
				pstmt.setString(3, entityId);
				count = pstmt.executeUpdate();
			}catch(SQLException ex){
				logger.error(ex.getMessage());
			}catch(Exception e){
				logger.error(e.getMessage());
			}finally{
				ConnectionManager.closeDBPreparedStatement(pstmt);	
		    	ConnectionManager.closeDBConnection(conn);		
			}
			return count;
	    }

	    
		
		public static int createApproveCart(Connection conn,int savedListId,int userId,String type,String groupName, int shipToAssigned, int userID,int reqTokenId,String approvalStatus,String approverSequence,String alwaysApprove) {
			int count=0;
			PreparedStatement pstmt = null;
			try
			{
				int siteId = 0;
				if(CommonDBQuery.getGlobalSiteId()>0){
		    		siteId = CommonDBQuery.getGlobalSiteId();
				}
				/*String sql = PropertyAction.SqlContainer.get("insertApproveCartName");*/
				String sql = "INSERT INTO SAVED_ITEM_LIST(SAVED_LIST_ID,SAVED_LIST_NAME,USER_ID,UPDATED_DATETIME,TYPE,ASSIGNED_SHIP_TO,SENT_BY_APPR_USER_ID,SITE_ID,REQ_TOKEN_ID,APPROVAL_STATUS,APPROVER_SEQ,ALWAYS_APPROVE) VALUES(?,?,?,SYSDATE,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, savedListId);
	    		pstmt.setString(2, groupName);
	    		pstmt.setInt(3, userId);
	    		pstmt.setString(4, type);
	    		pstmt.setInt(5, shipToAssigned);
	    		pstmt.setInt(6, userID);
	    		pstmt.setInt(7, siteId);
	    		pstmt.setInt(8, reqTokenId);
	    		pstmt.setString(9, approvalStatus);
	    		pstmt.setString(10, approverSequence);
	    		pstmt.setString(11, alwaysApprove);
	    		count = pstmt.executeUpdate();

			}
	        catch(SQLException ex)
	        {
	        	logger.error(ex.getMessage());
	        }
	        catch(Exception e)
	        {
	        	logger.error(e.getMessage());
	        }
	        finally
	        {
	        	ConnectionManager.closeDBPreparedStatement(pstmt);	
		    }
			return count;
		}
		
		public static int insertItemToEachAPAGroup(Connection conn,int savedListId,int itemId,int itemQty,String itemLevelRequiredByDate,String catalogId,String lineItemComment,int reqTokenId,String uom)
		{
			int count=0;
			PreparedStatement pstmt = null;
			try
			{
				/*String sql = PropertyAction.SqlContainer.get("insertItemToGroupQuery");*/
				String sql ="INSERT INTO SAVED_LIST_ITEMS (LIST_ITEM_ID,SAVED_LIST_ID,ITEM_ID,QTY,ITEMLEVEL_REQUIREDBYDATE,CATALOG_ID,LINE_ITEM_COMMENT,REQ_TOKEN_ID,UPDATED_DATETIME,UOM) VALUES (SAVED_LIST_ITEMS_SEQ.NEXTVAL,?,?,?,?,?,?,?,SYSDATE,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, savedListId);
	    		pstmt.setInt(2, itemId);
	    		pstmt.setInt(3, itemQty);
	    		pstmt.setString(4, itemLevelRequiredByDate);
	    		pstmt.setString(5,catalogId);
	    		pstmt.setString(6,lineItemComment);
	    		pstmt.setInt(7,reqTokenId);
	    		pstmt.setString(8,uom);
	    		count = pstmt.executeUpdate();
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
			return count;
		}



}
