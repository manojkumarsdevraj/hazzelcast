package com.unilog.ecommerce.user.validation;

public class RegValidatorFactory {
	private static RegValidatorFactory regValidatorFactory = null;
	
	private RegValidatorFactory() {}
	public static RegValidatorFactory getInstance() {
			synchronized (RegValidatorFactory.class) {
				if(regValidatorFactory == null) {
					regValidatorFactory = new RegValidatorFactory();
				}
			}
		return regValidatorFactory;
	}
	
	public static RegistrationValidator getValidator(String type) {
		RegRequestType regType = RegRequestType.valueOf(type.toUpperCase());
		RegistrationValidator validator = null;
		switch(regType) {
			case PURVIS :
				validator = PurvisRegValidatorImpl.getInstance();
					break;
			case GENERIC :
			default:
				validator = GenericRegValidator.getInstance();
				break;
		}
		return validator;
	}
}
