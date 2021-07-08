package com.unilog.ecommerce.user.validation;

import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.ecommerce.user.model.User;
import com.unilog.ecommerce.validataion.FieldValidator;
import com.unilog.ecommerce.validataion.ValidationStatus;

public class PurvisRegValidatorImpl extends GenericRegValidator{
	
	public static RegistrationValidator getInstance() {
		if(genericValidator == null) {
			synchronized (PurvisRegValidatorImpl.class) {
				if(genericValidator == null) {
					genericValidator = new PurvisRegValidatorImpl();
				}
			}
		}
		return genericValidator;
	}
	
	@Override
	public ValidationStatus validateB2bNewAccount(User user, String locale) {
		ValidationStatus status = new ValidationStatus();
		boolean isValid = true;
		if(!FieldValidator.isValidString(user.getFirstName())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.firstName.notEmpty"));
			isValid = false;
		}
		else if(!FieldValidator.isValidName(user.getFirstName())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.firstName.invalid"));
			isValid = false;
		}
		
		if(!FieldValidator.isValidString(user.getLastName())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.lastName.notEmpty"));
			isValid = false;
		}
		else if(!FieldValidator.isValidName(user.getLastName())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.lastName.invalid"));
			isValid = false;
		}
		
		if(!FieldValidator.isValidString(user.getAccountName())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.accountName.notEmpty"));
			isValid = false;
		}
		if(!FieldValidator.isValidString(user.getPhoneNumber())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.phoneNumber.notEmpty"));
			isValid = false;
		}
		else if(!FieldValidator.isValidPhoneNumber(user.getPhoneNumber())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.phoneNumber.invalid"));
			isValid = false;
		}
		
		if(!FieldValidator.isValidString(user.getAddress().getZipCode())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.zipCode.notEmpty"));
			isValid = false;
		}
		else if(!FieldValidator.isValidZipCode(user.getAddress().getZipCode())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.zipCode.invalid"));
			isValid = false;
		}
		if(!FieldValidator.isValidString(user.getEmailId())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.emailId.notEmpty"));
			isValid = false;
		}
		else if(!FieldValidator.isValidEmail(user.getEmailId())) {
			status.getDescriptions().add(LayoutLoader.getMessageProperties().get(locale).getProperty("form.user.address.emailId.invalid"));
			isValid = false;
		}
		status.setValid(isValid);
		return status;
	}
	
}
