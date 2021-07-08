package com.unilog.custommodule.utility;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.database.CommonDBQuery;
import com.unilog.misc.YahooBossSupport;
import com.unilog.products.ProductsModel;
import com.unilog.users.UsersModel;

public class WillCall {
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public String getWillCallHTML(ProductsModel itemInfo,UsersModel userInfo)
	{
		
		String htmlRes = "";
		Class<?>[] paramObject = new Class[2];
		paramObject[0] = ProductsModel.class;
		paramObject[1] = UsersModel.class;
		
		Object[] arguments = new Object[2];
		arguments[0] = itemInfo;
		arguments[1] = userInfo;
		
		try
		{
			String version = "V1";
			if(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_VERSION")!=null){
					version = CommonDBQuery.getSystemParamtersList().get("WILL_CALL_VERSION");
			}
			Class<?> cls = Class.forName("com.unilog.custommodule.utility.WillCall");
			Object obj = cls.newInstance();
			Method method = cls.getDeclaredMethod("getWillCallHTML"+version, paramObject);
			htmlRes = (String) method.invoke(obj, arguments);
			
		}
			
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return htmlRes;
	}
	public String getWillCallHTMLV1(ProductsModel itemInfo,UsersModel userDetail)
	{
		String htmlContent = "";
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String userLati="";
		String userLongi="";
		//String shipBranchId = (String)session.getAttribute("shipBranchId");
		//String BranchAvailValues="";
		//String branchIdValue="";
		/*String selectedBranch = (String)session.getAttribute("selectedBranchWillCall");
		if(selectedBranch!=null && !selectedBranch.trim().equalsIgnoreCase("")){
			if(selectedBranch.contains("Will Call") || selectedBranch.contains("Pickup")){
				String[] selectArra = selectedBranch.split("\\|");
				branchIdValue = selectArra[2]; // Removed due to Wil call removal
			}
		}*/
		String zipCode = "";
		if(userDetail.getZipCode()==null){
			//zipCode = (String)session.getAttribute("homeBranchZipCode");
			//userLati = (String)session.getAttribute("homeBranchLati");
			//userLongi = (String)session.getAttribute("homeBranchLongi");
		}else{
			if(CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT")!=null && CommonDBQuery.getSystemParamtersList().get("WILL_CALL_SUPPORT").trim().equalsIgnoreCase("Y")){
				YahooBossSupport getUserLocation = new YahooBossSupport();
				UsersModel locationDet = new UsersModel();
				locationDet.setZipCode(zipCode);
				locationDet = getUserLocation.locateUser(locationDet);
			    userLati  = locationDet.getLatitude();
			    userLongi = locationDet.getLongitude();
			    session.setAttribute("homeBranchLati", userLati);
			    session.setAttribute("homeBranchLongi", userLongi);
			    session.setAttribute("homeBranchZipCode", zipCode);
			    zipCode = zipCode+"|City: "+locationDet.getCity()+ "|State: "+locationDet.getState();
			    session.setAttribute("homeBranchDisplay", zipCode);
			}
		    
		}
		
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		contentObject.put("responseType","GeoLocationBranch");
		htmlContent = LayoutGenerator.templateLoader("AjaxResultPage", contentObject , null, null, null);
		return htmlContent;
	}
	
}
