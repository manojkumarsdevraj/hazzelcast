package com.unilog.services.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralLineItem;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralOrder;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralWarehouse;
import com.erp.service.model.SalesOrderManagementModel;
import com.google.gson.Gson;
import com.unilog.custommodule.model.FreightCalculatorModel;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.defaults.CustomFieldModel;
import com.unilog.defaults.Global;
import com.unilog.defaults.SendMailModel;
import com.unilog.maplocation.LocationModel;
import com.unilog.products.ProductsModel;
import com.unilog.propertiesutility.PropertyAction;
import com.unilog.sales.SalesModel;
import com.unilog.services.UnilogFactoryInterface;
import com.unilog.users.ShipVia;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomTable;
import com.unilog.velocitytool.CIMM2VelocityTool;


public class AprCustomServices implements UnilogFactoryInterface{

	private static ArrayList<ShipVia> SiteOrderModeList = null;
	private HttpServletRequest request;
	private static Map<String,String> ShipviamapDetalis = new LinkedHashMap<>();
	private static Map<String,List<String>> SalesRepMapDetalis = null;
	private static Map<String,String>  SalesRepMapDetaliswarehouse = null;
	private static UnilogFactoryInterface serviceProvider;
	private static ArrayList<ShipVia> Sitepromotioanl = null;
	private static ArrayList<ShipVia> Sitebanner = null;
	private static List<CustomTable> sitePromotioanl = null;
	private static List<CustomTable> siteBanner = null;
	private static List<CustomTable> popUpsiteBanner = null;
	private AprCustomServices() {}
	
	public static UnilogFactoryInterface getInstance() {
			synchronized (AprCustomServices.class) {
				if(serviceProvider == null) {
					serviceProvider = new AprCustomServices();
				}
			}
		return serviceProvider;
	}
	
	public  List<CustomTable> getOrderModeList() {
		List<CustomTable> OrderModeTable  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","ORDER_MODE_DETAILS");
		return OrderModeTable; 
		
	}
	
	public Map<String,Double> updatediscount(double erpdiscount,double roediscount) {
		Map<String,Double> discountMap = new HashMap<String,Double>();
		if(erpdiscount != 0.0) {
			discountMap.put("erpdiscountval", erpdiscount);
		}if(roediscount != 0.0) {
			discountMap.put("erproediscountval", roediscount);
		}
		return discountMap;
	}
	
	public void getShipviaZipList() {
		List<CustomTable> ShipviaTable  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","ZIPCODE_LEVEL_SHIPVIA");
		Map<String,String> Shipviamap = new LinkedHashMap<>();
		if(ShipviaTable!=null && ShipviaTable.size()>0) {
		for(CustomTable shipvia:ShipviaTable){
			for(Map<String, String> each : shipvia.getTableDetails()){
				Shipviamap.put(each.get("ZIPCODE"), each.get("SHIPVIA"));
			}
		}
	}
		ShipviamapDetalis = Shipviamap;
		
}
	
	public String getShipviaDetail(String zipcode) {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		LinkedHashMap<String,ArrayList<CustomFieldModel>>wareHouseCustomFieldsList = null;
		ArrayList<CustomFieldModel> customlist = null ;
		String shipvia = null;
		String targetbranch = null;
		if(session.getAttribute("targetbranch").toString() !=null) {
			targetbranch = session.getAttribute("targetbranch").toString();
		}
		String warehouse_id = null;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
		
	    try{
			conn =  ConnectionManager.getDBConnection();
			String sql = "select WAREHOUSE_ID from warehouse where WAREHOUSE_CODE = ? ";
			pstmt = conn.prepareStatement(sql);
	    	pstmt.setString(1, targetbranch);
	    	rs = pstmt.executeQuery();
	    	while(rs.next()){
	    		warehouse_id = rs.getString("WAREHOUSE_ID");
	    	}
		}catch (SQLException e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}	
		
		if((String)session.getAttribute("OrderOption")!=null && (session.getAttribute("OrderOption").toString().contains("SHIP")|| session.getAttribute("OrderOption").toString().contains("BID"))) {
			shipvia = ShipviamapDetalis.get(zipcode);
		}else if((String)session.getAttribute("OrderOption")!=null && (session.getAttribute("OrderOption").toString().contains("DELIVER"))){
			wareHouseCustomFieldsList = CommonDBQuery.getWareHouseCustomFieldsList();
			customlist = wareHouseCustomFieldsList.get(warehouse_id);
			for (CustomFieldModel customFieldModel : customlist) {
				if(customFieldModel.getFieldName().equalsIgnoreCase("Deliver_ShipVia")) {
					shipvia = customFieldModel.getCustomFieldvalue();
					break;
				}	
			}
		}else if((String)session.getAttribute("OrderOption")!=null && (session.getAttribute("OrderOption").toString().contains("PICKUP"))){
			wareHouseCustomFieldsList = CommonDBQuery.getWareHouseCustomFieldsList();
			customlist = wareHouseCustomFieldsList.get(warehouse_id);
			if(customlist !=null) {
			for (CustomFieldModel customFieldModel : customlist) {
				if(customFieldModel.getFieldName().equalsIgnoreCase("PickUp_ShipVia")) {
					shipvia = customFieldModel.getCustomFieldvalue();
					break;
				}
			}
		 } 
		}
		return shipvia;
}
	
