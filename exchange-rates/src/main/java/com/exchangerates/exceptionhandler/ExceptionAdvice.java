package com.exchangerates.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import com.exchangerates.response.BaseResponse;

/**
 * 
 * This class handles the global exceptions
 *
 */
@ControllerAdvice
public class ExceptionAdvice {

	/**
	 * This method handles ExchangeRateNotFoundException
	 * 
	 * @param dataNotFoundException
	 * @return the responseEntity
	 */
	@ExceptionHandler(ExchangeRateNotFoundException.class)
	public ResponseEntity<BaseResponse> handleDataNotFoundException(ExchangeRateNotFoundException dataNotFoundException){
		
		String errorMessage = dataNotFoundException.getMessage();
		
		BaseResponse response = new BaseResponse(false, errorMessage, HttpStatus.NOT_FOUND.value());
				
		return new ResponseEntity<BaseResponse>(response, HttpStatus.NOT_FOUND);
		
	}
	
	/**
	 * This method handles the HttpClientErrorException
	 * 
	 * @return ResponseEntity
	 */
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<BaseResponse> handleHttpClientErrorException(){
		
		BaseResponse response = new BaseResponse(false, "Rates not available", HttpStatus.NOT_FOUND.value());
				
		return new ResponseEntity<BaseResponse>(response, HttpStatus.NOT_FOUND);
		
		}
}
