package com.unilog.ecomm.promotion.exception;

public class SalesPromotionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String message = "Exception occured while caluclating the discounts";
	private Throwable originalException;
	
	public SalesPromotionException(String message, Throwable originalException) {
		super(message,originalException);
		this.message = message;
		this.originalException = originalException;
	}

	public SalesPromotionException(String message) {
		super(message);
		this.message = message;
	}

	public SalesPromotionException() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public Throwable getOriginalException() {
		return originalException;
	}
	
	
}
