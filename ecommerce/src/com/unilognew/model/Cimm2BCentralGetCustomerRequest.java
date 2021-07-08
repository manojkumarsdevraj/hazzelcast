package com.unilognew.model;

public class Cimm2BCentralGetCustomerRequest implements ERPServiceRequest{

	private String customerNumber;
	private int buyingCompanyId;
	private int userId;

	/**
	 * @return the customerNumber
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * @param customerNumber the customerNumber to set
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
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
}
