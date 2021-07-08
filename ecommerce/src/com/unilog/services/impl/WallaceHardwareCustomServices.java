package com.unilog.services.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralPriceBreaks;
import com.erp.service.model.SalesOrderManagementModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.ProductsModel;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class WallaceHardwareCustomServices implements UnilogFactoryInterface {
	private static UnilogFactoryInterface serviceProvider;
	private WallaceHardwareCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (WallaceHardwareCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new WallaceHardwareCustomServices();
				}
			}
		return serviceProvider;
	}
	
	@Override
	public void insertCustomFields(String brabchid,int userid,int buyingCompanyid,UsersModel userDetailsInput)
	{	
		String newsLetterCustomFiledValue="";
		if(userDetailsInput.getNewsLetterSub()!=null && userDetailsInput.getNewsLetterSub().equals("Y")){
			newsLetterCustomFiledValue="Y";
		}else{
			newsLetterCustomFiledValue="N";
		}
		UsersDAO.insertCustomField(CommonUtility.validateString(newsLetterCustomFiledValue),"NEWSLETTER",userid, buyingCompanyid,"USER");
	}
		
	public void getPrice(Cimm2BCentralItem item,ProductsModel product) {
		double price=0.0;
		if(item!=null && item.getPricingWarehouse()!=null && item.getPricingWarehouse().getCustomerPrice()!=null) {
			if(item.getPricingWarehouse().getCustomerPrice() > 0) {
				price=CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(item.getPricingWarehouse().getCustomerPrice()));
			}
			product.setPrice(price);
		}
	}	
	
	public void getUnitPrice(ProductsModel wallaceItemPrice,ProductsModel itemPrice) {
		if(wallaceItemPrice.getCimm2BCentralPricingWarehouse()!=null && CommonUtility.validateParseDoubleToString(wallaceItemPrice.getCimm2BCentralPricingWarehouse().getCustomerPrice())!=null && CommonUtility.validateParseDoubleToString(wallaceItemPrice.getCimm2BCentralPricingWarehouse().getCustomerPrice()).length()>0)
		{
			itemPrice.setCustomerPrice(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(wallaceItemPrice.getCimm2BCentralPricingWarehouse().getCustomerPrice())));
			itemPrice.setExtendedPrice(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(wallaceItemPrice.getCimm2BCentralPricingWarehouse().getExtendedPrice())));
			if(wallaceItemPrice.getPrice()<wallaceItemPrice.getCimm2BCentralPricingWarehouse().getCustomerPrice()) {
				itemPrice.setUnitPrice(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(wallaceItemPrice.getPrice())));
				wallaceItemPrice.setPrice(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(wallaceItemPrice.getPrice())));
			}else {
				itemPrice.setUnitPrice(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(wallaceItemPrice.getCimm2BCentralPricingWarehouse().getCustomerPrice())));
				wallaceItemPrice.setPrice(CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(wallaceItemPrice.getCimm2BCentralPricingWarehouse().getCustomerPrice())));
		}}
	}
	
	private void setMaxQtyBreak(List<ProductsModel> qtyBreaks) {
		int noOfBreaks = qtyBreaks.size();
		for(int i = 0; i < qtyBreaks.size(); i++) {
			ProductsModel eachBreak = qtyBreaks.get(i);
			if((i + 1) < noOfBreaks) {
				eachBreak.setMaximumQuantityBreak(qtyBreaks.get(i+1).getMinimumQuantityBreak() - 1);
			}
		}
	}
	@Override
	public void erpQuanitityBreakUpdate(ProductsModel productsModel, ProductsModel pricingResponse) {
		if(pricingResponse.getQuantityBreakList()!=null && pricingResponse.getQuantityBreakList().size()>0) {
		setMaxQtyBreak(pricingResponse.getQuantityBreakList());
		Optional<ProductsModel> quantityBreakPrice = pricingResponse.getQuantityBreakList().stream()
				.filter(qb -> quantityBreakRange(productsModel.getQty(), qb))
				.findFirst();
		if (quantityBreakPrice.isPresent()) {
			double price = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(quantityBreakPrice.get().getCustomerPriceBreak()));
			productsModel.setPrice(price);
			pricingResponse.setPrice(price);
		}else {
			double price = CommonUtility.roundHalfUp(CommonUtility.validateParseDoubleToString(pricingResponse.getCimm2BCentralPricingWarehouse().getCustomerPrice()));
			productsModel.setPrice(price);
			pricingResponse.setPrice(price);
		}
		}
	}
	
	private boolean quantityBreakRange(int qty, ProductsModel quantityBreak) {
		boolean status = false;
		if (quantityBreak.getMaximumQuantityBreak() > 0) {
            if (qty >= quantityBreak.getMinimumQuantityBreak() && qty <= quantityBreak.getMaximumQuantityBreak()) {
            	status = true;
            }
        } else {
            if (qty >= quantityBreak.getMinimumQuantityBreak()) {
            	status = true;
            }
        }
		return status;
	}
	
	@Override
	public int setBillAddressToShippAddress(Connection conn, UsersModel customerinfo, int shipId) {
		String entityid="0";
		entityid = customerinfo.getEntityId();
		customerinfo.setEntityId(null);
		shipId = UsersDAO.insertNewAddressintoBCAddressBook(conn,customerinfo,"Ship",false);
		customerinfo.setEntityId(entityid);
		return shipId;
	}
	
	@Override
	public void setBackOrdersFlag(Cimm2BCentralOrder orderRequest, SalesOrderManagementModel salesOrderInput) {
		if(salesOrderInput.getAcceptBackorders()!=null) {
			orderRequest.setAcceptBackorders(salesOrderInput.getAcceptBackorders());
		}
	}
	
	@Override
	public String addContextObject(SalesModel orderDetail, VelocityContext context) {
		context.put("acceptBackorders", orderDetail.getAcceptBackorders()!=null?orderDetail.getAcceptBackorders():"");
		return "Success" ;
	}
	
	@Override
	public void setBackOrderTypeToLineItems(Cimm2BCentralLineItem lineItem, ProductsModel cartItem, SalesOrderManagementModel salesOrderInput) {
		String acceptBackorders = "true";
		if(CommonUtility.validateString(cartItem.getAdditionalProperties()).equalsIgnoreCase("Y") ) {
			salesOrderInput.setAcceptBackorders(acceptBackorders);
		}
		lineItem.setBackorderType(cartItem.getAdditionalProperties());
	}
	
	@Override
	public String setFirstLoginTrueForInactiveUser() {
		return "Y";
	}
	
	@Override
	public void assignEntityIdForShip(UsersModel usersModel, HttpSession session) {
		String userToken = (String) session.getAttribute("userToken");
		if(CommonUtility.validateString(usersModel.getEntityId()).length()==0) {
			usersModel.setEntityId(userToken);
		}		
	}
	
	@Override
	public List<CustomTable> getPromotional_Image(){
		List<CustomTable> PromotionalTable  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","PROMOTIONAL_IMAGES");
		return PromotionalTable;
	}
	
	@Override
	public void setCustomSalesOrderInputValues(SalesOrderManagementModel salesOrderInput, HttpServletRequest request) {
		salesOrderInput.setOrderType(CommonUtility.validateString(request.getParameter("orderType")));
	}
	
	public String  setOverRideCatId(String overRideCatId){		
		return overRideCatId = CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("OVERRIDE_CAT_ID"));
	}
}
