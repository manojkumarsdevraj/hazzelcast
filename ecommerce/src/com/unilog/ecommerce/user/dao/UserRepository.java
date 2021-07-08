package com.unilog.ecommerce.user.dao;

import java.sql.Connection;

import com.unilog.ecommerce.user.model.User;

public interface UserRepository {
	int addUser(Connection connection, User user);
	boolean doesUserExists(String emailAddress);
	User setUserBillShipAddressId(User user, int customerId);
}
