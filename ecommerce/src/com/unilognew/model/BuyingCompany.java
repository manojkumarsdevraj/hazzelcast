package com.unilognew.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unilognew.util.CimmUtil;


public class BuyingCompany {
	
	public static final String DB_TABLE_ALIAS_NAME = "BC";
	public static final String DB_TABLE_NAME = "BUYING_COMPANY";
	public static final String DB_FLD_BUYING_COMPANY_ID="BUYING_COMPANY_ID";
	public static final String DB_FLD_CUSTOMER_NAME = "CUSTOMER_NAME";
	public static final String DB_FLD_SHORT_NAME = "SHORT_NAME";
	public static final String DB_FLD_ADDRESS1 = "ADDRESS1";
	public static final String DB_FLD_ADDRESS2 = "ADDRESS2";
	public static final String DB_FLD_CITY = "CITY";
	public static final String DB_FLD_STATE = "STATE";
	public static final String DB_FLD_COUNTRY = "COUNTRY";
	public static final String DB_FLD_ZIPCODE = "ZIP";
	public static final String DB_FLD_ENTITY_ID = "ENTITY_ID";
	public static final String DB_FLD_SHIP_TO_ID = "SHIP_TO_ID";
	public static final String DB_FLD_EMAIL = "EMAIL";	
	public static final String DB_FLD_PARENT_COMPANY_ID="PARENT_COMPANY_ID";
	public static final String DB_FLD_UPDATED_DATETIME="UPDATED_DATETIME";
	public static final String DB_FLD_GENERAL_CATALOG_ACCESS="GENERAL_CATALOG_ACCESS";
	public static final String DB_FLD_STATUS="STATUS";
	public static final String DB_FLD_CUSTOMER_TYPE="CUSTOMER_TYPE";
	public static final String DB_FLD_SUBSET_ID="SUBSET_ID";
	public static final String DB_FLD_WAREHOUSE_CODE_ID="WAREHOUSE_CODE_ID";
	public static final String DB_FLD_WAREHOUSE_CODE="WAREHOUSE_CODE";
	public static final String DB_FLD_TEMP_GUID="TEMP_GUID";
	public static final String DB_FLD_USER_EDITED = "USER_EDITED";
	
	private int buyingCompanyId;
	private String entityId;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipcode;
	private String country;
	private String phone;
	private String customerName;
	private String shortName;
	private String emailAddress;
	private String shipToId;
	private String customerType;
	private String subsetId;
	private int warehouseCodeId;
	private String warehouseCode;
	
	
	// non db fields
	

	private boolean selectedForUpdation=false;
	
	
	
	
	/**
	 * @return the customerType
	 */
	public String getCustomerType() {
		return customerType;
	}

	/**
	 * @param customerType the customerType to set
	 */
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	/**
	 * @return the shipToId
	 */
	public String getShipToId() {
		return shipToId;
	}

