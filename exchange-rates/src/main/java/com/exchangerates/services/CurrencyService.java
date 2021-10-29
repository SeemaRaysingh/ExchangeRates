package com.exchangerates.services;

import java.util.List;

import com.exchangerates.model.Currency;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface CurrencyService {

	public boolean loadData(String date, List<String> currencies);
	
	public boolean loadData(String date,String currency, String baseCurrency);
	
	public boolean loadData(String date);
	
	public double fetchRate(String date, String curr);
	
	public JsonNode fetchRate(String date);
}
