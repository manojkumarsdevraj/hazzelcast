package com.unilog.ecommerce.user.service;

import javax.servlet.http.HttpServletRequest;

import com.unilog.ecommerce.user.model.User;

public interface UserService {
	boolean b2bOnAccountService(User user);
	boolean b2bNewAccountService(User user);
	boolean b2cRetailService(User user);
	boolean b2bCreditRequest();
	default void customerImport(HttpServletRequest request) {}
}
