package com.unilog.rockwell;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;

import com.erp.service.ProductManagement;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.RockwellPriceResponse;
import com.erp.service.impl.ProductManagementImpl;
import com.erp.service.model.ProductManagementModel;
import com.erp.service.rest.util.HttpConnectionUtility;
import com.google.gson.Gson;
import com.unilog.cimmesb.client.request.HttpMethod;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.Global;
import com.unilog.exception.RestServiceException;
import com.unilog.products.ProductsDAO;
import com.unilog.products.ProductsModel;
import com.unilog.rockwell.model.BomModel;
import com.unilog.rockwell.model.BomResponse;
import com.unilog.utility.CommonUtility;

public class RockwellUtility {

	private static RockwellUtility rockwellUtility = null;
	Gson gson = new Gson();
	HttpResponse response;

	
	public static RockwellUtility getInstance(){
		synchronized (RockwellUtility.class) {
			if(rockwellUtility==null){
				rockwellUtility = new RockwellUtility();
			}
		}
		return rockwellUtility;
	}
	
	
	public String getJsonResponse(String requestUrl, String requestType, Object requestParameters) throws RestServiceException, IOException{
		String result = "";


		try {
			System.out.println("Request url : " + requestUrl);
			HttpConnectionUtility httpConnectionUtility = HttpConnectionUtility.getInstance();

			List<NameValuePair> headerList = new ArrayList<NameValuePair>();
			headerList.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
			
			StringEntity stringEntity=null;
			if (requestParameters != null && (requestType.equalsIgnoreCase(HttpMethod.POST.name()) || (requestType.equalsIgnoreCase(HttpMethod.PUT.name())) || (requestType.equalsIgnoreCase(HttpMethod.DELETE.name())))) {

				stringEntity = new StringEntity(gson.toJson(requestParameters), "UTF-8");
				System.out.println("RequestEntityParameters --- " + gson.toJson(requestParameters));
			}
			
			//response = httpConnectionUtility.getApiResponse(requestUrl, requestType, headers, requestParameters);
			
			result = httpConnectionUtility.getApiResponse(requestUrl, requestType, headerList, stringEntity);

			/*int responseCode = response.getStatusLine().getStatusCode();
			result = EntityUtils.toString(response.getEntity()).trim();
			System.out.println("Response Code : " + responseCode);*/
			System.out.println("Response for '" + requestUrl + "' --- "+ result);
		}
		catch (Exception e) {
			throw new RestServiceException(e.getMessage());
		}

		return result;
	}
	
	public String processAddtoCart(BomResponse bomResponse,String subsetId,String sessionId,int userId){
		Connection conn = null;
		ProductsModel product = null;
		try{
			 conn = ConnectionManager.getDBConnection();
			for(BomModel productList:bomResponse.getBOM()){
				if(productList.getQty()==0){
					productList.setQty(1);
				}
				
				LinkedHashMap<String, Object> utilityMap = new LinkedHashMap<String, Object>();
				utilityMap.put("considerLineItemComment", false);
				product = RockwellDao.checkItemExistInCIMM(conn, productList.getProduct(), subsetId);
				
				// Check item present in Cart
				ArrayList<Integer> cartId = new ArrayList<Integer>();
				boolean isItemPresentInCart=false;
				String partNumber=null;
				double sxeItemPrice=0.0;
				
				if(product == null){
					partNumber= productList.getProduct().replaceAll("\\W", "");
					
					ArrayList<ProductsModel> productListData = new ArrayList<ProductsModel>();
					ProductsModel productModel=new ProductsModel();
					productModel.setPartNumber(partNumber);
					productListData.add(productModel);
					ArrayList<String> partIdentifier = new ArrayList<String>();
					partIdentifier.add(partNumber);
					ArrayList<Integer> partIdentifierQuantity = new ArrayList<Integer>();
					partIdentifierQuantity.add(1);
					
					HttpServletRequest request =ServletActionContext.getRequest();
					HttpSession session = request.getSession(); 
					
					
					if(productListData!=null && productListData.size()>0  && CommonDBQuery.getSystemParamtersList().get("ERP")!=null && !CommonDBQuery.getSystemParamtersList().get("ERP").trim().equalsIgnoreCase("defaults"))
					{
						ProductManagement priceInquiry = new ProductManagementImpl();
						ProductManagementModel priceInquiryInput = new ProductManagementModel();
						String entityId = (String) session.getAttribute("entityId");
						priceInquiryInput.setEntityId(CommonUtility.validateString(entityId));
						priceInquiryInput.setHomeTerritory((String) session.getAttribute("shipBranchId"));
						priceInquiryInput.setPartIdentifier(productListData);
						priceInquiryInput.setPartIdentifierQuantity(partIdentifierQuantity);
						priceInquiryInput.setRequiredAvailabilty("Y");
						priceInquiryInput.setUserName(Global.USERNAME_KEY);
						priceInquiryInput.setUserToken((String)session.getAttribute("userToken"));
						priceInquiryInput.setSession(session);
						productListData = priceInquiry.priceInquiry(priceInquiryInput , productListData);
						if(productListData.size() >0){
						sxeItemPrice=productListData.get(0).getPrice();
						}
							
					}
					
				}

				
				cartId=RockwellDao.checkItemExistInCart(conn, partNumber,userId,sessionId);
				if(cartId!=null && cartId.size()>0)
				{
					isItemPresentInCart=true;
					ProductsDAO.updateCart(userId,  cartId.get(0), productList.getQty(), cartId.get(1),"",null,utilityMap);
				}
				if(!isItemPresentInCart){
					if(product==null){
						
						if(sxeItemPrice > 0){
							productList.setListPrice(sxeItemPrice);
						}else{
						// Get price from RockWell via CIMM2BCentral
						String ROCKWELL_PRICE_URL=CommonDBQuery.getSystemParamtersList().get("ROCKWELL_PRICE_URL")+productList.getProduct();
						Cimm2BCentralResponseEntity responseObject = Cimm2BCentralClient.getInstance().getDataObject(ROCKWELL_PRICE_URL, "GET", null,RockwellPriceResponse.class);
						RockwellPriceResponse priceResponse=(RockwellPriceResponse) responseObject.getData() ;
						if(priceResponse != null){
							productList.setListPrice(priceResponse.getListPrice());
						}
					}
						RockwellDao.insertNonCatalogItemToCart(conn, productList, sessionId,userId);
					}else{
							ProductsDAO.insertItemToCart(userId, product.getItemId(), productList.getQty(), sessionId,null,null,null,Double.toString(sxeItemPrice),1,0.0,0.0,utilityMap);
					}
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBConnection(conn);
		}
		return null;
	}
	

}
