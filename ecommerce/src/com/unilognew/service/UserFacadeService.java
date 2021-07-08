package com.unilognew.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unilog.utility.CommonUtility;
import com.unilognew.db.service.UserDAOService;
import com.unilognew.erp.service.IUserERPService;
import com.unilognew.erp.service.provider.ERPServicesFactory;
import com.unilognew.exception.DBServiceException;
import com.unilognew.exception.ERPServiceException;
import com.unilognew.model.BuyingCompanyAddressBook;
import com.unilognew.model.Cimm2BCentralGetCustomerRequest;
import com.unilognew.model.ERPServiceRequest;
import com.unilognew.model.EclipseShipToAddressRequest;
import com.unilognew.model.SXV6191BillToAddressRequest;
import com.unilognew.model.SXV6191ShipToAddressRequest;
import com.unilognew.service.ShipToAddressSyncService.SyncServiceBuilder;
import com.unilognew.util.ECommerceEnumType.AddressType;
import com.unilognew.util.ECommerceEnumType.ErpType;

public class UserFacadeService{

	private Logger logger = LoggerFactory.getLogger(UserFacadeService.class);

	private static UserFacadeService userFacadeService;


	public final static String MAP_KEY_USER_ID="USER_ID";
	public final static String MAP_KEY_CUSTOMER_ID="CUSTOMER_ID";
	public final static String MAP_KEY_GENERAL_CATALOG_ACCESS="GENRAL_CATALOG_ACCESS";
	public final static String MAP_KEY_SUBSET_ID="SUBSET_ID";
	public final static String MAP_KEY_BUYING_COMPANY_ID="BUYING_COMPANY_ID";	
	public final static String MAP_KEY_WAREHOUSECODE="WAREHOUSECODE";
	public final static String MAP_KEY_WAREHOUSECODEID="WAREHOUSECODEID";
	public final static String MAP_KEY_CHILD_BUYING_COMPANY_CREATION="MAP_KEY_CHILD_BUYING_COMPANY_CREATION";
	public final static String MAP_KEY_SINGLE_SHIP_TO="MAP_KEY_SINGLE_SHIP_TO";
	private ExecutorService executorService;

	private UserFacadeService() {
		//
	}





	/**
	 * @return the executorService
	 */
	public ExecutorService getExecutorService() {
		return executorService;
	}



