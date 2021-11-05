package com.exchangerates.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchangerates.constants.Constants;
import com.exchangerates.dao.CurrencyRepo;
import com.exchangerates.entity.Currency;
import com.exchangerates.response.ApiResponse;
import com.exchangerates.response.BaseResponse;
import com.exchangerates.services.CurrencyService;
import com.exchangerates.utility.Utility;

@RestController
public class CurrencyController {

	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private CurrencyRepo currencyRepo;
	
	/**
	 * This method calls the service layer to load the exchange rates in database for given date and currencies
	 * 
	 * @param date
	 * @param currencyList
	 * @return
	 */
	@RequestMapping(value="/Rates_By_Date_And_Currencies", method=RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> loadExchangeRates(@RequestParam (value ="date")String date,
					@RequestParam (value ="currencyList") List<String> currencyList){
		
		String status = Utility.validateDate(date);
	
		if(status.equals(Constants.SUCCESS)){
			
			currencyService.loadData(date, currencyList);
			
			return new ResponseEntity<>(new BaseResponse(true, Constants.RATE_LOADED, HttpStatus.OK.value()), HttpStatus.OK);
			
		}else {
			return new ResponseEntity<>(new BaseResponse(false, status , HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
		}
	}
		
	/**
	 * This method class the service layer to load the exchange rates in database for given date and currency and base
	 * 
	 * @param date
	 * @param currency
	 * @param base
	 * @return
	 */
	@RequestMapping(value="/Rate", method=RequestMethod.GET)
	public ResponseEntity<BaseResponse> loadExchangeRates(@RequestParam String date, @RequestParam String currency, 
			@RequestParam String base){
		
		Optional<LocalDate> validate = Optional.ofNullable(Utility.validateDateFormat(date));
		
		 String status = validate.isPresent() ? Constants.SUCCESS : Constants.INVALID_DATE_FORMAT;
	
		if(status.equals(Constants.SUCCESS)){
			
			currencyService.loadData(date, currency, base);
			
			return new ResponseEntity<>(new BaseResponse(true, Constants.RATE_LOADED, HttpStatus.OK.value()), HttpStatus.OK);
			
		}else {
			return new ResponseEntity<>(new BaseResponse(false, status , HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * This method class the service layer to load the exchange rates for given currency for whole year
	 * @param currency
	 * @return
	 */
	@RequestMapping(value="/Rates_For_Year/{currency}", method=RequestMethod.GET)
	public ResponseEntity<BaseResponse> loadExchangeRates(@PathVariable("currency") String currency){
	
		currencyService.loadData(currency);
		
		return new ResponseEntity<>(new BaseResponse(true, Constants.RATE_LOADED + " for " + currency
				, HttpStatus.OK.value()), HttpStatus.OK);
	}
	
	
	//User Story 2
	
	/**
	 * This method returns the rate for requested date and currency
	 * 
	 * @param date
	 * @param currency
	 * @return
	 */
	@RequestMapping(value="/Rate_By_Date_And_Currency", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> getRate(@RequestParam(value ="date")String date, 
			@RequestParam(value="currency") String currency){
		
		Optional<LocalDate> validate = Optional.ofNullable(Utility.validateDateFormat(date));
		
		if(validate.isPresent()){
			
			Currency currencyObj = currencyRepo.findByCurrencyAndDocDate(currency,date);
			
			if(currencyObj != null) {

				return new ResponseEntity<BaseResponse>(new ApiResponse	(true,"Rate is available"
						, HttpStatus.OK.value(), currencyObj.getRate()),HttpStatus.OK);
			}

			else {
				return new ResponseEntity<BaseResponse>(new BaseResponse(false,"Rate is not available for "
						+ currency + " for date : "+ date, HttpStatus.NOT_FOUND.value()),
						HttpStatus.NOT_FOUND);
			}
		}	else {
			return new ResponseEntity<>(new BaseResponse(false, Constants.INVALID_DATE_FORMAT , HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	/**
	 * This method returns the list of exchange rates from a given date to today
	 * @param date
	 * @return
	 */
	@RequestMapping(value="/Rate_By_Date",method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseResponse> getRatesForDateRange(@RequestParam("date") String date){
		
		 Optional<LocalDate> validate = Optional.ofNullable(Utility.validateDateFormat(date));
				
		if(validate.isPresent()){
			List<Currency> listOfCurrency  = currencyService.fetchRate(date);

			if(!listOfCurrency.isEmpty()){

				return new ResponseEntity<>(new ApiResponse(true, Constants.RATE_AVAILABLE , HttpStatus.OK.value(), listOfCurrency),HttpStatus.OK);
			}
			else 
				return new ResponseEntity<BaseResponse>(new BaseResponse(false, Constants.RATE_NOT_AVAILABLE, HttpStatus.NOT_FOUND.value()),
						HttpStatus.NOT_FOUND);
				}
		else {
			return new ResponseEntity<>(new BaseResponse(false, Constants.INVALID_DATE_FORMAT , HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
		}
	}
	
}