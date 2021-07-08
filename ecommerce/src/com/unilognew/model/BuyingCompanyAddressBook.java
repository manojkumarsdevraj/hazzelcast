package com.unilognew.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.unilognew.util.CimmUtil;
import com.unilognew.util.ECommerceEnumType.AddressType;


public class BuyingCompanyAddressBook implements Cloneable {
	
	public static final String DB_TABLE_ALIAS_NAME = "BCAB";
	public static final String DB_TABLE_NAME = "BC_ADDRESS_BOOK";
	public static final String DB_FLD_ADDRESS_BOOK_ID="BC_ADDRESS_BOOK_ID";
	public static final String DB_FLD_BUYING_COMPANY_ID="BUYING_COMPANY_ID";
	public static final String DB_FLD_FIRST_NAME = "FIRST_NAME";
	public static final String DB_FLD_LAST_NAME = "LAST_NAME";
	public static final String DB_FLD_ADDRESS1 = "ADDRESS1";
	public static final String DB_FLD_ADDRESS2 = "ADDRESS2";
	public static final String DB_FLD_CITY = "CITY";
	public static final String DB_FLD_STATE = "STATE";
	public static final String DB_FLD_COUNTRY = "COUNTRY";
	public static final String DB_FLD_ZIPCODE = "ZIPCODE";
	public static final String DB_FLD_ENTITY_ID = "ENTITY_ID";
	public static final String DB_FLD_PHONE = "PHONE";
	public static final String DB_FLD_ADDRESS_TYPE = "ADDRESS_TYPE";
	public static final String DB_FLD_SHIP_TO_ID = "SHIP_TO_ID";
	public static final String DB_FLD_EMAIL = "EMAIL";
	public static final String DB_FLD_SHIP_TO_NAME = "SHIP_TO_NAME";
	public static final String DB_FLD_UPDATED_DATETIME ="UPDATED_DATETIME";
	public static final String DB_FLD_STATUS = "STATUS";
	public static final String DB_FLD_DEFAULT_FOR_ALL = "DEFAULT_FOR_ALL";
	public static final String DB_FLD_USER_EDITED = "USER_EDITED";
	public static final String DB_FLD_WAREHOUSE_CODE ="WAREHOUSE_CODE";
	
	
	private int addressBookId;
	private int buyingCompanyId;
	private String entityId;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipcode;
	private String country;
	private String phone;
	private String firstName;
	private String lastName;
	private String shipToId;
	private AddressType addressType;
	private String shipToName;
	private String emailAddress;
	private int userShipToId;
	private String defaultForAll;
	private String customerType;
	private String warehouseCode;
	private int warehouseCodeId;
	private String wareHouseCodeStr;
	private Map<String, String> customFields;
	
	private String companyName;
	// non db fields
	



	private boolean selectedForUpdation=false;
	private String tempObjectId;
	
	
	public BuyingCompanyAddressBook() {
		  this.tempObjectId = CimmUtil.generateGUID();
		 }
	
	/**
	 * @return the defaultForAll
	 */
	public String getDefaultForAll() {
		return defaultForAll;
	}
	/**
	 * @param defaultForAll the defaultForAll to set
	 */
	public void setDefaultForAll(String defaultForAll) {
		this.defaultForAll = defaultForAll;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the addressType
	 */
	public AddressType getAddressType() {
		return addressType;
	}
	/**
	 * @param addressType the addressType to set
	 */
	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}
	/**
	 * @return the addressBookId
	 */
	public int getAddressBookId() {
		return addressBookId;
	}
	/**
	 * @param addressBookId the addressBookId to set
	 */
	public void setAddressBookId(int addressBookId) {
		this.addressBookId = addressBookId;
	}
	
