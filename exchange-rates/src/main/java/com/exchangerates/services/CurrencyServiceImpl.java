package com.exchangerates.services;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.exchangerates.dao.CurrencyRepo;
import com.exchangerates.entity.Currency;
import com.exchangerates.exceptionhandler.ExchangeRateNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class provides implementation for CurrencySservice interface
 *
 */
@Service
public class CurrencyServiceImpl implements CurrencyService{

	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	CurrencyRepo repo;
	
	@Value("${exchange.rates.api.url}")
	private String url;
	
	@Value("${exchange.rates.api.access_key}")
	private String accessKey;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyServiceImpl.class);

	
	public CurrencyServiceImpl() {
		super();
	}
	
	public CurrencyServiceImpl(CurrencyRepo repo) {
		this.repo = repo;
	}

	public CurrencyServiceImpl(RestTemplate restTemplate, CurrencyRepo repo) {
		this.restTemplate = restTemplate;
		this.repo = repo;
	}

	/**
	 * This method calls the external exchange rates API for given date and currencies
	 * 
	 * @param date
	 * @param currencies
	 */
	@Override 
	public void loadData(String date, List<String> currencies) {
		
		
		String currencyList = String.join(",", currencies);
		
		JsonNode responseNode = restTemplate.getForObject(url + date + "?access_key=" + accessKey + "&symbols="+currencyList, JsonNode.class);
	
		LOGGER.debug(url + date + "?access_key=" + accessKey + "&symbols="+currencyList);
		
		LOGGER.debug("Response : " +responseNode);
		
		if(!responseNode.has("success")){
			
			throw new ExchangeRateNotFoundException("Data Not Loaded for " + currencies );
			
		}
		
		 storeData(date,responseNode);
		
	}
	
	/**
	 * This method calls external exchange API for given date, currency and base
	 * 
	 * @param date
	 * @param currency
	 * @param baseCurrency
	 */
	@Override
	public void loadData(String date, String currency,String baseCurrency) {
		
		JsonNode responseNode = restTemplate.getForObject(url + date + "?access_key=" + accessKey + "&base="+baseCurrency+ "&symbols="+currency, JsonNode.class);
		
		LOGGER.debug(url + date + "?access_key=" + accessKey + "&base=" + baseCurrency + "&symbols=" +currency);

		if(!responseNode.has("success")){
			
			throw new ExchangeRateNotFoundException("Data Not Loaded for " + currency );
			
		}
		 storeData(date,responseNode);
	}
	
	
	/**
	 * This method calculates the first date of month for each month for whole year
	 * and calls exchange rate API for each date for the given currency
	 * 
	 * @param currency
	 */
	@Override
	public void loadData(String currency) {
		
		LocalDate docDate = LocalDate.now();
		
		LocalDate lastYearDate = docDate.minusYears(1);
		
		LocalDate firstDayOfMonth = docDate.with(TemporalAdjusters.firstDayOfMonth());
		
		while(firstDayOfMonth.isAfter(lastYearDate)) {
		
		JsonNode responseNode = restTemplate.getForObject(url + firstDayOfMonth.toString() + "?access_key=" + accessKey + "&symbols="+currency, JsonNode.class);
		
		LOGGER.debug(url + firstDayOfMonth.toString() + "?access_key=" + accessKey + "&symbols="+currency);
		
		if(!responseNode.has("success")){
				
				throw new ExchangeRateNotFoundException("Data Not Loaded for " + currency );
				
		}
		storeData(firstDayOfMonth.toString(),responseNode);
		
		firstDayOfMonth = firstDayOfMonth.minusMonths(1);
		
		}
	}
	
	
	/**
	 * This method stores the data in database received from exchange rates API 
	 * 
	 * @param date
	 * @param responseNode
	 */
	public void storeData(String date,JsonNode responseNode){
		
		 try{
			 
			String baseCurr = responseNode.get("base").asText();
				
			 JsonNode rateNode = responseNode.get("rates");
			 
			 ObjectMapper mapper = new ObjectMapper();
				
			 Map<String,Double> convertValue = mapper.convertValue(rateNode, Map.class);
			 
				 
			 for(Map.Entry<String, Double> entry : convertValue.entrySet()){
				 
				 Currency currencyObj = repo.findByCurrencyAndDocDate(entry.getKey(), date);
				 if(currencyObj != null) {
					  //assuming rate will not change for particular date
					
					 continue;
				 }
				 Currency currency = new Currency();
				 
				 currency.setCurrency(entry.getKey());
				 currency.setRate(entry.getValue());
				 currency.setDocDate(date);
				 currency.setBase(baseCurr);
				 repo.save(currency);
				 
			 	}
			 
			 }
			 catch(Exception e){
				 e.printStackTrace();
				 throw new ExchangeRateNotFoundException("Data Not Loaded");
			 }
	}
	
	 /**
	 * This method return the list of exchange rates which is selected from database from given date to today
	 * 
	 * @param date
	 * 
	 */
		public List<Currency> fetchRate(String date){
			
			String today =  LocalDate.now().toString();
			
			return repo.findByDocDateBetween(date, today);
		}

}
