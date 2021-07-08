package com.unilog.cmsmanagementdaoSQL;

public class WebLiveDaoSQL {
	
	public static String getStaticPageInfo() {
		return "SELECT STATIC_PAGE_ID, PAGE_NAME, PAGE_TITLE, PAGE_CONTENT FROM STATIC_PAGES WHERE  NVL(STATUS,-1)!='D' AND SITE_ID=?";//AND USER_EDITED=? not required
	}
	
	public static String getUpdatePageContent()
	{
		return "UPDATE STATIC_PAGES SET PAGE_CONTENT=REPLACE(PAGE_CONTENT,?,'') WHERE PAGE_CONTENT LIKE CONCAT(CONCAT('%',?),'%') AND NVL(STATUS,-1)!='D' AND SITE_ID=? ";//AND USER_EDITED=?
	}

}