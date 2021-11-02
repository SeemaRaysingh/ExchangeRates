package com.exchangerates.services;

import java.util.List;

import com.exchangerates.entity.Currency;

public interface CurrencyService {

	public void loadData(String date, List<String> currencies);
	
	public void loadData(String date,String currency, String baseCurrency);
	
	public void loadData(String date);
	
	public List<Currency> fetchRate(String date);
}
