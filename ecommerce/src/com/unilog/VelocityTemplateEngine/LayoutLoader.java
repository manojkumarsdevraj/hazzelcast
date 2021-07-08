package com.unilog.VelocityTemplateEngine;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unilog.VelocityTemplateEngine.Model.LayoutModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.utility.CommonUtility;

public class LayoutLoader {

	private static LayoutModel layoutModel;
	private static LinkedHashMap<String, Properties> messageProperties;

	public static void setLayoutModel(LayoutModel layoutModel) {
		LayoutLoader.layoutModel = layoutModel;
	}

	public static LayoutModel getLayoutModel() {
		return layoutModel;
	}

	static {
		generateLayout();
		generateLocale();
	}

	public static void generateLayout() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT SECTION_NAME PAGE_NAME,LAYOUT_NAME,TEMPLATES,SITE_NAME FROM LAYOUTS";
		layoutModel = new LayoutModel();
		LinkedHashMap<String, LinkedHashMap<String, TemplateModel>> layoutBySite = new LinkedHashMap<String, LinkedHashMap<String, TemplateModel>>();
		try {
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			LinkedHashMap<String, TemplateModel> layoutList = new LinkedHashMap<String, TemplateModel>();
			String siteNameFromDb = "";
			while (rs.next()) {
				Gson gson = new Gson();

				if (CommonUtility.validateString(rs.getString("SITE_NAME")).length() > 0) {
					siteNameFromDb = rs.getString("SITE_NAME");
				}
				layoutList = layoutBySite.get(siteNameFromDb);
				if (layoutList == null)
					layoutList = new LinkedHashMap<String, TemplateModel>();
				String templateString = "{\"LayoutName\":\"" + rs.getString("LAYOUT_NAME") + "\"}";

				System.out.println("Site Name : " + siteNameFromDb + " : Page Name : " + rs.getString("PAGE_NAME"));
				System.out.println(templateString);

				TemplateModel template = gson.fromJson(templateString, TemplateModel.class);
				templateString = "{" + rs.getString("TEMPLATES") + "}";
				LinkedHashMap<String, String> myMap = gson.fromJson(templateString,
						new TypeToken<LinkedHashMap<String, String>>() {
						}.getType());
				template.setLayoutObj(myMap);
				layoutList.put(rs.getString("PAGE_NAME"), template);
				layoutBySite.put(siteNameFromDb, layoutList);
			}

			/*
			 * layoutList = layoutBySite.get("ECOMMERCE_STD_TEMPLATE_V2");
			 * if(layoutList==null) layoutList = new LinkedHashMap<String, TemplateModel>();
			 * Gson gson = new Gson(); TemplateModel template = gson.fromJson(
			 * "{\"LayoutName\":\"Layout_Layout2\",\"BodyContent\":\"punchoutConfig.html\"}",
			 * TemplateModel.class); layoutList.put("PunchoutConfig", template); gson = new
			 * Gson(); template = gson.fromJson(
			 * "{\"LayoutName\":\"Layout_Layout3\",\"BodyContent\":\"interactiveadviserloader.html\"}",
			 * TemplateModel.class); layoutList.put("InteractiveAdviserPage", template);
			 * layoutBySite.put("ECOMMERCE_STD_TEMPLATE_V2", layoutList);
			 */

			layoutModel.setLayoutList(layoutList);
			layoutModel.setLayoutBySite(layoutBySite);

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);

		}

	}

	public static void generateLocale() {
		FileInputStream fis = null;
		try {
			Properties props = null;
			String[] arrVal = null;
			if (CommonDBQuery.getSystemParamtersList().get("LOCALE_LIST") != null) {
				arrVal = CommonDBQuery.getSystemParamtersList().get("LOCALE_LIST").split(",");
			}
			messageProperties = new LinkedHashMap<String, Properties>();
			if (arrVal != null && arrVal.length > 0) {
				for (String lnTemp : arrVal) {
					System.out.println(CommonDBQuery.getSystemParamtersList().get("SITE_BUILDER_PROPERTIES_FILE_PATH")
							+ "/" + lnTemp + "/properties.properties");
					fis = new FileInputStream(
							CommonDBQuery.getSystemParamtersList().get("SITE_BUILDER_PROPERTIES_FILE_PATH") + "/"
									+ lnTemp + "/properties.properties");
					props = new Properties();
					props.load(fis);
					messageProperties.put(lnTemp.trim().toUpperCase(), props);
					fis.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setMessageProperties(LinkedHashMap<String, Properties> messageProperties) {
		LayoutLoader.messageProperties = messageProperties;
	}

	public static LinkedHashMap<String, Properties> getMessageProperties() {
		return messageProperties;
	}

}
