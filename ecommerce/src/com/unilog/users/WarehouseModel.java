package com.unilog.users;

public class WarehouseModel implements Comparable<WarehouseModel> {
	
	private int wareHouseId;
	private String wareHouseCode;
	private String wareHouseName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String longitude;
	private String latitude;
	private double distance;
	private String note;
	private String workHours;
	private String emailAddress;
	private String phone;
	private String fax;
	private String areaCode;
	private String serviceManager;
	private int subsetId;
	
	
	public int getSubsetId() {
		return subsetId;
	}
	public void setSubsetId(int subsetId) {
		this.subsetId = subsetId;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getServiceManager() {
		return serviceManager;
	}
	public void setServiceManager(String serviceManager) {
		this.serviceManager = serviceManager;
	}
	public int getWareHouseId() {
		return wareHouseId;
	}
	public void setWareHouseId(int wareHouseId) {
		this.wareHouseId = wareHouseId;
	}
	public String getWareHouseCode() {
		return wareHouseCode;
	}
	public void setWareHouseCode(String wareHouseCode) {
		this.wareHouseCode = wareHouseCode;
	}
	public String getWareHouseName() {
		return wareHouseName;
	}
	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getWorkHours() {
		return workHours;
	}
	public void setWorkHours(String workHours) {
		this.workHours = workHours;
	}
	
	
	
	@Override
	public int compareTo(WarehouseModel wareHouseModel) {
		//double compareDistance=((WarehouseModel)wareHouseModel).getDistance();
		//return this.distance-compareDistance;
        /* For Ascending order*/
		int result = 0;
		try{
			result = new Double(this.distance).compareTo( wareHouseModel.getDistance());
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return result;
	}

}