	public String getwareHouseDetail(String targetbranch) {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		LinkedHashMap<String,ArrayList<CustomFieldModel>>wareHouseCustomFieldsList = null;
		ArrayList<CustomFieldModel> customlist = null ;
		String selectedBranch = null;
		String warehouse_id = null;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
		
	    try{
			conn =  ConnectionManager.getDBConnection();
			String sql = "select WAREHOUSE_ID from warehouse where WAREHOUSE_CODE = ? ";
			pstmt = conn.prepareStatement(sql);
	    	pstmt.setString(1, targetbranch);
	    	rs = pstmt.executeQuery();
	    	while(rs.next()){
	    		warehouse_id = rs.getString("WAREHOUSE_ID");
	    	}
		}catch (SQLException e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}	
		
		if((String)session.getAttribute("OrderOption")!=null && (session.getAttribute("OrderOption").toString().contains("SHIP")|| session.getAttribute("OrderOption").toString().contains("BID"))) {
			wareHouseCustomFieldsList = CommonDBQuery.getWareHouseCustomFieldsList();
			customlist = wareHouseCustomFieldsList.get(warehouse_id);
			if(customlist !=null) {
			for (CustomFieldModel customFieldModel : customlist) {
				if(customFieldModel.getFieldName().equalsIgnoreCase("Default_Shipping_Branch")) {
					selectedBranch = customFieldModel.getCustomFieldvalue();
					break;
				}	
			}
		}
		}
		return selectedBranch;
}
	
	
	public String getwareHouseDetailforanonymous_rep(String targetbranch) {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		LinkedHashMap<String,ArrayList<CustomFieldModel>>wareHouseCustomFieldsList = null;
		ArrayList<CustomFieldModel> customlist = null ;
		String selectedBranch = null;
		String warehouse_id = null;
		Connection  conn = null;
	    PreparedStatement pstmt=null;
	    ResultSet rs = null;
		
	    try{
			conn =  ConnectionManager.getDBConnection();
			String sql = "select WAREHOUSE_ID from warehouse where WAREHOUSE_CODE = ? ";
			pstmt = conn.prepareStatement(sql);
	    	pstmt.setString(1, targetbranch);
	    	rs = pstmt.executeQuery();
	    	while(rs.next()){
	    		warehouse_id = rs.getString("WAREHOUSE_ID");
	    	}
		}catch (SQLException e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}	
		
		
			wareHouseCustomFieldsList = CommonDBQuery.getWareHouseCustomFieldsList();
			customlist = wareHouseCustomFieldsList.get(warehouse_id);
			if(customlist !=null) {
				for (CustomFieldModel customFieldModel : customlist) {
					if(customFieldModel.getFieldName().equalsIgnoreCase("SManID_Anonymous")) {
						selectedBranch = customFieldModel.getCustomFieldvalue();
						break;
					}	
				}	
			}
		return selectedBranch;
}
	
