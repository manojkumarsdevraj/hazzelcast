package com.unilog.users;

import java.util.ArrayList;

public class Country {

	/**
	 * 
	 */
	
	
	private int countryId;
	private String countryCode;
	private String countryName;
	
	private ArrayList<State> stateList;
	
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		if(countryCode != null){
			this.countryCode = countryCode.trim();
		}else{
			this.countryCode = null;
		}
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		if(countryName != null){
			this.countryName = countryName.trim();
		}else{
			this.countryName = null;
		}
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setStateList(ArrayList<State> stateList) {
		this.stateList = stateList;
	}
	public ArrayList<State> getStateList() {
		if(stateList == null){
			stateList = new ArrayList<State>();
		}
		return stateList;
	}
	
	public boolean removeSate(long stateId){
		try{
			ArrayList<State> tempList = new ArrayList<State>();
			tempList.addAll(getStateList());
			for(int i=0;i<tempList.size();i++){
				if(stateId==tempList.get(i).getStateId()){
					return this.getStateList().remove(i)!= null;
				}
			}
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
