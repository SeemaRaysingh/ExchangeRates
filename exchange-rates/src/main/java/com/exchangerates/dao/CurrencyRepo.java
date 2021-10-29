package com.exchangerates.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.exchangerates.model.Currency;

public interface CurrencyRepo extends JpaRepository<Currency, String> {

	public Currency findByCurrNameAndDate(String currName, String date);
	
	public List<Currency> findByDateBetween(String startDate, String endDate);
}
