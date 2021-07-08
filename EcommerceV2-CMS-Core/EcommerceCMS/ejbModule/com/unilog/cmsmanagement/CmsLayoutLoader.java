package com.unilog.cmsmanagement;

import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.unilog.VelocityTemplateEngine.TemplateModel;
import com.unilog.model.CmsLayoutModel;

public class CmsLayoutLoader {

	private static CmsLayoutModel layoutModel;

	public static void setLayoutModel(CmsLayoutModel layoutModel) {
		CmsLayoutLoader.layoutModel = layoutModel;
	}

	public static CmsLayoutModel getLayoutModel() {
		return layoutModel;
	}
	
	static{
		generateLayout();
	}

	private static void generateLayout() {
		
		layoutModel = new CmsLayoutModel();
		LinkedHashMap<String, TemplateModel> layoutList = new LinkedHashMap<String, TemplateModel>();
		Gson gson = new Gson();
		   TemplateModel template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmsstaticpagelist.html\"}", TemplateModel.class);
		   layoutList.put("CMSStaticPageList", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS_W_HEADER\",\"BodyContent\":\"cmsstaticpage.html\"}", TemplateModel.class);
		   layoutList.put("CMSStaticPage", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmsbannerlist.html\"}", TemplateModel.class);
		   layoutList.put("CMSBannerList", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmsbanneredit.html\"}", TemplateModel.class);
		   layoutList.put("CMSBannerEdit", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_Layout3\",\"BodyContent\":\"cmsbannerimagelist.html\"}", TemplateModel.class);
		   layoutList.put("CMSBannerImageList", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmsbannerupload.html\"}", TemplateModel.class);
		   layoutList.put("CMSBannerUpload", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmswidgetlist.html\"}", TemplateModel.class);
		   layoutList.put("CMSWidgetList", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmswidgetaddedit.html\"}", TemplateModel.class);
		   layoutList.put("CMSWidgetEdit", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmswidgettype.html\"}", TemplateModel.class);
		   layoutList.put("CMSWidgetType", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_Layout3\",\"BodyContent\":\"cmswidgetlistajax.html\"}", TemplateModel.class);
		   layoutList.put("CMSWidgetListAjax", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmsformaddedit.html\"}", TemplateModel.class);
		   layoutList.put("CMSFormEdit", template);
		   
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmsformlist.html\"}", TemplateModel.class);
		   layoutList.put("CMSFormList", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_Layout3\",\"BodyContent\":\"cmsformlistajax.html\"}", TemplateModel.class);
		   layoutList.put("CMSFormListAjax", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmslibrarylist.html\"}", TemplateModel.class);
		   layoutList.put("CMSLibraryList", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_Layout3\",\"BodyContent\":\"cmslibrarylistajax.html\"}", TemplateModel.class);
		   layoutList.put("CMSLibraryListAjax", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_Layout3\",\"BodyContent\":\"cmsResultLoader.html\"}", TemplateModel.class);
		   layoutList.put("CMSResultLoader", template);
		   
		   template = gson.fromJson("{\"LayoutName\":\"Layout_CMS\",\"BodyContent\":\"cmspagehistory.html\"}", TemplateModel.class);
		   layoutList.put("CMSPageHistory", template);
		   
		   layoutModel.setLayoutList(layoutList);
	}
	
}
