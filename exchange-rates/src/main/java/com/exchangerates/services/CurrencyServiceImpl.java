package com.exchangerates.services;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.exchangerates.dao.CurrencyRepo;
import com.exchangerates.model.Currency;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

	
	
	@Override //for date and list of currencies
	public boolean loadData(String date, List<String> currencies) {
		
		String currencyList = String.join(",", currencies);
		
		JsonNode responseNode = restTemplate.getForObject(url + date + "?access_key=" + accessKey + "&symbols="+currencyList, JsonNode.class);
	
		LOGGER.debug(url + date + "?access_key=" + accessKey + "&symbols="+currencyList);
		
		return storeData(date,responseNode);
		
	}
	
	@Override // for particular date currency base
	public boolean loadData(String date, String currency,String baseCurrency) {
		
		JsonNode responseNode = restTemplate.getForObject(url + date + "?access_key=" + accessKey + "&symbols="+currency, JsonNode.class);
		
		return storeData(date,responseNode);
		
	}

	@Override  //whole year
	public boolean loadData(String currency) {
		
		boolean isSuccess = true;
		
		LocalDate docDate = LocalDate.now();
		
		LocalDate lastYearDate = docDate.minusYears(1);
		
		LocalDate firstDayOfMonth = docDate.with(TemporalAdjusters.firstDayOfMonth());
		
		try{
			
			while(firstDayOfMonth.isAfter(lastYearDate)) {
			
			JsonNode responseNode = restTemplate.getForObject(url + firstDayOfMonth.toString() + "?access_key=" + accessKey + "&symbols="+currency, JsonNode.class);
			
			boolean isDataStored = storeData(firstDayOfMonth.toString(),responseNode);
			
			if(!isDataStored){
				
				LOGGER.debug("data not stored for " + currency);
			}
			
			firstDayOfMonth = firstDayOfMonth.minusMonths(1);
			
	}
		}
		
		catch(Exception e){
			
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	
	
	public boolean storeData(String date,JsonNode responseNode){
		
		boolean isSuccess = true;
		
		if(responseNode.has("success") && responseNode.get("success").asBoolean()==true){
			
			String baseCurr = responseNode.get("base").asText();
				
			 JsonNode rateNode = responseNode.get("rates");
			 
			 ObjectMapper mapper = new ObjectMapper();
				
			 Map<String,Double> convertValue = mapper.convertValue(rateNode, Map.class);
			 
			 try{
			 for(Map.Entry<String, Double> entry : convertValue.entrySet()){
				 
				 try{
				 
				 Currency currency = new Currency();
				 currency.setCurrName(entry.getKey());
				 currency.setRate(entry.getValue());
				 currency.setDate(date);
				 currency.setBaseCurr(baseCurr);
					
				 repo.save(currency);
				 }
				 catch(ClassCastException e){
					 LOGGER.debug(e.getMessage());
					
				 }
			 }
			 }
			 catch(Exception e){
				 
				 isSuccess = false;
			 }
		}
		else {
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	public double fetchRate(String date, String curr){
			
		double rate = 0.0;
		
		Currency currency = repo.findByCurrNameAndDate(curr,date);
		
		if(currency!=null)
			rate = currency.getRate();
		
		return rate;
			
		}
	
		
	@SuppressWarnings("deprecation")
	public JsonNode fetchRate(String date){
		
		SimpleDateFormat simpleDate = new SimpleDateFormat("YYYY-MM-dd");
		
		String today = simpleDate.format(new Date());
		
		LOGGER.debug(today);
		
		List<Currency> currencyList = repo.findByDateBetween(date, today);
		
		LOGGER.debug("fetchRate : " + currencyList);
		
		ArrayNode outerNode = new ObjectMapper().createArrayNode();
		

		for (Currency currency : currencyList) {
			
			ObjectNode innerNode = new ObjectMapper().createObjectNode();
			
			innerNode.put("currency", currency.getCurrName());
			innerNode.put("rate", currency.getRate());
			innerNode.put("base", currency.getBaseCurr());
			innerNode.put("date", currency.getDate());
			
//			outerNode.put(currency.getCurrName(), innerNode);
			outerNode.add(innerNode);
		}
		
		return outerNode;
		
	}

}
