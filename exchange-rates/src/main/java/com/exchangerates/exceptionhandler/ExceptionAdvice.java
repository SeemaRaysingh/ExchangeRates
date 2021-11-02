package com.exchangerates.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.exchangerates.response.BaseResponse;

@ControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(ExchangeRateNotFoundException.class)
	public ResponseEntity<BaseResponse> handleDataNotFoundException(ExchangeRateNotFoundException dataNotFoundException){
		
		String errorMessage = dataNotFoundException.getMessage();
		
		BaseResponse response = new BaseResponse(false, errorMessage, HttpStatus.NOT_FOUND.value());
				
		return new ResponseEntity<BaseResponse>(response, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<BaseResponse> handleHttpClientErrorException(){
		
		BaseResponse response = new BaseResponse(false, "Rates not available", HttpStatus.NOT_FOUND.value());
				
		return new ResponseEntity<BaseResponse>(response, HttpStatus.NOT_FOUND);
		
		}
	
	@ExceptionHandler(BadRequest.class)
	public ResponseEntity<BaseResponse> handleBadRequest(){
		
		BaseResponse response = new BaseResponse(false, "Rates not available", HttpStatus.BAD_REQUEST.value());
				
		return new ResponseEntity<BaseResponse>(response, HttpStatus.BAD_REQUEST);
		
		}
}
