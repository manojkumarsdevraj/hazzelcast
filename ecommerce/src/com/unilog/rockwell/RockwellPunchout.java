package com.unilog.rockwell;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.rockwell.model.BomModel;
import com.unilog.rockwell.model.BomResponse;
import com.unilog.rockwell.model.ConfiguratorResponse;
import com.unilog.rockwell.model.ProductModel;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;

public class RockwellPunchout extends ActionSupport
{
	  private static final long serialVersionUID = 6592270966760417537L;
	  private String renderContent;
	  private HttpServletRequest request;
	  private HttpServletResponse response;
	  private String cid;
	  Gson gson =new Gson();
	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	public String getRenderContent() {
		return renderContent;
	}
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
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	public String punchoutFrame()
	  {
	    LinkedHashMap<String,Object> contentObject = new LinkedHashMap<String,Object>();
	    contentObject.put("catalogUrl", CommonDBQuery.getSystemParamtersList().get("ROCKWELL_CATALOG_URL"));
	    this.renderContent = LayoutGenerator.templateLoader("PunchoutFrame", contentObject, null, null, null);
	    return "success";
	  }
	
	public String addtocart(){
		try{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String productURL=CommonDBQuery.getSystemParamtersList().get("ROCKWELL_PRODUCT_URL");
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String output = RockwellUtility.getInstance().getJsonResponse(productURL+cid+"/sbom", "GET", null);
			ConfiguratorResponse configuratorResponse = gson.fromJson(output, ConfiguratorResponse.class);
			BomResponse bomResponse = gson.fromJson(output, BomResponse.class);
			RockwellUtility.getInstance().processAddtoCart(bomResponse, tempSubset, session.getId(), userId);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "addtocart";
	}
}
