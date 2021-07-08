package com.unilognew.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unilog.utility.CommonUtility;
import com.unilognew.db.service.UserDAOService;
import com.unilognew.erp.service.IUserERPService;
import com.unilognew.exception.DBServiceException;
import com.unilognew.model.BuyingCompany;
import com.unilognew.model.BuyingCompanyAddressBook;
import com.unilognew.model.ERPServiceRequest;
import com.unilognew.util.CimmUtil;
import com.unilognew.util.ECommerceEnumType.AddressType;
import com.unilognew.util.ECommerceEnumType.ErpType;

public class ShipToAddressSyncService implements Runnable {
	
	private Logger logger = LoggerFactory.getLogger(ShipToAddressSyncService.class);
	
	private List<BuyingCompanyAddressBook> shipToAddressListFromERP;
	private List<BuyingCompanyAddressBook> shipToAddressListFromDB;
	private BuyingCompanyAddressBook billToAddressFromDB;
	private BuyingCompanyAddressBook billToAddressFromERP;
	private List<BuyingCompanyAddressBook> consolidatedShipToAddressList;
	private IUserERPService userERPService  = null;
	private ERPServiceRequest shipToAddressRequest = null;
	private ERPServiceRequest billToAddressRequest = null;
	private ErpType erpType=null;
	private int buyingCompanyId;
	private int userId;
	private String generalCatalogAccess;
	private String subsetId;
	private String warehouseCode;
	private int warehouseCodeId;
	private boolean childBuyingCompanyCreation=false;
	private boolean singleShipTo=false;
	
	private ShipToAddressSyncService(SyncServiceBuilder syncServiceBuilder) {
		this.erpType = syncServiceBuilder.erpType;
		this.buyingCompanyId = syncServiceBuilder.buyingCompanyId;
		this.userId = syncServiceBuilder.userId;
		this.userERPService = syncServiceBuilder.userERPService;
		this.shipToAddressListFromERP = syncServiceBuilder.shipToAddressListFromERP;
		this.shipToAddressListFromDB = syncServiceBuilder.shipToAddressListFromDB;
		this.billToAddressFromERP = syncServiceBuilder.billToAddressFromERP;
		this.billToAddressFromDB = syncServiceBuilder.billToAddressFromDB;
		this.consolidatedShipToAddressList = syncServiceBuilder.consolidatedShipToAddressList;
		this.shipToAddressRequest = syncServiceBuilder.shipToAddressRequest;
		this.billToAddressRequest = syncServiceBuilder.billToAddressRequest;
		this.generalCatalogAccess = syncServiceBuilder.generalCatalogAccess;
		this.subsetId = syncServiceBuilder.subsetId;
		this.warehouseCode = syncServiceBuilder.warehouseCode;
		this.warehouseCodeId = syncServiceBuilder.warehouseCodeId;
		this.childBuyingCompanyCreation = syncServiceBuilder.childBuyingCompanyCreation;
		this.singleShipTo = syncServiceBuilder.singleShipTo;
	}
	
	public static class SyncServiceBuilder implements IUnilogBuilder<ShipToAddressSyncService> {
		private ErpType erpType=null;
		private int buyingCompanyId;
		private int userId;
		private IUserERPService userERPService  = null;
		private List<BuyingCompanyAddressBook> shipToAddressListFromERP;
		private List<BuyingCompanyAddressBook> shipToAddressListFromDB;
		private BuyingCompanyAddressBook billToAddressFromDB;
		private BuyingCompanyAddressBook billToAddressFromERP;
		private List<BuyingCompanyAddressBook> consolidatedShipToAddressList;
		private ERPServiceRequest shipToAddressRequest = null;
		private ERPServiceRequest billToAddressRequest = null;
		private String generalCatalogAccess;
		private String subsetId;
		private String warehouseCode;
		private int warehouseCodeId;
		private boolean childBuyingCompanyCreation; 
		private boolean singleShipTo;
		
