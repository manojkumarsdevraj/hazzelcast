package com.unilog.ecommerce.user.external;

import com.unilog.ecommerce.user.model.ExternalToken;
import com.unilog.ecommerce.user.model.User;

public interface ExternalUserService {
	ExternalToken authenticate(User user);
	String createUser(User user);
	String updateUser(User user);
}
