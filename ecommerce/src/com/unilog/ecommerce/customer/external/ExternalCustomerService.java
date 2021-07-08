package com.unilog.ecommerce.customer.external;

import com.unilog.ecommerce.customer.model.Customer;
import com.unilog.ecommerce.user.model.User;

public interface ExternalCustomerService {
	Customer getCustomer(String accountNumber);
	Customer createCustomer(Customer customer);
	Customer updateCustomer(Customer customer);
	int addShipAddress(User user);
}
