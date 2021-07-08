package com.unilog.products;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.erp.service.cimm2bcentral.action.ProductManagementAction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.unilog.VelocityTemplateEngine.LayoutGenerator;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.utility.CommonUtility;

public class VMICart extends ActionSupport {
	private static final long serialVersionUID = -2167590852259453564L;
	static final Logger logger = LoggerFactory.getLogger(VMICart.class);
	protected String target = ERROR;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String sortBy;
	private String renderContent;
		
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getRenderContent() {
		return renderContent;
	}

	public void setRenderContent(String renderContent) {
		this.renderContent = renderContent;
	}

	public String inventoryItems(){
		 request = ServletActionContext.getRequest();
		 HttpSession session = request.getSession();
		 LinkedHashMap<String, Object> contentObject =  new LinkedHashMap<String, Object>();
		 try {
			 if((sortBy==null || CommonUtility.validateString(sortBy).length()<1) && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DEFAULT_SHOPPING_CART_SORT_BY")).equalsIgnoreCase("PART_NUMBER")) {
				 sortBy = "partNumber";
			 }
			 if(sortBy!=null && sortBy.trim().length()>0) {
				 session.setAttribute("sortBy",sortBy );
				 contentObject.put("sortBy", sortBy);
			 }
			 contentObject = VMICartDAO.getVMICartDao(session, contentObject);
			 renderContent = LayoutGenerator.templateLoader("VMIItemsPage", contentObject , null, null, null);
		 }catch(Exception e) {
			 logger.error(e.getMessage());
		 }		 
		 return SUCCESS;
	 }
	 
	public String addToCart() {
		target = "ResultLoader";
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();		
		String productListIdStr = request.getParameter("productListIdStr");
		int productListId = 0;
		if(CommonUtility.validateString(productListIdStr).length() > 0) {
			productListId = CommonUtility.validateNumber(productListIdStr);
		}
		ArrayList<ProductsModel> itemDetalsFromSOLR = null;
		try {
			String ipAddress = "";
			ipAddress = request.getHeader("X-Forwarded-For");
			if(ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int subsetId = CommonUtility.validateNumber(tempSubset);			 
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);			
			String customerNumber = (String) session.getAttribute("customerId");
			int userId = CommonUtility.validateNumber(sessionUserId);
			int customerId = CommonUtility.validateNumber(customerNumber);
			String searchBySolrFieldName = null;
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("VMI_SEARCH_FIELD")).length()>0){
				searchBySolrFieldName = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("VMI_SEARCH_FIELD"));
			}else{
				searchBySolrFieldName = "partnumber";
			}
			LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();
			int quantity=0, thresholdQty=0, replenishToQty=0;			
			String qty = request.getParameter("qty");
			if(qty.trim().length()>0) {
				quantity = CommonUtility.validateNumber(qty);
			}
			String partNumber = request.getParameter("partNumber");
			String itemPartNumber = "\""+partNumber+"\"";
			thresholdQty = CommonUtility.validateNumber(request.getParameter("thresholdQty"));
			replenishToQty = CommonUtility.validateNumber(request.getParameter("replenishToQty"));
			itemDetalsFromSOLR = new ArrayList<ProductsModel>();
			
			itemDetalsFromSOLR = ProductHunterSolr.getItemDetailsForGivenPartNumbers(subsetId, generalSubset, itemPartNumber, 0, null, searchBySolrFieldName);
			
			if(itemDetalsFromSOLR!=null && itemDetalsFromSOLR.size()>0){
				for(ProductsModel itemListSolr : itemDetalsFromSOLR){
					if(CommonUtility.validateString(partNumber).equalsIgnoreCase(CommonUtility.validateString(itemListSolr.getPartNumber()))){
						ArrayList<Integer> cartId = new ArrayList<Integer>();
						cartId = VMICartDAO.selectFromVMICart(userId, customerId, partNumber);
						if(cartId != null && cartId.size() > 0) {
							VMICartDAO.updateItemsInVMICart(productListId, userId, customerId, partNumber, quantity, thresholdQty, replenishToQty, itemListSolr, session);							
						}else {
							VMICartDAO.insertItemsToVMICart(userId, customerId, partNumber, quantity, thresholdQty, replenishToQty, itemListSolr, session);
						}
					}	
				}
				contentObject = VMICartDAO.getVMICartDao(session, contentObject);
				renderContent = LayoutGenerator.templateLoader("VMIItemsPage", contentObject , null, null, null);
			}else {
				renderContent = "Item Not Found";
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return target;
	}
	
	public String submitCart() {
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String customerNumber = (String) session.getAttribute("customerId");
		int customerId = CommonUtility.validateNumber(customerNumber);
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<String, Object>();		
		String jsonString = request.getParameter("jsonString");		
		try {
			logger.info("JsonString of submitted VMICart items = {} ", jsonString);
			if(CommonUtility.validateString(jsonString).length() > 0) {
				Gson parseInput = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				ProductsModel parsedData = parseInput.fromJson(jsonString, ProductsModel.class);
				ArrayList<ProductsModel> vmiCartRequestList = parsedData.getItemDataList();
				if(vmiCartRequestList != null && vmiCartRequestList.size() > 0) {
					for(ProductsModel vmiCartModel : vmiCartRequestList) {
						VMICartDAO.updateVMICart(userId, customerId, session, vmiCartModel);
					}
				}				
			}
			contentObject = VMICartDAO.getVMIOrderedItemsList(session, contentObject);
			renderContent = LayoutGenerator.templateLoader("VMIOrderedItemsListPage", contentObject , null, null, null);			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}		
		return SUCCESS;
	}
	
	public String deleteCart() {
		target = "ResultLoader";
		int count = 0;
		try	{
			request =ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
			int userId = CommonUtility.validateNumber(sessionUserId);
			String customerIdStr = (String) session.getAttribute("customerId");
			int customerId = CommonUtility.validateNumber(customerIdStr);
			String VMICartListId = (String) request.getParameter("VMICartListId");
			int productListId = CommonUtility.validateNumber(VMICartListId);			
			count = VMICartDAO.deleteFromVMICart(customerId, userId, productListId, session);
		}catch(Exception e)	{
			logger.error(e.getMessage());
		}
		if(count > 0) {
			renderContent = "Item deleted Successfully!";
		}else {
			renderContent = "Failed!";
		}		
		return target;
	}	
	
	public String resetQuantityInCart() {
		request =ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
		int userId = CommonUtility.validateNumber(sessionUserId);
		String customerNumber = (String) session.getAttribute("customerId");
		int customerId = CommonUtility.validateNumber(customerNumber);
		int count = 0;		
		String jsonString = request.getParameter("jsonString");		
		try {
			logger.info("JsonString of Clear Qty Product List items = {} ", jsonString);
			if(CommonUtility.validateString(jsonString).length() > 0) {
				Gson parseInput = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				ProductsModel parsedData = parseInput.fromJson(jsonString, ProductsModel.class);
				ArrayList<ProductsModel> vmiCartRequestList = parsedData.getItemDataList();
				if(vmiCartRequestList != null && vmiCartRequestList.size() > 0) {
					for(ProductsModel vmiCartModel : vmiCartRequestList) {
						count = VMICartDAO.resetQtyInVMICart(session, customerId, userId, vmiCartModel);
					}
				}
				if(count > 0) {
					renderContent = "VMI Items Quantity has been reset successfully!";
				}else {
					renderContent = "Failed!";
				}
			}			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return SUCCESS;
	}

}
