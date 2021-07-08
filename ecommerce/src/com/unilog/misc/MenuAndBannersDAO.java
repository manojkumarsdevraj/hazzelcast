package com.unilog.misc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import oracle.jdbc.OracleTypes;

import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.ULLog;
import com.unilog.products.ProductHunterSolr;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.WordPressMenuModel;
import com.unilog.velocitytool.CIMM2VelocityTool;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

public class MenuAndBannersDAO {
	private static ArrayList<MenuAndBannersModal> topMenu=null;
	//public static ArrayList<MenuAndBannersModal> topMenu=null;
	public static LinkedHashMap<String,String> leftMenu=null;
	public static LinkedHashMap<String,String> manufacturerList=null;
	public static String thumbNail;
	public static String itemImage;
	public static String detailImage;
	public static String enlargedImage;
	public static String taxonomyImage;
	public static String documentPath;
	public static String brandLogoPath;
	public static String buyingCompanyLogoPath;
	public static String eclipseIsDownMessage;
	public static String eclipseDownCartMessage;
	public static String POValidStatus;
	public static ArrayList<MenuAndBannersModal> leftStaticMenu = null;
	public static ArrayList<MenuAndBannersModal> headerStaticMenu = null;
	public static ArrayList<MenuAndBannersModal> footerStaticMenu = null;
	public static ArrayList<MenuAndBannersModal> topStaticMenu = null;
	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> topMenuSublist =null;
	public static LinkedHashMap<String, ArrayList<ProductsModel>> topMenuListBySubset =null;
	public static LinkedHashMap<String,ArrayList<WordPressMenuModel>> wpMenu = null;
	public static LinkedHashMap<String,ArrayList<WordPressMenuModel>> wpMenuStatic = null;
	public static LinkedHashMap<Integer, ArrayList<MenuAndBannersModal>> secondLevelFromParent = null;
	private static LinkedHashMap<Integer, ArrayList<MenuAndBannersModal>> staticSubmenuList = null;
	private static LinkedHashMap<Integer,Integer> staticLinkId = null;
	private static LinkedHashMap<Integer,Integer> staticLinkParentId = null;
	public static LinkedHashMap<String,ArrayList<MenuAndBannersModal>>  thirdLevelFromParent = null;
	public static LinkedHashMap<String,ArrayList<MenuAndBannersModal>>  fourthLevelFromParent = null;
	public static LinkedHashMap<String,ArrayList<MenuAndBannersModal>>  fifthLevelFromParent = null;
	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> secondLevelMenu = null;
	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> thirdLevelMenu = null;
	private static LinkedHashMap<String, LinkedHashMap<String, ArrayList<MenuAndBannersModal>>> allSecMenuList = null;
	private static LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MenuAndBannersModal>>> allThirdLevMenuList = null;
	private static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> allManufactuerData = null;
	private static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> allBrandData = null;
	public static  ArrayList<MenuAndBannersModal> manufacturerListData=null;
	public static  ArrayList<MenuAndBannersModal> brandListData=null;
	public static ArrayList<WordPressMenuModel> wpNavStaticMenu = null;
	
	 
	static
	{
		getHeaderMenu();
		getHomePageBanners();
		getStaticMenu();
		getStaticSubmenu();		
	}
	
