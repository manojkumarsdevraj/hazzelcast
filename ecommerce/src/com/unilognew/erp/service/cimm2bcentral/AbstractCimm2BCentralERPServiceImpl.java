package com.unilognew.erp.service.cimm2bcentral;

import com.unilognew.erp.service.IERPService;

public abstract class AbstractCimm2BCentralERPServiceImpl implements IERPService {
	
	private boolean initRequired;
	protected String sxAppServerConnection;
	protected String userName;
	protected String password;

	public AbstractCimm2BCentralERPServiceImpl() {
		initRequired=true;
	}
	
	public void init() {
		// initRequired will be set to true whenever system parameters are reloaded
		if(initRequired) {/*
			LinkedHashMap<String,String> systemParametersList = CommonDBQuery.getSystemParamtersList();
			sxAppServerConnection = systemParametersList.get("SX_CONNECTSTRING");
			System.out.println("ERP Connect String :"+sxAppServerConnection);
			userName = systemParametersList.get("SX_LOGIN_USERNAME");
			password = CommonUtility.validateString(systemParametersList.get("SX_LOGIN_PASSWORD"));
			initRequired=false;
		*/}
	}

	/**
	 * @return the initRequired
	 */
	public boolean isInitRequired() {
		return initRequired;
	}

	/**
	 * @param initRequired the initRequired to set
	 */
	public void setInitRequired(boolean initRequired) {
		this.initRequired = initRequired;
	}

	/**
	 * @return the sxAppServerConnection
	 */
	public String getSxAppServerConnection() {
		return sxAppServerConnection;
	}

	/**
	 * @param sxAppServerConnection the sxAppServerConnection to set
	 */
	public void setSxAppServerConnection(String sxAppServerConnection) {
		this.sxAppServerConnection = sxAppServerConnection;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
