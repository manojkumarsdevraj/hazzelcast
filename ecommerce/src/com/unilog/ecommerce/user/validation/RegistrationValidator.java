package com.unilog.ecommerce.user.validation;

import com.unilog.ecommerce.user.model.User;
import com.unilog.ecommerce.validataion.ValidationStatus;

public interface RegistrationValidator {
	ValidationStatus validateB2bOnAccount(User user, String locale);
	ValidationStatus validateB2bNewAccount(User user, String locale);
	ValidationStatus validateB2cRetail(User user, String locale);
	ValidationStatus validateB2bCreditRequest(User user, String locale);
}