	public ProductsModel getwareHouseDetailsforordeoptions(String targetbranch) {
		request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		ProductsModel warehousedetails = null;
		
		if((String)session.getAttribute("OrderOption")!=null && (session.getAttribute("OrderOption").toString().contains("PICKUP")|| session.getAttribute("OrderOption").toString().contains("DELIVER"))) {
			LinkedHashMap<String,ProductsModel> list = CommonDBQuery.branchDetailData;
			
			 warehousedetails = list.get(targetbranch);
		}
		return warehousedetails;
}
	
	
	public void insertCustomertarget(String brabchid,int userid,int buyingCompanyid)
	{
		UsersDAO.insertCustomField(brabchid,"CUSTOMER_DEFAULT_TARGET_BRANCH",userid,buyingCompanyid,"BUYING_COMPANY");
	}
	
	public void sessionValue()
	{
		int count;
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String  branchid = request.getParameter("branchid");
			String  defaulttrgetchecked = request.getParameter("defaulttrgetchecked");
			LinkedHashMap<String, String> userCustomFieldValue = new LinkedHashMap<String, String>();
			String orderoption = (String)session.getAttribute("selected");
			
			if((defaulttrgetchecked !=null) && defaulttrgetchecked.equalsIgnoreCase("true") ){
			 if( branchid != "0" && branchid !=null )
			{
				count = UsersDAO.insertCustomField(branchid,"USER_DEFAULT_TARGET_BRANCH",Integer.parseInt((String) session.getAttribute(Global.USERID_KEY)),Integer.parseInt((String) session.getAttribute("contactId")),"USER");
				int userId = Integer.parseInt(((String)session.getAttribute(Global.USERID_KEY)));
				if(userId > 1 ){
					userCustomFieldValue = UsersDAO.getAllUserCustomFieldValue(userId);
					if(userCustomFieldValue!=null){
						session.setAttribute("userCustomFieldValue",userCustomFieldValue);
					}
				}
				session.setAttribute("checkedbranchid","0");
			}
			}else{
				if(((branchid !=null && branchid != "0" && orderoption!=null) && (!orderoption.toUpperCase().equalsIgnoreCase("SHIP TO MY ADDRESS")|| (!orderoption.toUpperCase().equalsIgnoreCase("BID ONLY"))))) {
					session.setAttribute("checkedbranchid",branchid);
				}else {
					session.setAttribute("checkedbranchid","0");
				}
		
			}
		}catch (Exception e) {
			e.printStackTrace();}
}
	public void getClusterdata()
	{
		Map<String,List<String>> clusterList= new LinkedHashMap<String, List<String>>();
		ArrayList<String> branch = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		Connection conn = null;
		String  branchid = null;
		String  userbranchid = null;
		String  custombranchid = null;
		
		try{
			conn = ConnectionManager.getDBConnection();
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			Map<String, String> userCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("userCustomFieldValue");
			Map<String, String> customerCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("customerCustomFieldValue");
			
			if((userCustomFieldValue!=null) && (request.getParameter("branchid") == null)){
				userbranchid = userCustomFieldValue.get("USER_DEFAULT_TARGET_BRANCH");
			} 
			if((customerCustomFieldValue!=null) && (userbranchid == null) && (request.getParameter("branchid") == null) ){
				custombranchid = customerCustomFieldValue.get("CUSTOMER_DEFAULT_TARGET_BRANCH");
			}
			if(request.getParameter("branchid") !=null)
			{
				branchid = request.getParameter("branchid");
			}
			
			if(userbranchid != null) {
				branchid = userbranchid;
			}else if(custombranchid !=null) {
				branchid = custombranchid;
			}/*else { //This Block is not required 
				branchid = branchid;
			}*/
			
			if(branchid != null) {
				pstmt =conn.prepareStatement(PropertyAction.SqlContainer.get("getClusterdata"));;
		    	pstmt.setString(1, branchid);
		    	rs = pstmt.executeQuery();
				while(rs.next()){
					branch.add(rs.getString("WAREHOUSE_CODE"));
				}
				clusterList.put("Cluster_"+branchid+"",branch);
				session.setAttribute("clusterdata", new Gson().toJson(clusterList));
			}else {
				session.setAttribute("clusterdata", "");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}		
	
}
	
	
	public Map<String,List<String>> getClusterdatabeforelogin()
	{
		Map<String,List<String>> clusterList= new LinkedHashMap<String, List<String>>();
		ArrayList<String> branch = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		Connection conn = null;
		String  branchid = null;
		String  userbranchid = null;
		String  custombranchid = null;
		
		try{
			conn = ConnectionManager.getDBConnection();
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			Map<String, String> userCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("userCustomFieldValue");
			Map<String, String> customerCustomFieldValue = (LinkedHashMap<String, String>) session.getAttribute("customerCustomFieldValue");
			
			if((userCustomFieldValue!=null) && (request.getParameter("branchid") == null)){
				userbranchid = userCustomFieldValue.get("USER_DEFAULT_TARGET_BRANCH");
			} 
			if((customerCustomFieldValue!=null) && (userbranchid == null) && (request.getParameter("branchid") == null) ){
				custombranchid = customerCustomFieldValue.get("CUSTOMER_DEFAULT_TARGET_BRANCH");
			}
			if(request.getParameter("branchid") !=null)
			{
				branchid = request.getParameter("branchid");
			}
			if(userbranchid != null) {
				branchid = userbranchid;
			}else if(custombranchid !=null) {
				branchid = custombranchid;
			}/*else {//This Block is not required
				branchid = branchid;
			}*/
			
			if(branchid != null) {
				pstmt =conn.prepareStatement(PropertyAction.SqlContainer.get("getClusterdata"));;
		    	pstmt.setString(1, branchid);
		    	rs = pstmt.executeQuery();
				while(rs.next()){
					branch.add(rs.getString("WAREHOUSE_CODE"));
				}
				clusterList.put("Cluster_"+branchid+"",branch);
				session.setAttribute("clusterdata", new Gson().toJson(clusterList));
			}else {
				session.setAttribute("clusterdata", "");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBConnection(conn);
		}		
	return clusterList;
}
	
	public ArrayList<LocationModel> loadServices(){
		ArrayList<LocationModel> services=new ArrayList<LocationModel>();
		String servicesInfo = PropertyAction.SqlContainer.get("getLocationServices");
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement preStat = null;

		try {
			conn = ConnectionManager.getDBConnection();
			preStat = conn.prepareStatement(servicesInfo);
			rs = preStat.executeQuery();
			while (rs.next()) {
				LocationModel fieldname=new LocationModel();
				fieldname.setServiceManager(rs.getString("FIELD_NAME"));
				services.add(fieldname);
			}
			}
		 catch (Exception e) {
				
				e.printStackTrace();
			}
			finally {
				ConnectionManager.closeDBPreparedStatement(preStat);
				ConnectionManager.closeDBConnection(conn);
			}
		return services;
	}
	
public void getSalesRepDetails() {
	List<CustomTable> SalesRepTable  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","SALES_REP_DETAILS");
	Map<String,List<String>> SalesrepMap = new LinkedHashMap();
	Map<String,String> SalesrepMapwarehouse = new LinkedHashMap();
	if(SalesRepTable!=null && SalesRepTable.size()>0) {
	for(CustomTable eachsalesrep:SalesRepTable){
		for(Map<String, String> each : eachsalesrep.getTableDetails()){
			List<String> sales = new ArrayList<String>();
			{
				sales.add(each.get("Email"));
				sales.add(each.get("Phone"));
				sales.add(each.get("Salesman Name"));
				sales.add(each.get("Primary Branch"));
				sales.add(each.get("SManID"));
				SalesrepMap.put(each.get("SManID"),sales);
				SalesrepMapwarehouse.put(each.get("SManID"), each.get("Email"));
				
			}
		}
	}

	}
	SalesRepMapDetalis = SalesrepMap;
	SalesRepMapDetaliswarehouse = SalesrepMapwarehouse;
}

public ArrayList<LocationModel> loadnearestwarehouses(ArrayList<LocationModel> locationList){
	ArrayList<LocationModel> locationnearestlist = new ArrayList<LocationModel>();
	for (LocationModel locationModel : locationList) {
		if(locationModel.getDistance() <= Integer.parseInt(CommonDBQuery.getSystemParamtersList().get("NEAREST_LOCATION_VALUE"))) {
			locationnearestlist.add(locationModel);
		}
	}
	return locationnearestlist;
}

public String  getSalesRepEmailDetail(String SManID) {
String SlaesRepDetails = SalesRepMapDetaliswarehouse.get(SManID);
return SlaesRepDetails;
}

public List<String>  getSalesRepDetailbywarehouse(String branch) {
List<String> SlaesRepDetails = SalesRepMapDetalis.get(branch);
return SlaesRepDetails;
}


public void removesessionvalues(HttpSession session) {
	session.removeAttribute("selected");
	session.removeAttribute("OrderOption");
	session.removeAttribute("checkedbranchid");
}
@Override
public void customizeOrderLineItem(Cimm2BCentralLineItem lineItem, ProductsModel cartItem) {
	lineItem.setListPrice(cartItem.getPrice());
}


@Override
public void mergeRequiredFields(ProductsModel item,ResultSet rs) throws SQLException {
	item.setDisplayPrice("N");
}

public double addtoCartCustomerPrice(ProductsModel itemPrice,ProductsModel eclipseitemPrice) {
	double price= 0;
	price	= price + eclipseitemPrice.getPrice();
	itemPrice.setUnitPrice(eclipseitemPrice.getPrice());
	return price;
}


public  List<CustomTable> getPromotional_Image() {
	List<CustomTable> PromotionalTable  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","PROMOTIONAL_IMAGES");
	return PromotionalTable;
	
}
public  List<CustomTable>  getDropDownBanner() {
	List<CustomTable> PromotioanlTable  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","Drop_Down_Promo_Banners");
	return PromotioanlTable;
	
}

public  List<CustomTable> getCART_POPUP_PROMO_IMAGE() {
	List<CustomTable> PopUp_PromotioanlTable  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","CART_POPUP_PROMO_IMAGE");
	return PopUp_PromotioanlTable;
	
}

public  List<CustomTable> getSales_Rep_Details() {
	List<CustomTable> accRepList  = CIMM2VelocityTool.getInstance().getCusomTableData("Website","SALES_REP_DETAILS");
	return accRepList;
	
}

public static ArrayList<ShipVia> getSiteOrderModeList() {
	return SiteOrderModeList;
}

public static void setSiteOrderModeList(ArrayList<ShipVia> siteOrderModeList) {
	SiteOrderModeList = siteOrderModeList;
}

public static ArrayList<ShipVia> getSitepromotioanl() {
	return Sitepromotioanl;
}

public static void setSitepromotioanl(ArrayList<ShipVia> sitepromotioanl) {
	Sitepromotioanl = sitepromotioanl;
}

public static void setSiteOrderModeList(List<CustomTable> siteOrderModeList) {
}

public static List<CustomTable> getSitePromotioanl() {
	return sitePromotioanl;
}

public static void setSitePromotioanl(List<CustomTable> sitePromotioanl) {
	AprCustomServices.sitePromotioanl = sitePromotioanl;
}

public static List<CustomTable> getPopUpsiteBanner() {
	return popUpsiteBanner;
}

public static void setPopUpsiteBanner(List<CustomTable> popUpsiteBanner) {
	AprCustomServices.popUpsiteBanner = popUpsiteBanner;
}

public static ArrayList<ShipVia> getSitebanner() {
	return Sitebanner;
}

public static void setSitebanner(ArrayList<ShipVia> sitebanner) {
	Sitebanner = sitebanner;
}

public static List<CustomTable> getSiteBanner() {
	return siteBanner;
}

public static void setSiteBanner(List<CustomTable> siteBanner) {
	AprCustomServices.siteBanner = siteBanner;
}



public SalesOrderManagementModel orderFreightcalculation(SalesOrderManagementModel salesOrderInput) {
	
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpSession session = request.getSession();
	double freightBaseAmount=0;
	double freightPercentage=0;
	double handlingBaseAmount=0;
	double handlingPercentage=0;
	double freight_Min_Threshold=0;
	double freight_Max_Threshold=0;
	String sessionUserId = (String) session.getAttribute(Global.USERID_KEY);
	 int userId = CommonUtility.validateNumber(sessionUserId);
	if(session.getAttribute("isRetailUser").toString().equals("Y") || CommonDBQuery.getSystemParamtersList().get("DEFAULT_CUSTOMER_ERP_ID").equals(userId)) {
		FreightCalculatorModel freightInput = new FreightCalculatorModel();
		List<Map<String, String>> customShipVias = null;
			  List<CustomTable> customValues = CIMM2VelocityTool.getInstance().getCusomTableData("Website","FRIGHT_TABLE");
			  if(customValues !=null && customValues.size() > 0){
			   customShipVias = customValues.get(0).getTableDetails();    
			  }
			 for(Map<String, String> tableValues :customShipVias){
				 freightBaseAmount=Double.parseDouble(tableValues.get("Frieght_Base_Amount"));
		         handlingBaseAmount=Double.parseDouble(tableValues.get("Handling_Base_Amount"));
		 		 handlingPercentage=Double.parseDouble(tableValues.get("Frieght_Percentage"));
		 		freight_Min_Threshold=Double.parseDouble(tableValues.get("Freight_Min_Threshold")); 
		 		freight_Max_Threshold=Double.parseDouble(tableValues.get("Freight_Max_Threshold")); 
				 
			     if(tableValues.get("SHIPVIACODE") != null)
			     { 
			    	 if(tableValues.get("SHIPVIACODE").equalsIgnoreCase(salesOrderInput.getShipVia())){
			    		 if((salesOrderInput.getOrderItems().get(0).getCartTotal() >= freight_Min_Threshold) && (salesOrderInput.getOrderItems().get(0).getCartTotal()  <= freight_Max_Threshold)) {
			    			 if(freightBaseAmount==0) {
			    				 double freight = ((salesOrderInput.getOrderItems().get(0).getCartTotal()*(handlingPercentage/100)));
			    				 salesOrderInput.setFrieghtCharges(String.valueOf(freight));
			    				 break;
			    			 }else {
			    				 salesOrderInput.setFrieghtCharges(String.valueOf(freightBaseAmount)); 
			    				 break;
			    			 }
			    		 }	
			    		 if(tableValues.get("SHIPVIACODE").equalsIgnoreCase("BW NEXTDAY")){
			    			 if((salesOrderInput.getOrderItems().get(0).getCartTotal() >= freight_Min_Threshold) && (salesOrderInput.getOrderItems().get(0).getCartTotal() <= freight_Max_Threshold)) {
				    			 if(freightBaseAmount==0) {
				    				 double freight = ((salesOrderInput.getOrderItems().get(0).getCartTotal()*(handlingPercentage/100)));
				    				 salesOrderInput.setFrieghtCharges(String.valueOf(freight)+ handlingBaseAmount);
				    			 }else {
				    				 salesOrderInput.setFrieghtCharges(String.valueOf(freightBaseAmount) + handlingBaseAmount); 
				    			 }
				    		 }	
			    		 }		 
			    }
			    	    		    	  		    	 
			  }
		}
	}	 
	return salesOrderInput;
	}


public String changeFromEmailaddress(SendMailModel sendMailModel) {
	return sendMailModel.getToEmailId();
}


public ArrayList<Cimm2BCentralLineItem> setlineItemComments(ArrayList<Cimm2BCentralLineItem> lineItems,
		SalesOrderManagementModel salesOrderInput){
		Cimm2BCentralLineItem cimm2bCentralLineItemnew = new Cimm2BCentralLineItem();
		cimm2bCentralLineItemnew.setLineItemComment(salesOrderInput.getOrderNotes());
		lineItems.add(cimm2bCentralLineItemnew);
		return lineItems;
	}

public  Cimm2BCentralOrder disableInteralNoteToERP(Cimm2BCentralOrder orderrequest){
	
	if(CommonUtility.validateString(orderrequest.getOrderComment()) != null){
		orderrequest.setOrderComment("");
	}
	return orderrequest;
	
}

 public Cimm2BCentralLineItem  disableUmqtyToErp(Cimm2BCentralLineItem cimm2bCentralLineItem){
		 cimm2bCentralLineItem.setUomQty("");
		 return cimm2bCentralLineItem;
}

}
