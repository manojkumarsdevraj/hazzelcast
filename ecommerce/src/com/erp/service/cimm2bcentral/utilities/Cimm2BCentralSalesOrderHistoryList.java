package com.erp.service.cimm2bcentral.utilities;

import java.util.List;

public class Cimm2BCentralSalesOrderHistoryList extends Cimm2BCentralResponseEntity{

	private List<Cimm2BCentralSalesOrderHistory> orderHistoryList;

	public List<Cimm2BCentralSalesOrderHistory> getOrderHistoryList() {
		return orderHistoryList;
	}

	public void setOrderHistoryList(List<Cimm2BCentralSalesOrderHistory> orderHistoryList) {
		this.orderHistoryList = orderHistoryList;
	}
}