		public SyncServiceBuilder(ErpType erpType,int buyingCompanyId,int userId,IUserERPService userERPService) {
			this.erpType = erpType;
			this.buyingCompanyId = buyingCompanyId;
			this.userId = userId;
			this.userERPService = userERPService;
		}
		
		public SyncServiceBuilder shipToAddressFromERP(List<BuyingCompanyAddressBook> shipToAddressListFromERP) {
			this.shipToAddressListFromERP = shipToAddressListFromERP;
			return this;
		}
		
		public SyncServiceBuilder shipToAddressFromDB(List<BuyingCompanyAddressBook> shipToAddressListFromDB) {
			this.shipToAddressListFromDB = shipToAddressListFromDB;
			return this;
		}
		
		public SyncServiceBuilder billToAddressFromERP(BuyingCompanyAddressBook billToAddressFromERP) {
			this.billToAddressFromERP = billToAddressFromERP;
			return this;
		}
		
		public SyncServiceBuilder billToAddressFromDB(BuyingCompanyAddressBook billToAddressFromDB) {
			this.billToAddressFromDB = billToAddressFromDB;
			return this;
		}
		
		public SyncServiceBuilder consolidatedShipToAddressList(List<BuyingCompanyAddressBook> consolidatedShipToAddressList) {
			this.consolidatedShipToAddressList = consolidatedShipToAddressList;
			return this;
		}
		
		public SyncServiceBuilder shipToAddressRequest(ERPServiceRequest shipToAddressRequest) {
			this.shipToAddressRequest = shipToAddressRequest;
			return this;
		}
		
		public SyncServiceBuilder billToAddressRequest(ERPServiceRequest billToAddressRequest) {
			this.billToAddressRequest = billToAddressRequest;
			return this;
		}
				
		public SyncServiceBuilder generalCatalogAccess(String generalCatalogAccess) {
			this.generalCatalogAccess = generalCatalogAccess;
			return this;
		}
		
				
		public SyncServiceBuilder subsetId(String subsetId) {
			this.subsetId = subsetId;
			return this;
		}
		
				
		public SyncServiceBuilder warehouseCode(String warehouseCode) {
			this.warehouseCode = warehouseCode;
			return this;
		}
		
				
		public SyncServiceBuilder warehouseCodeId(int warehouseCodeId) {
			this.warehouseCodeId = warehouseCodeId;
			return this;
		}
		
		public SyncServiceBuilder childBuyingCompanyCreation(boolean childBuyingCompanyCreation) {
			this.childBuyingCompanyCreation = childBuyingCompanyCreation;
			return this;
		}
		
		public SyncServiceBuilder singleShipTo(boolean singleShipTo) {
			this.singleShipTo = singleShipTo;
			return this;
		}
		

		public ShipToAddressSyncService build() {
			return new ShipToAddressSyncService(this);
		}
		
	}
	
	
	/**
	 * @return the erpType
	 */
	public ErpType getErpType() {
		return erpType;
	}




	/**
	 * @param erpType the erpType to set
	 */
	public void setErpType(ErpType erpType) {
		this.erpType = erpType;
	}




	/**
	 * @return the buyingCompanyId
	 */
	public int getBuyingCompanyId() {
		return buyingCompanyId;
	}