	public static synchronized  void getHeaderMenu()
	{
		
		eclipseIsDownMessage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEISDOWNMESSAGE");
		eclipseDownCartMessage = CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNCARTMESSAGE");
		POValidStatus=CommonDBQuery.getSystemParamtersList().get("IS_PO_MANDATORY");
		thumbNail = CommonDBQuery.getSystemParamtersList().get("THUMBNAIL");
        itemImage = CommonDBQuery.getSystemParamtersList().get("ITEMIMAGE");
        detailImage = CommonDBQuery.getSystemParamtersList().get("DETAILIMAGE");
        enlargedImage = CommonDBQuery.getSystemParamtersList().get("ENLARGEIMAGE");
        taxonomyImage = CommonDBQuery.getSystemParamtersList().get("TAXONOMYIMAGEPATH");
        documentPath = CommonDBQuery.getSystemParamtersList().get("DOCUMENTS");
        brandLogoPath = CommonDBQuery.getSystemParamtersList().get("BRANDLOGO");
		buyingCompanyLogoPath=CommonDBQuery.getSystemParamtersList().get("BUYINGCOMPANYLOGO");
		
		ResultSet rs = null;
	    Connection  conn = null;
	    Statement stmt = null;
	    int rows = 1;
	    
        
        try {
        	
        	conn = ConnectionManager.getDBConnection();
	          stmt = conn.createStatement();
	          
		         String categoryBeginLevel = CommonDBQuery.getSystemParamtersList().get("BEGIN_CATEGORY_DISPLAY_AT_LEVEL");
		         int categoryBegin = 1;
		         
		         if(categoryBeginLevel!=null && !categoryBeginLevel.trim().equalsIgnoreCase(""))
		         {
		        	 categoryBegin = CommonUtility.validateNumber(categoryBeginLevel);
		         }
		         
		          
		          topMenu = new ArrayList<MenuAndBannersModal>();
		          
		          topMenuListBySubset = new LinkedHashMap<String, ArrayList<ProductsModel>>();
		          topMenuListBySubset =  ProductHunterSolr.generateNavigationMenu(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"), rows,true);
		          if(topMenuListBySubset!=null && topMenuListBySubset.size()>0)
		          {
		        	  for (Map.Entry<String, ArrayList<ProductsModel>> entry : topMenuListBySubset.entrySet()) 
		              {            
		        		  ArrayList<ProductsModel> checkNavSize = entry.getValue();
		        		  if(checkNavSize!=null && checkNavSize.size()>0)
		        		  {
		        			  rows = checkNavSize.get(0).getResultCount();
		        			  break;
		        		  }
		                 
		              }
		          }
		          
		          if(rows>1)
		          topMenuListBySubset = ProductHunterSolr.generateNavigationMenu(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"), rows,false);
		          allSecMenuList = ProductHunterSolr.generateNavigationSubMenu(CommonDBQuery.getSystemParamtersList().get("TAXONOMY_ID"), rows);
		          
		          rows = 1;
		          allManufactuerData = ProductHunterSolr.generateManufacturerMenu(rows);
		          if(allManufactuerData!=null && allManufactuerData.size()>0)
		          {
		        	  for (Map.Entry<String, ArrayList<MenuAndBannersModal>> entry : allManufactuerData.entrySet()) 
		              {            
		        		  ArrayList<MenuAndBannersModal> checkBrandSize = entry.getValue();
		        		  if(checkBrandSize!=null && checkBrandSize.size()>0)
		        		  {
		        			  rows = checkBrandSize.get(0).getResultCount();
		        			  break;
		        		  }
		                 
		              }
		          }
		          if(rows>1){
		        	  allManufactuerData = ProductHunterSolr.generateManufacturerMenu(rows);  
		          }
		          rows = 1;
		          allBrandData =  ProductHunterSolr.generateBrandMenu(rows);
		          if(allBrandData!=null && allBrandData.size()>0)
		          {
		        	  for (Map.Entry<String, ArrayList<MenuAndBannersModal>> entry : allBrandData.entrySet()) 
		              {            
		        		  ArrayList<MenuAndBannersModal> checkBrandSize = entry.getValue();
		        		  if(checkBrandSize!=null && checkBrandSize.size()>0)
		        		  {
		        			  rows = checkBrandSize.get(0).getResultCount();
		        			  break;
		        		  }
		                 
		              }
		          }
		          
		          if(rows>1){
		        	  allBrandData =  ProductHunterSolr.generateBrandMenu(rows);
		          }
		         
		         categoryBegin = categoryBegin+1; // For third level set it as 2
		         

        } catch (Exception e) {
	    	  ULLog.errorTrace(e.fillInStackTrace());
	    	
	          e.printStackTrace();
	
	      } finally {
	    	  ConnectionManager.closeDBResultSet(rs);
	    	  ConnectionManager.closeDBStatement(stmt);
	    	  ConnectionManager.closeDBConnection(conn);	
	      } // finally
	}
	
	public static void getStaticSubmenu() {
		
		

		staticSubmenuList = new LinkedHashMap<Integer, ArrayList<MenuAndBannersModal>>();
		ArrayList<MenuAndBannersModal> staticMenuList = new ArrayList<MenuAndBannersModal>();
		staticLinkId = new LinkedHashMap<Integer, Integer>();
		staticLinkParentId = new LinkedHashMap<Integer, Integer>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql =PropertyAction.SqlContainer.get("staticSubMenu");
		
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, CommonDBQuery.getGlobalSiteId());
			rs = pstmt.executeQuery();
			System.out.println("----------------------HOME PAGE BANNERS --------------------");
			while(rs.next())
			{
				staticMenuList = staticSubmenuList.get(rs.getInt("PARENT_ID"));
				MenuAndBannersModal  staticPage = new MenuAndBannersModal();
				staticPage.setStaticPageType(rs.getString("STATIC_PAGE_TYPE"));
				
				staticPage.setMenuId(rs.getString("STATIC_PAGE_ID"));
				staticPage.setMenuName(rs.getString("LINK_NAME"));
				
				String menuUrlLink = CommonUtility.validateString(rs.getString("STATIC_PAGE_URL"));
    			if(CommonUtility.validateString(rs.getString("CUSTOM_URL")).length()>0) {
    			 menuUrlLink = CommonUtility.validateString(rs.getString("CUSTOM_URL"));
    			}else if (CommonUtility.validateString(rs.getString("PAGE_TITLE")).length()>0) {
    			 menuUrlLink = CommonUtility.validateString(rs.getString("PAGE_TITLE"));
    			}else if (CommonUtility.validateString(rs.getString("PAGE_NAME")).length()>0) {
    			 menuUrlLink = CommonUtility.validateString(rs.getString("PAGE_NAME"));
    			}
    			staticPage.setStaticPageUrl(menuUrlLink);
				
				String itemUrl = rs.getString("LINK_NAME");
				itemUrl = itemUrl.replaceAll(" ","-");
	            staticPage.setItemUrl(itemUrl);
				if(staticMenuList==null)
				{
					staticMenuList = new ArrayList<MenuAndBannersModal>();
					staticMenuList.add(staticPage);
					
				}
				else
				{
					staticMenuList.add(staticPage);
				}
				staticLinkId.put(rs.getInt("STATIC_PAGE_ID"), rs.getInt("STATIC_LINK_ID"));
				staticLinkParentId.put(rs.getInt("STATIC_PAGE_ID"), rs.getInt("PARENT_ID"));
				staticSubmenuList.put(rs.getInt("PARENT_ID"), staticMenuList);
				
				
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
	}

	public static ArrayList<MenuAndBannersModal> getHomePageBanners()
	{
		ArrayList<MenuAndBannersModal> homePageBanners = new ArrayList<MenuAndBannersModal>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//String sql ="SELECT BANNER_IMAGE_NAME,IMAGE_TYPE ,BANNER_LANDING_URL FROM BANNERS B,VALUE_LIST_DATA V WHERE SYSDATE BETWEEN B.EFFECTIVE_DATE AND B.END_DATE AND V.BANNER_ID = B.BANNER_ID AND V.VALUE_LIST_ID IN (SELECT VALUE_LIST_ID FROM VALUES_LIST WHERE VALUE_LIST_NAME='Home Page Banners')";
		//String sql ="SELECT BANNER_IMAGE_NAME,IMAGE_TYPE ,BANNER_LANDING_URL FROM BANNERS B,VALUE_LIST_DATA V WHERE SYSDATE BETWEEN NVL(B.EFFECTIVE_DATE,SYSDATE) AND NVL(B.END_DATE,SYSDATE) AND V.BANNER_ID = B.BANNER_ID AND V.VALUE_LIST_ID IN (SELECT VALUE_LIST_ID FROM VALUES_LIST WHERE VALUE_LIST_NAME='Home Page Banners')";
		String sql ="SELECT BANNER_IMAGE_NAME,IMAGE_TYPE ,BANNER_LANDING_URL, LIST_VALUE FROM BANNERS B,VALUE_LIST_DATA V WHERE SYSDATE BETWEEN NVL(B.EFFECTIVE_DATE,SYSDATE) AND NVL(B.END_DATE,SYSDATE) AND V.BANNER_ID = B.BANNER_ID AND V.VALUE_LIST_ID IN (SELECT VALUE_LIST_ID FROM VALUES_LIST WHERE VALUE_LIST_NAME='Home Page Banners')"; 
		
		try
		{
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("----------------------HOME PAGE BANNERS --------------------");
			while(rs.next())
			{
				MenuAndBannersModal bannerInfo = new MenuAndBannersModal();
				bannerInfo.setImageName(rs.getString("BANNER_IMAGE_NAME"));
				bannerInfo.setImageType(rs.getString("IMAGE_TYPE"));
				bannerInfo.setImageURL(rs.getString("BANNER_LANDING_URL"));
				bannerInfo.setBannerName(rs.getString("LIST_VALUE"));
				System.out.println("BNIMG/typ/url ----------------------"+rs.getString("BANNER_IMAGE_NAME")+"/"+rs.getString("IMAGE_TYPE")+"/"+rs.getString("BANNER_LANDING_URL")+"/"+rs.getString("LIST_VALUE"));
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
	
	public static void getWpMenuList(Connection conn){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WordPressMenuModel staticMenu = null;
		int i = 1001;
		ArrayList<WordPressMenuModel> navStaticMenu = new ArrayList<WordPressMenuModel>();
		LinkedHashMap<String, ArrayList<WordPressMenuModel>> tempList = new LinkedHashMap<String, ArrayList<WordPressMenuModel>>();
		
			wpMenuStatic = new LinkedHashMap<String, ArrayList<WordPressMenuModel>>();

		wpStaticMenu = new LinkedHashMap<String, LinkedHashMap<Integer,ArrayList<WordPressMenuModel>>>();
		String wpUrl = "";
		
		String customUrl = "";
		String urlType = "";
		try{
			String sql = "SELECT WSLT1.LINK_NAME PARENT_NAME,WSLT.STATIC_LINK_ID,WSLT.LINK_NAME,WSLT.LEVEL_NUMBER,WSLT.STATIC_PAGE_ID,WSLT.STATIC_PAGE_TYPE,WSLT.STATIC_PAGE_URL,CUSTOM_URL,REPLACE(WSLT.PARENT_ID,WSLT1.STATIC_LINK_ID,0) PARENT_ID,WSLT.DISP_SEQ,WSLT.OPEN_PAGE_IN from WEB_STATIC_LINKS_TREE WSLT,WEB_STATIC_LINKS_TREE WSLT1,STATIC_PAGES SP WHERE WSLT1.LEVEL_NUMBER=1  AND WSLT1.SITE_ID = ? AND WSLT.SITE_ID = WSLT1.SITE_ID AND WSLT.LEVEL_NUMBER > 1 AND WSLT.LEVEL1 = WSLT1.STATIC_LINK_ID AND SP.STATIC_PAGE_ID(+) = WSLT.STATIC_PAGE_ID ORDER BY NLSSORT(PARENT_NAME, 'NLS_SORT=generic_m'),LEVEL_NUMBER, DISP_SEQ, NLSSORT(STATIC_LINK_ID, 'NLS_SORT=generic_m')";
			LinkedHashMap<Integer, ArrayList<WordPressMenuModel>> menuType = new LinkedHashMap<Integer, ArrayList<WordPressMenuModel>>();
			ArrayList<WordPressMenuModel> menuList = new ArrayList<WordPressMenuModel>();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, CommonDBQuery.getGlobalSiteId());
			rs = pstmt.executeQuery();
			while(rs.next()){
				staticMenu = new WordPressMenuModel();
				urlType = rs.getString("STATIC_PAGE_TYPE");
				navStaticMenu = wpMenuStatic.get(rs.getString("PARENT_NAME"));
				if(navStaticMenu==null)
					navStaticMenu = new ArrayList<WordPressMenuModel>();
				
				staticMenu.setID(rs.getLong("STATIC_LINK_ID"));
				staticMenu.setMenu_item_parent(rs.getString("PARENT_ID"));
				staticMenu.setOpenIn(rs.getString("OPEN_PAGE_IN"));
				if(CommonUtility.validateString(urlType).equalsIgnoreCase("URL")){
					wpUrl = rs.getString("STATIC_PAGE_URL");
				}else{
					wpUrl = rs.getString("CUSTOM_URL");
					if(CommonUtility.validateString(wpUrl).equalsIgnoreCase("")){
						wpUrl = rs.getString("LINK_NAME");
						wpUrl  = wpUrl.replaceAll(" ","-");
					}
					wpUrl = "/"+wpUrl;
				}
				
				staticMenu.setUrl(wpUrl);
				staticMenu.setTitle(rs.getString("LINK_NAME"));
				staticMenu.setDb_id(rs.getLong("STATIC_LINK_ID"));
				staticMenu.setMenu_order(""+i);
				 i++;
				 
				 navStaticMenu.add(staticMenu);
				 menuType = wpStaticMenu.get(rs.getString("PARENT_NAME"));
				 
				 if(menuType==null){
					 menuType = new LinkedHashMap<Integer, ArrayList<WordPressMenuModel>>();
					 System.out.println(rs.getString("PARENT_NAME"));
				 }
				 
			menuList = menuType.get(rs.getInt("PARENT_ID"));
			if(menuList==null)
				menuList = new ArrayList<WordPressMenuModel>();
			
			
			menuList.add(staticMenu);
			menuType.put(rs.getInt("PARENT_ID"), menuList);
			wpMenuStatic.put(rs.getString("PARENT_NAME"), navStaticMenu);	 
			wpStaticMenu.put(rs.getString("PARENT_NAME"), menuType);
			
			
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
		}
	}
	
	public static void getStaticMenu()
	{
		
		System.out.println("Getting Static Menu :");
		
		Connection conn = null;
        ResultSet menuResult = null;
        CallableStatement stmt = null;
       
      
        try {
        	conn = ConnectionManager.getDBConnection();
        	 getWpMenuList(conn);
        	LinkedHashMap<String, ArrayList<MenuAndBannersModal>> menuType = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
        	ArrayList<MenuAndBannersModal> menuHeader = new ArrayList<MenuAndBannersModal>();
        	stmt = conn.prepareCall("{call STATIC_MENU_PROC_V2(?,?)}");
        	stmt.setInt(1, CommonDBQuery.getGlobalSiteId());
        	stmt.registerOutParameter(2,OracleTypes.CURSOR);
        	 stmt.execute();
        	 if(stmt.getObject(2)!=null)
        	 {
        		 menuResult = (ResultSet) stmt.getObject(2);
        	 }
        	 
        	 if(menuResult!=null)
        	 {
        		 while(menuResult.next())
        		 {
        			 MenuAndBannersModal menuHeaderVal = new MenuAndBannersModal();
        			 
        			 menuHeaderVal.setMenuName(menuResult.getString("L1_NAME"));
        			 menuHeaderVal.setMenuId(menuResult.getString("STATIC_PAGE_ID"));
        			 menuHeaderVal.setOpenPageIn(menuResult.getString("OPEN_PAGE_IN"));
        			 if(menuResult.getString("L2_NAME")!=null && !menuResult.getString("L2_NAME").trim().equalsIgnoreCase(""))
        			 {
        				 String itemUrl = menuResult.getString("L2_NAME");
 		             	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
 		             	itemUrl = itemUrl.replaceAll(" ","-");
 		             	menuHeaderVal.setItemUrl(itemUrl);
        				 menuHeaderVal.setLevel1Menu(menuResult.getString("L2_NAME"));
        				 menuHeaderVal.setLevel4Menu(menuResult.getString("L1_NAME"));
        			 }
        			 
        			 if(menuResult.getString("L3_NAME")!=null && !menuResult.getString("L3_NAME").trim().equalsIgnoreCase(""))
        			 {
        				 String itemUrl = menuResult.getString("L3_NAME");
  		             	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
  		             	itemUrl = itemUrl.replaceAll(" ","-");
  		             	menuHeaderVal.setItemUrl(itemUrl);
        				 menuHeaderVal.setLevel2Menu(menuResult.getString("L3_NAME"));
        				 menuHeaderVal.setLevel4Menu(menuResult.getString("L2_NAME"));
        			 }
        			 
        			 if(menuResult.getString("L4_NAME")!=null && !menuResult.getString("L4_NAME").trim().equalsIgnoreCase(""))
        			 {
        				 String itemUrl = menuResult.getString("L4_NAME");
  		             	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
  		             	itemUrl = itemUrl.replaceAll(" ","-");
  		             	menuHeaderVal.setItemUrl(itemUrl);
        				 menuHeaderVal.setLevel3Menu(menuResult.getString("L4_NAME"));
        				 menuHeaderVal.setLevel4Menu(menuResult.getString("L3_NAME"));
        			 }
        			 
        			 if(menuResult.getString("L5_NAME")!=null && !menuResult.getString("L5_NAME").trim().equalsIgnoreCase(""))
        			 {
        				 String itemUrl = menuResult.getString("L5_NAME");
  		             	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
  		             	itemUrl = itemUrl.replaceAll(" ","-");
  		             	menuHeaderVal.setItemUrl(itemUrl);
        				 menuHeaderVal.setLevel4Menu(menuResult.getString("L5_NAME"));
        				 
        			 }
        			 if(menuResult.getString("L6_NAME")!=null && !menuResult.getString("L6_NAME").trim().equalsIgnoreCase(""))
        			 {
        				 String itemUrl = menuResult.getString("L6_NAME");
  		             	//itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
  		             	itemUrl = itemUrl.replaceAll(" ","-");
  		             	menuHeaderVal.setItemUrl(itemUrl);
        				 menuHeaderVal.setLevel5Menu(menuResult.getString("L6_NAME"));
        			 }
        			 menuHeaderVal.setMenuId(menuResult.getString("STATIC_PAGE_ID"));
        			 menuHeaderVal.setLevelNumber(menuResult.getInt("LEVEL_NUMBER"));
        			 menuHeaderVal.setStaticPageType(menuResult.getString("STATIC_PAGE_TYPE"));
        			 
        			 String menuUrlLink = CommonUtility.validateString(menuResult.getString("STATIC_PAGE_URL"));
        			 if(CommonUtility.validateString(menuResult.getString("CUSTOM_URL")).length()>0) {
        				 menuUrlLink = CommonUtility.validateString(menuResult.getString("CUSTOM_URL"));
        			 }else if (CommonUtility.validateString(menuResult.getString("PAGE_TITLE")).length()>0) {
        				 menuUrlLink = CommonUtility.validateString(menuResult.getString("PAGE_TITLE"));
        			 }else if (CommonUtility.validateString(menuResult.getString("PAGE_NAME")).length()>0) {
        				 menuUrlLink = CommonUtility.validateString(menuResult.getString("PAGE_NAME"));
        			 }
        			 
        			 menuHeaderVal.setStaticPageUrl(menuUrlLink);

        			 menuHeader.add(menuHeaderVal);
        		 }
        		 ArrayList<MenuAndBannersModal> genMenu1 = new ArrayList<MenuAndBannersModal>();
        		 ArrayList<MenuAndBannersModal> genMenu2 = new ArrayList<MenuAndBannersModal>();
        		 ArrayList<MenuAndBannersModal> genMenu3 = new ArrayList<MenuAndBannersModal>();
        		 ArrayList<MenuAndBannersModal> genMenu4 = new ArrayList<MenuAndBannersModal>();
        		 ArrayList<MenuAndBannersModal> genMenu5 = new ArrayList<MenuAndBannersModal>();
        		 
        		 
        		 int loop = 0;
        		 for(MenuAndBannersModal mnList : menuHeader)
        		 {
        			 loop++;
        			 
        			 ArrayList<MenuAndBannersModal> tempMenu = menuType.get(mnList.getMenuName());
        			 
        			 MenuAndBannersModal levelMenu = new MenuAndBannersModal();
        			
        			 if(mnList.getLevelNumber()==2)
        			 {
        				 System.out.println(mnList.getMenuName() + " - " + mnList.getLevel1Menu() + " - "+mnList.getMenuId());
        				 levelMenu.setMenuName(mnList.getMenuName());
        				 levelMenu.setItemUrl(mnList.getItemUrl());
        				 levelMenu.setLevel1Menu(mnList.getLevel1Menu());
        				 levelMenu.setMenuId(mnList.getMenuId());
        				 levelMenu.setStaticPageType(mnList.getStaticPageType());
        				 levelMenu.setStaticPageUrl(mnList.getStaticPageUrl());
        				 levelMenu.setLevelNumber(mnList.getLevelNumber());
        				 levelMenu.setOpenPageIn(mnList.getOpenPageIn());
        				 if(tempMenu!=null && tempMenu.size()>0)
        				 {
        					 genMenu1 = tempMenu;
        					 genMenu1.add(levelMenu);
        					 menuType.put(mnList.getMenuName(), genMenu1);
        					 
        				 }
        				 else
        				 {
        					 genMenu1 = new ArrayList<MenuAndBannersModal>();
        					 genMenu1.add(levelMenu);
        					 menuType.put(mnList.getMenuName(), genMenu1);
        					 
        				 }
        			 }
        			 else if(mnList.getLevelNumber()==3)
        			 {
        				 //System.out.println("At the level number : " + mnList.getLevelNumber() + " : and loop : " + loop);
        				 genMenu2 = new ArrayList<MenuAndBannersModal>();
        				 levelMenu.setMenuName(mnList.getMenuName());
        				 levelMenu.setItemUrl(mnList.getItemUrl());
        				 levelMenu.setLevel1Menu(mnList.getLevel1Menu());
        				 levelMenu.setLevel2Menu(mnList.getLevel2Menu());
        				 levelMenu.setMenuId(mnList.getMenuId());
        				 levelMenu.setStaticPageType(mnList.getStaticPageType());
        				 levelMenu.setStaticPageUrl(mnList.getStaticPageUrl());
        				 levelMenu.setLevelNumber(mnList.getLevelNumber());
        				 levelMenu.setOpenPageIn(mnList.getOpenPageIn());
        				 ArrayList<MenuAndBannersModal> higherLevelMenu  = menuType.get(mnList.getMenuName());
        				 
        				 for(MenuAndBannersModal eachHigherMenu : higherLevelMenu){
        					 if(eachHigherMenu.getLevel1Menu().equalsIgnoreCase(mnList.getLevel1Menu())){
        						 eachHigherMenu.getMenuList().add(levelMenu);
        						 break;
        					 }
        				 }
        			 }else if(mnList.getLevelNumber()==4){
        				 System.out.println(mnList.getMenuName() + " - " + mnList.getLevel1Menu() + " - " +mnList.getLevel2Menu()+ " - " + mnList.getLevel3Menu()+ " - "+mnList.getMenuId());
        				 levelMenu.setMenuName(mnList.getMenuName());
        				 levelMenu.setItemUrl(mnList.getItemUrl());
        				 levelMenu.setLevel1Menu(mnList.getLevel1Menu());
        				 levelMenu.setLevel2Menu(mnList.getLevel2Menu());
        				 levelMenu.setLevel3Menu(mnList.getLevel3Menu());
        				 levelMenu.setLevel4Menu(mnList.getLevel4Menu());
        				 levelMenu.setLevel5Menu(mnList.getLevel5Menu());
        				 levelMenu.setMenuId(mnList.getMenuId());
        				 levelMenu.setStaticPageType(mnList.getStaticPageType());
        				 levelMenu.setStaticPageUrl(mnList.getStaticPageUrl());
        				 levelMenu.setLevelNumber(mnList.getLevelNumber());
        				 levelMenu.setOpenPageIn(mnList.getOpenPageIn());
        				 System.out.println(mnList.getMenuName());
        				 genMenu3.add(levelMenu);
        				 
        				 ArrayList<MenuAndBannersModal> higherLevelMenu = menuType.get(mnList.getMenuName());
        				 
        				 for(MenuAndBannersModal eachHigherMenu : higherLevelMenu){
        					 if(mnList.getLevel1Menu().equalsIgnoreCase(eachHigherMenu.getLevel1Menu())){
        						 ArrayList<MenuAndBannersModal> firstInnerMenu = eachHigherMenu.getMenuList();
        						 for(MenuAndBannersModal currentFirstInnerMenu : firstInnerMenu){
        							 if(currentFirstInnerMenu.getLevel2Menu().equalsIgnoreCase(mnList.getLevel2Menu())){
        								 currentFirstInnerMenu.getMenuList().add(levelMenu);
        								 break;
        							 }
        						 }
        						 break;
        					 }
        				 }
        				 
        				 
        			}else if(mnList.getLevelNumber()==5){
        				 System.out.println(mnList.getMenuName() + " - " + mnList.getLevel1Menu() + " - " +mnList.getLevel2Menu()+ " - " + mnList.getLevel3Menu()+  " - "+mnList.getLevel4Menu()+" - "+mnList.getMenuId());
        				 levelMenu.setMenuName(mnList.getMenuName());
        				 levelMenu.setItemUrl(mnList.getItemUrl());
        				 levelMenu.setLevel1Menu(mnList.getLevel1Menu());
        				 levelMenu.setLevel2Menu(mnList.getLevel2Menu());
        				 levelMenu.setLevel3Menu(mnList.getLevel3Menu());
        				 levelMenu.setLevel4Menu(mnList.getLevel4Menu());
        				 levelMenu.setLevel5Menu(mnList.getLevel5Menu());
        				 levelMenu.setStaticPageType(mnList.getStaticPageType());
        				 levelMenu.setStaticPageUrl(mnList.getStaticPageUrl());
        				 levelMenu.setMenuId(mnList.getMenuId());
        				 levelMenu.setLevelNumber(mnList.getLevelNumber());
        				 levelMenu.setOpenPageIn(mnList.getOpenPageIn());
        				 System.out.println(mnList.getMenuName());
        				 genMenu4.add(levelMenu);
        				 
        				 ArrayList<MenuAndBannersModal> higherLevelMenu = menuType.get(mnList.getMenuName());
        				 
        				 for(MenuAndBannersModal eachHigherMenu : higherLevelMenu){
        					 if(mnList.getLevel1Menu().equalsIgnoreCase(eachHigherMenu.getLevel1Menu())){
        						 ArrayList<MenuAndBannersModal> firstInnerMenuList = eachHigherMenu.getMenuList();
        						 for(MenuAndBannersModal currentFirstInnerMenu : firstInnerMenuList){
        							 if(currentFirstInnerMenu.getLevel2Menu().equalsIgnoreCase(mnList.getLevel2Menu())){
        								 ArrayList<MenuAndBannersModal> secondInnerMenuList = currentFirstInnerMenu.getMenuList();
        								 for(MenuAndBannersModal currentSecondInnerMenu : secondInnerMenuList){
        									 if(currentSecondInnerMenu.getLevel3Menu().equalsIgnoreCase(mnList.getLevel3Menu())){
        										 currentSecondInnerMenu.getMenuList().add(levelMenu);
        										 break;
        									 }
        								 }
        								 break;
        							 }
        						 }
        						 break;
        					 }
        				 }
        				 
        			}else if(mnList.getLevelNumber()==6){
        				 System.out.println(mnList.getMenuName() + " - " + mnList.getLevel1Menu() + " - " +mnList.getLevel2Menu()+ " - " + mnList.getLevel3Menu()+  " - "+mnList.getLevel4Menu()+" - "+mnList.getLevel5Menu()+" - "+mnList.getMenuId());
        				 levelMenu.setMenuName(mnList.getMenuName());
        				 levelMenu.setItemUrl(mnList.getItemUrl());
        				 levelMenu.setLevel1Menu(mnList.getLevel1Menu());
        				 levelMenu.setLevel2Menu(mnList.getLevel2Menu());
        				 levelMenu.setLevel3Menu(mnList.getLevel3Menu());
        				 levelMenu.setLevel4Menu(mnList.getLevel4Menu());
        				 levelMenu.setLevel5Menu(mnList.getLevel5Menu());
        				 levelMenu.setStaticPageType(mnList.getStaticPageType());
        				 levelMenu.setStaticPageUrl(mnList.getStaticPageUrl());
        				 levelMenu.setLevelNumber(mnList.getLevelNumber());
        				 levelMenu.setOpenPageIn(mnList.getOpenPageIn());
        				 levelMenu.setMenuId(mnList.getMenuId());
        				 
        				 System.out.println(mnList.getMenuName());
        				 
        				 genMenu5.add(levelMenu);
        				 
        				 ArrayList<MenuAndBannersModal> higherLevelMenu = menuType.get(mnList.getMenuName());
        				 
        				 for(MenuAndBannersModal eachHigherMenu : higherLevelMenu){
        					 if(mnList.getLevel1Menu().equalsIgnoreCase(eachHigherMenu.getLevel1Menu())){
        						 ArrayList<MenuAndBannersModal> firstInnerMenuList = eachHigherMenu.getMenuList();
        						 for(MenuAndBannersModal currentFirstInnerMenu : firstInnerMenuList){
        							 if(currentFirstInnerMenu.getLevel2Menu().equalsIgnoreCase(mnList.getLevel2Menu())){
        								 ArrayList<MenuAndBannersModal> secondInnerMenuList = currentFirstInnerMenu.getMenuList();
        								 for(MenuAndBannersModal currentSecondInnerMenu : secondInnerMenuList){
        									 if(currentSecondInnerMenu.getLevel3Menu().equalsIgnoreCase(mnList.getLevel3Menu())){
        										 ArrayList<MenuAndBannersModal> thirdInnerMenuList = currentSecondInnerMenu.getMenuList();
        										 for(MenuAndBannersModal currentThirdInnerMenuList : thirdInnerMenuList){
        											 if(currentThirdInnerMenuList.getLevel4Menu().equalsIgnoreCase(mnList.getLevel4Menu())){
        												 currentThirdInnerMenuList.getMenuList().add(levelMenu);
        												 break;
        											 }
        										 }
        										 break;
        									 }
        								 }
        								 break;
        							 }
        						 }
        						 break;
        					 }
        				 }
        				 
        			 }
        		 }
        		
        		 leftStaticMenu = new ArrayList<MenuAndBannersModal>();
        		 headerStaticMenu = new ArrayList<MenuAndBannersModal>();
        		 footerStaticMenu = new ArrayList<MenuAndBannersModal>();
        		 topStaticMenu = new ArrayList<MenuAndBannersModal>();
        		
        		 if(menuType!=null) {
        			 if(menuType.get("LeftMenu")!=null) {
            			 leftStaticMenu = menuType.get("LeftMenu");
            		 }
            		 if(menuType.get("Header")!=null) {
            			 headerStaticMenu = menuType.get("Header");
            		 }
            		 if(menuType.get("Footer")!=null) {
            			 footerStaticMenu = menuType.get("Footer");
            		 }
            		 if(menuType.get("TopMenu")!=null) {
            			 topStaticMenu = menuType.get("TopMenu");
            		 }
        		 }
        		 
        		 int mId =1;
        		 secondLevelFromParent = new LinkedHashMap<Integer, ArrayList<MenuAndBannersModal>>();
        		 for(MenuAndBannersModal tMenu:topStaticMenu)
        		 {
        			 if(tMenu.getMenuList().size()>0)
        			 {
        				 for(MenuAndBannersModal sMenu:tMenu.getMenuList())
        				 {
        					 //if(tMenu.getMenuId()!=null && !tMenu.getMenuId().trim().equalsIgnoreCase(""))
        					 //sMenu.setParentId(UsersDAO.validateNumber(tMenu.getMenuId()));
        					 sMenu.setParentId(mId);
        				 }
        				 secondLevelFromParent.put(mId, tMenu.getMenuList());
        			 }
        			 mId++;
        		 }
        		 mId =101;
        		 for(MenuAndBannersModal tMenu:footerStaticMenu)
        		 {
        			 if(tMenu.getMenuList().size()>0)
        			 {
        				 for(MenuAndBannersModal sMenu:tMenu.getMenuList())
        				 {
        					 sMenu.setParentId(mId);
        				 }
        				 secondLevelFromParent.put(mId, tMenu.getMenuList());
        			 }
        			 mId++;
        		 }
        		 
        		 int secLoop =0 ;
         		
        		 thirdLevelFromParent = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
        		for(Entry<Integer,ArrayList<MenuAndBannersModal>> menu : secondLevelFromParent.entrySet()){
        			ArrayList<MenuAndBannersModal> secMenu = menu.getValue();
        			if(secMenu!=null && secMenu.size()>0){
        			  for(MenuAndBannersModal thirdLevel : secMenu){
        				  ArrayList<MenuAndBannersModal> tempLevel4Menu = new ArrayList<MenuAndBannersModal>();
        				  for(MenuAndBannersModal levelfouritem : genMenu3){
        					  if(levelfouritem.getLevel1Menu().equalsIgnoreCase(thirdLevel.getLevel1Menu()) && levelfouritem.getLevel2Menu().equals(thirdLevel.getLevel2Menu())){
        						  tempLevel4Menu.add(levelfouritem);
        					  }
        				  }
        				if(tempLevel4Menu!=null && tempLevel4Menu.size()>0){
        					thirdLevelFromParent.put(thirdLevel.getLevel2Menu().toString(), tempLevel4Menu);
        					secLoop++;
        				}
        			  }
        			}
        		}
        		int thirdLoop = 0;
        		fourthLevelFromParent = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
        		
        		for(Entry<String,ArrayList<MenuAndBannersModal>> menu : thirdLevelFromParent.entrySet())
        		{
        		   
        			
        			ArrayList<MenuAndBannersModal > trdMenu = menu.getValue();
        			
        			
        			if(trdMenu!=null && trdMenu.size()>0)
        			{
        				for(MenuAndBannersModal level4menu : trdMenu)
        				{
        					ArrayList<MenuAndBannersModal> tempLevel5Menu = new ArrayList<MenuAndBannersModal>();
        					      					
        				
        				for(MenuAndBannersModal levelfiveitem : genMenu4)
        				{
        				        					
        					if(levelfiveitem.getLevel1Menu().equalsIgnoreCase(level4menu.getLevel1Menu()) && levelfiveitem.getLevel2Menu().equals(level4menu.getLevel2Menu()) && levelfiveitem.getLevel3Menu().equals(level4menu.getLevel3Menu()))
        							{
        						      
        						      tempLevel5Menu.add(levelfiveitem);
        							}
        				}
        				
        				if(tempLevel5Menu!=null && tempLevel5Menu.size()>0)
        				{
        				fourthLevelFromParent.put(level4menu.getLevel3Menu().toString(), tempLevel5Menu);
        				thirdLoop++;
        				}
        			}
        			
        			}
        			
        			
        			
        		
        		     
        			
        			
        		     
        		}
        		
        			
        		
        		fifthLevelFromParent = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
        		
        		for(Entry<String,ArrayList<MenuAndBannersModal>> menu : fourthLevelFromParent.entrySet())
        		{
        		   
        			
        			ArrayList<MenuAndBannersModal > trdMenu = menu.getValue();
        			
        			
        			if(trdMenu!=null && trdMenu.size()>0)
        			{
        				for(MenuAndBannersModal level4menu : trdMenu)
        				{
        					ArrayList<MenuAndBannersModal> tempLevel6Menu = new ArrayList<MenuAndBannersModal>();
        					      					
        				
        				for(MenuAndBannersModal levelsixthitem : genMenu5)
        				{
        				        					
        					if(levelsixthitem.getLevel4Menu().equalsIgnoreCase(level4menu.getLevel4Menu()) && levelsixthitem.getLevel2Menu().equals(level4menu.getLevel2Menu()) && levelsixthitem.getLevel3Menu().equals(level4menu.getLevel3Menu()))
        							{
        						      
        						      tempLevel6Menu.add(levelsixthitem);
        							}
        					
        				}
        				if(tempLevel6Menu!=null && tempLevel6Menu.size()>0)
        				{
        				fifthLevelFromParent.put(level4menu.getLevel4Menu().toString(), tempLevel6Menu);
        				}
        				
        				
        				
        			}
        			
        			}
        		}
        		topMenuSublist = new LinkedHashMap<String, ArrayList<MenuAndBannersModal>>();
        		topMenuSublist = menuType;
        		 for (Entry<String, ArrayList<MenuAndBannersModal>> entry : menuType.entrySet()) 
     	        {            
     	            
     	            if(entry.getValue().size()>0)
     	            {
     	            	for(MenuAndBannersModal testVal : entry.getValue())
     	            	{
     	            		System.out.println(entry.getKey()+" : " + testVal.getLevel1Menu() + " - " + testVal.getMenuId());
     	            		if(testVal.getMenuList().size()>0)
     	            		{
     	            			for(MenuAndBannersModal testVal2 : testVal.getMenuList())
             	            	{
     	            				System.out.println("\t\tLevel 2 : " + testVal2.getLevel2Menu() + " - " + testVal2.getMenuId());
             	            	}
     	            		}
     	            	}
     	            }
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
		     	 ConnectionManager.closeDBResultSet(menuResult);
		     	 ConnectionManager.closeDBStatement(stmt);
		     	 ConnectionManager.closeDBConnection(conn);
		  }
				
	}
	
	public static void displayMenuData()
	{	
		for (Map.Entry<String, String> entry : leftMenu.entrySet()) 
        {            
            
            System.out.println(entry.getKey()+" --------  "+entry.getValue());
        }
	}

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
		        	strQuery= PropertyAction.SqlContainer.get("manufacturerListQuery");
		        	ULLog.sqlTrace(strQuery.toString());

		        	preStat = conn.prepareStatement(strQuery);
		        	preStat.setInt(1, subsetId);
					rs = preStat.executeQuery();
					
					  while(rs.next()){			  
						 MenuAndBannersModal manufacturerListVal = new MenuAndBannersModal();
						 String itemUrl = rs.getString("BRAND_NAME").toString().trim();
			             //itemUrl = itemUrl.replaceAll("[^A-Za-z0-9 ]", "");
			             itemUrl = itemUrl.replaceAll(" ","-");
			             manufacturerListVal.setItemUrl(itemUrl);
						 manufacturerListVal.setBrandId(rs.getInt("BRAND_ID"));
						 manufacturerListVal.setBrandName(rs.getString("BRAND_NAME"));
						 brandMenu.add(manufacturerListVal);
					 }	   
		        }
			          catch(SQLException e)
			          {
			        	 ULLog.error("Error in getShopByManufacturerData():ERROR:" +  e.fillInStackTrace());
			          	e.printStackTrace();
			          }
			          catch(Exception e)
			          {
			 			 ULLog.errorTrace(e.fillInStackTrace());
			          	e.printStackTrace();
			          }
			
			       finally {	    	 
			    	  ConnectionManager.closeDBStatement(preStat);
				      ConnectionManager.closeDBConnection(conn);	
			      }
		
		return brandMenu;
	}

	public  static List<MenuAndBannersModal> featuredManufacturersList(HttpSession session){
		
		String tempSubset = (String) session.getAttribute("userSubsetId");
	    String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	    if(session.getAttribute("generalCatalog")==null){
	    	session.setAttribute("generalCatalog","0");
	    	tempGeneralSubset = (String) session.getAttribute("generalCatalog");
	    }
	    int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
		int subsetId = CommonUtility.validateNumber(tempSubset);
		
		List<MenuAndBannersModal> featureManufacturers = new ArrayList<MenuAndBannersModal>();
		
		String searchIndex = "PH_SEARCH_"+subsetId;
		if(generalSubset>0 && generalSubset!=subsetId)
			searchIndex = "PH_SEARCH_"+generalSubset+"_"+subsetId;
		
		ArrayList<MenuAndBannersModal> manufacturerList =  MenuAndBannersDAO.getAllManufactuerData().get(searchIndex);
		
		LinkedHashMap<Integer, ProductsModel> featuredManufacturerCustom = CIMM2VelocityTool.getInstance().featuredManufacturersCimmVelocity();
		
		for(Integer key: featuredManufacturerCustom.keySet()){
		for (MenuAndBannersModal eachManufacturer : manufacturerList) {
			if(key== eachManufacturer.getManufacturerId()){
				featureManufacturers.add(eachManufacturer);
			}
			}
		}
		return featureManufacturers;
	}
	
	public static void setStaticSubmenuList(LinkedHashMap<Integer, ArrayList<MenuAndBannersModal>> staticSubmenuList) {
		MenuAndBannersDAO.staticSubmenuList = staticSubmenuList;
	}

	public static LinkedHashMap<Integer, ArrayList<MenuAndBannersModal>> getStaticSubmenuList() {
		return staticSubmenuList;
	}

	public static void setStaticLinkId(LinkedHashMap<Integer,Integer> staticLinkId) {
		MenuAndBannersDAO.staticLinkId = staticLinkId;
	}

	public static LinkedHashMap<Integer,Integer> getStaticLinkId() {
		return staticLinkId;
	}

	public static void setStaticLinkParentId(LinkedHashMap<Integer,Integer> staticLinkParentId) {
		MenuAndBannersDAO.staticLinkParentId = staticLinkParentId;
	}

	public static LinkedHashMap<Integer,Integer> getStaticLinkParentId() {
		return staticLinkParentId;
	}

	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getAllBrandData() {
		return allBrandData;
	}

	public static void setAllBrandData(
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> allBrandData) {
		MenuAndBannersDAO.allBrandData = allBrandData;
	}

	public static ArrayList<MenuAndBannersModal> getBrandListData() {
		return brandListData;
	}

	public static void setBrandListData(ArrayList<MenuAndBannersModal> brandListData) {
		MenuAndBannersDAO.brandListData = brandListData;
	}

	public static void setWpMenu(LinkedHashMap<String,ArrayList<WordPressMenuModel>> wpMenu) {
		MenuAndBannersDAO.wpMenu = wpMenu;
	}

	public static LinkedHashMap<String,ArrayList<WordPressMenuModel>> getWpMenu() {
		return wpMenu;
	}
	
	public static ArrayList<WordPressMenuModel> getWpNavStaticMenu() {
		return wpNavStaticMenu;
	}

	public static void setWpNavStaticMenu(
			ArrayList<WordPressMenuModel> wpNavStaticMenu) {
		MenuAndBannersDAO.wpNavStaticMenu = wpNavStaticMenu;
	}

	public static LinkedHashMap<String,LinkedHashMap<Integer,ArrayList<WordPressMenuModel>>> wpStaticMenu = null;

	

	public static LinkedHashMap<String, LinkedHashMap<Integer, ArrayList<WordPressMenuModel>>> getWpStaticMenu() {
		return wpStaticMenu;
	}

	public static void setWpStaticMenu(
			LinkedHashMap<String, LinkedHashMap<Integer, ArrayList<WordPressMenuModel>>> wpStaticMenu) {
		MenuAndBannersDAO.wpStaticMenu = wpStaticMenu;
	}

	public static LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MenuAndBannersModal>>> getAllThirdLevMenuList() {
		return allThirdLevMenuList;
	}

	public static void setAllThirdLevMenuList(
			LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MenuAndBannersModal>>> allThirdLevMenuList) {
		MenuAndBannersDAO.allThirdLevMenuList = allThirdLevMenuList;
	}

	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getThirdLevelMenu() {
		return thirdLevelMenu;
	}

	public static void setThirdLevelMenu(
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> thirdLevelMenu) {
		MenuAndBannersDAO.thirdLevelMenu = thirdLevelMenu;
	}

	public static LinkedHashMap<String, ArrayList<ProductsModel>> getTopMenuListBySubset() {
		return topMenuListBySubset;
	}

	public static void setTopMenuListBySubset(
			LinkedHashMap<String, ArrayList<ProductsModel>> topMenuListBySubset) {
		MenuAndBannersDAO.topMenuListBySubset = topMenuListBySubset;
	}

	public static LinkedHashMap<String, String> getLeftMenu() {
		return leftMenu;
	}

	public static void setLeftMenu(LinkedHashMap<String, String> leftMenu) {
		MenuAndBannersDAO.leftMenu = leftMenu;
	}

	public static LinkedHashMap<String, String> getManufacturerList() {
		return manufacturerList;
	}

	public static void setManufacturerList(
			LinkedHashMap<String, String> manufacturerList) {
		MenuAndBannersDAO.manufacturerList = manufacturerList;
	}

	public static String getThumbNail() {
		return thumbNail;
	}

	public static void setThumbNail(String thumbNail) {
		MenuAndBannersDAO.thumbNail = thumbNail;
	}

	public static String getItemImage() {
		return itemImage;
	}

	public static void setItemImage(String itemImage) {
		MenuAndBannersDAO.itemImage = itemImage;
	}

	public static String getDetailImage() {
		return detailImage;
	}

	public static void setDetailImage(String detailImage) {
		MenuAndBannersDAO.detailImage = detailImage;
	}

	public static String getEnlargedImage() {
		return enlargedImage;
	}

	public static void setEnlargedImage(String enlargedImage) {
		MenuAndBannersDAO.enlargedImage = enlargedImage;
	}

	public static String getTaxonomyImage() {
		return taxonomyImage;
	}

	public static void setTaxonomyImage(String taxonomyImage) {
		MenuAndBannersDAO.taxonomyImage = taxonomyImage;
	}

	public static String getDocumentPath() {
		return documentPath;
	}

	public static void setDocumentPath(String documentPath) {
		MenuAndBannersDAO.documentPath = documentPath;
	}

	public static String getBrandLogoPath() {
		return brandLogoPath;
	}

	public static void setBrandLogoPath(String brandLogoPath) {
		MenuAndBannersDAO.brandLogoPath = brandLogoPath;
	}

	public static String getBuyingCompanyLogoPath() {
		return buyingCompanyLogoPath;
	}

	public static void setBuyingCompanyLogoPath(String buyingCompanyLogoPath) {
		MenuAndBannersDAO.buyingCompanyLogoPath = buyingCompanyLogoPath;
	}

	public static String getEclipseIsDownMessage() {
		return eclipseIsDownMessage;
	}

	public static void setEclipseIsDownMessage(String eclipseIsDownMessage) {
		MenuAndBannersDAO.eclipseIsDownMessage = eclipseIsDownMessage;
	}

	public static String getEclipseDownCartMessage() {
		return eclipseDownCartMessage;
	}

	public static void setEclipseDownCartMessage(String eclipseDownCartMessage) {
		MenuAndBannersDAO.eclipseDownCartMessage = eclipseDownCartMessage;
	}

	public static String getPOValidStatus() {
		return POValidStatus;
	}

	public static void setPOValidStatus(String pOValidStatus) {
		POValidStatus = pOValidStatus;
	}

	public static ArrayList<MenuAndBannersModal> getLeftStaticMenu() {
		return leftStaticMenu;
	}

	public static void setLeftStaticMenu(
			ArrayList<MenuAndBannersModal> leftStaticMenu) {
		MenuAndBannersDAO.leftStaticMenu = leftStaticMenu;
	}

	public static ArrayList<MenuAndBannersModal> getHeaderStaticMenu() {
		return headerStaticMenu;
	}

	public static void setHeaderStaticMenu(
			ArrayList<MenuAndBannersModal> headerStaticMenu) {
		MenuAndBannersDAO.headerStaticMenu = headerStaticMenu;
	}

	public static ArrayList<MenuAndBannersModal> getFooterStaticMenu() {
		return footerStaticMenu;
	}

	public static void setFooterStaticMenu(
			ArrayList<MenuAndBannersModal> footerStaticMenu) {
		MenuAndBannersDAO.footerStaticMenu = footerStaticMenu;
	}

	public static ArrayList<MenuAndBannersModal> getTopStaticMenu() {
		return topStaticMenu;
	}

	public static void setTopStaticMenu(ArrayList<MenuAndBannersModal> topStaticMenu) {
		MenuAndBannersDAO.topStaticMenu = topStaticMenu;
	}

	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getTopMenuSublist() {
		return topMenuSublist;
	}

	public static void setTopMenuSublist(
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> topMenuSublist) {
		MenuAndBannersDAO.topMenuSublist = topMenuSublist;
	}

	public static LinkedHashMap<Integer, ArrayList<MenuAndBannersModal>> getSecondLevelFromParent() {
		return secondLevelFromParent;
	}

	public static void setSecondLevelFromParent(
			LinkedHashMap<Integer, ArrayList<MenuAndBannersModal>> secondLevelFromParent) {
		MenuAndBannersDAO.secondLevelFromParent = secondLevelFromParent;
	}

	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getThirdLevelFromParent() {
		return thirdLevelFromParent;
	}

	public static void setThirdLevelFromParent(
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> thirdLevelFromParent) {
		MenuAndBannersDAO.thirdLevelFromParent = thirdLevelFromParent;
	}

	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getFourthLevelFromParent() {
		return fourthLevelFromParent;
	}

	public static void setFourthLevelFromParent(
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> fourthLevelFromParent) {
		MenuAndBannersDAO.fourthLevelFromParent = fourthLevelFromParent;
	}

	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getFifthLevelFromParent() {
		return fifthLevelFromParent;
	}

	public static void setFifthLevelFromParent(
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> fifthLevelFromParent) {
		MenuAndBannersDAO.fifthLevelFromParent = fifthLevelFromParent;
	}

	public static ArrayList<MenuAndBannersModal> getManufacturerListData() {
		return manufacturerListData;
	}

	public static void setManufacturerListData(
			ArrayList<MenuAndBannersModal> manufacturerListData) {
		MenuAndBannersDAO.manufacturerListData = manufacturerListData;
	}

	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getSecondLevelMenu() {
		return secondLevelMenu;
	}

	public static void setSecondLevelMenu(
			LinkedHashMap<String, ArrayList<MenuAndBannersModal>> secondLevelMenu) {
		MenuAndBannersDAO.secondLevelMenu = secondLevelMenu;
	}
	
	public static void setAllSecMenuList(LinkedHashMap<String, LinkedHashMap<String, ArrayList<MenuAndBannersModal>>> allSecMenuList) {
		MenuAndBannersDAO.allSecMenuList = allSecMenuList;
	}

	public static LinkedHashMap<String, LinkedHashMap<String, ArrayList<MenuAndBannersModal>>> getAllSecMenuList() {
		return allSecMenuList;
	}
	
	public static void setAllManufactuerData(LinkedHashMap<String, ArrayList<MenuAndBannersModal>> allManufactuerData) {
		MenuAndBannersDAO.allManufactuerData = allManufactuerData;
	}

	public static LinkedHashMap<String, ArrayList<MenuAndBannersModal>> getAllManufactuerData() {
		return allManufactuerData;
	}
	public static void setTopMenu(ArrayList<MenuAndBannersModal> topMenu) {
		MenuAndBannersDAO.topMenu = topMenu;
	}
	public static ArrayList<MenuAndBannersModal> getTopMenu() {
		return topMenu;
	}
	public static LinkedHashMap<String, ArrayList<WordPressMenuModel>> getWpMenuStatic() {
		return wpMenuStatic;
	}

	public static void setWpMenuStatic(
			LinkedHashMap<String, ArrayList<WordPressMenuModel>> wpMenuStatic) {
		MenuAndBannersDAO.wpMenuStatic = wpMenuStatic;
	}
}
