package com.exchangerates.controller;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchangerates.constants.Constants;
import com.exchangerates.response.Response;
import com.exchangerates.services.CurrencyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class CurrencyController {

	@Autowired
	CurrencyServiceImpl currencyServiceImpl;
	
	@RequestMapping(value="/loadRates", method=RequestMethod.POST)
	public ResponseEntity<Response> loadData(@RequestParam String date, @RequestParam List<String> currencyList){
		
		ObjectNode objectNode = new ObjectMapper().createObjectNode();
		 
		if(validateDate(date)){
			
			objectNode.put("message", "Date not allowed");
			
			return new ResponseEntity<>(new Response(Constants.INVALID_DATA, objectNode), HttpStatus.OK);
		}
		
		else{
			if(currencyServiceImpl.loadData(date, currencyList)) {
				objectNode.put("message", "Data Loaded");
				return new ResponseEntity<>(new Response(Constants.SUCCESS,objectNode), HttpStatus.OK);
			}
				
				
			else {
				objectNode.put("message", "Data Not Loaded");
				return new ResponseEntity<>(new Response(Constants.FAILURE,objectNode), HttpStatus.OK);
			}
		}
		
	}
	
	
	@RequestMapping(value="/loadRatesForDate", method=RequestMethod.POST)
	public ResponseEntity<Response> loadData(@RequestParam String date, @RequestParam String currency, 
			@RequestParam String baseCurrency){
		
		ObjectNode objectNode = new ObjectMapper().createObjectNode();
		
		if(currencyServiceImpl.loadData(date, currency, baseCurrency)){
			objectNode.put("message", "Data Loaded");
			return new ResponseEntity<>(new Response(Constants.SUCCESS,objectNode), HttpStatus.OK);
	}
		
	else {
		objectNode.put("message", "Data Not Loaded");
		return new ResponseEntity<>(new Response(Constants.FAILURE,objectNode), HttpStatus.OK);
	}
	
	}
	
	@RequestMapping(value="/loadRatesForYear", method=RequestMethod.POST)
	public ResponseEntity<Response> loadDataForWholeYear(@RequestParam String currency){
	
		ObjectNode objectNode = new ObjectMapper().createObjectNode();
		
		if(currencyServiceImpl.loadData(currency)) {
			objectNode.put("message", "Data Loaded");
			return new ResponseEntity<>(new Response(Constants.SUCCESS,objectNode), HttpStatus.OK);
		}
			
		else {
			objectNode.put("message", "Data Not Loaded");
			return new ResponseEntity<>(new Response(Constants.FAILURE,objectNode), HttpStatus.OK);
		}
	
	}
	
	private boolean validateDate(String date){
	
		LocalDate currDocDate = LocalDate.now();
		
		LocalDate userLocalDate = LocalDate.parse(date);
		
		LocalDate dateToBeCheckWith = currDocDate.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(12);
		
		if(userLocalDate.isBefore(dateToBeCheckWith) || userLocalDate.isAfter(currDocDate)) 
			return true;
		
		else return false;
			
	}
	
	//User Story 2
	
	@RequestMapping(value="/getRate", method=RequestMethod.GET)
	public ResponseEntity<Response> getRate(@RequestParam String date, @RequestParam String currency){
		
		 double rate = currencyServiceImpl.fetchRate(date, currency);
		 
		 ObjectNode objectNode = new ObjectMapper().createObjectNode();
		 
		 objectNode.put("rate", rate);
		 
		 if(rate == 0.0)
			 return new ResponseEntity<>(new Response(Constants.RATE_NOT_FOUND, objectNode),HttpStatus.OK);
		 else
			 return new ResponseEntity<>(new Response(Constants.SUCCESS, objectNode),HttpStatus.OK);
			 
	}
	
	@RequestMapping(value="/getRates", method=RequestMethod.GET)
	public ResponseEntity<Response> getRatesForDateRange(@RequestParam String date){
		
		 ObjectNode objectNode = currencyServiceImpl.fetchRate(date);
		 
		return new ResponseEntity<>(new Response(Constants.SUCCESS, objectNode),HttpStatus.OK);
			 
	}
	
}
