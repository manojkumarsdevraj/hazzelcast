package com.unilog.cmsmanagement.dao;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.google.gson.Gson;
import com.unilog.cmsmanagement.model.BannerInfo;
import com.unilog.cmsmanagement.model.BannerInfoData;
import com.unilog.cmsmanagement.util.BannerTemplate;
import com.unilog.cmsmanagement.util.CMSConstants;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.model.PageHistoryModel;
import com.unilog.model.SystemParameterModel;
import com.unilog.model.ThemeModel;
import com.unilog.model.WebsiteModel;
import com.unilog.model.locale.LocaleModel;
import com.unilog.model.staticpage.StaticPageData;
import com.unilog.utility.CommonUtility;

public class CmsDao {
	public int saveBannerInfo(BannerInfoData bannerInfoData,int userId) {
		PreparedStatement ps1 = null;
		Connection con = null;
		int n = -1;
		int index = 1;
		try{
			
			con = ConnectionManager.getDBConnection();
			int bannerInfoId= CommonDBQuery.getSequenceId("BANNER_INFO_ID_SEQ");	
			List<BannerInfo> bannerInfoList = bannerInfoData.getBannerInfo();

			String str = "INSERT INTO BANNER_INFO(BANNER_INFO_ID, BANNER_LIST_ID, BANNER_ID, SLIDER_EFFECT, CAPTION_TEXT, CAPTION_TOP_POS, CAPTION_LEFT_POS, UPDATED_DATETIME, USER_EDITED,BANNER_URL,DISPLAY_SEQ) VALUES(?, ?, ?, ?, ?, ?, ?, SYSDATE, ?,?,?)";
			ps1=con.prepareStatement(str);
			for(BannerInfo bannerInfo:bannerInfoList){
				bannerInfoId= CommonDBQuery.getSequenceId("BANNER_INFO_ID_SEQ");
				ps1.setInt(1,bannerInfoId);
				ps1.setInt(2,bannerInfoData.getId());
				ps1.setLong(3, bannerInfo.getId());
				ps1.setString(4,bannerInfo.getSliderEffect());
				ps1.setString(5,bannerInfo.getCaptionText());
				ps1.setInt(6, bannerInfo.getCaptionTopPos());
				ps1.setInt(7, bannerInfo.getCaptionLeftPos());
				ps1.setLong(8, userId);
				ps1.setString(9, bannerInfo.getBannerUrl());
				ps1.setInt(10, index);
				ps1.addBatch();
				index++;
			}
			
			
			int[] i = ps1.executeBatch();
			n = 1;
		}catch(Exception e){
			n = -1;
			e.printStackTrace();
		}finally{
			try {				
				if (ps1 != null) {				
					ps1.close();
				}if (con != null) 
				{
					con.close();
				}
			} catch (SQLException e) {
				n = -1;
				e.printStackTrace();
			}}
				
		return n;
	}
	
