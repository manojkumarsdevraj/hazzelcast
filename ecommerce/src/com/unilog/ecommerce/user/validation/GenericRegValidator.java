package com.unilog.ecommerce.user.validation;

import java.util.List;

import org.apache.log4j.Logger;

import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.ecommerce.user.dao.UserRepositoryFactory;
import com.unilog.ecommerce.user.model.Address;
import com.unilog.ecommerce.user.model.User;
import com.unilog.ecommerce.validataion.FieldValidator;
import com.unilog.ecommerce.validataion.ValidationStatus;

public class GenericRegValidator implements RegistrationValidator {
	private static final Logger logger = Logger.getLogger(GenericRegValidator.class);
	protected static RegistrationValidator genericValidator;
	
	public GenericRegValidator() {}
	
	public static RegistrationValidator getInstance() {
			synchronized (GenericRegValidator.class) {
				if(genericValidator == null) {
					genericValidator = new GenericRegValidator();
				}
			}
		return genericValidator;
	}
	@Override
	public ValidationStatus validateB2bOnAccount(User user, String locale) {
		ValidationStatus status = new ValidationStatus();
		List<String> descriptions = status.getDescriptions();
		boolean isValid = true;
		
		try {
			if(!FieldValidator.isValidString(user.getAccountNumber())) {
				descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.accountNumber.notEmpty"));
				isValid = false;
			}	
			if(!FieldValidator.isValidString(user.getAccountName())) {
				descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.accountName.notEmpty"));
				isValid = false;
			}
			status.setValid(isValid);
			status.setDescriptions(descriptions);
			
			status = validateBasicUserInfo(user, locale, status);
			if(status.isValid()) {
				user.setEmailId(user.getAddress().getEmailId());
				status = doesUserExists(user, locale, status);
			}
		}catch (Exception e) {
			status.setValid(false);
			logger.error("Validation Exception: B2B OnAccount ", e);
		}
		return status;
	}

	@Override
	public ValidationStatus validateB2bNewAccount(User user, String locale) {
		ValidationStatus status = new ValidationStatus();
		status.setValid(true);
		try {
			status = validateBasicUserInfo(user, locale, status);
			if(status.isValid()) {
				user.setEmailId(user.getAddress().getEmailId());
				status = doesUserExists(user, locale, status);
			}
		}catch (Exception e) {
			status.setValid(false);
			logger.error("Validation Exception: B2B NewAccount ", e);
		}
		return status;
	}

	@Override
	public ValidationStatus validateB2cRetail(User user, String locale) {
		ValidationStatus status = new ValidationStatus();
		status.setValid(true);
		try {
			status = validateBasicUserInfo(user, locale, status);
			if(status.isValid()) {
				user.setEmailId(user.getAddress().getEmailId());
				status = doesUserExists(user, locale, status);
			}
		}catch (Exception e) {
			status.setValid(false);
			logger.error("Validation Exception: B2C Retail ", e);
		}
		return status;
	}

	@Override
	public ValidationStatus validateB2bCreditRequest(User user, String locale) {
		return new ValidationStatus();
	}
	
	protected ValidationStatus doesUserExists(User user, String locale, ValidationStatus status) {
		String userRepoType = CommonDBQuery.getSystemParamtersList().get("USER_REG_REPO_TYPE");
		
		if(UserRepositoryFactory.getUserRepository(userRepoType).doesUserExists(user.getEmailId())) {
			status.setValid(false);
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.exists"));
		}
		return status;
	}
	
	protected ValidationStatus validateBasicUserInfo(User user, String locale, ValidationStatus status) {
		boolean isValid = status.isValid();
		List<String> descriptions = status.getDescriptions();
		
		if(!FieldValidator.isValidString(user.getFirstName())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.firstName.notEmpty"));
			isValid = false;
		}
		else if(!FieldValidator.isValidName(user.getFirstName())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.firstName.invalid"));
			isValid = false;
		}
		
		if(!FieldValidator.isValidString(user.getLastName())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.lastName.notEmpty"));
			isValid = false;
		}
		else if(!FieldValidator.isValidName(user.getLastName())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.lastName.invalid"));
			isValid = false;
		}
		
		if(!FieldValidator.isValidPassword(user.getPassword())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.password.invalid"));
			isValid = false;
		}
		else if(!user.getPassword().equals(user.getConfirmPassword())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.password.mismatch"));
			isValid = false;
		}
		status.setValid(isValid);
		status.setDescriptions(descriptions);
		status = validateAddressInfo(user.getAddress(), locale, status);
		return status;
	}
	
	protected ValidationStatus validateAddressInfo(Address address, String locale, ValidationStatus status) {
		boolean isValid = status.isValid();
		List<String> descriptions = status.getDescriptions();
		
		if(!FieldValidator.isValidString(address.getAddress1())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.address1.notEmpty"));
			isValid = false;
		}
		
		if(!FieldValidator.isValidString(address.getCity())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.city.notEmpty"));
			isValid = false;
		}
		
		if(!FieldValidator.isValidString(address.getCountry())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.country.notEmpty"));
			isValid = false;
		}
		
		if(!FieldValidator.isValidString(address.getState())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.state.notEmpty"));
			isValid = false;
		}
		
		if(!FieldValidator.isValidString(address.getZipCode())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.zipCode.notEmpty"));
			isValid = false;
		}
		else if(!FieldValidator.isValidZipCode(address.getZipCode())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.zipCode.invalid"));
			isValid = false;
		}
		if(!FieldValidator.isValidString(address.getPhoneNumber())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.phoneNumber.notEmpty"));
			isValid = false;
		}
		else if(!FieldValidator.isValidPhoneNumber(address.getPhoneNumber())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.phoneNumber.invalid"));
			isValid = false;
		}
		
		if(!FieldValidator.isValidString(address.getEmailId())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.emailId.notEmpty"));
			isValid = false;
		}
		else if(!FieldValidator.isValidEmail(address.getEmailId())) {
			descriptions.add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.emailId.invalid"));
			isValid = false;
		}
		status.setValid(isValid);
		status.setDescriptions(descriptions);
		return status;
	}
}
