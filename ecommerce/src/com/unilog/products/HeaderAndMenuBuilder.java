package com.unilog.products;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.utility.CommonUtility;

public class HeaderAndMenuBuilder {

	public static ArrayList<ProductsModel> getTopCategories()
	{
		ArrayList<ProductsModel> categoryMenu = new ArrayList<ProductsModel>();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			/*ArrayList<MenuAndBannersModal> tempTopmenu = new ArrayList<MenuAndBannersModal>();
			ArrayList<Integer> userTopTab = new ArrayList<Integer>();
			userTopTab = (ArrayList<Integer>) session.getAttribute("userTabList");
			//-------- Compare the taxonomy Id And then replace userTablist
			
			//---------Compare the taxonomy Id And then replace userTablist
			tempTopmenu = MenuAndBannersDAO.getTopMenu();
			
			//tempTopmenu = MenuAndBannersDAO.getTopMenu();
			for(MenuAndBannersModal tMenu:tempTopmenu)
			{
				boolean addMenu = false;
				if(userTopTab!=null && userTopTab.size()>0)
				{
					for(Integer uTop:userTopTab)
					{
						if(CommonUtility.validateNumber(tMenu.getCategoryCode())== uTop){
						   addMenu = true;
						   break;
					   }
					   }
				   }
				   if(addMenu)
				   {
					   categoryMenu.add(tMenu);
				   }
			   }*/
			  String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
				int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			String tempSubset = (String) session.getAttribute("userSubsetId");
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String searchIndex = "PH_SEARCH_"+subsetId;
			if(generalSubset>0 && generalSubset!=subsetId)
				searchIndex = "PH_SEARCH_"+generalSubset+"_"+subsetId;
			
			categoryMenu = MenuAndBannersDAO.getTopMenuListBySubset().get(searchIndex);
		}catch(Exception e){
			e.printStackTrace();
		}
		return categoryMenu;
	}
	
	public static ArrayList<MenuAndBannersModal> topStaticMenu()
	{
		ArrayList<MenuAndBannersModal> topStaticMenu = new ArrayList<MenuAndBannersModal>();
		try
		{
		topStaticMenu = MenuAndBannersDAO.topStaticMenu;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return topStaticMenu;
	}
	public static LinkedHashMap<String,ArrayList<MenuAndBannersModal>> topStaticMenuThirdLevel()
	{
		LinkedHashMap<String,ArrayList<MenuAndBannersModal>> topStaticMenuThirdLevel = new LinkedHashMap<String,ArrayList<MenuAndBannersModal>>();
		try
		{
			topStaticMenuThirdLevel = MenuAndBannersDAO.thirdLevelFromParent;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return topStaticMenuThirdLevel;
	}
	public static LinkedHashMap<String,ArrayList<MenuAndBannersModal>> topStaticMenuFourthLevel()
	{
		LinkedHashMap<String,ArrayList<MenuAndBannersModal>> topStaticMenuThirdLevel = new LinkedHashMap<String,ArrayList<MenuAndBannersModal>>();
		try
		{
			topStaticMenuThirdLevel = MenuAndBannersDAO.fourthLevelFromParent;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return topStaticMenuThirdLevel;
	}
}