	/**
	 * @param executorService the executorService to set
	 */
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}






	public static UserFacadeService getInstance() {
		synchronized(UserFacadeService.class) {
			if(userFacadeService==null) {
				userFacadeService = new UserFacadeService();
			}
		}

		return userFacadeService;
	}

	public  List<BuyingCompanyAddressBook> getShipToAddressList(Map<String, Object> erpParameters,ErpType erpType) {
		IUserERPService iUserERPService  = null;
		Map<AddressType,List<BuyingCompanyAddressBook>> allAddressListMapFromERP = null;
		List<BuyingCompanyAddressBook> billToAddressListFromERP = null;
		List<BuyingCompanyAddressBook> shipToAddressListERP = null;
		List<BuyingCompanyAddressBook> shipToAddressListDB = null;
		List<BuyingCompanyAddressBook> consolidatedShipToAddressList = null;
		List<BuyingCompanyAddressBook> filteredShipToAddressList = null;
		BuyingCompanyAddressBook billToAddressDB=null;
		BuyingCompanyAddressBook billToAddressERP=null;


		iUserERPService = ERPServicesFactory.getUserERPService(erpType);
		ERPServiceRequest shipToAddressRequest = null;
		ERPServiceRequest billToAddressRequest = null;
		boolean dbExceptionOccured=false;
		boolean userShipToRecordsExist = false;


		String customerId;
		int buyingCompanyId; 
		int userId;
		String subsetId;
		String generalCatalogAccess;
		String warehouseCode;
		int warehouseCodeId;
		Boolean childBuyingCompanyCreation;
		Boolean singleShipTo;
		
		final String parameterMissingMsg = "is not available in the map";

		try {
			userId =  (Integer) erpParameters.get(MAP_KEY_USER_ID);
		} catch(Exception e) {
			throw new IllegalArgumentException("User id"+parameterMissingMsg);
		}
		try {
			buyingCompanyId =  (Integer) erpParameters.get(MAP_KEY_BUYING_COMPANY_ID);
		} catch(Exception e) {
			throw new IllegalArgumentException("Buying company id"+parameterMissingMsg);
		}

		try {
			subsetId =  (String) erpParameters.get(MAP_KEY_SUBSET_ID);
		} catch(Exception e) {
			throw new IllegalArgumentException("User subset id"+parameterMissingMsg);
		}

		try {
			generalCatalogAccess =  (String) erpParameters.get(MAP_KEY_GENERAL_CATALOG_ACCESS);
		} catch(Exception e) {
			throw new IllegalArgumentException("General catalog access flag"+parameterMissingMsg);
		}
		try {
			warehouseCode =  (String) erpParameters.get(MAP_KEY_WAREHOUSECODE);
		} catch(Exception e) {
			throw new IllegalArgumentException("WareHouseCode"+parameterMissingMsg);
		}
		try {
			warehouseCodeId =  (Integer) erpParameters.get(MAP_KEY_WAREHOUSECODEID);
		} catch(Exception e) {
			throw new IllegalArgumentException("warehouseCodeId"+parameterMissingMsg);
		}
		try {
			childBuyingCompanyCreation =  (Boolean) erpParameters.get(MAP_KEY_CHILD_BUYING_COMPANY_CREATION);
		} catch(Exception e) {
			throw new IllegalArgumentException("childBuyingCompanyCreation"+parameterMissingMsg);
		}
		try {
			singleShipTo =  (Boolean) erpParameters.get(MAP_KEY_SINGLE_SHIP_TO);
		} catch(Exception e) {
			throw new IllegalArgumentException("singleShipTo"+parameterMissingMsg);
		}

		try {

			switch(erpType) {
			case SX:
				break;
			case SXV10:
				break;
			case SXV6191:
				try {
					customerId =  (String) erpParameters.get(MAP_KEY_CUSTOMER_ID);
				} catch(Exception e) {
					throw new IllegalArgumentException("Customer id"+parameterMissingMsg);
				}
				SXV6191ShipToAddressRequest sXV6191ShipToAddressRequest = new SXV6191ShipToAddressRequest();
				shipToAddressRequest = sXV6191ShipToAddressRequest;
				sXV6191ShipToAddressRequest.setCustomerNumber(customerId);
				sXV6191ShipToAddressRequest.setIncludeClosedJobs(false);

				SXV6191BillToAddressRequest sXV6191BillToAddressRequest = new SXV6191BillToAddressRequest();
				sXV6191BillToAddressRequest.setCustomerNumber(customerId);
				billToAddressRequest = sXV6191BillToAddressRequest;
				allAddressListMapFromERP = iUserERPService.getBillToAndShipToAddressForSync(sXV6191BillToAddressRequest, sXV6191ShipToAddressRequest);

				if(allAddressListMapFromERP!=null) {
					billToAddressListFromERP = allAddressListMapFromERP.get(AddressType.Bill);
					if(billToAddressListFromERP!=null && !billToAddressListFromERP.isEmpty()) {
						billToAddressERP = billToAddressListFromERP.get(0);
					}
					shipToAddressListERP = allAddressListMapFromERP.get(AddressType.Ship);
				}
				break;
			case ECLIPSE:
				break;
			case CIMM2BCENTRAL:
			case CIMMESB:
				try {
					customerId =  (String) erpParameters.get(MAP_KEY_CUSTOMER_ID);
				} catch(Exception e) {
					throw new IllegalArgumentException("Customer id"+parameterMissingMsg);
				}
				Cimm2BCentralGetCustomerRequest centralGetCustomerRequest = new Cimm2BCentralGetCustomerRequest();
				shipToAddressRequest = centralGetCustomerRequest;
				centralGetCustomerRequest.setCustomerNumber(customerId);
				centralGetCustomerRequest.setBuyingCompanyId(buyingCompanyId);
				centralGetCustomerRequest.setUserId(userId);

				allAddressListMapFromERP = iUserERPService.getBillToAndShipToAddressForSync(centralGetCustomerRequest, null);

				if(allAddressListMapFromERP!=null) {
					billToAddressListFromERP = allAddressListMapFromERP.get(AddressType.Bill);
					if(billToAddressListFromERP!=null && !billToAddressListFromERP.isEmpty()) {
						billToAddressERP = billToAddressListFromERP.get(0);
					}
					shipToAddressListERP = allAddressListMapFromERP.get(AddressType.Ship);
				}
				break;
			}


		} catch(ERPServiceException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch(Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}	

		try {

			shipToAddressListDB = UserDAOService.getInstance().getShipToAddressList(userId,buyingCompanyId);

			if(shipToAddressListDB!=null && !shipToAddressListDB.isEmpty()) {
				BuyingCompanyAddressBook buyingCompanyAddressBook = shipToAddressListDB.get(0);
				if(buyingCompanyAddressBook.getUserShipToId()>0) {
					userShipToRecordsExist = true;
				}
			}

			billToAddressDB = UserDAOService.getInstance().getBillToAddress(buyingCompanyId);

		} catch(DBServiceException e) {
			logger.info(e.getMessage());
			dbExceptionOccured = true;
		} catch(Exception e) {
			logger.info(e.getMessage());
			dbExceptionOccured = true;
		}			

		if(shipToAddressListERP!=null 
				&& shipToAddressListDB!=null) {
			consolidatedShipToAddressList = consolidateAddressBook(buyingCompanyId,shipToAddressListERP,shipToAddressListDB,singleShipTo);

			if(userShipToRecordsExist) {
				filteredShipToAddressList = new ArrayList<BuyingCompanyAddressBook>();
				for(BuyingCompanyAddressBook bcAddressBook:consolidatedShipToAddressList) {
					if(bcAddressBook.getUserShipToId()>0) {
						filteredShipToAddressList.add(bcAddressBook);
					}
				}
			} else {
				filteredShipToAddressList = consolidatedShipToAddressList;
			}

		} else if(shipToAddressListERP!=null 
				&& !shipToAddressListERP.isEmpty() && !dbExceptionOccured) {
			consolidatedShipToAddressList =  shipToAddressListERP;
			filteredShipToAddressList = consolidatedShipToAddressList;
		} else if (shipToAddressListDB!=null) {
			consolidatedShipToAddressList =  shipToAddressListDB;
			filteredShipToAddressList = consolidatedShipToAddressList;
		} else {
			consolidatedShipToAddressList = new ArrayList<BuyingCompanyAddressBook>();
			filteredShipToAddressList = consolidatedShipToAddressList;
		}

		if(erpType==ErpType.ECLIPSE) {
			// this is to loop thro' all the ship to id and get full details
			((EclipseShipToAddressRequest)shipToAddressRequest).setFullDetails(true);
		}

		SyncServiceBuilder builder = new SyncServiceBuilder(erpType,buyingCompanyId,userId,iUserERPService);
		if(CommonUtility.validateString(billToAddressERP.getCountry()).equalsIgnoreCase("USA") 
				|| CommonUtility.validateString(billToAddressERP.getCountry()).replaceAll("\\s+", "").toLowerCase().contains("america")
				|| CommonUtility.validateString(billToAddressERP.getCountry()).replaceAll("\\s+", "").equalsIgnoreCase("unitedstates")){
					billToAddressERP.setCountry("US");
		}

		builder = builder.billToAddressFromDB(billToAddressDB)
				.billToAddressFromERP(billToAddressERP)
				.consolidatedShipToAddressList(consolidatedShipToAddressList)
				.shipToAddressFromDB(shipToAddressListDB)
				.shipToAddressFromERP(shipToAddressListERP)
				.shipToAddressRequest(shipToAddressRequest)
				.billToAddressRequest(billToAddressRequest)
				.generalCatalogAccess(generalCatalogAccess)
				.subsetId(subsetId)
				.warehouseCode(warehouseCode)
				.warehouseCodeId(warehouseCodeId)
				.childBuyingCompanyCreation(childBuyingCompanyCreation)
				.singleShipTo(singleShipTo);

		// Please do not add any code below this line as it will delay the response to the user as
		// the process will be happening within this thread. Any processing of synchronization must happen inside the executor service call
		executorService.submit(builder.build());			
		return filteredShipToAddressList;
	}

	public List<BuyingCompanyAddressBook> consolidateAddressBook(int buyingCompanyId,List<BuyingCompanyAddressBook> erpAddressList,List<BuyingCompanyAddressBook> dbAddressList, Boolean singleShipTo) {

		List<BuyingCompanyAddressBook> consolidatedAddressBookList = null;
		try{
			if(erpAddressList!=null 
					&& !erpAddressList.isEmpty()
					&& dbAddressList!=null 
					&& !dbAddressList.isEmpty()) {
				consolidatedAddressBookList = new ArrayList<BuyingCompanyAddressBook>();
				for(BuyingCompanyAddressBook erpAddressBook:erpAddressList) {
					for(BuyingCompanyAddressBook dbAddressBook:dbAddressList) {								
						if(erpAddressBook.equals(dbAddressBook) || singleShipTo) {
							erpAddressBook.setAddressBookId(dbAddressBook.getAddressBookId());	
							if(singleShipTo){
								erpAddressBook.setShipToId(CommonUtility.validateParseIntegerToString(dbAddressBook.getAddressBookId()));
								dbAddressBook.setShipToId(CommonUtility.validateParseIntegerToString(dbAddressBook.getAddressBookId()));
							}
							erpAddressBook.setUserShipToId(dbAddressBook.getUserShipToId());
							break;
						} 
					}
	
					consolidatedAddressBookList.add(erpAddressBook);
				} 
			} else if (erpAddressList!=null && !erpAddressList.isEmpty()) {
				consolidatedAddressBookList = erpAddressList;
			} 
	
			for(BuyingCompanyAddressBook erpAddressBook:consolidatedAddressBookList) {
				erpAddressBook.setBuyingCompanyId(buyingCompanyId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return consolidatedAddressBookList;
	}



}
