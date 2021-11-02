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

	@Override //for date and list of currencies
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
	
	@Override // for particular date currency base
	public void loadData(String date, String currency,String baseCurrency) {
		
		JsonNode responseNode = restTemplate.getForObject(url + date + "?access_key=" + accessKey + "&base="+baseCurrency+ "&symbols="+currency, JsonNode.class);
		
		LOGGER.debug(url + date + "?access_key=" + accessKey + "&base=" + baseCurrency + "&symbols=" +currency);

		if(!responseNode.has("success")){
			
			throw new ExchangeRateNotFoundException("Data Not Loaded for " + currency );
			
		}
		 storeData(date,responseNode);
	}

	@Override  //whole year
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
	
	
	public List<Currency> fetchRate(String date){
		
		String today =  LocalDate.now().toString();
		
		return repo.findByDocDateBetween(date, today);
	}

}