	/**
	 * @param buyingCompanyId the buyingCompanyId to set
	 */
	public void setBuyingCompanyId(int buyingCompanyId) {
		this.buyingCompanyId = buyingCompanyId;
	}




	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}




	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}



	/**
	 * @return the userERPService
	 */
	public IUserERPService getUserERPService() {
		return userERPService;
	}


	/**
	 * @param userERPService the userERPService to set
	 */
	public void setUserERPService(IUserERPService userERPService) {
		this.userERPService = userERPService;
	}



	/**
	 * @return the generalCatalogAccess
	 */
	public String getGeneralCatalogAccess() {
		return generalCatalogAccess;
	}


	/**
	 * @param generalCatalogAccess the generalCatalogAccess to set
	 */
	public void setGeneralCatalogAccess(String generalCatalogAccess) {
		this.generalCatalogAccess = generalCatalogAccess;
	}


	/**
	 * @return the shipToAddressRequest
	 */
	public ERPServiceRequest getShipToAddressRequest() {
		return shipToAddressRequest;
	}




	/**
	 * @param eRPServiceRequest the shipToAddressRequest to set
	 */
	public void setShipToAddressRequest(ERPServiceRequest eRPServiceRequest) {
		this.shipToAddressRequest = eRPServiceRequest;
	}




	/**
	 * @return the shipToAddressListFromERP
	 */
	public List<BuyingCompanyAddressBook> getShipToAddressListFromERP() {
		return shipToAddressListFromERP;
	}




	/**
	 * @param shipToAddressListFromERP the shipToAddressListFromERP to set
	 */
	public void setShipToAddressListFromERP(
			List<BuyingCompanyAddressBook> shipToAddressListFromERP) {
		this.shipToAddressListFromERP = shipToAddressListFromERP;
	}




	/**
	 * @return the shipToAddressListFromDB
	 */
	public List<BuyingCompanyAddressBook> getShipToAddressListFromDB() {
		return shipToAddressListFromDB;
	}




	/**
	 * @param shipToAddressListFromDB the shipToAddressListFromDB to set
	 */
	public void setShipToAddressListFromDB(
			List<BuyingCompanyAddressBook> shipToAddressListFromDB) {
		this.shipToAddressListFromDB = shipToAddressListFromDB;
	}




	/**
	 * @return the billToAddressFromDB
	 */
	public BuyingCompanyAddressBook getBillToAddressFromDB() {
		return billToAddressFromDB;
	}




	/**
	 * @param billToAddressFromDB the billToAddressFromDB to set
	 */
	public void setBillToAddressFromDB(BuyingCompanyAddressBook billToAddressFromDB) {
		this.billToAddressFromDB = billToAddressFromDB;
	}




	/**
	 * @return the billToAddressFromERP
	 */
	public BuyingCompanyAddressBook getBillToAddressFromERP() {
		return billToAddressFromERP;
	}




	/**
	 * @param billToAddressFromERP the billToAddressFromERP to set
	 */
	public void setBillToAddressFromERP(
			BuyingCompanyAddressBook billToAddressFromERP) {
		this.billToAddressFromERP = billToAddressFromERP;
	}

	/**
	 * @return the consolidatedShipToAddressList
	 */
	public List<BuyingCompanyAddressBook> getConsolidatedShipToAddressList() {
		return consolidatedShipToAddressList;
	}

	/**
	 * @param consolidatedShipToAddressList the consolidatedShipToAddressList to set
	 */
	public void setConsolidatedShipToAddressList(
			List<BuyingCompanyAddressBook> consolidatedShipToAddressList) {
		this.consolidatedShipToAddressList = consolidatedShipToAddressList;
	}

	

	/**
	 * @return the billToAddressRequest
	 */
	public ERPServiceRequest getBillToAddressRequest() {
		return billToAddressRequest;
	}




	/**
	 * @param billToAddressRequest the billToAddressRequest to set
	 */
	public void setBillToAddressRequest(ERPServiceRequest billToAddressRequest) {
		this.billToAddressRequest = billToAddressRequest;
	}

	public String getSubsetId() {
		return subsetId;
	}




	public void setSubsetId(String subsetId) {
		this.subsetId = subsetId;
	}




	public String getWarehouseCode() {
		return warehouseCode;
	}




	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}




	public int getWarehouseCodeId() {
		return warehouseCodeId;
	}




	public void setWarehouseCodeId(int warehouseCodeId) {
		this.warehouseCodeId = warehouseCodeId;
	}
	
	

	public boolean isChildBuyingCompanyCreation() {
		return childBuyingCompanyCreation;
	}




	public void setChildBuyingCompanyCreation(boolean childBuyingCompanyCreation) {
		this.childBuyingCompanyCreation = childBuyingCompanyCreation;
	}


	public boolean isSingleShipTo() {
		return singleShipTo;
	}

	public void setSingleShipTo(boolean singleShipTo) {
		this.singleShipTo = singleShipTo;
	}

	@Override
	public void run() {
		
		List<String> queryList = null;
		UserDAOService userDAOService = null;
		
		try {
			if(erpType==ErpType.ECLIPSE) {
				//TODO: For Eclipse this needs to be handled in a loop
				logger.info("Approaching ERP... for data");
				shipToAddressListFromERP = userERPService.getCustomerShipToAddressList(shipToAddressRequest);
				this.consolidatedShipToAddressList = UserFacadeService.getInstance().consolidateAddressBook(this.buyingCompanyId,shipToAddressListFromERP, shipToAddressListFromDB,this.singleShipTo);			
			
				shipToAddressListFromERP = userERPService.getCustomerShipToAddressList(shipToAddressRequest);
			}
			
			userDAOService = UserDAOService.getInstance();
			
			BuyingCompany buyingCompany=userDAOService.getBuyingCompany(buyingCompanyId);
			
			queryList = getBillToAddressBuyingCompanyQueries(buyingCompany);
			
			queryList.addAll(getBillToAddressBuyingCompanyAddressBookQueries());

			queryList.addAll(getShipToAddressBuyingCompanyQueries(buyingCompany)) ;
			
			queryList.addAll(getShipToAddressBuyingCompanyAddressBookQueries(buyingCompany));
			
			logger.info("Synchronization is started for user = "+this.userId+" and buying company id "+this.buyingCompanyId);
			
			userDAOService.executeBatchQueries(queryList);
			
			userDAOService.generateOrResetKeywordForBuyingCompany(buyingCompanyId);
			
			logger.info("Synchronization is Done for user = "+this.userId+" and buying company id "+this.buyingCompanyId);
			
		} catch(Exception e) {
			logger.info("Synchronization failed for user = "+this.userId+" and buying company id "+this.buyingCompanyId+" due to "+e.getMessage());
		}
	}
	

	private List<String> getBillToAddressBuyingCompanyQueries(BuyingCompany parentBuyingCompany) throws DBServiceException {
		
		logger.info("Building the Bill To Address Buying company queries...");
		
		List<String> queryList = null;
		String query=null;
		queryList = new ArrayList<String>();
		try{
			Map<String,Object> dbFieldsThatAreDifferent = null;
			
			String customerType="";
			
			if(parentBuyingCompany!=null) {
				customerType = parentBuyingCompany.getCustomerType();
			}
			
			if(customerType==null || customerType.isEmpty()) {
				customerType="C";
			}
			
			if(parentBuyingCompany!=null) {
				
				if(CommonUtility.validateString(billToAddressFromERP.getEmailAddress()).length()==0) {
					billToAddressFromERP.setEmailAddress(parentBuyingCompany.getEmailAddress());
				}
				
				dbFieldsThatAreDifferent = parentBuyingCompany.getDBFieldsThatAreDifferent(this.billToAddressFromERP);
		
				query = getBuyingCompanyUpdateQuery(parentBuyingCompany,dbFieldsThatAreDifferent);
						
				if(query!=null && !query.isEmpty()) {
					queryList.add(query);	
				}
				
			} else {
				queryList.add(BuyingCompany.getInsertQuery(this.billToAddressFromERP,generalCatalogAccess,customerType,subsetId,warehouseCode,warehouseCodeId));
			}
				
			logger.info("Building the Ship To Address Buying company queries...Done");
		}catch(Exception e){
			e.printStackTrace();
		}
		return queryList;
	}
	
	
	private List<String> getBillToAddressBuyingCompanyAddressBookQueries() throws DBServiceException {
		
		logger.info("Building the Bill To Address Buying company queries...");
		
		Map<String,Object> dbFieldsThatAreDifferent = null;
		List<String> queryList = null;
		String query=null;
		queryList = new ArrayList<String>();
		try{
			if(billToAddressFromERP!=null && billToAddressFromDB!=null) {
				billToAddressFromERP.setAddressBookId(billToAddressFromDB.getAddressBookId());
				billToAddressFromERP.setUserShipToId(billToAddressFromDB.getUserShipToId());
				billToAddressFromERP.setBuyingCompanyId(buyingCompanyId);
				dbFieldsThatAreDifferent = billToAddressFromDB.getDBFieldsThatAreDifferent(billToAddressFromERP);
				query = this.getAddressBookUpdateQuery(billToAddressFromERP, dbFieldsThatAreDifferent);
				if(query!=null && !query.isEmpty()) { 
					queryList.add(query);
				}
			} else if(billToAddressFromERP!=null) {
				billToAddressFromERP.setBuyingCompanyId(buyingCompanyId);
				queryList.add(billToAddressFromERP.getInsertQuery(false));
			} else {
				logger.info("No information is available from ERP System so query cannot be built");
			}
				
			logger.info("Building the Bill To Address Buying company queries...Done");
		}catch(Exception e){
			e.printStackTrace();
		}
		return queryList;
	}
	
	private List<String> getShipToAddressBuyingCompanyQueries(BuyingCompany parentBuyingCompany) throws DBServiceException {
		
		logger.info("Building the Ship To Address Buying company queries...");
		
		List<String> queryList = null;
		List<BuyingCompany> buyingCompanyList = null;
		String query=null;
		UserDAOService userDAOService = null;
		queryList = new ArrayList<String>();
		try{
			userDAOService = UserDAOService.getInstance();
					
			buyingCompanyList = userDAOService.getChildBuyingCompany(buyingCompanyId);
			
			Map<String,Object> dbFieldsThatAreDifferent = null;
			BuyingCompany selectedBuyingCompany = null;
			for(BuyingCompanyAddressBook buyingCompanyAddressBook:consolidatedShipToAddressList) {
				selectedBuyingCompany = null;
				for(BuyingCompany buyingCompany:buyingCompanyList) {		
					if(buyingCompany.compareAddress(buyingCompanyAddressBook)) {
						if(buyingCompanyAddressBook.getEmailAddress()==null || buyingCompanyAddressBook.getEmailAddress().isEmpty()) {
							buyingCompanyAddressBook.setEmailAddress(parentBuyingCompany.getEmailAddress());
						}
						dbFieldsThatAreDifferent = buyingCompany.getDBFieldsThatAreDifferent(buyingCompanyAddressBook);
						buyingCompany.setSelectedForUpdation(true);
						selectedBuyingCompany = buyingCompany;
						break;
					} 
				}
				if(selectedBuyingCompany!=null) {
					query = getBuyingCompanyUpdateQuery(selectedBuyingCompany,dbFieldsThatAreDifferent);
					
					if(query!=null && !query.isEmpty()) {
						queryList.add(query);
					}
					
					// this is to update the child buying company bill to and ship to addresses in the buying company address book
	                BuyingCompanyAddressBook childBuyingCompanyBillToAddressBookDB = UserDAOService.getInstance().getBillToAddress(selectedBuyingCompany.getBuyingCompanyId());
	                
	                BuyingCompanyAddressBook childBuyingCompanyAddressBookERP = buyingCompanyAddressBook.clone();
	                
	                childBuyingCompanyAddressBookERP.setAddressType(AddressType.Bill);
	                childBuyingCompanyAddressBookERP.setBuyingCompanyId(selectedBuyingCompany.getBuyingCompanyId());
	                
	                if(childBuyingCompanyBillToAddressBookDB!=null) {
	                	
	                	if(childBuyingCompanyAddressBookERP.getEmailAddress()==null || childBuyingCompanyAddressBookERP.getEmailAddress().isEmpty()) {
	                		childBuyingCompanyAddressBookERP.setEmailAddress(parentBuyingCompany.getEmailAddress());
						}
	                	
	                	dbFieldsThatAreDifferent = childBuyingCompanyBillToAddressBookDB.getDBFieldsThatAreDifferent(childBuyingCompanyAddressBookERP);
	                	childBuyingCompanyAddressBookERP.setAddressBookId(childBuyingCompanyBillToAddressBookDB.getAddressBookId());
	                	query = getAddressBookUpdateQuery(childBuyingCompanyAddressBookERP,dbFieldsThatAreDifferent);
	                
		                if(query!=null && !query.isEmpty()) {
		                     queryList.add(query);
		                }
	                } else {
	                	queryList.add(childBuyingCompanyAddressBookERP.getInsertQuery(false));
	                }
	    			    
		            List<BuyingCompanyAddressBook> selectedBuyingCompanyShipToAddressBookList = UserDAOService.getInstance().getShipToAddressList(userId,selectedBuyingCompany.getBuyingCompanyId());
		            childBuyingCompanyAddressBookERP.setAddressType(AddressType.Ship);
		            
	                if(selectedBuyingCompanyShipToAddressBookList!=null && !selectedBuyingCompanyShipToAddressBookList.isEmpty()) {
		            	for(BuyingCompanyAddressBook childBuyingCompanyShipToAddressBookDB : selectedBuyingCompanyShipToAddressBookList) {
		                     dbFieldsThatAreDifferent = childBuyingCompanyShipToAddressBookDB.getDBFieldsThatAreDifferent(childBuyingCompanyAddressBookERP);
		                 	 childBuyingCompanyAddressBookERP.setAddressBookId(childBuyingCompanyShipToAddressBookDB.getAddressBookId());
		                     query = getAddressBookUpdateQuery(childBuyingCompanyAddressBookERP,dbFieldsThatAreDifferent);
		                     
		                     if(query!=null && !query.isEmpty()) {
		                          queryList.add(query);
		                     }
		                }
	                } else {
	                	queryList.add(childBuyingCompanyAddressBookERP.getInsertQuery(false));
	                }
				} else {
					
					if(this.childBuyingCompanyCreation) {
					
						buyingCompanyAddressBook.setEmailAddress(parentBuyingCompany.getEmailAddress());
						
						queryList.add(BuyingCompany.getInsertQuery(buyingCompanyAddressBook,generalCatalogAccess,parentBuyingCompany.getCustomerType(),subsetId,warehouseCode,warehouseCodeId));
					    
						// To create bill to entry in the address book for the new child buying company
						BuyingCompanyAddressBook shipToBuyingCompanyBillToAddressBook=buyingCompanyAddressBook.clone();
					    shipToBuyingCompanyBillToAddressBook.setTempObjectId(buyingCompanyAddressBook.getTempObjectId());
					    shipToBuyingCompanyBillToAddressBook.setAddressType(AddressType.Bill);
					    queryList.add(shipToBuyingCompanyBillToAddressBook.getInsertQuery(true));
					    
					    // To create ship to entry in the address book for the new child buying company
					    BuyingCompanyAddressBook shipToBuyingCompanyShipToAddressBook=buyingCompanyAddressBook.clone();
				        shipToBuyingCompanyShipToAddressBook.setTempObjectId(buyingCompanyAddressBook.getTempObjectId());
				        shipToBuyingCompanyShipToAddressBook.setAddressType(AddressType.Ship);
				        queryList.add(shipToBuyingCompanyShipToAddressBook.getInsertQuery(true));
					}
				    
				}
			}
			
			List<Integer> buyingCompanyIds = null;
			for (BuyingCompany buyingCompany : buyingCompanyList) {
				if (!buyingCompany.isSelectedForUpdation()) {
					queryList.addAll(buyingCompany.getDeleteQueries());
					if (buyingCompanyIds == null) {
						buyingCompanyIds = new ArrayList<Integer>();
					}
					buyingCompanyIds.add(buyingCompany.getBuyingCompanyId());
				}
			}
	
			if (buyingCompanyIds != null) {
				queryList.add(BuyingCompanyAddressBook
						.getDeleteQuery(buyingCompanyIds));
			}
	
	
			
			logger.info("Building the Ship To Address Buying company queries...Done");
		}catch(Exception e){
			e.printStackTrace();
		}
		return queryList;
	}
	
	private List<String> getShipToAddressBuyingCompanyAddressBookQueries(BuyingCompany parentBuyingCompany) {
		
		logger.info("Building the Ship To Address Buying company address book queries...");
		
		List<String> queryList = null;
		String query=null;
		try{
			queryList = new ArrayList<String>();
			if(this.consolidatedShipToAddressList!=null && !this.consolidatedShipToAddressList.isEmpty()) {
				
				Map<String,Object> dbFieldsThatAreDifferent = null;
				for(BuyingCompanyAddressBook buyingCompanyAddressBook:consolidatedShipToAddressList) {
					if(buyingCompanyAddressBook.getAddressBookId()>0) {
						for(BuyingCompanyAddressBook buyingCompanyAddressBookDB:shipToAddressListFromDB) {
							if(buyingCompanyAddressBookDB!=null 
									&& buyingCompanyAddressBookDB.getAddressBookId()==buyingCompanyAddressBook.getAddressBookId()) {
								if(buyingCompanyAddressBook.getEmailAddress()==null || buyingCompanyAddressBook.getEmailAddress().isEmpty()) {
									buyingCompanyAddressBook.setEmailAddress(parentBuyingCompany.getEmailAddress());
								}
								dbFieldsThatAreDifferent = buyingCompanyAddressBookDB.getDBFieldsThatAreDifferent(buyingCompanyAddressBook);
								buyingCompanyAddressBookDB.setSelectedForUpdation(true);
								break;
							}
						}
						query = getAddressBookUpdateQuery(buyingCompanyAddressBook,dbFieldsThatAreDifferent);
						if(query!=null && !query.isEmpty()) { 
							queryList.add(query);
						}
						
					} else {
						if(buyingCompanyAddressBook.getEmailAddress()==null || buyingCompanyAddressBook.getEmailAddress().isEmpty()) {
							buyingCompanyAddressBook.setEmailAddress(parentBuyingCompany.getEmailAddress());
						}
						queryList.add(buyingCompanyAddressBook.getInsertQuery(false));						
					}
				}
				
				for(BuyingCompanyAddressBook buyingCompanyAddressBookDB:shipToAddressListFromDB) {
					if(!buyingCompanyAddressBookDB.isSelectedForUpdation()) {
						queryList.add(buyingCompanyAddressBookDB.getDeleteQuery());	
					}
				}
			}
			logger.info("Building the Ship To Address Buying company address book queries...Done");
		}catch(Exception e){
			e.printStackTrace();
		}
		return queryList;
	}
	
	
	
	private String getBuyingCompanyUpdateQuery(BuyingCompany buyingCompany,Map<String,Object> dbFieldsThatAreDifferent) {
		StringBuilder queryBuilder = null;
		String query=null;
		try{
			if(buyingCompany!=null) {
				Iterator<Map.Entry<String,Object>> it = dbFieldsThatAreDifferent.entrySet().iterator();
				if(it.hasNext()) {
					queryBuilder = new StringBuilder("");
					queryBuilder.append("UPDATE ").append(BuyingCompany.DB_TABLE_NAME).append(" ")
					.append(BuyingCompany.DB_TABLE_ALIAS_NAME).append(" SET ");
					while (it.hasNext()) {
						Map.Entry<String,Object> pairs = (Map.Entry<String,Object>) it.next();
						queryBuilder.append(BuyingCompany.DB_TABLE_ALIAS_NAME).append(".").append(pairs.getKey()).append(" = ");
						if(pairs.getValue()!=null && pairs.getValue() instanceof String) {
							if(!((String)pairs.getValue()).isEmpty()) {
								queryBuilder.append("'").append(CimmUtil.replaceSingleQuote((String)pairs.getValue())).append("'");
							} else {
								queryBuilder.append("null");
							}
						} else {
							queryBuilder.append(pairs.getValue());
						}
						if(it.hasNext()) {
							queryBuilder.append(",");	
						}
					}
					queryBuilder.append(",").append(BuyingCompany.DB_TABLE_ALIAS_NAME).append(".").append(BuyingCompany.DB_FLD_USER_EDITED)
					.append("=").append(this.userId);
					queryBuilder.append(",").append(BuyingCompany.DB_TABLE_ALIAS_NAME).append(".").append(BuyingCompany.DB_FLD_UPDATED_DATETIME)
					.append("= sysdate");
					
					queryBuilder.append(" WHERE ").append(BuyingCompany.DB_TABLE_ALIAS_NAME)
					.append(".").append(BuyingCompany.DB_FLD_BUYING_COMPANY_ID)
					.append(" = ").append(buyingCompany.getBuyingCompanyId());
					query = queryBuilder.toString();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return query;
	}

	private String getAddressBookUpdateQuery(BuyingCompanyAddressBook buyingCompanyAddressBook,Map<String,Object> dbFieldsThatAreDifferent) {
		StringBuilder queryBuilder = null;
		String query=null;
		try{
			if(buyingCompanyAddressBook!=null) {
				Iterator<Map.Entry<String,Object>> it = dbFieldsThatAreDifferent.entrySet().iterator();
				
				if(it.hasNext()) {
					queryBuilder = new StringBuilder("");
					queryBuilder.append("UPDATE ").append(BuyingCompanyAddressBook.DB_TABLE_NAME).append(" ")
					.append(BuyingCompanyAddressBook.DB_TABLE_ALIAS_NAME).append(" SET ");
					while (it.hasNext()) {
						Map.Entry<String,Object> pairs = (Map.Entry<String,Object>) it.next();
						queryBuilder.append(BuyingCompanyAddressBook.DB_TABLE_ALIAS_NAME).append(".").append(pairs.getKey()).append(" = ");
						if(pairs.getValue()!=null && pairs.getValue() instanceof String) {
							if(!((String)pairs.getValue()).isEmpty()) {
								queryBuilder.append("'").append(CimmUtil.replaceSingleQuote((String)pairs.getValue())).append("'");
							} else {
								queryBuilder.append("null");
							}
						} else {
							queryBuilder.append(pairs.getValue());
						}
						if(it.hasNext()) {
							queryBuilder.append(",");	
						}
					}
					queryBuilder.append(",").append(BuyingCompanyAddressBook.DB_TABLE_ALIAS_NAME).append(".").append(BuyingCompanyAddressBook.DB_FLD_USER_EDITED)
					.append("=").append(this.userId);
					queryBuilder.append(",").append(BuyingCompanyAddressBook.DB_TABLE_ALIAS_NAME).append(".").append(BuyingCompanyAddressBook.DB_FLD_UPDATED_DATETIME)
					.append("= sysdate");
					queryBuilder.append(" WHERE ").append(BuyingCompanyAddressBook.DB_TABLE_ALIAS_NAME)
					.append(".").append(BuyingCompanyAddressBook.DB_FLD_ADDRESS_BOOK_ID)
					.append(" = ").append(buyingCompanyAddressBook.getAddressBookId());
					query = queryBuilder.toString();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return query;
	}

}
