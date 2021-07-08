package com.unilog.users;

public class State {

	/**
	 * 
	 */
	
	
	
	private long stateId;
	private String stateName;
	private String stateCode;
	private String countryCode;
	public long getStateId() {
		return stateId;
	}
	public void setStateId(long stateId) {
		this.stateId = stateId;
	}
	public String getStateName() {
		return stateName;
	}
	/*public void setStateName(String stateName) {
		this.stateName = stateName;
	}*/
	
	public void setStateName(String stateName) {
		if(stateName!=null)
		{
			 stateName = stateName.substring(0,1).toUpperCase() + stateName.substring(1);
		}this.stateName = stateName;
		
	}
	
	
	
	
	
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		if(stateCode!=null)
		{
			stateCode=stateCode.toUpperCase();
		}this.stateCode = stateCode;
		//this.stateCode = stateCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryCode() {
		return countryCode;
	}
}