	/**
	 * @param shipToId the shipToId to set
	 */
	public void setShipToId(String shipToId) {
		this.shipToId = shipToId;
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
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getSubsetId() {
		return subsetId;
	}

	public void setSubsetId(String subsetId) {
		this.subsetId = subsetId;
	}

	public int getWarehouseCodeId() {
		return warehouseCodeId;
	}

	public void setWarehouseCodeId(int warehouseCodeId) {
		this.warehouseCodeId = warehouseCodeId;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	

	/**
	 * @return the selectedForUpdation
	 */
	public boolean isSelectedForUpdation() {
		return selectedForUpdation;
	}

	/**
	 * @param selectedForUpdation the selectedForUpdation to set
	 */
	public void setSelectedForUpdation(boolean selectedForUpdation) {
		this.selectedForUpdation = selectedForUpdation;
	}
	
	public boolean compareAddress(BuyingCompanyAddressBook obj) throws IllegalArgumentException {
		if (obj == null) {
			throw new IllegalArgumentException("Properties cannot be compared as the object is null");
		}
		
		BuyingCompanyAddressBook other = (BuyingCompanyAddressBook) obj;
		

		if (shipToId == null) {
			if (other.getShipToId() != null && !other.getShipToId().isEmpty()) {
				return false;
			}
		} else if (!shipToId.equals(other.getShipToId())) {
			return false;
		} else if(!shipToId.isEmpty() && shipToId.equals(other.getShipToId())) {
			return true;
		}
		
		if (customerName == null) {
			if (other.getFirstName() != null && !other.getFirstName().isEmpty()) {
				return false;
			}
		}else if(customerName.isEmpty()) {
			if (other.getFirstName() != null && !other.getFirstName().isEmpty()) {
				return false;
			}
		}else if (!customerName.equals(other.getFirstName())) {
			return false;
		}
		
		
		if (address1 == null) {
			if (other.getAddress1() != null && !other.getAddress1().isEmpty()) {
				return false;
			}
		} else if(address1.isEmpty()) {
			if (other.getAddress1() != null && !other.getAddress1().isEmpty()) {
				return false;
			}
		}else if (!address1.equals(other.getAddress1())) {
			return false;
		}
		
		if (address2 == null) {
			if (other.getAddress2() != null && !other.getAddress2().isEmpty()) {
				return false;
			}
		}else if(address2.isEmpty()) {
			if (other.getAddress2() != null && !other.getAddress2().isEmpty()) {
				return false;
			}
		}else if (!address2.equals(other.getAddress2())) {
			return false;
		}
		
		if (city == null) {
			if (other.getCity() != null && !other.getCity().isEmpty()) {
				return false;
			}
		} else if(city.isEmpty()){
			
			if (other.getAddress2() != null && !other.getAddress2().isEmpty()) {
				return false;
			}
		}else if (!city.equals(other.getCity())) {
			return false;
		}		

		if (state == null) {
			if (other.getState() != null && !other.getState().isEmpty()) {
				return false;
			}
		}else if(state.isEmpty()){
			if (other.getState() != null && !other.getState().isEmpty()) {
				return false;
			}
		}else if (!state.equals(other.getState())) {
			return false;
		}
		
		if (country == null) {
			if (other.getCountry() != null && !other.getCountry().isEmpty()) {
				return false;
			}
		} else if(country.isEmpty()){
			
			if (other.getCountry() != null && !other.getCountry().isEmpty()) {
				return false;
			}
		}
		else if (!country.equals(other.getCountry())) {
			return false;
		}
		
		if (zipcode == null) {
			if (other.getZipcode() != null && !other.getZipcode().isEmpty()) {
				return false;
			}
		}else if(zipcode.isEmpty()){
			if (other.getZipcode() != null && !other.getZipcode().isEmpty()) {
				return false;
			}
		}else if (!zipcode.equals(other.getZipcode())) {
			return false;
		}
		
		return true;
	}
	

	public Map<String,Object> getDBFieldsThatAreDifferent(BuyingCompanyAddressBook obj) throws IllegalArgumentException {
		Map<String,Object> dbFieldsThatAreDifferent = null;
		
		dbFieldsThatAreDifferent = new HashMap<String,Object>();
		
		if (obj == null) {
			throw new IllegalArgumentException("Properties cannot be compared as the object is null");
		}
		
		BuyingCompanyAddressBook other = (BuyingCompanyAddressBook) obj;
		
		if (customerName == null) {
			if (other.getFirstName() != null && !other.getFirstName().isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_CUSTOMER_NAME,other.getFirstName());
			}
		} else if (!customerName.equals(other.getFirstName())) {
			dbFieldsThatAreDifferent.put(DB_FLD_CUSTOMER_NAME,other.getFirstName());
		}		
		
		if (address1 == null) {
			if (other.getAddress1() != null && !other.getAddress1().isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_ADDRESS1,other.getAddress1());
			}
		} else if (!address1.equals(other.getAddress1())) {
			dbFieldsThatAreDifferent.put(DB_FLD_ADDRESS1,other.getAddress1());
		}
		if (address2 == null) {
			if (other.getAddress2() != null && !other.getAddress2().isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_ADDRESS2,other.getAddress2());
			}
		} else if (!address2.equals(other.getAddress2())) {
			dbFieldsThatAreDifferent.put(DB_FLD_ADDRESS2,other.getAddress2());
		}
		
