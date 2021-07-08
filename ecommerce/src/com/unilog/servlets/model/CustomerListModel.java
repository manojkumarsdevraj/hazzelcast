package com.unilog.servlets.model;

import java.util.List;

public class CustomerListModel {
private List<CustomerModel> customerList;

public void setCustomerList(List<CustomerModel> customerList) {
	this.customerList = customerList;
}

public List<CustomerModel> getCustomerList() {
	return customerList;
}
}