	public int saveBannerListInfo(int userId, BannerInfoData bannerInfoData,int siteId,LinkedHashMap<String,String> bannerProperties) throws ParseException {
		PreparedStatement ps1 = null;
		Connection con = null;
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = null;
		if(!CommonUtility.validateString(bannerInfoData.getStartDate()).equalsIgnoreCase("")){
		formatter.parse(bannerInfoData.getStartDate());
		}
		Date endDate = null;
		if(!CommonUtility.validateString(bannerInfoData.getExpiryDate()).equalsIgnoreCase("")){
		formatter.parse(bannerInfoData.getExpiryDate());
		}
		int n = -1;
		try{
			String properties = "";
			if(bannerProperties!=null && bannerProperties.size()>0){
				Gson gson = new Gson();
				properties = gson.toJson(bannerProperties);
			}
			con = ConnectionManager.getDBConnection();
			int bannerListId= CommonDBQuery.getSequenceId("BANNER_LIST_ID_SEQ");		
			
			String str = "INSERT INTO BANNER_LIST(BANNER_LIST_ID, BANNER_NAME, START_DATE, EXPIRY_DATE, UPDATED_DATETIME, USER_EDITED,STATUS,SITE_ID,PROPERTIES) VALUES(?, ?, ?, ?, SYSDATE, ?,'A',?,?)";
			ps1=con.prepareStatement(str);
			ps1.setInt(1,bannerListId);
			ps1.setString(2, bannerInfoData.getBannerListName());
			ps1.setDate(3, BannerTemplate.getInstance().convertUtilToSqlDate(startDate));
			ps1.setDate(4, BannerTemplate.getInstance().convertUtilToSqlDate(endDate));
			ps1.setLong(5, userId);
			ps1.setInt(6, siteId);
			ps1.setString(7, properties);
			n = ps1.executeUpdate();
			if(n > 0){
				n = bannerListId;
			}
		}catch(Exception e){
			e.printStackTrace();
			if(e.getMessage() != null && e.getMessage().contains("unique constraint") ){
				
				return -1;  
			}else{
				return -9;
			}
		}finally{
			try {				
				if (ps1 != null) {				
					ps1.close();
				}if (con != null) 
				{
					con.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}}
				
		return n;
	}
	
	public int updateBannerListInfo(ArrayList<BannerInfo> bannerInfoList,int bannerListId,String bannerName, ArrayList<BannerInfo> bannerDelList,ArrayList<BannerInfo> updateBannerListInfoList,LinkedHashMap<String,String> bannerProperties,Date sDate,Date eDate,int userId) {
		PreparedStatement ps1 = null;
		Connection con = null;
		int n = -1;
		try{
			String properties = "";
			if(bannerProperties!=null && bannerProperties.size()>0){
				Gson gson = new Gson();
				properties = gson.toJson(bannerProperties);
			}
			con = ConnectionManager.getDBConnection();
			java.sql.Date sqlSdate = null;
			java.sql.Date sqlEdate = null;
			
			if(sDate!=null){
			sqlSdate = BannerTemplate.getInstance().convertUtilToSqlDate(sDate);
			}
			if(eDate!=null){
			sqlEdate = BannerTemplate.getInstance().convertUtilToSqlDate(eDate);
			}
			
			String str = "UPDATE BANNER_LIST SET BANNER_NAME = ?,UPDATED_DATETIME = SYSDATE, USER_EDITED = ?, PROPERTIES = ?, START_DATE = ?, EXPIRY_DATE = ? WHERE BANNER_LIST_ID = ?";
			ps1=con.prepareStatement(str);
			ps1.setString(1, bannerName);
			ps1.setLong(2,userId);
			ps1.setString(3, properties);
			ps1.setDate(4, sqlSdate);
			ps1.setDate(5, sqlEdate);
			ps1.setInt(6,bannerListId);
			
			n = ps1.executeUpdate();
			
			if(n>0 && bannerInfoList!=null && bannerInfoList.size()>0){
				if (ps1 != null) {				
					ps1.close();
				}
				str = "UPDATE BANNER_INFO SET SLIDER_EFFECT = ?,CAPTION_TEXT = ?, CAPTION_TOP_POS = ?, CAPTION_LEFT_POS = ?, UPDATED_DATETIME = SYSDATE, USER_EDITED = ?, BANNER_URL=?, DISPLAY_SEQ = ? WHERE BANNER_INFO_ID = ?";
				ps1=con.prepareStatement(str);
				for(BannerInfo bannerInfo:bannerInfoList){
					
					ps1.setString(1,bannerInfo.getSliderEffect());
					ps1.setString(2,bannerInfo.getCaptionText());
					ps1.setInt(3, bannerInfo.getCaptionTopPos());
					ps1.setInt(4, bannerInfo.getCaptionLeftPos());
					ps1.setLong(5, userId);
					ps1.setString(6, bannerInfo.getBannerUrl());
				//	ps1.setInt(7,bannerListId);
					ps1.setInt(7, bannerInfo.getDisplaySequence());
					ps1.setLong(8, bannerInfo.getId());
					
					ps1.addBatch();
				}
				int[] i = ps1.executeBatch();
				System.out.println("Number of Rows Executed: "+i.length);
				
				
				
			}
			
			if(bannerDelList!=null && bannerDelList.size() > 0){
				if (ps1 != null) {				
					ps1.close();
				}
				str = "DELETE FROM BANNER_INFO WHERE BANNER_LIST_ID = ? AND BANNER_INFO_ID = ?";
				ps1=con.prepareStatement(str);
				for(BannerInfo bannerInfo:bannerDelList){
				ps1.setInt(1,bannerListId);
				ps1.setLong(2, bannerInfo.getId());
				ps1.addBatch();
				}
				int[] i = ps1.executeBatch();
			}
			
			if(n>0 && updateBannerListInfoList!=null && updateBannerListInfoList.size()>0){
				if (ps1 != null) {				
					ps1.close();
				}
				
				str = "INSERT INTO BANNER_INFO(BANNER_INFO_ID, BANNER_LIST_ID, BANNER_ID, SLIDER_EFFECT, CAPTION_TEXT, CAPTION_TOP_POS, CAPTION_LEFT_POS, UPDATED_DATETIME, USER_EDITED,BANNER_URL,DISPLAY_SEQ) VALUES(?, ?, ?, ?, ?, ?, ?, SYSDATE, ?,?,?)";
				ps1=con.prepareStatement(str);
				int bannerInfoId= 0 ;	
				for(BannerInfo bannerInfo:updateBannerListInfoList){
					bannerInfoId= CommonDBQuery.getSequenceId("BANNER_INFO_ID_SEQ");	

					
					ps1.setInt(1,bannerInfoId);
					ps1.setInt(2,bannerListId);
					ps1.setLong(3, bannerInfo.getId());
					ps1.setString(4,bannerInfo.getSliderEffect());
					ps1.setString(5,bannerInfo.getCaptionText());
					ps1.setInt(6, bannerInfo.getCaptionTopPos());
					ps1.setInt(7, bannerInfo.getCaptionLeftPos());
					ps1.setLong(8, userId);
					ps1.setString(9,bannerInfo.getBannerUrl());
					ps1.setInt(10, bannerInfo.getDisplaySequence());
					ps1.addBatch();
				}
				int[] i = ps1.executeBatch();
				
			}
				
			
			

			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {				
				if (ps1 != null) {				
					ps1.close();
				}if (con != null) 
				{
					con.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
				if(e.getMessage() != null && e.getMessage().contains("unique constraint") ){
					
					return -1;  
				}else{
					return -9;
				}
			}
			}
				
		return n;
	}
	
	public static void generateXMLforStaticPage(String staticPagesPath, StaticPageData staticPages,String staticPageAddOrEdit) throws IOException {

		PreparedStatement ps2 =null;
		Connection con = null;
		ResultSet rs2 = null;
		
	String prevStaticPagePath = null;
		String fileName="";
		  String s=staticPages.getId()+"_";
		
		try{
			con = ConnectionManager.getDBConnection();
			fileName = staticPages.getId()+".xml";
		
		  File myFile = new File(staticPagesPath+fileName);			
		if (staticPageAddOrEdit.equalsIgnoreCase("EditStaticPage")) {
			if(staticPages.getStatus()!=null &&staticPages.getStatus().equalsIgnoreCase("N")){
				if(myFile != null){
					myFile.delete();
				}
			}
			

			// }

			File fileFolderPath = new File(staticPagesPath);
			if (fileFolderPath.exists()) {
				for (File filePath : fileFolderPath.listFiles()) {
					if (filePath.getName().equals(staticPages.getId() + ".xml")) {
						if (!filePath.getName().equals(fileName)) {
							if (filePath.renameTo(new File(staticPagesPath + fileName))) {
								System.out.println("File Name Changed..");									
							}
						}
					} else {
						if (filePath.getName().startsWith(s)) {
							if (!filePath.getName().equals(fileName)) {
								if (filePath.renameTo(new File(staticPagesPath + fileName))) {
									System.out.println("File Name Changed..");
								}
							}
						}
					}
				}
			}
		}
		
		if(staticPages.getStatus()!=null &&staticPages.getStatus().equalsIgnoreCase("A")){
			String removedNbsp = staticPages.getPageContent().replaceAll("\\<.*?>","");
			removedNbsp = removedNbsp.trim().replaceAll("&nbsp;", "");
			removedNbsp = removedNbsp.replaceAll("&Ecirc;", "");
			staticPages.setPlainText(removedNbsp);
			String pageContent = staticPages.getPageContent();//.replace(staticPages.getPlainText(), BannerTemplate.getInstance().encodeStringV2(staticPages.getPlainText(), true));
			PrintWriter pw1 = new PrintWriter(new FileWriter(myFile , false));
			pw1.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			pw1.println("<staticPage>"); 		
			pw1.println("<pageId>"+staticPages.getId()+"</pageId>");
			pw1.println("<pageName> <![CDATA[ "+staticPages.getPageName()+" ]]> </pageName>"); 		
			pw1.println("<pageTitle> <![CDATA[ "+staticPages.getPageTitle()+" ]]> </pageTitle>"); 		
			pw1.println("<metaKeywords> <![CDATA[ "+staticPages.getMetaKeywords()+" ]]> </metaKeywords>"); 		
			pw1.println("<metaDesc> <![CDATA[ "+staticPages.getMetaDescription()+" ]]> </metaDesc>"); 		
			pw1.println("<pageContent> <![CDATA[ "+pageContent+" ]]>  </pageContent>");
			
			pw1.println("<plainText> <![CDATA[ "+BannerTemplate.getInstance().encodeStringV2(removedNbsp, true)+" ]]>  </plainText>");
			pw1.println("</staticPage>");
			System.out.println(pw1.toString());
			if(pw1 != null){
				pw1.close();
			}
		}
	
		}catch (SQLException e) {
		
		e.printStackTrace();
	
		
	} finally {
		try {
			
			if (ps2 != null) {
				ps2.close();
			}				
			if (rs2 != null) {
				rs2.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
	
		}
		}


}
	
	public static String setasHomePageIcon(int pageId, boolean isDlink, int siteId){
		Connection conn = null;
		PreparedStatement pstmt =null;
		
		try{
			String sql1= "UPDATE STATIC_PAGES SET HOME_PAGE_ICON='N' where HOME_PAGE_ICON='Y' AND SITE_ID=?";
			String sql2= "UPDATE STATIC_PAGES SET HOME_PAGE_ICON='Y' where STATIC_PAGE_ID = ? AND SITE_ID=?";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, siteId);
			pstmt.executeUpdate();
			if(!isDlink){
			pstmt.close();
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, pageId);
			pstmt.setInt(2, siteId);
			pstmt.executeUpdate();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return null;

	}
	
	public static int addUpdateSystemParameter(SystemParameterModel systemParametersInfo,int userId,int siteId){

		Connection con = null;
		PreparedStatement ps1 = null;
		int created = 0;
		try {
			con = ConnectionManager.getDBConnection();
			if(CommonDBQuery.getSystemParamtersList().containsKey(systemParametersInfo.getConfigKey())){
				//System.err.println("Config Key Already Exist: "+systemParametersInfo.getConfigKey());
				return updateSystemParameter(con, systemParametersInfo, userId);
			}

			String strSql1 = "INSERT INTO SYSTEM_PARAMETERS (CONFIG_KEY, CONFIG_VALUE, CATEGORY, FIELD_TYPE, USER_EDITED, UPDATED_DATETIME, SYSTEM_PARAMETER_ID,SITE_ID) VALUES(?, ?, ?, ?, ?, SYSDATE,SYSTEM_PARAMETER_ID_SEQ.NEXTVAL,?)";
			ps1 = con.prepareStatement(strSql1);

			ps1.setString(1, systemParametersInfo.getConfigKey());
			if(systemParametersInfo.getFieldType() != null && systemParametersInfo.getFieldType().equalsIgnoreCase("CB")){
				ps1.setString(2, BannerTemplate.getInstance().convertBooleanToString(systemParametersInfo.getConfigValue()));
			}else{
				ps1.setString(2, ("" + systemParametersInfo.getConfigValue().toString().trim()));
			}
			ps1.setString(3, systemParametersInfo.getCategory());
			ps1.setString(4, systemParametersInfo.getFieldType());
			ps1.setLong(5, userId);
			ps1.setInt(6,siteId);
			created = ps1.executeUpdate();
			
		} catch (SQLException e) {
			if (e.getMessage().contains("unique constraint")) {
				return -1;
			}else{
				e.printStackTrace();
			}
			return -2;
		} finally {
		try {
			if (ps1 != null) {
				ps1.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return created;


	}
	
	public static int addUpdateSystemParameterMultiSite(SystemParameterModel systemParametersInfo, int userId,int siteId, int systemParamId){

		Connection con = null;
		PreparedStatement ps1 = null;
		int created = 0;
		try {
			con = ConnectionManager.getDBConnection();
			if(CommonDBQuery.getSystemParamtersList().containsKey(systemParametersInfo.getConfigKey())){
				//System.err.println("Config Key Already Exist: "+systemParametersInfo.getConfigKey());
				return updateSystemParameterViewMultiSite(con, systemParametersInfo, userId, siteId, systemParamId);
			}

			int sequenceValue = CommonDBQuery.getSequenceId("SYSTEM_PARAMETER_ID_SEQ.NEXTVAL");
			String strSql1 = "INSERT INTO SYSTEM_PARAMETERS (CONFIG_KEY, CONFIG_VALUE, CATEGORY, FIELD_TYPE, USER_EDITED, UPDATED_DATETIME, SYSTEM_PARAMETER_ID,SITE_ID) VALUES(?, ?, ?, ?, ?, SYSDATE,?,?)";
			ps1 = con.prepareStatement(strSql1);

			ps1.setString(1, systemParametersInfo.getConfigKey());
			if(systemParametersInfo.getFieldType() != null && systemParametersInfo.getFieldType().equalsIgnoreCase("CB")){
				ps1.setString(2, BannerTemplate.getInstance().convertBooleanToString(systemParametersInfo.getConfigValue()));
			}else{
				ps1.setString(2, ("" + systemParametersInfo.getConfigValue().toString().trim()));
			}
			ps1.setString(3, systemParametersInfo.getCategory());
			ps1.setString(4, systemParametersInfo.getFieldType());
			ps1.setLong(5, userId);
			ps1.setInt(6,sequenceValue);
			ps1.setInt(7,siteId);
			created = ps1.executeUpdate();
			if(created>0 && CommonDBQuery.websiteListArray!=null && CommonDBQuery.websiteListArray.size()>0) {
				for(WebsiteModel websiteModelObj : CommonDBQuery.websiteListArray) {
					String configValue = "";
					if(websiteModelObj.getSiteId() == siteId) {
						if(systemParametersInfo.getFieldType() != null && systemParametersInfo.getFieldType().equalsIgnoreCase("CB")){
							configValue = BannerTemplate.getInstance().convertBooleanToString(systemParametersInfo.getConfigValue());
						}else{
							configValue =  ("" + systemParametersInfo.getConfigValue().toString().trim());
						}
					}
					if (ps1 != null) { ps1.close(); }
					strSql1 = "INSERT INTO SITE_SYSTEM_PARAMETERS (SITE_SYSTEM_PARAMETER_ID,PARAMETER_VALUE,SITE_ID,SYSTEM_PARAMETER_ID, USER_EDITED, UPDATED_DATE_TIME) VALUES (SITE_SYSTEM_PARAMETER_ID_SEQ.NEXTVAL,?,?,?,?,SYSDATE)";
					ps1 = con.prepareStatement(strSql1);
					ps1.setString(1, configValue);
					ps1.setInt(2, websiteModelObj.getSiteId());
					ps1.setInt(3, sequenceValue);
					ps1.setLong(4, userId);
					created = ps1.executeUpdate();
				}
			}
			
		} catch (SQLException e) {
			if (e.getMessage().contains("unique constraint")) {
				return -1;
			}else{
				e.printStackTrace();
			}
			return -2;
		} finally {
		try {
			if (ps1 != null) {
				ps1.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return created;


	}
	
	public static int updateSystemParameter(Connection conn,SystemParameterModel systemParametersInfo,int userId){
		int updated = 0;
		PreparedStatement pstmt = null;
		
		try{
			String sql = "UPDATE SYSTEM_PARAMETERS SET CONFIG_VALUE=?  , USER_EDITED=? ,UPDATED_DATETIME=SYSDATE  WHERE CONFIG_KEY = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, systemParametersInfo.getConfigValue().toString());
			pstmt.setInt(2, userId);
			pstmt.setString(3, systemParametersInfo.getConfigKey());
			updated = pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		return updated;
	}
	
	public static int updateSystemParameterViewMultiSite(Connection conn,SystemParameterModel systemParametersInfo, int userId, int siteId, int systemParamId){
		int updated = 0;
		PreparedStatement pstmt = null;
		
		try{
			String sql = "UPDATE SITE_SYSTEM_PARAMETERS SET PARAMETER_VALUE=?, USER_EDITED=?, UPDATED_DATE_TIME=SYSDATE WHERE SITE_ID=? AND SYSTEM_PARAMETER_ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, systemParametersInfo.getConfigValue().toString());
			pstmt.setInt(2, userId);
			pstmt.setInt(3, siteId);
			pstmt.setInt(4, systemParamId);
			updated = pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
		return updated;
	}
	
	public static ArrayList<String> notificationList(){
		ArrayList<String> notificationNames = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT NOTIFICATION_NAME from CIMM_NOTIFICATIONS";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs!=null && rs.next())
			{
				notificationNames.add(rs.getString("NOTIFICATION_NAME"));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return notificationNames;
	}
	
	public static int exportFormContentsForSpecificFormName(OutputStream respOs,long formId,String formName){
		
int successval=1; 

		
		System.out.println("Inside FormReports");
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		Connection con =null;
		
		ArrayList<String> uiFieldNames = new ArrayList<String>();
		HashSet<String>  uniques = new LinkedHashSet<String>();
		HashMap<String, String> FieldNames=new HashMap<String, String>();
		HashMap<String, String> FieldNames1=new HashMap<String, String>();
		String updatedDate = null ;
		boolean empty;
		int formId1;
		String FieldNm;
		String FieldVl;
		String Field_val;
		int id = 0;
		try {
			Date date = new Date();
			con = ConnectionManager.getDBConnection();
	String strSql = "SELECT CF.CIMM_FORM_NAME,CF.CIMM_FORM_ID,CFC.FIELD_NAME,CFC.FIELD_VALUE,CFC.UPDATED_DATETIME,CU.USER_NAME FROM CIMM_USERS CU, CIMM_FORMS CF, CIMM_FORM_CONTENTS CFC WHERE CF.CIMM_FORM_ID = CFC.CIMM_FORM_ID AND CFC.USER_EDITED = CU.USER_ID(+) AND   CF.CIMM_FORM_NAME=? ";
	ps1 = con.prepareStatement(strSql);
	ps1.setString(1, formName);
	
	rs1 = ps1.executeQuery();

			
			int n = 2;
			long formIds = 0;

			while (rs1.next()) {
				uiFieldNames.add(rs1.getString("FIELD_NAME"));
			}
			uniques.addAll(uiFieldNames);
			uiFieldNames.clear();
			uiFieldNames.addAll(uniques);
			SXSSFWorkbook wb = new SXSSFWorkbook();

			Font font = wb.createFont();
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			
			CellStyle style;
			style = wb.createCellStyle(); 
			style.setFont(font); 
			
			
			String fileName = "FormReports_" + date.getTime()+ ".xlsx";
			//String fileName = "1.xlsx";
			
			FileOutputStream fos = new FileOutputStream(fileName);
			String FormName1=formName.replaceAll("[\\/:*?\"<>|]", "");
			System.out.println(FormName1);
	
			Sheet sheet = wb.createSheet(FormName1);  //create new sheet
					
			System.out.println("------Writing Header to Forms----------");
            
			int cellCount = 0;
			int rowCount = 1;
			int count1=0;
			
			Row row = sheet.createRow(rowCount);	
			Cell cell = row.createCell(cellCount++); 
			
			sheet = createNewSheetForFormReports(sheet,wb,row,rowCount,cell,cellCount,style,uiFieldNames);
			rowCount = 2;
			//
			
				

				String strSq2 = "SELECT CF.CIMM_FORM_NAME,CF.CIMM_FORM_ID,CFC.FIELD_NAME,CFC.FIELD_VALUE,CFC.UPDATED_DATETIME,CU.USER_NAME FROM CIMM_USERS CU, CIMM_FORMS CF, CIMM_FORM_CONTENTS CFC WHERE CF.CIMM_FORM_ID = CFC.CIMM_FORM_ID AND CFC.USER_EDITED = CU.USER_ID(+) AND   CF.CIMM_FORM_NAME=? ";
				ps2 = con.prepareStatement(strSq2);
				ps2.setString(1, formName);
				
				rs2 = ps2.executeQuery();

		 
			
			while (rs2.next()) 
			{

				formId1=rs2.getInt("CIMM_FORM_ID");
				FieldNm=rs2.getString("FIELD_NAME");
				FieldVl=rs2.getString("FIELD_VALUE");
				updatedDate=(rs2.getString("UPDATED_DATETIME"));
	              
				if((formId1==id || id==0))
				{
					FieldNames.put(FieldNm, FieldVl);
					id=formId1;
				}
	              
				else if(formId!=id && id!=0)
					{
						row = sheet.createRow(rowCount);	
						FieldNames1.put(FieldNm, FieldVl);
						int count = 0;
						//id=formId1;
						if(row.getRowNum() == 1000000)
							{			
								sheet = wb.createSheet(); 	
								cellCount = 0;
								rowCount = 1;
								row = sheet.createRow(rowCount);	
								cell = row.createCell(cellCount++); 
								sheet = createNewSheetForFormReports(sheet,wb,row,rowCount,cell,cellCount,style,uiFieldNames);
								rowCount = 2;
							}
							for(String s:uiFieldNames)
								{
									Field_val=FieldNames.get(s);
									cell = row.createCell(count++);
									cell.setCellValue(CMSConstants.encodeString(Field_val));											
								}
								cell = row.createCell(count++);
								cell.setCellValue(CMSConstants.encodeString(updatedDate));
								cell = row.createCell(count++);
								cell.setCellValue(id);
								FieldNames.clear();	
								FieldNames.putAll(FieldNames1);
								FieldNames1.clear();
								rowCount++;
								id=formId1;
							
					}
				
						
				}
              
			row = sheet.createRow(rowCount);	
			for(String s1:uiFieldNames)
			{
				Field_val=FieldNames.get(s1);
				cell = row.createCell(count1++);
				cell.setCellValue(CMSConstants.encodeString(Field_val));											
			}
			cell = row.createCell(count1++);
			cell.setCellValue(CMSConstants.encodeString(updatedDate));
			cell = row.createCell(count1++);
			cell.setCellValue(id);
			FieldNames.clear();
			FieldNames1.clear();
			
			File f = new File(fileName);
			System.out.println("file path---"+f.getAbsolutePath());
			wb.write(fos); 
			fos.close();
			ZipUploadStatusFile(CommonUtility.validateString(fileName), respOs); 

			boolean b = f.delete();
		
		}catch (Exception e) {
			e.printStackTrace();
			successval=-1;
		}

		finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
				if (ps1 != null) {
					ps1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (ps2 != null) {
					ps2.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				successval=-1;
				e.printStackTrace();
			}
		}
	
		return successval;
	}
	
	private static Sheet createNewSheetForFormReports(
			Sheet sheet, SXSSFWorkbook wb, Row row, int rowCount, Cell cell, int cellCount,CellStyle style,ArrayList<String> uiFieldNames) {

		for(int i=0;i<uiFieldNames.size();i++){
			   cell.setCellValue(uiFieldNames.get(i));
			   cell.setCellStyle(style);
			   cell = row.createCell(cellCount++);
			}
		cell = row.createCell(cellCount-1);
		cell.setCellValue("UPDATED_DATETIME");
		cell.setCellStyle(style);
		cell = row.createCell(cellCount++);
		cell.setCellValue("FORM_ID");
		cell.setCellStyle(style);
		return sheet;
	}

	public static void ZipUploadStatusFile(String fileName, OutputStream respOs)
	{
		try
		{  
			ZipOutputStream out = new ZipOutputStream(respOs);
			byte[] data = new byte[1024]; 
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
			int count;
			out.putNextEntry(new ZipEntry(fileName));
			while((count = in.read(data,0,1024)) != -1)
			{  
				out.write(data, 0, count);
			}
			in.close();
			out.flush();
			out.close();

			System.out.println("Your file is zipped");	
		}catch(Exception e)
		{
			e.printStackTrace();
		} 

	}

	public List<LocaleModel> getLoacleList() {

		//ArrayList<String> localeList = new ArrayList<String>();
		List<LocaleModel> ll=new ArrayList<LocaleModel>(); 
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT LOCALE_ID,COUNTRY_CODE,COUNTRY_NAME,LANGUAGE_CODE,LANGUAGE_NAME,VARIANT,IMAGE_NAME FROM LOCALE ORDER BY LANGUAGE_NAME";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs!=null && rs.next())
			{
				LocaleModel lm=new LocaleModel();
				/*localeList.add(rs.getString("LOCALE_ID"));
				localeList.add(rs.getString("LANGUAGE_NAME"));
				localeList.add(rs.getString("LANGUAGE_CODE"));*/
				lm.setLocaleId(rs.getInt("LOCALE_ID"));
				lm.setLanguageName(rs.getString("LANGUAGE_NAME"));
				lm.setLanguageCode(rs.getString("LANGUAGE_CODE"));
				ll.add(lm);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return ll;
	
	}

	public static int getIdByPageName(String pageName) {
		//ArrayList<String> localeList = new ArrayList<String>();
		int pageId = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT STATIC_PAGE_ID FROM STATIC_PAGES WHERE PAGE_NAME = '"+pageName+"'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs!=null && rs.next())
			{
				
				pageId=rs.getInt("STATIC_PAGE_ID");
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return pageId;
	}
	
	public List<WebsiteModel> getWebsiteList() {
		List<WebsiteModel> wl=new ArrayList<WebsiteModel>(); 
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT SITE_ID,SITE_NAME,SITE_URL,USER_EDITED,UPDATED_DATETIME,STATUS,SITE_DESIGN_ID,MOOD_ID,PRIMARY_SITE,WFL_PHASE_ID,SITE_LOGO,CLIENT_ID,TAXONOMY_ID,SUBSET_ID FROM WEBSITES WHERE STATUS = 'Y' ORDER BY SITE_ID";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs!=null && rs.next())
			{
				WebsiteModel wm=new WebsiteModel();
				wm.setSiteId(rs.getInt("SITE_ID"));
				wm.setSiteName(rs.getString("SITE_NAME"));
				wm.setSiteUrl(rs.getString("SITE_URL"));
				wm.setUserEdited(rs.getInt("USER_EDITED"));
				wm.setStatus(rs.getString("STATUS"));
				wm.setSiteDesignId(rs.getInt("SITE_DESIGN_ID"));
				wm.setSiteMoodId(rs.getInt("MOOD_ID"));
				wm.setPrimarySite(rs.getString("PRIMARY_SITE"));
				wm.setWflPhaseId(rs.getInt("WFL_PHASE_ID"));
				wm.setSiteLogo(rs.getString("SITE_LOGO"));
				wm.setSiteClientId(rs.getInt("CLIENT_ID"));
				wm.setSiteTaxonomyId(rs.getInt("TAXONOMY_ID"));
				wm.setSiteSubset(rs.getInt("SUBSET_ID"));
				wl.add(wm);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return wl;
	}
	
	public List<ThemeModel> getThemeList(int siteId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ThemeModel> themeList = new ArrayList<ThemeModel>();  
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT * FROM THEMES WHERE STATUS='A' AND SITE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, siteId);
			rs = pstmt.executeQuery();
			while(rs!=null && rs.next())
			{
				ThemeModel tm=new ThemeModel();
				tm.setId(rs.getInt("ID"));
				tm.setThemeName(rs.getString("THEME_NAME"));
				tm.setCss(rs.getString("CSS"));
				tm.setScss(rs.getString("SCSS"));
				tm.setCssName(rs.getString("CSS_NAME"));
				tm.setJsonData(rs.getString("JSON_DATA"));
				tm.setSiteId(rs.getInt("SITE_ID"));
				tm.setStartDate(convertDate(rs.getString("START_DATE")));
				tm.setEndDate(convertDate(rs.getString("END_DATE")));
				tm.setScheduled(rs.getString("SCHEDULED"));
				
				themeList.add(tm);
			}
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return themeList;
	}
	
	public String convertDate(String inputDate){
		String convertedDate = null;
		try{
			if(CommonUtility.validateString(inputDate).length()>0){
		        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		        SimpleDateFormat outFormat = new SimpleDateFormat("MM/dd/yyyy");

		        Date date = inFormat.parse(inputDate);
		        System.out.println(date);
		        StringBuffer sb = new StringBuffer();
		       

		        convertedDate = outFormat.format(date); //here's your new date string
		        System.out.println(convertedDate);
			}
		        
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return convertedDate;
	}
	
	public int deleteTheme(ThemeModel themeModel){
		int i = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "DELETE FROM THEMES WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, themeModel.getId());
			i = pstmt.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return i;
	}
	public int addUpdateTheme(ThemeModel themeModel){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try{
			int themeId = 0;
			conn = ConnectionManager.getDBConnection();
			String sql = "UPDATE THEMES SET SCSS = ?, CSS=?,JSON_DATA = ?,USER_EDITED = ?,START_DATE = ?, END_DATE = ?, SCHEDULED = ?, CSS_NAME = ?, UPDATED_DATETIME = SYSDATE WHERE ID = ?";
			if(themeModel.getId() == 0){
				themeId = CommonDBQuery.getSequenceId("THEMES_SEQ");
				sql = "INSERT INTO THEMES ( THEME_NAME, SCSS, CSS,JSON_DATA,SITE_ID, CSS_NAME, USER_EDITED,ID,START_DATE, END_DATE, SCHEDULED,UPDATED_DATETIME) VALUES (?, ?,?,?,?,?,?,?,?,?,?,SYSDATE)";
			}
			pstmt = conn.prepareStatement(sql);
			if(themeModel.getId() > 0){
				themeId = themeModel.getId();
				pstmt.setString(1, themeModel.getScss());
				pstmt.setCharacterStream(2, new StringReader(themeModel.getCss()),themeModel.getCss().length());
				pstmt.setCharacterStream(3, new StringReader(themeModel.getJsonData()),themeModel.getJsonData().length());
				pstmt.setInt(4, themeModel.getUserId());
				pstmt.setString(5, themeModel.getStartDate());
				pstmt.setString(6, themeModel.getEndDate());
				pstmt.setString(7, themeModel.getScheduled());
				pstmt.setString(8, themeModel.getCssName());
				pstmt.setInt(9, themeModel.getId());
			}else{
				pstmt.setString(1, themeModel.getThemeName());
				pstmt.setString(2, themeModel.getScss());
				pstmt.setCharacterStream(3, new StringReader(themeModel.getCss()),themeModel.getCss().length());
				pstmt.setCharacterStream(4, new StringReader(themeModel.getJsonData()),themeModel.getJsonData().length());
				pstmt.setInt(5, themeModel.getSiteId());
				pstmt.setString(6, themeModel.getCssName());
				pstmt.setInt(7, themeModel.getUserId());
				pstmt.setInt(8, themeId);
				pstmt.setString(9, themeModel.getStartDate());
				pstmt.setString(10, themeModel.getEndDate());
				pstmt.setString(11, themeModel.getScheduled());
			}
			
			
			i = pstmt.executeUpdate();
			if(i > 0){
				i = themeId;
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally{
			
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return i;
	}
	
	public int cloneTheme(ThemeModel themeModel){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "insert into themes select THEMES_SEQ.NEXTVAL,?,SCSS,CSS,JSON_DATA,SITE_ID,USER_EDITED,SYSDATE,?,STATUS,START_DATE,END_DATE,SCHEDULED FROM THEMES WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, themeModel.getThemeName());
			pstmt.setString(2, themeModel.getCssName());
			pstmt.setLong(3, themeModel.getId());
			i = pstmt.executeUpdate();

		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally{
			
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return i;
	}

	public int setHomePage(int homePageId,int siteId,String dlink) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "UPDATE WEBSITES SET HOME_PAGE_ID=? WHERE SITE_ID=?";
			pstmt = conn.prepareStatement(sql);
            if(dlink!=null && dlink.equalsIgnoreCase("Y")) {
            	pstmt.setString(1, null);
			}else {
				pstmt.setInt(1, homePageId);
			}
			pstmt.setInt(2, siteId);
			i = pstmt.executeUpdate();

		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally{
			
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return i;
	}

	public int setHomePageIcon(int siteId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 0;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT HOME_PAGE_ID FROM WEBSITES WHERE SITE_ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, siteId);
			rs = pstmt.executeQuery();
			while(rs!=null && rs.next())
			{
            i= rs.getInt("HOME_PAGE_ID");
			}
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally{
			
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return i;
	}
	
	public List<PageHistoryModel> getPageHistory(int pageId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<PageHistoryModel> pageHistoryList = new ArrayList<PageHistoryModel>();  
		try{
			conn = ConnectionManager.getDBConnection();
			String sql = "SELECT ASP.PAGE_NAME,ASP.PAGE_TITLE,ASP.PAGE_CONTENT,CU.USER_NAME,ASP.UPDATED_DATETIME,L.LANGUAGE_CODE, SP.PAGE_CONTENT CURRENT_CONTENT\r\n" + 
					"FROM AT_STATIC_PAGES ASP , STATIC_PAGES SP, LOCALE L , CIMM_USERS CU\r\n" + 
					"WHERE ASP.STATIC_PAGE_ID=SP.STATIC_PAGE_ID and ASP.LOCALE_ID=L.LOCALE_ID and \r\n" + 
					"CU.USER_NAME = (CASE WHEN ASP.USER_EDITED = 0 THEN 'cimmAdmin'\r\n" + 
					"                     WHEN ASP.USER_EDITED = CU.USER_ID THEN CU.USER_NAME\r\n" + 
					"                        ELSE null END)\r\n" + 
					"and ASP.STATIC_PAGE_ID= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pageId);
			rs = pstmt.executeQuery();
			while(rs!=null && rs.next())
			{
				PageHistoryModel ph=new PageHistoryModel();
				ph.setId(pageId);
				ph.setPageName(rs.getString("PAGE_NAME"));
				ph.setPageTitle(rs.getString("PAGE_TITLE"));
				ph.setPageContent(rs.getString("PAGE_CONTENT"));
				ph.setUserEdited(rs.getString("USER_NAME"));
				ph.setUpdatedDateTime(rs.getString("UPDATED_DATETIME"));
				ph.setLocaleCode(rs.getString("LANGUAGE_CODE"));
				if(rs.getString("PAGE_CONTENT").toString().equals(rs.getString("CURRENT_CONTENT")) )
				{
					ph.setRecord("Current");
				}else {
					ph.setRecord("History");
				}
				pageHistoryList.add(ph);
			}
			
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}
		return pageHistoryList;
	}
}