		if (city == null) {
			if (other.getCity() != null && !other.getCity().isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_CITY,other.getCity());
			}
		} else if (!city.equals(other.getCity())) {
			dbFieldsThatAreDifferent.put(DB_FLD_CITY,other.getCity());
		}
		

		if (state == null) {
			if (other.getState() != null && !other.getState().isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_STATE,other.getState());
			}
		} else if (!state.equals(other.getState())) {
			dbFieldsThatAreDifferent.put(DB_FLD_STATE,other.getState());
		}
		
		if (country == null) {
			if (other.getCountry() != null && !other.getCountry().isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_COUNTRY,other.getCountry());
			}
		} else if (!country.equals(other.getCountry())) {
			dbFieldsThatAreDifferent.put(DB_FLD_COUNTRY,other.getCountry());
		}
		
		if (zipcode == null) {
			if (other.getZipcode() != null && !other.getZipcode().isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_ZIPCODE,other.getZipcode());
			}
		} else if (!zipcode.equals(other.getZipcode())) {
			dbFieldsThatAreDifferent.put(DB_FLD_ZIPCODE,other.getZipcode());
		}

		if (shipToId == null) {
			if (other.getShipToId() != null && !other.getShipToId().isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_SHIP_TO_ID,other.getShipToId());
			}
		} else if (!shipToId.equals(other.getShipToId())) {
			dbFieldsThatAreDifferent.put(DB_FLD_SHIP_TO_ID,other.getShipToId());
		}
		if (emailAddress == null) {
			if (other.getEmailAddress() != null && !other.getEmailAddress().isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_EMAIL,other.getEmailAddress());
			}
		} else if (!emailAddress.equals(other.getEmailAddress())) {
			dbFieldsThatAreDifferent.put(DB_FLD_EMAIL,other.getEmailAddress());
		}
		
		
				
		return dbFieldsThatAreDifferent;
	}
	
	
		public List<String> getDeleteQueries() {
        
			List<String> deleteQueries = new ArrayList<String>();
			StringBuilder queryBuilder = new StringBuilder("");
			try{
	        	queryBuilder.append("UPDATE ").append(DB_TABLE_NAME).append(" SET ")
	        	.append(DB_FLD_STATUS).append("='").append("D").append("'")
	        	.append(" WHERE ").append(DB_FLD_PARENT_COMPANY_ID)
	        	.append(" = ").append(this.getBuyingCompanyId());
	        
		        deleteQueries.add(queryBuilder.toString());
		        
		        queryBuilder = new StringBuilder("");
		        
		        queryBuilder.append("UPDATE ").append(DB_TABLE_NAME).append(" SET ")
		        .append(DB_FLD_STATUS).append("='").append("D").append("'")
		        .append(" WHERE ").append(DB_FLD_BUYING_COMPANY_ID)
		        .append(" = ").append(this.getBuyingCompanyId());
		        deleteQueries.add(queryBuilder.toString());
			}catch(Exception e){
				e.printStackTrace();
			}
        
        return deleteQueries;
   }

	public static String getSubqueryToFindNewlyInsertedBuyingCompany(BuyingCompanyAddressBook bcAddressBook) {
		 
		  // This will help in identifying the newly inserted buying company in a batch process
		  // as the batch process cannot reply on the sequence current value as 
		  // some other thread might take another value from the sequence
		  
		  StringBuilder subQueryBuilder = new StringBuilder(" ( ").append("SELECT ")
		   .append(DB_FLD_BUYING_COMPANY_ID).append(" FROM ")
		   .append(DB_TABLE_NAME)
		   .append(" WHERE ")
		   .append(DB_FLD_TEMP_GUID).append("=")
		   .append("'")
		   .append(bcAddressBook.getTempObjectId())
		   .append("'").append(")").append(",");
		  
		  return subQueryBuilder.toString();
		 }


	public static String getInsertQuery(BuyingCompanyAddressBook buyingCompanyAddressBook,String generalCatalogUser,String customerType,String subsetId,String warehouseCode,int warehouseCodeId) {
		StringBuilder queryBuilder = new StringBuilder("");
		final String nullString="null";
		try{
			queryBuilder.append("INSERT INTO ").append(DB_TABLE_NAME).append(" ( ")
					.append(DB_FLD_BUYING_COMPANY_ID).append(",")
					.append(DB_FLD_PARENT_COMPANY_ID).append(",")
					.append(DB_FLD_ENTITY_ID).append(",")
					.append(DB_FLD_SHIP_TO_ID).append(",")
					.append(DB_FLD_CUSTOMER_NAME).append(",")
					.append(DB_FLD_SHORT_NAME).append(",")
					.append(DB_FLD_ADDRESS1).append(",")
					.append(DB_FLD_ADDRESS2).append(",")
					.append(DB_FLD_CITY).append(",")
					.append(DB_FLD_STATE).append(",")
					.append(DB_FLD_COUNTRY).append(",")
					.append(DB_FLD_ZIPCODE).append(",")
					.append(DB_FLD_EMAIL).append(",")
					.append(DB_FLD_WAREHOUSE_CODE_ID).append(",")
					.append(DB_FLD_WAREHOUSE_CODE).append(",")
					.append(DB_FLD_GENERAL_CATALOG_ACCESS).append(",")
					.append(DB_FLD_STATUS).append(",")
					.append(DB_FLD_CUSTOMER_TYPE).append(",")
					.append(DB_FLD_SUBSET_ID).append(",")
					.append(DB_FLD_TEMP_GUID)
					
					.append(") VALUES (")
					.append("BUYING_COMPANY_ID_SEQ.NEXTVAL").append(",")
					.append(buyingCompanyAddressBook.getBuyingCompanyId()).append(",");
					if(buyingCompanyAddressBook.getEntityId()!=null && !buyingCompanyAddressBook.getEntityId().isEmpty()) {
						queryBuilder.append("'").append(buyingCompanyAddressBook.getEntityId()).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
			
					if(buyingCompanyAddressBook.getShipToId()!=null && !buyingCompanyAddressBook.getShipToId().isEmpty()) {
						queryBuilder.append("'").append(buyingCompanyAddressBook.getShipToId()).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					if(buyingCompanyAddressBook.getFirstName()!=null && !buyingCompanyAddressBook.getFirstName().isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(buyingCompanyAddressBook.getFirstName())).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					
					if(buyingCompanyAddressBook.getFirstName()!=null && !buyingCompanyAddressBook.getFirstName().isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(buyingCompanyAddressBook.getFirstName())).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					
					if(buyingCompanyAddressBook.getAddress1()!=null && !buyingCompanyAddressBook.getAddress1().isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(buyingCompanyAddressBook.getAddress1())).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					
					queryBuilder.append(",");
					if(buyingCompanyAddressBook.getAddress2()!=null && !buyingCompanyAddressBook.getAddress2().isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(buyingCompanyAddressBook.getAddress2())).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					if(buyingCompanyAddressBook.getCity()!=null && !buyingCompanyAddressBook.getCity().isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(buyingCompanyAddressBook.getCity())).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					
					if(buyingCompanyAddressBook.getState()!=null && !buyingCompanyAddressBook.getState().isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(buyingCompanyAddressBook.getState())).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					
					if(buyingCompanyAddressBook.getCountry()!=null && !buyingCompanyAddressBook.getCountry().isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(buyingCompanyAddressBook.getCountry())).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					
					if(buyingCompanyAddressBook.getZipcode()!=null && !buyingCompanyAddressBook.getZipcode().isEmpty()) {
						queryBuilder.append("'").append(buyingCompanyAddressBook.getZipcode()).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					
					if(buyingCompanyAddressBook.getEmailAddress()!=null && !buyingCompanyAddressBook.getEmailAddress().isEmpty()) {
						queryBuilder.append(",'").append(CimmUtil.replaceSingleQuote(buyingCompanyAddressBook.getEmailAddress())).append("'");
					} else {
						queryBuilder.append(",").append(nullString);
					}
					if(warehouseCodeId>0) {
						queryBuilder.append(",").append(warehouseCodeId);
					} else {
						queryBuilder.append(",").append(0);
					}
					if(warehouseCode!=null && !warehouseCode.isEmpty()) {
						queryBuilder.append(",'").append(CimmUtil.replaceSingleQuote(warehouseCode)).append("'");
					} else {
						queryBuilder.append(",").append(nullString);
					}
					
					queryBuilder.append(",'").append(generalCatalogUser).append("'")
					.append(",'").append("A").append("'")
					.append(",'").append(customerType).append("'")
					.append(",'").append(subsetId).append("'")
					.append(",'").append(buyingCompanyAddressBook.getTempObjectId()).append("'");
					
					
					
					queryBuilder.append(")");
		}catch(Exception e){
			e.printStackTrace();
		}
		return queryBuilder.toString();
				
	}
	

	
}