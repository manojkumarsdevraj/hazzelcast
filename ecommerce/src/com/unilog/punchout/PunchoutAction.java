package com.unilog.punchout;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.punchout.model.PunchoutModel;
import com.unilog.utility.CommonUtility;

public class PunchoutAction extends ActionSupport {

	private static final long serialVersionUID = -1638116486879434335L;
	protected String target = ERROR;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String renderContent;
	private String punchoutType;
	private String procurementSystem;
	private String shareScret;
	private String networkId;
	private String result; 
	private String username;
	private String password;
	private int configId;

	public String getResult() {
	return result;
}
public void setResult(String result) {
	this.result = result;
}
	public String getPunchoutType() {
		return punchoutType;
	}
	public void setPunchoutType(String punchoutType) {
		this.punchoutType = punchoutType;
	}
	public String getProcurementSystem() {
		return procurementSystem;
	}
	public void setProcurementSystem(String procurementSystem) {
		this.procurementSystem = procurementSystem;
	}
	public String getShareScret() {
		return shareScret;
	}
	public void setShareScret(String shareScret) {
		this.shareScret = shareScret;
	}
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
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
	public String getRenderContent() {
		return renderContent;
	}
	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String configure(){
		target = SUCCESS;
		request =ServletActionContext.getRequest();
        HttpSession session = request.getSession(); 
        String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
        int userId = CommonUtility.validateNumber(sessionUserId);
		if(userId>1){
			PunchoutDAO punchoutDAO=new PunchoutDAO();
	        String customerNumber = (String) session.getAttribute("buyingCompanyId");
	        int customerNo=CommonUtility.validateNumber(customerNumber);
	        System.out.println(customerNo);
	        try{
		        PunchoutModel punchoutModel=punchoutDAO.getPunchoutConfiguration(customerNo);
		        LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
		        if(punchoutModel==null){
		        	punchoutModel = new PunchoutModel();
		        }
		  
		        contentObject.put("punchoutConfig", punchoutModel);
		        renderContent = LayoutGenerator.templateLoader("PunchoutConfig", contentObject , null, null, null);
	        } catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			target = "SESSIONEXPIRED";
		}
        
       
		return target;
	}
	
	public String saveConfigure(){
		target = SUCCESS;
		request =ServletActionContext.getRequest();       
        HttpSession session = request.getSession(); 
        String customerNumber = (String) session.getAttribute("buyingCompanyId");
        int customerNo=CommonUtility.validateNumber(customerNumber);
        String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
        int userId = CommonUtility.validateNumber(sessionUserId);
        String username=request.getParameter("username");
        String password=request.getParameter("password");    
        String ssiteId=CommonDBQuery.getSystemParamtersList().get("SITE_ID");
        int siteId=CommonUtility.validateNumber(ssiteId);
        String punchoutTypeSelected=request.getParameter("punchoutType");
        String procurementSystem=request.getParameter("procurementSystem");
        String networkId=request.getParameter("networkId");
     //   System.out.println("uname: "+username+"\npwd: "+password+"\ncustmerNo: "+customerNo+"\nsiteId "+siteId+"\npunchoutType"+punchoutTypeSelected+"\nuserId: "+userId);
        PunchoutDAO pDAO = new PunchoutDAO();
        int i=0;
        /*if(configId > 0){
        	
        }else{
        	
        }*/
      i=pDAO.insertUpdate(username, password,siteId,customerNo,punchoutTypeSelected,procurementSystem,networkId,userId,configId);
      if(i>0){
   	 result= "1|Updated Successfully";
    	 
      }
      else{
    	  result= "0|Update Not Successfully";
      }
    //  renderContent=result; 
      renderContent = LayoutGenerator.templateLoader("PunchoutConfig", null , null, null, null);
		return target;
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
}
