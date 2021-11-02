package com.exchangerates.exceptionhandler;

public class ExchangeRateNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 5031822050943878379L;
	
	String errorMessage;

	/**
	 * 
	 * @param errorMessage
	 */
	public ExchangeRateNotFoundException(String errorMessage) {
		super(errorMessage);
	}
	
}