	/**
	 * @return the shipToName
	 */
	public String getShipToName() {
		return shipToName;
	}
	/**
	 * @param shipToName the shipToName to set
	 */
	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
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
		if(emailAddress==null) {
			emailAddress="";
		}
		this.emailAddress = emailAddress;
	}
		
	
	/**
	 * @return the userShipToId
	 */
	public int getUserShipToId() {
		return userShipToId;
	}
	/**
	 * @param userShipToId the userShipToId to set
	 */
	public void setUserShipToId(int userShipToId) {
		this.userShipToId = userShipToId;
	}
		
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
	public String getTempObjectId() {
		return tempObjectId;
	}
	public void setTempObjectId(String tempObjectId) {
		this.tempObjectId = tempObjectId;
	}
	/**
	 * @return the customFields
	 */
	public Map<String, String> getCustomFields() {
		return customFields;
	}

	/**
	 * @param customFields the customFields to set
	 */
	public void setCustomFields(Map<String, String> customFields) {
		this.customFields = customFields;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		try{
			result = prime * result
					+ ((address1 == null) ? 0 : address1.hashCode());
			result = prime * result
					+ ((address2 == null) ? 0 : address2.hashCode());
			result = prime * result
					+ ((addressType == null) ? 0 : addressType.hashCode());
			result = prime * result + ((city == null) ? 0 : city.hashCode());
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			result = prime * result + ((country == null) ? 0 : country.hashCode());
			result = prime * result + ((zipcode == null) ? 0 : zipcode.hashCode());
			result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
			result = prime * result
					+ ((firstName == null) ? 0 : firstName.hashCode());
			result = prime * result + ((phone == null) ? 0 : phone.hashCode());
			result = prime * result
					+ ((shipToId == null) ? 0 : shipToId.hashCode());
			result = prime * result
					+ ((shipToName == null) ? 0 : shipToName.hashCode());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BuyingCompanyAddressBook)) {
			return false;
		}
		BuyingCompanyAddressBook other = (BuyingCompanyAddressBook) obj;
		
		// if shipToId are same, then both the objects are equal
		if (shipToId == null) {
			if (other.shipToId != null) {
				return false;
			}
		} else if (!shipToId.equals(other.shipToId)) {
			return false;
		} else if (!shipToId.isEmpty() && shipToId.equals(other.shipToId)) {
			return true;
		}

		if (shipToName == null) {
			if (other.shipToName != null) {
				return false;
			}
		} else if(shipToName.isEmpty()) {
			if (other.shipToName != null && !other.shipToName.isEmpty()) {
				return false;
			}
		} else if (!shipToName.equals(other.shipToName)) {
			return false;
		} 
		
		if (address1 == null) {
			if (other.address1 != null) {
				return false;
			}
		} else if(address1.isEmpty()) {
			if (other.address1 != null && !other.address1.isEmpty()) {
				return false;
			}
		} else if (!address1.equals(other.address1)) {
			return false;
		}
		if (address2 == null) {
			if (other.address2 != null) {
				return false;
			}
		} else if(address2.isEmpty()) {
			if (other.address2 != null && !other.address2.isEmpty()) {
				return false;
			}
		} else if (!address2.equals(other.address2)) {
			return false;
		}
		if (addressType != other.addressType) {
			return false;
		}
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if(city.isEmpty()) {
			if (other.city != null && !other.city.isEmpty()) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (country == null) {
			if (other.country != null) {
				return false;
			}
		} else if(country.isEmpty()) {
			if (other.country != null && !other.country.isEmpty()) {
				return false;
			}
		} else if (!country.equals(other.country)) {
			return false;
		}
		if (!entityId.equalsIgnoreCase(other.entityId)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if(firstName.isEmpty()) {
			if (other.firstName != null && !other.firstName.isEmpty()) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (phone == null) {
			if (other.phone != null) {
				return false;
			}
		} else if(phone.isEmpty()) {
			if (other.phone != null && !other.phone.isEmpty()) {
				return false;
			}
		} else if (!phone.equals(other.phone)) {
			return false;
		}
		
		if (state == null) {
			if (other.state != null) {
				return false;
			}
		} else if(state.isEmpty()) {
			if (other.state != null && !other.state.isEmpty()) {
				return false;
			}
		} else if (!state.equals(other.state)) {
			return false;
		}
		if (zipcode == null) {
			if (other.zipcode != null) {
				return false;
			}
		} else if(zipcode.isEmpty()) {
			if (other.zipcode != null && !other.zipcode.isEmpty()) {
				return false;
			}
		} else if (!zipcode.equals(other.zipcode)) {
			return false;
		}
		return true;
	}
	
	public Map<String,Object> getDBFieldsThatAreDifferent(Object obj) throws IllegalArgumentException {
		Map<String,Object> dbFieldsThatAreDifferent = null;
		
		dbFieldsThatAreDifferent = new HashMap<String,Object>();
		
		if (this == obj) {
			return dbFieldsThatAreDifferent;
		}
		if (obj == null) {
			throw new IllegalArgumentException("Properties cannot be compared as the object is null");
		}
		if (!(obj instanceof BuyingCompanyAddressBook)) {
			throw new IllegalArgumentException("Properties cannot be compared as the object is of different type");
		}
		
		BuyingCompanyAddressBook other = (BuyingCompanyAddressBook) obj;
		
		if(this.addressType==AddressType.Ship) {
		
			if (shipToId == null) {
				if (other.shipToId != null && !other.shipToId.isEmpty()) {
					dbFieldsThatAreDifferent.put(DB_FLD_SHIP_TO_ID,other.shipToId);
				}
			} else if (!shipToId.equals(other.shipToId)) {
				dbFieldsThatAreDifferent.put(DB_FLD_SHIP_TO_ID,other.shipToId);
			} 
			
			if (shipToName == null) {
				if (other.shipToName != null && !other.shipToName.isEmpty()) {
					dbFieldsThatAreDifferent.put(DB_FLD_SHIP_TO_NAME,other.shipToName);
				}
			} else if (!shipToName.equals(other.shipToName)) {
				dbFieldsThatAreDifferent.put(DB_FLD_SHIP_TO_NAME,other.shipToName);
			}
		}
		
		if (address1 == null) {
			if (other.address1 != null && !other.address1.isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_ADDRESS1,other.address1);
			}
		} else if (!address1.equals(other.address1)) {
			dbFieldsThatAreDifferent.put(DB_FLD_ADDRESS1,other.address1);
		}
		if (address2 == null) {
			if (other.address2 != null && !other.address2.isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_ADDRESS2,other.address2);
			}
		} else if (!address2.equals(other.address2)) {
			dbFieldsThatAreDifferent.put(DB_FLD_ADDRESS2,other.address2);
		}
		if (addressType != other.addressType) {
			dbFieldsThatAreDifferent.put(DB_FLD_ADDRESS_TYPE,other.addressType.name());
		}
		if (city == null) {
			if (other.city != null && !other.city.isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_CITY,other.city);
			}
		} else if (!city.equals(other.city)) {
			dbFieldsThatAreDifferent.put(DB_FLD_CITY,other.city);
		}
		if (country == null) {
			if (other.country != null && !other.country.isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_COUNTRY,other.country);
			}
		} else if (!country.equals(other.country)) {
			dbFieldsThatAreDifferent.put(DB_FLD_COUNTRY,other.country);
		}
		if (!entityId.equalsIgnoreCase(other.entityId)) {
			dbFieldsThatAreDifferent.put(DB_FLD_ENTITY_ID,other.entityId);
		}
		
		if(this.addressType==AddressType.Ship) {
			// The value for the first name of the bill to address is given while registration
			if (firstName == null) {
				if (other.firstName != null && !other.firstName.isEmpty()) {
					dbFieldsThatAreDifferent.put(DB_FLD_FIRST_NAME,other.firstName);
				}
			} else if (!firstName.equals(other.firstName)) {
				dbFieldsThatAreDifferent.put(DB_FLD_FIRST_NAME,other.firstName);
			}
		}
		
		if (phone == null) {
			if (other.phone != null && !other.phone.isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_PHONE,other.phone);
			}
		} else if (!phone.equals(other.phone)) {
			dbFieldsThatAreDifferent.put(DB_FLD_PHONE,other.phone);
		}
		
		if (state == null) {
			if (other.state != null && !other.state.isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_STATE,other.state);
			}
		} else if (!state.equals(other.state)) {
			dbFieldsThatAreDifferent.put(DB_FLD_STATE,other.state);
		}
		
		if (zipcode == null) {
			if (other.zipcode != null && !other.zipcode.isEmpty()) {
				dbFieldsThatAreDifferent.put(DB_FLD_ZIPCODE,other.zipcode);
			}
		} else if (!zipcode.equals(other.zipcode)) {
			dbFieldsThatAreDifferent.put(DB_FLD_ZIPCODE,other.zipcode);
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
	
	public String getDeleteQuery() {
		StringBuilder queryBuilder = new StringBuilder("");
		try{
			queryBuilder.append("DELETE FROM ").append(DB_TABLE_NAME)
			.append(" WHERE ").append(DB_FLD_ADDRESS_BOOK_ID)
			.append(" = ").append(this.getAddressBookId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return queryBuilder.toString();
	}

	public static String getDeleteQuery(List<Integer> buyingCompanyIds) {

		StringBuilder queryBuilder = new StringBuilder("");
		try{
			if (buyingCompanyIds != null && !buyingCompanyIds.isEmpty()) {
	
				queryBuilder.append("DELETE FROM ").append(DB_TABLE_NAME)
						.append(" WHERE ").append(DB_FLD_BUYING_COMPANY_ID);
				if (buyingCompanyIds.size() > 1) {
					queryBuilder.append(" in (");
	
					Iterator<Integer> iterator = buyingCompanyIds.iterator();
	
					while (iterator.hasNext()) {
						queryBuilder.append(iterator.next());
						if (iterator.hasNext()) {
							queryBuilder.append(",");
						}
					}
					queryBuilder.append(")");
				} else {
					queryBuilder.append("=").append(buyingCompanyIds.get(0));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return queryBuilder.toString();
	}
	
	public String getInsertQuery(boolean newBuyingCompany) {
		final String nullString="null";
		StringBuilder queryBuilder = new StringBuilder("");
		try{
			queryBuilder.append("INSERT INTO ").append(DB_TABLE_NAME).append(" ( ")
					.append(DB_FLD_ADDRESS_BOOK_ID).append(",")
					.append(DB_FLD_BUYING_COMPANY_ID).append(",")
					.append(DB_FLD_ENTITY_ID).append(",")
					.append(DB_FLD_SHIP_TO_ID).append(",")
					.append(DB_FLD_SHIP_TO_NAME).append(",")
					.append(DB_FLD_FIRST_NAME).append(",")
					.append(DB_FLD_LAST_NAME).append(",")
					.append(DB_FLD_ADDRESS1).append(",")
					.append(DB_FLD_ADDRESS2).append(",")
					.append(DB_FLD_CITY).append(",")
					.append(DB_FLD_STATE).append(",")
					.append(DB_FLD_COUNTRY).append(",")
					.append(DB_FLD_ZIPCODE).append(",")
					.append(DB_FLD_PHONE).append(",")
					.append(DB_FLD_EMAIL).append(",")
					.append(DB_FLD_ADDRESS_TYPE).append(",")
					.append(DB_FLD_WAREHOUSE_CODE).append(",")
					.append(DB_FLD_STATUS).append(",")
					.append(DB_FLD_DEFAULT_FOR_ALL)
					.append(") VALUES (")
					.append("BC_ADDRESS_BOOK_ID_SEQ.NEXTVAL").append(",");
					
					if(newBuyingCompany) {
						 queryBuilder.append(BuyingCompany.getSubqueryToFindNewlyInsertedBuyingCompany(this));
					} else {
						 queryBuilder.append(this.buyingCompanyId).append(",");
					}
					//queryBuilder.append(this.entityId).append(",");
			
					if(this.entityId!=null && !this.entityId.isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.entityId)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					if(this.shipToId!=null && !this.shipToId.isEmpty() 
							&& this.addressType!=null && this.addressType==AddressType.Ship) {
						queryBuilder.append("'").append(this.shipToId).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					
					queryBuilder.append(",");
					if(this.shipToName!=null  && !this.shipToName.isEmpty()
							&& this.addressType!=null && this.addressType==AddressType.Ship) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.shipToName)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					if(this.firstName!=null && !this.firstName.isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.firstName)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					if(this.lastName!=null && !this.lastName.isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.lastName)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					if(this.address1!=null && !this.address1.isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.address1)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					if(this.address2!=null && !this.address2.isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.address2)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					if(this.city!=null && !this.city.isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.city)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					
					if(this.state!=null && !this.state.isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.state)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					
					if(this.country!=null && !this.country.isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.country)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					
					if(this.zipcode!=null && !this.zipcode.isEmpty()) {
						queryBuilder.append("'").append(this.zipcode).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					
					if(this.phone!=null && !this.phone.isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.phone)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					
					if(this.emailAddress!=null) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.emailAddress)).append("'");
					} else {
						queryBuilder.append(nullString);
					}			
					queryBuilder.append(",");
					
					if(this.addressType!=null) {
						queryBuilder.append("'").append(this.addressType.name()).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",");
					if(this.wareHouseCodeStr!=null && !this.wareHouseCodeStr.isEmpty()) {
						queryBuilder.append("'").append(CimmUtil.replaceSingleQuote(this.wareHouseCodeStr)).append("'");
					} else {
						queryBuilder.append(nullString);
					}
					queryBuilder.append(",'A'");
					queryBuilder.append(",'Y'");
				
					queryBuilder.append(")");
		}catch(Exception e){
			e.printStackTrace();
		}
				
		return queryBuilder.toString();
				
	}
	
	@Override
	public BuyingCompanyAddressBook clone() {
		BuyingCompanyAddressBook buyingCompanyAddressBook = new BuyingCompanyAddressBook();
		try{
			buyingCompanyAddressBook.setFirstName(this.getFirstName());
			buyingCompanyAddressBook.setLastName(this.getLastName());
			buyingCompanyAddressBook.setAddressBookId(this.getAddressBookId());
			buyingCompanyAddressBook.setShipToId(this.getShipToId());
			buyingCompanyAddressBook.setShipToName(this.getShipToName());
			buyingCompanyAddressBook.setAddress1(this.getAddress1());
			buyingCompanyAddressBook.setAddress2(this.getAddress2());
			buyingCompanyAddressBook.setCity(this.getCity());
			buyingCompanyAddressBook.setState(this.getState());
			buyingCompanyAddressBook.setCountry(this.getCountry());
			buyingCompanyAddressBook.setAddressType(this.getAddressType());
			buyingCompanyAddressBook.setBuyingCompanyId(this.getBuyingCompanyId());
			buyingCompanyAddressBook.setDefaultForAll(this.getDefaultForAll());
			buyingCompanyAddressBook.setEntityId(this.getEntityId());
			buyingCompanyAddressBook.setEmailAddress(this.getEmailAddress());
			buyingCompanyAddressBook.setPhone(this.getPhone());
			buyingCompanyAddressBook.setUserShipToId(this.getUserShipToId());
			buyingCompanyAddressBook.setZipcode(this.getZipcode());
			buyingCompanyAddressBook.setSelectedForUpdation(this.isSelectedForUpdation());
			buyingCompanyAddressBook.setCustomerType(this.getCustomerType());
			buyingCompanyAddressBook.tempObjectId = CimmUtil.generateGUID();
		}catch(Exception e){
			e.printStackTrace();
		}
		return buyingCompanyAddressBook;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getWareHouseCodeStr() {
		return wareHouseCodeStr;
	}

	public void setWareHouseCodeStr(String wareHouseCodeStr) {
		this.wareHouseCodeStr = wareHouseCodeStr;
	}
}