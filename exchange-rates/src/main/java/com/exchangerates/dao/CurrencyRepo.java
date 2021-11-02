package com.exchangerates.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchangerates.entity.Currency;

@Repository
public interface CurrencyRepo extends JpaRepository<Currency, String> {

	public Currency findByCurrencyAndDocDate(String currName, String date);
	
	public List<Currency> findByDocDateBetween(String startDate, String endDate);
	
}
